package okhttp3.internal.platform;

import android.content.Context;

public interface ContextAwarePlatform {
    Context getApplicationContext();

    void setApplicationContext(Context context);
}
