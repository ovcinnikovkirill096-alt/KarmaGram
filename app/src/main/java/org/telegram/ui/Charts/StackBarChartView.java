package org.telegram.ui.Charts;

import android.animation.Animator;
import android.animation.ValueAnimator;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import androidx.core.graphics.ColorUtils;
import org.telegram.messenger.SegmentTree;
import org.telegram.ui.ActionBar.Theme;
import org.telegram.ui.Charts.data.ChartData;
import org.telegram.ui.Charts.data.StackBarChartData;
import org.telegram.ui.Charts.view_data.ChartHorizontalLinesData;
import org.telegram.ui.Charts.view_data.LineViewData;
import org.telegram.ui.Charts.view_data.StackBarViewData;
import org.telegram.ui.Charts.view_data.TransitionParams;

public class StackBarChartView extends BaseChartView {
    private long[] yMaxPoints;

    @Override // org.telegram.ui.Charts.BaseChartView
    protected void drawSelection(Canvas canvas) {
    }

    @Override // org.telegram.ui.Charts.BaseChartView
    protected float getMinDistance() {
        return 0.1f;
    }

    public StackBarChartView(Context context, Theme.ResourcesProvider resourcesProvider) {
        super(context, resourcesProvider);
        this.superDraw = true;
        this.useAlphaSignature = true;
    }

    @Override // org.telegram.ui.Charts.BaseChartView
    public StackBarViewData createLineViewData(ChartData.Line line) {
        return new StackBarViewData(line, this.resourcesProvider);
    }

    @Override // org.telegram.ui.Charts.BaseChartView
    protected void drawChart(Canvas canvas) {
        float f;
        float f2;
        float f3;
        float f4;
        Canvas canvas2 = canvas;
        ChartData chartData = this.chartData;
        if (chartData == null) {
            return;
        }
        float f5 = this.chartWidth;
        ChartPickerDelegate chartPickerDelegate = this.pickerDelegate;
        float f6 = chartPickerDelegate.pickerEnd;
        float f7 = chartPickerDelegate.pickerStart;
        float f8 = f5 / (f6 - f7);
        float f9 = BaseChartView.HORIZONTAL_PADDING;
        float f10 = (f7 * f8) - f9;
        if (((StackBarChartData) chartData).xPercentage.length < 2) {
            f = 1.0f;
            f2 = 1.0f;
        } else {
            float f11 = ((StackBarChartData) chartData).xPercentage[1] * f8;
            f = ((StackBarChartData) chartData).xPercentage[1] * (f8 - f11);
            f2 = f11;
        }
        int i = ((int) (f9 / f2)) + 1;
        int i2 = 0;
        int iMax = Math.max(0, (this.startXIndex - i) - 2);
        int iMin = Math.min(((StackBarChartData) this.chartData).xPercentage.length - 1, this.endXIndex + i + 2);
        for (int i3 = 0; i3 < this.lines.size(); i3++) {
            ((LineViewData) this.lines.get(i3)).linesPathBottomSize = 0;
        }
        canvas2.save();
        int i4 = this.transitionMode;
        float f12 = 0.0f;
        if (i4 == 2) {
            this.postTransition = true;
            this.selectionA = 0.0f;
            TransitionParams transitionParams = this.transitionParams;
            float f13 = transitionParams.progress;
            f4 = 1.0f - f13;
            f3 = 2.0f;
            canvas2.scale((f13 * 2.0f) + 1.0f, 1.0f, transitionParams.pX, transitionParams.pY);
        } else {
            f3 = 2.0f;
            if (i4 == 1) {
                TransitionParams transitionParams2 = this.transitionParams;
                f4 = transitionParams2.progress;
                canvas2.scale(f4, 1.0f, transitionParams2.pX, transitionParams2.pY);
            } else {
                f4 = i4 == 3 ? this.transitionParams.progress : 1.0f;
            }
        }
        boolean z = this.selectedIndex >= 0 && this.legendShowing;
        while (iMax <= iMin) {
            if (this.selectedIndex != iMax || !z) {
                int i5 = i2;
                float f14 = f12;
                while (i5 < this.lines.size()) {
                    LineViewData lineViewData = (LineViewData) this.lines.get(i5);
                    float f15 = f12;
                    if (lineViewData.enabled || lineViewData.alpha != f15) {
                        long[] jArr = lineViewData.line.y;
                        float f16 = ((f2 / f3) + (((StackBarChartData) this.chartData).xPercentage[iMax] * (f8 - f2))) - f10;
                        float measuredHeight = (jArr[iMax] / this.currentMaxHeight) * ((getMeasuredHeight() - this.chartBottom) - BaseChartView.SIGNATURE_TEXT_HEIGHT) * lineViewData.alpha;
                        float measuredHeight2 = (getMeasuredHeight() - this.chartBottom) - measuredHeight;
                        float[] fArr = lineViewData.linesPath;
                        int i6 = lineViewData.linesPathBottomSize;
                        int i7 = i6 + 1;
                        lineViewData.linesPathBottomSize = i7;
                        fArr[i6] = f16;
                        int i8 = i6 + 2;
                        lineViewData.linesPathBottomSize = i8;
                        fArr[i7] = measuredHeight2 - f14;
                        int i9 = i6 + 3;
                        lineViewData.linesPathBottomSize = i9;
                        fArr[i8] = f16;
                        lineViewData.linesPathBottomSize = i6 + 4;
                        fArr[i9] = (getMeasuredHeight() - this.chartBottom) - f14;
                        f14 += measuredHeight;
                    }
                    i5++;
                    f12 = f15;
                    f8 = f8;
                    iMin = iMin;
                }
            }
            iMax++;
            f12 = f12;
            f8 = f8;
            iMin = iMin;
            i2 = 0;
        }
        float f17 = f8;
        float f18 = f12;
        for (int i10 = 0; i10 < this.lines.size(); i10++) {
            StackBarViewData stackBarViewData = (StackBarViewData) this.lines.get(i10);
            Paint paint = (z || this.postTransition) ? stackBarViewData.unselectedPaint : stackBarViewData.paint;
            if (z) {
                stackBarViewData.unselectedPaint.setColor(ColorUtils.blendARGB(stackBarViewData.lineColor, stackBarViewData.blendColor, this.selectionA));
            }
            if (this.postTransition) {
                stackBarViewData.unselectedPaint.setColor(ColorUtils.blendARGB(stackBarViewData.lineColor, stackBarViewData.blendColor, 1.0f));
            }
            paint.setAlpha((int) (255.0f * f4));
            paint.setStrokeWidth(f);
            canvas2.drawLines(stackBarViewData.linesPath, 0, stackBarViewData.linesPathBottomSize, paint);
        }
        if (z) {
            int i11 = 0;
            float f19 = f18;
            while (i11 < this.lines.size()) {
                LineViewData lineViewData2 = (LineViewData) this.lines.get(i11);
                if (lineViewData2.enabled || lineViewData2.alpha != f18) {
                    long[] jArr2 = lineViewData2.line.y;
                    float[] fArr2 = ((StackBarChartData) this.chartData).xPercentage;
                    int i12 = this.selectedIndex;
                    float f20 = ((f2 / f3) + (fArr2[i12] * (f17 - f2))) - f10;
                    float measuredHeight3 = (jArr2[i12] / this.currentMaxHeight) * ((getMeasuredHeight() - this.chartBottom) - BaseChartView.SIGNATURE_TEXT_HEIGHT) * lineViewData2.alpha;
                    float measuredHeight4 = (getMeasuredHeight() - this.chartBottom) - measuredHeight3;
                    lineViewData2.paint.setStrokeWidth(f);
                    lineViewData2.paint.setAlpha((int) (f4 * 255.0f));
                    canvas2.drawLine(f20, measuredHeight4 - f19, f20, (getMeasuredHeight() - this.chartBottom) - f19, lineViewData2.paint);
                    f19 += measuredHeight3;
                }
                i11++;
                canvas2 = canvas;
            }
        }
        canvas.restore();
    }

    @Override // org.telegram.ui.Charts.BaseChartView
    protected void selectXOnChart(int i, int i2) {
        ChartData chartData = this.chartData;
        if (chartData == null) {
            return;
        }
        int i3 = this.selectedIndex;
        float f = this.chartFullWidth;
        float f2 = (this.pickerDelegate.pickerStart * f) - BaseChartView.HORIZONTAL_PADDING;
        float f3 = (i + f2) / (f - (((StackBarChartData) chartData).xPercentage.length < 2 ? 1.0f : ((StackBarChartData) chartData).xPercentage[1] * f));
        this.selectedCoordinate = f3;
        if (f3 < 0.0f) {
            this.selectedIndex = 0;
            this.selectedCoordinate = 0.0f;
        } else if (f3 > 1.0f) {
            this.selectedIndex = ((StackBarChartData) chartData).x.length - 1;
            this.selectedCoordinate = 1.0f;
        } else {
            int iFindIndex = ((StackBarChartData) chartData).findIndex(this.startXIndex, this.endXIndex, f3);
            this.selectedIndex = iFindIndex;
            int i4 = this.endXIndex;
            if (iFindIndex > i4) {
                this.selectedIndex = i4;
            }
            int i5 = this.selectedIndex;
            int i6 = this.startXIndex;
            if (i5 < i6) {
                this.selectedIndex = i6;
            }
        }
        if (i3 != this.selectedIndex) {
            this.legendShowing = true;
            animateLegend(true);
            moveLegend(f2);
            BaseChartView.DateSelectionListener dateSelectionListener = this.dateSelectionListener;
            if (dateSelectionListener != null) {
                dateSelectionListener.onDateSelected(getSelectedDate());
            }
            invalidate();
            runSmoothHaptic();
        }
    }

    @Override // org.telegram.ui.Charts.BaseChartView
    protected void drawPickerChart(Canvas canvas) {
        float f;
        float f2;
        ChartData chartData = this.chartData;
        if (chartData != null) {
            int length = ((StackBarChartData) chartData).xPercentage.length;
            int size = this.lines.size();
            int i = 0;
            for (int i2 = 0; i2 < this.lines.size(); i2++) {
                ((LineViewData) this.lines.get(i2)).linesPathBottomSize = 0;
            }
            boolean z = true;
            int iMax = Math.max(1, Math.round(length / 200.0f));
            long[] jArr = this.yMaxPoints;
            if (jArr == null || jArr.length < size) {
                this.yMaxPoints = new long[size];
            }
            int i3 = 0;
            while (i3 < length) {
                float f3 = ((StackBarChartData) this.chartData).xPercentage[i3] * this.pickerWidth;
                int i4 = i;
                while (true) {
                    f2 = 0.0f;
                    if (i4 >= size) {
                        break;
                    }
                    LineViewData lineViewData = (LineViewData) this.lines.get(i4);
                    if (lineViewData.enabled || lineViewData.alpha != 0.0f) {
                        long j = lineViewData.line.y[i3];
                        long[] jArr2 = this.yMaxPoints;
                        if (j > jArr2[i4]) {
                            jArr2[i4] = j;
                        }
                    }
                    i4++;
                }
                if (i3 % iMax == 0) {
                    int i5 = i;
                    float f4 = 0.0f;
                    while (i5 < size) {
                        LineViewData lineViewData2 = (LineViewData) this.lines.get(i5);
                        if (lineViewData2.enabled || lineViewData2.alpha != f2) {
                            float f5 = BaseChartView.ANIMATE_PICKER_SIZES ? this.pickerMaxHeight : ((StackBarChartData) this.chartData).maxValue;
                            long[] jArr3 = this.yMaxPoints;
                            float f6 = (jArr3[i5] / f5) * lineViewData2.alpha;
                            int i6 = this.pikerHeight;
                            float f7 = f6 * i6;
                            float[] fArr = lineViewData2.linesPath;
                            int i7 = lineViewData2.linesPathBottomSize;
                            int i8 = i7 + 1;
                            lineViewData2.linesPathBottomSize = i8;
                            fArr[i7] = f3;
                            int i9 = i7 + 2;
                            lineViewData2.linesPathBottomSize = i9;
                            fArr[i8] = (i6 - f7) - f4;
                            int i10 = i7 + 3;
                            lineViewData2.linesPathBottomSize = i10;
                            fArr[i9] = f3;
                            lineViewData2.linesPathBottomSize = i7 + 4;
                            fArr[i10] = i6 - f4;
                            f4 += f7;
                            jArr3[i5] = 0;
                        }
                        i5++;
                        z = z;
                        length = length;
                        f2 = 0.0f;
                    }
                }
                i3++;
                z = z;
                length = length;
                i = 0;
            }
            boolean z2 = z;
            ChartData chartData2 = this.chartData;
            if (((StackBarChartData) chartData2).xPercentage.length < 2) {
                f = 1.0f;
            } else {
                f = ((StackBarChartData) chartData2).xPercentage[z2 ? 1 : 0] * this.pickerWidth;
            }
            for (int i11 = 0; i11 < size; i11++) {
                LineViewData lineViewData3 = (LineViewData) this.lines.get(i11);
                lineViewData3.paint.setStrokeWidth(iMax * f);
                lineViewData3.paint.setAlpha(255);
                canvas.drawLines(lineViewData3.linesPath, 0, lineViewData3.linesPathBottomSize, lineViewData3.paint);
            }
        }
    }

    @Override // org.telegram.ui.Charts.BaseChartView
    public void onCheckChanged() {
        int length = ((ChartData.Line) ((StackBarChartData) this.chartData).lines.get(0)).y.length;
        int size = ((StackBarChartData) this.chartData).lines.size();
        ((StackBarChartData) this.chartData).ySum = new long[length];
        for (int i = 0; i < length; i++) {
            ((StackBarChartData) this.chartData).ySum[i] = 0;
            for (int i2 = 0; i2 < size; i2++) {
                if (((StackBarViewData) this.lines.get(i2)).enabled) {
                    ChartData chartData = this.chartData;
                    long[] jArr = ((StackBarChartData) chartData).ySum;
                    jArr[i] = jArr[i] + ((ChartData.Line) ((StackBarChartData) chartData).lines.get(i2)).y[i];
                }
            }
        }
        ChartData chartData2 = this.chartData;
        ((StackBarChartData) chartData2).ySumSegmentTree = new SegmentTree(((StackBarChartData) chartData2).ySum);
        super.onCheckChanged();
    }

    @Override // org.telegram.ui.Charts.BaseChartView
    public long findMaxValue(int i, int i2) {
        return ((StackBarChartData) this.chartData).findMax(i, i2);
    }

    @Override // org.telegram.ui.Charts.BaseChartView
    protected void updatePickerMinMaxHeight() {
        if (BaseChartView.ANIMATE_PICKER_SIZES) {
            int length = ((StackBarChartData) this.chartData).x.length;
            int size = this.lines.size();
            long j = 0;
            for (int i = 0; i < length; i++) {
                long j2 = 0;
                for (int i2 = 0; i2 < size; i2++) {
                    StackBarViewData stackBarViewData = (StackBarViewData) this.lines.get(i2);
                    if (stackBarViewData.enabled) {
                        j2 += stackBarViewData.line.y[i];
                    }
                }
                if (j2 > j) {
                    j = j2;
                }
            }
            if (j > 0) {
                float f = j;
                if (f != this.animatedToPickerMaxHeight) {
                    this.animatedToPickerMaxHeight = f;
                    Animator animator = this.pickerAnimator;
                    if (animator != null) {
                        animator.cancel();
                    }
                    ValueAnimator valueAnimatorCreateAnimator = createAnimator(this.pickerMaxHeight, this.animatedToPickerMaxHeight, new ValueAnimator.AnimatorUpdateListener() { // from class: org.telegram.ui.Charts.StackBarChartView.1
                        @Override // android.animation.ValueAnimator.AnimatorUpdateListener
                        public void onAnimationUpdate(ValueAnimator valueAnimator) {
                            StackBarChartView.this.pickerMaxHeight = ((Float) valueAnimator.getAnimatedValue()).floatValue();
                            StackBarChartView stackBarChartView = StackBarChartView.this;
                            stackBarChartView.invalidatePickerChart = true;
                            stackBarChartView.invalidate();
                        }
                    });
                    this.pickerAnimator = valueAnimatorCreateAnimator;
                    valueAnimatorCreateAnimator.start();
                }
            }
        }
    }

    @Override // org.telegram.ui.Charts.BaseChartView
    protected void initPickerMaxHeight() {
        super.initPickerMaxHeight();
        this.pickerMaxHeight = 0.0f;
        int length = ((StackBarChartData) this.chartData).x.length;
        int size = this.lines.size();
        for (int i = 0; i < length; i++) {
            long j = 0;
            for (int i2 = 0; i2 < size; i2++) {
                StackBarViewData stackBarViewData = (StackBarViewData) this.lines.get(i2);
                if (stackBarViewData.enabled) {
                    j += stackBarViewData.line.y[i];
                }
            }
            float f = j;
            if (f > this.pickerMaxHeight) {
                this.pickerMaxHeight = f;
            }
        }
    }

    @Override // org.telegram.ui.Charts.BaseChartView, android.view.View
    protected void onDraw(Canvas canvas) {
        tick();
        drawChart(canvas);
        drawBottomLine(canvas);
        this.tmpN = this.horizontalLines.size();
        int i = 0;
        while (true) {
            this.tmpI = i;
            int i2 = this.tmpI;
            if (i2 < this.tmpN) {
                drawHorizontalLines(canvas, (ChartHorizontalLinesData) this.horizontalLines.get(i2));
                drawSignaturesToHorizontalLines(canvas, (ChartHorizontalLinesData) this.horizontalLines.get(this.tmpI));
                i = this.tmpI + 1;
            } else {
                drawBottomSignature(canvas);
                drawPicker(canvas);
                drawSelection(canvas);
                super.onDraw(canvas);
                return;
            }
        }
    }
}
