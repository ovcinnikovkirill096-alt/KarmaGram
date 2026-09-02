package androidx.camera.camera2.pipe.compat;

import kotlin.Unit;
import kotlin.coroutines.Continuation;
import kotlin.coroutines.jvm.internal.SuspendLambda;
import kotlin.jvm.functions.Function2;
import kotlinx.coroutines.CoroutineScope;

final class CameraStateOpener$tryOpenCamera$2 extends SuspendLambda implements Function2 {
    final /* synthetic */ String $cameraId;
    final /* synthetic */ AndroidCameraState $cameraState;
    private /* synthetic */ Object L$0;
    Object L$1;
    Object L$2;
    Object L$3;
    Object L$4;
    int label;
    final /* synthetic */ CameraStateOpener this$0;

    /* JADX WARN: 'super' call moved to the top of the method (can break code semantics) */
    CameraStateOpener$tryOpenCamera$2(CameraStateOpener cameraStateOpener, String str, AndroidCameraState androidCameraState, Continuation continuation) {
        super(2, continuation);
        this.this$0 = cameraStateOpener;
        this.$cameraId = str;
        this.$cameraState = androidCameraState;
    }

    @Override // kotlin.coroutines.jvm.internal.BaseContinuationImpl
    public final Continuation create(Object obj, Continuation continuation) {
        CameraStateOpener$tryOpenCamera$2 cameraStateOpener$tryOpenCamera$2 = new CameraStateOpener$tryOpenCamera$2(this.this$0, this.$cameraId, this.$cameraState, continuation);
        cameraStateOpener$tryOpenCamera$2.L$0 = obj;
        return cameraStateOpener$tryOpenCamera$2;
    }

    @Override // kotlin.jvm.functions.Function2
    public final Object invoke(CoroutineScope coroutineScope, Continuation continuation) {
        return ((CameraStateOpener$tryOpenCamera$2) create(coroutineScope, continuation)).invokeSuspend(Unit.INSTANCE);
    }

    /* JADX WARN: Code duplicated, block: B:15:0x0097 A[Catch: all -> 0x0027, TRY_ENTER, TryCatch #0 {all -> 0x0027, blocks: (B:6:0x0022, B:30:0x00ff, B:15:0x0097, B:17:0x00aa, B:18:0x00b6, B:20:0x00bc, B:21:0x00c8, B:23:0x00ce, B:24:0x00da, B:26:0x00e0, B:27:0x00ec, B:32:0x0103, B:34:0x010b, B:35:0x011f, B:37:0x0125, B:38:0x0128, B:40:0x012e, B:41:0x0131, B:43:0x0137, B:44:0x013a, B:46:0x0140), top: B:54:0x0022 }] */
    /* JADX WARN: Code duplicated, block: B:17:0x00aa A[Catch: all -> 0x0027, TryCatch #0 {all -> 0x0027, blocks: (B:6:0x0022, B:30:0x00ff, B:15:0x0097, B:17:0x00aa, B:18:0x00b6, B:20:0x00bc, B:21:0x00c8, B:23:0x00ce, B:24:0x00da, B:26:0x00e0, B:27:0x00ec, B:32:0x0103, B:34:0x010b, B:35:0x011f, B:37:0x0125, B:38:0x0128, B:40:0x012e, B:41:0x0131, B:43:0x0137, B:44:0x013a, B:46:0x0140), top: B:54:0x0022 }] */
    /* JADX WARN: Code duplicated, block: B:20:0x00bc A[Catch: all -> 0x0027, TryCatch #0 {all -> 0x0027, blocks: (B:6:0x0022, B:30:0x00ff, B:15:0x0097, B:17:0x00aa, B:18:0x00b6, B:20:0x00bc, B:21:0x00c8, B:23:0x00ce, B:24:0x00da, B:26:0x00e0, B:27:0x00ec, B:32:0x0103, B:34:0x010b, B:35:0x011f, B:37:0x0125, B:38:0x0128, B:40:0x012e, B:41:0x0131, B:43:0x0137, B:44:0x013a, B:46:0x0140), top: B:54:0x0022 }] */
    /* JADX WARN: Code duplicated, block: B:23:0x00ce A[Catch: all -> 0x0027, TryCatch #0 {all -> 0x0027, blocks: (B:6:0x0022, B:30:0x00ff, B:15:0x0097, B:17:0x00aa, B:18:0x00b6, B:20:0x00bc, B:21:0x00c8, B:23:0x00ce, B:24:0x00da, B:26:0x00e0, B:27:0x00ec, B:32:0x0103, B:34:0x010b, B:35:0x011f, B:37:0x0125, B:38:0x0128, B:40:0x012e, B:41:0x0131, B:43:0x0137, B:44:0x013a, B:46:0x0140), top: B:54:0x0022 }] */
    /* JADX WARN: Code duplicated, block: B:26:0x00e0 A[Catch: all -> 0x0027, TryCatch #0 {all -> 0x0027, blocks: (B:6:0x0022, B:30:0x00ff, B:15:0x0097, B:17:0x00aa, B:18:0x00b6, B:20:0x00bc, B:21:0x00c8, B:23:0x00ce, B:24:0x00da, B:26:0x00e0, B:27:0x00ec, B:32:0x0103, B:34:0x010b, B:35:0x011f, B:37:0x0125, B:38:0x0128, B:40:0x012e, B:41:0x0131, B:43:0x0137, B:44:0x013a, B:46:0x0140), top: B:54:0x0022 }] */
    /* JADX WARN: Code duplicated, block: B:29:0x00fe A[RETURN] */
    /* JADX WARN: Code duplicated, block: B:32:0x0103 A[Catch: all -> 0x0027, TryCatch #0 {all -> 0x0027, blocks: (B:6:0x0022, B:30:0x00ff, B:15:0x0097, B:17:0x00aa, B:18:0x00b6, B:20:0x00bc, B:21:0x00c8, B:23:0x00ce, B:24:0x00da, B:26:0x00e0, B:27:0x00ec, B:32:0x0103, B:34:0x010b, B:35:0x011f, B:37:0x0125, B:38:0x0128, B:40:0x012e, B:41:0x0131, B:43:0x0137, B:44:0x013a, B:46:0x0140), top: B:54:0x0022 }] */
    /* JADX WARN: Code duplicated, block: B:34:0x010b A[Catch: all -> 0x0027, TryCatch #0 {all -> 0x0027, blocks: (B:6:0x0022, B:30:0x00ff, B:15:0x0097, B:17:0x00aa, B:18:0x00b6, B:20:0x00bc, B:21:0x00c8, B:23:0x00ce, B:24:0x00da, B:26:0x00e0, B:27:0x00ec, B:32:0x0103, B:34:0x010b, B:35:0x011f, B:37:0x0125, B:38:0x0128, B:40:0x012e, B:41:0x0131, B:43:0x0137, B:44:0x013a, B:46:0x0140), top: B:54:0x0022 }] */
    /* JADX WARN: Code duplicated, block: B:37:0x0125 A[Catch: all -> 0x0027, TryCatch #0 {all -> 0x0027, blocks: (B:6:0x0022, B:30:0x00ff, B:15:0x0097, B:17:0x00aa, B:18:0x00b6, B:20:0x00bc, B:21:0x00c8, B:23:0x00ce, B:24:0x00da, B:26:0x00e0, B:27:0x00ec, B:32:0x0103, B:34:0x010b, B:35:0x011f, B:37:0x0125, B:38:0x0128, B:40:0x012e, B:41:0x0131, B:43:0x0137, B:44:0x013a, B:46:0x0140), top: B:54:0x0022 }] */
    /* JADX WARN: Code duplicated, block: B:40:0x012e A[Catch: all -> 0x0027, TryCatch #0 {all -> 0x0027, blocks: (B:6:0x0022, B:30:0x00ff, B:15:0x0097, B:17:0x00aa, B:18:0x00b6, B:20:0x00bc, B:21:0x00c8, B:23:0x00ce, B:24:0x00da, B:26:0x00e0, B:27:0x00ec, B:32:0x0103, B:34:0x010b, B:35:0x011f, B:37:0x0125, B:38:0x0128, B:40:0x012e, B:41:0x0131, B:43:0x0137, B:44:0x013a, B:46:0x0140), top: B:54:0x0022 }] */
    /* JADX WARN: Code duplicated, block: B:43:0x0137 A[Catch: all -> 0x0027, TryCatch #0 {all -> 0x0027, blocks: (B:6:0x0022, B:30:0x00ff, B:15:0x0097, B:17:0x00aa, B:18:0x00b6, B:20:0x00bc, B:21:0x00c8, B:23:0x00ce, B:24:0x00da, B:26:0x00e0, B:27:0x00ec, B:32:0x0103, B:34:0x010b, B:35:0x011f, B:37:0x0125, B:38:0x0128, B:40:0x012e, B:41:0x0131, B:43:0x0137, B:44:0x013a, B:46:0x0140), top: B:54:0x0022 }] */
    /* JADX WARN: Code duplicated, block: B:46:0x0140 A[Catch: all -> 0x0027, TRY_LEAVE, TryCatch #0 {all -> 0x0027, blocks: (B:6:0x0022, B:30:0x00ff, B:15:0x0097, B:17:0x00aa, B:18:0x00b6, B:20:0x00bc, B:21:0x00c8, B:23:0x00ce, B:24:0x00da, B:26:0x00e0, B:27:0x00ec, B:32:0x0103, B:34:0x010b, B:35:0x011f, B:37:0x0125, B:38:0x0128, B:40:0x012e, B:41:0x0131, B:43:0x0137, B:44:0x013a, B:46:0x0140), top: B:54:0x0022 }] */
    /* JADX WARN: Unsupported multi-entry loop pattern (BACK_EDGE: B:28:0x00fc -> B:30:0x00ff). Please report as a decompilation issue!!! */
    /*  JADX ERROR: JadxOverflowException in pass: RegionMakerVisitor
        jadx.core.utils.exceptions.JadxOverflowException: Regions count limit reached at block B:32:0x0103
        	at jadx.core.utils.ErrorsCounter.addError(ErrorsCounter.java:59)
        	at jadx.core.utils.ErrorsCounter.error(ErrorsCounter.java:31)
        	at jadx.core.dex.attributes.nodes.NotificationAttrNode.addError(NotificationAttrNode.java:19)
        */
    @Override // kotlin.coroutines.jvm.internal.BaseContinuationImpl
    public final java.lang.Object invokeSuspend(java.lang.Object r14) {
        /*
            Method dump skipped, instruction units count: 354
            To view this dump add '--comments-level debug' option
        */
        throw new UnsupportedOperationException("Method not decompiled: androidx.camera.camera2.pipe.compat.CameraStateOpener$tryOpenCamera$2.invokeSuspend(java.lang.Object):java.lang.Object");
    }
}
