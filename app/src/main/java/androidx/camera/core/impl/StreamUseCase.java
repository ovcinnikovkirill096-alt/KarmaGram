package androidx.camera.core.impl;

import kotlin.enums.EnumEntries;
import kotlin.enums.EnumEntriesKt;

public enum StreamUseCase {
    DEFAULT(0),
    PREVIEW(1),
    VIDEO_RECORD(3),
    STILL_CAPTURE(2),
    VIDEO_CALL(5),
    PREVIEW_VIDEO_STILL(4),
    CROPPED_RAW(6);

    private static final /* synthetic */ EnumEntries $ENTRIES = EnumEntriesKt.enumEntries(values());
    private final long value;

    StreamUseCase(int i) {
        this.value = i;
    }

    public final long getValue() {
        return this.value;
    }
}
