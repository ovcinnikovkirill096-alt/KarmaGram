package androidx.lifecycle;

public final class AtomicReference {
    private final java.util.concurrent.atomic.AtomicReference base;

    public AtomicReference(Object obj) {
        this.base = new java.util.concurrent.atomic.AtomicReference(obj);
    }
}
