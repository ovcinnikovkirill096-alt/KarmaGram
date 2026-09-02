package org.telegram.ui.Charts;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import androidx.core.graphics.ColorUtils;
import org.telegram.ui.Charts.data.ChartData;
import org.telegram.ui.Charts.view_data.BarViewData;
import org.telegram.ui.Charts.view_data.ChartHorizontalLinesData;
import org.telegram.ui.Charts.view_data.TransitionParams;

public class BarChartView extends BaseChartView {
    @Override // org.telegram.ui.Charts.BaseChartView
    protected void drawSelection(Canvas canvas) {
    }

    @Override // org.telegram.ui.Charts.BaseChartView
    protected float getMinDistance() {
        return 0.1f;
    }

    public BarChartView(Context context) {
        super(context);
        this.superDraw = true;
        this.useAlphaSignature = true;
    }

    /* JADX WARN: Code duplicated, block: B:21:0x0091  */
    /* JADX WARN: Code duplicated, block: B:26:0x00ae  */
    /* JADX WARN: Code duplicated, block: B:28:0x00b5  */
    /* JADX WARN: Code duplicated, block: B:29:0x00b7  */
    /* JADX WARN: Code duplicated, block: B:32:0x00d1  */
    /* JADX WARN: Code duplicated, block: B:34:0x0101  */
    /* JADX WARN: Code duplicated, block: B:37:0x010b  */
    /* JADX WARN: Code duplicated, block: B:41:0x0131  */
    /* JADX WARN: Code duplicated, block: B:45:0x0139  */
    /* JADX WARN: Code duplicated, block: B:48:0x0140  */
    /* JADX WARN: Code duplicated, block: B:51:0x0155  */
    /* JADX WARN: Code duplicated, block: B:52:0x0164  */
    /* JADX WARN: Code duplicated, block: B:55:0x0175  */
    /* JADX WARN: Code duplicated, block: B:62:0x0195 A[SYNTHETIC] */
    @Override // org.telegram.ui.Charts.BaseChartView
    protected void drawChart(Canvas canvas) {
        float f;
        float f2;
        int i;
        BarViewData barViewData;
        float[] fArr;
        float f3;
        long[] jArr;
        float f4;
        float f5;
        int i2;
        int i3;
        int i4;
        float f6;
        float f7;
        float f8;
        int i5;
        int i6;
        Paint paint;
        int i7;
        int i8;
        float f9;
        float measuredHeight;
        Canvas canvas2 = canvas;
        ChartData chartData = this.chartData;
        if (chartData != null) {
            float f10 = this.chartWidth;
            ChartPickerDelegate chartPickerDelegate = this.pickerDelegate;
            float f11 = chartPickerDelegate.pickerEnd;
            float f12 = chartPickerDelegate.pickerStart;
            float f13 = f10 / (f11 - f12);
            float f14 = (f12 * f13) - BaseChartView.HORIZONTAL_PADDING;
            char c = 1;
            int i9 = this.startXIndex - 1;
            int i10 = 0;
            int i11 = i9 < 0 ? 0 : i9;
            int length = this.endXIndex + 1;
            if (length > ((ChartData.Line) chartData.lines.get(0)).y.length - 1) {
                length = ((ChartData.Line) this.chartData.lines.get(0)).y.length - 1;
            }
            int i12 = length;
            canvas2.save();
            float f15 = 0.0f;
            canvas2.clipRect(this.chartStart, 0.0f, this.chartEnd, getMeasuredHeight() - this.chartBottom);
            canvas2.save();
            int i13 = this.transitionMode;
            float f16 = 2.0f;
            int i14 = 2;
            float f17 = 1.0f;
            if (i13 == 2) {
                this.postTransition = true;
                this.selectionA = 0.0f;
                TransitionParams transitionParams = this.transitionParams;
                float f18 = transitionParams.progress;
                f2 = 1.0f - f18;
                canvas2.scale((f18 * 2.0f) + 1.0f, 1.0f, transitionParams.pX, transitionParams.pY);
            } else {
                if (i13 == 1) {
                    TransitionParams transitionParams2 = this.transitionParams;
                    f2 = transitionParams2.progress;
                    canvas2.scale(f2, 1.0f, transitionParams2.pX, transitionParams2.pY);
                } else {
                    f = 1.0f;
                }
                i = 0;
                while (i < this.lines.size()) {
                    barViewData = (BarViewData) this.lines.get(i);
                    if (barViewData.enabled && barViewData.alpha == f15) {
                        i2 = i;
                        f4 = f17;
                        i8 = i10;
                        i6 = i11;
                        f8 = f16;
                    } else {
                        fArr = this.chartData.xPercentage;
                        if (fArr.length < i14) {
                            f3 = f17;
                        } else {
                            f3 = fArr[c] * f13;
                        }
                        jArr = barViewData.line.y;
                        f4 = f17;
                        f5 = barViewData.alpha;
                        i2 = i;
                        i3 = i10;
                        i4 = i11;
                        f6 = f15;
                        f7 = f6;
                        f8 = f16;
                        i5 = i3;
                        while (i4 <= i12) {
                            f9 = ((f3 / f8) + (this.chartData.xPercentage[i4] * f13)) - f14;
                            int i15 = i11;
                            measuredHeight = (getMeasuredHeight() - this.chartBottom) - (((jArr[i4] / this.currentMaxHeight) * f5) * ((getMeasuredHeight() - this.chartBottom) - BaseChartView.SIGNATURE_TEXT_HEIGHT));
                            if (i4 == this.selectedIndex || !this.legendShowing) {
                                float[] fArr2 = barViewData.linesPath;
                                fArr2[i5] = f9;
                                fArr2[i5 + 1] = measuredHeight;
                                int i16 = i5 + 3;
                                fArr2[i5 + 2] = f9;
                                i5 += 4;
                                fArr2[i16] = getMeasuredHeight() - this.chartBottom;
                            } else {
                                f7 = measuredHeight;
                                f6 = f9;
                                i3 = 1;
                            }
                            i4++;
                            i11 = i15;
                        }
                        i6 = i11;
                        if (i3 == 0 || this.postTransition) {
                            paint = barViewData.unselectedPaint;
                        } else {
                            paint = barViewData.paint;
                        }
                        paint.setStrokeWidth(f3);
                        if (i3 != 0) {
                            barViewData.unselectedPaint.setColor(ColorUtils.blendARGB(barViewData.lineColor, barViewData.blendColor, f4 - this.selectionA));
                        }
                        if (this.postTransition) {
                            f15 = 0.0f;
                            barViewData.unselectedPaint.setColor(ColorUtils.blendARGB(barViewData.lineColor, barViewData.blendColor, 0.0f));
                        } else {
                            f15 = 0.0f;
                        }
                        i7 = (int) (255.0f * f);
                        paint.setAlpha(i7);
                        i8 = 0;
                        canvas2.drawLines(barViewData.linesPath, 0, i5, paint);
                        if (i3 != 0) {
                            barViewData.paint.setStrokeWidth(f3);
                            barViewData.paint.setAlpha(i7);
                            canvas2.drawLine(f6, f7, f6, getMeasuredHeight() - this.chartBottom, barViewData.paint);
                            barViewData.paint.setAlpha(255);
                        }
                    }
                    i = i2 + 1;
                    canvas2 = canvas;
                    i10 = i8;
                    f17 = f4;
                    f16 = f8;
                    i11 = i6;
                    c = 1;
                    i14 = 2;
                }
                canvas.restore();
                canvas.restore();
            }
            f = f2;
            i = 0;
            while (i < this.lines.size()) {
                barViewData = (BarViewData) this.lines.get(i);
                if (barViewData.enabled) {
                    fArr = this.chartData.xPercentage;
                    if (fArr.length < i14) {
                        f3 = f17;
                    } else {
                        f3 = fArr[c] * f13;
                    }
                    jArr = barViewData.line.y;
                    f4 = f17;
                    f5 = barViewData.alpha;
                    i2 = i;
                    i3 = i10;
                    i4 = i11;
                    f6 = f15;
                    f7 = f6;
                    f8 = f16;
                    i5 = i3;
                    while (i4 <= i12) {
                        f9 = ((f3 / f8) + (this.chartData.xPercentage[i4] * f13)) - f14;
                        int i17 = i11;
                        measuredHeight = (getMeasuredHeight() - this.chartBottom) - (((jArr[i4] / this.currentMaxHeight) * f5) * ((getMeasuredHeight() - this.chartBottom) - BaseChartView.SIGNATURE_TEXT_HEIGHT));
                        if (i4 == this.selectedIndex) {
                            float[] fArr3 = barViewData.linesPath;
                            fArr3[i5] = f9;
                            fArr3[i5 + 1] = measuredHeight;
                            int i18 = i5 + 3;
                            fArr3[i5 + 2] = f9;
                            i5 += 4;
                            fArr3[i18] = getMeasuredHeight() - this.chartBottom;
                        } else {
                            float[] fArr4 = barViewData.linesPath;
                            fArr4[i5] = f9;
                            fArr4[i5 + 1] = measuredHeight;
                            int i19 = i5 + 3;
                            fArr4[i5 + 2] = f9;
                            i5 += 4;
                            fArr4[i19] = getMeasuredHeight() - this.chartBottom;
                        }
                        i4++;
                        i11 = i17;
                    }
                    i6 = i11;
                    if (i3 == 0) {
                        paint = barViewData.unselectedPaint;
                    } else {
                        paint = barViewData.unselectedPaint;
                    }
                    paint.setStrokeWidth(f3);
                    if (i3 != 0) {
                        barViewData.unselectedPaint.setColor(ColorUtils.blendARGB(barViewData.lineColor, barViewData.blendColor, f4 - this.selectionA));
                    }
                    if (this.postTransition) {
                        f15 = 0.0f;
                        barViewData.unselectedPaint.setColor(ColorUtils.blendARGB(barViewData.lineColor, barViewData.blendColor, 0.0f));
                    } else {
                        f15 = 0.0f;
                    }
                    i7 = (int) (255.0f * f);
                    paint.setAlpha(i7);
                    i8 = 0;
                    canvas2.drawLines(barViewData.linesPath, 0, i5, paint);
                    if (i3 != 0) {
                        barViewData.paint.setStrokeWidth(f3);
                        barViewData.paint.setAlpha(i7);
                        canvas2.drawLine(f6, f7, f6, getMeasuredHeight() - this.chartBottom, barViewData.paint);
                        barViewData.paint.setAlpha(255);
                    }
                } else {
                    fArr = this.chartData.xPercentage;
                    if (fArr.length < i14) {
                        f3 = f17;
                    } else {
                        f3 = fArr[c] * f13;
                    }
                    jArr = barViewData.line.y;
                    f4 = f17;
                    f5 = barViewData.alpha;
                    i2 = i;
                    i3 = i10;
                    i4 = i11;
                    f6 = f15;
                    f7 = f6;
                    f8 = f16;
                    i5 = i3;
                    while (i4 <= i12) {
                        f9 = ((f3 / f8) + (this.chartData.xPercentage[i4] * f13)) - f14;
                        int i110 = i11;
                        measuredHeight = (getMeasuredHeight() - this.chartBottom) - (((jArr[i4] / this.currentMaxHeight) * f5) * ((getMeasuredHeight() - this.chartBottom) - BaseChartView.SIGNATURE_TEXT_HEIGHT));
                        if (i4 == this.selectedIndex) {
                            float[] fArr5 = barViewData.linesPath;
                            fArr5[i5] = f9;
                            fArr5[i5 + 1] = measuredHeight;
                            int i111 = i5 + 3;
                            fArr5[i5 + 2] = f9;
                            i5 += 4;
                            fArr5[i111] = getMeasuredHeight() - this.chartBottom;
                        } else {
                            float[] fArr6 = barViewData.linesPath;
                            fArr6[i5] = f9;
                            fArr6[i5 + 1] = measuredHeight;
                            int i112 = i5 + 3;
                            fArr6[i5 + 2] = f9;
                            i5 += 4;
                            fArr6[i112] = getMeasuredHeight() - this.chartBottom;
                        }
                        i4++;
                        i11 = i110;
                    }
                    i6 = i11;
                    if (i3 == 0) {
                        paint = barViewData.unselectedPaint;
                    } else {
                        paint = barViewData.unselectedPaint;
                    }
                    paint.setStrokeWidth(f3);
                    if (i3 != 0) {
                        barViewData.unselectedPaint.setColor(ColorUtils.blendARGB(barViewData.lineColor, barViewData.blendColor, f4 - this.selectionA));
                    }
                    if (this.postTransition) {
                        f15 = 0.0f;
                        barViewData.unselectedPaint.setColor(ColorUtils.blendARGB(barViewData.lineColor, barViewData.blendColor, 0.0f));
                    } else {
                        f15 = 0.0f;
                    }
                    i7 = (int) (255.0f * f);
                    paint.setAlpha(i7);
                    i8 = 0;
                    canvas2.drawLines(barViewData.linesPath, 0, i5, paint);
                    if (i3 != 0) {
                        barViewData.paint.setStrokeWidth(f3);
                        barViewData.paint.setAlpha(i7);
                        canvas2.drawLine(f6, f7, f6, getMeasuredHeight() - this.chartBottom, barViewData.paint);
                        barViewData.paint.setAlpha(255);
                    }
                }
                i = i2 + 1;
                canvas2 = canvas;
                i10 = i8;
                f17 = f4;
                f16 = f8;
                i11 = i6;
                c = 1;
                i14 = 2;
            }
            canvas.restore();
            canvas.restore();
        }
    }

    @Override // org.telegram.ui.Charts.BaseChartView
    protected void drawPickerChart(Canvas canvas) {
        int i;
        int i2;
        int i3;
        int measuredHeight = getMeasuredHeight();
        int i4 = BaseChartView.PICKER_PADDING;
        int i5 = measuredHeight - i4;
        int measuredHeight2 = (getMeasuredHeight() - this.pikerHeight) - i4;
        int size = this.lines.size();
        if (this.chartData != null) {
            int i6 = 0;
            while (i6 < size) {
                BarViewData barViewData = (BarViewData) this.lines.get(i6);
                if (barViewData.enabled || barViewData.alpha != 0.0f) {
                    barViewData.bottomLinePath.reset();
                    float[] fArr = this.chartData.xPercentage;
                    int length = fArr.length;
                    float f = fArr.length < 2 ? 1.0f : fArr[1] * this.pickerWidth;
                    long[] jArr = barViewData.line.y;
                    float f2 = barViewData.alpha;
                    int i7 = 0;
                    int i8 = 0;
                    while (i7 < length) {
                        long j = jArr[i7];
                        if (j < 0) {
                            i3 = i5;
                            size = size;
                        } else {
                            ChartData chartData = this.chartData;
                            i3 = i5;
                            float f3 = chartData.xPercentage[i7] * this.pickerWidth;
                            float f4 = (1.0f - ((j / (BaseChartView.ANIMATE_PICKER_SIZES ? this.pickerMaxHeight : chartData.maxValue)) * f2)) * (i3 - measuredHeight2);
                            float[] fArr2 = barViewData.linesPath;
                            fArr2[i8] = f3;
                            fArr2[i8 + 1] = f4;
                            int i9 = i8 + 3;
                            fArr2[i8 + 2] = f3;
                            i8 += 4;
                            fArr2[i9] = getMeasuredHeight() - this.chartBottom;
                        }
                        i7++;
                        i5 = i3;
                        size = size;
                    }
                    i = i5;
                    i2 = size;
                    barViewData.paint.setStrokeWidth(f + 2.0f);
                    canvas.drawLines(barViewData.linesPath, 0, i8, barViewData.paint);
                } else {
                    i = i5;
                    i2 = size;
                }
                i6++;
                i5 = i;
                size = i2;
            }
        }
    }

    @Override // org.telegram.ui.Charts.BaseChartView
    public BarViewData createLineViewData(ChartData.Line line) {
        return new BarViewData(line, this.resourcesProvider);
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
