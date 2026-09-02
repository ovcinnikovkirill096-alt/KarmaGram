package com.exteragram.messenger.utils.chats;

import android.app.Activity;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.TextUtils;
import com.android.tools.r8.RecordTag;
import com.exteragram.messenger.ExteraConfig;
import com.exteragram.messenger.ai.ui.GenerateFromMessageBottomSheet$GenerationData$$ExternalSyntheticRecord0;
import com.exteragram.messenger.components.QRCodeSheet;
import com.exteragram.messenger.export.api.ApiWrap$HistoryMessageMarkupButton$$ExternalSyntheticRecord0;
import com.exteragram.messenger.plugins.PluginsController;
import com.exteragram.messenger.plugins.hooks.MenuItemRecord;
import com.exteragram.messenger.plugins.ui.PluginsActivity;
import com.exteragram.messenger.plugins.utils.MenuContextBuilder;
import com.google.android.exoplayer2.util.Consumer;
import com.radolyn.ayugram.AyuUtils;
import com.radolyn.ayugram.controllers.AyuGhostController;
import com.radolyn.ayugram.preferences.GhostModePreferencesActivity;
import j$.util.Objects;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import org.telegram.messenger.AndroidUtilities;
import org.telegram.messenger.BuildVars;
import org.telegram.messenger.FileLog;
import org.telegram.messenger.LocaleController;
import org.telegram.messenger.MediaDataController;
import org.telegram.messenger.MessagesController;
import org.telegram.messenger.MrzRecognizer;
import org.telegram.messenger.R;
import org.telegram.messenger.UserConfig;
import org.telegram.messenger.browser.Browser;
import org.telegram.tgnet.ConnectionsManager;
import org.telegram.tgnet.RequestDelegate;
import org.telegram.tgnet.TLObject;
import org.telegram.tgnet.TLRPC;
import org.telegram.ui.ActionBar.BaseFragment;
import org.telegram.ui.ActionIntroActivity;
import org.telegram.ui.CallLogActivity;
import org.telegram.ui.CameraScanActivity;
import org.telegram.ui.ChannelCreateActivity;
import org.telegram.ui.ChatActivity;
import org.telegram.ui.Components.BulletinFactory;
import org.telegram.ui.ContactsActivity;
import org.telegram.ui.DialogsActivity;
import org.telegram.ui.GroupCreateActivity;
import org.telegram.ui.LaunchActivity;
import org.telegram.ui.ProfileActivity;
import org.telegram.ui.SettingsActivity;
import org.telegram.ui.WebAppDisclaimerAlert;
import org.telegram.ui.bots.BotWebViewSheet;
import org.telegram.ui.web.SearchEngine;

public abstract class MainMenuHelper {

    public static final class MenuItemInfo extends RecordTag {
        private final int iconRes;
        private final Runnable onClick;
        private final Runnable onLongClick;
        private final CharSequence text;

        private /* synthetic */ boolean $record$equals(Object obj) {
            if (!(obj instanceof MenuItemInfo)) {
                return false;
            }
            MenuItemInfo menuItemInfo = (MenuItemInfo) obj;
            return this.iconRes == menuItemInfo.iconRes && Objects.equals(this.text, menuItemInfo.text) && Objects.equals(this.onClick, menuItemInfo.onClick) && Objects.equals(this.onLongClick, menuItemInfo.onLongClick);
        }

        private /* synthetic */ Object[] $record$getFieldsAsObjects() {
            return new Object[]{Integer.valueOf(this.iconRes), this.text, this.onClick, this.onLongClick};
        }

        public MenuItemInfo(int i, CharSequence charSequence, Runnable runnable, Runnable runnable2) {
            this.iconRes = i;
            this.text = charSequence;
            this.onClick = runnable;
            this.onLongClick = runnable2;
        }

        public final boolean equals(Object obj) {
            return $record$equals(obj);
        }

        public final int hashCode() {
            return MainMenuHelper$MenuContext$$ExternalSyntheticRecord0.m(this.iconRes, this.text, this.onClick, this.onLongClick);
        }

        public int iconRes() {
            return this.iconRes;
        }

        public Runnable onClick() {
            return this.onClick;
        }

        public Runnable onLongClick() {
            return this.onLongClick;
        }

        public CharSequence text() {
            return this.text;
        }

        public final String toString() {
            return GenerateFromMessageBottomSheet$GenerationData$$ExternalSyntheticRecord0.m($record$getFieldsAsObjects(), MenuItemInfo.class, "iconRes;text;onClick;onLongClick");
        }
    }

    public static final class MenuContext extends RecordTag {
        private final Runnable archiveClick;
        private final int currentAccount;
        private final BaseFragment fragment;
        private final Map pluginContextData;

        private /* synthetic */ boolean $record$equals(Object obj) {
            if (!(obj instanceof MenuContext)) {
                return false;
            }
            MenuContext menuContext = (MenuContext) obj;
            return this.currentAccount == menuContext.currentAccount && Objects.equals(this.fragment, menuContext.fragment) && Objects.equals(this.archiveClick, menuContext.archiveClick) && Objects.equals(this.pluginContextData, menuContext.pluginContextData);
        }

        private /* synthetic */ Object[] $record$getFieldsAsObjects() {
            return new Object[]{Integer.valueOf(this.currentAccount), this.fragment, this.archiveClick, this.pluginContextData};
        }

        public MenuContext(int i, BaseFragment baseFragment, Runnable runnable, Map map) {
            this.currentAccount = i;
            this.fragment = baseFragment;
            this.archiveClick = runnable;
            this.pluginContextData = map;
        }

        public Runnable archiveClick() {
            return this.archiveClick;
        }

        public int currentAccount() {
            return this.currentAccount;
        }

        public final boolean equals(Object obj) {
            return $record$equals(obj);
        }

        public BaseFragment fragment() {
            return this.fragment;
        }

        public final int hashCode() {
            return MainMenuHelper$MenuContext$$ExternalSyntheticRecord0.m(this.currentAccount, this.fragment, this.archiveClick, this.pluginContextData);
        }

        public Map pluginContextData() {
            return this.pluginContextData;
        }

        public final String toString() {
            return GenerateFromMessageBottomSheet$GenerationData$$ExternalSyntheticRecord0.m($record$getFieldsAsObjects(), MenuContext.class, "currentAccount;fragment;archiveClick;pluginContextData");
        }
    }

    public static final class AttachMenuBotInfo extends RecordTag {
        private final TLRPC.TL_attachMenuBot bot;
        private final int iconRes;
        private final Runnable onClick;
        private final Runnable onLongClick;
        private final CharSequence text;

        private /* synthetic */ boolean $record$equals(Object obj) {
            if (!(obj instanceof AttachMenuBotInfo)) {
                return false;
            }
            AttachMenuBotInfo attachMenuBotInfo = (AttachMenuBotInfo) obj;
            return this.iconRes == attachMenuBotInfo.iconRes && Objects.equals(this.text, attachMenuBotInfo.text) && Objects.equals(this.bot, attachMenuBotInfo.bot) && Objects.equals(this.onClick, attachMenuBotInfo.onClick) && Objects.equals(this.onLongClick, attachMenuBotInfo.onLongClick);
        }

        private /* synthetic */ Object[] $record$getFieldsAsObjects() {
            return new Object[]{Integer.valueOf(this.iconRes), this.text, this.bot, this.onClick, this.onLongClick};
        }

        public AttachMenuBotInfo(int i, CharSequence charSequence, TLRPC.TL_attachMenuBot tL_attachMenuBot, Runnable runnable, Runnable runnable2) {
            this.iconRes = i;
            this.text = charSequence;
            this.bot = tL_attachMenuBot;
            this.onClick = runnable;
            this.onLongClick = runnable2;
        }

        public TLRPC.TL_attachMenuBot bot() {
            return this.bot;
        }

        public final boolean equals(Object obj) {
            return $record$equals(obj);
        }

        public final int hashCode() {
            return ApiWrap$HistoryMessageMarkupButton$$ExternalSyntheticRecord0.m(this.iconRes, this.text, this.bot, this.onClick, this.onLongClick);
        }

        public int iconRes() {
            return this.iconRes;
        }

        public Runnable onClick() {
            return this.onClick;
        }

        public Runnable onLongClick() {
            return this.onLongClick;
        }

        public CharSequence text() {
            return this.text;
        }

        public final String toString() {
            return GenerateFromMessageBottomSheet$GenerationData$$ExternalSyntheticRecord0.m($record$getFieldsAsObjects(), AttachMenuBotInfo.class, "iconRes;text;bot;onClick;onLongClick");
        }
    }

    public static MenuContext createMenuContext(int i, BaseFragment baseFragment) {
        return new MenuContext(i, baseFragment, null, null);
    }

    public static MenuContext createMenuContext(int i, BaseFragment baseFragment, Runnable runnable, Map map) {
        return new MenuContext(i, baseFragment, runnable, map);
    }

    public static Map createPluginContextData(int i, BaseFragment baseFragment) {
        MenuContextBuilder menuContextBuilderWithAccount = MenuContextBuilder.create().withAccount(i);
        if (baseFragment != null) {
            menuContextBuilderWithAccount.withContext(baseFragment.getContext() != null ? baseFragment.getContext() : baseFragment.getParentActivity());
        }
        TLRPC.User currentUser = UserConfig.getInstance(i).getCurrentUser();
        if (currentUser != null) {
            menuContextBuilderWithAccount.withUser(currentUser);
        }
        return menuContextBuilderWithAccount.build();
    }

    public static List resolveDrawerMenuItems(int i, MenuContext menuContext) {
        ExteraConfig.MainMenuItem byId = ExteraConfig.MainMenuItem.getById(i);
        if (byId == null) {
            return Collections.EMPTY_LIST;
        }
        int i2 = AnonymousClass2.$SwitchMap$com$exteragram$messenger$ExteraConfig$MainMenuItem[byId.ordinal()];
        if (i2 == 1) {
            return resolveDrawerBotMenuItems(menuContext);
        }
        if (i2 == 2) {
            return resolveDrawerPluginMenuItems(menuContext);
        }
        MenuItemInfo menuItemInfoResolveMenuItem = resolveMenuItem(i, menuContext);
        return menuItemInfoResolveMenuItem == null ? Collections.EMPTY_LIST : Collections.singletonList(menuItemInfoResolveMenuItem);
    }

    /* JADX INFO: renamed from: com.exteragram.messenger.utils.chats.MainMenuHelper$2, reason: invalid class name */
    static /* synthetic */ class AnonymousClass2 {
        static final /* synthetic */ int[] $SwitchMap$com$exteragram$messenger$ExteraConfig$MainMenuItem;

        static {
            int[] iArr = new int[ExteraConfig.MainMenuItem.values().length];
            $SwitchMap$com$exteragram$messenger$ExteraConfig$MainMenuItem = iArr;
            try {
                iArr[ExteraConfig.MainMenuItem.BOTS.ordinal()] = 1;
            } catch (NoSuchFieldError unused) {
            }
            try {
                $SwitchMap$com$exteragram$messenger$ExteraConfig$MainMenuItem[ExteraConfig.MainMenuItem.PLUGINS.ordinal()] = 2;
            } catch (NoSuchFieldError unused2) {
            }
            try {
                $SwitchMap$com$exteragram$messenger$ExteraConfig$MainMenuItem[ExteraConfig.MainMenuItem.PROFILE.ordinal()] = 3;
            } catch (NoSuchFieldError unused3) {
            }
            try {
                $SwitchMap$com$exteragram$messenger$ExteraConfig$MainMenuItem[ExteraConfig.MainMenuItem.ARCHIVE.ordinal()] = 4;
            } catch (NoSuchFieldError unused4) {
            }
            try {
                $SwitchMap$com$exteragram$messenger$ExteraConfig$MainMenuItem[ExteraConfig.MainMenuItem.NEW_GROUP.ordinal()] = 5;
            } catch (NoSuchFieldError unused5) {
            }
            try {
                $SwitchMap$com$exteragram$messenger$ExteraConfig$MainMenuItem[ExteraConfig.MainMenuItem.CONTACTS.ordinal()] = 6;
            } catch (NoSuchFieldError unused6) {
            }
            try {
                $SwitchMap$com$exteragram$messenger$ExteraConfig$MainMenuItem[ExteraConfig.MainMenuItem.CALLS.ordinal()] = 7;
            } catch (NoSuchFieldError unused7) {
            }
            try {
                $SwitchMap$com$exteragram$messenger$ExteraConfig$MainMenuItem[ExteraConfig.MainMenuItem.NEW_CHANNEL.ordinal()] = 8;
            } catch (NoSuchFieldError unused8) {
            }
            try {
                $SwitchMap$com$exteragram$messenger$ExteraConfig$MainMenuItem[ExteraConfig.MainMenuItem.SAVED.ordinal()] = 9;
            } catch (NoSuchFieldError unused9) {
            }
            try {
                $SwitchMap$com$exteragram$messenger$ExteraConfig$MainMenuItem[ExteraConfig.MainMenuItem.SETTINGS.ordinal()] = 10;
            } catch (NoSuchFieldError unused10) {
            }
            try {
                $SwitchMap$com$exteragram$messenger$ExteraConfig$MainMenuItem[ExteraConfig.MainMenuItem.BROWSER.ordinal()] = 11;
            } catch (NoSuchFieldError unused11) {
            }
            try {
                $SwitchMap$com$exteragram$messenger$ExteraConfig$MainMenuItem[ExteraConfig.MainMenuItem.QR.ordinal()] = 12;
            } catch (NoSuchFieldError unused12) {
            }
            try {
                $SwitchMap$com$exteragram$messenger$ExteraConfig$MainMenuItem[ExteraConfig.MainMenuItem.GHOST_MODE.ordinal()] = 13;
            } catch (NoSuchFieldError unused13) {
            }
            try {
                $SwitchMap$com$exteragram$messenger$ExteraConfig$MainMenuItem[ExteraConfig.MainMenuItem.KILL_APP.ordinal()] = 14;
            } catch (NoSuchFieldError unused14) {
            }
        }
    }

    public static MenuItemInfo resolveMenuItem(int i, MenuContext menuContext) {
        ExteraConfig.MainMenuItem byId = ExteraConfig.MainMenuItem.getById(i);
        if (byId != null && menuContext.fragment() != null) {
            final int iCurrentAccount = menuContext.currentAccount();
            final BaseFragment baseFragmentFragment = menuContext.fragment();
            switch (AnonymousClass2.$SwitchMap$com$exteragram$messenger$ExteraConfig$MainMenuItem[byId.ordinal()]) {
                case 2:
                    if (PluginsController.isPluginEngineSupported()) {
                        return new MenuItemInfo(R.drawable.msg_plugins, LocaleController.getString(R.string.Plugins), new Runnable() { // from class: com.exteragram.messenger.utils.chats.MainMenuHelper$$ExternalSyntheticLambda14
                            @Override // java.lang.Runnable
                            public final void run() {
                                baseFragmentFragment.presentFragment(new PluginsActivity());
                            }
                        }, null);
                    }
                    break;
                case 3:
                    return new MenuItemInfo(R.drawable.left_status_profile, LocaleController.getString(R.string.MyProfile), new Runnable() { // from class: com.exteragram.messenger.utils.chats.MainMenuHelper$$ExternalSyntheticLambda1
                        @Override // java.lang.Runnable
                        public final void run() {
                            MainMenuHelper.m1439$r8$lambda$v0h8pBSNeYqoHvMbcetfzvFNlU(iCurrentAccount, baseFragmentFragment);
                        }
                    }, null);
                case 4:
                    return new MenuItemInfo(R.drawable.msg_archive, LocaleController.getString(R.string.ArchivedChats), menuContext.archiveClick() != null ? menuContext.archiveClick() : new Runnable() { // from class: com.exteragram.messenger.utils.chats.MainMenuHelper$$ExternalSyntheticLambda7
                        @Override // java.lang.Runnable
                        public final void run() {
                            MainMenuHelper.m1434$r8$lambda$KDLBDKsgHSE6LBLdB8g62MSI00(baseFragmentFragment);
                        }
                    }, null);
                case 5:
                    return new MenuItemInfo(R.drawable.msg_groups, LocaleController.getString(R.string.NewGroup), new Runnable() { // from class: com.exteragram.messenger.utils.chats.MainMenuHelper$$ExternalSyntheticLambda8
                        @Override // java.lang.Runnable
                        public final void run() {
                            baseFragmentFragment.presentFragment(new GroupCreateActivity(new Bundle()));
                        }
                    }, null);
                case 6:
                    return new MenuItemInfo(R.drawable.msg_contacts, LocaleController.getString(R.string.Contacts), new Runnable() { // from class: com.exteragram.messenger.utils.chats.MainMenuHelper$$ExternalSyntheticLambda9
                        @Override // java.lang.Runnable
                        public final void run() {
                            MainMenuHelper.$r8$lambda$rtSRDj8a2WJm0LXRVKfT4uBArCY(baseFragmentFragment);
                        }
                    }, null);
                case 7:
                    return new MenuItemInfo(R.drawable.msg_calls, LocaleController.getString(R.string.Calls), new Runnable() { // from class: com.exteragram.messenger.utils.chats.MainMenuHelper$$ExternalSyntheticLambda10
                        @Override // java.lang.Runnable
                        public final void run() {
                            baseFragmentFragment.presentFragment(new CallLogActivity());
                        }
                    }, null);
                case 8:
                    return new MenuItemInfo(R.drawable.msg_channel, LocaleController.getString(R.string.NewChannel), new Runnable() { // from class: com.exteragram.messenger.utils.chats.MainMenuHelper$$ExternalSyntheticLambda11
                        @Override // java.lang.Runnable
                        public final void run() {
                            MainMenuHelper.$r8$lambda$Q8IakieKJqFN62CokMs4nVB419M(baseFragmentFragment);
                        }
                    }, null);
                case 9:
                    return new MenuItemInfo(R.drawable.msg_saved, LocaleController.getString(R.string.SavedMessages), new Runnable() { // from class: com.exteragram.messenger.utils.chats.MainMenuHelper$$ExternalSyntheticLambda12
                        @Override // java.lang.Runnable
                        public final void run() {
                            MainMenuHelper.$r8$lambda$a5bq_jLS447MCh9JamahNb2Edmk(iCurrentAccount, baseFragmentFragment);
                        }
                    }, null);
                case 10:
                    return new MenuItemInfo(R.drawable.msg_settings, LocaleController.getString(R.string.Settings), new Runnable() { // from class: com.exteragram.messenger.utils.chats.MainMenuHelper$$ExternalSyntheticLambda13
                        @Override // java.lang.Runnable
                        public final void run() {
                            baseFragmentFragment.presentFragment(new SettingsActivity());
                        }
                    }, null);
                case 11:
                    return new MenuItemInfo(R.drawable.msg2_language, LocaleController.getString(R.string.BrowserSettingsTitle), new Runnable() { // from class: com.exteragram.messenger.utils.chats.MainMenuHelper$$ExternalSyntheticLambda15
                        @Override // java.lang.Runnable
                        public final void run() {
                            MainMenuHelper.$r8$lambda$TSIhEbVdCMjRIBRygQDcrN3tWYY(baseFragmentFragment);
                        }
                    }, null);
                case 12:
                    return new MenuItemInfo(R.drawable.msg_qrcode, LocaleController.getString(R.string.AuthAnotherClient), new Runnable() { // from class: com.exteragram.messenger.utils.chats.MainMenuHelper$$ExternalSyntheticLambda2
                        @Override // java.lang.Runnable
                        public final void run() {
                            MainMenuHelper.$r8$lambda$saqA7TDqFSmi2ZXfA6fwir6uv4I(baseFragmentFragment);
                        }
                    }, null);
                case 13:
                    return new MenuItemInfo(AyuGhostController.getInstance(UserConfig.selectedAccount).isGhostModeActive() ? R.drawable.ayu_ghost_crossed : R.drawable.ayu_ghost, LocaleController.getString(AyuGhostController.getInstance(UserConfig.selectedAccount).isGhostModeActive() ? R.string.DisableGhostMode : R.string.EnableGhostMode), new Runnable() { // from class: com.exteragram.messenger.utils.chats.MainMenuHelper$$ExternalSyntheticLambda3
                        @Override // java.lang.Runnable
                        public final void run() {
                            AyuGhostController.getInstance(UserConfig.selectedAccount).toggleGhostMode(BulletinFactory.global());
                        }
                    }, new Runnable() { // from class: com.exteragram.messenger.utils.chats.MainMenuHelper$$ExternalSyntheticLambda4
                        @Override // java.lang.Runnable
                        public final void run() {
                            baseFragmentFragment.presentFragment(new GhostModePreferencesActivity());
                        }
                    });
                case 14:
                    return new MenuItemInfo(ExteraConfig.iconPack == 2 ? R.drawable.msg_block_remix : R.drawable.msg_disable, LocaleController.getString(R.string.KillApp), new Runnable() { // from class: com.exteragram.messenger.utils.chats.MainMenuHelper$$ExternalSyntheticLambda5
                        @Override // java.lang.Runnable
                        public final void run() {
                            AndroidUtilities.runOnUIThread(new Runnable() { // from class: com.exteragram.messenger.utils.chats.MainMenuHelper$$ExternalSyntheticLambda19
                                @Override // java.lang.Runnable
                                public final void run() {
                                    AyuUtils.killApplication(baseFragment.getParentActivity());
                                }
                            }, 200L);
                        }
                    }, new Runnable() { // from class: com.exteragram.messenger.utils.chats.MainMenuHelper$$ExternalSyntheticLambda6
                        @Override // java.lang.Runnable
                        public final void run() {
                            AndroidUtilities.runOnUIThread(new Runnable() { // from class: com.exteragram.messenger.utils.chats.MainMenuHelper$$ExternalSyntheticLambda17
                                @Override // java.lang.Runnable
                                public final void run() {
                                    AyuUtils.restartApplication();
                                }
                            }, 200L);
                        }
                    });
                default:
                    return null;
            }
        }
        return null;
    }

    /* JADX INFO: renamed from: $r8$lambda$v0h8pBSNeYqoHv-MbcetfzvFNlU, reason: not valid java name */
    public static /* synthetic */ void m1439$r8$lambda$v0h8pBSNeYqoHvMbcetfzvFNlU(int i, BaseFragment baseFragment) {
        Bundle bundle = new Bundle();
        bundle.putLong("user_id", UserConfig.getInstance(i).getClientUserId());
        bundle.putBoolean("my_profile", true);
        baseFragment.presentFragment(new ProfileActivity(bundle));
    }

    /* JADX INFO: renamed from: $r8$lambda$KDLBDKsgHSE6LBLdB8g6-2MSI00, reason: not valid java name */
    public static /* synthetic */ void m1434$r8$lambda$KDLBDKsgHSE6LBLdB8g62MSI00(BaseFragment baseFragment) {
        Bundle bundle = new Bundle();
        bundle.putInt("folderId", 1);
        baseFragment.presentFragment(new DialogsActivity(bundle));
    }

    public static /* synthetic */ void $r8$lambda$rtSRDj8a2WJm0LXRVKfT4uBArCY(BaseFragment baseFragment) {
        Bundle bundle = new Bundle();
        bundle.putBoolean("needPhonebook", true);
        bundle.putBoolean("needFinishFragment", false);
        baseFragment.presentFragment(new ContactsActivity(bundle));
    }

    public static /* synthetic */ void $r8$lambda$Q8IakieKJqFN62CokMs4nVB419M(BaseFragment baseFragment) {
        SharedPreferences globalMainSettings = MessagesController.getGlobalMainSettings();
        if (!BuildVars.DEBUG_VERSION && globalMainSettings.getBoolean("channel_intro", false)) {
            Bundle bundle = new Bundle();
            bundle.putInt("step", 0);
            baseFragment.presentFragment(new ChannelCreateActivity(bundle));
        } else {
            baseFragment.presentFragment(new ActionIntroActivity(0));
            globalMainSettings.edit().putBoolean("channel_intro", true).apply();
        }
    }

    public static /* synthetic */ void $r8$lambda$a5bq_jLS447MCh9JamahNb2Edmk(int i, BaseFragment baseFragment) {
        Bundle bundle = new Bundle();
        bundle.putLong("user_id", UserConfig.getInstance(i).getClientUserId());
        baseFragment.presentFragment(new ChatActivity(bundle));
    }

    public static /* synthetic */ void $r8$lambda$TSIhEbVdCMjRIBRygQDcrN3tWYY(BaseFragment baseFragment) {
        SearchEngine current = SearchEngine.getCurrent();
        String homepage = current.getHomepage();
        Activity parentActivity = baseFragment.getParentActivity();
        if (TextUtils.isEmpty(homepage)) {
            homepage = current.search_url;
        }
        Browser.openInTelegramBrowser(parentActivity, homepage, null);
    }

    public static /* synthetic */ void $r8$lambda$saqA7TDqFSmi2ZXfA6fwir6uv4I(BaseFragment baseFragment) {
        LaunchActivity launchActivity = LaunchActivity.instance;
        if (launchActivity != null && launchActivity.checkSelfPermission("android.permission.CAMERA") != 0) {
            LaunchActivity.instance.requestPermissions(new String[]{"android.permission.CAMERA"}, 34);
        } else {
            CameraScanActivity.showAsSheet(baseFragment, false, 1, (CameraScanActivity.CameraScanActivityDelegate) new AnonymousClass1());
        }
    }

    /* JADX INFO: renamed from: com.exteragram.messenger.utils.chats.MainMenuHelper$1, reason: invalid class name */
    class AnonymousClass1 implements CameraScanActivity.CameraScanActivityDelegate {
        @Override // org.telegram.ui.CameraScanActivity.CameraScanActivityDelegate
        public /* synthetic */ void didFindMrzInfo(MrzRecognizer.Result result) {
            CameraScanActivity.CameraScanActivityDelegate.CC.$default$didFindMrzInfo(this, result);
        }

        @Override // org.telegram.ui.CameraScanActivity.CameraScanActivityDelegate
        public /* synthetic */ void didFindQr(String str) {
            CameraScanActivity.CameraScanActivityDelegate.CC.$default$didFindQr(this, str);
        }

        @Override // org.telegram.ui.CameraScanActivity.CameraScanActivityDelegate
        public /* synthetic */ String getSubtitleText() {
            return CameraScanActivity.CameraScanActivityDelegate.CC.$default$getSubtitleText(this);
        }

        @Override // org.telegram.ui.CameraScanActivity.CameraScanActivityDelegate
        public /* synthetic */ void onDismiss() {
            CameraScanActivity.CameraScanActivityDelegate.CC.$default$onDismiss(this);
        }

        AnonymousClass1() {
        }

        @Override // org.telegram.ui.CameraScanActivity.CameraScanActivityDelegate
        public boolean processQr(final String str, final Runnable runnable) {
            AndroidUtilities.runOnUIThread(new Runnable() { // from class: com.exteragram.messenger.utils.chats.MainMenuHelper$1$$ExternalSyntheticLambda0
                @Override // java.lang.Runnable
                public final void run() {
                    MainMenuHelper.AnonymousClass1.$r8$lambda$PXFIpgdtIGghglRNcVl91ndQqRc(str, runnable);
                }
            }, 600L);
            return true;
        }

        public static /* synthetic */ void $r8$lambda$PXFIpgdtIGghglRNcVl91ndQqRc(final String str, Runnable runnable) {
            AndroidUtilities.runOnUIThread(new Runnable() { // from class: com.exteragram.messenger.utils.chats.MainMenuHelper$1$$ExternalSyntheticLambda1
                @Override // java.lang.Runnable
                public final void run() {
                    MainMenuHelper.AnonymousClass1.$r8$lambda$wwDUSw59NnCN0TJApzf27CVLJhA(str);
                }
            }, 150L);
            runnable.run();
        }

        public static /* synthetic */ void $r8$lambda$wwDUSw59NnCN0TJApzf27CVLJhA(String str) {
            BaseFragment safeLastFragment = LaunchActivity.getSafeLastFragment();
            if (safeLastFragment != null) {
                new QRCodeSheet(safeLastFragment, str).show();
            }
        }
    }

    public static List getAttachMenuBotItems(final MenuContext menuContext) {
        ArrayList arrayList;
        BaseFragment baseFragmentFragment = menuContext.fragment();
        LaunchActivity launchActivityFindLaunchActivity = findLaunchActivity(baseFragmentFragment);
        TLRPC.TL_attachMenuBots attachMenuBots = MediaDataController.getInstance(menuContext.currentAccount()).getAttachMenuBots();
        if (baseFragmentFragment == null || launchActivityFindLaunchActivity == null || attachMenuBots == null || (arrayList = attachMenuBots.bots) == null || arrayList.isEmpty()) {
            return Collections.EMPTY_LIST;
        }
        ArrayList arrayList2 = new ArrayList();
        ArrayList arrayList3 = attachMenuBots.bots;
        int size = arrayList3.size();
        int i = 0;
        while (i < size) {
            Object obj = arrayList3.get(i);
            i++;
            final TLRPC.TL_attachMenuBot tL_attachMenuBot = (TLRPC.TL_attachMenuBot) obj;
            if (tL_attachMenuBot.show_in_side_menu) {
                arrayList2.add(new AttachMenuBotInfo(getAttachMenuBotIconRes(tL_attachMenuBot), tL_attachMenuBot.short_name, tL_attachMenuBot, createAttachMenuBotClickAction(menuContext, tL_attachMenuBot, launchActivityFindLaunchActivity), new Runnable() { // from class: com.exteragram.messenger.utils.chats.MainMenuHelper$$ExternalSyntheticLambda16
                    @Override // java.lang.Runnable
                    public final void run() {
                        BotWebViewSheet.deleteBot(menuContext.currentAccount(), tL_attachMenuBot.bot_id, null);
                    }
                }));
            }
        }
        return arrayList2;
    }

    public static List getPluginMenuItems(MenuContext menuContext) {
        return PluginsController.getInstance().getMenuItemsForLocation("main_menu", getPluginContextData(menuContext));
    }

    public static Runnable createPluginClickAction(final MenuItemRecord menuItemRecord, MenuContext menuContext) {
        final Map pluginContextData = getPluginContextData(menuContext);
        return new Runnable() { // from class: com.exteragram.messenger.utils.chats.MainMenuHelper$$ExternalSyntheticLambda20
            @Override // java.lang.Runnable
            public final void run() {
                MainMenuHelper.m1436$r8$lambda$c8b7gcA8Tuk_fNWQv3ByRooMFw(menuItemRecord, pluginContextData);
            }
        };
    }

    /* JADX INFO: renamed from: $r8$lambda$c8b7gcA8T-uk_fNWQv3ByRooMFw, reason: not valid java name */
    public static /* synthetic */ void m1436$r8$lambda$c8b7gcA8Tuk_fNWQv3ByRooMFw(MenuItemRecord menuItemRecord, Map map) {
        try {
            menuItemRecord.onClickCallback.call(map);
        } catch (Exception e) {
            FileLog.e(e);
        }
    }

    private static List resolveDrawerBotMenuItems(MenuContext menuContext) {
        List<AttachMenuBotInfo> attachMenuBotItems = getAttachMenuBotItems(menuContext);
        if (attachMenuBotItems.isEmpty()) {
            return Collections.EMPTY_LIST;
        }
        ArrayList arrayList = new ArrayList(attachMenuBotItems.size());
        for (AttachMenuBotInfo attachMenuBotInfo : attachMenuBotItems) {
            arrayList.add(new MenuItemInfo(attachMenuBotInfo.iconRes(), attachMenuBotInfo.text(), attachMenuBotInfo.onClick(), attachMenuBotInfo.onLongClick()));
        }
        return arrayList;
    }

    private static List resolveDrawerPluginMenuItems(final MenuContext menuContext) {
        if (menuContext.fragment() == null) {
            return Collections.EMPTY_LIST;
        }
        MenuItemInfo menuItemInfoResolveMenuItem = resolveMenuItem(ExteraConfig.MainMenuItem.PLUGINS.id, menuContext);
        List<MenuItemRecord> pluginMenuItems = getPluginMenuItems(menuContext);
        if (pluginMenuItems.isEmpty()) {
            return menuItemInfoResolveMenuItem == null ? Collections.EMPTY_LIST : Collections.singletonList(menuItemInfoResolveMenuItem);
        }
        ArrayList arrayList = new ArrayList(pluginMenuItems.size() + (menuItemInfoResolveMenuItem != null ? 1 : 0));
        if (menuItemInfoResolveMenuItem != null) {
            arrayList.add(menuItemInfoResolveMenuItem);
        }
        for (MenuItemRecord menuItemRecord : pluginMenuItems) {
            if (menuItemRecord != null && !TextUtils.isEmpty(menuItemRecord.text)) {
                int i = menuItemRecord.iconResId;
                if (i == 0) {
                    i = R.drawable.msg_plugins;
                }
                arrayList.add(new MenuItemInfo(i, menuItemRecord.text, createPluginClickAction(menuItemRecord, menuContext), new Runnable() { // from class: com.exteragram.messenger.utils.chats.MainMenuHelper$$ExternalSyntheticLambda0
                    @Override // java.lang.Runnable
                    public final void run() {
                        menuContext.fragment().presentFragment(new PluginsActivity());
                    }
                }));
            }
        }
        if (arrayList.isEmpty()) {
            return menuItemInfoResolveMenuItem == null ? Collections.EMPTY_LIST : Collections.singletonList(menuItemInfoResolveMenuItem);
        }
        return arrayList;
    }

    private static Runnable createAttachMenuBotClickAction(final MenuContext menuContext, final TLRPC.TL_attachMenuBot tL_attachMenuBot, final LaunchActivity launchActivity) {
        return new Runnable() { // from class: com.exteragram.messenger.utils.chats.MainMenuHelper$$ExternalSyntheticLambda18
            @Override // java.lang.Runnable
            public final void run() {
                MainMenuHelper.$r8$lambda$o_YwMi1JmYtlR2Qakn1wG8eah3A(tL_attachMenuBot, menuContext, launchActivity);
            }
        };
    }

    public static /* synthetic */ void $r8$lambda$o_YwMi1JmYtlR2Qakn1wG8eah3A(final TLRPC.TL_attachMenuBot tL_attachMenuBot, final MenuContext menuContext, final LaunchActivity launchActivity) {
        if (tL_attachMenuBot.inactive || tL_attachMenuBot.side_menu_disclaimer_needed) {
            WebAppDisclaimerAlert.show(menuContext.fragment().getContext() != null ? menuContext.fragment().getContext() : launchActivity, new Consumer() { // from class: com.exteragram.messenger.utils.chats.MainMenuHelper$$ExternalSyntheticLambda21
                @Override // com.google.android.exoplayer2.util.Consumer
                public final void accept(Object obj) {
                    MainMenuHelper.$r8$lambda$cW2RQ2WOF26e4auR4y2LV4TZjNM(menuContext, tL_attachMenuBot, launchActivity, (Boolean) obj);
                }
            }, null, null);
        } else {
            LaunchActivity.showAttachMenuBot(launchActivity, menuContext.currentAccount(), tL_attachMenuBot, null, true);
        }
    }

    public static /* synthetic */ void $r8$lambda$cW2RQ2WOF26e4auR4y2LV4TZjNM(final MenuContext menuContext, final TLRPC.TL_attachMenuBot tL_attachMenuBot, final LaunchActivity launchActivity, Boolean bool) {
        TLRPC.TL_messages_toggleBotInAttachMenu tL_messages_toggleBotInAttachMenu = new TLRPC.TL_messages_toggleBotInAttachMenu();
        tL_messages_toggleBotInAttachMenu.bot = MessagesController.getInstance(menuContext.currentAccount()).getInputUser(tL_attachMenuBot.bot_id);
        tL_messages_toggleBotInAttachMenu.enabled = true;
        tL_messages_toggleBotInAttachMenu.write_allowed = true;
        ConnectionsManager.getInstance(menuContext.currentAccount()).sendRequest(tL_messages_toggleBotInAttachMenu, new RequestDelegate() { // from class: com.exteragram.messenger.utils.chats.MainMenuHelper$$ExternalSyntheticLambda22
            @Override // org.telegram.tgnet.RequestDelegate
            public final void run(TLObject tLObject, TLRPC.TL_error tL_error) {
                AndroidUtilities.runOnUIThread(new Runnable() { // from class: com.exteragram.messenger.utils.chats.MainMenuHelper$$ExternalSyntheticLambda23
                    @Override // java.lang.Runnable
                    public final void run() {
                        MainMenuHelper.$r8$lambda$37etZ14j_sORHKJdELVwNtL8yLI(tL_attachMenuBot, launchActivity, menuContext);
                    }
                });
            }
        }, 66);
    }

    public static /* synthetic */ void $r8$lambda$37etZ14j_sORHKJdELVwNtL8yLI(TLRPC.TL_attachMenuBot tL_attachMenuBot, LaunchActivity launchActivity, MenuContext menuContext) {
        tL_attachMenuBot.side_menu_disclaimer_needed = false;
        tL_attachMenuBot.inactive = false;
        LaunchActivity.showAttachMenuBot(launchActivity, menuContext.currentAccount(), tL_attachMenuBot, null, true);
        MediaDataController.getInstance(menuContext.currentAccount()).updateAttachMenuBotsInCache();
    }

    private static Map getPluginContextData(MenuContext menuContext) {
        if (menuContext.pluginContextData() != null) {
            return menuContext.pluginContextData();
        }
        return createPluginContextData(menuContext.currentAccount(), menuContext.fragment());
    }

    private static int getAttachMenuBotIconRes(TLRPC.TL_attachMenuBot tL_attachMenuBot) {
        return tL_attachMenuBot.bot_id == 1985737506 ? R.drawable.menu_wallet : R.drawable.msg_bot;
    }

    private static LaunchActivity findLaunchActivity(BaseFragment baseFragment) {
        if (baseFragment == null) {
            return LaunchActivity.instance;
        }
        Activity activityFindActivity = AndroidUtilities.findActivity(baseFragment.getContext() != null ? baseFragment.getContext() : baseFragment.getParentActivity());
        return activityFindActivity instanceof LaunchActivity ? (LaunchActivity) activityFindActivity : LaunchActivity.instance;
    }
}
