package androidx.camera.camera2.pipe.compat;

import androidx.camera.camera2.pipe.CameraError;
import kotlinx.coroutines.flow.Flow;

public interface VirtualCamera {
    /* JADX INFO: renamed from: disconnect-TPqeGZw, reason: not valid java name */
    void mo500disconnectTPqeGZw(CameraError cameraError);

    Flow getState();

    /* JADX INFO: renamed from: androidx.camera.camera2.pipe.compat.VirtualCamera$-CC, reason: invalid class name */
    public abstract /* synthetic */ class CC {
        /* JADX INFO: renamed from: disconnect-TPqeGZw$default, reason: not valid java name */
        public static /* synthetic */ void m501disconnectTPqeGZw$default(VirtualCamera virtualCamera, CameraError cameraError, int i, Object obj) {
            if (obj != null) {
                throw new UnsupportedOperationException("Super calls with default arguments not supported in this target, function: disconnect-TPqeGZw");
            }
            if ((i & 1) != 0) {
                cameraError = null;
            }
            virtualCamera.mo500disconnectTPqeGZw(cameraError);
        }
    }
}
