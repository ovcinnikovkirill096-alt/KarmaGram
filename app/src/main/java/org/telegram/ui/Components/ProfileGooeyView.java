package org.telegram.ui.Components;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BlendMode;
import android.graphics.Canvas;
import android.graphics.ColorMatrixColorFilter;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.graphics.RectF;
import android.graphics.RenderEffect;
import android.graphics.RenderNode;
import android.graphics.Shader;
import android.os.Build;
import android.widget.FrameLayout;
import androidx.core.math.MathUtils;
import org.telegram.messenger.AndroidUtilities;
import org.telegram.messenger.BotFullscreenButtons$$ExternalSyntheticApiModelOutline0;
import org.telegram.messenger.NotchInfoUtils;
import org.telegram.messenger.SharedConfig;
import org.telegram.messenger.Utilities;

public class ProfileGooeyView extends FrameLayout {
    private final Paint blackPaint;
    private float blurIntensity;
    private boolean enabled;
    private final Impl impl;
    private float intensity;
    public NotchInfoUtils.NotchInfo notchInfo;
    private final Path path;
    private float pullProgress;

    /* JADX INFO: Access modifiers changed from: private */
    interface Drawer {
        void draw(Canvas canvas);
    }

    /* JADX INFO: renamed from: -$$Nest$fgetblackPaint, reason: not valid java name */
    static /* bridge */ /* synthetic */ Paint m10696$$Nest$fgetblackPaint(ProfileGooeyView profileGooeyView) {
        return profileGooeyView.blackPaint;
    }

    /* JADX INFO: renamed from: -$$Nest$fgetblurIntensity, reason: not valid java name */
    static /* bridge */ /* synthetic */ float m10697$$Nest$fgetblurIntensity(ProfileGooeyView profileGooeyView) {
        return profileGooeyView.blurIntensity;
    }

    /* JADX INFO: renamed from: -$$Nest$fgetpath, reason: not valid java name */
    static /* bridge */ /* synthetic */ Path m10699$$Nest$fgetpath(ProfileGooeyView profileGooeyView) {
        return profileGooeyView.path;
    }

    /* JADX INFO: renamed from: -$$Nest$fgetpullProgress, reason: not valid java name */
    static /* bridge */ /* synthetic */ float m10700$$Nest$fgetpullProgress(ProfileGooeyView profileGooeyView) {
        return profileGooeyView.pullProgress;
    }

    public ProfileGooeyView(Context context) {
        super(context);
        Paint paint = new Paint(1);
        this.blackPaint = paint;
        this.path = new Path();
        paint.setColor(-16777216);
        if (Build.VERSION.SDK_INT >= 31 && SharedConfig.getDevicePerformanceClass() >= 1) {
            this.impl = new GPUImpl(SharedConfig.getDevicePerformanceClass() == 2 ? 1.0f : 1.5f);
        } else {
            this.impl = new CPUImpl();
        }
        setIntensity(15.0f);
        setBlurIntensity(0.0f);
        setWillNotDraw(false);
    }

    public float getAvatarEndScale() {
        float fMin;
        int iDp;
        NotchInfoUtils.NotchInfo notchInfo = this.notchInfo;
        if (notchInfo == null) {
            return 0.8f;
        }
        if (notchInfo.isLikelyCircle) {
            fMin = notchInfo.bounds.width() - AndroidUtilities.dp(2.0f);
            iDp = AndroidUtilities.dp(100.0f);
        } else {
            fMin = Math.min(notchInfo.bounds.width(), this.notchInfo.bounds.height());
            iDp = AndroidUtilities.dp(100.0f);
        }
        return Math.min(0.8f, fMin / iDp);
    }

    public void setIntensity(float f) {
        this.intensity = f;
        this.impl.setIntensity(f);
        invalidate();
    }

    public void setPullProgress(float f) {
        this.pullProgress = f;
        invalidate();
    }

    public void setBlurIntensity(float f) {
        this.blurIntensity = f;
        this.impl.setBlurIntensity(f);
        invalidate();
    }

    public void setGooeyEnabled(boolean z) {
        if (this.enabled == z) {
            return;
        }
        this.enabled = z;
        invalidate();
    }

    @Override // android.view.View
    protected void onSizeChanged(int i, int i2, int i3, int i4) {
        super.onSizeChanged(i, i2, i3, i4);
        NotchInfoUtils.NotchInfo info = NotchInfoUtils.getInfo(getContext());
        this.notchInfo = info;
        if ((info != null && info.gravity != 17) || getWidth() > getHeight()) {
            this.notchInfo = null;
        }
        this.impl.onSizeChanged(i, i2);
    }

    @Override // android.view.View
    public void draw(Canvas canvas) {
        if (!this.enabled) {
            super.draw(canvas);
        } else {
            this.impl.draw(new Drawer() { // from class: org.telegram.ui.Components.ProfileGooeyView$$ExternalSyntheticLambda0
                @Override // org.telegram.ui.Components.ProfileGooeyView.Drawer
                public final void draw(Canvas canvas2) {
                    this.f$0.lambda$draw$0(canvas2);
                }
            }, canvas);
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$draw$0(Canvas canvas) {
        canvas.save();
        canvas.translate(0.0f, AndroidUtilities.dp(32.0f));
        super.draw(canvas);
        canvas.restore();
    }

    private final class CPUImpl implements Impl {
        private Bitmap bitmap;
        private Canvas bitmapCanvas;
        private int bitmapOrigH;
        private int bitmapOrigW;
        private final Paint bitmapPaint;
        private final Paint bitmapPaint2;
        private int optimizedH;
        private int optimizedW;
        private final float scaleConst;

        @Override // org.telegram.ui.Components.ProfileGooeyView.Impl
        public /* synthetic */ void setBlurIntensity(float f) {
            Impl.CC.$default$setBlurIntensity(this, f);
        }

        @Override // org.telegram.ui.Components.ProfileGooeyView.Impl
        public /* synthetic */ void setIntensity(float f) {
            Impl.CC.$default$setIntensity(this, f);
        }

        private CPUImpl() {
            Paint paint = new Paint();
            this.bitmapPaint = paint;
            Paint paint2 = new Paint();
            this.bitmapPaint2 = paint2;
            this.scaleConst = 6.0f;
            paint.setFlags(7);
            paint.setFilterBitmap(true);
            paint2.setFlags(7);
            paint2.setFilterBitmap(true);
            paint2.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.SRC_ATOP));
            paint.setColorFilter(new ColorMatrixColorFilter(new float[]{0.0f, 0.0f, 0.0f, 0.0f, 0.0f, 0.0f, 0.0f, 0.0f, 0.0f, 0.0f, 0.0f, 0.0f, 0.0f, 0.0f, 0.0f, 0.0f, 0.0f, 0.0f, 60.0f, -7500.0f}));
        }

        @Override // org.telegram.ui.Components.ProfileGooeyView.Impl
        public void onSizeChanged(int i, int i2) {
            Bitmap bitmap = this.bitmap;
            if (bitmap != null) {
                AndroidUtilities.recycleBitmap(bitmap);
                this.bitmap = null;
            }
            this.optimizedW = Math.min(AndroidUtilities.dp(120.0f), i);
            int iMin = Math.min(AndroidUtilities.dp(220.0f), i2);
            this.optimizedH = iMin;
            this.bitmapOrigW = this.optimizedW;
            int iDp = iMin + AndroidUtilities.dp(32.0f);
            this.bitmapOrigH = iDp;
            this.bitmap = Bitmap.createBitmap((int) (this.bitmapOrigW / 6.0f), (int) (iDp / 6.0f), Bitmap.Config.ARGB_8888);
            this.bitmapCanvas = new Canvas(this.bitmap);
        }

        @Override // org.telegram.ui.Components.ProfileGooeyView.Impl
        public void draw(Drawer drawer, Canvas canvas) {
            int i;
            Bitmap bitmap = this.bitmap;
            if (bitmap == null || this.bitmapCanvas == null || bitmap.isRecycled()) {
                return;
            }
            int iClamp = (int) ((1.0f - ((MathUtils.clamp(ProfileGooeyView.this.blurIntensity, 0.2f, 0.3f) - 0.2f) / 0.10000001f)) * 255.0f);
            float width = (ProfileGooeyView.this.getWidth() - this.optimizedW) / 2.0f;
            canvas.save();
            canvas.translate(0.0f, -AndroidUtilities.dp(32.0f));
            if (iClamp != 255) {
                this.bitmap.eraseColor(0);
                this.bitmapCanvas.save();
                this.bitmapCanvas.scale(this.bitmap.getWidth() / this.bitmapOrigW, this.bitmap.getHeight() / this.bitmapOrigH);
                float f = -width;
                this.bitmapCanvas.translate(f, 0.0f);
                drawer.draw(this.bitmapCanvas);
                this.bitmapCanvas.restore();
                this.bitmapCanvas.save();
                this.bitmapCanvas.scale(this.bitmap.getWidth() / this.bitmapOrigW, this.bitmap.getHeight() / this.bitmapOrigH);
                if (ProfileGooeyView.this.notchInfo != null) {
                    this.bitmapCanvas.save();
                    this.bitmapCanvas.translate(f, AndroidUtilities.dp(32.0f));
                    ProfileGooeyView profileGooeyView = ProfileGooeyView.this;
                    NotchInfoUtils.NotchInfo notchInfo = profileGooeyView.notchInfo;
                    if (notchInfo.isLikelyCircle) {
                        float fMin = Math.min(notchInfo.bounds.width(), ProfileGooeyView.this.notchInfo.bounds.height()) / 2.0f;
                        Canvas canvas2 = this.bitmapCanvas;
                        float fCenterX = ProfileGooeyView.this.notchInfo.bounds.centerX();
                        RectF rectF = ProfileGooeyView.this.notchInfo.bounds;
                        canvas2.drawCircle(fCenterX, rectF.bottom - (rectF.width() / 2.0f), fMin, ProfileGooeyView.this.blackPaint);
                    } else if (notchInfo.isAccurate) {
                        this.bitmapCanvas.drawPath(notchInfo.path, profileGooeyView.blackPaint);
                    } else {
                        float fMax = Math.max(notchInfo.bounds.width(), ProfileGooeyView.this.notchInfo.bounds.height()) / 2.0f;
                        Canvas canvas3 = this.bitmapCanvas;
                        ProfileGooeyView profileGooeyView2 = ProfileGooeyView.this;
                        canvas3.drawRoundRect(profileGooeyView2.notchInfo.bounds, fMax, fMax, profileGooeyView2.blackPaint);
                    }
                    this.bitmapCanvas.restore();
                } else {
                    this.bitmapCanvas.drawRect(0.0f, 0.0f, this.optimizedW, AndroidUtilities.dp(32.0f), ProfileGooeyView.this.blackPaint);
                }
                this.bitmapCanvas.restore();
                Utilities.stackBlurBitmap(this.bitmap, (int) ((ProfileGooeyView.this.intensity * 2.0f) / 6.0f));
                canvas.save();
                canvas.translate(width, 0.0f);
                canvas.saveLayer(0.0f, 0.0f, this.bitmapOrigW, this.bitmapOrigH, null);
                canvas.scale(this.bitmapOrigW / this.bitmap.getWidth(), this.bitmapOrigH / this.bitmap.getHeight());
                canvas.drawBitmap(this.bitmap, 0.0f, 0.0f, this.bitmapPaint);
                canvas.drawBitmap(this.bitmap, 0.0f, 0.0f, this.bitmapPaint2);
                canvas.restore();
                canvas.restore();
            }
            if (iClamp != 0) {
                if (iClamp != 255) {
                    i = iClamp;
                    canvas.saveLayerAlpha(width, 0.0f, width + this.optimizedW, this.optimizedH, i);
                } else {
                    i = iClamp;
                }
                drawer.draw(canvas);
                if (i != 255) {
                    canvas.restore();
                }
            }
            canvas.restore();
        }
    }

    private final class GPUImpl implements Impl {
        private final Paint blackNodePaint;
        private final RenderNode blurNode;
        private final RenderNode effectNode;
        private final RenderNode effectNotchNode;
        private final float factorMult;
        private final Paint filter;
        private final RenderNode node;
        private final RectF temp;
        private final RectF whole;
        private final RectF wholeOptimized;

        @Override // org.telegram.ui.Components.ProfileGooeyView.Impl
        public /* synthetic */ void onSizeChanged(int i, int i2) {
            Impl.CC.$default$onSizeChanged(this, i, i2);
        }

        private GPUImpl(float f) {
            this.filter = new Paint(1);
            this.node = BotFullscreenButtons$$ExternalSyntheticApiModelOutline0.m("render");
            this.effectNotchNode = BotFullscreenButtons$$ExternalSyntheticApiModelOutline0.m("effectNotch");
            this.effectNode = BotFullscreenButtons$$ExternalSyntheticApiModelOutline0.m("effect");
            this.blurNode = BotFullscreenButtons$$ExternalSyntheticApiModelOutline0.m("blur");
            this.whole = new RectF();
            this.temp = new RectF();
            Paint paint = new Paint();
            this.blackNodePaint = paint;
            this.wholeOptimized = new RectF();
            this.factorMult = f;
            paint.setColor(-16777216);
            paint.setBlendMode(BlendMode.SRC_IN);
        }

        @Override // org.telegram.ui.Components.ProfileGooeyView.Impl
        public void setIntensity(float f) {
            RenderNode renderNode = this.effectNode;
            Shader.TileMode tileMode = Shader.TileMode.CLAMP;
            renderNode.setRenderEffect(RenderEffect.createBlurEffect(f, f, tileMode));
            this.effectNotchNode.setRenderEffect(RenderEffect.createBlurEffect(f, f, tileMode));
            this.filter.setColorFilter(new ColorMatrixColorFilter(new float[]{1.0f, 0.0f, 0.0f, 0.0f, 0.0f, 0.0f, 1.0f, 0.0f, 0.0f, 0.0f, 0.0f, 0.0f, 1.0f, 0.0f, 0.0f, 0.0f, 0.0f, 0.0f, 51.0f, -6375.0f}));
        }

        @Override // org.telegram.ui.Components.ProfileGooeyView.Impl
        public void setBlurIntensity(float f) {
            if (f == 0.0f) {
                this.blurNode.setRenderEffect(null);
            } else {
                this.blurNode.setRenderEffect(RenderEffect.createBlurEffect((ProfileGooeyView.this.intensity * f) / this.factorMult, (f * ProfileGooeyView.this.intensity) / this.factorMult, Shader.TileMode.DECAL));
            }
        }

        /* JADX WARN: Failed to calculate best type for var: r29v0 ??
        jadx.core.utils.exceptions.JadxRuntimeException: Type update failed for variable: r29v0 ??, new type: android.graphics.Canvas
        	at jadx.core.dex.visitors.typeinference.TypeUpdate.apply(TypeUpdate.java:109)
        	at jadx.core.dex.visitors.typeinference.TypeUpdate.apply(TypeUpdate.java:59)
        	at jadx.core.dex.visitors.typeinference.TypeInferenceVisitor.calculateFromBounds(TypeInferenceVisitor.java:147)
        	at jadx.core.dex.visitors.typeinference.TypeInferenceVisitor.setBestType(TypeInferenceVisitor.java:125)
        	at jadx.core.dex.visitors.typeinference.TypeInferenceVisitor.lambda$runTypePropagation$2(TypeInferenceVisitor.java:103)
        	at java.base/java.util.ArrayList.forEach(ArrayList.java:1596)
        	at jadx.core.dex.visitors.typeinference.TypeInferenceVisitor.runTypePropagation(TypeInferenceVisitor.java:103)
        	at jadx.core.dex.visitors.typeinference.TypeInferenceVisitor.visit(TypeInferenceVisitor.java:75)
        Caused by: java.lang.NullPointerException
         */
        /* JADX WARN: Failed to calculate best type for var: r2v14 ??
        jadx.core.utils.exceptions.JadxRuntimeException: Type update failed for variable: r2v14 ??, new type: int
        	at jadx.core.dex.visitors.typeinference.TypeUpdate.apply(TypeUpdate.java:109)
        	at jadx.core.dex.visitors.typeinference.TypeUpdate.apply(TypeUpdate.java:59)
        	at jadx.core.dex.visitors.typeinference.FixTypesVisitor.calculateFromBounds(FixTypesVisitor.java:159)
        	at jadx.core.dex.visitors.typeinference.FixTypesVisitor.setBestType(FixTypesVisitor.java:136)
        	at jadx.core.dex.visitors.typeinference.FixTypesVisitor.deduceType(FixTypesVisitor.java:241)
        	at jadx.core.dex.visitors.typeinference.FixTypesVisitor.tryDeduceTypes(FixTypesVisitor.java:224)
        	at jadx.core.dex.visitors.typeinference.FixTypesVisitor.visit(FixTypesVisitor.java:94)
        Caused by: java.lang.NullPointerException
         */
        /* JADX WARN: Failed to calculate best type for var: r2v14 ??
        jadx.core.utils.exceptions.JadxRuntimeException: Type update failed for variable: r2v14 ??, new type: int
        	at jadx.core.dex.visitors.typeinference.TypeUpdate.apply(TypeUpdate.java:109)
        	at jadx.core.dex.visitors.typeinference.TypeUpdate.apply(TypeUpdate.java:59)
        	at jadx.core.dex.visitors.typeinference.TypeInferenceVisitor.calculateFromBounds(TypeInferenceVisitor.java:147)
        	at jadx.core.dex.visitors.typeinference.TypeInferenceVisitor.setBestType(TypeInferenceVisitor.java:125)
        	at jadx.core.dex.visitors.typeinference.TypeInferenceVisitor.lambda$runTypePropagation$2(TypeInferenceVisitor.java:103)
        	at java.base/java.util.ArrayList.forEach(ArrayList.java:1596)
        	at jadx.core.dex.visitors.typeinference.TypeInferenceVisitor.runTypePropagation(TypeInferenceVisitor.java:103)
        	at jadx.core.dex.visitors.typeinference.TypeInferenceVisitor.visit(TypeInferenceVisitor.java:75)
        Caused by: java.lang.NullPointerException
         */
        /* JADX WARN: Failed to set immutable type for var: r29v0 ??
        jadx.core.utils.exceptions.JadxRuntimeException: Type update failed for variable: r29v0 ??, new type: android.graphics.Canvas
        	at jadx.core.dex.visitors.typeinference.TypeUpdate.apply(TypeUpdate.java:109)
        	at jadx.core.dex.visitors.typeinference.TypeUpdate.applyWithWiderIgnSame(TypeUpdate.java:73)
        	at jadx.core.dex.visitors.typeinference.TypeInferenceVisitor.setImmutableType(TypeInferenceVisitor.java:111)
        	at jadx.core.dex.visitors.typeinference.TypeInferenceVisitor.lambda$runTypePropagation$1(TypeInferenceVisitor.java:102)
        	at java.base/java.util.ArrayList.forEach(ArrayList.java:1596)
        	at jadx.core.dex.visitors.typeinference.TypeInferenceVisitor.runTypePropagation(TypeInferenceVisitor.java:102)
        	at jadx.core.dex.visitors.typeinference.TypeInferenceVisitor.visit(TypeInferenceVisitor.java:75)
        Caused by: java.lang.NullPointerException
         */
        /*  JADX ERROR: Types fix failed
            jadx.core.utils.exceptions.JadxRuntimeException: Type update failed for variable: r2v14 ??, new type: int
            	at jadx.core.dex.visitors.typeinference.TypeUpdate.apply(TypeUpdate.java:109)
            	at jadx.core.dex.visitors.typeinference.TypeUpdate.apply(TypeUpdate.java:59)
            	at jadx.core.dex.visitors.typeinference.FixTypesVisitor.tryPossibleTypes(FixTypesVisitor.java:186)
            	at jadx.core.dex.visitors.typeinference.FixTypesVisitor.deduceType(FixTypesVisitor.java:245)
            	at jadx.core.dex.visitors.typeinference.FixTypesVisitor.tryDeduceTypes(FixTypesVisitor.java:224)
            	at jadx.core.dex.visitors.typeinference.FixTypesVisitor.visit(FixTypesVisitor.java:94)
            Caused by: java.lang.NullPointerException
            */
        @Override // org.telegram.ui.Components.ProfileGooeyView.Impl
        public void draw(org.telegram.ui.Components.ProfileGooeyView.Drawer r28, android.graphics.Canvas r29) {
            /*
                Method dump skipped, instruction units count: 1203
                To view this dump add '--comments-level debug' option
            */
            throw new UnsupportedOperationException("Method not decompiled: org.telegram.ui.Components.ProfileGooeyView.GPUImpl.draw(org.telegram.ui.Components.ProfileGooeyView$Drawer, android.graphics.Canvas):void");
        }
    }

    private interface Impl {
        void draw(Drawer drawer, Canvas canvas);

        void onSizeChanged(int i, int i2);

        void setBlurIntensity(float f);

        void setIntensity(float f);

        /* JADX INFO: renamed from: org.telegram.ui.Components.ProfileGooeyView$Impl$-CC, reason: invalid class name */
        public abstract /* synthetic */ class CC {
            public static void $default$setIntensity(Impl impl, float f) {
            }

            public static void $default$setBlurIntensity(Impl impl, float f) {
            }

            public static void $default$onSizeChanged(Impl impl, int i, int i2) {
            }
        }
    }
}
