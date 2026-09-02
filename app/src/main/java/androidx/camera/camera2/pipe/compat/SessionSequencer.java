package androidx.camera.camera2.pipe.compat;

import kotlin.ResultKt;
import kotlin.Unit;
import kotlin.coroutines.Continuation;
import kotlin.coroutines.intrinsics.IntrinsicsKt;
import kotlin.coroutines.jvm.internal.ContinuationImpl;
import kotlin.enums.EnumEntries;
import kotlin.enums.EnumEntriesKt;
import kotlin.jvm.internal.Intrinsics;
import kotlinx.atomicfu.AtomicFU;
import kotlinx.atomicfu.AtomicRef;
import kotlinx.coroutines.sync.Mutex;

public final class SessionSequencer {
    private final ConcurrentSessionSequencer concurrentSequencer;
    private final AtomicRef state;

    public enum State {
        PENDING,
        CREATING,
        CREATED;

        private static final /* synthetic */ EnumEntries $ENTRIES = EnumEntriesKt.enumEntries(values());
    }

    /* JADX INFO: renamed from: androidx.camera.camera2.pipe.compat.SessionSequencer$awaitSessionLock$1, reason: invalid class name */
    static final class AnonymousClass1 extends ContinuationImpl {
        int label;
        /* synthetic */ Object result;

        AnonymousClass1(Continuation continuation) {
            super(continuation);
        }

        @Override // kotlin.coroutines.jvm.internal.BaseContinuationImpl
        public final Object invokeSuspend(Object obj) {
            this.result = obj;
            this.label |= Integer.MIN_VALUE;
            return SessionSequencer.this.awaitSessionLock(this);
        }
    }

    public SessionSequencer(ConcurrentSessionSequencer concurrentSequencer) {
        Intrinsics.checkNotNullParameter(concurrentSequencer, "concurrentSequencer");
        this.concurrentSequencer = concurrentSequencer;
        this.state = AtomicFU.atomic(State.PENDING);
    }

    /* JADX WARN: Code duplicated, block: B:7:0x0013  */
    public final Object awaitSessionLock(Continuation continuation) {
        AnonymousClass1 anonymousClass1;
        if (continuation instanceof AnonymousClass1) {
            anonymousClass1 = (AnonymousClass1) continuation;
            int i = anonymousClass1.label;
            if ((i & Integer.MIN_VALUE) != 0) {
                anonymousClass1.label = i - Integer.MIN_VALUE;
            } else {
                anonymousClass1 = new AnonymousClass1(continuation);
            }
        } else {
            anonymousClass1 = new AnonymousClass1(continuation);
        }
        Object obj = anonymousClass1.result;
        Object coroutine_suspended = IntrinsicsKt.getCOROUTINE_SUSPENDED();
        int i2 = anonymousClass1.label;
        if (i2 == 0) {
            ResultKt.throwOnFailure(obj);
            Mutex sharedMutex = this.concurrentSequencer.getSharedMutex();
            anonymousClass1.label = 1;
            if (Mutex.DefaultImpls.lock$default(sharedMutex, null, anonymousClass1, 1, null) == coroutine_suspended) {
                return coroutine_suspended;
            }
        } else {
            if (i2 != 1) {
                throw new IllegalStateException("call to 'resume' before 'invoke' with coroutine");
            }
            ResultKt.throwOnFailure(obj);
        }
        if (!this.state.compareAndSet(State.PENDING, State.CREATING)) {
            Mutex.DefaultImpls.unlock$default(this.concurrentSequencer.getSharedMutex(), null, 1, null);
        }
        return Unit.INSTANCE;
    }

    public final void release() {
        if (this.state.getAndSet(State.CREATED) == State.CREATING) {
            Mutex.DefaultImpls.unlock$default(this.concurrentSequencer.getSharedMutex(), null, 1, null);
        }
    }
}
