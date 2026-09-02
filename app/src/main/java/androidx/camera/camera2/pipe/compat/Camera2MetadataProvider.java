package androidx.camera.camera2.pipe.compat;

import androidx.camera.camera2.pipe.CameraExtensionMetadata;
import androidx.camera.camera2.pipe.CameraMetadata;
import java.util.Set;
import kotlin.coroutines.Continuation;

public interface Camera2MetadataProvider {
    /* JADX INFO: renamed from: awaitCameraExtensionMetadata-0r8Bogc */
    CameraExtensionMetadata mo471awaitCameraExtensionMetadata0r8Bogc(String str, int i);

    /* JADX INFO: renamed from: awaitCameraMetadata-EfqyGwQ */
    CameraMetadata mo472awaitCameraMetadataEfqyGwQ(String str);

    /* JADX INFO: renamed from: getCameraMetadata-0r8Bogc */
    Object mo473getCameraMetadata0r8Bogc(String str, Continuation continuation);

    /* JADX INFO: renamed from: getSupportedCameraExtensions-EfqyGwQ */
    Set mo474getSupportedCameraExtensionsEfqyGwQ(String str);
}
