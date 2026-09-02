package org.telegram.messenger;

import android.app.Activity;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.recaptcha.Recaptcha;
import com.google.android.recaptcha.RecaptchaAction;
import com.google.android.recaptcha.RecaptchaTasksClient;
import com.radolyn.ayugram.utils.fcm.AyuApplication;
import com.radolyn.ayugram.utils.fcm.CloudMessagingUtils;
import j$.util.Objects;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import org.telegram.tgnet.ConnectionsManager;

public class CaptchaController {
    public static HashMap<Integer, Request> currentRequests;

    public static class Request {
        public String action;
        public int currentAccount;
        public String key_id;
        public HashSet<Integer> requestTokens = new HashSet<>();

        public Request(int i, String str, String str2) {
            this.currentAccount = i;
            this.action = str;
            this.key_id = str2;
        }

        public int hashCode() {
            return Objects.hash(Integer.valueOf(this.currentAccount), this.action, this.key_id);
        }

        public void done(String str) {
            CaptchaController.currentRequests.remove(Integer.valueOf(hashCode()));
            int[] iArr = new int[this.requestTokens.size()];
            Iterator<Integer> it = this.requestTokens.iterator();
            int i = 0;
            while (it.hasNext()) {
                iArr[i] = it.next().intValue();
                i++;
            }
            ConnectionsManager.getInstance(this.currentAccount);
            ConnectionsManager.native_receivedCaptchaResult(this.currentAccount, iArr, str);
        }
    }

    public static void request(int i, int i2, final String str, final String str2) {
        if (currentRequests == null) {
            currentRequests = new HashMap<>();
        }
        Request request = currentRequests.get(Integer.valueOf(Objects.hash(Integer.valueOf(i), str, str2)));
        if (request != null) {
            request.requestTokens.add(Integer.valueOf(i2));
            return;
        }
        final Request request2 = new Request(i, str, str2);
        request2.requestTokens.add(Integer.valueOf(i2));
        Activity activity = AndroidUtilities.getActivity();
        if (activity == null) {
            FileLog.e("CaptchaController: no activity found");
            request2.done("RECAPTCHA_FAILED_NO_ACTIVITY");
        } else {
            final boolean[] zArr = {false};
            Utilities.globalQueue.postRunnable(new Runnable() { // from class: org.telegram.messenger.CaptchaController$$ExternalSyntheticLambda0
                @Override // java.lang.Runnable
                public final void run() {
                    CaptchaController.$r8$lambda$Rzow2e9RkKVaPuNkVC321zibrc8(zArr, request2);
                }
            }, 7000L);
            Recaptcha.getTasksClient(CloudMessagingUtils.spoofingNeeded() ? activity.getApplication() : new AyuApplication(activity.getApplication()), str2).addOnSuccessListener(new OnSuccessListener() { // from class: org.telegram.messenger.CaptchaController$$ExternalSyntheticLambda1
                @Override // com.google.android.gms.tasks.OnSuccessListener
                public final void onSuccess(Object obj) {
                    String str3 = str;
                    boolean[] zArr2 = zArr;
                    String str4 = str2;
                    CaptchaController.Request request3 = request2;
                    ((RecaptchaTasksClient) obj).executeTask(CaptchaController.getAction(str3)).addOnSuccessListener(new OnSuccessListener() { // from class: org.telegram.messenger.CaptchaController$$ExternalSyntheticLambda3
                        @Override // com.google.android.gms.tasks.OnSuccessListener
                        public final void onSuccess(Object obj2) {
                            CaptchaController.$r8$lambda$653bcEHkfwiHyOYTTmABSYBX2Eg(zArr2, str3, str4, request3, (String) obj2);
                        }
                    }).addOnFailureListener(new OnFailureListener() { // from class: org.telegram.messenger.CaptchaController$$ExternalSyntheticLambda4
                        @Override // com.google.android.gms.tasks.OnFailureListener
                        public final void onFailure(Exception exc) {
                            CaptchaController.m2771$r8$lambda$wAnfdkoiKDAM0n_6i_dRiLo(zArr2, request3, exc);
                        }
                    });
                }
            }).addOnFailureListener(new OnFailureListener() { // from class: org.telegram.messenger.CaptchaController$$ExternalSyntheticLambda2
                @Override // com.google.android.gms.tasks.OnFailureListener
                public final void onFailure(Exception exc) {
                    CaptchaController.$r8$lambda$pW24mWzzJOWsZzvwaRACq64tzsU(zArr, request2, exc);
                }
            });
        }
    }

    public static /* synthetic */ void $r8$lambda$Rzow2e9RkKVaPuNkVC321zibrc8(boolean[] zArr, Request request) {
        if (zArr[0]) {
            return;
        }
        zArr[0] = true;
        FileLog.e("CaptchaController: timeout after 7 seconds");
        request.done("RECAPTCHA_FAILED_TOKEN_NULL");
    }

    public static /* synthetic */ void $r8$lambda$653bcEHkfwiHyOYTTmABSYBX2Eg(boolean[] zArr, String str, String str2, Request request, String str3) {
        if (zArr[0]) {
            return;
        }
        zArr[0] = true;
        FileLog.d("CaptchaController: got token for {action=" + str + ", key_id=" + str2 + "}: " + str3);
        if (str3 == null) {
            request.done("RECAPTCHA_FAILED_TOKEN_NULL");
        } else {
            request.done(str3);
        }
    }

    /* JADX INFO: renamed from: $r8$lambda$wA-nfdko--iK-DAM0n_6i_dRiLo, reason: not valid java name */
    public static /* synthetic */ void m2771$r8$lambda$wAnfdkoiKDAM0n_6i_dRiLo(boolean[] zArr, Request request, Exception exc) {
        if (zArr[0]) {
            return;
        }
        zArr[0] = true;
        FileLog.e("CaptchaController: executeTask failure", exc);
        request.done("RECAPTCHA_FAILED_TASK_EXCEPTION_" + formatException(exc));
    }

    public static /* synthetic */ void $r8$lambda$pW24mWzzJOWsZzvwaRACq64tzsU(boolean[] zArr, Request request, Exception exc) {
        if (zArr[0]) {
            return;
        }
        zArr[0] = true;
        FileLog.e("CaptchaController: getTasksClient failure", exc);
        request.done("RECAPTCHA_FAILED_GETCLIENT_EXCEPTION_" + formatException(exc));
    }

    private static RecaptchaAction getAction(String str) {
        str.getClass();
        switch (str) {
            case "SIGNUP":
            case "signup":
                return RecaptchaAction.SIGNUP;
            case "LOGIN":
            case "login":
                return RecaptchaAction.LOGIN;
            default:
                return RecaptchaAction.custom(str);
        }
    }

    private static String formatException(Exception exc) {
        if (exc == null) {
            return "NULL";
        }
        return exc.getMessage() == null ? "MSG_NULL" : exc.getMessage().replaceAll(" ", "_").toUpperCase();
    }
}
