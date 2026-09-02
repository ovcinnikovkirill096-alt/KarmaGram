package com.radolyn.ayugram.utils.fcm;

import android.content.Context;
import com.google.firebase.FirebaseOptions;
import org.lsposed.lsparanoid.Deobfuscator$AyuGram4A$TMessagesProj;
import org.telegram.messenger.AndroidUtilities;
import org.telegram.messenger.ApplicationLoader;

public abstract class CloudMessagingUtils {
    public static boolean isSignatureSpoofed() {
        try {
            Context context = ApplicationLoader.applicationContext;
            return context.getPackageManager().getPackageInfo(context.getPackageName(), 64).signatures[0].toCharsString().equals("3082021730820180a0030201020204521f9d49300d06092a864886f70d0101050500305031193017060355040713105361696e742d50657465727362757267310b3009060355040a1302564b310b3009060355040b1302564b31193017060355040313104e696b6f6c6179204b75646173686f76301e170d3133303832393139313331335a170d3338303832333139313331335a305031193017060355040713105361696e742d50657465727362757267310b3009060355040a1302564b310b3009060355040b1302564b31193017060355040313104e696b6f6c6179204b75646173686f7630819f300d06092a864886f70d010101050003818d0030818902818100df5e993a0dec0ab5b557dfff77e0b2227186cbf13d1fd1ed8e9deb5650c5fd4467bb51bfa585228d084bd27045f7415b7c4e38f08be362639a2eeb9b0c749da460f2705f6a7e14aca76abe3360af00b719cc5f3ff4d4da05958327e948b3679e6417ad7baa8779b9d689799ba345839a049fd44362499054a0803a0178c773790203010001300d06092a864886f70d010105050003818100dda58cdd90159c431ecc4a15902eafb07a50e01ba9d4f8e655ec14b06bd8e8771239710a28991039e02e352762eb524af07602bbdfb479d3718658a534d411dfab30122c8d0a5efd165a620669d80a221a04ac7d68b3811150c769cf97d3274be9b9f27c4c5877eabbcf8990409e5943df8deb509fa83d68eabc74f7c5976743");
        } catch (Exception unused) {
            return false;
        }
    }

    public static boolean spoofingNeeded() {
        return spoofingNeeded(ApplicationLoader.applicationContext);
    }

    public static boolean spoofingNeeded(Context context) {
        if (context == null) {
            context = AndroidUtilities.getActivity();
        }
        if (context == null) {
            return false;
        }
        String packageName = context.getPackageName();
        return packageName.equals("org.telegram.messenger") || packageName.equals("org.telegram.messenger.web");
    }

    public static FirebaseOptions getConfig() {
        String packageName = ApplicationLoader.applicationContext.getPackageName();
        if (packageName.equals("org.telegram.messenger") || packageName.equals("org.telegram.messenger.web")) {
            return new FirebaseOptions.Builder().setApplicationId("1:760348033671:android:7396e651423888c3f66e22").setApiKey("AIzaSyA-t0jLPjUt2FxrA8VPK2EiYHcYcboIR6k").setStorageBucket("tmessages2.appspot.com").setDatabaseUrl("https://tmessages2.firebaseio.com").setProjectId("tmessages2").setGcmSenderId("760348033671").build();
        }
        throw new RuntimeException("Unknown package name for spoof: " + packageName);
    }

    public static boolean isFcmCallBypass() {
        for (StackTraceElement stackTraceElement : Thread.currentThread().getStackTrace()) {
            if (stackTraceElement.getMethodName().contains("requestPermissions") || stackTraceElement.getMethodName().contains("PermissionGranted")) {
                return true;
            }
            if ((stackTraceElement.getMethodName().contains("isEnabledFor") && stackTraceElement.getClassName().contains("GmsCompat")) || stackTraceElement.getMethodName().contains("showExtraNotifications")) {
                return true;
            }
        }
        return false;
    }
}
