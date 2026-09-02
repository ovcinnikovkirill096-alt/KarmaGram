package org.telegram.ui;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.graphics.Paint;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.TextView;
import androidx.annotation.Keep;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import java.io.File;
import java.util.ArrayList;
import okhttp3.internal.url._UrlKt;
import org.telegram.messenger.AndroidUtilities;
import org.telegram.messenger.DownloadController;
import org.telegram.messenger.ImageLoader;
import org.telegram.messenger.LocaleController;
import org.telegram.messenger.MessagesController;
import org.telegram.messenger.R;
import org.telegram.messenger.SaveToGallerySettingsHelper;
import org.telegram.messenger.SharedConfig;
import org.telegram.messenger.StatsController;
import org.telegram.messenger.Utilities;
import org.telegram.tgnet.RequestDelegate;
import org.telegram.tgnet.TLObject;
import org.telegram.tgnet.TLRPC;
import org.telegram.ui.ActionBar.ActionBar;
import org.telegram.ui.ActionBar.AlertDialog;
import org.telegram.ui.ActionBar.BaseFragment;
import org.telegram.ui.ActionBar.Theme;
import org.telegram.ui.ActionBar.ThemeDescription;
import org.telegram.ui.Cells.HeaderCell;
import org.telegram.ui.Cells.LanguageCell;
import org.telegram.ui.Cells.NotificationsCheckCell;
import org.telegram.ui.Cells.ShadowSectionCell;
import org.telegram.ui.Cells.TextCell;
import org.telegram.ui.Cells.TextCheckCell;
import org.telegram.ui.Cells.TextInfoPrivacyCell;
import org.telegram.ui.Cells.TextSettingsCell;
import org.telegram.ui.Components.AlertsCreator;
import org.telegram.ui.Components.CubicBezierInterpolator;
import org.telegram.ui.Components.LayoutHelper;
import org.telegram.ui.Components.RecyclerListView;
import org.telegram.ui.Components.voip.VoIPHelper;

public class DataSettingsActivity extends BaseFragment {
    private int callsSection2Row;
    private int callsSectionRow;

    @Keep
    private int clearDraftsRow;
    private int clearDraftsSectionRow;
    private int dataUsageRow;
    private int enableAllStreamInfoRow;
    private int enableAllStreamRow;
    private int enableCacheStreamRow;
    private int enableMkvRow;
    private int enableStreamRow;
    private LinearLayoutManager layoutManager;
    private ListAdapter listAdapter;
    private RecyclerListView listView;
    private int mediaDownloadSection2Row;
    private int mediaDownloadSectionRow;
    private int mobileRow;

    @Keep
    private int proxyRow;
    private int proxySection2Row;
    private int proxySectionRow;
    private int roamingRow;
    private int rowCount;

    @Keep
    private int saveToGalleryChannelsRow;
    private int saveToGalleryDividerRow;

    @Keep
    private int saveToGalleryGroupsRow;

    @Keep
    private int saveToGalleryPeerRow;
    private int saveToGallerySectionRow;
    private ArrayList storageDirs;
    private int storageNumRow;
    private boolean storageUsageLoading;
    private int storageUsageRow;
    private long storageUsageSize;
    private int streamSectionRow;
    private boolean updateStorageUsageAnimated;
    private boolean updateVoipUseLessData;
    private int usageSection2Row;
    private int usageSectionRow;

    @Keep
    private int useLessDataForCallsRow;
    private int wifiRow;

    @Keep
    private int resetDownloadRow = -1;
    private int autoplayHeaderRow = -1;
    private int autoplayGifsRow = -1;
    private int autoplayVideoRow = -1;
    private int autoplaySectionRow = -1;
    private int quickRepliesRow = -1;

    @Override // org.telegram.ui.ActionBar.BaseFragment
    public boolean isSupportEdgeToEdge() {
        return true;
    }

    @Override // org.telegram.ui.ActionBar.BaseFragment
    public boolean onFragmentCreate() {
        super.onFragmentCreate();
        DownloadController.getInstance(this.currentAccount).loadAutoDownloadConfig(true);
        updateRows(true);
        return true;
    }

    private void updateRows(boolean z) {
        int i;
        boolean z2 = false;
        this.usageSectionRow = 0;
        int i2 = 1 + 1;
        this.storageUsageRow = 1;
        this.rowCount = i2 + 1;
        this.dataUsageRow = i2;
        this.storageNumRow = -1;
        ArrayList<File> rootDirs = AndroidUtilities.getRootDirs();
        this.storageDirs = rootDirs;
        if (rootDirs.size() > 1) {
            int i3 = this.rowCount;
            this.rowCount = i3 + 1;
            this.storageNumRow = i3;
        }
        int i4 = this.rowCount;
        this.usageSection2Row = i4;
        this.mediaDownloadSectionRow = i4 + 1;
        this.mobileRow = i4 + 2;
        this.wifiRow = i4 + 3;
        this.rowCount = i4 + 5;
        this.roamingRow = i4 + 4;
        DownloadController downloadController = getDownloadController();
        if (downloadController.lowPreset.equals(downloadController.getCurrentRoamingPreset()) && downloadController.lowPreset.isEnabled() == downloadController.roamingPreset.enabled && downloadController.mediumPreset.equals(downloadController.getCurrentMobilePreset()) && downloadController.mediumPreset.isEnabled() == downloadController.mobilePreset.enabled && downloadController.highPreset.equals(downloadController.getCurrentWiFiPreset()) && downloadController.highPreset.isEnabled() == downloadController.wifiPreset.enabled) {
            z2 = true;
        }
        int i5 = this.resetDownloadRow;
        if (z2) {
            i = -1;
        } else {
            i = this.rowCount;
            this.rowCount = i + 1;
        }
        this.resetDownloadRow = i;
        ListAdapter listAdapter = this.listAdapter;
        if (listAdapter != null && !z) {
            if (i5 < 0 && i >= 0) {
                listAdapter.notifyItemChanged(this.roamingRow);
                this.listAdapter.notifyItemInserted(this.resetDownloadRow);
            } else if (i5 < 0 || i >= 0) {
                z = true;
            } else {
                listAdapter.notifyItemChanged(this.roamingRow);
                this.listAdapter.notifyItemRemoved(i5);
            }
        }
        int i6 = this.rowCount;
        this.mediaDownloadSection2Row = i6;
        this.saveToGallerySectionRow = i6 + 1;
        this.saveToGalleryPeerRow = i6 + 2;
        this.saveToGalleryGroupsRow = i6 + 3;
        this.saveToGalleryChannelsRow = i6 + 4;
        this.saveToGalleryDividerRow = i6 + 5;
        this.streamSectionRow = i6 + 6;
        this.enableStreamRow = i6 + 7;
        this.enableMkvRow = i6 + 8;
        this.enableAllStreamRow = i6 + 9;
        this.enableAllStreamInfoRow = i6 + 10;
        this.enableCacheStreamRow = -1;
        this.callsSectionRow = i6 + 11;
        this.useLessDataForCallsRow = i6 + 12;
        this.callsSection2Row = i6 + 13;
        this.proxySectionRow = i6 + 14;
        this.proxyRow = i6 + 15;
        this.proxySection2Row = i6 + 16;
        this.clearDraftsRow = i6 + 17;
        this.rowCount = i6 + 19;
        this.clearDraftsSectionRow = i6 + 18;
        ListAdapter listAdapter2 = this.listAdapter;
        if (listAdapter2 == null || !z) {
            return;
        }
        listAdapter2.notifyDataSetChanged();
    }

    private void loadCacheSize() {
        final Runnable runnable = new Runnable() { // from class: org.telegram.ui.DataSettingsActivity$$ExternalSyntheticLambda1
            @Override // java.lang.Runnable
            public final void run() {
                this.f$0.lambda$loadCacheSize$0();
            }
        };
        AndroidUtilities.runOnUIThread(runnable, 100L);
        final long jCurrentTimeMillis = System.currentTimeMillis();
        CacheControlActivity.calculateTotalSize(new Utilities.Callback() { // from class: org.telegram.ui.DataSettingsActivity$$ExternalSyntheticLambda2
            @Override // org.telegram.messenger.Utilities.Callback
            public final void run(Object obj) {
                this.f$0.lambda$loadCacheSize$1(runnable, jCurrentTimeMillis, (Long) obj);
            }
        });
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$loadCacheSize$0() {
        int i;
        this.storageUsageLoading = true;
        if (this.listAdapter == null || (i = this.storageUsageRow) < 0) {
            return;
        }
        rebind(i);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$loadCacheSize$1(Runnable runnable, long j, Long l) {
        int i;
        AndroidUtilities.cancelRunOnUIThread(runnable);
        this.updateStorageUsageAnimated = this.updateStorageUsageAnimated || System.currentTimeMillis() - j > 120;
        this.storageUsageSize = l.longValue();
        this.storageUsageLoading = false;
        if (this.listAdapter == null || (i = this.storageUsageRow) < 0) {
            return;
        }
        rebind(i);
    }

    private void rebind(int i) {
        if (this.listView == null || this.listAdapter == null) {
            return;
        }
        for (int i2 = 0; i2 < this.listView.getChildCount(); i2++) {
            RecyclerView.ViewHolder childViewHolder = this.listView.getChildViewHolder(this.listView.getChildAt(i2));
            if (childViewHolder != null && childViewHolder.getAdapterPosition() == i) {
                this.listAdapter.onBindViewHolder(childViewHolder, i);
                return;
            }
        }
    }

    private void rebindAll() {
        if (this.listView == null || this.listAdapter == null) {
            return;
        }
        for (int i = 0; i < this.listView.getChildCount(); i++) {
            View childAt = this.listView.getChildAt(i);
            RecyclerView.ViewHolder childViewHolder = this.listView.getChildViewHolder(childAt);
            if (childViewHolder != null) {
                this.listAdapter.onBindViewHolder(childViewHolder, this.listView.getChildAdapterPosition(childAt));
            }
        }
    }

    @Override // org.telegram.ui.ActionBar.BaseFragment
    public void onFragmentDestroy() {
        super.onFragmentDestroy();
        CacheControlActivity.canceled = true;
    }

    @Override // org.telegram.ui.ActionBar.BaseFragment
    public View createView(final Context context) {
        this.actionBar.setBackButtonImage(R.drawable.ic_ab_back);
        this.actionBar.setTitle(LocaleController.getString(R.string.DataSettings));
        if (AndroidUtilities.isTablet()) {
            this.actionBar.setOccupyStatusBar(false);
        }
        this.actionBar.setAllowOverlayTitle(true);
        this.actionBar.setActionBarMenuOnItemClick(new ActionBar.ActionBarMenuOnItemClick() { // from class: org.telegram.ui.DataSettingsActivity.1
            @Override // org.telegram.ui.ActionBar.ActionBar.ActionBarMenuOnItemClick
            public void onItemClick(int i) {
                if (i == -1) {
                    DataSettingsActivity.this.finishFragment();
                }
            }
        });
        this.listAdapter = new ListAdapter(context);
        FrameLayout frameLayout = new FrameLayout(context);
        this.fragmentView = frameLayout;
        frameLayout.setBackgroundColor(Theme.getColor(Theme.key_windowBackgroundGray));
        FrameLayout frameLayout2 = (FrameLayout) this.fragmentView;
        RecyclerListView recyclerListView = new RecyclerListView(context) { // from class: org.telegram.ui.DataSettingsActivity.2
            @Override // org.telegram.ui.Components.RecyclerListView
            public Integer getSelectorColor(int i) {
                if (i == DataSettingsActivity.this.resetDownloadRow) {
                    return Integer.valueOf(Theme.multAlpha(getThemedColor(Theme.key_text_RedRegular), 0.1f));
                }
                return Integer.valueOf(getThemedColor(Theme.key_listSelector));
            }
        };
        this.listView = recyclerListView;
        recyclerListView.setSections();
        this.actionBar.setAdaptiveBackground(this.listView);
        this.listView.setVerticalScrollBarEnabled(false);
        RecyclerListView recyclerListView2 = this.listView;
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(context, 1, false);
        this.layoutManager = linearLayoutManager;
        recyclerListView2.setLayoutManager(linearLayoutManager);
        frameLayout2.addView(this.listView, LayoutHelper.createFrame(-1, -1, 51));
        this.listView.setAdapter(this.listAdapter);
        this.listView.setOnItemClickListener(new RecyclerListView.OnItemClickListenerExtended() { // from class: org.telegram.ui.DataSettingsActivity$$ExternalSyntheticLambda0
            @Override // org.telegram.ui.Components.RecyclerListView.OnItemClickListenerExtended
            public /* synthetic */ boolean hasDoubleTap(View view, int i) {
                return RecyclerListView.OnItemClickListenerExtended.CC.$default$hasDoubleTap(this, view, i);
            }

            @Override // org.telegram.ui.Components.RecyclerListView.OnItemClickListenerExtended
            public /* synthetic */ void onDoubleTap(View view, int i, float f, float f2) {
                RecyclerListView.OnItemClickListenerExtended.CC.$default$onDoubleTap(this, view, i, f, f2);
            }

            @Override // org.telegram.ui.Components.RecyclerListView.OnItemClickListenerExtended
            public final void onItemClick(View view, int i, float f, float f2) {
                this.f$0.lambda$createView$9(context, view, i, f, f2);
            }
        });
        DefaultItemAnimator defaultItemAnimator = new DefaultItemAnimator();
        defaultItemAnimator.setDurations(350L);
        defaultItemAnimator.setInterpolator(CubicBezierInterpolator.EASE_OUT_QUINT);
        defaultItemAnimator.setDelayAnimations(false);
        defaultItemAnimator.setSupportsChangeAnimations(false);
        this.listView.setItemAnimator(defaultItemAnimator);
        return this.fragmentView;
    }

    /* JADX INFO: Access modifiers changed from: private */
    /* JADX WARN: Code duplicated, block: B:38:0x00b3  */
    /* JADX WARN: Multi-variable type inference failed */
    public /* synthetic */ void lambda$createView$9(Context context, View view, final int i, float f, float f2) {
        int i2;
        int i3;
        DownloadController.Preset preset;
        DownloadController.Preset preset2;
        String str;
        String str2;
        boolean z;
        boolean z2;
        int i4;
        CharSequence string;
        int i5 = this.saveToGalleryGroupsRow;
        int i6 = 2;
        int i7 = 1;
        if (i == i5 || i == this.saveToGalleryChannelsRow || i == this.saveToGalleryPeerRow) {
            if (i == i5) {
                i2 = 2;
            } else {
                i2 = i == this.saveToGalleryChannelsRow ? 4 : 1;
            }
            if ((LocaleController.isRTL && f <= AndroidUtilities.dp(76.0f)) || (!LocaleController.isRTL && f >= view.getMeasuredWidth() - AndroidUtilities.dp(76.0f))) {
                SaveToGallerySettingsHelper.getSettings(i2).toggle();
                AndroidUtilities.updateVisibleRows(this.listView);
                return;
            } else {
                Bundle bundle = new Bundle();
                bundle.putInt("type", i2);
                presentFragment(new SaveToGallerySettingsActivity(bundle));
                return;
            }
        }
        int i8 = 0;
        if (i == this.mobileRow || i == this.roamingRow || i == this.wifiRow) {
            int i9 = 2;
            if ((LocaleController.isRTL && f <= AndroidUtilities.dp(76.0f)) || (!LocaleController.isRTL && f >= view.getMeasuredWidth() - AndroidUtilities.dp(76.0f))) {
                this.listAdapter.isRowEnabled(this.resetDownloadRow);
                NotificationsCheckCell notificationsCheckCell = (NotificationsCheckCell) view;
                boolean zIsChecked = notificationsCheckCell.isChecked();
                if (i == this.mobileRow) {
                    preset = DownloadController.getInstance(this.currentAccount).mobilePreset;
                    preset2 = DownloadController.getInstance(this.currentAccount).mediumPreset;
                    str = "mobilePreset";
                    str2 = "currentMobilePreset";
                    i9 = 0;
                } else if (i == this.wifiRow) {
                    preset = DownloadController.getInstance(this.currentAccount).wifiPreset;
                    preset2 = DownloadController.getInstance(this.currentAccount).highPreset;
                    str = "wifiPreset";
                    str2 = "currentWifiPreset";
                    i9 = 1;
                } else {
                    preset = DownloadController.getInstance(this.currentAccount).roamingPreset;
                    preset2 = DownloadController.getInstance(this.currentAccount).lowPreset;
                    str = "roamingPreset";
                    str2 = "currentRoamingPreset";
                }
                if (!zIsChecked && preset.enabled) {
                    preset.set(preset2);
                    z = true;
                } else {
                    z = true;
                    preset.enabled = !preset.enabled;
                }
                SharedPreferences.Editor editorEdit = MessagesController.getMainSettings(this.currentAccount).edit();
                editorEdit.putString(str, preset.toString());
                editorEdit.putInt(str2, 3);
                editorEdit.apply();
                notificationsCheckCell.setChecked(zIsChecked ^ z);
                RecyclerView.ViewHolder viewHolderFindContainingViewHolder = this.listView.findContainingViewHolder(view);
                if (viewHolderFindContainingViewHolder != null) {
                    this.listAdapter.onBindViewHolder(viewHolderFindContainingViewHolder, i);
                }
                DownloadController.getInstance(this.currentAccount).checkAutodownloadSettings();
                DownloadController.getInstance(this.currentAccount).savePresetToServer(i9);
                updateRows(false);
                return;
            }
            if (i == this.mobileRow) {
                i3 = 0;
            } else {
                i3 = i == this.wifiRow ? 1 : 2;
            }
            presentFragment(new DataAutoDownloadActivity(i3));
            return;
        }
        if (i == this.resetDownloadRow) {
            if (getParentActivity() == null || !view.isEnabled()) {
                return;
            }
            AlertDialog.Builder builder = new AlertDialog.Builder(getParentActivity());
            builder.setTitle(LocaleController.getString(R.string.ResetAutomaticMediaDownloadAlertTitle));
            builder.setMessage(LocaleController.getString(R.string.ResetAutomaticMediaDownloadAlert));
            builder.setPositiveButton(LocaleController.getString(R.string.Reset), new AlertDialog.OnButtonClickListener() { // from class: org.telegram.ui.DataSettingsActivity$$ExternalSyntheticLambda3
                @Override // org.telegram.ui.ActionBar.AlertDialog.OnButtonClickListener
                public final void onClick(AlertDialog alertDialog, int i10) {
                    this.f$0.lambda$createView$2(alertDialog, i10);
                }
            });
            builder.setNegativeButton(LocaleController.getString(R.string.Cancel), null);
            AlertDialog alertDialogCreate = builder.create();
            showDialog(alertDialogCreate);
            TextView textView = (TextView) alertDialogCreate.getButton(-1);
            if (textView != null) {
                textView.setTextColor(Theme.getColor(Theme.key_text_RedBold));
                return;
            }
            return;
        }
        if (i == this.storageUsageRow) {
            presentFragment(new CacheControlActivity());
            return;
        }
        if (i == this.useLessDataForCallsRow) {
            final SharedPreferences globalMainSettings = MessagesController.getGlobalMainSettings();
            int i10 = globalMainSettings.getInt("VoipDataSaving", VoIPHelper.getDataSavingDefault());
            if (i10 == 0) {
                i6 = 0;
            } else if (i10 != 1) {
                if (i10 == 2) {
                    i6 = 3;
                } else if (i10 != 3) {
                    i6 = 0;
                } else {
                    i6 = 1;
                }
            }
            Dialog dialogCreateSingleChoiceDialog = AlertsCreator.createSingleChoiceDialog(getParentActivity(), new String[]{LocaleController.getString(R.string.UseLessDataNever), LocaleController.getString(R.string.UseLessDataOnRoaming), LocaleController.getString(R.string.UseLessDataOnMobile), LocaleController.getString(R.string.UseLessDataAlways)}, LocaleController.getString(R.string.VoipUseLessData), i6, new DialogInterface.OnClickListener() { // from class: org.telegram.ui.DataSettingsActivity$$ExternalSyntheticLambda4
                @Override // android.content.DialogInterface.OnClickListener
                public final void onClick(DialogInterface dialogInterface, int i11) {
                    this.f$0.lambda$createView$3(globalMainSettings, i, dialogInterface, i11);
                }
            });
            setVisibleDialog(dialogCreateSingleChoiceDialog);
            dialogCreateSingleChoiceDialog.show();
            return;
        }
        if (i == this.dataUsageRow) {
            presentFragment(new DataUsage2Activity());
            return;
        }
        if (i == this.storageNumRow) {
            final AlertDialog.Builder builder2 = new AlertDialog.Builder(getParentActivity());
            builder2.setTitle(LocaleController.getString(R.string.StoragePath));
            LinearLayout linearLayout = new LinearLayout(getParentActivity());
            linearLayout.setOrientation(1);
            builder2.setView(linearLayout);
            String absolutePath = ((File) this.storageDirs.get(0)).getAbsolutePath();
            if (!TextUtils.isEmpty(SharedConfig.storageCacheDir)) {
                int size = this.storageDirs.size();
                for (int i11 = 0; i11 < size; i11++) {
                    String absolutePath2 = ((File) this.storageDirs.get(i11)).getAbsolutePath();
                    if (absolutePath2.startsWith(SharedConfig.storageCacheDir)) {
                        absolutePath = absolutePath2;
                        break;
                    }
                }
            }
            try {
                z2 = this.storageDirs.size() != 2 || ((File) this.storageDirs.get(0)).getAbsolutePath().contains("/storage/emulated/") == ((File) this.storageDirs.get(1)).getAbsolutePath().contains("/storage/emulated/");
            } catch (Exception unused) {
            }
            int size2 = this.storageDirs.size();
            int i12 = 0;
            while (i12 < size2) {
                File file = (File) this.storageDirs.get(i12);
                final String absolutePath3 = file.getAbsolutePath();
                LanguageCell languageCell = new LanguageCell(context);
                int i13 = i7;
                languageCell.setPadding(AndroidUtilities.dp(4.0f), i8, AndroidUtilities.dp(4.0f), i8);
                languageCell.setTag(Integer.valueOf(i12));
                final boolean zContains = absolutePath3.contains("/storage/emulated/");
                if (!z2 || zContains) {
                    i4 = i8;
                    if (zContains) {
                        int i14 = R.string.StoragePathFreeInternal;
                        Object[] objArr = new Object[i13];
                        objArr[i4] = AndroidUtilities.formatFileSize(file.getFreeSpace());
                        string = LocaleController.formatString(i14, objArr);
                    } else {
                        int i15 = R.string.StoragePathFreeExternal;
                        String fileSize = AndroidUtilities.formatFileSize(file.getFreeSpace());
                        Object[] objArr2 = new Object[i13];
                        objArr2[i4] = fileSize;
                        string = LocaleController.formatString(i15, objArr2);
                    }
                } else {
                    int i16 = R.string.StoragePathFreeValueExternal;
                    i4 = i8;
                    Object[] objArr3 = new Object[i6];
                    objArr3[i4] = AndroidUtilities.formatFileSize(file.getFreeSpace());
                    objArr3[i13] = absolutePath3;
                    string = LocaleController.formatString(i16, objArr3);
                }
                languageCell.setValue(LocaleController.getString(zContains ? R.string.InternalStorage : R.string.SdCard), string);
                languageCell.setLanguageSelected(absolutePath3.startsWith(absolutePath), i4);
                languageCell.setBackground(Theme.createSelectorDrawable(Theme.getColor(Theme.key_dialogButtonSelector), 2));
                linearLayout.addView(languageCell);
                languageCell.setOnClickListener(new View.OnClickListener() { // from class: org.telegram.ui.DataSettingsActivity$$ExternalSyntheticLambda5
                    @Override // android.view.View.OnClickListener
                    public final void onClick(View view2) {
                        this.f$0.lambda$createView$5(absolutePath3, zContains, builder2, view2);
                    }
                });
                i12++;
                i6 = 2;
                i7 = 1;
                i8 = 0;
            }
            builder2.setNegativeButton(LocaleController.getString(R.string.Cancel), null);
            showDialog(builder2.create());
            return;
        }
        if (i == this.proxyRow) {
            presentFragment(new ProxyListActivity());
            return;
        }
        if (i == this.enableStreamRow) {
            SharedConfig.toggleStreamMedia();
            ((TextCheckCell) view).setChecked(SharedConfig.streamMedia);
            return;
        }
        if (i == this.enableAllStreamRow) {
            SharedConfig.toggleStreamAllVideo();
            ((TextCheckCell) view).setChecked(SharedConfig.streamAllVideo);
            return;
        }
        if (i == this.enableMkvRow) {
            SharedConfig.toggleStreamMkv();
            ((TextCheckCell) view).setChecked(SharedConfig.streamMkv);
            return;
        }
        if (i == this.enableCacheStreamRow) {
            SharedConfig.toggleSaveStreamMedia();
            ((TextCheckCell) view).setChecked(SharedConfig.saveStreamMedia);
            return;
        }
        if (i == this.quickRepliesRow) {
            presentFragment(new QuickRepliesSettingsActivity());
            return;
        }
        if (i == this.autoplayGifsRow) {
            SharedConfig.toggleAutoplayGifs();
            if (view instanceof TextCheckCell) {
                ((TextCheckCell) view).setChecked(SharedConfig.isAutoplayGifs());
                return;
            }
            return;
        }
        if (i == this.autoplayVideoRow) {
            SharedConfig.toggleAutoplayVideo();
            if (view instanceof TextCheckCell) {
                ((TextCheckCell) view).setChecked(SharedConfig.isAutoplayVideo());
                return;
            }
            return;
        }
        if (i == this.clearDraftsRow) {
            AlertDialog.Builder builder3 = new AlertDialog.Builder(getParentActivity());
            builder3.setTitle(LocaleController.getString(R.string.AreYouSureClearDraftsTitle));
            builder3.setMessage(LocaleController.getString(R.string.AreYouSureClearDrafts));
            builder3.setPositiveButton(LocaleController.getString(R.string.Delete), new AlertDialog.OnButtonClickListener() { // from class: org.telegram.ui.DataSettingsActivity$$ExternalSyntheticLambda6
                @Override // org.telegram.ui.ActionBar.AlertDialog.OnButtonClickListener
                public final void onClick(AlertDialog alertDialog, int i17) {
                    this.f$0.lambda$createView$8(alertDialog, i17);
                }
            });
            builder3.setNegativeButton(LocaleController.getString(R.string.Cancel), null);
            AlertDialog alertDialogCreate2 = builder3.create();
            showDialog(alertDialogCreate2);
            TextView textView2 = (TextView) alertDialogCreate2.getButton(-1);
            if (textView2 != null) {
                textView2.setTextColor(Theme.getColor(Theme.key_text_RedBold));
            }
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$createView$2(AlertDialog alertDialog, int i) {
        DownloadController.Preset preset;
        DownloadController.Preset preset2;
        String str;
        SharedPreferences.Editor editorEdit = MessagesController.getMainSettings(this.currentAccount).edit();
        for (int i2 = 0; i2 < 3; i2++) {
            if (i2 == 0) {
                preset = DownloadController.getInstance(this.currentAccount).mobilePreset;
                preset2 = DownloadController.getInstance(this.currentAccount).mediumPreset;
                str = "mobilePreset";
            } else if (i2 == 1) {
                preset = DownloadController.getInstance(this.currentAccount).wifiPreset;
                preset2 = DownloadController.getInstance(this.currentAccount).highPreset;
                str = "wifiPreset";
            } else {
                preset = DownloadController.getInstance(this.currentAccount).roamingPreset;
                preset2 = DownloadController.getInstance(this.currentAccount).lowPreset;
                str = "roamingPreset";
            }
            preset.set(preset2);
            preset.enabled = preset2.isEnabled();
            DownloadController.getInstance(this.currentAccount).currentMobilePreset = 3;
            editorEdit.putInt("currentMobilePreset", 3);
            DownloadController.getInstance(this.currentAccount).currentWifiPreset = 3;
            editorEdit.putInt("currentWifiPreset", 3);
            DownloadController.getInstance(this.currentAccount).currentRoamingPreset = 3;
            editorEdit.putInt("currentRoamingPreset", 3);
            editorEdit.putString(str, preset.toString());
        }
        editorEdit.apply();
        DownloadController.getInstance(this.currentAccount).checkAutodownloadSettings();
        for (int i3 = 0; i3 < 3; i3++) {
            DownloadController.getInstance(this.currentAccount).savePresetToServer(i3);
        }
        this.listAdapter.notifyItemRangeChanged(this.mobileRow, 4);
        updateRows(false);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$createView$3(SharedPreferences sharedPreferences, int i, DialogInterface dialogInterface, int i2) {
        int i3;
        if (i2 != 0) {
            i3 = 3;
            if (i2 != 1) {
                if (i2 != 2) {
                    i3 = i2 != 3 ? -1 : 2;
                } else {
                    i3 = 1;
                }
            }
        } else {
            i3 = 0;
        }
        if (i3 != -1) {
            sharedPreferences.edit().putInt("VoipDataSaving", i3).apply();
            this.updateVoipUseLessData = true;
        }
        ListAdapter listAdapter = this.listAdapter;
        if (listAdapter != null) {
            listAdapter.notifyItemChanged(i);
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$createView$5(final String str, boolean z, final AlertDialog.Builder builder, View view) {
        if (TextUtils.equals(SharedConfig.storageCacheDir, str)) {
            return;
        }
        if (!z) {
            AlertDialog.Builder builder2 = new AlertDialog.Builder(getContext());
            builder2.setTitle(LocaleController.getString(R.string.DecreaseSpeed));
            builder2.setMessage(LocaleController.getString(R.string.SdCardAlert));
            builder2.setPositiveButton(LocaleController.getString(R.string.Proceed), new AlertDialog.OnButtonClickListener() { // from class: org.telegram.ui.DataSettingsActivity$$ExternalSyntheticLambda7
                @Override // org.telegram.ui.ActionBar.AlertDialog.OnButtonClickListener
                public final void onClick(AlertDialog alertDialog, int i) {
                    this.f$0.lambda$createView$4(str, builder, alertDialog, i);
                }
            });
            builder2.setNegativeButton(LocaleController.getString(R.string.Back), null);
            builder2.show();
            return;
        }
        setStorageDirectory(str);
        builder.getDismissRunnable().run();
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$createView$4(String str, AlertDialog.Builder builder, AlertDialog alertDialog, int i) {
        setStorageDirectory(str);
        builder.getDismissRunnable().run();
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$createView$8(AlertDialog alertDialog, int i) {
        getConnectionsManager().sendRequest(new TLRPC.TL_messages_clearAllDrafts(), new RequestDelegate() { // from class: org.telegram.ui.DataSettingsActivity$$ExternalSyntheticLambda8
            @Override // org.telegram.tgnet.RequestDelegate
            public final void run(TLObject tLObject, TLRPC.TL_error tL_error) {
                this.f$0.lambda$createView$7(tLObject, tL_error);
            }
        });
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$createView$6() {
        getMediaDataController().clearAllDrafts(true);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$createView$7(TLObject tLObject, TLRPC.TL_error tL_error) {
        AndroidUtilities.runOnUIThread(new Runnable() { // from class: org.telegram.ui.DataSettingsActivity$$ExternalSyntheticLambda10
            @Override // java.lang.Runnable
            public final void run() {
                this.f$0.lambda$createView$6();
            }
        });
    }

    private void setStorageDirectory(String str) {
        SharedConfig.storageCacheDir = str;
        SharedConfig.saveConfig();
        if (str != null) {
            SharedConfig.readOnlyStorageDirAlertShowed = false;
        }
        rebind(this.storageNumRow);
        ImageLoader.getInstance().checkMediaPaths(new Runnable() { // from class: org.telegram.ui.DataSettingsActivity$$ExternalSyntheticLambda9
            @Override // java.lang.Runnable
            public final void run() {
                this.f$0.lambda$setStorageDirectory$10();
            }
        });
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$setStorageDirectory$10() {
        CacheControlActivity.resetCalculatedTotalSIze();
        loadCacheSize();
    }

    @Override // org.telegram.ui.ActionBar.BaseFragment
    protected void onDialogDismiss(Dialog dialog) {
        DownloadController.getInstance(this.currentAccount).checkAutodownloadSettings();
    }

    @Override // org.telegram.ui.ActionBar.BaseFragment
    public void onResume() {
        super.onResume();
        loadCacheSize();
        rebindAll();
        updateRows(false);
    }

    private class ListAdapter extends RecyclerListView.SelectionAdapter {
        private Context mContext;

        public ListAdapter(Context context) {
            this.mContext = context;
        }

        @Override // androidx.recyclerview.widget.RecyclerView.Adapter
        public int getItemCount() {
            return DataSettingsActivity.this.rowCount;
        }

        /* JADX WARN: Code duplicated, block: B:101:0x02f8  */
        /* JADX WARN: Code duplicated, block: B:109:0x0311  */
        /* JADX WARN: Code duplicated, block: B:204:0x0283 A[SYNTHETIC] */
        /* JADX WARN: Code duplicated, block: B:68:0x024d  */
        /* JADX WARN: Code duplicated, block: B:71:0x0260  */
        /* JADX WARN: Multi-variable type inference failed */
        @Override // androidx.recyclerview.widget.RecyclerView.Adapter
        public void onBindViewHolder(RecyclerView.ViewHolder viewHolder, int i) {
            boolean zEnabled;
            CharSequence charSequence;
            CharSequence charSequence2;
            boolean z;
            CharSequence string;
            DownloadController.Preset currentWiFiPreset;
            CharSequence charSequence3;
            CharSequence charSequenceCreateDescription;
            boolean z2;
            CharSequence charSequence4;
            StringBuilder sb;
            int i2;
            boolean z3;
            int i3;
            boolean z4;
            boolean z5;
            int[] iArr;
            boolean z6;
            String string2 = null;
            currentRoamingPreset = null;
            currentRoamingPreset = null;
            DownloadController.Preset currentRoamingPreset = null;
            int i4 = 0;
            switch (viewHolder.getItemViewType()) {
                case 1:
                    TextSettingsCell textSettingsCell = (TextSettingsCell) viewHolder.itemView;
                    textSettingsCell.setCanDisable(false);
                    textSettingsCell.setTextColor(Theme.getColor(Theme.key_windowBackgroundWhiteBlackText));
                    if (i == DataSettingsActivity.this.useLessDataForCallsRow) {
                        textSettingsCell.setIcon(0);
                        int i5 = MessagesController.getGlobalMainSettings().getInt("VoipDataSaving", VoIPHelper.getDataSavingDefault());
                        if (i5 == 0) {
                            string2 = LocaleController.getString(R.string.UseLessDataNever);
                        } else if (i5 == 1) {
                            string2 = LocaleController.getString(R.string.UseLessDataOnMobile);
                        } else if (i5 == 2) {
                            string2 = LocaleController.getString(R.string.UseLessDataAlways);
                        } else if (i5 == 3) {
                            string2 = LocaleController.getString(R.string.UseLessDataOnRoaming);
                        }
                        textSettingsCell.setTextAndValue(LocaleController.getString(R.string.VoipUseLessData), string2, DataSettingsActivity.this.updateVoipUseLessData, false);
                        DataSettingsActivity.this.updateVoipUseLessData = false;
                    } else if (i == DataSettingsActivity.this.proxyRow) {
                        textSettingsCell.setIcon(0);
                        textSettingsCell.setText(LocaleController.getString(R.string.ProxySettings), false);
                    } else if (i == DataSettingsActivity.this.resetDownloadRow) {
                        textSettingsCell.setIcon(0);
                        textSettingsCell.setCanDisable(true);
                        textSettingsCell.setTextColor(Theme.getColor(Theme.key_text_RedRegular));
                        textSettingsCell.setText(LocaleController.getString(R.string.ResetAutomaticMediaDownload), false);
                    } else if (i == DataSettingsActivity.this.quickRepliesRow) {
                        textSettingsCell.setIcon(0);
                        textSettingsCell.setText(LocaleController.getString(R.string.VoipQuickReplies), false);
                    } else if (i == DataSettingsActivity.this.clearDraftsRow) {
                        textSettingsCell.setIcon(0);
                        textSettingsCell.setText(LocaleController.getString(R.string.PrivacyDeleteCloudDrafts), false);
                    }
                    break;
                case 2:
                    HeaderCell headerCell = (HeaderCell) viewHolder.itemView;
                    if (i == DataSettingsActivity.this.mediaDownloadSectionRow) {
                        headerCell.setText(LocaleController.getString(R.string.AutomaticMediaDownload));
                    } else if (i == DataSettingsActivity.this.usageSectionRow) {
                        headerCell.setText(LocaleController.getString(R.string.DataUsage));
                    } else if (i == DataSettingsActivity.this.callsSectionRow) {
                        headerCell.setText(LocaleController.getString(R.string.Calls));
                    } else if (i == DataSettingsActivity.this.proxySectionRow) {
                        headerCell.setText(LocaleController.getString(R.string.Proxy));
                    } else if (i == DataSettingsActivity.this.streamSectionRow) {
                        headerCell.setText(LocaleController.getString(R.string.Streaming));
                    } else if (i == DataSettingsActivity.this.autoplayHeaderRow) {
                        headerCell.setText(LocaleController.getString(R.string.AutoplayMedia));
                    } else if (i == DataSettingsActivity.this.saveToGallerySectionRow) {
                        headerCell.setText(LocaleController.getString(R.string.SaveToGallerySettings));
                    }
                    break;
                case 3:
                    TextCheckCell textCheckCell = (TextCheckCell) viewHolder.itemView;
                    if (i == DataSettingsActivity.this.enableStreamRow) {
                        textCheckCell.setTextAndCheck(LocaleController.getString(R.string.EnableStreaming), SharedConfig.streamMedia, DataSettingsActivity.this.enableAllStreamRow != -1);
                        break;
                    } else if (i != DataSettingsActivity.this.enableCacheStreamRow) {
                        if (i == DataSettingsActivity.this.enableMkvRow) {
                            textCheckCell.setTextAndCheck("Stream MKV Videos β", SharedConfig.streamMkv, true);
                        } else if (i == DataSettingsActivity.this.enableAllStreamRow) {
                            textCheckCell.setTextAndCheck("Stream ALL Videos β", SharedConfig.streamAllVideo, false);
                        } else if (i == DataSettingsActivity.this.autoplayGifsRow) {
                            textCheckCell.setTextAndCheck(LocaleController.getString(R.string.AutoplayGIF), SharedConfig.isAutoplayGifs(), true);
                        } else if (i == DataSettingsActivity.this.autoplayVideoRow) {
                            textCheckCell.setTextAndCheck(LocaleController.getString(R.string.AutoplayVideo), SharedConfig.isAutoplayVideo(), false);
                        }
                        break;
                    }
                    break;
                case 4:
                    TextInfoPrivacyCell textInfoPrivacyCell = (TextInfoPrivacyCell) viewHolder.itemView;
                    if (i == DataSettingsActivity.this.enableAllStreamInfoRow) {
                        textInfoPrivacyCell.setText(LocaleController.getString(R.string.EnableAllStreamingInfo));
                    }
                    break;
                case 5:
                    NotificationsCheckCell notificationsCheckCell = (NotificationsCheckCell) viewHolder.itemView;
                    if (i == DataSettingsActivity.this.saveToGalleryPeerRow) {
                        string = LocaleController.getString(R.string.SaveToGalleryPrivate);
                        CharSequence charSequenceCreateDescription2 = SaveToGallerySettingsHelper.user.createDescription(((BaseFragment) DataSettingsActivity.this).currentAccount);
                        zEnabled = SaveToGallerySettingsHelper.user.enabled();
                        charSequence3 = charSequenceCreateDescription2;
                    } else if (i == DataSettingsActivity.this.saveToGalleryGroupsRow) {
                        string = LocaleController.getString(R.string.SaveToGalleryGroups);
                        CharSequence charSequenceCreateDescription3 = SaveToGallerySettingsHelper.groups.createDescription(((BaseFragment) DataSettingsActivity.this).currentAccount);
                        zEnabled = SaveToGallerySettingsHelper.groups.enabled();
                        charSequence3 = charSequenceCreateDescription3;
                    } else {
                        if (i == DataSettingsActivity.this.saveToGalleryChannelsRow) {
                            CharSequence string3 = LocaleController.getString(R.string.SaveToGalleryChannels);
                            charSequenceCreateDescription = SaveToGallerySettingsHelper.channels.createDescription(((BaseFragment) DataSettingsActivity.this).currentAccount);
                            zEnabled = SaveToGallerySettingsHelper.channels.enabled();
                            charSequence2 = string3;
                            z = false;
                        } else {
                            if (i == DataSettingsActivity.this.mobileRow) {
                                string = LocaleController.getString(R.string.WhenUsingMobileData);
                                zEnabled = DownloadController.getInstance(((BaseFragment) DataSettingsActivity.this).currentAccount).mobilePreset.enabled;
                                currentWiFiPreset = DownloadController.getInstance(((BaseFragment) DataSettingsActivity.this).currentAccount).getCurrentMobilePreset();
                            } else if (i == DataSettingsActivity.this.wifiRow) {
                                string = LocaleController.getString(R.string.WhenConnectedOnWiFi);
                                zEnabled = DownloadController.getInstance(((BaseFragment) DataSettingsActivity.this).currentAccount).wifiPreset.enabled;
                                currentWiFiPreset = DownloadController.getInstance(((BaseFragment) DataSettingsActivity.this).currentAccount).getCurrentWiFiPreset();
                            } else {
                                CharSequence string4 = LocaleController.getString(R.string.WhenRoaming);
                                zEnabled = DownloadController.getInstance(((BaseFragment) DataSettingsActivity.this).currentAccount).roamingPreset.enabled;
                                currentRoamingPreset = DownloadController.getInstance(((BaseFragment) DataSettingsActivity.this).currentAccount).getCurrentRoamingPreset();
                                charSequence = null;
                                charSequence2 = string4;
                                z = DataSettingsActivity.this.resetDownloadRow >= 0;
                            }
                            currentRoamingPreset = currentWiFiPreset;
                            charSequence3 = null;
                        }
                        if (currentRoamingPreset != null) {
                            charSequence = charSequenceCreateDescription;
                            sb = new StringBuilder();
                            i2 = 0;
                            z3 = false;
                            i3 = 0;
                            z4 = false;
                            z5 = false;
                            while (true) {
                                iArr = currentRoamingPreset.mask;
                                z6 = z3;
                                if (i2 < iArr.length) {
                                    if (!z3 && (iArr[i2] & 1) != 0) {
                                        z6 = z3;
                                        i3++;
                                        z6 = true;
                                    }
                                    if (!z4 && (iArr[i2] & 4) != 0) {
                                        i3++;
                                        z4 = true;
                                    }
                                    if (z5 && (iArr[i2] & 8) != 0) {
                                        i3++;
                                        z5 = true;
                                    }
                                    i2++;
                                    z3 = z6;
                                    z4 = z4;
                                    z5 = z5;
                                } else {
                                    if (currentRoamingPreset.enabled || i3 == 0) {
                                        zEnabled = zEnabled;
                                        z3 = z3 ? 1 : 0;
                                        sb.append(LocaleController.getString(R.string.NoMediaAutoDownload));
                                    } else {
                                        if (z3) {
                                            sb.append(LocaleController.getString(R.string.AutoDownloadPhotosOn));
                                        }
                                        if (z4) {
                                            if (sb.length() > 0) {
                                                sb.append(", ");
                                            }
                                            sb.append(LocaleController.getString(R.string.AutoDownloadVideosOn));
                                            sb.append(String.format(" (%1$s)", AndroidUtilities.formatFileSize(currentRoamingPreset.sizes[DownloadController.typeToIndex(4)], true, false)));
                                        }
                                        if (z5) {
                                            if (sb.length() > 0) {
                                                sb.append(", ");
                                            }
                                            sb.append(LocaleController.getString(R.string.AutoDownloadFilesOn));
                                            sb.append(String.format(" (%1$s)", AndroidUtilities.formatFileSize(currentRoamingPreset.sizes[DownloadController.typeToIndex(8)], true, false)));
                                        }
                                    }
                                    if ((!z3 || z4 || z5) && zEnabled) {
                                    }
                                    z2 = i4;
                                    charSequence4 = sb;
                                }
                            }
                        } else {
                            charSequence = charSequenceCreateDescription;
                            z2 = zEnabled;
                            charSequence4 = charSequence;
                        }
                        CharSequence charSequence5 = charSequence4;
                        notificationsCheckCell.setAnimationsEnabled(true);
                        notificationsCheckCell.setTextAndValueAndCheck(charSequence2, charSequence5, z2, 0, true, z);
                    }
                    charSequence2 = string;
                    z = true;
                    charSequence = charSequence3;
                    if (currentRoamingPreset != null) {
                        charSequence = charSequenceCreateDescription;
                        sb = new StringBuilder();
                        i2 = 0;
                        z3 = false;
                        i3 = 0;
                        z4 = false;
                        z5 = false;
                        while (true) {
                            iArr = currentRoamingPreset.mask;
                            z6 = z3;
                            if (i2 < iArr.length) {
                                if (!z3) {
                                    z6 = z3;
                                    i3++;
                                    z6 = true;
                                }
                                if (!z4) {
                                    i3++;
                                    z4 = true;
                                }
                                if (z5) {
                                }
                                i2++;
                                z3 = z6;
                                z4 = z4;
                                z5 = z5;
                            } else {
                                if (currentRoamingPreset.enabled) {
                                    zEnabled = zEnabled;
                                    z3 = z3 ? 1 : 0;
                                    sb.append(LocaleController.getString(R.string.NoMediaAutoDownload));
                                } else {
                                    zEnabled = zEnabled;
                                    z3 = z3 ? 1 : 0;
                                    sb.append(LocaleController.getString(R.string.NoMediaAutoDownload));
                                }
                                i4 = !z3 ? 1 : 1;
                                z2 = i4;
                                charSequence4 = sb;
                            }
                        }
                    } else {
                        charSequence = charSequenceCreateDescription;
                        z2 = zEnabled;
                        charSequence4 = charSequence;
                    }
                    CharSequence charSequence6 = charSequence4;
                    notificationsCheckCell.setAnimationsEnabled(true);
                    notificationsCheckCell.setTextAndValueAndCheck(charSequence2, charSequence6, z2, 0, true, z);
                    break;
                case 6:
                    TextCell textCell = (TextCell) viewHolder.itemView;
                    if (i == DataSettingsActivity.this.storageUsageRow) {
                        if (DataSettingsActivity.this.storageUsageLoading) {
                            textCell.setTextAndValueAndColorfulIcon(LocaleController.getString(R.string.StorageUsage), _UrlKt.FRAGMENT_ENCODE_SET, false, R.drawable.msg_filled_storageusage, -11565578, -13276952, true);
                            textCell.setDrawLoading(true, 45, DataSettingsActivity.this.updateStorageUsageAnimated);
                        } else {
                            textCell.setTextAndValueAndColorfulIcon(LocaleController.getString(R.string.StorageUsage), DataSettingsActivity.this.storageUsageSize <= 0 ? _UrlKt.FRAGMENT_ENCODE_SET : AndroidUtilities.formatFileSize(DataSettingsActivity.this.storageUsageSize), true, R.drawable.msg_filled_storageusage, -11565578, -13276952, true);
                            textCell.setDrawLoading(false, 45, DataSettingsActivity.this.updateStorageUsageAnimated);
                        }
                        DataSettingsActivity.this.updateStorageUsageAnimated = false;
                    } else if (i == DataSettingsActivity.this.dataUsageRow) {
                        StatsController statsController = StatsController.getInstance(((BaseFragment) DataSettingsActivity.this).currentAccount);
                        textCell.setTextAndValueAndColorfulIcon(LocaleController.getString(R.string.NetworkUsage), AndroidUtilities.formatFileSize(statsController.getReceivedBytesCount(0, 6) + statsController.getReceivedBytesCount(1, 6) + statsController.getReceivedBytesCount(2, 6) + statsController.getSentBytesCount(0, 6) + statsController.getSentBytesCount(1, 6) + statsController.getSentBytesCount(2, 6)), true, R.drawable.msg_filled_datausage, -11154873, -14175180, DataSettingsActivity.this.storageNumRow != -1);
                    } else if (i == DataSettingsActivity.this.storageNumRow) {
                        String absolutePath = ((File) DataSettingsActivity.this.storageDirs.get(0)).getAbsolutePath();
                        if (!TextUtils.isEmpty(SharedConfig.storageCacheDir)) {
                            int size = DataSettingsActivity.this.storageDirs.size();
                            while (i4 < size) {
                                String absolutePath2 = ((File) DataSettingsActivity.this.storageDirs.get(i4)).getAbsolutePath();
                                if (absolutePath2.startsWith(SharedConfig.storageCacheDir)) {
                                    absolutePath = absolutePath2;
                                } else {
                                    i4++;
                                }
                            }
                        }
                        textCell.setTextAndValueAndColorfulIcon(LocaleController.getString(R.string.StoragePath), LocaleController.getString((absolutePath == null || absolutePath.contains("/storage/emulated/")) ? R.string.InternalStorage : R.string.SdCard), true, R.drawable.msg_filled_sdcard, -1007845, -1996271, false);
                    }
                    break;
            }
        }

        @Override // androidx.recyclerview.widget.RecyclerView.Adapter
        public void onViewAttachedToWindow(RecyclerView.ViewHolder viewHolder) {
            if (viewHolder.getItemViewType() == 3) {
                TextCheckCell textCheckCell = (TextCheckCell) viewHolder.itemView;
                int adapterPosition = viewHolder.getAdapterPosition();
                if (adapterPosition == DataSettingsActivity.this.enableCacheStreamRow) {
                    textCheckCell.setChecked(SharedConfig.saveStreamMedia);
                    return;
                }
                if (adapterPosition == DataSettingsActivity.this.enableStreamRow) {
                    textCheckCell.setChecked(SharedConfig.streamMedia);
                    return;
                }
                if (adapterPosition == DataSettingsActivity.this.enableAllStreamRow) {
                    textCheckCell.setChecked(SharedConfig.streamAllVideo);
                    return;
                }
                if (adapterPosition == DataSettingsActivity.this.enableMkvRow) {
                    textCheckCell.setChecked(SharedConfig.streamMkv);
                } else if (adapterPosition == DataSettingsActivity.this.autoplayGifsRow) {
                    textCheckCell.setChecked(SharedConfig.isAutoplayGifs());
                } else if (adapterPosition == DataSettingsActivity.this.autoplayVideoRow) {
                    textCheckCell.setChecked(SharedConfig.isAutoplayVideo());
                }
            }
        }

        public boolean isRowEnabled(int i) {
            return i == DataSettingsActivity.this.mobileRow || i == DataSettingsActivity.this.roamingRow || i == DataSettingsActivity.this.wifiRow || i == DataSettingsActivity.this.storageUsageRow || i == DataSettingsActivity.this.useLessDataForCallsRow || i == DataSettingsActivity.this.dataUsageRow || i == DataSettingsActivity.this.proxyRow || i == DataSettingsActivity.this.clearDraftsRow || i == DataSettingsActivity.this.enableCacheStreamRow || i == DataSettingsActivity.this.enableStreamRow || i == DataSettingsActivity.this.enableAllStreamRow || i == DataSettingsActivity.this.enableMkvRow || i == DataSettingsActivity.this.quickRepliesRow || i == DataSettingsActivity.this.autoplayVideoRow || i == DataSettingsActivity.this.autoplayGifsRow || i == DataSettingsActivity.this.storageNumRow || i == DataSettingsActivity.this.saveToGalleryGroupsRow || i == DataSettingsActivity.this.saveToGalleryPeerRow || i == DataSettingsActivity.this.saveToGalleryChannelsRow || i == DataSettingsActivity.this.resetDownloadRow;
        }

        @Override // org.telegram.ui.Components.RecyclerListView.SelectionAdapter
        public boolean isEnabled(RecyclerView.ViewHolder viewHolder) {
            return isRowEnabled(viewHolder.getAdapterPosition());
        }

        @Override // androidx.recyclerview.widget.RecyclerView.Adapter
        public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
            View shadowSectionCell;
            if (i == 0) {
                shadowSectionCell = new ShadowSectionCell(this.mContext);
            } else if (i == 1) {
                shadowSectionCell = new TextSettingsCell(this.mContext);
            } else if (i == 2) {
                shadowSectionCell = new HeaderCell(this.mContext, 22);
            } else if (i == 3) {
                shadowSectionCell = new TextCheckCell(this.mContext);
            } else if (i == 4) {
                shadowSectionCell = new TextInfoPrivacyCell(this.mContext);
            } else if (i == 5) {
                shadowSectionCell = new NotificationsCheckCell(this.mContext);
            } else {
                shadowSectionCell = new TextCell(this.mContext);
            }
            shadowSectionCell.setLayoutParams(new RecyclerView.LayoutParams(-1, -2));
            return new RecyclerListView.Holder(shadowSectionCell);
        }

        @Override // androidx.recyclerview.widget.RecyclerView.Adapter
        public int getItemViewType(int i) {
            if (i == DataSettingsActivity.this.mediaDownloadSection2Row || i == DataSettingsActivity.this.usageSection2Row || i == DataSettingsActivity.this.callsSection2Row || i == DataSettingsActivity.this.proxySection2Row || i == DataSettingsActivity.this.autoplaySectionRow || i == DataSettingsActivity.this.clearDraftsSectionRow || i == DataSettingsActivity.this.saveToGalleryDividerRow) {
                return 0;
            }
            if (i == DataSettingsActivity.this.mediaDownloadSectionRow || i == DataSettingsActivity.this.streamSectionRow || i == DataSettingsActivity.this.callsSectionRow || i == DataSettingsActivity.this.usageSectionRow || i == DataSettingsActivity.this.proxySectionRow || i == DataSettingsActivity.this.autoplayHeaderRow || i == DataSettingsActivity.this.saveToGallerySectionRow) {
                return 2;
            }
            if (i == DataSettingsActivity.this.enableCacheStreamRow || i == DataSettingsActivity.this.enableStreamRow || i == DataSettingsActivity.this.enableAllStreamRow || i == DataSettingsActivity.this.enableMkvRow || i == DataSettingsActivity.this.autoplayGifsRow || i == DataSettingsActivity.this.autoplayVideoRow) {
                return 3;
            }
            if (i == DataSettingsActivity.this.enableAllStreamInfoRow) {
                return 4;
            }
            if (i == DataSettingsActivity.this.mobileRow || i == DataSettingsActivity.this.wifiRow || i == DataSettingsActivity.this.roamingRow || i == DataSettingsActivity.this.saveToGalleryGroupsRow || i == DataSettingsActivity.this.saveToGalleryPeerRow || i == DataSettingsActivity.this.saveToGalleryChannelsRow) {
                return 5;
            }
            return (i == DataSettingsActivity.this.storageUsageRow || i == DataSettingsActivity.this.dataUsageRow || i == DataSettingsActivity.this.storageNumRow) ? 6 : 1;
        }
    }

    @Override // org.telegram.ui.ActionBar.BaseFragment
    public ArrayList getThemeDescriptions() {
        ArrayList arrayList = new ArrayList();
        arrayList.add(new ThemeDescription(this.listView, ThemeDescription.FLAG_CELLBACKGROUNDCOLOR, new Class[]{TextSettingsCell.class, TextCheckCell.class, HeaderCell.class, NotificationsCheckCell.class}, null, null, null, Theme.key_windowBackgroundWhite));
        arrayList.add(new ThemeDescription(this.fragmentView, ThemeDescription.FLAG_BACKGROUND, null, null, null, null, Theme.key_windowBackgroundGray));
        arrayList.add(new ThemeDescription(this.listView, ThemeDescription.FLAG_LISTGLOWCOLOR, null, null, null, null, Theme.key_actionBarDefault));
        arrayList.add(new ThemeDescription(this.actionBar, ThemeDescription.FLAG_AB_ITEMSCOLOR, null, null, null, null, Theme.key_actionBarDefaultIcon));
        arrayList.add(new ThemeDescription(this.actionBar, ThemeDescription.FLAG_AB_TITLECOLOR, null, null, null, null, Theme.key_actionBarDefaultTitle));
        arrayList.add(new ThemeDescription(this.actionBar, ThemeDescription.FLAG_AB_SELECTORCOLOR, null, null, null, null, Theme.key_actionBarDefaultSelector));
        int i = Theme.key_windowBackgroundWhiteBlackText;
        arrayList.add(new ThemeDescription(this.listView, 0, new Class[]{NotificationsCheckCell.class}, new String[]{"textView"}, (Paint[]) null, (Drawable[]) null, (ThemeDescription.ThemeDescriptionDelegate) null, i));
        int i2 = Theme.key_windowBackgroundWhiteGrayText2;
        arrayList.add(new ThemeDescription(this.listView, 0, new Class[]{NotificationsCheckCell.class}, new String[]{"valueTextView"}, (Paint[]) null, (Drawable[]) null, (ThemeDescription.ThemeDescriptionDelegate) null, i2));
        int i3 = Theme.key_switchTrack;
        arrayList.add(new ThemeDescription(this.listView, 0, new Class[]{NotificationsCheckCell.class}, new String[]{"checkBox"}, (Paint[]) null, (Drawable[]) null, (ThemeDescription.ThemeDescriptionDelegate) null, i3));
        int i4 = Theme.key_switchTrackChecked;
        arrayList.add(new ThemeDescription(this.listView, 0, new Class[]{NotificationsCheckCell.class}, new String[]{"checkBox"}, (Paint[]) null, (Drawable[]) null, (ThemeDescription.ThemeDescriptionDelegate) null, i4));
        arrayList.add(new ThemeDescription(this.listView, ThemeDescription.FLAG_SELECTOR, null, null, null, null, Theme.key_listSelector));
        arrayList.add(new ThemeDescription(this.listView, 0, new Class[]{View.class}, Theme.dividerPaint, null, null, Theme.key_divider));
        arrayList.add(new ThemeDescription(this.listView, 0, new Class[]{TextSettingsCell.class}, new String[]{"textView"}, (Paint[]) null, (Drawable[]) null, (ThemeDescription.ThemeDescriptionDelegate) null, i));
        arrayList.add(new ThemeDescription(this.listView, 0, new Class[]{TextSettingsCell.class}, new String[]{"valueTextView"}, (Paint[]) null, (Drawable[]) null, (ThemeDescription.ThemeDescriptionDelegate) null, Theme.key_windowBackgroundWhiteValueText));
        arrayList.add(new ThemeDescription(this.listView, 0, new Class[]{HeaderCell.class}, new String[]{"textView"}, (Paint[]) null, (Drawable[]) null, (ThemeDescription.ThemeDescriptionDelegate) null, Theme.key_windowBackgroundWhiteBlueHeader));
        arrayList.add(new ThemeDescription(this.listView, 0, new Class[]{TextCheckCell.class}, new String[]{"textView"}, (Paint[]) null, (Drawable[]) null, (ThemeDescription.ThemeDescriptionDelegate) null, i));
        arrayList.add(new ThemeDescription(this.listView, 0, new Class[]{TextCheckCell.class}, new String[]{"valueTextView"}, (Paint[]) null, (Drawable[]) null, (ThemeDescription.ThemeDescriptionDelegate) null, i2));
        arrayList.add(new ThemeDescription(this.listView, 0, new Class[]{TextCheckCell.class}, new String[]{"checkBox"}, (Paint[]) null, (Drawable[]) null, (ThemeDescription.ThemeDescriptionDelegate) null, i3));
        arrayList.add(new ThemeDescription(this.listView, 0, new Class[]{TextCheckCell.class}, new String[]{"checkBox"}, (Paint[]) null, (Drawable[]) null, (ThemeDescription.ThemeDescriptionDelegate) null, i4));
        arrayList.add(new ThemeDescription(this.listView, 0, new Class[]{TextInfoPrivacyCell.class}, new String[]{"textView"}, (Paint[]) null, (Drawable[]) null, (ThemeDescription.ThemeDescriptionDelegate) null, Theme.key_windowBackgroundWhiteGrayText4));
        return arrayList;
    }

    @Override // org.telegram.ui.ActionBar.BaseFragment
    public void onInsets(int i, int i2, int i3, int i4) {
        this.listView.setPadding(0, 0, 0, i4);
        this.listView.setClipToPadding(false);
    }
}
