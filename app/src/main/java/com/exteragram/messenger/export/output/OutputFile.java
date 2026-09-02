package com.exteragram.messenger.export.output;

import android.util.Log;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.nio.channels.FileChannel;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicLong;
import okhttp3.internal.url._UrlKt;
import org.telegram.messenger.FileLog;
import org.telegram.messenger.Utilities;
import org.telegram.tgnet.NativeByteBuffer;

public class OutputFile {
    public final File _file;
    private boolean _inStats = false;
    private long _offset = 0;
    private final Stats _stats;

    public OutputFile(String str, Stats stats) {
        File file = new File(str);
        this._file = file;
        try {
            if (file.getPath().contains("/")) {
                file.getParentFile().mkdirs();
            }
            file.createNewFile();
            this._stats = stats;
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public static String PrepareRelativePath(String str, String str2) {
        String str3;
        if (!new File(str + "/" + str2).exists()) {
            return str2;
        }
        int iIndexOf = str2.indexOf(46);
        int i = 0;
        final String strSubstring = str2.substring(0, iIndexOf);
        final String strSubstring2 = iIndexOf >= 0 ? str2.substring(iIndexOf) : _UrlKt.FRAGMENT_ENCODE_SET;
        Utilities.CallbackReturn callbackReturn = new Utilities.CallbackReturn() { // from class: com.exteragram.messenger.export.output.OutputFile$$ExternalSyntheticLambda0
            @Override // org.telegram.messenger.Utilities.CallbackReturn
            public final Object run(Object obj) {
                return OutputFile.$r8$lambda$ApE4zRWH4xloOxmAXEkNaQI5bA4(strSubstring, strSubstring2, (Integer) obj);
            }
        };
        do {
            i++;
            str3 = (String) callbackReturn.run(Integer.valueOf(i));
        } while (new File(str + str3).exists());
        return str3;
    }

    public static /* synthetic */ String $r8$lambda$ApE4zRWH4xloOxmAXEkNaQI5bA4(String str, String str2, Integer num) {
        return str + " (" + num + ")" + str2;
    }

    public long size() {
        return this._offset;
    }

    public boolean empty() {
        return this._offset == 0;
    }

    public AbstractWriter.Result writeBlock(String str) throws Throwable {
        AbstractWriter.Result resultWriteBlockAttempt = writeBlockAttempt(str);
        if (resultWriteBlockAttempt.isSuccess()) {
            return resultWriteBlockAttempt;
        }
        throw new IllegalStateException("result is not success for block: " + str);
    }

    public AbstractWriter.Result writeBlock(NativeByteBuffer nativeByteBuffer) {
        AbstractWriter.Result resultWriteBlockAttempt = writeBlockAttempt(nativeByteBuffer);
        if (resultWriteBlockAttempt.isSuccess()) {
            return resultWriteBlockAttempt;
        }
        throw new IllegalStateException("result is not success for block: " + nativeByteBuffer);
    }

    public AbstractWriter.Result writeBlock(byte[] bArr) throws Throwable {
        AbstractWriter.Result resultWriteBlockAttempt = writeBlockAttempt(bArr);
        if (resultWriteBlockAttempt.isSuccess()) {
            return resultWriteBlockAttempt;
        }
        throw new IllegalStateException("result is not success for block: " + bArr);
    }

    public AbstractWriter.Result writeBlockAttempt(String str) throws Throwable {
        Stats stats = this._stats;
        if (stats != null && !this._inStats) {
            this._inStats = true;
            stats.incrementFiles();
        }
        int length = str.length();
        if (length == 0) {
            Log.e("exteraGram", "size of block to write was zero!");
            return AbstractWriter.Result.Success();
        }
        FileOutputStream fileOutputStream = null;
        try {
            try {
                FileOutputStream fileOutputStream2 = new FileOutputStream(this._file, true);
                try {
                    fileOutputStream2.write(str.getBytes());
                    this._offset += (long) length;
                    Stats stats2 = this._stats;
                    if (stats2 != null) {
                        stats2.incrementBytes(length);
                    }
                    try {
                        fileOutputStream2.close();
                        return AbstractWriter.Result.Success();
                    } catch (IOException e) {
                        throw new RuntimeException(e);
                    }
                } catch (Exception e2) {
                    e = e2;
                    fileOutputStream = fileOutputStream2;
                    FileLog.e(e);
                    if (fileOutputStream != null) {
                        try {
                            fileOutputStream.close();
                        } catch (IOException e3) {
                            throw new RuntimeException(e3);
                        }
                    }
                    return AbstractWriter.Result.Error();
                } catch (Throwable th) {
                    th = th;
                    fileOutputStream = fileOutputStream2;
                    if (fileOutputStream != null) {
                        try {
                            fileOutputStream.close();
                        } catch (IOException e4) {
                            throw new RuntimeException(e4);
                        }
                    }
                    throw th;
                }
            } catch (Throwable th2) {
                th = th2;
            }
        } catch (Exception e5) {
            e = e5;
        }
    }

    public AbstractWriter.Result writeBlockAttempt(byte[] bArr) throws Throwable {
        Stats stats = this._stats;
        if (stats != null && !this._inStats) {
            this._inStats = true;
            stats.incrementFiles();
        }
        int length = bArr.length;
        if (length == 0) {
            Log.e("exteraGram", "size of block to write was zero!");
            return AbstractWriter.Result.Success();
        }
        FileOutputStream fileOutputStream = null;
        try {
            try {
                FileOutputStream fileOutputStream2 = new FileOutputStream(this._file, true);
                try {
                    fileOutputStream2.write(bArr);
                    this._offset += (long) length;
                    Stats stats2 = this._stats;
                    if (stats2 != null) {
                        stats2.incrementBytes(length);
                    }
                    try {
                        fileOutputStream2.close();
                        return AbstractWriter.Result.Success();
                    } catch (IOException e) {
                        throw new RuntimeException(e);
                    }
                } catch (Exception e2) {
                    e = e2;
                    fileOutputStream = fileOutputStream2;
                    FileLog.e(e);
                    if (fileOutputStream != null) {
                        try {
                            fileOutputStream.close();
                        } catch (IOException e3) {
                            throw new RuntimeException(e3);
                        }
                    }
                    return AbstractWriter.Result.Error();
                } catch (Throwable th) {
                    th = th;
                    fileOutputStream = fileOutputStream2;
                    if (fileOutputStream != null) {
                        try {
                            fileOutputStream.close();
                        } catch (IOException e4) {
                            throw new RuntimeException(e4);
                        }
                    }
                    throw th;
                }
            } catch (Exception e5) {
                e = e5;
            }
        } catch (Throwable th2) {
            th = th2;
        }
    }

    public AbstractWriter.Result writeBlockAttempt(NativeByteBuffer nativeByteBuffer) {
        RandomAccessFile randomAccessFile;
        IOException e;
        Stats stats = this._stats;
        if (stats != null && !this._inStats) {
            this._inStats = true;
            stats.incrementFiles();
        }
        int iLimit = nativeByteBuffer.buffer.limit();
        if (iLimit == 0) {
            Log.e("exteraGram", "size of block to write was zero!");
            return AbstractWriter.Result.Success();
        }
        try {
            randomAccessFile = new RandomAccessFile(this._file, "rws");
            try {
                randomAccessFile.seek(randomAccessFile.length());
            } catch (IOException e2) {
                e = e2;
                FileLog.e(e);
            }
        } catch (IOException e3) {
            randomAccessFile = null;
            e = e3;
        }
        try {
            try {
                FileChannel channel = randomAccessFile.getChannel();
                channel.write(nativeByteBuffer.buffer);
                this._offset += (long) iLimit;
                Stats stats2 = this._stats;
                if (stats2 != null) {
                    stats2.incrementBytes(iLimit);
                }
                channel.close();
                try {
                    randomAccessFile.close();
                    return AbstractWriter.Result.Success();
                } catch (IOException e4) {
                    throw new RuntimeException(e4);
                }
            } catch (Throwable th) {
                try {
                    randomAccessFile.close();
                    throw th;
                } catch (IOException e5) {
                    throw new RuntimeException(e5);
                }
            }
        } catch (Exception e6) {
            FileLog.e(e6);
            try {
                randomAccessFile.close();
                return AbstractWriter.Result.Error();
            } catch (IOException e7) {
                throw new RuntimeException(e7);
            }
        }
    }

    public static class Stats {
        private final AtomicInteger _files = new AtomicInteger(0);
        private final AtomicLong _bytes = new AtomicLong(0);

        public void incrementFiles() {
            this._files.getAndIncrement();
        }

        public void incrementBytes(int i) {
            this._bytes.addAndGet(i);
        }

        public int filesCount() {
            return this._files.get();
        }

        public long bytesCount() {
            return this._bytes.get();
        }
    }
}
