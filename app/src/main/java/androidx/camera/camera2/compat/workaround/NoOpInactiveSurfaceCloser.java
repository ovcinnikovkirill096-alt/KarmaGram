package androidx.camera.camera2.compat.workaround;

import androidx.camera.camera2.pipe.CameraGraph;
import androidx.camera.core.impl.DeferrableSurface;
import kotlin.jvm.internal.Intrinsics;

public final class NoOpInactiveSurfaceCloser implements InactiveSurfaceCloser {
    public static final NoOpInactiveSurfaceCloser INSTANCE = new NoOpInactiveSurfaceCloser();

    @Override // androidx.camera.camera2.compat.workaround.InactiveSurfaceCloser
    public void closeAll() {
    }

    @Override // androidx.camera.camera2.compat.workaround.InactiveSurfaceCloser
    /* JADX INFO: renamed from: configure-hB7JTeY */
    public void mo45configurehB7JTeY(int i, DeferrableSurface deferrableSurface, CameraGraph graph) {
        Intrinsics.checkNotNullParameter(deferrableSurface, "deferrableSurface");
        Intrinsics.checkNotNullParameter(graph, "graph");
    }

    @Override // androidx.camera.camera2.compat.workaround.InactiveSurfaceCloser
    public void onSurfaceInactive(DeferrableSurface deferrableSurface) {
        Intrinsics.checkNotNullParameter(deferrableSurface, "deferrableSurface");
    }

    private NoOpInactiveSurfaceCloser() {
    }
}
