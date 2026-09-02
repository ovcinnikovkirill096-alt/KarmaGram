package com.yandex.mapkit.user_location;

import com.yandex.mapkit.layers.ObjectEvent;

public interface UserLocationIconChanged extends ObjectEvent {
    UserLocationIconType getIconType();
}
