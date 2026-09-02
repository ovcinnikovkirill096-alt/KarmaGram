package androidx.camera.camera2.pipe.compat;

import android.hardware.camera2.CameraExtensionSession$StateCallback;
import android.hardware.camera2.params.ExtensionSessionConfiguration;
import java.util.List;
import java.util.concurrent.Executor;

public abstract /* synthetic */ class Api31Compat$$ExternalSyntheticApiModelOutline0 {
    public static /* synthetic */ ExtensionSessionConfiguration m(int i, List list, Executor executor, CameraExtensionSession$StateCallback cameraExtensionSession$StateCallback) {
        return new ExtensionSessionConfiguration(i, list, executor, cameraExtensionSession$StateCallback);
    }
}
