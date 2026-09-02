package kotlinx.serialization.json.internal;

import kotlin.Result;
import kotlin.ResultKt;
import kotlin.text.StringsKt;
import org.telegram.tgnet.TLObject;

public abstract class ArrayPoolsKt {
    private static final int MAX_CHARS_IN_POOL;

    static {
        Object objM2437constructorimpl;
        try {
            Result.Companion companion = Result.Companion;
            String property = System.getProperty("kotlinx.serialization.json.pool.size");
            objM2437constructorimpl = Result.m2437constructorimpl(property != null ? StringsKt.toIntOrNull(property) : null);
        } catch (Throwable th) {
            Result.Companion companion2 = Result.Companion;
            objM2437constructorimpl = Result.m2437constructorimpl(ResultKt.createFailure(th));
        }
        Integer num = (Integer) (Result.m2441isFailureimpl(objM2437constructorimpl) ? null : objM2437constructorimpl);
        MAX_CHARS_IN_POOL = num != null ? num.intValue() : TLObject.FLAG_21;
    }
}
