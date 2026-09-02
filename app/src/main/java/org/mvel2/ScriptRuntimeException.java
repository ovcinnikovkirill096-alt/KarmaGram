package org.mvel2;

public class ScriptRuntimeException extends RuntimeException {
    public ScriptRuntimeException() {
    }

    public ScriptRuntimeException(String str) {
        super(str);
    }

    public ScriptRuntimeException(String str, Throwable th) {
        super(str, th);
    }

    public ScriptRuntimeException(Throwable th) {
        super(th);
    }
}
