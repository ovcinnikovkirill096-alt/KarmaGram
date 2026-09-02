package com.exteragram.messenger.preferences;

import android.content.Context;
import android.content.SharedPreferences;
import android.view.View;
import android.widget.FrameLayout;
import com.exteragram.messenger.ExteraConfig;
import com.exteragram.messenger.plugins.PluginsController;
import com.exteragram.messenger.plugins.ui.PluginsActivity;
import com.exteragram.messenger.preferences.appearance.AppearancePreferencesActivity;
import com.exteragram.messenger.preferences.chats.ChatsPreferencesActivity;
import com.exteragram.messenger.preferences.components.HeaderSettingsCell;
import com.exteragram.messenger.utils.system.VibratorUtils;
import com.radolyn.ayugram.AyuConfig;
import com.radolyn.ayugram.ui.AlertUtils;
import java.util.ArrayList;
import org.telegram.messenger.AndroidUtilities;
import org.telegram.messenger.BuildVars;
import org.telegram.messenger.LocaleController;
import org.telegram.messenger.R;
import org.telegram.messenger.browser.Browser;
import org.telegram.ui.ActionBar.ActionBar;
import org.telegram.ui.ActionBar.Theme;
import org.telegram.ui.Components.LayoutHelper;
import org.telegram.ui.Components.UItem;
import org.telegram.ui.Components.UniversalAdapter;
import org.telegram.ui.LaunchActivity;

public class MainPreferencesActivity extends BasePreferencesActivity {
    private HeaderSettingsCell headerSettingsCell;

    @Override // com.exteragram.messenger.preferences.BasePreferencesActivity
    protected boolean hasHeaderCell() {
        return true;
    }

    @Override // com.exteragram.messenger.preferences.BasePreferencesActivity
    protected boolean hasWhiteActionBar() {
        return true;
    }

    public enum PreferenceItem {
        HEADER_CELL,
        GENERAL_CATEGORY,
        APPEARANCE_CATEGORY,
        CHATS_CATEGORY,
        PLUGINS_CATEGORY,
        OTHER_CATEGORY,
        CHANNEL,
        CHAT,
        CROWDIN,
        WEBSITE;

        public int getId() {
            return ordinal() + 1;
        }
    }

    @Override // com.exteragram.messenger.preferences.BasePreferencesActivity
    public String getTitle() {
        return LocaleController.getString(R.string.Preferences);
    }

    @Override // com.exteragram.messenger.preferences.BasePreferencesActivity, org.telegram.ui.ActionBar.BaseFragment
    public View createView(Context context) {
        View viewCreateView = super.createView(context);
        this.headerSettingsCell = new HeaderSettingsCell(context);
        this.actionBar.setBackground(null);
        ActionBar actionBar = this.actionBar;
        int i = Theme.key_windowBackgroundWhiteBlackText;
        actionBar.setTitleColor(Theme.getColor(i));
        this.actionBar.setItemsColor(Theme.getColor(i), false);
        this.actionBar.setItemsBackgroundColor(Theme.getColor(Theme.key_listSelector), false);
        this.actionBar.setCastShadows(false);
        this.actionBar.setAddToContainer(false);
        this.actionBar.getTitleTextView().setAlpha(0.0f);
        if (viewCreateView instanceof FrameLayout) {
            ((FrameLayout) viewCreateView).addView(this.actionBar, LayoutHelper.createFrame(-1, -2.0f));
        }
        this.fragmentView = viewCreateView;
        return viewCreateView;
    }

    @Override // com.exteragram.messenger.preferences.BasePreferencesActivity
    protected void fillItems(ArrayList arrayList, UniversalAdapter universalAdapter) {
        arrayList.add(UItem.asCustomShadow(this.headerSettingsCell, 198));
        arrayList.add(UItem.asHeader(LocaleController.getString(R.string.Categories)));
        arrayList.add(UItem.asButton(PreferenceItem.GENERAL_CATEGORY.getId(), R.drawable.msg_media, LocaleController.getString(R.string.General)).setSearchable(this).setLinkAlias("general", this));
        arrayList.add(UItem.asButton(PreferenceItem.APPEARANCE_CATEGORY.getId(), R.drawable.msg_theme, LocaleController.getString(R.string.Appearance)).setSearchable(this).setLinkAlias("appearance", this));
        arrayList.add(UItem.asButton(PreferenceItem.CHATS_CATEGORY.getId(), R.drawable.msg_discussion, LocaleController.getString(R.string.SearchAllChatsShort)).setSearchable(this).setLinkAlias("chats", this));
        if (PluginsController.isPluginEngineSupported()) {
            arrayList.add(UItem.asButton(PreferenceItem.PLUGINS_CATEGORY.getId(), R.drawable.msg_plugins, LocaleController.getString(R.string.Plugins)).setSearchable(this).setLinkAlias("plugins", this));
        }
        arrayList.add(UItem.asButton(PreferenceItem.OTHER_CATEGORY.getId(), R.drawable.msg_fave, LocaleController.getString(R.string.LocalOther)).setSearchable(this).setLinkAlias("other", this));
        arrayList.add(UItem.asShadow());
        arrayList.add(UItem.asHeader(LocaleController.getString(R.string.Links)));
        arrayList.add(UItem.asButton(PreferenceItem.CHANNEL.getId(), R.drawable.msg_channel, LocaleController.getString(R.string.ProfileChannel), "@exteraGram").setSearchable(this).setLinkAlias("channel", this));
        arrayList.add(UItem.asButton(PreferenceItem.CHAT.getId(), R.drawable.msg_groups, LocaleController.getString(R.string.SearchAllChatsShort), "@exteraChat").setSearchable(this).setLinkAlias("chat", this));
        arrayList.add(UItem.asButton(PreferenceItem.CROWDIN.getId(), R.drawable.msg_translate, LocaleController.getString(R.string.Crowdin), "Crowdin").setSearchable(this).setLinkAlias("crowdin", this));
        arrayList.add(UItem.asButton(PreferenceItem.WEBSITE.getId(), R.drawable.msg_language, LocaleController.getString(R.string.Website), "exteraGram.app").showDivider(false).setSearchable(this).setLinkAlias("website", this));
        arrayList.add(UItem.asShadow());
    }

    @Override // com.exteragram.messenger.preferences.BasePreferencesActivity
    protected void onClick(UItem uItem, View view, int i, float f, float f2) {
        int i2 = uItem.id;
        if (i2 <= 0 || i2 > PreferenceItem.values().length) {
            return;
        }
        switch (AnonymousClass1.$SwitchMap$com$exteragram$messenger$preferences$MainPreferencesActivity$PreferenceItem[PreferenceItem.values()[uItem.id - 1].ordinal()]) {
            case 1:
                if (!BuildVars.PM_BUILD) {
                    ((LaunchActivity) getParentActivity()).checkAppUpdate(true);
                }
                break;
            case 2:
                presentFragment(new GeneralPreferencesActivity());
                break;
            case 3:
                presentFragment(new AppearancePreferencesActivity());
                break;
            case 4:
                presentFragment(new ChatsPreferencesActivity());
                break;
            case 5:
                presentFragment(new PluginsActivity());
                break;
            case 6:
                presentFragment(new OtherPreferencesActivity());
                break;
            case 7:
                showNoticeBeforeOpeningChat("exteraGram");
                break;
            case 8:
                showNoticeBeforeOpeningChat("exteraChat");
                break;
            case 9:
                Browser.openUrl(getParentActivity(), "https://crowdin.com/project/exteralocales");
                break;
            case 10:
                Browser.openUrl(getParentActivity(), "https://exteraGram.app");
                break;
        }
    }

    /* JADX INFO: renamed from: com.exteragram.messenger.preferences.MainPreferencesActivity$1, reason: invalid class name */
    static /* synthetic */ class AnonymousClass1 {
        static final /* synthetic */ int[] $SwitchMap$com$exteragram$messenger$preferences$MainPreferencesActivity$PreferenceItem;

        static {
            int[] iArr = new int[PreferenceItem.values().length];
            $SwitchMap$com$exteragram$messenger$preferences$MainPreferencesActivity$PreferenceItem = iArr;
            try {
                iArr[PreferenceItem.HEADER_CELL.ordinal()] = 1;
            } catch (NoSuchFieldError unused) {
            }
            try {
                $SwitchMap$com$exteragram$messenger$preferences$MainPreferencesActivity$PreferenceItem[PreferenceItem.GENERAL_CATEGORY.ordinal()] = 2;
            } catch (NoSuchFieldError unused2) {
            }
            try {
                $SwitchMap$com$exteragram$messenger$preferences$MainPreferencesActivity$PreferenceItem[PreferenceItem.APPEARANCE_CATEGORY.ordinal()] = 3;
            } catch (NoSuchFieldError unused3) {
            }
            try {
                $SwitchMap$com$exteragram$messenger$preferences$MainPreferencesActivity$PreferenceItem[PreferenceItem.CHATS_CATEGORY.ordinal()] = 4;
            } catch (NoSuchFieldError unused4) {
            }
            try {
                $SwitchMap$com$exteragram$messenger$preferences$MainPreferencesActivity$PreferenceItem[PreferenceItem.PLUGINS_CATEGORY.ordinal()] = 5;
            } catch (NoSuchFieldError unused5) {
            }
            try {
                $SwitchMap$com$exteragram$messenger$preferences$MainPreferencesActivity$PreferenceItem[PreferenceItem.OTHER_CATEGORY.ordinal()] = 6;
            } catch (NoSuchFieldError unused6) {
            }
            try {
                $SwitchMap$com$exteragram$messenger$preferences$MainPreferencesActivity$PreferenceItem[PreferenceItem.CHANNEL.ordinal()] = 7;
            } catch (NoSuchFieldError unused7) {
            }
            try {
                $SwitchMap$com$exteragram$messenger$preferences$MainPreferencesActivity$PreferenceItem[PreferenceItem.CHAT.ordinal()] = 8;
            } catch (NoSuchFieldError unused8) {
            }
            try {
                $SwitchMap$com$exteragram$messenger$preferences$MainPreferencesActivity$PreferenceItem[PreferenceItem.CROWDIN.ordinal()] = 9;
            } catch (NoSuchFieldError unused9) {
            }
            try {
                $SwitchMap$com$exteragram$messenger$preferences$MainPreferencesActivity$PreferenceItem[PreferenceItem.WEBSITE.ordinal()] = 10;
            } catch (NoSuchFieldError unused10) {
            }
        }
    }

    @Override // com.exteragram.messenger.preferences.BasePreferencesActivity
    protected boolean onLongClick(UItem uItem, View view, int i, float f, float f2) {
        int i2 = uItem.id;
        if (i2 <= 0 || i2 > PreferenceItem.values().length) {
            return false;
        }
        if (PreferenceItem.values()[uItem.id - 1] == PreferenceItem.HEADER_CELL) {
            SharedPreferences.Editor editor = ExteraConfig.editor;
            boolean z = !ExteraConfig.useSystemIconShape;
            ExteraConfig.useSystemIconShape = z;
            editor.putBoolean("useSystemIconShape", z).apply();
            view.performHapticFeedback(VibratorUtils.getType(3), 1);
            view.invalidate();
            return true;
        }
        return super.onLongClick(uItem, view, i, f, f2);
    }

    @Override // com.exteragram.messenger.preferences.BasePreferencesActivity, org.telegram.ui.ActionBar.BaseFragment
    public void onInsets(int i, int i2, int i3, int i4) {
        super.onInsets(i, i2, i3, i4);
        this.listView.setPadding(0, i2 + AndroidUtilities.dp(12.0f), 0, i4);
    }

    private void showNoticeBeforeOpeningChat(String str) {
        if (AyuConfig.sawExteraChatsAlert) {
            getMessagesController().openByUserName(str, this, 1);
        } else {
            AlertUtils.showExteraChatsAlert(this, str);
        }
    }
}
