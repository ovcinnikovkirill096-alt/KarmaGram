package com.yandex.mapkit;

import com.yandex.runtime.bindings.Archive;
import com.yandex.runtime.bindings.Serializable;

public class ScreenPoint implements Serializable {
    private float x;
    private float y;

    public ScreenPoint(float f, float f2) {
        this.x = f;
        this.y = f2;
    }

    public ScreenPoint() {
    }

    public float getX() {
        return this.x;
    }

    public float getY() {
        return this.y;
    }

    @Override // com.yandex.runtime.bindings.Serializable
    public void serialize(Archive archive) {
        this.x = archive.add(this.x);
        this.y = archive.add(this.y);
    }
}
