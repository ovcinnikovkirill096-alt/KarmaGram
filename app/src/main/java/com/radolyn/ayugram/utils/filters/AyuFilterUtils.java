package com.radolyn.ayugram.utils.filters;

import android.content.DialogInterface;
import com.exteragram.messenger.export.output.json.JsonContext$$ExternalSyntheticLambda17;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonSyntaxException;
import com.radolyn.ayugram.AyuConfig;
import com.radolyn.ayugram.controllers.AyuFilterCacheController;
import com.radolyn.ayugram.database.AyuData;
import com.radolyn.ayugram.database.entities.RegexFilter;
import com.radolyn.ayugram.database.entities.RegexFilterGlobalExclusion;
import com.radolyn.ayugram.preferences.FiltersPreferencesActivity;
import com.radolyn.ayugram.preferences.utils.FiltersImportBottomSheet;
import com.radolyn.ayugram.utils.AyuMessageUtils;
import com.radolyn.ayugram.utils.network.AyuRequestUtils;
import j$.util.Collection;
import j$.util.Objects;
import j$.util.function.Function$CC;
import j$.util.function.Predicate$CC;
import j$.util.stream.Collectors;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.UUID;
import java.util.function.Function;
import java.util.function.Predicate;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.ResponseBody;
import org.telegram.messenger.AndroidUtilities;
import org.telegram.messenger.DialogObject;
import org.telegram.messenger.LocaleController;
import org.telegram.messenger.R;
import org.telegram.messenger.UserConfig;
import org.telegram.tgnet.TLObject;
import org.telegram.ui.ActionBar.BaseFragment;
import org.telegram.ui.Components.BulletinFactory;

public class AyuFilterUtils {
    private static final int BACKUP_VERSION = 2;
    private static final HashSet<String> EMPTY_STRING_SET = new HashSet<>();
    private static HashSet<Long> shadowBanList;

    public static class ApplyChanges {
        public ArrayList<RegexFilter> filtersOverrides;
        public ArrayList<RegexFilterGlobalExclusion> newExclusions;
        public ArrayList<RegexFilter> newFilters;
        public HashMap<Long, String> peersToBeResolved;
        public ArrayList<RegexFilterGlobalExclusion> removeExclusions;
        public ArrayList<String> removeFiltersById;
    }

    public static class Backup {
        public List<BackupExclusion> exclusions;
        public List<RegexFilter> filters;
        public HashMap<Long, String> peers;
        public List<BackupExclusion> removeExclusions;
        public List<String> removeFiltersById;
        public int version;

        public static class BackupExclusion {
            public long dialogId;
            public String filterId;
        }
    }

    static {
        reloadShadowBan();
    }

    public static void reloadShadowBan() {
        shadowBanList = loadShadowBanList();
    }

    private static HashSet<Long> loadShadowBanList() {
        return (HashSet) Collection.EL.stream(AyuConfig.preferences.getStringSet("shadowBanList", EMPTY_STRING_SET)).map(new Function() { // from class: com.radolyn.ayugram.utils.filters.AyuFilterUtils$$ExternalSyntheticLambda8
            public /* synthetic */ Function andThen(Function function) {
                return Function$CC.$default$andThen(this, function);
            }

            @Override // java.util.function.Function
            public final Object apply(Object obj) {
                return Long.valueOf(Long.parseLong((String) obj));
            }

            public /* synthetic */ Function compose(Function function) {
                return Function$CC.$default$compose(this, function);
            }
        }).collect(Collectors.toCollection(new AyuFilterUtils$$ExternalSyntheticLambda9()));
    }

    public static Set<Long> getShadowBanList() {
        return shadowBanList;
    }

    public static void addShadowBan(long j) {
        shadowBanList.add(Long.valueOf(j));
        setShadowBanList();
    }

    public static void removeShadowBan(long j) {
        shadowBanList.remove(Long.valueOf(j));
        setShadowBanList();
    }

    public static boolean isShadowBanned(long j) {
        return shadowBanList.contains(Long.valueOf(j));
    }

    private static void setShadowBanList() {
        AyuConfig.preferences.edit().putStringSet("shadowBanList", new HashSet((java.util.Collection) Collection.EL.stream(shadowBanList).map(new JsonContext$$ExternalSyntheticLambda17()).collect(Collectors.toSet()))).apply();
    }

    public static void importFromLink(DialogInterface dialogInterface, BaseFragment baseFragment, String str) {
        if (str.isEmpty()) {
            BulletinFactory.of(baseFragment).createSimpleBulletin(R.raw.error, LocaleController.getString(R.string.FiltersToastFailFetch)).show();
        } else {
            new OkHttpClient.Builder().followRedirects(true).build().newCall(new Request.Builder().url(str).build()).enqueue(new AnonymousClass1(dialogInterface, baseFragment, str));
        }
    }

    /* JADX INFO: renamed from: com.radolyn.ayugram.utils.filters.AyuFilterUtils$1, reason: invalid class name */
    class AnonymousClass1 implements Callback {
        final /* synthetic */ DialogInterface val$dialog;
        final /* synthetic */ BaseFragment val$fragment;
        final /* synthetic */ String val$link;

        AnonymousClass1(DialogInterface dialogInterface, BaseFragment baseFragment, String str) {
            this.val$dialog = dialogInterface;
            this.val$fragment = baseFragment;
            this.val$link = str;
        }

        @Override // okhttp3.Callback
        public void onFailure(Call call, IOException iOException) {
            final DialogInterface dialogInterface = this.val$dialog;
            final BaseFragment baseFragment = this.val$fragment;
            AndroidUtilities.runOnUIThread(new Runnable() { // from class: com.radolyn.ayugram.utils.filters.AyuFilterUtils$1$$ExternalSyntheticLambda1
                @Override // java.lang.Runnable
                public final void run() {
                    AyuFilterUtils.AnonymousClass1.$r8$lambda$h1L_As4WDVB8q5t84qKLOyGgKFU(dialogInterface, baseFragment);
                }
            });
        }

        public static /* synthetic */ void $r8$lambda$h1L_As4WDVB8q5t84qKLOyGgKFU(DialogInterface dialogInterface, BaseFragment baseFragment) {
            if (dialogInterface != null) {
                dialogInterface.dismiss();
            }
            BulletinFactory.of(baseFragment).createSimpleBulletin(R.raw.error, LocaleController.getString(R.string.FiltersToastFailFetch)).show();
        }

        @Override // okhttp3.Callback
        public void onResponse(Call call, final Response response) {
            final DialogInterface dialogInterface = this.val$dialog;
            final BaseFragment baseFragment = this.val$fragment;
            final String str = this.val$link;
            AndroidUtilities.runOnUIThread(new Runnable() { // from class: com.radolyn.ayugram.utils.filters.AyuFilterUtils$1$$ExternalSyntheticLambda0
                @Override // java.lang.Runnable
                public final void run() {
                    AyuFilterUtils.AnonymousClass1.$r8$lambda$nzH87Sf0VROJRJuuXFRpyFCk3sM(dialogInterface, response, baseFragment, str);
                }
            });
        }

        public static /* synthetic */ void $r8$lambda$nzH87Sf0VROJRJuuXFRpyFCk3sM(DialogInterface dialogInterface, Response response, BaseFragment baseFragment, String str) {
            if (dialogInterface != null) {
                dialogInterface.dismiss();
            }
            try {
                ResponseBody responseBodyBody = response.body();
                Objects.requireNonNull(responseBodyBody);
                AyuFilterUtils.importFilters(baseFragment, responseBodyBody.string());
                AyuConfig.editor.putString("lastFiltersImportLink", str).apply();
            } catch (IOException unused) {
                BulletinFactory.of(baseFragment).createSimpleBulletin(R.raw.error, LocaleController.getString(R.string.FiltersToastFailImport)).show();
            }
        }
    }

    public static void importFilters(final BaseFragment baseFragment, String str) {
        ApplyChanges applyChangesPrepareChanges = prepareChanges(str);
        if (applyChangesPrepareChanges == null) {
            BulletinFactory.of(baseFragment).createSimpleBulletin(R.raw.error, LocaleController.getString(R.string.FiltersToastFailImport)).show();
            return;
        }
        if (applyChangesPrepareChanges.newFilters.isEmpty() && applyChangesPrepareChanges.removeFiltersById.isEmpty() && applyChangesPrepareChanges.filtersOverrides.isEmpty() && applyChangesPrepareChanges.newExclusions.isEmpty() && applyChangesPrepareChanges.removeExclusions.isEmpty() && applyChangesPrepareChanges.peersToBeResolved.isEmpty()) {
            BulletinFactory.of(baseFragment).createSimpleBulletin(R.raw.error, LocaleController.getString(R.string.FiltersToastFailNoChanges)).show();
        } else {
            new FiltersImportBottomSheet(baseFragment, applyChangesPrepareChanges, new Runnable() { // from class: com.radolyn.ayugram.utils.filters.AyuFilterUtils$$ExternalSyntheticLambda10
                @Override // java.lang.Runnable
                public final void run() {
                    AyuFilterUtils.$r8$lambda$u4L87AoxRLUeC7NACQjsjzsK2Mw(baseFragment);
                }
            }).show();
        }
    }

    public static /* synthetic */ void $r8$lambda$u4L87AoxRLUeC7NACQjsjzsK2Mw(BaseFragment baseFragment) {
        if (baseFragment instanceof FiltersPreferencesActivity) {
            ((FiltersPreferencesActivity) baseFragment).onResume();
        }
    }

    public static String export() {
        TLObject dialogInAnyWay;
        String publicUsername;
        Gson gsonCreate = new GsonBuilder().setPrettyPrinting().serializeNulls().create();
        Backup backup = new Backup();
        backup.version = 2;
        backup.filters = AyuData.getRegexFilterDao().getAll();
        backup.exclusions = (List) Collection.EL.stream(AyuData.getRegexFilterDao().getAllExclusions()).map(new Function() { // from class: com.radolyn.ayugram.utils.filters.AyuFilterUtils$$ExternalSyntheticLambda6
            public /* synthetic */ Function andThen(Function function) {
                return Function$CC.$default$andThen(this, function);
            }

            @Override // java.util.function.Function
            public final Object apply(Object obj) {
                return AyuFilterUtils.$r8$lambda$Qzm4nwBT2nA_WA7VpamsNR_Jwqw((RegexFilterGlobalExclusion) obj);
            }

            public /* synthetic */ Function compose(Function function) {
                return Function$CC.$default$compose(this, function);
            }
        }).collect(Collectors.toList());
        backup.peers = new HashMap<>();
        for (RegexFilter regexFilter : backup.filters) {
            Long l = regexFilter.dialogId;
            if (l != null && (dialogInAnyWay = AyuMessageUtils.getDialogInAnyWay(l.longValue(), Integer.valueOf(UserConfig.selectedAccount), false)) != null && (publicUsername = DialogObject.getPublicUsername(dialogInAnyWay)) != null) {
                backup.peers.put(regexFilter.dialogId, "@" + publicUsername);
            }
        }
        return gsonCreate.toJson(backup);
    }

    public static /* synthetic */ Backup.BackupExclusion $r8$lambda$Qzm4nwBT2nA_WA7VpamsNR_Jwqw(RegexFilterGlobalExclusion regexFilterGlobalExclusion) {
        Backup.BackupExclusion backupExclusion = new Backup.BackupExclusion();
        backupExclusion.dialogId = regexFilterGlobalExclusion.dialogId;
        backupExclusion.filterId = regexFilterGlobalExclusion.filterId.toString();
        return backupExclusion;
    }

    public static ApplyChanges prepareChanges(String str) {
        try {
            Backup backup = (Backup) new GsonBuilder().setPrettyPrinting().serializeNulls().create().fromJson(str, Backup.class);
            if (backup != null && backup.version <= 2) {
                final List<RegexFilter> all = AyuData.getRegexFilterDao().getAll();
                final List<RegexFilterGlobalExclusion> allExclusions = AyuData.getRegexFilterDao().getAllExclusions();
                LinkedHashMap linkedHashMap = new LinkedHashMap();
                ArrayList<String> arrayList = new ArrayList<>();
                ArrayList<RegexFilter> arrayList2 = new ArrayList<>();
                ArrayList<RegexFilterGlobalExclusion> arrayList3 = new ArrayList<>();
                ArrayList<RegexFilterGlobalExclusion> arrayList4 = new ArrayList<>();
                HashMap<Long, String> map = new HashMap<>();
                List<RegexFilter> list = backup.filters;
                if (list != null) {
                    for (final RegexFilter regexFilter : list) {
                        RegexFilter regexFilter2 = (RegexFilter) Collection.EL.stream(all).filter(new Predicate() { // from class: com.radolyn.ayugram.utils.filters.AyuFilterUtils$$ExternalSyntheticLambda1
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
                                return ((RegexFilter) obj).id.equals(regexFilter.id);
                            }
                        }).findFirst().orElse(null);
                        if (regexFilter2 != null) {
                            if (!filterEquals(regexFilter2, regexFilter)) {
                                arrayList2.add(regexFilter);
                            }
                        } else {
                            linkedHashMap.put(regexFilter.id, regexFilter);
                        }
                    }
                }
                List<Backup.BackupExclusion> list2 = backup.exclusions;
                if (list2 != null) {
                    for (final Backup.BackupExclusion backupExclusion : list2) {
                        if (((RegexFilterGlobalExclusion) Collection.EL.stream(allExclusions).filter(new Predicate() { // from class: com.radolyn.ayugram.utils.filters.AyuFilterUtils$$ExternalSyntheticLambda2
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
                                return AyuFilterUtils.m2407$r8$lambda$eLCQ8gBYVTuK_2xHFhdQM79rG8(backupExclusion, (RegexFilterGlobalExclusion) obj);
                            }
                        }).findFirst().orElse(null)) == null) {
                            RegexFilterGlobalExclusion regexFilterGlobalExclusion = new RegexFilterGlobalExclusion();
                            regexFilterGlobalExclusion.filterId = UUID.fromString(backupExclusion.filterId);
                            regexFilterGlobalExclusion.dialogId = backupExclusion.dialogId;
                            arrayList3.add(regexFilterGlobalExclusion);
                        }
                    }
                }
                List<String> list3 = backup.removeFiltersById;
                if (list3 != null) {
                    arrayList.addAll((java.util.Collection) Collection.EL.stream(list3).filter(new Predicate() { // from class: com.radolyn.ayugram.utils.filters.AyuFilterUtils$$ExternalSyntheticLambda3
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
                            return Collection.EL.stream(all).anyMatch(new Predicate() { // from class: com.radolyn.ayugram.utils.filters.AyuFilterUtils$$ExternalSyntheticLambda7
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
                                public final boolean test(Object obj2) {
                                    return ((RegexFilter) obj2).id.equals(UUID.fromString(str));
                                }
                            });
                        }
                    }).collect(Collectors.toList()));
                }
                List<Backup.BackupExclusion> list4 = backup.removeExclusions;
                if (list4 != null) {
                    arrayList4.addAll((java.util.Collection) Collection.EL.stream(list4).filter(new Predicate() { // from class: com.radolyn.ayugram.utils.filters.AyuFilterUtils$$ExternalSyntheticLambda4
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
                            return Collection.EL.stream(allExclusions).anyMatch(new Predicate() { // from class: com.radolyn.ayugram.utils.filters.AyuFilterUtils$$ExternalSyntheticLambda0
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
                                public final boolean test(Object obj2) {
                                    return AyuFilterUtils.m2406$r8$lambda$8gSkeXefGTxyDhYwMUXTWi5Ouw(backupExclusion, (RegexFilterGlobalExclusion) obj2);
                                }
                            });
                        }
                    }).map(new Function() { // from class: com.radolyn.ayugram.utils.filters.AyuFilterUtils$$ExternalSyntheticLambda5
                        public /* synthetic */ Function andThen(Function function) {
                            return Function$CC.$default$andThen(this, function);
                        }

                        @Override // java.util.function.Function
                        public final Object apply(Object obj) {
                            return AyuFilterUtils.$r8$lambda$oMDHoEa8gQchUjjIWpyNSFlj5O8((AyuFilterUtils.Backup.BackupExclusion) obj);
                        }

                        public /* synthetic */ Function compose(Function function) {
                            return Function$CC.$default$compose(this, function);
                        }
                    }).collect(Collectors.toList()));
                }
                HashMap<Long, String> map2 = backup.peers;
                if (map2 != null) {
                    for (Map.Entry<Long, String> entry : map2.entrySet()) {
                        if (AyuMessageUtils.getDialogInAnyWay(entry.getKey().longValue(), Integer.valueOf(UserConfig.selectedAccount), false) == null) {
                            map.put(entry.getKey(), entry.getValue());
                        }
                    }
                }
                ApplyChanges applyChanges = new ApplyChanges();
                applyChanges.newFilters = new ArrayList<>(linkedHashMap.values());
                applyChanges.removeFiltersById = arrayList;
                applyChanges.filtersOverrides = arrayList2;
                applyChanges.newExclusions = arrayList3;
                applyChanges.removeExclusions = arrayList4;
                applyChanges.peersToBeResolved = map;
                return applyChanges;
            }
        } catch (JsonSyntaxException unused) {
        }
        return null;
    }

    /* JADX INFO: renamed from: $r8$lambda$eLCQ8gBYV-TuK_2xHFhdQM79rG8, reason: not valid java name */
    public static /* synthetic */ boolean m2407$r8$lambda$eLCQ8gBYVTuK_2xHFhdQM79rG8(Backup.BackupExclusion backupExclusion, RegexFilterGlobalExclusion regexFilterGlobalExclusion) {
        return regexFilterGlobalExclusion.filterId.toString().equals(backupExclusion.filterId) && regexFilterGlobalExclusion.dialogId == backupExclusion.dialogId;
    }

    /* JADX INFO: renamed from: $r8$lambda$8gSkeXef-GTxyDhYwMUXTWi5Ouw, reason: not valid java name */
    public static /* synthetic */ boolean m2406$r8$lambda$8gSkeXefGTxyDhYwMUXTWi5Ouw(Backup.BackupExclusion backupExclusion, RegexFilterGlobalExclusion regexFilterGlobalExclusion) {
        return regexFilterGlobalExclusion.filterId.equals(UUID.fromString(backupExclusion.filterId)) && regexFilterGlobalExclusion.dialogId == backupExclusion.dialogId;
    }

    public static /* synthetic */ RegexFilterGlobalExclusion $r8$lambda$oMDHoEa8gQchUjjIWpyNSFlj5O8(Backup.BackupExclusion backupExclusion) {
        RegexFilterGlobalExclusion regexFilterGlobalExclusion = new RegexFilterGlobalExclusion();
        regexFilterGlobalExclusion.filterId = UUID.fromString(backupExclusion.filterId);
        regexFilterGlobalExclusion.dialogId = backupExclusion.dialogId;
        return regexFilterGlobalExclusion;
    }

    public static void applyChanges(ApplyChanges applyChanges) {
        if (applyChanges == null) {
            return;
        }
        int i = 0;
        if (!applyChanges.newFilters.isEmpty()) {
            ArrayList<RegexFilter> arrayList = applyChanges.newFilters;
            int size = arrayList.size();
            int i2 = 0;
            while (i2 < size) {
                RegexFilter regexFilter = arrayList.get(i2);
                i2++;
                AyuData.getRegexFilterDao().insert(regexFilter);
            }
        }
        if (!applyChanges.removeFiltersById.isEmpty()) {
            ArrayList<String> arrayList2 = applyChanges.removeFiltersById;
            int size2 = arrayList2.size();
            int i3 = 0;
            while (i3 < size2) {
                String str = arrayList2.get(i3);
                i3++;
                AyuData.getRegexFilterDao().delete(UUID.fromString(str));
            }
        }
        if (!applyChanges.filtersOverrides.isEmpty()) {
            ArrayList<RegexFilter> arrayList3 = applyChanges.filtersOverrides;
            int size3 = arrayList3.size();
            int i4 = 0;
            while (i4 < size3) {
                RegexFilter regexFilter2 = arrayList3.get(i4);
                i4++;
                AyuData.getRegexFilterDao().update(regexFilter2);
            }
        }
        if (!applyChanges.newExclusions.isEmpty()) {
            ArrayList<RegexFilterGlobalExclusion> arrayList4 = applyChanges.newExclusions;
            int size4 = arrayList4.size();
            int i5 = 0;
            while (i5 < size4) {
                RegexFilterGlobalExclusion regexFilterGlobalExclusion = arrayList4.get(i5);
                i5++;
                AyuData.getRegexFilterDao().insertExclusion(regexFilterGlobalExclusion);
            }
        }
        if (!applyChanges.removeExclusions.isEmpty()) {
            ArrayList<RegexFilterGlobalExclusion> arrayList5 = applyChanges.removeExclusions;
            int size5 = arrayList5.size();
            while (i < size5) {
                RegexFilterGlobalExclusion regexFilterGlobalExclusion2 = arrayList5.get(i);
                i++;
                RegexFilterGlobalExclusion regexFilterGlobalExclusion3 = regexFilterGlobalExclusion2;
                AyuData.getRegexFilterDao().deleteExclusion(regexFilterGlobalExclusion3.dialogId, regexFilterGlobalExclusion3.filterId);
            }
        }
        if (!applyChanges.peersToBeResolved.isEmpty()) {
            AyuRequestUtils.resolveAllChats(applyChanges.peersToBeResolved);
        }
        AyuFilterCacheController.rebuildCache();
    }

    private static boolean filterEquals(RegexFilter regexFilter, RegexFilter regexFilter2) {
        return regexFilter.id.equals(regexFilter2.id) && regexFilter.text.equals(regexFilter2.text) && regexFilter.caseInsensitive == regexFilter2.caseInsensitive && regexFilter.reversed == regexFilter2.reversed && Objects.equals(regexFilter.dialogId, regexFilter2.dialogId) && regexFilter.enabled == regexFilter2.enabled;
    }
}
