package com.google.android.material.internal;

import android.graphics.drawable.Drawable;

@Deprecated
public interface ViewOverlayImpl {
    void add(Drawable drawable);

    void remove(Drawable drawable);
}
