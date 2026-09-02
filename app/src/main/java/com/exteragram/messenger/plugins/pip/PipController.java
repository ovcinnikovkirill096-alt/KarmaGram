package com.exteragram.messenger.plugins.pip;

import com.exteragram.messenger.plugins.PluginsController;
import com.exteragram.messenger.utils.network.ExteraHttpClient;
import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.reflect.TypeToken;
import j$.util.concurrent.ConcurrentHashMap;
import j$.util.concurrent.ConcurrentMap$EL;
import j$.util.function.Function$CC;
import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FilenameFilter;
import java.io.IOException;
import java.io.InputStream;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.Iterator;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Set;
import java.util.function.Function;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;
import kotlin.Pair;
import kotlin.TuplesKt;
import kotlin.Unit;
import kotlin.collections.ArraysKt;
import kotlin.collections.CollectionsKt;
import kotlin.collections.SetsKt;
import kotlin.io.ByteStreamsKt;
import kotlin.io.CloseableKt;
import kotlin.io.FilesKt;
import kotlin.jvm.functions.Function1;
import kotlin.jvm.internal.Intrinsics;
import kotlin.sequences.Sequence;
import kotlin.sequences.SequencesKt;
import kotlin.text.MatchResult;
import kotlin.text.Regex;
import kotlin.text.StringsKt;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import org.lsposed.lsparanoid.Deobfuscator$AyuGram4A$TMessagesProj;
import org.mvel2.asm.signature.SignatureVisitor;
import org.telegram.messenger.FileLog;

public final class PipController {
    public static final PipController INSTANCE;
    private static final Set<String> PREINSTALLED_PACKAGES;
    private static final Regex REGEX_MARKER_EXTRA;
    private static final Regex REGEX_MARKER_OS_NAME;
    private static final Regex REGEX_MARKER_PLATFORM_SYSTEM;
    private static final Regex REGEX_MARKER_PYTHON_VERSION;
    private static final Regex REGEX_MARKER_SYS_PLATFORM;
    private static final Regex REGEX_NORMALIZE;
    private static final Regex REGEX_REQ_EXTRA;
    private static final Regex REGEX_REQ_PAREN;
    private static final Regex REGEX_REQ_PARSE;
    private static final Regex REGEX_REQ_SPECS;
    private static final Regex REGEX_VERSION_SPLIT;
    private static final OkHttpClient client;
    private static final Gson gson;
    private static final ConcurrentHashMap<String, Object> installLocks;
    private static String pythonVersion;
    private static final ConcurrentHashMap<String, ConcurrentHashMap<String, Set<String>>> registry;
    private static final String ENV_SYS_PLATFORM = "linux";
    private static final String ENV_PLATFORM_SYSTEM = "Linux";
    private static final String ENV_OS_NAME = "posix";

    public interface InstallerDelegate {
        boolean isCancelled();

        void onProgress(String str);
    }

    private PipController() {
    }

    static {
        PipController pipController = new PipController();
        INSTANCE = pipController;
        client = ExteraHttpClient.INSTANCE.getClient();
        gson = new Gson();
        registry = new ConcurrentHashMap<>();
        installLocks = new ConcurrentHashMap<>();
        pythonVersion = "3.11.10";
        REGEX_NORMALIZE = new Regex("[-_.]+");
        REGEX_REQ_PARSE = new Regex("^([a-zA-Z0-9_.-]+)(.*)");
        REGEX_REQ_SPECS = new Regex("^(===|==|>=|<=|>|<|~=|!=)\\s*(.*)");
        REGEX_REQ_EXTRA = new Regex("\\[.*?]");
        REGEX_REQ_PAREN = new Regex("[()]");
        REGEX_VERSION_SPLIT = new Regex("([0-9]+)|([a-zA-Z]+)");
        REGEX_MARKER_SYS_PLATFORM = new Regex("sys_platform\\s*(==|!=)\\s*['\"]([^'\"]+)['\"]");
        REGEX_MARKER_PLATFORM_SYSTEM = new Regex("platform_system\\s*(==|!=)\\s*['\"]([^'\"]+)['\"]");
        REGEX_MARKER_OS_NAME = new Regex("os_name\\s*(==|!=)\\s*['\"]([^'\"]+)['\"]");
        REGEX_MARKER_PYTHON_VERSION = new Regex("python_version\\s*(===|==|>=|<=|>|<|!=|~=)\\s*['\"]([^'\"]+)['\"]");
        REGEX_MARKER_EXTRA = new Regex("extra\\s*==\\s*");
        pipController.loadRegistry();
        Set of = SetsKt.setOf((Object[]) new String[]{"beautifulsoup4", "debugpy", "lxml", "packaging", "pillow", "requests", "pyyaml"});
        ArrayList arrayList = new ArrayList(CollectionsKt.collectionSizeOrDefault(of, 10));
        Iterator it = of.iterator();
        while (it.hasNext()) {
            arrayList.add(INSTANCE.normalizePackageName((String) it.next()));
        }
        PREINSTALLED_PACKAGES = CollectionsKt.toSet(arrayList);
    }

    private final File getLibsDir() {
        File file = new File(PluginsController.getInstance().pluginsDir, "libs");
        file.mkdirs();
        return file;
    }

    private final File getRegistryFile() {
        return new File(getLibsDir(), "registry.json");
    }

    public final String getPythonVersion() {
        return pythonVersion;
    }

    public final void setPythonVersion(String str) {
        Intrinsics.checkNotNullParameter(str, "<set-?>");
        pythonVersion = str;
    }

    private final String normalizePackageName(String str) {
        String lowerCase = str.toLowerCase(Locale.ROOT);
        Intrinsics.checkNotNullExpressionValue(lowerCase, "toLowerCase(...)");
        return REGEX_NORMALIZE.replace(lowerCase, "-");
    }

    private final synchronized void loadRegistry() {
        try {
            if (getRegistryFile().exists()) {
                try {
                    Object objFromJson = gson.fromJson(FilesKt.readText$default(getRegistryFile(), null, 1, null), new TypeToken<Map<String, ? extends Map<String, ? extends Set<? extends String>>>>() { // from class: com.exteragram.messenger.plugins.pip.PipController$loadRegistry$type$1
                    }.getType());
                    Intrinsics.checkNotNullExpressionValue(objFromJson, "fromJson(...)");
                    registry.clear();
                    for (Map.Entry entry : ((Map) objFromJson).entrySet()) {
                        String str = (String) entry.getKey();
                        Map map = (Map) entry.getValue();
                        String strNormalizePackageName = INSTANCE.normalizePackageName(str);
                        ConcurrentHashMap<String, Set<String>> concurrentHashMap = new ConcurrentHashMap<>();
                        for (Map.Entry entry2 : map.entrySet()) {
                            String str2 = (String) entry2.getKey();
                            Set set = (Set) entry2.getValue();
                            ConcurrentHashMap concurrentHashMap2 = new ConcurrentHashMap();
                            Iterator it = set.iterator();
                            while (it.hasNext()) {
                                concurrentHashMap2.put((String) it.next(), Boolean.TRUE);
                            }
                            concurrentHashMap.put(str2, Collections.newSetFromMap(concurrentHashMap2));
                        }
                        registry.put(strNormalizePackageName, concurrentHashMap);
                    }
                } catch (Exception e) {
                    FileLog.e("PipController: Failed to load registry", e);
                    getRegistryFile().renameTo(new File(getLibsDir(), "registry.json.bak"));
                }
            }
        } catch (Throwable th) {
            throw th;
        }
    }

    private final synchronized void saveRegistry() {
        try {
            try {
                File file = new File(getLibsDir(), "registry.temp");
                String json = gson.toJson(registry);
                Intrinsics.checkNotNullExpressionValue(json, "toJson(...)");
                FilesKt.writeText$default(file, json, null, 2, null);
                if (!file.renameTo(getRegistryFile()) && (!getRegistryFile().delete() || !file.renameTo(getRegistryFile()))) {
                    FilesKt.copyTo$default(file, getRegistryFile(), true, 0, 4, null);
                    file.delete();
                }
            } catch (Exception e) {
                FileLog.e("PipController: Failed to save registry", e);
            }
        } catch (Throwable th) {
            throw th;
        }
    }

    public final void cleanup() {
        cleanupInternal();
    }

    public static /* synthetic */ List installDependencies$default(PipController pipController, List list, String str, InstallerDelegate installerDelegate, int i, Object obj) {
        if ((i & 4) != 0) {
            installerDelegate = null;
        }
        return pipController.installDependencies(list, str, installerDelegate);
    }

    public final List<String> installDependencies(List<String> list, String str, InstallerDelegate installerDelegate) {
        String str2;
        Exception exc;
        Intrinsics.checkNotNullParameter(list, "requirements");
        Intrinsics.checkNotNullParameter(str, "pluginId");
        ArrayList arrayList = new ArrayList();
        LinkedHashSet linkedHashSet = new LinkedHashSet();
        try {
            for (String str3 : list) {
                if (installerDelegate != null) {
                    try {
                        if (installerDelegate.isCancelled()) {
                            throw new IOException("Installation cancelled");
                        }
                    } catch (Exception e) {
                        exc = e;
                        str2 = str;
                        FileLog.e("PipController: Failed to install dependencies for " + str2 + ": " + list, exc);
                        loadRegistry();
                        removeOrphanedDirectories();
                        throw exc;
                    }
                }
                if (!StringsKt.isBlank(str3)) {
                    Pair requirement = parseRequirement(str3);
                    String str4 = (String) requirement.component1();
                    List<Pair> list2 = (List) requirement.component2();
                    String strNormalizePackageName = normalizePackageName(str4);
                    if (!PREINSTALLED_PACKAGES.contains(strNormalizePackageName)) {
                        str2 = str;
                        InstallerDelegate installerDelegate2 = installerDelegate;
                        try {
                            resolveAndInstall(strNormalizePackageName, list2, linkedHashSet, str2, installerDelegate2);
                            str = str2;
                            installerDelegate = installerDelegate2;
                        } catch (Exception e2) {
                            e = e2;
                            exc = e;
                            FileLog.e("PipController: Failed to install dependencies for " + str2 + ": " + list, exc);
                            loadRegistry();
                            removeOrphanedDirectories();
                            throw exc;
                        }
                    }
                }
            }
            str2 = str;
            updateRegistryForPlugin(str2, linkedHashSet);
            for (Pair pair : linkedHashSet) {
                String absolutePath = getLibPath((String) pair.component1(), (String) pair.component2()).getAbsolutePath();
                Intrinsics.checkNotNullExpressionValue(absolutePath, "getAbsolutePath(...)");
                arrayList.add(absolutePath);
            }
            saveRegistry();
            return arrayList;
        } catch (Exception e3) {
            e = e3;
            str2 = str;
        }
    }

    private final void resolveAndInstall(String str, List<Pair> list, Set<Pair> set, String str2, InstallerDelegate installerDelegate) throws IOException {
        Object obj;
        Map.Entry entry;
        String strInstallPackage;
        Set<Map.Entry<String, Set<String>>> setEntrySet;
        Object next;
        Object value;
        PipController pipController = this;
        Set<Pair> set2 = set;
        String str3 = str2;
        InstallerDelegate installerDelegate2 = installerDelegate;
        if (installerDelegate2 != null && installerDelegate2.isCancelled()) {
            throw new IOException("Installation cancelled");
        }
        ConcurrentHashMap<String, Object> concurrentHashMap = installLocks;
        final Function1 function1 = new Function1() { // from class: com.exteragram.messenger.plugins.pip.PipController$$ExternalSyntheticLambda1
            @Override // kotlin.jvm.functions.Function1
            public final Object invoke(Object obj2) {
                return PipController.resolveAndInstall$lambda$0((String) obj2);
            }
        };
        Object objComputeIfAbsent = ConcurrentMap$EL.computeIfAbsent(concurrentHashMap, str, new Function() { // from class: com.exteragram.messenger.plugins.pip.PipController$$ExternalSyntheticLambda2
            public /* synthetic */ Function andThen(Function function) {
                return Function$CC.$default$andThen(this, function);
            }

            @Override // java.util.function.Function
            public final Object apply(Object obj2) {
                return function1.invoke(obj2);
            }

            public /* synthetic */ Function compose(Function function) {
                return Function$CC.$default$compose(this, function);
            }
        });
        Intrinsics.checkNotNullExpressionValue(objComputeIfAbsent, "computeIfAbsent(...)");
        synchronized (objComputeIfAbsent) {
            if (installerDelegate2 != null) {
                try {
                    if (installerDelegate2.isCancelled()) {
                        throw new IOException("Installation cancelled");
                    }
                } catch (Throwable th) {
                    throw th;
                }
            }
            ConcurrentHashMap<String, Set<String>> concurrentHashMap2 = registry.get(str);
            obj = null;
            if (concurrentHashMap2 == null || (setEntrySet = concurrentHashMap2.entrySet()) == null) {
                entry = null;
            } else {
                Iterator<T> it = setEntrySet.iterator();
                do {
                    if (!it.hasNext()) {
                        next = null;
                        break;
                    } else {
                        next = it.next();
                        value = ((Map.Entry) next).getValue();
                        Intrinsics.checkNotNullExpressionValue(value, "<get-value>(...)");
                    }
                } while (((Collection) value).isEmpty());
                entry = (Map.Entry) next;
            }
            if (entry != null) {
                Object key = entry.getKey();
                Intrinsics.checkNotNullExpressionValue(key, "<get-key>(...)");
                strInstallPackage = (String) key;
                Object value2 = entry.getValue();
                Intrinsics.checkNotNullExpressionValue(value2, "<get-value>(...)");
                String strJoinToString$default = CollectionsKt.joinToString$default((Iterable) value2, ", ", null, null, 0, null, null, 62, null);
                if (!INSTANCE.checkVersionSatisfies(strInstallPackage, list)) {
                    throw new IOException("Dependency conflict for '" + str + "': Active version " + strInstallPackage + " (used by " + strJoinToString$default + ") does not satisfy requirement '" + (!list.isEmpty() ? CollectionsKt.joinToString$default(list, ",", null, null, 0, null, new Function1() { // from class: com.exteragram.messenger.plugins.pip.PipController$$ExternalSyntheticLambda3
                        @Override // kotlin.jvm.functions.Function1
                        public final Object invoke(Object obj2) {
                            return PipController.resolveAndInstall$lambda$2$1((Pair) obj2);
                        }
                    }, 30, null) : "(latest)") + "'.");
                }
                FileLog.d("PipController: Reusing active version " + strInstallPackage + " of " + str + " (used by " + strJoinToString$default + ") for " + str3);
            } else {
                PipController pipController2 = INSTANCE;
                String strFindInstalledVersion = pipController2.findInstalledVersion(str, list);
                if (strFindInstalledVersion != null) {
                    strInstallPackage = strFindInstalledVersion;
                } else {
                    strFindInstalledVersion = pipController2.findVersionOnDisk(str, list);
                    if (strFindInstalledVersion != null) {
                        FileLog.d("PipController: Found " + str + ' ' + strFindInstalledVersion + " on disk, adopting.");
                        strInstallPackage = strFindInstalledVersion;
                    } else {
                        strInstallPackage = pipController2.installPackage(str, list, installerDelegate2);
                    }
                }
            }
        }
        for (Object obj2 : set2) {
            if (Intrinsics.areEqual(((Pair) obj2).getFirst(), str)) {
                obj = obj2;
                break;
            }
        }
        Pair pair = (Pair) obj;
        if (pair != null) {
            String str4 = (String) pair.getSecond();
            if (VersionComparator.INSTANCE.compare(strInstallPackage, str4) <= 0) {
                return;
            }
            FileLog.d("PipController: Upgrading " + str + " from " + str4 + " to " + strInstallPackage + " for " + str3);
            set2.remove(pair);
        }
        ConcurrentHashMap<String, ConcurrentHashMap<String, Set<String>>> concurrentHashMap3 = registry;
        final Function1 function2 = new Function1() { // from class: com.exteragram.messenger.plugins.pip.PipController$$ExternalSyntheticLambda4
            @Override // kotlin.jvm.functions.Function1
            public final Object invoke(Object obj3) {
                return PipController.resolveAndInstall$lambda$4((String) obj3);
            }
        };
        ConcurrentHashMap concurrentHashMap4 = (ConcurrentHashMap) ConcurrentMap$EL.computeIfAbsent(concurrentHashMap3, str, new Function() { // from class: com.exteragram.messenger.plugins.pip.PipController$$ExternalSyntheticLambda5
            public /* synthetic */ Function andThen(Function function) {
                return Function$CC.$default$andThen(this, function);
            }

            @Override // java.util.function.Function
            public final Object apply(Object obj3) {
                return PipController.resolveAndInstall$lambda$5(function2, obj3);
            }

            public /* synthetic */ Function compose(Function function) {
                return Function$CC.$default$compose(this, function);
            }
        });
        final Function1 function3 = new Function1() { // from class: com.exteragram.messenger.plugins.pip.PipController$$ExternalSyntheticLambda6
            @Override // kotlin.jvm.functions.Function1
            public final Object invoke(Object obj3) {
                return PipController.resolveAndInstall$lambda$6((String) obj3);
            }
        };
        ((Set) ConcurrentMap$EL.computeIfAbsent(concurrentHashMap4, strInstallPackage, new Function() { // from class: com.exteragram.messenger.plugins.pip.PipController$$ExternalSyntheticLambda7
            public /* synthetic */ Function andThen(Function function) {
                return Function$CC.$default$andThen(this, function);
            }

            @Override // java.util.function.Function
            public final Object apply(Object obj3) {
                return PipController.resolveAndInstall$lambda$7(function3, obj3);
            }

            public /* synthetic */ Function compose(Function function) {
                return Function$CC.$default$compose(this, function);
            }
        })).add(str3);
        set2.add(TuplesKt.to(str, strInstallPackage));
        File fileFindMetadataFile = pipController.findMetadataFile(str, strInstallPackage);
        if (fileFindMetadataFile == null || !fileFindMetadataFile.exists()) {
            return;
        }
        for (String str5 : pipController.parseDependenciesFromMetadata(fileFindMetadataFile)) {
            if (installerDelegate2 != null && installerDelegate2.isCancelled()) {
                throw new IOException("Installation cancelled");
            }
            Pair requirement = pipController.parseRequirement(str5);
            pipController.resolveAndInstall(pipController.normalizePackageName((String) requirement.component1()), (List) requirement.component2(), set2, str3, installerDelegate2);
            pipController = this;
            set2 = set;
            str3 = str2;
            installerDelegate2 = installerDelegate;
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static final Object resolveAndInstall$lambda$0(String str) {
        Intrinsics.checkNotNullParameter(str, "it");
        return new Object();
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static final CharSequence resolveAndInstall$lambda$2$1(Pair pair) {
        Intrinsics.checkNotNullParameter(pair, "it");
        return ((String) pair.getFirst()) + ((String) pair.getSecond());
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static final ConcurrentHashMap resolveAndInstall$lambda$4(String str) {
        Intrinsics.checkNotNullParameter(str, "it");
        return new ConcurrentHashMap();
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static final ConcurrentHashMap resolveAndInstall$lambda$5(Function1 function1, Object obj) {
        return (ConcurrentHashMap) function1.invoke(obj);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static final Set resolveAndInstall$lambda$6(String str) {
        Intrinsics.checkNotNullParameter(str, "it");
        return Collections.newSetFromMap(new ConcurrentHashMap());
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static final Set resolveAndInstall$lambda$7(Function1 function1, Object obj) {
        return (Set) function1.invoke(obj);
    }

    private final void updateRegistryForPlugin(String str, Set<Pair> set) {
        for (Map.Entry<String, ConcurrentHashMap<String, Set<String>>> entry : registry.entrySet()) {
            String key = entry.getKey();
            for (Map.Entry<String, Set<String>> entry2 : entry.getValue().entrySet()) {
                String key2 = entry2.getKey();
                Set<String> value = entry2.getValue();
                Pair pair = TuplesKt.to(key, key2);
                if (value.contains(str) && !set.contains(pair)) {
                    value.remove(str);
                }
            }
        }
    }

    private final void cleanupInternal() throws IOException {
        ArrayList arrayList = new ArrayList();
        for (Map.Entry<String, ConcurrentHashMap<String, Set<String>>> entry : registry.entrySet()) {
            String key = entry.getKey();
            for (Map.Entry<String, Set<String>> entry2 : entry.getValue().entrySet()) {
                String key2 = entry2.getKey();
                if (entry2.getValue().isEmpty()) {
                    arrayList.add(TuplesKt.to(key, key2));
                }
            }
        }
        int size = arrayList.size();
        int i = 0;
        while (i < size) {
            Object obj = arrayList.get(i);
            i++;
            Pair pair = (Pair) obj;
            String str = (String) pair.component1();
            String str2 = (String) pair.component2();
            ConcurrentHashMap<String, ConcurrentHashMap<String, Set<String>>> concurrentHashMap = registry;
            ConcurrentHashMap<String, Set<String>> concurrentHashMap2 = concurrentHashMap.get(str);
            if (concurrentHashMap2 != null) {
                concurrentHashMap2.remove(str2);
            }
            ConcurrentHashMap<String, Set<String>> concurrentHashMap3 = concurrentHashMap.get(str);
            if (concurrentHashMap3 != null && concurrentHashMap3.isEmpty()) {
                concurrentHashMap.remove(str);
            }
            INSTANCE.deletePackage(str, str2);
        }
        removeOrphanedDirectories();
    }

    private final void removeOrphanedDirectories() {
        LinkedHashSet linkedHashSet = new LinkedHashSet();
        for (Map.Entry<String, ConcurrentHashMap<String, Set<String>>> entry : registry.entrySet()) {
            String key = entry.getKey();
            Set<String> setKeySet = entry.getValue().keySet();
            Intrinsics.checkNotNullExpressionValue(setKeySet, "<get-keys>(...)");
            for (String str : setKeySet) {
                try {
                    PipController pipController = INSTANCE;
                    Intrinsics.checkNotNull(str);
                    String canonicalPath = pipController.getLibPath(key, str).getCanonicalPath();
                    Intrinsics.checkNotNullExpressionValue(canonicalPath, "getCanonicalPath(...)");
                    linkedHashSet.add(canonicalPath);
                } catch (IOException unused) {
                }
            }
        }
        File[] fileArrListFiles = getLibsDir().listFiles();
        if (fileArrListFiles != null) {
            for (File file : fileArrListFiles) {
                if (file.isDirectory()) {
                    try {
                        if (!linkedHashSet.contains(file.getCanonicalPath())) {
                            FileLog.d("PipController: Removing orphaned library: " + file.getName());
                            Intrinsics.checkNotNull(file);
                            FilesKt.deleteRecursively(file);
                        }
                    } catch (Exception e) {
                        FileLog.e(e);
                    }
                }
            }
        }
    }

    public final void uninstallDependencies(String str) {
        Intrinsics.checkNotNullParameter(str, "pluginId");
        ArrayList arrayList = new ArrayList();
        int i = 0;
        boolean z = false;
        for (Map.Entry<String, ConcurrentHashMap<String, Set<String>>> entry : registry.entrySet()) {
            String key = entry.getKey();
            for (Map.Entry<String, Set<String>> entry2 : entry.getValue().entrySet()) {
                String key2 = entry2.getKey();
                Set<String> value = entry2.getValue();
                if (value.remove(str)) {
                    if (value.isEmpty()) {
                        arrayList.add(TuplesKt.to(key, key2));
                    }
                    z = true;
                }
            }
        }
        int size = arrayList.size();
        while (i < size) {
            Object obj = arrayList.get(i);
            i++;
            Pair pair = (Pair) obj;
            String str2 = (String) pair.component1();
            String str3 = (String) pair.component2();
            ConcurrentHashMap<String, ConcurrentHashMap<String, Set<String>>> concurrentHashMap = registry;
            ConcurrentHashMap<String, Set<String>> concurrentHashMap2 = concurrentHashMap.get(str2);
            if (concurrentHashMap2 != null) {
                concurrentHashMap2.remove(str3);
            }
            ConcurrentHashMap<String, Set<String>> concurrentHashMap3 = concurrentHashMap.get(str2);
            if (concurrentHashMap3 != null && concurrentHashMap3.isEmpty()) {
                concurrentHashMap.remove(str2);
            }
            INSTANCE.deletePackage(str2, str3);
        }
        if (z) {
            saveRegistry();
        }
    }

    private final String installPackage(String str, List<Pair> list, InstallerDelegate installerDelegate) throws IOException {
        String asString;
        String asString2;
        String strJoinToString$default = !list.isEmpty() ? CollectionsKt.joinToString$default(list, ",", null, null, 0, null, new Function1() { // from class: com.exteragram.messenger.plugins.pip.PipController$$ExternalSyntheticLambda0
            @Override // kotlin.jvm.functions.Function1
            public final Object invoke(Object obj) {
                return PipController.installPackage$lambda$0((Pair) obj);
            }
        }, 30, null) : "(latest)";
        FileLog.d("PipController: Installing " + str + ' ' + strJoinToString$default);
        if (installerDelegate != null) {
            installerDelegate.onProgress("Resolving " + str + "...");
        }
        Response responseExecuteWithRetry = executeWithRetry(new Request.Builder().url("https://pypi.org/pypi/" + str + "/json").build(), installerDelegate);
        try {
            if (!responseExecuteWithRetry.isSuccessful()) {
                if (responseExecuteWithRetry.code() == 404) {
                    throw new IOException("Package " + str + " not found on PyPI");
                }
                throw new IOException("Failed to fetch metadata for " + str + ": " + responseExecuteWithRetry.code());
            }
            JsonObject jsonObject = (JsonObject) gson.fromJson(responseExecuteWithRetry.body().string(), JsonObject.class);
            JsonObject asJsonObject = jsonObject.getAsJsonObject("info");
            if (asJsonObject.has("requires_python") && !asJsonObject.get("requires_python").isJsonNull()) {
                String asString3 = asJsonObject.get("requires_python").getAsString();
                PipController pipController = INSTANCE;
                Intrinsics.checkNotNull(asString3);
                if (!pipController.checkVersionSatisfies(pythonVersion, pipController.parseSpecs(asString3))) {
                    throw new IOException("Package " + str + " requires Python " + asString3 + ", but current is " + pythonVersion);
                }
            }
            JsonObject asJsonObject2 = jsonObject.getAsJsonObject("releases");
            Set setKeySet = asJsonObject2.keySet();
            Intrinsics.checkNotNullExpressionValue(setKeySet, "keySet(...)");
            List list2 = CollectionsKt.toList(setKeySet);
            ArrayList arrayList = new ArrayList();
            for (Object obj : list2) {
                String str2 = (String) obj;
                PipController pipController2 = INSTANCE;
                Intrinsics.checkNotNull(str2);
                if (pipController2.checkVersionSatisfies(str2, list)) {
                    arrayList.add(obj);
                }
            }
            String str3 = (String) CollectionsKt.maxWithOrNull(arrayList, VersionComparator.INSTANCE);
            if (str3 == null) {
                throw new IOException("No matching version found for " + str + ' ' + strJoinToString$default);
            }
            Iterator it = asJsonObject2.getAsJsonArray(str3).iterator();
            Intrinsics.checkNotNullExpressionValue(it, "iterator(...)");
            while (true) {
                if (!it.hasNext()) {
                    asString = null;
                    asString2 = null;
                    break;
                }
                JsonObject asJsonObject3 = ((JsonElement) it.next()).getAsJsonObject();
                String asString4 = asJsonObject3.get("filename").getAsString();
                if (Intrinsics.areEqual(asJsonObject3.get("packagetype").getAsString(), "bdist_wheel")) {
                    Intrinsics.checkNotNull(asString4);
                    if (StringsKt.contains$default((CharSequence) asString4, (CharSequence) "-none-any", false, 2, (Object) null)) {
                        asString = asJsonObject3.get("url").getAsString();
                        JsonObject asJsonObject4 = asJsonObject3.getAsJsonObject("digests");
                        if (asJsonObject4 != null && asJsonObject4.has("sha256")) {
                            asString2 = asJsonObject4.get("sha256").getAsString();
                            break;
                        }
                        asString2 = null;
                        break;
                    }
                }
            }
            Unit unit = Unit.INSTANCE;
            CloseableKt.closeFinally(responseExecuteWithRetry, null);
            if (asString == null) {
                throw new IOException("No pure-Python wheel found for " + str + ". Binary packages are not supported.");
            }
            Intrinsics.checkNotNull(str3);
            File libPath = getLibPath(str, str3);
            File file = new File(getLibsDir(), "tmp_" + str + '_' + System.currentTimeMillis());
            file.mkdirs();
            try {
                if (installerDelegate != null) {
                    try {
                        installerDelegate.onProgress("Downloading " + str + ' ' + str3 + "...");
                    } catch (Exception e) {
                        FileLog.e("PipController: Failed to download/install " + str, e);
                        throw e;
                    }
                }
                File file2 = new File(file, "package.whl");
                Response responseExecuteWithRetry2 = executeWithRetry(new Request.Builder().url(asString).build(), installerDelegate);
                try {
                    if (!responseExecuteWithRetry2.isSuccessful()) {
                        throw new IOException("Download failed: " + responseExecuteWithRetry2.code());
                    }
                    InputStream inputStreamByteStream = responseExecuteWithRetry2.body().byteStream();
                    FileOutputStream fileOutputStream = new FileOutputStream(file2);
                    try {
                        ByteStreamsKt.copyTo$default(inputStreamByteStream, fileOutputStream, 0, 2, null);
                        CloseableKt.closeFinally(fileOutputStream, null);
                        CloseableKt.closeFinally(responseExecuteWithRetry2, null);
                        if (asString2 != null && !StringsKt.equals(calculateSha256(file2), asString2, true)) {
                            throw new IOException("Checksum mismatch");
                        }
                        FileInputStream fileInputStream = new FileInputStream(file2);
                        try {
                            unzip(fileInputStream, file);
                            CloseableKt.closeFinally(fileInputStream, null);
                            file2.delete();
                            if (libPath.exists()) {
                                FilesKt.deleteRecursively(libPath);
                            }
                            if (!file.renameTo(libPath)) {
                                FilesKt.copyRecursively$default(file, libPath, true, null, 4, null);
                            }
                            FilesKt.deleteRecursively(file);
                            return str3;
                        } catch (Throwable th) {
                            try {
                                throw th;
                            } catch (Throwable th2) {
                                CloseableKt.closeFinally(fileInputStream, th);
                                throw th2;
                            }
                        }
                    } catch (Throwable th3) {
                        try {
                            throw th3;
                        } catch (Throwable th4) {
                            CloseableKt.closeFinally(fileOutputStream, th3);
                            throw th4;
                        }
                    }
                } catch (Throwable th5) {
                    try {
                        throw th5;
                    } catch (Throwable th6) {
                        CloseableKt.closeFinally(responseExecuteWithRetry2, th5);
                        throw th6;
                    }
                }
            } catch (Throwable th7) {
                FilesKt.deleteRecursively(file);
                throw th7;
            }
        } catch (Throwable th8) {
            try {
                throw th8;
            } catch (Throwable th9) {
                CloseableKt.closeFinally(responseExecuteWithRetry, th8);
                throw th9;
            }
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static final CharSequence installPackage$lambda$0(Pair pair) {
        Intrinsics.checkNotNullParameter(pair, "it");
        return ((String) pair.getFirst()) + ((String) pair.getSecond());
    }

    private final Response executeWithRetry(Request request, InstallerDelegate installerDelegate) throws IOException {
        int i = 0;
        IOException e = null;
        while (i < 3) {
            if (installerDelegate != null && installerDelegate.isCancelled()) {
                throw new IOException("Installation cancelled");
            }
            try {
                return client.newCall(request).execute();
            } catch (IOException e2) {
                e = e2;
                i++;
                FileLog.w("PipController: Network error, retrying (" + i + "/3): " + e.getMessage());
                try {
                    Thread.sleep(((long) i) * 1000);
                } catch (InterruptedException unused) {
                }
            }
        }
        if (installerDelegate != null && installerDelegate.isCancelled()) {
            throw new IOException("Installation cancelled");
        }
        if (e != null) {
            throw e;
        }
        throw new IOException("Unknown network error");
    }

    public static final void unzip(InputStream inputStream, File file) throws IOException {
        Intrinsics.checkNotNullParameter(inputStream, "inputStream");
        Intrinsics.checkNotNullParameter(file, "targetDir");
        String canonicalPath = file.getCanonicalPath();
        ZipInputStream zipInputStream = new ZipInputStream(new BufferedInputStream(inputStream));
        while (true) {
            try {
                ZipEntry nextEntry = zipInputStream.getNextEntry();
                if (nextEntry != null) {
                    File file2 = new File(file, nextEntry.getName());
                    String canonicalPath2 = file2.getCanonicalPath();
                    Intrinsics.checkNotNullExpressionValue(canonicalPath2, "getCanonicalPath(...)");
                    if (!StringsKt.startsWith$default(canonicalPath2, canonicalPath + File.separator, false, 2, (Object) null)) {
                        throw new SecurityException("Zip Slip vulnerability detected: " + nextEntry.getName());
                    }
                    if (nextEntry.isDirectory()) {
                        file2.mkdirs();
                    } else {
                        File parentFile = file2.getParentFile();
                        if (parentFile != null) {
                            parentFile.mkdirs();
                        }
                        FileOutputStream fileOutputStream = new FileOutputStream(file2);
                        try {
                            ByteStreamsKt.copyTo$default(zipInputStream, fileOutputStream, 0, 2, null);
                            CloseableKt.closeFinally(fileOutputStream, null);
                        } catch (Throwable th) {
                            try {
                                throw th;
                            } catch (Throwable th2) {
                                CloseableKt.closeFinally(fileOutputStream, th);
                                throw th2;
                            }
                        }
                    }
                    zipInputStream.closeEntry();
                } else {
                    Unit unit = Unit.INSTANCE;
                    CloseableKt.closeFinally(zipInputStream, null);
                    return;
                }
            } catch (Throwable th3) {
                try {
                    throw th3;
                } catch (Throwable th4) {
                    CloseableKt.closeFinally(zipInputStream, th3);
                    throw th4;
                }
            }
        }
    }

    private final String findVersionOnDisk(String str, final List<Pair> list) {
        Sequence sequenceAsSequence;
        Sequence sequenceFilter;
        Sequence map;
        Sequence sequenceFilter2;
        final String str2 = str + SignatureVisitor.SUPER;
        File[] fileArrListFiles = getLibsDir().listFiles();
        if (fileArrListFiles == null || (sequenceAsSequence = ArraysKt.asSequence(fileArrListFiles)) == null || (sequenceFilter = SequencesKt.filter(sequenceAsSequence, new Function1() { // from class: com.exteragram.messenger.plugins.pip.PipController$$ExternalSyntheticLambda10
            @Override // kotlin.jvm.functions.Function1
            public final Object invoke(Object obj) {
                return Boolean.valueOf(PipController.findVersionOnDisk$lambda$0(str2, (File) obj));
            }
        })) == null || (map = SequencesKt.map(sequenceFilter, new Function1() { // from class: com.exteragram.messenger.plugins.pip.PipController$$ExternalSyntheticLambda11
            @Override // kotlin.jvm.functions.Function1
            public final Object invoke(Object obj) {
                return PipController.findVersionOnDisk$lambda$1(str2, (File) obj);
            }
        })) == null || (sequenceFilter2 = SequencesKt.filter(map, new Function1() { // from class: com.exteragram.messenger.plugins.pip.PipController$$ExternalSyntheticLambda12
            @Override // kotlin.jvm.functions.Function1
            public final Object invoke(Object obj) {
                return Boolean.valueOf(PipController.findVersionOnDisk$lambda$2(list, (String) obj));
            }
        })) == null) {
            return null;
        }
        return (String) SequencesKt.maxWithOrNull(sequenceFilter2, VersionComparator.INSTANCE);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static final boolean findVersionOnDisk$lambda$0(String str, File file) {
        if (file.isDirectory()) {
            String name = file.getName();
            Intrinsics.checkNotNullExpressionValue(name, "getName(...)");
            if (StringsKt.startsWith$default(name, str, false, 2, (Object) null)) {
                return true;
            }
        }
        return false;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static final String findVersionOnDisk$lambda$1(String str, File file) {
        String name = file.getName();
        Intrinsics.checkNotNullExpressionValue(name, "getName(...)");
        String strSubstring = name.substring(str.length());
        Intrinsics.checkNotNullExpressionValue(strSubstring, "substring(...)");
        return strSubstring;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static final boolean findVersionOnDisk$lambda$2(List list, String str) {
        Intrinsics.checkNotNullParameter(str, "version");
        return str.length() > 0 && INSTANCE.checkVersionSatisfies(str, list);
    }

    private final String findInstalledVersion(String str, List<Pair> list) {
        ConcurrentHashMap<String, Set<String>> concurrentHashMap = registry.get(str);
        if (concurrentHashMap == null) {
            return null;
        }
        Set<String> setKeySet = concurrentHashMap.keySet();
        Intrinsics.checkNotNullExpressionValue(setKeySet, "<get-keys>(...)");
        ArrayList arrayList = new ArrayList();
        for (Object obj : setKeySet) {
            String str2 = (String) obj;
            PipController pipController = INSTANCE;
            Intrinsics.checkNotNull(str2);
            if (pipController.checkVersionSatisfies(str2, list)) {
                arrayList.add(obj);
            }
        }
        return (String) CollectionsKt.maxWithOrNull(arrayList, VersionComparator.INSTANCE);
    }

    private final File getLibPath(String str, String str2) throws IOException {
        if (StringsKt.contains$default((CharSequence) str2, (CharSequence) "/", false, 2, (Object) null) || StringsKt.contains$default((CharSequence) str2, (CharSequence) "\\", false, 2, (Object) null) || StringsKt.contains$default((CharSequence) str2, (CharSequence) "..", false, 2, (Object) null)) {
            throw new IOException("Invalid version: " + str2);
        }
        return new File(getLibsDir(), str + SignatureVisitor.SUPER + str2);
    }

    private final void deletePackage(String str, String str2) throws IOException {
        File libPath = getLibPath(str, str2);
        if (libPath.exists()) {
            FilesKt.deleteRecursively(libPath);
            FileLog.d("PipController: Deleted package " + libPath.getName());
        }
    }

    private final Pair parseRequirement(String str) {
        String string = StringsKt.trim(REGEX_REQ_PAREN.replace(StringsKt.trim(REGEX_REQ_EXTRA.replace(StringsKt.trim((String) StringsKt.split$default((CharSequence) str, new String[]{";"}, false, 0, 6, (Object) null).get(0)).toString(), "")).toString(), "")).toString();
        MatchResult matchResultFind$default = Regex.find$default(REGEX_REQ_PARSE, string, 0, 2, null);
        if (matchResultFind$default == null) {
            return TuplesKt.to(string, CollectionsKt.emptyList());
        }
        MatchResult.Destructured destructured = matchResultFind$default.getDestructured();
        return TuplesKt.to((String) destructured.getMatch().getGroupValues().get(1), parseSpecs((String) destructured.getMatch().getGroupValues().get(2)));
    }

    private final List<Pair> parseSpecs(String str) {
        ArrayList arrayList = new ArrayList();
        if (!StringsKt.isBlank(str)) {
            Iterator it = StringsKt.split$default((CharSequence) str, new String[]{","}, false, 0, 6, (Object) null).iterator();
            while (it.hasNext()) {
                MatchResult matchResultFind$default = Regex.find$default(REGEX_REQ_SPECS, StringsKt.trim((String) it.next()).toString(), 0, 2, null);
                if (matchResultFind$default != null) {
                    MatchResult.Destructured destructured = matchResultFind$default.getDestructured();
                    arrayList.add(TuplesKt.to((String) destructured.getMatch().getGroupValues().get(1), StringsKt.trim((String) destructured.getMatch().getGroupValues().get(2)).toString()));
                }
            }
        }
        return arrayList;
    }

    private final List<String> parseDependenciesFromMetadata(File file) {
        ArrayList arrayList = new ArrayList();
        try {
            for (String str : FilesKt.readLines$default(file, null, 1, null)) {
                if (StringsKt.startsWith$default(str, "Requires-Dist:", false, 2, (Object) null)) {
                    String strSubstring = str.substring(14);
                    Intrinsics.checkNotNullExpressionValue(strSubstring, "substring(...)");
                    String string = StringsKt.trim(strSubstring).toString();
                    if (StringsKt.contains$default((CharSequence) string, (CharSequence) ";", false, 2, (Object) null)) {
                        List listSplit$default = StringsKt.split$default((CharSequence) string, new String[]{";"}, false, 2, 2, (Object) null);
                        String lowerCase = ((String) listSplit$default.get(1)).toLowerCase(Locale.ROOT);
                        Intrinsics.checkNotNullExpressionValue(lowerCase, "toLowerCase(...)");
                        if (INSTANCE.isMarkerCompatible(lowerCase)) {
                            arrayList.add(StringsKt.trim((String) listSplit$default.get(0)).toString());
                        }
                    } else {
                        arrayList.add(string);
                    }
                }
            }
            return arrayList;
        } catch (Exception e) {
            FileLog.e("PipController: Failed to parse metadata", e);
            return arrayList;
        }
    }

    private final boolean isMarkerCompatible(String str) {
        if (REGEX_MARKER_EXTRA.containsMatchIn(str) || !checkMarker(str, REGEX_MARKER_SYS_PLATFORM, "linux") || !checkMarker(str, REGEX_MARKER_PLATFORM_SYSTEM, "Linux") || !checkMarker(str, REGEX_MARKER_OS_NAME, "posix")) {
            return false;
        }
        MatchResult matchResultFind$default = Regex.find$default(REGEX_MARKER_PYTHON_VERSION, str, 0, 2, null);
        if (matchResultFind$default != null) {
            MatchResult.Destructured destructured = matchResultFind$default.getDestructured();
            if (!checkVersionSatisfies(pythonVersion, CollectionsKt.listOf(TuplesKt.to((String) destructured.getMatch().getGroupValues().get(1), (String) destructured.getMatch().getGroupValues().get(2))))) {
                return false;
            }
        }
        return true;
    }

    private final boolean checkMarker(String str, Regex regex, String str2) {
        MatchResult matchResultFind$default = Regex.find$default(regex, str, 0, 2, null);
        if (matchResultFind$default == null) {
            return true;
        }
        MatchResult.Destructured destructured = matchResultFind$default.getDestructured();
        String str3 = (String) destructured.getMatch().getGroupValues().get(1);
        String str4 = (String) destructured.getMatch().getGroupValues().get(2);
        Locale locale = Locale.ROOT;
        String lowerCase = str2.toLowerCase(locale);
        Intrinsics.checkNotNullExpressionValue(lowerCase, "toLowerCase(...)");
        String lowerCase2 = str4.toLowerCase(locale);
        Intrinsics.checkNotNullExpressionValue(lowerCase2, "toLowerCase(...)");
        if (Intrinsics.areEqual(str3, "==")) {
            return Intrinsics.areEqual(lowerCase, lowerCase2);
        }
        return (Intrinsics.areEqual(str3, "!=") && Intrinsics.areEqual(lowerCase, lowerCase2)) ? false : true;
    }

    private final File findMetadataFile(String str, String str2) {
        File[] fileArrListFiles = getLibPath(str, str2).listFiles(new FilenameFilter() { // from class: com.exteragram.messenger.plugins.pip.PipController$$ExternalSyntheticLambda8
            @Override // java.io.FilenameFilter
            public final boolean accept(File file, String str3) {
                return PipController.findMetadataFile$lambda$0(file, str3);
            }
        });
        File file = fileArrListFiles != null ? (File) ArraysKt.firstOrNull(fileArrListFiles) : null;
        if (file != null) {
            return new File(file, "METADATA");
        }
        return null;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static final boolean findMetadataFile$lambda$0(File file, String str) {
        Intrinsics.checkNotNull(str);
        return StringsKt.endsWith$default(str, ".dist-info", false, 2, (Object) null);
    }

    private final boolean checkVersionSatisfies(String str, List<Pair> list) {
        if (list.isEmpty()) {
            return true;
        }
        for (Pair pair : list) {
            String str2 = (String) pair.component1();
            String str3 = (String) pair.component2();
            VersionComparator versionComparator = VersionComparator.INSTANCE;
            int iCompare = versionComparator.compare(str, str3);
            int iHashCode = str2.hashCode();
            if (iHashCode == 60) {
                if (str2.equals("<") && iCompare >= 0) {
                    return false;
                }
            } else if (iHashCode == 62) {
                if (str2.equals(">") && iCompare <= 0) {
                    return false;
                }
            } else if (iHashCode == 1084) {
                if (str2.equals("!=") && iCompare == 0) {
                    return false;
                }
            } else if (iHashCode == 1921) {
                if (str2.equals("<=") && iCompare > 0) {
                    return false;
                }
            } else if (iHashCode == 1952) {
                if (str2.equals("==") && iCompare != 0) {
                    return false;
                }
            } else if (iHashCode == 1983) {
                if (str2.equals(">=") && iCompare < 0) {
                    return false;
                }
            } else if (iHashCode == 3967 && str2.equals("~=")) {
                if (iCompare < 0) {
                    return false;
                }
                List mutableList = CollectionsKt.toMutableList((Collection) StringsKt.split$default((CharSequence) str3, new String[]{"."}, false, 0, 6, (Object) null));
                if (mutableList.size() >= 2) {
                    mutableList.remove(CollectionsKt.getLastIndex(mutableList));
                    int lastIndex = CollectionsKt.getLastIndex(mutableList);
                    Integer intOrNull = StringsKt.toIntOrNull((String) mutableList.get(lastIndex));
                    if (intOrNull != null) {
                        mutableList.set(lastIndex, String.valueOf(intOrNull.intValue() + 1));
                        if (versionComparator.compare(str, CollectionsKt.joinToString$default(mutableList, ".", null, null, 0, null, null, 62, null)) >= 0) {
                            return false;
                        }
                    } else {
                        continue;
                    }
                } else {
                    continue;
                }
            }
        }
        return true;
    }

    private final String calculateSha256(File file) throws NoSuchAlgorithmException {
        MessageDigest messageDigest = MessageDigest.getInstance("SHA-256");
        FileInputStream fileInputStream = new FileInputStream(file);
        try {
            byte[] bArr = new byte[8192];
            while (true) {
                int i = fileInputStream.read(bArr);
                if (i != -1) {
                    messageDigest.update(bArr, 0, i);
                } else {
                    Unit unit = Unit.INSTANCE;
                    CloseableKt.closeFinally(fileInputStream, null);
                    byte[] bArrDigest = messageDigest.digest();
                    Intrinsics.checkNotNullExpressionValue(bArrDigest, "digest(...)");
                    return ArraysKt.joinToString$default(bArrDigest, (CharSequence) "", (CharSequence) null, (CharSequence) null, 0, (CharSequence) null, new Function1() { // from class: com.exteragram.messenger.plugins.pip.PipController$$ExternalSyntheticLambda9
                        @Override // kotlin.jvm.functions.Function1
                        public final Object invoke(Object obj) {
                            return PipController.calculateSha256$lambda$1(((Byte) obj).byteValue());
                        }
                    }, 30, (Object) null);
                }
            }
        } catch (Throwable th) {
            try {
                throw th;
            } catch (Throwable th2) {
                CloseableKt.closeFinally(fileInputStream, th);
                throw th2;
            }
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static final CharSequence calculateSha256$lambda$1(byte b) {
        String str = String.format("%02x", Arrays.copyOf(new Object[]{Byte.valueOf(b)}, 1));
        Intrinsics.checkNotNullExpressionValue(str, "format(...)");
        return str;
    }

    /* JADX INFO: Access modifiers changed from: private */
    static final class VersionComparator implements Comparator<String> {
        public static final VersionComparator INSTANCE = new VersionComparator();

        private VersionComparator() {
        }

        @Override // java.util.Comparator
        public int compare(String str, String str2) {
            Intrinsics.checkNotNullParameter(str, "v1");
            Intrinsics.checkNotNullParameter(str2, "v2");
            List<String> listSplitVersion = splitVersion(str);
            List<String> listSplitVersion2 = splitVersion(str2);
            int iMax = Math.max(listSplitVersion.size(), listSplitVersion2.size());
            for (int i = 0; i < iMax; i++) {
                String string = (String) CollectionsKt.getOrNull(listSplitVersion, i);
                String string2 = (String) CollectionsKt.getOrNull(listSplitVersion2, i);
                if (string == null) {
                    if ((string2 != null ? StringsKt.toIntOrNull(string2) : null) != null) {
                        string = "0";
                    }
                }
                if (string2 == null) {
                    if ((string != null ? StringsKt.toIntOrNull(string) : null) != null) {
                        string2 = "0";
                    }
                }
                if (string == null) {
                    string = "";
                }
                if (string2 == null) {
                    string2 = "";
                }
                if (!Intrinsics.areEqual(string, string2)) {
                    Integer intOrNull = StringsKt.toIntOrNull(string);
                    Integer intOrNull2 = StringsKt.toIntOrNull(string2);
                    if (intOrNull != null && intOrNull2 != null) {
                        int iCompare = Intrinsics.compare(intOrNull.intValue(), intOrNull2.intValue());
                        if (iCompare != 0) {
                            return iCompare;
                        }
                    } else {
                        if (intOrNull != null) {
                            return 1;
                        }
                        if (intOrNull2 != null) {
                            return -1;
                        }
                        int weight = getWeight(string);
                        int weight2 = getWeight(string2);
                        if (weight != weight2) {
                            return Intrinsics.compare(weight, weight2);
                        }
                        int iCompareTo = string.compareTo(string2);
                        if (iCompareTo != 0) {
                            return iCompareTo;
                        }
                    }
                }
            }
            return 0;
        }

        private final int getWeight(String str) {
            if (Intrinsics.areEqual(str, "post")) {
                return 110;
            }
            if (str.length() == 0) {
                return 100;
            }
            if (Intrinsics.areEqual(str, "rc") || Intrinsics.areEqual(str, "c")) {
                return 80;
            }
            if (Intrinsics.areEqual(str, "beta") || Intrinsics.areEqual(str, "b")) {
                return 70;
            }
            if (Intrinsics.areEqual(str, "alpha") || Intrinsics.areEqual(str, "a")) {
                return 60;
            }
            return Intrinsics.areEqual(str, "dev") ? 50 : 0;
        }

        private final List<String> splitVersion(String str) {
            return SequencesKt.toList(SequencesKt.map(Regex.findAll$default(PipController.REGEX_VERSION_SPLIT, str, 0, 2, null), new Function1() { // from class: com.exteragram.messenger.plugins.pip.PipController$VersionComparator$$ExternalSyntheticLambda0
                @Override // kotlin.jvm.functions.Function1
                public final Object invoke(Object obj) {
                    return PipController.VersionComparator.splitVersion$lambda$0((MatchResult) obj);
                }
            }));
        }

        /* JADX INFO: Access modifiers changed from: private */
        public static final String splitVersion$lambda$0(MatchResult matchResult) {
            Intrinsics.checkNotNullParameter(matchResult, "it");
            return matchResult.getValue();
        }
    }
}
