package com.yandex.runtime.attestation_storage.internal;

public interface AttestationListener {
    void onAttestationFailed(String str);

    void onAttestationReceived(byte[] bArr);
}
