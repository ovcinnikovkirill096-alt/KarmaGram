package com.google.android.gms.location;

import android.os.Looper;
import com.google.android.gms.tasks.Task;

public interface FusedLocationProviderClient {
    Task getLastLocation();

    Task removeLocationUpdates(LocationCallback locationCallback);

    Task requestLocationUpdates(LocationRequest locationRequest, LocationCallback locationCallback, Looper looper);
}
