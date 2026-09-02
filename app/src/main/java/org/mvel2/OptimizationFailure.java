package org.mvel2;

public class OptimizationFailure extends RuntimeException {
    public OptimizationFailure() {
    }

    public OptimizationFailure(String str) {
        super(str);
    }

    public OptimizationFailure(String str, Throwable th) {
        super(str, th);
    }

    public OptimizationFailure(Throwable th) {
        super(th);
    }
}
