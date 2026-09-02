package androidx.camera.camera2.pipe.core;

import java.util.Collection;
import java.util.List;
import kotlin.Unit;
import kotlin.collections.ArrayDeque;
import kotlin.collections.CollectionsKt;
import kotlin.coroutines.Continuation;
import kotlin.coroutines.jvm.internal.SuspendLambda;
import kotlin.jvm.functions.Function1;
import kotlin.jvm.functions.Function2;
import kotlin.jvm.internal.DefaultConstructorMarker;
import kotlin.jvm.internal.Intrinsics;
import kotlinx.atomicfu.AtomicBoolean;
import kotlinx.atomicfu.AtomicFU;
import kotlinx.coroutines.BuildersKt__Builders_commonKt;
import kotlinx.coroutines.CoroutineScope;
import kotlinx.coroutines.SupervisorKt;
import kotlinx.coroutines.channels.Channel;
import kotlinx.coroutines.channels.ChannelKt;
import kotlinx.coroutines.channels.ChannelResult;

public final class PruningProcessingQueue {
    public static final Companion Companion = new Companion(null);
    private final int capacity;
    private final Channel channel;
    private final Function1 onUnprocessedElements;
    private final Function2 process;
    private final Function1 prune;
    private final ArrayDeque queue;
    private final AtomicBoolean started;

    public PruningProcessingQueue(int i, Function1 prune, Function1 onUnprocessedElements, Function2 process) {
        Intrinsics.checkNotNullParameter(prune, "prune");
        Intrinsics.checkNotNullParameter(onUnprocessedElements, "onUnprocessedElements");
        Intrinsics.checkNotNullParameter(process, "process");
        this.capacity = i;
        this.prune = prune;
        this.onUnprocessedElements = onUnprocessedElements;
        this.process = process;
        this.started = AtomicFU.atomic(false);
        this.channel = ChannelKt.Channel$default(i, null, new Function1() { // from class: androidx.camera.camera2.pipe.core.PruningProcessingQueue$$ExternalSyntheticLambda2
            @Override // kotlin.jvm.functions.Function1
            public final Object invoke(Object obj) {
                return PruningProcessingQueue.channel$lambda$0(this.f$0, obj);
            }
        }, 2, null);
        this.queue = new ArrayDeque();
    }

    public /* synthetic */ PruningProcessingQueue(int i, Function1 function1, Function1 function2, Function2 function3, int i2, DefaultConstructorMarker defaultConstructorMarker) {
        this((i2 & 1) != 0 ? Integer.MAX_VALUE : i, (i2 & 2) != 0 ? new Function1() { // from class: androidx.camera.camera2.pipe.core.PruningProcessingQueue$$ExternalSyntheticLambda0
            @Override // kotlin.jvm.functions.Function1
            public final Object invoke(Object obj) {
                return PruningProcessingQueue._init_$lambda$0((List) obj);
            }
        } : function1, (i2 & 4) != 0 ? new Function1() { // from class: androidx.camera.camera2.pipe.core.PruningProcessingQueue$$ExternalSyntheticLambda1
            @Override // kotlin.jvm.functions.Function1
            public final Object invoke(Object obj) {
                return PruningProcessingQueue._init_$lambda$1((List) obj);
            }
        } : function2, function3);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static final Unit _init_$lambda$0(List it) {
        Intrinsics.checkNotNullParameter(it, "it");
        return Unit.INSTANCE;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static final Unit _init_$lambda$1(List it) {
        Intrinsics.checkNotNullParameter(it, "it");
        return Unit.INSTANCE;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static final Unit channel$lambda$0(PruningProcessingQueue pruningProcessingQueue, Object obj) {
        pruningProcessingQueue.queue.add(obj);
        return Unit.INSTANCE;
    }

    public final boolean tryEmit(Object obj) {
        return ChannelResult.m2514isSuccessimpl(this.channel.mo2503trySendJP2dKIU(obj));
    }

    /* JADX INFO: renamed from: androidx.camera.camera2.pipe.core.PruningProcessingQueue$processingLoop$2, reason: invalid class name */
    static final class AnonymousClass2 extends SuspendLambda implements Function2 {
        private /* synthetic */ Object L$0;
        Object L$1;
        int label;

        AnonymousClass2(Continuation continuation) {
            super(2, continuation);
        }

        @Override // kotlin.coroutines.jvm.internal.BaseContinuationImpl
        public final Continuation create(Object obj, Continuation continuation) {
            AnonymousClass2 anonymousClass2 = PruningProcessingQueue.this.new AnonymousClass2(continuation);
            anonymousClass2.L$0 = obj;
            return anonymousClass2;
        }

        @Override // kotlin.jvm.functions.Function2
        public final Object invoke(CoroutineScope coroutineScope, Continuation continuation) {
            return ((AnonymousClass2) create(coroutineScope, continuation)).invokeSuspend(Unit.INSTANCE);
        }

        /* JADX WARN: Code duplicated, block: B:15:0x0039 A[Catch: all -> 0x001a, CancellationException -> 0x00e1, TRY_ENTER, TryCatch #2 {CancellationException -> 0x00e1, all -> 0x001a, blocks: (B:6:0x0016, B:15:0x0039, B:17:0x005a, B:18:0x0066), top: B:43:0x0016 }] */
        /* JADX WARN: Code duplicated, block: B:17:0x005a A[Catch: all -> 0x001a, CancellationException -> 0x00e1, TryCatch #2 {CancellationException -> 0x00e1, all -> 0x001a, blocks: (B:6:0x0016, B:15:0x0039, B:17:0x005a, B:18:0x0066), top: B:43:0x0016 }] */
        /* JADX WARN: Code duplicated, block: B:20:0x0072 A[RETURN] */
        /* JADX WARN: Code duplicated, block: B:23:0x007f  */
        /* JADX WARN: Unsupported multi-entry loop pattern (BACK_EDGE: B:19:0x0070 -> B:21:0x0073). Please report as a decompilation issue!!! */
        /*  JADX ERROR: JadxOverflowException in pass: RegionMakerVisitor
            jadx.core.utils.exceptions.JadxOverflowException: Regions stack size limit reached
            	at jadx.core.utils.ErrorsCounter.addError(ErrorsCounter.java:59)
            	at jadx.core.utils.ErrorsCounter.error(ErrorsCounter.java:31)
            	at jadx.core.dex.attributes.nodes.NotificationAttrNode.addError(NotificationAttrNode.java:19)
            */
        @Override // kotlin.coroutines.jvm.internal.BaseContinuationImpl
        public final java.lang.Object invokeSuspend(java.lang.Object r12) {
            /*
                Method dump skipped, instruction units count: 248
                To view this dump add '--comments-level debug' option
            */
            throw new UnsupportedOperationException("Method not decompiled: androidx.camera.camera2.pipe.core.PruningProcessingQueue.AnonymousClass2.invokeSuspend(java.lang.Object):java.lang.Object");
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public final Object processingLoop(Continuation continuation) {
        return SupervisorKt.supervisorScope(new AnonymousClass2(null), continuation);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public final void closeAndReleaseUnprocessedElements(Throwable th) {
        if (this.channel.close(th)) {
            Object objMo2502tryReceivePtdJZtk = this.channel.mo2502tryReceivePtdJZtk();
            while (ChannelResult.m2514isSuccessimpl(objMo2502tryReceivePtdJZtk)) {
                this.queue.add(ChannelResult.m2511getOrThrowimpl(objMo2502tryReceivePtdJZtk));
                objMo2502tryReceivePtdJZtk = this.channel.mo2502tryReceivePtdJZtk();
            }
            if (this.queue.isEmpty()) {
                return;
            }
            this.onUnprocessedElements.invoke(CollectionsKt.toMutableList((Collection) this.queue));
            this.queue.clear();
        }
    }

    public static final class Companion {
        public /* synthetic */ Companion(DefaultConstructorMarker defaultConstructorMarker) {
            this();
        }

        private Companion() {
        }

        public final PruningProcessingQueue processIn(PruningProcessingQueue pruningProcessingQueue, CoroutineScope scope) {
            Intrinsics.checkNotNullParameter(pruningProcessingQueue, "<this>");
            Intrinsics.checkNotNullParameter(scope, "scope");
            if (!pruningProcessingQueue.started.compareAndSet(false, true)) {
                throw new IllegalStateException("PruningProcessingQueue cannot be re-started!");
            }
            if (BuildersKt__Builders_commonKt.launch$default(scope, null, null, new PruningProcessingQueue$Companion$processIn$job$1(pruningProcessingQueue, null), 3, null).isCancelled()) {
                pruningProcessingQueue.closeAndReleaseUnprocessedElements(null);
            }
            return pruningProcessingQueue;
        }
    }
}
