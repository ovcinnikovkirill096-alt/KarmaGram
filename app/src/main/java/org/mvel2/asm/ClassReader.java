package org.mvel2.asm;

import com.android.dx.cf.attrib.AttAnnotationDefault;
import com.android.dx.cf.attrib.AttBootstrapMethods;
import com.android.dx.cf.attrib.AttCode;
import com.android.dx.cf.attrib.AttConstantValue;
import com.android.dx.cf.attrib.AttDeprecated;
import com.android.dx.cf.attrib.AttEnclosingMethod;
import com.android.dx.cf.attrib.AttExceptions;
import com.android.dx.cf.attrib.AttInnerClasses;
import com.android.dx.cf.attrib.AttRuntimeInvisibleAnnotations;
import com.android.dx.cf.attrib.AttRuntimeInvisibleParameterAnnotations;
import com.android.dx.cf.attrib.AttRuntimeVisibleAnnotations;
import com.android.dx.cf.attrib.AttRuntimeVisibleParameterAnnotations;
import com.android.dx.cf.attrib.AttSignature;
import com.android.dx.cf.attrib.AttSourceDebugExtension;
import com.android.dx.cf.attrib.AttSourceFile;
import com.android.dx.cf.attrib.AttSynthetic;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;

public class ClassReader {
    static final int EXPAND_ASM_INSNS = 256;
    public static final int EXPAND_FRAMES = 8;
    private static final int INPUT_STREAM_DATA_CHUNK_SIZE = 4096;
    private static final int MAX_BUFFER_SIZE = 1048576;
    public static final int SKIP_CODE = 1;
    public static final int SKIP_DEBUG = 2;
    public static final int SKIP_FRAMES = 4;

    @Deprecated
    public final byte[] b;
    private final int[] bootstrapMethodOffsets;
    final byte[] classFileBuffer;
    private final ConstantDynamic[] constantDynamicValues;
    private final String[] constantUtf8Values;
    private final int[] cpInfoOffsets;
    public final int header;
    private final int maxStringLength;

    protected void readBytecodeInstructionOffset(int i) {
    }

    public ClassReader(byte[] bArr) {
        this(bArr, 0, bArr.length);
    }

    public ClassReader(byte[] bArr, int i, int i2) {
        this(bArr, i, true);
    }

    /* JADX WARN: Can't fix incorrect switch cases order, some code will duplicate */
    /* JADX WARN: Code duplicated, block: B:19:0x0063 A[PHI: r8
  0x0063: PHI (r8v3 int) = (r8v0 int), (r8v1 int), (r8v4 int) binds: [B:12:0x0052, B:22:0x006f, B:18:0x0062] A[DONT_GENERATE, DONT_INLINE]] */
    ClassReader(byte[] bArr, int i, boolean z) {
        this.classFileBuffer = bArr;
        this.b = bArr;
        if (z) {
            int i2 = i + 6;
            if (readShort(i2) > 65) {
                throw new IllegalArgumentException("Unsupported class file major version " + ((int) readShort(i2)));
            }
        }
        int unsignedShort = readUnsignedShort(i + 8);
        this.cpInfoOffsets = new int[unsignedShort];
        this.constantUtf8Values = new String[unsignedShort];
        int i3 = i + 10;
        int i4 = 0;
        boolean z2 = false;
        boolean z3 = false;
        int i5 = 1;
        while (i5 < unsignedShort) {
            int i6 = i5 + 1;
            int i7 = i3 + 1;
            this.cpInfoOffsets[i5] = i7;
            int unsignedShort2 = 3;
            switch (bArr[i3]) {
                case 1:
                    unsignedShort2 = 3 + readUnsignedShort(i7);
                    if (unsignedShort2 > i4) {
                        i5 = i6;
                        i4 = unsignedShort2;
                    } else {
                        i5 = i6;
                    }
                    i3 += unsignedShort2;
                    break;
                case 2:
                case 13:
                case 14:
                default:
                    throw new IllegalArgumentException();
                case 3:
                case 4:
                case 9:
                case 10:
                case 11:
                case 12:
                    i5 = i6;
                    unsignedShort2 = 5;
                    i3 += unsignedShort2;
                    break;
                case 5:
                case 6:
                    i5 += 2;
                    unsignedShort2 = 9;
                    i3 += unsignedShort2;
                    break;
                case 7:
                case 8:
                case 16:
                case 19:
                case 20:
                    i5 = i6;
                    i3 += unsignedShort2;
                    break;
                case 15:
                    unsignedShort2 = 4;
                    i5 = i6;
                    i3 += unsignedShort2;
                    break;
                case 17:
                    z2 = true;
                    z3 = true;
                    i5 = i6;
                    unsignedShort2 = 5;
                    i3 += unsignedShort2;
                    break;
                case 18:
                    z3 = true;
                    i5 = i6;
                    unsignedShort2 = 5;
                    i3 += unsignedShort2;
                    break;
            }
        }
        this.maxStringLength = i4;
        this.header = i3;
        this.constantDynamicValues = z2 ? new ConstantDynamic[unsignedShort] : null;
        this.bootstrapMethodOffsets = z3 ? readBootstrapMethodsAttribute(i4) : null;
    }

    public ClassReader(InputStream inputStream) {
        this(readStream(inputStream, false));
    }

    public ClassReader(String str) {
        this(readStream(ClassLoader.getSystemResourceAsStream(str.replace('.', '/') + ".class"), true));
    }

    private static byte[] readStream(InputStream inputStream, boolean z) throws IOException {
        if (inputStream == null) {
            throw new IOException("Class not found");
        }
        int iComputeBufferSize = computeBufferSize(inputStream);
        try {
            ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
            try {
                byte[] bArr = new byte[iComputeBufferSize];
                int i = 0;
                while (true) {
                    int i2 = inputStream.read(bArr, 0, iComputeBufferSize);
                    if (i2 == -1) {
                        break;
                    }
                    byteArrayOutputStream.write(bArr, 0, i2);
                    i++;
                }
                byteArrayOutputStream.flush();
                if (i != 1) {
                    byte[] byteArray = byteArrayOutputStream.toByteArray();
                    byteArrayOutputStream.close();
                    if (z) {
                        inputStream.close();
                    }
                    return byteArray;
                }
                byteArrayOutputStream.close();
                if (z) {
                    inputStream.close();
                }
                return bArr;
            } catch (Throwable th) {
                try {
                    byteArrayOutputStream.close();
                } catch (Throwable unused) {
                }
                throw th;
            }
        } catch (Throwable th2) {
            if (z) {
                inputStream.close();
            }
            throw th2;
        }
    }

    private static int computeBufferSize(InputStream inputStream) throws IOException {
        int iAvailable = inputStream.available();
        if (iAvailable < 256) {
            return 4096;
        }
        return Math.min(iAvailable, 1048576);
    }

    public int getAccess() {
        return readUnsignedShort(this.header);
    }

    public String getClassName() {
        return readClass(this.header + 2, new char[this.maxStringLength]);
    }

    public String getSuperName() {
        return readClass(this.header + 4, new char[this.maxStringLength]);
    }

    public String[] getInterfaces() {
        int i = this.header + 6;
        int unsignedShort = readUnsignedShort(i);
        String[] strArr = new String[unsignedShort];
        if (unsignedShort > 0) {
            char[] cArr = new char[this.maxStringLength];
            for (int i2 = 0; i2 < unsignedShort; i2++) {
                i += 2;
                strArr[i2] = readClass(i, cArr);
            }
        }
        return strArr;
    }

    public void accept(ClassVisitor classVisitor, int i) {
        accept(classVisitor, new Attribute[0], i);
    }

    public void accept(ClassVisitor classVisitor, Attribute[] attributeArr, int i) {
        Context context;
        ClassReader classReader;
        String str;
        int i2;
        String str2;
        int i3;
        Context context2 = new Context();
        context2.attributePrototypes = attributeArr;
        context2.parsingOptions = i;
        char[] cArr = new char[this.maxStringLength];
        context2.charBuffer = cArr;
        int i4 = this.header;
        int unsignedShort = readUnsignedShort(i4);
        String str3 = readClass(i4 + 2, cArr);
        String str4 = readClass(i4 + 4, cArr);
        int unsignedShort2 = readUnsignedShort(i4 + 6);
        String[] strArr = new String[unsignedShort2];
        int i5 = i4 + 8;
        for (int i6 = 0; i6 < unsignedShort2; i6++) {
            strArr[i6] = readClass(i5, cArr);
            i5 += 2;
        }
        int firstAttributeOffset = getFirstAttributeOffset();
        int unsignedShort3 = readUnsignedShort(firstAttributeOffset - 2);
        String str5 = null;
        String utf = null;
        String str6 = null;
        int i7 = 0;
        int i8 = 0;
        String utf8 = null;
        int i9 = 0;
        int i10 = 0;
        String str7 = null;
        int i11 = 0;
        int i12 = 0;
        int i13 = 0;
        int i14 = 0;
        Attribute attribute = null;
        int i15 = 0;
        int i16 = 0;
        int i17 = 0;
        while (unsignedShort3 > 0) {
            int i18 = firstAttributeOffset;
            String utf9 = readUTF8(i18, cArr);
            int i19 = readInt(i18 + 2);
            String utf10 = str5;
            int i20 = i18 + 6;
            String str8 = utf;
            if (AttSourceFile.ATTRIBUTE_NAME.equals(utf9)) {
                utf10 = readUTF8(i20, cArr);
                i3 = unsignedShort;
                i2 = i20;
                str3 = str3;
                utf = str8;
                i19 = i19;
                context2 = context2;
            } else {
                if (AttInnerClasses.ATTRIBUTE_NAME.equals(utf9)) {
                    i3 = unsignedShort;
                    i2 = i20;
                    i16 = i2;
                } else if (AttEnclosingMethod.ATTRIBUTE_NAME.equals(utf9)) {
                    i3 = unsignedShort;
                    i2 = i20;
                    i8 = i2;
                } else {
                    if ("NestHost".equals(utf9)) {
                        str6 = readClass(i20, cArr);
                    } else if ("NestMembers".equals(utf9)) {
                        i3 = unsignedShort;
                        i2 = i20;
                        i14 = i2;
                    } else if ("PermittedSubclasses".equals(utf9)) {
                        i3 = unsignedShort;
                        i2 = i20;
                        i15 = i2;
                    } else if (AttSignature.ATTRIBUTE_NAME.equals(utf9)) {
                        utf8 = readUTF8(i20, cArr);
                    } else if (AttRuntimeVisibleAnnotations.ATTRIBUTE_NAME.equals(utf9)) {
                        i3 = unsignedShort;
                        i2 = i20;
                        i7 = i2;
                    } else if ("RuntimeVisibleTypeAnnotations".equals(utf9)) {
                        i3 = unsignedShort;
                        i2 = i20;
                        i12 = i2;
                    } else {
                        if (AttDeprecated.ATTRIBUTE_NAME.equals(utf9)) {
                            i3 = 131072 | unsignedShort;
                        } else if (AttSynthetic.ATTRIBUTE_NAME.equals(utf9)) {
                            i3 = unsignedShort | 4096;
                        } else if (AttSourceDebugExtension.ATTRIBUTE_NAME.equals(utf9)) {
                            if (i19 > this.classFileBuffer.length - i20) {
                                throw new IllegalArgumentException();
                            }
                            utf = readUtf(i20, i19, new char[i19]);
                            i3 = unsignedShort;
                            i2 = i20;
                            str3 = str3;
                        } else if (AttRuntimeInvisibleAnnotations.ATTRIBUTE_NAME.equals(utf9)) {
                            i3 = unsignedShort;
                            i2 = i20;
                            i11 = i2;
                        } else if ("RuntimeInvisibleTypeAnnotations".equals(utf9)) {
                            i3 = unsignedShort;
                            i2 = i20;
                            i13 = i2;
                        } else if ("Record".equals(utf9)) {
                            i3 = 65536 | unsignedShort;
                            i2 = i20;
                            i17 = i2;
                        } else if ("Module".equals(utf9)) {
                            i3 = unsignedShort;
                            i2 = i20;
                            i9 = i2;
                        } else if ("ModuleMainClass".equals(utf9)) {
                            str7 = readClass(i20, cArr);
                        } else if ("ModulePackages".equals(utf9)) {
                            i3 = unsignedShort;
                            i2 = i20;
                            i10 = i2;
                        } else {
                            if (AttBootstrapMethods.ATTRIBUTE_NAME.equals(utf9)) {
                                str = str8;
                                i2 = i20;
                                i19 = i19;
                                str2 = str6;
                            } else {
                                str = str8;
                                str2 = str6;
                                i2 = i20;
                                i19 = i19;
                                Attribute attribute2 = readAttribute(attributeArr, utf9, i2, i19, cArr, -1, null);
                                attribute2.nextAttribute = attribute;
                                attribute = attribute2;
                            }
                            utf = str;
                            str6 = str2;
                            i3 = unsignedShort;
                        }
                        i2 = i20;
                    }
                    i3 = unsignedShort;
                    i2 = i20;
                }
                utf = str8;
                str3 = str3;
            }
            int i21 = i2 + i19;
            unsignedShort3--;
            unsignedShort = i3;
            str5 = utf10;
            context2 = context2;
            str3 = str3;
            firstAttributeOffset = i21;
        }
        String str9 = str5;
        Context context3 = context2;
        String str10 = str3;
        String str11 = utf;
        String str12 = str6;
        Attribute attribute3 = attribute;
        classVisitor.visit(readInt(this.cpInfoOffsets[1] - 7), unsignedShort, str10, utf8, str4, strArr);
        if ((i & 2) == 0 && (str9 != null || str11 != null)) {
            classVisitor.visitSource(str9, str11);
        }
        if (i9 != 0) {
            context = context3;
            classReader = this;
            classReader.readModuleAttributes(classVisitor, context, i9, i10, str7);
        } else {
            context = context3;
            classReader = this;
        }
        if (str12 != null) {
            classVisitor.visitNestHost(str12);
        }
        if (i8 != 0) {
            String str13 = classReader.readClass(i8, cArr);
            int unsignedShort4 = classReader.readUnsignedShort(i8 + 2);
            classVisitor.visitOuterClass(str13, unsignedShort4 == 0 ? null : classReader.readUTF8(classReader.cpInfoOffsets[unsignedShort4], cArr), unsignedShort4 == 0 ? null : classReader.readUTF8(classReader.cpInfoOffsets[unsignedShort4] + 2, cArr));
        }
        if (i7 != 0) {
            int unsignedShort5 = classReader.readUnsignedShort(i7);
            int elementValues = i7 + 2;
            while (true) {
                int i22 = unsignedShort5 - 1;
                if (unsignedShort5 <= 0) {
                    break;
                }
                elementValues = classReader.readElementValues(classVisitor.visitAnnotation(classReader.readUTF8(elementValues, cArr), true), elementValues + 2, true, cArr);
                unsignedShort5 = i22;
            }
        }
        int i23 = i11;
        if (i23 != 0) {
            int unsignedShort6 = classReader.readUnsignedShort(i23);
            int elementValues2 = i23 + 2;
            while (true) {
                int i24 = unsignedShort6 - 1;
                if (unsignedShort6 <= 0) {
                    break;
                }
                elementValues2 = classReader.readElementValues(classVisitor.visitAnnotation(classReader.readUTF8(elementValues2, cArr), false), elementValues2 + 2, true, cArr);
                unsignedShort6 = i24;
            }
        }
        int i25 = i12;
        if (i25 != 0) {
            int unsignedShort7 = classReader.readUnsignedShort(i25);
            int elementValues3 = i25 + 2;
            while (true) {
                int i26 = unsignedShort7 - 1;
                if (unsignedShort7 <= 0) {
                    break;
                }
                int typeAnnotationTarget = classReader.readTypeAnnotationTarget(context, elementValues3);
                elementValues3 = classReader.readElementValues(classVisitor.visitTypeAnnotation(context.currentTypeAnnotationTarget, context.currentTypeAnnotationTargetPath, classReader.readUTF8(typeAnnotationTarget, cArr), true), typeAnnotationTarget + 2, true, cArr);
                unsignedShort7 = i26;
            }
        }
        int i27 = i13;
        if (i27 != 0) {
            int unsignedShort8 = classReader.readUnsignedShort(i27);
            int elementValues4 = i27 + 2;
            while (true) {
                int i28 = unsignedShort8 - 1;
                if (unsignedShort8 <= 0) {
                    break;
                }
                int typeAnnotationTarget2 = classReader.readTypeAnnotationTarget(context, elementValues4);
                elementValues4 = classReader.readElementValues(classVisitor.visitTypeAnnotation(context.currentTypeAnnotationTarget, context.currentTypeAnnotationTargetPath, classReader.readUTF8(typeAnnotationTarget2, cArr), false), typeAnnotationTarget2 + 2, true, cArr);
                unsignedShort8 = i28;
            }
        }
        while (attribute3 != null) {
            Attribute attribute4 = attribute3.nextAttribute;
            attribute3.nextAttribute = null;
            classVisitor.visitAttribute(attribute3);
            attribute3 = attribute4;
        }
        int i29 = i14;
        if (i29 != 0) {
            int unsignedShort9 = classReader.readUnsignedShort(i29);
            int i30 = i29 + 2;
            while (true) {
                int i31 = unsignedShort9 - 1;
                if (unsignedShort9 <= 0) {
                    break;
                }
                classVisitor.visitNestMember(classReader.readClass(i30, cArr));
                i30 += 2;
                unsignedShort9 = i31;
            }
        }
        int i32 = i15;
        if (i32 != 0) {
            int unsignedShort10 = classReader.readUnsignedShort(i32);
            int i33 = i32 + 2;
            while (true) {
                int i34 = unsignedShort10 - 1;
                if (unsignedShort10 <= 0) {
                    break;
                }
                classVisitor.visitPermittedSubclass(classReader.readClass(i33, cArr));
                i33 += 2;
                unsignedShort10 = i34;
            }
        }
        int i35 = i16;
        if (i35 != 0) {
            int unsignedShort11 = classReader.readUnsignedShort(i35);
            int i36 = i35 + 2;
            while (true) {
                int i37 = unsignedShort11 - 1;
                if (unsignedShort11 <= 0) {
                    break;
                }
                classVisitor.visitInnerClass(classReader.readClass(i36, cArr), classReader.readClass(i36 + 2, cArr), classReader.readUTF8(i36 + 4, cArr), classReader.readUnsignedShort(i36 + 6));
                i36 += 8;
                unsignedShort11 = i37;
            }
        }
        int i38 = i17;
        if (i38 != 0) {
            int unsignedShort12 = classReader.readUnsignedShort(i38);
            int recordComponent = i38 + 2;
            while (true) {
                int i39 = unsignedShort12 - 1;
                if (unsignedShort12 <= 0) {
                    break;
                }
                recordComponent = classReader.readRecordComponent(classVisitor, context, recordComponent);
                unsignedShort12 = i39;
            }
        }
        int unsignedShort13 = classReader.readUnsignedShort(i5);
        int field = i5 + 2;
        while (true) {
            int i40 = unsignedShort13 - 1;
            if (unsignedShort13 <= 0) {
                break;
            }
            field = classReader.readField(classVisitor, context, field);
            unsignedShort13 = i40;
        }
        int unsignedShort14 = classReader.readUnsignedShort(field);
        int method = field + 2;
        while (true) {
            int i41 = unsignedShort14 - 1;
            if (unsignedShort14 > 0) {
                method = classReader.readMethod(classVisitor, context, method);
                unsignedShort14 = i41;
            } else {
                classVisitor.visitEnd();
                return;
            }
        }
    }

    private void readModuleAttributes(ClassVisitor classVisitor, Context context, int i, int i2, String str) {
        String[] strArr;
        char[] cArr = context.charBuffer;
        int i3 = i + 6;
        ModuleVisitor moduleVisitorVisitModule = classVisitor.visitModule(readModule(i, cArr), readUnsignedShort(i + 2), readUTF8(i + 4, cArr));
        if (moduleVisitorVisitModule == null) {
            return;
        }
        if (str != null) {
            moduleVisitorVisitModule.visitMainClass(str);
        }
        if (i2 != 0) {
            int unsignedShort = readUnsignedShort(i2);
            int i4 = i2 + 2;
            while (true) {
                int i5 = unsignedShort - 1;
                if (unsignedShort <= 0) {
                    break;
                }
                moduleVisitorVisitModule.visitPackage(readPackage(i4, cArr));
                i4 += 2;
                unsignedShort = i5;
            }
        }
        int unsignedShort2 = readUnsignedShort(i3);
        int i6 = i + 8;
        while (true) {
            int i7 = unsignedShort2 - 1;
            if (unsignedShort2 <= 0) {
                break;
            }
            String module = readModule(i6, cArr);
            int unsignedShort3 = readUnsignedShort(i6 + 2);
            String utf8 = readUTF8(i6 + 4, cArr);
            i6 += 6;
            moduleVisitorVisitModule.visitRequire(module, unsignedShort3, utf8);
            unsignedShort2 = i7;
        }
        int unsignedShort4 = readUnsignedShort(i6);
        int i8 = i6 + 2;
        while (true) {
            int i9 = unsignedShort4 - 1;
            String[] strArr2 = null;
            if (unsignedShort4 <= 0) {
                break;
            }
            String str2 = readPackage(i8, cArr);
            int unsignedShort5 = readUnsignedShort(i8 + 2);
            int unsignedShort6 = readUnsignedShort(i8 + 4);
            i8 += 6;
            if (unsignedShort6 != 0) {
                strArr2 = new String[unsignedShort6];
                for (int i10 = 0; i10 < unsignedShort6; i10++) {
                    strArr2[i10] = readModule(i8, cArr);
                    i8 += 2;
                }
            }
            moduleVisitorVisitModule.visitExport(str2, unsignedShort5, strArr2);
            unsignedShort4 = i9;
        }
        int unsignedShort7 = readUnsignedShort(i8);
        int i11 = i8 + 2;
        while (true) {
            int i12 = unsignedShort7 - 1;
            if (unsignedShort7 <= 0) {
                break;
            }
            String str3 = readPackage(i11, cArr);
            int unsignedShort8 = readUnsignedShort(i11 + 2);
            int unsignedShort9 = readUnsignedShort(i11 + 4);
            i11 += 6;
            if (unsignedShort9 != 0) {
                strArr = new String[unsignedShort9];
                for (int i13 = 0; i13 < unsignedShort9; i13++) {
                    strArr[i13] = readModule(i11, cArr);
                    i11 += 2;
                }
            } else {
                strArr = null;
            }
            moduleVisitorVisitModule.visitOpen(str3, unsignedShort8, strArr);
            unsignedShort7 = i12;
        }
        int unsignedShort10 = readUnsignedShort(i11);
        int i14 = i11 + 2;
        while (true) {
            int i15 = unsignedShort10 - 1;
            if (unsignedShort10 <= 0) {
                break;
            }
            moduleVisitorVisitModule.visitUse(readClass(i14, cArr));
            i14 += 2;
            unsignedShort10 = i15;
        }
        int unsignedShort11 = readUnsignedShort(i14);
        int i16 = i14 + 2;
        while (true) {
            int i17 = unsignedShort11 - 1;
            if (unsignedShort11 > 0) {
                String str4 = readClass(i16, cArr);
                int unsignedShort12 = readUnsignedShort(i16 + 2);
                i16 += 4;
                String[] strArr3 = new String[unsignedShort12];
                for (int i18 = 0; i18 < unsignedShort12; i18++) {
                    strArr3[i18] = readClass(i16, cArr);
                    i16 += 2;
                }
                moduleVisitorVisitModule.visitProvide(str4, strArr3);
                unsignedShort11 = i17;
            } else {
                moduleVisitorVisitModule.visitEnd();
                return;
            }
        }
    }

    private int readRecordComponent(ClassVisitor classVisitor, Context context, int i) {
        int i2;
        Attribute attribute;
        char[] cArr = context.charBuffer;
        String utf8 = readUTF8(i, cArr);
        String utf9 = readUTF8(i + 2, cArr);
        int unsignedShort = readUnsignedShort(i + 4);
        int i3 = i + 6;
        int i4 = 0;
        Attribute attribute2 = null;
        int i5 = 0;
        String utf10 = null;
        int i6 = 0;
        int i7 = 0;
        while (true) {
            int i8 = unsignedShort - 1;
            if (unsignedShort <= 0) {
                break;
            }
            String utf11 = readUTF8(i3, cArr);
            int i9 = readInt(i3 + 2);
            int i10 = i3 + 6;
            if (AttSignature.ATTRIBUTE_NAME.equals(utf11)) {
                utf10 = readUTF8(i10, cArr);
                i10 = i4;
            } else {
                if (AttRuntimeVisibleAnnotations.ATTRIBUTE_NAME.equals(utf11)) {
                    i6 = i10;
                    attribute = attribute2;
                    i2 = i9;
                    i10 = i4;
                    i10 = i6;
                } else if (!"RuntimeVisibleTypeAnnotations".equals(utf11)) {
                    if (AttRuntimeInvisibleAnnotations.ATTRIBUTE_NAME.equals(utf11)) {
                        i7 = i10;
                        attribute = attribute2;
                        i2 = i9;
                        i10 = i4;
                        i10 = i7;
                    } else if ("RuntimeInvisibleTypeAnnotations".equals(utf11)) {
                        i5 = i10;
                        attribute = attribute2;
                        i2 = i9;
                        i10 = i4;
                        i10 = i5;
                    } else {
                        int i11 = i4;
                        i10 = i10;
                        Attribute attribute3 = attribute2;
                        i2 = i9;
                        Attribute attribute4 = readAttribute(context.attributePrototypes, utf11, i10, i2, cArr, -1, null);
                        attribute4.nextAttribute = attribute3;
                        attribute = attribute4;
                        i10 = i11;
                        i5 = i5;
                    }
                }
                int i12 = i10 + i2;
                i4 = i10;
                i3 = i12;
                attribute2 = attribute;
                unsignedShort = i8;
            }
            attribute = attribute2;
            i2 = i9;
            int i13 = i10 + i2;
            i4 = i10;
            i3 = i13;
            attribute2 = attribute;
            unsignedShort = i8;
        }
        int i14 = i4;
        Attribute attribute5 = attribute2;
        int i15 = i5;
        RecordComponentVisitor recordComponentVisitorVisitRecordComponent = classVisitor.visitRecordComponent(utf8, utf9, utf10);
        if (recordComponentVisitorVisitRecordComponent == null) {
            return i3;
        }
        if (i6 != 0) {
            int unsignedShort2 = readUnsignedShort(i6);
            int elementValues = i6 + 2;
            while (true) {
                int i16 = unsignedShort2 - 1;
                if (unsignedShort2 <= 0) {
                    break;
                }
                elementValues = readElementValues(recordComponentVisitorVisitRecordComponent.visitAnnotation(readUTF8(elementValues, cArr), true), elementValues + 2, true, cArr);
                unsignedShort2 = i16;
            }
        }
        if (i7 != 0) {
            int unsignedShort3 = readUnsignedShort(i7);
            int elementValues2 = i7 + 2;
            while (true) {
                int i17 = unsignedShort3 - 1;
                if (unsignedShort3 <= 0) {
                    break;
                }
                elementValues2 = readElementValues(recordComponentVisitorVisitRecordComponent.visitAnnotation(readUTF8(elementValues2, cArr), false), elementValues2 + 2, true, cArr);
                unsignedShort3 = i17;
            }
        }
        if (i14 != 0) {
            int unsignedShort4 = readUnsignedShort(i14);
            int elementValues3 = i14 + 2;
            while (true) {
                int i18 = unsignedShort4 - 1;
                if (unsignedShort4 <= 0) {
                    break;
                }
                int typeAnnotationTarget = readTypeAnnotationTarget(context, elementValues3);
                elementValues3 = readElementValues(recordComponentVisitorVisitRecordComponent.visitTypeAnnotation(context.currentTypeAnnotationTarget, context.currentTypeAnnotationTargetPath, readUTF8(typeAnnotationTarget, cArr), true), typeAnnotationTarget + 2, true, cArr);
                unsignedShort4 = i18;
            }
        }
        if (i15 != 0) {
            int unsignedShort5 = readUnsignedShort(i15);
            int elementValues4 = i15 + 2;
            while (true) {
                int i19 = unsignedShort5 - 1;
                if (unsignedShort5 <= 0) {
                    break;
                }
                int typeAnnotationTarget2 = readTypeAnnotationTarget(context, elementValues4);
                elementValues4 = readElementValues(recordComponentVisitorVisitRecordComponent.visitTypeAnnotation(context.currentTypeAnnotationTarget, context.currentTypeAnnotationTargetPath, readUTF8(typeAnnotationTarget2, cArr), false), typeAnnotationTarget2 + 2, true, cArr);
                unsignedShort5 = i19;
            }
        }
        Attribute attribute6 = attribute5;
        while (attribute6 != null) {
            Attribute attribute7 = attribute6.nextAttribute;
            attribute6.nextAttribute = null;
            recordComponentVisitorVisitRecordComponent.visitAttribute(attribute6);
            attribute6 = attribute7;
        }
        recordComponentVisitorVisitRecordComponent.visitEnd();
        return i3;
    }

    private int readField(ClassVisitor classVisitor, Context context, int i) {
        int i2;
        int i3;
        int i4;
        Context context2 = context;
        char[] cArr = context2.charBuffer;
        int unsignedShort = readUnsignedShort(i);
        String utf8 = readUTF8(i + 2, cArr);
        String utf9 = readUTF8(i + 4, cArr);
        int unsignedShort2 = readUnsignedShort(i + 6);
        int i5 = i + 8;
        int i6 = unsignedShort;
        int i7 = 0;
        int i8 = 0;
        int i9 = 0;
        int i10 = 0;
        String utf10 = null;
        Object obj = null;
        Attribute attribute = null;
        while (true) {
            int i11 = unsignedShort2 - 1;
            if (unsignedShort2 <= 0) {
                break;
            }
            int i12 = i7;
            String utf11 = readUTF8(i5, cArr);
            int i13 = readInt(i5 + 2);
            int i14 = i5 + 6;
            if (AttConstantValue.ATTRIBUTE_NAME.equals(utf11)) {
                int unsignedShort3 = readUnsignedShort(i14);
                obj = unsignedShort3 == 0 ? null : readConst(unsignedShort3, cArr);
            } else {
                if (AttSignature.ATTRIBUTE_NAME.equals(utf11)) {
                    utf10 = readUTF8(i14, cArr);
                } else {
                    if (AttDeprecated.ATTRIBUTE_NAME.equals(utf11)) {
                        i4 = 131072 | i6;
                    } else if (AttSynthetic.ATTRIBUTE_NAME.equals(utf11)) {
                        i4 = i6 | 4096;
                    } else if (AttRuntimeVisibleAnnotations.ATTRIBUTE_NAME.equals(utf11)) {
                        i7 = i14;
                        i2 = i7;
                        i14 = i8;
                        i3 = i13;
                    } else {
                        if ("RuntimeVisibleTypeAnnotations".equals(utf11)) {
                            i2 = i14;
                            i9 = i2;
                        } else if (AttRuntimeInvisibleAnnotations.ATTRIBUTE_NAME.equals(utf11)) {
                            i2 = i14;
                            i3 = i13;
                            i7 = i12;
                        } else if ("RuntimeInvisibleTypeAnnotations".equals(utf11)) {
                            i2 = i14;
                            i10 = i2;
                        } else {
                            i2 = i14;
                            int i15 = i8;
                            i3 = i13;
                            Attribute attribute2 = readAttribute(context2.attributePrototypes, utf11, i2, i3, cArr, -1, null);
                            attribute2.nextAttribute = attribute;
                            attribute = attribute2;
                            i9 = i9;
                            i14 = i15;
                            i7 = i12;
                            i10 = i10;
                        }
                        i14 = i8;
                        i3 = i13;
                        i7 = i12;
                    }
                    i2 = i14;
                    i6 = i4;
                    i14 = i8;
                    i3 = i13;
                    i7 = i12;
                }
                int i16 = i2 + i3;
                context2 = context;
                i8 = i14;
                i5 = i16;
                unsignedShort2 = i11;
            }
            i2 = i14;
            i14 = i8;
            i3 = i13;
            i7 = i12;
            int i17 = i2 + i3;
            context2 = context;
            i8 = i14;
            i5 = i17;
            unsignedShort2 = i11;
        }
        int i18 = i7;
        int i19 = i8;
        int i20 = i9;
        int i21 = i10;
        FieldVisitor fieldVisitorVisitField = classVisitor.visitField(i6, utf8, utf9, utf10, obj);
        if (fieldVisitorVisitField == null) {
            return i5;
        }
        if (i18 != 0) {
            int unsignedShort4 = readUnsignedShort(i18);
            int elementValues = i18 + 2;
            while (true) {
                int i22 = unsignedShort4 - 1;
                if (unsignedShort4 <= 0) {
                    break;
                }
                elementValues = readElementValues(fieldVisitorVisitField.visitAnnotation(readUTF8(elementValues, cArr), true), elementValues + 2, true, cArr);
                unsignedShort4 = i22;
            }
        }
        if (i19 != 0) {
            int unsignedShort5 = readUnsignedShort(i19);
            int elementValues2 = i19 + 2;
            while (true) {
                int i23 = unsignedShort5 - 1;
                if (unsignedShort5 <= 0) {
                    break;
                }
                elementValues2 = readElementValues(fieldVisitorVisitField.visitAnnotation(readUTF8(elementValues2, cArr), false), elementValues2 + 2, true, cArr);
                unsignedShort5 = i23;
            }
        }
        if (i20 != 0) {
            int unsignedShort6 = readUnsignedShort(i20);
            int elementValues3 = i20 + 2;
            while (true) {
                int i24 = unsignedShort6 - 1;
                if (unsignedShort6 <= 0) {
                    break;
                }
                int typeAnnotationTarget = readTypeAnnotationTarget(context, elementValues3);
                elementValues3 = readElementValues(fieldVisitorVisitField.visitTypeAnnotation(context.currentTypeAnnotationTarget, context.currentTypeAnnotationTargetPath, readUTF8(typeAnnotationTarget, cArr), true), typeAnnotationTarget + 2, true, cArr);
                unsignedShort6 = i24;
            }
        }
        if (i21 != 0) {
            int unsignedShort7 = readUnsignedShort(i21);
            int elementValues4 = i21 + 2;
            while (true) {
                int i25 = unsignedShort7 - 1;
                if (unsignedShort7 <= 0) {
                    break;
                }
                int typeAnnotationTarget2 = readTypeAnnotationTarget(context, elementValues4);
                elementValues4 = readElementValues(fieldVisitorVisitField.visitTypeAnnotation(context.currentTypeAnnotationTarget, context.currentTypeAnnotationTargetPath, readUTF8(typeAnnotationTarget2, cArr), false), typeAnnotationTarget2 + 2, true, cArr);
                unsignedShort7 = i25;
            }
        }
        while (attribute != null) {
            Attribute attribute3 = attribute.nextAttribute;
            attribute.nextAttribute = null;
            fieldVisitorVisitField.visitAttribute(attribute);
            attribute = attribute3;
        }
        fieldVisitorVisitField.visitEnd();
        return i5;
    }

    private int readMethod(ClassVisitor classVisitor, Context context, int i) {
        int i2;
        int i3;
        int i4;
        ClassReader classReader = this;
        char[] cArr = context.charBuffer;
        context.currentMethodAccessFlags = classReader.readUnsignedShort(i);
        context.currentMethodName = classReader.readUTF8(i + 2, cArr);
        int i5 = i + 4;
        context.currentMethodDescriptor = classReader.readUTF8(i5, cArr);
        int unsignedShort = classReader.readUnsignedShort(i + 6);
        int i6 = i + 8;
        int i7 = 0;
        int i8 = 0;
        int i9 = 0;
        int i10 = 0;
        Attribute attribute = null;
        int unsignedShort2 = 0;
        int i11 = 0;
        int i12 = 0;
        String[] strArr = null;
        boolean z = false;
        int i13 = 0;
        int i14 = 0;
        int i15 = 0;
        int i16 = 0;
        while (true) {
            int i17 = unsignedShort - 1;
            if (unsignedShort <= 0) {
                break;
            }
            int i18 = i7;
            String utf8 = classReader.readUTF8(i6, cArr);
            int i19 = classReader.readInt(i6 + 2);
            int i20 = i8;
            int i21 = i6 + 6;
            int i22 = i18;
            if (AttCode.ATTRIBUTE_NAME.equals(utf8)) {
                if ((context.parsingOptions & 1) == 0) {
                    i16 = i21;
                }
            } else if (AttExceptions.ATTRIBUTE_NAME.equals(utf8)) {
                int unsignedShort3 = classReader.readUnsignedShort(i21);
                int i23 = i6 + 8;
                strArr = new String[unsignedShort3];
                for (int i24 = 0; i24 < unsignedShort3; i24++) {
                    strArr[i24] = classReader.readClass(i23, cArr);
                    i23 += 2;
                }
                i13 = i21;
            } else if (AttSignature.ATTRIBUTE_NAME.equals(utf8)) {
                unsignedShort2 = classReader.readUnsignedShort(i21);
            } else if (AttDeprecated.ATTRIBUTE_NAME.equals(utf8)) {
                context.currentMethodAccessFlags |= 131072;
            } else if (AttRuntimeVisibleAnnotations.ATTRIBUTE_NAME.equals(utf8)) {
                i22 = i21;
            } else {
                if ("RuntimeVisibleTypeAnnotations".equals(utf8)) {
                    i3 = i21;
                    i4 = i19;
                } else if (AttAnnotationDefault.ATTRIBUTE_NAME.equals(utf8)) {
                    i12 = i21;
                } else if (AttSynthetic.ATTRIBUTE_NAME.equals(utf8)) {
                    context.currentMethodAccessFlags |= 4096;
                    i3 = i9;
                    i4 = i19;
                    z = true;
                } else if (AttRuntimeInvisibleAnnotations.ATTRIBUTE_NAME.equals(utf8)) {
                    i20 = i21;
                } else if ("RuntimeInvisibleTypeAnnotations".equals(utf8)) {
                    i10 = i21;
                } else if (AttRuntimeVisibleParameterAnnotations.ATTRIBUTE_NAME.equals(utf8)) {
                    i14 = i21;
                } else if (AttRuntimeInvisibleParameterAnnotations.ATTRIBUTE_NAME.equals(utf8)) {
                    i15 = i21;
                } else if ("MethodParameters".equals(utf8)) {
                    i11 = i21;
                } else {
                    i3 = i9;
                    i4 = i19;
                    Attribute attribute2 = classReader.readAttribute(context.attributePrototypes, utf8, i21, i4, cArr, -1, null);
                    attribute2.nextAttribute = attribute;
                    attribute = attribute2;
                    i10 = i10;
                }
                i6 = i21 + i4;
                cArr = cArr;
                unsignedShort = i17;
                i8 = i20;
                i7 = i22;
                i9 = i3;
            }
            i3 = i9;
            i4 = i19;
            i6 = i21 + i4;
            cArr = cArr;
            unsignedShort = i17;
            i8 = i20;
            i7 = i22;
            i9 = i3;
        }
        int i25 = i7;
        int i26 = i8;
        int i27 = i9;
        char[] cArr2 = cArr;
        int i28 = i10;
        int i29 = i12;
        int i30 = i11;
        MethodVisitor methodVisitorVisitMethod = classVisitor.visitMethod(context.currentMethodAccessFlags, context.currentMethodName, context.currentMethodDescriptor, unsignedShort2 == 0 ? null : classReader.readUtf(unsignedShort2, cArr2), strArr);
        if (methodVisitorVisitMethod == null) {
            return i6;
        }
        if (methodVisitorVisitMethod instanceof MethodWriter) {
            MethodWriter methodWriter = (MethodWriter) methodVisitorVisitMethod;
            int i31 = unsignedShort2;
            i2 = i30;
            boolean zCanCopyMethodAttributes = methodWriter.canCopyMethodAttributes(classReader, z, (context.currentMethodAccessFlags & 131072) != 0, classReader.readUnsignedShort(i5), i31, i13);
            classReader = classReader;
            if (zCanCopyMethodAttributes) {
                methodWriter.setMethodAttributesSource(i, i6 - i);
                return i6;
            }
        } else {
            i2 = i30;
        }
        if (i2 != 0 && (context.parsingOptions & 2) == 0) {
            int i32 = classReader.readByte(i2);
            int i33 = i2 + 1;
            while (true) {
                int i34 = i32 - 1;
                if (i32 <= 0) {
                    break;
                }
                methodVisitorVisitMethod.visitParameter(classReader.readUTF8(i33, cArr2), classReader.readUnsignedShort(i33 + 2));
                i33 += 4;
                i32 = i34;
            }
        }
        if (r3 != 0) {
            AnnotationVisitor annotationVisitorVisitAnnotationDefault = methodVisitorVisitMethod.visitAnnotationDefault();
            classReader.readElementValue(annotationVisitorVisitAnnotationDefault, i29, null, cArr2);
            if (annotationVisitorVisitAnnotationDefault != null) {
                annotationVisitorVisitAnnotationDefault.visitEnd();
            }
        }
        if (i25 != 0) {
            int unsignedShort4 = classReader.readUnsignedShort(i25);
            int elementValues = i25 + 2;
            while (true) {
                int i35 = unsignedShort4 - 1;
                if (unsignedShort4 <= 0) {
                    break;
                }
                elementValues = classReader.readElementValues(methodVisitorVisitMethod.visitAnnotation(classReader.readUTF8(elementValues, cArr2), true), elementValues + 2, true, cArr2);
                unsignedShort4 = i35;
            }
        }
        if (i26 != 0) {
            int unsignedShort5 = classReader.readUnsignedShort(i26);
            int elementValues2 = i26 + 2;
            while (true) {
                int i36 = unsignedShort5 - 1;
                if (unsignedShort5 <= 0) {
                    break;
                }
                elementValues2 = classReader.readElementValues(methodVisitorVisitMethod.visitAnnotation(classReader.readUTF8(elementValues2, cArr2), false), elementValues2 + 2, true, cArr2);
                unsignedShort5 = i36;
            }
        }
        if (i27 != 0) {
            int unsignedShort6 = classReader.readUnsignedShort(i27);
            int elementValues3 = i27 + 2;
            while (true) {
                int i37 = unsignedShort6 - 1;
                if (unsignedShort6 <= 0) {
                    break;
                }
                int typeAnnotationTarget = classReader.readTypeAnnotationTarget(context, elementValues3);
                elementValues3 = classReader.readElementValues(methodVisitorVisitMethod.visitTypeAnnotation(context.currentTypeAnnotationTarget, context.currentTypeAnnotationTargetPath, classReader.readUTF8(typeAnnotationTarget, cArr2), true), typeAnnotationTarget + 2, true, cArr2);
                unsignedShort6 = i37;
            }
        }
        if (i28 != 0) {
            int unsignedShort7 = classReader.readUnsignedShort(i28);
            int elementValues4 = i28 + 2;
            while (true) {
                int i38 = unsignedShort7 - 1;
                if (unsignedShort7 <= 0) {
                    break;
                }
                int typeAnnotationTarget2 = classReader.readTypeAnnotationTarget(context, elementValues4);
                elementValues4 = classReader.readElementValues(methodVisitorVisitMethod.visitTypeAnnotation(context.currentTypeAnnotationTarget, context.currentTypeAnnotationTargetPath, classReader.readUTF8(typeAnnotationTarget2, cArr2), false), typeAnnotationTarget2 + 2, true, cArr2);
                unsignedShort7 = i38;
            }
        }
        int i39 = i14;
        if (i39 != 0) {
            classReader.readParameterAnnotations(methodVisitorVisitMethod, context, i39, true);
        }
        int i40 = i15;
        if (i40 != 0) {
            classReader.readParameterAnnotations(methodVisitorVisitMethod, context, i40, false);
        }
        while (attribute != null) {
            Attribute attribute3 = attribute.nextAttribute;
            attribute.nextAttribute = null;
            methodVisitorVisitMethod.visitAttribute(attribute);
            attribute = attribute3;
        }
        int i41 = i16;
        if (i41 != 0) {
            methodVisitorVisitMethod.visitCode();
            classReader.readCode(methodVisitorVisitMethod, context, i41);
        }
        methodVisitorVisitMethod.visitEnd();
        return i6;
    }

    /*  JADX ERROR: Type inference failed
        jadx.core.utils.exceptions.JadxOverflowException: Type inference error: updates count limit reached with updateSeq = 31261. Try increasing type updates limit count.
        	at jadx.core.utils.ErrorsCounter.addError(ErrorsCounter.java:59)
        	at jadx.core.utils.ErrorsCounter.error(ErrorsCounter.java:31)
        	at jadx.core.dex.attributes.nodes.NotificationAttrNode.addError(NotificationAttrNode.java:19)
        	at jadx.core.dex.visitors.typeinference.TypeInferenceVisitor.visit(TypeInferenceVisitor.java:79)
        */
    private void readCode(org.mvel2.asm.MethodVisitor r40, org.mvel2.asm.Context r41, int r42) {
        /*
            Method dump skipped, instruction units count: 3126
            To view this dump add '--comments-level debug' option
        */
        throw new UnsupportedOperationException("Method not decompiled: org.mvel2.asm.ClassReader.readCode(org.mvel2.asm.MethodVisitor, org.mvel2.asm.Context, int):void");
    }

    protected Label readLabel(int i, Label[] labelArr) {
        if (labelArr[i] == null) {
            labelArr[i] = new Label();
        }
        return labelArr[i];
    }

    private Label createLabel(int i, Label[] labelArr) {
        Label label = readLabel(i, labelArr);
        label.flags = (short) (label.flags & (-2));
        return label;
    }

    private void createDebugLabel(int i, Label[] labelArr) {
        if (labelArr[i] == null) {
            Label label = readLabel(i, labelArr);
            label.flags = (short) (label.flags | 1);
        }
    }

    /* JADX WARN: Code duplicated, block: B:15:0x004d A[FALL_THROUGH] */
    private int[] readTypeAnnotations(MethodVisitor methodVisitor, Context context, int i, boolean z) {
        int i2;
        char[] cArr = context.charBuffer;
        int unsignedShort = readUnsignedShort(i);
        int[] iArr = new int[unsignedShort];
        int elementValues = i + 2;
        for (int i3 = 0; i3 < unsignedShort; i3++) {
            iArr[i3] = elementValues;
            int i4 = readInt(elementValues);
            int i5 = i4 >>> 24;
            if (i5 != 23) {
                switch (i5) {
                    default:
                        switch (i5) {
                            case 64:
                            case 65:
                                int unsignedShort2 = readUnsignedShort(elementValues + 1);
                                i2 = elementValues + 3;
                                while (true) {
                                    int i6 = unsignedShort2 - 1;
                                    if (unsignedShort2 > 0) {
                                        int unsignedShort3 = readUnsignedShort(i2);
                                        int unsignedShort4 = readUnsignedShort(i2 + 2);
                                        i2 += 6;
                                        createLabel(unsignedShort3, context.currentMethodLabels);
                                        createLabel(unsignedShort3 + unsignedShort4, context.currentMethodLabels);
                                        unsignedShort2 = i6;
                                    }
                                    break;
                                }
                                break;
                            case 66:
                            case 67:
                            case 68:
                            case 69:
                            case 70:
                                break;
                            case 71:
                            case 72:
                            case 73:
                            case 74:
                            case 75:
                                i2 = elementValues + 4;
                                break;
                            default:
                                throw new IllegalArgumentException();
                        }
                    case 16:
                    case 17:
                    case 18:
                        i2 = elementValues + 3;
                        break;
                }
            } else {
                i2 = elementValues + 3;
            }
            int i7 = readByte(i2);
            if (i5 == 66) {
                TypePath typePath = i7 != 0 ? new TypePath(this.classFileBuffer, i2) : null;
                int i8 = i2 + (i7 * 2) + 1;
                elementValues = readElementValues(methodVisitor.visitTryCatchAnnotation(i4 & (-256), typePath, readUTF8(i8, cArr), z), i8 + 2, true, cArr);
            } else {
                elementValues = readElementValues(null, i2 + (i7 * 2) + 3, true, cArr);
            }
        }
        return iArr;
    }

    private int getTypeAnnotationBytecodeOffset(int[] iArr, int i) {
        if (iArr == null || i >= iArr.length || readByte(iArr[i]) < 67) {
            return -1;
        }
        return readUnsignedShort(iArr[i] + 1);
    }

    /* JADX WARN: Can't fix incorrect switch cases order, some code will duplicate */
    /* JADX WARN: Code duplicated, block: B:17:0x006d  */
    /* JADX WARN: Code duplicated, block: B:18:0x0070  */
    private int readTypeAnnotationTarget(Context context, int i) {
        int i2;
        int i3;
        int i4 = readInt(i);
        int i5 = i4 >>> 24;
        if (i5 != 0 && i5 != 1) {
            switch (i5) {
                case 16:
                case 17:
                case 18:
                case 23:
                    i2 = i4 & (-256);
                    i3 = i + 3;
                    break;
                case 19:
                case 20:
                case 21:
                    i2 = i4 & (-16777216);
                    i3 = i + 1;
                    break;
                case 22:
                    i2 = i4 & Opcodes.V_PREVIEW;
                    i3 = i + 2;
                    break;
                default:
                    switch (i5) {
                        case 64:
                        case 65:
                            i2 = i4 & (-16777216);
                            int unsignedShort = readUnsignedShort(i + 1);
                            i3 = i + 3;
                            context.currentLocalVariableAnnotationRangeStarts = new Label[unsignedShort];
                            context.currentLocalVariableAnnotationRangeEnds = new Label[unsignedShort];
                            context.currentLocalVariableAnnotationRangeIndices = new int[unsignedShort];
                            for (int i6 = 0; i6 < unsignedShort; i6++) {
                                int unsignedShort2 = readUnsignedShort(i3);
                                int unsignedShort3 = readUnsignedShort(i3 + 2);
                                int unsignedShort4 = readUnsignedShort(i3 + 4);
                                i3 += 6;
                                context.currentLocalVariableAnnotationRangeStarts[i6] = createLabel(unsignedShort2, context.currentMethodLabels);
                                context.currentLocalVariableAnnotationRangeEnds[i6] = createLabel(unsignedShort2 + unsignedShort3, context.currentMethodLabels);
                                context.currentLocalVariableAnnotationRangeIndices[i6] = unsignedShort4;
                            }
                            break;
                        case 66:
                            i2 = i4 & (-256);
                            i3 = i + 3;
                            break;
                        case 67:
                        case 68:
                        case 69:
                        case 70:
                            i2 = i4 & (-16777216);
                            i3 = i + 3;
                            break;
                        case 71:
                        case 72:
                        case 73:
                        case 74:
                        case 75:
                            i2 = i4 & (-16776961);
                            i3 = i + 4;
                            break;
                        default:
                            throw new IllegalArgumentException();
                    }
                    break;
            }
        } else {
            i2 = i4 & Opcodes.V_PREVIEW;
            i3 = i + 2;
        }
        context.currentTypeAnnotationTarget = i2;
        int i7 = readByte(i3);
        context.currentTypeAnnotationTargetPath = i7 == 0 ? null : new TypePath(this.classFileBuffer, i3);
        return i3 + 1 + (i7 * 2);
    }

    private void readParameterAnnotations(MethodVisitor methodVisitor, Context context, int i, boolean z) {
        int elementValues = i + 1;
        int i2 = this.classFileBuffer[i] & 255;
        methodVisitor.visitAnnotableParameterCount(i2, z);
        char[] cArr = context.charBuffer;
        for (int i3 = 0; i3 < i2; i3++) {
            int unsignedShort = readUnsignedShort(elementValues);
            elementValues += 2;
            while (true) {
                int i4 = unsignedShort - 1;
                if (unsignedShort > 0) {
                    elementValues = readElementValues(methodVisitor.visitParameterAnnotation(i3, readUTF8(elementValues, cArr), z), elementValues + 2, true, cArr);
                    unsignedShort = i4;
                }
            }
        }
    }

    private int readElementValues(AnnotationVisitor annotationVisitor, int i, boolean z, char[] cArr) {
        int unsignedShort = readUnsignedShort(i);
        int elementValue = i + 2;
        if (!z) {
            while (true) {
                int i2 = unsignedShort - 1;
                if (unsignedShort <= 0) {
                    break;
                }
                elementValue = readElementValue(annotationVisitor, elementValue, null, cArr);
                unsignedShort = i2;
            }
        } else {
            while (true) {
                int i3 = unsignedShort - 1;
                if (unsignedShort <= 0) {
                    break;
                }
                elementValue = readElementValue(annotationVisitor, elementValue + 2, readUTF8(elementValue, cArr), cArr);
                unsignedShort = i3;
            }
        }
        if (annotationVisitor != null) {
            annotationVisitor.visitEnd();
        }
        return elementValue;
    }

    private int readElementValue(AnnotationVisitor annotationVisitor, int i, String str, char[] cArr) {
        Object obj;
        int i2 = 0;
        if (annotationVisitor == null) {
            int i3 = this.classFileBuffer[i] & 255;
            if (i3 == 64) {
                return readElementValues(null, i + 3, true, cArr);
            }
            if (i3 != 91) {
                return i3 != 101 ? i + 3 : i + 5;
            }
            return readElementValues(null, i + 1, false, cArr);
        }
        int i4 = i + 1;
        int i5 = this.classFileBuffer[i] & 255;
        if (i5 != 64) {
            if (i5 != 70) {
                if (i5 == 83) {
                    annotationVisitor.visit(str, Short.valueOf((short) readInt(this.cpInfoOffsets[readUnsignedShort(i4)])));
                    return i + 3;
                }
                if (i5 == 99) {
                    annotationVisitor.visit(str, Type.getType(readUTF8(i4, cArr)));
                    return i + 3;
                }
                if (i5 == 101) {
                    annotationVisitor.visitEnum(str, readUTF8(i4, cArr), readUTF8(i + 3, cArr));
                    return i + 5;
                }
                if (i5 == 115) {
                    annotationVisitor.visit(str, readUTF8(i4, cArr));
                    return i + 3;
                }
                if (i5 != 73 && i5 != 74) {
                    if (i5 == 90) {
                        if (readInt(this.cpInfoOffsets[readUnsignedShort(i4)]) == 0) {
                            obj = Boolean.FALSE;
                        } else {
                            obj = Boolean.TRUE;
                        }
                        annotationVisitor.visit(str, obj);
                        return i + 3;
                    }
                    if (i5 != 91) {
                        switch (i5) {
                            case 66:
                                annotationVisitor.visit(str, Byte.valueOf((byte) readInt(this.cpInfoOffsets[readUnsignedShort(i4)])));
                                return i + 3;
                            case 67:
                                annotationVisitor.visit(str, Character.valueOf((char) readInt(this.cpInfoOffsets[readUnsignedShort(i4)])));
                                return i + 3;
                            case 68:
                                break;
                            default:
                                throw new IllegalArgumentException();
                        }
                    } else {
                        int unsignedShort = readUnsignedShort(i4);
                        int i6 = i + 3;
                        if (unsignedShort == 0) {
                            return readElementValues(annotationVisitor.visitArray(str), i + 1, false, cArr);
                        }
                        int i7 = this.classFileBuffer[i6] & 255;
                        if (i7 == 70) {
                            float[] fArr = new float[unsignedShort];
                            while (i2 < unsignedShort) {
                                fArr[i2] = Float.intBitsToFloat(readInt(this.cpInfoOffsets[readUnsignedShort(i6 + 1)]));
                                i6 += 3;
                                i2++;
                            }
                            annotationVisitor.visit(str, fArr);
                            return i6;
                        }
                        if (i7 == 83) {
                            short[] sArr = new short[unsignedShort];
                            while (i2 < unsignedShort) {
                                sArr[i2] = (short) readInt(this.cpInfoOffsets[readUnsignedShort(i6 + 1)]);
                                i6 += 3;
                                i2++;
                            }
                            annotationVisitor.visit(str, sArr);
                            return i6;
                        }
                        if (i7 == 90) {
                            boolean[] zArr = new boolean[unsignedShort];
                            for (int i8 = 0; i8 < unsignedShort; i8++) {
                                zArr[i8] = readInt(this.cpInfoOffsets[readUnsignedShort(i6 + 1)]) != 0;
                                i6 += 3;
                            }
                            annotationVisitor.visit(str, zArr);
                            return i6;
                        }
                        if (i7 == 73) {
                            int[] iArr = new int[unsignedShort];
                            while (i2 < unsignedShort) {
                                iArr[i2] = readInt(this.cpInfoOffsets[readUnsignedShort(i6 + 1)]);
                                i6 += 3;
                                i2++;
                            }
                            annotationVisitor.visit(str, iArr);
                            return i6;
                        }
                        if (i7 != 74) {
                            switch (i7) {
                                case 66:
                                    byte[] bArr = new byte[unsignedShort];
                                    while (i2 < unsignedShort) {
                                        bArr[i2] = (byte) readInt(this.cpInfoOffsets[readUnsignedShort(i6 + 1)]);
                                        i6 += 3;
                                        i2++;
                                    }
                                    annotationVisitor.visit(str, bArr);
                                    return i6;
                                case 67:
                                    char[] cArr2 = new char[unsignedShort];
                                    while (i2 < unsignedShort) {
                                        cArr2[i2] = (char) readInt(this.cpInfoOffsets[readUnsignedShort(i6 + 1)]);
                                        i6 += 3;
                                        i2++;
                                    }
                                    annotationVisitor.visit(str, cArr2);
                                    return i6;
                                case 68:
                                    double[] dArr = new double[unsignedShort];
                                    while (i2 < unsignedShort) {
                                        dArr[i2] = Double.longBitsToDouble(readLong(this.cpInfoOffsets[readUnsignedShort(i6 + 1)]));
                                        i6 += 3;
                                        i2++;
                                    }
                                    annotationVisitor.visit(str, dArr);
                                    return i6;
                                default:
                                    return readElementValues(annotationVisitor.visitArray(str), i + 1, false, cArr);
                            }
                        }
                        long[] jArr = new long[unsignedShort];
                        while (i2 < unsignedShort) {
                            jArr[i2] = readLong(this.cpInfoOffsets[readUnsignedShort(i6 + 1)]);
                            i6 += 3;
                            i2++;
                        }
                        annotationVisitor.visit(str, jArr);
                        return i6;
                    }
                }
            }
            annotationVisitor.visit(str, readConst(readUnsignedShort(i4), cArr));
            return i + 3;
        }
        return readElementValues(annotationVisitor.visitAnnotation(str, readUTF8(i4, cArr)), i + 3, true, cArr);
    }

    private void computeImplicitFrame(Context context) {
        int i;
        String str = context.currentMethodDescriptor;
        Object[] objArr = context.currentFrameLocalTypes;
        int i2 = 0;
        if ((context.currentMethodAccessFlags & 8) == 0) {
            if ("<init>".equals(context.currentMethodName)) {
                objArr[0] = Opcodes.UNINITIALIZED_THIS;
            } else {
                objArr[0] = readClass(this.header + 2, context.charBuffer);
            }
            i2 = 1;
        }
        int i3 = 1;
        while (true) {
            int i4 = i3 + 1;
            char cCharAt = str.charAt(i3);
            if (cCharAt == 'F') {
                i = i2 + 1;
                objArr[i2] = Opcodes.FLOAT;
            } else if (cCharAt != 'L') {
                if (cCharAt != 'S' && cCharAt != 'I') {
                    if (cCharAt == 'J') {
                        i = i2 + 1;
                        objArr[i2] = Opcodes.LONG;
                    } else if (cCharAt != 'Z') {
                        if (cCharAt != '[') {
                            switch (cCharAt) {
                                case 'B':
                                case 'C':
                                    break;
                                case 'D':
                                    i = i2 + 1;
                                    objArr[i2] = Opcodes.DOUBLE;
                                    break;
                                default:
                                    context.currentFrameLocalCount = i2;
                                    return;
                            }
                        } else {
                            while (str.charAt(i4) == '[') {
                                i4++;
                            }
                            if (str.charAt(i4) == 'L') {
                                do {
                                    i4++;
                                } while (str.charAt(i4) != ';');
                            }
                            int i5 = i4 + 1;
                            objArr[i2] = str.substring(i3, i5);
                            i3 = i5;
                            i2++;
                        }
                    }
                }
                i = i2 + 1;
                objArr[i2] = Opcodes.INTEGER;
            } else {
                int i6 = i4;
                while (str.charAt(i6) != ';') {
                    i6++;
                }
                objArr[i2] = str.substring(i4, i6);
                i2++;
                i3 = i6 + 1;
            }
            i2 = i;
            i3 = i4;
        }
    }

    private int readStackMapFrame(int i, boolean z, boolean z2, Context context) {
        int verificationTypeInfo;
        int i2;
        char[] cArr = context.charBuffer;
        Label[] labelArr = context.currentMethodLabels;
        if (z) {
            verificationTypeInfo = i + 1;
            i2 = this.classFileBuffer[i] & 255;
        } else {
            context.currentFrameOffset = -1;
            verificationTypeInfo = i;
            i2 = 255;
        }
        context.currentFrameLocalCountDelta = 0;
        if (i2 < 64) {
            context.currentFrameType = 3;
            context.currentFrameStackCount = 0;
        } else if (i2 < 128) {
            i2 -= 64;
            verificationTypeInfo = readVerificationTypeInfo(verificationTypeInfo, context.currentFrameStackTypes, 0, cArr, labelArr);
            context.currentFrameType = 4;
            context.currentFrameStackCount = 1;
        } else if (i2 >= 247) {
            int unsignedShort = readUnsignedShort(verificationTypeInfo);
            int i3 = verificationTypeInfo;
            verificationTypeInfo = i3 + 2;
            if (i2 == 247) {
                verificationTypeInfo = readVerificationTypeInfo(verificationTypeInfo, context.currentFrameStackTypes, 0, cArr, labelArr);
                context.currentFrameType = 4;
                context.currentFrameStackCount = 1;
            } else if (i2 >= 248 && i2 < 251) {
                context.currentFrameType = 2;
                int i4 = 251 - i2;
                context.currentFrameLocalCountDelta = i4;
                context.currentFrameLocalCount -= i4;
                context.currentFrameStackCount = 0;
            } else if (i2 == 251) {
                context.currentFrameType = 3;
                context.currentFrameStackCount = 0;
            } else if (i2 < 255) {
                int i5 = i2 - 251;
                int i6 = z2 ? context.currentFrameLocalCount : 0;
                int i7 = i5;
                while (i7 > 0) {
                    verificationTypeInfo = readVerificationTypeInfo(verificationTypeInfo, context.currentFrameLocalTypes, i6, cArr, labelArr);
                    i7--;
                    i6++;
                }
                context.currentFrameType = 1;
                context.currentFrameLocalCountDelta = i5;
                context.currentFrameLocalCount += i5;
                context.currentFrameStackCount = 0;
            } else {
                int unsignedShort2 = readUnsignedShort(verificationTypeInfo);
                int verificationTypeInfo2 = i3 + 4;
                context.currentFrameType = 0;
                context.currentFrameLocalCountDelta = unsignedShort2;
                context.currentFrameLocalCount = unsignedShort2;
                for (int i8 = 0; i8 < unsignedShort2; i8++) {
                    verificationTypeInfo2 = readVerificationTypeInfo(verificationTypeInfo2, context.currentFrameLocalTypes, i8, cArr, labelArr);
                }
                int unsignedShort3 = readUnsignedShort(verificationTypeInfo2);
                verificationTypeInfo = verificationTypeInfo2 + 2;
                context.currentFrameStackCount = unsignedShort3;
                for (int i9 = 0; i9 < unsignedShort3; i9++) {
                    verificationTypeInfo = readVerificationTypeInfo(verificationTypeInfo, context.currentFrameStackTypes, i9, cArr, labelArr);
                }
            }
            i2 = unsignedShort;
        } else {
            throw new IllegalArgumentException();
        }
        int i10 = context.currentFrameOffset + i2 + 1;
        context.currentFrameOffset = i10;
        createLabel(i10, labelArr);
        return verificationTypeInfo;
    }

    private int readVerificationTypeInfo(int i, Object[] objArr, int i2, char[] cArr, Label[] labelArr) {
        int i3 = i + 1;
        switch (this.classFileBuffer[i] & 255) {
            case 0:
                objArr[i2] = Opcodes.TOP;
                return i3;
            case 1:
                objArr[i2] = Opcodes.INTEGER;
                return i3;
            case 2:
                objArr[i2] = Opcodes.FLOAT;
                return i3;
            case 3:
                objArr[i2] = Opcodes.DOUBLE;
                return i3;
            case 4:
                objArr[i2] = Opcodes.LONG;
                return i3;
            case 5:
                objArr[i2] = Opcodes.NULL;
                return i3;
            case 6:
                objArr[i2] = Opcodes.UNINITIALIZED_THIS;
                return i3;
            case 7:
                objArr[i2] = readClass(i3, cArr);
                break;
            case 8:
                objArr[i2] = createLabel(readUnsignedShort(i3), labelArr);
                break;
            default:
                throw new IllegalArgumentException();
        }
        return i + 3;
    }

    final int getFirstAttributeOffset() {
        int i = this.header;
        int unsignedShort = i + 8 + (readUnsignedShort(i + 6) * 2);
        int unsignedShort2 = readUnsignedShort(unsignedShort);
        int i2 = unsignedShort + 2;
        while (true) {
            int i3 = unsignedShort2 - 1;
            if (unsignedShort2 <= 0) {
                break;
            }
            int unsignedShort3 = readUnsignedShort(i2 + 6);
            i2 += 8;
            while (true) {
                int i4 = unsignedShort3 - 1;
                if (unsignedShort3 > 0) {
                    i2 += readInt(i2 + 2) + 6;
                    unsignedShort3 = i4;
                }
            }
            unsignedShort2 = i3;
        }
        int unsignedShort4 = readUnsignedShort(i2);
        int i5 = i2 + 2;
        while (true) {
            int i6 = unsignedShort4 - 1;
            if (unsignedShort4 <= 0) {
                return i5 + 2;
            }
            int unsignedShort5 = readUnsignedShort(i5 + 6);
            i5 += 8;
            while (true) {
                int i7 = unsignedShort5 - 1;
                if (unsignedShort5 > 0) {
                    i5 += readInt(i5 + 2) + 6;
                    unsignedShort5 = i7;
                }
            }
            unsignedShort4 = i6;
        }
    }

    private int[] readBootstrapMethodsAttribute(int i) {
        char[] cArr = new char[i];
        int firstAttributeOffset = getFirstAttributeOffset();
        for (int unsignedShort = readUnsignedShort(firstAttributeOffset - 2); unsignedShort > 0; unsignedShort--) {
            String utf8 = readUTF8(firstAttributeOffset, cArr);
            int i2 = readInt(firstAttributeOffset + 2);
            int i3 = firstAttributeOffset + 6;
            if (AttBootstrapMethods.ATTRIBUTE_NAME.equals(utf8)) {
                int unsignedShort2 = readUnsignedShort(i3);
                int[] iArr = new int[unsignedShort2];
                int unsignedShort3 = firstAttributeOffset + 8;
                for (int i4 = 0; i4 < unsignedShort2; i4++) {
                    iArr[i4] = unsignedShort3;
                    unsignedShort3 += (readUnsignedShort(unsignedShort3 + 2) * 2) + 4;
                }
                return iArr;
            }
            firstAttributeOffset = i3 + i2;
        }
        throw new IllegalArgumentException();
    }

    private Attribute readAttribute(Attribute[] attributeArr, String str, int i, int i2, char[] cArr, int i3, Label[] labelArr) {
        for (Attribute attribute : attributeArr) {
            if (attribute.type.equals(str)) {
                return attribute.read(this, i, i2, cArr, i3, labelArr);
            }
        }
        return new Attribute(str).read(this, i, i2, null, -1, null);
    }

    public int getItemCount() {
        return this.cpInfoOffsets.length;
    }

    public int getItem(int i) {
        return this.cpInfoOffsets[i];
    }

    public int getMaxStringLength() {
        return this.maxStringLength;
    }

    public int readByte(int i) {
        return this.classFileBuffer[i] & 255;
    }

    public int readUnsignedShort(int i) {
        byte[] bArr = this.classFileBuffer;
        return (bArr[i + 1] & 255) | ((bArr[i] & 255) << 8);
    }

    public short readShort(int i) {
        byte[] bArr = this.classFileBuffer;
        return (short) ((bArr[i + 1] & 255) | ((bArr[i] & 255) << 8));
    }

    public int readInt(int i) {
        byte[] bArr = this.classFileBuffer;
        return (bArr[i + 3] & 255) | ((bArr[i] & 255) << 24) | ((bArr[i + 1] & 255) << 16) | ((bArr[i + 2] & 255) << 8);
    }

    public long readLong(int i) {
        return (((long) readInt(i)) << 32) | (((long) readInt(i + 4)) & 4294967295L);
    }

    public String readUTF8(int i, char[] cArr) {
        int unsignedShort = readUnsignedShort(i);
        if (i == 0 || unsignedShort == 0) {
            return null;
        }
        return readUtf(unsignedShort, cArr);
    }

    final String readUtf(int i, char[] cArr) {
        String[] strArr = this.constantUtf8Values;
        String str = strArr[i];
        if (str != null) {
            return str;
        }
        int i2 = this.cpInfoOffsets[i];
        String utf = readUtf(i2 + 2, readUnsignedShort(i2), cArr);
        strArr[i] = utf;
        return utf;
    }

    private String readUtf(int i, int i2, char[] cArr) {
        int i3;
        int i4 = i2 + i;
        byte[] bArr = this.classFileBuffer;
        int i5 = 0;
        while (i < i4) {
            int i6 = i + 1;
            byte b = bArr[i];
            if ((b & 128) == 0) {
                cArr[i5] = (char) (b & 127);
                i5++;
                i = i6;
            } else {
                if ((b & 224) == 192) {
                    i3 = i5 + 1;
                    i += 2;
                    cArr[i5] = (char) (((b & 31) << 6) + (bArr[i6] & 63));
                } else {
                    i3 = i5 + 1;
                    int i7 = i + 2;
                    i += 3;
                    cArr[i5] = (char) (((b & 15) << 12) + ((bArr[i6] & 63) << 6) + (bArr[i7] & 63));
                }
                i5 = i3;
            }
        }
        return new String(cArr, 0, i5);
    }

    private String readStringish(int i, char[] cArr) {
        return readUTF8(this.cpInfoOffsets[readUnsignedShort(i)], cArr);
    }

    public String readClass(int i, char[] cArr) {
        return readStringish(i, cArr);
    }

    public String readModule(int i, char[] cArr) {
        return readStringish(i, cArr);
    }

    public String readPackage(int i, char[] cArr) {
        return readStringish(i, cArr);
    }

    private ConstantDynamic readConstantDynamic(int i, char[] cArr) {
        ConstantDynamic constantDynamic = this.constantDynamicValues[i];
        if (constantDynamic != null) {
            return constantDynamic;
        }
        int[] iArr = this.cpInfoOffsets;
        int i2 = iArr[i];
        int i3 = iArr[readUnsignedShort(i2 + 2)];
        String utf8 = readUTF8(i3, cArr);
        String utf9 = readUTF8(i3 + 2, cArr);
        int i4 = this.bootstrapMethodOffsets[readUnsignedShort(i2)];
        Handle handle = (Handle) readConst(readUnsignedShort(i4), cArr);
        int unsignedShort = readUnsignedShort(i4 + 2);
        Object[] objArr = new Object[unsignedShort];
        int i5 = i4 + 4;
        for (int i6 = 0; i6 < unsignedShort; i6++) {
            objArr[i6] = readConst(readUnsignedShort(i5), cArr);
            i5 += 2;
        }
        ConstantDynamic[] constantDynamicArr = this.constantDynamicValues;
        ConstantDynamic constantDynamic2 = new ConstantDynamic(utf8, utf9, handle, objArr);
        constantDynamicArr[i] = constantDynamic2;
        return constantDynamic2;
    }

    public Object readConst(int i, char[] cArr) {
        int i2 = this.cpInfoOffsets[i];
        byte b = this.classFileBuffer[i2 - 1];
        switch (b) {
            case 3:
                return Integer.valueOf(readInt(i2));
            case 4:
                return Float.valueOf(Float.intBitsToFloat(readInt(i2)));
            case 5:
                return Long.valueOf(readLong(i2));
            case 6:
                return Double.valueOf(Double.longBitsToDouble(readLong(i2)));
            case 7:
                return Type.getObjectType(readUTF8(i2, cArr));
            case 8:
                return readUTF8(i2, cArr);
            default:
                switch (b) {
                    case 15:
                        int i3 = readByte(i2);
                        int i4 = this.cpInfoOffsets[readUnsignedShort(i2 + 1)];
                        int i5 = this.cpInfoOffsets[readUnsignedShort(i4 + 2)];
                        return new Handle(i3, readClass(i4, cArr), readUTF8(i5, cArr), readUTF8(i5 + 2, cArr), this.classFileBuffer[i4 - 1] == 11);
                    case 16:
                        return Type.getMethodType(readUTF8(i2, cArr));
                    case 17:
                        return readConstantDynamic(i, cArr);
                    default:
                        throw new IllegalArgumentException();
                }
        }
    }
}
