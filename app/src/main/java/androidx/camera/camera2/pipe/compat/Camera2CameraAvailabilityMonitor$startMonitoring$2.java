package androidx.camera.camera2.pipe.compat;

import androidx.camera.camera2.pipe.CameraId;
import androidx.camera.camera2.pipe.core.Log;
import java.util.Iterator;
import java.util.concurrent.CopyOnWriteArrayList;
import kotlin.ResultKt;
import kotlin.Unit;
import kotlin.coroutines.Continuation;
import kotlin.coroutines.intrinsics.IntrinsicsKt;
import kotlin.coroutines.jvm.internal.Boxing;
import kotlin.coroutines.jvm.internal.SuspendLambda;
import kotlin.jvm.functions.Function2;
import kotlin.jvm.internal.Intrinsics;
import kotlinx.coroutines.BuildersKt__Builders_commonKt;
import kotlinx.coroutines.CompletableDeferred;
import kotlinx.coroutines.CompletableDeferredKt;
import kotlinx.coroutines.CoroutineScope;
import kotlinx.coroutines.CoroutineScopeKt;
import kotlinx.coroutines.SupervisorKt;
import kotlinx.coroutines.TimeoutKt;
import kotlinx.coroutines.flow.Flow;
import kotlinx.coroutines.flow.FlowCollector;

public final class Camera2CameraAvailabilityMonitor$startMonitoring$2 implements CameraAvailabilityMonitor.Session {
    private final CopyOnWriteArrayList listeners;
    private final CoroutineScope scope;

    Camera2CameraAvailabilityMonitor$startMonitoring$2(Camera2CameraAvailabilityMonitor camera2CameraAvailabilityMonitor, String str) {
        CoroutineScope CoroutineScope = CoroutineScopeKt.CoroutineScope(camera2CameraAvailabilityMonitor.threads.getBackgroundDispatcher().plus(SupervisorKt.SupervisorJob(camera2CameraAvailabilityMonitor.cameraPipeJob)));
        this.scope = CoroutineScope;
        this.listeners = new CopyOnWriteArrayList();
        BuildersKt__Builders_commonKt.launch$default(CoroutineScope, null, null, new AnonymousClass1(camera2CameraAvailabilityMonitor, str, this, null), 3, null);
    }

    /* JADX INFO: renamed from: androidx.camera.camera2.pipe.compat.Camera2CameraAvailabilityMonitor$startMonitoring$2$1, reason: invalid class name */
    static final class AnonymousClass1 extends SuspendLambda implements Function2 {
        final /* synthetic */ String $cameraId;
        int label;
        final /* synthetic */ Camera2CameraAvailabilityMonitor this$0;
        final /* synthetic */ Camera2CameraAvailabilityMonitor$startMonitoring$2 this$1;

        /* JADX WARN: 'super' call moved to the top of the method (can break code semantics) */
        AnonymousClass1(Camera2CameraAvailabilityMonitor camera2CameraAvailabilityMonitor, String str, Camera2CameraAvailabilityMonitor$startMonitoring$2 camera2CameraAvailabilityMonitor$startMonitoring$2, Continuation continuation) {
            super(2, continuation);
            this.this$0 = camera2CameraAvailabilityMonitor;
            this.$cameraId = str;
            this.this$1 = camera2CameraAvailabilityMonitor$startMonitoring$2;
        }

        @Override // kotlin.coroutines.jvm.internal.BaseContinuationImpl
        public final Continuation create(Object obj, Continuation continuation) {
            return new AnonymousClass1(this.this$0, this.$cameraId, this.this$1, continuation);
        }

        @Override // kotlin.jvm.functions.Function2
        public final Object invoke(CoroutineScope coroutineScope, Continuation continuation) {
            return ((AnonymousClass1) create(coroutineScope, continuation)).invokeSuspend(Unit.INSTANCE);
        }

        @Override // kotlin.coroutines.jvm.internal.BaseContinuationImpl
        public final Object invokeSuspend(Object obj) {
            Object coroutine_suspended = IntrinsicsKt.getCOROUTINE_SUSPENDED();
            int i = this.label;
            if (i == 0) {
                ResultKt.throwOnFailure(obj);
                Flow flow = this.this$0.availableCameraFlow;
                final String str = this.$cameraId;
                final Camera2CameraAvailabilityMonitor$startMonitoring$2 camera2CameraAvailabilityMonitor$startMonitoring$2 = this.this$1;
                FlowCollector flowCollector = new FlowCollector() { // from class: androidx.camera.camera2.pipe.compat.Camera2CameraAvailabilityMonitor.startMonitoring.2.1.1
                    @Override // kotlinx.coroutines.flow.FlowCollector
                    public /* bridge */ /* synthetic */ Object emit(Object obj2, Continuation continuation) {
                        return m444emit0r8Bogc(((CameraId) obj2).m239unboximpl(), continuation);
                    }

                    /* JADX INFO: renamed from: emit-0r8Bogc, reason: not valid java name */
                    public final Object m444emit0r8Bogc(String str2, Continuation continuation) {
                        if (CameraId.m236equalsimpl0(str2, str)) {
                            if (Log.INSTANCE.getDEBUG_LOGGABLE()) {
                                android.util.Log.d("CXCP", ((Object) CameraId.m238toStringimpl(str2)) + " has become available! Notifying listeners...");
                            }
                            Iterator it = camera2CameraAvailabilityMonitor$startMonitoring$2.listeners.iterator();
                            Intrinsics.checkNotNullExpressionValue(it, "iterator(...)");
                            while (it.hasNext()) {
                                ((CompletableDeferred) it.next()).complete(Unit.INSTANCE);
                            }
                        }
                        return Unit.INSTANCE;
                    }
                };
                this.label = 1;
                if (flow.collect(flowCollector, this) == coroutine_suspended) {
                    return coroutine_suspended;
                }
            } else {
                if (i != 1) {
                    throw new IllegalStateException("call to 'resume' before 'invoke' with coroutine");
                }
                ResultKt.throwOnFailure(obj);
            }
            return Unit.INSTANCE;
        }
    }

    /* JADX WARN: Code duplicated, block: B:7:0x0013  */
    @Override // androidx.camera.camera2.pipe.compat.CameraAvailabilityMonitor.Session
    public Object awaitAvailableCamera(long j, Continuation continuation) {
        Camera2CameraAvailabilityMonitor$startMonitoring$2$awaitAvailableCamera$1 camera2CameraAvailabilityMonitor$startMonitoring$2$awaitAvailableCamera$1;
        CompletableDeferred completableDeferred;
        if (continuation instanceof Camera2CameraAvailabilityMonitor$startMonitoring$2$awaitAvailableCamera$1) {
            camera2CameraAvailabilityMonitor$startMonitoring$2$awaitAvailableCamera$1 = (Camera2CameraAvailabilityMonitor$startMonitoring$2$awaitAvailableCamera$1) continuation;
            int i = camera2CameraAvailabilityMonitor$startMonitoring$2$awaitAvailableCamera$1.label;
            if ((i & Integer.MIN_VALUE) != 0) {
                camera2CameraAvailabilityMonitor$startMonitoring$2$awaitAvailableCamera$1.label = i - Integer.MIN_VALUE;
            } else {
                camera2CameraAvailabilityMonitor$startMonitoring$2$awaitAvailableCamera$1 = new Camera2CameraAvailabilityMonitor$startMonitoring$2$awaitAvailableCamera$1(this, continuation);
            }
        } else {
            camera2CameraAvailabilityMonitor$startMonitoring$2$awaitAvailableCamera$1 = new Camera2CameraAvailabilityMonitor$startMonitoring$2$awaitAvailableCamera$1(this, continuation);
        }
        Object objWithTimeoutOrNull = camera2CameraAvailabilityMonitor$startMonitoring$2$awaitAvailableCamera$1.result;
        Object coroutine_suspended = IntrinsicsKt.getCOROUTINE_SUSPENDED();
        int i2 = camera2CameraAvailabilityMonitor$startMonitoring$2$awaitAvailableCamera$1.label;
        if (i2 == 0) {
            ResultKt.throwOnFailure(objWithTimeoutOrNull);
            CompletableDeferred completableDeferredCompletableDeferred$default = CompletableDeferredKt.CompletableDeferred$default(null, 1, null);
            this.listeners.add(completableDeferredCompletableDeferred$default);
            Camera2CameraAvailabilityMonitor$startMonitoring$2$awaitAvailableCamera$success$1 camera2CameraAvailabilityMonitor$startMonitoring$2$awaitAvailableCamera$success$1 = new Camera2CameraAvailabilityMonitor$startMonitoring$2$awaitAvailableCamera$success$1(completableDeferredCompletableDeferred$default, null);
            camera2CameraAvailabilityMonitor$startMonitoring$2$awaitAvailableCamera$1.L$0 = completableDeferredCompletableDeferred$default;
            camera2CameraAvailabilityMonitor$startMonitoring$2$awaitAvailableCamera$1.label = 1;
            objWithTimeoutOrNull = TimeoutKt.withTimeoutOrNull(j, camera2CameraAvailabilityMonitor$startMonitoring$2$awaitAvailableCamera$success$1, camera2CameraAvailabilityMonitor$startMonitoring$2$awaitAvailableCamera$1);
            if (objWithTimeoutOrNull == coroutine_suspended) {
                return coroutine_suspended;
            }
            completableDeferred = completableDeferredCompletableDeferred$default;
        } else {
            if (i2 != 1) {
                throw new IllegalStateException("call to 'resume' before 'invoke' with coroutine");
            }
            completableDeferred = (CompletableDeferred) camera2CameraAvailabilityMonitor$startMonitoring$2$awaitAvailableCamera$1.L$0;
            ResultKt.throwOnFailure(objWithTimeoutOrNull);
        }
        boolean z = objWithTimeoutOrNull != null;
        this.listeners.remove(completableDeferred);
        return Boxing.boxBoolean(z);
    }

    @Override // java.lang.AutoCloseable
    public void close() {
        CoroutineScopeKt.cancel$default(this.scope, null, 1, null);
    }
}
