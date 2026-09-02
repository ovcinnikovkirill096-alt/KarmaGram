package org.telegram.ui.web;

import android.os.AsyncTask;
import android.os.Build;
import android.webkit.MimeTypeMap;
import androidx.annotation.Keep;
import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.ProtocolException;
import java.net.URL;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;
import org.telegram.messenger.AndroidUtilities;
import org.telegram.messenger.FileLog;
import org.telegram.messenger.UserConfig;
import org.telegram.messenger.Utilities;
import org.telegram.ui.Stories.recorder.StoryEntry;

@Keep
public class HttpGetFileTask extends AsyncTask<String, Void, File> {
    private Utilities.Callback<File> doneCallback;
    private Exception exception;
    private File file;
    private long max_size = -1;
    private String overrideExt;
    private Utilities.Callback<Float> progressCallback;

    public HttpGetFileTask(Utilities.Callback<File> callback, Utilities.Callback<Float> callback2) {
        this.doneCallback = callback;
        this.progressCallback = callback2;
    }

    @Keep
    public HttpGetFileTask setOverrideExtension(String str) {
        this.overrideExt = str;
        return this;
    }

    @Keep
    public HttpGetFileTask setDestFile(File file) {
        this.file = file;
        return this;
    }

    @Keep
    public HttpGetFileTask setMaxSize(long j) {
        this.max_size = j;
        return this;
    }

    /* JADX INFO: Access modifiers changed from: protected */
    /* JADX WARN: Code duplicated, block: B:134:0x00ec A[EXC_TOP_SPLITTER, SYNTHETIC] */
    /* JADX WARN: Code duplicated, block: B:140:0x00dd A[EXC_TOP_SPLITTER, SYNTHETIC] */
    /* JADX WARN: Code duplicated, block: B:149:0x0126 A[SYNTHETIC] */
    /* JADX WARN: Code duplicated, block: B:157:0x0123 A[SYNTHETIC] */
    /* JADX WARN: Code duplicated, block: B:158:0x0123 A[SYNTHETIC] */
    /* JADX WARN: Code duplicated, block: B:35:0x0085 A[Catch: Exception -> 0x0041, TryCatch #13 {Exception -> 0x0041, blocks: (B:9:0x0013, B:11:0x0025, B:14:0x0046, B:18:0x0055, B:20:0x005e, B:24:0x0068, B:33:0x007f, B:35:0x0085, B:37:0x008f, B:41:0x0099, B:43:0x00a0, B:45:0x00a3, B:47:0x00a7, B:51:0x00b8, B:50:0x00ac, B:52:0x00c0, B:36:0x008a, B:19:0x005a), top: B:144:0x0013 }] */
    /* JADX WARN: Code duplicated, block: B:36:0x008a A[Catch: Exception -> 0x0041, TryCatch #13 {Exception -> 0x0041, blocks: (B:9:0x0013, B:11:0x0025, B:14:0x0046, B:18:0x0055, B:20:0x005e, B:24:0x0068, B:33:0x007f, B:35:0x0085, B:37:0x008f, B:41:0x0099, B:43:0x00a0, B:45:0x00a3, B:47:0x00a7, B:51:0x00b8, B:50:0x00ac, B:52:0x00c0, B:36:0x008a, B:19:0x005a), top: B:144:0x0013 }] */
    /* JADX WARN: Code duplicated, block: B:47:0x00a7 A[Catch: Exception -> 0x0041, TryCatch #13 {Exception -> 0x0041, blocks: (B:9:0x0013, B:11:0x0025, B:14:0x0046, B:18:0x0055, B:20:0x005e, B:24:0x0068, B:33:0x007f, B:35:0x0085, B:37:0x008f, B:41:0x0099, B:43:0x00a0, B:45:0x00a3, B:47:0x00a7, B:51:0x00b8, B:50:0x00ac, B:52:0x00c0, B:36:0x008a, B:19:0x005a), top: B:144:0x0013 }] */
    /* JADX WARN: Code duplicated, block: B:49:0x00ab  */
    /* JADX WARN: Code duplicated, block: B:50:0x00ac A[Catch: Exception -> 0x0041, TryCatch #13 {Exception -> 0x0041, blocks: (B:9:0x0013, B:11:0x0025, B:14:0x0046, B:18:0x0055, B:20:0x005e, B:24:0x0068, B:33:0x007f, B:35:0x0085, B:37:0x008f, B:41:0x0099, B:43:0x00a0, B:45:0x00a3, B:47:0x00a7, B:51:0x00b8, B:50:0x00ac, B:52:0x00c0, B:36:0x008a, B:19:0x005a), top: B:144:0x0013 }] */
    /* JADX WARN: Code duplicated, block: B:77:0x010c  */
    /* JADX WARN: Code duplicated, block: B:79:0x0110  */
    /* JADX WARN: Code duplicated, block: B:82:0x011b A[Catch: all -> 0x00f2, TryCatch #9 {all -> 0x00f2, blocks: (B:59:0x00dd, B:61:0x00ec, B:80:0x0113, B:82:0x011b, B:66:0x00f6, B:84:0x0126, B:86:0x012a), top: B:140:0x00dd, inners: #6 }] */
    /* JADX WARN: Code duplicated, block: B:86:0x012a A[Catch: all -> 0x00f2, TRY_LEAVE, TryCatch #9 {all -> 0x00f2, blocks: (B:59:0x00dd, B:61:0x00ec, B:80:0x0113, B:82:0x011b, B:66:0x00f6, B:84:0x0126, B:86:0x012a), top: B:140:0x00dd, inners: #6 }] */
    /* JADX WARN: Code duplicated, block: B:88:0x0134 A[Catch: all -> 0x0109, TRY_ENTER, TRY_LEAVE, TryCatch #3 {all -> 0x0109, blocks: (B:67:0x00f9, B:88:0x0134, B:102:0x0155, B:101:0x0152, B:98:0x014d), top: B:129:0x00ce, inners: #8 }] */
    /* JADX WARN: Code duplicated, block: B:92:0x0143  */
    /* JADX WARN: Code duplicated, block: B:93:0x0144 A[Catch: Exception -> 0x0103, TRY_LEAVE, TryCatch #11 {Exception -> 0x0103, blocks: (B:69:0x00ff, B:116:0x016f, B:115:0x016c, B:90:0x013a, B:93:0x0144, B:112:0x0167), top: B:132:0x0167, inners: #5 }] */
    /* JADX WARN: Multi-variable type inference failed */
    /* JADX WARN: Type inference failed for: r13v2, types: [java.lang.StringBuilder] */
    /* JADX WARN: Type inference failed for: r16v0 */
    /* JADX WARN: Type inference failed for: r16v1 */
    /* JADX WARN: Type inference failed for: r16v10 */
    /* JADX WARN: Type inference failed for: r16v11, types: [long] */
    /* JADX WARN: Type inference failed for: r16v12 */
    /* JADX WARN: Type inference failed for: r16v2, types: [int] */
    /* JADX WARN: Type inference failed for: r16v3 */
    /* JADX WARN: Type inference failed for: r16v4 */
    /* JADX WARN: Type inference failed for: r16v5 */
    /* JADX WARN: Type inference failed for: r16v6 */
    /* JADX WARN: Type inference failed for: r16v7 */
    /* JADX WARN: Type inference failed for: r16v8 */
    /* JADX WARN: Type inference failed for: r16v9 */
    /* JADX WARN: Type inference failed for: r4v0 */
    /* JADX WARN: Type inference failed for: r4v1, types: [long] */
    /* JADX WARN: Type inference failed for: r4v14 */
    /* JADX WARN: Type inference failed for: r4v18 */
    /* JADX WARN: Type inference failed for: r4v3 */
    /* JADX WARN: Type inference failed for: r4v6 */
    /* JADX WARN: Type inference failed for: r4v8 */
    /* JADX WARN: Type inference failed for: r7v0 */
    /* JADX WARN: Type inference failed for: r7v1, types: [long] */
    /* JADX WARN: Type inference failed for: r7v10 */
    /* JADX WARN: Type inference failed for: r7v11, types: [long] */
    /* JADX WARN: Type inference failed for: r7v12 */
    /* JADX WARN: Type inference failed for: r7v13, types: [long] */
    /* JADX WARN: Type inference failed for: r7v14 */
    /* JADX WARN: Type inference failed for: r7v16 */
    /* JADX WARN: Type inference failed for: r7v17 */
    /* JADX WARN: Type inference failed for: r7v18 */
    /* JADX WARN: Type inference failed for: r7v19 */
    /* JADX WARN: Type inference failed for: r7v2 */
    /* JADX WARN: Type inference failed for: r7v20 */
    /* JADX WARN: Type inference failed for: r7v21 */
    /* JADX WARN: Type inference failed for: r7v3 */
    /* JADX WARN: Type inference failed for: r7v4 */
    /* JADX WARN: Type inference failed for: r7v5 */
    /* JADX WARN: Type inference failed for: r7v6 */
    /* JADX WARN: Type inference failed for: r7v7 */
    /* JADX WARN: Type inference failed for: r7v8 */
    /* JADX WARN: Type inference failed for: r7v9 */
    @Override // android.os.AsyncTask
    public File doInBackground(String... strArr) throws Throwable {
        ?? r16;
        InputStream errorStream;
        long contentLength;
        BufferedInputStream bufferedInputStream;
        Throwable th;
        FileOutputStream fileOutputStream;
        Throwable th2;
        FileChannel channel;
        byte[] bArr;
        int i;
        final float fClamp01;
        String extensionFromMimeType;
        String str = strArr[0];
        ?? r4 = 0;
        int i2 = 0;
        ?? r7 = 0;
        while (true) {
            if (i2 < 5) {
                boolean z = i2 > 0;
                try {
                    HttpURLConnection httpURLConnection = (HttpURLConnection) new URL(str).openConnection();
                    httpURLConnection.setRequestMethod("GET");
                    if (z) {
                        httpURLConnection.setRequestProperty("Range", "bytes=" + r7 + "-");
                    }
                    httpURLConnection.setDoInput(true);
                    int responseCode = httpURLConnection.getResponseCode();
                    if (responseCode >= 200 && responseCode < 300) {
                        errorStream = httpURLConnection.getInputStream();
                    } else {
                        errorStream = httpURLConnection.getErrorStream();
                    }
                    int responseCode2 = httpURLConnection.getResponseCode();
                    r7 = r7;
                    if (z && responseCode2 != 206) {
                        r7 = r7;
                        FileLog.d("failed to resume, server doesn't support partial content. downloading from the beginning");
                        try {
                            File file = this.file;
                            if (file != null) {
                                try {
                                    file.delete();
                                } catch (Exception unused) {
                                }
                                this.file = null;
                            }
                            z = false;
                            r7 = r4;
                            r7 = r7;
                            if (Build.VERSION.SDK_INT >= 24) {
                                contentLength = httpURLConnection.getContentLengthLong();
                            } else {
                                contentLength = httpURLConnection.getContentLength();
                            }
                            long j = this.max_size;
                            r16 = (j > r4 ? 1 : (j == r4 ? 0 : -1));
                            if (r16 <= 0) {
                            }
                            if (this.file == null) {
                                extensionFromMimeType = this.overrideExt;
                                if (extensionFromMimeType != null) {
                                    extensionFromMimeType = MimeTypeMap.getSingleton().getExtensionFromMimeType(httpURLConnection.getContentType());
                                }
                                this.file = StoryEntry.makeCacheFile(UserConfig.selectedAccount, extensionFromMimeType);
                            }
                            bufferedInputStream = new BufferedInputStream(errorStream, 16384);
                            fileOutputStream = new FileOutputStream(this.file, z);
                            channel = fileOutputStream.getChannel();
                            bArr = new byte[16384];
                            r4 = r4;
                            r7 = r7;
                            while (true) {
                                i = bufferedInputStream.read(bArr);
                                r16 = r4;
                                if (i != -1) {
                                    channel.write(ByteBuffer.wrap(bArr, 0, i));
                                    r7 += (long) i;
                                    if (isCancelled()) {
                                        this.file.delete();
                                        break;
                                        break;
                                    }
                                    if (contentLength > r16) {
                                        fClamp01 = Utilities.clamp01(((float) r7) / contentLength);
                                        if (this.progressCallback != null) {
                                            AndroidUtilities.runOnUIThread(new Runnable() { // from class: org.telegram.ui.web.HttpGetFileTask$$ExternalSyntheticLambda0
                                                @Override // java.lang.Runnable
                                                public final void run() {
                                                    this.f$0.lambda$doInBackground$0(fClamp01);
                                                }
                                            });
                                        }
                                    }
                                    r4 = r16;
                                    r7 = r7;
                                } else {
                                    if (this.progressCallback != null) {
                                        AndroidUtilities.runOnUIThread(new Runnable() { // from class: org.telegram.ui.web.HttpGetFileTask$$ExternalSyntheticLambda1
                                            @Override // java.lang.Runnable
                                            public final void run() {
                                                this.f$0.lambda$doInBackground$1();
                                            }
                                        });
                                    }
                                    if (channel != null) {
                                        channel.close();
                                    }
                                    fileOutputStream.close();
                                    bufferedInputStream.close();
                                    if (isCancelled()) {
                                        return null;
                                    }
                                    return this.file;
                                }
                            }
                        } catch (Exception e) {
                            e = e;
                            r7 = r4;
                            r16 = r7;
                        }
                    } else {
                        r7 = r7;
                        if (Build.VERSION.SDK_INT >= 24) {
                            contentLength = httpURLConnection.getContentLengthLong();
                        } else {
                            contentLength = httpURLConnection.getContentLength();
                        }
                        long j2 = this.max_size;
                        r16 = (j2 > r4 ? 1 : (j2 == r4 ? 0 : -1));
                        if (r16 <= 0 && contentLength > j2) {
                            errorStream.close();
                            if (this.file != null) {
                                this.file = null;
                            }
                            return null;
                        }
                        if (this.file == null) {
                            extensionFromMimeType = this.overrideExt;
                            if (extensionFromMimeType != null) {
                                extensionFromMimeType = MimeTypeMap.getSingleton().getExtensionFromMimeType(httpURLConnection.getContentType());
                            }
                            this.file = StoryEntry.makeCacheFile(UserConfig.selectedAccount, extensionFromMimeType);
                        }
                        bufferedInputStream = new BufferedInputStream(errorStream, 16384);
                        try {
                            try {
                                fileOutputStream = new FileOutputStream(this.file, z);
                                try {
                                    try {
                                        channel = fileOutputStream.getChannel();
                                        try {
                                            bArr = new byte[16384];
                                            r4 = r4;
                                            r7 = r7;
                                            while (true) {
                                                i = bufferedInputStream.read(bArr);
                                                r16 = r4;
                                                if (i != -1) {
                                                    try {
                                                        channel.write(ByteBuffer.wrap(bArr, 0, i));
                                                        r7 += (long) i;
                                                        if (isCancelled()) {
                                                            try {
                                                                this.file.delete();
                                                                break;
                                                            } catch (Exception e2) {
                                                                FileLog.e(e2);
                                                            }
                                                        } else {
                                                            if (contentLength > r16) {
                                                                fClamp01 = Utilities.clamp01(((float) r7) / contentLength);
                                                                if (this.progressCallback != null) {
                                                                    AndroidUtilities.runOnUIThread(new Runnable() { // from class: org.telegram.ui.web.HttpGetFileTask$$ExternalSyntheticLambda0
                                                                        @Override // java.lang.Runnable
                                                                        public final void run() {
                                                                            this.f$0.lambda$doInBackground$0(fClamp01);
                                                                        }
                                                                    });
                                                                }
                                                            }
                                                            r4 = r16;
                                                            r7 = r7;
                                                        }
                                                    } catch (Throwable th3) {
                                                        th = th3;
                                                        Throwable th4 = th;
                                                        if (channel == null) {
                                                            throw th4;
                                                        }
                                                        try {
                                                            channel.close();
                                                            throw th4;
                                                        } catch (Throwable th5) {
                                                            th4.addSuppressed(th5);
                                                            throw th4;
                                                        }
                                                    }
                                                } else {
                                                    if (this.progressCallback != null) {
                                                        AndroidUtilities.runOnUIThread(new Runnable() { // from class: org.telegram.ui.web.HttpGetFileTask$$ExternalSyntheticLambda1
                                                            @Override // java.lang.Runnable
                                                            public final void run() {
                                                                this.f$0.lambda$doInBackground$1();
                                                            }
                                                        });
                                                    }
                                                    if (channel != null) {
                                                        channel.close();
                                                    }
                                                    fileOutputStream.close();
                                                    bufferedInputStream.close();
                                                    if (isCancelled()) {
                                                        return null;
                                                    }
                                                    return this.file;
                                                }
                                            }
                                        } catch (Throwable th6) {
                                            th = th6;
                                            r16 = r4;
                                        }
                                    } catch (Throwable th7) {
                                        th = th7;
                                        th2 = th;
                                        try {
                                            fileOutputStream.close();
                                            throw th2;
                                        } catch (Throwable th8) {
                                            th2.addSuppressed(th8);
                                            throw th2;
                                        }
                                    }
                                } catch (Throwable th9) {
                                    th = th9;
                                    r16 = r4;
                                    th2 = th;
                                    fileOutputStream.close();
                                    throw th2;
                                }
                            } catch (Throwable th10) {
                                th = th10;
                                r16 = r4;
                                th = th;
                                try {
                                    try {
                                        bufferedInputStream.close();
                                        throw th;
                                    } catch (Throwable th11) {
                                        th.addSuppressed(th11);
                                        throw th;
                                    }
                                } catch (Exception e3) {
                                    e = e3;
                                }
                            }
                        } catch (Throwable th12) {
                            th = th12;
                            th = th;
                            bufferedInputStream.close();
                            throw th;
                        }
                    }
                } catch (Exception e4) {
                    e = e4;
                    r16 = r4;
                }
                if (e instanceof ProtocolException) {
                    FileLog.d("got unexpected end of stream, lets try to resume");
                    i2++;
                    r4 = r16;
                    r7 = r7;
                } else {
                    this.exception = e;
                    FileLog.e(e);
                    return null;
                }
            } else {
                this.exception = new RuntimeException("too many retries");
                return null;
            }
        }
        channel.close();
        fileOutputStream.close();
        bufferedInputStream.close();
        return null;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$doInBackground$0(float f) {
        this.progressCallback.run(Float.valueOf(f));
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$doInBackground$1() {
        this.progressCallback.run(Float.valueOf(1.0f));
    }

    /* JADX INFO: Access modifiers changed from: protected */
    @Override // android.os.AsyncTask
    public void onPostExecute(File file) {
        Utilities.Callback<File> callback = this.doneCallback;
        if (callback != null) {
            if (this.exception == null) {
                callback.run(file);
            } else {
                callback.run(null);
            }
        }
    }
}
