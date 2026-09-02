package androidx.camera.camera2.impl;

import androidx.camera.camera2.adapter.CoroutineAdaptersKt;
import androidx.camera.core.CameraControl;
import kotlin.ResultKt;
import kotlin.Unit;
import kotlin.coroutines.Continuation;
import kotlin.coroutines.intrinsics.IntrinsicsKt;
import kotlin.coroutines.jvm.internal.Boxing;
import kotlin.coroutines.jvm.internal.SuspendLambda;
import kotlin.jvm.functions.Function1;
import kotlin.jvm.functions.Function2;
import kotlin.jvm.internal.Intrinsics;
import kotlinx.coroutines.CompletableDeferred;
import kotlinx.coroutines.CoroutineScope;
import kotlinx.coroutines.Deferred;

public final class LowLightBoostControl$setLowLightBoostAsync$$inlined$confineLaunch$1 extends SuspendLambda implements Function2 {
    final /* synthetic */ boolean $cancelPreviousTask$inlined;
    final /* synthetic */ boolean $lowLightBoost$inlined;
    final /* synthetic */ CompletableDeferred $signal$inlined;
    int label;
    final /* synthetic */ LowLightBoostControl this$0;

    /* JADX WARN: 'super' call moved to the top of the method (can break code semantics) */
    public LowLightBoostControl$setLowLightBoostAsync$$inlined$confineLaunch$1(Continuation continuation, LowLightBoostControl lowLightBoostControl, CompletableDeferred completableDeferred, boolean z, boolean z2) {
        super(2, continuation);
        this.this$0 = lowLightBoostControl;
        this.$signal$inlined = completableDeferred;
        this.$lowLightBoost$inlined = z;
        this.$cancelPreviousTask$inlined = z2;
    }

    @Override // kotlin.coroutines.jvm.internal.BaseContinuationImpl
    public final Continuation create(Object obj, Continuation continuation) {
        return new LowLightBoostControl$setLowLightBoostAsync$$inlined$confineLaunch$1(continuation, this.this$0, this.$signal$inlined, this.$lowLightBoost$inlined, this.$cancelPreviousTask$inlined);
    }

    @Override // kotlin.jvm.functions.Function2
    public final Object invoke(CoroutineScope coroutineScope, Continuation continuation) {
        return ((LowLightBoostControl$setLowLightBoostAsync$$inlined$confineLaunch$1) create(coroutineScope, continuation)).invokeSuspend(Unit.INSTANCE);
    }

    /* JADX WARN: Code duplicated, block: B:17:0x0037  */
    /* JADX WARN: Code duplicated, block: B:18:0x0050  */
    /* JADX WARN: Code duplicated, block: B:20:0x005b  */
    /* JADX WARN: Code duplicated, block: B:23:0x006c  */
    /* JADX WARN: Code duplicated, block: B:25:0x0070  */
    /* JADX WARN: Code duplicated, block: B:28:0x007d  */
    /* JADX WARN: Code duplicated, block: B:29:0x0083  */
    /* JADX WARN: Code duplicated, block: B:31:0x008b  */
    /* JADX WARN: Code duplicated, block: B:34:0x00a1  */
    /* JADX WARN: Code duplicated, block: B:35:0x00a7  */
    /* JADX WARN: Code duplicated, block: B:38:0x00c0  */
    @Override // kotlin.coroutines.jvm.internal.BaseContinuationImpl
    public final Object invokeSuspend(Object obj) {
        boolean zBooleanValue;
        CompletableDeferred completableDeferred;
        Integer numBoxInt;
        final CompletableDeferred completableDeferred2;
        final LowLightBoostControl lowLightBoostControl;
        Object coroutine_suspended = IntrinsicsKt.getCOROUTINE_SUSPENDED();
        int i = this.label;
        if (i == 0) {
            ResultKt.throwOnFailure(obj);
            Deferred checkFrameRateJob$camera_camera2 = this.this$0.getCheckFrameRateJob$camera_camera2();
            if (checkFrameRateJob$camera_camera2 != null) {
                this.label = 1;
                obj = checkFrameRateJob$camera_camera2.await(this);
                if (obj == coroutine_suspended) {
                    return coroutine_suspended;
                }
            } else {
                zBooleanValue = false;
            }
            if (zBooleanValue) {
                LowLightBoostControl lowLightBoostControl2 = this.this$0;
                lowLightBoostControl2.setLiveDataValue(lowLightBoostControl2._lowLightBoostState, -1);
                this.this$0.createFailureResult(this.$signal$inlined, new IllegalStateException("Low Light Boost is disabled when expected frame rate range exceeds 30."));
            } else {
                this.this$0.isLowLightBoostOn = this.$lowLightBoost$inlined;
                if (!this.$lowLightBoost$inlined) {
                    LowLightBoostControl lowLightBoostControl3 = this.this$0;
                    lowLightBoostControl3.setLiveDataValue(lowLightBoostControl3._lowLightBoostState, -1);
                }
                if (this.this$0.getRequestControl() == null) {
                    this.this$0.createFailureResult(this.$signal$inlined, new CameraControl.OperationCanceledException("Camera is not active."));
                    Unit unit = Unit.INSTANCE;
                } else {
                    if (this.$lowLightBoost$inlined) {
                        LowLightBoostControl lowLightBoostControl4 = this.this$0;
                        lowLightBoostControl4.setLiveDataValue(lowLightBoostControl4._lowLightBoostState, 0);
                    }
                    if (this.$cancelPreviousTask$inlined) {
                        this.this$0.stopRunningTaskInternal();
                    } else {
                        completableDeferred = this.this$0._updateSignal;
                        if (completableDeferred != null) {
                            CoroutineAdaptersKt.propagateTo(this.$signal$inlined, completableDeferred);
                        }
                    }
                    this.this$0._updateSignal = this.$signal$inlined;
                    State3AControl state3AControl = this.this$0.state3AControl;
                    if (this.$lowLightBoost$inlined) {
                        numBoxInt = Boxing.boxInt(6);
                    } else {
                        numBoxInt = null;
                    }
                    CoroutineAdaptersKt.propagateTo(state3AControl.setPreferredAeModeAsync(numBoxInt), this.$signal$inlined);
                    completableDeferred2 = this.$signal$inlined;
                    lowLightBoostControl = this.this$0;
                    if (completableDeferred2.invokeOnCompletion(new Function1() { // from class: androidx.camera.camera2.impl.LowLightBoostControl$setLowLightBoostAsync$2$1$2
                        @Override // kotlin.jvm.functions.Function1
                        public /* bridge */ /* synthetic */ Object invoke(Object obj2) {
                            invoke((Throwable) obj2);
                            return Unit.INSTANCE;
                        }

                        public final void invoke(Throwable th) {
                            if (Intrinsics.areEqual(completableDeferred2, lowLightBoostControl._updateSignal)) {
                                lowLightBoostControl._updateSignal = null;
                            }
                        }
                    }) == null) {
                        this.this$0.createFailureResult(this.$signal$inlined, new CameraControl.OperationCanceledException("Camera is not active."));
                        Unit unit2 = Unit.INSTANCE;
                    }
                }
            }
            return Unit.INSTANCE;
        }
        if (i != 1) {
            throw new IllegalStateException("call to 'resume' before 'invoke' with coroutine");
        }
        ResultKt.throwOnFailure(obj);
        zBooleanValue = ((Boolean) obj).booleanValue();
        if (zBooleanValue) {
            LowLightBoostControl lowLightBoostControl5 = this.this$0;
            lowLightBoostControl5.setLiveDataValue(lowLightBoostControl5._lowLightBoostState, -1);
            this.this$0.createFailureResult(this.$signal$inlined, new IllegalStateException("Low Light Boost is disabled when expected frame rate range exceeds 30."));
        } else {
            this.this$0.isLowLightBoostOn = this.$lowLightBoost$inlined;
            if (!this.$lowLightBoost$inlined) {
                LowLightBoostControl lowLightBoostControl6 = this.this$0;
                lowLightBoostControl6.setLiveDataValue(lowLightBoostControl6._lowLightBoostState, -1);
            }
            if (this.this$0.getRequestControl() == null) {
                this.this$0.createFailureResult(this.$signal$inlined, new CameraControl.OperationCanceledException("Camera is not active."));
                Unit unit3 = Unit.INSTANCE;
            } else {
                if (this.$lowLightBoost$inlined) {
                    LowLightBoostControl lowLightBoostControl7 = this.this$0;
                    lowLightBoostControl7.setLiveDataValue(lowLightBoostControl7._lowLightBoostState, 0);
                }
                if (this.$cancelPreviousTask$inlined) {
                    this.this$0.stopRunningTaskInternal();
                } else {
                    completableDeferred = this.this$0._updateSignal;
                    if (completableDeferred != null) {
                        CoroutineAdaptersKt.propagateTo(this.$signal$inlined, completableDeferred);
                    }
                }
                this.this$0._updateSignal = this.$signal$inlined;
                State3AControl state3AControl2 = this.this$0.state3AControl;
                if (this.$lowLightBoost$inlined) {
                    numBoxInt = Boxing.boxInt(6);
                } else {
                    numBoxInt = null;
                }
                CoroutineAdaptersKt.propagateTo(state3AControl2.setPreferredAeModeAsync(numBoxInt), this.$signal$inlined);
                completableDeferred2 = this.$signal$inlined;
                lowLightBoostControl = this.this$0;
                if (completableDeferred2.invokeOnCompletion(new Function1() { // from class: androidx.camera.camera2.impl.LowLightBoostControl$setLowLightBoostAsync$2$1$2
                    @Override // kotlin.jvm.functions.Function1
                    public /* bridge */ /* synthetic */ Object invoke(Object obj2) {
                        invoke((Throwable) obj2);
                        return Unit.INSTANCE;
                    }

                    public final void invoke(Throwable th) {
                        if (Intrinsics.areEqual(completableDeferred2, lowLightBoostControl._updateSignal)) {
                            lowLightBoostControl._updateSignal = null;
                        }
                    }
                }) == null) {
                    this.this$0.createFailureResult(this.$signal$inlined, new CameraControl.OperationCanceledException("Camera is not active."));
                    Unit unit4 = Unit.INSTANCE;
                }
            }
        }
        return Unit.INSTANCE;
    }
}
