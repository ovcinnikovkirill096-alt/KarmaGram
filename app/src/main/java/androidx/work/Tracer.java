package androidx.work;

public interface Tracer {
    void beginAsyncSection(String str, int i);

    void beginSection(String str);

    void endAsyncSection(String str, int i);

    void endSection();

    boolean isEnabled();
}
