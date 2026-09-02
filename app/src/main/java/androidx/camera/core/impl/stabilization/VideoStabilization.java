package androidx.camera.core.impl.stabilization;

import kotlin.enums.EnumEntries;
import kotlin.enums.EnumEntriesKt;
import kotlin.jvm.internal.DefaultConstructorMarker;

public enum VideoStabilization {
    UNSPECIFIED,
    OFF,
    ON,
    PREVIEW;

    private static final /* synthetic */ EnumEntries $ENTRIES = EnumEntriesKt.enumEntries(values());
    public static final Companion Companion = new Companion(null);

    public static final class Companion {
        public /* synthetic */ Companion(DefaultConstructorMarker defaultConstructorMarker) {
            this();
        }

        private Companion() {
        }

        public final VideoStabilization from$camera_core(int i, int i2) {
            if (i == 1 || i2 == 1) {
                return VideoStabilization.OFF;
            }
            if (i == 2) {
                return VideoStabilization.PREVIEW;
            }
            if (i2 == 2) {
                return VideoStabilization.ON;
            }
            return VideoStabilization.UNSPECIFIED;
        }
    }
}
