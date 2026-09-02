package com.radolyn.ayugram;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Process;
import android.text.Html;
import android.text.SpannableString;
import android.util.LongSparseArray;
import com.exteragram.messenger.utils.text.LocaleUtils;
import j$.time.LocalDate;
import j$.time.format.DateTimeFormatter;
import java.util.ArrayList;
import org.telegram.messenger.ApplicationLoader;
import org.telegram.messenger.BuildVars;
import org.telegram.messenger.FileLog;
import org.telegram.messenger.Utilities;
import org.telegram.ui.ActionBar.BaseFragment;
import org.telegram.ui.LaunchActivity;

public class AyuUtils {
    public static String generateRandomString(int i) {
        return Utilities.generateRandomString(i).toLowerCase();
    }

    public static String getPackageName() {
        return ApplicationLoader.applicationContext.getPackageName();
    }

    public static String getVersionPretty() {
        return LocalDate.parse(BuildVars.AYU_VERSION, DateTimeFormatter.ofPattern("yyyyMMdd")).format(DateTimeFormatter.ofPattern("yyyy.MM.dd"));
    }

    public static CharSequence htmlToString(String str) {
        SpannableString spannableString;
        if (Build.VERSION.SDK_INT >= 24) {
            spannableString = new SpannableString(Html.fromHtml(str, 0));
        } else {
            spannableString = new SpannableString(Html.fromHtml(str));
        }
        return LocaleUtils.formatWithURLs(spannableString);
    }

    public static void killApplication(Activity activity) {
        activity.finishAndRemoveTask();
        Process.killProcess(Process.myPid());
    }

    public static void restartApplication() {
        BaseFragment safeLastFragment = LaunchActivity.getSafeLastFragment();
        if (safeLastFragment == null) {
            return;
        }
        Context context = safeLastFragment.getContext();
        Intent launchIntentForPackage = context.getPackageManager().getLaunchIntentForPackage(context.getPackageName());
        Intent intentMakeRestartActivityTask = Intent.makeRestartActivityTask(launchIntentForPackage == null ? null : launchIntentForPackage.getComponent());
        intentMakeRestartActivityTask.setPackage(context.getPackageName());
        context.startActivity(intentMakeRestartActivityTask);
        Runtime.getRuntime().exit(0);
    }

    public static void logError(String str, Throwable th) {
        FileLog.e(str, th);
    }

    public static <T> ArrayList<T> sparseArrayToList(LongSparseArray<T> longSparseArray) {
        ArrayList<T> arrayList = new ArrayList<>(longSparseArray.size());
        for (int i = 0; i < longSparseArray.size(); i++) {
            arrayList.add(longSparseArray.valueAt(i));
        }
        return arrayList;
    }
}
