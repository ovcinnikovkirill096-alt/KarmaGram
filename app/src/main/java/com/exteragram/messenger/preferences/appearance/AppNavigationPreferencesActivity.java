package com.exteragram.messenger.preferences.appearance;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.view.View;
import androidx.core.content.ContextCompat;
import com.exteragram.messenger.ExteraConfig;
import com.exteragram.messenger.plugins.PluginsController;
import com.exteragram.messenger.preferences.BasePreferencesActivity;
import com.exteragram.messenger.utils.ui.PopupUtils;
import com.google.android.exoplayer2.util.Consumer;
import j$.util.Collection;
import j$.util.function.Predicate$CC;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.function.Predicate;
import org.telegram.messenger.AndroidUtilities;
import org.telegram.messenger.LocaleController;
import org.telegram.messenger.MessagesController;
import org.telegram.messenger.NotificationCenter;
import org.telegram.messenger.R;
import org.telegram.messenger.SharedConfig;
import org.telegram.messenger.Utilities;
import org.telegram.ui.ActionBar.ActionBarMenuItem;
import org.telegram.ui.Components.BulletinFactory;
import org.telegram.ui.Components.UItem;
import org.telegram.ui.Components.UniversalAdapter;
import org.telegram.ui.Components.UniversalRecyclerView;

public class AppNavigationPreferencesActivity extends BasePreferencesActivity {
    private CharSequence[] bottomNavigationModes;
    private Drawable reorderIcon;
    private ActionBarMenuItem resetItem;
    private CharSequence[] tabletMode;
    private final HashMap itemDetails = new HashMap();
    private final ArrayList stableDividerIds = new ArrayList();
    private int nextDividerId = -2000;

    public enum AppNavigationItem {
        DRAWER,
        IMMERSIVE_ANIMATION,
        BOTTOM_NAVIGATION_BAR_MODE,
        PREDICTIVE_BACK_ANIMATION,
        SPRING_ANIMATIONS,
        TABLET_MODE;

        public int getId() {
            return ordinal() + 150;
        }

        public static AppNavigationItem fromId(int i) {
            int i2 = i - 150;
            if (i2 < 0 || i2 >= values().length) {
                return null;
            }
            return values()[i2];
        }
    }

    private static class ItemInfo {
        int iconRes;
        CharSequence name;

        ItemInfo(CharSequence charSequence, int i) {
            this.name = charSequence;
            this.iconRes = i;
        }
    }

    @Override // com.exteragram.messenger.preferences.BasePreferencesActivity
    protected void initializeOptionStrings() {
        initItemDetails();
        this.tabletMode = new CharSequence[]{LocaleController.getString(R.string.DistanceUnitsAutomatic), LocaleController.getString(R.string.PasswordOn), LocaleController.getString(R.string.PasswordOff)};
        this.bottomNavigationModes = new CharSequence[]{LocaleController.getString(R.string.BottomNavigationModeShow), LocaleController.getString(R.string.BottomNavigationModeHide), LocaleController.getString(R.string.BottomNavigationModeFloating)};
    }

    private void initItemDetails() {
        this.itemDetails.put(Integer.valueOf(ExteraConfig.MainMenuItem.GHOST_MODE.id), new ItemInfo(LocaleController.getString(R.string.EnableGhostMode), R.drawable.ayu_ghost));
        this.itemDetails.put(Integer.valueOf(ExteraConfig.MainMenuItem.KILL_APP.id), new ItemInfo(LocaleController.getString(R.string.KillApp), ExteraConfig.iconPack == 2 ? R.drawable.msg_block_remix : R.drawable.msg_disable));
        this.itemDetails.put(Integer.valueOf(ExteraConfig.MainMenuItem.PROFILE.id), new ItemInfo(LocaleController.getString(R.string.MyProfile), R.drawable.left_status_profile));
        this.itemDetails.put(Integer.valueOf(ExteraConfig.MainMenuItem.ARCHIVE.id), new ItemInfo(LocaleController.getString(R.string.ArchivedChats), R.drawable.msg_archive));
        this.itemDetails.put(Integer.valueOf(ExteraConfig.MainMenuItem.BOTS.id), new ItemInfo(LocaleController.getString(R.string.FilterBots), R.drawable.msg_bot));
        this.itemDetails.put(Integer.valueOf(ExteraConfig.MainMenuItem.NEW_GROUP.id), new ItemInfo(LocaleController.getString(R.string.NewGroup), R.drawable.msg_groups));
        this.itemDetails.put(Integer.valueOf(ExteraConfig.MainMenuItem.CONTACTS.id), new ItemInfo(LocaleController.getString(R.string.Contacts), R.drawable.msg_contacts));
        this.itemDetails.put(Integer.valueOf(ExteraConfig.MainMenuItem.NEW_CHANNEL.id), new ItemInfo(LocaleController.getString(R.string.NewChannel), R.drawable.msg_channel));
        this.itemDetails.put(Integer.valueOf(ExteraConfig.MainMenuItem.CALLS.id), new ItemInfo(LocaleController.getString(R.string.Calls), R.drawable.msg_calls));
        this.itemDetails.put(Integer.valueOf(ExteraConfig.MainMenuItem.SAVED.id), new ItemInfo(LocaleController.getString(R.string.SavedMessages), R.drawable.msg_saved));
        this.itemDetails.put(Integer.valueOf(ExteraConfig.MainMenuItem.SETTINGS.id), new ItemInfo(LocaleController.getString(R.string.Settings), R.drawable.msg_settings_old));
        this.itemDetails.put(Integer.valueOf(ExteraConfig.MainMenuItem.PLUGINS.id), new ItemInfo(LocaleController.getString(R.string.Plugins), R.drawable.msg_plugins));
        this.itemDetails.put(Integer.valueOf(ExteraConfig.MainMenuItem.BROWSER.id), new ItemInfo(LocaleController.getString(R.string.BrowserSettingsTitle), R.drawable.msg2_language));
        this.itemDetails.put(Integer.valueOf(ExteraConfig.MainMenuItem.QR.id), new ItemInfo(LocaleController.getString(R.string.AuthAnotherClient), R.drawable.msg_qrcode));
        Collection.EL.removeIf(ExteraConfig.mainMenuHiddenItems, new Predicate() { // from class: com.exteragram.messenger.preferences.appearance.AppNavigationPreferencesActivity$$ExternalSyntheticLambda6
            public /* synthetic */ Predicate and(Predicate predicate) {
                return Predicate$CC.$default$and(this, predicate);
            }

            public /* synthetic */ Predicate negate() {
                return Predicate$CC.$default$negate(this);
            }

            public /* synthetic */ Predicate or(Predicate predicate) {
                return Predicate$CC.$default$or(this, predicate);
            }

            @Override // java.util.function.Predicate
            public final boolean test(Object obj) {
                return AppNavigationPreferencesActivity.$r8$lambda$Po3A4RwV39YnktoT4g7JvvXtYv0((Integer) obj);
            }
        });
        this.stableDividerIds.clear();
        this.nextDividerId = -2000;
        ArrayList arrayList = ExteraConfig.mainMenuLayout;
        int size = arrayList.size();
        int i = 0;
        while (i < size) {
            Object obj = arrayList.get(i);
            i++;
            if (((Integer) obj).intValue() == ExteraConfig.MainMenuItem.DIVIDER.id) {
                ArrayList arrayList2 = this.stableDividerIds;
                int i2 = this.nextDividerId;
                this.nextDividerId = i2 - 1;
                arrayList2.add(Integer.valueOf(i2));
            }
        }
    }

    public static /* synthetic */ boolean $r8$lambda$Po3A4RwV39YnktoT4g7JvvXtYv0(Integer num) {
        return num.intValue() == ExteraConfig.MainMenuItem.DIVIDER.id;
    }

    @Override // com.exteragram.messenger.preferences.BasePreferencesActivity
    public String getTitle() {
        return LocaleController.getString(R.string.AppNavigation);
    }

    @Override // com.exteragram.messenger.preferences.BasePreferencesActivity, org.telegram.ui.ActionBar.BaseFragment
    public View createView(Context context) {
        View viewCreateView = super.createView(context);
        ActionBarMenuItem actionBarMenuItemAddItem = this.actionBar.createMenu().addItem(0, R.drawable.msg_reset);
        this.resetItem = actionBarMenuItemAddItem;
        actionBarMenuItemAddItem.setContentDescription(LocaleController.getString(R.string.Reset));
        updateResetButtonVisibility();
        this.resetItem.setOnClickListener(new View.OnClickListener() { // from class: com.exteragram.messenger.preferences.appearance.AppNavigationPreferencesActivity$$ExternalSyntheticLambda7
            @Override // android.view.View.OnClickListener
            public final void onClick(View view) {
                this.f$0.lambda$createView$1(view);
            }
        });
        UniversalRecyclerView universalRecyclerView = this.listView;
        if (universalRecyclerView != null) {
            universalRecyclerView.allowReorder(true);
            this.listView.listenReorder(new Utilities.Callback2() { // from class: com.exteragram.messenger.preferences.appearance.AppNavigationPreferencesActivity$$ExternalSyntheticLambda8
                @Override // org.telegram.messenger.Utilities.Callback2
                public final void run(Object obj, Object obj2) {
                    this.f$0.updateConfigFromReorder(((Integer) obj).intValue(), (ArrayList) obj2);
                }
            });
        }
        return viewCreateView;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$createView$1(View view) {
        resetToDefault();
    }

    @Override // com.exteragram.messenger.preferences.BasePreferencesActivity
    protected void fillItems(ArrayList arrayList, UniversalAdapter universalAdapter) {
        if (this.reorderIcon == null) {
            this.reorderIcon = ContextCompat.getDrawable(getContext(), R.drawable.list_reorder);
        }
        arrayList.add(UItem.asHeader(LocaleController.getString(R.string.General)));
        arrayList.add(UItem.asButton(AppNavigationItem.TABLET_MODE.getId(), LocaleController.getString(R.string.TabletMode), this.tabletMode[ExteraConfig.tabletMode]).setSearchable(this).setLinkAlias("tabletMode", this));
        arrayList.add(UItem.asButton(AppNavigationItem.BOTTOM_NAVIGATION_BAR_MODE.getId(), LocaleController.getString(R.string.BottomNavigationBarMode), this.bottomNavigationModes[ExteraConfig.BottomNavigationBar.getMode()]).setSearchable(this).setLinkAlias("bottomNavigationBarMode", this));
        if (Build.VERSION.SDK_INT >= 34) {
            arrayList.add(UItem.asCheck(AppNavigationItem.PREDICTIVE_BACK_ANIMATION.getId(), LocaleController.getString(R.string.PredictiveBackAnimation)).setChecked(ExteraConfig.predictiveBackAnimation).setSearchable(this).setLinkAlias("predictiveBackAnimation", this));
        }
        arrayList.add(UItem.asCheck(AppNavigationItem.SPRING_ANIMATIONS.getId(), LocaleController.getString(R.string.SpringAnimations)).setChecked(ExteraConfig.springAnimations).setSearchable(this).setLinkAlias("springAnimations", this));
        arrayList.add(UItem.asShadow(LocaleController.getString(R.string.SpringAnimationsInfo)));
        arrayList.add(UItem.asHeader(LocaleController.getString(R.string.AppNavigation)));
        arrayList.add(UItem.asCheck(AppNavigationItem.DRAWER.getId(), LocaleController.getString(R.string.NavigationDrawer)).setChecked(ExteraConfig.navigationDrawer).setSearchable(this).setLinkAlias("navigationDrawer", this));
        if (ExteraConfig.navigationDrawer) {
            arrayList.add(UItem.asCheck(AppNavigationItem.IMMERSIVE_ANIMATION.getId(), LocaleController.getString(R.string.NavigationDrawerImmersiveAnimation)).setChecked(ExteraConfig.immersiveDrawerAnimation).setSearchable(this).setLinkAlias("immersiveDrawerAnimation", this));
        }
        arrayList.add(UItem.asShadow(LocaleController.getString(R.string.NavigationDrawerInfo)));
        addMenuSection(arrayList, universalAdapter, LocaleController.getString(R.string.MainMenuItems), ExteraConfig.mainMenuLayout, true);
        arrayList.add(UItem.asShadow(LocaleController.getString(R.string.MainMenuItemsInfo)));
        if (ExteraConfig.mainMenuHiddenItems.isEmpty()) {
            return;
        }
        addMenuSection(arrayList, universalAdapter, LocaleController.getString(R.string.MainMenuHiddenItems), ExteraConfig.mainMenuHiddenItems, false);
        arrayList.add(UItem.asShadow(null));
    }

    private void addMenuSection(ArrayList arrayList, UniversalAdapter universalAdapter, String str, ArrayList arrayList2, boolean z) {
        universalAdapter.whiteSectionStart();
        arrayList.add(UItem.asHeader(str));
        universalAdapter.reorderSectionStart();
        int size = arrayList2.size();
        int i = 0;
        int i2 = 0;
        while (i2 < size) {
            Object obj = arrayList2.get(i2);
            i2++;
            Integer num = (Integer) obj;
            if (num.intValue() != ExteraConfig.MainMenuItem.PLUGINS.id || PluginsController.isPluginEngineSupported()) {
                int iIntValue = num.intValue();
                ExteraConfig.MainMenuItem mainMenuItem = ExteraConfig.MainMenuItem.DIVIDER;
                if (iIntValue == mainMenuItem.id) {
                    if (z && i < this.stableDividerIds.size()) {
                        arrayList.add(createMenuItem(((Integer) this.stableDividerIds.get(i)).intValue(), null));
                    } else if (!z) {
                        arrayList.add(createMenuItem(mainMenuItem.id, null));
                    }
                    i++;
                } else {
                    ItemInfo itemInfo = (ItemInfo) this.itemDetails.get(num);
                    if (itemInfo != null) {
                        arrayList.add(createMenuItem(num.intValue(), itemInfo));
                    }
                }
            }
        }
        universalAdapter.reorderSectionEnd();
        if (z) {
            arrayList.add(UItem.asButton(-200, R.drawable.msg_add, LocaleController.getString(R.string.MainMenuAddDivider)).accent());
        }
        universalAdapter.whiteSectionEnd();
    }

    private UItem createMenuItem(int i, ItemInfo itemInfo) {
        UItem uItemAsButton;
        if (i <= -2000 || i == ExteraConfig.MainMenuItem.DIVIDER.id) {
            uItemAsButton = UItem.asButton(i, R.drawable.msg_block, LocaleController.getString(R.string.MainMenuDivider));
        } else {
            if (itemInfo == null) {
                return null;
            }
            uItemAsButton = UItem.asButton(i, itemInfo.iconRes, itemInfo.name);
        }
        uItemAsButton.object2 = this.reorderIcon;
        return uItemAsButton;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void updateConfigFromReorder(int i, ArrayList arrayList) {
        ArrayList arrayList2 = new ArrayList();
        ArrayList arrayList3 = new ArrayList();
        int size = arrayList.size();
        int i2 = 0;
        while (i2 < size) {
            Object obj = arrayList.get(i2);
            i2++;
            UItem uItem = (UItem) obj;
            int i3 = uItem.id;
            if (i3 > -2000 && i3 != ExteraConfig.MainMenuItem.DIVIDER.id) {
                arrayList2.add(Integer.valueOf(i3));
            } else if (i == 0) {
                arrayList2.add(Integer.valueOf(ExteraConfig.MainMenuItem.DIVIDER.id));
                int i4 = uItem.id;
                if (i4 > -2000) {
                    i4 = this.nextDividerId;
                    this.nextDividerId = i4 - 1;
                }
                arrayList3.add(Integer.valueOf(i4));
            }
        }
        if (i == 0) {
            this.stableDividerIds.clear();
            this.stableDividerIds.addAll(arrayList3);
            if (ExteraConfig.BottomNavigationBar.hidden()) {
                Integer numValueOf = Integer.valueOf(ExteraConfig.MainMenuItem.SETTINGS.id);
                if (!arrayList2.contains(numValueOf)) {
                    arrayList2.add(numValueOf);
                }
            }
            ExteraConfig.mainMenuLayout.clear();
            ExteraConfig.mainMenuLayout.addAll(arrayList2);
        } else if (i == 1) {
            if (ExteraConfig.BottomNavigationBar.hidden()) {
                arrayList2.remove(Integer.valueOf(ExteraConfig.MainMenuItem.SETTINGS.id));
            }
            ExteraConfig.mainMenuHiddenItems.clear();
            ExteraConfig.mainMenuHiddenItems.addAll(arrayList2);
        }
        saveAndNotify();
    }

    @Override // com.exteragram.messenger.preferences.BasePreferencesActivity
    protected void onClick(UItem uItem, View view, int i, float f, float f2) {
        UniversalAdapter universalAdapter;
        int i2 = uItem.id;
        AppNavigationItem appNavigationItemFromId = AppNavigationItem.fromId(i2);
        int i3 = 0;
        if (appNavigationItemFromId != null) {
            switch (AnonymousClass1.$SwitchMap$com$exteragram$messenger$preferences$appearance$AppNavigationPreferencesActivity$AppNavigationItem[appNavigationItemFromId.ordinal()]) {
                case 1:
                    toggleBooleanSettingAndRefresh("navigationDrawer", uItem, new Consumer() { // from class: com.exteragram.messenger.preferences.appearance.AppNavigationPreferencesActivity$$ExternalSyntheticLambda0
                        @Override // com.google.android.exoplayer2.util.Consumer
                        public final void accept(Object obj) {
                            ExteraConfig.navigationDrawer = ((Boolean) obj).booleanValue();
                        }
                    });
                    getNotificationCenter().lambda$postNotificationNameOnUIThread$1(NotificationCenter.mainUserInfoChanged, new Object[0]);
                    UniversalRecyclerView universalRecyclerView = this.listView;
                    if (universalRecyclerView != null && (universalAdapter = universalRecyclerView.adapter) != null) {
                        universalAdapter.update(true);
                    }
                    this.parentLayout.rebuildFragments(0);
                    break;
                case 2:
                    toggleBooleanSettingAndRefresh("immersiveDrawerAnimation", uItem, new Consumer() { // from class: com.exteragram.messenger.preferences.appearance.AppNavigationPreferencesActivity$$ExternalSyntheticLambda1
                        @Override // com.google.android.exoplayer2.util.Consumer
                        public final void accept(Object obj) {
                            ExteraConfig.immersiveDrawerAnimation = ((Boolean) obj).booleanValue();
                        }
                    });
                    break;
                case 3:
                    showListDialog(uItem, this.bottomNavigationModes, LocaleController.getString(R.string.BottomNavigationBarMode), ExteraConfig.BottomNavigationBar.getMode(), new PopupUtils.OnItemClickListener() { // from class: com.exteragram.messenger.preferences.appearance.AppNavigationPreferencesActivity$$ExternalSyntheticLambda2
                        @Override // com.exteragram.messenger.utils.ui.PopupUtils.OnItemClickListener
                        public final void onClick(int i4) {
                            this.f$0.lambda$onClick$4(i4);
                        }
                    });
                    break;
                case 4:
                    toggleBooleanSettingAndRefresh("predictiveBackAnimation", uItem, new Consumer() { // from class: com.exteragram.messenger.preferences.appearance.AppNavigationPreferencesActivity$$ExternalSyntheticLambda3
                        @Override // com.google.android.exoplayer2.util.Consumer
                        public final void accept(Object obj) {
                            ExteraConfig.predictiveBackAnimation = ((Boolean) obj).booleanValue();
                        }
                    });
                    showRestartBulletin();
                    break;
                case 5:
                    toggleBooleanSettingAndRefresh("springAnimations", uItem, new Consumer() { // from class: com.exteragram.messenger.preferences.appearance.AppNavigationPreferencesActivity$$ExternalSyntheticLambda4
                        @Override // com.google.android.exoplayer2.util.Consumer
                        public final void accept(Object obj) {
                            AppNavigationPreferencesActivity.m1338$r8$lambda$u07TReZ5mBL6eLs_1kcuhUW0_k((Boolean) obj);
                        }
                    });
                    break;
                case 6:
                    showListDialog(uItem, this.tabletMode, LocaleController.getString(R.string.TabletMode), ExteraConfig.tabletMode, new PopupUtils.OnItemClickListener() { // from class: com.exteragram.messenger.preferences.appearance.AppNavigationPreferencesActivity$$ExternalSyntheticLambda5
                        @Override // com.exteragram.messenger.utils.ui.PopupUtils.OnItemClickListener
                        public final void onClick(int i4) {
                            this.f$0.lambda$onClick$7(i4);
                        }
                    });
                    break;
            }
            return;
        }
        if (i2 == -200) {
            ArrayList arrayList = this.stableDividerIds;
            int i4 = this.nextDividerId;
            this.nextDividerId = i4 - 1;
            arrayList.add(Integer.valueOf(i4));
            ExteraConfig.mainMenuLayout.add(Integer.valueOf(ExteraConfig.MainMenuItem.DIVIDER.id));
            saveAndNotify();
            return;
        }
        if (i2 <= -2000) {
            int iIndexOf = this.stableDividerIds.indexOf(Integer.valueOf(i2));
            if (iIndexOf != -1) {
                int i5 = 0;
                while (true) {
                    if (i3 >= ExteraConfig.mainMenuLayout.size()) {
                        i3 = -1;
                        break;
                    }
                    if (((Integer) ExteraConfig.mainMenuLayout.get(i3)).intValue() == ExteraConfig.MainMenuItem.DIVIDER.id) {
                        if (i5 == iIndexOf) {
                            break;
                        } else {
                            i5++;
                        }
                    }
                    i3++;
                }
                if (i3 != -1) {
                    this.stableDividerIds.remove(iIndexOf);
                    ExteraConfig.mainMenuLayout.remove(i3);
                    saveAndNotify();
                    return;
                }
                return;
            }
            return;
        }
        ExteraConfig.MainMenuItem mainMenuItem = ExteraConfig.MainMenuItem.DIVIDER;
        if (i2 == mainMenuItem.id) {
            ExteraConfig.mainMenuHiddenItems.remove(Integer.valueOf(i2));
            saveAndNotify();
            return;
        }
        if (ExteraConfig.BottomNavigationBar.hidden() && i2 == ExteraConfig.MainMenuItem.SETTINGS.id && ExteraConfig.mainMenuLayout.contains(Integer.valueOf(i2))) {
            BulletinFactory.of(this).createErrorBulletin(LocaleController.getString(R.string.MainMenuRemoveSettingsInfo)).show();
            return;
        }
        if (ExteraConfig.mainMenuLayout.contains(Integer.valueOf(i2))) {
            ExteraConfig.mainMenuLayout.remove(Integer.valueOf(i2));
            if (i2 != mainMenuItem.id && !ExteraConfig.mainMenuHiddenItems.contains(Integer.valueOf(i2))) {
                ExteraConfig.mainMenuHiddenItems.add(0, Integer.valueOf(i2));
            }
        } else if (ExteraConfig.mainMenuHiddenItems.contains(Integer.valueOf(i2))) {
            ExteraConfig.mainMenuHiddenItems.remove(Integer.valueOf(i2));
            ExteraConfig.mainMenuLayout.add(Integer.valueOf(i2));
        }
        saveAndNotify();
    }

    /* JADX INFO: renamed from: com.exteragram.messenger.preferences.appearance.AppNavigationPreferencesActivity$1, reason: invalid class name */
    static /* synthetic */ class AnonymousClass1 {
        static final /* synthetic */ int[] $SwitchMap$com$exteragram$messenger$preferences$appearance$AppNavigationPreferencesActivity$AppNavigationItem;

        static {
            int[] iArr = new int[AppNavigationItem.values().length];
            $SwitchMap$com$exteragram$messenger$preferences$appearance$AppNavigationPreferencesActivity$AppNavigationItem = iArr;
            try {
                iArr[AppNavigationItem.DRAWER.ordinal()] = 1;
            } catch (NoSuchFieldError unused) {
            }
            try {
                $SwitchMap$com$exteragram$messenger$preferences$appearance$AppNavigationPreferencesActivity$AppNavigationItem[AppNavigationItem.IMMERSIVE_ANIMATION.ordinal()] = 2;
            } catch (NoSuchFieldError unused2) {
            }
            try {
                $SwitchMap$com$exteragram$messenger$preferences$appearance$AppNavigationPreferencesActivity$AppNavigationItem[AppNavigationItem.BOTTOM_NAVIGATION_BAR_MODE.ordinal()] = 3;
            } catch (NoSuchFieldError unused3) {
            }
            try {
                $SwitchMap$com$exteragram$messenger$preferences$appearance$AppNavigationPreferencesActivity$AppNavigationItem[AppNavigationItem.PREDICTIVE_BACK_ANIMATION.ordinal()] = 4;
            } catch (NoSuchFieldError unused4) {
            }
            try {
                $SwitchMap$com$exteragram$messenger$preferences$appearance$AppNavigationPreferencesActivity$AppNavigationItem[AppNavigationItem.SPRING_ANIMATIONS.ordinal()] = 5;
            } catch (NoSuchFieldError unused5) {
            }
            try {
                $SwitchMap$com$exteragram$messenger$preferences$appearance$AppNavigationPreferencesActivity$AppNavigationItem[AppNavigationItem.TABLET_MODE.ordinal()] = 6;
            } catch (NoSuchFieldError unused6) {
            }
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$onClick$4(int i) {
        boolean z = ExteraConfig.BottomNavigationBar.hidden() != (i == 1);
        changeIntSetting("bottomNavigationBarMode", i);
        ExteraConfig.BottomNavigationBar.setMode(i);
        if (z) {
            resetToDefault();
        } else {
            ExteraConfig.ensureSettingsVisibility();
            getNotificationCenter().lambda$postNotificationNameOnUIThread$1(NotificationCenter.mainUserInfoChanged, new Object[0]);
            updateResetButtonVisibility();
        }
        this.parentLayout.rebuildFragments(0);
    }

    /* JADX INFO: renamed from: $r8$lambda$u07TReZ5mBL6eLs-_1kcuhUW0_k, reason: not valid java name */
    public static /* synthetic */ void m1338$r8$lambda$u07TReZ5mBL6eLs_1kcuhUW0_k(Boolean bool) {
        ExteraConfig.springAnimations = bool.booleanValue();
        if (bool.booleanValue()) {
            MessagesController.getGlobalMainSettings().edit().putBoolean("view_animations", true).apply();
            SharedConfig.setAnimationsEnabled(true);
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$onClick$7(int i) {
        ExteraConfig.tabletMode = i;
        changeIntSetting("tabletMode", i);
        showRestartBulletin();
    }

    private void saveAndNotify() {
        ExteraConfig.saveMainMenuLayout();
        getNotificationCenter().lambda$postNotificationNameOnUIThread$1(NotificationCenter.mainUserInfoChanged, new Object[0]);
        refreshEditorList();
        updateResetButtonVisibility();
    }

    private void refreshEditorList() {
        UniversalRecyclerView universalRecyclerView = this.listView;
        if (universalRecyclerView == null || universalRecyclerView.adapter == null) {
            return;
        }
        universalRecyclerView.hideSelector(false);
        this.listView.cancelClickRunnables(false);
        this.listView.adapter.update(true);
    }

    private void updateResetButtonVisibility() {
        if (this.resetItem == null) {
            return;
        }
        boolean zEquals = ExteraConfig.mainMenuLayout.equals(ExteraConfig.getDefaultMainMenuLayout());
        if (!zEquals && this.resetItem.getVisibility() == 8) {
            AndroidUtilities.updateViewVisibilityAnimated(this.resetItem, true, 0.5f, true);
        } else if (zEquals && this.resetItem.getVisibility() == 0) {
            AndroidUtilities.updateViewVisibilityAnimated(this.resetItem, false, 0.5f, true);
        }
    }

    private void resetToDefault() {
        ExteraConfig.mainMenuLayout.clear();
        ExteraConfig.mainMenuLayout.addAll(ExteraConfig.getDefaultMainMenuLayout());
        ExteraConfig.mainMenuHiddenItems.clear();
        for (ExteraConfig.MainMenuItem mainMenuItem : ExteraConfig.MainMenuItem.values()) {
            if (mainMenuItem != ExteraConfig.MainMenuItem.DIVIDER && !ExteraConfig.mainMenuLayout.contains(Integer.valueOf(mainMenuItem.id)) && (mainMenuItem != ExteraConfig.MainMenuItem.PLUGINS || PluginsController.isPluginEngineSupported())) {
                ExteraConfig.mainMenuHiddenItems.add(Integer.valueOf(mainMenuItem.id));
            }
        }
        this.stableDividerIds.clear();
        this.nextDividerId = -2000;
        ArrayList arrayList = ExteraConfig.mainMenuLayout;
        int size = arrayList.size();
        int i = 0;
        while (i < size) {
            Object obj = arrayList.get(i);
            i++;
            if (((Integer) obj).intValue() == ExteraConfig.MainMenuItem.DIVIDER.id) {
                ArrayList arrayList2 = this.stableDividerIds;
                int i2 = this.nextDividerId;
                this.nextDividerId = i2 - 1;
                arrayList2.add(Integer.valueOf(i2));
            }
        }
        ExteraConfig.saveMainMenuLayout();
        getNotificationCenter().lambda$postNotificationNameOnUIThread$1(NotificationCenter.mainUserInfoChanged, new Object[0]);
        refreshEditorList();
        updateResetButtonVisibility();
    }
}
