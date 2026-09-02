package androidx.constraintlayout.core.widgets;

import androidx.constraintlayout.core.ArrayRow;
import androidx.constraintlayout.core.LinearSystem;
import androidx.constraintlayout.core.SolverVariable;
import java.util.ArrayList;

public abstract class Chain {
    public static void applyChainConstraints(ConstraintWidgetContainer constraintWidgetContainer, LinearSystem linearSystem, ArrayList arrayList, int i) {
        int i2;
        ChainHead[] chainHeadArr;
        int i3;
        if (i == 0) {
            i2 = constraintWidgetContainer.mHorizontalChainsSize;
            chainHeadArr = constraintWidgetContainer.mHorizontalChainsArray;
            i3 = 0;
        } else {
            i2 = constraintWidgetContainer.mVerticalChainsSize;
            chainHeadArr = constraintWidgetContainer.mVerticalChainsArray;
            i3 = 2;
        }
        for (int i4 = 0; i4 < i2; i4++) {
            ChainHead chainHead = chainHeadArr[i4];
            chainHead.define();
            if (arrayList == null || arrayList.contains(chainHead.mFirst)) {
                applyChainConstraints(constraintWidgetContainer, linearSystem, i, i3, chainHead);
            }
        }
    }

    /* JADX WARN: Code duplicated, block: B:100:0x016d  */
    /* JADX WARN: Code duplicated, block: B:102:0x0173  */
    /* JADX WARN: Code duplicated, block: B:104:0x0194  */
    /* JADX WARN: Code duplicated, block: B:16:0x0033 A[PHI: r15 r16
  0x0033: PHI (r15v26 boolean) = (r15v1 boolean), (r15v28 boolean) binds: [B:26:0x0047, B:15:0x0031] A[DONT_GENERATE, DONT_INLINE]
  0x0033: PHI (r16v5 boolean) = (r16v1 boolean), (r16v7 boolean) binds: [B:26:0x0047, B:15:0x0031] A[DONT_GENERATE, DONT_INLINE]] */
    /* JADX WARN: Code duplicated, block: B:17:0x0035 A[PHI: r15 r16
  0x0035: PHI (r15v3 boolean) = (r15v1 boolean), (r15v28 boolean) binds: [B:26:0x0047, B:15:0x0031] A[DONT_GENERATE, DONT_INLINE]
  0x0035: PHI (r16v3 boolean) = (r16v1 boolean), (r16v7 boolean) binds: [B:26:0x0047, B:15:0x0031] A[DONT_GENERATE, DONT_INLINE]] */
    /* JADX WARN: Code duplicated, block: B:218:0x038a  */
    /* JADX WARN: Code duplicated, block: B:289:0x04b5  */
    /* JADX WARN: Code duplicated, block: B:292:0x04c2  */
    /* JADX WARN: Code duplicated, block: B:293:0x04c5  */
    /* JADX WARN: Code duplicated, block: B:296:0x04cb  */
    /* JADX WARN: Code duplicated, block: B:297:0x04ce  */
    /* JADX WARN: Code duplicated, block: B:299:0x04d2  */
    /* JADX WARN: Code duplicated, block: B:301:0x04da  */
    /* JADX WARN: Code duplicated, block: B:304:0x04e2  */
    /* JADX WARN: Code duplicated, block: B:317:0x038b A[SYNTHETIC] */
    /* JADX WARN: Code duplicated, block: B:98:0x016a  */
    /* JADX WARN: Multi-variable type inference failed */
    /* JADX WARN: Type inference failed for: r0v27, types: [androidx.constraintlayout.core.LinearSystem] */
    /* JADX WARN: Type inference failed for: r0v28 */
    /* JADX WARN: Type inference failed for: r0v44 */
    /* JADX WARN: Type inference failed for: r0v5 */
    /* JADX WARN: Type inference failed for: r0v6, types: [androidx.constraintlayout.core.LinearSystem] */
    /* JADX WARN: Type inference failed for: r14v1 */
    /* JADX WARN: Type inference failed for: r14v2, types: [androidx.constraintlayout.core.widgets.ConstraintWidget] */
    /* JADX WARN: Type inference failed for: r14v24 */
    /* JADX WARN: Type inference failed for: r14v25 */
    /* JADX WARN: Type inference failed for: r14v26 */
    /* JADX WARN: Type inference failed for: r5v17, types: [androidx.constraintlayout.core.SolverVariable] */
    static void applyChainConstraints(ConstraintWidgetContainer constraintWidgetContainer, LinearSystem linearSystem, int i, int i2, ChainHead chainHead) {
        boolean z;
        boolean z2;
        boolean z3;
        float f;
        ?? r0;
        LinearSystem linearSystem2;
        ConstraintAnchor constraintAnchor;
        SolverVariable solverVariable;
        SolverVariable solverVariable2;
        int i3;
        ConstraintAnchor constraintAnchor2;
        SolverVariable solverVariable3;
        int i4;
        ConstraintAnchor[] constraintAnchorArr;
        int i5;
        ConstraintAnchor constraintAnchor3;
        ConstraintAnchor constraintAnchor4;
        SolverVariable solverVariable4;
        ConstraintAnchor constraintAnchor5;
        Object obj;
        float f2;
        int size;
        ConstraintAnchor constraintAnchor6;
        int i6;
        int i7 = i;
        ConstraintWidget constraintWidget = chainHead.mFirst;
        ConstraintWidget constraintWidget2 = chainHead.mLast;
        ConstraintWidget constraintWidget3 = chainHead.mFirstVisibleWidget;
        ConstraintWidget constraintWidget4 = chainHead.mLastVisibleWidget;
        ConstraintWidget constraintWidget5 = chainHead.mHead;
        float f3 = chainHead.mTotalWeight;
        boolean z4 = constraintWidgetContainer.mListDimensionBehaviors[i7] == ConstraintWidget.DimensionBehaviour.WRAP_CONTENT;
        if (i7 == 0) {
            int i8 = constraintWidget5.mHorizontalChainStyle;
            z = i8 == 0;
            z2 = i8 == 1;
            if (i8 == 2) {
                z3 = true;
            } else {
                z3 = false;
            }
        } else {
            int i9 = constraintWidget5.mVerticalChainStyle;
            z = i9 == 0;
            z2 = i9 == 1;
            if (i9 == 2) {
                z3 = true;
            } else {
                z3 = false;
            }
        }
        ?? r14 = constraintWidget;
        boolean z5 = false;
        while (true) {
            f = f3;
            Object obj2 = null;
            if (z5) {
                break;
            }
            ConstraintAnchor constraintAnchor7 = r14.mListAnchors[i2];
            int i10 = z3 ? 1 : 4;
            int margin = constraintAnchor7.getMargin();
            ConstraintWidget.DimensionBehaviour dimensionBehaviour = r14.mListDimensionBehaviors[i7];
            boolean z6 = z4;
            ConstraintWidget.DimensionBehaviour dimensionBehaviour2 = ConstraintWidget.DimensionBehaviour.MATCH_CONSTRAINT;
            boolean z7 = dimensionBehaviour == dimensionBehaviour2 && r14.mResolvedMatchConstraintDefault[i7] == 0;
            boolean z8 = z3;
            ConstraintAnchor constraintAnchor8 = constraintAnchor7.mTarget;
            if (constraintAnchor8 != null && r14 != constraintWidget) {
                margin += constraintAnchor8.getMargin();
            }
            int i11 = margin;
            if (z8 && r14 != constraintWidget && r14 != constraintWidget3) {
                i10 = 8;
            }
            boolean z9 = z7;
            ConstraintAnchor constraintAnchor9 = constraintAnchor7.mTarget;
            if (constraintAnchor9 != null) {
                if (r14 == constraintWidget3) {
                    linearSystem.addGreaterThan(constraintAnchor7.mSolverVariable, constraintAnchor9.mSolverVariable, i11, 6);
                } else {
                    linearSystem.addGreaterThan(constraintAnchor7.mSolverVariable, constraintAnchor9.mSolverVariable, i11, 8);
                }
                if (z9 && !z8) {
                    i10 = 5;
                }
                linearSystem.addEquality(constraintAnchor7.mSolverVariable, constraintAnchor7.mTarget.mSolverVariable, i11, (r14 == constraintWidget3 && z8 && r14.isInBarrier(i7)) ? 5 : i10);
            } else {
                z5 = z5;
                z = z;
            }
            if (z6) {
                if (r14.getVisibility() == 8 || r14.mListDimensionBehaviors[i7] != dimensionBehaviour2) {
                    i6 = 0;
                } else {
                    ConstraintAnchor[] constraintAnchorArr2 = r14.mListAnchors;
                    i6 = 0;
                    linearSystem.addGreaterThan(constraintAnchorArr2[i2 + 1].mSolverVariable, constraintAnchorArr2[i2].mSolverVariable, 0, 5);
                }
                linearSystem.addGreaterThan(r14.mListAnchors[i2].mSolverVariable, constraintWidgetContainer.mListAnchors[i2].mSolverVariable, i6, 8);
            }
            ConstraintAnchor constraintAnchor10 = r14.mListAnchors[i2 + 1].mTarget;
            if (constraintAnchor10 != null) {
                ConstraintWidget constraintWidget6 = constraintAnchor10.mOwner;
                ConstraintAnchor constraintAnchor11 = constraintWidget6.mListAnchors[i2].mTarget;
                if (constraintAnchor11 != null && constraintAnchor11.mOwner == r14) {
                    obj2 = constraintWidget6;
                }
            }
            if (obj2 != null) {
                r14 = obj2;
                z5 = z5;
            } else {
                z5 = true;
            }
            f3 = f;
            z4 = z6;
            z3 = z8;
            z = z;
            r14 = r14;
        }
        boolean z10 = z4;
        boolean z11 = z3;
        boolean z12 = z;
        if (constraintWidget4 != null) {
            int i12 = i2 + 1;
            if (constraintWidget2.mListAnchors[i12].mTarget != null) {
                ConstraintAnchor constraintAnchor12 = constraintWidget4.mListAnchors[i12];
                if (constraintWidget4.mListDimensionBehaviors[i7] == ConstraintWidget.DimensionBehaviour.MATCH_CONSTRAINT && constraintWidget4.mResolvedMatchConstraintDefault[i7] == 0 && !z11) {
                    ConstraintAnchor constraintAnchor13 = constraintAnchor12.mTarget;
                    if (constraintAnchor13.mOwner == constraintWidgetContainer) {
                        linearSystem.addEquality(constraintAnchor12.mSolverVariable, constraintAnchor13.mSolverVariable, -constraintAnchor12.getMargin(), 5);
                    } else if (z11) {
                        constraintAnchor6 = constraintAnchor12.mTarget;
                        if (constraintAnchor6.mOwner == constraintWidgetContainer) {
                            linearSystem.addEquality(constraintAnchor12.mSolverVariable, constraintAnchor6.mSolverVariable, -constraintAnchor12.getMargin(), 4);
                        }
                    }
                } else if (z11) {
                    constraintAnchor6 = constraintAnchor12.mTarget;
                    if (constraintAnchor6.mOwner == constraintWidgetContainer) {
                        linearSystem.addEquality(constraintAnchor12.mSolverVariable, constraintAnchor6.mSolverVariable, -constraintAnchor12.getMargin(), 4);
                    }
                }
                linearSystem.addLowerThan(constraintAnchor12.mSolverVariable, constraintWidget2.mListAnchors[i12].mTarget.mSolverVariable, -constraintAnchor12.getMargin(), 6);
            }
        }
        if (z10) {
            int i13 = i2 + 1;
            SolverVariable solverVariable5 = constraintWidgetContainer.mListAnchors[i13].mSolverVariable;
            ConstraintAnchor constraintAnchor14 = constraintWidget2.mListAnchors[i13];
            linearSystem.addGreaterThan(solverVariable5, constraintAnchor14.mSolverVariable, constraintAnchor14.getMargin(), 8);
        }
        ArrayList arrayList = chainHead.mWeightedMatchConstraintsWidgets;
        if (arrayList != null && (size = arrayList.size()) > 1) {
            float f4 = (!chainHead.mHasUndefinedWeights || chainHead.mHasComplexMatchWeights) ? f : chainHead.mWidgetsMatchCount;
            float f5 = 0.0f;
            float f6 = 0.0f;
            ConstraintWidget constraintWidget7 = null;
            int i14 = 0;
            while (i14 < size) {
                ConstraintWidget constraintWidget8 = (ConstraintWidget) arrayList.get(i14);
                float f7 = constraintWidget8.mWeight[i7];
                if (f7 < f5) {
                    if (chainHead.mHasComplexMatchWeights) {
                        ConstraintAnchor[] constraintAnchorArr3 = constraintWidget8.mListAnchors;
                        f5 = f5;
                        linearSystem.addEquality(constraintAnchorArr3[i2 + 1].mSolverVariable, constraintAnchorArr3[i2].mSolverVariable, 0, 4);
                    } else {
                        f7 = 1.0f;
                    }
                    arrayList = arrayList;
                    i14++;
                    f5 = f5;
                    arrayList = arrayList;
                }
                float f8 = f7;
                if (f8 == f5) {
                    ConstraintAnchor[] constraintAnchorArr4 = constraintWidget8.mListAnchors;
                    linearSystem.addEquality(constraintAnchorArr4[i2 + 1].mSolverVariable, constraintAnchorArr4[i2].mSolverVariable, 0, 8);
                    arrayList = arrayList;
                } else {
                    if (constraintWidget7 != null) {
                        ConstraintAnchor[] constraintAnchorArr5 = constraintWidget7.mListAnchors;
                        SolverVariable solverVariable6 = constraintAnchorArr5[i2].mSolverVariable;
                        int i15 = i2 + 1;
                        SolverVariable solverVariable7 = constraintAnchorArr5[i15].mSolverVariable;
                        ConstraintAnchor[] constraintAnchorArr6 = constraintWidget8.mListAnchors;
                        SolverVariable solverVariable8 = constraintAnchorArr6[i2].mSolverVariable;
                        SolverVariable solverVariable9 = constraintAnchorArr6[i15].mSolverVariable;
                        ArrayRow arrayRowCreateRow = linearSystem.createRow();
                        arrayRowCreateRow.createRowEqualMatchDimensions(f6, f4, f8, solverVariable6, solverVariable7, solverVariable8, solverVariable9);
                        linearSystem.addConstraint(arrayRowCreateRow);
                    }
                    constraintWidget7 = constraintWidget8;
                    f6 = f8;
                }
                i14++;
                f5 = f5;
                arrayList = arrayList;
            }
        }
        if (constraintWidget3 != null && (constraintWidget3 == constraintWidget4 || z11)) {
            ConstraintAnchor constraintAnchor15 = constraintWidget.mListAnchors[i2];
            int i16 = i2 + 1;
            ConstraintAnchor constraintAnchor16 = constraintWidget2.mListAnchors[i16];
            ConstraintAnchor constraintAnchor17 = constraintAnchor15.mTarget;
            SolverVariable solverVariable10 = constraintAnchor17 != null ? constraintAnchor17.mSolverVariable : null;
            ConstraintAnchor constraintAnchor18 = constraintAnchor16.mTarget;
            SolverVariable solverVariable11 = constraintAnchor18 != null ? constraintAnchor18.mSolverVariable : null;
            ConstraintAnchor constraintAnchor19 = constraintWidget3.mListAnchors[i2];
            if (constraintWidget4 != null) {
                constraintAnchor16 = constraintWidget4.mListAnchors[i16];
            }
            if (solverVariable10 != null && solverVariable11 != null) {
                if (i7 == 0) {
                    f2 = constraintWidget5.mHorizontalBiasPercent;
                } else {
                    f2 = constraintWidget5.mVerticalBiasPercent;
                }
                linearSystem.addCentering(constraintAnchor19.mSolverVariable, solverVariable10, constraintAnchor19.getMargin(), f2, solverVariable11, constraintAnchor16.mSolverVariable, constraintAnchor16.getMargin(), 7);
            }
        } else {
            if (!z12 || constraintWidget3 == null) {
                if (z2 && constraintWidget3 != null) {
                    int i17 = chainHead.mWidgetsMatchCount;
                    boolean z13 = i17 > 0 && chainHead.mWidgetsCount == i17;
                    ConstraintWidget constraintWidget9 = constraintWidget3;
                    ConstraintWidget constraintWidget10 = constraintWidget9;
                    while (constraintWidget9 != null) {
                        ConstraintWidget constraintWidget11 = constraintWidget9.mNextChainWidget[i];
                        while (constraintWidget11 != null && constraintWidget11.getVisibility() == 8) {
                            constraintWidget11 = constraintWidget11.mNextChainWidget[i];
                        }
                        if (constraintWidget9 != constraintWidget3 && constraintWidget9 != constraintWidget4 && constraintWidget11 != null) {
                            if (constraintWidget11 == constraintWidget4) {
                                constraintWidget11 = null;
                            }
                            ConstraintAnchor constraintAnchor20 = constraintWidget9.mListAnchors[i2];
                            SolverVariable solverVariable12 = constraintAnchor20.mSolverVariable;
                            ConstraintAnchor constraintAnchor21 = constraintAnchor20.mTarget;
                            if (constraintAnchor21 != null) {
                                SolverVariable solverVariable13 = constraintAnchor21.mSolverVariable;
                            }
                            int i18 = i2 + 1;
                            SolverVariable solverVariable14 = constraintWidget10.mListAnchors[i18].mSolverVariable;
                            int margin2 = constraintAnchor20.getMargin();
                            int margin3 = constraintWidget9.mListAnchors[i18].getMargin();
                            if (constraintWidget11 != null) {
                                constraintAnchor = constraintWidget11.mListAnchors[i2];
                                solverVariable = constraintAnchor.mSolverVariable;
                                ConstraintAnchor constraintAnchor22 = constraintAnchor.mTarget;
                                solverVariable2 = constraintAnchor22 != null ? constraintAnchor22.mSolverVariable : null;
                            } else {
                                constraintAnchor = constraintWidget4.mListAnchors[i2];
                                solverVariable = constraintAnchor != null ? constraintAnchor.mSolverVariable : null;
                                solverVariable2 = constraintWidget9.mListAnchors[i18].mSolverVariable;
                            }
                            if (constraintAnchor != null) {
                                margin3 += constraintAnchor.getMargin();
                            }
                            int margin4 = margin2 + constraintWidget10.mListAnchors[i18].getMargin();
                            int i19 = z13 ? 8 : 4;
                            if (solverVariable12 != null && solverVariable14 != null && solverVariable != null && solverVariable2 != null) {
                                linearSystem.addCentering(solverVariable12, solverVariable14, margin4, 0.5f, solverVariable, solverVariable2, margin3, i19);
                            }
                            constraintWidget11 = constraintWidget11;
                        }
                        if (constraintWidget9.getVisibility() != 8) {
                            constraintWidget10 = constraintWidget9;
                        }
                        constraintWidget9 = constraintWidget11;
                    }
                    ConstraintAnchor constraintAnchor23 = constraintWidget3.mListAnchors[i2];
                    ConstraintAnchor constraintAnchor24 = constraintWidget.mListAnchors[i2].mTarget;
                    int i20 = i2 + 1;
                    ConstraintAnchor constraintAnchor25 = constraintWidget4.mListAnchors[i20];
                    ConstraintAnchor constraintAnchor26 = constraintWidget2.mListAnchors[i20].mTarget;
                    if (constraintAnchor24 == null) {
                        r0 = linearSystem;
                    } else {
                        if (constraintWidget3 != constraintWidget4) {
                            linearSystem.addEquality(constraintAnchor23.mSolverVariable, constraintAnchor24.mSolverVariable, constraintAnchor23.getMargin(), 5);
                        } else if (constraintAnchor26 != null) {
                            linearSystem2 = linearSystem;
                            linearSystem2.addCentering(constraintAnchor23.mSolverVariable, constraintAnchor24.mSolverVariable, constraintAnchor23.getMargin(), 0.5f, constraintAnchor25.mSolverVariable, constraintAnchor26.mSolverVariable, constraintAnchor25.getMargin(), 5);
                        }
                        r0 = linearSystem;
                    }
                    if (constraintAnchor26 != null && constraintWidget3 != constraintWidget4) {
                        r0.addEquality(constraintAnchor25.mSolverVariable, constraintAnchor26.mSolverVariable, -constraintAnchor25.getMargin(), 5);
                    }
                }
                if ((z12 && !z2) || constraintWidget3 == null || constraintWidget3 == constraintWidget4) {
                    return;
                }
                constraintAnchorArr = constraintWidget3.mListAnchors;
                ConstraintAnchor constraintAnchor27 = constraintAnchorArr[i2];
                if (constraintWidget4 == null) {
                    constraintWidget4 = constraintWidget3;
                }
                i5 = i2 + 1;
                constraintAnchor3 = constraintWidget4.mListAnchors[i5];
                constraintAnchor4 = constraintAnchor27.mTarget;
                if (constraintAnchor4 != null) {
                    solverVariable4 = constraintAnchor4.mSolverVariable;
                } else {
                    solverVariable4 = null;
                }
                constraintAnchor5 = constraintAnchor3.mTarget;
                if (constraintAnchor5 != null) {
                    obj = constraintAnchor5.mSolverVariable;
                } else {
                    obj = null;
                }
                if (constraintWidget2 != constraintWidget4) {
                    ConstraintAnchor constraintAnchor28 = constraintWidget2.mListAnchors[i5].mTarget;
                    obj = constraintAnchor28 != null ? constraintAnchor28.mSolverVariable : null;
                }
                if (constraintWidget3 == constraintWidget4) {
                    constraintAnchor3 = constraintAnchorArr[i5];
                }
                if (solverVariable4 != null || obj == null) {
                }
                r0.addCentering(constraintAnchor27.mSolverVariable, solverVariable4, constraintAnchor27.getMargin(), 0.5f, obj, constraintAnchor3.mSolverVariable, constraintWidget4.mListAnchors[i5].getMargin(), 5);
                return;
            }
            int i21 = chainHead.mWidgetsMatchCount;
            boolean z14 = i21 > 0 && chainHead.mWidgetsCount == i21;
            ConstraintWidget constraintWidget12 = constraintWidget3;
            ConstraintWidget constraintWidget13 = constraintWidget12;
            while (constraintWidget12 != null) {
                ConstraintWidget constraintWidget14 = constraintWidget12.mNextChainWidget[i7];
                while (true) {
                    if (constraintWidget14 == null) {
                        i3 = 8;
                        break;
                    }
                    i3 = 8;
                    if (constraintWidget14.getVisibility() != 8) {
                        break;
                    } else {
                        constraintWidget14 = constraintWidget14.mNextChainWidget[i7];
                    }
                }
                if (constraintWidget14 != null || constraintWidget12 == constraintWidget4) {
                    ConstraintAnchor constraintAnchor29 = constraintWidget12.mListAnchors[i2];
                    SolverVariable solverVariable15 = constraintAnchor29.mSolverVariable;
                    ConstraintAnchor constraintAnchor30 = constraintAnchor29.mTarget;
                    SolverVariable solverVariable16 = constraintAnchor30 != null ? constraintAnchor30.mSolverVariable : null;
                    if (constraintWidget13 != constraintWidget12) {
                        solverVariable16 = constraintWidget13.mListAnchors[i2 + 1].mSolverVariable;
                    } else if (constraintWidget12 == constraintWidget3) {
                        ConstraintAnchor constraintAnchor31 = constraintWidget.mListAnchors[i2].mTarget;
                        solverVariable16 = constraintAnchor31 != null ? constraintAnchor31.mSolverVariable : null;
                    }
                    int margin5 = constraintAnchor29.getMargin();
                    int i22 = i2 + 1;
                    int margin6 = constraintWidget12.mListAnchors[i22].getMargin();
                    if (constraintWidget14 != null) {
                        constraintAnchor2 = constraintWidget14.mListAnchors[i2];
                        solverVariable3 = constraintAnchor2.mSolverVariable;
                    } else {
                        constraintAnchor2 = constraintWidget2.mListAnchors[i22].mTarget;
                        solverVariable3 = constraintAnchor2 != null ? constraintAnchor2.mSolverVariable : null;
                    }
                    SolverVariable solverVariable17 = constraintWidget12.mListAnchors[i22].mSolverVariable;
                    if (constraintAnchor2 != null) {
                        margin6 += constraintAnchor2.getMargin();
                    }
                    int margin7 = margin5 + constraintWidget13.mListAnchors[i22].getMargin();
                    if (solverVariable15 == null || solverVariable16 == null || solverVariable3 == null || solverVariable17 == null) {
                        i4 = 8;
                    } else {
                        if (constraintWidget12 == constraintWidget3) {
                            margin7 = constraintWidget3.mListAnchors[i2].getMargin();
                        }
                        if (constraintWidget12 == constraintWidget4) {
                            margin6 = constraintWidget4.mListAnchors[i22].getMargin();
                        }
                        constraintWidget14 = constraintWidget14;
                        i4 = 8;
                        linearSystem.addCentering(solverVariable15, solverVariable16, margin7, 0.5f, solverVariable3, solverVariable17, margin6, z14 ? 8 : 5);
                    }
                    if (constraintWidget12.getVisibility() != i4) {
                        constraintWidget13 = constraintWidget12;
                    }
                    i7 = i;
                    constraintWidget12 = constraintWidget14;
                } else {
                    i4 = i3;
                }
                if (constraintWidget12.getVisibility() != i4) {
                    constraintWidget13 = constraintWidget12;
                }
                i7 = i;
                constraintWidget12 = constraintWidget14;
            }
        }
        r0 = linearSystem;
        if (z12) {
        }
        constraintAnchorArr = constraintWidget3.mListAnchors;
        ConstraintAnchor constraintAnchor210 = constraintAnchorArr[i2];
        if (constraintWidget4 == null) {
            constraintWidget4 = constraintWidget3;
        }
        i5 = i2 + 1;
        constraintAnchor3 = constraintWidget4.mListAnchors[i5];
        constraintAnchor4 = constraintAnchor210.mTarget;
        if (constraintAnchor4 != null) {
            solverVariable4 = constraintAnchor4.mSolverVariable;
        } else {
            solverVariable4 = null;
        }
        constraintAnchor5 = constraintAnchor3.mTarget;
        if (constraintAnchor5 != null) {
            obj = constraintAnchor5.mSolverVariable;
        } else {
            obj = null;
        }
        if (constraintWidget2 != constraintWidget4) {
            ConstraintAnchor constraintAnchor211 = constraintWidget2.mListAnchors[i5].mTarget;
            obj = constraintAnchor211 != null ? constraintAnchor211.mSolverVariable : null;
        }
        if (constraintWidget3 == constraintWidget4) {
            constraintAnchor3 = constraintAnchorArr[i5];
        }
        if (solverVariable4 != null) {
        }
    }
}
