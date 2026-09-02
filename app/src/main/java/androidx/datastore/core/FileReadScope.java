package androidx.datastore.core;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.concurrent.atomic.AtomicBoolean;
import kotlin.ResultKt;
import kotlin.coroutines.Continuation;
import kotlin.coroutines.intrinsics.IntrinsicsKt;
import kotlin.coroutines.jvm.internal.ContinuationImpl;
import kotlin.io.CloseableKt;
import kotlin.jvm.internal.Intrinsics;

public class FileReadScope implements ReadScope {
    private final AtomicBoolean closed;
    private final File file;
    private final Serializer serializer;

    /* JADX INFO: renamed from: androidx.datastore.core.FileReadScope$readData$1, reason: invalid class name */
    static final class AnonymousClass1 extends ContinuationImpl {
        Object L$0;
        Object L$1;
        int label;
        /* synthetic */ Object result;

        AnonymousClass1(Continuation continuation) {
            super(continuation);
        }

        @Override // kotlin.coroutines.jvm.internal.BaseContinuationImpl
        public final Object invokeSuspend(Object obj) {
            this.result = obj;
            this.label |= Integer.MIN_VALUE;
            return FileReadScope.readData$suspendImpl(FileReadScope.this, this);
        }
    }

    @Override // androidx.datastore.core.ReadScope
    public Object readData(Continuation continuation) {
        return readData$suspendImpl(this, continuation);
    }

    public FileReadScope(File file, Serializer serializer) {
        Intrinsics.checkNotNullParameter(file, "file");
        Intrinsics.checkNotNullParameter(serializer, "serializer");
        this.file = file;
        this.serializer = serializer;
        this.closed = new AtomicBoolean(false);
    }

    protected final File getFile() {
        return this.file;
    }

    protected final Serializer getSerializer() {
        return this.serializer;
    }

    /* JADX WARN: Code duplicated, block: B:7:0x0013  */
    /* JADX WARN: Multi-variable type inference failed */
    /* JADX WARN: Type inference failed for: r2v0, types: [int] */
    /* JADX WARN: Type inference failed for: r2v1 */
    /* JADX WARN: Type inference failed for: r2v11, types: [androidx.datastore.core.FileReadScope] */
    /* JADX WARN: Type inference failed for: r2v5 */
    /* JADX WARN: Type inference failed for: r2v9 */
    /* JADX WARN: Type inference failed for: r7v0, types: [androidx.datastore.core.FileReadScope, java.lang.Object] */
    /* JADX WARN: Type inference failed for: r7v1 */
    /* JADX WARN: Type inference failed for: r7v2, types: [androidx.datastore.core.FileReadScope] */
    static /* synthetic */ Object readData$suspendImpl(FileReadScope fileReadScope, Continuation continuation) {
        AnonymousClass1 anonymousClass1;
        Throwable th;
        java.io.Closeable closeable;
        java.io.Closeable closeable2;
        Throwable th2;
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
        ?? r2 = anonymousClass1.label;
        try {
            if (r2 != 0) {
                if (r2 == 1) {
                    closeable2 = (java.io.Closeable) anonymousClass1.L$1;
                    r2 = (FileReadScope) anonymousClass1.L$0;
                    try {
                        ResultKt.throwOnFailure(obj);
                        CloseableKt.closeFinally(closeable2, null);
                        return obj;
                    } catch (Throwable th3) {
                        th2 = th3;
                        try {
                            throw th2;
                        } catch (Throwable th4) {
                            CloseableKt.closeFinally(closeable2, th2);
                            throw th4;
                        }
                    }
                }
                if (r2 != 2) {
                    throw new IllegalStateException("call to 'resume' before 'invoke' with coroutine");
                }
                closeable = (java.io.Closeable) anonymousClass1.L$0;
                try {
                    ResultKt.throwOnFailure(obj);
                    CloseableKt.closeFinally(closeable, null);
                    return obj;
                } catch (Throwable th5) {
                    th = th5;
                    try {
                        throw th;
                    } catch (Throwable th6) {
                        CloseableKt.closeFinally(closeable, th);
                        throw th6;
                    }
                }
            }
            ResultKt.throwOnFailure(obj);
            fileReadScope.checkNotClosed();
            try {
                FileInputStream fileInputStream = new FileInputStream(((FileReadScope) fileReadScope).file);
                try {
                    Serializer serializer = ((FileReadScope) fileReadScope).serializer;
                    anonymousClass1.L$0 = fileReadScope;
                    anonymousClass1.L$1 = fileInputStream;
                    anonymousClass1.label = 1;
                    Object from = serializer.readFrom(fileInputStream, anonymousClass1);
                    if (from != coroutine_suspended) {
                        closeable2 = fileInputStream;
                        obj = from;
                        CloseableKt.closeFinally(closeable2, null);
                        return obj;
                    }
                } catch (Throwable th7) {
                    r2 = fileReadScope;
                    closeable2 = fileInputStream;
                    th2 = th7;
                    throw th2;
                }
            } catch (FileNotFoundException unused) {
                if (((FileReadScope) fileReadScope).file.exists()) {
                    FileInputStream fileInputStream2 = new FileInputStream(((FileReadScope) fileReadScope).file);
                    try {
                        Serializer serializer2 = ((FileReadScope) fileReadScope).serializer;
                        anonymousClass1.L$0 = fileInputStream2;
                        anonymousClass1.L$1 = null;
                        anonymousClass1.label = 2;
                        Object from2 = serializer2.readFrom(fileInputStream2, anonymousClass1);
                        if (from2 != coroutine_suspended) {
                            obj = from2;
                            closeable = fileInputStream2;
                            CloseableKt.closeFinally(closeable, null);
                            return obj;
                        }
                    } catch (Throwable th8) {
                        th = th8;
                        closeable = fileInputStream2;
                        throw th;
                    }
                } else {
                    return ((FileReadScope) fileReadScope).serializer.getDefaultValue();
                }
            }
            return coroutine_suspended;
        } catch (FileNotFoundException unused2) {
            fileReadScope = r2;
        }
    }

    @Override // androidx.datastore.core.Closeable
    public void close() {
        this.closed.set(true);
    }

    protected final void checkNotClosed() {
        if (this.closed.get()) {
            throw new IllegalStateException("This scope has already been closed.");
        }
    }
}
