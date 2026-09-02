package org.telegram.ui.Stories.recorder;

import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Matrix;
import android.graphics.PointF;
import android.graphics.SurfaceTexture;
import android.hardware.Camera;
import android.os.Build;
import android.view.MotionEvent;
import android.view.ViewConfiguration;
import com.exteragram.messenger.utils.system.VibratorUtils;
import com.google.common.primitives.Floats;
import com.google.zxing.common.detector.MathUtils;
import okhttp3.internal.url._UrlKt;
import org.telegram.messenger.AndroidUtilities;
import org.telegram.messenger.ApplicationLoader;
import org.telegram.messenger.FileLog;
import org.telegram.messenger.LocaleController;
import org.telegram.messenger.MessagesController;
import org.telegram.messenger.R;
import org.telegram.messenger.SharedConfig;
import org.telegram.messenger.UserConfig;
import org.telegram.messenger.camera.CameraSessionWrapper;
import org.telegram.messenger.camera.CameraView;
import org.telegram.tgnet.ConnectionsManager;
import org.telegram.tgnet.RequestDelegate;
import org.telegram.tgnet.TLObject;
import org.telegram.tgnet.TLRPC;
import org.telegram.ui.ActionBar.AlertDialog;

public abstract class DualCameraView extends CameraView {
    private static final int[] dualWhitelistByDevice = {1893745684, -215458996, -862041025, -1258375037, -1320049076, -215749424, 1901578030, -215451421, 1908491424, -1321491332, -1155551678, 1908524435, 976847578, -1489198134, 1910814392, -713271737, -2010722764, 1407170066, -821405251, -1394190955, -1394190055, 1407170066, 1407159934, 1407172057, 1231389747, -2076538925, 41497626, 846150482, -1198092731, -251277614, -2073158771, 1273004781};
    private static final int[] dualWhitelistByModel = new int[0];
    private boolean allowRotation;
    private float angle;
    private boolean atBottom;
    private boolean atTop;
    private float cx;
    private float cy;
    private boolean doNotSpanRotation;
    private boolean down;
    private boolean dualAvailable;
    private boolean enabledSavedDual;
    private final Matrix finalMatrix;
    private boolean firstMeasure;
    private float h;
    private Matrix invMatrix;
    private Runnable lastFocusToPoint;
    private final PointF lastTouch;
    private float lastTouchDistance;
    private double lastTouchRotation;
    private Runnable longpressRunnable;
    private boolean multitouch;
    private float rotationDiff;
    private boolean snappedRotation;
    private long tapTime;
    private float tapX;
    private float tapY;
    private Matrix tempMatrix;
    private float[] tempPoint;
    private final Matrix toGL;
    private final Matrix toScreen;
    private final PointF touch;
    private final Matrix touchMatrix;
    private float[] vertex;
    private final float[] vertices;
    private float[] verticesDst;
    private float[] verticesSrc;
    private float w;

    public static /* synthetic */ void $r8$lambda$BDUEjGDaDbdUbrccOYYisJzQ2iY(TLObject tLObject, TLRPC.TL_error tL_error) {
    }

    protected abstract void onEntityDraggedBottom(boolean z);

    protected abstract void onEntityDraggedTop(boolean z);

    protected abstract void onSavedDualCameraSuccess();

    public DualCameraView(Context context, boolean z, boolean z2) {
        super(context, z, z2);
        this.lastTouch = new PointF();
        this.touch = new PointF();
        this.touchMatrix = new Matrix();
        this.finalMatrix = new Matrix();
        this.tempPoint = new float[4];
        this.toScreen = new Matrix();
        this.toGL = new Matrix();
        this.firstMeasure = true;
        this.invMatrix = new Matrix();
        this.vertices = new float[2];
        this.tempMatrix = new Matrix();
        this.vertex = new float[2];
        this.dualAvailable = dualAvailableStatic(context);
    }

    @Override // android.view.View
    public boolean onTouchEvent(MotionEvent motionEvent) {
        return super.onTouchEvent(motionEvent) || touchEvent(motionEvent);
    }

    @Override // android.view.ViewGroup
    public boolean onInterceptTouchEvent(MotionEvent motionEvent) {
        if (motionEvent.getAction() == 0 && isAtDual(motionEvent.getX(), motionEvent.getY())) {
            return touchEvent(motionEvent);
        }
        return super.onInterceptTouchEvent(motionEvent);
    }

    @Override // org.telegram.messenger.camera.CameraView
    public void destroy(boolean z, Runnable runnable) {
        saveDual();
        super.destroy(z, runnable);
    }

    @Override // org.telegram.messenger.camera.CameraView, android.widget.FrameLayout, android.view.View
    protected void onMeasure(int i, int i2) {
        super.onMeasure(i, i2);
        setupToScreenMatrix();
    }

    private void setupToScreenMatrix() {
        this.toScreen.reset();
        this.toScreen.postTranslate(1.0f, -1.0f);
        this.toScreen.postScale(getMeasuredWidth() / 2.0f, (-getMeasuredHeight()) / 2.0f);
        this.toScreen.invert(this.toGL);
    }

    @Override // org.telegram.messenger.camera.CameraView, android.view.TextureView.SurfaceTextureListener
    public void onSurfaceTextureAvailable(SurfaceTexture surfaceTexture, int i, int i2) {
        if (this.firstMeasure) {
            if (isSavedDual()) {
                this.enabledSavedDual = true;
                setupDualMatrix();
                this.dual = true;
            }
            this.firstMeasure = false;
        }
        super.onSurfaceTextureAvailable(surfaceTexture, i, i2);
    }

    @Override // org.telegram.messenger.camera.CameraView
    protected void onDualCameraSuccess() {
        saveDual();
        if (this.enabledSavedDual) {
            onSavedDualCameraSuccess();
        }
        log(true);
    }

    private void log(boolean z) {
        boolean zDualAvailableDefault = dualAvailableDefault(ApplicationLoader.applicationContext, false);
        if (MessagesController.getInstance(UserConfig.selectedAccount).collectDeviceStats) {
            try {
                TLRPC.TL_help_saveAppLog tL_help_saveAppLog = new TLRPC.TL_help_saveAppLog();
                TLRPC.TL_inputAppEvent tL_inputAppEvent = new TLRPC.TL_inputAppEvent();
                tL_inputAppEvent.time = ConnectionsManager.getInstance(UserConfig.selectedAccount).getCurrentTime();
                tL_inputAppEvent.type = "android_dual_camera";
                TLRPC.TL_jsonObject tL_jsonObject = new TLRPC.TL_jsonObject();
                TLRPC.TL_jsonObjectValue tL_jsonObjectValue = new TLRPC.TL_jsonObjectValue();
                tL_jsonObjectValue.key = "device";
                TLRPC.TL_jsonString tL_jsonString = new TLRPC.TL_jsonString();
                tL_jsonString.value = _UrlKt.FRAGMENT_ENCODE_SET + Build.MANUFACTURER + Build.MODEL;
                tL_jsonObjectValue.value = tL_jsonString;
                tL_jsonObject.value.add(tL_jsonObjectValue);
                tL_inputAppEvent.data = tL_jsonObject;
                tL_inputAppEvent.peer = (z ? 1 : 0) | (zDualAvailableDefault ? 2 : 0);
                tL_help_saveAppLog.events.add(tL_inputAppEvent);
                ConnectionsManager.getInstance(UserConfig.selectedAccount).sendRequest(tL_help_saveAppLog, new RequestDelegate() { // from class: org.telegram.ui.Stories.recorder.DualCameraView$$ExternalSyntheticLambda0
                    @Override // org.telegram.tgnet.RequestDelegate
                    public final void run(TLObject tLObject, TLRPC.TL_error tL_error) {
                        DualCameraView.$r8$lambda$BDUEjGDaDbdUbrccOYYisJzQ2iY(tLObject, tL_error);
                    }
                });
            } catch (Exception unused) {
            }
        }
    }

    public void resetSaved() {
        resetSavedDual();
    }

    @Override // org.telegram.messenger.camera.CameraView
    public void toggleDual() {
        if (isDual() || dualAvailable()) {
            if (!isDual()) {
                setupDualMatrix();
            } else {
                resetSaved();
            }
            super.toggleDual();
        }
    }

    private void setupDualMatrix() {
        Matrix dualPosition = getDualPosition();
        dualPosition.reset();
        Matrix savedDualMatrix = getSavedDualMatrix();
        if (savedDualMatrix != null) {
            dualPosition.set(savedDualMatrix);
        } else {
            dualPosition.postConcat(this.toScreen);
            float measuredWidth = getMeasuredWidth() * 0.43f;
            float measuredHeight = getMeasuredHeight() * 0.43f;
            float fMin = Math.min(getMeasuredWidth(), getMeasuredWidth()) * 0.025f;
            dualPosition.postScale(measuredWidth / getMeasuredWidth(), measuredHeight / getMeasuredHeight());
            dualPosition.postTranslate((getMeasuredWidth() - fMin) - measuredWidth, fMin);
            dualPosition.postConcat(this.toGL);
        }
        updateDualPosition();
    }

    public boolean isAtDual(float f, float f2) {
        if (!isDual()) {
            return false;
        }
        float[] fArr = this.vertex;
        fArr[0] = f;
        fArr[1] = f2;
        this.toGL.mapPoints(fArr);
        getDualPosition().invert(this.invMatrix);
        this.invMatrix.mapPoints(this.vertex);
        int dualShape = getDualShape() % 3;
        float f3 = (dualShape == 0 || dualShape == 1 || dualShape == 3) ? 0.5625f : 1.0f;
        float[] fArr2 = this.vertex;
        float f4 = fArr2[0];
        if (f4 >= -1.0f && f4 <= 1.0f) {
            float f5 = fArr2[1];
            if (f5 >= (-f3) && f5 <= f3) {
                return true;
            }
        }
        return false;
    }

    private boolean checkTap(MotionEvent motionEvent) {
        if (motionEvent.getAction() == 0) {
            this.tapTime = System.currentTimeMillis();
            this.tapX = motionEvent.getX();
            this.tapY = motionEvent.getY();
            this.lastFocusToPoint = null;
            Runnable runnable = this.longpressRunnable;
            if (runnable != null) {
                AndroidUtilities.cancelRunOnUIThread(runnable);
                this.longpressRunnable = null;
            }
            if (!isAtDual(this.tapX, this.tapY)) {
                return false;
            }
            Runnable runnable2 = new Runnable() { // from class: org.telegram.ui.Stories.recorder.DualCameraView$$ExternalSyntheticLambda1
                @Override // java.lang.Runnable
                public final void run() {
                    this.f$0.lambda$checkTap$1();
                }
            };
            this.longpressRunnable = runnable2;
            AndroidUtilities.runOnUIThread(runnable2, ViewConfiguration.getLongPressTimeout());
            return true;
        }
        if (motionEvent.getAction() == 1) {
            if (System.currentTimeMillis() - this.tapTime <= ViewConfiguration.getTapTimeout() && MathUtils.distance(this.tapX, this.tapY, motionEvent.getX(), motionEvent.getY()) < AndroidUtilities.dp(10.0f)) {
                if (isAtDual(this.tapX, this.tapY)) {
                    switchCamera();
                    this.lastFocusToPoint = null;
                } else {
                    this.lastFocusToPoint = new Runnable() { // from class: org.telegram.ui.Stories.recorder.DualCameraView$$ExternalSyntheticLambda2
                        @Override // java.lang.Runnable
                        public final void run() {
                            this.f$0.lambda$checkTap$2();
                        }
                    };
                }
            }
            this.tapTime = -1L;
            Runnable runnable3 = this.longpressRunnable;
            if (runnable3 == null) {
                return false;
            }
            AndroidUtilities.cancelRunOnUIThread(runnable3);
            this.longpressRunnable = null;
            return false;
        }
        if (motionEvent.getAction() != 3) {
            return false;
        }
        this.tapTime = -1L;
        this.lastFocusToPoint = null;
        Runnable runnable4 = this.longpressRunnable;
        if (runnable4 == null) {
            return false;
        }
        AndroidUtilities.cancelRunOnUIThread(runnable4);
        this.longpressRunnable = null;
        return false;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$checkTap$1() {
        if (this.tapTime > 0) {
            dualToggleShape();
            try {
                performHapticFeedback(VibratorUtils.getType(0), 1);
            } catch (Exception unused) {
            }
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$checkTap$2() {
        focusToPoint((int) this.tapX, (int) this.tapY);
    }

    public void allowToTapFocus() {
        Runnable runnable = this.lastFocusToPoint;
        if (runnable != null) {
            runnable.run();
            this.lastFocusToPoint = null;
        }
    }

    public void clearTapFocus() {
        this.lastFocusToPoint = null;
        this.tapTime = -1L;
    }

    /* JADX WARN: Failed to calculate best type for var: r3v6 ??
    jadx.core.utils.exceptions.JadxRuntimeException: Type update failed for variable: r3v6 ??, new type: float
    	at jadx.core.dex.visitors.typeinference.TypeUpdate.apply(TypeUpdate.java:109)
    	at jadx.core.dex.visitors.typeinference.TypeUpdate.apply(TypeUpdate.java:59)
    	at jadx.core.dex.visitors.typeinference.TypeInferenceVisitor.calculateFromBounds(TypeInferenceVisitor.java:147)
    	at jadx.core.dex.visitors.typeinference.TypeInferenceVisitor.setBestType(TypeInferenceVisitor.java:125)
    	at jadx.core.dex.visitors.typeinference.TypeInferenceVisitor.lambda$runTypePropagation$2(TypeInferenceVisitor.java:103)
    	at java.base/java.util.ArrayList.forEach(ArrayList.java:1596)
    	at jadx.core.dex.visitors.typeinference.TypeInferenceVisitor.runTypePropagation(TypeInferenceVisitor.java:103)
    	at jadx.core.dex.visitors.typeinference.TypeInferenceVisitor.visit(TypeInferenceVisitor.java:75)
    Caused by: java.lang.NullPointerException
     */
    /* JADX WARN: Failed to calculate best type for var: r4v17 ??
    jadx.core.utils.exceptions.JadxRuntimeException: Type update failed for variable: r4v17 ??, new type: android.graphics.Matrix
    	at jadx.core.dex.visitors.typeinference.TypeUpdate.apply(TypeUpdate.java:109)
    	at jadx.core.dex.visitors.typeinference.TypeUpdate.apply(TypeUpdate.java:59)
    	at jadx.core.dex.visitors.typeinference.TypeInferenceVisitor.calculateFromBounds(TypeInferenceVisitor.java:147)
    	at jadx.core.dex.visitors.typeinference.TypeInferenceVisitor.setBestType(TypeInferenceVisitor.java:125)
    	at jadx.core.dex.visitors.typeinference.TypeInferenceVisitor.lambda$runTypePropagation$2(TypeInferenceVisitor.java:103)
    	at java.base/java.util.ArrayList.forEach(ArrayList.java:1596)
    	at jadx.core.dex.visitors.typeinference.TypeInferenceVisitor.runTypePropagation(TypeInferenceVisitor.java:103)
    	at jadx.core.dex.visitors.typeinference.TypeInferenceVisitor.visit(TypeInferenceVisitor.java:75)
    Caused by: java.lang.NullPointerException
     */
    /* JADX WARN: Failed to calculate best type for var: r4v23 ??
    jadx.core.utils.exceptions.JadxRuntimeException: Type update failed for variable: r4v23 ??, new type: float
    	at jadx.core.dex.visitors.typeinference.TypeUpdate.apply(TypeUpdate.java:109)
    	at jadx.core.dex.visitors.typeinference.TypeUpdate.apply(TypeUpdate.java:59)
    	at jadx.core.dex.visitors.typeinference.TypeInferenceVisitor.calculateFromBounds(TypeInferenceVisitor.java:147)
    	at jadx.core.dex.visitors.typeinference.TypeInferenceVisitor.setBestType(TypeInferenceVisitor.java:125)
    	at jadx.core.dex.visitors.typeinference.TypeInferenceVisitor.lambda$runTypePropagation$2(TypeInferenceVisitor.java:103)
    	at java.base/java.util.ArrayList.forEach(ArrayList.java:1596)
    	at jadx.core.dex.visitors.typeinference.TypeInferenceVisitor.runTypePropagation(TypeInferenceVisitor.java:103)
    	at jadx.core.dex.visitors.typeinference.TypeInferenceVisitor.visit(TypeInferenceVisitor.java:75)
    Caused by: java.lang.NullPointerException
     */
    /* JADX WARN: Failed to calculate best type for var: r4v24 ??
    jadx.core.utils.exceptions.JadxRuntimeException: Type update failed for variable: r4v24 ??, new type: float
    	at jadx.core.dex.visitors.typeinference.TypeUpdate.apply(TypeUpdate.java:109)
    	at jadx.core.dex.visitors.typeinference.TypeUpdate.apply(TypeUpdate.java:59)
    	at jadx.core.dex.visitors.typeinference.TypeInferenceVisitor.calculateFromBounds(TypeInferenceVisitor.java:147)
    	at jadx.core.dex.visitors.typeinference.TypeInferenceVisitor.setBestType(TypeInferenceVisitor.java:125)
    	at jadx.core.dex.visitors.typeinference.TypeInferenceVisitor.lambda$runTypePropagation$2(TypeInferenceVisitor.java:103)
    	at java.base/java.util.ArrayList.forEach(ArrayList.java:1596)
    	at jadx.core.dex.visitors.typeinference.TypeInferenceVisitor.runTypePropagation(TypeInferenceVisitor.java:103)
    	at jadx.core.dex.visitors.typeinference.TypeInferenceVisitor.visit(TypeInferenceVisitor.java:75)
    Caused by: java.lang.NullPointerException
     */
    /* JADX WARN: Failed to calculate best type for var: r4v25 ??
    jadx.core.utils.exceptions.JadxRuntimeException: Type update failed for variable: r4v25 ??, new type: float
    	at jadx.core.dex.visitors.typeinference.TypeUpdate.apply(TypeUpdate.java:109)
    	at jadx.core.dex.visitors.typeinference.TypeUpdate.apply(TypeUpdate.java:59)
    	at jadx.core.dex.visitors.typeinference.TypeInferenceVisitor.calculateFromBounds(TypeInferenceVisitor.java:147)
    	at jadx.core.dex.visitors.typeinference.TypeInferenceVisitor.setBestType(TypeInferenceVisitor.java:125)
    	at jadx.core.dex.visitors.typeinference.TypeInferenceVisitor.lambda$runTypePropagation$2(TypeInferenceVisitor.java:103)
    	at java.base/java.util.ArrayList.forEach(ArrayList.java:1596)
    	at jadx.core.dex.visitors.typeinference.TypeInferenceVisitor.runTypePropagation(TypeInferenceVisitor.java:103)
    	at jadx.core.dex.visitors.typeinference.TypeInferenceVisitor.visit(TypeInferenceVisitor.java:75)
    Caused by: java.lang.NullPointerException
     */
    /* JADX WARN: Failed to calculate best type for var: r7v30 ??
    jadx.core.utils.exceptions.JadxRuntimeException: Type update failed for variable: r7v30 ??, new type: android.graphics.Matrix
    	at jadx.core.dex.visitors.typeinference.TypeUpdate.apply(TypeUpdate.java:109)
    	at jadx.core.dex.visitors.typeinference.TypeUpdate.apply(TypeUpdate.java:59)
    	at jadx.core.dex.visitors.typeinference.TypeInferenceVisitor.calculateFromBounds(TypeInferenceVisitor.java:147)
    	at jadx.core.dex.visitors.typeinference.TypeInferenceVisitor.setBestType(TypeInferenceVisitor.java:125)
    	at jadx.core.dex.visitors.typeinference.TypeInferenceVisitor.lambda$runTypePropagation$2(TypeInferenceVisitor.java:103)
    	at java.base/java.util.ArrayList.forEach(ArrayList.java:1596)
    	at jadx.core.dex.visitors.typeinference.TypeInferenceVisitor.runTypePropagation(TypeInferenceVisitor.java:103)
    	at jadx.core.dex.visitors.typeinference.TypeInferenceVisitor.visit(TypeInferenceVisitor.java:75)
    Caused by: java.lang.NullPointerException
     */
    /* JADX WARN: Failed to calculate best type for var: r7v31 ??
    jadx.core.utils.exceptions.JadxRuntimeException: Type update failed for variable: r7v31 ??, new type: android.graphics.Matrix
    	at jadx.core.dex.visitors.typeinference.TypeUpdate.apply(TypeUpdate.java:109)
    	at jadx.core.dex.visitors.typeinference.TypeUpdate.apply(TypeUpdate.java:59)
    	at jadx.core.dex.visitors.typeinference.TypeInferenceVisitor.calculateFromBounds(TypeInferenceVisitor.java:147)
    	at jadx.core.dex.visitors.typeinference.TypeInferenceVisitor.setBestType(TypeInferenceVisitor.java:125)
    	at jadx.core.dex.visitors.typeinference.TypeInferenceVisitor.lambda$runTypePropagation$2(TypeInferenceVisitor.java:103)
    	at java.base/java.util.ArrayList.forEach(ArrayList.java:1596)
    	at jadx.core.dex.visitors.typeinference.TypeInferenceVisitor.runTypePropagation(TypeInferenceVisitor.java:103)
    	at jadx.core.dex.visitors.typeinference.TypeInferenceVisitor.visit(TypeInferenceVisitor.java:75)
    Caused by: java.lang.NullPointerException
     */
    /* JADX WARN: Failed to calculate best type for var: r8v16 ??
    jadx.core.utils.exceptions.JadxRuntimeException: Type update failed for variable: r8v16 ??, new type: float
    	at jadx.core.dex.visitors.typeinference.TypeUpdate.apply(TypeUpdate.java:109)
    	at jadx.core.dex.visitors.typeinference.TypeUpdate.apply(TypeUpdate.java:59)
    	at jadx.core.dex.visitors.typeinference.TypeInferenceVisitor.calculateFromBounds(TypeInferenceVisitor.java:147)
    	at jadx.core.dex.visitors.typeinference.TypeInferenceVisitor.setBestType(TypeInferenceVisitor.java:125)
    	at jadx.core.dex.visitors.typeinference.TypeInferenceVisitor.lambda$runTypePropagation$2(TypeInferenceVisitor.java:103)
    	at java.base/java.util.ArrayList.forEach(ArrayList.java:1596)
    	at jadx.core.dex.visitors.typeinference.TypeInferenceVisitor.runTypePropagation(TypeInferenceVisitor.java:103)
    	at jadx.core.dex.visitors.typeinference.TypeInferenceVisitor.visit(TypeInferenceVisitor.java:75)
    Caused by: java.lang.NullPointerException
     */
    /* JADX WARN: Failed to calculate best type for var: r8v5 ??
    jadx.core.utils.exceptions.JadxRuntimeException: Type update failed for variable: r8v5 ??, new type: float
    	at jadx.core.dex.visitors.typeinference.TypeUpdate.apply(TypeUpdate.java:109)
    	at jadx.core.dex.visitors.typeinference.TypeUpdate.apply(TypeUpdate.java:59)
    	at jadx.core.dex.visitors.typeinference.FixTypesVisitor.calculateFromBounds(FixTypesVisitor.java:159)
    	at jadx.core.dex.visitors.typeinference.FixTypesVisitor.setBestType(FixTypesVisitor.java:136)
    	at jadx.core.dex.visitors.typeinference.FixTypesVisitor.deduceType(FixTypesVisitor.java:241)
    	at jadx.core.dex.visitors.typeinference.FixTypesVisitor.tryDeduceTypes(FixTypesVisitor.java:224)
    	at jadx.core.dex.visitors.typeinference.FixTypesVisitor.visit(FixTypesVisitor.java:94)
    Caused by: java.lang.NullPointerException
     */
    /* JADX WARN: Failed to calculate best type for var: r8v5 ??
    jadx.core.utils.exceptions.JadxRuntimeException: Type update failed for variable: r8v5 ??, new type: float
    	at jadx.core.dex.visitors.typeinference.TypeUpdate.apply(TypeUpdate.java:109)
    	at jadx.core.dex.visitors.typeinference.TypeUpdate.apply(TypeUpdate.java:59)
    	at jadx.core.dex.visitors.typeinference.TypeInferenceVisitor.calculateFromBounds(TypeInferenceVisitor.java:147)
    	at jadx.core.dex.visitors.typeinference.TypeInferenceVisitor.setBestType(TypeInferenceVisitor.java:125)
    	at jadx.core.dex.visitors.typeinference.TypeInferenceVisitor.lambda$runTypePropagation$2(TypeInferenceVisitor.java:103)
    	at java.base/java.util.ArrayList.forEach(ArrayList.java:1596)
    	at jadx.core.dex.visitors.typeinference.TypeInferenceVisitor.runTypePropagation(TypeInferenceVisitor.java:103)
    	at jadx.core.dex.visitors.typeinference.TypeInferenceVisitor.visit(TypeInferenceVisitor.java:75)
    Caused by: java.lang.NullPointerException
     */
    /* JADX WARN: Failed to calculate best type for var: r8v6 ??
    jadx.core.utils.exceptions.JadxRuntimeException: Type update failed for variable: r8v6 ??, new type: float
    	at jadx.core.dex.visitors.typeinference.TypeUpdate.apply(TypeUpdate.java:109)
    	at jadx.core.dex.visitors.typeinference.TypeUpdate.apply(TypeUpdate.java:59)
    	at jadx.core.dex.visitors.typeinference.TypeInferenceVisitor.calculateFromBounds(TypeInferenceVisitor.java:147)
    	at jadx.core.dex.visitors.typeinference.TypeInferenceVisitor.setBestType(TypeInferenceVisitor.java:125)
    	at jadx.core.dex.visitors.typeinference.TypeInferenceVisitor.lambda$runTypePropagation$2(TypeInferenceVisitor.java:103)
    	at java.base/java.util.ArrayList.forEach(ArrayList.java:1596)
    	at jadx.core.dex.visitors.typeinference.TypeInferenceVisitor.runTypePropagation(TypeInferenceVisitor.java:103)
    	at jadx.core.dex.visitors.typeinference.TypeInferenceVisitor.visit(TypeInferenceVisitor.java:75)
    Caused by: java.lang.NullPointerException
     */
    /*  JADX ERROR: Types fix failed
        jadx.core.utils.exceptions.JadxRuntimeException: Type update failed for variable: r8v5 ??, new type: char
        	at jadx.core.dex.visitors.typeinference.TypeUpdate.apply(TypeUpdate.java:109)
        	at jadx.core.dex.visitors.typeinference.TypeUpdate.apply(TypeUpdate.java:59)
        	at jadx.core.dex.visitors.typeinference.FixTypesVisitor.tryPossibleTypes(FixTypesVisitor.java:186)
        	at jadx.core.dex.visitors.typeinference.FixTypesVisitor.deduceType(FixTypesVisitor.java:245)
        	at jadx.core.dex.visitors.typeinference.FixTypesVisitor.tryDeduceTypes(FixTypesVisitor.java:224)
        	at jadx.core.dex.visitors.typeinference.FixTypesVisitor.visit(FixTypesVisitor.java:94)
        Caused by: java.lang.NullPointerException
        */
    private boolean touchEvent(android.view.MotionEvent r21) {
        /*
            Method dump skipped, instruction units count: 749
            To view this dump add '--comments-level debug' option
        */
        throw new UnsupportedOperationException("Method not decompiled: org.telegram.ui.Stories.recorder.DualCameraView.touchEvent(android.view.MotionEvent):boolean");
    }

    public boolean isDualTouch() {
        return this.down;
    }

    private void extractPointsData(Matrix matrix) {
        float[] fArr = this.vertices;
        fArr[0] = 0.0f;
        fArr[1] = 0.0f;
        matrix.mapPoints(fArr);
        float[] fArr2 = this.vertices;
        this.cx = fArr2[0];
        this.cy = fArr2[1];
        fArr2[0] = 1.0f;
        fArr2[1] = 0.0f;
        matrix.mapPoints(fArr2);
        float[] fArr3 = this.vertices;
        this.angle = (float) Math.toDegrees(Math.atan2(fArr3[1] - this.cy, fArr3[0] - this.cx));
        float f = this.cx;
        float f2 = this.cy;
        float[] fArr4 = this.vertices;
        this.w = MathUtils.distance(f, f2, fArr4[0], fArr4[1]) * 2.0f;
        float[] fArr5 = this.vertices;
        fArr5[0] = 0.0f;
        fArr5[1] = 1.0f;
        matrix.mapPoints(fArr5);
        float f3 = this.cx;
        float f4 = this.cy;
        float[] fArr6 = this.vertices;
        this.h = MathUtils.distance(f3, f4, fArr6[0], fArr6[1]) * 2.0f;
    }

    public boolean isPointInsideDual(Matrix matrix, float f, float f2) {
        if (this.verticesSrc == null) {
            this.verticesSrc = new float[8];
        }
        if (this.verticesDst == null) {
            this.verticesDst = new float[8];
        }
        int dualShape = getDualShape() % 3;
        float f3 = (dualShape == 0 || dualShape == 1 || dualShape == 3) ? 0.5625f : 1.0f;
        float[] fArr = this.verticesSrc;
        fArr[0] = -1.0f;
        float f4 = -f3;
        fArr[1] = f4;
        fArr[2] = 1.0f;
        fArr[3] = f4;
        fArr[4] = 1.0f;
        fArr[5] = f3;
        fArr[6] = -1.0f;
        fArr[7] = f3;
        matrix.mapPoints(this.verticesDst, fArr);
        float[] fArr2 = this.verticesDst;
        float f5 = fArr2[0];
        float f6 = fArr2[2];
        float f7 = (f5 - f6) * (f5 - f6);
        float f8 = fArr2[1];
        float f9 = fArr2[3];
        double dSqrt = Math.sqrt(f7 + ((f8 - f9) * (f8 - f9)));
        float[] fArr3 = this.verticesDst;
        float f10 = fArr3[2];
        float f11 = fArr3[4];
        float f12 = (f10 - f11) * (f10 - f11);
        float f13 = fArr3[3];
        float f14 = fArr3[5];
        double dSqrt2 = Math.sqrt(f12 + ((f13 - f14) * (f13 - f14)));
        float[] fArr4 = this.verticesDst;
        float f15 = fArr4[4];
        float f16 = fArr4[6];
        float f17 = (f15 - f16) * (f15 - f16);
        float f18 = fArr4[5];
        float f19 = fArr4[7];
        double dSqrt3 = Math.sqrt(f17 + ((f18 - f19) * (f18 - f19)));
        float[] fArr5 = this.verticesDst;
        float f20 = fArr5[6];
        float f21 = fArr5[0];
        float f22 = (f20 - f21) * (f20 - f21);
        float f23 = fArr5[7];
        float f24 = fArr5[1];
        double dSqrt4 = Math.sqrt(f22 + ((f23 - f24) * (f23 - f24)));
        float[] fArr6 = this.verticesDst;
        float f25 = fArr6[0];
        float f26 = fArr6[1];
        double dSqrt5 = Math.sqrt(((f25 - f) * (f25 - f)) + ((f26 - f2) * (f26 - f2)));
        float[] fArr7 = this.verticesDst;
        float f27 = fArr7[2];
        float f28 = fArr7[3];
        double dSqrt6 = Math.sqrt(((f27 - f) * (f27 - f)) + ((f28 - f2) * (f28 - f2)));
        float[] fArr8 = this.verticesDst;
        float f29 = fArr8[4];
        float f30 = fArr8[5];
        double dSqrt7 = Math.sqrt(((f29 - f) * (f29 - f)) + ((f30 - f2) * (f30 - f2)));
        float[] fArr9 = this.verticesDst;
        float f31 = fArr9[6];
        float f32 = fArr9[7];
        double dSqrt8 = Math.sqrt(((f31 - f) * (f31 - f)) + ((f32 - f2) * (f32 - f2)));
        double d = ((dSqrt + dSqrt5) + dSqrt6) / 2.0d;
        double d2 = ((dSqrt2 + dSqrt6) + dSqrt7) / 2.0d;
        double d3 = ((dSqrt3 + dSqrt7) + dSqrt8) / 2.0d;
        double d4 = ((dSqrt4 + dSqrt8) + dSqrt5) / 2.0d;
        return (((Math.sqrt((((d - dSqrt) * d) * (d - dSqrt5)) * (d - dSqrt6)) + Math.sqrt((((d2 - dSqrt2) * d2) * (d2 - dSqrt6)) * (d2 - dSqrt7))) + Math.sqrt((((d3 - dSqrt3) * d3) * (d3 - dSqrt7)) * (d3 - dSqrt8))) + Math.sqrt((((d4 - dSqrt4) * d4) * (d4 - dSqrt8)) * (d4 - dSqrt5))) - (dSqrt * dSqrt2) < 1.0d;
    }

    @Override // org.telegram.messenger.camera.CameraView, org.telegram.messenger.camera.CameraController.ErrorCallback
    public void onError(int i, Camera camera, CameraSessionWrapper cameraSessionWrapper) {
        if (isDual()) {
            if (!dualAvailableDefault(getContext(), false)) {
                SharedPreferences.Editor editorEdit = MessagesController.getGlobalMainSettings().edit();
                this.dualAvailable = false;
                editorEdit.putBoolean("dual_available", false).apply();
                new AlertDialog.Builder(getContext()).setTitle(LocaleController.getString(R.string.DualErrorTitle)).setMessage(LocaleController.getString(R.string.DualErrorMessage)).setPositiveButton(LocaleController.getString(R.string.OK), null).show();
            }
            log(false);
            toggleDual();
        }
        if (getCameraSession(0) != null && getCameraSession(0).equals(cameraSessionWrapper)) {
            resetCamera();
        }
        onCameraError();
    }

    protected void onCameraError() {
        resetSaved();
    }

    public boolean dualAvailable() {
        return this.dualAvailable;
    }

    public static boolean dualAvailableDefault(Context context, boolean z) {
        int i = 0;
        boolean z2 = SharedConfig.getDevicePerformanceClass() >= 1 && Camera.getNumberOfCameras() > 1 && SharedConfig.allowPreparingHevcPlayers();
        if (!z2) {
            return z2;
        }
        boolean z3 = context != null && context.getPackageManager().hasSystemFeature("android.hardware.camera.concurrent");
        if (!z3 && z) {
            int iHashCode = (Build.MANUFACTURER + " " + Build.DEVICE).toUpperCase().hashCode();
            int i2 = 0;
            while (true) {
                int[] iArr = dualWhitelistByDevice;
                if (i2 >= iArr.length) {
                    break;
                }
                if (iArr[i2] == iHashCode) {
                    z3 = true;
                    break;
                }
                i2++;
            }
            if (!z3) {
                int iHashCode2 = (Build.MANUFACTURER + Build.MODEL).toUpperCase().hashCode();
                while (true) {
                    int[] iArr2 = dualWhitelistByModel;
                    if (i >= iArr2.length) {
                        break;
                    }
                    if (iArr2[i] == iHashCode2) {
                        return true;
                    }
                    i++;
                }
            }
        }
        return z3;
    }

    public static boolean dualAvailableStatic(Context context) {
        return MessagesController.getGlobalMainSettings().getBoolean("dual_available", dualAvailableDefault(context, true));
    }

    public static boolean roundDualAvailableStatic(Context context) {
        return MessagesController.getGlobalMainSettings().getBoolean("rounddual_available", roundDualAvailableDefault(context));
    }

    public static boolean roundDualAvailableDefault(Context context) {
        return SharedConfig.getDevicePerformanceClass() >= 1 && Camera.getNumberOfCameras() > 1 && SharedConfig.allowPreparingHevcPlayers() && context != null && context.getPackageManager().hasSystemFeature("android.hardware.camera.concurrent");
    }

    private Matrix getSavedDualMatrix() {
        String string = MessagesController.getGlobalMainSettings().getString("dualmatrix", null);
        if (string == null) {
            return null;
        }
        String[] strArrSplit = string.split(";");
        if (strArrSplit.length != 9) {
            return null;
        }
        float[] fArr = new float[9];
        for (int i = 0; i < strArrSplit.length; i++) {
            try {
                fArr[i] = Float.parseFloat(strArrSplit[i]);
            } catch (Exception e) {
                FileLog.e(e);
                return null;
            }
        }
        Matrix matrix = new Matrix();
        matrix.setValues(fArr);
        return matrix;
    }

    public boolean isSavedDual() {
        return dualAvailableStatic(getContext()) && MessagesController.getGlobalMainSettings().getBoolean("dualcam", dualAvailableDefault(ApplicationLoader.applicationContext, false));
    }

    private void resetSavedDual() {
        MessagesController.getGlobalMainSettings().edit().putBoolean("dualcam", false).remove("dualmatrix").apply();
    }

    private void saveDual() {
        SharedPreferences.Editor editorEdit = MessagesController.getGlobalMainSettings().edit();
        editorEdit.putBoolean("dualcam", isDual());
        if (isDual()) {
            float[] fArr = new float[9];
            getDualPosition().getValues(fArr);
            editorEdit.putString("dualmatrix", Floats.join(";", fArr));
        } else {
            editorEdit.remove("dualmatrix");
        }
        editorEdit.apply();
    }
}
