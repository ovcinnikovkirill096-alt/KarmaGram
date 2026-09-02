package org.telegram.ui.Charts;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.Path;
import android.graphics.RectF;
import org.telegram.messenger.AndroidUtilities;
import org.telegram.ui.ActionBar.Theme;
import org.telegram.ui.Charts.data.ChartData;
import org.telegram.ui.Charts.data.StackLinearChartData;
import org.telegram.ui.Charts.view_data.ChartHorizontalLinesData;
import org.telegram.ui.Charts.view_data.LineViewData;
import org.telegram.ui.Charts.view_data.StackLinearViewData;
import org.telegram.ui.Charts.view_data.TransitionParams;

public class StackLinearChartView extends BaseChartView {
    private float[] mapPoints;
    private Matrix matrix;
    Path ovalPath;
    boolean[] skipPoints;
    float[] startFromY;

    @Override // org.telegram.ui.Charts.BaseChartView
    public long findMaxValue(int i, int i2) {
        return 100L;
    }

    @Override // org.telegram.ui.Charts.BaseChartView
    protected float getMinDistance() {
        return 0.1f;
    }

    public StackLinearChartView(Context context) {
        super(context);
        this.matrix = new Matrix();
        this.mapPoints = new float[2];
        this.ovalPath = new Path();
        this.superDraw = true;
        this.useAlphaSignature = true;
        this.drawPointOnSelection = false;
    }

    @Override // org.telegram.ui.Charts.BaseChartView
    public StackLinearViewData createLineViewData(ChartData.Line line) {
        return new StackLinearViewData(line);
    }

    /* JADX WARN: Code duplicated, block: B:113:0x03d7  */
    /* JADX WARN: Code duplicated, block: B:116:0x03e9  */
    /* JADX WARN: Code duplicated, block: B:118:0x03f7 A[ADDED_TO_REGION] */
    /* JADX WARN: Code duplicated, block: B:120:0x042c  */
    /* JADX WARN: Code duplicated, block: B:122:0x043c  */
    /* JADX WARN: Code duplicated, block: B:125:0x0444  */
    /* JADX WARN: Code duplicated, block: B:126:0x0447  */
    /* JADX WARN: Code duplicated, block: B:128:0x044b A[ADDED_TO_REGION] */
    /* JADX WARN: Code duplicated, block: B:142:0x0481  */
    /* JADX WARN: Code duplicated, block: B:144:0x0487 A[DONT_INVERT] */
    /* JADX WARN: Code duplicated, block: B:145:0x0489  */
    /* JADX WARN: Code duplicated, block: B:146:0x0492  */
    /* JADX WARN: Code duplicated, block: B:148:0x0499  */
    /* JADX WARN: Code duplicated, block: B:149:0x04a2  */
    /* JADX WARN: Code duplicated, block: B:152:0x04ad  */
    /* JADX WARN: Code duplicated, block: B:154:0x04bc A[ADDED_TO_REGION] */
    /* JADX WARN: Code duplicated, block: B:156:0x04ea  */
    /* JADX WARN: Code duplicated, block: B:159:0x04f4 A[DONT_INVERT] */
    /* JADX WARN: Code duplicated, block: B:160:0x04f6  */
    /* JADX WARN: Code duplicated, block: B:162:0x0509  */
    /* JADX WARN: Code duplicated, block: B:164:0x0515  */
    /* JADX WARN: Code duplicated, block: B:167:0x056b  */
    /* JADX WARN: Code duplicated, block: B:179:0x058f  */
    /* JADX WARN: Code duplicated, block: B:181:0x0599 A[DONT_INVERT] */
    /* JADX WARN: Code duplicated, block: B:182:0x059b  */
    /* JADX WARN: Code duplicated, block: B:184:0x05a9  */
    /* JADX WARN: Code duplicated, block: B:186:0x05ac  */
    /* JADX WARN: Code duplicated, block: B:187:0x05bd  */
    /* JADX WARN: Code duplicated, block: B:189:0x05c0  */
    /* JADX WARN: Code duplicated, block: B:190:0x05cc  */
    /* JADX WARN: Code duplicated, block: B:192:0x05d7 A[PHI: r17
  0x05d7: PHI (r17v4 float) = (r17v3 float), (r17v3 float), (r17v5 float) binds: [B:151:0x04ab, B:159:0x04f4, B:213:0x05d7] A[DONT_GENERATE, DONT_INLINE]] */
    /* JADX WARN: Code duplicated, block: B:193:0x05db  */
    /* JADX WARN: Code duplicated, block: B:199:0x0621  */
    /* JADX WARN: Code duplicated, block: B:202:0x0636 A[LOOP:5: B:201:0x0634->B:202:0x0636, LOOP_END] */
    /* JADX WARN: Code duplicated, block: B:34:0x0128  */
    /* JADX WARN: Code duplicated, block: B:35:0x012b  */
    /* JADX WARN: Code duplicated, block: B:38:0x0159  */
    /* JADX WARN: Code duplicated, block: B:41:0x016a  */
    /* JADX WARN: Code duplicated, block: B:46:0x0183  */
    /* JADX WARN: Code duplicated, block: B:48:0x0191  */
    /* JADX WARN: Code duplicated, block: B:54:0x01b2  */
    /* JADX WARN: Code duplicated, block: B:59:0x01d3  */
    /* JADX WARN: Code duplicated, block: B:61:0x01dc  */
    /* JADX WARN: Code duplicated, block: B:63:0x01e2  */
    /* JADX WARN: Code duplicated, block: B:64:0x01e8  */
    /* JADX WARN: Code duplicated, block: B:65:0x01ef  */
    /* JADX WARN: Code duplicated, block: B:68:0x01f4  */
    /* JADX WARN: Code duplicated, block: B:71:0x0210  */
    /* JADX WARN: Code duplicated, block: B:72:0x0216  */
    /* JADX WARN: Code duplicated, block: B:75:0x022a A[ADDED_TO_REGION] */
    /* JADX WARN: Code duplicated, block: B:79:0x0260  */
    /* JADX WARN: Code duplicated, block: B:80:0x0265  */
    /* JADX WARN: Code duplicated, block: B:82:0x0269  */
    /* JADX WARN: Code duplicated, block: B:85:0x0274 A[ADDED_TO_REGION] */
    @Override // org.telegram.ui.Charts.BaseChartView
    protected void drawChart(Canvas canvas) {
        float f;
        float f2;
        float f3;
        int i;
        ChartData chartData;
        float f4;
        int iMax;
        int iMin;
        boolean z;
        int i2;
        float f5;
        float f6;
        int i3;
        int size;
        float f7;
        int i4;
        int i5;
        int i6;
        float f8;
        float f9;
        int i7;
        LineViewData lineViewData;
        long[] jArr;
        float f10;
        float f11;
        ChartData chartData2;
        float f12;
        float measuredWidth;
        int i8;
        float measuredHeight;
        float measuredHeight2;
        float f13;
        float f14;
        int i9;
        float f15;
        float f16;
        float f17;
        float f18;
        float f19;
        TransitionParams transitionParams;
        float f20;
        float f21;
        float measuredWidth2;
        float measuredHeight3;
        float f22;
        double degrees;
        float f23;
        float f24;
        int iQuarterForPoint;
        int iQuarterForPoint2;
        float f25;
        float measuredHeight4;
        float f26;
        float f27;
        float f28;
        float f29;
        double degrees2;
        float[] fArr;
        LineViewData lineViewData2;
        int i10;
        long j;
        if (this.chartData != null) {
            float f30 = this.chartWidth;
            ChartPickerDelegate chartPickerDelegate = this.pickerDelegate;
            float f31 = chartPickerDelegate.pickerEnd;
            float f32 = chartPickerDelegate.pickerStart;
            float f33 = f30 / (f31 - f32);
            float f34 = (f32 * f33) - BaseChartView.HORIZONTAL_PADDING;
            float fCenterX = this.chartArea.centerX();
            float fCenterY = this.chartArea.centerY() + AndroidUtilities.dp(16.0f);
            for (int i11 = 0; i11 < this.lines.size(); i11++) {
                ((StackLinearViewData) this.lines.get(i11)).chartPath.reset();
                ((StackLinearViewData) this.lines.get(i11)).chartPathPicker.reset();
            }
            canvas.save();
            boolean[] zArr = this.skipPoints;
            if (zArr == null || zArr.length < ((StackLinearChartData) this.chartData).lines.size()) {
                this.skipPoints = new boolean[((StackLinearChartData) this.chartData).lines.size()];
                this.startFromY = new float[((StackLinearChartData) this.chartData).lines.size()];
            }
            int i12 = this.transitionMode;
            if (i12 == 2) {
                float f35 = this.transitionParams.progress / 0.6f;
                if (f35 > 1.0f) {
                    f35 = 1.0f;
                }
                this.ovalPath.reset();
                float fWidth = this.chartArea.width() > this.chartArea.height() ? this.chartArea.width() : this.chartArea.height();
                float fHeight = (this.chartArea.width() > this.chartArea.height() ? this.chartArea.height() : this.chartArea.width()) * 0.45f;
                float f36 = fHeight + (((fWidth - fHeight) / 2.0f) * (1.0f - this.transitionParams.progress));
                RectF rectF = new RectF();
                f = 1.0f;
                f2 = 0.0f;
                rectF.set(fCenterX - f36, fCenterY - f36, fCenterX + f36, fCenterY + f36);
                this.ovalPath.addRoundRect(rectF, f36, f36, Path.Direction.CW);
                canvas.clipPath(this.ovalPath);
                f3 = f35;
            } else {
                f = 1.0f;
                f2 = 0.0f;
                if (i12 == 3) {
                    i = (int) (this.transitionParams.progress * 255.0f);
                    f3 = 0.0f;
                } else {
                    f3 = 0.0f;
                }
                chartData = this.chartData;
                if (((StackLinearChartData) chartData).xPercentage.length < 2) {
                    f4 = f;
                } else {
                    f4 = ((StackLinearChartData) chartData).xPercentage[1] * f33;
                }
                int i13 = ((int) (BaseChartView.HORIZONTAL_PADDING / f4)) + 1;
                iMax = Math.max(0, (this.startXIndex - i13) - 1);
                iMin = Math.min(((StackLinearChartData) this.chartData).xPercentage.length - 1, this.endXIndex + i13 + 1);
                z = false;
                i2 = iMax;
                f5 = f2;
                f6 = f5;
                while (i2 <= iMin) {
                    f7 = f2;
                    i4 = 0;
                    i5 = 0;
                    i6 = 0;
                    while (i4 < this.lines.size()) {
                        lineViewData2 = (LineViewData) this.lines.get(i4);
                        float f37 = f33;
                        if (lineViewData2.enabled && lineViewData2.alpha == f2) {
                            i10 = i4;
                        } else {
                            i10 = i4;
                            j = lineViewData2.line.y[i2];
                            if (j > 0) {
                                f7 += j * lineViewData2.alpha;
                                i5++;
                            }
                            i6 = i10;
                        }
                        i4 = i10 + 1;
                        f33 = f37;
                        f3 = f3;
                    }
                    f8 = f33;
                    float f38 = f3;
                    f9 = f2;
                    i7 = 0;
                    while (i7 < this.lines.size()) {
                        lineViewData = (LineViewData) this.lines.get(i7);
                        if (lineViewData.enabled && lineViewData.alpha == f2) {
                            f10 = f34;
                            f21 = f2;
                            i8 = i6;
                            i9 = i5;
                        } else {
                            jArr = lineViewData.line.y;
                            f10 = f34;
                            if (i5 == 1) {
                                if (jArr[i2] == 0) {
                                    f11 = f2;
                                } else {
                                    f11 = lineViewData.alpha;
                                }
                            } else if (f7 == f2) {
                                f11 = f2;
                            } else {
                                f11 = (jArr[i2] * lineViewData.alpha) / f7;
                            }
                            chartData2 = this.chartData;
                            f12 = (((StackLinearChartData) chartData2).xPercentage[i2] * f8) - f10;
                            if (i2 == iMin) {
                                measuredWidth = getMeasuredWidth();
                            } else {
                                measuredWidth = (((StackLinearChartData) chartData2).xPercentage[i2 + 1] * f8) - f10;
                            }
                            i8 = i6;
                            if (f11 == f2 && i7 == i8) {
                                z = true;
                            }
                            float measuredHeight5 = ((getMeasuredHeight() - this.chartBottom) - BaseChartView.SIGNATURE_TEXT_HEIGHT) * f11;
                            measuredHeight = ((getMeasuredHeight() - this.chartBottom) - measuredHeight5) - f9;
                            this.startFromY[i7] = measuredHeight;
                            measuredHeight2 = getMeasuredHeight() - this.chartBottom;
                            if (i2 == iMin) {
                                f13 = measuredHeight2;
                                f5 = f12;
                            } else {
                                f13 = measuredHeight2;
                                if (i2 == iMax) {
                                    f6 = f12;
                                }
                            }
                            float f39 = measuredWidth;
                            if (this.transitionMode == 2 || i7 == i8) {
                                f14 = f9;
                                i9 = i5;
                                f15 = measuredHeight;
                                f16 = f2;
                                f17 = f12;
                                f18 = f17;
                                f19 = f13;
                            } else {
                                if (f12 < fCenterX) {
                                    TransitionParams transitionParams2 = this.transitionParams;
                                    f29 = transitionParams2.startX[i7];
                                    f28 = transitionParams2.startY[i7];
                                } else {
                                    TransitionParams transitionParams3 = this.transitionParams;
                                    float f40 = transitionParams3.endX[i7];
                                    f28 = transitionParams3.endY[i7];
                                    f29 = f40;
                                }
                                float f41 = fCenterX - f29;
                                float f42 = fCenterY - f28;
                                float f43 = (((f12 - f29) * f42) / f41) + f28;
                                float f44 = f - f38;
                                float f45 = measuredHeight * f44;
                                float f46 = f43 * f38;
                                float f47 = f45 + f46;
                                float f48 = (f13 * f44) + f46;
                                float f49 = f42 / f41;
                                if (f49 > f2) {
                                    degrees2 = Math.toDegrees(-Math.atan(f49));
                                } else {
                                    degrees2 = Math.toDegrees(Math.atan(Math.abs(f49)));
                                }
                                float f50 = ((float) degrees2) - 90.0f;
                                if (f12 >= fCenterX) {
                                    float[] fArr2 = this.mapPoints;
                                    fArr2[0] = f12;
                                    fArr2[1] = f47;
                                    this.matrix.reset();
                                    f16 = f50;
                                    this.matrix.postRotate(this.transitionParams.progress * f16, fCenterX, fCenterY);
                                    this.matrix.mapPoints(this.mapPoints);
                                    float[] fArr3 = this.mapPoints;
                                    float f51 = fArr3[0];
                                    float f52 = fArr3[1];
                                    if (f51 < fCenterX) {
                                        f51 = fCenterX;
                                    }
                                    fArr3[0] = f12;
                                    fArr3[1] = f48;
                                    this.matrix.reset();
                                    this.matrix.postRotate(this.transitionParams.progress * f16, fCenterX, fCenterY);
                                    this.matrix.mapPoints(this.mapPoints);
                                    i9 = i5;
                                    f15 = f52;
                                    f18 = f51;
                                    f14 = f9;
                                    f19 = this.mapPoints[1];
                                    f17 = f12 < fCenterX ? fCenterX : f12;
                                } else {
                                    f16 = f50;
                                    if (f39 >= fCenterX) {
                                        f17 = (f12 * f44) + (fCenterX * f38);
                                        float f53 = (f47 * f44) + (fCenterY * f38);
                                        f14 = f9;
                                        i9 = i5;
                                        f19 = f53;
                                        f15 = f19;
                                        f18 = f17;
                                    } else {
                                        float[] fArr4 = this.mapPoints;
                                        fArr4[0] = f12;
                                        fArr4[1] = f47;
                                        this.matrix.reset();
                                        Matrix matrix = this.matrix;
                                        TransitionParams transitionParams4 = this.transitionParams;
                                        f14 = f9;
                                        float f54 = transitionParams4.progress;
                                        matrix.postRotate((f54 * f16) + (f54 * transitionParams4.angle[i7]), fCenterX, fCenterY);
                                        this.matrix.mapPoints(this.mapPoints);
                                        float[] fArr5 = this.mapPoints;
                                        float f55 = fArr5[0];
                                        float f56 = fArr5[1];
                                        if (f39 >= fCenterX) {
                                            fArr = fArr5;
                                            float f57 = this.transitionParams.progress;
                                            fArr[0] = (f12 * (f - f57)) + (f57 * fCenterX);
                                        } else {
                                            fArr = fArr5;
                                            fArr[0] = f12;
                                        }
                                        fArr[1] = f48;
                                        this.matrix.reset();
                                        Matrix matrix2 = this.matrix;
                                        TransitionParams transitionParams5 = this.transitionParams;
                                        float f58 = transitionParams5.progress;
                                        matrix2.postRotate((f58 * f16) + (f58 * transitionParams5.angle[i7]), fCenterX, fCenterY);
                                        this.matrix.mapPoints(this.mapPoints);
                                        float[] fArr6 = this.mapPoints;
                                        float f59 = fArr6[0];
                                        f19 = fArr6[1];
                                        f17 = f59;
                                        f18 = f55;
                                        i9 = i5;
                                        f15 = f56;
                                    }
                                }
                            }
                            if (i2 == iMax) {
                                measuredHeight4 = getMeasuredHeight();
                                if (this.transitionMode == 2 || i7 == i8) {
                                    f26 = f2;
                                    f27 = measuredHeight4;
                                } else {
                                    float[] fArr7 = this.mapPoints;
                                    fArr7[0] = f2 - fCenterX;
                                    fArr7[1] = measuredHeight4;
                                    this.matrix.reset();
                                    Matrix matrix3 = this.matrix;
                                    TransitionParams transitionParams6 = this.transitionParams;
                                    float f60 = transitionParams6.progress;
                                    matrix3.postRotate((f16 * f60) + (f60 * transitionParams6.angle[i7]), fCenterX, fCenterY);
                                    this.matrix.mapPoints(this.mapPoints);
                                    float[] fArr8 = this.mapPoints;
                                    f26 = fArr8[0];
                                    f27 = fArr8[1];
                                }
                                lineViewData.chartPath.moveTo(f26, f27);
                                this.skipPoints[i7] = false;
                            } else {
                                f6 = f6;
                            }
                            transitionParams = this.transitionParams;
                            if (transitionParams == null) {
                                f20 = f2;
                            } else {
                                f20 = transitionParams.progress;
                            }
                            if (f11 != f2 && i2 > 0 && jArr[i2 - 1] == 0 && i2 < iMin && jArr[i2 + 1] == 0 && this.transitionMode != 2) {
                                if (!this.skipPoints[i7]) {
                                    if (i7 == i8) {
                                        lineViewData.chartPath.lineTo(f17, f19 * (f - f20));
                                    } else {
                                        lineViewData.chartPath.lineTo(f17, f19);
                                    }
                                }
                                this.skipPoints[i7] = true;
                            } else {
                                if (this.skipPoints[i7]) {
                                    if (i7 == i8) {
                                        lineViewData.chartPath.lineTo(f17, f19 * (f - f20));
                                    } else {
                                        lineViewData.chartPath.lineTo(f17, f19);
                                    }
                                }
                                if (i7 == i8) {
                                    lineViewData.chartPath.lineTo(f18, (f - f20) * f15);
                                } else {
                                    lineViewData.chartPath.lineTo(f18, f15);
                                }
                                this.skipPoints[i7] = false;
                            }
                            if (i2 == iMin) {
                                measuredWidth2 = getMeasuredWidth();
                                measuredHeight3 = getMeasuredHeight();
                                if (this.transitionMode != 2 && i7 != i8) {
                                    float[] fArr9 = this.mapPoints;
                                    fArr9[0] = measuredWidth2 + fCenterX;
                                    fArr9[1] = measuredHeight3;
                                    this.matrix.reset();
                                    Matrix matrix4 = this.matrix;
                                    TransitionParams transitionParams7 = this.transitionParams;
                                    matrix4.postRotate(transitionParams7.progress * transitionParams7.angle[i7], fCenterX, fCenterY);
                                    this.matrix.mapPoints(this.mapPoints);
                                    float[] fArr10 = this.mapPoints;
                                    float f61 = fArr10[0];
                                    float f62 = fArr10[1];
                                } else {
                                    lineViewData.chartPath.lineTo(measuredWidth2, measuredHeight3);
                                }
                                if (this.transitionMode == 2) {
                                    if (i7 != i8) {
                                        TransitionParams transitionParams8 = this.transitionParams;
                                        f22 = (fCenterY - transitionParams8.startY[i7]) / (fCenterX - transitionParams8.startX[i7]);
                                        if (f22 > f2) {
                                            degrees = Math.toDegrees(-Math.atan(f22));
                                        } else {
                                            degrees = Math.toDegrees(Math.atan(Math.abs(f22)));
                                        }
                                        TransitionParams transitionParams9 = this.transitionParams;
                                        float f63 = transitionParams9.startX[i7];
                                        float f64 = transitionParams9.startY[i7];
                                        float[] fArr11 = this.mapPoints;
                                        fArr11[0] = f63;
                                        fArr11[1] = f64;
                                        this.matrix.reset();
                                        Matrix matrix5 = this.matrix;
                                        TransitionParams transitionParams10 = this.transitionParams;
                                        float f65 = transitionParams10.progress;
                                        matrix5.postRotate(((((float) degrees) - 90.0f) * f65) + (f65 * transitionParams10.angle[i7]), fCenterX, fCenterY);
                                        this.matrix.mapPoints(this.mapPoints);
                                        float[] fArr12 = this.mapPoints;
                                        f23 = fArr12[0];
                                        f24 = fArr12[1];
                                        if (Math.abs(f18 - f23) < 0.001d || ((f24 >= fCenterY || f15 >= fCenterY) && (f24 <= fCenterY || f15 <= fCenterY))) {
                                            iQuarterForPoint = quarterForPoint(f18, f15);
                                            iQuarterForPoint2 = quarterForPoint(f23, f24);
                                        } else if (this.transitionParams.angle[i7] == -180.0f) {
                                            iQuarterForPoint2 = 0;
                                            iQuarterForPoint = 0;
                                        } else {
                                            iQuarterForPoint = 0;
                                            iQuarterForPoint2 = 3;
                                        }
                                        while (iQuarterForPoint <= iQuarterForPoint2) {
                                            if (iQuarterForPoint == 0) {
                                                f25 = f2;
                                                lineViewData.chartPath.lineTo(getMeasuredWidth(), f25);
                                            } else {
                                                if (iQuarterForPoint == 1) {
                                                    lineViewData.chartPath.lineTo(getMeasuredWidth(), getMeasuredHeight());
                                                    f25 = 0.0f;
                                                } else if (iQuarterForPoint == 2) {
                                                    f25 = 0.0f;
                                                    lineViewData.chartPath.lineTo(0.0f, getMeasuredHeight());
                                                } else {
                                                    f25 = 0.0f;
                                                    lineViewData.chartPath.lineTo(0.0f, 0.0f);
                                                }
                                                iQuarterForPoint++;
                                                f2 = f25;
                                            }
                                            iQuarterForPoint++;
                                            f2 = f25;
                                        }
                                    }
                                    f21 = f2;
                                } else {
                                    f21 = f2;
                                }
                            } else {
                                f21 = f2;
                            }
                            f9 = f14 + measuredHeight5;
                            f5 = f5;
                            f6 = f6;
                        }
                        i7++;
                        f2 = f21;
                        i5 = i9;
                        i = i;
                        iMax = iMax;
                        i6 = i8;
                        f34 = f10;
                    }
                    i2++;
                    f33 = f8;
                    f3 = f38;
                    iMax = iMax;
                }
                i3 = i;
                canvas.save();
                canvas.clipRect(f6, BaseChartView.SIGNATURE_TEXT_HEIGHT, f5, getMeasuredHeight() - this.chartBottom);
                if (z) {
                    canvas.drawColor(Theme.getColor(Theme.key_statisticChartLineEmpty));
                }
                for (size = this.lines.size() - 1; size >= 0; size--) {
                    LineViewData lineViewData3 = (LineViewData) this.lines.get(size);
                    lineViewData3.paint.setAlpha(i3);
                    canvas.drawPath(lineViewData3.chartPath, lineViewData3.paint);
                    lineViewData3.paint.setAlpha(255);
                }
                canvas.restore();
                canvas.restore();
            }
            i = 255;
            chartData = this.chartData;
            if (((StackLinearChartData) chartData).xPercentage.length < 2) {
                f4 = f;
            } else {
                f4 = ((StackLinearChartData) chartData).xPercentage[1] * f33;
            }
            int i14 = ((int) (BaseChartView.HORIZONTAL_PADDING / f4)) + 1;
            iMax = Math.max(0, (this.startXIndex - i14) - 1);
            iMin = Math.min(((StackLinearChartData) this.chartData).xPercentage.length - 1, this.endXIndex + i14 + 1);
            z = false;
            i2 = iMax;
            f5 = f2;
            f6 = f5;
            while (i2 <= iMin) {
                f7 = f2;
                i4 = 0;
                i5 = 0;
                i6 = 0;
                while (i4 < this.lines.size()) {
                    lineViewData2 = (LineViewData) this.lines.get(i4);
                    float f310 = f33;
                    if (lineViewData2.enabled) {
                        i10 = i4;
                        j = lineViewData2.line.y[i2];
                        if (j > 0) {
                            f7 += j * lineViewData2.alpha;
                            i5++;
                        }
                        i6 = i10;
                    } else {
                        i10 = i4;
                        j = lineViewData2.line.y[i2];
                        if (j > 0) {
                            f7 += j * lineViewData2.alpha;
                            i5++;
                        }
                        i6 = i10;
                    }
                    i4 = i10 + 1;
                    f33 = f310;
                    f3 = f3;
                }
                f8 = f33;
                float f311 = f3;
                f9 = f2;
                i7 = 0;
                while (i7 < this.lines.size()) {
                    lineViewData = (LineViewData) this.lines.get(i7);
                    if (lineViewData.enabled) {
                        jArr = lineViewData.line.y;
                        f10 = f34;
                        if (i5 == 1) {
                            if (jArr[i2] == 0) {
                                f11 = f2;
                            } else {
                                f11 = lineViewData.alpha;
                            }
                        } else if (f7 == f2) {
                            f11 = f2;
                        } else {
                            f11 = (jArr[i2] * lineViewData.alpha) / f7;
                        }
                        chartData2 = this.chartData;
                        f12 = (((StackLinearChartData) chartData2).xPercentage[i2] * f8) - f10;
                        if (i2 == iMin) {
                            measuredWidth = getMeasuredWidth();
                        } else {
                            measuredWidth = (((StackLinearChartData) chartData2).xPercentage[i2 + 1] * f8) - f10;
                        }
                        i8 = i6;
                        if (f11 == f2) {
                            z = true;
                        }
                        float measuredHeight6 = ((getMeasuredHeight() - this.chartBottom) - BaseChartView.SIGNATURE_TEXT_HEIGHT) * f11;
                        measuredHeight = ((getMeasuredHeight() - this.chartBottom) - measuredHeight6) - f9;
                        this.startFromY[i7] = measuredHeight;
                        measuredHeight2 = getMeasuredHeight() - this.chartBottom;
                        if (i2 == iMin) {
                            f13 = measuredHeight2;
                            f5 = f12;
                        } else {
                            f13 = measuredHeight2;
                            if (i2 == iMax) {
                                f6 = f12;
                            }
                        }
                        float f312 = measuredWidth;
                        if (this.transitionMode == 2) {
                            f14 = f9;
                            i9 = i5;
                            f15 = measuredHeight;
                            f16 = f2;
                            f17 = f12;
                            f18 = f17;
                            f19 = f13;
                        } else {
                            f14 = f9;
                            i9 = i5;
                            f15 = measuredHeight;
                            f16 = f2;
                            f17 = f12;
                            f18 = f17;
                            f19 = f13;
                        }
                        if (i2 == iMax) {
                            measuredHeight4 = getMeasuredHeight();
                            if (this.transitionMode == 2) {
                                f26 = f2;
                                f27 = measuredHeight4;
                            } else {
                                f26 = f2;
                                f27 = measuredHeight4;
                            }
                            lineViewData.chartPath.moveTo(f26, f27);
                            this.skipPoints[i7] = false;
                        } else {
                            f6 = f6;
                        }
                        transitionParams = this.transitionParams;
                        if (transitionParams == null) {
                            f20 = f2;
                        } else {
                            f20 = transitionParams.progress;
                        }
                        if (f11 != f2) {
                            if (this.skipPoints[i7]) {
                                if (i7 == i8) {
                                    lineViewData.chartPath.lineTo(f17, f19 * (f - f20));
                                } else {
                                    lineViewData.chartPath.lineTo(f17, f19);
                                }
                            }
                            if (i7 == i8) {
                                lineViewData.chartPath.lineTo(f18, (f - f20) * f15);
                            } else {
                                lineViewData.chartPath.lineTo(f18, f15);
                            }
                            this.skipPoints[i7] = false;
                        } else {
                            if (this.skipPoints[i7]) {
                                if (i7 == i8) {
                                    lineViewData.chartPath.lineTo(f17, f19 * (f - f20));
                                } else {
                                    lineViewData.chartPath.lineTo(f17, f19);
                                }
                            }
                            if (i7 == i8) {
                                lineViewData.chartPath.lineTo(f18, (f - f20) * f15);
                            } else {
                                lineViewData.chartPath.lineTo(f18, f15);
                            }
                            this.skipPoints[i7] = false;
                        }
                        if (i2 == iMin) {
                            measuredWidth2 = getMeasuredWidth();
                            measuredHeight3 = getMeasuredHeight();
                            if (this.transitionMode != 2) {
                                lineViewData.chartPath.lineTo(measuredWidth2, measuredHeight3);
                            } else {
                                lineViewData.chartPath.lineTo(measuredWidth2, measuredHeight3);
                            }
                            if (this.transitionMode == 2) {
                                if (i7 != i8) {
                                    TransitionParams transitionParams11 = this.transitionParams;
                                    f22 = (fCenterY - transitionParams11.startY[i7]) / (fCenterX - transitionParams11.startX[i7]);
                                    if (f22 > f2) {
                                        degrees = Math.toDegrees(-Math.atan(f22));
                                    } else {
                                        degrees = Math.toDegrees(Math.atan(Math.abs(f22)));
                                    }
                                    TransitionParams transitionParams12 = this.transitionParams;
                                    float f66 = transitionParams12.startX[i7];
                                    float f67 = transitionParams12.startY[i7];
                                    float[] fArr13 = this.mapPoints;
                                    fArr13[0] = f66;
                                    fArr13[1] = f67;
                                    this.matrix.reset();
                                    Matrix matrix6 = this.matrix;
                                    TransitionParams transitionParams13 = this.transitionParams;
                                    float f68 = transitionParams13.progress;
                                    matrix6.postRotate(((((float) degrees) - 90.0f) * f68) + (f68 * transitionParams13.angle[i7]), fCenterX, fCenterY);
                                    this.matrix.mapPoints(this.mapPoints);
                                    float[] fArr14 = this.mapPoints;
                                    f23 = fArr14[0];
                                    f24 = fArr14[1];
                                    if (Math.abs(f18 - f23) < 0.001d) {
                                        iQuarterForPoint = quarterForPoint(f18, f15);
                                        iQuarterForPoint2 = quarterForPoint(f23, f24);
                                    } else {
                                        iQuarterForPoint = quarterForPoint(f18, f15);
                                        iQuarterForPoint2 = quarterForPoint(f23, f24);
                                    }
                                    while (iQuarterForPoint <= iQuarterForPoint2) {
                                        if (iQuarterForPoint == 0) {
                                            f25 = f2;
                                            lineViewData.chartPath.lineTo(getMeasuredWidth(), f25);
                                        } else {
                                            if (iQuarterForPoint == 1) {
                                                lineViewData.chartPath.lineTo(getMeasuredWidth(), getMeasuredHeight());
                                                f25 = 0.0f;
                                            } else if (iQuarterForPoint == 2) {
                                                f25 = 0.0f;
                                                lineViewData.chartPath.lineTo(0.0f, getMeasuredHeight());
                                            } else {
                                                f25 = 0.0f;
                                                lineViewData.chartPath.lineTo(0.0f, 0.0f);
                                            }
                                            iQuarterForPoint++;
                                            f2 = f25;
                                        }
                                        iQuarterForPoint++;
                                        f2 = f25;
                                    }
                                }
                                f21 = f2;
                            } else {
                                f21 = f2;
                            }
                        } else {
                            f21 = f2;
                        }
                        f9 = f14 + measuredHeight6;
                        f5 = f5;
                        f6 = f6;
                    } else {
                        jArr = lineViewData.line.y;
                        f10 = f34;
                        if (i5 == 1) {
                            if (jArr[i2] == 0) {
                                f11 = f2;
                            } else {
                                f11 = lineViewData.alpha;
                            }
                        } else if (f7 == f2) {
                            f11 = f2;
                        } else {
                            f11 = (jArr[i2] * lineViewData.alpha) / f7;
                        }
                        chartData2 = this.chartData;
                        f12 = (((StackLinearChartData) chartData2).xPercentage[i2] * f8) - f10;
                        if (i2 == iMin) {
                            measuredWidth = getMeasuredWidth();
                        } else {
                            measuredWidth = (((StackLinearChartData) chartData2).xPercentage[i2 + 1] * f8) - f10;
                        }
                        i8 = i6;
                        if (f11 == f2) {
                            z = true;
                        }
                        float measuredHeight7 = ((getMeasuredHeight() - this.chartBottom) - BaseChartView.SIGNATURE_TEXT_HEIGHT) * f11;
                        measuredHeight = ((getMeasuredHeight() - this.chartBottom) - measuredHeight7) - f9;
                        this.startFromY[i7] = measuredHeight;
                        measuredHeight2 = getMeasuredHeight() - this.chartBottom;
                        if (i2 == iMin) {
                            f13 = measuredHeight2;
                            f5 = f12;
                        } else {
                            f13 = measuredHeight2;
                            if (i2 == iMax) {
                                f6 = f12;
                            }
                        }
                        float f313 = measuredWidth;
                        if (this.transitionMode == 2) {
                            f14 = f9;
                            i9 = i5;
                            f15 = measuredHeight;
                            f16 = f2;
                            f17 = f12;
                            f18 = f17;
                            f19 = f13;
                        } else {
                            f14 = f9;
                            i9 = i5;
                            f15 = measuredHeight;
                            f16 = f2;
                            f17 = f12;
                            f18 = f17;
                            f19 = f13;
                        }
                        if (i2 == iMax) {
                            measuredHeight4 = getMeasuredHeight();
                            if (this.transitionMode == 2) {
                                f26 = f2;
                                f27 = measuredHeight4;
                            } else {
                                f26 = f2;
                                f27 = measuredHeight4;
                            }
                            lineViewData.chartPath.moveTo(f26, f27);
                            this.skipPoints[i7] = false;
                        } else {
                            f6 = f6;
                        }
                        transitionParams = this.transitionParams;
                        if (transitionParams == null) {
                            f20 = f2;
                        } else {
                            f20 = transitionParams.progress;
                        }
                        if (f11 != f2) {
                            if (this.skipPoints[i7]) {
                                if (i7 == i8) {
                                    lineViewData.chartPath.lineTo(f17, f19 * (f - f20));
                                } else {
                                    lineViewData.chartPath.lineTo(f17, f19);
                                }
                            }
                            if (i7 == i8) {
                                lineViewData.chartPath.lineTo(f18, (f - f20) * f15);
                            } else {
                                lineViewData.chartPath.lineTo(f18, f15);
                            }
                            this.skipPoints[i7] = false;
                        } else {
                            if (this.skipPoints[i7]) {
                                if (i7 == i8) {
                                    lineViewData.chartPath.lineTo(f17, f19 * (f - f20));
                                } else {
                                    lineViewData.chartPath.lineTo(f17, f19);
                                }
                            }
                            if (i7 == i8) {
                                lineViewData.chartPath.lineTo(f18, (f - f20) * f15);
                            } else {
                                lineViewData.chartPath.lineTo(f18, f15);
                            }
                            this.skipPoints[i7] = false;
                        }
                        if (i2 == iMin) {
                            measuredWidth2 = getMeasuredWidth();
                            measuredHeight3 = getMeasuredHeight();
                            if (this.transitionMode != 2) {
                                lineViewData.chartPath.lineTo(measuredWidth2, measuredHeight3);
                            } else {
                                lineViewData.chartPath.lineTo(measuredWidth2, measuredHeight3);
                            }
                            if (this.transitionMode == 2) {
                                if (i7 != i8) {
                                    TransitionParams transitionParams14 = this.transitionParams;
                                    f22 = (fCenterY - transitionParams14.startY[i7]) / (fCenterX - transitionParams14.startX[i7]);
                                    if (f22 > f2) {
                                        degrees = Math.toDegrees(-Math.atan(f22));
                                    } else {
                                        degrees = Math.toDegrees(Math.atan(Math.abs(f22)));
                                    }
                                    TransitionParams transitionParams15 = this.transitionParams;
                                    float f69 = transitionParams15.startX[i7];
                                    float f610 = transitionParams15.startY[i7];
                                    float[] fArr15 = this.mapPoints;
                                    fArr15[0] = f69;
                                    fArr15[1] = f610;
                                    this.matrix.reset();
                                    Matrix matrix7 = this.matrix;
                                    TransitionParams transitionParams16 = this.transitionParams;
                                    float f611 = transitionParams16.progress;
                                    matrix7.postRotate(((((float) degrees) - 90.0f) * f611) + (f611 * transitionParams16.angle[i7]), fCenterX, fCenterY);
                                    this.matrix.mapPoints(this.mapPoints);
                                    float[] fArr16 = this.mapPoints;
                                    f23 = fArr16[0];
                                    f24 = fArr16[1];
                                    if (Math.abs(f18 - f23) < 0.001d) {
                                        iQuarterForPoint = quarterForPoint(f18, f15);
                                        iQuarterForPoint2 = quarterForPoint(f23, f24);
                                    } else {
                                        iQuarterForPoint = quarterForPoint(f18, f15);
                                        iQuarterForPoint2 = quarterForPoint(f23, f24);
                                    }
                                    while (iQuarterForPoint <= iQuarterForPoint2) {
                                        if (iQuarterForPoint == 0) {
                                            f25 = f2;
                                            lineViewData.chartPath.lineTo(getMeasuredWidth(), f25);
                                        } else {
                                            if (iQuarterForPoint == 1) {
                                                lineViewData.chartPath.lineTo(getMeasuredWidth(), getMeasuredHeight());
                                                f25 = 0.0f;
                                            } else if (iQuarterForPoint == 2) {
                                                f25 = 0.0f;
                                                lineViewData.chartPath.lineTo(0.0f, getMeasuredHeight());
                                            } else {
                                                f25 = 0.0f;
                                                lineViewData.chartPath.lineTo(0.0f, 0.0f);
                                            }
                                            iQuarterForPoint++;
                                            f2 = f25;
                                        }
                                        iQuarterForPoint++;
                                        f2 = f25;
                                    }
                                }
                                f21 = f2;
                            } else {
                                f21 = f2;
                            }
                        } else {
                            f21 = f2;
                        }
                        f9 = f14 + measuredHeight7;
                        f5 = f5;
                        f6 = f6;
                    }
                    i7++;
                    f2 = f21;
                    i5 = i9;
                    i = i;
                    iMax = iMax;
                    i6 = i8;
                    f34 = f10;
                }
                i2++;
                f33 = f8;
                f3 = f311;
                iMax = iMax;
            }
            i3 = i;
            canvas.save();
            canvas.clipRect(f6, BaseChartView.SIGNATURE_TEXT_HEIGHT, f5, getMeasuredHeight() - this.chartBottom);
            if (z) {
                canvas.drawColor(Theme.getColor(Theme.key_statisticChartLineEmpty));
            }
            while (size >= 0) {
                LineViewData lineViewData4 = (LineViewData) this.lines.get(size);
                lineViewData4.paint.setAlpha(i3);
                canvas.drawPath(lineViewData4.chartPath, lineViewData4.paint);
                lineViewData4.paint.setAlpha(255);
            }
            canvas.restore();
            canvas.restore();
        }
    }

    private int quarterForPoint(float f, float f2) {
        float fCenterX = this.chartArea.centerX();
        float fCenterY = this.chartArea.centerY() + AndroidUtilities.dp(16.0f);
        if (f >= fCenterX && f2 <= fCenterY) {
            return 0;
        }
        if (f < fCenterX || f2 < fCenterY) {
            return (f >= fCenterX || f2 < fCenterY) ? 3 : 2;
        }
        return 1;
    }

    /* JADX WARN: Code duplicated, block: B:39:0x00ce  */
    @Override // org.telegram.ui.Charts.BaseChartView
    protected void drawPickerChart(Canvas canvas) {
        long j;
        float f;
        float f2;
        float f3;
        if (this.chartData != null) {
            int size = this.lines.size();
            for (int i = 0; i < size; i++) {
                ((StackLinearViewData) this.lines.get(i)).chartPathPicker.reset();
            }
            ChartData chartData = this.chartData;
            int i2 = ((StackLinearChartData) chartData).simplifiedSize;
            boolean[] zArr = this.skipPoints;
            if (zArr == null || zArr.length < ((StackLinearChartData) chartData).lines.size()) {
                this.skipPoints = new boolean[((StackLinearChartData) this.chartData).lines.size()];
            }
            int i3 = 0;
            boolean z = false;
            while (true) {
                boolean z2 = true;
                if (i3 >= i2) {
                    break;
                }
                float f4 = 0.0f;
                float f5 = 0.0f;
                int i4 = 0;
                int i5 = 0;
                int i6 = 0;
                while (true) {
                    j = 0;
                    if (i4 >= this.lines.size()) {
                        break;
                    }
                    LineViewData lineViewData = (LineViewData) this.lines.get(i4);
                    if (lineViewData.enabled || lineViewData.alpha != 0.0f) {
                        ChartData chartData2 = this.chartData;
                        if (((StackLinearChartData) chartData2).simplifiedY[i4][i3] > 0) {
                            f5 += ((StackLinearChartData) chartData2).simplifiedY[i4][i3] * lineViewData.alpha;
                            i5++;
                        }
                        i6 = i4;
                    }
                    i4++;
                }
                int i7 = i2 - 1;
                float f6 = (i3 / i7) * this.pickerWidth;
                float f7 = 0.0f;
                int i8 = 0;
                while (i8 < this.lines.size()) {
                    LineViewData lineViewData2 = (LineViewData) this.lines.get(i8);
                    long j2 = j;
                    if (lineViewData2.enabled || lineViewData2.alpha != f4) {
                        if (i5 == z2) {
                            if (((StackLinearChartData) this.chartData).simplifiedY[i8][i3] == j2) {
                                f2 = f4;
                                f = f2;
                            } else {
                                f2 = lineViewData2.alpha;
                                f = f4;
                            }
                        } else if (f5 == f4) {
                            f2 = f4;
                            f = f2;
                        } else {
                            f = f4;
                            f2 = (((StackLinearChartData) this.chartData).simplifiedY[i8][i3] * lineViewData2.alpha) / f5;
                        }
                        if (f2 == f && i8 == i6) {
                            z = z2;
                        }
                        int i9 = this.pikerHeight;
                        float f8 = f2 * i9;
                        float f9 = (i9 - f8) - f7;
                        if (i3 == 0) {
                            f3 = f;
                            lineViewData2.chartPathPicker.moveTo(f3, i9);
                            this.skipPoints[i8] = false;
                        } else {
                            f3 = f;
                        }
                        ChartData chartData3 = this.chartData;
                        if (((StackLinearChartData) chartData3).simplifiedY[i8][i3] == j2 && i3 > 0 && ((StackLinearChartData) chartData3).simplifiedY[i8][i3 - 1] == j2 && i3 < i7 && ((StackLinearChartData) chartData3).simplifiedY[i8][i3 + 1] == j2) {
                            if (!this.skipPoints[i8]) {
                                lineViewData2.chartPathPicker.lineTo(f6, this.pikerHeight);
                            }
                            this.skipPoints[i8] = z2;
                        } else {
                            if (this.skipPoints[i8]) {
                                lineViewData2.chartPathPicker.lineTo(f6, this.pikerHeight);
                            }
                            lineViewData2.chartPathPicker.lineTo(f6, f9);
                            this.skipPoints[i8] = false;
                        }
                        if (i3 == i7) {
                            lineViewData2.chartPathPicker.lineTo(this.pickerWidth, this.pikerHeight);
                        }
                        f7 += f8;
                    } else {
                        i2 = i2;
                        z2 = z2;
                        f3 = f4;
                    }
                    i8++;
                    f4 = f3;
                    j = j2;
                    z2 = z2;
                    i2 = i2;
                }
                i3++;
            }
            if (z) {
                canvas.drawColor(Theme.getColor(Theme.key_statisticChartLineEmpty));
            }
            for (int size2 = this.lines.size() - 1; size2 >= 0; size2--) {
                LineViewData lineViewData3 = (LineViewData) this.lines.get(size2);
                canvas.drawPath(lineViewData3.chartPathPicker, lineViewData3.paint);
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

    /* JADX WARN: Code duplicated, block: B:38:0x0109  */
    @Override // org.telegram.ui.Charts.BaseChartView
    public void fillTransitionParams(TransitionParams transitionParams) {
        float f;
        float f2;
        int i;
        ChartData chartData = this.chartData;
        if (chartData == null) {
            return;
        }
        float f3 = this.chartWidth;
        ChartPickerDelegate chartPickerDelegate = this.pickerDelegate;
        float f4 = chartPickerDelegate.pickerEnd;
        float f5 = chartPickerDelegate.pickerStart;
        float f6 = f3 / (f4 - f5);
        float f7 = BaseChartView.HORIZONTAL_PADDING;
        float f8 = (f5 * f6) - f7;
        int i2 = 2;
        int i3 = 1;
        int i4 = ((int) (f7 / (((StackLinearChartData) chartData).xPercentage.length < 2 ? 1.0f : ((StackLinearChartData) chartData).xPercentage[1] * f6))) + 1;
        int i5 = 0;
        int iMax = Math.max(0, (this.startXIndex - i4) - 1);
        int iMin = Math.min(((StackLinearChartData) this.chartData).xPercentage.length - 1, this.endXIndex + i4 + 1);
        this.transitionParams.startX = new float[((StackLinearChartData) this.chartData).lines.size()];
        this.transitionParams.startY = new float[((StackLinearChartData) this.chartData).lines.size()];
        this.transitionParams.endX = new float[((StackLinearChartData) this.chartData).lines.size()];
        this.transitionParams.endY = new float[((StackLinearChartData) this.chartData).lines.size()];
        this.transitionParams.angle = new float[((StackLinearChartData) this.chartData).lines.size()];
        int i6 = 0;
        while (i6 < i2) {
            int i7 = i6 == i3 ? iMin : iMax;
            float f9 = 0.0f;
            int i8 = i5;
            int i9 = i8;
            float f10 = 0.0f;
            while (i8 < this.lines.size()) {
                LineViewData lineViewData = (LineViewData) this.lines.get(i8);
                if (lineViewData.enabled || lineViewData.alpha != f9) {
                    f2 = f9;
                    i = i8;
                    long j = lineViewData.line.y[i7];
                    if (j > 0) {
                        f10 += j * lineViewData.alpha;
                        i9++;
                    }
                } else {
                    f2 = f9;
                    i = i8;
                }
                i8 = i + 1;
                f9 = f2;
            }
            float f11 = f9;
            int i10 = 0;
            int i11 = 0;
            while (i10 < this.lines.size()) {
                LineViewData lineViewData2 = (LineViewData) this.lines.get(i10);
                if (lineViewData2.enabled || lineViewData2.alpha != f11) {
                    long[] jArr = lineViewData2.line.y;
                    if (i9 == i3) {
                        if (jArr[i7] == 0) {
                            f = f11;
                        } else {
                            f = lineViewData2.alpha;
                        }
                    } else if (f10 == f11) {
                        f = f11;
                    } else {
                        f = (jArr[i7] * lineViewData2.alpha) / f10;
                    }
                    float f12 = (((StackLinearChartData) this.chartData).xPercentage[i7] * f6) - f8;
                    float measuredHeight = f * ((getMeasuredHeight() - this.chartBottom) - BaseChartView.SIGNATURE_TEXT_HEIGHT);
                    float f13 = i11;
                    float measuredHeight2 = ((getMeasuredHeight() - this.chartBottom) - measuredHeight) - f13;
                    i11 = (int) (f13 + measuredHeight);
                    if (i6 == 0) {
                        TransitionParams transitionParams2 = this.transitionParams;
                        transitionParams2.startX[i10] = f12;
                        transitionParams2.startY[i10] = measuredHeight2;
                    } else {
                        TransitionParams transitionParams3 = this.transitionParams;
                        transitionParams3.endX[i10] = f12;
                        transitionParams3.endY[i10] = measuredHeight2;
                    }
                }
                i10++;
                i3 = 1;
            }
            i6++;
            i5 = 0;
            i2 = 2;
            i3 = 1;
        }
    }
}
