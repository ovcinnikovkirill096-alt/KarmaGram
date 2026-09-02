package androidx.camera.camera2.pipe.compat;

import androidx.camera.camera2.pipe.CameraError;
import androidx.camera.camera2.pipe.CameraId;
import androidx.camera.camera2.pipe.core.DurationNs;
import kotlin.jvm.internal.DefaultConstructorMarker;
import kotlin.jvm.internal.Intrinsics;

public final class CameraStateClosed extends CameraState {
    private final DurationNs cameraActiveDurationNs;
    private final ClosedReason cameraClosedReason;
    private final DurationNs cameraClosingDurationNs;
    private final CameraError cameraErrorCode;
    private final Throwable cameraException;
    private final String cameraId;
    private final DurationNs cameraOpenDurationNs;
    private final Integer cameraRetryCount;
    private final DurationNs cameraRetryDurationNs;

    public /* synthetic */ CameraStateClosed(String str, ClosedReason closedReason, Integer num, DurationNs durationNs, Throwable th, DurationNs durationNs2, DurationNs durationNs3, DurationNs durationNs4, CameraError cameraError, DefaultConstructorMarker defaultConstructorMarker) {
        this(str, closedReason, num, durationNs, th, durationNs2, durationNs3, durationNs4, cameraError);
    }

    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (!(obj instanceof CameraStateClosed)) {
            return false;
        }
        CameraStateClosed cameraStateClosed = (CameraStateClosed) obj;
        return CameraId.m236equalsimpl0(this.cameraId, cameraStateClosed.cameraId) && this.cameraClosedReason == cameraStateClosed.cameraClosedReason && Intrinsics.areEqual(this.cameraRetryCount, cameraStateClosed.cameraRetryCount) && Intrinsics.areEqual(this.cameraRetryDurationNs, cameraStateClosed.cameraRetryDurationNs) && Intrinsics.areEqual(this.cameraException, cameraStateClosed.cameraException) && Intrinsics.areEqual(this.cameraOpenDurationNs, cameraStateClosed.cameraOpenDurationNs) && Intrinsics.areEqual(this.cameraActiveDurationNs, cameraStateClosed.cameraActiveDurationNs) && Intrinsics.areEqual(this.cameraClosingDurationNs, cameraStateClosed.cameraClosingDurationNs) && Intrinsics.areEqual(this.cameraErrorCode, cameraStateClosed.cameraErrorCode);
    }

    public int hashCode() {
        int iM237hashCodeimpl = ((CameraId.m237hashCodeimpl(this.cameraId) * 31) + this.cameraClosedReason.hashCode()) * 31;
        Integer num = this.cameraRetryCount;
        int iHashCode = (iM237hashCodeimpl + (num == null ? 0 : num.hashCode())) * 31;
        DurationNs durationNs = this.cameraRetryDurationNs;
        int iM515hashCodeimpl = (iHashCode + (durationNs == null ? 0 : DurationNs.m515hashCodeimpl(durationNs.m517unboximpl()))) * 31;
        Throwable th = this.cameraException;
        int iHashCode2 = (iM515hashCodeimpl + (th == null ? 0 : th.hashCode())) * 31;
        DurationNs durationNs2 = this.cameraOpenDurationNs;
        int iM515hashCodeimpl2 = (iHashCode2 + (durationNs2 == null ? 0 : DurationNs.m515hashCodeimpl(durationNs2.m517unboximpl()))) * 31;
        DurationNs durationNs3 = this.cameraActiveDurationNs;
        int iM515hashCodeimpl3 = (iM515hashCodeimpl2 + (durationNs3 == null ? 0 : DurationNs.m515hashCodeimpl(durationNs3.m517unboximpl()))) * 31;
        DurationNs durationNs4 = this.cameraClosingDurationNs;
        int iM515hashCodeimpl4 = (iM515hashCodeimpl3 + (durationNs4 == null ? 0 : DurationNs.m515hashCodeimpl(durationNs4.m517unboximpl()))) * 31;
        CameraError cameraError = this.cameraErrorCode;
        return iM515hashCodeimpl4 + (cameraError != null ? CameraError.m185hashCodeimpl(cameraError.m188unboximpl()) : 0);
    }

    public String toString() {
        return "CameraStateClosed(cameraId=" + ((Object) CameraId.m238toStringimpl(this.cameraId)) + ", cameraClosedReason=" + this.cameraClosedReason + ", cameraRetryCount=" + this.cameraRetryCount + ", cameraRetryDurationNs=" + this.cameraRetryDurationNs + ", cameraException=" + this.cameraException + ", cameraOpenDurationNs=" + this.cameraOpenDurationNs + ", cameraActiveDurationNs=" + this.cameraActiveDurationNs + ", cameraClosingDurationNs=" + this.cameraClosingDurationNs + ", cameraErrorCode=" + this.cameraErrorCode + ')';
    }

    /* JADX WARN: 'super' call moved to the top of the method (can break code semantics) */
    private CameraStateClosed(String cameraId, ClosedReason cameraClosedReason, Integer num, DurationNs durationNs, Throwable th, DurationNs durationNs2, DurationNs durationNs3, DurationNs durationNs4, CameraError cameraError) {
        super(null);
        Intrinsics.checkNotNullParameter(cameraId, "cameraId");
        Intrinsics.checkNotNullParameter(cameraClosedReason, "cameraClosedReason");
        this.cameraId = cameraId;
        this.cameraClosedReason = cameraClosedReason;
        this.cameraRetryCount = num;
        this.cameraRetryDurationNs = durationNs;
        this.cameraException = th;
        this.cameraOpenDurationNs = durationNs2;
        this.cameraActiveDurationNs = durationNs3;
        this.cameraClosingDurationNs = durationNs4;
        this.cameraErrorCode = cameraError;
    }

    public /* synthetic */ CameraStateClosed(String str, ClosedReason closedReason, Integer num, DurationNs durationNs, Throwable th, DurationNs durationNs2, DurationNs durationNs3, DurationNs durationNs4, CameraError cameraError, int i, DefaultConstructorMarker defaultConstructorMarker) {
        this(str, closedReason, (i & 4) != 0 ? null : num, (i & 8) != 0 ? null : durationNs, (i & 16) != 0 ? null : th, (i & 32) != 0 ? null : durationNs2, (i & 64) != 0 ? null : durationNs3, (i & 128) != 0 ? null : durationNs4, (i & 256) != 0 ? null : cameraError, null);
    }

    /* JADX INFO: renamed from: getCameraErrorCode-mVEW8x0, reason: not valid java name */
    public final CameraError m478getCameraErrorCodemVEW8x0() {
        return this.cameraErrorCode;
    }
}
