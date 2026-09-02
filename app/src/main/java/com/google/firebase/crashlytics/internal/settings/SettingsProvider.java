package com.google.firebase.crashlytics.internal.settings;

import com.google.android.gms.tasks.Task;

public interface SettingsProvider {
    Task getSettingsAsync();

    Settings getSettingsSync();
}
