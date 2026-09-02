package androidx.camera.camera2.pipe.compat;

import android.hardware.camera2.CaptureRequest;
import java.util.List;

public interface CameraConstrainedHighSpeedCaptureSessionWrapper extends CameraCaptureSessionWrapper {
    List createHighSpeedRequestList(CaptureRequest captureRequest);
}
