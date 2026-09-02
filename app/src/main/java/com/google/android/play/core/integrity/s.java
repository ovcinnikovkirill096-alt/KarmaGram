package com.google.android.play.core.integrity;

import android.content.Context;

final class s {
    private final s a = this;
    private final com.google.android.play.integrity.internal.al b;
    private final com.google.android.play.integrity.internal.al c;
    private final com.google.android.play.integrity.internal.al d;
    private final com.google.android.play.integrity.internal.al e;
    private final com.google.android.play.integrity.internal.al f;

    /* synthetic */ s(Context context, r rVar) {
        com.google.android.play.integrity.internal.ai aiVarB = com.google.android.play.integrity.internal.aj.b(context);
        this.b = aiVarB;
        com.google.android.play.integrity.internal.al alVarB = com.google.android.play.integrity.internal.ah.b(an.a);
        this.c = alVarB;
        com.google.android.play.integrity.internal.al alVarB2 = com.google.android.play.integrity.internal.ah.b(new az(aiVarB, alVarB));
        this.d = alVarB2;
        com.google.android.play.integrity.internal.al alVarB3 = com.google.android.play.integrity.internal.ah.b(new be(alVarB2));
        this.e = alVarB3;
        this.f = com.google.android.play.integrity.internal.ah.b(new am(alVarB2, alVarB3));
    }

    public final StandardIntegrityManager a() {
        return (StandardIntegrityManager) this.f.a();
    }
}
