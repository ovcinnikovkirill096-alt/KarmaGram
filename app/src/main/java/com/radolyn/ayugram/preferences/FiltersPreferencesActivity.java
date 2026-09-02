package com.radolyn.ayugram.preferences;

import android.content.ClipboardManager;
import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;
import com.exteragram.messenger.preferences.BasePreferencesActivity;
import com.google.android.exoplayer2.util.Consumer;
import com.radolyn.ayugram.AyuConfig;
import com.radolyn.ayugram.AyuConstants;
import com.radolyn.ayugram.controllers.AyuFilterCacheController;
import com.radolyn.ayugram.database.AyuData;
import com.radolyn.ayugram.database.entities.RegexFilter;
import com.radolyn.ayugram.database.entities.RegexFilterGlobalExclusion;
import com.radolyn.ayugram.utils.AyuMessageUtils;
import com.radolyn.ayugram.utils.filters.AyuFilterUtils;
import de.robv.android.xposed.callbacks.XCallback;
import j$.util.Objects;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.MultipartBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.internal.url._UrlKt;
import org.telegram.messenger.AndroidUtilities;
import org.telegram.messenger.ContactsController;
import org.telegram.messenger.LocaleController;
import org.telegram.messenger.MessagesStorage;
import org.telegram.messenger.NotificationCenter;
import org.telegram.messenger.R;
import org.telegram.messenger.UserConfig;
import org.telegram.tgnet.TLObject;
import org.telegram.ui.ActionBar.ActionBar;
import org.telegram.ui.ActionBar.ActionBarMenuItem;
import org.telegram.ui.ActionBar.AlertDialog;
import org.telegram.ui.ActionBar.BottomSheet;
import org.telegram.ui.ActionBar.Theme;
import org.telegram.ui.Cells.EditTextSettingsCell;
import org.telegram.ui.Components.BulletinFactory;
import org.telegram.ui.Components.LayoutHelper;
import org.telegram.ui.Components.UItem;
import org.telegram.ui.Components.UniversalAdapter;
import org.telegram.ui.Components.UniversalRecyclerView;
import org.telegram.ui.DialogsActivity;
import org.telegram.ui.TopicsFragment;

public class FiltersPreferencesActivity extends BasePreferencesActivity implements NotificationCenter.NotificationCenterDelegate {
    private ActionBarMenuItem.Item exportItemItem;
    private final Map filtersByDialogId = new HashMap();
    private final Map idToDialog = new HashMap();
    private ActionBarMenuItem menuItem;
    private int sharedCount;

    private enum ItemId {
        HEADER_GENERAL,
        ENABLE,
        ENABLE_IN_CHATS,
        HIDE_FROM_BLOCKED,
        SHADOW_DIVIDER,
        SHARED_FILTERS,
        SHADOWBAN;

        public int getId() {
            return ordinal() + 1;
        }
    }

    private static class FilterItem {
        int minus;
        int plus;

        private FilterItem() {
        }
    }

    @Override // org.telegram.ui.ActionBar.BaseFragment
    public boolean onFragmentCreate() {
        super.onFragmentCreate();
        NotificationCenter.getGlobalInstance().addObserver(this, AyuConstants.PEER_RESOLVED_NOTIFICATION);
        return true;
    }

    @Override // org.telegram.ui.ActionBar.BaseFragment
    public void onFragmentDestroy() {
        NotificationCenter.getGlobalInstance().removeObserver(this, AyuConstants.PEER_RESOLVED_NOTIFICATION);
        super.onFragmentDestroy();
    }

    @Override // org.telegram.messenger.NotificationCenter.NotificationCenterDelegate
    public void didReceivedNotification(int i, int i2, Object... objArr) {
        UniversalRecyclerView universalRecyclerView;
        if (i != AyuConstants.PEER_RESOLVED_NOTIFICATION || (universalRecyclerView = this.listView) == null) {
            return;
        }
        universalRecyclerView.adapter.update(false);
    }

    @Override // com.exteragram.messenger.preferences.BasePreferencesActivity, org.telegram.ui.ActionBar.BaseFragment
    public View createView(Context context) {
        View viewCreateView = super.createView(context);
        ActionBarMenuItem actionBarMenuItemAddItem = this.actionBar.createMenu().addItem(0, R.drawable.ic_ab_other);
        this.menuItem = actionBarMenuItemAddItem;
        actionBarMenuItemAddItem.lazilyAddSubItem(1, R.drawable.msg_user_search, LocaleController.getString(R.string.FiltersMenuSelectChat));
        this.menuItem.lazilyAddColoredGap();
        this.menuItem.lazilyAddSubItem(2, R.drawable.msg_archive, LocaleController.getString(R.string.FiltersMenuImport));
        ActionBarMenuItem.Item itemLazilyAddSubItem = this.menuItem.lazilyAddSubItem(3, R.drawable.msg_unarchive, LocaleController.getString(R.string.FiltersMenuExport));
        this.exportItemItem = itemLazilyAddSubItem;
        itemLazilyAddSubItem.setVisibility(8);
        this.menuItem.lazilyAddColoredGap();
        ActionBarMenuItem.Item itemLazilyAddSubItem2 = this.menuItem.lazilyAddSubItem(4, R.drawable.msg_clear, LocaleController.getString(R.string.FiltersMenuClear));
        int i = Theme.key_color_red;
        itemLazilyAddSubItem2.setColors(Theme.getColor(i), Theme.getColor(i));
        this.menuItem.setSubMenuDelegate(new ActionBarMenuItem.ActionBarSubMenuItemDelegate() { // from class: com.radolyn.ayugram.preferences.FiltersPreferencesActivity.1
            @Override // org.telegram.ui.ActionBar.ActionBarMenuItem.ActionBarSubMenuItemDelegate
            public void onHideSubMenu() {
            }

            @Override // org.telegram.ui.ActionBar.ActionBarMenuItem.ActionBarSubMenuItemDelegate
            public void onShowSubMenu() {
                FiltersPreferencesActivity.this.exportItemItem.setVisibility((!FiltersPreferencesActivity.this.filtersByDialogId.isEmpty() || FiltersPreferencesActivity.this.sharedCount > 0) ? 0 : 8);
            }
        });
        this.actionBar.setActionBarMenuOnItemClick(new ActionBar.ActionBarMenuOnItemClick() { // from class: com.radolyn.ayugram.preferences.FiltersPreferencesActivity.2
            @Override // org.telegram.ui.ActionBar.ActionBar.ActionBarMenuOnItemClick
            public void onItemClick(int i2) {
                if (i2 == -1) {
                    FiltersPreferencesActivity.this.finishFragment();
                    return;
                }
                if (i2 == 1) {
                    FiltersPreferencesActivity.this.selectChat();
                    return;
                }
                if (i2 == 2) {
                    FiltersPreferencesActivity.this.importFilters();
                } else if (i2 == 3) {
                    FiltersPreferencesActivity.this.exportFilters();
                } else if (i2 == 4) {
                    FiltersPreferencesActivity.this.clearFilters();
                }
            }
        });
        return viewCreateView;
    }

    @Override // com.exteragram.messenger.preferences.BasePreferencesActivity
    public String getTitle() {
        return LocaleController.getString(R.string.RegexFilters);
    }

    @Override // com.exteragram.messenger.preferences.BasePreferencesActivity
    protected void fillItems(ArrayList arrayList, UniversalAdapter universalAdapter) {
        String string;
        this.sharedCount = 0;
        this.filtersByDialogId.clear();
        this.idToDialog.clear();
        List<RegexFilter> all = AyuData.getRegexFilterDao().getAll();
        List<RegexFilterGlobalExclusion> allExclusions = AyuData.getRegexFilterDao().getAllExclusions();
        Iterator<RegexFilter> it = all.iterator();
        while (true) {
            if (!it.hasNext()) {
                break;
            }
            RegexFilter next = it.next();
            Long l = next.dialogId;
            if (l == null) {
                this.sharedCount++;
            } else {
                FilterItem filterItem = (FilterItem) this.filtersByDialogId.get(l);
                if (filterItem == null) {
                    Map map = this.filtersByDialogId;
                    Long l2 = next.dialogId;
                    FilterItem filterItem2 = new FilterItem();
                    map.put(l2, filterItem2);
                    filterItem = filterItem2;
                }
                filterItem.plus++;
            }
        }
        for (RegexFilterGlobalExclusion regexFilterGlobalExclusion : allExclusions) {
            FilterItem filterItem3 = (FilterItem) this.filtersByDialogId.get(Long.valueOf(regexFilterGlobalExclusion.dialogId));
            if (filterItem3 == null) {
                Map map2 = this.filtersByDialogId;
                Long lValueOf = Long.valueOf(regexFilterGlobalExclusion.dialogId);
                FilterItem filterItem4 = new FilterItem();
                map2.put(lValueOf, filterItem4);
                filterItem3 = filterItem4;
            }
            filterItem3.minus++;
        }
        arrayList.add(UItem.asHeader(LocaleController.getString(R.string.General)));
        arrayList.add(UItem.asCheck(ItemId.ENABLE.getId(), LocaleController.getString(R.string.RegexFiltersEnable)).setChecked(AyuConfig.filtersEnabled).setSearchable(this).setLinkAlias("filtersEnabled", this));
        arrayList.add(UItem.asCheck(ItemId.ENABLE_IN_CHATS.getId(), LocaleController.getString(R.string.RegexFiltersEnableSharedInChats)).setChecked(AyuConfig.regexFiltersInChats).setSearchable(this).setLinkAlias("filtersInChats", this));
        arrayList.add(UItem.asCheck(ItemId.HIDE_FROM_BLOCKED.getId(), LocaleController.getString(R.string.FiltersHideFromBlocked)).setChecked(AyuConfig.hideFromBlocked).setSearchable(this).setLinkAlias("hideFromBlocked", this));
        arrayList.add(UItem.asShadow());
        arrayList.add(UItem.asAyuPeerCell(9998, UserConfig.getInstance(UserConfig.selectedAccount).getClientUserId(), LocaleController.getString(R.string.RegexFiltersShared), LocaleController.formatPluralString("RegexFiltersAmount", this.sharedCount, new Object[0])).setSearchable(this).setLinkAlias("sharedFilters", this));
        arrayList.add(UItem.asAyuPeerCell(9999, UserConfig.getInstance(UserConfig.selectedAccount).getClientUserId(), LocaleController.getString(R.string.FiltersShadowBan), LocaleController.formatPluralString("RegexFiltersAmount", AyuFilterUtils.getShadowBanList().size(), new Object[0])).setSearchable(this).setLinkAlias("shadowBanList", this));
        arrayList.add(UItem.asShadow());
        int i = 0;
        for (Map.Entry entry : this.filtersByDialogId.entrySet()) {
            Long l3 = (Long) entry.getKey();
            long jLongValue = l3.longValue();
            FilterItem filterItem5 = (FilterItem) entry.getValue();
            TLObject dialogInAnyWay = AyuMessageUtils.getDialogInAnyWay(jLongValue, Integer.valueOf(this.currentAccount));
            if (dialogInAnyWay != null) {
                string = ContactsController.formatName(dialogInAnyWay);
                if (TextUtils.isEmpty(string)) {
                    string = "?";
                }
            } else {
                string = Long.toString(jLongValue);
            }
            StringBuilder sb = new StringBuilder();
            int i2 = filterItem5.plus;
            if (i2 > 0) {
                sb.append(LocaleController.formatPluralString("RegexFiltersAmount", i2, new Object[0]));
            }
            if (filterItem5.plus > 0 && filterItem5.minus > 0) {
                sb.append(", ");
            }
            int i3 = filterItem5.minus;
            if (i3 > 0) {
                sb.append(LocaleController.formatPluralString("RegexFiltersExcludedAmount", i3, new Object[0]));
            }
            int i4 = i + XCallback.PRIORITY_HIGHEST;
            this.idToDialog.put(Integer.valueOf(i4), l3);
            arrayList.add(UItem.asAyuPeerCell(i4, jLongValue, string, sb));
            i++;
        }
    }

    @Override // com.exteragram.messenger.preferences.BasePreferencesActivity
    protected void onClick(UItem uItem, View view, int i, float f, float f2) {
        if (uItem == null) {
            return;
        }
        int i2 = uItem.id;
        if (i2 == 9998) {
            presentFragment(new FiltersListPreferencesActivity(null));
            return;
        }
        if (i2 == 9999) {
            presentFragment(new FiltersShadowBanPreferencesActivity());
            return;
        }
        if (i2 >= 10000) {
            Long l = (Long) this.idToDialog.get(Integer.valueOf(i2));
            if (l != null) {
                presentFragment(new FiltersListPreferencesActivity(l));
                return;
            }
            return;
        }
        if (i2 <= 0 || i2 > ItemId.values().length) {
            return;
        }
        int i3 = AnonymousClass4.$SwitchMap$com$radolyn$ayugram$preferences$FiltersPreferencesActivity$ItemId[ItemId.values()[uItem.id - 1].ordinal()];
        if (i3 == 1) {
            toggleBooleanSettingAndRefresh(AyuConfig.preferences, "filtersEnabled", uItem, new Consumer() { // from class: com.radolyn.ayugram.preferences.FiltersPreferencesActivity$$ExternalSyntheticLambda0
                @Override // com.google.android.exoplayer2.util.Consumer
                public final void accept(Object obj) {
                    AyuConfig.filtersEnabled = ((Boolean) obj).booleanValue();
                }
            });
            return;
        }
        if (i3 == 2) {
            toggleBooleanSettingAndRefresh(AyuConfig.preferences, "regexFiltersInChats", uItem, new Consumer() { // from class: com.radolyn.ayugram.preferences.FiltersPreferencesActivity$$ExternalSyntheticLambda1
                @Override // com.google.android.exoplayer2.util.Consumer
                public final void accept(Object obj) {
                    AyuConfig.regexFiltersInChats = ((Boolean) obj).booleanValue();
                }
            });
            return;
        }
        if (i3 != 3) {
            return;
        }
        toggleBooleanSettingAndRefresh(AyuConfig.preferences, "hideFromBlocked", uItem, new Consumer() { // from class: com.radolyn.ayugram.preferences.FiltersPreferencesActivity$$ExternalSyntheticLambda2
            @Override // com.google.android.exoplayer2.util.Consumer
            public final void accept(Object obj) {
                AyuConfig.hideFromBlocked = ((Boolean) obj).booleanValue();
            }
        });
        if (!AyuConfig.hideFromBlocked || AyuConfig.filtersEnabled) {
            return;
        }
        SharedPreferences.Editor editor = AyuConfig.editor;
        AyuConfig.filtersEnabled = true;
        editor.putBoolean("filtersEnabled", true).apply();
        this.listView.adapter.update(true);
        AyuFilterCacheController.rebuildCache();
        BulletinFactory.of(this).createSimpleBulletin(R.raw.info, LocaleController.getString(R.string.FiltersHideFromBlockedNote)).show();
    }

    /* JADX INFO: renamed from: com.radolyn.ayugram.preferences.FiltersPreferencesActivity$4, reason: invalid class name */
    static /* synthetic */ class AnonymousClass4 {
        static final /* synthetic */ int[] $SwitchMap$com$radolyn$ayugram$preferences$FiltersPreferencesActivity$ItemId;

        static {
            int[] iArr = new int[ItemId.values().length];
            $SwitchMap$com$radolyn$ayugram$preferences$FiltersPreferencesActivity$ItemId = iArr;
            try {
                iArr[ItemId.ENABLE.ordinal()] = 1;
            } catch (NoSuchFieldError unused) {
            }
            try {
                $SwitchMap$com$radolyn$ayugram$preferences$FiltersPreferencesActivity$ItemId[ItemId.ENABLE_IN_CHATS.ordinal()] = 2;
            } catch (NoSuchFieldError unused2) {
            }
            try {
                $SwitchMap$com$radolyn$ayugram$preferences$FiltersPreferencesActivity$ItemId[ItemId.HIDE_FROM_BLOCKED.ordinal()] = 3;
            } catch (NoSuchFieldError unused3) {
            }
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void selectChat() {
        Bundle bundle = new Bundle();
        bundle.putInt("dialogsType", 666);
        bundle.putBoolean("onlySelect", true);
        bundle.putBoolean("canSelectTopics", false);
        bundle.putBoolean("allowSwitchAccount", true);
        bundle.putBoolean("checkCanWrite", false);
        final DialogsActivity dialogsActivity = new DialogsActivity(bundle);
        dialogsActivity.setDelegate(new DialogsActivity.DialogsActivityDelegate() { // from class: com.radolyn.ayugram.preferences.FiltersPreferencesActivity$$ExternalSyntheticLambda7
            @Override // org.telegram.ui.DialogsActivity.DialogsActivityDelegate
            public /* synthetic */ boolean canSelectStories() {
                return DialogsActivity.DialogsActivityDelegate.CC.$default$canSelectStories(this);
            }

            @Override // org.telegram.ui.DialogsActivity.DialogsActivityDelegate
            public final boolean didSelectDialogs(DialogsActivity dialogsActivity2, ArrayList arrayList, CharSequence charSequence, boolean z, boolean z2, int i, int i2, TopicsFragment topicsFragment) {
                return this.f$0.lambda$selectChat$3(dialogsActivity, dialogsActivity2, arrayList, charSequence, z, z2, i, i2, topicsFragment);
            }

            @Override // org.telegram.ui.DialogsActivity.DialogsActivityDelegate
            public /* synthetic */ boolean didSelectStories(DialogsActivity dialogsActivity2) {
                return DialogsActivity.DialogsActivityDelegate.CC.$default$didSelectStories(this, dialogsActivity2);
            }
        });
        presentFragment(dialogsActivity);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ boolean lambda$selectChat$3(DialogsActivity dialogsActivity, DialogsActivity dialogsActivity2, ArrayList arrayList, CharSequence charSequence, boolean z, boolean z2, int i, int i2, TopicsFragment topicsFragment) {
        if (arrayList != null && !arrayList.isEmpty()) {
            long j = ((MessagesStorage.TopicKey) arrayList.get(0)).dialogId;
            if (dialogsActivity == dialogsActivity2) {
                presentFragment(new FiltersListPreferencesActivity(Long.valueOf(j)), true);
            } else {
                dialogsActivity2.presentFragment(new FiltersListPreferencesActivity(Long.valueOf(j)), true);
            }
        }
        return true;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void importFilters() {
        BottomSheet.Builder builder = new BottomSheet.Builder(getContext());
        builder.setTitle(LocaleController.getString(R.string.FiltersImportTitle));
        builder.setItems(new CharSequence[]{LocaleController.getString(R.string.FiltersImportClipboard), LocaleController.getString(R.string.FiltersImportURL)}, new DialogInterface.OnClickListener() { // from class: com.radolyn.ayugram.preferences.FiltersPreferencesActivity$$ExternalSyntheticLambda3
            @Override // android.content.DialogInterface.OnClickListener
            public final void onClick(DialogInterface dialogInterface, int i) {
                this.f$0.lambda$importFilters$6(dialogInterface, i);
            }
        });
        builder.create().show();
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$importFilters$6(DialogInterface dialogInterface, int i) {
        CharSequence text = ((ClipboardManager) getContext().getSystemService("clipboard")).getText();
        String string = text != null ? text.toString() : _UrlKt.FRAGMENT_ENCODE_SET;
        if (i == 0) {
            try {
                AyuFilterUtils.importFilters(this, string);
                return;
            } catch (Exception unused) {
                BulletinFactory.of(this).createSimpleBulletin(R.raw.error, LocaleController.getString(R.string.FiltersToastFailImport)).show();
                return;
            }
        }
        if (i == 1) {
            String string2 = AyuConfig.preferences.getString("lastFiltersImportLink", _UrlKt.FRAGMENT_ENCODE_SET);
            if (!string.contains(".txt") && !string.contains("github") && !string.contains("bin") && !string.contains("paste")) {
                string = string2;
            }
            AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
            builder.setTitle(LocaleController.getString(R.string.FiltersImportURL));
            LinearLayout linearLayout = new LinearLayout(getContext());
            final EditTextSettingsCell editTextSettingsCell = new EditTextSettingsCell(getContext());
            editTextSettingsCell.setText(string, true);
            linearLayout.setGravity(1);
            linearLayout.addView(editTextSettingsCell, LayoutHelper.createLinear(-1, -2, 1, 0, 0, 24, 0));
            builder.setView(linearLayout);
            builder.setPositiveButton(LocaleController.getString(R.string.FiltersImportAction), new AlertDialog.OnButtonClickListener() { // from class: com.radolyn.ayugram.preferences.FiltersPreferencesActivity$$ExternalSyntheticLambda8
                @Override // org.telegram.ui.ActionBar.AlertDialog.OnButtonClickListener
                public final void onClick(AlertDialog alertDialog, int i2) {
                    this.f$0.lambda$importFilters$4(editTextSettingsCell, alertDialog, i2);
                }
            });
            builder.setNegativeButton(LocaleController.getString(R.string.Cancel), new AlertDialog.OnButtonClickListener() { // from class: com.radolyn.ayugram.preferences.FiltersPreferencesActivity$$ExternalSyntheticLambda9
                @Override // org.telegram.ui.ActionBar.AlertDialog.OnButtonClickListener
                public final void onClick(AlertDialog alertDialog, int i2) {
                    alertDialog.cancel();
                }
            });
            builder.show();
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$importFilters$4(EditTextSettingsCell editTextSettingsCell, AlertDialog alertDialog, int i) {
        AyuFilterUtils.importFromLink(alertDialog, this, editTextSettingsCell.getText());
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void exportFilters() {
        BottomSheet.Builder builder = new BottomSheet.Builder(getContext());
        builder.setTitle(LocaleController.getString(R.string.FiltersExportTitle));
        builder.setItems(new CharSequence[]{LocaleController.getString(R.string.FiltersExportClipboard), LocaleController.getString(R.string.FiltersExportURL)}, new DialogInterface.OnClickListener() { // from class: com.radolyn.ayugram.preferences.FiltersPreferencesActivity$$ExternalSyntheticLambda4
            @Override // android.content.DialogInterface.OnClickListener
            public final void onClick(DialogInterface dialogInterface, int i) {
                this.f$0.lambda$exportFilters$7(dialogInterface, i);
            }
        });
        builder.create().show();
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$exportFilters$7(DialogInterface dialogInterface, int i) {
        String strExport = AyuFilterUtils.export();
        if (i != 0) {
            if (i == 1) {
                new OkHttpClient.Builder().followRedirects(true).build().newCall(new Request.Builder().url("https://dpaste.com/api/v2/").post(new MultipartBody.Builder().setType(MultipartBody.FORM).addFormDataPart("content", strExport).addFormDataPart("syntax", "json").addFormDataPart("title", "AyuGram Filters").build()).build()).enqueue(new AnonymousClass3());
            }
        } else {
            AndroidUtilities.addToClipboard(strExport);
            if (AndroidUtilities.shouldShowClipboardToast()) {
                Toast.makeText(getParentActivity(), LocaleController.getString(R.string.TextCopied), 0).show();
            }
        }
    }

    /* JADX INFO: renamed from: com.radolyn.ayugram.preferences.FiltersPreferencesActivity$3, reason: invalid class name */
    class AnonymousClass3 implements Callback {
        AnonymousClass3() {
        }

        /* JADX INFO: Access modifiers changed from: private */
        public /* synthetic */ void lambda$onFailure$0() {
            BulletinFactory.of(FiltersPreferencesActivity.this).createSimpleBulletin(R.raw.error, LocaleController.getString(R.string.FiltersToastFailFetch)).show();
        }

        @Override // okhttp3.Callback
        public void onFailure(Call call, IOException iOException) {
            AndroidUtilities.runOnUIThread(new Runnable() { // from class: com.radolyn.ayugram.preferences.FiltersPreferencesActivity$3$$ExternalSyntheticLambda1
                @Override // java.lang.Runnable
                public final void run() {
                    this.f$0.lambda$onFailure$0();
                }
            });
        }

        @Override // okhttp3.Callback
        public void onResponse(Call call, final Response response) {
            AndroidUtilities.runOnUIThread(new Runnable() { // from class: com.radolyn.ayugram.preferences.FiltersPreferencesActivity$3$$ExternalSyntheticLambda0
                @Override // java.lang.Runnable
                public final void run() {
                    this.f$0.lambda$onResponse$1(response);
                }
            });
        }

        /* JADX INFO: Access modifiers changed from: private */
        public /* synthetic */ void lambda$onResponse$1(Response response) {
            try {
                String strHeader = response.header("Location");
                Objects.requireNonNull(strHeader);
                AndroidUtilities.addToClipboard(strHeader + ".txt");
                if (AndroidUtilities.shouldShowClipboardToast()) {
                    Toast.makeText(FiltersPreferencesActivity.this.getParentActivity(), LocaleController.getString(R.string.TextCopied), 0).show();
                }
            } catch (NullPointerException unused) {
                BulletinFactory.of(FiltersPreferencesActivity.this).createSimpleBulletin(R.raw.error, LocaleController.getString(R.string.FiltersToastFailPublish)).show();
            }
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void clearFilters() {
        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        builder.setTitle(LocaleController.getString(R.string.FiltersClearPopupTitle));
        builder.setMessage(LocaleController.getString(R.string.FiltersClearPopupText));
        builder.setPositiveButton(LocaleController.getString(R.string.FiltersClearPopupActionText), new AlertDialog.OnButtonClickListener() { // from class: com.radolyn.ayugram.preferences.FiltersPreferencesActivity$$ExternalSyntheticLambda5
            @Override // org.telegram.ui.ActionBar.AlertDialog.OnButtonClickListener
            public final void onClick(AlertDialog alertDialog, int i) {
                this.f$0.lambda$clearFilters$8(alertDialog, i);
            }
        });
        builder.setNegativeButton(LocaleController.getString(R.string.Cancel), null);
        final List unknown = getUnknown();
        final List unknownExclusions = getUnknownExclusions();
        if (!unknown.isEmpty() || !unknownExclusions.isEmpty()) {
            builder.setNeutralButton(LocaleController.getString(R.string.FiltersClearPopupAltActionText), new AlertDialog.OnButtonClickListener() { // from class: com.radolyn.ayugram.preferences.FiltersPreferencesActivity$$ExternalSyntheticLambda6
                @Override // org.telegram.ui.ActionBar.AlertDialog.OnButtonClickListener
                public final void onClick(AlertDialog alertDialog, int i) {
                    this.f$0.lambda$clearFilters$9(unknown, unknownExclusions, alertDialog, i);
                }
            });
        }
        AlertDialog alertDialogCreate = builder.create();
        showDialog(alertDialogCreate);
        TextView textView = (TextView) alertDialogCreate.getButton(-1);
        if (textView != null) {
            textView.setTextColor(Theme.getColor(Theme.key_text_RedBold));
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$clearFilters$8(AlertDialog alertDialog, int i) {
        AyuData.clearRegexFilterDatabase();
        AyuFilterCacheController.rebuildCache();
        this.listView.adapter.update(true);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$clearFilters$9(List list, List list2, AlertDialog alertDialog, int i) {
        Iterator it = list.iterator();
        while (it.hasNext()) {
            AyuData.getRegexFilterDao().delete((UUID) it.next());
        }
        Iterator it2 = list2.iterator();
        while (it2.hasNext()) {
            RegexFilterGlobalExclusion regexFilterGlobalExclusion = (RegexFilterGlobalExclusion) it2.next();
            AyuData.getRegexFilterDao().deleteExclusion(regexFilterGlobalExclusion.dialogId, regexFilterGlobalExclusion.filterId);
        }
        AyuFilterCacheController.rebuildCache();
        this.listView.adapter.update(true);
    }

    private List getUnknown() {
        HashSet hashSet = new HashSet();
        for (RegexFilter regexFilter : AyuData.getRegexFilterDao().getAll()) {
            Long l = regexFilter.dialogId;
            if (l != null && AyuMessageUtils.getDialogInAnyWay(l.longValue(), Integer.valueOf(UserConfig.selectedAccount), false) == null) {
                hashSet.add(regexFilter.id);
            }
        }
        return new ArrayList(hashSet);
    }

    private List getUnknownExclusions() {
        HashSet hashSet = new HashSet();
        for (RegexFilterGlobalExclusion regexFilterGlobalExclusion : AyuData.getRegexFilterDao().getAllExclusions()) {
            if (AyuMessageUtils.getDialogInAnyWay(regexFilterGlobalExclusion.dialogId, Integer.valueOf(UserConfig.selectedAccount), false) == null || AyuData.getRegexFilterDao().getById(regexFilterGlobalExclusion.filterId) == null) {
                hashSet.add(regexFilterGlobalExclusion);
            }
        }
        return new ArrayList(hashSet);
    }
}
