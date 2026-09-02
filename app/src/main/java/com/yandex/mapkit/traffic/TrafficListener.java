package com.yandex.mapkit.traffic;

public interface TrafficListener {
    void onTrafficChanged(TrafficLevel trafficLevel);

    void onTrafficExpired();

    void onTrafficLoading();
}
