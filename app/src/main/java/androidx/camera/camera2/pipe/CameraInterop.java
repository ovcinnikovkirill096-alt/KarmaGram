package androidx.camera.camera2.pipe;

import kotlinx.atomicfu.AtomicFU;
import kotlinx.atomicfu.AtomicInt;

public final class CameraInterop {
    public static final CameraInterop INSTANCE = new CameraInterop();
    private static final AtomicInt captureSessionIds = AtomicFU.atomic(0);

    public static final class CameraCaptureSessionId {
        /* JADX INFO: renamed from: constructor-impl, reason: not valid java name */
        public static int m241constructorimpl(int i) {
            return i;
        }
    }

    public interface CaptureSessionListener {
        /* JADX INFO: renamed from: onActive-rphkYDA */
        void mo64onActiverphkYDA(String str, int i);

        /* JADX INFO: renamed from: onCaptureQueueEmpty-rphkYDA */
        void mo65onCaptureQueueEmptyrphkYDA(String str, int i);

        /* JADX INFO: renamed from: onClosed-rphkYDA */
        void mo66onClosedrphkYDA(String str, int i);

        /* JADX INFO: renamed from: onConfigureFailed-rphkYDA */
        void mo67onConfigureFailedrphkYDA(String str, int i);

        /* JADX INFO: renamed from: onConfigured-rphkYDA */
        void mo68onConfiguredrphkYDA(String str, int i);

        /* JADX INFO: renamed from: onReady-rphkYDA */
        void mo69onReadyrphkYDA(String str, int i);
    }

    private CameraInterop() {
    }
}
