package com.google.android.recaptcha.internal;

import android.app.Application;
import android.webkit.WebView;
import java.util.List;
import kotlin.Result;
import kotlin.ResultKt;
import kotlin.Unit;
import kotlin.coroutines.Continuation;
import kotlin.coroutines.intrinsics.IntrinsicsKt;
import kotlin.coroutines.jvm.internal.SuspendLambda;
import kotlin.jvm.functions.Function2;
import kotlin.sequences.SequencesKt;
import kotlinx.coroutines.AwaitKt;
import kotlinx.coroutines.BuildersKt;
import kotlinx.coroutines.CoroutineScope;
import kotlinx.coroutines.JobKt;
import kotlinx.coroutines.JobKt__JobKt;

final class zzaj extends SuspendLambda implements Function2 {
    Object zza;
    int zzb;
    final /* synthetic */ Application zzc;
    final /* synthetic */ zzab zzd;
    final /* synthetic */ String zze;
    final /* synthetic */ zzbq zzf;
    final /* synthetic */ zzbd zzg;
    final /* synthetic */ zzbg zzh;
    final /* synthetic */ long zzi;
    final /* synthetic */ zzt zzj;

    /* JADX WARN: 'super' call moved to the top of the method (can break code semantics) */
    zzaj(Application application, zzab zzabVar, String str, zzbq zzbqVar, zzbd zzbdVar, zzt zztVar, WebView webView, zzbg zzbgVar, long j, Continuation continuation) {
        super(2, continuation);
        this.zzc = application;
        this.zzd = zzabVar;
        this.zze = str;
        this.zzf = zzbqVar;
        this.zzg = zzbdVar;
        this.zzj = zztVar;
        this.zzh = zzbgVar;
        this.zzi = j;
    }

    @Override // kotlin.coroutines.jvm.internal.BaseContinuationImpl
    public final Continuation create(Object obj, Continuation continuation) {
        return new zzaj(this.zzc, this.zzd, this.zze, this.zzf, this.zzg, this.zzj, null, this.zzh, this.zzi, continuation);
    }

    @Override // kotlin.jvm.functions.Function2
    public final /* bridge */ /* synthetic */ Object invoke(Object obj, Object obj2) {
        return ((zzaj) create((CoroutineScope) obj, (Continuation) obj2)).invokeSuspend(Unit.INSTANCE);
    }

    /* JADX WARN: Code duplicated, block: B:17:0x0094  */
    /* JADX WARN: Code duplicated, block: B:19:0x00b6  */
    /* JADX WARN: Code duplicated, block: B:22:0x00e1  */
    @Override // kotlin.coroutines.jvm.internal.BaseContinuationImpl
    public final Object invokeSuspend(Object obj) throws Throwable {
        Object objWithContext;
        zzoe zzoeVar;
        Object objZzb;
        Throwable th;
        zzoe zzoeVar2;
        zzt zztVar;
        Throwable thM2439exceptionOrNullimpl;
        List list;
        Object coroutine_suspended = IntrinsicsKt.getCOROUTINE_SUSPENDED();
        int i = this.zzb;
        if (i == 0) {
            ResultKt.throwOnFailure(obj);
            zzam zzamVar = zzam.zza;
            Application application = this.zzc;
            zzab zzabVar = this.zzd;
            String str = this.zze;
            zzbq zzbqVar = this.zzf;
            zzbd zzbdVar = this.zzg;
            zzt zztVar2 = this.zzj;
            this.zzb = 1;
            objWithContext = BuildersKt.withContext(zztVar2.zza().getCoroutineContext(), new zzal(application, str, zzbdVar, zzbqVar, zzabVar, null), this);
            if (objWithContext != coroutine_suspended) {
            }
            return coroutine_suspended;
        }
        if (i == 1) {
            ResultKt.throwOnFailure(obj);
            objWithContext = obj;
        } else {
            if (i == 2) {
                zzoeVar = (zzoe) this.zza;
                ResultKt.throwOnFailure(obj);
                objZzb = ((Result) obj).m2444unboximpl();
                zzoeVar2 = zzoeVar;
                zztVar = this.zzj;
                thM2439exceptionOrNullimpl = Result.m2439exceptionOrNullimpl(objZzb);
                if (thM2439exceptionOrNullimpl != null) {
                    JobKt__JobKt.cancelChildren$default(zztVar.zzc().getCoroutineContext(), null, 1, null);
                    list = SequencesKt.toList(JobKt.getJob(zztVar.zzc().getCoroutineContext()).getChildren());
                    this.zza = thM2439exceptionOrNullimpl;
                    this.zzb = 3;
                    if (AwaitKt.joinAll(list, this) != coroutine_suspended) {
                        th = thM2439exceptionOrNullimpl;
                    }
                    return coroutine_suspended;
                }
                Application application2 = this.zzc;
                zzam zzamVar2 = zzam.zza;
                return new zzaw(application2, zzam.zze(), this.zze, this.zzj, this.zzd, zzoeVar2, this.zzg, this.zzh, new zzq(application2), new zzbs());
            }
            th = (Throwable) this.zza;
            ResultKt.throwOnFailure(obj);
        }
        zzam zzamVar3 = zzam.zza;
        zzam.zzf(new zzg(null, 1, null));
        throw th;
        zzoeVar = (zzoe) objWithContext;
        zzam.zze().zzd(new zzez(new WebView(this.zzc), this.zze, this.zzc, this.zzd, this.zzg, this.zzj, this.zzh, this.zzf));
        long j = this.zzi;
        zzg zzgVarZze = zzam.zze();
        this.zza = zzoeVar;
        this.zzb = 2;
        objZzb = zzgVarZze.zzb(j, zzoeVar, this);
        if (objZzb != coroutine_suspended) {
            zzoeVar2 = zzoeVar;
            zztVar = this.zzj;
            thM2439exceptionOrNullimpl = Result.m2439exceptionOrNullimpl(objZzb);
            if (thM2439exceptionOrNullimpl != null) {
                Application application3 = this.zzc;
                zzam zzamVar4 = zzam.zza;
                return new zzaw(application3, zzam.zze(), this.zze, this.zzj, this.zzd, zzoeVar2, this.zzg, this.zzh, new zzq(application3), new zzbs());
            }
            JobKt__JobKt.cancelChildren$default(zztVar.zzc().getCoroutineContext(), null, 1, null);
            list = SequencesKt.toList(JobKt.getJob(zztVar.zzc().getCoroutineContext()).getChildren());
            this.zza = thM2439exceptionOrNullimpl;
            this.zzb = 3;
            if (AwaitKt.joinAll(list, this) != coroutine_suspended) {
                th = thM2439exceptionOrNullimpl;
                zzam zzamVar5 = zzam.zza;
                zzam.zzf(new zzg(null, 1, null));
                throw th;
            }
        }
        return coroutine_suspended;
    }
}
