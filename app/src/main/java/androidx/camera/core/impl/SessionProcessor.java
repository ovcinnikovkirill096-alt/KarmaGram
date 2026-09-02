package androidx.camera.core.impl;

import android.util.Pair;
import java.util.Set;

public interface SessionProcessor {
    Pair getImplementationType();

    Set getSupportedCameraOperations();
}
