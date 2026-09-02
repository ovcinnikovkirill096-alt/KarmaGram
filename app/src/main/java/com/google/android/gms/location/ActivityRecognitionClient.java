package com.google.android.gms.location;

import android.app.PendingIntent;
import com.google.android.gms.tasks.Task;

public interface ActivityRecognitionClient {
    Task removeActivityUpdates(PendingIntent pendingIntent);

    Task requestActivityUpdates(long j, PendingIntent pendingIntent);
}
