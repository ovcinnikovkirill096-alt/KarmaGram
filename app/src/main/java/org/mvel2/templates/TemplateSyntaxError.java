package org.mvel2.templates;

public class TemplateSyntaxError extends RuntimeException {
    public TemplateSyntaxError() {
    }

    public TemplateSyntaxError(String str) {
        super(str);
    }

    public TemplateSyntaxError(String str, Throwable th) {
        super(str, th);
    }

    public TemplateSyntaxError(Throwable th) {
        super(th);
    }
}
