package androidx.camera.core.impl;

import androidx.camera.core.RetryPolicy;

public interface RetryPolicyInternal extends RetryPolicy {
    RetryPolicy copy(long j);
}
