package androidx.camera.camera2.pipe;

import androidx.camera.camera2.pipe.graph.GraphListener;
import java.util.List;
import java.util.Set;
import kotlin.coroutines.Continuation;
import kotlinx.coroutines.Deferred;
import kotlinx.coroutines.flow.Flow;

public interface CameraBackend {
    List awaitCameraIds();

    /* JADX INFO: renamed from: awaitCameraMetadata-EfqyGwQ, reason: not valid java name */
    CameraMetadata mo154awaitCameraMetadataEfqyGwQ(String str);

    Set awaitConcurrentCameraIds();

    CameraController createCameraController(CameraContext cameraContext, CameraGraphId cameraGraphId, CameraGraph.Config config, GraphListener graphListener, StreamGraph streamGraph, SurfaceTracker surfaceTracker);

    Flow getCameraIds();

    /* JADX INFO: renamed from: getId-QwmhuAM, reason: not valid java name */
    String mo155getIdQwmhuAM();

    /* JADX INFO: renamed from: isConfigSupported-NpXggIU, reason: not valid java name */
    Object mo156isConfigSupportedNpXggIU(CameraGraph.Config config, Continuation continuation);

    Deferred shutdownAsync();
}
