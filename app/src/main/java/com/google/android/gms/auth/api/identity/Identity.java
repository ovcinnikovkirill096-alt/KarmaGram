package com.google.android.gms.auth.api.identity;

import android.content.Context;
import com.google.android.gms.common.internal.Preconditions;
import com.google.android.gms.internal.p000authapi.zbaq;

public abstract class Identity {
    public static SignInClient getSignInClient(Context context) {
        return new zbaq((Context) Preconditions.checkNotNull(context), new zbv());
    }
}
