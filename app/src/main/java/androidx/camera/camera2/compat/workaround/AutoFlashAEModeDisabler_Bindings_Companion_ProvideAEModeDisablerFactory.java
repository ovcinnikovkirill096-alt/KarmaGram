package androidx.camera.camera2.compat.workaround;

import androidx.camera.camera2.compat.quirk.CameraQuirks;
import dagger.internal.Preconditions;
import dagger.internal.Provider;

public abstract class AutoFlashAEModeDisabler_Bindings_Companion_ProvideAEModeDisablerFactory implements Provider {
    public static AutoFlashAEModeDisabler provideAEModeDisabler(CameraQuirks cameraQuirks) {
        return (AutoFlashAEModeDisabler) Preconditions.checkNotNullFromProvides(AutoFlashAEModeDisabler.Bindings.Companion.provideAEModeDisabler(cameraQuirks));
    }
}
