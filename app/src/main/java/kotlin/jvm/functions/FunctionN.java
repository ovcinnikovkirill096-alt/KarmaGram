package kotlin.jvm.functions;

import kotlin.Function;
import kotlin.jvm.internal.FunctionBase;

public interface FunctionN<R> extends Function, FunctionBase<R> {
    @Override // kotlin.jvm.internal.FunctionBase
    int getArity();

    R invoke(Object... objArr);
}
