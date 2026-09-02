package androidx.camera.camera2.pipe.internal;

import androidx.camera.camera2.adapter.EvCompValue$$ExternalSyntheticBackport0;
import androidx.camera.camera2.pipe.CameraTimestamp;
import androidx.camera.camera2.pipe.CameraTimestamp$$ExternalSyntheticBackport0;
import androidx.camera.camera2.pipe.FrameNumber;
import androidx.camera.camera2.pipe.OutputStatus;
import androidx.camera.camera2.pipe.core.Log;
import androidx.camera.camera2.pipe.media.Finalizer;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import kotlin.Unit;
import kotlin.collections.CollectionsKt;
import kotlin.jvm.internal.DefaultConstructorMarker;
import kotlin.jvm.internal.Intrinsics;
import kotlin.jvm.internal.Ref$ObjectRef;
import kotlinx.atomicfu.AtomicBoolean;
import kotlinx.atomicfu.AtomicFU;

public final class OutputDistributor implements AutoCloseable {
    private final Map availableOutputs;
    private long cameraOutputSequenceNumbers;
    private boolean closed;
    private long lastFailedCameraOutputNumber;
    private long lastFailedFrameNumber;
    private final Object lock;
    private final int maximumCachedOutputs;
    private long newestCameraOutputNumber;
    private long newestFrameNumber;
    private final Finalizer outputFinalizer;
    private final OutputMatcher outputMatcher;
    private final List startedOutputs;

    public interface OutputListener {
        /* JADX INFO: renamed from: onOutputComplete-3ejhThk */
        void mo576onOutputComplete3ejhThk(long j, long j2, long j3, long j4, Object obj);
    }

    public OutputDistributor(int i, Finalizer outputFinalizer, OutputMatcher outputMatcher) {
        Intrinsics.checkNotNullParameter(outputFinalizer, "outputFinalizer");
        Intrinsics.checkNotNullParameter(outputMatcher, "outputMatcher");
        this.maximumCachedOutputs = i;
        this.outputFinalizer = outputFinalizer;
        this.outputMatcher = outputMatcher;
        this.lock = new Object();
        this.cameraOutputSequenceNumbers = 1L;
        this.newestCameraOutputNumber = Long.MIN_VALUE;
        this.newestFrameNumber = FrameNumber.m274constructorimpl(Long.MIN_VALUE);
        this.lastFailedFrameNumber = Long.MIN_VALUE;
        this.lastFailedCameraOutputNumber = Long.MIN_VALUE;
        this.startedOutputs = new ArrayList();
        this.availableOutputs = new LinkedHashMap();
    }

    public /* synthetic */ OutputDistributor(int i, Finalizer finalizer, OutputMatcher outputMatcher, int i2, DefaultConstructorMarker defaultConstructorMarker) {
        this((i2 & 1) != 0 ? 3 : i, finalizer, outputMatcher);
    }

    /* JADX WARN: Code duplicated, block: B:116:? A[RETURN, SYNTHETIC] */
    /* JADX WARN: Code duplicated, block: B:75:0x0157  */
    /* JADX WARN: Code duplicated, block: B:78:0x0163 A[LOOP:2: B:76:0x015d->B:78:0x0163, LOOP_END] */
    /* JADX WARN: Code duplicated, block: B:81:0x0179  */
    /* JADX WARN: Code duplicated, block: B:84:0x0184  */
    /* JADX WARN: Code duplicated, block: B:86:0x0187  */
    /* JADX WARN: Code duplicated, block: B:88:0x018e A[DONT_INVERT] */
    /* JADX WARN: Code duplicated, block: B:89:0x0190  */
    /* JADX WARN: Code duplicated, block: B:91:0x01a2  */
    /* JADX WARN: Code duplicated, block: B:93:0x01a6  */
    /* JADX WARN: Code duplicated, block: B:94:0x01ab  */
    /* JADX WARN: Code duplicated, block: B:96:0x01be  */
    /* JADX WARN: Code duplicated, block: B:98:0x01cb  */
    /* JADX INFO: renamed from: onOutputStarted-qGubWw0, reason: not valid java name */
    public final void m582onOutputStartedqGubWw0(long j, long j2, long j3, OutputListener outputListener) {
        Object next;
        OutputDistributor outputDistributor;
        Object next2;
        Long l;
        Object objRemove;
        List listRemoveOutputsOlderThan;
        boolean z;
        OutputResult outputResult;
        OutputResult outputResult2;
        Object objM588constructorimpl;
        long jLongValue;
        Object objM594unboximpl;
        Iterator it;
        Object next3;
        Intrinsics.checkNotNullParameter(outputListener, "outputListener");
        Ref$ObjectRef ref$ObjectRef = new Ref$ObjectRef();
        synchronized (this.lock) {
            try {
                Iterator it2 = this.startedOutputs.iterator();
                do {
                    if (!it2.hasNext()) {
                        next = null;
                        break;
                    }
                    next = it2.next();
                } while (!FrameNumber.m276equalsimpl0(((StartedOutput) next).m585getCameraFrameNumberUgla2oM(), j));
                StartedOutput startedOutput = (StartedOutput) next;
                if (startedOutput != null) {
                    if (Log.INSTANCE.getWARN_LOGGABLE()) {
                        android.util.Log.w("CXCP", "onOutputStarted was invoked multiple times with a previously started output!onOutputStarted with " + ((Object) FrameNumber.m278toStringimpl(j)) + ", " + ((Object) CameraTimestamp.m251toStringimpl(j2)) + ", " + j3 + ". Previously started output: " + startedOutput + ". Ignoring.");
                    }
                    return;
                }
                boolean z2 = this.closed;
                long j4 = this.cameraOutputSequenceNumbers;
                this.cameraOutputSequenceNumbers = j4 + 1;
                try {
                    if (!z2 && this.lastFailedFrameNumber != j && this.lastFailedCameraOutputNumber != j3) {
                        boolean z3 = j < this.newestFrameNumber;
                        if (!z3) {
                            this.newestFrameNumber = j;
                        }
                        boolean z4 = j3 < this.newestCameraOutputNumber;
                        if (!z4) {
                            this.newestCameraOutputNumber = j3;
                        }
                        boolean z5 = z3 || z4;
                        Iterator it3 = this.availableOutputs.keySet().iterator();
                        do {
                            if (!it3.hasNext()) {
                                next3 = null;
                                break;
                            }
                            next3 = it3.next();
                        } while (!this.outputMatcher.fuzzyEqual(j3, ((Number) next3).longValue()));
                        l = (Long) next3;
                        if (l != null) {
                            objRemove = this.availableOutputs.remove(l);
                            listRemoveOutputsOlderThan = removeOutputsOlderThan(z5, j4, j3);
                            outputDistributor = this;
                        } else {
                            outputDistributor = this;
                            boolean z6 = z5;
                            List list = outputDistributor.startedOutputs;
                            StartedOutput startedOutput2 = new StartedOutput(z6, j, j2, j4, j3, outputListener, null);
                            j4 = j4;
                            list.add(startedOutput2);
                            objRemove = null;
                            listRemoveOutputsOlderThan = null;
                            l = null;
                            z = false;
                        }
                        Unit unit = Unit.INSTANCE;
                        if (listRemoveOutputsOlderThan != null) {
                            it = listRemoveOutputsOlderThan.iterator();
                            while (it.hasNext()) {
                                ((StartedOutput) it.next()).m584completeWithFailuretXNfJfc(OutputStatus.Companion.m312getERROR_OUTPUT_MISSINGU7r42EA());
                            }
                        }
                        outputResult = (OutputResult) ref$ObjectRef.element;
                        if (outputResult != null) {
                            objM594unboximpl = outputResult.m594unboximpl();
                            if (!OutputResult.m590getAvailableimpl(objM594unboximpl)) {
                                objM594unboximpl = null;
                            }
                            if (objM594unboximpl != null) {
                                outputDistributor.outputFinalizer.finalize(objM594unboximpl);
                            }
                        }
                        if (z) {
                            if (z2) {
                                OutputResult.Companion companion = OutputResult.Companion;
                                objM588constructorimpl = OutputResult.m588constructorimpl(OutputStatus.m303boximpl(OutputStatus.Companion.m310getERROR_OUTPUT_ABORTEDU7r42EA()));
                            } else {
                                outputResult2 = (OutputResult) objRemove;
                                if (outputResult2 != null) {
                                    objM588constructorimpl = outputResult2.m594unboximpl();
                                } else {
                                    OutputResult.Companion companion2 = OutputResult.Companion;
                                    objM588constructorimpl = OutputResult.m588constructorimpl(OutputStatus.m303boximpl(OutputStatus.Companion.m311getERROR_OUTPUT_FAILEDU7r42EA()));
                                }
                            }
                            Object obj = objM588constructorimpl;
                            if (l != null) {
                                jLongValue = l.longValue();
                            } else {
                                jLongValue = -1;
                            }
                            outputListener.mo576onOutputComplete3ejhThk(j, j2, j4, jLongValue, obj);
                        }
                    }
                    outputDistributor = this;
                    Iterator it4 = outputDistributor.availableOutputs.keySet().iterator();
                    do {
                        if (!it4.hasNext()) {
                            next2 = null;
                            break;
                        }
                        next2 = it4.next();
                    } while (!outputDistributor.outputMatcher.fuzzyEqual(j3, ((Number) next2).longValue()));
                    l = (Long) next2;
                    ref$ObjectRef.element = l != null ? (OutputResult) outputDistributor.availableOutputs.remove(l) : null;
                    objRemove = null;
                    listRemoveOutputsOlderThan = null;
                    z = true;
                    Unit unit2 = Unit.INSTANCE;
                    if (listRemoveOutputsOlderThan != null) {
                        it = listRemoveOutputsOlderThan.iterator();
                        while (it.hasNext()) {
                            ((StartedOutput) it.next()).m584completeWithFailuretXNfJfc(OutputStatus.Companion.m312getERROR_OUTPUT_MISSINGU7r42EA());
                        }
                    }
                    outputResult = (OutputResult) ref$ObjectRef.element;
                    if (outputResult != null) {
                        objM594unboximpl = outputResult.m594unboximpl();
                        if (!OutputResult.m590getAvailableimpl(objM594unboximpl)) {
                            objM594unboximpl = null;
                        }
                        if (objM594unboximpl != null) {
                            outputDistributor.outputFinalizer.finalize(objM594unboximpl);
                        }
                    }
                    if (z) {
                        if (z2) {
                            OutputResult.Companion companion3 = OutputResult.Companion;
                            objM588constructorimpl = OutputResult.m588constructorimpl(OutputStatus.m303boximpl(OutputStatus.Companion.m310getERROR_OUTPUT_ABORTEDU7r42EA()));
                        } else {
                            outputResult2 = (OutputResult) objRemove;
                            if (outputResult2 != null) {
                                objM588constructorimpl = outputResult2.m594unboximpl();
                            } else {
                                OutputResult.Companion companion4 = OutputResult.Companion;
                                objM588constructorimpl = OutputResult.m588constructorimpl(OutputStatus.m303boximpl(OutputStatus.Companion.m311getERROR_OUTPUT_FAILEDU7r42EA()));
                            }
                        }
                        Object obj2 = objM588constructorimpl;
                        if (l != null) {
                            jLongValue = l.longValue();
                        } else {
                            jLongValue = -1;
                        }
                        outputListener.mo576onOutputComplete3ejhThk(j, j2, j4, jLongValue, obj2);
                    }
                } catch (Throwable th) {
                    th = th;
                    throw th;
                }
            } catch (Throwable th2) {
                th = th2;
            }
        }
    }

    /* JADX INFO: renamed from: onOutputResult-DvZWqE8, reason: not valid java name */
    public final void m581onOutputResultDvZWqE8(long j, Object obj) {
        Object objM587boximpl;
        List listRemoveOutputsOlderThan;
        Object next;
        synchronized (this.lock) {
            try {
                if (this.closed || this.outputMatcher.fuzzyEqual(this.lastFailedCameraOutputNumber, j)) {
                    objM587boximpl = OutputResult.m587boximpl(obj);
                } else {
                    Iterator it = this.startedOutputs.iterator();
                    do {
                        if (!it.hasNext()) {
                            next = null;
                            break;
                        }
                        next = it.next();
                    } while (!this.outputMatcher.fuzzyEqual(((StartedOutput) next).getCameraOutputNumber(), j));
                    StartedOutput startedOutput = (StartedOutput) next;
                    if (startedOutput != null) {
                        listRemoveOutputsOlderThan = removeOutputsOlderThan(startedOutput);
                        startedOutput.m583completeWithDvZWqE8(j, obj);
                        this.startedOutputs.remove(startedOutput);
                        objM587boximpl = null;
                    } else {
                        this.availableOutputs.put(Long.valueOf(j), OutputResult.m587boximpl(obj));
                        if (this.availableOutputs.size() > this.maximumCachedOutputs) {
                            objM587boximpl = this.availableOutputs.remove(Long.valueOf(((Number) CollectionsKt.first(this.availableOutputs.keySet())).longValue()));
                        } else {
                            objM587boximpl = null;
                            listRemoveOutputsOlderThan = null;
                        }
                    }
                    Unit unit = Unit.INSTANCE;
                }
                listRemoveOutputsOlderThan = null;
                Unit unit2 = Unit.INSTANCE;
            } catch (Throwable th) {
                throw th;
            }
        }
        OutputResult outputResult = (OutputResult) objM587boximpl;
        if (outputResult != null) {
            Object objM594unboximpl = outputResult.m594unboximpl();
            Object obj2 = OutputResult.m590getAvailableimpl(objM594unboximpl) ? objM594unboximpl : null;
            if (obj2 != null) {
                this.outputFinalizer.finalize(obj2);
            }
        }
        if (listRemoveOutputsOlderThan != null) {
            Iterator it2 = listRemoveOutputsOlderThan.iterator();
            while (it2.hasNext()) {
                ((StartedOutput) it2.next()).m584completeWithFailuretXNfJfc(OutputStatus.Companion.m312getERROR_OUTPUT_MISSINGU7r42EA());
            }
        }
    }

    /* JADX INFO: renamed from: onOutputFailure-Vw7M1qk, reason: not valid java name */
    public final void m580onOutputFailureVw7M1qk(long j) {
        synchronized (this.lock) {
            try {
                if (this.closed) {
                    return;
                }
                this.lastFailedFrameNumber = j;
                Iterator it = this.startedOutputs.iterator();
                StartedOutput startedOutput = null;
                boolean z = false;
                Object obj = null;
                while (true) {
                    if (!it.hasNext()) {
                        if (z) {
                            break;
                        }
                    } else {
                        Object next = it.next();
                        if (FrameNumber.m276equalsimpl0(((StartedOutput) next).m585getCameraFrameNumberUgla2oM(), j)) {
                            if (!z) {
                                z = true;
                                obj = next;
                            }
                        }
                    }
                    obj = null;
                    break;
                }
                StartedOutput startedOutput2 = (StartedOutput) obj;
                if (startedOutput2 != null) {
                    this.lastFailedCameraOutputNumber = startedOutput2.getCameraOutputNumber();
                    this.startedOutputs.remove(startedOutput2);
                    Unit unit = Unit.INSTANCE;
                    startedOutput = startedOutput2;
                }
                if (startedOutput != null) {
                    startedOutput.m584completeWithFailuretXNfJfc(OutputStatus.Companion.m311getERROR_OUTPUT_FAILEDU7r42EA());
                }
            } catch (Throwable th) {
                throw th;
            }
        }
    }

    private final List removeOutputsOlderThan(StartedOutput startedOutput) {
        return removeOutputsOlderThan(startedOutput.isOutOfOrder(), startedOutput.getCameraOutputSequence(), startedOutput.getCameraOutputNumber());
    }

    private final List removeOutputsOlderThan(boolean z, long j, long j2) {
        List list = this.startedOutputs;
        ArrayList arrayList = new ArrayList();
        for (Object obj : list) {
            StartedOutput startedOutput = (StartedOutput) obj;
            if (startedOutput.isOutOfOrder() == z && startedOutput.getCameraOutputSequence() < j && startedOutput.getCameraOutputNumber() < j2) {
                arrayList.add(obj);
            }
        }
        this.startedOutputs.removeAll(arrayList);
        return arrayList;
    }

    @Override // java.lang.AutoCloseable
    public void close() {
        synchronized (this.lock) {
            if (this.closed) {
                return;
            }
            this.closed = true;
            List mutableList = CollectionsKt.toMutableList(this.availableOutputs.values());
            this.availableOutputs.clear();
            List mutableList2 = CollectionsKt.toMutableList((Collection) this.startedOutputs);
            this.startedOutputs.clear();
            Unit unit = Unit.INSTANCE;
            Iterator it = mutableList.iterator();
            while (it.hasNext()) {
                Object objM594unboximpl = ((OutputResult) it.next()).m594unboximpl();
                Finalizer finalizer = this.outputFinalizer;
                if (!OutputResult.m590getAvailableimpl(objM594unboximpl)) {
                    objM594unboximpl = null;
                }
                finalizer.finalize(objM594unboximpl);
            }
            Iterator it2 = mutableList2.iterator();
            while (it2.hasNext()) {
                ((StartedOutput) it2.next()).m584completeWithFailuretXNfJfc(OutputStatus.Companion.m310getERROR_OUTPUT_ABORTEDU7r42EA());
            }
        }
    }

    private static final class StartedOutput {
        private final long cameraFrameNumber;
        private final long cameraOutputNumber;
        private final long cameraOutputSequence;
        private final long cameraTimestamp;
        private final AtomicBoolean complete;
        private final boolean isOutOfOrder;
        private final OutputListener outputListener;

        public /* synthetic */ StartedOutput(boolean z, long j, long j2, long j3, long j4, OutputListener outputListener, DefaultConstructorMarker defaultConstructorMarker) {
            this(z, j, j2, j3, j4, outputListener);
        }

        public boolean equals(Object obj) {
            if (this == obj) {
                return true;
            }
            if (!(obj instanceof StartedOutput)) {
                return false;
            }
            StartedOutput startedOutput = (StartedOutput) obj;
            return this.isOutOfOrder == startedOutput.isOutOfOrder && FrameNumber.m276equalsimpl0(this.cameraFrameNumber, startedOutput.cameraFrameNumber) && CameraTimestamp.m249equalsimpl0(this.cameraTimestamp, startedOutput.cameraTimestamp) && this.cameraOutputSequence == startedOutput.cameraOutputSequence && this.cameraOutputNumber == startedOutput.cameraOutputNumber && Intrinsics.areEqual(this.outputListener, startedOutput.outputListener);
        }

        public int hashCode() {
            return (((((((((EvCompValue$$ExternalSyntheticBackport0.m(this.isOutOfOrder) * 31) + FrameNumber.m277hashCodeimpl(this.cameraFrameNumber)) * 31) + CameraTimestamp.m250hashCodeimpl(this.cameraTimestamp)) * 31) + CameraTimestamp$$ExternalSyntheticBackport0.m(this.cameraOutputSequence)) * 31) + CameraTimestamp$$ExternalSyntheticBackport0.m(this.cameraOutputNumber)) * 31) + this.outputListener.hashCode();
        }

        public String toString() {
            return "StartedOutput(isOutOfOrder=" + this.isOutOfOrder + ", cameraFrameNumber=" + ((Object) FrameNumber.m278toStringimpl(this.cameraFrameNumber)) + ", cameraTimestamp=" + ((Object) CameraTimestamp.m251toStringimpl(this.cameraTimestamp)) + ", cameraOutputSequence=" + this.cameraOutputSequence + ", cameraOutputNumber=" + this.cameraOutputNumber + ", outputListener=" + this.outputListener + ')';
        }

        private StartedOutput(boolean z, long j, long j2, long j3, long j4, OutputListener outputListener) {
            Intrinsics.checkNotNullParameter(outputListener, "outputListener");
            this.isOutOfOrder = z;
            this.cameraFrameNumber = j;
            this.cameraTimestamp = j2;
            this.cameraOutputSequence = j3;
            this.cameraOutputNumber = j4;
            this.outputListener = outputListener;
            this.complete = AtomicFU.atomic(false);
        }

        public final boolean isOutOfOrder() {
            return this.isOutOfOrder;
        }

        /* JADX INFO: renamed from: getCameraFrameNumber-Ugla2oM, reason: not valid java name */
        public final long m585getCameraFrameNumberUgla2oM() {
            return this.cameraFrameNumber;
        }

        public final long getCameraOutputSequence() {
            return this.cameraOutputSequence;
        }

        public final long getCameraOutputNumber() {
            return this.cameraOutputNumber;
        }

        /* JADX INFO: renamed from: completeWithFailure-tXNfJfc, reason: not valid java name */
        public final void m584completeWithFailuretXNfJfc(int i) {
            OutputResult.Companion companion = OutputResult.Companion;
            m583completeWithDvZWqE8(-1L, OutputResult.m588constructorimpl(OutputStatus.m303boximpl(i)));
        }

        /* JADX INFO: renamed from: completeWith-DvZWqE8, reason: not valid java name */
        public final void m583completeWithDvZWqE8(long j, Object obj) {
            if (!this.complete.compareAndSet(false, true)) {
                throw new IllegalStateException(("Output " + this.cameraOutputSequence + " at " + ((Object) FrameNumber.m278toStringimpl(this.cameraFrameNumber)) + " for " + j + " was completed multiple times!").toString());
            }
            this.outputListener.mo576onOutputComplete3ejhThk(this.cameraFrameNumber, this.cameraTimestamp, this.cameraOutputSequence, j, obj);
        }
    }
}
