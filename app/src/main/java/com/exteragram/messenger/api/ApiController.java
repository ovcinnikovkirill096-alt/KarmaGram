package com.exteragram.messenger.api;

import android.content.Context;
import android.content.SharedPreferences;
import androidx.work.Constraints;
import androidx.work.ExistingPeriodicWorkPolicy;
import androidx.work.NetworkType;
import androidx.work.PeriodicWorkRequest;
import androidx.work.WorkManager;
import com.exteragram.messenger.ExteraConfig;
import com.exteragram.messenger.api.db.DatabaseHelper;
import com.exteragram.messenger.api.dto.ProfileDTO;
import com.exteragram.messenger.api.network.ApiClient;
import com.exteragram.messenger.api.network.ApiService;
import com.exteragram.messenger.api.worker.SyncWorker;
import j$.time.Duration;
import j$.time.Instant;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.concurrent.TimeUnit;
import kotlin.Pair;
import kotlin.ResultKt;
import kotlin.Unit;
import kotlin.collections.CollectionsKt;
import kotlin.coroutines.Continuation;
import kotlin.coroutines.intrinsics.IntrinsicsKt;
import kotlin.coroutines.jvm.internal.Boxing;
import kotlin.coroutines.jvm.internal.ContinuationImpl;
import kotlin.coroutines.jvm.internal.DebugMetadata;
import kotlin.coroutines.jvm.internal.SpillingKt;
import kotlin.coroutines.jvm.internal.SuspendLambda;
import kotlin.jvm.functions.Function2;
import kotlin.jvm.internal.Intrinsics;
import kotlinx.coroutines.BuildersKt__Builders_commonKt;
import kotlinx.coroutines.CoroutineScope;
import kotlinx.coroutines.CoroutineScopeKt;
import kotlinx.coroutines.Dispatchers;
import org.telegram.messenger.ApplicationLoader;
import org.telegram.messenger.FileLog;
import org.telegram.ui.Components.ForegroundDetector;
import retrofit2.Response;

public final class ApiController {
    private static long lastBoostySyncTime;
    private static long lastExchangeRatesSyncTime;
    private static long lastProfilesSyncTime;
    public static final ApiController INSTANCE = new ApiController();
    private static final CoroutineScope scope = CoroutineScopeKt.CoroutineScope(Dispatchers.getIO());

    /* JADX INFO: renamed from: com.exteragram.messenger.api.ApiController$performSync$1, reason: invalid class name and case insensitive filesystem */
    @DebugMetadata(c = "com.exteragram.messenger.api.ApiController", f = "ApiController.kt", l = {77, 79, 94, 96, 103, 106}, m = "performSync", v = 1)
    static final class C01351 extends ContinuationImpl {
        int I$0;
        long J$0;
        Object L$0;
        Object L$1;
        Object L$2;
        Object L$3;
        Object L$4;
        Object L$5;
        Object L$6;
        boolean Z$0;
        int label;
        /* synthetic */ Object result;

        C01351(Continuation<? super C01351> continuation) {
            super(continuation);
        }

        @Override // kotlin.coroutines.jvm.internal.BaseContinuationImpl
        public final Object invokeSuspend(Object obj) {
            this.result = obj;
            this.label |= Integer.MIN_VALUE;
            return ApiController.this.performSync(null, false, this);
        }
    }

    public static final void sync() {
        sync$default(null, false, 3, null);
    }

    public static final void sync(SharedPreferences preferences) {
        Intrinsics.checkNotNullParameter(preferences, "preferences");
        sync$default(preferences, false, 2, null);
    }

    private ApiController() {
    }

    public static final void init() {
        sync$default(null, false, 3, null);
        scheduleWorker();
        ForegroundDetector.getInstance().addListener(new ForegroundDetector.Listener() { // from class: com.exteragram.messenger.api.ApiController.init.1
            @Override // org.telegram.ui.Components.ForegroundDetector.Listener
            public void onBecameBackground() {
            }

            @Override // org.telegram.ui.Components.ForegroundDetector.Listener
            public void onBecameForeground() {
                ApiController.sync$default(null, false, 3, null);
            }
        });
    }

    public static /* synthetic */ void sync$default(SharedPreferences preferences, boolean z, int i, Object obj) {
        if ((i & 1) != 0) {
            preferences = ExteraConfig.preferences;
            Intrinsics.checkNotNullExpressionValue(preferences, "preferences");
        }
        if ((i & 2) != 0) {
            z = false;
        }
        sync(preferences, z);
    }

    public static final void sync(SharedPreferences preferences, boolean z) {
        Intrinsics.checkNotNullParameter(preferences, "preferences");
        long jCurrentTimeMillis = System.currentTimeMillis();
        boolean z2 = true;
        boolean z3 = z || jCurrentTimeMillis - lastProfilesSyncTime >= Duration.ofMinutes(8L).toMillis();
        boolean z4 = z || jCurrentTimeMillis - lastBoostySyncTime >= Duration.ofMinutes(40L).toMillis();
        if (!z && jCurrentTimeMillis - lastExchangeRatesSyncTime < Duration.ofHours(2L).toMillis()) {
            z2 = false;
        }
        if (z3 || z4 || z2) {
            BuildersKt__Builders_commonKt.launch$default(scope, null, null, new C01361(preferences, z, null), 3, null);
        }
    }

    /* JADX INFO: renamed from: com.exteragram.messenger.api.ApiController$sync$1, reason: invalid class name and case insensitive filesystem */
    @DebugMetadata(c = "com.exteragram.messenger.api.ApiController$sync$1", f = "ApiController.kt", l = {66}, m = "invokeSuspend", v = 1)
    static final class C01361 extends SuspendLambda implements Function2<CoroutineScope, Continuation<? super Unit>, Object> {
        final /* synthetic */ boolean $force;
        final /* synthetic */ SharedPreferences $preferences;
        int label;

        /* JADX WARN: 'super' call moved to the top of the method (can break code semantics) */
        C01361(SharedPreferences sharedPreferences, boolean z, Continuation<? super C01361> continuation) {
            super(2, continuation);
            this.$preferences = sharedPreferences;
            this.$force = z;
        }

        @Override // kotlin.coroutines.jvm.internal.BaseContinuationImpl
        public final Continuation<Unit> create(Object obj, Continuation<?> continuation) {
            return new C01361(this.$preferences, this.$force, continuation);
        }

        @Override // kotlin.jvm.functions.Function2
        public final Object invoke(CoroutineScope coroutineScope, Continuation<? super Unit> continuation) {
            return ((C01361) create(coroutineScope, continuation)).invokeSuspend(Unit.INSTANCE);
        }

        @Override // kotlin.coroutines.jvm.internal.BaseContinuationImpl
        public final Object invokeSuspend(Object obj) {
            Object coroutine_suspended = IntrinsicsKt.getCOROUTINE_SUSPENDED();
            int i = this.label;
            if (i == 0) {
                ResultKt.throwOnFailure(obj);
                ApiController apiController = ApiController.INSTANCE;
                SharedPreferences sharedPreferences = this.$preferences;
                boolean z = this.$force;
                this.label = 1;
                if (apiController.performSync(sharedPreferences, z, this) == coroutine_suspended) {
                    return coroutine_suspended;
                }
            } else {
                if (i != 1) {
                    throw new IllegalStateException("call to 'resume' before 'invoke' with coroutine");
                }
                ResultKt.throwOnFailure(obj);
            }
            return Unit.INSTANCE;
        }
    }

    public static /* synthetic */ Object performSync$default(ApiController apiController, SharedPreferences preferences, boolean z, Continuation continuation, int i, Object obj) {
        if ((i & 1) != 0) {
            preferences = ExteraConfig.preferences;
            Intrinsics.checkNotNullExpressionValue(preferences, "preferences");
        }
        if ((i & 2) != 0) {
            z = false;
        }
        return apiController.performSync(preferences, z, continuation);
    }

    /* JADX WARN: Code duplicated, block: B:113:0x02e9 A[Catch: all -> 0x024c, TryCatch #4 {all -> 0x024c, blocks: (B:120:0x032c, B:111:0x02e0, B:113:0x02e9, B:92:0x023e, B:94:0x0248, B:97:0x024f, B:99:0x0259, B:101:0x0279, B:102:0x028f, B:104:0x0295, B:105:0x02a7), top: B:137:0x023e }] */
    /* JADX WARN: Code duplicated, block: B:116:0x0321  */
    /* JADX WARN: Code duplicated, block: B:118:0x0327 A[PHI: r1 r8 r9 r16
  0x0327: PHI (r1v9 int) = (r1v7 int), (r1v10 int) binds: [B:119:0x0329, B:112:0x02e7] A[DONT_GENERATE, DONT_INLINE]
  0x0327: PHI (r8v4 android.content.SharedPreferences) = (r8v3 android.content.SharedPreferences), (r8v5 android.content.SharedPreferences) binds: [B:119:0x0329, B:112:0x02e7] A[DONT_GENERATE, DONT_INLINE]
  0x0327: PHI (r9v7 long) = (r9v5 long), (r9v8 long) binds: [B:119:0x0329, B:112:0x02e7] A[DONT_GENERATE, DONT_INLINE]
  0x0327: PHI (r16v6 boolean) = (r16v3 boolean), (r16v7 boolean) binds: [B:119:0x0329, B:112:0x02e7] A[DONT_GENERATE, DONT_INLINE]] */
    /* JADX WARN: Code duplicated, block: B:126:0x034b  */
    /* JADX WARN: Code duplicated, block: B:127:0x034e  */
    /* JADX WARN: Code duplicated, block: B:62:0x0172 A[DONT_INVERT, PHI: r1 r5 r7 r8
  0x0172: PHI (r1v3 ??) = (r1v27 ??), (r1v28 ??), (r1v29 ??), (r1v30 ??), (r1v31 ??) binds: [B:61:0x016f, B:39:0x010e, B:47:0x0136, B:49:0x013c, B:53:0x0162] A[DONT_GENERATE, DONT_INLINE]
  0x0172: PHI (r5v3 android.content.SharedPreferences) = 
  (r5v2 android.content.SharedPreferences)
  (r5v13 android.content.SharedPreferences)
  (r5v14 android.content.SharedPreferences)
  (r5v14 android.content.SharedPreferences)
  (r5v18 android.content.SharedPreferences)
 binds: [B:61:0x016f, B:39:0x010e, B:47:0x0136, B:49:0x013c, B:53:0x0162] A[DONT_GENERATE, DONT_INLINE]
  0x0172: PHI (r7v3 int) = (r7v2 int), (r7v27 int), (r7v28 int), (r7v28 int), (r7v32 int) binds: [B:61:0x016f, B:39:0x010e, B:47:0x0136, B:49:0x013c, B:53:0x0162] A[DONT_GENERATE, DONT_INLINE]
  0x0172: PHI (r8v2 long) = (r8v1 long), (r8v10 long), (r8v11 long), (r8v11 long), (r8v14 long) binds: [B:61:0x016f, B:39:0x010e, B:47:0x0136, B:49:0x013c, B:53:0x0162] A[DONT_GENERATE, DONT_INLINE]] */
    /* JADX WARN: Code duplicated, block: B:63:0x0174  */
    /* JADX WARN: Code duplicated, block: B:70:0x0198 A[Catch: all -> 0x005d, TryCatch #5 {all -> 0x005d, blocks: (B:13:0x0056, B:18:0x0084, B:21:0x009d, B:80:0x01f3, B:81:0x01f6, B:83:0x01fc, B:85:0x0202, B:87:0x0215, B:88:0x0226, B:90:0x022c, B:24:0x00b4, B:74:0x01c0, B:68:0x018c, B:70:0x0198, B:76:0x01cb), top: B:130:0x002b }] */
    /* JADX WARN: Code duplicated, block: B:72:0x01b5  */
    /* JADX WARN: Code duplicated, block: B:73:0x01b7  */
    /* JADX WARN: Code duplicated, block: B:76:0x01cb A[Catch: all -> 0x005d, TryCatch #5 {all -> 0x005d, blocks: (B:13:0x0056, B:18:0x0084, B:21:0x009d, B:80:0x01f3, B:81:0x01f6, B:83:0x01fc, B:85:0x0202, B:87:0x0215, B:88:0x0226, B:90:0x022c, B:24:0x00b4, B:74:0x01c0, B:68:0x018c, B:70:0x0198, B:76:0x01cb), top: B:130:0x002b }] */
    /* JADX WARN: Code duplicated, block: B:78:0x01e8  */
    /* JADX WARN: Code duplicated, block: B:79:0x01ea  */
    /* JADX WARN: Code duplicated, block: B:7:0x0019  */
    /* JADX WARN: Code duplicated, block: B:83:0x01fc A[Catch: all -> 0x005d, TryCatch #5 {all -> 0x005d, blocks: (B:13:0x0056, B:18:0x0084, B:21:0x009d, B:80:0x01f3, B:81:0x01f6, B:83:0x01fc, B:85:0x0202, B:87:0x0215, B:88:0x0226, B:90:0x022c, B:24:0x00b4, B:74:0x01c0, B:68:0x018c, B:70:0x0198, B:76:0x01cb), top: B:130:0x002b }] */
    /* JADX WARN: Multi-variable type inference failed */
    /* JADX WARN: Not initialized variable reg: 7, insn: 0x00d4: MOVE (r19 I:??[long, double]) = (r7 I:??[long, double]), block:B:30:0x00d4 */
    /* JADX WARN: Not initialized variable reg: 9, insn: 0x00d8: MOVE (r5 I:??[OBJECT, ARRAY]) = (r9 I:??[OBJECT, ARRAY]), block:B:30:0x00d4 */
    /* JADX WARN: Type inference failed for: r1v0, types: [boolean] */
    /* JADX WARN: Type inference failed for: r1v1 */
    /* JADX WARN: Type inference failed for: r1v14, types: [boolean] */
    /* JADX WARN: Type inference failed for: r1v16 */
    /* JADX WARN: Type inference failed for: r1v17 */
    /* JADX WARN: Type inference failed for: r1v18 */
    /* JADX WARN: Type inference failed for: r1v2 */
    /* JADX WARN: Type inference failed for: r1v20 */
    /* JADX WARN: Type inference failed for: r1v26 */
    /* JADX WARN: Type inference failed for: r1v27 */
    /* JADX WARN: Type inference failed for: r1v28 */
    /* JADX WARN: Type inference failed for: r1v29 */
    /* JADX WARN: Type inference failed for: r1v3, types: [boolean] */
    /* JADX WARN: Type inference failed for: r1v30 */
    /* JADX WARN: Type inference failed for: r1v31 */
    /* JADX WARN: Type inference failed for: r1v32 */
    /* JADX WARN: Type inference failed for: r5v25 */
    /* JADX WARN: Type inference failed for: r5v26 */
    /* JADX WARN: Type inference failed for: r5v27 */
    /* JADX WARN: Type inference failed for: r5v28 */
    /* JADX WARN: Type inference failed for: r5v29 */
    /* JADX WARN: Type inference failed for: r5v30 */
    /* JADX WARN: Type inference failed for: r5v4 */
    /* JADX WARN: Type inference failed for: r5v5 */
    /* JADX WARN: Type inference failed for: r5v6 */
    /* JADX WARN: Type inference failed for: r5v7, types: [boolean] */
    /* JADX WARN: Type inference failed for: r5v8 */
    /* JADX WARN: Type inference failed for: r5v9, types: [boolean] */
    public final Object performSync(SharedPreferences sharedPreferences, boolean z, Continuation<? super Boolean> continuation) {
        C01351 c01351;
        boolean z2;
        long j;
        int i;
        SharedPreferences sharedPreferences2;
        SharedPreferences sharedPreferences3;
        long j2;
        ?? r1;
        String string;
        ApiService apiService;
        Object updates;
        ApiService apiService2;
        SharedPreferences sharedPreferences4;
        ?? r5;
        int i2;
        long j3;
        Object allProfiles;
        ?? r6;
        Response response;
        ?? r7;
        Response response2;
        ApiService apiService3;
        long j4;
        SharedPreferences sharedPreferences5;
        List list;
        List list2;
        List<ProfileDTO> list3;
        String str;
        ?? r8;
        DatabaseHelper databaseHelper;
        SharedPreferences sharedPreferences6;
        long j5;
        boolean z3;
        Response response3;
        ?? r2 = z;
        if (continuation instanceof C01351) {
            c01351 = (C01351) continuation;
            int i3 = c01351.label;
            if ((i3 & Integer.MIN_VALUE) != 0) {
                c01351.label = i3 - Integer.MIN_VALUE;
            } else {
                c01351 = new C01351(continuation);
            }
        } else {
            c01351 = new C01351(continuation);
        }
        Object boostySubscribers = c01351.result;
        Object coroutine_suspended = IntrinsicsKt.getCOROUTINE_SUSPENDED();
        int i4 = c01351.label;
        try {
            try {
                switch (i4) {
                    case 0:
                        ResultKt.throwOnFailure(boostySubscribers);
                        long jCurrentTimeMillis = System.currentTimeMillis();
                        if (r2 != 0 || jCurrentTimeMillis - lastBoostySyncTime >= Duration.ofMinutes(40L).toMillis()) {
                            try {
                                ApiService apiService4 = ApiClient.INSTANCE.getApiService();
                                sharedPreferences3 = sharedPreferences;
                                try {
                                    c01351.L$0 = sharedPreferences3;
                                    c01351.Z$0 = r2;
                                    c01351.J$0 = jCurrentTimeMillis;
                                    c01351.I$0 = 1;
                                    c01351.label = 1;
                                    boostySubscribers = apiService4.getBoostySubscribers(c01351);
                                    if (boostySubscribers != coroutine_suspended) {
                                        j2 = jCurrentTimeMillis;
                                        i = 1;
                                        r2 = r2;
                                        try {
                                            response3 = (Response) boostySubscribers;
                                            r1 = r2;
                                            if (!response3.isSuccessful() && response3.body() != null) {
                                                DatabaseHelper databaseHelper2 = DatabaseHelper.INSTANCE;
                                                Object objBody = response3.body();
                                                Intrinsics.checkNotNull(objBody);
                                                c01351.L$0 = sharedPreferences3;
                                                c01351.L$1 = SpillingKt.nullOutSpilledVariable(response3);
                                                c01351.Z$0 = r2;
                                                c01351.J$0 = j2;
                                                c01351.I$0 = i == true ? 1 : 0;
                                                c01351.label = 2;
                                                if (databaseHelper2.insertBoostySubscribers((List) objBody, c01351) == coroutine_suspended) {
                                                    r1 = r2;
                                                    r2 = r2;
                                                } else {
                                                    r1 = r2;
                                                    r2 = r2;
                                                    lastBoostySyncTime = j2;
                                                    r1 = r2;
                                                    if (r1 == 0) {
                                                    }
                                                    string = sharedPreferences3.getString("lastSyncTimestamp", null);
                                                    apiService = ApiClient.INSTANCE.getApiService();
                                                    if (string == null) {
                                                        c01351.L$0 = sharedPreferences3;
                                                        c01351.L$1 = SpillingKt.nullOutSpilledVariable(string);
                                                        c01351.L$2 = SpillingKt.nullOutSpilledVariable(apiService);
                                                        c01351.Z$0 = r1;
                                                        c01351.J$0 = j2;
                                                        c01351.I$0 = i == true ? 1 : 0;
                                                        c01351.label = 3;
                                                        allProfiles = apiService.getAllProfiles(c01351);
                                                        if (allProfiles == coroutine_suspended) {
                                                            apiService2 = apiService;
                                                            boostySubscribers = allProfiles;
                                                            sharedPreferences4 = sharedPreferences3;
                                                            r6 = r1;
                                                            i2 = i == true ? 1 : 0;
                                                            j3 = j2;
                                                            response = (Response) boostySubscribers;
                                                            r7 = r6;
                                                            response2 = response;
                                                            apiService3 = apiService2;
                                                            long j6 = j3;
                                                            String str2 = string;
                                                            j4 = j6;
                                                            sharedPreferences5 = sharedPreferences4;
                                                            if (response2.isSuccessful()) {
                                                            }
                                                            z2 = true;
                                                            i = 0;
                                                            if (i != 0) {
                                                                z3 = z2;
                                                            } else {
                                                                z3 = false;
                                                            }
                                                            return Boxing.boxBoolean(z3);
                                                        }
                                                    } else {
                                                        c01351.L$0 = sharedPreferences3;
                                                        c01351.L$1 = SpillingKt.nullOutSpilledVariable(string);
                                                        c01351.L$2 = SpillingKt.nullOutSpilledVariable(apiService);
                                                        c01351.Z$0 = r1;
                                                        c01351.J$0 = j2;
                                                        c01351.I$0 = i == true ? 1 : 0;
                                                        c01351.label = 4;
                                                        updates = apiService.getUpdates(string, c01351);
                                                        if (updates == coroutine_suspended) {
                                                            apiService2 = apiService;
                                                            boostySubscribers = updates;
                                                            sharedPreferences4 = sharedPreferences3;
                                                            r5 = r1;
                                                            i2 = i == true ? 1 : 0;
                                                            j3 = j2;
                                                            response = (Response) boostySubscribers;
                                                            r7 = r5;
                                                            response2 = response;
                                                            apiService3 = apiService2;
                                                            long j7 = j3;
                                                            String str3 = string;
                                                            j4 = j7;
                                                            sharedPreferences5 = sharedPreferences4;
                                                            if (response2.isSuccessful()) {
                                                            }
                                                            z2 = true;
                                                            i = 0;
                                                            if (i != 0) {
                                                                z3 = z2;
                                                            } else {
                                                                z3 = false;
                                                            }
                                                            return Boxing.boxBoolean(z3);
                                                        }
                                                    }
                                                }
                                            } else {
                                                if (r1 == 0 || j2 - lastProfilesSyncTime >= Duration.ofMinutes(8L).toMillis()) {
                                                    string = sharedPreferences3.getString("lastSyncTimestamp", null);
                                                    apiService = ApiClient.INSTANCE.getApiService();
                                                    if (string == null) {
                                                        c01351.L$0 = sharedPreferences3;
                                                        c01351.L$1 = SpillingKt.nullOutSpilledVariable(string);
                                                        c01351.L$2 = SpillingKt.nullOutSpilledVariable(apiService);
                                                        c01351.Z$0 = r1;
                                                        c01351.J$0 = j2;
                                                        c01351.I$0 = i == true ? 1 : 0;
                                                        c01351.label = 3;
                                                        allProfiles = apiService.getAllProfiles(c01351);
                                                        if (allProfiles == coroutine_suspended) {
                                                            apiService2 = apiService;
                                                            boostySubscribers = allProfiles;
                                                            sharedPreferences4 = sharedPreferences3;
                                                            r6 = r1;
                                                            i2 = i == true ? 1 : 0;
                                                            j3 = j2;
                                                            response = (Response) boostySubscribers;
                                                            r7 = r6;
                                                            response2 = response;
                                                            apiService3 = apiService2;
                                                            long j8 = j3;
                                                            String str4 = string;
                                                            j4 = j8;
                                                            sharedPreferences5 = sharedPreferences4;
                                                            if (response2.isSuccessful() || response2.body() == null) {
                                                                z2 = true;
                                                                i = 0;
                                                            } else {
                                                                Object objBody2 = response2.body();
                                                                Intrinsics.checkNotNull(objBody2);
                                                                list = (List) objBody2;
                                                                if (list.isEmpty()) {
                                                                    z2 = true;
                                                                    i = i2;
                                                                    SharedPreferences.Editor editorEdit = sharedPreferences5.edit();
                                                                    editorEdit.putString("lastSyncTimestamp", Instant.now().toString());
                                                                    editorEdit.apply();
                                                                    lastProfilesSyncTime = j4;
                                                                } else {
                                                                    ArrayList arrayList = new ArrayList();
                                                                    ArrayList arrayList2 = new ArrayList();
                                                                    Iterator it = list.iterator();
                                                                    while (it.hasNext()) {
                                                                        Iterator it2 = it;
                                                                        Object next = it2.next();
                                                                        String str5 = str4;
                                                                        z2 = true;
                                                                        try {
                                                                            if (Intrinsics.areEqual(((ProfileDTO) next).getDeleted(), Boxing.boxBoolean(true))) {
                                                                                arrayList.add(next);
                                                                            } else {
                                                                                arrayList2.add(next);
                                                                            }
                                                                            it = it2;
                                                                            str4 = str5;
                                                                        } catch (Throwable th) {
                                                                            th = th;
                                                                            FileLog.e(th);
                                                                            i = 0;
                                                                            if (i != 0) {
                                                                                z3 = z2;
                                                                            } else {
                                                                                z3 = false;
                                                                            }
                                                                            return Boxing.boxBoolean(z3);
                                                                        }
                                                                    }
                                                                    String str6 = str4;
                                                                    z2 = true;
                                                                    Pair pair = new Pair(arrayList, arrayList2);
                                                                    list2 = (List) pair.component1();
                                                                    list3 = (List) pair.component2();
                                                                    if (!list2.isEmpty()) {
                                                                        DatabaseHelper databaseHelper3 = DatabaseHelper.INSTANCE;
                                                                        List list4 = list2;
                                                                        ArrayList arrayList3 = new ArrayList(CollectionsKt.collectionSizeOrDefault(list4, 10));
                                                                        Iterator it3 = list4.iterator();
                                                                        while (it3.hasNext()) {
                                                                            arrayList3.add(Boxing.boxLong(((ProfileDTO) it3.next()).getId()));
                                                                        }
                                                                        c01351.L$0 = sharedPreferences5;
                                                                        c01351.L$1 = SpillingKt.nullOutSpilledVariable(str6);
                                                                        c01351.L$2 = SpillingKt.nullOutSpilledVariable(apiService3);
                                                                        c01351.L$3 = SpillingKt.nullOutSpilledVariable(response2);
                                                                        c01351.L$4 = SpillingKt.nullOutSpilledVariable(list);
                                                                        c01351.L$5 = SpillingKt.nullOutSpilledVariable(list2);
                                                                        c01351.L$6 = list3;
                                                                        c01351.Z$0 = r7;
                                                                        c01351.J$0 = j4;
                                                                        c01351.I$0 = i2;
                                                                        c01351.label = 5;
                                                                        if (databaseHelper3.deleteProfiles(arrayList3, c01351) != coroutine_suspended) {
                                                                            list2 = list2;
                                                                        }
                                                                    }
                                                                    str = str6;
                                                                    r8 = r7;
                                                                    if (list3.isEmpty()) {
                                                                        i = i2;
                                                                    } else {
                                                                        databaseHelper = DatabaseHelper.INSTANCE;
                                                                        c01351.L$0 = sharedPreferences5;
                                                                        c01351.L$1 = SpillingKt.nullOutSpilledVariable(str);
                                                                        c01351.L$2 = SpillingKt.nullOutSpilledVariable(apiService3);
                                                                        c01351.L$3 = SpillingKt.nullOutSpilledVariable(response2);
                                                                        c01351.L$4 = SpillingKt.nullOutSpilledVariable(list);
                                                                        c01351.L$5 = SpillingKt.nullOutSpilledVariable(list2);
                                                                        c01351.L$6 = SpillingKt.nullOutSpilledVariable(list3);
                                                                        c01351.Z$0 = r8;
                                                                        c01351.J$0 = j4;
                                                                        c01351.I$0 = i2;
                                                                        c01351.label = 6;
                                                                        if (databaseHelper.insertProfiles(list3, c01351) != coroutine_suspended) {
                                                                            sharedPreferences6 = sharedPreferences5;
                                                                            j5 = j4;
                                                                            i = i2;
                                                                            sharedPreferences5 = sharedPreferences6;
                                                                            j4 = j5;
                                                                        }
                                                                    }
                                                                    SharedPreferences.Editor editorEdit2 = sharedPreferences5.edit();
                                                                    editorEdit2.putString("lastSyncTimestamp", Instant.now().toString());
                                                                    editorEdit2.apply();
                                                                    lastProfilesSyncTime = j4;
                                                                }
                                                            }
                                                            break;
                                                        }
                                                    } else {
                                                        c01351.L$0 = sharedPreferences3;
                                                        c01351.L$1 = SpillingKt.nullOutSpilledVariable(string);
                                                        c01351.L$2 = SpillingKt.nullOutSpilledVariable(apiService);
                                                        c01351.Z$0 = r1;
                                                        c01351.J$0 = j2;
                                                        c01351.I$0 = i == true ? 1 : 0;
                                                        c01351.label = 4;
                                                        updates = apiService.getUpdates(string, c01351);
                                                        if (updates == coroutine_suspended) {
                                                            apiService2 = apiService;
                                                            boostySubscribers = updates;
                                                            sharedPreferences4 = sharedPreferences3;
                                                            r5 = r1;
                                                            i2 = i == true ? 1 : 0;
                                                            j3 = j2;
                                                            response = (Response) boostySubscribers;
                                                            r7 = r5;
                                                            response2 = response;
                                                            apiService3 = apiService2;
                                                            long j9 = j3;
                                                            String str7 = string;
                                                            j4 = j9;
                                                            sharedPreferences5 = sharedPreferences4;
                                                            if (response2.isSuccessful()) {
                                                            }
                                                            z2 = true;
                                                            i = 0;
                                                        }
                                                    }
                                                } else {
                                                    z2 = true;
                                                }
                                                if (i != 0) {
                                                    z3 = z2;
                                                } else {
                                                    z3 = false;
                                                }
                                                return Boxing.boxBoolean(z3);
                                            }
                                        } catch (Throwable th2) {
                                            th = th2;
                                            FileLog.e(th);
                                            r1 = r2;
                                        }
                                    }
                                } catch (Throwable th3) {
                                    th = th3;
                                    j2 = jCurrentTimeMillis;
                                    i = 1;
                                    FileLog.e(th);
                                    r1 = r2;
                                }
                            } catch (Throwable th4) {
                                th = th4;
                                sharedPreferences3 = sharedPreferences;
                            }
                        } else {
                            sharedPreferences3 = sharedPreferences;
                            j2 = jCurrentTimeMillis;
                            i = 1;
                            r1 = r2;
                            if (r1 == 0) {
                            }
                            string = sharedPreferences3.getString("lastSyncTimestamp", null);
                            apiService = ApiClient.INSTANCE.getApiService();
                            if (string == null) {
                                c01351.L$0 = sharedPreferences3;
                                c01351.L$1 = SpillingKt.nullOutSpilledVariable(string);
                                c01351.L$2 = SpillingKt.nullOutSpilledVariable(apiService);
                                c01351.Z$0 = r1;
                                c01351.J$0 = j2;
                                c01351.I$0 = i == true ? 1 : 0;
                                c01351.label = 3;
                                allProfiles = apiService.getAllProfiles(c01351);
                                if (allProfiles == coroutine_suspended) {
                                    apiService2 = apiService;
                                    boostySubscribers = allProfiles;
                                    sharedPreferences4 = sharedPreferences3;
                                    r6 = r1;
                                    i2 = i == true ? 1 : 0;
                                    j3 = j2;
                                    response = (Response) boostySubscribers;
                                    r7 = r6;
                                    response2 = response;
                                    apiService3 = apiService2;
                                    long j10 = j3;
                                    String str8 = string;
                                    j4 = j10;
                                    sharedPreferences5 = sharedPreferences4;
                                    if (response2.isSuccessful()) {
                                    }
                                    z2 = true;
                                    i = 0;
                                    if (i != 0) {
                                        z3 = z2;
                                    } else {
                                        z3 = false;
                                    }
                                    return Boxing.boxBoolean(z3);
                                }
                            } else {
                                c01351.L$0 = sharedPreferences3;
                                c01351.L$1 = SpillingKt.nullOutSpilledVariable(string);
                                c01351.L$2 = SpillingKt.nullOutSpilledVariable(apiService);
                                c01351.Z$0 = r1;
                                c01351.J$0 = j2;
                                c01351.I$0 = i == true ? 1 : 0;
                                c01351.label = 4;
                                updates = apiService.getUpdates(string, c01351);
                                if (updates == coroutine_suspended) {
                                    apiService2 = apiService;
                                    boostySubscribers = updates;
                                    sharedPreferences4 = sharedPreferences3;
                                    r5 = r1;
                                    i2 = i == true ? 1 : 0;
                                    j3 = j2;
                                    response = (Response) boostySubscribers;
                                    r7 = r5;
                                    response2 = response;
                                    apiService3 = apiService2;
                                    long j11 = j3;
                                    String str9 = string;
                                    j4 = j11;
                                    sharedPreferences5 = sharedPreferences4;
                                    if (response2.isSuccessful()) {
                                    }
                                    z2 = true;
                                    i = 0;
                                    if (i != 0) {
                                        z3 = z2;
                                    } else {
                                        z3 = false;
                                    }
                                    return Boxing.boxBoolean(z3);
                                }
                            }
                        }
                        return coroutine_suspended;
                    case 1:
                        int i5 = c01351.I$0;
                        long j12 = c01351.J$0;
                        boolean z4 = c01351.Z$0;
                        SharedPreferences sharedPreferences7 = (SharedPreferences) c01351.L$0;
                        ResultKt.throwOnFailure(boostySubscribers);
                        i = i5;
                        r2 = z4;
                        sharedPreferences3 = sharedPreferences7;
                        j2 = j12;
                        response3 = (Response) boostySubscribers;
                        r1 = r2;
                        if (!response3.isSuccessful()) {
                            if (r1 == 0) {
                                break;
                            }
                            string = sharedPreferences3.getString("lastSyncTimestamp", null);
                            apiService = ApiClient.INSTANCE.getApiService();
                            if (string == null) {
                                c01351.L$0 = sharedPreferences3;
                                c01351.L$1 = SpillingKt.nullOutSpilledVariable(string);
                                c01351.L$2 = SpillingKt.nullOutSpilledVariable(apiService);
                                c01351.Z$0 = r1;
                                c01351.J$0 = j2;
                                c01351.I$0 = i == true ? 1 : 0;
                                c01351.label = 3;
                                allProfiles = apiService.getAllProfiles(c01351);
                                if (allProfiles == coroutine_suspended) {
                                    apiService2 = apiService;
                                    boostySubscribers = allProfiles;
                                    sharedPreferences4 = sharedPreferences3;
                                    r6 = r1;
                                    i2 = i == true ? 1 : 0;
                                    j3 = j2;
                                    response = (Response) boostySubscribers;
                                    r7 = r6;
                                }
                            } else {
                                c01351.L$0 = sharedPreferences3;
                                c01351.L$1 = SpillingKt.nullOutSpilledVariable(string);
                                c01351.L$2 = SpillingKt.nullOutSpilledVariable(apiService);
                                c01351.Z$0 = r1;
                                c01351.J$0 = j2;
                                c01351.I$0 = i == true ? 1 : 0;
                                c01351.label = 4;
                                updates = apiService.getUpdates(string, c01351);
                                if (updates == coroutine_suspended) {
                                    apiService2 = apiService;
                                    boostySubscribers = updates;
                                    sharedPreferences4 = sharedPreferences3;
                                    r5 = r1;
                                    i2 = i == true ? 1 : 0;
                                    j3 = j2;
                                    response = (Response) boostySubscribers;
                                    r7 = r5;
                                }
                            }
                            return coroutine_suspended;
                        }
                        if (r1 == 0) {
                            break;
                        }
                        string = sharedPreferences3.getString("lastSyncTimestamp", null);
                        apiService = ApiClient.INSTANCE.getApiService();
                        if (string == null) {
                            c01351.L$0 = sharedPreferences3;
                            c01351.L$1 = SpillingKt.nullOutSpilledVariable(string);
                            c01351.L$2 = SpillingKt.nullOutSpilledVariable(apiService);
                            c01351.Z$0 = r1;
                            c01351.J$0 = j2;
                            c01351.I$0 = i == true ? 1 : 0;
                            c01351.label = 3;
                            allProfiles = apiService.getAllProfiles(c01351);
                            if (allProfiles == coroutine_suspended) {
                                apiService2 = apiService;
                                boostySubscribers = allProfiles;
                                sharedPreferences4 = sharedPreferences3;
                                r6 = r1;
                                i2 = i == true ? 1 : 0;
                                j3 = j2;
                                response = (Response) boostySubscribers;
                                r7 = r6;
                            }
                        } else {
                            c01351.L$0 = sharedPreferences3;
                            c01351.L$1 = SpillingKt.nullOutSpilledVariable(string);
                            c01351.L$2 = SpillingKt.nullOutSpilledVariable(apiService);
                            c01351.Z$0 = r1;
                            c01351.J$0 = j2;
                            c01351.I$0 = i == true ? 1 : 0;
                            c01351.label = 4;
                            updates = apiService.getUpdates(string, c01351);
                            if (updates == coroutine_suspended) {
                                apiService2 = apiService;
                                boostySubscribers = updates;
                                sharedPreferences4 = sharedPreferences3;
                                r5 = r1;
                                i2 = i == true ? 1 : 0;
                                j3 = j2;
                                response = (Response) boostySubscribers;
                                r7 = r5;
                            }
                        }
                        return coroutine_suspended;
                        response2 = response;
                        apiService3 = apiService2;
                        long j13 = j3;
                        String str10 = string;
                        j4 = j13;
                        sharedPreferences5 = sharedPreferences4;
                        if (response2.isSuccessful()) {
                            break;
                        }
                        z2 = true;
                        i = 0;
                        if (i != 0) {
                            z3 = z2;
                        } else {
                            z3 = false;
                        }
                        return Boxing.boxBoolean(z3);
                    case 2:
                        int i6 = c01351.I$0;
                        long j14 = c01351.J$0;
                        boolean z5 = c01351.Z$0;
                        SharedPreferences sharedPreferences8 = (SharedPreferences) c01351.L$0;
                        ResultKt.throwOnFailure(boostySubscribers);
                        i = i6;
                        r2 = z5;
                        sharedPreferences3 = sharedPreferences8;
                        j2 = j14;
                        r1 = r2;
                        r2 = r2;
                        lastBoostySyncTime = j2;
                        r1 = r2;
                        if (r1 == 0) {
                            break;
                        }
                        string = sharedPreferences3.getString("lastSyncTimestamp", null);
                        apiService = ApiClient.INSTANCE.getApiService();
                        if (string == null) {
                            c01351.L$0 = sharedPreferences3;
                            c01351.L$1 = SpillingKt.nullOutSpilledVariable(string);
                            c01351.L$2 = SpillingKt.nullOutSpilledVariable(apiService);
                            c01351.Z$0 = r1;
                            c01351.J$0 = j2;
                            c01351.I$0 = i == true ? 1 : 0;
                            c01351.label = 3;
                            allProfiles = apiService.getAllProfiles(c01351);
                            if (allProfiles == coroutine_suspended) {
                                apiService2 = apiService;
                                boostySubscribers = allProfiles;
                                sharedPreferences4 = sharedPreferences3;
                                r6 = r1;
                                i2 = i == true ? 1 : 0;
                                j3 = j2;
                                response = (Response) boostySubscribers;
                                r7 = r6;
                                response2 = response;
                                apiService3 = apiService2;
                                long j15 = j3;
                                String str11 = string;
                                j4 = j15;
                                sharedPreferences5 = sharedPreferences4;
                                if (response2.isSuccessful()) {
                                    break;
                                }
                                z2 = true;
                                i = 0;
                                if (i != 0) {
                                    z3 = z2;
                                } else {
                                    z3 = false;
                                }
                                return Boxing.boxBoolean(z3);
                            }
                        } else {
                            c01351.L$0 = sharedPreferences3;
                            c01351.L$1 = SpillingKt.nullOutSpilledVariable(string);
                            c01351.L$2 = SpillingKt.nullOutSpilledVariable(apiService);
                            c01351.Z$0 = r1;
                            c01351.J$0 = j2;
                            c01351.I$0 = i == true ? 1 : 0;
                            c01351.label = 4;
                            updates = apiService.getUpdates(string, c01351);
                            if (updates == coroutine_suspended) {
                                apiService2 = apiService;
                                boostySubscribers = updates;
                                sharedPreferences4 = sharedPreferences3;
                                r5 = r1;
                                i2 = i == true ? 1 : 0;
                                j3 = j2;
                                response = (Response) boostySubscribers;
                                r7 = r5;
                                response2 = response;
                                apiService3 = apiService2;
                                long j16 = j3;
                                String str12 = string;
                                j4 = j16;
                                sharedPreferences5 = sharedPreferences4;
                                if (response2.isSuccessful()) {
                                    break;
                                }
                                z2 = true;
                                i = 0;
                                if (i != 0) {
                                    z3 = z2;
                                } else {
                                    z3 = false;
                                }
                                return Boxing.boxBoolean(z3);
                            }
                        }
                        return coroutine_suspended;
                    case 3:
                        i2 = c01351.I$0;
                        j3 = c01351.J$0;
                        boolean z6 = c01351.Z$0;
                        apiService2 = (ApiService) c01351.L$2;
                        string = (String) c01351.L$1;
                        sharedPreferences4 = (SharedPreferences) c01351.L$0;
                        ResultKt.throwOnFailure(boostySubscribers);
                        r6 = z6;
                        response = (Response) boostySubscribers;
                        r7 = r6;
                        response2 = response;
                        apiService3 = apiService2;
                        long j17 = j3;
                        String str13 = string;
                        j4 = j17;
                        sharedPreferences5 = sharedPreferences4;
                        if (response2.isSuccessful()) {
                            break;
                        }
                        z2 = true;
                        i = 0;
                        if (i != 0) {
                            z3 = z2;
                        } else {
                            z3 = false;
                        }
                        return Boxing.boxBoolean(z3);
                    case 4:
                        i2 = c01351.I$0;
                        j3 = c01351.J$0;
                        boolean z7 = c01351.Z$0;
                        apiService2 = (ApiService) c01351.L$2;
                        string = (String) c01351.L$1;
                        sharedPreferences4 = (SharedPreferences) c01351.L$0;
                        ResultKt.throwOnFailure(boostySubscribers);
                        r5 = z7;
                        response = (Response) boostySubscribers;
                        r7 = r5;
                        response2 = response;
                        apiService3 = apiService2;
                        long j18 = j3;
                        String str14 = string;
                        j4 = j18;
                        sharedPreferences5 = sharedPreferences4;
                        if (response2.isSuccessful()) {
                            break;
                        }
                        z2 = true;
                        i = 0;
                        if (i != 0) {
                            z3 = z2;
                        } else {
                            z3 = false;
                        }
                        return Boxing.boxBoolean(z3);
                    case 5:
                        i2 = c01351.I$0;
                        j4 = c01351.J$0;
                        boolean z8 = c01351.Z$0;
                        list3 = (List) c01351.L$6;
                        list2 = (List) c01351.L$5;
                        list = (List) c01351.L$4;
                        response2 = (Response) c01351.L$3;
                        apiService3 = (ApiService) c01351.L$2;
                        str = (String) c01351.L$1;
                        sharedPreferences5 = (SharedPreferences) c01351.L$0;
                        ResultKt.throwOnFailure(boostySubscribers);
                        z2 = true;
                        r8 = z8;
                        if (list3.isEmpty()) {
                            databaseHelper = DatabaseHelper.INSTANCE;
                            c01351.L$0 = sharedPreferences5;
                            c01351.L$1 = SpillingKt.nullOutSpilledVariable(str);
                            c01351.L$2 = SpillingKt.nullOutSpilledVariable(apiService3);
                            c01351.L$3 = SpillingKt.nullOutSpilledVariable(response2);
                            c01351.L$4 = SpillingKt.nullOutSpilledVariable(list);
                            c01351.L$5 = SpillingKt.nullOutSpilledVariable(list2);
                            c01351.L$6 = SpillingKt.nullOutSpilledVariable(list3);
                            c01351.Z$0 = r8;
                            c01351.J$0 = j4;
                            c01351.I$0 = i2;
                            c01351.label = 6;
                            if (databaseHelper.insertProfiles(list3, c01351) != coroutine_suspended) {
                                sharedPreferences6 = sharedPreferences5;
                                j5 = j4;
                                i = i2;
                                sharedPreferences5 = sharedPreferences6;
                                j4 = j5;
                            }
                            return coroutine_suspended;
                        }
                        i = i2;
                        SharedPreferences.Editor editorEdit3 = sharedPreferences5.edit();
                        editorEdit3.putString("lastSyncTimestamp", Instant.now().toString());
                        editorEdit3.apply();
                        lastProfilesSyncTime = j4;
                        if (i != 0) {
                            z3 = z2;
                        } else {
                            z3 = false;
                        }
                        return Boxing.boxBoolean(z3);
                    case 6:
                        i2 = c01351.I$0;
                        j5 = c01351.J$0;
                        sharedPreferences6 = (SharedPreferences) c01351.L$0;
                        ResultKt.throwOnFailure(boostySubscribers);
                        z2 = true;
                        i = i2;
                        sharedPreferences5 = sharedPreferences6;
                        j4 = j5;
                        SharedPreferences.Editor editorEdit4 = sharedPreferences5.edit();
                        editorEdit4.putString("lastSyncTimestamp", Instant.now().toString());
                        editorEdit4.apply();
                        lastProfilesSyncTime = j4;
                        if (i != 0) {
                            z3 = z2;
                        } else {
                            z3 = false;
                        }
                        return Boxing.boxBoolean(z3);
                    default:
                        throw new IllegalStateException("call to 'resume' before 'invoke' with coroutine");
                }
            } catch (Throwable th5) {
                th = th5;
                i = r2 == true ? 1 : 0;
                r2 = i4;
                sharedPreferences3 = sharedPreferences2;
                j2 = j;
            }
        } catch (Throwable th6) {
            th = th6;
            z2 = true;
            FileLog.e(th);
        }
    }

    public static final void scheduleWorker() {
        PeriodicWorkRequest periodicWorkRequest = (PeriodicWorkRequest) ((PeriodicWorkRequest.Builder) new PeriodicWorkRequest.Builder(SyncWorker.class, 1L, TimeUnit.HOURS).setConstraints(new Constraints.Builder().setRequiredNetworkType(NetworkType.CONNECTED).build())).build();
        WorkManager.Companion companion = WorkManager.Companion;
        Context applicationContext = ApplicationLoader.applicationContext;
        Intrinsics.checkNotNullExpressionValue(applicationContext, "applicationContext");
        companion.getInstance(applicationContext).enqueueUniquePeriodicWork("api_sync_work", ExistingPeriodicWorkPolicy.UPDATE, periodicWorkRequest);
    }
}
