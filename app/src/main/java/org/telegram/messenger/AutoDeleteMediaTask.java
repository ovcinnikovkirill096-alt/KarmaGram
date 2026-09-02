package org.telegram.messenger;

import android.util.SparseArray;
import j$.util.concurrent.ConcurrentHashMap;
import java.io.File;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Set;

public class AutoDeleteMediaTask {
    public static Set<String> usingFilePaths = Collections.newSetFromMap(new ConcurrentHashMap());

    public static void run() {
        final int iCurrentTimeMillis = (int) (System.currentTimeMillis() / 1000);
        if (Math.abs(iCurrentTimeMillis - SharedConfig.lastKeepMediaCheckTime) < 86400) {
            return;
        }
        SharedConfig.lastKeepMediaCheckTime = iCurrentTimeMillis;
        final File fileCheckDirectory = FileLoader.checkDirectory(4);
        Utilities.cacheClearQueue.postRunnable(new Runnable() { // from class: org.telegram.messenger.AutoDeleteMediaTask$$ExternalSyntheticLambda0
            @Override // java.lang.Runnable
            public final void run() {
                AutoDeleteMediaTask.$r8$lambda$ap5ZJdebehBgIX4zKtBVK_9N3_k(iCurrentTimeMillis, fileCheckDirectory);
            }
        });
    }

    /* JADX WARN: Code duplicated, block: B:142:0x02ab  */
    public static /* synthetic */ void $r8$lambda$ap5ZJdebehBgIX4zKtBVK_9N3_k(int i, File file) {
        int i2;
        long j;
        int i3;
        int i4;
        long j2;
        long daysInSeconds;
        long daysInSeconds2;
        int i5;
        ArrayList<? extends CacheByChatsController.KeepMediaFile> arrayList;
        long j3;
        long jCurrentTimeMillis = System.currentTimeMillis();
        if (BuildVars.LOGS_ENABLED) {
            FileLog.d("checkKeepMedia start task");
        }
        ArrayList arrayList2 = new ArrayList();
        int i6 = 0;
        boolean z = false;
        while (true) {
            i2 = 1;
            if (i6 >= 16) {
                break;
            }
            if (UserConfig.getInstance(i6).isClientActivated()) {
                CacheByChatsController cacheByChatsController = UserConfig.getInstance(i6).getMessagesController().getCacheByChatsController();
                arrayList2.add(cacheByChatsController);
                if (cacheByChatsController.getKeepMediaExceptionsByDialogs().size() > 0) {
                    z = true;
                }
            }
            i6++;
        }
        int i7 = 4;
        int[] iArr = new int[4];
        boolean z2 = true;
        long j4 = Long.MAX_VALUE;
        for (int i8 = 0; i8 < 4; i8++) {
            int i9 = SharedConfig.getPreferences().getInt("keep_media_type_" + i8, CacheByChatsController.getDefault(i8));
            iArr[i8] = i9;
            if (i9 != CacheByChatsController.KEEP_MEDIA_FOREVER) {
                z2 = false;
            }
            long daysInSeconds3 = CacheByChatsController.getDaysInSeconds(i9);
            if (daysInSeconds3 < j4) {
                j4 = daysInSeconds3;
            }
        }
        if (z) {
            z2 = false;
        }
        SparseArray<File> sparseArrayCreateMediaPaths = ImageLoader.getInstance().createMediaPaths();
        int i10 = 0;
        int i11 = 0;
        long length = 0;
        while (i11 < sparseArrayCreateMediaPaths.size()) {
            if (z2 && (sparseArrayCreateMediaPaths.keyAt(i11) == i2 || sparseArrayCreateMediaPaths.keyAt(i11) == 3)) {
                j2 = jCurrentTimeMillis;
            } else {
                int i12 = sparseArrayCreateMediaPaths.keyAt(i11) == i7 ? i2 : 0;
                try {
                    File[] fileArrListFiles = sparseArrayCreateMediaPaths.valueAt(i11).listFiles();
                    ArrayList<? extends CacheByChatsController.KeepMediaFile> arrayList3 = new ArrayList<>();
                    if (fileArrListFiles != null) {
                        for (int i13 = 0; i13 < fileArrListFiles.length; i13++) {
                            if (!fileArrListFiles[i13].isDirectory() && !usingFilePaths.contains(fileArrListFiles[i13].getAbsolutePath())) {
                                arrayList3.add(new CacheByChatsController.KeepMediaFile(fileArrListFiles[i13]));
                            }
                        }
                    }
                    for (int i14 = 0; i14 < arrayList2.size(); i14++) {
                        ((CacheByChatsController) arrayList2.get(i14)).lookupFiles(arrayList3);
                    }
                    int i15 = 0;
                    while (i15 < arrayList3.size()) {
                        CacheByChatsController.KeepMediaFile keepMediaFile = (CacheByChatsController.KeepMediaFile) arrayList3.get(i15);
                        try {
                            if (keepMediaFile.isStory) {
                                daysInSeconds2 = CacheByChatsController.getDaysInSeconds(iArr[3]);
                                j2 = jCurrentTimeMillis;
                            } else {
                                j2 = jCurrentTimeMillis;
                                int i16 = keepMediaFile.keepMedia;
                                if (i16 != CacheByChatsController.KEEP_MEDIA_FOREVER) {
                                    if (i16 >= 0) {
                                        daysInSeconds = CacheByChatsController.getDaysInSeconds(i16);
                                    } else {
                                        int i17 = keepMediaFile.dialogType;
                                        if (i17 >= 0) {
                                            daysInSeconds = CacheByChatsController.getDaysInSeconds(iArr[i17]);
                                        } else if (i12 == 0) {
                                            daysInSeconds = j4;
                                        }
                                    }
                                    if (daysInSeconds != Long.MAX_VALUE) {
                                        daysInSeconds2 = daysInSeconds;
                                    }
                                }
                                i5 = i15;
                                arrayList = arrayList3;
                                i15 = i5 + 1;
                                i = i;
                                arrayList3 = arrayList;
                                jCurrentTimeMillis = j2;
                            }
                            arrayList = arrayList3;
                            long lastUsageFileTime = Utilities.getLastUsageFileTime(keepMediaFile.file.getAbsolutePath());
                            if (lastUsageFileTime <= 316000000 || lastUsageFileTime >= j3) {
                                i5 = i15;
                            } else {
                                i5 = i15;
                                if (!usingFilePaths.contains(keepMediaFile.file.getPath())) {
                                    try {
                                        if (BuildVars.LOGS_ENABLED) {
                                            i10++;
                                            length += keepMediaFile.file.length();
                                        }
                                        if (BuildVars.DEBUG_PRIVATE_VERSION) {
                                            FileLog.d("delete file " + keepMediaFile.file.getPath() + " last_usage_time=" + lastUsageFileTime + " time_local=" + j3 + " story=" + keepMediaFile.isStory);
                                        }
                                        keepMediaFile.file.delete();
                                    } catch (Exception e) {
                                        FileLog.e(e);
                                    }
                                }
                            }
                            i15 = i5 + 1;
                            i = i;
                            arrayList3 = arrayList;
                            jCurrentTimeMillis = j2;
                        } catch (Throwable th) {
                            th = th;
                            FileLog.e(th);
                            i11++;
                            jCurrentTimeMillis = j2;
                            i7 = 4;
                            i2 = 1;
                        }
                        j3 = ((long) i) - daysInSeconds2;
                    }
                    j2 = jCurrentTimeMillis;
                } catch (Throwable th2) {
                    th = th2;
                    j2 = jCurrentTimeMillis;
                }
            }
            i11++;
            jCurrentTimeMillis = j2;
            i7 = 4;
            i2 = 1;
        }
        long j5 = jCurrentTimeMillis;
        int i18 = SharedConfig.getPreferences().getInt("cache_limit", Integer.MAX_VALUE);
        if (i18 == Integer.MAX_VALUE) {
            j = 0;
            i3 = 0;
            i4 = 0;
        } else {
            long j6 = i18 == 1 ? 314572800L : ((long) i18) * 1048576000;
            long dirSize = 0;
            for (int i19 = 0; i19 < sparseArrayCreateMediaPaths.size(); i19++) {
                dirSize += Utilities.getDirSize(sparseArrayCreateMediaPaths.valueAt(i19).getAbsolutePath(), 0, true);
            }
            if (dirSize > j6) {
                ArrayList<? extends CacheByChatsController.KeepMediaFile> arrayList4 = new ArrayList<>();
                for (int i20 = 0; i20 < sparseArrayCreateMediaPaths.size(); i20++) {
                    fillFilesRecursive(sparseArrayCreateMediaPaths.valueAt(i20), arrayList4);
                }
                for (int i21 = 0; i21 < arrayList2.size(); i21++) {
                    ((CacheByChatsController) arrayList2.get(i21)).lookupFiles(arrayList4);
                }
                Collections.sort(arrayList4, new Comparator() { // from class: org.telegram.messenger.AutoDeleteMediaTask$$ExternalSyntheticLambda1
                    @Override // java.util.Comparator
                    public final int compare(Object obj, Object obj2) {
                        return AutoDeleteMediaTask.$r8$lambda$8dg4GbivFh1IfgXiPENigECyG2w((AutoDeleteMediaTask.FileInfoInternal) obj, (AutoDeleteMediaTask.FileInfoInternal) obj2);
                    }
                });
                j = 0;
                int i22 = 0;
                i3 = 0;
                for (int i23 = 0; i23 < arrayList4.size(); i23++) {
                    if (((FileInfoInternal) arrayList4.get(i23)).keepMedia != CacheByChatsController.KEEP_MEDIA_FOREVER) {
                        if (((FileInfoInternal) arrayList4.get(i23)).lastUsageDate > 0) {
                            long length2 = ((FileInfoInternal) arrayList4.get(i23)).file.length();
                            dirSize -= length2;
                            i3++;
                            j += length2;
                            try {
                                ((FileInfoInternal) arrayList4.get(i23)).file.delete();
                            } catch (Exception unused) {
                            }
                            if (dirSize < j6) {
                                break;
                            }
                        } else {
                            i22++;
                        }
                    }
                }
                i4 = i22;
            } else {
                j = 0;
                i3 = 0;
                i4 = 0;
            }
        }
        File file2 = new File(file, "acache");
        if (file2.exists()) {
            try {
                Utilities.clearDir(file2.getAbsolutePath(), 0, i - 86400, false);
            } catch (Throwable th3) {
                FileLog.e(th3);
            }
        }
        MessagesController.getGlobalMainSettings().edit().putInt("lastKeepMediaCheckTime", SharedConfig.lastKeepMediaCheckTime).apply();
        if (BuildVars.LOGS_ENABLED) {
            FileLog.d("checkKeepMedia task end time " + (System.currentTimeMillis() - j5) + " auto deleted info: files " + i10 + " size " + AndroidUtilities.formatFileSize(length) + "   deleted by size limit info: files " + i3 + " size " + AndroidUtilities.formatFileSize(j) + " unknownTimeFiles " + i4);
        }
    }

    public static /* synthetic */ int $r8$lambda$8dg4GbivFh1IfgXiPENigECyG2w(FileInfoInternal fileInfoInternal, FileInfoInternal fileInfoInternal2) {
        long j = fileInfoInternal2.lastUsageDate;
        long j2 = fileInfoInternal.lastUsageDate;
        if (j > j2) {
            return -1;
        }
        return j < j2 ? 1 : 0;
    }

    private static void fillFilesRecursive(File file, ArrayList<FileInfoInternal> arrayList) {
        File[] fileArrListFiles;
        if (file == null || (fileArrListFiles = file.listFiles()) == null) {
            return;
        }
        for (File file2 : fileArrListFiles) {
            if (file2.isDirectory()) {
                fillFilesRecursive(file2, arrayList);
            } else if (!file2.getName().equals(".nomedia") && !usingFilePaths.contains(file2.getAbsolutePath())) {
                arrayList.add(new FileInfoInternal(file2));
            }
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    static class FileInfoInternal extends CacheByChatsController.KeepMediaFile {
        final long lastUsageDate;

        private FileInfoInternal(File file) {
            super(file);
            this.lastUsageDate = Utilities.getLastUsageFileTime(file.getAbsolutePath());
        }
    }

    public static void lockFile(File file) {
        if (file == null) {
            return;
        }
        lockFile(file.getAbsolutePath());
    }

    public static void unlockFile(File file) {
        if (file == null) {
            return;
        }
        unlockFile(file.getAbsolutePath());
    }

    public static void lockFile(String str) {
        if (str == null) {
            return;
        }
        usingFilePaths.add(str);
    }

    public static void unlockFile(String str) {
        if (str == null) {
            return;
        }
        usingFilePaths.remove(str);
    }
}
