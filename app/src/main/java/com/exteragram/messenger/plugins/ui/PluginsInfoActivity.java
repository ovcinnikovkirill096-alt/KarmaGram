package com.exteragram.messenger.plugins.ui;

import android.content.SharedPreferences;
import android.os.Build;
import android.text.Html;
import android.text.SpannableString;
import android.view.View;
import com.exteragram.messenger.ExteraConfig;
import com.exteragram.messenger.plugins.PluginsController;
import com.exteragram.messenger.plugins.PythonPluginsEngine;
import com.exteragram.messenger.preferences.BasePreferencesActivity;
import com.exteragram.messenger.preferences.utils.SettingsRegistry;
import com.exteragram.messenger.utils.text.LocaleUtils;
import com.google.android.exoplayer2.util.Consumer;
import java.util.ArrayList;
import org.lsposed.lsparanoid.Deobfuscator$AyuGram4A$TMessagesProj;
import org.telegram.messenger.LocaleController;
import org.telegram.messenger.NotificationCenter;
import org.telegram.messenger.R;
import org.telegram.messenger.browser.Browser;
import org.telegram.ui.Cells.TextCheckCell;
import org.telegram.ui.Components.BulletinFactory;
import org.telegram.ui.Components.UItem;
import org.telegram.ui.Components.UniversalAdapter;

public class PluginsInfoActivity extends BasePreferencesActivity implements NotificationCenter.NotificationCenterDelegate {

    public enum PreferenceItem {
        DEVELOPER_MODE,
        COMPACT_VIEW,
        SAFE_MODE,
        SDK_AUTO_UPDATE,
        SDK_BETA_VERSIONS,
        CHECK_SDK_UPDATES,
        RESTORE_SDK_FROM_APK,
        DOCUMENTATION,
        TRUSTED_PLUGINS,
        PLUGINS_DISABLE_ART_OPTS,
        SDK_HEADER;

        public int getId() {
            return ordinal() + 1;
        }
    }

    @Override // com.exteragram.messenger.preferences.BasePreferencesActivity
    public String getTitle() {
        return LocaleController.getString(R.string.PluginsEngine);
    }

    @Override // org.telegram.ui.ActionBar.BaseFragment
    public boolean onFragmentCreate() {
        super.onFragmentCreate();
        NotificationCenter.getGlobalInstance().addObserver(this, NotificationCenter.pluginsPySdkInfoChanged);
        PythonPluginsEngine.Updater.notifyWhenChangeStatus = true;
        return true;
    }

    @Override // org.telegram.ui.ActionBar.BaseFragment
    public void onFragmentDestroy() {
        super.onFragmentDestroy();
        NotificationCenter.getGlobalInstance().removeObserver(this, NotificationCenter.pluginsPySdkInfoChanged);
        PythonPluginsEngine.Updater.notifyWhenChangeStatus = false;
        if (PythonPluginsEngine.Updater.status == 2) {
            PythonPluginsEngine.Updater.status = 0;
        }
    }

    @Override // org.telegram.messenger.NotificationCenter.NotificationCenterDelegate
    public void didReceivedNotification(int i, int i2, Object... objArr) {
        if (i == NotificationCenter.pluginsPySdkInfoChanged) {
            this.listView.adapter.update(true);
        }
    }

    @Override // com.exteragram.messenger.preferences.BasePreferencesActivity
    protected void fillItems(ArrayList<UItem> arrayList, UniversalAdapter universalAdapter) {
        SpannableString spannableString;
        arrayList.add(UItem.asHeader(LocaleController.getString(R.string.Settings)));
        arrayList.add(UItem.asCheck(PreferenceItem.DEVELOPER_MODE.getId(), LocaleController.getString(R.string.PluginsDevMode), R.drawable.msg_settings).setChecked(ExteraConfig.pluginsDevMode).setEnabled(ExteraConfig.pluginsEngine).setSearchable(this).setLinkAlias("pluginsDeveloperMode", this));
        arrayList.add(UItem.asCheck(PreferenceItem.COMPACT_VIEW.getId(), LocaleController.getString(R.string.PluginsCompactView), R.drawable.msg_topics).setChecked(ExteraConfig.pluginsCompactView).setEnabled(ExteraConfig.pluginsEngine).setSearchable(this).setLinkAlias("pluginsCompactView", this));
        arrayList.add(UItem.asCheck(PreferenceItem.PLUGINS_DISABLE_ART_OPTS.getId(), LocaleController.getString(R.string.PluginsDisableArt), R.drawable.msg_link2).setChecked(ExteraConfig.pluginsDisableArtOpts).setEnabled(ExteraConfig.pluginsEngine).setSearchable(this).setValue(LocaleController.getString(R.string.PluginsDisableArtInfo)).setMultiline(true).setLinkAlias("pluginsDisableArtOpts", this));
        arrayList.add(UItem.asCheck(PreferenceItem.SAFE_MODE.getId(), LocaleController.getString(R.string.PluginsSafeMode), R.drawable.msg_secret).setChecked(ExteraConfig.pluginsSafeMode).setSearchable(this).setLinkAlias("pluginsSafeMode", this));
        arrayList.add(UItem.asShadow(LocaleController.getString(R.string.PluginsSafeModeInfo2)));
        arrayList.add(UItem.asAnimatedHeader(PreferenceItem.SDK_HEADER.getId(), SettingsRegistry.markAsNewFeature("Plugins-Python-SDK") ? LocaleUtils.applyNewSpan("Python SDK") : "Python SDK"));
        arrayList.add(UItem.asCheck(PreferenceItem.SDK_AUTO_UPDATE.getId(), LocaleController.getString(R.string.PluginsPySdkAutoUpdate)).setChecked(ExteraConfig.pluginsPySdkAutoUpdate).setSearchable(this).setEnabled(PythonPluginsEngine.Updater.status < 3).setValue(PythonPluginsEngine.Updater.getStateString()).setMultiline(true).setLinkAlias("pluginsPySdkAutoUpdate", this));
        arrayList.add(UItem.asCheck(PreferenceItem.SDK_BETA_VERSIONS.getId(), LocaleController.getString(R.string.PluginsPySdkEnableBetaVersion)).setChecked(ExteraConfig.pluginsPySdkBetaVersions).setSearchable(this).setEnabled(PythonPluginsEngine.Updater.status < 3).setLinkAlias("pluginsPySdkBetaVersions", this));
        arrayList.add(UItem.asButton(PreferenceItem.CHECK_SDK_UPDATES.getId(), LocaleController.getString(R.string.PluginsPySdkCheckUpdates)).accent().setSearchable(this).setEnabled(PythonPluginsEngine.Updater.status < 3).setIcon(R.drawable.msg_retry).setLinkAlias("pluginsPySdkCheckUpdates", this));
        if (ExteraConfig.pluginsDevMode && !ExteraConfig.pluginsEngine && !PythonPluginsEngine.Updater.isSdkFromApk()) {
            arrayList.add(UItem.asButton(PreferenceItem.RESTORE_SDK_FROM_APK.getId(), LocaleController.getString(R.string.RestoreSdkFromApk)).red().setIcon(R.drawable.msg_reset));
        }
        arrayList.add(UItem.asShadow());
        arrayList.add(UItem.asHeader(LocaleController.getString(R.string.Links)));
        arrayList.add(UItem.asButton(PreferenceItem.DOCUMENTATION.getId(), LocaleController.getString(R.string.PluginsDocumentation)).setSearchable(this).setIcon(R.drawable.menu_intro).setLinkAlias("pluginsDocumentation", this));
        arrayList.add(UItem.asButton(PreferenceItem.TRUSTED_PLUGINS.getId(), LocaleController.getString(R.string.PluginsTrusted)).accent().setIcon(R.drawable.msg2_policy).setSearchable(this).setLinkAlias("trustedPlugins", this));
        String string = LocaleController.getString(R.string.PluginsPoweredBy);
        if (Build.VERSION.SDK_INT >= 24) {
            spannableString = new SpannableString(Html.fromHtml(string, 0));
        } else {
            spannableString = new SpannableString(Html.fromHtml(string));
        }
        arrayList.add(UItem.asShadow(LocaleUtils.formatWithHtmlURLs(spannableString)));
    }

    @Override // com.exteragram.messenger.preferences.BasePreferencesActivity
    protected void onClick(UItem uItem, View view, int i, float f, float f2) {
        int i2 = uItem.id;
        if (i2 <= 0 || i2 > PreferenceItem.values().length) {
            return;
        }
        PreferenceItem preferenceItem = PreferenceItem.values()[uItem.id - 1];
        if ((view instanceof TextCheckCell) && (ExteraConfig.pluginsEngine || preferenceItem == PreferenceItem.SAFE_MODE || preferenceItem == PreferenceItem.SDK_AUTO_UPDATE || preferenceItem == PreferenceItem.SDK_BETA_VERSIONS)) {
            switch (AnonymousClass1.$SwitchMap$com$exteragram$messenger$plugins$ui$PluginsInfoActivity$PreferenceItem[preferenceItem.ordinal()]) {
                case 1:
                    toggleBooleanSettingAndRefresh("pluginsDevMode", uItem, new Consumer() { // from class: com.exteragram.messenger.plugins.ui.PluginsInfoActivity$$ExternalSyntheticLambda0
                        @Override // com.google.android.exoplayer2.util.Consumer
                        public final void accept(Object obj) {
                            this.f$0.lambda$onClick$0((Boolean) obj);
                        }
                    });
                    break;
                case 2:
                    toggleBooleanSettingAndRefresh("pluginsCompactView", uItem, new Consumer() { // from class: com.exteragram.messenger.plugins.ui.PluginsInfoActivity$$ExternalSyntheticLambda1
                        @Override // com.google.android.exoplayer2.util.Consumer
                        public final void accept(Object obj) {
                            PluginsInfoActivity.m1289$r8$lambda$pWKtYTyPbBtRw4e0nvUkinwVnM((Boolean) obj);
                        }
                    });
                    break;
                case 3:
                    final SharedPreferences sharedPreferences = PluginsController.getInstance().preferences;
                    toggleBooleanSettingAndRefresh(sharedPreferences, "had_crash", uItem, new Consumer() { // from class: com.exteragram.messenger.plugins.ui.PluginsInfoActivity$$ExternalSyntheticLambda2
                        @Override // com.google.android.exoplayer2.util.Consumer
                        public final void accept(Object obj) {
                            PluginsInfoActivity.$r8$lambda$W4VL9MhcoPOWziaGyIvwfRWHxNQ(sharedPreferences, (Boolean) obj);
                        }
                    });
                    break;
                case 4:
                    toggleBooleanSettingAndRefresh("pluginsDisableArtOpts", uItem, new Consumer() { // from class: com.exteragram.messenger.plugins.ui.PluginsInfoActivity$$ExternalSyntheticLambda3
                        @Override // com.google.android.exoplayer2.util.Consumer
                        public final void accept(Object obj) {
                            this.f$0.lambda$onClick$3((Boolean) obj);
                        }
                    });
                    break;
                case 5:
                    toggleBooleanSettingAndRefresh("pluginsPySdkAutoUpdate", uItem, new Consumer() { // from class: com.exteragram.messenger.plugins.ui.PluginsInfoActivity$$ExternalSyntheticLambda4
                        @Override // com.google.android.exoplayer2.util.Consumer
                        public final void accept(Object obj) {
                            ExteraConfig.pluginsPySdkAutoUpdate = ((Boolean) obj).booleanValue();
                        }
                    });
                    break;
                case 6:
                    toggleBooleanSettingAndRefresh("pluginsPySdkBetaVersions", uItem, new Consumer() { // from class: com.exteragram.messenger.plugins.ui.PluginsInfoActivity$$ExternalSyntheticLambda5
                        @Override // com.google.android.exoplayer2.util.Consumer
                        public final void accept(Object obj) {
                            PluginsInfoActivity.$r8$lambda$s1k45N0SihJ1Lfr6dtegNEuyfDE((Boolean) obj);
                        }
                    });
                    break;
            }
            return;
        }
        PreferenceItem preferenceItem2 = PreferenceItem.DOCUMENTATION;
        if (preferenceItem == preferenceItem2 || preferenceItem == PreferenceItem.TRUSTED_PLUGINS) {
            Browser.openUrl(getParentActivity(), (preferenceItem == preferenceItem2 ? "-98827589430204" : "-97869811723196"));
            return;
        }
        if (preferenceItem == PreferenceItem.CHECK_SDK_UPDATES) {
            PythonPluginsEngine.Updater.checkUpdates(true);
        } else if (preferenceItem == PreferenceItem.RESTORE_SDK_FROM_APK) {
            PythonPluginsEngine.Updater.restoreSdkFromApk();
            BulletinFactory.of(this).createSimpleBulletin(R.raw.contact_check, LocaleController.getString(R.string.RestartRequired)).show();
            this.listView.adapter.update(true);
        }
    }

    /* JADX INFO: renamed from: com.exteragram.messenger.plugins.ui.PluginsInfoActivity$1, reason: invalid class name */
    static /* synthetic */ class AnonymousClass1 {
        static final /* synthetic */ int[] $SwitchMap$com$exteragram$messenger$plugins$ui$PluginsInfoActivity$PreferenceItem;

        static {
            int[] iArr = new int[PreferenceItem.values().length];
            $SwitchMap$com$exteragram$messenger$plugins$ui$PluginsInfoActivity$PreferenceItem = iArr;
            try {
                iArr[PreferenceItem.DEVELOPER_MODE.ordinal()] = 1;
            } catch (NoSuchFieldError unused) {
            }
            try {
                $SwitchMap$com$exteragram$messenger$plugins$ui$PluginsInfoActivity$PreferenceItem[PreferenceItem.COMPACT_VIEW.ordinal()] = 2;
            } catch (NoSuchFieldError unused2) {
            }
            try {
                $SwitchMap$com$exteragram$messenger$plugins$ui$PluginsInfoActivity$PreferenceItem[PreferenceItem.SAFE_MODE.ordinal()] = 3;
            } catch (NoSuchFieldError unused3) {
            }
            try {
                $SwitchMap$com$exteragram$messenger$plugins$ui$PluginsInfoActivity$PreferenceItem[PreferenceItem.PLUGINS_DISABLE_ART_OPTS.ordinal()] = 4;
            } catch (NoSuchFieldError unused4) {
            }
            try {
                $SwitchMap$com$exteragram$messenger$plugins$ui$PluginsInfoActivity$PreferenceItem[PreferenceItem.SDK_AUTO_UPDATE.ordinal()] = 5;
            } catch (NoSuchFieldError unused5) {
            }
            try {
                $SwitchMap$com$exteragram$messenger$plugins$ui$PluginsInfoActivity$PreferenceItem[PreferenceItem.SDK_BETA_VERSIONS.ordinal()] = 6;
            } catch (NoSuchFieldError unused6) {
            }
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$onClick$0(Boolean bool) {
        ExteraConfig.pluginsDevMode = bool.booleanValue();
        PluginsController.getInstance().checkDevServers();
        BulletinFactory bulletinFactoryOf = BulletinFactory.of(this);
        boolean z = ExteraConfig.pluginsDevMode;
        bulletinFactoryOf.createSimpleBulletin(z ? R.raw.contact_check : R.raw.error, LocaleController.getString(z ? R.string.PluginsDevServerLaunched : R.string.PluginsDevServerStopped)).show();
    }

    /* JADX INFO: renamed from: $r8$lambda$pWKtYTy-PbBtRw4e0nvUkinwVnM, reason: not valid java name */
    public static /* synthetic */ void m1289$r8$lambda$pWKtYTyPbBtRw4e0nvUkinwVnM(Boolean bool) {
        ExteraConfig.pluginsCompactView = bool.booleanValue();
        NotificationCenter.getGlobalInstance().lambda$postNotificationNameOnUIThread$1(NotificationCenter.reloadInterface, new Object[0]);
    }

    public static /* synthetic */ void $r8$lambda$W4VL9MhcoPOWziaGyIvwfRWHxNQ(SharedPreferences sharedPreferences, Boolean bool) {
        ExteraConfig.pluginsSafeMode = bool.booleanValue();
        if (bool.booleanValue()) {
            sharedPreferences.edit().putString("crashed_plugin_id", "manual!").apply();
        } else {
            sharedPreferences.edit().remove("crashed_plugin_id").apply();
        }
        PluginsController.getInstance().restart();
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$onClick$3(Boolean bool) {
        ExteraConfig.pluginsDisableArtOpts = bool.booleanValue();
        PluginsController.applyArtOpts();
        showRestartBulletin();
    }

    public static /* synthetic */ void $r8$lambda$s1k45N0SihJ1Lfr6dtegNEuyfDE(Boolean bool) {
        ExteraConfig.pluginsPySdkBetaVersions = bool.booleanValue();
        PythonPluginsEngine.Updater.checkUpdates();
    }
}
