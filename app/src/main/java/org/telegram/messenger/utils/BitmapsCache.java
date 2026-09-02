package org.telegram.messenger.utils;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Build;
import j$.util.Comparator;
import j$.util.concurrent.ConcurrentHashMap;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.nio.ByteBuffer;
import java.util.ArrayList;
import java.util.Collections;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.function.ToIntFunction;
import org.telegram.messenger.AndroidUtilities;
import org.telegram.messenger.BuildVars;
import org.telegram.messenger.DispatchQueuePoolBackground;
import org.telegram.messenger.FileLoader;
import org.telegram.messenger.FileLog;
import org.telegram.messenger.SharedConfig;
import org.telegram.messenger.Utilities;
import org.telegram.ui.Components.RLottieDrawable;

public class BitmapsCache {
    private static ThreadPoolExecutor bitmapCompressExecutor;
    static volatile boolean cleanupScheduled;
    private static boolean mkdir;
    private static CacheGeneratorSharedTools sharedTools;
    private static int taskCounter;
    byte[] bufferTmp;
    volatile boolean cacheCreated;
    RandomAccessFile cachedFile;
    public volatile boolean checked;
    int compressQuality;
    boolean error;
    final File file;
    volatile boolean fileExist;
    String fileName;
    private int frameIndex;
    int h;
    BitmapFactory.Options options;
    volatile boolean recycled;
    private final Cacheable source;
    private int tryCount;
    final boolean useSharedBuffers;
    int w;
    static final ConcurrentHashMap sharedBuffers = new ConcurrentHashMap();
    private static final int N = Utilities.clamp(Runtime.getRuntime().availableProcessors() - 2, 6, 1);
    public final AtomicInteger framesProcessed = new AtomicInteger(0);
    ArrayList frameOffsets = new ArrayList();
    private final Object mutex = new Object();
    public AtomicBoolean cancelled = new AtomicBoolean(false);
    private Runnable cleanupSharedBuffers = new Runnable() { // from class: org.telegram.messenger.utils.BitmapsCache.1
        @Override // java.lang.Runnable
        public void run() {
            for (Thread thread : BitmapsCache.sharedBuffers.keySet()) {
                if (!thread.isAlive()) {
                    BitmapsCache.sharedBuffers.remove(thread);
                }
            }
            if (!BitmapsCache.sharedBuffers.isEmpty()) {
                AndroidUtilities.runOnUIThread(BitmapsCache.this.cleanupSharedBuffers, 5000L);
            } else {
                BitmapsCache.cleanupScheduled = false;
            }
        }
    };

    public static class CacheOptions {
        public int compressQuality = 100;
        public boolean fallback = false;
        public boolean firstFrame;
    }

    public interface Cacheable {
        int getNextFrame(Bitmap bitmap);

        void prepareForGenerateCache();

        void releaseForGenerateCache();
    }

    public static class Metadata {
        public int frame;
    }

    public void cancelCreate() {
    }

    public BitmapsCache(File file, Cacheable cacheable, CacheOptions cacheOptions, int i, int i2, boolean z) {
        this.source = cacheable;
        this.w = i;
        this.h = i2;
        this.compressQuality = cacheOptions.compressQuality;
        this.fileName = file.getName();
        if (bitmapCompressExecutor == null) {
            int i3 = N;
            bitmapCompressExecutor = new ThreadPoolExecutor(i3, i3, 60L, TimeUnit.SECONDS, new LinkedBlockingQueue());
        }
        File file2 = new File(FileLoader.checkDirectory(4), "acache");
        if (!mkdir) {
            file2.mkdir();
            mkdir = true;
        }
        StringBuilder sb = new StringBuilder();
        sb.append(this.fileName);
        sb.append("_");
        sb.append(i);
        sb.append("_");
        sb.append(i2);
        sb.append(z ? "_nolimit" : " ");
        sb.append(".pcache2");
        File file3 = new File(file2, sb.toString());
        this.file = file3;
        this.useSharedBuffers = i < AndroidUtilities.dp(60.0f) && i2 < AndroidUtilities.dp(60.0f);
        if (SharedConfig.getDevicePerformanceClass() >= 2) {
            this.fileExist = file3.exists();
            if (this.fileExist) {
                try {
                    RandomAccessFile randomAccessFile = new RandomAccessFile(file3, "r");
                    try {
                        this.cacheCreated = randomAccessFile.readBoolean();
                        if (this.cacheCreated && this.frameOffsets.isEmpty()) {
                            randomAccessFile.seek(randomAccessFile.readInt());
                            int i4 = randomAccessFile.readInt();
                            fillFrames(randomAccessFile, i4 > 10000 ? 0 : i4);
                            if (this.frameOffsets.isEmpty()) {
                                this.cacheCreated = false;
                                this.fileExist = false;
                                this.checked = true;
                                file3.delete();
                            } else {
                                closeCachedFile();
                                this.cachedFile = new RandomAccessFile(file3, "r");
                            }
                        }
                        randomAccessFile.close();
                    } catch (Throwable th) {
                        try {
                            randomAccessFile.close();
                            throw th;
                        } catch (Throwable th2) {
                            th.addSuppressed(th2);
                            throw th;
                        }
                    }
                } catch (Throwable th3) {
                    FileLog.e(th3);
                    this.file.delete();
                    this.fileExist = false;
                    this.checked = true;
                }
            }
            this.checked = true;
            return;
        }
        this.fileExist = false;
        this.cacheCreated = false;
    }

    public static void incrementTaskCounter() {
        taskCounter++;
    }

    public static void decrementTaskCounter() {
        int i = taskCounter - 1;
        taskCounter = i;
        if (i <= 0) {
            taskCounter = 0;
            RLottieDrawable.lottieCacheGenerateQueue.postRunnable(new Runnable() { // from class: org.telegram.messenger.utils.BitmapsCache$$ExternalSyntheticLambda0
                @Override // java.lang.Runnable
                public final void run() {
                    BitmapsCache.m3946$r8$lambda$75FwZaXfPh7AZ0gZ3tKl5Rqbyg();
                }
            });
        }
    }

    /* JADX INFO: renamed from: $r8$lambda$75FwZaXfPh-7AZ0gZ3tKl5Rqbyg, reason: not valid java name */
    public static /* synthetic */ void m3946$r8$lambda$75FwZaXfPh7AZ0gZ3tKl5Rqbyg() {
        CacheGeneratorSharedTools cacheGeneratorSharedTools = sharedTools;
        if (cacheGeneratorSharedTools != null) {
            cacheGeneratorSharedTools.release();
            sharedTools = null;
        }
    }

    /* JADX WARN: Code duplicated, block: B:110:0x01ca A[EXC_TOP_SPLITTER, SYNTHETIC] */
    /* JADX WARN: Code duplicated, block: B:112:0x00cf A[EXC_TOP_SPLITTER, SYNTHETIC] */
    /* JADX WARN: Code duplicated, block: B:114:0x01be A[EXC_TOP_SPLITTER, SYNTHETIC] */
    /* JADX WARN: Code duplicated, block: B:118:0x00fa A[EXC_TOP_SPLITTER, SYNTHETIC] */
    /* JADX WARN: Code duplicated, block: B:128:0x00f1 A[SYNTHETIC] */
    /* JADX WARN: Code duplicated, block: B:130:0x01a3 A[SYNTHETIC] */
    /* JADX WARN: Code duplicated, block: B:133:0x0102 A[SYNTHETIC] */
    /* JADX WARN: Code duplicated, block: B:138:0x01cd A[SYNTHETIC] */
    /* JADX WARN: Code duplicated, block: B:44:0x0096 A[Catch: all -> 0x005b, IOException -> 0x005e, FileNotFoundException -> 0x0061, TryCatch #12 {FileNotFoundException -> 0x0061, IOException -> 0x005e, blocks: (B:3:0x0002, B:17:0x004e, B:32:0x0073, B:38:0x0081, B:42:0x0088, B:44:0x0096, B:45:0x009d, B:46:0x00cb, B:48:0x00cf, B:52:0x00d7, B:54:0x00df, B:57:0x00e7, B:60:0x00f2, B:62:0x00f6, B:64:0x00fa, B:68:0x0102, B:67:0x00ff, B:69:0x0105, B:70:0x0125, B:72:0x012b, B:73:0x0148, B:75:0x0189, B:78:0x01a3, B:79:0x01aa, B:81:0x01ae, B:82:0x01b3, B:83:0x01b6, B:85:0x01ba, B:87:0x01be, B:91:0x01c6, B:90:0x01c3, B:95:0x01d0, B:51:0x00d4), top: B:124:0x0002, outer: #9 }] */
    /* JADX WARN: Code duplicated, block: B:62:0x00f6 A[Catch: all -> 0x005b, IOException -> 0x005e, FileNotFoundException -> 0x0061, TRY_LEAVE, TryCatch #12 {FileNotFoundException -> 0x0061, IOException -> 0x005e, blocks: (B:3:0x0002, B:17:0x004e, B:32:0x0073, B:38:0x0081, B:42:0x0088, B:44:0x0096, B:45:0x009d, B:46:0x00cb, B:48:0x00cf, B:52:0x00d7, B:54:0x00df, B:57:0x00e7, B:60:0x00f2, B:62:0x00f6, B:64:0x00fa, B:68:0x0102, B:67:0x00ff, B:69:0x0105, B:70:0x0125, B:72:0x012b, B:73:0x0148, B:75:0x0189, B:78:0x01a3, B:79:0x01aa, B:81:0x01ae, B:82:0x01b3, B:83:0x01b6, B:85:0x01ba, B:87:0x01be, B:91:0x01c6, B:90:0x01c3, B:95:0x01d0, B:51:0x00d4), top: B:124:0x0002, outer: #9 }] */
    /* JADX WARN: Code duplicated, block: B:72:0x012b A[Catch: all -> 0x005b, IOException -> 0x005e, FileNotFoundException -> 0x0061, LOOP:2: B:70:0x0125->B:72:0x012b, LOOP_END, TryCatch #12 {FileNotFoundException -> 0x0061, IOException -> 0x005e, blocks: (B:3:0x0002, B:17:0x004e, B:32:0x0073, B:38:0x0081, B:42:0x0088, B:44:0x0096, B:45:0x009d, B:46:0x00cb, B:48:0x00cf, B:52:0x00d7, B:54:0x00df, B:57:0x00e7, B:60:0x00f2, B:62:0x00f6, B:64:0x00fa, B:68:0x0102, B:67:0x00ff, B:69:0x0105, B:70:0x0125, B:72:0x012b, B:73:0x0148, B:75:0x0189, B:78:0x01a3, B:79:0x01aa, B:81:0x01ae, B:82:0x01b3, B:83:0x01b6, B:85:0x01ba, B:87:0x01be, B:91:0x01c6, B:90:0x01c3, B:95:0x01d0, B:51:0x00d4), top: B:124:0x0002, outer: #9 }] */
    /* JADX WARN: Code duplicated, block: B:75:0x0189 A[Catch: all -> 0x005b, IOException -> 0x005e, FileNotFoundException -> 0x0061, TRY_ENTER, TryCatch #12 {FileNotFoundException -> 0x0061, IOException -> 0x005e, blocks: (B:3:0x0002, B:17:0x004e, B:32:0x0073, B:38:0x0081, B:42:0x0088, B:44:0x0096, B:45:0x009d, B:46:0x00cb, B:48:0x00cf, B:52:0x00d7, B:54:0x00df, B:57:0x00e7, B:60:0x00f2, B:62:0x00f6, B:64:0x00fa, B:68:0x0102, B:67:0x00ff, B:69:0x0105, B:70:0x0125, B:72:0x012b, B:73:0x0148, B:75:0x0189, B:78:0x01a3, B:79:0x01aa, B:81:0x01ae, B:82:0x01b3, B:83:0x01b6, B:85:0x01ba, B:87:0x01be, B:91:0x01c6, B:90:0x01c3, B:95:0x01d0, B:51:0x00d4), top: B:124:0x0002, outer: #9 }] */
    /* JADX WARN: Code duplicated, block: B:77:0x01a2  */
    /* JADX WARN: Code duplicated, block: B:81:0x01ae A[Catch: all -> 0x005b, IOException -> 0x005e, FileNotFoundException -> 0x0061, TryCatch #12 {FileNotFoundException -> 0x0061, IOException -> 0x005e, blocks: (B:3:0x0002, B:17:0x004e, B:32:0x0073, B:38:0x0081, B:42:0x0088, B:44:0x0096, B:45:0x009d, B:46:0x00cb, B:48:0x00cf, B:52:0x00d7, B:54:0x00df, B:57:0x00e7, B:60:0x00f2, B:62:0x00f6, B:64:0x00fa, B:68:0x0102, B:67:0x00ff, B:69:0x0105, B:70:0x0125, B:72:0x012b, B:73:0x0148, B:75:0x0189, B:78:0x01a3, B:79:0x01aa, B:81:0x01ae, B:82:0x01b3, B:83:0x01b6, B:85:0x01ba, B:87:0x01be, B:91:0x01c6, B:90:0x01c3, B:95:0x01d0, B:51:0x00d4), top: B:124:0x0002, outer: #9 }] */
    /* JADX WARN: Code duplicated, block: B:85:0x01ba A[Catch: all -> 0x005b, IOException -> 0x005e, FileNotFoundException -> 0x0061, TRY_LEAVE, TryCatch #12 {FileNotFoundException -> 0x0061, IOException -> 0x005e, blocks: (B:3:0x0002, B:17:0x004e, B:32:0x0073, B:38:0x0081, B:42:0x0088, B:44:0x0096, B:45:0x009d, B:46:0x00cb, B:48:0x00cf, B:52:0x00d7, B:54:0x00df, B:57:0x00e7, B:60:0x00f2, B:62:0x00f6, B:64:0x00fa, B:68:0x0102, B:67:0x00ff, B:69:0x0105, B:70:0x0125, B:72:0x012b, B:73:0x0148, B:75:0x0189, B:78:0x01a3, B:79:0x01aa, B:81:0x01ae, B:82:0x01b3, B:83:0x01b6, B:85:0x01ba, B:87:0x01be, B:91:0x01c6, B:90:0x01c3, B:95:0x01d0, B:51:0x00d4), top: B:124:0x0002, outer: #9 }] */
    public void createCache() {
        RandomAccessFile randomAccessFile;
        final RandomAccessFile randomAccessFile2;
        final Bitmap[] bitmapArr;
        final ImmutableByteArrayOutputStream[] immutableByteArrayOutputStreamArr;
        final CountDownLatch[] countDownLatchArr;
        final ArrayList arrayList;
        final AtomicBoolean atomicBoolean;
        final int i;
        final int i2;
        CountDownLatch countDownLatch;
        CountDownLatch countDownLatch2;
        Bitmap bitmap;
        int i3;
        int i4;
        CountDownLatch countDownLatch3;
        try {
            try {
                if (this.file.exists()) {
                    try {
                        randomAccessFile = new RandomAccessFile(this.file, "r");
                        try {
                            this.cacheCreated = randomAccessFile.readBoolean();
                            if (this.cacheCreated) {
                                this.frameOffsets.clear();
                                randomAccessFile.seek(randomAccessFile.readInt());
                                int i5 = randomAccessFile.readInt();
                                if (i5 > 10000) {
                                    i5 = 0;
                                }
                                if (i5 > 0) {
                                    fillFrames(randomAccessFile, i5);
                                    randomAccessFile.seek(0L);
                                    if (this.cachedFile != randomAccessFile) {
                                        closeCachedFile();
                                    }
                                    this.cachedFile = randomAccessFile;
                                    this.fileExist = true;
                                    this.checked = true;
                                    if (this.cachedFile != randomAccessFile) {
                                        try {
                                            randomAccessFile.close();
                                        } catch (Throwable unused) {
                                        }
                                    }
                                } else {
                                    this.fileExist = false;
                                    this.cacheCreated = false;
                                    this.checked = true;
                                }
                            }
                            if (!this.cacheCreated) {
                                this.file.delete();
                            }
                            if (this.cachedFile != randomAccessFile) {
                                try {
                                    randomAccessFile.close();
                                } catch (Throwable unused2) {
                                }
                            }
                            while (true) {
                                countDownLatch = countDownLatchArr[i];
                                if (countDownLatch != null) {
                                    try {
                                        countDownLatch.await();
                                    } catch (InterruptedException e) {
                                        e.printStackTrace();
                                    }
                                }
                                if (!this.cancelled.get() || atomicBoolean.get()) {
                                    break;
                                    break;
                                }
                                if (this.source.getNextFrame(bitmapArr[i]) != 1) {
                                    countDownLatchArr[i] = new CountDownLatch(1);
                                    bitmapCompressExecutor.execute(new Runnable() { // from class: org.telegram.messenger.utils.BitmapsCache$$ExternalSyntheticLambda1
                                        @Override // java.lang.Runnable
                                        public final void run() {
                                            this.f$0.lambda$createCache$1(atomicBoolean, bitmapArr, i, immutableByteArrayOutputStreamArr, i2, randomAccessFile2, arrayList, countDownLatchArr);
                                        }
                                    });
                                    i++;
                                    i2++;
                                    if (i >= N) {
                                        i = 0;
                                    }
                                    this.framesProcessed.set(i2);
                                } else {
                                    for (i3 = 0; i3 < N; i3++) {
                                        countDownLatch3 = countDownLatchArr[i3];
                                        if (countDownLatch3 != null) {
                                            try {
                                                countDownLatch3.await();
                                            } catch (InterruptedException e2) {
                                                e2.printStackTrace();
                                            }
                                        }
                                    }
                                    int length = (int) randomAccessFile2.length();
                                    Collections.sort(arrayList, Comparator.CC.comparingInt(new ToIntFunction() { // from class: org.telegram.messenger.utils.BitmapsCache$$ExternalSyntheticLambda2
                                        @Override // java.util.function.ToIntFunction
                                        public final int applyAsInt(Object obj) {
                                            return ((BitmapsCache.FrameOffset) obj).index;
                                        }
                                    }));
                                    immutableByteArrayOutputStreamArr[0].reset();
                                    int size = arrayList.size();
                                    immutableByteArrayOutputStreamArr[0].writeInt(size);
                                    for (i4 = 0; i4 < arrayList.size(); i4++) {
                                        immutableByteArrayOutputStreamArr[0].writeInt(((FrameOffset) arrayList.get(i4)).frameOffset);
                                        immutableByteArrayOutputStreamArr[0].writeInt(((FrameOffset) arrayList.get(i4)).frameSize);
                                    }
                                    randomAccessFile2.write(immutableByteArrayOutputStreamArr[0].buf, 0, (size * 8) + 4);
                                    immutableByteArrayOutputStreamArr[0].reset();
                                    randomAccessFile2.seek(0L);
                                    randomAccessFile2.writeBoolean(true);
                                    randomAccessFile2.writeInt(length);
                                    atomicBoolean.set(true);
                                    randomAccessFile2.close();
                                    this.frameOffsets.clear();
                                    this.frameOffsets.addAll(arrayList);
                                    closeCachedFile();
                                    this.cachedFile = new RandomAccessFile(this.file, "r");
                                    this.cacheCreated = true;
                                    this.fileExist = true;
                                    this.checked = true;
                                    this.source.releaseForGenerateCache();
                                    return;
                                }
                            }
                        } catch (Throwable unused3) {
                            try {
                                this.file.delete();
                            } catch (Throwable unused4) {
                            }
                            if (this.cachedFile != randomAccessFile && randomAccessFile != null) {
                                randomAccessFile.close();
                            }
                        }
                    } catch (Throwable unused5) {
                        randomAccessFile = null;
                    }
                    randomAccessFile2 = new RandomAccessFile(this.file, "rw");
                    if (sharedTools == null) {
                        sharedTools = new CacheGeneratorSharedTools();
                    }
                    sharedTools.allocate(this.h, this.w);
                    bitmapArr = sharedTools.bitmap;
                    immutableByteArrayOutputStreamArr = sharedTools.byteArrayOutputStream;
                    countDownLatchArr = new CountDownLatch[N];
                    arrayList = new ArrayList();
                    randomAccessFile2.writeBoolean(false);
                    randomAccessFile2.writeInt(0);
                    atomicBoolean = new AtomicBoolean(false);
                    this.source.prepareForGenerateCache();
                    i = 0;
                    i2 = 0;
                    if (BuildVars.DEBUG_VERSION) {
                        FileLog.d("cancelled cache generation");
                    }
                    atomicBoolean.set(true);
                    for (int i6 = 0; i6 < N; i6++) {
                        countDownLatch2 = countDownLatchArr[i6];
                        if (countDownLatch2 != null) {
                            try {
                                countDownLatch2.await();
                            } catch (InterruptedException e3) {
                                e3.printStackTrace();
                            }
                        }
                        bitmap = bitmapArr[i6];
                        if (bitmap != null) {
                            try {
                                bitmap.recycle();
                            } catch (Exception unused6) {
                            }
                        }
                    }
                    randomAccessFile2.close();
                    this.source.releaseForGenerateCache();
                } else {
                    randomAccessFile2 = new RandomAccessFile(this.file, "rw");
                    if (sharedTools == null) {
                        sharedTools = new CacheGeneratorSharedTools();
                    }
                    sharedTools.allocate(this.h, this.w);
                    bitmapArr = sharedTools.bitmap;
                    immutableByteArrayOutputStreamArr = sharedTools.byteArrayOutputStream;
                    countDownLatchArr = new CountDownLatch[N];
                    arrayList = new ArrayList();
                    randomAccessFile2.writeBoolean(false);
                    randomAccessFile2.writeInt(0);
                    atomicBoolean = new AtomicBoolean(false);
                    this.source.prepareForGenerateCache();
                    i = 0;
                    i2 = 0;
                    while (true) {
                        countDownLatch = countDownLatchArr[i];
                        if (countDownLatch != null) {
                            countDownLatch.await();
                        }
                        if (!this.cancelled.get()) {
                            break;
                        }
                        if (this.source.getNextFrame(bitmapArr[i]) != 1) {
                            countDownLatchArr[i] = new CountDownLatch(1);
                            bitmapCompressExecutor.execute(new Runnable() { // from class: org.telegram.messenger.utils.BitmapsCache$$ExternalSyntheticLambda1
                                @Override // java.lang.Runnable
                                public final void run() {
                                    this.f$0.lambda$createCache$1(atomicBoolean, bitmapArr, i, immutableByteArrayOutputStreamArr, i2, randomAccessFile2, arrayList, countDownLatchArr);
                                }
                            });
                            i++;
                            i2++;
                            if (i >= N) {
                                i = 0;
                            }
                            this.framesProcessed.set(i2);
                        } else {
                            while (i3 < N) {
                                countDownLatch3 = countDownLatchArr[i3];
                                if (countDownLatch3 != null) {
                                    countDownLatch3.await();
                                }
                            }
                            int length2 = (int) randomAccessFile2.length();
                            Collections.sort(arrayList, Comparator.CC.comparingInt(new ToIntFunction() { // from class: org.telegram.messenger.utils.BitmapsCache$$ExternalSyntheticLambda2
                                @Override // java.util.function.ToIntFunction
                                public final int applyAsInt(Object obj) {
                                    return ((BitmapsCache.FrameOffset) obj).index;
                                }
                            }));
                            immutableByteArrayOutputStreamArr[0].reset();
                            int size2 = arrayList.size();
                            immutableByteArrayOutputStreamArr[0].writeInt(size2);
                            while (i4 < arrayList.size()) {
                                immutableByteArrayOutputStreamArr[0].writeInt(((FrameOffset) arrayList.get(i4)).frameOffset);
                                immutableByteArrayOutputStreamArr[0].writeInt(((FrameOffset) arrayList.get(i4)).frameSize);
                            }
                            randomAccessFile2.write(immutableByteArrayOutputStreamArr[0].buf, 0, (size2 * 8) + 4);
                            immutableByteArrayOutputStreamArr[0].reset();
                            randomAccessFile2.seek(0L);
                            randomAccessFile2.writeBoolean(true);
                            randomAccessFile2.writeInt(length2);
                            atomicBoolean.set(true);
                            randomAccessFile2.close();
                            this.frameOffsets.clear();
                            this.frameOffsets.addAll(arrayList);
                            closeCachedFile();
                            this.cachedFile = new RandomAccessFile(this.file, "r");
                            this.cacheCreated = true;
                            this.fileExist = true;
                            this.checked = true;
                            this.source.releaseForGenerateCache();
                            return;
                        }
                    }
                    if (BuildVars.DEBUG_VERSION) {
                        FileLog.d("cancelled cache generation");
                    }
                    atomicBoolean.set(true);
                    while (i6 < N) {
                        countDownLatch2 = countDownLatchArr[i6];
                        if (countDownLatch2 != null) {
                            countDownLatch2.await();
                        }
                        bitmap = bitmapArr[i6];
                        if (bitmap != null) {
                            bitmap.recycle();
                        }
                    }
                    randomAccessFile2.close();
                    this.source.releaseForGenerateCache();
                }
                this.source.releaseForGenerateCache();
            } catch (Throwable th) {
                this.source.releaseForGenerateCache();
                throw th;
            }
        } catch (FileNotFoundException e4) {
            e4.printStackTrace();
        } catch (IOException e5) {
            e5.printStackTrace();
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$createCache$1(AtomicBoolean atomicBoolean, Bitmap[] bitmapArr, int i, ImmutableByteArrayOutputStream[] immutableByteArrayOutputStreamArr, int i2, RandomAccessFile randomAccessFile, ArrayList arrayList, CountDownLatch[] countDownLatchArr) {
        if (this.cancelled.get() || atomicBoolean.get()) {
            return;
        }
        Bitmap.CompressFormat compressFormat = Bitmap.CompressFormat.WEBP;
        if (Build.VERSION.SDK_INT <= 28) {
            compressFormat = Bitmap.CompressFormat.PNG;
        }
        bitmapArr[i].compress(compressFormat, this.compressQuality, immutableByteArrayOutputStreamArr[i]);
        int i3 = immutableByteArrayOutputStreamArr[i].count;
        try {
            synchronized (this.mutex) {
                try {
                    FrameOffset frameOffset = new FrameOffset(i2);
                    frameOffset.frameOffset = (int) randomAccessFile.length();
                    arrayList.add(frameOffset);
                    randomAccessFile.write(immutableByteArrayOutputStreamArr[i].buf, 0, i3);
                    frameOffset.frameSize = i3;
                    immutableByteArrayOutputStreamArr[i].reset();
                } catch (Throwable th) {
                    throw th;
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
            try {
                randomAccessFile.close();
            } catch (Exception unused) {
            } finally {
                atomicBoolean.set(true);
            }
        }
        countDownLatchArr[i].countDown();
    }

    private void fillFrames(RandomAccessFile randomAccessFile, int i) throws IOException {
        if (i == 0) {
            return;
        }
        byte[] bArr = new byte[i * 8];
        randomAccessFile.read(bArr);
        ByteBuffer byteBufferWrap = ByteBuffer.wrap(bArr);
        for (int i2 = 0; i2 < i; i2++) {
            FrameOffset frameOffset = new FrameOffset(i2);
            frameOffset.frameOffset = byteBufferWrap.getInt();
            frameOffset.frameSize = byteBufferWrap.getInt();
            this.frameOffsets.add(frameOffset);
        }
    }

    public int getFrame(Bitmap bitmap, Metadata metadata) {
        int frame = getFrame(this.frameIndex, bitmap);
        metadata.frame = this.frameIndex;
        if (this.cacheCreated && !this.frameOffsets.isEmpty()) {
            int i = this.frameIndex + 1;
            this.frameIndex = i;
            if (i >= this.frameOffsets.size()) {
                this.frameIndex = 0;
            }
        }
        return frame;
    }

    public int getFrame(int i, Bitmap bitmap) {
        RandomAccessFile randomAccessFile;
        if (this.error) {
            return -1;
        }
        RandomAccessFile randomAccessFile2 = null;
        try {
            if (!this.cacheCreated && !this.fileExist) {
                return -1;
            }
            if (!this.cacheCreated || (randomAccessFile = this.cachedFile) == null) {
                randomAccessFile = new RandomAccessFile(this.file, "r");
                try {
                    this.cacheCreated = randomAccessFile.readBoolean();
                    if (this.cacheCreated && this.frameOffsets.isEmpty()) {
                        randomAccessFile.seek(randomAccessFile.readInt());
                        fillFrames(randomAccessFile, randomAccessFile.readInt());
                    }
                    if (this.frameOffsets.size() == 0) {
                        this.cacheCreated = false;
                        this.checked = true;
                    }
                    if (!this.cacheCreated) {
                        randomAccessFile.close();
                        return -1;
                    }
                } catch (FileNotFoundException unused) {
                    randomAccessFile2 = randomAccessFile;
                } catch (Throwable th) {
                    th = th;
                    randomAccessFile2 = randomAccessFile;
                    FileLog.e(th);
                    int i2 = this.tryCount + 1;
                    this.tryCount = i2;
                    if (i2 > 10) {
                        this.error = true;
                    }
                    if (this.error) {
                        try {
                            randomAccessFile2.close();
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                    return -1;
                }
            }
            if (this.frameOffsets.size() == 0) {
                return -1;
            }
            FrameOffset frameOffset = (FrameOffset) this.frameOffsets.get(Utilities.clamp(i, this.frameOffsets.size() - 1, 0));
            randomAccessFile.seek(frameOffset.frameOffset);
            byte[] buffer = getBuffer(frameOffset);
            randomAccessFile.readFully(buffer, 0, frameOffset.frameSize);
            if (!this.recycled) {
                if (this.cachedFile != randomAccessFile) {
                    closeCachedFile();
                }
                this.cachedFile = randomAccessFile;
            } else {
                this.cachedFile = null;
                randomAccessFile.close();
            }
            if (this.options == null) {
                this.options = new BitmapFactory.Options();
            }
            BitmapFactory.Options options = this.options;
            options.inBitmap = bitmap;
            BitmapFactory.decodeByteArray(buffer, 0, frameOffset.frameSize, options);
            this.options.inBitmap = null;
            return 0;
        } catch (FileNotFoundException unused2) {
        } catch (Throwable th2) {
            th = th2;
        }
        if (this.error && randomAccessFile2 != null) {
            randomAccessFile2.close();
        }
        return -1;
    }

    private void closeCachedFile() {
        RandomAccessFile randomAccessFile = this.cachedFile;
        if (randomAccessFile != null) {
            try {
                randomAccessFile.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    private byte[] getBuffer(FrameOffset frameOffset) {
        byte[] bArr;
        boolean z = this.useSharedBuffers && Thread.currentThread().getName().startsWith(DispatchQueuePoolBackground.THREAD_PREFIX);
        if (z) {
            bArr = (byte[]) sharedBuffers.get(Thread.currentThread());
        } else {
            bArr = this.bufferTmp;
        }
        if (bArr != null && bArr.length >= frameOffset.frameSize) {
            return bArr;
        }
        byte[] bArr2 = new byte[(int) (frameOffset.frameSize * 1.3f)];
        if (z) {
            sharedBuffers.put(Thread.currentThread(), bArr2);
            if (!cleanupScheduled) {
                cleanupScheduled = true;
                AndroidUtilities.runOnUIThread(this.cleanupSharedBuffers, 5000L);
            }
            return bArr2;
        }
        this.bufferTmp = bArr2;
        return bArr2;
    }

    public boolean needGenCache() {
        return (this.cacheCreated && this.fileExist) ? false : true;
    }

    public void recycle() {
        RandomAccessFile randomAccessFile = this.cachedFile;
        if (randomAccessFile != null) {
            try {
                randomAccessFile.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
            this.cachedFile = null;
        }
        this.recycled = true;
    }

    public int getFrameCount() {
        return this.frameOffsets.size();
    }

    /* JADX INFO: Access modifiers changed from: private */
    class FrameOffset {
        int frameOffset;
        int frameSize;
        final int index;

        private FrameOffset(int i) {
            this.index = i;
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    static class CacheGeneratorSharedTools {
        private Bitmap[] bitmap;
        ImmutableByteArrayOutputStream[] byteArrayOutputStream;
        private int lastSize;

        private CacheGeneratorSharedTools() {
            this.byteArrayOutputStream = new ImmutableByteArrayOutputStream[BitmapsCache.N];
            this.bitmap = new Bitmap[BitmapsCache.N];
        }

        void allocate(int i, int i2) {
            int i3 = (i2 << 16) + i;
            boolean z = this.lastSize != i3;
            this.lastSize = i3;
            for (int i4 = 0; i4 < BitmapsCache.N; i4++) {
                if (z || this.bitmap[i4] == null) {
                    final Bitmap bitmap = this.bitmap[i4];
                    if (bitmap != null) {
                        Utilities.globalQueue.postRunnable(new Runnable() { // from class: org.telegram.messenger.utils.BitmapsCache$CacheGeneratorSharedTools$$ExternalSyntheticLambda1
                            @Override // java.lang.Runnable
                            public final void run() {
                                bitmap.recycle();
                            }
                        });
                    }
                    this.bitmap[i4] = Bitmap.createBitmap(i2, i, Bitmap.Config.ARGB_8888);
                }
                ImmutableByteArrayOutputStream[] immutableByteArrayOutputStreamArr = this.byteArrayOutputStream;
                if (immutableByteArrayOutputStreamArr[i4] == null) {
                    immutableByteArrayOutputStreamArr[i4] = new ImmutableByteArrayOutputStream(i2 * i * 2);
                }
            }
        }

        void release() {
            final ArrayList arrayList = null;
            for (int i = 0; i < BitmapsCache.N; i++) {
                if (this.bitmap[i] != null) {
                    if (arrayList == null) {
                        arrayList = new ArrayList();
                    }
                    arrayList.add(this.bitmap[i]);
                }
                this.bitmap[i] = null;
                this.byteArrayOutputStream[i] = null;
            }
            if (arrayList.isEmpty()) {
                return;
            }
            Utilities.globalQueue.postRunnable(new Runnable() { // from class: org.telegram.messenger.utils.BitmapsCache$CacheGeneratorSharedTools$$ExternalSyntheticLambda0
                @Override // java.lang.Runnable
                public final void run() {
                    BitmapsCache.CacheGeneratorSharedTools.$r8$lambda$92tmsO31cfirt8xhSYMxR5CoewY(arrayList);
                }
            });
        }

        public static /* synthetic */ void $r8$lambda$92tmsO31cfirt8xhSYMxR5CoewY(ArrayList arrayList) {
            int size = arrayList.size();
            int i = 0;
            while (i < size) {
                Object obj = arrayList.get(i);
                i++;
                ((Bitmap) obj).recycle();
            }
        }
    }
}
