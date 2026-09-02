package com.google.zxing.qrcode;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.RectF;
import android.graphics.Region;
import android.graphics.drawable.GradientDrawable;
import com.google.zxing.EncodeHintType;
import com.google.zxing.qrcode.decoder.ErrorCorrectionLevel;
import com.google.zxing.qrcode.encoder.ByteMatrix;
import com.google.zxing.qrcode.encoder.Encoder;
import java.util.Arrays;
import java.util.Map;
import org.telegram.messenger.AndroidUtilities;
import org.telegram.messenger.R;
import org.telegram.messenger.SvgHelper;

public final class QRCodeWriter {
    private int imageBlockX;
    private int imageBloks;
    private int imageSize;
    private ByteMatrix input;
    private int sideQuadSize;
    private float[] radii = new float[8];
    public boolean includeSideQuads = true;

    public Bitmap encode(String str, int i, int i2, Map map, Bitmap bitmap) {
        return encode(str, i, i2, map, bitmap, 1.0f, -1, -16777216);
    }

    public Bitmap encode(String str, int i, int i2, Map map, Bitmap bitmap, float f, int i3, int i4) {
        return encode(str, i, i2, map, bitmap, f, i3, i4, true);
    }

    /* JADX WARN: Code duplicated, block: B:14:0x0045 A[PHI: r4
  0x0045: PHI (r4v3 com.google.zxing.qrcode.decoder.ErrorCorrectionLevel) = 
  (r4v2 com.google.zxing.qrcode.decoder.ErrorCorrectionLevel)
  (r4v27 com.google.zxing.qrcode.decoder.ErrorCorrectionLevel)
 binds: [B:7:0x0018, B:12:0x0034] A[DONT_GENERATE, DONT_INLINE]] */
    /* JADX WARN: Multi-variable type inference failed */
    public Bitmap encode(String str, int i, int i2, Map map, Bitmap bitmap, float f, int i3, int i4, boolean z) {
        int i5;
        int i6;
        Bitmap bitmap2;
        Paint paint;
        GradientDrawable gradientDrawable;
        Canvas canvas;
        char c;
        int i7;
        int i8;
        boolean z2;
        int i9;
        Canvas canvas2;
        Paint paint2;
        if (str.isEmpty()) {
            throw new IllegalArgumentException("Found empty contents");
        }
        if (i < 0 || i2 < 0) {
            throw new IllegalArgumentException("Requested dimensions are too small: " + i + 'x' + i2);
        }
        ErrorCorrectionLevel errorCorrectionLevelValueOf = ErrorCorrectionLevel.L;
        if (map != null) {
            EncodeHintType encodeHintType = EncodeHintType.ERROR_CORRECTION;
            if (map.containsKey(encodeHintType)) {
                errorCorrectionLevelValueOf = ErrorCorrectionLevel.valueOf(map.get(encodeHintType).toString());
            }
            EncodeHintType encodeHintType2 = EncodeHintType.MARGIN;
            if (map.containsKey(encodeHintType2)) {
                i5 = Integer.parseInt(map.get(encodeHintType2).toString());
            } else {
                i5 = 4;
            }
        } else {
            i5 = 4;
        }
        ByteMatrix matrix = Encoder.encode(str, errorCorrectionLevelValueOf, map).getMatrix();
        this.input = matrix;
        if (matrix == null) {
            throw new IllegalStateException();
        }
        int width = matrix.getWidth();
        int height = this.input.getHeight();
        int i10 = 0;
        for (int i11 = 0; i11 < width && has(i11, 0); i11++) {
            this.sideQuadSize++;
        }
        int i12 = i5 * 2;
        int i13 = width + i12;
        int i14 = i12 + height;
        int iMin = Math.min(Math.max(i, i13) / i13, Math.max(i2, i14) / i14);
        int i15 = iMin * width;
        int i16 = i15 + 32;
        Bitmap bitmapCreateBitmap = (bitmap == null || bitmap.getWidth() != i16) ? Bitmap.createBitmap(i16, i16, Bitmap.Config.ARGB_8888) : bitmap;
        Canvas canvas3 = new Canvas(bitmapCreateBitmap);
        canvas3.drawColor(i3);
        Paint paint3 = new Paint(1);
        paint3.setColor(i4);
        GradientDrawable gradientDrawable2 = new GradientDrawable();
        gradientDrawable2.setShape(0);
        gradientDrawable2.setCornerRadii(this.radii);
        int iRound = !z ? 0 : Math.round((i15 / 4.65f) / iMin);
        this.imageBloks = iRound;
        if (iRound % 2 != width % 2) {
            this.imageBloks = iRound + 1;
        }
        int i17 = this.imageBloks;
        this.imageBlockX = (width - i17) / 2;
        int i18 = (i17 * iMin) - 24;
        this.imageSize = i18;
        int i19 = (i16 - i18) / 2;
        int i20 = 16;
        if (this.includeSideQuads) {
            paint3.setColor(i4);
            canvas = canvas3;
            paint = paint3;
            bitmap2 = bitmapCreateBitmap;
            i6 = iMin;
            c = 4;
            gradientDrawable = gradientDrawable2;
            drawSideQuadsGradient(canvas, paint, gradientDrawable, this.sideQuadSize, iMin, 16, i16, f, this.radii, i3, i4);
        } else {
            i6 = iMin;
            bitmap2 = bitmapCreateBitmap;
            paint = paint3;
            gradientDrawable = gradientDrawable2;
            canvas = canvas3;
            c = 4;
        }
        boolean z3 = Color.alpha(i3) == 0;
        float f2 = (i6 / 2.0f) * f;
        int i21 = 16;
        int i22 = 0;
        while (i22 < height) {
            int i23 = i20;
            int i24 = i10;
            while (i24 < width) {
                int i25 = i10;
                if (has(i24, i22)) {
                    Arrays.fill(this.radii, f2);
                    if (has(i24, i22 - 1)) {
                        float[] fArr = this.radii;
                        fArr[r28] = 0.0f;
                        fArr[i25] = 0.0f;
                        fArr[3] = 0.0f;
                        fArr[2] = 0.0f;
                    }
                    if (has(i24, i22 + 1)) {
                        float[] fArr2 = this.radii;
                        fArr2[7] = 0.0f;
                        fArr2[6] = 0.0f;
                        fArr2[5] = 0.0f;
                        fArr2[c] = 0.0f;
                    }
                    if (has(i24 - 1, i22)) {
                        float[] fArr3 = this.radii;
                        fArr3[r28] = 0.0f;
                        fArr3[i25] = 0.0f;
                        fArr3[7] = 0.0f;
                        fArr3[6] = 0.0f;
                    }
                    if (has(i24 + 1, i22)) {
                        float[] fArr4 = this.radii;
                        fArr4[3] = 0.0f;
                        fArr4[2] = 0.0f;
                        fArr4[5] = 0.0f;
                        fArr4[c] = 0.0f;
                    }
                    gradientDrawable.setColor(i4);
                    gradientDrawable.setBounds(i23, i21, i23 + i6, i21 + i6);
                    gradientDrawable.draw(canvas);
                    paint2 = paint;
                    canvas2 = canvas;
                    z2 = z3;
                } else {
                    Paint paint4 = paint;
                    Arrays.fill(this.radii, 0.0f);
                    int i26 = i24 - 1;
                    int i27 = i22 - 1;
                    if (has(i26, i27) && has(i26, i22) && has(i24, i27)) {
                        float[] fArr5 = this.radii;
                        fArr5[r28] = f2;
                        fArr5[i25] = f2;
                        i7 = r28;
                    } else {
                        i7 = i25;
                    }
                    int i28 = i7;
                    int i29 = i24 + 1;
                    if (has(i29, i27) && has(i29, i22) && has(i24, i27)) {
                        float[] fArr6 = this.radii;
                        fArr6[3] = f2;
                        fArr6[2] = f2;
                        i8 = r28;
                    } else {
                        i8 = i28;
                    }
                    z2 = z3;
                    int i30 = i22 + 1;
                    if (has(i26, i30) && has(i26, i22) && has(i24, i30)) {
                        float[] fArr7 = this.radii;
                        fArr7[7] = f2;
                        fArr7[6] = f2;
                        i8 = r28;
                    }
                    if (has(i29, i30) && has(i29, i22) && has(i24, i30)) {
                        float[] fArr8 = this.radii;
                        fArr8[5] = f2;
                        fArr8[c] = f2;
                        i9 = r28;
                    } else {
                        i9 = i8;
                    }
                    if (i9 == 0 || z2) {
                        canvas2 = canvas;
                        paint2 = paint4;
                    } else {
                        int i31 = i23 + i6;
                        int i32 = i21 + i6;
                        canvas.drawRect(i23, i21, i31, i32, paint4);
                        canvas2 = canvas;
                        paint2 = paint4;
                        gradientDrawable.setColor(i3);
                        gradientDrawable.setBounds(i23, i21, i31, i32);
                        gradientDrawable.draw(canvas2);
                    }
                }
                i24++;
                i23 += i6;
                canvas = canvas2;
                paint = paint2;
                z3 = z2;
                i10 = i25;
            }
            i22++;
            i21 += i6;
            paint = paint;
            i10 = i10;
            i20 = 16;
        }
        Canvas canvas4 = canvas;
        boolean z4 = i10;
        if (z) {
            String res = AndroidUtilities.readRes(null, R.raw.qr_logo);
            int i33 = this.imageSize;
            Bitmap bitmap3 = SvgHelper.getBitmap(res, i33, i33, z4);
            float f3 = i19;
            canvas4.drawBitmap(bitmap3, f3, f3, (Paint) null);
            bitmap3.recycle();
        }
        canvas4.setBitmap(null);
        return bitmap2;
    }

    public static void drawSideQuadsGradient(Canvas canvas, Paint paint, GradientDrawable gradientDrawable, float f, float f2, int i, float f3, float f4, float[] fArr, int i2, int i3) {
        float f5;
        float f6;
        float f7;
        float f8;
        boolean z = Color.alpha(i2) == 0;
        gradientDrawable.setShape(0);
        gradientDrawable.setCornerRadii(fArr);
        Path path = new Path();
        RectF rectF = new RectF();
        for (int i4 = 0; i4 < 3; i4++) {
            if (i4 == 0) {
                f8 = i;
                f7 = f8;
            } else {
                if (i4 == 1) {
                    f6 = i;
                    f5 = (f3 - (f * f2)) - f6;
                } else {
                    f5 = i;
                    f6 = (f3 - (f * f2)) - f5;
                }
                f7 = f5;
                f8 = f6;
            }
            if (z) {
                float f9 = (f - 1.0f) * f2;
                rectF.set(f7 + f2, f8 + f2, f7 + f9, f9 + f8);
                float f10 = ((f * f2) / 4.0f) * f4;
                path.reset();
                path.addRoundRect(rectF, f10, f10, Path.Direction.CW);
                path.close();
                canvas.save();
                canvas.clipPath(path, Region.Op.DIFFERENCE);
            }
            float f11 = f * f2;
            Arrays.fill(fArr, (f11 / 3.0f) * f4);
            gradientDrawable.setColor(i3);
            gradientDrawable.setBounds((int) f7, (int) f8, (int) (f7 + f11), (int) (f8 + f11));
            gradientDrawable.draw(canvas);
            float f12 = f8;
            float f13 = f7 + f2;
            float f14 = f12 + f2;
            float f15 = (f - 1.0f) * f2;
            float f16 = f7 + f15;
            float f17 = f15 + f12;
            canvas.drawRect(f13, f14, f16, f17, paint);
            if (z) {
                canvas.restore();
            }
            if (!z) {
                Arrays.fill(fArr, (f11 / 4.0f) * f4);
                gradientDrawable.setColor(i2);
                gradientDrawable.setBounds((int) f13, (int) f14, (int) f16, (int) f17);
                gradientDrawable.draw(canvas);
            }
            float f18 = (f - 2.0f) * f2;
            Arrays.fill(fArr, (f18 / 4.0f) * f4);
            gradientDrawable.setColor(i3);
            float f19 = 2.0f * f2;
            gradientDrawable.setBounds((int) (f7 + f19), (int) (f12 + f19), (int) (f7 + f18), (int) (f12 + f18));
            gradientDrawable.draw(canvas);
        }
    }

    public static void drawSideQuads(Canvas canvas, float f, float f2, Paint paint, float f3, float f4, int i, float f5, float f6, float[] fArr, boolean z) {
        float f7;
        float f8;
        Path path = new Path();
        for (int i2 = 0; i2 < 3; i2++) {
            if (i2 == 0) {
                f7 = i;
                f8 = f7;
            } else if (i2 == 1) {
                f8 = i;
                f7 = (f5 - (f3 * f4)) - f8;
            } else {
                f7 = i;
                f8 = (f5 - (f3 * f4)) - f7;
            }
            float f9 = f7 + f;
            float f10 = f8 + f2;
            if (z) {
                RectF rectF = AndroidUtilities.rectTmp;
                float f11 = (f3 - 1.0f) * f4;
                rectF.set(f9 + f4, f10 + f4, f9 + f11, f11 + f10);
                float f12 = ((f3 * f4) / 4.0f) * f6;
                path.reset();
                path.addRoundRect(rectF, f12, f12, Path.Direction.CW);
                path.close();
                canvas.save();
                canvas.clipPath(path, Region.Op.DIFFERENCE);
            }
            float f13 = f3 * f4;
            float f14 = (f13 / 3.0f) * f6;
            RectF rectF2 = AndroidUtilities.rectTmp;
            rectF2.set(f9, f10, f9 + f13, f13 + f10);
            canvas.drawRoundRect(rectF2, f14, f14, paint);
            if (z) {
                canvas.restore();
            }
            float f15 = (f3 - 2.0f) * f4;
            float f16 = (f15 / 4.0f) * f6;
            float f17 = 2.0f * f4;
            rectF2.set(f9 + f17, f17 + f10, f9 + f15, f10 + f15);
            canvas.drawRoundRect(rectF2, f16, f16, paint);
        }
    }

    private boolean has(int i, int i2) {
        int i3 = this.imageBlockX;
        if (i >= i3) {
            int i4 = this.imageBloks;
            if (i < i3 + i4 && i2 >= i3 && i2 < i3 + i4) {
                return false;
            }
        }
        if ((i < this.sideQuadSize || i >= this.input.getWidth() - this.sideQuadSize) && i2 < this.sideQuadSize) {
            return false;
        }
        return (i >= this.sideQuadSize || i2 < this.input.getHeight() - this.sideQuadSize) && i >= 0 && i2 >= 0 && i < this.input.getWidth() && i2 < this.input.getHeight() && this.input.get(i, i2) == 1;
    }

    public int getImageSize() {
        return this.imageSize;
    }
}
