package org.telegram.messenger;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import com.radolyn.ayugram.AyuConstants;

public class RefererReceiver extends BroadcastReceiver {
    @Override // android.content.BroadcastReceiver
    public void onReceive(Context context, Intent intent) {
        try {
            MessagesController.getInstance(UserConfig.selectedAccount).setReferer(AyuConstants.BUILD_STORE_PACKAGE);
        } catch (Exception unused) {
        }
    }
}
