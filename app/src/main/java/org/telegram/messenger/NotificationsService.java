package org.telegram.messenger;

import android.app.Notification;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Intent;
import android.os.Build;
import android.os.IBinder;
import androidx.core.app.NotificationChannelCompat;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;
import com.radolyn.ayugram.AyuConfig;
import java.util.Random;
import org.telegram.ui.LaunchActivity;

public class NotificationsService extends Service {
    private static final String[] notifications = {"⊂(◉‿◉)つ", "(｡◕‿‿◕｡)", "(｡◕‿◕｡)", "¯\\_(ツ)_/¯", "\\(^-^)/", "＼(＾O＾)／", "\\(ᵔᵕᵔ)/", "ԅ(≖‿≖ԅ)", "(⊃｡•́‿•̀｡)⊃", "•ᴗ•", "(~‾▿‾)~", "｡^‿^｡", "(⁎˃ᆺ˂)", "(≧◡≦)", "\\(★ω★)/", "(✿◠‿◠)", "＼(٥⁀▽⁀ )／", "(*^.^*)", "( ` ω ´ )", "｡ﾟ･ (>﹏<) ･ﾟ｡", "╮(︶▽︶)╭", "(￣～￣;)", "(＾• ω •＾)", "╮( ˘ ､ ˘ )╭", "(´• ω •`)ﾉ", "(￣﹃￣)", "(๑ᵔ⤙ᵔ๑)", "( ￣ー￣)φ__", "(^_<)〜☆", "ヽ(・∀・)ﾉ", "┐(シ)┌", "(＞﹏＜)", "(⌒_⌒;)", "o(≧▽≦)o", "(￣ω￣)", "(=`ω´=)", "(－ω－) zzZ", "(￣▽￣)ノ", "(´｡• ω •｡`)", "(´｡• ᵕ •｡`)"};

    @Override // android.app.Service
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override // android.app.Service
    public int onStartCommand(Intent intent, int i, int i2) {
        return 1;
    }

    @Override // android.app.Service
    public void onCreate() {
        Notification notificationBuild;
        super.onCreate();
        ApplicationLoader.postInitApplication();
        int i = Build.VERSION.SDK_INT;
        if (i < 26 || !AyuConfig.keepAliveService) {
            return;
        }
        NotificationManagerCompat.from(this).createNotificationChannel(new NotificationChannelCompat.Builder("ayugram_push", 3).setName("AyuGram Push Service").setLightsEnabled(false).setVibrationEnabled(false).setSound(null, null).build());
        String[] strArr = notifications;
        String str = strArr[new Random().nextInt(strArr.length)];
        try {
            notificationBuild = new NotificationCompat.Builder(this, "ayugram_push").setContentIntent(PendingIntent.getActivity(this, 0, new Intent(this, (Class<?>) LaunchActivity.class), i >= 34 ? 67108864 : 33554432)).setSmallIcon(R.drawable.msg_premium_badge).setShowWhen(false).setOngoing(true).setContentText(str).setCategory("status").build();
        } catch (Exception unused) {
            notificationBuild = new NotificationCompat.Builder(this, "ayugram_push").setSmallIcon(R.drawable.msg_premium_badge).setShowWhen(false).setOngoing(true).setContentText(str).setCategory("status").build();
        }
        try {
            startForeground(9999, notificationBuild);
        } catch (Throwable unused2) {
        }
    }

    @Override // android.app.Service
    public void onDestroy() {
        super.onDestroy();
        if (AyuConfig.keepAliveService) {
            try {
                stopForeground(true);
            } catch (Throwable unused) {
            }
        }
        if (MessagesController.getGlobalNotificationsSettings().getBoolean("pushService", true)) {
            Intent intent = new Intent("org.telegram.start");
            intent.setPackage(getPackageName());
            try {
                sendBroadcast(intent);
            } catch (Throwable unused2) {
            }
        }
    }

    public void onTimeout(int i, int i2) {
        super.onTimeout(i, i2);
        if (AyuConfig.keepAliveService) {
            try {
                stopSelf();
            } catch (Throwable unused) {
            }
        }
    }
}
