package com.exteragram.messenger.icons;

import com.exteragram.messenger.export.output.FileManager;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;
import java.util.zip.ZipOutputStream;
import kotlin.ResultKt;
import kotlin.Unit;
import kotlin.collections.CollectionsKt;
import kotlin.collections.MapsKt;
import kotlin.coroutines.Continuation;
import kotlin.coroutines.intrinsics.IntrinsicsKt;
import kotlin.coroutines.jvm.internal.Boxing;
import kotlin.coroutines.jvm.internal.SuspendLambda;
import kotlin.io.ByteStreamsKt;
import kotlin.io.CloseableKt;
import kotlin.io.FilesKt;
import kotlin.jvm.functions.Function2;
import kotlin.jvm.internal.Intrinsics;
import kotlin.jvm.internal.Ref$ObjectRef;
import kotlin.ranges.RangesKt;
import kotlin.text.StringsKt;
import kotlinx.coroutines.BuildersKt;
import kotlinx.coroutines.BuildersKt__BuildersKt;
import kotlinx.coroutines.CoroutineScope;
import kotlinx.coroutines.Dispatchers;
import org.json.JSONObject;
import org.telegram.messenger.ApplicationLoader;
import org.telegram.messenger.FileLog;

public final class IconPackStorage {
    public static final IconPackStorage INSTANCE = new IconPackStorage();
    private static Map cachedCustomPacks;

    private IconPackStorage() {
    }

    public final File getIconPacksDirectory() {
        File file = new File(ApplicationLoader.applicationContext.getFilesDir(), "icon_packs");
        if (!file.exists()) {
            file.mkdirs();
        }
        return file;
    }

    private final IconPack parseMetadataFile(File file) {
        Iterator<String> itKeys;
        try {
            JSONObject jSONObject = new JSONObject(FilesKt.readText$default(file, null, 1, null));
            String string = jSONObject.getString("packName");
            String string2 = jSONObject.getString("packId");
            String strOptString = jSONObject.optString("author", "Unknown");
            String strOptString2 = jSONObject.optString("version", "1.0");
            JSONObject jSONObjectOptJSONObject = jSONObject.optJSONObject("icons");
            LinkedHashMap linkedHashMap = new LinkedHashMap();
            if (jSONObjectOptJSONObject != null && (itKeys = jSONObjectOptJSONObject.keys()) != null) {
                while (itKeys.hasNext()) {
                    String next = itKeys.next();
                    linkedHashMap.put(next, jSONObjectOptJSONObject.getString(next));
                }
            }
            Intrinsics.checkNotNull(string2);
            Intrinsics.checkNotNull(string);
            Intrinsics.checkNotNull(strOptString);
            Intrinsics.checkNotNull(strOptString2);
            return new IconPack(string2, string, strOptString, strOptString2, linkedHashMap, null, null, 96, null);
        } catch (Exception e) {
            FileLog.e("Error parsing metadata.json", e);
            return null;
        }
    }

    public final IconPack findPackById(String packId) {
        IconPack iconPack;
        Intrinsics.checkNotNullParameter(packId, "packId");
        Map map = cachedCustomPacks;
        if (map != null && (iconPack = (IconPack) map.get(packId)) != null) {
            return iconPack;
        }
        File file = new File(getIconPacksDirectory(), packId);
        if (!file.isDirectory()) {
            return null;
        }
        File file2 = new File(file, "metadata.json");
        if (file2.exists()) {
            return parseMetadataFile(file2);
        }
        return null;
    }

    /* JADX INFO: renamed from: com.exteragram.messenger.icons.IconPackStorage$bundlePack$2, reason: invalid class name */
    static final class AnonymousClass2 extends SuspendLambda implements Function2 {
        final /* synthetic */ String $packId;
        int label;

        /* JADX WARN: 'super' call moved to the top of the method (can break code semantics) */
        AnonymousClass2(String str, Continuation continuation) {
            super(2, continuation);
            this.$packId = str;
        }

        @Override // kotlin.coroutines.jvm.internal.BaseContinuationImpl
        public final Continuation create(Object obj, Continuation continuation) {
            return new AnonymousClass2(this.$packId, continuation);
        }

        @Override // kotlin.jvm.functions.Function2
        public final Object invoke(CoroutineScope coroutineScope, Continuation continuation) {
            return ((AnonymousClass2) create(coroutineScope, continuation)).invokeSuspend(Unit.INSTANCE);
        }

        @Override // kotlin.coroutines.jvm.internal.BaseContinuationImpl
        public final Object invokeSuspend(Object obj) {
            IntrinsicsKt.getCOROUTINE_SUSPENDED();
            if (this.label != 0) {
                throw new IllegalStateException("call to 'resume' before 'invoke' with coroutine");
            }
            ResultKt.throwOnFailure(obj);
            IconPackStorage iconPackStorage = IconPackStorage.INSTANCE;
            IconPack iconPackFindPackById = iconPackStorage.findPackById(this.$packId);
            if (iconPackFindPackById == null) {
                return null;
            }
            File file = new File(iconPackStorage.getIconPacksDirectory(), this.$packId);
            if (!file.exists()) {
                return null;
            }
            File file2 = new File(ApplicationLoader.applicationContext.getCacheDir(), "shared_packs");
            try {
                if (file2.exists()) {
                    FilesKt.deleteRecursively(file2);
                }
                file2.mkdirs();
                File file3 = new File(file2, FileManager.fileNameFromUserString(iconPackFindPackById.getName()) + ".icons");
                ZipOutputStream zipOutputStream = new ZipOutputStream(new FileOutputStream(file3));
                try {
                    File[] fileArrListFiles = file.listFiles();
                    if (fileArrListFiles != null) {
                        for (File file4 : fileArrListFiles) {
                            if (file4.isFile()) {
                                zipOutputStream.putNextEntry(new ZipEntry(file4.getName()));
                                Intrinsics.checkNotNull(file4);
                                FileInputStream fileInputStream = new FileInputStream(file4);
                                try {
                                    ByteStreamsKt.copyTo$default(fileInputStream, zipOutputStream, 0, 2, null);
                                    CloseableKt.closeFinally(fileInputStream, null);
                                    zipOutputStream.closeEntry();
                                } catch (Throwable th) {
                                    try {
                                        throw th;
                                    } catch (Throwable th2) {
                                        CloseableKt.closeFinally(fileInputStream, th);
                                        throw th2;
                                    }
                                }
                            }
                        }
                        Unit unit = Unit.INSTANCE;
                    }
                    CloseableKt.closeFinally(zipOutputStream, null);
                    return file3;
                } catch (Throwable th3) {
                    try {
                        throw th3;
                    } catch (Throwable th4) {
                        CloseableKt.closeFinally(zipOutputStream, th3);
                        throw th4;
                    }
                }
            } catch (Exception e) {
                FileLog.e("Failed to bundle pack: " + this.$packId, e);
                return null;
            }
        }
    }

    public final Object bundlePack(String str, Continuation continuation) {
        return BuildersKt.withContext(Dispatchers.getIO(), new AnonymousClass2(str, null), continuation);
    }

    /* JADX INFO: renamed from: com.exteragram.messenger.icons.IconPackStorage$bundlePackBlocking$1, reason: invalid class name */
    static final class AnonymousClass1 extends SuspendLambda implements Function2 {
        final /* synthetic */ String $packId;
        int label;

        /* JADX WARN: 'super' call moved to the top of the method (can break code semantics) */
        AnonymousClass1(String str, Continuation continuation) {
            super(2, continuation);
            this.$packId = str;
        }

        @Override // kotlin.coroutines.jvm.internal.BaseContinuationImpl
        public final Continuation create(Object obj, Continuation continuation) {
            return new AnonymousClass1(this.$packId, continuation);
        }

        @Override // kotlin.jvm.functions.Function2
        public final Object invoke(CoroutineScope coroutineScope, Continuation continuation) {
            return ((AnonymousClass1) create(coroutineScope, continuation)).invokeSuspend(Unit.INSTANCE);
        }

        @Override // kotlin.coroutines.jvm.internal.BaseContinuationImpl
        public final Object invokeSuspend(Object obj) {
            Object coroutine_suspended = IntrinsicsKt.getCOROUTINE_SUSPENDED();
            int i = this.label;
            if (i != 0) {
                if (i != 1) {
                    throw new IllegalStateException("call to 'resume' before 'invoke' with coroutine");
                }
                ResultKt.throwOnFailure(obj);
                return obj;
            }
            ResultKt.throwOnFailure(obj);
            IconPackStorage iconPackStorage = IconPackStorage.INSTANCE;
            String str = this.$packId;
            this.label = 1;
            Object objBundlePack = iconPackStorage.bundlePack(str, this);
            return objBundlePack == coroutine_suspended ? coroutine_suspended : objBundlePack;
        }
    }

    public final File bundlePackBlocking(String packId) {
        Intrinsics.checkNotNullParameter(packId, "packId");
        return (File) BuildersKt__BuildersKt.runBlocking$default(null, new AnonymousClass1(packId, null), 1, null);
    }

    public final List getCustomPacks() {
        IconPack metadataFile;
        Collection collectionValues;
        Map map = cachedCustomPacks;
        if (map != null && (collectionValues = map.values()) != null) {
            return new ArrayList(collectionValues);
        }
        ArrayList arrayList = new ArrayList();
        File[] fileArrListFiles = getIconPacksDirectory().listFiles();
        int i = 0;
        if (fileArrListFiles != null) {
            for (File file : fileArrListFiles) {
                if (file.isDirectory()) {
                    File file2 = new File(file, "metadata.json");
                    if (file2.exists() && (metadataFile = INSTANCE.parseMetadataFile(file2)) != null) {
                        arrayList.add(metadataFile);
                    }
                }
            }
        }
        LinkedHashMap linkedHashMap = new LinkedHashMap(RangesKt.coerceAtLeast(MapsKt.mapCapacity(CollectionsKt.collectionSizeOrDefault(arrayList, 10)), 16));
        int size = arrayList.size();
        while (i < size) {
            Object obj = arrayList.get(i);
            i++;
            linkedHashMap.put(((IconPack) obj).getId(), obj);
        }
        cachedCustomPacks = linkedHashMap;
        return arrayList;
    }

    public final void saveIconPackMetadata(IconPack iconPack) {
        Intrinsics.checkNotNullParameter(iconPack, "iconPack");
        File file = new File(getIconPacksDirectory(), iconPack.getId());
        if (!file.exists()) {
            file.mkdirs();
        }
        File file2 = new File(file, "metadata.json");
        try {
            JSONObject jSONObject = new JSONObject();
            jSONObject.put("schemaVersion", 1);
            jSONObject.put("packName", iconPack.getName());
            jSONObject.put("packId", iconPack.getId());
            jSONObject.put("author", iconPack.getAuthor());
            jSONObject.put("version", iconPack.getVersion());
            JSONObject jSONObject2 = new JSONObject();
            for (Map.Entry entry : iconPack.getIcons().entrySet()) {
                jSONObject2.put((String) entry.getKey(), (String) entry.getValue());
            }
            jSONObject.put("icons", jSONObject2);
            String string = jSONObject.toString(4);
            Intrinsics.checkNotNullExpressionValue(string, "toString(...)");
            FilesKt.writeText$default(file2, string, null, 2, null);
            cachedCustomPacks = null;
        } catch (Exception e) {
            FileLog.e("Error saving metadata", e);
        }
    }

    public final void deletePack(String packId) {
        Intrinsics.checkNotNullParameter(packId, "packId");
        File file = new File(getIconPacksDirectory(), packId);
        if (file.exists()) {
            FilesKt.deleteRecursively(file);
            cachedCustomPacks = null;
        }
    }

    /* JADX INFO: renamed from: com.exteragram.messenger.icons.IconPackStorage$installPack$2, reason: invalid class name and case insensitive filesystem */
    static final class C01482 extends SuspendLambda implements Function2 {
        final /* synthetic */ File $file;
        int label;

        /* JADX WARN: 'super' call moved to the top of the method (can break code semantics) */
        C01482(File file, Continuation continuation) {
            super(2, continuation);
            this.$file = file;
        }

        @Override // kotlin.coroutines.jvm.internal.BaseContinuationImpl
        public final Continuation create(Object obj, Continuation continuation) {
            return new C01482(this.$file, continuation);
        }

        @Override // kotlin.jvm.functions.Function2
        public final Object invoke(CoroutineScope coroutineScope, Continuation continuation) {
            return ((C01482) create(coroutineScope, continuation)).invokeSuspend(Unit.INSTANCE);
        }

        @Override // kotlin.coroutines.jvm.internal.BaseContinuationImpl
        public final Object invokeSuspend(Object obj) {
            IntrinsicsKt.getCOROUTINE_SUSPENDED();
            if (this.label != 0) {
                throw new IllegalStateException("call to 'resume' before 'invoke' with coroutine");
            }
            ResultKt.throwOnFailure(obj);
            File file = new File(ApplicationLoader.applicationContext.getCacheDir(), "install_" + System.currentTimeMillis());
            boolean z = false;
            try {
                try {
                    if (!file.mkdirs()) {
                        Boolean boolBoxBoolean = Boxing.boxBoolean(false);
                        if (file.exists()) {
                            FilesKt.deleteRecursively(file);
                        }
                        return boolBoxBoolean;
                    }
                    Ref$ObjectRef ref$ObjectRef = new Ref$ObjectRef();
                    FileInputStream fileInputStream = new FileInputStream(this.$file);
                    try {
                        ZipInputStream zipInputStream = new ZipInputStream(fileInputStream);
                        try {
                            for (ZipEntry nextEntry = zipInputStream.getNextEntry(); nextEntry != null; nextEntry = zipInputStream.getNextEntry()) {
                                File file2 = new File(file, nextEntry.getName());
                                String canonicalPath = file2.getCanonicalPath();
                                Intrinsics.checkNotNullExpressionValue(canonicalPath, "getCanonicalPath(...)");
                                String canonicalPath2 = file.getCanonicalPath();
                                Intrinsics.checkNotNullExpressionValue(canonicalPath2, "getCanonicalPath(...)");
                                if (!StringsKt.startsWith$default(canonicalPath, canonicalPath2, false, 2, (Object) null)) {
                                    throw new SecurityException("Zip Path Traversal Vulnerability");
                                }
                                if (nextEntry.isDirectory()) {
                                    Boxing.boxBoolean(file2.mkdirs());
                                } else {
                                    new File(file2.getParent()).mkdirs();
                                    FileOutputStream fileOutputStream = new FileOutputStream(file2);
                                    try {
                                        ByteStreamsKt.copyTo$default(zipInputStream, fileOutputStream, 0, 2, null);
                                        CloseableKt.closeFinally(fileOutputStream, null);
                                        if (Intrinsics.areEqual(nextEntry.getName(), "metadata.json")) {
                                            ref$ObjectRef.element = new JSONObject(FilesKt.readText$default(file2, null, 1, null));
                                        }
                                    } catch (Throwable th) {
                                        try {
                                            throw th;
                                        } catch (Throwable th2) {
                                            CloseableKt.closeFinally(fileOutputStream, th);
                                            throw th2;
                                        }
                                    }
                                }
                                try {
                                    throw th;
                                } catch (Throwable th3) {
                                    CloseableKt.closeFinally(fileInputStream, th);
                                    throw th3;
                                }
                            }
                            Unit unit = Unit.INSTANCE;
                            CloseableKt.closeFinally(zipInputStream, null);
                            CloseableKt.closeFinally(fileInputStream, null);
                            JSONObject jSONObject = (JSONObject) ref$ObjectRef.element;
                            String strOptString = jSONObject != null ? jSONObject.optString("packId") : null;
                            if (strOptString != null && !StringsKt.isBlank(strOptString)) {
                                IconPackStorage iconPackStorage = IconPackStorage.INSTANCE;
                                File file3 = new File(iconPackStorage.getIconPacksDirectory(), strOptString);
                                File file4 = new File(iconPackStorage.getIconPacksDirectory(), strOptString + "_trash_" + System.currentTimeMillis());
                                if (file3.exists() && !file3.renameTo(file4) && !FilesKt.deleteRecursively(file3)) {
                                    Boolean boolBoxBoolean2 = Boxing.boxBoolean(false);
                                    if (file.exists()) {
                                        FilesKt.deleteRecursively(file);
                                    }
                                    return boolBoxBoolean2;
                                }
                                if (file.renameTo(file3) || FilesKt.copyRecursively$default(file, file3, true, null, 4, null)) {
                                    if (file4.exists()) {
                                        FilesKt.deleteRecursively(file4);
                                    }
                                    IconPackStorage.cachedCustomPacks = null;
                                    if (file.exists()) {
                                        FilesKt.deleteRecursively(file);
                                    }
                                    z = true;
                                    return Boxing.boxBoolean(z);
                                }
                                if (file4.exists()) {
                                    if (file3.exists()) {
                                        FilesKt.deleteRecursively(file3);
                                    }
                                    file4.renameTo(file3);
                                }
                                Boolean boolBoxBoolean3 = Boxing.boxBoolean(false);
                                if (file.exists()) {
                                    FilesKt.deleteRecursively(file);
                                }
                                return boolBoxBoolean3;
                            }
                            Boolean boolBoxBoolean4 = Boxing.boxBoolean(false);
                            if (file.exists()) {
                                FilesKt.deleteRecursively(file);
                            }
                            return boolBoxBoolean4;
                        } catch (Throwable th4) {
                            try {
                                throw th4;
                            } catch (Throwable th5) {
                                CloseableKt.closeFinally(zipInputStream, th4);
                                throw th5;
                            }
                        }
                    } catch (Throwable th6) {
                        throw th6;
                    }
                } catch (Throwable th7) {
                    if (!file.exists()) {
                        throw th7;
                    }
                    FilesKt.deleteRecursively(file);
                    throw th7;
                }
            } catch (Exception e) {
                FileLog.e("Pack installation failed", e);
                if (file.exists()) {
                    FilesKt.deleteRecursively(file);
                }
            }
        }
    }

    public final Object installPack(File file, Continuation continuation) {
        return BuildersKt.withContext(Dispatchers.getIO(), new C01482(file, null), continuation);
    }

    /* JADX INFO: renamed from: com.exteragram.messenger.icons.IconPackStorage$parsePackFromZip$2, reason: invalid class name and case insensitive filesystem */
    static final class C01492 extends SuspendLambda implements Function2 {
        final /* synthetic */ File $file;
        int label;

        /* JADX WARN: 'super' call moved to the top of the method (can break code semantics) */
        C01492(File file, Continuation continuation) {
            super(2, continuation);
            this.$file = file;
        }

        @Override // kotlin.coroutines.jvm.internal.BaseContinuationImpl
        public final Continuation create(Object obj, Continuation continuation) {
            return new C01492(this.$file, continuation);
        }

        @Override // kotlin.jvm.functions.Function2
        public final Object invoke(CoroutineScope coroutineScope, Continuation continuation) {
            return ((C01492) create(coroutineScope, continuation)).invokeSuspend(Unit.INSTANCE);
        }

        @Override // kotlin.coroutines.jvm.internal.BaseContinuationImpl
        public final Object invokeSuspend(Object obj) {
            Iterator<String> itKeys;
            IntrinsicsKt.getCOROUTINE_SUSPENDED();
            if (this.label != 0) {
                throw new IllegalStateException("call to 'resume' before 'invoke' with coroutine");
            }
            ResultKt.throwOnFailure(obj);
            File file = new File(ApplicationLoader.applicationContext.getCacheDir(), "preview_" + System.currentTimeMillis());
            try {
                if (!file.mkdirs()) {
                    return null;
                }
                FileInputStream fileInputStream = new FileInputStream(this.$file);
                try {
                    ZipInputStream zipInputStream = new ZipInputStream(fileInputStream);
                    try {
                        JSONObject jSONObject = null;
                        for (ZipEntry nextEntry = zipInputStream.getNextEntry(); nextEntry != null; nextEntry = zipInputStream.getNextEntry()) {
                            File file2 = new File(file, nextEntry.getName());
                            String canonicalPath = file2.getCanonicalPath();
                            Intrinsics.checkNotNullExpressionValue(canonicalPath, "getCanonicalPath(...)");
                            String canonicalPath2 = file.getCanonicalPath();
                            Intrinsics.checkNotNullExpressionValue(canonicalPath2, "getCanonicalPath(...)");
                            if (!StringsKt.startsWith$default(canonicalPath, canonicalPath2, false, 2, (Object) null)) {
                                throw new SecurityException("Zip Path Traversal Vulnerability");
                            }
                            if (!nextEntry.isDirectory()) {
                                new File(file2.getParent()).mkdirs();
                                FileOutputStream fileOutputStream = new FileOutputStream(file2);
                                try {
                                    ByteStreamsKt.copyTo$default(zipInputStream, fileOutputStream, 0, 2, null);
                                    CloseableKt.closeFinally(fileOutputStream, null);
                                    if (Intrinsics.areEqual(nextEntry.getName(), "metadata.json")) {
                                        jSONObject = new JSONObject(FilesKt.readText$default(file2, null, 1, null));
                                    }
                                } catch (Throwable th) {
                                    try {
                                        throw th;
                                    } catch (Throwable th2) {
                                        CloseableKt.closeFinally(fileOutputStream, th);
                                        throw th2;
                                    }
                                }
                            }
                            try {
                                throw th;
                            } catch (Throwable th3) {
                                CloseableKt.closeFinally(fileInputStream, th);
                                throw th3;
                            }
                        }
                        Unit unit = Unit.INSTANCE;
                        CloseableKt.closeFinally(zipInputStream, null);
                        CloseableKt.closeFinally(fileInputStream, null);
                        if (jSONObject != null) {
                            String string = jSONObject.getString("packName");
                            String string2 = jSONObject.getString("packId");
                            String strOptString = jSONObject.optString("author", "Unknown");
                            String strOptString2 = jSONObject.optString("version", "1.0");
                            JSONObject jSONObjectOptJSONObject = jSONObject.optJSONObject("icons");
                            LinkedHashMap linkedHashMap = new LinkedHashMap();
                            if (jSONObjectOptJSONObject != null && (itKeys = jSONObjectOptJSONObject.keys()) != null) {
                                while (itKeys.hasNext()) {
                                    String next = itKeys.next();
                                    linkedHashMap.put(next, jSONObjectOptJSONObject.getString(next));
                                }
                            }
                            Intrinsics.checkNotNull(string2);
                            Intrinsics.checkNotNull(string);
                            Intrinsics.checkNotNull(strOptString);
                            Intrinsics.checkNotNull(strOptString2);
                            return new IconPack(string2, string, strOptString, strOptString2, linkedHashMap, null, file, 32, null);
                        }
                    } catch (Throwable th4) {
                        try {
                            throw th4;
                        } catch (Throwable th5) {
                            CloseableKt.closeFinally(zipInputStream, th4);
                            throw th5;
                        }
                    }
                } catch (Throwable th6) {
                    throw th6;
                }
            } catch (Exception e) {
                FileLog.e("Failed to parse pack for preview", e);
                FilesKt.deleteRecursively(file);
            }
            return null;
        }
    }

    public final Object parsePackFromZip(File file, Continuation continuation) {
        return BuildersKt.withContext(Dispatchers.getIO(), new C01492(file, null), continuation);
    }
}
