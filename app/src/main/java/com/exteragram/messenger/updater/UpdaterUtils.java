package com.exteragram.messenger.updater;

import android.app.Activity;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageInstaller;
import android.net.Uri;
import android.os.Build;
import android.provider.Settings;
import android.text.TextUtils;
import android.util.Log;
import android.widget.LinearLayout;
import android.widget.TextView;
import androidx.arch.core.util.Function;
import androidx.core.app.NotificationChannelCompat;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;
import androidx.core.content.ContextCompat;
import com.exteragram.messenger.utils.AppUtils;
import com.exteragram.messenger.utils.network.RemoteUtils;
import com.radolyn.ayugram.AyuInfra;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;
import okhttp3.internal.url._UrlKt;
import org.telegram.messenger.AndroidUtilities;
import org.telegram.messenger.ApplicationLoader;
import org.telegram.messenger.BuildVars;
import org.telegram.messenger.FileLoader;
import org.telegram.messenger.FileLog;
import org.telegram.messenger.LocaleController;
import org.telegram.messenger.MessageObject;
import org.telegram.messenger.R;
import org.telegram.messenger.SharedConfig;
import org.telegram.messenger.UserConfig;
import org.telegram.messenger.Utilities;
import org.telegram.messenger.XiaomiUtilities;
import org.telegram.tgnet.TLRPC;
import org.telegram.ui.ActionBar.AlertDialog;
import org.telegram.ui.ActionBar.Theme;
import org.telegram.ui.Components.AlertsCreator;
import org.telegram.ui.Components.BulletinFactory;
import org.telegram.ui.Components.LayoutHelper;
import org.telegram.ui.Components.StickerImageView;
import org.telegram.ui.LaunchActivity;

public abstract class UpdaterUtils {
    private static AlertDialog dialog;

    public static void getAppUpdate(final Utilities.Callback2 callback2) {
        RemoteUtils.getMessages(new Utilities.Callback2() { // from class: com.exteragram.messenger.updater.UpdaterUtils$$ExternalSyntheticLambda1
            @Override // org.telegram.messenger.Utilities.Callback2
            public final void run(Object obj, Object obj2) {
                UpdaterUtils.m1429$r8$lambda$IowTZwq1pPTn6AMIS595dkH1as(callback2, (TLRPC.messages_Messages) obj, (TLRPC.TL_error) obj2);
            }
        });
    }

    /* JADX INFO: renamed from: $r8$lambda$IowTZwq1pPTn6AMIS59-5dkH1as, reason: not valid java name */
    public static /* synthetic */ void m1429$r8$lambda$IowTZwq1pPTn6AMIS595dkH1as(final Utilities.Callback2 callback2, TLRPC.messages_Messages messages_messages, TLRPC.TL_error tL_error) {
        if (tL_error != null || messages_messages == null) {
            callback2.run(null, tL_error);
        } else {
            parseUpdateResponse(messages_messages, new Utilities.Callback() { // from class: com.exteragram.messenger.updater.UpdaterUtils$$ExternalSyntheticLambda3
                @Override // org.telegram.messenger.Utilities.Callback
                public final void run(Object obj) {
                    UpdaterUtils.$r8$lambda$ghzDpb6CudUboMzPfRBCUQzDaBY(callback2, (TLRPC.TL_help_appUpdate) obj);
                }
            });
        }
    }

    public static /* synthetic */ void $r8$lambda$ghzDpb6CudUboMzPfRBCUQzDaBY(Utilities.Callback2 callback2, TLRPC.TL_help_appUpdate tL_help_appUpdate) {
        if (tL_help_appUpdate.id > 0 && !AyuInfra.isModified()) {
            Log.d("AyuGram", "Update found and app is not modified");
            callback2.run(tL_help_appUpdate, null);
        } else {
            TLRPC.TL_error tL_error = new TLRPC.TL_error();
            tL_error.text = "NO_UPDATE_METADATA";
            callback2.run(tL_help_appUpdate, tL_error);
        }
    }

    private static void parseUpdateResponse(TLRPC.messages_Messages messages_messages, final Utilities.Callback callback) {
        final int i;
        final int i2;
        final int i3;
        final TLRPC.TL_help_appUpdate tL_help_appUpdate = new TLRPC.TL_help_appUpdate();
        ArrayList arrayList = messages_messages.messages;
        int size = arrayList.size();
        int i4 = 0;
        while (true) {
            i = -1;
            if (i4 >= size) {
                i2 = -1;
                i3 = -1;
                break;
            }
            Object obj = arrayList.get(i4);
            i4++;
            TLRPC.Message message = (TLRPC.Message) obj;
            if (message instanceof TLRPC.TL_message) {
                String str = message.message;
                StringBuilder sb = new StringBuilder();
                sb.append("update_");
                sb.append(BuildVars.IS_LITE_VERSION ? "lite_" : _UrlKt.FRAGMENT_ENCODE_SET);
                sb.append(AyuInfra.getPackageVersion());
                sb.append("\n");
                if (str.startsWith(sb.toString())) {
                    String[] strArrSplit = message.message.split("\n");
                    if (strArrSplit.length >= 6) {
                        int i5 = -1;
                        int i6 = -1;
                        int i7 = -1;
                        for (String str2 : strArrSplit) {
                            byte b = 2;
                            String[] strArrSplit2 = str2.split("=", 2);
                            if (strArrSplit2.length == 2) {
                                String strTrim = strArrSplit2[0].trim();
                                String strTrim2 = strArrSplit2[1].trim();
                                strTrim.getClass();
                                switch (strTrim.hashCode()) {
                                    case -1890252483:
                                        b = strTrim.equals("sticker") ? (byte) 0 : (byte) -1;
                                        break;
                                    case -1085916422:
                                        b = strTrim.equals("can_not_skip") ? (byte) 1 : (byte) -1;
                                        break;
                                    case 3556653:
                                        if (!strTrim.equals("text")) {
                                            b = -1;
                                        }
                                        break;
                                    case 351608024:
                                        b = strTrim.equals("version") ? (byte) 3 : (byte) -1;
                                        break;
                                    case 861720859:
                                        b = strTrim.equals("document") ? (byte) 4 : (byte) -1;
                                        break;
                                    default:
                                        b = -1;
                                        break;
                                }
                                switch (b) {
                                    case 0:
                                        i6 = Integer.parseInt(strTrim2);
                                        break;
                                    case 1:
                                        tL_help_appUpdate.can_not_skip = Boolean.parseBoolean(strTrim2);
                                        break;
                                    case 2:
                                        i7 = Integer.parseInt(strTrim2);
                                        break;
                                    case 3:
                                        tL_help_appUpdate.version = strTrim2;
                                        break;
                                    case 4:
                                        i5 = Integer.parseInt(strTrim2);
                                        break;
                                }
                            }
                        }
                        tL_help_appUpdate.id = 1338;
                        i = i5;
                        i3 = i6;
                        i2 = i7;
                    }
                } else {
                    continue;
                }
            }
        }
        if (tL_help_appUpdate.id == 0) {
            callback.run(tL_help_appUpdate);
            return;
        }
        if (SharedConfig.versionBiggerOrEqual(BuildVars.AYU_VERSION, tL_help_appUpdate.version)) {
            Log.d("AyuGram", BuildVars.AYU_VERSION + " >= " + tL_help_appUpdate.version + ", no update needed");
            callback.run(tL_help_appUpdate);
            return;
        }
        RemoteUtils.getUpdateMessages(new Utilities.Callback2() { // from class: com.exteragram.messenger.updater.UpdaterUtils$$ExternalSyntheticLambda5
            @Override // org.telegram.messenger.Utilities.Callback2
            public final void run(Object obj2, Object obj3) {
                UpdaterUtils.m1428$r8$lambda$FJGawJwRbcTIKWZBZLd1XMg7BA(callback, tL_help_appUpdate, i2, i, i3, (TLRPC.messages_Messages) obj2, (TLRPC.TL_error) obj3);
            }
        });
    }

    /* JADX INFO: renamed from: $r8$lambda$FJGawJwRbcTI-KWZBZLd1XMg7BA, reason: not valid java name */
    public static /* synthetic */ void m1428$r8$lambda$FJGawJwRbcTIKWZBZLd1XMg7BA(Utilities.Callback callback, TLRPC.TL_help_appUpdate tL_help_appUpdate, int i, int i2, int i3, TLRPC.messages_Messages messages_messages, TLRPC.TL_error tL_error) {
        if (messages_messages == null || tL_error != null) {
            callback.run(tL_help_appUpdate);
            return;
        }
        ArrayList arrayList = messages_messages.messages;
        int size = arrayList.size();
        int i4 = 0;
        while (i4 < size) {
            Object obj = arrayList.get(i4);
            i4++;
            TLRPC.Message message = (TLRPC.Message) obj;
            if (message instanceof TLRPC.TL_message) {
                if (message.id == i) {
                    tL_help_appUpdate.entities = message.entities;
                    tL_help_appUpdate.text = message.message;
                } else {
                    TLRPC.MessageMedia messageMedia = message.media;
                    if (messageMedia != null) {
                        TLRPC.Document document = messageMedia.getDocument();
                        if (message.id == i2 && !document.attributes.isEmpty() && document.attributes.get(0).file_name.endsWith(".apk")) {
                            tL_help_appUpdate.document = document;
                            tL_help_appUpdate.flags |= 2;
                        } else if (message.id == i3 && MessageObject.isStickerDocument(document)) {
                            tL_help_appUpdate.sticker = document;
                            tL_help_appUpdate.flags |= 8;
                        }
                    }
                }
            }
        }
        callback.run(tL_help_appUpdate);
    }

    public static void installUpdate(final Activity activity, TLRPC.Document document) {
        if (activity == null || document == null) {
            return;
        }
        if (XiaomiUtilities.isMIUI()) {
            AndroidUtilities.openForView(document, activity);
            return;
        }
        final File pathToAttach = FileLoader.getInstance(UserConfig.selectedAccount).getPathToAttach(document, true);
        if (pathToAttach == null) {
            return;
        }
        AlertDialog alertDialog = dialog;
        if (alertDialog == null || !alertDialog.isShowing()) {
            showInstallDialog(activity);
            Utilities.globalQueue.postRunnable(new Runnable() { // from class: com.exteragram.messenger.updater.UpdaterUtils$$ExternalSyntheticLambda0
                @Override // java.lang.Runnable
                public final void run() {
                    UpdaterUtils.$r8$lambda$yqFMWMGrZzPs9teqMLorV3QSXjY(activity, pathToAttach);
                }
            });
        }
    }

    public static /* synthetic */ void $r8$lambda$yqFMWMGrZzPs9teqMLorV3QSXjY(Activity activity, File file) {
        InstallReceiver installReceiverRegister = register(activity, new Runnable() { // from class: com.exteragram.messenger.updater.UpdaterUtils$$ExternalSyntheticLambda2
            @Override // java.lang.Runnable
            public final void run() {
                UpdaterUtils.$r8$lambda$Yyx_ymCvZCiUCedMnTmQ4uD9eD0();
            }
        });
        installApk(activity, file);
        Intent intentWaitIntent = installReceiverRegister.waitIntent();
        if (intentWaitIntent != null) {
            activity.startActivity(intentWaitIntent);
        }
    }

    public static /* synthetic */ void $r8$lambda$Yyx_ymCvZCiUCedMnTmQ4uD9eD0() {
        AlertDialog alertDialog = dialog;
        if (alertDialog != null) {
            alertDialog.dismiss();
            dialog = null;
        }
    }

    private static void showInstallDialog(Activity activity) {
        String string;
        LinearLayout linearLayout = new LinearLayout(activity);
        linearLayout.setOrientation(1);
        linearLayout.setLayoutParams(LayoutHelper.createFrame(-1, -2.0f, 51, 4.0f, 4.0f, 4.0f, 4.0f));
        StickerImageView stickerImageView = new StickerImageView(activity, UserConfig.selectedAccount);
        stickerImageView.setStickerPackName("UtyaDuckFull");
        stickerImageView.setStickerNum(0);
        stickerImageView.getImageReceiver().setAutoRepeat(1);
        linearLayout.addView(stickerImageView, LayoutHelper.createLinear(160, 160, 49, 17, 24, 17, 0));
        TextView textView = new TextView(activity);
        textView.setTypeface(AndroidUtilities.getTypeface(AndroidUtilities.TYPEFACE_ROBOTO_MEDIUM));
        textView.setTextSize(1, 16.0f);
        textView.setTextColor(Theme.getColor(Theme.key_dialogTextBlack));
        textView.setSingleLine(true);
        textView.setEllipsize(TextUtils.TruncateAt.END);
        textView.setText(LocaleController.getString(R.string.UpdateInstalling));
        linearLayout.addView(textView, LayoutHelper.createLinear(-2, -2, 49, 17, 20, 17, 0));
        TextView textView2 = new TextView(activity);
        textView2.setGravity(17);
        textView2.setTextSize(1, 13.0f);
        textView2.setTextColor(Theme.getColor(Theme.key_dialogTextGray));
        if (Build.VERSION.SDK_INT < 29 || Settings.canDrawOverlays(activity)) {
            string = LocaleController.getString(R.string.UpdateInstallingRelaunch);
        } else {
            string = LocaleController.getString(R.string.UpdateInstallingNotification);
        }
        textView2.setText(string);
        linearLayout.addView(textView2, LayoutHelper.createLinear(-2, -2, 49, 17, 4, 17, 24));
        AlertDialog.Builder builder = new AlertDialog.Builder(activity);
        builder.setView(linearLayout);
        AlertDialog alertDialogCreate = builder.create();
        dialog = alertDialogCreate;
        alertDialogCreate.setCanceledOnTouchOutside(false);
        dialog.setCancelable(false);
        dialog.show();
    }

    private static void installApk(Activity activity, File file) {
        PendingIntent broadcast = PendingIntent.getBroadcast(activity, 0, new Intent(UpdaterUtils.class.getName()).setPackage(activity.getPackageName()), 167772160);
        PackageInstaller packageInstaller = activity.getPackageManager().getPackageInstaller();
        PackageInstaller.SessionParams sessionParams = new PackageInstaller.SessionParams(1);
        if (Build.VERSION.SDK_INT >= 31) {
            sessionParams.setRequireUserAction(2);
        }
        try {
            PackageInstaller.Session sessionOpenSession = packageInstaller.openSession(packageInstaller.createSession(sessionParams));
            try {
                OutputStream outputStreamOpenWrite = sessionOpenSession.openWrite(file.getName(), 0L, file.length());
                try {
                    FileInputStream fileInputStream = new FileInputStream(file);
                    try {
                        transfer(fileInputStream, outputStreamOpenWrite);
                        fileInputStream.close();
                        if (outputStreamOpenWrite != null) {
                            outputStreamOpenWrite.close();
                        }
                        sessionOpenSession.commit(broadcast.getIntentSender());
                        sessionOpenSession.close();
                    } catch (Throwable th) {
                        try {
                            fileInputStream.close();
                            throw th;
                        } catch (Throwable th2) {
                            th.addSuppressed(th2);
                            throw th;
                        }
                    }
                } catch (Throwable th3) {
                    if (outputStreamOpenWrite == null) {
                        throw th3;
                    }
                    try {
                        outputStreamOpenWrite.close();
                        throw th3;
                    } catch (Throwable th4) {
                        th3.addSuppressed(th4);
                        throw th3;
                    }
                    FileLog.e(e);
                    handleInstallError(activity, file, e);
                }
            } catch (Throwable th5) {
                if (sessionOpenSession == null) {
                    throw th5;
                }
                try {
                    sessionOpenSession.close();
                    throw th5;
                } catch (Throwable th6) {
                    th5.addSuppressed(th6);
                    throw th5;
                }
            }
        } catch (IOException e) {
            FileLog.e(e);
            handleInstallError(activity, file, e);
        }
    }

    private static void handleInstallError(Activity activity, File file, IOException iOException) {
        AndroidUtilities.runOnUIThread(new Runnable() { // from class: com.exteragram.messenger.updater.UpdaterUtils$$ExternalSyntheticLambda4
            @Override // java.lang.Runnable
            public final void run() {
                UpdaterUtils.$r8$lambda$7ZDy7OZWWRRScjHttmHHPHeOFco();
            }
        });
        AlertsCreator.createSimpleAlert(activity, LocaleController.getString(R.string.ErrorOccurred) + "\n" + iOException.getLocalizedMessage()).show();
        AndroidUtilities.openForView(file, "install.apk", "application/vnd.android.package-archive", activity, null, false);
    }

    public static /* synthetic */ void $r8$lambda$7ZDy7OZWWRRScjHttmHHPHeOFco() {
        AlertDialog alertDialog = dialog;
        if (alertDialog != null) {
            alertDialog.dismiss();
            dialog = null;
        }
    }

    private static void transfer(InputStream inputStream, OutputStream outputStream) throws IOException {
        byte[] bArr = new byte[8192];
        while (true) {
            int i = inputStream.read(bArr, 0, 8192);
            if (i < 0) {
                return;
            } else {
                outputStream.write(bArr, 0, i);
            }
        }
    }

    private static InstallReceiver register(Context context, Runnable runnable) {
        InstallReceiver installReceiver = new InstallReceiver(context, ApplicationLoader.getApplicationId(), runnable);
        ContextCompat.registerReceiver(context, installReceiver, new IntentFilter(UpdaterUtils.class.getName()), 4);
        return installReceiver;
    }

    /* JADX INFO: Access modifiers changed from: private */
    static class InstallReceiver extends BroadcastReceiver {
        private final Context context;
        private Intent intent;
        private final CountDownLatch latch;
        private final Runnable onSuccess;
        private final String packageName;

        private InstallReceiver(Context context, String str, Runnable runnable) {
            this.latch = new CountDownLatch(1);
            this.intent = null;
            this.context = context;
            this.packageName = str;
            this.onSuccess = runnable;
        }

        @Override // android.content.BroadcastReceiver
        public void onReceive(Context context, Intent intent) {
            if ("android.intent.action.PACKAGE_ADDED".equals(intent.getAction())) {
                Uri data = intent.getData();
                if (data == null || this.onSuccess == null || !data.getSchemeSpecificPart().equals(this.packageName)) {
                    return;
                }
                this.onSuccess.run();
                this.context.unregisterReceiver(this);
                return;
            }
            handlePackageInstallerResult(intent);
        }

        private void handlePackageInstallerResult(Intent intent) {
            int intExtra = intent.getIntExtra("android.content.pm.extra.STATUS", 4);
            if (intExtra == -1) {
                this.intent = (Intent) intent.getParcelableExtra("android.intent.extra.INTENT");
            } else {
                if (intExtra == 1 || intExtra == 2 || intExtra == 4 || intExtra == 5 || intExtra == 6 || intExtra == 7) {
                    handleFailure(intent);
                }
                Runnable runnable = this.onSuccess;
                if (runnable != null) {
                    runnable.run();
                }
                this.context.unregisterReceiver(this);
            }
            this.latch.countDown();
        }

        private void handleFailure(final Intent intent) {
            PackageInstaller packageInstaller;
            PackageInstaller.SessionInfo sessionInfo;
            int intExtra = intent.getIntExtra("android.content.pm.extra.SESSION_ID", 0);
            if (intExtra > 0 && (sessionInfo = (packageInstaller = this.context.getPackageManager().getPackageInstaller()).getSessionInfo(intExtra)) != null) {
                packageInstaller.abandonSession(sessionInfo.getSessionId());
            }
            Context context = this.context;
            if (context instanceof LaunchActivity) {
                ((LaunchActivity) context).showBulletin(new Function() { // from class: com.exteragram.messenger.updater.UpdaterUtils$InstallReceiver$$ExternalSyntheticLambda0
                    @Override // androidx.arch.core.util.Function
                    public final Object apply(Object obj) {
                        return ((BulletinFactory) obj).createErrorBulletin(LocaleController.formatString(R.string.UpdateFailedToInstall, Integer.valueOf(intent.getIntExtra("android.content.pm.extra.STATUS", 1))));
                    }
                });
            }
        }

        public Intent waitIntent() {
            try {
                this.latch.await(5L, TimeUnit.SECONDS);
            } catch (Exception unused) {
            }
            return this.intent;
        }
    }

    public static class UpdateReceiver extends BroadcastReceiver {
        @Override // android.content.BroadcastReceiver
        public void onReceive(Context context, Intent intent) {
            if ("android.intent.action.MY_PACKAGE_REPLACED".equals(intent.getAction())) {
                String packageName = context.getPackageName();
                if (packageName.equals(context.getPackageManager().getInstallerPackageName(packageName))) {
                    launchApp(context);
                }
            }
        }

        private void launchApp(Context context) {
            Intent flags = new Intent(context, (Class<?>) LaunchActivity.class).setFlags(268435456);
            if (Build.VERSION.SDK_INT < 29 || Settings.canDrawOverlays(context)) {
                context.startActivity(flags);
            } else {
                showNotification(context, flags);
            }
        }

        private void showNotification(Context context, Intent intent) {
            NotificationChannelCompat notificationChannelCompatBuild = new NotificationChannelCompat.Builder("updated", 4).setName(LocaleController.getString(R.string.UpdateApp)).setLightsEnabled(false).setVibrationEnabled(false).setSound(null, null).build();
            NotificationManagerCompat notificationManagerCompatFrom = NotificationManagerCompat.from(context);
            notificationManagerCompatFrom.createNotificationChannel(notificationChannelCompatBuild);
            notificationManagerCompatFrom.notify(8732833, new NotificationCompat.Builder(context, "updated").setSmallIcon(R.drawable.notification).setColor(AppUtils.getNotificationColor()).setShowWhen(false).setContentText(LocaleController.getString(R.string.UpdateInstalledNotification)).setCategory("status").setContentIntent(PendingIntent.getActivity(context, 0, intent, 201326592)).build());
        }
    }
}
