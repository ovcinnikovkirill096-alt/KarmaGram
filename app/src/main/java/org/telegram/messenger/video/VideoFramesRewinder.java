package org.telegram.messenger.video;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.view.View;
import java.io.File;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.Iterator;
import java.util.TreeSet;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicLong;
import org.telegram.messenger.AndroidUtilities;
import org.telegram.messenger.FileLog;
import org.telegram.messenger.SharedConfig;
import org.telegram.messenger.UserConfig;
import org.telegram.messenger.Utilities;
import org.telegram.ui.Components.AnimatedFileDrawable;

public class VideoFramesRewinder {
    private Frame currentFrame;
    private boolean destroyAfterPrepare;
    int h;
    private boolean isPreparing;
    private long lastSeek;
    private int maxFrameSide;
    private int maxFramesCount;
    private View parentView;
    private long prepareToMs;
    private float prepareWithSpeed;
    private long ptr;
    int w;
    private final Paint paint = new Paint(2);
    private final int[] meta = new int[6];
    private final ArrayList<Frame> freeFrames = new ArrayList<>();
    private final TreeSet<Frame> frames = new TreeSet<>(new Comparator() { // from class: org.telegram.messenger.video.VideoFramesRewinder$$ExternalSyntheticLambda1
        @Override // java.util.Comparator
        public final int compare(Object obj, Object obj2) {
            return VideoFramesRewinder.$r8$lambda$Gp5VzuvDbaeLquWZKjlA_BCZs5Q((VideoFramesRewinder.Frame) obj, (VideoFramesRewinder.Frame) obj2);
        }
    });
    private AtomicBoolean stop = new AtomicBoolean(false);
    private AtomicLong until = new AtomicLong(0);
    private float lastSpeed = 1.0f;
    private Runnable prepareRunnable = new Runnable() { // from class: org.telegram.messenger.video.VideoFramesRewinder$$ExternalSyntheticLambda2
        @Override // java.lang.Runnable
        public final void run() {
            this.f$0.lambda$new$2();
        }
    };

    public VideoFramesRewinder() {
        int devicePerformanceClass = SharedConfig.getDevicePerformanceClass();
        if (devicePerformanceClass == 1) {
            this.maxFramesCount = 200;
            this.maxFrameSide = 580;
        } else if (devicePerformanceClass == 2) {
            this.maxFramesCount = 400;
            this.maxFrameSide = 720;
        } else {
            this.maxFramesCount = 100;
            this.maxFrameSide = 480;
        }
    }

    public void draw(Canvas canvas, int i, int i2) {
        this.w = i;
        this.h = i2;
        if (this.ptr == 0 || this.currentFrame == null) {
            return;
        }
        canvas.save();
        canvas.scale(i / this.currentFrame.bitmap.getWidth(), i2 / this.currentFrame.bitmap.getHeight());
        canvas.drawBitmap(this.currentFrame.bitmap, 0.0f, 0.0f, this.paint);
        canvas.restore();
    }

    public boolean isReady() {
        return this.ptr != 0;
    }

    public void setup(File file) {
        if (file == null) {
            release();
        } else {
            this.stop.set(false);
            this.ptr = AnimatedFileDrawable.createDecoder(file.getAbsolutePath(), this.meta, UserConfig.selectedAccount, 0L, null, true);
        }
    }

    public static /* synthetic */ int $r8$lambda$Gp5VzuvDbaeLquWZKjlA_BCZs5Q(Frame frame, Frame frame2) {
        return (int) (frame.position - frame2.position);
    }

    /* JADX INFO: Access modifiers changed from: private */
    class Frame {
        Bitmap bitmap;
        long position;

        private Frame() {
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    /* JADX WARN: Code duplicated, block: B:27:0x00cd A[LOOP:1: B:25:0x00b8->B:27:0x00cd, LOOP_END] */
    /* JADX WARN: Code duplicated, block: B:30:0x0115  */
    /* JADX WARN: Code duplicated, block: B:33:0x011b  */
    /* JADX WARN: Code duplicated, block: B:43:0x0139 A[EDGE_INSN: B:43:0x0139->B:36:0x0139 BREAK  A[LOOP:0: B:7:0x0058->B:34:0x0127], SYNTHETIC] */
    /* JADX WARN: Code duplicated, block: B:45:0x0127 A[SYNTHETIC] */
    /* JADX WARN: Code duplicated, block: B:46:0x00f0 A[EDGE_INSN: B:46:0x00f0->B:28:0x00f0 BREAK  A[LOOP:1: B:25:0x00b8->B:27:0x00cd], SYNTHETIC] */
    public /* synthetic */ void lambda$new$2() {
        Frame frame;
        int i;
        int i2;
        int i3;
        long j;
        Bitmap bitmap;
        final ArrayList arrayList = new ArrayList();
        final long jCurrentTimeMillis = System.currentTimeMillis();
        int[] iArr = this.meta;
        char c = 4;
        int i4 = iArr[4];
        int i5 = 0;
        int iMin = Math.min(this.w / 4, iArr[0]);
        int iMin2 = Math.min(this.h / 4, this.meta[1]);
        int i6 = this.maxFrameSide;
        if (iMin > i6 || iMin2 > i6) {
            float fMax = i6 / Math.max(iMin, iMin2);
            iMin = (int) (iMin * fMax);
            iMin2 = (int) (iMin2 * fMax);
        }
        AnimatedFileDrawable.seekToMs(this.ptr, this.prepareToMs - ((long) (this.prepareWithSpeed * 350.0f)), this.meta, false, 1381, null);
        char c2 = 3;
        long j2 = this.meta[3];
        int i7 = 0;
        int i8 = 0;
        while (true) {
            char c3 = c;
            if (this.meta[c2] > this.until.get() || i7 >= this.maxFramesCount || this.stop.get()) {
                break;
            }
            float f = 1000.0f / i4;
            char c4 = c2;
            long j3 = j2;
            long j4 = (long) (j2 + (this.prepareWithSpeed * f));
            if (!this.freeFrames.isEmpty()) {
                frame = this.freeFrames.remove(i5);
            } else {
                frame = new Frame();
            }
            Bitmap bitmap2 = frame.bitmap;
            if (bitmap2 == null || bitmap2.getWidth() != iMin || frame.bitmap.getHeight() != iMin2) {
                AndroidUtilities.recycleBitmap(frame.bitmap);
                try {
                    frame.bitmap = Bitmap.createBitmap(iMin, iMin2, Bitmap.Config.ARGB_8888);
                    while (true) {
                        i = i7;
                        i2 = i4;
                        i3 = iMin2;
                        if (((long) this.meta[c4]) + ((long) Math.ceil(f)) < j4) {
                            break;
                        }
                        long j5 = this.ptr;
                        int[] iArr2 = this.meta;
                        AnimatedFileDrawable.getVideoFrame(j5, null, iArr2, 0, true, 0.0f, iArr2[c3], false, null);
                        i4 = i2;
                        i7 = i;
                        iMin2 = i3;
                    }
                    j = this.ptr;
                    bitmap = frame.bitmap;
                    if (AnimatedFileDrawable.getVideoFrame(j, bitmap, this.meta, bitmap.getRowBytes(), true, 0.0f, this.meta[c3], false, null) == 0) {
                        i8++;
                        if (i8 > 6) {
                            break;
                        }
                    } else {
                        long j6 = this.meta[c4];
                        frame.position = j6;
                        arrayList.add(frame);
                        j3 = j6;
                    }
                    i7 = i + 1;
                    i4 = i2;
                    c2 = c4;
                    c = c3;
                    j2 = j3;
                    iMin2 = i3;
                    i5 = 0;
                } catch (OutOfMemoryError unused) {
                    FileLog.d("[VideoFramesRewinder] failed to create bitmap: out of memory");
                }
            } else {
                while (true) {
                    i = i7;
                    i2 = i4;
                    i3 = iMin2;
                    if (((long) this.meta[c4]) + ((long) Math.ceil(f)) < j4) {
                        break;
                        break;
                    }
                    long j7 = this.ptr;
                    int[] iArr3 = this.meta;
                    AnimatedFileDrawable.getVideoFrame(j7, null, iArr3, 0, true, 0.0f, iArr3[c3], false, null);
                    i4 = i2;
                    i7 = i;
                    iMin2 = i3;
                }
                j = this.ptr;
                bitmap = frame.bitmap;
                if (AnimatedFileDrawable.getVideoFrame(j, bitmap, this.meta, bitmap.getRowBytes(), true, 0.0f, this.meta[c3], false, null) == 0) {
                    i8++;
                    if (i8 > 6) {
                        break;
                        break;
                    }
                } else {
                    long j8 = this.meta[c4];
                    frame.position = j8;
                    arrayList.add(frame);
                    j3 = j8;
                }
                i7 = i + 1;
                i4 = i2;
                c2 = c4;
                c = c3;
                j2 = j3;
                iMin2 = i3;
                i5 = 0;
            }
        }
        AndroidUtilities.runOnUIThread(new Runnable() { // from class: org.telegram.messenger.video.VideoFramesRewinder$$ExternalSyntheticLambda0
            @Override // java.lang.Runnable
            public final void run() {
                this.f$0.lambda$new$1(arrayList, jCurrentTimeMillis);
            }
        });
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$new$1(ArrayList arrayList, long j) {
        FileLog.d("[VideoFramesRewinder] total prepare of " + arrayList.size() + " took " + (System.currentTimeMillis() - j) + "ms");
        if (!arrayList.isEmpty()) {
            FileLog.d("[VideoFramesRewinder] prepared from " + ((Frame) arrayList.get(0)).position + "ms to " + ((Frame) arrayList.get(arrayList.size() - 1)).position + "ms (requested up to " + this.prepareToMs + "ms)");
        }
        this.isPreparing = false;
        Iterator<Frame> it = this.frames.iterator();
        while (it.hasNext()) {
            Frame next = it.next();
            if (this.currentFrame != next && next.position > this.lastSeek) {
                if (this.freeFrames.size() > 20) {
                    AndroidUtilities.recycleBitmap(next.bitmap);
                } else {
                    this.freeFrames.add(next);
                }
                it.remove();
            }
        }
        while (!arrayList.isEmpty() && this.frames.size() < this.maxFramesCount) {
            this.frames.add((Frame) arrayList.remove(arrayList.size() - 1));
        }
        if (arrayList.size() > 0) {
            FileLog.d("[VideoFramesRewinder] prepared " + arrayList.size() + " more frames than I could fit :(");
        }
        if (this.destroyAfterPrepare) {
            release();
            this.stop.set(false);
        }
    }

    private void prepare(long j) {
        if (this.isPreparing) {
            return;
        }
        FileLog.d("[VideoFramesRewinder] starting preparing " + j + "ms");
        this.isPreparing = true;
        this.prepareToMs = j;
        this.prepareWithSpeed = this.lastSpeed;
        Utilities.themeQueue.postRunnable(this.prepareRunnable);
    }

    public void seek(long j, float f) {
        if (this.ptr == 0) {
            return;
        }
        this.lastSeek = j;
        this.lastSpeed = f;
        this.until.set(j);
        Iterator<Frame> it = this.frames.iterator();
        ArrayList arrayList = new ArrayList();
        while (it.hasNext()) {
            Frame next = it.next();
            arrayList.add(Long.valueOf(next.position));
            float f2 = 25.0f * f;
            if (Math.abs(next.position - j) < f2) {
                if (this.currentFrame != next) {
                    FileLog.d("[VideoFramesRewinder] found a frame " + next.position + "ms to fit to " + j + "ms from " + this.frames.size() + " frames");
                    this.currentFrame = next;
                    invalidate();
                    int i = 0;
                    while (it.hasNext()) {
                        it.next();
                        it.remove();
                        i++;
                    }
                    if (i > 0) {
                        FileLog.d("[VideoFramesRewinder] also deleted " + i + " frames after this frame");
                    }
                }
                for (int size = arrayList.size() - 2; size >= 0; size--) {
                    long jLongValue = ((Long) arrayList.get(size + 1)).longValue();
                    long jLongValue2 = ((Long) arrayList.get(size)).longValue();
                    if (Math.abs(jLongValue - jLongValue2) > f2) {
                        prepare(jLongValue2);
                        return;
                    }
                }
                prepare(Math.max(0L, this.frames.first().position - 20));
                return;
            }
        }
        FileLog.d("[VideoFramesRewinder] didn't find a frame, wanting to prepare " + j + "ms");
        prepare(Math.max(0L, j));
    }

    public void clearCurrent() {
        if (this.currentFrame != null) {
            this.currentFrame = null;
            invalidate();
        }
    }

    public void release() {
        if (this.isPreparing) {
            this.stop.set(true);
            this.destroyAfterPrepare = true;
            return;
        }
        AnimatedFileDrawable.destroyDecoder(this.ptr);
        this.ptr = 0L;
        int i = 0;
        this.destroyAfterPrepare = false;
        clearCurrent();
        this.until.set(0L);
        Iterator<Frame> it = this.frames.iterator();
        while (it.hasNext()) {
            AndroidUtilities.recycleBitmap(it.next().bitmap);
        }
        this.frames.clear();
        ArrayList<Frame> arrayList = this.freeFrames;
        int size = arrayList.size();
        while (i < size) {
            Frame frame = arrayList.get(i);
            i++;
            AndroidUtilities.recycleBitmap(frame.bitmap);
        }
        this.freeFrames.clear();
    }

    public void setParentView(View view) {
        this.parentView = view;
    }

    private void invalidate() {
        View view = this.parentView;
        if (view != null) {
            view.invalidate();
        }
    }
}
