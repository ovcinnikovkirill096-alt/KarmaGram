package androidx.camera.camera2.compat.workaround;

import androidx.camera.camera2.compat.quirk.CameraQuirks;
import dagger.internal.Preconditions;
import dagger.internal.Provider;

public abstract class MeteringRegionCorrection_Bindings_Companion_ProvideMeteringRegionCorrectionFactory implements Provider {
    public static MeteringRegionCorrection provideMeteringRegionCorrection(CameraQuirks cameraQuirks) {
        return (MeteringRegionCorrection) Preconditions.checkNotNullFromProvides(MeteringRegionCorrection.Bindings.Companion.provideMeteringRegionCorrection(cameraQuirks));
    }
}
