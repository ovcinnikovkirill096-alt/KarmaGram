package androidx.camera.camera2.pipe.compat;

import kotlin.enums.EnumEntries;
import kotlin.enums.EnumEntriesKt;

public enum ClosedReason {
    APP_CLOSED,
    APP_DISCONNECTED,
    CAMERA2_CLOSED,
    CAMERA2_DISCONNECTED,
    CAMERA2_ERROR,
    CAMERA2_EXCEPTION;

    private static final /* synthetic */ EnumEntries $ENTRIES = EnumEntriesKt.enumEntries(values());
}
