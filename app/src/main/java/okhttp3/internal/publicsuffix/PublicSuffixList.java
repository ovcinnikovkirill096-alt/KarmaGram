package okhttp3.internal.publicsuffix;

import okio.ByteString;

public interface PublicSuffixList {
    public static final Companion Companion = Companion.$$INSTANCE;

    void ensureLoaded();

    ByteString getBytes();

    ByteString getExceptionBytes();

    public static final class Companion {
        static final /* synthetic */ Companion $$INSTANCE = new Companion();

        private Companion() {
        }
    }
}
