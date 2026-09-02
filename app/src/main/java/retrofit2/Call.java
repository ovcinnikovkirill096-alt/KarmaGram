package retrofit2;

import okhttp3.Request;

public interface Call<T> extends Cloneable {
    void cancel();

    /* JADX INFO: renamed from: clone */
    Call mo19876clone();

    void enqueue(Callback callback);

    boolean isCanceled();

    Request request();
}
