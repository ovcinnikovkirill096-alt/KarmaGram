package com.exteragram.messenger.export.output.json;

import j$.util.function.Function$CC;
import java.util.function.Function;

public final /* synthetic */ class JsonContext$$ExternalSyntheticLambda17 implements Function {
    public /* synthetic */ Function andThen(Function function) {
        return Function$CC.$default$andThen(this, function);
    }

    @Override // java.util.function.Function
    public final Object apply(Object obj) {
        return String.valueOf((Long) obj);
    }

    public /* synthetic */ Function compose(Function function) {
        return Function$CC.$default$compose(this, function);
    }
}
