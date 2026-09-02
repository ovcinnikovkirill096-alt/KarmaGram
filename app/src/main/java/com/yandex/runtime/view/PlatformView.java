package com.yandex.runtime.view;

import android.view.View;
import com.yandex.runtime.NativeObject;

public interface PlatformView {
    void destroyNativePlatformView();

    NativeObject getNativePlatformView();

    View getView();

    boolean isDebugModeEnabled();

    void onMemoryWarning();

    void pause();

    void resume();

    void setNoninteractive(boolean z);

    void setOffscreenBufferEnabled(boolean z);

    void start();

    void stop();
}
