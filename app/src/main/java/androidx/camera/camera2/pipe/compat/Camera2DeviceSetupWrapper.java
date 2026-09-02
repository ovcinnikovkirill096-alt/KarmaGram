package androidx.camera.camera2.pipe.compat;

import android.hardware.camera2.CaptureRequest;

public interface Camera2DeviceSetupWrapper {
    CaptureRequest.Builder createCaptureRequest(int i);
}
