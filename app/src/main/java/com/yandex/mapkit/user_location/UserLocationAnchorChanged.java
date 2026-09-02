package com.yandex.mapkit.user_location;

import com.yandex.mapkit.layers.ObjectEvent;

public interface UserLocationAnchorChanged extends ObjectEvent {
    UserLocationAnchorType getAnchorType();
}
