package com.radolyn.ayugram.preferences;

import android.content.Context;
import android.view.View;
import android.widget.FrameLayout;
import com.exteragram.messenger.preferences.BasePreferencesActivity;
import com.exteragram.messenger.preferences.components.HeaderSettingsCell;
import com.exteragram.messenger.utils.system.VibratorUtils;
import java.util.ArrayList;
import org.lsposed.lsparanoid.Deobfuscator$AyuGram4A$TMessagesProj;
import org.telegram.messenger.AndroidUtilities;
import org.telegram.messenger.BuildVars;
import org.telegram.messenger.LocaleController;
import org.telegram.messenger.MessagesController;
import org.telegram.messenger.R;
import org.telegram.messenger.browser.Browser;
import org.telegram.ui.ActionBar.ActionBar;
import org.telegram.ui.ActionBar.Theme;
import org.telegram.ui.Components.LayoutHelper;
import org.telegram.ui.Components.UItem;
import org.telegram.ui.Components.UniversalAdapter;
import org.telegram.ui.LaunchActivity;

public class AyuMainPreferencesActivity extends BasePreferencesActivity {
    private HeaderSettingsCell headerSettingsCell;

    @Override // com.exteragram.messenger.preferences.BasePreferencesActivity
    protected boolean hasHeaderCell() {
        return true;
    }

    @Override // com.exteragram.messenger.preferences.BasePreferencesActivity
    protected boolean hasWhiteActionBar() {
        return true;
    }

    private enum PreferenceItem {
        HEADER_CELL,
        GHOST_MODE_CATEGORY,
        SPY_CATEGORY,
        FILTERS_CATEGORY,
        CUSTOMIZATION_CATEGORY,
        CHANNEL,
        CHAT,
        CROWDIN,
        DOCS;

        public int getId() {
            return ordinal() + 1;
        }
    }

    @Override // com.exteragram.messenger.preferences.BasePreferencesActivity
    public String getTitle() {
        return LocaleController.getString(R.string.AyuPreferences);
    }

    @Override // com.exteragram.messenger.preferences.BasePreferencesActivity, org.telegram.ui.ActionBar.BaseFragment
    public View createView(Context context) {
        View viewCreateView = super.createView(context);
        HeaderSettingsCell headerSettingsCell = new HeaderSettingsCell(context);
        this.headerSettingsCell = headerSettingsCell;
        headerSettingsCell.setIsAyuGram(true);
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
        arrayList.add(UItem.asButton(PreferenceItem.GHOST_MODE_CATEGORY.getId(), R.drawable.ayu_ghost, LocaleController.getString(R.string.CategoryGhostMode)).setSearchable(this).setLinkAlias("ghostMode", this));
        arrayList.add(UItem.asButton(PreferenceItem.SPY_CATEGORY.getId(), R.drawable.msg_bots, LocaleController.getString(R.string.CategorySpy)).setSearchable(this).setLinkAlias("spy", this));
        arrayList.add(UItem.asButton(PreferenceItem.FILTERS_CATEGORY.getId(), R.drawable.menu_tag_filter, LocaleController.getString(R.string.CategoryFilters)).setSearchable(this).setLinkAlias("filters", this));
        arrayList.add(UItem.asButton(PreferenceItem.CUSTOMIZATION_CATEGORY.getId(), R.drawable.msg_theme, LocaleController.getString(R.string.CategoryCustomization)).setSearchable(this).setLinkAlias("customization", this));
        arrayList.add(UItem.asShadow());
        arrayList.add(UItem.asHeader(LocaleController.getString(R.string.Links)));
        arrayList.add(UItem.asButton(PreferenceItem.CHANNEL.getId(), R.drawable.msg_channel, LocaleController.getString(R.string.ProfileChannel), "@ayugram"));
        arrayList.add(UItem.asButton(PreferenceItem.CHAT.getId(), R.drawable.msg_groups, LocaleController.getString(R.string.SearchAllChatsShort), "@ayugramchat"));
        arrayList.add(UItem.asButton(PreferenceItem.CROWDIN.getId(), R.drawable.msg_translate, LocaleController.getString(R.string.Crowdin), "Crowdin"));
        arrayList.add(UItem.asButton(PreferenceItem.DOCS.getId(), R.drawable.msg_language, LocaleController.getString(R.string.DocsText), "ayugram.one").showDivider(false));
    }

    @Override // com.exteragram.messenger.preferences.BasePreferencesActivity
    protected void onClick(UItem uItem, View view, int i, float f, float f2) {
        int i2;
        if (uItem != null && (i2 = uItem.id) > 0 && i2 <= PreferenceItem.values().length) {
            switch (AnonymousClass1.$SwitchMap$com$radolyn$ayugram$preferences$AyuMainPreferencesActivity$PreferenceItem[PreferenceItem.values()[uItem.id - 1].ordinal()]) {
                case 1:
                    if (!BuildVars.PM_BUILD) {
                        ((LaunchActivity) getParentActivity()).checkAppUpdate(true);
                    }
                    break;
                case 2:
                    presentFragment(new GhostModePreferencesActivity());
                    break;
                case 3:
                    presentFragment(new SpyPreferencesActivity());
                    break;
                case 4:
                    presentFragment(new FiltersPreferencesActivity());
                    break;
                case 5:
                    presentFragment(new CustomizationPreferencesActivity());
                    break;
                case 6:
                    MessagesController.getInstance(this.currentAccount).openByUserName("ayugram", this, 1);
                    break;
                case 7:
                    MessagesController.getInstance(this.currentAccount).openByUserName("ayugramchat", this, 1);
                    break;
                case 8:
                    Browser.openUrl(getParentActivity(), "https://translate.ayugram.one");
                    break;
                case 9:
                    Browser.openUrl(getParentActivity(), "https://docs.ayugram.one");
                    break;
            }
        }
    }

    /* JADX INFO: renamed from: com.radolyn.ayugram.preferences.AyuMainPreferencesActivity$1, reason: invalid class name */
    static /* synthetic */ class AnonymousClass1 {
        static final /* synthetic */ int[] $SwitchMap$com$radolyn$ayugram$preferences$AyuMainPreferencesActivity$PreferenceItem;

        static {
            int[] iArr = new int[PreferenceItem.values().length];
            $SwitchMap$com$radolyn$ayugram$preferences$AyuMainPreferencesActivity$PreferenceItem = iArr;
            try {
                iArr[PreferenceItem.HEADER_CELL.ordinal()] = 1;
            } catch (NoSuchFieldError unused) {
            }
            try {
                $SwitchMap$com$radolyn$ayugram$preferences$AyuMainPreferencesActivity$PreferenceItem[PreferenceItem.GHOST_MODE_CATEGORY.ordinal()] = 2;
            } catch (NoSuchFieldError unused2) {
            }
            try {
                $SwitchMap$com$radolyn$ayugram$preferences$AyuMainPreferencesActivity$PreferenceItem[PreferenceItem.SPY_CATEGORY.ordinal()] = 3;
            } catch (NoSuchFieldError unused3) {
            }
            try {
                $SwitchMap$com$radolyn$ayugram$preferences$AyuMainPreferencesActivity$PreferenceItem[PreferenceItem.FILTERS_CATEGORY.ordinal()] = 4;
            } catch (NoSuchFieldError unused4) {
            }
            try {
                $SwitchMap$com$radolyn$ayugram$preferences$AyuMainPreferencesActivity$PreferenceItem[PreferenceItem.CUSTOMIZATION_CATEGORY.ordinal()] = 5;
            } catch (NoSuchFieldError unused5) {
            }
            try {
                $SwitchMap$com$radolyn$ayugram$preferences$AyuMainPreferencesActivity$PreferenceItem[PreferenceItem.CHANNEL.ordinal()] = 6;
            } catch (NoSuchFieldError unused6) {
            }
            try {
                $SwitchMap$com$radolyn$ayugram$preferences$AyuMainPreferencesActivity$PreferenceItem[PreferenceItem.CHAT.ordinal()] = 7;
            } catch (NoSuchFieldError unused7) {
            }
            try {
                $SwitchMap$com$radolyn$ayugram$preferences$AyuMainPreferencesActivity$PreferenceItem[PreferenceItem.CROWDIN.ordinal()] = 8;
            } catch (NoSuchFieldError unused8) {
            }
            try {
                $SwitchMap$com$radolyn$ayugram$preferences$AyuMainPreferencesActivity$PreferenceItem[PreferenceItem.DOCS.ordinal()] = 9;
            } catch (NoSuchFieldError unused9) {
            }
        }
    }

    @Override // com.exteragram.messenger.preferences.BasePreferencesActivity
    protected boolean onLongClick(UItem uItem, View view, int i, float f, float f2) {
        if (uItem.view == this.headerSettingsCell) {
            presentFragment(new DebugPreferencesActivity());
            view.performHapticFeedback(VibratorUtils.getType(3), 1);
            return true;
        }
        return super.onLongClick(uItem, view, i, f, f2);
    }

    @Override // com.exteragram.messenger.preferences.BasePreferencesActivity, org.telegram.ui.ActionBar.BaseFragment
    public void onInsets(int i, int i2, int i3, int i4) {
        super.onInsets(i, i2, i3, i4);
        this.listView.setPadding(0, i2 + AndroidUtilities.dp(12.0f), 0, i4);
    }
}
