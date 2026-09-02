package androidx.room.util;

import androidx.room.RoomDatabase;
import androidx.room.RoomDatabaseKt;
import androidx.room.TransactionElement;
import androidx.room.coroutines.RunBlockingUninterruptible_androidKt;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;
import kotlin.ResultKt;
import kotlin.Unit;
import kotlin.coroutines.Continuation;
import kotlin.coroutines.CoroutineContext;
import kotlin.coroutines.EmptyCoroutineContext;
import kotlin.coroutines.intrinsics.IntrinsicsKt;
import kotlin.coroutines.jvm.internal.ContinuationImpl;
import kotlin.coroutines.jvm.internal.SuspendLambda;
import kotlin.io.CloseableKt;
import kotlin.jvm.functions.Function1;
import kotlin.jvm.functions.Function2;
import kotlin.jvm.internal.Intrinsics;
import kotlinx.coroutines.BuildersKt;
import kotlinx.coroutines.CoroutineScope;

abstract /* synthetic */ class DBUtil__DBUtil_androidKt {

    /* JADX INFO: renamed from: androidx.room.util.DBUtil__DBUtil_androidKt$performInTransactionSuspending$1, reason: invalid class name and case insensitive filesystem */
    static final class C01291 extends ContinuationImpl {
        Object L$0;
        Object L$1;
        int label;
        /* synthetic */ Object result;

        C01291(Continuation continuation) {
            super(continuation);
        }

        @Override // kotlin.coroutines.jvm.internal.BaseContinuationImpl
        public final Object invokeSuspend(Object obj) {
            this.result = obj;
            this.label |= Integer.MIN_VALUE;
            return DBUtil.performInTransactionSuspending(null, null, this);
        }
    }

    /* JADX INFO: renamed from: androidx.room.util.DBUtil__DBUtil_androidKt$performSuspending$1, reason: invalid class name and case insensitive filesystem */
    static final class C01301 extends ContinuationImpl {
        Object L$0;
        Object L$1;
        boolean Z$0;
        boolean Z$1;
        int label;
        /* synthetic */ Object result;

        C01301(Continuation continuation) {
            super(continuation);
        }

        @Override // kotlin.coroutines.jvm.internal.BaseContinuationImpl
        public final Object invokeSuspend(Object obj) {
            this.result = obj;
            this.label |= Integer.MIN_VALUE;
            return DBUtil.performSuspending(null, false, false, null, this);
        }
    }

    /* JADX WARN: Code duplicated, block: B:8:0x0016  */
    public static final Object performSuspending(RoomDatabase roomDatabase, boolean z, boolean z2, Function1 function1, Continuation continuation) {
        C01301 c01301;
        RoomDatabase roomDatabase2;
        boolean z3;
        Function1 function2;
        if (continuation instanceof C01301) {
            c01301 = (C01301) continuation;
            int i = c01301.label;
            if ((i & Integer.MIN_VALUE) != 0) {
                c01301.label = i - Integer.MIN_VALUE;
            } else {
                c01301 = new C01301(continuation);
            }
        } else {
            c01301 = new C01301(continuation);
        }
        C01301 c01302 = c01301;
        Object obj = c01302.result;
        Object coroutine_suspended = IntrinsicsKt.getCOROUTINE_SUSPENDED();
        int i2 = c01302.label;
        if (i2 == 0) {
            ResultKt.throwOnFailure(obj);
            if (roomDatabase.inCompatibilityMode() && roomDatabase.isOpenInternal$room_runtime() && roomDatabase.inTransaction()) {
                DBUtil__DBUtil_androidKt$performSuspending$lambda$1$$inlined$internalPerform$1 dBUtil__DBUtil_androidKt$performSuspending$lambda$1$$inlined$internalPerform$1 = new DBUtil__DBUtil_androidKt$performSuspending$lambda$1$$inlined$internalPerform$1(z2, z, roomDatabase, null, function1);
                c01302.label = 1;
                Object objUseConnection = roomDatabase.useConnection(z, dBUtil__DBUtil_androidKt$performSuspending$lambda$1$$inlined$internalPerform$1, c01302);
                if (objUseConnection != coroutine_suspended) {
                    return objUseConnection;
                }
            } else {
                c01302.L$0 = roomDatabase;
                c01302.L$1 = function1;
                c01302.Z$0 = z;
                c01302.Z$1 = z2;
                c01302.label = 2;
                Object coroutineContext = DBUtil.getCoroutineContext(roomDatabase, z2, c01302);
                if (coroutineContext != coroutine_suspended) {
                    roomDatabase2 = roomDatabase;
                    obj = coroutineContext;
                    z3 = z2;
                    function2 = function1;
                }
            }
        }
        if (i2 == 1) {
            ResultKt.throwOnFailure(obj);
            return obj;
        }
        if (i2 != 2) {
            if (i2 != 3) {
                throw new IllegalStateException("call to 'resume' before 'invoke' with coroutine");
            }
            ResultKt.throwOnFailure(obj);
            return obj;
        }
        boolean z4 = c01302.Z$1;
        z = c01302.Z$0;
        Function1 function3 = (Function1) c01302.L$1;
        RoomDatabase roomDatabase3 = (RoomDatabase) c01302.L$0;
        ResultKt.throwOnFailure(obj);
        z3 = z4;
        function2 = function3;
        roomDatabase2 = roomDatabase3;
        DBUtil__DBUtil_androidKt$performSuspending$$inlined$compatCoroutineExecute$DBUtil__DBUtil_androidKt$1 dBUtil__DBUtil_androidKt$performSuspending$$inlined$compatCoroutineExecute$DBUtil__DBUtil_androidKt$1 = new DBUtil__DBUtil_androidKt$performSuspending$$inlined$compatCoroutineExecute$DBUtil__DBUtil_androidKt$1(null, roomDatabase2, z, z3, function2);
        c01302.L$0 = null;
        c01302.L$1 = null;
        c01302.label = 3;
        Object objWithContext = BuildersKt.withContext((CoroutineContext) obj, dBUtil__DBUtil_androidKt$performSuspending$$inlined$compatCoroutineExecute$DBUtil__DBUtil_androidKt$1, c01302);
        return objWithContext == coroutine_suspended ? coroutine_suspended : objWithContext;
    }

    public static final Object performBlocking(RoomDatabase db, boolean z, boolean z2, Function1 block) {
        Intrinsics.checkNotNullParameter(db, "db");
        Intrinsics.checkNotNullParameter(block, "block");
        db.assertNotMainThread();
        db.assertNotSuspendingTransaction();
        CoroutineContext coroutineContext = db.getSuspendingTransactionContext().get();
        if (coroutineContext == null) {
            coroutineContext = EmptyCoroutineContext.INSTANCE;
        }
        return RunBlockingUninterruptible_androidKt.runBlockingUninterruptible(new AnonymousClass1(coroutineContext, db, z2, z, block, null));
    }

    /* JADX INFO: renamed from: androidx.room.util.DBUtil__DBUtil_androidKt$performBlocking$1, reason: invalid class name */
    static final class AnonymousClass1 extends SuspendLambda implements Function2 {
        final /* synthetic */ Function1 $block;
        final /* synthetic */ CoroutineContext $context;
        final /* synthetic */ RoomDatabase $db;
        final /* synthetic */ boolean $inTransaction;
        final /* synthetic */ boolean $isReadOnly;
        int label;

        /* JADX WARN: 'super' call moved to the top of the method (can break code semantics) */
        AnonymousClass1(CoroutineContext coroutineContext, RoomDatabase roomDatabase, boolean z, boolean z2, Function1 function1, Continuation continuation) {
            super(2, continuation);
            this.$context = coroutineContext;
            this.$db = roomDatabase;
            this.$inTransaction = z;
            this.$isReadOnly = z2;
            this.$block = function1;
        }

        @Override // kotlin.coroutines.jvm.internal.BaseContinuationImpl
        public final Continuation create(Object obj, Continuation continuation) {
            return new AnonymousClass1(this.$context, this.$db, this.$inTransaction, this.$isReadOnly, this.$block, continuation);
        }

        @Override // kotlin.jvm.functions.Function2
        public final Object invoke(CoroutineScope coroutineScope, Continuation continuation) {
            return ((AnonymousClass1) create(coroutineScope, continuation)).invokeSuspend(Unit.INSTANCE);
        }

        /* JADX INFO: renamed from: androidx.room.util.DBUtil__DBUtil_androidKt$performBlocking$1$1, reason: invalid class name and collision with other inner class name */
        static final class C00111 extends SuspendLambda implements Function2 {
            final /* synthetic */ Function1 $block;
            final /* synthetic */ RoomDatabase $db;
            final /* synthetic */ boolean $inTransaction;
            final /* synthetic */ boolean $isReadOnly;
            int label;

            /* JADX WARN: 'super' call moved to the top of the method (can break code semantics) */
            C00111(RoomDatabase roomDatabase, boolean z, boolean z2, Function1 function1, Continuation continuation) {
                super(2, continuation);
                this.$db = roomDatabase;
                this.$inTransaction = z;
                this.$isReadOnly = z2;
                this.$block = function1;
            }

            @Override // kotlin.coroutines.jvm.internal.BaseContinuationImpl
            public final Continuation create(Object obj, Continuation continuation) {
                return new C00111(this.$db, this.$inTransaction, this.$isReadOnly, this.$block, continuation);
            }

            @Override // kotlin.jvm.functions.Function2
            public final Object invoke(CoroutineScope coroutineScope, Continuation continuation) {
                return ((C00111) create(coroutineScope, continuation)).invokeSuspend(Unit.INSTANCE);
            }

            @Override // kotlin.coroutines.jvm.internal.BaseContinuationImpl
            public final Object invokeSuspend(Object obj) {
                Object coroutine_suspended = IntrinsicsKt.getCOROUTINE_SUSPENDED();
                int i = this.label;
                if (i != 0) {
                    if (i != 1) {
                        throw new IllegalStateException("call to 'resume' before 'invoke' with coroutine");
                    }
                    ResultKt.throwOnFailure(obj);
                    return obj;
                }
                ResultKt.throwOnFailure(obj);
                boolean z = !(this.$db.inCompatibilityMode() && this.$db.inTransaction()) && this.$inTransaction;
                RoomDatabase roomDatabase = this.$db;
                boolean z2 = this.$isReadOnly;
                DBUtil__DBUtil_androidKt$performBlocking$1$1$invokeSuspend$$inlined$internalPerform$1 dBUtil__DBUtil_androidKt$performBlocking$1$1$invokeSuspend$$inlined$internalPerform$1 = new DBUtil__DBUtil_androidKt$performBlocking$1$1$invokeSuspend$$inlined$internalPerform$1(z, z2, roomDatabase, null, this.$block);
                this.label = 1;
                Object objUseConnection = roomDatabase.useConnection(z2, dBUtil__DBUtil_androidKt$performBlocking$1$1$invokeSuspend$$inlined$internalPerform$1, this);
                return objUseConnection == coroutine_suspended ? coroutine_suspended : objUseConnection;
            }
        }

        @Override // kotlin.coroutines.jvm.internal.BaseContinuationImpl
        public final Object invokeSuspend(Object obj) {
            Object coroutine_suspended = IntrinsicsKt.getCOROUTINE_SUSPENDED();
            int i = this.label;
            if (i != 0) {
                if (i != 1) {
                    throw new IllegalStateException("call to 'resume' before 'invoke' with coroutine");
                }
                ResultKt.throwOnFailure(obj);
                return obj;
            }
            ResultKt.throwOnFailure(obj);
            CoroutineContext coroutineContext = this.$context;
            C00111 c00111 = new C00111(this.$db, this.$inTransaction, this.$isReadOnly, this.$block, null);
            this.label = 1;
            Object objWithContext = BuildersKt.withContext(coroutineContext, c00111, this);
            return objWithContext == coroutine_suspended ? coroutine_suspended : objWithContext;
        }
    }

    /* JADX WARN: Code duplicated, block: B:8:0x0014  */
    public static final Object performInTransactionSuspending(RoomDatabase roomDatabase, Function1 function1, Continuation continuation) {
        C01291 c01291;
        RoomDatabase roomDatabase2;
        Function1 function2;
        if (continuation instanceof C01291) {
            c01291 = (C01291) continuation;
            int i = c01291.label;
            if ((i & Integer.MIN_VALUE) != 0) {
                c01291.label = i - Integer.MIN_VALUE;
            } else {
                c01291 = new C01291(continuation);
            }
        } else {
            c01291 = new C01291(continuation);
        }
        C01291 c01292 = c01291;
        Object coroutineContext = c01292.result;
        Object coroutine_suspended = IntrinsicsKt.getCOROUTINE_SUSPENDED();
        int i2 = c01292.label;
        if (i2 == 0) {
            ResultKt.throwOnFailure(coroutineContext);
            if (roomDatabase.inCompatibilityMode()) {
                AnonymousClass2 anonymousClass2 = new AnonymousClass2(roomDatabase, function1, null);
                c01292.label = 1;
                Object objWithTransactionContext = RoomDatabaseKt.withTransactionContext(roomDatabase, anonymousClass2, c01292);
                if (objWithTransactionContext != coroutine_suspended) {
                    return objWithTransactionContext;
                }
            } else if (roomDatabase.inCompatibilityMode() && roomDatabase.isOpenInternal$room_runtime() && roomDatabase.inTransaction()) {
                DBUtil__DBUtil_androidKt$performInTransactionSuspending$lambda$3$$inlined$internalPerform$1 dBUtil__DBUtil_androidKt$performInTransactionSuspending$lambda$3$$inlined$internalPerform$1 = new DBUtil__DBUtil_androidKt$performInTransactionSuspending$lambda$3$$inlined$internalPerform$1(true, false, roomDatabase, null, function1);
                c01292.label = 2;
                Object objUseConnection = roomDatabase.useConnection(false, dBUtil__DBUtil_androidKt$performInTransactionSuspending$lambda$3$$inlined$internalPerform$1, c01292);
                if (objUseConnection != coroutine_suspended) {
                    return objUseConnection;
                }
            } else {
                c01292.L$0 = roomDatabase;
                c01292.L$1 = function1;
                c01292.label = 3;
                coroutineContext = DBUtil.getCoroutineContext(roomDatabase, true, c01292);
                if (coroutineContext != coroutine_suspended) {
                    roomDatabase2 = roomDatabase;
                    function2 = function1;
                }
            }
        }
        if (i2 == 1) {
            ResultKt.throwOnFailure(coroutineContext);
            return coroutineContext;
        }
        if (i2 == 2) {
            ResultKt.throwOnFailure(coroutineContext);
            return coroutineContext;
        }
        if (i2 != 3) {
            if (i2 != 4) {
                throw new IllegalStateException("call to 'resume' before 'invoke' with coroutine");
            }
            ResultKt.throwOnFailure(coroutineContext);
            return coroutineContext;
        }
        function2 = (Function1) c01292.L$1;
        roomDatabase2 = (RoomDatabase) c01292.L$0;
        ResultKt.throwOnFailure(coroutineContext);
        DBUtil__DBUtil_androidKt$performInTransactionSuspending$$inlined$compatCoroutineExecute$DBUtil__DBUtil_androidKt$1 dBUtil__DBUtil_androidKt$performInTransactionSuspending$$inlined$compatCoroutineExecute$DBUtil__DBUtil_androidKt$1 = new DBUtil__DBUtil_androidKt$performInTransactionSuspending$$inlined$compatCoroutineExecute$DBUtil__DBUtil_androidKt$1(null, roomDatabase2, function2);
        c01292.L$0 = null;
        c01292.L$1 = null;
        c01292.label = 4;
        Object objWithContext = BuildersKt.withContext((CoroutineContext) coroutineContext, dBUtil__DBUtil_androidKt$performInTransactionSuspending$$inlined$compatCoroutineExecute$DBUtil__DBUtil_androidKt$1, c01292);
        return objWithContext == coroutine_suspended ? coroutine_suspended : objWithContext;
    }

    /* JADX INFO: renamed from: androidx.room.util.DBUtil__DBUtil_androidKt$performInTransactionSuspending$2, reason: invalid class name */
    static final class AnonymousClass2 extends SuspendLambda implements Function1 {
        final /* synthetic */ Function1 $block;
        final /* synthetic */ RoomDatabase $db;
        int label;

        /* JADX WARN: 'super' call moved to the top of the method (can break code semantics) */
        AnonymousClass2(RoomDatabase roomDatabase, Function1 function1, Continuation continuation) {
            super(1, continuation);
            this.$db = roomDatabase;
            this.$block = function1;
        }

        @Override // kotlin.coroutines.jvm.internal.BaseContinuationImpl
        public final Continuation create(Continuation continuation) {
            return new AnonymousClass2(this.$db, this.$block, continuation);
        }

        @Override // kotlin.jvm.functions.Function1
        public final Object invoke(Continuation continuation) {
            return ((AnonymousClass2) create(continuation)).invokeSuspend(Unit.INSTANCE);
        }

        @Override // kotlin.coroutines.jvm.internal.BaseContinuationImpl
        public final Object invokeSuspend(Object obj) {
            Object coroutine_suspended = IntrinsicsKt.getCOROUTINE_SUSPENDED();
            int i = this.label;
            if (i != 0) {
                if (i != 1) {
                    throw new IllegalStateException("call to 'resume' before 'invoke' with coroutine");
                }
                ResultKt.throwOnFailure(obj);
                return obj;
            }
            ResultKt.throwOnFailure(obj);
            RoomDatabase roomDatabase = this.$db;
            DBUtil__DBUtil_androidKt$performInTransactionSuspending$2$invokeSuspend$$inlined$internalPerform$1 dBUtil__DBUtil_androidKt$performInTransactionSuspending$2$invokeSuspend$$inlined$internalPerform$1 = new DBUtil__DBUtil_androidKt$performInTransactionSuspending$2$invokeSuspend$$inlined$internalPerform$1(true, false, roomDatabase, null, this.$block);
            this.label = 1;
            Object objUseConnection = roomDatabase.useConnection(false, dBUtil__DBUtil_androidKt$performInTransactionSuspending$2$invokeSuspend$$inlined$internalPerform$1, this);
            return objUseConnection == coroutine_suspended ? coroutine_suspended : objUseConnection;
        }
    }

    public static final Object getCoroutineContext(RoomDatabase roomDatabase, boolean z, Continuation continuation) {
        TransactionElement transactionElement = (TransactionElement) continuation.getContext().get(TransactionElement.Key);
        CoroutineContext transactionDispatcher$room_runtime = transactionElement != null ? transactionElement.getTransactionDispatcher$room_runtime() : null;
        if (!roomDatabase.inCompatibilityMode()) {
            CoroutineContext queryContext = roomDatabase.getQueryContext();
            if (transactionDispatcher$room_runtime == null) {
                transactionDispatcher$room_runtime = EmptyCoroutineContext.INSTANCE;
            }
            return queryContext.plus(transactionDispatcher$room_runtime);
        }
        if (transactionDispatcher$room_runtime != null) {
            return roomDatabase.getQueryContext().plus(transactionDispatcher$room_runtime);
        }
        if (z) {
            return roomDatabase.getTransactionContext$room_runtime();
        }
        return roomDatabase.getQueryContext();
    }

    public static final int readVersion(File databaseFile) {
        Intrinsics.checkNotNullParameter(databaseFile, "databaseFile");
        FileChannel channel = new FileInputStream(databaseFile).getChannel();
        try {
            ByteBuffer byteBufferAllocate = ByteBuffer.allocate(4);
            channel.tryLock(60L, 4L, true);
            channel.position(60L);
            if (channel.read(byteBufferAllocate) != 4) {
                throw new IOException("Bad database header, unable to read 4 bytes at offset 60");
            }
            byteBufferAllocate.rewind();
            int i = byteBufferAllocate.getInt();
            CloseableKt.closeFinally(channel, null);
            return i;
        } catch (Throwable th) {
            try {
                throw th;
            } catch (Throwable th2) {
                CloseableKt.closeFinally(channel, th);
                throw th2;
            }
        }
    }
}
