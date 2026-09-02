package com.google.android.play.core.integrity;

import android.content.Context;

final class v {
    private static o a;

    static synchronized o a(Context context) {
        try {
            if (a == null) {
                m mVar = new m(null);
                mVar.a(com.google.android.play.integrity.internal.ae.a(context));
                a = mVar.b();
            }
        } catch (Throwable th) {
            throw th;
        }
        return a;
    }
}
