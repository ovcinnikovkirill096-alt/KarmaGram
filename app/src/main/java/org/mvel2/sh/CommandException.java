package org.mvel2.sh;

public class CommandException extends RuntimeException {
    public CommandException() {
    }

    public CommandException(String str) {
        super(str);
    }

    public CommandException(String str, Throwable th) {
        super(str, th);
    }

    public CommandException(Throwable th) {
        super(th);
    }
}
