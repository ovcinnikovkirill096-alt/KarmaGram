package com.google.android.gms.auth.api.signin.internal;

import android.os.IInterface;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.common.api.Status;

public interface zbr extends IInterface {
    void zbb(Status status);

    void zbc(Status status);

    void zbd(GoogleSignInAccount googleSignInAccount, Status status);
}
