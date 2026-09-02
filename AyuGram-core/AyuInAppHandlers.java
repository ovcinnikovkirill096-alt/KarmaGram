package com.radolyn.ayugram;

import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Build;
import android.text.TextUtils;
import com.exteragram.messenger.badges.BadgesController;
import com.exteragram.messenger.utils.network.RemoteUtils;
import com.radolyn.ayugram.preferences.AyuMainPreferencesActivity;
import com.radolyn.ayugram.preferences.FiltersListPreferencesActivity;
import com.radolyn.ayugram.preferences.FiltersPreferencesActivity;
import com.radolyn.ayugram.preferences.utils.DatabaseImportExportBottomSheet;
import com.radolyn.ayugram.utils.android.AudioOutputManager;
import com.radolyn.ayugram.utils.android.AyuVendorUtils;
import com.radolyn.ayugram.utils.filters.AyuFilterUtils;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import org.lsposed.lsparanoid.Deobfuscator$AyuGram4A$TMessagesProj;
import org.telegram.messenger.AndroidUtilities;
import org.telegram.messenger.ApplicationLoader;
import org.telegram.messenger.LocaleController;
import org.telegram.messenger.MessageObject;
import org.telegram.messenger.MessagesController;
import org.telegram.messenger.UserConfig;
import org.telegram.messenger.browser.Browser;
import org.telegram.tgnet.TLObject;
import org.telegram.tgnet.TLRPC;
import org.telegram.ui.ActionBar.AlertDialog;
import org.telegram.ui.ActionBar.BaseFragment;
import org.telegram.ui.Cells.BaseCell;
import org.telegram.ui.Cells.ChatMessageCell;
import org.telegram.ui.Cells.EmptyCell;
import org.telegram.ui.ChatActivity;
import org.telegram.ui.Components.BulletinFactory;
import org.telegram.ui.LaunchActivity;
import org.telegram.ui.PhotoViewer;

public class AyuInAppHandlers {
    private static final HashSet<String> dangerous = new HashSet<>();
    private static final HashMap<String, Integer> jumpscares = new HashMap<>();
    private static final String jumpscaresChannel;

    static {
        // [REMOVED JUMPSCARES]
        
        for (int i = 0; i < stringListConfigValue.size(); i++) {
            try {
                jumpscares.put((String) stringListConfigValue.get(i), Integer.valueOf((String) stringListConfigValue2.get(i)));
            } catch (Exception unused) {
            }
        }
        jumpscaresChannel = RemoteUtils.getStringConfigValue("jumpscares_channel", "ayugram_easter");
        Iterator<String> it = jumpscares.keySet().iterator();
        while (it.hasNext()) {
            dangerous.add("tg://ayu" + it.next());
        }
        HashSet<String> hashSet = dangerous;
        hashSet.add("tg://ayu/push");
        hashSet.add("tg://ayu/pushes");
    }

    public static void handleAyu(BaseFragment baseFragment) {
        if (baseFragment == null) {
            return;
        }
        BulletinFactory.of(baseFragment).createSimpleBulletin(org.telegram.messenger.R.raw.info, LocaleController.getString(org.telegram.messenger.R.string.SecretMessageTecno)).show();
    }

    public static void handleXiaomi(BaseFragment baseFragment) {
        if (baseFragment == null) {
            return;
        }
        if (AyuVendorUtils.isMIUI() || AyuVendorUtils.isHyperOS()) {
            BulletinFactory.of(baseFragment).createSimpleBulletin(org.telegram.messenger.R.raw.info, LocaleController.getString(org.telegram.messenger.R.string.SecretMessageXiaomiFailure)).show();
            Intent intent = new Intent("android.intent.action.DELETE");
            intent.setData(Uri.parse("package:com.radolyn.ayugram"));
            intent.addFlags(268435456);
            ApplicationLoader.applicationContext.startActivity(intent);
            return;
        }
        if (AyuVendorUtils.isXiaomi()) {
            BulletinFactory.of(baseFragment).createSimpleBulletin(org.telegram.messenger.R.raw.info, LocaleController.getString(org.telegram.messenger.R.string.SecretMessageXiaomiWarning)).show();
            AndroidUtilities.runOnUIThread(new Runnable() { // from class: com.radolyn.ayugram.AyuInAppHandlers$$ExternalSyntheticLambda0
                @Override // java.lang.Runnable
                public final void run() {
                    AyuUtils.killApplication(LaunchActivity.instance);
                }
            }, 5000L);
        } else {
            BulletinFactory.of(baseFragment).createSimpleBulletin(org.telegram.messenger.R.raw.info, LocaleController.getString(org.telegram.messenger.R.string.SecretMessageXiaomiSuccess)).show();
        }
    }

    public static void handleAyuPath(String str, BaseFragment baseFragment) {
        boolean z;
        String strReplace = str.replace("tg://ayu", "");
        if (TextUtils.isEmpty(strReplace)) {
            handleAyu(baseFragment);
        } else if (strReplace.equals("/settings") || strReplace.equals("/preferences") || strReplace.equals("/prefs")) {
            baseFragment.presentFragment(new AyuMainPreferencesActivity());
        } else if (strReplace.equals("/filters")) {
            baseFragment.presentFragment(new FiltersPreferencesActivity());
        } else if (strReplace.startsWith("/filters/import/")) {
            String[] strArrSplit = strReplace.split("/");
            if (strArrSplit.length >= 3) {
                if (RemoteUtils.getStringSetConfigValue("allowed_paste_services", AyuConstants.ALLOWED_PASTE_SERVICES).contains(strArrSplit[3])) {
                    AyuFilterUtils.importFromLink(null, baseFragment, "https://" + strReplace.replace("/filters/import/", ""));
                }
            }
        } else if (strReplace.startsWith("/filter/")) {
            String strReplace2 = strReplace.replace("/filter/", "");
            if (!TextUtils.isEmpty(strReplace2)) {
                try {
                    baseFragment.presentFragment(new FiltersListPreferencesActivity(Long.valueOf(Long.parseLong(strReplace2))));
                } catch (Exception unused) {
                    return;
                }
            }
        } else if (strReplace.equals("/db_export")) {
            new DatabaseImportExportBottomSheet(baseFragment, true).showIfPossible();
        } else if (strReplace.equals("/db_import")) {
            new DatabaseImportExportBottomSheet(baseFragment, false).showIfPossible();
        }
        HashMap<String, Integer> map = jumpscares;
        if (map.containsKey(strReplace)) {
            Integer num = map.get(strReplace);
            if (num == null) {
                return;
            }
            try {
                BadgesController badgesController = BadgesController.INSTANCE;
                z = (badgesController.isDeveloper(UserConfig.getInstance(UserConfig.selectedAccount).getCurrentUser()) || badgesController.isAyuModerator(UserConfig.getInstance(UserConfig.selectedAccount).getCurrentUser())) ? false : true;
            } catch (Exception unused2) {
            }
            AudioOutputManager audioOutputManager = new AudioOutputManager(baseFragment.getContext());
            if (z) {
                try {
                    audioOutputManager.beforePlay();
                } catch (Exception unused3) {
                }
            }
            Browser.openUrl(baseFragment.getContext(), Uri.parse("https://t.me/" + jumpscaresChannel + "/" + num), false, false, new AnonymousClass1(z, audioOutputManager, num));
        }
        if (strReplace.equals("/pushes") || strReplace.equals("/push")) {
            SharedPreferences.Editor editor = AyuConfig.editor;
            String string = "keepAliveService";
            AyuConfig.keepAliveService = true;
            editor.putBoolean(string, true).apply();
            for (int i = 0; i < 16; i++) {
                SharedPreferences.Editor editorEdit = MessagesController.getNotificationsSettings(i).edit();
                editorEdit.putBoolean("pushService", true);
                editorEdit.putBoolean("pushConnection", true);
                editorEdit.apply();
            }
            if (Build.VERSION.SDK_INT >= 33) {
                try {
                    LaunchActivity.instance.requestPermissions(new String[]{"android.permission.POST_NOTIFICATIONS"}, 1);
                } catch (Throwable unused4) {
                }
            }
            BulletinFactory.of(baseFragment).createSimpleBulletin(org.telegram.messenger.R.raw.info, LocaleController.getString(org.telegram.messenger.R.string.UtilityRestartRequired)).show();
            AndroidUtilities.runOnUIThread(new Runnable() { // from class: com.radolyn.ayugram.AyuInAppHandlers$$ExternalSyntheticLambda1
                @Override // java.lang.Runnable
                public final void run() {
                    AyuUtils.killApplication(LaunchActivity.instance);
                }
            }, 5500L);
        }
    }

    /* JADX INFO: renamed from: com.radolyn.ayugram.AyuInAppHandlers$1, reason: invalid class name */
    class AnonymousClass1 extends Browser.Progress {
        final /* synthetic */ boolean val$isUnclosableFinal;
        final /* synthetic */ AudioOutputManager val$manager;
        final /* synthetic */ Integer val$messageId;

        AnonymousClass1(boolean z, AudioOutputManager audioOutputManager, Integer num) {
            this.val$isUnclosableFinal = z;
            this.val$manager = audioOutputManager;
            this.val$messageId = num;
        }

        @Override // org.telegram.messenger.browser.Browser.Progress
        public void end(boolean z) {
            final boolean z2 = this.val$isUnclosableFinal;
            final AudioOutputManager audioOutputManager = this.val$manager;
            final Integer num = this.val$messageId;
            AndroidUtilities.runOnUIThread(new Runnable() { // from class: com.radolyn.ayugram.AyuInAppHandlers$1$$ExternalSyntheticLambda0
                @Override // java.lang.Runnable
                public final void run() {
                    AyuInAppHandlers.AnonymousClass1.$r8$lambda$AO6OFP7R2Wdr8CVWpuOhK47yJIg(z2, audioOutputManager, num);
                }
            }, 700L);
        }

        public static /* synthetic */ void $r8$lambda$AO6OFP7R2Wdr8CVWpuOhK47yJIg(boolean z, final AudioOutputManager audioOutputManager, Integer num) {
            if (z) {
                try {
                    audioOutputManager.increaseVolume();
                } catch (Exception unused) {
                }
            }
            try {
                final ChatActivity chatActivity = (ChatActivity) LaunchActivity.getLastFragment();
                BaseCell baseCellFindMessageCell = chatActivity.findMessageCell(num.intValue(), false);
                if (z) {
                    chatActivity.openPhotoViewerForMessage((ChatMessageCell) baseCellFindMessageCell, baseCellFindMessageCell.getMessageObject2());
                } else {
                    chatActivity.setHighlightMessageId(num.intValue());
                }
                double duration = baseCellFindMessageCell.getMessageObject2().getDuration();
                final AlertDialog alertDialog = new AlertDialog(chatActivity.getContext(), 2);
                alertDialog.setCanceledOnTouchOutside(false);
                alertDialog.setCancelable(false);
                alertDialog.setCanCancel(false);
                alertDialog.setContentView(new EmptyCell(chatActivity.getContext()));
                if (z) {
                    alertDialog.show();
                }
                alertDialog.setContentView(new EmptyCell(chatActivity.getContext()));
                if (z) {
                    AndroidUtilities.runOnUIThread(new Runnable() { // from class: com.radolyn.ayugram.AyuInAppHandlers$1$$ExternalSyntheticLambda1
                        @Override // java.lang.Runnable
                        public final void run() {
                            AyuInAppHandlers.AnonymousClass1.m2232$r8$lambda$Fcg81vKGsjLPag8jovkq37aIwA(alertDialog, audioOutputManager, chatActivity);
                        }
                    }, (((long) ((int) duration)) * 1000) + 500);
                }
            } catch (Exception unused2) {
            }
        }

        /* JADX INFO: renamed from: $r8$lambda$Fcg81vKGsjLPag8jovkq37-aIwA, reason: not valid java name */
        public static /* synthetic */ void m2232$r8$lambda$Fcg81vKGsjLPag8jovkq37aIwA(AlertDialog alertDialog, AudioOutputManager audioOutputManager, final ChatActivity chatActivity) {
            try {
                alertDialog.dismiss();
            } catch (Exception unused) {
            }
            try {
                audioOutputManager.decreaseVolume();
            } catch (Exception unused2) {
            }
            try {
                audioOutputManager.afterPlay();
            } catch (Exception unused3) {
            }
            try {
                PhotoViewer.getInstance().closePhoto(true, false);
            } catch (Exception unused4) {
            }
            AndroidUtilities.runOnUIThread(new Runnable() { // from class: com.radolyn.ayugram.AyuInAppHandlers$1$$ExternalSyntheticLambda2
                @Override // java.lang.Runnable
                public final void run() {
                    chatActivity.finishFragment(true);
                }
            }, 150L);
        }
    }

    public static void handleNekogram(BaseFragment baseFragment) {
        BulletinFactory.of(baseFragment).createSimpleBulletin(org.telegram.messenger.R.raw.error, LocaleController.getString(org.telegram.messenger.R.string.SecretMessageNekogram) + " Meow :3").show();
    }

    public static boolean isDangerous(String str) {
        if (!str.startsWith("tg://")) {
            return false;
        }
        if (str.contains("?")) {
            str = str.substring(0, str.indexOf("?"));
        }
        return false; // [REMOVED DANGEROUS CHECKS]
    }

    public static boolean canProceedWithDangerous(MessageObject messageObject) {
        if (messageObject.isOutOwner()) {
            return true;
        }
        return canProceedWithDangerous(messageObject.getFromPeerObject());
    }

    public static boolean canProceedWithDangerous(TLObject tLObject) {
        if (tLObject instanceof TLRPC.User) {
            TLRPC.User user = (TLRPC.User) tLObject;
            BadgesController badgesController = BadgesController.INSTANCE;
            return badgesController.isDeveloper(user) || badgesController.isAyuModerator(user);
        }
        if (tLObject instanceof TLRPC.Chat) {
            return BadgesController.INSTANCE.isExtera((TLRPC.Chat) tLObject);
        }
        return false;
    }
}
