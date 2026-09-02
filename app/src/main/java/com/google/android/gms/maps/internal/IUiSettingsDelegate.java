package com.google.android.gms.maps.internal;

import android.os.IInterface;

public interface IUiSettingsDelegate extends IInterface {
    void setCompassEnabled(boolean z);

    void setMyLocationButtonEnabled(boolean z);

    void setZoomControlsEnabled(boolean z);
}
