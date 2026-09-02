package androidx.camera.camera2.pipe;

import java.util.List;
import java.util.Set;
import kotlinx.coroutines.flow.Flow;

public interface CameraDevices {
    /* JADX INFO: renamed from: awaitCameraIds-SeavPBo, reason: not valid java name */
    List mo173awaitCameraIdsSeavPBo(String str);

    /* JADX INFO: renamed from: awaitCameraMetadata-FpsL5FU, reason: not valid java name */
    CameraMetadata mo174awaitCameraMetadataFpsL5FU(String str, String str2);

    /* JADX INFO: renamed from: awaitConcurrentCameraIds-SeavPBo, reason: not valid java name */
    Set mo175awaitConcurrentCameraIdsSeavPBo(String str);

    /* JADX INFO: renamed from: cameraIdsFlow-SeavPBo, reason: not valid java name */
    Flow mo176cameraIdsFlowSeavPBo(String str);

    /* JADX INFO: renamed from: androidx.camera.camera2.pipe.CameraDevices$-CC, reason: invalid class name */
    public abstract /* synthetic */ class CC {
        /* JADX INFO: renamed from: cameraIdsFlow-SeavPBo$default, reason: not valid java name */
        public static /* synthetic */ Flow m180cameraIdsFlowSeavPBo$default(CameraDevices cameraDevices, String str, int i, Object obj) {
            if (obj != null) {
                throw new UnsupportedOperationException("Super calls with default arguments not supported in this target, function: cameraIdsFlow-SeavPBo");
            }
            if ((i & 1) != 0) {
                str = null;
            }
            return cameraDevices.mo176cameraIdsFlowSeavPBo(str);
        }

        /* JADX INFO: renamed from: awaitCameraIds-SeavPBo$default, reason: not valid java name */
        public static /* synthetic */ List m177awaitCameraIdsSeavPBo$default(CameraDevices cameraDevices, String str, int i, Object obj) {
            if (obj != null) {
                throw new UnsupportedOperationException("Super calls with default arguments not supported in this target, function: awaitCameraIds-SeavPBo");
            }
            if ((i & 1) != 0) {
                str = null;
            }
            return cameraDevices.mo173awaitCameraIdsSeavPBo(str);
        }

        /* JADX INFO: renamed from: awaitConcurrentCameraIds-SeavPBo$default, reason: not valid java name */
        public static /* synthetic */ Set m179awaitConcurrentCameraIdsSeavPBo$default(CameraDevices cameraDevices, String str, int i, Object obj) {
            if (obj != null) {
                throw new UnsupportedOperationException("Super calls with default arguments not supported in this target, function: awaitConcurrentCameraIds-SeavPBo");
            }
            if ((i & 1) != 0) {
                str = null;
            }
            return cameraDevices.mo175awaitConcurrentCameraIdsSeavPBo(str);
        }

        /* JADX INFO: renamed from: awaitCameraMetadata-FpsL5FU$default, reason: not valid java name */
        public static /* synthetic */ CameraMetadata m178awaitCameraMetadataFpsL5FU$default(CameraDevices cameraDevices, String str, String str2, int i, Object obj) {
            if (obj != null) {
                throw new UnsupportedOperationException("Super calls with default arguments not supported in this target, function: awaitCameraMetadata-FpsL5FU");
            }
            if ((i & 2) != 0) {
                str2 = null;
            }
            return cameraDevices.mo174awaitCameraMetadataFpsL5FU(str, str2);
        }
    }
}
