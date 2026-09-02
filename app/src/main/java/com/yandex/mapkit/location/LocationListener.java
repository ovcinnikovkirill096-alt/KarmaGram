package com.yandex.mapkit.location;

public interface LocationListener {
    void onLocationStatusUpdated(LocationStatus locationStatus);

    void onLocationUpdated(Location location);
}
