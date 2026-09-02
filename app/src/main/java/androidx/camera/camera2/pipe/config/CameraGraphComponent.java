package androidx.camera.camera2.pipe.config;

import androidx.camera.camera2.pipe.CameraGraph;

public interface CameraGraphComponent {

    public interface Builder {
        CameraGraphComponent build();

        Builder cameraGraphConfigModule(CameraGraphConfigModule cameraGraphConfigModule);
    }

    CameraGraph cameraGraph();
}
