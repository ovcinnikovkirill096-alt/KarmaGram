package com.google.android.play.core.integrity;

import android.content.Context;

final class o {
    private final o a = this;
    private final com.google.android.play.integrity.internal.al b;
    private final com.google.android.play.integrity.internal.al c;
    private final com.google.android.play.integrity.internal.al d;
    private final com.google.android.play.integrity.internal.al e;

    /* synthetic */ o(Context context, n nVar) {
        com.google.android.play.integrity.internal.ai aiVarB = com.google.android.play.integrity.internal.aj.b(context);
        this.b = aiVarB;
        com.google.android.play.integrity.internal.al alVarB = com.google.android.play.integrity.internal.ah.b(y.a);
        this.c = alVarB;
        com.google.android.play.integrity.internal.al alVarB2 = com.google.android.play.integrity.internal.ah.b(new af(aiVarB, alVarB));
        this.d = alVarB2;
        this.e = com.google.android.play.integrity.internal.ah.b(new x(alVarB2));
    }

    public final IntegrityManager a() {
        return (IntegrityManager) this.e.a();
    }
}
