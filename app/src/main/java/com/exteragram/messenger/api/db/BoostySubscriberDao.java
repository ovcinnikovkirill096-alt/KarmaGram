package com.exteragram.messenger.api.db;

import com.exteragram.messenger.api.dto.BoostySubscriberDTO;
import java.util.List;
import kotlin.ResultKt;
import kotlin.Unit;
import kotlin.coroutines.Continuation;
import kotlin.coroutines.intrinsics.IntrinsicsKt;
import kotlin.coroutines.jvm.internal.ContinuationImpl;
import kotlin.coroutines.jvm.internal.DebugMetadata;
import kotlin.coroutines.jvm.internal.SpillingKt;

public interface BoostySubscriberDao {

    /* JADX INFO: renamed from: com.exteragram.messenger.api.db.BoostySubscriberDao$replaceSubscribers$1, reason: invalid class name */
    @DebugMetadata(c = "com.exteragram.messenger.api.db.BoostySubscriberDao", f = "BoostySubscriberDao.kt", l = {34, 35}, m = "replaceSubscribers$suspendImpl", v = 1)
    static final class AnonymousClass1 extends ContinuationImpl {
        Object L$0;
        Object L$1;
        int label;
        /* synthetic */ Object result;

        AnonymousClass1(Continuation<? super AnonymousClass1> continuation) {
            super(continuation);
        }

        @Override // kotlin.coroutines.jvm.internal.BaseContinuationImpl
        public final Object invokeSuspend(Object obj) {
            this.result = obj;
            this.label |= Integer.MIN_VALUE;
            return CC.replaceSubscribers$suspendImpl(BoostySubscriberDao.this, null, this);
        }
    }

    Object deleteAll(Continuation<? super Unit> continuation);

    Object getAll(Continuation<? super List<BoostySubscriberDTO>> continuation);

    Object insertAll(List<BoostySubscriberDTO> list, Continuation<? super Unit> continuation);

    Object replaceSubscribers(List<BoostySubscriberDTO> list, Continuation<? super Unit> continuation);

    /* JADX INFO: renamed from: com.exteragram.messenger.api.db.BoostySubscriberDao$-CC, reason: invalid class name */
    public abstract /* synthetic */ class CC {
        /* JADX WARN: Code duplicated, block: B:7:0x0013  */
        /* JADX WARN: Code restructure failed: missing block: B:20:0x006b, code lost:
        
            if (r5.insertAll(r6, r0) == r1) goto L21;
         */
        /* JADX WARN: Multi-variable type inference failed */
        /*
            Code decompiled incorrectly, please refer to instructions dump.
        */
        public static /* synthetic */ Object replaceSubscribers$suspendImpl(BoostySubscriberDao boostySubscriberDao, List list, Continuation continuation) {
            AnonymousClass1 anonymousClass1;
            BoostySubscriberDao boostySubscriberDao2;
            if (continuation instanceof AnonymousClass1) {
                anonymousClass1 = (AnonymousClass1) continuation;
                int i = anonymousClass1.label;
                if ((i & Integer.MIN_VALUE) != 0) {
                    anonymousClass1.label = i - Integer.MIN_VALUE;
                } else {
                    anonymousClass1 = boostySubscriberDao.new AnonymousClass1(continuation);
                }
            } else {
                anonymousClass1 = boostySubscriberDao.new AnonymousClass1(continuation);
            }
            Object obj = anonymousClass1.result;
            Object coroutine_suspended = IntrinsicsKt.getCOROUTINE_SUSPENDED();
            int i2 = anonymousClass1.label;
            if (i2 == 0) {
                ResultKt.throwOnFailure(obj);
                anonymousClass1.L$0 = boostySubscriberDao;
                anonymousClass1.L$1 = list;
                anonymousClass1.label = 1;
                if (boostySubscriberDao.deleteAll(anonymousClass1) != coroutine_suspended) {
                }
                boostySubscriberDao2 = boostySubscriberDao;
                return coroutine_suspended;
            }
            if (i2 == 1) {
                list = (List) anonymousClass1.L$1;
                BoostySubscriberDao boostySubscriberDao3 = (BoostySubscriberDao) anonymousClass1.L$0;
                ResultKt.throwOnFailure(obj);
                boostySubscriberDao2 = boostySubscriberDao3;
            } else {
                if (i2 != 2) {
                    throw new IllegalStateException("call to 'resume' before 'invoke' with coroutine");
                }
                ResultKt.throwOnFailure(obj);
            }
            return Unit.INSTANCE;
            boostySubscriberDao2 = boostySubscriberDao;
            anonymousClass1.L$0 = SpillingKt.nullOutSpilledVariable(boostySubscriberDao2);
            anonymousClass1.L$1 = SpillingKt.nullOutSpilledVariable(list);
            anonymousClass1.label = 2;
        }
    }

    public static final class DefaultImpls {
        @Deprecated
        public static Object replaceSubscribers(BoostySubscriberDao boostySubscriberDao, List<BoostySubscriberDTO> list, Continuation<? super Unit> continuation) {
            return CC.replaceSubscribers$suspendImpl(boostySubscriberDao, list, continuation);
        }
    }
}
