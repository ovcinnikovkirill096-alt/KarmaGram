package androidx.camera.camera2.pipe.internal;

import androidx.camera.camera2.pipe.OutputStatus;
import kotlin.jvm.internal.DefaultConstructorMarker;
import kotlin.jvm.internal.Intrinsics;

public final class OutputResult {
    public static final Companion Companion = new Companion(null);
    private final Object result;

    /* JADX INFO: renamed from: box-impl, reason: not valid java name */
    public static final /* synthetic */ OutputResult m587boximpl(Object obj) {
        return new OutputResult(obj);
    }

    /* JADX INFO: Access modifiers changed from: private */
    /* JADX INFO: renamed from: constructor-impl, reason: not valid java name */
    public static Object m588constructorimpl(Object obj) {
        return obj;
    }

    /* JADX INFO: renamed from: equals-impl, reason: not valid java name */
    public static boolean m589equalsimpl(Object obj, Object obj2) {
        return (obj2 instanceof OutputResult) && Intrinsics.areEqual(obj, ((OutputResult) obj2).m594unboximpl());
    }

    /* JADX INFO: renamed from: hashCode-impl, reason: not valid java name */
    public static int m592hashCodeimpl(Object obj) {
        if (obj == null) {
            return 0;
        }
        return obj.hashCode();
    }

    /* JADX INFO: renamed from: toString-impl, reason: not valid java name */
    public static String m593toStringimpl(Object obj) {
        return "OutputResult(result=" + obj + ')';
    }

    public boolean equals(Object obj) {
        return m589equalsimpl(this.result, obj);
    }

    public int hashCode() {
        return m592hashCodeimpl(this.result);
    }

    public String toString() {
        return m593toStringimpl(this.result);
    }

    /* JADX INFO: renamed from: unbox-impl, reason: not valid java name */
    public final /* synthetic */ Object m594unboximpl() {
        return this.result;
    }

    private /* synthetic */ OutputResult(Object obj) {
        this.result = obj;
    }

    /* JADX INFO: renamed from: getAvailable-impl, reason: not valid java name */
    public static final boolean m590getAvailableimpl(Object obj) {
        return (m591getFailureimpl(obj) || obj == null) ? false : true;
    }

    /* JADX INFO: renamed from: getFailure-impl, reason: not valid java name */
    public static final boolean m591getFailureimpl(Object obj) {
        return obj instanceof OutputStatus;
    }

    public static final class Companion {
        public /* synthetic */ Companion(DefaultConstructorMarker defaultConstructorMarker) {
            this();
        }

        private Companion() {
        }
    }
}
