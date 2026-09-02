package androidx.room;

import androidx.room.concurrent.CloseBarrier;
import androidx.sqlite.SQLite;
import androidx.sqlite.SQLiteConnection;
import androidx.sqlite.SQLiteStatement;
import java.util.LinkedHashMap;
import java.util.Locale;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.atomic.AtomicBoolean;
import kotlin.Pair;
import kotlin.ResultKt;
import kotlin.TuplesKt;
import kotlin.Unit;
import kotlin.collections.MapsKt;
import kotlin.collections.SetsKt;
import kotlin.coroutines.Continuation;
import kotlin.coroutines.intrinsics.IntrinsicsKt;
import kotlin.coroutines.jvm.internal.ContinuationImpl;
import kotlin.jdk7.AutoCloseableKt;
import kotlin.jvm.functions.Function0;
import kotlin.jvm.functions.Function1;
import kotlin.jvm.internal.DefaultConstructorMarker;
import kotlin.jvm.internal.Intrinsics;
import kotlin.text.StringsKt;
import kotlinx.coroutines.BuildersKt__Builders_commonKt;
import kotlinx.coroutines.CoroutineName;
import kotlinx.coroutines.flow.Flow;
import kotlinx.coroutines.flow.FlowKt;
import okhttp3.internal.url._UrlKt;

public final class TriggerBasedInvalidationTracker {
    public static final Companion Companion = new Companion(null);
    private static final String[] TRIGGERS = {"INSERT", "UPDATE", "DELETE"};
    private final RoomDatabase database;
    private final ObservedTableStates observedTableStates;
    private final ObservedTableVersions observedTableVersions;
    private Function0 onAllowRefresh;
    private final Function1 onInvalidatedTablesIds;
    private final AtomicBoolean pendingRefresh;
    private final Map shadowTablesMap;
    private final Map tableIdLookup;
    private final String[] tablesNames;
    private final boolean useTempTable;
    private final Map viewTables;

    /* JADX INFO: renamed from: androidx.room.TriggerBasedInvalidationTracker$checkInvalidatedTables$1, reason: invalid class name */
    static final class AnonymousClass1 extends ContinuationImpl {
        Object L$0;
        int label;
        /* synthetic */ Object result;

        AnonymousClass1(Continuation continuation) {
            super(continuation);
        }

        @Override // kotlin.coroutines.jvm.internal.BaseContinuationImpl
        public final Object invokeSuspend(Object obj) {
            this.result = obj;
            this.label |= Integer.MIN_VALUE;
            return TriggerBasedInvalidationTracker.this.checkInvalidatedTables(null, this);
        }
    }

    /* JADX INFO: renamed from: androidx.room.TriggerBasedInvalidationTracker$notifyInvalidation$1, reason: invalid class name and case insensitive filesystem */
    static final class C01211 extends ContinuationImpl {
        Object L$0;
        int label;
        /* synthetic */ Object result;

        C01211(Continuation continuation) {
            super(continuation);
        }

        @Override // kotlin.coroutines.jvm.internal.BaseContinuationImpl
        public final Object invokeSuspend(Object obj) {
            this.result = obj;
            this.label |= Integer.MIN_VALUE;
            return TriggerBasedInvalidationTracker.this.notifyInvalidation(this);
        }
    }

    /* JADX INFO: renamed from: androidx.room.TriggerBasedInvalidationTracker$startTrackingTable$1, reason: invalid class name and case insensitive filesystem */
    static final class C01221 extends ContinuationImpl {
        int I$0;
        int I$1;
        int I$2;
        Object L$0;
        Object L$1;
        Object L$2;
        int label;
        /* synthetic */ Object result;

        C01221(Continuation continuation) {
            super(continuation);
        }

        @Override // kotlin.coroutines.jvm.internal.BaseContinuationImpl
        public final Object invokeSuspend(Object obj) {
            this.result = obj;
            this.label |= Integer.MIN_VALUE;
            return TriggerBasedInvalidationTracker.this.startTrackingTable(null, 0, this);
        }
    }

    /* JADX INFO: renamed from: androidx.room.TriggerBasedInvalidationTracker$stopTrackingTable$1, reason: invalid class name and case insensitive filesystem */
    static final class C01231 extends ContinuationImpl {
        int I$0;
        int I$1;
        Object L$0;
        Object L$1;
        Object L$2;
        int label;
        /* synthetic */ Object result;

        C01231(Continuation continuation) {
            super(continuation);
        }

        @Override // kotlin.coroutines.jvm.internal.BaseContinuationImpl
        public final Object invokeSuspend(Object obj) {
            this.result = obj;
            this.label |= Integer.MIN_VALUE;
            return TriggerBasedInvalidationTracker.this.stopTrackingTable(null, 0, this);
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static final boolean onAllowRefresh$lambda$0() {
        return true;
    }

    public TriggerBasedInvalidationTracker(RoomDatabase database, Map shadowTablesMap, Map viewTables, String[] tableNames, boolean z, Function1 onInvalidatedTablesIds) {
        String lowerCase;
        Intrinsics.checkNotNullParameter(database, "database");
        Intrinsics.checkNotNullParameter(shadowTablesMap, "shadowTablesMap");
        Intrinsics.checkNotNullParameter(viewTables, "viewTables");
        Intrinsics.checkNotNullParameter(tableNames, "tableNames");
        Intrinsics.checkNotNullParameter(onInvalidatedTablesIds, "onInvalidatedTablesIds");
        this.database = database;
        this.shadowTablesMap = shadowTablesMap;
        this.viewTables = viewTables;
        this.useTempTable = z;
        this.onInvalidatedTablesIds = onInvalidatedTablesIds;
        this.pendingRefresh = new AtomicBoolean(false);
        this.onAllowRefresh = new Function0() { // from class: androidx.room.TriggerBasedInvalidationTracker$$ExternalSyntheticLambda1
            @Override // kotlin.jvm.functions.Function0
            public final Object invoke() {
                return Boolean.valueOf(TriggerBasedInvalidationTracker.onAllowRefresh$lambda$0());
            }
        };
        this.tableIdLookup = new LinkedHashMap();
        int length = tableNames.length;
        String[] strArr = new String[length];
        for (int i = 0; i < length; i++) {
            String str = tableNames[i];
            Locale locale = Locale.ROOT;
            String lowerCase2 = str.toLowerCase(locale);
            Intrinsics.checkNotNullExpressionValue(lowerCase2, "toLowerCase(...)");
            this.tableIdLookup.put(lowerCase2, Integer.valueOf(i));
            String str2 = (String) this.shadowTablesMap.get(tableNames[i]);
            if (str2 != null) {
                lowerCase = str2.toLowerCase(locale);
                Intrinsics.checkNotNullExpressionValue(lowerCase, "toLowerCase(...)");
            } else {
                lowerCase = null;
            }
            if (lowerCase != null) {
                lowerCase2 = lowerCase;
            }
            strArr[i] = lowerCase2;
        }
        this.tablesNames = strArr;
        for (Map.Entry entry : this.shadowTablesMap.entrySet()) {
            String str3 = (String) entry.getValue();
            Locale locale2 = Locale.ROOT;
            String lowerCase3 = str3.toLowerCase(locale2);
            Intrinsics.checkNotNullExpressionValue(lowerCase3, "toLowerCase(...)");
            if (this.tableIdLookup.containsKey(lowerCase3)) {
                String lowerCase4 = ((String) entry.getKey()).toLowerCase(locale2);
                Intrinsics.checkNotNullExpressionValue(lowerCase4, "toLowerCase(...)");
                Map map = this.tableIdLookup;
                map.put(lowerCase4, MapsKt.getValue(map, lowerCase3));
            }
        }
        this.observedTableStates = new ObservedTableStates(this.tablesNames.length);
        this.observedTableVersions = new ObservedTableVersions(this.tablesNames.length);
    }

    public final void setOnAllowRefresh$room_runtime(Function0 function0) {
        Intrinsics.checkNotNullParameter(function0, "<set-?>");
        this.onAllowRefresh = function0;
    }

    public final void configureConnection(SQLiteConnection connection) throws Exception {
        Intrinsics.checkNotNullParameter(connection, "connection");
        SQLiteStatement sQLiteStatementPrepare = connection.prepare("PRAGMA query_only");
        try {
            sQLiteStatementPrepare.step();
            boolean z = sQLiteStatementPrepare.getBoolean(0);
            AutoCloseableKt.closeFinally(sQLiteStatementPrepare, null);
            if (z) {
                return;
            }
            SQLite.execSQL(connection, "PRAGMA temp_store = MEMORY");
            SQLite.execSQL(connection, "PRAGMA recursive_triggers = 1");
            SQLite.execSQL(connection, "DROP TABLE IF EXISTS room_table_modification_log");
            if (this.useTempTable) {
                SQLite.execSQL(connection, "CREATE TEMP TABLE IF NOT EXISTS room_table_modification_log (table_id INTEGER PRIMARY KEY, invalidated INTEGER NOT NULL DEFAULT 0)");
            } else {
                SQLite.execSQL(connection, StringsKt.replace$default("CREATE TEMP TABLE IF NOT EXISTS room_table_modification_log (table_id INTEGER PRIMARY KEY, invalidated INTEGER NOT NULL DEFAULT 0)", "TEMP", _UrlKt.FRAGMENT_ENCODE_SET, false, 4, (Object) null));
            }
            this.observedTableStates.forceNeedSync$room_runtime();
        } catch (Throwable th) {
            try {
                throw th;
            } catch (Throwable th2) {
                AutoCloseableKt.closeFinally(sQLiteStatementPrepare, th);
                throw th2;
            }
        }
    }

    public final Flow createFlow$room_runtime(String[] resolvedTableNames, int[] tableIds, boolean z) {
        Intrinsics.checkNotNullParameter(resolvedTableNames, "resolvedTableNames");
        Intrinsics.checkNotNullParameter(tableIds, "tableIds");
        return FlowKt.flow(new TriggerBasedInvalidationTracker$createFlow$1(this, tableIds, z, resolvedTableNames, null));
    }

    public final Pair validateTableNames$room_runtime(String[] names) {
        Intrinsics.checkNotNullParameter(names, "names");
        String[] strArrResolveViews = resolveViews(names);
        int length = strArrResolveViews.length;
        int[] iArr = new int[length];
        for (int i = 0; i < length; i++) {
            String str = strArrResolveViews[i];
            Map map = this.tableIdLookup;
            String lowerCase = str.toLowerCase(Locale.ROOT);
            Intrinsics.checkNotNullExpressionValue(lowerCase, "toLowerCase(...)");
            Integer num = (Integer) map.get(lowerCase);
            if (num == null) {
                throw new IllegalArgumentException("There is no table with name " + str);
            }
            iArr[i] = num.intValue();
        }
        return TuplesKt.to(strArrResolveViews, iArr);
    }

    private final String[] resolveViews(String[] strArr) {
        Set setCreateSetBuilder = SetsKt.createSetBuilder();
        for (String str : strArr) {
            Map map = this.viewTables;
            String lowerCase = str.toLowerCase(Locale.ROOT);
            Intrinsics.checkNotNullExpressionValue(lowerCase, "toLowerCase(...)");
            Set set = (Set) map.get(lowerCase);
            if (set != null) {
                setCreateSetBuilder.addAll(set);
            } else {
                setCreateSetBuilder.add(str);
            }
        }
        return (String[]) SetsKt.build(setCreateSetBuilder).toArray(new String[0]);
    }

    public final boolean onObserverAdded$room_runtime(int[] tableIds) {
        Intrinsics.checkNotNullParameter(tableIds, "tableIds");
        return this.observedTableStates.onObserverAdded$room_runtime(tableIds);
    }

    public final boolean onObserverRemoved$room_runtime(int[] tableIds) {
        Intrinsics.checkNotNullParameter(tableIds, "tableIds");
        return this.observedTableStates.onObserverRemoved$room_runtime(tableIds);
    }

    /* JADX WARN: Code duplicated, block: B:7:0x0013  */
    public final Object syncTriggers$room_runtime(Continuation continuation) throws Throwable {
        TriggerBasedInvalidationTracker$syncTriggers$1 triggerBasedInvalidationTracker$syncTriggers$1;
        CloseBarrier closeBarrier;
        Throwable th;
        if (continuation instanceof TriggerBasedInvalidationTracker$syncTriggers$1) {
            triggerBasedInvalidationTracker$syncTriggers$1 = (TriggerBasedInvalidationTracker$syncTriggers$1) continuation;
            int i = triggerBasedInvalidationTracker$syncTriggers$1.label;
            if ((i & Integer.MIN_VALUE) != 0) {
                triggerBasedInvalidationTracker$syncTriggers$1.label = i - Integer.MIN_VALUE;
            } else {
                triggerBasedInvalidationTracker$syncTriggers$1 = new TriggerBasedInvalidationTracker$syncTriggers$1(this, continuation);
            }
        } else {
            triggerBasedInvalidationTracker$syncTriggers$1 = new TriggerBasedInvalidationTracker$syncTriggers$1(this, continuation);
        }
        Object obj = triggerBasedInvalidationTracker$syncTriggers$1.result;
        Object coroutine_suspended = IntrinsicsKt.getCOROUTINE_SUSPENDED();
        int i2 = triggerBasedInvalidationTracker$syncTriggers$1.label;
        if (i2 == 0) {
            ResultKt.throwOnFailure(obj);
            CloseBarrier closeBarrier$room_runtime = this.database.getCloseBarrier$room_runtime();
            if (closeBarrier$room_runtime.block$room_runtime()) {
                try {
                    RoomDatabase roomDatabase = this.database;
                    TriggerBasedInvalidationTracker$syncTriggers$2$1 triggerBasedInvalidationTracker$syncTriggers$2$1 = new TriggerBasedInvalidationTracker$syncTriggers$2$1(this, null);
                    triggerBasedInvalidationTracker$syncTriggers$1.L$0 = closeBarrier$room_runtime;
                    triggerBasedInvalidationTracker$syncTriggers$1.label = 1;
                    if (roomDatabase.useConnection(false, triggerBasedInvalidationTracker$syncTriggers$2$1, triggerBasedInvalidationTracker$syncTriggers$1) == coroutine_suspended) {
                        return coroutine_suspended;
                    }
                    closeBarrier = closeBarrier$room_runtime;
                    closeBarrier.unblock$room_runtime();
                } catch (Throwable th2) {
                    closeBarrier = closeBarrier$room_runtime;
                    th = th2;
                    closeBarrier.unblock$room_runtime();
                    throw th;
                }
            }
        } else {
            if (i2 != 1) {
                throw new IllegalStateException("call to 'resume' before 'invoke' with coroutine");
            }
            closeBarrier = (CloseBarrier) triggerBasedInvalidationTracker$syncTriggers$1.L$0;
            try {
                ResultKt.throwOnFailure(obj);
                closeBarrier.unblock$room_runtime();
            } catch (Throwable th3) {
                th = th3;
                closeBarrier.unblock$room_runtime();
                throw th;
            }
        }
        return Unit.INSTANCE;
    }

    /* JADX INFO: Access modifiers changed from: private */
    /* JADX WARN: Code duplicated, block: B:21:0x0087  */
    /* JADX WARN: Code duplicated, block: B:23:0x008d  */
    /* JADX WARN: Code duplicated, block: B:24:0x0090  */
    /* JADX WARN: Code duplicated, block: B:29:0x00e5  */
    /* JADX WARN: Code duplicated, block: B:7:0x0013  */
    /* JADX WARN: Code restructure failed: missing block: B:17:0x0074, code lost:
    
        if (androidx.room.TransactorKt.execSQL(r13, r15, r0) == r1) goto L27;
     */
    /* JADX WARN: Code restructure failed: missing block: B:26:0x00e0, code lost:
    
        if (androidx.room.TransactorKt.execSQL(r7, r15, r0) == r1) goto L27;
     */
    /* JADX WARN: Code restructure failed: missing block: B:27:0x00e2, code lost:
    
        return r1;
     */
    /* JADX WARN: Unsupported multi-entry loop pattern (BACK_EDGE: B:26:0x00e0 -> B:28:0x00e3). Please report as a decompilation issue!!! */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
    */
    public final Object startTrackingTable(PooledConnection pooledConnection, int i, Continuation continuation) {
        C01221 c01221;
        PooledConnection pooledConnection2;
        int length;
        String[] strArr;
        int i2;
        int i3;
        String str;
        String str2;
        if (continuation instanceof C01221) {
            c01221 = (C01221) continuation;
            int i4 = c01221.label;
            if ((i4 & Integer.MIN_VALUE) != 0) {
                c01221.label = i4 - Integer.MIN_VALUE;
            } else {
                c01221 = new C01221(continuation);
            }
        } else {
            c01221 = new C01221(continuation);
        }
        Object obj = c01221.result;
        Object coroutine_suspended = IntrinsicsKt.getCOROUTINE_SUSPENDED();
        int i5 = c01221.label;
        if (i5 != 0) {
            if (i5 == 1) {
                i = c01221.I$0;
                pooledConnection = (PooledConnection) c01221.L$0;
                ResultKt.throwOnFailure(obj);
            } else {
                if (i5 != 2) {
                    throw new IllegalStateException("call to 'resume' before 'invoke' with coroutine");
                }
                length = c01221.I$2;
                i3 = c01221.I$1;
                i2 = c01221.I$0;
                strArr = (String[]) c01221.L$2;
                str = (String) c01221.L$1;
                pooledConnection2 = (PooledConnection) c01221.L$0;
                ResultKt.throwOnFailure(obj);
            }
            i3++;
            if (i3 < length) {
                return Unit.INSTANCE;
            }
            String str3 = strArr[i3];
            if (this.useTempTable) {
                str2 = "TEMP";
            } else {
                str2 = _UrlKt.FRAGMENT_ENCODE_SET;
            }
            String str4 = "CREATE " + str2 + " TRIGGER IF NOT EXISTS `" + Companion.getTriggerName(str, str3) + "` AFTER " + str3 + " ON `" + str + "` BEGIN UPDATE room_table_modification_log SET invalidated = 1 WHERE table_id = " + i2 + " AND invalidated = 0; END";
            c01221.L$0 = pooledConnection2;
            c01221.L$1 = str;
            c01221.L$2 = strArr;
            c01221.I$0 = i2;
            c01221.I$1 = i3;
            c01221.I$2 = length;
            c01221.label = 2;
        } else {
            ResultKt.throwOnFailure(obj);
            String str5 = "INSERT OR IGNORE INTO room_table_modification_log VALUES(" + i + ", 0)";
            c01221.L$0 = pooledConnection;
            c01221.I$0 = i;
            c01221.label = 1;
        }
        String str6 = this.tablesNames[i];
        String[] strArr2 = TRIGGERS;
        pooledConnection2 = pooledConnection;
        length = strArr2.length;
        strArr = strArr2;
        i2 = i;
        i3 = 0;
        str = str6;
        if (i3 < length) {
            return Unit.INSTANCE;
        }
        String str7 = strArr[i3];
        if (this.useTempTable) {
            str2 = "TEMP";
        } else {
            str2 = _UrlKt.FRAGMENT_ENCODE_SET;
        }
        String str8 = "CREATE " + str2 + " TRIGGER IF NOT EXISTS `" + Companion.getTriggerName(str, str7) + "` AFTER " + str7 + " ON `" + str + "` BEGIN UPDATE room_table_modification_log SET invalidated = 1 WHERE table_id = " + i2 + " AND invalidated = 0; END";
        c01221.L$0 = pooledConnection2;
        c01221.L$1 = str;
        c01221.L$2 = strArr;
        c01221.I$0 = i2;
        c01221.I$1 = i3;
        c01221.I$2 = length;
        c01221.label = 2;
    }

    /* JADX INFO: Access modifiers changed from: private */
    /* JADX WARN: Code duplicated, block: B:16:0x0054  */
    /* JADX WARN: Code duplicated, block: B:18:0x0084 A[RETURN] */
    /* JADX WARN: Code duplicated, block: B:19:0x0085  */
    /* JADX WARN: Code duplicated, block: B:7:0x0013  */
    /* JADX WARN: Unsupported multi-entry loop pattern (BACK_EDGE: B:19:0x0085 -> B:20:0x0087). Please report as a decompilation issue!!! */
    /*  JADX ERROR: JadxOverflowException in pass: RegionMakerVisitor
        jadx.core.utils.exceptions.JadxOverflowException: Regions stack size limit reached
        	at jadx.core.utils.ErrorsCounter.addError(ErrorsCounter.java:59)
        	at jadx.core.utils.ErrorsCounter.error(ErrorsCounter.java:31)
        	at jadx.core.dex.attributes.nodes.NotificationAttrNode.addError(NotificationAttrNode.java:19)
        */
    public final java.lang.Object stopTrackingTable(androidx.room.PooledConnection r10, int r11, kotlin.coroutines.Continuation r12) {
        /*
            r9 = this;
            boolean r0 = r12 instanceof androidx.room.TriggerBasedInvalidationTracker.C01231
            if (r0 == 0) goto L13
            r0 = r12
            androidx.room.TriggerBasedInvalidationTracker$stopTrackingTable$1 r0 = (androidx.room.TriggerBasedInvalidationTracker.C01231) r0
            int r1 = r0.label
            r2 = -2147483648(0xffffffff80000000, float:-0.0)
            r3 = r1 & r2
            if (r3 == 0) goto L13
            int r1 = r1 - r2
            r0.label = r1
            goto L18
        L13:
            androidx.room.TriggerBasedInvalidationTracker$stopTrackingTable$1 r0 = new androidx.room.TriggerBasedInvalidationTracker$stopTrackingTable$1
            r0.<init>(r12)
        L18:
            java.lang.Object r12 = r0.result
            java.lang.Object r1 = kotlin.coroutines.intrinsics.IntrinsicsKt.getCOROUTINE_SUSPENDED()
            int r2 = r0.label
            r3 = 1
            if (r2 == 0) goto L42
            if (r2 != r3) goto L3a
            int r10 = r0.I$1
            int r11 = r0.I$0
            java.lang.Object r2 = r0.L$2
            java.lang.String[] r2 = (java.lang.String[]) r2
            java.lang.Object r4 = r0.L$1
            java.lang.String r4 = (java.lang.String) r4
            java.lang.Object r5 = r0.L$0
            androidx.room.PooledConnection r5 = (androidx.room.PooledConnection) r5
            kotlin.ResultKt.throwOnFailure(r12)
            r12 = r4
            goto L87
        L3a:
            java.lang.IllegalStateException r10 = new java.lang.IllegalStateException
            java.lang.String r11 = "call to 'resume' before 'invoke' with coroutine"
            r10.<init>(r11)
            throw r10
        L42:
            kotlin.ResultKt.throwOnFailure(r12)
            java.lang.String[] r12 = r9.tablesNames
            r11 = r12[r11]
            java.lang.String[] r12 = androidx.room.TriggerBasedInvalidationTracker.TRIGGERS
            int r2 = r12.length
            r4 = 0
            r8 = r11
            r11 = r10
            r10 = r2
            r2 = r12
            r12 = r8
        L52:
            if (r4 >= r10) goto L8b
            r5 = r2[r4]
            androidx.room.TriggerBasedInvalidationTracker$Companion r6 = androidx.room.TriggerBasedInvalidationTracker.Companion
            java.lang.String r5 = androidx.room.TriggerBasedInvalidationTracker.Companion.access$getTriggerName(r6, r12, r5)
            java.lang.StringBuilder r6 = new java.lang.StringBuilder
            r6.<init>()
            java.lang.String r7 = "DROP TRIGGER IF EXISTS `"
            r6.append(r7)
            r6.append(r5)
            r5 = 96
            r6.append(r5)
            java.lang.String r5 = r6.toString()
            r0.L$0 = r11
            r0.L$1 = r12
            r0.L$2 = r2
            r0.I$0 = r4
            r0.I$1 = r10
            r0.label = r3
            java.lang.Object r5 = androidx.room.TransactorKt.execSQL(r11, r5, r0)
            if (r5 != r1) goto L85
            return r1
        L85:
            r5 = r11
            r11 = r4
        L87:
            int r4 = r11 + 1
            r11 = r5
            goto L52
        L8b:
            kotlin.Unit r10 = kotlin.Unit.INSTANCE
            return r10
        */
        throw new UnsupportedOperationException("Method not decompiled: androidx.room.TriggerBasedInvalidationTracker.stopTrackingTable(androidx.room.PooledConnection, int, kotlin.coroutines.Continuation):java.lang.Object");
    }

    public final void refreshInvalidationAsync$room_runtime(Function0 onRefreshScheduled, Function0 onRefreshCompleted) {
        Intrinsics.checkNotNullParameter(onRefreshScheduled, "onRefreshScheduled");
        Intrinsics.checkNotNullParameter(onRefreshCompleted, "onRefreshCompleted");
        if (this.pendingRefresh.compareAndSet(false, true)) {
            onRefreshScheduled.invoke();
            BuildersKt__Builders_commonKt.launch$default(this.database.getCoroutineScope(), new CoroutineName("Room Invalidation Tracker Refresh"), null, new TriggerBasedInvalidationTracker$refreshInvalidationAsync$3(this, onRefreshCompleted, null), 2, null);
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    /* JADX WARN: Code duplicated, block: B:7:0x0013  */
    public final Object notifyInvalidation(Continuation continuation) throws Throwable {
        C01211 c01211;
        CloseBarrier closeBarrier;
        Throwable th;
        if (continuation instanceof C01211) {
            c01211 = (C01211) continuation;
            int i = c01211.label;
            if ((i & Integer.MIN_VALUE) != 0) {
                c01211.label = i - Integer.MIN_VALUE;
            } else {
                c01211 = new C01211(continuation);
            }
        } else {
            c01211 = new C01211(continuation);
        }
        Object obj = c01211.result;
        Object coroutine_suspended = IntrinsicsKt.getCOROUTINE_SUSPENDED();
        int i2 = c01211.label;
        if (i2 == 0) {
            ResultKt.throwOnFailure(obj);
            CloseBarrier closeBarrier$room_runtime = this.database.getCloseBarrier$room_runtime();
            if (closeBarrier$room_runtime.block$room_runtime()) {
                try {
                    if (!this.pendingRefresh.compareAndSet(true, false)) {
                        Set setEmptySet = SetsKt.emptySet();
                        closeBarrier$room_runtime.unblock$room_runtime();
                        return setEmptySet;
                    }
                    if (!((Boolean) this.onAllowRefresh.invoke()).booleanValue()) {
                        Set setEmptySet2 = SetsKt.emptySet();
                        closeBarrier$room_runtime.unblock$room_runtime();
                        return setEmptySet2;
                    }
                    RoomDatabase roomDatabase = this.database;
                    TriggerBasedInvalidationTracker$notifyInvalidation$2$invalidatedTableIds$1 triggerBasedInvalidationTracker$notifyInvalidation$2$invalidatedTableIds$1 = new TriggerBasedInvalidationTracker$notifyInvalidation$2$invalidatedTableIds$1(this, null);
                    c01211.L$0 = closeBarrier$room_runtime;
                    c01211.label = 1;
                    Object objUseConnection = roomDatabase.useConnection(false, triggerBasedInvalidationTracker$notifyInvalidation$2$invalidatedTableIds$1, c01211);
                    if (objUseConnection == coroutine_suspended) {
                        return coroutine_suspended;
                    }
                    closeBarrier = closeBarrier$room_runtime;
                    obj = objUseConnection;
                } catch (Throwable th2) {
                    closeBarrier = closeBarrier$room_runtime;
                    th = th2;
                    closeBarrier.unblock$room_runtime();
                    throw th;
                }
            } else {
                return SetsKt.emptySet();
            }
        } else {
            if (i2 != 1) {
                throw new IllegalStateException("call to 'resume' before 'invoke' with coroutine");
            }
            closeBarrier = (CloseBarrier) c01211.L$0;
            try {
                ResultKt.throwOnFailure(obj);
            } catch (Throwable th3) {
                th = th3;
                closeBarrier.unblock$room_runtime();
                throw th;
            }
        }
        Set set = (Set) obj;
        if (!set.isEmpty()) {
            this.observedTableVersions.increment(set);
            this.onInvalidatedTablesIds.invoke(set);
        }
        closeBarrier.unblock$room_runtime();
        return set;
    }

    /* JADX INFO: Access modifiers changed from: private */
    /* JADX WARN: Code duplicated, block: B:7:0x0013  */
    public final Object checkInvalidatedTables(PooledConnection pooledConnection, Continuation continuation) {
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
        Object objUsePrepared = anonymousClass1.result;
        Object coroutine_suspended = IntrinsicsKt.getCOROUTINE_SUSPENDED();
        int i2 = anonymousClass1.label;
        if (i2 == 0) {
            ResultKt.throwOnFailure(objUsePrepared);
            Function1 function1 = new Function1() { // from class: androidx.room.TriggerBasedInvalidationTracker$$ExternalSyntheticLambda0
                @Override // kotlin.jvm.functions.Function1
                public final Object invoke(Object obj) {
                    return TriggerBasedInvalidationTracker.checkInvalidatedTables$lambda$14((SQLiteStatement) obj);
                }
            };
            anonymousClass1.L$0 = pooledConnection;
            anonymousClass1.label = 1;
            objUsePrepared = pooledConnection.usePrepared("SELECT * FROM room_table_modification_log WHERE invalidated = 1", function1, anonymousClass1);
            if (objUsePrepared != coroutine_suspended) {
            }
            return coroutine_suspended;
        }
        if (i2 != 1) {
            if (i2 != 2) {
                throw new IllegalStateException("call to 'resume' before 'invoke' with coroutine");
            }
            Set set = (Set) anonymousClass1.L$0;
            ResultKt.throwOnFailure(objUsePrepared);
            return set;
        }
        pooledConnection = (PooledConnection) anonymousClass1.L$0;
        ResultKt.throwOnFailure(objUsePrepared);
        Set set2 = (Set) objUsePrepared;
        if (!set2.isEmpty()) {
            anonymousClass1.L$0 = set2;
            anonymousClass1.label = 2;
            if (TransactorKt.execSQL(pooledConnection, "UPDATE room_table_modification_log SET invalidated = 0 WHERE invalidated = 1", anonymousClass1) == coroutine_suspended) {
                return coroutine_suspended;
            }
        }
        return set2;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static final Set checkInvalidatedTables$lambda$14(SQLiteStatement statement) {
        Intrinsics.checkNotNullParameter(statement, "statement");
        Set setCreateSetBuilder = SetsKt.createSetBuilder();
        while (statement.step()) {
            setCreateSetBuilder.add(Integer.valueOf((int) statement.getLong(0)));
        }
        return SetsKt.build(setCreateSetBuilder);
    }

    public final void resetSync$room_runtime() {
        this.observedTableStates.resetTriggerState$room_runtime();
    }

    public static final class Companion {
        public /* synthetic */ Companion(DefaultConstructorMarker defaultConstructorMarker) {
            this();
        }

        private Companion() {
        }

        /* JADX INFO: Access modifiers changed from: private */
        public final String getTriggerName(String str, String str2) {
            return "room_table_modification_trigger_" + str + '_' + str2;
        }
    }
}
