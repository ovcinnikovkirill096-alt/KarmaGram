package androidx.camera.core.featuregroup.impl.feature;

import kotlin.enums.EnumEntries;
import kotlin.enums.EnumEntriesKt;

public enum FeatureTypeInternal {
    DYNAMIC_RANGE,
    FPS_RANGE,
    VIDEO_STABILIZATION,
    IMAGE_FORMAT,
    RECORDING_QUALITY;

    private static final /* synthetic */ EnumEntries $ENTRIES = EnumEntriesKt.enumEntries(values());

    public static EnumEntries getEntries() {
        return $ENTRIES;
    }
}
