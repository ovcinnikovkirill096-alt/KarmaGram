package androidx.camera.camera2.pipe;

import kotlin.jvm.internal.DefaultConstructorMarker;
import kotlin.jvm.internal.Intrinsics;

public final class Result3A {
    private final FrameMetadata frameMetadata;
    private final int status;

    public /* synthetic */ Result3A(int i, FrameMetadata frameMetadata, DefaultConstructorMarker defaultConstructorMarker) {
        this(i, frameMetadata);
    }

    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (!(obj instanceof Result3A)) {
            return false;
        }
        Result3A result3A = (Result3A) obj;
        return Status.m394equalsimpl0(this.status, result3A.status) && Intrinsics.areEqual(this.frameMetadata, result3A.frameMetadata);
    }

    public int hashCode() {
        int iM395hashCodeimpl = Status.m395hashCodeimpl(this.status) * 31;
        FrameMetadata frameMetadata = this.frameMetadata;
        return iM395hashCodeimpl + (frameMetadata == null ? 0 : frameMetadata.hashCode());
    }

    public String toString() {
        return "Result3A(status=" + ((Object) Status.m396toStringimpl(this.status)) + ", frameMetadata=" + this.frameMetadata + ')';
    }

    private Result3A(int i, FrameMetadata frameMetadata) {
        this.status = i;
        this.frameMetadata = frameMetadata;
    }

    public /* synthetic */ Result3A(int i, FrameMetadata frameMetadata, int i2, DefaultConstructorMarker defaultConstructorMarker) {
        this(i, (i2 & 2) != 0 ? null : frameMetadata, null);
    }

    public final FrameMetadata getFrameMetadata() {
        return this.frameMetadata;
    }

    /* JADX INFO: renamed from: getStatus-JvTi9ms, reason: not valid java name */
    public final int m392getStatusJvTi9ms() {
        return this.status;
    }

    public static final class Status {
        public static final Companion Companion = new Companion(null);
        private static final int OK = m393constructorimpl(0);
        private static final int FRAME_LIMIT_REACHED = m393constructorimpl(1);
        private static final int TIME_LIMIT_REACHED = m393constructorimpl(2);
        private static final int SUBMIT_CANCELLED = m393constructorimpl(3);
        private static final int SUBMIT_FAILED = m393constructorimpl(4);

        /* JADX INFO: renamed from: constructor-impl, reason: not valid java name */
        private static int m393constructorimpl(int i) {
            return i;
        }

        /* JADX INFO: renamed from: equals-impl0, reason: not valid java name */
        public static final boolean m394equalsimpl0(int i, int i2) {
            return i == i2;
        }

        /* JADX INFO: renamed from: hashCode-impl, reason: not valid java name */
        public static int m395hashCodeimpl(int i) {
            return i;
        }

        /* JADX INFO: renamed from: toString-impl, reason: not valid java name */
        public static String m396toStringimpl(int i) {
            return "Status(value=" + i + ')';
        }

        public static final class Companion {
            public /* synthetic */ Companion(DefaultConstructorMarker defaultConstructorMarker) {
                this();
            }

            private Companion() {
            }

            /* JADX INFO: renamed from: getOK-JvTi9ms, reason: not valid java name */
            public final int m398getOKJvTi9ms() {
                return Status.OK;
            }

            /* JADX INFO: renamed from: getFRAME_LIMIT_REACHED-JvTi9ms, reason: not valid java name */
            public final int m397getFRAME_LIMIT_REACHEDJvTi9ms() {
                return Status.FRAME_LIMIT_REACHED;
            }

            /* JADX INFO: renamed from: getTIME_LIMIT_REACHED-JvTi9ms, reason: not valid java name */
            public final int m401getTIME_LIMIT_REACHEDJvTi9ms() {
                return Status.TIME_LIMIT_REACHED;
            }

            /* JADX INFO: renamed from: getSUBMIT_CANCELLED-JvTi9ms, reason: not valid java name */
            public final int m399getSUBMIT_CANCELLEDJvTi9ms() {
                return Status.SUBMIT_CANCELLED;
            }

            /* JADX INFO: renamed from: getSUBMIT_FAILED-JvTi9ms, reason: not valid java name */
            public final int m400getSUBMIT_FAILEDJvTi9ms() {
                return Status.SUBMIT_FAILED;
            }
        }
    }
}
