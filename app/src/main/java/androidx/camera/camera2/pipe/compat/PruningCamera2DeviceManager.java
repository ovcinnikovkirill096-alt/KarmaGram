package androidx.camera.camera2.pipe.compat;

import androidx.camera.camera2.pipe.CameraError;
import androidx.camera.camera2.pipe.CameraId;
import androidx.camera.camera2.pipe.GraphState;
import androidx.camera.camera2.pipe.core.Log;
import androidx.camera.camera2.pipe.core.Permissions;
import androidx.camera.camera2.pipe.core.PruningProcessingQueue;
import androidx.camera.camera2.pipe.core.Threads;
import androidx.camera.camera2.pipe.core.Token;
import androidx.camera.camera2.pipe.graph.GraphListener;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.ListIterator;
import java.util.Set;
import kotlin.NoWhenBranchMatchedException;
import kotlin.ResultKt;
import kotlin.Unit;
import kotlin.collections.CollectionsKt;
import kotlin.coroutines.Continuation;
import kotlin.coroutines.intrinsics.IntrinsicsKt;
import kotlin.coroutines.jvm.internal.ContinuationImpl;
import kotlin.jvm.functions.Function1;
import kotlin.jvm.internal.DefaultConstructorMarker;
import kotlin.jvm.internal.Intrinsics;
import kotlinx.coroutines.CompletableDeferred;
import kotlinx.coroutines.CoroutineScope;
import kotlinx.coroutines.Deferred;

public final class PruningCamera2DeviceManager implements Camera2DeviceManager {
    private final Set activeCameras;
    private final Camera2DeviceCloser camera2DeviceCloser;
    private final Camera2ErrorProcessor camera2ErrorProcessor;
    private final List pendingRequestOpens;
    private final Permissions permissions;
    private final PruningProcessingQueue queue;
    private final RetryingCameraStateOpener retryingCameraStateOpener;
    private final CoroutineScope scope;
    private final Threads threads;

    /* JADX INFO: renamed from: androidx.camera.camera2.pipe.compat.PruningCamera2DeviceManager$connectPendingRequestOpens$1, reason: invalid class name */
    static final class AnonymousClass1 extends ContinuationImpl {
        Object L$0;
        Object L$1;
        int label;
        /* synthetic */ Object result;

        AnonymousClass1(Continuation continuation) {
            super(continuation);
        }

        @Override // kotlin.coroutines.jvm.internal.BaseContinuationImpl
        public final Object invokeSuspend(Object obj) {
            this.result = obj;
            this.label |= Integer.MIN_VALUE;
            return PruningCamera2DeviceManager.this.connectPendingRequestOpens(null, this);
        }
    }

    /* JADX INFO: renamed from: androidx.camera.camera2.pipe.compat.PruningCamera2DeviceManager$processRequestClose$1, reason: invalid class name and case insensitive filesystem */
    static final class C00991 extends ContinuationImpl {
        Object L$0;
        int label;
        /* synthetic */ Object result;

        C00991(Continuation continuation) {
            super(continuation);
        }

        @Override // kotlin.coroutines.jvm.internal.BaseContinuationImpl
        public final Object invokeSuspend(Object obj) {
            this.result = obj;
            this.label |= Integer.MIN_VALUE;
            return PruningCamera2DeviceManager.this.processRequestClose(null, this);
        }
    }

    /* JADX INFO: renamed from: androidx.camera.camera2.pipe.compat.PruningCamera2DeviceManager$processRequestCloseAll$1, reason: invalid class name and case insensitive filesystem */
    static final class C01001 extends ContinuationImpl {
        Object L$0;
        Object L$1;
        int label;
        /* synthetic */ Object result;

        C01001(Continuation continuation) {
            super(continuation);
        }

        @Override // kotlin.coroutines.jvm.internal.BaseContinuationImpl
        public final Object invokeSuspend(Object obj) {
            this.result = obj;
            this.label |= Integer.MIN_VALUE;
            return PruningCamera2DeviceManager.this.processRequestCloseAll(null, this);
        }
    }

    /* JADX INFO: renamed from: androidx.camera.camera2.pipe.compat.PruningCamera2DeviceManager$processRequestCloseById$1, reason: invalid class name and case insensitive filesystem */
    static final class C01011 extends ContinuationImpl {
        Object L$0;
        Object L$1;
        int label;
        /* synthetic */ Object result;

        C01011(Continuation continuation) {
            super(continuation);
        }

        @Override // kotlin.coroutines.jvm.internal.BaseContinuationImpl
        public final Object invokeSuspend(Object obj) {
            this.result = obj;
            this.label |= Integer.MIN_VALUE;
            return PruningCamera2DeviceManager.this.processRequestCloseById(null, this);
        }
    }

    /* JADX INFO: renamed from: androidx.camera.camera2.pipe.compat.PruningCamera2DeviceManager$processRequestOpen$1, reason: invalid class name and case insensitive filesystem */
    static final class C01021 extends ContinuationImpl {
        Object L$0;
        Object L$1;
        Object L$2;
        int label;
        /* synthetic */ Object result;

        C01021(Continuation continuation) {
            super(continuation);
        }

        @Override // kotlin.coroutines.jvm.internal.BaseContinuationImpl
        public final Object invokeSuspend(Object obj) {
            this.result = obj;
            this.label |= Integer.MIN_VALUE;
            return PruningCamera2DeviceManager.this.processRequestOpen(null, this);
        }
    }

    public PruningCamera2DeviceManager(Permissions permissions, RetryingCameraStateOpener retryingCameraStateOpener, Camera2DeviceCloser camera2DeviceCloser, Camera2ErrorProcessor camera2ErrorProcessor, Threads threads) {
        Intrinsics.checkNotNullParameter(permissions, "permissions");
        Intrinsics.checkNotNullParameter(retryingCameraStateOpener, "retryingCameraStateOpener");
        Intrinsics.checkNotNullParameter(camera2DeviceCloser, "camera2DeviceCloser");
        Intrinsics.checkNotNullParameter(camera2ErrorProcessor, "camera2ErrorProcessor");
        Intrinsics.checkNotNullParameter(threads, "threads");
        this.permissions = permissions;
        this.retryingCameraStateOpener = retryingCameraStateOpener;
        this.camera2DeviceCloser = camera2DeviceCloser;
        this.camera2ErrorProcessor = camera2ErrorProcessor;
        this.threads = threads;
        CoroutineScope cameraPipeScope = threads.getCameraPipeScope();
        this.scope = cameraPipeScope;
        this.queue = PruningProcessingQueue.Companion.processIn(new PruningProcessingQueue(0, new PruningCamera2DeviceManager$queue$1(this), null, new PruningCamera2DeviceManager$queue$2(this, null), 5, null), cameraPipeScope);
        this.activeCameras = new LinkedHashSet();
        this.pendingRequestOpens = new ArrayList();
    }

    private static final class PendingRequestOpen {
        private final ActiveCamera activeCamera;
        private final RequestOpen request;
        private final Token token;

        public PendingRequestOpen(RequestOpen request, ActiveCamera activeCamera, Token token) {
            Intrinsics.checkNotNullParameter(request, "request");
            Intrinsics.checkNotNullParameter(activeCamera, "activeCamera");
            Intrinsics.checkNotNullParameter(token, "token");
            this.request = request;
            this.activeCamera = activeCamera;
            this.token = token;
        }

        public final RequestOpen getRequest() {
            return this.request;
        }

        public final ActiveCamera getActiveCamera() {
            return this.activeCamera;
        }

        public final Token getToken() {
            return this.token;
        }
    }

    @Override // androidx.camera.camera2.pipe.compat.Camera2DeviceManager
    /* JADX INFO: renamed from: open-zDSwpeU */
    public VirtualCamera mo462openzDSwpeU(String cameraId, List sharedCameraIds, GraphListener graphListener, boolean z, Function1 isForegroundObserver) {
        Intrinsics.checkNotNullParameter(cameraId, "cameraId");
        Intrinsics.checkNotNullParameter(sharedCameraIds, "sharedCameraIds");
        Intrinsics.checkNotNullParameter(graphListener, "graphListener");
        Intrinsics.checkNotNullParameter(isForegroundObserver, "isForegroundObserver");
        VirtualCameraState virtualCameraState = new VirtualCameraState(cameraId, graphListener, this.scope, null);
        if (this.queue.tryEmit(new RequestOpen(virtualCameraState, sharedCameraIds, graphListener, z, isForegroundObserver))) {
            return virtualCameraState;
        }
        if (Log.INSTANCE.getERROR_LOGGABLE()) {
            android.util.Log.e("CXCP", "Camera open request failed for " + ((Object) CameraId.m238toStringimpl(cameraId)) + '!');
        }
        graphListener.onGraphError(new GraphState.GraphStateError(CameraError.Companion.m197getERROR_CAMERA_OPENERv7Vf74A(), false, null));
        return null;
    }

    @Override // androidx.camera.camera2.pipe.compat.Camera2DeviceManager
    /* JADX INFO: renamed from: close-EfqyGwQ */
    public Deferred mo461closeEfqyGwQ(String cameraId) {
        Intrinsics.checkNotNullParameter(cameraId, "cameraId");
        RequestCloseById requestCloseById = new RequestCloseById(cameraId, null);
        if (!this.queue.tryEmit(requestCloseById)) {
            if (Log.INSTANCE.getERROR_LOGGABLE()) {
                android.util.Log.e("CXCP", "Camera close by ID request failed for " + ((Object) CameraId.m238toStringimpl(cameraId)) + '!');
            }
            requestCloseById.getDeferred().complete(Unit.INSTANCE);
        }
        return requestCloseById.getDeferred();
    }

    @Override // androidx.camera.camera2.pipe.compat.Camera2DeviceManager
    public Deferred closeAll(boolean z) {
        if (z) {
            this.retryingCameraStateOpener.cancelOpen();
        }
        RequestCloseAll requestCloseAll = new RequestCloseAll();
        if (!this.queue.tryEmit(requestCloseAll)) {
            if (Log.INSTANCE.getERROR_LOGGABLE()) {
                android.util.Log.e("CXCP", "Camera close all request failed!");
            }
            requestCloseAll.getDeferred().complete(Unit.INSTANCE);
        }
        return requestCloseAll.getDeferred();
    }

    /* JADX WARN: Code duplicated, block: B:58:0x014b  */
    public final void prune$camera_camera2_pipe(List requests) {
        boolean z;
        int iNextIndex;
        Integer numValueOf;
        boolean zContains;
        final CompletableDeferred deferred;
        Intrinsics.checkNotNullParameter(requests, "requests");
        ArrayList arrayList = new ArrayList();
        for (Object obj : requests) {
            if (((CameraRequest) obj) instanceof RequestClose) {
                arrayList.add(obj);
            }
        }
        requests.removeAll(arrayList);
        Iterator it = CollectionsKt.reversed(arrayList).iterator();
        while (true) {
            z = false;
            if (!it.hasNext()) {
                break;
            } else {
                requests.add(0, (CameraRequest) it.next());
            }
        }
        ListIterator listIterator = requests.listIterator(requests.size());
        while (true) {
            if (listIterator.hasPrevious()) {
                if (((CameraRequest) listIterator.previous()) instanceof RequestCloseAll) {
                    iNextIndex = listIterator.nextIndex();
                    break;
                }
            } else {
                iNextIndex = -1;
                break;
            }
        }
        if (iNextIndex > 0) {
            Object obj2 = requests.get(iNextIndex);
            Intrinsics.checkNotNull(obj2, "null cannot be cast to non-null type androidx.camera.camera2.pipe.compat.RequestCloseAll");
            RequestCloseAll requestCloseAll = (RequestCloseAll) obj2;
            for (int i = 0; i < iNextIndex; i++) {
                CameraRequest cameraRequest = (CameraRequest) requests.remove(0);
                if (cameraRequest instanceof RequestCloseById) {
                    deferred = ((RequestCloseById) cameraRequest).getDeferred();
                } else {
                    deferred = cameraRequest instanceof RequestCloseAll ? ((RequestCloseAll) cameraRequest).getDeferred() : null;
                }
                if (deferred != null) {
                    requestCloseAll.getDeferred().invokeOnCompletion(new Function1() { // from class: androidx.camera.camera2.pipe.compat.PruningCamera2DeviceManager$$ExternalSyntheticLambda0
                        @Override // kotlin.jvm.functions.Function1
                        public final Object invoke(Object obj3) {
                            return PruningCamera2DeviceManager.prune$lambda$2$0(deferred, (Throwable) obj3);
                        }
                    });
                }
                onRemoved(cameraRequest);
            }
        }
        LinkedHashSet linkedHashSet = new LinkedHashSet();
        Iterator it2 = requests.iterator();
        int i2 = 0;
        while (it2.hasNext()) {
            int i3 = i2 + 1;
            final CameraRequest cameraRequest2 = (CameraRequest) it2.next();
            if (cameraRequest2 instanceof RequestOpen) {
                RequestOpen requestOpen = (RequestOpen) cameraRequest2;
                String strM502getCameraIdDz_R5H8 = requestOpen.getVirtualCamera().m502getCameraIdDz_R5H8();
                Set set = CollectionsKt.toSet(CollectionsKt.plus(requestOpen.getSharedCameraIds(), CameraId.m233boximpl(strM502getCameraIdDz_R5H8)));
                int size = requests.size();
                int i4 = i3;
                while (true) {
                    if (i4 >= size) {
                        numValueOf = null;
                        break;
                    }
                    CameraRequest cameraRequest3 = (CameraRequest) requests.get(i4);
                    if (cameraRequest3 instanceof RequestCloseById) {
                        zContains = set.contains(CameraId.m233boximpl(((RequestCloseById) cameraRequest3).m490getActiveCameraIdDz_R5H8()));
                    } else if (cameraRequest3 instanceof RequestOpen) {
                        boolean z2 = (requestOpen.isPrewarm() || !((RequestOpen) cameraRequest3).isPrewarm()) ? true : z;
                        RequestOpen requestOpen2 = (RequestOpen) cameraRequest3;
                        String strM502getCameraIdDz_R5H9 = requestOpen2.getVirtualCamera().m502getCameraIdDz_R5H8();
                        Set set2 = CollectionsKt.toSet(CollectionsKt.plus(requestOpen2.getSharedCameraIds(), CameraId.m233boximpl(strM502getCameraIdDz_R5H9)));
                        if (!z2 || (!CameraId.m236equalsimpl0(strM502getCameraIdDz_R5H8, strM502getCameraIdDz_R5H9) && Intrinsics.areEqual(set, set2))) {
                            zContains = false;
                        } else {
                            zContains = true;
                        }
                    } else {
                        zContains = false;
                    }
                    if (zContains) {
                        numValueOf = Integer.valueOf(i4);
                        break;
                    } else {
                        i4++;
                        z = false;
                    }
                }
            } else {
                if (!(cameraRequest2 instanceof RequestCloseById)) {
                    numValueOf = null;
                    break;
                }
                int size2 = requests.size();
                int i5 = i3;
                while (true) {
                    if (i5 >= size2) {
                        numValueOf = null;
                        break;
                    }
                    CameraRequest cameraRequest4 = (CameraRequest) requests.get(i5);
                    if ((cameraRequest4 instanceof RequestCloseById) && CameraId.m236equalsimpl0(((RequestCloseById) cameraRequest4).m490getActiveCameraIdDz_R5H8(), ((RequestCloseById) cameraRequest2).m490getActiveCameraIdDz_R5H8())) {
                        numValueOf = Integer.valueOf(i5);
                        break;
                    }
                    i5++;
                }
            }
            if (numValueOf != null) {
                CameraRequest cameraRequest5 = (CameraRequest) requests.get(numValueOf.intValue());
                if (Log.INSTANCE.getDEBUG_LOGGABLE()) {
                    android.util.Log.d("CXCP", cameraRequest2 + " is pruned by " + cameraRequest5);
                }
                linkedHashSet.add(Integer.valueOf(i2));
                if ((cameraRequest2 instanceof RequestCloseById) && (cameraRequest5 instanceof RequestCloseById)) {
                    ((RequestCloseById) cameraRequest5).getDeferred().invokeOnCompletion(new Function1() { // from class: androidx.camera.camera2.pipe.compat.PruningCamera2DeviceManager$$ExternalSyntheticLambda1
                        @Override // kotlin.jvm.functions.Function1
                        public final Object invoke(Object obj3) {
                            return PruningCamera2DeviceManager.prune$lambda$6(cameraRequest2, (Throwable) obj3);
                        }
                    });
                }
            }
            i2 = i3;
            z = false;
        }
        Iterator it3 = removeIndices(requests, linkedHashSet).iterator();
        while (it3.hasNext()) {
            onRemoved((CameraRequest) it3.next());
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static final Unit prune$lambda$2$0(CompletableDeferred completableDeferred, Throwable th) {
        Unit unit = Unit.INSTANCE;
        completableDeferred.complete(unit);
        return unit;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static final Unit prune$lambda$6(CameraRequest cameraRequest, Throwable th) {
        CompletableDeferred deferred = ((RequestCloseById) cameraRequest).getDeferred();
        Unit unit = Unit.INSTANCE;
        deferred.complete(unit);
        return unit;
    }

    private final void onRemoved(CameraRequest cameraRequest) {
        if (cameraRequest instanceof RequestOpen) {
            VirtualCamera.CC.m501disconnectTPqeGZw$default(((RequestOpen) cameraRequest).getVirtualCamera(), null, 1, null);
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public final Object process(CameraRequest cameraRequest, Continuation continuation) throws Throwable {
        if (cameraRequest instanceof RequestOpen) {
            Object objProcessRequestOpen = processRequestOpen((RequestOpen) cameraRequest, continuation);
            return objProcessRequestOpen == IntrinsicsKt.getCOROUTINE_SUSPENDED() ? objProcessRequestOpen : Unit.INSTANCE;
        }
        if (cameraRequest instanceof RequestClose) {
            Object objProcessRequestClose = processRequestClose((RequestClose) cameraRequest, continuation);
            return objProcessRequestClose == IntrinsicsKt.getCOROUTINE_SUSPENDED() ? objProcessRequestClose : Unit.INSTANCE;
        }
        if (cameraRequest instanceof RequestCloseById) {
            Object objProcessRequestCloseById = processRequestCloseById((RequestCloseById) cameraRequest, continuation);
            return objProcessRequestCloseById == IntrinsicsKt.getCOROUTINE_SUSPENDED() ? objProcessRequestCloseById : Unit.INSTANCE;
        }
        if (!(cameraRequest instanceof RequestCloseAll)) {
            throw new NoWhenBranchMatchedException();
        }
        Object objProcessRequestCloseAll = processRequestCloseAll((RequestCloseAll) cameraRequest, continuation);
        return objProcessRequestCloseAll == IntrinsicsKt.getCOROUTINE_SUSPENDED() ? objProcessRequestCloseAll : Unit.INSTANCE;
    }

    /* JADX INFO: Access modifiers changed from: private */
    /* JADX WARN: Code duplicated, block: B:102:0x02a5  */
    /* JADX WARN: Code duplicated, block: B:105:0x02b7  */
    /* JADX WARN: Code duplicated, block: B:111:0x02d1  */
    /* JADX WARN: Code duplicated, block: B:113:0x02d7  */
    /* JADX WARN: Code duplicated, block: B:115:0x02dd  */
    /* JADX WARN: Code duplicated, block: B:120:0x02f2  */
    /* JADX WARN: Code duplicated, block: B:123:0x02f8  */
    /* JADX WARN: Code duplicated, block: B:125:0x029f A[SYNTHETIC] */
    /* JADX WARN: Code duplicated, block: B:127:0x0294 A[SYNTHETIC] */
    /* JADX WARN: Code duplicated, block: B:128:? A[LOOP:0: B:86:0x0253->B:128:?, LOOP_END, SYNTHETIC] */
    /* JADX WARN: Code duplicated, block: B:129:? A[LOOP:1: B:94:0x0279->B:129:?, LOOP_END, SYNTHETIC] */
    /* JADX WARN: Code duplicated, block: B:131:0x02ee A[SYNTHETIC] */
    /* JADX WARN: Code duplicated, block: B:133:? A[LOOP:2: B:53:0x0177->B:133:?, LOOP_END, SYNTHETIC] */
    /* JADX WARN: Code duplicated, block: B:51:0x0169 A[LOOP:3: B:49:0x0163->B:51:0x0169, LOOP_END] */
    /* JADX WARN: Code duplicated, block: B:55:0x017d  */
    /* JADX WARN: Code duplicated, block: B:63:0x01b2  */
    /* JADX WARN: Code duplicated, block: B:66:0x01bc  */
    /* JADX WARN: Code duplicated, block: B:68:0x01c6  */
    /* JADX WARN: Code duplicated, block: B:70:0x01ce  */
    /* JADX WARN: Code duplicated, block: B:71:0x01f9  */
    /* JADX WARN: Code duplicated, block: B:73:0x0201  */
    /* JADX WARN: Code duplicated, block: B:76:0x021f  */
    /* JADX WARN: Code duplicated, block: B:78:0x0225  */
    /* JADX WARN: Code duplicated, block: B:7:0x0013  */
    /* JADX WARN: Code duplicated, block: B:80:0x023b  */
    /* JADX WARN: Code duplicated, block: B:82:0x0245  */
    /* JADX WARN: Code duplicated, block: B:85:0x024f  */
    /* JADX WARN: Code duplicated, block: B:88:0x0259  */
    /* JADX WARN: Code duplicated, block: B:90:0x026b  */
    /* JADX WARN: Code duplicated, block: B:93:0x0275  */
    /* JADX WARN: Code duplicated, block: B:96:0x027f  */
    /* JADX WARN: Code restructure failed: missing block: B:107:0x02cb, code lost:
    
        if (connectPendingRequestOpens(r11, r0) == r1) goto L117;
     */
    /* JADX WARN: Code restructure failed: missing block: B:116:0x02ec, code lost:
    
        if (r12.connectTo(r11, r0, r0) == r1) goto L117;
     */
    /* JADX WARN: Instruction removed from duplicated block: B:70:0x01ce, please report this as an issue */
    /* JADX WARN: Instruction removed from duplicated block: B:73:0x0201, please report this as an issue */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
    */
    public final Object processRequestOpen(RequestOpen requestOpen, Continuation continuation) throws Throwable {
        C01021 c01021;
        String strM502getCameraIdDz_R5H8;
        ArrayList arrayList;
        RequestOpen requestOpen2;
        List list;
        Iterator it;
        Iterator it2;
        String str;
        Object objM487retrieveActiveCameraRzXb1QE;
        Object obj;
        String str2;
        RequestOpen requestOpen3;
        ActiveCamera activeCamera;
        RetrieveActiveCameraResult retrieveActiveCameraResult;
        ActiveCamera activeCamera2;
        Token token;
        List sharedCameraIds;
        Iterator it3;
        String strM239unboximpl;
        List list2;
        Iterator it4;
        VirtualCameraState virtualCamera;
        RetrieveActiveCameraResult.Error error;
        if (continuation instanceof C01021) {
            c01021 = (C01021) continuation;
            int i = c01021.label;
            if ((i & Integer.MIN_VALUE) != 0) {
                c01021.label = i - Integer.MIN_VALUE;
            } else {
                c01021 = new C01021(continuation);
            }
        } else {
            c01021 = new C01021(continuation);
        }
        Object obj2 = c01021.result;
        Object coroutine_suspended = IntrinsicsKt.getCOROUTINE_SUSPENDED();
        switch (c01021.label) {
            case 0:
                ResultKt.throwOnFailure(obj2);
                strM502getCameraIdDz_R5H8 = requestOpen.getVirtualCamera().m502getCameraIdDz_R5H8();
                if (Log.INSTANCE.getINFO_LOGGABLE()) {
                    android.util.Log.i("CXCP", "PruningCamera2DeviceManager#processRequestOpen(" + ((Object) CameraId.m238toStringimpl(strM502getCameraIdDz_R5H8)) + ')');
                }
                if (requestOpen.getSharedCameraIds().isEmpty()) {
                    Set set = this.activeCameras;
                    arrayList = new ArrayList();
                    for (Object obj3 : set) {
                        if (!CameraId.m236equalsimpl0(((ActiveCamera) obj3).m425getCameraIdDz_R5H8(), strM502getCameraIdDz_R5H8)) {
                            arrayList.add(obj3);
                        }
                    }
                } else {
                    Set set2 = CollectionsKt.toSet(CollectionsKt.plus(requestOpen.getSharedCameraIds(), CameraId.m233boximpl(requestOpen.getVirtualCamera().m502getCameraIdDz_R5H8())));
                    Set set3 = this.activeCameras;
                    ArrayList arrayList2 = new ArrayList();
                    for (Object obj4 : set3) {
                        if (!Intrinsics.areEqual(((ActiveCamera) obj4).getAllCameraIds$camera_camera2_pipe(), set2)) {
                            arrayList2.add(obj4);
                        }
                    }
                    arrayList = arrayList2;
                }
                if (arrayList.isEmpty()) {
                    requestOpen2 = requestOpen;
                    str = strM502getCameraIdDz_R5H8;
                    this.camera2ErrorProcessor.m464setActiveVirtualCamera0r8Bogc$camera_camera2_pipe(str, requestOpen2.getVirtualCamera());
                    c01021.L$0 = requestOpen2;
                    c01021.L$1 = str;
                    c01021.L$2 = null;
                    c01021.label = 3;
                    objM487retrieveActiveCameraRzXb1QE = m487retrieveActiveCameraRzXb1QE(str, requestOpen2, c01021);
                    if (objM487retrieveActiveCameraRzXb1QE != coroutine_suspended) {
                        obj = objM487retrieveActiveCameraRzXb1QE;
                        str2 = str;
                        requestOpen3 = requestOpen2;
                        retrieveActiveCameraResult = (RetrieveActiveCameraResult) obj;
                        if (retrieveActiveCameraResult instanceof RetrieveActiveCameraResult.Error) {
                            error = (RetrieveActiveCameraResult.Error) retrieveActiveCameraResult;
                            if (error.m489getLastCameraErrormVEW8x0() != null) {
                                if (Log.INSTANCE.getERROR_LOGGABLE()) {
                                    android.util.Log.e("CXCP", "Failed to retrieve active camera for " + ((Object) CameraId.m238toStringimpl(str2)) + ". Last camera error was " + ((Object) CameraError.m187toStringimpl(error.m489getLastCameraErrormVEW8x0().m188unboximpl())));
                                }
                            } else if (Log.INSTANCE.getWARN_LOGGABLE()) {
                                android.util.Log.w("CXCP", "Failed to retrieve active camera for " + ((Object) CameraId.m238toStringimpl(str2)) + ". Camera might have been closed during opening.");
                            }
                            return Unit.INSTANCE;
                        }
                        if (!(retrieveActiveCameraResult instanceof RetrieveActiveCameraResult.Success)) {
                            throw new IllegalStateException("Check failed.");
                        }
                        RetrieveActiveCameraResult.Success success = (RetrieveActiveCameraResult.Success) retrieveActiveCameraResult;
                        activeCamera2 = success.getActiveCamera();
                        token = success.getToken();
                        if (requestOpen3.getSharedCameraIds().isEmpty()) {
                            sharedCameraIds = requestOpen3.getSharedCameraIds();
                            if (sharedCameraIds instanceof Collection) {
                                it3 = sharedCameraIds.iterator();
                                while (true) {
                                    if (it3.hasNext()) {
                                        strM239unboximpl = ((CameraId) it3.next()).m239unboximpl();
                                        list2 = this.pendingRequestOpens;
                                        if (list2 instanceof Collection) {
                                            it4 = list2.iterator();
                                            while (true) {
                                                if (it4.hasNext()) {
                                                    if (CameraId.m236equalsimpl0(((PendingRequestOpen) it4.next()).getActiveCamera().m425getCameraIdDz_R5H8(), strM239unboximpl)) {
                                                    }
                                                }
                                            }
                                        } else {
                                            it4 = list2.iterator();
                                            while (true) {
                                                if (it4.hasNext()) {
                                                    if (CameraId.m236equalsimpl0(((PendingRequestOpen) it4.next()).getActiveCamera().m425getCameraIdDz_R5H8(), strM239unboximpl)) {
                                                    }
                                                }
                                            }
                                        }
                                        this.pendingRequestOpens.add(new PendingRequestOpen(requestOpen3, activeCamera2, token));
                                        return Unit.INSTANCE;
                                    }
                                }
                            } else {
                                it3 = sharedCameraIds.iterator();
                                while (true) {
                                    if (it3.hasNext()) {
                                        strM239unboximpl = ((CameraId) it3.next()).m239unboximpl();
                                        list2 = this.pendingRequestOpens;
                                        if (list2 instanceof Collection) {
                                            it4 = list2.iterator();
                                            while (true) {
                                                if (it4.hasNext()) {
                                                    if (CameraId.m236equalsimpl0(((PendingRequestOpen) it4.next()).getActiveCamera().m425getCameraIdDz_R5H8(), strM239unboximpl)) {
                                                    }
                                                }
                                            }
                                        } else {
                                            it4 = list2.iterator();
                                            while (true) {
                                                if (it4.hasNext()) {
                                                    if (CameraId.m236equalsimpl0(((PendingRequestOpen) it4.next()).getActiveCamera().m425getCameraIdDz_R5H8(), strM239unboximpl)) {
                                                    }
                                                }
                                            }
                                        }
                                        this.pendingRequestOpens.add(new PendingRequestOpen(requestOpen3, activeCamera2, token));
                                        return Unit.INSTANCE;
                                    }
                                }
                            }
                            if (requestOpen3.isPrewarm()) {
                                throw new IllegalStateException("Check failed.");
                            }
                            virtualCamera = requestOpen3.getVirtualCamera();
                            c01021.L$0 = requestOpen3;
                            c01021.L$1 = null;
                            c01021.label = 4;
                            if (activeCamera2.connectTo(virtualCamera, token, c01021) != coroutine_suspended) {
                                c01021 = c01021;
                                Set set4 = CollectionsKt.toSet(requestOpen3.getSharedCameraIds());
                                c01021.L$0 = null;
                                c01021.label = 5;
                            }
                        } else {
                            if (requestOpen3.isPrewarm()) {
                                token.release();
                                return Unit.INSTANCE;
                            }
                            VirtualCameraState virtualCamera2 = requestOpen3.getVirtualCamera();
                            c01021.L$0 = null;
                            c01021.L$1 = null;
                            c01021.label = 6;
                        }
                    }
                    break;
                } else {
                    this.activeCameras.removeAll(arrayList);
                    List list3 = this.pendingRequestOpens;
                    List arrayList3 = new ArrayList();
                    for (Object obj5 : list3) {
                        if (arrayList.contains(((PendingRequestOpen) obj5).getActiveCamera())) {
                            arrayList3.add(obj5);
                        }
                    }
                    c01021.L$0 = requestOpen;
                    c01021.L$1 = strM502getCameraIdDz_R5H8;
                    c01021.L$2 = arrayList;
                    c01021.label = 1;
                    if (disconnectPendingRequestOpens(arrayList3, c01021) != coroutine_suspended) {
                        ArrayList arrayList4 = arrayList;
                        requestOpen2 = requestOpen;
                        list = arrayList4;
                        it = list.iterator();
                        while (it.hasNext()) {
                            ((ActiveCamera) it.next()).close();
                        }
                        it2 = list.iterator();
                        while (it2.hasNext()) {
                            activeCamera = (ActiveCamera) it2.next();
                            c01021.L$0 = requestOpen2;
                            c01021.L$1 = strM502getCameraIdDz_R5H8;
                            c01021.L$2 = it2;
                            c01021.label = 2;
                            if (activeCamera.awaitClosed(c01021) == coroutine_suspended) {
                            }
                        }
                        str = strM502getCameraIdDz_R5H8;
                        this.camera2ErrorProcessor.m464setActiveVirtualCamera0r8Bogc$camera_camera2_pipe(str, requestOpen2.getVirtualCamera());
                        c01021.L$0 = requestOpen2;
                        c01021.L$1 = str;
                        c01021.L$2 = null;
                        c01021.label = 3;
                        objM487retrieveActiveCameraRzXb1QE = m487retrieveActiveCameraRzXb1QE(str, requestOpen2, c01021);
                        if (objM487retrieveActiveCameraRzXb1QE != coroutine_suspended) {
                            obj = objM487retrieveActiveCameraRzXb1QE;
                            str2 = str;
                            requestOpen3 = requestOpen2;
                            retrieveActiveCameraResult = (RetrieveActiveCameraResult) obj;
                            if (retrieveActiveCameraResult instanceof RetrieveActiveCameraResult.Error) {
                                error = (RetrieveActiveCameraResult.Error) retrieveActiveCameraResult;
                                if (error.m489getLastCameraErrormVEW8x0() != null) {
                                    if (Log.INSTANCE.getERROR_LOGGABLE()) {
                                        android.util.Log.e("CXCP", "Failed to retrieve active camera for " + ((Object) CameraId.m238toStringimpl(str2)) + ". Last camera error was " + ((Object) CameraError.m187toStringimpl(error.m489getLastCameraErrormVEW8x0().m188unboximpl())));
                                    }
                                } else if (Log.INSTANCE.getWARN_LOGGABLE()) {
                                    android.util.Log.w("CXCP", "Failed to retrieve active camera for " + ((Object) CameraId.m238toStringimpl(str2)) + ". Camera might have been closed during opening.");
                                }
                                return Unit.INSTANCE;
                            }
                            if (!(retrieveActiveCameraResult instanceof RetrieveActiveCameraResult.Success)) {
                                throw new IllegalStateException("Check failed.");
                            }
                            RetrieveActiveCameraResult.Success success2 = (RetrieveActiveCameraResult.Success) retrieveActiveCameraResult;
                            activeCamera2 = success2.getActiveCamera();
                            token = success2.getToken();
                            if (requestOpen3.getSharedCameraIds().isEmpty()) {
                                if (requestOpen3.isPrewarm()) {
                                    token.release();
                                    return Unit.INSTANCE;
                                }
                                VirtualCameraState virtualCamera3 = requestOpen3.getVirtualCamera();
                                c01021.L$0 = null;
                                c01021.L$1 = null;
                                c01021.label = 6;
                            } else {
                                sharedCameraIds = requestOpen3.getSharedCameraIds();
                                if ((sharedCameraIds instanceof Collection) || !sharedCameraIds.isEmpty()) {
                                    it3 = sharedCameraIds.iterator();
                                    while (true) {
                                        if (it3.hasNext()) {
                                            strM239unboximpl = ((CameraId) it3.next()).m239unboximpl();
                                            list2 = this.pendingRequestOpens;
                                            if ((list2 instanceof Collection) || !list2.isEmpty()) {
                                                it4 = list2.iterator();
                                                while (true) {
                                                    if (it4.hasNext()) {
                                                        if (CameraId.m236equalsimpl0(((PendingRequestOpen) it4.next()).getActiveCamera().m425getCameraIdDz_R5H8(), strM239unboximpl)) {
                                                        }
                                                    }
                                                }
                                            }
                                            this.pendingRequestOpens.add(new PendingRequestOpen(requestOpen3, activeCamera2, token));
                                            return Unit.INSTANCE;
                                        }
                                    }
                                }
                                if (requestOpen3.isPrewarm()) {
                                    throw new IllegalStateException("Check failed.");
                                }
                                virtualCamera = requestOpen3.getVirtualCamera();
                                c01021.L$0 = requestOpen3;
                                c01021.L$1 = null;
                                c01021.label = 4;
                                if (activeCamera2.connectTo(virtualCamera, token, c01021) != coroutine_suspended) {
                                    c01021 = c01021;
                                    Set set5 = CollectionsKt.toSet(requestOpen3.getSharedCameraIds());
                                    c01021.L$0 = null;
                                    c01021.label = 5;
                                }
                            }
                        }
                    }
                    break;
                }
                return coroutine_suspended;
            case 1:
                list = (List) c01021.L$2;
                strM502getCameraIdDz_R5H8 = (String) c01021.L$1;
                requestOpen2 = (RequestOpen) c01021.L$0;
                ResultKt.throwOnFailure(obj2);
                it = list.iterator();
                while (it.hasNext()) {
                    ((ActiveCamera) it.next()).close();
                }
                it2 = list.iterator();
                while (it2.hasNext()) {
                    activeCamera = (ActiveCamera) it2.next();
                    c01021.L$0 = requestOpen2;
                    c01021.L$1 = strM502getCameraIdDz_R5H8;
                    c01021.L$2 = it2;
                    c01021.label = 2;
                    if (activeCamera.awaitClosed(c01021) == coroutine_suspended) {
                        return coroutine_suspended;
                    }
                }
                str = strM502getCameraIdDz_R5H8;
                this.camera2ErrorProcessor.m464setActiveVirtualCamera0r8Bogc$camera_camera2_pipe(str, requestOpen2.getVirtualCamera());
                c01021.L$0 = requestOpen2;
                c01021.L$1 = str;
                c01021.L$2 = null;
                c01021.label = 3;
                objM487retrieveActiveCameraRzXb1QE = m487retrieveActiveCameraRzXb1QE(str, requestOpen2, c01021);
                if (objM487retrieveActiveCameraRzXb1QE != coroutine_suspended) {
                    obj = objM487retrieveActiveCameraRzXb1QE;
                    str2 = str;
                    requestOpen3 = requestOpen2;
                    retrieveActiveCameraResult = (RetrieveActiveCameraResult) obj;
                    if (retrieveActiveCameraResult instanceof RetrieveActiveCameraResult.Error) {
                        error = (RetrieveActiveCameraResult.Error) retrieveActiveCameraResult;
                        if (error.m489getLastCameraErrormVEW8x0() != null) {
                            if (Log.INSTANCE.getERROR_LOGGABLE()) {
                                android.util.Log.e("CXCP", "Failed to retrieve active camera for " + ((Object) CameraId.m238toStringimpl(str2)) + ". Last camera error was " + ((Object) CameraError.m187toStringimpl(error.m489getLastCameraErrormVEW8x0().m188unboximpl())));
                            }
                        } else if (Log.INSTANCE.getWARN_LOGGABLE()) {
                            android.util.Log.w("CXCP", "Failed to retrieve active camera for " + ((Object) CameraId.m238toStringimpl(str2)) + ". Camera might have been closed during opening.");
                        }
                        return Unit.INSTANCE;
                    }
                    if (!(retrieveActiveCameraResult instanceof RetrieveActiveCameraResult.Success)) {
                        throw new IllegalStateException("Check failed.");
                    }
                    RetrieveActiveCameraResult.Success success3 = (RetrieveActiveCameraResult.Success) retrieveActiveCameraResult;
                    activeCamera2 = success3.getActiveCamera();
                    token = success3.getToken();
                    if (requestOpen3.getSharedCameraIds().isEmpty()) {
                        if (requestOpen3.isPrewarm()) {
                            token.release();
                            return Unit.INSTANCE;
                        }
                        VirtualCameraState virtualCamera4 = requestOpen3.getVirtualCamera();
                        c01021.L$0 = null;
                        c01021.L$1 = null;
                        c01021.label = 6;
                    } else {
                        sharedCameraIds = requestOpen3.getSharedCameraIds();
                        if (sharedCameraIds instanceof Collection) {
                            it3 = sharedCameraIds.iterator();
                            while (true) {
                                if (it3.hasNext()) {
                                    strM239unboximpl = ((CameraId) it3.next()).m239unboximpl();
                                    list2 = this.pendingRequestOpens;
                                    if (list2 instanceof Collection) {
                                        it4 = list2.iterator();
                                        while (true) {
                                            if (it4.hasNext()) {
                                                if (CameraId.m236equalsimpl0(((PendingRequestOpen) it4.next()).getActiveCamera().m425getCameraIdDz_R5H8(), strM239unboximpl)) {
                                                }
                                            }
                                        }
                                    } else {
                                        it4 = list2.iterator();
                                        while (true) {
                                            if (it4.hasNext()) {
                                                if (CameraId.m236equalsimpl0(((PendingRequestOpen) it4.next()).getActiveCamera().m425getCameraIdDz_R5H8(), strM239unboximpl)) {
                                                }
                                            }
                                        }
                                    }
                                    this.pendingRequestOpens.add(new PendingRequestOpen(requestOpen3, activeCamera2, token));
                                    return Unit.INSTANCE;
                                }
                            }
                        } else {
                            it3 = sharedCameraIds.iterator();
                            while (true) {
                                if (it3.hasNext()) {
                                    strM239unboximpl = ((CameraId) it3.next()).m239unboximpl();
                                    list2 = this.pendingRequestOpens;
                                    if (list2 instanceof Collection) {
                                        it4 = list2.iterator();
                                        while (true) {
                                            if (it4.hasNext()) {
                                                if (CameraId.m236equalsimpl0(((PendingRequestOpen) it4.next()).getActiveCamera().m425getCameraIdDz_R5H8(), strM239unboximpl)) {
                                                }
                                            }
                                        }
                                    } else {
                                        it4 = list2.iterator();
                                        while (true) {
                                            if (it4.hasNext()) {
                                                if (CameraId.m236equalsimpl0(((PendingRequestOpen) it4.next()).getActiveCamera().m425getCameraIdDz_R5H8(), strM239unboximpl)) {
                                                }
                                            }
                                        }
                                    }
                                    this.pendingRequestOpens.add(new PendingRequestOpen(requestOpen3, activeCamera2, token));
                                    return Unit.INSTANCE;
                                }
                            }
                        }
                        if (requestOpen3.isPrewarm()) {
                            throw new IllegalStateException("Check failed.");
                        }
                        virtualCamera = requestOpen3.getVirtualCamera();
                        c01021.L$0 = requestOpen3;
                        c01021.L$1 = null;
                        c01021.label = 4;
                        if (activeCamera2.connectTo(virtualCamera, token, c01021) != coroutine_suspended) {
                            c01021 = c01021;
                            Set set6 = CollectionsKt.toSet(requestOpen3.getSharedCameraIds());
                            c01021.L$0 = null;
                            c01021.label = 5;
                        }
                    }
                    break;
                }
                return coroutine_suspended;
            case 2:
                it2 = (Iterator) c01021.L$2;
                strM502getCameraIdDz_R5H8 = (String) c01021.L$1;
                requestOpen2 = (RequestOpen) c01021.L$0;
                ResultKt.throwOnFailure(obj2);
                while (it2.hasNext()) {
                    activeCamera = (ActiveCamera) it2.next();
                    c01021.L$0 = requestOpen2;
                    c01021.L$1 = strM502getCameraIdDz_R5H8;
                    c01021.L$2 = it2;
                    c01021.label = 2;
                    if (activeCamera.awaitClosed(c01021) == coroutine_suspended) {
                        return coroutine_suspended;
                    }
                }
                str = strM502getCameraIdDz_R5H8;
                this.camera2ErrorProcessor.m464setActiveVirtualCamera0r8Bogc$camera_camera2_pipe(str, requestOpen2.getVirtualCamera());
                c01021.L$0 = requestOpen2;
                c01021.L$1 = str;
                c01021.L$2 = null;
                c01021.label = 3;
                objM487retrieveActiveCameraRzXb1QE = m487retrieveActiveCameraRzXb1QE(str, requestOpen2, c01021);
                if (objM487retrieveActiveCameraRzXb1QE != coroutine_suspended) {
                    obj = objM487retrieveActiveCameraRzXb1QE;
                    str2 = str;
                    requestOpen3 = requestOpen2;
                    retrieveActiveCameraResult = (RetrieveActiveCameraResult) obj;
                    if (retrieveActiveCameraResult instanceof RetrieveActiveCameraResult.Error) {
                        error = (RetrieveActiveCameraResult.Error) retrieveActiveCameraResult;
                        if (error.m489getLastCameraErrormVEW8x0() != null) {
                            if (Log.INSTANCE.getERROR_LOGGABLE()) {
                                android.util.Log.e("CXCP", "Failed to retrieve active camera for " + ((Object) CameraId.m238toStringimpl(str2)) + ". Last camera error was " + ((Object) CameraError.m187toStringimpl(error.m489getLastCameraErrormVEW8x0().m188unboximpl())));
                            }
                        } else if (Log.INSTANCE.getWARN_LOGGABLE()) {
                            android.util.Log.w("CXCP", "Failed to retrieve active camera for " + ((Object) CameraId.m238toStringimpl(str2)) + ". Camera might have been closed during opening.");
                        }
                        return Unit.INSTANCE;
                    }
                    if (!(retrieveActiveCameraResult instanceof RetrieveActiveCameraResult.Success)) {
                        throw new IllegalStateException("Check failed.");
                    }
                    RetrieveActiveCameraResult.Success success4 = (RetrieveActiveCameraResult.Success) retrieveActiveCameraResult;
                    activeCamera2 = success4.getActiveCamera();
                    token = success4.getToken();
                    if (requestOpen3.getSharedCameraIds().isEmpty()) {
                        if (requestOpen3.isPrewarm()) {
                            token.release();
                            return Unit.INSTANCE;
                        }
                        VirtualCameraState virtualCamera5 = requestOpen3.getVirtualCamera();
                        c01021.L$0 = null;
                        c01021.L$1 = null;
                        c01021.label = 6;
                    } else {
                        sharedCameraIds = requestOpen3.getSharedCameraIds();
                        if (sharedCameraIds instanceof Collection) {
                            it3 = sharedCameraIds.iterator();
                            while (true) {
                                if (it3.hasNext()) {
                                    strM239unboximpl = ((CameraId) it3.next()).m239unboximpl();
                                    list2 = this.pendingRequestOpens;
                                    if (list2 instanceof Collection) {
                                        it4 = list2.iterator();
                                        while (true) {
                                            if (it4.hasNext()) {
                                                if (CameraId.m236equalsimpl0(((PendingRequestOpen) it4.next()).getActiveCamera().m425getCameraIdDz_R5H8(), strM239unboximpl)) {
                                                }
                                            }
                                        }
                                    } else {
                                        it4 = list2.iterator();
                                        while (true) {
                                            if (it4.hasNext()) {
                                                if (CameraId.m236equalsimpl0(((PendingRequestOpen) it4.next()).getActiveCamera().m425getCameraIdDz_R5H8(), strM239unboximpl)) {
                                                }
                                            }
                                        }
                                    }
                                    this.pendingRequestOpens.add(new PendingRequestOpen(requestOpen3, activeCamera2, token));
                                    return Unit.INSTANCE;
                                }
                            }
                        } else {
                            it3 = sharedCameraIds.iterator();
                            while (true) {
                                if (it3.hasNext()) {
                                    strM239unboximpl = ((CameraId) it3.next()).m239unboximpl();
                                    list2 = this.pendingRequestOpens;
                                    if (list2 instanceof Collection) {
                                        it4 = list2.iterator();
                                        while (true) {
                                            if (it4.hasNext()) {
                                                if (CameraId.m236equalsimpl0(((PendingRequestOpen) it4.next()).getActiveCamera().m425getCameraIdDz_R5H8(), strM239unboximpl)) {
                                                }
                                            }
                                        }
                                    } else {
                                        it4 = list2.iterator();
                                        while (true) {
                                            if (it4.hasNext()) {
                                                if (CameraId.m236equalsimpl0(((PendingRequestOpen) it4.next()).getActiveCamera().m425getCameraIdDz_R5H8(), strM239unboximpl)) {
                                                }
                                            }
                                        }
                                    }
                                    this.pendingRequestOpens.add(new PendingRequestOpen(requestOpen3, activeCamera2, token));
                                    return Unit.INSTANCE;
                                }
                            }
                        }
                        if (requestOpen3.isPrewarm()) {
                            throw new IllegalStateException("Check failed.");
                        }
                        virtualCamera = requestOpen3.getVirtualCamera();
                        c01021.L$0 = requestOpen3;
                        c01021.L$1 = null;
                        c01021.label = 4;
                        if (activeCamera2.connectTo(virtualCamera, token, c01021) != coroutine_suspended) {
                            c01021 = c01021;
                            Set set7 = CollectionsKt.toSet(requestOpen3.getSharedCameraIds());
                            c01021.L$0 = null;
                            c01021.label = 5;
                        }
                    }
                    break;
                }
                return coroutine_suspended;
            case 3:
                String str3 = (String) c01021.L$1;
                RequestOpen requestOpen4 = (RequestOpen) c01021.L$0;
                ResultKt.throwOnFailure(obj2);
                str2 = str3;
                requestOpen3 = requestOpen4;
                obj = obj2;
                retrieveActiveCameraResult = (RetrieveActiveCameraResult) obj;
                if (retrieveActiveCameraResult instanceof RetrieveActiveCameraResult.Error) {
                    error = (RetrieveActiveCameraResult.Error) retrieveActiveCameraResult;
                    if (error.m489getLastCameraErrormVEW8x0() != null) {
                        if (Log.INSTANCE.getERROR_LOGGABLE()) {
                            android.util.Log.e("CXCP", "Failed to retrieve active camera for " + ((Object) CameraId.m238toStringimpl(str2)) + ". Last camera error was " + ((Object) CameraError.m187toStringimpl(error.m489getLastCameraErrormVEW8x0().m188unboximpl())));
                        }
                    } else if (Log.INSTANCE.getWARN_LOGGABLE()) {
                        android.util.Log.w("CXCP", "Failed to retrieve active camera for " + ((Object) CameraId.m238toStringimpl(str2)) + ". Camera might have been closed during opening.");
                    }
                    return Unit.INSTANCE;
                }
                if (!(retrieveActiveCameraResult instanceof RetrieveActiveCameraResult.Success)) {
                    throw new IllegalStateException("Check failed.");
                }
                RetrieveActiveCameraResult.Success success5 = (RetrieveActiveCameraResult.Success) retrieveActiveCameraResult;
                activeCamera2 = success5.getActiveCamera();
                token = success5.getToken();
                if (requestOpen3.getSharedCameraIds().isEmpty()) {
                    sharedCameraIds = requestOpen3.getSharedCameraIds();
                    if (sharedCameraIds instanceof Collection) {
                        it3 = sharedCameraIds.iterator();
                        while (true) {
                            if (it3.hasNext()) {
                                strM239unboximpl = ((CameraId) it3.next()).m239unboximpl();
                                list2 = this.pendingRequestOpens;
                                if (list2 instanceof Collection) {
                                    it4 = list2.iterator();
                                    while (true) {
                                        if (it4.hasNext()) {
                                            if (CameraId.m236equalsimpl0(((PendingRequestOpen) it4.next()).getActiveCamera().m425getCameraIdDz_R5H8(), strM239unboximpl)) {
                                            }
                                        }
                                    }
                                } else {
                                    it4 = list2.iterator();
                                    while (true) {
                                        if (it4.hasNext()) {
                                            if (CameraId.m236equalsimpl0(((PendingRequestOpen) it4.next()).getActiveCamera().m425getCameraIdDz_R5H8(), strM239unboximpl)) {
                                            }
                                        }
                                    }
                                }
                                this.pendingRequestOpens.add(new PendingRequestOpen(requestOpen3, activeCamera2, token));
                                return Unit.INSTANCE;
                            }
                        }
                    } else {
                        it3 = sharedCameraIds.iterator();
                        while (true) {
                            if (it3.hasNext()) {
                                strM239unboximpl = ((CameraId) it3.next()).m239unboximpl();
                                list2 = this.pendingRequestOpens;
                                if (list2 instanceof Collection) {
                                    it4 = list2.iterator();
                                    while (true) {
                                        if (it4.hasNext()) {
                                            if (CameraId.m236equalsimpl0(((PendingRequestOpen) it4.next()).getActiveCamera().m425getCameraIdDz_R5H8(), strM239unboximpl)) {
                                            }
                                        }
                                    }
                                } else {
                                    it4 = list2.iterator();
                                    while (true) {
                                        if (it4.hasNext()) {
                                            if (CameraId.m236equalsimpl0(((PendingRequestOpen) it4.next()).getActiveCamera().m425getCameraIdDz_R5H8(), strM239unboximpl)) {
                                            }
                                        }
                                    }
                                }
                                this.pendingRequestOpens.add(new PendingRequestOpen(requestOpen3, activeCamera2, token));
                                return Unit.INSTANCE;
                            }
                        }
                    }
                    if (requestOpen3.isPrewarm()) {
                        throw new IllegalStateException("Check failed.");
                    }
                    virtualCamera = requestOpen3.getVirtualCamera();
                    c01021.L$0 = requestOpen3;
                    c01021.L$1 = null;
                    c01021.label = 4;
                    if (activeCamera2.connectTo(virtualCamera, token, c01021) != coroutine_suspended) {
                        c01021 = c01021;
                        Set set8 = CollectionsKt.toSet(requestOpen3.getSharedCameraIds());
                        c01021.L$0 = null;
                        c01021.label = 5;
                        break;
                    }
                    return coroutine_suspended;
                }
                if (requestOpen3.isPrewarm()) {
                    token.release();
                    return Unit.INSTANCE;
                }
                VirtualCameraState virtualCamera6 = requestOpen3.getVirtualCamera();
                c01021.L$0 = null;
                c01021.L$1 = null;
                c01021.label = 6;
                break;
                break;
            case 4:
                requestOpen3 = (RequestOpen) c01021.L$0;
                ResultKt.throwOnFailure(obj2);
                Set set9 = CollectionsKt.toSet(requestOpen3.getSharedCameraIds());
                c01021.L$0 = null;
                c01021.label = 5;
                break;
            case 5:
                ResultKt.throwOnFailure(obj2);
                return Unit.INSTANCE;
            case 6:
                ResultKt.throwOnFailure(obj2);
                return Unit.INSTANCE;
            default:
                throw new IllegalStateException("call to 'resume' before 'invoke' with coroutine");
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    /* JADX WARN: Code duplicated, block: B:7:0x0013  */
    /* JADX WARN: Code restructure failed: missing block: B:32:0x00d0, code lost:
    
        if (r9.awaitClosed(r0) == r1) goto L33;
     */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
    */
    public final Object processRequestClose(RequestClose requestClose, Continuation continuation) throws Throwable {
        C00991 c00991;
        if (continuation instanceof C00991) {
            c00991 = (C00991) continuation;
            int i = c00991.label;
            if ((i & Integer.MIN_VALUE) != 0) {
                c00991.label = i - Integer.MIN_VALUE;
            } else {
                c00991 = new C00991(continuation);
            }
        } else {
            c00991 = new C00991(continuation);
        }
        Object obj = c00991.result;
        Object coroutine_suspended = IntrinsicsKt.getCOROUTINE_SUSPENDED();
        int i2 = c00991.label;
        if (i2 == 0) {
            ResultKt.throwOnFailure(obj);
            String strM425getCameraIdDz_R5H8 = requestClose.getActiveCamera().m425getCameraIdDz_R5H8();
            if (Log.INSTANCE.getINFO_LOGGABLE()) {
                android.util.Log.i("CXCP", "PruningCamera2DeviceManager#processRequestClose(" + ((Object) CameraId.m238toStringimpl(strM425getCameraIdDz_R5H8)) + ')');
            }
            if (this.activeCameras.contains(requestClose.getActiveCamera())) {
                this.activeCameras.remove(requestClose.getActiveCamera());
            }
            List list = this.pendingRequestOpens;
            List arrayList = new ArrayList();
            for (Object obj2 : list) {
                if (Intrinsics.areEqual(((PendingRequestOpen) obj2).getActiveCamera(), requestClose.getActiveCamera())) {
                    arrayList.add(obj2);
                }
            }
            c00991.L$0 = requestClose;
            c00991.label = 1;
            if (disconnectPendingRequestOpens(arrayList, c00991) != coroutine_suspended) {
            }
            return coroutine_suspended;
        }
        if (i2 == 1) {
            requestClose = (RequestClose) c00991.L$0;
            ResultKt.throwOnFailure(obj);
        } else {
            if (i2 != 2) {
                throw new IllegalStateException("call to 'resume' before 'invoke' with coroutine");
            }
            ResultKt.throwOnFailure(obj);
        }
        return Unit.INSTANCE;
        requestClose.getActiveCamera().close();
        ActiveCamera activeCamera = requestClose.getActiveCamera();
        c00991.L$0 = null;
        c00991.label = 2;
    }

    /* JADX INFO: Access modifiers changed from: private */
    /* JADX WARN: Code duplicated, block: B:7:0x0013  */
    public final Object processRequestCloseById(RequestCloseById requestCloseById, Continuation continuation) throws Throwable {
        C01011 c01011;
        RequestCloseById requestCloseById2;
        String str;
        Object next;
        RequestCloseById requestCloseById3;
        if (continuation instanceof C01011) {
            c01011 = (C01011) continuation;
            int i = c01011.label;
            if ((i & Integer.MIN_VALUE) != 0) {
                c01011.label = i - Integer.MIN_VALUE;
            } else {
                c01011 = new C01011(continuation);
            }
        } else {
            c01011 = new C01011(continuation);
        }
        Object obj = c01011.result;
        Object coroutine_suspended = IntrinsicsKt.getCOROUTINE_SUSPENDED();
        int i2 = c01011.label;
        if (i2 == 0) {
            ResultKt.throwOnFailure(obj);
            String strM490getActiveCameraIdDz_R5H8 = requestCloseById.m490getActiveCameraIdDz_R5H8();
            if (Log.INSTANCE.getINFO_LOGGABLE()) {
                android.util.Log.i("CXCP", "PruningCamera2DeviceManager#processRequestCloseById(" + ((Object) CameraId.m238toStringimpl(requestCloseById.m490getActiveCameraIdDz_R5H8())) + ')');
            }
            List list = this.pendingRequestOpens;
            List arrayList = new ArrayList();
            for (Object obj2 : list) {
                if (CameraId.m236equalsimpl0(((PendingRequestOpen) obj2).getRequest().getVirtualCamera().m502getCameraIdDz_R5H8(), strM490getActiveCameraIdDz_R5H8)) {
                    arrayList.add(obj2);
                }
            }
            c01011.L$0 = requestCloseById;
            c01011.L$1 = strM490getActiveCameraIdDz_R5H8;
            c01011.label = 1;
            if (disconnectPendingRequestOpens(arrayList, c01011) != coroutine_suspended) {
                requestCloseById2 = requestCloseById;
                str = strM490getActiveCameraIdDz_R5H8;
            }
            return coroutine_suspended;
        }
        if (i2 == 1) {
            str = (String) c01011.L$1;
            requestCloseById2 = (RequestCloseById) c01011.L$0;
            ResultKt.throwOnFailure(obj);
        } else {
            if (i2 != 2) {
                throw new IllegalStateException("call to 'resume' before 'invoke' with coroutine");
            }
            requestCloseById3 = (RequestCloseById) c01011.L$0;
            ResultKt.throwOnFailure(obj);
        }
        requestCloseById2 = requestCloseById3;
        CompletableDeferred deferred = requestCloseById2.getDeferred();
        Unit unit = Unit.INSTANCE;
        deferred.complete(unit);
        return unit;
        Iterator it = this.activeCameras.iterator();
        do {
            if (!it.hasNext()) {
                next = null;
                break;
            }
            next = it.next();
        } while (!CameraId.m236equalsimpl0(((ActiveCamera) next).m425getCameraIdDz_R5H8(), str));
        ActiveCamera activeCamera = (ActiveCamera) next;
        if (activeCamera != null) {
            this.activeCameras.remove(activeCamera);
            activeCamera.close();
            c01011.L$0 = requestCloseById2;
            c01011.L$1 = null;
            c01011.label = 2;
            if (activeCamera.awaitClosed(c01011) != coroutine_suspended) {
                requestCloseById3 = requestCloseById2;
                requestCloseById2 = requestCloseById3;
            }
            return coroutine_suspended;
        }
        CompletableDeferred deferred2 = requestCloseById2.getDeferred();
        Unit unit2 = Unit.INSTANCE;
        deferred2.complete(unit2);
        return unit2;
    }

    /* JADX INFO: Access modifiers changed from: private */
    /* JADX WARN: Code duplicated, block: B:29:0x0087  */
    /* JADX WARN: Code duplicated, block: B:35:0x0099 A[SYNTHETIC] */
    /* JADX WARN: Code duplicated, block: B:37:? A[LOOP:0: B:27:0x0081->B:37:?, LOOP_END, SYNTHETIC] */
    /* JADX WARN: Code duplicated, block: B:7:0x0013  */
    /* JADX WARN: Code restructure failed: missing block: B:20:0x0060, code lost:
    
        if (disconnectPendingRequestOpens(r7, r0) == r1) goto L31;
     */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
    */
    public final Object processRequestCloseAll(RequestCloseAll requestCloseAll, Continuation continuation) throws Throwable {
        C01001 c01001;
        RequestCloseAll requestCloseAll2;
        Iterator it;
        ActiveCamera activeCamera;
        if (continuation instanceof C01001) {
            c01001 = (C01001) continuation;
            int i = c01001.label;
            if ((i & Integer.MIN_VALUE) != 0) {
                c01001.label = i - Integer.MIN_VALUE;
            } else {
                c01001 = new C01001(continuation);
            }
        } else {
            c01001 = new C01001(continuation);
        }
        Object obj = c01001.result;
        Object coroutine_suspended = IntrinsicsKt.getCOROUTINE_SUSPENDED();
        int i2 = c01001.label;
        if (i2 == 0) {
            ResultKt.throwOnFailure(obj);
            if (Log.INSTANCE.getINFO_LOGGABLE()) {
                android.util.Log.i("CXCP", "PruningCamera2DeviceManager#processRequestCloseAll()");
            }
            List list = this.pendingRequestOpens;
            c01001.L$0 = requestCloseAll;
            c01001.label = 1;
        } else {
            if (i2 == 1) {
                requestCloseAll = (RequestCloseAll) c01001.L$0;
                ResultKt.throwOnFailure(obj);
            } else {
                if (i2 != 2) {
                    throw new IllegalStateException("call to 'resume' before 'invoke' with coroutine");
                }
                it = (Iterator) c01001.L$1;
                requestCloseAll2 = (RequestCloseAll) c01001.L$0;
                ResultKt.throwOnFailure(obj);
            }
            while (it.hasNext()) {
                activeCamera = (ActiveCamera) it.next();
                c01001.L$0 = requestCloseAll2;
                c01001.L$1 = it;
                c01001.label = 2;
                if (activeCamera.awaitClosed(c01001) == coroutine_suspended) {
                    return coroutine_suspended;
                }
            }
            this.activeCameras.clear();
            CompletableDeferred deferred = requestCloseAll2.getDeferred();
            Unit unit = Unit.INSTANCE;
            deferred.complete(unit);
            return unit;
        }
        Iterator it2 = this.activeCameras.iterator();
        while (it2.hasNext()) {
            ((ActiveCamera) it2.next()).close();
        }
        requestCloseAll2 = requestCloseAll;
        it = this.activeCameras.iterator();
        while (it.hasNext()) {
            activeCamera = (ActiveCamera) it.next();
            c01001.L$0 = requestCloseAll2;
            c01001.L$1 = it;
            c01001.label = 2;
            if (activeCamera.awaitClosed(c01001) == coroutine_suspended) {
                return coroutine_suspended;
            }
        }
        this.activeCameras.clear();
        CompletableDeferred deferred2 = requestCloseAll2.getDeferred();
        Unit unit2 = Unit.INSTANCE;
        deferred2.complete(unit2);
        return unit2;
    }

    /* JADX INFO: Access modifiers changed from: private */
    /* JADX WARN: Code duplicated, block: B:19:0x0068  */
    /* JADX WARN: Code duplicated, block: B:24:0x007f  */
    /* JADX WARN: Code duplicated, block: B:26:0x0092  */
    /* JADX WARN: Code duplicated, block: B:27:0x0094  */
    /* JADX WARN: Code duplicated, block: B:65:0x0078 A[SYNTHETIC] */
    /* JADX WARN: Code duplicated, block: B:67:? A[LOOP:0: B:17:0x0062->B:67:?, LOOP_END, SYNTHETIC] */
    /* JADX WARN: Code duplicated, block: B:7:0x0013  */
    /* JADX WARN: Multi-variable type inference failed */
    /* JADX WARN: Unsupported multi-entry loop pattern (BACK_EDGE: B:27:0x0094 -> B:28:0x0096). Please report as a decompilation issue!!! */
    /*  JADX ERROR: JadxOverflowException in pass: RegionMakerVisitor
        jadx.core.utils.exceptions.JadxOverflowException: Regions stack size limit reached
        	at jadx.core.utils.ErrorsCounter.addError(ErrorsCounter.java:59)
        	at jadx.core.utils.ErrorsCounter.error(ErrorsCounter.java:31)
        	at jadx.core.dex.attributes.nodes.NotificationAttrNode.addError(NotificationAttrNode.java:19)
        */
    /* JADX INFO: renamed from: retrieveActiveCamera-RzXb1QE, reason: not valid java name */
    public final java.lang.Object m487retrieveActiveCameraRzXb1QE(java.lang.String r13, androidx.camera.camera2.pipe.compat.RequestOpen r14, kotlin.coroutines.Continuation r15) throws java.lang.Throwable {
        /*
            Method dump skipped, instruction units count: 383
            To view this dump add '--comments-level debug' option
        */
        throw new UnsupportedOperationException("Method not decompiled: androidx.camera.camera2.pipe.compat.PruningCamera2DeviceManager.m487retrieveActiveCameraRzXb1QE(java.lang.String, androidx.camera.camera2.pipe.compat.RequestOpen, kotlin.coroutines.Continuation):java.lang.Object");
    }

    /* JADX INFO: Access modifiers changed from: private */
    /* JADX WARN: Code duplicated, block: B:7:0x0013  */
    /* JADX INFO: renamed from: openCameraWithRetry-zDSwpeU, reason: not valid java name */
    public final Object m486openCameraWithRetryzDSwpeU(String str, List list, Function1 function1, CoroutineScope coroutineScope, Continuation continuation) {
        PruningCamera2DeviceManager$openCameraWithRetry$1 pruningCamera2DeviceManager$openCameraWithRetry$1;
        if (continuation instanceof PruningCamera2DeviceManager$openCameraWithRetry$1) {
            pruningCamera2DeviceManager$openCameraWithRetry$1 = (PruningCamera2DeviceManager$openCameraWithRetry$1) continuation;
            int i = pruningCamera2DeviceManager$openCameraWithRetry$1.label;
            if ((i & Integer.MIN_VALUE) != 0) {
                pruningCamera2DeviceManager$openCameraWithRetry$1.label = i - Integer.MIN_VALUE;
            } else {
                pruningCamera2DeviceManager$openCameraWithRetry$1 = new PruningCamera2DeviceManager$openCameraWithRetry$1(this, continuation);
            }
        } else {
            pruningCamera2DeviceManager$openCameraWithRetry$1 = new PruningCamera2DeviceManager$openCameraWithRetry$1(this, continuation);
        }
        Object objMo492openCameraWithRetryaeCOTgg = pruningCamera2DeviceManager$openCameraWithRetry$1.result;
        Object coroutine_suspended = IntrinsicsKt.getCOROUTINE_SUSPENDED();
        int i2 = pruningCamera2DeviceManager$openCameraWithRetry$1.label;
        if (i2 == 0) {
            ResultKt.throwOnFailure(objMo492openCameraWithRetryaeCOTgg);
            if (Log.INSTANCE.getDEBUG_LOGGABLE()) {
                android.util.Log.d("CXCP", "Opening " + ((Object) CameraId.m238toStringimpl(str)) + " with retries...");
            }
            RetryingCameraStateOpener retryingCameraStateOpener = this.retryingCameraStateOpener;
            Camera2DeviceCloser camera2DeviceCloser = this.camera2DeviceCloser;
            pruningCamera2DeviceManager$openCameraWithRetry$1.L$0 = str;
            pruningCamera2DeviceManager$openCameraWithRetry$1.L$1 = list;
            pruningCamera2DeviceManager$openCameraWithRetry$1.L$2 = coroutineScope;
            pruningCamera2DeviceManager$openCameraWithRetry$1.label = 1;
            objMo492openCameraWithRetryaeCOTgg = retryingCameraStateOpener.mo492openCameraWithRetryaeCOTgg(str, camera2DeviceCloser, function1, pruningCamera2DeviceManager$openCameraWithRetry$1);
            if (objMo492openCameraWithRetryaeCOTgg == coroutine_suspended) {
                return coroutine_suspended;
            }
        } else {
            if (i2 != 1) {
                throw new IllegalStateException("call to 'resume' before 'invoke' with coroutine");
            }
            coroutineScope = (CoroutineScope) pruningCamera2DeviceManager$openCameraWithRetry$1.L$2;
            list = (List) pruningCamera2DeviceManager$openCameraWithRetry$1.L$1;
            str = (String) pruningCamera2DeviceManager$openCameraWithRetry$1.L$0;
            ResultKt.throwOnFailure(objMo492openCameraWithRetryaeCOTgg);
        }
        OpenCameraResult openCameraResult = (OpenCameraResult) objMo492openCameraWithRetryaeCOTgg;
        if (openCameraResult.getCameraState() == null) {
            return new OpenVirtualCameraResult.Error(openCameraResult.m481getErrorCodemVEW8x0(), null);
        }
        return new OpenVirtualCameraResult.Success(new ActiveCamera(openCameraResult.getCameraState(), CollectionsKt.toSet(CollectionsKt.plus(list, CameraId.m233boximpl(str))), coroutineScope, new Function1() { // from class: androidx.camera.camera2.pipe.compat.PruningCamera2DeviceManager$$ExternalSyntheticLambda2
            @Override // kotlin.jvm.functions.Function1
            public final Object invoke(Object obj) {
                return PruningCamera2DeviceManager.openCameraWithRetry_zDSwpeU$lambda$1(this.f$0, (ActiveCamera) obj);
            }
        }));
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static final Unit openCameraWithRetry_zDSwpeU$lambda$1(PruningCamera2DeviceManager pruningCamera2DeviceManager, ActiveCamera activeCamera) {
        Intrinsics.checkNotNullParameter(activeCamera, "activeCamera");
        pruningCamera2DeviceManager.queue.tryEmit(new RequestClose(activeCamera));
        return Unit.INSTANCE;
    }

    /* JADX INFO: Access modifiers changed from: private */
    /* JADX WARN: Code duplicated, block: B:23:0x007c  */
    /* JADX WARN: Code duplicated, block: B:25:0x00a8  */
    /* JADX WARN: Code duplicated, block: B:28:0x00b2  */
    /* JADX WARN: Code duplicated, block: B:31:0x00bc  */
    /* JADX WARN: Code duplicated, block: B:33:0x00ce  */
    /* JADX WARN: Code duplicated, block: B:35:0x00d4  */
    /* JADX WARN: Code duplicated, block: B:38:0x00de  */
    /* JADX WARN: Code duplicated, block: B:45:0x010f A[RETURN] */
    /* JADX WARN: Code duplicated, block: B:50:0x00ef A[SYNTHETIC] */
    /* JADX WARN: Code duplicated, block: B:7:0x0013  */
    /* JADX WARN: Unsupported multi-entry loop pattern (BACK_EDGE: B:44:0x010d -> B:46:0x0110). Please report as a decompilation issue!!! */
    /*  JADX ERROR: JadxOverflowException in pass: RegionMakerVisitor
        jadx.core.utils.exceptions.JadxOverflowException: Regions count limit reached at block B:31:0x00bc
        	at jadx.core.utils.ErrorsCounter.addError(ErrorsCounter.java:59)
        	at jadx.core.utils.ErrorsCounter.error(ErrorsCounter.java:31)
        	at jadx.core.dex.attributes.nodes.NotificationAttrNode.addError(NotificationAttrNode.java:19)
        */
    public final java.lang.Object connectPendingRequestOpens(java.util.Set r9, kotlin.coroutines.Continuation r10) {
        /*
            Method dump skipped, instruction units count: 282
            To view this dump add '--comments-level debug' option
        */
        throw new UnsupportedOperationException("Method not decompiled: androidx.camera.camera2.pipe.compat.PruningCamera2DeviceManager.connectPendingRequestOpens(java.util.Set, kotlin.coroutines.Continuation):java.lang.Object");
    }

    private final Object disconnectPendingRequestOpens(List list, Continuation continuation) {
        Iterator it = list.iterator();
        while (it.hasNext()) {
            PendingRequestOpen pendingRequestOpen = (PendingRequestOpen) it.next();
            pendingRequestOpen.getToken().release();
            this.pendingRequestOpens.remove(pendingRequestOpen);
        }
        return Unit.INSTANCE;
    }

    private final List removeIndices(List list, Set set) {
        ArrayList arrayList = new ArrayList();
        Iterator it = CollectionsKt.sorted(set).iterator();
        while (it.hasNext()) {
            arrayList.add(list.remove(((Number) it.next()).intValue() - arrayList.size()));
        }
        return arrayList;
    }

    private interface RetrieveActiveCameraResult {

        public static final class Success implements RetrieveActiveCameraResult {
            private final ActiveCamera activeCamera;
            private final Token token;

            public boolean equals(Object obj) {
                if (this == obj) {
                    return true;
                }
                if (!(obj instanceof Success)) {
                    return false;
                }
                Success success = (Success) obj;
                return Intrinsics.areEqual(this.activeCamera, success.activeCamera) && Intrinsics.areEqual(this.token, success.token);
            }

            public int hashCode() {
                return (this.activeCamera.hashCode() * 31) + this.token.hashCode();
            }

            public String toString() {
                return "Success(activeCamera=" + this.activeCamera + ", token=" + this.token + ')';
            }

            public Success(ActiveCamera activeCamera, Token token) {
                Intrinsics.checkNotNullParameter(activeCamera, "activeCamera");
                Intrinsics.checkNotNullParameter(token, "token");
                this.activeCamera = activeCamera;
                this.token = token;
            }

            public final ActiveCamera getActiveCamera() {
                return this.activeCamera;
            }

            public final Token getToken() {
                return this.token;
            }
        }

        public static final class Error implements RetrieveActiveCameraResult {
            private final CameraError lastCameraError;

            public /* synthetic */ Error(CameraError cameraError, DefaultConstructorMarker defaultConstructorMarker) {
                this(cameraError);
            }

            public boolean equals(Object obj) {
                if (this == obj) {
                    return true;
                }
                return (obj instanceof Error) && Intrinsics.areEqual(this.lastCameraError, ((Error) obj).lastCameraError);
            }

            public int hashCode() {
                CameraError cameraError = this.lastCameraError;
                if (cameraError == null) {
                    return 0;
                }
                return CameraError.m185hashCodeimpl(cameraError.m188unboximpl());
            }

            public String toString() {
                return "Error(lastCameraError=" + this.lastCameraError + ')';
            }

            private Error(CameraError cameraError) {
                this.lastCameraError = cameraError;
            }

            /* JADX INFO: renamed from: getLastCameraError-mVEW8x0, reason: not valid java name */
            public final CameraError m489getLastCameraErrormVEW8x0() {
                return this.lastCameraError;
            }
        }
    }

    private interface OpenVirtualCameraResult {

        public static final class Success implements OpenVirtualCameraResult {
            private final ActiveCamera activeCamera;

            public boolean equals(Object obj) {
                if (this == obj) {
                    return true;
                }
                return (obj instanceof Success) && Intrinsics.areEqual(this.activeCamera, ((Success) obj).activeCamera);
            }

            public int hashCode() {
                return this.activeCamera.hashCode();
            }

            public String toString() {
                return "Success(activeCamera=" + this.activeCamera + ')';
            }

            public Success(ActiveCamera activeCamera) {
                Intrinsics.checkNotNullParameter(activeCamera, "activeCamera");
                this.activeCamera = activeCamera;
            }

            public final ActiveCamera getActiveCamera() {
                return this.activeCamera;
            }
        }

        public static final class Error implements OpenVirtualCameraResult {
            private final CameraError lastCameraError;

            public /* synthetic */ Error(CameraError cameraError, DefaultConstructorMarker defaultConstructorMarker) {
                this(cameraError);
            }

            public boolean equals(Object obj) {
                if (this == obj) {
                    return true;
                }
                return (obj instanceof Error) && Intrinsics.areEqual(this.lastCameraError, ((Error) obj).lastCameraError);
            }

            public int hashCode() {
                CameraError cameraError = this.lastCameraError;
                if (cameraError == null) {
                    return 0;
                }
                return CameraError.m185hashCodeimpl(cameraError.m188unboximpl());
            }

            public String toString() {
                return "Error(lastCameraError=" + this.lastCameraError + ')';
            }

            private Error(CameraError cameraError) {
                this.lastCameraError = cameraError;
            }

            /* JADX INFO: renamed from: getLastCameraError-mVEW8x0, reason: not valid java name */
            public final CameraError m488getLastCameraErrormVEW8x0() {
                return this.lastCameraError;
            }
        }
    }
}
