package org.telegram.messenger.utils;

import android.app.Activity;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.os.Build;
import android.os.Handler;
import android.os.HandlerThread;
import android.os.Looper;
import android.view.FrameMetrics;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import java.util.Locale;
import java.util.concurrent.atomic.AtomicBoolean;
import org.telegram.messenger.AndroidUtilities;

public final class FrameMetricsOverlayView extends View {
    private final AtomicBoolean attachedToWindowManager;
    private final Paint bgPaint;
    private Window hostWindow;
    private Window.OnFrameMetricsAvailableListener listener;
    private WindowManager.LayoutParams lp;
    private Handler metricsHandler;
    private HandlerThread metricsThread;
    private final Runnable redraw;
    private final AtomicBoolean running;
    private final Paint textPaint;
    private final Handler uiHandler;
    private WindowManager wm;

    /* JADX INFO: Access modifiers changed from: private */
    enum Metric {
        UNKNOWN_DELAY_DURATION(0, "unknown delay", true),
        INPUT_HANDLING_DURATION(1, "input", true),
        ANIMATION_DURATION(2, "animation", true),
        LAYOUT_MEASURE_DURATION(3, "layout", true),
        DRAW_DURATION(4, "draw", true),
        SYNC_DURATION(5, "sync", true),
        COMMAND_ISSUE_DURATION(6, "cmd issue", true),
        SWAP_BUFFERS_DURATION(7, "swap buffers", true),
        GPU_DURATION(12, "gpu", true, 31),
        TOTAL_DURATION(8, "total", true);

        double avgMs;
        final boolean isDuration;
        final int key;
        final String label;
        long last;
        final int minApi;

        Metric(int i, String str, boolean z) {
            this(i, str, z, 24);
        }

        Metric(int i, String str, boolean z, int i2) {
            this.last = Long.MIN_VALUE;
            this.avgMs = 0.0d;
            this.key = i;
            this.label = str;
            this.isDuration = z;
            this.minApi = i2;
        }

        boolean isAvailable() {
            return Build.VERSION.SDK_INT >= this.minApi;
        }
    }

    public static FrameMetricsOverlayView attachToActivityCorner(Activity activity, int i, int i2) {
        FrameMetricsOverlayView frameMetricsOverlayView = new FrameMetricsOverlayView(activity);
        frameMetricsOverlayView.attachInternal(activity, i, i2);
        return frameMetricsOverlayView;
    }

    public void detach() {
        stop();
        if (this.wm != null && this.attachedToWindowManager.getAndSet(false)) {
            try {
                this.wm.removeViewImmediate(this);
            } catch (Throwable unused) {
            }
        }
        this.wm = null;
        this.lp = null;
        this.hostWindow = null;
    }

    public FrameMetricsOverlayView(Context context) {
        super(context.getApplicationContext());
        Paint paint = new Paint(1);
        this.bgPaint = paint;
        Paint paint2 = new Paint(1);
        this.textPaint = paint2;
        this.running = new AtomicBoolean(false);
        this.attachedToWindowManager = new AtomicBoolean(false);
        this.uiHandler = new Handler(Looper.getMainLooper());
        this.redraw = new Runnable() { // from class: org.telegram.messenger.utils.FrameMetricsOverlayView.1
            @Override // java.lang.Runnable
            public void run() {
                if (FrameMetricsOverlayView.this.running.get()) {
                    FrameMetricsOverlayView.this.invalidate();
                    FrameMetricsOverlayView.this.uiHandler.postDelayed(this, 300L);
                }
            }
        };
        paint.setColor(-1342177280);
        paint2.setColor(-1);
        paint2.setTextSize(AndroidUtilities.dp(10.0f));
        paint2.setFakeBoldText(true);
        paint2.setTypeface(AndroidUtilities.getTypeface(AndroidUtilities.TYPEFACE_ROBOTO_MONO));
        setWillNotDraw(false);
    }

    private void attachInternal(Activity activity, int i, int i2) {
        this.wm = (WindowManager) activity.getSystemService("window");
        this.hostWindow = activity.getWindow();
        WindowManager.LayoutParams layoutParams = new WindowManager.LayoutParams(-2, -2, 2, 792, -3);
        this.lp = layoutParams;
        layoutParams.gravity = i;
        int iDp = AndroidUtilities.dp(i2);
        WindowManager.LayoutParams layoutParams2 = this.lp;
        layoutParams2.x = iDp;
        layoutParams2.y = iDp;
        layoutParams2.width = AndroidUtilities.dp(260.0f);
        this.wm.addView(this, this.lp);
        this.attachedToWindowManager.set(true);
        start();
    }

    private void start() {
        if (this.running.getAndSet(true)) {
            return;
        }
        HandlerThread handlerThread = new HandlerThread("FrameMetrics");
        this.metricsThread = handlerThread;
        handlerThread.start();
        this.metricsHandler = new Handler(this.metricsThread.getLooper());
        Window.OnFrameMetricsAvailableListener onFrameMetricsAvailableListener = new Window.OnFrameMetricsAvailableListener() { // from class: org.telegram.messenger.utils.FrameMetricsOverlayView$$ExternalSyntheticLambda0
            @Override // android.view.Window.OnFrameMetricsAvailableListener
            public final void onFrameMetricsAvailable(Window window, FrameMetrics frameMetrics, int i) {
                FrameMetricsOverlayView.m3950$r8$lambda$m42WIPdz4HTot3OVFKZa0YbcXM(window, frameMetrics, i);
            }
        };
        this.listener = onFrameMetricsAvailableListener;
        this.hostWindow.addOnFrameMetricsAvailableListener(onFrameMetricsAvailableListener, this.metricsHandler);
        this.uiHandler.post(this.redraw);
    }

    /* JADX INFO: renamed from: $r8$lambda$m42WIPdz4HTot3OVFKZ-a0YbcXM, reason: not valid java name */
    public static /* synthetic */ void m3950$r8$lambda$m42WIPdz4HTot3OVFKZa0YbcXM(Window window, FrameMetrics frameMetrics, int i) {
        for (Metric metric : Metric.values()) {
            if (!metric.isAvailable()) {
                metric.last = Long.MIN_VALUE;
            } else {
                long metric2 = frameMetrics.getMetric(metric.key);
                metric.last = metric2;
                if (metric.isDuration && metric2 >= 0) {
                    double d = metric2 / 1000000.0d;
                    double d2 = metric.avgMs;
                    if (d2 != 0.0d) {
                        d = ((d - d2) * 0.05d) + d2;
                    }
                    metric.avgMs = d;
                }
            }
        }
    }

    private void stop() {
        Window.OnFrameMetricsAvailableListener onFrameMetricsAvailableListener;
        this.running.set(false);
        this.uiHandler.removeCallbacks(this.redraw);
        Window window = this.hostWindow;
        if (window != null && (onFrameMetricsAvailableListener = this.listener) != null) {
            window.removeOnFrameMetricsAvailableListener(onFrameMetricsAvailableListener);
        }
        HandlerThread handlerThread = this.metricsThread;
        if (handlerThread != null) {
            handlerThread.quitSafely();
        }
    }

    @Override // android.view.View
    protected void onDraw(Canvas canvas) {
        long j;
        int i;
        String str;
        double d;
        float fDp = AndroidUtilities.dp(8.0f);
        float fDp2 = AndroidUtilities.dp(14.0f);
        int i2 = 0;
        for (Metric metric : Metric.values()) {
            i2++;
        }
        canvas.drawRoundRect(0.0f, 0.0f, getWidth() > 0 ? getWidth() : AndroidUtilities.dp(260.0f), (2.0f * fDp) + ((i2 + 6) * fDp2), AndroidUtilities.dp(10.0f), AndroidUtilities.dp(10.0f), this.bgPaint);
        float f = fDp + fDp2;
        Metric[] metricArrValues = Metric.values();
        int length = metricArrValues.length;
        double d2 = 0.0d;
        double d3 = 0.0d;
        double d4 = 0.0d;
        double d5 = 0.0d;
        long j2 = 0;
        int i3 = 0;
        long j3 = 0;
        long j4 = 0;
        long j5 = 0;
        while (i3 < length) {
            int i4 = length;
            Metric metric2 = metricArrValues[i3];
            if (metric2.isAvailable()) {
                j = j5;
                long j6 = metric2.last;
                if (j6 >= 0) {
                    i = i3;
                    if (metric2.isDuration) {
                        fDp2 = fDp2;
                        String str2 = String.format(Locale.US, "%-16s : %5.2f / %5.2f ms", metric2.label, Double.valueOf(j6 / 1000000.0d), Double.valueOf(metric2.avgMs));
                        switch (AnonymousClass2.$SwitchMap$org$telegram$messenger$utils$FrameMetricsOverlayView$Metric[metric2.ordinal()]) {
                            case 1:
                            case 2:
                            case 3:
                            case 4:
                                str2 = str2;
                                j4 += metric2.last;
                                d4 += metric2.avgMs;
                                j5 = j;
                                break;
                            case 5:
                                long j7 = metric2.last;
                                j4 += j7;
                                d = metric2.avgMs;
                                d4 += d;
                                j3 += j7;
                                d3 += d;
                                j5 = j;
                                break;
                            case 6:
                                j3 += metric2.last;
                                d = metric2.avgMs;
                                d3 += d;
                                j5 = j;
                                break;
                            case 7:
                                str2 = str2;
                                j2 += metric2.last;
                                d5 += metric2.avgMs;
                                j5 = j;
                                break;
                            case 8:
                            case 9:
                                j5 = j + metric2.last;
                                str2 = str2;
                                d2 += metric2.avgMs;
                                break;
                            default:
                                str2 = str2;
                                j5 = j;
                                break;
                        }
                        str = str2;
                    } else {
                        str = String.format(Locale.US, "%-16s : %d", metric2.label, Long.valueOf(j6));
                    }
                    canvas.drawText(str, fDp, f, this.textPaint);
                    f += fDp2;
                    i3 = i + 1;
                    length = i4;
                    fDp2 = fDp2;
                }
                j5 = j;
                canvas.drawText(str, fDp, f, this.textPaint);
                f += fDp2;
                i3 = i + 1;
                length = i4;
                fDp2 = fDp2;
            } else {
                j = j5;
            }
            i = i3;
            str = String.format(Locale.US, "%-16s : n/a", metric2.label);
            j5 = j;
            canvas.drawText(str, fDp, f, this.textPaint);
            f += fDp2;
            i3 = i + 1;
            length = i4;
            fDp2 = fDp2;
        }
        float f2 = fDp2;
        long j8 = j5;
        long jMax = Math.max(j4, Math.max(j3, j2));
        double d6 = d3;
        double d7 = d4;
        double dMax = Math.max(d7, Math.max(d6, d5));
        float f3 = f + f2;
        Locale locale = Locale.US;
        canvas.drawText(String.format(locale, "%-16s : %5.2f / %5.2f ms", "ui", Double.valueOf(j4 / 1000000.0d), Double.valueOf(d7)), fDp, f3, this.textPaint);
        float f4 = f3 + f2;
        canvas.drawText(String.format(locale, "%-16s : %5.2f / %5.2f ms", "rt", Double.valueOf(j3 / 1000000.0d), Double.valueOf(d6)), fDp, f4, this.textPaint);
        float f5 = f4 + f2;
        canvas.drawText(String.format(locale, "%-16s : %5.2f / %5.2f ms", "gpu", Double.valueOf(j2 / 1000000.0d), Double.valueOf(d5)), fDp, f5, this.textPaint);
        float f6 = f5 + f2;
        canvas.drawText(String.format(locale, "%-16s : %5.2f / %5.2f ms", "other", Double.valueOf(j8 / 1000000.0d), Double.valueOf(d2)), fDp, f6, this.textPaint);
        canvas.drawText(String.format(locale, "%-16s : %5.2f / %5.2f ms", "frame", Double.valueOf(jMax / 1000000.0d), Double.valueOf(dMax)), fDp, f6 + f2, this.textPaint);
    }

    /* JADX INFO: renamed from: org.telegram.messenger.utils.FrameMetricsOverlayView$2, reason: invalid class name */
    static /* synthetic */ class AnonymousClass2 {
        static final /* synthetic */ int[] $SwitchMap$org$telegram$messenger$utils$FrameMetricsOverlayView$Metric;

        static {
            int[] iArr = new int[Metric.values().length];
            $SwitchMap$org$telegram$messenger$utils$FrameMetricsOverlayView$Metric = iArr;
            try {
                iArr[Metric.INPUT_HANDLING_DURATION.ordinal()] = 1;
            } catch (NoSuchFieldError unused) {
            }
            try {
                $SwitchMap$org$telegram$messenger$utils$FrameMetricsOverlayView$Metric[Metric.ANIMATION_DURATION.ordinal()] = 2;
            } catch (NoSuchFieldError unused2) {
            }
            try {
                $SwitchMap$org$telegram$messenger$utils$FrameMetricsOverlayView$Metric[Metric.LAYOUT_MEASURE_DURATION.ordinal()] = 3;
            } catch (NoSuchFieldError unused3) {
            }
            try {
                $SwitchMap$org$telegram$messenger$utils$FrameMetricsOverlayView$Metric[Metric.DRAW_DURATION.ordinal()] = 4;
            } catch (NoSuchFieldError unused4) {
            }
            try {
                $SwitchMap$org$telegram$messenger$utils$FrameMetricsOverlayView$Metric[Metric.SYNC_DURATION.ordinal()] = 5;
            } catch (NoSuchFieldError unused5) {
            }
            try {
                $SwitchMap$org$telegram$messenger$utils$FrameMetricsOverlayView$Metric[Metric.COMMAND_ISSUE_DURATION.ordinal()] = 6;
            } catch (NoSuchFieldError unused6) {
            }
            try {
                $SwitchMap$org$telegram$messenger$utils$FrameMetricsOverlayView$Metric[Metric.GPU_DURATION.ordinal()] = 7;
            } catch (NoSuchFieldError unused7) {
            }
            try {
                $SwitchMap$org$telegram$messenger$utils$FrameMetricsOverlayView$Metric[Metric.UNKNOWN_DELAY_DURATION.ordinal()] = 8;
            } catch (NoSuchFieldError unused8) {
            }
            try {
                $SwitchMap$org$telegram$messenger$utils$FrameMetricsOverlayView$Metric[Metric.SWAP_BUFFERS_DURATION.ordinal()] = 9;
            } catch (NoSuchFieldError unused9) {
            }
        }
    }

    @Override // android.view.View
    protected void onMeasure(int i, int i2) {
        setMeasuredDimension(AndroidUtilities.dp(260.0f), (AndroidUtilities.dp(8.0f) * 2) + (AndroidUtilities.dp(14.0f) * (Metric.values().length + 6)));
    }
}
