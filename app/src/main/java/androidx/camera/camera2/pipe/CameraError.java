package androidx.camera.camera2.pipe;

import android.hardware.camera2.CameraAccessException;
import android.os.Build;
import androidx.camera.camera2.pipe.core.Log;
import kotlin.jvm.internal.DefaultConstructorMarker;
import kotlin.jvm.internal.Intrinsics;
import org.telegram.messenger.voip.Instance;

public final class CameraError {
    private final int value;
    public static final Companion Companion = new Companion(null);
    private static final int ERROR_UNDETERMINED = m182constructorimpl(0);
    private static final int ERROR_CAMERA_IN_USE = m182constructorimpl(1);
    private static final int ERROR_CAMERA_LIMIT_EXCEEDED = m182constructorimpl(2);
    private static final int ERROR_CAMERA_DISABLED = m182constructorimpl(3);
    private static final int ERROR_CAMERA_DEVICE = m182constructorimpl(4);
    private static final int ERROR_CAMERA_SERVICE = m182constructorimpl(5);
    private static final int ERROR_CAMERA_DISCONNECTED = m182constructorimpl(6);
    private static final int ERROR_ILLEGAL_ARGUMENT_EXCEPTION = m182constructorimpl(7);
    private static final int ERROR_SECURITY_EXCEPTION = m182constructorimpl(8);
    private static final int ERROR_GRAPH_CONFIG = m182constructorimpl(9);
    private static final int ERROR_DO_NOT_DISTURB_ENABLED = m182constructorimpl(10);
    private static final int ERROR_UNKNOWN_EXCEPTION = m182constructorimpl(11);
    private static final int ERROR_CAMERA_OPENER = m182constructorimpl(12);
    private static final int ERROR_CAMERA_OPEN_TIMEOUT = m182constructorimpl(13);

    /* JADX INFO: renamed from: box-impl, reason: not valid java name */
    public static final /* synthetic */ CameraError m181boximpl(int i) {
        return new CameraError(i);
    }

    /* JADX INFO: renamed from: constructor-impl, reason: not valid java name */
    private static int m182constructorimpl(int i) {
        return i;
    }

    /* JADX INFO: renamed from: equals-impl, reason: not valid java name */
    public static boolean m183equalsimpl(int i, Object obj) {
        return (obj instanceof CameraError) && i == ((CameraError) obj).m188unboximpl();
    }

    /* JADX INFO: renamed from: equals-impl0, reason: not valid java name */
    public static final boolean m184equalsimpl0(int i, int i2) {
        return i == i2;
    }

    /* JADX INFO: renamed from: hashCode-impl, reason: not valid java name */
    public static int m185hashCodeimpl(int i) {
        return i;
    }

    public boolean equals(Object obj) {
        return m183equalsimpl(this.value, obj);
    }

    public int hashCode() {
        return m185hashCodeimpl(this.value);
    }

    /* JADX INFO: renamed from: unbox-impl, reason: not valid java name */
    public final /* synthetic */ int m188unboximpl() {
        return this.value;
    }

    private /* synthetic */ CameraError(int i) {
        this.value = i;
    }

    /* JADX INFO: renamed from: isDisconnected-impl, reason: not valid java name */
    public static final boolean m186isDisconnectedimpl(int i) {
        return m184equalsimpl0(i, ERROR_CAMERA_DISCONNECTED) || m184equalsimpl0(i, ERROR_CAMERA_IN_USE) || m184equalsimpl0(i, ERROR_CAMERA_LIMIT_EXCEEDED);
    }

    public static final class Companion {
        public /* synthetic */ Companion(DefaultConstructorMarker defaultConstructorMarker) {
            this();
        }

        private Companion() {
        }

        /* JADX INFO: renamed from: getERROR_UNDETERMINED-v7Vf74A, reason: not valid java name */
        public final int m204getERROR_UNDETERMINEDv7Vf74A() {
            return CameraError.ERROR_UNDETERMINED;
        }

        /* JADX INFO: renamed from: getERROR_CAMERA_IN_USE-v7Vf74A, reason: not valid java name */
        public final int m195getERROR_CAMERA_IN_USEv7Vf74A() {
            return CameraError.ERROR_CAMERA_IN_USE;
        }

        /* JADX INFO: renamed from: getERROR_CAMERA_LIMIT_EXCEEDED-v7Vf74A, reason: not valid java name */
        public final int m196getERROR_CAMERA_LIMIT_EXCEEDEDv7Vf74A() {
            return CameraError.ERROR_CAMERA_LIMIT_EXCEEDED;
        }

        /* JADX INFO: renamed from: getERROR_CAMERA_DISABLED-v7Vf74A, reason: not valid java name */
        public final int m193getERROR_CAMERA_DISABLEDv7Vf74A() {
            return CameraError.ERROR_CAMERA_DISABLED;
        }

        /* JADX INFO: renamed from: getERROR_CAMERA_DEVICE-v7Vf74A, reason: not valid java name */
        public final int m192getERROR_CAMERA_DEVICEv7Vf74A() {
            return CameraError.ERROR_CAMERA_DEVICE;
        }

        /* JADX INFO: renamed from: getERROR_CAMERA_SERVICE-v7Vf74A, reason: not valid java name */
        public final int m199getERROR_CAMERA_SERVICEv7Vf74A() {
            return CameraError.ERROR_CAMERA_SERVICE;
        }

        /* JADX INFO: renamed from: getERROR_CAMERA_DISCONNECTED-v7Vf74A, reason: not valid java name */
        public final int m194getERROR_CAMERA_DISCONNECTEDv7Vf74A() {
            return CameraError.ERROR_CAMERA_DISCONNECTED;
        }

        /* JADX INFO: renamed from: getERROR_ILLEGAL_ARGUMENT_EXCEPTION-v7Vf74A, reason: not valid java name */
        public final int m202getERROR_ILLEGAL_ARGUMENT_EXCEPTIONv7Vf74A() {
            return CameraError.ERROR_ILLEGAL_ARGUMENT_EXCEPTION;
        }

        /* JADX INFO: renamed from: getERROR_SECURITY_EXCEPTION-v7Vf74A, reason: not valid java name */
        public final int m203getERROR_SECURITY_EXCEPTIONv7Vf74A() {
            return CameraError.ERROR_SECURITY_EXCEPTION;
        }

        /* JADX INFO: renamed from: getERROR_GRAPH_CONFIG-v7Vf74A, reason: not valid java name */
        public final int m201getERROR_GRAPH_CONFIGv7Vf74A() {
            return CameraError.ERROR_GRAPH_CONFIG;
        }

        /* JADX INFO: renamed from: getERROR_DO_NOT_DISTURB_ENABLED-v7Vf74A, reason: not valid java name */
        public final int m200getERROR_DO_NOT_DISTURB_ENABLEDv7Vf74A() {
            return CameraError.ERROR_DO_NOT_DISTURB_ENABLED;
        }

        /* JADX INFO: renamed from: getERROR_UNKNOWN_EXCEPTION-v7Vf74A, reason: not valid java name */
        public final int m205getERROR_UNKNOWN_EXCEPTIONv7Vf74A() {
            return CameraError.ERROR_UNKNOWN_EXCEPTION;
        }

        /* JADX INFO: renamed from: getERROR_CAMERA_OPENER-v7Vf74A, reason: not valid java name */
        public final int m197getERROR_CAMERA_OPENERv7Vf74A() {
            return CameraError.ERROR_CAMERA_OPENER;
        }

        /* JADX INFO: renamed from: getERROR_CAMERA_OPEN_TIMEOUT-v7Vf74A, reason: not valid java name */
        public final int m198getERROR_CAMERA_OPEN_TIMEOUTv7Vf74A() {
            return CameraError.ERROR_CAMERA_OPEN_TIMEOUT;
        }

        /* JADX INFO: renamed from: from-PVuDhNw$camera_camera2_pipe, reason: not valid java name */
        public final int m191fromPVuDhNw$camera_camera2_pipe(Throwable throwable) {
            Intrinsics.checkNotNullParameter(throwable, "throwable");
            if (throwable instanceof CameraAccessException) {
                return m190fromPVuDhNw$camera_camera2_pipe((CameraAccessException) throwable);
            }
            if (throwable instanceof IllegalArgumentException) {
                return m202getERROR_ILLEGAL_ARGUMENT_EXCEPTIONv7Vf74A();
            }
            if (throwable instanceof SecurityException) {
                return m203getERROR_SECURITY_EXCEPTIONv7Vf74A();
            }
            if (!shouldHandleDoNotDisturbException$camera_camera2_pipe(throwable)) {
                if (Log.INSTANCE.getWARN_LOGGABLE()) {
                    android.util.Log.w("CXCP", "Unexpected throwable: " + throwable);
                }
                return m205getERROR_UNKNOWN_EXCEPTIONv7Vf74A();
            }
            return m200getERROR_DO_NOT_DISTURB_ENABLEDv7Vf74A();
        }

        /* JADX INFO: renamed from: from-PVuDhNw$camera_camera2_pipe, reason: not valid java name */
        public final int m190fromPVuDhNw$camera_camera2_pipe(CameraAccessException exception) {
            Intrinsics.checkNotNullParameter(exception, "exception");
            int reason = exception.getReason();
            if (reason == 1) {
                return m193getERROR_CAMERA_DISABLEDv7Vf74A();
            }
            if (reason == 2) {
                return m194getERROR_CAMERA_DISCONNECTEDv7Vf74A();
            }
            if (reason == 3) {
                return m204getERROR_UNDETERMINEDv7Vf74A();
            }
            if (reason == 4) {
                return m195getERROR_CAMERA_IN_USEv7Vf74A();
            }
            if (reason != 5) {
                if (Log.INSTANCE.getWARN_LOGGABLE()) {
                    android.util.Log.w("CXCP", "Unexpected CameraAccessException: " + exception);
                }
                return m205getERROR_UNKNOWN_EXCEPTIONv7Vf74A();
            }
            return m196getERROR_CAMERA_LIMIT_EXCEEDEDv7Vf74A();
        }

        /* JADX INFO: renamed from: from-PVuDhNw$camera_camera2_pipe, reason: not valid java name */
        public final int m189fromPVuDhNw$camera_camera2_pipe(int i) {
            if (i == 1) {
                return m195getERROR_CAMERA_IN_USEv7Vf74A();
            }
            if (i == 2) {
                return m196getERROR_CAMERA_LIMIT_EXCEEDEDv7Vf74A();
            }
            if (i == 3) {
                return m193getERROR_CAMERA_DISABLEDv7Vf74A();
            }
            if (i == 4) {
                return m192getERROR_CAMERA_DEVICEv7Vf74A();
            }
            if (i == 5) {
                return m199getERROR_CAMERA_SERVICEv7Vf74A();
            }
            throw new IllegalArgumentException("Unexpected StateCallback error code: " + i);
        }

        public final boolean shouldHandleDoNotDisturbException$camera_camera2_pipe(Throwable throwable) {
            Intrinsics.checkNotNullParameter(throwable, "throwable");
            return Build.VERSION.SDK_INT == 28 && isDoNotDisturbException(throwable);
        }

        private final boolean isDoNotDisturbException(Throwable th) {
            if (!(th instanceof RuntimeException)) {
                return false;
            }
            StackTraceElement[] stackTrace = ((RuntimeException) th).getStackTrace();
            Intrinsics.checkNotNull(stackTrace);
            return Intrinsics.areEqual(!(stackTrace.length == 0) ? stackTrace[0].getMethodName() : null, "_enableShutterSound");
        }
    }

    public String toString() {
        return m187toStringimpl(this.value);
    }

    /* JADX INFO: renamed from: toString-impl, reason: not valid java name */
    public static String m187toStringimpl(int i) {
        String str;
        StringBuilder sb = new StringBuilder();
        sb.append("CameraError(");
        if (m184equalsimpl0(i, ERROR_UNDETERMINED)) {
            str = "ERROR_UNDETERMINED";
        } else if (m184equalsimpl0(i, ERROR_CAMERA_IN_USE)) {
            str = "ERROR_CAMERA_IN_USE";
        } else if (m184equalsimpl0(i, ERROR_CAMERA_LIMIT_EXCEEDED)) {
            str = "ERROR_CAMERA_LIMIT_EXCEEDED";
        } else if (m184equalsimpl0(i, ERROR_CAMERA_DISABLED)) {
            str = "ERROR_CAMERA_DISABLED";
        } else if (m184equalsimpl0(i, ERROR_CAMERA_DEVICE)) {
            str = "ERROR_CAMERA_DEVICE";
        } else if (m184equalsimpl0(i, ERROR_CAMERA_SERVICE)) {
            str = "ERROR_CAMERA_SERVICE";
        } else if (m184equalsimpl0(i, ERROR_CAMERA_DISCONNECTED)) {
            str = "ERROR_CAMERA_DISCONNECTED";
        } else if (m184equalsimpl0(i, ERROR_ILLEGAL_ARGUMENT_EXCEPTION)) {
            str = "ERROR_ILLEGAL_ARGUMENT_EXCEPTION";
        } else if (m184equalsimpl0(i, ERROR_SECURITY_EXCEPTION)) {
            str = "ERROR_SECURITY_EXCEPTION";
        } else if (m184equalsimpl0(i, ERROR_GRAPH_CONFIG)) {
            str = "ERROR_GRAPH_CONFIG";
        } else if (m184equalsimpl0(i, ERROR_DO_NOT_DISTURB_ENABLED)) {
            str = "ERROR_DO_NOT_DISTURB_ENABLED";
        } else if (m184equalsimpl0(i, ERROR_UNKNOWN_EXCEPTION)) {
            str = "ERROR_UNKNOWN_EXCEPTION";
        } else if (m184equalsimpl0(i, ERROR_CAMERA_OPENER)) {
            str = "ERROR_CAMERA_OPENER";
        } else {
            str = m184equalsimpl0(i, ERROR_CAMERA_OPEN_TIMEOUT) ? "ERROR_CAMERA_OPEN_TIMEOUT" : Instance.ERROR_UNKNOWN;
        }
        sb.append(str);
        sb.append(')');
        return sb.toString();
    }
}
