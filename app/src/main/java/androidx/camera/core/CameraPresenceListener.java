package androidx.camera.core;

import java.util.Set;

public interface CameraPresenceListener {
    void onCamerasAdded(Set set);

    void onCamerasRemoved(Set set);
}
