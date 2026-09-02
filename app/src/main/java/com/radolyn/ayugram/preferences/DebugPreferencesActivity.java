package com.radolyn.ayugram.preferences;

import android.content.SharedPreferences;
import android.os.Debug;
import android.os.Handler;
import android.os.Looper;
import android.system.Os;
import android.system.OsConstants;
import android.util.SparseArray;
import android.util.SparseIntArray;
import android.util.SparseLongArray;
import android.view.View;
import com.exteragram.messenger.preferences.BasePreferencesActivity;
import com.exteragram.messenger.utils.chats.ChatUtils;
import com.google.android.exoplayer2.util.Consumer;
import com.radolyn.ayugram.AyuConfig;
import com.radolyn.ayugram.AyuState;
import com.radolyn.ayugram.controllers.AyuMessagesController;
import com.radolyn.ayugram.database.AyuData;
import j$.util.List;
import j$.util.Map;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Locale;
import org.lsposed.lsparanoid.Deobfuscator$AyuGram4A$TMessagesProj;
import org.telegram.messenger.AndroidUtilities;
import org.telegram.messenger.FileLoader;
import org.telegram.messenger.LocaleController;
import org.telegram.messenger.PushListenerController;
import org.telegram.messenger.R;
import org.telegram.messenger.SharedConfig;
import org.telegram.messenger.UserConfig;
import org.telegram.ui.Components.BulletinFactory;
import org.telegram.ui.Components.UItem;
import org.telegram.ui.Components.UniversalAdapter;
import org.telegram.ui.Components.UniversalRecyclerView;

public class DebugPreferencesActivity extends BasePreferencesActivity {
    private final SparseIntArray tidRowMap = new SparseIntArray();
    private final SparseArray threadNames = new SparseArray();
    private final SparseLongArray lastTidCpu = new SparseLongArray();
    private final HashMap tidCpuPercent = new HashMap();
    private double currentRamMb = 0.0d;
    private final long ticksPerSecond = getTicksPerSecond();
    private final Handler statsHandler = new Handler(Looper.getMainLooper());
    private final Runnable statsRunnable = new Runnable() { // from class: com.radolyn.ayugram.preferences.DebugPreferencesActivity.1
        @Override // java.lang.Runnable
        public void run() {
            DebugPreferencesActivity.this.refreshStats();
            DebugPreferencesActivity.this.statsHandler.postDelayed(this, 1000L);
        }
    };
    private double averageGetFilePathTime = 0.0d;

    private enum ItemId {
        HEADER_GENERAL,
        FORCE_SHOW_DOWNLOAD,
        PROBE_OTHER_ACCOUNTS,
        DISABLE_HOOK,
        SHADOW_BUTTONS,
        HEADER_DEBUG,
        WAL_MODE,
        SHOW_SCREENSHOT,
        SHADOW_COUNTERS,
        GOOGLE_AVAILABLE,
        FCM_TOKEN,
        PUSH_STATUS,
        PUSHES_RECEIVED,
        GET_FILE_PATH_AVG,
        RESET_ALERTS,
        SHADOW_DEBUG,
        HEADER_STATE,
        DELETED_MESSAGES_COUNT,
        DELETED_DIALOGS_COUNT,
        LAST_SEEN_COUNT,
        LAST_MESSAGES_COUNT,
        SHADOW_STATE,
        HEADER_STATS,
        MEMORY_USAGE;

        public int getId() {
            return ordinal() + 1;
        }
    }

    @Override // org.telegram.ui.ActionBar.BaseFragment
    public boolean onFragmentCreate() {
        super.onFragmentCreate();
        initThreadRows();
        return true;
    }

    @Override // com.exteragram.messenger.preferences.BasePreferencesActivity, org.telegram.ui.ActionBar.BaseFragment
    public void onResume() {
        super.onResume();
        this.statsHandler.post(this.statsRunnable);
        UniversalRecyclerView universalRecyclerView = this.listView;
        if (universalRecyclerView != null) {
            universalRecyclerView.adapter.update(false);
        }
    }

    @Override // com.exteragram.messenger.preferences.BasePreferencesActivity, org.telegram.ui.ActionBar.BaseFragment
    public void onPause() {
        this.statsHandler.removeCallbacks(this.statsRunnable);
        super.onPause();
    }

    @Override // com.exteragram.messenger.preferences.BasePreferencesActivity
    public String getTitle() {
        return "ðŸ¤”";
    }

    @Override // com.exteragram.messenger.preferences.BasePreferencesActivity
    protected void fillItems(ArrayList arrayList, UniversalAdapter universalAdapter) {
        arrayList.add(UItem.asHeader(LocaleController.getString(R.string.General)));
        arrayList.add(UItem.asCheck(ItemId.FORCE_SHOW_DOWNLOAD.getId(), "Force show download buttons").setChecked(AyuConfig.forceShowDownloadButtons));
        arrayList.add(UItem.asCheck(ItemId.PROBE_OTHER_ACCOUNTS.getId(), "Peek online using other accounts").setChecked(AyuConfig.probeUsingOtherAccounts));
        arrayList.add(UItem.asCheck(ItemId.DISABLE_HOOK.getId(), "Disable hook").setChecked(AyuConfig.disableHook));
        arrayList.add(UItem.asShadow());
        arrayList.add(UItem.asHeader(LocaleController.getString(R.string.SettingsDebug)));
        arrayList.add(UItem.asCheck(ItemId.WAL_MODE.getId(), LocaleController.getString(R.string.WALMode)).setChecked(AyuConfig.WALMode));
        arrayList.add(UItem.asCheck(ItemId.SHOW_SCREENSHOT.getId(), "Show screenshot btn").setChecked(AyuConfig.showScreenshot));
        arrayList.add(UItem.asShadow());
        arrayList.add(UItem.asButton(ItemId.GOOGLE_AVAILABLE.getId(), "FCM available", (PushListenerController.GooglePushListenerServiceProvider.INSTANCE.hasServices() ? "-107945804999612" : "-107894265392060")));
        arrayList.add(UItem.asButton(ItemId.FCM_TOKEN.getId(), "FCM token", SharedConfig.pushString));
        arrayList.add(UItem.asButton(ItemId.PUSH_STATUS.getId(), "Push status", SharedConfig.pushStringStatus));
        arrayList.add(UItem.asButton(ItemId.PUSHES_RECEIVED.getId(), LocaleController.getString(R.string.PushNotificationCount), String.valueOf(AyuState.getFcmCounter())));
        arrayList.add(UItem.asButton(ItemId.GET_FILE_PATH_AVG.getId(), "getPath average", this.averageGetFilePathTime > 0.0d ? this.averageGetFilePathTime + " ms" : "..."));
        arrayList.add(UItem.asButton(ItemId.RESET_ALERTS.getId(), "Reset alerts", "click"));
        arrayList.add(UItem.asShadow());
        arrayList.add(UItem.asHeader("Database State"));
        arrayList.add(UItem.asButton(ItemId.DELETED_MESSAGES_COUNT.getId(), "Deleted messages", String.valueOf(AyuData.getDeletedMessageDao().getDeletedCount())));
        arrayList.add(UItem.asButton(ItemId.DELETED_DIALOGS_COUNT.getId(), "Deleted dialogs", String.valueOf(AyuData.getDeletedDialogDao().getDeletedCount())));
        arrayList.add(UItem.asButton(ItemId.LAST_SEEN_COUNT.getId(), "Last seen", String.valueOf(AyuData.getSpyDao().getLastSeenCount())));
        int lastMessagesCount = 0;
        for (int i = 0; i < 16; i++) {
            lastMessagesCount += AyuMessagesController.getInstance(i).getLastMessagesCount();
        }
        arrayList.add(UItem.asButton(ItemId.LAST_MESSAGES_COUNT.getId(), "Last messages", String.valueOf(lastMessagesCount)));
        arrayList.add(UItem.asShadow());
        arrayList.add(UItem.asHeader("Stats"));
        arrayList.add(UItem.asButton(ItemId.MEMORY_USAGE.getId(), "RAM", String.format(Locale.US, "%.2f MB", Double.valueOf(this.currentRamMb))));
        ArrayList arrayList2 = new ArrayList(this.tidCpuPercent.keySet());
        List.EL.sort(arrayList2, new Comparator() { // from class: com.radolyn.ayugram.preferences.DebugPreferencesActivity$$ExternalSyntheticLambda0
            @Override // java.util.Comparator
            public final int compare(Object obj, Object obj2) {
                return this.f$0.lambda$fillItems$0((Integer) obj, (Integer) obj2);
            }
        });
        int iMin = Math.min(arrayList2.size(), 20);
        for (int i2 = 0; i2 < iMin; i2++) {
            Integer num = (Integer) arrayList2.get(i2);
            int iIntValue = num.intValue();
            String str = (String) this.threadNames.get(iIntValue, "TID " + iIntValue);
            Double d = (Double) Map.EL.getOrDefault(this.tidCpuPercent, num, Double.valueOf(0.0d));
            d.doubleValue();
            arrayList.add(UItem.asButton(50000 + i2, "CPU " + str, String.format(Locale.US, "%.1f %%", d)));
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ int lambda$fillItems$0(Integer num, Integer num2) {
        HashMap map = this.tidCpuPercent;
        Double dValueOf = Double.valueOf(0.0d);
        return Double.compare(((Double) Map.EL.getOrDefault(map, num2, dValueOf)).doubleValue(), ((Double) Map.EL.getOrDefault(this.tidCpuPercent, num, dValueOf)).doubleValue());
    }

    @Override // com.exteragram.messenger.preferences.BasePreferencesActivity
    protected void onClick(UItem uItem, View view, int i, float f, float f2) {
        int i2;
        if (uItem == null || (i2 = uItem.id) >= 50000 || i2 <= 0 || i2 > ItemId.values().length) {
            return;
        }
        switch (AnonymousClass2.$SwitchMap$com$radolyn$ayugram$preferences$DebugPreferencesActivity$ItemId[ItemId.values()[uItem.id - 1].ordinal()]) {
            case 1:
                toggleBooleanSettingAndRefresh(AyuConfig.preferences, "forceShowDownloadButtons", uItem, new Consumer() { // from class: com.radolyn.ayugram.preferences.DebugPreferencesActivity$$ExternalSyntheticLambda1
                    @Override // com.google.android.exoplayer2.util.Consumer
                    public final void accept(Object obj) {
                        AyuConfig.forceShowDownloadButtons = ((Boolean) obj).booleanValue();
                    }
                });
                break;
            case 2:
                toggleBooleanSettingAndRefresh(AyuConfig.preferences, "probeUsingOtherAccounts", uItem, new Consumer() { // from class: com.radolyn.ayugram.preferences.DebugPreferencesActivity$$ExternalSyntheticLambda2
                    @Override // com.google.android.exoplayer2.util.Consumer
                    public final void accept(Object obj) {
                        AyuConfig.probeUsingOtherAccounts = ((Boolean) obj).booleanValue();
                    }
                });
                break;
            case 3:
                toggleBooleanSettingAndRefresh(AyuConfig.preferences, "disableHook", uItem, new Consumer() { // from class: com.radolyn.ayugram.preferences.DebugPreferencesActivity$$ExternalSyntheticLambda3
                    @Override // com.google.android.exoplayer2.util.Consumer
                    public final void accept(Object obj) {
                        AyuConfig.disableHook = ((Boolean) obj).booleanValue();
                    }
                });
                break;
            case 4:
                toggleBooleanSettingAndRefresh(AyuConfig.preferences, "walMode", uItem, new Consumer() { // from class: com.radolyn.ayugram.preferences.DebugPreferencesActivity$$ExternalSyntheticLambda4
                    @Override // com.google.android.exoplayer2.util.Consumer
                    public final void accept(Object obj) {
                        AyuConfig.WALMode = ((Boolean) obj).booleanValue();
                    }
                });
                break;
            case 5:
                toggleBooleanSettingAndRefresh(AyuConfig.preferences, "showScreenshot", uItem, new Consumer() { // from class: com.radolyn.ayugram.preferences.DebugPreferencesActivity$$ExternalSyntheticLambda5
                    @Override // com.google.android.exoplayer2.util.Consumer
                    public final void accept(Object obj) {
                        AyuConfig.showScreenshot = ((Boolean) obj).booleanValue();
                    }
                });
                break;
            case 6:
                AndroidUtilities.addToClipboard(SharedConfig.pushString);
                break;
            case 7:
                AndroidUtilities.addToClipboard(SharedConfig.pushStringStatus);
                break;
            case 8:
                this.averageGetFilePathTime = 0.0d;
                setAverageGetFilePathTime();
                break;
            case 9:
                SharedPreferences.Editor editor = AyuConfig.editor;
                String string = "sawFirstLaunchAlert";
                AyuConfig.sawFirstLaunchAlert = false;
                editor.putBoolean(string, false).apply();
                SharedPreferences.Editor editor2 = AyuConfig.editor;
                String string2 = "sawExteraChatsAlert";
                AyuConfig.sawExteraChatsAlert = false;
                editor2.putBoolean(string2, false).apply();
                SharedPreferences.Editor editor3 = AyuConfig.editor;
                String string3 = "sawLocalPremiumAlert";
                AyuConfig.sawLocalPremiumAlert = false;
                editor3.putBoolean(string3, false).apply();
                SharedPreferences.Editor editor4 = AyuConfig.editor;
                String string4 = "sawSaveAttachmentsAlert";
                AyuConfig.sawSaveAttachmentsAlert = false;
                editor4.putBoolean(string4, false).apply();
                BulletinFactory.of(this).createSimpleBulletin(R.raw.contact_check, "Done~").show();
                break;
        }
    }

    /* JADX INFO: renamed from: com.radolyn.ayugram.preferences.DebugPreferencesActivity$2, reason: invalid class name */
    static /* synthetic */ class AnonymousClass2 {
        static final /* synthetic */ int[] $SwitchMap$com$radolyn$ayugram$preferences$DebugPreferencesActivity$ItemId;

        static {
            int[] iArr = new int[ItemId.values().length];
            $SwitchMap$com$radolyn$ayugram$preferences$DebugPreferencesActivity$ItemId = iArr;
            try {
                iArr[ItemId.FORCE_SHOW_DOWNLOAD.ordinal()] = 1;
            } catch (NoSuchFieldError unused) {
            }
            try {
                $SwitchMap$com$radolyn$ayugram$preferences$DebugPreferencesActivity$ItemId[ItemId.PROBE_OTHER_ACCOUNTS.ordinal()] = 2;
            } catch (NoSuchFieldError unused2) {
            }
            try {
                $SwitchMap$com$radolyn$ayugram$preferences$DebugPreferencesActivity$ItemId[ItemId.DISABLE_HOOK.ordinal()] = 3;
            } catch (NoSuchFieldError unused3) {
            }
            try {
                $SwitchMap$com$radolyn$ayugram$preferences$DebugPreferencesActivity$ItemId[ItemId.WAL_MODE.ordinal()] = 4;
            } catch (NoSuchFieldError unused4) {
            }
            try {
                $SwitchMap$com$radolyn$ayugram$preferences$DebugPreferencesActivity$ItemId[ItemId.SHOW_SCREENSHOT.ordinal()] = 5;
            } catch (NoSuchFieldError unused5) {
            }
            try {
                $SwitchMap$com$radolyn$ayugram$preferences$DebugPreferencesActivity$ItemId[ItemId.FCM_TOKEN.ordinal()] = 6;
            } catch (NoSuchFieldError unused6) {
            }
            try {
                $SwitchMap$com$radolyn$ayugram$preferences$DebugPreferencesActivity$ItemId[ItemId.PUSH_STATUS.ordinal()] = 7;
            } catch (NoSuchFieldError unused7) {
            }
            try {
                $SwitchMap$com$radolyn$ayugram$preferences$DebugPreferencesActivity$ItemId[ItemId.GET_FILE_PATH_AVG.ordinal()] = 8;
            } catch (NoSuchFieldError unused8) {
            }
            try {
                $SwitchMap$com$radolyn$ayugram$preferences$DebugPreferencesActivity$ItemId[ItemId.RESET_ALERTS.ordinal()] = 9;
            } catch (NoSuchFieldError unused9) {
            }
        }
    }

    private void setAverageGetFilePathTime() {
        ChatUtils.utilsQueue.postRunnable(new Runnable() { // from class: com.radolyn.ayugram.preferences.DebugPreferencesActivity$$ExternalSyntheticLambda6
            @Override // java.lang.Runnable
            public final void run() {
                this.f$0.lambda$setAverageGetFilePathTime$7();
            }
        });
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$setAverageGetFilePathTime$7() {
        FileLoader fileLoader = FileLoader.getInstance(UserConfig.selectedAccount);
        long jCurrentTimeMillis = System.currentTimeMillis();
        for (int i = 0; i < 100; i++) {
            fileLoader.getFileDatabase().getPath(5469733571210003142L, 2, 3, true);
        }
        final double dCurrentTimeMillis = (System.currentTimeMillis() - jCurrentTimeMillis) / 100.0d;
        AndroidUtilities.runOnUIThread(new Runnable() { // from class: com.radolyn.ayugram.preferences.DebugPreferencesActivity$$ExternalSyntheticLambda7
            @Override // java.lang.Runnable
            public final void run() {
                this.f$0.lambda$setAverageGetFilePathTime$6(dCurrentTimeMillis);
            }
        });
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$setAverageGetFilePathTime$6(double d) {
        this.averageGetFilePathTime = d;
        UniversalRecyclerView universalRecyclerView = this.listView;
        if (universalRecyclerView != null) {
            universalRecyclerView.adapter.update(true);
        }
    }

    private void initThreadRows() {
        try {
            File[] fileArrListFiles = new File("/proc/self/task").listFiles();
            if (fileArrListFiles == null) {
                return;
            }
            for (File file : fileArrListFiles) {
                int i = Integer.parseInt(file.getName());
                String strTrim = "tid " + i;
                try {
                    BufferedReader bufferedReader = new BufferedReader(new FileReader(new File(file, "comm")));
                    try {
                        String line = bufferedReader.readLine();
                        if (line != null) {
                            strTrim = line.trim();
                        }
                        bufferedReader.close();
                        this.threadNames.put(i, strTrim);
                        this.lastTidCpu.put(i, getThreadCpuTicks(i));
                        this.tidCpuPercent.put(Integer.valueOf(i), Double.valueOf(0.0d));
                    } catch (Throwable th) {
                        try {
                            bufferedReader.close();
                        } catch (Throwable th2) {
                            th.addSuppressed(th2);
                        }
                        throw th;
                    }
                } catch (Exception unused) {
                }
            }
        } catch (Exception unused2) {
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void refreshStats() {
        Debug.MemoryInfo memoryInfo = new Debug.MemoryInfo();
        Debug.getMemoryInfo(memoryInfo);
        this.currentRamMb = ((double) memoryInfo.getTotalPss()) / 1024.0d;
        for (int i = 0; i < this.threadNames.size(); i++) {
            int iKeyAt = this.threadNames.keyAt(i);
            long threadCpuTicks = getThreadCpuTicks(iKeyAt);
            long j = threadCpuTicks - this.lastTidCpu.get(iKeyAt, threadCpuTicks);
            this.lastTidCpu.put(iKeyAt, threadCpuTicks);
            long j2 = this.ticksPerSecond;
            this.tidCpuPercent.put(Integer.valueOf(iKeyAt), Double.valueOf(j2 > 0 ? (j * 100.0d) / j2 : 0.0d));
        }
        UniversalRecyclerView universalRecyclerView = this.listView;
        if (universalRecyclerView != null) {
            universalRecyclerView.adapter.update(false);
        }
    }

    private long getThreadCpuTicks(int i) {
        try {
            BufferedReader bufferedReader = new BufferedReader(new FileReader("/proc/self/task/" + i + "/stat"));
            try {
                String line = bufferedReader.readLine();
                if (line != null) {
                    String[] strArrSplit = line.split("\\s+");
                    long j = Long.parseLong(strArrSplit[13]) + Long.parseLong(strArrSplit[14]);
                    bufferedReader.close();
                    return j;
                }
                bufferedReader.close();
                return 0L;
            } catch (Throwable th) {
                try {
                    bufferedReader.close();
                } catch (Throwable th2) {
                    th.addSuppressed(th2);
                }
                throw th;
            }
        } catch (Exception unused) {
            return 0L;
        }
        return 0L;
    }

    private long getTicksPerSecond() {
        try {
            return Os.sysconf(OsConstants._SC_CLK_TCK);
        } catch (Exception unused) {
            return 100L;
        }
    }
}
