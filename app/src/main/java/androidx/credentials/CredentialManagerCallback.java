package androidx.credentials;

public interface CredentialManagerCallback {
    void onError(Object obj);

    void onResult(Object obj);
}
