package org.mvel2.optimizers;

public class OptimizationNotSupported extends RuntimeException {
    public OptimizationNotSupported() {
    }

    public OptimizationNotSupported(String str) {
        super(str);
    }

    public OptimizationNotSupported(String str, Throwable th) {
        super(str, th);
    }

    public OptimizationNotSupported(Throwable th) {
        super(th);
    }
}
