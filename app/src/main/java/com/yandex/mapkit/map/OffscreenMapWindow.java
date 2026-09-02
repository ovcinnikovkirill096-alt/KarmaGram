package com.yandex.mapkit.map;

import android.graphics.Bitmap;

public interface OffscreenMapWindow {
    Bitmap captureScreenshot();

    MapWindow getMapWindow();
}
