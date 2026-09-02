package com.google.android.play.core.integrity;

import android.app.PendingIntent;

final class ah extends IntegrityTokenResponse {
    private final String a;
    private final u b;

    ah(String str, com.google.android.play.integrity.internal.q qVar, PendingIntent pendingIntent) {
        this.a = str;
        this.b = new u(qVar, pendingIntent);
    }

    @Override // com.google.android.play.core.integrity.IntegrityTokenResponse
    public final String token() {
        return this.a;
    }
}
