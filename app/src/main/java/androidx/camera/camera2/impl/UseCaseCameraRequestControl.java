package androidx.camera.camera2.impl;

import androidx.camera.camera2.pipe.AeMode;
import androidx.camera.camera2.pipe.Lock3ABehavior;
import androidx.camera.core.impl.Config;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import kotlin.coroutines.Continuation;
import kotlin.enums.EnumEntries;
import kotlin.enums.EnumEntriesKt;
import kotlinx.coroutines.Deferred;

public interface UseCaseCameraRequestControl {

    public enum Type {
        SESSION_CONFIG,
        DEFAULT,
        CAMERA2_CAMERA_CONTROL;

        private static final /* synthetic */ EnumEntries $ENTRIES = EnumEntriesKt.enumEntries(values());

        public static EnumEntries getEntries() {
            return $ENTRIES;
        }
    }

    Object awaitSurfaceSetup(Continuation continuation);

    Deferred cancelFocusAndMeteringAsync();

    void close();

    List issueSingleCaptureAsync(List list, int i, int i2, int i3);

    Deferred removeParametersAsync(List list, Type type);

    Deferred setParametersAsync(Map map, Type type, Config.OptionPriority optionPriority);

    /* JADX INFO: renamed from: setTorchOffAsync-MtizInI */
    Deferred mo75setTorchOffAsyncMtizInI(int i);

    Deferred setTorchOnAsync();

    /* JADX INFO: renamed from: startFocusAndMeteringAsync-NxRnBj4 */
    Deferred mo76startFocusAndMeteringAsyncNxRnBj4(List list, List list2, List list3, Lock3ABehavior lock3ABehavior, Lock3ABehavior lock3ABehavior2, Lock3ABehavior lock3ABehavior3, AeMode aeMode, long j);

    Deferred submitParameters(Map map, Type type, Config.OptionPriority optionPriority);

    Deferred update3aRegions(List list, List list2, List list3);

    Deferred updateCamera2ConfigAsync(Config config, Map map);

    Deferred updateRepeatingRequestAsync(boolean z, Collection collection);

    /* JADX INFO: renamed from: androidx.camera.camera2.impl.UseCaseCameraRequestControl$-CC, reason: invalid class name */
    public abstract /* synthetic */ class CC {
        public static /* synthetic */ Deferred setParametersAsync$default(UseCaseCameraRequestControl useCaseCameraRequestControl, Map map, Type type, Config.OptionPriority optionPriority, int i, Object obj) {
            if (obj != null) {
                throw new UnsupportedOperationException("Super calls with default arguments not supported in this target, function: setParametersAsync");
            }
            if ((i & 2) != 0) {
                type = Type.DEFAULT;
            }
            if ((i & 4) != 0) {
                optionPriority = UseCaseCameraKt.getDefaultOptionPriority();
            }
            return useCaseCameraRequestControl.setParametersAsync(map, type, optionPriority);
        }

        public static /* synthetic */ Deferred submitParameters$default(UseCaseCameraRequestControl useCaseCameraRequestControl, Map map, Type type, Config.OptionPriority optionPriority, int i, Object obj) {
            if (obj != null) {
                throw new UnsupportedOperationException("Super calls with default arguments not supported in this target, function: submitParameters");
            }
            if ((i & 2) != 0) {
                type = Type.DEFAULT;
            }
            if ((i & 4) != 0) {
                optionPriority = UseCaseCameraKt.getDefaultOptionPriority();
            }
            return useCaseCameraRequestControl.submitParameters(map, type, optionPriority);
        }

        public static /* synthetic */ Deferred removeParametersAsync$default(UseCaseCameraRequestControl useCaseCameraRequestControl, List list, Type type, int i, Object obj) {
            if (obj != null) {
                throw new UnsupportedOperationException("Super calls with default arguments not supported in this target, function: removeParametersAsync");
            }
            if ((i & 2) != 0) {
                type = Type.DEFAULT;
            }
            return useCaseCameraRequestControl.removeParametersAsync(list, type);
        }

        /* JADX INFO: renamed from: startFocusAndMeteringAsync-NxRnBj4$default, reason: not valid java name */
        public static /* synthetic */ Deferred m102startFocusAndMeteringAsyncNxRnBj4$default(UseCaseCameraRequestControl useCaseCameraRequestControl, List list, List list2, List list3, Lock3ABehavior lock3ABehavior, Lock3ABehavior lock3ABehavior2, Lock3ABehavior lock3ABehavior3, AeMode aeMode, long j, int i, Object obj) {
            if (obj != null) {
                throw new UnsupportedOperationException("Super calls with default arguments not supported in this target, function: startFocusAndMeteringAsync-NxRnBj4");
            }
            if ((i & 1) != 0) {
                list = null;
            }
            if ((i & 2) != 0) {
                list2 = null;
            }
            if ((i & 4) != 0) {
                list3 = null;
            }
            if ((i & 8) != 0) {
                lock3ABehavior = null;
            }
            if ((i & 16) != 0) {
                lock3ABehavior2 = null;
            }
            if ((i & 32) != 0) {
                lock3ABehavior3 = null;
            }
            if ((i & 64) != 0) {
                aeMode = null;
            }
            if ((i & 128) != 0) {
                j = 3000000000L;
            }
            return useCaseCameraRequestControl.mo76startFocusAndMeteringAsyncNxRnBj4(list, list2, list3, lock3ABehavior, lock3ABehavior2, lock3ABehavior3, aeMode, j);
        }
    }
}
