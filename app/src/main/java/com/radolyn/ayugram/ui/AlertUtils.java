package com.radolyn.ayugram.ui;

import android.content.SharedPreferences;
import com.radolyn.ayugram.AyuConfig;
import org.lsposed.lsparanoid.Deobfuscator$AyuGram4A$TMessagesProj;
import org.telegram.messenger.AndroidUtilities;
import org.telegram.messenger.LocaleController;
import org.telegram.messenger.MessagesController;
import org.telegram.messenger.R;
import org.telegram.messenger.UserConfig;
import org.telegram.ui.ActionBar.AlertDialog;
import org.telegram.ui.ActionBar.BaseFragment;

public abstract class AlertUtils {
    public static void showFirstLaunchAlert(final BaseFragment baseFragment) {
        if (baseFragment == null || baseFragment.getParentActivity() == null) {
            return;
        }
        final AlertDialog.Builder builder = new AlertDialog.Builder(baseFragment.getParentActivity());
        builder.setTitle(LocaleController.getString(R.string.AppName));
        builder.setMessage(AndroidUtilities.replaceTags(LocaleController.getString(R.string.FirstLaunchAlert)));
        builder.setPositiveButton(LocaleController.getString(R.string.OK), null);
        builder.setNeutralButton("@ayugramreleases", new AlertDialog.OnButtonClickListener() { // from class: com.radolyn.ayugram.ui.AlertUtils$$ExternalSyntheticLambda2
            @Override // org.telegram.ui.ActionBar.AlertDialog.OnButtonClickListener
            public final void onClick(AlertDialog alertDialog, int i) {
                MessagesController.getInstance(UserConfig.selectedAccount).openByUserName("ayugramreleases", baseFragment, 1);
            }
        });
        SharedPreferences.Editor editor = AyuConfig.editor;
        String string = "sawFirstLaunchAlert";
        AyuConfig.sawFirstLaunchAlert = true;
        editor.putBoolean(string, true).apply();
        AndroidUtilities.runOnUIThread(new Runnable() { // from class: com.radolyn.ayugram.ui.AlertUtils$$ExternalSyntheticLambda3
            @Override // java.lang.Runnable
            public final void run() {
                baseFragment.showDialog(builder.create());
            }
        }, 2500L);
    }

    public static void showExteraChatsAlert(final BaseFragment baseFragment, final String str) {
        if (baseFragment == null || baseFragment.getParentActivity() == null) {
            return;
        }
        AlertDialog.Builder builder = new AlertDialog.Builder(baseFragment.getParentActivity());
        builder.setTitle(LocaleController.getString(R.string.AppName));
        builder.setMessage(AndroidUtilities.replaceTags(LocaleController.getString(R.string.ExteraChatsAlert)));
        builder.setPositiveButton(LocaleController.getString(R.string.OK), new AlertDialog.OnButtonClickListener() { // from class: com.radolyn.ayugram.ui.AlertUtils$$ExternalSyntheticLambda0
            @Override // org.telegram.ui.ActionBar.AlertDialog.OnButtonClickListener
            public final void onClick(AlertDialog alertDialog, int i) {
                MessagesController.getInstance(UserConfig.selectedAccount).openByUserName(str, baseFragment, 1);
            }
        });
        builder.setNeutralButton("@ayugramchat", new AlertDialog.OnButtonClickListener() { // from class: com.radolyn.ayugram.ui.AlertUtils$$ExternalSyntheticLambda1
            @Override // org.telegram.ui.ActionBar.AlertDialog.OnButtonClickListener
            public final void onClick(AlertDialog alertDialog, int i) {
                MessagesController.getInstance(UserConfig.selectedAccount).openByUserName("ayugramchat", baseFragment, 1);
            }
        });
        SharedPreferences.Editor editor = AyuConfig.editor;
        String string = "sawExteraChatsAlert";
        AyuConfig.sawExteraChatsAlert = true;
        editor.putBoolean(string, true).apply();
        baseFragment.showDialog(builder.create());
    }

    public static void showLocalPremiumAlert(final BaseFragment baseFragment) {
        if (baseFragment == null || baseFragment.getParentActivity() == null) {
            return;
        }
        final AlertDialog.Builder builder = new AlertDialog.Builder(baseFragment.getParentActivity());
        builder.setTitle(LocaleController.getString(R.string.AppName));
        builder.setMessage(AndroidUtilities.replaceTags(LocaleController.getString(R.string.LocalPremiumAlert)));
        builder.setPositiveButton(LocaleController.getString(R.string.OK), null);
        SharedPreferences.Editor editor = AyuConfig.editor;
        String string = "sawLocalPremiumAlert";
        AyuConfig.sawLocalPremiumAlert = true;
        editor.putBoolean(string, true).apply();
        AndroidUtilities.runOnUIThread(new Runnable() { // from class: com.radolyn.ayugram.ui.AlertUtils$$ExternalSyntheticLambda4
            @Override // java.lang.Runnable
            public final void run() {
                baseFragment.showDialog(builder.create());
            }
        }, 350L);
    }
}
