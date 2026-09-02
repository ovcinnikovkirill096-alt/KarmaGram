package com.sun.jna;

import java.lang.reflect.Field;

public class StructureWriteContext extends ToNativeContext {
    private Field field;
    private Structure struct;

    StructureWriteContext(Structure structure, Field field) {
        this.struct = structure;
        this.field = field;
    }

    public Structure getStructure() {
        return this.struct;
    }

    public Field getField() {
        return this.field;
    }
}
