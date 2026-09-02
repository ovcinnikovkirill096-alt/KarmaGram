package com.google.android.gms.auth.blockstore.restorecredential;

import com.google.android.gms.tasks.Task;

public interface RestoreCredentialClient {
    Task getRestoreCredential(GetRestoreCredentialRequest getRestoreCredentialRequest);
}
