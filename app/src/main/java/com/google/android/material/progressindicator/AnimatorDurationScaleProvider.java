package com.google.android.material.progressindicator;

import android.content.ContentResolver;
import android.provider.Settings;

public class AnimatorDurationScaleProvider {
    public float getSystemAnimatorDurationScale(ContentResolver contentResolver) {
        return Settings.Global.getFloat(contentResolver, "animator_duration_scale", 1.0f);
    }
}
