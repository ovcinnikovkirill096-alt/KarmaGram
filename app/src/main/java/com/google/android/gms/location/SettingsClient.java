package com.google.android.gms.location;

import com.google.android.gms.tasks.Task;

public interface SettingsClient {
    Task checkLocationSettings(LocationSettingsRequest locationSettingsRequest);
}
