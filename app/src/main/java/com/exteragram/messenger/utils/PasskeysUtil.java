package com.exteragram.messenger.utils;

import android.app.Activity;
import android.os.Build;
import androidx.credentials.CredentialManager;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import kotlin.Result;
import kotlin.ResultKt;
import kotlin.Unit;
import kotlin.jvm.internal.Intrinsics;
import org.json.JSONObject;
import org.telegram.messenger.AndroidUtilities;
import org.telegram.messenger.ApplicationLoader;
import org.telegram.messenger.LocaleController;
import org.telegram.messenger.R;
import org.telegram.messenger.browser.Browser;
import org.telegram.ui.Components.Bulletin;
import org.telegram.ui.Components.BulletinFactory;

public final class PasskeysUtil {
    public static final PasskeysUtil INSTANCE = new PasskeysUtil();

    private PasskeysUtil() {
    }

    public static final Bulletin showUnsupportedBulletin(BulletinFactory factory) {
        Intrinsics.checkNotNullParameter(factory, "factory");
        Bulletin bulletinShow = factory.createSimpleBulletin(R.raw.error, LocaleController.getString(R.string.PasskeyUnsupportedTitle), AndroidUtilities.replaceMultipleTags(LocaleController.getString(R.string.PasskeyUnsupportedMessage), new Runnable() { // from class: com.exteragram.messenger.utils.PasskeysUtil$$ExternalSyntheticLambda0
            @Override // java.lang.Runnable
            public final void run() {
                PasskeysUtil.showUnsupportedBulletin$lambda$0();
            }
        }, new Runnable() { // from class: com.exteragram.messenger.utils.PasskeysUtil$$ExternalSyntheticLambda1
            @Override // java.lang.Runnable
            public final void run() {
                PasskeysUtil.showUnsupportedBulletin$lambda$1();
            }
        })).show();
        Intrinsics.checkNotNullExpressionValue(bulletinShow, "show(...)");
        return bulletinShow;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static final void showUnsupportedBulletin$lambda$0() {
        Browser.openUrl(ApplicationLoader.applicationContext, "https://github.com/bitwarden/android");
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static final void showUnsupportedBulletin$lambda$1() {
        Browser.openUrl(ApplicationLoader.applicationContext, "https://github.com/Kunzisoft/KeePassDX");
    }

    public static final byte[] computeClientDataHash(String clientDataJSON) throws NoSuchAlgorithmException {
        Intrinsics.checkNotNullParameter(clientDataJSON, "clientDataJSON");
        MessageDigest messageDigest = MessageDigest.getInstance("SHA-256");
        Charset UTF_8 = StandardCharsets.UTF_8;
        Intrinsics.checkNotNullExpressionValue(UTF_8, "UTF_8");
        byte[] bytes = clientDataJSON.getBytes(UTF_8);
        Intrinsics.checkNotNullExpressionValue(bytes, "getBytes(...)");
        byte[] bArrDigest = messageDigest.digest(bytes);
        Intrinsics.checkNotNullExpressionValue(bArrDigest, "digest(...)");
        return bArrDigest;
    }

    public static final String generateClientDataJSONRaw(boolean z, String str, String str2) {
        String string = new JSONObject().put("type", z ? "webauthn.get" : "webauthn.create").put("challenge", str).put("origin", str2).toString();
        Intrinsics.checkNotNullExpressionValue(string, "toString(...)");
        return string;
    }

    public static final void openSettings(Activity activity) {
        Intrinsics.checkNotNullParameter(activity, "activity");
        if (Build.VERSION.SDK_INT < 34) {
            return;
        }
        try {
            Result.Companion companion = Result.Companion;
            CredentialManager.Companion.create(activity).createSettingsPendingIntent().send();
            Result.m2437constructorimpl(Unit.INSTANCE);
        } catch (Throwable th) {
            Result.Companion companion2 = Result.Companion;
            Result.m2437constructorimpl(ResultKt.createFailure(th));
        }
    }
}
