package androidx.camera.camera2.compat.workaround;

import androidx.camera.camera2.compat.quirk.CameraQuirks;
import dagger.internal.Preconditions;
import dagger.internal.Provider;

public abstract class UseFlashModeTorchFor3aUpdate_Bindings_Companion_ProvideUseFlashModeTorchFor3aUpdateFactory implements Provider {
    public static UseFlashModeTorchFor3aUpdate provideUseFlashModeTorchFor3aUpdate(CameraQuirks cameraQuirks) {
        return (UseFlashModeTorchFor3aUpdate) Preconditions.checkNotNullFromProvides(UseFlashModeTorchFor3aUpdate.Bindings.Companion.provideUseFlashModeTorchFor3aUpdate(cameraQuirks));
    }
}
