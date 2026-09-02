package androidx.camera.camera2.adapter;

import android.util.Log;
import androidx.camera.camera2.impl.Camera2Logger;
import androidx.camera.camera2.internal.CameraCompatibilityFilter;
import androidx.camera.camera2.pipe.CameraDevices;
import androidx.camera.camera2.pipe.CameraGraph;
import androidx.camera.camera2.pipe.CameraId;
import androidx.camera.camera2.pipe.CameraPipe;
import androidx.camera.core.CameraIdentifier;
import androidx.camera.core.CameraInfo;
import androidx.camera.core.CameraSelector;
import androidx.camera.core.InitializationException;
import androidx.camera.core.Logger;
import androidx.camera.core.concurrent.CameraCoordinator;
import androidx.camera.core.impl.CameraInternal;
import androidx.camera.core.impl.CameraRepository;
import androidx.camera.core.impl.CameraUpdateException;
import androidx.camera.core.impl.InternalCameraPresenceListener;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import kotlin.Pair;
import kotlin.Unit;
import kotlin.collections.CollectionsKt;
import kotlin.collections.MapsKt;
import kotlin.collections.SetsKt;
import kotlin.jvm.internal.Intrinsics;

public final class CameraCoordinatorAdapter implements CameraCoordinator, InternalCameraPresenceListener {
    private List activeConcurrentCameraInfosList;
    private final CameraDevices cameraDevices;
    private CameraPipe cameraPipe;
    private CameraRepository cameraRepository;
    private Map concurrentCameraIdMap;
    private Set concurrentCameraIdsSet;
    private int concurrentMode;
    private boolean concurrentModeOn;
    private final Object lock;
    private List pendingCameraIds;

    public CameraCoordinatorAdapter(CameraPipe cameraPipe, CameraDevices cameraDevices) {
        Intrinsics.checkNotNullParameter(cameraDevices, "cameraDevices");
        this.cameraPipe = cameraPipe;
        this.cameraDevices = cameraDevices;
        this.lock = new Object();
        this.concurrentCameraIdsSet = SetsKt.emptySet();
        this.concurrentCameraIdMap = MapsKt.emptyMap();
        this.activeConcurrentCameraInfosList = CollectionsKt.emptyList();
        this.pendingCameraIds = new ArrayList();
    }

    @Override // androidx.camera.core.concurrent.CameraCoordinator
    public void init(CameraRepository repository) throws CameraUpdateException {
        List listEmptyList;
        Intrinsics.checkNotNullParameter(repository, "repository");
        synchronized (this.lock) {
            this.cameraRepository = repository;
            Unit unit = Unit.INSTANCE;
        }
        List listM177awaitCameraIdsSeavPBo$default = CameraDevices.CC.m177awaitCameraIdsSeavPBo$default(this.cameraDevices, null, 1, null);
        if (listM177awaitCameraIdsSeavPBo$default == null) {
            listEmptyList = CollectionsKt.emptyList();
        } else {
            List list = listM177awaitCameraIdsSeavPBo$default;
            listEmptyList = new ArrayList(CollectionsKt.collectionSizeOrDefault(list, 10));
            Iterator it = list.iterator();
            while (it.hasNext()) {
                listEmptyList.add(((CameraId) it.next()).m239unboximpl());
            }
        }
        onCamerasUpdated(listEmptyList);
    }

    @Override // androidx.camera.core.impl.InternalCameraPresenceListener
    public void onCamerasUpdated(List cameraIds) throws CameraUpdateException {
        Intrinsics.checkNotNullParameter(cameraIds, "cameraIds");
        LinkedHashSet linkedHashSet = new LinkedHashSet();
        LinkedHashMap linkedHashMap = new LinkedHashMap();
        try {
            Set<Set> setM179awaitConcurrentCameraIdsSeavPBo$default = CameraDevices.CC.m179awaitConcurrentCameraIdsSeavPBo$default(this.cameraDevices, null, 1, null);
            if (setM179awaitConcurrentCameraIdsSeavPBo$default == null) {
                setM179awaitConcurrentCameraIdsSeavPBo$default = SetsKt.emptySet();
            }
            for (Set set : setM179awaitConcurrentCameraIdsSeavPBo$default) {
                ArrayList arrayList = new ArrayList(CollectionsKt.collectionSizeOrDefault(set, 10));
                Iterator it = set.iterator();
                while (it.hasNext()) {
                    arrayList.add(((CameraId) it.next()).m239unboximpl());
                }
                Set set2 = CollectionsKt.toSet(arrayList);
                if (cameraIds.containsAll(set2)) {
                    List list = CollectionsKt.toList(set);
                    if (list.size() >= 2) {
                        String strM239unboximpl = ((CameraId) list.get(0)).m239unboximpl();
                        String strM239unboximpl2 = ((CameraId) list.get(1)).m239unboximpl();
                        try {
                            if (CameraCompatibilityFilter.isBackwardCompatible(strM239unboximpl, this.cameraDevices) && CameraCompatibilityFilter.isBackwardCompatible(strM239unboximpl2, this.cameraDevices)) {
                                linkedHashSet.add(set);
                                if (!linkedHashMap.containsKey(strM239unboximpl)) {
                                    linkedHashMap.put(strM239unboximpl, new ArrayList());
                                }
                                Object obj = linkedHashMap.get(strM239unboximpl);
                                Intrinsics.checkNotNull(obj);
                                ((List) obj).add(strM239unboximpl2);
                                if (!linkedHashMap.containsKey(strM239unboximpl2)) {
                                    linkedHashMap.put(strM239unboximpl2, new ArrayList());
                                }
                                Object obj2 = linkedHashMap.get(strM239unboximpl2);
                                Intrinsics.checkNotNull(obj2);
                                ((List) obj2).add(strM239unboximpl);
                            }
                        } catch (InitializationException e) {
                            Camera2Logger camera2Logger = Camera2Logger.INSTANCE;
                            if (Logger.isWarnEnabled("CXCP")) {
                                Log.w(Camera2Logger.TRUNCATED_TAG, "Skipping incompatible concurrent pair: " + set + " due to " + e.getMessage());
                            }
                        }
                    }
                } else {
                    Camera2Logger camera2Logger2 = Camera2Logger.INSTANCE;
                    if (Logger.isWarnEnabled("CXCP")) {
                        Log.w(Camera2Logger.TRUNCATED_TAG, "Failed to retrieve concurrent camera: " + set2 + " from " + cameraIds);
                    }
                }
            }
            synchronized (this.lock) {
                this.concurrentCameraIdsSet = linkedHashSet;
                this.concurrentCameraIdMap = linkedHashMap;
                Unit unit = Unit.INSTANCE;
            }
        } catch (Exception e2) {
            throw new CameraUpdateException("Failed to retrieve concurrent camera id info for camera-pipe.", e2);
        }
    }

    @Override // androidx.camera.core.concurrent.CameraCoordinator
    public List getConcurrentCameraSelectors() {
        List list;
        synchronized (this.lock) {
            try {
                Set<Set> set = this.concurrentCameraIdsSet;
                ArrayList arrayList = new ArrayList(CollectionsKt.collectionSizeOrDefault(set, 10));
                for (Set set2 : set) {
                    ArrayList arrayList2 = new ArrayList(CollectionsKt.collectionSizeOrDefault(set2, 10));
                    Iterator it = set2.iterator();
                    while (it.hasNext()) {
                        arrayList2.add(CameraSelector.of(CameraIdentifier.Factory.create$default(((CameraId) it.next()).m239unboximpl(), null, null, 6, null)));
                    }
                    arrayList.add(CollectionsKt.toList(arrayList2));
                }
                list = CollectionsKt.toList(arrayList);
            } catch (Throwable th) {
                throw th;
            }
        }
        return list;
    }

    @Override // androidx.camera.core.concurrent.CameraCoordinator
    public List getActiveConcurrentCameraInfos() {
        ArrayList arrayList;
        synchronized (this.lock) {
            arrayList = new ArrayList(this.activeConcurrentCameraInfosList);
        }
        return arrayList;
    }

    @Override // androidx.camera.core.concurrent.CameraCoordinator
    public void addPendingCameraInfo(CameraInfo cameraInfo) {
        Intrinsics.checkNotNullParameter(cameraInfo, "cameraInfo");
        synchronized (this.lock) {
            try {
                if (this.concurrentModeOn) {
                    List list = this.pendingCameraIds;
                    String strM13getCameraIdzjxgSG8 = CameraInfoAdapter.Companion.m13getCameraIdzjxgSG8(cameraInfo);
                    CameraId cameraIdM233boximpl = strM13getCameraIdzjxgSG8 != null ? CameraId.m233boximpl(strM13getCameraIdzjxgSG8) : null;
                    if (cameraIdM233boximpl == null) {
                        throw new IllegalStateException("Required value was null.");
                    }
                    list.add(cameraIdM233boximpl.m239unboximpl());
                    tryStartConcurrentGraph();
                }
                Unit unit = Unit.INSTANCE;
            } catch (Throwable th) {
                throw th;
            }
        }
    }

    @Override // androidx.camera.core.concurrent.CameraCoordinator
    public void removePendingCameraInfo(CameraInfo cameraInfo) {
        Intrinsics.checkNotNullParameter(cameraInfo, "cameraInfo");
        synchronized (this.lock) {
            try {
                if (this.concurrentModeOn) {
                    List list = this.pendingCameraIds;
                    String strM13getCameraIdzjxgSG8 = CameraInfoAdapter.Companion.m13getCameraIdzjxgSG8(cameraInfo);
                    CameraId cameraIdM233boximpl = strM13getCameraIdzjxgSG8 != null ? CameraId.m233boximpl(strM13getCameraIdzjxgSG8) : null;
                    if (cameraIdM233boximpl == null) {
                        throw new IllegalStateException("Required value was null.");
                    }
                    list.remove(cameraIdM233boximpl.m239unboximpl());
                }
                Unit unit = Unit.INSTANCE;
            } catch (Throwable th) {
                throw th;
            }
        }
    }

    @Override // androidx.camera.core.concurrent.CameraCoordinator
    public void setActiveConcurrentCameraInfos(List cameraInfos) {
        Intrinsics.checkNotNullParameter(cameraInfos, "cameraInfos");
        synchronized (this.lock) {
            this.activeConcurrentCameraInfosList = cameraInfos;
            tryStartConcurrentGraph();
            Unit unit = Unit.INSTANCE;
        }
    }

    private final void tryStartConcurrentGraph() {
        CameraInternalAdapter cameraInternalAdapter;
        synchronized (this.lock) {
            try {
                if (!this.activeConcurrentCameraInfosList.isEmpty() && !this.pendingCameraIds.isEmpty()) {
                    List list = this.activeConcurrentCameraInfosList;
                    ArrayList arrayList = new ArrayList(CollectionsKt.collectionSizeOrDefault(list, 10));
                    Iterator it = list.iterator();
                    while (true) {
                        if (!it.hasNext()) {
                            if (Intrinsics.areEqual(CollectionsKt.toSet(arrayList), CollectionsKt.toSet(this.pendingCameraIds))) {
                                this.pendingCameraIds.clear();
                                List list2 = this.activeConcurrentCameraInfosList;
                                synchronized (this.lock) {
                                    CameraRepository cameraRepository = this.cameraRepository;
                                    if (cameraRepository == null) {
                                        Camera2Logger camera2Logger = Camera2Logger.INSTANCE;
                                        if (Logger.isErrorEnabled("CXCP")) {
                                            Log.e(Camera2Logger.TRUNCATED_TAG, "Coordinator has not been initialized with a CameraRepository.");
                                        }
                                        return;
                                    }
                                    ArrayList arrayList2 = new ArrayList();
                                    Iterator it2 = list2.iterator();
                                    while (it2.hasNext()) {
                                        try {
                                            String strM13getCameraIdzjxgSG8 = CameraInfoAdapter.Companion.m13getCameraIdzjxgSG8((CameraInfo) it2.next());
                                            Intrinsics.checkNotNull(strM13getCameraIdzjxgSG8);
                                            CameraInternal camera = cameraRepository.getCamera(strM13getCameraIdzjxgSG8);
                                            cameraInternalAdapter = camera instanceof CameraInternalAdapter ? (CameraInternalAdapter) camera : null;
                                        } catch (IllegalArgumentException unused) {
                                        }
                                        if (cameraInternalAdapter != null) {
                                            arrayList2.add(cameraInternalAdapter);
                                        }
                                    }
                                    ArrayList arrayList3 = new ArrayList(CollectionsKt.collectionSizeOrDefault(arrayList2, 10));
                                    int size = arrayList2.size();
                                    int i = 0;
                                    while (i < size) {
                                        Object obj = arrayList2.get(i);
                                        i++;
                                        CameraGraph.Config deferredCameraGraphConfig$camera_camera2 = ((CameraInternalAdapter) obj).getDeferredCameraGraphConfig$camera_camera2();
                                        if (deferredCameraGraphConfig$camera_camera2 == null) {
                                            throw new IllegalStateException("Every CameraInternal instance is expected to have a deferred CameraGraph config");
                                        }
                                        arrayList3.add(deferredCameraGraphConfig$camera_camera2);
                                    }
                                    CameraPipe cameraPipe = this.cameraPipe;
                                    if (cameraPipe == null) {
                                        throw new IllegalStateException("Required value was null.");
                                    }
                                    List listCreateCameraGraphs = cameraPipe.createCameraGraphs(new CameraGraph.ConcurrentConfig(arrayList3));
                                    if (listCreateCameraGraphs.size() != arrayList3.size()) {
                                        throw new IllegalStateException("Check failed.");
                                    }
                                    for (Pair pair : CollectionsKt.zip(arrayList2, listCreateCameraGraphs)) {
                                        ((CameraInternalAdapter) pair.component1()).resumeDeferredCameraGraphCreation$camera_camera2((CameraGraph) pair.component2());
                                    }
                                    return;
                                }
                            }
                            return;
                        }
                        String strM13getCameraIdzjxgSG9 = CameraInfoAdapter.Companion.m13getCameraIdzjxgSG8((CameraInfo) it.next());
                        CameraId cameraIdM233boximpl = strM13getCameraIdzjxgSG9 != null ? CameraId.m233boximpl(strM13getCameraIdzjxgSG9) : null;
                        if (cameraIdM233boximpl == null) {
                            throw new IllegalStateException("Required value was null.");
                        }
                        arrayList.add(cameraIdM233boximpl.m239unboximpl());
                    }
                }
            } catch (Throwable th) {
                throw th;
            }
        }
    }

    @Override // androidx.camera.core.concurrent.CameraCoordinator
    public int getCameraOperatingMode() {
        int i;
        synchronized (this.lock) {
            i = this.concurrentMode;
        }
        return i;
    }

    @Override // androidx.camera.core.concurrent.CameraCoordinator
    public void setCameraOperatingMode(int i) {
        CameraRepository cameraRepository;
        synchronized (this.lock) {
            this.concurrentMode = i;
            cameraRepository = this.cameraRepository;
        }
        if (cameraRepository == null) {
            return;
        }
        boolean z = i == 2;
        this.concurrentModeOn = z;
        if (!z) {
            this.activeConcurrentCameraInfosList = CollectionsKt.emptyList();
        }
        Iterator it = cameraRepository.getCameras().iterator();
        Intrinsics.checkNotNullExpressionValue(it, "iterator(...)");
        while (it.hasNext()) {
            CameraInternal cameraInternal = (CameraInternal) it.next();
            CameraInternalAdapter cameraInternalAdapter = cameraInternal instanceof CameraInternalAdapter ? (CameraInternalAdapter) cameraInternal : null;
            if (cameraInternalAdapter != null) {
                if (i == 1) {
                    cameraInternalAdapter.setCameraGraphCreationMode$camera_camera2(true);
                } else if (i == 2) {
                    cameraInternalAdapter.setCameraGraphCreationMode$camera_camera2(false);
                }
            }
        }
    }

    public void shutdown() {
        this.cameraPipe = null;
        this.concurrentModeOn = false;
        synchronized (this.lock) {
            this.cameraRepository = null;
            this.concurrentCameraIdsSet = SetsKt.emptySet();
            this.concurrentCameraIdMap = MapsKt.emptyMap();
            this.activeConcurrentCameraInfosList = CollectionsKt.emptyList();
            this.concurrentMode = 0;
            this.pendingCameraIds.clear();
            Unit unit = Unit.INSTANCE;
        }
    }
}
