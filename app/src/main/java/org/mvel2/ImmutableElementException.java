package org.mvel2;

public class ImmutableElementException extends RuntimeException {
    public ImmutableElementException() {
    }

    public ImmutableElementException(String str) {
        super(str);
    }

    public ImmutableElementException(String str, Throwable th) {
        super(str, th);
    }

    public ImmutableElementException(Throwable th) {
        super(th);
    }
}
