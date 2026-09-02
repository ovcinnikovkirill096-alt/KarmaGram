package org.telegram.ui.Components;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.RectF;
import android.view.MotionEvent;
import android.widget.FrameLayout;
import org.telegram.messenger.AndroidUtilities;
import org.telegram.ui.BubbleActivity;

public class PhotoFilterBlurControl extends FrameLayout {
    private static final float BlurInsetProximity = AndroidUtilities.dp(20.0f);
    private static final float BlurViewCenterInset = AndroidUtilities.dp(30.0f);
    private static final float BlurViewRadiusInset = AndroidUtilities.dp(30.0f);
    private final int GestureStateBegan;
    private final int GestureStateCancelled;
    private final int GestureStateChanged;
    private final int GestureStateEnded;
    private final int GestureStateFailed;
    private BlurViewActiveControl activeControl;
    private Size actualAreaSize;
    private float angle;
    private Paint arcPaint;
    private RectF arcRect;
    private Point centerPoint;
    private boolean checkForMoving;
    private boolean checkForZooming;
    private PhotoFilterLinearBlurControlDelegate delegate;
    private float falloff;
    private boolean inBubbleMode;
    private boolean isMoving;
    private boolean isZooming;
    private Paint paint;
    private float pointerScale;
    private float pointerStartX;
    private float pointerStartY;
    private float size;
    private Point startCenterPoint;
    private float startDistance;
    private float startPointerDistance;
    private float startRadius;
    private int type;

    private enum BlurViewActiveControl {
        BlurViewActiveControlNone,
        BlurViewActiveControlCenter,
        BlurViewActiveControlInnerRadius,
        BlurViewActiveControlOuterRadius,
        BlurViewActiveControlWholeArea,
        BlurViewActiveControlRotation
    }

    public interface PhotoFilterLinearBlurControlDelegate {
        void valueChanged(Point point, float f, float f2, float f3);
    }

    private float degreesToRadians(float f) {
        return (f * 3.1415927f) / 180.0f;
    }

    private void setSelected(boolean z, boolean z2) {
    }

    public PhotoFilterBlurControl(Context context) {
        super(context);
        this.GestureStateBegan = 1;
        this.GestureStateChanged = 2;
        this.GestureStateEnded = 3;
        this.GestureStateCancelled = 4;
        this.GestureStateFailed = 5;
        this.startCenterPoint = new Point();
        this.actualAreaSize = new Size();
        this.centerPoint = new Point(0.5f, 0.5f);
        this.falloff = 0.15f;
        this.size = 0.35f;
        this.arcRect = new RectF();
        this.pointerScale = 1.0f;
        this.checkForMoving = true;
        this.paint = new Paint(1);
        this.arcPaint = new Paint(1);
        setWillNotDraw(false);
        this.paint.setColor(-1);
        this.arcPaint.setColor(-1);
        this.arcPaint.setStrokeWidth(AndroidUtilities.dp(2.0f));
        this.arcPaint.setStyle(Paint.Style.STROKE);
        this.inBubbleMode = context instanceof BubbleActivity;
    }

    public void setType(int i) {
        this.type = i;
        invalidate();
    }

    public void setDelegate(PhotoFilterLinearBlurControlDelegate photoFilterLinearBlurControlDelegate) {
        this.delegate = photoFilterLinearBlurControlDelegate;
    }

    private float getDistance(MotionEvent motionEvent) {
        if (motionEvent.getPointerCount() != 2) {
            return 0.0f;
        }
        float x = motionEvent.getX(0);
        float y = motionEvent.getY(0);
        float x2 = x - motionEvent.getX(1);
        float y2 = y - motionEvent.getY(1);
        return (float) Math.sqrt((x2 * x2) + (y2 * y2));
    }

    /* JADX WARN: Code duplicated, block: B:18:0x002e  */
    /* JADX WARN: Code duplicated, block: B:20:0x0032  */
    /* JADX WARN: Code duplicated, block: B:21:0x0038  */
    /* JADX WARN: Code duplicated, block: B:23:0x003c  */
    /* JADX WARN: Code duplicated, block: B:25:0x0047  */
    /* JADX WARN: Code duplicated, block: B:27:0x004d  */
    /* JADX WARN: Code duplicated, block: B:33:0x008d  */
    /* JADX WARN: Code duplicated, block: B:34:0x008f  */
    /* JADX WARN: Code duplicated, block: B:37:0x0093  */
    /* JADX WARN: Code duplicated, block: B:38:0x0095  */
    /* JADX WARN: Code duplicated, block: B:41:0x009a  */
    /* JADX WARN: Code duplicated, block: B:44:0x00a0  */
    /* JADX WARN: Code duplicated, block: B:46:0x00d1  */
    /* JADX WARN: Code duplicated, block: B:47:0x00d4  */
    /* JADX WARN: Code duplicated, block: B:52:0x00e4  */
    /* JADX WARN: Code duplicated, block: B:54:0x00ea  */
    /* JADX WARN: Code duplicated, block: B:57:0x00f3  */
    /* JADX WARN: Code duplicated, block: B:62:0x0100  */
    /* JADX WARN: Code duplicated, block: B:63:0x0102  */
    /* JADX WARN: Code duplicated, block: B:65:0x0108  */
    /* JADX WARN: Code duplicated, block: B:66:0x010b  */
    /* JADX WARN: Code duplicated, block: B:71:0x011b  */
    /* JADX WARN: Code duplicated, block: B:73:0x0121  */
    /* JADX WARN: Code duplicated, block: B:78:0x012e  */
    /* JADX WARN: Code duplicated, block: B:79:0x0132  */
    /* JADX WARN: Code duplicated, block: B:81:0x0136  */
    /* JADX WARN: Code duplicated, block: B:84:0x0143  */
    /* JADX WARN: Code duplicated, block: B:89:0x0151  */
    @Override // android.view.View
    public boolean onTouchEvent(MotionEvent motionEvent) {
        Point point;
        float fSqrt;
        float actualInnerRadius;
        float actualOuterRadius;
        boolean z;
        float f;
        float f2;
        int i;
        float f3;
        float fAbs;
        float f4;
        int actionMasked = motionEvent.getActionMasked();
        if (actionMasked == 0) {
            if (motionEvent.getPointerCount() == 1) {
                if (this.checkForMoving && !this.isMoving) {
                    float x = motionEvent.getX();
                    float y = motionEvent.getY();
                    Point actualCenterPoint = getActualCenterPoint();
                    point = new Point(x - actualCenterPoint.x, y - actualCenterPoint.y);
                    float f5 = point.x;
                    float f6 = point.y;
                    fSqrt = (float) Math.sqrt((f5 * f5) + (f6 * f6));
                    actualInnerRadius = getActualInnerRadius();
                    actualOuterRadius = getActualOuterRadius();
                    if (Math.abs(actualOuterRadius - actualInnerRadius) < BlurInsetProximity) {
                        z = true;
                    } else {
                        z = false;
                    }
                    if (z) {
                        f = 0.0f;
                    } else {
                        f = BlurViewRadiusInset;
                    }
                    f2 = z ? 0.0f : BlurViewRadiusInset;
                    i = this.type;
                    if (i == 0) {
                        fAbs = (float) Math.abs((((double) point.x) * Math.cos(((double) degreesToRadians(this.angle)) + 1.5707963267948966d)) + (((double) point.y) * Math.sin(((double) degreesToRadians(this.angle)) + 1.5707963267948966d)));
                        if (fSqrt < BlurViewCenterInset) {
                            this.isMoving = true;
                        } else {
                            f4 = BlurViewRadiusInset;
                            if (fAbs <= actualInnerRadius - f4 && fAbs < f + actualInnerRadius) {
                                this.isMoving = true;
                            } else if ((fAbs <= actualOuterRadius - f2 && fAbs < actualOuterRadius + f4) || fAbs <= actualInnerRadius - f4 || fAbs >= actualOuterRadius + f4) {
                                this.isMoving = true;
                            }
                        }
                    } else if (i == 1) {
                        if (fSqrt < BlurViewCenterInset) {
                            this.isMoving = true;
                        } else {
                            f3 = BlurViewRadiusInset;
                            if (fSqrt <= actualInnerRadius - f3 && fSqrt < actualInnerRadius + f) {
                                this.isMoving = true;
                            } else if (fSqrt > actualOuterRadius - f2 && fSqrt < actualOuterRadius + f3) {
                                this.isMoving = true;
                            }
                        }
                    }
                    this.checkForMoving = false;
                    if (this.isMoving) {
                        handlePan(1, motionEvent);
                    }
                }
            } else {
                if (this.isMoving) {
                    handlePan(3, motionEvent);
                    this.checkForMoving = true;
                    this.isMoving = false;
                }
                if (motionEvent.getPointerCount() == 2) {
                    if (this.checkForZooming && !this.isZooming) {
                        handlePinch(1, motionEvent);
                        this.isZooming = true;
                    }
                } else {
                    handlePinch(3, motionEvent);
                    this.checkForZooming = true;
                    this.isZooming = false;
                }
            }
        } else if (actionMasked == 1) {
            if (this.isMoving) {
                handlePan(3, motionEvent);
                this.isMoving = false;
            } else if (this.isZooming) {
                handlePinch(3, motionEvent);
                this.isZooming = false;
            }
            this.checkForMoving = true;
            this.checkForZooming = true;
        } else if (actionMasked != 2) {
            if (actionMasked == 3) {
                if (this.isMoving) {
                    handlePan(3, motionEvent);
                    this.isMoving = false;
                } else if (this.isZooming) {
                    handlePinch(3, motionEvent);
                    this.isZooming = false;
                }
                this.checkForMoving = true;
                this.checkForZooming = true;
            } else if (actionMasked != 5) {
                if (actionMasked == 6) {
                    if (this.isMoving) {
                        handlePan(3, motionEvent);
                        this.isMoving = false;
                    } else if (this.isZooming) {
                        handlePinch(3, motionEvent);
                        this.isZooming = false;
                    }
                    this.checkForMoving = true;
                    this.checkForZooming = true;
                }
            } else if (motionEvent.getPointerCount() == 1) {
                if (this.checkForMoving) {
                    float x2 = motionEvent.getX();
                    float y2 = motionEvent.getY();
                    Point actualCenterPoint2 = getActualCenterPoint();
                    point = new Point(x2 - actualCenterPoint2.x, y2 - actualCenterPoint2.y);
                    float f7 = point.x;
                    float f8 = point.y;
                    fSqrt = (float) Math.sqrt((f7 * f7) + (f8 * f8));
                    actualInnerRadius = getActualInnerRadius();
                    actualOuterRadius = getActualOuterRadius();
                    if (Math.abs(actualOuterRadius - actualInnerRadius) < BlurInsetProximity) {
                        z = true;
                    } else {
                        z = false;
                    }
                    if (z) {
                        f = 0.0f;
                    } else {
                        f = BlurViewRadiusInset;
                    }
                    if (z) {
                    }
                    i = this.type;
                    if (i == 0) {
                        fAbs = (float) Math.abs((((double) point.x) * Math.cos(((double) degreesToRadians(this.angle)) + 1.5707963267948966d)) + (((double) point.y) * Math.sin(((double) degreesToRadians(this.angle)) + 1.5707963267948966d)));
                        if (fSqrt < BlurViewCenterInset) {
                            this.isMoving = true;
                        } else {
                            f4 = BlurViewRadiusInset;
                            if (fAbs <= actualInnerRadius - f4) {
                                if (fAbs <= actualOuterRadius - f2) {
                                    this.isMoving = true;
                                } else {
                                    this.isMoving = true;
                                }
                            } else if (fAbs <= actualOuterRadius - f2) {
                                this.isMoving = true;
                            } else {
                                this.isMoving = true;
                            }
                        }
                    } else if (i == 1) {
                        if (fSqrt < BlurViewCenterInset) {
                            this.isMoving = true;
                        } else {
                            f3 = BlurViewRadiusInset;
                            if (fSqrt <= actualInnerRadius - f3) {
                                if (fSqrt > actualOuterRadius - f2) {
                                    this.isMoving = true;
                                }
                            } else if (fSqrt > actualOuterRadius - f2) {
                                this.isMoving = true;
                            }
                        }
                    }
                    this.checkForMoving = false;
                    if (this.isMoving) {
                        handlePan(1, motionEvent);
                    }
                }
            } else {
                if (this.isMoving) {
                    handlePan(3, motionEvent);
                    this.checkForMoving = true;
                    this.isMoving = false;
                }
                if (motionEvent.getPointerCount() == 2) {
                    if (this.checkForZooming) {
                        handlePinch(1, motionEvent);
                        this.isZooming = true;
                    }
                } else {
                    handlePinch(3, motionEvent);
                    this.checkForZooming = true;
                    this.isZooming = false;
                }
            }
        } else if (this.isMoving) {
            handlePan(2, motionEvent);
        } else if (this.isZooming) {
            handlePinch(2, motionEvent);
        }
        return true;
    }

    private void handlePan(int i, MotionEvent motionEvent) {
        float x = motionEvent.getX();
        float y = motionEvent.getY();
        Point actualCenterPoint = getActualCenterPoint();
        float f = x - actualCenterPoint.x;
        float f2 = y - actualCenterPoint.y;
        float fSqrt = (float) Math.sqrt((f * f) + (f2 * f2));
        Size size = this.actualAreaSize;
        float fMin = Math.min(size.width, size.height);
        float f3 = this.falloff * fMin;
        float f4 = this.size * fMin;
        float fAbs = (float) Math.abs((((double) f) * Math.cos(((double) degreesToRadians(this.angle)) + 1.5707963267948966d)) + (((double) f2) * Math.sin(((double) degreesToRadians(this.angle)) + 1.5707963267948966d)));
        int i2 = 0;
        if (i == 1) {
            this.pointerStartX = motionEvent.getX();
            this.pointerStartY = motionEvent.getY();
            i2 = Math.abs(f4 - f3) < BlurInsetProximity ? 1 : 0;
            float f5 = i2 != 0 ? 0.0f : BlurViewRadiusInset;
            float f6 = i2 == 0 ? BlurViewRadiusInset : 0.0f;
            int i3 = this.type;
            if (i3 == 0) {
                if (fSqrt < BlurViewCenterInset) {
                    this.activeControl = BlurViewActiveControl.BlurViewActiveControlCenter;
                    this.startCenterPoint = actualCenterPoint;
                } else {
                    float f7 = BlurViewRadiusInset;
                    if (fAbs > f3 - f7 && fAbs < f5 + f3) {
                        this.activeControl = BlurViewActiveControl.BlurViewActiveControlInnerRadius;
                        this.startDistance = fAbs;
                        this.startRadius = f3;
                    } else if (fAbs > f4 - f6 && fAbs < f4 + f7) {
                        this.activeControl = BlurViewActiveControl.BlurViewActiveControlOuterRadius;
                        this.startDistance = fAbs;
                        this.startRadius = f4;
                    } else if (fAbs <= f3 - f7 || fAbs >= f4 + f7) {
                        this.activeControl = BlurViewActiveControl.BlurViewActiveControlRotation;
                    }
                }
            } else if (i3 == 1) {
                if (fSqrt < BlurViewCenterInset) {
                    this.activeControl = BlurViewActiveControl.BlurViewActiveControlCenter;
                    this.startCenterPoint = actualCenterPoint;
                } else {
                    float f8 = BlurViewRadiusInset;
                    if (fSqrt > f3 - f8 && fSqrt < f5 + f3) {
                        this.activeControl = BlurViewActiveControl.BlurViewActiveControlInnerRadius;
                        this.startDistance = fSqrt;
                        this.startRadius = f3;
                    } else if (fSqrt > f4 - f6 && fSqrt < f8 + f4) {
                        this.activeControl = BlurViewActiveControl.BlurViewActiveControlOuterRadius;
                        this.startDistance = fSqrt;
                        this.startRadius = f4;
                    }
                }
            }
            setSelected(true, true);
            return;
        }
        if (i != 2) {
            if (i == 3 || i == 4 || i == 5) {
                this.activeControl = BlurViewActiveControl.BlurViewActiveControlNone;
                setSelected(false, true);
                return;
            }
            return;
        }
        int i4 = this.type;
        if (i4 == 0) {
            int i5 = AnonymousClass1.$SwitchMap$org$telegram$ui$Components$PhotoFilterBlurControl$BlurViewActiveControl[this.activeControl.ordinal()];
            if (i5 == 1) {
                float f9 = x - this.pointerStartX;
                float f10 = y - this.pointerStartY;
                float width = (getWidth() - this.actualAreaSize.width) / 2.0f;
                float f11 = this.inBubbleMode ? 0 : AndroidUtilities.statusBarHeight;
                float height = getHeight();
                Size size2 = this.actualAreaSize;
                float f12 = size2.height;
                Rect rect = new Rect(width, f11 + ((height - f12) / 2.0f), size2.width, f12);
                float f13 = rect.x;
                float fMax = Math.max(f13, Math.min(rect.width + f13, this.startCenterPoint.x + f9));
                float f14 = rect.y;
                Point point = new Point(fMax, Math.max(f14, Math.min(rect.height + f14, this.startCenterPoint.y + f10)));
                float f15 = point.x - rect.x;
                Size size3 = this.actualAreaSize;
                float f16 = size3.width;
                this.centerPoint = new Point(f15 / f16, ((point.y - rect.y) + ((f16 - size3.height) / 2.0f)) / f16);
            } else if (i5 == 2) {
                this.falloff = Math.min(Math.max(0.1f, (this.startRadius + (fAbs - this.startDistance)) / fMin), this.size - 0.02f);
            } else if (i5 == 3) {
                this.size = Math.max(this.falloff + 0.02f, (this.startRadius + (fAbs - this.startDistance)) / fMin);
            } else if (i5 == 4) {
                float f17 = x - this.pointerStartX;
                float f18 = y - this.pointerStartY;
                boolean z = x > actualCenterPoint.x;
                boolean z2 = y > actualCenterPoint.y;
                boolean z3 = Math.abs(f18) > Math.abs(f17);
                if (z || z2 ? !(!z || z2 ? !z || !z2 ? !z3 ? f17 >= 0.0f : f18 >= 0.0f : !z3 ? f17 >= 0.0f : f18 <= 0.0f : !z3 ? f17 <= 0.0f : f18 <= 0.0f) : !(!z3 ? f17 <= 0.0f : f18 >= 0.0f)) {
                    i2 = 1;
                }
                this.angle += ((((float) Math.sqrt((f17 * f17) + (f18 * f18))) * ((i2 * 2) - 1)) / 3.1415927f) / 1.15f;
                this.pointerStartX = x;
                this.pointerStartY = y;
            }
        } else if (i4 == 1) {
            int i6 = AnonymousClass1.$SwitchMap$org$telegram$ui$Components$PhotoFilterBlurControl$BlurViewActiveControl[this.activeControl.ordinal()];
            if (i6 == 1) {
                float f19 = x - this.pointerStartX;
                float f20 = y - this.pointerStartY;
                float width2 = (getWidth() - this.actualAreaSize.width) / 2.0f;
                float f21 = this.inBubbleMode ? 0 : AndroidUtilities.statusBarHeight;
                float height2 = getHeight();
                Size size4 = this.actualAreaSize;
                float f22 = size4.height;
                Rect rect2 = new Rect(width2, f21 + ((height2 - f22) / 2.0f), size4.width, f22);
                float f23 = rect2.x;
                float fMax2 = Math.max(f23, Math.min(rect2.width + f23, this.startCenterPoint.x + f19));
                float f24 = rect2.y;
                Point point2 = new Point(fMax2, Math.max(f24, Math.min(rect2.height + f24, this.startCenterPoint.y + f20)));
                float f25 = point2.x - rect2.x;
                Size size5 = this.actualAreaSize;
                float f26 = size5.width;
                this.centerPoint = new Point(f25 / f26, ((point2.y - rect2.y) + ((f26 - size5.height) / 2.0f)) / f26);
            } else if (i6 == 2) {
                this.falloff = Math.min(Math.max(0.1f, (this.startRadius + (fSqrt - this.startDistance)) / fMin), this.size - 0.02f);
            } else if (i6 == 3) {
                this.size = Math.max(this.falloff + 0.02f, (this.startRadius + (fSqrt - this.startDistance)) / fMin);
            }
        }
        invalidate();
        PhotoFilterLinearBlurControlDelegate photoFilterLinearBlurControlDelegate = this.delegate;
        if (photoFilterLinearBlurControlDelegate != null) {
            photoFilterLinearBlurControlDelegate.valueChanged(this.centerPoint, this.falloff, this.size, degreesToRadians(this.angle) + 1.5707964f);
        }
    }

    /* JADX INFO: renamed from: org.telegram.ui.Components.PhotoFilterBlurControl$1, reason: invalid class name */
    static /* synthetic */ class AnonymousClass1 {
        static final /* synthetic */ int[] $SwitchMap$org$telegram$ui$Components$PhotoFilterBlurControl$BlurViewActiveControl;

        static {
            int[] iArr = new int[BlurViewActiveControl.values().length];
            $SwitchMap$org$telegram$ui$Components$PhotoFilterBlurControl$BlurViewActiveControl = iArr;
            try {
                iArr[BlurViewActiveControl.BlurViewActiveControlCenter.ordinal()] = 1;
            } catch (NoSuchFieldError unused) {
            }
            try {
                $SwitchMap$org$telegram$ui$Components$PhotoFilterBlurControl$BlurViewActiveControl[BlurViewActiveControl.BlurViewActiveControlInnerRadius.ordinal()] = 2;
            } catch (NoSuchFieldError unused2) {
            }
            try {
                $SwitchMap$org$telegram$ui$Components$PhotoFilterBlurControl$BlurViewActiveControl[BlurViewActiveControl.BlurViewActiveControlOuterRadius.ordinal()] = 3;
            } catch (NoSuchFieldError unused3) {
            }
            try {
                $SwitchMap$org$telegram$ui$Components$PhotoFilterBlurControl$BlurViewActiveControl[BlurViewActiveControl.BlurViewActiveControlRotation.ordinal()] = 4;
            } catch (NoSuchFieldError unused4) {
            }
        }
    }

    private void handlePinch(int i, MotionEvent motionEvent) {
        if (i == 1) {
            this.startPointerDistance = getDistance(motionEvent);
            this.pointerScale = 1.0f;
            this.activeControl = BlurViewActiveControl.BlurViewActiveControlWholeArea;
            setSelected(true, true);
        } else if (i != 2) {
            if (i == 3 || i == 4 || i == 5) {
                this.activeControl = BlurViewActiveControl.BlurViewActiveControlNone;
                setSelected(false, true);
                return;
            }
            return;
        }
        float distance = getDistance(motionEvent);
        float f = this.pointerScale + (((distance - this.startPointerDistance) / AndroidUtilities.density) * 0.01f);
        this.pointerScale = f;
        float fMax = Math.max(0.1f, this.falloff * f);
        this.falloff = fMax;
        this.size = Math.max(fMax + 0.02f, this.size * this.pointerScale);
        this.pointerScale = 1.0f;
        this.startPointerDistance = distance;
        invalidate();
        PhotoFilterLinearBlurControlDelegate photoFilterLinearBlurControlDelegate = this.delegate;
        if (photoFilterLinearBlurControlDelegate != null) {
            photoFilterLinearBlurControlDelegate.valueChanged(this.centerPoint, this.falloff, this.size, degreesToRadians(this.angle) + 1.5707964f);
        }
    }

    public void setActualAreaSize(float f, float f2) {
        Size size = this.actualAreaSize;
        size.width = f;
        size.height = f2;
    }

    @Override // android.view.View
    protected void onDraw(Canvas canvas) {
        Canvas canvas2 = canvas;
        super.onDraw(canvas);
        Point actualCenterPoint = getActualCenterPoint();
        float actualInnerRadius = getActualInnerRadius();
        float actualOuterRadius = getActualOuterRadius();
        canvas2.translate(actualCenterPoint.x, actualCenterPoint.y);
        int i = this.type;
        int i2 = 0;
        if (i == 0) {
            canvas2.rotate(this.angle);
            float fDp = AndroidUtilities.dp(6.0f);
            float fDp2 = AndroidUtilities.dp(12.0f);
            float fDp3 = AndroidUtilities.dp(1.5f);
            for (int i3 = 0; i3 < 30; i3++) {
                float f = fDp2 + fDp;
                float f2 = i3 * f;
                float f3 = -actualInnerRadius;
                float f4 = f2 + fDp2;
                float f5 = fDp3 - actualInnerRadius;
                canvas2.drawRect(f2, f3, f4, f5, this.paint);
                float f6 = ((-i3) * f) - fDp;
                float f7 = f6 - fDp2;
                canvas2 = canvas;
                canvas2.drawRect(f7, f3, f6, f5, this.paint);
                float f8 = fDp3 + actualInnerRadius;
                canvas2.drawRect(f2, actualInnerRadius, f4, f8, this.paint);
                canvas2.drawRect(f7, actualInnerRadius, f6, f8, this.paint);
            }
            float fDp4 = AndroidUtilities.dp(6.0f);
            while (i2 < 64) {
                float f9 = fDp4 + fDp;
                float f10 = i2 * f9;
                float f11 = -actualOuterRadius;
                float f12 = fDp4 + f10;
                float f13 = fDp3 - actualOuterRadius;
                canvas.drawRect(f10, f11, f12, f13, this.paint);
                float f14 = ((-i2) * f9) - fDp;
                float f15 = f14 - fDp4;
                canvas.drawRect(f15, f11, f14, f13, this.paint);
                float f16 = fDp3 + actualOuterRadius;
                canvas.drawRect(f10, actualOuterRadius, f12, f16, this.paint);
                canvas.drawRect(f15, actualOuterRadius, f14, f16, this.paint);
                i2++;
            }
        } else if (i == 1) {
            float f17 = -actualInnerRadius;
            this.arcRect.set(f17, f17, actualInnerRadius, actualInnerRadius);
            for (int i4 = 0; i4 < 22; i4++) {
                canvas.drawArc(this.arcRect, 16.35f * i4, 10.2f, false, this.arcPaint);
            }
            float f18 = -actualOuterRadius;
            this.arcRect.set(f18, f18, actualOuterRadius, actualOuterRadius);
            while (i2 < 64) {
                canvas.drawArc(this.arcRect, 5.62f * i2, 3.6f, false, this.arcPaint);
                i2++;
            }
        }
        canvas.drawCircle(0.0f, 0.0f, AndroidUtilities.dp(8.0f), this.paint);
    }

    private Point getActualCenterPoint() {
        float width = getWidth();
        float f = this.actualAreaSize.width;
        float f2 = ((width - f) / 2.0f) + (this.centerPoint.x * f);
        int i = !this.inBubbleMode ? AndroidUtilities.statusBarHeight : 0;
        float height = getHeight();
        Size size = this.actualAreaSize;
        float f3 = size.height;
        float f4 = i + ((height - f3) / 2.0f);
        float f5 = size.width;
        return new Point(f2, (f4 - ((f5 - f3) / 2.0f)) + (this.centerPoint.y * f5));
    }

    private float getActualInnerRadius() {
        Size size = this.actualAreaSize;
        return Math.min(size.width, size.height) * this.falloff;
    }

    private float getActualOuterRadius() {
        Size size = this.actualAreaSize;
        return Math.min(size.width, size.height) * this.size;
    }
}
