package kotlinx.coroutines.channels;

import kotlin.Result;
import kotlin.ResultKt;
import kotlin.Unit;
import kotlin.coroutines.Continuation;
import kotlin.coroutines.intrinsics.IntrinsicsKt;
import kotlin.coroutines.jvm.internal.SuspendLambda;
import kotlin.jvm.functions.Function2;
import kotlinx.coroutines.BuildersKt__BuildersKt;
import kotlinx.coroutines.CoroutineScope;

abstract /* synthetic */ class ChannelsKt__ChannelsKt {

    /* JADX INFO: renamed from: kotlinx.coroutines.channels.ChannelsKt__ChannelsKt$trySendBlocking$2, reason: invalid class name */
    static final class AnonymousClass2 extends SuspendLambda implements Function2 {
        final /* synthetic */ Object $element;
        final /* synthetic */ SendChannel $this_trySendBlocking;
        private /* synthetic */ Object L$0;
        int label;

        /* JADX WARN: 'super' call moved to the top of the method (can break code semantics) */
        AnonymousClass2(SendChannel sendChannel, Object obj, Continuation continuation) {
            super(2, continuation);
            this.$this_trySendBlocking = sendChannel;
            this.$element = obj;
        }

        @Override // kotlin.coroutines.jvm.internal.BaseContinuationImpl
        public final Continuation create(Object obj, Continuation continuation) {
            AnonymousClass2 anonymousClass2 = new AnonymousClass2(this.$this_trySendBlocking, this.$element, continuation);
            anonymousClass2.L$0 = obj;
            return anonymousClass2;
        }

        @Override // kotlin.jvm.functions.Function2
        public final Object invoke(CoroutineScope coroutineScope, Continuation continuation) {
            return ((AnonymousClass2) create(coroutineScope, continuation)).invokeSuspend(Unit.INSTANCE);
        }

        @Override // kotlin.coroutines.jvm.internal.BaseContinuationImpl
        public final Object invokeSuspend(Object obj) {
            Object objM2437constructorimpl;
            Object coroutine_suspended = IntrinsicsKt.getCOROUTINE_SUSPENDED();
            int i = this.label;
            try {
                if (i == 0) {
                    ResultKt.throwOnFailure(obj);
                    SendChannel sendChannel = this.$this_trySendBlocking;
                    Object obj2 = this.$element;
                    Result.Companion companion = Result.Companion;
                    this.label = 1;
                    if (sendChannel.send(obj2, this) == coroutine_suspended) {
                        return coroutine_suspended;
                    }
                } else {
                    if (i != 1) {
                        throw new IllegalStateException("call to 'resume' before 'invoke' with coroutine");
                    }
                    ResultKt.throwOnFailure(obj);
                }
                objM2437constructorimpl = Result.m2437constructorimpl(Unit.INSTANCE);
            } catch (Throwable th) {
                Result.Companion companion2 = Result.Companion;
                objM2437constructorimpl = Result.m2437constructorimpl(ResultKt.createFailure(th));
            }
            return ChannelResult.m2506boximpl(Result.m2442isSuccessimpl(objM2437constructorimpl) ? ChannelResult.Companion.m2519successJP2dKIU(Unit.INSTANCE) : ChannelResult.Companion.m2517closedJP2dKIU(Result.m2439exceptionOrNullimpl(objM2437constructorimpl)));
        }
    }

    public static final Object trySendBlocking(SendChannel sendChannel, Object obj) {
        Object objMo2503trySendJP2dKIU = sendChannel.mo2503trySendJP2dKIU(obj);
        if (objMo2503trySendJP2dKIU instanceof ChannelResult.Failed) {
            return ((ChannelResult) BuildersKt__BuildersKt.runBlocking$default(null, new AnonymousClass2(sendChannel, obj, null), 1, null)).m2516unboximpl();
        }
        return ChannelResult.Companion.m2519successJP2dKIU(Unit.INSTANCE);
    }
}
