package com.yandex.runtime.graphics_state;

public class GraphicsViewState {
    public static native void logViewCreateAttempt(GraphicsViewType graphicsViewType);

    public static native boolean viewCreateAttempted(GraphicsViewType graphicsViewType);
}
