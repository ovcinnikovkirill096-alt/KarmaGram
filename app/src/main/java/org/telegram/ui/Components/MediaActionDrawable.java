package org.telegram.ui.Components;

import android.graphics.Canvas;
import android.graphics.ColorFilter;
import android.graphics.LinearGradient;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffColorFilter;
import android.graphics.RectF;
import android.graphics.Shader;
import android.graphics.drawable.Drawable;
import android.support.annotation.Keep;
import android.text.TextPaint;
import android.view.animation.DecelerateInterpolator;
import org.telegram.messenger.AndroidUtilities;
import org.telegram.ui.ActionBar.Theme;

public class MediaActionDrawable extends Drawable {
    private float animatedDownloadProgress;
    private boolean animatingTransition;
    private ColorFilter colorFilter;
    private int currentIcon;
    private MediaActionDrawableDelegate delegate;
    private float downloadProgress;
    private float downloadProgressAnimationStart;
    private float downloadProgressTime;
    private float downloadRadOffset;
    private LinearGradient gradientDrawable;
    private Matrix gradientMatrix;
    private boolean hasOverlayImage;
    private boolean isMini;
    private long lastAnimationTime;
    private Theme.MessageDrawable messageDrawable;
    private int nextIcon;
    private String percentString;
    private int percentStringWidth;
    private float savedTransitionProgress;
    private TextPaint textPaint = new TextPaint(1);
    public Paint paint = new Paint(1);
    private Paint backPaint = new Paint(1);
    public Paint paint2 = new Paint(1);
    private Paint paint3 = new Paint(1);
    private RectF rect = new RectF();
    private float scale = 1.0f;
    private DecelerateInterpolator interpolator = new DecelerateInterpolator();
    private float transitionAnimationTime = 400.0f;
    private int lastPercent = -1;
    private float overrideAlpha = 1.0f;
    private float transitionProgress = 1.0f;
    public boolean drawProgressCircle = true;
    private float downloadIconScale = 1.0f;

    public interface MediaActionDrawableDelegate {
        void invalidate();
    }

    public static float getCircleValue(float f) {
        while (f > 360.0f) {
            f -= 360.0f;
        }
        return f;
    }

    @Override // android.graphics.drawable.Drawable
    public int getOpacity() {
        return -2;
    }

    @Override // android.graphics.drawable.Drawable
    public void setAlpha(int i) {
    }

    public void setDownloadIconScale(float f) {
        this.downloadIconScale = f;
    }

    @Keep
    public float getDownloadIconScale() {
        return this.downloadIconScale;
    }

    public MediaActionDrawable() {
        this.paint.setColor(-1);
        this.paint.setStrokeCap(Paint.Cap.ROUND);
        this.paint.setStrokeWidth(AndroidUtilities.dp(3.0f));
        this.paint.setStyle(Paint.Style.STROKE);
        this.paint3.setColor(-1);
        this.textPaint.setTypeface(AndroidUtilities.bold());
        this.textPaint.setTextSize(AndroidUtilities.dp(13.0f));
        this.textPaint.setColor(-1);
        this.paint2.setColor(-1);
    }

    public void setOverrideAlpha(float f) {
        this.overrideAlpha = f;
    }

    @Override // android.graphics.drawable.Drawable
    public void setColorFilter(ColorFilter colorFilter) {
        this.paint.setColorFilter(colorFilter);
        this.paint2.setColorFilter(colorFilter);
        this.paint3.setColorFilter(colorFilter);
        this.textPaint.setColorFilter(colorFilter);
    }

    public void setColor(int i) {
        int i2 = (-16777216) | i;
        this.paint.setColor(i2);
        this.paint2.setColor(i2);
        this.paint3.setColor(i2);
        this.textPaint.setColor(i2);
        this.colorFilter = new PorterDuffColorFilter(i, PorterDuff.Mode.MULTIPLY);
    }

    public void setBackColor(int i) {
        this.backPaint.setColor(i | (-16777216));
    }

    public void setMini(boolean z) {
        this.isMini = z;
        this.paint.setStrokeWidth(AndroidUtilities.dp(z ? 2.0f : 3.0f));
    }

    public void setDelegate(MediaActionDrawableDelegate mediaActionDrawableDelegate) {
        this.delegate = mediaActionDrawableDelegate;
    }

    public boolean setIcon(int i, boolean z) {
        int i2;
        int i3;
        if (this.currentIcon == i && (i3 = this.nextIcon) != i) {
            this.currentIcon = i3;
            this.transitionProgress = 1.0f;
        }
        if (z) {
            int i4 = this.currentIcon;
            if (i4 == i || (i2 = this.nextIcon) == i) {
                return false;
            }
            if ((i4 == 0 && i == 1) || (i4 == 1 && i == 0)) {
                this.transitionAnimationTime = 300.0f;
            } else if (i4 == 2 && (i == 3 || i == 14)) {
                this.transitionAnimationTime = this.drawProgressCircle ? 400.0f : 250.0f;
            } else if (i4 != 4 && i == 6) {
                this.transitionAnimationTime = 360.0f;
            } else if ((i4 == 4 && i == 14) || (i4 == 14 && i == 4)) {
                this.transitionAnimationTime = 160.0f;
            } else {
                this.transitionAnimationTime = 220.0f;
            }
            if (this.animatingTransition) {
                this.currentIcon = i2;
            }
            this.animatingTransition = true;
            this.nextIcon = i;
            this.savedTransitionProgress = this.transitionProgress;
            this.transitionProgress = 0.0f;
        } else {
            if (this.currentIcon == i) {
                return false;
            }
            this.animatingTransition = false;
            this.nextIcon = i;
            this.currentIcon = i;
            this.savedTransitionProgress = this.transitionProgress;
            this.transitionProgress = 1.0f;
        }
        if (i == 3 || i == 14) {
            this.downloadRadOffset = 112.0f;
            this.animatedDownloadProgress = 0.0f;
            this.downloadProgressAnimationStart = 0.0f;
            this.downloadProgressTime = 0.0f;
        }
        invalidateSelf();
        return true;
    }

    public int getCurrentIcon() {
        return this.nextIcon;
    }

    public int getPreviousIcon() {
        return this.currentIcon;
    }

    public void setProgress(float f, boolean z) {
        if (this.downloadProgress == f) {
            return;
        }
        if (!z) {
            this.animatedDownloadProgress = f;
            this.downloadProgressAnimationStart = f;
        } else {
            if (this.animatedDownloadProgress > f) {
                this.animatedDownloadProgress = f;
            }
            this.downloadProgressAnimationStart = this.animatedDownloadProgress;
        }
        this.downloadProgress = f;
        this.downloadProgressTime = 0.0f;
        invalidateSelf();
    }

    public float getProgress() {
        return this.downloadProgress;
    }

    public float getTransitionProgress() {
        if (this.animatingTransition) {
            return this.transitionProgress;
        }
        return 1.0f;
    }

    public void setBackgroundDrawable(Theme.MessageDrawable messageDrawable) {
        this.messageDrawable = messageDrawable;
    }

    public void setBackgroundGradientDrawable(LinearGradient linearGradient) {
        this.gradientDrawable = linearGradient;
        this.gradientMatrix = new Matrix();
    }

    public void setHasOverlayImage(boolean z) {
        this.hasOverlayImage = z;
    }

    @Override // android.graphics.drawable.Drawable
    public void setBounds(int i, int i2, int i3, int i4) {
        super.setBounds(i, i2, i3, i4);
        float intrinsicWidth = (i3 - i) / getIntrinsicWidth();
        this.scale = intrinsicWidth;
        if (intrinsicWidth < 0.7f) {
            this.paint.setStrokeWidth(AndroidUtilities.dp(2.0f));
        }
    }

    @Override // android.graphics.drawable.Drawable
    public void invalidateSelf() {
        super.invalidateSelf();
        MediaActionDrawableDelegate mediaActionDrawableDelegate = this.delegate;
        if (mediaActionDrawableDelegate != null) {
            mediaActionDrawableDelegate.invalidate();
        }
    }

    public void applyShaderMatrix(boolean z) {
        Theme.MessageDrawable messageDrawable = this.messageDrawable;
        if (messageDrawable == null || !messageDrawable.hasGradient() || this.hasOverlayImage) {
            return;
        }
        android.graphics.Rect bounds = getBounds();
        Shader gradientShader = this.messageDrawable.getGradientShader();
        Matrix matrix = this.messageDrawable.getMatrix();
        matrix.reset();
        this.messageDrawable.applyMatrixScale();
        if (z) {
            matrix.postTranslate(-bounds.centerX(), (-this.messageDrawable.getTopY()) + bounds.top);
        } else {
            matrix.postTranslate(0.0f, -this.messageDrawable.getTopY());
        }
        gradientShader.setLocalMatrix(matrix);
    }

    /* JADX WARN: Code duplicated, block: B:102:0x0309  */
    /* JADX WARN: Code duplicated, block: B:105:0x0317  */
    /* JADX WARN: Code duplicated, block: B:106:0x0328  */
    /* JADX WARN: Code duplicated, block: B:165:0x04e4  */
    /* JADX WARN: Code duplicated, block: B:189:0x0578  */
    /* JADX WARN: Code duplicated, block: B:235:0x066c  */
    /* JADX WARN: Code duplicated, block: B:238:0x0676  */
    /* JADX WARN: Code duplicated, block: B:240:0x0680  */
    /* JADX WARN: Code duplicated, block: B:242:0x0690  */
    /* JADX WARN: Code duplicated, block: B:244:0x0694  */
    /* JADX WARN: Code duplicated, block: B:247:0x06f0  */
    /* JADX WARN: Code duplicated, block: B:249:0x06f4  */
    /* JADX WARN: Code duplicated, block: B:251:0x06f9  */
    /* JADX WARN: Code duplicated, block: B:254:0x0701  */
    /* JADX WARN: Code duplicated, block: B:261:0x070e A[ADDED_TO_REGION] */
    /* JADX WARN: Code duplicated, block: B:266:0x0720  */
    /* JADX WARN: Code duplicated, block: B:267:0x0723  */
    /* JADX WARN: Code duplicated, block: B:270:0x0743  */
    /* JADX WARN: Code duplicated, block: B:274:0x074a  */
    /* JADX WARN: Code duplicated, block: B:278:0x0779  */
    /* JADX WARN: Code duplicated, block: B:279:0x077e  */
    /* JADX WARN: Code duplicated, block: B:281:0x0781  */
    /* JADX WARN: Code duplicated, block: B:285:0x0788  */
    /* JADX WARN: Code duplicated, block: B:291:0x07ad  */
    /* JADX WARN: Code duplicated, block: B:293:0x07b1  */
    /* JADX WARN: Code duplicated, block: B:295:0x07b5  */
    /* JADX WARN: Code duplicated, block: B:296:0x07ba  */
    /* JADX WARN: Code duplicated, block: B:298:0x07be  */
    /* JADX WARN: Code duplicated, block: B:300:0x07c4  */
    /* JADX WARN: Code duplicated, block: B:302:0x07c8  */
    /* JADX WARN: Code duplicated, block: B:304:0x07cd  */
    /* JADX WARN: Code duplicated, block: B:305:0x07d5  */
    /* JADX WARN: Code duplicated, block: B:307:0x07d9  */
    /* JADX WARN: Code duplicated, block: B:309:0x07e0  */
    /* JADX WARN: Code duplicated, block: B:311:0x07e4  */
    /* JADX WARN: Code duplicated, block: B:313:0x07e8  */
    /* JADX WARN: Code duplicated, block: B:315:0x07ec  */
    /* JADX WARN: Code duplicated, block: B:318:0x07f7 A[ADDED_TO_REGION] */
    /* JADX WARN: Code duplicated, block: B:319:0x07f9  */
    /* JADX WARN: Code duplicated, block: B:323:0x080c  */
    /* JADX WARN: Code duplicated, block: B:324:0x080f  */
    /* JADX WARN: Code duplicated, block: B:327:0x0828  */
    /* JADX WARN: Code duplicated, block: B:330:0x086f  */
    /* JADX WARN: Code duplicated, block: B:333:0x0878  */
    /* JADX WARN: Code duplicated, block: B:335:0x087c  */
    /* JADX WARN: Code duplicated, block: B:339:0x088d  */
    /* JADX WARN: Code duplicated, block: B:340:0x0892  */
    /* JADX WARN: Code duplicated, block: B:342:0x0896  */
    /* JADX WARN: Code duplicated, block: B:343:0x0899  */
    /* JADX WARN: Code duplicated, block: B:346:0x08a3  */
    /* JADX WARN: Code duplicated, block: B:347:0x08a6  */
    /* JADX WARN: Code duplicated, block: B:350:0x08b8  */
    /* JADX WARN: Code duplicated, block: B:353:0x08ee  */
    /* JADX WARN: Code duplicated, block: B:356:0x08f7  */
    /* JADX WARN: Code duplicated, block: B:358:0x08fb  */
    /* JADX WARN: Code duplicated, block: B:362:0x090a  */
    /* JADX WARN: Code duplicated, block: B:363:0x090d A[DONT_INVERT] */
    /* JADX WARN: Code duplicated, block: B:364:0x090f  */
    /* JADX WARN: Code duplicated, block: B:365:0x0912  */
    /* JADX WARN: Code duplicated, block: B:368:0x0935  */
    /* JADX WARN: Code duplicated, block: B:371:0x0946  */
    /* JADX WARN: Code duplicated, block: B:373:0x094a  */
    /* JADX WARN: Code duplicated, block: B:376:0x097c  */
    /* JADX WARN: Code duplicated, block: B:379:0x0984 A[ADDED_TO_REGION] */
    /* JADX WARN: Code duplicated, block: B:383:0x098c A[DONT_INVERT] */
    /* JADX WARN: Code duplicated, block: B:384:0x098e  */
    /* JADX WARN: Code duplicated, block: B:386:0x0992  */
    /* JADX WARN: Code duplicated, block: B:387:0x0994  */
    /* JADX WARN: Code duplicated, block: B:389:0x0998  */
    /* JADX WARN: Code duplicated, block: B:391:0x099c  */
    /* JADX WARN: Code duplicated, block: B:393:0x09a0  */
    /* JADX WARN: Code duplicated, block: B:395:0x09a8  */
    /* JADX WARN: Code duplicated, block: B:396:0x09ab  */
    /* JADX WARN: Code duplicated, block: B:398:0x09b0 A[PHI: r4
  0x09b0: PHI (r4v123 int) = (r4v120 int), (r4v135 int) binds: [B:401:0x09b6, B:397:0x09ae] A[DONT_GENERATE, DONT_INLINE]] */
    /* JADX WARN: Code duplicated, block: B:399:0x09b3 A[PHI: r4
  0x09b3: PHI (r4v122 int) = (r4v120 int), (r4v135 int) binds: [B:401:0x09b6, B:397:0x09ae] A[DONT_GENERATE, DONT_INLINE]] */
    /* JADX WARN: Code duplicated, block: B:400:0x09b5  */
    /* JADX WARN: Code duplicated, block: B:405:0x09bd A[ADDED_TO_REGION] */
    /* JADX WARN: Code duplicated, block: B:408:0x09c2 A[ADDED_TO_REGION] */
    /* JADX WARN: Code duplicated, block: B:422:0x0a17  */
    /* JADX WARN: Code duplicated, block: B:423:0x0a1a  */
    /* JADX WARN: Code duplicated, block: B:426:0x0a23  */
    /* JADX WARN: Code duplicated, block: B:436:0x0a4d A[DONT_INVERT] */
    /* JADX WARN: Code duplicated, block: B:437:0x0a4f  */
    /* JADX WARN: Code duplicated, block: B:441:0x0a59  */
    /* JADX WARN: Code duplicated, block: B:442:0x0a64  */
    /* JADX WARN: Code duplicated, block: B:444:0x0a68  */
    /* JADX WARN: Code duplicated, block: B:447:0x0a7e  */
    /* JADX WARN: Code duplicated, block: B:449:0x0a81  */
    /* JADX WARN: Code duplicated, block: B:455:0x0aa4  */
    /* JADX WARN: Code duplicated, block: B:457:0x0aa8  */
    /* JADX WARN: Code duplicated, block: B:461:0x0ab5  */
    /* JADX WARN: Code duplicated, block: B:463:0x0abb  */
    /* JADX WARN: Code duplicated, block: B:465:0x0acd  */
    /* JADX WARN: Code duplicated, block: B:466:0x0ad2  */
    /* JADX WARN: Code duplicated, block: B:467:0x0ad4  */
    /* JADX WARN: Code duplicated, block: B:469:0x0ae1  */
    /* JADX WARN: Code duplicated, block: B:471:0x0ae6  */
    /* JADX WARN: Code duplicated, block: B:472:0x0af5  */
    /* JADX WARN: Code duplicated, block: B:476:0x0b10  */
    /* JADX WARN: Code duplicated, block: B:478:0x0b46  */
    /* JADX WARN: Code duplicated, block: B:481:0x0b4f  */
    /* JADX WARN: Code duplicated, block: B:482:0x0b6b  */
    /* JADX WARN: Code duplicated, block: B:484:0x0b6f A[ADDED_TO_REGION] */
    /* JADX WARN: Code duplicated, block: B:487:0x0b8a  */
    /* JADX WARN: Code duplicated, block: B:488:0x0b8d  */
    /* JADX WARN: Code duplicated, block: B:491:0x0bad  */
    /* JADX WARN: Code duplicated, block: B:493:0x0bc8  */
    /* JADX WARN: Code duplicated, block: B:494:0x0bcb  */
    /* JADX WARN: Code duplicated, block: B:498:0x0be9 A[ADDED_TO_REGION] */
    /* JADX WARN: Code duplicated, block: B:501:0x0bfe  */
    /* JADX WARN: Code duplicated, block: B:502:0x0c01  */
    /* JADX WARN: Code duplicated, block: B:505:0x0c2b  */
    /* JADX WARN: Code duplicated, block: B:508:0x0c36  */
    /* JADX WARN: Code duplicated, block: B:511:0x0c40  */
    /* JADX WARN: Code duplicated, block: B:513:0x0c4a  */
    /* JADX WARN: Code duplicated, block: B:514:0x0c4d  */
    /* JADX WARN: Code duplicated, block: B:517:0x0c7e  */
    /* JADX WARN: Code duplicated, block: B:520:0x0c87  */
    /* JADX WARN: Code duplicated, block: B:525:0x0c96  */
    /* JADX WARN: Code duplicated, block: B:527:0x0c9a  */
    /* JADX WARN: Code duplicated, block: B:528:0x0cba  */
    /* JADX WARN: Code duplicated, block: B:532:0x0cd0  */
    /* JADX WARN: Code duplicated, block: B:535:0x0cd8  */
    /* JADX WARN: Code duplicated, block: B:545:0x0ceb  */
    /* JADX WARN: Code duplicated, block: B:547:0x0d03  */
    /* JADX WARN: Code duplicated, block: B:549:0x0d0e  */
    /* JADX WARN: Code duplicated, block: B:551:0x0d1a  */
    /* JADX WARN: Code duplicated, block: B:552:0x0d21  */
    /* JADX WARN: Code duplicated, block: B:556:0x0d35  */
    /* JADX WARN: Code duplicated, block: B:558:0x0d3d  */
    /* JADX WARN: Code duplicated, block: B:560:0x0d48  */
    /* JADX WARN: Code duplicated, block: B:564:0x0d59  */
    /* JADX WARN: Code duplicated, block: B:566:? A[RETURN, SYNTHETIC] */
    @Override // android.graphics.drawable.Drawable
    public void draw(Canvas canvas) {
        float f;
        int iSave;
        int i;
        float f2;
        float f3;
        float f4;
        float f5;
        float f6;
        float fDp;
        float f7;
        float fDp2;
        float fDp3;
        float fDp4;
        float f8;
        float f9;
        float f10;
        float f11;
        float f12;
        float f13;
        float f14;
        float f15;
        float f16;
        float f17;
        float f18;
        int i2;
        boolean z;
        int i3;
        float fMin;
        float fMax;
        float f19;
        float f20;
        int i4;
        Path[] pathArr;
        Path[] pathArr2;
        Path[] pathArr3;
        Path[] pathArr4;
        Drawable drawable;
        int i5;
        Drawable drawable2;
        Drawable drawable3;
        int i6;
        android.graphics.Rect rect;
        Drawable drawable4;
        int i7;
        int i8;
        int i9;
        float f21;
        int i10;
        int i11;
        int i12;
        int i13;
        int i14;
        float f22;
        float f23;
        int i15;
        int i16;
        int i17;
        float f24;
        int i18;
        float f25;
        int i19;
        float f26;
        int i20;
        float fMin2;
        float f27;
        int iDp;
        int iDp2;
        int i21;
        float f28;
        float f29;
        float f30;
        long j;
        int i22;
        float f31;
        float f32;
        float f33;
        float f34;
        int i23;
        float f35;
        float f36;
        int i24;
        Path path;
        Path path2;
        Path path3;
        int i25;
        Path path4;
        Path path5;
        int i26;
        int i27;
        int i28;
        int i29;
        float f37;
        float fMin3;
        float f38;
        int iMin;
        float f39;
        float fCenterX;
        float fDp5;
        float f40;
        float f41;
        float f42;
        float fCenterX2;
        int iCenterY;
        int iCenterY2;
        float f43;
        int i30;
        float f44;
        int i31;
        int i32;
        float f45;
        int i33;
        int i34;
        Canvas canvas2 = canvas;
        android.graphics.Rect bounds = getBounds();
        Theme.MessageDrawable messageDrawable = this.messageDrawable;
        if (messageDrawable != null && messageDrawable.hasGradient() && !this.hasOverlayImage) {
            Shader gradientShader = this.messageDrawable.getGradientShader();
            this.paint.setShader(gradientShader);
            this.paint2.setShader(gradientShader);
            this.paint3.setShader(gradientShader);
        } else if (this.gradientDrawable != null && !this.hasOverlayImage) {
            this.gradientMatrix.reset();
            this.gradientMatrix.setTranslate(0.0f, bounds.top);
            this.gradientDrawable.setLocalMatrix(this.gradientMatrix);
            this.paint.setShader(this.gradientDrawable);
            this.paint2.setShader(this.gradientDrawable);
            this.paint3.setShader(this.gradientDrawable);
        } else {
            this.paint.setShader(null);
            this.paint2.setShader(null);
            this.paint3.setShader(null);
        }
        int iCenterX = bounds.centerX();
        int iCenterY3 = bounds.centerY();
        int i35 = this.nextIcon;
        if (i35 == 4) {
            int i36 = this.currentIcon;
            if (i36 == 3 || i36 == 14) {
                f = 0.0f;
                i = 0;
            } else {
                iSave = canvas2.save();
                float f46 = 1.0f - this.transitionProgress;
                f = 0.0f;
                canvas2.scale(f46, f46, iCenterX, iCenterY3);
                i = iSave;
            }
        } else {
            f = 0.0f;
            if ((i35 == 6 || i35 == 10) && this.currentIcon == 4) {
                iSave = canvas2.save();
                float f47 = this.transitionProgress;
                canvas2.scale(f47, f47, iCenterX, iCenterY3);
                i = iSave;
            } else {
                i = 0;
            }
        }
        AndroidUtilities.dp(3.0f);
        float interpolation = 90.0f;
        if (this.currentIcon == 2 || this.nextIcon == 2) {
            applyShaderMatrix(false);
            if (this.drawProgressCircle) {
                float f48 = iCenterY3;
                float fDp6 = f48 - (AndroidUtilities.dp(9.0f) * this.scale);
                float fDp7 = (AndroidUtilities.dp(9.0f) * this.scale) + f48;
                float fDp8 = (AndroidUtilities.dp(12.0f) * this.scale) + f48;
                int i37 = this.currentIcon;
                if ((i37 == 3 || i37 == 14) && this.nextIcon == 2) {
                    this.paint.setAlpha((int) (Math.min(1.0f, this.transitionProgress / 0.5f) * 255.0f));
                    f6 = this.transitionProgress;
                    fDp = (AndroidUtilities.dp(12.0f) * this.scale) + f48;
                    f7 = 1.0f;
                } else {
                    int i38 = this.nextIcon;
                    if (i38 != 3 && i38 != 14 && i38 != 2) {
                        this.paint.setAlpha((int) (Math.min(1.0f, this.savedTransitionProgress / 0.5f) * 255.0f * (1.0f - this.transitionProgress)));
                        f18 = this.savedTransitionProgress;
                    } else {
                        this.paint.setAlpha(255);
                        f18 = this.transitionProgress;
                    }
                    f6 = f18;
                    f7 = 1.0f;
                    fDp = f48 + (AndroidUtilities.dp(1.0f) * this.scale);
                }
                if (this.animatingTransition) {
                    int i39 = this.nextIcon;
                    if (i39 == 2 || f6 <= 0.5f) {
                        if (i39 == 2) {
                            f14 = 1.0f - f6;
                        } else {
                            f14 = f6 / 0.5f;
                            f6 = 1.0f - f14;
                        }
                        fDp6 += (fDp - fDp6) * f14;
                        fDp7 += (fDp8 - fDp7) * f14;
                        float f49 = iCenterX;
                        fDp2 = f49 - ((AndroidUtilities.dp(8.0f) * f6) * this.scale);
                        fDp3 = f49 + (AndroidUtilities.dp(8.0f) * f6 * this.scale);
                        fDp4 = AndroidUtilities.dp(8.0f) * f6;
                        f8 = this.scale;
                    } else {
                        float fDp9 = AndroidUtilities.dp(13.0f);
                        float f50 = this.scale;
                        float fDp10 = (fDp9 * f50 * f50) + (this.isMini ? AndroidUtilities.dp(2.0f) : 0);
                        float f51 = f6 - 0.5f;
                        float f52 = f51 / 0.5f;
                        if (f51 > 0.2f) {
                            f16 = (f51 - 0.2f) / 0.3f;
                            f15 = f7;
                        } else {
                            f15 = f51 / 0.2f;
                            f16 = f;
                        }
                        float f53 = iCenterX;
                        float f54 = f53 - fDp10;
                        float f55 = fDp10 / 2.0f;
                        this.rect.set(f54, fDp8 - f55, f53, f55 + fDp8);
                        float f56 = f16 * 100.0f;
                        canvas2.drawArc(this.rect, f56, (f52 * 104.0f) - f56, false, this.paint);
                        float f57 = fDp + ((fDp8 - fDp) * f15);
                        if (f16 > f) {
                            float f58 = this.nextIcon == 14 ? f : (-45.0f) * (f7 - f16);
                            float fDp11 = AndroidUtilities.dp(7.0f) * f16 * this.scale;
                            int iMin2 = (int) (f16 * 255.0f);
                            int i40 = this.nextIcon;
                            if (i40 != 3 && i40 != 14 && i40 != 2) {
                                float f59 = f7;
                                iMin2 = (int) (iMin2 * (f59 - Math.min(f59, this.transitionProgress / 0.5f)));
                            }
                            int i41 = iMin2;
                            if (f58 != f) {
                                canvas2.save();
                                f17 = f48;
                                canvas2.rotate(f58, f53, f17);
                            } else {
                                f17 = f48;
                            }
                            if (i41 != 0) {
                                this.paint.setAlpha(i41);
                                if (this.nextIcon == 14) {
                                    this.paint3.setAlpha(i41);
                                    this.rect.set(iCenterX - AndroidUtilities.dp(3.5f), iCenterY3 - AndroidUtilities.dp(3.5f), AndroidUtilities.dp(3.5f) + iCenterX, AndroidUtilities.dp(3.5f) + iCenterY3);
                                    canvas2.drawRoundRect(this.rect, AndroidUtilities.dp(2.0f), AndroidUtilities.dp(2.0f), this.paint3);
                                    this.paint.setAlpha((int) (i41 * 0.15f));
                                    int iDp3 = AndroidUtilities.dp(this.isMini ? 2.0f : 4.0f);
                                    this.rect.set(bounds.left + iDp3, bounds.top + iDp3, bounds.right - iDp3, bounds.bottom - iDp3);
                                    canvas2.drawArc(this.rect, 0.0f, 360.0f, false, this.paint);
                                    this.paint.setAlpha(i41);
                                } else {
                                    float f60 = f53 - fDp11;
                                    float f61 = f17 - fDp11;
                                    float f62 = f53 + fDp11;
                                    float f63 = f17 + fDp11;
                                    canvas.drawLine(f60, f61, f62, f63, this.paint);
                                    canvas.drawLine(f62, f61, f60, f63, this.paint);
                                }
                            }
                            if (f58 != f) {
                                canvas.restore();
                            }
                        }
                        fDp7 = fDp8;
                        fDp3 = f53;
                        fDp2 = fDp3;
                        fDp6 = f57;
                    }
                    f9 = fDp3;
                    f10 = fDp7;
                    f11 = fDp8;
                    f12 = fDp2;
                    if (fDp6 != f10) {
                        float f64 = iCenterX;
                        canvas.drawLine(f64, fDp6, f64, f10, this.paint);
                    }
                    f13 = iCenterX;
                    if (f12 != f13) {
                        canvas2 = canvas;
                        canvas2.drawLine(f12, f11, f13, f10, this.paint);
                        canvas2.drawLine(f9, f11, f13, f10, this.paint);
                    } else {
                        canvas2 = canvas;
                    }
                } else {
                    float f65 = iCenterX;
                    fDp2 = f65 - (AndroidUtilities.dp(8.0f) * this.scale);
                    fDp3 = f65 + (AndroidUtilities.dp(8.0f) * this.scale);
                    fDp4 = AndroidUtilities.dp(8.0f);
                    f8 = this.scale;
                }
                fDp8 = fDp7 - (fDp4 * f8);
                f9 = fDp3;
                f10 = fDp7;
                f11 = fDp8;
                f12 = fDp2;
                if (fDp6 != f10) {
                    float f66 = iCenterX;
                    canvas.drawLine(f66, fDp6, f66, f10, this.paint);
                }
                f13 = iCenterX;
                if (f12 != f13) {
                    canvas2 = canvas;
                    canvas2.drawLine(f12, f11, f13, f10, this.paint);
                    canvas2.drawLine(f9, f11, f13, f10, this.paint);
                } else {
                    canvas2 = canvas;
                }
            } else {
                boolean z2 = this.currentIcon == 14 || this.nextIcon == 14;
                if (this.nextIcon == 2) {
                    f2 = 1.0f;
                    f3 = 1.0f - this.transitionProgress;
                } else {
                    f2 = 1.0f;
                    f3 = this.transitionProgress;
                }
                float f67 = this.scale * this.downloadIconScale;
                float fDp12 = AndroidUtilities.dp(8.0f) * f67;
                float fDp13 = AndroidUtilities.dp(f2) * f67;
                float fDp14 = AndroidUtilities.dp(7.0f) * f67;
                float fDp15 = AndroidUtilities.dp(9.0f) * f67;
                float fDp16 = AndroidUtilities.dp(2.0f) * f67;
                float fDp17 = AndroidUtilities.dp(3.5f);
                if (z2) {
                    float f68 = 1.0f - f3;
                    if (f68 > f) {
                        canvas2.save();
                        float f69 = iCenterX;
                        float f70 = iCenterY3;
                        canvas2.scale(f68, f68, f69, f70);
                        this.paint.setAlpha((int) (f68 * 255.0f * this.overrideAlpha));
                        float f71 = f70 + fDp15;
                        float f72 = f71 - fDp12;
                        canvas2.drawLine(f69, f70 - fDp15, f69, f71, this.paint);
                        canvas.drawLine(f69 - fDp12, f72, f69, f71, this.paint);
                        canvas2 = canvas;
                        canvas2.drawLine(f69 + fDp12, f72, f69, f71, this.paint);
                        canvas2.restore();
                    }
                    if (f3 > f) {
                        canvas2.save();
                        float f73 = iCenterX;
                        float f74 = iCenterY3;
                        canvas2.scale(f3, f3, f73, f74);
                        this.paint3.setAlpha((int) (f3 * 255.0f * this.overrideAlpha));
                        this.rect.set(f73 - fDp17, f74 - fDp17, f73 + fDp17, f74 + fDp17);
                        canvas2.drawRoundRect(this.rect, AndroidUtilities.dp(2.0f), AndroidUtilities.dp(2.0f), this.paint3);
                        canvas2.restore();
                    }
                } else {
                    int i42 = this.currentIcon;
                    if ((i42 == 3 || this.nextIcon == 3) && this.animatingTransition) {
                        canvas2.save();
                        float f75 = iCenterX;
                        float f76 = iCenterY3;
                        canvas2.rotate(f3 * 90.0f, f75, f76);
                        float f77 = 1.0f - f3;
                        if (f77 > f) {
                            this.paint.setAlpha((int) (f77 * 255.0f));
                            float f78 = f77 * fDp15;
                            canvas2.drawLine(f75, f76 - f78, f75, f78 + f76, this.paint);
                            f4 = f75;
                        } else {
                            f4 = f75;
                        }
                        this.paint.setAlpha(255);
                        float f79 = (f76 + fDp13) - (fDp12 * f3);
                        float f80 = (f76 + fDp15) - (fDp16 * f3);
                        float f81 = fDp13 * f3;
                        float f82 = fDp14 * f3;
                        canvas.drawLine((f4 - fDp12) + f81, f79, f4 + f82, f80, this.paint);
                        canvas2 = canvas;
                        canvas2.drawLine((f4 + fDp12) - f81, f79, f4 - f82, f80, this.paint);
                        canvas2.restore();
                    } else {
                        if (this.animatingTransition) {
                            f5 = f3;
                        } else {
                            f5 = i42 == 2 ? f : 1.0f;
                        }
                        float f83 = 1.0f - f5;
                        int i43 = (int) (f83 * 255.0f);
                        if (i43 > 0) {
                            canvas2.save();
                            float f84 = iCenterX;
                            float f85 = iCenterY3;
                            canvas2.scale(f83, f83, f84, f85);
                            this.paint.setAlpha(i43);
                            float f86 = f85 + fDp15;
                            float f87 = f86 - fDp12;
                            canvas2.drawLine(f84, f85 - fDp15, f84, f86, this.paint);
                            canvas.drawLine(f84 - fDp12, f87, f84, f86, this.paint);
                            canvas2 = canvas;
                            canvas2.drawLine(f84 + fDp12, f87, f84, f86, this.paint);
                            canvas2.restore();
                        }
                    }
                }
            }
        }
        int i44 = this.currentIcon;
        float f88 = 360.0f;
        if (i44 != 3 && i44 != 14) {
            if (i44 == 4 && ((i34 = this.nextIcon) == 14 || i34 == 3)) {
                z = false;
                i2 = 15;
            } else if (i44 == 10 || this.nextIcon == 10 || i44 == 13) {
                int i45 = this.nextIcon;
                int i46 = (i45 == 4 || i45 == 6) ? (int) ((1.0f - this.transitionProgress) * 255.0f) : 255;
                if (i46 != 0) {
                    applyShaderMatrix(false);
                    this.paint.setAlpha((int) (i46 * this.overrideAlpha));
                    if (this.drawProgressCircle) {
                        float fMax2 = Math.max(4.0f, this.animatedDownloadProgress * 360.0f);
                        int iDp4 = AndroidUtilities.dp(this.isMini ? 2.0f : 4.0f);
                        this.rect.set(bounds.left + iDp4, bounds.top + iDp4, bounds.right - iDp4, bounds.bottom - iDp4);
                        i2 = 15;
                        canvas2.drawArc(this.rect, this.downloadRadOffset, fMax2, false, this.paint);
                    } else {
                        i2 = 15;
                    }
                } else {
                    i2 = 15;
                }
            } else {
                i2 = 15;
            }
            i3 = this.currentIcon;
            if (i3 == this.nextIcon) {
                f20 = 1.0f;
                f19 = 1.0f;
            } else {
                if (i3 != 4 || i3 == 3 || i3 == 14) {
                    fMin = this.transitionProgress;
                    fMax = 1.0f - fMin;
                } else {
                    fMin = Math.min(1.0f, this.transitionProgress / 0.5f);
                    fMax = Math.max(f, 1.0f - (this.transitionProgress / 0.5f));
                }
                f19 = fMax;
                f20 = fMin;
            }
            i4 = this.nextIcon;
            if (i4 == i2) {
                pathArr = Theme.chat_updatePath;
            } else {
                if (this.currentIcon == i2) {
                    pathArr2 = Theme.chat_updatePath;
                    pathArr = null;
                } else {
                    pathArr = null;
                }
                if (i4 == 5) {
                    pathArr = Theme.chat_filePath;
                } else if (this.currentIcon == 5) {
                    pathArr2 = Theme.chat_filePath;
                }
                pathArr3 = pathArr;
                pathArr4 = pathArr2;
                if (i4 == 7) {
                    drawable2 = Theme.chat_flameIcon;
                    drawable = null;
                    i5 = 8;
                } else {
                    if (this.currentIcon == 7) {
                        drawable = Theme.chat_flameIcon;
                    } else {
                        drawable = null;
                    }
                    i5 = 8;
                    drawable2 = null;
                }
                if (i4 == i5) {
                    drawable2 = Theme.chat_gifIcon;
                } else if (this.currentIcon == i5) {
                    drawable = Theme.chat_gifIcon;
                }
                drawable3 = drawable;
                if (this.currentIcon != 9 || i4 == 9) {
                    applyShaderMatrix(false);
                    Paint paint = this.paint;
                    if (this.currentIcon == this.nextIcon) {
                        i6 = 255;
                    } else {
                        i6 = (int) (this.transitionProgress * 255.0f);
                    }
                    paint.setAlpha(i6);
                    int iDp5 = AndroidUtilities.dp(7.0f) + iCenterY3;
                    int iDp6 = iCenterX - AndroidUtilities.dp(3.0f);
                    if (this.currentIcon != this.nextIcon) {
                        canvas2.save();
                        float f89 = this.transitionProgress;
                        canvas2.scale(f89, f89, iCenterX, iCenterY3);
                    }
                    float f90 = iDp6;
                    float f91 = iDp5;
                    rect = bounds;
                    drawable4 = drawable2;
                    canvas2.drawLine(iDp6 - AndroidUtilities.dp(6.0f), iDp5 - AndroidUtilities.dp(6.0f), f90, f91, this.paint);
                    canvas2 = canvas;
                    canvas2.drawLine(f90, f91, iDp6 + AndroidUtilities.dp(12.0f), iDp5 - AndroidUtilities.dp(12.0f), this.paint);
                    if (this.currentIcon != this.nextIcon) {
                        canvas2.restore();
                    }
                } else {
                    rect = bounds;
                    drawable4 = drawable2;
                }
                if (this.currentIcon != 12 || this.nextIcon == 12) {
                    applyShaderMatrix(false);
                    i7 = this.currentIcon;
                    i8 = this.nextIcon;
                    if (i7 == i8) {
                        i9 = 13;
                        f21 = 1.0f;
                    } else {
                        i9 = 13;
                        if (i8 == 13) {
                            f21 = this.transitionProgress;
                        } else {
                            f21 = 1.0f - this.transitionProgress;
                        }
                    }
                    Paint paint2 = this.paint;
                    if (i7 == i8) {
                        i10 = 255;
                    } else {
                        i10 = (int) (f21 * 255.0f);
                    }
                    paint2.setAlpha(i10);
                    AndroidUtilities.dp(7.0f);
                    AndroidUtilities.dp(3.0f);
                    if (this.currentIcon != this.nextIcon) {
                        canvas2.save();
                        canvas2.scale(f21, f21, iCenterX, iCenterY3);
                    }
                    float fDp18 = AndroidUtilities.dp(7.0f) * this.scale;
                    float f92 = iCenterX;
                    float f93 = f92 - fDp18;
                    float f94 = iCenterY3;
                    float f95 = f94 - fDp18;
                    float f96 = f92 + fDp18;
                    float f97 = fDp18 + f94;
                    i11 = i;
                    i12 = i9;
                    canvas2.drawLine(f93, f95, f96, f97, this.paint);
                    canvas2 = canvas;
                    canvas2.drawLine(f96, f95, f93, f97, this.paint);
                    if (this.currentIcon != this.nextIcon) {
                        canvas2.restore();
                    }
                } else {
                    i11 = i;
                    i12 = 13;
                }
                if (this.currentIcon != i12 || this.nextIcon == i12) {
                    applyShaderMatrix(false);
                    i13 = this.currentIcon;
                    i14 = this.nextIcon;
                    if (i13 == i14) {
                        f22 = 1.0f;
                    } else if (i14 == i12) {
                        f22 = this.transitionProgress;
                    } else {
                        f22 = 1.0f - this.transitionProgress;
                    }
                    this.textPaint.setAlpha((int) (f22 * 255.0f));
                    int iDp7 = AndroidUtilities.dp(5.0f) + iCenterY3;
                    int i47 = iCenterX - (this.percentStringWidth / 2);
                    f23 = 5.0f;
                    if (this.currentIcon != this.nextIcon) {
                        canvas2.save();
                        canvas2.scale(f22, f22, iCenterX, iCenterY3);
                    }
                    i15 = (int) (this.animatedDownloadProgress * 100.0f);
                    if (this.percentString != null || i15 != this.lastPercent) {
                        this.lastPercent = i15;
                        String str = String.format("%d%%", Integer.valueOf(i15));
                        this.percentString = str;
                        this.percentStringWidth = (int) Math.ceil(this.textPaint.measureText(str));
                    }
                    canvas2.drawText(this.percentString, i47, iDp7, this.textPaint);
                    if (this.currentIcon != this.nextIcon) {
                        canvas2.restore();
                    }
                } else {
                    f23 = 5.0f;
                }
                i16 = this.currentIcon;
                i17 = 1;
                if (i16 != 0 || i16 == 1 || (i28 = this.nextIcon) == 0 || i28 == 1) {
                    if (i16 == 0 || this.nextIcon != 1) {
                        if (i16 == 1) {
                            if (this.nextIcon != 0) {
                                i17 = 1;
                            } else if (this.animatingTransition) {
                                if (this.nextIcon == 0) {
                                    f24 = 1.0f - this.transitionProgress;
                                } else {
                                    f24 = this.transitionProgress;
                                }
                                i17 = 1;
                            } else {
                                i17 = 1;
                                if (this.nextIcon == 1) {
                                    f24 = 1.0f;
                                } else {
                                    f24 = 0.0f;
                                }
                            }
                        }
                        if (i16 == i17) {
                            f24 = 1.0f;
                        } else {
                            f24 = 0.0f;
                        }
                    } else if (this.animatingTransition) {
                        if (this.nextIcon == 0) {
                            f24 = 1.0f - this.transitionProgress;
                        } else {
                            f24 = this.transitionProgress;
                        }
                        i17 = 1;
                    } else {
                        i17 = 1;
                        if (this.nextIcon == 1) {
                            f24 = 1.0f;
                        } else {
                            f24 = 0.0f;
                        }
                    }
                    i18 = this.nextIcon;
                    if ((i18 != 0 || i18 == i17) && (i16 == 0 || i16 == i17)) {
                        this.paint2.setAlpha(255);
                    } else if (i18 == 4) {
                        this.paint2.setAlpha((int) ((1.0f - this.transitionProgress) * 255.0f));
                    } else {
                        this.paint2.setAlpha(i16 == i18 ? 255 : (int) (this.transitionProgress * 255.0f));
                    }
                    applyShaderMatrix(true);
                    canvas2.save();
                    canvas2.translate(rect.centerX() + (AndroidUtilities.dp(1.0f) * (1.0f - f24)), rect.centerY());
                    f25 = f24 * 500.0f;
                    i19 = this.currentIcon;
                    if (i19 == 1) {
                        f26 = 90.0f;
                    } else {
                        f26 = 0.0f;
                    }
                    if (i19 == 0 || this.nextIcon != 1) {
                        if (i19 == 1 && this.nextIcon == 0) {
                            if (f25 < 100.0f) {
                                interpolation = CubicBezierInterpolator.EASE_BOTH.getInterpolation(f25 / 100.0f) * (-5.0f);
                            } else if (f25 < 484.0f) {
                                interpolation = (CubicBezierInterpolator.EASE_BOTH.getInterpolation((f25 - 100.0f) / 384.0f) * 95.0f) - 5.0f;
                            }
                        }
                        canvas2.rotate(f26);
                        i20 = this.currentIcon;
                        if ((i20 == 0 && i20 != 1) || i20 == 4) {
                            canvas2.scale(f20, f20);
                        }
                        Theme.playPauseAnimator.draw(canvas2, this.paint2, f25);
                        canvas2.scale(1.0f, -1.0f);
                        Theme.playPauseAnimator.draw(canvas2, this.paint2, f25);
                        canvas2.restore();
                    } else {
                        if (f25 < 384.0f) {
                            interpolation = CubicBezierInterpolator.EASE_BOTH.getInterpolation(f25 / 384.0f) * 95.0f;
                        } else if (f25 < 484.0f) {
                            interpolation = 95.0f - (CubicBezierInterpolator.EASE_BOTH.getInterpolation((f25 - 384.0f) / 100.0f) * f23);
                        }
                        f25 += 100.0f;
                    }
                    f26 = interpolation;
                    canvas2.rotate(f26);
                    i20 = this.currentIcon;
                    if (i20 == 0) {
                        canvas2.scale(f20, f20);
                    } else {
                        canvas2.scale(f20, f20);
                    }
                    Theme.playPauseAnimator.draw(canvas2, this.paint2, f25);
                    canvas2.scale(1.0f, -1.0f);
                    Theme.playPauseAnimator.draw(canvas2, this.paint2, f25);
                    canvas2.restore();
                }
                if (this.currentIcon != 6 || this.nextIcon == 6) {
                    applyShaderMatrix(false);
                    if (this.currentIcon != 6) {
                        f28 = this.transitionProgress;
                        if (f28 > 0.5f) {
                            f30 = (f28 - 0.5f) / 0.5f;
                            fMin2 = 1.0f - Math.min(1.0f, f30 / 0.5f);
                            if (f30 > 0.5f) {
                                f29 = (f30 - 0.5f) / 0.5f;
                            } else {
                                f29 = 0.0f;
                            }
                        } else {
                            f29 = 0.0f;
                            fMin2 = 1.0f;
                        }
                        this.paint.setAlpha(255);
                        f27 = f29;
                    } else {
                        if (this.nextIcon != 6) {
                            this.paint.setAlpha((int) ((1.0f - this.transitionProgress) * 255.0f));
                        } else {
                            this.paint.setAlpha(255);
                        }
                        fMin2 = 0.0f;
                        f27 = 1.0f;
                    }
                    iDp = AndroidUtilities.dp(7.0f) + iCenterY3;
                    iDp2 = iCenterX - AndroidUtilities.dp(3.0f);
                    if (fMin2 < 1.0f) {
                        i21 = iDp2;
                        canvas.drawLine(iDp2 - AndroidUtilities.dp(6.0f), iDp - AndroidUtilities.dp(6.0f), iDp2 - (AndroidUtilities.dp(6.0f) * fMin2), iDp - (AndroidUtilities.dp(6.0f) * fMin2), this.paint);
                    } else {
                        i21 = iDp2;
                    }
                    if (f27 > 0.0f) {
                        float f98 = i21;
                        float f99 = iDp;
                        canvas2 = canvas;
                        canvas2.drawLine(f98, f99, f98 + (AndroidUtilities.dp(12.0f) * f27), f99 - (AndroidUtilities.dp(12.0f) * f27), this.paint);
                    } else {
                        canvas2 = canvas;
                    }
                } else {
                    f20 = f20;
                }
                if (drawable3 != null && drawable3 != drawable4) {
                    int intrinsicWidth = (int) (drawable3.getIntrinsicWidth() * f19);
                    int intrinsicHeight = (int) (drawable3.getIntrinsicHeight() * f19);
                    drawable3.setColorFilter(this.colorFilter);
                    if (this.currentIcon == this.nextIcon) {
                        i27 = 255;
                    } else {
                        i27 = (int) ((1.0f - this.transitionProgress) * 255.0f);
                    }
                    drawable3.setAlpha(i27);
                    int i48 = intrinsicWidth / 2;
                    int i49 = intrinsicHeight / 2;
                    drawable3.setBounds(iCenterX - i48, iCenterY3 - i49, i48 + iCenterX, i49 + iCenterY3);
                    drawable3.draw(canvas2);
                }
                if (drawable4 != null) {
                    int intrinsicWidth2 = (int) (drawable4.getIntrinsicWidth() * f20);
                    int intrinsicHeight2 = (int) (drawable4.getIntrinsicHeight() * f20);
                    drawable4.setColorFilter(this.colorFilter);
                    if (this.currentIcon == this.nextIcon) {
                        i26 = 255;
                    } else {
                        i26 = (int) (this.transitionProgress * 255.0f);
                    }
                    drawable4.setAlpha(i26);
                    int i50 = intrinsicWidth2 / 2;
                    int i51 = intrinsicHeight2 / 2;
                    drawable4.setBounds(iCenterX - i50, iCenterY3 - i51, i50 + iCenterX, i51 + iCenterY3);
                    drawable4.draw(canvas2);
                }
                if (pathArr4 != null && pathArr4 != pathArr3) {
                    int iDp8 = AndroidUtilities.dp(24.0f);
                    this.paint2.setStyle(Paint.Style.FILL_AND_STROKE);
                    Paint paint3 = this.paint2;
                    if (this.currentIcon == this.nextIcon) {
                        i25 = 255;
                    } else {
                        i25 = (int) ((1.0f - this.transitionProgress) * 255.0f);
                    }
                    paint3.setAlpha(i25);
                    applyShaderMatrix(true);
                    canvas2.save();
                    canvas2.translate(iCenterX, iCenterY3);
                    canvas2.scale(f19, f19);
                    float f100 = (-iDp8) / 2;
                    canvas2.translate(f100, f100);
                    path4 = pathArr4[0];
                    if (path4 != null) {
                        canvas2.drawPath(path4, this.paint2);
                    }
                    path5 = pathArr4[1];
                    if (path5 != null) {
                        canvas2.drawPath(path5, this.backPaint);
                    }
                    canvas2.restore();
                }
                if (pathArr3 != null) {
                    int iDp9 = AndroidUtilities.dp(24.0f);
                    if (this.currentIcon == this.nextIcon) {
                        i24 = 255;
                    } else {
                        i24 = (int) (this.transitionProgress * 255.0f);
                    }
                    this.paint2.setStyle(Paint.Style.FILL_AND_STROKE);
                    this.paint2.setAlpha(i24);
                    applyShaderMatrix(true);
                    canvas2.save();
                    canvas2.translate(iCenterX, iCenterY3);
                    float f101 = f20;
                    canvas2.scale(f101, f101);
                    float f102 = (-iDp9) / 2;
                    canvas2.translate(f102, f102);
                    path = pathArr3[0];
                    if (path != null) {
                        canvas2.drawPath(path, this.paint2);
                    }
                    if (pathArr3.length >= 3 && (path3 = pathArr3[2]) != null) {
                        canvas2.drawPath(path3, this.paint);
                    }
                    path2 = pathArr3[1];
                    if (path2 != null) {
                        if (i24 != 255) {
                            int alpha = this.backPaint.getAlpha();
                            this.backPaint.setAlpha((int) (alpha * (i24 / 255.0f)));
                            canvas2.drawPath(pathArr3[1], this.backPaint);
                            this.backPaint.setAlpha(alpha);
                        } else {
                            canvas2.drawPath(path2, this.backPaint);
                        }
                    }
                    canvas2.restore();
                }
                long jCurrentTimeMillis = System.currentTimeMillis();
                j = jCurrentTimeMillis - this.lastAnimationTime;
                if (j > 17) {
                    j = 17;
                }
                this.lastAnimationTime = jCurrentTimeMillis;
                i22 = this.currentIcon;
                if (i22 != 3 || i22 == 14 || ((i22 == 4 && this.nextIcon == 14) || i22 == 10 || i22 == 13)) {
                    float f103 = this.downloadRadOffset + ((360 * j) / 2500.0f);
                    this.downloadRadOffset = f103;
                    this.downloadRadOffset = getCircleValue(f103);
                    if (this.nextIcon != 2) {
                        f31 = this.downloadProgress;
                        f32 = this.downloadProgressAnimationStart;
                        f33 = f31 - f32;
                        if (f33 > 0.0f) {
                            f34 = this.downloadProgressTime + j;
                            this.downloadProgressTime = f34;
                            if (f34 >= 200.0f) {
                                this.animatedDownloadProgress = f31;
                                this.downloadProgressAnimationStart = f31;
                                this.downloadProgressTime = 0.0f;
                            } else {
                                this.animatedDownloadProgress = f32 + (f33 * this.interpolator.getInterpolation(f34 / 200.0f));
                            }
                        }
                    }
                    invalidateSelf();
                }
                if (this.animatingTransition) {
                    f35 = this.transitionProgress;
                    if (f35 < 1.0f) {
                        f36 = f35 + (j / this.transitionAnimationTime);
                        this.transitionProgress = f36;
                        if (f36 >= 1.0f) {
                            this.currentIcon = this.nextIcon;
                            this.transitionProgress = 1.0f;
                            this.animatingTransition = false;
                        }
                        invalidateSelf();
                    }
                }
                i23 = i11;
                if (i23 >= 1) {
                    canvas2.restoreToCount(i23);
                }
            }
            pathArr2 = null;
            if (i4 == 5) {
                pathArr = Theme.chat_filePath;
            } else if (this.currentIcon == 5) {
                pathArr2 = Theme.chat_filePath;
            }
            pathArr3 = pathArr;
            pathArr4 = pathArr2;
            if (i4 == 7) {
                drawable2 = Theme.chat_flameIcon;
                drawable = null;
                i5 = 8;
            } else {
                if (this.currentIcon == 7) {
                    drawable = Theme.chat_flameIcon;
                } else {
                    drawable = null;
                }
                i5 = 8;
                drawable2 = null;
            }
            if (i4 == i5) {
                drawable2 = Theme.chat_gifIcon;
            } else if (this.currentIcon == i5) {
                drawable = Theme.chat_gifIcon;
            }
            drawable3 = drawable;
            if (this.currentIcon != 9) {
                applyShaderMatrix(false);
                Paint paint4 = this.paint;
                if (this.currentIcon == this.nextIcon) {
                    i6 = 255;
                } else {
                    i6 = (int) (this.transitionProgress * 255.0f);
                }
                paint4.setAlpha(i6);
                int iDp10 = AndroidUtilities.dp(7.0f) + iCenterY3;
                int iDp11 = iCenterX - AndroidUtilities.dp(3.0f);
                if (this.currentIcon != this.nextIcon) {
                    canvas2.save();
                    float f810 = this.transitionProgress;
                    canvas2.scale(f810, f810, iCenterX, iCenterY3);
                }
                float f910 = iDp11;
                float f911 = iDp10;
                rect = bounds;
                drawable4 = drawable2;
                canvas2.drawLine(iDp11 - AndroidUtilities.dp(6.0f), iDp10 - AndroidUtilities.dp(6.0f), f910, f911, this.paint);
                canvas2 = canvas;
                canvas2.drawLine(f910, f911, iDp11 + AndroidUtilities.dp(12.0f), iDp10 - AndroidUtilities.dp(12.0f), this.paint);
                if (this.currentIcon != this.nextIcon) {
                    canvas2.restore();
                }
            } else {
                applyShaderMatrix(false);
                Paint paint5 = this.paint;
                if (this.currentIcon == this.nextIcon) {
                    i6 = 255;
                } else {
                    i6 = (int) (this.transitionProgress * 255.0f);
                }
                paint5.setAlpha(i6);
                int iDp12 = AndroidUtilities.dp(7.0f) + iCenterY3;
                int iDp13 = iCenterX - AndroidUtilities.dp(3.0f);
                if (this.currentIcon != this.nextIcon) {
                    canvas2.save();
                    float f811 = this.transitionProgress;
                    canvas2.scale(f811, f811, iCenterX, iCenterY3);
                }
                float f912 = iDp13;
                float f913 = iDp12;
                rect = bounds;
                drawable4 = drawable2;
                canvas2.drawLine(iDp13 - AndroidUtilities.dp(6.0f), iDp12 - AndroidUtilities.dp(6.0f), f912, f913, this.paint);
                canvas2 = canvas;
                canvas2.drawLine(f912, f913, iDp13 + AndroidUtilities.dp(12.0f), iDp12 - AndroidUtilities.dp(12.0f), this.paint);
                if (this.currentIcon != this.nextIcon) {
                    canvas2.restore();
                }
            }
            if (this.currentIcon != 12) {
                applyShaderMatrix(false);
                i7 = this.currentIcon;
                i8 = this.nextIcon;
                if (i7 == i8) {
                    i9 = 13;
                    f21 = 1.0f;
                } else {
                    i9 = 13;
                    if (i8 == 13) {
                        f21 = this.transitionProgress;
                    } else {
                        f21 = 1.0f - this.transitionProgress;
                    }
                }
                Paint paint6 = this.paint;
                if (i7 == i8) {
                    i10 = 255;
                } else {
                    i10 = (int) (f21 * 255.0f);
                }
                paint6.setAlpha(i10);
                AndroidUtilities.dp(7.0f);
                AndroidUtilities.dp(3.0f);
                if (this.currentIcon != this.nextIcon) {
                    canvas2.save();
                    canvas2.scale(f21, f21, iCenterX, iCenterY3);
                }
                float fDp19 = AndroidUtilities.dp(7.0f) * this.scale;
                float f914 = iCenterX;
                float f915 = f914 - fDp19;
                float f916 = iCenterY3;
                float f917 = f916 - fDp19;
                float f918 = f914 + fDp19;
                float f919 = fDp19 + f916;
                i11 = i;
                i12 = i9;
                canvas2.drawLine(f915, f917, f918, f919, this.paint);
                canvas2 = canvas;
                canvas2.drawLine(f918, f917, f915, f919, this.paint);
                if (this.currentIcon != this.nextIcon) {
                    canvas2.restore();
                }
            } else {
                applyShaderMatrix(false);
                i7 = this.currentIcon;
                i8 = this.nextIcon;
                if (i7 == i8) {
                    i9 = 13;
                    f21 = 1.0f;
                } else {
                    i9 = 13;
                    if (i8 == 13) {
                        f21 = this.transitionProgress;
                    } else {
                        f21 = 1.0f - this.transitionProgress;
                    }
                }
                Paint paint7 = this.paint;
                if (i7 == i8) {
                    i10 = 255;
                } else {
                    i10 = (int) (f21 * 255.0f);
                }
                paint7.setAlpha(i10);
                AndroidUtilities.dp(7.0f);
                AndroidUtilities.dp(3.0f);
                if (this.currentIcon != this.nextIcon) {
                    canvas2.save();
                    canvas2.scale(f21, f21, iCenterX, iCenterY3);
                }
                float fDp110 = AndroidUtilities.dp(7.0f) * this.scale;
                float f9110 = iCenterX;
                float f9111 = f9110 - fDp110;
                float f9112 = iCenterY3;
                float f9113 = f9112 - fDp110;
                float f9114 = f9110 + fDp110;
                float f9115 = fDp110 + f9112;
                i11 = i;
                i12 = i9;
                canvas2.drawLine(f9111, f9113, f9114, f9115, this.paint);
                canvas2 = canvas;
                canvas2.drawLine(f9114, f9113, f9111, f9115, this.paint);
                if (this.currentIcon != this.nextIcon) {
                    canvas2.restore();
                }
            }
            if (this.currentIcon != i12) {
                applyShaderMatrix(false);
                i13 = this.currentIcon;
                i14 = this.nextIcon;
                if (i13 == i14) {
                    f22 = 1.0f;
                } else if (i14 == i12) {
                    f22 = this.transitionProgress;
                } else {
                    f22 = 1.0f - this.transitionProgress;
                }
                this.textPaint.setAlpha((int) (f22 * 255.0f));
                int iDp14 = AndroidUtilities.dp(5.0f) + iCenterY3;
                int i410 = iCenterX - (this.percentStringWidth / 2);
                f23 = 5.0f;
                if (this.currentIcon != this.nextIcon) {
                    canvas2.save();
                    canvas2.scale(f22, f22, iCenterX, iCenterY3);
                }
                i15 = (int) (this.animatedDownloadProgress * 100.0f);
                if (this.percentString != null) {
                    this.lastPercent = i15;
                    String str2 = String.format("%d%%", Integer.valueOf(i15));
                    this.percentString = str2;
                    this.percentStringWidth = (int) Math.ceil(this.textPaint.measureText(str2));
                } else {
                    this.lastPercent = i15;
                    String str3 = String.format("%d%%", Integer.valueOf(i15));
                    this.percentString = str3;
                    this.percentStringWidth = (int) Math.ceil(this.textPaint.measureText(str3));
                }
                canvas2.drawText(this.percentString, i410, iDp14, this.textPaint);
                if (this.currentIcon != this.nextIcon) {
                    canvas2.restore();
                }
            } else {
                applyShaderMatrix(false);
                i13 = this.currentIcon;
                i14 = this.nextIcon;
                if (i13 == i14) {
                    f22 = 1.0f;
                } else if (i14 == i12) {
                    f22 = this.transitionProgress;
                } else {
                    f22 = 1.0f - this.transitionProgress;
                }
                this.textPaint.setAlpha((int) (f22 * 255.0f));
                int iDp15 = AndroidUtilities.dp(5.0f) + iCenterY3;
                int i411 = iCenterX - (this.percentStringWidth / 2);
                f23 = 5.0f;
                if (this.currentIcon != this.nextIcon) {
                    canvas2.save();
                    canvas2.scale(f22, f22, iCenterX, iCenterY3);
                }
                i15 = (int) (this.animatedDownloadProgress * 100.0f);
                if (this.percentString != null) {
                    this.lastPercent = i15;
                    String str4 = String.format("%d%%", Integer.valueOf(i15));
                    this.percentString = str4;
                    this.percentStringWidth = (int) Math.ceil(this.textPaint.measureText(str4));
                } else {
                    this.lastPercent = i15;
                    String str5 = String.format("%d%%", Integer.valueOf(i15));
                    this.percentString = str5;
                    this.percentStringWidth = (int) Math.ceil(this.textPaint.measureText(str5));
                }
                canvas2.drawText(this.percentString, i411, iDp15, this.textPaint);
                if (this.currentIcon != this.nextIcon) {
                    canvas2.restore();
                }
            }
            i16 = this.currentIcon;
            i17 = 1;
            if (i16 != 0) {
                if (i16 == 0) {
                    if (i16 == 1) {
                        if (this.nextIcon != 0) {
                            i17 = 1;
                        } else if (this.animatingTransition) {
                            if (this.nextIcon == 0) {
                                f24 = 1.0f - this.transitionProgress;
                            } else {
                                f24 = this.transitionProgress;
                            }
                            i17 = 1;
                        } else {
                            i17 = 1;
                            if (this.nextIcon == 1) {
                                f24 = 1.0f;
                            } else {
                                f24 = 0.0f;
                            }
                        }
                        i18 = this.nextIcon;
                        if (i18 != 0) {
                            this.paint2.setAlpha(255);
                        } else {
                            this.paint2.setAlpha(255);
                        }
                        applyShaderMatrix(true);
                        canvas2.save();
                        canvas2.translate(rect.centerX() + (AndroidUtilities.dp(1.0f) * (1.0f - f24)), rect.centerY());
                        f25 = f24 * 500.0f;
                        i19 = this.currentIcon;
                        if (i19 == 1) {
                            f26 = 90.0f;
                        } else {
                            f26 = 0.0f;
                        }
                        if (i19 == 0) {
                            if (i19 == 1) {
                                if (f25 < 100.0f) {
                                    interpolation = CubicBezierInterpolator.EASE_BOTH.getInterpolation(f25 / 100.0f) * (-5.0f);
                                } else if (f25 < 484.0f) {
                                    interpolation = (CubicBezierInterpolator.EASE_BOTH.getInterpolation((f25 - 100.0f) / 384.0f) * 95.0f) - 5.0f;
                                }
                                f26 = interpolation;
                            }
                        } else if (i19 == 1) {
                            if (f25 < 100.0f) {
                                interpolation = CubicBezierInterpolator.EASE_BOTH.getInterpolation(f25 / 100.0f) * (-5.0f);
                            } else if (f25 < 484.0f) {
                                interpolation = (CubicBezierInterpolator.EASE_BOTH.getInterpolation((f25 - 100.0f) / 384.0f) * 95.0f) - 5.0f;
                            }
                            f26 = interpolation;
                        }
                        canvas2.rotate(f26);
                        i20 = this.currentIcon;
                        if (i20 == 0) {
                            canvas2.scale(f20, f20);
                        } else {
                            canvas2.scale(f20, f20);
                        }
                        Theme.playPauseAnimator.draw(canvas2, this.paint2, f25);
                        canvas2.scale(1.0f, -1.0f);
                        Theme.playPauseAnimator.draw(canvas2, this.paint2, f25);
                        canvas2.restore();
                    }
                    if (i16 == i17) {
                        f24 = 1.0f;
                    } else {
                        f24 = 0.0f;
                    }
                    i18 = this.nextIcon;
                    if (i18 != 0) {
                        this.paint2.setAlpha(255);
                    } else {
                        this.paint2.setAlpha(255);
                    }
                    applyShaderMatrix(true);
                    canvas2.save();
                    canvas2.translate(rect.centerX() + (AndroidUtilities.dp(1.0f) * (1.0f - f24)), rect.centerY());
                    f25 = f24 * 500.0f;
                    i19 = this.currentIcon;
                    if (i19 == 1) {
                        f26 = 90.0f;
                    } else {
                        f26 = 0.0f;
                    }
                    if (i19 == 0) {
                        if (i19 == 1) {
                            if (f25 < 100.0f) {
                                interpolation = CubicBezierInterpolator.EASE_BOTH.getInterpolation(f25 / 100.0f) * (-5.0f);
                            } else if (f25 < 484.0f) {
                                interpolation = (CubicBezierInterpolator.EASE_BOTH.getInterpolation((f25 - 100.0f) / 384.0f) * 95.0f) - 5.0f;
                            }
                            f26 = interpolation;
                        }
                    } else if (i19 == 1) {
                        if (f25 < 100.0f) {
                            interpolation = CubicBezierInterpolator.EASE_BOTH.getInterpolation(f25 / 100.0f) * (-5.0f);
                        } else if (f25 < 484.0f) {
                            interpolation = (CubicBezierInterpolator.EASE_BOTH.getInterpolation((f25 - 100.0f) / 384.0f) * 95.0f) - 5.0f;
                        }
                        f26 = interpolation;
                    }
                    canvas2.rotate(f26);
                    i20 = this.currentIcon;
                    if (i20 == 0) {
                        canvas2.scale(f20, f20);
                    } else {
                        canvas2.scale(f20, f20);
                    }
                    Theme.playPauseAnimator.draw(canvas2, this.paint2, f25);
                    canvas2.scale(1.0f, -1.0f);
                    Theme.playPauseAnimator.draw(canvas2, this.paint2, f25);
                    canvas2.restore();
                } else {
                    if (i16 == 1) {
                        if (this.nextIcon != 0) {
                            i17 = 1;
                        } else if (this.animatingTransition) {
                            if (this.nextIcon == 0) {
                                f24 = 1.0f - this.transitionProgress;
                            } else {
                                f24 = this.transitionProgress;
                            }
                            i17 = 1;
                        } else {
                            i17 = 1;
                            if (this.nextIcon == 1) {
                                f24 = 1.0f;
                            } else {
                                f24 = 0.0f;
                            }
                        }
                        i18 = this.nextIcon;
                        if (i18 != 0) {
                            this.paint2.setAlpha(255);
                        } else {
                            this.paint2.setAlpha(255);
                        }
                        applyShaderMatrix(true);
                        canvas2.save();
                        canvas2.translate(rect.centerX() + (AndroidUtilities.dp(1.0f) * (1.0f - f24)), rect.centerY());
                        f25 = f24 * 500.0f;
                        i19 = this.currentIcon;
                        if (i19 == 1) {
                            f26 = 90.0f;
                        } else {
                            f26 = 0.0f;
                        }
                        if (i19 == 0) {
                            if (i19 == 1) {
                                if (f25 < 100.0f) {
                                    interpolation = CubicBezierInterpolator.EASE_BOTH.getInterpolation(f25 / 100.0f) * (-5.0f);
                                } else if (f25 < 484.0f) {
                                    interpolation = (CubicBezierInterpolator.EASE_BOTH.getInterpolation((f25 - 100.0f) / 384.0f) * 95.0f) - 5.0f;
                                }
                                f26 = interpolation;
                            }
                        } else if (i19 == 1) {
                            if (f25 < 100.0f) {
                                interpolation = CubicBezierInterpolator.EASE_BOTH.getInterpolation(f25 / 100.0f) * (-5.0f);
                            } else if (f25 < 484.0f) {
                                interpolation = (CubicBezierInterpolator.EASE_BOTH.getInterpolation((f25 - 100.0f) / 384.0f) * 95.0f) - 5.0f;
                            }
                            f26 = interpolation;
                        }
                        canvas2.rotate(f26);
                        i20 = this.currentIcon;
                        if (i20 == 0) {
                            canvas2.scale(f20, f20);
                        } else {
                            canvas2.scale(f20, f20);
                        }
                        Theme.playPauseAnimator.draw(canvas2, this.paint2, f25);
                        canvas2.scale(1.0f, -1.0f);
                        Theme.playPauseAnimator.draw(canvas2, this.paint2, f25);
                        canvas2.restore();
                    }
                    if (i16 == i17) {
                        f24 = 1.0f;
                    } else {
                        f24 = 0.0f;
                    }
                    i18 = this.nextIcon;
                    if (i18 != 0) {
                        this.paint2.setAlpha(255);
                    } else {
                        this.paint2.setAlpha(255);
                    }
                    applyShaderMatrix(true);
                    canvas2.save();
                    canvas2.translate(rect.centerX() + (AndroidUtilities.dp(1.0f) * (1.0f - f24)), rect.centerY());
                    f25 = f24 * 500.0f;
                    i19 = this.currentIcon;
                    if (i19 == 1) {
                        f26 = 90.0f;
                    } else {
                        f26 = 0.0f;
                    }
                    if (i19 == 0) {
                        if (i19 == 1) {
                            if (f25 < 100.0f) {
                                interpolation = CubicBezierInterpolator.EASE_BOTH.getInterpolation(f25 / 100.0f) * (-5.0f);
                            } else if (f25 < 484.0f) {
                                interpolation = (CubicBezierInterpolator.EASE_BOTH.getInterpolation((f25 - 100.0f) / 384.0f) * 95.0f) - 5.0f;
                            }
                            f26 = interpolation;
                        }
                    } else if (i19 == 1) {
                        if (f25 < 100.0f) {
                            interpolation = CubicBezierInterpolator.EASE_BOTH.getInterpolation(f25 / 100.0f) * (-5.0f);
                        } else if (f25 < 484.0f) {
                            interpolation = (CubicBezierInterpolator.EASE_BOTH.getInterpolation((f25 - 100.0f) / 384.0f) * 95.0f) - 5.0f;
                        }
                        f26 = interpolation;
                    }
                    canvas2.rotate(f26);
                    i20 = this.currentIcon;
                    if (i20 == 0) {
                        canvas2.scale(f20, f20);
                    } else {
                        canvas2.scale(f20, f20);
                    }
                    Theme.playPauseAnimator.draw(canvas2, this.paint2, f25);
                    canvas2.scale(1.0f, -1.0f);
                    Theme.playPauseAnimator.draw(canvas2, this.paint2, f25);
                    canvas2.restore();
                }
            } else if (i16 == 0) {
                if (i16 == 1) {
                    if (this.nextIcon != 0) {
                        i17 = 1;
                    } else if (this.animatingTransition) {
                        if (this.nextIcon == 0) {
                            f24 = 1.0f - this.transitionProgress;
                        } else {
                            f24 = this.transitionProgress;
                        }
                        i17 = 1;
                    } else {
                        i17 = 1;
                        if (this.nextIcon == 1) {
                            f24 = 1.0f;
                        } else {
                            f24 = 0.0f;
                        }
                    }
                    i18 = this.nextIcon;
                    if (i18 != 0) {
                        this.paint2.setAlpha(255);
                    } else {
                        this.paint2.setAlpha(255);
                    }
                    applyShaderMatrix(true);
                    canvas2.save();
                    canvas2.translate(rect.centerX() + (AndroidUtilities.dp(1.0f) * (1.0f - f24)), rect.centerY());
                    f25 = f24 * 500.0f;
                    i19 = this.currentIcon;
                    if (i19 == 1) {
                        f26 = 90.0f;
                    } else {
                        f26 = 0.0f;
                    }
                    if (i19 == 0) {
                        if (i19 == 1) {
                            if (f25 < 100.0f) {
                                interpolation = CubicBezierInterpolator.EASE_BOTH.getInterpolation(f25 / 100.0f) * (-5.0f);
                            } else if (f25 < 484.0f) {
                                interpolation = (CubicBezierInterpolator.EASE_BOTH.getInterpolation((f25 - 100.0f) / 384.0f) * 95.0f) - 5.0f;
                            }
                            f26 = interpolation;
                        }
                    } else if (i19 == 1) {
                        if (f25 < 100.0f) {
                            interpolation = CubicBezierInterpolator.EASE_BOTH.getInterpolation(f25 / 100.0f) * (-5.0f);
                        } else if (f25 < 484.0f) {
                            interpolation = (CubicBezierInterpolator.EASE_BOTH.getInterpolation((f25 - 100.0f) / 384.0f) * 95.0f) - 5.0f;
                        }
                        f26 = interpolation;
                    }
                    canvas2.rotate(f26);
                    i20 = this.currentIcon;
                    if (i20 == 0) {
                        canvas2.scale(f20, f20);
                    } else {
                        canvas2.scale(f20, f20);
                    }
                    Theme.playPauseAnimator.draw(canvas2, this.paint2, f25);
                    canvas2.scale(1.0f, -1.0f);
                    Theme.playPauseAnimator.draw(canvas2, this.paint2, f25);
                    canvas2.restore();
                }
                if (i16 == i17) {
                    f24 = 1.0f;
                } else {
                    f24 = 0.0f;
                }
                i18 = this.nextIcon;
                if (i18 != 0) {
                    this.paint2.setAlpha(255);
                } else {
                    this.paint2.setAlpha(255);
                }
                applyShaderMatrix(true);
                canvas2.save();
                canvas2.translate(rect.centerX() + (AndroidUtilities.dp(1.0f) * (1.0f - f24)), rect.centerY());
                f25 = f24 * 500.0f;
                i19 = this.currentIcon;
                if (i19 == 1) {
                    f26 = 90.0f;
                } else {
                    f26 = 0.0f;
                }
                if (i19 == 0) {
                    if (i19 == 1) {
                        if (f25 < 100.0f) {
                            interpolation = CubicBezierInterpolator.EASE_BOTH.getInterpolation(f25 / 100.0f) * (-5.0f);
                        } else if (f25 < 484.0f) {
                            interpolation = (CubicBezierInterpolator.EASE_BOTH.getInterpolation((f25 - 100.0f) / 384.0f) * 95.0f) - 5.0f;
                        }
                        f26 = interpolation;
                    }
                } else if (i19 == 1) {
                    if (f25 < 100.0f) {
                        interpolation = CubicBezierInterpolator.EASE_BOTH.getInterpolation(f25 / 100.0f) * (-5.0f);
                    } else if (f25 < 484.0f) {
                        interpolation = (CubicBezierInterpolator.EASE_BOTH.getInterpolation((f25 - 100.0f) / 384.0f) * 95.0f) - 5.0f;
                    }
                    f26 = interpolation;
                }
                canvas2.rotate(f26);
                i20 = this.currentIcon;
                if (i20 == 0) {
                    canvas2.scale(f20, f20);
                } else {
                    canvas2.scale(f20, f20);
                }
                Theme.playPauseAnimator.draw(canvas2, this.paint2, f25);
                canvas2.scale(1.0f, -1.0f);
                Theme.playPauseAnimator.draw(canvas2, this.paint2, f25);
                canvas2.restore();
            } else {
                if (i16 == 1) {
                    if (this.nextIcon != 0) {
                        i17 = 1;
                    } else if (this.animatingTransition) {
                        if (this.nextIcon == 0) {
                            f24 = 1.0f - this.transitionProgress;
                        } else {
                            f24 = this.transitionProgress;
                        }
                        i17 = 1;
                    } else {
                        i17 = 1;
                        if (this.nextIcon == 1) {
                            f24 = 1.0f;
                        } else {
                            f24 = 0.0f;
                        }
                    }
                    i18 = this.nextIcon;
                    if (i18 != 0) {
                        this.paint2.setAlpha(255);
                    } else {
                        this.paint2.setAlpha(255);
                    }
                    applyShaderMatrix(true);
                    canvas2.save();
                    canvas2.translate(rect.centerX() + (AndroidUtilities.dp(1.0f) * (1.0f - f24)), rect.centerY());
                    f25 = f24 * 500.0f;
                    i19 = this.currentIcon;
                    if (i19 == 1) {
                        f26 = 90.0f;
                    } else {
                        f26 = 0.0f;
                    }
                    if (i19 == 0) {
                        if (i19 == 1) {
                            if (f25 < 100.0f) {
                                interpolation = CubicBezierInterpolator.EASE_BOTH.getInterpolation(f25 / 100.0f) * (-5.0f);
                            } else if (f25 < 484.0f) {
                                interpolation = (CubicBezierInterpolator.EASE_BOTH.getInterpolation((f25 - 100.0f) / 384.0f) * 95.0f) - 5.0f;
                            }
                            f26 = interpolation;
                        }
                    } else if (i19 == 1) {
                        if (f25 < 100.0f) {
                            interpolation = CubicBezierInterpolator.EASE_BOTH.getInterpolation(f25 / 100.0f) * (-5.0f);
                        } else if (f25 < 484.0f) {
                            interpolation = (CubicBezierInterpolator.EASE_BOTH.getInterpolation((f25 - 100.0f) / 384.0f) * 95.0f) - 5.0f;
                        }
                        f26 = interpolation;
                    }
                    canvas2.rotate(f26);
                    i20 = this.currentIcon;
                    if (i20 == 0) {
                        canvas2.scale(f20, f20);
                    } else {
                        canvas2.scale(f20, f20);
                    }
                    Theme.playPauseAnimator.draw(canvas2, this.paint2, f25);
                    canvas2.scale(1.0f, -1.0f);
                    Theme.playPauseAnimator.draw(canvas2, this.paint2, f25);
                    canvas2.restore();
                }
                if (i16 == i17) {
                    f24 = 1.0f;
                } else {
                    f24 = 0.0f;
                }
                i18 = this.nextIcon;
                if (i18 != 0) {
                    this.paint2.setAlpha(255);
                } else {
                    this.paint2.setAlpha(255);
                }
                applyShaderMatrix(true);
                canvas2.save();
                canvas2.translate(rect.centerX() + (AndroidUtilities.dp(1.0f) * (1.0f - f24)), rect.centerY());
                f25 = f24 * 500.0f;
                i19 = this.currentIcon;
                if (i19 == 1) {
                    f26 = 90.0f;
                } else {
                    f26 = 0.0f;
                }
                if (i19 == 0) {
                    if (i19 == 1) {
                        if (f25 < 100.0f) {
                            interpolation = CubicBezierInterpolator.EASE_BOTH.getInterpolation(f25 / 100.0f) * (-5.0f);
                        } else if (f25 < 484.0f) {
                            interpolation = (CubicBezierInterpolator.EASE_BOTH.getInterpolation((f25 - 100.0f) / 384.0f) * 95.0f) - 5.0f;
                        }
                        f26 = interpolation;
                    }
                } else if (i19 == 1) {
                    if (f25 < 100.0f) {
                        interpolation = CubicBezierInterpolator.EASE_BOTH.getInterpolation(f25 / 100.0f) * (-5.0f);
                    } else if (f25 < 484.0f) {
                        interpolation = (CubicBezierInterpolator.EASE_BOTH.getInterpolation((f25 - 100.0f) / 384.0f) * 95.0f) - 5.0f;
                    }
                    f26 = interpolation;
                }
                canvas2.rotate(f26);
                i20 = this.currentIcon;
                if (i20 == 0) {
                    canvas2.scale(f20, f20);
                } else {
                    canvas2.scale(f20, f20);
                }
                Theme.playPauseAnimator.draw(canvas2, this.paint2, f25);
                canvas2.scale(1.0f, -1.0f);
                Theme.playPauseAnimator.draw(canvas2, this.paint2, f25);
                canvas2.restore();
            }
            if (this.currentIcon != 6) {
                applyShaderMatrix(false);
                if (this.currentIcon != 6) {
                    f28 = this.transitionProgress;
                    if (f28 > 0.5f) {
                        f30 = (f28 - 0.5f) / 0.5f;
                        fMin2 = 1.0f - Math.min(1.0f, f30 / 0.5f);
                        if (f30 > 0.5f) {
                            f29 = (f30 - 0.5f) / 0.5f;
                        } else {
                            f29 = 0.0f;
                        }
                    } else {
                        f29 = 0.0f;
                        fMin2 = 1.0f;
                    }
                    this.paint.setAlpha(255);
                    f27 = f29;
                } else {
                    if (this.nextIcon != 6) {
                        this.paint.setAlpha((int) ((1.0f - this.transitionProgress) * 255.0f));
                    } else {
                        this.paint.setAlpha(255);
                    }
                    fMin2 = 0.0f;
                    f27 = 1.0f;
                }
                iDp = AndroidUtilities.dp(7.0f) + iCenterY3;
                iDp2 = iCenterX - AndroidUtilities.dp(3.0f);
                if (fMin2 < 1.0f) {
                    i21 = iDp2;
                    canvas.drawLine(iDp2 - AndroidUtilities.dp(6.0f), iDp - AndroidUtilities.dp(6.0f), iDp2 - (AndroidUtilities.dp(6.0f) * fMin2), iDp - (AndroidUtilities.dp(6.0f) * fMin2), this.paint);
                } else {
                    i21 = iDp2;
                }
                if (f27 > 0.0f) {
                    float f920 = i21;
                    float f921 = iDp;
                    canvas2 = canvas;
                    canvas2.drawLine(f920, f921, f920 + (AndroidUtilities.dp(12.0f) * f27), f921 - (AndroidUtilities.dp(12.0f) * f27), this.paint);
                } else {
                    canvas2 = canvas;
                }
            } else {
                applyShaderMatrix(false);
                if (this.currentIcon != 6) {
                    f28 = this.transitionProgress;
                    if (f28 > 0.5f) {
                        f30 = (f28 - 0.5f) / 0.5f;
                        fMin2 = 1.0f - Math.min(1.0f, f30 / 0.5f);
                        if (f30 > 0.5f) {
                            f29 = (f30 - 0.5f) / 0.5f;
                        } else {
                            f29 = 0.0f;
                        }
                    } else {
                        f29 = 0.0f;
                        fMin2 = 1.0f;
                    }
                    this.paint.setAlpha(255);
                    f27 = f29;
                } else {
                    if (this.nextIcon != 6) {
                        this.paint.setAlpha((int) ((1.0f - this.transitionProgress) * 255.0f));
                    } else {
                        this.paint.setAlpha(255);
                    }
                    fMin2 = 0.0f;
                    f27 = 1.0f;
                }
                iDp = AndroidUtilities.dp(7.0f) + iCenterY3;
                iDp2 = iCenterX - AndroidUtilities.dp(3.0f);
                if (fMin2 < 1.0f) {
                    i21 = iDp2;
                    canvas.drawLine(iDp2 - AndroidUtilities.dp(6.0f), iDp - AndroidUtilities.dp(6.0f), iDp2 - (AndroidUtilities.dp(6.0f) * fMin2), iDp - (AndroidUtilities.dp(6.0f) * fMin2), this.paint);
                } else {
                    i21 = iDp2;
                }
                if (f27 > 0.0f) {
                    float f922 = i21;
                    float f923 = iDp;
                    canvas2 = canvas;
                    canvas2.drawLine(f922, f923, f922 + (AndroidUtilities.dp(12.0f) * f27), f923 - (AndroidUtilities.dp(12.0f) * f27), this.paint);
                } else {
                    canvas2 = canvas;
                }
            }
            if (drawable3 != null) {
                int intrinsicWidth3 = (int) (drawable3.getIntrinsicWidth() * f19);
                int intrinsicHeight3 = (int) (drawable3.getIntrinsicHeight() * f19);
                drawable3.setColorFilter(this.colorFilter);
                if (this.currentIcon == this.nextIcon) {
                    i27 = 255;
                } else {
                    i27 = (int) ((1.0f - this.transitionProgress) * 255.0f);
                }
                drawable3.setAlpha(i27);
                int i412 = intrinsicWidth3 / 2;
                int i413 = intrinsicHeight3 / 2;
                drawable3.setBounds(iCenterX - i412, iCenterY3 - i413, i412 + iCenterX, i413 + iCenterY3);
                drawable3.draw(canvas2);
            }
            if (drawable4 != null) {
                int intrinsicWidth4 = (int) (drawable4.getIntrinsicWidth() * f20);
                int intrinsicHeight4 = (int) (drawable4.getIntrinsicHeight() * f20);
                drawable4.setColorFilter(this.colorFilter);
                if (this.currentIcon == this.nextIcon) {
                    i26 = 255;
                } else {
                    i26 = (int) (this.transitionProgress * 255.0f);
                }
                drawable4.setAlpha(i26);
                int i52 = intrinsicWidth4 / 2;
                int i53 = intrinsicHeight4 / 2;
                drawable4.setBounds(iCenterX - i52, iCenterY3 - i53, i52 + iCenterX, i53 + iCenterY3);
                drawable4.draw(canvas2);
            }
            if (pathArr4 != null) {
                int iDp16 = AndroidUtilities.dp(24.0f);
                this.paint2.setStyle(Paint.Style.FILL_AND_STROKE);
                Paint paint8 = this.paint2;
                if (this.currentIcon == this.nextIcon) {
                    i25 = 255;
                } else {
                    i25 = (int) ((1.0f - this.transitionProgress) * 255.0f);
                }
                paint8.setAlpha(i25);
                applyShaderMatrix(true);
                canvas2.save();
                canvas2.translate(iCenterX, iCenterY3);
                canvas2.scale(f19, f19);
                float f104 = (-iDp16) / 2;
                canvas2.translate(f104, f104);
                path4 = pathArr4[0];
                if (path4 != null) {
                    canvas2.drawPath(path4, this.paint2);
                }
                path5 = pathArr4[1];
                if (path5 != null) {
                    canvas2.drawPath(path5, this.backPaint);
                }
                canvas2.restore();
            }
            if (pathArr3 != null) {
                int iDp17 = AndroidUtilities.dp(24.0f);
                if (this.currentIcon == this.nextIcon) {
                    i24 = 255;
                } else {
                    i24 = (int) (this.transitionProgress * 255.0f);
                }
                this.paint2.setStyle(Paint.Style.FILL_AND_STROKE);
                this.paint2.setAlpha(i24);
                applyShaderMatrix(true);
                canvas2.save();
                canvas2.translate(iCenterX, iCenterY3);
                float f105 = f20;
                canvas2.scale(f105, f105);
                float f106 = (-iDp17) / 2;
                canvas2.translate(f106, f106);
                path = pathArr3[0];
                if (path != null) {
                    canvas2.drawPath(path, this.paint2);
                }
                if (pathArr3.length >= 3) {
                    canvas2.drawPath(path3, this.paint);
                }
                path2 = pathArr3[1];
                if (path2 != null) {
                    if (i24 != 255) {
                        int alpha2 = this.backPaint.getAlpha();
                        this.backPaint.setAlpha((int) (alpha2 * (i24 / 255.0f)));
                        canvas2.drawPath(pathArr3[1], this.backPaint);
                        this.backPaint.setAlpha(alpha2);
                    } else {
                        canvas2.drawPath(path2, this.backPaint);
                    }
                }
                canvas2.restore();
            }
            long jCurrentTimeMillis2 = System.currentTimeMillis();
            j = jCurrentTimeMillis2 - this.lastAnimationTime;
            if (j > 17) {
                j = 17;
            }
            this.lastAnimationTime = jCurrentTimeMillis2;
            i22 = this.currentIcon;
            if (i22 != 3) {
                float f107 = this.downloadRadOffset + ((360 * j) / 2500.0f);
                this.downloadRadOffset = f107;
                this.downloadRadOffset = getCircleValue(f107);
                if (this.nextIcon != 2) {
                    f31 = this.downloadProgress;
                    f32 = this.downloadProgressAnimationStart;
                    f33 = f31 - f32;
                    if (f33 > 0.0f) {
                        f34 = this.downloadProgressTime + j;
                        this.downloadProgressTime = f34;
                        if (f34 >= 200.0f) {
                            this.animatedDownloadProgress = f31;
                            this.downloadProgressAnimationStart = f31;
                            this.downloadProgressTime = 0.0f;
                        } else {
                            this.animatedDownloadProgress = f32 + (f33 * this.interpolator.getInterpolation(f34 / 200.0f));
                        }
                    }
                }
                invalidateSelf();
            } else {
                float f108 = this.downloadRadOffset + ((360 * j) / 2500.0f);
                this.downloadRadOffset = f108;
                this.downloadRadOffset = getCircleValue(f108);
                if (this.nextIcon != 2) {
                    f31 = this.downloadProgress;
                    f32 = this.downloadProgressAnimationStart;
                    f33 = f31 - f32;
                    if (f33 > 0.0f) {
                        f34 = this.downloadProgressTime + j;
                        this.downloadProgressTime = f34;
                        if (f34 >= 200.0f) {
                            this.animatedDownloadProgress = f31;
                            this.downloadProgressAnimationStart = f31;
                            this.downloadProgressTime = 0.0f;
                        } else {
                            this.animatedDownloadProgress = f32 + (f33 * this.interpolator.getInterpolation(f34 / 200.0f));
                        }
                    }
                }
                invalidateSelf();
            }
            if (this.animatingTransition) {
                f35 = this.transitionProgress;
                if (f35 < 1.0f) {
                    f36 = f35 + (j / this.transitionAnimationTime);
                    this.transitionProgress = f36;
                    if (f36 >= 1.0f) {
                        this.currentIcon = this.nextIcon;
                        this.transitionProgress = 1.0f;
                        this.animatingTransition = false;
                    }
                    invalidateSelf();
                }
            }
            i23 = i11;
            if (i23 >= 1) {
                canvas2.restoreToCount(i23);
            }
        }
        i2 = 15;
        z = false;
        applyShaderMatrix(z);
        int i54 = this.nextIcon;
        if (i54 == 2) {
            if (this.drawProgressCircle) {
                float f109 = this.transitionProgress;
                if (f109 <= 0.5f) {
                    float f110 = 1.0f - (f109 / 0.5f);
                    fDp5 = AndroidUtilities.dp(7.0f) * f110 * this.scale;
                    i33 = (int) (f110 * 255.0f);
                } else {
                    fDp5 = f;
                    i33 = 0;
                }
            } else {
                fDp5 = f;
                i33 = 0;
            }
            iMin = i33;
            fCenterX = f;
            f40 = fCenterX;
            f39 = f40;
            f38 = 1.0f;
        } else {
            if (i54 != i2 && i54 != 0 && i54 != 1 && i54 != 5 && i54 != 8 && i54 != 9 && i54 != 7) {
                if (i54 == 6) {
                    i29 = 6;
                } else if (i54 == 4) {
                    f38 = 1.0f - this.transitionProgress;
                    fDp5 = AndroidUtilities.dp(7.0f) * this.scale;
                    int i55 = (int) (f38 * 255.0f);
                    if (this.currentIcon == 14) {
                        fCenterX = bounds.left;
                        iCenterY2 = bounds.top;
                    } else {
                        fCenterX = bounds.centerX();
                        iCenterY2 = bounds.centerY();
                    }
                    iMin = i55;
                    f40 = f;
                    f39 = iCenterY2;
                } else if (i54 == 14 || i54 == 3) {
                    float f111 = this.transitionProgress;
                    float f112 = 1.0f - f111;
                    if (this.currentIcon == 4) {
                        f42 = f111;
                        f41 = f;
                    } else {
                        f41 = f112 * 45.0f;
                        f42 = 1.0f;
                    }
                    float fDp20 = AndroidUtilities.dp(7.0f) * this.scale;
                    int i56 = (int) (f111 * 255.0f);
                    if (this.nextIcon == 14) {
                        fCenterX2 = bounds.left;
                        iCenterY = bounds.top;
                    } else {
                        fCenterX2 = bounds.centerX();
                        iCenterY = bounds.centerY();
                    }
                    f39 = iCenterY;
                    iMin = i56;
                    f38 = f42;
                    fDp5 = fDp20;
                    f40 = f41;
                    fCenterX = fCenterX2;
                } else {
                    fDp5 = AndroidUtilities.dp(7.0f) * this.scale;
                    f88 = 360.0f;
                    fCenterX = f;
                    f40 = fCenterX;
                    f39 = f40;
                    f38 = 1.0f;
                    f37 = 1.0f;
                    iMin = 255;
                }
                if (f38 != f37) {
                    canvas2.save();
                    canvas2.scale(f38, f38, fCenterX, f39);
                }
                if (f40 != f) {
                    canvas2.save();
                    canvas2.rotate(f40, iCenterX, iCenterY3);
                }
                if (iMin != 0) {
                    f45 = iMin;
                    this.paint.setAlpha((int) (this.overrideAlpha * f45));
                    if (this.currentIcon != 14 || this.nextIcon == 14) {
                        f43 = 4.0f;
                        this.paint3.setAlpha((int) (f45 * this.overrideAlpha));
                        this.rect.set(iCenterX - AndroidUtilities.dp(3.5f), iCenterY3 - AndroidUtilities.dp(3.5f), AndroidUtilities.dp(3.5f) + iCenterX, AndroidUtilities.dp(3.5f) + iCenterY3);
                        canvas2.drawRoundRect(this.rect, AndroidUtilities.dp(2.0f), AndroidUtilities.dp(2.0f), this.paint3);
                    } else {
                        float f113 = iCenterX;
                        float f114 = f113 - fDp5;
                        float f115 = iCenterY3;
                        float f116 = f115 - fDp5;
                        float f117 = f113 + fDp5;
                        float f118 = f115 + fDp5;
                        f43 = 4.0f;
                        canvas2.drawLine(f114, f116, f117, f118, this.paint);
                        canvas2 = canvas;
                        canvas2.drawLine(f117, f116, f114, f118, this.paint);
                    }
                } else {
                    f43 = 4.0f;
                }
                if (f40 != f) {
                    canvas2.restore();
                }
                if (f38 != f37) {
                    canvas2.restore();
                }
                i30 = this.currentIcon;
                if ((i30 != 3 || i30 == 14 || (i30 == 4 && ((i32 = this.nextIcon) == 14 || i32 == 3))) && iMin != 0 && this.drawProgressCircle) {
                    float fMax3 = Math.max(f43, this.animatedDownloadProgress * f88);
                    if (this.isMini) {
                        f44 = 2.0f;
                    } else {
                        f44 = f43;
                    }
                    int iDp18 = AndroidUtilities.dp(f44);
                    this.rect.set(bounds.left + iDp18, bounds.top + iDp18, bounds.right - iDp18, bounds.bottom - iDp18);
                    i31 = this.currentIcon;
                    if (i31 != 14 || (i31 == 4 && this.nextIcon == 14)) {
                        this.paint.setAlpha((int) (iMin * 0.15f * this.overrideAlpha));
                        canvas2.drawArc(this.rect, 0.0f, 360.0f, false, this.paint);
                        this.paint.setAlpha(iMin);
                    }
                    canvas2 = canvas;
                    canvas2.drawArc(this.rect, this.downloadRadOffset, fMax3, false, this.paint);
                }
                i3 = this.currentIcon;
                if (i3 == this.nextIcon) {
                    f20 = 1.0f;
                    f19 = 1.0f;
                } else {
                    if (i3 != 4) {
                        fMin = this.transitionProgress;
                        fMax = 1.0f - fMin;
                    } else {
                        fMin = this.transitionProgress;
                        fMax = 1.0f - fMin;
                    }
                    f19 = fMax;
                    f20 = fMin;
                }
                i4 = this.nextIcon;
                if (i4 == i2) {
                    pathArr = Theme.chat_updatePath;
                } else {
                    if (this.currentIcon == i2) {
                        pathArr2 = Theme.chat_updatePath;
                        pathArr = null;
                    } else {
                        pathArr = null;
                    }
                    if (i4 == 5) {
                        pathArr = Theme.chat_filePath;
                    } else if (this.currentIcon == 5) {
                        pathArr2 = Theme.chat_filePath;
                    }
                    pathArr3 = pathArr;
                    pathArr4 = pathArr2;
                    if (i4 == 7) {
                        drawable2 = Theme.chat_flameIcon;
                        drawable = null;
                        i5 = 8;
                    } else {
                        if (this.currentIcon == 7) {
                            drawable = Theme.chat_flameIcon;
                        } else {
                            drawable = null;
                        }
                        i5 = 8;
                        drawable2 = null;
                    }
                    if (i4 == i5) {
                        drawable2 = Theme.chat_gifIcon;
                    } else if (this.currentIcon == i5) {
                        drawable = Theme.chat_gifIcon;
                    }
                    drawable3 = drawable;
                    if (this.currentIcon != 9) {
                        applyShaderMatrix(false);
                        Paint paint9 = this.paint;
                        if (this.currentIcon == this.nextIcon) {
                            i6 = 255;
                        } else {
                            i6 = (int) (this.transitionProgress * 255.0f);
                        }
                        paint9.setAlpha(i6);
                        int iDp19 = AndroidUtilities.dp(7.0f) + iCenterY3;
                        int iDp110 = iCenterX - AndroidUtilities.dp(3.0f);
                        if (this.currentIcon != this.nextIcon) {
                            canvas2.save();
                            float f812 = this.transitionProgress;
                            canvas2.scale(f812, f812, iCenterX, iCenterY3);
                        }
                        float f9116 = iDp110;
                        float f9117 = iDp19;
                        rect = bounds;
                        drawable4 = drawable2;
                        canvas2.drawLine(iDp110 - AndroidUtilities.dp(6.0f), iDp19 - AndroidUtilities.dp(6.0f), f9116, f9117, this.paint);
                        canvas2 = canvas;
                        canvas2.drawLine(f9116, f9117, iDp110 + AndroidUtilities.dp(12.0f), iDp19 - AndroidUtilities.dp(12.0f), this.paint);
                        if (this.currentIcon != this.nextIcon) {
                            canvas2.restore();
                        }
                    } else {
                        applyShaderMatrix(false);
                        Paint paint10 = this.paint;
                        if (this.currentIcon == this.nextIcon) {
                            i6 = 255;
                        } else {
                            i6 = (int) (this.transitionProgress * 255.0f);
                        }
                        paint10.setAlpha(i6);
                        int iDp111 = AndroidUtilities.dp(7.0f) + iCenterY3;
                        int iDp112 = iCenterX - AndroidUtilities.dp(3.0f);
                        if (this.currentIcon != this.nextIcon) {
                            canvas2.save();
                            float f813 = this.transitionProgress;
                            canvas2.scale(f813, f813, iCenterX, iCenterY3);
                        }
                        float f9118 = iDp112;
                        float f9119 = iDp111;
                        rect = bounds;
                        drawable4 = drawable2;
                        canvas2.drawLine(iDp112 - AndroidUtilities.dp(6.0f), iDp111 - AndroidUtilities.dp(6.0f), f9118, f9119, this.paint);
                        canvas2 = canvas;
                        canvas2.drawLine(f9118, f9119, iDp112 + AndroidUtilities.dp(12.0f), iDp111 - AndroidUtilities.dp(12.0f), this.paint);
                        if (this.currentIcon != this.nextIcon) {
                            canvas2.restore();
                        }
                    }
                    if (this.currentIcon != 12) {
                        applyShaderMatrix(false);
                        i7 = this.currentIcon;
                        i8 = this.nextIcon;
                        if (i7 == i8) {
                            i9 = 13;
                            f21 = 1.0f;
                        } else {
                            i9 = 13;
                            if (i8 == 13) {
                                f21 = this.transitionProgress;
                            } else {
                                f21 = 1.0f - this.transitionProgress;
                            }
                        }
                        Paint paint11 = this.paint;
                        if (i7 == i8) {
                            i10 = 255;
                        } else {
                            i10 = (int) (f21 * 255.0f);
                        }
                        paint11.setAlpha(i10);
                        AndroidUtilities.dp(7.0f);
                        AndroidUtilities.dp(3.0f);
                        if (this.currentIcon != this.nextIcon) {
                            canvas2.save();
                            canvas2.scale(f21, f21, iCenterX, iCenterY3);
                        }
                        float fDp111 = AndroidUtilities.dp(7.0f) * this.scale;
                        float f91110 = iCenterX;
                        float f91111 = f91110 - fDp111;
                        float f91112 = iCenterY3;
                        float f91113 = f91112 - fDp111;
                        float f91114 = f91110 + fDp111;
                        float f91115 = fDp111 + f91112;
                        i11 = i;
                        i12 = i9;
                        canvas2.drawLine(f91111, f91113, f91114, f91115, this.paint);
                        canvas2 = canvas;
                        canvas2.drawLine(f91114, f91113, f91111, f91115, this.paint);
                        if (this.currentIcon != this.nextIcon) {
                            canvas2.restore();
                        }
                    } else {
                        applyShaderMatrix(false);
                        i7 = this.currentIcon;
                        i8 = this.nextIcon;
                        if (i7 == i8) {
                            i9 = 13;
                            f21 = 1.0f;
                        } else {
                            i9 = 13;
                            if (i8 == 13) {
                                f21 = this.transitionProgress;
                            } else {
                                f21 = 1.0f - this.transitionProgress;
                            }
                        }
                        Paint paint12 = this.paint;
                        if (i7 == i8) {
                            i10 = 255;
                        } else {
                            i10 = (int) (f21 * 255.0f);
                        }
                        paint12.setAlpha(i10);
                        AndroidUtilities.dp(7.0f);
                        AndroidUtilities.dp(3.0f);
                        if (this.currentIcon != this.nextIcon) {
                            canvas2.save();
                            canvas2.scale(f21, f21, iCenterX, iCenterY3);
                        }
                        float fDp112 = AndroidUtilities.dp(7.0f) * this.scale;
                        float f91116 = iCenterX;
                        float f91117 = f91116 - fDp112;
                        float f91118 = iCenterY3;
                        float f91119 = f91118 - fDp112;
                        float f911110 = f91116 + fDp112;
                        float f911111 = fDp112 + f91118;
                        i11 = i;
                        i12 = i9;
                        canvas2.drawLine(f91117, f91119, f911110, f911111, this.paint);
                        canvas2 = canvas;
                        canvas2.drawLine(f911110, f91119, f91117, f911111, this.paint);
                        if (this.currentIcon != this.nextIcon) {
                            canvas2.restore();
                        }
                    }
                    if (this.currentIcon != i12) {
                        applyShaderMatrix(false);
                        i13 = this.currentIcon;
                        i14 = this.nextIcon;
                        if (i13 == i14) {
                            f22 = 1.0f;
                        } else if (i14 == i12) {
                            f22 = this.transitionProgress;
                        } else {
                            f22 = 1.0f - this.transitionProgress;
                        }
                        this.textPaint.setAlpha((int) (f22 * 255.0f));
                        int iDp113 = AndroidUtilities.dp(5.0f) + iCenterY3;
                        int i414 = iCenterX - (this.percentStringWidth / 2);
                        f23 = 5.0f;
                        if (this.currentIcon != this.nextIcon) {
                            canvas2.save();
                            canvas2.scale(f22, f22, iCenterX, iCenterY3);
                        }
                        i15 = (int) (this.animatedDownloadProgress * 100.0f);
                        if (this.percentString != null) {
                            this.lastPercent = i15;
                            String str6 = String.format("%d%%", Integer.valueOf(i15));
                            this.percentString = str6;
                            this.percentStringWidth = (int) Math.ceil(this.textPaint.measureText(str6));
                        } else {
                            this.lastPercent = i15;
                            String str7 = String.format("%d%%", Integer.valueOf(i15));
                            this.percentString = str7;
                            this.percentStringWidth = (int) Math.ceil(this.textPaint.measureText(str7));
                        }
                        canvas2.drawText(this.percentString, i414, iDp113, this.textPaint);
                        if (this.currentIcon != this.nextIcon) {
                            canvas2.restore();
                        }
                    } else {
                        applyShaderMatrix(false);
                        i13 = this.currentIcon;
                        i14 = this.nextIcon;
                        if (i13 == i14) {
                            f22 = 1.0f;
                        } else if (i14 == i12) {
                            f22 = this.transitionProgress;
                        } else {
                            f22 = 1.0f - this.transitionProgress;
                        }
                        this.textPaint.setAlpha((int) (f22 * 255.0f));
                        int iDp114 = AndroidUtilities.dp(5.0f) + iCenterY3;
                        int i415 = iCenterX - (this.percentStringWidth / 2);
                        f23 = 5.0f;
                        if (this.currentIcon != this.nextIcon) {
                            canvas2.save();
                            canvas2.scale(f22, f22, iCenterX, iCenterY3);
                        }
                        i15 = (int) (this.animatedDownloadProgress * 100.0f);
                        if (this.percentString != null) {
                            this.lastPercent = i15;
                            String str8 = String.format("%d%%", Integer.valueOf(i15));
                            this.percentString = str8;
                            this.percentStringWidth = (int) Math.ceil(this.textPaint.measureText(str8));
                        } else {
                            this.lastPercent = i15;
                            String str9 = String.format("%d%%", Integer.valueOf(i15));
                            this.percentString = str9;
                            this.percentStringWidth = (int) Math.ceil(this.textPaint.measureText(str9));
                        }
                        canvas2.drawText(this.percentString, i415, iDp114, this.textPaint);
                        if (this.currentIcon != this.nextIcon) {
                            canvas2.restore();
                        }
                    }
                    i16 = this.currentIcon;
                    i17 = 1;
                    if (i16 != 0) {
                        if (i16 == 0) {
                            if (i16 == 1) {
                                if (this.nextIcon != 0) {
                                    i17 = 1;
                                } else if (this.animatingTransition) {
                                    if (this.nextIcon == 0) {
                                        f24 = 1.0f - this.transitionProgress;
                                    } else {
                                        f24 = this.transitionProgress;
                                    }
                                    i17 = 1;
                                } else {
                                    i17 = 1;
                                    if (this.nextIcon == 1) {
                                        f24 = 1.0f;
                                    } else {
                                        f24 = 0.0f;
                                    }
                                }
                                i18 = this.nextIcon;
                                if (i18 != 0) {
                                    this.paint2.setAlpha(255);
                                } else {
                                    this.paint2.setAlpha(255);
                                }
                                applyShaderMatrix(true);
                                canvas2.save();
                                canvas2.translate(rect.centerX() + (AndroidUtilities.dp(1.0f) * (1.0f - f24)), rect.centerY());
                                f25 = f24 * 500.0f;
                                i19 = this.currentIcon;
                                if (i19 == 1) {
                                    f26 = 90.0f;
                                } else {
                                    f26 = 0.0f;
                                }
                                if (i19 == 0) {
                                    if (i19 == 1) {
                                        if (f25 < 100.0f) {
                                            interpolation = CubicBezierInterpolator.EASE_BOTH.getInterpolation(f25 / 100.0f) * (-5.0f);
                                        } else if (f25 < 484.0f) {
                                            interpolation = (CubicBezierInterpolator.EASE_BOTH.getInterpolation((f25 - 100.0f) / 384.0f) * 95.0f) - 5.0f;
                                        }
                                        f26 = interpolation;
                                    }
                                } else if (i19 == 1) {
                                    if (f25 < 100.0f) {
                                        interpolation = CubicBezierInterpolator.EASE_BOTH.getInterpolation(f25 / 100.0f) * (-5.0f);
                                    } else if (f25 < 484.0f) {
                                        interpolation = (CubicBezierInterpolator.EASE_BOTH.getInterpolation((f25 - 100.0f) / 384.0f) * 95.0f) - 5.0f;
                                    }
                                    f26 = interpolation;
                                }
                                canvas2.rotate(f26);
                                i20 = this.currentIcon;
                                if (i20 == 0) {
                                    canvas2.scale(f20, f20);
                                } else {
                                    canvas2.scale(f20, f20);
                                }
                                Theme.playPauseAnimator.draw(canvas2, this.paint2, f25);
                                canvas2.scale(1.0f, -1.0f);
                                Theme.playPauseAnimator.draw(canvas2, this.paint2, f25);
                                canvas2.restore();
                            }
                            if (i16 == i17) {
                                f24 = 1.0f;
                            } else {
                                f24 = 0.0f;
                            }
                            i18 = this.nextIcon;
                            if (i18 != 0) {
                                this.paint2.setAlpha(255);
                            } else {
                                this.paint2.setAlpha(255);
                            }
                            applyShaderMatrix(true);
                            canvas2.save();
                            canvas2.translate(rect.centerX() + (AndroidUtilities.dp(1.0f) * (1.0f - f24)), rect.centerY());
                            f25 = f24 * 500.0f;
                            i19 = this.currentIcon;
                            if (i19 == 1) {
                                f26 = 90.0f;
                            } else {
                                f26 = 0.0f;
                            }
                            if (i19 == 0) {
                                if (i19 == 1) {
                                    if (f25 < 100.0f) {
                                        interpolation = CubicBezierInterpolator.EASE_BOTH.getInterpolation(f25 / 100.0f) * (-5.0f);
                                    } else if (f25 < 484.0f) {
                                        interpolation = (CubicBezierInterpolator.EASE_BOTH.getInterpolation((f25 - 100.0f) / 384.0f) * 95.0f) - 5.0f;
                                    }
                                    f26 = interpolation;
                                }
                            } else if (i19 == 1) {
                                if (f25 < 100.0f) {
                                    interpolation = CubicBezierInterpolator.EASE_BOTH.getInterpolation(f25 / 100.0f) * (-5.0f);
                                } else if (f25 < 484.0f) {
                                    interpolation = (CubicBezierInterpolator.EASE_BOTH.getInterpolation((f25 - 100.0f) / 384.0f) * 95.0f) - 5.0f;
                                }
                                f26 = interpolation;
                            }
                            canvas2.rotate(f26);
                            i20 = this.currentIcon;
                            if (i20 == 0) {
                                canvas2.scale(f20, f20);
                            } else {
                                canvas2.scale(f20, f20);
                            }
                            Theme.playPauseAnimator.draw(canvas2, this.paint2, f25);
                            canvas2.scale(1.0f, -1.0f);
                            Theme.playPauseAnimator.draw(canvas2, this.paint2, f25);
                            canvas2.restore();
                        } else {
                            if (i16 == 1) {
                                if (this.nextIcon != 0) {
                                    i17 = 1;
                                } else if (this.animatingTransition) {
                                    if (this.nextIcon == 0) {
                                        f24 = 1.0f - this.transitionProgress;
                                    } else {
                                        f24 = this.transitionProgress;
                                    }
                                    i17 = 1;
                                } else {
                                    i17 = 1;
                                    if (this.nextIcon == 1) {
                                        f24 = 1.0f;
                                    } else {
                                        f24 = 0.0f;
                                    }
                                }
                                i18 = this.nextIcon;
                                if (i18 != 0) {
                                    this.paint2.setAlpha(255);
                                } else {
                                    this.paint2.setAlpha(255);
                                }
                                applyShaderMatrix(true);
                                canvas2.save();
                                canvas2.translate(rect.centerX() + (AndroidUtilities.dp(1.0f) * (1.0f - f24)), rect.centerY());
                                f25 = f24 * 500.0f;
                                i19 = this.currentIcon;
                                if (i19 == 1) {
                                    f26 = 90.0f;
                                } else {
                                    f26 = 0.0f;
                                }
                                if (i19 == 0) {
                                    if (i19 == 1) {
                                        if (f25 < 100.0f) {
                                            interpolation = CubicBezierInterpolator.EASE_BOTH.getInterpolation(f25 / 100.0f) * (-5.0f);
                                        } else if (f25 < 484.0f) {
                                            interpolation = (CubicBezierInterpolator.EASE_BOTH.getInterpolation((f25 - 100.0f) / 384.0f) * 95.0f) - 5.0f;
                                        }
                                        f26 = interpolation;
                                    }
                                } else if (i19 == 1) {
                                    if (f25 < 100.0f) {
                                        interpolation = CubicBezierInterpolator.EASE_BOTH.getInterpolation(f25 / 100.0f) * (-5.0f);
                                    } else if (f25 < 484.0f) {
                                        interpolation = (CubicBezierInterpolator.EASE_BOTH.getInterpolation((f25 - 100.0f) / 384.0f) * 95.0f) - 5.0f;
                                    }
                                    f26 = interpolation;
                                }
                                canvas2.rotate(f26);
                                i20 = this.currentIcon;
                                if (i20 == 0) {
                                    canvas2.scale(f20, f20);
                                } else {
                                    canvas2.scale(f20, f20);
                                }
                                Theme.playPauseAnimator.draw(canvas2, this.paint2, f25);
                                canvas2.scale(1.0f, -1.0f);
                                Theme.playPauseAnimator.draw(canvas2, this.paint2, f25);
                                canvas2.restore();
                            }
                            if (i16 == i17) {
                                f24 = 1.0f;
                            } else {
                                f24 = 0.0f;
                            }
                            i18 = this.nextIcon;
                            if (i18 != 0) {
                                this.paint2.setAlpha(255);
                            } else {
                                this.paint2.setAlpha(255);
                            }
                            applyShaderMatrix(true);
                            canvas2.save();
                            canvas2.translate(rect.centerX() + (AndroidUtilities.dp(1.0f) * (1.0f - f24)), rect.centerY());
                            f25 = f24 * 500.0f;
                            i19 = this.currentIcon;
                            if (i19 == 1) {
                                f26 = 90.0f;
                            } else {
                                f26 = 0.0f;
                            }
                            if (i19 == 0) {
                                if (i19 == 1) {
                                    if (f25 < 100.0f) {
                                        interpolation = CubicBezierInterpolator.EASE_BOTH.getInterpolation(f25 / 100.0f) * (-5.0f);
                                    } else if (f25 < 484.0f) {
                                        interpolation = (CubicBezierInterpolator.EASE_BOTH.getInterpolation((f25 - 100.0f) / 384.0f) * 95.0f) - 5.0f;
                                    }
                                    f26 = interpolation;
                                }
                            } else if (i19 == 1) {
                                if (f25 < 100.0f) {
                                    interpolation = CubicBezierInterpolator.EASE_BOTH.getInterpolation(f25 / 100.0f) * (-5.0f);
                                } else if (f25 < 484.0f) {
                                    interpolation = (CubicBezierInterpolator.EASE_BOTH.getInterpolation((f25 - 100.0f) / 384.0f) * 95.0f) - 5.0f;
                                }
                                f26 = interpolation;
                            }
                            canvas2.rotate(f26);
                            i20 = this.currentIcon;
                            if (i20 == 0) {
                                canvas2.scale(f20, f20);
                            } else {
                                canvas2.scale(f20, f20);
                            }
                            Theme.playPauseAnimator.draw(canvas2, this.paint2, f25);
                            canvas2.scale(1.0f, -1.0f);
                            Theme.playPauseAnimator.draw(canvas2, this.paint2, f25);
                            canvas2.restore();
                        }
                    } else if (i16 == 0) {
                        if (i16 == 1) {
                            if (this.nextIcon != 0) {
                                i17 = 1;
                            } else if (this.animatingTransition) {
                                if (this.nextIcon == 0) {
                                    f24 = 1.0f - this.transitionProgress;
                                } else {
                                    f24 = this.transitionProgress;
                                }
                                i17 = 1;
                            } else {
                                i17 = 1;
                                if (this.nextIcon == 1) {
                                    f24 = 1.0f;
                                } else {
                                    f24 = 0.0f;
                                }
                            }
                            i18 = this.nextIcon;
                            if (i18 != 0) {
                                this.paint2.setAlpha(255);
                            } else {
                                this.paint2.setAlpha(255);
                            }
                            applyShaderMatrix(true);
                            canvas2.save();
                            canvas2.translate(rect.centerX() + (AndroidUtilities.dp(1.0f) * (1.0f - f24)), rect.centerY());
                            f25 = f24 * 500.0f;
                            i19 = this.currentIcon;
                            if (i19 == 1) {
                                f26 = 90.0f;
                            } else {
                                f26 = 0.0f;
                            }
                            if (i19 == 0) {
                                if (i19 == 1) {
                                    if (f25 < 100.0f) {
                                        interpolation = CubicBezierInterpolator.EASE_BOTH.getInterpolation(f25 / 100.0f) * (-5.0f);
                                    } else if (f25 < 484.0f) {
                                        interpolation = (CubicBezierInterpolator.EASE_BOTH.getInterpolation((f25 - 100.0f) / 384.0f) * 95.0f) - 5.0f;
                                    }
                                    f26 = interpolation;
                                }
                            } else if (i19 == 1) {
                                if (f25 < 100.0f) {
                                    interpolation = CubicBezierInterpolator.EASE_BOTH.getInterpolation(f25 / 100.0f) * (-5.0f);
                                } else if (f25 < 484.0f) {
                                    interpolation = (CubicBezierInterpolator.EASE_BOTH.getInterpolation((f25 - 100.0f) / 384.0f) * 95.0f) - 5.0f;
                                }
                                f26 = interpolation;
                            }
                            canvas2.rotate(f26);
                            i20 = this.currentIcon;
                            if (i20 == 0) {
                                canvas2.scale(f20, f20);
                            } else {
                                canvas2.scale(f20, f20);
                            }
                            Theme.playPauseAnimator.draw(canvas2, this.paint2, f25);
                            canvas2.scale(1.0f, -1.0f);
                            Theme.playPauseAnimator.draw(canvas2, this.paint2, f25);
                            canvas2.restore();
                        }
                        if (i16 == i17) {
                            f24 = 1.0f;
                        } else {
                            f24 = 0.0f;
                        }
                        i18 = this.nextIcon;
                        if (i18 != 0) {
                            this.paint2.setAlpha(255);
                        } else {
                            this.paint2.setAlpha(255);
                        }
                        applyShaderMatrix(true);
                        canvas2.save();
                        canvas2.translate(rect.centerX() + (AndroidUtilities.dp(1.0f) * (1.0f - f24)), rect.centerY());
                        f25 = f24 * 500.0f;
                        i19 = this.currentIcon;
                        if (i19 == 1) {
                            f26 = 90.0f;
                        } else {
                            f26 = 0.0f;
                        }
                        if (i19 == 0) {
                            if (i19 == 1) {
                                if (f25 < 100.0f) {
                                    interpolation = CubicBezierInterpolator.EASE_BOTH.getInterpolation(f25 / 100.0f) * (-5.0f);
                                } else if (f25 < 484.0f) {
                                    interpolation = (CubicBezierInterpolator.EASE_BOTH.getInterpolation((f25 - 100.0f) / 384.0f) * 95.0f) - 5.0f;
                                }
                                f26 = interpolation;
                            }
                        } else if (i19 == 1) {
                            if (f25 < 100.0f) {
                                interpolation = CubicBezierInterpolator.EASE_BOTH.getInterpolation(f25 / 100.0f) * (-5.0f);
                            } else if (f25 < 484.0f) {
                                interpolation = (CubicBezierInterpolator.EASE_BOTH.getInterpolation((f25 - 100.0f) / 384.0f) * 95.0f) - 5.0f;
                            }
                            f26 = interpolation;
                        }
                        canvas2.rotate(f26);
                        i20 = this.currentIcon;
                        if (i20 == 0) {
                            canvas2.scale(f20, f20);
                        } else {
                            canvas2.scale(f20, f20);
                        }
                        Theme.playPauseAnimator.draw(canvas2, this.paint2, f25);
                        canvas2.scale(1.0f, -1.0f);
                        Theme.playPauseAnimator.draw(canvas2, this.paint2, f25);
                        canvas2.restore();
                    } else {
                        if (i16 == 1) {
                            if (this.nextIcon != 0) {
                                i17 = 1;
                            } else if (this.animatingTransition) {
                                if (this.nextIcon == 0) {
                                    f24 = 1.0f - this.transitionProgress;
                                } else {
                                    f24 = this.transitionProgress;
                                }
                                i17 = 1;
                            } else {
                                i17 = 1;
                                if (this.nextIcon == 1) {
                                    f24 = 1.0f;
                                } else {
                                    f24 = 0.0f;
                                }
                            }
                            i18 = this.nextIcon;
                            if (i18 != 0) {
                                this.paint2.setAlpha(255);
                            } else {
                                this.paint2.setAlpha(255);
                            }
                            applyShaderMatrix(true);
                            canvas2.save();
                            canvas2.translate(rect.centerX() + (AndroidUtilities.dp(1.0f) * (1.0f - f24)), rect.centerY());
                            f25 = f24 * 500.0f;
                            i19 = this.currentIcon;
                            if (i19 == 1) {
                                f26 = 90.0f;
                            } else {
                                f26 = 0.0f;
                            }
                            if (i19 == 0) {
                                if (i19 == 1) {
                                    if (f25 < 100.0f) {
                                        interpolation = CubicBezierInterpolator.EASE_BOTH.getInterpolation(f25 / 100.0f) * (-5.0f);
                                    } else if (f25 < 484.0f) {
                                        interpolation = (CubicBezierInterpolator.EASE_BOTH.getInterpolation((f25 - 100.0f) / 384.0f) * 95.0f) - 5.0f;
                                    }
                                    f26 = interpolation;
                                }
                            } else if (i19 == 1) {
                                if (f25 < 100.0f) {
                                    interpolation = CubicBezierInterpolator.EASE_BOTH.getInterpolation(f25 / 100.0f) * (-5.0f);
                                } else if (f25 < 484.0f) {
                                    interpolation = (CubicBezierInterpolator.EASE_BOTH.getInterpolation((f25 - 100.0f) / 384.0f) * 95.0f) - 5.0f;
                                }
                                f26 = interpolation;
                            }
                            canvas2.rotate(f26);
                            i20 = this.currentIcon;
                            if (i20 == 0) {
                                canvas2.scale(f20, f20);
                            } else {
                                canvas2.scale(f20, f20);
                            }
                            Theme.playPauseAnimator.draw(canvas2, this.paint2, f25);
                            canvas2.scale(1.0f, -1.0f);
                            Theme.playPauseAnimator.draw(canvas2, this.paint2, f25);
                            canvas2.restore();
                        }
                        if (i16 == i17) {
                            f24 = 1.0f;
                        } else {
                            f24 = 0.0f;
                        }
                        i18 = this.nextIcon;
                        if (i18 != 0) {
                            this.paint2.setAlpha(255);
                        } else {
                            this.paint2.setAlpha(255);
                        }
                        applyShaderMatrix(true);
                        canvas2.save();
                        canvas2.translate(rect.centerX() + (AndroidUtilities.dp(1.0f) * (1.0f - f24)), rect.centerY());
                        f25 = f24 * 500.0f;
                        i19 = this.currentIcon;
                        if (i19 == 1) {
                            f26 = 90.0f;
                        } else {
                            f26 = 0.0f;
                        }
                        if (i19 == 0) {
                            if (i19 == 1) {
                                if (f25 < 100.0f) {
                                    interpolation = CubicBezierInterpolator.EASE_BOTH.getInterpolation(f25 / 100.0f) * (-5.0f);
                                } else if (f25 < 484.0f) {
                                    interpolation = (CubicBezierInterpolator.EASE_BOTH.getInterpolation((f25 - 100.0f) / 384.0f) * 95.0f) - 5.0f;
                                }
                                f26 = interpolation;
                            }
                        } else if (i19 == 1) {
                            if (f25 < 100.0f) {
                                interpolation = CubicBezierInterpolator.EASE_BOTH.getInterpolation(f25 / 100.0f) * (-5.0f);
                            } else if (f25 < 484.0f) {
                                interpolation = (CubicBezierInterpolator.EASE_BOTH.getInterpolation((f25 - 100.0f) / 384.0f) * 95.0f) - 5.0f;
                            }
                            f26 = interpolation;
                        }
                        canvas2.rotate(f26);
                        i20 = this.currentIcon;
                        if (i20 == 0) {
                            canvas2.scale(f20, f20);
                        } else {
                            canvas2.scale(f20, f20);
                        }
                        Theme.playPauseAnimator.draw(canvas2, this.paint2, f25);
                        canvas2.scale(1.0f, -1.0f);
                        Theme.playPauseAnimator.draw(canvas2, this.paint2, f25);
                        canvas2.restore();
                    }
                    if (this.currentIcon != 6) {
                        applyShaderMatrix(false);
                        if (this.currentIcon != 6) {
                            f28 = this.transitionProgress;
                            if (f28 > 0.5f) {
                                f30 = (f28 - 0.5f) / 0.5f;
                                fMin2 = 1.0f - Math.min(1.0f, f30 / 0.5f);
                                if (f30 > 0.5f) {
                                    f29 = (f30 - 0.5f) / 0.5f;
                                } else {
                                    f29 = 0.0f;
                                }
                            } else {
                                f29 = 0.0f;
                                fMin2 = 1.0f;
                            }
                            this.paint.setAlpha(255);
                            f27 = f29;
                        } else {
                            if (this.nextIcon != 6) {
                                this.paint.setAlpha((int) ((1.0f - this.transitionProgress) * 255.0f));
                            } else {
                                this.paint.setAlpha(255);
                            }
                            fMin2 = 0.0f;
                            f27 = 1.0f;
                        }
                        iDp = AndroidUtilities.dp(7.0f) + iCenterY3;
                        iDp2 = iCenterX - AndroidUtilities.dp(3.0f);
                        if (fMin2 < 1.0f) {
                            i21 = iDp2;
                            canvas.drawLine(iDp2 - AndroidUtilities.dp(6.0f), iDp - AndroidUtilities.dp(6.0f), iDp2 - (AndroidUtilities.dp(6.0f) * fMin2), iDp - (AndroidUtilities.dp(6.0f) * fMin2), this.paint);
                        } else {
                            i21 = iDp2;
                        }
                        if (f27 > 0.0f) {
                            float f924 = i21;
                            float f925 = iDp;
                            canvas2 = canvas;
                            canvas2.drawLine(f924, f925, f924 + (AndroidUtilities.dp(12.0f) * f27), f925 - (AndroidUtilities.dp(12.0f) * f27), this.paint);
                        } else {
                            canvas2 = canvas;
                        }
                    } else {
                        applyShaderMatrix(false);
                        if (this.currentIcon != 6) {
                            f28 = this.transitionProgress;
                            if (f28 > 0.5f) {
                                f30 = (f28 - 0.5f) / 0.5f;
                                fMin2 = 1.0f - Math.min(1.0f, f30 / 0.5f);
                                if (f30 > 0.5f) {
                                    f29 = (f30 - 0.5f) / 0.5f;
                                } else {
                                    f29 = 0.0f;
                                }
                            } else {
                                f29 = 0.0f;
                                fMin2 = 1.0f;
                            }
                            this.paint.setAlpha(255);
                            f27 = f29;
                        } else {
                            if (this.nextIcon != 6) {
                                this.paint.setAlpha((int) ((1.0f - this.transitionProgress) * 255.0f));
                            } else {
                                this.paint.setAlpha(255);
                            }
                            fMin2 = 0.0f;
                            f27 = 1.0f;
                        }
                        iDp = AndroidUtilities.dp(7.0f) + iCenterY3;
                        iDp2 = iCenterX - AndroidUtilities.dp(3.0f);
                        if (fMin2 < 1.0f) {
                            i21 = iDp2;
                            canvas.drawLine(iDp2 - AndroidUtilities.dp(6.0f), iDp - AndroidUtilities.dp(6.0f), iDp2 - (AndroidUtilities.dp(6.0f) * fMin2), iDp - (AndroidUtilities.dp(6.0f) * fMin2), this.paint);
                        } else {
                            i21 = iDp2;
                        }
                        if (f27 > 0.0f) {
                            float f926 = i21;
                            float f927 = iDp;
                            canvas2 = canvas;
                            canvas2.drawLine(f926, f927, f926 + (AndroidUtilities.dp(12.0f) * f27), f927 - (AndroidUtilities.dp(12.0f) * f27), this.paint);
                        } else {
                            canvas2 = canvas;
                        }
                    }
                    if (drawable3 != null) {
                        int intrinsicWidth5 = (int) (drawable3.getIntrinsicWidth() * f19);
                        int intrinsicHeight5 = (int) (drawable3.getIntrinsicHeight() * f19);
                        drawable3.setColorFilter(this.colorFilter);
                        if (this.currentIcon == this.nextIcon) {
                            i27 = 255;
                        } else {
                            i27 = (int) ((1.0f - this.transitionProgress) * 255.0f);
                        }
                        drawable3.setAlpha(i27);
                        int i416 = intrinsicWidth5 / 2;
                        int i417 = intrinsicHeight5 / 2;
                        drawable3.setBounds(iCenterX - i416, iCenterY3 - i417, i416 + iCenterX, i417 + iCenterY3);
                        drawable3.draw(canvas2);
                    }
                    if (drawable4 != null) {
                        int intrinsicWidth6 = (int) (drawable4.getIntrinsicWidth() * f20);
                        int intrinsicHeight6 = (int) (drawable4.getIntrinsicHeight() * f20);
                        drawable4.setColorFilter(this.colorFilter);
                        if (this.currentIcon == this.nextIcon) {
                            i26 = 255;
                        } else {
                            i26 = (int) (this.transitionProgress * 255.0f);
                        }
                        drawable4.setAlpha(i26);
                        int i57 = intrinsicWidth6 / 2;
                        int i58 = intrinsicHeight6 / 2;
                        drawable4.setBounds(iCenterX - i57, iCenterY3 - i58, i57 + iCenterX, i58 + iCenterY3);
                        drawable4.draw(canvas2);
                    }
                    if (pathArr4 != null) {
                        int iDp115 = AndroidUtilities.dp(24.0f);
                        this.paint2.setStyle(Paint.Style.FILL_AND_STROKE);
                        Paint paint13 = this.paint2;
                        if (this.currentIcon == this.nextIcon) {
                            i25 = 255;
                        } else {
                            i25 = (int) ((1.0f - this.transitionProgress) * 255.0f);
                        }
                        paint13.setAlpha(i25);
                        applyShaderMatrix(true);
                        canvas2.save();
                        canvas2.translate(iCenterX, iCenterY3);
                        canvas2.scale(f19, f19);
                        float f1010 = (-iDp115) / 2;
                        canvas2.translate(f1010, f1010);
                        path4 = pathArr4[0];
                        if (path4 != null) {
                            canvas2.drawPath(path4, this.paint2);
                        }
                        path5 = pathArr4[1];
                        if (path5 != null) {
                            canvas2.drawPath(path5, this.backPaint);
                        }
                        canvas2.restore();
                    }
                    if (pathArr3 != null) {
                        int iDp116 = AndroidUtilities.dp(24.0f);
                        if (this.currentIcon == this.nextIcon) {
                            i24 = 255;
                        } else {
                            i24 = (int) (this.transitionProgress * 255.0f);
                        }
                        this.paint2.setStyle(Paint.Style.FILL_AND_STROKE);
                        this.paint2.setAlpha(i24);
                        applyShaderMatrix(true);
                        canvas2.save();
                        canvas2.translate(iCenterX, iCenterY3);
                        float f1011 = f20;
                        canvas2.scale(f1011, f1011);
                        float f1012 = (-iDp116) / 2;
                        canvas2.translate(f1012, f1012);
                        path = pathArr3[0];
                        if (path != null) {
                            canvas2.drawPath(path, this.paint2);
                        }
                        if (pathArr3.length >= 3) {
                            canvas2.drawPath(path3, this.paint);
                        }
                        path2 = pathArr3[1];
                        if (path2 != null) {
                            if (i24 != 255) {
                                int alpha3 = this.backPaint.getAlpha();
                                this.backPaint.setAlpha((int) (alpha3 * (i24 / 255.0f)));
                                canvas2.drawPath(pathArr3[1], this.backPaint);
                                this.backPaint.setAlpha(alpha3);
                            } else {
                                canvas2.drawPath(path2, this.backPaint);
                            }
                        }
                        canvas2.restore();
                    }
                    long jCurrentTimeMillis3 = System.currentTimeMillis();
                    j = jCurrentTimeMillis3 - this.lastAnimationTime;
                    if (j > 17) {
                        j = 17;
                    }
                    this.lastAnimationTime = jCurrentTimeMillis3;
                    i22 = this.currentIcon;
                    if (i22 != 3) {
                        float f1013 = this.downloadRadOffset + ((360 * j) / 2500.0f);
                        this.downloadRadOffset = f1013;
                        this.downloadRadOffset = getCircleValue(f1013);
                        if (this.nextIcon != 2) {
                            f31 = this.downloadProgress;
                            f32 = this.downloadProgressAnimationStart;
                            f33 = f31 - f32;
                            if (f33 > 0.0f) {
                                f34 = this.downloadProgressTime + j;
                                this.downloadProgressTime = f34;
                                if (f34 >= 200.0f) {
                                    this.animatedDownloadProgress = f31;
                                    this.downloadProgressAnimationStart = f31;
                                    this.downloadProgressTime = 0.0f;
                                } else {
                                    this.animatedDownloadProgress = f32 + (f33 * this.interpolator.getInterpolation(f34 / 200.0f));
                                }
                            }
                        }
                        invalidateSelf();
                    } else {
                        float f1014 = this.downloadRadOffset + ((360 * j) / 2500.0f);
                        this.downloadRadOffset = f1014;
                        this.downloadRadOffset = getCircleValue(f1014);
                        if (this.nextIcon != 2) {
                            f31 = this.downloadProgress;
                            f32 = this.downloadProgressAnimationStart;
                            f33 = f31 - f32;
                            if (f33 > 0.0f) {
                                f34 = this.downloadProgressTime + j;
                                this.downloadProgressTime = f34;
                                if (f34 >= 200.0f) {
                                    this.animatedDownloadProgress = f31;
                                    this.downloadProgressAnimationStart = f31;
                                    this.downloadProgressTime = 0.0f;
                                } else {
                                    this.animatedDownloadProgress = f32 + (f33 * this.interpolator.getInterpolation(f34 / 200.0f));
                                }
                            }
                        }
                        invalidateSelf();
                    }
                    if (this.animatingTransition) {
                        f35 = this.transitionProgress;
                        if (f35 < 1.0f) {
                            f36 = f35 + (j / this.transitionAnimationTime);
                            this.transitionProgress = f36;
                            if (f36 >= 1.0f) {
                                this.currentIcon = this.nextIcon;
                                this.transitionProgress = 1.0f;
                                this.animatingTransition = false;
                            }
                            invalidateSelf();
                        }
                    }
                    i23 = i11;
                    if (i23 >= 1) {
                        canvas2.restoreToCount(i23);
                    }
                }
                pathArr2 = null;
                if (i4 == 5) {
                    pathArr = Theme.chat_filePath;
                } else if (this.currentIcon == 5) {
                    pathArr2 = Theme.chat_filePath;
                }
                pathArr3 = pathArr;
                pathArr4 = pathArr2;
                if (i4 == 7) {
                    drawable2 = Theme.chat_flameIcon;
                    drawable = null;
                    i5 = 8;
                } else {
                    if (this.currentIcon == 7) {
                        drawable = Theme.chat_flameIcon;
                    } else {
                        drawable = null;
                    }
                    i5 = 8;
                    drawable2 = null;
                }
                if (i4 == i5) {
                    drawable2 = Theme.chat_gifIcon;
                } else if (this.currentIcon == i5) {
                    drawable = Theme.chat_gifIcon;
                }
                drawable3 = drawable;
                if (this.currentIcon != 9) {
                    applyShaderMatrix(false);
                    Paint paint14 = this.paint;
                    if (this.currentIcon == this.nextIcon) {
                        i6 = 255;
                    } else {
                        i6 = (int) (this.transitionProgress * 255.0f);
                    }
                    paint14.setAlpha(i6);
                    int iDp117 = AndroidUtilities.dp(7.0f) + iCenterY3;
                    int iDp118 = iCenterX - AndroidUtilities.dp(3.0f);
                    if (this.currentIcon != this.nextIcon) {
                        canvas2.save();
                        float f814 = this.transitionProgress;
                        canvas2.scale(f814, f814, iCenterX, iCenterY3);
                    }
                    float f91120 = iDp118;
                    float f91121 = iDp117;
                    rect = bounds;
                    drawable4 = drawable2;
                    canvas2.drawLine(iDp118 - AndroidUtilities.dp(6.0f), iDp117 - AndroidUtilities.dp(6.0f), f91120, f91121, this.paint);
                    canvas2 = canvas;
                    canvas2.drawLine(f91120, f91121, iDp118 + AndroidUtilities.dp(12.0f), iDp117 - AndroidUtilities.dp(12.0f), this.paint);
                    if (this.currentIcon != this.nextIcon) {
                        canvas2.restore();
                    }
                } else {
                    applyShaderMatrix(false);
                    Paint paint15 = this.paint;
                    if (this.currentIcon == this.nextIcon) {
                        i6 = 255;
                    } else {
                        i6 = (int) (this.transitionProgress * 255.0f);
                    }
                    paint15.setAlpha(i6);
                    int iDp119 = AndroidUtilities.dp(7.0f) + iCenterY3;
                    int iDp1110 = iCenterX - AndroidUtilities.dp(3.0f);
                    if (this.currentIcon != this.nextIcon) {
                        canvas2.save();
                        float f815 = this.transitionProgress;
                        canvas2.scale(f815, f815, iCenterX, iCenterY3);
                    }
                    float f91122 = iDp1110;
                    float f91123 = iDp119;
                    rect = bounds;
                    drawable4 = drawable2;
                    canvas2.drawLine(iDp1110 - AndroidUtilities.dp(6.0f), iDp119 - AndroidUtilities.dp(6.0f), f91122, f91123, this.paint);
                    canvas2 = canvas;
                    canvas2.drawLine(f91122, f91123, iDp1110 + AndroidUtilities.dp(12.0f), iDp119 - AndroidUtilities.dp(12.0f), this.paint);
                    if (this.currentIcon != this.nextIcon) {
                        canvas2.restore();
                    }
                }
                if (this.currentIcon != 12) {
                    applyShaderMatrix(false);
                    i7 = this.currentIcon;
                    i8 = this.nextIcon;
                    if (i7 == i8) {
                        i9 = 13;
                        f21 = 1.0f;
                    } else {
                        i9 = 13;
                        if (i8 == 13) {
                            f21 = this.transitionProgress;
                        } else {
                            f21 = 1.0f - this.transitionProgress;
                        }
                    }
                    Paint paint16 = this.paint;
                    if (i7 == i8) {
                        i10 = 255;
                    } else {
                        i10 = (int) (f21 * 255.0f);
                    }
                    paint16.setAlpha(i10);
                    AndroidUtilities.dp(7.0f);
                    AndroidUtilities.dp(3.0f);
                    if (this.currentIcon != this.nextIcon) {
                        canvas2.save();
                        canvas2.scale(f21, f21, iCenterX, iCenterY3);
                    }
                    float fDp113 = AndroidUtilities.dp(7.0f) * this.scale;
                    float f911112 = iCenterX;
                    float f911113 = f911112 - fDp113;
                    float f911114 = iCenterY3;
                    float f911115 = f911114 - fDp113;
                    float f911116 = f911112 + fDp113;
                    float f911117 = fDp113 + f911114;
                    i11 = i;
                    i12 = i9;
                    canvas2.drawLine(f911113, f911115, f911116, f911117, this.paint);
                    canvas2 = canvas;
                    canvas2.drawLine(f911116, f911115, f911113, f911117, this.paint);
                    if (this.currentIcon != this.nextIcon) {
                        canvas2.restore();
                    }
                } else {
                    applyShaderMatrix(false);
                    i7 = this.currentIcon;
                    i8 = this.nextIcon;
                    if (i7 == i8) {
                        i9 = 13;
                        f21 = 1.0f;
                    } else {
                        i9 = 13;
                        if (i8 == 13) {
                            f21 = this.transitionProgress;
                        } else {
                            f21 = 1.0f - this.transitionProgress;
                        }
                    }
                    Paint paint17 = this.paint;
                    if (i7 == i8) {
                        i10 = 255;
                    } else {
                        i10 = (int) (f21 * 255.0f);
                    }
                    paint17.setAlpha(i10);
                    AndroidUtilities.dp(7.0f);
                    AndroidUtilities.dp(3.0f);
                    if (this.currentIcon != this.nextIcon) {
                        canvas2.save();
                        canvas2.scale(f21, f21, iCenterX, iCenterY3);
                    }
                    float fDp114 = AndroidUtilities.dp(7.0f) * this.scale;
                    float f911118 = iCenterX;
                    float f911119 = f911118 - fDp114;
                    float f9111110 = iCenterY3;
                    float f9111111 = f9111110 - fDp114;
                    float f9111112 = f911118 + fDp114;
                    float f9111113 = fDp114 + f9111110;
                    i11 = i;
                    i12 = i9;
                    canvas2.drawLine(f911119, f9111111, f9111112, f9111113, this.paint);
                    canvas2 = canvas;
                    canvas2.drawLine(f9111112, f9111111, f911119, f9111113, this.paint);
                    if (this.currentIcon != this.nextIcon) {
                        canvas2.restore();
                    }
                }
                if (this.currentIcon != i12) {
                    applyShaderMatrix(false);
                    i13 = this.currentIcon;
                    i14 = this.nextIcon;
                    if (i13 == i14) {
                        f22 = 1.0f;
                    } else if (i14 == i12) {
                        f22 = this.transitionProgress;
                    } else {
                        f22 = 1.0f - this.transitionProgress;
                    }
                    this.textPaint.setAlpha((int) (f22 * 255.0f));
                    int iDp1111 = AndroidUtilities.dp(5.0f) + iCenterY3;
                    int i418 = iCenterX - (this.percentStringWidth / 2);
                    f23 = 5.0f;
                    if (this.currentIcon != this.nextIcon) {
                        canvas2.save();
                        canvas2.scale(f22, f22, iCenterX, iCenterY3);
                    }
                    i15 = (int) (this.animatedDownloadProgress * 100.0f);
                    if (this.percentString != null) {
                        this.lastPercent = i15;
                        String str10 = String.format("%d%%", Integer.valueOf(i15));
                        this.percentString = str10;
                        this.percentStringWidth = (int) Math.ceil(this.textPaint.measureText(str10));
                    } else {
                        this.lastPercent = i15;
                        String str11 = String.format("%d%%", Integer.valueOf(i15));
                        this.percentString = str11;
                        this.percentStringWidth = (int) Math.ceil(this.textPaint.measureText(str11));
                    }
                    canvas2.drawText(this.percentString, i418, iDp1111, this.textPaint);
                    if (this.currentIcon != this.nextIcon) {
                        canvas2.restore();
                    }
                } else {
                    applyShaderMatrix(false);
                    i13 = this.currentIcon;
                    i14 = this.nextIcon;
                    if (i13 == i14) {
                        f22 = 1.0f;
                    } else if (i14 == i12) {
                        f22 = this.transitionProgress;
                    } else {
                        f22 = 1.0f - this.transitionProgress;
                    }
                    this.textPaint.setAlpha((int) (f22 * 255.0f));
                    int iDp1112 = AndroidUtilities.dp(5.0f) + iCenterY3;
                    int i419 = iCenterX - (this.percentStringWidth / 2);
                    f23 = 5.0f;
                    if (this.currentIcon != this.nextIcon) {
                        canvas2.save();
                        canvas2.scale(f22, f22, iCenterX, iCenterY3);
                    }
                    i15 = (int) (this.animatedDownloadProgress * 100.0f);
                    if (this.percentString != null) {
                        this.lastPercent = i15;
                        String str12 = String.format("%d%%", Integer.valueOf(i15));
                        this.percentString = str12;
                        this.percentStringWidth = (int) Math.ceil(this.textPaint.measureText(str12));
                    } else {
                        this.lastPercent = i15;
                        String str13 = String.format("%d%%", Integer.valueOf(i15));
                        this.percentString = str13;
                        this.percentStringWidth = (int) Math.ceil(this.textPaint.measureText(str13));
                    }
                    canvas2.drawText(this.percentString, i419, iDp1112, this.textPaint);
                    if (this.currentIcon != this.nextIcon) {
                        canvas2.restore();
                    }
                }
                i16 = this.currentIcon;
                i17 = 1;
                if (i16 != 0) {
                    if (i16 == 0) {
                        if (i16 == 1) {
                            if (this.nextIcon != 0) {
                                i17 = 1;
                            } else if (this.animatingTransition) {
                                if (this.nextIcon == 0) {
                                    f24 = 1.0f - this.transitionProgress;
                                } else {
                                    f24 = this.transitionProgress;
                                }
                                i17 = 1;
                            } else {
                                i17 = 1;
                                if (this.nextIcon == 1) {
                                    f24 = 1.0f;
                                } else {
                                    f24 = 0.0f;
                                }
                            }
                            i18 = this.nextIcon;
                            if (i18 != 0) {
                                this.paint2.setAlpha(255);
                            } else {
                                this.paint2.setAlpha(255);
                            }
                            applyShaderMatrix(true);
                            canvas2.save();
                            canvas2.translate(rect.centerX() + (AndroidUtilities.dp(1.0f) * (1.0f - f24)), rect.centerY());
                            f25 = f24 * 500.0f;
                            i19 = this.currentIcon;
                            if (i19 == 1) {
                                f26 = 90.0f;
                            } else {
                                f26 = 0.0f;
                            }
                            if (i19 == 0) {
                                if (i19 == 1) {
                                    if (f25 < 100.0f) {
                                        interpolation = CubicBezierInterpolator.EASE_BOTH.getInterpolation(f25 / 100.0f) * (-5.0f);
                                    } else if (f25 < 484.0f) {
                                        interpolation = (CubicBezierInterpolator.EASE_BOTH.getInterpolation((f25 - 100.0f) / 384.0f) * 95.0f) - 5.0f;
                                    }
                                    f26 = interpolation;
                                }
                            } else if (i19 == 1) {
                                if (f25 < 100.0f) {
                                    interpolation = CubicBezierInterpolator.EASE_BOTH.getInterpolation(f25 / 100.0f) * (-5.0f);
                                } else if (f25 < 484.0f) {
                                    interpolation = (CubicBezierInterpolator.EASE_BOTH.getInterpolation((f25 - 100.0f) / 384.0f) * 95.0f) - 5.0f;
                                }
                                f26 = interpolation;
                            }
                            canvas2.rotate(f26);
                            i20 = this.currentIcon;
                            if (i20 == 0) {
                                canvas2.scale(f20, f20);
                            } else {
                                canvas2.scale(f20, f20);
                            }
                            Theme.playPauseAnimator.draw(canvas2, this.paint2, f25);
                            canvas2.scale(1.0f, -1.0f);
                            Theme.playPauseAnimator.draw(canvas2, this.paint2, f25);
                            canvas2.restore();
                        }
                        if (i16 == i17) {
                            f24 = 1.0f;
                        } else {
                            f24 = 0.0f;
                        }
                        i18 = this.nextIcon;
                        if (i18 != 0) {
                            this.paint2.setAlpha(255);
                        } else {
                            this.paint2.setAlpha(255);
                        }
                        applyShaderMatrix(true);
                        canvas2.save();
                        canvas2.translate(rect.centerX() + (AndroidUtilities.dp(1.0f) * (1.0f - f24)), rect.centerY());
                        f25 = f24 * 500.0f;
                        i19 = this.currentIcon;
                        if (i19 == 1) {
                            f26 = 90.0f;
                        } else {
                            f26 = 0.0f;
                        }
                        if (i19 == 0) {
                            if (i19 == 1) {
                                if (f25 < 100.0f) {
                                    interpolation = CubicBezierInterpolator.EASE_BOTH.getInterpolation(f25 / 100.0f) * (-5.0f);
                                } else if (f25 < 484.0f) {
                                    interpolation = (CubicBezierInterpolator.EASE_BOTH.getInterpolation((f25 - 100.0f) / 384.0f) * 95.0f) - 5.0f;
                                }
                                f26 = interpolation;
                            }
                        } else if (i19 == 1) {
                            if (f25 < 100.0f) {
                                interpolation = CubicBezierInterpolator.EASE_BOTH.getInterpolation(f25 / 100.0f) * (-5.0f);
                            } else if (f25 < 484.0f) {
                                interpolation = (CubicBezierInterpolator.EASE_BOTH.getInterpolation((f25 - 100.0f) / 384.0f) * 95.0f) - 5.0f;
                            }
                            f26 = interpolation;
                        }
                        canvas2.rotate(f26);
                        i20 = this.currentIcon;
                        if (i20 == 0) {
                            canvas2.scale(f20, f20);
                        } else {
                            canvas2.scale(f20, f20);
                        }
                        Theme.playPauseAnimator.draw(canvas2, this.paint2, f25);
                        canvas2.scale(1.0f, -1.0f);
                        Theme.playPauseAnimator.draw(canvas2, this.paint2, f25);
                        canvas2.restore();
                    } else {
                        if (i16 == 1) {
                            if (this.nextIcon != 0) {
                                i17 = 1;
                            } else if (this.animatingTransition) {
                                if (this.nextIcon == 0) {
                                    f24 = 1.0f - this.transitionProgress;
                                } else {
                                    f24 = this.transitionProgress;
                                }
                                i17 = 1;
                            } else {
                                i17 = 1;
                                if (this.nextIcon == 1) {
                                    f24 = 1.0f;
                                } else {
                                    f24 = 0.0f;
                                }
                            }
                            i18 = this.nextIcon;
                            if (i18 != 0) {
                                this.paint2.setAlpha(255);
                            } else {
                                this.paint2.setAlpha(255);
                            }
                            applyShaderMatrix(true);
                            canvas2.save();
                            canvas2.translate(rect.centerX() + (AndroidUtilities.dp(1.0f) * (1.0f - f24)), rect.centerY());
                            f25 = f24 * 500.0f;
                            i19 = this.currentIcon;
                            if (i19 == 1) {
                                f26 = 90.0f;
                            } else {
                                f26 = 0.0f;
                            }
                            if (i19 == 0) {
                                if (i19 == 1) {
                                    if (f25 < 100.0f) {
                                        interpolation = CubicBezierInterpolator.EASE_BOTH.getInterpolation(f25 / 100.0f) * (-5.0f);
                                    } else if (f25 < 484.0f) {
                                        interpolation = (CubicBezierInterpolator.EASE_BOTH.getInterpolation((f25 - 100.0f) / 384.0f) * 95.0f) - 5.0f;
                                    }
                                    f26 = interpolation;
                                }
                            } else if (i19 == 1) {
                                if (f25 < 100.0f) {
                                    interpolation = CubicBezierInterpolator.EASE_BOTH.getInterpolation(f25 / 100.0f) * (-5.0f);
                                } else if (f25 < 484.0f) {
                                    interpolation = (CubicBezierInterpolator.EASE_BOTH.getInterpolation((f25 - 100.0f) / 384.0f) * 95.0f) - 5.0f;
                                }
                                f26 = interpolation;
                            }
                            canvas2.rotate(f26);
                            i20 = this.currentIcon;
                            if (i20 == 0) {
                                canvas2.scale(f20, f20);
                            } else {
                                canvas2.scale(f20, f20);
                            }
                            Theme.playPauseAnimator.draw(canvas2, this.paint2, f25);
                            canvas2.scale(1.0f, -1.0f);
                            Theme.playPauseAnimator.draw(canvas2, this.paint2, f25);
                            canvas2.restore();
                        }
                        if (i16 == i17) {
                            f24 = 1.0f;
                        } else {
                            f24 = 0.0f;
                        }
                        i18 = this.nextIcon;
                        if (i18 != 0) {
                            this.paint2.setAlpha(255);
                        } else {
                            this.paint2.setAlpha(255);
                        }
                        applyShaderMatrix(true);
                        canvas2.save();
                        canvas2.translate(rect.centerX() + (AndroidUtilities.dp(1.0f) * (1.0f - f24)), rect.centerY());
                        f25 = f24 * 500.0f;
                        i19 = this.currentIcon;
                        if (i19 == 1) {
                            f26 = 90.0f;
                        } else {
                            f26 = 0.0f;
                        }
                        if (i19 == 0) {
                            if (i19 == 1) {
                                if (f25 < 100.0f) {
                                    interpolation = CubicBezierInterpolator.EASE_BOTH.getInterpolation(f25 / 100.0f) * (-5.0f);
                                } else if (f25 < 484.0f) {
                                    interpolation = (CubicBezierInterpolator.EASE_BOTH.getInterpolation((f25 - 100.0f) / 384.0f) * 95.0f) - 5.0f;
                                }
                                f26 = interpolation;
                            }
                        } else if (i19 == 1) {
                            if (f25 < 100.0f) {
                                interpolation = CubicBezierInterpolator.EASE_BOTH.getInterpolation(f25 / 100.0f) * (-5.0f);
                            } else if (f25 < 484.0f) {
                                interpolation = (CubicBezierInterpolator.EASE_BOTH.getInterpolation((f25 - 100.0f) / 384.0f) * 95.0f) - 5.0f;
                            }
                            f26 = interpolation;
                        }
                        canvas2.rotate(f26);
                        i20 = this.currentIcon;
                        if (i20 == 0) {
                            canvas2.scale(f20, f20);
                        } else {
                            canvas2.scale(f20, f20);
                        }
                        Theme.playPauseAnimator.draw(canvas2, this.paint2, f25);
                        canvas2.scale(1.0f, -1.0f);
                        Theme.playPauseAnimator.draw(canvas2, this.paint2, f25);
                        canvas2.restore();
                    }
                } else if (i16 == 0) {
                    if (i16 == 1) {
                        if (this.nextIcon != 0) {
                            i17 = 1;
                        } else if (this.animatingTransition) {
                            if (this.nextIcon == 0) {
                                f24 = 1.0f - this.transitionProgress;
                            } else {
                                f24 = this.transitionProgress;
                            }
                            i17 = 1;
                        } else {
                            i17 = 1;
                            if (this.nextIcon == 1) {
                                f24 = 1.0f;
                            } else {
                                f24 = 0.0f;
                            }
                        }
                        i18 = this.nextIcon;
                        if (i18 != 0) {
                            this.paint2.setAlpha(255);
                        } else {
                            this.paint2.setAlpha(255);
                        }
                        applyShaderMatrix(true);
                        canvas2.save();
                        canvas2.translate(rect.centerX() + (AndroidUtilities.dp(1.0f) * (1.0f - f24)), rect.centerY());
                        f25 = f24 * 500.0f;
                        i19 = this.currentIcon;
                        if (i19 == 1) {
                            f26 = 90.0f;
                        } else {
                            f26 = 0.0f;
                        }
                        if (i19 == 0) {
                            if (i19 == 1) {
                                if (f25 < 100.0f) {
                                    interpolation = CubicBezierInterpolator.EASE_BOTH.getInterpolation(f25 / 100.0f) * (-5.0f);
                                } else if (f25 < 484.0f) {
                                    interpolation = (CubicBezierInterpolator.EASE_BOTH.getInterpolation((f25 - 100.0f) / 384.0f) * 95.0f) - 5.0f;
                                }
                                f26 = interpolation;
                            }
                        } else if (i19 == 1) {
                            if (f25 < 100.0f) {
                                interpolation = CubicBezierInterpolator.EASE_BOTH.getInterpolation(f25 / 100.0f) * (-5.0f);
                            } else if (f25 < 484.0f) {
                                interpolation = (CubicBezierInterpolator.EASE_BOTH.getInterpolation((f25 - 100.0f) / 384.0f) * 95.0f) - 5.0f;
                            }
                            f26 = interpolation;
                        }
                        canvas2.rotate(f26);
                        i20 = this.currentIcon;
                        if (i20 == 0) {
                            canvas2.scale(f20, f20);
                        } else {
                            canvas2.scale(f20, f20);
                        }
                        Theme.playPauseAnimator.draw(canvas2, this.paint2, f25);
                        canvas2.scale(1.0f, -1.0f);
                        Theme.playPauseAnimator.draw(canvas2, this.paint2, f25);
                        canvas2.restore();
                    }
                    if (i16 == i17) {
                        f24 = 1.0f;
                    } else {
                        f24 = 0.0f;
                    }
                    i18 = this.nextIcon;
                    if (i18 != 0) {
                        this.paint2.setAlpha(255);
                    } else {
                        this.paint2.setAlpha(255);
                    }
                    applyShaderMatrix(true);
                    canvas2.save();
                    canvas2.translate(rect.centerX() + (AndroidUtilities.dp(1.0f) * (1.0f - f24)), rect.centerY());
                    f25 = f24 * 500.0f;
                    i19 = this.currentIcon;
                    if (i19 == 1) {
                        f26 = 90.0f;
                    } else {
                        f26 = 0.0f;
                    }
                    if (i19 == 0) {
                        if (i19 == 1) {
                            if (f25 < 100.0f) {
                                interpolation = CubicBezierInterpolator.EASE_BOTH.getInterpolation(f25 / 100.0f) * (-5.0f);
                            } else if (f25 < 484.0f) {
                                interpolation = (CubicBezierInterpolator.EASE_BOTH.getInterpolation((f25 - 100.0f) / 384.0f) * 95.0f) - 5.0f;
                            }
                            f26 = interpolation;
                        }
                    } else if (i19 == 1) {
                        if (f25 < 100.0f) {
                            interpolation = CubicBezierInterpolator.EASE_BOTH.getInterpolation(f25 / 100.0f) * (-5.0f);
                        } else if (f25 < 484.0f) {
                            interpolation = (CubicBezierInterpolator.EASE_BOTH.getInterpolation((f25 - 100.0f) / 384.0f) * 95.0f) - 5.0f;
                        }
                        f26 = interpolation;
                    }
                    canvas2.rotate(f26);
                    i20 = this.currentIcon;
                    if (i20 == 0) {
                        canvas2.scale(f20, f20);
                    } else {
                        canvas2.scale(f20, f20);
                    }
                    Theme.playPauseAnimator.draw(canvas2, this.paint2, f25);
                    canvas2.scale(1.0f, -1.0f);
                    Theme.playPauseAnimator.draw(canvas2, this.paint2, f25);
                    canvas2.restore();
                } else {
                    if (i16 == 1) {
                        if (this.nextIcon != 0) {
                            i17 = 1;
                        } else if (this.animatingTransition) {
                            if (this.nextIcon == 0) {
                                f24 = 1.0f - this.transitionProgress;
                            } else {
                                f24 = this.transitionProgress;
                            }
                            i17 = 1;
                        } else {
                            i17 = 1;
                            if (this.nextIcon == 1) {
                                f24 = 1.0f;
                            } else {
                                f24 = 0.0f;
                            }
                        }
                        i18 = this.nextIcon;
                        if (i18 != 0) {
                            this.paint2.setAlpha(255);
                        } else {
                            this.paint2.setAlpha(255);
                        }
                        applyShaderMatrix(true);
                        canvas2.save();
                        canvas2.translate(rect.centerX() + (AndroidUtilities.dp(1.0f) * (1.0f - f24)), rect.centerY());
                        f25 = f24 * 500.0f;
                        i19 = this.currentIcon;
                        if (i19 == 1) {
                            f26 = 90.0f;
                        } else {
                            f26 = 0.0f;
                        }
                        if (i19 == 0) {
                            if (i19 == 1) {
                                if (f25 < 100.0f) {
                                    interpolation = CubicBezierInterpolator.EASE_BOTH.getInterpolation(f25 / 100.0f) * (-5.0f);
                                } else if (f25 < 484.0f) {
                                    interpolation = (CubicBezierInterpolator.EASE_BOTH.getInterpolation((f25 - 100.0f) / 384.0f) * 95.0f) - 5.0f;
                                }
                                f26 = interpolation;
                            }
                        } else if (i19 == 1) {
                            if (f25 < 100.0f) {
                                interpolation = CubicBezierInterpolator.EASE_BOTH.getInterpolation(f25 / 100.0f) * (-5.0f);
                            } else if (f25 < 484.0f) {
                                interpolation = (CubicBezierInterpolator.EASE_BOTH.getInterpolation((f25 - 100.0f) / 384.0f) * 95.0f) - 5.0f;
                            }
                            f26 = interpolation;
                        }
                        canvas2.rotate(f26);
                        i20 = this.currentIcon;
                        if (i20 == 0) {
                            canvas2.scale(f20, f20);
                        } else {
                            canvas2.scale(f20, f20);
                        }
                        Theme.playPauseAnimator.draw(canvas2, this.paint2, f25);
                        canvas2.scale(1.0f, -1.0f);
                        Theme.playPauseAnimator.draw(canvas2, this.paint2, f25);
                        canvas2.restore();
                    }
                    if (i16 == i17) {
                        f24 = 1.0f;
                    } else {
                        f24 = 0.0f;
                    }
                    i18 = this.nextIcon;
                    if (i18 != 0) {
                        this.paint2.setAlpha(255);
                    } else {
                        this.paint2.setAlpha(255);
                    }
                    applyShaderMatrix(true);
                    canvas2.save();
                    canvas2.translate(rect.centerX() + (AndroidUtilities.dp(1.0f) * (1.0f - f24)), rect.centerY());
                    f25 = f24 * 500.0f;
                    i19 = this.currentIcon;
                    if (i19 == 1) {
                        f26 = 90.0f;
                    } else {
                        f26 = 0.0f;
                    }
                    if (i19 == 0) {
                        if (i19 == 1) {
                            if (f25 < 100.0f) {
                                interpolation = CubicBezierInterpolator.EASE_BOTH.getInterpolation(f25 / 100.0f) * (-5.0f);
                            } else if (f25 < 484.0f) {
                                interpolation = (CubicBezierInterpolator.EASE_BOTH.getInterpolation((f25 - 100.0f) / 384.0f) * 95.0f) - 5.0f;
                            }
                            f26 = interpolation;
                        }
                    } else if (i19 == 1) {
                        if (f25 < 100.0f) {
                            interpolation = CubicBezierInterpolator.EASE_BOTH.getInterpolation(f25 / 100.0f) * (-5.0f);
                        } else if (f25 < 484.0f) {
                            interpolation = (CubicBezierInterpolator.EASE_BOTH.getInterpolation((f25 - 100.0f) / 384.0f) * 95.0f) - 5.0f;
                        }
                        f26 = interpolation;
                    }
                    canvas2.rotate(f26);
                    i20 = this.currentIcon;
                    if (i20 == 0) {
                        canvas2.scale(f20, f20);
                    } else {
                        canvas2.scale(f20, f20);
                    }
                    Theme.playPauseAnimator.draw(canvas2, this.paint2, f25);
                    canvas2.scale(1.0f, -1.0f);
                    Theme.playPauseAnimator.draw(canvas2, this.paint2, f25);
                    canvas2.restore();
                }
                if (this.currentIcon != 6) {
                    applyShaderMatrix(false);
                    if (this.currentIcon != 6) {
                        f28 = this.transitionProgress;
                        if (f28 > 0.5f) {
                            f30 = (f28 - 0.5f) / 0.5f;
                            fMin2 = 1.0f - Math.min(1.0f, f30 / 0.5f);
                            if (f30 > 0.5f) {
                                f29 = (f30 - 0.5f) / 0.5f;
                            } else {
                                f29 = 0.0f;
                            }
                        } else {
                            f29 = 0.0f;
                            fMin2 = 1.0f;
                        }
                        this.paint.setAlpha(255);
                        f27 = f29;
                    } else {
                        if (this.nextIcon != 6) {
                            this.paint.setAlpha((int) ((1.0f - this.transitionProgress) * 255.0f));
                        } else {
                            this.paint.setAlpha(255);
                        }
                        fMin2 = 0.0f;
                        f27 = 1.0f;
                    }
                    iDp = AndroidUtilities.dp(7.0f) + iCenterY3;
                    iDp2 = iCenterX - AndroidUtilities.dp(3.0f);
                    if (fMin2 < 1.0f) {
                        i21 = iDp2;
                        canvas.drawLine(iDp2 - AndroidUtilities.dp(6.0f), iDp - AndroidUtilities.dp(6.0f), iDp2 - (AndroidUtilities.dp(6.0f) * fMin2), iDp - (AndroidUtilities.dp(6.0f) * fMin2), this.paint);
                    } else {
                        i21 = iDp2;
                    }
                    if (f27 > 0.0f) {
                        float f928 = i21;
                        float f929 = iDp;
                        canvas2 = canvas;
                        canvas2.drawLine(f928, f929, f928 + (AndroidUtilities.dp(12.0f) * f27), f929 - (AndroidUtilities.dp(12.0f) * f27), this.paint);
                    } else {
                        canvas2 = canvas;
                    }
                } else {
                    applyShaderMatrix(false);
                    if (this.currentIcon != 6) {
                        f28 = this.transitionProgress;
                        if (f28 > 0.5f) {
                            f30 = (f28 - 0.5f) / 0.5f;
                            fMin2 = 1.0f - Math.min(1.0f, f30 / 0.5f);
                            if (f30 > 0.5f) {
                                f29 = (f30 - 0.5f) / 0.5f;
                            } else {
                                f29 = 0.0f;
                            }
                        } else {
                            f29 = 0.0f;
                            fMin2 = 1.0f;
                        }
                        this.paint.setAlpha(255);
                        f27 = f29;
                    } else {
                        if (this.nextIcon != 6) {
                            this.paint.setAlpha((int) ((1.0f - this.transitionProgress) * 255.0f));
                        } else {
                            this.paint.setAlpha(255);
                        }
                        fMin2 = 0.0f;
                        f27 = 1.0f;
                    }
                    iDp = AndroidUtilities.dp(7.0f) + iCenterY3;
                    iDp2 = iCenterX - AndroidUtilities.dp(3.0f);
                    if (fMin2 < 1.0f) {
                        i21 = iDp2;
                        canvas.drawLine(iDp2 - AndroidUtilities.dp(6.0f), iDp - AndroidUtilities.dp(6.0f), iDp2 - (AndroidUtilities.dp(6.0f) * fMin2), iDp - (AndroidUtilities.dp(6.0f) * fMin2), this.paint);
                    } else {
                        i21 = iDp2;
                    }
                    if (f27 > 0.0f) {
                        float f9210 = i21;
                        float f9211 = iDp;
                        canvas2 = canvas;
                        canvas2.drawLine(f9210, f9211, f9210 + (AndroidUtilities.dp(12.0f) * f27), f9211 - (AndroidUtilities.dp(12.0f) * f27), this.paint);
                    } else {
                        canvas2 = canvas;
                    }
                }
                if (drawable3 != null) {
                    int intrinsicWidth7 = (int) (drawable3.getIntrinsicWidth() * f19);
                    int intrinsicHeight7 = (int) (drawable3.getIntrinsicHeight() * f19);
                    drawable3.setColorFilter(this.colorFilter);
                    if (this.currentIcon == this.nextIcon) {
                        i27 = 255;
                    } else {
                        i27 = (int) ((1.0f - this.transitionProgress) * 255.0f);
                    }
                    drawable3.setAlpha(i27);
                    int i4110 = intrinsicWidth7 / 2;
                    int i4111 = intrinsicHeight7 / 2;
                    drawable3.setBounds(iCenterX - i4110, iCenterY3 - i4111, i4110 + iCenterX, i4111 + iCenterY3);
                    drawable3.draw(canvas2);
                }
                if (drawable4 != null) {
                    int intrinsicWidth8 = (int) (drawable4.getIntrinsicWidth() * f20);
                    int intrinsicHeight8 = (int) (drawable4.getIntrinsicHeight() * f20);
                    drawable4.setColorFilter(this.colorFilter);
                    if (this.currentIcon == this.nextIcon) {
                        i26 = 255;
                    } else {
                        i26 = (int) (this.transitionProgress * 255.0f);
                    }
                    drawable4.setAlpha(i26);
                    int i59 = intrinsicWidth8 / 2;
                    int i510 = intrinsicHeight8 / 2;
                    drawable4.setBounds(iCenterX - i59, iCenterY3 - i510, i59 + iCenterX, i510 + iCenterY3);
                    drawable4.draw(canvas2);
                }
                if (pathArr4 != null) {
                    int iDp1113 = AndroidUtilities.dp(24.0f);
                    this.paint2.setStyle(Paint.Style.FILL_AND_STROKE);
                    Paint paint18 = this.paint2;
                    if (this.currentIcon == this.nextIcon) {
                        i25 = 255;
                    } else {
                        i25 = (int) ((1.0f - this.transitionProgress) * 255.0f);
                    }
                    paint18.setAlpha(i25);
                    applyShaderMatrix(true);
                    canvas2.save();
                    canvas2.translate(iCenterX, iCenterY3);
                    canvas2.scale(f19, f19);
                    float f1015 = (-iDp1113) / 2;
                    canvas2.translate(f1015, f1015);
                    path4 = pathArr4[0];
                    if (path4 != null) {
                        canvas2.drawPath(path4, this.paint2);
                    }
                    path5 = pathArr4[1];
                    if (path5 != null) {
                        canvas2.drawPath(path5, this.backPaint);
                    }
                    canvas2.restore();
                }
                if (pathArr3 != null) {
                    int iDp1114 = AndroidUtilities.dp(24.0f);
                    if (this.currentIcon == this.nextIcon) {
                        i24 = 255;
                    } else {
                        i24 = (int) (this.transitionProgress * 255.0f);
                    }
                    this.paint2.setStyle(Paint.Style.FILL_AND_STROKE);
                    this.paint2.setAlpha(i24);
                    applyShaderMatrix(true);
                    canvas2.save();
                    canvas2.translate(iCenterX, iCenterY3);
                    float f1016 = f20;
                    canvas2.scale(f1016, f1016);
                    float f1017 = (-iDp1114) / 2;
                    canvas2.translate(f1017, f1017);
                    path = pathArr3[0];
                    if (path != null) {
                        canvas2.drawPath(path, this.paint2);
                    }
                    if (pathArr3.length >= 3) {
                        canvas2.drawPath(path3, this.paint);
                    }
                    path2 = pathArr3[1];
                    if (path2 != null) {
                        if (i24 != 255) {
                            int alpha4 = this.backPaint.getAlpha();
                            this.backPaint.setAlpha((int) (alpha4 * (i24 / 255.0f)));
                            canvas2.drawPath(pathArr3[1], this.backPaint);
                            this.backPaint.setAlpha(alpha4);
                        } else {
                            canvas2.drawPath(path2, this.backPaint);
                        }
                    }
                    canvas2.restore();
                }
                long jCurrentTimeMillis4 = System.currentTimeMillis();
                j = jCurrentTimeMillis4 - this.lastAnimationTime;
                if (j > 17) {
                    j = 17;
                }
                this.lastAnimationTime = jCurrentTimeMillis4;
                i22 = this.currentIcon;
                if (i22 != 3) {
                    float f1018 = this.downloadRadOffset + ((360 * j) / 2500.0f);
                    this.downloadRadOffset = f1018;
                    this.downloadRadOffset = getCircleValue(f1018);
                    if (this.nextIcon != 2) {
                        f31 = this.downloadProgress;
                        f32 = this.downloadProgressAnimationStart;
                        f33 = f31 - f32;
                        if (f33 > 0.0f) {
                            f34 = this.downloadProgressTime + j;
                            this.downloadProgressTime = f34;
                            if (f34 >= 200.0f) {
                                this.animatedDownloadProgress = f31;
                                this.downloadProgressAnimationStart = f31;
                                this.downloadProgressTime = 0.0f;
                            } else {
                                this.animatedDownloadProgress = f32 + (f33 * this.interpolator.getInterpolation(f34 / 200.0f));
                            }
                        }
                    }
                    invalidateSelf();
                } else {
                    float f1019 = this.downloadRadOffset + ((360 * j) / 2500.0f);
                    this.downloadRadOffset = f1019;
                    this.downloadRadOffset = getCircleValue(f1019);
                    if (this.nextIcon != 2) {
                        f31 = this.downloadProgress;
                        f32 = this.downloadProgressAnimationStart;
                        f33 = f31 - f32;
                        if (f33 > 0.0f) {
                            f34 = this.downloadProgressTime + j;
                            this.downloadProgressTime = f34;
                            if (f34 >= 200.0f) {
                                this.animatedDownloadProgress = f31;
                                this.downloadProgressAnimationStart = f31;
                                this.downloadProgressTime = 0.0f;
                            } else {
                                this.animatedDownloadProgress = f32 + (f33 * this.interpolator.getInterpolation(f34 / 200.0f));
                            }
                        }
                    }
                    invalidateSelf();
                }
                if (this.animatingTransition) {
                    f35 = this.transitionProgress;
                    if (f35 < 1.0f) {
                        f36 = f35 + (j / this.transitionAnimationTime);
                        this.transitionProgress = f36;
                        if (f36 >= 1.0f) {
                            this.currentIcon = this.nextIcon;
                            this.transitionProgress = 1.0f;
                            this.animatingTransition = false;
                        }
                        invalidateSelf();
                    }
                }
                i23 = i11;
                if (i23 >= 1) {
                    canvas2.restoreToCount(i23);
                }
            }
            i29 = 6;
            if (i54 == i29) {
                f37 = 1.0f;
                fMin3 = Math.min(1.0f, this.transitionProgress / 0.5f);
            } else {
                f37 = 1.0f;
                fMin3 = this.transitionProgress;
            }
            f38 = f37 - fMin3;
            float fCenterX3 = bounds.centerX();
            float fCenterY = bounds.centerY();
            float fDp21 = AndroidUtilities.dp(7.0f) * f38 * this.scale;
            iMin = (int) (Math.min(f37, f38 * 2.0f) * 255.0f);
            f39 = fCenterY;
            fCenterX = fCenterX3;
            fDp5 = fDp21;
            f40 = f;
            if (f38 != f37) {
                canvas2.save();
                canvas2.scale(f38, f38, fCenterX, f39);
            }
            if (f40 != f) {
                canvas2.save();
                canvas2.rotate(f40, iCenterX, iCenterY3);
            }
            if (iMin != 0) {
                f45 = iMin;
                this.paint.setAlpha((int) (this.overrideAlpha * f45));
                if (this.currentIcon != 14) {
                    f43 = 4.0f;
                    this.paint3.setAlpha((int) (f45 * this.overrideAlpha));
                    this.rect.set(iCenterX - AndroidUtilities.dp(3.5f), iCenterY3 - AndroidUtilities.dp(3.5f), AndroidUtilities.dp(3.5f) + iCenterX, AndroidUtilities.dp(3.5f) + iCenterY3);
                    canvas2.drawRoundRect(this.rect, AndroidUtilities.dp(2.0f), AndroidUtilities.dp(2.0f), this.paint3);
                } else {
                    f43 = 4.0f;
                    this.paint3.setAlpha((int) (f45 * this.overrideAlpha));
                    this.rect.set(iCenterX - AndroidUtilities.dp(3.5f), iCenterY3 - AndroidUtilities.dp(3.5f), AndroidUtilities.dp(3.5f) + iCenterX, AndroidUtilities.dp(3.5f) + iCenterY3);
                    canvas2.drawRoundRect(this.rect, AndroidUtilities.dp(2.0f), AndroidUtilities.dp(2.0f), this.paint3);
                }
            } else {
                f43 = 4.0f;
            }
            if (f40 != f) {
                canvas2.restore();
            }
            if (f38 != f37) {
                canvas2.restore();
            }
            i30 = this.currentIcon;
            if (i30 != 3) {
                float fMax4 = Math.max(f43, this.animatedDownloadProgress * f88);
                if (this.isMini) {
                    f44 = 2.0f;
                } else {
                    f44 = f43;
                }
                int iDp120 = AndroidUtilities.dp(f44);
                this.rect.set(bounds.left + iDp120, bounds.top + iDp120, bounds.right - iDp120, bounds.bottom - iDp120);
                i31 = this.currentIcon;
                if (i31 != 14) {
                    this.paint.setAlpha((int) (iMin * 0.15f * this.overrideAlpha));
                    canvas2.drawArc(this.rect, 0.0f, 360.0f, false, this.paint);
                    this.paint.setAlpha(iMin);
                } else {
                    this.paint.setAlpha((int) (iMin * 0.15f * this.overrideAlpha));
                    canvas2.drawArc(this.rect, 0.0f, 360.0f, false, this.paint);
                    this.paint.setAlpha(iMin);
                }
                canvas2 = canvas;
                canvas2.drawArc(this.rect, this.downloadRadOffset, fMax4, false, this.paint);
            } else {
                float fMax5 = Math.max(f43, this.animatedDownloadProgress * f88);
                if (this.isMini) {
                    f44 = 2.0f;
                } else {
                    f44 = f43;
                }
                int iDp121 = AndroidUtilities.dp(f44);
                this.rect.set(bounds.left + iDp121, bounds.top + iDp121, bounds.right - iDp121, bounds.bottom - iDp121);
                i31 = this.currentIcon;
                if (i31 != 14) {
                    this.paint.setAlpha((int) (iMin * 0.15f * this.overrideAlpha));
                    canvas2.drawArc(this.rect, 0.0f, 360.0f, false, this.paint);
                    this.paint.setAlpha(iMin);
                } else {
                    this.paint.setAlpha((int) (iMin * 0.15f * this.overrideAlpha));
                    canvas2.drawArc(this.rect, 0.0f, 360.0f, false, this.paint);
                    this.paint.setAlpha(iMin);
                }
                canvas2 = canvas;
                canvas2.drawArc(this.rect, this.downloadRadOffset, fMax5, false, this.paint);
            }
            i3 = this.currentIcon;
            if (i3 == this.nextIcon) {
                f20 = 1.0f;
                f19 = 1.0f;
            } else {
                if (i3 != 4) {
                    fMin = this.transitionProgress;
                    fMax = 1.0f - fMin;
                } else {
                    fMin = this.transitionProgress;
                    fMax = 1.0f - fMin;
                }
                f19 = fMax;
                f20 = fMin;
            }
            i4 = this.nextIcon;
            if (i4 == i2) {
                pathArr = Theme.chat_updatePath;
            } else {
                if (this.currentIcon == i2) {
                    pathArr2 = Theme.chat_updatePath;
                    pathArr = null;
                } else {
                    pathArr = null;
                }
                if (i4 == 5) {
                    pathArr = Theme.chat_filePath;
                } else if (this.currentIcon == 5) {
                    pathArr2 = Theme.chat_filePath;
                }
                pathArr3 = pathArr;
                pathArr4 = pathArr2;
                if (i4 == 7) {
                    drawable2 = Theme.chat_flameIcon;
                    drawable = null;
                    i5 = 8;
                } else {
                    if (this.currentIcon == 7) {
                        drawable = Theme.chat_flameIcon;
                    } else {
                        drawable = null;
                    }
                    i5 = 8;
                    drawable2 = null;
                }
                if (i4 == i5) {
                    drawable2 = Theme.chat_gifIcon;
                } else if (this.currentIcon == i5) {
                    drawable = Theme.chat_gifIcon;
                }
                drawable3 = drawable;
                if (this.currentIcon != 9) {
                    applyShaderMatrix(false);
                    Paint paint19 = this.paint;
                    if (this.currentIcon == this.nextIcon) {
                        i6 = 255;
                    } else {
                        i6 = (int) (this.transitionProgress * 255.0f);
                    }
                    paint19.setAlpha(i6);
                    int iDp1115 = AndroidUtilities.dp(7.0f) + iCenterY3;
                    int iDp1116 = iCenterX - AndroidUtilities.dp(3.0f);
                    if (this.currentIcon != this.nextIcon) {
                        canvas2.save();
                        float f816 = this.transitionProgress;
                        canvas2.scale(f816, f816, iCenterX, iCenterY3);
                    }
                    float f91124 = iDp1116;
                    float f91125 = iDp1115;
                    rect = bounds;
                    drawable4 = drawable2;
                    canvas2.drawLine(iDp1116 - AndroidUtilities.dp(6.0f), iDp1115 - AndroidUtilities.dp(6.0f), f91124, f91125, this.paint);
                    canvas2 = canvas;
                    canvas2.drawLine(f91124, f91125, iDp1116 + AndroidUtilities.dp(12.0f), iDp1115 - AndroidUtilities.dp(12.0f), this.paint);
                    if (this.currentIcon != this.nextIcon) {
                        canvas2.restore();
                    }
                } else {
                    applyShaderMatrix(false);
                    Paint paint110 = this.paint;
                    if (this.currentIcon == this.nextIcon) {
                        i6 = 255;
                    } else {
                        i6 = (int) (this.transitionProgress * 255.0f);
                    }
                    paint110.setAlpha(i6);
                    int iDp1117 = AndroidUtilities.dp(7.0f) + iCenterY3;
                    int iDp1118 = iCenterX - AndroidUtilities.dp(3.0f);
                    if (this.currentIcon != this.nextIcon) {
                        canvas2.save();
                        float f817 = this.transitionProgress;
                        canvas2.scale(f817, f817, iCenterX, iCenterY3);
                    }
                    float f91126 = iDp1118;
                    float f91127 = iDp1117;
                    rect = bounds;
                    drawable4 = drawable2;
                    canvas2.drawLine(iDp1118 - AndroidUtilities.dp(6.0f), iDp1117 - AndroidUtilities.dp(6.0f), f91126, f91127, this.paint);
                    canvas2 = canvas;
                    canvas2.drawLine(f91126, f91127, iDp1118 + AndroidUtilities.dp(12.0f), iDp1117 - AndroidUtilities.dp(12.0f), this.paint);
                    if (this.currentIcon != this.nextIcon) {
                        canvas2.restore();
                    }
                }
                if (this.currentIcon != 12) {
                    applyShaderMatrix(false);
                    i7 = this.currentIcon;
                    i8 = this.nextIcon;
                    if (i7 == i8) {
                        i9 = 13;
                        f21 = 1.0f;
                    } else {
                        i9 = 13;
                        if (i8 == 13) {
                            f21 = this.transitionProgress;
                        } else {
                            f21 = 1.0f - this.transitionProgress;
                        }
                    }
                    Paint paint111 = this.paint;
                    if (i7 == i8) {
                        i10 = 255;
                    } else {
                        i10 = (int) (f21 * 255.0f);
                    }
                    paint111.setAlpha(i10);
                    AndroidUtilities.dp(7.0f);
                    AndroidUtilities.dp(3.0f);
                    if (this.currentIcon != this.nextIcon) {
                        canvas2.save();
                        canvas2.scale(f21, f21, iCenterX, iCenterY3);
                    }
                    float fDp115 = AndroidUtilities.dp(7.0f) * this.scale;
                    float f9111114 = iCenterX;
                    float f9111115 = f9111114 - fDp115;
                    float f9111116 = iCenterY3;
                    float f9111117 = f9111116 - fDp115;
                    float f9111118 = f9111114 + fDp115;
                    float f9111119 = fDp115 + f9111116;
                    i11 = i;
                    i12 = i9;
                    canvas2.drawLine(f9111115, f9111117, f9111118, f9111119, this.paint);
                    canvas2 = canvas;
                    canvas2.drawLine(f9111118, f9111117, f9111115, f9111119, this.paint);
                    if (this.currentIcon != this.nextIcon) {
                        canvas2.restore();
                    }
                } else {
                    applyShaderMatrix(false);
                    i7 = this.currentIcon;
                    i8 = this.nextIcon;
                    if (i7 == i8) {
                        i9 = 13;
                        f21 = 1.0f;
                    } else {
                        i9 = 13;
                        if (i8 == 13) {
                            f21 = this.transitionProgress;
                        } else {
                            f21 = 1.0f - this.transitionProgress;
                        }
                    }
                    Paint paint112 = this.paint;
                    if (i7 == i8) {
                        i10 = 255;
                    } else {
                        i10 = (int) (f21 * 255.0f);
                    }
                    paint112.setAlpha(i10);
                    AndroidUtilities.dp(7.0f);
                    AndroidUtilities.dp(3.0f);
                    if (this.currentIcon != this.nextIcon) {
                        canvas2.save();
                        canvas2.scale(f21, f21, iCenterX, iCenterY3);
                    }
                    float fDp116 = AndroidUtilities.dp(7.0f) * this.scale;
                    float f91111110 = iCenterX;
                    float f91111111 = f91111110 - fDp116;
                    float f91111112 = iCenterY3;
                    float f91111113 = f91111112 - fDp116;
                    float f91111114 = f91111110 + fDp116;
                    float f91111115 = fDp116 + f91111112;
                    i11 = i;
                    i12 = i9;
                    canvas2.drawLine(f91111111, f91111113, f91111114, f91111115, this.paint);
                    canvas2 = canvas;
                    canvas2.drawLine(f91111114, f91111113, f91111111, f91111115, this.paint);
                    if (this.currentIcon != this.nextIcon) {
                        canvas2.restore();
                    }
                }
                if (this.currentIcon != i12) {
                    applyShaderMatrix(false);
                    i13 = this.currentIcon;
                    i14 = this.nextIcon;
                    if (i13 == i14) {
                        f22 = 1.0f;
                    } else if (i14 == i12) {
                        f22 = this.transitionProgress;
                    } else {
                        f22 = 1.0f - this.transitionProgress;
                    }
                    this.textPaint.setAlpha((int) (f22 * 255.0f));
                    int iDp1119 = AndroidUtilities.dp(5.0f) + iCenterY3;
                    int i4112 = iCenterX - (this.percentStringWidth / 2);
                    f23 = 5.0f;
                    if (this.currentIcon != this.nextIcon) {
                        canvas2.save();
                        canvas2.scale(f22, f22, iCenterX, iCenterY3);
                    }
                    i15 = (int) (this.animatedDownloadProgress * 100.0f);
                    if (this.percentString != null) {
                        this.lastPercent = i15;
                        String str14 = String.format("%d%%", Integer.valueOf(i15));
                        this.percentString = str14;
                        this.percentStringWidth = (int) Math.ceil(this.textPaint.measureText(str14));
                    } else {
                        this.lastPercent = i15;
                        String str15 = String.format("%d%%", Integer.valueOf(i15));
                        this.percentString = str15;
                        this.percentStringWidth = (int) Math.ceil(this.textPaint.measureText(str15));
                    }
                    canvas2.drawText(this.percentString, i4112, iDp1119, this.textPaint);
                    if (this.currentIcon != this.nextIcon) {
                        canvas2.restore();
                    }
                } else {
                    applyShaderMatrix(false);
                    i13 = this.currentIcon;
                    i14 = this.nextIcon;
                    if (i13 == i14) {
                        f22 = 1.0f;
                    } else if (i14 == i12) {
                        f22 = this.transitionProgress;
                    } else {
                        f22 = 1.0f - this.transitionProgress;
                    }
                    this.textPaint.setAlpha((int) (f22 * 255.0f));
                    int iDp11110 = AndroidUtilities.dp(5.0f) + iCenterY3;
                    int i4113 = iCenterX - (this.percentStringWidth / 2);
                    f23 = 5.0f;
                    if (this.currentIcon != this.nextIcon) {
                        canvas2.save();
                        canvas2.scale(f22, f22, iCenterX, iCenterY3);
                    }
                    i15 = (int) (this.animatedDownloadProgress * 100.0f);
                    if (this.percentString != null) {
                        this.lastPercent = i15;
                        String str16 = String.format("%d%%", Integer.valueOf(i15));
                        this.percentString = str16;
                        this.percentStringWidth = (int) Math.ceil(this.textPaint.measureText(str16));
                    } else {
                        this.lastPercent = i15;
                        String str17 = String.format("%d%%", Integer.valueOf(i15));
                        this.percentString = str17;
                        this.percentStringWidth = (int) Math.ceil(this.textPaint.measureText(str17));
                    }
                    canvas2.drawText(this.percentString, i4113, iDp11110, this.textPaint);
                    if (this.currentIcon != this.nextIcon) {
                        canvas2.restore();
                    }
                }
                i16 = this.currentIcon;
                i17 = 1;
                if (i16 != 0) {
                    if (i16 == 0) {
                        if (i16 == 1) {
                            if (this.nextIcon != 0) {
                                i17 = 1;
                            } else if (this.animatingTransition) {
                                if (this.nextIcon == 0) {
                                    f24 = 1.0f - this.transitionProgress;
                                } else {
                                    f24 = this.transitionProgress;
                                }
                                i17 = 1;
                            } else {
                                i17 = 1;
                                if (this.nextIcon == 1) {
                                    f24 = 1.0f;
                                } else {
                                    f24 = 0.0f;
                                }
                            }
                            i18 = this.nextIcon;
                            if (i18 != 0) {
                                this.paint2.setAlpha(255);
                            } else {
                                this.paint2.setAlpha(255);
                            }
                            applyShaderMatrix(true);
                            canvas2.save();
                            canvas2.translate(rect.centerX() + (AndroidUtilities.dp(1.0f) * (1.0f - f24)), rect.centerY());
                            f25 = f24 * 500.0f;
                            i19 = this.currentIcon;
                            if (i19 == 1) {
                                f26 = 90.0f;
                            } else {
                                f26 = 0.0f;
                            }
                            if (i19 == 0) {
                                if (i19 == 1) {
                                    if (f25 < 100.0f) {
                                        interpolation = CubicBezierInterpolator.EASE_BOTH.getInterpolation(f25 / 100.0f) * (-5.0f);
                                    } else if (f25 < 484.0f) {
                                        interpolation = (CubicBezierInterpolator.EASE_BOTH.getInterpolation((f25 - 100.0f) / 384.0f) * 95.0f) - 5.0f;
                                    }
                                    f26 = interpolation;
                                }
                            } else if (i19 == 1) {
                                if (f25 < 100.0f) {
                                    interpolation = CubicBezierInterpolator.EASE_BOTH.getInterpolation(f25 / 100.0f) * (-5.0f);
                                } else if (f25 < 484.0f) {
                                    interpolation = (CubicBezierInterpolator.EASE_BOTH.getInterpolation((f25 - 100.0f) / 384.0f) * 95.0f) - 5.0f;
                                }
                                f26 = interpolation;
                            }
                            canvas2.rotate(f26);
                            i20 = this.currentIcon;
                            if (i20 == 0) {
                                canvas2.scale(f20, f20);
                            } else {
                                canvas2.scale(f20, f20);
                            }
                            Theme.playPauseAnimator.draw(canvas2, this.paint2, f25);
                            canvas2.scale(1.0f, -1.0f);
                            Theme.playPauseAnimator.draw(canvas2, this.paint2, f25);
                            canvas2.restore();
                        }
                        if (i16 == i17) {
                            f24 = 1.0f;
                        } else {
                            f24 = 0.0f;
                        }
                        i18 = this.nextIcon;
                        if (i18 != 0) {
                            this.paint2.setAlpha(255);
                        } else {
                            this.paint2.setAlpha(255);
                        }
                        applyShaderMatrix(true);
                        canvas2.save();
                        canvas2.translate(rect.centerX() + (AndroidUtilities.dp(1.0f) * (1.0f - f24)), rect.centerY());
                        f25 = f24 * 500.0f;
                        i19 = this.currentIcon;
                        if (i19 == 1) {
                            f26 = 90.0f;
                        } else {
                            f26 = 0.0f;
                        }
                        if (i19 == 0) {
                            if (i19 == 1) {
                                if (f25 < 100.0f) {
                                    interpolation = CubicBezierInterpolator.EASE_BOTH.getInterpolation(f25 / 100.0f) * (-5.0f);
                                } else if (f25 < 484.0f) {
                                    interpolation = (CubicBezierInterpolator.EASE_BOTH.getInterpolation((f25 - 100.0f) / 384.0f) * 95.0f) - 5.0f;
                                }
                                f26 = interpolation;
                            }
                        } else if (i19 == 1) {
                            if (f25 < 100.0f) {
                                interpolation = CubicBezierInterpolator.EASE_BOTH.getInterpolation(f25 / 100.0f) * (-5.0f);
                            } else if (f25 < 484.0f) {
                                interpolation = (CubicBezierInterpolator.EASE_BOTH.getInterpolation((f25 - 100.0f) / 384.0f) * 95.0f) - 5.0f;
                            }
                            f26 = interpolation;
                        }
                        canvas2.rotate(f26);
                        i20 = this.currentIcon;
                        if (i20 == 0) {
                            canvas2.scale(f20, f20);
                        } else {
                            canvas2.scale(f20, f20);
                        }
                        Theme.playPauseAnimator.draw(canvas2, this.paint2, f25);
                        canvas2.scale(1.0f, -1.0f);
                        Theme.playPauseAnimator.draw(canvas2, this.paint2, f25);
                        canvas2.restore();
                    } else {
                        if (i16 == 1) {
                            if (this.nextIcon != 0) {
                                i17 = 1;
                            } else if (this.animatingTransition) {
                                if (this.nextIcon == 0) {
                                    f24 = 1.0f - this.transitionProgress;
                                } else {
                                    f24 = this.transitionProgress;
                                }
                                i17 = 1;
                            } else {
                                i17 = 1;
                                if (this.nextIcon == 1) {
                                    f24 = 1.0f;
                                } else {
                                    f24 = 0.0f;
                                }
                            }
                            i18 = this.nextIcon;
                            if (i18 != 0) {
                                this.paint2.setAlpha(255);
                            } else {
                                this.paint2.setAlpha(255);
                            }
                            applyShaderMatrix(true);
                            canvas2.save();
                            canvas2.translate(rect.centerX() + (AndroidUtilities.dp(1.0f) * (1.0f - f24)), rect.centerY());
                            f25 = f24 * 500.0f;
                            i19 = this.currentIcon;
                            if (i19 == 1) {
                                f26 = 90.0f;
                            } else {
                                f26 = 0.0f;
                            }
                            if (i19 == 0) {
                                if (i19 == 1) {
                                    if (f25 < 100.0f) {
                                        interpolation = CubicBezierInterpolator.EASE_BOTH.getInterpolation(f25 / 100.0f) * (-5.0f);
                                    } else if (f25 < 484.0f) {
                                        interpolation = (CubicBezierInterpolator.EASE_BOTH.getInterpolation((f25 - 100.0f) / 384.0f) * 95.0f) - 5.0f;
                                    }
                                    f26 = interpolation;
                                }
                            } else if (i19 == 1) {
                                if (f25 < 100.0f) {
                                    interpolation = CubicBezierInterpolator.EASE_BOTH.getInterpolation(f25 / 100.0f) * (-5.0f);
                                } else if (f25 < 484.0f) {
                                    interpolation = (CubicBezierInterpolator.EASE_BOTH.getInterpolation((f25 - 100.0f) / 384.0f) * 95.0f) - 5.0f;
                                }
                                f26 = interpolation;
                            }
                            canvas2.rotate(f26);
                            i20 = this.currentIcon;
                            if (i20 == 0) {
                                canvas2.scale(f20, f20);
                            } else {
                                canvas2.scale(f20, f20);
                            }
                            Theme.playPauseAnimator.draw(canvas2, this.paint2, f25);
                            canvas2.scale(1.0f, -1.0f);
                            Theme.playPauseAnimator.draw(canvas2, this.paint2, f25);
                            canvas2.restore();
                        }
                        if (i16 == i17) {
                            f24 = 1.0f;
                        } else {
                            f24 = 0.0f;
                        }
                        i18 = this.nextIcon;
                        if (i18 != 0) {
                            this.paint2.setAlpha(255);
                        } else {
                            this.paint2.setAlpha(255);
                        }
                        applyShaderMatrix(true);
                        canvas2.save();
                        canvas2.translate(rect.centerX() + (AndroidUtilities.dp(1.0f) * (1.0f - f24)), rect.centerY());
                        f25 = f24 * 500.0f;
                        i19 = this.currentIcon;
                        if (i19 == 1) {
                            f26 = 90.0f;
                        } else {
                            f26 = 0.0f;
                        }
                        if (i19 == 0) {
                            if (i19 == 1) {
                                if (f25 < 100.0f) {
                                    interpolation = CubicBezierInterpolator.EASE_BOTH.getInterpolation(f25 / 100.0f) * (-5.0f);
                                } else if (f25 < 484.0f) {
                                    interpolation = (CubicBezierInterpolator.EASE_BOTH.getInterpolation((f25 - 100.0f) / 384.0f) * 95.0f) - 5.0f;
                                }
                                f26 = interpolation;
                            }
                        } else if (i19 == 1) {
                            if (f25 < 100.0f) {
                                interpolation = CubicBezierInterpolator.EASE_BOTH.getInterpolation(f25 / 100.0f) * (-5.0f);
                            } else if (f25 < 484.0f) {
                                interpolation = (CubicBezierInterpolator.EASE_BOTH.getInterpolation((f25 - 100.0f) / 384.0f) * 95.0f) - 5.0f;
                            }
                            f26 = interpolation;
                        }
                        canvas2.rotate(f26);
                        i20 = this.currentIcon;
                        if (i20 == 0) {
                            canvas2.scale(f20, f20);
                        } else {
                            canvas2.scale(f20, f20);
                        }
                        Theme.playPauseAnimator.draw(canvas2, this.paint2, f25);
                        canvas2.scale(1.0f, -1.0f);
                        Theme.playPauseAnimator.draw(canvas2, this.paint2, f25);
                        canvas2.restore();
                    }
                } else if (i16 == 0) {
                    if (i16 == 1) {
                        if (this.nextIcon != 0) {
                            i17 = 1;
                        } else if (this.animatingTransition) {
                            if (this.nextIcon == 0) {
                                f24 = 1.0f - this.transitionProgress;
                            } else {
                                f24 = this.transitionProgress;
                            }
                            i17 = 1;
                        } else {
                            i17 = 1;
                            if (this.nextIcon == 1) {
                                f24 = 1.0f;
                            } else {
                                f24 = 0.0f;
                            }
                        }
                        i18 = this.nextIcon;
                        if (i18 != 0) {
                            this.paint2.setAlpha(255);
                        } else {
                            this.paint2.setAlpha(255);
                        }
                        applyShaderMatrix(true);
                        canvas2.save();
                        canvas2.translate(rect.centerX() + (AndroidUtilities.dp(1.0f) * (1.0f - f24)), rect.centerY());
                        f25 = f24 * 500.0f;
                        i19 = this.currentIcon;
                        if (i19 == 1) {
                            f26 = 90.0f;
                        } else {
                            f26 = 0.0f;
                        }
                        if (i19 == 0) {
                            if (i19 == 1) {
                                if (f25 < 100.0f) {
                                    interpolation = CubicBezierInterpolator.EASE_BOTH.getInterpolation(f25 / 100.0f) * (-5.0f);
                                } else if (f25 < 484.0f) {
                                    interpolation = (CubicBezierInterpolator.EASE_BOTH.getInterpolation((f25 - 100.0f) / 384.0f) * 95.0f) - 5.0f;
                                }
                                f26 = interpolation;
                            }
                        } else if (i19 == 1) {
                            if (f25 < 100.0f) {
                                interpolation = CubicBezierInterpolator.EASE_BOTH.getInterpolation(f25 / 100.0f) * (-5.0f);
                            } else if (f25 < 484.0f) {
                                interpolation = (CubicBezierInterpolator.EASE_BOTH.getInterpolation((f25 - 100.0f) / 384.0f) * 95.0f) - 5.0f;
                            }
                            f26 = interpolation;
                        }
                        canvas2.rotate(f26);
                        i20 = this.currentIcon;
                        if (i20 == 0) {
                            canvas2.scale(f20, f20);
                        } else {
                            canvas2.scale(f20, f20);
                        }
                        Theme.playPauseAnimator.draw(canvas2, this.paint2, f25);
                        canvas2.scale(1.0f, -1.0f);
                        Theme.playPauseAnimator.draw(canvas2, this.paint2, f25);
                        canvas2.restore();
                    }
                    if (i16 == i17) {
                        f24 = 1.0f;
                    } else {
                        f24 = 0.0f;
                    }
                    i18 = this.nextIcon;
                    if (i18 != 0) {
                        this.paint2.setAlpha(255);
                    } else {
                        this.paint2.setAlpha(255);
                    }
                    applyShaderMatrix(true);
                    canvas2.save();
                    canvas2.translate(rect.centerX() + (AndroidUtilities.dp(1.0f) * (1.0f - f24)), rect.centerY());
                    f25 = f24 * 500.0f;
                    i19 = this.currentIcon;
                    if (i19 == 1) {
                        f26 = 90.0f;
                    } else {
                        f26 = 0.0f;
                    }
                    if (i19 == 0) {
                        if (i19 == 1) {
                            if (f25 < 100.0f) {
                                interpolation = CubicBezierInterpolator.EASE_BOTH.getInterpolation(f25 / 100.0f) * (-5.0f);
                            } else if (f25 < 484.0f) {
                                interpolation = (CubicBezierInterpolator.EASE_BOTH.getInterpolation((f25 - 100.0f) / 384.0f) * 95.0f) - 5.0f;
                            }
                            f26 = interpolation;
                        }
                    } else if (i19 == 1) {
                        if (f25 < 100.0f) {
                            interpolation = CubicBezierInterpolator.EASE_BOTH.getInterpolation(f25 / 100.0f) * (-5.0f);
                        } else if (f25 < 484.0f) {
                            interpolation = (CubicBezierInterpolator.EASE_BOTH.getInterpolation((f25 - 100.0f) / 384.0f) * 95.0f) - 5.0f;
                        }
                        f26 = interpolation;
                    }
                    canvas2.rotate(f26);
                    i20 = this.currentIcon;
                    if (i20 == 0) {
                        canvas2.scale(f20, f20);
                    } else {
                        canvas2.scale(f20, f20);
                    }
                    Theme.playPauseAnimator.draw(canvas2, this.paint2, f25);
                    canvas2.scale(1.0f, -1.0f);
                    Theme.playPauseAnimator.draw(canvas2, this.paint2, f25);
                    canvas2.restore();
                } else {
                    if (i16 == 1) {
                        if (this.nextIcon != 0) {
                            i17 = 1;
                        } else if (this.animatingTransition) {
                            if (this.nextIcon == 0) {
                                f24 = 1.0f - this.transitionProgress;
                            } else {
                                f24 = this.transitionProgress;
                            }
                            i17 = 1;
                        } else {
                            i17 = 1;
                            if (this.nextIcon == 1) {
                                f24 = 1.0f;
                            } else {
                                f24 = 0.0f;
                            }
                        }
                        i18 = this.nextIcon;
                        if (i18 != 0) {
                            this.paint2.setAlpha(255);
                        } else {
                            this.paint2.setAlpha(255);
                        }
                        applyShaderMatrix(true);
                        canvas2.save();
                        canvas2.translate(rect.centerX() + (AndroidUtilities.dp(1.0f) * (1.0f - f24)), rect.centerY());
                        f25 = f24 * 500.0f;
                        i19 = this.currentIcon;
                        if (i19 == 1) {
                            f26 = 90.0f;
                        } else {
                            f26 = 0.0f;
                        }
                        if (i19 == 0) {
                            if (i19 == 1) {
                                if (f25 < 100.0f) {
                                    interpolation = CubicBezierInterpolator.EASE_BOTH.getInterpolation(f25 / 100.0f) * (-5.0f);
                                } else if (f25 < 484.0f) {
                                    interpolation = (CubicBezierInterpolator.EASE_BOTH.getInterpolation((f25 - 100.0f) / 384.0f) * 95.0f) - 5.0f;
                                }
                                f26 = interpolation;
                            }
                        } else if (i19 == 1) {
                            if (f25 < 100.0f) {
                                interpolation = CubicBezierInterpolator.EASE_BOTH.getInterpolation(f25 / 100.0f) * (-5.0f);
                            } else if (f25 < 484.0f) {
                                interpolation = (CubicBezierInterpolator.EASE_BOTH.getInterpolation((f25 - 100.0f) / 384.0f) * 95.0f) - 5.0f;
                            }
                            f26 = interpolation;
                        }
                        canvas2.rotate(f26);
                        i20 = this.currentIcon;
                        if (i20 == 0) {
                            canvas2.scale(f20, f20);
                        } else {
                            canvas2.scale(f20, f20);
                        }
                        Theme.playPauseAnimator.draw(canvas2, this.paint2, f25);
                        canvas2.scale(1.0f, -1.0f);
                        Theme.playPauseAnimator.draw(canvas2, this.paint2, f25);
                        canvas2.restore();
                    }
                    if (i16 == i17) {
                        f24 = 1.0f;
                    } else {
                        f24 = 0.0f;
                    }
                    i18 = this.nextIcon;
                    if (i18 != 0) {
                        this.paint2.setAlpha(255);
                    } else {
                        this.paint2.setAlpha(255);
                    }
                    applyShaderMatrix(true);
                    canvas2.save();
                    canvas2.translate(rect.centerX() + (AndroidUtilities.dp(1.0f) * (1.0f - f24)), rect.centerY());
                    f25 = f24 * 500.0f;
                    i19 = this.currentIcon;
                    if (i19 == 1) {
                        f26 = 90.0f;
                    } else {
                        f26 = 0.0f;
                    }
                    if (i19 == 0) {
                        if (i19 == 1) {
                            if (f25 < 100.0f) {
                                interpolation = CubicBezierInterpolator.EASE_BOTH.getInterpolation(f25 / 100.0f) * (-5.0f);
                            } else if (f25 < 484.0f) {
                                interpolation = (CubicBezierInterpolator.EASE_BOTH.getInterpolation((f25 - 100.0f) / 384.0f) * 95.0f) - 5.0f;
                            }
                            f26 = interpolation;
                        }
                    } else if (i19 == 1) {
                        if (f25 < 100.0f) {
                            interpolation = CubicBezierInterpolator.EASE_BOTH.getInterpolation(f25 / 100.0f) * (-5.0f);
                        } else if (f25 < 484.0f) {
                            interpolation = (CubicBezierInterpolator.EASE_BOTH.getInterpolation((f25 - 100.0f) / 384.0f) * 95.0f) - 5.0f;
                        }
                        f26 = interpolation;
                    }
                    canvas2.rotate(f26);
                    i20 = this.currentIcon;
                    if (i20 == 0) {
                        canvas2.scale(f20, f20);
                    } else {
                        canvas2.scale(f20, f20);
                    }
                    Theme.playPauseAnimator.draw(canvas2, this.paint2, f25);
                    canvas2.scale(1.0f, -1.0f);
                    Theme.playPauseAnimator.draw(canvas2, this.paint2, f25);
                    canvas2.restore();
                }
                if (this.currentIcon != 6) {
                    applyShaderMatrix(false);
                    if (this.currentIcon != 6) {
                        f28 = this.transitionProgress;
                        if (f28 > 0.5f) {
                            f30 = (f28 - 0.5f) / 0.5f;
                            fMin2 = 1.0f - Math.min(1.0f, f30 / 0.5f);
                            if (f30 > 0.5f) {
                                f29 = (f30 - 0.5f) / 0.5f;
                            } else {
                                f29 = 0.0f;
                            }
                        } else {
                            f29 = 0.0f;
                            fMin2 = 1.0f;
                        }
                        this.paint.setAlpha(255);
                        f27 = f29;
                    } else {
                        if (this.nextIcon != 6) {
                            this.paint.setAlpha((int) ((1.0f - this.transitionProgress) * 255.0f));
                        } else {
                            this.paint.setAlpha(255);
                        }
                        fMin2 = 0.0f;
                        f27 = 1.0f;
                    }
                    iDp = AndroidUtilities.dp(7.0f) + iCenterY3;
                    iDp2 = iCenterX - AndroidUtilities.dp(3.0f);
                    if (fMin2 < 1.0f) {
                        i21 = iDp2;
                        canvas.drawLine(iDp2 - AndroidUtilities.dp(6.0f), iDp - AndroidUtilities.dp(6.0f), iDp2 - (AndroidUtilities.dp(6.0f) * fMin2), iDp - (AndroidUtilities.dp(6.0f) * fMin2), this.paint);
                    } else {
                        i21 = iDp2;
                    }
                    if (f27 > 0.0f) {
                        float f9212 = i21;
                        float f9213 = iDp;
                        canvas2 = canvas;
                        canvas2.drawLine(f9212, f9213, f9212 + (AndroidUtilities.dp(12.0f) * f27), f9213 - (AndroidUtilities.dp(12.0f) * f27), this.paint);
                    } else {
                        canvas2 = canvas;
                    }
                } else {
                    applyShaderMatrix(false);
                    if (this.currentIcon != 6) {
                        f28 = this.transitionProgress;
                        if (f28 > 0.5f) {
                            f30 = (f28 - 0.5f) / 0.5f;
                            fMin2 = 1.0f - Math.min(1.0f, f30 / 0.5f);
                            if (f30 > 0.5f) {
                                f29 = (f30 - 0.5f) / 0.5f;
                            } else {
                                f29 = 0.0f;
                            }
                        } else {
                            f29 = 0.0f;
                            fMin2 = 1.0f;
                        }
                        this.paint.setAlpha(255);
                        f27 = f29;
                    } else {
                        if (this.nextIcon != 6) {
                            this.paint.setAlpha((int) ((1.0f - this.transitionProgress) * 255.0f));
                        } else {
                            this.paint.setAlpha(255);
                        }
                        fMin2 = 0.0f;
                        f27 = 1.0f;
                    }
                    iDp = AndroidUtilities.dp(7.0f) + iCenterY3;
                    iDp2 = iCenterX - AndroidUtilities.dp(3.0f);
                    if (fMin2 < 1.0f) {
                        i21 = iDp2;
                        canvas.drawLine(iDp2 - AndroidUtilities.dp(6.0f), iDp - AndroidUtilities.dp(6.0f), iDp2 - (AndroidUtilities.dp(6.0f) * fMin2), iDp - (AndroidUtilities.dp(6.0f) * fMin2), this.paint);
                    } else {
                        i21 = iDp2;
                    }
                    if (f27 > 0.0f) {
                        float f9214 = i21;
                        float f9215 = iDp;
                        canvas2 = canvas;
                        canvas2.drawLine(f9214, f9215, f9214 + (AndroidUtilities.dp(12.0f) * f27), f9215 - (AndroidUtilities.dp(12.0f) * f27), this.paint);
                    } else {
                        canvas2 = canvas;
                    }
                }
                if (drawable3 != null) {
                    int intrinsicWidth9 = (int) (drawable3.getIntrinsicWidth() * f19);
                    int intrinsicHeight9 = (int) (drawable3.getIntrinsicHeight() * f19);
                    drawable3.setColorFilter(this.colorFilter);
                    if (this.currentIcon == this.nextIcon) {
                        i27 = 255;
                    } else {
                        i27 = (int) ((1.0f - this.transitionProgress) * 255.0f);
                    }
                    drawable3.setAlpha(i27);
                    int i4114 = intrinsicWidth9 / 2;
                    int i4115 = intrinsicHeight9 / 2;
                    drawable3.setBounds(iCenterX - i4114, iCenterY3 - i4115, i4114 + iCenterX, i4115 + iCenterY3);
                    drawable3.draw(canvas2);
                }
                if (drawable4 != null) {
                    int intrinsicWidth10 = (int) (drawable4.getIntrinsicWidth() * f20);
                    int intrinsicHeight10 = (int) (drawable4.getIntrinsicHeight() * f20);
                    drawable4.setColorFilter(this.colorFilter);
                    if (this.currentIcon == this.nextIcon) {
                        i26 = 255;
                    } else {
                        i26 = (int) (this.transitionProgress * 255.0f);
                    }
                    drawable4.setAlpha(i26);
                    int i511 = intrinsicWidth10 / 2;
                    int i512 = intrinsicHeight10 / 2;
                    drawable4.setBounds(iCenterX - i511, iCenterY3 - i512, i511 + iCenterX, i512 + iCenterY3);
                    drawable4.draw(canvas2);
                }
                if (pathArr4 != null) {
                    int iDp11111 = AndroidUtilities.dp(24.0f);
                    this.paint2.setStyle(Paint.Style.FILL_AND_STROKE);
                    Paint paint113 = this.paint2;
                    if (this.currentIcon == this.nextIcon) {
                        i25 = 255;
                    } else {
                        i25 = (int) ((1.0f - this.transitionProgress) * 255.0f);
                    }
                    paint113.setAlpha(i25);
                    applyShaderMatrix(true);
                    canvas2.save();
                    canvas2.translate(iCenterX, iCenterY3);
                    canvas2.scale(f19, f19);
                    float f10110 = (-iDp11111) / 2;
                    canvas2.translate(f10110, f10110);
                    path4 = pathArr4[0];
                    if (path4 != null) {
                        canvas2.drawPath(path4, this.paint2);
                    }
                    path5 = pathArr4[1];
                    if (path5 != null) {
                        canvas2.drawPath(path5, this.backPaint);
                    }
                    canvas2.restore();
                }
                if (pathArr3 != null) {
                    int iDp11112 = AndroidUtilities.dp(24.0f);
                    if (this.currentIcon == this.nextIcon) {
                        i24 = 255;
                    } else {
                        i24 = (int) (this.transitionProgress * 255.0f);
                    }
                    this.paint2.setStyle(Paint.Style.FILL_AND_STROKE);
                    this.paint2.setAlpha(i24);
                    applyShaderMatrix(true);
                    canvas2.save();
                    canvas2.translate(iCenterX, iCenterY3);
                    float f10111 = f20;
                    canvas2.scale(f10111, f10111);
                    float f10112 = (-iDp11112) / 2;
                    canvas2.translate(f10112, f10112);
                    path = pathArr3[0];
                    if (path != null) {
                        canvas2.drawPath(path, this.paint2);
                    }
                    if (pathArr3.length >= 3) {
                        canvas2.drawPath(path3, this.paint);
                    }
                    path2 = pathArr3[1];
                    if (path2 != null) {
                        if (i24 != 255) {
                            int alpha5 = this.backPaint.getAlpha();
                            this.backPaint.setAlpha((int) (alpha5 * (i24 / 255.0f)));
                            canvas2.drawPath(pathArr3[1], this.backPaint);
                            this.backPaint.setAlpha(alpha5);
                        } else {
                            canvas2.drawPath(path2, this.backPaint);
                        }
                    }
                    canvas2.restore();
                }
                long jCurrentTimeMillis5 = System.currentTimeMillis();
                j = jCurrentTimeMillis5 - this.lastAnimationTime;
                if (j > 17) {
                    j = 17;
                }
                this.lastAnimationTime = jCurrentTimeMillis5;
                i22 = this.currentIcon;
                if (i22 != 3) {
                    float f10113 = this.downloadRadOffset + ((360 * j) / 2500.0f);
                    this.downloadRadOffset = f10113;
                    this.downloadRadOffset = getCircleValue(f10113);
                    if (this.nextIcon != 2) {
                        f31 = this.downloadProgress;
                        f32 = this.downloadProgressAnimationStart;
                        f33 = f31 - f32;
                        if (f33 > 0.0f) {
                            f34 = this.downloadProgressTime + j;
                            this.downloadProgressTime = f34;
                            if (f34 >= 200.0f) {
                                this.animatedDownloadProgress = f31;
                                this.downloadProgressAnimationStart = f31;
                                this.downloadProgressTime = 0.0f;
                            } else {
                                this.animatedDownloadProgress = f32 + (f33 * this.interpolator.getInterpolation(f34 / 200.0f));
                            }
                        }
                    }
                    invalidateSelf();
                } else {
                    float f10114 = this.downloadRadOffset + ((360 * j) / 2500.0f);
                    this.downloadRadOffset = f10114;
                    this.downloadRadOffset = getCircleValue(f10114);
                    if (this.nextIcon != 2) {
                        f31 = this.downloadProgress;
                        f32 = this.downloadProgressAnimationStart;
                        f33 = f31 - f32;
                        if (f33 > 0.0f) {
                            f34 = this.downloadProgressTime + j;
                            this.downloadProgressTime = f34;
                            if (f34 >= 200.0f) {
                                this.animatedDownloadProgress = f31;
                                this.downloadProgressAnimationStart = f31;
                                this.downloadProgressTime = 0.0f;
                            } else {
                                this.animatedDownloadProgress = f32 + (f33 * this.interpolator.getInterpolation(f34 / 200.0f));
                            }
                        }
                    }
                    invalidateSelf();
                }
                if (this.animatingTransition) {
                    f35 = this.transitionProgress;
                    if (f35 < 1.0f) {
                        f36 = f35 + (j / this.transitionAnimationTime);
                        this.transitionProgress = f36;
                        if (f36 >= 1.0f) {
                            this.currentIcon = this.nextIcon;
                            this.transitionProgress = 1.0f;
                            this.animatingTransition = false;
                        }
                        invalidateSelf();
                    }
                }
                i23 = i11;
                if (i23 >= 1) {
                    canvas2.restoreToCount(i23);
                }
            }
            pathArr2 = null;
            if (i4 == 5) {
                pathArr = Theme.chat_filePath;
            } else if (this.currentIcon == 5) {
                pathArr2 = Theme.chat_filePath;
            }
            pathArr3 = pathArr;
            pathArr4 = pathArr2;
            if (i4 == 7) {
                drawable2 = Theme.chat_flameIcon;
                drawable = null;
                i5 = 8;
            } else {
                if (this.currentIcon == 7) {
                    drawable = Theme.chat_flameIcon;
                } else {
                    drawable = null;
                }
                i5 = 8;
                drawable2 = null;
            }
            if (i4 == i5) {
                drawable2 = Theme.chat_gifIcon;
            } else if (this.currentIcon == i5) {
                drawable = Theme.chat_gifIcon;
            }
            drawable3 = drawable;
            if (this.currentIcon != 9) {
                applyShaderMatrix(false);
                Paint paint114 = this.paint;
                if (this.currentIcon == this.nextIcon) {
                    i6 = 255;
                } else {
                    i6 = (int) (this.transitionProgress * 255.0f);
                }
                paint114.setAlpha(i6);
                int iDp11113 = AndroidUtilities.dp(7.0f) + iCenterY3;
                int iDp11114 = iCenterX - AndroidUtilities.dp(3.0f);
                if (this.currentIcon != this.nextIcon) {
                    canvas2.save();
                    float f818 = this.transitionProgress;
                    canvas2.scale(f818, f818, iCenterX, iCenterY3);
                }
                float f91128 = iDp11114;
                float f91129 = iDp11113;
                rect = bounds;
                drawable4 = drawable2;
                canvas2.drawLine(iDp11114 - AndroidUtilities.dp(6.0f), iDp11113 - AndroidUtilities.dp(6.0f), f91128, f91129, this.paint);
                canvas2 = canvas;
                canvas2.drawLine(f91128, f91129, iDp11114 + AndroidUtilities.dp(12.0f), iDp11113 - AndroidUtilities.dp(12.0f), this.paint);
                if (this.currentIcon != this.nextIcon) {
                    canvas2.restore();
                }
            } else {
                applyShaderMatrix(false);
                Paint paint115 = this.paint;
                if (this.currentIcon == this.nextIcon) {
                    i6 = 255;
                } else {
                    i6 = (int) (this.transitionProgress * 255.0f);
                }
                paint115.setAlpha(i6);
                int iDp11115 = AndroidUtilities.dp(7.0f) + iCenterY3;
                int iDp11116 = iCenterX - AndroidUtilities.dp(3.0f);
                if (this.currentIcon != this.nextIcon) {
                    canvas2.save();
                    float f819 = this.transitionProgress;
                    canvas2.scale(f819, f819, iCenterX, iCenterY3);
                }
                float f911210 = iDp11116;
                float f911211 = iDp11115;
                rect = bounds;
                drawable4 = drawable2;
                canvas2.drawLine(iDp11116 - AndroidUtilities.dp(6.0f), iDp11115 - AndroidUtilities.dp(6.0f), f911210, f911211, this.paint);
                canvas2 = canvas;
                canvas2.drawLine(f911210, f911211, iDp11116 + AndroidUtilities.dp(12.0f), iDp11115 - AndroidUtilities.dp(12.0f), this.paint);
                if (this.currentIcon != this.nextIcon) {
                    canvas2.restore();
                }
            }
            if (this.currentIcon != 12) {
                applyShaderMatrix(false);
                i7 = this.currentIcon;
                i8 = this.nextIcon;
                if (i7 == i8) {
                    i9 = 13;
                    f21 = 1.0f;
                } else {
                    i9 = 13;
                    if (i8 == 13) {
                        f21 = this.transitionProgress;
                    } else {
                        f21 = 1.0f - this.transitionProgress;
                    }
                }
                Paint paint116 = this.paint;
                if (i7 == i8) {
                    i10 = 255;
                } else {
                    i10 = (int) (f21 * 255.0f);
                }
                paint116.setAlpha(i10);
                AndroidUtilities.dp(7.0f);
                AndroidUtilities.dp(3.0f);
                if (this.currentIcon != this.nextIcon) {
                    canvas2.save();
                    canvas2.scale(f21, f21, iCenterX, iCenterY3);
                }
                float fDp117 = AndroidUtilities.dp(7.0f) * this.scale;
                float f91111116 = iCenterX;
                float f91111117 = f91111116 - fDp117;
                float f91111118 = iCenterY3;
                float f91111119 = f91111118 - fDp117;
                float f911111110 = f91111116 + fDp117;
                float f911111111 = fDp117 + f91111118;
                i11 = i;
                i12 = i9;
                canvas2.drawLine(f91111117, f91111119, f911111110, f911111111, this.paint);
                canvas2 = canvas;
                canvas2.drawLine(f911111110, f91111119, f91111117, f911111111, this.paint);
                if (this.currentIcon != this.nextIcon) {
                    canvas2.restore();
                }
            } else {
                applyShaderMatrix(false);
                i7 = this.currentIcon;
                i8 = this.nextIcon;
                if (i7 == i8) {
                    i9 = 13;
                    f21 = 1.0f;
                } else {
                    i9 = 13;
                    if (i8 == 13) {
                        f21 = this.transitionProgress;
                    } else {
                        f21 = 1.0f - this.transitionProgress;
                    }
                }
                Paint paint117 = this.paint;
                if (i7 == i8) {
                    i10 = 255;
                } else {
                    i10 = (int) (f21 * 255.0f);
                }
                paint117.setAlpha(i10);
                AndroidUtilities.dp(7.0f);
                AndroidUtilities.dp(3.0f);
                if (this.currentIcon != this.nextIcon) {
                    canvas2.save();
                    canvas2.scale(f21, f21, iCenterX, iCenterY3);
                }
                float fDp118 = AndroidUtilities.dp(7.0f) * this.scale;
                float f911111112 = iCenterX;
                float f911111113 = f911111112 - fDp118;
                float f911111114 = iCenterY3;
                float f911111115 = f911111114 - fDp118;
                float f911111116 = f911111112 + fDp118;
                float f911111117 = fDp118 + f911111114;
                i11 = i;
                i12 = i9;
                canvas2.drawLine(f911111113, f911111115, f911111116, f911111117, this.paint);
                canvas2 = canvas;
                canvas2.drawLine(f911111116, f911111115, f911111113, f911111117, this.paint);
                if (this.currentIcon != this.nextIcon) {
                    canvas2.restore();
                }
            }
            if (this.currentIcon != i12) {
                applyShaderMatrix(false);
                i13 = this.currentIcon;
                i14 = this.nextIcon;
                if (i13 == i14) {
                    f22 = 1.0f;
                } else if (i14 == i12) {
                    f22 = this.transitionProgress;
                } else {
                    f22 = 1.0f - this.transitionProgress;
                }
                this.textPaint.setAlpha((int) (f22 * 255.0f));
                int iDp11117 = AndroidUtilities.dp(5.0f) + iCenterY3;
                int i4116 = iCenterX - (this.percentStringWidth / 2);
                f23 = 5.0f;
                if (this.currentIcon != this.nextIcon) {
                    canvas2.save();
                    canvas2.scale(f22, f22, iCenterX, iCenterY3);
                }
                i15 = (int) (this.animatedDownloadProgress * 100.0f);
                if (this.percentString != null) {
                    this.lastPercent = i15;
                    String str18 = String.format("%d%%", Integer.valueOf(i15));
                    this.percentString = str18;
                    this.percentStringWidth = (int) Math.ceil(this.textPaint.measureText(str18));
                } else {
                    this.lastPercent = i15;
                    String str19 = String.format("%d%%", Integer.valueOf(i15));
                    this.percentString = str19;
                    this.percentStringWidth = (int) Math.ceil(this.textPaint.measureText(str19));
                }
                canvas2.drawText(this.percentString, i4116, iDp11117, this.textPaint);
                if (this.currentIcon != this.nextIcon) {
                    canvas2.restore();
                }
            } else {
                applyShaderMatrix(false);
                i13 = this.currentIcon;
                i14 = this.nextIcon;
                if (i13 == i14) {
                    f22 = 1.0f;
                } else if (i14 == i12) {
                    f22 = this.transitionProgress;
                } else {
                    f22 = 1.0f - this.transitionProgress;
                }
                this.textPaint.setAlpha((int) (f22 * 255.0f));
                int iDp11118 = AndroidUtilities.dp(5.0f) + iCenterY3;
                int i4117 = iCenterX - (this.percentStringWidth / 2);
                f23 = 5.0f;
                if (this.currentIcon != this.nextIcon) {
                    canvas2.save();
                    canvas2.scale(f22, f22, iCenterX, iCenterY3);
                }
                i15 = (int) (this.animatedDownloadProgress * 100.0f);
                if (this.percentString != null) {
                    this.lastPercent = i15;
                    String str110 = String.format("%d%%", Integer.valueOf(i15));
                    this.percentString = str110;
                    this.percentStringWidth = (int) Math.ceil(this.textPaint.measureText(str110));
                } else {
                    this.lastPercent = i15;
                    String str111 = String.format("%d%%", Integer.valueOf(i15));
                    this.percentString = str111;
                    this.percentStringWidth = (int) Math.ceil(this.textPaint.measureText(str111));
                }
                canvas2.drawText(this.percentString, i4117, iDp11118, this.textPaint);
                if (this.currentIcon != this.nextIcon) {
                    canvas2.restore();
                }
            }
            i16 = this.currentIcon;
            i17 = 1;
            if (i16 != 0) {
                if (i16 == 0) {
                    if (i16 == 1) {
                        if (this.nextIcon != 0) {
                            i17 = 1;
                        } else if (this.animatingTransition) {
                            if (this.nextIcon == 0) {
                                f24 = 1.0f - this.transitionProgress;
                            } else {
                                f24 = this.transitionProgress;
                            }
                            i17 = 1;
                        } else {
                            i17 = 1;
                            if (this.nextIcon == 1) {
                                f24 = 1.0f;
                            } else {
                                f24 = 0.0f;
                            }
                        }
                        i18 = this.nextIcon;
                        if (i18 != 0) {
                            this.paint2.setAlpha(255);
                        } else {
                            this.paint2.setAlpha(255);
                        }
                        applyShaderMatrix(true);
                        canvas2.save();
                        canvas2.translate(rect.centerX() + (AndroidUtilities.dp(1.0f) * (1.0f - f24)), rect.centerY());
                        f25 = f24 * 500.0f;
                        i19 = this.currentIcon;
                        if (i19 == 1) {
                            f26 = 90.0f;
                        } else {
                            f26 = 0.0f;
                        }
                        if (i19 == 0) {
                            if (i19 == 1) {
                                if (f25 < 100.0f) {
                                    interpolation = CubicBezierInterpolator.EASE_BOTH.getInterpolation(f25 / 100.0f) * (-5.0f);
                                } else if (f25 < 484.0f) {
                                    interpolation = (CubicBezierInterpolator.EASE_BOTH.getInterpolation((f25 - 100.0f) / 384.0f) * 95.0f) - 5.0f;
                                }
                                f26 = interpolation;
                            }
                        } else if (i19 == 1) {
                            if (f25 < 100.0f) {
                                interpolation = CubicBezierInterpolator.EASE_BOTH.getInterpolation(f25 / 100.0f) * (-5.0f);
                            } else if (f25 < 484.0f) {
                                interpolation = (CubicBezierInterpolator.EASE_BOTH.getInterpolation((f25 - 100.0f) / 384.0f) * 95.0f) - 5.0f;
                            }
                            f26 = interpolation;
                        }
                        canvas2.rotate(f26);
                        i20 = this.currentIcon;
                        if (i20 == 0) {
                            canvas2.scale(f20, f20);
                        } else {
                            canvas2.scale(f20, f20);
                        }
                        Theme.playPauseAnimator.draw(canvas2, this.paint2, f25);
                        canvas2.scale(1.0f, -1.0f);
                        Theme.playPauseAnimator.draw(canvas2, this.paint2, f25);
                        canvas2.restore();
                    }
                    if (i16 == i17) {
                        f24 = 1.0f;
                    } else {
                        f24 = 0.0f;
                    }
                    i18 = this.nextIcon;
                    if (i18 != 0) {
                        this.paint2.setAlpha(255);
                    } else {
                        this.paint2.setAlpha(255);
                    }
                    applyShaderMatrix(true);
                    canvas2.save();
                    canvas2.translate(rect.centerX() + (AndroidUtilities.dp(1.0f) * (1.0f - f24)), rect.centerY());
                    f25 = f24 * 500.0f;
                    i19 = this.currentIcon;
                    if (i19 == 1) {
                        f26 = 90.0f;
                    } else {
                        f26 = 0.0f;
                    }
                    if (i19 == 0) {
                        if (i19 == 1) {
                            if (f25 < 100.0f) {
                                interpolation = CubicBezierInterpolator.EASE_BOTH.getInterpolation(f25 / 100.0f) * (-5.0f);
                            } else if (f25 < 484.0f) {
                                interpolation = (CubicBezierInterpolator.EASE_BOTH.getInterpolation((f25 - 100.0f) / 384.0f) * 95.0f) - 5.0f;
                            }
                            f26 = interpolation;
                        }
                    } else if (i19 == 1) {
                        if (f25 < 100.0f) {
                            interpolation = CubicBezierInterpolator.EASE_BOTH.getInterpolation(f25 / 100.0f) * (-5.0f);
                        } else if (f25 < 484.0f) {
                            interpolation = (CubicBezierInterpolator.EASE_BOTH.getInterpolation((f25 - 100.0f) / 384.0f) * 95.0f) - 5.0f;
                        }
                        f26 = interpolation;
                    }
                    canvas2.rotate(f26);
                    i20 = this.currentIcon;
                    if (i20 == 0) {
                        canvas2.scale(f20, f20);
                    } else {
                        canvas2.scale(f20, f20);
                    }
                    Theme.playPauseAnimator.draw(canvas2, this.paint2, f25);
                    canvas2.scale(1.0f, -1.0f);
                    Theme.playPauseAnimator.draw(canvas2, this.paint2, f25);
                    canvas2.restore();
                } else {
                    if (i16 == 1) {
                        if (this.nextIcon != 0) {
                            i17 = 1;
                        } else if (this.animatingTransition) {
                            if (this.nextIcon == 0) {
                                f24 = 1.0f - this.transitionProgress;
                            } else {
                                f24 = this.transitionProgress;
                            }
                            i17 = 1;
                        } else {
                            i17 = 1;
                            if (this.nextIcon == 1) {
                                f24 = 1.0f;
                            } else {
                                f24 = 0.0f;
                            }
                        }
                        i18 = this.nextIcon;
                        if (i18 != 0) {
                            this.paint2.setAlpha(255);
                        } else {
                            this.paint2.setAlpha(255);
                        }
                        applyShaderMatrix(true);
                        canvas2.save();
                        canvas2.translate(rect.centerX() + (AndroidUtilities.dp(1.0f) * (1.0f - f24)), rect.centerY());
                        f25 = f24 * 500.0f;
                        i19 = this.currentIcon;
                        if (i19 == 1) {
                            f26 = 90.0f;
                        } else {
                            f26 = 0.0f;
                        }
                        if (i19 == 0) {
                            if (i19 == 1) {
                                if (f25 < 100.0f) {
                                    interpolation = CubicBezierInterpolator.EASE_BOTH.getInterpolation(f25 / 100.0f) * (-5.0f);
                                } else if (f25 < 484.0f) {
                                    interpolation = (CubicBezierInterpolator.EASE_BOTH.getInterpolation((f25 - 100.0f) / 384.0f) * 95.0f) - 5.0f;
                                }
                                f26 = interpolation;
                            }
                        } else if (i19 == 1) {
                            if (f25 < 100.0f) {
                                interpolation = CubicBezierInterpolator.EASE_BOTH.getInterpolation(f25 / 100.0f) * (-5.0f);
                            } else if (f25 < 484.0f) {
                                interpolation = (CubicBezierInterpolator.EASE_BOTH.getInterpolation((f25 - 100.0f) / 384.0f) * 95.0f) - 5.0f;
                            }
                            f26 = interpolation;
                        }
                        canvas2.rotate(f26);
                        i20 = this.currentIcon;
                        if (i20 == 0) {
                            canvas2.scale(f20, f20);
                        } else {
                            canvas2.scale(f20, f20);
                        }
                        Theme.playPauseAnimator.draw(canvas2, this.paint2, f25);
                        canvas2.scale(1.0f, -1.0f);
                        Theme.playPauseAnimator.draw(canvas2, this.paint2, f25);
                        canvas2.restore();
                    }
                    if (i16 == i17) {
                        f24 = 1.0f;
                    } else {
                        f24 = 0.0f;
                    }
                    i18 = this.nextIcon;
                    if (i18 != 0) {
                        this.paint2.setAlpha(255);
                    } else {
                        this.paint2.setAlpha(255);
                    }
                    applyShaderMatrix(true);
                    canvas2.save();
                    canvas2.translate(rect.centerX() + (AndroidUtilities.dp(1.0f) * (1.0f - f24)), rect.centerY());
                    f25 = f24 * 500.0f;
                    i19 = this.currentIcon;
                    if (i19 == 1) {
                        f26 = 90.0f;
                    } else {
                        f26 = 0.0f;
                    }
                    if (i19 == 0) {
                        if (i19 == 1) {
                            if (f25 < 100.0f) {
                                interpolation = CubicBezierInterpolator.EASE_BOTH.getInterpolation(f25 / 100.0f) * (-5.0f);
                            } else if (f25 < 484.0f) {
                                interpolation = (CubicBezierInterpolator.EASE_BOTH.getInterpolation((f25 - 100.0f) / 384.0f) * 95.0f) - 5.0f;
                            }
                            f26 = interpolation;
                        }
                    } else if (i19 == 1) {
                        if (f25 < 100.0f) {
                            interpolation = CubicBezierInterpolator.EASE_BOTH.getInterpolation(f25 / 100.0f) * (-5.0f);
                        } else if (f25 < 484.0f) {
                            interpolation = (CubicBezierInterpolator.EASE_BOTH.getInterpolation((f25 - 100.0f) / 384.0f) * 95.0f) - 5.0f;
                        }
                        f26 = interpolation;
                    }
                    canvas2.rotate(f26);
                    i20 = this.currentIcon;
                    if (i20 == 0) {
                        canvas2.scale(f20, f20);
                    } else {
                        canvas2.scale(f20, f20);
                    }
                    Theme.playPauseAnimator.draw(canvas2, this.paint2, f25);
                    canvas2.scale(1.0f, -1.0f);
                    Theme.playPauseAnimator.draw(canvas2, this.paint2, f25);
                    canvas2.restore();
                }
            } else if (i16 == 0) {
                if (i16 == 1) {
                    if (this.nextIcon != 0) {
                        i17 = 1;
                    } else if (this.animatingTransition) {
                        if (this.nextIcon == 0) {
                            f24 = 1.0f - this.transitionProgress;
                        } else {
                            f24 = this.transitionProgress;
                        }
                        i17 = 1;
                    } else {
                        i17 = 1;
                        if (this.nextIcon == 1) {
                            f24 = 1.0f;
                        } else {
                            f24 = 0.0f;
                        }
                    }
                    i18 = this.nextIcon;
                    if (i18 != 0) {
                        this.paint2.setAlpha(255);
                    } else {
                        this.paint2.setAlpha(255);
                    }
                    applyShaderMatrix(true);
                    canvas2.save();
                    canvas2.translate(rect.centerX() + (AndroidUtilities.dp(1.0f) * (1.0f - f24)), rect.centerY());
                    f25 = f24 * 500.0f;
                    i19 = this.currentIcon;
                    if (i19 == 1) {
                        f26 = 90.0f;
                    } else {
                        f26 = 0.0f;
                    }
                    if (i19 == 0) {
                        if (i19 == 1) {
                            if (f25 < 100.0f) {
                                interpolation = CubicBezierInterpolator.EASE_BOTH.getInterpolation(f25 / 100.0f) * (-5.0f);
                            } else if (f25 < 484.0f) {
                                interpolation = (CubicBezierInterpolator.EASE_BOTH.getInterpolation((f25 - 100.0f) / 384.0f) * 95.0f) - 5.0f;
                            }
                            f26 = interpolation;
                        }
                    } else if (i19 == 1) {
                        if (f25 < 100.0f) {
                            interpolation = CubicBezierInterpolator.EASE_BOTH.getInterpolation(f25 / 100.0f) * (-5.0f);
                        } else if (f25 < 484.0f) {
                            interpolation = (CubicBezierInterpolator.EASE_BOTH.getInterpolation((f25 - 100.0f) / 384.0f) * 95.0f) - 5.0f;
                        }
                        f26 = interpolation;
                    }
                    canvas2.rotate(f26);
                    i20 = this.currentIcon;
                    if (i20 == 0) {
                        canvas2.scale(f20, f20);
                    } else {
                        canvas2.scale(f20, f20);
                    }
                    Theme.playPauseAnimator.draw(canvas2, this.paint2, f25);
                    canvas2.scale(1.0f, -1.0f);
                    Theme.playPauseAnimator.draw(canvas2, this.paint2, f25);
                    canvas2.restore();
                }
                if (i16 == i17) {
                    f24 = 1.0f;
                } else {
                    f24 = 0.0f;
                }
                i18 = this.nextIcon;
                if (i18 != 0) {
                    this.paint2.setAlpha(255);
                } else {
                    this.paint2.setAlpha(255);
                }
                applyShaderMatrix(true);
                canvas2.save();
                canvas2.translate(rect.centerX() + (AndroidUtilities.dp(1.0f) * (1.0f - f24)), rect.centerY());
                f25 = f24 * 500.0f;
                i19 = this.currentIcon;
                if (i19 == 1) {
                    f26 = 90.0f;
                } else {
                    f26 = 0.0f;
                }
                if (i19 == 0) {
                    if (i19 == 1) {
                        if (f25 < 100.0f) {
                            interpolation = CubicBezierInterpolator.EASE_BOTH.getInterpolation(f25 / 100.0f) * (-5.0f);
                        } else if (f25 < 484.0f) {
                            interpolation = (CubicBezierInterpolator.EASE_BOTH.getInterpolation((f25 - 100.0f) / 384.0f) * 95.0f) - 5.0f;
                        }
                        f26 = interpolation;
                    }
                } else if (i19 == 1) {
                    if (f25 < 100.0f) {
                        interpolation = CubicBezierInterpolator.EASE_BOTH.getInterpolation(f25 / 100.0f) * (-5.0f);
                    } else if (f25 < 484.0f) {
                        interpolation = (CubicBezierInterpolator.EASE_BOTH.getInterpolation((f25 - 100.0f) / 384.0f) * 95.0f) - 5.0f;
                    }
                    f26 = interpolation;
                }
                canvas2.rotate(f26);
                i20 = this.currentIcon;
                if (i20 == 0) {
                    canvas2.scale(f20, f20);
                } else {
                    canvas2.scale(f20, f20);
                }
                Theme.playPauseAnimator.draw(canvas2, this.paint2, f25);
                canvas2.scale(1.0f, -1.0f);
                Theme.playPauseAnimator.draw(canvas2, this.paint2, f25);
                canvas2.restore();
            } else {
                if (i16 == 1) {
                    if (this.nextIcon != 0) {
                        i17 = 1;
                    } else if (this.animatingTransition) {
                        if (this.nextIcon == 0) {
                            f24 = 1.0f - this.transitionProgress;
                        } else {
                            f24 = this.transitionProgress;
                        }
                        i17 = 1;
                    } else {
                        i17 = 1;
                        if (this.nextIcon == 1) {
                            f24 = 1.0f;
                        } else {
                            f24 = 0.0f;
                        }
                    }
                    i18 = this.nextIcon;
                    if (i18 != 0) {
                        this.paint2.setAlpha(255);
                    } else {
                        this.paint2.setAlpha(255);
                    }
                    applyShaderMatrix(true);
                    canvas2.save();
                    canvas2.translate(rect.centerX() + (AndroidUtilities.dp(1.0f) * (1.0f - f24)), rect.centerY());
                    f25 = f24 * 500.0f;
                    i19 = this.currentIcon;
                    if (i19 == 1) {
                        f26 = 90.0f;
                    } else {
                        f26 = 0.0f;
                    }
                    if (i19 == 0) {
                        if (i19 == 1) {
                            if (f25 < 100.0f) {
                                interpolation = CubicBezierInterpolator.EASE_BOTH.getInterpolation(f25 / 100.0f) * (-5.0f);
                            } else if (f25 < 484.0f) {
                                interpolation = (CubicBezierInterpolator.EASE_BOTH.getInterpolation((f25 - 100.0f) / 384.0f) * 95.0f) - 5.0f;
                            }
                            f26 = interpolation;
                        }
                    } else if (i19 == 1) {
                        if (f25 < 100.0f) {
                            interpolation = CubicBezierInterpolator.EASE_BOTH.getInterpolation(f25 / 100.0f) * (-5.0f);
                        } else if (f25 < 484.0f) {
                            interpolation = (CubicBezierInterpolator.EASE_BOTH.getInterpolation((f25 - 100.0f) / 384.0f) * 95.0f) - 5.0f;
                        }
                        f26 = interpolation;
                    }
                    canvas2.rotate(f26);
                    i20 = this.currentIcon;
                    if (i20 == 0) {
                        canvas2.scale(f20, f20);
                    } else {
                        canvas2.scale(f20, f20);
                    }
                    Theme.playPauseAnimator.draw(canvas2, this.paint2, f25);
                    canvas2.scale(1.0f, -1.0f);
                    Theme.playPauseAnimator.draw(canvas2, this.paint2, f25);
                    canvas2.restore();
                }
                if (i16 == i17) {
                    f24 = 1.0f;
                } else {
                    f24 = 0.0f;
                }
                i18 = this.nextIcon;
                if (i18 != 0) {
                    this.paint2.setAlpha(255);
                } else {
                    this.paint2.setAlpha(255);
                }
                applyShaderMatrix(true);
                canvas2.save();
                canvas2.translate(rect.centerX() + (AndroidUtilities.dp(1.0f) * (1.0f - f24)), rect.centerY());
                f25 = f24 * 500.0f;
                i19 = this.currentIcon;
                if (i19 == 1) {
                    f26 = 90.0f;
                } else {
                    f26 = 0.0f;
                }
                if (i19 == 0) {
                    if (i19 == 1) {
                        if (f25 < 100.0f) {
                            interpolation = CubicBezierInterpolator.EASE_BOTH.getInterpolation(f25 / 100.0f) * (-5.0f);
                        } else if (f25 < 484.0f) {
                            interpolation = (CubicBezierInterpolator.EASE_BOTH.getInterpolation((f25 - 100.0f) / 384.0f) * 95.0f) - 5.0f;
                        }
                        f26 = interpolation;
                    }
                } else if (i19 == 1) {
                    if (f25 < 100.0f) {
                        interpolation = CubicBezierInterpolator.EASE_BOTH.getInterpolation(f25 / 100.0f) * (-5.0f);
                    } else if (f25 < 484.0f) {
                        interpolation = (CubicBezierInterpolator.EASE_BOTH.getInterpolation((f25 - 100.0f) / 384.0f) * 95.0f) - 5.0f;
                    }
                    f26 = interpolation;
                }
                canvas2.rotate(f26);
                i20 = this.currentIcon;
                if (i20 == 0) {
                    canvas2.scale(f20, f20);
                } else {
                    canvas2.scale(f20, f20);
                }
                Theme.playPauseAnimator.draw(canvas2, this.paint2, f25);
                canvas2.scale(1.0f, -1.0f);
                Theme.playPauseAnimator.draw(canvas2, this.paint2, f25);
                canvas2.restore();
            }
            if (this.currentIcon != 6) {
                applyShaderMatrix(false);
                if (this.currentIcon != 6) {
                    f28 = this.transitionProgress;
                    if (f28 > 0.5f) {
                        f30 = (f28 - 0.5f) / 0.5f;
                        fMin2 = 1.0f - Math.min(1.0f, f30 / 0.5f);
                        if (f30 > 0.5f) {
                            f29 = (f30 - 0.5f) / 0.5f;
                        } else {
                            f29 = 0.0f;
                        }
                    } else {
                        f29 = 0.0f;
                        fMin2 = 1.0f;
                    }
                    this.paint.setAlpha(255);
                    f27 = f29;
                } else {
                    if (this.nextIcon != 6) {
                        this.paint.setAlpha((int) ((1.0f - this.transitionProgress) * 255.0f));
                    } else {
                        this.paint.setAlpha(255);
                    }
                    fMin2 = 0.0f;
                    f27 = 1.0f;
                }
                iDp = AndroidUtilities.dp(7.0f) + iCenterY3;
                iDp2 = iCenterX - AndroidUtilities.dp(3.0f);
                if (fMin2 < 1.0f) {
                    i21 = iDp2;
                    canvas.drawLine(iDp2 - AndroidUtilities.dp(6.0f), iDp - AndroidUtilities.dp(6.0f), iDp2 - (AndroidUtilities.dp(6.0f) * fMin2), iDp - (AndroidUtilities.dp(6.0f) * fMin2), this.paint);
                } else {
                    i21 = iDp2;
                }
                if (f27 > 0.0f) {
                    float f9216 = i21;
                    float f9217 = iDp;
                    canvas2 = canvas;
                    canvas2.drawLine(f9216, f9217, f9216 + (AndroidUtilities.dp(12.0f) * f27), f9217 - (AndroidUtilities.dp(12.0f) * f27), this.paint);
                } else {
                    canvas2 = canvas;
                }
            } else {
                applyShaderMatrix(false);
                if (this.currentIcon != 6) {
                    f28 = this.transitionProgress;
                    if (f28 > 0.5f) {
                        f30 = (f28 - 0.5f) / 0.5f;
                        fMin2 = 1.0f - Math.min(1.0f, f30 / 0.5f);
                        if (f30 > 0.5f) {
                            f29 = (f30 - 0.5f) / 0.5f;
                        } else {
                            f29 = 0.0f;
                        }
                    } else {
                        f29 = 0.0f;
                        fMin2 = 1.0f;
                    }
                    this.paint.setAlpha(255);
                    f27 = f29;
                } else {
                    if (this.nextIcon != 6) {
                        this.paint.setAlpha((int) ((1.0f - this.transitionProgress) * 255.0f));
                    } else {
                        this.paint.setAlpha(255);
                    }
                    fMin2 = 0.0f;
                    f27 = 1.0f;
                }
                iDp = AndroidUtilities.dp(7.0f) + iCenterY3;
                iDp2 = iCenterX - AndroidUtilities.dp(3.0f);
                if (fMin2 < 1.0f) {
                    i21 = iDp2;
                    canvas.drawLine(iDp2 - AndroidUtilities.dp(6.0f), iDp - AndroidUtilities.dp(6.0f), iDp2 - (AndroidUtilities.dp(6.0f) * fMin2), iDp - (AndroidUtilities.dp(6.0f) * fMin2), this.paint);
                } else {
                    i21 = iDp2;
                }
                if (f27 > 0.0f) {
                    float f9218 = i21;
                    float f9219 = iDp;
                    canvas2 = canvas;
                    canvas2.drawLine(f9218, f9219, f9218 + (AndroidUtilities.dp(12.0f) * f27), f9219 - (AndroidUtilities.dp(12.0f) * f27), this.paint);
                } else {
                    canvas2 = canvas;
                }
            }
            if (drawable3 != null) {
                int intrinsicWidth11 = (int) (drawable3.getIntrinsicWidth() * f19);
                int intrinsicHeight11 = (int) (drawable3.getIntrinsicHeight() * f19);
                drawable3.setColorFilter(this.colorFilter);
                if (this.currentIcon == this.nextIcon) {
                    i27 = 255;
                } else {
                    i27 = (int) ((1.0f - this.transitionProgress) * 255.0f);
                }
                drawable3.setAlpha(i27);
                int i4118 = intrinsicWidth11 / 2;
                int i4119 = intrinsicHeight11 / 2;
                drawable3.setBounds(iCenterX - i4118, iCenterY3 - i4119, i4118 + iCenterX, i4119 + iCenterY3);
                drawable3.draw(canvas2);
            }
            if (drawable4 != null) {
                int intrinsicWidth12 = (int) (drawable4.getIntrinsicWidth() * f20);
                int intrinsicHeight12 = (int) (drawable4.getIntrinsicHeight() * f20);
                drawable4.setColorFilter(this.colorFilter);
                if (this.currentIcon == this.nextIcon) {
                    i26 = 255;
                } else {
                    i26 = (int) (this.transitionProgress * 255.0f);
                }
                drawable4.setAlpha(i26);
                int i513 = intrinsicWidth12 / 2;
                int i514 = intrinsicHeight12 / 2;
                drawable4.setBounds(iCenterX - i513, iCenterY3 - i514, i513 + iCenterX, i514 + iCenterY3);
                drawable4.draw(canvas2);
            }
            if (pathArr4 != null) {
                int iDp11119 = AndroidUtilities.dp(24.0f);
                this.paint2.setStyle(Paint.Style.FILL_AND_STROKE);
                Paint paint118 = this.paint2;
                if (this.currentIcon == this.nextIcon) {
                    i25 = 255;
                } else {
                    i25 = (int) ((1.0f - this.transitionProgress) * 255.0f);
                }
                paint118.setAlpha(i25);
                applyShaderMatrix(true);
                canvas2.save();
                canvas2.translate(iCenterX, iCenterY3);
                canvas2.scale(f19, f19);
                float f10115 = (-iDp11119) / 2;
                canvas2.translate(f10115, f10115);
                path4 = pathArr4[0];
                if (path4 != null) {
                    canvas2.drawPath(path4, this.paint2);
                }
                path5 = pathArr4[1];
                if (path5 != null) {
                    canvas2.drawPath(path5, this.backPaint);
                }
                canvas2.restore();
            }
            if (pathArr3 != null) {
                int iDp111110 = AndroidUtilities.dp(24.0f);
                if (this.currentIcon == this.nextIcon) {
                    i24 = 255;
                } else {
                    i24 = (int) (this.transitionProgress * 255.0f);
                }
                this.paint2.setStyle(Paint.Style.FILL_AND_STROKE);
                this.paint2.setAlpha(i24);
                applyShaderMatrix(true);
                canvas2.save();
                canvas2.translate(iCenterX, iCenterY3);
                float f10116 = f20;
                canvas2.scale(f10116, f10116);
                float f10117 = (-iDp111110) / 2;
                canvas2.translate(f10117, f10117);
                path = pathArr3[0];
                if (path != null) {
                    canvas2.drawPath(path, this.paint2);
                }
                if (pathArr3.length >= 3) {
                    canvas2.drawPath(path3, this.paint);
                }
                path2 = pathArr3[1];
                if (path2 != null) {
                    if (i24 != 255) {
                        int alpha6 = this.backPaint.getAlpha();
                        this.backPaint.setAlpha((int) (alpha6 * (i24 / 255.0f)));
                        canvas2.drawPath(pathArr3[1], this.backPaint);
                        this.backPaint.setAlpha(alpha6);
                    } else {
                        canvas2.drawPath(path2, this.backPaint);
                    }
                }
                canvas2.restore();
            }
            long jCurrentTimeMillis6 = System.currentTimeMillis();
            j = jCurrentTimeMillis6 - this.lastAnimationTime;
            if (j > 17) {
                j = 17;
            }
            this.lastAnimationTime = jCurrentTimeMillis6;
            i22 = this.currentIcon;
            if (i22 != 3) {
                float f10118 = this.downloadRadOffset + ((360 * j) / 2500.0f);
                this.downloadRadOffset = f10118;
                this.downloadRadOffset = getCircleValue(f10118);
                if (this.nextIcon != 2) {
                    f31 = this.downloadProgress;
                    f32 = this.downloadProgressAnimationStart;
                    f33 = f31 - f32;
                    if (f33 > 0.0f) {
                        f34 = this.downloadProgressTime + j;
                        this.downloadProgressTime = f34;
                        if (f34 >= 200.0f) {
                            this.animatedDownloadProgress = f31;
                            this.downloadProgressAnimationStart = f31;
                            this.downloadProgressTime = 0.0f;
                        } else {
                            this.animatedDownloadProgress = f32 + (f33 * this.interpolator.getInterpolation(f34 / 200.0f));
                        }
                    }
                }
                invalidateSelf();
            } else {
                float f10119 = this.downloadRadOffset + ((360 * j) / 2500.0f);
                this.downloadRadOffset = f10119;
                this.downloadRadOffset = getCircleValue(f10119);
                if (this.nextIcon != 2) {
                    f31 = this.downloadProgress;
                    f32 = this.downloadProgressAnimationStart;
                    f33 = f31 - f32;
                    if (f33 > 0.0f) {
                        f34 = this.downloadProgressTime + j;
                        this.downloadProgressTime = f34;
                        if (f34 >= 200.0f) {
                            this.animatedDownloadProgress = f31;
                            this.downloadProgressAnimationStart = f31;
                            this.downloadProgressTime = 0.0f;
                        } else {
                            this.animatedDownloadProgress = f32 + (f33 * this.interpolator.getInterpolation(f34 / 200.0f));
                        }
                    }
                }
                invalidateSelf();
            }
            if (this.animatingTransition) {
                f35 = this.transitionProgress;
                if (f35 < 1.0f) {
                    f36 = f35 + (j / this.transitionAnimationTime);
                    this.transitionProgress = f36;
                    if (f36 >= 1.0f) {
                        this.currentIcon = this.nextIcon;
                        this.transitionProgress = 1.0f;
                        this.animatingTransition = false;
                    }
                    invalidateSelf();
                }
            }
            i23 = i11;
            if (i23 >= 1) {
                canvas2.restoreToCount(i23);
            }
        }
        f37 = 1.0f;
        if (f38 != f37) {
            canvas2.save();
            canvas2.scale(f38, f38, fCenterX, f39);
        }
        if (f40 != f) {
            canvas2.save();
            canvas2.rotate(f40, iCenterX, iCenterY3);
        }
        if (iMin != 0) {
            f45 = iMin;
            this.paint.setAlpha((int) (this.overrideAlpha * f45));
            if (this.currentIcon != 14) {
                f43 = 4.0f;
                this.paint3.setAlpha((int) (f45 * this.overrideAlpha));
                this.rect.set(iCenterX - AndroidUtilities.dp(3.5f), iCenterY3 - AndroidUtilities.dp(3.5f), AndroidUtilities.dp(3.5f) + iCenterX, AndroidUtilities.dp(3.5f) + iCenterY3);
                canvas2.drawRoundRect(this.rect, AndroidUtilities.dp(2.0f), AndroidUtilities.dp(2.0f), this.paint3);
            } else {
                f43 = 4.0f;
                this.paint3.setAlpha((int) (f45 * this.overrideAlpha));
                this.rect.set(iCenterX - AndroidUtilities.dp(3.5f), iCenterY3 - AndroidUtilities.dp(3.5f), AndroidUtilities.dp(3.5f) + iCenterX, AndroidUtilities.dp(3.5f) + iCenterY3);
                canvas2.drawRoundRect(this.rect, AndroidUtilities.dp(2.0f), AndroidUtilities.dp(2.0f), this.paint3);
            }
        } else {
            f43 = 4.0f;
        }
        if (f40 != f) {
            canvas2.restore();
        }
        if (f38 != f37) {
            canvas2.restore();
        }
        i30 = this.currentIcon;
        if (i30 != 3) {
            float fMax6 = Math.max(f43, this.animatedDownloadProgress * f88);
            if (this.isMini) {
                f44 = 2.0f;
            } else {
                f44 = f43;
            }
            int iDp122 = AndroidUtilities.dp(f44);
            this.rect.set(bounds.left + iDp122, bounds.top + iDp122, bounds.right - iDp122, bounds.bottom - iDp122);
            i31 = this.currentIcon;
            if (i31 != 14) {
                this.paint.setAlpha((int) (iMin * 0.15f * this.overrideAlpha));
                canvas2.drawArc(this.rect, 0.0f, 360.0f, false, this.paint);
                this.paint.setAlpha(iMin);
            } else {
                this.paint.setAlpha((int) (iMin * 0.15f * this.overrideAlpha));
                canvas2.drawArc(this.rect, 0.0f, 360.0f, false, this.paint);
                this.paint.setAlpha(iMin);
            }
            canvas2 = canvas;
            canvas2.drawArc(this.rect, this.downloadRadOffset, fMax6, false, this.paint);
        } else {
            float fMax7 = Math.max(f43, this.animatedDownloadProgress * f88);
            if (this.isMini) {
                f44 = 2.0f;
            } else {
                f44 = f43;
            }
            int iDp123 = AndroidUtilities.dp(f44);
            this.rect.set(bounds.left + iDp123, bounds.top + iDp123, bounds.right - iDp123, bounds.bottom - iDp123);
            i31 = this.currentIcon;
            if (i31 != 14) {
                this.paint.setAlpha((int) (iMin * 0.15f * this.overrideAlpha));
                canvas2.drawArc(this.rect, 0.0f, 360.0f, false, this.paint);
                this.paint.setAlpha(iMin);
            } else {
                this.paint.setAlpha((int) (iMin * 0.15f * this.overrideAlpha));
                canvas2.drawArc(this.rect, 0.0f, 360.0f, false, this.paint);
                this.paint.setAlpha(iMin);
            }
            canvas2 = canvas;
            canvas2.drawArc(this.rect, this.downloadRadOffset, fMax7, false, this.paint);
        }
        i3 = this.currentIcon;
        if (i3 == this.nextIcon) {
            f20 = 1.0f;
            f19 = 1.0f;
        } else {
            if (i3 != 4) {
                fMin = this.transitionProgress;
                fMax = 1.0f - fMin;
            } else {
                fMin = this.transitionProgress;
                fMax = 1.0f - fMin;
            }
            f19 = fMax;
            f20 = fMin;
        }
        i4 = this.nextIcon;
        if (i4 == i2) {
            pathArr = Theme.chat_updatePath;
        } else {
            if (this.currentIcon == i2) {
                pathArr2 = Theme.chat_updatePath;
                pathArr = null;
            } else {
                pathArr = null;
            }
            if (i4 == 5) {
                pathArr = Theme.chat_filePath;
            } else if (this.currentIcon == 5) {
                pathArr2 = Theme.chat_filePath;
            }
            pathArr3 = pathArr;
            pathArr4 = pathArr2;
            if (i4 == 7) {
                drawable2 = Theme.chat_flameIcon;
                drawable = null;
                i5 = 8;
            } else {
                if (this.currentIcon == 7) {
                    drawable = Theme.chat_flameIcon;
                } else {
                    drawable = null;
                }
                i5 = 8;
                drawable2 = null;
            }
            if (i4 == i5) {
                drawable2 = Theme.chat_gifIcon;
            } else if (this.currentIcon == i5) {
                drawable = Theme.chat_gifIcon;
            }
            drawable3 = drawable;
            if (this.currentIcon != 9) {
                applyShaderMatrix(false);
                Paint paint119 = this.paint;
                if (this.currentIcon == this.nextIcon) {
                    i6 = 255;
                } else {
                    i6 = (int) (this.transitionProgress * 255.0f);
                }
                paint119.setAlpha(i6);
                int iDp111111 = AndroidUtilities.dp(7.0f) + iCenterY3;
                int iDp111112 = iCenterX - AndroidUtilities.dp(3.0f);
                if (this.currentIcon != this.nextIcon) {
                    canvas2.save();
                    float f8110 = this.transitionProgress;
                    canvas2.scale(f8110, f8110, iCenterX, iCenterY3);
                }
                float f911212 = iDp111112;
                float f911213 = iDp111111;
                rect = bounds;
                drawable4 = drawable2;
                canvas2.drawLine(iDp111112 - AndroidUtilities.dp(6.0f), iDp111111 - AndroidUtilities.dp(6.0f), f911212, f911213, this.paint);
                canvas2 = canvas;
                canvas2.drawLine(f911212, f911213, iDp111112 + AndroidUtilities.dp(12.0f), iDp111111 - AndroidUtilities.dp(12.0f), this.paint);
                if (this.currentIcon != this.nextIcon) {
                    canvas2.restore();
                }
            } else {
                applyShaderMatrix(false);
                Paint paint1110 = this.paint;
                if (this.currentIcon == this.nextIcon) {
                    i6 = 255;
                } else {
                    i6 = (int) (this.transitionProgress * 255.0f);
                }
                paint1110.setAlpha(i6);
                int iDp111113 = AndroidUtilities.dp(7.0f) + iCenterY3;
                int iDp111114 = iCenterX - AndroidUtilities.dp(3.0f);
                if (this.currentIcon != this.nextIcon) {
                    canvas2.save();
                    float f8111 = this.transitionProgress;
                    canvas2.scale(f8111, f8111, iCenterX, iCenterY3);
                }
                float f911214 = iDp111114;
                float f911215 = iDp111113;
                rect = bounds;
                drawable4 = drawable2;
                canvas2.drawLine(iDp111114 - AndroidUtilities.dp(6.0f), iDp111113 - AndroidUtilities.dp(6.0f), f911214, f911215, this.paint);
                canvas2 = canvas;
                canvas2.drawLine(f911214, f911215, iDp111114 + AndroidUtilities.dp(12.0f), iDp111113 - AndroidUtilities.dp(12.0f), this.paint);
                if (this.currentIcon != this.nextIcon) {
                    canvas2.restore();
                }
            }
            if (this.currentIcon != 12) {
                applyShaderMatrix(false);
                i7 = this.currentIcon;
                i8 = this.nextIcon;
                if (i7 == i8) {
                    i9 = 13;
                    f21 = 1.0f;
                } else {
                    i9 = 13;
                    if (i8 == 13) {
                        f21 = this.transitionProgress;
                    } else {
                        f21 = 1.0f - this.transitionProgress;
                    }
                }
                Paint paint1111 = this.paint;
                if (i7 == i8) {
                    i10 = 255;
                } else {
                    i10 = (int) (f21 * 255.0f);
                }
                paint1111.setAlpha(i10);
                AndroidUtilities.dp(7.0f);
                AndroidUtilities.dp(3.0f);
                if (this.currentIcon != this.nextIcon) {
                    canvas2.save();
                    canvas2.scale(f21, f21, iCenterX, iCenterY3);
                }
                float fDp119 = AndroidUtilities.dp(7.0f) * this.scale;
                float f911111118 = iCenterX;
                float f911111119 = f911111118 - fDp119;
                float f9111111110 = iCenterY3;
                float f9111111111 = f9111111110 - fDp119;
                float f9111111112 = f911111118 + fDp119;
                float f9111111113 = fDp119 + f9111111110;
                i11 = i;
                i12 = i9;
                canvas2.drawLine(f911111119, f9111111111, f9111111112, f9111111113, this.paint);
                canvas2 = canvas;
                canvas2.drawLine(f9111111112, f9111111111, f911111119, f9111111113, this.paint);
                if (this.currentIcon != this.nextIcon) {
                    canvas2.restore();
                }
            } else {
                applyShaderMatrix(false);
                i7 = this.currentIcon;
                i8 = this.nextIcon;
                if (i7 == i8) {
                    i9 = 13;
                    f21 = 1.0f;
                } else {
                    i9 = 13;
                    if (i8 == 13) {
                        f21 = this.transitionProgress;
                    } else {
                        f21 = 1.0f - this.transitionProgress;
                    }
                }
                Paint paint1112 = this.paint;
                if (i7 == i8) {
                    i10 = 255;
                } else {
                    i10 = (int) (f21 * 255.0f);
                }
                paint1112.setAlpha(i10);
                AndroidUtilities.dp(7.0f);
                AndroidUtilities.dp(3.0f);
                if (this.currentIcon != this.nextIcon) {
                    canvas2.save();
                    canvas2.scale(f21, f21, iCenterX, iCenterY3);
                }
                float fDp1110 = AndroidUtilities.dp(7.0f) * this.scale;
                float f9111111114 = iCenterX;
                float f9111111115 = f9111111114 - fDp1110;
                float f9111111116 = iCenterY3;
                float f9111111117 = f9111111116 - fDp1110;
                float f9111111118 = f9111111114 + fDp1110;
                float f9111111119 = fDp1110 + f9111111116;
                i11 = i;
                i12 = i9;
                canvas2.drawLine(f9111111115, f9111111117, f9111111118, f9111111119, this.paint);
                canvas2 = canvas;
                canvas2.drawLine(f9111111118, f9111111117, f9111111115, f9111111119, this.paint);
                if (this.currentIcon != this.nextIcon) {
                    canvas2.restore();
                }
            }
            if (this.currentIcon != i12) {
                applyShaderMatrix(false);
                i13 = this.currentIcon;
                i14 = this.nextIcon;
                if (i13 == i14) {
                    f22 = 1.0f;
                } else if (i14 == i12) {
                    f22 = this.transitionProgress;
                } else {
                    f22 = 1.0f - this.transitionProgress;
                }
                this.textPaint.setAlpha((int) (f22 * 255.0f));
                int iDp111115 = AndroidUtilities.dp(5.0f) + iCenterY3;
                int i41110 = iCenterX - (this.percentStringWidth / 2);
                f23 = 5.0f;
                if (this.currentIcon != this.nextIcon) {
                    canvas2.save();
                    canvas2.scale(f22, f22, iCenterX, iCenterY3);
                }
                i15 = (int) (this.animatedDownloadProgress * 100.0f);
                if (this.percentString != null) {
                    this.lastPercent = i15;
                    String str112 = String.format("%d%%", Integer.valueOf(i15));
                    this.percentString = str112;
                    this.percentStringWidth = (int) Math.ceil(this.textPaint.measureText(str112));
                } else {
                    this.lastPercent = i15;
                    String str113 = String.format("%d%%", Integer.valueOf(i15));
                    this.percentString = str113;
                    this.percentStringWidth = (int) Math.ceil(this.textPaint.measureText(str113));
                }
                canvas2.drawText(this.percentString, i41110, iDp111115, this.textPaint);
                if (this.currentIcon != this.nextIcon) {
                    canvas2.restore();
                }
            } else {
                applyShaderMatrix(false);
                i13 = this.currentIcon;
                i14 = this.nextIcon;
                if (i13 == i14) {
                    f22 = 1.0f;
                } else if (i14 == i12) {
                    f22 = this.transitionProgress;
                } else {
                    f22 = 1.0f - this.transitionProgress;
                }
                this.textPaint.setAlpha((int) (f22 * 255.0f));
                int iDp111116 = AndroidUtilities.dp(5.0f) + iCenterY3;
                int i41111 = iCenterX - (this.percentStringWidth / 2);
                f23 = 5.0f;
                if (this.currentIcon != this.nextIcon) {
                    canvas2.save();
                    canvas2.scale(f22, f22, iCenterX, iCenterY3);
                }
                i15 = (int) (this.animatedDownloadProgress * 100.0f);
                if (this.percentString != null) {
                    this.lastPercent = i15;
                    String str114 = String.format("%d%%", Integer.valueOf(i15));
                    this.percentString = str114;
                    this.percentStringWidth = (int) Math.ceil(this.textPaint.measureText(str114));
                } else {
                    this.lastPercent = i15;
                    String str115 = String.format("%d%%", Integer.valueOf(i15));
                    this.percentString = str115;
                    this.percentStringWidth = (int) Math.ceil(this.textPaint.measureText(str115));
                }
                canvas2.drawText(this.percentString, i41111, iDp111116, this.textPaint);
                if (this.currentIcon != this.nextIcon) {
                    canvas2.restore();
                }
            }
            i16 = this.currentIcon;
            i17 = 1;
            if (i16 != 0) {
                if (i16 == 0) {
                    if (i16 == 1) {
                        if (this.nextIcon != 0) {
                            i17 = 1;
                        } else if (this.animatingTransition) {
                            if (this.nextIcon == 0) {
                                f24 = 1.0f - this.transitionProgress;
                            } else {
                                f24 = this.transitionProgress;
                            }
                            i17 = 1;
                        } else {
                            i17 = 1;
                            if (this.nextIcon == 1) {
                                f24 = 1.0f;
                            } else {
                                f24 = 0.0f;
                            }
                        }
                        i18 = this.nextIcon;
                        if (i18 != 0) {
                            this.paint2.setAlpha(255);
                        } else {
                            this.paint2.setAlpha(255);
                        }
                        applyShaderMatrix(true);
                        canvas2.save();
                        canvas2.translate(rect.centerX() + (AndroidUtilities.dp(1.0f) * (1.0f - f24)), rect.centerY());
                        f25 = f24 * 500.0f;
                        i19 = this.currentIcon;
                        if (i19 == 1) {
                            f26 = 90.0f;
                        } else {
                            f26 = 0.0f;
                        }
                        if (i19 == 0) {
                            if (i19 == 1) {
                                if (f25 < 100.0f) {
                                    interpolation = CubicBezierInterpolator.EASE_BOTH.getInterpolation(f25 / 100.0f) * (-5.0f);
                                } else if (f25 < 484.0f) {
                                    interpolation = (CubicBezierInterpolator.EASE_BOTH.getInterpolation((f25 - 100.0f) / 384.0f) * 95.0f) - 5.0f;
                                }
                                f26 = interpolation;
                            }
                        } else if (i19 == 1) {
                            if (f25 < 100.0f) {
                                interpolation = CubicBezierInterpolator.EASE_BOTH.getInterpolation(f25 / 100.0f) * (-5.0f);
                            } else if (f25 < 484.0f) {
                                interpolation = (CubicBezierInterpolator.EASE_BOTH.getInterpolation((f25 - 100.0f) / 384.0f) * 95.0f) - 5.0f;
                            }
                            f26 = interpolation;
                        }
                        canvas2.rotate(f26);
                        i20 = this.currentIcon;
                        if (i20 == 0) {
                            canvas2.scale(f20, f20);
                        } else {
                            canvas2.scale(f20, f20);
                        }
                        Theme.playPauseAnimator.draw(canvas2, this.paint2, f25);
                        canvas2.scale(1.0f, -1.0f);
                        Theme.playPauseAnimator.draw(canvas2, this.paint2, f25);
                        canvas2.restore();
                    }
                    if (i16 == i17) {
                        f24 = 1.0f;
                    } else {
                        f24 = 0.0f;
                    }
                    i18 = this.nextIcon;
                    if (i18 != 0) {
                        this.paint2.setAlpha(255);
                    } else {
                        this.paint2.setAlpha(255);
                    }
                    applyShaderMatrix(true);
                    canvas2.save();
                    canvas2.translate(rect.centerX() + (AndroidUtilities.dp(1.0f) * (1.0f - f24)), rect.centerY());
                    f25 = f24 * 500.0f;
                    i19 = this.currentIcon;
                    if (i19 == 1) {
                        f26 = 90.0f;
                    } else {
                        f26 = 0.0f;
                    }
                    if (i19 == 0) {
                        if (i19 == 1) {
                            if (f25 < 100.0f) {
                                interpolation = CubicBezierInterpolator.EASE_BOTH.getInterpolation(f25 / 100.0f) * (-5.0f);
                            } else if (f25 < 484.0f) {
                                interpolation = (CubicBezierInterpolator.EASE_BOTH.getInterpolation((f25 - 100.0f) / 384.0f) * 95.0f) - 5.0f;
                            }
                            f26 = interpolation;
                        }
                    } else if (i19 == 1) {
                        if (f25 < 100.0f) {
                            interpolation = CubicBezierInterpolator.EASE_BOTH.getInterpolation(f25 / 100.0f) * (-5.0f);
                        } else if (f25 < 484.0f) {
                            interpolation = (CubicBezierInterpolator.EASE_BOTH.getInterpolation((f25 - 100.0f) / 384.0f) * 95.0f) - 5.0f;
                        }
                        f26 = interpolation;
                    }
                    canvas2.rotate(f26);
                    i20 = this.currentIcon;
                    if (i20 == 0) {
                        canvas2.scale(f20, f20);
                    } else {
                        canvas2.scale(f20, f20);
                    }
                    Theme.playPauseAnimator.draw(canvas2, this.paint2, f25);
                    canvas2.scale(1.0f, -1.0f);
                    Theme.playPauseAnimator.draw(canvas2, this.paint2, f25);
                    canvas2.restore();
                } else {
                    if (i16 == 1) {
                        if (this.nextIcon != 0) {
                            i17 = 1;
                        } else if (this.animatingTransition) {
                            if (this.nextIcon == 0) {
                                f24 = 1.0f - this.transitionProgress;
                            } else {
                                f24 = this.transitionProgress;
                            }
                            i17 = 1;
                        } else {
                            i17 = 1;
                            if (this.nextIcon == 1) {
                                f24 = 1.0f;
                            } else {
                                f24 = 0.0f;
                            }
                        }
                        i18 = this.nextIcon;
                        if (i18 != 0) {
                            this.paint2.setAlpha(255);
                        } else {
                            this.paint2.setAlpha(255);
                        }
                        applyShaderMatrix(true);
                        canvas2.save();
                        canvas2.translate(rect.centerX() + (AndroidUtilities.dp(1.0f) * (1.0f - f24)), rect.centerY());
                        f25 = f24 * 500.0f;
                        i19 = this.currentIcon;
                        if (i19 == 1) {
                            f26 = 90.0f;
                        } else {
                            f26 = 0.0f;
                        }
                        if (i19 == 0) {
                            if (i19 == 1) {
                                if (f25 < 100.0f) {
                                    interpolation = CubicBezierInterpolator.EASE_BOTH.getInterpolation(f25 / 100.0f) * (-5.0f);
                                } else if (f25 < 484.0f) {
                                    interpolation = (CubicBezierInterpolator.EASE_BOTH.getInterpolation((f25 - 100.0f) / 384.0f) * 95.0f) - 5.0f;
                                }
                                f26 = interpolation;
                            }
                        } else if (i19 == 1) {
                            if (f25 < 100.0f) {
                                interpolation = CubicBezierInterpolator.EASE_BOTH.getInterpolation(f25 / 100.0f) * (-5.0f);
                            } else if (f25 < 484.0f) {
                                interpolation = (CubicBezierInterpolator.EASE_BOTH.getInterpolation((f25 - 100.0f) / 384.0f) * 95.0f) - 5.0f;
                            }
                            f26 = interpolation;
                        }
                        canvas2.rotate(f26);
                        i20 = this.currentIcon;
                        if (i20 == 0) {
                            canvas2.scale(f20, f20);
                        } else {
                            canvas2.scale(f20, f20);
                        }
                        Theme.playPauseAnimator.draw(canvas2, this.paint2, f25);
                        canvas2.scale(1.0f, -1.0f);
                        Theme.playPauseAnimator.draw(canvas2, this.paint2, f25);
                        canvas2.restore();
                    }
                    if (i16 == i17) {
                        f24 = 1.0f;
                    } else {
                        f24 = 0.0f;
                    }
                    i18 = this.nextIcon;
                    if (i18 != 0) {
                        this.paint2.setAlpha(255);
                    } else {
                        this.paint2.setAlpha(255);
                    }
                    applyShaderMatrix(true);
                    canvas2.save();
                    canvas2.translate(rect.centerX() + (AndroidUtilities.dp(1.0f) * (1.0f - f24)), rect.centerY());
                    f25 = f24 * 500.0f;
                    i19 = this.currentIcon;
                    if (i19 == 1) {
                        f26 = 90.0f;
                    } else {
                        f26 = 0.0f;
                    }
                    if (i19 == 0) {
                        if (i19 == 1) {
                            if (f25 < 100.0f) {
                                interpolation = CubicBezierInterpolator.EASE_BOTH.getInterpolation(f25 / 100.0f) * (-5.0f);
                            } else if (f25 < 484.0f) {
                                interpolation = (CubicBezierInterpolator.EASE_BOTH.getInterpolation((f25 - 100.0f) / 384.0f) * 95.0f) - 5.0f;
                            }
                            f26 = interpolation;
                        }
                    } else if (i19 == 1) {
                        if (f25 < 100.0f) {
                            interpolation = CubicBezierInterpolator.EASE_BOTH.getInterpolation(f25 / 100.0f) * (-5.0f);
                        } else if (f25 < 484.0f) {
                            interpolation = (CubicBezierInterpolator.EASE_BOTH.getInterpolation((f25 - 100.0f) / 384.0f) * 95.0f) - 5.0f;
                        }
                        f26 = interpolation;
                    }
                    canvas2.rotate(f26);
                    i20 = this.currentIcon;
                    if (i20 == 0) {
                        canvas2.scale(f20, f20);
                    } else {
                        canvas2.scale(f20, f20);
                    }
                    Theme.playPauseAnimator.draw(canvas2, this.paint2, f25);
                    canvas2.scale(1.0f, -1.0f);
                    Theme.playPauseAnimator.draw(canvas2, this.paint2, f25);
                    canvas2.restore();
                }
            } else if (i16 == 0) {
                if (i16 == 1) {
                    if (this.nextIcon != 0) {
                        i17 = 1;
                    } else if (this.animatingTransition) {
                        if (this.nextIcon == 0) {
                            f24 = 1.0f - this.transitionProgress;
                        } else {
                            f24 = this.transitionProgress;
                        }
                        i17 = 1;
                    } else {
                        i17 = 1;
                        if (this.nextIcon == 1) {
                            f24 = 1.0f;
                        } else {
                            f24 = 0.0f;
                        }
                    }
                    i18 = this.nextIcon;
                    if (i18 != 0) {
                        this.paint2.setAlpha(255);
                    } else {
                        this.paint2.setAlpha(255);
                    }
                    applyShaderMatrix(true);
                    canvas2.save();
                    canvas2.translate(rect.centerX() + (AndroidUtilities.dp(1.0f) * (1.0f - f24)), rect.centerY());
                    f25 = f24 * 500.0f;
                    i19 = this.currentIcon;
                    if (i19 == 1) {
                        f26 = 90.0f;
                    } else {
                        f26 = 0.0f;
                    }
                    if (i19 == 0) {
                        if (i19 == 1) {
                            if (f25 < 100.0f) {
                                interpolation = CubicBezierInterpolator.EASE_BOTH.getInterpolation(f25 / 100.0f) * (-5.0f);
                            } else if (f25 < 484.0f) {
                                interpolation = (CubicBezierInterpolator.EASE_BOTH.getInterpolation((f25 - 100.0f) / 384.0f) * 95.0f) - 5.0f;
                            }
                            f26 = interpolation;
                        }
                    } else if (i19 == 1) {
                        if (f25 < 100.0f) {
                            interpolation = CubicBezierInterpolator.EASE_BOTH.getInterpolation(f25 / 100.0f) * (-5.0f);
                        } else if (f25 < 484.0f) {
                            interpolation = (CubicBezierInterpolator.EASE_BOTH.getInterpolation((f25 - 100.0f) / 384.0f) * 95.0f) - 5.0f;
                        }
                        f26 = interpolation;
                    }
                    canvas2.rotate(f26);
                    i20 = this.currentIcon;
                    if (i20 == 0) {
                        canvas2.scale(f20, f20);
                    } else {
                        canvas2.scale(f20, f20);
                    }
                    Theme.playPauseAnimator.draw(canvas2, this.paint2, f25);
                    canvas2.scale(1.0f, -1.0f);
                    Theme.playPauseAnimator.draw(canvas2, this.paint2, f25);
                    canvas2.restore();
                }
                if (i16 == i17) {
                    f24 = 1.0f;
                } else {
                    f24 = 0.0f;
                }
                i18 = this.nextIcon;
                if (i18 != 0) {
                    this.paint2.setAlpha(255);
                } else {
                    this.paint2.setAlpha(255);
                }
                applyShaderMatrix(true);
                canvas2.save();
                canvas2.translate(rect.centerX() + (AndroidUtilities.dp(1.0f) * (1.0f - f24)), rect.centerY());
                f25 = f24 * 500.0f;
                i19 = this.currentIcon;
                if (i19 == 1) {
                    f26 = 90.0f;
                } else {
                    f26 = 0.0f;
                }
                if (i19 == 0) {
                    if (i19 == 1) {
                        if (f25 < 100.0f) {
                            interpolation = CubicBezierInterpolator.EASE_BOTH.getInterpolation(f25 / 100.0f) * (-5.0f);
                        } else if (f25 < 484.0f) {
                            interpolation = (CubicBezierInterpolator.EASE_BOTH.getInterpolation((f25 - 100.0f) / 384.0f) * 95.0f) - 5.0f;
                        }
                        f26 = interpolation;
                    }
                } else if (i19 == 1) {
                    if (f25 < 100.0f) {
                        interpolation = CubicBezierInterpolator.EASE_BOTH.getInterpolation(f25 / 100.0f) * (-5.0f);
                    } else if (f25 < 484.0f) {
                        interpolation = (CubicBezierInterpolator.EASE_BOTH.getInterpolation((f25 - 100.0f) / 384.0f) * 95.0f) - 5.0f;
                    }
                    f26 = interpolation;
                }
                canvas2.rotate(f26);
                i20 = this.currentIcon;
                if (i20 == 0) {
                    canvas2.scale(f20, f20);
                } else {
                    canvas2.scale(f20, f20);
                }
                Theme.playPauseAnimator.draw(canvas2, this.paint2, f25);
                canvas2.scale(1.0f, -1.0f);
                Theme.playPauseAnimator.draw(canvas2, this.paint2, f25);
                canvas2.restore();
            } else {
                if (i16 == 1) {
                    if (this.nextIcon != 0) {
                        i17 = 1;
                    } else if (this.animatingTransition) {
                        if (this.nextIcon == 0) {
                            f24 = 1.0f - this.transitionProgress;
                        } else {
                            f24 = this.transitionProgress;
                        }
                        i17 = 1;
                    } else {
                        i17 = 1;
                        if (this.nextIcon == 1) {
                            f24 = 1.0f;
                        } else {
                            f24 = 0.0f;
                        }
                    }
                    i18 = this.nextIcon;
                    if (i18 != 0) {
                        this.paint2.setAlpha(255);
                    } else {
                        this.paint2.setAlpha(255);
                    }
                    applyShaderMatrix(true);
                    canvas2.save();
                    canvas2.translate(rect.centerX() + (AndroidUtilities.dp(1.0f) * (1.0f - f24)), rect.centerY());
                    f25 = f24 * 500.0f;
                    i19 = this.currentIcon;
                    if (i19 == 1) {
                        f26 = 90.0f;
                    } else {
                        f26 = 0.0f;
                    }
                    if (i19 == 0) {
                        if (i19 == 1) {
                            if (f25 < 100.0f) {
                                interpolation = CubicBezierInterpolator.EASE_BOTH.getInterpolation(f25 / 100.0f) * (-5.0f);
                            } else if (f25 < 484.0f) {
                                interpolation = (CubicBezierInterpolator.EASE_BOTH.getInterpolation((f25 - 100.0f) / 384.0f) * 95.0f) - 5.0f;
                            }
                            f26 = interpolation;
                        }
                    } else if (i19 == 1) {
                        if (f25 < 100.0f) {
                            interpolation = CubicBezierInterpolator.EASE_BOTH.getInterpolation(f25 / 100.0f) * (-5.0f);
                        } else if (f25 < 484.0f) {
                            interpolation = (CubicBezierInterpolator.EASE_BOTH.getInterpolation((f25 - 100.0f) / 384.0f) * 95.0f) - 5.0f;
                        }
                        f26 = interpolation;
                    }
                    canvas2.rotate(f26);
                    i20 = this.currentIcon;
                    if (i20 == 0) {
                        canvas2.scale(f20, f20);
                    } else {
                        canvas2.scale(f20, f20);
                    }
                    Theme.playPauseAnimator.draw(canvas2, this.paint2, f25);
                    canvas2.scale(1.0f, -1.0f);
                    Theme.playPauseAnimator.draw(canvas2, this.paint2, f25);
                    canvas2.restore();
                }
                if (i16 == i17) {
                    f24 = 1.0f;
                } else {
                    f24 = 0.0f;
                }
                i18 = this.nextIcon;
                if (i18 != 0) {
                    this.paint2.setAlpha(255);
                } else {
                    this.paint2.setAlpha(255);
                }
                applyShaderMatrix(true);
                canvas2.save();
                canvas2.translate(rect.centerX() + (AndroidUtilities.dp(1.0f) * (1.0f - f24)), rect.centerY());
                f25 = f24 * 500.0f;
                i19 = this.currentIcon;
                if (i19 == 1) {
                    f26 = 90.0f;
                } else {
                    f26 = 0.0f;
                }
                if (i19 == 0) {
                    if (i19 == 1) {
                        if (f25 < 100.0f) {
                            interpolation = CubicBezierInterpolator.EASE_BOTH.getInterpolation(f25 / 100.0f) * (-5.0f);
                        } else if (f25 < 484.0f) {
                            interpolation = (CubicBezierInterpolator.EASE_BOTH.getInterpolation((f25 - 100.0f) / 384.0f) * 95.0f) - 5.0f;
                        }
                        f26 = interpolation;
                    }
                } else if (i19 == 1) {
                    if (f25 < 100.0f) {
                        interpolation = CubicBezierInterpolator.EASE_BOTH.getInterpolation(f25 / 100.0f) * (-5.0f);
                    } else if (f25 < 484.0f) {
                        interpolation = (CubicBezierInterpolator.EASE_BOTH.getInterpolation((f25 - 100.0f) / 384.0f) * 95.0f) - 5.0f;
                    }
                    f26 = interpolation;
                }
                canvas2.rotate(f26);
                i20 = this.currentIcon;
                if (i20 == 0) {
                    canvas2.scale(f20, f20);
                } else {
                    canvas2.scale(f20, f20);
                }
                Theme.playPauseAnimator.draw(canvas2, this.paint2, f25);
                canvas2.scale(1.0f, -1.0f);
                Theme.playPauseAnimator.draw(canvas2, this.paint2, f25);
                canvas2.restore();
            }
            if (this.currentIcon != 6) {
                applyShaderMatrix(false);
                if (this.currentIcon != 6) {
                    f28 = this.transitionProgress;
                    if (f28 > 0.5f) {
                        f30 = (f28 - 0.5f) / 0.5f;
                        fMin2 = 1.0f - Math.min(1.0f, f30 / 0.5f);
                        if (f30 > 0.5f) {
                            f29 = (f30 - 0.5f) / 0.5f;
                        } else {
                            f29 = 0.0f;
                        }
                    } else {
                        f29 = 0.0f;
                        fMin2 = 1.0f;
                    }
                    this.paint.setAlpha(255);
                    f27 = f29;
                } else {
                    if (this.nextIcon != 6) {
                        this.paint.setAlpha((int) ((1.0f - this.transitionProgress) * 255.0f));
                    } else {
                        this.paint.setAlpha(255);
                    }
                    fMin2 = 0.0f;
                    f27 = 1.0f;
                }
                iDp = AndroidUtilities.dp(7.0f) + iCenterY3;
                iDp2 = iCenterX - AndroidUtilities.dp(3.0f);
                if (fMin2 < 1.0f) {
                    i21 = iDp2;
                    canvas.drawLine(iDp2 - AndroidUtilities.dp(6.0f), iDp - AndroidUtilities.dp(6.0f), iDp2 - (AndroidUtilities.dp(6.0f) * fMin2), iDp - (AndroidUtilities.dp(6.0f) * fMin2), this.paint);
                } else {
                    i21 = iDp2;
                }
                if (f27 > 0.0f) {
                    float f92110 = i21;
                    float f92111 = iDp;
                    canvas2 = canvas;
                    canvas2.drawLine(f92110, f92111, f92110 + (AndroidUtilities.dp(12.0f) * f27), f92111 - (AndroidUtilities.dp(12.0f) * f27), this.paint);
                } else {
                    canvas2 = canvas;
                }
            } else {
                applyShaderMatrix(false);
                if (this.currentIcon != 6) {
                    f28 = this.transitionProgress;
                    if (f28 > 0.5f) {
                        f30 = (f28 - 0.5f) / 0.5f;
                        fMin2 = 1.0f - Math.min(1.0f, f30 / 0.5f);
                        if (f30 > 0.5f) {
                            f29 = (f30 - 0.5f) / 0.5f;
                        } else {
                            f29 = 0.0f;
                        }
                    } else {
                        f29 = 0.0f;
                        fMin2 = 1.0f;
                    }
                    this.paint.setAlpha(255);
                    f27 = f29;
                } else {
                    if (this.nextIcon != 6) {
                        this.paint.setAlpha((int) ((1.0f - this.transitionProgress) * 255.0f));
                    } else {
                        this.paint.setAlpha(255);
                    }
                    fMin2 = 0.0f;
                    f27 = 1.0f;
                }
                iDp = AndroidUtilities.dp(7.0f) + iCenterY3;
                iDp2 = iCenterX - AndroidUtilities.dp(3.0f);
                if (fMin2 < 1.0f) {
                    i21 = iDp2;
                    canvas.drawLine(iDp2 - AndroidUtilities.dp(6.0f), iDp - AndroidUtilities.dp(6.0f), iDp2 - (AndroidUtilities.dp(6.0f) * fMin2), iDp - (AndroidUtilities.dp(6.0f) * fMin2), this.paint);
                } else {
                    i21 = iDp2;
                }
                if (f27 > 0.0f) {
                    float f92112 = i21;
                    float f92113 = iDp;
                    canvas2 = canvas;
                    canvas2.drawLine(f92112, f92113, f92112 + (AndroidUtilities.dp(12.0f) * f27), f92113 - (AndroidUtilities.dp(12.0f) * f27), this.paint);
                } else {
                    canvas2 = canvas;
                }
            }
            if (drawable3 != null) {
                int intrinsicWidth13 = (int) (drawable3.getIntrinsicWidth() * f19);
                int intrinsicHeight13 = (int) (drawable3.getIntrinsicHeight() * f19);
                drawable3.setColorFilter(this.colorFilter);
                if (this.currentIcon == this.nextIcon) {
                    i27 = 255;
                } else {
                    i27 = (int) ((1.0f - this.transitionProgress) * 255.0f);
                }
                drawable3.setAlpha(i27);
                int i41112 = intrinsicWidth13 / 2;
                int i41113 = intrinsicHeight13 / 2;
                drawable3.setBounds(iCenterX - i41112, iCenterY3 - i41113, i41112 + iCenterX, i41113 + iCenterY3);
                drawable3.draw(canvas2);
            }
            if (drawable4 != null) {
                int intrinsicWidth14 = (int) (drawable4.getIntrinsicWidth() * f20);
                int intrinsicHeight14 = (int) (drawable4.getIntrinsicHeight() * f20);
                drawable4.setColorFilter(this.colorFilter);
                if (this.currentIcon == this.nextIcon) {
                    i26 = 255;
                } else {
                    i26 = (int) (this.transitionProgress * 255.0f);
                }
                drawable4.setAlpha(i26);
                int i515 = intrinsicWidth14 / 2;
                int i516 = intrinsicHeight14 / 2;
                drawable4.setBounds(iCenterX - i515, iCenterY3 - i516, i515 + iCenterX, i516 + iCenterY3);
                drawable4.draw(canvas2);
            }
            if (pathArr4 != null) {
                int iDp111117 = AndroidUtilities.dp(24.0f);
                this.paint2.setStyle(Paint.Style.FILL_AND_STROKE);
                Paint paint1113 = this.paint2;
                if (this.currentIcon == this.nextIcon) {
                    i25 = 255;
                } else {
                    i25 = (int) ((1.0f - this.transitionProgress) * 255.0f);
                }
                paint1113.setAlpha(i25);
                applyShaderMatrix(true);
                canvas2.save();
                canvas2.translate(iCenterX, iCenterY3);
                canvas2.scale(f19, f19);
                float f101110 = (-iDp111117) / 2;
                canvas2.translate(f101110, f101110);
                path4 = pathArr4[0];
                if (path4 != null) {
                    canvas2.drawPath(path4, this.paint2);
                }
                path5 = pathArr4[1];
                if (path5 != null) {
                    canvas2.drawPath(path5, this.backPaint);
                }
                canvas2.restore();
            }
            if (pathArr3 != null) {
                int iDp111118 = AndroidUtilities.dp(24.0f);
                if (this.currentIcon == this.nextIcon) {
                    i24 = 255;
                } else {
                    i24 = (int) (this.transitionProgress * 255.0f);
                }
                this.paint2.setStyle(Paint.Style.FILL_AND_STROKE);
                this.paint2.setAlpha(i24);
                applyShaderMatrix(true);
                canvas2.save();
                canvas2.translate(iCenterX, iCenterY3);
                float f101111 = f20;
                canvas2.scale(f101111, f101111);
                float f101112 = (-iDp111118) / 2;
                canvas2.translate(f101112, f101112);
                path = pathArr3[0];
                if (path != null) {
                    canvas2.drawPath(path, this.paint2);
                }
                if (pathArr3.length >= 3) {
                    canvas2.drawPath(path3, this.paint);
                }
                path2 = pathArr3[1];
                if (path2 != null) {
                    if (i24 != 255) {
                        int alpha7 = this.backPaint.getAlpha();
                        this.backPaint.setAlpha((int) (alpha7 * (i24 / 255.0f)));
                        canvas2.drawPath(pathArr3[1], this.backPaint);
                        this.backPaint.setAlpha(alpha7);
                    } else {
                        canvas2.drawPath(path2, this.backPaint);
                    }
                }
                canvas2.restore();
            }
            long jCurrentTimeMillis7 = System.currentTimeMillis();
            j = jCurrentTimeMillis7 - this.lastAnimationTime;
            if (j > 17) {
                j = 17;
            }
            this.lastAnimationTime = jCurrentTimeMillis7;
            i22 = this.currentIcon;
            if (i22 != 3) {
                float f101113 = this.downloadRadOffset + ((360 * j) / 2500.0f);
                this.downloadRadOffset = f101113;
                this.downloadRadOffset = getCircleValue(f101113);
                if (this.nextIcon != 2) {
                    f31 = this.downloadProgress;
                    f32 = this.downloadProgressAnimationStart;
                    f33 = f31 - f32;
                    if (f33 > 0.0f) {
                        f34 = this.downloadProgressTime + j;
                        this.downloadProgressTime = f34;
                        if (f34 >= 200.0f) {
                            this.animatedDownloadProgress = f31;
                            this.downloadProgressAnimationStart = f31;
                            this.downloadProgressTime = 0.0f;
                        } else {
                            this.animatedDownloadProgress = f32 + (f33 * this.interpolator.getInterpolation(f34 / 200.0f));
                        }
                    }
                }
                invalidateSelf();
            } else {
                float f101114 = this.downloadRadOffset + ((360 * j) / 2500.0f);
                this.downloadRadOffset = f101114;
                this.downloadRadOffset = getCircleValue(f101114);
                if (this.nextIcon != 2) {
                    f31 = this.downloadProgress;
                    f32 = this.downloadProgressAnimationStart;
                    f33 = f31 - f32;
                    if (f33 > 0.0f) {
                        f34 = this.downloadProgressTime + j;
                        this.downloadProgressTime = f34;
                        if (f34 >= 200.0f) {
                            this.animatedDownloadProgress = f31;
                            this.downloadProgressAnimationStart = f31;
                            this.downloadProgressTime = 0.0f;
                        } else {
                            this.animatedDownloadProgress = f32 + (f33 * this.interpolator.getInterpolation(f34 / 200.0f));
                        }
                    }
                }
                invalidateSelf();
            }
            if (this.animatingTransition) {
                f35 = this.transitionProgress;
                if (f35 < 1.0f) {
                    f36 = f35 + (j / this.transitionAnimationTime);
                    this.transitionProgress = f36;
                    if (f36 >= 1.0f) {
                        this.currentIcon = this.nextIcon;
                        this.transitionProgress = 1.0f;
                        this.animatingTransition = false;
                    }
                    invalidateSelf();
                }
            }
            i23 = i11;
            if (i23 >= 1) {
                canvas2.restoreToCount(i23);
            }
        }
        pathArr2 = null;
        if (i4 == 5) {
            pathArr = Theme.chat_filePath;
        } else if (this.currentIcon == 5) {
            pathArr2 = Theme.chat_filePath;
        }
        pathArr3 = pathArr;
        pathArr4 = pathArr2;
        if (i4 == 7) {
            drawable2 = Theme.chat_flameIcon;
            drawable = null;
            i5 = 8;
        } else {
            if (this.currentIcon == 7) {
                drawable = Theme.chat_flameIcon;
            } else {
                drawable = null;
            }
            i5 = 8;
            drawable2 = null;
        }
        if (i4 == i5) {
            drawable2 = Theme.chat_gifIcon;
        } else if (this.currentIcon == i5) {
            drawable = Theme.chat_gifIcon;
        }
        drawable3 = drawable;
        if (this.currentIcon != 9) {
            applyShaderMatrix(false);
            Paint paint1114 = this.paint;
            if (this.currentIcon == this.nextIcon) {
                i6 = 255;
            } else {
                i6 = (int) (this.transitionProgress * 255.0f);
            }
            paint1114.setAlpha(i6);
            int iDp111119 = AndroidUtilities.dp(7.0f) + iCenterY3;
            int iDp1111110 = iCenterX - AndroidUtilities.dp(3.0f);
            if (this.currentIcon != this.nextIcon) {
                canvas2.save();
                float f8112 = this.transitionProgress;
                canvas2.scale(f8112, f8112, iCenterX, iCenterY3);
            }
            float f911216 = iDp1111110;
            float f911217 = iDp111119;
            rect = bounds;
            drawable4 = drawable2;
            canvas2.drawLine(iDp1111110 - AndroidUtilities.dp(6.0f), iDp111119 - AndroidUtilities.dp(6.0f), f911216, f911217, this.paint);
            canvas2 = canvas;
            canvas2.drawLine(f911216, f911217, iDp1111110 + AndroidUtilities.dp(12.0f), iDp111119 - AndroidUtilities.dp(12.0f), this.paint);
            if (this.currentIcon != this.nextIcon) {
                canvas2.restore();
            }
        } else {
            applyShaderMatrix(false);
            Paint paint1115 = this.paint;
            if (this.currentIcon == this.nextIcon) {
                i6 = 255;
            } else {
                i6 = (int) (this.transitionProgress * 255.0f);
            }
            paint1115.setAlpha(i6);
            int iDp1111111 = AndroidUtilities.dp(7.0f) + iCenterY3;
            int iDp1111112 = iCenterX - AndroidUtilities.dp(3.0f);
            if (this.currentIcon != this.nextIcon) {
                canvas2.save();
                float f8113 = this.transitionProgress;
                canvas2.scale(f8113, f8113, iCenterX, iCenterY3);
            }
            float f911218 = iDp1111112;
            float f911219 = iDp1111111;
            rect = bounds;
            drawable4 = drawable2;
            canvas2.drawLine(iDp1111112 - AndroidUtilities.dp(6.0f), iDp1111111 - AndroidUtilities.dp(6.0f), f911218, f911219, this.paint);
            canvas2 = canvas;
            canvas2.drawLine(f911218, f911219, iDp1111112 + AndroidUtilities.dp(12.0f), iDp1111111 - AndroidUtilities.dp(12.0f), this.paint);
            if (this.currentIcon != this.nextIcon) {
                canvas2.restore();
            }
        }
        if (this.currentIcon != 12) {
            applyShaderMatrix(false);
            i7 = this.currentIcon;
            i8 = this.nextIcon;
            if (i7 == i8) {
                i9 = 13;
                f21 = 1.0f;
            } else {
                i9 = 13;
                if (i8 == 13) {
                    f21 = this.transitionProgress;
                } else {
                    f21 = 1.0f - this.transitionProgress;
                }
            }
            Paint paint1116 = this.paint;
            if (i7 == i8) {
                i10 = 255;
            } else {
                i10 = (int) (f21 * 255.0f);
            }
            paint1116.setAlpha(i10);
            AndroidUtilities.dp(7.0f);
            AndroidUtilities.dp(3.0f);
            if (this.currentIcon != this.nextIcon) {
                canvas2.save();
                canvas2.scale(f21, f21, iCenterX, iCenterY3);
            }
            float fDp1111 = AndroidUtilities.dp(7.0f) * this.scale;
            float f91111111110 = iCenterX;
            float f91111111111 = f91111111110 - fDp1111;
            float f91111111112 = iCenterY3;
            float f91111111113 = f91111111112 - fDp1111;
            float f91111111114 = f91111111110 + fDp1111;
            float f91111111115 = fDp1111 + f91111111112;
            i11 = i;
            i12 = i9;
            canvas2.drawLine(f91111111111, f91111111113, f91111111114, f91111111115, this.paint);
            canvas2 = canvas;
            canvas2.drawLine(f91111111114, f91111111113, f91111111111, f91111111115, this.paint);
            if (this.currentIcon != this.nextIcon) {
                canvas2.restore();
            }
        } else {
            applyShaderMatrix(false);
            i7 = this.currentIcon;
            i8 = this.nextIcon;
            if (i7 == i8) {
                i9 = 13;
                f21 = 1.0f;
            } else {
                i9 = 13;
                if (i8 == 13) {
                    f21 = this.transitionProgress;
                } else {
                    f21 = 1.0f - this.transitionProgress;
                }
            }
            Paint paint1117 = this.paint;
            if (i7 == i8) {
                i10 = 255;
            } else {
                i10 = (int) (f21 * 255.0f);
            }
            paint1117.setAlpha(i10);
            AndroidUtilities.dp(7.0f);
            AndroidUtilities.dp(3.0f);
            if (this.currentIcon != this.nextIcon) {
                canvas2.save();
                canvas2.scale(f21, f21, iCenterX, iCenterY3);
            }
            float fDp1112 = AndroidUtilities.dp(7.0f) * this.scale;
            float f91111111116 = iCenterX;
            float f91111111117 = f91111111116 - fDp1112;
            float f91111111118 = iCenterY3;
            float f91111111119 = f91111111118 - fDp1112;
            float f911111111110 = f91111111116 + fDp1112;
            float f911111111111 = fDp1112 + f91111111118;
            i11 = i;
            i12 = i9;
            canvas2.drawLine(f91111111117, f91111111119, f911111111110, f911111111111, this.paint);
            canvas2 = canvas;
            canvas2.drawLine(f911111111110, f91111111119, f91111111117, f911111111111, this.paint);
            if (this.currentIcon != this.nextIcon) {
                canvas2.restore();
            }
        }
        if (this.currentIcon != i12) {
            applyShaderMatrix(false);
            i13 = this.currentIcon;
            i14 = this.nextIcon;
            if (i13 == i14) {
                f22 = 1.0f;
            } else if (i14 == i12) {
                f22 = this.transitionProgress;
            } else {
                f22 = 1.0f - this.transitionProgress;
            }
            this.textPaint.setAlpha((int) (f22 * 255.0f));
            int iDp1111113 = AndroidUtilities.dp(5.0f) + iCenterY3;
            int i41114 = iCenterX - (this.percentStringWidth / 2);
            f23 = 5.0f;
            if (this.currentIcon != this.nextIcon) {
                canvas2.save();
                canvas2.scale(f22, f22, iCenterX, iCenterY3);
            }
            i15 = (int) (this.animatedDownloadProgress * 100.0f);
            if (this.percentString != null) {
                this.lastPercent = i15;
                String str116 = String.format("%d%%", Integer.valueOf(i15));
                this.percentString = str116;
                this.percentStringWidth = (int) Math.ceil(this.textPaint.measureText(str116));
            } else {
                this.lastPercent = i15;
                String str117 = String.format("%d%%", Integer.valueOf(i15));
                this.percentString = str117;
                this.percentStringWidth = (int) Math.ceil(this.textPaint.measureText(str117));
            }
            canvas2.drawText(this.percentString, i41114, iDp1111113, this.textPaint);
            if (this.currentIcon != this.nextIcon) {
                canvas2.restore();
            }
        } else {
            applyShaderMatrix(false);
            i13 = this.currentIcon;
            i14 = this.nextIcon;
            if (i13 == i14) {
                f22 = 1.0f;
            } else if (i14 == i12) {
                f22 = this.transitionProgress;
            } else {
                f22 = 1.0f - this.transitionProgress;
            }
            this.textPaint.setAlpha((int) (f22 * 255.0f));
            int iDp1111114 = AndroidUtilities.dp(5.0f) + iCenterY3;
            int i41115 = iCenterX - (this.percentStringWidth / 2);
            f23 = 5.0f;
            if (this.currentIcon != this.nextIcon) {
                canvas2.save();
                canvas2.scale(f22, f22, iCenterX, iCenterY3);
            }
            i15 = (int) (this.animatedDownloadProgress * 100.0f);
            if (this.percentString != null) {
                this.lastPercent = i15;
                String str118 = String.format("%d%%", Integer.valueOf(i15));
                this.percentString = str118;
                this.percentStringWidth = (int) Math.ceil(this.textPaint.measureText(str118));
            } else {
                this.lastPercent = i15;
                String str119 = String.format("%d%%", Integer.valueOf(i15));
                this.percentString = str119;
                this.percentStringWidth = (int) Math.ceil(this.textPaint.measureText(str119));
            }
            canvas2.drawText(this.percentString, i41115, iDp1111114, this.textPaint);
            if (this.currentIcon != this.nextIcon) {
                canvas2.restore();
            }
        }
        i16 = this.currentIcon;
        i17 = 1;
        if (i16 != 0) {
            if (i16 == 0) {
                if (i16 == 1) {
                    if (this.nextIcon != 0) {
                        i17 = 1;
                    } else if (this.animatingTransition) {
                        if (this.nextIcon == 0) {
                            f24 = 1.0f - this.transitionProgress;
                        } else {
                            f24 = this.transitionProgress;
                        }
                        i17 = 1;
                    } else {
                        i17 = 1;
                        if (this.nextIcon == 1) {
                            f24 = 1.0f;
                        } else {
                            f24 = 0.0f;
                        }
                    }
                    i18 = this.nextIcon;
                    if (i18 != 0) {
                        this.paint2.setAlpha(255);
                    } else {
                        this.paint2.setAlpha(255);
                    }
                    applyShaderMatrix(true);
                    canvas2.save();
                    canvas2.translate(rect.centerX() + (AndroidUtilities.dp(1.0f) * (1.0f - f24)), rect.centerY());
                    f25 = f24 * 500.0f;
                    i19 = this.currentIcon;
                    if (i19 == 1) {
                        f26 = 90.0f;
                    } else {
                        f26 = 0.0f;
                    }
                    if (i19 == 0) {
                        if (i19 == 1) {
                            if (f25 < 100.0f) {
                                interpolation = CubicBezierInterpolator.EASE_BOTH.getInterpolation(f25 / 100.0f) * (-5.0f);
                            } else if (f25 < 484.0f) {
                                interpolation = (CubicBezierInterpolator.EASE_BOTH.getInterpolation((f25 - 100.0f) / 384.0f) * 95.0f) - 5.0f;
                            }
                            f26 = interpolation;
                        }
                    } else if (i19 == 1) {
                        if (f25 < 100.0f) {
                            interpolation = CubicBezierInterpolator.EASE_BOTH.getInterpolation(f25 / 100.0f) * (-5.0f);
                        } else if (f25 < 484.0f) {
                            interpolation = (CubicBezierInterpolator.EASE_BOTH.getInterpolation((f25 - 100.0f) / 384.0f) * 95.0f) - 5.0f;
                        }
                        f26 = interpolation;
                    }
                    canvas2.rotate(f26);
                    i20 = this.currentIcon;
                    if (i20 == 0) {
                        canvas2.scale(f20, f20);
                    } else {
                        canvas2.scale(f20, f20);
                    }
                    Theme.playPauseAnimator.draw(canvas2, this.paint2, f25);
                    canvas2.scale(1.0f, -1.0f);
                    Theme.playPauseAnimator.draw(canvas2, this.paint2, f25);
                    canvas2.restore();
                }
                if (i16 == i17) {
                    f24 = 1.0f;
                } else {
                    f24 = 0.0f;
                }
                i18 = this.nextIcon;
                if (i18 != 0) {
                    this.paint2.setAlpha(255);
                } else {
                    this.paint2.setAlpha(255);
                }
                applyShaderMatrix(true);
                canvas2.save();
                canvas2.translate(rect.centerX() + (AndroidUtilities.dp(1.0f) * (1.0f - f24)), rect.centerY());
                f25 = f24 * 500.0f;
                i19 = this.currentIcon;
                if (i19 == 1) {
                    f26 = 90.0f;
                } else {
                    f26 = 0.0f;
                }
                if (i19 == 0) {
                    if (i19 == 1) {
                        if (f25 < 100.0f) {
                            interpolation = CubicBezierInterpolator.EASE_BOTH.getInterpolation(f25 / 100.0f) * (-5.0f);
                        } else if (f25 < 484.0f) {
                            interpolation = (CubicBezierInterpolator.EASE_BOTH.getInterpolation((f25 - 100.0f) / 384.0f) * 95.0f) - 5.0f;
                        }
                        f26 = interpolation;
                    }
                } else if (i19 == 1) {
                    if (f25 < 100.0f) {
                        interpolation = CubicBezierInterpolator.EASE_BOTH.getInterpolation(f25 / 100.0f) * (-5.0f);
                    } else if (f25 < 484.0f) {
                        interpolation = (CubicBezierInterpolator.EASE_BOTH.getInterpolation((f25 - 100.0f) / 384.0f) * 95.0f) - 5.0f;
                    }
                    f26 = interpolation;
                }
                canvas2.rotate(f26);
                i20 = this.currentIcon;
                if (i20 == 0) {
                    canvas2.scale(f20, f20);
                } else {
                    canvas2.scale(f20, f20);
                }
                Theme.playPauseAnimator.draw(canvas2, this.paint2, f25);
                canvas2.scale(1.0f, -1.0f);
                Theme.playPauseAnimator.draw(canvas2, this.paint2, f25);
                canvas2.restore();
            } else {
                if (i16 == 1) {
                    if (this.nextIcon != 0) {
                        i17 = 1;
                    } else if (this.animatingTransition) {
                        if (this.nextIcon == 0) {
                            f24 = 1.0f - this.transitionProgress;
                        } else {
                            f24 = this.transitionProgress;
                        }
                        i17 = 1;
                    } else {
                        i17 = 1;
                        if (this.nextIcon == 1) {
                            f24 = 1.0f;
                        } else {
                            f24 = 0.0f;
                        }
                    }
                    i18 = this.nextIcon;
                    if (i18 != 0) {
                        this.paint2.setAlpha(255);
                    } else {
                        this.paint2.setAlpha(255);
                    }
                    applyShaderMatrix(true);
                    canvas2.save();
                    canvas2.translate(rect.centerX() + (AndroidUtilities.dp(1.0f) * (1.0f - f24)), rect.centerY());
                    f25 = f24 * 500.0f;
                    i19 = this.currentIcon;
                    if (i19 == 1) {
                        f26 = 90.0f;
                    } else {
                        f26 = 0.0f;
                    }
                    if (i19 == 0) {
                        if (i19 == 1) {
                            if (f25 < 100.0f) {
                                interpolation = CubicBezierInterpolator.EASE_BOTH.getInterpolation(f25 / 100.0f) * (-5.0f);
                            } else if (f25 < 484.0f) {
                                interpolation = (CubicBezierInterpolator.EASE_BOTH.getInterpolation((f25 - 100.0f) / 384.0f) * 95.0f) - 5.0f;
                            }
                            f26 = interpolation;
                        }
                    } else if (i19 == 1) {
                        if (f25 < 100.0f) {
                            interpolation = CubicBezierInterpolator.EASE_BOTH.getInterpolation(f25 / 100.0f) * (-5.0f);
                        } else if (f25 < 484.0f) {
                            interpolation = (CubicBezierInterpolator.EASE_BOTH.getInterpolation((f25 - 100.0f) / 384.0f) * 95.0f) - 5.0f;
                        }
                        f26 = interpolation;
                    }
                    canvas2.rotate(f26);
                    i20 = this.currentIcon;
                    if (i20 == 0) {
                        canvas2.scale(f20, f20);
                    } else {
                        canvas2.scale(f20, f20);
                    }
                    Theme.playPauseAnimator.draw(canvas2, this.paint2, f25);
                    canvas2.scale(1.0f, -1.0f);
                    Theme.playPauseAnimator.draw(canvas2, this.paint2, f25);
                    canvas2.restore();
                }
                if (i16 == i17) {
                    f24 = 1.0f;
                } else {
                    f24 = 0.0f;
                }
                i18 = this.nextIcon;
                if (i18 != 0) {
                    this.paint2.setAlpha(255);
                } else {
                    this.paint2.setAlpha(255);
                }
                applyShaderMatrix(true);
                canvas2.save();
                canvas2.translate(rect.centerX() + (AndroidUtilities.dp(1.0f) * (1.0f - f24)), rect.centerY());
                f25 = f24 * 500.0f;
                i19 = this.currentIcon;
                if (i19 == 1) {
                    f26 = 90.0f;
                } else {
                    f26 = 0.0f;
                }
                if (i19 == 0) {
                    if (i19 == 1) {
                        if (f25 < 100.0f) {
                            interpolation = CubicBezierInterpolator.EASE_BOTH.getInterpolation(f25 / 100.0f) * (-5.0f);
                        } else if (f25 < 484.0f) {
                            interpolation = (CubicBezierInterpolator.EASE_BOTH.getInterpolation((f25 - 100.0f) / 384.0f) * 95.0f) - 5.0f;
                        }
                        f26 = interpolation;
                    }
                } else if (i19 == 1) {
                    if (f25 < 100.0f) {
                        interpolation = CubicBezierInterpolator.EASE_BOTH.getInterpolation(f25 / 100.0f) * (-5.0f);
                    } else if (f25 < 484.0f) {
                        interpolation = (CubicBezierInterpolator.EASE_BOTH.getInterpolation((f25 - 100.0f) / 384.0f) * 95.0f) - 5.0f;
                    }
                    f26 = interpolation;
                }
                canvas2.rotate(f26);
                i20 = this.currentIcon;
                if (i20 == 0) {
                    canvas2.scale(f20, f20);
                } else {
                    canvas2.scale(f20, f20);
                }
                Theme.playPauseAnimator.draw(canvas2, this.paint2, f25);
                canvas2.scale(1.0f, -1.0f);
                Theme.playPauseAnimator.draw(canvas2, this.paint2, f25);
                canvas2.restore();
            }
        } else if (i16 == 0) {
            if (i16 == 1) {
                if (this.nextIcon != 0) {
                    i17 = 1;
                } else if (this.animatingTransition) {
                    if (this.nextIcon == 0) {
                        f24 = 1.0f - this.transitionProgress;
                    } else {
                        f24 = this.transitionProgress;
                    }
                    i17 = 1;
                } else {
                    i17 = 1;
                    if (this.nextIcon == 1) {
                        f24 = 1.0f;
                    } else {
                        f24 = 0.0f;
                    }
                }
                i18 = this.nextIcon;
                if (i18 != 0) {
                    this.paint2.setAlpha(255);
                } else {
                    this.paint2.setAlpha(255);
                }
                applyShaderMatrix(true);
                canvas2.save();
                canvas2.translate(rect.centerX() + (AndroidUtilities.dp(1.0f) * (1.0f - f24)), rect.centerY());
                f25 = f24 * 500.0f;
                i19 = this.currentIcon;
                if (i19 == 1) {
                    f26 = 90.0f;
                } else {
                    f26 = 0.0f;
                }
                if (i19 == 0) {
                    if (i19 == 1) {
                        if (f25 < 100.0f) {
                            interpolation = CubicBezierInterpolator.EASE_BOTH.getInterpolation(f25 / 100.0f) * (-5.0f);
                        } else if (f25 < 484.0f) {
                            interpolation = (CubicBezierInterpolator.EASE_BOTH.getInterpolation((f25 - 100.0f) / 384.0f) * 95.0f) - 5.0f;
                        }
                        f26 = interpolation;
                    }
                } else if (i19 == 1) {
                    if (f25 < 100.0f) {
                        interpolation = CubicBezierInterpolator.EASE_BOTH.getInterpolation(f25 / 100.0f) * (-5.0f);
                    } else if (f25 < 484.0f) {
                        interpolation = (CubicBezierInterpolator.EASE_BOTH.getInterpolation((f25 - 100.0f) / 384.0f) * 95.0f) - 5.0f;
                    }
                    f26 = interpolation;
                }
                canvas2.rotate(f26);
                i20 = this.currentIcon;
                if (i20 == 0) {
                    canvas2.scale(f20, f20);
                } else {
                    canvas2.scale(f20, f20);
                }
                Theme.playPauseAnimator.draw(canvas2, this.paint2, f25);
                canvas2.scale(1.0f, -1.0f);
                Theme.playPauseAnimator.draw(canvas2, this.paint2, f25);
                canvas2.restore();
            }
            if (i16 == i17) {
                f24 = 1.0f;
            } else {
                f24 = 0.0f;
            }
            i18 = this.nextIcon;
            if (i18 != 0) {
                this.paint2.setAlpha(255);
            } else {
                this.paint2.setAlpha(255);
            }
            applyShaderMatrix(true);
            canvas2.save();
            canvas2.translate(rect.centerX() + (AndroidUtilities.dp(1.0f) * (1.0f - f24)), rect.centerY());
            f25 = f24 * 500.0f;
            i19 = this.currentIcon;
            if (i19 == 1) {
                f26 = 90.0f;
            } else {
                f26 = 0.0f;
            }
            if (i19 == 0) {
                if (i19 == 1) {
                    if (f25 < 100.0f) {
                        interpolation = CubicBezierInterpolator.EASE_BOTH.getInterpolation(f25 / 100.0f) * (-5.0f);
                    } else if (f25 < 484.0f) {
                        interpolation = (CubicBezierInterpolator.EASE_BOTH.getInterpolation((f25 - 100.0f) / 384.0f) * 95.0f) - 5.0f;
                    }
                    f26 = interpolation;
                }
            } else if (i19 == 1) {
                if (f25 < 100.0f) {
                    interpolation = CubicBezierInterpolator.EASE_BOTH.getInterpolation(f25 / 100.0f) * (-5.0f);
                } else if (f25 < 484.0f) {
                    interpolation = (CubicBezierInterpolator.EASE_BOTH.getInterpolation((f25 - 100.0f) / 384.0f) * 95.0f) - 5.0f;
                }
                f26 = interpolation;
            }
            canvas2.rotate(f26);
            i20 = this.currentIcon;
            if (i20 == 0) {
                canvas2.scale(f20, f20);
            } else {
                canvas2.scale(f20, f20);
            }
            Theme.playPauseAnimator.draw(canvas2, this.paint2, f25);
            canvas2.scale(1.0f, -1.0f);
            Theme.playPauseAnimator.draw(canvas2, this.paint2, f25);
            canvas2.restore();
        } else {
            if (i16 == 1) {
                if (this.nextIcon != 0) {
                    i17 = 1;
                } else if (this.animatingTransition) {
                    if (this.nextIcon == 0) {
                        f24 = 1.0f - this.transitionProgress;
                    } else {
                        f24 = this.transitionProgress;
                    }
                    i17 = 1;
                } else {
                    i17 = 1;
                    if (this.nextIcon == 1) {
                        f24 = 1.0f;
                    } else {
                        f24 = 0.0f;
                    }
                }
                i18 = this.nextIcon;
                if (i18 != 0) {
                    this.paint2.setAlpha(255);
                } else {
                    this.paint2.setAlpha(255);
                }
                applyShaderMatrix(true);
                canvas2.save();
                canvas2.translate(rect.centerX() + (AndroidUtilities.dp(1.0f) * (1.0f - f24)), rect.centerY());
                f25 = f24 * 500.0f;
                i19 = this.currentIcon;
                if (i19 == 1) {
                    f26 = 90.0f;
                } else {
                    f26 = 0.0f;
                }
                if (i19 == 0) {
                    if (i19 == 1) {
                        if (f25 < 100.0f) {
                            interpolation = CubicBezierInterpolator.EASE_BOTH.getInterpolation(f25 / 100.0f) * (-5.0f);
                        } else if (f25 < 484.0f) {
                            interpolation = (CubicBezierInterpolator.EASE_BOTH.getInterpolation((f25 - 100.0f) / 384.0f) * 95.0f) - 5.0f;
                        }
                        f26 = interpolation;
                    }
                } else if (i19 == 1) {
                    if (f25 < 100.0f) {
                        interpolation = CubicBezierInterpolator.EASE_BOTH.getInterpolation(f25 / 100.0f) * (-5.0f);
                    } else if (f25 < 484.0f) {
                        interpolation = (CubicBezierInterpolator.EASE_BOTH.getInterpolation((f25 - 100.0f) / 384.0f) * 95.0f) - 5.0f;
                    }
                    f26 = interpolation;
                }
                canvas2.rotate(f26);
                i20 = this.currentIcon;
                if (i20 == 0) {
                    canvas2.scale(f20, f20);
                } else {
                    canvas2.scale(f20, f20);
                }
                Theme.playPauseAnimator.draw(canvas2, this.paint2, f25);
                canvas2.scale(1.0f, -1.0f);
                Theme.playPauseAnimator.draw(canvas2, this.paint2, f25);
                canvas2.restore();
            }
            if (i16 == i17) {
                f24 = 1.0f;
            } else {
                f24 = 0.0f;
            }
            i18 = this.nextIcon;
            if (i18 != 0) {
                this.paint2.setAlpha(255);
            } else {
                this.paint2.setAlpha(255);
            }
            applyShaderMatrix(true);
            canvas2.save();
            canvas2.translate(rect.centerX() + (AndroidUtilities.dp(1.0f) * (1.0f - f24)), rect.centerY());
            f25 = f24 * 500.0f;
            i19 = this.currentIcon;
            if (i19 == 1) {
                f26 = 90.0f;
            } else {
                f26 = 0.0f;
            }
            if (i19 == 0) {
                if (i19 == 1) {
                    if (f25 < 100.0f) {
                        interpolation = CubicBezierInterpolator.EASE_BOTH.getInterpolation(f25 / 100.0f) * (-5.0f);
                    } else if (f25 < 484.0f) {
                        interpolation = (CubicBezierInterpolator.EASE_BOTH.getInterpolation((f25 - 100.0f) / 384.0f) * 95.0f) - 5.0f;
                    }
                    f26 = interpolation;
                }
            } else if (i19 == 1) {
                if (f25 < 100.0f) {
                    interpolation = CubicBezierInterpolator.EASE_BOTH.getInterpolation(f25 / 100.0f) * (-5.0f);
                } else if (f25 < 484.0f) {
                    interpolation = (CubicBezierInterpolator.EASE_BOTH.getInterpolation((f25 - 100.0f) / 384.0f) * 95.0f) - 5.0f;
                }
                f26 = interpolation;
            }
            canvas2.rotate(f26);
            i20 = this.currentIcon;
            if (i20 == 0) {
                canvas2.scale(f20, f20);
            } else {
                canvas2.scale(f20, f20);
            }
            Theme.playPauseAnimator.draw(canvas2, this.paint2, f25);
            canvas2.scale(1.0f, -1.0f);
            Theme.playPauseAnimator.draw(canvas2, this.paint2, f25);
            canvas2.restore();
        }
        if (this.currentIcon != 6) {
            applyShaderMatrix(false);
            if (this.currentIcon != 6) {
                f28 = this.transitionProgress;
                if (f28 > 0.5f) {
                    f30 = (f28 - 0.5f) / 0.5f;
                    fMin2 = 1.0f - Math.min(1.0f, f30 / 0.5f);
                    if (f30 > 0.5f) {
                        f29 = (f30 - 0.5f) / 0.5f;
                    } else {
                        f29 = 0.0f;
                    }
                } else {
                    f29 = 0.0f;
                    fMin2 = 1.0f;
                }
                this.paint.setAlpha(255);
                f27 = f29;
            } else {
                if (this.nextIcon != 6) {
                    this.paint.setAlpha((int) ((1.0f - this.transitionProgress) * 255.0f));
                } else {
                    this.paint.setAlpha(255);
                }
                fMin2 = 0.0f;
                f27 = 1.0f;
            }
            iDp = AndroidUtilities.dp(7.0f) + iCenterY3;
            iDp2 = iCenterX - AndroidUtilities.dp(3.0f);
            if (fMin2 < 1.0f) {
                i21 = iDp2;
                canvas.drawLine(iDp2 - AndroidUtilities.dp(6.0f), iDp - AndroidUtilities.dp(6.0f), iDp2 - (AndroidUtilities.dp(6.0f) * fMin2), iDp - (AndroidUtilities.dp(6.0f) * fMin2), this.paint);
            } else {
                i21 = iDp2;
            }
            if (f27 > 0.0f) {
                float f92114 = i21;
                float f92115 = iDp;
                canvas2 = canvas;
                canvas2.drawLine(f92114, f92115, f92114 + (AndroidUtilities.dp(12.0f) * f27), f92115 - (AndroidUtilities.dp(12.0f) * f27), this.paint);
            } else {
                canvas2 = canvas;
            }
        } else {
            applyShaderMatrix(false);
            if (this.currentIcon != 6) {
                f28 = this.transitionProgress;
                if (f28 > 0.5f) {
                    f30 = (f28 - 0.5f) / 0.5f;
                    fMin2 = 1.0f - Math.min(1.0f, f30 / 0.5f);
                    if (f30 > 0.5f) {
                        f29 = (f30 - 0.5f) / 0.5f;
                    } else {
                        f29 = 0.0f;
                    }
                } else {
                    f29 = 0.0f;
                    fMin2 = 1.0f;
                }
                this.paint.setAlpha(255);
                f27 = f29;
            } else {
                if (this.nextIcon != 6) {
                    this.paint.setAlpha((int) ((1.0f - this.transitionProgress) * 255.0f));
                } else {
                    this.paint.setAlpha(255);
                }
                fMin2 = 0.0f;
                f27 = 1.0f;
            }
            iDp = AndroidUtilities.dp(7.0f) + iCenterY3;
            iDp2 = iCenterX - AndroidUtilities.dp(3.0f);
            if (fMin2 < 1.0f) {
                i21 = iDp2;
                canvas.drawLine(iDp2 - AndroidUtilities.dp(6.0f), iDp - AndroidUtilities.dp(6.0f), iDp2 - (AndroidUtilities.dp(6.0f) * fMin2), iDp - (AndroidUtilities.dp(6.0f) * fMin2), this.paint);
            } else {
                i21 = iDp2;
            }
            if (f27 > 0.0f) {
                float f92116 = i21;
                float f92117 = iDp;
                canvas2 = canvas;
                canvas2.drawLine(f92116, f92117, f92116 + (AndroidUtilities.dp(12.0f) * f27), f92117 - (AndroidUtilities.dp(12.0f) * f27), this.paint);
            } else {
                canvas2 = canvas;
            }
        }
        if (drawable3 != null) {
            int intrinsicWidth15 = (int) (drawable3.getIntrinsicWidth() * f19);
            int intrinsicHeight15 = (int) (drawable3.getIntrinsicHeight() * f19);
            drawable3.setColorFilter(this.colorFilter);
            if (this.currentIcon == this.nextIcon) {
                i27 = 255;
            } else {
                i27 = (int) ((1.0f - this.transitionProgress) * 255.0f);
            }
            drawable3.setAlpha(i27);
            int i41116 = intrinsicWidth15 / 2;
            int i41117 = intrinsicHeight15 / 2;
            drawable3.setBounds(iCenterX - i41116, iCenterY3 - i41117, i41116 + iCenterX, i41117 + iCenterY3);
            drawable3.draw(canvas2);
        }
        if (drawable4 != null) {
            int intrinsicWidth16 = (int) (drawable4.getIntrinsicWidth() * f20);
            int intrinsicHeight16 = (int) (drawable4.getIntrinsicHeight() * f20);
            drawable4.setColorFilter(this.colorFilter);
            if (this.currentIcon == this.nextIcon) {
                i26 = 255;
            } else {
                i26 = (int) (this.transitionProgress * 255.0f);
            }
            drawable4.setAlpha(i26);
            int i517 = intrinsicWidth16 / 2;
            int i518 = intrinsicHeight16 / 2;
            drawable4.setBounds(iCenterX - i517, iCenterY3 - i518, i517 + iCenterX, i518 + iCenterY3);
            drawable4.draw(canvas2);
        }
        if (pathArr4 != null) {
            int iDp1111115 = AndroidUtilities.dp(24.0f);
            this.paint2.setStyle(Paint.Style.FILL_AND_STROKE);
            Paint paint1118 = this.paint2;
            if (this.currentIcon == this.nextIcon) {
                i25 = 255;
            } else {
                i25 = (int) ((1.0f - this.transitionProgress) * 255.0f);
            }
            paint1118.setAlpha(i25);
            applyShaderMatrix(true);
            canvas2.save();
            canvas2.translate(iCenterX, iCenterY3);
            canvas2.scale(f19, f19);
            float f101115 = (-iDp1111115) / 2;
            canvas2.translate(f101115, f101115);
            path4 = pathArr4[0];
            if (path4 != null) {
                canvas2.drawPath(path4, this.paint2);
            }
            path5 = pathArr4[1];
            if (path5 != null) {
                canvas2.drawPath(path5, this.backPaint);
            }
            canvas2.restore();
        }
        if (pathArr3 != null) {
            int iDp1111116 = AndroidUtilities.dp(24.0f);
            if (this.currentIcon == this.nextIcon) {
                i24 = 255;
            } else {
                i24 = (int) (this.transitionProgress * 255.0f);
            }
            this.paint2.setStyle(Paint.Style.FILL_AND_STROKE);
            this.paint2.setAlpha(i24);
            applyShaderMatrix(true);
            canvas2.save();
            canvas2.translate(iCenterX, iCenterY3);
            float f101116 = f20;
            canvas2.scale(f101116, f101116);
            float f101117 = (-iDp1111116) / 2;
            canvas2.translate(f101117, f101117);
            path = pathArr3[0];
            if (path != null) {
                canvas2.drawPath(path, this.paint2);
            }
            if (pathArr3.length >= 3) {
                canvas2.drawPath(path3, this.paint);
            }
            path2 = pathArr3[1];
            if (path2 != null) {
                if (i24 != 255) {
                    int alpha8 = this.backPaint.getAlpha();
                    this.backPaint.setAlpha((int) (alpha8 * (i24 / 255.0f)));
                    canvas2.drawPath(pathArr3[1], this.backPaint);
                    this.backPaint.setAlpha(alpha8);
                } else {
                    canvas2.drawPath(path2, this.backPaint);
                }
            }
            canvas2.restore();
        }
        long jCurrentTimeMillis8 = System.currentTimeMillis();
        j = jCurrentTimeMillis8 - this.lastAnimationTime;
        if (j > 17) {
            j = 17;
        }
        this.lastAnimationTime = jCurrentTimeMillis8;
        i22 = this.currentIcon;
        if (i22 != 3) {
            float f101118 = this.downloadRadOffset + ((360 * j) / 2500.0f);
            this.downloadRadOffset = f101118;
            this.downloadRadOffset = getCircleValue(f101118);
            if (this.nextIcon != 2) {
                f31 = this.downloadProgress;
                f32 = this.downloadProgressAnimationStart;
                f33 = f31 - f32;
                if (f33 > 0.0f) {
                    f34 = this.downloadProgressTime + j;
                    this.downloadProgressTime = f34;
                    if (f34 >= 200.0f) {
                        this.animatedDownloadProgress = f31;
                        this.downloadProgressAnimationStart = f31;
                        this.downloadProgressTime = 0.0f;
                    } else {
                        this.animatedDownloadProgress = f32 + (f33 * this.interpolator.getInterpolation(f34 / 200.0f));
                    }
                }
            }
            invalidateSelf();
        } else {
            float f101119 = this.downloadRadOffset + ((360 * j) / 2500.0f);
            this.downloadRadOffset = f101119;
            this.downloadRadOffset = getCircleValue(f101119);
            if (this.nextIcon != 2) {
                f31 = this.downloadProgress;
                f32 = this.downloadProgressAnimationStart;
                f33 = f31 - f32;
                if (f33 > 0.0f) {
                    f34 = this.downloadProgressTime + j;
                    this.downloadProgressTime = f34;
                    if (f34 >= 200.0f) {
                        this.animatedDownloadProgress = f31;
                        this.downloadProgressAnimationStart = f31;
                        this.downloadProgressTime = 0.0f;
                    } else {
                        this.animatedDownloadProgress = f32 + (f33 * this.interpolator.getInterpolation(f34 / 200.0f));
                    }
                }
            }
            invalidateSelf();
        }
        if (this.animatingTransition) {
            f35 = this.transitionProgress;
            if (f35 < 1.0f) {
                f36 = f35 + (j / this.transitionAnimationTime);
                this.transitionProgress = f36;
                if (f36 >= 1.0f) {
                    this.currentIcon = this.nextIcon;
                    this.transitionProgress = 1.0f;
                    this.animatingTransition = false;
                }
                invalidateSelf();
            }
        }
        i23 = i11;
        if (i23 >= 1) {
            canvas2.restoreToCount(i23);
        }
    }

    @Override // android.graphics.drawable.Drawable
    public int getIntrinsicWidth() {
        return AndroidUtilities.dp(48.0f);
    }

    @Override // android.graphics.drawable.Drawable
    public int getIntrinsicHeight() {
        return AndroidUtilities.dp(48.0f);
    }

    @Override // android.graphics.drawable.Drawable
    public int getMinimumWidth() {
        return AndroidUtilities.dp(48.0f);
    }

    @Override // android.graphics.drawable.Drawable
    public int getMinimumHeight() {
        return AndroidUtilities.dp(48.0f);
    }
}
