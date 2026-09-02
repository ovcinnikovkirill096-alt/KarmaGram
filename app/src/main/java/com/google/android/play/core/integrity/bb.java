package com.google.android.play.core.integrity;

import android.app.PendingIntent;

final class bb extends StandardIntegrityManager.StandardIntegrityToken {
    private final String a;
    private final u b;

    bb(String str, com.google.android.play.integrity.internal.q qVar, PendingIntent pendingIntent) {
        this.a = str;
        this.b = new u(qVar, pendingIntent);
    }

    @Override // com.google.android.play.core.integrity.StandardIntegrityManager.StandardIntegrityToken
    public final String token() {
        return this.a;
    }
}
