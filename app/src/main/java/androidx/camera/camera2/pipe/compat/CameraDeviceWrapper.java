package androidx.camera.camera2.pipe.compat;

import android.hardware.camera2.CaptureRequest;
import android.hardware.camera2.TotalCaptureResult;
import android.hardware.camera2.params.InputConfiguration;
import androidx.camera.camera2.pipe.UnsafeWrapper;
import java.util.List;

public interface CameraDeviceWrapper extends UnsafeWrapper, AudioRestrictionController.Listener {
    /* JADX INFO: renamed from: createCaptureRequest-2PPcXtw */
    CaptureRequest.Builder mo427createCaptureRequest2PPcXtw(int i);

    boolean createCaptureSession(SessionConfigData sessionConfigData);

    boolean createCaptureSession(List list, CameraCaptureSessionWrapper.StateCallback stateCallback);

    boolean createCaptureSessionByOutputConfigurations(List list, CameraCaptureSessionWrapper.StateCallback stateCallback);

    boolean createConstrainedHighSpeedCaptureSession(List list, CameraCaptureSessionWrapper.StateCallback stateCallback);

    boolean createExtensionSession(ExtensionSessionConfigData extensionSessionConfigData);

    CaptureRequest.Builder createReprocessCaptureRequest(TotalCaptureResult totalCaptureResult);

    boolean createReprocessableCaptureSession(InputConfiguration inputConfiguration, List list, CameraCaptureSessionWrapper.StateCallback stateCallback);

    boolean createReprocessableCaptureSessionByConfigurations(InputConfigData inputConfigData, List list, CameraCaptureSessionWrapper.StateCallback stateCallback);

    /* JADX INFO: renamed from: getCameraId-Dz_R5H8 */
    String mo428getCameraIdDz_R5H8();

    void onDeviceClosed();

    void onDeviceClosing();
}
