package okhttp3.internal;

import com.google.android.exoplayer2.mediacodec.AsynchronousMediaCodecBufferEnqueuer$$ExternalSyntheticBackportWithForwarding1;
import java.util.concurrent.atomic.AtomicReference;
import kotlin.jvm.functions.Function0;
import kotlin.jvm.internal.Intrinsics;
import kotlin.reflect.KClass;

public final class TagsKt {
    public static final <T> T computeIfAbsent(AtomicReference<Tags> atomicReference, KClass type, Function0<? extends T> compute) {
        Tags tags;
        Intrinsics.checkNotNullParameter(atomicReference, "<this>");
        Intrinsics.checkNotNullParameter(type, "type");
        Intrinsics.checkNotNullParameter(compute, "compute");
        T tInvoke = null;
        do {
            tags = atomicReference.get();
            T t = (T) tags.get(type);
            if (t != null) {
                return t;
            }
            if (tInvoke == null) {
                tInvoke = compute.invoke();
            }
        } while (!AsynchronousMediaCodecBufferEnqueuer$$ExternalSyntheticBackportWithForwarding1.m(atomicReference, tags, tags.plus(type, tInvoke)));
        return tInvoke;
    }
}
