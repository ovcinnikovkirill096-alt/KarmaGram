package com.google.android.exoplayer2.video;

import android.graphics.SurfaceTexture;

public interface VideoListener {
    boolean onSurfaceDestroyed(SurfaceTexture surfaceTexture);

    void onSurfaceTextureUpdated(SurfaceTexture surfaceTexture);
}
