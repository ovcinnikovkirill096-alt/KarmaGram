package com.yandex.runtime.bindings;

import android.graphics.PointF;

public class PointHandler implements ArchivingHandler<PointF> {
    private final boolean isOptional;

    public PointHandler() {
        this(false);
    }

    public PointHandler(boolean z) {
        this.isOptional = z;
    }

    @Override // com.yandex.runtime.bindings.ArchivingHandler
    public PointF add(PointF pointF, Archive archive) {
        return archive.add(pointF, this.isOptional);
    }
}
