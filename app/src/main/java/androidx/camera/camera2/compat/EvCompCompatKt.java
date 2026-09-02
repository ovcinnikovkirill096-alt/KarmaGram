package androidx.camera.camera2.compat;

import android.util.Range;

public abstract class EvCompCompatKt {
    private static final Range EMPTY_RANGE = new Range(0, 0);

    public static final Range getEMPTY_RANGE() {
        return EMPTY_RANGE;
    }
}
