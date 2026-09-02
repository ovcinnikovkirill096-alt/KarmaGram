package androidx.room.coroutines;

import android.database.SQLException;
import androidx.room.concurrent.ThreadLocal_jvmAndroidKt;
import androidx.sqlite.SQLite;
import androidx.sqlite.SQLiteConnection;
import androidx.sqlite.SQLiteDriver;
import kotlin.ExceptionsKt;
import kotlin.KotlinNothingValueException;
import kotlin.ResultKt;
import kotlin.Unit;
import kotlin.coroutines.Continuation;
import kotlin.coroutines.CoroutineContext;
import kotlin.coroutines.intrinsics.IntrinsicsKt;
import kotlin.coroutines.jvm.internal.ContinuationImpl;
import kotlin.coroutines.jvm.internal.SuspendLambda;
import kotlin.jvm.functions.Function0;
import kotlin.jvm.functions.Function2;
import kotlin.jvm.internal.Intrinsics;
import kotlin.jvm.internal.Ref$ObjectRef;
import kotlin.time.Duration;
import kotlin.time.DurationKt;
import kotlin.time.DurationUnit;
import kotlinx.coroutines.BuildersKt;
import kotlinx.coroutines.CoroutineScope;

public final class ConnectionPoolImpl implements ConnectionPool {
    private final ConnectionElementKey connectionElementKey;
    private final ThreadLocal connectionThreadLocal;
    private final SQLiteDriver driver;
    private volatile boolean isClosed;
    private int onTimeout;
    private final Pool readers;
    private long timeout;
    private final Pool writers;

    /* JADX INFO: renamed from: androidx.room.coroutines.ConnectionPoolImpl$useConnection$1, reason: invalid class name */
    static final class AnonymousClass1 extends ContinuationImpl {
        Object L$0;
        Object L$1;
        Object L$2;
        Object L$3;
        Object L$4;
        Object L$5;
        boolean Z$0;
        int label;
        /* synthetic */ Object result;

        AnonymousClass1(Continuation continuation) {
            super(continuation);
        }

        @Override // kotlin.coroutines.jvm.internal.BaseContinuationImpl
        public final Object invokeSuspend(Object obj) {
            this.result = obj;
            this.label |= Integer.MIN_VALUE;
            return ConnectionPoolImpl.this.useConnection(false, null, this);
        }
    }

    public ConnectionPoolImpl(final SQLiteDriver driver, final String fileName, int i) {
        Intrinsics.checkNotNullParameter(driver, "driver");
        Intrinsics.checkNotNullParameter(fileName, "fileName");
        this.connectionElementKey = new ConnectionElementKey();
        this.connectionThreadLocal = new ThreadLocal();
        Duration.Companion companion = Duration.Companion;
        this.timeout = DurationKt.toDuration(30, DurationUnit.SECONDS);
        this.onTimeout = 2;
        this.driver = driver;
        Pool pool = new Pool(1, new Function0() { // from class: androidx.room.coroutines.ConnectionPoolImpl$$ExternalSyntheticLambda3
            @Override // kotlin.jvm.functions.Function0
            public final Object invoke() {
                return driver.open(fileName);
            }
        }, i);
        this.readers = pool;
        this.writers = pool;
    }

    public ConnectionPoolImpl(final SQLiteDriver driver, final String fileName, int i, int i2, int i3) {
        Intrinsics.checkNotNullParameter(driver, "driver");
        Intrinsics.checkNotNullParameter(fileName, "fileName");
        this.connectionElementKey = new ConnectionElementKey();
        this.connectionThreadLocal = new ThreadLocal();
        Duration.Companion companion = Duration.Companion;
        this.timeout = DurationKt.toDuration(30, DurationUnit.SECONDS);
        this.onTimeout = 2;
        if (i <= 0) {
            throw new IllegalArgumentException("Maximum number of readers must be greater than 0");
        }
        if (i2 <= 0) {
            throw new IllegalArgumentException("Maximum number of writers must be greater than 0");
        }
        this.driver = driver;
        this.readers = new Pool(i, new Function0() { // from class: androidx.room.coroutines.ConnectionPoolImpl$$ExternalSyntheticLambda0
            @Override // kotlin.jvm.functions.Function0
            public final Object invoke() {
                return ConnectionPoolImpl._init_$lambda$4(driver, fileName);
            }
        }, i3);
        this.writers = new Pool(i2, new Function0() { // from class: androidx.room.coroutines.ConnectionPoolImpl$$ExternalSyntheticLambda1
            @Override // kotlin.jvm.functions.Function0
            public final Object invoke() {
                return driver.open(fileName);
            }
        }, i3);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static final SQLiteConnection _init_$lambda$4(SQLiteDriver sQLiteDriver, String str) throws Exception {
        SQLiteConnection sQLiteConnectionOpen = sQLiteDriver.open(str);
        SQLite.execSQL(sQLiteConnectionOpen, "PRAGMA query_only = 1");
        return sQLiteConnectionOpen;
    }

    /* JADX WARN: Code duplicated, block: B:68:0x0130  */
    /* JADX WARN: Code duplicated, block: B:71:0x013c A[Catch: all -> 0x0176, TRY_LEAVE, TryCatch #2 {all -> 0x0176, blocks: (B:64:0x0121, B:69:0x0131, B:71:0x013c, B:81:0x017a, B:82:0x0181), top: B:101:0x0121 }] */
    /* JADX WARN: Code duplicated, block: B:74:0x015c  */
    /* JADX WARN: Code duplicated, block: B:77:0x0164  */
    /* JADX WARN: Code duplicated, block: B:7:0x001b  */
    /* JADX WARN: Code duplicated, block: B:81:0x017a A[Catch: all -> 0x0176, TRY_ENTER, TryCatch #2 {all -> 0x0176, blocks: (B:64:0x0121, B:69:0x0131, B:71:0x013c, B:81:0x017a, B:82:0x0181), top: B:101:0x0121 }] */
    @Override // androidx.room.coroutines.ConnectionPool
    public Object useConnection(boolean z, Function2 function2, Continuation continuation) throws Exception {
        AnonymousClass1 anonymousClass1;
        Pool pool;
        Ref$ObjectRef ref$ObjectRef;
        Throwable th;
        Pool pool2;
        CoroutineContext context;
        Function2 function3;
        ConnectionElementKey connectionElementKey;
        Pool pool3;
        Ref$ObjectRef ref$ObjectRef2;
        Object obj;
        Ref$ObjectRef ref$ObjectRef3;
        PooledConnectionImpl pooledConnectionImpl;
        final boolean z2 = z;
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
        Object objWithContext = anonymousClass1.result;
        Object coroutine_suspended = IntrinsicsKt.getCOROUTINE_SUSPENDED();
        int i2 = anonymousClass1.label;
        boolean z3 = true;
        if (i2 != 0) {
            if (i2 == 1) {
                ResultKt.throwOnFailure(objWithContext);
                return objWithContext;
            }
            if (i2 == 2) {
                ResultKt.throwOnFailure(objWithContext);
                return objWithContext;
            }
            if (i2 == 3) {
                z2 = anonymousClass1.Z$0;
                connectionElementKey = (ConnectionElementKey) anonymousClass1.L$5;
                Ref$ObjectRef ref$ObjectRef4 = (Ref$ObjectRef) anonymousClass1.L$4;
                CoroutineContext coroutineContext = (CoroutineContext) anonymousClass1.L$3;
                Ref$ObjectRef ref$ObjectRef5 = (Ref$ObjectRef) anonymousClass1.L$2;
                pool3 = (Pool) anonymousClass1.L$1;
                function3 = (Function2) anonymousClass1.L$0;
                try {
                    ResultKt.throwOnFailure(objWithContext);
                    ref$ObjectRef2 = ref$ObjectRef4;
                    ref$ObjectRef = ref$ObjectRef5;
                    context = coroutineContext;
                    try {
                        ConnectionWithLock connectionWithLockMarkAcquired = ((ConnectionWithLock) objWithContext).markAcquired(context);
                        if (this.readers != this.writers || !z2) {
                            z3 = false;
                        }
                        ref$ObjectRef2.element = new PooledConnectionImpl(connectionElementKey, connectionWithLockMarkAcquired, z3);
                        obj = ref$ObjectRef.element;
                        if (obj != null) {
                            throw new IllegalArgumentException("Required value was null.");
                        }
                        CoroutineContext coroutineContextCreateConnectionContext = createConnectionContext((PooledConnectionImpl) obj);
                        AnonymousClass4 anonymousClass4 = new AnonymousClass4(function3, ref$ObjectRef, null);
                        anonymousClass1.L$0 = pool3;
                        anonymousClass1.L$1 = ref$ObjectRef;
                        anonymousClass1.L$2 = null;
                        anonymousClass1.L$3 = null;
                        anonymousClass1.L$4 = null;
                        anonymousClass1.L$5 = null;
                        anonymousClass1.label = 4;
                        objWithContext = BuildersKt.withContext(coroutineContextCreateConnectionContext, anonymousClass4, anonymousClass1);
                        if (objWithContext != coroutine_suspended) {
                            ref$ObjectRef3 = ref$ObjectRef;
                            pool2 = pool3;
                        }
                        return coroutine_suspended;
                    } catch (Throwable th2) {
                        th = th2;
                        pool2 = pool3;
                        throw th;
                    }
                } catch (Throwable th3) {
                    th = th3;
                    ref$ObjectRef = ref$ObjectRef5;
                    pool2 = pool3;
                    throw th;
                }
            }
            if (i2 != 4) {
                throw new IllegalStateException("call to 'resume' before 'invoke' with coroutine");
            }
            ref$ObjectRef3 = (Ref$ObjectRef) anonymousClass1.L$1;
            pool2 = (Pool) anonymousClass1.L$0;
            try {
                ResultKt.throwOnFailure(objWithContext);
            } catch (Throwable th4) {
                ref$ObjectRef = ref$ObjectRef3;
                th = th4;
            }
            pooledConnectionImpl = (PooledConnectionImpl) ref$ObjectRef3.element;
            if (pooledConnectionImpl != null) {
                pooledConnectionImpl.markRecycled();
                pooledConnectionImpl.getDelegate().markReleased();
                pool2.recycle(pooledConnectionImpl.getDelegate());
            }
            return objWithContext;
        }
        ResultKt.throwOnFailure(objWithContext);
        if (this.isClosed) {
            SQLite.throwSQLiteException(21, "Connection pool is closed");
            throw new KotlinNothingValueException();
        }
        PooledConnectionImpl connectionWrapper = (PooledConnectionImpl) this.connectionThreadLocal.get();
        if (connectionWrapper == null) {
            ConnectionElement connectionElement = (ConnectionElement) anonymousClass1.getContext().get(this.connectionElementKey);
            connectionWrapper = connectionElement != null ? connectionElement.getConnectionWrapper() : null;
        }
        if (connectionWrapper != null) {
            if (!z2 && connectionWrapper.isReadOnly()) {
                SQLite.throwSQLiteException(1, "Cannot upgrade connection from reader to writer");
                throw new KotlinNothingValueException();
            }
            if (anonymousClass1.getContext().get(this.connectionElementKey) == null) {
                CoroutineContext coroutineContextCreateConnectionContext2 = createConnectionContext(connectionWrapper);
                AnonymousClass2 anonymousClass2 = new AnonymousClass2(function2, connectionWrapper, null);
                anonymousClass1.label = 1;
                Object objWithContext2 = BuildersKt.withContext(coroutineContextCreateConnectionContext2, anonymousClass2, anonymousClass1);
                if (objWithContext2 != coroutine_suspended) {
                    return objWithContext2;
                }
            } else {
                anonymousClass1.label = 2;
                Object objInvoke = function2.invoke(connectionWrapper, anonymousClass1);
                if (objInvoke != coroutine_suspended) {
                    return objInvoke;
                }
            }
        } else {
            if (z2) {
                pool = this.readers;
            } else {
                pool = this.writers;
            }
            ref$ObjectRef = new Ref$ObjectRef();
            try {
                context = anonymousClass1.getContext();
                ConnectionElementKey connectionElementKey2 = this.connectionElementKey;
                long j = this.timeout;
                Function0 function0 = new Function0() { // from class: androidx.room.coroutines.ConnectionPoolImpl$$ExternalSyntheticLambda2
                    @Override // kotlin.jvm.functions.Function0
                    public final Object invoke() {
                        return ConnectionPoolImpl.useConnection$lambda$6(this.f$0, z2);
                    }
                };
                anonymousClass1.L$0 = function2;
                anonymousClass1.L$1 = pool;
                anonymousClass1.L$2 = ref$ObjectRef;
                anonymousClass1.L$3 = context;
                anonymousClass1.L$4 = ref$ObjectRef;
                anonymousClass1.L$5 = connectionElementKey2;
                anonymousClass1.Z$0 = z2;
                anonymousClass1.label = 3;
                Object objM795acquireWithTimeoutKLykuaI = pool.m795acquireWithTimeoutKLykuaI(j, function0, anonymousClass1);
                if (objM795acquireWithTimeoutKLykuaI != coroutine_suspended) {
                    function3 = function2;
                    connectionElementKey = connectionElementKey2;
                    pool3 = pool;
                    objWithContext = objM795acquireWithTimeoutKLykuaI;
                    ref$ObjectRef2 = ref$ObjectRef;
                    ConnectionWithLock connectionWithLockMarkAcquired2 = ((ConnectionWithLock) objWithContext).markAcquired(context);
                    if (this.readers != this.writers) {
                        z3 = false;
                    } else {
                        z3 = false;
                    }
                    ref$ObjectRef2.element = new PooledConnectionImpl(connectionElementKey, connectionWithLockMarkAcquired2, z3);
                    obj = ref$ObjectRef.element;
                    if (obj != null) {
                        throw new IllegalArgumentException("Required value was null.");
                    }
                    CoroutineContext coroutineContextCreateConnectionContext3 = createConnectionContext((PooledConnectionImpl) obj);
                    AnonymousClass4 anonymousClass5 = new AnonymousClass4(function3, ref$ObjectRef, null);
                    anonymousClass1.L$0 = pool3;
                    anonymousClass1.L$1 = ref$ObjectRef;
                    anonymousClass1.L$2 = null;
                    anonymousClass1.L$3 = null;
                    anonymousClass1.L$4 = null;
                    anonymousClass1.L$5 = null;
                    anonymousClass1.label = 4;
                    objWithContext = BuildersKt.withContext(coroutineContextCreateConnectionContext3, anonymousClass5, anonymousClass1);
                    if (objWithContext != coroutine_suspended) {
                        ref$ObjectRef3 = ref$ObjectRef;
                        pool2 = pool3;
                        pooledConnectionImpl = (PooledConnectionImpl) ref$ObjectRef3.element;
                        if (pooledConnectionImpl != null) {
                            pooledConnectionImpl.markRecycled();
                            pooledConnectionImpl.getDelegate().markReleased();
                            pool2.recycle(pooledConnectionImpl.getDelegate());
                        }
                        return objWithContext;
                    }
                }
            } catch (Throwable th5) {
                th = th5;
                pool2 = pool;
            }
        }
        return coroutine_suspended;
        try {
            throw th;
        } catch (Throwable th6) {
            try {
                PooledConnectionImpl pooledConnectionImpl2 = (PooledConnectionImpl) ref$ObjectRef.element;
                if (pooledConnectionImpl2 == null) {
                    throw th6;
                }
                pooledConnectionImpl2.markRecycled();
                pooledConnectionImpl2.getDelegate().markReleased();
                pool2.recycle(pooledConnectionImpl2.getDelegate());
                throw th6;
            } catch (Throwable th7) {
                ExceptionsKt.addSuppressed(th, th7);
                throw th6;
            }
        }
    }

    /* JADX INFO: renamed from: androidx.room.coroutines.ConnectionPoolImpl$useConnection$2, reason: invalid class name */
    static final class AnonymousClass2 extends SuspendLambda implements Function2 {
        final /* synthetic */ Function2 $block;
        final /* synthetic */ PooledConnectionImpl $confinedConnection;
        int label;

        /* JADX WARN: 'super' call moved to the top of the method (can break code semantics) */
        AnonymousClass2(Function2 function2, PooledConnectionImpl pooledConnectionImpl, Continuation continuation) {
            super(2, continuation);
            this.$block = function2;
            this.$confinedConnection = pooledConnectionImpl;
        }

        @Override // kotlin.coroutines.jvm.internal.BaseContinuationImpl
        public final Continuation create(Object obj, Continuation continuation) {
            return new AnonymousClass2(this.$block, this.$confinedConnection, continuation);
        }

        @Override // kotlin.jvm.functions.Function2
        public final Object invoke(CoroutineScope coroutineScope, Continuation continuation) {
            return ((AnonymousClass2) create(coroutineScope, continuation)).invokeSuspend(Unit.INSTANCE);
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
            Function2 function2 = this.$block;
            PooledConnectionImpl pooledConnectionImpl = this.$confinedConnection;
            this.label = 1;
            Object objInvoke = function2.invoke(pooledConnectionImpl, this);
            return objInvoke == coroutine_suspended ? coroutine_suspended : objInvoke;
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static final Unit useConnection$lambda$6(ConnectionPoolImpl connectionPoolImpl, boolean z) {
        connectionPoolImpl.onTimeout(z);
        return Unit.INSTANCE;
    }

    /* JADX INFO: renamed from: androidx.room.coroutines.ConnectionPoolImpl$useConnection$4, reason: invalid class name */
    static final class AnonymousClass4 extends SuspendLambda implements Function2 {
        final /* synthetic */ Function2 $block;
        final /* synthetic */ Ref$ObjectRef $connection;
        int label;

        /* JADX WARN: 'super' call moved to the top of the method (can break code semantics) */
        AnonymousClass4(Function2 function2, Ref$ObjectRef ref$ObjectRef, Continuation continuation) {
            super(2, continuation);
            this.$block = function2;
            this.$connection = ref$ObjectRef;
        }

        @Override // kotlin.coroutines.jvm.internal.BaseContinuationImpl
        public final Continuation create(Object obj, Continuation continuation) {
            return new AnonymousClass4(this.$block, this.$connection, continuation);
        }

        @Override // kotlin.jvm.functions.Function2
        public final Object invoke(CoroutineScope coroutineScope, Continuation continuation) {
            return ((AnonymousClass4) create(coroutineScope, continuation)).invokeSuspend(Unit.INSTANCE);
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
            Function2 function2 = this.$block;
            Object obj2 = this.$connection.element;
            this.label = 1;
            Object objInvoke = function2.invoke(obj2, this);
            return objInvoke == coroutine_suspended ? coroutine_suspended : objInvoke;
        }
    }

    private final CoroutineContext createConnectionContext(PooledConnectionImpl pooledConnectionImpl) {
        return new ConnectionElement(this.connectionElementKey, pooledConnectionImpl).plus(ThreadLocal_jvmAndroidKt.asContextElement(this.connectionThreadLocal, pooledConnectionImpl));
    }

    private final void onTimeout(boolean z) {
        String str = z ? "reader" : "writer";
        StringBuilder sb = new StringBuilder();
        sb.append("Timed out attempting to acquire a " + str + " connection.");
        sb.append('\n');
        sb.append('\n');
        sb.append("Writer pool:");
        sb.append('\n');
        this.writers.dump(sb);
        sb.append("Reader pool:");
        sb.append('\n');
        this.readers.dump(sb);
        try {
            SQLite.throwSQLiteException(5, sb.toString());
            throw new KotlinNothingValueException();
        } catch (SQLException e) {
            int i = this.onTimeout;
            if (i == 1) {
                throw e;
            }
            if (i != 2) {
                return;
            }
            e.printStackTrace();
        }
    }

    @Override // androidx.room.coroutines.ConnectionPool, java.lang.AutoCloseable
    public void close() {
        if (this.isClosed) {
            return;
        }
        this.isClosed = true;
        this.readers.close();
        this.writers.close();
    }
}
