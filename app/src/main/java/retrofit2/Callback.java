package retrofit2;

public interface Callback {
    void onFailure(Call call, Throwable th);

    void onResponse(Call call, Response response);
}
