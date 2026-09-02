package androidx.camera.camera2.pipe.compat;

import androidx.camera.camera2.pipe.CameraId;
import androidx.camera.camera2.pipe.GraphState;
import androidx.camera.camera2.pipe.internal.CameraErrorListener;
import java.util.LinkedHashMap;
import java.util.Map;
import kotlin.Unit;
import kotlin.jvm.internal.Intrinsics;

public final class Camera2ErrorProcessor implements CameraErrorListener {
    private final Object lock = new Object();
    private final Map virtualCameraStateMap = new LinkedHashMap();

    @Override // androidx.camera.camera2.pipe.internal.CameraErrorListener
    /* JADX INFO: renamed from: onCameraError-3M5Xam4, reason: not valid java name */
    public void mo463onCameraError3M5Xam4(String cameraId, int i, boolean z) {
        VirtualCameraState virtualCameraState;
        Intrinsics.checkNotNullParameter(cameraId, "cameraId");
        synchronized (this.lock) {
            virtualCameraState = (VirtualCameraState) this.virtualCameraStateMap.get(CameraId.m233boximpl(cameraId));
        }
        if (virtualCameraState == null) {
            return;
        }
        virtualCameraState.getGraphListener().onGraphError(new GraphState.GraphStateError(i, z, null));
    }

    /* JADX INFO: renamed from: setActiveVirtualCamera-0r8Bogc$camera_camera2_pipe, reason: not valid java name */
    public final void m464setActiveVirtualCamera0r8Bogc$camera_camera2_pipe(String cameraId, VirtualCameraState virtualCameraState) {
        Intrinsics.checkNotNullParameter(cameraId, "cameraId");
        Intrinsics.checkNotNullParameter(virtualCameraState, "virtualCameraState");
        synchronized (this.lock) {
            this.virtualCameraStateMap.put(CameraId.m233boximpl(cameraId), virtualCameraState);
            Unit unit = Unit.INSTANCE;
        }
    }
}
