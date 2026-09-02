package androidx.camera.camera2.pipe.internal;

import androidx.appcompat.app.WindowDecorActionBar$$ExternalSyntheticThrowCCEIfNotNull0;
import androidx.camera.camera2.config.UseCaseGraphContext$$ExternalSyntheticAutoCloseableDispatcher0;
import androidx.camera.camera2.pipe.CameraStream;
import androidx.camera.camera2.pipe.FrameId;
import androidx.camera.camera2.pipe.OutputStatus;
import androidx.camera.camera2.pipe.OutputStream;
import androidx.camera.camera2.pipe.RequestMetadata;
import androidx.camera.camera2.pipe.StreamId;
import androidx.camera.camera2.pipe.media.OutputImage;
import androidx.camera.camera2.pipe.media.SharedOutputImage;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Set;
import java.util.concurrent.CopyOnWriteArrayList;
import kotlin.collections.CollectionsKt;
import kotlin.enums.EnumEntries;
import kotlin.enums.EnumEntriesKt;
import kotlin.jvm.internal.DefaultConstructorMarker;
import kotlin.jvm.internal.Intrinsics;
import kotlinx.atomicfu.AtomicFU;
import kotlinx.atomicfu.AtomicInt;
import kotlinx.atomicfu.AtomicLong;
import kotlinx.atomicfu.AtomicRef;
import kotlinx.coroutines.CompletableDeferred;
import kotlinx.coroutines.CompletableDeferredKt;

public final class FrameState {
    public static final Companion Companion = new Companion(null);
    private static final AtomicLong frameIds = AtomicFU.atomic(0L);
    private final long frameId;
    private final FrameInfoOutput frameInfoOutput;
    private final long frameNumber;
    private final long frameTimestamp;
    private final List imageOutputs;
    private final CopyOnWriteArrayList listenerStates;
    private final AtomicInt remainingStreamCount;
    private final RequestMetadata requestMetadata;
    private final AtomicRef state;

    private enum State {
        STARTED,
        FRAME_INFO_COMPLETE,
        STREAM_RESULTS_COMPLETE,
        COMPLETE;

        private static final /* synthetic */ EnumEntries $ENTRIES = EnumEntriesKt.enumEntries(values());
    }

    public static final /* synthetic */ class WhenMappings {
        public static final /* synthetic */ int[] $EnumSwitchMapping$0;

        static {
            int[] iArr = new int[State.values().length];
            try {
                iArr[State.STARTED.ordinal()] = 1;
            } catch (NoSuchFieldError unused) {
            }
            try {
                iArr[State.FRAME_INFO_COMPLETE.ordinal()] = 2;
            } catch (NoSuchFieldError unused2) {
            }
            try {
                iArr[State.STREAM_RESULTS_COMPLETE.ordinal()] = 3;
            } catch (NoSuchFieldError unused3) {
            }
            try {
                iArr[State.COMPLETE.ordinal()] = 4;
            } catch (NoSuchFieldError unused4) {
            }
            $EnumSwitchMapping$0 = iArr;
        }
    }

    public /* synthetic */ FrameState(RequestMetadata requestMetadata, long j, long j2, Set set, DefaultConstructorMarker defaultConstructorMarker) {
        this(requestMetadata, j, j2, set);
    }

    public final class ImageOutput extends FrameOutput implements OutputDistributor.OutputListener {
        private final int outputId;
        private final AtomicInt remainingOutputResults;
        private final int streamId;
        final /* synthetic */ FrameState this$0;

        public /* synthetic */ ImageOutput(FrameState frameState, int i, int i2, AtomicInt atomicInt, DefaultConstructorMarker defaultConstructorMarker) {
            this(frameState, i, i2, atomicInt);
        }

        @Override // androidx.camera.camera2.pipe.internal.OutputDistributor.OutputListener
        /* JADX INFO: renamed from: onOutputComplete-3ejhThk */
        public void mo576onOutputComplete3ejhThk(long j, long j2, long j3, long j4, Object obj) throws Exception {
            int iM308unboximpl;
            OutputImage outputImage = (OutputImage) (OutputResult.m590getAvailableimpl(obj) ? obj : null);
            if (outputImage != null) {
                SharedOutputImage sharedOutputImageFrom = SharedOutputImage.Companion.from(outputImage);
                if (!getInternalResult().complete(OutputResult.m587boximpl(OutputResult.m588constructorimpl(sharedOutputImageFrom)))) {
                    UseCaseGraphContext$$ExternalSyntheticAutoCloseableDispatcher0.m(sharedOutputImageFrom);
                }
            } else {
                CompletableDeferred internalResult = getInternalResult();
                if (OutputResult.m590getAvailableimpl(obj)) {
                    iM308unboximpl = OutputStatus.Companion.m309getAVAILABLEU7r42EA();
                } else if (obj == null) {
                    iM308unboximpl = OutputStatus.Companion.m313getUNAVAILABLEU7r42EA();
                } else {
                    iM308unboximpl = ((OutputStatus) obj).m308unboximpl();
                }
                internalResult.complete(OutputResult.m587boximpl(OutputResult.m588constructorimpl(OutputStatus.m303boximpl(iM308unboximpl))));
            }
            if (this.remainingOutputResults.decrementAndGet() == 0) {
                Iterator it = this.this$0.listenerStates.iterator();
                Intrinsics.checkNotNullExpressionValue(it, "iterator(...)");
                if (it.hasNext()) {
                    WindowDecorActionBar$$ExternalSyntheticThrowCCEIfNotNull0.m(it.next());
                    throw null;
                }
                this.this$0.m573onStreamResultCompleteaKI5c8E(this.streamId);
            }
        }

        private ImageOutput(FrameState frameState, int i, int i2, AtomicInt remainingOutputResults) {
            Intrinsics.checkNotNullParameter(remainingOutputResults, "remainingOutputResults");
            this.this$0 = frameState;
            this.streamId = i;
            this.outputId = i2;
            this.remainingOutputResults = remainingOutputResults;
        }

        /* JADX INFO: renamed from: getStreamId-ptHMqGs, reason: not valid java name */
        public final int m578getStreamIdptHMqGs() {
            return this.streamId;
        }

        /* JADX INFO: renamed from: getOutputId-4LaLFng, reason: not valid java name */
        public final int m577getOutputId4LaLFng() {
            return this.outputId;
        }

        @Override // androidx.camera.camera2.pipe.internal.FrameState.FrameOutput
        protected void release() throws Exception {
            OutputResult.Companion companion = OutputResult.Companion;
            CompletableDeferred internalResult = getInternalResult();
            Object obj = null;
            if (internalResult.isCompleted() && !internalResult.isCancelled()) {
                Object objM594unboximpl = ((OutputResult) internalResult.getCompleted()).m594unboximpl();
                if (OutputResult.m590getAvailableimpl(objM594unboximpl)) {
                    obj = objM594unboximpl;
                }
            }
            SharedOutputImage sharedOutputImage = (SharedOutputImage) obj;
            if (sharedOutputImage != null) {
                UseCaseGraphContext$$ExternalSyntheticAutoCloseableDispatcher0.m(sharedOutputImage);
            }
        }
    }

    private FrameState(RequestMetadata requestMetadata, long j, long j2, Set imageStreams) {
        Object next;
        Intrinsics.checkNotNullParameter(requestMetadata, "requestMetadata");
        Intrinsics.checkNotNullParameter(imageStreams, "imageStreams");
        this.requestMetadata = requestMetadata;
        this.frameNumber = j;
        this.frameTimestamp = j2;
        this.frameId = Companion.m575nextFrameIdOMxQvVY();
        this.frameInfoOutput = new FrameInfoOutput();
        List listCreateListBuilder = CollectionsKt.createListBuilder();
        Iterator it = requestMetadata.getStreams().keySet().iterator();
        while (it.hasNext()) {
            int iM423unboximpl = ((StreamId) it.next()).m423unboximpl();
            Iterator it2 = imageStreams.iterator();
            do {
                if (!it2.hasNext()) {
                    next = null;
                    break;
                }
                next = it2.next();
            } while (!StreamId.m420equalsimpl0(((CameraStream) next).m247getIdptHMqGs(), iM423unboximpl));
            CameraStream cameraStream = (CameraStream) next;
            if (cameraStream != null) {
                List outputs = cameraStream.getOutputs();
                AtomicInt atomicIntAtomic = AtomicFU.atomic(outputs.size());
                int size = outputs.size();
                for (int i = 0; i < size; i++) {
                    listCreateListBuilder.add(new ImageOutput(this, iM423unboximpl, ((OutputStream) outputs.get(i)).mo317getId4LaLFng(), atomicIntAtomic, null));
                }
            }
        }
        List listBuild = CollectionsKt.build(listCreateListBuilder);
        this.imageOutputs = listBuild;
        this.state = AtomicFU.atomic(State.STARTED);
        List list = listBuild;
        ArrayList arrayList = new ArrayList(CollectionsKt.collectionSizeOrDefault(list, 10));
        Iterator it3 = list.iterator();
        while (it3.hasNext()) {
            arrayList.add(StreamId.m417boximpl(((ImageOutput) it3.next()).m578getStreamIdptHMqGs()));
        }
        this.remainingStreamCount = AtomicFU.atomic(CollectionsKt.distinct(arrayList).size());
        this.listenerStates = new CopyOnWriteArrayList();
    }

    /* JADX INFO: renamed from: getFrameNumber-Ugla2oM, reason: not valid java name */
    public final long m572getFrameNumberUgla2oM() {
        return this.frameNumber;
    }

    public final FrameInfoOutput getFrameInfoOutput() {
        return this.frameInfoOutput;
    }

    public final List getImageOutputs() {
        return this.imageOutputs;
    }

    public final void onFrameInfoComplete() {
        Object value;
        State state;
        AtomicRef atomicRef = this.state;
        do {
            value = atomicRef.getValue();
            State state2 = (State) value;
            int i = WhenMappings.$EnumSwitchMapping$0[state2.ordinal()];
            if (i == 1) {
                state = State.FRAME_INFO_COMPLETE;
            } else if (i == 3) {
                state = State.COMPLETE;
            } else {
                throw new IllegalStateException("Unexpected frame state for " + this + "! State is " + state2 + ' ');
            }
        } while (!atomicRef.compareAndSet(value, state));
        Iterator it = this.listenerStates.iterator();
        Intrinsics.checkNotNullExpressionValue(it, "iterator(...)");
        if (it.hasNext()) {
            WindowDecorActionBar$$ExternalSyntheticThrowCCEIfNotNull0.m(it.next());
            throw null;
        }
        if (state == State.COMPLETE) {
            invokeOnFrameComplete();
        }
    }

    /* JADX INFO: renamed from: onStreamResultComplete-aKI5c8E, reason: not valid java name */
    public final void m573onStreamResultCompleteaKI5c8E(int i) {
        Object value;
        State state;
        if (this.remainingStreamCount.decrementAndGet() != 0) {
            return;
        }
        AtomicRef atomicRef = this.state;
        do {
            value = atomicRef.getValue();
            State state2 = (State) value;
            int i2 = WhenMappings.$EnumSwitchMapping$0[state2.ordinal()];
            if (i2 == 1) {
                state = State.STREAM_RESULTS_COMPLETE;
            } else if (i2 == 2) {
                state = State.COMPLETE;
            } else {
                throw new IllegalStateException("Unexpected frame state for " + this + "! State is " + state2 + ' ');
            }
        } while (!atomicRef.compareAndSet(value, state));
        Iterator it = this.listenerStates.iterator();
        Intrinsics.checkNotNullExpressionValue(it, "iterator(...)");
        if (it.hasNext()) {
            WindowDecorActionBar$$ExternalSyntheticThrowCCEIfNotNull0.m(it.next());
            throw null;
        }
        if (state == State.COMPLETE) {
            invokeOnFrameComplete();
        }
    }

    private final void invokeOnFrameComplete() {
        Iterator it = this.listenerStates.iterator();
        Intrinsics.checkNotNullExpressionValue(it, "iterator(...)");
        if (it.hasNext()) {
            WindowDecorActionBar$$ExternalSyntheticThrowCCEIfNotNull0.m(it.next());
            throw null;
        }
    }

    public String toString() {
        return "Frame-" + ((Object) FrameId.m270toStringimpl(this.frameId)) + '(' + this.frameNumber + '@' + this.frameTimestamp + ')';
    }

    public static abstract class FrameOutput {
        private final AtomicInt count = AtomicFU.atomic(1);
        private final CompletableDeferred internalResult = CompletableDeferredKt.CompletableDeferred$default(null, 1, null);

        protected abstract void release();

        protected final CompletableDeferred getInternalResult() {
            return this.internalResult;
        }

        public final void decrement() {
            if (this.count.decrementAndGet() == 0) {
                OutputResult.Companion companion = OutputResult.Companion;
                this.internalResult.complete(OutputResult.m587boximpl(OutputResult.m588constructorimpl(OutputStatus.m303boximpl(OutputStatus.Companion.m313getUNAVAILABLEU7r42EA()))));
                release();
            }
        }
    }

    public final class FrameInfoOutput extends FrameOutput implements OutputDistributor.OutputListener {
        @Override // androidx.camera.camera2.pipe.internal.FrameState.FrameOutput
        protected void release() {
        }

        public FrameInfoOutput() {
        }

        @Override // androidx.camera.camera2.pipe.internal.OutputDistributor.OutputListener
        /* JADX INFO: renamed from: onOutputComplete-3ejhThk, reason: not valid java name */
        public void mo576onOutputComplete3ejhThk(long j, long j2, long j3, long j4, Object obj) {
            getInternalResult().complete(OutputResult.m587boximpl(obj));
            FrameState.this.onFrameInfoComplete();
        }
    }

    public static final class Companion {
        public /* synthetic */ Companion(DefaultConstructorMarker defaultConstructorMarker) {
            this();
        }

        private Companion() {
        }

        /* JADX INFO: Access modifiers changed from: private */
        /* JADX INFO: renamed from: nextFrameId-OMxQvVY, reason: not valid java name */
        public final long m575nextFrameIdOMxQvVY() {
            return FrameId.m269constructorimpl(FrameState.frameIds.incrementAndGet());
        }
    }
}
