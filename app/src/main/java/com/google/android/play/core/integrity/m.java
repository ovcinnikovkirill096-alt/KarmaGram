package com.google.android.play.core.integrity;

import android.content.Context;

final class m implements t {
    private Context a;

    private m() {
    }

    /* synthetic */ m(l lVar) {
    }

    public final m a(Context context) {
        context.getClass();
        this.a = context;
        return this;
    }

    @Override // com.google.android.play.core.integrity.t
    public final o b() {
        com.google.android.play.integrity.internal.ak.a(this.a, Context.class);
        return new o(this.a, null);
    }
}
