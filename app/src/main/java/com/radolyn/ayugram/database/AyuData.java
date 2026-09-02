package com.radolyn.ayugram.database;

import androidx.room.Room;
import androidx.sqlite.db.SimpleSQLiteQuery;
import com.exteragram.messenger.utils.chats.ChatUtils;
import com.radolyn.ayugram.AyuConfig;
import com.radolyn.ayugram.AyuConstants;
import com.radolyn.ayugram.controllers.AyuMessagesController;
import com.radolyn.ayugram.database.dao.DeletedDialogDao;
import com.radolyn.ayugram.database.dao.DeletedMessageDao;
import com.radolyn.ayugram.database.dao.EditedMessageDao;
import com.radolyn.ayugram.database.dao.RegexFilterDao;
import com.radolyn.ayugram.database.dao.SpyDao;
import java.io.File;
import org.lsposed.lsparanoid.Deobfuscator$AyuGram4A$TMessagesProj;
import org.telegram.messenger.AndroidUtilities;
import org.telegram.messenger.ApplicationLoader;
import org.telegram.messenger.FileLog;
import org.telegram.ui.LaunchActivity;

public class AyuData {
    private static AyuDatabase database;
    private static DeletedDialogDao deletedDialogDao;
    private static DeletedMessageDao deletedMessageDao;
    private static EditedMessageDao editedMessageDao;
    private static Runnable nextTidyUpAttachmentsRunnable;
    private static Runnable nextTidyUpDBRunnable;
    private static RegexFilterDao regexFilterDao;
    private static SpyDao spyDao;
    private static final Object sync = new Object();

    static {
        create();
    }

    public static void create() {
        synchronized (sync) {
            try {
                if (database != null) {
                    return;
                }
                AyuDatabase ayuDatabase = (AyuDatabase) Room.databaseBuilder(ApplicationLoader.applicationContext, AyuDatabase.class, AyuConstants.AYU_DATABASE).addMigrations(AyuMigrations.MIGRATION_25_26, AyuMigrations.MIGRATION_33_34).allowMainThreadQueries().fallbackToDestructiveMigration().build();
                database = ayuDatabase;
                editedMessageDao = ayuDatabase.editedMessageDao();
                deletedMessageDao = database.deletedMessageDao();
                deletedDialogDao = database.deletedDialogDao();
                regexFilterDao = database.regexFilterDao();
                spyDao = database.spyDao();
                tidyUpDB();
                tidyUpAttachments();
            } catch (Throwable th) {
                throw th;
            }
        }
    }

    public static EditedMessageDao getEditedMessageDao() {
        return editedMessageDao;
    }

    public static DeletedMessageDao getDeletedMessageDao() {
        return deletedMessageDao;
    }

    public static DeletedDialogDao getDeletedDialogDao() {
        return deletedDialogDao;
    }

    public static RegexFilterDao getRegexFilterDao() {
        return regexFilterDao;
    }

    public static SpyDao getSpyDao() {
        return spyDao;
    }

    public static long getAyuDatabaseSize() {
        File databasePath = ApplicationLoader.applicationContext.getDatabasePath(AyuConstants.AYU_DATABASE);
        File file = new File(databasePath.getAbsolutePath() + "-shm");
        File file2 = new File(databasePath.getAbsolutePath() + "-wal");
        long length = databasePath.exists() ? databasePath.length() : 0L;
        if (file.exists()) {
            length += file.length();
        }
        return file2.exists() ? length + file2.length() : length;
    }

    public static void clearMessageDatabase() {
        deletedDialogDao.deleteAll();
        deletedMessageDao.deleteAll();
        editedMessageDao.deleteAll();
        tidyUpDB(true);
    }

    public static void clearRegexFilterDatabase() {
        regexFilterDao.deleteAllFilters();
        regexFilterDao.deleteAllExclusions();
    }

    public static boolean exportDatabase() {
        boolean zCopyFile;
        database.close();
        database = null;
        File databasePath = ApplicationLoader.applicationContext.getDatabasePath(AyuConstants.AYU_DATABASE);
        File file = new File(AyuConfig.getSavePathJava(), "database-export.sqlite");
        if (databasePath.exists()) {
            try {
                zCopyFile = AndroidUtilities.copyFile(databasePath, file);
            } catch (Exception e) {
                FileLog.e(e);
                zCopyFile = false;
            }
        } else {
            zCopyFile = false;
        }
        create();
        return zCopyFile;
    }

    public static boolean importDatabase() {
        boolean zCopyFile;
        if (!canImportDatabase()) {
            return false;
        }
        File file = new File(AyuConfig.getSavePathJava(), "database-export.sqlite");
        File databasePath = ApplicationLoader.applicationContext.getDatabasePath(AyuConstants.AYU_DATABASE);
        deleteDatabase();
        if (file.exists()) {
            try {
                zCopyFile = AndroidUtilities.copyFile(file, databasePath);
            } catch (Exception e) {
                FileLog.e(e);
                zCopyFile = false;
            }
        } else {
            zCopyFile = false;
        }
        try {
            database = null;
            create();
            return zCopyFile;
        } catch (Exception unused) {
            deleteDatabase();
            create();
            return false;
        }
    }

    private static void deleteDatabase() {
        AyuDatabase ayuDatabase = database;
        if (ayuDatabase != null && ayuDatabase.isOpen()) {
            database.close();
            database = null;
        }
        File databasePath = ApplicationLoader.applicationContext.getDatabasePath(AyuConstants.AYU_DATABASE);
        File file = new File(databasePath.getAbsolutePath() + "-shm");
        File file2 = new File(databasePath.getAbsolutePath() + "-wal");
        ApplicationLoader.applicationContext.deleteDatabase(AyuConstants.AYU_DATABASE);
        if (databasePath.exists()) {
            databasePath.delete();
        }
        if (file.exists()) {
            file.delete();
        }
        if (file2.exists()) {
            file2.delete();
        }
    }

    public static boolean canImportDatabase() {
        File file = new File(AyuConfig.getSavePathJava(), "database-export.sqlite");
        return file.exists() && file.isFile();
    }

    public static void tidyUpDB() {
        tidyUpDB(false);
    }

    public static void tidyUpDB(boolean z) {
        if (LaunchActivity.isActive) {
            long jCurrentTimeMillis = System.currentTimeMillis() / 1000;
            long j = AyuConfig.preferences.getInt("last_db_tidyup", 0);
            if (!z && jCurrentTimeMillis - j < 86400) {
                if (nextTidyUpDBRunnable != null) {
                    ChatUtils.utilsQueue.cancelRunnable(nextTidyUpDBRunnable);
                }
                nextTidyUpDBRunnable = new Runnable() { // from class: com.radolyn.ayugram.database.AyuData$$ExternalSyntheticLambda0
                    @Override // java.lang.Runnable
                    public final void run() {
                        AyuData.tidyUpDB(false);
                    }
                };
                ChatUtils.utilsQueue.postRunnable(nextTidyUpDBRunnable, 3600000L);
                return;
            }
            AyuConfig.preferences.edit().putInt("last_db_tidyup", (int) (System.currentTimeMillis() / 1000)).apply();
            try {
                getSpyDao().deleteOldReads();
                getSpyDao().deleteOldContentsRead();
                getSpyDao().deleteOldLastSeen();
                getSpyDao().vacuum(new SimpleSQLiteQuery("VACUUM"));
            } catch (Exception e) {
                FileLog.e(e);
            }
        }
    }

    public static void tidyUpAttachments() {
        if (LaunchActivity.isActive) {
            if ((System.currentTimeMillis() / 1000) - ((long) AyuConfig.preferences.getInt("last_attachments_tidyup", 0)) < 86400) {
                if (nextTidyUpAttachmentsRunnable != null) {
                    ChatUtils.utilsQueue.cancelRunnable(nextTidyUpAttachmentsRunnable);
                }
                nextTidyUpAttachmentsRunnable = new Runnable() { // from class: com.radolyn.ayugram.database.AyuData$$ExternalSyntheticLambda1
                    @Override // java.lang.Runnable
                    public final void run() {
                        AyuData.tidyUpAttachments();
                    }
                };
                ChatUtils.utilsQueue.postRunnable(nextTidyUpAttachmentsRunnable, 3600000L);
                return;
            }
            AyuConfig.preferences.edit().putInt("last_attachments_tidyup", (int) (System.currentTimeMillis() / 1000)).apply();
            if (AyuConfig.saveMediaMaxCacheSize == Integer.MAX_VALUE) {
                return;
            }
            AyuMessagesController.onAttachmentsCleanUp();
        }
    }
}
