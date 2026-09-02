package androidx.datastore.preferences.protobuf;

import androidx.appcompat.app.WindowDecorActionBar$$ExternalSyntheticThrowCCEIfNotNull0;
import java.lang.reflect.Field;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import org.telegram.messenger.CharacterCompat;
import sun.misc.Unsafe;

final class MessageSchema implements Schema {
    private static final int[] EMPTY_INT_ARRAY = new int[0];
    private static final Unsafe UNSAFE = UnsafeUtil.getUnsafe();
    private final int[] buffer;
    private final int checkInitializedCount;
    private final MessageLite defaultInstance;
    private final ExtensionSchema extensionSchema;
    private final boolean hasExtensions;
    private final int[] intArray;
    private final ListFieldSchema listFieldSchema;
    private final boolean lite;
    private final MapFieldSchema mapFieldSchema;
    private final int maxFieldNumber;
    private final int minFieldNumber;
    private final NewInstanceSchema newInstanceSchema;
    private final Object[] objects;
    private final int repeatedFieldOffsetStart;
    private final ProtoSyntax syntax;
    private final UnknownFieldSchema unknownFieldSchema;
    private final boolean useCachedSizeField;

    private static boolean isEnforceUtf8(int i) {
        return (i & 536870912) != 0;
    }

    private static boolean isRequired(int i) {
        return (i & 268435456) != 0;
    }

    private static long offset(int i) {
        return i & 1048575;
    }

    private static int type(int i) {
        return (i & 267386880) >>> 20;
    }

    private MessageSchema(int[] iArr, Object[] objArr, int i, int i2, MessageLite messageLite, ProtoSyntax protoSyntax, boolean z, int[] iArr2, int i3, int i4, NewInstanceSchema newInstanceSchema, ListFieldSchema listFieldSchema, UnknownFieldSchema unknownFieldSchema, ExtensionSchema extensionSchema, MapFieldSchema mapFieldSchema) {
        this.buffer = iArr;
        this.objects = objArr;
        this.minFieldNumber = i;
        this.maxFieldNumber = i2;
        this.lite = messageLite instanceof GeneratedMessageLite;
        this.syntax = protoSyntax;
        this.hasExtensions = extensionSchema != null && extensionSchema.hasExtensions(messageLite);
        this.useCachedSizeField = z;
        this.intArray = iArr2;
        this.checkInitializedCount = i3;
        this.repeatedFieldOffsetStart = i4;
        this.newInstanceSchema = newInstanceSchema;
        this.listFieldSchema = listFieldSchema;
        this.unknownFieldSchema = unknownFieldSchema;
        this.extensionSchema = extensionSchema;
        this.defaultInstance = messageLite;
        this.mapFieldSchema = mapFieldSchema;
    }

    static MessageSchema newSchema(Class cls, MessageInfo messageInfo, NewInstanceSchema newInstanceSchema, ListFieldSchema listFieldSchema, UnknownFieldSchema unknownFieldSchema, ExtensionSchema extensionSchema, MapFieldSchema mapFieldSchema) {
        if (messageInfo instanceof RawMessageInfo) {
            return newSchemaForRawMessageInfo((RawMessageInfo) messageInfo, newInstanceSchema, listFieldSchema, unknownFieldSchema, extensionSchema, mapFieldSchema);
        }
        WindowDecorActionBar$$ExternalSyntheticThrowCCEIfNotNull0.m(messageInfo);
        return newSchemaForMessageInfo(null, newInstanceSchema, listFieldSchema, unknownFieldSchema, extensionSchema, mapFieldSchema);
    }

    /* JADX WARN: Code duplicated, block: B:121:0x0251  */
    /* JADX WARN: Code duplicated, block: B:122:0x0254  */
    /* JADX WARN: Code duplicated, block: B:125:0x026b  */
    /* JADX WARN: Code duplicated, block: B:126:0x026e  */
    /* JADX WARN: Code duplicated, block: B:163:0x0326  */
    /* JADX WARN: Code duplicated, block: B:180:0x0375  */
    /* JADX WARN: Code duplicated, block: B:183:0x0383  */
    static MessageSchema newSchemaForRawMessageInfo(RawMessageInfo rawMessageInfo, NewInstanceSchema newInstanceSchema, ListFieldSchema listFieldSchema, UnknownFieldSchema unknownFieldSchema, ExtensionSchema extensionSchema, MapFieldSchema mapFieldSchema) {
        int i;
        int iCharAt;
        int i2;
        int i3;
        int i4;
        int i5;
        int[] iArr;
        int i6;
        int i7;
        int i8;
        char cCharAt;
        int i9;
        char cCharAt2;
        int i10;
        char cCharAt3;
        int i11;
        char cCharAt4;
        int i12;
        char cCharAt5;
        int i13;
        char cCharAt6;
        int i14;
        char cCharAt7;
        int i15;
        char cCharAt8;
        int i16;
        int i17;
        int i18;
        int i19;
        int iObjectFieldOffset;
        int iObjectFieldOffset2;
        int i20;
        int i21;
        int iObjectFieldOffset3;
        int i22;
        Field fieldReflectField;
        char cCharAt9;
        int i23;
        int i24;
        int i25;
        Object obj;
        Field fieldReflectField2;
        int i26;
        Object obj2;
        Field fieldReflectField3;
        int i27;
        char cCharAt10;
        int i28;
        char cCharAt11;
        int i29;
        char cCharAt12;
        int i30;
        char cCharAt13;
        String stringInfo = rawMessageInfo.getStringInfo();
        int length = stringInfo.length();
        char cCharAt14 = stringInfo.charAt(0);
        char c = CharacterCompat.MIN_HIGH_SURROGATE;
        if (cCharAt14 >= 55296) {
            int i31 = 1;
            while (true) {
                i = i31 + 1;
                if (stringInfo.charAt(i31) < 55296) {
                    break;
                }
                i31 = i;
            }
        } else {
            i = 1;
        }
        int i32 = i + 1;
        int iCharAt2 = stringInfo.charAt(i);
        if (iCharAt2 >= 55296) {
            int i33 = iCharAt2 & 8191;
            int i34 = 13;
            while (true) {
                i30 = i32 + 1;
                cCharAt13 = stringInfo.charAt(i32);
                if (cCharAt13 < 55296) {
                    break;
                }
                i33 |= (cCharAt13 & 8191) << i34;
                i34 += 13;
                i32 = i30;
            }
            iCharAt2 = i33 | (cCharAt13 << i34);
            i32 = i30;
        }
        if (iCharAt2 == 0) {
            i4 = 0;
            iCharAt = 0;
            i3 = 0;
            i7 = 0;
            i2 = 0;
            i6 = 0;
            iArr = EMPTY_INT_ARRAY;
            i5 = 0;
        } else {
            int i35 = i32 + 1;
            int iCharAt3 = stringInfo.charAt(i32);
            if (iCharAt3 >= 55296) {
                int i36 = iCharAt3 & 8191;
                int i37 = 13;
                while (true) {
                    i15 = i35 + 1;
                    cCharAt8 = stringInfo.charAt(i35);
                    if (cCharAt8 < 55296) {
                        break;
                    }
                    i36 |= (cCharAt8 & 8191) << i37;
                    i37 += 13;
                    i35 = i15;
                }
                iCharAt3 = i36 | (cCharAt8 << i37);
                i35 = i15;
            }
            int i38 = i35 + 1;
            int iCharAt4 = stringInfo.charAt(i35);
            if (iCharAt4 >= 55296) {
                int i39 = iCharAt4 & 8191;
                int i40 = 13;
                while (true) {
                    i14 = i38 + 1;
                    cCharAt7 = stringInfo.charAt(i38);
                    if (cCharAt7 < 55296) {
                        break;
                    }
                    i39 |= (cCharAt7 & 8191) << i40;
                    i40 += 13;
                    i38 = i14;
                }
                iCharAt4 = i39 | (cCharAt7 << i40);
                i38 = i14;
            }
            int i41 = i38 + 1;
            int iCharAt5 = stringInfo.charAt(i38);
            if (iCharAt5 >= 55296) {
                int i42 = iCharAt5 & 8191;
                int i43 = 13;
                while (true) {
                    i13 = i41 + 1;
                    cCharAt6 = stringInfo.charAt(i41);
                    if (cCharAt6 < 55296) {
                        break;
                    }
                    i42 |= (cCharAt6 & 8191) << i43;
                    i43 += 13;
                    i41 = i13;
                }
                iCharAt5 = i42 | (cCharAt6 << i43);
                i41 = i13;
            }
            int i44 = i41 + 1;
            int iCharAt6 = stringInfo.charAt(i41);
            if (iCharAt6 >= 55296) {
                int i45 = iCharAt6 & 8191;
                int i46 = 13;
                while (true) {
                    i12 = i44 + 1;
                    cCharAt5 = stringInfo.charAt(i44);
                    if (cCharAt5 < 55296) {
                        break;
                    }
                    i45 |= (cCharAt5 & 8191) << i46;
                    i46 += 13;
                    i44 = i12;
                }
                iCharAt6 = i45 | (cCharAt5 << i46);
                i44 = i12;
            }
            int i47 = i44 + 1;
            iCharAt = stringInfo.charAt(i44);
            if (iCharAt >= 55296) {
                int i48 = iCharAt & 8191;
                int i49 = 13;
                while (true) {
                    i11 = i47 + 1;
                    cCharAt4 = stringInfo.charAt(i47);
                    if (cCharAt4 < 55296) {
                        break;
                    }
                    i48 |= (cCharAt4 & 8191) << i49;
                    i49 += 13;
                    i47 = i11;
                }
                iCharAt = i48 | (cCharAt4 << i49);
                i47 = i11;
            }
            int i50 = i47 + 1;
            int iCharAt7 = stringInfo.charAt(i47);
            if (iCharAt7 >= 55296) {
                int i51 = iCharAt7 & 8191;
                int i52 = 13;
                while (true) {
                    i10 = i50 + 1;
                    cCharAt3 = stringInfo.charAt(i50);
                    if (cCharAt3 < 55296) {
                        break;
                    }
                    i51 |= (cCharAt3 & 8191) << i52;
                    i52 += 13;
                    i50 = i10;
                }
                iCharAt7 = i51 | (cCharAt3 << i52);
                i50 = i10;
            }
            int i53 = i50 + 1;
            int iCharAt8 = stringInfo.charAt(i50);
            if (iCharAt8 >= 55296) {
                int i54 = iCharAt8 & 8191;
                int i55 = 13;
                while (true) {
                    i9 = i53 + 1;
                    cCharAt2 = stringInfo.charAt(i53);
                    if (cCharAt2 < 55296) {
                        break;
                    }
                    i54 |= (cCharAt2 & 8191) << i55;
                    i55 += 13;
                    i53 = i9;
                }
                iCharAt8 = i54 | (cCharAt2 << i55);
                i53 = i9;
            }
            int i56 = i53 + 1;
            int iCharAt9 = stringInfo.charAt(i53);
            if (iCharAt9 >= 55296) {
                int i57 = iCharAt9 & 8191;
                int i58 = 13;
                while (true) {
                    i8 = i56 + 1;
                    cCharAt = stringInfo.charAt(i56);
                    if (cCharAt < 55296) {
                        break;
                    }
                    i57 |= (cCharAt & 8191) << i58;
                    i58 += 13;
                    i56 = i8;
                }
                iCharAt9 = i57 | (cCharAt << i58);
                i56 = i8;
            }
            int[] iArr2 = new int[iCharAt9 + iCharAt7 + iCharAt8];
            i2 = (iCharAt3 * 2) + iCharAt4;
            int i59 = iCharAt7;
            i3 = iCharAt5;
            i4 = i59;
            i5 = iCharAt3;
            iArr = iArr2;
            i6 = iCharAt9;
            i32 = i56;
            i7 = iCharAt6;
        }
        Unsafe unsafe = UNSAFE;
        Object[] objects = rawMessageInfo.getObjects();
        Class<?> cls = rawMessageInfo.getDefaultInstance().getClass();
        int[] iArr3 = new int[iCharAt * 3];
        Object[] objArr = new Object[iCharAt * 2];
        int i60 = i6 + i4;
        int i61 = i60;
        int i62 = i6;
        int i63 = 0;
        int i64 = 0;
        while (i32 < length) {
            int i65 = i32 + 1;
            int iCharAt10 = stringInfo.charAt(i32);
            if (iCharAt10 >= c) {
                int i66 = iCharAt10 & 8191;
                int i67 = i65;
                int i68 = 13;
                while (true) {
                    i29 = i67 + 1;
                    cCharAt12 = stringInfo.charAt(i67);
                    if (cCharAt12 < c) {
                        break;
                    }
                    i66 |= (cCharAt12 & 8191) << i68;
                    i68 += 13;
                    i67 = i29;
                }
                iCharAt10 = i66 | (cCharAt12 << i68);
                i16 = i29;
            } else {
                i16 = i65;
            }
            int i69 = i16 + 1;
            int iCharAt11 = stringInfo.charAt(i16);
            if (iCharAt11 >= c) {
                int i70 = iCharAt11 & 8191;
                int i71 = i69;
                int i72 = 13;
                while (true) {
                    i28 = i71 + 1;
                    cCharAt11 = stringInfo.charAt(i71);
                    if (cCharAt11 < c) {
                        break;
                    }
                    i70 |= (cCharAt11 & 8191) << i72;
                    i72 += 13;
                    i71 = i28;
                }
                iCharAt11 = i70 | (cCharAt11 << i72);
                i17 = i28;
            } else {
                i17 = i69;
            }
            int i73 = iCharAt11 & 255;
            int i74 = length;
            if ((iCharAt11 & 1024) != 0) {
                iArr[i63] = i64;
                i63++;
            }
            int[] iArr4 = iArr3;
            if (i73 >= 51) {
                int i75 = i17 + 1;
                int iCharAt12 = stringInfo.charAt(i17);
                char c2 = CharacterCompat.MIN_HIGH_SURROGATE;
                if (iCharAt12 >= 55296) {
                    int i76 = iCharAt12 & 8191;
                    int i77 = 13;
                    while (true) {
                        i27 = i75 + 1;
                        cCharAt10 = stringInfo.charAt(i75);
                        if (cCharAt10 < c2) {
                            break;
                        }
                        i76 |= (cCharAt10 & 8191) << i77;
                        i77 += 13;
                        i75 = i27;
                        c2 = CharacterCompat.MIN_HIGH_SURROGATE;
                    }
                    iCharAt12 = i76 | (cCharAt10 << i77);
                    i75 = i27;
                }
                int i78 = i73 - 51;
                int i79 = i75;
                if (i78 == 9 || i78 == 17) {
                    i24 = i2 + 1;
                    objArr[((i64 / 3) * 2) + 1] = objects[i2];
                } else {
                    if (i78 == 12 && (rawMessageInfo.getSyntax().equals(ProtoSyntax.PROTO2) || (iCharAt11 & 2048) != 0)) {
                        i24 = i2 + 1;
                        objArr[((i64 / 3) * 2) + 1] = objects[i2];
                    }
                    i25 = iCharAt12 * 2;
                    obj = objects[i25];
                    if (obj instanceof Field) {
                        fieldReflectField2 = (Field) obj;
                    } else {
                        fieldReflectField2 = reflectField(cls, (String) obj);
                        objects[i25] = fieldReflectField2;
                    }
                    int iObjectFieldOffset4 = (int) unsafe.objectFieldOffset(fieldReflectField2);
                    i26 = i25 + 1;
                    obj2 = objects[i26];
                    if (obj2 instanceof Field) {
                        fieldReflectField3 = (Field) obj2;
                    } else {
                        fieldReflectField3 = reflectField(cls, (String) obj2);
                        objects[i26] = fieldReflectField3;
                    }
                    stringInfo = stringInfo;
                    iObjectFieldOffset3 = (int) unsafe.objectFieldOffset(fieldReflectField3);
                    i22 = iObjectFieldOffset4;
                    i21 = 0;
                    i18 = iCharAt10;
                    i32 = i79;
                }
                i2 = i24;
                i25 = iCharAt12 * 2;
                obj = objects[i25];
                if (obj instanceof Field) {
                    fieldReflectField2 = (Field) obj;
                } else {
                    fieldReflectField2 = reflectField(cls, (String) obj);
                    objects[i25] = fieldReflectField2;
                }
                int iObjectFieldOffset5 = (int) unsafe.objectFieldOffset(fieldReflectField2);
                i26 = i25 + 1;
                obj2 = objects[i26];
                if (obj2 instanceof Field) {
                    fieldReflectField3 = (Field) obj2;
                } else {
                    fieldReflectField3 = reflectField(cls, (String) obj2);
                    objects[i26] = fieldReflectField3;
                }
                stringInfo = stringInfo;
                iObjectFieldOffset3 = (int) unsafe.objectFieldOffset(fieldReflectField3);
                i22 = iObjectFieldOffset5;
                i21 = 0;
                i18 = iCharAt10;
                i32 = i79;
            } else {
                int i80 = i2 + 1;
                Field fieldReflectField4 = reflectField(cls, (String) objects[i2]);
                if (i73 == 9 || i73 == 17) {
                    i18 = iCharAt10;
                    objArr[((i64 / 3) * 2) + 1] = fieldReflectField4.getType();
                } else {
                    if (i73 == 27 || i73 == 49) {
                        i18 = iCharAt10;
                        i23 = i2 + 2;
                        objArr[((i64 / 3) * 2) + 1] = objects[i80];
                    } else if (i73 == 12 || i73 == 30 || i73 == 44) {
                        i18 = iCharAt10;
                        if (rawMessageInfo.getSyntax() == ProtoSyntax.PROTO2 || (iCharAt11 & 2048) != 0) {
                            i23 = i2 + 2;
                            objArr[((i64 / 3) * 2) + 1] = objects[i80];
                        }
                        iObjectFieldOffset = (int) unsafe.objectFieldOffset(fieldReflectField4);
                        if ((iCharAt11 & 4096) != 0 || i73 > 17) {
                            iObjectFieldOffset2 = 1048575;
                            i20 = i17;
                            i21 = 0;
                        } else {
                            int i81 = i17 + 1;
                            int iCharAt13 = stringInfo.charAt(i17);
                            if (iCharAt13 >= 55296) {
                                int i82 = iCharAt13 & 8191;
                                int i83 = 13;
                                while (true) {
                                    i20 = i81 + 1;
                                    cCharAt9 = stringInfo.charAt(i81);
                                    if (cCharAt9 < 55296) {
                                        break;
                                    }
                                    i82 |= (cCharAt9 & 8191) << i83;
                                    i83 += 13;
                                    i81 = i20;
                                }
                                iCharAt13 = i82 | (cCharAt9 << i83);
                            } else {
                                i20 = i81;
                            }
                            int i84 = (i5 * 2) + (iCharAt13 / 32);
                            Object obj3 = objects[i84];
                            if (obj3 instanceof Field) {
                                fieldReflectField = (Field) obj3;
                            } else {
                                fieldReflectField = reflectField(cls, (String) obj3);
                                objects[i84] = fieldReflectField;
                            }
                            iObjectFieldOffset2 = (int) unsafe.objectFieldOffset(fieldReflectField);
                            i21 = iCharAt13 % 32;
                        }
                        int i85 = iObjectFieldOffset2;
                        if (i73 >= 18 && i73 <= 49) {
                            iArr[i61] = iObjectFieldOffset;
                            i61++;
                        }
                        iObjectFieldOffset3 = i85;
                        i22 = iObjectFieldOffset;
                        i2 = i19;
                        i32 = i20;
                    } else {
                        if (i73 == 50) {
                            int i86 = i62 + 1;
                            iArr[i62] = i64;
                            int i87 = (i64 / 3) * 2;
                            int i88 = i2 + 2;
                            objArr[i87] = objects[i80];
                            if ((iCharAt11 & 2048) != 0) {
                                i19 = i2 + 3;
                                objArr[i87 + 1] = objects[i88];
                                i18 = iCharAt10;
                                i62 = i86;
                            } else {
                                i19 = i88;
                                i62 = i86;
                                i18 = iCharAt10;
                            }
                        } else {
                            i18 = iCharAt10;
                        }
                        iObjectFieldOffset = (int) unsafe.objectFieldOffset(fieldReflectField4);
                        if ((iCharAt11 & 4096) != 0) {
                            iObjectFieldOffset2 = 1048575;
                            i20 = i17;
                            i21 = 0;
                        } else {
                            iObjectFieldOffset2 = 1048575;
                            i20 = i17;
                            i21 = 0;
                        }
                        int i89 = iObjectFieldOffset2;
                        if (i73 >= 18) {
                            iArr[i61] = iObjectFieldOffset;
                            i61++;
                        }
                        iObjectFieldOffset3 = i89;
                        i22 = iObjectFieldOffset;
                        i2 = i19;
                        i32 = i20;
                    }
                    i19 = i23;
                    iObjectFieldOffset = (int) unsafe.objectFieldOffset(fieldReflectField4);
                    if ((iCharAt11 & 4096) != 0) {
                        iObjectFieldOffset2 = 1048575;
                        i20 = i17;
                        i21 = 0;
                    } else {
                        iObjectFieldOffset2 = 1048575;
                        i20 = i17;
                        i21 = 0;
                    }
                    int i810 = iObjectFieldOffset2;
                    if (i73 >= 18) {
                        iArr[i61] = iObjectFieldOffset;
                        i61++;
                    }
                    iObjectFieldOffset3 = i810;
                    i22 = iObjectFieldOffset;
                    i2 = i19;
                    i32 = i20;
                }
                i19 = i80;
                iObjectFieldOffset = (int) unsafe.objectFieldOffset(fieldReflectField4);
                if ((iCharAt11 & 4096) != 0) {
                    iObjectFieldOffset2 = 1048575;
                    i20 = i17;
                    i21 = 0;
                } else {
                    iObjectFieldOffset2 = 1048575;
                    i20 = i17;
                    i21 = 0;
                }
                int i811 = iObjectFieldOffset2;
                if (i73 >= 18) {
                    iArr[i61] = iObjectFieldOffset;
                    i61++;
                }
                iObjectFieldOffset3 = i811;
                i22 = iObjectFieldOffset;
                i2 = i19;
                i32 = i20;
            }
            int i90 = i64 + 1;
            iArr4[i64] = i18;
            int i91 = i64 + 2;
            int i92 = iObjectFieldOffset3;
            iArr4[i90] = ((iCharAt11 & 512) != 0 ? 536870912 : 0) | ((iCharAt11 & 256) != 0 ? 268435456 : 0) | ((iCharAt11 & 2048) != 0 ? Integer.MIN_VALUE : 0) | (i73 << 20) | i22;
            i64 += 3;
            iArr4[i91] = (i21 << 20) | i92;
            length = i74;
            iArr3 = iArr4;
            stringInfo = stringInfo;
            c = CharacterCompat.MIN_HIGH_SURROGATE;
        }
        return new MessageSchema(iArr3, objArr, i3, i7, rawMessageInfo.getDefaultInstance(), rawMessageInfo.getSyntax(), false, iArr, i6, i60, newInstanceSchema, listFieldSchema, unknownFieldSchema, extensionSchema, mapFieldSchema);
    }

    private static Field reflectField(Class cls, String str) {
        try {
            return cls.getDeclaredField(str);
        } catch (NoSuchFieldException unused) {
            Field[] declaredFields = cls.getDeclaredFields();
            for (Field field : declaredFields) {
                if (str.equals(field.getName())) {
                    return field;
                }
            }
            throw new RuntimeException("Field " + str + " for " + cls.getName() + " not found. Known fields are " + Arrays.toString(declaredFields));
        }
    }

    static MessageSchema newSchemaForMessageInfo(StructuralMessageInfo structuralMessageInfo, NewInstanceSchema newInstanceSchema, ListFieldSchema listFieldSchema, UnknownFieldSchema unknownFieldSchema, ExtensionSchema extensionSchema, MapFieldSchema mapFieldSchema) {
        throw null;
    }

    @Override // androidx.datastore.preferences.protobuf.Schema
    public Object newInstance() {
        return this.newInstanceSchema.newInstance(this.defaultInstance);
    }

    @Override // androidx.datastore.preferences.protobuf.Schema
    public boolean equals(Object obj, Object obj2) {
        int length = this.buffer.length;
        for (int i = 0; i < length; i += 3) {
            if (!equals(obj, obj2, i)) {
                return false;
            }
        }
        if (!this.unknownFieldSchema.getFromMessage(obj).equals(this.unknownFieldSchema.getFromMessage(obj2))) {
            return false;
        }
        if (this.hasExtensions) {
            return this.extensionSchema.getExtensions(obj).equals(this.extensionSchema.getExtensions(obj2));
        }
        return true;
    }

    private boolean equals(Object obj, Object obj2, int i) {
        int iTypeAndOffsetAt = typeAndOffsetAt(i);
        long jOffset = offset(iTypeAndOffsetAt);
        switch (type(iTypeAndOffsetAt)) {
            case 0:
                return arePresentForEquals(obj, obj2, i) && Double.doubleToLongBits(UnsafeUtil.getDouble(obj, jOffset)) == Double.doubleToLongBits(UnsafeUtil.getDouble(obj2, jOffset));
            case 1:
                return arePresentForEquals(obj, obj2, i) && Float.floatToIntBits(UnsafeUtil.getFloat(obj, jOffset)) == Float.floatToIntBits(UnsafeUtil.getFloat(obj2, jOffset));
            case 2:
                return arePresentForEquals(obj, obj2, i) && UnsafeUtil.getLong(obj, jOffset) == UnsafeUtil.getLong(obj2, jOffset);
            case 3:
                return arePresentForEquals(obj, obj2, i) && UnsafeUtil.getLong(obj, jOffset) == UnsafeUtil.getLong(obj2, jOffset);
            case 4:
                return arePresentForEquals(obj, obj2, i) && UnsafeUtil.getInt(obj, jOffset) == UnsafeUtil.getInt(obj2, jOffset);
            case 5:
                return arePresentForEquals(obj, obj2, i) && UnsafeUtil.getLong(obj, jOffset) == UnsafeUtil.getLong(obj2, jOffset);
            case 6:
                return arePresentForEquals(obj, obj2, i) && UnsafeUtil.getInt(obj, jOffset) == UnsafeUtil.getInt(obj2, jOffset);
            case 7:
                return arePresentForEquals(obj, obj2, i) && UnsafeUtil.getBoolean(obj, jOffset) == UnsafeUtil.getBoolean(obj2, jOffset);
            case 8:
                return arePresentForEquals(obj, obj2, i) && SchemaUtil.safeEquals(UnsafeUtil.getObject(obj, jOffset), UnsafeUtil.getObject(obj2, jOffset));
            case 9:
                return arePresentForEquals(obj, obj2, i) && SchemaUtil.safeEquals(UnsafeUtil.getObject(obj, jOffset), UnsafeUtil.getObject(obj2, jOffset));
            case 10:
                return arePresentForEquals(obj, obj2, i) && SchemaUtil.safeEquals(UnsafeUtil.getObject(obj, jOffset), UnsafeUtil.getObject(obj2, jOffset));
            case 11:
                return arePresentForEquals(obj, obj2, i) && UnsafeUtil.getInt(obj, jOffset) == UnsafeUtil.getInt(obj2, jOffset);
            case 12:
                return arePresentForEquals(obj, obj2, i) && UnsafeUtil.getInt(obj, jOffset) == UnsafeUtil.getInt(obj2, jOffset);
            case 13:
                return arePresentForEquals(obj, obj2, i) && UnsafeUtil.getInt(obj, jOffset) == UnsafeUtil.getInt(obj2, jOffset);
            case 14:
                return arePresentForEquals(obj, obj2, i) && UnsafeUtil.getLong(obj, jOffset) == UnsafeUtil.getLong(obj2, jOffset);
            case 15:
                return arePresentForEquals(obj, obj2, i) && UnsafeUtil.getInt(obj, jOffset) == UnsafeUtil.getInt(obj2, jOffset);
            case 16:
                return arePresentForEquals(obj, obj2, i) && UnsafeUtil.getLong(obj, jOffset) == UnsafeUtil.getLong(obj2, jOffset);
            case 17:
                return arePresentForEquals(obj, obj2, i) && SchemaUtil.safeEquals(UnsafeUtil.getObject(obj, jOffset), UnsafeUtil.getObject(obj2, jOffset));
            case 18:
            case 19:
            case 20:
            case 21:
            case 22:
            case 23:
            case 24:
            case 25:
            case 26:
            case 27:
            case 28:
            case 29:
            case 30:
            case 31:
            case 32:
            case 33:
            case 34:
            case 35:
            case 36:
            case 37:
            case 38:
            case 39:
            case 40:
            case 41:
            case 42:
            case 43:
            case 44:
            case 45:
            case 46:
            case 47:
            case 48:
            case 49:
                return SchemaUtil.safeEquals(UnsafeUtil.getObject(obj, jOffset), UnsafeUtil.getObject(obj2, jOffset));
            case 50:
                return SchemaUtil.safeEquals(UnsafeUtil.getObject(obj, jOffset), UnsafeUtil.getObject(obj2, jOffset));
            case 51:
            case 52:
            case 53:
            case 54:
            case 55:
            case 56:
            case 57:
            case 58:
            case 59:
            case 60:
            case 61:
            case 62:
            case 63:
            case 64:
            case 65:
            case 66:
            case 67:
            case 68:
                return isOneofCaseEqual(obj, obj2, i) && SchemaUtil.safeEquals(UnsafeUtil.getObject(obj, jOffset), UnsafeUtil.getObject(obj2, jOffset));
            default:
                return true;
        }
    }

    @Override // androidx.datastore.preferences.protobuf.Schema
    public int hashCode(Object obj) {
        int i;
        int iHashLong;
        int length = this.buffer.length;
        int i2 = 0;
        for (int i3 = 0; i3 < length; i3 += 3) {
            int iTypeAndOffsetAt = typeAndOffsetAt(i3);
            int iNumberAt = numberAt(i3);
            long jOffset = offset(iTypeAndOffsetAt);
            int iHashCode = 37;
            switch (type(iTypeAndOffsetAt)) {
                case 0:
                    i = i2 * 53;
                    iHashLong = Internal.hashLong(Double.doubleToLongBits(UnsafeUtil.getDouble(obj, jOffset)));
                    i2 = i + iHashLong;
                    break;
                case 1:
                    i = i2 * 53;
                    iHashLong = Float.floatToIntBits(UnsafeUtil.getFloat(obj, jOffset));
                    i2 = i + iHashLong;
                    break;
                case 2:
                    i = i2 * 53;
                    iHashLong = Internal.hashLong(UnsafeUtil.getLong(obj, jOffset));
                    i2 = i + iHashLong;
                    break;
                case 3:
                    i = i2 * 53;
                    iHashLong = Internal.hashLong(UnsafeUtil.getLong(obj, jOffset));
                    i2 = i + iHashLong;
                    break;
                case 4:
                    i = i2 * 53;
                    iHashLong = UnsafeUtil.getInt(obj, jOffset);
                    i2 = i + iHashLong;
                    break;
                case 5:
                    i = i2 * 53;
                    iHashLong = Internal.hashLong(UnsafeUtil.getLong(obj, jOffset));
                    i2 = i + iHashLong;
                    break;
                case 6:
                    i = i2 * 53;
                    iHashLong = UnsafeUtil.getInt(obj, jOffset);
                    i2 = i + iHashLong;
                    break;
                case 7:
                    i = i2 * 53;
                    iHashLong = Internal.hashBoolean(UnsafeUtil.getBoolean(obj, jOffset));
                    i2 = i + iHashLong;
                    break;
                case 8:
                    i = i2 * 53;
                    iHashLong = ((String) UnsafeUtil.getObject(obj, jOffset)).hashCode();
                    i2 = i + iHashLong;
                    break;
                case 9:
                    Object object = UnsafeUtil.getObject(obj, jOffset);
                    if (object != null) {
                        iHashCode = object.hashCode();
                    }
                    i2 = (i2 * 53) + iHashCode;
                    break;
                case 10:
                    i = i2 * 53;
                    iHashLong = UnsafeUtil.getObject(obj, jOffset).hashCode();
                    i2 = i + iHashLong;
                    break;
                case 11:
                    i = i2 * 53;
                    iHashLong = UnsafeUtil.getInt(obj, jOffset);
                    i2 = i + iHashLong;
                    break;
                case 12:
                    i = i2 * 53;
                    iHashLong = UnsafeUtil.getInt(obj, jOffset);
                    i2 = i + iHashLong;
                    break;
                case 13:
                    i = i2 * 53;
                    iHashLong = UnsafeUtil.getInt(obj, jOffset);
                    i2 = i + iHashLong;
                    break;
                case 14:
                    i = i2 * 53;
                    iHashLong = Internal.hashLong(UnsafeUtil.getLong(obj, jOffset));
                    i2 = i + iHashLong;
                    break;
                case 15:
                    i = i2 * 53;
                    iHashLong = UnsafeUtil.getInt(obj, jOffset);
                    i2 = i + iHashLong;
                    break;
                case 16:
                    i = i2 * 53;
                    iHashLong = Internal.hashLong(UnsafeUtil.getLong(obj, jOffset));
                    i2 = i + iHashLong;
                    break;
                case 17:
                    Object object2 = UnsafeUtil.getObject(obj, jOffset);
                    if (object2 != null) {
                        iHashCode = object2.hashCode();
                    }
                    i2 = (i2 * 53) + iHashCode;
                    break;
                case 18:
                case 19:
                case 20:
                case 21:
                case 22:
                case 23:
                case 24:
                case 25:
                case 26:
                case 27:
                case 28:
                case 29:
                case 30:
                case 31:
                case 32:
                case 33:
                case 34:
                case 35:
                case 36:
                case 37:
                case 38:
                case 39:
                case 40:
                case 41:
                case 42:
                case 43:
                case 44:
                case 45:
                case 46:
                case 47:
                case 48:
                case 49:
                    i = i2 * 53;
                    iHashLong = UnsafeUtil.getObject(obj, jOffset).hashCode();
                    i2 = i + iHashLong;
                    break;
                case 50:
                    i = i2 * 53;
                    iHashLong = UnsafeUtil.getObject(obj, jOffset).hashCode();
                    i2 = i + iHashLong;
                    break;
                case 51:
                    if (isOneofPresent(obj, iNumberAt, i3)) {
                        i = i2 * 53;
                        iHashLong = Internal.hashLong(Double.doubleToLongBits(oneofDoubleAt(obj, jOffset)));
                        i2 = i + iHashLong;
                    }
                    break;
                case 52:
                    if (isOneofPresent(obj, iNumberAt, i3)) {
                        i = i2 * 53;
                        iHashLong = Float.floatToIntBits(oneofFloatAt(obj, jOffset));
                        i2 = i + iHashLong;
                    }
                    break;
                case 53:
                    if (isOneofPresent(obj, iNumberAt, i3)) {
                        i = i2 * 53;
                        iHashLong = Internal.hashLong(oneofLongAt(obj, jOffset));
                        i2 = i + iHashLong;
                    }
                    break;
                case 54:
                    if (isOneofPresent(obj, iNumberAt, i3)) {
                        i = i2 * 53;
                        iHashLong = Internal.hashLong(oneofLongAt(obj, jOffset));
                        i2 = i + iHashLong;
                    }
                    break;
                case 55:
                    if (isOneofPresent(obj, iNumberAt, i3)) {
                        i = i2 * 53;
                        iHashLong = oneofIntAt(obj, jOffset);
                        i2 = i + iHashLong;
                    }
                    break;
                case 56:
                    if (isOneofPresent(obj, iNumberAt, i3)) {
                        i = i2 * 53;
                        iHashLong = Internal.hashLong(oneofLongAt(obj, jOffset));
                        i2 = i + iHashLong;
                    }
                    break;
                case 57:
                    if (isOneofPresent(obj, iNumberAt, i3)) {
                        i = i2 * 53;
                        iHashLong = oneofIntAt(obj, jOffset);
                        i2 = i + iHashLong;
                    }
                    break;
                case 58:
                    if (isOneofPresent(obj, iNumberAt, i3)) {
                        i = i2 * 53;
                        iHashLong = Internal.hashBoolean(oneofBooleanAt(obj, jOffset));
                        i2 = i + iHashLong;
                    }
                    break;
                case 59:
                    if (isOneofPresent(obj, iNumberAt, i3)) {
                        i = i2 * 53;
                        iHashLong = ((String) UnsafeUtil.getObject(obj, jOffset)).hashCode();
                        i2 = i + iHashLong;
                    }
                    break;
                case 60:
                    if (isOneofPresent(obj, iNumberAt, i3)) {
                        i = i2 * 53;
                        iHashLong = UnsafeUtil.getObject(obj, jOffset).hashCode();
                        i2 = i + iHashLong;
                    }
                    break;
                case 61:
                    if (isOneofPresent(obj, iNumberAt, i3)) {
                        i = i2 * 53;
                        iHashLong = UnsafeUtil.getObject(obj, jOffset).hashCode();
                        i2 = i + iHashLong;
                    }
                    break;
                case 62:
                    if (isOneofPresent(obj, iNumberAt, i3)) {
                        i = i2 * 53;
                        iHashLong = oneofIntAt(obj, jOffset);
                        i2 = i + iHashLong;
                    }
                    break;
                case 63:
                    if (isOneofPresent(obj, iNumberAt, i3)) {
                        i = i2 * 53;
                        iHashLong = oneofIntAt(obj, jOffset);
                        i2 = i + iHashLong;
                    }
                    break;
                case 64:
                    if (isOneofPresent(obj, iNumberAt, i3)) {
                        i = i2 * 53;
                        iHashLong = oneofIntAt(obj, jOffset);
                        i2 = i + iHashLong;
                    }
                    break;
                case 65:
                    if (isOneofPresent(obj, iNumberAt, i3)) {
                        i = i2 * 53;
                        iHashLong = Internal.hashLong(oneofLongAt(obj, jOffset));
                        i2 = i + iHashLong;
                    }
                    break;
                case 66:
                    if (isOneofPresent(obj, iNumberAt, i3)) {
                        i = i2 * 53;
                        iHashLong = oneofIntAt(obj, jOffset);
                        i2 = i + iHashLong;
                    }
                    break;
                case 67:
                    if (isOneofPresent(obj, iNumberAt, i3)) {
                        i = i2 * 53;
                        iHashLong = Internal.hashLong(oneofLongAt(obj, jOffset));
                        i2 = i + iHashLong;
                    }
                    break;
                case 68:
                    if (isOneofPresent(obj, iNumberAt, i3)) {
                        i = i2 * 53;
                        iHashLong = UnsafeUtil.getObject(obj, jOffset).hashCode();
                        i2 = i + iHashLong;
                    }
                    break;
            }
        }
        int iHashCode2 = (i2 * 53) + this.unknownFieldSchema.getFromMessage(obj).hashCode();
        return this.hasExtensions ? (iHashCode2 * 53) + this.extensionSchema.getExtensions(obj).hashCode() : iHashCode2;
    }

    @Override // androidx.datastore.preferences.protobuf.Schema
    public void mergeFrom(Object obj, Object obj2) {
        checkMutable(obj);
        obj2.getClass();
        for (int i = 0; i < this.buffer.length; i += 3) {
            mergeSingleField(obj, obj2, i);
        }
        SchemaUtil.mergeUnknownFields(this.unknownFieldSchema, obj, obj2);
        if (this.hasExtensions) {
            SchemaUtil.mergeExtensions(this.extensionSchema, obj, obj2);
        }
    }

    private void mergeSingleField(Object obj, Object obj2, int i) {
        int iTypeAndOffsetAt = typeAndOffsetAt(i);
        long jOffset = offset(iTypeAndOffsetAt);
        int iNumberAt = numberAt(i);
        switch (type(iTypeAndOffsetAt)) {
            case 0:
                if (isFieldPresent(obj2, i)) {
                    UnsafeUtil.putDouble(obj, jOffset, UnsafeUtil.getDouble(obj2, jOffset));
                    setFieldPresent(obj, i);
                }
                break;
            case 1:
                if (isFieldPresent(obj2, i)) {
                    UnsafeUtil.putFloat(obj, jOffset, UnsafeUtil.getFloat(obj2, jOffset));
                    setFieldPresent(obj, i);
                }
                break;
            case 2:
                if (isFieldPresent(obj2, i)) {
                    UnsafeUtil.putLong(obj, jOffset, UnsafeUtil.getLong(obj2, jOffset));
                    setFieldPresent(obj, i);
                }
                break;
            case 3:
                if (isFieldPresent(obj2, i)) {
                    UnsafeUtil.putLong(obj, jOffset, UnsafeUtil.getLong(obj2, jOffset));
                    setFieldPresent(obj, i);
                }
                break;
            case 4:
                if (isFieldPresent(obj2, i)) {
                    UnsafeUtil.putInt(obj, jOffset, UnsafeUtil.getInt(obj2, jOffset));
                    setFieldPresent(obj, i);
                }
                break;
            case 5:
                if (isFieldPresent(obj2, i)) {
                    UnsafeUtil.putLong(obj, jOffset, UnsafeUtil.getLong(obj2, jOffset));
                    setFieldPresent(obj, i);
                }
                break;
            case 6:
                if (isFieldPresent(obj2, i)) {
                    UnsafeUtil.putInt(obj, jOffset, UnsafeUtil.getInt(obj2, jOffset));
                    setFieldPresent(obj, i);
                }
                break;
            case 7:
                if (isFieldPresent(obj2, i)) {
                    UnsafeUtil.putBoolean(obj, jOffset, UnsafeUtil.getBoolean(obj2, jOffset));
                    setFieldPresent(obj, i);
                }
                break;
            case 8:
                if (isFieldPresent(obj2, i)) {
                    UnsafeUtil.putObject(obj, jOffset, UnsafeUtil.getObject(obj2, jOffset));
                    setFieldPresent(obj, i);
                }
                break;
            case 9:
                mergeMessage(obj, obj2, i);
                break;
            case 10:
                if (isFieldPresent(obj2, i)) {
                    UnsafeUtil.putObject(obj, jOffset, UnsafeUtil.getObject(obj2, jOffset));
                    setFieldPresent(obj, i);
                }
                break;
            case 11:
                if (isFieldPresent(obj2, i)) {
                    UnsafeUtil.putInt(obj, jOffset, UnsafeUtil.getInt(obj2, jOffset));
                    setFieldPresent(obj, i);
                }
                break;
            case 12:
                if (isFieldPresent(obj2, i)) {
                    UnsafeUtil.putInt(obj, jOffset, UnsafeUtil.getInt(obj2, jOffset));
                    setFieldPresent(obj, i);
                }
                break;
            case 13:
                if (isFieldPresent(obj2, i)) {
                    UnsafeUtil.putInt(obj, jOffset, UnsafeUtil.getInt(obj2, jOffset));
                    setFieldPresent(obj, i);
                }
                break;
            case 14:
                if (isFieldPresent(obj2, i)) {
                    UnsafeUtil.putLong(obj, jOffset, UnsafeUtil.getLong(obj2, jOffset));
                    setFieldPresent(obj, i);
                }
                break;
            case 15:
                if (isFieldPresent(obj2, i)) {
                    UnsafeUtil.putInt(obj, jOffset, UnsafeUtil.getInt(obj2, jOffset));
                    setFieldPresent(obj, i);
                }
                break;
            case 16:
                if (isFieldPresent(obj2, i)) {
                    UnsafeUtil.putLong(obj, jOffset, UnsafeUtil.getLong(obj2, jOffset));
                    setFieldPresent(obj, i);
                }
                break;
            case 17:
                mergeMessage(obj, obj2, i);
                break;
            case 18:
            case 19:
            case 20:
            case 21:
            case 22:
            case 23:
            case 24:
            case 25:
            case 26:
            case 27:
            case 28:
            case 29:
            case 30:
            case 31:
            case 32:
            case 33:
            case 34:
            case 35:
            case 36:
            case 37:
            case 38:
            case 39:
            case 40:
            case 41:
            case 42:
            case 43:
            case 44:
            case 45:
            case 46:
            case 47:
            case 48:
            case 49:
                this.listFieldSchema.mergeListsAt(obj, obj2, jOffset);
                break;
            case 50:
                SchemaUtil.mergeMap(this.mapFieldSchema, obj, obj2, jOffset);
                break;
            case 51:
            case 52:
            case 53:
            case 54:
            case 55:
            case 56:
            case 57:
            case 58:
            case 59:
                if (isOneofPresent(obj2, iNumberAt, i)) {
                    UnsafeUtil.putObject(obj, jOffset, UnsafeUtil.getObject(obj2, jOffset));
                    setOneofPresent(obj, iNumberAt, i);
                }
                break;
            case 60:
                mergeOneofMessage(obj, obj2, i);
                break;
            case 61:
            case 62:
            case 63:
            case 64:
            case 65:
            case 66:
            case 67:
                if (isOneofPresent(obj2, iNumberAt, i)) {
                    UnsafeUtil.putObject(obj, jOffset, UnsafeUtil.getObject(obj2, jOffset));
                    setOneofPresent(obj, iNumberAt, i);
                }
                break;
            case 68:
                mergeOneofMessage(obj, obj2, i);
                break;
        }
    }

    private void mergeMessage(Object obj, Object obj2, int i) {
        if (isFieldPresent(obj2, i)) {
            long jOffset = offset(typeAndOffsetAt(i));
            Unsafe unsafe = UNSAFE;
            Object object = unsafe.getObject(obj2, jOffset);
            if (object == null) {
                throw new IllegalStateException("Source subfield " + numberAt(i) + " is present but null: " + obj2);
            }
            Schema messageFieldSchema = getMessageFieldSchema(i);
            if (!isFieldPresent(obj, i)) {
                if (!isMutable(object)) {
                    unsafe.putObject(obj, jOffset, object);
                } else {
                    Object objNewInstance = messageFieldSchema.newInstance();
                    messageFieldSchema.mergeFrom(objNewInstance, object);
                    unsafe.putObject(obj, jOffset, objNewInstance);
                }
                setFieldPresent(obj, i);
                return;
            }
            Object object2 = unsafe.getObject(obj, jOffset);
            if (!isMutable(object2)) {
                Object objNewInstance2 = messageFieldSchema.newInstance();
                messageFieldSchema.mergeFrom(objNewInstance2, object2);
                unsafe.putObject(obj, jOffset, objNewInstance2);
                object2 = objNewInstance2;
            }
            messageFieldSchema.mergeFrom(object2, object);
        }
    }

    private void mergeOneofMessage(Object obj, Object obj2, int i) {
        int iNumberAt = numberAt(i);
        if (isOneofPresent(obj2, iNumberAt, i)) {
            long jOffset = offset(typeAndOffsetAt(i));
            Unsafe unsafe = UNSAFE;
            Object object = unsafe.getObject(obj2, jOffset);
            if (object == null) {
                throw new IllegalStateException("Source subfield " + numberAt(i) + " is present but null: " + obj2);
            }
            Schema messageFieldSchema = getMessageFieldSchema(i);
            if (!isOneofPresent(obj, iNumberAt, i)) {
                if (!isMutable(object)) {
                    unsafe.putObject(obj, jOffset, object);
                } else {
                    Object objNewInstance = messageFieldSchema.newInstance();
                    messageFieldSchema.mergeFrom(objNewInstance, object);
                    unsafe.putObject(obj, jOffset, objNewInstance);
                }
                setOneofPresent(obj, iNumberAt, i);
                return;
            }
            Object object2 = unsafe.getObject(obj, jOffset);
            if (!isMutable(object2)) {
                Object objNewInstance2 = messageFieldSchema.newInstance();
                messageFieldSchema.mergeFrom(objNewInstance2, object2);
                unsafe.putObject(obj, jOffset, objNewInstance2);
                object2 = objNewInstance2;
            }
            messageFieldSchema.mergeFrom(object2, object);
        }
    }

    /* JADX WARN: Can't fix incorrect switch cases order, some code will duplicate */
    /* JADX WARN: Code duplicated, block: B:247:0x0552 A[PHI: r0 r1
  0x0552: PHI (r0v2 androidx.datastore.preferences.protobuf.MessageSchema) = 
  (r0v1 androidx.datastore.preferences.protobuf.MessageSchema)
  (r0v1 androidx.datastore.preferences.protobuf.MessageSchema)
  (r0v1 androidx.datastore.preferences.protobuf.MessageSchema)
  (r0v24 androidx.datastore.preferences.protobuf.MessageSchema)
  (r0v30 androidx.datastore.preferences.protobuf.MessageSchema)
  (r0v1 androidx.datastore.preferences.protobuf.MessageSchema)
  (r0v1 androidx.datastore.preferences.protobuf.MessageSchema)
  (r0v1 androidx.datastore.preferences.protobuf.MessageSchema)
  (r0v1 androidx.datastore.preferences.protobuf.MessageSchema)
  (r0v1 androidx.datastore.preferences.protobuf.MessageSchema)
  (r0v1 androidx.datastore.preferences.protobuf.MessageSchema)
  (r0v1 androidx.datastore.preferences.protobuf.MessageSchema)
  (r0v1 androidx.datastore.preferences.protobuf.MessageSchema)
  (r0v1 androidx.datastore.preferences.protobuf.MessageSchema)
  (r0v1 androidx.datastore.preferences.protobuf.MessageSchema)
  (r0v1 androidx.datastore.preferences.protobuf.MessageSchema)
  (r0v1 androidx.datastore.preferences.protobuf.MessageSchema)
  (r0v1 androidx.datastore.preferences.protobuf.MessageSchema)
  (r0v1 androidx.datastore.preferences.protobuf.MessageSchema)
  (r0v1 androidx.datastore.preferences.protobuf.MessageSchema)
  (r0v1 androidx.datastore.preferences.protobuf.MessageSchema)
  (r0v1 androidx.datastore.preferences.protobuf.MessageSchema)
  (r0v1 androidx.datastore.preferences.protobuf.MessageSchema)
  (r0v1 androidx.datastore.preferences.protobuf.MessageSchema)
  (r0v1 androidx.datastore.preferences.protobuf.MessageSchema)
  (r0v1 androidx.datastore.preferences.protobuf.MessageSchema)
  (r0v1 androidx.datastore.preferences.protobuf.MessageSchema)
  (r0v1 androidx.datastore.preferences.protobuf.MessageSchema)
  (r0v1 androidx.datastore.preferences.protobuf.MessageSchema)
  (r0v1 androidx.datastore.preferences.protobuf.MessageSchema)
  (r0v1 androidx.datastore.preferences.protobuf.MessageSchema)
  (r0v1 androidx.datastore.preferences.protobuf.MessageSchema)
  (r0v1 androidx.datastore.preferences.protobuf.MessageSchema)
  (r0v1 androidx.datastore.preferences.protobuf.MessageSchema)
  (r0v1 androidx.datastore.preferences.protobuf.MessageSchema)
  (r0v1 androidx.datastore.preferences.protobuf.MessageSchema)
  (r0v1 androidx.datastore.preferences.protobuf.MessageSchema)
  (r0v1 androidx.datastore.preferences.protobuf.MessageSchema)
 binds: [B:22:0x005b, B:245:0x0548, B:215:0x04ab, B:201:0x0462, B:193:0x043b, B:187:0x0414, B:164:0x032b, B:158:0x030d, B:152:0x02ef, B:146:0x02d1, B:140:0x02b3, B:134:0x0295, B:128:0x0277, B:122:0x0259, B:116:0x023b, B:110:0x021e, B:104:0x0201, B:98:0x01e4, B:92:0x01c7, B:85:0x01a5, B:80:0x0171, B:77:0x0165, B:74:0x0155, B:71:0x0145, B:68:0x0135, B:65:0x0129, B:62:0x011d, B:59:0x0110, B:53:0x00f2, B:50:0x00df, B:47:0x00ce, B:44:0x00bf, B:41:0x00b0, B:38:0x00a5, B:35:0x009a, B:32:0x008b, B:29:0x007c, B:25:0x0064] A[DONT_GENERATE, DONT_INLINE]
  0x0552: PHI (r1v4 java.lang.Object) = 
  (r1v1 java.lang.Object)
  (r1v1 java.lang.Object)
  (r1v1 java.lang.Object)
  (r1v5 java.lang.Object)
  (r1v1 java.lang.Object)
  (r1v1 java.lang.Object)
  (r1v1 java.lang.Object)
  (r1v1 java.lang.Object)
  (r1v1 java.lang.Object)
  (r1v1 java.lang.Object)
  (r1v1 java.lang.Object)
  (r1v1 java.lang.Object)
  (r1v1 java.lang.Object)
  (r1v1 java.lang.Object)
  (r1v1 java.lang.Object)
  (r1v1 java.lang.Object)
  (r1v1 java.lang.Object)
  (r1v1 java.lang.Object)
  (r1v1 java.lang.Object)
  (r1v1 java.lang.Object)
  (r1v1 java.lang.Object)
  (r1v1 java.lang.Object)
  (r1v1 java.lang.Object)
  (r1v1 java.lang.Object)
  (r1v1 java.lang.Object)
  (r1v1 java.lang.Object)
  (r1v1 java.lang.Object)
  (r1v1 java.lang.Object)
  (r1v1 java.lang.Object)
  (r1v1 java.lang.Object)
  (r1v1 java.lang.Object)
  (r1v1 java.lang.Object)
  (r1v1 java.lang.Object)
  (r1v1 java.lang.Object)
  (r1v1 java.lang.Object)
  (r1v1 java.lang.Object)
  (r1v1 java.lang.Object)
  (r1v1 java.lang.Object)
 binds: [B:22:0x005b, B:245:0x0548, B:215:0x04ab, B:201:0x0462, B:193:0x043b, B:187:0x0414, B:164:0x032b, B:158:0x030d, B:152:0x02ef, B:146:0x02d1, B:140:0x02b3, B:134:0x0295, B:128:0x0277, B:122:0x0259, B:116:0x023b, B:110:0x021e, B:104:0x0201, B:98:0x01e4, B:92:0x01c7, B:85:0x01a5, B:80:0x0171, B:77:0x0165, B:74:0x0155, B:71:0x0145, B:68:0x0135, B:65:0x0129, B:62:0x011d, B:59:0x0110, B:53:0x00f2, B:50:0x00df, B:47:0x00ce, B:44:0x00bf, B:41:0x00b0, B:38:0x00a5, B:35:0x009a, B:32:0x008b, B:29:0x007c, B:25:0x0064] A[DONT_GENERATE, DONT_INLINE]] */
    @Override // androidx.datastore.preferences.protobuf.Schema
    public int getSerializedSize(Object obj) {
        int i;
        int iComputeDoubleSize;
        int iComputeFloatSize;
        int iComputeInt64Size;
        int iComputeSizeFixed64ListNoTag;
        int iComputeTagSize;
        int iComputeUInt32SizeNoTag;
        MessageSchema messageSchema = this;
        Object obj2 = obj;
        Unsafe unsafe = UNSAFE;
        int i2 = 1048575;
        int i3 = 0;
        int i4 = 0;
        int i5 = 0;
        int i6 = 1048575;
        while (i3 < messageSchema.buffer.length) {
            int iTypeAndOffsetAt = messageSchema.typeAndOffsetAt(i3);
            int iType = type(iTypeAndOffsetAt);
            int iNumberAt = messageSchema.numberAt(i3);
            int i7 = messageSchema.buffer[i3 + 2];
            int i8 = i7 & i2;
            if (iType <= 17) {
                if (i8 != i6) {
                    i4 = i8 == i2 ? 0 : unsafe.getInt(obj2, i8);
                    i6 = i8;
                }
                i = 1 << (i7 >>> 20);
            } else {
                i = 0;
            }
            int i9 = i5;
            long jOffset = offset(iTypeAndOffsetAt);
            if (iType < FieldType.DOUBLE_LIST_PACKED.id() || iType > FieldType.SINT64_LIST_PACKED.id()) {
                i8 = 0;
            }
            switch (iType) {
                case 0:
                    if (!messageSchema.isFieldPresent(obj2, i3, i6, i4, i)) {
                        i5 = i9;
                    } else {
                        iComputeDoubleSize = CodedOutputStream.computeDoubleSize(iNumberAt, 0.0d);
                        i5 = i9 + iComputeDoubleSize;
                    }
                    break;
                case 1:
                    if (messageSchema.isFieldPresent(obj2, i3, i6, i4, i)) {
                        iComputeFloatSize = CodedOutputStream.computeFloatSize(iNumberAt, 0.0f);
                        i5 = i9 + iComputeFloatSize;
                        messageSchema = this;
                        obj2 = obj;
                    }
                    messageSchema = this;
                    obj2 = obj;
                    i5 = i9;
                    break;
                case 2:
                    if (messageSchema.isFieldPresent(obj2, i3, i6, i4, i)) {
                        iComputeInt64Size = CodedOutputStream.computeInt64Size(iNumberAt, unsafe.getLong(obj2, jOffset));
                        i5 = i9 + iComputeInt64Size;
                        messageSchema = this;
                    }
                    messageSchema = this;
                    i5 = i9;
                    break;
                case 3:
                    if (messageSchema.isFieldPresent(obj2, i3, i6, i4, i)) {
                        iComputeInt64Size = CodedOutputStream.computeUInt64Size(iNumberAt, unsafe.getLong(obj2, jOffset));
                        i5 = i9 + iComputeInt64Size;
                        messageSchema = this;
                    }
                    messageSchema = this;
                    i5 = i9;
                    break;
                case 4:
                    if (messageSchema.isFieldPresent(obj2, i3, i6, i4, i)) {
                        iComputeInt64Size = CodedOutputStream.computeInt32Size(iNumberAt, unsafe.getInt(obj2, jOffset));
                        i5 = i9 + iComputeInt64Size;
                        messageSchema = this;
                    }
                    messageSchema = this;
                    i5 = i9;
                    break;
                case 5:
                    if (messageSchema.isFieldPresent(obj2, i3, i6, i4, i)) {
                        iComputeFloatSize = CodedOutputStream.computeFixed64Size(iNumberAt, 0L);
                        i5 = i9 + iComputeFloatSize;
                        messageSchema = this;
                        obj2 = obj;
                    }
                    messageSchema = this;
                    obj2 = obj;
                    i5 = i9;
                    break;
                case 6:
                    if (messageSchema.isFieldPresent(obj2, i3, i6, i4, i)) {
                        iComputeFloatSize = CodedOutputStream.computeFixed32Size(iNumberAt, 0);
                        i5 = i9 + iComputeFloatSize;
                        messageSchema = this;
                        obj2 = obj;
                    }
                    messageSchema = this;
                    obj2 = obj;
                    i5 = i9;
                    break;
                case 7:
                    if (messageSchema.isFieldPresent(obj2, i3, i6, i4, i)) {
                        iComputeFloatSize = CodedOutputStream.computeBoolSize(iNumberAt, true);
                        i5 = i9 + iComputeFloatSize;
                        messageSchema = this;
                        obj2 = obj;
                    }
                    messageSchema = this;
                    obj2 = obj;
                    i5 = i9;
                    break;
                case 8:
                    if (messageSchema.isFieldPresent(obj2, i3, i6, i4, i)) {
                        Object object = unsafe.getObject(obj2, jOffset);
                        if (object instanceof ByteString) {
                            iComputeInt64Size = CodedOutputStream.computeBytesSize(iNumberAt, (ByteString) object);
                        } else {
                            iComputeInt64Size = CodedOutputStream.computeStringSize(iNumberAt, (String) object);
                        }
                        i5 = i9 + iComputeInt64Size;
                        messageSchema = this;
                    }
                    messageSchema = this;
                    i5 = i9;
                    break;
                case 9:
                    if (!messageSchema.isFieldPresent(obj2, i3, i6, i4, i)) {
                        i5 = i9;
                    } else {
                        iComputeDoubleSize = SchemaUtil.computeSizeMessage(iNumberAt, unsafe.getObject(obj2, jOffset), messageSchema.getMessageFieldSchema(i3));
                        i5 = i9 + iComputeDoubleSize;
                    }
                    break;
                case 10:
                    if (messageSchema.isFieldPresent(obj2, i3, i6, i4, i)) {
                        iComputeInt64Size = CodedOutputStream.computeBytesSize(iNumberAt, (ByteString) unsafe.getObject(obj2, jOffset));
                        i5 = i9 + iComputeInt64Size;
                        messageSchema = this;
                    }
                    messageSchema = this;
                    i5 = i9;
                    break;
                case 11:
                    if (messageSchema.isFieldPresent(obj2, i3, i6, i4, i)) {
                        iComputeInt64Size = CodedOutputStream.computeUInt32Size(iNumberAt, unsafe.getInt(obj2, jOffset));
                        i5 = i9 + iComputeInt64Size;
                        messageSchema = this;
                    }
                    messageSchema = this;
                    i5 = i9;
                    break;
                case 12:
                    if (messageSchema.isFieldPresent(obj2, i3, i6, i4, i)) {
                        iComputeInt64Size = CodedOutputStream.computeEnumSize(iNumberAt, unsafe.getInt(obj2, jOffset));
                        i5 = i9 + iComputeInt64Size;
                        messageSchema = this;
                    }
                    messageSchema = this;
                    i5 = i9;
                    break;
                case 13:
                    if (messageSchema.isFieldPresent(obj2, i3, i6, i4, i)) {
                        iComputeFloatSize = CodedOutputStream.computeSFixed32Size(iNumberAt, 0);
                        i5 = i9 + iComputeFloatSize;
                        messageSchema = this;
                        obj2 = obj;
                    }
                    messageSchema = this;
                    obj2 = obj;
                    i5 = i9;
                    break;
                case 14:
                    if (messageSchema.isFieldPresent(obj2, i3, i6, i4, i)) {
                        iComputeFloatSize = CodedOutputStream.computeSFixed64Size(iNumberAt, 0L);
                        i5 = i9 + iComputeFloatSize;
                        messageSchema = this;
                        obj2 = obj;
                    }
                    messageSchema = this;
                    obj2 = obj;
                    i5 = i9;
                    break;
                case 15:
                    if (messageSchema.isFieldPresent(obj2, i3, i6, i4, i)) {
                        iComputeInt64Size = CodedOutputStream.computeSInt32Size(iNumberAt, unsafe.getInt(obj2, jOffset));
                        i5 = i9 + iComputeInt64Size;
                        messageSchema = this;
                    }
                    messageSchema = this;
                    i5 = i9;
                    break;
                case 16:
                    if (messageSchema.isFieldPresent(obj2, i3, i6, i4, i)) {
                        iComputeInt64Size = CodedOutputStream.computeSInt64Size(iNumberAt, unsafe.getLong(obj2, jOffset));
                        i5 = i9 + iComputeInt64Size;
                        messageSchema = this;
                    }
                    messageSchema = this;
                    i5 = i9;
                    break;
                case 17:
                    if (!messageSchema.isFieldPresent(obj2, i3, i6, i4, i)) {
                        i5 = i9;
                    } else {
                        iComputeDoubleSize = CodedOutputStream.computeGroupSize(iNumberAt, (MessageLite) unsafe.getObject(obj2, jOffset), messageSchema.getMessageFieldSchema(i3));
                        i5 = i9 + iComputeDoubleSize;
                    }
                    break;
                case 18:
                    iComputeDoubleSize = SchemaUtil.computeSizeFixed64List(iNumberAt, (List) unsafe.getObject(obj2, jOffset), false);
                    i5 = i9 + iComputeDoubleSize;
                    break;
                case 19:
                    iComputeDoubleSize = SchemaUtil.computeSizeFixed32List(iNumberAt, (List) unsafe.getObject(obj2, jOffset), false);
                    i5 = i9 + iComputeDoubleSize;
                    break;
                case 20:
                    iComputeDoubleSize = SchemaUtil.computeSizeInt64List(iNumberAt, (List) unsafe.getObject(obj2, jOffset), false);
                    i5 = i9 + iComputeDoubleSize;
                    break;
                case 21:
                    iComputeDoubleSize = SchemaUtil.computeSizeUInt64List(iNumberAt, (List) unsafe.getObject(obj2, jOffset), false);
                    i5 = i9 + iComputeDoubleSize;
                    break;
                case 22:
                    iComputeDoubleSize = SchemaUtil.computeSizeInt32List(iNumberAt, (List) unsafe.getObject(obj2, jOffset), false);
                    i5 = i9 + iComputeDoubleSize;
                    break;
                case 23:
                    iComputeDoubleSize = SchemaUtil.computeSizeFixed64List(iNumberAt, (List) unsafe.getObject(obj2, jOffset), false);
                    i5 = i9 + iComputeDoubleSize;
                    break;
                case 24:
                    iComputeDoubleSize = SchemaUtil.computeSizeFixed32List(iNumberAt, (List) unsafe.getObject(obj2, jOffset), false);
                    i5 = i9 + iComputeDoubleSize;
                    break;
                case 25:
                    iComputeDoubleSize = SchemaUtil.computeSizeBoolList(iNumberAt, (List) unsafe.getObject(obj2, jOffset), false);
                    i5 = i9 + iComputeDoubleSize;
                    break;
                case 26:
                    iComputeDoubleSize = SchemaUtil.computeSizeStringList(iNumberAt, (List) unsafe.getObject(obj2, jOffset));
                    i5 = i9 + iComputeDoubleSize;
                    break;
                case 27:
                    iComputeDoubleSize = SchemaUtil.computeSizeMessageList(iNumberAt, (List) unsafe.getObject(obj2, jOffset), messageSchema.getMessageFieldSchema(i3));
                    i5 = i9 + iComputeDoubleSize;
                    break;
                case 28:
                    iComputeDoubleSize = SchemaUtil.computeSizeByteStringList(iNumberAt, (List) unsafe.getObject(obj2, jOffset));
                    i5 = i9 + iComputeDoubleSize;
                    break;
                case 29:
                    iComputeDoubleSize = SchemaUtil.computeSizeUInt32List(iNumberAt, (List) unsafe.getObject(obj2, jOffset), false);
                    i5 = i9 + iComputeDoubleSize;
                    break;
                case 30:
                    iComputeDoubleSize = SchemaUtil.computeSizeEnumList(iNumberAt, (List) unsafe.getObject(obj2, jOffset), false);
                    i5 = i9 + iComputeDoubleSize;
                    break;
                case 31:
                    iComputeDoubleSize = SchemaUtil.computeSizeFixed32List(iNumberAt, (List) unsafe.getObject(obj2, jOffset), false);
                    i5 = i9 + iComputeDoubleSize;
                    break;
                case 32:
                    iComputeDoubleSize = SchemaUtil.computeSizeFixed64List(iNumberAt, (List) unsafe.getObject(obj2, jOffset), false);
                    i5 = i9 + iComputeDoubleSize;
                    break;
                case 33:
                    iComputeDoubleSize = SchemaUtil.computeSizeSInt32List(iNumberAt, (List) unsafe.getObject(obj2, jOffset), false);
                    i5 = i9 + iComputeDoubleSize;
                    break;
                case 34:
                    iComputeDoubleSize = SchemaUtil.computeSizeSInt64List(iNumberAt, (List) unsafe.getObject(obj2, jOffset), false);
                    i5 = i9 + iComputeDoubleSize;
                    break;
                case 35:
                    iComputeSizeFixed64ListNoTag = SchemaUtil.computeSizeFixed64ListNoTag((List) unsafe.getObject(obj2, jOffset));
                    if (iComputeSizeFixed64ListNoTag <= 0) {
                        i5 = i9;
                    } else {
                        if (messageSchema.useCachedSizeField) {
                            unsafe.putInt(obj2, i8, iComputeSizeFixed64ListNoTag);
                        }
                        iComputeTagSize = CodedOutputStream.computeTagSize(iNumberAt);
                        iComputeUInt32SizeNoTag = CodedOutputStream.computeUInt32SizeNoTag(iComputeSizeFixed64ListNoTag);
                        i5 = i9 + iComputeTagSize + iComputeUInt32SizeNoTag + iComputeSizeFixed64ListNoTag;
                    }
                    break;
                case 36:
                    iComputeSizeFixed64ListNoTag = SchemaUtil.computeSizeFixed32ListNoTag((List) unsafe.getObject(obj2, jOffset));
                    if (iComputeSizeFixed64ListNoTag <= 0) {
                        i5 = i9;
                    } else {
                        if (messageSchema.useCachedSizeField) {
                            unsafe.putInt(obj2, i8, iComputeSizeFixed64ListNoTag);
                        }
                        iComputeTagSize = CodedOutputStream.computeTagSize(iNumberAt);
                        iComputeUInt32SizeNoTag = CodedOutputStream.computeUInt32SizeNoTag(iComputeSizeFixed64ListNoTag);
                        i5 = i9 + iComputeTagSize + iComputeUInt32SizeNoTag + iComputeSizeFixed64ListNoTag;
                    }
                    break;
                case 37:
                    iComputeSizeFixed64ListNoTag = SchemaUtil.computeSizeInt64ListNoTag((List) unsafe.getObject(obj2, jOffset));
                    if (iComputeSizeFixed64ListNoTag <= 0) {
                        i5 = i9;
                    } else {
                        if (messageSchema.useCachedSizeField) {
                            unsafe.putInt(obj2, i8, iComputeSizeFixed64ListNoTag);
                        }
                        iComputeTagSize = CodedOutputStream.computeTagSize(iNumberAt);
                        iComputeUInt32SizeNoTag = CodedOutputStream.computeUInt32SizeNoTag(iComputeSizeFixed64ListNoTag);
                        i5 = i9 + iComputeTagSize + iComputeUInt32SizeNoTag + iComputeSizeFixed64ListNoTag;
                    }
                    break;
                case 38:
                    iComputeSizeFixed64ListNoTag = SchemaUtil.computeSizeUInt64ListNoTag((List) unsafe.getObject(obj2, jOffset));
                    if (iComputeSizeFixed64ListNoTag <= 0) {
                        i5 = i9;
                    } else {
                        if (messageSchema.useCachedSizeField) {
                            unsafe.putInt(obj2, i8, iComputeSizeFixed64ListNoTag);
                        }
                        iComputeTagSize = CodedOutputStream.computeTagSize(iNumberAt);
                        iComputeUInt32SizeNoTag = CodedOutputStream.computeUInt32SizeNoTag(iComputeSizeFixed64ListNoTag);
                        i5 = i9 + iComputeTagSize + iComputeUInt32SizeNoTag + iComputeSizeFixed64ListNoTag;
                    }
                    break;
                case 39:
                    iComputeSizeFixed64ListNoTag = SchemaUtil.computeSizeInt32ListNoTag((List) unsafe.getObject(obj2, jOffset));
                    if (iComputeSizeFixed64ListNoTag <= 0) {
                        i5 = i9;
                    } else {
                        if (messageSchema.useCachedSizeField) {
                            unsafe.putInt(obj2, i8, iComputeSizeFixed64ListNoTag);
                        }
                        iComputeTagSize = CodedOutputStream.computeTagSize(iNumberAt);
                        iComputeUInt32SizeNoTag = CodedOutputStream.computeUInt32SizeNoTag(iComputeSizeFixed64ListNoTag);
                        i5 = i9 + iComputeTagSize + iComputeUInt32SizeNoTag + iComputeSizeFixed64ListNoTag;
                    }
                    break;
                case 40:
                    iComputeSizeFixed64ListNoTag = SchemaUtil.computeSizeFixed64ListNoTag((List) unsafe.getObject(obj2, jOffset));
                    if (iComputeSizeFixed64ListNoTag <= 0) {
                        i5 = i9;
                    } else {
                        if (messageSchema.useCachedSizeField) {
                            unsafe.putInt(obj2, i8, iComputeSizeFixed64ListNoTag);
                        }
                        iComputeTagSize = CodedOutputStream.computeTagSize(iNumberAt);
                        iComputeUInt32SizeNoTag = CodedOutputStream.computeUInt32SizeNoTag(iComputeSizeFixed64ListNoTag);
                        i5 = i9 + iComputeTagSize + iComputeUInt32SizeNoTag + iComputeSizeFixed64ListNoTag;
                    }
                    break;
                case 41:
                    iComputeSizeFixed64ListNoTag = SchemaUtil.computeSizeFixed32ListNoTag((List) unsafe.getObject(obj2, jOffset));
                    if (iComputeSizeFixed64ListNoTag <= 0) {
                        i5 = i9;
                    } else {
                        if (messageSchema.useCachedSizeField) {
                            unsafe.putInt(obj2, i8, iComputeSizeFixed64ListNoTag);
                        }
                        iComputeTagSize = CodedOutputStream.computeTagSize(iNumberAt);
                        iComputeUInt32SizeNoTag = CodedOutputStream.computeUInt32SizeNoTag(iComputeSizeFixed64ListNoTag);
                        i5 = i9 + iComputeTagSize + iComputeUInt32SizeNoTag + iComputeSizeFixed64ListNoTag;
                    }
                    break;
                case 42:
                    iComputeSizeFixed64ListNoTag = SchemaUtil.computeSizeBoolListNoTag((List) unsafe.getObject(obj2, jOffset));
                    if (iComputeSizeFixed64ListNoTag <= 0) {
                        i5 = i9;
                    } else {
                        if (messageSchema.useCachedSizeField) {
                            unsafe.putInt(obj2, i8, iComputeSizeFixed64ListNoTag);
                        }
                        iComputeTagSize = CodedOutputStream.computeTagSize(iNumberAt);
                        iComputeUInt32SizeNoTag = CodedOutputStream.computeUInt32SizeNoTag(iComputeSizeFixed64ListNoTag);
                        i5 = i9 + iComputeTagSize + iComputeUInt32SizeNoTag + iComputeSizeFixed64ListNoTag;
                    }
                    break;
                case 43:
                    iComputeSizeFixed64ListNoTag = SchemaUtil.computeSizeUInt32ListNoTag((List) unsafe.getObject(obj2, jOffset));
                    if (iComputeSizeFixed64ListNoTag <= 0) {
                        i5 = i9;
                    } else {
                        if (messageSchema.useCachedSizeField) {
                            unsafe.putInt(obj2, i8, iComputeSizeFixed64ListNoTag);
                        }
                        iComputeTagSize = CodedOutputStream.computeTagSize(iNumberAt);
                        iComputeUInt32SizeNoTag = CodedOutputStream.computeUInt32SizeNoTag(iComputeSizeFixed64ListNoTag);
                        i5 = i9 + iComputeTagSize + iComputeUInt32SizeNoTag + iComputeSizeFixed64ListNoTag;
                    }
                    break;
                case 44:
                    iComputeSizeFixed64ListNoTag = SchemaUtil.computeSizeEnumListNoTag((List) unsafe.getObject(obj2, jOffset));
                    if (iComputeSizeFixed64ListNoTag <= 0) {
                        i5 = i9;
                    } else {
                        if (messageSchema.useCachedSizeField) {
                            unsafe.putInt(obj2, i8, iComputeSizeFixed64ListNoTag);
                        }
                        iComputeTagSize = CodedOutputStream.computeTagSize(iNumberAt);
                        iComputeUInt32SizeNoTag = CodedOutputStream.computeUInt32SizeNoTag(iComputeSizeFixed64ListNoTag);
                        i5 = i9 + iComputeTagSize + iComputeUInt32SizeNoTag + iComputeSizeFixed64ListNoTag;
                    }
                    break;
                case 45:
                    iComputeSizeFixed64ListNoTag = SchemaUtil.computeSizeFixed32ListNoTag((List) unsafe.getObject(obj2, jOffset));
                    if (iComputeSizeFixed64ListNoTag <= 0) {
                        i5 = i9;
                    } else {
                        if (messageSchema.useCachedSizeField) {
                            unsafe.putInt(obj2, i8, iComputeSizeFixed64ListNoTag);
                        }
                        iComputeTagSize = CodedOutputStream.computeTagSize(iNumberAt);
                        iComputeUInt32SizeNoTag = CodedOutputStream.computeUInt32SizeNoTag(iComputeSizeFixed64ListNoTag);
                        i5 = i9 + iComputeTagSize + iComputeUInt32SizeNoTag + iComputeSizeFixed64ListNoTag;
                    }
                    break;
                case 46:
                    iComputeSizeFixed64ListNoTag = SchemaUtil.computeSizeFixed64ListNoTag((List) unsafe.getObject(obj2, jOffset));
                    if (iComputeSizeFixed64ListNoTag <= 0) {
                        i5 = i9;
                    } else {
                        if (messageSchema.useCachedSizeField) {
                            unsafe.putInt(obj2, i8, iComputeSizeFixed64ListNoTag);
                        }
                        iComputeTagSize = CodedOutputStream.computeTagSize(iNumberAt);
                        iComputeUInt32SizeNoTag = CodedOutputStream.computeUInt32SizeNoTag(iComputeSizeFixed64ListNoTag);
                        i5 = i9 + iComputeTagSize + iComputeUInt32SizeNoTag + iComputeSizeFixed64ListNoTag;
                    }
                    break;
                case 47:
                    iComputeSizeFixed64ListNoTag = SchemaUtil.computeSizeSInt32ListNoTag((List) unsafe.getObject(obj2, jOffset));
                    if (iComputeSizeFixed64ListNoTag <= 0) {
                        i5 = i9;
                    } else {
                        if (messageSchema.useCachedSizeField) {
                            unsafe.putInt(obj2, i8, iComputeSizeFixed64ListNoTag);
                        }
                        iComputeTagSize = CodedOutputStream.computeTagSize(iNumberAt);
                        iComputeUInt32SizeNoTag = CodedOutputStream.computeUInt32SizeNoTag(iComputeSizeFixed64ListNoTag);
                        i5 = i9 + iComputeTagSize + iComputeUInt32SizeNoTag + iComputeSizeFixed64ListNoTag;
                    }
                    break;
                case 48:
                    iComputeSizeFixed64ListNoTag = SchemaUtil.computeSizeSInt64ListNoTag((List) unsafe.getObject(obj2, jOffset));
                    if (iComputeSizeFixed64ListNoTag <= 0) {
                        i5 = i9;
                    } else {
                        if (messageSchema.useCachedSizeField) {
                            unsafe.putInt(obj2, i8, iComputeSizeFixed64ListNoTag);
                        }
                        iComputeTagSize = CodedOutputStream.computeTagSize(iNumberAt);
                        iComputeUInt32SizeNoTag = CodedOutputStream.computeUInt32SizeNoTag(iComputeSizeFixed64ListNoTag);
                        i5 = i9 + iComputeTagSize + iComputeUInt32SizeNoTag + iComputeSizeFixed64ListNoTag;
                    }
                    break;
                case 49:
                    iComputeDoubleSize = SchemaUtil.computeSizeGroupList(iNumberAt, (List) unsafe.getObject(obj2, jOffset), messageSchema.getMessageFieldSchema(i3));
                    i5 = i9 + iComputeDoubleSize;
                    break;
                case 50:
                    iComputeDoubleSize = messageSchema.mapFieldSchema.getSerializedSize(iNumberAt, unsafe.getObject(obj2, jOffset), messageSchema.getMapFieldDefaultEntry(i3));
                    i5 = i9 + iComputeDoubleSize;
                    break;
                case 51:
                    if (!messageSchema.isOneofPresent(obj2, iNumberAt, i3)) {
                        i5 = i9;
                    } else {
                        iComputeDoubleSize = CodedOutputStream.computeDoubleSize(iNumberAt, 0.0d);
                        i5 = i9 + iComputeDoubleSize;
                    }
                    break;
                case 52:
                    if (!messageSchema.isOneofPresent(obj2, iNumberAt, i3)) {
                        i5 = i9;
                    } else {
                        iComputeDoubleSize = CodedOutputStream.computeFloatSize(iNumberAt, 0.0f);
                        i5 = i9 + iComputeDoubleSize;
                    }
                    break;
                case 53:
                    if (!messageSchema.isOneofPresent(obj2, iNumberAt, i3)) {
                        i5 = i9;
                    } else {
                        iComputeDoubleSize = CodedOutputStream.computeInt64Size(iNumberAt, oneofLongAt(obj2, jOffset));
                        i5 = i9 + iComputeDoubleSize;
                    }
                    break;
                case 54:
                    if (!messageSchema.isOneofPresent(obj2, iNumberAt, i3)) {
                        i5 = i9;
                    } else {
                        iComputeDoubleSize = CodedOutputStream.computeUInt64Size(iNumberAt, oneofLongAt(obj2, jOffset));
                        i5 = i9 + iComputeDoubleSize;
                    }
                    break;
                case 55:
                    if (!messageSchema.isOneofPresent(obj2, iNumberAt, i3)) {
                        i5 = i9;
                    } else {
                        iComputeDoubleSize = CodedOutputStream.computeInt32Size(iNumberAt, oneofIntAt(obj2, jOffset));
                        i5 = i9 + iComputeDoubleSize;
                    }
                    break;
                case 56:
                    if (!messageSchema.isOneofPresent(obj2, iNumberAt, i3)) {
                        i5 = i9;
                    } else {
                        iComputeDoubleSize = CodedOutputStream.computeFixed64Size(iNumberAt, 0L);
                        i5 = i9 + iComputeDoubleSize;
                    }
                    break;
                case 57:
                    if (!messageSchema.isOneofPresent(obj2, iNumberAt, i3)) {
                        i5 = i9;
                    } else {
                        iComputeDoubleSize = CodedOutputStream.computeFixed32Size(iNumberAt, 0);
                        i5 = i9 + iComputeDoubleSize;
                    }
                    break;
                case 58:
                    if (!messageSchema.isOneofPresent(obj2, iNumberAt, i3)) {
                        i5 = i9;
                    } else {
                        iComputeDoubleSize = CodedOutputStream.computeBoolSize(iNumberAt, true);
                        i5 = i9 + iComputeDoubleSize;
                    }
                    break;
                case 59:
                    if (!messageSchema.isOneofPresent(obj2, iNumberAt, i3)) {
                        i5 = i9;
                    } else {
                        Object object2 = unsafe.getObject(obj2, jOffset);
                        if (object2 instanceof ByteString) {
                            iComputeDoubleSize = CodedOutputStream.computeBytesSize(iNumberAt, (ByteString) object2);
                        } else {
                            iComputeDoubleSize = CodedOutputStream.computeStringSize(iNumberAt, (String) object2);
                        }
                        i5 = i9 + iComputeDoubleSize;
                    }
                    break;
                case 60:
                    if (!messageSchema.isOneofPresent(obj2, iNumberAt, i3)) {
                        i5 = i9;
                    } else {
                        iComputeDoubleSize = SchemaUtil.computeSizeMessage(iNumberAt, unsafe.getObject(obj2, jOffset), messageSchema.getMessageFieldSchema(i3));
                        i5 = i9 + iComputeDoubleSize;
                    }
                    break;
                case 61:
                    if (!messageSchema.isOneofPresent(obj2, iNumberAt, i3)) {
                        i5 = i9;
                    } else {
                        iComputeDoubleSize = CodedOutputStream.computeBytesSize(iNumberAt, (ByteString) unsafe.getObject(obj2, jOffset));
                        i5 = i9 + iComputeDoubleSize;
                    }
                    break;
                case 62:
                    if (!messageSchema.isOneofPresent(obj2, iNumberAt, i3)) {
                        i5 = i9;
                    } else {
                        iComputeDoubleSize = CodedOutputStream.computeUInt32Size(iNumberAt, oneofIntAt(obj2, jOffset));
                        i5 = i9 + iComputeDoubleSize;
                    }
                    break;
                case 63:
                    if (!messageSchema.isOneofPresent(obj2, iNumberAt, i3)) {
                        i5 = i9;
                    } else {
                        iComputeDoubleSize = CodedOutputStream.computeEnumSize(iNumberAt, oneofIntAt(obj2, jOffset));
                        i5 = i9 + iComputeDoubleSize;
                    }
                    break;
                case 64:
                    if (!messageSchema.isOneofPresent(obj2, iNumberAt, i3)) {
                        i5 = i9;
                    } else {
                        iComputeDoubleSize = CodedOutputStream.computeSFixed32Size(iNumberAt, 0);
                        i5 = i9 + iComputeDoubleSize;
                    }
                    break;
                case 65:
                    if (!messageSchema.isOneofPresent(obj2, iNumberAt, i3)) {
                        i5 = i9;
                    } else {
                        iComputeDoubleSize = CodedOutputStream.computeSFixed64Size(iNumberAt, 0L);
                        i5 = i9 + iComputeDoubleSize;
                    }
                    break;
                case 66:
                    if (!messageSchema.isOneofPresent(obj2, iNumberAt, i3)) {
                        i5 = i9;
                    } else {
                        iComputeDoubleSize = CodedOutputStream.computeSInt32Size(iNumberAt, oneofIntAt(obj2, jOffset));
                        i5 = i9 + iComputeDoubleSize;
                    }
                    break;
                case 67:
                    if (!messageSchema.isOneofPresent(obj2, iNumberAt, i3)) {
                        i5 = i9;
                    } else {
                        iComputeDoubleSize = CodedOutputStream.computeSInt64Size(iNumberAt, oneofLongAt(obj2, jOffset));
                        i5 = i9 + iComputeDoubleSize;
                    }
                    break;
                case 68:
                    if (!messageSchema.isOneofPresent(obj2, iNumberAt, i3)) {
                        i5 = i9;
                    } else {
                        iComputeDoubleSize = CodedOutputStream.computeGroupSize(iNumberAt, (MessageLite) unsafe.getObject(obj2, jOffset), messageSchema.getMessageFieldSchema(i3));
                        i5 = i9 + iComputeDoubleSize;
                    }
                    break;
                default:
                    i5 = i9;
                    break;
            }
            i3 += 3;
            i2 = 1048575;
        }
        int unknownFieldsSerializedSize = i5 + messageSchema.getUnknownFieldsSerializedSize(messageSchema.unknownFieldSchema, obj2);
        return messageSchema.hasExtensions ? unknownFieldsSerializedSize + messageSchema.extensionSchema.getExtensions(obj2).getSerializedSize() : unknownFieldsSerializedSize;
    }

    private int getUnknownFieldsSerializedSize(UnknownFieldSchema unknownFieldSchema, Object obj) {
        return unknownFieldSchema.getSerializedSize(unknownFieldSchema.getFromMessage(obj));
    }

    @Override // androidx.datastore.preferences.protobuf.Schema
    public void writeTo(Object obj, Writer writer) {
        if (writer.fieldOrder() == Writer.FieldOrder.DESCENDING) {
            writeFieldsInDescendingOrder(obj, writer);
        } else {
            writeFieldsInAscendingOrder(obj, writer);
        }
    }

    /* JADX WARN: Can't fix incorrect switch cases order, some code will duplicate */
    /* JADX WARN: Code duplicated, block: B:7:0x0022  */
    private void writeFieldsInAscendingOrder(Object obj, Writer writer) {
        Map.Entry entry;
        Iterator it;
        boolean z;
        int i;
        int i2;
        int i3;
        boolean z2;
        MessageSchema messageSchema = this;
        if (messageSchema.hasExtensions) {
            FieldSet extensions = messageSchema.extensionSchema.getExtensions(obj);
            if (extensions.isEmpty()) {
                entry = null;
                it = null;
            } else {
                Iterator it2 = extensions.iterator();
                entry = (Map.Entry) it2.next();
                it = it2;
            }
        } else {
            entry = null;
            it = null;
        }
        int length = messageSchema.buffer.length;
        Unsafe unsafe = UNSAFE;
        int i4 = 1048575;
        int i5 = 0;
        int i6 = 0;
        while (i5 < length) {
            int iTypeAndOffsetAt = messageSchema.typeAndOffsetAt(i5);
            int iNumberAt = messageSchema.numberAt(i5);
            int iType = type(iTypeAndOffsetAt);
            if (iType <= 17) {
                int i7 = messageSchema.buffer[i5 + 2];
                z = true;
                int i8 = i7 & 1048575;
                if (i8 != i4) {
                    i6 = i8 == 1048575 ? 0 : unsafe.getInt(obj, i8);
                    i4 = i8;
                }
                i = i4;
                i2 = i6;
                i3 = 1 << (i7 >>> 20);
            } else {
                z = true;
                i = i4;
                i2 = i6;
                i3 = 0;
            }
            while (entry != null && messageSchema.extensionSchema.extensionNumber(entry) <= iNumberAt) {
                messageSchema.extensionSchema.serializeExtension(writer, entry);
                entry = it.hasNext() ? (Map.Entry) it.next() : null;
            }
            long jOffset = offset(iTypeAndOffsetAt);
            switch (iType) {
                case 0:
                    if (messageSchema.isFieldPresent(obj, i5, i, i2, i3)) {
                        writer.writeDouble(iNumberAt, doubleAt(obj, jOffset));
                    }
                    break;
                case 1:
                    if (messageSchema.isFieldPresent(obj, i5, i, i2, i3)) {
                        writer.writeFloat(iNumberAt, floatAt(obj, jOffset));
                    }
                    messageSchema = this;
                    break;
                case 2:
                    if (messageSchema.isFieldPresent(obj, i5, i, i2, i3)) {
                        writer.writeInt64(iNumberAt, unsafe.getLong(obj, jOffset));
                    }
                    messageSchema = this;
                    break;
                case 3:
                    if (messageSchema.isFieldPresent(obj, i5, i, i2, i3)) {
                        writer.writeUInt64(iNumberAt, unsafe.getLong(obj, jOffset));
                    }
                    messageSchema = this;
                    break;
                case 4:
                    if (messageSchema.isFieldPresent(obj, i5, i, i2, i3)) {
                        writer.writeInt32(iNumberAt, unsafe.getInt(obj, jOffset));
                    }
                    messageSchema = this;
                    break;
                case 5:
                    if (messageSchema.isFieldPresent(obj, i5, i, i2, i3)) {
                        writer.writeFixed64(iNumberAt, unsafe.getLong(obj, jOffset));
                    }
                    messageSchema = this;
                    break;
                case 6:
                    if (messageSchema.isFieldPresent(obj, i5, i, i2, i3)) {
                        writer.writeFixed32(iNumberAt, unsafe.getInt(obj, jOffset));
                    }
                    messageSchema = this;
                    break;
                case 7:
                    if (messageSchema.isFieldPresent(obj, i5, i, i2, i3)) {
                        writer.writeBool(iNumberAt, booleanAt(obj, jOffset));
                    }
                    messageSchema = this;
                    break;
                case 8:
                    if (messageSchema.isFieldPresent(obj, i5, i, i2, i3)) {
                        messageSchema.writeString(iNumberAt, unsafe.getObject(obj, jOffset), writer);
                    }
                    break;
                case 9:
                    if (messageSchema.isFieldPresent(obj, i5, i, i2, i3)) {
                        writer.writeMessage(iNumberAt, unsafe.getObject(obj, jOffset), messageSchema.getMessageFieldSchema(i5));
                    }
                    break;
                case 10:
                    if (messageSchema.isFieldPresent(obj, i5, i, i2, i3)) {
                        writer.writeBytes(iNumberAt, (ByteString) unsafe.getObject(obj, jOffset));
                    }
                    messageSchema = this;
                    break;
                case 11:
                    if (messageSchema.isFieldPresent(obj, i5, i, i2, i3)) {
                        writer.writeUInt32(iNumberAt, unsafe.getInt(obj, jOffset));
                    }
                    messageSchema = this;
                    break;
                case 12:
                    if (messageSchema.isFieldPresent(obj, i5, i, i2, i3)) {
                        writer.writeEnum(iNumberAt, unsafe.getInt(obj, jOffset));
                    }
                    messageSchema = this;
                    break;
                case 13:
                    if (messageSchema.isFieldPresent(obj, i5, i, i2, i3)) {
                        writer.writeSFixed32(iNumberAt, unsafe.getInt(obj, jOffset));
                    }
                    messageSchema = this;
                    break;
                case 14:
                    if (messageSchema.isFieldPresent(obj, i5, i, i2, i3)) {
                        writer.writeSFixed64(iNumberAt, unsafe.getLong(obj, jOffset));
                    }
                    messageSchema = this;
                    break;
                case 15:
                    if (messageSchema.isFieldPresent(obj, i5, i, i2, i3)) {
                        writer.writeSInt32(iNumberAt, unsafe.getInt(obj, jOffset));
                    }
                    messageSchema = this;
                    break;
                case 16:
                    if (messageSchema.isFieldPresent(obj, i5, i, i2, i3)) {
                        writer.writeSInt64(iNumberAt, unsafe.getLong(obj, jOffset));
                    }
                    messageSchema = this;
                    break;
                case 17:
                    if (messageSchema.isFieldPresent(obj, i5, i, i2, i3)) {
                        writer.writeGroup(iNumberAt, unsafe.getObject(obj, jOffset), messageSchema.getMessageFieldSchema(i5));
                    }
                    break;
                case 18:
                    SchemaUtil.writeDoubleList(messageSchema.numberAt(i5), (List) unsafe.getObject(obj, jOffset), writer, false);
                    break;
                case 19:
                    SchemaUtil.writeFloatList(messageSchema.numberAt(i5), (List) unsafe.getObject(obj, jOffset), writer, false);
                    break;
                case 20:
                    SchemaUtil.writeInt64List(messageSchema.numberAt(i5), (List) unsafe.getObject(obj, jOffset), writer, false);
                    break;
                case 21:
                    SchemaUtil.writeUInt64List(messageSchema.numberAt(i5), (List) unsafe.getObject(obj, jOffset), writer, false);
                    break;
                case 22:
                    SchemaUtil.writeInt32List(messageSchema.numberAt(i5), (List) unsafe.getObject(obj, jOffset), writer, false);
                    break;
                case 23:
                    SchemaUtil.writeFixed64List(messageSchema.numberAt(i5), (List) unsafe.getObject(obj, jOffset), writer, false);
                    break;
                case 24:
                    SchemaUtil.writeFixed32List(messageSchema.numberAt(i5), (List) unsafe.getObject(obj, jOffset), writer, false);
                    break;
                case 25:
                    SchemaUtil.writeBoolList(messageSchema.numberAt(i5), (List) unsafe.getObject(obj, jOffset), writer, false);
                    break;
                case 26:
                    SchemaUtil.writeStringList(messageSchema.numberAt(i5), (List) unsafe.getObject(obj, jOffset), writer);
                    break;
                case 27:
                    SchemaUtil.writeMessageList(messageSchema.numberAt(i5), (List) unsafe.getObject(obj, jOffset), writer, messageSchema.getMessageFieldSchema(i5));
                    break;
                case 28:
                    SchemaUtil.writeBytesList(messageSchema.numberAt(i5), (List) unsafe.getObject(obj, jOffset), writer);
                    break;
                case 29:
                    z2 = false;
                    SchemaUtil.writeUInt32List(messageSchema.numberAt(i5), (List) unsafe.getObject(obj, jOffset), writer, false);
                    break;
                case 30:
                    z2 = false;
                    SchemaUtil.writeEnumList(messageSchema.numberAt(i5), (List) unsafe.getObject(obj, jOffset), writer, false);
                    break;
                case 31:
                    z2 = false;
                    SchemaUtil.writeSFixed32List(messageSchema.numberAt(i5), (List) unsafe.getObject(obj, jOffset), writer, false);
                    break;
                case 32:
                    z2 = false;
                    SchemaUtil.writeSFixed64List(messageSchema.numberAt(i5), (List) unsafe.getObject(obj, jOffset), writer, false);
                    break;
                case 33:
                    z2 = false;
                    SchemaUtil.writeSInt32List(messageSchema.numberAt(i5), (List) unsafe.getObject(obj, jOffset), writer, false);
                    break;
                case 34:
                    z2 = false;
                    SchemaUtil.writeSInt64List(messageSchema.numberAt(i5), (List) unsafe.getObject(obj, jOffset), writer, false);
                    break;
                case 35:
                    SchemaUtil.writeDoubleList(messageSchema.numberAt(i5), (List) unsafe.getObject(obj, jOffset), writer, z);
                    break;
                case 36:
                    SchemaUtil.writeFloatList(messageSchema.numberAt(i5), (List) unsafe.getObject(obj, jOffset), writer, z);
                    break;
                case 37:
                    SchemaUtil.writeInt64List(messageSchema.numberAt(i5), (List) unsafe.getObject(obj, jOffset), writer, z);
                    break;
                case 38:
                    SchemaUtil.writeUInt64List(messageSchema.numberAt(i5), (List) unsafe.getObject(obj, jOffset), writer, z);
                    break;
                case 39:
                    SchemaUtil.writeInt32List(messageSchema.numberAt(i5), (List) unsafe.getObject(obj, jOffset), writer, z);
                    break;
                case 40:
                    SchemaUtil.writeFixed64List(messageSchema.numberAt(i5), (List) unsafe.getObject(obj, jOffset), writer, z);
                    break;
                case 41:
                    SchemaUtil.writeFixed32List(messageSchema.numberAt(i5), (List) unsafe.getObject(obj, jOffset), writer, z);
                    break;
                case 42:
                    SchemaUtil.writeBoolList(messageSchema.numberAt(i5), (List) unsafe.getObject(obj, jOffset), writer, z);
                    break;
                case 43:
                    SchemaUtil.writeUInt32List(messageSchema.numberAt(i5), (List) unsafe.getObject(obj, jOffset), writer, z);
                    break;
                case 44:
                    SchemaUtil.writeEnumList(messageSchema.numberAt(i5), (List) unsafe.getObject(obj, jOffset), writer, z);
                    break;
                case 45:
                    SchemaUtil.writeSFixed32List(messageSchema.numberAt(i5), (List) unsafe.getObject(obj, jOffset), writer, z);
                    break;
                case 46:
                    SchemaUtil.writeSFixed64List(messageSchema.numberAt(i5), (List) unsafe.getObject(obj, jOffset), writer, z);
                    break;
                case 47:
                    SchemaUtil.writeSInt32List(messageSchema.numberAt(i5), (List) unsafe.getObject(obj, jOffset), writer, z);
                    break;
                case 48:
                    SchemaUtil.writeSInt64List(messageSchema.numberAt(i5), (List) unsafe.getObject(obj, jOffset), writer, z);
                    break;
                case 49:
                    SchemaUtil.writeGroupList(messageSchema.numberAt(i5), (List) unsafe.getObject(obj, jOffset), writer, messageSchema.getMessageFieldSchema(i5));
                    break;
                case 50:
                    messageSchema.writeMapHelper(writer, iNumberAt, unsafe.getObject(obj, jOffset), i5);
                    break;
                case 51:
                    if (messageSchema.isOneofPresent(obj, iNumberAt, i5)) {
                        writer.writeDouble(iNumberAt, oneofDoubleAt(obj, jOffset));
                    }
                    break;
                case 52:
                    if (messageSchema.isOneofPresent(obj, iNumberAt, i5)) {
                        writer.writeFloat(iNumberAt, oneofFloatAt(obj, jOffset));
                    }
                    break;
                case 53:
                    if (messageSchema.isOneofPresent(obj, iNumberAt, i5)) {
                        writer.writeInt64(iNumberAt, oneofLongAt(obj, jOffset));
                    }
                    break;
                case 54:
                    if (messageSchema.isOneofPresent(obj, iNumberAt, i5)) {
                        writer.writeUInt64(iNumberAt, oneofLongAt(obj, jOffset));
                    }
                    break;
                case 55:
                    if (messageSchema.isOneofPresent(obj, iNumberAt, i5)) {
                        writer.writeInt32(iNumberAt, oneofIntAt(obj, jOffset));
                    }
                    break;
                case 56:
                    if (messageSchema.isOneofPresent(obj, iNumberAt, i5)) {
                        writer.writeFixed64(iNumberAt, oneofLongAt(obj, jOffset));
                    }
                    break;
                case 57:
                    if (messageSchema.isOneofPresent(obj, iNumberAt, i5)) {
                        writer.writeFixed32(iNumberAt, oneofIntAt(obj, jOffset));
                    }
                    break;
                case 58:
                    if (messageSchema.isOneofPresent(obj, iNumberAt, i5)) {
                        writer.writeBool(iNumberAt, oneofBooleanAt(obj, jOffset));
                    }
                    break;
                case 59:
                    if (messageSchema.isOneofPresent(obj, iNumberAt, i5)) {
                        messageSchema.writeString(iNumberAt, unsafe.getObject(obj, jOffset), writer);
                    }
                    break;
                case 60:
                    if (messageSchema.isOneofPresent(obj, iNumberAt, i5)) {
                        writer.writeMessage(iNumberAt, unsafe.getObject(obj, jOffset), messageSchema.getMessageFieldSchema(i5));
                    }
                    break;
                case 61:
                    if (messageSchema.isOneofPresent(obj, iNumberAt, i5)) {
                        writer.writeBytes(iNumberAt, (ByteString) unsafe.getObject(obj, jOffset));
                    }
                    break;
                case 62:
                    if (messageSchema.isOneofPresent(obj, iNumberAt, i5)) {
                        writer.writeUInt32(iNumberAt, oneofIntAt(obj, jOffset));
                    }
                    break;
                case 63:
                    if (messageSchema.isOneofPresent(obj, iNumberAt, i5)) {
                        writer.writeEnum(iNumberAt, oneofIntAt(obj, jOffset));
                    }
                    break;
                case 64:
                    if (messageSchema.isOneofPresent(obj, iNumberAt, i5)) {
                        writer.writeSFixed32(iNumberAt, oneofIntAt(obj, jOffset));
                    }
                    break;
                case 65:
                    if (messageSchema.isOneofPresent(obj, iNumberAt, i5)) {
                        writer.writeSFixed64(iNumberAt, oneofLongAt(obj, jOffset));
                    }
                    break;
                case 66:
                    if (messageSchema.isOneofPresent(obj, iNumberAt, i5)) {
                        writer.writeSInt32(iNumberAt, oneofIntAt(obj, jOffset));
                    }
                    break;
                case 67:
                    if (messageSchema.isOneofPresent(obj, iNumberAt, i5)) {
                        writer.writeSInt64(iNumberAt, oneofLongAt(obj, jOffset));
                    }
                    break;
                case 68:
                    if (messageSchema.isOneofPresent(obj, iNumberAt, i5)) {
                        writer.writeGroup(iNumberAt, unsafe.getObject(obj, jOffset), messageSchema.getMessageFieldSchema(i5));
                    }
                    break;
                default:
                    break;
            }
            i5 += 3;
            i6 = i2;
            i4 = i;
            entry = entry;
        }
        while (entry != null) {
            messageSchema.extensionSchema.serializeExtension(writer, entry);
            entry = it.hasNext() ? (Map.Entry) it.next() : null;
        }
        messageSchema.writeUnknownInMessageTo(messageSchema.unknownFieldSchema, obj, writer);
    }

    /* JADX WARN: Code duplicated, block: B:7:0x0021  */
    private void writeFieldsInDescendingOrder(Object obj, Writer writer) {
        Iterator itDescendingIterator;
        Map.Entry entry;
        writeUnknownInMessageTo(this.unknownFieldSchema, obj, writer);
        if (this.hasExtensions) {
            FieldSet extensions = this.extensionSchema.getExtensions(obj);
            if (extensions.isEmpty()) {
                itDescendingIterator = null;
                entry = null;
            } else {
                itDescendingIterator = extensions.descendingIterator();
                entry = (Map.Entry) itDescendingIterator.next();
            }
        } else {
            itDescendingIterator = null;
            entry = null;
        }
        for (int length = this.buffer.length - 3; length >= 0; length -= 3) {
            int iTypeAndOffsetAt = typeAndOffsetAt(length);
            int iNumberAt = numberAt(length);
            while (entry != null && this.extensionSchema.extensionNumber(entry) > iNumberAt) {
                this.extensionSchema.serializeExtension(writer, entry);
                entry = itDescendingIterator.hasNext() ? (Map.Entry) itDescendingIterator.next() : null;
            }
            switch (type(iTypeAndOffsetAt)) {
                case 0:
                    if (isFieldPresent(obj, length)) {
                        writer.writeDouble(iNumberAt, doubleAt(obj, offset(iTypeAndOffsetAt)));
                    }
                    break;
                case 1:
                    if (isFieldPresent(obj, length)) {
                        writer.writeFloat(iNumberAt, floatAt(obj, offset(iTypeAndOffsetAt)));
                    }
                    break;
                case 2:
                    if (isFieldPresent(obj, length)) {
                        writer.writeInt64(iNumberAt, longAt(obj, offset(iTypeAndOffsetAt)));
                    }
                    break;
                case 3:
                    if (isFieldPresent(obj, length)) {
                        writer.writeUInt64(iNumberAt, longAt(obj, offset(iTypeAndOffsetAt)));
                    }
                    break;
                case 4:
                    if (isFieldPresent(obj, length)) {
                        writer.writeInt32(iNumberAt, intAt(obj, offset(iTypeAndOffsetAt)));
                    }
                    break;
                case 5:
                    if (isFieldPresent(obj, length)) {
                        writer.writeFixed64(iNumberAt, longAt(obj, offset(iTypeAndOffsetAt)));
                    }
                    break;
                case 6:
                    if (isFieldPresent(obj, length)) {
                        writer.writeFixed32(iNumberAt, intAt(obj, offset(iTypeAndOffsetAt)));
                    }
                    break;
                case 7:
                    if (isFieldPresent(obj, length)) {
                        writer.writeBool(iNumberAt, booleanAt(obj, offset(iTypeAndOffsetAt)));
                    }
                    break;
                case 8:
                    if (isFieldPresent(obj, length)) {
                        writeString(iNumberAt, UnsafeUtil.getObject(obj, offset(iTypeAndOffsetAt)), writer);
                    }
                    break;
                case 9:
                    if (isFieldPresent(obj, length)) {
                        writer.writeMessage(iNumberAt, UnsafeUtil.getObject(obj, offset(iTypeAndOffsetAt)), getMessageFieldSchema(length));
                    }
                    break;
                case 10:
                    if (isFieldPresent(obj, length)) {
                        writer.writeBytes(iNumberAt, (ByteString) UnsafeUtil.getObject(obj, offset(iTypeAndOffsetAt)));
                    }
                    break;
                case 11:
                    if (isFieldPresent(obj, length)) {
                        writer.writeUInt32(iNumberAt, intAt(obj, offset(iTypeAndOffsetAt)));
                    }
                    break;
                case 12:
                    if (isFieldPresent(obj, length)) {
                        writer.writeEnum(iNumberAt, intAt(obj, offset(iTypeAndOffsetAt)));
                    }
                    break;
                case 13:
                    if (isFieldPresent(obj, length)) {
                        writer.writeSFixed32(iNumberAt, intAt(obj, offset(iTypeAndOffsetAt)));
                    }
                    break;
                case 14:
                    if (isFieldPresent(obj, length)) {
                        writer.writeSFixed64(iNumberAt, longAt(obj, offset(iTypeAndOffsetAt)));
                    }
                    break;
                case 15:
                    if (isFieldPresent(obj, length)) {
                        writer.writeSInt32(iNumberAt, intAt(obj, offset(iTypeAndOffsetAt)));
                    }
                    break;
                case 16:
                    if (isFieldPresent(obj, length)) {
                        writer.writeSInt64(iNumberAt, longAt(obj, offset(iTypeAndOffsetAt)));
                    }
                    break;
                case 17:
                    if (isFieldPresent(obj, length)) {
                        writer.writeGroup(iNumberAt, UnsafeUtil.getObject(obj, offset(iTypeAndOffsetAt)), getMessageFieldSchema(length));
                    }
                    break;
                case 18:
                    SchemaUtil.writeDoubleList(numberAt(length), (List) UnsafeUtil.getObject(obj, offset(iTypeAndOffsetAt)), writer, false);
                    break;
                case 19:
                    SchemaUtil.writeFloatList(numberAt(length), (List) UnsafeUtil.getObject(obj, offset(iTypeAndOffsetAt)), writer, false);
                    break;
                case 20:
                    SchemaUtil.writeInt64List(numberAt(length), (List) UnsafeUtil.getObject(obj, offset(iTypeAndOffsetAt)), writer, false);
                    break;
                case 21:
                    SchemaUtil.writeUInt64List(numberAt(length), (List) UnsafeUtil.getObject(obj, offset(iTypeAndOffsetAt)), writer, false);
                    break;
                case 22:
                    SchemaUtil.writeInt32List(numberAt(length), (List) UnsafeUtil.getObject(obj, offset(iTypeAndOffsetAt)), writer, false);
                    break;
                case 23:
                    SchemaUtil.writeFixed64List(numberAt(length), (List) UnsafeUtil.getObject(obj, offset(iTypeAndOffsetAt)), writer, false);
                    break;
                case 24:
                    SchemaUtil.writeFixed32List(numberAt(length), (List) UnsafeUtil.getObject(obj, offset(iTypeAndOffsetAt)), writer, false);
                    break;
                case 25:
                    SchemaUtil.writeBoolList(numberAt(length), (List) UnsafeUtil.getObject(obj, offset(iTypeAndOffsetAt)), writer, false);
                    break;
                case 26:
                    SchemaUtil.writeStringList(numberAt(length), (List) UnsafeUtil.getObject(obj, offset(iTypeAndOffsetAt)), writer);
                    break;
                case 27:
                    SchemaUtil.writeMessageList(numberAt(length), (List) UnsafeUtil.getObject(obj, offset(iTypeAndOffsetAt)), writer, getMessageFieldSchema(length));
                    break;
                case 28:
                    SchemaUtil.writeBytesList(numberAt(length), (List) UnsafeUtil.getObject(obj, offset(iTypeAndOffsetAt)), writer);
                    break;
                case 29:
                    SchemaUtil.writeUInt32List(numberAt(length), (List) UnsafeUtil.getObject(obj, offset(iTypeAndOffsetAt)), writer, false);
                    break;
                case 30:
                    SchemaUtil.writeEnumList(numberAt(length), (List) UnsafeUtil.getObject(obj, offset(iTypeAndOffsetAt)), writer, false);
                    break;
                case 31:
                    SchemaUtil.writeSFixed32List(numberAt(length), (List) UnsafeUtil.getObject(obj, offset(iTypeAndOffsetAt)), writer, false);
                    break;
                case 32:
                    SchemaUtil.writeSFixed64List(numberAt(length), (List) UnsafeUtil.getObject(obj, offset(iTypeAndOffsetAt)), writer, false);
                    break;
                case 33:
                    SchemaUtil.writeSInt32List(numberAt(length), (List) UnsafeUtil.getObject(obj, offset(iTypeAndOffsetAt)), writer, false);
                    break;
                case 34:
                    SchemaUtil.writeSInt64List(numberAt(length), (List) UnsafeUtil.getObject(obj, offset(iTypeAndOffsetAt)), writer, false);
                    break;
                case 35:
                    SchemaUtil.writeDoubleList(numberAt(length), (List) UnsafeUtil.getObject(obj, offset(iTypeAndOffsetAt)), writer, true);
                    break;
                case 36:
                    SchemaUtil.writeFloatList(numberAt(length), (List) UnsafeUtil.getObject(obj, offset(iTypeAndOffsetAt)), writer, true);
                    break;
                case 37:
                    SchemaUtil.writeInt64List(numberAt(length), (List) UnsafeUtil.getObject(obj, offset(iTypeAndOffsetAt)), writer, true);
                    break;
                case 38:
                    SchemaUtil.writeUInt64List(numberAt(length), (List) UnsafeUtil.getObject(obj, offset(iTypeAndOffsetAt)), writer, true);
                    break;
                case 39:
                    SchemaUtil.writeInt32List(numberAt(length), (List) UnsafeUtil.getObject(obj, offset(iTypeAndOffsetAt)), writer, true);
                    break;
                case 40:
                    SchemaUtil.writeFixed64List(numberAt(length), (List) UnsafeUtil.getObject(obj, offset(iTypeAndOffsetAt)), writer, true);
                    break;
                case 41:
                    SchemaUtil.writeFixed32List(numberAt(length), (List) UnsafeUtil.getObject(obj, offset(iTypeAndOffsetAt)), writer, true);
                    break;
                case 42:
                    SchemaUtil.writeBoolList(numberAt(length), (List) UnsafeUtil.getObject(obj, offset(iTypeAndOffsetAt)), writer, true);
                    break;
                case 43:
                    SchemaUtil.writeUInt32List(numberAt(length), (List) UnsafeUtil.getObject(obj, offset(iTypeAndOffsetAt)), writer, true);
                    break;
                case 44:
                    SchemaUtil.writeEnumList(numberAt(length), (List) UnsafeUtil.getObject(obj, offset(iTypeAndOffsetAt)), writer, true);
                    break;
                case 45:
                    SchemaUtil.writeSFixed32List(numberAt(length), (List) UnsafeUtil.getObject(obj, offset(iTypeAndOffsetAt)), writer, true);
                    break;
                case 46:
                    SchemaUtil.writeSFixed64List(numberAt(length), (List) UnsafeUtil.getObject(obj, offset(iTypeAndOffsetAt)), writer, true);
                    break;
                case 47:
                    SchemaUtil.writeSInt32List(numberAt(length), (List) UnsafeUtil.getObject(obj, offset(iTypeAndOffsetAt)), writer, true);
                    break;
                case 48:
                    SchemaUtil.writeSInt64List(numberAt(length), (List) UnsafeUtil.getObject(obj, offset(iTypeAndOffsetAt)), writer, true);
                    break;
                case 49:
                    SchemaUtil.writeGroupList(numberAt(length), (List) UnsafeUtil.getObject(obj, offset(iTypeAndOffsetAt)), writer, getMessageFieldSchema(length));
                    break;
                case 50:
                    writeMapHelper(writer, iNumberAt, UnsafeUtil.getObject(obj, offset(iTypeAndOffsetAt)), length);
                    break;
                case 51:
                    if (isOneofPresent(obj, iNumberAt, length)) {
                        writer.writeDouble(iNumberAt, oneofDoubleAt(obj, offset(iTypeAndOffsetAt)));
                    }
                    break;
                case 52:
                    if (isOneofPresent(obj, iNumberAt, length)) {
                        writer.writeFloat(iNumberAt, oneofFloatAt(obj, offset(iTypeAndOffsetAt)));
                    }
                    break;
                case 53:
                    if (isOneofPresent(obj, iNumberAt, length)) {
                        writer.writeInt64(iNumberAt, oneofLongAt(obj, offset(iTypeAndOffsetAt)));
                    }
                    break;
                case 54:
                    if (isOneofPresent(obj, iNumberAt, length)) {
                        writer.writeUInt64(iNumberAt, oneofLongAt(obj, offset(iTypeAndOffsetAt)));
                    }
                    break;
                case 55:
                    if (isOneofPresent(obj, iNumberAt, length)) {
                        writer.writeInt32(iNumberAt, oneofIntAt(obj, offset(iTypeAndOffsetAt)));
                    }
                    break;
                case 56:
                    if (isOneofPresent(obj, iNumberAt, length)) {
                        writer.writeFixed64(iNumberAt, oneofLongAt(obj, offset(iTypeAndOffsetAt)));
                    }
                    break;
                case 57:
                    if (isOneofPresent(obj, iNumberAt, length)) {
                        writer.writeFixed32(iNumberAt, oneofIntAt(obj, offset(iTypeAndOffsetAt)));
                    }
                    break;
                case 58:
                    if (isOneofPresent(obj, iNumberAt, length)) {
                        writer.writeBool(iNumberAt, oneofBooleanAt(obj, offset(iTypeAndOffsetAt)));
                    }
                    break;
                case 59:
                    if (isOneofPresent(obj, iNumberAt, length)) {
                        writeString(iNumberAt, UnsafeUtil.getObject(obj, offset(iTypeAndOffsetAt)), writer);
                    }
                    break;
                case 60:
                    if (isOneofPresent(obj, iNumberAt, length)) {
                        writer.writeMessage(iNumberAt, UnsafeUtil.getObject(obj, offset(iTypeAndOffsetAt)), getMessageFieldSchema(length));
                    }
                    break;
                case 61:
                    if (isOneofPresent(obj, iNumberAt, length)) {
                        writer.writeBytes(iNumberAt, (ByteString) UnsafeUtil.getObject(obj, offset(iTypeAndOffsetAt)));
                    }
                    break;
                case 62:
                    if (isOneofPresent(obj, iNumberAt, length)) {
                        writer.writeUInt32(iNumberAt, oneofIntAt(obj, offset(iTypeAndOffsetAt)));
                    }
                    break;
                case 63:
                    if (isOneofPresent(obj, iNumberAt, length)) {
                        writer.writeEnum(iNumberAt, oneofIntAt(obj, offset(iTypeAndOffsetAt)));
                    }
                    break;
                case 64:
                    if (isOneofPresent(obj, iNumberAt, length)) {
                        writer.writeSFixed32(iNumberAt, oneofIntAt(obj, offset(iTypeAndOffsetAt)));
                    }
                    break;
                case 65:
                    if (isOneofPresent(obj, iNumberAt, length)) {
                        writer.writeSFixed64(iNumberAt, oneofLongAt(obj, offset(iTypeAndOffsetAt)));
                    }
                    break;
                case 66:
                    if (isOneofPresent(obj, iNumberAt, length)) {
                        writer.writeSInt32(iNumberAt, oneofIntAt(obj, offset(iTypeAndOffsetAt)));
                    }
                    break;
                case 67:
                    if (isOneofPresent(obj, iNumberAt, length)) {
                        writer.writeSInt64(iNumberAt, oneofLongAt(obj, offset(iTypeAndOffsetAt)));
                    }
                    break;
                case 68:
                    if (isOneofPresent(obj, iNumberAt, length)) {
                        writer.writeGroup(iNumberAt, UnsafeUtil.getObject(obj, offset(iTypeAndOffsetAt)), getMessageFieldSchema(length));
                    }
                    break;
            }
        }
        while (entry != null) {
            this.extensionSchema.serializeExtension(writer, entry);
            entry = itDescendingIterator.hasNext() ? (Map.Entry) itDescendingIterator.next() : null;
        }
    }

    private void writeMapHelper(Writer writer, int i, Object obj, int i2) {
        if (obj != null) {
            writer.writeMap(i, this.mapFieldSchema.forMapMetadata(getMapFieldDefaultEntry(i2)), this.mapFieldSchema.forMapData(obj));
        }
    }

    private void writeUnknownInMessageTo(UnknownFieldSchema unknownFieldSchema, Object obj, Writer writer) {
        unknownFieldSchema.writeTo(unknownFieldSchema.getFromMessage(obj), writer);
    }

    @Override // androidx.datastore.preferences.protobuf.Schema
    public void mergeFrom(Object obj, Reader reader, ExtensionRegistryLite extensionRegistryLite) throws Throwable {
        extensionRegistryLite.getClass();
        checkMutable(obj);
        mergeFromHelper(this.unknownFieldSchema, this.extensionSchema, obj, reader, extensionRegistryLite);
    }

    /* JADX WARN: Code duplicated, block: B:229:0x06d2 A[Catch: all -> 0x06f3, TRY_LEAVE, TryCatch #9 {all -> 0x06f3, blocks: (B:227:0x06cc, B:229:0x06d2, B:240:0x06f7, B:241:0x06fc), top: B:274:0x06cc }] */
    /* JADX WARN: Code duplicated, block: B:234:0x06df A[LOOP:2: B:232:0x06db->B:234:0x06df, LOOP_END] */
    /* JADX WARN: Code duplicated, block: B:239:0x06f5 A[DONT_INVERT] */
    /* JADX WARN: Code duplicated, block: B:240:0x06f7 A[Catch: all -> 0x06f3, TRY_ENTER, TryCatch #9 {all -> 0x06f3, blocks: (B:227:0x06cc, B:229:0x06d2, B:240:0x06f7, B:241:0x06fc), top: B:274:0x06cc }] */
    /* JADX WARN: Code duplicated, block: B:246:0x070a A[LOOP:3: B:244:0x0706->B:246:0x070a, LOOP_END] */
    /* JADX WARN: Code duplicated, block: B:256:0x072d A[LOOP:4: B:254:0x0729->B:256:0x072d, LOOP_END] */
    /* JADX WARN: Code duplicated, block: B:259:0x0740  */
    /* JADX WARN: Code duplicated, block: B:316:0x06d8 A[SYNTHETIC] */
    /* JADX WARN: Code duplicated, block: B:317:0x0703 A[SYNTHETIC] */
    /* JADX WARN: Code duplicated, block: B:332:? A[RETURN, SYNTHETIC] */
    /* JADX WARN: Code duplicated, block: B:334:? A[RETURN, SYNTHETIC] */
    private void mergeFromHelper(UnknownFieldSchema unknownFieldSchema, ExtensionSchema extensionSchema, Object obj, Reader reader, ExtensionRegistryLite extensionRegistryLite) throws Throwable {
        Object obj2;
        int i;
        Object objFilterMapUnknownEnumValues;
        UnknownFieldSchema unknownFieldSchema2;
        MessageSchema messageSchema;
        Object obj3;
        Object builderFromMessage;
        UnknownFieldSchema unknownFieldSchema3;
        ExtensionSchema extensionSchema2;
        Object objFindExtensionByNumber;
        UnknownFieldSchema unknownFieldSchema4;
        int i2;
        Object objFilterMapUnknownEnumValues2;
        int i3;
        Reader reader2;
        MessageSchema messageSchema2;
        Object obj4;
        Object obj5;
        MessageSchema messageSchema3 = this;
        ExtensionRegistryLite extensionRegistryLite2 = extensionRegistryLite;
        FieldSet mutableExtensions = null;
        Object extension = null;
        while (true) {
            try {
                int fieldNumber = reader.getFieldNumber();
                int iPositionForFieldNumber = messageSchema3.positionForFieldNumber(fieldNumber);
                if (iPositionForFieldNumber >= 0) {
                    obj3 = obj;
                    extensionRegistryLite = extensionRegistryLite2;
                    builderFromMessage = extension;
                    unknownFieldSchema3 = unknownFieldSchema;
                    Reader reader3 = reader;
                    try {
                        int iTypeAndOffsetAt = messageSchema3.typeAndOffsetAt(iPositionForFieldNumber);
                        try {
                            switch (type(iTypeAndOffsetAt)) {
                                case 0:
                                    UnsafeUtil.putDouble(obj3, offset(iTypeAndOffsetAt), reader3.readDouble());
                                    messageSchema3.setFieldPresent(obj3, iPositionForFieldNumber);
                                    extension = builderFromMessage;
                                    extensionRegistryLite2 = extensionRegistryLite;
                                    break;
                                case 1:
                                    UnsafeUtil.putFloat(obj3, offset(iTypeAndOffsetAt), reader3.readFloat());
                                    messageSchema3.setFieldPresent(obj3, iPositionForFieldNumber);
                                    extension = builderFromMessage;
                                    extensionRegistryLite2 = extensionRegistryLite;
                                    break;
                                case 2:
                                    UnsafeUtil.putLong(obj3, offset(iTypeAndOffsetAt), reader3.readInt64());
                                    messageSchema3.setFieldPresent(obj3, iPositionForFieldNumber);
                                    extension = builderFromMessage;
                                    extensionRegistryLite2 = extensionRegistryLite;
                                    break;
                                case 3:
                                    UnsafeUtil.putLong(obj3, offset(iTypeAndOffsetAt), reader3.readUInt64());
                                    messageSchema3.setFieldPresent(obj3, iPositionForFieldNumber);
                                    extension = builderFromMessage;
                                    extensionRegistryLite2 = extensionRegistryLite;
                                    break;
                                case 4:
                                    UnsafeUtil.putInt(obj3, offset(iTypeAndOffsetAt), reader3.readInt32());
                                    messageSchema3.setFieldPresent(obj3, iPositionForFieldNumber);
                                    extension = builderFromMessage;
                                    extensionRegistryLite2 = extensionRegistryLite;
                                    break;
                                case 5:
                                    UnsafeUtil.putLong(obj3, offset(iTypeAndOffsetAt), reader3.readFixed64());
                                    messageSchema3.setFieldPresent(obj3, iPositionForFieldNumber);
                                    extension = builderFromMessage;
                                    extensionRegistryLite2 = extensionRegistryLite;
                                    break;
                                case 6:
                                    UnsafeUtil.putInt(obj3, offset(iTypeAndOffsetAt), reader3.readFixed32());
                                    messageSchema3.setFieldPresent(obj3, iPositionForFieldNumber);
                                    extension = builderFromMessage;
                                    extensionRegistryLite2 = extensionRegistryLite;
                                    break;
                                case 7:
                                    UnsafeUtil.putBoolean(obj3, offset(iTypeAndOffsetAt), reader3.readBool());
                                    messageSchema3.setFieldPresent(obj3, iPositionForFieldNumber);
                                    extension = builderFromMessage;
                                    extensionRegistryLite2 = extensionRegistryLite;
                                    break;
                                case 8:
                                    messageSchema3.readString(obj3, iTypeAndOffsetAt, reader3);
                                    messageSchema3.setFieldPresent(obj3, iPositionForFieldNumber);
                                    extension = builderFromMessage;
                                    extensionRegistryLite2 = extensionRegistryLite;
                                    break;
                                case 9:
                                    MessageLite messageLite = (MessageLite) messageSchema3.mutableMessageFieldForMerge(obj3, iPositionForFieldNumber);
                                    reader3.mergeMessageField(messageLite, messageSchema3.getMessageFieldSchema(iPositionForFieldNumber), extensionRegistryLite);
                                    messageSchema3.storeMessageField(obj3, iPositionForFieldNumber, messageLite);
                                    extension = builderFromMessage;
                                    extensionRegistryLite2 = extensionRegistryLite;
                                    break;
                                case 10:
                                    UnsafeUtil.putObject(obj3, offset(iTypeAndOffsetAt), reader3.readBytes());
                                    messageSchema3.setFieldPresent(obj3, iPositionForFieldNumber);
                                    extension = builderFromMessage;
                                    extensionRegistryLite2 = extensionRegistryLite;
                                    break;
                                case 11:
                                    UnsafeUtil.putInt(obj3, offset(iTypeAndOffsetAt), reader3.readUInt32());
                                    messageSchema3.setFieldPresent(obj3, iPositionForFieldNumber);
                                    extension = builderFromMessage;
                                    extensionRegistryLite2 = extensionRegistryLite;
                                    break;
                                case 12:
                                    int i4 = reader3.readEnum();
                                    messageSchema3.getEnumFieldVerifier(iPositionForFieldNumber);
                                    UnsafeUtil.putInt(obj3, offset(iTypeAndOffsetAt), i4);
                                    messageSchema3.setFieldPresent(obj3, iPositionForFieldNumber);
                                    extension = builderFromMessage;
                                    extensionRegistryLite2 = extensionRegistryLite;
                                    break;
                                case 13:
                                    UnsafeUtil.putInt(obj3, offset(iTypeAndOffsetAt), reader3.readSFixed32());
                                    messageSchema3.setFieldPresent(obj3, iPositionForFieldNumber);
                                    extension = builderFromMessage;
                                    extensionRegistryLite2 = extensionRegistryLite;
                                    break;
                                case 14:
                                    UnsafeUtil.putLong(obj3, offset(iTypeAndOffsetAt), reader3.readSFixed64());
                                    messageSchema3.setFieldPresent(obj3, iPositionForFieldNumber);
                                    extension = builderFromMessage;
                                    extensionRegistryLite2 = extensionRegistryLite;
                                    break;
                                case 15:
                                    UnsafeUtil.putInt(obj3, offset(iTypeAndOffsetAt), reader3.readSInt32());
                                    messageSchema3.setFieldPresent(obj3, iPositionForFieldNumber);
                                    extension = builderFromMessage;
                                    extensionRegistryLite2 = extensionRegistryLite;
                                    break;
                                case 16:
                                    UnsafeUtil.putLong(obj3, offset(iTypeAndOffsetAt), reader3.readSInt64());
                                    messageSchema3.setFieldPresent(obj3, iPositionForFieldNumber);
                                    extension = builderFromMessage;
                                    extensionRegistryLite2 = extensionRegistryLite;
                                    break;
                                case 17:
                                    MessageLite messageLite2 = (MessageLite) messageSchema3.mutableMessageFieldForMerge(obj3, iPositionForFieldNumber);
                                    reader3.mergeGroupField(messageLite2, messageSchema3.getMessageFieldSchema(iPositionForFieldNumber), extensionRegistryLite);
                                    messageSchema3.storeMessageField(obj3, iPositionForFieldNumber, messageLite2);
                                    extension = builderFromMessage;
                                    extensionRegistryLite2 = extensionRegistryLite;
                                    break;
                                case 18:
                                    reader3.readDoubleList(messageSchema3.listFieldSchema.mutableListAt(obj3, offset(iTypeAndOffsetAt)));
                                    extension = builderFromMessage;
                                    extensionRegistryLite2 = extensionRegistryLite;
                                    break;
                                case 19:
                                    reader3.readFloatList(messageSchema3.listFieldSchema.mutableListAt(obj3, offset(iTypeAndOffsetAt)));
                                    extension = builderFromMessage;
                                    extensionRegistryLite2 = extensionRegistryLite;
                                    break;
                                case 20:
                                    reader3.readInt64List(messageSchema3.listFieldSchema.mutableListAt(obj3, offset(iTypeAndOffsetAt)));
                                    extension = builderFromMessage;
                                    extensionRegistryLite2 = extensionRegistryLite;
                                    break;
                                case 21:
                                    reader3.readUInt64List(messageSchema3.listFieldSchema.mutableListAt(obj3, offset(iTypeAndOffsetAt)));
                                    extension = builderFromMessage;
                                    extensionRegistryLite2 = extensionRegistryLite;
                                    break;
                                case 22:
                                    reader3.readInt32List(messageSchema3.listFieldSchema.mutableListAt(obj3, offset(iTypeAndOffsetAt)));
                                    extension = builderFromMessage;
                                    extensionRegistryLite2 = extensionRegistryLite;
                                    break;
                                case 23:
                                    reader3.readFixed64List(messageSchema3.listFieldSchema.mutableListAt(obj3, offset(iTypeAndOffsetAt)));
                                    extension = builderFromMessage;
                                    extensionRegistryLite2 = extensionRegistryLite;
                                    break;
                                case 24:
                                    reader3.readFixed32List(messageSchema3.listFieldSchema.mutableListAt(obj3, offset(iTypeAndOffsetAt)));
                                    extension = builderFromMessage;
                                    extensionRegistryLite2 = extensionRegistryLite;
                                    break;
                                case 25:
                                    reader3.readBoolList(messageSchema3.listFieldSchema.mutableListAt(obj3, offset(iTypeAndOffsetAt)));
                                    extension = builderFromMessage;
                                    extensionRegistryLite2 = extensionRegistryLite;
                                    break;
                                case 26:
                                    messageSchema3.readStringList(obj3, iTypeAndOffsetAt, reader3);
                                    extension = builderFromMessage;
                                    extensionRegistryLite2 = extensionRegistryLite;
                                    break;
                                case 27:
                                    unknownFieldSchema = unknownFieldSchema3;
                                    obj2 = obj3;
                                    MessageSchema messageSchema4 = messageSchema3;
                                    try {
                                        extensionRegistryLite = extensionRegistryLite;
                                        try {
                                            messageSchema3.readMessageList(obj2, iTypeAndOffsetAt, reader3, messageSchema4.getMessageFieldSchema(iPositionForFieldNumber), extensionRegistryLite);
                                            extension = builderFromMessage;
                                        } catch (InvalidProtocolBufferException.InvalidWireTypeException unused) {
                                            extension = builderFromMessage;
                                            try {
                                                if (unknownFieldSchema.shouldDiscardUnknownFields(reader3)) {
                                                    if (extension == null) {
                                                        extension = unknownFieldSchema.getBuilderFromMessage(obj2);
                                                    }
                                                    if (!unknownFieldSchema.mergeOneFieldFrom(extension, reader3, 0)) {
                                                        objFilterMapUnknownEnumValues2 = extension;
                                                        for (i2 = messageSchema3.checkInitializedCount; i2 < messageSchema3.repeatedFieldOffsetStart; i2++) {
                                                            objFilterMapUnknownEnumValues2 = messageSchema3.filterMapUnknownEnumValues(obj2, messageSchema3.intArray[i2], objFilterMapUnknownEnumValues2, unknownFieldSchema, obj);
                                                        }
                                                        if (objFilterMapUnknownEnumValues2 == null) {
                                                            return;
                                                        }
                                                        unknownFieldSchema.setBuilderToMessage(obj2, objFilterMapUnknownEnumValues2);
                                                        return;
                                                    }
                                                } else if (!reader3.skipField()) {
                                                    objFilterMapUnknownEnumValues2 = extension;
                                                    for (i3 = messageSchema3.checkInitializedCount; i3 < messageSchema3.repeatedFieldOffsetStart; i3++) {
                                                        objFilterMapUnknownEnumValues2 = messageSchema3.filterMapUnknownEnumValues(obj2, messageSchema3.intArray[i3], objFilterMapUnknownEnumValues2, unknownFieldSchema, obj);
                                                    }
                                                    if (objFilterMapUnknownEnumValues2 == null) {
                                                        return;
                                                    }
                                                    unknownFieldSchema.setBuilderToMessage(obj2, objFilterMapUnknownEnumValues2);
                                                    return;
                                                }
                                            } catch (Throwable th) {
                                                th = th;
                                            }
                                        } catch (Throwable th2) {
                                            th = th2;
                                        }
                                    } catch (InvalidProtocolBufferException.InvalidWireTypeException unused2) {
                                        extensionRegistryLite = extensionRegistryLite;
                                        messageSchema3 = messageSchema4;
                                        reader3 = reader3;
                                    }
                                    extensionRegistryLite2 = extensionRegistryLite;
                                    break;
                                case 28:
                                    reader2 = reader3;
                                    messageSchema2 = messageSchema3;
                                    reader2.readBytesList(messageSchema2.listFieldSchema.mutableListAt(obj3, offset(iTypeAndOffsetAt)));
                                    messageSchema3 = messageSchema2;
                                    extension = builderFromMessage;
                                    extensionRegistryLite2 = extensionRegistryLite;
                                    break;
                                case 29:
                                    reader2 = reader3;
                                    unknownFieldSchema = unknownFieldSchema3;
                                    obj2 = obj3;
                                    messageSchema2 = messageSchema3;
                                    try {
                                        try {
                                            reader2.readUInt32List(messageSchema2.listFieldSchema.mutableListAt(obj2, offset(iTypeAndOffsetAt)));
                                            messageSchema3 = messageSchema2;
                                            extension = builderFromMessage;
                                        } catch (Throwable th3) {
                                            th = th3;
                                            messageSchema3 = messageSchema2;
                                            extension = builderFromMessage;
                                            i = messageSchema3.checkInitializedCount;
                                            objFilterMapUnknownEnumValues = extension;
                                            while (i < messageSchema3.repeatedFieldOffsetStart) {
                                                objFilterMapUnknownEnumValues = messageSchema3.filterMapUnknownEnumValues(obj2, messageSchema3.intArray[i], objFilterMapUnknownEnumValues, unknownFieldSchema, obj);
                                                i++;
                                                messageSchema3 = this;
                                            }
                                            unknownFieldSchema2 = unknownFieldSchema;
                                            if (objFilterMapUnknownEnumValues != null) {
                                                unknownFieldSchema2.setBuilderToMessage(obj2, objFilterMapUnknownEnumValues);
                                            }
                                            throw th;
                                        }
                                    } catch (InvalidProtocolBufferException.InvalidWireTypeException unused3) {
                                        messageSchema3 = messageSchema2;
                                        extension = builderFromMessage;
                                        reader3 = reader2;
                                        if (unknownFieldSchema.shouldDiscardUnknownFields(reader3)) {
                                            if (extension == null) {
                                                extension = unknownFieldSchema.getBuilderFromMessage(obj2);
                                            }
                                            if (!unknownFieldSchema.mergeOneFieldFrom(extension, reader3, 0)) {
                                                objFilterMapUnknownEnumValues2 = extension;
                                                while (i2 < messageSchema3.repeatedFieldOffsetStart) {
                                                    objFilterMapUnknownEnumValues2 = messageSchema3.filterMapUnknownEnumValues(obj2, messageSchema3.intArray[i2], objFilterMapUnknownEnumValues2, unknownFieldSchema, obj);
                                                }
                                                if (objFilterMapUnknownEnumValues2 == null) {
                                                    return;
                                                }
                                                unknownFieldSchema.setBuilderToMessage(obj2, objFilterMapUnknownEnumValues2);
                                                return;
                                            }
                                        } else if (!reader3.skipField()) {
                                            objFilterMapUnknownEnumValues2 = extension;
                                            while (i3 < messageSchema3.repeatedFieldOffsetStart) {
                                                objFilterMapUnknownEnumValues2 = messageSchema3.filterMapUnknownEnumValues(obj2, messageSchema3.intArray[i3], objFilterMapUnknownEnumValues2, unknownFieldSchema, obj);
                                            }
                                            if (objFilterMapUnknownEnumValues2 == null) {
                                                return;
                                            }
                                            unknownFieldSchema.setBuilderToMessage(obj2, objFilterMapUnknownEnumValues2);
                                            return;
                                        }
                                    }
                                    extensionRegistryLite2 = extensionRegistryLite;
                                    break;
                                case 30:
                                    messageSchema2 = messageSchema3;
                                    obj2 = obj3;
                                    reader2 = reader3;
                                    extension = builderFromMessage;
                                    try {
                                        List listMutableListAt = messageSchema2.listFieldSchema.mutableListAt(obj2, offset(iTypeAndOffsetAt));
                                        reader2.readEnumList(listMutableListAt);
                                        messageSchema2.getEnumFieldVerifier(iPositionForFieldNumber);
                                        obj4 = obj2;
                                        try {
                                            extension = SchemaUtil.filterUnknownEnumList(obj4, fieldNumber, listMutableListAt, null, extension, unknownFieldSchema);
                                            extensionRegistryLite = extensionRegistryLite;
                                            messageSchema3 = messageSchema2;
                                        } catch (InvalidProtocolBufferException.InvalidWireTypeException unused4) {
                                            unknownFieldSchema = unknownFieldSchema;
                                            obj2 = obj4;
                                            messageSchema3 = messageSchema2;
                                            reader3 = reader2;
                                            if (unknownFieldSchema.shouldDiscardUnknownFields(reader3)) {
                                                if (extension == null) {
                                                    extension = unknownFieldSchema.getBuilderFromMessage(obj2);
                                                }
                                                if (!unknownFieldSchema.mergeOneFieldFrom(extension, reader3, 0)) {
                                                    objFilterMapUnknownEnumValues2 = extension;
                                                    while (i2 < messageSchema3.repeatedFieldOffsetStart) {
                                                        objFilterMapUnknownEnumValues2 = messageSchema3.filterMapUnknownEnumValues(obj2, messageSchema3.intArray[i2], objFilterMapUnknownEnumValues2, unknownFieldSchema, obj);
                                                    }
                                                    if (objFilterMapUnknownEnumValues2 == null) {
                                                        return;
                                                    }
                                                    unknownFieldSchema.setBuilderToMessage(obj2, objFilterMapUnknownEnumValues2);
                                                    return;
                                                }
                                            } else if (!reader3.skipField()) {
                                                objFilterMapUnknownEnumValues2 = extension;
                                                while (i3 < messageSchema3.repeatedFieldOffsetStart) {
                                                    objFilterMapUnknownEnumValues2 = messageSchema3.filterMapUnknownEnumValues(obj2, messageSchema3.intArray[i3], objFilterMapUnknownEnumValues2, unknownFieldSchema, obj);
                                                }
                                                if (objFilterMapUnknownEnumValues2 == null) {
                                                    return;
                                                }
                                                unknownFieldSchema.setBuilderToMessage(obj2, objFilterMapUnknownEnumValues2);
                                                return;
                                            }
                                        } catch (Throwable th4) {
                                            th = th4;
                                            obj2 = obj4;
                                            unknownFieldSchema = unknownFieldSchema;
                                            messageSchema3 = messageSchema2;
                                            i = messageSchema3.checkInitializedCount;
                                            objFilterMapUnknownEnumValues = extension;
                                            while (i < messageSchema3.repeatedFieldOffsetStart) {
                                                objFilterMapUnknownEnumValues = messageSchema3.filterMapUnknownEnumValues(obj2, messageSchema3.intArray[i], objFilterMapUnknownEnumValues, unknownFieldSchema, obj);
                                                i++;
                                                messageSchema3 = this;
                                            }
                                            unknownFieldSchema2 = unknownFieldSchema;
                                            if (objFilterMapUnknownEnumValues != null) {
                                                unknownFieldSchema2.setBuilderToMessage(obj2, objFilterMapUnknownEnumValues);
                                            }
                                            throw th;
                                        }
                                        break;
                                    } catch (InvalidProtocolBufferException.InvalidWireTypeException unused5) {
                                        messageSchema3 = messageSchema2;
                                        reader3 = reader2;
                                        if (unknownFieldSchema.shouldDiscardUnknownFields(reader3)) {
                                            if (extension == null) {
                                                extension = unknownFieldSchema.getBuilderFromMessage(obj2);
                                            }
                                            if (!unknownFieldSchema.mergeOneFieldFrom(extension, reader3, 0)) {
                                                objFilterMapUnknownEnumValues2 = extension;
                                                while (i2 < messageSchema3.repeatedFieldOffsetStart) {
                                                    objFilterMapUnknownEnumValues2 = messageSchema3.filterMapUnknownEnumValues(obj2, messageSchema3.intArray[i2], objFilterMapUnknownEnumValues2, unknownFieldSchema, obj);
                                                }
                                                if (objFilterMapUnknownEnumValues2 == null) {
                                                    return;
                                                }
                                                unknownFieldSchema.setBuilderToMessage(obj2, objFilterMapUnknownEnumValues2);
                                                return;
                                            }
                                        } else if (!reader3.skipField()) {
                                            objFilterMapUnknownEnumValues2 = extension;
                                            while (i3 < messageSchema3.repeatedFieldOffsetStart) {
                                                objFilterMapUnknownEnumValues2 = messageSchema3.filterMapUnknownEnumValues(obj2, messageSchema3.intArray[i3], objFilterMapUnknownEnumValues2, unknownFieldSchema, obj);
                                            }
                                            if (objFilterMapUnknownEnumValues2 == null) {
                                                return;
                                            }
                                            unknownFieldSchema.setBuilderToMessage(obj2, objFilterMapUnknownEnumValues2);
                                            return;
                                        }
                                    } catch (Throwable th5) {
                                        th = th5;
                                        unknownFieldSchema = unknownFieldSchema;
                                    }
                                    extensionRegistryLite2 = extensionRegistryLite;
                                    break;
                                case 31:
                                    reader2 = reader3;
                                    extension = builderFromMessage;
                                    messageSchema2 = messageSchema3;
                                    reader2.readSFixed32List(messageSchema2.listFieldSchema.mutableListAt(obj3, offset(iTypeAndOffsetAt)));
                                    builderFromMessage = extension;
                                    messageSchema3 = messageSchema2;
                                    extension = builderFromMessage;
                                    extensionRegistryLite2 = extensionRegistryLite;
                                    break;
                                case 32:
                                    reader2 = reader3;
                                    extension = builderFromMessage;
                                    messageSchema2 = messageSchema3;
                                    reader2.readSFixed64List(messageSchema2.listFieldSchema.mutableListAt(obj3, offset(iTypeAndOffsetAt)));
                                    builderFromMessage = extension;
                                    messageSchema3 = messageSchema2;
                                    extension = builderFromMessage;
                                    extensionRegistryLite2 = extensionRegistryLite;
                                    break;
                                case 33:
                                    reader2 = reader3;
                                    extension = builderFromMessage;
                                    messageSchema2 = messageSchema3;
                                    reader2.readSInt32List(messageSchema2.listFieldSchema.mutableListAt(obj3, offset(iTypeAndOffsetAt)));
                                    builderFromMessage = extension;
                                    messageSchema3 = messageSchema2;
                                    extension = builderFromMessage;
                                    extensionRegistryLite2 = extensionRegistryLite;
                                    break;
                                case 34:
                                    reader2 = reader3;
                                    extension = builderFromMessage;
                                    messageSchema2 = messageSchema3;
                                    reader2.readSInt64List(messageSchema2.listFieldSchema.mutableListAt(obj3, offset(iTypeAndOffsetAt)));
                                    builderFromMessage = extension;
                                    messageSchema3 = messageSchema2;
                                    extension = builderFromMessage;
                                    extensionRegistryLite2 = extensionRegistryLite;
                                    break;
                                case 35:
                                    reader2 = reader3;
                                    extension = builderFromMessage;
                                    messageSchema2 = messageSchema3;
                                    reader2.readDoubleList(messageSchema2.listFieldSchema.mutableListAt(obj3, offset(iTypeAndOffsetAt)));
                                    builderFromMessage = extension;
                                    messageSchema3 = messageSchema2;
                                    extension = builderFromMessage;
                                    extensionRegistryLite2 = extensionRegistryLite;
                                    break;
                                case 36:
                                    reader2 = reader3;
                                    extension = builderFromMessage;
                                    messageSchema2 = messageSchema3;
                                    reader2.readFloatList(messageSchema2.listFieldSchema.mutableListAt(obj3, offset(iTypeAndOffsetAt)));
                                    builderFromMessage = extension;
                                    messageSchema3 = messageSchema2;
                                    extension = builderFromMessage;
                                    extensionRegistryLite2 = extensionRegistryLite;
                                    break;
                                case 37:
                                    reader2 = reader3;
                                    extension = builderFromMessage;
                                    messageSchema2 = messageSchema3;
                                    reader2.readInt64List(messageSchema2.listFieldSchema.mutableListAt(obj3, offset(iTypeAndOffsetAt)));
                                    builderFromMessage = extension;
                                    messageSchema3 = messageSchema2;
                                    extension = builderFromMessage;
                                    extensionRegistryLite2 = extensionRegistryLite;
                                    break;
                                case 38:
                                    reader2 = reader3;
                                    extension = builderFromMessage;
                                    messageSchema2 = messageSchema3;
                                    reader2.readUInt64List(messageSchema2.listFieldSchema.mutableListAt(obj3, offset(iTypeAndOffsetAt)));
                                    builderFromMessage = extension;
                                    messageSchema3 = messageSchema2;
                                    extension = builderFromMessage;
                                    extensionRegistryLite2 = extensionRegistryLite;
                                    break;
                                case 39:
                                    reader2 = reader3;
                                    extension = builderFromMessage;
                                    messageSchema2 = messageSchema3;
                                    reader2.readInt32List(messageSchema2.listFieldSchema.mutableListAt(obj3, offset(iTypeAndOffsetAt)));
                                    builderFromMessage = extension;
                                    messageSchema3 = messageSchema2;
                                    extension = builderFromMessage;
                                    extensionRegistryLite2 = extensionRegistryLite;
                                    break;
                                case 40:
                                    reader2 = reader3;
                                    extension = builderFromMessage;
                                    messageSchema2 = messageSchema3;
                                    reader2.readFixed64List(messageSchema2.listFieldSchema.mutableListAt(obj3, offset(iTypeAndOffsetAt)));
                                    builderFromMessage = extension;
                                    messageSchema3 = messageSchema2;
                                    extension = builderFromMessage;
                                    extensionRegistryLite2 = extensionRegistryLite;
                                    break;
                                case 41:
                                    reader2 = reader3;
                                    extension = builderFromMessage;
                                    messageSchema2 = messageSchema3;
                                    reader2.readFixed32List(messageSchema2.listFieldSchema.mutableListAt(obj3, offset(iTypeAndOffsetAt)));
                                    builderFromMessage = extension;
                                    messageSchema3 = messageSchema2;
                                    extension = builderFromMessage;
                                    extensionRegistryLite2 = extensionRegistryLite;
                                    break;
                                case 42:
                                    reader2 = reader3;
                                    extension = builderFromMessage;
                                    messageSchema2 = messageSchema3;
                                    reader2.readBoolList(messageSchema2.listFieldSchema.mutableListAt(obj3, offset(iTypeAndOffsetAt)));
                                    builderFromMessage = extension;
                                    messageSchema3 = messageSchema2;
                                    extension = builderFromMessage;
                                    extensionRegistryLite2 = extensionRegistryLite;
                                    break;
                                case 43:
                                    reader2 = reader3;
                                    obj2 = obj3;
                                    extension = builderFromMessage;
                                    messageSchema2 = messageSchema3;
                                    try {
                                        reader2.readUInt32List(messageSchema2.listFieldSchema.mutableListAt(obj2, offset(iTypeAndOffsetAt)));
                                        builderFromMessage = extension;
                                        messageSchema3 = messageSchema2;
                                        extension = builderFromMessage;
                                    } catch (InvalidProtocolBufferException.InvalidWireTypeException unused6) {
                                        messageSchema3 = messageSchema2;
                                        reader3 = reader2;
                                        if (unknownFieldSchema.shouldDiscardUnknownFields(reader3)) {
                                            if (extension == null) {
                                                extension = unknownFieldSchema.getBuilderFromMessage(obj2);
                                            }
                                            if (!unknownFieldSchema.mergeOneFieldFrom(extension, reader3, 0)) {
                                                objFilterMapUnknownEnumValues2 = extension;
                                                while (i2 < messageSchema3.repeatedFieldOffsetStart) {
                                                    objFilterMapUnknownEnumValues2 = messageSchema3.filterMapUnknownEnumValues(obj2, messageSchema3.intArray[i2], objFilterMapUnknownEnumValues2, unknownFieldSchema, obj);
                                                }
                                                if (objFilterMapUnknownEnumValues2 == null) {
                                                    return;
                                                }
                                                unknownFieldSchema.setBuilderToMessage(obj2, objFilterMapUnknownEnumValues2);
                                                return;
                                            }
                                        } else if (!reader3.skipField()) {
                                            objFilterMapUnknownEnumValues2 = extension;
                                            while (i3 < messageSchema3.repeatedFieldOffsetStart) {
                                                objFilterMapUnknownEnumValues2 = messageSchema3.filterMapUnknownEnumValues(obj2, messageSchema3.intArray[i3], objFilterMapUnknownEnumValues2, unknownFieldSchema, obj);
                                            }
                                            if (objFilterMapUnknownEnumValues2 == null) {
                                                return;
                                            }
                                            unknownFieldSchema.setBuilderToMessage(obj2, objFilterMapUnknownEnumValues2);
                                            return;
                                        }
                                    } catch (Throwable th6) {
                                        th = th6;
                                        unknownFieldSchema = unknownFieldSchema;
                                        messageSchema3 = messageSchema2;
                                        i = messageSchema3.checkInitializedCount;
                                        objFilterMapUnknownEnumValues = extension;
                                        while (i < messageSchema3.repeatedFieldOffsetStart) {
                                            objFilterMapUnknownEnumValues = messageSchema3.filterMapUnknownEnumValues(obj2, messageSchema3.intArray[i], objFilterMapUnknownEnumValues, unknownFieldSchema, obj);
                                            i++;
                                            messageSchema3 = this;
                                        }
                                        unknownFieldSchema2 = unknownFieldSchema;
                                        if (objFilterMapUnknownEnumValues != null) {
                                            unknownFieldSchema2.setBuilderToMessage(obj2, objFilterMapUnknownEnumValues);
                                        }
                                        throw th;
                                    }
                                    extensionRegistryLite2 = extensionRegistryLite;
                                    break;
                                case 44:
                                    messageSchema2 = messageSchema3;
                                    obj4 = obj3;
                                    reader2 = reader3;
                                    try {
                                        List listMutableListAt2 = messageSchema2.listFieldSchema.mutableListAt(obj4, offset(iTypeAndOffsetAt));
                                        reader2.readEnumList(listMutableListAt2);
                                        messageSchema2.getEnumFieldVerifier(iPositionForFieldNumber);
                                        extension = builderFromMessage;
                                        try {
                                            extension = SchemaUtil.filterUnknownEnumList(obj4, fieldNumber, listMutableListAt2, null, extension, unknownFieldSchema3);
                                            extensionRegistryLite = extensionRegistryLite;
                                            messageSchema3 = messageSchema2;
                                        } catch (InvalidProtocolBufferException.InvalidWireTypeException unused7) {
                                            unknownFieldSchema = unknownFieldSchema;
                                            obj2 = obj4;
                                            messageSchema3 = messageSchema2;
                                            reader3 = reader2;
                                            if (unknownFieldSchema.shouldDiscardUnknownFields(reader3)) {
                                                if (extension == null) {
                                                    extension = unknownFieldSchema.getBuilderFromMessage(obj2);
                                                }
                                                if (!unknownFieldSchema.mergeOneFieldFrom(extension, reader3, 0)) {
                                                    objFilterMapUnknownEnumValues2 = extension;
                                                    while (i2 < messageSchema3.repeatedFieldOffsetStart) {
                                                        objFilterMapUnknownEnumValues2 = messageSchema3.filterMapUnknownEnumValues(obj2, messageSchema3.intArray[i2], objFilterMapUnknownEnumValues2, unknownFieldSchema, obj);
                                                    }
                                                    if (objFilterMapUnknownEnumValues2 == null) {
                                                        return;
                                                    }
                                                    unknownFieldSchema.setBuilderToMessage(obj2, objFilterMapUnknownEnumValues2);
                                                    return;
                                                }
                                            } else if (!reader3.skipField()) {
                                                objFilterMapUnknownEnumValues2 = extension;
                                                while (i3 < messageSchema3.repeatedFieldOffsetStart) {
                                                    objFilterMapUnknownEnumValues2 = messageSchema3.filterMapUnknownEnumValues(obj2, messageSchema3.intArray[i3], objFilterMapUnknownEnumValues2, unknownFieldSchema, obj);
                                                }
                                                if (objFilterMapUnknownEnumValues2 == null) {
                                                    return;
                                                }
                                                unknownFieldSchema.setBuilderToMessage(obj2, objFilterMapUnknownEnumValues2);
                                                return;
                                            }
                                        } catch (Throwable th7) {
                                            th = th7;
                                            obj2 = obj4;
                                            unknownFieldSchema = unknownFieldSchema;
                                            messageSchema3 = messageSchema2;
                                            i = messageSchema3.checkInitializedCount;
                                            objFilterMapUnknownEnumValues = extension;
                                            while (i < messageSchema3.repeatedFieldOffsetStart) {
                                                objFilterMapUnknownEnumValues = messageSchema3.filterMapUnknownEnumValues(obj2, messageSchema3.intArray[i], objFilterMapUnknownEnumValues, unknownFieldSchema, obj);
                                                i++;
                                                messageSchema3 = this;
                                            }
                                            unknownFieldSchema2 = unknownFieldSchema;
                                            if (objFilterMapUnknownEnumValues != null) {
                                                unknownFieldSchema2.setBuilderToMessage(obj2, objFilterMapUnknownEnumValues);
                                            }
                                            throw th;
                                        }
                                    } catch (InvalidProtocolBufferException.InvalidWireTypeException unused8) {
                                        extension = builderFromMessage;
                                    } catch (Throwable th8) {
                                        th = th8;
                                        obj2 = obj4;
                                        extension = builderFromMessage;
                                    }
                                    extensionRegistryLite2 = extensionRegistryLite;
                                    break;
                                case 45:
                                    messageSchema2 = messageSchema3;
                                    obj5 = obj3;
                                    reader2 = reader3;
                                    reader2.readSFixed32List(messageSchema2.listFieldSchema.mutableListAt(obj5, offset(iTypeAndOffsetAt)));
                                    messageSchema3 = messageSchema2;
                                    extension = builderFromMessage;
                                    extensionRegistryLite2 = extensionRegistryLite;
                                    break;
                                case 46:
                                    messageSchema2 = messageSchema3;
                                    obj5 = obj3;
                                    reader2 = reader3;
                                    reader2.readSFixed64List(messageSchema2.listFieldSchema.mutableListAt(obj5, offset(iTypeAndOffsetAt)));
                                    messageSchema3 = messageSchema2;
                                    extension = builderFromMessage;
                                    extensionRegistryLite2 = extensionRegistryLite;
                                    break;
                                case 47:
                                    messageSchema2 = messageSchema3;
                                    obj5 = obj3;
                                    reader2 = reader3;
                                    reader2.readSInt32List(messageSchema2.listFieldSchema.mutableListAt(obj5, offset(iTypeAndOffsetAt)));
                                    messageSchema3 = messageSchema2;
                                    extension = builderFromMessage;
                                    extensionRegistryLite2 = extensionRegistryLite;
                                    break;
                                case 48:
                                    messageSchema2 = messageSchema3;
                                    obj5 = obj3;
                                    reader2 = reader3;
                                    unknownFieldSchema = unknownFieldSchema3;
                                    try {
                                        reader2.readSInt64List(messageSchema2.listFieldSchema.mutableListAt(obj5, offset(iTypeAndOffsetAt)));
                                        messageSchema3 = messageSchema2;
                                        extension = builderFromMessage;
                                    } catch (InvalidProtocolBufferException.InvalidWireTypeException unused9) {
                                        obj2 = obj5;
                                        messageSchema3 = messageSchema2;
                                        extension = builderFromMessage;
                                        reader3 = reader2;
                                        if (unknownFieldSchema.shouldDiscardUnknownFields(reader3)) {
                                            if (extension == null) {
                                                extension = unknownFieldSchema.getBuilderFromMessage(obj2);
                                            }
                                            if (!unknownFieldSchema.mergeOneFieldFrom(extension, reader3, 0)) {
                                                objFilterMapUnknownEnumValues2 = extension;
                                                while (i2 < messageSchema3.repeatedFieldOffsetStart) {
                                                    objFilterMapUnknownEnumValues2 = messageSchema3.filterMapUnknownEnumValues(obj2, messageSchema3.intArray[i2], objFilterMapUnknownEnumValues2, unknownFieldSchema, obj);
                                                }
                                                if (objFilterMapUnknownEnumValues2 == null) {
                                                    return;
                                                }
                                                unknownFieldSchema.setBuilderToMessage(obj2, objFilterMapUnknownEnumValues2);
                                                return;
                                            }
                                        } else if (!reader3.skipField()) {
                                            objFilterMapUnknownEnumValues2 = extension;
                                            while (i3 < messageSchema3.repeatedFieldOffsetStart) {
                                                objFilterMapUnknownEnumValues2 = messageSchema3.filterMapUnknownEnumValues(obj2, messageSchema3.intArray[i3], objFilterMapUnknownEnumValues2, unknownFieldSchema, obj);
                                            }
                                            if (objFilterMapUnknownEnumValues2 == null) {
                                                return;
                                            }
                                            unknownFieldSchema.setBuilderToMessage(obj2, objFilterMapUnknownEnumValues2);
                                            return;
                                        }
                                    } catch (Throwable th9) {
                                        th = th9;
                                        obj2 = obj5;
                                        messageSchema3 = messageSchema2;
                                        extension = builderFromMessage;
                                        i = messageSchema3.checkInitializedCount;
                                        objFilterMapUnknownEnumValues = extension;
                                        while (i < messageSchema3.repeatedFieldOffsetStart) {
                                            objFilterMapUnknownEnumValues = messageSchema3.filterMapUnknownEnumValues(obj2, messageSchema3.intArray[i], objFilterMapUnknownEnumValues, unknownFieldSchema, obj);
                                            i++;
                                            messageSchema3 = this;
                                        }
                                        unknownFieldSchema2 = unknownFieldSchema;
                                        if (objFilterMapUnknownEnumValues != null) {
                                            unknownFieldSchema2.setBuilderToMessage(obj2, objFilterMapUnknownEnumValues);
                                        }
                                        throw th;
                                    }
                                    extensionRegistryLite2 = extensionRegistryLite;
                                    break;
                                case 49:
                                    unknownFieldSchema = unknownFieldSchema3;
                                    try {
                                        obj2 = obj;
                                        try {
                                            messageSchema3.readGroupList(obj2, offset(iTypeAndOffsetAt), reader, messageSchema3.getMessageFieldSchema(iPositionForFieldNumber), extensionRegistryLite);
                                            messageSchema2 = messageSchema3;
                                            obj5 = obj2;
                                            reader2 = reader;
                                            messageSchema3 = messageSchema2;
                                            extension = builderFromMessage;
                                        } catch (InvalidProtocolBufferException.InvalidWireTypeException unused10) {
                                            extensionRegistryLite = extensionRegistryLite;
                                            reader3 = reader;
                                            extension = builderFromMessage;
                                            if (unknownFieldSchema.shouldDiscardUnknownFields(reader3)) {
                                                if (extension == null) {
                                                    extension = unknownFieldSchema.getBuilderFromMessage(obj2);
                                                }
                                                if (!unknownFieldSchema.mergeOneFieldFrom(extension, reader3, 0)) {
                                                    objFilterMapUnknownEnumValues2 = extension;
                                                    while (i2 < messageSchema3.repeatedFieldOffsetStart) {
                                                        objFilterMapUnknownEnumValues2 = messageSchema3.filterMapUnknownEnumValues(obj2, messageSchema3.intArray[i2], objFilterMapUnknownEnumValues2, unknownFieldSchema, obj);
                                                    }
                                                    if (objFilterMapUnknownEnumValues2 == null) {
                                                        return;
                                                    }
                                                    unknownFieldSchema.setBuilderToMessage(obj2, objFilterMapUnknownEnumValues2);
                                                    return;
                                                }
                                            } else if (!reader3.skipField()) {
                                                objFilterMapUnknownEnumValues2 = extension;
                                                while (i3 < messageSchema3.repeatedFieldOffsetStart) {
                                                    objFilterMapUnknownEnumValues2 = messageSchema3.filterMapUnknownEnumValues(obj2, messageSchema3.intArray[i3], objFilterMapUnknownEnumValues2, unknownFieldSchema, obj);
                                                }
                                                if (objFilterMapUnknownEnumValues2 == null) {
                                                    return;
                                                }
                                                unknownFieldSchema.setBuilderToMessage(obj2, objFilterMapUnknownEnumValues2);
                                                return;
                                            }
                                        } catch (Throwable th10) {
                                            th = th10;
                                            messageSchema2 = messageSchema3;
                                            messageSchema3 = messageSchema2;
                                            extension = builderFromMessage;
                                            i = messageSchema3.checkInitializedCount;
                                            objFilterMapUnknownEnumValues = extension;
                                            while (i < messageSchema3.repeatedFieldOffsetStart) {
                                                objFilterMapUnknownEnumValues = messageSchema3.filterMapUnknownEnumValues(obj2, messageSchema3.intArray[i], objFilterMapUnknownEnumValues, unknownFieldSchema, obj);
                                                i++;
                                                messageSchema3 = this;
                                            }
                                            unknownFieldSchema2 = unknownFieldSchema;
                                            if (objFilterMapUnknownEnumValues != null) {
                                                unknownFieldSchema2.setBuilderToMessage(obj2, objFilterMapUnknownEnumValues);
                                            }
                                            throw th;
                                        }
                                        extensionRegistryLite2 = extensionRegistryLite;
                                    } catch (Throwable th11) {
                                        th = th11;
                                        messageSchema2 = messageSchema3;
                                        obj5 = obj;
                                        obj2 = obj5;
                                        messageSchema3 = messageSchema2;
                                        extension = builderFromMessage;
                                        i = messageSchema3.checkInitializedCount;
                                        objFilterMapUnknownEnumValues = extension;
                                        while (i < messageSchema3.repeatedFieldOffsetStart) {
                                            objFilterMapUnknownEnumValues = messageSchema3.filterMapUnknownEnumValues(obj2, messageSchema3.intArray[i], objFilterMapUnknownEnumValues, unknownFieldSchema, obj);
                                            i++;
                                            messageSchema3 = this;
                                        }
                                        unknownFieldSchema2 = unknownFieldSchema;
                                        if (objFilterMapUnknownEnumValues != null) {
                                            unknownFieldSchema2.setBuilderToMessage(obj2, objFilterMapUnknownEnumValues);
                                        }
                                        throw th;
                                    }
                                    break;
                                case 50:
                                    unknownFieldSchema = unknownFieldSchema3;
                                    try {
                                        try {
                                            messageSchema3.mergeMap(obj3, iPositionForFieldNumber, messageSchema3.getMapFieldDefaultEntry(iPositionForFieldNumber), extensionRegistryLite, reader);
                                            extensionRegistryLite = extensionRegistryLite;
                                            extension = builderFromMessage;
                                        } catch (InvalidProtocolBufferException.InvalidWireTypeException unused11) {
                                            obj2 = obj;
                                            reader3 = reader;
                                            extensionRegistryLite = extensionRegistryLite;
                                            extension = builderFromMessage;
                                            if (unknownFieldSchema.shouldDiscardUnknownFields(reader3)) {
                                                if (extension == null) {
                                                    extension = unknownFieldSchema.getBuilderFromMessage(obj2);
                                                }
                                                if (!unknownFieldSchema.mergeOneFieldFrom(extension, reader3, 0)) {
                                                    objFilterMapUnknownEnumValues2 = extension;
                                                    while (i2 < messageSchema3.repeatedFieldOffsetStart) {
                                                        objFilterMapUnknownEnumValues2 = messageSchema3.filterMapUnknownEnumValues(obj2, messageSchema3.intArray[i2], objFilterMapUnknownEnumValues2, unknownFieldSchema, obj);
                                                    }
                                                    if (objFilterMapUnknownEnumValues2 == null) {
                                                        return;
                                                    }
                                                    unknownFieldSchema.setBuilderToMessage(obj2, objFilterMapUnknownEnumValues2);
                                                    return;
                                                }
                                            } else if (!reader3.skipField()) {
                                                objFilterMapUnknownEnumValues2 = extension;
                                                while (i3 < messageSchema3.repeatedFieldOffsetStart) {
                                                    objFilterMapUnknownEnumValues2 = messageSchema3.filterMapUnknownEnumValues(obj2, messageSchema3.intArray[i3], objFilterMapUnknownEnumValues2, unknownFieldSchema, obj);
                                                }
                                                if (objFilterMapUnknownEnumValues2 == null) {
                                                    return;
                                                }
                                                unknownFieldSchema.setBuilderToMessage(obj2, objFilterMapUnknownEnumValues2);
                                                return;
                                            }
                                        }
                                        extensionRegistryLite2 = extensionRegistryLite;
                                    } catch (Throwable th12) {
                                        th = th12;
                                        obj2 = obj;
                                    }
                                    break;
                                case 51:
                                    UnsafeUtil.putObject(obj3, offset(iTypeAndOffsetAt), Double.valueOf(reader3.readDouble()));
                                    messageSchema3.setOneofPresent(obj3, fieldNumber, iPositionForFieldNumber);
                                    extension = builderFromMessage;
                                    extensionRegistryLite2 = extensionRegistryLite;
                                    break;
                                case 52:
                                    UnsafeUtil.putObject(obj3, offset(iTypeAndOffsetAt), Float.valueOf(reader3.readFloat()));
                                    messageSchema3.setOneofPresent(obj3, fieldNumber, iPositionForFieldNumber);
                                    extension = builderFromMessage;
                                    extensionRegistryLite2 = extensionRegistryLite;
                                    break;
                                case 53:
                                    UnsafeUtil.putObject(obj3, offset(iTypeAndOffsetAt), Long.valueOf(reader3.readInt64()));
                                    messageSchema3.setOneofPresent(obj3, fieldNumber, iPositionForFieldNumber);
                                    extension = builderFromMessage;
                                    extensionRegistryLite2 = extensionRegistryLite;
                                    break;
                                case 54:
                                    UnsafeUtil.putObject(obj3, offset(iTypeAndOffsetAt), Long.valueOf(reader3.readUInt64()));
                                    messageSchema3.setOneofPresent(obj3, fieldNumber, iPositionForFieldNumber);
                                    extension = builderFromMessage;
                                    extensionRegistryLite2 = extensionRegistryLite;
                                    break;
                                case 55:
                                    UnsafeUtil.putObject(obj3, offset(iTypeAndOffsetAt), Integer.valueOf(reader3.readInt32()));
                                    messageSchema3.setOneofPresent(obj3, fieldNumber, iPositionForFieldNumber);
                                    extension = builderFromMessage;
                                    extensionRegistryLite2 = extensionRegistryLite;
                                    break;
                                case 56:
                                    UnsafeUtil.putObject(obj3, offset(iTypeAndOffsetAt), Long.valueOf(reader3.readFixed64()));
                                    messageSchema3.setOneofPresent(obj3, fieldNumber, iPositionForFieldNumber);
                                    extension = builderFromMessage;
                                    extensionRegistryLite2 = extensionRegistryLite;
                                    break;
                                case 57:
                                    UnsafeUtil.putObject(obj3, offset(iTypeAndOffsetAt), Integer.valueOf(reader3.readFixed32()));
                                    messageSchema3.setOneofPresent(obj3, fieldNumber, iPositionForFieldNumber);
                                    extension = builderFromMessage;
                                    extensionRegistryLite2 = extensionRegistryLite;
                                    break;
                                case 58:
                                    UnsafeUtil.putObject(obj3, offset(iTypeAndOffsetAt), Boolean.valueOf(reader3.readBool()));
                                    messageSchema3.setOneofPresent(obj3, fieldNumber, iPositionForFieldNumber);
                                    extension = builderFromMessage;
                                    extensionRegistryLite2 = extensionRegistryLite;
                                    break;
                                case 59:
                                    messageSchema3.readString(obj3, iTypeAndOffsetAt, reader3);
                                    messageSchema3.setOneofPresent(obj3, fieldNumber, iPositionForFieldNumber);
                                    extension = builderFromMessage;
                                    extensionRegistryLite2 = extensionRegistryLite;
                                    break;
                                case 60:
                                    MessageLite messageLite3 = (MessageLite) messageSchema3.mutableOneofMessageFieldForMerge(obj3, fieldNumber, iPositionForFieldNumber);
                                    reader3.mergeMessageField(messageLite3, messageSchema3.getMessageFieldSchema(iPositionForFieldNumber), extensionRegistryLite);
                                    messageSchema3.storeOneofMessageField(obj3, fieldNumber, iPositionForFieldNumber, messageLite3);
                                    extension = builderFromMessage;
                                    extensionRegistryLite2 = extensionRegistryLite;
                                    break;
                                case 61:
                                    UnsafeUtil.putObject(obj3, offset(iTypeAndOffsetAt), reader3.readBytes());
                                    messageSchema3.setOneofPresent(obj3, fieldNumber, iPositionForFieldNumber);
                                    extension = builderFromMessage;
                                    extensionRegistryLite2 = extensionRegistryLite;
                                    break;
                                case 62:
                                    UnsafeUtil.putObject(obj3, offset(iTypeAndOffsetAt), Integer.valueOf(reader3.readUInt32()));
                                    messageSchema3.setOneofPresent(obj3, fieldNumber, iPositionForFieldNumber);
                                    extension = builderFromMessage;
                                    extensionRegistryLite2 = extensionRegistryLite;
                                    break;
                                case 63:
                                    int i5 = reader3.readEnum();
                                    messageSchema3.getEnumFieldVerifier(iPositionForFieldNumber);
                                    UnsafeUtil.putObject(obj3, offset(iTypeAndOffsetAt), Integer.valueOf(i5));
                                    messageSchema3.setOneofPresent(obj3, fieldNumber, iPositionForFieldNumber);
                                    extension = builderFromMessage;
                                    extensionRegistryLite2 = extensionRegistryLite;
                                    break;
                                case 64:
                                    UnsafeUtil.putObject(obj3, offset(iTypeAndOffsetAt), Integer.valueOf(reader3.readSFixed32()));
                                    messageSchema3.setOneofPresent(obj3, fieldNumber, iPositionForFieldNumber);
                                    extension = builderFromMessage;
                                    extensionRegistryLite2 = extensionRegistryLite;
                                    break;
                                case 65:
                                    UnsafeUtil.putObject(obj3, offset(iTypeAndOffsetAt), Long.valueOf(reader3.readSFixed64()));
                                    messageSchema3.setOneofPresent(obj3, fieldNumber, iPositionForFieldNumber);
                                    extension = builderFromMessage;
                                    extensionRegistryLite2 = extensionRegistryLite;
                                    break;
                                case 66:
                                    UnsafeUtil.putObject(obj3, offset(iTypeAndOffsetAt), Integer.valueOf(reader3.readSInt32()));
                                    messageSchema3.setOneofPresent(obj3, fieldNumber, iPositionForFieldNumber);
                                    extension = builderFromMessage;
                                    extensionRegistryLite2 = extensionRegistryLite;
                                    break;
                                case 67:
                                    UnsafeUtil.putObject(obj3, offset(iTypeAndOffsetAt), Long.valueOf(reader3.readSInt64()));
                                    messageSchema3.setOneofPresent(obj3, fieldNumber, iPositionForFieldNumber);
                                    extension = builderFromMessage;
                                    extensionRegistryLite2 = extensionRegistryLite;
                                    break;
                                case 68:
                                    unknownFieldSchema = unknownFieldSchema3;
                                    try {
                                        MessageLite messageLite4 = (MessageLite) messageSchema3.mutableOneofMessageFieldForMerge(obj3, fieldNumber, iPositionForFieldNumber);
                                        reader3.mergeGroupField(messageLite4, messageSchema3.getMessageFieldSchema(iPositionForFieldNumber), extensionRegistryLite);
                                        messageSchema3.storeOneofMessageField(obj3, fieldNumber, iPositionForFieldNumber, messageLite4);
                                        extension = builderFromMessage;
                                    } catch (InvalidProtocolBufferException.InvalidWireTypeException unused12) {
                                        obj2 = obj3;
                                        extension = builderFromMessage;
                                        if (unknownFieldSchema.shouldDiscardUnknownFields(reader3)) {
                                            if (extension == null) {
                                                extension = unknownFieldSchema.getBuilderFromMessage(obj2);
                                            }
                                            if (!unknownFieldSchema.mergeOneFieldFrom(extension, reader3, 0)) {
                                                objFilterMapUnknownEnumValues2 = extension;
                                                while (i2 < messageSchema3.repeatedFieldOffsetStart) {
                                                    objFilterMapUnknownEnumValues2 = messageSchema3.filterMapUnknownEnumValues(obj2, messageSchema3.intArray[i2], objFilterMapUnknownEnumValues2, unknownFieldSchema, obj);
                                                }
                                                if (objFilterMapUnknownEnumValues2 == null) {
                                                    return;
                                                }
                                                unknownFieldSchema.setBuilderToMessage(obj2, objFilterMapUnknownEnumValues2);
                                                return;
                                            }
                                        } else if (!reader3.skipField()) {
                                            objFilterMapUnknownEnumValues2 = extension;
                                            while (i3 < messageSchema3.repeatedFieldOffsetStart) {
                                                objFilterMapUnknownEnumValues2 = messageSchema3.filterMapUnknownEnumValues(obj2, messageSchema3.intArray[i3], objFilterMapUnknownEnumValues2, unknownFieldSchema, obj);
                                            }
                                            if (objFilterMapUnknownEnumValues2 == null) {
                                                return;
                                            }
                                            unknownFieldSchema.setBuilderToMessage(obj2, objFilterMapUnknownEnumValues2);
                                            return;
                                        }
                                    } catch (Throwable th13) {
                                        th = th13;
                                        obj2 = obj3;
                                        extension = builderFromMessage;
                                        i = messageSchema3.checkInitializedCount;
                                        objFilterMapUnknownEnumValues = extension;
                                        while (i < messageSchema3.repeatedFieldOffsetStart) {
                                            objFilterMapUnknownEnumValues = messageSchema3.filterMapUnknownEnumValues(obj2, messageSchema3.intArray[i], objFilterMapUnknownEnumValues, unknownFieldSchema, obj);
                                            i++;
                                            messageSchema3 = this;
                                        }
                                        unknownFieldSchema2 = unknownFieldSchema;
                                        if (objFilterMapUnknownEnumValues != null) {
                                            unknownFieldSchema2.setBuilderToMessage(obj2, objFilterMapUnknownEnumValues);
                                        }
                                        throw th;
                                    }
                                    extensionRegistryLite2 = extensionRegistryLite;
                                    break;
                                default:
                                    if (builderFromMessage == null) {
                                        builderFromMessage = unknownFieldSchema3.getBuilderFromMessage(obj3);
                                    }
                                    if (!unknownFieldSchema3.mergeOneFieldFrom(builderFromMessage, reader3, 0)) {
                                        Object objFilterMapUnknownEnumValues3 = builderFromMessage;
                                        for (int i6 = messageSchema3.checkInitializedCount; i6 < messageSchema3.repeatedFieldOffsetStart; i6++) {
                                            objFilterMapUnknownEnumValues3 = messageSchema3.filterMapUnknownEnumValues(obj3, messageSchema3.intArray[i6], objFilterMapUnknownEnumValues3, unknownFieldSchema3, obj);
                                        }
                                        if (objFilterMapUnknownEnumValues3 != null) {
                                            unknownFieldSchema3.setBuilderToMessage(obj3, objFilterMapUnknownEnumValues3);
                                            return;
                                        }
                                        return;
                                    }
                                    extension = builderFromMessage;
                                    extensionRegistryLite2 = extensionRegistryLite;
                                    break;
                                    break;
                            }
                        } catch (InvalidProtocolBufferException.InvalidWireTypeException unused13) {
                            unknownFieldSchema = unknownFieldSchema3;
                        }
                    } catch (Throwable th14) {
                        th = th14;
                        unknownFieldSchema = unknownFieldSchema3;
                        obj2 = obj3;
                        extension = builderFromMessage;
                        i = messageSchema3.checkInitializedCount;
                        objFilterMapUnknownEnumValues = extension;
                        while (i < messageSchema3.repeatedFieldOffsetStart) {
                            objFilterMapUnknownEnumValues = messageSchema3.filterMapUnknownEnumValues(obj2, messageSchema3.intArray[i], objFilterMapUnknownEnumValues, unknownFieldSchema, obj);
                            i++;
                            messageSchema3 = this;
                        }
                        unknownFieldSchema2 = unknownFieldSchema;
                        if (objFilterMapUnknownEnumValues != null) {
                            unknownFieldSchema2.setBuilderToMessage(obj2, objFilterMapUnknownEnumValues);
                        }
                        throw th;
                    }
                } else {
                    if (fieldNumber == Integer.MAX_VALUE) {
                        int i7 = messageSchema3.checkInitializedCount;
                        Object objFilterMapUnknownEnumValues4 = extension;
                        while (i7 < messageSchema3.repeatedFieldOffsetStart) {
                            objFilterMapUnknownEnumValues4 = messageSchema3.filterMapUnknownEnumValues(obj, messageSchema3.intArray[i7], objFilterMapUnknownEnumValues4, unknownFieldSchema, obj);
                            i7++;
                            messageSchema3 = messageSchema3;
                        }
                        if (objFilterMapUnknownEnumValues4 != null) {
                            unknownFieldSchema.setBuilderToMessage(obj, objFilterMapUnknownEnumValues4);
                        }
                        return;
                    }
                    messageSchema = messageSchema3;
                    try {
                        if (messageSchema.hasExtensions) {
                            extensionSchema2 = extensionSchema;
                            objFindExtensionByNumber = extensionSchema2.findExtensionByNumber(extensionRegistryLite2, messageSchema.defaultInstance, fieldNumber);
                        } else {
                            extensionSchema2 = extensionSchema;
                            objFindExtensionByNumber = null;
                        }
                        if (objFindExtensionByNumber != null) {
                            if (mutableExtensions == null) {
                                try {
                                    mutableExtensions = extensionSchema.getMutableExtensions(obj);
                                } catch (Throwable th15) {
                                    th = th15;
                                    obj2 = obj;
                                    unknownFieldSchema = unknownFieldSchema;
                                    messageSchema3 = messageSchema;
                                }
                            }
                            Object obj6 = extension;
                            FieldSet fieldSet = mutableExtensions;
                            try {
                                mutableExtensions = fieldSet;
                                extension = extensionSchema2.parseExtension(obj, reader, objFindExtensionByNumber, extensionRegistryLite2, fieldSet, obj6, unknownFieldSchema);
                                extensionRegistryLite2 = extensionRegistryLite2;
                                messageSchema3 = messageSchema;
                            } catch (Throwable th16) {
                                th = th16;
                                obj2 = obj;
                                builderFromMessage = obj6;
                                unknownFieldSchema4 = unknownFieldSchema;
                                unknownFieldSchema = unknownFieldSchema4;
                                messageSchema3 = messageSchema;
                                extension = builderFromMessage;
                                i = messageSchema3.checkInitializedCount;
                                objFilterMapUnknownEnumValues = extension;
                                while (i < messageSchema3.repeatedFieldOffsetStart) {
                                    objFilterMapUnknownEnumValues = messageSchema3.filterMapUnknownEnumValues(obj2, messageSchema3.intArray[i], objFilterMapUnknownEnumValues, unknownFieldSchema, obj);
                                    i++;
                                    messageSchema3 = this;
                                }
                                unknownFieldSchema2 = unknownFieldSchema;
                                if (objFilterMapUnknownEnumValues != null) {
                                    unknownFieldSchema2.setBuilderToMessage(obj2, objFilterMapUnknownEnumValues);
                                }
                                throw th;
                            }
                        } else {
                            obj2 = obj;
                            builderFromMessage = extension;
                            unknownFieldSchema4 = unknownFieldSchema;
                            ExtensionRegistryLite extensionRegistryLite3 = extensionRegistryLite2;
                            try {
                                if (unknownFieldSchema4.shouldDiscardUnknownFields(reader)) {
                                    try {
                                        if (!reader.skipField()) {
                                        }
                                        extensionRegistryLite2 = extensionRegistryLite3;
                                        messageSchema3 = messageSchema;
                                        extension = builderFromMessage;
                                    } catch (Throwable th17) {
                                        th = th17;
                                        unknownFieldSchema = unknownFieldSchema4;
                                        messageSchema3 = messageSchema;
                                        extension = builderFromMessage;
                                        i = messageSchema3.checkInitializedCount;
                                        objFilterMapUnknownEnumValues = extension;
                                        while (i < messageSchema3.repeatedFieldOffsetStart) {
                                            objFilterMapUnknownEnumValues = messageSchema3.filterMapUnknownEnumValues(obj2, messageSchema3.intArray[i], objFilterMapUnknownEnumValues, unknownFieldSchema, obj);
                                            i++;
                                            messageSchema3 = this;
                                        }
                                        unknownFieldSchema2 = unknownFieldSchema;
                                        if (objFilterMapUnknownEnumValues != null) {
                                            unknownFieldSchema2.setBuilderToMessage(obj2, objFilterMapUnknownEnumValues);
                                        }
                                        throw th;
                                    }
                                } else {
                                    if (builderFromMessage == null) {
                                        builderFromMessage = unknownFieldSchema4.getBuilderFromMessage(obj2);
                                    }
                                    if (!unknownFieldSchema4.mergeOneFieldFrom(builderFromMessage, reader, 0)) {
                                    }
                                    extensionRegistryLite2 = extensionRegistryLite3;
                                    messageSchema3 = messageSchema;
                                    extension = builderFromMessage;
                                }
                            } catch (Throwable th18) {
                                th = th18;
                                messageSchema3 = messageSchema;
                                unknownFieldSchema = unknownFieldSchema4;
                            }
                        }
                    } catch (Throwable th19) {
                        th = th19;
                        obj3 = obj;
                        builderFromMessage = extension;
                        unknownFieldSchema3 = unknownFieldSchema;
                        messageSchema3 = messageSchema;
                        unknownFieldSchema = unknownFieldSchema3;
                        obj2 = obj3;
                    }
                }
                extension = builderFromMessage;
            } catch (Throwable th20) {
                th = th20;
                unknownFieldSchema = unknownFieldSchema;
                obj2 = obj;
            }
            i = messageSchema3.checkInitializedCount;
            objFilterMapUnknownEnumValues = extension;
            while (i < messageSchema3.repeatedFieldOffsetStart) {
                objFilterMapUnknownEnumValues = messageSchema3.filterMapUnknownEnumValues(obj2, messageSchema3.intArray[i], objFilterMapUnknownEnumValues, unknownFieldSchema, obj);
                i++;
                messageSchema3 = this;
            }
            unknownFieldSchema2 = unknownFieldSchema;
            if (objFilterMapUnknownEnumValues != null) {
                unknownFieldSchema2.setBuilderToMessage(obj2, objFilterMapUnknownEnumValues);
            }
            throw th;
        }
        Object objFilterMapUnknownEnumValues5 = builderFromMessage;
        for (int i8 = messageSchema.checkInitializedCount; i8 < messageSchema.repeatedFieldOffsetStart; i8++) {
            objFilterMapUnknownEnumValues5 = messageSchema.filterMapUnknownEnumValues(obj2, messageSchema.intArray[i8], objFilterMapUnknownEnumValues5, unknownFieldSchema4, obj);
        }
        if (objFilterMapUnknownEnumValues5 != null) {
            unknownFieldSchema4.setBuilderToMessage(obj2, objFilterMapUnknownEnumValues5);
        }
    }

    private Schema getMessageFieldSchema(int i) {
        int i2 = (i / 3) * 2;
        Schema schema = (Schema) this.objects[i2];
        if (schema != null) {
            return schema;
        }
        Schema schemaSchemaFor = Protobuf.getInstance().schemaFor((Class) this.objects[i2 + 1]);
        this.objects[i2] = schemaSchemaFor;
        return schemaSchemaFor;
    }

    private Object getMapFieldDefaultEntry(int i) {
        return this.objects[(i / 3) * 2];
    }

    private Internal.EnumVerifier getEnumFieldVerifier(int i) {
        WindowDecorActionBar$$ExternalSyntheticThrowCCEIfNotNull0.m(this.objects[((i / 3) * 2) + 1]);
        return null;
    }

    private Object mutableMessageFieldForMerge(Object obj, int i) {
        Schema messageFieldSchema = getMessageFieldSchema(i);
        long jOffset = offset(typeAndOffsetAt(i));
        if (!isFieldPresent(obj, i)) {
            return messageFieldSchema.newInstance();
        }
        Object object = UNSAFE.getObject(obj, jOffset);
        if (isMutable(object)) {
            return object;
        }
        Object objNewInstance = messageFieldSchema.newInstance();
        if (object != null) {
            messageFieldSchema.mergeFrom(objNewInstance, object);
        }
        return objNewInstance;
    }

    private void storeMessageField(Object obj, int i, Object obj2) {
        UNSAFE.putObject(obj, offset(typeAndOffsetAt(i)), obj2);
        setFieldPresent(obj, i);
    }

    private Object mutableOneofMessageFieldForMerge(Object obj, int i, int i2) {
        Schema messageFieldSchema = getMessageFieldSchema(i2);
        if (!isOneofPresent(obj, i, i2)) {
            return messageFieldSchema.newInstance();
        }
        Object object = UNSAFE.getObject(obj, offset(typeAndOffsetAt(i2)));
        if (isMutable(object)) {
            return object;
        }
        Object objNewInstance = messageFieldSchema.newInstance();
        if (object != null) {
            messageFieldSchema.mergeFrom(objNewInstance, object);
        }
        return objNewInstance;
    }

    private void storeOneofMessageField(Object obj, int i, int i2, Object obj2) {
        UNSAFE.putObject(obj, offset(typeAndOffsetAt(i2)), obj2);
        setOneofPresent(obj, i, i2);
    }

    /* JADX WARN: Code duplicated, block: B:25:0x006a  */
    /* JADX WARN: Code duplicated, block: B:27:0x0070  */
    /* JADX WARN: Code duplicated, block: B:40:0x007d A[SYNTHETIC] */
    @Override // androidx.datastore.preferences.protobuf.Schema
    public void makeImmutable(Object obj) {
        if (isMutable(obj)) {
            if (obj instanceof GeneratedMessageLite) {
                GeneratedMessageLite generatedMessageLite = (GeneratedMessageLite) obj;
                generatedMessageLite.clearMemoizedSerializedSize();
                generatedMessageLite.clearMemoizedHashCode();
                generatedMessageLite.markImmutable();
            }
            int length = this.buffer.length;
            for (int i = 0; i < length; i += 3) {
                int iTypeAndOffsetAt = typeAndOffsetAt(i);
                long jOffset = offset(iTypeAndOffsetAt);
                int iType = type(iTypeAndOffsetAt);
                if (iType != 9) {
                    if (iType != 60 && iType != 68) {
                        switch (iType) {
                            case 17:
                                if (isFieldPresent(obj, i)) {
                                    getMessageFieldSchema(i).makeImmutable(UNSAFE.getObject(obj, jOffset));
                                }
                                break;
                            case 18:
                            case 19:
                            case 20:
                            case 21:
                            case 22:
                            case 23:
                            case 24:
                            case 25:
                            case 26:
                            case 27:
                            case 28:
                            case 29:
                            case 30:
                            case 31:
                            case 32:
                            case 33:
                            case 34:
                            case 35:
                            case 36:
                            case 37:
                            case 38:
                            case 39:
                            case 40:
                            case 41:
                            case 42:
                            case 43:
                            case 44:
                            case 45:
                            case 46:
                            case 47:
                            case 48:
                            case 49:
                                this.listFieldSchema.makeImmutableListAt(obj, jOffset);
                                break;
                            case 50:
                                Unsafe unsafe = UNSAFE;
                                Object object = unsafe.getObject(obj, jOffset);
                                if (object != null) {
                                    unsafe.putObject(obj, jOffset, this.mapFieldSchema.toImmutable(object));
                                }
                                break;
                        }
                    } else if (isOneofPresent(obj, numberAt(i), i)) {
                        getMessageFieldSchema(i).makeImmutable(UNSAFE.getObject(obj, jOffset));
                    }
                } else if (isFieldPresent(obj, i)) {
                    getMessageFieldSchema(i).makeImmutable(UNSAFE.getObject(obj, jOffset));
                }
            }
            this.unknownFieldSchema.makeImmutable(obj);
            if (this.hasExtensions) {
                this.extensionSchema.makeImmutable(obj);
            }
        }
    }

    private final void mergeMap(Object obj, int i, Object obj2, ExtensionRegistryLite extensionRegistryLite, Reader reader) {
        long jOffset = offset(typeAndOffsetAt(i));
        Object object = UnsafeUtil.getObject(obj, jOffset);
        if (object == null) {
            object = this.mapFieldSchema.newMapField(obj2);
            UnsafeUtil.putObject(obj, jOffset, object);
        } else if (this.mapFieldSchema.isImmutable(object)) {
            Object objNewMapField = this.mapFieldSchema.newMapField(obj2);
            this.mapFieldSchema.mergeFrom(objNewMapField, object);
            UnsafeUtil.putObject(obj, jOffset, objNewMapField);
            object = objNewMapField;
        }
        reader.readMap(this.mapFieldSchema.forMutableMapData(object), this.mapFieldSchema.forMapMetadata(obj2), extensionRegistryLite);
    }

    private Object filterMapUnknownEnumValues(Object obj, int i, Object obj2, UnknownFieldSchema unknownFieldSchema, Object obj3) {
        numberAt(i);
        if (UnsafeUtil.getObject(obj, offset(typeAndOffsetAt(i))) == null) {
            return obj2;
        }
        getEnumFieldVerifier(i);
        return obj2;
    }

    /* JADX WARN: Code duplicated, block: B:39:0x007c  */
    /* JADX WARN: Code duplicated, block: B:58:0x0082 A[SYNTHETIC] */
    /* JADX WARN: Code duplicated, block: B:63:0x0094 A[SYNTHETIC] */
    @Override // androidx.datastore.preferences.protobuf.Schema
    public final boolean isInitialized(Object obj) {
        int i;
        int i2;
        int i3 = 1048575;
        int i4 = 0;
        int i5 = 0;
        while (i4 < this.checkInitializedCount) {
            int i6 = this.intArray[i4];
            int iNumberAt = numberAt(i6);
            int iTypeAndOffsetAt = typeAndOffsetAt(i6);
            int i7 = this.buffer[i6 + 2];
            int i8 = i7 & 1048575;
            int i9 = 1 << (i7 >>> 20);
            if (i8 != i3) {
                if (i8 != 1048575) {
                    i5 = UNSAFE.getInt(obj, i8);
                }
                i2 = i5;
                i = i8;
            } else {
                i = i3;
                i2 = i5;
            }
            Object obj2 = obj;
            if (isRequired(iTypeAndOffsetAt) && !isFieldPresent(obj2, i6, i, i2, i9)) {
                return false;
            }
            int iType = type(iTypeAndOffsetAt);
            if (iType == 9 || iType == 17) {
                if (isFieldPresent(obj2, i6, i, i2, i9) && !isInitialized(obj2, iTypeAndOffsetAt, getMessageFieldSchema(i6))) {
                    return false;
                }
            } else if (iType == 27) {
                if (!isListInitialized(obj2, iTypeAndOffsetAt, i6)) {
                    return false;
                }
            } else if (iType == 60 || iType == 68) {
                if (isOneofPresent(obj2, iNumberAt, i6) && !isInitialized(obj2, iTypeAndOffsetAt, getMessageFieldSchema(i6))) {
                    return false;
                }
            } else if (iType == 49) {
                if (!isListInitialized(obj2, iTypeAndOffsetAt, i6)) {
                    return false;
                }
            } else if (iType == 50 && !isMapInitialized(obj2, iTypeAndOffsetAt, i6)) {
                return false;
            }
            i4++;
            obj = obj2;
            i3 = i;
            i5 = i2;
        }
        return !this.hasExtensions || this.extensionSchema.getExtensions(obj).isInitialized();
    }

    private static boolean isInitialized(Object obj, int i, Schema schema) {
        return schema.isInitialized(UnsafeUtil.getObject(obj, offset(i)));
    }

    private boolean isListInitialized(Object obj, int i, int i2) {
        List list = (List) UnsafeUtil.getObject(obj, offset(i));
        if (list.isEmpty()) {
            return true;
        }
        Schema messageFieldSchema = getMessageFieldSchema(i2);
        for (int i3 = 0; i3 < list.size(); i3++) {
            if (!messageFieldSchema.isInitialized(list.get(i3))) {
                return false;
            }
        }
        return true;
    }

    private boolean isMapInitialized(Object obj, int i, int i2) {
        Map mapForMapData = this.mapFieldSchema.forMapData(UnsafeUtil.getObject(obj, offset(i)));
        if (mapForMapData.isEmpty()) {
            return true;
        }
        if (this.mapFieldSchema.forMapMetadata(getMapFieldDefaultEntry(i2)).valueType.getJavaType() != WireFormat.JavaType.MESSAGE) {
            return true;
        }
        Schema schemaSchemaFor = null;
        for (Object obj2 : mapForMapData.values()) {
            if (schemaSchemaFor == null) {
                schemaSchemaFor = Protobuf.getInstance().schemaFor((Class) obj2.getClass());
            }
            if (!schemaSchemaFor.isInitialized(obj2)) {
                return false;
            }
        }
        return true;
    }

    private void writeString(int i, Object obj, Writer writer) {
        if (obj instanceof String) {
            writer.writeString(i, (String) obj);
        } else {
            writer.writeBytes(i, (ByteString) obj);
        }
    }

    private void readString(Object obj, int i, Reader reader) {
        if (isEnforceUtf8(i)) {
            UnsafeUtil.putObject(obj, offset(i), reader.readStringRequireUtf8());
        } else if (this.lite) {
            UnsafeUtil.putObject(obj, offset(i), reader.readString());
        } else {
            UnsafeUtil.putObject(obj, offset(i), reader.readBytes());
        }
    }

    private void readStringList(Object obj, int i, Reader reader) {
        if (isEnforceUtf8(i)) {
            reader.readStringListRequireUtf8(this.listFieldSchema.mutableListAt(obj, offset(i)));
        } else {
            reader.readStringList(this.listFieldSchema.mutableListAt(obj, offset(i)));
        }
    }

    private void readMessageList(Object obj, int i, Reader reader, Schema schema, ExtensionRegistryLite extensionRegistryLite) {
        reader.readMessageList(this.listFieldSchema.mutableListAt(obj, offset(i)), schema, extensionRegistryLite);
    }

    private void readGroupList(Object obj, long j, Reader reader, Schema schema, ExtensionRegistryLite extensionRegistryLite) {
        reader.readGroupList(this.listFieldSchema.mutableListAt(obj, j), schema, extensionRegistryLite);
    }

    private int numberAt(int i) {
        return this.buffer[i];
    }

    private int typeAndOffsetAt(int i) {
        return this.buffer[i + 1];
    }

    private int presenceMaskAndOffsetAt(int i) {
        return this.buffer[i + 2];
    }

    private static boolean isMutable(Object obj) {
        if (obj == null) {
            return false;
        }
        if (obj instanceof GeneratedMessageLite) {
            return ((GeneratedMessageLite) obj).isMutable();
        }
        return true;
    }

    private static void checkMutable(Object obj) {
        if (isMutable(obj)) {
            return;
        }
        throw new IllegalArgumentException("Mutating immutable message: " + obj);
    }

    private static double doubleAt(Object obj, long j) {
        return UnsafeUtil.getDouble(obj, j);
    }

    private static float floatAt(Object obj, long j) {
        return UnsafeUtil.getFloat(obj, j);
    }

    private static int intAt(Object obj, long j) {
        return UnsafeUtil.getInt(obj, j);
    }

    private static long longAt(Object obj, long j) {
        return UnsafeUtil.getLong(obj, j);
    }

    private static boolean booleanAt(Object obj, long j) {
        return UnsafeUtil.getBoolean(obj, j);
    }

    private static double oneofDoubleAt(Object obj, long j) {
        return ((Double) UnsafeUtil.getObject(obj, j)).doubleValue();
    }

    private static float oneofFloatAt(Object obj, long j) {
        return ((Float) UnsafeUtil.getObject(obj, j)).floatValue();
    }

    private static int oneofIntAt(Object obj, long j) {
        return ((Integer) UnsafeUtil.getObject(obj, j)).intValue();
    }

    private static long oneofLongAt(Object obj, long j) {
        return ((Long) UnsafeUtil.getObject(obj, j)).longValue();
    }

    private static boolean oneofBooleanAt(Object obj, long j) {
        return ((Boolean) UnsafeUtil.getObject(obj, j)).booleanValue();
    }

    private boolean arePresentForEquals(Object obj, Object obj2, int i) {
        return isFieldPresent(obj, i) == isFieldPresent(obj2, i);
    }

    private boolean isFieldPresent(Object obj, int i, int i2, int i3, int i4) {
        if (i2 == 1048575) {
            return isFieldPresent(obj, i);
        }
        return (i3 & i4) != 0;
    }

    private boolean isFieldPresent(Object obj, int i) {
        boolean zEquals;
        int iPresenceMaskAndOffsetAt = presenceMaskAndOffsetAt(i);
        long j = 1048575 & iPresenceMaskAndOffsetAt;
        if (j != 1048575) {
            return (UnsafeUtil.getInt(obj, j) & (1 << (iPresenceMaskAndOffsetAt >>> 20))) != 0;
        }
        int iTypeAndOffsetAt = typeAndOffsetAt(i);
        long jOffset = offset(iTypeAndOffsetAt);
        switch (type(iTypeAndOffsetAt)) {
            case 0:
                return Double.doubleToRawLongBits(UnsafeUtil.getDouble(obj, jOffset)) != 0;
            case 1:
                return Float.floatToRawIntBits(UnsafeUtil.getFloat(obj, jOffset)) != 0;
            case 2:
                return UnsafeUtil.getLong(obj, jOffset) != 0;
            case 3:
                return UnsafeUtil.getLong(obj, jOffset) != 0;
            case 4:
                return UnsafeUtil.getInt(obj, jOffset) != 0;
            case 5:
                return UnsafeUtil.getLong(obj, jOffset) != 0;
            case 6:
                return UnsafeUtil.getInt(obj, jOffset) != 0;
            case 7:
                return UnsafeUtil.getBoolean(obj, jOffset);
            case 8:
                Object object = UnsafeUtil.getObject(obj, jOffset);
                if (object instanceof String) {
                    zEquals = ((String) object).isEmpty();
                } else if (object instanceof ByteString) {
                    zEquals = ByteString.EMPTY.equals(object);
                } else {
                    throw new IllegalArgumentException();
                }
                break;
            case 9:
                return UnsafeUtil.getObject(obj, jOffset) != null;
            case 10:
                zEquals = ByteString.EMPTY.equals(UnsafeUtil.getObject(obj, jOffset));
                break;
            case 11:
                return UnsafeUtil.getInt(obj, jOffset) != 0;
            case 12:
                return UnsafeUtil.getInt(obj, jOffset) != 0;
            case 13:
                return UnsafeUtil.getInt(obj, jOffset) != 0;
            case 14:
                return UnsafeUtil.getLong(obj, jOffset) != 0;
            case 15:
                return UnsafeUtil.getInt(obj, jOffset) != 0;
            case 16:
                return UnsafeUtil.getLong(obj, jOffset) != 0;
            case 17:
                return UnsafeUtil.getObject(obj, jOffset) != null;
            default:
                throw new IllegalArgumentException();
        }
        return !zEquals;
    }

    private void setFieldPresent(Object obj, int i) {
        int iPresenceMaskAndOffsetAt = presenceMaskAndOffsetAt(i);
        long j = 1048575 & iPresenceMaskAndOffsetAt;
        if (j == 1048575) {
            return;
        }
        UnsafeUtil.putInt(obj, j, (1 << (iPresenceMaskAndOffsetAt >>> 20)) | UnsafeUtil.getInt(obj, j));
    }

    private boolean isOneofPresent(Object obj, int i, int i2) {
        return UnsafeUtil.getInt(obj, (long) (presenceMaskAndOffsetAt(i2) & 1048575)) == i;
    }

    private boolean isOneofCaseEqual(Object obj, Object obj2, int i) {
        long jPresenceMaskAndOffsetAt = presenceMaskAndOffsetAt(i) & 1048575;
        return UnsafeUtil.getInt(obj, jPresenceMaskAndOffsetAt) == UnsafeUtil.getInt(obj2, jPresenceMaskAndOffsetAt);
    }

    private void setOneofPresent(Object obj, int i, int i2) {
        UnsafeUtil.putInt(obj, presenceMaskAndOffsetAt(i2) & 1048575, i);
    }

    private int positionForFieldNumber(int i) {
        if (i < this.minFieldNumber || i > this.maxFieldNumber) {
            return -1;
        }
        return slowPositionForFieldNumber(i, 0);
    }

    private int slowPositionForFieldNumber(int i, int i2) {
        int length = (this.buffer.length / 3) - 1;
        while (i2 <= length) {
            int i3 = (length + i2) >>> 1;
            int i4 = i3 * 3;
            int iNumberAt = numberAt(i4);
            if (i == iNumberAt) {
                return i4;
            }
            if (i < iNumberAt) {
                length = i3 - 1;
            } else {
                i2 = i3 + 1;
            }
        }
        return -1;
    }
}
