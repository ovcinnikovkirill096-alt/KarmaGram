package com.android.dx.cf.code;

import com.android.dx.cf.iface.Method;
import com.android.dx.cf.iface.MethodList;
import com.android.dx.rop.code.AccessFlags;
import com.android.dx.rop.code.FillArrayDataInsn;
import com.android.dx.rop.code.Insn;
import com.android.dx.rop.code.InvokePolymorphicInsn;
import com.android.dx.rop.code.PlainCstInsn;
import com.android.dx.rop.code.PlainInsn;
import com.android.dx.rop.code.RegisterSpec;
import com.android.dx.rop.code.RegisterSpecList;
import com.android.dx.rop.code.Rop;
import com.android.dx.rop.code.Rops;
import com.android.dx.rop.code.SourcePosition;
import com.android.dx.rop.code.SwitchInsn;
import com.android.dx.rop.code.ThrowingCstInsn;
import com.android.dx.rop.code.ThrowingInsn;
import com.android.dx.rop.code.TranslationAdvice;
import com.android.dx.rop.cst.Constant;
import com.android.dx.rop.cst.CstCallSiteRef;
import com.android.dx.rop.cst.CstFieldRef;
import com.android.dx.rop.cst.CstInteger;
import com.android.dx.rop.cst.CstMethodRef;
import com.android.dx.rop.cst.CstNat;
import com.android.dx.rop.cst.CstString;
import com.android.dx.rop.cst.CstType;
import com.android.dx.rop.type.Type;
import com.android.dx.rop.type.TypeBearer;
import com.android.dx.rop.type.TypeList;
import com.android.dx.util.IntList;
import java.util.ArrayList;

final class RopperMachine extends ValueAwareMachine {
    private static final CstType ARRAY_REFLECT_TYPE;
    private static final CstMethodRef MULTIANEWARRAY_METHOD;
    private final TranslationAdvice advice;
    private boolean blockCanThrow;
    private TypeList catches;
    private boolean catchesUsed;
    private int extraBlockCount;
    private boolean hasJsr;
    private final ArrayList<Insn> insns;
    private final int maxLocals;
    private final ConcreteMethod method;
    private final MethodList methods;
    private int primarySuccessorIndex;
    private ReturnAddress returnAddress;
    private Rop returnOp;
    private SourcePosition returnPosition;
    private boolean returns;
    private final Ropper ropper;

    static {
        CstType cstType = new CstType(Type.internClassName("java/lang/reflect/Array"));
        ARRAY_REFLECT_TYPE = cstType;
        MULTIANEWARRAY_METHOD = new CstMethodRef(cstType, new CstNat(new CstString("newInstance"), new CstString("(Ljava/lang/Class;[I)Ljava/lang/Object;")));
    }

    public RopperMachine(Ropper ropper, ConcreteMethod concreteMethod, TranslationAdvice translationAdvice, MethodList methodList) {
        super(concreteMethod.getEffectiveDescriptor());
        if (methodList == null) {
            throw new NullPointerException("methods == null");
        }
        if (ropper == null) {
            throw new NullPointerException("ropper == null");
        }
        if (translationAdvice == null) {
            throw new NullPointerException("advice == null");
        }
        this.ropper = ropper;
        this.method = concreteMethod;
        this.methods = methodList;
        this.advice = translationAdvice;
        this.maxLocals = concreteMethod.getMaxLocals();
        this.insns = new ArrayList<>(25);
        this.catches = null;
        this.catchesUsed = false;
        this.returns = false;
        this.primarySuccessorIndex = -1;
        this.extraBlockCount = 0;
        this.blockCanThrow = false;
        this.returnOp = null;
        this.returnPosition = null;
    }

    public ArrayList<Insn> getInsns() {
        return this.insns;
    }

    public Rop getReturnOp() {
        return this.returnOp;
    }

    public SourcePosition getReturnPosition() {
        return this.returnPosition;
    }

    public void startBlock(TypeList typeList) {
        this.catches = typeList;
        this.insns.clear();
        this.catchesUsed = false;
        this.returns = false;
        this.primarySuccessorIndex = 0;
        this.extraBlockCount = 0;
        this.blockCanThrow = false;
        this.hasJsr = false;
        this.returnAddress = null;
    }

    public boolean wereCatchesUsed() {
        return this.catchesUsed;
    }

    public boolean returns() {
        return this.returns;
    }

    public int getPrimarySuccessorIndex() {
        return this.primarySuccessorIndex;
    }

    public int getExtraBlockCount() {
        return this.extraBlockCount;
    }

    public boolean canThrow() {
        return this.blockCanThrow;
    }

    public boolean hasJsr() {
        return this.hasJsr;
    }

    public boolean hasRet() {
        return this.returnAddress != null;
    }

    public ReturnAddress getReturnAddress() {
        return this.returnAddress;
    }

    /* JADX WARN: Code duplicated, block: B:100:0x02fa  */
    /* JADX WARN: Code duplicated, block: B:101:0x0303  */
    /* JADX WARN: Code duplicated, block: B:103:0x0308  */
    /* JADX WARN: Code duplicated, block: B:105:0x0315  */
    /* JADX WARN: Code duplicated, block: B:106:0x0319  */
    /* JADX WARN: Code duplicated, block: B:108:0x0323  */
    /* JADX WARN: Code duplicated, block: B:111:0x032f  */
    /* JADX WARN: Code duplicated, block: B:113:0x0336  */
    /* JADX WARN: Code duplicated, block: B:128:? A[RETURN, SYNTHETIC] */
    /* JADX WARN: Code duplicated, block: B:79:0x0260  */
    /* JADX WARN: Code duplicated, block: B:81:0x0266  */
    /* JADX WARN: Code duplicated, block: B:83:0x0278  */
    /* JADX WARN: Code duplicated, block: B:85:0x028d  */
    /* JADX WARN: Code duplicated, block: B:87:0x0291  */
    /* JADX WARN: Code duplicated, block: B:89:0x0298  */
    /* JADX WARN: Code duplicated, block: B:91:0x02a6  */
    /* JADX WARN: Code duplicated, block: B:93:0x02ca A[DONT_INVERT] */
    /* JADX WARN: Code duplicated, block: B:94:0x02cc A[DONT_INVERT] */
    /* JADX WARN: Code duplicated, block: B:95:0x02ce  */
    /* JADX WARN: Code duplicated, block: B:97:0x02d6  */
    /* JADX WARN: Code duplicated, block: B:98:0x02e3  */
    /* JADX WARN: Multi-variable type inference failed */
    /* JADX WARN: Type inference failed for: r0v27, types: [int] */
    /* JADX WARN: Type inference failed for: r14v8, types: [com.android.dx.rop.type.TypeBearer] */
    /* JADX WARN: Type inference failed for: r2v19 */
    /* JADX WARN: Type inference failed for: r2v20 */
    /* JADX WARN: Type inference failed for: r4v26 */
    /* JADX WARN: Type inference failed for: r4v27 */
    /* JADX WARN: Type inference failed for: r8v17, types: [com.android.dx.rop.code.Insn, java.lang.Object] */
    /* JADX WARN: Type inference failed for: r8v18 */
    /* JADX WARN: Type inference failed for: r8v23 */
    /* JADX WARN: Type inference failed for: r8v24 */
    /* JADX WARN: Type inference failed for: r9v13, types: [int] */
    /* JADX WARN: Type inference failed for: r9v14 */
    /* JADX WARN: Type inference failed for: r9v15 */
    /* JADX WARN: Type inference failed for: r9v16 */
    /* JADX WARN: Type inference failed for: r9v3, types: [com.android.dx.rop.type.TypeBearer] */
    @Override // com.android.dx.cf.code.ValueAwareMachine, com.android.dx.cf.code.Machine
    public void run(Frame frame, int i, int i2) {
        RegisterSpec registerSpec;
        RegisterSpecList registerSpecListWithoutFirst;
        int i3;
        boolean z;
        TypeBearer typeBearer;
        RegisterSpec registerSpec2;
        Object plainInsn;
        RegisterSpecList registerSpecList;
        Constant constantIntern;
        SwitchList auxCases;
        ArrayList<Constant> initValues;
        boolean zCanThrow;
        Object obj;
        boolean z2;
        Constant constant;
        Insn plainInsn2;
        Insn plainCstInsn;
        RegisterSpec registerSpec3;
        TypeBearer typeBearer2;
        ?? r9;
        ?? r8;
        Type returnType;
        ThrowingCstInsn throwingCstInsn;
        boolean z3;
        int i4 = i2;
        int size = this.maxLocals + frame.getStack().size();
        RegisterSpecList sources = getSources(i4, size);
        int size2 = sources.size();
        super.run(frame, i, i2);
        SourcePosition sourcePositionMakeSourcePosistion = this.method.makeSourcePosistion(i);
        RegisterSpec localTarget = getLocalTarget(i4 == 54);
        int iResultCount = resultCount();
        RegisterSpec registerSpec4 = null;
        if (iResultCount != 0) {
            if (localTarget == null) {
                if (iResultCount == 1) {
                    localTarget = RegisterSpec.make(size, result(0));
                } else {
                    int firstTempStackReg = this.ropper.getFirstTempStackReg();
                    RegisterSpec[] registerSpecArr = new RegisterSpec[size2];
                    for (int i5 = 0; i5 < size2; i5++) {
                        RegisterSpec registerSpec5 = sources.get(i5);
                        TypeBearer typeBearer3 = registerSpec5.getTypeBearer();
                        RegisterSpec registerSpecWithReg = registerSpec5.withReg(firstTempStackReg);
                        this.insns.add(new PlainInsn(Rops.opMove(typeBearer3), sourcePositionMakeSourcePosistion, registerSpecWithReg, registerSpec5));
                        registerSpecArr[i5] = registerSpecWithReg;
                        firstTempStackReg += registerSpec5.getCategory();
                    }
                    for (int auxInt = getAuxInt(); auxInt != 0; auxInt >>= 4) {
                        RegisterSpec registerSpec6 = registerSpecArr[(auxInt & 15) - 1];
                        TypeBearer typeBearer4 = registerSpec6.getTypeBearer();
                        this.insns.add(new PlainInsn(Rops.opMove(typeBearer4), sourcePositionMakeSourcePosistion, registerSpec6.withReg(size), registerSpec6));
                        size += typeBearer4.getType().getCategory();
                    }
                    return;
                }
            }
            registerSpec = localTarget;
        } else if (i4 == 87 || i4 == 88) {
            return;
        } else {
            registerSpec = null;
        }
        TypeBearer typeBearer5 = registerSpec != null ? registerSpec : Type.VOID;
        Constant auxCst = getAuxCst();
        if (i4 == 197) {
            this.blockCanThrow = true;
            this.extraBlockCount = 6;
            int nextReg = registerSpec.getNextReg();
            Type type = Type.INT_ARRAY;
            RegisterSpec registerSpecMake = RegisterSpec.make(nextReg, type);
            this.insns.add(new ThrowingCstInsn(Rops.opFilledNewArray(type, size2), sourcePositionMakeSourcePosistion, sources, this.catches, CstType.INT_ARRAY));
            this.insns.add(new PlainInsn(Rops.opMoveResult(type), sourcePositionMakeSourcePosistion, registerSpecMake, RegisterSpecList.EMPTY));
            Type classType = ((CstType) auxCst).getClassType();
            for (int i6 = 0; i6 < size2; i6++) {
                classType = classType.getComponentType();
            }
            RegisterSpec registerSpecMake2 = RegisterSpec.make(registerSpec.getReg(), Type.CLASS);
            if (classType.isPrimitive()) {
                CstFieldRef cstFieldRefForPrimitiveType = CstFieldRef.forPrimitiveType(classType);
                sourcePositionMakeSourcePosistion = sourcePositionMakeSourcePosistion;
                i3 = 0;
                z3 = true;
                typeBearer = typeBearer5;
                throwingCstInsn = new ThrowingCstInsn(Rops.GET_STATIC_OBJECT, sourcePositionMakeSourcePosistion, RegisterSpecList.EMPTY, this.catches, cstFieldRefForPrimitiveType);
            } else {
                sourcePositionMakeSourcePosistion = sourcePositionMakeSourcePosistion;
                i3 = 0;
                typeBearer = typeBearer5;
                Rop rop = Rops.CONST_OBJECT;
                RegisterSpecList registerSpecList2 = RegisterSpecList.EMPTY;
                TypeList typeList = this.catches;
                CstType cstType = new CstType(classType);
                z3 = true;
                throwingCstInsn = new ThrowingCstInsn(rop, sourcePositionMakeSourcePosistion, registerSpecList2, typeList, cstType);
            }
            this.insns.add(throwingCstInsn);
            Rop ropOpMoveResultPseudo = Rops.opMoveResultPseudo(registerSpecMake2.getType());
            RegisterSpecList registerSpecList3 = RegisterSpecList.EMPTY;
            this.insns.add(new PlainInsn(ropOpMoveResultPseudo, sourcePositionMakeSourcePosistion, registerSpecMake2, registerSpecList3));
            RegisterSpec registerSpecMake3 = RegisterSpec.make(registerSpec.getReg(), Type.OBJECT);
            CstMethodRef cstMethodRef = MULTIANEWARRAY_METHOD;
            this.insns.add(new ThrowingCstInsn(Rops.opInvokeStatic(cstMethodRef.getPrototype()), sourcePositionMakeSourcePosistion, RegisterSpecList.make(registerSpecMake2, registerSpecMake), this.catches, cstMethodRef));
            this.insns.add(new PlainInsn(Rops.opMoveResult(cstMethodRef.getPrototype().getReturnType()), sourcePositionMakeSourcePosistion, registerSpecMake3, registerSpecList3));
            registerSpecListWithoutFirst = RegisterSpecList.make(registerSpecMake3);
            i4 = 192;
            z = z3;
        } else {
            registerSpecListWithoutFirst = sources;
            i3 = 0;
            z = true;
            registerSpec4 = null;
            typeBearer = typeBearer5;
            auxCst = auxCst;
            if (i4 == 168) {
                this.hasJsr = true;
                return;
            } else if (i4 == 169) {
                try {
                    this.returnAddress = (ReturnAddress) arg(0);
                    return;
                } catch (ClassCastException e) {
                    throw new RuntimeException("Argument to RET was not a ReturnAddress", e);
                }
            }
        }
        int iJopToRopOpcode = jopToRopOpcode(i4, auxCst);
        Rop ropRopFor = Rops.ropFor(iJopToRopOpcode, typeBearer, registerSpecListWithoutFirst, auxCst);
        if (registerSpec != null && ropRopFor.isCallLike()) {
            this.extraBlockCount += z ? 1 : 0;
            if (ropRopFor.getOpcode() == 59) {
                returnType = ((CstCallSiteRef) auxCst).getReturnType();
            } else {
                returnType = ((CstMethodRef) auxCst).getPrototype().getReturnType();
            }
            PlainInsn plainInsn3 = new PlainInsn(Rops.opMoveResult(returnType), sourcePositionMakeSourcePosistion, registerSpec, RegisterSpecList.EMPTY);
            registerSpec2 = registerSpec4;
            plainInsn = plainInsn3;
        } else if (registerSpec == null || !ropRopFor.canThrow()) {
            registerSpec2 = registerSpec;
            plainInsn = registerSpec4;
        } else {
            this.extraBlockCount += z ? 1 : 0;
            plainInsn = new PlainInsn(Rops.opMoveResultPseudo(registerSpec.getTypeBearer()), sourcePositionMakeSourcePosistion, registerSpec, RegisterSpecList.EMPTY);
            registerSpec2 = registerSpec4;
        }
        if (iJopToRopOpcode == 41) {
            constantIntern = CstType.intern(ropRopFor.getResult());
        } else {
            if (auxCst == null && size2 == 2) {
                ?? typeBearer6 = registerSpecListWithoutFirst.get(i3).getTypeBearer();
                ?? typeBearer7 = registerSpecListWithoutFirst.get(z ? 1 : 0).getTypeBearer();
                if ((typeBearer7.isConstant() || typeBearer6.isConstant()) && this.advice.hasConstantOperation(ropRopFor, registerSpecListWithoutFirst.get(i3), registerSpecListWithoutFirst.get(z ? 1 : 0))) {
                    if (typeBearer7.isConstant()) {
                        Constant constantMake = (Constant) typeBearer7;
                        registerSpecListWithoutFirst = registerSpecListWithoutFirst.withoutLast();
                        if (ropRopFor.getOpcode() == 15) {
                            constantMake = CstInteger.make(-((CstInteger) typeBearer7).getValue());
                            iJopToRopOpcode = 14;
                        }
                        constantIntern = constantMake;
                    } else {
                        registerSpecListWithoutFirst = registerSpecListWithoutFirst.withoutFirst();
                        constantIntern = (Constant) typeBearer6;
                    }
                    ropRopFor = Rops.ropFor(iJopToRopOpcode, typeBearer, registerSpecListWithoutFirst, constantIntern);
                }
                auxCases = getAuxCases();
                initValues = getInitValues();
                zCanThrow = ropRopFor.canThrow();
                this.blockCanThrow |= zCanThrow;
                if (auxCases != null) {
                    if (auxCases.size() == 0) {
                        plainInsn2 = new PlainInsn(Rops.GOTO, sourcePositionMakeSourcePosistion, (RegisterSpec) null, RegisterSpecList.EMPTY);
                        this.primarySuccessorIndex = 0;
                        r8 = plainInsn;
                        r9 = z ? 1 : 0;
                        constant = auxCst;
                    } else {
                        IntList values = auxCases.getValues();
                        plainCstInsn = new SwitchInsn(ropRopFor, sourcePositionMakeSourcePosistion, registerSpec2, registerSpecList, values);
                        this.primarySuccessorIndex = values.size();
                        constant = auxCst;
                        plainInsn2 = plainCstInsn;
                        r8 = plainInsn;
                        r9 = z;
                    }
                } else if (iJopToRopOpcode == 33) {
                    if (registerSpecList.size() != 0) {
                        registerSpec3 = registerSpecList.get(0);
                        typeBearer2 = registerSpec3.getTypeBearer();
                        if (registerSpec3.getReg() != 0) {
                            this.insns.add(new PlainInsn(Rops.opMove(typeBearer2), sourcePositionMakeSourcePosistion, RegisterSpec.make(0, typeBearer2), registerSpec3));
                        }
                    }
                    plainInsn2 = new PlainInsn(Rops.GOTO, sourcePositionMakeSourcePosistion, (RegisterSpec) null, RegisterSpecList.EMPTY);
                    this.primarySuccessorIndex = 0;
                    updateReturnOp(ropRopFor, sourcePositionMakeSourcePosistion);
                    this.returns = z;
                    r8 = plainInsn;
                    r9 = z ? 1 : 0;
                    constant = auxCst;
                } else if (auxCst == null) {
                    obj = plainInsn;
                    z2 = z ? 1 : 0;
                    constant = auxCst;
                    if (zCanThrow) {
                        ThrowingInsn throwingInsn = new ThrowingInsn(ropRopFor, sourcePositionMakeSourcePosistion, registerSpecList, this.catches);
                        this.catchesUsed = z2;
                        if (i4 == 191) {
                            this.primarySuccessorIndex = -1;
                        } else {
                            this.primarySuccessorIndex = this.catches.size();
                        }
                        plainInsn2 = throwingInsn;
                        r8 = obj;
                        r9 = z2;
                    } else {
                        plainInsn2 = new PlainInsn(ropRopFor, sourcePositionMakeSourcePosistion, registerSpec2, registerSpecList);
                        r8 = obj;
                        r9 = z2;
                    }
                } else if (zCanThrow) {
                    if (ropRopFor.getOpcode() == 58) {
                        plainInsn2 = makeInvokePolymorphicInsn(ropRopFor, sourcePositionMakeSourcePosistion, registerSpecList, this.catches, auxCst);
                        constant = auxCst;
                    } else {
                        constant = auxCst;
                        plainInsn2 = new ThrowingCstInsn(ropRopFor, sourcePositionMakeSourcePosistion, registerSpecList, this.catches, constant);
                    }
                    this.catchesUsed = z;
                    this.primarySuccessorIndex = this.catches.size();
                    r8 = plainInsn;
                    r9 = z;
                } else {
                    constant = auxCst;
                    plainCstInsn = new PlainCstInsn(ropRopFor, sourcePositionMakeSourcePosistion, registerSpec2, registerSpecList, constant);
                    plainInsn2 = plainCstInsn;
                    r8 = plainInsn;
                    r9 = z;
                }
                this.insns.add(plainInsn2);
                if (r8 != 0) {
                    this.insns.add((Insn) r8);
                }
                if (initValues != null) {
                    this.extraBlockCount += r9;
                    this.insns.add(new FillArrayDataInsn(Rops.FILL_ARRAY_DATA, sourcePositionMakeSourcePosistion, RegisterSpecList.make(r8.getResult()), initValues, constant));
                }
            }
            registerSpecList = registerSpecListWithoutFirst;
            auxCases = getAuxCases();
            initValues = getInitValues();
            zCanThrow = ropRopFor.canThrow();
            this.blockCanThrow |= zCanThrow;
            if (auxCases != null) {
                if (auxCases.size() == 0) {
                    plainInsn2 = new PlainInsn(Rops.GOTO, sourcePositionMakeSourcePosistion, (RegisterSpec) null, RegisterSpecList.EMPTY);
                    this.primarySuccessorIndex = 0;
                    r8 = plainInsn;
                    r9 = z ? 1 : 0;
                    constant = auxCst;
                } else {
                    IntList values2 = auxCases.getValues();
                    plainCstInsn = new SwitchInsn(ropRopFor, sourcePositionMakeSourcePosistion, registerSpec2, registerSpecList, values2);
                    this.primarySuccessorIndex = values2.size();
                    constant = auxCst;
                    plainInsn2 = plainCstInsn;
                    r8 = plainInsn;
                    r9 = z;
                }
            } else if (iJopToRopOpcode == 33) {
                if (registerSpecList.size() != 0) {
                    registerSpec3 = registerSpecList.get(0);
                    typeBearer2 = registerSpec3.getTypeBearer();
                    if (registerSpec3.getReg() != 0) {
                        this.insns.add(new PlainInsn(Rops.opMove(typeBearer2), sourcePositionMakeSourcePosistion, RegisterSpec.make(0, typeBearer2), registerSpec3));
                    }
                }
                plainInsn2 = new PlainInsn(Rops.GOTO, sourcePositionMakeSourcePosistion, (RegisterSpec) null, RegisterSpecList.EMPTY);
                this.primarySuccessorIndex = 0;
                updateReturnOp(ropRopFor, sourcePositionMakeSourcePosistion);
                this.returns = z;
                r8 = plainInsn;
                r9 = z ? 1 : 0;
                constant = auxCst;
            } else if (auxCst == null) {
                obj = plainInsn;
                z2 = z ? 1 : 0;
                constant = auxCst;
                if (zCanThrow) {
                    ThrowingInsn throwingInsn2 = new ThrowingInsn(ropRopFor, sourcePositionMakeSourcePosistion, registerSpecList, this.catches);
                    this.catchesUsed = z2;
                    if (i4 == 191) {
                        this.primarySuccessorIndex = -1;
                    } else {
                        this.primarySuccessorIndex = this.catches.size();
                    }
                    plainInsn2 = throwingInsn2;
                    r8 = obj;
                    r9 = z2;
                } else {
                    plainInsn2 = new PlainInsn(ropRopFor, sourcePositionMakeSourcePosistion, registerSpec2, registerSpecList);
                    r8 = obj;
                    r9 = z2;
                }
            } else if (zCanThrow) {
                if (ropRopFor.getOpcode() == 58) {
                    plainInsn2 = makeInvokePolymorphicInsn(ropRopFor, sourcePositionMakeSourcePosistion, registerSpecList, this.catches, auxCst);
                    constant = auxCst;
                } else {
                    constant = auxCst;
                    plainInsn2 = new ThrowingCstInsn(ropRopFor, sourcePositionMakeSourcePosistion, registerSpecList, this.catches, constant);
                }
                this.catchesUsed = z;
                this.primarySuccessorIndex = this.catches.size();
                r8 = plainInsn;
                r9 = z;
            } else {
                constant = auxCst;
                plainCstInsn = new PlainCstInsn(ropRopFor, sourcePositionMakeSourcePosistion, registerSpec2, registerSpecList, constant);
                plainInsn2 = plainCstInsn;
                r8 = plainInsn;
                r9 = z;
            }
            this.insns.add(plainInsn2);
            if (r8 != 0) {
                this.insns.add((Insn) r8);
            }
            if (initValues != null) {
                this.extraBlockCount += r9;
                this.insns.add(new FillArrayDataInsn(Rops.FILL_ARRAY_DATA, sourcePositionMakeSourcePosistion, RegisterSpecList.make(r8.getResult()), initValues, constant));
            }
        }
        registerSpecList = registerSpecListWithoutFirst;
        auxCst = constantIntern;
        auxCases = getAuxCases();
        initValues = getInitValues();
        zCanThrow = ropRopFor.canThrow();
        this.blockCanThrow |= zCanThrow;
        if (auxCases != null) {
            if (auxCases.size() == 0) {
                plainInsn2 = new PlainInsn(Rops.GOTO, sourcePositionMakeSourcePosistion, (RegisterSpec) null, RegisterSpecList.EMPTY);
                this.primarySuccessorIndex = 0;
                r8 = plainInsn;
                r9 = z ? 1 : 0;
                constant = auxCst;
            } else {
                IntList values3 = auxCases.getValues();
                plainCstInsn = new SwitchInsn(ropRopFor, sourcePositionMakeSourcePosistion, registerSpec2, registerSpecList, values3);
                this.primarySuccessorIndex = values3.size();
                constant = auxCst;
                plainInsn2 = plainCstInsn;
                r8 = plainInsn;
                r9 = z;
            }
        } else if (iJopToRopOpcode == 33) {
            if (registerSpecList.size() != 0) {
                registerSpec3 = registerSpecList.get(0);
                typeBearer2 = registerSpec3.getTypeBearer();
                if (registerSpec3.getReg() != 0) {
                    this.insns.add(new PlainInsn(Rops.opMove(typeBearer2), sourcePositionMakeSourcePosistion, RegisterSpec.make(0, typeBearer2), registerSpec3));
                }
            }
            plainInsn2 = new PlainInsn(Rops.GOTO, sourcePositionMakeSourcePosistion, (RegisterSpec) null, RegisterSpecList.EMPTY);
            this.primarySuccessorIndex = 0;
            updateReturnOp(ropRopFor, sourcePositionMakeSourcePosistion);
            this.returns = z;
            r8 = plainInsn;
            r9 = z ? 1 : 0;
            constant = auxCst;
        } else if (auxCst == null) {
            obj = plainInsn;
            z2 = z ? 1 : 0;
            constant = auxCst;
            if (zCanThrow) {
                ThrowingInsn throwingInsn3 = new ThrowingInsn(ropRopFor, sourcePositionMakeSourcePosistion, registerSpecList, this.catches);
                this.catchesUsed = z2;
                if (i4 == 191) {
                    this.primarySuccessorIndex = -1;
                } else {
                    this.primarySuccessorIndex = this.catches.size();
                }
                plainInsn2 = throwingInsn3;
                r8 = obj;
                r9 = z2;
            } else {
                plainInsn2 = new PlainInsn(ropRopFor, sourcePositionMakeSourcePosistion, registerSpec2, registerSpecList);
                r8 = obj;
                r9 = z2;
            }
        } else if (zCanThrow) {
            if (ropRopFor.getOpcode() == 58) {
                plainInsn2 = makeInvokePolymorphicInsn(ropRopFor, sourcePositionMakeSourcePosistion, registerSpecList, this.catches, auxCst);
                constant = auxCst;
            } else {
                constant = auxCst;
                plainInsn2 = new ThrowingCstInsn(ropRopFor, sourcePositionMakeSourcePosistion, registerSpecList, this.catches, constant);
            }
            this.catchesUsed = z;
            this.primarySuccessorIndex = this.catches.size();
            r8 = plainInsn;
            r9 = z;
        } else {
            constant = auxCst;
            plainCstInsn = new PlainCstInsn(ropRopFor, sourcePositionMakeSourcePosistion, registerSpec2, registerSpecList, constant);
            plainInsn2 = plainCstInsn;
            r8 = plainInsn;
            r9 = z;
        }
        this.insns.add(plainInsn2);
        if (r8 != 0) {
            this.insns.add((Insn) r8);
        }
        if (initValues != null) {
            this.extraBlockCount += r9;
            this.insns.add(new FillArrayDataInsn(Rops.FILL_ARRAY_DATA, sourcePositionMakeSourcePosistion, RegisterSpecList.make(r8.getResult()), initValues, constant));
        }
    }

    private RegisterSpecList getSources(int i, int i2) {
        RegisterSpecList registerSpecList;
        int iArgCount = argCount();
        if (iArgCount == 0) {
            return RegisterSpecList.EMPTY;
        }
        int localIndex = getLocalIndex();
        if (localIndex >= 0) {
            registerSpecList = new RegisterSpecList(1);
            registerSpecList.set(0, RegisterSpec.make(localIndex, arg(0)));
        } else {
            RegisterSpecList registerSpecList2 = new RegisterSpecList(iArgCount);
            for (int i3 = 0; i3 < iArgCount; i3++) {
                RegisterSpec registerSpecMake = RegisterSpec.make(i2, arg(i3));
                registerSpecList2.set(i3, registerSpecMake);
                i2 += registerSpecMake.getCategory();
            }
            if (i == 79) {
                if (iArgCount != 3) {
                    throw new RuntimeException("shouldn't happen");
                }
                RegisterSpec registerSpec = registerSpecList2.get(0);
                RegisterSpec registerSpec2 = registerSpecList2.get(1);
                registerSpecList2.set(0, registerSpecList2.get(2));
                registerSpecList2.set(1, registerSpec);
                registerSpecList2.set(2, registerSpec2);
            } else if (i == 181) {
                if (iArgCount != 2) {
                    throw new RuntimeException("shouldn't happen");
                }
                RegisterSpec registerSpec3 = registerSpecList2.get(0);
                registerSpecList2.set(0, registerSpecList2.get(1));
                registerSpecList2.set(1, registerSpec3);
            }
            registerSpecList = registerSpecList2;
        }
        registerSpecList.setImmutable();
        return registerSpecList;
    }

    private void updateReturnOp(Rop rop, SourcePosition sourcePosition) {
        if (rop == null) {
            throw new NullPointerException("op == null");
        }
        if (sourcePosition == null) {
            throw new NullPointerException("pos == null");
        }
        Rop rop2 = this.returnOp;
        if (rop2 == null) {
            this.returnOp = rop;
            this.returnPosition = sourcePosition;
        } else {
            if (rop2 != rop) {
                throw new SimException("return op mismatch: " + rop + ", " + this.returnOp);
            }
            if (sourcePosition.getLine() > this.returnPosition.getLine()) {
                this.returnPosition = sourcePosition;
            }
        }
    }

    private int jopToRopOpcode(int i, Constant constant) {
        if (i == 0) {
            return 1;
        }
        if (i == 20) {
            return 5;
        }
        if (i == 21) {
            return 2;
        }
        if (i == 171) {
            return 13;
        }
        if (i == 172) {
            return 33;
        }
        if (i == 198) {
            return 7;
        }
        if (i == 199) {
            return 8;
        }
        switch (i) {
            case 0:
                return 1;
            case 18:
                return 5;
            case 46:
                return 38;
            case 54:
                return 2;
            case 79:
                return 39;
            case 96:
                return 14;
            case 100:
                return 15;
            case 104:
                return 16;
            case 108:
                return 17;
            case 112:
                return 18;
            case 116:
                return 19;
            case 120:
                return 23;
            case 122:
                return 24;
            case 124:
                return 25;
            case 126:
                return 20;
            case 128:
                return 21;
            case 130:
                return 22;
            default:
                switch (i) {
                    case 132:
                        return 14;
                    case 133:
                    case 134:
                    case 135:
                    case 136:
                    case 137:
                    case 138:
                    case 139:
                    case 140:
                    case 141:
                    case 142:
                    case 143:
                    case 144:
                        return 29;
                    case 145:
                        return 30;
                    case 146:
                        return 31;
                    case 147:
                        return 32;
                    case 148:
                    case 149:
                    case 151:
                        return 27;
                    case 150:
                    case 152:
                        return 28;
                    case 153:
                    case 159:
                    case 165:
                        return 7;
                    case 154:
                    case 160:
                    case 166:
                        return 8;
                    case 155:
                    case 161:
                        return 9;
                    case 156:
                    case 162:
                        return 10;
                    case 157:
                    case 163:
                        return 12;
                    case 158:
                    case 164:
                        return 11;
                    case 167:
                        return 6;
                    default:
                        switch (i) {
                            case 177:
                                return 33;
                            case 178:
                                return 46;
                            case 179:
                                return 48;
                            case 180:
                                return 45;
                            case 181:
                                return 47;
                            case 182:
                                CstMethodRef cstMethodRef = (CstMethodRef) constant;
                                if (cstMethodRef.getDefiningClass().equals(this.method.getDefiningClass())) {
                                    for (int i2 = 0; i2 < this.methods.size(); i2++) {
                                        Method method = this.methods.get(i2);
                                        if (AccessFlags.isPrivate(method.getAccessFlags()) && cstMethodRef.getNat().equals(method.getNat())) {
                                            return 52;
                                        }
                                    }
                                }
                                return cstMethodRef.isSignaturePolymorphic() ? 58 : 50;
                            case 183:
                                CstMethodRef cstMethodRef2 = (CstMethodRef) constant;
                                return (cstMethodRef2.isInstanceInit() || cstMethodRef2.getDefiningClass().equals(this.method.getDefiningClass())) ? 52 : 51;
                            case 184:
                                return 49;
                            case 185:
                                return 53;
                            case 186:
                                return 59;
                            case 187:
                                return 40;
                            case 188:
                            case 189:
                                return 41;
                            case 190:
                                return 34;
                            case 191:
                                return 35;
                            case 192:
                                return 43;
                            case 193:
                                return 44;
                            case 194:
                                return 36;
                            case 195:
                                return 37;
                            default:
                                throw new RuntimeException("shouldn't happen");
                        }
                }
        }
    }

    private Insn makeInvokePolymorphicInsn(Rop rop, SourcePosition sourcePosition, RegisterSpecList registerSpecList, TypeList typeList, Constant constant) {
        return new InvokePolymorphicInsn(rop, sourcePosition, registerSpecList, typeList, (CstMethodRef) constant);
    }
}
