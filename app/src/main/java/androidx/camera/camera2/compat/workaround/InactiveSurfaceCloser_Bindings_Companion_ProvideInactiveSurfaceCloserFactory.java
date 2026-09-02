package androidx.camera.camera2.compat.workaround;

import androidx.camera.camera2.compat.quirk.CameraQuirks;
import dagger.internal.Preconditions;
import dagger.internal.Provider;

public abstract class InactiveSurfaceCloser_Bindings_Companion_ProvideInactiveSurfaceCloserFactory implements Provider {
    public static InactiveSurfaceCloser provideInactiveSurfaceCloser(CameraQuirks cameraQuirks) {
        return (InactiveSurfaceCloser) Preconditions.checkNotNullFromProvides(InactiveSurfaceCloser.Bindings.Companion.provideInactiveSurfaceCloser(cameraQuirks));
    }
}
