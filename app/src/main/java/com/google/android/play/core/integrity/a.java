package com.google.android.play.core.integrity;

import android.app.PendingIntent;

final class a extends ag {
    private String a;
    private com.google.android.play.integrity.internal.q b;
    private PendingIntent c;

    a() {
    }

    @Override // com.google.android.play.core.integrity.ag
    final ag a(PendingIntent pendingIntent) {
        this.c = pendingIntent;
        return this;
    }

    @Override // com.google.android.play.core.integrity.ag
    final ag b(com.google.android.play.integrity.internal.q qVar) {
        if (qVar == null) {
            throw new NullPointerException("Null logger");
        }
        this.b = qVar;
        return this;
    }

    @Override // com.google.android.play.core.integrity.ag
    final ag c(String str) {
        this.a = str;
        return this;
    }

    @Override // com.google.android.play.core.integrity.ag
    final ah d() {
        com.google.android.play.integrity.internal.q qVar;
        String str = this.a;
        if (str != null && (qVar = this.b) != null) {
            return new ah(str, qVar, this.c);
        }
        StringBuilder sb = new StringBuilder();
        if (this.a == null) {
            sb.append(" token");
        }
        if (this.b == null) {
            sb.append(" logger");
        }
        throw new IllegalStateException("Missing required properties:".concat(sb.toString()));
    }
}
