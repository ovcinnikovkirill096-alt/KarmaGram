package org.telegram.messenger;

import android.annotation.TargetApi;
import android.app.ActivityManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.Point;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffColorFilter;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.GradientDrawable;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Environment;
import android.os.SystemClock;
import android.text.TextUtils;
import android.util.Pair;
import android.util.SparseArray;
import androidx.core.graphics.ColorUtils;
import com.radolyn.ayugram.AyuConfig;
import com.radolyn.ayugram.AyuConstants;
import com.radolyn.ayugram.controllers.AyuMessagesController;
import com.radolyn.ayugram.controllers.messages.SaveMessageRequest;
import j$.util.Objects;
import j$.util.concurrent.ConcurrentHashMap;
import j$.util.function.Consumer$CC;
import j$.util.stream.Stream;
import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.RandomAccessFile;
import java.net.HttpURLConnection;
import java.net.SocketException;
import java.net.SocketTimeoutException;
import java.net.URL;
import java.net.URLConnection;
import java.net.UnknownHostException;
import java.nio.file.CopyOption;
import java.nio.file.Files;
import java.nio.file.LinkOption;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.function.Consumer;
import java.util.zip.GZIPInputStream;
import okhttp3.internal.url._UrlKt;
import org.json.JSONArray;
import org.json.JSONObject;
import org.telegram.DispatchQueuePriority;
import org.telegram.tgnet.ConnectionsManager;
import org.telegram.tgnet.RequestDelegate;
import org.telegram.tgnet.TLObject;
import org.telegram.tgnet.TLRPC;
import org.telegram.ui.Cells.ChatMessageCell;
import org.telegram.ui.Components.AnimatedFileDrawable;
import org.telegram.ui.Components.BackgroundGradientDrawable;
import org.telegram.ui.Components.MotionBackgroundDrawable;
import org.telegram.ui.Components.RLottieDrawable;
import org.telegram.ui.web.WebInstantView;

public class ImageLoader {
    public static final String AUTOPLAY_FILTER = "g";
    public static final int CACHE_TYPE_CACHE = 1;
    public static final int CACHE_TYPE_ENCRYPTED = 2;
    public static final int CACHE_TYPE_NONE = 0;
    private static final boolean DEBUG_MODE = false;
    private boolean canForce8888;
    private LruCache<BitmapDrawable> lottieMemCache;
    private LruCache<BitmapDrawable> memCache;
    private LruCache<BitmapDrawable> smallImagesMemCache;
    private LruCache<BitmapDrawable> wallpaperMemCache;
    private static ThreadLocal<byte[]> bytesLocal = new ThreadLocal<>();
    private static ThreadLocal<byte[]> bytesThumbLocal = new ThreadLocal<>();
    private static byte[] header = new byte[12];
    private static byte[] headerThumb = new byte[12];
    private static volatile ImageLoader Instance = null;
    private HashMap<String, Integer> bitmapUseCounts = new HashMap<>();
    ArrayList<AnimatedFileDrawable> cachedAnimatedFileDrawables = new ArrayList<>();
    private HashMap<String, CacheImage> imageLoadingByUrl = new HashMap<>();
    private HashMap<String, CacheImage> imageLoadingByUrlPframe = new HashMap<>();
    public ConcurrentHashMap<String, CacheImage> imageLoadingByKeys = new ConcurrentHashMap<>();
    public HashSet<String> imageLoadingKeys = new HashSet<>();
    private SparseArray<CacheImage> imageLoadingByTag = new SparseArray<>();
    private HashMap<String, ThumbGenerateInfo> waitingForQualityThumb = new HashMap<>();
    private SparseArray<String> waitingForQualityThumbByTag = new SparseArray<>();
    private LinkedList<HttpImageTask> httpTasks = new LinkedList<>();
    private LinkedList<ArtworkLoadTask> artworkTasks = new LinkedList<>();
    private DispatchQueuePriority cacheOutQueue = new DispatchQueuePriority("cacheOutQueue");
    private DispatchQueue cacheThumbOutQueue = new DispatchQueue("cacheThumbOutQueue");
    private DispatchQueue thumbGeneratingQueue = new DispatchQueue("thumbGeneratingQueue");
    private DispatchQueue imageLoadQueue = new DispatchQueue("imageLoadQueue");
    private HashMap<String, String> replacedBitmaps = new HashMap<>();
    private ConcurrentHashMap<String, long[]> fileProgresses = new ConcurrentHashMap<>();
    private HashMap<String, ThumbGenerateTask> thumbGenerateTasks = new HashMap<>();
    private HashMap<String, Integer> forceLoadingImages = new HashMap<>();
    private int currentHttpTasksCount = 0;
    private int currentArtworkTasksCount = 0;
    private ConcurrentHashMap<String, WebFile> testWebFile = new ConcurrentHashMap<>();
    private LinkedList<HttpFileTask> httpFileLoadTasks = new LinkedList<>();
    private HashMap<String, HttpFileTask> httpFileLoadTasksByKeys = new HashMap<>();
    private HashMap<String, Runnable> retryHttpsTasks = new HashMap<>();
    private int currentHttpFileLoadTasksCount = 0;
    private String ignoreRemoval = null;
    private volatile long lastCacheOutTime = 0;
    private int lastImageNum = 0;
    private File telegramPath = null;

    /* JADX INFO: renamed from: -$$Nest$fgetcanForce8888, reason: not valid java name */
    static /* bridge */ /* synthetic */ boolean m3004$$Nest$fgetcanForce8888(ImageLoader imageLoader) {
        return imageLoader.canForce8888;
    }

    /* JADX INFO: renamed from: -$$Nest$fputlastCacheOutTime, reason: not valid java name */
    static /* bridge */ /* synthetic */ void m3019$$Nest$fputlastCacheOutTime(ImageLoader imageLoader, long j) {
        imageLoader.lastCacheOutTime = j;
    }

    /* JADX INFO: renamed from: -$$Nest$misAnimatedAvatar, reason: not valid java name */
    static /* bridge */ /* synthetic */ boolean m3025$$Nest$misAnimatedAvatar(ImageLoader imageLoader, String str) {
        return imageLoader.isAnimatedAvatar(str);
    }

    /* JADX INFO: renamed from: -$$Nest$sfgetbytesLocal, reason: not valid java name */
    static /* bridge */ /* synthetic */ ThreadLocal m3030$$Nest$sfgetbytesLocal() {
        return bytesLocal;
    }

    /* JADX INFO: renamed from: -$$Nest$sfgetbytesThumbLocal, reason: not valid java name */
    static /* bridge */ /* synthetic */ ThreadLocal m3031$$Nest$sfgetbytesThumbLocal() {
        return bytesThumbLocal;
    }

    /* JADX INFO: renamed from: -$$Nest$sfgetheader, reason: not valid java name */
    static /* bridge */ /* synthetic */ byte[] m3032$$Nest$sfgetheader() {
        return header;
    }

    /* JADX INFO: renamed from: -$$Nest$sfgetheaderThumb, reason: not valid java name */
    static /* bridge */ /* synthetic */ byte[] m3033$$Nest$sfgetheaderThumb() {
        return headerThumb;
    }

    public static boolean hasAutoplayFilter(String str) {
        if (str == null) {
            return false;
        }
        String[] strArrSplit = str.split("_");
        for (int i = 0; i < strArrSplit.length; i++) {
            if (AUTOPLAY_FILTER.equals(strArrSplit[i]) || "pframe".equals(strArrSplit[i])) {
                return true;
            }
        }
        return false;
    }

    public static Drawable createStripedBitmap(ArrayList<TLRPC.PhotoSize> arrayList) {
        for (int i = 0; i < arrayList.size(); i++) {
            if (arrayList.get(i) instanceof TLRPC.TL_photoStrippedSize) {
                return new BitmapDrawable(ApplicationLoader.applicationContext.getResources(), getStrippedPhotoBitmap(((TLRPC.TL_photoStrippedSize) arrayList.get(i)).bytes, "b"));
            }
        }
        return null;
    }

    public static boolean isSdCardPath(File file) {
        return !TextUtils.isEmpty(SharedConfig.storageCacheDir) && file.getAbsolutePath().startsWith(SharedConfig.storageCacheDir);
    }

    public void moveToFront(String str) {
        if (str == null) {
            return;
        }
        if (this.lottieMemCache.get(str) != null) {
            this.lottieMemCache.moveToFront(str);
        }
        if (this.memCache.get(str) != null) {
            this.memCache.moveToFront(str);
        }
        if (this.smallImagesMemCache.get(str) != null) {
            this.smallImagesMemCache.moveToFront(str);
        }
    }

    public void putThumbsToCache(ArrayList<MessageThumb> arrayList) {
        for (int i = 0; i < arrayList.size(); i++) {
            putImageToCache(arrayList.get(i).drawable, arrayList.get(i).key, true);
        }
    }

    public LruCache<BitmapDrawable> getLottieMemCahce() {
        return this.lottieMemCache;
    }

    private static class ThumbGenerateInfo {
        private boolean big;
        private String filter;
        private ArrayList<ImageReceiver> imageReceiverArray;
        private ArrayList<Integer> imageReceiverGuidsArray;
        private TLRPC.Document parentDocument;

        private ThumbGenerateInfo() {
            this.imageReceiverArray = new ArrayList<>();
            this.imageReceiverGuidsArray = new ArrayList<>();
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    class HttpFileTask extends AsyncTask<Void, Void, Boolean> {
        private int currentAccount;
        private String ext;
        private int fileSize;
        private long lastProgressTime;
        private File tempFile;
        private String url;
        private RandomAccessFile fileOutputStream = null;
        private boolean canRetry = true;

        public HttpFileTask(String str, File file, String str2, int i) {
            this.url = str;
            this.tempFile = file;
            this.ext = str2;
            this.currentAccount = i;
        }

        private void reportProgress(final long j, final long j2) {
            long jElapsedRealtime = SystemClock.elapsedRealtime();
            if (j != j2) {
                long j3 = this.lastProgressTime;
                if (j3 != 0 && j3 >= jElapsedRealtime - 100) {
                    return;
                }
            }
            this.lastProgressTime = jElapsedRealtime;
            Utilities.stageQueue.postRunnable(new Runnable() { // from class: org.telegram.messenger.ImageLoader$HttpFileTask$$ExternalSyntheticLambda1
                @Override // java.lang.Runnable
                public final void run() {
                    this.f$0.lambda$reportProgress$1(j, j2);
                }
            });
        }

        /* JADX INFO: Access modifiers changed from: private */
        public /* synthetic */ void lambda$reportProgress$1(final long j, final long j2) {
            ImageLoader.this.fileProgresses.put(this.url, new long[]{j, j2});
            AndroidUtilities.runOnUIThread(new Runnable() { // from class: org.telegram.messenger.ImageLoader$HttpFileTask$$ExternalSyntheticLambda0
                @Override // java.lang.Runnable
                public final void run() {
                    this.f$0.lambda$reportProgress$0(j, j2);
                }
            });
        }

        /* JADX INFO: Access modifiers changed from: private */
        public /* synthetic */ void lambda$reportProgress$0(long j, long j2) {
            NotificationCenter.getInstance(this.currentAccount).lambda$postNotificationNameOnUIThread$1(NotificationCenter.fileLoadProgressChanged, this.url, Long.valueOf(j), Long.valueOf(j2));
        }

        /* JADX INFO: Access modifiers changed from: protected */
        @Override // android.os.AsyncTask
        public Boolean doInBackground(Void... voidArr) {
            InputStream inputStream;
            URLConnection uRLConnectionOpenConnection;
            InputStream inputStream2;
            List<String> list;
            String str;
            int responseCode;
            boolean z = true;
            boolean z2 = false;
            try {
                uRLConnectionOpenConnection = new URL(this.url).openConnection();
                try {
                    uRLConnectionOpenConnection.addRequestProperty("User-Agent", "Mozilla/5.0 (iPhone; CPU iPhone OS 10_0 like Mac OS X) AppleWebKit/602.1.38 (KHTML, like Gecko) Version/10.0 Mobile/14A5297c Safari/602.1");
                    uRLConnectionOpenConnection.setConnectTimeout(5000);
                    uRLConnectionOpenConnection.setReadTimeout(5000);
                    if (uRLConnectionOpenConnection instanceof HttpURLConnection) {
                        HttpURLConnection httpURLConnection = (HttpURLConnection) uRLConnectionOpenConnection;
                        httpURLConnection.setInstanceFollowRedirects(true);
                        int responseCode2 = httpURLConnection.getResponseCode();
                        if (responseCode2 == 302 || responseCode2 == 301 || responseCode2 == 303) {
                            String headerField = httpURLConnection.getHeaderField("Location");
                            String headerField2 = httpURLConnection.getHeaderField("Set-Cookie");
                            uRLConnectionOpenConnection = new URL(headerField).openConnection();
                            uRLConnectionOpenConnection.setRequestProperty("Cookie", headerField2);
                            uRLConnectionOpenConnection.addRequestProperty("User-Agent", "Mozilla/5.0 (iPhone; CPU iPhone OS 10_0 like Mac OS X) AppleWebKit/602.1.38 (KHTML, like Gecko) Version/10.0 Mobile/14A5297c Safari/602.1");
                        }
                    }
                    uRLConnectionOpenConnection.connect();
                    inputStream2 = uRLConnectionOpenConnection.getInputStream();
                    try {
                        this.fileOutputStream = new RandomAccessFile(this.tempFile, "rws");
                    } catch (Throwable th) {
                        inputStream = inputStream2;
                        th = th;
                        if (th instanceof SocketTimeoutException) {
                            if (ApplicationLoader.isNetworkOnline()) {
                                this.canRetry = false;
                            }
                        } else if (th instanceof UnknownHostException) {
                            this.canRetry = false;
                        } else if (th instanceof SocketException) {
                            if (th.getMessage() != null && th.getMessage().contains("ECONNRESET")) {
                                this.canRetry = false;
                            }
                        } else if (th instanceof FileNotFoundException) {
                            this.canRetry = false;
                        }
                        FileLog.e(th);
                        inputStream2 = inputStream;
                    }
                } catch (Throwable th2) {
                    th = th2;
                    inputStream = null;
                }
            } catch (Throwable th3) {
                th = th3;
                inputStream = null;
                uRLConnectionOpenConnection = null;
            }
            if (this.canRetry) {
                try {
                    if ((uRLConnectionOpenConnection instanceof HttpURLConnection) && (responseCode = ((HttpURLConnection) uRLConnectionOpenConnection).getResponseCode()) != 200 && responseCode != 202 && responseCode != 304) {
                        this.canRetry = false;
                    }
                } catch (Exception e) {
                    FileLog.e(e);
                }
                if (uRLConnectionOpenConnection != null) {
                    try {
                        Map<String, List<String>> headerFields = uRLConnectionOpenConnection.getHeaderFields();
                        if (headerFields != null && (list = headerFields.get("content-Length")) != null && !list.isEmpty() && (str = list.get(0)) != null) {
                            this.fileSize = Utilities.parseInt((CharSequence) str).intValue();
                        }
                    } catch (Exception e2) {
                        FileLog.e(e2);
                    }
                }
                if (inputStream2 != null) {
                    try {
                        byte[] bArr = new byte[32768];
                        int i = 0;
                        while (true) {
                            try {
                                if (!isCancelled()) {
                                    try {
                                        int i2 = inputStream2.read(bArr);
                                        if (i2 > 0) {
                                            this.fileOutputStream.write(bArr, 0, i2);
                                            i += i2;
                                            int i3 = this.fileSize;
                                            if (i3 > 0) {
                                                reportProgress(i, i3);
                                            }
                                        } else {
                                            if (i2 == -1) {
                                                try {
                                                    int i4 = this.fileSize;
                                                    if (i4 != 0) {
                                                        reportProgress(i4, i4);
                                                    }
                                                } catch (Exception e3) {
                                                    e = e3;
                                                    FileLog.e(e);
                                                }
                                            }
                                            z2 = z;
                                        }
                                    } catch (Exception e4) {
                                        e = e4;
                                        z = false;
                                    }
                                    FileLog.e(e);
                                    z2 = z;
                                }
                                z = false;
                            } catch (Throwable th4) {
                                th = th4;
                                FileLog.e(th);
                            }
                            z2 = z;
                        }
                    } catch (Throwable th5) {
                        th = th5;
                        z = false;
                    }
                }
                try {
                    RandomAccessFile randomAccessFile = this.fileOutputStream;
                    if (randomAccessFile != null) {
                        randomAccessFile.close();
                        this.fileOutputStream = null;
                    }
                } catch (Throwable th6) {
                    FileLog.e(th6);
                }
                if (inputStream2 != null) {
                    try {
                        inputStream2.close();
                    } catch (Throwable th7) {
                        FileLog.e(th7);
                    }
                }
            }
            return Boolean.valueOf(z2);
        }

        /* JADX INFO: Access modifiers changed from: protected */
        @Override // android.os.AsyncTask
        public void onPostExecute(Boolean bool) {
            ImageLoader.this.runHttpFileLoadTasks(this, bool.booleanValue() ? 2 : 1);
        }

        @Override // android.os.AsyncTask
        protected void onCancelled() {
            ImageLoader.this.runHttpFileLoadTasks(this, 2);
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    class ArtworkLoadTask extends AsyncTask<Void, Void, String> {
        private CacheImage cacheImage;
        private boolean canRetry = true;
        private HttpURLConnection httpConnection;
        private boolean small;

        public ArtworkLoadTask(CacheImage cacheImage) {
            this.cacheImage = cacheImage;
            this.small = Uri.parse(cacheImage.imageLocation.path).getQueryParameter("s") != null;
        }

        /* JADX INFO: Access modifiers changed from: protected */
        @Override // android.os.AsyncTask
        public String doInBackground(Void... voidArr) {
            InputStream inputStream;
            ByteArrayOutputStream byteArrayOutputStream;
            int i;
            int responseCode;
            try {
                try {
                    HttpURLConnection httpURLConnection = (HttpURLConnection) new URL(this.cacheImage.imageLocation.path.replace("athumb://", "https://")).openConnection();
                    this.httpConnection = httpURLConnection;
                    httpURLConnection.setConnectTimeout(5000);
                    this.httpConnection.setReadTimeout(5000);
                    this.httpConnection.connect();
                    try {
                        HttpURLConnection httpURLConnection2 = this.httpConnection;
                        if (httpURLConnection2 != null && (responseCode = httpURLConnection2.getResponseCode()) != 200 && responseCode != 202 && responseCode != 304) {
                            this.canRetry = false;
                        }
                    } catch (Exception e) {
                        FileLog.e(e);
                    }
                    InputStream inputStream2 = this.httpConnection.getInputStream();
                    try {
                        ByteArrayOutputStream byteArrayOutputStream2 = new ByteArrayOutputStream();
                        try {
                            byte[] bArr = new byte[32768];
                            while (!isCancelled() && (i = inputStream2.read(bArr)) > 0) {
                                byteArrayOutputStream2.write(bArr, 0, i);
                            }
                            this.canRetry = false;
                            JSONArray jSONArray = new JSONObject(new String(byteArrayOutputStream2.toByteArray())).getJSONArray("results");
                            if (jSONArray.length() > 0) {
                                String string = jSONArray.getJSONObject(0).getString("artworkUrl100");
                                if (this.small) {
                                    try {
                                        HttpURLConnection httpURLConnection3 = this.httpConnection;
                                        if (httpURLConnection3 != null) {
                                            httpURLConnection3.disconnect();
                                        }
                                    } catch (Throwable unused) {
                                    }
                                    if (inputStream2 != null) {
                                        try {
                                            inputStream2.close();
                                        } catch (Throwable th) {
                                            FileLog.e(th);
                                        }
                                    }
                                    try {
                                        byteArrayOutputStream2.close();
                                    } catch (Exception unused2) {
                                    }
                                    return string;
                                }
                                String strReplace = string.replace("100x100", "600x600");
                                try {
                                    HttpURLConnection httpURLConnection4 = this.httpConnection;
                                    if (httpURLConnection4 != null) {
                                        httpURLConnection4.disconnect();
                                    }
                                } catch (Throwable unused3) {
                                }
                                if (inputStream2 != null) {
                                    try {
                                        inputStream2.close();
                                    } catch (Throwable th2) {
                                        FileLog.e(th2);
                                    }
                                }
                                try {
                                    byteArrayOutputStream2.close();
                                } catch (Exception unused4) {
                                }
                                return strReplace;
                            }
                            try {
                                HttpURLConnection httpURLConnection5 = this.httpConnection;
                                if (httpURLConnection5 != null) {
                                    httpURLConnection5.disconnect();
                                }
                            } catch (Throwable unused5) {
                            }
                            if (inputStream2 != null) {
                                try {
                                    inputStream2.close();
                                } catch (Throwable th3) {
                                    FileLog.e(th3);
                                }
                            }
                            byteArrayOutputStream2.close();
                        } catch (Throwable th4) {
                            inputStream = inputStream2;
                            th = th4;
                            byteArrayOutputStream = byteArrayOutputStream2;
                            try {
                                if (th instanceof SocketTimeoutException) {
                                    if (ApplicationLoader.isNetworkOnline()) {
                                        this.canRetry = false;
                                    }
                                } else if (th instanceof UnknownHostException) {
                                    this.canRetry = false;
                                } else if (th instanceof SocketException) {
                                    if (th.getMessage() != null && th.getMessage().contains("ECONNRESET")) {
                                        this.canRetry = false;
                                    }
                                } else if (th instanceof FileNotFoundException) {
                                    this.canRetry = false;
                                }
                                FileLog.e(th);
                                try {
                                    HttpURLConnection httpURLConnection6 = this.httpConnection;
                                    if (httpURLConnection6 != null) {
                                        httpURLConnection6.disconnect();
                                    }
                                } catch (Throwable unused6) {
                                }
                                if (inputStream != null) {
                                    try {
                                        inputStream.close();
                                    } catch (Throwable th5) {
                                        FileLog.e(th5);
                                    }
                                }
                                if (byteArrayOutputStream != null) {
                                }
                                return null;
                            } finally {
                                try {
                                    HttpURLConnection httpURLConnection7 = this.httpConnection;
                                    if (httpURLConnection7 != null) {
                                        httpURLConnection7.disconnect();
                                    }
                                } catch (Throwable unused7) {
                                }
                                if (inputStream != null) {
                                    try {
                                        inputStream.close();
                                    } catch (Throwable th6) {
                                        FileLog.e(th6);
                                    }
                                }
                                if (byteArrayOutputStream != null) {
                                    try {
                                        byteArrayOutputStream.close();
                                    } catch (Exception unused8) {
                                    }
                                }
                            }
                        }
                    } catch (Throwable th7) {
                        inputStream = inputStream2;
                        th = th7;
                        byteArrayOutputStream = null;
                    }
                } catch (Throwable th8) {
                    th = th8;
                    inputStream = null;
                    byteArrayOutputStream = null;
                }
            } catch (Exception unused9) {
            }
            return null;
        }

        /* JADX INFO: Access modifiers changed from: protected */
        @Override // android.os.AsyncTask
        public void onPostExecute(final String str) {
            if (str != null) {
                ImageLoader.this.imageLoadQueue.postRunnable(new Runnable() { // from class: org.telegram.messenger.ImageLoader$ArtworkLoadTask$$ExternalSyntheticLambda1
                    @Override // java.lang.Runnable
                    public final void run() {
                        this.f$0.lambda$onPostExecute$0(str);
                    }
                });
            } else if (this.canRetry) {
                ImageLoader.this.artworkLoadError(this.cacheImage.url);
            }
            ImageLoader.this.imageLoadQueue.postRunnable(new Runnable() { // from class: org.telegram.messenger.ImageLoader$ArtworkLoadTask$$ExternalSyntheticLambda2
                @Override // java.lang.Runnable
                public final void run() {
                    this.f$0.lambda$onPostExecute$1();
                }
            });
        }

        /* JADX INFO: Access modifiers changed from: private */
        public /* synthetic */ void lambda$onPostExecute$0(String str) {
            CacheImage cacheImage = this.cacheImage;
            cacheImage.httpTask = ImageLoader.this.new HttpImageTask(cacheImage, 0, str);
            ImageLoader.this.httpTasks.add(this.cacheImage.httpTask);
            ImageLoader.this.runHttpTasks(false);
        }

        /* JADX INFO: Access modifiers changed from: private */
        public /* synthetic */ void lambda$onPostExecute$1() {
            ImageLoader.this.runArtworkTasks(true);
        }

        /* JADX INFO: Access modifiers changed from: private */
        public /* synthetic */ void lambda$onCancelled$2() {
            ImageLoader.this.runArtworkTasks(true);
        }

        @Override // android.os.AsyncTask
        protected void onCancelled() {
            ImageLoader.this.imageLoadQueue.postRunnable(new Runnable() { // from class: org.telegram.messenger.ImageLoader$ArtworkLoadTask$$ExternalSyntheticLambda0
                @Override // java.lang.Runnable
                public final void run() {
                    this.f$0.lambda$onCancelled$2();
                }
            });
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    class HttpImageTask extends AsyncTask<Void, Void, Boolean> {
        private CacheImage cacheImage;
        private boolean canRetry = true;
        private RandomAccessFile fileOutputStream;
        private HttpURLConnection httpConnection;
        private long imageSize;
        private long lastProgressTime;
        private String overrideUrl;

        /* JADX INFO: renamed from: $r8$lambda$cv6vUIEcOP54Auh2HI4OW-QaCg0, reason: not valid java name */
        public static /* synthetic */ void m3048$r8$lambda$cv6vUIEcOP54Auh2HI4OWQaCg0(TLObject tLObject, TLRPC.TL_error tL_error) {
        }

        public HttpImageTask(CacheImage cacheImage, long j) {
            this.cacheImage = cacheImage;
            this.imageSize = j;
        }

        public HttpImageTask(CacheImage cacheImage, int i, String str) {
            this.cacheImage = cacheImage;
            this.imageSize = i;
            this.overrideUrl = str;
        }

        private void reportProgress(final long j, final long j2) {
            long jElapsedRealtime = SystemClock.elapsedRealtime();
            if (j != j2) {
                long j3 = this.lastProgressTime;
                if (j3 != 0 && j3 >= jElapsedRealtime - 100) {
                    return;
                }
            }
            this.lastProgressTime = jElapsedRealtime;
            Utilities.stageQueue.postRunnable(new Runnable() { // from class: org.telegram.messenger.ImageLoader$HttpImageTask$$ExternalSyntheticLambda7
                @Override // java.lang.Runnable
                public final void run() {
                    this.f$0.lambda$reportProgress$1(j, j2);
                }
            });
        }

        /* JADX INFO: Access modifiers changed from: private */
        public /* synthetic */ void lambda$reportProgress$1(final long j, final long j2) {
            ImageLoader.this.fileProgresses.put(this.cacheImage.url, new long[]{j, j2});
            AndroidUtilities.runOnUIThread(new Runnable() { // from class: org.telegram.messenger.ImageLoader$HttpImageTask$$ExternalSyntheticLambda6
                @Override // java.lang.Runnable
                public final void run() {
                    this.f$0.lambda$reportProgress$0(j, j2);
                }
            });
        }

        /* JADX INFO: Access modifiers changed from: private */
        public /* synthetic */ void lambda$reportProgress$0(long j, long j2) {
            NotificationCenter.getInstance(this.cacheImage.currentAccount).lambda$postNotificationNameOnUIThread$1(NotificationCenter.fileLoadProgressChanged, this.cacheImage.url, Long.valueOf(j), Long.valueOf(j2));
        }

        /* JADX INFO: Access modifiers changed from: protected */
        /* JADX WARN: Code duplicated, block: B:111:0x018f A[Catch: all -> 0x0195, TRY_LEAVE, TryCatch #3 {all -> 0x0195, blocks: (B:109:0x018b, B:111:0x018f), top: B:138:0x018b }] */
        /* JADX WARN: Code duplicated, block: B:117:0x019d A[Catch: all -> 0x01a0, TRY_LEAVE, TryCatch #0 {all -> 0x01a0, blocks: (B:115:0x0199, B:117:0x019d), top: B:132:0x0199 }] */
        /* JADX WARN: Code duplicated, block: B:142:0x01a2 A[EXC_TOP_SPLITTER, SYNTHETIC] */
        /* JADX WARN: Code duplicated, block: B:47:0x00e4  */
        @Override // android.os.AsyncTask
        public Boolean doInBackground(Void... voidArr) {
            InputStream inputStream;
            InputStream inputStream2;
            WebFile webFile;
            HttpURLConnection httpURLConnection;
            List<String> list;
            String str;
            int responseCode;
            CacheImage cacheImage;
            File file;
            HttpURLConnection httpURLConnection2;
            RandomAccessFile randomAccessFile;
            boolean z = true;
            boolean z2 = false;
            if (isCancelled()) {
                inputStream2 = null;
            } else {
                try {
                    String str2 = this.cacheImage.imageLocation.path;
                    if (str2.startsWith("https://static-maps") || str2.startsWith("https://maps.googleapis")) {
                        int i = MessagesController.getInstance(this.cacheImage.currentAccount).mapProvider;
                        if ((i == 3 || i == 4) && (webFile = (WebFile) ImageLoader.this.testWebFile.get(str2)) != null) {
                            TLRPC.TL_upload_getWebFile tL_upload_getWebFile = new TLRPC.TL_upload_getWebFile();
                            tL_upload_getWebFile.location = webFile.location;
                            tL_upload_getWebFile.offset = 0;
                            tL_upload_getWebFile.limit = 0;
                            ConnectionsManager.getInstance(this.cacheImage.currentAccount).sendRequest(tL_upload_getWebFile, new RequestDelegate() { // from class: org.telegram.messenger.ImageLoader$HttpImageTask$$ExternalSyntheticLambda0
                                @Override // org.telegram.tgnet.RequestDelegate
                                public final void run(TLObject tLObject, TLRPC.TL_error tL_error) {
                                    ImageLoader.HttpImageTask.m3048$r8$lambda$cv6vUIEcOP54Auh2HI4OWQaCg0(tLObject, tL_error);
                                }
                            });
                        }
                    }
                    String str3 = this.overrideUrl;
                    if (str3 != null) {
                        str2 = str3;
                    }
                    HttpURLConnection httpURLConnection3 = (HttpURLConnection) new URL(str2).openConnection();
                    this.httpConnection = httpURLConnection3;
                    httpURLConnection3.addRequestProperty("User-Agent", "Mozilla/5.0 (iPhone; CPU iPhone OS 10_0 like Mac OS X) AppleWebKit/602.1.38 (KHTML, like Gecko) Version/10.0 Mobile/14A5297c Safari/602.1");
                    this.httpConnection.setConnectTimeout(5000);
                    this.httpConnection.setReadTimeout(5000);
                    this.httpConnection.setInstanceFollowRedirects(true);
                    if (isCancelled()) {
                        inputStream2 = null;
                    } else {
                        this.httpConnection.connect();
                        inputStream2 = this.httpConnection.getInputStream();
                        try {
                            this.fileOutputStream = new RandomAccessFile(this.cacheImage.tempFilePath, "rws");
                        } catch (Throwable th) {
                            inputStream = inputStream2;
                            th = th;
                            if (th instanceof SocketTimeoutException) {
                                if (ApplicationLoader.isNetworkOnline()) {
                                    this.canRetry = false;
                                }
                            } else if (th instanceof UnknownHostException) {
                                this.canRetry = false;
                            } else if (th instanceof SocketException) {
                                if (th.getMessage() != null && th.getMessage().contains("ECONNRESET")) {
                                    this.canRetry = false;
                                }
                            } else if (th instanceof FileNotFoundException) {
                                this.canRetry = false;
                            }
                            FileLog.e(th);
                            inputStream2 = inputStream;
                        }
                    }
                } catch (Throwable th2) {
                    th = th2;
                    inputStream = null;
                }
            }
            if (!isCancelled()) {
                try {
                    HttpURLConnection httpURLConnection4 = this.httpConnection;
                    if (httpURLConnection4 != null && (responseCode = httpURLConnection4.getResponseCode()) != 200 && responseCode != 202 && responseCode != 304) {
                        this.canRetry = false;
                    }
                } catch (Exception e) {
                    FileLog.e(e);
                }
                if (this.imageSize == 0 && (httpURLConnection = this.httpConnection) != null) {
                    try {
                        Map<String, List<String>> headerFields = httpURLConnection.getHeaderFields();
                        if (headerFields != null && (list = headerFields.get("content-Length")) != null && !list.isEmpty() && (str = list.get(0)) != null) {
                            this.imageSize = Utilities.parseInt((CharSequence) str).intValue();
                        }
                    } catch (Exception e2) {
                        FileLog.e(e2);
                    }
                }
                if (inputStream2 != null) {
                    try {
                        byte[] bArr = new byte[8192];
                        int i2 = 0;
                        while (true) {
                            if (!isCancelled()) {
                                try {
                                    int i3 = inputStream2.read(bArr);
                                    if (i3 > 0) {
                                        i2 += i3;
                                        this.fileOutputStream.write(bArr, 0, i3);
                                        long j = this.imageSize;
                                        if (j != 0) {
                                            reportProgress(i2, j);
                                        }
                                    } else if (i3 == -1) {
                                        try {
                                            long j2 = this.imageSize;
                                            if (j2 == 0) {
                                                break;
                                            }
                                            reportProgress(j2, j2);
                                            break;
                                        } catch (Exception e3) {
                                            z2 = true;
                                            e = e3;
                                            FileLog.e(e);
                                            z = z2;
                                            break;
                                        } catch (Throwable th3) {
                                            z2 = true;
                                            th = th3;
                                            FileLog.e(th);
                                            randomAccessFile = this.fileOutputStream;
                                            if (randomAccessFile != null) {
                                                randomAccessFile.close();
                                                this.fileOutputStream = null;
                                            }
                                            httpURLConnection2 = this.httpConnection;
                                            if (httpURLConnection2 != null) {
                                                httpURLConnection2.disconnect();
                                            }
                                            if (inputStream2 != null) {
                                                try {
                                                    inputStream2.close();
                                                } catch (Throwable th4) {
                                                    FileLog.e(th4);
                                                }
                                            }
                                            if (z2) {
                                                CacheImage cacheImage2 = this.cacheImage;
                                                cacheImage2.finalFilePath = cacheImage2.tempFilePath;
                                            }
                                            return Boolean.valueOf(z2);
                                        }
                                    }
                                } catch (Exception e4) {
                                    e = e4;
                                }
                                FileLog.e(e);
                            }
                            z = z2;
                            break;
                        }
                        z2 = z;
                    } catch (Throwable th5) {
                        th = th5;
                    }
                }
            }
            try {
                randomAccessFile = this.fileOutputStream;
                if (randomAccessFile != null) {
                    randomAccessFile.close();
                    this.fileOutputStream = null;
                }
            } catch (Throwable th6) {
                FileLog.e(th6);
            }
            try {
                httpURLConnection2 = this.httpConnection;
                if (httpURLConnection2 != null) {
                    httpURLConnection2.disconnect();
                }
            } catch (Throwable unused) {
            }
            if (inputStream2 != null) {
                inputStream2.close();
            }
            if (z2 && (file = (cacheImage = this.cacheImage).tempFilePath) != null && !file.renameTo(cacheImage.finalFilePath)) {
                CacheImage cacheImage3 = this.cacheImage;
                cacheImage3.finalFilePath = cacheImage3.tempFilePath;
            }
            return Boolean.valueOf(z2);
        }

        /* JADX INFO: Access modifiers changed from: protected */
        @Override // android.os.AsyncTask
        public void onPostExecute(final Boolean bool) {
            if (bool.booleanValue() || !this.canRetry) {
                ImageLoader imageLoader = ImageLoader.this;
                CacheImage cacheImage = this.cacheImage;
                imageLoader.fileDidLoaded(cacheImage.url, cacheImage.finalFilePath, 0);
            } else {
                ImageLoader.this.httpFileLoadError(this.cacheImage.url);
            }
            Utilities.stageQueue.postRunnable(new Runnable() { // from class: org.telegram.messenger.ImageLoader$HttpImageTask$$ExternalSyntheticLambda4
                @Override // java.lang.Runnable
                public final void run() {
                    this.f$0.lambda$onPostExecute$4(bool);
                }
            });
            ImageLoader.this.imageLoadQueue.postRunnable(new Runnable() { // from class: org.telegram.messenger.ImageLoader$HttpImageTask$$ExternalSyntheticLambda5
                @Override // java.lang.Runnable
                public final void run() {
                    this.f$0.lambda$onPostExecute$5();
                }
            }, this.cacheImage.priority);
        }

        /* JADX INFO: Access modifiers changed from: private */
        public /* synthetic */ void lambda$onPostExecute$4(final Boolean bool) {
            ImageLoader.this.fileProgresses.remove(this.cacheImage.url);
            AndroidUtilities.runOnUIThread(new Runnable() { // from class: org.telegram.messenger.ImageLoader$HttpImageTask$$ExternalSyntheticLambda3
                @Override // java.lang.Runnable
                public final void run() {
                    this.f$0.lambda$onPostExecute$3(bool);
                }
            });
        }

        /* JADX INFO: Access modifiers changed from: private */
        public /* synthetic */ void lambda$onPostExecute$3(Boolean bool) {
            if (bool.booleanValue()) {
                NotificationCenter notificationCenter = NotificationCenter.getInstance(this.cacheImage.currentAccount);
                int i = NotificationCenter.fileLoaded;
                CacheImage cacheImage = this.cacheImage;
                notificationCenter.lambda$postNotificationNameOnUIThread$1(i, cacheImage.url, cacheImage.finalFilePath);
                return;
            }
            NotificationCenter.getInstance(this.cacheImage.currentAccount).lambda$postNotificationNameOnUIThread$1(NotificationCenter.fileLoadFailed, this.cacheImage.url, 2);
        }

        /* JADX INFO: Access modifiers changed from: private */
        public /* synthetic */ void lambda$onPostExecute$5() {
            ImageLoader.this.runHttpTasks(true);
        }

        /* JADX INFO: Access modifiers changed from: private */
        public /* synthetic */ void lambda$onCancelled$6() {
            ImageLoader.this.runHttpTasks(true);
        }

        @Override // android.os.AsyncTask
        protected void onCancelled() {
            ImageLoader.this.imageLoadQueue.postRunnable(new Runnable() { // from class: org.telegram.messenger.ImageLoader$HttpImageTask$$ExternalSyntheticLambda1
                @Override // java.lang.Runnable
                public final void run() {
                    this.f$0.lambda$onCancelled$6();
                }
            }, this.cacheImage.priority);
            Utilities.stageQueue.postRunnable(new Runnable() { // from class: org.telegram.messenger.ImageLoader$HttpImageTask$$ExternalSyntheticLambda2
                @Override // java.lang.Runnable
                public final void run() {
                    this.f$0.lambda$onCancelled$8();
                }
            });
        }

        /* JADX INFO: Access modifiers changed from: private */
        public /* synthetic */ void lambda$onCancelled$8() {
            ImageLoader.this.fileProgresses.remove(this.cacheImage.url);
            AndroidUtilities.runOnUIThread(new Runnable() { // from class: org.telegram.messenger.ImageLoader$HttpImageTask$$ExternalSyntheticLambda8
                @Override // java.lang.Runnable
                public final void run() {
                    this.f$0.lambda$onCancelled$7();
                }
            });
        }

        /* JADX INFO: Access modifiers changed from: private */
        public /* synthetic */ void lambda$onCancelled$7() {
            NotificationCenter.getInstance(this.cacheImage.currentAccount).lambda$postNotificationNameOnUIThread$1(NotificationCenter.fileLoadFailed, this.cacheImage.url, 1);
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    class ThumbGenerateTask implements Runnable {
        private ThumbGenerateInfo info;
        private int mediaType;
        private File originalPath;

        public ThumbGenerateTask(int i, File file, ThumbGenerateInfo thumbGenerateInfo) {
            this.mediaType = i;
            this.originalPath = file;
            this.info = thumbGenerateInfo;
        }

        private void removeTask() {
            ThumbGenerateInfo thumbGenerateInfo = this.info;
            if (thumbGenerateInfo == null) {
                return;
            }
            final String attachFileName = FileLoader.getAttachFileName(thumbGenerateInfo.parentDocument);
            ImageLoader.this.imageLoadQueue.postRunnable(new Runnable() { // from class: org.telegram.messenger.ImageLoader$ThumbGenerateTask$$ExternalSyntheticLambda1
                @Override // java.lang.Runnable
                public final void run() {
                    this.f$0.lambda$removeTask$0(attachFileName);
                }
            });
        }

        /* JADX INFO: Access modifiers changed from: private */
        public /* synthetic */ void lambda$removeTask$0(String str) {
            ImageLoader.this.thumbGenerateTasks.remove(str);
        }

        @Override // java.lang.Runnable
        public void run() {
            int iMin;
            Bitmap bitmapCreateScaledBitmap;
            try {
                if (this.info == null) {
                    removeTask();
                    return;
                }
                final String str = "q_" + this.info.parentDocument.dc_id + "_" + this.info.parentDocument.id;
                File file = new File(FileLoader.getDirectory(4), str + ".jpg");
                if (!file.exists() && this.originalPath.exists()) {
                    if (this.info.big) {
                        Point point = AndroidUtilities.displaySize;
                        iMin = Math.max(point.x, point.y);
                    } else {
                        Point point2 = AndroidUtilities.displaySize;
                        iMin = Math.min(180, Math.min(point2.x, point2.y) / 4);
                    }
                    int i = this.mediaType;
                    Bitmap bitmapLoadBitmap = null;
                    if (i == 0) {
                        float f = iMin;
                        bitmapLoadBitmap = ImageLoader.loadBitmap(this.originalPath.toString(), null, f, f, false);
                    } else {
                        int i2 = 2;
                        if (i == 2) {
                            String string = this.originalPath.toString();
                            if (!this.info.big) {
                                i2 = 1;
                            }
                            bitmapLoadBitmap = SendMessagesHelper.createVideoThumbnail(string, i2);
                        } else if (i == 3) {
                            String lowerCase = this.originalPath.toString().toLowerCase();
                            if (lowerCase.endsWith("mp4")) {
                                String string2 = this.originalPath.toString();
                                if (!this.info.big) {
                                    i2 = 1;
                                }
                                bitmapLoadBitmap = SendMessagesHelper.createVideoThumbnail(string2, i2);
                            } else if (lowerCase.endsWith(".jpg") || lowerCase.endsWith(".jpeg") || lowerCase.endsWith(".png") || lowerCase.endsWith(".gif")) {
                                float f2 = iMin;
                                bitmapLoadBitmap = ImageLoader.loadBitmap(lowerCase, null, f2, f2, false);
                            }
                        }
                    }
                    if (bitmapLoadBitmap == null) {
                        removeTask();
                        return;
                    }
                    int width = bitmapLoadBitmap.getWidth();
                    int height = bitmapLoadBitmap.getHeight();
                    if (width != 0 && height != 0) {
                        float f3 = width;
                        float f4 = iMin;
                        float f5 = height;
                        float fMin = Math.min(f3 / f4, f5 / f4);
                        if (fMin > 1.0f && (bitmapCreateScaledBitmap = Bitmaps.createScaledBitmap(bitmapLoadBitmap, (int) (f3 / fMin), (int) (f5 / fMin), true)) != bitmapLoadBitmap) {
                            bitmapLoadBitmap.recycle();
                            bitmapLoadBitmap = bitmapCreateScaledBitmap;
                        }
                        FileOutputStream fileOutputStream = new FileOutputStream(file);
                        bitmapLoadBitmap.compress(Bitmap.CompressFormat.JPEG, this.info.big ? 83 : 60, fileOutputStream);
                        try {
                            fileOutputStream.close();
                        } catch (Exception e) {
                            FileLog.e(e);
                        }
                        final BitmapDrawable bitmapDrawable = new BitmapDrawable(bitmapLoadBitmap);
                        final ArrayList arrayList = new ArrayList(this.info.imageReceiverArray);
                        final ArrayList arrayList2 = new ArrayList(this.info.imageReceiverGuidsArray);
                        AndroidUtilities.runOnUIThread(new Runnable() { // from class: org.telegram.messenger.ImageLoader$ThumbGenerateTask$$ExternalSyntheticLambda0
                            @Override // java.lang.Runnable
                            public final void run() {
                                this.f$0.lambda$run$1(str, arrayList, bitmapDrawable, arrayList2);
                            }
                        });
                        return;
                    }
                    removeTask();
                    return;
                }
                removeTask();
            } catch (Throwable th) {
                FileLog.e(th);
                removeTask();
            }
        }

        /* JADX INFO: Access modifiers changed from: private */
        public /* synthetic */ void lambda$run$1(String str, ArrayList arrayList, BitmapDrawable bitmapDrawable, ArrayList arrayList2) {
            removeTask();
            if (this.info.filter != null) {
                str = str + "@" + this.info.filter;
            }
            String str2 = str;
            for (int i = 0; i < arrayList.size(); i++) {
                ((ImageReceiver) arrayList.get(i)).setImageBitmapByKey(bitmapDrawable, str2, 0, false, ((Integer) arrayList2.get(i)).intValue());
            }
            if (str2.contains("nocache")) {
                return;
            }
            ImageLoader.this.memCache.put(str2, bitmapDrawable);
        }
    }

    public static String decompressGzip(File file) {
        StringBuilder sb = new StringBuilder();
        if (file == null) {
            return _UrlKt.FRAGMENT_ENCODE_SET;
        }
        try {
            GZIPInputStream gZIPInputStream = new GZIPInputStream(new FileInputStream(file));
            try {
                BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(gZIPInputStream, "UTF-8"));
                while (true) {
                    try {
                        String line = bufferedReader.readLine();
                        if (line != null) {
                            sb.append(line);
                        } else {
                            String string = sb.toString();
                            bufferedReader.close();
                            gZIPInputStream.close();
                            return string;
                        }
                    } catch (Throwable th) {
                        try {
                            bufferedReader.close();
                        } catch (Throwable th2) {
                            th.addSuppressed(th2);
                        }
                        throw th;
                    }
                    try {
                        gZIPInputStream.close();
                    } catch (Throwable th3) {
                        th.addSuppressed(th3);
                    }
                    throw th;
                }
            } catch (Throwable th4) {
                gZIPInputStream.close();
                throw th4;
            }
        } catch (Exception unused) {
            return _UrlKt.FRAGMENT_ENCODE_SET;
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    class CacheOutTask implements Runnable {
        private CacheImage cacheImage;
        private boolean isCancelled;
        private Thread runningThread;
        private final Object sync = new Object();

        public CacheOutTask(CacheImage cacheImage) {
            this.cacheImage = cacheImage;
        }

        /* JADX WARN: Not initialized variable reg: 22, insn: 0x06bb: MOVE (r4 I:??[int, float, boolean, short, byte, char, OBJECT, ARRAY]) = (r22 I:??[int, float, boolean, short, byte, char, OBJECT, ARRAY]), block:B:421:0x06bb */
        /*  JADX ERROR: Type inference failed
            jadx.core.utils.exceptions.JadxOverflowException: Type inference error: updates count limit reached with updateSeq = 37181. Try increasing type updates limit count.
            	at jadx.core.utils.ErrorsCounter.addError(ErrorsCounter.java:59)
            	at jadx.core.utils.ErrorsCounter.error(ErrorsCounter.java:31)
            	at jadx.core.dex.attributes.nodes.NotificationAttrNode.addError(NotificationAttrNode.java:19)
            	at jadx.core.dex.visitors.typeinference.TypeInferenceVisitor.visit(TypeInferenceVisitor.java:79)
            */
        @Override // java.lang.Runnable
        public void run() {
            /*
                Method dump skipped, instruction units count: 3718
                To view this dump add '--comments-level debug' option
            */
            throw new UnsupportedOperationException("Method not decompiled: org.telegram.messenger.ImageLoader.CacheOutTask.run():void");
        }

        private Bitmap applyWallpaperSetting(Bitmap bitmap, TLRPC.WallPaper wallPaper) {
            int patternColor;
            if (!wallPaper.pattern || wallPaper.settings == null) {
                TLRPC.WallPaperSettings wallPaperSettings = wallPaper.settings;
                return (wallPaperSettings == null || !wallPaperSettings.blur) ? bitmap : Utilities.blurWallpaper(bitmap);
            }
            Bitmap bitmapCreateBitmap = Bitmap.createBitmap(bitmap.getWidth(), bitmap.getHeight(), Bitmap.Config.ARGB_8888);
            Canvas canvas = new Canvas(bitmapCreateBitmap);
            TLRPC.WallPaperSettings wallPaperSettings2 = wallPaper.settings;
            boolean z = true;
            if (wallPaperSettings2.second_background_color == 0) {
                patternColor = AndroidUtilities.getPatternColor(wallPaperSettings2.background_color);
                canvas.drawColor(ColorUtils.setAlphaComponent(wallPaper.settings.background_color, 255));
            } else if (wallPaperSettings2.third_background_color == 0) {
                int alphaComponent = ColorUtils.setAlphaComponent(wallPaperSettings2.background_color, 255);
                int alphaComponent2 = ColorUtils.setAlphaComponent(wallPaper.settings.second_background_color, 255);
                int averageColor = AndroidUtilities.getAverageColor(alphaComponent, alphaComponent2);
                GradientDrawable gradientDrawable = new GradientDrawable(BackgroundGradientDrawable.getGradientOrientation(wallPaper.settings.rotation), new int[]{alphaComponent, alphaComponent2});
                gradientDrawable.setBounds(0, 0, bitmapCreateBitmap.getWidth(), bitmapCreateBitmap.getHeight());
                gradientDrawable.draw(canvas);
                patternColor = averageColor;
            } else {
                int alphaComponent3 = ColorUtils.setAlphaComponent(wallPaperSettings2.background_color, 255);
                int alphaComponent4 = ColorUtils.setAlphaComponent(wallPaper.settings.second_background_color, 255);
                int alphaComponent5 = ColorUtils.setAlphaComponent(wallPaper.settings.third_background_color, 255);
                int i = wallPaper.settings.fourth_background_color;
                int alphaComponent6 = i == 0 ? 0 : ColorUtils.setAlphaComponent(i, 255);
                int patternColor2 = MotionBackgroundDrawable.getPatternColor(alphaComponent3, alphaComponent4, alphaComponent5, alphaComponent6);
                MotionBackgroundDrawable motionBackgroundDrawable = new MotionBackgroundDrawable();
                motionBackgroundDrawable.setColors(alphaComponent3, alphaComponent4, alphaComponent5, alphaComponent6);
                motionBackgroundDrawable.setBounds(0, 0, bitmapCreateBitmap.getWidth(), bitmapCreateBitmap.getHeight());
                motionBackgroundDrawable.setPatternBitmap(wallPaper.settings.intensity, bitmap);
                motionBackgroundDrawable.draw(canvas);
                z = false;
                patternColor = patternColor2;
            }
            if (z) {
                Paint paint = new Paint(2);
                paint.setColorFilter(new PorterDuffColorFilter(patternColor, PorterDuff.Mode.SRC_IN));
                paint.setAlpha((int) ((wallPaper.settings.intensity / 100.0f) * 255.0f));
                canvas.drawBitmap(bitmap, 0.0f, 0.0f, paint);
            }
            return bitmapCreateBitmap;
        }

        private void loadLastFrame(RLottieDrawable rLottieDrawable, int i, int i2, boolean z, boolean z2) {
            Bitmap bitmapCreateBitmap;
            Canvas canvas;
            Drawable bitmapDrawable;
            if (z && z2) {
                float f = i * 1.2f;
                float f2 = i2 * 1.2f;
                bitmapCreateBitmap = Bitmap.createBitmap((int) f, (int) f2, Bitmap.Config.ARGB_8888);
                canvas = new Canvas(bitmapCreateBitmap);
                canvas.scale(2.0f, 2.0f, f / 2.0f, f2 / 2.0f);
            } else {
                bitmapCreateBitmap = Bitmap.createBitmap(i, i2, Bitmap.Config.ARGB_8888);
                canvas = new Canvas(bitmapCreateBitmap);
            }
            rLottieDrawable.prepareForGenerateCache();
            Bitmap bitmapCreateBitmap2 = Bitmap.createBitmap(rLottieDrawable.getIntrinsicWidth(), rLottieDrawable.getIntrinsicHeight(), Bitmap.Config.ARGB_8888);
            rLottieDrawable.setGeneratingFrame(z ? rLottieDrawable.getFramesCount() - 1 : 0);
            rLottieDrawable.getNextFrame(bitmapCreateBitmap2);
            rLottieDrawable.releaseForGenerateCache();
            canvas.save();
            if (!z || !z2) {
                canvas.scale(bitmapCreateBitmap2.getWidth() / i, bitmapCreateBitmap2.getHeight() / i2, i / 2.0f, i2 / 2.0f);
            }
            Paint paint = new Paint(1);
            paint.setFilterBitmap(true);
            if (z && z2) {
                canvas.drawBitmap(bitmapCreateBitmap2, (bitmapCreateBitmap.getWidth() - bitmapCreateBitmap2.getWidth()) / 2.0f, (bitmapCreateBitmap.getHeight() - bitmapCreateBitmap2.getHeight()) / 2.0f, paint);
                bitmapDrawable = new ImageReceiver.ReactionLastFrame(bitmapCreateBitmap);
            } else {
                canvas.drawBitmap(bitmapCreateBitmap2, 0.0f, 0.0f, paint);
                bitmapDrawable = new BitmapDrawable(bitmapCreateBitmap);
            }
            rLottieDrawable.recycle(false);
            bitmapCreateBitmap2.recycle();
            onPostExecute(bitmapDrawable);
        }

        private void onPostExecute(final Drawable drawable) {
            AndroidUtilities.runOnUIThread(new Runnable() { // from class: org.telegram.messenger.ImageLoader$CacheOutTask$$ExternalSyntheticLambda1
                @Override // java.lang.Runnable
                public final void run() {
                    this.f$0.lambda$onPostExecute$1(drawable);
                }
            });
        }

        /* JADX INFO: Access modifiers changed from: private */
        /* JADX WARN: Code duplicated, block: B:42:0x011a A[PHI: r7
  0x011a: PHI (r7v6 android.graphics.drawable.Drawable) = (r7v5 android.graphics.drawable.Drawable), (r7v0 android.graphics.drawable.Drawable) binds: [B:40:0x010a, B:12:0x0044] A[DONT_GENERATE, DONT_INLINE]] */
        public /* synthetic */ void lambda$onPostExecute$1(Drawable drawable) {
            final Drawable drawable2;
            final String str;
            BitmapDrawable bitmapDrawable;
            Drawable drawable3;
            boolean z = false;
            if (drawable instanceof RLottieDrawable) {
                RLottieDrawable rLottieDrawable = (RLottieDrawable) drawable;
                Drawable drawable4 = (Drawable) ImageLoader.this.lottieMemCache.get(this.cacheImage.key);
                if (drawable4 == null) {
                    ImageLoader.this.lottieMemCache.put(this.cacheImage.key, rLottieDrawable);
                    drawable3 = rLottieDrawable;
                } else {
                    rLottieDrawable.recycle(false);
                    drawable3 = drawable4;
                }
                ImageLoader.this.incrementUseCount(this.cacheImage.key);
                str = this.cacheImage.key;
                drawable2 = drawable3;
            } else if (drawable instanceof AnimatedFileDrawable) {
                AnimatedFileDrawable animatedFileDrawable = (AnimatedFileDrawable) drawable;
                if (animatedFileDrawable.isWebmSticker) {
                    BitmapDrawable fromLottieCache = ImageLoader.this.getFromLottieCache(this.cacheImage.key);
                    if (fromLottieCache == null) {
                        ImageLoader.this.lottieMemCache.put(this.cacheImage.key, animatedFileDrawable);
                        bitmapDrawable = animatedFileDrawable;
                    } else {
                        animatedFileDrawable.recycle();
                        bitmapDrawable = fromLottieCache;
                    }
                    ImageLoader.this.incrementUseCount(this.cacheImage.key);
                    str = this.cacheImage.key;
                    drawable2 = bitmapDrawable;
                } else {
                    str = null;
                    drawable2 = drawable;
                }
            } else if (drawable instanceof BitmapDrawable) {
                BitmapDrawable bitmapDrawable2 = (BitmapDrawable) drawable;
                BitmapDrawable fromMemCache = ImageLoader.this.getFromMemCache(this.cacheImage.key);
                boolean z2 = true;
                if (fromMemCache == null) {
                    if (this.cacheImage.key.endsWith("_f")) {
                        ImageLoader.this.wallpaperMemCache.put(this.cacheImage.key, bitmapDrawable2);
                    } else {
                        if (!this.cacheImage.key.endsWith("_isc") && !this.cacheImage.key.endsWith("_nocache") && bitmapDrawable2.getBitmap().getWidth() <= AndroidUtilities.density * 80.0f && bitmapDrawable2.getBitmap().getHeight() <= AndroidUtilities.density * 80.0f) {
                            ImageLoader.this.smallImagesMemCache.put(this.cacheImage.key, bitmapDrawable2);
                        } else if (!this.cacheImage.key.endsWith("_nocache")) {
                            ImageLoader.this.memCache.put(this.cacheImage.key, bitmapDrawable2);
                        }
                        z = true;
                    }
                    z2 = z;
                    drawable = bitmapDrawable2;
                } else {
                    AndroidUtilities.recycleBitmap(bitmapDrawable2.getBitmap());
                    drawable = fromMemCache;
                }
                if (z2) {
                    ImageLoader.this.incrementUseCount(this.cacheImage.key);
                    str = this.cacheImage.key;
                    drawable2 = drawable;
                } else {
                    str = null;
                    drawable2 = drawable;
                }
            } else {
                drawable2 = null;
                str = null;
            }
            ImageLoader.this.imageLoadQueue.postRunnable(new Runnable() { // from class: org.telegram.messenger.ImageLoader$CacheOutTask$$ExternalSyntheticLambda0
                @Override // java.lang.Runnable
                public final void run() {
                    this.f$0.lambda$onPostExecute$0(drawable2, str);
                }
            }, this.cacheImage.priority);
        }

        /* JADX INFO: Access modifiers changed from: private */
        public /* synthetic */ void lambda$onPostExecute$0(Drawable drawable, String str) {
            this.cacheImage.setImageAndClear(drawable, str);
        }

        public void cancel() {
            synchronized (this.sync) {
                try {
                    this.isCancelled = true;
                    Thread thread = this.runningThread;
                    if (thread != null) {
                        thread.interrupt();
                    }
                } catch (Exception unused) {
                }
            }
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public boolean isAnimatedAvatar(String str) {
        return str != null && str.endsWith("avatar");
    }

    private boolean isPFrame(String str) {
        return str != null && str.endsWith("pframe");
    }

    public BitmapDrawable getFromMemCache(String str) {
        BitmapDrawable bitmapDrawable = this.memCache.get(str);
        if (bitmapDrawable == null) {
            bitmapDrawable = this.smallImagesMemCache.get(str);
        }
        if (bitmapDrawable == null) {
            bitmapDrawable = this.wallpaperMemCache.get(str);
        }
        return bitmapDrawable == null ? getFromLottieCache(str) : bitmapDrawable;
    }

    public static Bitmap getStrippedPhotoBitmap(byte[] bArr, String str) {
        Bitmap bitmap;
        int length = (bArr.length - 3) + Bitmaps.header.length + Bitmaps.footer.length;
        byte[] bArr2 = bytesLocal.get();
        if (bArr2 == null || bArr2.length < length) {
            bArr2 = null;
        }
        if (bArr2 == null) {
            bArr2 = new byte[length];
            bytesLocal.set(bArr2);
        }
        byte[] bArr3 = Bitmaps.header;
        System.arraycopy(bArr3, 0, bArr2, 0, bArr3.length);
        System.arraycopy(bArr, 3, bArr2, Bitmaps.header.length, bArr.length - 3);
        System.arraycopy(Bitmaps.footer, 0, bArr2, (Bitmaps.header.length + bArr.length) - 3, Bitmaps.footer.length);
        bArr2[164] = bArr[1];
        bArr2[166] = bArr[2];
        BitmapFactory.Options options = new BitmapFactory.Options();
        boolean z = !TextUtils.isEmpty(str) && str.contains("r");
        options.inPreferredConfig = (SharedConfig.deviceIsHigh() || z) ? Bitmap.Config.ARGB_8888 : Bitmap.Config.RGB_565;
        Bitmap bitmapDecodeByteArray = BitmapFactory.decodeByteArray(bArr2, 0, length, options);
        if (z) {
            Bitmap bitmapCreateBitmap = Bitmap.createBitmap(bitmapDecodeByteArray.getWidth(), bitmapDecodeByteArray.getHeight(), bitmapDecodeByteArray.getConfig());
            Canvas canvas = new Canvas(bitmapCreateBitmap);
            canvas.save();
            canvas.scale(1.2f, 1.2f, bitmapDecodeByteArray.getWidth() / 2.0f, bitmapDecodeByteArray.getHeight() / 2.0f);
            canvas.drawBitmap(bitmapDecodeByteArray, 0.0f, 0.0f, (Paint) null);
            canvas.restore();
            Path path = new Path();
            path.addCircle(bitmapDecodeByteArray.getWidth() / 2.0f, bitmapDecodeByteArray.getHeight() / 2.0f, Math.min(bitmapDecodeByteArray.getWidth(), bitmapDecodeByteArray.getHeight()) / 2.0f, Path.Direction.CW);
            canvas.clipPath(path);
            canvas.drawBitmap(bitmapDecodeByteArray, 0.0f, 0.0f, (Paint) null);
            bitmapDecodeByteArray.recycle();
            bitmap = bitmapCreateBitmap;
        } else {
            bitmap = bitmapDecodeByteArray;
        }
        if (bitmap != null && !TextUtils.isEmpty(str) && str.contains("b")) {
            Utilities.blurBitmap(bitmap, 3, 1, bitmap.getWidth(), bitmap.getHeight(), bitmap.getRowBytes());
        }
        return bitmap;
    }

    /* JADX INFO: Access modifiers changed from: private */
    class CacheImage {
        protected ArtworkLoadTask artworkTask;
        protected CacheOutTask cacheTask;
        protected int cacheType;
        protected int currentAccount;
        protected File encryptionKeyPath;
        protected String ext;
        protected String filter;
        protected ArrayList<String> filters;
        protected File finalFilePath;
        protected HttpImageTask httpTask;
        protected ImageLocation imageLocation;
        protected ArrayList<ImageReceiver> imageReceiverArray;
        protected ArrayList<Integer> imageReceiverGuidsArray;
        protected int imageType;
        public boolean isPFrame;
        protected String key;
        protected ArrayList<String> keys;
        protected Object parentObject;
        public int priority;
        public Runnable runningTask;
        protected SecureDocument secureDocument;
        protected long size;
        protected File tempFilePath;
        protected int type;
        protected ArrayList<Integer> types;
        protected String url;

        private CacheImage() {
            this.priority = 1;
            this.imageReceiverArray = new ArrayList<>();
            this.imageReceiverGuidsArray = new ArrayList<>();
            this.keys = new ArrayList<>();
            this.filters = new ArrayList<>();
            this.types = new ArrayList<>();
        }

        public void addImageReceiver(ImageReceiver imageReceiver, String str, String str2, int i, int i2) {
            int iIndexOf = this.imageReceiverArray.indexOf(imageReceiver);
            if (iIndexOf >= 0 && Objects.equals(this.imageReceiverArray.get(iIndexOf).getImageKey(), str)) {
                this.imageReceiverGuidsArray.set(iIndexOf, Integer.valueOf(i2));
                return;
            }
            this.imageReceiverArray.add(imageReceiver);
            this.imageReceiverGuidsArray.add(Integer.valueOf(i2));
            this.keys.add(str);
            this.filters.add(str2);
            this.types.add(Integer.valueOf(i));
            ImageLoader.this.imageLoadingByTag.put(imageReceiver.getTag(i), this);
        }

        public void replaceImageReceiver(ImageReceiver imageReceiver, String str, String str2, int i, int i2) {
            int iIndexOf = this.imageReceiverArray.indexOf(imageReceiver);
            if (iIndexOf == -1) {
                return;
            }
            if (this.types.get(iIndexOf).intValue() != i) {
                ArrayList<ImageReceiver> arrayList = this.imageReceiverArray;
                iIndexOf = arrayList.subList(iIndexOf + 1, arrayList.size()).indexOf(imageReceiver);
                if (iIndexOf == -1) {
                    return;
                }
            }
            this.imageReceiverGuidsArray.set(iIndexOf, Integer.valueOf(i2));
            this.keys.set(iIndexOf, str);
            this.filters.set(iIndexOf, str2);
        }

        public void setImageReceiverGuid(ImageReceiver imageReceiver, int i) {
            int iIndexOf = this.imageReceiverArray.indexOf(imageReceiver);
            if (iIndexOf == -1) {
                return;
            }
            this.imageReceiverGuidsArray.set(iIndexOf, Integer.valueOf(i));
        }

        public void removeImageReceiver(ImageReceiver imageReceiver) {
            int iIntValue = this.type;
            int i = 0;
            while (i < this.imageReceiverArray.size()) {
                ImageReceiver imageReceiver2 = this.imageReceiverArray.get(i);
                if (imageReceiver2 == null || imageReceiver2 == imageReceiver) {
                    this.imageReceiverArray.remove(i);
                    this.imageReceiverGuidsArray.remove(i);
                    this.keys.remove(i);
                    this.filters.remove(i);
                    iIntValue = this.types.remove(i).intValue();
                    if (imageReceiver2 != null) {
                        ImageLoader.this.imageLoadingByTag.remove(imageReceiver2.getTag(iIntValue));
                    }
                    i--;
                }
                i++;
            }
            if (this.imageReceiverArray.isEmpty()) {
                if (this.imageLocation != null && !ImageLoader.this.forceLoadingImages.containsKey(this.key)) {
                    ImageLocation imageLocation = this.imageLocation;
                    if (imageLocation.location != null) {
                        FileLoader.getInstance(this.currentAccount).cancelLoadFile(this.imageLocation.location, this.ext);
                    } else if (imageLocation.document != null) {
                        FileLoader.getInstance(this.currentAccount).cancelLoadFile(this.imageLocation.document);
                    } else if (imageLocation.secureDocument != null) {
                        FileLoader.getInstance(this.currentAccount).cancelLoadFile(this.imageLocation.secureDocument);
                    } else if (imageLocation.webFile != null) {
                        FileLoader.getInstance(this.currentAccount).cancelLoadFile(this.imageLocation.webFile);
                    }
                }
                if (this.cacheTask != null) {
                    if (iIntValue == 1) {
                        ImageLoader.this.cacheThumbOutQueue.cancelRunnable(this.cacheTask);
                    } else {
                        ImageLoader.this.cacheOutQueue.cancelRunnable(this.cacheTask);
                        ImageLoader.this.cacheOutQueue.cancelRunnable(this.runningTask);
                    }
                    this.cacheTask.cancel();
                    this.cacheTask = null;
                }
                if (this.httpTask != null) {
                    ImageLoader.this.httpTasks.remove(this.httpTask);
                    this.httpTask.cancel(true);
                    this.httpTask = null;
                }
                if (this.artworkTask != null) {
                    ImageLoader.this.artworkTasks.remove(this.artworkTask);
                    this.artworkTask.cancel(true);
                    this.artworkTask = null;
                }
                if (this.url != null) {
                    ImageLoader.this.imageLoadingByUrl.remove(this.url);
                }
                if (this.url != null) {
                    ImageLoader.this.imageLoadingByUrlPframe.remove(this.url);
                }
                String str = this.key;
                if (str != null) {
                    ImageLoader.this.imageLoadingByKeys.remove(str);
                    ImageLoader.this.imageLoadingKeys.remove(ImageLoader.cutFilter(this.key));
                }
            }
        }

        /* JADX WARN: Multi-variable type inference failed */
        /* JADX WARN: Type inference failed for: r3v0, types: [org.telegram.messenger.FileLoader] */
        /* JADX WARN: Type inference failed for: r7v5, types: [org.telegram.messenger.WebFile] */
        /* JADX WARN: Type inference failed for: r7v6 */
        /* JADX WARN: Type inference failed for: r7v7 */
        /* JADX WARN: Type inference failed for: r8v3, types: [org.telegram.tgnet.TLRPC$FileLocation] */
        /* JADX WARN: Type inference failed for: r8v4 */
        /* JADX WARN: Type inference failed for: r8v5 */
        /* JADX WARN: Type inference failed for: r9v0 */
        /* JADX WARN: Type inference failed for: r9v1, types: [java.lang.String] */
        /* JADX WARN: Type inference failed for: r9v2 */
        void changePriority(int i) {
            TLRPC.Document document;
            SecureDocument secureDocument;
            Object obj;
            SecureDocument secureDocument2;
            SecureDocument secureDocument3;
            ?? r9;
            ?? r8;
            ?? r7;
            ImageLocation imageLocation = this.imageLocation;
            if (imageLocation != null) {
                TLRPC.TL_fileLocationToBeDeprecated tL_fileLocationToBeDeprecated = imageLocation.location;
                if (tL_fileLocationToBeDeprecated != null) {
                    r9 = this.ext;
                    r8 = tL_fileLocationToBeDeprecated;
                    document = null;
                    secureDocument = null;
                    r7 = 0;
                } else {
                    TLRPC.Document document2 = imageLocation.document;
                    if (document2 != null) {
                        document = document2;
                        secureDocument = null;
                    } else {
                        SecureDocument secureDocument4 = imageLocation.secureDocument;
                        if (secureDocument4 != null) {
                            secureDocument = secureDocument4;
                            document = null;
                            secureDocument3 = null;
                            secureDocument2 = secureDocument3;
                            obj = secureDocument3;
                        } else {
                            WebFile webFile = imageLocation.webFile;
                            if (webFile != null) {
                                obj = webFile;
                                document = null;
                                secureDocument = null;
                                secureDocument2 = null;
                            } else {
                                document = null;
                                secureDocument = null;
                            }
                        }
                        r9 = secureDocument2;
                        r7 = obj;
                        r8 = secureDocument2;
                    }
                    secureDocument3 = secureDocument;
                    secureDocument2 = secureDocument3;
                    obj = secureDocument3;
                    r9 = secureDocument2;
                    r7 = obj;
                    r8 = secureDocument2;
                }
                FileLoader.getInstance(this.currentAccount).changePriority(i, document, secureDocument, r7, r8, r9, null);
            }
        }

        public void setImageAndClear(final Drawable drawable, final String str) {
            final CacheImage cacheImage;
            if (drawable != null) {
                final ArrayList arrayList = new ArrayList(this.imageReceiverArray);
                final ArrayList arrayList2 = new ArrayList(this.imageReceiverGuidsArray);
                cacheImage = this;
                AndroidUtilities.runOnUIThread(new Runnable() { // from class: org.telegram.messenger.ImageLoader$CacheImage$$ExternalSyntheticLambda0
                    @Override // java.lang.Runnable
                    public final void run() {
                        this.f$0.lambda$setImageAndClear$0(drawable, arrayList, arrayList2, str);
                    }
                });
            } else {
                cacheImage = this;
            }
            for (int i = 0; i < cacheImage.imageReceiverArray.size(); i++) {
                ImageLoader.this.imageLoadingByTag.remove(cacheImage.imageReceiverArray.get(i).getTag(cacheImage.type));
            }
            cacheImage.imageReceiverArray.clear();
            cacheImage.imageReceiverGuidsArray.clear();
            if (cacheImage.url != null) {
                ImageLoader.this.imageLoadingByUrl.remove(cacheImage.url);
            }
            if (cacheImage.url != null) {
                ImageLoader.this.imageLoadingByUrlPframe.remove(cacheImage.url);
            }
            String str2 = cacheImage.key;
            if (str2 != null) {
                ImageLoader.this.imageLoadingByKeys.remove(str2);
                ImageLoader.this.imageLoadingKeys.remove(ImageLoader.cutFilter(cacheImage.key));
            }
        }

        /* JADX INFO: Access modifiers changed from: private */
        /* JADX WARN: Code duplicated, block: B:24:0x0050 A[LOOP:1: B:22:0x004a->B:24:0x0050, LOOP_END] */
        /* JADX WARN: Code duplicated, block: B:34:? A[SYNTHETIC] */
        public /* synthetic */ void lambda$setImageAndClear$0(Drawable drawable, ArrayList arrayList, ArrayList arrayList2, String str) {
            int i = 0;
            if (drawable instanceof AnimatedFileDrawable) {
                AnimatedFileDrawable animatedFileDrawable = (AnimatedFileDrawable) drawable;
                if (!animatedFileDrawable.isWebmSticker) {
                    boolean z = false;
                    while (i < arrayList.size()) {
                        ImageReceiver imageReceiver = (ImageReceiver) arrayList.get(i);
                        AnimatedFileDrawable animatedFileDrawableMakeCopy = i == 0 ? animatedFileDrawable : animatedFileDrawable.makeCopy();
                        if (imageReceiver.setImageBitmapByKey(animatedFileDrawableMakeCopy, this.key, this.type, false, ((Integer) arrayList2.get(i)).intValue())) {
                            if (animatedFileDrawableMakeCopy == animatedFileDrawable) {
                                z = true;
                            }
                        } else if (animatedFileDrawableMakeCopy != animatedFileDrawable) {
                            animatedFileDrawableMakeCopy.recycle();
                        }
                        i++;
                    }
                    if (!z) {
                        animatedFileDrawable.recycle();
                    }
                } else {
                    while (i < arrayList.size()) {
                        ((ImageReceiver) arrayList.get(i)).setImageBitmapByKey(drawable, this.key, this.types.get(i).intValue(), false, ((Integer) arrayList2.get(i)).intValue());
                        i++;
                    }
                }
            } else {
                while (i < arrayList.size()) {
                    ((ImageReceiver) arrayList.get(i)).setImageBitmapByKey(drawable, this.key, this.types.get(i).intValue(), false, ((Integer) arrayList2.get(i)).intValue());
                    i++;
                }
            }
            if (str != null) {
                ImageLoader.this.decrementUseCount(str);
            }
        }
    }

    public static ImageLoader getInstance() {
        ImageLoader imageLoader;
        ImageLoader imageLoader2 = Instance;
        if (imageLoader2 != null) {
            return imageLoader2;
        }
        synchronized (ImageLoader.class) {
            try {
                imageLoader = Instance;
                if (imageLoader == null) {
                    imageLoader = new ImageLoader();
                    Instance = imageLoader;
                }
            } catch (Throwable th) {
                throw th;
            }
        }
        return imageLoader;
    }

    public ImageLoader() {
        this.thumbGeneratingQueue.setPriority(1);
        int memoryClass = ((ActivityManager) ApplicationLoader.applicationContext.getSystemService("activity")).getMemoryClass();
        boolean z = memoryClass >= 192;
        this.canForce8888 = z;
        int iMin = Math.min(z ? 30 : 15, memoryClass / 7) * 1048576;
        float f = iMin;
        this.memCache = new LruCache<BitmapDrawable>((int) (0.8f * f)) { // from class: org.telegram.messenger.ImageLoader.1
            /* JADX INFO: Access modifiers changed from: protected */
            @Override // org.telegram.messenger.LruCache
            public int sizeOf(String str, BitmapDrawable bitmapDrawable) {
                return ImageLoader.this.sizeOfBitmapDrawable(bitmapDrawable);
            }

            /* JADX INFO: Access modifiers changed from: protected */
            @Override // org.telegram.messenger.LruCache
            public void entryRemoved(boolean z2, String str, BitmapDrawable bitmapDrawable, BitmapDrawable bitmapDrawable2) {
                if (ImageLoader.this.ignoreRemoval == null || !ImageLoader.this.ignoreRemoval.equals(str)) {
                    Integer num = (Integer) ImageLoader.this.bitmapUseCounts.get(str);
                    if (num == null || num.intValue() == 0) {
                        Bitmap bitmap = bitmapDrawable.getBitmap();
                        if (bitmap.isRecycled()) {
                            return;
                        }
                        ArrayList arrayList = new ArrayList();
                        arrayList.add(bitmap);
                        AndroidUtilities.recycleBitmaps(arrayList);
                    }
                }
            }
        };
        this.smallImagesMemCache = new LruCache<BitmapDrawable>((int) (f * 0.2f)) { // from class: org.telegram.messenger.ImageLoader.2
            /* JADX INFO: Access modifiers changed from: protected */
            @Override // org.telegram.messenger.LruCache
            public int sizeOf(String str, BitmapDrawable bitmapDrawable) {
                return ImageLoader.this.sizeOfBitmapDrawable(bitmapDrawable);
            }

            /* JADX INFO: Access modifiers changed from: protected */
            @Override // org.telegram.messenger.LruCache
            public void entryRemoved(boolean z2, String str, BitmapDrawable bitmapDrawable, BitmapDrawable bitmapDrawable2) {
                if (ImageLoader.this.ignoreRemoval == null || !ImageLoader.this.ignoreRemoval.equals(str)) {
                    Integer num = (Integer) ImageLoader.this.bitmapUseCounts.get(str);
                    if (num == null || num.intValue() == 0) {
                        Bitmap bitmap = bitmapDrawable.getBitmap();
                        if (bitmap.isRecycled()) {
                            return;
                        }
                        ArrayList arrayList = new ArrayList();
                        arrayList.add(bitmap);
                        AndroidUtilities.recycleBitmaps(arrayList);
                    }
                }
            }
        };
        this.wallpaperMemCache = new LruCache<BitmapDrawable>(iMin / 4) { // from class: org.telegram.messenger.ImageLoader.3
            /* JADX INFO: Access modifiers changed from: protected */
            @Override // org.telegram.messenger.LruCache
            public int sizeOf(String str, BitmapDrawable bitmapDrawable) {
                return ImageLoader.this.sizeOfBitmapDrawable(bitmapDrawable);
            }
        };
        this.lottieMemCache = new LruCache<BitmapDrawable>(10485760) { // from class: org.telegram.messenger.ImageLoader.4
            /* JADX INFO: Access modifiers changed from: protected */
            @Override // org.telegram.messenger.LruCache
            public int sizeOf(String str, BitmapDrawable bitmapDrawable) {
                return ImageLoader.this.sizeOfBitmapDrawable(bitmapDrawable);
            }

            @Override // org.telegram.messenger.LruCache
            public BitmapDrawable put(String str, BitmapDrawable bitmapDrawable) {
                if (bitmapDrawable instanceof AnimatedFileDrawable) {
                    ImageLoader.this.cachedAnimatedFileDrawables.add((AnimatedFileDrawable) bitmapDrawable);
                }
                return (BitmapDrawable) super.put(str, bitmapDrawable);
            }

            /* JADX INFO: Access modifiers changed from: protected */
            @Override // org.telegram.messenger.LruCache
            public void entryRemoved(boolean z2, String str, BitmapDrawable bitmapDrawable, BitmapDrawable bitmapDrawable2) {
                Integer num = (Integer) ImageLoader.this.bitmapUseCounts.get(str);
                boolean z3 = bitmapDrawable instanceof AnimatedFileDrawable;
                if (z3) {
                    ImageLoader.this.cachedAnimatedFileDrawables.remove((AnimatedFileDrawable) bitmapDrawable);
                }
                if (num == null || num.intValue() == 0) {
                    if (z3) {
                        ((AnimatedFileDrawable) bitmapDrawable).recycle();
                    }
                    if (bitmapDrawable instanceof RLottieDrawable) {
                        ((RLottieDrawable) bitmapDrawable).recycle(false);
                    }
                }
            }
        };
        SparseArray sparseArray = new SparseArray();
        File cacheDir = AndroidUtilities.getCacheDir();
        if (!cacheDir.isDirectory()) {
            try {
                cacheDir.mkdirs();
            } catch (Exception e) {
                FileLog.e(e);
            }
        }
        AndroidUtilities.createEmptyFile(new File(cacheDir, ".nomedia"));
        sparseArray.put(4, cacheDir);
        for (int i = 0; i < 16; i++) {
            FileLoader.getInstance(i).setDelegate(new AnonymousClass5(i));
        }
        FileLoader.setMediaDirs(sparseArray);
        AnonymousClass6 anonymousClass6 = new AnonymousClass6();
        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction("android.intent.action.MEDIA_BAD_REMOVAL");
        intentFilter.addAction("android.intent.action.MEDIA_CHECKING");
        intentFilter.addAction("android.intent.action.MEDIA_EJECT");
        intentFilter.addAction("android.intent.action.MEDIA_MOUNTED");
        intentFilter.addAction("android.intent.action.MEDIA_NOFS");
        intentFilter.addAction("android.intent.action.MEDIA_REMOVED");
        intentFilter.addAction("android.intent.action.MEDIA_SHARED");
        intentFilter.addAction("android.intent.action.MEDIA_UNMOUNTABLE");
        intentFilter.addAction("android.intent.action.MEDIA_UNMOUNTED");
        intentFilter.addDataScheme("file");
        try {
            if (Build.VERSION.SDK_INT >= 33) {
                ApplicationLoader.applicationContext.registerReceiver(anonymousClass6, intentFilter, 4);
            } else {
                ApplicationLoader.applicationContext.registerReceiver(anonymousClass6, intentFilter);
            }
        } catch (Throwable unused) {
        }
        checkMediaPaths();
    }

    /* JADX INFO: renamed from: org.telegram.messenger.ImageLoader$5, reason: invalid class name */
    class AnonymousClass5 implements FileLoader.FileLoaderDelegate {
        final /* synthetic */ int val$currentAccount;

        AnonymousClass5(int i) {
            this.val$currentAccount = i;
        }

        @Override // org.telegram.messenger.FileLoader.FileLoaderDelegate
        public void fileUploadProgressChanged(FileUploadOperation fileUploadOperation, final String str, final long j, final long j2, final boolean z) {
            ImageLoader.this.fileProgresses.put(str, new long[]{j, j2});
            long jElapsedRealtime = SystemClock.elapsedRealtime();
            long j3 = fileUploadOperation.lastProgressUpdateTime;
            if (j3 == 0 || j3 < jElapsedRealtime - 100 || j == j2) {
                fileUploadOperation.lastProgressUpdateTime = jElapsedRealtime;
                final int i = this.val$currentAccount;
                AndroidUtilities.runOnUIThread(new Runnable() { // from class: org.telegram.messenger.ImageLoader$5$$ExternalSyntheticLambda3
                    @Override // java.lang.Runnable
                    public final void run() {
                        NotificationCenter.getInstance(i).lambda$postNotificationNameOnUIThread$1(NotificationCenter.fileUploadProgressChanged, str, Long.valueOf(j), Long.valueOf(j2), Boolean.valueOf(z));
                    }
                });
            }
        }

        @Override // org.telegram.messenger.FileLoader.FileLoaderDelegate
        public void fileDidUploaded(final String str, final TLRPC.InputFile inputFile, final TLRPC.InputEncryptedFile inputEncryptedFile, final byte[] bArr, final byte[] bArr2, final long j) {
            DispatchQueue dispatchQueue = Utilities.stageQueue;
            final int i = this.val$currentAccount;
            dispatchQueue.postRunnable(new Runnable() { // from class: org.telegram.messenger.ImageLoader$5$$ExternalSyntheticLambda8
                @Override // java.lang.Runnable
                public final void run() {
                    this.f$0.lambda$fileDidUploaded$2(i, str, inputFile, inputEncryptedFile, bArr, bArr2, j);
                }
            });
        }

        /* JADX INFO: Access modifiers changed from: private */
        public /* synthetic */ void lambda$fileDidUploaded$2(final int i, final String str, final TLRPC.InputFile inputFile, final TLRPC.InputEncryptedFile inputEncryptedFile, final byte[] bArr, final byte[] bArr2, final long j) {
            AndroidUtilities.runOnUIThread(new Runnable() { // from class: org.telegram.messenger.ImageLoader$5$$ExternalSyntheticLambda7
                @Override // java.lang.Runnable
                public final void run() {
                    NotificationCenter.getInstance(i).lambda$postNotificationNameOnUIThread$1(NotificationCenter.fileUploaded, str, inputFile, inputEncryptedFile, bArr, bArr2, Long.valueOf(j));
                }
            });
            ImageLoader.this.fileProgresses.remove(str);
        }

        @Override // org.telegram.messenger.FileLoader.FileLoaderDelegate
        public void fileDidFailedUpload(final String str, final boolean z) {
            DispatchQueue dispatchQueue = Utilities.stageQueue;
            final int i = this.val$currentAccount;
            dispatchQueue.postRunnable(new Runnable() { // from class: org.telegram.messenger.ImageLoader$5$$ExternalSyntheticLambda2
                @Override // java.lang.Runnable
                public final void run() {
                    this.f$0.lambda$fileDidFailedUpload$4(i, str, z);
                }
            });
        }

        /* JADX INFO: Access modifiers changed from: private */
        public /* synthetic */ void lambda$fileDidFailedUpload$4(final int i, final String str, final boolean z) {
            AndroidUtilities.runOnUIThread(new Runnable() { // from class: org.telegram.messenger.ImageLoader$5$$ExternalSyntheticLambda0
                @Override // java.lang.Runnable
                public final void run() {
                    NotificationCenter.getInstance(i).lambda$postNotificationNameOnUIThread$1(NotificationCenter.fileUploadFailed, str, Boolean.valueOf(z));
                }
            });
            ImageLoader.this.fileProgresses.remove(str);
        }

        @Override // org.telegram.messenger.FileLoader.FileLoaderDelegate
        public void fileDidLoaded(final String str, final File file, final Object obj, final int i) {
            ImageLoader.this.fileProgresses.remove(str);
            final int i2 = this.val$currentAccount;
            AndroidUtilities.runOnUIThread(new Runnable() { // from class: org.telegram.messenger.ImageLoader$5$$ExternalSyntheticLambda1
                @Override // java.lang.Runnable
                public final void run() {
                    this.f$0.lambda$fileDidLoaded$5(file, str, i2, obj, i);
                }
            });
        }

        /* JADX INFO: Access modifiers changed from: private */
        public /* synthetic */ void lambda$fileDidLoaded$5(File file, String str, int i, Object obj, int i2) {
            MessageObject messageObject;
            TLRPC.Message message;
            FilePathDatabase.FileMeta fileMetadataFromParent;
            int i3;
            if (file != null && ((str.endsWith(".mp4") || str.endsWith(".jpg")) && (fileMetadataFromParent = FileLoader.getFileMetadataFromParent(i, obj)) != null)) {
                MessageObject messageObject2 = obj instanceof MessageObject ? (MessageObject) obj : null;
                long j = fileMetadataFromParent.dialogId;
                if (j >= 0) {
                    i3 = 1;
                } else {
                    i3 = ChatObject.isChannelAndNotMegaGroup(MessagesController.getInstance(i).getChat(Long.valueOf(-j))) ? 4 : 2;
                }
                if (SaveToGallerySettingsHelper.needSave(i3, fileMetadataFromParent, messageObject2, i)) {
                    AndroidUtilities.addMediaToGallery(file.toString());
                }
            }
            NotificationCenter.getInstance(i).lambda$postNotificationNameOnUIThread$1(NotificationCenter.fileLoaded, str, file);
            ImageLoader.this.fileDidLoaded(str, file, i2);
            if (AyuConfig.saveDeletedMessages && (obj instanceof MessageObject) && (message = (messageObject = (MessageObject) obj).messageOwner) != null && message.ayuDeleted) {
                AyuMessagesController.getInstance(messageObject.currentAccount).onMediaDownloaded(new SaveMessageRequest(messageObject.currentAccount, message));
            }
        }

        @Override // org.telegram.messenger.FileLoader.FileLoaderDelegate
        public void fileDidFailedLoad(final String str, final int i) {
            ImageLoader.this.fileProgresses.remove(str);
            final int i2 = this.val$currentAccount;
            AndroidUtilities.runOnUIThread(new Runnable() { // from class: org.telegram.messenger.ImageLoader$5$$ExternalSyntheticLambda6
                @Override // java.lang.Runnable
                public final void run() {
                    this.f$0.lambda$fileDidFailedLoad$6(str, i, i2);
                }
            });
        }

        /* JADX INFO: Access modifiers changed from: private */
        public /* synthetic */ void lambda$fileDidFailedLoad$6(String str, int i, int i2) {
            ImageLoader.this.fileDidFailedLoad(str, i);
            NotificationCenter.getInstance(i2).lambda$postNotificationNameOnUIThread$1(NotificationCenter.fileLoadFailed, str, Integer.valueOf(i));
        }

        @Override // org.telegram.messenger.FileLoader.FileLoaderDelegate
        public void fileLoadProgressChanged(final FileLoadOperation fileLoadOperation, final String str, final long j, final long j2) {
            ImageLoader.this.fileProgresses.put(str, new long[]{j, j2});
            if (!ImageLoader.this.imageLoadingByUrlPframe.isEmpty() && fileLoadOperation.checkPrefixPreloadFinished()) {
                ImageLoader.this.imageLoadQueue.postRunnable(new Runnable() { // from class: org.telegram.messenger.ImageLoader$5$$ExternalSyntheticLambda4
                    @Override // java.lang.Runnable
                    public final void run() {
                        this.f$0.lambda$fileLoadProgressChanged$7(str, fileLoadOperation);
                    }
                });
            }
            long jElapsedRealtime = SystemClock.elapsedRealtime();
            long j3 = fileLoadOperation.lastProgressUpdateTime;
            if (j3 == 0 || j3 < jElapsedRealtime - 500 || j == 0) {
                fileLoadOperation.lastProgressUpdateTime = jElapsedRealtime;
                final int i = this.val$currentAccount;
                AndroidUtilities.runOnUIThread(new Runnable() { // from class: org.telegram.messenger.ImageLoader$5$$ExternalSyntheticLambda5
                    @Override // java.lang.Runnable
                    public final void run() {
                        NotificationCenter.getInstance(i).lambda$postNotificationNameOnUIThread$1(NotificationCenter.fileLoadProgressChanged, str, Long.valueOf(j), Long.valueOf(j2));
                    }
                });
            }
        }

        /* JADX INFO: Access modifiers changed from: private */
        public /* synthetic */ void lambda$fileLoadProgressChanged$7(String str, FileLoadOperation fileLoadOperation) {
            CacheImage cacheImage = (CacheImage) ImageLoader.this.imageLoadingByUrlPframe.remove(str);
            if (cacheImage == null) {
                return;
            }
            ImageLoader.this.imageLoadingByUrl.remove(str);
            ArrayList arrayList = new ArrayList();
            for (int i = 0; i < cacheImage.imageReceiverArray.size(); i++) {
                String str2 = cacheImage.keys.get(i);
                String str3 = cacheImage.filters.get(i);
                int iIntValue = cacheImage.types.get(i).intValue();
                ImageReceiver imageReceiver = cacheImage.imageReceiverArray.get(i);
                int iIntValue2 = cacheImage.imageReceiverGuidsArray.get(i).intValue();
                CacheImage cacheImage2 = ImageLoader.this.imageLoadingByKeys.get(str2);
                if (cacheImage2 == null) {
                    cacheImage2 = new CacheImage();
                    cacheImage2.priority = cacheImage.priority;
                    cacheImage2.secureDocument = cacheImage.secureDocument;
                    cacheImage2.currentAccount = cacheImage.currentAccount;
                    cacheImage2.finalFilePath = fileLoadOperation.getCurrentFile();
                    cacheImage2.parentObject = cacheImage.parentObject;
                    cacheImage2.isPFrame = cacheImage.isPFrame;
                    cacheImage2.key = str2;
                    cacheImage2.imageLocation = cacheImage.imageLocation;
                    cacheImage2.type = iIntValue;
                    cacheImage2.ext = cacheImage.ext;
                    cacheImage2.encryptionKeyPath = cacheImage.encryptionKeyPath;
                    cacheImage2.cacheTask = ImageLoader.this.new CacheOutTask(cacheImage2);
                    cacheImage2.filter = str3;
                    cacheImage2.imageType = cacheImage.imageType;
                    cacheImage2.cacheType = cacheImage.cacheType;
                    ImageLoader.this.imageLoadingByKeys.put(str2, cacheImage2);
                    ImageLoader.this.imageLoadingKeys.add(ImageLoader.cutFilter(str2));
                    arrayList.add(cacheImage2.cacheTask);
                }
                cacheImage2.addImageReceiver(imageReceiver, str2, str3, iIntValue, iIntValue2);
            }
            for (int i2 = 0; i2 < arrayList.size(); i2++) {
                CacheOutTask cacheOutTask = (CacheOutTask) arrayList.get(i2);
                if (cacheOutTask.cacheImage.type == 1) {
                    ImageLoader.this.cacheThumbOutQueue.postRunnable(cacheOutTask);
                } else {
                    ImageLoader.this.cacheOutQueue.postRunnable(cacheOutTask, cacheOutTask.cacheImage.priority);
                }
            }
        }
    }

    /* JADX INFO: renamed from: org.telegram.messenger.ImageLoader$6, reason: invalid class name */
    class AnonymousClass6 extends BroadcastReceiver {
        AnonymousClass6() {
        }

        @Override // android.content.BroadcastReceiver
        public void onReceive(Context context, Intent intent) {
            if (BuildVars.LOGS_ENABLED) {
                FileLog.d("file system changed");
            }
            Runnable runnable = new Runnable() { // from class: org.telegram.messenger.ImageLoader$6$$ExternalSyntheticLambda0
                @Override // java.lang.Runnable
                public final void run() {
                    this.f$0.lambda$onReceive$0();
                }
            };
            if ("android.intent.action.MEDIA_UNMOUNTED".equals(intent.getAction())) {
                AndroidUtilities.runOnUIThread(runnable, 1000L);
            } else {
                runnable.run();
            }
        }

        /* JADX INFO: Access modifiers changed from: private */
        public /* synthetic */ void lambda$onReceive$0() {
            ImageLoader.this.checkMediaPaths();
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public int sizeOfBitmapDrawable(BitmapDrawable bitmapDrawable) {
        if (bitmapDrawable instanceof AnimatedFileDrawable) {
            AnimatedFileDrawable animatedFileDrawable = (AnimatedFileDrawable) bitmapDrawable;
            return Math.max(animatedFileDrawable.getIntrinsicHeight() * bitmapDrawable.getIntrinsicWidth() * 12, animatedFileDrawable.getRenderingHeight() * animatedFileDrawable.getRenderingWidth() * 12);
        }
        if (bitmapDrawable instanceof RLottieDrawable) {
            return bitmapDrawable.getIntrinsicWidth() * bitmapDrawable.getIntrinsicHeight() * 8;
        }
        return bitmapDrawable.getBitmap().getByteCount();
    }

    public void checkMediaPaths() {
        checkMediaPaths(null);
    }

    public void checkMediaPaths(final Runnable runnable) {
        this.cacheOutQueue.postRunnable(new Runnable() { // from class: org.telegram.messenger.ImageLoader$$ExternalSyntheticLambda7
            @Override // java.lang.Runnable
            public final void run() {
                this.f$0.lambda$checkMediaPaths$1(runnable);
            }
        });
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$checkMediaPaths$1(final Runnable runnable) {
        final SparseArray<File> sparseArrayCreateMediaPaths = createMediaPaths();
        AndroidUtilities.runOnUIThread(new Runnable() { // from class: org.telegram.messenger.ImageLoader$$ExternalSyntheticLambda8
            @Override // java.lang.Runnable
            public final void run() {
                ImageLoader.m2997$r8$lambda$bace6vELOUCoq4DLR0nHnQHBEk(sparseArrayCreateMediaPaths, runnable);
            }
        });
    }

    /* JADX INFO: renamed from: $r8$lambda$bac-e6vELOUCoq4DLR0nHnQHBEk, reason: not valid java name */
    public static /* synthetic */ void m2997$r8$lambda$bace6vELOUCoq4DLR0nHnQHBEk(SparseArray sparseArray, Runnable runnable) {
        FileLoader.setMediaDirs(sparseArray);
        if (runnable != null) {
            runnable.run();
        }
    }

    public void addTestWebFile(String str, WebFile webFile) {
        if (str == null || webFile == null) {
            return;
        }
        this.testWebFile.put(str, webFile);
    }

    public void removeTestWebFile(String str) {
        if (str == null) {
            return;
        }
        this.testWebFile.remove(str);
    }

    @TargetApi(26)
    private static void moveDirectory(File file, final File file2) {
        if (file.exists()) {
            if (file2.exists() || file2.mkdir()) {
                try {
                    Stream streamConvert = Stream.VivifiedWrapper.convert(Files.list(file.toPath()));
                    try {
                        streamConvert.forEach(new Consumer() { // from class: org.telegram.messenger.ImageLoader$$ExternalSyntheticLambda11
                            @Override // java.util.function.Consumer
                            /* JADX INFO: renamed from: accept */
                            public final void v(Object obj) {
                                ImageLoader.m2999$r8$lambda$gOJHwLnGci070vSdd2ZIzBwZVo(file2, (java.nio.file.Path) obj);
                            }

                            public /* synthetic */ Consumer andThen(Consumer consumer) {
                                return Consumer$CC.$default$andThen(this, consumer);
                            }
                        });
                        streamConvert.close();
                    } catch (Throwable th) {
                        if (streamConvert != null) {
                            try {
                                streamConvert.close();
                            } catch (Throwable th2) {
                                th.addSuppressed(th2);
                            }
                        }
                        throw th;
                    }
                } catch (Exception e) {
                    FileLog.e(e);
                }
            }
        }
    }

    /* JADX INFO: renamed from: $r8$lambda$gOJ-HwLnGci070vSdd2ZIzBwZVo, reason: not valid java name */
    public static /* synthetic */ void m2999$r8$lambda$gOJHwLnGci070vSdd2ZIzBwZVo(File file, java.nio.file.Path path) {
        File file2 = new File(file, path.getFileName().toString());
        if (Files.isDirectory(path, new LinkOption[0])) {
            moveDirectory(path.toFile(), file2);
            return;
        }
        try {
            Files.move(path, file2.toPath(), new CopyOption[0]);
        } catch (Exception e) {
            FileLog.e(e);
        }
    }

    public SparseArray<File> createMediaPaths() {
        File file;
        File[] externalFilesDirs;
        SparseArray<File> sparseArray = new SparseArray<>();
        File cacheDir = AndroidUtilities.getCacheDir();
        if (!cacheDir.isDirectory()) {
            try {
                cacheDir.mkdirs();
            } catch (Exception e) {
                FileLog.e(e);
            }
        }
        AndroidUtilities.createEmptyFile(new File(cacheDir, ".nomedia"));
        sparseArray.put(4, cacheDir);
        if (BuildVars.LOGS_ENABLED) {
            FileLog.d("cache path = " + cacheDir);
        }
        FileLog.d("selected SD card = " + SharedConfig.storageCacheDir);
        try {
            if ("mounted".equals(Environment.getExternalStorageState())) {
                File externalStorageDirectory = Environment.getExternalStorageDirectory();
                File file2 = null;
                if (!TextUtils.isEmpty(SharedConfig.storageCacheDir)) {
                    ArrayList<File> rootDirs = AndroidUtilities.getRootDirs();
                    if (rootDirs != null) {
                        int size = rootDirs.size();
                        for (int i = 0; i < size; i++) {
                            File file3 = rootDirs.get(i);
                            FileLog.d("root dir " + i + " " + file3);
                            if (file3.getAbsolutePath().startsWith(SharedConfig.storageCacheDir)) {
                                externalStorageDirectory = file3;
                                break;
                            }
                        }
                    }
                    if (!externalStorageDirectory.getAbsolutePath().startsWith(SharedConfig.storageCacheDir) && (externalFilesDirs = ApplicationLoader.applicationContext.getExternalFilesDirs(null)) != null) {
                        for (int i2 = 0; i2 < externalFilesDirs.length; i2++) {
                            if (externalFilesDirs[i2] != null) {
                                FileLog.d("dirsDebug " + i2 + " " + externalFilesDirs[i2]);
                            }
                        }
                    }
                }
                FileLog.d("external storage = " + externalStorageDirectory);
                if (Build.VERSION.SDK_INT >= 30) {
                    try {
                        if (ApplicationLoader.applicationContext.getExternalMediaDirs().length > 0) {
                            File publicStorageDir = getPublicStorageDir();
                            try {
                                file = new File(publicStorageDir, AyuConstants.APP_NAME);
                                try {
                                    file.mkdirs();
                                } catch (Exception e2) {
                                    e = e2;
                                    FileLog.e(e);
                                }
                            } catch (Exception e3) {
                                file = publicStorageDir;
                                e = e3;
                            }
                        } else {
                            file = null;
                        }
                    } catch (Exception e4) {
                        e = e4;
                        file = null;
                    }
                    this.telegramPath = new File(ApplicationLoader.applicationContext.getExternalFilesDir(null), AyuConstants.APP_NAME);
                    file2 = file;
                } else {
                    if (TextUtils.isEmpty(SharedConfig.storageCacheDir) || !externalStorageDirectory.getAbsolutePath().startsWith(SharedConfig.storageCacheDir)) {
                        if (externalStorageDirectory.exists()) {
                            if (externalStorageDirectory.isDirectory()) {
                                if (!externalStorageDirectory.canWrite()) {
                                }
                            }
                        } else if (externalStorageDirectory.mkdirs()) {
                            if (!externalStorageDirectory.canWrite()) {
                            }
                        }
                        FileLog.d("can't write to this directory = " + externalStorageDirectory + " use files dir");
                        externalStorageDirectory = ApplicationLoader.applicationContext.getExternalFilesDir(null);
                    }
                    this.telegramPath = new File(externalStorageDirectory, AyuConstants.APP_NAME);
                }
                this.telegramPath.mkdirs();
                if (!this.telegramPath.isDirectory()) {
                    ArrayList<File> dataDirs = AndroidUtilities.getDataDirs();
                    int size2 = dataDirs.size();
                    for (int i3 = 0; i3 < size2; i3++) {
                        File file4 = dataDirs.get(i3);
                        if (file4 != null && !TextUtils.isEmpty(SharedConfig.storageCacheDir) && file4.getAbsolutePath().startsWith(SharedConfig.storageCacheDir)) {
                            File file5 = new File(file4, AyuConstants.APP_NAME);
                            this.telegramPath = file5;
                            file5.mkdirs();
                            break;
                        }
                    }
                }
                if (this.telegramPath.isDirectory()) {
                    try {
                        File file6 = new File(this.telegramPath, AyuConstants.APP_NAME + " Images");
                        file6.mkdir();
                        if (file6.isDirectory() && canMoveFiles(cacheDir, file6, 0)) {
                            sparseArray.put(0, file6);
                            if (BuildVars.LOGS_ENABLED) {
                                FileLog.d("image path = " + file6);
                            }
                        }
                    } catch (Exception e5) {
                        FileLog.e(e5);
                    }
                    try {
                        File file7 = new File(this.telegramPath, AyuConstants.APP_NAME + " Video");
                        file7.mkdir();
                        if (file7.isDirectory() && canMoveFiles(cacheDir, file7, 2)) {
                            sparseArray.put(2, file7);
                            if (BuildVars.LOGS_ENABLED) {
                                FileLog.d("video path = " + file7);
                            }
                        }
                    } catch (Exception e6) {
                        FileLog.e(e6);
                    }
                    try {
                        File file8 = new File(this.telegramPath, AyuConstants.APP_NAME + " Audio");
                        file8.mkdir();
                        if (file8.isDirectory() && canMoveFiles(cacheDir, file8, 1)) {
                            AndroidUtilities.createEmptyFile(new File(file8, ".nomedia"));
                            sparseArray.put(1, file8);
                            if (BuildVars.LOGS_ENABLED) {
                                FileLog.d("audio path = " + file8);
                            }
                        }
                    } catch (Exception e7) {
                        FileLog.e(e7);
                    }
                    try {
                        File file9 = new File(this.telegramPath, AyuConstants.APP_NAME + " Documents");
                        file9.mkdir();
                        if (file9.isDirectory() && canMoveFiles(cacheDir, file9, 3)) {
                            AndroidUtilities.createEmptyFile(new File(file9, ".nomedia"));
                            sparseArray.put(3, file9);
                            if (BuildVars.LOGS_ENABLED) {
                                FileLog.d("documents path = " + file9);
                            }
                        }
                    } catch (Exception e8) {
                        FileLog.e(e8);
                    }
                    try {
                        File file10 = new File(this.telegramPath, AyuConstants.APP_NAME + " Files");
                        file10.mkdir();
                        if (file10.isDirectory() && canMoveFiles(cacheDir, file10, 5)) {
                            AndroidUtilities.createEmptyFile(new File(file10, ".nomedia"));
                            sparseArray.put(5, file10);
                            if (BuildVars.LOGS_ENABLED) {
                                FileLog.d("files path = " + file10);
                            }
                        }
                    } catch (Exception e9) {
                        FileLog.e(e9);
                    }
                    try {
                        File file11 = new File(this.telegramPath, AyuConstants.APP_NAME + " Stories");
                        file11.mkdir();
                        if (file11.isDirectory() && canMoveFiles(cacheDir, file11, 6)) {
                            AndroidUtilities.createEmptyFile(new File(file11, ".nomedia"));
                            sparseArray.put(6, file11);
                            if (BuildVars.LOGS_ENABLED) {
                                FileLog.d("stories path = " + file11);
                            }
                        }
                    } catch (Exception e10) {
                        FileLog.e(e10);
                    }
                }
                if (file2 != null && file2.isDirectory()) {
                    try {
                        File file12 = new File(file2, AyuConstants.APP_NAME + " Images");
                        file12.mkdir();
                        if (file12.isDirectory() && canMoveFiles(cacheDir, file12, 0)) {
                            sparseArray.put(100, file12);
                            if (BuildVars.LOGS_ENABLED) {
                                FileLog.d("image path = " + file12);
                            }
                        }
                    } catch (Exception e11) {
                        FileLog.e(e11);
                    }
                    try {
                        File file13 = new File(file2, AyuConstants.APP_NAME + " Video");
                        file13.mkdir();
                        if (file13.isDirectory() && canMoveFiles(cacheDir, file13, 2)) {
                            sparseArray.put(101, file13);
                            if (BuildVars.LOGS_ENABLED) {
                                FileLog.d("video path = " + file13);
                            }
                        }
                    } catch (Exception e12) {
                        FileLog.e(e12);
                    }
                }
            } else if (BuildVars.LOGS_ENABLED) {
                FileLog.d("this Android can't rename files");
            }
            SharedConfig.checkSaveToGalleryFiles();
        } catch (Exception e13) {
            FileLog.e(e13);
        }
        return sparseArray;
    }

    private File getPublicStorageDir() {
        File file = ApplicationLoader.applicationContext.getExternalMediaDirs()[0];
        if (!TextUtils.isEmpty(SharedConfig.storageCacheDir)) {
            for (int i = 0; i < ApplicationLoader.applicationContext.getExternalMediaDirs().length; i++) {
                File file2 = ApplicationLoader.applicationContext.getExternalMediaDirs()[i];
                if (file2 != null && file2.getPath().startsWith(SharedConfig.storageCacheDir)) {
                    file = ApplicationLoader.applicationContext.getExternalMediaDirs()[i];
                }
            }
        }
        return file;
    }

    private boolean canMoveFiles(File file, File file2, int i) throws Throwable {
        File file3;
        File file4;
        RandomAccessFile randomAccessFile = null;
        try {
            try {
                if (i == 0 || i == 3 || i == 5 || i == 6 || i == 1 || i == 2) {
                    file3 = new File(file, "000000000_999999_temp.f");
                    file4 = new File(file2, "000000000_999999.f");
                } else {
                    file4 = null;
                    file3 = null;
                }
                byte[] bArr = new byte[1024];
                file3.createNewFile();
                RandomAccessFile randomAccessFile2 = new RandomAccessFile(file3, "rws");
                try {
                    randomAccessFile2.write(bArr);
                    randomAccessFile2.close();
                    boolean zRenameTo = file3.renameTo(file4);
                    file3.delete();
                    file4.delete();
                    return zRenameTo;
                } catch (Exception e) {
                    e = e;
                    randomAccessFile = randomAccessFile2;
                    FileLog.e(e);
                    if (randomAccessFile == null) {
                        return false;
                    }
                    try {
                        randomAccessFile.close();
                        return false;
                    } catch (Exception e2) {
                        FileLog.e(e2);
                        return false;
                    }
                } catch (Throwable th) {
                    th = th;
                    randomAccessFile = randomAccessFile2;
                    if (randomAccessFile != null) {
                        try {
                            randomAccessFile.close();
                        } catch (Exception e3) {
                            FileLog.e(e3);
                        }
                    }
                    throw th;
                }
            } catch (Throwable th2) {
                th = th2;
            }
        } catch (Exception e4) {
            e = e4;
        }
    }

    public Float getFileProgress(String str) {
        long[] jArr;
        if (str == null || (jArr = this.fileProgresses.get(str)) == null) {
            return null;
        }
        long j = jArr[1];
        if (j == 0) {
            return Float.valueOf(0.0f);
        }
        return Float.valueOf(Math.min(1.0f, jArr[0] / j));
    }

    public long[] getFileProgressSizes(String str) {
        if (str == null) {
            return null;
        }
        return this.fileProgresses.get(str);
    }

    public String getReplacedKey(String str) {
        if (str == null) {
            return null;
        }
        return this.replacedBitmaps.get(str);
    }

    /* JADX WARN: Code duplicated, block: B:18:0x004d  */
    private void performReplace(String str, String str2) {
        LruCache<BitmapDrawable> lruCache = this.memCache;
        BitmapDrawable bitmapDrawable = lruCache.get(str);
        if (bitmapDrawable == null) {
            lruCache = this.smallImagesMemCache;
            bitmapDrawable = lruCache.get(str);
        }
        this.replacedBitmaps.put(str, str2);
        if (bitmapDrawable != null) {
            BitmapDrawable bitmapDrawable2 = lruCache.get(str2);
            if (bitmapDrawable2 != null && bitmapDrawable2.getBitmap() != null && bitmapDrawable.getBitmap() != null) {
                Bitmap bitmap = bitmapDrawable2.getBitmap();
                Bitmap bitmap2 = bitmapDrawable.getBitmap();
                if (bitmap.getWidth() <= bitmap2.getWidth() && bitmap.getHeight() <= bitmap2.getHeight()) {
                    this.ignoreRemoval = str;
                    lruCache.remove(str);
                    lruCache.put(str2, bitmapDrawable);
                    this.ignoreRemoval = null;
                } else {
                    lruCache.remove(str);
                }
            } else {
                this.ignoreRemoval = str;
                lruCache.remove(str);
                lruCache.put(str2, bitmapDrawable);
                this.ignoreRemoval = null;
            }
        }
        Integer num = this.bitmapUseCounts.get(str);
        if (num != null) {
            this.bitmapUseCounts.put(str2, num);
            this.bitmapUseCounts.remove(str);
        }
    }

    public void incrementUseCount(String str) {
        Integer num = this.bitmapUseCounts.get(str);
        if (num == null) {
            this.bitmapUseCounts.put(str, 1);
        } else {
            this.bitmapUseCounts.put(str, Integer.valueOf(num.intValue() + 1));
        }
    }

    public boolean decrementUseCount(String str) {
        Integer num = this.bitmapUseCounts.get(str);
        if (num == null) {
            return true;
        }
        if (num.intValue() == 1) {
            this.bitmapUseCounts.remove(str);
            return true;
        }
        this.bitmapUseCounts.put(str, Integer.valueOf(num.intValue() - 1));
        return false;
    }

    public void removeImage(String str) {
        this.bitmapUseCounts.remove(str);
        this.memCache.remove(str);
        this.smallImagesMemCache.remove(str);
    }

    public boolean isInMemCache(String str, boolean z) {
        if (z) {
            return getFromLottieCache(str) != null;
        }
        return getFromMemCache(str) != null;
    }

    public void clearMemory() {
        this.smallImagesMemCache.evictAll();
        this.memCache.evictAll();
        this.lottieMemCache.evictAll();
    }

    private void removeFromWaitingForThumb(int i, ImageReceiver imageReceiver) {
        String str = this.waitingForQualityThumbByTag.get(i);
        if (str != null) {
            ThumbGenerateInfo thumbGenerateInfo = this.waitingForQualityThumb.get(str);
            if (thumbGenerateInfo != null) {
                int iIndexOf = thumbGenerateInfo.imageReceiverArray.indexOf(imageReceiver);
                if (iIndexOf >= 0) {
                    thumbGenerateInfo.imageReceiverArray.remove(iIndexOf);
                    thumbGenerateInfo.imageReceiverGuidsArray.remove(iIndexOf);
                }
                if (thumbGenerateInfo.imageReceiverArray.isEmpty()) {
                    this.waitingForQualityThumb.remove(str);
                }
            }
            this.waitingForQualityThumbByTag.remove(i);
        }
    }

    public void changeFileLoadingPriorityForImageReceiver(final ImageReceiver imageReceiver) {
        if (imageReceiver == null) {
            return;
        }
        final int fileLoadingPriority = imageReceiver.getFileLoadingPriority();
        this.imageLoadQueue.postRunnable(new Runnable() { // from class: org.telegram.messenger.ImageLoader$$ExternalSyntheticLambda0
            @Override // java.lang.Runnable
            public final void run() {
                this.f$0.lambda$changeFileLoadingPriorityForImageReceiver$3(imageReceiver, fileLoadingPriority);
            }
        });
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$changeFileLoadingPriorityForImageReceiver$3(ImageReceiver imageReceiver, int i) {
        CacheImage cacheImage;
        int i2 = 0;
        while (true) {
            int i3 = 3;
            if (i2 >= 3) {
                return;
            }
            if (i2 == 0) {
                i3 = 1;
            } else if (i2 == 1) {
                i3 = 0;
            }
            int tag = imageReceiver.getTag(i3);
            if (tag != 0 && (cacheImage = this.imageLoadingByTag.get(tag)) != null) {
                cacheImage.changePriority(i);
            }
            i2++;
        }
    }

    public void cancelLoadingForImageReceiver(final ImageReceiver imageReceiver, final boolean z) {
        if (imageReceiver == null) {
            return;
        }
        WebInstantView.cancelLoadPhoto(imageReceiver);
        ArrayList<Runnable> loadingOperations = imageReceiver.getLoadingOperations();
        synchronized (loadingOperations) {
            try {
                if (!loadingOperations.isEmpty()) {
                    for (int i = 0; i < loadingOperations.size(); i++) {
                        this.imageLoadQueue.cancelRunnable(loadingOperations.get(i));
                    }
                    loadingOperations.clear();
                }
            } catch (Throwable th) {
                throw th;
            }
        }
        imageReceiver.addLoadingImageRunnable(null);
        this.imageLoadQueue.postRunnable(new Runnable() { // from class: org.telegram.messenger.ImageLoader$$ExternalSyntheticLambda3
            @Override // java.lang.Runnable
            public final void run() {
                this.f$0.lambda$cancelLoadingForImageReceiver$4(z, imageReceiver);
            }
        });
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$cancelLoadingForImageReceiver$4(boolean z, ImageReceiver imageReceiver) {
        int i = 0;
        while (true) {
            int i2 = 3;
            if (i >= 3) {
                return;
            }
            if (i > 0 && !z) {
                return;
            }
            if (i == 0) {
                i2 = 1;
            } else if (i == 1) {
                i2 = 0;
            }
            int tag = imageReceiver.getTag(i2);
            if (tag != 0) {
                if (i == 0) {
                    removeFromWaitingForThumb(tag, imageReceiver);
                }
                CacheImage cacheImage = this.imageLoadingByTag.get(tag);
                if (cacheImage != null) {
                    cacheImage.removeImageReceiver(imageReceiver);
                }
            }
            i++;
        }
    }

    public BitmapDrawable getImageFromMemory(TLObject tLObject, String str, String str2) {
        String strMD5 = null;
        if (tLObject == null && str == null) {
            return null;
        }
        if (str != null) {
            strMD5 = Utilities.MD5(str);
        } else if (tLObject instanceof TLRPC.FileLocation) {
            TLRPC.FileLocation fileLocation = (TLRPC.FileLocation) tLObject;
            strMD5 = fileLocation.volume_id + "_" + fileLocation.local_id;
        } else if (tLObject instanceof TLRPC.Document) {
            TLRPC.Document document = (TLRPC.Document) tLObject;
            strMD5 = document.dc_id + "_" + document.id;
        } else if (tLObject instanceof SecureDocument) {
            SecureDocument secureDocument = (SecureDocument) tLObject;
            strMD5 = secureDocument.secureFile.dc_id + "_" + secureDocument.secureFile.id;
        } else if (tLObject instanceof WebFile) {
            strMD5 = Utilities.MD5(((WebFile) tLObject).url);
        }
        if (str2 != null) {
            strMD5 = strMD5 + "@" + str2;
        }
        return getFromMemCache(strMD5);
    }

    /* JADX INFO: Access modifiers changed from: private */
    /* JADX INFO: renamed from: replaceImageInCacheInternal, reason: merged with bridge method [inline-methods] */
    public void lambda$replaceImageInCache$5(String str, String str2, ImageLocation imageLocation) {
        ArrayList<String> filterKeys;
        for (int i = 0; i < 2; i++) {
            if (i == 0) {
                filterKeys = this.memCache.getFilterKeys(str);
            } else {
                filterKeys = this.smallImagesMemCache.getFilterKeys(str);
            }
            if (filterKeys != null) {
                for (int i2 = 0; i2 < filterKeys.size(); i2++) {
                    String str3 = filterKeys.get(i2);
                    String str4 = str + "@" + str3;
                    String str5 = str2 + "@" + str3;
                    performReplace(str4, str5);
                    NotificationCenter.getGlobalInstance().lambda$postNotificationNameOnUIThread$1(NotificationCenter.didReplacedPhotoInMemCache, str4, str5, imageLocation);
                }
            } else {
                performReplace(str, str2);
                NotificationCenter.getGlobalInstance().lambda$postNotificationNameOnUIThread$1(NotificationCenter.didReplacedPhotoInMemCache, str, str2, imageLocation);
            }
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static String cutFilter(String str) {
        if (str == null) {
            return null;
        }
        int iIndexOf = str.indexOf(64);
        return iIndexOf >= 0 ? str.substring(0, iIndexOf) : str;
    }

    public void replaceImageInCache(final String str, final String str2, final ImageLocation imageLocation, boolean z) {
        if (z) {
            AndroidUtilities.runOnUIThread(new Runnable() { // from class: org.telegram.messenger.ImageLoader$$ExternalSyntheticLambda10
                @Override // java.lang.Runnable
                public final void run() {
                    this.f$0.lambda$replaceImageInCache$5(str, str2, imageLocation);
                }
            });
        } else {
            lambda$replaceImageInCache$5(str, str2, imageLocation);
        }
    }

    public void putImageToCache(BitmapDrawable bitmapDrawable, String str, boolean z) {
        if (str.endsWith("_nocache")) {
            return;
        }
        if (z) {
            this.smallImagesMemCache.put(str, bitmapDrawable);
        } else {
            this.memCache.put(str, bitmapDrawable);
        }
    }

    private void generateThumb(int i, File file, ThumbGenerateInfo thumbGenerateInfo) {
        if ((i != 0 && i != 2 && i != 3) || file == null || thumbGenerateInfo == null) {
            return;
        }
        if (this.thumbGenerateTasks.get(FileLoader.getAttachFileName(thumbGenerateInfo.parentDocument)) == null) {
            this.thumbGeneratingQueue.postRunnable(new ThumbGenerateTask(i, file, thumbGenerateInfo));
        }
    }

    public void cancelForceLoadingForImageReceiver(ImageReceiver imageReceiver) {
        final String imageKey;
        if (imageReceiver == null || (imageKey = imageReceiver.getImageKey()) == null) {
            return;
        }
        this.imageLoadQueue.postRunnable(new Runnable() { // from class: org.telegram.messenger.ImageLoader$$ExternalSyntheticLambda13
            @Override // java.lang.Runnable
            public final void run() {
                this.f$0.lambda$cancelForceLoadingForImageReceiver$6(imageKey);
            }
        });
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$cancelForceLoadingForImageReceiver$6(String str) {
        this.forceLoadingImages.remove(str);
    }

    private void createLoadOperationForImageReceiver(final ImageReceiver imageReceiver, final String str, final String str2, final String str3, final ImageLocation imageLocation, final String str4, final long j, final int i, final int i2, final int i3, final int i4) {
        if (imageReceiver == null || str2 == null || str == null || imageLocation == null) {
            return;
        }
        int tag = imageReceiver.getTag(i2);
        if (tag == 0) {
            tag = this.lastImageNum;
            imageReceiver.setTag(tag, i2);
            int i5 = this.lastImageNum + 1;
            this.lastImageNum = i5;
            if (i5 == Integer.MAX_VALUE) {
                this.lastImageNum = 0;
            }
        }
        final int i6 = tag;
        final boolean zIsNeedsQualityThumb = imageReceiver.isNeedsQualityThumb();
        final Object parentObject = imageReceiver.getParentObject();
        final TLRPC.Document qualityThumbDocument = imageReceiver.getQualityThumbDocument();
        final boolean zIsShouldGenerateQualityThumb = imageReceiver.isShouldGenerateQualityThumb();
        final int currentAccount = imageReceiver.getCurrentAccount();
        final boolean z = i2 == 0 && imageReceiver.isCurrentKeyQuality();
        Runnable runnable = new Runnable() { // from class: org.telegram.messenger.ImageLoader$$ExternalSyntheticLambda6
            @Override // java.lang.Runnable
            public final void run() {
                this.f$0.lambda$createLoadOperationForImageReceiver$7(i3, str2, str, i6, imageReceiver, i4, str4, i2, imageLocation, z, parentObject, currentAccount, qualityThumbDocument, zIsNeedsQualityThumb, zIsShouldGenerateQualityThumb, str3, i, j);
            }
        };
        this.imageLoadQueue.postRunnable(runnable, imageReceiver.getFileLoadingPriority() == 0 ? 0L : 1L);
        imageReceiver.addLoadingImageRunnable(runnable);
    }

    /* JADX INFO: Access modifiers changed from: private */
    /* JADX WARN: Code duplicated, block: B:126:0x029c  */
    /* JADX WARN: Code duplicated, block: B:128:0x02a0  */
    /* JADX WARN: Code duplicated, block: B:132:0x02ae  */
    /* JADX WARN: Code duplicated, block: B:185:0x03b2  */
    /* JADX WARN: Code duplicated, block: B:187:0x03b8  */
    /* JADX WARN: Code duplicated, block: B:188:0x03c2  */
    /* JADX WARN: Code duplicated, block: B:190:0x03c8  */
    /* JADX WARN: Code duplicated, block: B:191:0x03d4  */
    /* JADX WARN: Code duplicated, block: B:203:0x041e  */
    /* JADX WARN: Code duplicated, block: B:205:0x0425  */
    /* JADX WARN: Code duplicated, block: B:208:0x042e  */
    /* JADX WARN: Code duplicated, block: B:210:0x0438  */
    /* JADX WARN: Code duplicated, block: B:211:0x043d  */
    /* JADX WARN: Code duplicated, block: B:213:0x0446  */
    /* JADX WARN: Code duplicated, block: B:214:0x044a  */
    /* JADX WARN: Code duplicated, block: B:222:0x0479  */
    /* JADX WARN: Code duplicated, block: B:224:0x0482  */
    /* JADX WARN: Code duplicated, block: B:225:0x049a  */
    /* JADX WARN: Code duplicated, block: B:227:0x04a1  */
    /* JADX WARN: Code duplicated, block: B:229:0x04ad  */
    /* JADX WARN: Code duplicated, block: B:232:0x04bd  */
    /* JADX WARN: Code duplicated, block: B:23:0x0090  */
    /* JADX WARN: Code duplicated, block: B:240:0x04d7  */
    /* JADX WARN: Code duplicated, block: B:247:0x0512  */
    /* JADX WARN: Code duplicated, block: B:249:0x0523  */
    /* JADX WARN: Code duplicated, block: B:254:0x0532  */
    /* JADX WARN: Code duplicated, block: B:257:0x055e  */
    /* JADX WARN: Code duplicated, block: B:260:0x0563  */
    /* JADX WARN: Code duplicated, block: B:262:0x058a  */
    /* JADX WARN: Code duplicated, block: B:265:0x0593 A[ADDED_TO_REGION] */
    /* JADX WARN: Code duplicated, block: B:309:0x0681  */
    /* JADX WARN: Code duplicated, block: B:311:0x0689  */
    /* JADX WARN: Code duplicated, block: B:37:0x00d9  */
    /* JADX WARN: Code duplicated, block: B:65:0x018f  */
    /* JADX WARN: Code duplicated, block: B:71:0x01a5  */
    /* JADX WARN: Code duplicated, block: B:92:0x0216  */
    /* JADX WARN: Code duplicated, block: B:94:0x0227  */
    /* JADX WARN: Code duplicated, block: B:95:0x022a  */
    /* JADX WARN: Code duplicated, block: B:98:0x022f  */
    /* JADX WARN: Instruction removed from duplicated block: B:260:0x0563, please report this as an issue */
    /* JADX WARN: Multi-variable type inference failed */
    public /* synthetic */ void lambda$createLoadOperationForImageReceiver$7(int i, String str, String str2, int i2, ImageReceiver imageReceiver, int i3, String str3, int i4, ImageLocation imageLocation, boolean z, Object obj, int i5, TLRPC.Document document, boolean z2, boolean z3, String str4, int i6, long j) {
        String str5;
        int i7;
        int i8;
        String str6;
        int i9;
        int i10;
        int i11;
        File file;
        int i12;
        int mediaType;
        String str7;
        File pathToMessage;
        boolean z4;
        File file2;
        File file3;
        TLRPC.Document document2;
        int i13;
        CacheImage cacheImage;
        int i14;
        String str8;
        int i15;
        int i16;
        long j2;
        ImageLoader imageLoader;
        int i17;
        ImageLoader imageLoader2;
        int i18;
        boolean z5;
        String str9;
        ImageLoader imageLoader3;
        int i19;
        long j3;
        TLRPC.Document document3;
        File file4;
        ImageLoader imageLoader4;
        int i20;
        File file5;
        int i21;
        int i22;
        int i23;
        int i24;
        ImageLoader imageLoader5 = this;
        ImageReceiver imageReceiver2 = imageReceiver;
        TLRPC.Document document4 = document;
        if (i != 2) {
            CacheImage cacheImage2 = imageLoader5.imageLoadingByUrl.get(str);
            CacheImage cacheImage3 = imageLoader5.imageLoadingByKeys.get(str2);
            CacheImage cacheImage4 = imageLoader5.imageLoadingByTag.get(i2);
            if (cacheImage4 != null) {
                if (cacheImage4 == cacheImage3) {
                    cacheImage4.setImageReceiverGuid(imageReceiver2, i3);
                    cacheImage2 = cacheImage2;
                    cacheImage3 = cacheImage3;
                    i7 = 0;
                } else if (cacheImage4 == cacheImage2) {
                    cacheImage2 = cacheImage2;
                    cacheImage3 = cacheImage3;
                    i7 = 0;
                    if (cacheImage3 == null) {
                        cacheImage4.replaceImageReceiver(imageReceiver2, str2, str3, i4, i3);
                    }
                } else {
                    i7 = 0;
                    cacheImage4.removeImageReceiver(imageReceiver2);
                }
                i24 = 1;
                if (i24 == 0 && cacheImage3 != null) {
                    cacheImage3.addImageReceiver(imageReceiver2, str2, str3, i4, i3);
                    i24 = 1;
                }
                if (i24 == 0 || cacheImage2 == null) {
                    imageReceiver2 = imageReceiver;
                    str5 = str3;
                    i8 = i24;
                } else {
                    imageReceiver2 = imageReceiver;
                    str5 = str3;
                    cacheImage2.addImageReceiver(imageReceiver2, str2, str5, i4, i3);
                    i8 = 1;
                }
            } else {
                i7 = 0;
            }
            i24 = i7;
            if (i24 == 0) {
                cacheImage3.addImageReceiver(imageReceiver2, str2, str3, i4, i3);
                i24 = 1;
            }
            if (i24 == 0) {
                imageReceiver2 = imageReceiver;
                str5 = str3;
                i8 = i24;
            } else {
                imageReceiver2 = imageReceiver;
                str5 = str3;
                i8 = i24;
            }
        } else {
            str5 = str3;
            i7 = 0;
            i8 = 0;
        }
        if (i8 == 0) {
            String str10 = imageLocation.path;
            if (str10 != null) {
                if (str10.startsWith("http") || str10.startsWith("athumb")) {
                    i10 = i7;
                    file = null;
                } else {
                    if (str10.startsWith("thumb://")) {
                        int iIndexOf = str10.indexOf(":", 8);
                        if (iIndexOf >= 0) {
                            file = new File(str10.substring(iIndexOf + 1));
                        } else {
                            file = null;
                        }
                    } else if (str10.startsWith("vthumb://")) {
                        int iIndexOf2 = str10.indexOf(":", 9);
                        if (iIndexOf2 >= 0) {
                            file = new File(str10.substring(iIndexOf2 + 1));
                        } else {
                            file = null;
                        }
                    } else {
                        file = new File(str10);
                    }
                    i10 = 1;
                }
                i = i;
                str6 = "athumb";
                i9 = i7;
            } else {
                if (i == 0 && z) {
                    if (obj instanceof MessageObject) {
                        MessageObject messageObject = (MessageObject) obj;
                        document2 = messageObject.getDocument();
                        str7 = messageObject.messageOwner.attachPath;
                        str6 = "athumb";
                        pathToMessage = FileLoader.getInstance(i5).getPathToMessage(messageObject.messageOwner);
                        mediaType = messageObject.getMediaType();
                        z4 = i7;
                    } else {
                        str6 = "athumb";
                        if (document4 != null) {
                            File pathToAttach = FileLoader.getInstance(i5).getPathToAttach(document4, true);
                            mediaType = MessageObject.isVideoDocument(document4) ? 2 : 3;
                            pathToMessage = pathToAttach;
                            z4 = 1;
                            str7 = null;
                        } else {
                            i12 = i7;
                            mediaType = i12 == true ? 1 : 0;
                            document4 = null;
                            str7 = null;
                            pathToMessage = null;
                        }
                    }
                    if (document4 != null) {
                        if (z2) {
                            file2 = new File(FileLoader.getDirectory(4), "q_" + document4.dc_id + "_" + document4.id + ".jpg");
                            if (file2.exists()) {
                                i9 = 1;
                            } else {
                                i9 = i7;
                                file2 = null;
                            }
                        } else {
                            i9 = i7;
                            file2 = null;
                        }
                        if (TextUtils.isEmpty(str7)) {
                            file3 = null;
                        } else {
                            file3 = new File(str7);
                            if (!file3.exists()) {
                                file3 = null;
                            }
                        }
                        if (file3 == null) {
                            file3 = pathToMessage;
                        }
                        if (file2 == null) {
                            String attachFileName = FileLoader.getAttachFileName(document4);
                            ThumbGenerateInfo thumbGenerateInfo = imageLoader5.waitingForQualityThumb.get(attachFileName);
                            if (thumbGenerateInfo == null) {
                                thumbGenerateInfo = new ThumbGenerateInfo();
                                thumbGenerateInfo.parentDocument = document4;
                                thumbGenerateInfo.filter = str5;
                                thumbGenerateInfo.big = z4;
                                imageLoader5.waitingForQualityThumb.put(attachFileName, thumbGenerateInfo);
                            }
                            if (!thumbGenerateInfo.imageReceiverArray.contains(imageReceiver2)) {
                                thumbGenerateInfo.imageReceiverArray.add(imageReceiver2);
                                thumbGenerateInfo.imageReceiverGuidsArray.add(Integer.valueOf(i3));
                            }
                            imageLoader5.waitingForQualityThumbByTag.put(i2, attachFileName);
                            if (file3.exists() && z3) {
                                imageLoader5.generateThumb(mediaType, file3, thumbGenerateInfo);
                                return;
                            }
                        } else {
                            i = i;
                            file = file2;
                            i10 = 1;
                        }
                    } else {
                        z4 = i12;
                        document4 = document2;
                        i9 = i7;
                        i10 = 1;
                    }
                } else {
                    str6 = "athumb";
                    i9 = i7;
                    i10 = i9;
                }
                i11 = 2;
                file = null;
                if (i != i11) {
                    boolean zIsEncrypted = imageLocation.isEncrypted();
                    i13 = i10;
                    cacheImage = new CacheImage();
                    if (imageReceiver2.getFileLoadingPriority() == 0) {
                        i14 = i7;
                    } else {
                        i14 = 1;
                    }
                    cacheImage.priority = i14;
                    if (!z) {
                        if (imageLocation.imageType != i11 || MessageObject.isGifDocument(imageLocation.webFile) || MessageObject.isGifDocument(imageLocation.document) || MessageObject.isRoundVideoDocument(imageLocation.document) || MessageObject.isVideoSticker(imageLocation.document)) {
                            cacheImage.imageType = i11;
                        } else {
                            String str11 = imageLocation.path;
                            if (str11 != null && !str11.startsWith("vthumb") && !str11.startsWith("thumb")) {
                                String httpUrlExtension = getHttpUrlExtension(str11, "jpg");
                                if (httpUrlExtension.equalsIgnoreCase("webm") || httpUrlExtension.equalsIgnoreCase("mp4") || httpUrlExtension.equalsIgnoreCase("gif")) {
                                    cacheImage.imageType = i11;
                                } else if ("tgs".equals(str4)) {
                                    cacheImage.imageType = 1;
                                }
                            }
                        }
                    }
                    if (file == null) {
                        TLRPC.PhotoSize photoSize = imageLocation.photoSize;
                        j2 = 0;
                        z5 = photoSize instanceof TLRPC.TL_photoStrippedSize;
                        str9 = AUTOPLAY_FILTER;
                        if (!z5 || (photoSize instanceof TLRPC.TL_photoPathSize)) {
                            str8 = str;
                            i15 = i6;
                            imageLoader3 = imageLoader5;
                            int i25 = i9;
                            str9 = AUTOPLAY_FILTER;
                            i19 = 1;
                            i17 = i25;
                            j3 = 0;
                            i16 = 1;
                        } else {
                            SecureDocument secureDocument = imageLocation.secureDocument;
                            if (secureDocument != null) {
                                cacheImage.secureDocument = secureDocument;
                                int i26 = secureDocument.secureFile.dc_id == Integer.MIN_VALUE ? 1 : i7;
                                file = new File(FileLoader.getDirectory(4), str);
                                i15 = i6;
                                i17 = i9;
                                i16 = i26;
                                str8 = str;
                                i19 = 1;
                            } else {
                                String str12 = "application/x-tgwallpattern";
                                int i27 = i9;
                                if (AUTOPLAY_FILTER.equals(str5) || imageLoader5.isAnimatedAvatar(str5)) {
                                    document3 = imageLocation.document;
                                    if (document3 != null) {
                                        i20 = i13;
                                        if (document3 instanceof TLRPC.TL_documentEncrypted) {
                                            file5 = new File(FileLoader.getDirectory(4), str);
                                        } else if (MessageObject.isVideoDocument(document3)) {
                                            file5 = new File(FileLoader.getDirectory(2), str);
                                        } else {
                                            file5 = new File(FileLoader.getDirectory(3), str);
                                        }
                                        if ((!imageLoader5.isAnimatedAvatar(str5) || AUTOPLAY_FILTER.equals(str5)) && !file5.exists()) {
                                            file5 = new File(FileLoader.getDirectory(4), document3.dc_id + "_" + document3.id + ".temp");
                                        }
                                        file = file5;
                                        if (document3 instanceof DocumentObject.ThemeDocument) {
                                            if (((DocumentObject.ThemeDocument) document3).wallpaper == null) {
                                                i20 = 1;
                                            }
                                            cacheImage.imageType = 5;
                                            i21 = i20;
                                        } else {
                                            if ("application/x-tgsdice".equals(imageLocation.document.mime_type)) {
                                                cacheImage.imageType = 1;
                                                i21 = 1;
                                            } else {
                                                if ("application/x-tgsticker".equals(document3.mime_type)) {
                                                    cacheImage.imageType = 1;
                                                } else if (str12.equals(document3.mime_type) || FileLoader.getDocumentFileName(imageLocation.document).endsWith(".svg")) {
                                                    cacheImage.imageType = 3;
                                                }
                                                i21 = i20;
                                            }
                                            i19 = 1;
                                            imageLoader3 = this;
                                            str8 = str;
                                            i17 = i27;
                                            i15 = i6;
                                            i16 = i21;
                                            j3 = document3.size;
                                            str9 = AUTOPLAY_FILTER;
                                        }
                                        i19 = 1;
                                        imageLoader3 = this;
                                        str8 = str;
                                        i17 = i27;
                                        i15 = i6;
                                        i16 = i21;
                                        j3 = document3.size;
                                        str9 = AUTOPLAY_FILTER;
                                    } else {
                                        i16 = i13;
                                        if (imageLocation.webFile != null) {
                                            str8 = str;
                                            file = new File(FileLoader.getDirectory(3), str8);
                                            i19 = 1;
                                            imageLoader3 = this;
                                            i17 = i27;
                                            str9 = AUTOPLAY_FILTER;
                                            j3 = 0;
                                            i15 = i6;
                                        } else {
                                            str8 = str;
                                            i19 = 1;
                                            if (i6 == 1) {
                                                file4 = new File(FileLoader.getDirectory(4), str8);
                                            } else {
                                                file4 = new File(FileLoader.getDirectory(i7), str8);
                                            }
                                            imageLoader4 = this;
                                            if (imageLoader4.isAnimatedAvatar(str5)) {
                                                str9 = AUTOPLAY_FILTER;
                                            } else {
                                                str9 = AUTOPLAY_FILTER;
                                                if (str9.equals(str5) && imageLocation.location != null && !file4.exists()) {
                                                }
                                                j3 = 0;
                                                i15 = i6;
                                                file = file4;
                                                i17 = i27;
                                                imageLoader3 = imageLoader4;
                                            }
                                            file4 = new File(FileLoader.getDirectory(4), imageLocation.location.volume_id + "_" + imageLocation.location.local_id + ".temp");
                                            j3 = 0;
                                            i15 = i6;
                                            file = file4;
                                            i17 = i27;
                                            imageLoader3 = imageLoader4;
                                        }
                                    }
                                } else if (i6 != 0 || j <= 0 || imageLocation.path != null || zIsEncrypted) {
                                    File file6 = new File(FileLoader.getDirectory(4), str);
                                    if (file6.exists()) {
                                        i22 = i6;
                                        i23 = 1;
                                    } else {
                                        i22 = i6;
                                        if (i22 == 2) {
                                            file6 = new File(FileLoader.getDirectory(4), str + ".enc");
                                        }
                                        i23 = i27;
                                    }
                                    TLRPC.Document document5 = imageLocation.document;
                                    if (document5 != null) {
                                        if (document5 instanceof DocumentObject.ThemeDocument) {
                                            if (((DocumentObject.ThemeDocument) document5).wallpaper == null) {
                                                i13 = 1;
                                            }
                                            cacheImage.imageType = 5;
                                        } else if ("application/x-tgsdice".equals(document5.mime_type)) {
                                            cacheImage.imageType = 1;
                                            imageLoader3 = imageLoader5;
                                            i16 = 1;
                                            i17 = i23;
                                            i15 = i22;
                                            str9 = AUTOPLAY_FILTER;
                                            file = file6;
                                            str8 = str;
                                            j3 = 0;
                                            i19 = 1;
                                        } else if ("application/x-tgsticker".equals(imageLocation.document.mime_type)) {
                                            cacheImage.imageType = 1;
                                        } else if ("application/x-tgwallpattern".equals(imageLocation.document.mime_type) || FileLoader.getDocumentFileName(imageLocation.document).endsWith(".svg")) {
                                            cacheImage.imageType = 3;
                                        }
                                    }
                                    i16 = i13;
                                    str8 = str;
                                    i15 = i22;
                                    i19 = 1;
                                    i17 = i23;
                                    file = file6;
                                } else {
                                    str12 = "application/x-tgwallpattern";
                                    document3 = imageLocation.document;
                                    if (document3 != null) {
                                        i20 = i13;
                                        if (document3 instanceof TLRPC.TL_documentEncrypted) {
                                            file5 = new File(FileLoader.getDirectory(4), str);
                                        } else if (MessageObject.isVideoDocument(document3)) {
                                            file5 = new File(FileLoader.getDirectory(2), str);
                                        } else {
                                            file5 = new File(FileLoader.getDirectory(3), str);
                                        }
                                        if (!imageLoader5.isAnimatedAvatar(str5)) {
                                            file5 = new File(FileLoader.getDirectory(4), document3.dc_id + "_" + document3.id + ".temp");
                                        } else {
                                            file5 = new File(FileLoader.getDirectory(4), document3.dc_id + "_" + document3.id + ".temp");
                                        }
                                        file = file5;
                                        if (document3 instanceof DocumentObject.ThemeDocument) {
                                            if (((DocumentObject.ThemeDocument) document3).wallpaper == null) {
                                                i20 = 1;
                                            }
                                            cacheImage.imageType = 5;
                                            i21 = i20;
                                        } else {
                                            if ("application/x-tgsdice".equals(imageLocation.document.mime_type)) {
                                                cacheImage.imageType = 1;
                                                i21 = 1;
                                            } else {
                                                if ("application/x-tgsticker".equals(document3.mime_type)) {
                                                    cacheImage.imageType = 1;
                                                } else if (str12.equals(document3.mime_type)) {
                                                    cacheImage.imageType = 3;
                                                } else {
                                                    cacheImage.imageType = 3;
                                                }
                                                i21 = i20;
                                            }
                                            i19 = 1;
                                            imageLoader3 = this;
                                            str8 = str;
                                            i17 = i27;
                                            i15 = i6;
                                            i16 = i21;
                                            j3 = document3.size;
                                            str9 = AUTOPLAY_FILTER;
                                        }
                                        i19 = 1;
                                        imageLoader3 = this;
                                        str8 = str;
                                        i17 = i27;
                                        i15 = i6;
                                        i16 = i21;
                                        j3 = document3.size;
                                        str9 = AUTOPLAY_FILTER;
                                    } else {
                                        i16 = i13;
                                        if (imageLocation.webFile != null) {
                                            str8 = str;
                                            file = new File(FileLoader.getDirectory(3), str8);
                                            i19 = 1;
                                            imageLoader3 = this;
                                            i17 = i27;
                                            str9 = AUTOPLAY_FILTER;
                                            j3 = 0;
                                            i15 = i6;
                                        } else {
                                            str8 = str;
                                            i19 = 1;
                                            if (i6 == 1) {
                                                file4 = new File(FileLoader.getDirectory(4), str8);
                                            } else {
                                                file4 = new File(FileLoader.getDirectory(i7), str8);
                                            }
                                            imageLoader4 = this;
                                            if (imageLoader4.isAnimatedAvatar(str5)) {
                                                str9 = AUTOPLAY_FILTER;
                                                if (str9.equals(str5)) {
                                                }
                                                j3 = 0;
                                                i15 = i6;
                                                file = file4;
                                                i17 = i27;
                                                imageLoader3 = imageLoader4;
                                            } else {
                                                str9 = AUTOPLAY_FILTER;
                                            }
                                            file4 = new File(FileLoader.getDirectory(4), imageLocation.location.volume_id + "_" + imageLocation.location.local_id + ".temp");
                                            j3 = 0;
                                            i15 = i6;
                                            file = file4;
                                            i17 = i27;
                                            imageLoader3 = imageLoader4;
                                        }
                                    }
                                }
                            }
                            j3 = 0;
                            imageLoader3 = imageLoader5;
                        }
                        if (!hasAutoplayFilter(str5) || imageLoader3.isAnimatedAvatar(str5)) {
                            imageLoader = imageLoader3;
                            cacheImage.imageType = 2;
                            cacheImage.size = j3;
                            cacheImage.isPFrame = imageLoader3.isPFrame(str5);
                            if (!str9.equals(str5) || imageLoader3.isAnimatedAvatar(str5)) {
                                imageLoader = imageLoader3;
                                i16 = i19;
                                imageLoader2 = imageLoader3;
                            }
                        }
                        cacheImage.type = i4;
                        cacheImage.key = str2;
                        cacheImage.cacheType = i15;
                        cacheImage.filter = str5;
                        cacheImage.imageLocation = imageLocation;
                        cacheImage.ext = str4;
                        cacheImage.currentAccount = i5;
                        cacheImage.parentObject = obj;
                        i18 = imageLocation.imageType;
                        if (i18 != 0) {
                            cacheImage.imageType = i18;
                        }
                        if (i15 == 2) {
                            cacheImage.encryptionKeyPath = new File(FileLoader.getInternalCacheDir(), str8 + ".enc.key");
                        }
                        String str13 = str6;
                        cacheImage.addImageReceiver(imageReceiver, str2, str5, i4, i3);
                        if (i16 == 0 || i17 != 0 || file.exists()) {
                            cacheImage.finalFilePath = file;
                            cacheImage.imageLocation = imageLocation;
                            cacheImage.cacheTask = imageLoader2.new CacheOutTask(cacheImage);
                            imageLoader2.imageLoadingByKeys.put(str2, cacheImage);
                            imageLoader2.imageLoadingKeys.add(cutFilter(str2));
                            if (i != 0) {
                                imageLoader2.cacheThumbOutQueue.postRunnable(cacheImage.cacheTask);
                            } else {
                                cacheImage.runningTask = imageLoader2.cacheOutQueue.postRunnable(cacheImage.cacheTask, cacheImage.priority);
                            }
                        }
                        cacheImage.url = str8;
                        imageLoader2.imageLoadingByUrl.put(str8, cacheImage);
                        if (cacheImage.isPFrame) {
                            imageLoader2.imageLoadingByUrlPframe.put(str8, cacheImage);
                        }
                        String str14 = imageLocation.path;
                        if (str14 != null) {
                            String strMD5 = Utilities.MD5(str14);
                            cacheImage.tempFilePath = new File(FileLoader.getDirectory(4), strMD5 + "_temp.jpg");
                            cacheImage.finalFilePath = file;
                            if (imageLocation.path.startsWith(str13)) {
                                ArtworkLoadTask artworkLoadTask = imageLoader2.new ArtworkLoadTask(cacheImage);
                                cacheImage.artworkTask = artworkLoadTask;
                                imageLoader2.artworkTasks.add(artworkLoadTask);
                                imageLoader2.runArtworkTasks(i7);
                                return;
                            }
                            HttpImageTask httpImageTask = imageLoader2.new HttpImageTask(cacheImage, j);
                            cacheImage.httpTask = httpImageTask;
                            imageLoader2.httpTasks.add(httpImageTask);
                            imageLoader2.runHttpTasks(i7);
                            return;
                        }
                        int i28 = i7;
                        int fileLoadingPriority = i != 0 ? 3 : imageReceiver.getFileLoadingPriority();
                        if (imageLocation.location != null) {
                            FileLoader.getInstance(i5).loadFile(imageLocation, obj, str4, fileLoadingPriority, (i15 != 0 || (j > j2 && imageLocation.key == null)) ? i15 : 1);
                        } else if (imageLocation.document != null) {
                            FileLoader.getInstance(i5).loadFile(imageLocation.document, obj, fileLoadingPriority, i15);
                        } else if (imageLocation.secureDocument != null) {
                            FileLoader.getInstance(i5).loadFile(imageLocation.secureDocument, fileLoadingPriority);
                        } else if (imageLocation.webFile != null) {
                            FileLoader.getInstance(i5).loadFile(imageLocation.webFile, fileLoadingPriority, i15);
                        }
                        if (imageReceiver.isForceLoding()) {
                            imageLoader2.forceLoadingImages.put(cacheImage.key, Integer.valueOf(i28));
                            return;
                        }
                        return;
                    }
                    str8 = str;
                    i15 = i6;
                    i16 = i13;
                    j2 = 0;
                    imageLoader = imageLoader5;
                    i17 = i9;
                    imageLoader = imageLoader3;
                    imageLoader = imageLoader3;
                    imageLoader2 = imageLoader;
                    cacheImage.type = i4;
                    cacheImage.key = str2;
                    cacheImage.cacheType = i15;
                    cacheImage.filter = str5;
                    cacheImage.imageLocation = imageLocation;
                    cacheImage.ext = str4;
                    cacheImage.currentAccount = i5;
                    cacheImage.parentObject = obj;
                    i18 = imageLocation.imageType;
                    if (i18 != 0) {
                        cacheImage.imageType = i18;
                    }
                    if (i15 == 2) {
                        cacheImage.encryptionKeyPath = new File(FileLoader.getInternalCacheDir(), str8 + ".enc.key");
                    }
                    String str15 = str6;
                    cacheImage.addImageReceiver(imageReceiver, str2, str5, i4, i3);
                    if (i16 == 0) {
                    }
                    cacheImage.finalFilePath = file;
                    cacheImage.imageLocation = imageLocation;
                    cacheImage.cacheTask = imageLoader2.new CacheOutTask(cacheImage);
                    imageLoader2.imageLoadingByKeys.put(str2, cacheImage);
                    imageLoader2.imageLoadingKeys.add(cutFilter(str2));
                    if (i != 0) {
                        imageLoader2.cacheThumbOutQueue.postRunnable(cacheImage.cacheTask);
                    } else {
                        cacheImage.runningTask = imageLoader2.cacheOutQueue.postRunnable(cacheImage.cacheTask, cacheImage.priority);
                    }
                }
            }
            i11 = 2;
            if (i != i11) {
                boolean zIsEncrypted2 = imageLocation.isEncrypted();
                i13 = i10;
                cacheImage = new CacheImage();
                if (imageReceiver2.getFileLoadingPriority() == 0) {
                    i14 = i7;
                } else {
                    i14 = 1;
                }
                cacheImage.priority = i14;
                if (!z) {
                    if (imageLocation.imageType != i11) {
                        cacheImage.imageType = i11;
                    } else {
                        cacheImage.imageType = i11;
                    }
                }
                if (file == null) {
                    TLRPC.PhotoSize photoSize2 = imageLocation.photoSize;
                    j2 = 0;
                    z5 = photoSize2 instanceof TLRPC.TL_photoStrippedSize;
                    str9 = AUTOPLAY_FILTER;
                    if (z5) {
                        str8 = str;
                        i15 = i6;
                        imageLoader3 = imageLoader5;
                        int i29 = i9;
                        str9 = AUTOPLAY_FILTER;
                        i19 = 1;
                        i17 = i29;
                        j3 = 0;
                        i16 = 1;
                    } else {
                        str8 = str;
                        i15 = i6;
                        imageLoader3 = imageLoader5;
                        int i210 = i9;
                        str9 = AUTOPLAY_FILTER;
                        i19 = 1;
                        i17 = i210;
                        j3 = 0;
                        i16 = 1;
                    }
                    if (!hasAutoplayFilter(str5)) {
                        imageLoader = imageLoader3;
                        cacheImage.imageType = 2;
                        cacheImage.size = j3;
                        cacheImage.isPFrame = imageLoader3.isPFrame(str5);
                        if (!str9.equals(str5)) {
                        }
                        imageLoader = imageLoader3;
                        i16 = i19;
                        imageLoader2 = imageLoader3;
                    } else {
                        imageLoader = imageLoader3;
                        cacheImage.imageType = 2;
                        cacheImage.size = j3;
                        cacheImage.isPFrame = imageLoader3.isPFrame(str5);
                        if (!str9.equals(str5)) {
                        }
                        imageLoader = imageLoader3;
                        i16 = i19;
                        imageLoader2 = imageLoader3;
                    }
                    cacheImage.type = i4;
                    cacheImage.key = str2;
                    cacheImage.cacheType = i15;
                    cacheImage.filter = str5;
                    cacheImage.imageLocation = imageLocation;
                    cacheImage.ext = str4;
                    cacheImage.currentAccount = i5;
                    cacheImage.parentObject = obj;
                    i18 = imageLocation.imageType;
                    if (i18 != 0) {
                        cacheImage.imageType = i18;
                    }
                    if (i15 == 2) {
                        cacheImage.encryptionKeyPath = new File(FileLoader.getInternalCacheDir(), str8 + ".enc.key");
                    }
                    String str16 = str6;
                    cacheImage.addImageReceiver(imageReceiver, str2, str5, i4, i3);
                    if (i16 == 0) {
                    }
                    cacheImage.finalFilePath = file;
                    cacheImage.imageLocation = imageLocation;
                    cacheImage.cacheTask = imageLoader2.new CacheOutTask(cacheImage);
                    imageLoader2.imageLoadingByKeys.put(str2, cacheImage);
                    imageLoader2.imageLoadingKeys.add(cutFilter(str2));
                    if (i != 0) {
                        imageLoader2.cacheThumbOutQueue.postRunnable(cacheImage.cacheTask);
                    } else {
                        cacheImage.runningTask = imageLoader2.cacheOutQueue.postRunnable(cacheImage.cacheTask, cacheImage.priority);
                    }
                }
                str8 = str;
                i15 = i6;
                i16 = i13;
                j2 = 0;
                imageLoader = imageLoader5;
                i17 = i9;
                imageLoader = imageLoader3;
                imageLoader = imageLoader3;
                imageLoader2 = imageLoader;
                cacheImage.type = i4;
                cacheImage.key = str2;
                cacheImage.cacheType = i15;
                cacheImage.filter = str5;
                cacheImage.imageLocation = imageLocation;
                cacheImage.ext = str4;
                cacheImage.currentAccount = i5;
                cacheImage.parentObject = obj;
                i18 = imageLocation.imageType;
                if (i18 != 0) {
                    cacheImage.imageType = i18;
                }
                if (i15 == 2) {
                    cacheImage.encryptionKeyPath = new File(FileLoader.getInternalCacheDir(), str8 + ".enc.key");
                }
                String str17 = str6;
                cacheImage.addImageReceiver(imageReceiver, str2, str5, i4, i3);
                if (i16 == 0) {
                }
                cacheImage.finalFilePath = file;
                cacheImage.imageLocation = imageLocation;
                cacheImage.cacheTask = imageLoader2.new CacheOutTask(cacheImage);
                imageLoader2.imageLoadingByKeys.put(str2, cacheImage);
                imageLoader2.imageLoadingKeys.add(cutFilter(str2));
                if (i != 0) {
                    imageLoader2.cacheThumbOutQueue.postRunnable(cacheImage.cacheTask);
                } else {
                    cacheImage.runningTask = imageLoader2.cacheOutQueue.postRunnable(cacheImage.cacheTask, cacheImage.priority);
                }
            }
        }
    }

    public void preloadArtwork(final String str) {
        this.imageLoadQueue.postRunnable(new Runnable() { // from class: org.telegram.messenger.ImageLoader$$ExternalSyntheticLambda2
            @Override // java.lang.Runnable
            public final void run() {
                this.f$0.lambda$preloadArtwork$8(str);
            }
        });
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$preloadArtwork$8(String str) {
        String httpUrlExtension = getHttpUrlExtension(str, "jpg");
        String str2 = Utilities.MD5(str) + "." + httpUrlExtension;
        File file = new File(FileLoader.getDirectory(4), str2);
        if (file.exists()) {
            return;
        }
        ImageLocation forPath = ImageLocation.getForPath(str);
        CacheImage cacheImage = new CacheImage();
        cacheImage.type = 1;
        cacheImage.key = Utilities.MD5(str);
        cacheImage.filter = null;
        cacheImage.imageLocation = forPath;
        cacheImage.ext = httpUrlExtension;
        cacheImage.parentObject = null;
        int i = forPath.imageType;
        if (i != 0) {
            cacheImage.imageType = i;
        }
        cacheImage.url = str2;
        this.imageLoadingByUrl.put(str2, cacheImage);
        String strMD5 = Utilities.MD5(forPath.path);
        cacheImage.tempFilePath = new File(FileLoader.getDirectory(4), strMD5 + "_temp.jpg");
        cacheImage.finalFilePath = file;
        ArtworkLoadTask artworkLoadTask = new ArtworkLoadTask(cacheImage);
        cacheImage.artworkTask = artworkLoadTask;
        this.artworkTasks.add(artworkLoadTask);
        runArtworkTasks(false);
    }

    public void loadImageForImageReceiver(ImageReceiver imageReceiver) {
        loadImageForImageReceiver(imageReceiver, null);
    }

    /* JADX WARN: Code duplicated, block: B:108:0x01bd  */
    /* JADX WARN: Code duplicated, block: B:169:0x02a2  */
    /* JADX WARN: Code duplicated, block: B:70:0x0112  */
    /* JADX WARN: Code duplicated, block: B:95:0x0174  */
    public void loadImageForImageReceiver(ImageReceiver imageReceiver, List<ImageReceiver> list) {
        boolean z;
        boolean z2;
        boolean z3;
        ImageReceiver imageReceiver2;
        boolean z4;
        boolean z5;
        ImageLocation forDocument;
        ImageLocation imageLocation;
        int i;
        String str;
        String str2;
        ImageLocation imageLocation2;
        int i2;
        boolean z6;
        boolean z7;
        ImageLocation imageLocation3;
        String str3;
        BitmapDrawable fromLottieCache;
        boolean zHasBitmap;
        ImageReceiver imageReceiver3 = imageReceiver;
        if (imageReceiver3 == null) {
            return;
        }
        String mediaKey = imageReceiver3.getMediaKey();
        int newGuid = imageReceiver3.getNewGuid();
        if (mediaKey != null) {
            ImageLocation mediaLocation = imageReceiver3.getMediaLocation();
            BitmapDrawable bitmapDrawableFindInPreloadImageReceivers = findInPreloadImageReceivers(mediaKey, list);
            if (bitmapDrawableFindInPreloadImageReceivers == null) {
                if (useLottieMemCache(mediaLocation, mediaKey)) {
                    bitmapDrawableFindInPreloadImageReceivers = getFromLottieCache(mediaKey);
                } else {
                    BitmapDrawable bitmapDrawable = this.memCache.get(mediaKey);
                    if (bitmapDrawable != null) {
                        this.memCache.moveToFront(mediaKey);
                    }
                    if (bitmapDrawable == null && (bitmapDrawable = this.smallImagesMemCache.get(mediaKey)) != null) {
                        this.smallImagesMemCache.moveToFront(mediaKey);
                    }
                    bitmapDrawableFindInPreloadImageReceivers = bitmapDrawable;
                    if (bitmapDrawableFindInPreloadImageReceivers == null && (bitmapDrawableFindInPreloadImageReceivers = this.wallpaperMemCache.get(mediaKey)) != null) {
                        this.wallpaperMemCache.moveToFront(mediaKey);
                    }
                }
            }
            Drawable drawable = bitmapDrawableFindInPreloadImageReceivers;
            if (drawable instanceof RLottieDrawable) {
                zHasBitmap = ((RLottieDrawable) drawable).hasBitmap();
            } else {
                zHasBitmap = drawable instanceof AnimatedFileDrawable ? ((AnimatedFileDrawable) drawable).hasBitmap() : true;
            }
            if (zHasBitmap && drawable != null) {
                cancelLoadingForImageReceiver(imageReceiver3, true);
                imageReceiver3.setImageBitmapByKey(drawable, mediaKey, 3, true, newGuid);
                if (!imageReceiver.isForcePreview()) {
                    return;
                }
                imageReceiver3 = imageReceiver;
                z2 = false;
                z = true;
            } else if (drawable != null) {
                imageReceiver3 = imageReceiver;
                imageReceiver3.setImageBitmapByKey(drawable, mediaKey, 3, true, newGuid);
                z = false;
                z2 = true;
            } else {
                imageReceiver3 = imageReceiver;
                z = false;
                z2 = false;
            }
        } else {
            z = false;
            z2 = false;
        }
        String imageKey = imageReceiver3.getImageKey();
        if (z || imageKey == null) {
            z3 = z;
        } else {
            ImageLocation imageLocation4 = imageReceiver3.getImageLocation();
            BitmapDrawable bitmapDrawableFindInPreloadImageReceivers2 = findInPreloadImageReceivers(imageKey, list);
            if (bitmapDrawableFindInPreloadImageReceivers2 == null && useLottieMemCache(imageLocation4, imageKey)) {
                bitmapDrawableFindInPreloadImageReceivers2 = getFromLottieCache(imageKey);
            }
            if (bitmapDrawableFindInPreloadImageReceivers2 == null) {
                BitmapDrawable bitmapDrawable2 = this.memCache.get(imageKey);
                if (bitmapDrawable2 != null) {
                    this.memCache.moveToFront(imageKey);
                }
                if (bitmapDrawable2 == null && (bitmapDrawable2 = this.smallImagesMemCache.get(imageKey)) != null) {
                    this.smallImagesMemCache.moveToFront(imageKey);
                }
                bitmapDrawableFindInPreloadImageReceivers2 = bitmapDrawable2;
                if (bitmapDrawableFindInPreloadImageReceivers2 == null && (bitmapDrawableFindInPreloadImageReceivers2 = this.wallpaperMemCache.get(imageKey)) != null) {
                    this.wallpaperMemCache.moveToFront(imageKey);
                }
            }
            if (bitmapDrawableFindInPreloadImageReceivers2 != null) {
                cancelLoadingForImageReceiver(imageReceiver3, true);
                imageReceiver3.setImageBitmapByKey(bitmapDrawableFindInPreloadImageReceivers2, imageKey, 0, true, newGuid);
                if (!imageReceiver.isForcePreview() && (mediaKey == null || z2)) {
                    return;
                } else {
                    z3 = true;
                }
            } else {
                z3 = z;
            }
        }
        String thumbKey = imageReceiver.getThumbKey();
        if (thumbKey == null) {
            imageReceiver2 = imageReceiver;
            z4 = false;
        } else {
            if (useLottieMemCache(imageReceiver.getThumbLocation(), thumbKey)) {
                fromLottieCache = getFromLottieCache(thumbKey);
            } else {
                fromLottieCache = this.memCache.get(thumbKey);
                if (fromLottieCache != null) {
                    this.memCache.moveToFront(thumbKey);
                }
                if (fromLottieCache == null && (fromLottieCache = this.smallImagesMemCache.get(thumbKey)) != null) {
                    this.smallImagesMemCache.moveToFront(thumbKey);
                }
                if (fromLottieCache == null && (fromLottieCache = this.wallpaperMemCache.get(thumbKey)) != null) {
                    this.wallpaperMemCache.moveToFront(thumbKey);
                }
            }
            BitmapDrawable bitmapDrawable3 = fromLottieCache;
            if (bitmapDrawable3 != null) {
                imageReceiver2 = imageReceiver;
                imageReceiver2.setImageBitmapByKey(bitmapDrawable3, thumbKey, 1, true, newGuid);
                cancelLoadingForImageReceiver(imageReceiver2, false);
                if (z3 && imageReceiver2.isForcePreview()) {
                    return;
                } else {
                    z4 = true;
                }
            } else {
                imageReceiver2 = imageReceiver;
                z4 = false;
            }
        }
        Object parentObject = imageReceiver2.getParentObject();
        TLRPC.Document qualityThumbDocument = imageReceiver2.getQualityThumbDocument();
        ImageLocation thumbLocation = imageReceiver2.getThumbLocation();
        String thumbFilter = imageReceiver2.getThumbFilter();
        ImageLocation mediaLocation2 = imageReceiver2.getMediaLocation();
        String mediaFilter = imageReceiver2.getMediaFilter();
        ImageLocation imageLocation5 = imageReceiver2.getImageLocation();
        String imageFilter = imageReceiver2.getImageFilter();
        if (imageLocation5 == null && imageReceiver2.isNeedsQualityThumb() && imageReceiver2.isCurrentKeyQuality()) {
            if (parentObject instanceof MessageObject) {
                forDocument = ImageLocation.getForDocument(((MessageObject) parentObject).getDocument());
            } else if (qualityThumbDocument != null) {
                forDocument = ImageLocation.getForDocument(qualityThumbDocument);
            } else {
                z5 = false;
                forDocument = imageLocation5;
            }
            z5 = true;
        } else {
            z5 = false;
            forDocument = imageLocation5;
        }
        String str4 = null;
        String str5 = (forDocument == null || forDocument.imageType != 2) ? null : "mp4";
        String str6 = (mediaLocation2 == null || mediaLocation2.imageType != 2) ? null : "mp4";
        String ext = imageReceiver2.getExt();
        if (ext == null) {
            ext = "jpg";
        }
        String str7 = str5 == null ? ext : str5;
        if (str6 == null) {
            str6 = ext;
        }
        boolean z8 = z4;
        ImageLocation imageLocation6 = mediaLocation2;
        boolean z9 = z5;
        String str8 = null;
        String str9 = null;
        String str10 = null;
        String str11 = null;
        int i3 = 0;
        boolean z10 = false;
        while (true) {
            imageLocation = imageLocation5;
            if (i3 >= 2) {
                break;
            }
            if (i3 == 0) {
                imageLocation2 = forDocument;
                str2 = str7;
            } else {
                str2 = str6;
                imageLocation2 = imageLocation6;
            }
            if (imageLocation2 == null) {
                i2 = newGuid;
                z6 = z2;
            } else {
                i2 = newGuid;
                z6 = z2;
                String key = imageLocation2.getKey(parentObject, imageLocation6 != null ? imageLocation6 : forDocument, false);
                if (key != null) {
                    z7 = z3;
                    String key2 = imageLocation2.getKey(parentObject, imageLocation6 != null ? imageLocation6 : forDocument, true);
                    if (imageLocation2.path != null) {
                        key2 = key2 + "." + getHttpUrlExtension(imageLocation2.path, "jpg");
                        imageLocation3 = forDocument;
                    } else {
                        TLRPC.PhotoSize photoSize = imageLocation2.photoSize;
                        imageLocation3 = forDocument;
                        if ((photoSize instanceof TLRPC.TL_photoStrippedSize) || (photoSize instanceof TLRPC.TL_photoPathSize)) {
                            key2 = key2 + "." + str2;
                        } else if (imageLocation2.location != null) {
                            String str12 = key2 + "." + str2;
                            if (imageReceiver.getExt() == null) {
                                TLRPC.TL_fileLocationToBeDeprecated tL_fileLocationToBeDeprecated = imageLocation2.location;
                                if (tL_fileLocationToBeDeprecated.key == null) {
                                    str3 = str12;
                                    if (tL_fileLocationToBeDeprecated.volume_id != -2147483648L || tL_fileLocationToBeDeprecated.local_id >= 0) {
                                        key2 = str3;
                                    }
                                } else {
                                    str3 = str12;
                                }
                                key2 = str3;
                            } else {
                                str3 = str12;
                                key2 = str3;
                            }
                        } else {
                            WebFile webFile = imageLocation2.webFile;
                            if (webFile != null) {
                                key2 = key2 + "." + getHttpUrlExtension(imageLocation2.webFile.url, FileLoader.getMimeTypePart(webFile.mime_type));
                            } else if (imageLocation2.secureDocument != null) {
                                key2 = key2 + "." + str2;
                            } else if (imageLocation2.document != null) {
                                if (i3 == 0 && z9) {
                                    key = "q_" + key;
                                }
                                String documentFileName = FileLoader.getDocumentFileName(imageLocation2.document);
                                int iLastIndexOf = documentFileName.lastIndexOf(46);
                                String str13 = _UrlKt.FRAGMENT_ENCODE_SET;
                                String strSubstring = iLastIndexOf == -1 ? _UrlKt.FRAGMENT_ENCODE_SET : documentFileName.substring(iLastIndexOf);
                                if (strSubstring.length() > 1) {
                                    str13 = strSubstring;
                                } else if ("video/mp4".equals(imageLocation2.document.mime_type)) {
                                    str13 = ".mp4";
                                } else if ("video/x-matroska".equals(imageLocation2.document.mime_type)) {
                                    str13 = ".mkv";
                                }
                                key2 = key2 + str13;
                                z10 = (MessageObject.isVideoDocument(imageLocation2.document) || MessageObject.isGifDocument(imageLocation2.document) || MessageObject.isRoundVideoDocument(imageLocation2.document) || MessageObject.canPreviewDocument(imageLocation2.document)) ? false : true;
                            } else if (parentObject instanceof TLRPC.StickerSet) {
                                key2 = key2 + "." + str2;
                            }
                        }
                    }
                    if (i3 == 0) {
                        str9 = key;
                        str10 = key2;
                    } else {
                        str8 = key;
                        str11 = key2;
                    }
                    if (imageLocation2 != thumbLocation) {
                        forDocument = imageLocation3;
                    } else if (i3 == 0) {
                        str9 = null;
                        forDocument = null;
                        str10 = null;
                    } else {
                        str8 = null;
                        imageLocation6 = null;
                        str11 = null;
                        forDocument = imageLocation3;
                    }
                }
                i3++;
                imageLocation5 = imageLocation;
                newGuid = i2;
                z2 = z6;
                z3 = z7;
            }
            z7 = z3;
            i3++;
            imageLocation5 = imageLocation;
            newGuid = i2;
            z2 = z6;
            z3 = z7;
        }
        ImageLocation imageLocation7 = forDocument;
        int i4 = newGuid;
        boolean z11 = z2;
        boolean z12 = z3;
        if (thumbLocation != null) {
            ImageLocation strippedLocation = imageReceiver.getStrippedLocation();
            if (strippedLocation == null) {
                strippedLocation = imageLocation6 != null ? imageLocation6 : imageLocation;
            }
            String key3 = thumbLocation.getKey(parentObject, strippedLocation, false);
            i = 1;
            String key4 = thumbLocation.getKey(parentObject, strippedLocation, true);
            if (thumbLocation.path != null) {
                key4 = key4 + "." + getHttpUrlExtension(thumbLocation.path, "jpg");
            } else {
                TLRPC.PhotoSize photoSize2 = thumbLocation.photoSize;
                if ((photoSize2 instanceof TLRPC.TL_photoStrippedSize) || (photoSize2 instanceof TLRPC.TL_photoPathSize)) {
                    key4 = key4 + "." + ext;
                } else if (thumbLocation.location != null) {
                    key4 = key4 + "." + ext;
                }
            }
            str4 = key4;
            str = key3;
        } else {
            i = 1;
            str = null;
        }
        if (str8 != null && mediaFilter != null) {
            str8 = str8 + "@" + mediaFilter;
        }
        if (str9 != null && imageFilter != null) {
            str9 = str9 + "@" + imageFilter;
        }
        if (str != null && thumbFilter != null) {
            str = str + "@" + thumbFilter;
        }
        if (imageReceiver.getUniqKeyPrefix() != null && str9 != null) {
            str9 = imageReceiver.getUniqKeyPrefix() + str9;
        }
        String str14 = str9;
        if (imageReceiver.getUniqKeyPrefix() != null && str8 != null) {
            str8 = imageReceiver.getUniqKeyPrefix() + str8;
        }
        String str15 = str8;
        if (imageLocation7 != null) {
            if (imageLocation7.path != null) {
                createLoadOperationForImageReceiver(imageReceiver, str, str4, ext, thumbLocation, thumbFilter, 0L, 1, 1, z8 ? 2 : i, i4);
                createLoadOperationForImageReceiver(imageReceiver, str14, str10, str7, imageLocation7, imageFilter, imageReceiver.getSize(), 1, 0, 0, i4);
                return;
            }
            imageLocation7 = imageLocation7;
        }
        if (imageLocation6 != null) {
            int cacheType = imageReceiver.getCacheType();
            int i5 = (cacheType == 0 && z10) ? i : cacheType;
            int i6 = i5 == 0 ? i : i5;
            if (!z8) {
                createLoadOperationForImageReceiver(imageReceiver, str, str4, ext, thumbLocation, thumbFilter, 0L, i6, 1, 1, i4);
            }
            if (!z12) {
                createLoadOperationForImageReceiver(imageReceiver, str14, str10, str7, imageLocation7, imageFilter, 0L, 1, 0, 0, i4);
            }
            if (z11) {
                return;
            }
            createLoadOperationForImageReceiver(imageReceiver, str15, str11, str6, imageLocation6, mediaFilter, imageReceiver.getSize(), i5, 3, 0, i4);
            return;
        }
        int cacheType2 = imageReceiver.getCacheType();
        int i7 = (cacheType2 == 0 && z10) ? i : cacheType2;
        createLoadOperationForImageReceiver(imageReceiver, str, str4, ext, thumbLocation, thumbFilter, 0L, i7 == 0 ? i : i7, 1, z8 ? 2 : i, i4);
        createLoadOperationForImageReceiver(imageReceiver, str14, str10, str7, imageLocation7, imageFilter, imageReceiver.getSize(), i7, 0, 0, i4);
    }

    private Drawable findInPreloadImageReceivers(String str, List<ImageReceiver> list) {
        if (list == null) {
            return null;
        }
        for (int i = 0; i < list.size(); i++) {
            ImageReceiver imageReceiver = list.get(i);
            if (str.equals(imageReceiver.getImageKey())) {
                return imageReceiver.getImageDrawable();
            }
            if (str.equals(imageReceiver.getMediaKey())) {
                return imageReceiver.getMediaDrawable();
            }
        }
        return null;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public BitmapDrawable getFromLottieCache(String str) {
        BitmapDrawable bitmapDrawable = this.lottieMemCache.get(str);
        if (!(bitmapDrawable instanceof AnimatedFileDrawable) || !((AnimatedFileDrawable) bitmapDrawable).isRecycled()) {
            return bitmapDrawable;
        }
        this.lottieMemCache.remove(str);
        return null;
    }

    private boolean useLottieMemCache(ImageLocation imageLocation, String str) {
        return (str.endsWith("_firstframe") || str.endsWith("_lastframe") || ((imageLocation == null || (!MessageObject.isAnimatedStickerDocument(imageLocation.document, true) && imageLocation.imageType != 1 && !MessageObject.isVideoSticker(imageLocation.document))) && !isAnimatedAvatar(str))) ? false : true;
    }

    public boolean hasLottieMemCache(String str) {
        LruCache<BitmapDrawable> lruCache = this.lottieMemCache;
        return lruCache != null && lruCache.contains(str);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void httpFileLoadError(final String str) {
        this.imageLoadQueue.postRunnable(new Runnable() { // from class: org.telegram.messenger.ImageLoader$$ExternalSyntheticLambda4
            @Override // java.lang.Runnable
            public final void run() {
                this.f$0.lambda$httpFileLoadError$9(str);
            }
        });
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$httpFileLoadError$9(String str) {
        CacheImage cacheImage = this.imageLoadingByUrl.get(str);
        if (cacheImage == null) {
            return;
        }
        HttpImageTask httpImageTask = cacheImage.httpTask;
        if (httpImageTask != null) {
            HttpImageTask httpImageTask2 = new HttpImageTask(httpImageTask.cacheImage, httpImageTask.imageSize);
            cacheImage.httpTask = httpImageTask2;
            this.httpTasks.add(httpImageTask2);
        }
        runHttpTasks(false);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void artworkLoadError(final String str) {
        this.imageLoadQueue.postRunnable(new Runnable() { // from class: org.telegram.messenger.ImageLoader$$ExternalSyntheticLambda12
            @Override // java.lang.Runnable
            public final void run() {
                this.f$0.lambda$artworkLoadError$10(str);
            }
        });
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$artworkLoadError$10(String str) {
        CacheImage cacheImage = this.imageLoadingByUrl.get(str);
        if (cacheImage == null) {
            return;
        }
        ArtworkLoadTask artworkLoadTask = cacheImage.artworkTask;
        if (artworkLoadTask != null) {
            ArtworkLoadTask artworkLoadTask2 = new ArtworkLoadTask(artworkLoadTask.cacheImage);
            cacheImage.artworkTask = artworkLoadTask2;
            this.artworkTasks.add(artworkLoadTask2);
        }
        runArtworkTasks(false);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void fileDidLoaded(final String str, final File file, final int i) {
        this.imageLoadQueue.postRunnable(new Runnable() { // from class: org.telegram.messenger.ImageLoader$$ExternalSyntheticLambda14
            @Override // java.lang.Runnable
            public final void run() {
                this.f$0.lambda$fileDidLoaded$11(str, i, file);
            }
        });
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$fileDidLoaded$11(String str, int i, File file) {
        ThumbGenerateInfo thumbGenerateInfo = this.waitingForQualityThumb.get(str);
        if (thumbGenerateInfo != null && thumbGenerateInfo.parentDocument != null) {
            generateThumb(i, file, thumbGenerateInfo);
            this.waitingForQualityThumb.remove(str);
        }
        CacheImage cacheImage = this.imageLoadingByUrl.get(str);
        if (cacheImage == null) {
            return;
        }
        this.imageLoadingByUrl.remove(str);
        this.imageLoadingByUrlPframe.remove(str);
        ArrayList arrayList = new ArrayList();
        for (int i2 = 0; i2 < cacheImage.imageReceiverArray.size(); i2++) {
            String str2 = cacheImage.keys.get(i2);
            String str3 = cacheImage.filters.get(i2);
            int iIntValue = cacheImage.types.get(i2).intValue();
            ImageReceiver imageReceiver = cacheImage.imageReceiverArray.get(i2);
            int iIntValue2 = cacheImage.imageReceiverGuidsArray.get(i2).intValue();
            CacheImage cacheImage2 = this.imageLoadingByKeys.get(str2);
            if (cacheImage2 == null) {
                cacheImage2 = new CacheImage();
                cacheImage2.priority = cacheImage.priority;
                cacheImage2.secureDocument = cacheImage.secureDocument;
                cacheImage2.currentAccount = cacheImage.currentAccount;
                cacheImage2.finalFilePath = file;
                cacheImage2.parentObject = cacheImage.parentObject;
                cacheImage2.isPFrame = cacheImage.isPFrame;
                cacheImage2.key = str2;
                cacheImage2.cacheType = cacheImage.cacheType;
                cacheImage2.imageLocation = cacheImage.imageLocation;
                cacheImage2.type = iIntValue;
                cacheImage2.ext = cacheImage.ext;
                cacheImage2.encryptionKeyPath = cacheImage.encryptionKeyPath;
                cacheImage2.cacheTask = new CacheOutTask(cacheImage2);
                cacheImage2.filter = str3;
                cacheImage2.imageType = cacheImage.imageType;
                this.imageLoadingByKeys.put(str2, cacheImage2);
                this.imageLoadingKeys.add(cutFilter(str2));
                arrayList.add(cacheImage2.cacheTask);
            }
            cacheImage2.addImageReceiver(imageReceiver, str2, str3, iIntValue, iIntValue2);
        }
        for (int i3 = 0; i3 < arrayList.size(); i3++) {
            CacheOutTask cacheOutTask = (CacheOutTask) arrayList.get(i3);
            if (cacheOutTask.cacheImage.type == 1) {
                this.cacheThumbOutQueue.postRunnable(cacheOutTask);
            } else {
                this.cacheOutQueue.postRunnable(cacheOutTask, cacheOutTask.cacheImage.priority);
            }
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void fileDidFailedLoad(final String str, int i) {
        if (i == 1) {
            return;
        }
        this.imageLoadQueue.postRunnable(new Runnable() { // from class: org.telegram.messenger.ImageLoader$$ExternalSyntheticLambda9
            @Override // java.lang.Runnable
            public final void run() {
                this.f$0.lambda$fileDidFailedLoad$12(str);
            }
        });
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$fileDidFailedLoad$12(String str) {
        CacheImage cacheImage = this.imageLoadingByUrl.get(str);
        if (cacheImage != null) {
            cacheImage.setImageAndClear(null, null);
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void runHttpTasks(boolean z) {
        if (z) {
            this.currentHttpTasksCount--;
        }
        while (this.currentHttpTasksCount < 4 && !this.httpTasks.isEmpty()) {
            HttpImageTask httpImageTaskPoll = this.httpTasks.poll();
            if (httpImageTaskPoll != null) {
                httpImageTaskPoll.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, null, null, null);
                this.currentHttpTasksCount++;
            }
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void runArtworkTasks(boolean z) {
        if (z) {
            this.currentArtworkTasksCount--;
        }
        while (this.currentArtworkTasksCount < 4 && !this.artworkTasks.isEmpty()) {
            try {
                this.artworkTasks.poll().executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, null, null, null);
                this.currentArtworkTasksCount++;
            } catch (Throwable unused) {
                runArtworkTasks(false);
            }
        }
    }

    public boolean isLoadingHttpFile(String str) {
        return this.httpFileLoadTasksByKeys.containsKey(str);
    }

    public static String getHttpFileName(String str) {
        return Utilities.MD5(str);
    }

    public static File getHttpFilePath(String str, String str2) {
        String httpUrlExtension = getHttpUrlExtension(str, str2);
        return new File(FileLoader.getDirectory(4), Utilities.MD5(str) + "." + httpUrlExtension);
    }

    public void loadHttpFile(String str, String str2, int i) {
        if (str == null || str.length() == 0 || this.httpFileLoadTasksByKeys.containsKey(str)) {
            return;
        }
        String httpUrlExtension = getHttpUrlExtension(str, str2);
        File file = new File(FileLoader.getDirectory(4), Utilities.MD5(str) + "_temp." + httpUrlExtension);
        file.delete();
        HttpFileTask httpFileTask = new HttpFileTask(str, file, httpUrlExtension, i);
        this.httpFileLoadTasks.add(httpFileTask);
        this.httpFileLoadTasksByKeys.put(str, httpFileTask);
        runHttpFileLoadTasks(null, 0);
    }

    public void cancelLoadHttpFile(String str) {
        HttpFileTask httpFileTask = this.httpFileLoadTasksByKeys.get(str);
        if (httpFileTask != null) {
            httpFileTask.cancel(true);
            this.httpFileLoadTasksByKeys.remove(str);
            this.httpFileLoadTasks.remove(httpFileTask);
        }
        Runnable runnable = this.retryHttpsTasks.get(str);
        if (runnable != null) {
            AndroidUtilities.cancelRunOnUIThread(runnable);
        }
        runHttpFileLoadTasks(null, 0);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void runHttpFileLoadTasks(final HttpFileTask httpFileTask, final int i) {
        AndroidUtilities.runOnUIThread(new Runnable() { // from class: org.telegram.messenger.ImageLoader$$ExternalSyntheticLambda1
            @Override // java.lang.Runnable
            public final void run() {
                this.f$0.lambda$runHttpFileLoadTasks$14(httpFileTask, i);
            }
        });
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$runHttpFileLoadTasks$14(HttpFileTask httpFileTask, int i) {
        ImageLoader imageLoader;
        if (httpFileTask != null) {
            this.currentHttpFileLoadTasksCount--;
        }
        if (httpFileTask == null) {
            imageLoader = this;
        } else if (i != 1) {
            imageLoader = this;
            if (i == 2) {
                imageLoader.httpFileLoadTasksByKeys.remove(httpFileTask.url);
                File file = new File(FileLoader.getDirectory(4), Utilities.MD5(httpFileTask.url) + "." + httpFileTask.ext);
                if (!httpFileTask.tempFile.renameTo(file)) {
                    file = httpFileTask.tempFile;
                }
                NotificationCenter.getInstance(httpFileTask.currentAccount).lambda$postNotificationNameOnUIThread$1(NotificationCenter.httpFileDidLoad, httpFileTask.url, file.toString());
            }
        } else if (httpFileTask.canRetry) {
            imageLoader = this;
            final HttpFileTask httpFileTask2 = imageLoader.new HttpFileTask(httpFileTask.url, httpFileTask.tempFile, httpFileTask.ext, httpFileTask.currentAccount);
            Runnable runnable = new Runnable() { // from class: org.telegram.messenger.ImageLoader$$ExternalSyntheticLambda5
                @Override // java.lang.Runnable
                public final void run() {
                    this.f$0.lambda$runHttpFileLoadTasks$13(httpFileTask2);
                }
            };
            imageLoader.retryHttpsTasks.put(httpFileTask.url, runnable);
            AndroidUtilities.runOnUIThread(runnable, 1000L);
        } else {
            imageLoader = this;
            imageLoader.httpFileLoadTasksByKeys.remove(httpFileTask.url);
            NotificationCenter.getInstance(httpFileTask.currentAccount).lambda$postNotificationNameOnUIThread$1(NotificationCenter.httpFileDidFailedLoad, httpFileTask.url, 0);
        }
        while (imageLoader.currentHttpFileLoadTasksCount < 2 && !imageLoader.httpFileLoadTasks.isEmpty()) {
            imageLoader.httpFileLoadTasks.poll().executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, null, null, null);
            imageLoader.currentHttpFileLoadTasksCount++;
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$runHttpFileLoadTasks$13(HttpFileTask httpFileTask) {
        this.httpFileLoadTasks.add(httpFileTask);
        runHttpFileLoadTasks(null, 0);
    }

    public static boolean shouldSendImageAsDocument(String str, Uri uri) {
        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = true;
        if (str == null && uri != null && uri.getScheme() != null) {
            if (uri.getScheme().contains("file")) {
                str = uri.getPath();
            } else {
                try {
                    str = AndroidUtilities.getPath(uri);
                } catch (Throwable th) {
                    FileLog.e(th);
                }
            }
        }
        if (str != null) {
            BitmapFactory.decodeFile(str, options);
        } else if (uri != null) {
            try {
                InputStream inputStreamOpenInputStream = ApplicationLoader.applicationContext.getContentResolver().openInputStream(uri);
                BitmapFactory.decodeStream(inputStreamOpenInputStream, null, options);
                inputStreamOpenInputStream.close();
            } catch (Throwable th2) {
                FileLog.e(th2);
                return false;
            }
        }
        float f = options.outWidth;
        float f2 = options.outHeight;
        return f / f2 > 10.0f || f2 / f > 10.0f;
    }

    /* JADX WARN: Code duplicated, block: B:111:0x019f A[Catch: all -> 0x01a4, TRY_LEAVE, TryCatch #1 {all -> 0x01a4, blocks: (B:109:0x018d, B:111:0x019f), top: B:149:0x018d }] */
    /* JADX WARN: Code duplicated, block: B:116:0x01aa A[DONT_INVERT] */
    /* JADX WARN: Code duplicated, block: B:121:0x01b6 A[Catch: all -> 0x01ba, TryCatch #12 {all -> 0x01ba, blocks: (B:119:0x01b2, B:121:0x01b6, B:124:0x01bd, B:126:0x01cf), top: B:171:0x01b2 }] */
    /* JADX WARN: Code duplicated, block: B:126:0x01cf A[Catch: all -> 0x01ba, TRY_LEAVE, TryCatch #12 {all -> 0x01ba, blocks: (B:119:0x01b2, B:121:0x01b6, B:124:0x01bd, B:126:0x01cf), top: B:171:0x01b2 }] */
    /* JADX WARN: Code duplicated, block: B:128:0x01d4  */
    /* JADX WARN: Code duplicated, block: B:149:0x018d A[EXC_TOP_SPLITTER, SYNTHETIC] */
    /* JADX WARN: Code duplicated, block: B:161:0x0141 A[EXC_TOP_SPLITTER, SYNTHETIC] */
    /* JADX WARN: Code duplicated, block: B:167:0x0147 A[EXC_TOP_SPLITTER, SYNTHETIC] */
    /* JADX WARN: Code duplicated, block: B:171:0x01b2 A[EXC_TOP_SPLITTER, SYNTHETIC] */
    /* JADX WARN: Code duplicated, block: B:175:0x01ac A[EXC_TOP_SPLITTER, SYNTHETIC] */
    /* JADX WARN: Code duplicated, block: B:177:0x009d A[EDGE_INSN: B:177:0x009d->B:43:0x009d BREAK  A[LOOP:0: B:40:0x0095->B:42:0x009b], SYNTHETIC] */
    /* JADX WARN: Code duplicated, block: B:181:? A[RETURN, SYNTHETIC] */
    /* JADX WARN: Code duplicated, block: B:32:0x007a  */
    /* JADX WARN: Code duplicated, block: B:33:0x007f  */
    /* JADX WARN: Code duplicated, block: B:36:0x0089  */
    /* JADX WARN: Code duplicated, block: B:39:0x0094  */
    /* JADX WARN: Code duplicated, block: B:42:0x009b A[LOOP:0: B:40:0x0095->B:42:0x009b, LOOP_END] */
    /* JADX WARN: Code duplicated, block: B:52:0x00c9 A[Catch: all -> 0x00e6, PHI: r8 r9
  0x00c9: PHI (r8v5 android.util.Pair<java.lang.Integer, java.lang.Integer>) = 
  (r8v2 android.util.Pair<java.lang.Integer, java.lang.Integer>)
  (r8v7 android.util.Pair<java.lang.Integer, java.lang.Integer>)
 binds: [B:55:0x00d0, B:51:0x00c7] A[DONT_GENERATE, DONT_INLINE]
  0x00c9: PHI (r9v22 java.io.InputStream) = (r9v21 java.io.InputStream), (r9v25 java.io.InputStream) binds: [B:55:0x00d0, B:51:0x00c7] A[DONT_GENERATE, DONT_INLINE], TRY_ENTER, TryCatch #2 {all -> 0x00e6, blocks: (B:45:0x00a1, B:47:0x00af, B:52:0x00c9, B:56:0x00d1, B:58:0x00db, B:62:0x00e8), top: B:151:0x00a1 }] */
    /* JADX WARN: Code duplicated, block: B:58:0x00db A[Catch: all -> 0x00e6, TryCatch #2 {all -> 0x00e6, blocks: (B:45:0x00a1, B:47:0x00af, B:52:0x00c9, B:56:0x00d1, B:58:0x00db, B:62:0x00e8), top: B:151:0x00a1 }] */
    /* JADX WARN: Code duplicated, block: B:65:0x00f7 A[Catch: all -> 0x012b, TryCatch #6 {all -> 0x012b, blocks: (B:63:0x00ed, B:65:0x00f7, B:69:0x0106, B:73:0x0112, B:74:0x0115, B:76:0x011f), top: B:159:0x00ed }] */
    /* JADX WARN: Code duplicated, block: B:67:0x0103  */
    /* JADX WARN: Code duplicated, block: B:68:0x0105  */
    /* JADX WARN: Code duplicated, block: B:71:0x0110  */
    /* JADX WARN: Code duplicated, block: B:72:0x0111  */
    /* JADX WARN: Code duplicated, block: B:76:0x011f A[Catch: all -> 0x012b, TRY_LEAVE, TryCatch #6 {all -> 0x012b, blocks: (B:63:0x00ed, B:65:0x00f7, B:69:0x0106, B:73:0x0112, B:74:0x0115, B:76:0x011f), top: B:159:0x00ed }] */
    /* JADX WARN: Code duplicated, block: B:79:0x0133 A[DONT_INVERT] */
    /* JADX WARN: Code duplicated, block: B:80:0x0135  */
    /* JADX WARN: Code duplicated, block: B:88:0x014b A[Catch: all -> 0x014f, TryCatch #10 {all -> 0x014f, blocks: (B:86:0x0147, B:88:0x014b, B:91:0x0152, B:93:0x0164), top: B:167:0x0147 }] */
    /* JADX WARN: Code duplicated, block: B:93:0x0164 A[Catch: all -> 0x014f, TRY_LEAVE, TryCatch #10 {all -> 0x014f, blocks: (B:86:0x0147, B:88:0x014b, B:91:0x0152, B:93:0x0164), top: B:167:0x0147 }] */
    public static Bitmap loadBitmap(String str, Uri uri, float f, float f2, boolean z) {
        String str2;
        InputStream inputStreamOpenInputStream;
        float f3;
        float f4;
        float fMin;
        int i;
        Matrix matrix;
        float f5;
        Matrix matrix2;
        Bitmap bitmapDecodeFile;
        Bitmap bitmapCreateBitmap;
        Bitmap bitmapCreateBitmap2;
        Bitmap bitmapDecodeStream;
        Bitmap bitmapCreateBitmap3;
        Pair<Integer, Integer> imageOrientation;
        float f6;
        float f7;
        InputStream inputStreamOpenInputStream2;
        int i2;
        int i3;
        String path;
        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = true;
        if (str != null || uri == null || uri.getScheme() == null) {
            str2 = str;
        } else {
            if (uri.getScheme().contains("file")) {
                path = uri.getPath();
            } else {
                if (Build.VERSION.SDK_INT < 30 || !"content".equals(uri.getScheme())) {
                    try {
                        path = AndroidUtilities.getPath(uri);
                    } catch (Throwable th) {
                        FileLog.e(th);
                        str2 = str;
                    }
                }
                str2 = str;
            }
            str2 = path;
        }
        Bitmap bitmapDecodeFile2 = null;
        try {
            try {
                if (str2 != null) {
                    BitmapFactory.decodeFile(str2, options);
                } else {
                    if (uri != null) {
                        try {
                            InputStream inputStreamOpenInputStream3 = ApplicationLoader.applicationContext.getContentResolver().openInputStream(uri);
                            BitmapFactory.decodeStream(inputStreamOpenInputStream3, null, options);
                            inputStreamOpenInputStream3.close();
                            inputStreamOpenInputStream = ApplicationLoader.applicationContext.getContentResolver().openInputStream(uri);
                        } catch (Throwable th2) {
                            FileLog.e(th2);
                            return null;
                        }
                    }
                    f3 = options.outWidth / f;
                    f4 = options.outHeight / f2;
                    if (z) {
                        fMin = Math.max(f3, f4);
                    } else {
                        fMin = Math.min(f3, f4);
                    }
                    if (fMin < 1.0f) {
                        fMin = 1.0f;
                    }
                    options.inJustDecodeBounds = false;
                    i = (int) fMin;
                    options.inSampleSize = i;
                    if (i % 2 != 0) {
                        i2 = 1;
                        while (true) {
                            i3 = i2 * 2;
                            if (i3 < options.inSampleSize) {
                                break;
                            }
                            i2 = i3;
                        }
                        options.inSampleSize = i2;
                    }
                    options.inPurgeable = false;
                    imageOrientation = AndroidUtilities.getImageOrientation(str2);
                    if (((Integer) imageOrientation.first).intValue() == 0 && ((Integer) imageOrientation.second).intValue() == 0) {
                        try {
                            inputStreamOpenInputStream2 = ApplicationLoader.applicationContext.getContentResolver().openInputStream(uri);
                            try {
                                imageOrientation = AndroidUtilities.getImageOrientation(inputStreamOpenInputStream2);
                                if (inputStreamOpenInputStream2 != null) {
                                    inputStreamOpenInputStream2.close();
                                }
                            } catch (Throwable unused) {
                                if (inputStreamOpenInputStream2 == null) {
                                    if (((Integer) imageOrientation.first).intValue() == 0) {
                                    }
                                    matrix = new Matrix();
                                    if (((Integer) imageOrientation.second).intValue() != 0) {
                                        f6 = -1.0f;
                                        if (((Integer) imageOrientation.second).intValue() == 1) {
                                            f7 = -1.0f;
                                        } else {
                                            f7 = 1.0f;
                                        }
                                        if (((Integer) imageOrientation.second).intValue() != 2) {
                                            f6 = 1.0f;
                                        }
                                        matrix.postScale(f7, f6);
                                    }
                                    if (((Integer) imageOrientation.first).intValue() != 0) {
                                        matrix.postRotate(((Integer) imageOrientation.first).intValue());
                                    }
                                }
                                f5 = fMin / options.inSampleSize;
                                if (f5 > 1.0f) {
                                    if (matrix == null) {
                                        matrix = new Matrix();
                                    }
                                    float f8 = 1.0f / f5;
                                    matrix.postScale(f8, f8);
                                }
                                matrix2 = matrix;
                                if (str2 == null) {
                                    if (uri == null) {
                                        return null;
                                    }
                                    try {
                                        bitmapDecodeStream = BitmapFactory.decodeStream(inputStreamOpenInputStream, null, options);
                                        if (bitmapDecodeStream != null) {
                                            try {
                                                if (options.inPurgeable) {
                                                    Utilities.pinBitmap(bitmapDecodeStream);
                                                }
                                                bitmapCreateBitmap3 = Bitmaps.createBitmap(bitmapDecodeStream, 0, 0, bitmapDecodeStream.getWidth(), bitmapDecodeStream.getHeight(), matrix2, true);
                                                if (bitmapCreateBitmap3 != bitmapDecodeStream) {
                                                    bitmapDecodeStream.recycle();
                                                    bitmapDecodeFile2 = bitmapCreateBitmap3;
                                                } else {
                                                    bitmapDecodeFile2 = bitmapDecodeStream;
                                                }
                                            } catch (Throwable th3) {
                                                th = th3;
                                                bitmapDecodeFile2 = bitmapDecodeStream;
                                                try {
                                                    FileLog.e(th);
                                                } finally {
                                                    try {
                                                        inputStreamOpenInputStream.close();
                                                    } catch (Throwable th4) {
                                                        FileLog.e(th4);
                                                    }
                                                }
                                            }
                                        } else {
                                            bitmapDecodeFile2 = bitmapDecodeStream;
                                        }
                                    } catch (Throwable th5) {
                                        th = th5;
                                    }
                                    try {
                                        return bitmapDecodeFile2;
                                    } catch (Throwable th6) {
                                        return bitmapDecodeFile2;
                                    }
                                }
                                try {
                                    bitmapDecodeFile = BitmapFactory.decodeFile(str2, options);
                                    if (bitmapDecodeFile != null) {
                                        try {
                                            if (options.inPurgeable) {
                                                Utilities.pinBitmap(bitmapDecodeFile);
                                            }
                                            bitmapCreateBitmap2 = Bitmaps.createBitmap(bitmapDecodeFile, 0, 0, bitmapDecodeFile.getWidth(), bitmapDecodeFile.getHeight(), matrix2, true);
                                            if (bitmapCreateBitmap2 != bitmapDecodeFile) {
                                                bitmapDecodeFile.recycle();
                                                return bitmapCreateBitmap2;
                                            }
                                        } catch (Throwable th7) {
                                            th = th7;
                                            bitmapDecodeFile2 = bitmapDecodeFile;
                                            FileLog.e(th);
                                            getInstance().clearMemory();
                                            if (bitmapDecodeFile2 == null) {
                                                try {
                                                    bitmapDecodeFile2 = BitmapFactory.decodeFile(str2, options);
                                                    if (bitmapDecodeFile2 != null && options.inPurgeable) {
                                                        Utilities.pinBitmap(bitmapDecodeFile2);
                                                    }
                                                    bitmapDecodeFile = bitmapDecodeFile2;
                                                    if (bitmapDecodeFile != null) {
                                                        try {
                                                            bitmapCreateBitmap = Bitmaps.createBitmap(bitmapDecodeFile, 0, 0, bitmapDecodeFile.getWidth(), bitmapDecodeFile.getHeight(), matrix2, true);
                                                            if (bitmapCreateBitmap != bitmapDecodeFile) {
                                                                bitmapDecodeFile.recycle();
                                                                bitmapDecodeFile = bitmapCreateBitmap;
                                                            }
                                                        } catch (Throwable th8) {
                                                            th = th8;
                                                            bitmapDecodeFile2 = bitmapDecodeFile;
                                                            FileLog.e(th);
                                                            return bitmapDecodeFile2;
                                                        }
                                                    }
                                                } catch (Throwable th9) {
                                                    th = th9;
                                                    FileLog.e(th);
                                                    return bitmapDecodeFile2;
                                                }
                                            } else {
                                                bitmapDecodeFile = bitmapDecodeFile2;
                                                if (bitmapDecodeFile != null) {
                                                    bitmapCreateBitmap = Bitmaps.createBitmap(bitmapDecodeFile, 0, 0, bitmapDecodeFile.getWidth(), bitmapDecodeFile.getHeight(), matrix2, true);
                                                    if (bitmapCreateBitmap != bitmapDecodeFile) {
                                                        bitmapDecodeFile.recycle();
                                                        bitmapDecodeFile = bitmapCreateBitmap;
                                                    }
                                                }
                                            }
                                        }
                                    }
                                } catch (Throwable th10) {
                                    th = th10;
                                }
                                return bitmapDecodeFile;
                            }
                        } catch (Throwable unused2) {
                            inputStreamOpenInputStream2 = null;
                        }
                    }
                    if (((Integer) imageOrientation.first).intValue() == 0 || ((Integer) imageOrientation.second).intValue() != 0) {
                        matrix = new Matrix();
                        if (((Integer) imageOrientation.second).intValue() != 0) {
                            f6 = -1.0f;
                            if (((Integer) imageOrientation.second).intValue() == 1) {
                                f7 = -1.0f;
                            } else {
                                f7 = 1.0f;
                            }
                            if (((Integer) imageOrientation.second).intValue() != 2) {
                                f6 = 1.0f;
                            }
                            matrix.postScale(f7, f6);
                        }
                        if (((Integer) imageOrientation.first).intValue() != 0) {
                            matrix.postRotate(((Integer) imageOrientation.first).intValue());
                        }
                    } else {
                        matrix = null;
                    }
                    f5 = fMin / options.inSampleSize;
                    if (f5 > 1.0f) {
                        if (matrix == null) {
                            matrix = new Matrix();
                        }
                        float f9 = 1.0f / f5;
                        matrix.postScale(f9, f9);
                    }
                    matrix2 = matrix;
                    if (str2 == null) {
                        bitmapDecodeFile = BitmapFactory.decodeFile(str2, options);
                        if (bitmapDecodeFile != null) {
                            if (options.inPurgeable) {
                                Utilities.pinBitmap(bitmapDecodeFile);
                            }
                            bitmapCreateBitmap2 = Bitmaps.createBitmap(bitmapDecodeFile, 0, 0, bitmapDecodeFile.getWidth(), bitmapDecodeFile.getHeight(), matrix2, true);
                            if (bitmapCreateBitmap2 != bitmapDecodeFile) {
                                bitmapDecodeFile.recycle();
                                return bitmapCreateBitmap2;
                            }
                        }
                        return bitmapDecodeFile;
                    }
                    if (uri == null) {
                        return null;
                    }
                    bitmapDecodeStream = BitmapFactory.decodeStream(inputStreamOpenInputStream, null, options);
                    if (bitmapDecodeStream != null) {
                        if (options.inPurgeable) {
                            Utilities.pinBitmap(bitmapDecodeStream);
                        }
                        bitmapCreateBitmap3 = Bitmaps.createBitmap(bitmapDecodeStream, 0, 0, bitmapDecodeStream.getWidth(), bitmapDecodeStream.getHeight(), matrix2, true);
                        if (bitmapCreateBitmap3 != bitmapDecodeStream) {
                            bitmapDecodeStream.recycle();
                            bitmapDecodeFile2 = bitmapCreateBitmap3;
                        } else {
                            bitmapDecodeFile2 = bitmapDecodeStream;
                        }
                    } else {
                        bitmapDecodeFile2 = bitmapDecodeStream;
                    }
                    return bitmapDecodeFile2;
                }
                if (((Integer) imageOrientation.second).intValue() != 0) {
                    f6 = -1.0f;
                    if (((Integer) imageOrientation.second).intValue() == 1) {
                        f7 = -1.0f;
                    } else {
                        f7 = 1.0f;
                    }
                    if (((Integer) imageOrientation.second).intValue() != 2) {
                        f6 = 1.0f;
                    }
                    matrix.postScale(f7, f6);
                }
                if (((Integer) imageOrientation.first).intValue() != 0) {
                    matrix.postRotate(((Integer) imageOrientation.first).intValue());
                }
            } catch (Throwable unused3) {
            }
            imageOrientation = AndroidUtilities.getImageOrientation(str2);
            if (((Integer) imageOrientation.first).intValue() == 0) {
                inputStreamOpenInputStream2 = ApplicationLoader.applicationContext.getContentResolver().openInputStream(uri);
                imageOrientation = AndroidUtilities.getImageOrientation(inputStreamOpenInputStream2);
                if (inputStreamOpenInputStream2 != null) {
                    inputStreamOpenInputStream2.close();
                }
            }
            if (((Integer) imageOrientation.first).intValue() == 0) {
            }
            matrix = new Matrix();
        } catch (Throwable unused4) {
        }
        inputStreamOpenInputStream = null;
        f3 = options.outWidth / f;
        f4 = options.outHeight / f2;
        if (z) {
            fMin = Math.max(f3, f4);
        } else {
            fMin = Math.min(f3, f4);
        }
        if (fMin < 1.0f) {
            fMin = 1.0f;
        }
        options.inJustDecodeBounds = false;
        i = (int) fMin;
        options.inSampleSize = i;
        if (i % 2 != 0) {
            i2 = 1;
            while (true) {
                i3 = i2 * 2;
                if (i3 < options.inSampleSize) {
                    break;
                    break;
                }
                i2 = i3;
            }
            options.inSampleSize = i2;
        }
        options.inPurgeable = false;
        f5 = fMin / options.inSampleSize;
        if (f5 > 1.0f) {
            if (matrix == null) {
                matrix = new Matrix();
            }
            float f10 = 1.0f / f5;
            matrix.postScale(f10, f10);
        }
        matrix2 = matrix;
        if (str2 == null) {
            bitmapDecodeFile = BitmapFactory.decodeFile(str2, options);
            if (bitmapDecodeFile != null) {
                if (options.inPurgeable) {
                    Utilities.pinBitmap(bitmapDecodeFile);
                }
                bitmapCreateBitmap2 = Bitmaps.createBitmap(bitmapDecodeFile, 0, 0, bitmapDecodeFile.getWidth(), bitmapDecodeFile.getHeight(), matrix2, true);
                if (bitmapCreateBitmap2 != bitmapDecodeFile) {
                    bitmapDecodeFile.recycle();
                    return bitmapCreateBitmap2;
                }
            }
            return bitmapDecodeFile;
        }
        if (uri == null) {
            return null;
        }
        bitmapDecodeStream = BitmapFactory.decodeStream(inputStreamOpenInputStream, null, options);
        if (bitmapDecodeStream != null) {
            if (options.inPurgeable) {
                Utilities.pinBitmap(bitmapDecodeStream);
            }
            bitmapCreateBitmap3 = Bitmaps.createBitmap(bitmapDecodeStream, 0, 0, bitmapDecodeStream.getWidth(), bitmapDecodeStream.getHeight(), matrix2, true);
            if (bitmapCreateBitmap3 != bitmapDecodeStream) {
                bitmapDecodeStream.recycle();
                bitmapDecodeFile2 = bitmapCreateBitmap3;
            } else {
                bitmapDecodeFile2 = bitmapDecodeStream;
            }
        } else {
            bitmapDecodeFile2 = bitmapDecodeStream;
        }
        return bitmapDecodeFile2;
    }

    public static void fillPhotoSizeWithBytes(TLRPC.PhotoSize photoSize) {
        if (photoSize != null) {
            byte[] bArr = photoSize.bytes;
            if (bArr == null || bArr.length == 0) {
                try {
                    RandomAccessFile randomAccessFile = new RandomAccessFile(FileLoader.getInstance(UserConfig.selectedAccount).getPathToAttach(photoSize, true), "r");
                    if (((int) randomAccessFile.length()) < 20000) {
                        byte[] bArr2 = new byte[(int) randomAccessFile.length()];
                        photoSize.bytes = bArr2;
                        randomAccessFile.readFully(bArr2, 0, bArr2.length);
                    }
                } catch (Throwable th) {
                    FileLog.e(th);
                }
            }
        }
    }

    public static TLRPC.PhotoSize fileToSize(String str, boolean z) {
        if (str == null) {
            return null;
        }
        try {
            BitmapFactory.Options options = new BitmapFactory.Options();
            options.inJustDecodeBounds = true;
            BitmapFactory.decodeFile(str, options);
            int i = options.outWidth;
            int i2 = options.outHeight;
            TLRPC.TL_fileLocationToBeDeprecated tL_fileLocationToBeDeprecated = new TLRPC.TL_fileLocationToBeDeprecated();
            tL_fileLocationToBeDeprecated.volume_id = -2147483648L;
            tL_fileLocationToBeDeprecated.dc_id = Integer.MIN_VALUE;
            tL_fileLocationToBeDeprecated.local_id = SharedConfig.getLastLocalId();
            tL_fileLocationToBeDeprecated.file_reference = new byte[0];
            TLRPC.TL_photoSize_layer127 tL_photoSize_layer127 = new TLRPC.TL_photoSize_layer127();
            tL_photoSize_layer127.location = tL_fileLocationToBeDeprecated;
            tL_photoSize_layer127.w = i;
            tL_photoSize_layer127.h = i2;
            if (i <= 100 && i2 <= 100) {
                tL_photoSize_layer127.type = "s";
            } else if (i <= 320 && i2 <= 320) {
                tL_photoSize_layer127.type = "m";
            } else if (i <= 800 && i2 <= 800) {
                tL_photoSize_layer127.type = "x";
            } else if (i <= 1280 && i2 <= 1280) {
                tL_photoSize_layer127.type = "y";
            } else {
                tL_photoSize_layer127.type = "w";
            }
            String str2 = tL_fileLocationToBeDeprecated.volume_id + "_" + tL_fileLocationToBeDeprecated.local_id + ".jpg";
            File directory = (z || tL_fileLocationToBeDeprecated.volume_id == -2147483648L) ? FileLoader.getDirectory(4) : FileLoader.getDirectory(0);
            File file = new File(directory, str2);
            new File(str).renameTo(file);
            tL_photoSize_layer127.size = (int) file.length();
            return tL_photoSize_layer127;
        } catch (Exception e) {
            FileLog.e(e);
            return null;
        }
    }

    public static class PhotoSizeFromPhoto extends TLRPC.PhotoSize {
        public final TLRPC.InputPhoto inputPhoto;
        public final TLRPC.Photo photo;

        public PhotoSizeFromPhoto(TLRPC.Photo photo) {
            this.photo = photo;
            TLRPC.TL_inputPhoto tL_inputPhoto = new TLRPC.TL_inputPhoto();
            tL_inputPhoto.id = photo.id;
            tL_inputPhoto.file_reference = photo.file_reference;
            tL_inputPhoto.access_hash = photo.access_hash;
            this.inputPhoto = tL_inputPhoto;
        }
    }

    /* JADX WARN: Code duplicated, block: B:14:0x0020  */
    /* JADX WARN: Code duplicated, block: B:18:0x0056  */
    /* JADX WARN: Code duplicated, block: B:20:0x005a A[ADDED_TO_REGION] */
    /* JADX WARN: Code duplicated, block: B:22:0x0062  */
    /* JADX WARN: Code duplicated, block: B:24:0x0066 A[ADDED_TO_REGION] */
    /* JADX WARN: Code duplicated, block: B:26:0x006e  */
    /* JADX WARN: Code duplicated, block: B:28:0x0072 A[ADDED_TO_REGION] */
    /* JADX WARN: Code duplicated, block: B:30:0x007a  */
    private static TLRPC.PhotoSize scaleAndSaveImageInternal(TLRPC.PhotoSize photoSize, Bitmap bitmap, Bitmap.CompressFormat compressFormat, boolean z, int i, int i2, float f, float f2, float f3, int i3, boolean z2, boolean z3, boolean z4) throws IOException {
        TLRPC.TL_fileLocationToBeDeprecated tL_fileLocationToBeDeprecated;
        int i4;
        Bitmap bitmapCreateScaledBitmap = (f3 > 1.0f || z3) ? Bitmaps.createScaledBitmap(bitmap, i, i2, true) : bitmap;
        if (photoSize != null) {
            TLRPC.FileLocation fileLocation = photoSize.location;
            if (!(fileLocation instanceof TLRPC.TL_fileLocationToBeDeprecated)) {
                tL_fileLocationToBeDeprecated = new TLRPC.TL_fileLocationToBeDeprecated();
                tL_fileLocationToBeDeprecated.volume_id = -2147483648L;
                tL_fileLocationToBeDeprecated.dc_id = Integer.MIN_VALUE;
                tL_fileLocationToBeDeprecated.local_id = SharedConfig.getLastLocalId();
                tL_fileLocationToBeDeprecated.file_reference = new byte[0];
                photoSize = new TLRPC.TL_photoSize_layer127();
                photoSize.location = tL_fileLocationToBeDeprecated;
                photoSize.w = bitmapCreateScaledBitmap.getWidth();
                int height = bitmapCreateScaledBitmap.getHeight();
                photoSize.h = height;
                i4 = photoSize.w;
                if (i4 > 100 && height <= 100) {
                    photoSize.type = "s";
                } else if (i4 > 320 && height <= 320) {
                    photoSize.type = "m";
                } else if (i4 > 800 && height <= 800) {
                    photoSize.type = "x";
                } else if (i4 > 1280 && height <= 1280) {
                    photoSize.type = "y";
                } else {
                    photoSize.type = "w";
                }
            } else {
                tL_fileLocationToBeDeprecated = (TLRPC.TL_fileLocationToBeDeprecated) fileLocation;
            }
        } else {
            tL_fileLocationToBeDeprecated = new TLRPC.TL_fileLocationToBeDeprecated();
            tL_fileLocationToBeDeprecated.volume_id = -2147483648L;
            tL_fileLocationToBeDeprecated.dc_id = Integer.MIN_VALUE;
            tL_fileLocationToBeDeprecated.local_id = SharedConfig.getLastLocalId();
            tL_fileLocationToBeDeprecated.file_reference = new byte[0];
            photoSize = new TLRPC.TL_photoSize_layer127();
            photoSize.location = tL_fileLocationToBeDeprecated;
            photoSize.w = bitmapCreateScaledBitmap.getWidth();
            int height2 = bitmapCreateScaledBitmap.getHeight();
            photoSize.h = height2;
            i4 = photoSize.w;
            if (i4 > 100) {
                if (i4 > 320) {
                    if (i4 > 800) {
                        if (i4 > 1280) {
                            photoSize.type = "w";
                        } else {
                            photoSize.type = "w";
                        }
                    } else if (i4 > 1280) {
                        photoSize.type = "w";
                    } else {
                        photoSize.type = "w";
                    }
                } else if (i4 > 800) {
                    if (i4 > 1280) {
                        photoSize.type = "w";
                    } else {
                        photoSize.type = "w";
                    }
                } else if (i4 > 1280) {
                    photoSize.type = "w";
                } else {
                    photoSize.type = "w";
                }
            } else if (i4 > 320) {
                if (i4 > 800) {
                    if (i4 > 1280) {
                        photoSize.type = "w";
                    } else {
                        photoSize.type = "w";
                    }
                } else if (i4 > 1280) {
                    photoSize.type = "w";
                } else {
                    photoSize.type = "w";
                }
            } else if (i4 > 800) {
                if (i4 > 1280) {
                    photoSize.type = "w";
                } else {
                    photoSize.type = "w";
                }
            } else if (i4 > 1280) {
                photoSize.type = "w";
            } else {
                photoSize.type = "w";
            }
        }
        int i5 = AnonymousClass7.$SwitchMap$android$graphics$Bitmap$CompressFormat[compressFormat.ordinal()];
        String str = tL_fileLocationToBeDeprecated.volume_id + "_" + tL_fileLocationToBeDeprecated.local_id + ((i5 == 1 || i5 == 2 || i5 == 3) ? ".webp" : ".jpg");
        File directory = (z4 || tL_fileLocationToBeDeprecated.volume_id == -2147483648L) ? FileLoader.getDirectory(4) : FileLoader.getDirectory(0);
        FileOutputStream fileOutputStream = new FileOutputStream(new File(directory, str));
        bitmapCreateScaledBitmap.compress(compressFormat, i3, fileOutputStream);
        if (!z2) {
            photoSize.size = (int) fileOutputStream.getChannel().size();
        }
        fileOutputStream.close();
        if (z2) {
            ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
            bitmapCreateScaledBitmap.compress(compressFormat, i3, byteArrayOutputStream);
            byte[] byteArray = byteArrayOutputStream.toByteArray();
            photoSize.bytes = byteArray;
            photoSize.size = byteArray.length;
            byteArrayOutputStream.close();
        }
        if (bitmapCreateScaledBitmap != bitmap) {
            bitmapCreateScaledBitmap.recycle();
        }
        return photoSize;
    }

    /* JADX INFO: renamed from: org.telegram.messenger.ImageLoader$7, reason: invalid class name */
    static /* synthetic */ class AnonymousClass7 {
        static final /* synthetic */ int[] $SwitchMap$android$graphics$Bitmap$CompressFormat;

        static {
            int[] iArr = new int[Bitmap.CompressFormat.values().length];
            $SwitchMap$android$graphics$Bitmap$CompressFormat = iArr;
            try {
                iArr[Bitmap.CompressFormat.WEBP.ordinal()] = 1;
            } catch (NoSuchFieldError unused) {
            }
            try {
                $SwitchMap$android$graphics$Bitmap$CompressFormat[Bitmap.CompressFormat.WEBP_LOSSY.ordinal()] = 2;
            } catch (NoSuchFieldError unused2) {
            }
            try {
                $SwitchMap$android$graphics$Bitmap$CompressFormat[Bitmap.CompressFormat.WEBP_LOSSLESS.ordinal()] = 3;
            } catch (NoSuchFieldError unused3) {
            }
        }
    }

    public static TLRPC.PhotoSize scaleAndSaveImage(Bitmap bitmap, float f, float f2, int i, boolean z) {
        return scaleAndSaveImage(null, bitmap, Bitmap.CompressFormat.JPEG, false, f, f2, i, z, 0, 0, false);
    }

    public static TLRPC.PhotoSize scaleAndSaveImage(TLRPC.PhotoSize photoSize, Bitmap bitmap, float f, float f2, int i, boolean z, boolean z2) {
        return scaleAndSaveImage(photoSize, bitmap, Bitmap.CompressFormat.JPEG, false, f, f2, i, z, 0, 0, z2);
    }

    public static TLRPC.PhotoSize scaleAndSaveImage(Bitmap bitmap, float f, float f2, int i, boolean z, int i2, int i3) {
        return scaleAndSaveImage(null, bitmap, Bitmap.CompressFormat.JPEG, false, f, f2, i, z, i2, i3, false);
    }

    public static TLRPC.PhotoSize scaleAndSaveImage(Bitmap bitmap, float f, float f2, boolean z, int i, boolean z2, int i2, int i3) {
        return scaleAndSaveImage(null, bitmap, Bitmap.CompressFormat.JPEG, z, f, f2, i, z2, i2, i3, false);
    }

    public static TLRPC.PhotoSize scaleAndSaveImage(Bitmap bitmap, Bitmap.CompressFormat compressFormat, float f, float f2, int i, boolean z, int i2, int i3) {
        return scaleAndSaveImage(null, bitmap, compressFormat, false, f, f2, i, z, i2, i3, false);
    }

    /* JADX WARN: Code duplicated, block: B:27:0x004a  */
    /* JADX WARN: Code duplicated, block: B:30:0x0058  */
    public static TLRPC.PhotoSize scaleAndSaveImage(TLRPC.PhotoSize photoSize, Bitmap bitmap, Bitmap.CompressFormat compressFormat, boolean z, float f, float f2, int i, boolean z2, int i2, int i3, boolean z3) {
        boolean z4;
        float fMax;
        if (bitmap == null) {
            return null;
        }
        float width = bitmap.getWidth();
        float height = bitmap.getHeight();
        if (width != 0.0f && height != 0.0f) {
            float fMax2 = Math.max(width / f, height / f2);
            if (i2 == 0 || i3 == 0) {
                z4 = false;
            } else {
                float f3 = i2;
                if (width < f3 || height < i3) {
                    if (width < f3 && height > i3) {
                        fMax = width / f3;
                    } else if (width > f3) {
                        float f4 = i3;
                        if (height < f4) {
                            fMax = height / f4;
                        } else {
                            fMax = Math.max(width / f3, height / i3);
                        }
                    } else {
                        fMax = Math.max(width / f3, height / i3);
                    }
                    fMax2 = fMax;
                    z4 = true;
                } else {
                    z4 = false;
                }
            }
            boolean z5 = z4;
            float f5 = fMax2;
            int i4 = (int) (width / f5);
            int i5 = (int) (height / f5);
            if (i5 != 0 && i4 != 0) {
                try {
                    return scaleAndSaveImageInternal(photoSize, bitmap, compressFormat, z, i4, i5, width, height, f5, i, z2, z5, z3);
                } catch (Throwable th) {
                    FileLog.e(th);
                    getInstance().clearMemory();
                }
            }
        }
        return null;
    }

    public static String getHttpUrlExtension(String str, String str2) {
        String lastPathSegment = Uri.parse(str).getLastPathSegment();
        if (!TextUtils.isEmpty(lastPathSegment) && lastPathSegment.length() > 1) {
            str = lastPathSegment;
        }
        int iLastIndexOf = str.lastIndexOf(46);
        String strSubstring = iLastIndexOf != -1 ? str.substring(iLastIndexOf + 1) : null;
        return (strSubstring == null || strSubstring.length() == 0 || strSubstring.length() > 4) ? str2 : strSubstring;
    }

    public static void saveMessageThumbs(TLRPC.Message message) {
        byte[] bArr;
        TLRPC.PhotoSize tL_photoSize_layer127;
        TLRPC.MessageMedia messageMedia = message.media;
        if (messageMedia == null) {
            return;
        }
        int i = 0;
        if (messageMedia instanceof TLRPC.TL_messageMediaPaidMedia) {
            TLRPC.TL_messageMediaPaidMedia tL_messageMediaPaidMedia = (TLRPC.TL_messageMediaPaidMedia) messageMedia;
            while (i < tL_messageMediaPaidMedia.extended_media.size()) {
                TLRPC.MessageExtendedMedia messageExtendedMedia = tL_messageMediaPaidMedia.extended_media.get(i);
                if (messageExtendedMedia instanceof TLRPC.TL_messageExtendedMedia) {
                    saveMessageThumbs(message, ((TLRPC.TL_messageExtendedMedia) messageExtendedMedia).media);
                }
                i++;
            }
            return;
        }
        TLRPC.PhotoSize photoSizeFindPhotoCachedSize = findPhotoCachedSize(message);
        if (photoSizeFindPhotoCachedSize == null || (bArr = photoSizeFindPhotoCachedSize.bytes) == null || bArr.length == 0) {
            return;
        }
        TLRPC.FileLocation fileLocation = photoSizeFindPhotoCachedSize.location;
        if (fileLocation == null || (fileLocation instanceof TLRPC.TL_fileLocationUnavailable)) {
            TLRPC.TL_fileLocationToBeDeprecated tL_fileLocationToBeDeprecated = new TLRPC.TL_fileLocationToBeDeprecated();
            photoSizeFindPhotoCachedSize.location = tL_fileLocationToBeDeprecated;
            tL_fileLocationToBeDeprecated.volume_id = -2147483648L;
            tL_fileLocationToBeDeprecated.local_id = SharedConfig.getLastLocalId();
        }
        if (photoSizeFindPhotoCachedSize.h <= 50 && photoSizeFindPhotoCachedSize.w <= 50) {
            tL_photoSize_layer127 = new TLRPC.TL_photoStrippedSize();
            tL_photoSize_layer127.location = photoSizeFindPhotoCachedSize.location;
            tL_photoSize_layer127.bytes = photoSizeFindPhotoCachedSize.bytes;
            tL_photoSize_layer127.h = photoSizeFindPhotoCachedSize.h;
            tL_photoSize_layer127.w = photoSizeFindPhotoCachedSize.w;
        } else {
            boolean z = true;
            File pathToAttach = FileLoader.getInstance(UserConfig.selectedAccount).getPathToAttach(photoSizeFindPhotoCachedSize, true);
            if (MessageObject.shouldEncryptPhotoOrVideo(UserConfig.selectedAccount, message)) {
                pathToAttach = new File(pathToAttach.getAbsolutePath() + ".enc");
            } else {
                z = false;
            }
            if (!pathToAttach.exists()) {
                if (z) {
                    try {
                        RandomAccessFile randomAccessFile = new RandomAccessFile(new File(FileLoader.getInternalCacheDir(), pathToAttach.getName() + ".key"), "rws");
                        long length = randomAccessFile.length();
                        byte[] bArr2 = new byte[32];
                        byte[] bArr3 = new byte[16];
                        if (length > 0 && length % 48 == 0) {
                            randomAccessFile.read(bArr2, 0, 32);
                            randomAccessFile.read(bArr3, 0, 16);
                        } else {
                            Utilities.random.nextBytes(bArr2);
                            Utilities.random.nextBytes(bArr3);
                            randomAccessFile.write(bArr2);
                            randomAccessFile.write(bArr3);
                        }
                        randomAccessFile.close();
                        byte[] bArr4 = photoSizeFindPhotoCachedSize.bytes;
                        Utilities.aesCtrDecryptionByteArray(bArr4, bArr2, bArr3, 0, bArr4.length, 0);
                    } catch (Exception e) {
                        FileLog.e(e);
                    }
                }
                RandomAccessFile randomAccessFile2 = new RandomAccessFile(pathToAttach, "rws");
                randomAccessFile2.write(photoSizeFindPhotoCachedSize.bytes);
                randomAccessFile2.close();
            }
            tL_photoSize_layer127 = new TLRPC.TL_photoSize_layer127();
            tL_photoSize_layer127.w = photoSizeFindPhotoCachedSize.w;
            tL_photoSize_layer127.h = photoSizeFindPhotoCachedSize.h;
            tL_photoSize_layer127.location = photoSizeFindPhotoCachedSize.location;
            tL_photoSize_layer127.size = photoSizeFindPhotoCachedSize.size;
            tL_photoSize_layer127.type = photoSizeFindPhotoCachedSize.type;
        }
        TLRPC.MessageMedia messageMedia2 = message.media;
        if (messageMedia2 instanceof TLRPC.TL_messageMediaPhoto) {
            int size = messageMedia2.photo.sizes.size();
            while (i < size) {
                if (((TLRPC.PhotoSize) message.media.photo.sizes.get(i)) instanceof TLRPC.TL_photoCachedSize) {
                    message.media.photo.sizes.set(i, tL_photoSize_layer127);
                    return;
                }
                i++;
            }
            return;
        }
        if (messageMedia2 instanceof TLRPC.TL_messageMediaDocument) {
            int size2 = messageMedia2.document.thumbs.size();
            while (i < size2) {
                if (message.media.document.thumbs.get(i) instanceof TLRPC.TL_photoCachedSize) {
                    message.media.document.thumbs.set(i, tL_photoSize_layer127);
                    return;
                }
                i++;
            }
            return;
        }
        if (messageMedia2 instanceof TLRPC.TL_messageMediaWebPage) {
            int size3 = messageMedia2.webpage.photo.sizes.size();
            while (i < size3) {
                if (((TLRPC.PhotoSize) message.media.webpage.photo.sizes.get(i)) instanceof TLRPC.TL_photoCachedSize) {
                    message.media.webpage.photo.sizes.set(i, tL_photoSize_layer127);
                    return;
                }
                i++;
            }
        }
    }

    public static void saveMessageThumbs(TLRPC.Message message, TLRPC.MessageMedia messageMedia) {
        TLRPC.PhotoSize photoSizeFindPhotoCachedSize;
        byte[] bArr;
        TLRPC.PhotoSize tL_photoSize_layer127;
        if (message == null || messageMedia == null || (photoSizeFindPhotoCachedSize = findPhotoCachedSize(messageMedia)) == null || (bArr = photoSizeFindPhotoCachedSize.bytes) == null || bArr.length == 0) {
            return;
        }
        TLRPC.FileLocation fileLocation = photoSizeFindPhotoCachedSize.location;
        if (fileLocation == null || (fileLocation instanceof TLRPC.TL_fileLocationUnavailable)) {
            TLRPC.TL_fileLocationToBeDeprecated tL_fileLocationToBeDeprecated = new TLRPC.TL_fileLocationToBeDeprecated();
            photoSizeFindPhotoCachedSize.location = tL_fileLocationToBeDeprecated;
            tL_fileLocationToBeDeprecated.volume_id = -2147483648L;
            tL_fileLocationToBeDeprecated.local_id = SharedConfig.getLastLocalId();
        }
        int i = 0;
        if (photoSizeFindPhotoCachedSize.h <= 50 && photoSizeFindPhotoCachedSize.w <= 50) {
            tL_photoSize_layer127 = new TLRPC.TL_photoStrippedSize();
            tL_photoSize_layer127.location = photoSizeFindPhotoCachedSize.location;
            tL_photoSize_layer127.bytes = photoSizeFindPhotoCachedSize.bytes;
            tL_photoSize_layer127.h = photoSizeFindPhotoCachedSize.h;
            tL_photoSize_layer127.w = photoSizeFindPhotoCachedSize.w;
        } else {
            boolean z = true;
            File pathToAttach = FileLoader.getInstance(UserConfig.selectedAccount).getPathToAttach(photoSizeFindPhotoCachedSize, true);
            if (MessageObject.shouldEncryptPhotoOrVideo(UserConfig.selectedAccount, message)) {
                pathToAttach = new File(pathToAttach.getAbsolutePath() + ".enc");
            } else {
                z = false;
            }
            if (!pathToAttach.exists()) {
                if (z) {
                    try {
                        RandomAccessFile randomAccessFile = new RandomAccessFile(new File(FileLoader.getInternalCacheDir(), pathToAttach.getName() + ".key"), "rws");
                        long length = randomAccessFile.length();
                        byte[] bArr2 = new byte[32];
                        byte[] bArr3 = new byte[16];
                        if (length > 0 && length % 48 == 0) {
                            randomAccessFile.read(bArr2, 0, 32);
                            randomAccessFile.read(bArr3, 0, 16);
                        } else {
                            Utilities.random.nextBytes(bArr2);
                            Utilities.random.nextBytes(bArr3);
                            randomAccessFile.write(bArr2);
                            randomAccessFile.write(bArr3);
                        }
                        randomAccessFile.close();
                        byte[] bArr4 = photoSizeFindPhotoCachedSize.bytes;
                        Utilities.aesCtrDecryptionByteArray(bArr4, bArr2, bArr3, 0, bArr4.length, 0);
                    } catch (Exception e) {
                        FileLog.e(e);
                    }
                }
                RandomAccessFile randomAccessFile2 = new RandomAccessFile(pathToAttach, "rws");
                randomAccessFile2.write(photoSizeFindPhotoCachedSize.bytes);
                randomAccessFile2.close();
            }
            tL_photoSize_layer127 = new TLRPC.TL_photoSize_layer127();
            tL_photoSize_layer127.w = photoSizeFindPhotoCachedSize.w;
            tL_photoSize_layer127.h = photoSizeFindPhotoCachedSize.h;
            tL_photoSize_layer127.location = photoSizeFindPhotoCachedSize.location;
            tL_photoSize_layer127.size = photoSizeFindPhotoCachedSize.size;
            tL_photoSize_layer127.type = photoSizeFindPhotoCachedSize.type;
        }
        if (messageMedia instanceof TLRPC.TL_messageMediaPhoto) {
            int size = messageMedia.photo.sizes.size();
            while (i < size) {
                if (((TLRPC.PhotoSize) messageMedia.photo.sizes.get(i)) instanceof TLRPC.TL_photoCachedSize) {
                    messageMedia.photo.sizes.set(i, tL_photoSize_layer127);
                    return;
                }
                i++;
            }
            return;
        }
        if (messageMedia instanceof TLRPC.TL_messageMediaDocument) {
            int size2 = messageMedia.document.thumbs.size();
            while (i < size2) {
                if (messageMedia.document.thumbs.get(i) instanceof TLRPC.TL_photoCachedSize) {
                    messageMedia.document.thumbs.set(i, tL_photoSize_layer127);
                    return;
                }
                i++;
            }
            return;
        }
        if (messageMedia instanceof TLRPC.TL_messageMediaWebPage) {
            int size3 = messageMedia.webpage.photo.sizes.size();
            while (i < size3) {
                if (((TLRPC.PhotoSize) messageMedia.webpage.photo.sizes.get(i)) instanceof TLRPC.TL_photoCachedSize) {
                    messageMedia.webpage.photo.sizes.set(i, tL_photoSize_layer127);
                    return;
                }
                i++;
            }
        }
    }

    private static TLRPC.PhotoSize findPhotoCachedSize(TLRPC.Message message) {
        TLRPC.MessageMedia messageMedia = message.media;
        int i = 0;
        if (messageMedia instanceof TLRPC.TL_messageMediaPhoto) {
            int size = messageMedia.photo.sizes.size();
            while (i < size) {
                TLRPC.PhotoSize photoSize = (TLRPC.PhotoSize) message.media.photo.sizes.get(i);
                if (photoSize instanceof TLRPC.TL_photoCachedSize) {
                    return photoSize;
                }
                i++;
            }
            return null;
        }
        if (messageMedia instanceof TLRPC.TL_messageMediaDocument) {
            TLRPC.Document document = messageMedia.document;
            if (document == null) {
                return null;
            }
            int size2 = document.thumbs.size();
            while (i < size2) {
                TLRPC.PhotoSize photoSize2 = message.media.document.thumbs.get(i);
                if (photoSize2 instanceof TLRPC.TL_photoCachedSize) {
                    return photoSize2;
                }
                i++;
            }
            return null;
        }
        if (messageMedia instanceof TLRPC.TL_messageMediaWebPage) {
            TLRPC.Photo photo = messageMedia.webpage.photo;
            if (photo == null) {
                return null;
            }
            int size3 = photo.sizes.size();
            while (i < size3) {
                TLRPC.PhotoSize photoSize3 = (TLRPC.PhotoSize) message.media.webpage.photo.sizes.get(i);
                if (photoSize3 instanceof TLRPC.TL_photoCachedSize) {
                    return photoSize3;
                }
                i++;
            }
            return null;
        }
        if ((messageMedia instanceof TLRPC.TL_messageMediaInvoice) && !messageMedia.extended_media.isEmpty() && (message.media.extended_media.get(0) instanceof TLRPC.TL_messageExtendedMediaPreview)) {
            return ((TLRPC.TL_messageExtendedMediaPreview) message.media.extended_media.get(0)).thumb;
        }
        return null;
    }

    private static TLRPC.PhotoSize findPhotoCachedSize(TLRPC.MessageMedia messageMedia) {
        int i = 0;
        if (messageMedia instanceof TLRPC.TL_messageMediaPhoto) {
            int size = messageMedia.photo.sizes.size();
            while (i < size) {
                TLRPC.PhotoSize photoSize = (TLRPC.PhotoSize) messageMedia.photo.sizes.get(i);
                if (photoSize instanceof TLRPC.TL_photoCachedSize) {
                    return photoSize;
                }
                i++;
            }
            return null;
        }
        if (messageMedia instanceof TLRPC.TL_messageMediaDocument) {
            TLRPC.Document document = messageMedia.document;
            if (document == null) {
                return null;
            }
            int size2 = document.thumbs.size();
            while (i < size2) {
                TLRPC.PhotoSize photoSize2 = messageMedia.document.thumbs.get(i);
                if (photoSize2 instanceof TLRPC.TL_photoCachedSize) {
                    return photoSize2;
                }
                i++;
            }
            return null;
        }
        if (messageMedia instanceof TLRPC.TL_messageMediaWebPage) {
            TLRPC.Photo photo = messageMedia.webpage.photo;
            if (photo == null) {
                return null;
            }
            int size3 = photo.sizes.size();
            while (i < size3) {
                TLRPC.PhotoSize photoSize3 = (TLRPC.PhotoSize) messageMedia.webpage.photo.sizes.get(i);
                if (photoSize3 instanceof TLRPC.TL_photoCachedSize) {
                    return photoSize3;
                }
                i++;
            }
            return null;
        }
        if ((messageMedia instanceof TLRPC.TL_messageMediaInvoice) && !messageMedia.extended_media.isEmpty() && (messageMedia.extended_media.get(0) instanceof TLRPC.TL_messageExtendedMediaPreview)) {
            return ((TLRPC.TL_messageExtendedMediaPreview) messageMedia.extended_media.get(0)).thumb;
        }
        return null;
    }

    public static void saveMessagesThumbs(ArrayList<TLRPC.Message> arrayList) {
        if (arrayList == null || arrayList.isEmpty()) {
            return;
        }
        for (int i = 0; i < arrayList.size(); i++) {
            saveMessageThumbs(arrayList.get(i));
        }
    }

    public static MessageThumb generateMessageThumb(TLRPC.Message message) {
        int i;
        int i2;
        Bitmap strippedPhotoBitmap;
        byte[] bArr;
        TLRPC.PhotoSize photoSizeFindPhotoCachedSize = findPhotoCachedSize(message);
        if (photoSizeFindPhotoCachedSize != null && (bArr = photoSizeFindPhotoCachedSize.bytes) != null && bArr.length != 0) {
            File pathToAttach = FileLoader.getInstance(UserConfig.selectedAccount).getPathToAttach(photoSizeFindPhotoCachedSize, true);
            TLRPC.TL_photoSize_layer127 tL_photoSize_layer127 = new TLRPC.TL_photoSize_layer127();
            tL_photoSize_layer127.w = photoSizeFindPhotoCachedSize.w;
            tL_photoSize_layer127.h = photoSizeFindPhotoCachedSize.h;
            tL_photoSize_layer127.location = photoSizeFindPhotoCachedSize.location;
            tL_photoSize_layer127.size = photoSizeFindPhotoCachedSize.size;
            tL_photoSize_layer127.type = photoSizeFindPhotoCachedSize.type;
            if (pathToAttach.exists() && message.grouped_id == 0) {
                org.telegram.ui.Components.Point messageSize = ChatMessageCell.getMessageSize(photoSizeFindPhotoCachedSize.w, photoSizeFindPhotoCachedSize.h);
                String str = String.format(Locale.US, "%d_%d@%d_%d_b", Long.valueOf(photoSizeFindPhotoCachedSize.location.volume_id), Integer.valueOf(photoSizeFindPhotoCachedSize.location.local_id), Integer.valueOf((int) (messageSize.x / AndroidUtilities.density)), Integer.valueOf((int) (messageSize.y / AndroidUtilities.density)));
                if (!getInstance().isInMemCache(str, false)) {
                    String path = pathToAttach.getPath();
                    float f = messageSize.x;
                    float f2 = AndroidUtilities.density;
                    Bitmap bitmapLoadBitmap = loadBitmap(path, null, (int) (f / f2), (int) (messageSize.y / f2), false);
                    if (bitmapLoadBitmap != null) {
                        Utilities.blurBitmap(bitmapLoadBitmap, 3, 1, bitmapLoadBitmap.getWidth(), bitmapLoadBitmap.getHeight(), bitmapLoadBitmap.getRowBytes());
                        float f3 = messageSize.x;
                        float f4 = AndroidUtilities.density;
                        Bitmap bitmapCreateScaledBitmap = Bitmaps.createScaledBitmap(bitmapLoadBitmap, (int) (f3 / f4), (int) (messageSize.y / f4), true);
                        if (bitmapCreateScaledBitmap != bitmapLoadBitmap) {
                            bitmapLoadBitmap.recycle();
                            bitmapLoadBitmap = bitmapCreateScaledBitmap;
                        }
                        return new MessageThumb(str, new BitmapDrawable(bitmapLoadBitmap));
                    }
                }
            }
        } else {
            TLRPC.MessageMedia messageMedia = message.media;
            if (messageMedia instanceof TLRPC.TL_messageMediaDocument) {
                int size = messageMedia.document.thumbs.size();
                for (int i3 = 0; i3 < size; i3++) {
                    TLRPC.PhotoSize photoSize = message.media.document.thumbs.get(i3);
                    if (photoSize instanceof TLRPC.TL_photoStrippedSize) {
                        TLRPC.PhotoSize closestPhotoSizeWithSize = FileLoader.getClosestPhotoSizeWithSize(message.media.document.thumbs, 320);
                        if (closestPhotoSizeWithSize == null) {
                            int i4 = 0;
                            while (true) {
                                if (i4 >= message.media.document.attributes.size()) {
                                    i = 0;
                                    i2 = 0;
                                    break;
                                }
                                if (message.media.document.attributes.get(i4) instanceof TLRPC.TL_documentAttributeVideo) {
                                    TLRPC.TL_documentAttributeVideo tL_documentAttributeVideo = (TLRPC.TL_documentAttributeVideo) message.media.document.attributes.get(i4);
                                    i2 = tL_documentAttributeVideo.h;
                                    i = tL_documentAttributeVideo.w;
                                    break;
                                }
                                i4++;
                            }
                        } else {
                            i2 = closestPhotoSizeWithSize.h;
                            i = closestPhotoSizeWithSize.w;
                        }
                        org.telegram.ui.Components.Point messageSize2 = ChatMessageCell.getMessageSize(i, i2);
                        String str2 = String.format(Locale.US, "%s_false@%d_%d_b", ImageLocation.getStrippedKey(message, message, photoSize), Integer.valueOf((int) (messageSize2.x / AndroidUtilities.density)), Integer.valueOf((int) (messageSize2.y / AndroidUtilities.density)));
                        if (!getInstance().isInMemCache(str2, false) && (strippedPhotoBitmap = getStrippedPhotoBitmap(photoSize.bytes, null)) != null) {
                            Utilities.blurBitmap(strippedPhotoBitmap, 3, 1, strippedPhotoBitmap.getWidth(), strippedPhotoBitmap.getHeight(), strippedPhotoBitmap.getRowBytes());
                            float f5 = messageSize2.x;
                            float f6 = AndroidUtilities.density;
                            Bitmap bitmapCreateScaledBitmap2 = Bitmaps.createScaledBitmap(strippedPhotoBitmap, (int) (f5 / f6), (int) (messageSize2.y / f6), true);
                            if (bitmapCreateScaledBitmap2 != strippedPhotoBitmap) {
                                strippedPhotoBitmap.recycle();
                                strippedPhotoBitmap = bitmapCreateScaledBitmap2;
                            }
                            return new MessageThumb(str2, new BitmapDrawable(strippedPhotoBitmap));
                        }
                    }
                }
            }
        }
        return null;
    }

    public void onFragmentStackChanged() {
        for (int i = 0; i < this.cachedAnimatedFileDrawables.size(); i++) {
            this.cachedAnimatedFileDrawables.get(i).repeatCount = 0;
        }
    }

    public DispatchQueuePriority getCacheOutQueue() {
        return this.cacheOutQueue;
    }

    public static class MessageThumb {
        BitmapDrawable drawable;
        String key;

        public MessageThumb(String str, BitmapDrawable bitmapDrawable) {
            this.key = str;
            this.drawable = bitmapDrawable;
        }
    }
}
