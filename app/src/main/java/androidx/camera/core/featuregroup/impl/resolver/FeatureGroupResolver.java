package androidx.camera.core.featuregroup.impl.resolver;

import androidx.camera.core.SessionConfig;

public interface FeatureGroupResolver {
    FeatureGroupResolutionResult resolveFeatureGroup(SessionConfig sessionConfig);
}
