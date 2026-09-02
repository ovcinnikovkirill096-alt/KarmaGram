package androidx.camera.camera2.pipe.config;

import androidx.camera.camera2.pipe.core.Threads;
import dagger.internal.Preconditions;
import dagger.internal.Provider;
import kotlinx.coroutines.CoroutineScope;
import kotlinx.coroutines.Job;

public abstract class SharedCameraGraphModules_Companion_ProvideCameraGraphCoroutineScopeFactory implements Provider {
    public static CoroutineScope provideCameraGraphCoroutineScope(Threads threads, Job job) {
        return (CoroutineScope) Preconditions.checkNotNullFromProvides(SharedCameraGraphModules.Companion.provideCameraGraphCoroutineScope(threads, job));
    }
}
