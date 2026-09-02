package androidx.camera.camera2.pipe;

import java.util.Iterator;
import java.util.List;

public interface StreamGraph {
    CameraStream get(CameraStream.Config config);

    /* JADX INFO: renamed from: get-aKI5c8E, reason: not valid java name */
    CameraStream mo413getaKI5c8E(int i);

    /* JADX INFO: renamed from: get-iYJqvbA, reason: not valid java name */
    OutputStream mo414getiYJqvbA(int i);

    List getInputs();

    List getOutputs();

    List getStreams();

    /* JADX INFO: renamed from: androidx.camera.camera2.pipe.StreamGraph$-CC, reason: invalid class name */
    public abstract /* synthetic */ class CC {
        /* JADX INFO: renamed from: $default$get-aKI5c8E, reason: not valid java name */
        public static CameraStream m415$default$getaKI5c8E(StreamGraph streamGraph, int i) {
            Object next;
            Iterator it = streamGraph.getStreams().iterator();
            while (it.hasNext()) {
                next = it.next();
                if (StreamId.m420equalsimpl0(((CameraStream) next).m247getIdptHMqGs(), i)) {
                    return (CameraStream) next;
                }
            }
            next = null;
            return (CameraStream) next;
        }

        /* JADX INFO: renamed from: $default$get-iYJqvbA, reason: not valid java name */
        public static OutputStream m416$default$getiYJqvbA(StreamGraph streamGraph, int i) {
            Object next;
            Iterator it = streamGraph.getOutputs().iterator();
            while (it.hasNext()) {
                next = it.next();
                if (OutputId.m299equalsimpl0(((OutputStream) next).mo317getId4LaLFng(), i)) {
                    return (OutputStream) next;
                }
            }
            next = null;
            return (OutputStream) next;
        }
    }
}
