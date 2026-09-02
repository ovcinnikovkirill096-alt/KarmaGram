package org.telegram.tgnet;

public interface ResultCallback {
    void onComplete(Object obj);

    void onError(TLRPC.TL_error tL_error);

    /* JADX INFO: renamed from: org.telegram.tgnet.ResultCallback$-CC, reason: invalid class name */
    public abstract /* synthetic */ class CC {
        public static void $default$onError(ResultCallback resultCallback, TLRPC.TL_error tL_error) {
        }

        public static void $default$onError(ResultCallback resultCallback, Throwable th) {
        }
    }
}
