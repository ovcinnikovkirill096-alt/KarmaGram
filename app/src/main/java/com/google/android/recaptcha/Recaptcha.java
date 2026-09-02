package com.google.android.recaptcha;

import android.app.Application;
import com.google.android.gms.tasks.Task;
import com.google.android.recaptcha.internal.zzam;
import com.google.android.recaptcha.internal.zzaw;
import kotlin.Result;
import kotlin.ResultKt;
import kotlin.coroutines.Continuation;
import kotlin.coroutines.intrinsics.IntrinsicsKt;

public final class Recaptcha {
    public static final Recaptcha INSTANCE = new Recaptcha();

    private Recaptcha() {
    }

    /* JADX INFO: renamed from: getClient-BWLJW6A$default, reason: not valid java name */
    public static /* synthetic */ Object m2150getClientBWLJW6A$default(Recaptcha recaptcha, Application application, String str, long j, Continuation continuation, int i, Object obj) {
        if ((i & 4) != 0) {
            j = 10000;
        }
        return recaptcha.m2151getClientBWLJW6A(application, str, j, continuation);
    }

    public static final Task<RecaptchaTasksClient> getTasksClient(Application application, String str) {
        return zzam.zzd(application, str, 10000L);
    }

    public static final Task<RecaptchaTasksClient> getTasksClient(Application application, String str, long j) {
        return zzam.zzd(application, str, j);
    }

    /* JADX WARN: Code duplicated, block: B:8:0x0014  */
    /* JADX INFO: renamed from: getClient-BWLJW6A, reason: not valid java name */
    public final Object m2151getClientBWLJW6A(Application application, String str, long j, Continuation<? super Result> continuation) {
        Recaptcha$getClient$1 recaptcha$getClient$1;
        if (continuation instanceof Recaptcha$getClient$1) {
            recaptcha$getClient$1 = (Recaptcha$getClient$1) continuation;
            int i = recaptcha$getClient$1.zzc;
            if ((i & Integer.MIN_VALUE) != 0) {
                recaptcha$getClient$1.zzc = i - Integer.MIN_VALUE;
            } else {
                recaptcha$getClient$1 = new Recaptcha$getClient$1(this, continuation);
            }
        } else {
            recaptcha$getClient$1 = new Recaptcha$getClient$1(this, continuation);
        }
        Recaptcha$getClient$1 recaptcha$getClient$2 = recaptcha$getClient$1;
        Object objZzc = recaptcha$getClient$2.zza;
        Object coroutine_suspended = IntrinsicsKt.getCOROUTINE_SUSPENDED();
        int i2 = recaptcha$getClient$2.zzc;
        try {
            if (i2 == 0) {
                ResultKt.throwOnFailure(objZzc);
                Result.Companion companion = Result.Companion;
                zzam zzamVar = zzam.zza;
                recaptcha$getClient$2.zzc = 1;
                objZzc = zzam.zzc(application, str, j, null, recaptcha$getClient$2);
                if (objZzc == coroutine_suspended) {
                    return coroutine_suspended;
                }
            } else {
                if (i2 != 1) {
                    throw new IllegalStateException("call to 'resume' before 'invoke' with coroutine");
                }
                ResultKt.throwOnFailure(objZzc);
            }
            return Result.m2437constructorimpl((zzaw) objZzc);
        } catch (Throwable th) {
            Result.Companion companion2 = Result.Companion;
            return Result.m2437constructorimpl(ResultKt.createFailure(th));
        }
    }
}
