package androidx.constraintlayout.core.widgets;

import androidx.constraintlayout.core.Cache;
import androidx.constraintlayout.core.LinearSystem;
import androidx.constraintlayout.core.SolverVariable;
import androidx.constraintlayout.core.state.WidgetFrame;
import androidx.constraintlayout.core.widgets.analyzer.ChainRun;
import androidx.constraintlayout.core.widgets.analyzer.DependencyNode;
import androidx.constraintlayout.core.widgets.analyzer.HorizontalWidgetRun;
import androidx.constraintlayout.core.widgets.analyzer.VerticalWidgetRun;
import androidx.constraintlayout.core.widgets.analyzer.WidgetRun;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
import okhttp3.internal.url._UrlKt;

public class ConstraintWidget {
    public static float DEFAULT_BIAS = 0.5f;
    public ChainRun horizontalChainRun;
    public int horizontalGroup;
    protected ArrayList mAnchors;
    private boolean mAnimated;
    int mBaselineDistance;
    public ConstraintAnchor mCenter;
    private Object mCompanionWidget;
    private int mContainerItemSkip;
    private String mDebugName;
    public float mDimensionRatio;
    protected int mDimensionRatioSide;
    boolean mGroupsToSolver;
    int mHeight;
    float mHorizontalBiasPercent;
    boolean mHorizontalChainFixedPosition;
    int mHorizontalChainStyle;
    ConstraintWidget mHorizontalNextWidget;
    boolean mHorizontalWrapVisited;
    private boolean mInPlaceholder;
    private boolean[] mIsInBarrier;
    public ConstraintAnchor[] mListAnchors;
    public DimensionBehaviour[] mListDimensionBehaviors;
    protected ConstraintWidget[] mListNextMatchConstraintsWidget;
    protected int mMinHeight;
    protected int mMinWidth;
    protected ConstraintWidget[] mNextChainWidget;
    protected int mOffsetX;
    protected int mOffsetY;
    public ConstraintWidget mParent;
    int mRelX;
    int mRelY;
    private String mType;
    float mVerticalBiasPercent;
    boolean mVerticalChainFixedPosition;
    int mVerticalChainStyle;
    ConstraintWidget mVerticalNextWidget;
    boolean mVerticalWrapVisited;
    private int mVisibility;
    public float[] mWeight;
    int mWidth;
    protected int mX;
    protected int mY;
    public String stringId;
    public ChainRun verticalChainRun;
    public int verticalGroup;
    public boolean measured = false;
    public WidgetRun[] run = new WidgetRun[2];
    public HorizontalWidgetRun mHorizontalRun = null;
    public VerticalWidgetRun mVerticalRun = null;
    public boolean[] isTerminalWidget = {true, true};
    boolean mResolvedHasRatio = false;
    private boolean mMeasureRequested = true;
    private boolean mOptimizeWrapO = false;
    private boolean mOptimizeWrapOnResolved = true;
    private int mWidthOverride = -1;
    private int mHeightOverride = -1;
    public WidgetFrame frame = new WidgetFrame(this);
    private boolean mResolvedHorizontal = false;
    private boolean mResolvedVertical = false;
    private boolean mHorizontalSolvingPass = false;
    private boolean mVerticalSolvingPass = false;
    public int mHorizontalResolution = -1;
    public int mVerticalResolution = -1;
    private int mWrapBehaviorInParent = 0;
    public int mMatchConstraintDefaultWidth = 0;
    public int mMatchConstraintDefaultHeight = 0;
    public int[] mResolvedMatchConstraintDefault = new int[2];
    public int mMatchConstraintMinWidth = 0;
    public int mMatchConstraintMaxWidth = 0;
    public float mMatchConstraintPercentWidth = 1.0f;
    public int mMatchConstraintMinHeight = 0;
    public int mMatchConstraintMaxHeight = 0;
    public float mMatchConstraintPercentHeight = 1.0f;
    int mResolvedDimensionRatioSide = -1;
    float mResolvedDimensionRatio = 1.0f;
    private int[] mMaxDimension = {Integer.MAX_VALUE, Integer.MAX_VALUE};
    public float mCircleConstraintAngle = Float.NaN;
    private boolean mHasBaseline = false;
    private boolean mInVirtualLayout = false;
    private int mLastHorizontalMeasureSpec = 0;
    private int mLastVerticalMeasureSpec = 0;
    public ConstraintAnchor mLeft = new ConstraintAnchor(this, ConstraintAnchor.Type.LEFT);
    public ConstraintAnchor mTop = new ConstraintAnchor(this, ConstraintAnchor.Type.TOP);
    public ConstraintAnchor mRight = new ConstraintAnchor(this, ConstraintAnchor.Type.RIGHT);
    public ConstraintAnchor mBottom = new ConstraintAnchor(this, ConstraintAnchor.Type.BOTTOM);
    public ConstraintAnchor mBaseline = new ConstraintAnchor(this, ConstraintAnchor.Type.BASELINE);
    ConstraintAnchor mCenterX = new ConstraintAnchor(this, ConstraintAnchor.Type.CENTER_X);
    ConstraintAnchor mCenterY = new ConstraintAnchor(this, ConstraintAnchor.Type.CENTER_Y);

    public enum DimensionBehaviour {
        FIXED,
        WRAP_CONTENT,
        MATCH_CONSTRAINT,
        MATCH_PARENT
    }

    public WidgetRun getRun(int i) {
        if (i == 0) {
            return this.mHorizontalRun;
        }
        if (i == 1) {
            return this.mVerticalRun;
        }
        return null;
    }

    public void setFinalLeft(int i) {
        this.mLeft.setFinalValue(i);
        this.mX = i;
    }

    public void setFinalTop(int i) {
        this.mTop.setFinalValue(i);
        this.mY = i;
    }

    public boolean isHorizontalSolvingPassDone() {
        return this.mHorizontalSolvingPass;
    }

    public boolean isVerticalSolvingPassDone() {
        return this.mVerticalSolvingPass;
    }

    public void markHorizontalSolvingPassDone() {
        this.mHorizontalSolvingPass = true;
    }

    public void markVerticalSolvingPassDone() {
        this.mVerticalSolvingPass = true;
    }

    public void setFinalHorizontal(int i, int i2) {
        if (this.mResolvedHorizontal) {
            return;
        }
        this.mLeft.setFinalValue(i);
        this.mRight.setFinalValue(i2);
        this.mX = i;
        this.mWidth = i2 - i;
        this.mResolvedHorizontal = true;
    }

    public void setFinalVertical(int i, int i2) {
        if (this.mResolvedVertical) {
            return;
        }
        this.mTop.setFinalValue(i);
        this.mBottom.setFinalValue(i2);
        this.mY = i;
        this.mHeight = i2 - i;
        if (this.mHasBaseline) {
            this.mBaseline.setFinalValue(i + this.mBaselineDistance);
        }
        this.mResolvedVertical = true;
    }

    public void setFinalBaseline(int i) {
        if (this.mHasBaseline) {
            int i2 = i - this.mBaselineDistance;
            int i3 = this.mHeight + i2;
            this.mY = i2;
            this.mTop.setFinalValue(i2);
            this.mBottom.setFinalValue(i3);
            this.mBaseline.setFinalValue(i);
            this.mResolvedVertical = true;
        }
    }

    public boolean isResolvedHorizontally() {
        if (this.mResolvedHorizontal) {
            return true;
        }
        return this.mLeft.hasFinalValue() && this.mRight.hasFinalValue();
    }

    public boolean isResolvedVertically() {
        if (this.mResolvedVertical) {
            return true;
        }
        return this.mTop.hasFinalValue() && this.mBottom.hasFinalValue();
    }

    public void resetFinalResolution() {
        this.mResolvedHorizontal = false;
        this.mResolvedVertical = false;
        this.mHorizontalSolvingPass = false;
        this.mVerticalSolvingPass = false;
        int size = this.mAnchors.size();
        for (int i = 0; i < size; i++) {
            ((ConstraintAnchor) this.mAnchors.get(i)).resetFinalResolution();
        }
    }

    public boolean hasDependencies() {
        int size = this.mAnchors.size();
        for (int i = 0; i < size; i++) {
            if (((ConstraintAnchor) this.mAnchors.get(i)).hasDependents()) {
                return true;
            }
        }
        return false;
    }

    public boolean hasDanglingDimension(int i) {
        if (i == 0) {
            return (this.mLeft.mTarget != null ? 1 : 0) + (this.mRight.mTarget != null ? 1 : 0) < 2;
        }
        return ((this.mTop.mTarget != null ? 1 : 0) + (this.mBottom.mTarget != null ? 1 : 0)) + (this.mBaseline.mTarget != null ? 1 : 0) < 2;
    }

    public boolean hasResolvedTargets(int i, int i2) {
        ConstraintAnchor constraintAnchor;
        ConstraintAnchor constraintAnchor2;
        if (i == 0) {
            ConstraintAnchor constraintAnchor3 = this.mLeft.mTarget;
            return constraintAnchor3 != null && constraintAnchor3.hasFinalValue() && (constraintAnchor2 = this.mRight.mTarget) != null && constraintAnchor2.hasFinalValue() && (this.mRight.mTarget.getFinalValue() - this.mRight.getMargin()) - (this.mLeft.mTarget.getFinalValue() + this.mLeft.getMargin()) >= i2;
        }
        ConstraintAnchor constraintAnchor4 = this.mTop.mTarget;
        if (constraintAnchor4 != null && constraintAnchor4.hasFinalValue() && (constraintAnchor = this.mBottom.mTarget) != null && constraintAnchor.hasFinalValue() && (this.mBottom.mTarget.getFinalValue() - this.mBottom.getMargin()) - (this.mTop.mTarget.getFinalValue() + this.mTop.getMargin()) >= i2) {
            return true;
        }
        return false;
    }

    public boolean isInVirtualLayout() {
        return this.mInVirtualLayout;
    }

    public void setInVirtualLayout(boolean z) {
        this.mInVirtualLayout = z;
    }

    public int getMaxHeight() {
        return this.mMaxDimension[1];
    }

    public int getMaxWidth() {
        return this.mMaxDimension[0];
    }

    public void setMaxWidth(int i) {
        this.mMaxDimension[0] = i;
    }

    public void setMaxHeight(int i) {
        this.mMaxDimension[1] = i;
    }

    public void setHasBaseline(boolean z) {
        this.mHasBaseline = z;
    }

    public boolean isInPlaceholder() {
        return this.mInPlaceholder;
    }

    public void setInPlaceholder(boolean z) {
        this.mInPlaceholder = z;
    }

    protected void setInBarrier(int i, boolean z) {
        this.mIsInBarrier[i] = z;
    }

    public boolean isInBarrier(int i) {
        return this.mIsInBarrier[i];
    }

    public void setMeasureRequested(boolean z) {
        this.mMeasureRequested = z;
    }

    public boolean isMeasureRequested() {
        return this.mMeasureRequested && this.mVisibility != 8;
    }

    public void setWrapBehaviorInParent(int i) {
        if (i < 0 || i > 3) {
            return;
        }
        this.mWrapBehaviorInParent = i;
    }

    public int getLastHorizontalMeasureSpec() {
        return this.mLastHorizontalMeasureSpec;
    }

    public int getLastVerticalMeasureSpec() {
        return this.mLastVerticalMeasureSpec;
    }

    public void setLastMeasureSpec(int i, int i2) {
        this.mLastHorizontalMeasureSpec = i;
        this.mLastVerticalMeasureSpec = i2;
        setMeasureRequested(false);
    }

    public void reset() {
        this.mLeft.reset();
        this.mTop.reset();
        this.mRight.reset();
        this.mBottom.reset();
        this.mBaseline.reset();
        this.mCenterX.reset();
        this.mCenterY.reset();
        this.mCenter.reset();
        this.mParent = null;
        this.mCircleConstraintAngle = Float.NaN;
        this.mWidth = 0;
        this.mHeight = 0;
        this.mDimensionRatio = 0.0f;
        this.mDimensionRatioSide = -1;
        this.mX = 0;
        this.mY = 0;
        this.mOffsetX = 0;
        this.mOffsetY = 0;
        this.mBaselineDistance = 0;
        this.mMinWidth = 0;
        this.mMinHeight = 0;
        float f = DEFAULT_BIAS;
        this.mHorizontalBiasPercent = f;
        this.mVerticalBiasPercent = f;
        DimensionBehaviour[] dimensionBehaviourArr = this.mListDimensionBehaviors;
        DimensionBehaviour dimensionBehaviour = DimensionBehaviour.FIXED;
        dimensionBehaviourArr[0] = dimensionBehaviour;
        dimensionBehaviourArr[1] = dimensionBehaviour;
        this.mCompanionWidget = null;
        this.mContainerItemSkip = 0;
        this.mVisibility = 0;
        this.mType = null;
        this.mHorizontalWrapVisited = false;
        this.mVerticalWrapVisited = false;
        this.mHorizontalChainStyle = 0;
        this.mVerticalChainStyle = 0;
        this.mHorizontalChainFixedPosition = false;
        this.mVerticalChainFixedPosition = false;
        float[] fArr = this.mWeight;
        fArr[0] = -1.0f;
        fArr[1] = -1.0f;
        this.mHorizontalResolution = -1;
        this.mVerticalResolution = -1;
        int[] iArr = this.mMaxDimension;
        iArr[0] = Integer.MAX_VALUE;
        iArr[1] = Integer.MAX_VALUE;
        this.mMatchConstraintDefaultWidth = 0;
        this.mMatchConstraintDefaultHeight = 0;
        this.mMatchConstraintPercentWidth = 1.0f;
        this.mMatchConstraintPercentHeight = 1.0f;
        this.mMatchConstraintMaxWidth = Integer.MAX_VALUE;
        this.mMatchConstraintMaxHeight = Integer.MAX_VALUE;
        this.mMatchConstraintMinWidth = 0;
        this.mMatchConstraintMinHeight = 0;
        this.mResolvedHasRatio = false;
        this.mResolvedDimensionRatioSide = -1;
        this.mResolvedDimensionRatio = 1.0f;
        this.mGroupsToSolver = false;
        boolean[] zArr = this.isTerminalWidget;
        zArr[0] = true;
        zArr[1] = true;
        this.mInVirtualLayout = false;
        boolean[] zArr2 = this.mIsInBarrier;
        zArr2[0] = false;
        zArr2[1] = false;
        this.mMeasureRequested = true;
        int[] iArr2 = this.mResolvedMatchConstraintDefault;
        iArr2[0] = 0;
        iArr2[1] = 0;
        this.mWidthOverride = -1;
        this.mHeightOverride = -1;
    }

    private void serializeAttribute(StringBuilder sb, String str, float f, float f2) {
        if (f == f2) {
            return;
        }
        sb.append(str);
        sb.append(" :   ");
        sb.append(f);
        sb.append(",\n");
    }

    private void serializeAttribute(StringBuilder sb, String str, int i, int i2) {
        if (i == i2) {
            return;
        }
        sb.append(str);
        sb.append(" :   ");
        sb.append(i);
        sb.append(",\n");
    }

    private void serializeAttribute(StringBuilder sb, String str, String str2, String str3) {
        if (str3.equals(str2)) {
            return;
        }
        sb.append(str);
        sb.append(" :   ");
        sb.append(str2);
        sb.append(",\n");
    }

    private void serializeDimensionRatio(StringBuilder sb, String str, float f, int i) {
        if (f == 0.0f) {
            return;
        }
        sb.append(str);
        sb.append(" :  [");
        sb.append(f);
        sb.append(",");
        sb.append(i);
        sb.append(_UrlKt.FRAGMENT_ENCODE_SET);
        sb.append("],\n");
    }

    public boolean oppositeDimensionsTied() {
        DimensionBehaviour[] dimensionBehaviourArr = this.mListDimensionBehaviors;
        DimensionBehaviour dimensionBehaviour = dimensionBehaviourArr[0];
        DimensionBehaviour dimensionBehaviour2 = DimensionBehaviour.MATCH_CONSTRAINT;
        return dimensionBehaviour == dimensionBehaviour2 && dimensionBehaviourArr[1] == dimensionBehaviour2;
    }

    public boolean hasDimensionOverride() {
        return (this.mWidthOverride == -1 && this.mHeightOverride == -1) ? false : true;
    }

    public ConstraintWidget() {
        ConstraintAnchor constraintAnchor = new ConstraintAnchor(this, ConstraintAnchor.Type.CENTER);
        this.mCenter = constraintAnchor;
        this.mListAnchors = new ConstraintAnchor[]{this.mLeft, this.mRight, this.mTop, this.mBottom, this.mBaseline, constraintAnchor};
        this.mAnchors = new ArrayList();
        this.mIsInBarrier = new boolean[2];
        DimensionBehaviour dimensionBehaviour = DimensionBehaviour.FIXED;
        this.mListDimensionBehaviors = new DimensionBehaviour[]{dimensionBehaviour, dimensionBehaviour};
        this.mParent = null;
        this.mWidth = 0;
        this.mHeight = 0;
        this.mDimensionRatio = 0.0f;
        this.mDimensionRatioSide = -1;
        this.mX = 0;
        this.mY = 0;
        this.mRelX = 0;
        this.mRelY = 0;
        this.mOffsetX = 0;
        this.mOffsetY = 0;
        this.mBaselineDistance = 0;
        float f = DEFAULT_BIAS;
        this.mHorizontalBiasPercent = f;
        this.mVerticalBiasPercent = f;
        this.mContainerItemSkip = 0;
        this.mVisibility = 0;
        this.mAnimated = false;
        this.mDebugName = null;
        this.mType = null;
        this.mGroupsToSolver = false;
        this.mHorizontalChainStyle = 0;
        this.mVerticalChainStyle = 0;
        this.mWeight = new float[]{-1.0f, -1.0f};
        this.mListNextMatchConstraintsWidget = new ConstraintWidget[]{null, null};
        this.mNextChainWidget = new ConstraintWidget[]{null, null};
        this.mHorizontalNextWidget = null;
        this.mVerticalNextWidget = null;
        this.horizontalGroup = -1;
        this.verticalGroup = -1;
        addAnchors();
    }

    public void ensureWidgetRuns() {
        if (this.mHorizontalRun == null) {
            this.mHorizontalRun = new HorizontalWidgetRun(this);
        }
        if (this.mVerticalRun == null) {
            this.mVerticalRun = new VerticalWidgetRun(this);
        }
    }

    public void resetSolverVariables(Cache cache) {
        this.mLeft.resetSolverVariable(cache);
        this.mTop.resetSolverVariable(cache);
        this.mRight.resetSolverVariable(cache);
        this.mBottom.resetSolverVariable(cache);
        this.mBaseline.resetSolverVariable(cache);
        this.mCenter.resetSolverVariable(cache);
        this.mCenterX.resetSolverVariable(cache);
        this.mCenterY.resetSolverVariable(cache);
    }

    private void addAnchors() {
        this.mAnchors.add(this.mLeft);
        this.mAnchors.add(this.mTop);
        this.mAnchors.add(this.mRight);
        this.mAnchors.add(this.mBottom);
        this.mAnchors.add(this.mCenterX);
        this.mAnchors.add(this.mCenterY);
        this.mAnchors.add(this.mCenter);
        this.mAnchors.add(this.mBaseline);
    }

    public ConstraintWidget getParent() {
        return this.mParent;
    }

    public void setParent(ConstraintWidget constraintWidget) {
        this.mParent = constraintWidget;
    }

    public void connectCircularConstraint(ConstraintWidget constraintWidget, float f, int i) {
        ConstraintAnchor.Type type = ConstraintAnchor.Type.CENTER;
        immediateConnect(type, constraintWidget, type, i, 0);
        this.mCircleConstraintAngle = f;
    }

    public void setVisibility(int i) {
        this.mVisibility = i;
    }

    public int getVisibility() {
        return this.mVisibility;
    }

    public String getDebugName() {
        return this.mDebugName;
    }

    public void setDebugName(String str) {
        this.mDebugName = str;
    }

    public void createObjectVariables(LinearSystem linearSystem) {
        linearSystem.createObjectVariable(this.mLeft);
        linearSystem.createObjectVariable(this.mTop);
        linearSystem.createObjectVariable(this.mRight);
        linearSystem.createObjectVariable(this.mBottom);
        if (this.mBaselineDistance > 0) {
            linearSystem.createObjectVariable(this.mBaseline);
        }
    }

    public String toString() {
        String str;
        StringBuilder sb = new StringBuilder();
        String str2 = this.mType;
        String str3 = _UrlKt.FRAGMENT_ENCODE_SET;
        if (str2 != null) {
            str = "type: " + this.mType + " ";
        } else {
            str = _UrlKt.FRAGMENT_ENCODE_SET;
        }
        sb.append(str);
        if (this.mDebugName != null) {
            str3 = "id: " + this.mDebugName + " ";
        }
        sb.append(str3);
        sb.append("(");
        sb.append(this.mX);
        sb.append(", ");
        sb.append(this.mY);
        sb.append(") - (");
        sb.append(this.mWidth);
        sb.append(" x ");
        sb.append(this.mHeight);
        sb.append(")");
        return sb.toString();
    }

    public int getX() {
        ConstraintWidget constraintWidget = this.mParent;
        if (constraintWidget != null && (constraintWidget instanceof ConstraintWidgetContainer)) {
            return ((ConstraintWidgetContainer) constraintWidget).mPaddingLeft + this.mX;
        }
        return this.mX;
    }

    public int getY() {
        ConstraintWidget constraintWidget = this.mParent;
        if (constraintWidget != null && (constraintWidget instanceof ConstraintWidgetContainer)) {
            return ((ConstraintWidgetContainer) constraintWidget).mPaddingTop + this.mY;
        }
        return this.mY;
    }

    public int getWidth() {
        if (this.mVisibility == 8) {
            return 0;
        }
        return this.mWidth;
    }

    public int getHeight() {
        if (this.mVisibility == 8) {
            return 0;
        }
        return this.mHeight;
    }

    public int getLength(int i) {
        if (i == 0) {
            return getWidth();
        }
        if (i == 1) {
            return getHeight();
        }
        return 0;
    }

    public int getMinWidth() {
        return this.mMinWidth;
    }

    public int getMinHeight() {
        return this.mMinHeight;
    }

    public int getRight() {
        return getX() + this.mWidth;
    }

    public int getBottom() {
        return getY() + this.mHeight;
    }

    public int getHorizontalMargin() {
        ConstraintAnchor constraintAnchor = this.mLeft;
        int i = constraintAnchor != null ? constraintAnchor.mMargin : 0;
        ConstraintAnchor constraintAnchor2 = this.mRight;
        return constraintAnchor2 != null ? i + constraintAnchor2.mMargin : i;
    }

    public int getVerticalMargin() {
        int i = this.mLeft != null ? this.mTop.mMargin : 0;
        return this.mRight != null ? i + this.mBottom.mMargin : i;
    }

    public float getHorizontalBiasPercent() {
        return this.mHorizontalBiasPercent;
    }

    public float getVerticalBiasPercent() {
        return this.mVerticalBiasPercent;
    }

    public float getBiasPercent(int i) {
        if (i == 0) {
            return this.mHorizontalBiasPercent;
        }
        if (i == 1) {
            return this.mVerticalBiasPercent;
        }
        return -1.0f;
    }

    public boolean hasBaseline() {
        return this.mHasBaseline;
    }

    public int getBaselineDistance() {
        return this.mBaselineDistance;
    }

    public Object getCompanionWidget() {
        return this.mCompanionWidget;
    }

    public void setX(int i) {
        this.mX = i;
    }

    public void setY(int i) {
        this.mY = i;
    }

    public void setOrigin(int i, int i2) {
        this.mX = i;
        this.mY = i2;
    }

    /* JADX INFO: renamed from: androidx.constraintlayout.core.widgets.ConstraintWidget$1, reason: invalid class name */
    static /* synthetic */ class AnonymousClass1 {
        static final /* synthetic */ int[] $SwitchMap$androidx$constraintlayout$core$widgets$ConstraintAnchor$Type;

        static {
            int[] iArr = new int[ConstraintAnchor.Type.values().length];
            $SwitchMap$androidx$constraintlayout$core$widgets$ConstraintAnchor$Type = iArr;
            try {
                iArr[ConstraintAnchor.Type.LEFT.ordinal()] = 1;
            } catch (NoSuchFieldError unused) {
            }
            try {
                $SwitchMap$androidx$constraintlayout$core$widgets$ConstraintAnchor$Type[ConstraintAnchor.Type.TOP.ordinal()] = 2;
            } catch (NoSuchFieldError unused2) {
            }
            try {
                $SwitchMap$androidx$constraintlayout$core$widgets$ConstraintAnchor$Type[ConstraintAnchor.Type.RIGHT.ordinal()] = 3;
            } catch (NoSuchFieldError unused3) {
            }
            try {
                $SwitchMap$androidx$constraintlayout$core$widgets$ConstraintAnchor$Type[ConstraintAnchor.Type.BOTTOM.ordinal()] = 4;
            } catch (NoSuchFieldError unused4) {
            }
            try {
                $SwitchMap$androidx$constraintlayout$core$widgets$ConstraintAnchor$Type[ConstraintAnchor.Type.BASELINE.ordinal()] = 5;
            } catch (NoSuchFieldError unused5) {
            }
            try {
                $SwitchMap$androidx$constraintlayout$core$widgets$ConstraintAnchor$Type[ConstraintAnchor.Type.CENTER.ordinal()] = 6;
            } catch (NoSuchFieldError unused6) {
            }
            try {
                $SwitchMap$androidx$constraintlayout$core$widgets$ConstraintAnchor$Type[ConstraintAnchor.Type.CENTER_X.ordinal()] = 7;
            } catch (NoSuchFieldError unused7) {
            }
            try {
                $SwitchMap$androidx$constraintlayout$core$widgets$ConstraintAnchor$Type[ConstraintAnchor.Type.CENTER_Y.ordinal()] = 8;
            } catch (NoSuchFieldError unused8) {
            }
            try {
                $SwitchMap$androidx$constraintlayout$core$widgets$ConstraintAnchor$Type[ConstraintAnchor.Type.NONE.ordinal()] = 9;
            } catch (NoSuchFieldError unused9) {
            }
        }
    }

    public void setWidth(int i) {
        this.mWidth = i;
        int i2 = this.mMinWidth;
        if (i < i2) {
            this.mWidth = i2;
        }
    }

    public void setHeight(int i) {
        this.mHeight = i;
        int i2 = this.mMinHeight;
        if (i < i2) {
            this.mHeight = i2;
        }
    }

    public void setHorizontalMatchStyle(int i, int i2, int i3, float f) {
        this.mMatchConstraintDefaultWidth = i;
        this.mMatchConstraintMinWidth = i2;
        if (i3 == Integer.MAX_VALUE) {
            i3 = 0;
        }
        this.mMatchConstraintMaxWidth = i3;
        this.mMatchConstraintPercentWidth = f;
        if (f <= 0.0f || f >= 1.0f || i != 0) {
            return;
        }
        this.mMatchConstraintDefaultWidth = 2;
    }

    public void setVerticalMatchStyle(int i, int i2, int i3, float f) {
        this.mMatchConstraintDefaultHeight = i;
        this.mMatchConstraintMinHeight = i2;
        if (i3 == Integer.MAX_VALUE) {
            i3 = 0;
        }
        this.mMatchConstraintMaxHeight = i3;
        this.mMatchConstraintPercentHeight = f;
        if (f <= 0.0f || f >= 1.0f || i != 0) {
            return;
        }
        this.mMatchConstraintDefaultHeight = 2;
    }

    /* JADX WARN: Code duplicated, block: B:39:0x0086 A[PHI: r0
  0x0086: PHI (r0v2 int) = (r0v1 int), (r0v0 int), (r0v0 int), (r0v0 int), (r0v0 int), (r0v0 int) binds: [B:46:0x0086, B:36:0x007f, B:24:0x0051, B:26:0x0057, B:28:0x0063, B:30:0x0067] A[DONT_GENERATE, DONT_INLINE]] */
    /* JADX WARN: Unsupported multi-entry loop pattern (BACK_EDGE: B:39:0x0086 -> B:40:0x0087). Please report as a decompilation issue!!! */
    public void setDimensionRatio(String str) {
        float fAbs;
        int i = 0;
        if (str == null || str.length() == 0) {
            this.mDimensionRatio = 0.0f;
            return;
        }
        int length = str.length();
        int iIndexOf = str.indexOf(44);
        int i2 = 0;
        int i3 = -1;
        if (iIndexOf > 0 && iIndexOf < length - 1) {
            String strSubstring = str.substring(0, iIndexOf);
            if (!strSubstring.equalsIgnoreCase("W")) {
                i2 = strSubstring.equalsIgnoreCase("H") ? 1 : -1;
            }
            i3 = i2;
            i2 = iIndexOf + 1;
        }
        int iIndexOf2 = str.indexOf(58);
        try {
            if (iIndexOf2 >= 0 && iIndexOf2 < length - 1) {
                String strSubstring2 = str.substring(i2, iIndexOf2);
                String strSubstring3 = str.substring(iIndexOf2 + 1);
                if (strSubstring2.length() <= 0 || strSubstring3.length() <= 0) {
                    fAbs = i;
                } else {
                    float f = Float.parseFloat(strSubstring2);
                    float f2 = Float.parseFloat(strSubstring3);
                    if (f <= 0.0f || f2 <= 0.0f) {
                        fAbs = i;
                    } else if (i3 == 1) {
                        fAbs = Math.abs(f2 / f);
                    } else {
                        fAbs = Math.abs(f / f2);
                    }
                }
            } else {
                String strSubstring4 = str.substring(i2);
                if (strSubstring4.length() > 0) {
                    fAbs = Float.parseFloat(strSubstring4);
                } else {
                    fAbs = i;
                }
            }
        } catch (NumberFormatException unused) {
        }
        i = (fAbs > i ? 1 : (fAbs == i ? 0 : -1));
        if (i > 0) {
            this.mDimensionRatio = fAbs;
            this.mDimensionRatioSide = i3;
        }
    }

    public float getDimensionRatio() {
        return this.mDimensionRatio;
    }

    public int getDimensionRatioSide() {
        return this.mDimensionRatioSide;
    }

    public void setHorizontalBiasPercent(float f) {
        this.mHorizontalBiasPercent = f;
    }

    public void setVerticalBiasPercent(float f) {
        this.mVerticalBiasPercent = f;
    }

    public void setMinWidth(int i) {
        if (i < 0) {
            this.mMinWidth = 0;
        } else {
            this.mMinWidth = i;
        }
    }

    public void setMinHeight(int i) {
        if (i < 0) {
            this.mMinHeight = 0;
        } else {
            this.mMinHeight = i;
        }
    }

    public void setFrame(int i, int i2, int i3, int i4) {
        int i5;
        int i6;
        int i7 = i3 - i;
        int i8 = i4 - i2;
        this.mX = i;
        this.mY = i2;
        if (this.mVisibility == 8) {
            this.mWidth = 0;
            this.mHeight = 0;
            return;
        }
        DimensionBehaviour[] dimensionBehaviourArr = this.mListDimensionBehaviors;
        DimensionBehaviour dimensionBehaviour = dimensionBehaviourArr[0];
        DimensionBehaviour dimensionBehaviour2 = DimensionBehaviour.FIXED;
        if (dimensionBehaviour == dimensionBehaviour2 && i7 < (i6 = this.mWidth)) {
            i7 = i6;
        }
        if (dimensionBehaviourArr[1] == dimensionBehaviour2 && i8 < (i5 = this.mHeight)) {
            i8 = i5;
        }
        this.mWidth = i7;
        this.mHeight = i8;
        int i9 = this.mMinHeight;
        if (i8 < i9) {
            this.mHeight = i9;
        }
        int i10 = this.mMinWidth;
        if (i7 < i10) {
            this.mWidth = i10;
        }
        int i11 = this.mMatchConstraintMaxWidth;
        if (i11 > 0 && dimensionBehaviour == DimensionBehaviour.MATCH_CONSTRAINT) {
            this.mWidth = Math.min(this.mWidth, i11);
        }
        int i12 = this.mMatchConstraintMaxHeight;
        if (i12 > 0 && this.mListDimensionBehaviors[1] == DimensionBehaviour.MATCH_CONSTRAINT) {
            this.mHeight = Math.min(this.mHeight, i12);
        }
        int i13 = this.mWidth;
        if (i7 != i13) {
            this.mWidthOverride = i13;
        }
        int i14 = this.mHeight;
        if (i8 != i14) {
            this.mHeightOverride = i14;
        }
    }

    public void setHorizontalDimension(int i, int i2) {
        this.mX = i;
        int i3 = i2 - i;
        this.mWidth = i3;
        int i4 = this.mMinWidth;
        if (i3 < i4) {
            this.mWidth = i4;
        }
    }

    public void setVerticalDimension(int i, int i2) {
        this.mY = i;
        int i3 = i2 - i;
        this.mHeight = i3;
        int i4 = this.mMinHeight;
        if (i3 < i4) {
            this.mHeight = i4;
        }
    }

    public void setBaselineDistance(int i) {
        this.mBaselineDistance = i;
        this.mHasBaseline = i > 0;
    }

    public void setCompanionWidget(Object obj) {
        this.mCompanionWidget = obj;
    }

    public void setHorizontalWeight(float f) {
        this.mWeight[0] = f;
    }

    public void setVerticalWeight(float f) {
        this.mWeight[1] = f;
    }

    public void setHorizontalChainStyle(int i) {
        this.mHorizontalChainStyle = i;
    }

    public int getHorizontalChainStyle() {
        return this.mHorizontalChainStyle;
    }

    public void setVerticalChainStyle(int i) {
        this.mVerticalChainStyle = i;
    }

    public int getVerticalChainStyle() {
        return this.mVerticalChainStyle;
    }

    public boolean allowedInBarrier() {
        return this.mVisibility != 8;
    }

    public void immediateConnect(ConstraintAnchor.Type type, ConstraintWidget constraintWidget, ConstraintAnchor.Type type2, int i, int i2) {
        getAnchor(type).connect(constraintWidget.getAnchor(type2), i, i2, true);
    }

    public void connect(ConstraintAnchor constraintAnchor, ConstraintAnchor constraintAnchor2, int i) {
        if (constraintAnchor.getOwner() == this) {
            connect(constraintAnchor.getType(), constraintAnchor2.getOwner(), constraintAnchor2.getType(), i);
        }
    }

    public void connect(ConstraintAnchor.Type type, ConstraintWidget constraintWidget, ConstraintAnchor.Type type2, int i) {
        ConstraintAnchor.Type type3;
        ConstraintAnchor.Type type4;
        boolean z;
        ConstraintAnchor.Type type5 = ConstraintAnchor.Type.CENTER;
        if (type == type5) {
            if (type2 == type5) {
                ConstraintAnchor.Type type6 = ConstraintAnchor.Type.LEFT;
                ConstraintAnchor anchor = getAnchor(type6);
                ConstraintAnchor.Type type7 = ConstraintAnchor.Type.RIGHT;
                ConstraintAnchor anchor2 = getAnchor(type7);
                ConstraintAnchor.Type type8 = ConstraintAnchor.Type.TOP;
                ConstraintAnchor anchor3 = getAnchor(type8);
                ConstraintAnchor.Type type9 = ConstraintAnchor.Type.BOTTOM;
                ConstraintAnchor anchor4 = getAnchor(type9);
                boolean z2 = true;
                if ((anchor == null || !anchor.isConnected()) && (anchor2 == null || !anchor2.isConnected())) {
                    connect(type6, constraintWidget, type6, 0);
                    connect(type7, constraintWidget, type7, 0);
                    z = true;
                } else {
                    z = false;
                }
                if ((anchor3 == null || !anchor3.isConnected()) && (anchor4 == null || !anchor4.isConnected())) {
                    connect(type8, constraintWidget, type8, 0);
                    connect(type9, constraintWidget, type9, 0);
                } else {
                    z2 = false;
                }
                if (z && z2) {
                    getAnchor(type5).connect(constraintWidget.getAnchor(type5), 0);
                    return;
                }
                if (z) {
                    ConstraintAnchor.Type type10 = ConstraintAnchor.Type.CENTER_X;
                    getAnchor(type10).connect(constraintWidget.getAnchor(type10), 0);
                    return;
                } else {
                    if (z2) {
                        ConstraintAnchor.Type type11 = ConstraintAnchor.Type.CENTER_Y;
                        getAnchor(type11).connect(constraintWidget.getAnchor(type11), 0);
                        return;
                    }
                    return;
                }
            }
            ConstraintAnchor.Type type12 = ConstraintAnchor.Type.LEFT;
            if (type2 == type12 || type2 == ConstraintAnchor.Type.RIGHT) {
                connect(type12, constraintWidget, type2, 0);
                connect(ConstraintAnchor.Type.RIGHT, constraintWidget, type2, 0);
                getAnchor(type5).connect(constraintWidget.getAnchor(type2), 0);
                return;
            }
            ConstraintAnchor.Type type13 = ConstraintAnchor.Type.TOP;
            if (type2 == type13 || type2 == ConstraintAnchor.Type.BOTTOM) {
                connect(type13, constraintWidget, type2, 0);
                connect(ConstraintAnchor.Type.BOTTOM, constraintWidget, type2, 0);
                getAnchor(type5).connect(constraintWidget.getAnchor(type2), 0);
                return;
            }
            return;
        }
        ConstraintAnchor.Type type14 = ConstraintAnchor.Type.CENTER_X;
        if (type == type14 && (type2 == (type4 = ConstraintAnchor.Type.LEFT) || type2 == ConstraintAnchor.Type.RIGHT)) {
            ConstraintAnchor anchor5 = getAnchor(type4);
            ConstraintAnchor anchor6 = constraintWidget.getAnchor(type2);
            ConstraintAnchor anchor7 = getAnchor(ConstraintAnchor.Type.RIGHT);
            anchor5.connect(anchor6, 0);
            anchor7.connect(anchor6, 0);
            getAnchor(type14).connect(anchor6, 0);
            return;
        }
        ConstraintAnchor.Type type15 = ConstraintAnchor.Type.CENTER_Y;
        if (type == type15 && (type2 == (type3 = ConstraintAnchor.Type.TOP) || type2 == ConstraintAnchor.Type.BOTTOM)) {
            ConstraintAnchor anchor8 = constraintWidget.getAnchor(type2);
            getAnchor(type3).connect(anchor8, 0);
            getAnchor(ConstraintAnchor.Type.BOTTOM).connect(anchor8, 0);
            getAnchor(type15).connect(anchor8, 0);
            return;
        }
        if (type == type14 && type2 == type14) {
            ConstraintAnchor.Type type16 = ConstraintAnchor.Type.LEFT;
            getAnchor(type16).connect(constraintWidget.getAnchor(type16), 0);
            ConstraintAnchor.Type type17 = ConstraintAnchor.Type.RIGHT;
            getAnchor(type17).connect(constraintWidget.getAnchor(type17), 0);
            getAnchor(type14).connect(constraintWidget.getAnchor(type2), 0);
            return;
        }
        if (type == type15 && type2 == type15) {
            ConstraintAnchor.Type type18 = ConstraintAnchor.Type.TOP;
            getAnchor(type18).connect(constraintWidget.getAnchor(type18), 0);
            ConstraintAnchor.Type type19 = ConstraintAnchor.Type.BOTTOM;
            getAnchor(type19).connect(constraintWidget.getAnchor(type19), 0);
            getAnchor(type15).connect(constraintWidget.getAnchor(type2), 0);
            return;
        }
        ConstraintAnchor anchor9 = getAnchor(type);
        ConstraintAnchor anchor10 = constraintWidget.getAnchor(type2);
        if (anchor9.isValidConnection(anchor10)) {
            ConstraintAnchor.Type type20 = ConstraintAnchor.Type.BASELINE;
            if (type == type20) {
                ConstraintAnchor anchor11 = getAnchor(ConstraintAnchor.Type.TOP);
                ConstraintAnchor anchor12 = getAnchor(ConstraintAnchor.Type.BOTTOM);
                if (anchor11 != null) {
                    anchor11.reset();
                }
                if (anchor12 != null) {
                    anchor12.reset();
                }
            } else if (type == ConstraintAnchor.Type.TOP || type == ConstraintAnchor.Type.BOTTOM) {
                ConstraintAnchor anchor13 = getAnchor(type20);
                if (anchor13 != null) {
                    anchor13.reset();
                }
                ConstraintAnchor anchor14 = getAnchor(type5);
                if (anchor14.getTarget() != anchor10) {
                    anchor14.reset();
                }
                ConstraintAnchor opposite = getAnchor(type).getOpposite();
                ConstraintAnchor anchor15 = getAnchor(type15);
                if (anchor15.isConnected()) {
                    opposite.reset();
                    anchor15.reset();
                }
            } else if (type == ConstraintAnchor.Type.LEFT || type == ConstraintAnchor.Type.RIGHT) {
                ConstraintAnchor anchor16 = getAnchor(type5);
                if (anchor16.getTarget() != anchor10) {
                    anchor16.reset();
                }
                ConstraintAnchor opposite2 = getAnchor(type).getOpposite();
                ConstraintAnchor anchor17 = getAnchor(type14);
                if (anchor17.isConnected()) {
                    opposite2.reset();
                    anchor17.reset();
                }
            }
            anchor9.connect(anchor10, i);
        }
    }

    public void resetAnchors() {
        ConstraintWidget parent = getParent();
        if (parent != null && (parent instanceof ConstraintWidgetContainer) && ((ConstraintWidgetContainer) getParent()).handlesInternalConstraints()) {
            return;
        }
        int size = this.mAnchors.size();
        for (int i = 0; i < size; i++) {
            ((ConstraintAnchor) this.mAnchors.get(i)).reset();
        }
    }

    public ConstraintAnchor getAnchor(ConstraintAnchor.Type type) {
        switch (AnonymousClass1.$SwitchMap$androidx$constraintlayout$core$widgets$ConstraintAnchor$Type[type.ordinal()]) {
            case 1:
                return this.mLeft;
            case 2:
                return this.mTop;
            case 3:
                return this.mRight;
            case 4:
                return this.mBottom;
            case 5:
                return this.mBaseline;
            case 6:
                return this.mCenter;
            case 7:
                return this.mCenterX;
            case 8:
                return this.mCenterY;
            case 9:
                return null;
            default:
                throw new AssertionError(type.name());
        }
    }

    public DimensionBehaviour getHorizontalDimensionBehaviour() {
        return this.mListDimensionBehaviors[0];
    }

    public DimensionBehaviour getVerticalDimensionBehaviour() {
        return this.mListDimensionBehaviors[1];
    }

    public DimensionBehaviour getDimensionBehaviour(int i) {
        if (i == 0) {
            return getHorizontalDimensionBehaviour();
        }
        if (i == 1) {
            return getVerticalDimensionBehaviour();
        }
        return null;
    }

    public void setHorizontalDimensionBehaviour(DimensionBehaviour dimensionBehaviour) {
        this.mListDimensionBehaviors[0] = dimensionBehaviour;
    }

    public void setVerticalDimensionBehaviour(DimensionBehaviour dimensionBehaviour) {
        this.mListDimensionBehaviors[1] = dimensionBehaviour;
    }

    public boolean isInHorizontalChain() {
        ConstraintAnchor constraintAnchor = this.mLeft;
        ConstraintAnchor constraintAnchor2 = constraintAnchor.mTarget;
        if (constraintAnchor2 != null && constraintAnchor2.mTarget == constraintAnchor) {
            return true;
        }
        ConstraintAnchor constraintAnchor3 = this.mRight;
        ConstraintAnchor constraintAnchor4 = constraintAnchor3.mTarget;
        return constraintAnchor4 != null && constraintAnchor4.mTarget == constraintAnchor3;
    }

    public ConstraintWidget getPreviousChainMember(int i) {
        ConstraintAnchor constraintAnchor;
        ConstraintAnchor constraintAnchor2;
        if (i != 0) {
            if (i == 1 && (constraintAnchor2 = (constraintAnchor = this.mTop).mTarget) != null && constraintAnchor2.mTarget == constraintAnchor) {
                return constraintAnchor2.mOwner;
            }
            return null;
        }
        ConstraintAnchor constraintAnchor3 = this.mLeft;
        ConstraintAnchor constraintAnchor4 = constraintAnchor3.mTarget;
        if (constraintAnchor4 == null || constraintAnchor4.mTarget != constraintAnchor3) {
            return null;
        }
        return constraintAnchor4.mOwner;
    }

    public ConstraintWidget getNextChainMember(int i) {
        ConstraintAnchor constraintAnchor;
        ConstraintAnchor constraintAnchor2;
        if (i != 0) {
            if (i == 1 && (constraintAnchor2 = (constraintAnchor = this.mBottom).mTarget) != null && constraintAnchor2.mTarget == constraintAnchor) {
                return constraintAnchor2.mOwner;
            }
            return null;
        }
        ConstraintAnchor constraintAnchor3 = this.mRight;
        ConstraintAnchor constraintAnchor4 = constraintAnchor3.mTarget;
        if (constraintAnchor4 == null || constraintAnchor4.mTarget != constraintAnchor3) {
            return null;
        }
        return constraintAnchor4.mOwner;
    }

    public boolean isInVerticalChain() {
        ConstraintAnchor constraintAnchor = this.mTop;
        ConstraintAnchor constraintAnchor2 = constraintAnchor.mTarget;
        if (constraintAnchor2 != null && constraintAnchor2.mTarget == constraintAnchor) {
            return true;
        }
        ConstraintAnchor constraintAnchor3 = this.mBottom;
        ConstraintAnchor constraintAnchor4 = constraintAnchor3.mTarget;
        return constraintAnchor4 != null && constraintAnchor4.mTarget == constraintAnchor3;
    }

    private boolean isChainHead(int i) {
        ConstraintAnchor constraintAnchor;
        ConstraintAnchor constraintAnchor2;
        int i2 = i * 2;
        ConstraintAnchor[] constraintAnchorArr = this.mListAnchors;
        ConstraintAnchor constraintAnchor3 = constraintAnchorArr[i2];
        ConstraintAnchor constraintAnchor4 = constraintAnchor3.mTarget;
        return (constraintAnchor4 == null || constraintAnchor4.mTarget == constraintAnchor3 || (constraintAnchor2 = (constraintAnchor = constraintAnchorArr[i2 + 1]).mTarget) == null || constraintAnchor2.mTarget != constraintAnchor) ? false : true;
    }

    /* JADX WARN: Code duplicated, block: B:185:0x02be  */
    /* JADX WARN: Code duplicated, block: B:187:0x02c3 A[ADDED_TO_REGION] */
    /* JADX WARN: Code duplicated, block: B:190:0x02c9  */
    /* JADX WARN: Code duplicated, block: B:193:0x02cf  */
    /* JADX WARN: Code duplicated, block: B:197:0x02d9  */
    /* JADX WARN: Code duplicated, block: B:19:0x004d  */
    /* JADX WARN: Code duplicated, block: B:200:0x02e5  */
    /* JADX WARN: Code duplicated, block: B:203:0x02eb  */
    /* JADX WARN: Code duplicated, block: B:205:0x02ee  */
    /* JADX WARN: Code duplicated, block: B:206:0x02f0  */
    /* JADX WARN: Code duplicated, block: B:209:0x030b  */
    /* JADX WARN: Code duplicated, block: B:230:0x036a  */
    /* JADX WARN: Code duplicated, block: B:245:0x03f4  */
    /* JADX WARN: Code duplicated, block: B:261:0x0449  */
    /* JADX WARN: Code duplicated, block: B:264:0x045b  */
    /* JADX WARN: Code duplicated, block: B:265:0x045d  */
    /* JADX WARN: Code duplicated, block: B:267:0x0460  */
    /* JADX WARN: Code duplicated, block: B:304:0x0537  */
    /* JADX WARN: Code duplicated, block: B:306:0x053e  */
    /* JADX WARN: Code duplicated, block: B:308:0x0545  */
    /* JADX WARN: Code duplicated, block: B:309:0x0554  */
    /* JADX WARN: Code duplicated, block: B:310:0x0557  */
    /* JADX WARN: Code duplicated, block: B:313:0x056f  */
    /* JADX WARN: Multi-variable type inference failed */
    public void addToSolver(LinearSystem linearSystem, boolean z) {
        boolean z2;
        boolean z3;
        ConstraintWidget constraintWidget;
        ConstraintWidget constraintWidget2;
        boolean z4;
        boolean z5;
        int i;
        SolverVariable solverVariable;
        int i2;
        int i3;
        boolean z6;
        int i4;
        boolean z7;
        DimensionBehaviour dimensionBehaviour;
        DimensionBehaviour dimensionBehaviour2;
        boolean z8;
        int i5;
        int i6;
        boolean z9;
        SolverVariable solverVariable2;
        SolverVariable solverVariable3;
        SolverVariable solverVariable4;
        int i7;
        int i8;
        char c;
        int i9;
        int i10;
        LinearSystem linearSystem2;
        boolean z10;
        VerticalWidgetRun verticalWidgetRun;
        HorizontalWidgetRun horizontalWidgetRun;
        int i11;
        int i12;
        boolean zIsInHorizontalChain;
        boolean zIsInVerticalChain;
        HorizontalWidgetRun horizontalWidgetRun2;
        VerticalWidgetRun verticalWidgetRun2;
        LinearSystem linearSystem3 = linearSystem;
        SolverVariable solverVariableCreateObjectVariable = linearSystem3.createObjectVariable(this.mLeft);
        SolverVariable solverVariableCreateObjectVariable2 = linearSystem3.createObjectVariable(this.mRight);
        SolverVariable solverVariableCreateObjectVariable3 = linearSystem3.createObjectVariable(this.mTop);
        SolverVariable solverVariableCreateObjectVariable4 = linearSystem3.createObjectVariable(this.mBottom);
        SolverVariable solverVariableCreateObjectVariable5 = linearSystem3.createObjectVariable(this.mBaseline);
        ConstraintWidget constraintWidget3 = this.mParent;
        if (constraintWidget3 == null) {
            z2 = false;
            z3 = false;
        } else {
            z3 = constraintWidget3 != null && constraintWidget3.mListDimensionBehaviors[0] == DimensionBehaviour.WRAP_CONTENT;
            z2 = constraintWidget3 != null && constraintWidget3.mListDimensionBehaviors[1] == DimensionBehaviour.WRAP_CONTENT;
            int i13 = this.mWrapBehaviorInParent;
            if (i13 == 1) {
                z2 = false;
            } else if (i13 == 2) {
                z3 = false;
            } else if (i13 == 3) {
                z2 = false;
                z3 = false;
            }
        }
        if (this.mVisibility == 8 && !this.mAnimated && !hasDependencies()) {
            boolean[] zArr = this.mIsInBarrier;
            if (!zArr[0] && !zArr[1]) {
                return;
            }
        }
        boolean z11 = this.mResolvedHorizontal;
        if (z11 || this.mResolvedVertical) {
            if (z11) {
                linearSystem3.addEquality(solverVariableCreateObjectVariable, this.mX);
                linearSystem3.addEquality(solverVariableCreateObjectVariable2, this.mX + this.mWidth);
                if (z3 && (constraintWidget2 = this.mParent) != null) {
                    if (this.mOptimizeWrapOnResolved) {
                        ConstraintWidgetContainer constraintWidgetContainer = (ConstraintWidgetContainer) constraintWidget2;
                        constraintWidgetContainer.addHorizontalWrapMinVariable(this.mLeft);
                        constraintWidgetContainer.addHorizontalWrapMaxVariable(this.mRight);
                    } else {
                        linearSystem3.addGreaterThan(linearSystem3.createObjectVariable(constraintWidget2.mRight), solverVariableCreateObjectVariable2, 0, 5);
                    }
                }
            }
            if (this.mResolvedVertical) {
                linearSystem3.addEquality(solverVariableCreateObjectVariable3, this.mY);
                linearSystem3.addEquality(solverVariableCreateObjectVariable4, this.mY + this.mHeight);
                if (this.mBaseline.hasDependents()) {
                    linearSystem3.addEquality(solverVariableCreateObjectVariable5, this.mY + this.mBaselineDistance);
                }
                if (z2 && (constraintWidget = this.mParent) != null) {
                    if (this.mOptimizeWrapOnResolved) {
                        ConstraintWidgetContainer constraintWidgetContainer2 = (ConstraintWidgetContainer) constraintWidget;
                        constraintWidgetContainer2.addVerticalWrapMinVariable(this.mTop);
                        constraintWidgetContainer2.addVerticalWrapMaxVariable(this.mBottom);
                    } else {
                        linearSystem3.addGreaterThan(linearSystem3.createObjectVariable(constraintWidget.mBottom), solverVariableCreateObjectVariable4, 0, 5);
                    }
                }
            }
            if (this.mResolvedHorizontal && this.mResolvedVertical) {
                this.mResolvedHorizontal = false;
                this.mResolvedVertical = false;
                return;
            }
        }
        boolean z12 = LinearSystem.USE_DEPENDENCY_ORDERING;
        if (z && (horizontalWidgetRun2 = this.mHorizontalRun) != null && (verticalWidgetRun2 = this.mVerticalRun) != null) {
            DependencyNode dependencyNode = horizontalWidgetRun2.start;
            if (dependencyNode.resolved && horizontalWidgetRun2.end.resolved && verticalWidgetRun2.start.resolved && verticalWidgetRun2.end.resolved) {
                linearSystem3.addEquality(solverVariableCreateObjectVariable, dependencyNode.value);
                linearSystem3.addEquality(solverVariableCreateObjectVariable2, this.mHorizontalRun.end.value);
                linearSystem3.addEquality(solverVariableCreateObjectVariable3, this.mVerticalRun.start.value);
                linearSystem3.addEquality(solverVariableCreateObjectVariable4, this.mVerticalRun.end.value);
                linearSystem3.addEquality(solverVariableCreateObjectVariable5, this.mVerticalRun.baseline.value);
                if (this.mParent != null) {
                    if (z3 && this.isTerminalWidget[0] && !isInHorizontalChain()) {
                        linearSystem3.addGreaterThan(linearSystem3.createObjectVariable(this.mParent.mRight), solverVariableCreateObjectVariable2, 0, 8);
                    }
                    if (z2 && this.isTerminalWidget[1] && !isInVerticalChain()) {
                        linearSystem3.addGreaterThan(linearSystem3.createObjectVariable(this.mParent.mBottom), solverVariableCreateObjectVariable4, 0, 8);
                    }
                }
                this.mResolvedHorizontal = false;
                this.mResolvedVertical = false;
                return;
            }
        }
        if (this.mParent != null) {
            if (isChainHead(0)) {
                ((ConstraintWidgetContainer) this.mParent).addChain(this, 0);
                zIsInHorizontalChain = true;
            } else {
                zIsInHorizontalChain = isInHorizontalChain();
            }
            if (isChainHead(1)) {
                ((ConstraintWidgetContainer) this.mParent).addChain(this, 1);
                zIsInVerticalChain = true;
            } else {
                zIsInVerticalChain = isInVerticalChain();
            }
            if (!zIsInHorizontalChain && z3 && this.mVisibility != 8 && this.mLeft.mTarget == null && this.mRight.mTarget == null) {
                linearSystem3.addGreaterThan(linearSystem3.createObjectVariable(this.mParent.mRight), solverVariableCreateObjectVariable2, 0, 1);
            }
            if (!zIsInVerticalChain && z2 && this.mVisibility != 8 && this.mTop.mTarget == null && this.mBottom.mTarget == null && this.mBaseline == null) {
                linearSystem3.addGreaterThan(linearSystem3.createObjectVariable(this.mParent.mBottom), solverVariableCreateObjectVariable4, 0, 1);
            }
            z4 = zIsInHorizontalChain;
            z5 = zIsInVerticalChain;
        } else {
            z4 = false;
            z5 = false;
        }
        int i14 = this.mWidth;
        int i15 = this.mMinWidth;
        if (i14 >= i15) {
            i15 = i14;
        }
        int i16 = this.mHeight;
        int i17 = this.mMinHeight;
        if (i16 >= i17) {
            i17 = i16;
        }
        DimensionBehaviour[] dimensionBehaviourArr = this.mListDimensionBehaviors;
        DimensionBehaviour dimensionBehaviour3 = dimensionBehaviourArr[0];
        DimensionBehaviour dimensionBehaviour4 = DimensionBehaviour.MATCH_CONSTRAINT;
        boolean z13 = dimensionBehaviour3 != dimensionBehaviour4;
        DimensionBehaviour dimensionBehaviour5 = dimensionBehaviourArr[1];
        boolean z14 = dimensionBehaviour5 != dimensionBehaviour4;
        int i18 = this.mDimensionRatioSide;
        this.mResolvedDimensionRatioSide = i18;
        int i19 = i15;
        float f = this.mDimensionRatio;
        this.mResolvedDimensionRatio = f;
        int i20 = this.mMatchConstraintDefaultWidth;
        int i21 = this.mMatchConstraintDefaultHeight;
        if (f > 0.0f) {
            i = i17;
            if (this.mVisibility != 8) {
                i2 = (dimensionBehaviour3 == dimensionBehaviour4 && i20 == 0) ? 3 : i20;
                int i22 = (dimensionBehaviour5 == dimensionBehaviour4 && i21 == 0) ? 3 : i21;
                if (dimensionBehaviour3 == dimensionBehaviour4 && dimensionBehaviour5 == dimensionBehaviour4) {
                    solverVariable = solverVariableCreateObjectVariable2;
                    i12 = 3;
                    if (i2 == 3 && i22 == 3) {
                        setupDimensionRatio(z3, z2, z13, z14);
                    }
                    i3 = i22;
                    z6 = true;
                    int[] iArr = this.mResolvedMatchConstraintDefault;
                    iArr[0] = i2;
                    iArr[1] = i3;
                    this.mResolvedHasRatio = z6;
                    if (z6) {
                        int i23 = this.mResolvedDimensionRatioSide;
                        i4 = -1;
                        boolean z15 = i23 != 0 || i23 == -1;
                        if (z6 || !((i11 = this.mResolvedDimensionRatioSide) == 1 || i11 == i4)) {
                            z7 = false;
                        } else {
                            z7 = true;
                        }
                        dimensionBehaviour = this.mListDimensionBehaviors[0];
                        dimensionBehaviour2 = DimensionBehaviour.WRAP_CONTENT;
                        if (dimensionBehaviour == dimensionBehaviour2 || !(this instanceof ConstraintWidgetContainer)) {
                            z8 = false;
                        } else {
                            z8 = true;
                        }
                        if (z8) {
                            i5 = 0;
                        } else {
                            i5 = i19;
                        }
                        boolean z16 = !this.mCenter.isConnected();
                        boolean[] zArr2 = this.mIsInBarrier;
                        boolean z17 = zArr2[0];
                        boolean z18 = zArr2[1];
                        if (this.mHorizontalResolution != 2 || this.mResolvedHorizontal) {
                            i6 = i2;
                            z9 = z3;
                        } else {
                            if (z && (horizontalWidgetRun = this.mHorizontalRun) != null) {
                                DependencyNode dependencyNode2 = horizontalWidgetRun.start;
                                if (dependencyNode2.resolved && horizontalWidgetRun.end.resolved) {
                                    if (z) {
                                        linearSystem3.addEquality(solverVariableCreateObjectVariable, dependencyNode2.value);
                                        SolverVariable solverVariable5 = solverVariable;
                                        linearSystem3.addEquality(solverVariable5, this.mHorizontalRun.end.value);
                                        if (this.mParent != null && z3 && this.isTerminalWidget[0] && !isInHorizontalChain()) {
                                            linearSystem3.addGreaterThan(linearSystem3.createObjectVariable(this.mParent.mRight), solverVariable5, 0, 8);
                                        }
                                        solverVariable = solverVariable5;
                                    }
                                    i6 = i2;
                                    z9 = z3;
                                }
                            }
                            SolverVariable solverVariable6 = solverVariable;
                            ConstraintWidget constraintWidget4 = this.mParent;
                            SolverVariable solverVariableCreateObjectVariable6 = constraintWidget4 != null ? linearSystem3.createObjectVariable(constraintWidget4.mRight) : null;
                            ConstraintWidget constraintWidget5 = this.mParent;
                            SolverVariable solverVariableCreateObjectVariable7 = constraintWidget5 != null ? linearSystem3.createObjectVariable(constraintWidget5.mLeft) : null;
                            boolean z19 = this.isTerminalWidget[0];
                            DimensionBehaviour[] dimensionBehaviourArr2 = this.mListDimensionBehaviors;
                            solverVariable = solverVariable6;
                            DimensionBehaviour dimensionBehaviour6 = dimensionBehaviourArr2[0];
                            ConstraintAnchor constraintAnchor = this.mLeft;
                            SolverVariable solverVariable7 = solverVariableCreateObjectVariable7;
                            ConstraintAnchor constraintAnchor2 = this.mRight;
                            z6 = z6;
                            z9 = z3;
                            int i24 = this.mX;
                            int i25 = this.mMinWidth;
                            int i26 = this.mMaxDimension[0];
                            float f2 = this.mHorizontalBiasPercent;
                            boolean z20 = dimensionBehaviourArr2[1] == dimensionBehaviour4;
                            solverVariableCreateObjectVariable = solverVariableCreateObjectVariable;
                            boolean z21 = z2;
                            SolverVariable solverVariable8 = solverVariableCreateObjectVariable6;
                            z2 = z21;
                            i6 = i2;
                            dimensionBehaviour2 = dimensionBehaviour2;
                            linearSystem3 = linearSystem;
                            applyConstraints(linearSystem3, true, z9, z2, z19, solverVariable7, solverVariable8, dimensionBehaviour6, z8, constraintAnchor, constraintAnchor2, i24, i5, i25, i26, f2, z15, z20, z4, z5, z17, i6, i3, this.mMatchConstraintMinWidth, this.mMatchConstraintMaxWidth, this.mMatchConstraintPercentWidth, z16);
                        }
                        if (z || (verticalWidgetRun = this.mVerticalRun) == null) {
                            solverVariable2 = r24;
                            solverVariable3 = r25;
                            solverVariable4 = r26;
                            i7 = 0;
                            i8 = 8;
                            c = 1;
                            i9 = 1;
                        } else {
                            DependencyNode dependencyNode3 = verticalWidgetRun.start;
                            if (dependencyNode3.resolved && verticalWidgetRun.end.resolved) {
                                int i27 = dependencyNode3.value;
                                solverVariable2 = solverVariableCreateObjectVariable3;
                                linearSystem3.addEquality(solverVariable2, i27);
                                solverVariable3 = solverVariableCreateObjectVariable4;
                                linearSystem3.addEquality(solverVariable3, this.mVerticalRun.end.value);
                                solverVariable4 = solverVariableCreateObjectVariable5;
                                linearSystem3.addEquality(solverVariable4, this.mVerticalRun.baseline.value);
                                ConstraintWidget constraintWidget6 = this.mParent;
                                if (constraintWidget6 == null || z5 || !z2) {
                                    i7 = 0;
                                    i8 = 8;
                                    c = 1;
                                } else {
                                    c = 1;
                                    if (this.isTerminalWidget[1]) {
                                        i7 = 0;
                                        i8 = 8;
                                        linearSystem3.addGreaterThan(linearSystem3.createObjectVariable(constraintWidget6.mBottom), solverVariable3, 0, 8);
                                    } else {
                                        i7 = 0;
                                        i8 = 8;
                                    }
                                }
                                i9 = i7;
                            } else {
                                solverVariable2 = r24;
                                solverVariable3 = r25;
                                solverVariable4 = r26;
                                i7 = 0;
                                i8 = 8;
                                c = 1;
                                i9 = 1;
                            }
                        }
                        if (this.mVerticalResolution == 2) {
                            i10 = i7;
                        } else {
                            i10 = i9;
                        }
                        if (i10 == 0 && !this.mResolvedVertical) {
                            boolean z22 = (this.mListDimensionBehaviors[c] == dimensionBehaviour2 && (this instanceof ConstraintWidgetContainer)) ? c : i7;
                            int i28 = z22 != 0 ? i7 : i;
                            ConstraintWidget constraintWidget7 = this.mParent;
                            SolverVariable solverVariableCreateObjectVariable8 = constraintWidget7 != null ? linearSystem3.createObjectVariable(constraintWidget7.mBottom) : null;
                            ConstraintWidget constraintWidget8 = this.mParent;
                            SolverVariable solverVariableCreateObjectVariable9 = constraintWidget8 != null ? linearSystem3.createObjectVariable(constraintWidget8.mTop) : null;
                            if (this.mBaselineDistance > 0 || this.mVisibility == i8) {
                                z10 = z16;
                                ConstraintAnchor constraintAnchor3 = this.mBaseline;
                                if (constraintAnchor3.mTarget != null) {
                                    linearSystem3.addEquality(solverVariable4, solverVariable2, getBaselineDistance(), i8);
                                    linearSystem3.addEquality(solverVariable4, linearSystem3.createObjectVariable(this.mBaseline.mTarget), this.mBaseline.getMargin(), i8);
                                    if (z2) {
                                        linearSystem3.addGreaterThan(solverVariableCreateObjectVariable8, linearSystem3.createObjectVariable(this.mBottom), i7, 5);
                                    }
                                    z10 = i7;
                                } else if (this.mVisibility == i8) {
                                    linearSystem3.addEquality(solverVariable4, solverVariable2, constraintAnchor3.getMargin(), i8);
                                    z10 = z16;
                                } else {
                                    linearSystem3.addEquality(solverVariable4, solverVariable2, getBaselineDistance(), i8);
                                    z10 = z16;
                                }
                            }
                            z10 = z16;
                            boolean z23 = this.isTerminalWidget[c];
                            DimensionBehaviour[] dimensionBehaviourArr3 = this.mListDimensionBehaviors;
                            int i29 = i7;
                            applyConstraints(linearSystem, false, z2, z9, z23, solverVariableCreateObjectVariable9, solverVariableCreateObjectVariable8, dimensionBehaviourArr3[c], z22, this.mTop, this.mBottom, this.mY, i28, this.mMinHeight, this.mMaxDimension[c], this.mVerticalBiasPercent, z7, dimensionBehaviourArr3[i29] == dimensionBehaviour4 ? c : i29, z5, z4, z18, i3, i6, this.mMatchConstraintMinHeight, this.mMatchConstraintMaxHeight, this.mMatchConstraintPercentHeight, z10);
                        }
                        if (!z6) {
                            linearSystem2 = linearSystem;
                        } else if (this.mResolvedDimensionRatioSide == 1) {
                            linearSystem.addRatio(solverVariable3, solverVariable2, solverVariable, solverVariableCreateObjectVariable, this.mResolvedDimensionRatio, 8);
                            linearSystem2 = linearSystem;
                        } else {
                            linearSystem.addRatio(solverVariable, solverVariableCreateObjectVariable, solverVariable3, solverVariable2, this.mResolvedDimensionRatio, 8);
                            linearSystem2 = linearSystem;
                        }
                        if (this.mCenter.isConnected()) {
                            linearSystem2.addCenterPoint(this, this.mCenter.getTarget().getOwner(), (float) Math.toRadians(this.mCircleConstraintAngle + 90.0f), this.mCenter.getMargin());
                        }
                        this.mResolvedHorizontal = false;
                        this.mResolvedVertical = false;
                    }
                    i4 = -1;
                    if (z6) {
                        z7 = false;
                    } else {
                        z7 = false;
                    }
                    dimensionBehaviour = this.mListDimensionBehaviors[0];
                    dimensionBehaviour2 = DimensionBehaviour.WRAP_CONTENT;
                    if (dimensionBehaviour == dimensionBehaviour2) {
                        z8 = false;
                    } else {
                        z8 = false;
                    }
                    if (z8) {
                        i5 = 0;
                    } else {
                        i5 = i19;
                    }
                    boolean z110 = !this.mCenter.isConnected();
                    boolean[] zArr3 = this.mIsInBarrier;
                    boolean z111 = zArr3[0];
                    boolean z112 = zArr3[1];
                    if (this.mHorizontalResolution != 2) {
                        i6 = i2;
                        z9 = z3;
                    } else {
                        i6 = i2;
                        z9 = z3;
                    }
                    if (z) {
                        solverVariable2 = r24;
                        solverVariable3 = r25;
                        solverVariable4 = r26;
                        i7 = 0;
                        i8 = 8;
                        c = 1;
                        i9 = 1;
                    } else {
                        solverVariable2 = r24;
                        solverVariable3 = r25;
                        solverVariable4 = r26;
                        i7 = 0;
                        i8 = 8;
                        c = 1;
                        i9 = 1;
                    }
                    if (this.mVerticalResolution == 2) {
                        i10 = i7;
                    } else {
                        i10 = i9;
                    }
                    if (i10 == 0) {
                    }
                    if (!z6) {
                        linearSystem2 = linearSystem;
                    } else if (this.mResolvedDimensionRatioSide == 1) {
                        linearSystem.addRatio(solverVariable3, solverVariable2, solverVariable, solverVariableCreateObjectVariable, this.mResolvedDimensionRatio, 8);
                        linearSystem2 = linearSystem;
                    } else {
                        linearSystem.addRatio(solverVariable, solverVariableCreateObjectVariable, solverVariable3, solverVariable2, this.mResolvedDimensionRatio, 8);
                        linearSystem2 = linearSystem;
                    }
                    if (this.mCenter.isConnected()) {
                        linearSystem2.addCenterPoint(this, this.mCenter.getTarget().getOwner(), (float) Math.toRadians(this.mCircleConstraintAngle + 90.0f), this.mCenter.getMargin());
                    }
                    this.mResolvedHorizontal = false;
                    this.mResolvedVertical = false;
                }
                solverVariable = solverVariableCreateObjectVariable2;
                i12 = 3;
                if (dimensionBehaviour3 == dimensionBehaviour4 && i2 == i12) {
                    this.mResolvedDimensionRatioSide = 0;
                    i19 = (int) (i16 * f);
                    if (dimensionBehaviour5 != dimensionBehaviour4) {
                        i2 = 4;
                        i3 = i22;
                    } else {
                        i3 = i22;
                        z6 = true;
                    }
                    int[] iArr2 = this.mResolvedMatchConstraintDefault;
                    iArr2[0] = i2;
                    iArr2[1] = i3;
                    this.mResolvedHasRatio = z6;
                    if (z6) {
                        int i210 = this.mResolvedDimensionRatioSide;
                        i4 = -1;
                        if (i210 != 0) {
                        }
                        if (z6) {
                            z7 = false;
                        } else {
                            z7 = false;
                        }
                        dimensionBehaviour = this.mListDimensionBehaviors[0];
                        dimensionBehaviour2 = DimensionBehaviour.WRAP_CONTENT;
                        if (dimensionBehaviour == dimensionBehaviour2) {
                            z8 = false;
                        } else {
                            z8 = false;
                        }
                        if (z8) {
                            i5 = 0;
                        } else {
                            i5 = i19;
                        }
                        boolean z113 = !this.mCenter.isConnected();
                        boolean[] zArr4 = this.mIsInBarrier;
                        boolean z114 = zArr4[0];
                        boolean z115 = zArr4[1];
                        if (this.mHorizontalResolution != 2) {
                            i6 = i2;
                            z9 = z3;
                        } else {
                            i6 = i2;
                            z9 = z3;
                        }
                        if (z) {
                            solverVariable2 = r24;
                            solverVariable3 = r25;
                            solverVariable4 = r26;
                            i7 = 0;
                            i8 = 8;
                            c = 1;
                            i9 = 1;
                        } else {
                            solverVariable2 = r24;
                            solverVariable3 = r25;
                            solverVariable4 = r26;
                            i7 = 0;
                            i8 = 8;
                            c = 1;
                            i9 = 1;
                        }
                        if (this.mVerticalResolution == 2) {
                            i10 = i7;
                        } else {
                            i10 = i9;
                        }
                        if (i10 == 0) {
                        }
                        if (!z6) {
                            linearSystem2 = linearSystem;
                        } else if (this.mResolvedDimensionRatioSide == 1) {
                            linearSystem.addRatio(solverVariable3, solverVariable2, solverVariable, solverVariableCreateObjectVariable, this.mResolvedDimensionRatio, 8);
                            linearSystem2 = linearSystem;
                        } else {
                            linearSystem.addRatio(solverVariable, solverVariableCreateObjectVariable, solverVariable3, solverVariable2, this.mResolvedDimensionRatio, 8);
                            linearSystem2 = linearSystem;
                        }
                        if (this.mCenter.isConnected()) {
                            linearSystem2.addCenterPoint(this, this.mCenter.getTarget().getOwner(), (float) Math.toRadians(this.mCircleConstraintAngle + 90.0f), this.mCenter.getMargin());
                        }
                        this.mResolvedHorizontal = false;
                        this.mResolvedVertical = false;
                    }
                    i4 = -1;
                    if (z6) {
                        z7 = false;
                    } else {
                        z7 = false;
                    }
                    dimensionBehaviour = this.mListDimensionBehaviors[0];
                    dimensionBehaviour2 = DimensionBehaviour.WRAP_CONTENT;
                    if (dimensionBehaviour == dimensionBehaviour2) {
                        z8 = false;
                    } else {
                        z8 = false;
                    }
                    if (z8) {
                        i5 = 0;
                    } else {
                        i5 = i19;
                    }
                    boolean z116 = !this.mCenter.isConnected();
                    boolean[] zArr5 = this.mIsInBarrier;
                    boolean z117 = zArr5[0];
                    boolean z118 = zArr5[1];
                    if (this.mHorizontalResolution != 2) {
                        i6 = i2;
                        z9 = z3;
                    } else {
                        i6 = i2;
                        z9 = z3;
                    }
                    if (z) {
                        solverVariable2 = r24;
                        solverVariable3 = r25;
                        solverVariable4 = r26;
                        i7 = 0;
                        i8 = 8;
                        c = 1;
                        i9 = 1;
                    } else {
                        solverVariable2 = r24;
                        solverVariable3 = r25;
                        solverVariable4 = r26;
                        i7 = 0;
                        i8 = 8;
                        c = 1;
                        i9 = 1;
                    }
                    if (this.mVerticalResolution == 2) {
                        i10 = i7;
                    } else {
                        i10 = i9;
                    }
                    if (i10 == 0) {
                    }
                    if (!z6) {
                        linearSystem2 = linearSystem;
                    } else if (this.mResolvedDimensionRatioSide == 1) {
                        linearSystem.addRatio(solverVariable3, solverVariable2, solverVariable, solverVariableCreateObjectVariable, this.mResolvedDimensionRatio, 8);
                        linearSystem2 = linearSystem;
                    } else {
                        linearSystem.addRatio(solverVariable, solverVariableCreateObjectVariable, solverVariable3, solverVariable2, this.mResolvedDimensionRatio, 8);
                        linearSystem2 = linearSystem;
                    }
                    if (this.mCenter.isConnected()) {
                        linearSystem2.addCenterPoint(this, this.mCenter.getTarget().getOwner(), (float) Math.toRadians(this.mCircleConstraintAngle + 90.0f), this.mCenter.getMargin());
                    }
                    this.mResolvedHorizontal = false;
                    this.mResolvedVertical = false;
                }
                if (dimensionBehaviour5 == dimensionBehaviour4 && i22 == i12) {
                    this.mResolvedDimensionRatioSide = 1;
                    if (i18 == -1) {
                        this.mResolvedDimensionRatio = 1.0f / f;
                    }
                    i = (int) (this.mResolvedDimensionRatio * i14);
                    if (dimensionBehaviour3 != dimensionBehaviour4) {
                        i3 = 4;
                    }
                    int[] iArr3 = this.mResolvedMatchConstraintDefault;
                    iArr3[0] = i2;
                    iArr3[1] = i3;
                    this.mResolvedHasRatio = z6;
                    if (z6) {
                        int i211 = this.mResolvedDimensionRatioSide;
                        i4 = -1;
                        if (i211 != 0) {
                        }
                        if (z6) {
                            z7 = false;
                        } else {
                            z7 = false;
                        }
                        dimensionBehaviour = this.mListDimensionBehaviors[0];
                        dimensionBehaviour2 = DimensionBehaviour.WRAP_CONTENT;
                        if (dimensionBehaviour == dimensionBehaviour2) {
                            z8 = false;
                        } else {
                            z8 = false;
                        }
                        if (z8) {
                            i5 = 0;
                        } else {
                            i5 = i19;
                        }
                        boolean z119 = !this.mCenter.isConnected();
                        boolean[] zArr6 = this.mIsInBarrier;
                        boolean z1110 = zArr6[0];
                        boolean z1111 = zArr6[1];
                        if (this.mHorizontalResolution != 2) {
                            i6 = i2;
                            z9 = z3;
                        } else {
                            i6 = i2;
                            z9 = z3;
                        }
                        if (z) {
                            solverVariable2 = r24;
                            solverVariable3 = r25;
                            solverVariable4 = r26;
                            i7 = 0;
                            i8 = 8;
                            c = 1;
                            i9 = 1;
                        } else {
                            solverVariable2 = r24;
                            solverVariable3 = r25;
                            solverVariable4 = r26;
                            i7 = 0;
                            i8 = 8;
                            c = 1;
                            i9 = 1;
                        }
                        if (this.mVerticalResolution == 2) {
                            i10 = i7;
                        } else {
                            i10 = i9;
                        }
                        if (i10 == 0) {
                        }
                        if (!z6) {
                            linearSystem2 = linearSystem;
                        } else if (this.mResolvedDimensionRatioSide == 1) {
                            linearSystem.addRatio(solverVariable3, solverVariable2, solverVariable, solverVariableCreateObjectVariable, this.mResolvedDimensionRatio, 8);
                            linearSystem2 = linearSystem;
                        } else {
                            linearSystem.addRatio(solverVariable, solverVariableCreateObjectVariable, solverVariable3, solverVariable2, this.mResolvedDimensionRatio, 8);
                            linearSystem2 = linearSystem;
                        }
                        if (this.mCenter.isConnected()) {
                            linearSystem2.addCenterPoint(this, this.mCenter.getTarget().getOwner(), (float) Math.toRadians(this.mCircleConstraintAngle + 90.0f), this.mCenter.getMargin());
                        }
                        this.mResolvedHorizontal = false;
                        this.mResolvedVertical = false;
                    }
                    i4 = -1;
                    if (z6) {
                        z7 = false;
                    } else {
                        z7 = false;
                    }
                    dimensionBehaviour = this.mListDimensionBehaviors[0];
                    dimensionBehaviour2 = DimensionBehaviour.WRAP_CONTENT;
                    if (dimensionBehaviour == dimensionBehaviour2) {
                        z8 = false;
                    } else {
                        z8 = false;
                    }
                    if (z8) {
                        i5 = 0;
                    } else {
                        i5 = i19;
                    }
                    boolean z1112 = !this.mCenter.isConnected();
                    boolean[] zArr7 = this.mIsInBarrier;
                    boolean z1113 = zArr7[0];
                    boolean z1114 = zArr7[1];
                    if (this.mHorizontalResolution != 2) {
                        i6 = i2;
                        z9 = z3;
                    } else {
                        i6 = i2;
                        z9 = z3;
                    }
                    if (z) {
                        solverVariable2 = r24;
                        solverVariable3 = r25;
                        solverVariable4 = r26;
                        i7 = 0;
                        i8 = 8;
                        c = 1;
                        i9 = 1;
                    } else {
                        solverVariable2 = r24;
                        solverVariable3 = r25;
                        solverVariable4 = r26;
                        i7 = 0;
                        i8 = 8;
                        c = 1;
                        i9 = 1;
                    }
                    if (this.mVerticalResolution == 2) {
                        i10 = i7;
                    } else {
                        i10 = i9;
                    }
                    if (i10 == 0) {
                    }
                    if (!z6) {
                        linearSystem2 = linearSystem;
                    } else if (this.mResolvedDimensionRatioSide == 1) {
                        linearSystem.addRatio(solverVariable3, solverVariable2, solverVariable, solverVariableCreateObjectVariable, this.mResolvedDimensionRatio, 8);
                        linearSystem2 = linearSystem;
                    } else {
                        linearSystem.addRatio(solverVariable, solverVariableCreateObjectVariable, solverVariable3, solverVariable2, this.mResolvedDimensionRatio, 8);
                        linearSystem2 = linearSystem;
                    }
                    if (this.mCenter.isConnected()) {
                        linearSystem2.addCenterPoint(this, this.mCenter.getTarget().getOwner(), (float) Math.toRadians(this.mCircleConstraintAngle + 90.0f), this.mCenter.getMargin());
                    }
                    this.mResolvedHorizontal = false;
                    this.mResolvedVertical = false;
                }
                i3 = i22;
                z6 = true;
                int[] iArr4 = this.mResolvedMatchConstraintDefault;
                iArr4[0] = i2;
                iArr4[1] = i3;
                this.mResolvedHasRatio = z6;
                if (z6) {
                    int i212 = this.mResolvedDimensionRatioSide;
                    i4 = -1;
                    if (i212 != 0) {
                    }
                    if (z6) {
                        z7 = false;
                    } else {
                        z7 = false;
                    }
                    dimensionBehaviour = this.mListDimensionBehaviors[0];
                    dimensionBehaviour2 = DimensionBehaviour.WRAP_CONTENT;
                    if (dimensionBehaviour == dimensionBehaviour2) {
                        z8 = false;
                    } else {
                        z8 = false;
                    }
                    if (z8) {
                        i5 = 0;
                    } else {
                        i5 = i19;
                    }
                    boolean z1115 = !this.mCenter.isConnected();
                    boolean[] zArr8 = this.mIsInBarrier;
                    boolean z1116 = zArr8[0];
                    boolean z1117 = zArr8[1];
                    if (this.mHorizontalResolution != 2) {
                        i6 = i2;
                        z9 = z3;
                    } else {
                        i6 = i2;
                        z9 = z3;
                    }
                    if (z) {
                        solverVariable2 = r24;
                        solverVariable3 = r25;
                        solverVariable4 = r26;
                        i7 = 0;
                        i8 = 8;
                        c = 1;
                        i9 = 1;
                    } else {
                        solverVariable2 = r24;
                        solverVariable3 = r25;
                        solverVariable4 = r26;
                        i7 = 0;
                        i8 = 8;
                        c = 1;
                        i9 = 1;
                    }
                    if (this.mVerticalResolution == 2) {
                        i10 = i7;
                    } else {
                        i10 = i9;
                    }
                    if (i10 == 0) {
                    }
                    if (!z6) {
                        linearSystem2 = linearSystem;
                    } else if (this.mResolvedDimensionRatioSide == 1) {
                        linearSystem.addRatio(solverVariable3, solverVariable2, solverVariable, solverVariableCreateObjectVariable, this.mResolvedDimensionRatio, 8);
                        linearSystem2 = linearSystem;
                    } else {
                        linearSystem.addRatio(solverVariable, solverVariableCreateObjectVariable, solverVariable3, solverVariable2, this.mResolvedDimensionRatio, 8);
                        linearSystem2 = linearSystem;
                    }
                    if (this.mCenter.isConnected()) {
                        linearSystem2.addCenterPoint(this, this.mCenter.getTarget().getOwner(), (float) Math.toRadians(this.mCircleConstraintAngle + 90.0f), this.mCenter.getMargin());
                    }
                    this.mResolvedHorizontal = false;
                    this.mResolvedVertical = false;
                }
                i4 = -1;
                if (z6) {
                    z7 = false;
                } else {
                    z7 = false;
                }
                dimensionBehaviour = this.mListDimensionBehaviors[0];
                dimensionBehaviour2 = DimensionBehaviour.WRAP_CONTENT;
                if (dimensionBehaviour == dimensionBehaviour2) {
                    z8 = false;
                } else {
                    z8 = false;
                }
                if (z8) {
                    i5 = 0;
                } else {
                    i5 = i19;
                }
                boolean z1118 = !this.mCenter.isConnected();
                boolean[] zArr9 = this.mIsInBarrier;
                boolean z1119 = zArr9[0];
                boolean z11110 = zArr9[1];
                if (this.mHorizontalResolution != 2) {
                    i6 = i2;
                    z9 = z3;
                } else {
                    i6 = i2;
                    z9 = z3;
                }
                if (z) {
                    solverVariable2 = r24;
                    solverVariable3 = r25;
                    solverVariable4 = r26;
                    i7 = 0;
                    i8 = 8;
                    c = 1;
                    i9 = 1;
                } else {
                    solverVariable2 = r24;
                    solverVariable3 = r25;
                    solverVariable4 = r26;
                    i7 = 0;
                    i8 = 8;
                    c = 1;
                    i9 = 1;
                }
                if (this.mVerticalResolution == 2) {
                    i10 = i7;
                } else {
                    i10 = i9;
                }
                if (i10 == 0) {
                }
                if (!z6) {
                    linearSystem2 = linearSystem;
                } else if (this.mResolvedDimensionRatioSide == 1) {
                    linearSystem.addRatio(solverVariable3, solverVariable2, solverVariable, solverVariableCreateObjectVariable, this.mResolvedDimensionRatio, 8);
                    linearSystem2 = linearSystem;
                } else {
                    linearSystem.addRatio(solverVariable, solverVariableCreateObjectVariable, solverVariable3, solverVariable2, this.mResolvedDimensionRatio, 8);
                    linearSystem2 = linearSystem;
                }
                if (this.mCenter.isConnected()) {
                    linearSystem2.addCenterPoint(this, this.mCenter.getTarget().getOwner(), (float) Math.toRadians(this.mCircleConstraintAngle + 90.0f), this.mCenter.getMargin());
                }
                this.mResolvedHorizontal = false;
                this.mResolvedVertical = false;
            }
            z6 = false;
            int[] iArr5 = this.mResolvedMatchConstraintDefault;
            iArr5[0] = i2;
            iArr5[1] = i3;
            this.mResolvedHasRatio = z6;
            if (z6) {
                int i213 = this.mResolvedDimensionRatioSide;
                i4 = -1;
                if (i213 != 0) {
                }
                if (z6) {
                    z7 = false;
                } else {
                    z7 = false;
                }
                dimensionBehaviour = this.mListDimensionBehaviors[0];
                dimensionBehaviour2 = DimensionBehaviour.WRAP_CONTENT;
                if (dimensionBehaviour == dimensionBehaviour2) {
                    z8 = false;
                } else {
                    z8 = false;
                }
                if (z8) {
                    i5 = 0;
                } else {
                    i5 = i19;
                }
                boolean z11111 = !this.mCenter.isConnected();
                boolean[] zArr10 = this.mIsInBarrier;
                boolean z11112 = zArr10[0];
                boolean z11113 = zArr10[1];
                if (this.mHorizontalResolution != 2) {
                    i6 = i2;
                    z9 = z3;
                } else {
                    i6 = i2;
                    z9 = z3;
                }
                if (z) {
                    solverVariable2 = r24;
                    solverVariable3 = r25;
                    solverVariable4 = r26;
                    i7 = 0;
                    i8 = 8;
                    c = 1;
                    i9 = 1;
                } else {
                    solverVariable2 = r24;
                    solverVariable3 = r25;
                    solverVariable4 = r26;
                    i7 = 0;
                    i8 = 8;
                    c = 1;
                    i9 = 1;
                }
                if (this.mVerticalResolution == 2) {
                    i10 = i7;
                } else {
                    i10 = i9;
                }
                if (i10 == 0) {
                }
                if (!z6) {
                    linearSystem2 = linearSystem;
                } else if (this.mResolvedDimensionRatioSide == 1) {
                    linearSystem.addRatio(solverVariable3, solverVariable2, solverVariable, solverVariableCreateObjectVariable, this.mResolvedDimensionRatio, 8);
                    linearSystem2 = linearSystem;
                } else {
                    linearSystem.addRatio(solverVariable, solverVariableCreateObjectVariable, solverVariable3, solverVariable2, this.mResolvedDimensionRatio, 8);
                    linearSystem2 = linearSystem;
                }
                if (this.mCenter.isConnected()) {
                    linearSystem2.addCenterPoint(this, this.mCenter.getTarget().getOwner(), (float) Math.toRadians(this.mCircleConstraintAngle + 90.0f), this.mCenter.getMargin());
                }
                this.mResolvedHorizontal = false;
                this.mResolvedVertical = false;
            }
            i4 = -1;
            if (z6) {
                z7 = false;
            } else {
                z7 = false;
            }
            dimensionBehaviour = this.mListDimensionBehaviors[0];
            dimensionBehaviour2 = DimensionBehaviour.WRAP_CONTENT;
            if (dimensionBehaviour == dimensionBehaviour2) {
                z8 = false;
            } else {
                z8 = false;
            }
            if (z8) {
                i5 = 0;
            } else {
                i5 = i19;
            }
            boolean z11114 = !this.mCenter.isConnected();
            boolean[] zArr11 = this.mIsInBarrier;
            boolean z11115 = zArr11[0];
            boolean z11116 = zArr11[1];
            if (this.mHorizontalResolution != 2) {
                i6 = i2;
                z9 = z3;
            } else {
                i6 = i2;
                z9 = z3;
            }
            if (z) {
                solverVariable2 = r24;
                solverVariable3 = r25;
                solverVariable4 = r26;
                i7 = 0;
                i8 = 8;
                c = 1;
                i9 = 1;
            } else {
                solverVariable2 = r24;
                solverVariable3 = r25;
                solverVariable4 = r26;
                i7 = 0;
                i8 = 8;
                c = 1;
                i9 = 1;
            }
            if (this.mVerticalResolution == 2) {
                i10 = i7;
            } else {
                i10 = i9;
            }
            if (i10 == 0) {
            }
            if (!z6) {
                linearSystem2 = linearSystem;
            } else if (this.mResolvedDimensionRatioSide == 1) {
                linearSystem.addRatio(solverVariable3, solverVariable2, solverVariable, solverVariableCreateObjectVariable, this.mResolvedDimensionRatio, 8);
                linearSystem2 = linearSystem;
            } else {
                linearSystem.addRatio(solverVariable, solverVariableCreateObjectVariable, solverVariable3, solverVariable2, this.mResolvedDimensionRatio, 8);
                linearSystem2 = linearSystem;
            }
            if (this.mCenter.isConnected()) {
                linearSystem2.addCenterPoint(this, this.mCenter.getTarget().getOwner(), (float) Math.toRadians(this.mCircleConstraintAngle + 90.0f), this.mCenter.getMargin());
            }
            this.mResolvedHorizontal = false;
            this.mResolvedVertical = false;
        }
        i = i17;
        solverVariable = solverVariableCreateObjectVariable2;
        i2 = i20;
        i3 = i21;
        z6 = false;
        int[] iArr6 = this.mResolvedMatchConstraintDefault;
        iArr6[0] = i2;
        iArr6[1] = i3;
        this.mResolvedHasRatio = z6;
        if (z6) {
            int i214 = this.mResolvedDimensionRatioSide;
            i4 = -1;
            if (i214 != 0) {
            }
            if (z6) {
                z7 = false;
            } else {
                z7 = false;
            }
            dimensionBehaviour = this.mListDimensionBehaviors[0];
            dimensionBehaviour2 = DimensionBehaviour.WRAP_CONTENT;
            if (dimensionBehaviour == dimensionBehaviour2) {
                z8 = false;
            } else {
                z8 = false;
            }
            if (z8) {
                i5 = 0;
            } else {
                i5 = i19;
            }
            boolean z11117 = !this.mCenter.isConnected();
            boolean[] zArr12 = this.mIsInBarrier;
            boolean z11118 = zArr12[0];
            boolean z11119 = zArr12[1];
            if (this.mHorizontalResolution != 2) {
                i6 = i2;
                z9 = z3;
            } else {
                i6 = i2;
                z9 = z3;
            }
            if (z) {
                solverVariable2 = r24;
                solverVariable3 = r25;
                solverVariable4 = r26;
                i7 = 0;
                i8 = 8;
                c = 1;
                i9 = 1;
            } else {
                solverVariable2 = r24;
                solverVariable3 = r25;
                solverVariable4 = r26;
                i7 = 0;
                i8 = 8;
                c = 1;
                i9 = 1;
            }
            if (this.mVerticalResolution == 2) {
                i10 = i7;
            } else {
                i10 = i9;
            }
            if (i10 == 0) {
            }
            if (!z6) {
                linearSystem2 = linearSystem;
            } else if (this.mResolvedDimensionRatioSide == 1) {
                linearSystem.addRatio(solverVariable3, solverVariable2, solverVariable, solverVariableCreateObjectVariable, this.mResolvedDimensionRatio, 8);
                linearSystem2 = linearSystem;
            } else {
                linearSystem.addRatio(solverVariable, solverVariableCreateObjectVariable, solverVariable3, solverVariable2, this.mResolvedDimensionRatio, 8);
                linearSystem2 = linearSystem;
            }
            if (this.mCenter.isConnected()) {
                linearSystem2.addCenterPoint(this, this.mCenter.getTarget().getOwner(), (float) Math.toRadians(this.mCircleConstraintAngle + 90.0f), this.mCenter.getMargin());
            }
            this.mResolvedHorizontal = false;
            this.mResolvedVertical = false;
        }
        i4 = -1;
        if (z6) {
            z7 = false;
        } else {
            z7 = false;
        }
        dimensionBehaviour = this.mListDimensionBehaviors[0];
        dimensionBehaviour2 = DimensionBehaviour.WRAP_CONTENT;
        if (dimensionBehaviour == dimensionBehaviour2) {
            z8 = false;
        } else {
            z8 = false;
        }
        if (z8) {
            i5 = 0;
        } else {
            i5 = i19;
        }
        boolean z111110 = !this.mCenter.isConnected();
        boolean[] zArr13 = this.mIsInBarrier;
        boolean z111111 = zArr13[0];
        boolean z111112 = zArr13[1];
        if (this.mHorizontalResolution != 2) {
            i6 = i2;
            z9 = z3;
        } else {
            i6 = i2;
            z9 = z3;
        }
        if (z) {
            solverVariable2 = r24;
            solverVariable3 = r25;
            solverVariable4 = r26;
            i7 = 0;
            i8 = 8;
            c = 1;
            i9 = 1;
        } else {
            solverVariable2 = r24;
            solverVariable3 = r25;
            solverVariable4 = r26;
            i7 = 0;
            i8 = 8;
            c = 1;
            i9 = 1;
        }
        if (this.mVerticalResolution == 2) {
            i10 = i7;
        } else {
            i10 = i9;
        }
        if (i10 == 0) {
        }
        if (!z6) {
            linearSystem2 = linearSystem;
        } else if (this.mResolvedDimensionRatioSide == 1) {
            linearSystem.addRatio(solverVariable3, solverVariable2, solverVariable, solverVariableCreateObjectVariable, this.mResolvedDimensionRatio, 8);
            linearSystem2 = linearSystem;
        } else {
            linearSystem.addRatio(solverVariable, solverVariableCreateObjectVariable, solverVariable3, solverVariable2, this.mResolvedDimensionRatio, 8);
            linearSystem2 = linearSystem;
        }
        if (this.mCenter.isConnected()) {
            linearSystem2.addCenterPoint(this, this.mCenter.getTarget().getOwner(), (float) Math.toRadians(this.mCircleConstraintAngle + 90.0f), this.mCenter.getMargin());
        }
        this.mResolvedHorizontal = false;
        this.mResolvedVertical = false;
    }

    boolean addFirst() {
        return (this instanceof VirtualLayout) || (this instanceof Guideline);
    }

    public void setupDimensionRatio(boolean z, boolean z2, boolean z3, boolean z4) {
        if (this.mResolvedDimensionRatioSide == -1) {
            if (z3 && !z4) {
                this.mResolvedDimensionRatioSide = 0;
            } else if (!z3 && z4) {
                this.mResolvedDimensionRatioSide = 1;
                if (this.mDimensionRatioSide == -1) {
                    this.mResolvedDimensionRatio = 1.0f / this.mResolvedDimensionRatio;
                }
            }
        }
        if (this.mResolvedDimensionRatioSide == 0 && (!this.mTop.isConnected() || !this.mBottom.isConnected())) {
            this.mResolvedDimensionRatioSide = 1;
        } else if (this.mResolvedDimensionRatioSide == 1 && (!this.mLeft.isConnected() || !this.mRight.isConnected())) {
            this.mResolvedDimensionRatioSide = 0;
        }
        if (this.mResolvedDimensionRatioSide == -1 && (!this.mTop.isConnected() || !this.mBottom.isConnected() || !this.mLeft.isConnected() || !this.mRight.isConnected())) {
            if (this.mTop.isConnected() && this.mBottom.isConnected()) {
                this.mResolvedDimensionRatioSide = 0;
            } else if (this.mLeft.isConnected() && this.mRight.isConnected()) {
                this.mResolvedDimensionRatio = 1.0f / this.mResolvedDimensionRatio;
                this.mResolvedDimensionRatioSide = 1;
            }
        }
        if (this.mResolvedDimensionRatioSide == -1) {
            int i = this.mMatchConstraintMinWidth;
            if (i > 0 && this.mMatchConstraintMinHeight == 0) {
                this.mResolvedDimensionRatioSide = 0;
            } else {
                if (i != 0 || this.mMatchConstraintMinHeight <= 0) {
                    return;
                }
                this.mResolvedDimensionRatio = 1.0f / this.mResolvedDimensionRatio;
                this.mResolvedDimensionRatioSide = 1;
            }
        }
    }

    /* JADX WARN: Code duplicated, block: B:362:0x0582  */
    private void applyConstraints(LinearSystem linearSystem, boolean z, boolean z2, boolean z3, boolean z4, SolverVariable solverVariable, SolverVariable solverVariable2, DimensionBehaviour dimensionBehaviour, boolean z5, ConstraintAnchor constraintAnchor, ConstraintAnchor constraintAnchor2, int i, int i2, int i3, int i4, float f, boolean z6, boolean z7, boolean z8, boolean z9, boolean z10, int i5, int i6, int i7, int i8, float f2, boolean z11) {
        boolean z12;
        int iMin;
        int i9;
        SolverVariable solverVariable3;
        boolean z13;
        boolean z14;
        int i10;
        int i11;
        SolverVariable solverVariableCreateObjectVariable;
        SolverVariable solverVariableCreateObjectVariable2;
        int i12;
        char c;
        char c2;
        ConstraintAnchor constraintAnchor3;
        boolean z15;
        SolverVariable solverVariable4;
        boolean z16;
        boolean z17;
        int i13;
        int i14;
        boolean z18;
        boolean z19;
        SolverVariable solverVariable5;
        ConstraintWidget constraintWidget;
        int i15;
        boolean z20;
        int iMin2;
        int i16;
        int i17;
        int i18;
        int i19;
        int i20;
        int i21;
        int i22;
        ConstraintWidget constraintWidget2;
        int i23;
        ConstraintWidget constraintWidget3;
        linearSystem = linearSystem;
        SolverVariable solverVariableCreateObjectVariable3 = linearSystem.createObjectVariable(constraintAnchor);
        SolverVariable solverVariableCreateObjectVariable4 = linearSystem.createObjectVariable(constraintAnchor2);
        SolverVariable solverVariableCreateObjectVariable5 = linearSystem.createObjectVariable(constraintAnchor.getTarget());
        SolverVariable solverVariableCreateObjectVariable6 = linearSystem.createObjectVariable(constraintAnchor2.getTarget());
        LinearSystem.getMetrics();
        boolean zIsConnected = constraintAnchor.isConnected();
        boolean zIsConnected2 = constraintAnchor2.isConnected();
        boolean zIsConnected3 = this.mCenter.isConnected();
        int i24 = zIsConnected2 ? (zIsConnected ? 1 : 0) + 1 : zIsConnected ? 1 : 0;
        if (zIsConnected3) {
            i24++;
        }
        int i25 = z6 ? 3 : i5;
        SolverVariable solverVariable6 = solverVariableCreateObjectVariable6;
        int iOrdinal = dimensionBehaviour.ordinal();
        boolean z21 = (iOrdinal == 0 || iOrdinal == 1 || iOrdinal != 2 || i25 == 4) ? false : true;
        int i26 = this.mWidthOverride;
        if (i26 != -1 && z) {
            this.mWidthOverride = -1;
            i2 = i26;
            z21 = false;
        }
        int i27 = this.mHeightOverride;
        if (i27 == -1 || z) {
            i27 = i2;
        } else {
            this.mHeightOverride = -1;
            z21 = false;
        }
        int i28 = i27;
        if (this.mVisibility == 8) {
            iMin = 0;
            z12 = false;
        } else {
            z12 = z21;
            iMin = i28;
        }
        if (z11) {
            if (!zIsConnected && !zIsConnected2 && !zIsConnected3) {
                linearSystem.addEquality(solverVariableCreateObjectVariable3, i);
            } else if (zIsConnected && !zIsConnected2) {
                i9 = 8;
                linearSystem.addEquality(solverVariableCreateObjectVariable3, solverVariableCreateObjectVariable5, constraintAnchor.getMargin(), 8);
            }
            i9 = 8;
        } else {
            i9 = 8;
        }
        if (z12 == 0) {
            if (z5) {
                linearSystem.addEquality(solverVariableCreateObjectVariable4, solverVariableCreateObjectVariable3, 0, 3);
                if (i3 > 0) {
                    linearSystem.addGreaterThan(solverVariableCreateObjectVariable4, solverVariableCreateObjectVariable3, i3, 8);
                }
                if (i4 < Integer.MAX_VALUE) {
                    linearSystem.addLowerThan(solverVariableCreateObjectVariable4, solverVariableCreateObjectVariable3, i4, 8);
                }
            } else {
                linearSystem.addEquality(solverVariableCreateObjectVariable4, solverVariableCreateObjectVariable3, iMin, i9);
            }
            i11 = i8;
            solverVariable3 = solverVariableCreateObjectVariable4;
            i24 = i24 == true ? 1 : 0;
            solverVariable6 = solverVariable6;
            z13 = z12;
            z14 = z4;
            i10 = i7;
        } else if (i24 == 2 || z6 || !(i25 == 1 || i25 == 0)) {
            int i29 = i7 == -2 ? iMin : i7;
            int i30 = i8 == -2 ? iMin : i8;
            if (iMin > 0 && i25 != 1) {
                iMin = 0;
            }
            if (i29 > 0) {
                linearSystem.addGreaterThan(solverVariableCreateObjectVariable4, solverVariableCreateObjectVariable3, i29, 8);
                iMin = Math.max(iMin, i29);
            }
            if (i30 > 0) {
                if (!z2 || i25 != 1) {
                    linearSystem.addLowerThan(solverVariableCreateObjectVariable4, solverVariableCreateObjectVariable3, i30, 8);
                }
                iMin = Math.min(iMin, i30);
            }
            if (i25 == 1) {
                if (z2) {
                    linearSystem.addEquality(solverVariableCreateObjectVariable4, solverVariableCreateObjectVariable3, iMin, 8);
                } else if (z8) {
                    linearSystem.addEquality(solverVariableCreateObjectVariable4, solverVariableCreateObjectVariable3, iMin, 5);
                    linearSystem.addLowerThan(solverVariableCreateObjectVariable4, solverVariableCreateObjectVariable3, iMin, 8);
                } else {
                    linearSystem.addEquality(solverVariableCreateObjectVariable4, solverVariableCreateObjectVariable3, iMin, 5);
                    linearSystem.addLowerThan(solverVariableCreateObjectVariable4, solverVariableCreateObjectVariable3, iMin, 8);
                }
                solverVariable3 = solverVariableCreateObjectVariable4;
                solverVariable6 = solverVariable6;
                z13 = z12;
                z14 = z4;
                i10 = i29;
                i11 = i30;
                i24 = i24 == true ? 1 : 0;
            } else {
                if (i25 == 2) {
                    ConstraintAnchor.Type type = constraintAnchor.getType();
                    ConstraintAnchor.Type type2 = ConstraintAnchor.Type.TOP;
                    if (type == type2 || constraintAnchor.getType() == ConstraintAnchor.Type.BOTTOM) {
                        solverVariableCreateObjectVariable = linearSystem.createObjectVariable(this.mParent.getAnchor(type2));
                        solverVariableCreateObjectVariable2 = linearSystem.createObjectVariable(this.mParent.getAnchor(ConstraintAnchor.Type.BOTTOM));
                    } else {
                        solverVariableCreateObjectVariable = linearSystem.createObjectVariable(this.mParent.getAnchor(ConstraintAnchor.Type.LEFT));
                        solverVariableCreateObjectVariable2 = linearSystem.createObjectVariable(this.mParent.getAnchor(ConstraintAnchor.Type.RIGHT));
                    }
                    SolverVariable solverVariable7 = solverVariableCreateObjectVariable2;
                    solverVariable3 = solverVariableCreateObjectVariable4;
                    linearSystem.addConstraint(linearSystem.createRow().createRowDimensionRatio(solverVariable3, solverVariableCreateObjectVariable3, solverVariable7, solverVariableCreateObjectVariable, f2));
                    if (z2) {
                        z12 = false;
                    }
                    z14 = z4;
                    z13 = z12;
                } else {
                    solverVariable3 = solverVariableCreateObjectVariable4;
                    z13 = z12;
                    z14 = true;
                }
                i10 = i29;
                i11 = i30;
            }
        } else {
            int iMax = Math.max(i7, iMin);
            if (i8 > 0) {
                iMax = Math.min(i8, iMax);
            }
            linearSystem.addEquality(solverVariableCreateObjectVariable4, solverVariableCreateObjectVariable3, iMax, 8);
            i10 = i7;
            i11 = i8;
            solverVariable3 = solverVariableCreateObjectVariable4;
            i24 = i24 == true ? 1 : 0;
            solverVariable6 = solverVariable6;
            z13 = false;
            z14 = z4;
        }
        if (!z11) {
            i12 = 8;
            c = 1;
            c2 = 2;
        } else {
            if (!z8) {
                if (!zIsConnected && !zIsConnected2 && !zIsConnected3) {
                    i17 = 5;
                    z20 = z2;
                    i23 = i17;
                } else if (!zIsConnected || zIsConnected2) {
                    if (zIsConnected || !zIsConnected2) {
                        if (zIsConnected && zIsConnected2) {
                            ConstraintWidget constraintWidget4 = constraintAnchor.mTarget.mOwner;
                            ConstraintWidget constraintWidget5 = constraintAnchor2.mTarget.mOwner;
                            ConstraintWidget parent = getParent();
                            int i31 = 6;
                            if (!z13) {
                                z15 = true;
                                if (solverVariableCreateObjectVariable5.isFinalValue && solverVariable6.isFinalValue) {
                                    SolverVariable solverVariable8 = solverVariable6;
                                    linearSystem.addCentering(solverVariableCreateObjectVariable3, solverVariableCreateObjectVariable5, constraintAnchor.getMargin(), f, solverVariable8, solverVariable3, constraintAnchor2.getMargin(), 8);
                                    if (z2 && z14) {
                                        int margin = constraintAnchor2.mTarget != null ? constraintAnchor2.getMargin() : 0;
                                        if (solverVariable8 != solverVariable2) {
                                            linearSystem.addGreaterThan(solverVariable2, solverVariable3, margin, 5);
                                            return;
                                        }
                                        return;
                                    }
                                    return;
                                }
                                SolverVariable solverVariable9 = solverVariable6;
                                solverVariableCreateObjectVariable3 = solverVariableCreateObjectVariable3;
                                solverVariable4 = solverVariable9;
                                linearSystem = linearSystem;
                                solverVariableCreateObjectVariable5 = solverVariableCreateObjectVariable5;
                                z16 = true;
                                z17 = true;
                                i31 = 6;
                                i13 = 5;
                                i14 = 4;
                                z18 = false;
                            } else if (i25 == 0) {
                                if (i11 != 0 || i10 != 0) {
                                    i21 = 5;
                                    i22 = 5;
                                    z17 = true;
                                    z18 = false;
                                    z16 = true;
                                } else if (solverVariableCreateObjectVariable5.isFinalValue && solverVariable6.isFinalValue) {
                                    linearSystem.addEquality(solverVariableCreateObjectVariable3, solverVariableCreateObjectVariable5, constraintAnchor.getMargin(), 8);
                                    linearSystem.addEquality(solverVariable3, solverVariable6, -constraintAnchor2.getMargin(), 8);
                                    return;
                                } else {
                                    i21 = 8;
                                    i22 = 8;
                                    z17 = false;
                                    z18 = true;
                                    z16 = false;
                                }
                                if ((constraintWidget4 instanceof Barrier) || (constraintWidget5 instanceof Barrier)) {
                                    solverVariable4 = solverVariable6;
                                    z15 = true;
                                    i14 = 4;
                                } else {
                                    solverVariable4 = solverVariable6;
                                    i14 = i22;
                                    z15 = true;
                                }
                                i13 = i21;
                                solverVariableCreateObjectVariable5 = solverVariableCreateObjectVariable5;
                                i31 = 6;
                            } else {
                                if (i25 == 2) {
                                    if ((constraintWidget4 instanceof Barrier) || (constraintWidget5 instanceof Barrier)) {
                                        solverVariable4 = solverVariable6;
                                        z15 = true;
                                        i14 = 4;
                                    } else {
                                        solverVariable4 = solverVariable6;
                                        z15 = true;
                                        i14 = 5;
                                    }
                                    i13 = 5;
                                } else if (i25 == 1) {
                                    SolverVariable solverVariable10 = solverVariable6;
                                    solverVariableCreateObjectVariable3 = solverVariableCreateObjectVariable3;
                                    solverVariable4 = solverVariable10;
                                    solverVariableCreateObjectVariable5 = solverVariableCreateObjectVariable5;
                                    i31 = 6;
                                    z15 = true;
                                    i14 = 4;
                                    i13 = 8;
                                } else if (i25 != 3) {
                                    z15 = true;
                                    SolverVariable solverVariable11 = solverVariable6;
                                    solverVariableCreateObjectVariable3 = solverVariableCreateObjectVariable3;
                                    solverVariable4 = solverVariable11;
                                    linearSystem = linearSystem;
                                    solverVariableCreateObjectVariable5 = solverVariableCreateObjectVariable5;
                                    i31 = 6;
                                    i14 = 4;
                                    i13 = 5;
                                    z16 = false;
                                    z17 = false;
                                    z18 = false;
                                } else if (this.mResolvedDimensionRatioSide == -1) {
                                    if (z9) {
                                        SolverVariable solverVariable12 = solverVariable6;
                                        solverVariableCreateObjectVariable3 = solverVariableCreateObjectVariable3;
                                        solverVariable4 = solverVariable12;
                                        linearSystem = linearSystem;
                                        solverVariableCreateObjectVariable5 = solverVariableCreateObjectVariable5;
                                        z15 = true;
                                        i31 = z2 ? 5 : 4;
                                    } else {
                                        SolverVariable solverVariable13 = solverVariable6;
                                        solverVariableCreateObjectVariable3 = solverVariableCreateObjectVariable3;
                                        solverVariable4 = solverVariable13;
                                        linearSystem = linearSystem;
                                        solverVariableCreateObjectVariable5 = solverVariableCreateObjectVariable5;
                                        z15 = true;
                                        i31 = 8;
                                    }
                                    i14 = 5;
                                    i13 = 8;
                                    z16 = true;
                                    z17 = true;
                                    z18 = true;
                                } else {
                                    if (z6) {
                                        if (i6 != 2) {
                                            z15 = true;
                                            if (i6 != 1) {
                                                i19 = 8;
                                                i20 = 5;
                                            }
                                            solverVariable4 = solverVariable6;
                                            i13 = i19;
                                            i14 = i20;
                                            z16 = z15;
                                            z17 = z16;
                                            z18 = z17;
                                        } else {
                                            z15 = true;
                                        }
                                        i19 = 5;
                                        i20 = 4;
                                        solverVariable4 = solverVariable6;
                                        i13 = i19;
                                        i14 = i20;
                                        z16 = z15;
                                        z17 = z16;
                                        z18 = z17;
                                    } else {
                                        z15 = true;
                                        if (i11 > 0) {
                                            solverVariable4 = solverVariable6;
                                            z16 = true;
                                            z17 = true;
                                            z18 = true;
                                            i14 = 5;
                                        } else if (i11 != 0 || i10 != 0) {
                                            solverVariable4 = solverVariable6;
                                            z16 = true;
                                            z17 = true;
                                            z18 = true;
                                            i14 = 4;
                                        } else if (z9) {
                                            solverVariable4 = solverVariable6;
                                            i13 = (constraintWidget4 == parent || constraintWidget5 == parent) ? 5 : 4;
                                            z16 = true;
                                            z17 = true;
                                            z18 = true;
                                            i14 = 4;
                                        } else {
                                            solverVariable4 = solverVariable6;
                                            z16 = true;
                                            z17 = true;
                                            z18 = true;
                                            i14 = 8;
                                        }
                                        i13 = 5;
                                    }
                                    linearSystem = linearSystem;
                                }
                                z16 = true;
                                z17 = true;
                                z18 = false;
                            }
                            if (z16 && solverVariableCreateObjectVariable5 == solverVariable4 && constraintWidget4 != parent) {
                                z16 = false;
                                z19 = false;
                            } else {
                                z19 = z15;
                            }
                            if (z17) {
                                if (z13 || z7 || z9 || solverVariableCreateObjectVariable5 != solverVariable || solverVariable4 != solverVariable2) {
                                    i18 = i31;
                                    z20 = z2;
                                } else {
                                    i18 = 8;
                                    z20 = false;
                                    i13 = 8;
                                    z19 = false;
                                }
                                SolverVariable solverVariable14 = solverVariableCreateObjectVariable3;
                                constraintWidget = parent;
                                i15 = 8;
                                SolverVariable solverVariable15 = solverVariable3;
                                linearSystem.addCentering(solverVariable14, solverVariableCreateObjectVariable5, constraintAnchor.getMargin(), f, solverVariable4, solverVariable15, constraintAnchor2.getMargin(), i18);
                                SolverVariable solverVariable16 = solverVariable4;
                                solverVariable5 = solverVariable14;
                                solverVariable6 = solverVariable16;
                                solverVariable3 = solverVariable15;
                            } else {
                                solverVariable6 = solverVariable4;
                                solverVariable5 = solverVariableCreateObjectVariable3;
                                constraintWidget = parent;
                                z15 = z15;
                                i15 = 8;
                                z20 = z2;
                            }
                            if (this.mVisibility == i15 && !constraintAnchor2.hasDependents()) {
                                return;
                            }
                            if (z16) {
                                int i32 = (!z20 || solverVariableCreateObjectVariable5 == solverVariable6 || z13 || !((constraintWidget4 instanceof Barrier) || (constraintWidget5 instanceof Barrier))) ? i13 : 6;
                                linearSystem.addGreaterThan(solverVariable5, solverVariableCreateObjectVariable5, constraintAnchor.getMargin(), i32);
                                linearSystem.addLowerThan(solverVariable3, solverVariable6, -constraintAnchor2.getMargin(), i32);
                                i13 = i32;
                            }
                            if (!z20 || !z10 || (constraintWidget4 instanceof Barrier) || (constraintWidget5 instanceof Barrier) || constraintWidget5 == constraintWidget) {
                                iMin2 = i14;
                                i16 = i13;
                                z15 = z19;
                            } else {
                                iMin2 = 6;
                                i16 = 6;
                            }
                            if (z15) {
                                if (z18 && (!z9 || z3)) {
                                    if (constraintWidget4 != constraintWidget && constraintWidget5 != constraintWidget) {
                                        i31 = iMin2;
                                    }
                                    if ((constraintWidget4 instanceof Guideline) || (constraintWidget5 instanceof Guideline)) {
                                        i31 = 5;
                                    }
                                    if ((constraintWidget4 instanceof Barrier) || (constraintWidget5 instanceof Barrier)) {
                                        i31 = 5;
                                    }
                                    iMin2 = Math.max(z9 ? 5 : i31, iMin2);
                                }
                                if (z20) {
                                    iMin2 = Math.min(i16, iMin2);
                                    if (z6 && !z9 && (constraintWidget4 == constraintWidget || constraintWidget5 == constraintWidget)) {
                                        iMin2 = 4;
                                    }
                                }
                                linearSystem.addEquality(solverVariable5, solverVariableCreateObjectVariable5, constraintAnchor.getMargin(), iMin2);
                                linearSystem.addEquality(solverVariable3, solverVariable6, -constraintAnchor2.getMargin(), iMin2);
                            }
                            if (z20) {
                                int margin2 = solverVariable == solverVariableCreateObjectVariable5 ? constraintAnchor.getMargin() : 0;
                                if (solverVariableCreateObjectVariable5 != solverVariable) {
                                    linearSystem.addGreaterThan(solverVariable5, solverVariable, margin2, 5);
                                }
                            }
                            if (!z20 || !z13 || i3 != 0 || i10 != 0) {
                                i17 = 5;
                            } else if (z13 && i25 == 3) {
                                linearSystem.addGreaterThan(solverVariable3, solverVariable5, 0, i15);
                                i17 = 5;
                            } else {
                                i17 = 5;
                                linearSystem.addGreaterThan(solverVariable3, solverVariable5, 0, 5);
                            }
                        }
                        i23 = i17;
                    } else {
                        linearSystem.addEquality(solverVariable3, solverVariable6, -constraintAnchor2.getMargin(), 8);
                        if (z2) {
                            if (this.mOptimizeWrapO && solverVariableCreateObjectVariable3.isFinalValue && (constraintWidget2 = this.mParent) != null) {
                                ConstraintWidgetContainer constraintWidgetContainer = (ConstraintWidgetContainer) constraintWidget2;
                                if (z) {
                                    constraintWidgetContainer.addHorizontalWrapMinVariable(constraintAnchor);
                                } else {
                                    constraintWidgetContainer.addVerticalWrapMinVariable(constraintAnchor);
                                }
                            } else {
                                i17 = 5;
                                linearSystem.addGreaterThan(solverVariableCreateObjectVariable3, solverVariable, 0, 5);
                            }
                        }
                        z20 = z2;
                        i23 = i17;
                    }
                    i17 = 5;
                    z20 = z2;
                    i23 = i17;
                } else {
                    i23 = (z2 && (constraintAnchor.mTarget.mOwner instanceof Barrier)) ? 8 : 5;
                    z20 = z2;
                    solverVariable6 = solverVariable6;
                }
                if (z20 && z14) {
                    int margin3 = constraintAnchor2.mTarget != null ? constraintAnchor2.getMargin() : 0;
                    if (solverVariable6 != solverVariable2) {
                        if (this.mOptimizeWrapO && solverVariable3.isFinalValue && (constraintWidget3 = this.mParent) != null) {
                            ConstraintWidgetContainer constraintWidgetContainer2 = (ConstraintWidgetContainer) constraintWidget3;
                            if (z) {
                                constraintWidgetContainer2.addHorizontalWrapMaxVariable(constraintAnchor2);
                                return;
                            } else {
                                constraintWidgetContainer2.addVerticalWrapMaxVariable(constraintAnchor2);
                                return;
                            }
                        }
                        linearSystem.addGreaterThan(solverVariable2, solverVariable3, margin3, i23);
                        return;
                    }
                    return;
                }
                return;
            }
            c2 = 2;
            i12 = 8;
            c = 1;
        }
        if (i24 < c2 && z2 && z14) {
            linearSystem.addGreaterThan(solverVariableCreateObjectVariable3, solverVariable, 0, i12);
            char c3 = (z || this.mBaseline.mTarget == null) ? c : (char) 0;
            if (!z && (constraintAnchor3 = this.mBaseline.mTarget) != null) {
                ConstraintWidget constraintWidget6 = constraintAnchor3.mOwner;
                if (constraintWidget6.mDimensionRatio != 0.0f) {
                    DimensionBehaviour[] dimensionBehaviourArr = constraintWidget6.mListDimensionBehaviors;
                    DimensionBehaviour dimensionBehaviour2 = dimensionBehaviourArr[0];
                    DimensionBehaviour dimensionBehaviour3 = DimensionBehaviour.MATCH_CONSTRAINT;
                    if (dimensionBehaviour2 == dimensionBehaviour3 && dimensionBehaviourArr[c] == dimensionBehaviour3) {
                        c3 = c;
                    } else {
                        c3 = 0;
                    }
                } else {
                    c3 = 0;
                }
            }
            if (c3 != 0) {
                linearSystem.addGreaterThan(solverVariable2, solverVariable3, 0, i12);
            }
        }
    }

    public void updateFromSolver(LinearSystem linearSystem, boolean z) {
        VerticalWidgetRun verticalWidgetRun;
        HorizontalWidgetRun horizontalWidgetRun;
        int objectVariableValue = linearSystem.getObjectVariableValue(this.mLeft);
        int objectVariableValue2 = linearSystem.getObjectVariableValue(this.mTop);
        int objectVariableValue3 = linearSystem.getObjectVariableValue(this.mRight);
        int objectVariableValue4 = linearSystem.getObjectVariableValue(this.mBottom);
        if (z && (horizontalWidgetRun = this.mHorizontalRun) != null) {
            DependencyNode dependencyNode = horizontalWidgetRun.start;
            if (dependencyNode.resolved) {
                DependencyNode dependencyNode2 = horizontalWidgetRun.end;
                if (dependencyNode2.resolved) {
                    objectVariableValue = dependencyNode.value;
                    objectVariableValue3 = dependencyNode2.value;
                }
            }
        }
        if (z && (verticalWidgetRun = this.mVerticalRun) != null) {
            DependencyNode dependencyNode3 = verticalWidgetRun.start;
            if (dependencyNode3.resolved) {
                DependencyNode dependencyNode4 = verticalWidgetRun.end;
                if (dependencyNode4.resolved) {
                    objectVariableValue2 = dependencyNode3.value;
                    objectVariableValue4 = dependencyNode4.value;
                }
            }
        }
        int i = objectVariableValue4 - objectVariableValue2;
        if (objectVariableValue3 - objectVariableValue < 0 || i < 0 || objectVariableValue == Integer.MIN_VALUE || objectVariableValue == Integer.MAX_VALUE || objectVariableValue2 == Integer.MIN_VALUE || objectVariableValue2 == Integer.MAX_VALUE || objectVariableValue3 == Integer.MIN_VALUE || objectVariableValue3 == Integer.MAX_VALUE || objectVariableValue4 == Integer.MIN_VALUE || objectVariableValue4 == Integer.MAX_VALUE) {
            objectVariableValue = 0;
            objectVariableValue4 = 0;
            objectVariableValue2 = 0;
            objectVariableValue3 = 0;
        }
        setFrame(objectVariableValue, objectVariableValue2, objectVariableValue3, objectVariableValue4);
    }

    public void updateFromRuns(boolean z, boolean z2) {
        int i;
        int i2;
        boolean zIsResolved = z & this.mHorizontalRun.isResolved();
        boolean zIsResolved2 = z2 & this.mVerticalRun.isResolved();
        HorizontalWidgetRun horizontalWidgetRun = this.mHorizontalRun;
        int i3 = horizontalWidgetRun.start.value;
        VerticalWidgetRun verticalWidgetRun = this.mVerticalRun;
        int i4 = verticalWidgetRun.start.value;
        int i5 = horizontalWidgetRun.end.value;
        int i6 = verticalWidgetRun.end.value;
        int i7 = i6 - i4;
        if (i5 - i3 < 0 || i7 < 0 || i3 == Integer.MIN_VALUE || i3 == Integer.MAX_VALUE || i4 == Integer.MIN_VALUE || i4 == Integer.MAX_VALUE || i5 == Integer.MIN_VALUE || i5 == Integer.MAX_VALUE || i6 == Integer.MIN_VALUE || i6 == Integer.MAX_VALUE) {
            i5 = 0;
            i3 = 0;
            i6 = 0;
            i4 = 0;
        }
        int i8 = i5 - i3;
        int i9 = i6 - i4;
        if (zIsResolved) {
            this.mX = i3;
        }
        if (zIsResolved2) {
            this.mY = i4;
        }
        if (this.mVisibility == 8) {
            this.mWidth = 0;
            this.mHeight = 0;
            return;
        }
        if (zIsResolved) {
            if (this.mListDimensionBehaviors[0] == DimensionBehaviour.FIXED && i8 < (i2 = this.mWidth)) {
                i8 = i2;
            }
            this.mWidth = i8;
            int i10 = this.mMinWidth;
            if (i8 < i10) {
                this.mWidth = i10;
            }
        }
        if (zIsResolved2) {
            if (this.mListDimensionBehaviors[1] == DimensionBehaviour.FIXED && i9 < (i = this.mHeight)) {
                i9 = i;
            }
            this.mHeight = i9;
            int i11 = this.mMinHeight;
            if (i9 < i11) {
                this.mHeight = i11;
            }
        }
    }

    public void addChildrenToSolverByDependency(ConstraintWidgetContainer constraintWidgetContainer, LinearSystem linearSystem, HashSet hashSet, int i, boolean z) {
        if (z) {
            if (!hashSet.contains(this)) {
                return;
            }
            Optimizer.checkMatchParent(constraintWidgetContainer, linearSystem, this);
            hashSet.remove(this);
            addToSolver(linearSystem, constraintWidgetContainer.optimizeFor(64));
        }
        if (i == 0) {
            HashSet dependents = this.mLeft.getDependents();
            if (dependents != null) {
                Iterator it = dependents.iterator();
                while (it.hasNext()) {
                    ((ConstraintAnchor) it.next()).mOwner.addChildrenToSolverByDependency(constraintWidgetContainer, linearSystem, hashSet, i, true);
                }
            }
            HashSet dependents2 = this.mRight.getDependents();
            if (dependents2 != null) {
                Iterator it2 = dependents2.iterator();
                while (it2.hasNext()) {
                    ((ConstraintAnchor) it2.next()).mOwner.addChildrenToSolverByDependency(constraintWidgetContainer, linearSystem, hashSet, i, true);
                }
                return;
            }
            return;
        }
        HashSet dependents3 = this.mTop.getDependents();
        if (dependents3 != null) {
            Iterator it3 = dependents3.iterator();
            while (it3.hasNext()) {
                ((ConstraintAnchor) it3.next()).mOwner.addChildrenToSolverByDependency(constraintWidgetContainer, linearSystem, hashSet, i, true);
            }
        }
        HashSet dependents4 = this.mBottom.getDependents();
        if (dependents4 != null) {
            Iterator it4 = dependents4.iterator();
            while (it4.hasNext()) {
                ((ConstraintAnchor) it4.next()).mOwner.addChildrenToSolverByDependency(constraintWidgetContainer, linearSystem, hashSet, i, true);
            }
        }
        HashSet dependents5 = this.mBaseline.getDependents();
        if (dependents5 != null) {
            Iterator it5 = dependents5.iterator();
            while (it5.hasNext()) {
                ((ConstraintAnchor) it5.next()).mOwner.addChildrenToSolverByDependency(constraintWidgetContainer, linearSystem, hashSet, i, true);
            }
        }
    }

    public void getSceneString(StringBuilder sb) {
        sb.append("  " + this.stringId + ":{\n");
        StringBuilder sb2 = new StringBuilder();
        sb2.append("    actualWidth:");
        sb2.append(this.mWidth);
        sb.append(sb2.toString());
        sb.append("\n");
        sb.append("    actualHeight:" + this.mHeight);
        sb.append("\n");
        sb.append("    actualLeft:" + this.mX);
        sb.append("\n");
        sb.append("    actualTop:" + this.mY);
        sb.append("\n");
        getSceneString(sb, "left", this.mLeft);
        getSceneString(sb, "top", this.mTop);
        getSceneString(sb, "right", this.mRight);
        getSceneString(sb, "bottom", this.mBottom);
        getSceneString(sb, "baseline", this.mBaseline);
        getSceneString(sb, "centerX", this.mCenterX);
        getSceneString(sb, "centerY", this.mCenterY);
        getSceneString(sb, "    width", this.mWidth, this.mMinWidth, this.mMaxDimension[0], this.mWidthOverride, this.mMatchConstraintMinWidth, this.mMatchConstraintDefaultWidth, this.mMatchConstraintPercentWidth, this.mListDimensionBehaviors[0], this.mWeight[0]);
        getSceneString(sb, "    height", this.mHeight, this.mMinHeight, this.mMaxDimension[1], this.mHeightOverride, this.mMatchConstraintMinHeight, this.mMatchConstraintDefaultHeight, this.mMatchConstraintPercentHeight, this.mListDimensionBehaviors[1], this.mWeight[1]);
        serializeDimensionRatio(sb, "    dimensionRatio", this.mDimensionRatio, this.mDimensionRatioSide);
        serializeAttribute(sb, "    horizontalBias", this.mHorizontalBiasPercent, DEFAULT_BIAS);
        serializeAttribute(sb, "    verticalBias", this.mVerticalBiasPercent, DEFAULT_BIAS);
        serializeAttribute(sb, "    horizontalChainStyle", this.mHorizontalChainStyle, 0);
        serializeAttribute(sb, "    verticalChainStyle", this.mVerticalChainStyle, 0);
        sb.append("  }");
    }

    private void getSceneString(StringBuilder sb, String str, int i, int i2, int i3, int i4, int i5, int i6, float f, DimensionBehaviour dimensionBehaviour, float f2) {
        sb.append(str);
        sb.append(" :  {\n");
        serializeAttribute(sb, "      behavior", dimensionBehaviour.toString(), DimensionBehaviour.FIXED.toString());
        serializeAttribute(sb, "      size", i, 0);
        serializeAttribute(sb, "      min", i2, 0);
        serializeAttribute(sb, "      max", i3, Integer.MAX_VALUE);
        serializeAttribute(sb, "      matchMin", i5, 0);
        serializeAttribute(sb, "      matchDef", i6, 0);
        serializeAttribute(sb, "      matchPercent", f, 1.0f);
        sb.append("    },\n");
    }

    private void getSceneString(StringBuilder sb, String str, ConstraintAnchor constraintAnchor) {
        if (constraintAnchor.mTarget == null) {
            return;
        }
        sb.append("    ");
        sb.append(str);
        sb.append(" : [ '");
        sb.append(constraintAnchor.mTarget);
        sb.append("'");
        if (constraintAnchor.mGoneMargin != Integer.MIN_VALUE || constraintAnchor.mMargin != 0) {
            sb.append(",");
            sb.append(constraintAnchor.mMargin);
            if (constraintAnchor.mGoneMargin != Integer.MIN_VALUE) {
                sb.append(",");
                sb.append(constraintAnchor.mGoneMargin);
                sb.append(",");
            }
        }
        sb.append(" ] ,\n");
    }
}
