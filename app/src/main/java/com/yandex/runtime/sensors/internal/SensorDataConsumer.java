package com.yandex.runtime.sensors.internal;

import android.hardware.SensorEvent;

public interface SensorDataConsumer {
    void consume(SensorEvent sensorEvent);

    void sensorUnavailable();
}
