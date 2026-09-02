package androidx.recyclerview.widget;

import android.content.Context;
import android.graphics.Rect;
import android.view.View;
import android.view.ViewGroup;
import java.util.ArrayList;
import java.util.Arrays;
import org.telegram.tgnet.TLObject;

public abstract class GridLayoutManagerFixed extends GridLayoutManager {
    private ArrayList additionalViews;
    private boolean canScrollVertically;

    protected abstract boolean hasSiblingChild(int i);

    public abstract boolean shouldLayoutChildFromOpositeSide(View view);

    public GridLayoutManagerFixed(Context context, int i, int i2, boolean z) {
        super(context, i, i2, z);
        this.additionalViews = new ArrayList(4);
        this.canScrollVertically = true;
    }

    public void setCanScrollVertically(boolean z) {
        this.canScrollVertically = z;
    }

    @Override // androidx.recyclerview.widget.LinearLayoutManager, androidx.recyclerview.widget.RecyclerView.LayoutManager
    public boolean canScrollVertically() {
        return this.canScrollVertically;
    }

    @Override // androidx.recyclerview.widget.LinearLayoutManager
    protected void recycleViewsFromStart(RecyclerView.Recycler recycler, int i, int i2) {
        if (i < 0) {
            return;
        }
        int childCount = getChildCount();
        if (!this.mShouldReverseLayout) {
            for (int i3 = 0; i3 < childCount; i3++) {
                View childAt = getChildAt(i3);
                if (childAt.getBottom() + ((ViewGroup.MarginLayoutParams) ((RecyclerView.LayoutParams) childAt.getLayoutParams())).bottomMargin > i || childAt.getTop() + childAt.getHeight() > i) {
                    recycleChildren(recycler, 0, i3);
                    return;
                }
            }
            return;
        }
        int i4 = childCount - 1;
        for (int i5 = i4; i5 >= 0; i5--) {
            View childAt2 = getChildAt(i5);
            if (childAt2.getBottom() + ((ViewGroup.MarginLayoutParams) ((RecyclerView.LayoutParams) childAt2.getLayoutParams())).bottomMargin > i || childAt2.getTop() + childAt2.getHeight() > i) {
                recycleChildren(recycler, i4, i5);
                return;
            }
        }
    }

    @Override // androidx.recyclerview.widget.GridLayoutManager
    protected int[] calculateItemBorders(int[] iArr, int i, int i2) {
        if (iArr == null || iArr.length != i + 1 || iArr[iArr.length - 1] != i2) {
            iArr = new int[i + 1];
        }
        iArr[0] = 0;
        for (int i3 = 1; i3 <= i; i3++) {
            iArr[i3] = (int) Math.ceil((i3 / i) * i2);
        }
        return iArr;
    }

    @Override // androidx.recyclerview.widget.GridLayoutManager
    protected void measureChild(View view, int i, boolean z) {
        GridLayoutManager.LayoutParams layoutParams = (GridLayoutManager.LayoutParams) view.getLayoutParams();
        Rect rect = layoutParams.mDecorInsets;
        int i2 = rect.top + rect.bottom + ((ViewGroup.MarginLayoutParams) layoutParams).topMargin + ((ViewGroup.MarginLayoutParams) layoutParams).bottomMargin;
        measureChildWithDecorationsAndMargin(view, RecyclerView.LayoutManager.getChildMeasureSpec(this.mCachedBorders[layoutParams.mSpanSize], i, rect.left + rect.right + ((ViewGroup.MarginLayoutParams) layoutParams).leftMargin + ((ViewGroup.MarginLayoutParams) layoutParams).rightMargin, ((ViewGroup.MarginLayoutParams) layoutParams).width, false), RecyclerView.LayoutManager.getChildMeasureSpec(this.mOrientationHelper.getTotalSpace(), getHeightMode(), i2, ((ViewGroup.MarginLayoutParams) layoutParams).height, true), z);
    }

    /* JADX WARN: Code duplicated, block: B:104:0x01f1  */
    /* JADX WARN: Code duplicated, block: B:87:0x0197 A[PHI: r5
  0x0197: PHI (r5v4 int) = (r5v1 int), (r5v9 int) binds: [B:86:0x0195, B:81:0x018c] A[DONT_GENERATE, DONT_INLINE]] */
    /* JADX WARN: Code duplicated, block: B:89:0x019b  */
    /* JADX WARN: Code duplicated, block: B:90:0x01a5  */
    /* JADX WARN: Code duplicated, block: B:93:0x01ba  */
    /* JADX WARN: Code duplicated, block: B:95:0x01cf  */
    /* JADX WARN: Code duplicated, block: B:98:0x01e2  */
    /* JADX WARN: Code duplicated, block: B:99:0x01e4  */
    /* JADX WARN: Multi-variable type inference failed */
    /* JADX WARN: Type inference failed for: r12v0 */
    /* JADX WARN: Type inference failed for: r12v1, types: [boolean, int] */
    /* JADX WARN: Type inference failed for: r12v4 */
    /* JADX WARN: Type inference failed for: r15v0 */
    /* JADX WARN: Type inference failed for: r15v1 */
    /* JADX WARN: Type inference failed for: r15v2 */
    /* JADX WARN: Type inference failed for: r15v3 */
    /* JADX WARN: Type inference failed for: r15v4 */
    /* JADX WARN: Type inference failed for: r1v25 */
    /* JADX WARN: Type inference failed for: r1v3 */
    /* JADX WARN: Type inference failed for: r1v4 */
    /* JADX WARN: Type inference failed for: r2v3 */
    /* JADX WARN: Type inference failed for: r2v39 */
    /* JADX WARN: Type inference failed for: r2v42 */
    /* JADX WARN: Type inference failed for: r5v26, types: [int] */
    /* JADX WARN: Type inference failed for: r6v17 */
    @Override // androidx.recyclerview.widget.GridLayoutManager, androidx.recyclerview.widget.LinearLayoutManager
    void layoutChunk(RecyclerView.Recycler recycler, RecyclerView.State state, LinearLayoutManager.LayoutState layoutState, LinearLayoutManager.LayoutChunkResult layoutChunkResult) {
        int i;
        int i2;
        int i3;
        int width;
        int i4;
        int i5;
        int i6;
        int width2;
        int i7;
        GridLayoutManager.LayoutParams layoutParams;
        int decoratedMeasurementInOther;
        int i8;
        int i9;
        boolean z;
        View next;
        GridLayoutManagerFixed gridLayoutManagerFixed = this;
        RecyclerView.Recycler recycler2 = recycler;
        int modeInOther = gridLayoutManagerFixed.mOrientationHelper.getModeInOther();
        boolean z2 = false;
        ?? r12 = 1;
        boolean z3 = layoutState.mItemDirection == 1;
        layoutChunkResult.mConsumed = 0;
        int i10 = layoutState.mCurrentPosition;
        int i11 = -1;
        if (gridLayoutManagerFixed.mShouldReverseLayout && layoutState.mLayoutDirection != -1 && gridLayoutManagerFixed.hasSiblingChild(i10) && gridLayoutManagerFixed.findViewByPosition(layoutState.mCurrentPosition + 1) == null) {
            if (gridLayoutManagerFixed.hasSiblingChild(layoutState.mCurrentPosition + 1)) {
                layoutState.mCurrentPosition += 3;
            } else {
                layoutState.mCurrentPosition += 2;
            }
            int i12 = layoutState.mCurrentPosition;
            for (int i13 = i12; i13 > i10; i13--) {
                View next2 = layoutState.next(recycler2);
                if (next2 != null) {
                    gridLayoutManagerFixed.additionalViews.add(next2);
                    if (i13 != i12) {
                        gridLayoutManagerFixed.calculateItemDecorationsForChild(next2, gridLayoutManagerFixed.mDecorInsets);
                        gridLayoutManagerFixed.measureChild(next2, modeInOther, false);
                        int decoratedMeasurement = gridLayoutManagerFixed.mOrientationHelper.getDecoratedMeasurement(next2);
                        layoutState.mOffset -= decoratedMeasurement;
                        layoutState.mAvailable += decoratedMeasurement;
                    }
                }
            }
            layoutState.mCurrentPosition = i12;
        }
        ?? r1 = 1;
        while (r1 != 0) {
            int spanSize = gridLayoutManagerFixed.mSpanCount;
            ?? r15 = (gridLayoutManagerFixed.additionalViews.isEmpty() ? 1 : 0) ^ r12;
            int i14 = z2 ? 1 : 0;
            while (i14 < gridLayoutManagerFixed.mSpanCount && layoutState.hasMore(state) && spanSize > 0) {
                int i15 = layoutState.mCurrentPosition;
                spanSize -= gridLayoutManagerFixed.getSpanSize(recycler2, state, i15);
                if (spanSize < 0) {
                    break;
                }
                if (!gridLayoutManagerFixed.additionalViews.isEmpty()) {
                    next = (View) gridLayoutManagerFixed.additionalViews.get(z2 ? 1 : 0);
                    gridLayoutManagerFixed.additionalViews.remove(z2 ? 1 : 0);
                    layoutState.mCurrentPosition -= r12;
                } else {
                    next = layoutState.next(recycler2);
                }
                if (next == null) {
                    break;
                }
                gridLayoutManagerFixed.mSet[i14 == true ? 1 : 0] = next;
                i14 = (i14 == true ? 1 : 0) + 1;
                if (layoutState.mLayoutDirection == i11 && spanSize <= 0 && gridLayoutManagerFixed.hasSiblingChild(i15)) {
                    r15 = r12;
                }
            }
            if (i14 == 0) {
                layoutChunkResult.mFinished = r12;
                return;
            }
            gridLayoutManagerFixed.assignSpans(recycler2, state, i14 == true ? 1 : 0, z3);
            float f = 0.0f;
            int i16 = z2 ? 1 : 0;
            int i17 = i16;
            while (i16 < i14) {
                View view = gridLayoutManagerFixed.mSet[i16];
                if (layoutState.mScrapList == null) {
                    if (z3) {
                        gridLayoutManagerFixed.addView(view);
                    } else {
                        gridLayoutManagerFixed.addView(view, z2 ? 1 : 0);
                    }
                } else if (z3) {
                    gridLayoutManagerFixed.addDisappearingView(view);
                } else {
                    gridLayoutManagerFixed.addDisappearingView(view, z2 ? 1 : 0);
                }
                gridLayoutManagerFixed.calculateItemDecorationsForChild(view, gridLayoutManagerFixed.mDecorInsets);
                gridLayoutManagerFixed.measureChild(view, modeInOther, z2);
                int decoratedMeasurement2 = gridLayoutManagerFixed.mOrientationHelper.getDecoratedMeasurement(view);
                if (decoratedMeasurement2 > i17) {
                    i17 = decoratedMeasurement2;
                }
                float decoratedMeasurementInOther2 = (gridLayoutManagerFixed.mOrientationHelper.getDecoratedMeasurementInOther(view) * 1.0f) / ((GridLayoutManager.LayoutParams) view.getLayoutParams()).mSpanSize;
                if (decoratedMeasurementInOther2 > f) {
                    f = decoratedMeasurementInOther2;
                }
                i16++;
            }
            int i18 = z2 ? 1 : 0;
            while (i18 < i14) {
                View view2 = gridLayoutManagerFixed.mSet[i18];
                if (gridLayoutManagerFixed.mOrientationHelper.getDecoratedMeasurement(view2) != i17) {
                    GridLayoutManager.LayoutParams layoutParams2 = (GridLayoutManager.LayoutParams) view2.getLayoutParams();
                    Rect rect = layoutParams2.mDecorInsets;
                    z = false;
                    gridLayoutManagerFixed.measureChildWithDecorationsAndMargin(view2, RecyclerView.LayoutManager.getChildMeasureSpec(gridLayoutManagerFixed.mCachedBorders[layoutParams2.mSpanSize], TLObject.FLAG_30, rect.left + rect.right + ((ViewGroup.MarginLayoutParams) layoutParams2).leftMargin + ((ViewGroup.MarginLayoutParams) layoutParams2).rightMargin, ((ViewGroup.MarginLayoutParams) layoutParams2).width, false), View.MeasureSpec.makeMeasureSpec((i17 == true ? 1 : 0) - (((rect.top + rect.bottom) + ((ViewGroup.MarginLayoutParams) layoutParams2).topMargin) + ((ViewGroup.MarginLayoutParams) layoutParams2).bottomMargin), TLObject.FLAG_30), true);
                } else {
                    z = z2;
                }
                i18++;
                z2 = z;
            }
            boolean z4 = z2;
            boolean zShouldLayoutChildFromOpositeSide = gridLayoutManagerFixed.shouldLayoutChildFromOpositeSide(gridLayoutManagerFixed.mSet[z4 ? 1 : 0]);
            if (zShouldLayoutChildFromOpositeSide) {
                i = -1;
                if (layoutState.mLayoutDirection == -1) {
                    if (layoutState.mLayoutDirection == i) {
                        int i19 = layoutState.mOffset - layoutChunkResult.mConsumed;
                        i5 = i19 - (i17 == true ? 1 : 0);
                        i6 = i19;
                        width2 = z4 ? 1 : 0;
                    } else {
                        i5 = layoutChunkResult.mConsumed + layoutState.mOffset;
                        i6 = i5 + (i17 == true ? 1 : 0);
                        width2 = gridLayoutManagerFixed.getWidth();
                    }
                    i7 = (i14 == true ? 1 : 0) - 1;
                    while (i7 >= 0) {
                        View view3 = gridLayoutManagerFixed.mSet[i7];
                        layoutParams = (GridLayoutManager.LayoutParams) view3.getLayoutParams();
                        decoratedMeasurementInOther = gridLayoutManagerFixed.mOrientationHelper.getDecoratedMeasurementInOther(view3);
                        if (layoutState.mLayoutDirection == 1) {
                            width2 -= decoratedMeasurementInOther;
                        }
                        i8 = decoratedMeasurementInOther + width2;
                        i9 = width2;
                        gridLayoutManagerFixed = this;
                        int i20 = i17;
                        gridLayoutManagerFixed.layoutDecoratedWithMargins(view3, i9 == true ? 1 : 0, i5, i8, i6);
                        if (layoutState.mLayoutDirection == -1) {
                            width2 = i8;
                        } else {
                            width2 = i9 == true ? 1 : 0;
                        }
                        if (!layoutParams.isItemRemoved() || layoutParams.isItemChanged()) {
                            layoutChunkResult.mIgnoreConsumed = true;
                        }
                        layoutChunkResult.mFocusable |= view3.hasFocusable();
                        i7--;
                        i17 = i20 == true ? 1 : 0;
                    }
                    i4 = i17;
                }
                layoutChunkResult.mConsumed += i4;
                Arrays.fill(gridLayoutManagerFixed.mSet, (Object) null);
                recycler2 = recycler;
                r12 = 1;
                r1 = r15 == true ? 1 : 0;
                z2 = false;
                i11 = -1;
            } else {
                i = -1;
            }
            if (!zShouldLayoutChildFromOpositeSide && layoutState.mLayoutDirection == 1) {
                if (layoutState.mLayoutDirection == i) {
                    int i110 = layoutState.mOffset - layoutChunkResult.mConsumed;
                    i5 = i110 - (i17 == true ? 1 : 0);
                    i6 = i110;
                    width2 = z4 ? 1 : 0;
                } else {
                    i5 = layoutChunkResult.mConsumed + layoutState.mOffset;
                    i6 = i5 + (i17 == true ? 1 : 0);
                    width2 = gridLayoutManagerFixed.getWidth();
                }
                i7 = (i14 == true ? 1 : 0) - 1;
                while (i7 >= 0) {
                    View view4 = gridLayoutManagerFixed.mSet[i7];
                    layoutParams = (GridLayoutManager.LayoutParams) view4.getLayoutParams();
                    decoratedMeasurementInOther = gridLayoutManagerFixed.mOrientationHelper.getDecoratedMeasurementInOther(view4);
                    if (layoutState.mLayoutDirection == 1) {
                        width2 -= decoratedMeasurementInOther;
                    }
                    i8 = decoratedMeasurementInOther + width2;
                    i9 = width2;
                    gridLayoutManagerFixed = this;
                    int i21 = i17;
                    gridLayoutManagerFixed.layoutDecoratedWithMargins(view4, i9 == true ? 1 : 0, i5, i8, i6);
                    if (layoutState.mLayoutDirection == -1) {
                        width2 = i8;
                    } else {
                        width2 = i9 == true ? 1 : 0;
                    }
                    if (!layoutParams.isItemRemoved()) {
                        layoutChunkResult.mIgnoreConsumed = true;
                    } else {
                        layoutChunkResult.mIgnoreConsumed = true;
                    }
                    layoutChunkResult.mFocusable |= view4.hasFocusable();
                    i7--;
                    i17 = i21 == true ? 1 : 0;
                }
                i4 = i17;
            } else {
                int i22 = i17 == true ? 1 : 0;
                if (layoutState.mLayoutDirection == -1) {
                    i3 = layoutState.mOffset - layoutChunkResult.mConsumed;
                    i2 = i3 - (i22 == true ? 1 : 0);
                    width = gridLayoutManagerFixed.getWidth();
                } else {
                    i2 = layoutChunkResult.mConsumed + layoutState.mOffset;
                    i3 = i2 + (i22 == true ? 1 : 0);
                    width = 0;
                }
                int i23 = i3;
                int i24 = 0;
                while (i24 < i14) {
                    View view5 = gridLayoutManagerFixed.mSet[i24];
                    GridLayoutManager.LayoutParams layoutParams3 = (GridLayoutManager.LayoutParams) view5.getLayoutParams();
                    int decoratedMeasurementInOther3 = gridLayoutManagerFixed.mOrientationHelper.getDecoratedMeasurementInOther(view5);
                    int i25 = i22;
                    if (layoutState.mLayoutDirection == -1) {
                        width -= decoratedMeasurementInOther3;
                    }
                    int i26 = decoratedMeasurementInOther3 + width;
                    int i27 = i14;
                    int i28 = width;
                    width = i26;
                    gridLayoutManagerFixed = this;
                    gridLayoutManagerFixed.layoutDecoratedWithMargins(view5, i28, i2, width, i23);
                    if (layoutState.mLayoutDirection != 1) {
                        width = i28;
                    }
                    if (layoutParams3.isItemRemoved() || layoutParams3.isItemChanged()) {
                        layoutChunkResult.mIgnoreConsumed = true;
                    }
                    layoutChunkResult.mFocusable |= view5.hasFocusable();
                    i24++;
                    i14 = i27 == true ? 1 : 0;
                    i22 = i25 == true ? 1 : 0;
                }
                i4 = i22;
            }
            layoutChunkResult.mConsumed += i4;
            Arrays.fill(gridLayoutManagerFixed.mSet, (Object) null);
            recycler2 = recycler;
            r12 = 1;
            r1 = r15 == true ? 1 : 0;
            z2 = false;
            i11 = -1;
        }
    }
}
