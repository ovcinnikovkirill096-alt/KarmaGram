package androidx.camera.camera2.pipe.config;

import androidx.camera.camera2.pipe.CameraBackend;
import androidx.camera.camera2.pipe.CameraBackendFactory;
import androidx.camera.camera2.pipe.CameraBackendId;
import androidx.camera.camera2.pipe.CameraBackends;
import androidx.camera.camera2.pipe.CameraContext;
import androidx.camera.camera2.pipe.CameraController;
import androidx.camera.camera2.pipe.CameraGraph;
import androidx.camera.camera2.pipe.CameraGraphId;
import androidx.camera.camera2.pipe.CameraId;
import androidx.camera.camera2.pipe.CameraMetadata;
import androidx.camera.camera2.pipe.StreamGraph;
import androidx.camera.camera2.pipe.SurfaceTracker;
import androidx.camera.camera2.pipe.graph.GraphProcessorImpl;
import kotlin.jvm.internal.DefaultConstructorMarker;
import kotlin.jvm.internal.Intrinsics;

public abstract class InternalCameraGraphModules {
    public static final Companion Companion = new Companion(null);

    public static final class Companion {
        public /* synthetic */ Companion(DefaultConstructorMarker defaultConstructorMarker) {
            this();
        }

        private Companion() {
        }

        public final CameraBackend provideCameraBackend(CameraBackends cameraBackends, CameraGraph.Config graphConfig, CameraContext cameraContext) {
            Intrinsics.checkNotNullParameter(cameraBackends, "cameraBackends");
            Intrinsics.checkNotNullParameter(graphConfig, "graphConfig");
            Intrinsics.checkNotNullParameter(cameraContext, "cameraContext");
            CameraBackendFactory customCameraBackend = graphConfig.getCustomCameraBackend();
            if (customCameraBackend != null) {
                return customCameraBackend.create(cameraContext);
            }
            String strM207getCameraBackendIdAKmI2lo = graphConfig.m207getCameraBackendIdAKmI2lo();
            if (strM207getCameraBackendIdAKmI2lo != null) {
                CameraBackend cameraBackendMo164getSG3A4s8 = cameraBackends.mo164getSG3A4s8(strM207getCameraBackendIdAKmI2lo);
                if (cameraBackendMo164getSG3A4s8 != null) {
                    return cameraBackendMo164getSG3A4s8;
                }
                throw new IllegalStateException(("Failed to initialize " + ((Object) CameraBackendId.m162toStringimpl(strM207getCameraBackendIdAKmI2lo)) + " from " + graphConfig).toString());
            }
            return cameraBackends.getDefault();
        }

        public final CameraMetadata provideCameraMetadata(CameraGraph.Config graphConfig, CameraBackend cameraBackend) {
            Intrinsics.checkNotNullParameter(graphConfig, "graphConfig");
            Intrinsics.checkNotNullParameter(cameraBackend, "cameraBackend");
            CameraMetadata cameraMetadataMo154awaitCameraMetadataEfqyGwQ = cameraBackend.mo154awaitCameraMetadataEfqyGwQ(graphConfig.m206getCameraDz_R5H8());
            if (cameraMetadataMo154awaitCameraMetadataEfqyGwQ != null) {
                return cameraMetadataMo154awaitCameraMetadataEfqyGwQ;
            }
            throw new IllegalStateException(("Failed to load metadata for " + ((Object) CameraId.m238toStringimpl(graphConfig.m206getCameraDz_R5H8())) + '!').toString());
        }

        public final CameraController provideCameraController(CameraGraphId graphId, CameraGraph.Config graphConfig, CameraBackend cameraBackend, CameraContext cameraContext, GraphProcessorImpl graphProcessor, StreamGraph streamGraph, SurfaceTracker surfaceTracker) {
            Intrinsics.checkNotNullParameter(graphId, "graphId");
            Intrinsics.checkNotNullParameter(graphConfig, "graphConfig");
            Intrinsics.checkNotNullParameter(cameraBackend, "cameraBackend");
            Intrinsics.checkNotNullParameter(cameraContext, "cameraContext");
            Intrinsics.checkNotNullParameter(graphProcessor, "graphProcessor");
            Intrinsics.checkNotNullParameter(streamGraph, "streamGraph");
            Intrinsics.checkNotNullParameter(surfaceTracker, "surfaceTracker");
            return cameraBackend.createCameraController(cameraContext, graphId, graphConfig, graphProcessor, streamGraph, surfaceTracker);
        }
    }
}
