package com.google.android.gms.common.internal;

import com.google.android.gms.tasks.Task;

public interface TelemetryLoggingClient {
    Task log(TelemetryData telemetryData);
}
