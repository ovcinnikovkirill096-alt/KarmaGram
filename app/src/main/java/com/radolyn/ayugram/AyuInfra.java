package com.radolyn.ayugram;

import com.radolyn.ayugram.utils.fcm.CloudMessagingUtils;
import java.io.File;
import java.security.MessageDigest;
import java.util.Arrays;
import java.util.Formatter;
import org.lsposed.lsparanoid.Deobfuscator$AyuGram4A$TMessagesProj;
import org.telegram.messenger.AndroidUtilities;
import org.telegram.messenger.ApplicationLoader;
import org.telegram.messenger.BuildVars;
import org.telegram.messenger.FileLog;

public abstract class AyuInfra {
    private static Boolean isModified;
    private static final String[] EXPECTED_SIGNATURES = {"f27d68285b107eb06cf14c74bb866368f1fbdc7f", "dbe511bc799a0d56b6dc20416af7b141416fa7ef"};
    private static final String[] EXPECTED_PACKAGE_NAMES = {"com.radolyn.ayugram", "org.telegram.messenger", "org.telegram.messenger.web"};

    public static void init() {
        AyuWorker.run();
        initializeAttachmentsFolder();
        if (isModified()) {
            FileLog.d("AyuGram is modified");
        }
    }

    public static boolean isModified() {
        if (isModified == null) {
            isModified = Boolean.valueOf(isAppModified());
        }
        return isModified.booleanValue();
    }

    public static String getVersionString() {
        StringBuilder sb = new StringBuilder();
        sb.append("AyuGram");
        if (BuildVars.IS_LITE_VERSION) {
            sb.append(" Lite");
        }
        sb.append(' ');
        sb.append(BuildVars.AYU_VERSION);
        if (CloudMessagingUtils.spoofingNeeded()) {
            sb.append(' ');
            sb.append(getPackageVersion().toUpperCase());
        }
        if (isModified()) {
            sb.append(" mod ðŸ’€");
        }
        return sb.toString();
    }

    public static void initializeAttachmentsFolder() {
        File file = new File(AyuConfig.getSavePathJava(), ".nomedia");
        try {
            AyuConfig.getSavePathJava().mkdirs();
            if (!AyuConfig.getSavePathJava().isDirectory() || file.exists()) {
                return;
            }
            AndroidUtilities.createEmptyFile(file);
            if (file.exists()) {
                return;
            }
            File file2 = new File(AyuConfig.getSavePathJava(), "nomedia_tmp" + AyuUtils.generateRandomString(3));
            AndroidUtilities.createEmptyFile(file2);
            if (!file2.exists() || file2.renameTo(file) || file2.delete()) {
                return;
            }
            file2.deleteOnExit();
        } catch (Throwable th) {
            FileLog.e(th);
        }
    }

    public static String getPackageVersion() {
        String packageName = AyuUtils.getPackageName();
        int iHashCode = packageName.hashCode();
        if (iHashCode != -1897170512) {
            if (iHashCode == -733096426 && packageName.equals("org.telegram.messenger.web")) {
                return "fcmweb";
            }
        } else if (packageName.equals("org.telegram.messenger")) {
            return "fcm";
        }
        return "default";
    }

    private static boolean isAppModified() {
        try {
            String str = ApplicationLoader.applicationLoaderInstance.getPackageManager().getPackageInfo(ApplicationLoader.applicationContext.getPackageName(), 64).packageName;
            byte[] bArrDigest = MessageDigest.getInstance("SHA-1").digest(ApplicationLoader.applicationLoaderInstance.getAyuPackageManager().getOriginalSignature().toByteArray());
            Formatter formatter = new Formatter();
            for (byte b : bArrDigest) {
                formatter.format("%02x", Byte.valueOf(b));
            }
            return (Arrays.asList(EXPECTED_PACKAGE_NAMES).contains(str) && Arrays.asList(EXPECTED_SIGNATURES).contains(formatter.toString())) ? false : true;
        } catch (Exception e) {
            FileLog.e(e);
            return true;
        }
    }
}
