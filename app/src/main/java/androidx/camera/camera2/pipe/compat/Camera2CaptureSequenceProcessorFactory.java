package androidx.camera.camera2.pipe.compat;

import androidx.camera.camera2.pipe.CaptureSequenceProcessor;
import java.util.Map;

public interface Camera2CaptureSequenceProcessorFactory {
    CaptureSequenceProcessor create(CameraCaptureSessionWrapper cameraCaptureSessionWrapper, Map map, Map map2);
}
