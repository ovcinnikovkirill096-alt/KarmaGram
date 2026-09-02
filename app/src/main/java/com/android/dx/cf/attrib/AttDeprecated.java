package com.android.dx.cf.attrib;

public final class AttDeprecated extends BaseAttribute {
    public static final String ATTRIBUTE_NAME = "Deprecated";

    @Override // com.android.dx.cf.iface.Attribute
    public int byteLength() {
        return 6;
    }

    public AttDeprecated() {
        super(ATTRIBUTE_NAME);
    }
}
