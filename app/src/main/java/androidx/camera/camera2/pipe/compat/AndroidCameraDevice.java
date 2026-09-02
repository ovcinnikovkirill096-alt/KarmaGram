package androidx.camera.camera2.pipe.compat;

import android.hardware.camera2.CameraAccessException;
import android.hardware.camera2.CameraDevice;
import android.hardware.camera2.CaptureRequest;
import android.hardware.camera2.TotalCaptureResult;
import android.hardware.camera2.params.InputConfiguration;
import android.os.Trace;
import androidx.camera.camera2.pipe.CameraError;
import androidx.camera.camera2.pipe.CameraId;
import androidx.camera.camera2.pipe.CameraInterop;
import androidx.camera.camera2.pipe.CameraMetadata;
import androidx.camera.camera2.pipe.core.Debug;
import androidx.camera.camera2.pipe.core.DurationNs;
import androidx.camera.camera2.pipe.core.Log;
import androidx.camera.camera2.pipe.core.Threads;
import androidx.camera.camera2.pipe.core.Timestamps;
import androidx.camera.camera2.pipe.internal.CameraErrorListener;
import java.util.Arrays;
import java.util.List;
import kotlin.Pair;
import kotlin.Unit;
import kotlin.jvm.internal.DefaultConstructorMarker;
import kotlin.jvm.internal.Intrinsics;
import kotlin.jvm.internal.Reflection;
import kotlin.reflect.KClass;
import kotlinx.atomicfu.AtomicBoolean;
import kotlinx.atomicfu.AtomicFU;
import kotlinx.atomicfu.AtomicRef;
import org.mvel2.asm.signature.SignatureVisitor;

public final class AndroidCameraDevice implements CameraDeviceWrapper {
    private final AtomicRef _lastStateCallback;
    private final CameraDevice cameraDevice;
    private final CameraErrorListener cameraErrorListener;
    private final String cameraId;
    private final CameraMetadata cameraMetadata;
    private final AtomicBoolean closed;
    private final CameraInterop.CaptureSessionListener interopCaptureSessionListener;
    private final Threads threads;

    public /* synthetic */ AndroidCameraDevice(CameraMetadata cameraMetadata, CameraDevice cameraDevice, String str, CameraErrorListener cameraErrorListener, CameraInterop.CaptureSessionListener captureSessionListener, Threads threads, DefaultConstructorMarker defaultConstructorMarker) {
        this(cameraMetadata, cameraDevice, str, cameraErrorListener, captureSessionListener, threads);
    }

    private AndroidCameraDevice(CameraMetadata cameraMetadata, CameraDevice cameraDevice, String cameraId, CameraErrorListener cameraErrorListener, CameraInterop.CaptureSessionListener captureSessionListener, Threads threads) {
        Intrinsics.checkNotNullParameter(cameraMetadata, "cameraMetadata");
        Intrinsics.checkNotNullParameter(cameraDevice, "cameraDevice");
        Intrinsics.checkNotNullParameter(cameraId, "cameraId");
        Intrinsics.checkNotNullParameter(cameraErrorListener, "cameraErrorListener");
        Intrinsics.checkNotNullParameter(threads, "threads");
        this.cameraMetadata = cameraMetadata;
        this.cameraDevice = cameraDevice;
        this.cameraId = cameraId;
        this.cameraErrorListener = cameraErrorListener;
        this.interopCaptureSessionListener = captureSessionListener;
        this.threads = threads;
        this.closed = AtomicFU.atomic(false);
        this._lastStateCallback = AtomicFU.atomic((Object) null);
    }

    @Override // androidx.camera.camera2.pipe.compat.CameraDeviceWrapper
    /* JADX INFO: renamed from: getCameraId-Dz_R5H8, reason: not valid java name */
    public String mo428getCameraIdDz_R5H8() {
        return this.cameraId;
    }

    /* JADX WARN: Multi-variable type inference failed */
    /* JADX WARN: Type inference failed for: r13v3, types: [boolean] */
    /* JADX WARN: Type inference failed for: r9v0, types: [java.lang.String] */
    /* JADX WARN: Type inference failed for: r9v1 */
    /* JADX WARN: Type inference failed for: r9v12 */
    /* JADX WARN: Type inference failed for: r9v13 */
    /* JADX WARN: Type inference failed for: r9v14 */
    /* JADX WARN: Type inference failed for: r9v2, types: [int] */
    /* JADX WARN: Type inference failed for: r9v3 */
    /* JADX WARN: Type inference failed for: r9v4 */
    /* JADX WARN: Type inference failed for: r9v5 */
    /* JADX WARN: Type inference failed for: r9v8, types: [int] */
    /* JADX WARN: Type inference failed for: r9v9 */
    /*  JADX ERROR: JadxRuntimeException in pass: CodeShrinkVisitor
        jadx.core.utils.exceptions.JadxRuntimeException: Can't change immutable type int to ?? for r9v8 ??
        	at jadx.core.dex.instructions.args.SSAVar.setType(SSAVar.java:114)
        	at jadx.core.dex.instructions.args.RegisterArg.setType(RegisterArg.java:52)
        	at jadx.core.dex.instructions.args.InsnArg.wrapInstruction(InsnArg.java:139)
        	at jadx.core.dex.visitors.shrink.CodeShrinkVisitor.inline(CodeShrinkVisitor.java:212)
        	at jadx.core.dex.visitors.shrink.CodeShrinkVisitor.checkInline(CodeShrinkVisitor.java:143)
        	at jadx.core.dex.visitors.shrink.CodeShrinkVisitor.shrinkBlock(CodeShrinkVisitor.java:68)
        	at jadx.core.dex.visitors.shrink.CodeShrinkVisitor.shrinkMethod(CodeShrinkVisitor.java:48)
        	at jadx.core.dex.visitors.shrink.CodeShrinkVisitor.visit(CodeShrinkVisitor.java:39)
        */
    @Override // androidx.camera.camera2.pipe.compat.CameraDeviceWrapper
    public boolean createCaptureSession(java.util.List r29, androidx.camera.camera2.pipe.compat.CameraCaptureSessionWrapper.StateCallback r30) {
        /*
            Method dump skipped, instruction units count: 600
            To view this dump add '--comments-level debug' option
        */
        throw new UnsupportedOperationException("Method not decompiled: androidx.camera.camera2.pipe.compat.AndroidCameraDevice.createCaptureSession(java.util.List, androidx.camera.camera2.pipe.compat.CameraCaptureSessionWrapper$StateCallback):boolean");
    }

    /* JADX WARN: Multi-variable type inference failed */
    /* JADX WARN: Type inference failed for: r13v7, types: [boolean] */
    /* JADX WARN: Type inference failed for: r23v10 */
    /* JADX WARN: Type inference failed for: r23v11 */
    /* JADX WARN: Type inference failed for: r23v12 */
    /* JADX WARN: Type inference failed for: r23v5 */
    /* JADX WARN: Type inference failed for: r23v6 */
    /* JADX WARN: Type inference failed for: r6v1, types: [java.lang.StringBuilder] */
    /* JADX WARN: Type inference failed for: r7v2, types: [java.lang.StringBuilder] */
    /* JADX WARN: Type inference failed for: r8v2 */
    /* JADX WARN: Type inference failed for: r8v3, types: [java.lang.String] */
    /* JADX WARN: Type inference failed for: r8v5, types: [java.lang.String] */
    /* JADX WARN: Type inference failed for: r9v0, types: [java.lang.String] */
    /* JADX WARN: Type inference failed for: r9v1 */
    /* JADX WARN: Type inference failed for: r9v10, types: [java.lang.String] */
    /* JADX WARN: Type inference failed for: r9v11 */
    /* JADX WARN: Type inference failed for: r9v12 */
    /* JADX WARN: Type inference failed for: r9v13, types: [java.lang.String] */
    /* JADX WARN: Type inference failed for: r9v2, types: [int] */
    /* JADX WARN: Type inference failed for: r9v3 */
    /* JADX WARN: Type inference failed for: r9v4 */
    /* JADX WARN: Type inference failed for: r9v7, types: [int] */
    /* JADX WARN: Type inference failed for: r9v9 */
    /*  JADX ERROR: JadxRuntimeException in pass: CodeShrinkVisitor
        jadx.core.utils.exceptions.JadxRuntimeException: Can't change immutable type int to ?? for r9v7 ??
        	at jadx.core.dex.instructions.args.SSAVar.setType(SSAVar.java:114)
        	at jadx.core.dex.instructions.args.RegisterArg.setType(RegisterArg.java:52)
        	at jadx.core.dex.instructions.args.InsnArg.wrapInstruction(InsnArg.java:139)
        	at jadx.core.dex.visitors.shrink.CodeShrinkVisitor.inline(CodeShrinkVisitor.java:212)
        	at jadx.core.dex.visitors.shrink.CodeShrinkVisitor.checkInline(CodeShrinkVisitor.java:143)
        	at jadx.core.dex.visitors.shrink.CodeShrinkVisitor.shrinkBlock(CodeShrinkVisitor.java:68)
        	at jadx.core.dex.visitors.shrink.CodeShrinkVisitor.shrinkMethod(CodeShrinkVisitor.java:48)
        	at jadx.core.dex.visitors.shrink.CodeShrinkVisitor.visit(CodeShrinkVisitor.java:39)
        */
    @Override // androidx.camera.camera2.pipe.compat.CameraDeviceWrapper
    public boolean createExtensionSession(androidx.camera.camera2.pipe.compat.ExtensionSessionConfigData r28) {
        /*
            Method dump skipped, instruction units count: 736
            To view this dump add '--comments-level debug' option
        */
        throw new UnsupportedOperationException("Method not decompiled: androidx.camera.camera2.pipe.compat.AndroidCameraDevice.createExtensionSession(androidx.camera.camera2.pipe.compat.ExtensionSessionConfigData):boolean");
    }

    /* JADX WARN: Code duplicated, block: B:33:0x00f4 A[Catch: all -> 0x00bb, TryCatch #5 {all -> 0x00bb, blocks: (B:15:0x00a9, B:31:0x00f0, B:33:0x00f4, B:35:0x00fc, B:36:0x0114, B:38:0x0121, B:40:0x0125, B:42:0x0129, B:44:0x012d, B:47:0x0132, B:49:0x0136, B:51:0x013e, B:52:0x0144, B:53:0x0145, B:55:0x014d, B:56:0x0165), top: B:84:0x00a9 }] */
    /* JADX WARN: Code duplicated, block: B:35:0x00fc A[Catch: all -> 0x00bb, TryCatch #5 {all -> 0x00bb, blocks: (B:15:0x00a9, B:31:0x00f0, B:33:0x00f4, B:35:0x00fc, B:36:0x0114, B:38:0x0121, B:40:0x0125, B:42:0x0129, B:44:0x012d, B:47:0x0132, B:49:0x0136, B:51:0x013e, B:52:0x0144, B:53:0x0145, B:55:0x014d, B:56:0x0165), top: B:84:0x00a9 }] */
    /* JADX WARN: Code duplicated, block: B:38:0x0121 A[Catch: all -> 0x00bb, TryCatch #5 {all -> 0x00bb, blocks: (B:15:0x00a9, B:31:0x00f0, B:33:0x00f4, B:35:0x00fc, B:36:0x0114, B:38:0x0121, B:40:0x0125, B:42:0x0129, B:44:0x012d, B:47:0x0132, B:49:0x0136, B:51:0x013e, B:52:0x0144, B:53:0x0145, B:55:0x014d, B:56:0x0165), top: B:84:0x00a9 }] */
    /* JADX WARN: Code duplicated, block: B:53:0x0145 A[Catch: all -> 0x00bb, TryCatch #5 {all -> 0x00bb, blocks: (B:15:0x00a9, B:31:0x00f0, B:33:0x00f4, B:35:0x00fc, B:36:0x0114, B:38:0x0121, B:40:0x0125, B:42:0x0129, B:44:0x012d, B:47:0x0132, B:49:0x0136, B:51:0x013e, B:52:0x0144, B:53:0x0145, B:55:0x014d, B:56:0x0165), top: B:84:0x00a9 }] */
    /* JADX WARN: Code duplicated, block: B:55:0x014d A[Catch: all -> 0x00bb, TryCatch #5 {all -> 0x00bb, blocks: (B:15:0x00a9, B:31:0x00f0, B:33:0x00f4, B:35:0x00fc, B:36:0x0114, B:38:0x0121, B:40:0x0125, B:42:0x0129, B:44:0x012d, B:47:0x0132, B:49:0x0136, B:51:0x013e, B:52:0x0144, B:53:0x0145, B:55:0x014d, B:56:0x0165), top: B:84:0x00a9 }] */
    /* JADX WARN: Code duplicated, block: B:60:0x0189  */
    /* JADX WARN: Code duplicated, block: B:62:0x01d4  */
    /* JADX WARN: Code duplicated, block: B:64:0x01da  */
    /* JADX WARN: Code duplicated, block: B:66:0x01f9  */
    /* JADX WARN: Code duplicated, block: B:68:0x01fe  */
    /* JADX WARN: Code duplicated, block: B:69:0x0200 A[ORIG_RETURN, RETURN] */
    /* JADX WARN: Code duplicated, block: B:78:0x0229  */
    /* JADX WARN: Instruction removed from duplicated block: B:35:0x00fc, please report this as an issue */
    /* JADX WARN: Instruction removed from duplicated block: B:55:0x014d, please report this as an issue */
    /* JADX WARN: Instruction removed from duplicated block: B:60:0x0189, please report this as an issue */
    /* JADX WARN: Instruction removed from duplicated block: B:64:0x01da, please report this as an issue */
    /* JADX WARN: Instruction removed from duplicated block: B:78:0x0229, please report this as an issue */
    /* JADX WARN: Multi-variable type inference failed */
    @Override // androidx.camera.camera2.pipe.compat.CameraDeviceWrapper
    public boolean createReprocessableCaptureSession(InputConfiguration input, List outputs, CameraCaptureSessionWrapper.StateCallback stateCallback) throws Throwable {
        Debug debug;
        int i;
        int i2;
        String str;
        String str2;
        long jM513constructorimpl;
        SessionStateCallback sessionStateCallback;
        boolean z;
        Unit unit;
        int i3;
        long jM513constructorimpl2;
        Log log;
        String str3 = "format(...)";
        String str4 = "f ms";
        String str5 = "%.";
        Intrinsics.checkNotNullParameter(input, "input");
        Intrinsics.checkNotNullParameter(outputs, "outputs");
        Intrinsics.checkNotNullParameter(stateCallback, "stateCallback");
        Pair pairCheckAndSetStateCallback = checkAndSetStateCallback(stateCallback);
        boolean zBooleanValue = ((Boolean) pairCheckAndSetStateCallback.component1()).booleanValue();
        SessionStateCallback sessionStateCallback2 = (SessionStateCallback) pairCheckAndSetStateCallback.component2();
        if (!zBooleanValue) {
            return false;
        }
        if (sessionStateCallback2 != null) {
            onSessionDisconnectedWithTrace(sessionStateCallback2);
        }
        Debug debug2 = Debug.INSTANCE;
        String str6 = "CXCP#createReprocessableCaptureSession" + SignatureVisitor.SUPER + mo428getCameraIdDz_R5H8();
        long jMo521nowvQl9yQU = debug2.getSystemTimeSource$camera_camera2_pipe().mo521nowvQl9yQU();
        try {
            Trace.beginSection(str6);
            String strMo428getCameraIdDz_R5H8 = mo428getCameraIdDz_R5H8();
            debug = debug2;
            try {
                CameraErrorListener cameraErrorListener = this.cameraErrorListener;
                try {
                    sessionStateCallback = sessionStateCallback2;
                    try {
                        str4 = "f ms";
                        str3 = "format(...)";
                        str2 = str6;
                        str5 = "%.";
                        z = true;
                        try {
                            try {
                                this.cameraDevice.createReprocessableCaptureSession(input, outputs, new AndroidCaptureSessionStateCallback(this, stateCallback, sessionStateCallback, this.cameraErrorListener, this.interopCaptureSessionListener, this.threads.getCamera2Handler()), this.threads.getCamera2Handler());
                                unit = Unit.INSTANCE;
                                i3 = z;
                            } catch (Exception e) {
                                e = e;
                                if (e instanceof CameraAccessException) {
                                    if (Log.INSTANCE.getWARN_LOGGABLE()) {
                                        android.util.Log.w("CXCP", "Failed to execute call: Camera encountered an error: " + e.getMessage());
                                    }
                                    cameraErrorListener.mo463onCameraError3M5Xam4(strMo428getCameraIdDz_R5H8, CameraError.Companion.m190fromPVuDhNw$camera_camera2_pipe((CameraAccessException) e), z);
                                } else if (!(e instanceof IllegalArgumentException) || (e instanceof SecurityException) || (e instanceof UnsupportedOperationException) || (e instanceof NullPointerException)) {
                                    if (Log.INSTANCE.getWARN_LOGGABLE()) {
                                        android.util.Log.w("CXCP", "Failed to execute call: Unexpected exception: " + e.getMessage());
                                    }
                                    cameraErrorListener.mo463onCameraError3M5Xam4(strMo428getCameraIdDz_R5H8, CameraError.Companion.m201getERROR_GRAPH_CONFIGv7Vf74A(), false);
                                } else {
                                    if (!(e instanceof IllegalStateException)) {
                                        throw e;
                                    }
                                    if (Log.INSTANCE.getDEBUG_LOGGABLE()) {
                                        android.util.Log.d("CXCP", "Failed to execute call: Camera may be closed");
                                    }
                                }
                                unit = null;
                                i3 = z;
                            }
                        } catch (Throwable th) {
                            th = th;
                            str4 = str4;
                            str5 = str5;
                            str = str3;
                            i = 3;
                            i2 = z;
                            Trace.endSection();
                            jM513constructorimpl = DurationNs.m513constructorimpl(debug.getSystemTimeSource$camera_camera2_pipe().mo521nowvQl9yQU() - jMo521nowvQl9yQU);
                            if (Log.INSTANCE.getDEBUG_LOGGABLE()) {
                                StringBuilder sb = new StringBuilder();
                                sb.append(str2);
                                sb.append(" - ");
                                Timestamps timestamps = Timestamps.INSTANCE;
                                Object[] objArr = new Object[i2];
                                objArr[0] = Double.valueOf(jM513constructorimpl / 1000000.0d);
                                String str7 = String.format(null, str5 + i + str4, Arrays.copyOf(objArr, i2));
                                Intrinsics.checkNotNullExpressionValue(str7, str);
                                sb.append(str7);
                                android.util.Log.d("CXCP", sb.toString());
                            }
                            throw th;
                        }
                    } catch (Exception e2) {
                        e = e2;
                        str2 = str6;
                        z = true;
                        if (e instanceof CameraAccessException) {
                            if (Log.INSTANCE.getWARN_LOGGABLE()) {
                                android.util.Log.w("CXCP", "Failed to execute call: Camera encountered an error: " + e.getMessage());
                            }
                            cameraErrorListener.mo463onCameraError3M5Xam4(strMo428getCameraIdDz_R5H8, CameraError.Companion.m190fromPVuDhNw$camera_camera2_pipe((CameraAccessException) e), z);
                        } else if (e instanceof IllegalArgumentException) {
                            if (Log.INSTANCE.getWARN_LOGGABLE()) {
                                android.util.Log.w("CXCP", "Failed to execute call: Unexpected exception: " + e.getMessage());
                            }
                            cameraErrorListener.mo463onCameraError3M5Xam4(strMo428getCameraIdDz_R5H8, CameraError.Companion.m201getERROR_GRAPH_CONFIGv7Vf74A(), false);
                        } else {
                            if (Log.INSTANCE.getWARN_LOGGABLE()) {
                                android.util.Log.w("CXCP", "Failed to execute call: Unexpected exception: " + e.getMessage());
                            }
                            cameraErrorListener.mo463onCameraError3M5Xam4(strMo428getCameraIdDz_R5H8, CameraError.Companion.m201getERROR_GRAPH_CONFIGv7Vf74A(), false);
                        }
                        unit = null;
                        i3 = z;
                        Trace.endSection();
                        jM513constructorimpl2 = DurationNs.m513constructorimpl(debug.getSystemTimeSource$camera_camera2_pipe().mo521nowvQl9yQU() - jMo521nowvQl9yQU);
                        log = Log.INSTANCE;
                        if (log.getDEBUG_LOGGABLE()) {
                            StringBuilder sb2 = new StringBuilder();
                            sb2.append(str2);
                            sb2.append(" - ");
                            Timestamps timestamps2 = Timestamps.INSTANCE;
                            Object[] objArr2 = new Object[i3];
                            objArr2[0] = Double.valueOf(jM513constructorimpl2 / 1000000.0d);
                            String str8 = String.format(null, str5 + 3 + str4, Arrays.copyOf(objArr2, i3));
                            Intrinsics.checkNotNullExpressionValue(str8, str3);
                            sb2.append(str8);
                            android.util.Log.d("CXCP", sb2.toString());
                        }
                        if (unit == null) {
                            if (log.getWARN_LOGGABLE()) {
                                android.util.Log.w("CXCP", "Failed to create reprocess session from " + this.cameraDevice + ". Finalizing previous session");
                            }
                            if (sessionStateCallback != null) {
                                onSessionFinalizedWithTrace(sessionStateCallback);
                            }
                        }
                        if (unit != null) {
                            return i3;
                        }
                        return false;
                    } catch (Throwable th2) {
                        th = th2;
                        str2 = str6;
                        z = true;
                        str4 = str4;
                        str5 = str5;
                        str = str3;
                        i = 3;
                        i2 = z;
                        Trace.endSection();
                        jM513constructorimpl = DurationNs.m513constructorimpl(debug.getSystemTimeSource$camera_camera2_pipe().mo521nowvQl9yQU() - jMo521nowvQl9yQU);
                        if (Log.INSTANCE.getDEBUG_LOGGABLE()) {
                            StringBuilder sb3 = new StringBuilder();
                            sb3.append(str2);
                            sb3.append(" - ");
                            Timestamps timestamps3 = Timestamps.INSTANCE;
                            Object[] objArr3 = new Object[i2];
                            objArr3[0] = Double.valueOf(jM513constructorimpl / 1000000.0d);
                            String str9 = String.format(null, str5 + i + str4, Arrays.copyOf(objArr3, i2));
                            Intrinsics.checkNotNullExpressionValue(str9, str);
                            sb3.append(str9);
                            android.util.Log.d("CXCP", sb3.toString());
                        }
                        throw th;
                    }
                } catch (Exception e3) {
                    e = e3;
                    str2 = str6;
                    sessionStateCallback = sessionStateCallback2;
                } catch (Throwable th3) {
                    th = th3;
                    str2 = str6;
                }
                Trace.endSection();
                jM513constructorimpl2 = DurationNs.m513constructorimpl(debug.getSystemTimeSource$camera_camera2_pipe().mo521nowvQl9yQU() - jMo521nowvQl9yQU);
                log = Log.INSTANCE;
                if (log.getDEBUG_LOGGABLE()) {
                    StringBuilder sb4 = new StringBuilder();
                    sb4.append(str2);
                    sb4.append(" - ");
                    Timestamps timestamps4 = Timestamps.INSTANCE;
                    Object[] objArr4 = new Object[i3];
                    objArr4[0] = Double.valueOf(jM513constructorimpl2 / 1000000.0d);
                    String str10 = String.format(null, str5 + 3 + str4, Arrays.copyOf(objArr4, i3));
                    Intrinsics.checkNotNullExpressionValue(str10, str3);
                    sb4.append(str10);
                    android.util.Log.d("CXCP", sb4.toString());
                }
                if (unit == null) {
                    if (log.getWARN_LOGGABLE()) {
                        android.util.Log.w("CXCP", "Failed to create reprocess session from " + this.cameraDevice + ". Finalizing previous session");
                    }
                    if (sessionStateCallback != null) {
                        onSessionFinalizedWithTrace(sessionStateCallback);
                    }
                }
                if (unit != null) {
                    return i3;
                }
                return false;
            } catch (Throwable th4) {
                th = th4;
                i = 3;
                i2 = 1;
                str = "format(...)";
                str2 = str6;
                Trace.endSection();
                jM513constructorimpl = DurationNs.m513constructorimpl(debug.getSystemTimeSource$camera_camera2_pipe().mo521nowvQl9yQU() - jMo521nowvQl9yQU);
                if (Log.INSTANCE.getDEBUG_LOGGABLE()) {
                    StringBuilder sb5 = new StringBuilder();
                    sb5.append(str2);
                    sb5.append(" - ");
                    Timestamps timestamps5 = Timestamps.INSTANCE;
                    Object[] objArr5 = new Object[i2];
                    objArr5[0] = Double.valueOf(jM513constructorimpl / 1000000.0d);
                    String str11 = String.format(null, str5 + i + str4, Arrays.copyOf(objArr5, i2));
                    Intrinsics.checkNotNullExpressionValue(str11, str);
                    sb5.append(str11);
                    android.util.Log.d("CXCP", sb5.toString());
                }
                throw th;
            }
        } catch (Throwable th5) {
            th = th5;
            debug = debug2;
        }
    }

    /* JADX WARN: Multi-variable type inference failed */
    /* JADX WARN: Type inference failed for: r13v3, types: [boolean] */
    /* JADX WARN: Type inference failed for: r9v0, types: [java.lang.String] */
    /* JADX WARN: Type inference failed for: r9v1 */
    /* JADX WARN: Type inference failed for: r9v12 */
    /* JADX WARN: Type inference failed for: r9v13 */
    /* JADX WARN: Type inference failed for: r9v14 */
    /* JADX WARN: Type inference failed for: r9v2, types: [int] */
    /* JADX WARN: Type inference failed for: r9v3 */
    /* JADX WARN: Type inference failed for: r9v4 */
    /* JADX WARN: Type inference failed for: r9v5 */
    /* JADX WARN: Type inference failed for: r9v8, types: [int] */
    /* JADX WARN: Type inference failed for: r9v9 */
    /*  JADX ERROR: JadxRuntimeException in pass: CodeShrinkVisitor
        jadx.core.utils.exceptions.JadxRuntimeException: Can't change immutable type int to ?? for r9v8 ??
        	at jadx.core.dex.instructions.args.SSAVar.setType(SSAVar.java:114)
        	at jadx.core.dex.instructions.args.RegisterArg.setType(RegisterArg.java:52)
        	at jadx.core.dex.instructions.args.InsnArg.wrapInstruction(InsnArg.java:139)
        	at jadx.core.dex.visitors.shrink.CodeShrinkVisitor.inline(CodeShrinkVisitor.java:212)
        	at jadx.core.dex.visitors.shrink.CodeShrinkVisitor.checkInline(CodeShrinkVisitor.java:143)
        	at jadx.core.dex.visitors.shrink.CodeShrinkVisitor.shrinkBlock(CodeShrinkVisitor.java:68)
        	at jadx.core.dex.visitors.shrink.CodeShrinkVisitor.shrinkMethod(CodeShrinkVisitor.java:48)
        	at jadx.core.dex.visitors.shrink.CodeShrinkVisitor.visit(CodeShrinkVisitor.java:39)
        */
    @Override // androidx.camera.camera2.pipe.compat.CameraDeviceWrapper
    public boolean createConstrainedHighSpeedCaptureSession(java.util.List r29, androidx.camera.camera2.pipe.compat.CameraCaptureSessionWrapper.StateCallback r30) {
        /*
            Method dump skipped, instruction units count: 600
            To view this dump add '--comments-level debug' option
        */
        throw new UnsupportedOperationException("Method not decompiled: androidx.camera.camera2.pipe.compat.AndroidCameraDevice.createConstrainedHighSpeedCaptureSession(java.util.List, androidx.camera.camera2.pipe.compat.CameraCaptureSessionWrapper$StateCallback):boolean");
    }

    /* JADX WARN: Multi-variable type inference failed */
    /* JADX WARN: Type inference failed for: r12v11 */
    /* JADX WARN: Type inference failed for: r12v12 */
    /* JADX WARN: Type inference failed for: r12v3, types: [java.lang.String] */
    /* JADX WARN: Type inference failed for: r12v4 */
    /* JADX WARN: Type inference failed for: r12v7 */
    /* JADX WARN: Type inference failed for: r12v9, types: [boolean] */
    /* JADX WARN: Type inference failed for: r6v2, types: [java.lang.StringBuilder] */
    /* JADX WARN: Type inference failed for: r8v0, types: [java.lang.String] */
    /* JADX WARN: Type inference failed for: r8v1, types: [int] */
    /* JADX WARN: Type inference failed for: r8v11 */
    /* JADX WARN: Type inference failed for: r8v12, types: [java.lang.String] */
    /* JADX WARN: Type inference failed for: r8v13 */
    /* JADX WARN: Type inference failed for: r8v14 */
    /* JADX WARN: Type inference failed for: r8v15 */
    /* JADX WARN: Type inference failed for: r8v2 */
    /* JADX WARN: Type inference failed for: r8v3 */
    /* JADX WARN: Type inference failed for: r8v4 */
    /* JADX WARN: Type inference failed for: r8v5 */
    /* JADX WARN: Type inference failed for: r8v8, types: [int] */
    /* JADX WARN: Type inference failed for: r8v9 */
    /*  JADX ERROR: JadxRuntimeException in pass: CodeShrinkVisitor
        jadx.core.utils.exceptions.JadxRuntimeException: Can't change immutable type int to ?? for r8v8 ??
        	at jadx.core.dex.instructions.args.SSAVar.setType(SSAVar.java:114)
        	at jadx.core.dex.instructions.args.RegisterArg.setType(RegisterArg.java:52)
        	at jadx.core.dex.instructions.args.InsnArg.wrapInstruction(InsnArg.java:139)
        	at jadx.core.dex.visitors.shrink.CodeShrinkVisitor.inline(CodeShrinkVisitor.java:212)
        	at jadx.core.dex.visitors.shrink.CodeShrinkVisitor.checkInline(CodeShrinkVisitor.java:143)
        	at jadx.core.dex.visitors.shrink.CodeShrinkVisitor.shrinkBlock(CodeShrinkVisitor.java:68)
        	at jadx.core.dex.visitors.shrink.CodeShrinkVisitor.shrinkMethod(CodeShrinkVisitor.java:48)
        	at jadx.core.dex.visitors.shrink.CodeShrinkVisitor.visit(CodeShrinkVisitor.java:39)
        */
    @Override // androidx.camera.camera2.pipe.compat.CameraDeviceWrapper
    public boolean createCaptureSessionByOutputConfigurations(java.util.List r26, androidx.camera.camera2.pipe.compat.CameraCaptureSessionWrapper.StateCallback r27) {
        /*
            Method dump skipped, instruction units count: 630
            To view this dump add '--comments-level debug' option
        */
        throw new UnsupportedOperationException("Method not decompiled: androidx.camera.camera2.pipe.compat.AndroidCameraDevice.createCaptureSessionByOutputConfigurations(java.util.List, androidx.camera.camera2.pipe.compat.CameraCaptureSessionWrapper$StateCallback):boolean");
    }

    /* JADX WARN: Multi-variable type inference failed */
    /* JADX WARN: Type inference failed for: r12v6, types: [boolean] */
    /* JADX WARN: Type inference failed for: r6v5, types: [java.lang.StringBuilder] */
    /* JADX WARN: Type inference failed for: r8v1 */
    /* JADX WARN: Type inference failed for: r8v2, types: [java.lang.String] */
    /* JADX WARN: Type inference failed for: r8v4 */
    /* JADX WARN: Type inference failed for: r8v6 */
    /* JADX WARN: Type inference failed for: r8v8 */
    /* JADX WARN: Type inference failed for: r9v0, types: [java.lang.String] */
    /* JADX WARN: Type inference failed for: r9v1 */
    /* JADX WARN: Type inference failed for: r9v11 */
    /* JADX WARN: Type inference failed for: r9v12, types: [java.lang.String] */
    /* JADX WARN: Type inference failed for: r9v13 */
    /* JADX WARN: Type inference failed for: r9v14 */
    /* JADX WARN: Type inference failed for: r9v15 */
    /* JADX WARN: Type inference failed for: r9v2, types: [int] */
    /* JADX WARN: Type inference failed for: r9v3 */
    /* JADX WARN: Type inference failed for: r9v4 */
    /* JADX WARN: Type inference failed for: r9v5 */
    /* JADX WARN: Type inference failed for: r9v6 */
    /* JADX WARN: Type inference failed for: r9v9, types: [int] */
    /*  JADX ERROR: JadxRuntimeException in pass: CodeShrinkVisitor
        jadx.core.utils.exceptions.JadxRuntimeException: Can't change immutable type int to ?? for r9v9 ??
        	at jadx.core.dex.instructions.args.SSAVar.setType(SSAVar.java:114)
        	at jadx.core.dex.instructions.args.RegisterArg.setType(RegisterArg.java:52)
        	at jadx.core.dex.instructions.args.InsnArg.wrapInstruction(InsnArg.java:139)
        	at jadx.core.dex.visitors.shrink.CodeShrinkVisitor.inline(CodeShrinkVisitor.java:212)
        	at jadx.core.dex.visitors.shrink.CodeShrinkVisitor.checkInline(CodeShrinkVisitor.java:143)
        	at jadx.core.dex.visitors.shrink.CodeShrinkVisitor.shrinkBlock(CodeShrinkVisitor.java:68)
        	at jadx.core.dex.visitors.shrink.CodeShrinkVisitor.shrinkMethod(CodeShrinkVisitor.java:48)
        	at jadx.core.dex.visitors.shrink.CodeShrinkVisitor.visit(CodeShrinkVisitor.java:39)
        */
    @Override // androidx.camera.camera2.pipe.compat.CameraDeviceWrapper
    public boolean createReprocessableCaptureSessionByConfigurations(androidx.camera.camera2.pipe.compat.InputConfigData r27, java.util.List r28, androidx.camera.camera2.pipe.compat.CameraCaptureSessionWrapper.StateCallback r29) {
        /*
            Method dump skipped, instruction units count: 667
            To view this dump add '--comments-level debug' option
        */
        throw new UnsupportedOperationException("Method not decompiled: androidx.camera.camera2.pipe.compat.AndroidCameraDevice.createReprocessableCaptureSessionByConfigurations(androidx.camera.camera2.pipe.compat.InputConfigData, java.util.List, androidx.camera.camera2.pipe.compat.CameraCaptureSessionWrapper$StateCallback):boolean");
    }

    /* JADX WARN: Multi-variable type inference failed */
    /* JADX WARN: Type inference failed for: r12v2 */
    /* JADX WARN: Type inference failed for: r12v3, types: [java.lang.String] */
    /* JADX WARN: Type inference failed for: r12v5, types: [java.lang.String] */
    /* JADX WARN: Type inference failed for: r13v3, types: [boolean] */
    /* JADX WARN: Type inference failed for: r23v12 */
    /* JADX WARN: Type inference failed for: r23v13 */
    /* JADX WARN: Type inference failed for: r23v14 */
    /* JADX WARN: Type inference failed for: r23v5 */
    /* JADX WARN: Type inference failed for: r23v6 */
    /* JADX WARN: Type inference failed for: r4v14, types: [java.lang.StringBuilder] */
    /* JADX WARN: Type inference failed for: r5v1, types: [java.lang.StringBuilder] */
    /* JADX WARN: Type inference failed for: r8v0, types: [java.lang.String] */
    /* JADX WARN: Type inference failed for: r8v1 */
    /* JADX WARN: Type inference failed for: r8v10 */
    /* JADX WARN: Type inference failed for: r8v11, types: [java.lang.String] */
    /* JADX WARN: Type inference failed for: r8v12 */
    /* JADX WARN: Type inference failed for: r8v13, types: [java.lang.String] */
    /* JADX WARN: Type inference failed for: r8v2, types: [int] */
    /* JADX WARN: Type inference failed for: r8v3 */
    /* JADX WARN: Type inference failed for: r8v4 */
    /* JADX WARN: Type inference failed for: r8v7, types: [int] */
    /* JADX WARN: Type inference failed for: r8v9 */
    /*  JADX ERROR: JadxRuntimeException in pass: CodeShrinkVisitor
        jadx.core.utils.exceptions.JadxRuntimeException: Can't change immutable type int to ?? for r8v7 ??
        	at jadx.core.dex.instructions.args.SSAVar.setType(SSAVar.java:114)
        	at jadx.core.dex.instructions.args.RegisterArg.setType(RegisterArg.java:52)
        	at jadx.core.dex.instructions.args.InsnArg.wrapInstruction(InsnArg.java:139)
        	at jadx.core.dex.visitors.shrink.CodeShrinkVisitor.inline(CodeShrinkVisitor.java:212)
        	at jadx.core.dex.visitors.shrink.CodeShrinkVisitor.checkInline(CodeShrinkVisitor.java:143)
        	at jadx.core.dex.visitors.shrink.CodeShrinkVisitor.shrinkBlock(CodeShrinkVisitor.java:68)
        	at jadx.core.dex.visitors.shrink.CodeShrinkVisitor.shrinkMethod(CodeShrinkVisitor.java:48)
        	at jadx.core.dex.visitors.shrink.CodeShrinkVisitor.visit(CodeShrinkVisitor.java:39)
        */
    @Override // androidx.camera.camera2.pipe.compat.CameraDeviceWrapper
    public boolean createCaptureSession(androidx.camera.camera2.pipe.compat.SessionConfigData r28) {
        /*
            Method dump skipped, instruction units count: 995
            To view this dump add '--comments-level debug' option
        */
        throw new UnsupportedOperationException("Method not decompiled: androidx.camera.camera2.pipe.compat.AndroidCameraDevice.createCaptureSession(androidx.camera.camera2.pipe.compat.SessionConfigData):boolean");
    }

    @Override // androidx.camera.camera2.pipe.compat.CameraDeviceWrapper
    /* JADX INFO: renamed from: createCaptureRequest-2PPcXtw, reason: not valid java name */
    public CaptureRequest.Builder mo427createCaptureRequest2PPcXtw(int i) throws Throwable {
        double d;
        CaptureRequest.Builder builderCreateCaptureRequest;
        Debug debug = Debug.INSTANCE;
        String str = "CXCP#createCaptureRequest" + SignatureVisitor.SUPER + mo428getCameraIdDz_R5H8();
        long jMo521nowvQl9yQU = debug.getSystemTimeSource$camera_camera2_pipe().mo521nowvQl9yQU();
        try {
            Trace.beginSection(str);
            d = 1000000.0d;
            try {
                String strMo428getCameraIdDz_R5H8 = mo428getCameraIdDz_R5H8();
                CameraErrorListener cameraErrorListener = this.cameraErrorListener;
                try {
                    builderCreateCaptureRequest = this.cameraDevice.createCaptureRequest(i);
                } catch (Exception e) {
                    if (e instanceof CameraAccessException) {
                        if (Log.INSTANCE.getWARN_LOGGABLE()) {
                            android.util.Log.w("CXCP", "Failed to execute call: Camera encountered an error: " + e.getMessage());
                        }
                        cameraErrorListener.mo463onCameraError3M5Xam4(strMo428getCameraIdDz_R5H8, CameraError.Companion.m190fromPVuDhNw$camera_camera2_pipe((CameraAccessException) e), true);
                    } else if ((e instanceof IllegalArgumentException) || (e instanceof SecurityException) || (e instanceof UnsupportedOperationException) || (e instanceof NullPointerException)) {
                        if (Log.INSTANCE.getWARN_LOGGABLE()) {
                            android.util.Log.w("CXCP", "Failed to execute call: Unexpected exception: " + e.getMessage());
                        }
                        cameraErrorListener.mo463onCameraError3M5Xam4(strMo428getCameraIdDz_R5H8, CameraError.Companion.m201getERROR_GRAPH_CONFIGv7Vf74A(), false);
                    } else {
                        if (!(e instanceof IllegalStateException)) {
                            throw e;
                        }
                        if (Log.INSTANCE.getDEBUG_LOGGABLE()) {
                            android.util.Log.d("CXCP", "Failed to execute call: Camera may be closed");
                        }
                    }
                    builderCreateCaptureRequest = null;
                }
                Trace.endSection();
                long jM513constructorimpl = DurationNs.m513constructorimpl(debug.getSystemTimeSource$camera_camera2_pipe().mo521nowvQl9yQU() - jMo521nowvQl9yQU);
                if (Log.INSTANCE.getDEBUG_LOGGABLE()) {
                    StringBuilder sb = new StringBuilder();
                    sb.append(str);
                    sb.append(" - ");
                    Timestamps timestamps = Timestamps.INSTANCE;
                    String str2 = String.format(null, "%.3f ms", Arrays.copyOf(new Object[]{Double.valueOf(jM513constructorimpl / 1000000.0d)}, 1));
                    Intrinsics.checkNotNullExpressionValue(str2, "format(...)");
                    sb.append(str2);
                    android.util.Log.d("CXCP", sb.toString());
                }
                return builderCreateCaptureRequest;
            } catch (Throwable th) {
                th = th;
                Trace.endSection();
                long jM513constructorimpl2 = DurationNs.m513constructorimpl(debug.getSystemTimeSource$camera_camera2_pipe().mo521nowvQl9yQU() - jMo521nowvQl9yQU);
                if (Log.INSTANCE.getDEBUG_LOGGABLE()) {
                    StringBuilder sb2 = new StringBuilder();
                    sb2.append(str);
                    sb2.append(" - ");
                    Timestamps timestamps2 = Timestamps.INSTANCE;
                    String str3 = String.format(null, "%.3f ms", Arrays.copyOf(new Object[]{Double.valueOf(jM513constructorimpl2 / d)}, 1));
                    Intrinsics.checkNotNullExpressionValue(str3, "format(...)");
                    sb2.append(str3);
                    android.util.Log.d("CXCP", sb2.toString());
                }
                throw th;
            }
        } catch (Throwable th2) {
            th = th2;
            d = 1000000.0d;
        }
    }

    @Override // androidx.camera.camera2.pipe.compat.AudioRestrictionController.Listener
    /* JADX INFO: renamed from: onCameraAudioRestrictionUpdated-LwUUkyU, reason: not valid java name */
    public void mo429onCameraAudioRestrictionUpdatedLwUUkyU(int i) {
        Debug debug = Debug.INSTANCE;
        try {
            Trace.beginSection("setCameraAudioRestriction");
            String strMo428getCameraIdDz_R5H8 = mo428getCameraIdDz_R5H8();
            CameraErrorListener cameraErrorListener = this.cameraErrorListener;
            try {
                Api30Compat.setCameraAudioRestriction(this.cameraDevice, i);
                Unit unit = Unit.INSTANCE;
            } catch (Exception e) {
                if (e instanceof CameraAccessException) {
                    if (Log.INSTANCE.getWARN_LOGGABLE()) {
                        android.util.Log.w("CXCP", "Failed to execute call: Camera encountered an error: " + e.getMessage());
                    }
                    cameraErrorListener.mo463onCameraError3M5Xam4(strMo428getCameraIdDz_R5H8, CameraError.Companion.m190fromPVuDhNw$camera_camera2_pipe((CameraAccessException) e), true);
                } else if ((e instanceof IllegalArgumentException) || (e instanceof SecurityException) || (e instanceof UnsupportedOperationException) || (e instanceof NullPointerException)) {
                    if (Log.INSTANCE.getWARN_LOGGABLE()) {
                        android.util.Log.w("CXCP", "Failed to execute call: Unexpected exception: " + e.getMessage());
                    }
                    cameraErrorListener.mo463onCameraError3M5Xam4(strMo428getCameraIdDz_R5H8, CameraError.Companion.m201getERROR_GRAPH_CONFIGv7Vf74A(), false);
                } else {
                    if (!(e instanceof IllegalStateException)) {
                        throw e;
                    }
                    if (Log.INSTANCE.getDEBUG_LOGGABLE()) {
                        android.util.Log.d("CXCP", "Failed to execute call: Camera may be closed");
                    }
                }
            }
            Trace.endSection();
        } catch (Throwable th) {
            Trace.endSection();
            throw th;
        }
    }

    @Override // androidx.camera.camera2.pipe.compat.CameraDeviceWrapper
    public void onDeviceClosing() {
        SessionStateCallback sessionStateCallback;
        if (!this.closed.compareAndSet(false, true) || (sessionStateCallback = (SessionStateCallback) this._lastStateCallback.getValue()) == null) {
            return;
        }
        onSessionDisconnectedWithTrace(sessionStateCallback);
    }

    @Override // androidx.camera.camera2.pipe.compat.CameraDeviceWrapper
    public void onDeviceClosed() {
        if (!this.closed.getValue()) {
            throw new IllegalStateException("Check failed.");
        }
        SessionStateCallback sessionStateCallback = (SessionStateCallback) this._lastStateCallback.getAndSet(null);
        if (sessionStateCallback != null) {
            onSessionFinalizedWithTrace(sessionStateCallback);
        }
    }

    @Override // androidx.camera.camera2.pipe.UnsafeWrapper
    public Object unwrapAs(KClass type) {
        Intrinsics.checkNotNullParameter(type, "type");
        if (!Intrinsics.areEqual(type, Reflection.getOrCreateKotlinClass(CameraDevice.class))) {
            return null;
        }
        CameraDevice cameraDevice = this.cameraDevice;
        Intrinsics.checkNotNull(cameraDevice, "null cannot be cast to non-null type T of androidx.camera.camera2.pipe.compat.AndroidCameraDevice.unwrapAs");
        return cameraDevice;
    }

    public String toString() {
        return "AndroidCameraDevice(camera=" + ((Object) CameraId.m238toStringimpl(mo428getCameraIdDz_R5H8())) + ')';
    }

    @Override // androidx.camera.camera2.pipe.compat.CameraDeviceWrapper
    public CaptureRequest.Builder createReprocessCaptureRequest(TotalCaptureResult inputResult) throws Throwable {
        double d;
        CaptureRequest.Builder builderCreateReprocessCaptureRequest;
        Intrinsics.checkNotNullParameter(inputResult, "inputResult");
        Debug debug = Debug.INSTANCE;
        String str = "CXCP#createReprocessCaptureRequest" + SignatureVisitor.SUPER + mo428getCameraIdDz_R5H8();
        long jMo521nowvQl9yQU = debug.getSystemTimeSource$camera_camera2_pipe().mo521nowvQl9yQU();
        try {
            Trace.beginSection(str);
            d = 1000000.0d;
            try {
                String strMo428getCameraIdDz_R5H8 = mo428getCameraIdDz_R5H8();
                CameraErrorListener cameraErrorListener = this.cameraErrorListener;
                try {
                    builderCreateReprocessCaptureRequest = this.cameraDevice.createReprocessCaptureRequest(inputResult);
                } catch (Exception e) {
                    if (e instanceof CameraAccessException) {
                        if (Log.INSTANCE.getWARN_LOGGABLE()) {
                            android.util.Log.w("CXCP", "Failed to execute call: Camera encountered an error: " + e.getMessage());
                        }
                        cameraErrorListener.mo463onCameraError3M5Xam4(strMo428getCameraIdDz_R5H8, CameraError.Companion.m190fromPVuDhNw$camera_camera2_pipe((CameraAccessException) e), true);
                    } else if ((e instanceof IllegalArgumentException) || (e instanceof SecurityException) || (e instanceof UnsupportedOperationException) || (e instanceof NullPointerException)) {
                        if (Log.INSTANCE.getWARN_LOGGABLE()) {
                            android.util.Log.w("CXCP", "Failed to execute call: Unexpected exception: " + e.getMessage());
                        }
                        cameraErrorListener.mo463onCameraError3M5Xam4(strMo428getCameraIdDz_R5H8, CameraError.Companion.m201getERROR_GRAPH_CONFIGv7Vf74A(), false);
                    } else {
                        if (!(e instanceof IllegalStateException)) {
                            throw e;
                        }
                        if (Log.INSTANCE.getDEBUG_LOGGABLE()) {
                            android.util.Log.d("CXCP", "Failed to execute call: Camera may be closed");
                        }
                    }
                    builderCreateReprocessCaptureRequest = null;
                }
                Trace.endSection();
                long jM513constructorimpl = DurationNs.m513constructorimpl(debug.getSystemTimeSource$camera_camera2_pipe().mo521nowvQl9yQU() - jMo521nowvQl9yQU);
                if (Log.INSTANCE.getDEBUG_LOGGABLE()) {
                    StringBuilder sb = new StringBuilder();
                    sb.append(str);
                    sb.append(" - ");
                    Timestamps timestamps = Timestamps.INSTANCE;
                    String str2 = String.format(null, "%.3f ms", Arrays.copyOf(new Object[]{Double.valueOf(jM513constructorimpl / 1000000.0d)}, 1));
                    Intrinsics.checkNotNullExpressionValue(str2, "format(...)");
                    sb.append(str2);
                    android.util.Log.d("CXCP", sb.toString());
                }
                return builderCreateReprocessCaptureRequest;
            } catch (Throwable th) {
                th = th;
                Trace.endSection();
                long jM513constructorimpl2 = DurationNs.m513constructorimpl(debug.getSystemTimeSource$camera_camera2_pipe().mo521nowvQl9yQU() - jMo521nowvQl9yQU);
                if (Log.INSTANCE.getDEBUG_LOGGABLE()) {
                    StringBuilder sb2 = new StringBuilder();
                    sb2.append(str);
                    sb2.append(" - ");
                    Timestamps timestamps2 = Timestamps.INSTANCE;
                    String str3 = String.format(null, "%.3f ms", Arrays.copyOf(new Object[]{Double.valueOf(jM513constructorimpl2 / d)}, 1));
                    Intrinsics.checkNotNullExpressionValue(str3, "format(...)");
                    sb2.append(str3);
                    android.util.Log.d("CXCP", sb2.toString());
                }
                throw th;
            }
        } catch (Throwable th2) {
            th = th2;
            d = 1000000.0d;
        }
    }

    private final Pair checkAndSetStateCallback(SessionStateCallback sessionStateCallback) {
        if (this.closed.getValue()) {
            onSessionFinalizedWithTrace(sessionStateCallback);
            return new Pair(Boolean.FALSE, null);
        }
        return new Pair(Boolean.TRUE, this._lastStateCallback.getAndSet(sessionStateCallback));
    }

    private final void onSessionDisconnectedWithTrace(SessionStateCallback sessionStateCallback) {
        Debug debug = Debug.INSTANCE;
        try {
            Trace.beginSection(this + "#onSessionDisconnected");
            sessionStateCallback.onSessionDisconnected();
            Unit unit = Unit.INSTANCE;
        } finally {
            Trace.endSection();
        }
    }

    private final void onSessionFinalizedWithTrace(SessionStateCallback sessionStateCallback) {
        Debug debug = Debug.INSTANCE;
        try {
            Trace.beginSection(this + "#onSessionFinalized");
            sessionStateCallback.onSessionFinalized();
            Unit unit = Unit.INSTANCE;
        } finally {
            Trace.endSection();
        }
    }
}
