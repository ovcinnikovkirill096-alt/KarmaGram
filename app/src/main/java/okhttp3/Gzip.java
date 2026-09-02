package okhttp3;

import kotlin.jvm.internal.Intrinsics;
import okio.BufferedSource;
import okio.GzipSource;
import okio.Source;

public final class Gzip implements CompressionInterceptor.DecompressionAlgorithm {
    public static final Gzip INSTANCE = new Gzip();

    private Gzip() {
    }

    @Override // okhttp3.CompressionInterceptor.DecompressionAlgorithm
    public String getEncoding() {
        return "gzip";
    }

    @Override // okhttp3.CompressionInterceptor.DecompressionAlgorithm
    public Source decompress(BufferedSource compressedSource) {
        Intrinsics.checkNotNullParameter(compressedSource, "compressedSource");
        return new GzipSource(compressedSource);
    }
}
