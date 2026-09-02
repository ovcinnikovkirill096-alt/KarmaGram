package com.google.android.play.core.integrity;

import com.google.android.gms.tasks.Task;

final class w implements IntegrityManager {
    private final ad a;

    w(ad adVar) {
        this.a = adVar;
    }

    @Override // com.google.android.play.core.integrity.IntegrityManager
    public final Task<IntegrityTokenResponse> requestIntegrityToken(IntegrityTokenRequest integrityTokenRequest) {
        return this.a.b(integrityTokenRequest);
    }
}
