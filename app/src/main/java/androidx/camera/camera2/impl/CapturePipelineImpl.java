package androidx.camera.camera2.impl;

import android.hardware.camera2.CaptureResult;
import android.util.Log;
import androidx.camera.camera2.adapter.CaptureConfigAdapter;
import androidx.camera.camera2.adapter.CaptureResultAdapter;
import androidx.camera.camera2.compat.workaround.FlashAvailabilityCheckerKt;
import androidx.camera.camera2.compat.workaround.UseTorchAsFlash;
import androidx.camera.camera2.config.UseCaseGraphContext;
import androidx.camera.camera2.pipe.CameraGraph;
import androidx.camera.camera2.pipe.FrameInfo;
import androidx.camera.camera2.pipe.FrameMetadata;
import androidx.camera.camera2.pipe.Lock3ABehavior;
import androidx.camera.camera2.pipe.Metadata;
import androidx.camera.camera2.pipe.Request;
import androidx.camera.camera2.pipe.RequestFailure;
import androidx.camera.camera2.pipe.RequestMetadata;
import androidx.camera.camera2.pipe.RequestNumber;
import androidx.camera.camera2.pipe.RequestTemplate;
import androidx.camera.camera2.pipe.Result3A;
import androidx.camera.core.ImageCaptureException;
import androidx.camera.core.Logger;
import androidx.camera.core.impl.CameraCaptureResult;
import androidx.camera.core.impl.CaptureConfig;
import androidx.camera.core.impl.Config;
import androidx.camera.core.impl.ConvergenceUtils;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;
import javax.inject.Provider;
import kotlin.Lazy;
import kotlin.LazyKt;
import kotlin.ResultKt;
import kotlin.Unit;
import kotlin.collections.CollectionsKt;
import kotlin.collections.MapsKt;
import kotlin.coroutines.Continuation;
import kotlin.coroutines.intrinsics.IntrinsicsKt;
import kotlin.coroutines.jvm.internal.Boxing;
import kotlin.coroutines.jvm.internal.ContinuationImpl;
import kotlin.coroutines.jvm.internal.SuspendLambda;
import kotlin.enums.EnumEntries;
import kotlin.enums.EnumEntriesKt;
import kotlin.jdk7.AutoCloseableKt;
import kotlin.jvm.functions.Function0;
import kotlin.jvm.functions.Function1;
import kotlin.jvm.functions.Function2;
import kotlin.jvm.internal.DefaultConstructorMarker;
import kotlin.jvm.internal.Intrinsics;
import kotlin.reflect.KClass;
import kotlinx.coroutines.BuildersKt__Builders_commonKt;
import kotlinx.coroutines.CompletableDeferred;
import kotlinx.coroutines.CompletableDeferredKt;
import kotlinx.coroutines.CoroutineScope;
import kotlinx.coroutines.Deferred;
import kotlinx.coroutines.TimeoutKt;

public final class CapturePipelineImpl implements CapturePipeline {
    private final CaptureConfigAdapter configAdapter;
    private final CapturePipelineImpl$emptyRequestMetadata$1 emptyRequestMetadata;
    private final FlashControl flashControl;
    private FrameMetadata frameMetadata;
    private final Lazy hasFlashUnit$delegate;
    private final ComboRequestListener requestListener;
    private int template;
    private final UseCaseThreads threads;
    private final TorchControl torchControl;
    private final Lazy useCaseCameraState$delegate;
    private final Provider useCaseCameraStateProvider;
    private final UseCaseGraphContext useCaseGraphContext;
    private final UseTorchAsFlash useTorchAsFlash;
    private final VideoUsageControl videoUsageControl;

    private enum PipelineTask {
        PRE_CAPTURE,
        MAIN_CAPTURE,
        POST_CAPTURE;

        private static final /* synthetic */ EnumEntries $ENTRIES = EnumEntriesKt.enumEntries(values());
    }

    /* JADX INFO: renamed from: androidx.camera.camera2.impl.CapturePipelineImpl$aePreCaptureApplyCapture$1, reason: invalid class name */
    static final class AnonymousClass1 extends ContinuationImpl {
        int I$0;
        long J$0;
        Object L$0;
        Object L$1;
        Object L$2;
        Object L$3;
        int label;
        /* synthetic */ Object result;

        AnonymousClass1(Continuation continuation) {
            super(continuation);
        }

        @Override // kotlin.coroutines.jvm.internal.BaseContinuationImpl
        public final Object invokeSuspend(Object obj) {
            this.result = obj;
            this.label |= Integer.MIN_VALUE;
            return CapturePipelineImpl.this.aePreCaptureApplyCapture(null, 0L, 0, null, this);
        }
    }

    /* JADX INFO: renamed from: androidx.camera.camera2.impl.CapturePipelineImpl$defaultCapture$1, reason: invalid class name and case insensitive filesystem */
    static final class C00741 extends ContinuationImpl {
        int I$0;
        Object L$0;
        Object L$1;
        int label;
        /* synthetic */ Object result;

        C00741(Continuation continuation) {
            super(continuation);
        }

        @Override // kotlin.coroutines.jvm.internal.BaseContinuationImpl
        public final Object invokeSuspend(Object obj) {
            this.result = obj;
            this.label |= Integer.MIN_VALUE;
            return CapturePipelineImpl.this.defaultCapture(null, 0, 0, null, this);
        }
    }

    /* JADX INFO: renamed from: androidx.camera.camera2.impl.CapturePipelineImpl$defaultNoFlashCapture$1, reason: invalid class name and case insensitive filesystem */
    static final class C00751 extends ContinuationImpl {
        int I$0;
        Object L$0;
        Object L$1;
        Object L$2;
        int label;
        /* synthetic */ Object result;

        C00751(Continuation continuation) {
            super(continuation);
        }

        @Override // kotlin.coroutines.jvm.internal.BaseContinuationImpl
        public final Object invokeSuspend(Object obj) {
            this.result = obj;
            this.label |= Integer.MIN_VALUE;
            return CapturePipelineImpl.this.defaultNoFlashCapture(null, 0, null, this);
        }
    }

    /* JADX INFO: renamed from: androidx.camera.camera2.impl.CapturePipelineImpl$getFrameMetadata$1, reason: invalid class name and case insensitive filesystem */
    static final class C00761 extends ContinuationImpl {
        Object L$0;
        int label;
        /* synthetic */ Object result;

        C00761(Continuation continuation) {
            super(continuation);
        }

        @Override // kotlin.coroutines.jvm.internal.BaseContinuationImpl
        public final Object invokeSuspend(Object obj) {
            this.result = obj;
            this.label |= Integer.MIN_VALUE;
            return CapturePipelineImpl.this.getFrameMetadata(this);
        }
    }

    /* JADX INFO: renamed from: androidx.camera.camera2.impl.CapturePipelineImpl$invokeCaptureTasks$1, reason: invalid class name and case insensitive filesystem */
    static final class C00771 extends ContinuationImpl {
        int I$0;
        int I$1;
        Object L$0;
        Object L$1;
        int label;
        /* synthetic */ Object result;

        C00771(Continuation continuation) {
            super(continuation);
        }

        @Override // kotlin.coroutines.jvm.internal.BaseContinuationImpl
        public final Object invokeSuspend(Object obj) {
            this.result = obj;
            this.label |= Integer.MIN_VALUE;
            return CapturePipelineImpl.this.invokeCaptureTasks(null, 0, 0, 0, null, this);
        }
    }

    /* JADX INFO: renamed from: androidx.camera.camera2.impl.CapturePipelineImpl$invokeScreenFlashPostCaptureTasks$1, reason: invalid class name and case insensitive filesystem */
    static final class C00781 extends ContinuationImpl {
        int I$0;
        Object L$0;
        int label;
        /* synthetic */ Object result;

        C00781(Continuation continuation) {
            super(continuation);
        }

        @Override // kotlin.coroutines.jvm.internal.BaseContinuationImpl
        public final Object invokeSuspend(Object obj) {
            this.result = obj;
            this.label |= Integer.MIN_VALUE;
            return CapturePipelineImpl.this.invokeScreenFlashPostCaptureTasks(0, this);
        }
    }

    /* JADX INFO: renamed from: androidx.camera.camera2.impl.CapturePipelineImpl$invokeScreenFlashPreCaptureTasks$1, reason: invalid class name and case insensitive filesystem */
    static final class C00791 extends ContinuationImpl {
        int I$0;
        Object L$0;
        int label;
        /* synthetic */ Object result;

        C00791(Continuation continuation) {
            super(continuation);
        }

        @Override // kotlin.coroutines.jvm.internal.BaseContinuationImpl
        public final Object invokeSuspend(Object obj) {
            this.result = obj;
            this.label |= Integer.MIN_VALUE;
            return CapturePipelineImpl.this.invokeScreenFlashPreCaptureTasks(0, this);
        }
    }

    /* JADX INFO: renamed from: androidx.camera.camera2.impl.CapturePipelineImpl$isPhysicalFlashRequired$1, reason: invalid class name and case insensitive filesystem */
    static final class C00801 extends ContinuationImpl {
        int label;
        /* synthetic */ Object result;

        C00801(Continuation continuation) {
            super(continuation);
        }

        @Override // kotlin.coroutines.jvm.internal.BaseContinuationImpl
        public final Object invokeSuspend(Object obj) {
            this.result = obj;
            this.label |= Integer.MIN_VALUE;
            return CapturePipelineImpl.this.isPhysicalFlashRequired(0, this);
        }
    }

    /* JADX INFO: renamed from: androidx.camera.camera2.impl.CapturePipelineImpl$lockAf$1, reason: invalid class name and case insensitive filesystem */
    static final class C00811 extends ContinuationImpl {
        long J$0;
        Object L$0;
        boolean Z$0;
        int label;
        /* synthetic */ Object result;

        C00811(Continuation continuation) {
            super(continuation);
        }

        @Override // kotlin.coroutines.jvm.internal.BaseContinuationImpl
        public final Object invokeSuspend(Object obj) {
            this.result = obj;
            this.label |= Integer.MIN_VALUE;
            return CapturePipelineImpl.this.lockAf(0L, false, this);
        }
    }

    /* JADX INFO: renamed from: androidx.camera.camera2.impl.CapturePipelineImpl$screenFlashCapture$1, reason: invalid class name and case insensitive filesystem */
    static final class C00821 extends ContinuationImpl {
        int I$0;
        Object L$0;
        Object L$1;
        Object L$2;
        int label;
        /* synthetic */ Object result;

        C00821(Continuation continuation) {
            super(continuation);
        }

        @Override // kotlin.coroutines.jvm.internal.BaseContinuationImpl
        public final Object invokeSuspend(Object obj) {
            this.result = obj;
            this.label |= Integer.MIN_VALUE;
            return CapturePipelineImpl.this.screenFlashCapture(null, 0, null, this);
        }
    }

    /* JADX INFO: renamed from: androidx.camera.camera2.impl.CapturePipelineImpl$torchApplyCapture$1, reason: invalid class name and case insensitive filesystem */
    static final class C00831 extends ContinuationImpl {
        int I$0;
        int I$1;
        int I$2;
        long J$0;
        Object L$0;
        Object L$1;
        Object L$2;
        Object L$3;
        boolean Z$0;
        int label;
        /* synthetic */ Object result;

        C00831(Continuation continuation) {
            super(continuation);
        }

        @Override // kotlin.coroutines.jvm.internal.BaseContinuationImpl
        public final Object invokeSuspend(Object obj) {
            this.result = obj;
            this.label |= Integer.MIN_VALUE;
            return CapturePipelineImpl.this.torchApplyCapture(null, 0, 0L, null, false, this);
        }
    }

    /* JADX INFO: renamed from: androidx.camera.camera2.impl.CapturePipelineImpl$torchAsFlashCapture$1, reason: invalid class name and case insensitive filesystem */
    static final class C00841 extends ContinuationImpl {
        int I$0;
        Object L$0;
        Object L$1;
        int label;
        /* synthetic */ Object result;

        C00841(Continuation continuation) {
            super(continuation);
        }

        @Override // kotlin.coroutines.jvm.internal.BaseContinuationImpl
        public final Object invokeSuspend(Object obj) {
            this.result = obj;
            this.label |= Integer.MIN_VALUE;
            return CapturePipelineImpl.this.torchAsFlashCapture(null, 0, 0, null, this);
        }
    }

    /* JADX INFO: renamed from: androidx.camera.camera2.impl.CapturePipelineImpl$unlockAf$1, reason: invalid class name and case insensitive filesystem */
    static final class C00851 extends ContinuationImpl {
        long J$0;
        Object L$0;
        int label;
        /* synthetic */ Object result;

        C00851(Continuation continuation) {
            super(continuation);
        }

        @Override // kotlin.coroutines.jvm.internal.BaseContinuationImpl
        public final Object invokeSuspend(Object obj) {
            this.result = obj;
            this.label |= Integer.MIN_VALUE;
            return CapturePipelineImpl.this.unlockAf(0L, this);
        }
    }

    /* JADX INFO: renamed from: androidx.camera.camera2.impl.CapturePipelineImpl$waitForResult$1, reason: invalid class name and case insensitive filesystem */
    static final class C00861 extends ContinuationImpl {
        Object L$0;
        int label;
        /* synthetic */ Object result;

        C00861(Continuation continuation) {
            super(continuation);
        }

        @Override // kotlin.coroutines.jvm.internal.BaseContinuationImpl
        public final Object invokeSuspend(Object obj) {
            this.result = obj;
            this.label |= Integer.MIN_VALUE;
            return CapturePipelineImpl.this.waitForResult(0L, null, this);
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static final boolean waitForResult$lambda$0(FrameInfo frameInfo) {
        Intrinsics.checkNotNullParameter(frameInfo, "<unused var>");
        return true;
    }

    /* JADX WARN: Type inference failed for: r2v6, types: [androidx.camera.camera2.impl.CapturePipelineImpl$emptyRequestMetadata$1] */
    public CapturePipelineImpl(CaptureConfigAdapter configAdapter, FlashControl flashControl, TorchControl torchControl, VideoUsageControl videoUsageControl, UseCaseThreads threads, ComboRequestListener requestListener, UseTorchAsFlash useTorchAsFlash, final CameraProperties cameraProperties, Provider useCaseCameraStateProvider, UseCaseGraphContext useCaseGraphContext) {
        Intrinsics.checkNotNullParameter(configAdapter, "configAdapter");
        Intrinsics.checkNotNullParameter(flashControl, "flashControl");
        Intrinsics.checkNotNullParameter(torchControl, "torchControl");
        Intrinsics.checkNotNullParameter(videoUsageControl, "videoUsageControl");
        Intrinsics.checkNotNullParameter(threads, "threads");
        Intrinsics.checkNotNullParameter(requestListener, "requestListener");
        Intrinsics.checkNotNullParameter(useTorchAsFlash, "useTorchAsFlash");
        Intrinsics.checkNotNullParameter(cameraProperties, "cameraProperties");
        Intrinsics.checkNotNullParameter(useCaseCameraStateProvider, "useCaseCameraStateProvider");
        Intrinsics.checkNotNullParameter(useCaseGraphContext, "useCaseGraphContext");
        this.configAdapter = configAdapter;
        this.flashControl = flashControl;
        this.torchControl = torchControl;
        this.videoUsageControl = videoUsageControl;
        this.threads = threads;
        this.requestListener = requestListener;
        this.useTorchAsFlash = useTorchAsFlash;
        this.useCaseCameraStateProvider = useCaseCameraStateProvider;
        this.useCaseGraphContext = useCaseGraphContext;
        this.hasFlashUnit$delegate = LazyKt.lazy(new Function0() { // from class: androidx.camera.camera2.impl.CapturePipelineImpl$$ExternalSyntheticLambda1
            @Override // kotlin.jvm.functions.Function0
            public final Object invoke() {
                return Boolean.valueOf(CapturePipelineImpl.hasFlashUnit_delegate$lambda$0(cameraProperties));
            }
        });
        this.useCaseCameraState$delegate = LazyKt.lazy(new Function0() { // from class: androidx.camera.camera2.impl.CapturePipelineImpl$$ExternalSyntheticLambda2
            @Override // kotlin.jvm.functions.Function0
            public final Object invoke() {
                return CapturePipelineImpl.useCaseCameraState_delegate$lambda$0(this.f$0);
            }
        });
        this.template = 1;
        this.emptyRequestMetadata = new RequestMetadata() { // from class: androidx.camera.camera2.impl.CapturePipelineImpl$emptyRequestMetadata$1
            private final int template = RequestTemplate.m385constructorimpl(0);
            private final Map streams = MapsKt.emptyMap();
            private final boolean repeating = true;
            private final Request request = new Request(CollectionsKt.emptyList(), null, null, null, null, null, 62, null);
            private final long requestNumber = RequestNumber.m379constructorimpl(0);

            @Override // androidx.camera.camera2.pipe.Metadata
            public Object get(Metadata.Key key) {
                Intrinsics.checkNotNullParameter(key, "key");
                return null;
            }

            @Override // androidx.camera.camera2.pipe.Metadata
            public Object getOrDefault(Metadata.Key key, Object obj) {
                Intrinsics.checkNotNullParameter(key, "key");
                return obj;
            }

            @Override // androidx.camera.camera2.pipe.UnsafeWrapper
            public Object unwrapAs(KClass type) {
                Intrinsics.checkNotNullParameter(type, "type");
                return null;
            }

            @Override // androidx.camera.camera2.pipe.RequestMetadata
            public Map getStreams() {
                return this.streams;
            }

            @Override // androidx.camera.camera2.pipe.RequestMetadata
            public boolean getRepeating() {
                return this.repeating;
            }

            @Override // androidx.camera.camera2.pipe.RequestMetadata
            public Request getRequest() {
                return this.request;
            }

            @Override // androidx.camera.camera2.pipe.RequestMetadata
            /* JADX INFO: renamed from: getRequestNumber-my6kx4g, reason: not valid java name */
            public long mo73getRequestNumbermy6kx4g() {
                return this.requestNumber;
            }
        };
    }

    private static final class MainCaptureParams {
        private final List configs;
        private final int requestTemplate;
        private final Config sessionConfigOptions;

        public /* synthetic */ MainCaptureParams(List list, int i, Config config, DefaultConstructorMarker defaultConstructorMarker) {
            this(list, i, config);
        }

        public boolean equals(Object obj) {
            if (this == obj) {
                return true;
            }
            if (!(obj instanceof MainCaptureParams)) {
                return false;
            }
            MainCaptureParams mainCaptureParams = (MainCaptureParams) obj;
            return Intrinsics.areEqual(this.configs, mainCaptureParams.configs) && RequestTemplate.m387equalsimpl0(this.requestTemplate, mainCaptureParams.requestTemplate) && Intrinsics.areEqual(this.sessionConfigOptions, mainCaptureParams.sessionConfigOptions);
        }

        public int hashCode() {
            return (((this.configs.hashCode() * 31) + RequestTemplate.m389hashCodeimpl(this.requestTemplate)) * 31) + this.sessionConfigOptions.hashCode();
        }

        public String toString() {
            return "MainCaptureParams(configs=" + this.configs + ", requestTemplate=" + ((Object) RequestTemplate.m390toStringimpl(this.requestTemplate)) + ", sessionConfigOptions=" + this.sessionConfigOptions + ')';
        }

        private MainCaptureParams(List configs, int i, Config sessionConfigOptions) {
            Intrinsics.checkNotNullParameter(configs, "configs");
            Intrinsics.checkNotNullParameter(sessionConfigOptions, "sessionConfigOptions");
            this.configs = configs;
            this.requestTemplate = i;
            this.sessionConfigOptions = sessionConfigOptions;
        }

        public final List getConfigs() {
            return this.configs;
        }

        /* JADX INFO: renamed from: getRequestTemplate-fGx8uWA, reason: not valid java name */
        public final int m72getRequestTemplatefGx8uWA() {
            return this.requestTemplate;
        }

        public final Config getSessionConfigOptions() {
            return this.sessionConfigOptions;
        }
    }

    private final boolean getHasFlashUnit() {
        return ((Boolean) this.hasFlashUnit$delegate.getValue()).booleanValue();
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static final boolean hasFlashUnit_delegate$lambda$0(CameraProperties cameraProperties) {
        return FlashAvailabilityCheckerKt.isFlashAvailable$default(cameraProperties, false, 1, null);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public final UseCaseCameraState getUseCaseCameraState() {
        return (UseCaseCameraState) this.useCaseCameraState$delegate.getValue();
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static final UseCaseCameraState useCaseCameraState_delegate$lambda$0(CapturePipelineImpl capturePipelineImpl) {
        return (UseCaseCameraState) capturePipelineImpl.useCaseCameraStateProvider.get();
    }

    public int getTemplate() {
        return this.template;
    }

    @Override // androidx.camera.camera2.impl.CapturePipeline
    public void setTemplate(int i) {
        this.template = i;
    }

    /* JADX INFO: Access modifiers changed from: private */
    /* JADX WARN: Code duplicated, block: B:32:0x007d  */
    /* JADX WARN: Code duplicated, block: B:8:0x0014  */
    /* JADX WARN: Instruction removed from duplicated block: B:32:0x007d, please report this as an issue */
    public final Object getFrameMetadata(Continuation continuation) {
        C00761 c00761;
        CapturePipelineImpl capturePipelineImpl;
        CapturePipelineImpl capturePipelineImpl2;
        if (continuation instanceof C00761) {
            c00761 = (C00761) continuation;
            int i = c00761.label;
            if ((i & Integer.MIN_VALUE) != 0) {
                c00761.label = i - Integer.MIN_VALUE;
            } else {
                c00761 = new C00761(continuation);
            }
        } else {
            c00761 = new C00761(continuation);
        }
        C00761 c00762 = c00761;
        Object objWaitForResult$default = c00762.result;
        Object coroutine_suspended = IntrinsicsKt.getCOROUTINE_SUSPENDED();
        int i2 = c00762.label;
        if (i2 == 0) {
            ResultKt.throwOnFailure(objWaitForResult$default);
            if (this.frameMetadata == null) {
                Camera2Logger camera2Logger = Camera2Logger.INSTANCE;
                if (Logger.isDebugEnabled("CXCP")) {
                    Log.d(Camera2Logger.TRUNCATED_TAG, "getFrameMetadata: waiting for result");
                }
                long j = CapturePipelineKt.CHECK_FLASH_REQUIRED_TIMEOUT_IN_NS;
                c00762.L$0 = this;
                c00762.label = 1;
                capturePipelineImpl = this;
                objWaitForResult$default = waitForResult$default(capturePipelineImpl, j, null, c00762, 2, null);
                if (objWaitForResult$default == coroutine_suspended) {
                    return coroutine_suspended;
                }
                capturePipelineImpl2 = capturePipelineImpl;
            } else {
                capturePipelineImpl = this;
            }
            Camera2Logger camera2Logger2 = Camera2Logger.INSTANCE;
            if (Logger.isDebugEnabled("CXCP")) {
                Log.d(Camera2Logger.TRUNCATED_TAG, "getFrameMetadata: frameMetadata = " + this.frameMetadata);
            }
            return capturePipelineImpl.frameMetadata;
        }
        if (i2 != 1) {
            throw new IllegalStateException("call to 'resume' before 'invoke' with coroutine");
        }
        capturePipelineImpl2 = (CapturePipelineImpl) c00762.L$0;
        ResultKt.throwOnFailure(objWaitForResult$default);
        capturePipelineImpl = this;
        FrameInfo frameInfo = (FrameInfo) objWaitForResult$default;
        capturePipelineImpl2.frameMetadata = frameInfo != null ? frameInfo.getMetadata() : null;
        Camera2Logger camera2Logger3 = Camera2Logger.INSTANCE;
        if (Logger.isDebugEnabled("CXCP")) {
            Log.d(Camera2Logger.TRUNCATED_TAG, "getFrameMetadata: frameMetadata = " + this.frameMetadata);
        }
        return capturePipelineImpl.frameMetadata;
    }

    /* JADX INFO: Access modifiers changed from: private */
    /* JADX WARN: Code duplicated, block: B:8:0x0014  */
    public final Object invokeCaptureTasks(List list, int i, int i2, int i3, MainCaptureParams mainCaptureParams, Continuation continuation) throws Exception {
        C00771 c00771;
        if (continuation instanceof C00771) {
            c00771 = (C00771) continuation;
            int i4 = c00771.label;
            if ((i4 & Integer.MIN_VALUE) != 0) {
                c00771.label = i4 - Integer.MIN_VALUE;
            } else {
                c00771 = new C00771(continuation);
            }
        } else {
            c00771 = new C00771(continuation);
        }
        C00771 c00772 = c00771;
        Object objIsTorchAsFlash = c00772.result;
        Object coroutine_suspended = IntrinsicsKt.getCOROUTINE_SUSPENDED();
        int i5 = c00772.label;
        if (i5 == 0) {
            ResultKt.throwOnFailure(objIsTorchAsFlash);
            Camera2Logger camera2Logger = Camera2Logger.INSTANCE;
            if (Logger.isDebugEnabled("CXCP")) {
                Log.d(Camera2Logger.TRUNCATED_TAG, "CapturePipeline#invokeCaptureTasks: tasks = " + list + ", captureMode = " + i + ", flashMode = " + i2 + ", flashType = " + i3);
            }
            this.frameMetadata = null;
            if (list.contains(PipelineTask.MAIN_CAPTURE) && mainCaptureParams == null) {
                throw new IllegalStateException("Must not be null for PipelineType.MAIN_CAPTURE");
            }
            if (i2 == 3) {
                c00772.label = 1;
                Object objScreenFlashCapture = screenFlashCapture(mainCaptureParams, i, list, c00772);
                if (objScreenFlashCapture != coroutine_suspended) {
                    return objScreenFlashCapture;
                }
            } else {
                c00772.L$0 = list;
                c00772.L$1 = mainCaptureParams;
                c00772.I$0 = i;
                c00772.I$1 = i2;
                c00772.label = 2;
                objIsTorchAsFlash = isTorchAsFlash(i3, c00772);
                if (objIsTorchAsFlash != coroutine_suspended) {
                }
            }
            return coroutine_suspended;
        }
        if (i5 == 1) {
            ResultKt.throwOnFailure(objIsTorchAsFlash);
            return objIsTorchAsFlash;
        }
        if (i5 != 2) {
            if (i5 == 3) {
                ResultKt.throwOnFailure(objIsTorchAsFlash);
                return objIsTorchAsFlash;
            }
            if (i5 != 4) {
                throw new IllegalStateException("call to 'resume' before 'invoke' with coroutine");
            }
            ResultKt.throwOnFailure(objIsTorchAsFlash);
            return objIsTorchAsFlash;
        }
        i2 = c00772.I$1;
        i = c00772.I$0;
        mainCaptureParams = (MainCaptureParams) c00772.L$1;
        list = (List) c00772.L$0;
        ResultKt.throwOnFailure(objIsTorchAsFlash);
        int i6 = i2;
        int i7 = i;
        MainCaptureParams mainCaptureParams2 = mainCaptureParams;
        List list2 = list;
        if (((Boolean) objIsTorchAsFlash).booleanValue()) {
            c00772.L$0 = null;
            c00772.L$1 = null;
            c00772.label = 3;
            Object obj = torchAsFlashCapture(mainCaptureParams2, i7, i6, list2, c00772);
            if (obj != coroutine_suspended) {
                return obj;
            }
        } else {
            c00772.L$0 = null;
            c00772.L$1 = null;
            c00772.label = 4;
            Object objDefaultCapture = defaultCapture(mainCaptureParams2, i7, i6, list2, c00772);
            if (objDefaultCapture != coroutine_suspended) {
                return objDefaultCapture;
            }
        }
        return coroutine_suspended;
    }

    @Override // androidx.camera.camera2.impl.CapturePipeline
    /* JADX INFO: renamed from: submitStillCaptures-BvXKQx0 */
    public Object mo44submitStillCapturesBvXKQx0(List list, int i, Config config, int i2, int i3, int i4, Continuation continuation) {
        return invokeCaptureTasks(CollectionsKt.listOf((Object[]) new PipelineTask[]{PipelineTask.PRE_CAPTURE, PipelineTask.MAIN_CAPTURE, PipelineTask.POST_CAPTURE}), i2, i4, i3, new MainCaptureParams(list, i, config, null), continuation);
    }

    /* JADX INFO: Access modifiers changed from: private */
    /* JADX WARN: Code duplicated, block: B:46:0x00be A[RETURN] */
    /* JADX WARN: Code duplicated, block: B:8:0x0014  */
    public final Object torchAsFlashCapture(MainCaptureParams mainCaptureParams, int i, int i2, List list, Continuation continuation) throws Exception {
        C00841 c00841;
        Object objDefaultNoFlashCapture;
        if (continuation instanceof C00841) {
            c00841 = (C00841) continuation;
            int i3 = c00841.label;
            if ((i3 & Integer.MIN_VALUE) != 0) {
                c00841.label = i3 - Integer.MIN_VALUE;
            } else {
                c00841 = new C00841(continuation);
            }
        } else {
            c00841 = new C00841(continuation);
        }
        C00841 c00842 = c00841;
        Object objIsPhysicalFlashRequired = c00842.result;
        Object coroutine_suspended = IntrinsicsKt.getCOROUTINE_SUSPENDED();
        int i4 = c00842.label;
        if (i4 == 0) {
            ResultKt.throwOnFailure(objIsPhysicalFlashRequired);
            Camera2Logger camera2Logger = Camera2Logger.INSTANCE;
            if (Logger.isDebugEnabled("CXCP")) {
                Log.d(Camera2Logger.TRUNCATED_TAG, "CapturePipeline#torchAsFlashCapture");
            }
            if (getHasFlashUnit()) {
                c00842.L$0 = mainCaptureParams;
                c00842.L$1 = list;
                c00842.I$0 = i;
                c00842.label = 1;
                objIsPhysicalFlashRequired = isPhysicalFlashRequired(i2, c00842);
                if (objIsPhysicalFlashRequired == coroutine_suspended) {
                }
            } else {
                c00842.L$0 = null;
                c00842.L$1 = null;
                c00842.label = 3;
                objDefaultNoFlashCapture = defaultNoFlashCapture(mainCaptureParams, i, list, c00842);
                if (objDefaultNoFlashCapture == coroutine_suspended) {
                    return objDefaultNoFlashCapture;
                }
            }
            return coroutine_suspended;
        }
        if (i4 != 1) {
            if (i4 == 2) {
                ResultKt.throwOnFailure(objIsPhysicalFlashRequired);
                return objIsPhysicalFlashRequired;
            }
            if (i4 != 3) {
                throw new IllegalStateException("call to 'resume' before 'invoke' with coroutine");
            }
            ResultKt.throwOnFailure(objIsPhysicalFlashRequired);
            return objIsPhysicalFlashRequired;
        }
        i = c00842.I$0;
        list = (List) c00842.L$1;
        mainCaptureParams = (MainCaptureParams) c00842.L$0;
        ResultKt.throwOnFailure(objIsPhysicalFlashRequired);
        List list2 = list;
        if (((Boolean) objIsPhysicalFlashRequired).booleanValue()) {
            boolean z = true;
            long j = CapturePipelineKt.CHECK_3A_WITH_FLASH_TIMEOUT_IN_NS;
            if (this.useTorchAsFlash.shouldDisableAePrecapture() || this.videoUsageControl.isInVideoUsage()) {
                z = false;
            }
            boolean z2 = z;
            c00842.L$0 = null;
            c00842.L$1 = null;
            c00842.label = 2;
            Object obj = torchApplyCapture(mainCaptureParams, i, j, list2, z2, c00842);
            if (obj != coroutine_suspended) {
                return obj;
            }
        } else {
            list = list2;
            c00842.L$0 = null;
            c00842.L$1 = null;
            c00842.label = 3;
            objDefaultNoFlashCapture = defaultNoFlashCapture(mainCaptureParams, i, list, c00842);
            if (objDefaultNoFlashCapture == coroutine_suspended) {
                return objDefaultNoFlashCapture;
            }
        }
        return coroutine_suspended;
    }

    /* JADX INFO: Access modifiers changed from: private */
    /* JADX WARN: Code duplicated, block: B:8:0x0014  */
    public final Object defaultCapture(MainCaptureParams mainCaptureParams, int i, int i2, List list, Continuation continuation) throws Exception {
        C00741 c00741;
        if (continuation instanceof C00741) {
            c00741 = (C00741) continuation;
            int i3 = c00741.label;
            if ((i3 & Integer.MIN_VALUE) != 0) {
                c00741.label = i3 - Integer.MIN_VALUE;
            } else {
                c00741 = new C00741(continuation);
            }
        } else {
            c00741 = new C00741(continuation);
        }
        C00741 c00742 = c00741;
        Object objIsPhysicalFlashRequired = c00742.result;
        Object coroutine_suspended = IntrinsicsKt.getCOROUTINE_SUSPENDED();
        int i4 = c00742.label;
        if (i4 == 0) {
            ResultKt.throwOnFailure(objIsPhysicalFlashRequired);
            if (getHasFlashUnit()) {
                c00742.L$0 = mainCaptureParams;
                c00742.L$1 = list;
                c00742.I$0 = i;
                c00742.label = 1;
                objIsPhysicalFlashRequired = isPhysicalFlashRequired(i2, c00742);
                if (objIsPhysicalFlashRequired == coroutine_suspended) {
                }
            } else {
                c00742.label = 4;
                Object objDefaultNoFlashCapture = defaultNoFlashCapture(mainCaptureParams, i, list, c00742);
                if (objDefaultNoFlashCapture != coroutine_suspended) {
                    return objDefaultNoFlashCapture;
                }
            }
            return coroutine_suspended;
        }
        if (i4 != 1) {
            if (i4 == 2) {
                ResultKt.throwOnFailure(objIsPhysicalFlashRequired);
                return objIsPhysicalFlashRequired;
            }
            if (i4 == 3) {
                ResultKt.throwOnFailure(objIsPhysicalFlashRequired);
                return objIsPhysicalFlashRequired;
            }
            if (i4 != 4) {
                throw new IllegalStateException("call to 'resume' before 'invoke' with coroutine");
            }
            ResultKt.throwOnFailure(objIsPhysicalFlashRequired);
            return objIsPhysicalFlashRequired;
        }
        i = c00742.I$0;
        list = (List) c00742.L$1;
        mainCaptureParams = (MainCaptureParams) c00742.L$0;
        ResultKt.throwOnFailure(objIsPhysicalFlashRequired);
        MainCaptureParams mainCaptureParams2 = mainCaptureParams;
        int i5 = i;
        List list2 = list;
        boolean zBooleanValue = ((Boolean) objIsPhysicalFlashRequired).booleanValue();
        long j = zBooleanValue ? CapturePipelineKt.CHECK_3A_WITH_FLASH_TIMEOUT_IN_NS : CapturePipelineKt.CHECK_3A_TIMEOUT_IN_NS;
        if (zBooleanValue || i5 == 0) {
            c00742.L$0 = null;
            c00742.L$1 = null;
            c00742.label = 2;
            Object objAePreCaptureApplyCapture = aePreCaptureApplyCapture(mainCaptureParams2, j, i5, list2, c00742);
            if (objAePreCaptureApplyCapture != coroutine_suspended) {
                return objAePreCaptureApplyCapture;
            }
        } else {
            c00742.L$0 = null;
            c00742.L$1 = null;
            c00742.label = 3;
            Object objDefaultNoFlashCapture2 = defaultNoFlashCapture(mainCaptureParams2, i5, list2, c00742);
            if (objDefaultNoFlashCapture2 != coroutine_suspended) {
                return objDefaultNoFlashCapture2;
            }
        }
        return coroutine_suspended;
    }

    /* JADX INFO: Access modifiers changed from: private */
    /* JADX WARN: Code duplicated, block: B:42:0x00df  */
    /* JADX WARN: Code duplicated, block: B:46:0x00f6  */
    /* JADX WARN: Code duplicated, block: B:48:0x00fc  */
    /* JADX WARN: Code duplicated, block: B:50:0x0107  */
    /* JADX WARN: Code duplicated, block: B:52:0x0111  */
    /* JADX WARN: Code duplicated, block: B:53:0x011b  */
    /* JADX WARN: Code duplicated, block: B:55:0x0123  */
    /* JADX WARN: Code duplicated, block: B:58:0x0133  */
    /* JADX WARN: Code duplicated, block: B:60:0x013d  */
    /* JADX WARN: Code duplicated, block: B:7:0x0019  */
    public final Object defaultNoFlashCapture(MainCaptureParams mainCaptureParams, int i, List list, Continuation continuation) {
        C00751 c00751;
        int i2;
        CapturePipelineImpl capturePipelineImpl;
        MainCaptureParams mainCaptureParams2;
        List listListOf;
        List list2 = list;
        if (continuation instanceof C00751) {
            c00751 = (C00751) continuation;
            int i3 = c00751.label;
            if ((i3 & Integer.MIN_VALUE) != 0) {
                c00751.label = i3 - Integer.MIN_VALUE;
            } else {
                c00751 = new C00751(continuation);
            }
        } else {
            c00751 = new C00751(continuation);
        }
        Object obj = c00751.result;
        Object coroutine_suspended = IntrinsicsKt.getCOROUTINE_SUSPENDED();
        int i4 = c00751.label;
        if (i4 == 0) {
            ResultKt.throwOnFailure(obj);
            Camera2Logger camera2Logger = Camera2Logger.INSTANCE;
            if (Logger.isDebugEnabled("CXCP")) {
                Log.d(Camera2Logger.TRUNCATED_TAG, "CapturePipeline#defaultNoFlashCapture");
            }
            i2 = i == 0 ? 1 : 0;
            if (Logger.isDebugEnabled("CXCP")) {
                Log.d(Camera2Logger.TRUNCATED_TAG, "CapturePipeline#List<PipelineTask>.invoke: tasks = " + list2);
            }
            if (list2.contains(PipelineTask.PRE_CAPTURE)) {
                if (Logger.isDebugEnabled("CXCP")) {
                    Log.d(Camera2Logger.TRUNCATED_TAG, "CapturePipeline#List<PipelineTask>.invoke: starting PRE_CAPTURE");
                }
                if (i2 != 0) {
                    if (Logger.isDebugEnabled("CXCP")) {
                        Log.d(Camera2Logger.TRUNCATED_TAG, "CapturePipeline#defaultNoFlashCapture: Locking 3A");
                    }
                    long j = CapturePipelineKt.CHECK_3A_TIMEOUT_IN_NS;
                    c00751.L$0 = this;
                    c00751.L$1 = list2;
                    c00751.L$2 = mainCaptureParams;
                    c00751.I$0 = i2;
                    c00751.label = 1;
                    if (lockAf(j, false, c00751) == coroutine_suspended) {
                        return coroutine_suspended;
                    }
                    capturePipelineImpl = this;
                    mainCaptureParams2 = mainCaptureParams;
                } else {
                    capturePipelineImpl = this;
                    mainCaptureParams2 = mainCaptureParams;
                }
                if (Logger.isDebugEnabled("CXCP")) {
                    Log.d(Camera2Logger.TRUNCATED_TAG, "CapturePipeline#List<PipelineTask>.invoke: PRE_CAPTURE completed");
                }
            } else {
                capturePipelineImpl = this;
                mainCaptureParams2 = mainCaptureParams;
            }
            if (list2.contains(PipelineTask.MAIN_CAPTURE)) {
                if (Logger.isDebugEnabled("CXCP")) {
                    Log.d(Camera2Logger.TRUNCATED_TAG, "CapturePipeline#List<PipelineTask>.invoke: starting MAIN_CAPTURE");
                }
                if (mainCaptureParams2 != null) {
                    throw new IllegalStateException("Required value was null.");
                }
                listListOf = capturePipelineImpl.submitRequestInternal(mainCaptureParams2);
                if (Logger.isDebugEnabled("CXCP")) {
                    Log.d(Camera2Logger.TRUNCATED_TAG, "CapturePipeline#List<PipelineTask>.invoke: MAIN_CAPTURE completed");
                }
            } else {
                listListOf = CollectionsKt.listOf(CompletableDeferredKt.CompletableDeferred((Object) null));
            }
            if (list2.contains(PipelineTask.POST_CAPTURE)) {
                BuildersKt__Builders_commonKt.launch$default(capturePipelineImpl.threads.getSequentialScope(), null, null, new CapturePipelineImpl$defaultNoFlashCapture$$inlined$invoke$1(listListOf, null, i2 != 0, this), 3, null);
            }
            return listListOf;
        }
        if (i4 != 1) {
            throw new IllegalStateException("call to 'resume' before 'invoke' with coroutine");
        }
        int i5 = c00751.I$0;
        mainCaptureParams2 = (MainCaptureParams) c00751.L$2;
        List list3 = (List) c00751.L$1;
        capturePipelineImpl = (CapturePipelineImpl) c00751.L$0;
        ResultKt.throwOnFailure(obj);
        i2 = i5;
        list2 = list3;
        Camera2Logger camera2Logger2 = Camera2Logger.INSTANCE;
        if (Logger.isDebugEnabled("CXCP")) {
            Log.d(Camera2Logger.TRUNCATED_TAG, "CapturePipeline#defaultNoFlashCapture: Locking 3A done");
        }
        if (Logger.isDebugEnabled("CXCP")) {
            Log.d(Camera2Logger.TRUNCATED_TAG, "CapturePipeline#List<PipelineTask>.invoke: PRE_CAPTURE completed");
        }
        if (list2.contains(PipelineTask.MAIN_CAPTURE)) {
            if (Logger.isDebugEnabled("CXCP")) {
                Log.d(Camera2Logger.TRUNCATED_TAG, "CapturePipeline#List<PipelineTask>.invoke: starting MAIN_CAPTURE");
            }
            if (mainCaptureParams2 != null) {
                throw new IllegalStateException("Required value was null.");
            }
            listListOf = capturePipelineImpl.submitRequestInternal(mainCaptureParams2);
            if (Logger.isDebugEnabled("CXCP")) {
                Log.d(Camera2Logger.TRUNCATED_TAG, "CapturePipeline#List<PipelineTask>.invoke: MAIN_CAPTURE completed");
            }
        } else {
            listListOf = CollectionsKt.listOf(CompletableDeferredKt.CompletableDeferred((Object) null));
        }
        if (list2.contains(PipelineTask.POST_CAPTURE)) {
            BuildersKt__Builders_commonKt.launch$default(capturePipelineImpl.threads.getSequentialScope(), null, null, new CapturePipelineImpl$defaultNoFlashCapture$$inlined$invoke$1(listListOf, null, i2 != 0, this), 3, null);
        }
        return listListOf;
    }

    /* JADX INFO: Access modifiers changed from: private */
    /* JADX WARN: Code duplicated, block: B:101:0x02b7 A[DONT_INVERT] */
    /* JADX WARN: Code duplicated, block: B:102:0x02b9  */
    /* JADX WARN: Code duplicated, block: B:104:0x02bf  */
    /* JADX WARN: Code duplicated, block: B:108:0x02e1  */
    /* JADX WARN: Code duplicated, block: B:111:0x02f0  */
    /* JADX WARN: Code duplicated, block: B:113:0x02fe  */
    /* JADX WARN: Code duplicated, block: B:115:0x0305  */
    /* JADX WARN: Code duplicated, block: B:119:0x032b  */
    /* JADX WARN: Code duplicated, block: B:122:0x033a  */
    /* JADX WARN: Code duplicated, block: B:123:0x0344  */
    /* JADX WARN: Code duplicated, block: B:126:0x0350  */
    /* JADX WARN: Code duplicated, block: B:131:0x036f  */
    /* JADX WARN: Code duplicated, block: B:133:0x0375  */
    /* JADX WARN: Code duplicated, block: B:135:0x0380  */
    /* JADX WARN: Code duplicated, block: B:137:0x038a  */
    /* JADX WARN: Code duplicated, block: B:138:0x0394  */
    /* JADX WARN: Code duplicated, block: B:140:0x039c  */
    /* JADX WARN: Code duplicated, block: B:143:0x03ac  */
    /* JADX WARN: Code duplicated, block: B:145:0x03b7  */
    /* JADX WARN: Code duplicated, block: B:146:0x03ba  */
    /* JADX WARN: Code duplicated, block: B:149:0x03bf  */
    /* JADX WARN: Code duplicated, block: B:62:0x01bf  */
    /* JADX WARN: Code duplicated, block: B:65:0x01db  */
    /* JADX WARN: Code duplicated, block: B:67:0x01e1  */
    /* JADX WARN: Code duplicated, block: B:71:0x020d  */
    /* JADX WARN: Code duplicated, block: B:75:0x021c  */
    /* JADX WARN: Code duplicated, block: B:76:0x021f  */
    /* JADX WARN: Code duplicated, block: B:78:0x0223  */
    /* JADX WARN: Code duplicated, block: B:79:0x0226  */
    /* JADX WARN: Code duplicated, block: B:83:0x0258  */
    /* JADX WARN: Code duplicated, block: B:86:0x027d  */
    /* JADX WARN: Code duplicated, block: B:87:0x027f  */
    /* JADX WARN: Code duplicated, block: B:91:0x028e  */
    /* JADX WARN: Code duplicated, block: B:99:0x02b4  */
    /* JADX WARN: Instruction removed from duplicated block: B:91:0x028e, please report this as an issue */
    /* JADX WARN: Multi-variable type inference failed */
    /* JADX WARN: Type inference failed for: r7v2 */
    /* JADX WARN: Type inference failed for: r7v26 */
    /* JADX WARN: Type inference failed for: r7v3, types: [java.lang.AutoCloseable] */
    /* JADX WARN: Type inference failed for: r7v33 */
    /* JADX WARN: Type inference failed for: r7v35, types: [java.lang.AutoCloseable] */
    /* JADX WARN: Type inference failed for: r7v49 */
    /* JADX WARN: Type inference failed for: r7v50 */
    /* JADX WARN: Type inference failed for: r7v51 */
    /* JADX WARN: Type inference failed for: r7v52 */
    public final Object torchApplyCapture(MainCaptureParams mainCaptureParams, int i, long j, List list, boolean z, Continuation continuation) throws Exception {
        C00831 c00831;
        ?? r7;
        Throwable th;
        ?? r8;
        int i2;
        MainCaptureParams mainCaptureParams2;
        boolean z2;
        Throwable th2;
        int i3;
        List list2;
        CapturePipelineImpl capturePipelineImpl;
        int i4;
        boolean z3;
        int i5;
        int i6;
        boolean z4;
        List list3;
        int i7;
        long j2;
        long j3;
        boolean z5;
        int i8;
        Function1 function1;
        int i9;
        MainCaptureParams mainCaptureParams3;
        List list4;
        CapturePipelineImpl capturePipelineImpl2;
        Object objAcquireSession;
        long j4;
        int i10;
        MainCaptureParams mainCaptureParams4;
        int i11;
        List list5;
        CapturePipelineImpl capturePipelineImpl3;
        AutoCloseable autoCloseable;
        boolean z6;
        boolean z7;
        boolean z8;
        int i12;
        MainCaptureParams mainCaptureParams5;
        Object objLock3AForCapture$default;
        AutoCloseable autoCloseable2;
        CapturePipelineImpl capturePipelineImpl4;
        boolean z9;
        Result3A result3A;
        List listListOf;
        int i13;
        boolean z10;
        int i14;
        if (!(continuation instanceof C00831) || (r7 = (i14 = (c00831 = (C00831) continuation).label) & Integer.MIN_VALUE) == 0) {
            c00831 = new C00831(continuation);
        } else {
            c00831.label = i14 - Integer.MIN_VALUE;
        }
        C00831 c00832 = c00831;
        Object objAwait = c00832.result;
        Object coroutine_suspended = IntrinsicsKt.getCOROUTINE_SUSPENDED();
        try {
            switch (c00832.label) {
                case 0:
                    ResultKt.throwOnFailure(objAwait);
                    Camera2Logger camera2Logger = Camera2Logger.INSTANCE;
                    if (Logger.isDebugEnabled("CXCP")) {
                        Log.d(Camera2Logger.TRUNCATED_TAG, "CapturePipeline#torchApplyCapture");
                    }
                    Integer num = (Integer) this.torchControl.getTorchStateLiveData().getValue();
                    i2 = (num != null && num.intValue() == 0) ? 1 : 0;
                    int i15 = (i2 != 0 || i == 0) ? 1 : 0;
                    if (Logger.isDebugEnabled("CXCP")) {
                        Log.d(Camera2Logger.TRUNCATED_TAG, "CapturePipeline#List<PipelineTask>.invoke: tasks = " + list);
                    }
                    if (list.contains(PipelineTask.PRE_CAPTURE)) {
                        if (Logger.isDebugEnabled("CXCP")) {
                            Log.d(Camera2Logger.TRUNCATED_TAG, "CapturePipeline#List<PipelineTask>.invoke: starting PRE_CAPTURE");
                        }
                        if (i2 != 0) {
                            if (Logger.isDebugEnabled("CXCP")) {
                                Log.d(Camera2Logger.TRUNCATED_TAG, "CapturePipeline#torchApplyCapture: Setting torch");
                            }
                            Deferred deferredM83setTorchAsyncOup_wC0$camera_camera2$default = TorchControl.m83setTorchAsyncOup_wC0$camera_camera2$default(this.torchControl, TorchControl.TorchMode.Companion.m95getUSED_AS_FLASHIRs_R8(), false, false, 6, null);
                            c00832.L$0 = this;
                            c00832.L$1 = list;
                            mainCaptureParams2 = mainCaptureParams;
                            c00832.L$2 = mainCaptureParams2;
                            c00832.I$0 = i;
                            c00832.J$0 = j;
                            c00832.Z$0 = z;
                            c00832.I$1 = i2;
                            c00832.I$2 = i15;
                            c00832.label = 1;
                            if (deferredM83setTorchAsyncOup_wC0$camera_camera2$default.join(c00832) != coroutine_suspended) {
                                i5 = i2;
                                capturePipelineImpl = this;
                                i6 = i15;
                                z4 = z;
                                list3 = list;
                                j3 = j;
                                i7 = i;
                                j2 = j3;
                                Camera2Logger camera2Logger2 = Camera2Logger.INSTANCE;
                                if (Logger.isDebugEnabled("CXCP")) {
                                    Log.d(Camera2Logger.TRUNCATED_TAG, "CapturePipeline#torchApplyCapture: Setting torch done");
                                }
                                if (!z4) {
                                    if (Logger.isDebugEnabled("CXCP")) {
                                        Log.d(Camera2Logger.TRUNCATED_TAG, "CapturePipeline#torchApplyCapture: Locking 3A for capture");
                                    }
                                    CameraGraph graph = this.useCaseGraphContext.getGraph();
                                    c00832.L$0 = capturePipelineImpl;
                                    c00832.L$1 = list3;
                                    c00832.L$2 = mainCaptureParams2;
                                    c00832.I$0 = i7;
                                    c00832.J$0 = j2;
                                    c00832.Z$0 = z4;
                                    c00832.I$1 = i5;
                                    c00832.I$2 = i6;
                                    c00832.label = 2;
                                    objAcquireSession = graph.acquireSession(c00832);
                                    if (objAcquireSession != coroutine_suspended) {
                                        j4 = j2;
                                        i4 = i6;
                                        objAwait = objAcquireSession;
                                        i10 = i5;
                                        mainCaptureParams4 = mainCaptureParams2;
                                        i11 = i7;
                                        long j5 = j4;
                                        list5 = list3;
                                        capturePipelineImpl3 = capturePipelineImpl;
                                        autoCloseable = (AutoCloseable) objAwait;
                                        try {
                                            CameraGraph.Session session = (CameraGraph.Session) autoCloseable;
                                            if (i11 == 0) {
                                                z6 = true;
                                            } else {
                                                z6 = false;
                                            }
                                            if (i11 == 0) {
                                                z7 = true;
                                            } else {
                                                z7 = false;
                                            }
                                            c00832.L$0 = capturePipelineImpl3;
                                            c00832.L$1 = list5;
                                            c00832.L$2 = mainCaptureParams4;
                                            c00832.L$3 = autoCloseable;
                                            c00832.I$0 = i11;
                                            c00832.Z$0 = z4;
                                            c00832.I$1 = i10;
                                            c00832.I$2 = i4;
                                            c00832.label = 3;
                                            z8 = z4;
                                            i12 = i11;
                                            boolean z11 = z7;
                                            mainCaptureParams5 = mainCaptureParams4;
                                            th2 = null;
                                            objLock3AForCapture$default = CameraGraph.Session.CC.lock3AForCapture$default(session, z6, z11, 0, j5, c00832, 4, null);
                                            if (objLock3AForCapture$default != coroutine_suspended) {
                                                autoCloseable2 = autoCloseable;
                                                objAwait = objLock3AForCapture$default;
                                                list2 = list5;
                                                mainCaptureParams2 = mainCaptureParams5;
                                                capturePipelineImpl4 = capturePipelineImpl3;
                                                z9 = z8;
                                                c00832.L$0 = capturePipelineImpl4;
                                                c00832.L$1 = list2;
                                                c00832.L$2 = mainCaptureParams2;
                                                c00832.L$3 = autoCloseable2;
                                                c00832.I$0 = i12;
                                                c00832.Z$0 = z9;
                                                c00832.I$1 = i10;
                                                c00832.I$2 = i4;
                                                c00832.label = 4;
                                                objAwait = ((Deferred) objAwait).await(c00832);
                                                if (objAwait == coroutine_suspended) {
                                                    z5 = z9;
                                                    i8 = i12;
                                                    r7 = autoCloseable2;
                                                    result3A = (Result3A) objAwait;
                                                    AutoCloseableKt.closeFinally(r7, th2);
                                                    Camera2Logger camera2Logger3 = Camera2Logger.INSTANCE;
                                                    if (Logger.isDebugEnabled("CXCP")) {
                                                        Log.d(Camera2Logger.TRUNCATED_TAG, "CapturePipeline#torchApplyCapture: Locking 3A for capture done, result3A = " + result3A);
                                                    }
                                                    i2 = i10;
                                                    capturePipelineImpl = capturePipelineImpl4;
                                                    z2 = true;
                                                    if (Logger.isDebugEnabled("CXCP")) {
                                                        Log.d(Camera2Logger.TRUNCATED_TAG, "CapturePipeline#List<PipelineTask>.invoke: PRE_CAPTURE completed");
                                                    }
                                                    i3 = i8;
                                                    z3 = z5;
                                                }
                                            }
                                        } catch (Throwable th3) {
                                            th = th3;
                                            r8 = autoCloseable;
                                            try {
                                                throw th;
                                            } catch (Throwable th4) {
                                                AutoCloseableKt.closeFinally(r8, th);
                                                throw th4;
                                            }
                                        }
                                    }
                                } else {
                                    th2 = null;
                                    if (i6 == 0) {
                                        z2 = true;
                                        i4 = i6;
                                        z5 = z4;
                                        i2 = i5;
                                        i8 = i7;
                                        list2 = list3;
                                    } else if (i7 == 0) {
                                        if (Logger.isDebugEnabled("CXCP")) {
                                            Log.d(Camera2Logger.TRUNCATED_TAG, "CapturePipeline#torchApplyCapture: Locking 3A");
                                        }
                                        c00832.L$0 = capturePipelineImpl;
                                        c00832.L$1 = list3;
                                        c00832.L$2 = mainCaptureParams2;
                                        c00832.I$0 = i7;
                                        c00832.Z$0 = z4;
                                        c00832.I$1 = i5;
                                        c00832.I$2 = i6;
                                        c00832.label = 5;
                                        z2 = true;
                                        if (lockAf(j2, true, c00832) != coroutine_suspended) {
                                            i4 = i6;
                                            z5 = z4;
                                            i9 = i5;
                                            mainCaptureParams3 = mainCaptureParams2;
                                            i8 = i7;
                                            list4 = list3;
                                            capturePipelineImpl2 = capturePipelineImpl;
                                            Camera2Logger camera2Logger4 = Camera2Logger.INSTANCE;
                                            if (Logger.isDebugEnabled("CXCP")) {
                                                Log.d(Camera2Logger.TRUNCATED_TAG, "CapturePipeline#torchApplyCapture: Locking 3A done");
                                            }
                                            i2 = i9;
                                            capturePipelineImpl = capturePipelineImpl2;
                                            list2 = list4;
                                            mainCaptureParams2 = mainCaptureParams3;
                                        }
                                    } else {
                                        z2 = true;
                                        if (Logger.isDebugEnabled("CXCP")) {
                                            Log.d(Camera2Logger.TRUNCATED_TAG, "CapturePipeline#torchApplyCapture: Awaiting 3A convergence");
                                        }
                                        function1 = new Function1() { // from class: androidx.camera.camera2.impl.CapturePipelineImpl$torchApplyCapture$3$8
                                            @Override // kotlin.jvm.functions.Function1
                                            public final Boolean invoke(FrameInfo it) {
                                                Intrinsics.checkNotNullParameter(it, "it");
                                                return Boolean.valueOf(ConvergenceUtils.is3AConverged(this.this$0.toCameraCaptureResult(it.getMetadata()), true));
                                            }
                                        };
                                        c00832.L$0 = capturePipelineImpl;
                                        c00832.L$1 = list3;
                                        c00832.L$2 = mainCaptureParams2;
                                        c00832.I$0 = i7;
                                        c00832.Z$0 = z4;
                                        c00832.I$1 = i5;
                                        c00832.I$2 = i6;
                                        c00832.label = 6;
                                        if (waitForResult(j2, function1, c00832) != coroutine_suspended) {
                                            i4 = i6;
                                            z5 = z4;
                                            i9 = i5;
                                            mainCaptureParams3 = mainCaptureParams2;
                                            i8 = i7;
                                            list4 = list3;
                                            capturePipelineImpl2 = capturePipelineImpl;
                                            Camera2Logger camera2Logger5 = Camera2Logger.INSTANCE;
                                            if (Logger.isDebugEnabled("CXCP")) {
                                                Log.d(Camera2Logger.TRUNCATED_TAG, "CapturePipeline#torchApplyCapture: 3A convergence waiting done");
                                            }
                                            i2 = i9;
                                            capturePipelineImpl = capturePipelineImpl2;
                                            list2 = list4;
                                            mainCaptureParams2 = mainCaptureParams3;
                                        }
                                    }
                                    if (Logger.isDebugEnabled("CXCP")) {
                                        Log.d(Camera2Logger.TRUNCATED_TAG, "CapturePipeline#List<PipelineTask>.invoke: PRE_CAPTURE completed");
                                    }
                                    i3 = i8;
                                    z3 = z5;
                                }
                            }
                        } else {
                            mainCaptureParams2 = mainCaptureParams;
                            i5 = i2;
                            capturePipelineImpl = this;
                            i6 = i15;
                            z4 = z;
                            list3 = list;
                            i7 = i;
                            j2 = j;
                            if (!z4) {
                                if (Logger.isDebugEnabled("CXCP")) {
                                    Log.d(Camera2Logger.TRUNCATED_TAG, "CapturePipeline#torchApplyCapture: Locking 3A for capture");
                                }
                                CameraGraph graph2 = this.useCaseGraphContext.getGraph();
                                c00832.L$0 = capturePipelineImpl;
                                c00832.L$1 = list3;
                                c00832.L$2 = mainCaptureParams2;
                                c00832.I$0 = i7;
                                c00832.J$0 = j2;
                                c00832.Z$0 = z4;
                                c00832.I$1 = i5;
                                c00832.I$2 = i6;
                                c00832.label = 2;
                                objAcquireSession = graph2.acquireSession(c00832);
                                if (objAcquireSession != coroutine_suspended) {
                                    j4 = j2;
                                    i4 = i6;
                                    objAwait = objAcquireSession;
                                    i10 = i5;
                                    mainCaptureParams4 = mainCaptureParams2;
                                    i11 = i7;
                                    long j6 = j4;
                                    list5 = list3;
                                    capturePipelineImpl3 = capturePipelineImpl;
                                    autoCloseable = (AutoCloseable) objAwait;
                                    CameraGraph.Session session2 = (CameraGraph.Session) autoCloseable;
                                    if (i11 == 0) {
                                        z6 = true;
                                    } else {
                                        z6 = false;
                                    }
                                    if (i11 == 0) {
                                        z7 = true;
                                    } else {
                                        z7 = false;
                                    }
                                    c00832.L$0 = capturePipelineImpl3;
                                    c00832.L$1 = list5;
                                    c00832.L$2 = mainCaptureParams4;
                                    c00832.L$3 = autoCloseable;
                                    c00832.I$0 = i11;
                                    c00832.Z$0 = z4;
                                    c00832.I$1 = i10;
                                    c00832.I$2 = i4;
                                    c00832.label = 3;
                                    z8 = z4;
                                    i12 = i11;
                                    boolean z12 = z7;
                                    mainCaptureParams5 = mainCaptureParams4;
                                    th2 = null;
                                    objLock3AForCapture$default = CameraGraph.Session.CC.lock3AForCapture$default(session2, z6, z12, 0, j6, c00832, 4, null);
                                    if (objLock3AForCapture$default != coroutine_suspended) {
                                        autoCloseable2 = autoCloseable;
                                        objAwait = objLock3AForCapture$default;
                                        list2 = list5;
                                        mainCaptureParams2 = mainCaptureParams5;
                                        capturePipelineImpl4 = capturePipelineImpl3;
                                        z9 = z8;
                                        c00832.L$0 = capturePipelineImpl4;
                                        c00832.L$1 = list2;
                                        c00832.L$2 = mainCaptureParams2;
                                        c00832.L$3 = autoCloseable2;
                                        c00832.I$0 = i12;
                                        c00832.Z$0 = z9;
                                        c00832.I$1 = i10;
                                        c00832.I$2 = i4;
                                        c00832.label = 4;
                                        objAwait = ((Deferred) objAwait).await(c00832);
                                        if (objAwait == coroutine_suspended) {
                                            z5 = z9;
                                            i8 = i12;
                                            r7 = autoCloseable2;
                                            result3A = (Result3A) objAwait;
                                            AutoCloseableKt.closeFinally(r7, th2);
                                            Camera2Logger camera2Logger6 = Camera2Logger.INSTANCE;
                                            if (Logger.isDebugEnabled("CXCP")) {
                                                Log.d(Camera2Logger.TRUNCATED_TAG, "CapturePipeline#torchApplyCapture: Locking 3A for capture done, result3A = " + result3A);
                                            }
                                            i2 = i10;
                                            capturePipelineImpl = capturePipelineImpl4;
                                            z2 = true;
                                            if (Logger.isDebugEnabled("CXCP")) {
                                                Log.d(Camera2Logger.TRUNCATED_TAG, "CapturePipeline#List<PipelineTask>.invoke: PRE_CAPTURE completed");
                                            }
                                            i3 = i8;
                                            z3 = z5;
                                        }
                                    }
                                }
                            } else {
                                th2 = null;
                                if (i6 == 0) {
                                    z2 = true;
                                    i4 = i6;
                                    z5 = z4;
                                    i2 = i5;
                                    i8 = i7;
                                    list2 = list3;
                                } else if (i7 == 0) {
                                    if (Logger.isDebugEnabled("CXCP")) {
                                        Log.d(Camera2Logger.TRUNCATED_TAG, "CapturePipeline#torchApplyCapture: Locking 3A");
                                    }
                                    c00832.L$0 = capturePipelineImpl;
                                    c00832.L$1 = list3;
                                    c00832.L$2 = mainCaptureParams2;
                                    c00832.I$0 = i7;
                                    c00832.Z$0 = z4;
                                    c00832.I$1 = i5;
                                    c00832.I$2 = i6;
                                    c00832.label = 5;
                                    z2 = true;
                                    if (lockAf(j2, true, c00832) != coroutine_suspended) {
                                        i4 = i6;
                                        z5 = z4;
                                        i9 = i5;
                                        mainCaptureParams3 = mainCaptureParams2;
                                        i8 = i7;
                                        list4 = list3;
                                        capturePipelineImpl2 = capturePipelineImpl;
                                        Camera2Logger camera2Logger7 = Camera2Logger.INSTANCE;
                                        if (Logger.isDebugEnabled("CXCP")) {
                                            Log.d(Camera2Logger.TRUNCATED_TAG, "CapturePipeline#torchApplyCapture: Locking 3A done");
                                        }
                                        i2 = i9;
                                        capturePipelineImpl = capturePipelineImpl2;
                                        list2 = list4;
                                        mainCaptureParams2 = mainCaptureParams3;
                                    }
                                } else {
                                    z2 = true;
                                    if (Logger.isDebugEnabled("CXCP")) {
                                        Log.d(Camera2Logger.TRUNCATED_TAG, "CapturePipeline#torchApplyCapture: Awaiting 3A convergence");
                                    }
                                    function1 = new Function1() { // from class: androidx.camera.camera2.impl.CapturePipelineImpl$torchApplyCapture$3$8
                                        @Override // kotlin.jvm.functions.Function1
                                        public final Boolean invoke(FrameInfo it) {
                                            Intrinsics.checkNotNullParameter(it, "it");
                                            return Boolean.valueOf(ConvergenceUtils.is3AConverged(this.this$0.toCameraCaptureResult(it.getMetadata()), true));
                                        }
                                    };
                                    c00832.L$0 = capturePipelineImpl;
                                    c00832.L$1 = list3;
                                    c00832.L$2 = mainCaptureParams2;
                                    c00832.I$0 = i7;
                                    c00832.Z$0 = z4;
                                    c00832.I$1 = i5;
                                    c00832.I$2 = i6;
                                    c00832.label = 6;
                                    if (waitForResult(j2, function1, c00832) != coroutine_suspended) {
                                        i4 = i6;
                                        z5 = z4;
                                        i9 = i5;
                                        mainCaptureParams3 = mainCaptureParams2;
                                        i8 = i7;
                                        list4 = list3;
                                        capturePipelineImpl2 = capturePipelineImpl;
                                        Camera2Logger camera2Logger8 = Camera2Logger.INSTANCE;
                                        if (Logger.isDebugEnabled("CXCP")) {
                                            Log.d(Camera2Logger.TRUNCATED_TAG, "CapturePipeline#torchApplyCapture: 3A convergence waiting done");
                                        }
                                        i2 = i9;
                                        capturePipelineImpl = capturePipelineImpl2;
                                        list2 = list4;
                                        mainCaptureParams2 = mainCaptureParams3;
                                    }
                                }
                                if (Logger.isDebugEnabled("CXCP")) {
                                    Log.d(Camera2Logger.TRUNCATED_TAG, "CapturePipeline#List<PipelineTask>.invoke: PRE_CAPTURE completed");
                                }
                                i3 = i8;
                                z3 = z5;
                            }
                        }
                        return coroutine_suspended;
                    }
                    mainCaptureParams2 = mainCaptureParams;
                    z2 = true;
                    th2 = null;
                    i3 = i;
                    list2 = list;
                    capturePipelineImpl = this;
                    i4 = i15;
                    z3 = z;
                    if (list2.contains(PipelineTask.MAIN_CAPTURE)) {
                        if (Logger.isDebugEnabled("CXCP")) {
                            Log.d(Camera2Logger.TRUNCATED_TAG, "CapturePipeline#List<PipelineTask>.invoke: starting MAIN_CAPTURE");
                        }
                        if (mainCaptureParams2 != null) {
                            throw new IllegalStateException("Required value was null.");
                        }
                        listListOf = capturePipelineImpl.submitRequestInternal(mainCaptureParams2);
                        if (Logger.isDebugEnabled("CXCP")) {
                            Log.d(Camera2Logger.TRUNCATED_TAG, "CapturePipeline#List<PipelineTask>.invoke: MAIN_CAPTURE completed");
                        }
                    } else {
                        listListOf = CollectionsKt.listOf(CompletableDeferredKt.CompletableDeferred(th2));
                    }
                    if (list2.contains(PipelineTask.POST_CAPTURE)) {
                        CoroutineScope sequentialScope = capturePipelineImpl.threads.getSequentialScope();
                        i13 = i4;
                        if (i2 != 0) {
                            z10 = z2;
                        } else {
                            z10 = false;
                        }
                        if (i13 == 0) {
                            z2 = false;
                        }
                        BuildersKt__Builders_commonKt.launch$default(sequentialScope, null, null, new CapturePipelineImpl$torchApplyCapture$$inlined$invoke$1(listListOf, null, z10, this, z3, z2, i3), 3, null);
                    }
                    return listListOf;
                case 1:
                    int i16 = c00832.I$2;
                    int i17 = c00832.I$1;
                    z4 = c00832.Z$0;
                    long j7 = c00832.J$0;
                    i7 = c00832.I$0;
                    MainCaptureParams mainCaptureParams6 = (MainCaptureParams) c00832.L$2;
                    list3 = (List) c00832.L$1;
                    capturePipelineImpl = (CapturePipelineImpl) c00832.L$0;
                    ResultKt.throwOnFailure(objAwait);
                    i6 = i16;
                    j3 = j7;
                    i5 = i17;
                    mainCaptureParams2 = mainCaptureParams6;
                    j2 = j3;
                    Camera2Logger camera2Logger9 = Camera2Logger.INSTANCE;
                    if (Logger.isDebugEnabled("CXCP")) {
                        Log.d(Camera2Logger.TRUNCATED_TAG, "CapturePipeline#torchApplyCapture: Setting torch done");
                    }
                    if (!z4) {
                        th2 = null;
                        if (i6 == 0) {
                            z2 = true;
                            i4 = i6;
                            z5 = z4;
                            i2 = i5;
                            i8 = i7;
                            list2 = list3;
                        } else if (i7 == 0) {
                            if (Logger.isDebugEnabled("CXCP")) {
                                Log.d(Camera2Logger.TRUNCATED_TAG, "CapturePipeline#torchApplyCapture: Locking 3A");
                            }
                            c00832.L$0 = capturePipelineImpl;
                            c00832.L$1 = list3;
                            c00832.L$2 = mainCaptureParams2;
                            c00832.I$0 = i7;
                            c00832.Z$0 = z4;
                            c00832.I$1 = i5;
                            c00832.I$2 = i6;
                            c00832.label = 5;
                            z2 = true;
                            if (lockAf(j2, true, c00832) != coroutine_suspended) {
                                i4 = i6;
                                z5 = z4;
                                i9 = i5;
                                mainCaptureParams3 = mainCaptureParams2;
                                i8 = i7;
                                list4 = list3;
                                capturePipelineImpl2 = capturePipelineImpl;
                                Camera2Logger camera2Logger10 = Camera2Logger.INSTANCE;
                                if (Logger.isDebugEnabled("CXCP")) {
                                    Log.d(Camera2Logger.TRUNCATED_TAG, "CapturePipeline#torchApplyCapture: Locking 3A done");
                                }
                                i2 = i9;
                                capturePipelineImpl = capturePipelineImpl2;
                                list2 = list4;
                                mainCaptureParams2 = mainCaptureParams3;
                            }
                        } else {
                            z2 = true;
                            if (Logger.isDebugEnabled("CXCP")) {
                                Log.d(Camera2Logger.TRUNCATED_TAG, "CapturePipeline#torchApplyCapture: Awaiting 3A convergence");
                            }
                            function1 = new Function1() { // from class: androidx.camera.camera2.impl.CapturePipelineImpl$torchApplyCapture$3$8
                                @Override // kotlin.jvm.functions.Function1
                                public final Boolean invoke(FrameInfo it) {
                                    Intrinsics.checkNotNullParameter(it, "it");
                                    return Boolean.valueOf(ConvergenceUtils.is3AConverged(this.this$0.toCameraCaptureResult(it.getMetadata()), true));
                                }
                            };
                            c00832.L$0 = capturePipelineImpl;
                            c00832.L$1 = list3;
                            c00832.L$2 = mainCaptureParams2;
                            c00832.I$0 = i7;
                            c00832.Z$0 = z4;
                            c00832.I$1 = i5;
                            c00832.I$2 = i6;
                            c00832.label = 6;
                            if (waitForResult(j2, function1, c00832) != coroutine_suspended) {
                                i4 = i6;
                                z5 = z4;
                                i9 = i5;
                                mainCaptureParams3 = mainCaptureParams2;
                                i8 = i7;
                                list4 = list3;
                                capturePipelineImpl2 = capturePipelineImpl;
                                Camera2Logger camera2Logger11 = Camera2Logger.INSTANCE;
                                if (Logger.isDebugEnabled("CXCP")) {
                                    Log.d(Camera2Logger.TRUNCATED_TAG, "CapturePipeline#torchApplyCapture: 3A convergence waiting done");
                                }
                                i2 = i9;
                                capturePipelineImpl = capturePipelineImpl2;
                                list2 = list4;
                                mainCaptureParams2 = mainCaptureParams3;
                            }
                        }
                        if (Logger.isDebugEnabled("CXCP")) {
                            Log.d(Camera2Logger.TRUNCATED_TAG, "CapturePipeline#List<PipelineTask>.invoke: PRE_CAPTURE completed");
                        }
                        i3 = i8;
                        z3 = z5;
                        if (list2.contains(PipelineTask.MAIN_CAPTURE)) {
                            if (Logger.isDebugEnabled("CXCP")) {
                                Log.d(Camera2Logger.TRUNCATED_TAG, "CapturePipeline#List<PipelineTask>.invoke: starting MAIN_CAPTURE");
                            }
                            if (mainCaptureParams2 != null) {
                                throw new IllegalStateException("Required value was null.");
                            }
                            listListOf = capturePipelineImpl.submitRequestInternal(mainCaptureParams2);
                            if (Logger.isDebugEnabled("CXCP")) {
                                Log.d(Camera2Logger.TRUNCATED_TAG, "CapturePipeline#List<PipelineTask>.invoke: MAIN_CAPTURE completed");
                            }
                        } else {
                            listListOf = CollectionsKt.listOf(CompletableDeferredKt.CompletableDeferred(th2));
                        }
                        if (list2.contains(PipelineTask.POST_CAPTURE)) {
                            CoroutineScope sequentialScope2 = capturePipelineImpl.threads.getSequentialScope();
                            i13 = i4;
                            if (i2 != 0) {
                                z10 = z2;
                            } else {
                                z10 = false;
                            }
                            if (i13 == 0) {
                                z2 = false;
                            }
                            BuildersKt__Builders_commonKt.launch$default(sequentialScope2, null, null, new CapturePipelineImpl$torchApplyCapture$$inlined$invoke$1(listListOf, null, z10, this, z3, z2, i3), 3, null);
                        }
                        return listListOf;
                    }
                    if (Logger.isDebugEnabled("CXCP")) {
                        Log.d(Camera2Logger.TRUNCATED_TAG, "CapturePipeline#torchApplyCapture: Locking 3A for capture");
                    }
                    CameraGraph graph3 = this.useCaseGraphContext.getGraph();
                    c00832.L$0 = capturePipelineImpl;
                    c00832.L$1 = list3;
                    c00832.L$2 = mainCaptureParams2;
                    c00832.I$0 = i7;
                    c00832.J$0 = j2;
                    c00832.Z$0 = z4;
                    c00832.I$1 = i5;
                    c00832.I$2 = i6;
                    c00832.label = 2;
                    objAcquireSession = graph3.acquireSession(c00832);
                    if (objAcquireSession != coroutine_suspended) {
                        j4 = j2;
                        i4 = i6;
                        objAwait = objAcquireSession;
                        i10 = i5;
                        mainCaptureParams4 = mainCaptureParams2;
                        i11 = i7;
                        long j8 = j4;
                        list5 = list3;
                        capturePipelineImpl3 = capturePipelineImpl;
                        autoCloseable = (AutoCloseable) objAwait;
                        CameraGraph.Session session3 = (CameraGraph.Session) autoCloseable;
                        if (i11 == 0) {
                            z6 = true;
                        } else {
                            z6 = false;
                        }
                        if (i11 == 0) {
                            z7 = true;
                        } else {
                            z7 = false;
                        }
                        c00832.L$0 = capturePipelineImpl3;
                        c00832.L$1 = list5;
                        c00832.L$2 = mainCaptureParams4;
                        c00832.L$3 = autoCloseable;
                        c00832.I$0 = i11;
                        c00832.Z$0 = z4;
                        c00832.I$1 = i10;
                        c00832.I$2 = i4;
                        c00832.label = 3;
                        z8 = z4;
                        i12 = i11;
                        boolean z13 = z7;
                        mainCaptureParams5 = mainCaptureParams4;
                        th2 = null;
                        objLock3AForCapture$default = CameraGraph.Session.CC.lock3AForCapture$default(session3, z6, z13, 0, j8, c00832, 4, null);
                        if (objLock3AForCapture$default != coroutine_suspended) {
                            autoCloseable2 = autoCloseable;
                            objAwait = objLock3AForCapture$default;
                            list2 = list5;
                            mainCaptureParams2 = mainCaptureParams5;
                            capturePipelineImpl4 = capturePipelineImpl3;
                            z9 = z8;
                            c00832.L$0 = capturePipelineImpl4;
                            c00832.L$1 = list2;
                            c00832.L$2 = mainCaptureParams2;
                            c00832.L$3 = autoCloseable2;
                            c00832.I$0 = i12;
                            c00832.Z$0 = z9;
                            c00832.I$1 = i10;
                            c00832.I$2 = i4;
                            c00832.label = 4;
                            objAwait = ((Deferred) objAwait).await(c00832);
                            if (objAwait == coroutine_suspended) {
                                z5 = z9;
                                i8 = i12;
                                r7 = autoCloseable2;
                                result3A = (Result3A) objAwait;
                                AutoCloseableKt.closeFinally(r7, th2);
                                Camera2Logger camera2Logger12 = Camera2Logger.INSTANCE;
                                if (Logger.isDebugEnabled("CXCP")) {
                                    Log.d(Camera2Logger.TRUNCATED_TAG, "CapturePipeline#torchApplyCapture: Locking 3A for capture done, result3A = " + result3A);
                                }
                                i2 = i10;
                                capturePipelineImpl = capturePipelineImpl4;
                                z2 = true;
                                if (Logger.isDebugEnabled("CXCP")) {
                                    Log.d(Camera2Logger.TRUNCATED_TAG, "CapturePipeline#List<PipelineTask>.invoke: PRE_CAPTURE completed");
                                }
                                i3 = i8;
                                z3 = z5;
                                if (list2.contains(PipelineTask.MAIN_CAPTURE)) {
                                    if (Logger.isDebugEnabled("CXCP")) {
                                        Log.d(Camera2Logger.TRUNCATED_TAG, "CapturePipeline#List<PipelineTask>.invoke: starting MAIN_CAPTURE");
                                    }
                                    if (mainCaptureParams2 != null) {
                                        throw new IllegalStateException("Required value was null.");
                                    }
                                    listListOf = capturePipelineImpl.submitRequestInternal(mainCaptureParams2);
                                    if (Logger.isDebugEnabled("CXCP")) {
                                        Log.d(Camera2Logger.TRUNCATED_TAG, "CapturePipeline#List<PipelineTask>.invoke: MAIN_CAPTURE completed");
                                    }
                                } else {
                                    listListOf = CollectionsKt.listOf(CompletableDeferredKt.CompletableDeferred(th2));
                                }
                                if (list2.contains(PipelineTask.POST_CAPTURE)) {
                                    CoroutineScope sequentialScope3 = capturePipelineImpl.threads.getSequentialScope();
                                    i13 = i4;
                                    if (i2 != 0) {
                                        z10 = z2;
                                    } else {
                                        z10 = false;
                                    }
                                    if (i13 == 0) {
                                        z2 = false;
                                    }
                                    BuildersKt__Builders_commonKt.launch$default(sequentialScope3, null, null, new CapturePipelineImpl$torchApplyCapture$$inlined$invoke$1(listListOf, null, z10, this, z3, z2, i3), 3, null);
                                }
                                return listListOf;
                            }
                        }
                    }
                    return coroutine_suspended;
                case 2:
                    i4 = c00832.I$2;
                    i10 = c00832.I$1;
                    z4 = c00832.Z$0;
                    long j9 = c00832.J$0;
                    i7 = c00832.I$0;
                    MainCaptureParams mainCaptureParams7 = (MainCaptureParams) c00832.L$2;
                    list3 = (List) c00832.L$1;
                    capturePipelineImpl = (CapturePipelineImpl) c00832.L$0;
                    ResultKt.throwOnFailure(objAwait);
                    j4 = j9;
                    mainCaptureParams4 = mainCaptureParams7;
                    i11 = i7;
                    long j10 = j4;
                    list5 = list3;
                    capturePipelineImpl3 = capturePipelineImpl;
                    autoCloseable = (AutoCloseable) objAwait;
                    CameraGraph.Session session4 = (CameraGraph.Session) autoCloseable;
                    if (i11 == 0) {
                        z6 = true;
                    } else {
                        z6 = false;
                    }
                    if (i11 == 0) {
                        z7 = true;
                    } else {
                        z7 = false;
                    }
                    c00832.L$0 = capturePipelineImpl3;
                    c00832.L$1 = list5;
                    c00832.L$2 = mainCaptureParams4;
                    c00832.L$3 = autoCloseable;
                    c00832.I$0 = i11;
                    c00832.Z$0 = z4;
                    c00832.I$1 = i10;
                    c00832.I$2 = i4;
                    c00832.label = 3;
                    z8 = z4;
                    i12 = i11;
                    boolean z14 = z7;
                    mainCaptureParams5 = mainCaptureParams4;
                    th2 = null;
                    objLock3AForCapture$default = CameraGraph.Session.CC.lock3AForCapture$default(session4, z6, z14, 0, j10, c00832, 4, null);
                    if (objLock3AForCapture$default != coroutine_suspended) {
                        autoCloseable2 = autoCloseable;
                        objAwait = objLock3AForCapture$default;
                        list2 = list5;
                        mainCaptureParams2 = mainCaptureParams5;
                        capturePipelineImpl4 = capturePipelineImpl3;
                        z9 = z8;
                        c00832.L$0 = capturePipelineImpl4;
                        c00832.L$1 = list2;
                        c00832.L$2 = mainCaptureParams2;
                        c00832.L$3 = autoCloseable2;
                        c00832.I$0 = i12;
                        c00832.Z$0 = z9;
                        c00832.I$1 = i10;
                        c00832.I$2 = i4;
                        c00832.label = 4;
                        objAwait = ((Deferred) objAwait).await(c00832);
                        if (objAwait == coroutine_suspended) {
                            z5 = z9;
                            i8 = i12;
                            r7 = autoCloseable2;
                            result3A = (Result3A) objAwait;
                            AutoCloseableKt.closeFinally(r7, th2);
                            Camera2Logger camera2Logger13 = Camera2Logger.INSTANCE;
                            if (Logger.isDebugEnabled("CXCP")) {
                                Log.d(Camera2Logger.TRUNCATED_TAG, "CapturePipeline#torchApplyCapture: Locking 3A for capture done, result3A = " + result3A);
                            }
                            i2 = i10;
                            capturePipelineImpl = capturePipelineImpl4;
                            z2 = true;
                            if (Logger.isDebugEnabled("CXCP")) {
                                Log.d(Camera2Logger.TRUNCATED_TAG, "CapturePipeline#List<PipelineTask>.invoke: PRE_CAPTURE completed");
                            }
                            i3 = i8;
                            z3 = z5;
                            if (list2.contains(PipelineTask.MAIN_CAPTURE)) {
                                if (Logger.isDebugEnabled("CXCP")) {
                                    Log.d(Camera2Logger.TRUNCATED_TAG, "CapturePipeline#List<PipelineTask>.invoke: starting MAIN_CAPTURE");
                                }
                                if (mainCaptureParams2 != null) {
                                    throw new IllegalStateException("Required value was null.");
                                }
                                listListOf = capturePipelineImpl.submitRequestInternal(mainCaptureParams2);
                                if (Logger.isDebugEnabled("CXCP")) {
                                    Log.d(Camera2Logger.TRUNCATED_TAG, "CapturePipeline#List<PipelineTask>.invoke: MAIN_CAPTURE completed");
                                }
                            } else {
                                listListOf = CollectionsKt.listOf(CompletableDeferredKt.CompletableDeferred(th2));
                            }
                            if (list2.contains(PipelineTask.POST_CAPTURE)) {
                                CoroutineScope sequentialScope4 = capturePipelineImpl.threads.getSequentialScope();
                                i13 = i4;
                                if (i2 != 0) {
                                    z10 = z2;
                                } else {
                                    z10 = false;
                                }
                                if (i13 == 0) {
                                    z2 = false;
                                }
                                BuildersKt__Builders_commonKt.launch$default(sequentialScope4, null, null, new CapturePipelineImpl$torchApplyCapture$$inlined$invoke$1(listListOf, null, z10, this, z3, z2, i3), 3, null);
                            }
                            return listListOf;
                        }
                    }
                    return coroutine_suspended;
                case 3:
                    i4 = c00832.I$2;
                    i10 = c00832.I$1;
                    z9 = c00832.Z$0;
                    int i18 = c00832.I$0;
                    AutoCloseable autoCloseable3 = (AutoCloseable) c00832.L$3;
                    MainCaptureParams mainCaptureParams8 = (MainCaptureParams) c00832.L$2;
                    List list6 = (List) c00832.L$1;
                    CapturePipelineImpl capturePipelineImpl5 = (CapturePipelineImpl) c00832.L$0;
                    try {
                        ResultKt.throwOnFailure(objAwait);
                        i12 = i18;
                        autoCloseable2 = autoCloseable3;
                        mainCaptureParams2 = mainCaptureParams8;
                        list2 = list6;
                        capturePipelineImpl4 = capturePipelineImpl5;
                        th2 = null;
                        c00832.L$0 = capturePipelineImpl4;
                        c00832.L$1 = list2;
                        c00832.L$2 = mainCaptureParams2;
                        c00832.L$3 = autoCloseable2;
                        c00832.I$0 = i12;
                        c00832.Z$0 = z9;
                        c00832.I$1 = i10;
                        c00832.I$2 = i4;
                        c00832.label = 4;
                        objAwait = ((Deferred) objAwait).await(c00832);
                        if (objAwait == coroutine_suspended) {
                            return coroutine_suspended;
                        }
                        z5 = z9;
                        i8 = i12;
                        r7 = autoCloseable2;
                        result3A = (Result3A) objAwait;
                        AutoCloseableKt.closeFinally(r7, th2);
                        Camera2Logger camera2Logger14 = Camera2Logger.INSTANCE;
                        if (Logger.isDebugEnabled("CXCP")) {
                            Log.d(Camera2Logger.TRUNCATED_TAG, "CapturePipeline#torchApplyCapture: Locking 3A for capture done, result3A = " + result3A);
                        }
                        i2 = i10;
                        capturePipelineImpl = capturePipelineImpl4;
                        z2 = true;
                        if (Logger.isDebugEnabled("CXCP")) {
                            Log.d(Camera2Logger.TRUNCATED_TAG, "CapturePipeline#List<PipelineTask>.invoke: PRE_CAPTURE completed");
                        }
                        i3 = i8;
                        z3 = z5;
                        if (list2.contains(PipelineTask.MAIN_CAPTURE)) {
                            if (Logger.isDebugEnabled("CXCP")) {
                                Log.d(Camera2Logger.TRUNCATED_TAG, "CapturePipeline#List<PipelineTask>.invoke: starting MAIN_CAPTURE");
                            }
                            if (mainCaptureParams2 != null) {
                                throw new IllegalStateException("Required value was null.");
                            }
                            listListOf = capturePipelineImpl.submitRequestInternal(mainCaptureParams2);
                            if (Logger.isDebugEnabled("CXCP")) {
                                Log.d(Camera2Logger.TRUNCATED_TAG, "CapturePipeline#List<PipelineTask>.invoke: MAIN_CAPTURE completed");
                            }
                        } else {
                            listListOf = CollectionsKt.listOf(CompletableDeferredKt.CompletableDeferred(th2));
                        }
                        if (list2.contains(PipelineTask.POST_CAPTURE)) {
                            CoroutineScope sequentialScope5 = capturePipelineImpl.threads.getSequentialScope();
                            i13 = i4;
                            if (i2 != 0) {
                                z10 = z2;
                            } else {
                                z10 = false;
                            }
                            if (i13 == 0) {
                                z2 = false;
                            }
                            BuildersKt__Builders_commonKt.launch$default(sequentialScope5, null, null, new CapturePipelineImpl$torchApplyCapture$$inlined$invoke$1(listListOf, null, z10, this, z3, z2, i3), 3, null);
                        }
                        return listListOf;
                    } catch (Throwable th5) {
                        th = th5;
                        r8 = autoCloseable3;
                        throw th;
                    }
                case 4:
                    i4 = c00832.I$2;
                    i10 = c00832.I$1;
                    z5 = c00832.Z$0;
                    i8 = c00832.I$0;
                    AutoCloseable autoCloseable4 = (AutoCloseable) c00832.L$3;
                    mainCaptureParams2 = (MainCaptureParams) c00832.L$2;
                    list2 = (List) c00832.L$1;
                    capturePipelineImpl4 = (CapturePipelineImpl) c00832.L$0;
                    ResultKt.throwOnFailure(objAwait);
                    th2 = null;
                    r7 = autoCloseable4;
                    result3A = (Result3A) objAwait;
                    AutoCloseableKt.closeFinally(r7, th2);
                    Camera2Logger camera2Logger15 = Camera2Logger.INSTANCE;
                    if (Logger.isDebugEnabled("CXCP")) {
                        Log.d(Camera2Logger.TRUNCATED_TAG, "CapturePipeline#torchApplyCapture: Locking 3A for capture done, result3A = " + result3A);
                    }
                    i2 = i10;
                    capturePipelineImpl = capturePipelineImpl4;
                    z2 = true;
                    if (Logger.isDebugEnabled("CXCP")) {
                        Log.d(Camera2Logger.TRUNCATED_TAG, "CapturePipeline#List<PipelineTask>.invoke: PRE_CAPTURE completed");
                    }
                    i3 = i8;
                    z3 = z5;
                    if (list2.contains(PipelineTask.MAIN_CAPTURE)) {
                        if (Logger.isDebugEnabled("CXCP")) {
                            Log.d(Camera2Logger.TRUNCATED_TAG, "CapturePipeline#List<PipelineTask>.invoke: starting MAIN_CAPTURE");
                        }
                        if (mainCaptureParams2 != null) {
                            throw new IllegalStateException("Required value was null.");
                        }
                        listListOf = capturePipelineImpl.submitRequestInternal(mainCaptureParams2);
                        if (Logger.isDebugEnabled("CXCP")) {
                            Log.d(Camera2Logger.TRUNCATED_TAG, "CapturePipeline#List<PipelineTask>.invoke: MAIN_CAPTURE completed");
                        }
                    } else {
                        listListOf = CollectionsKt.listOf(CompletableDeferredKt.CompletableDeferred(th2));
                    }
                    if (list2.contains(PipelineTask.POST_CAPTURE)) {
                        CoroutineScope sequentialScope6 = capturePipelineImpl.threads.getSequentialScope();
                        i13 = i4;
                        if (i2 != 0) {
                            z10 = z2;
                        } else {
                            z10 = false;
                        }
                        if (i13 == 0) {
                            z2 = false;
                        }
                        BuildersKt__Builders_commonKt.launch$default(sequentialScope6, null, null, new CapturePipelineImpl$torchApplyCapture$$inlined$invoke$1(listListOf, null, z10, this, z3, z2, i3), 3, null);
                    }
                    return listListOf;
                case 5:
                    i4 = c00832.I$2;
                    i9 = c00832.I$1;
                    z5 = c00832.Z$0;
                    i8 = c00832.I$0;
                    mainCaptureParams3 = (MainCaptureParams) c00832.L$2;
                    list4 = (List) c00832.L$1;
                    capturePipelineImpl2 = (CapturePipelineImpl) c00832.L$0;
                    ResultKt.throwOnFailure(objAwait);
                    z2 = true;
                    th2 = null;
                    Camera2Logger camera2Logger16 = Camera2Logger.INSTANCE;
                    if (Logger.isDebugEnabled("CXCP")) {
                        Log.d(Camera2Logger.TRUNCATED_TAG, "CapturePipeline#torchApplyCapture: Locking 3A done");
                    }
                    i2 = i9;
                    capturePipelineImpl = capturePipelineImpl2;
                    list2 = list4;
                    mainCaptureParams2 = mainCaptureParams3;
                    if (Logger.isDebugEnabled("CXCP")) {
                        Log.d(Camera2Logger.TRUNCATED_TAG, "CapturePipeline#List<PipelineTask>.invoke: PRE_CAPTURE completed");
                    }
                    i3 = i8;
                    z3 = z5;
                    if (list2.contains(PipelineTask.MAIN_CAPTURE)) {
                        if (Logger.isDebugEnabled("CXCP")) {
                            Log.d(Camera2Logger.TRUNCATED_TAG, "CapturePipeline#List<PipelineTask>.invoke: starting MAIN_CAPTURE");
                        }
                        if (mainCaptureParams2 != null) {
                            throw new IllegalStateException("Required value was null.");
                        }
                        listListOf = capturePipelineImpl.submitRequestInternal(mainCaptureParams2);
                        if (Logger.isDebugEnabled("CXCP")) {
                            Log.d(Camera2Logger.TRUNCATED_TAG, "CapturePipeline#List<PipelineTask>.invoke: MAIN_CAPTURE completed");
                        }
                    } else {
                        listListOf = CollectionsKt.listOf(CompletableDeferredKt.CompletableDeferred(th2));
                    }
                    if (list2.contains(PipelineTask.POST_CAPTURE)) {
                        CoroutineScope sequentialScope7 = capturePipelineImpl.threads.getSequentialScope();
                        i13 = i4;
                        if (i2 != 0) {
                            z10 = z2;
                        } else {
                            z10 = false;
                        }
                        if (i13 == 0) {
                            z2 = false;
                        }
                        BuildersKt__Builders_commonKt.launch$default(sequentialScope7, null, null, new CapturePipelineImpl$torchApplyCapture$$inlined$invoke$1(listListOf, null, z10, this, z3, z2, i3), 3, null);
                    }
                    return listListOf;
                case 6:
                    i4 = c00832.I$2;
                    i9 = c00832.I$1;
                    z5 = c00832.Z$0;
                    i8 = c00832.I$0;
                    mainCaptureParams3 = (MainCaptureParams) c00832.L$2;
                    list4 = (List) c00832.L$1;
                    capturePipelineImpl2 = (CapturePipelineImpl) c00832.L$0;
                    ResultKt.throwOnFailure(objAwait);
                    z2 = true;
                    th2 = null;
                    Camera2Logger camera2Logger17 = Camera2Logger.INSTANCE;
                    if (Logger.isDebugEnabled("CXCP")) {
                        Log.d(Camera2Logger.TRUNCATED_TAG, "CapturePipeline#torchApplyCapture: 3A convergence waiting done");
                    }
                    i2 = i9;
                    capturePipelineImpl = capturePipelineImpl2;
                    list2 = list4;
                    mainCaptureParams2 = mainCaptureParams3;
                    if (Logger.isDebugEnabled("CXCP")) {
                        Log.d(Camera2Logger.TRUNCATED_TAG, "CapturePipeline#List<PipelineTask>.invoke: PRE_CAPTURE completed");
                    }
                    i3 = i8;
                    z3 = z5;
                    if (list2.contains(PipelineTask.MAIN_CAPTURE)) {
                        if (Logger.isDebugEnabled("CXCP")) {
                            Log.d(Camera2Logger.TRUNCATED_TAG, "CapturePipeline#List<PipelineTask>.invoke: starting MAIN_CAPTURE");
                        }
                        if (mainCaptureParams2 != null) {
                            throw new IllegalStateException("Required value was null.");
                        }
                        listListOf = capturePipelineImpl.submitRequestInternal(mainCaptureParams2);
                        if (Logger.isDebugEnabled("CXCP")) {
                            Log.d(Camera2Logger.TRUNCATED_TAG, "CapturePipeline#List<PipelineTask>.invoke: MAIN_CAPTURE completed");
                        }
                    } else {
                        listListOf = CollectionsKt.listOf(CompletableDeferredKt.CompletableDeferred(th2));
                    }
                    if (list2.contains(PipelineTask.POST_CAPTURE)) {
                        CoroutineScope sequentialScope8 = capturePipelineImpl.threads.getSequentialScope();
                        i13 = i4;
                        if (i2 != 0) {
                            z10 = z2;
                        } else {
                            z10 = false;
                        }
                        if (i13 == 0) {
                            z2 = false;
                        }
                        BuildersKt__Builders_commonKt.launch$default(sequentialScope8, null, null, new CapturePipelineImpl$torchApplyCapture$$inlined$invoke$1(listListOf, null, z10, this, z3, z2, i3), 3, null);
                    }
                    return listListOf;
                default:
                    throw new IllegalStateException("call to 'resume' before 'invoke' with coroutine");
            }
        } catch (Throwable th6) {
            th = th6;
            r8 = r7;
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    /* JADX WARN: Code duplicated, block: B:100:0x0201  */
    /* JADX WARN: Code duplicated, block: B:67:0x017a  */
    /* JADX WARN: Code duplicated, block: B:70:0x0186 A[Catch: all -> 0x004c, TryCatch #3 {all -> 0x004c, blocks: (B:15:0x0047, B:68:0x017e, B:70:0x0186, B:71:0x018f), top: B:108:0x0047 }] */
    /* JADX WARN: Code duplicated, block: B:74:0x019b  */
    /* JADX WARN: Code duplicated, block: B:87:0x01c1  */
    /* JADX WARN: Code duplicated, block: B:89:0x01c7  */
    /* JADX WARN: Code duplicated, block: B:8:0x001a  */
    /* JADX WARN: Code duplicated, block: B:91:0x01d2  */
    /* JADX WARN: Code duplicated, block: B:93:0x01dc  */
    /* JADX WARN: Code duplicated, block: B:95:0x01e8  */
    /* JADX WARN: Code duplicated, block: B:97:0x01f0  */
    public final Object aePreCaptureApplyCapture(MainCaptureParams mainCaptureParams, long j, int i, List list, Continuation continuation) throws Exception {
        AnonymousClass1 anonymousClass1;
        MainCaptureParams mainCaptureParams2;
        int i2;
        CapturePipelineImpl capturePipelineImpl;
        List list2;
        MainCaptureParams mainCaptureParams3;
        Object obj;
        int i3;
        long j2;
        CapturePipelineImpl capturePipelineImpl2;
        AutoCloseable autoCloseable;
        AutoCloseable autoCloseable2;
        CameraGraph.Session session;
        Throwable th;
        AutoCloseable autoCloseable3;
        boolean z;
        CapturePipelineImpl capturePipelineImpl3;
        List list3;
        boolean z2;
        MainCaptureParams mainCaptureParams4;
        int i4;
        List list4;
        List list5;
        Continuation continuation2;
        List listListOf;
        List list6 = list;
        if (continuation instanceof AnonymousClass1) {
            anonymousClass1 = (AnonymousClass1) continuation;
            int i5 = anonymousClass1.label;
            if ((i5 & Integer.MIN_VALUE) != 0) {
                anonymousClass1.label = i5 - Integer.MIN_VALUE;
            } else {
                anonymousClass1 = new AnonymousClass1(continuation);
            }
        } else {
            anonymousClass1 = new AnonymousClass1(continuation);
        }
        AnonymousClass1 anonymousClass2 = anonymousClass1;
        Object obj2 = anonymousClass2.result;
        Object coroutine_suspended = IntrinsicsKt.getCOROUTINE_SUSPENDED();
        int i6 = anonymousClass2.label;
        try {
            try {
                if (i6 == 0) {
                    ResultKt.throwOnFailure(obj2);
                    Camera2Logger camera2Logger = Camera2Logger.INSTANCE;
                    if (Logger.isDebugEnabled("CXCP")) {
                        Log.d(Camera2Logger.TRUNCATED_TAG, "CapturePipeline#aePreCaptureApplyCapture");
                    }
                    if (Logger.isDebugEnabled("CXCP")) {
                        Log.d(Camera2Logger.TRUNCATED_TAG, "CapturePipeline#List<PipelineTask>.invoke: tasks = " + list6);
                    }
                    if (list6.contains(PipelineTask.PRE_CAPTURE)) {
                        if (Logger.isDebugEnabled("CXCP")) {
                            Log.d(Camera2Logger.TRUNCATED_TAG, "CapturePipeline#List<PipelineTask>.invoke: starting PRE_CAPTURE");
                        }
                        if (Logger.isDebugEnabled("CXCP")) {
                            Log.d(Camera2Logger.TRUNCATED_TAG, "CapturePipeline#aePreCaptureApplyCapture: Acquiring session for locking 3A");
                        }
                        CameraGraph graph = this.useCaseGraphContext.getGraph();
                        anonymousClass2.L$0 = this;
                        anonymousClass2.L$1 = list6;
                        anonymousClass2.L$2 = mainCaptureParams;
                        anonymousClass2.J$0 = j;
                        anonymousClass2.I$0 = i;
                        anonymousClass2.label = 1;
                        Object objAcquireSession = graph.acquireSession(anonymousClass2);
                        if (objAcquireSession != coroutine_suspended) {
                            list2 = list6;
                            mainCaptureParams3 = mainCaptureParams;
                            obj = objAcquireSession;
                            i3 = i;
                            j2 = j;
                            capturePipelineImpl2 = this;
                        }
                        return coroutine_suspended;
                    }
                    mainCaptureParams2 = mainCaptureParams;
                    i2 = i;
                    capturePipelineImpl = this;
                    if (list6.contains(PipelineTask.MAIN_CAPTURE)) {
                        if (Logger.isDebugEnabled("CXCP")) {
                            Log.d(Camera2Logger.TRUNCATED_TAG, "CapturePipeline#List<PipelineTask>.invoke: starting MAIN_CAPTURE");
                        }
                        if (mainCaptureParams2 != null) {
                            throw new IllegalStateException("Required value was null.");
                        }
                        List listSubmitRequestInternal = capturePipelineImpl.submitRequestInternal(mainCaptureParams2);
                        if (Logger.isDebugEnabled("CXCP")) {
                            Log.d(Camera2Logger.TRUNCATED_TAG, "CapturePipeline#List<PipelineTask>.invoke: MAIN_CAPTURE completed");
                        }
                        listListOf = listSubmitRequestInternal;
                        continuation2 = null;
                    } else {
                        continuation2 = null;
                        listListOf = CollectionsKt.listOf(CompletableDeferredKt.CompletableDeferred((Object) null));
                    }
                    if (list6.contains(PipelineTask.POST_CAPTURE)) {
                        BuildersKt__Builders_commonKt.launch$default(capturePipelineImpl.threads.getSequentialScope(), null, null, new CapturePipelineImpl$aePreCaptureApplyCapture$$inlined$invoke$1(listListOf, continuation2, this, i2), 3, null);
                    }
                    return listListOf;
                }
                if (i6 != 1) {
                    if (i6 != 2) {
                        if (i6 != 3) {
                            throw new IllegalStateException("call to 'resume' before 'invoke' with coroutine");
                        }
                        i4 = anonymousClass2.I$0;
                        autoCloseable3 = (AutoCloseable) anonymousClass2.L$3;
                        mainCaptureParams2 = (MainCaptureParams) anonymousClass2.L$2;
                        list5 = (List) anonymousClass2.L$1;
                        capturePipelineImpl = (CapturePipelineImpl) anonymousClass2.L$0;
                        try {
                            ResultKt.throwOnFailure(obj2);
                            Camera2Logger camera2Logger2 = Camera2Logger.INSTANCE;
                            if (Logger.isDebugEnabled("CXCP")) {
                                Log.d(Camera2Logger.TRUNCATED_TAG, "CapturePipeline#aePreCaptureApplyCapture: Locking 3A for capture done");
                            }
                            Unit unit = Unit.INSTANCE;
                            AutoCloseableKt.closeFinally(autoCloseable3, null);
                            if (Logger.isDebugEnabled("CXCP")) {
                                Log.d(Camera2Logger.TRUNCATED_TAG, "CapturePipeline#List<PipelineTask>.invoke: PRE_CAPTURE completed");
                            }
                            i2 = i4;
                            list6 = list5;
                            if (list6.contains(PipelineTask.MAIN_CAPTURE)) {
                                if (Logger.isDebugEnabled("CXCP")) {
                                    Log.d(Camera2Logger.TRUNCATED_TAG, "CapturePipeline#List<PipelineTask>.invoke: starting MAIN_CAPTURE");
                                }
                                if (mainCaptureParams2 != null) {
                                    throw new IllegalStateException("Required value was null.");
                                }
                                List listSubmitRequestInternal2 = capturePipelineImpl.submitRequestInternal(mainCaptureParams2);
                                if (Logger.isDebugEnabled("CXCP")) {
                                    Log.d(Camera2Logger.TRUNCATED_TAG, "CapturePipeline#List<PipelineTask>.invoke: MAIN_CAPTURE completed");
                                }
                                listListOf = listSubmitRequestInternal2;
                                continuation2 = null;
                            } else {
                                continuation2 = null;
                                listListOf = CollectionsKt.listOf(CompletableDeferredKt.CompletableDeferred((Object) null));
                            }
                            if (list6.contains(PipelineTask.POST_CAPTURE)) {
                                BuildersKt__Builders_commonKt.launch$default(capturePipelineImpl.threads.getSequentialScope(), null, null, new CapturePipelineImpl$aePreCaptureApplyCapture$$inlined$invoke$1(listListOf, continuation2, this, i2), 3, null);
                            }
                            return listListOf;
                        } catch (Throwable th2) {
                            th = th2;
                            try {
                                throw th;
                            } catch (Throwable th3) {
                                AutoCloseableKt.closeFinally(autoCloseable3, th);
                                throw th3;
                            }
                        }
                    }
                    i4 = anonymousClass2.I$0;
                    AutoCloseable autoCloseable4 = (AutoCloseable) anonymousClass2.L$3;
                    mainCaptureParams4 = (MainCaptureParams) anonymousClass2.L$2;
                    List list7 = (List) anonymousClass2.L$1;
                    CapturePipelineImpl capturePipelineImpl4 = (CapturePipelineImpl) anonymousClass2.L$0;
                    try {
                        ResultKt.throwOnFailure(obj2);
                        autoCloseable2 = autoCloseable4;
                        list4 = list7;
                        capturePipelineImpl = capturePipelineImpl4;
                        anonymousClass2.L$0 = capturePipelineImpl;
                        anonymousClass2.L$1 = list4;
                        anonymousClass2.L$2 = mainCaptureParams4;
                        anonymousClass2.L$3 = autoCloseable2;
                        anonymousClass2.I$0 = i4;
                        anonymousClass2.label = 3;
                        if (((Deferred) obj2).join(anonymousClass2) != coroutine_suspended) {
                            MainCaptureParams mainCaptureParams5 = mainCaptureParams4;
                            list5 = list4;
                            mainCaptureParams2 = mainCaptureParams5;
                            autoCloseable3 = autoCloseable2;
                            Camera2Logger camera2Logger3 = Camera2Logger.INSTANCE;
                            if (Logger.isDebugEnabled("CXCP")) {
                                Log.d(Camera2Logger.TRUNCATED_TAG, "CapturePipeline#aePreCaptureApplyCapture: Locking 3A for capture done");
                            }
                            Unit unit2 = Unit.INSTANCE;
                            AutoCloseableKt.closeFinally(autoCloseable3, null);
                            if (Logger.isDebugEnabled("CXCP")) {
                                Log.d(Camera2Logger.TRUNCATED_TAG, "CapturePipeline#List<PipelineTask>.invoke: PRE_CAPTURE completed");
                            }
                            i2 = i4;
                            list6 = list5;
                            if (list6.contains(PipelineTask.MAIN_CAPTURE)) {
                                if (Logger.isDebugEnabled("CXCP")) {
                                    Log.d(Camera2Logger.TRUNCATED_TAG, "CapturePipeline#List<PipelineTask>.invoke: starting MAIN_CAPTURE");
                                }
                                if (mainCaptureParams2 != null) {
                                    throw new IllegalStateException("Required value was null.");
                                }
                                List listSubmitRequestInternal3 = capturePipelineImpl.submitRequestInternal(mainCaptureParams2);
                                if (Logger.isDebugEnabled("CXCP")) {
                                    Log.d(Camera2Logger.TRUNCATED_TAG, "CapturePipeline#List<PipelineTask>.invoke: MAIN_CAPTURE completed");
                                }
                                listListOf = listSubmitRequestInternal3;
                                continuation2 = null;
                            } else {
                                continuation2 = null;
                                listListOf = CollectionsKt.listOf(CompletableDeferredKt.CompletableDeferred((Object) null));
                            }
                            if (list6.contains(PipelineTask.POST_CAPTURE)) {
                                BuildersKt__Builders_commonKt.launch$default(capturePipelineImpl.threads.getSequentialScope(), null, null, new CapturePipelineImpl$aePreCaptureApplyCapture$$inlined$invoke$1(listListOf, continuation2, this, i2), 3, null);
                            }
                            return listListOf;
                        }
                        return coroutine_suspended;
                    } catch (Throwable th4) {
                        th = th4;
                        autoCloseable3 = autoCloseable4;
                        throw th;
                    }
                }
                int i7 = anonymousClass2.I$0;
                long j3 = anonymousClass2.J$0;
                MainCaptureParams mainCaptureParams6 = (MainCaptureParams) anonymousClass2.L$2;
                List list8 = (List) anonymousClass2.L$1;
                capturePipelineImpl2 = (CapturePipelineImpl) anonymousClass2.L$0;
                ResultKt.throwOnFailure(obj2);
                i3 = i7;
                mainCaptureParams3 = mainCaptureParams6;
                list2 = list8;
                j2 = j3;
                obj = obj2;
                Object objLock3AForCapture$default = CameraGraph.Session.CC.lock3AForCapture$default(session, z2, z, 0, j2, anonymousClass2, 4, null);
                if (objLock3AForCapture$default != coroutine_suspended) {
                    mainCaptureParams4 = mainCaptureParams3;
                    i4 = i3;
                    obj2 = objLock3AForCapture$default;
                    capturePipelineImpl = capturePipelineImpl3;
                    list4 = list3;
                    anonymousClass2.L$0 = capturePipelineImpl;
                    anonymousClass2.L$1 = list4;
                    anonymousClass2.L$2 = mainCaptureParams4;
                    anonymousClass2.L$3 = autoCloseable2;
                    anonymousClass2.I$0 = i4;
                    anonymousClass2.label = 3;
                    if (((Deferred) obj2).join(anonymousClass2) != coroutine_suspended) {
                        MainCaptureParams mainCaptureParams7 = mainCaptureParams4;
                        list5 = list4;
                        mainCaptureParams2 = mainCaptureParams7;
                        autoCloseable3 = autoCloseable2;
                        Camera2Logger camera2Logger4 = Camera2Logger.INSTANCE;
                        if (Logger.isDebugEnabled("CXCP")) {
                            Log.d(Camera2Logger.TRUNCATED_TAG, "CapturePipeline#aePreCaptureApplyCapture: Locking 3A for capture done");
                        }
                        Unit unit3 = Unit.INSTANCE;
                        AutoCloseableKt.closeFinally(autoCloseable3, null);
                        if (Logger.isDebugEnabled("CXCP")) {
                            Log.d(Camera2Logger.TRUNCATED_TAG, "CapturePipeline#List<PipelineTask>.invoke: PRE_CAPTURE completed");
                        }
                        i2 = i4;
                        list6 = list5;
                        if (list6.contains(PipelineTask.MAIN_CAPTURE)) {
                            if (Logger.isDebugEnabled("CXCP")) {
                                Log.d(Camera2Logger.TRUNCATED_TAG, "CapturePipeline#List<PipelineTask>.invoke: starting MAIN_CAPTURE");
                            }
                            if (mainCaptureParams2 != null) {
                                throw new IllegalStateException("Required value was null.");
                            }
                            List listSubmitRequestInternal4 = capturePipelineImpl.submitRequestInternal(mainCaptureParams2);
                            if (Logger.isDebugEnabled("CXCP")) {
                                Log.d(Camera2Logger.TRUNCATED_TAG, "CapturePipeline#List<PipelineTask>.invoke: MAIN_CAPTURE completed");
                            }
                            listListOf = listSubmitRequestInternal4;
                            continuation2 = null;
                        } else {
                            continuation2 = null;
                            listListOf = CollectionsKt.listOf(CompletableDeferredKt.CompletableDeferred((Object) null));
                        }
                        if (list6.contains(PipelineTask.POST_CAPTURE)) {
                            BuildersKt__Builders_commonKt.launch$default(capturePipelineImpl.threads.getSequentialScope(), null, null, new CapturePipelineImpl$aePreCaptureApplyCapture$$inlined$invoke$1(listListOf, continuation2, this, i2), 3, null);
                        }
                        return listListOf;
                    }
                }
                return coroutine_suspended;
            } catch (Throwable th5) {
                th = th5;
                th = th;
                autoCloseable3 = autoCloseable2;
                throw th;
            }
            session = (CameraGraph.Session) autoCloseable;
            Camera2Logger camera2Logger5 = Camera2Logger.INSTANCE;
            if (Logger.isDebugEnabled("CXCP")) {
                try {
                    Log.d(Camera2Logger.TRUNCATED_TAG, "CapturePipeline#aePreCaptureApplyCapture: Locking 3A for capture");
                } catch (Throwable th6) {
                    th = th6;
                    autoCloseable3 = autoCloseable;
                    throw th;
                }
            }
            boolean z3 = i3 == 0;
            z = i3 == 0;
            anonymousClass2.L$0 = capturePipelineImpl2;
            anonymousClass2.L$1 = list2;
            anonymousClass2.L$2 = mainCaptureParams3;
            anonymousClass2.L$3 = autoCloseable;
            anonymousClass2.I$0 = i3;
            anonymousClass2.label = 2;
            capturePipelineImpl3 = capturePipelineImpl2;
            list3 = list2;
            z2 = z3;
            autoCloseable2 = autoCloseable;
        } catch (Throwable th7) {
            th = th7;
            autoCloseable2 = autoCloseable;
            th = th;
            autoCloseable3 = autoCloseable2;
            throw th;
        }
        autoCloseable = (AutoCloseable) obj;
    }

    /* JADX INFO: Access modifiers changed from: private */
    /* JADX WARN: Code duplicated, block: B:35:0x00b8  */
    /* JADX WARN: Code duplicated, block: B:37:0x00be  */
    /* JADX WARN: Code duplicated, block: B:39:0x00c9  */
    /* JADX WARN: Code duplicated, block: B:41:0x00d3  */
    /* JADX WARN: Code duplicated, block: B:42:0x00dd  */
    /* JADX WARN: Code duplicated, block: B:44:0x00e5  */
    /* JADX WARN: Code duplicated, block: B:47:0x00f5  */
    /* JADX WARN: Code duplicated, block: B:7:0x0013  */
    public final Object screenFlashCapture(MainCaptureParams mainCaptureParams, int i, List list, Continuation continuation) {
        C00821 c00821;
        CapturePipelineImpl capturePipelineImpl;
        List listListOf;
        if (continuation instanceof C00821) {
            c00821 = (C00821) continuation;
            int i2 = c00821.label;
            if ((i2 & Integer.MIN_VALUE) != 0) {
                c00821.label = i2 - Integer.MIN_VALUE;
            } else {
                c00821 = new C00821(continuation);
            }
        } else {
            c00821 = new C00821(continuation);
        }
        Object obj = c00821.result;
        Object coroutine_suspended = IntrinsicsKt.getCOROUTINE_SUSPENDED();
        int i3 = c00821.label;
        if (i3 == 0) {
            ResultKt.throwOnFailure(obj);
            Camera2Logger camera2Logger = Camera2Logger.INSTANCE;
            if (Logger.isDebugEnabled("CXCP")) {
                Log.d(Camera2Logger.TRUNCATED_TAG, "CapturePipeline#screenFlashCapture");
            }
            if (Logger.isDebugEnabled("CXCP")) {
                Log.d(Camera2Logger.TRUNCATED_TAG, "CapturePipeline#List<PipelineTask>.invoke: tasks = " + list);
            }
            if (list.contains(PipelineTask.PRE_CAPTURE)) {
                if (Logger.isDebugEnabled("CXCP")) {
                    Log.d(Camera2Logger.TRUNCATED_TAG, "CapturePipeline#List<PipelineTask>.invoke: starting PRE_CAPTURE");
                }
                c00821.L$0 = this;
                c00821.L$1 = list;
                c00821.L$2 = mainCaptureParams;
                c00821.I$0 = i;
                c00821.label = 1;
                if (invokeScreenFlashPreCaptureTasks(i, c00821) == coroutine_suspended) {
                    return coroutine_suspended;
                }
                capturePipelineImpl = this;
            } else {
                capturePipelineImpl = this;
            }
            if (list.contains(PipelineTask.MAIN_CAPTURE)) {
                if (Logger.isDebugEnabled("CXCP")) {
                    Log.d(Camera2Logger.TRUNCATED_TAG, "CapturePipeline#List<PipelineTask>.invoke: starting MAIN_CAPTURE");
                }
                if (mainCaptureParams != null) {
                    throw new IllegalStateException("Required value was null.");
                }
                listListOf = capturePipelineImpl.submitRequestInternal(mainCaptureParams);
                if (Logger.isDebugEnabled("CXCP")) {
                    Log.d(Camera2Logger.TRUNCATED_TAG, "CapturePipeline#List<PipelineTask>.invoke: MAIN_CAPTURE completed");
                }
            } else {
                listListOf = CollectionsKt.listOf(CompletableDeferredKt.CompletableDeferred((Object) null));
            }
            if (list.contains(PipelineTask.POST_CAPTURE)) {
                BuildersKt__Builders_commonKt.launch$default(capturePipelineImpl.threads.getSequentialScope(), null, null, new CapturePipelineImpl$screenFlashCapture$$inlined$invoke$1(listListOf, null, this, i), 3, null);
            }
            return listListOf;
        }
        if (i3 != 1) {
            throw new IllegalStateException("call to 'resume' before 'invoke' with coroutine");
        }
        i = c00821.I$0;
        mainCaptureParams = (MainCaptureParams) c00821.L$2;
        list = (List) c00821.L$1;
        capturePipelineImpl = (CapturePipelineImpl) c00821.L$0;
        ResultKt.throwOnFailure(obj);
        Camera2Logger camera2Logger2 = Camera2Logger.INSTANCE;
        if (Logger.isDebugEnabled("CXCP")) {
            Log.d(Camera2Logger.TRUNCATED_TAG, "CapturePipeline#List<PipelineTask>.invoke: PRE_CAPTURE completed");
        }
        if (list.contains(PipelineTask.MAIN_CAPTURE)) {
            if (Logger.isDebugEnabled("CXCP")) {
                Log.d(Camera2Logger.TRUNCATED_TAG, "CapturePipeline#List<PipelineTask>.invoke: starting MAIN_CAPTURE");
            }
            if (mainCaptureParams != null) {
                throw new IllegalStateException("Required value was null.");
            }
            listListOf = capturePipelineImpl.submitRequestInternal(mainCaptureParams);
            if (Logger.isDebugEnabled("CXCP")) {
                Log.d(Camera2Logger.TRUNCATED_TAG, "CapturePipeline#List<PipelineTask>.invoke: MAIN_CAPTURE completed");
            }
        } else {
            listListOf = CollectionsKt.listOf(CompletableDeferredKt.CompletableDeferred((Object) null));
        }
        if (list.contains(PipelineTask.POST_CAPTURE)) {
            BuildersKt__Builders_commonKt.launch$default(capturePipelineImpl.threads.getSequentialScope(), null, null, new CapturePipelineImpl$screenFlashCapture$$inlined$invoke$1(listListOf, null, this, i), 3, null);
        }
        return listListOf;
    }

    /* JADX WARN: Code duplicated, block: B:36:0x008a A[Catch: all -> 0x0095, TryCatch #1 {all -> 0x0095, blocks: (B:34:0x007f, B:36:0x008a, B:39:0x009b, B:43:0x00a3), top: B:62:0x007f }] */
    /* JADX WARN: Code duplicated, block: B:41:0x00a1  */
    /* JADX WARN: Code duplicated, block: B:42:0x00a2  */
    /* JADX WARN: Code duplicated, block: B:46:0x00b3  */
    /* JADX WARN: Code duplicated, block: B:8:0x0014  */
    /* JADX WARN: Code restructure failed: missing block: B:48:0x00c0, code lost:
    
        if (r15 == r0) goto L49;
     */
    /* JADX WARN: Multi-variable type inference failed */
    /* JADX WARN: Type inference failed for: r14v0, types: [int] */
    /* JADX WARN: Type inference failed for: r14v1 */
    /* JADX WARN: Type inference failed for: r14v10, types: [java.lang.AutoCloseable] */
    /* JADX WARN: Type inference failed for: r14v19 */
    /* JADX WARN: Type inference failed for: r14v2, types: [java.lang.AutoCloseable] */
    /* JADX WARN: Type inference failed for: r14v20 */
    /* JADX WARN: Type inference failed for: r14v22 */
    /* JADX WARN: Type inference failed for: r14v23 */
    /* JADX WARN: Type inference failed for: r14v24 */
    /* JADX WARN: Type inference failed for: r14v25 */
    /* JADX WARN: Type inference failed for: r14v26 */
    /* JADX WARN: Type inference failed for: r14v3, types: [int] */
    /* JADX WARN: Type inference failed for: r14v4 */
    /* JADX WARN: Type inference failed for: r14v6 */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
    */
    public final Object invokeScreenFlashPreCaptureTasks(int i, Continuation continuation) throws Exception {
        C00791 c00791;
        Throwable th;
        ?? r14;
        ?? r15;
        ?? r16;
        AutoCloseable autoCloseable;
        Object objLock3AForCapture$default;
        AutoCloseable autoCloseable2;
        if (continuation instanceof C00791) {
            c00791 = (C00791) continuation;
            int i2 = c00791.label;
            if ((i2 & Integer.MIN_VALUE) != 0) {
                c00791.label = i2 - Integer.MIN_VALUE;
            } else {
                c00791 = new C00791(continuation);
            }
        } else {
            c00791 = new C00791(continuation);
        }
        C00791 c00792 = c00791;
        Object objAcquireSession = c00792.result;
        Object coroutine_suspended = IntrinsicsKt.getCOROUTINE_SUSPENDED();
        int i3 = c00792.label;
        boolean z = true;
        try {
            if (i3 == 0) {
                ResultKt.throwOnFailure(objAcquireSession);
                FlashControl flashControl = this.flashControl;
                c00792.I$0 = i;
                c00792.label = 1;
                if (flashControl.startScreenFlashCaptureTasks(c00792) != coroutine_suspended) {
                }
                r15 = i;
                return coroutine_suspended;
            }
            if (i3 == 1) {
                int i4 = c00792.I$0;
                ResultKt.throwOnFailure(objAcquireSession);
                r15 = i4;
            } else {
                if (i3 == 2) {
                    int i5 = c00792.I$0;
                    ResultKt.throwOnFailure(objAcquireSession);
                    r16 = i5;
                    autoCloseable = (AutoCloseable) objAcquireSession;
                    try {
                        CameraGraph.Session session = (CameraGraph.Session) autoCloseable;
                        Camera2Logger camera2Logger = Camera2Logger.INSTANCE;
                        if (Logger.isDebugEnabled("CXCP")) {
                            Log.d(Camera2Logger.TRUNCATED_TAG, "screenFlashPreCapture: Locking 3A for capture");
                        }
                        long j = CapturePipelineKt.CHECK_3A_WITH_SCREEN_FLASH_TIMEOUT_IN_NS;
                        if (r16 == 0) {
                            z = false;
                        }
                        c00792.L$0 = autoCloseable;
                        c00792.label = 3;
                        objLock3AForCapture$default = CameraGraph.Session.CC.lock3AForCapture$default(session, z, true, 0, j, c00792, 4, null);
                        if (objLock3AForCapture$default != coroutine_suspended) {
                            objAcquireSession = objLock3AForCapture$default;
                            autoCloseable2 = autoCloseable;
                            c00792.L$0 = autoCloseable2;
                            c00792.label = 4;
                            objAcquireSession = ((Deferred) objAcquireSession).await(c00792);
                            i = autoCloseable2;
                        }
                        r15 = i;
                        return coroutine_suspended;
                    } catch (Throwable th2) {
                        th = th2;
                        r14 = autoCloseable;
                        try {
                            throw th;
                        } catch (Throwable th3) {
                            AutoCloseableKt.closeFinally(r14, th);
                            throw th3;
                        }
                    }
                }
                if (i3 == 3) {
                    AutoCloseable autoCloseable3 = (AutoCloseable) c00792.L$0;
                    ResultKt.throwOnFailure(objAcquireSession);
                    autoCloseable2 = autoCloseable3;
                    c00792.L$0 = autoCloseable2;
                    c00792.label = 4;
                    objAcquireSession = ((Deferred) objAcquireSession).await(c00792);
                    i = autoCloseable2;
                } else {
                    if (i3 != 4) {
                        throw new IllegalStateException("call to 'resume' before 'invoke' with coroutine");
                    }
                    AutoCloseable autoCloseable4 = (AutoCloseable) c00792.L$0;
                    ResultKt.throwOnFailure(objAcquireSession);
                    i = autoCloseable4;
                }
            }
            Result3A result3A = (Result3A) objAcquireSession;
            Camera2Logger camera2Logger2 = Camera2Logger.INSTANCE;
            if (Logger.isDebugEnabled("CXCP")) {
                Log.d(Camera2Logger.TRUNCATED_TAG, "screenFlashPreCapture: Locking 3A for capture done, result3A = " + result3A);
            }
            Unit unit = Unit.INSTANCE;
            AutoCloseableKt.closeFinally(i, null);
            return Unit.INSTANCE;
            r15 = i;
            CameraGraph graph = this.useCaseGraphContext.getGraph();
            c00792.I$0 = r15;
            c00792.label = 2;
            objAcquireSession = graph.acquireSession(c00792);
            r16 = r15;
            if (objAcquireSession != coroutine_suspended) {
                autoCloseable = (AutoCloseable) objAcquireSession;
                CameraGraph.Session session2 = (CameraGraph.Session) autoCloseable;
                Camera2Logger camera2Logger3 = Camera2Logger.INSTANCE;
                if (Logger.isDebugEnabled("CXCP")) {
                    Log.d(Camera2Logger.TRUNCATED_TAG, "screenFlashPreCapture: Locking 3A for capture");
                }
                long j2 = CapturePipelineKt.CHECK_3A_WITH_SCREEN_FLASH_TIMEOUT_IN_NS;
                if (r16 == 0) {
                    z = false;
                }
                c00792.L$0 = autoCloseable;
                c00792.label = 3;
                objLock3AForCapture$default = CameraGraph.Session.CC.lock3AForCapture$default(session2, z, true, 0, j2, c00792, 4, null);
                if (objLock3AForCapture$default != coroutine_suspended) {
                    objAcquireSession = objLock3AForCapture$default;
                    autoCloseable2 = autoCloseable;
                    c00792.L$0 = autoCloseable2;
                    c00792.label = 4;
                    objAcquireSession = ((Deferred) objAcquireSession).await(c00792);
                    i = autoCloseable2;
                }
            }
            r15 = i;
            return coroutine_suspended;
        } catch (Throwable th4) {
            th = th4;
            r14 = i;
        }
    }

    /* JADX WARN: Code duplicated, block: B:34:0x008d A[Catch: all -> 0x0098, TryCatch #1 {all -> 0x0098, blocks: (B:32:0x0082, B:34:0x008d, B:40:0x00a1), top: B:56:0x0082 }] */
    /* JADX WARN: Code duplicated, block: B:38:0x009f  */
    /* JADX WARN: Code duplicated, block: B:39:0x00a0  */
    /* JADX WARN: Code duplicated, block: B:43:0x00ac  */
    /* JADX WARN: Code duplicated, block: B:46:0x00b5 A[Catch: all -> 0x0036, TryCatch #0 {all -> 0x0036, blocks: (B:14:0x0031, B:44:0x00ad, B:46:0x00b5, B:47:0x00bf), top: B:54:0x0031 }] */
    /* JADX WARN: Code duplicated, block: B:7:0x0013  */
    public final Object invokeScreenFlashPostCaptureTasks(int i, Continuation continuation) throws Exception {
        C00781 c00781;
        AutoCloseable autoCloseable;
        Throwable th;
        AutoCloseable autoCloseable2;
        CameraGraph.Session session;
        if (continuation instanceof C00781) {
            c00781 = (C00781) continuation;
            int i2 = c00781.label;
            if ((i2 & Integer.MIN_VALUE) != 0) {
                c00781.label = i2 - Integer.MIN_VALUE;
            } else {
                c00781 = new C00781(continuation);
            }
        } else {
            c00781 = new C00781(continuation);
        }
        Object objAcquireSession = c00781.result;
        Object coroutine_suspended = IntrinsicsKt.getCOROUTINE_SUSPENDED();
        int i3 = c00781.label;
        boolean z = true;
        if (i3 == 0) {
            ResultKt.throwOnFailure(objAcquireSession);
            FlashControl flashControl = this.flashControl;
            c00781.I$0 = i;
            c00781.label = 1;
            if (flashControl.stopScreenFlashCaptureTasks(c00781) != coroutine_suspended) {
            }
            return coroutine_suspended;
        }
        if (i3 != 1) {
            if (i3 != 2) {
                if (i3 != 3) {
                    throw new IllegalStateException("call to 'resume' before 'invoke' with coroutine");
                }
                autoCloseable2 = (AutoCloseable) c00781.L$0;
                try {
                    ResultKt.throwOnFailure(objAcquireSession);
                    Camera2Logger camera2Logger = Camera2Logger.INSTANCE;
                    if (Logger.isDebugEnabled("CXCP")) {
                        Log.d(Camera2Logger.TRUNCATED_TAG, "screenFlashPostCapture: Unlocking 3A done");
                    }
                    Unit unit = Unit.INSTANCE;
                    AutoCloseableKt.closeFinally(autoCloseable2, null);
                    return Unit.INSTANCE;
                } catch (Throwable th2) {
                    th = th2;
                    try {
                        throw th;
                    } catch (Throwable th3) {
                        AutoCloseableKt.closeFinally(autoCloseable2, th);
                        throw th3;
                    }
                }
            }
            i = c00781.I$0;
            ResultKt.throwOnFailure(objAcquireSession);
            autoCloseable = (AutoCloseable) objAcquireSession;
            try {
                session = (CameraGraph.Session) autoCloseable;
                Camera2Logger camera2Logger2 = Camera2Logger.INSTANCE;
                if (Logger.isDebugEnabled("CXCP")) {
                    Log.d(Camera2Logger.TRUNCATED_TAG, "screenFlashPostCapture: Unlocking 3A");
                }
                if (i == 0) {
                    z = false;
                }
                c00781.L$0 = autoCloseable;
                c00781.label = 3;
                if (session.unlock3APostCapture(z, c00781) != coroutine_suspended) {
                    autoCloseable2 = autoCloseable;
                    Camera2Logger camera2Logger3 = Camera2Logger.INSTANCE;
                    if (Logger.isDebugEnabled("CXCP")) {
                        Log.d(Camera2Logger.TRUNCATED_TAG, "screenFlashPostCapture: Unlocking 3A done");
                    }
                    Unit unit2 = Unit.INSTANCE;
                    AutoCloseableKt.closeFinally(autoCloseable2, null);
                    return Unit.INSTANCE;
                }
                return coroutine_suspended;
            } catch (Throwable th4) {
                th = th4;
                autoCloseable2 = autoCloseable;
                throw th;
            }
        }
        i = c00781.I$0;
        ResultKt.throwOnFailure(objAcquireSession);
        Camera2Logger camera2Logger4 = Camera2Logger.INSTANCE;
        if (Logger.isDebugEnabled("CXCP")) {
            Log.d(Camera2Logger.TRUNCATED_TAG, "screenFlashPostCapture: Acquiring session for unlocking 3A");
        }
        CameraGraph graph = this.useCaseGraphContext.getGraph();
        c00781.I$0 = i;
        c00781.label = 2;
        objAcquireSession = graph.acquireSession(c00781);
        if (objAcquireSession != coroutine_suspended) {
            autoCloseable = (AutoCloseable) objAcquireSession;
            session = (CameraGraph.Session) autoCloseable;
            Camera2Logger camera2Logger5 = Camera2Logger.INSTANCE;
            if (Logger.isDebugEnabled("CXCP")) {
                Log.d(Camera2Logger.TRUNCATED_TAG, "screenFlashPostCapture: Unlocking 3A");
            }
            if (i == 0) {
                z = false;
            }
            c00781.L$0 = autoCloseable;
            c00781.label = 3;
            if (session.unlock3APostCapture(z, c00781) != coroutine_suspended) {
                autoCloseable2 = autoCloseable;
                Camera2Logger camera2Logger6 = Camera2Logger.INSTANCE;
                if (Logger.isDebugEnabled("CXCP")) {
                    Log.d(Camera2Logger.TRUNCATED_TAG, "screenFlashPostCapture: Unlocking 3A done");
                }
                Unit unit3 = Unit.INSTANCE;
                AutoCloseableKt.closeFinally(autoCloseable2, null);
                return Unit.INSTANCE;
            }
        }
        return coroutine_suspended;
    }

    /* JADX INFO: Access modifiers changed from: private */
    /* JADX WARN: Code duplicated, block: B:37:0x00d6 A[RETURN] */
    /* JADX WARN: Code duplicated, block: B:55:? A[RETURN, SYNTHETIC] */
    /* JADX WARN: Code duplicated, block: B:7:0x0017  */
    public final Object lockAf(long j, boolean z, Continuation continuation) throws Exception {
        C00811 c00811;
        boolean z2;
        long j2;
        AutoCloseable autoCloseable;
        Object obj;
        AutoCloseable autoCloseable2;
        CameraGraph.Session session;
        Lock3ABehavior lock3ABehaviorM286boximpl;
        Function1 convergeCondition;
        long j3;
        C00811 c00812;
        int i;
        Object objM231lock3AtS25XM$default;
        C00811 c00813;
        AutoCloseable autoCloseable3;
        Throwable th;
        Object objAwait;
        if (continuation instanceof C00811) {
            c00811 = (C00811) continuation;
            int i2 = c00811.label;
            if ((i2 & Integer.MIN_VALUE) != 0) {
                c00811.label = i2 - Integer.MIN_VALUE;
            } else {
                c00811 = new C00811(continuation);
            }
        } else {
            c00811 = new C00811(continuation);
        }
        Object objAcquireSession = c00811.result;
        Object coroutine_suspended = IntrinsicsKt.getCOROUTINE_SUSPENDED();
        int i3 = c00811.label;
        try {
            try {
                if (i3 == 0) {
                    ResultKt.throwOnFailure(objAcquireSession);
                    CameraGraph graph = this.useCaseGraphContext.getGraph();
                    c00811.J$0 = j;
                    z2 = z;
                    c00811.Z$0 = z2;
                    c00811.label = 1;
                    objAcquireSession = graph.acquireSession(c00811);
                    if (objAcquireSession == coroutine_suspended) {
                        return coroutine_suspended;
                    }
                    j2 = j;
                } else {
                    if (i3 == 1) {
                        z2 = c00811.Z$0;
                        long j4 = c00811.J$0;
                        ResultKt.throwOnFailure(objAcquireSession);
                        j2 = j4;
                    } else {
                        if (i3 != 2) {
                            if (i3 != 3) {
                                throw new IllegalStateException("call to 'resume' before 'invoke' with coroutine");
                            }
                            ResultKt.throwOnFailure(objAcquireSession);
                            return objAcquireSession;
                        }
                        autoCloseable3 = (AutoCloseable) c00811.L$0;
                        try {
                            ResultKt.throwOnFailure(objAcquireSession);
                            objM231lock3AtS25XM$default = objAcquireSession;
                            obj = coroutine_suspended;
                            c00813 = c00811;
                            autoCloseable2 = autoCloseable3;
                            i = 3;
                        } catch (Throwable th2) {
                            th = th2;
                            try {
                                throw th;
                            } catch (Throwable th3) {
                                AutoCloseableKt.closeFinally(autoCloseable3, th);
                                throw th3;
                            }
                        }
                    }
                    Deferred deferred = (Deferred) objM231lock3AtS25XM$default;
                    AutoCloseableKt.closeFinally(autoCloseable2, null);
                    c00813.L$0 = null;
                    c00813.label = i;
                    objAwait = deferred.await(c00813);
                    if (objAwait == obj) {
                        return obj;
                    }
                    return objAwait;
                }
                objM231lock3AtS25XM$default = CameraGraph.Session.CC.m231lock3AtS25XM$default(session, null, null, null, null, null, null, null, lock3ABehaviorM286boximpl, null, null, convergeCondition, null, 0, j2, j3, c00812, 6719, null);
                c00813 = c00812;
                if (objM231lock3AtS25XM$default == obj) {
                    return obj;
                }
                Deferred deferred2 = (Deferred) objM231lock3AtS25XM$default;
                AutoCloseableKt.closeFinally(autoCloseable2, null);
                c00813.L$0 = null;
                c00813.label = i;
                objAwait = deferred2.await(c00813);
                if (objAwait == obj) {
                    return obj;
                }
                return objAwait;
            } catch (Throwable th4) {
                th = th4;
                th = th;
                autoCloseable3 = autoCloseable2;
                throw th;
            }
            session = (CameraGraph.Session) autoCloseable;
            lock3ABehaviorM286boximpl = Lock3ABehavior.m286boximpl(Lock3ABehavior.Companion.m293getAFTER_CURRENT_SCANhRqSH3k());
            convergeCondition = getConvergeCondition(z2);
            j3 = CapturePipelineKt.CHECK_3A_TIMEOUT_IN_NS;
            c00811.L$0 = autoCloseable;
            c00811.label = 2;
            c00812 = c00811;
            autoCloseable2 = autoCloseable;
            i = 3;
        } catch (Throwable th5) {
            th = th5;
            autoCloseable2 = autoCloseable;
            th = th;
            autoCloseable3 = autoCloseable2;
            throw th;
        }
        autoCloseable = (AutoCloseable) objAcquireSession;
        obj = coroutine_suspended;
    }

    private final Function1 getConvergeCondition(final boolean z) {
        return new Function1() { // from class: androidx.camera.camera2.impl.CapturePipelineImpl$$ExternalSyntheticLambda3
            @Override // kotlin.jvm.functions.Function1
            public final Object invoke(Object obj) {
                return Boolean.valueOf(CapturePipelineImpl.getConvergeCondition$lambda$0(this.f$0, z, (FrameMetadata) obj));
            }
        };
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static final boolean getConvergeCondition$lambda$0(CapturePipelineImpl capturePipelineImpl, boolean z, FrameMetadata frameMetadata) {
        Intrinsics.checkNotNullParameter(frameMetadata, "frameMetadata");
        return ConvergenceUtils.is3AConverged(capturePipelineImpl.toCameraCaptureResult(frameMetadata), z);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public final CameraCaptureResult toCameraCaptureResult(final FrameMetadata frameMetadata) {
        return new CaptureResultAdapter(this.emptyRequestMetadata, frameMetadata.mo272getFrameNumberUgla2oM(), new FrameInfo(frameMetadata, this) { // from class: androidx.camera.camera2.impl.CapturePipelineImpl$toCameraCaptureResult$frameInfo$1
            private final String camera;
            private final FrameMetadata frameMetadata;
            private final long frameNumber;
            private final FrameMetadata metadata;
            private final RequestMetadata requestMetadata;

            @Override // androidx.camera.camera2.pipe.UnsafeWrapper
            public Object unwrapAs(KClass type) {
                Intrinsics.checkNotNullParameter(type, "type");
                return null;
            }

            {
                this.frameMetadata = frameMetadata;
                this.metadata = frameMetadata;
                this.camera = frameMetadata.mo271getCameraDz_R5H8();
                this.frameNumber = frameMetadata.mo272getFrameNumberUgla2oM();
                this.requestMetadata = this.emptyRequestMetadata;
            }

            @Override // androidx.camera.camera2.pipe.FrameInfo
            public FrameMetadata getMetadata() {
                return this.metadata;
            }
        }, null);
    }

    /* JADX INFO: Access modifiers changed from: private */
    /* JADX WARN: Code duplicated, block: B:37:0x0093 A[RETURN] */
    /* JADX WARN: Code duplicated, block: B:8:0x0018  */
    public final Object unlockAf(long j, Continuation continuation) throws Exception {
        C00851 c00851;
        long j2;
        long j3;
        AutoCloseable autoCloseable;
        Throwable th;
        AutoCloseable autoCloseable2;
        Object objAwait;
        if (continuation instanceof C00851) {
            c00851 = (C00851) continuation;
            int i = c00851.label;
            if ((i & Integer.MIN_VALUE) != 0) {
                c00851.label = i - Integer.MIN_VALUE;
            } else {
                c00851 = new C00851(continuation);
            }
        } else {
            c00851 = new C00851(continuation);
        }
        C00851 c00852 = c00851;
        Object objAcquireSession = c00852.result;
        Object coroutine_suspended = IntrinsicsKt.getCOROUTINE_SUSPENDED();
        int i2 = c00852.label;
        try {
            if (i2 == 0) {
                ResultKt.throwOnFailure(objAcquireSession);
                CameraGraph graph = this.useCaseGraphContext.getGraph();
                j2 = j;
                c00852.J$0 = j2;
                c00852.label = 1;
                objAcquireSession = graph.acquireSession(c00852);
                if (objAcquireSession != coroutine_suspended) {
                }
                return coroutine_suspended;
            }
            if (i2 != 1) {
                if (i2 != 2) {
                    if (i2 != 3) {
                        throw new IllegalStateException("call to 'resume' before 'invoke' with coroutine");
                    }
                    ResultKt.throwOnFailure(objAcquireSession);
                    return objAcquireSession;
                }
                autoCloseable2 = (AutoCloseable) c00852.L$0;
                try {
                    ResultKt.throwOnFailure(objAcquireSession);
                    Deferred deferred = (Deferred) objAcquireSession;
                    AutoCloseableKt.closeFinally(autoCloseable2, null);
                    c00852.L$0 = null;
                    c00852.label = 3;
                    objAwait = deferred.await(c00852);
                    if (objAwait != coroutine_suspended) {
                        return coroutine_suspended;
                    }
                    return objAwait;
                } catch (Throwable th2) {
                    th = th2;
                    try {
                        throw th;
                    } catch (Throwable th3) {
                        AutoCloseableKt.closeFinally(autoCloseable2, th);
                        throw th3;
                    }
                }
            }
            j2 = c00852.J$0;
            ResultKt.throwOnFailure(objAcquireSession);
            Boolean boolBoxBoolean = Boxing.boxBoolean(true);
            c00852.L$0 = autoCloseable;
            c00852.label = 2;
            objAcquireSession = CameraGraph.Session.CC.unlock3A$default((CameraGraph.Session) autoCloseable, null, boolBoxBoolean, null, null, 0, j3, c00852, 29, null);
            if (objAcquireSession != coroutine_suspended) {
                autoCloseable2 = autoCloseable;
                Deferred deferred2 = (Deferred) objAcquireSession;
                AutoCloseableKt.closeFinally(autoCloseable2, null);
                c00852.L$0 = null;
                c00852.label = 3;
                objAwait = deferred2.await(c00852);
                if (objAwait != coroutine_suspended) {
                    return objAwait;
                }
            }
            return coroutine_suspended;
        } catch (Throwable th4) {
            th = th4;
            autoCloseable2 = autoCloseable;
            throw th;
        }
        j3 = j2;
        autoCloseable = (AutoCloseable) objAcquireSession;
    }

    private final List submitRequestInternal(MainCaptureParams mainCaptureParams) {
        Camera2Logger camera2Logger = Camera2Logger.INSTANCE;
        if (Logger.isDebugEnabled("CXCP")) {
            Log.d(Camera2Logger.TRUNCATED_TAG, "CapturePipeline#submitRequestInternal; Submitting " + mainCaptureParams.getConfigs() + " with CameraPipe");
        }
        ArrayList arrayList = new ArrayList();
        List configs = mainCaptureParams.getConfigs();
        ArrayList arrayList2 = new ArrayList();
        Iterator it = configs.iterator();
        while (true) {
            Request requestM16mapToRequestnAberiA = null;
            if (!it.hasNext()) {
                break;
            }
            CaptureConfig captureConfig = (CaptureConfig) it.next();
            final CompletableDeferred completableDeferredCompletableDeferred$default = CompletableDeferredKt.CompletableDeferred$default(null, 1, null);
            arrayList.add(completableDeferredCompletableDeferred$default);
            try {
                requestM16mapToRequestnAberiA = this.configAdapter.m16mapToRequestnAberiA(captureConfig, mainCaptureParams.m72getRequestTemplatefGx8uWA(), mainCaptureParams.getSessionConfigOptions(), CollectionsKt.listOf(new Request.Listener() { // from class: androidx.camera.camera2.impl.CapturePipelineImpl$submitRequestInternal$requests$1$1
                    @Override // androidx.camera.camera2.pipe.Request.Listener
                    /* JADX INFO: renamed from: onBufferLost-DlC0U5Y */
                    public /* synthetic */ void mo18onBufferLostDlC0U5Y(RequestMetadata requestMetadata, long j, int i) {
                        Intrinsics.checkNotNullParameter(requestMetadata, "requestMetadata");
                    }

                    @Override // androidx.camera.camera2.pipe.Request.Listener
                    /* JADX INFO: renamed from: onBufferLost-iiEMlm4 */
                    public /* synthetic */ void mo19onBufferLostiiEMlm4(RequestMetadata requestMetadata, long j, int i, int i2) {
                        Intrinsics.checkNotNullParameter(requestMetadata, "requestMetadata");
                    }

                    @Override // androidx.camera.camera2.pipe.Request.Listener
                    public /* synthetic */ void onCaptureProgress(RequestMetadata requestMetadata, int i) {
                        Intrinsics.checkNotNullParameter(requestMetadata, "requestMetadata");
                    }

                    @Override // androidx.camera.camera2.pipe.Request.Listener
                    /* JADX INFO: renamed from: onComplete-CcXjc1I */
                    public /* synthetic */ void mo20onCompleteCcXjc1I(RequestMetadata requestMetadata, long j, FrameInfo frameInfo) {
                        Request.Listener.CC.m371$default$onCompleteCcXjc1I(this, requestMetadata, j, frameInfo);
                    }

                    @Override // androidx.camera.camera2.pipe.Request.Listener
                    /* JADX INFO: renamed from: onPartialCaptureResult-CcXjc1I */
                    public /* synthetic */ void mo22onPartialCaptureResultCcXjc1I(RequestMetadata requestMetadata, long j, FrameMetadata frameMetadata) {
                        Request.Listener.CC.m373$default$onPartialCaptureResultCcXjc1I(this, requestMetadata, j, frameMetadata);
                    }

                    @Override // androidx.camera.camera2.pipe.Request.Listener
                    /* JADX INFO: renamed from: onReadoutStarted-mP9r-9w */
                    public /* synthetic */ void mo23onReadoutStartedmP9r9w(RequestMetadata requestMetadata, long j, long j2) {
                        Intrinsics.checkNotNullParameter(requestMetadata, "requestMetadata");
                    }

                    @Override // androidx.camera.camera2.pipe.Request.Listener
                    public /* synthetic */ void onRequestSequenceAborted(RequestMetadata requestMetadata) {
                        Intrinsics.checkNotNullParameter(requestMetadata, "requestMetadata");
                    }

                    @Override // androidx.camera.camera2.pipe.Request.Listener
                    /* JADX INFO: renamed from: onRequestSequenceCompleted-RuT0dZU */
                    public /* synthetic */ void mo24onRequestSequenceCompletedRuT0dZU(RequestMetadata requestMetadata, long j) {
                        Intrinsics.checkNotNullParameter(requestMetadata, "requestMetadata");
                    }

                    @Override // androidx.camera.camera2.pipe.Request.Listener
                    public /* synthetic */ void onRequestSequenceCreated(RequestMetadata requestMetadata) {
                        Intrinsics.checkNotNullParameter(requestMetadata, "requestMetadata");
                    }

                    @Override // androidx.camera.camera2.pipe.Request.Listener
                    public /* synthetic */ void onRequestSequenceSubmitted(RequestMetadata requestMetadata) {
                        Intrinsics.checkNotNullParameter(requestMetadata, "requestMetadata");
                    }

                    @Override // androidx.camera.camera2.pipe.Request.Listener
                    /* JADX INFO: renamed from: onStarted-uGKBvU4 */
                    public /* synthetic */ void mo25onStarteduGKBvU4(RequestMetadata requestMetadata, long j, long j2) {
                        Intrinsics.checkNotNullParameter(requestMetadata, "requestMetadata");
                    }

                    @Override // androidx.camera.camera2.pipe.Request.Listener
                    public void onAborted(Request request) {
                        Intrinsics.checkNotNullParameter(request, "request");
                        completableDeferredCompletableDeferred$default.completeExceptionally(new ImageCaptureException(3, "Capture request is cancelled because camera is closed", null));
                    }

                    @Override // androidx.camera.camera2.pipe.Request.Listener
                    /* JADX INFO: renamed from: onTotalCaptureResult-CcXjc1I */
                    public void mo26onTotalCaptureResultCcXjc1I(RequestMetadata requestMetadata, long j, FrameInfo totalCaptureResult) {
                        Intrinsics.checkNotNullParameter(requestMetadata, "requestMetadata");
                        Intrinsics.checkNotNullParameter(totalCaptureResult, "totalCaptureResult");
                        completableDeferredCompletableDeferred$default.complete(null);
                    }

                    @Override // androidx.camera.camera2.pipe.Request.Listener
                    /* JADX INFO: renamed from: onFailed-CcXjc1I */
                    public void mo21onFailedCcXjc1I(RequestMetadata requestMetadata, long j, RequestFailure requestFailure) {
                        Intrinsics.checkNotNullParameter(requestMetadata, "requestMetadata");
                        Intrinsics.checkNotNullParameter(requestFailure, "requestFailure");
                        completableDeferredCompletableDeferred$default.completeExceptionally(new ImageCaptureException(2, "Capture request failed with reason " + requestFailure.getReason(), null));
                    }
                }));
            } catch (IllegalStateException e) {
                Camera2Logger camera2Logger2 = Camera2Logger.INSTANCE;
                if (Logger.isInfoEnabled("CXCP")) {
                    Log.i(Camera2Logger.TRUNCATED_TAG, "CapturePipeline#submitRequestInternal: configAdapter.mapToRequest failed!", e);
                }
                completableDeferredCompletableDeferred$default.completeExceptionally(new ImageCaptureException(2, "Capture request failed with reason " + e.getMessage(), e));
            }
            if (requestM16mapToRequestnAberiA != null) {
                arrayList2.add(requestM16mapToRequestnAberiA);
            }
        }
        if (arrayList2.isEmpty()) {
            return arrayList;
        }
        BuildersKt__Builders_commonKt.launch$default(this.threads.getSequentialScope(), null, null, new CapturePipelineImpl$submitRequestInternal$$inlined$confineLaunch$1(null, this, arrayList, arrayList2), 3, null);
        return arrayList;
    }

    /* JADX INFO: Access modifiers changed from: private */
    /* JADX WARN: Code duplicated, block: B:24:0x0046  */
    /* JADX WARN: Code duplicated, block: B:7:0x0013  */
    public final Object isPhysicalFlashRequired(int i, Continuation continuation) {
        C00801 c00801;
        if (continuation instanceof C00801) {
            c00801 = (C00801) continuation;
            int i2 = c00801.label;
            if ((i2 & Integer.MIN_VALUE) != 0) {
                c00801.label = i2 - Integer.MIN_VALUE;
            } else {
                c00801 = new C00801(continuation);
            }
        } else {
            c00801 = new C00801(continuation);
        }
        Object frameMetadata = c00801.result;
        Object coroutine_suspended = IntrinsicsKt.getCOROUTINE_SUSPENDED();
        int i3 = c00801.label;
        boolean z = false;
        if (i3 == 0) {
            ResultKt.throwOnFailure(frameMetadata);
            if (i == 0) {
                c00801.label = 1;
                frameMetadata = getFrameMetadata(c00801);
                if (frameMetadata == coroutine_suspended) {
                    return coroutine_suspended;
                }
            } else if (i == 1) {
                z = true;
            } else if (i != 2 && i != 3) {
                throw new AssertionError(i);
            }
            return Boxing.boxBoolean(z);
        }
        if (i3 != 1) {
            throw new IllegalStateException("call to 'resume' before 'invoke' with coroutine");
        }
        ResultKt.throwOnFailure(frameMetadata);
        FrameMetadata frameMetadata2 = (FrameMetadata) frameMetadata;
        if (frameMetadata2 != null) {
            CaptureResult.Key CONTROL_AE_STATE = CaptureResult.CONTROL_AE_STATE;
            Intrinsics.checkNotNullExpressionValue(CONTROL_AE_STATE, "CONTROL_AE_STATE");
            Integer num = (Integer) frameMetadata2.get(CONTROL_AE_STATE);
            if (num != null && num.intValue() == 4) {
                z = true;
            }
        }
        return Boxing.boxBoolean(z);
    }

    /* JADX INFO: Access modifiers changed from: private */
    /* JADX WARN: Code duplicated, block: B:7:0x0013  */
    public final Object waitForResult(long j, Function1 function1, Continuation continuation) {
        C00861 c00861;
        ResultListener resultListener;
        if (continuation instanceof C00861) {
            c00861 = (C00861) continuation;
            int i = c00861.label;
            if ((i & Integer.MIN_VALUE) != 0) {
                c00861.label = i - Integer.MIN_VALUE;
            } else {
                c00861 = new C00861(continuation);
            }
        } else {
            c00861 = new C00861(continuation);
        }
        Object obj = c00861.result;
        Object coroutine_suspended = IntrinsicsKt.getCOROUTINE_SUSPENDED();
        int i2 = c00861.label;
        if (i2 == 0) {
            ResultKt.throwOnFailure(obj);
            ResultListener resultListener2 = new ResultListener(j, function1);
            this.requestListener.addListener(resultListener2, this.threads.getSequentialExecutor());
            BuildersKt__Builders_commonKt.launch$default(this.threads.getSequentialScope(), null, null, new CapturePipelineImpl$waitForResult$resultListener$1$1(resultListener2, this, null), 3, null);
            long millis = TimeUnit.NANOSECONDS.toMillis(j);
            AnonymousClass3 anonymousClass3 = new AnonymousClass3(resultListener2, null);
            c00861.L$0 = resultListener2;
            c00861.label = 1;
            Object objWithTimeoutOrNull = TimeoutKt.withTimeoutOrNull(millis, anonymousClass3, c00861);
            if (objWithTimeoutOrNull == coroutine_suspended) {
                return coroutine_suspended;
            }
            obj = objWithTimeoutOrNull;
            resultListener = resultListener2;
        } else {
            if (i2 != 1) {
                throw new IllegalStateException("call to 'resume' before 'invoke' with coroutine");
            }
            resultListener = (ResultListener) c00861.L$0;
            ResultKt.throwOnFailure(obj);
        }
        if (((FrameInfo) obj) == null) {
            this.requestListener.removeListener(resultListener);
        }
        return obj;
    }

    static /* synthetic */ Object waitForResult$default(CapturePipelineImpl capturePipelineImpl, long j, Function1 function1, Continuation continuation, int i, Object obj) {
        if ((i & 2) != 0) {
            function1 = new Function1() { // from class: androidx.camera.camera2.impl.CapturePipelineImpl$$ExternalSyntheticLambda0
                @Override // kotlin.jvm.functions.Function1
                public final Object invoke(Object obj2) {
                    return Boolean.valueOf(CapturePipelineImpl.waitForResult$lambda$0((FrameInfo) obj2));
                }
            };
        }
        return capturePipelineImpl.waitForResult(j, function1, continuation);
    }

    /* JADX INFO: renamed from: androidx.camera.camera2.impl.CapturePipelineImpl$waitForResult$3, reason: invalid class name */
    static final class AnonymousClass3 extends SuspendLambda implements Function2 {
        final /* synthetic */ ResultListener $resultListener;
        int label;

        /* JADX WARN: 'super' call moved to the top of the method (can break code semantics) */
        AnonymousClass3(ResultListener resultListener, Continuation continuation) {
            super(2, continuation);
            this.$resultListener = resultListener;
        }

        @Override // kotlin.coroutines.jvm.internal.BaseContinuationImpl
        public final Continuation create(Object obj, Continuation continuation) {
            return new AnonymousClass3(this.$resultListener, continuation);
        }

        @Override // kotlin.jvm.functions.Function2
        public final Object invoke(CoroutineScope coroutineScope, Continuation continuation) {
            return ((AnonymousClass3) create(coroutineScope, continuation)).invokeSuspend(Unit.INSTANCE);
        }

        @Override // kotlin.coroutines.jvm.internal.BaseContinuationImpl
        public final Object invokeSuspend(Object obj) {
            Object coroutine_suspended = IntrinsicsKt.getCOROUTINE_SUSPENDED();
            int i = this.label;
            if (i != 0) {
                if (i != 1) {
                    throw new IllegalStateException("call to 'resume' before 'invoke' with coroutine");
                }
                ResultKt.throwOnFailure(obj);
                return obj;
            }
            ResultKt.throwOnFailure(obj);
            Deferred result = this.$resultListener.getResult();
            this.label = 1;
            Object objAwait = result.await(this);
            return objAwait == coroutine_suspended ? coroutine_suspended : objAwait;
        }
    }

    private final Object isTorchAsFlash(int i, Continuation continuation) {
        if (getTemplate() != 3 && i != 1) {
            return this.useTorchAsFlash.shouldUseTorchAsFlash(new AnonymousClass2(null), continuation);
        }
        return Boxing.boxBoolean(true);
    }

    /* JADX INFO: renamed from: androidx.camera.camera2.impl.CapturePipelineImpl$isTorchAsFlash$2, reason: invalid class name */
    static final class AnonymousClass2 extends SuspendLambda implements Function1 {
        int label;

        AnonymousClass2(Continuation continuation) {
            super(1, continuation);
        }

        @Override // kotlin.coroutines.jvm.internal.BaseContinuationImpl
        public final Continuation create(Continuation continuation) {
            return CapturePipelineImpl.this.new AnonymousClass2(continuation);
        }

        @Override // kotlin.jvm.functions.Function1
        public final Object invoke(Continuation continuation) {
            return ((AnonymousClass2) create(continuation)).invokeSuspend(Unit.INSTANCE);
        }

        @Override // kotlin.coroutines.jvm.internal.BaseContinuationImpl
        public final Object invokeSuspend(Object obj) {
            Object coroutine_suspended = IntrinsicsKt.getCOROUTINE_SUSPENDED();
            int i = this.label;
            if (i != 0) {
                if (i != 1) {
                    throw new IllegalStateException("call to 'resume' before 'invoke' with coroutine");
                }
                ResultKt.throwOnFailure(obj);
                return obj;
            }
            ResultKt.throwOnFailure(obj);
            CapturePipelineImpl capturePipelineImpl = CapturePipelineImpl.this;
            this.label = 1;
            Object frameMetadata = capturePipelineImpl.getFrameMetadata(this);
            return frameMetadata == coroutine_suspended ? coroutine_suspended : frameMetadata;
        }
    }
}
