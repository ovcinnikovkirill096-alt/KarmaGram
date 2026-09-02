package androidx.camera.camera2.pipe.compat;

import android.view.Surface;
import androidx.camera.camera2.pipe.UnsafeWrapper;

public interface OutputConfigurationWrapper extends UnsafeWrapper {
    void addSurface(Surface surface);
}
