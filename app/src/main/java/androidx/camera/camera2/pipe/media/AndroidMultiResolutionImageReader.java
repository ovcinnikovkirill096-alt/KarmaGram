package androidx.camera.camera2.pipe.media;

import android.media.ImageReader;
import androidx.camera.camera2.pipe.UnsafeWrapper;
import java.util.List;

public abstract class AndroidMultiResolutionImageReader implements UnsafeWrapper, AutoCloseable, ImageReader.OnImageAvailableListener {
    public abstract List getOutputConfigurations$camera_camera2_pipe();
}
