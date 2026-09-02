package androidx.constraintlayout.core.widgets.analyzer;

import androidx.constraintlayout.core.widgets.ConstraintAnchor;
import androidx.constraintlayout.core.widgets.ConstraintWidget;
import androidx.constraintlayout.core.widgets.ConstraintWidgetContainer;
import java.util.ArrayList;

public class ChainRun extends WidgetRun {
    private int mChainStyle;
    ArrayList mWidgets;

    public ChainRun(ConstraintWidget constraintWidget, int i) {
        super(constraintWidget);
        this.mWidgets = new ArrayList();
        this.orientation = i;
        build();
    }

    public String toString() {
        StringBuilder sb = new StringBuilder("ChainRun ");
        sb.append(this.orientation == 0 ? "horizontal : " : "vertical : ");
        ArrayList arrayList = this.mWidgets;
        int size = arrayList.size();
        int i = 0;
        while (i < size) {
            Object obj = arrayList.get(i);
            i++;
            sb.append("<");
            sb.append((WidgetRun) obj);
            sb.append("> ");
        }
        return sb.toString();
    }

    @Override // androidx.constraintlayout.core.widgets.analyzer.WidgetRun
    boolean supportsWrapComputation() {
        int size = this.mWidgets.size();
        for (int i = 0; i < size; i++) {
            if (!((WidgetRun) this.mWidgets.get(i)).supportsWrapComputation()) {
                return false;
            }
        }
        return true;
    }

    @Override // androidx.constraintlayout.core.widgets.analyzer.WidgetRun
    public long getWrapDimension() {
        int size = this.mWidgets.size();
        long wrapDimension = 0;
        for (int i = 0; i < size; i++) {
            WidgetRun widgetRun = (WidgetRun) this.mWidgets.get(i);
            wrapDimension = wrapDimension + ((long) widgetRun.start.mMargin) + widgetRun.getWrapDimension() + ((long) widgetRun.end.mMargin);
        }
        return wrapDimension;
    }

    private void build() {
        ConstraintWidget constraintWidget;
        ConstraintWidget constraintWidget2 = this.mWidget;
        ConstraintWidget previousChainMember = constraintWidget2.getPreviousChainMember(this.orientation);
        while (true) {
            ConstraintWidget constraintWidget3 = previousChainMember;
            constraintWidget = constraintWidget2;
            constraintWidget2 = constraintWidget3;
            if (constraintWidget2 == null) {
                break;
            } else {
                previousChainMember = constraintWidget2.getPreviousChainMember(this.orientation);
            }
        }
        this.mWidget = constraintWidget;
        this.mWidgets.add(constraintWidget.getRun(this.orientation));
        ConstraintWidget nextChainMember = constraintWidget.getNextChainMember(this.orientation);
        while (nextChainMember != null) {
            this.mWidgets.add(nextChainMember.getRun(this.orientation));
            nextChainMember = nextChainMember.getNextChainMember(this.orientation);
        }
        ArrayList arrayList = this.mWidgets;
        int size = arrayList.size();
        int i = 0;
        while (i < size) {
            Object obj = arrayList.get(i);
            i++;
            WidgetRun widgetRun = (WidgetRun) obj;
            int i2 = this.orientation;
            if (i2 == 0) {
                widgetRun.mWidget.horizontalChainRun = this;
            } else if (i2 == 1) {
                widgetRun.mWidget.verticalChainRun = this;
            }
        }
        if (this.orientation == 0 && ((ConstraintWidgetContainer) this.mWidget.getParent()).isRtl() && this.mWidgets.size() > 1) {
            ArrayList arrayList2 = this.mWidgets;
            this.mWidget = ((WidgetRun) arrayList2.get(arrayList2.size() - 1)).mWidget;
        }
        this.mChainStyle = this.orientation == 0 ? this.mWidget.getHorizontalChainStyle() : this.mWidget.getVerticalChainStyle();
    }

    @Override // androidx.constraintlayout.core.widgets.analyzer.WidgetRun
    void clear() {
        this.mRunGroup = null;
        ArrayList arrayList = this.mWidgets;
        int size = arrayList.size();
        int i = 0;
        while (i < size) {
            Object obj = arrayList.get(i);
            i++;
            ((WidgetRun) obj).clear();
        }
    }

    /* JADX WARN: Code duplicated, block: B:90:0x0160  */
    @Override // androidx.constraintlayout.core.widgets.analyzer.WidgetRun, androidx.constraintlayout.core.widgets.analyzer.Dependency
    public void update(Dependency dependency) {
        int i;
        int i2;
        boolean z;
        float f;
        float f2;
        int i3;
        int i4;
        int i5;
        int i6;
        float f3;
        int i7;
        int i8;
        int i9;
        int i10;
        int i11;
        boolean z2;
        if (this.start.resolved && this.end.resolved) {
            ConstraintWidget parent = this.mWidget.getParent();
            boolean zIsRtl = parent instanceof ConstraintWidgetContainer ? ((ConstraintWidgetContainer) parent).isRtl() : false;
            int i12 = this.end.value - this.start.value;
            int size = this.mWidgets.size();
            int i13 = 0;
            while (true) {
                i = -1;
                i2 = 8;
                if (i13 >= size) {
                    i13 = -1;
                    break;
                } else if (((WidgetRun) this.mWidgets.get(i13)).mWidget.getVisibility() != 8) {
                    break;
                } else {
                    i13++;
                }
            }
            int i14 = size - 1;
            for (int i15 = i14; i15 >= 0; i15--) {
                if (((WidgetRun) this.mWidgets.get(i15)).mWidget.getVisibility() != 8) {
                    i = i15;
                    break;
                }
            }
            int i16 = 0;
            while (true) {
                if (i16 >= 2) {
                    z = zIsRtl;
                    f = 0.0f;
                    f2 = 0.0f;
                    i3 = 0;
                    i4 = 0;
                    i5 = 0;
                    break;
                }
                int i17 = 0;
                i4 = 0;
                i5 = 0;
                int i18 = 0;
                f2 = 0.0f;
                while (i17 < size) {
                    WidgetRun widgetRun = (WidgetRun) this.mWidgets.get(i17);
                    if (widgetRun.mWidget.getVisibility() == i2) {
                        z2 = zIsRtl;
                    } else {
                        i18++;
                        if (i17 > 0 && i17 >= i13) {
                            i4 += widgetRun.start.mMargin;
                        }
                        DimensionDependency dimensionDependency = widgetRun.mDimension;
                        int i19 = dimensionDependency.value;
                        boolean z3 = widgetRun.mDimensionBehavior != ConstraintWidget.DimensionBehaviour.MATCH_CONSTRAINT;
                        if (z3) {
                            int i20 = this.orientation;
                            if (i20 == 0 && !widgetRun.mWidget.mHorizontalRun.mDimension.resolved) {
                                return;
                            }
                            if (i20 == 1 && !widgetRun.mWidget.mVerticalRun.mDimension.resolved) {
                                return;
                            } else {
                                z2 = zIsRtl;
                            }
                        } else {
                            z2 = zIsRtl;
                            if (widgetRun.matchConstraintsType == 1 && i16 == 0) {
                                i19 = dimensionDependency.wrapValue;
                                i5++;
                            } else if (dimensionDependency.resolved) {
                            }
                            z3 = true;
                        }
                        if (z3) {
                            i4 += i19;
                        } else {
                            i5++;
                            float f4 = widgetRun.mWidget.mWeight[this.orientation];
                            if (f4 >= 0.0f) {
                                f2 += f4;
                            }
                        }
                        if (i17 < i14 && i17 < i) {
                            i4 += -widgetRun.end.mMargin;
                        }
                    }
                    i17++;
                    zIsRtl = z2;
                    i2 = 8;
                }
                z = zIsRtl;
                f = 0.0f;
                if (i4 < i12 || i5 == 0) {
                    i3 = i18;
                    break;
                } else {
                    i16++;
                    zIsRtl = z;
                    i2 = 8;
                }
            }
            int i21 = this.start.value;
            if (z) {
                i21 = this.end.value;
            }
            float f5 = 0.5f;
            if (i4 > i12) {
                i21 = z ? i21 + ((int) (((i4 - i12) / 2.0f) + 0.5f)) : i21 - ((int) (((i4 - i12) / 2.0f) + 0.5f));
            }
            if (i5 > 0) {
                float f6 = i12 - i4;
                int i22 = (int) ((f6 / i5) + 0.5f);
                int i23 = 0;
                int i24 = 0;
                while (i23 < size) {
                    WidgetRun widgetRun2 = (WidgetRun) this.mWidgets.get(i23);
                    float f7 = f5;
                    int i25 = i21;
                    if (widgetRun2.mWidget.getVisibility() != 8 && widgetRun2.mDimensionBehavior == ConstraintWidget.DimensionBehaviour.MATCH_CONSTRAINT) {
                        DimensionDependency dimensionDependency2 = widgetRun2.mDimension;
                        if (dimensionDependency2.resolved) {
                            i22 = i22;
                            i24 = i24;
                        } else {
                            int i26 = f2 > f ? (int) (((widgetRun2.mWidget.mWeight[this.orientation] * f6) / f2) + f7) : i22;
                            if (this.orientation == 0) {
                                ConstraintWidget constraintWidget = widgetRun2.mWidget;
                                i10 = constraintWidget.mMatchConstraintMaxWidth;
                                i11 = constraintWidget.mMatchConstraintMinWidth;
                            } else {
                                ConstraintWidget constraintWidget2 = widgetRun2.mWidget;
                                i10 = constraintWidget2.mMatchConstraintMaxHeight;
                                i11 = constraintWidget2.mMatchConstraintMinHeight;
                            }
                            int i27 = i24;
                            int iMax = Math.max(i11, widgetRun2.matchConstraintsType == 1 ? Math.min(i26, dimensionDependency2.wrapValue) : i26);
                            if (i10 > 0) {
                                iMax = Math.min(i10, iMax);
                            }
                            if (iMax != i26) {
                                i24 = i27 + 1;
                                i26 = iMax;
                            } else {
                                i24 = i27;
                            }
                            widgetRun2.mDimension.resolve(i26);
                        }
                    } else {
                        i22 = i22;
                        i24 = i24;
                    }
                    i23++;
                    f5 = f7;
                    i21 = i25;
                    f6 = f6;
                    i22 = i22;
                }
                i6 = i21;
                f3 = f5;
                int i28 = i24;
                if (i28 > 0) {
                    i5 -= i28;
                    i4 = 0;
                    for (int i29 = 0; i29 < size; i29++) {
                        WidgetRun widgetRun3 = (WidgetRun) this.mWidgets.get(i29);
                        if (widgetRun3.mWidget.getVisibility() != 8) {
                            if (i29 > 0 && i29 >= i13) {
                                i4 += widgetRun3.start.mMargin;
                            }
                            i4 += widgetRun3.mDimension.value;
                            if (i29 < i14 && i29 < i) {
                                i4 += -widgetRun3.end.mMargin;
                            }
                        }
                    }
                }
                i8 = 2;
                if (this.mChainStyle == 2 && i28 == 0) {
                    i7 = 0;
                    this.mChainStyle = 0;
                } else {
                    i7 = 0;
                }
            } else {
                i6 = i21;
                f3 = 0.5f;
                i7 = 0;
                i8 = 2;
            }
            if (i4 > i12) {
                this.mChainStyle = i8;
            }
            if (i3 > 0 && i5 == 0 && i13 == i) {
                this.mChainStyle = i8;
            }
            int i30 = this.mChainStyle;
            if (i30 == 1) {
                if (i3 > 1) {
                    i9 = (i12 - i4) / (i3 - 1);
                } else {
                    i9 = i3 == 1 ? (i12 - i4) / 2 : i7;
                }
                if (i5 > 0) {
                    i9 = i7;
                }
                int i31 = i6;
                while (i7 < size) {
                    WidgetRun widgetRun4 = (WidgetRun) this.mWidgets.get(z ? size - (i7 + 1) : i7);
                    if (widgetRun4.mWidget.getVisibility() == 8) {
                        widgetRun4.start.resolve(i31);
                        widgetRun4.end.resolve(i31);
                    } else {
                        if (i7 > 0) {
                            i31 = z ? i31 - i9 : i31 + i9;
                        }
                        if (i7 > 0 && i7 >= i13) {
                            if (z) {
                                i31 -= widgetRun4.start.mMargin;
                            } else {
                                i31 += widgetRun4.start.mMargin;
                            }
                        }
                        if (z) {
                            widgetRun4.end.resolve(i31);
                        } else {
                            widgetRun4.start.resolve(i31);
                        }
                        DimensionDependency dimensionDependency3 = widgetRun4.mDimension;
                        int i32 = dimensionDependency3.value;
                        if (widgetRun4.mDimensionBehavior == ConstraintWidget.DimensionBehaviour.MATCH_CONSTRAINT && widgetRun4.matchConstraintsType == 1) {
                            i32 = dimensionDependency3.wrapValue;
                        }
                        i31 = z ? i31 - i32 : i31 + i32;
                        if (z) {
                            widgetRun4.start.resolve(i31);
                        } else {
                            widgetRun4.end.resolve(i31);
                        }
                        widgetRun4.mResolved = true;
                        if (i7 < i14 && i7 < i) {
                            if (z) {
                                i31 -= -widgetRun4.end.mMargin;
                            } else {
                                i31 += -widgetRun4.end.mMargin;
                            }
                        }
                    }
                    i7++;
                }
                return;
            }
            if (i30 == 0) {
                int i33 = (i12 - i4) / (i3 + 1);
                if (i5 > 0) {
                    i33 = i7;
                }
                int i34 = i6;
                while (i7 < size) {
                    WidgetRun widgetRun5 = (WidgetRun) this.mWidgets.get(z ? size - (i7 + 1) : i7);
                    if (widgetRun5.mWidget.getVisibility() == 8) {
                        widgetRun5.start.resolve(i34);
                        widgetRun5.end.resolve(i34);
                    } else {
                        int i35 = z ? i34 - i33 : i34 + i33;
                        if (i7 > 0 && i7 >= i13) {
                            if (z) {
                                i35 -= widgetRun5.start.mMargin;
                            } else {
                                i35 += widgetRun5.start.mMargin;
                            }
                        }
                        if (z) {
                            widgetRun5.end.resolve(i35);
                        } else {
                            widgetRun5.start.resolve(i35);
                        }
                        DimensionDependency dimensionDependency4 = widgetRun5.mDimension;
                        int iMin = dimensionDependency4.value;
                        if (widgetRun5.mDimensionBehavior == ConstraintWidget.DimensionBehaviour.MATCH_CONSTRAINT && widgetRun5.matchConstraintsType == 1) {
                            iMin = Math.min(iMin, dimensionDependency4.wrapValue);
                        }
                        i34 = z ? i35 - iMin : i35 + iMin;
                        if (z) {
                            widgetRun5.start.resolve(i34);
                        } else {
                            widgetRun5.end.resolve(i34);
                        }
                        if (i7 < i14 && i7 < i) {
                            if (z) {
                                i34 -= -widgetRun5.end.mMargin;
                            } else {
                                i34 += -widgetRun5.end.mMargin;
                            }
                        }
                    }
                    i7++;
                }
                return;
            }
            if (i30 == 2) {
                float horizontalBiasPercent = this.orientation == 0 ? this.mWidget.getHorizontalBiasPercent() : this.mWidget.getVerticalBiasPercent();
                if (z) {
                    horizontalBiasPercent = 1.0f - horizontalBiasPercent;
                }
                int i36 = (int) (((i12 - i4) * horizontalBiasPercent) + f3);
                if (i36 < 0 || i5 > 0) {
                    i36 = i7;
                }
                int i37 = z ? i6 - i36 : i6 + i36;
                while (i7 < size) {
                    WidgetRun widgetRun6 = (WidgetRun) this.mWidgets.get(z ? size - (i7 + 1) : i7);
                    if (widgetRun6.mWidget.getVisibility() == 8) {
                        widgetRun6.start.resolve(i37);
                        widgetRun6.end.resolve(i37);
                    } else {
                        if (i7 > 0 && i7 >= i13) {
                            if (z) {
                                i37 -= widgetRun6.start.mMargin;
                            } else {
                                i37 += widgetRun6.start.mMargin;
                            }
                        }
                        if (z) {
                            widgetRun6.end.resolve(i37);
                        } else {
                            widgetRun6.start.resolve(i37);
                        }
                        DimensionDependency dimensionDependency5 = widgetRun6.mDimension;
                        int i38 = dimensionDependency5.value;
                        if (widgetRun6.mDimensionBehavior == ConstraintWidget.DimensionBehaviour.MATCH_CONSTRAINT && widgetRun6.matchConstraintsType == 1) {
                            i38 = dimensionDependency5.wrapValue;
                        }
                        i37 = z ? i37 - i38 : i37 + i38;
                        if (z) {
                            widgetRun6.start.resolve(i37);
                        } else {
                            widgetRun6.end.resolve(i37);
                        }
                        if (i7 < i14 && i7 < i) {
                            if (z) {
                                i37 -= -widgetRun6.end.mMargin;
                            } else {
                                i37 += -widgetRun6.end.mMargin;
                            }
                        }
                    }
                    i7++;
                }
            }
        }
    }

    @Override // androidx.constraintlayout.core.widgets.analyzer.WidgetRun
    public void applyToWidget() {
        for (int i = 0; i < this.mWidgets.size(); i++) {
            ((WidgetRun) this.mWidgets.get(i)).applyToWidget();
        }
    }

    private ConstraintWidget getFirstVisibleWidget() {
        for (int i = 0; i < this.mWidgets.size(); i++) {
            WidgetRun widgetRun = (WidgetRun) this.mWidgets.get(i);
            if (widgetRun.mWidget.getVisibility() != 8) {
                return widgetRun.mWidget;
            }
        }
        return null;
    }

    private ConstraintWidget getLastVisibleWidget() {
        for (int size = this.mWidgets.size() - 1; size >= 0; size--) {
            WidgetRun widgetRun = (WidgetRun) this.mWidgets.get(size);
            if (widgetRun.mWidget.getVisibility() != 8) {
                return widgetRun.mWidget;
            }
        }
        return null;
    }

    @Override // androidx.constraintlayout.core.widgets.analyzer.WidgetRun
    void apply() {
        ArrayList arrayList = this.mWidgets;
        int size = arrayList.size();
        int i = 0;
        while (i < size) {
            Object obj = arrayList.get(i);
            i++;
            ((WidgetRun) obj).apply();
        }
        int size2 = this.mWidgets.size();
        if (size2 < 1) {
            return;
        }
        ConstraintWidget constraintWidget = ((WidgetRun) this.mWidgets.get(0)).mWidget;
        ConstraintWidget constraintWidget2 = ((WidgetRun) this.mWidgets.get(size2 - 1)).mWidget;
        if (this.orientation == 0) {
            ConstraintAnchor constraintAnchor = constraintWidget.mLeft;
            ConstraintAnchor constraintAnchor2 = constraintWidget2.mRight;
            DependencyNode target = getTarget(constraintAnchor, 0);
            int margin = constraintAnchor.getMargin();
            ConstraintWidget firstVisibleWidget = getFirstVisibleWidget();
            if (firstVisibleWidget != null) {
                margin = firstVisibleWidget.mLeft.getMargin();
            }
            if (target != null) {
                addTarget(this.start, target, margin);
            }
            DependencyNode target2 = getTarget(constraintAnchor2, 0);
            int margin2 = constraintAnchor2.getMargin();
            ConstraintWidget lastVisibleWidget = getLastVisibleWidget();
            if (lastVisibleWidget != null) {
                margin2 = lastVisibleWidget.mRight.getMargin();
            }
            if (target2 != null) {
                addTarget(this.end, target2, -margin2);
            }
        } else {
            ConstraintAnchor constraintAnchor3 = constraintWidget.mTop;
            ConstraintAnchor constraintAnchor4 = constraintWidget2.mBottom;
            DependencyNode target3 = getTarget(constraintAnchor3, 1);
            int margin3 = constraintAnchor3.getMargin();
            ConstraintWidget firstVisibleWidget2 = getFirstVisibleWidget();
            if (firstVisibleWidget2 != null) {
                margin3 = firstVisibleWidget2.mTop.getMargin();
            }
            if (target3 != null) {
                addTarget(this.start, target3, margin3);
            }
            DependencyNode target4 = getTarget(constraintAnchor4, 1);
            int margin4 = constraintAnchor4.getMargin();
            ConstraintWidget lastVisibleWidget2 = getLastVisibleWidget();
            if (lastVisibleWidget2 != null) {
                margin4 = lastVisibleWidget2.mBottom.getMargin();
            }
            if (target4 != null) {
                addTarget(this.end, target4, -margin4);
            }
        }
        this.start.updateDelegate = this;
        this.end.updateDelegate = this;
    }
}
