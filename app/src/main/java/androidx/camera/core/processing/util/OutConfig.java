package androidx.camera.core.processing.util;

import android.graphics.Rect;
import android.util.Size;
import androidx.camera.core.impl.utils.TransformUtils;
import androidx.camera.core.processing.SurfaceEdge;
import java.util.UUID;

public abstract class OutConfig {
    public abstract Rect getCropRect();

    public abstract int getFormat();

    public abstract int getRotationDegrees();

    public abstract Size getSize();

    public abstract int getTargets();

    abstract UUID getUuid();

    public abstract boolean isMirroring();

    public abstract boolean shouldRespectInputCropRect();

    public static OutConfig of(SurfaceEdge surfaceEdge) {
        return of(surfaceEdge.getTargets(), surfaceEdge.getFormat(), surfaceEdge.getCropRect(), TransformUtils.getRotatedSize(surfaceEdge.getCropRect(), surfaceEdge.getRotationDegrees()), surfaceEdge.getRotationDegrees(), surfaceEdge.isMirroring());
    }

    public static OutConfig of(int i, int i2, Rect rect, Size size, int i3, boolean z) {
        return of(i, i2, rect, size, i3, z, false);
    }

    public static OutConfig of(int i, int i2, Rect rect, Size size, int i3, boolean z, boolean z2) {
        return new AutoValue_OutConfig(UUID.randomUUID(), i, i2, rect, size, i3, z, z2);
    }
}
