package org.telegram.messenger.pip.activity;

public interface IPipActivityListener {
    void onCompleteEnterToPip();

    void onCompleteExitFromPip(boolean z);

    void onStartEnterToPip();

    void onStartExitFromPip(boolean z);

    /* JADX INFO: renamed from: org.telegram.messenger.pip.activity.IPipActivityListener$-CC, reason: invalid class name */
    public abstract /* synthetic */ class CC {
        public static void $default$onStartEnterToPip(IPipActivityListener iPipActivityListener) {
        }

        public static void $default$onCompleteEnterToPip(IPipActivityListener iPipActivityListener) {
        }

        public static void $default$onStartExitFromPip(IPipActivityListener iPipActivityListener, boolean z) {
        }

        public static void $default$onCompleteExitFromPip(IPipActivityListener iPipActivityListener, boolean z) {
        }
    }
}
