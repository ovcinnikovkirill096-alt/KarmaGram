package androidx.camera.core.internal.utils;

public interface RingBuffer {

    public interface OnRemoveCallback {
        void onRemove(Object obj);
    }

    Object dequeue();

    boolean isEmpty();
}
