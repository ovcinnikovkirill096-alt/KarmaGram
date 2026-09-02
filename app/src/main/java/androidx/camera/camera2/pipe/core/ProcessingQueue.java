package androidx.camera.camera2.pipe.core;

import java.util.Collection;
import java.util.List;
import kotlin.Unit;
import kotlin.collections.ArrayDeque;
import kotlin.collections.CollectionsKt;
import kotlin.coroutines.Continuation;
import kotlin.coroutines.jvm.internal.ContinuationImpl;
import kotlin.jvm.functions.Function1;
import kotlin.jvm.functions.Function2;
import kotlin.jvm.internal.DefaultConstructorMarker;
import kotlin.jvm.internal.Intrinsics;
import kotlinx.atomicfu.AtomicBoolean;
import kotlinx.atomicfu.AtomicFU;
import kotlinx.coroutines.BuildersKt__Builders_commonKt;
import kotlinx.coroutines.CoroutineScope;
import kotlinx.coroutines.channels.Channel;
import kotlinx.coroutines.channels.ChannelKt;
import kotlinx.coroutines.channels.ChannelResult;

public final class ProcessingQueue {
    public static final Companion Companion = new Companion(null);
    private final int capacity;
    private final Channel channel;
    private final Function1 onUnprocessedElements;
    private final Function2 process;
    private final ArrayDeque queue;
    private final AtomicBoolean started;

    /* JADX INFO: renamed from: androidx.camera.camera2.pipe.core.ProcessingQueue$processingLoop$1, reason: invalid class name */
    static final class AnonymousClass1 extends ContinuationImpl {
        int I$0;
        int label;
        /* synthetic */ Object result;

        AnonymousClass1(Continuation continuation) {
            super(continuation);
        }

        @Override // kotlin.coroutines.jvm.internal.BaseContinuationImpl
        public final Object invokeSuspend(Object obj) {
            this.result = obj;
            this.label |= Integer.MIN_VALUE;
            return ProcessingQueue.this.processingLoop(this);
        }
    }

    public ProcessingQueue(int i, Function1 onUnprocessedElements, Function2 process) {
        Intrinsics.checkNotNullParameter(onUnprocessedElements, "onUnprocessedElements");
        Intrinsics.checkNotNullParameter(process, "process");
        this.capacity = i;
        this.onUnprocessedElements = onUnprocessedElements;
        this.process = process;
        this.started = AtomicFU.atomic(false);
        this.channel = ChannelKt.Channel$default(i, null, new Function1() { // from class: androidx.camera.camera2.pipe.core.ProcessingQueue$$ExternalSyntheticLambda1
            @Override // kotlin.jvm.functions.Function1
            public final Object invoke(Object obj) {
                return ProcessingQueue.channel$lambda$0(this.f$0, obj);
            }
        }, 2, null);
        this.queue = new ArrayDeque();
    }

    public /* synthetic */ ProcessingQueue(int i, Function1 function1, Function2 function2, int i2, DefaultConstructorMarker defaultConstructorMarker) {
        this((i2 & 1) != 0 ? Integer.MAX_VALUE : i, (i2 & 2) != 0 ? new Function1() { // from class: androidx.camera.camera2.pipe.core.ProcessingQueue$$ExternalSyntheticLambda0
            @Override // kotlin.jvm.functions.Function1
            public final Object invoke(Object obj) {
                return ProcessingQueue._init_$lambda$0((List) obj);
            }
        } : function1, function2);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static final Unit _init_$lambda$0(List it) {
        Intrinsics.checkNotNullParameter(it, "it");
        return Unit.INSTANCE;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static final Unit channel$lambda$0(ProcessingQueue processingQueue, Object obj) {
        processingQueue.queue.add(obj);
        return Unit.INSTANCE;
    }

    public final boolean tryEmit(Object obj) {
        return ChannelResult.m2514isSuccessimpl(this.channel.mo2503trySendJP2dKIU(obj));
    }

    /* JADX INFO: Access modifiers changed from: private */
    /* JADX WARN: Code duplicated, block: B:22:0x003f A[Catch: all -> 0x002e, TRY_ENTER, TryCatch #0 {all -> 0x002e, blocks: (B:13:0x002a, B:35:0x0088, B:26:0x004f, B:28:0x0057, B:29:0x005d, B:31:0x0063, B:32:0x0073, B:22:0x003f, B:25:0x004a, B:19:0x0038), top: B:40:0x0022 }] */
    /* JADX WARN: Code duplicated, block: B:24:0x0049  */
    /* JADX WARN: Code duplicated, block: B:25:0x004a A[Catch: all -> 0x002e, TryCatch #0 {all -> 0x002e, blocks: (B:13:0x002a, B:35:0x0088, B:26:0x004f, B:28:0x0057, B:29:0x005d, B:31:0x0063, B:32:0x0073, B:22:0x003f, B:25:0x004a, B:19:0x0038), top: B:40:0x0022 }] */
    /* JADX WARN: Code duplicated, block: B:28:0x0057 A[Catch: all -> 0x002e, TryCatch #0 {all -> 0x002e, blocks: (B:13:0x002a, B:35:0x0088, B:26:0x004f, B:28:0x0057, B:29:0x005d, B:31:0x0063, B:32:0x0073, B:22:0x003f, B:25:0x004a, B:19:0x0038), top: B:40:0x0022 }] */
    /* JADX WARN: Code duplicated, block: B:31:0x0063 A[Catch: all -> 0x002e, LOOP:0: B:29:0x005d->B:31:0x0063, LOOP_END, TryCatch #0 {all -> 0x002e, blocks: (B:13:0x002a, B:35:0x0088, B:26:0x004f, B:28:0x0057, B:29:0x005d, B:31:0x0063, B:32:0x0073, B:22:0x003f, B:25:0x004a, B:19:0x0038), top: B:40:0x0022 }] */
    /* JADX WARN: Code duplicated, block: B:7:0x0013  */
    /* JADX WARN: Unsupported multi-entry loop pattern (BACK_EDGE: B:33:0x0085 -> B:35:0x0088). Please report as a decompilation issue!!! */
    /*  JADX ERROR: JadxOverflowException in pass: RegionMakerVisitor
        jadx.core.utils.exceptions.JadxOverflowException: Regions stack size limit reached
        	at jadx.core.utils.ErrorsCounter.addError(ErrorsCounter.java:59)
        	at jadx.core.utils.ErrorsCounter.error(ErrorsCounter.java:31)
        	at jadx.core.dex.attributes.nodes.NotificationAttrNode.addError(NotificationAttrNode.java:19)
        */
    public final java.lang.Object processingLoop(kotlin.coroutines.Continuation r7) {
        /*
            r6 = this;
            boolean r0 = r7 instanceof androidx.camera.camera2.pipe.core.ProcessingQueue.AnonymousClass1
            if (r0 == 0) goto L13
            r0 = r7
            androidx.camera.camera2.pipe.core.ProcessingQueue$processingLoop$1 r0 = (androidx.camera.camera2.pipe.core.ProcessingQueue.AnonymousClass1) r0
            int r1 = r0.label
            r2 = -2147483648(0xffffffff80000000, float:-0.0)
            r3 = r1 & r2
            if (r3 == 0) goto L13
            int r1 = r1 - r2
            r0.label = r1
            goto L18
        L13:
            androidx.camera.camera2.pipe.core.ProcessingQueue$processingLoop$1 r0 = new androidx.camera.camera2.pipe.core.ProcessingQueue$processingLoop$1
            r0.<init>(r7)
        L18:
            java.lang.Object r7 = r0.result
            java.lang.Object r1 = kotlin.coroutines.intrinsics.IntrinsicsKt.getCOROUTINE_SUSPENDED()
            int r2 = r0.label
            r3 = 2
            r4 = 1
            if (r2 == 0) goto L3c
            if (r2 == r4) goto L38
            if (r2 != r3) goto L30
            int r2 = r0.I$0
            kotlin.ResultKt.throwOnFailure(r7)     // Catch: java.lang.Throwable -> L2e
            goto L88
        L2e:
            r7 = move-exception
            goto L91
        L30:
            java.lang.IllegalStateException r7 = new java.lang.IllegalStateException
            java.lang.String r0 = "call to 'resume' before 'invoke' with coroutine"
            r7.<init>(r0)
            throw r7
        L38:
            kotlin.ResultKt.throwOnFailure(r7)     // Catch: java.lang.Throwable -> L2e
            goto L4a
        L3c:
            kotlin.ResultKt.throwOnFailure(r7)
        L3f:
            kotlinx.coroutines.channels.Channel r7 = r6.channel     // Catch: java.lang.Throwable -> L2e
            r0.label = r4     // Catch: java.lang.Throwable -> L2e
            java.lang.Object r7 = r7.receive(r0)     // Catch: java.lang.Throwable -> L2e
            if (r7 != r1) goto L4a
            goto L87
        L4a:
            kotlin.collections.ArrayDeque r2 = r6.queue     // Catch: java.lang.Throwable -> L2e
            r2.add(r7)     // Catch: java.lang.Throwable -> L2e
        L4f:
            kotlin.collections.ArrayDeque r7 = r6.queue     // Catch: java.lang.Throwable -> L2e
            boolean r7 = r7.isEmpty()     // Catch: java.lang.Throwable -> L2e
            if (r7 != 0) goto L3f
            kotlinx.coroutines.channels.Channel r7 = r6.channel     // Catch: java.lang.Throwable -> L2e
            java.lang.Object r7 = r7.mo2502tryReceivePtdJZtk()     // Catch: java.lang.Throwable -> L2e
        L5d:
            boolean r2 = kotlinx.coroutines.channels.ChannelResult.m2514isSuccessimpl(r7)     // Catch: java.lang.Throwable -> L2e
            if (r2 == 0) goto L73
            kotlin.collections.ArrayDeque r2 = r6.queue     // Catch: java.lang.Throwable -> L2e
            java.lang.Object r7 = kotlinx.coroutines.channels.ChannelResult.m2511getOrThrowimpl(r7)     // Catch: java.lang.Throwable -> L2e
            r2.add(r7)     // Catch: java.lang.Throwable -> L2e
            kotlinx.coroutines.channels.Channel r7 = r6.channel     // Catch: java.lang.Throwable -> L2e
            java.lang.Object r7 = r7.mo2502tryReceivePtdJZtk()     // Catch: java.lang.Throwable -> L2e
            goto L5d
        L73:
            kotlin.collections.ArrayDeque r7 = r6.queue     // Catch: java.lang.Throwable -> L2e
            int r2 = r7.size()     // Catch: java.lang.Throwable -> L2e
            kotlin.jvm.functions.Function2 r7 = r6.process     // Catch: java.lang.Throwable -> L2e
            kotlin.collections.ArrayDeque r5 = r6.queue     // Catch: java.lang.Throwable -> L2e
            r0.I$0 = r2     // Catch: java.lang.Throwable -> L2e
            r0.label = r3     // Catch: java.lang.Throwable -> L2e
            java.lang.Object r7 = r7.invoke(r5, r0)     // Catch: java.lang.Throwable -> L2e
            if (r7 != r1) goto L88
        L87:
            return r1
        L88:
            kotlin.collections.ArrayDeque r7 = r6.queue     // Catch: java.lang.Throwable -> L2e
            int r7 = r7.size()     // Catch: java.lang.Throwable -> L2e
            if (r2 != r7) goto L4f
            goto L3f
        L91:
            r6.releaseUnprocessedElements(r7)
            throw r7
        */
        throw new UnsupportedOperationException("Method not decompiled: androidx.camera.camera2.pipe.core.ProcessingQueue.processingLoop(kotlin.coroutines.Continuation):java.lang.Object");
    }

    /* JADX INFO: Access modifiers changed from: private */
    public final void releaseUnprocessedElements(Throwable th) {
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

        public final ProcessingQueue processIn(ProcessingQueue processingQueue, CoroutineScope scope) {
            Intrinsics.checkNotNullParameter(processingQueue, "<this>");
            Intrinsics.checkNotNullParameter(scope, "scope");
            if (!processingQueue.started.compareAndSet(false, true)) {
                throw new IllegalStateException("ProcessingQueue cannot be re-started!");
            }
            if (BuildersKt__Builders_commonKt.launch$default(scope, null, null, new ProcessingQueue$Companion$processIn$job$1(processingQueue, null), 3, null).isCancelled()) {
                processingQueue.releaseUnprocessedElements(null);
            }
            return processingQueue;
        }
    }
}
