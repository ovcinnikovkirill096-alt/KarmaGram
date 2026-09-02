package androidx.camera.camera2.adapter;

import android.content.Context;
import android.hardware.camera2.CameraManager;
import android.util.Log;
import androidx.camera.camera2.pipe.CameraId;
import androidx.camera.core.CameraIdentifier;
import androidx.camera.core.impl.AbstractCameraPresenceSource;
import androidx.concurrent.futures.CallbackToFutureAdapter;
import androidx.concurrent.futures.ListenableFutureKt;
import com.google.common.util.concurrent.ListenableFuture;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.concurrent.atomic.AtomicBoolean;
import kotlin.ResultKt;
import kotlin.Unit;
import kotlin.collections.CollectionsKt;
import kotlin.coroutines.Continuation;
import kotlin.coroutines.intrinsics.IntrinsicsKt;
import kotlin.coroutines.jvm.internal.Boxing;
import kotlin.coroutines.jvm.internal.ContinuationImpl;
import kotlin.coroutines.jvm.internal.SuspendLambda;
import kotlin.jvm.functions.Function2;
import kotlin.jvm.functions.Function3;
import kotlin.jvm.internal.DefaultConstructorMarker;
import kotlin.jvm.internal.Intrinsics;
import kotlin.jvm.internal.Ref$BooleanRef;
import kotlinx.coroutines.BuildersKt__Builders_commonKt;
import kotlinx.coroutines.CoroutineScope;
import kotlinx.coroutines.Job;
import kotlinx.coroutines.flow.Flow;
import kotlinx.coroutines.flow.FlowCollector;
import kotlinx.coroutines.flow.FlowKt;

public final class PipeCameraPresenceSource extends AbstractCameraPresenceSource {
    public static final Companion Companion = new Companion(null);
    private final CameraManager cameraManager;
    private final CoroutineScope coroutineScope;
    private Job flowCollectionJob;
    private final Flow idFlow;
    private final AtomicBoolean isMonitoring;

    /* JADX WARN: 'super' call moved to the top of the method (can break code semantics) */
    public PipeCameraPresenceSource(Flow idFlow, CoroutineScope coroutineScope, List initialCameraIds, Context context) {
        super(initialCameraIds);
        Intrinsics.checkNotNullParameter(idFlow, "idFlow");
        Intrinsics.checkNotNullParameter(coroutineScope, "coroutineScope");
        Intrinsics.checkNotNullParameter(initialCameraIds, "initialCameraIds");
        Intrinsics.checkNotNullParameter(context, "context");
        this.idFlow = idFlow;
        this.coroutineScope = coroutineScope;
        this.isMonitoring = new AtomicBoolean(false);
        Object systemService = context.getSystemService("camera");
        Intrinsics.checkNotNull(systemService, "null cannot be cast to non-null type android.hardware.camera2.CameraManager");
        this.cameraManager = (CameraManager) systemService;
    }

    @Override // androidx.camera.core.impl.AbstractCameraPresenceSource
    protected void startMonitoring() {
        if (!this.isMonitoring.compareAndSet(false, true)) {
            Log.i("PipePresenceSrc", "Monitoring is already active. Ignoring redundant start call.");
            return;
        }
        Log.i("PipePresenceSrc", "Starting to collect camera ID flow.");
        Job job = this.flowCollectionJob;
        if (job != null) {
            Job.DefaultImpls.cancel$default(job, null, 1, null);
        }
        Ref$BooleanRef ref$BooleanRef = new Ref$BooleanRef();
        ref$BooleanRef.element = true;
        final Flow flow = this.idFlow;
        this.flowCollectionJob = FlowKt.launchIn(FlowKt.m2522catch(FlowKt.onEach(new Flow() { // from class: androidx.camera.camera2.adapter.PipeCameraPresenceSource$startMonitoring$$inlined$map$1

            /* JADX INFO: renamed from: androidx.camera.camera2.adapter.PipeCameraPresenceSource$startMonitoring$$inlined$map$1$2, reason: invalid class name */
            public static final class AnonymousClass2 implements FlowCollector {
                final /* synthetic */ FlowCollector $this_unsafeFlow;

                /* JADX INFO: renamed from: androidx.camera.camera2.adapter.PipeCameraPresenceSource$startMonitoring$$inlined$map$1$2$1, reason: invalid class name */
                public static final class AnonymousClass1 extends ContinuationImpl {
                    int label;
                    /* synthetic */ Object result;

                    public AnonymousClass1(Continuation continuation) {
                        super(continuation);
                    }

                    @Override // kotlin.coroutines.jvm.internal.BaseContinuationImpl
                    public final Object invokeSuspend(Object obj) {
                        this.result = obj;
                        this.label |= Integer.MIN_VALUE;
                        return AnonymousClass2.this.emit(null, this);
                    }
                }

                public AnonymousClass2(FlowCollector flowCollector) {
                    this.$this_unsafeFlow = flowCollector;
                }

                /* JADX WARN: Code duplicated, block: B:7:0x0013  */
                @Override // kotlinx.coroutines.flow.FlowCollector
                public final Object emit(Object obj, Continuation continuation) {
                    AnonymousClass1 anonymousClass1;
                    if (continuation instanceof AnonymousClass1) {
                        anonymousClass1 = (AnonymousClass1) continuation;
                        int i = anonymousClass1.label;
                        if ((i & Integer.MIN_VALUE) != 0) {
                            anonymousClass1.label = i - Integer.MIN_VALUE;
                        } else {
                            anonymousClass1 = new AnonymousClass1(continuation);
                        }
                    } else {
                        anonymousClass1 = new AnonymousClass1(continuation);
                    }
                    Object obj2 = anonymousClass1.result;
                    Object coroutine_suspended = IntrinsicsKt.getCOROUTINE_SUSPENDED();
                    int i2 = anonymousClass1.label;
                    if (i2 == 0) {
                        ResultKt.throwOnFailure(obj2);
                        FlowCollector flowCollector = this.$this_unsafeFlow;
                        ArrayList arrayList = new ArrayList();
                        Iterator it = ((List) obj).iterator();
                        while (it.hasNext()) {
                            String strM239unboximpl = ((CameraId) it.next()).m239unboximpl();
                            CameraIdentifier cameraIdentifierCreate$default = null;
                            try {
                                cameraIdentifierCreate$default = CameraIdentifier.Factory.create$default(strM239unboximpl, null, null, 6, null);
                            } catch (Exception e) {
                                Log.w("PipePresenceSrc", "Failed to create CameraIdentifier for pipeId: " + strM239unboximpl, e);
                            }
                            if (cameraIdentifierCreate$default != null) {
                                arrayList.add(cameraIdentifierCreate$default);
                            }
                        }
                        anonymousClass1.label = 1;
                        if (flowCollector.emit(arrayList, anonymousClass1) == coroutine_suspended) {
                            return coroutine_suspended;
                        }
                    } else {
                        if (i2 != 1) {
                            throw new IllegalStateException("call to 'resume' before 'invoke' with coroutine");
                        }
                        ResultKt.throwOnFailure(obj2);
                    }
                    return Unit.INSTANCE;
                }
            }

            @Override // kotlinx.coroutines.flow.Flow
            public Object collect(FlowCollector flowCollector, Continuation continuation) {
                Object objCollect = flow.collect(new AnonymousClass2(flowCollector), continuation);
                return objCollect == IntrinsicsKt.getCOROUTINE_SUSPENDED() ? objCollect : Unit.INSTANCE;
            }
        }, new AnonymousClass2(ref$BooleanRef, null)), new AnonymousClass3(null)), this.coroutineScope);
    }

    /* JADX INFO: renamed from: androidx.camera.camera2.adapter.PipeCameraPresenceSource$startMonitoring$2, reason: invalid class name */
    static final class AnonymousClass2 extends SuspendLambda implements Function2 {
        final /* synthetic */ Ref$BooleanRef $isFirstEmission;
        /* synthetic */ Object L$0;
        int label;

        /* JADX WARN: 'super' call moved to the top of the method (can break code semantics) */
        AnonymousClass2(Ref$BooleanRef ref$BooleanRef, Continuation continuation) {
            super(2, continuation);
            this.$isFirstEmission = ref$BooleanRef;
        }

        @Override // kotlin.coroutines.jvm.internal.BaseContinuationImpl
        public final Continuation create(Object obj, Continuation continuation) {
            AnonymousClass2 anonymousClass2 = PipeCameraPresenceSource.this.new AnonymousClass2(this.$isFirstEmission, continuation);
            anonymousClass2.L$0 = obj;
            return anonymousClass2;
        }

        @Override // kotlin.jvm.functions.Function2
        public final Object invoke(List list, Continuation continuation) {
            return ((AnonymousClass2) create(list, continuation)).invokeSuspend(Unit.INSTANCE);
        }

        @Override // kotlin.coroutines.jvm.internal.BaseContinuationImpl
        public final Object invokeSuspend(Object obj) {
            Object coroutine_suspended = IntrinsicsKt.getCOROUTINE_SUSPENDED();
            int i = this.label;
            if (i == 0) {
                ResultKt.throwOnFailure(obj);
                List list = (List) this.L$0;
                Log.d("PipePresenceSrc", "Flow emitted new camera set: " + CollectionsKt.joinToString$default(list, null, null, null, 0, null, null, 63, null));
                if (PipeCameraPresenceSource.this.isMonitoring.get()) {
                    if (!this.$isFirstEmission.element) {
                        PipeCameraPresenceSource.this.updateData(list);
                    } else {
                        Log.i("PipePresenceSrc", "Handling first camera set, triggering fresh query.");
                        ListenableFuture listenableFutureFetchData = PipeCameraPresenceSource.this.fetchData();
                        this.label = 1;
                        if (ListenableFutureKt.await(listenableFutureFetchData, this) == coroutine_suspended) {
                            return coroutine_suspended;
                        }
                    }
                } else {
                    Boxing.boxInt(Log.d("PipePresenceSrc", "Ignoring camera update because monitoring is stopped."));
                }
                return Unit.INSTANCE;
            }
            if (i != 1) {
                throw new IllegalStateException("call to 'resume' before 'invoke' with coroutine");
            }
            ResultKt.throwOnFailure(obj);
            this.$isFirstEmission.element = false;
            return Unit.INSTANCE;
        }
    }

    /* JADX INFO: renamed from: androidx.camera.camera2.adapter.PipeCameraPresenceSource$startMonitoring$3, reason: invalid class name */
    static final class AnonymousClass3 extends SuspendLambda implements Function3 {
        /* synthetic */ Object L$0;
        int label;

        AnonymousClass3(Continuation continuation) {
            super(3, continuation);
        }

        @Override // kotlin.jvm.functions.Function3
        public final Object invoke(FlowCollector flowCollector, Throwable th, Continuation continuation) {
            AnonymousClass3 anonymousClass3 = PipeCameraPresenceSource.this.new AnonymousClass3(continuation);
            anonymousClass3.L$0 = th;
            return anonymousClass3.invokeSuspend(Unit.INSTANCE);
        }

        @Override // kotlin.coroutines.jvm.internal.BaseContinuationImpl
        public final Object invokeSuspend(Object obj) {
            IntrinsicsKt.getCOROUTINE_SUSPENDED();
            if (this.label != 0) {
                throw new IllegalStateException("call to 'resume' before 'invoke' with coroutine");
            }
            ResultKt.throwOnFailure(obj);
            Throwable th = (Throwable) this.L$0;
            Log.e("PipePresenceSrc", "Error in camera ID flow collection.", th);
            if (PipeCameraPresenceSource.this.isMonitoring.get()) {
                PipeCameraPresenceSource.this.updateError(th);
            } else {
                Boxing.boxInt(Log.d("PipePresenceSrc", "Ignoring error because monitoring is stopped."));
            }
            return Unit.INSTANCE;
        }
    }

    @Override // androidx.camera.core.impl.AbstractCameraPresenceSource
    public void stopMonitoring() {
        Log.i("PipePresenceSrc", "Stopping camera ID flow collection.");
        if (this.isMonitoring.compareAndSet(true, false)) {
            Job job = this.flowCollectionJob;
            if (job != null) {
                Job.DefaultImpls.cancel$default(job, null, 1, null);
            }
            this.flowCollectionJob = null;
        }
    }

    @Override // androidx.camera.core.impl.Observable
    public ListenableFuture fetchData() {
        ListenableFuture future = CallbackToFutureAdapter.getFuture(new CallbackToFutureAdapter.Resolver() { // from class: androidx.camera.camera2.adapter.PipeCameraPresenceSource$$ExternalSyntheticLambda0
            @Override // androidx.concurrent.futures.CallbackToFutureAdapter.Resolver
            public final Object attachCompleter(CallbackToFutureAdapter.Completer completer) {
                return PipeCameraPresenceSource.fetchData$lambda$0(this.f$0, completer);
            }
        });
        Intrinsics.checkNotNullExpressionValue(future, "getFuture(...)");
        return future;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static final Object fetchData$lambda$0(PipeCameraPresenceSource pipeCameraPresenceSource, CallbackToFutureAdapter.Completer completer) {
        Intrinsics.checkNotNullParameter(completer, "completer");
        BuildersKt__Builders_commonKt.launch$default(pipeCameraPresenceSource.coroutineScope, null, null, new PipeCameraPresenceSource$fetchData$1$1(pipeCameraPresenceSource, completer, null), 3, null);
        return "FetchData for PipeCameraPresence0";
    }

    public static final class Companion {
        public /* synthetic */ Companion(DefaultConstructorMarker defaultConstructorMarker) {
            this();
        }

        private Companion() {
        }
    }
}
