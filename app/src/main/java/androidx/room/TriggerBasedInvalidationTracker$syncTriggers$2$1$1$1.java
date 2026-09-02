package androidx.room;

import kotlin.Unit;
import kotlin.coroutines.Continuation;
import kotlin.coroutines.jvm.internal.SuspendLambda;
import kotlin.jvm.functions.Function2;

final class TriggerBasedInvalidationTracker$syncTriggers$2$1$1$1 extends SuspendLambda implements Function2 {
    final /* synthetic */ Transactor $connection;
    final /* synthetic */ ObservedTableStates.ObserveOp[] $tablesToSync;
    int I$0;
    int I$1;
    int I$2;
    Object L$0;
    Object L$1;
    Object L$2;
    int label;
    final /* synthetic */ TriggerBasedInvalidationTracker this$0;

    public static final /* synthetic */ class WhenMappings {
        public static final /* synthetic */ int[] $EnumSwitchMapping$0;

        static {
            int[] iArr = new int[ObservedTableStates.ObserveOp.values().length];
            try {
                iArr[ObservedTableStates.ObserveOp.NO_OP.ordinal()] = 1;
            } catch (NoSuchFieldError unused) {
            }
            try {
                iArr[ObservedTableStates.ObserveOp.ADD.ordinal()] = 2;
            } catch (NoSuchFieldError unused2) {
            }
            try {
                iArr[ObservedTableStates.ObserveOp.REMOVE.ordinal()] = 3;
            } catch (NoSuchFieldError unused3) {
            }
            $EnumSwitchMapping$0 = iArr;
        }
    }

    /* JADX WARN: 'super' call moved to the top of the method (can break code semantics) */
    TriggerBasedInvalidationTracker$syncTriggers$2$1$1$1(ObservedTableStates.ObserveOp[] observeOpArr, TriggerBasedInvalidationTracker triggerBasedInvalidationTracker, Transactor transactor, Continuation continuation) {
        super(2, continuation);
        this.$tablesToSync = observeOpArr;
        this.this$0 = triggerBasedInvalidationTracker;
        this.$connection = transactor;
    }

    @Override // kotlin.coroutines.jvm.internal.BaseContinuationImpl
    public final Continuation create(Object obj, Continuation continuation) {
        return new TriggerBasedInvalidationTracker$syncTriggers$2$1$1$1(this.$tablesToSync, this.this$0, this.$connection, continuation);
    }

    @Override // kotlin.jvm.functions.Function2
    public final Object invoke(TransactionScope transactionScope, Continuation continuation) {
        return ((TriggerBasedInvalidationTracker$syncTriggers$2$1$1$1) create(transactionScope, continuation)).invokeSuspend(Unit.INSTANCE);
    }

    /* JADX WARN: Code duplicated, block: B:11:0x003e  */
    /* JADX WARN: Code duplicated, block: B:26:0x0086  */
    /* JADX WARN: Unsupported multi-entry loop pattern (BACK_EDGE: B:26:0x0086 -> B:27:0x0087). Please report as a decompilation issue!!! */
    /*  JADX ERROR: JadxOverflowException in pass: RegionMakerVisitor
        jadx.core.utils.exceptions.JadxOverflowException: Regions stack size limit reached
        	at jadx.core.utils.ErrorsCounter.addError(ErrorsCounter.java:59)
        	at jadx.core.utils.ErrorsCounter.error(ErrorsCounter.java:31)
        	at jadx.core.dex.attributes.nodes.NotificationAttrNode.addError(NotificationAttrNode.java:19)
        */
    @Override // kotlin.coroutines.jvm.internal.BaseContinuationImpl
    public final java.lang.Object invokeSuspend(java.lang.Object r12) {
        /*
            r11 = this;
            java.lang.Object r0 = kotlin.coroutines.intrinsics.IntrinsicsKt.getCOROUTINE_SUSPENDED()
            int r1 = r11.label
            r2 = 2
            r3 = 1
            if (r1 == 0) goto L2c
            if (r1 == r3) goto Le
            if (r1 != r2) goto L24
        Le:
            int r1 = r11.I$2
            int r4 = r11.I$1
            int r5 = r11.I$0
            java.lang.Object r6 = r11.L$2
            androidx.room.Transactor r6 = (androidx.room.Transactor) r6
            java.lang.Object r7 = r11.L$1
            androidx.room.TriggerBasedInvalidationTracker r7 = (androidx.room.TriggerBasedInvalidationTracker) r7
            java.lang.Object r8 = r11.L$0
            androidx.room.ObservedTableStates$ObserveOp[] r8 = (androidx.room.ObservedTableStates.ObserveOp[]) r8
            kotlin.ResultKt.throwOnFailure(r12)
            goto L68
        L24:
            java.lang.IllegalStateException r12 = new java.lang.IllegalStateException
            java.lang.String r0 = "call to 'resume' before 'invoke' with coroutine"
            r12.<init>(r0)
            throw r12
        L2c:
            kotlin.ResultKt.throwOnFailure(r12)
            androidx.room.ObservedTableStates$ObserveOp[] r12 = r11.$tablesToSync
            androidx.room.TriggerBasedInvalidationTracker r1 = r11.this$0
            androidx.room.Transactor r4 = r11.$connection
            int r5 = r12.length
            r6 = 0
            r8 = r12
            r7 = r1
            r12 = r4
            r1 = r5
            r4 = r6
        L3c:
            if (r4 >= r1) goto L89
            r5 = r8[r4]
            int r9 = r6 + 1
            int[] r10 = androidx.room.TriggerBasedInvalidationTracker$syncTriggers$2$1$1$1.WhenMappings.$EnumSwitchMapping$0
            int r5 = r5.ordinal()
            r5 = r10[r5]
            if (r5 == r3) goto L86
            if (r5 == r2) goto L71
            r10 = 3
            if (r5 != r10) goto L6b
            r11.L$0 = r8
            r11.L$1 = r7
            r11.L$2 = r12
            r11.I$0 = r9
            r11.I$1 = r4
            r11.I$2 = r1
            r11.label = r2
            java.lang.Object r5 = androidx.room.TriggerBasedInvalidationTracker.access$stopTrackingTable(r7, r12, r6, r11)
            if (r5 != r0) goto L66
            goto L85
        L66:
            r6 = r12
            r5 = r9
        L68:
            r12 = r6
            r6 = r5
            goto L87
        L6b:
            kotlin.NoWhenBranchMatchedException r12 = new kotlin.NoWhenBranchMatchedException
            r12.<init>()
            throw r12
        L71:
            r11.L$0 = r8
            r11.L$1 = r7
            r11.L$2 = r12
            r11.I$0 = r9
            r11.I$1 = r4
            r11.I$2 = r1
            r11.label = r3
            java.lang.Object r5 = androidx.room.TriggerBasedInvalidationTracker.access$startTrackingTable(r7, r12, r6, r11)
            if (r5 != r0) goto L66
        L85:
            return r0
        L86:
            r6 = r9
        L87:
            int r4 = r4 + r3
            goto L3c
        L89:
            kotlin.Unit r12 = kotlin.Unit.INSTANCE
            return r12
        */
        throw new UnsupportedOperationException("Method not decompiled: androidx.room.TriggerBasedInvalidationTracker$syncTriggers$2$1$1$1.invokeSuspend(java.lang.Object):java.lang.Object");
    }
}
