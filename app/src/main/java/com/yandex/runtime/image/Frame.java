package com.yandex.runtime.image;

public class Frame {
    private final long duration;
    private final ImageProvider image;

    public Frame(ImageProvider imageProvider, long j) {
        this.image = imageProvider;
        this.duration = j;
    }

    public ImageProvider getImage() {
        return this.image;
    }

    public long getDuration() {
        return this.duration;
    }
}
