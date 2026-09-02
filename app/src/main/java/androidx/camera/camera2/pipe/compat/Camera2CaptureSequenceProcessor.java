package androidx.camera.camera2.pipe.compat;

import android.hardware.camera2.CaptureRequest;
import android.hardware.camera2.TotalCaptureResult;
import android.os.Trace;
import android.util.ArrayMap;
import android.view.Surface;
import androidx.camera.camera2.config.UseCaseGraphContext$$ExternalSyntheticAutoCloseableDispatcher0;
import androidx.camera.camera2.pipe.CameraStream;
import androidx.camera.camera2.pipe.CaptureSequence;
import androidx.camera.camera2.pipe.CaptureSequenceProcessor;
import androidx.camera.camera2.pipe.InputStream;
import androidx.camera.camera2.pipe.OutputId;
import androidx.camera.camera2.pipe.OutputStream;
import androidx.camera.camera2.pipe.Request;
import androidx.camera.camera2.pipe.RequestTemplate;
import androidx.camera.camera2.pipe.RequestsKt;
import androidx.camera.camera2.pipe.StreamFormat;
import androidx.camera.camera2.pipe.StreamGraph;
import androidx.camera.camera2.pipe.StreamId;
import androidx.camera.camera2.pipe.StrictMode;
import androidx.camera.camera2.pipe.core.Debug;
import androidx.camera.camera2.pipe.core.Log;
import androidx.camera.camera2.pipe.core.Threads;
import androidx.camera.camera2.pipe.media.AndroidImageWriter;
import androidx.camera.camera2.pipe.media.ImageWrapper;
import androidx.camera.camera2.pipe.media.ImageWriterWrapper;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import kotlin.ResultKt;
import kotlin.Unit;
import kotlin.collections.CollectionsKt;
import kotlin.coroutines.Continuation;
import kotlin.coroutines.intrinsics.IntrinsicsKt;
import kotlin.coroutines.jvm.internal.SuspendLambda;
import kotlin.jvm.functions.Function1;
import kotlin.jvm.internal.DefaultConstructorMarker;
import kotlin.jvm.internal.Intrinsics;
import kotlin.jvm.internal.Reflection;

public final class Camera2CaptureSequenceProcessor implements CaptureSequenceProcessor {
    public static final Companion Companion = new Companion(null);
    private final boolean awaitRepeatingRequestOnDisconnect;
    private final int debugId;
    private boolean disconnected;
    private final ImageWriterWrapper imageWriter;
    private Camera2CaptureSequence lastSingleRepeatingRequestSequence;
    private final Object lock;
    private final Map outputToSurfaceMap;
    private final CameraCaptureSessionWrapper session;
    private final StreamGraph streamGraph;
    private final Map streamToSurfaceMap;
    private final StrictMode strictMode;
    private final int template;
    private final Threads threads;

    public /* synthetic */ Camera2CaptureSequenceProcessor(CameraCaptureSessionWrapper cameraCaptureSessionWrapper, Threads threads, int i, Map map, Map map2, StreamGraph streamGraph, StrictMode strictMode, boolean z, DefaultConstructorMarker defaultConstructorMarker) {
        this(cameraCaptureSessionWrapper, threads, i, map, map2, streamGraph, strictMode, z);
    }

    private Camera2CaptureSequenceProcessor(CameraCaptureSessionWrapper session, Threads threads, int i, Map streamToSurfaceMap, Map outputToSurfaceMap, StreamGraph streamGraph, StrictMode strictMode, boolean z) {
        Intrinsics.checkNotNullParameter(session, "session");
        Intrinsics.checkNotNullParameter(threads, "threads");
        Intrinsics.checkNotNullParameter(streamToSurfaceMap, "streamToSurfaceMap");
        Intrinsics.checkNotNullParameter(outputToSurfaceMap, "outputToSurfaceMap");
        Intrinsics.checkNotNullParameter(streamGraph, "streamGraph");
        Intrinsics.checkNotNullParameter(strictMode, "strictMode");
        this.session = session;
        this.threads = threads;
        this.template = i;
        this.streamToSurfaceMap = streamToSurfaceMap;
        this.outputToSurfaceMap = outputToSurfaceMap;
        this.streamGraph = streamGraph;
        this.strictMode = strictMode;
        this.awaitRepeatingRequestOnDisconnect = z;
        this.debugId = Camera2CaptureSequenceProcessorKt.getCaptureSequenceProcessorDebugIds().incrementAndGet();
        this.lock = new Object();
        ImageWriterWrapper imageWriterWrapperM595createU86x6Zg = null;
        if (!streamGraph.getInputs().isEmpty()) {
            InputStream inputStream = (InputStream) CollectionsKt.first(streamGraph.getInputs());
            Surface inputSurface = session.getInputSurface();
            if (inputSurface == null) {
                throw new IllegalStateException("inputSurface is required to create instance of imageWriter.");
            }
            try {
                imageWriterWrapperM595createU86x6Zg = AndroidImageWriter.Companion.m595createU86x6Zg(inputSurface, inputStream.mo282getIdm1bwn9M(), inputStream.getMaxImages(), StreamFormat.m403boximpl(inputStream.mo281getFormat8FPWQzE()), threads.getCamera2Handler());
            } catch (RuntimeException e) {
                if (Log.INSTANCE.getERROR_LOGGABLE()) {
                    android.util.Log.e("CXCP", "Failed to create ImageWriter for session " + this.session + "! Reprocessing will not be supported!", e);
                }
            }
            if (imageWriterWrapperM595createU86x6Zg != null && Log.INSTANCE.getDEBUG_LOGGABLE()) {
                android.util.Log.d("CXCP", "Created ImageWriter " + imageWriterWrapperM595createU86x6Zg + " for session " + this.session);
            }
        }
        this.imageWriter = imageWriterWrapperM595createU86x6Zg;
    }

    /* JADX WARN: Code duplicated, block: B:103:0x027b  */
    @Override // androidx.camera.camera2.pipe.CaptureSequenceProcessor
    public Camera2CaptureSequence build(boolean z, List requests, Map map, Map map2, Map map3, CaptureSequence.CaptureSequenceListener sequenceListener, List listeners) {
        boolean z2;
        ArrayMap arrayMap;
        Iterator it;
        ArrayList arrayList;
        boolean z3;
        boolean z4;
        ArrayMap arrayMap2;
        ArrayList arrayList2;
        ArrayList arrayList3;
        this = this;
        Map defaultParameters = map;
        Map graphParameters = map2;
        Map requiredParameters = map3;
        Intrinsics.checkNotNullParameter(requests, "requests");
        Intrinsics.checkNotNullParameter(defaultParameters, "defaultParameters");
        Intrinsics.checkNotNullParameter(graphParameters, "graphParameters");
        Intrinsics.checkNotNullParameter(requiredParameters, "requiredParameters");
        Intrinsics.checkNotNullParameter(sequenceListener, "sequenceListener");
        Intrinsics.checkNotNullParameter(listeners, "listeners");
        ArrayList arrayList4 = new ArrayList(requests.size());
        ArrayList arrayList5 = new ArrayList(requests.size());
        ArrayMap arrayMap3 = new ArrayMap();
        ArrayMap arrayMap4 = new ArrayMap();
        ArrayMap arrayMap5 = new ArrayMap();
        if (!this.validateRequestList(requests, this.session) || !this.buildSurfaceMaps(requests, arrayMap3, arrayMap4, arrayMap5)) {
            return null;
        }
        Iterator it2 = requests.iterator();
        while (it2.hasNext()) {
            Request request = (Request) it2.next();
            if (Log.INSTANCE.getDEBUG_LOGGABLE()) {
                android.util.Log.d("CXCP", "Building CaptureRequest for " + request);
            }
            RequestTemplate requestTemplateM368getTemplateejQnlcg = request.m368getTemplateejQnlcg();
            int iM391unboximpl = requestTemplateM368getTemplateejQnlcg != null ? requestTemplateM368getTemplateejQnlcg.m391unboximpl() : this.template;
            CaptureRequest.Builder builderM458buildCaptureRequestBuilder0UCm73U = this.m458buildCaptureRequestBuilder0UCm73U(request, iM391unboximpl);
            if (builderM458buildCaptureRequestBuilder0UCm73U == null) {
                return null;
            }
            CameraPipeKeys cameraPipeKeys = CameraPipeKeys.INSTANCE;
            it2 = it2;
            Object obj = requiredParameters.get(cameraPipeKeys.getCamera2CaptureRequestTag());
            if (obj == null) {
                obj = defaultParameters.get(cameraPipeKeys.getCamera2CaptureRequestTag());
            }
            builderM458buildCaptureRequestBuilder0UCm73U.setTag(obj);
            int size = request.getStreams().size();
            int i = 0;
            boolean z5 = false;
            while (true) {
                z2 = true;
                if (i >= size) {
                    break;
                }
                int i2 = size;
                Surface surface = (Surface) arrayMap5.get(request.getStreams().get(i));
                if (surface != null) {
                    builderM458buildCaptureRequestBuilder0UCm73U.addTarget(surface);
                    z5 = true;
                }
                i++;
                size = i2;
            }
            if (!z5) {
                throw new IllegalStateException("Check failed.");
            }
            if (request.getInputRequest() == null) {
                RequestsKt.writeParameters(builderM458buildCaptureRequestBuilder0UCm73U, defaultParameters);
                RequestsKt.writeParameters(builderM458buildCaptureRequestBuilder0UCm73U, graphParameters);
                RequestsKt.writeParameters(builderM458buildCaptureRequestBuilder0UCm73U, request.getParameters());
                RequestsKt.writeParameters(builderM458buildCaptureRequestBuilder0UCm73U, requiredParameters);
            } else {
                if (this.imageWriter == null) {
                    if (Log.INSTANCE.getERROR_LOGGABLE()) {
                        android.util.Log.e("CXCP", "Failed to queue request to ImageWriter - No ImageWriter available!");
                    }
                    return null;
                }
                ImageWrapper image = request.getInputRequest().getImage();
                synchronized (this.lock) {
                    if (this.disconnected) {
                        if (Log.INSTANCE.getWARN_LOGGABLE()) {
                            android.util.Log.w("CXCP", this + " disconnected. " + image + " can't be queued to " + this.imageWriter);
                        }
                        return null;
                    }
                    Unit unit = Unit.INSTANCE;
                    Log log = Log.INSTANCE;
                    if (log.getDEBUG_LOGGABLE()) {
                        android.util.Log.d("CXCP", "Queuing image " + image + " for reprocessing to ImageWriter " + this.imageWriter);
                    }
                    if (!this.imageWriter.queueInputImage(image)) {
                        if (log.getDEBUG_LOGGABLE()) {
                            android.util.Log.d("CXCP", "Failed to queue image " + image + " for reprocessing to ImageWriter " + this.imageWriter);
                        }
                        return null;
                    }
                    RequestsKt.writeParameters(builderM458buildCaptureRequestBuilder0UCm73U, request.getParameters());
                }
            }
            ArrayMap arrayMap6 = arrayMap4;
            int i3 = iM391unboximpl;
            long jNextRequestNumber = Camera2CaptureSequenceProcessorKt.nextRequestNumber();
            CaptureRequest captureRequestBuild = builderM458buildCaptureRequestBuilder0UCm73U.build();
            Intrinsics.checkNotNullExpressionValue(captureRequestBuild, "build(...)");
            CameraCaptureSessionWrapper cameraCaptureSessionWrapper = this.session;
            if (cameraCaptureSessionWrapper instanceof CameraConstrainedHighSpeedCaptureSessionWrapper) {
                List listCreateHighSpeedRequestList = ((CameraConstrainedHighSpeedCaptureSessionWrapper) cameraCaptureSessionWrapper).createHighSpeedRequestList(captureRequestBuild);
                if (listCreateHighSpeedRequestList == null) {
                    return null;
                }
                List streams = request.getStreams();
                if (!(streams instanceof Collection) || !streams.isEmpty()) {
                    Iterator it3 = streams.iterator();
                    while (true) {
                        if (!it3.hasNext()) {
                            arrayMap = arrayMap6;
                            arrayList = arrayList5;
                            z2 = false;
                            break;
                        }
                        ((StreamId) it3.next()).m423unboximpl();
                        List outputs = this.streamGraph.getOutputs();
                        arrayMap = arrayMap6;
                        if (!(outputs instanceof Collection) || !outputs.isEmpty()) {
                            Iterator it4 = outputs.iterator();
                            while (true) {
                                if (!it4.hasNext()) {
                                    it = it3;
                                    arrayList = arrayList5;
                                    z4 = false;
                                    break;
                                }
                                OutputStream outputStream = (OutputStream) it4.next();
                                OutputStream.StreamUseCase streamUseCaseMo319getStreamUseCase8x2ez34 = outputStream.mo319getStreamUseCase8x2ez34();
                                it = it3;
                                arrayList = arrayList5;
                                if (streamUseCaseMo319getStreamUseCase8x2ez34 == null ? false : OutputStream.StreamUseCase.m350equalsimpl0(streamUseCaseMo319getStreamUseCase8x2ez34.m353unboximpl(), OutputStream.StreamUseCase.Companion.m356getVIDEO_RECORDvrKr8v8())) {
                                    z3 = true;
                                } else {
                                    OutputStream.StreamUseHint streamUseHintMo320getStreamUseHintHIPxoCc = outputStream.mo320getStreamUseHintHIPxoCc();
                                    if (streamUseHintMo320getStreamUseHintHIPxoCc == null ? false : OutputStream.StreamUseHint.m360equalsimpl0(streamUseHintMo320getStreamUseHintHIPxoCc.m363unboximpl(), OutputStream.StreamUseHint.Companion.m365getVIDEO_RECORD4VYZOf8())) {
                                        z3 = true;
                                    } else {
                                        z3 = false;
                                    }
                                }
                                if (z3) {
                                    z4 = true;
                                    break;
                                }
                                arrayList5 = arrayList;
                                it3 = it;
                            }
                        } else {
                            it = it3;
                            arrayList = arrayList5;
                            z4 = false;
                            break;
                        }
                        if (z4) {
                            break;
                        }
                        arrayMap6 = arrayMap;
                        arrayList5 = arrayList;
                        it3 = it;
                    }
                } else {
                    arrayMap = arrayMap6;
                    arrayList = arrayList5;
                    z2 = false;
                    break;
                }
                if (z2) {
                    arrayMap2 = arrayMap5;
                    arrayList2 = arrayList4;
                    arrayList3 = arrayList;
                    int i4 = 0;
                    int size2 = listCreateHighSpeedRequestList.size();
                    while (i4 < size2) {
                        int i5 = size2;
                        ArrayList arrayList6 = arrayList2;
                        Camera2RequestMetadata camera2RequestMetadata = new Camera2RequestMetadata(this.session, (CaptureRequest) listCreateHighSpeedRequestList.get(i4), map, map2, map3, arrayMap2, i3, z, request, jNextRequestNumber, null);
                        arrayList3.add(listCreateHighSpeedRequestList.get(i4));
                        arrayList6.add(camera2RequestMetadata);
                        i4++;
                        arrayList2 = arrayList6;
                        size2 = i5;
                    }
                    defaultParameters = map;
                    graphParameters = map2;
                    requiredParameters = map3;
                } else {
                    graphParameters = map2;
                    requiredParameters = map3;
                    arrayMap2 = arrayMap5;
                    arrayList3 = arrayList;
                    defaultParameters = map;
                    Camera2RequestMetadata camera2RequestMetadata2 = new Camera2RequestMetadata(this.session, (CaptureRequest) listCreateHighSpeedRequestList.get(0), defaultParameters, graphParameters, requiredParameters, arrayMap2, i3, z, request, jNextRequestNumber, null);
                    arrayList3.add(listCreateHighSpeedRequestList.get(0));
                    arrayList2 = arrayList4;
                    arrayList2.add(camera2RequestMetadata2);
                }
                arrayList5 = arrayList3;
                arrayList4 = arrayList2;
                arrayMap5 = arrayMap2;
                arrayMap3 = arrayMap3;
                arrayMap4 = arrayMap;
            } else {
                ArrayList arrayList7 = arrayList5;
                ArrayMap arrayMap7 = arrayMap3;
                ArrayMap arrayMap8 = arrayMap5;
                defaultParameters = map;
                graphParameters = map2;
                requiredParameters = map3;
                ArrayList arrayList8 = arrayList4;
                Camera2RequestMetadata camera2RequestMetadata3 = new Camera2RequestMetadata(cameraCaptureSessionWrapper, captureRequestBuild, defaultParameters, graphParameters, requiredParameters, arrayMap8, i3, z, request, jNextRequestNumber, null);
                arrayList7.add(captureRequestBuild);
                arrayList8.add(camera2RequestMetadata3);
                arrayList4 = arrayList8;
                arrayList5 = arrayList7;
                arrayMap5 = arrayMap8;
                arrayMap3 = arrayMap7;
                arrayMap4 = arrayMap6;
                it2 = it2;
            }
        }
        return new Camera2CaptureSequence(this.session.getDevice().mo428getCameraIdDz_R5H8(), z, arrayList5, arrayList4, listeners, sequenceListener, arrayMap3, arrayMap4, this.streamGraph, this.strictMode, null);
    }

    @Override // androidx.camera.camera2.pipe.CaptureSequenceProcessor
    public Integer submit(Camera2CaptureSequence captureSequence) {
        Integer numCaptureBurst;
        Intrinsics.checkNotNullParameter(captureSequence, "captureSequence");
        synchronized (this.lock) {
            if (this.disconnected) {
                if (Log.INSTANCE.getWARN_LOGGABLE()) {
                    android.util.Log.w("CXCP", this + " disconnected. " + captureSequence + " won't be submitted");
                }
                return null;
            }
            if (captureSequence.getCaptureRequestList().size() == 1 && !(this.session instanceof CameraConstrainedHighSpeedCaptureSessionWrapper)) {
                if (captureSequence.getRepeating()) {
                    if (this.awaitRepeatingRequestOnDisconnect) {
                        this.lastSingleRepeatingRequestSequence = captureSequence;
                    }
                    numCaptureBurst = this.session.setRepeatingRequest((CaptureRequest) captureSequence.getCaptureRequestList().get(0), captureSequence);
                } else {
                    numCaptureBurst = this.session.capture((CaptureRequest) captureSequence.getCaptureRequestList().get(0), captureSequence);
                }
            } else if (captureSequence.getRepeating()) {
                numCaptureBurst = this.session.setRepeatingBurst(captureSequence.getCaptureRequestList(), captureSequence);
            } else {
                numCaptureBurst = this.session.captureBurst(captureSequence.getCaptureRequestList(), captureSequence);
            }
            return numCaptureBurst;
        }
    }

    @Override // androidx.camera.camera2.pipe.CaptureSequenceProcessor
    public void abortCaptures() {
        synchronized (this.lock) {
            try {
                if (Log.INSTANCE.getDEBUG_LOGGABLE()) {
                    android.util.Log.d("CXCP", this + "#abortCaptures");
                }
                this.session.abortCaptures();
                Unit unit = Unit.INSTANCE;
            } catch (Throwable th) {
                throw th;
            }
        }
    }

    @Override // androidx.camera.camera2.pipe.CaptureSequenceProcessor
    public void stopRepeating() {
        synchronized (this.lock) {
            try {
                if (Log.INSTANCE.getDEBUG_LOGGABLE()) {
                    android.util.Log.d("CXCP", this + "#stopRepeating");
                }
                this.session.stopRepeating();
                Unit unit = Unit.INSTANCE;
            } catch (Throwable th) {
                throw th;
            }
        }
    }

    @Override // androidx.camera.camera2.pipe.CaptureSequenceProcessor
    public Object shutdown(Continuation continuation) {
        disconnect$camera_camera2_pipe();
        return Unit.INSTANCE;
    }

    public final void disconnect$camera_camera2_pipe() {
        Camera2CaptureSequence camera2CaptureSequence;
        Debug debug = Debug.INSTANCE;
        try {
            Trace.beginSection(this + "#disconnect");
            synchronized (this.lock) {
                try {
                    if (this.disconnected) {
                        camera2CaptureSequence = null;
                    } else {
                        this.disconnected = true;
                        ImageWriterWrapper imageWriterWrapper = this.imageWriter;
                        if (imageWriterWrapper != null) {
                            UseCaseGraphContext$$ExternalSyntheticAutoCloseableDispatcher0.m(imageWriterWrapper);
                        }
                        Surface inputSurface = this.session.getInputSurface();
                        if (inputSurface != null) {
                            inputSurface.release();
                        }
                        camera2CaptureSequence = this.lastSingleRepeatingRequestSequence;
                    }
                } catch (Throwable th) {
                    throw th;
                }
            }
            if (this.awaitRepeatingRequestOnDisconnect && camera2CaptureSequence != null) {
                awaitRepeatingRequestStarted(camera2CaptureSequence);
            }
            Unit unit = Unit.INSTANCE;
            Trace.endSection();
        } catch (Throwable th2) {
            Trace.endSection();
            throw th2;
        }
    }

    public String toString() {
        return "Camera2CaptureSequenceProcessor-" + this.debugId;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public final void awaitRepeatingRequestStarted(Camera2CaptureSequence camera2CaptureSequence) {
        Log log = Log.INSTANCE;
        if (log.getDEBUG_LOGGABLE()) {
            android.util.Log.d("CXCP", "Waiting for the last repeating request sequence: " + camera2CaptureSequence);
        }
        if (((Unit) this.threads.runBlockingCheckedOrNull(2000L, new AnonymousClass2(camera2CaptureSequence, null))) == null && log.getERROR_LOGGABLE()) {
            android.util.Log.e("CXCP", this + "#close: awaitStarted on last repeating request timed out, lastSingleRepeatingRequestSequence = " + camera2CaptureSequence);
        }
    }

    /* JADX INFO: renamed from: androidx.camera.camera2.pipe.compat.Camera2CaptureSequenceProcessor$awaitRepeatingRequestStarted$2, reason: invalid class name */
    static final class AnonymousClass2 extends SuspendLambda implements Function1 {
        final /* synthetic */ Camera2CaptureSequence $captureSequence;
        int label;

        /* JADX WARN: 'super' call moved to the top of the method (can break code semantics) */
        AnonymousClass2(Camera2CaptureSequence camera2CaptureSequence, Continuation continuation) {
            super(1, continuation);
            this.$captureSequence = camera2CaptureSequence;
        }

        @Override // kotlin.coroutines.jvm.internal.BaseContinuationImpl
        public final Continuation create(Continuation continuation) {
            return new AnonymousClass2(this.$captureSequence, continuation);
        }

        @Override // kotlin.jvm.functions.Function1
        public final Object invoke(Continuation continuation) {
            return ((AnonymousClass2) create(continuation)).invokeSuspend(Unit.INSTANCE);
        }

        @Override // kotlin.coroutines.jvm.internal.BaseContinuationImpl
        public final Object invokeSuspend(Object obj) {
            Object coroutine_suspended = IntrinsicsKt.getCOROUTINE_SUSPENDED();
            int i = this.label;
            if (i == 0) {
                ResultKt.throwOnFailure(obj);
                Camera2CaptureSequence camera2CaptureSequence = this.$captureSequence;
                this.label = 1;
                if (camera2CaptureSequence.awaitStarted$camera_camera2_pipe(this) == coroutine_suspended) {
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

    /* JADX WARN: Code duplicated, block: B:39:0x00aa  */
    /* JADX WARN: Code duplicated, block: B:79:0x016f  */
    private final boolean validateRequestList(List list, CameraCaptureSessionWrapper cameraCaptureSessionWrapper) {
        boolean z;
        boolean z2;
        boolean z3;
        boolean z4;
        boolean z5;
        boolean z6;
        boolean z7;
        if (list.isEmpty()) {
            throw new IllegalStateException("build(...) should never be called with an empty request list!");
        }
        if (cameraCaptureSessionWrapper instanceof CameraConstrainedHighSpeedCaptureSessionWrapper) {
            Iterator it = list.iterator();
            Boolean bool = null;
            Boolean bool2 = null;
            while (it.hasNext()) {
                Request request = (Request) it.next();
                List streams = request.getStreams();
                if (!(streams instanceof Collection) || !streams.isEmpty()) {
                    Iterator it2 = streams.iterator();
                    while (true) {
                        if (!it2.hasNext()) {
                            z3 = false;
                            break;
                        }
                        ((StreamId) it2.next()).m423unboximpl();
                        List outputs = this.streamGraph.getOutputs();
                        if (!(outputs instanceof Collection) || !outputs.isEmpty()) {
                            Iterator it3 = outputs.iterator();
                            while (true) {
                                if (!it3.hasNext()) {
                                    z2 = false;
                                    break;
                                }
                                OutputStream outputStream = (OutputStream) it3.next();
                                OutputStream.StreamUseCase streamUseCaseMo319getStreamUseCase8x2ez34 = outputStream.mo319getStreamUseCase8x2ez34();
                                if (streamUseCaseMo319getStreamUseCase8x2ez34 == null ? false : OutputStream.StreamUseCase.m350equalsimpl0(streamUseCaseMo319getStreamUseCase8x2ez34.m353unboximpl(), OutputStream.StreamUseCase.Companion.m355getPREVIEWvrKr8v8())) {
                                    z = true;
                                } else {
                                    OutputStream.StreamUseHint streamUseHintMo320getStreamUseHintHIPxoCc = outputStream.mo320getStreamUseHintHIPxoCc();
                                    if ((streamUseHintMo320getStreamUseHintHIPxoCc == null ? false : OutputStream.StreamUseHint.m360equalsimpl0(streamUseHintMo320getStreamUseHintHIPxoCc.m363unboximpl(), OutputStream.StreamUseHint.Companion.m364getDEFAULT4VYZOf8())) || outputStream.mo320getStreamUseHintHIPxoCc() == null) {
                                        z = true;
                                    } else {
                                        z = false;
                                    }
                                }
                                if (z) {
                                    z2 = true;
                                    break;
                                }
                            }
                        } else {
                            z2 = false;
                            break;
                            break;
                        }
                        if (z2) {
                            z3 = true;
                            break;
                        }
                    }
                } else {
                    z3 = false;
                    break;
                }
                Boolean boolValueOf = Boolean.valueOf(z3);
                if (bool != null && !Intrinsics.areEqual(bool, boolValueOf) && Log.INSTANCE.getERROR_LOGGABLE()) {
                    android.util.Log.e("CXCP", "The previous high speed request and the current high speed request must both have a preview stream use case or hint. Previous request contains preview stream use case or hint: " + bool.booleanValue() + ". Current request contains preview stream use case or hint: " + z3 + '.');
                }
                List streams2 = request.getStreams();
                if (!(streams2 instanceof Collection) || !streams2.isEmpty()) {
                    Iterator it4 = streams2.iterator();
                    while (true) {
                        if (!it4.hasNext()) {
                            z6 = false;
                            break;
                        }
                        ((StreamId) it4.next()).m423unboximpl();
                        List outputs2 = this.streamGraph.getOutputs();
                        if (!(outputs2 instanceof Collection) || !outputs2.isEmpty()) {
                            Iterator it5 = outputs2.iterator();
                            while (true) {
                                if (!it5.hasNext()) {
                                    z5 = false;
                                    break;
                                }
                                OutputStream outputStream2 = (OutputStream) it5.next();
                                OutputStream.StreamUseCase streamUseCaseMo319getStreamUseCase8x2ez35 = outputStream2.mo319getStreamUseCase8x2ez34();
                                if (streamUseCaseMo319getStreamUseCase8x2ez35 == null ? false : OutputStream.StreamUseCase.m350equalsimpl0(streamUseCaseMo319getStreamUseCase8x2ez35.m353unboximpl(), OutputStream.StreamUseCase.Companion.m356getVIDEO_RECORDvrKr8v8())) {
                                    z4 = true;
                                } else {
                                    OutputStream.StreamUseHint streamUseHintMo320getStreamUseHintHIPxoCc2 = outputStream2.mo320getStreamUseHintHIPxoCc();
                                    if (streamUseHintMo320getStreamUseHintHIPxoCc2 == null ? false : OutputStream.StreamUseHint.m360equalsimpl0(streamUseHintMo320getStreamUseHintHIPxoCc2.m363unboximpl(), OutputStream.StreamUseHint.Companion.m365getVIDEO_RECORD4VYZOf8())) {
                                        z4 = true;
                                    } else {
                                        z4 = false;
                                    }
                                }
                                if (z4) {
                                    z5 = true;
                                    break;
                                }
                            }
                        } else {
                            z5 = false;
                            break;
                            break;
                        }
                        if (z5) {
                            z6 = true;
                            break;
                        }
                    }
                } else {
                    z6 = false;
                    break;
                }
                Boolean boolValueOf2 = Boolean.valueOf(z6);
                if (bool2 != null && !Intrinsics.areEqual(bool2, boolValueOf2) && Log.INSTANCE.getERROR_LOGGABLE()) {
                    android.util.Log.e("CXCP", "The previous high speed request and the current high speed request do not have the same video stream use case. Previous request contains video stream use case: " + bool2.booleanValue() + ". Current request contains video stream use case: " + z6 + '.');
                }
                List outputs3 = this.streamGraph.getOutputs();
                if (!(outputs3 instanceof Collection) || !outputs3.isEmpty()) {
                    Iterator it6 = outputs3.iterator();
                    while (true) {
                        if (!it6.hasNext()) {
                            z7 = true;
                            break;
                        }
                        if (!((OutputStream) it6.next()).isValidForHighSpeedOperatingMode()) {
                            z7 = false;
                            break;
                        }
                    }
                } else {
                    z7 = true;
                    break;
                }
                if (!z7) {
                    if (Log.INSTANCE.getERROR_LOGGABLE()) {
                        android.util.Log.e("CXCP", "HIGH_SPEED CameraGraph must only contain Preview and/or Video streams. Configured outputs are " + this.streamGraph.getOutputs());
                    }
                    return false;
                }
                bool2 = boolValueOf2;
                bool = boolValueOf;
            }
        }
        return true;
    }

    private final boolean buildSurfaceMaps(List list, Map map, Map map2, Map map3) {
        if (list.isEmpty()) {
            throw new IllegalStateException("build(...) should never be called with an empty request list!");
        }
        Iterator it = list.iterator();
        while (it.hasNext()) {
            Request request = (Request) it.next();
            Iterator it2 = request.getStreams().iterator();
            boolean z = false;
            while (it2.hasNext()) {
                int iM423unboximpl = ((StreamId) it2.next()).m423unboximpl();
                if (!map3.containsKey(StreamId.m417boximpl(iM423unboximpl))) {
                    Surface surface = (Surface) this.streamToSurfaceMap.get(StreamId.m417boximpl(iM423unboximpl));
                    if (surface != null) {
                        map.put(surface, StreamId.m417boximpl(iM423unboximpl));
                        map3.put(StreamId.m417boximpl(iM423unboximpl), surface);
                        CameraStream cameraStreamMo413getaKI5c8E = this.streamGraph.mo413getaKI5c8E(iM423unboximpl);
                        if (cameraStreamMo413getaKI5c8E == null) {
                            throw new IllegalStateException("Required value was null.");
                        }
                        for (OutputStream outputStream : cameraStreamMo413getaKI5c8E.getOutputs()) {
                            Object obj = this.outputToSurfaceMap.get(OutputId.m296boximpl(outputStream.mo317getId4LaLFng()));
                            if (obj == null) {
                                throw new IllegalStateException("Required value was null.");
                            }
                            map2.put((Surface) obj, OutputId.m296boximpl(outputStream.mo317getId4LaLFng()));
                        }
                    } else {
                        continue;
                    }
                }
                z = true;
            }
            if (!z) {
                if (Log.INSTANCE.getINFO_LOGGABLE()) {
                    android.util.Log.i("CXCP", "  Failed to bind any surfaces for " + request + '!');
                }
                return false;
            }
            if (!z) {
                throw new IllegalStateException("Check failed.");
            }
        }
        return true;
    }

    /* JADX INFO: renamed from: buildCaptureRequestBuilder-0UCm73U, reason: not valid java name */
    private final CaptureRequest.Builder m458buildCaptureRequestBuilder0UCm73U(Request request, int i) {
        CaptureRequest.Builder builderMo427createCaptureRequest2PPcXtw;
        if (request.getInputRequest() != null) {
            TotalCaptureResult totalCaptureResult = (TotalCaptureResult) request.getInputRequest().getFrameInfo().unwrapAs(Reflection.getOrCreateKotlinClass(TotalCaptureResult.class));
            if (totalCaptureResult == null) {
                throw new IllegalStateException(("Failed to unwrap FrameInfo " + request.getInputRequest().getFrameInfo() + " as TotalCaptureResult").toString());
            }
            builderMo427createCaptureRequest2PPcXtw = this.session.getDevice().createReprocessCaptureRequest(totalCaptureResult);
        } else {
            builderMo427createCaptureRequest2PPcXtw = this.session.getDevice().mo427createCaptureRequest2PPcXtw(i);
        }
        if (builderMo427createCaptureRequest2PPcXtw != null) {
            return builderMo427createCaptureRequest2PPcXtw;
        }
        if (request.getInputRequest() != null) {
            if (!Log.INSTANCE.getINFO_LOGGABLE()) {
                return null;
            }
            android.util.Log.i("CXCP", "Failed to create a ReprocessingCaptureRequest.Builder from " + request.getInputRequest().getFrameInfo() + '!');
            return null;
        }
        if (!Log.INSTANCE.getINFO_LOGGABLE()) {
            return null;
        }
        android.util.Log.i("CXCP", "Failed to create a CaptureRequest.Builder from " + ((Object) RequestTemplate.m390toStringimpl(i)) + '!');
        return null;
    }

    public static final class Companion {
        public /* synthetic */ Companion(DefaultConstructorMarker defaultConstructorMarker) {
            this();
        }

        private Companion() {
        }
    }
}
