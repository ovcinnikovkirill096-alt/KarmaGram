package androidx.room.util;

import android.os.Build;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.channels.Channels;
import java.nio.channels.FileChannel;
import java.nio.channels.ReadableByteChannel;
import kotlin.jvm.internal.Intrinsics;

public abstract class FileUtil {
    public static final void copy(ReadableByteChannel input, FileChannel output) throws Throwable {
        ReadableByteChannel readableByteChannel;
        FileChannel fileChannel;
        Intrinsics.checkNotNullParameter(input, "input");
        Intrinsics.checkNotNullParameter(output, "output");
        try {
            try {
                if (Build.VERSION.SDK_INT > 23) {
                    readableByteChannel = input;
                    fileChannel = output;
                    fileChannel.transferFrom(readableByteChannel, 0L, Long.MAX_VALUE);
                } else {
                    readableByteChannel = input;
                    fileChannel = output;
                    InputStream inputStreamNewInputStream = Channels.newInputStream(readableByteChannel);
                    OutputStream outputStreamNewOutputStream = Channels.newOutputStream(fileChannel);
                    byte[] bArr = new byte[4096];
                    while (true) {
                        int i = inputStreamNewInputStream.read(bArr);
                        if (i <= 0) {
                            break;
                        } else {
                            outputStreamNewOutputStream.write(bArr, 0, i);
                        }
                    }
                }
                fileChannel.force(false);
                readableByteChannel.close();
                fileChannel.close();
            } catch (Throwable th) {
                th = th;
                Throwable th2 = th;
                readableByteChannel.close();
                fileChannel.close();
                throw th2;
            }
        } catch (Throwable th3) {
            th = th3;
            readableByteChannel = input;
            fileChannel = output;
        }
    }
}
