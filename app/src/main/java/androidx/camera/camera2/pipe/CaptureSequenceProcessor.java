package androidx.camera.camera2.pipe;

import java.util.List;
import java.util.Map;
import kotlin.coroutines.Continuation;

public interface CaptureSequenceProcessor {
    void abortCaptures();

    CaptureSequence build(boolean z, List list, Map map, Map map2, Map map3, CaptureSequence.CaptureSequenceListener captureSequenceListener, List list2);

    Object shutdown(Continuation continuation);

    void stopRepeating();

    Integer submit(CaptureSequence captureSequence);
}
