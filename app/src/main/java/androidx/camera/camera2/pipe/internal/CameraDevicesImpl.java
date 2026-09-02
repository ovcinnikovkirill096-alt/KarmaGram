package androidx.camera.camera2.pipe.internal;

import android.os.Trace;
import androidx.camera.camera2.pipe.CameraBackend;
import androidx.camera.camera2.pipe.CameraBackendId;
import androidx.camera.camera2.pipe.CameraBackends;
import androidx.camera.camera2.pipe.CameraDevices;
import androidx.camera.camera2.pipe.CameraId;
import androidx.camera.camera2.pipe.CameraMetadata;
import androidx.camera.camera2.pipe.core.Debug;
import androidx.camera.camera2.pipe.core.Log;
import java.util.List;
import java.util.Set;
import kotlin.jvm.internal.Intrinsics;
import kotlinx.coroutines.flow.Flow;

public final class CameraDevicesImpl implements CameraDevices {
    private final CameraBackends cameraBackends;

    public CameraDevicesImpl(CameraBackends cameraBackends) {
        Intrinsics.checkNotNullParameter(cameraBackends, "cameraBackends");
        this.cameraBackends = cameraBackends;
    }

    @Override // androidx.camera.camera2.pipe.CameraDevices
    /* JADX INFO: renamed from: cameraIdsFlow-SeavPBo */
    public Flow mo176cameraIdsFlowSeavPBo(String str) {
        return m567getCameraBackendSeavPBo(str).getCameraIds();
    }

    @Override // androidx.camera.camera2.pipe.CameraDevices
    /* JADX INFO: renamed from: awaitCameraIds-SeavPBo */
    public List mo173awaitCameraIdsSeavPBo(String str) {
        CameraBackend cameraBackendM567getCameraBackendSeavPBo = m567getCameraBackendSeavPBo(str);
        List listAwaitCameraIds = cameraBackendM567getCameraBackendSeavPBo.awaitCameraIds();
        if (listAwaitCameraIds == null && Log.INSTANCE.getWARN_LOGGABLE()) {
            android.util.Log.w("CXCP", "Failed to load cameraIds from " + ((Object) CameraBackendId.m162toStringimpl(cameraBackendM567getCameraBackendSeavPBo.mo155getIdQwmhuAM())));
        }
        return listAwaitCameraIds;
    }

    @Override // androidx.camera.camera2.pipe.CameraDevices
    /* JADX INFO: renamed from: awaitConcurrentCameraIds-SeavPBo */
    public Set mo175awaitConcurrentCameraIdsSeavPBo(String str) {
        return m567getCameraBackendSeavPBo(str).awaitConcurrentCameraIds();
    }

    @Override // androidx.camera.camera2.pipe.CameraDevices
    /* JADX INFO: renamed from: awaitCameraMetadata-FpsL5FU */
    public CameraMetadata mo174awaitCameraMetadataFpsL5FU(String cameraId, String str) {
        Intrinsics.checkNotNullParameter(cameraId, "cameraId");
        CameraBackend cameraBackendM567getCameraBackendSeavPBo = m567getCameraBackendSeavPBo(str);
        CameraMetadata cameraMetadataMo154awaitCameraMetadataEfqyGwQ = cameraBackendM567getCameraBackendSeavPBo.mo154awaitCameraMetadataEfqyGwQ(cameraId);
        if (cameraMetadataMo154awaitCameraMetadataEfqyGwQ == null && Log.INSTANCE.getWARN_LOGGABLE()) {
            android.util.Log.w("CXCP", "Failed to load metadata for " + ((Object) CameraId.m238toStringimpl(cameraId)) + " from " + ((Object) CameraBackendId.m162toStringimpl(cameraBackendM567getCameraBackendSeavPBo.mo155getIdQwmhuAM())));
        }
        return cameraMetadataMo154awaitCameraMetadataEfqyGwQ;
    }

    /* JADX INFO: renamed from: getCameraBackend-SeavPBo, reason: not valid java name */
    private final CameraBackend m567getCameraBackendSeavPBo(String str) {
        Debug debug = Debug.INSTANCE;
        try {
            Trace.beginSection("getCameraBackend");
            if (str == null) {
                str = this.cameraBackends.getDefault().mo155getIdQwmhuAM();
            }
            CameraBackend cameraBackendMo164getSG3A4s8 = this.cameraBackends.mo164getSG3A4s8(str);
            if (cameraBackendMo164getSG3A4s8 != null) {
                Trace.endSection();
                return cameraBackendMo164getSG3A4s8;
            }
            throw new IllegalStateException(("Failed to load CameraBackend " + ((Object) CameraBackendId.m162toStringimpl(str))).toString());
        } catch (Throwable th) {
            Trace.endSection();
            throw th;
        }
    }
}
