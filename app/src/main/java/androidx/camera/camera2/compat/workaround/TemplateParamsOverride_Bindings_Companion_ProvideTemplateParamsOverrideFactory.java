package androidx.camera.camera2.compat.workaround;

import androidx.camera.camera2.compat.quirk.CameraQuirks;
import dagger.internal.Preconditions;
import dagger.internal.Provider;

public abstract class TemplateParamsOverride_Bindings_Companion_ProvideTemplateParamsOverrideFactory implements Provider {
    public static TemplateParamsOverride provideTemplateParamsOverride(CameraQuirks cameraQuirks) {
        return (TemplateParamsOverride) Preconditions.checkNotNullFromProvides(TemplateParamsOverride.Bindings.Companion.provideTemplateParamsOverride(cameraQuirks));
    }
}
