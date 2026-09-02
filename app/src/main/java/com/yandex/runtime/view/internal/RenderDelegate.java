package com.yandex.runtime.view.internal;

public interface RenderDelegate {
    void requestRender();

    void setForceRender(boolean z);
}
