package com.radolyn.ayugram.utils;

import com.exteragram.messenger.backup.PreferencesUtils$$ExternalSyntheticBackport1;
import com.exteragram.messenger.utils.network.RemoteUtils;
import com.radolyn.ayugram.utils.fcm.IntegrityServiceException;
import java.util.Set;
import org.lsposed.lsparanoid.Deobfuscator$AyuGram4A$TMessagesProj;
import org.telegram.messenger.Utilities;
import org.telegram.ui.LoginActivity;

public abstract class PlayIntegrityUtils {
    public static String getTokenString() {
        String stringConfigValue = RemoteUtils.getStringConfigValue("integrity_pass_method", "exception_gp_not_installed");
        if (stringConfigValue.equals("random")) {
            Set stringSetConfigValue = RemoteUtils.getStringSetConfigValue("integrity_pass_random", PreferencesUtils$$ExternalSyntheticBackport1.m(new Object[]{"exception_gp_not_installed", "exception_token_null", "ignore"}));
            return map((String) stringSetConfigValue.toArray()[Utilities.random.nextInt(stringSetConfigValue.size())]);
        }
        return map(stringConfigValue);
    }

    private static String map(String str) {
        switch (str.hashCode()) {
            case -1409683949:
                if (str.equals("exception_by_id")) {
                    return "PLAYINTEGRITY_FAILED_EXCEPTION_" + LoginActivity.errorString(new IntegrityServiceException(RemoteUtils.getIntConfigValue("integrity_pass_exception_id", -3).intValue(), null));
                }
                break;
            case -1385650691:
                if (str.equals("exception_token_null")) {
                    return "PLAYINTEGRITY_FAILED_EXCEPTION_NULL";
                }
                break;
            case -1190396462:
                if (str.equals("ignore")) {
                    return null;
                }
                break;
            case 551846637:
                if (str.equals("exception_wrong_nonce")) {
                    return "PLAYINTEGRITY_FAILED_EXCEPTION_" + LoginActivity.errorString(new IntegrityServiceException(-13, new IllegalArgumentException("bad base-64")));
                }
                break;
            case 1656306827:
                if (str.equals("exception_no_project")) {
                    return "PLAYINTEGRITY_FAILED_EXCEPTION_NOPROJECT";
                }
                break;
            case 1699061288:
                if (str.equals("exception_gp_not_installed")) {
                    return "PLAYINTEGRITY_FAILED_EXCEPTION_" + LoginActivity.errorString(new IntegrityServiceException(-2, null));
                }
                break;
        }
        return "PLAYINTEGRITY_FAILED_EXCEPTION_NULL";
    }
}
