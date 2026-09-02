package org.mvel2.compiler;

public class EndWithValue extends RuntimeException {
    private Object value;

    public EndWithValue(Object obj) {
        this.value = obj;
    }

    public Object getValue() {
        return this.value;
    }
}
