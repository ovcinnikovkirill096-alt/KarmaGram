package org.telegram.ui.Components;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.DashPathEffect;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.text.TextPaint;
import android.view.MotionEvent;
import android.view.View;
import android.view.accessibility.AccessibilityNodeInfo;
import androidx.core.graphics.ColorUtils;
import androidx.core.math.MathUtils;
import org.telegram.messenger.AndroidUtilities;
import org.telegram.messenger.LocaleController;
import org.telegram.tgnet.TLObject;
import org.telegram.ui.ActionBar.Theme;

public class SlideChooseView extends View {
    private final SeekBarAccessibilityDelegate accessibilityDelegate;
    private boolean allowSlide;
    private Callback callback;
    private int circleSize;
    private int dashedFrom;
    private int gapSize;
    private int lastDash;
    private Drawable[] leftDrawables;
    private Paint linePaint;
    private int lineSize;
    private int minIndex;
    private boolean moving;
    private AnimatedFloat movingAnimatedHolder;
    private boolean needDivider;
    private int[] optionsSizes;
    private String[] optionsStr;
    private Paint paint;
    private final Theme.ResourcesProvider resourcesProvider;
    private int selectedIndex;
    private AnimatedFloat selectedIndexAnimatedHolder;
    private float selectedIndexTouch;
    private int sideSide;
    private boolean startMoving;
    private int startMovingPreset;
    private TextPaint textPaint;
    private boolean touchWasClose;
    private float xTouchDown;
    private float yTouchDown;

    public SlideChooseView(Context context) {
        this(context, null);
    }

    public SlideChooseView(Context context, Theme.ResourcesProvider resourcesProvider) {
        super(context);
        this.dashedFrom = -1;
        this.needDivider = false;
        this.minIndex = Integer.MIN_VALUE;
        CubicBezierInterpolator cubicBezierInterpolator = CubicBezierInterpolator.DEFAULT;
        this.selectedIndexAnimatedHolder = new AnimatedFloat(this, 120L, cubicBezierInterpolator);
        this.movingAnimatedHolder = new AnimatedFloat(this, 150L, cubicBezierInterpolator);
        this.touchWasClose = false;
        this.allowSlide = true;
        this.resourcesProvider = resourcesProvider;
        this.paint = new Paint(1);
        this.textPaint = new TextPaint(1);
        Paint paint = new Paint(1);
        this.linePaint = paint;
        paint.setStrokeWidth(AndroidUtilities.dp(2.0f));
        this.linePaint.setStrokeCap(Paint.Cap.ROUND);
        this.textPaint.setTextSize(AndroidUtilities.dp(13.0f));
        this.accessibilityDelegate = new IntSeekBarAccessibilityDelegate() { // from class: org.telegram.ui.Components.SlideChooseView.1
            @Override // org.telegram.ui.Components.IntSeekBarAccessibilityDelegate
            protected int getProgress() {
                return SlideChooseView.this.selectedIndex;
            }

            @Override // org.telegram.ui.Components.IntSeekBarAccessibilityDelegate
            protected void setProgress(int i) {
                SlideChooseView.this.setOption(i);
            }

            @Override // org.telegram.ui.Components.IntSeekBarAccessibilityDelegate
            protected int getMaxValue() {
                return SlideChooseView.this.optionsStr.length - 1;
            }

            @Override // org.telegram.ui.Components.SeekBarAccessibilityDelegate
            protected CharSequence getContentDescription(View view) {
                if (SlideChooseView.this.selectedIndex < SlideChooseView.this.optionsStr.length) {
                    return SlideChooseView.this.optionsStr[SlideChooseView.this.selectedIndex];
                }
                return null;
            }
        };
    }

    public void setCallback(Callback callback) {
        this.callback = callback;
    }

    public void setOptions(int i, String... strArr) {
        setOptions(i, null, strArr);
    }

    public void setOptions(int i, Drawable[] drawableArr, String... strArr) {
        this.optionsStr = strArr;
        this.leftDrawables = drawableArr;
        this.selectedIndex = i;
        this.optionsSizes = new int[strArr.length];
        int i2 = 0;
        while (true) {
            String[] strArr2 = this.optionsStr;
            if (i2 >= strArr2.length) {
                break;
            }
            this.optionsSizes[i2] = (int) Math.ceil(this.textPaint.measureText(strArr2[i2]));
            i2++;
        }
        Drawable[] drawableArr2 = this.leftDrawables;
        if (drawableArr2 != null) {
            for (Drawable drawable : drawableArr2) {
                drawable.setBounds(0, 0, drawable.getIntrinsicWidth(), drawable.getIntrinsicHeight());
            }
        }
        requestLayout();
    }

    public void setMinAllowedIndex(int i) {
        String[] strArr;
        if (i != -1 && (strArr = this.optionsStr) != null) {
            i = Math.min(i, strArr.length - 1);
        }
        if (this.minIndex != i) {
            this.minIndex = i;
            if (this.selectedIndex < i) {
                this.selectedIndex = i;
            }
            invalidate();
        }
    }

    public void setDashedFrom(int i) {
        this.dashedFrom = i;
    }

    public void setNeedDivider(boolean z) {
        this.needDivider = z;
    }

    @Override // android.view.View
    public boolean onTouchEvent(MotionEvent motionEvent) {
        if (!this.allowSlide) {
            return true;
        }
        float x = motionEvent.getX();
        float y = motionEvent.getY();
        float f = x - this.sideSide;
        int i = this.circleSize;
        float fClamp = MathUtils.clamp((f + (i / 2.0f)) / ((this.lineSize + (this.gapSize * 2)) + i), 0.0f, this.optionsStr.length - 1);
        boolean z = Math.abs(fClamp - ((float) Math.round(fClamp))) < 0.35f;
        if (z) {
            fClamp = Math.round(fClamp);
        }
        int i2 = this.minIndex;
        if (i2 != Integer.MIN_VALUE) {
            fClamp = Math.max(fClamp, i2);
        }
        if (motionEvent.getAction() == 0) {
            this.xTouchDown = x;
            this.yTouchDown = y;
            this.selectedIndexTouch = fClamp;
            this.startMovingPreset = this.selectedIndex;
            this.startMoving = true;
            invalidate();
        } else if (motionEvent.getAction() == 2) {
            if (!this.moving && Math.abs(this.xTouchDown - x) > Math.abs(this.yTouchDown - y)) {
                getParent().requestDisallowInterceptTouchEvent(true);
            }
            if (this.startMoving && Math.abs(this.xTouchDown - x) >= AndroidUtilities.touchSlop) {
                this.moving = true;
                this.startMoving = false;
            }
            if (this.moving) {
                this.selectedIndexTouch = fClamp;
                invalidate();
                if (Math.round(this.selectedIndexTouch) != this.selectedIndex && z) {
                    setOption(Math.round(this.selectedIndexTouch));
                }
            }
            invalidate();
        } else if (motionEvent.getAction() == 1 || motionEvent.getAction() == 3) {
            if (!this.moving) {
                this.selectedIndexTouch = fClamp;
                if (motionEvent.getAction() == 1 && Math.round(this.selectedIndexTouch) != this.selectedIndex) {
                    setOption(Math.round(this.selectedIndexTouch));
                }
            } else {
                int i3 = this.selectedIndex;
                if (i3 != this.startMovingPreset) {
                    setOption(i3);
                }
            }
            Callback callback = this.callback;
            if (callback != null) {
                callback.onTouchEnd();
            }
            this.startMoving = false;
            this.moving = false;
            invalidate();
            getParent().requestDisallowInterceptTouchEvent(false);
        }
        return true;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void setOption(int i) {
        if (this.selectedIndex != i) {
            AndroidUtilities.vibrateCursor(this);
        }
        this.selectedIndex = i;
        Callback callback = this.callback;
        if (callback != null) {
            callback.onOptionSelected(i);
        }
        invalidate();
    }

    @Override // android.view.View
    protected void onMeasure(int i, int i2) {
        super.onMeasure(i, View.MeasureSpec.makeMeasureSpec(AndroidUtilities.dp(74.0f), TLObject.FLAG_30));
        this.circleSize = AndroidUtilities.dp(6.0f);
        this.gapSize = AndroidUtilities.dp(2.0f);
        this.sideSide = AndroidUtilities.dp(22.0f);
        int measuredWidth = getMeasuredWidth();
        int i3 = this.circleSize;
        String[] strArr = this.optionsStr;
        this.lineSize = (((measuredWidth - (i3 * strArr.length)) - ((this.gapSize * 2) * (strArr.length - 1))) - (this.sideSide * 2)) / Math.max(1, strArr.length - 1);
    }

    @Override // android.view.View
    protected void onDraw(Canvas canvas) {
        float f;
        float f2;
        Canvas canvas2 = canvas;
        float f3 = this.selectedIndexAnimatedHolder.set(this.selectedIndex);
        float f4 = 0.0f;
        float f5 = 1.0f;
        float f6 = this.movingAnimatedHolder.set(this.moving ? 1.0f : 0.0f);
        int measuredHeight = (getMeasuredHeight() / 2) + AndroidUtilities.dp(11.0f);
        int i = 0;
        while (i < this.optionsStr.length) {
            int i2 = this.sideSide;
            int i3 = this.lineSize + (this.gapSize * 2);
            int i4 = this.circleSize;
            int i5 = i2 + ((i3 + i4) * i) + (i4 / 2);
            float f7 = i;
            float f8 = f7 - f3;
            float fMax = Math.max(f4, f5 - Math.abs(f8));
            float fClamp = MathUtils.clamp((f3 - f7) + f5, f4, f5);
            int themedColor = getThemedColor(Theme.key_switchTrack);
            int themedColor2 = getThemedColor(Theme.key_switchTrackChecked);
            float f9 = f5;
            int i6 = this.minIndex;
            int iBlendARGB = ColorUtils.blendARGB(themedColor, Theme.multAlpha(themedColor2, (i6 == Integer.MIN_VALUE || i > i6) ? f9 : 0.5f), fClamp);
            if (!this.allowSlide) {
                iBlendARGB = AndroidUtilities.getTransparentColor(iBlendARGB, 0.5f);
            }
            this.paint.setColor(iBlendARGB);
            this.linePaint.setColor(iBlendARGB);
            float f10 = measuredHeight;
            canvas2.drawCircle(i5, f10, AndroidUtilities.lerp(this.circleSize / 2, AndroidUtilities.dpf2(6.0f), fMax), this.paint);
            float f11 = 3.0f;
            if (i != 0) {
                int i7 = (i5 - (this.circleSize / 2)) - this.gapSize;
                int i8 = this.lineSize;
                int i9 = i7 - i8;
                int i10 = this.dashedFrom;
                if (i10 != -1 && i - 1 >= i10) {
                    int iDpf2 = (int) (i9 + AndroidUtilities.dpf2(3.0f));
                    int iDpf3 = (int) (i8 - AndroidUtilities.dpf2(3.0f));
                    float f12 = iDpf3;
                    float fDpf2 = f12 / AndroidUtilities.dpf2(13.0f);
                    if (this.lastDash != fDpf2) {
                        this.linePaint.setPathEffect(new DashPathEffect(new float[]{AndroidUtilities.dpf2(6.0f), (f12 - (AndroidUtilities.dpf2(8.0f) * fDpf2)) / (fDpf2 - f9)}, 0.0f));
                        this.lastDash = (int) fDpf2;
                    }
                    f = 0.5f;
                    canvas2 = canvas;
                    canvas2.drawLine(iDpf2 + AndroidUtilities.dpf2(f9), f10, (iDpf2 + iDpf3) - AndroidUtilities.dpf2(f9), f10, this.linePaint);
                } else {
                    f11 = 3.0f;
                    f = 0.5f;
                    float f13 = f8 - f9;
                    float fClamp2 = MathUtils.clamp(f9 - Math.abs(f13), 0.0f, f9);
                    int iDpf4 = (int) (i8 - (AndroidUtilities.dpf2(3.0f) * MathUtils.clamp(f9 - Math.min(Math.abs(f8), Math.abs(f13)), 0.0f, f9)));
                    int iDpf5 = (int) (i9 + (AndroidUtilities.dpf2(3.0f) * fClamp2));
                    f9 = 1.0f;
                    canvas2 = canvas;
                    canvas2.drawRect(iDpf5, f10 - AndroidUtilities.dpf2(1.0f), iDpf4 + iDpf5, AndroidUtilities.dpf2(1.0f) + f10, this.paint);
                }
            } else {
                f = 0.5f;
                f11 = 3.0f;
            }
            int i11 = this.optionsSizes[i];
            String str = this.optionsStr[i];
            this.textPaint.setColor(AndroidUtilities.getTransparentColor(ColorUtils.blendARGB(getThemedColor(Theme.key_windowBackgroundWhiteGrayText), getThemedColor(Theme.key_windowBackgroundWhiteBlueText), fMax), this.allowSlide ? f9 : f));
            if (this.leftDrawables != null) {
                canvas2.save();
                if (i == 0) {
                    canvas2.translate(AndroidUtilities.dp(12.0f), AndroidUtilities.dp(15.5f));
                } else if (i == this.optionsStr.length - 1) {
                    canvas2.translate(((getMeasuredWidth() - i11) - AndroidUtilities.dp(22.0f)) - AndroidUtilities.dp(10.0f), AndroidUtilities.dp(28.0f) - AndroidUtilities.dp(12.5f));
                } else {
                    canvas2.translate((i5 - (i11 / 2)) - AndroidUtilities.dp(10.0f), AndroidUtilities.dp(28.0f) - AndroidUtilities.dp(12.5f));
                }
                this.leftDrawables[i].setColorFilter(this.textPaint.getColor(), PorterDuff.Mode.MULTIPLY);
                this.leftDrawables[i].draw(canvas2);
                canvas2.restore();
                canvas2.save();
                f2 = 0.0f;
                canvas2.translate((this.leftDrawables[i].getIntrinsicWidth() / 2.0f) - AndroidUtilities.dp(i == 0 ? f11 : 2.0f), 0.0f);
            } else {
                f2 = 0.0f;
            }
            if (i == 0) {
                canvas2.drawText(str, AndroidUtilities.dp(22.0f), AndroidUtilities.dp(28.0f), this.textPaint);
            } else if (i == this.optionsStr.length - 1) {
                canvas2.drawText(str, (getMeasuredWidth() - i11) - AndroidUtilities.dp(22.0f), AndroidUtilities.dp(28.0f), this.textPaint);
            } else {
                canvas2.drawText(str, i5 - (i11 / 2), AndroidUtilities.dp(28.0f), this.textPaint);
            }
            if (this.leftDrawables != null) {
                canvas2.restore();
            }
            i++;
            f4 = f2;
            f5 = f9;
        }
        float f14 = f5;
        float f15 = f4;
        float f16 = this.sideSide;
        int i12 = this.lineSize + (this.gapSize * 2);
        int i13 = this.circleSize;
        float f17 = f16 + ((i12 + i13) * f3) + (i13 / 2);
        Paint paint = this.paint;
        int i14 = Theme.key_switchTrackChecked;
        paint.setColor(AndroidUtilities.getTransparentColor(ColorUtils.setAlphaComponent(getThemedColor(i14), 80), this.allowSlide ? f14 : 0.5f));
        float f18 = measuredHeight;
        canvas2.drawCircle(f17, f18, AndroidUtilities.dp(f6 * 12.0f), this.paint);
        this.paint.setColor(AndroidUtilities.getTransparentColor(getThemedColor(i14), this.allowSlide ? f14 : 0.5f));
        canvas2.drawCircle(f17, f18, AndroidUtilities.dp(6.0f), this.paint);
        if (this.needDivider) {
            canvas2.drawLine(LocaleController.isRTL ? f15 : AndroidUtilities.dp(21.0f), getMeasuredHeight() - 1, getMeasuredWidth() - (LocaleController.isRTL ? AndroidUtilities.dp(21.0f) : 0), getMeasuredHeight() - 1, Theme.dividerPaint);
        }
    }

    @Override // android.view.View
    public void onInitializeAccessibilityNodeInfo(AccessibilityNodeInfo accessibilityNodeInfo) {
        super.onInitializeAccessibilityNodeInfo(accessibilityNodeInfo);
        this.accessibilityDelegate.onInitializeAccessibilityNodeInfoInternal(this, accessibilityNodeInfo);
    }

    @Override // android.view.View
    public boolean performAccessibilityAction(int i, Bundle bundle) {
        return super.performAccessibilityAction(i, bundle) || this.accessibilityDelegate.performAccessibilityActionInternal(this, i, bundle);
    }

    public int getSelectedIndex() {
        return this.selectedIndex;
    }

    private int getThemedColor(int i) {
        return Theme.getColor(i, this.resourcesProvider);
    }

    public interface Callback {
        void onOptionSelected(int i);

        void onTouchEnd();

        /* JADX INFO: renamed from: org.telegram.ui.Components.SlideChooseView$Callback$-CC, reason: invalid class name */
        public abstract /* synthetic */ class CC {
            public static void $default$onTouchEnd(Callback callback) {
            }
        }
    }

    public void setAllowSlide(boolean z) {
        this.allowSlide = z;
    }
}
