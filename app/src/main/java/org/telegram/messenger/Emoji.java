package org.telegram.messenger;

import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.ColorFilter;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.text.Spannable;
import android.text.TextPaint;
import android.text.TextUtils;
import android.text.style.ImageSpan;
import android.util.SparseIntArray;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import com.exteragram.messenger.utils.text.LocaleUtils;
import com.exteragram.messenger.utils.ui.FontUtils;
import j$.util.Objects;
import java.io.InputStream;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Locale;
import java.util.Map;
import okhttp3.internal.url._UrlKt;
import okhttp3.internal.ws.WebSocketProtocol;
import org.telegram.tgnet.TLRPC;
import org.telegram.ui.Components.AnimatedEmojiSpan;
import org.telegram.ui.Components.ColoredImageSpan;

public class Emoji {
    private static final String[] DEFAULT_RECENT;
    private static final int MAX_RECENT_EMOJI_COUNT = 48;
    public static int bigImgSize;
    public static int drawImgSize;
    private static SparseIntArray emojiAlphaMasks;
    private static final Bitmap[][] emojiBmp;
    public static final HashMap<String, String> emojiColor;
    private static final int[] emojiCounts;
    public static boolean emojiDrawingUseAlpha;
    public static float emojiDrawingYOffset;
    public static final HashMap<String, Integer> emojiUseHistory;
    public static final Runnable invalidateUiRunnable;
    private static final boolean[][] loadingEmoji;
    public static Paint placeholderPaint;
    public static final ArrayList<String> recentEmoji;
    private static boolean recentEmojiLoaded;
    private static final HashMap<CharSequence, DrawableInfo> rects = new HashMap<>();
    private static boolean inited = false;

    public static abstract class EmojiDrawable extends Drawable {
        public boolean fullSize = false;
        int placeholderColor = 268435456;

        public boolean isLoaded() {
            return false;
        }

        public void preload() {
        }
    }

    static {
        String[][] strArr = EmojiData.data;
        emojiCounts = new int[]{strArr[0].length, strArr[1].length, strArr[2].length, strArr[3].length, strArr[4].length, strArr[5].length, strArr[6].length, strArr[7].length};
        emojiBmp = new Bitmap[8][];
        loadingEmoji = new boolean[8][];
        emojiUseHistory = new HashMap<>();
        recentEmoji = new ArrayList<>();
        emojiColor = new HashMap<>();
        invalidateUiRunnable = new Runnable() { // from class: org.telegram.messenger.Emoji$$ExternalSyntheticLambda2
            @Override // java.lang.Runnable
            public final void run() {
                NotificationCenter.getGlobalInstance().lambda$postNotificationNameOnUIThread$1(NotificationCenter.emojiLoaded, new Object[0]);
            }
        };
        emojiDrawingUseAlpha = true;
        DEFAULT_RECENT = new String[]{"😂", "😘", "❤", "😍", "😊", "😁", "👍", "☺", "😔", "😄", "😭", "💋", "😒", "😳", "😜", "🙈", "😉", "😃", "😢", "😝", "😱", "😡", "😏", "😞", "😅", "😚", "🙊", "😌", "😀", "😋", "😆", "👌", "😐", "😕"};
        drawImgSize = AndroidUtilities.dp(20.0f);
        bigImgSize = AndroidUtilities.dp(AndroidUtilities.isTablet() ? 40.0f : 34.0f);
        int i = 0;
        while (true) {
            Bitmap[][] bitmapArr = emojiBmp;
            if (i >= bitmapArr.length) {
                break;
            }
            int i2 = emojiCounts[i];
            bitmapArr[i] = new Bitmap[i2];
            loadingEmoji[i] = new boolean[i2];
            i++;
        }
        for (int i3 = 0; i3 < EmojiData.data.length; i3++) {
            int i4 = 0;
            while (true) {
                String[] strArr2 = EmojiData.data[i3];
                if (i4 < strArr2.length) {
                    rects.put(strArr2[i4], new DrawableInfo((byte) i3, (short) i4, i4));
                    i4++;
                }
            }
        }
        Paint paint = new Paint();
        placeholderPaint = paint;
        paint.setColor(0);
    }

    public static void preloadEmoji(CharSequence charSequence) {
        DrawableInfo drawableInfo = getDrawableInfo(charSequence);
        if (drawableInfo != null) {
            loadEmoji(drawableInfo.page, drawableInfo.page2);
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static void loadEmoji(final byte b, final short s) {
        if (emojiBmp[b][s] == null) {
            boolean[] zArr = loadingEmoji[b];
            if (zArr[s]) {
                return;
            }
            zArr[s] = true;
            Utilities.globalQueue.postRunnable(new Runnable() { // from class: org.telegram.messenger.Emoji$$ExternalSyntheticLambda3
                @Override // java.lang.Runnable
                public final void run() {
                    Emoji.$r8$lambda$GJCd0wAjo_k4MPoC4ervNAm2qe0(b, s);
                }
            });
        }
    }

    public static /* synthetic */ void $r8$lambda$GJCd0wAjo_k4MPoC4ervNAm2qe0(byte b, short s) {
        StringBuilder sb = new StringBuilder();
        sb.append("emoji/");
        Locale locale = Locale.US;
        sb.append(String.format(locale, "%d_%d.png", Byte.valueOf(b), Short.valueOf(s)));
        Bitmap bitmapLoadBitmap = loadBitmap(sb.toString());
        try {
            if (emojiAlphaMasks == null) {
                emojiAlphaMasks = loadEmojiAlphaMasks();
            }
            SparseIntArray sparseIntArray = emojiAlphaMasks;
            int i = sparseIntArray != null ? sparseIntArray.get((b * 4096) + s, -1) : -1;
            if (bitmapLoadBitmap != null && i != -1) {
                Bitmap bitmapLoadBitmap2 = loadBitmap("emoji/masks/" + String.format(locale, "%d.png", Integer.valueOf(i)));
                if (bitmapLoadBitmap2 != null) {
                    int width = bitmapLoadBitmap.getWidth();
                    int height = bitmapLoadBitmap.getHeight();
                    int i2 = width * height;
                    int[] iArr = new int[i2];
                    int[] iArr2 = new int[i2];
                    bitmapLoadBitmap.getPixels(iArr, 0, width, 0, 0, width, height);
                    bitmapLoadBitmap2.getPixels(iArr2, 0, width, 0, 0, width, height);
                    bitmapLoadBitmap2.recycle();
                    for (int i3 = 0; i3 < i2; i3++) {
                        iArr[i3] = (iArr[i3] & 16777215) | ((iArr2[i3] & 255) << 24);
                    }
                    bitmapLoadBitmap.recycle();
                    Bitmap bitmapCreateBitmap = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888);
                    try {
                        bitmapCreateBitmap.setPixels(iArr, 0, width, 0, 0, width, height);
                        bitmapLoadBitmap = bitmapCreateBitmap;
                    } catch (Exception e) {
                        e = e;
                        bitmapLoadBitmap = bitmapCreateBitmap;
                        FileLog.e(e);
                    }
                }
            }
        } catch (Exception e2) {
            e = e2;
        }
        if (bitmapLoadBitmap != null) {
            emojiBmp[b][s] = bitmapLoadBitmap;
            Runnable runnable = invalidateUiRunnable;
            AndroidUtilities.cancelRunOnUIThread(runnable);
            AndroidUtilities.runOnUIThread(runnable);
        }
        loadingEmoji[b][s] = false;
    }

    private static SparseIntArray loadEmojiAlphaMasks() {
        try {
            InputStream inputStreamOpen = ApplicationLoader.applicationContext.getAssets().open("emoji/metadata.bin");
            try {
                ArrayList arrayList = new ArrayList();
                byte[] bArr = new byte[8192];
                int i = 0;
                while (true) {
                    int i2 = inputStreamOpen.read(bArr);
                    if (i2 == -1) {
                        break;
                    }
                    byte[] bArr2 = new byte[i2];
                    System.arraycopy(bArr, 0, bArr2, 0, i2);
                    arrayList.add(bArr2);
                    i += i2;
                    FileLog.e(e);
                    return null;
                }
                byte[] bArr3 = new byte[i];
                int size = arrayList.size();
                int length = 0;
                int i3 = 0;
                while (i3 < size) {
                    Object obj = arrayList.get(i3);
                    i3++;
                    byte[] bArr4 = (byte[]) obj;
                    System.arraycopy(bArr4, 0, bArr3, length, bArr4.length);
                    length += bArr4.length;
                }
                ByteBuffer byteBufferOrder = ByteBuffer.wrap(bArr3).order(ByteOrder.LITTLE_ENDIAN);
                int i4 = i / 4;
                SparseIntArray sparseIntArray = new SparseIntArray(i4);
                for (int i5 = 0; i5 < i4; i5++) {
                    sparseIntArray.put(byteBufferOrder.getShort() & 65535, 65535 & byteBufferOrder.getShort());
                }
                inputStreamOpen.close();
                return sparseIntArray;
            } catch (Throwable th) {
                if (inputStreamOpen != null) {
                    try {
                        inputStreamOpen.close();
                    } catch (Throwable th2) {
                        th.addSuppressed(th2);
                    }
                }
                throw th;
            }
        } catch (Exception e) {
            FileLog.e(e);
            return null;
        }
    }

    public static Bitmap loadBitmap(String str) {
        Bitmap bitmapDecodeStream;
        try {
            int i = AndroidUtilities.density <= 1.0f ? 2 : 1;
            try {
                InputStream inputStreamOpen = ApplicationLoader.applicationContext.getAssets().open(str);
                BitmapFactory.Options options = new BitmapFactory.Options();
                options.inJustDecodeBounds = false;
                options.inSampleSize = i;
                bitmapDecodeStream = BitmapFactory.decodeStream(inputStreamOpen, null, options);
                try {
                    inputStreamOpen.close();
                    return bitmapDecodeStream;
                } catch (Throwable th) {
                    th = th;
                    FileLog.e(th);
                    return bitmapDecodeStream;
                }
            } catch (Throwable th2) {
                th = th2;
                bitmapDecodeStream = null;
            }
        } catch (Throwable th3) {
            if (BuildVars.LOGS_ENABLED) {
                FileLog.e("Error loading emoji", th3);
            }
            return null;
        }
    }

    public static void invalidateAll(View view) {
        if (view instanceof ViewGroup) {
            ViewGroup viewGroup = (ViewGroup) view;
            for (int i = 0; i < viewGroup.getChildCount(); i++) {
                invalidateAll(viewGroup.getChildAt(i));
            }
            return;
        }
        if (view instanceof TextView) {
            view.invalidate();
        }
    }

    public static String fixEmoji(String str) {
        int length = str.length();
        int i = 0;
        while (i < length) {
            char cCharAt = str.charAt(i);
            if (cCharAt < 55356 || cCharAt > 55358) {
                if (cCharAt == 8419) {
                    return str;
                }
                if (cCharAt >= '#' && cCharAt <= 12953 && EmojiData.emojiToFE0FMap.containsKey(Character.valueOf(cCharAt))) {
                    StringBuilder sb = new StringBuilder();
                    i++;
                    sb.append(str.substring(0, i));
                    sb.append("️");
                    sb.append(str.substring(i));
                    str = sb.toString();
                    length++;
                }
            } else if (cCharAt != 55356 || i >= length - 1) {
                i++;
            } else {
                int i2 = i + 1;
                char cCharAt2 = str.charAt(i2);
                if (cCharAt2 == 56879 || cCharAt2 == 56324 || cCharAt2 == 56858 || cCharAt2 == 56703 || cCharAt2 == 57331 || cCharAt2 == 57131 || cCharAt2 == 56385 || cCharAt2 == 56693 || cCharAt2 == 57292 || cCharAt2 == 57291) {
                    StringBuilder sb2 = new StringBuilder();
                    i += 2;
                    sb2.append(str.substring(0, i));
                    sb2.append("️");
                    sb2.append(str.substring(i));
                    str = sb2.toString();
                    length++;
                } else {
                    i = i2;
                }
            }
            i++;
        }
        return str;
    }

    public static EmojiDrawable getEmojiDrawable(CharSequence charSequence) {
        CompoundEmoji.CompoundEmojiDrawable compoundEmojiDrawable;
        DrawableInfo drawableInfo = getDrawableInfo(charSequence);
        if (drawableInfo != null) {
            SimpleEmojiDrawable simpleEmojiDrawable = new SimpleEmojiDrawable(drawableInfo, endsWithRightArrow(charSequence));
            int i = drawImgSize;
            simpleEmojiDrawable.setBounds(0, 0, i, i);
            return simpleEmojiDrawable;
        }
        if (charSequence == null || (compoundEmojiDrawable = CompoundEmoji.getCompoundEmojiDrawable(charSequence.toString())) == null) {
            return null;
        }
        int i2 = drawImgSize;
        compoundEmojiDrawable.setBounds(0, 0, i2, i2);
        return compoundEmojiDrawable;
    }

    public static boolean endsWithRightArrow(CharSequence charSequence) {
        return charSequence != null && charSequence.length() > 2 && charSequence.charAt(charSequence.length() - 2) == 8205 && charSequence.charAt(charSequence.length() - 1) == 10145;
    }

    private static DrawableInfo getDrawableInfo(CharSequence charSequence) {
        CharSequence charSequence2;
        if (endsWithRightArrow(charSequence)) {
            charSequence = charSequence.subSequence(0, charSequence.length() - 2);
        }
        HashMap<CharSequence, DrawableInfo> map = rects;
        DrawableInfo drawableInfo = map.get(charSequence);
        return (drawableInfo != null || (charSequence2 = EmojiData.emojiAliasMap.get(charSequence)) == null) ? drawableInfo : map.get(charSequence2);
    }

    public static boolean isValidEmoji(CharSequence charSequence) {
        CharSequence charSequence2;
        if (TextUtils.isEmpty(charSequence)) {
            return false;
        }
        HashMap<CharSequence, DrawableInfo> map = rects;
        DrawableInfo drawableInfo = map.get(charSequence);
        if (drawableInfo == null && (charSequence2 = EmojiData.emojiAliasMap.get(charSequence)) != null) {
            drawableInfo = map.get(charSequence2);
        }
        return drawableInfo != null;
    }

    public static Drawable getEmojiBigDrawable(String str) {
        CharSequence charSequence;
        EmojiDrawable compoundEmojiDrawable = CompoundEmoji.getCompoundEmojiDrawable(str);
        if (compoundEmojiDrawable != null) {
            int i = drawImgSize;
            compoundEmojiDrawable.setBounds(0, 0, i, i);
        } else {
            compoundEmojiDrawable = null;
        }
        if (compoundEmojiDrawable == null) {
            compoundEmojiDrawable = getEmojiDrawable(str);
        }
        if (compoundEmojiDrawable == null && (charSequence = EmojiData.emojiAliasMap.get(str)) != null) {
            compoundEmojiDrawable = getEmojiDrawable(charSequence);
        }
        if (compoundEmojiDrawable == null) {
            return null;
        }
        int i2 = bigImgSize;
        compoundEmojiDrawable.setBounds(0, 0, i2, i2);
        compoundEmojiDrawable.fullSize = true;
        return compoundEmojiDrawable;
    }

    public static class SimpleEmojiDrawable extends EmojiDrawable {
        private static Paint paint = new Paint(2);
        private static Rect rect = new Rect();
        private static final TextPaint textPaint = new TextPaint(1);
        private DrawableInfo info;
        private boolean invert;

        @Override // android.graphics.drawable.Drawable
        public int getOpacity() {
            return -2;
        }

        @Override // android.graphics.drawable.Drawable
        public void setColorFilter(ColorFilter colorFilter) {
        }

        public SimpleEmojiDrawable(DrawableInfo drawableInfo, boolean z) {
            this.info = drawableInfo;
            this.invert = z;
        }

        public DrawableInfo getDrawableInfo() {
            return this.info;
        }

        public Rect getDrawRect() {
            Rect bounds = getBounds();
            int iCenterX = bounds.centerX();
            int iCenterY = bounds.centerY();
            Rect rect2 = rect;
            boolean z = this.fullSize;
            rect2.left = iCenterX - ((z ? Emoji.bigImgSize : Emoji.drawImgSize) / 2);
            rect.right = iCenterX + ((z ? Emoji.bigImgSize : Emoji.drawImgSize) / 2);
            rect.top = iCenterY - ((z ? Emoji.bigImgSize : Emoji.drawImgSize) / 2);
            rect.bottom = iCenterY + ((z ? Emoji.bigImgSize : Emoji.drawImgSize) / 2);
            return rect;
        }

        @Override // android.graphics.drawable.Drawable
        public void draw(Canvas canvas) {
            Rect bounds;
            if (!SharedConfig.useSystemEmoji && !isLoaded()) {
                DrawableInfo drawableInfo = this.info;
                Emoji.loadEmoji(drawableInfo.page, drawableInfo.page2);
                Emoji.placeholderPaint.setColor(this.placeholderColor);
                Rect bounds2 = getBounds();
                canvas.drawCircle(bounds2.centerX(), bounds2.centerY(), bounds2.width() * 0.4f, Emoji.placeholderPaint);
                return;
            }
            if (this.fullSize) {
                bounds = getDrawRect();
            } else {
                bounds = getBounds();
            }
            Rect rect2 = bounds;
            if (SharedConfig.useSystemEmoji) {
                String[][] strArr = EmojiData.data;
                DrawableInfo drawableInfo2 = this.info;
                String strFixEmoji = Emoji.fixEmoji(strArr[drawableInfo2.page][drawableInfo2.emojiIndex]);
                TextPaint textPaint2 = textPaint;
                textPaint2.setTextSize(rect2.height() * 0.8f);
                textPaint2.setTypeface(FontUtils.getSystemEmojiTypeface());
                canvas.drawText(strFixEmoji, 0, strFixEmoji.length(), rect2.left, rect2.bottom - (rect2.height() * 0.225f), (Paint) textPaint2);
                return;
            }
            if (canvas.quickReject(rect2.left, rect2.top, rect2.right, rect2.bottom, Canvas.EdgeType.AA)) {
                return;
            }
            if (this.invert) {
                canvas.save();
                canvas.scale(-1.0f, 1.0f, rect2.centerX(), rect2.centerY());
            }
            Bitmap[][] bitmapArr = Emoji.emojiBmp;
            DrawableInfo drawableInfo3 = this.info;
            canvas.drawBitmap(bitmapArr[drawableInfo3.page][drawableInfo3.page2], (Rect) null, rect2, paint);
            if (this.invert) {
                canvas.restore();
            }
        }

        @Override // android.graphics.drawable.Drawable
        public void setAlpha(int i) {
            paint.setAlpha(i);
        }

        @Override // org.telegram.messenger.Emoji.EmojiDrawable
        public boolean isLoaded() {
            Bitmap[][] bitmapArr = Emoji.emojiBmp;
            DrawableInfo drawableInfo = this.info;
            return bitmapArr[drawableInfo.page][drawableInfo.page2] != null;
        }

        @Override // org.telegram.messenger.Emoji.EmojiDrawable
        public void preload() {
            if (isLoaded()) {
                return;
            }
            DrawableInfo drawableInfo = this.info;
            Emoji.loadEmoji(drawableInfo.page, drawableInfo.page2);
        }
    }

    private static class DrawableInfo {
        public int emojiIndex;
        public byte page;
        public short page2;

        public DrawableInfo(byte b, short s, int i) {
            this.page = b;
            this.page2 = s;
            this.emojiIndex = i;
        }
    }

    public static class EmojiSpanRange {
        public CharSequence code;
        public int end;
        public int start;

        public EmojiSpanRange(int i, int i2, CharSequence charSequence) {
            this.start = i;
            this.end = i2;
            this.code = charSequence;
        }
    }

    public static boolean fullyConsistsOfEmojis(CharSequence charSequence) {
        int[] iArr = new int[1];
        parseEmojis(charSequence, iArr);
        return iArr[0] > 0;
    }

    public static ArrayList<EmojiSpanRange> parseEmojis(CharSequence charSequence) {
        return parseEmojis(charSequence, null);
    }

    /* JADX WARN: Code duplicated, block: B:118:0x019e  */
    /* JADX WARN: Code duplicated, block: B:122:0x01a6 A[Catch: Exception -> 0x0078, TryCatch #0 {Exception -> 0x0078, blocks: (B:10:0x002e, B:28:0x006c, B:87:0x011c, B:89:0x0120, B:91:0x012d, B:95:0x013b, B:122:0x01a6, B:124:0x01aa, B:128:0x01b7, B:130:0x01bd, B:167:0x021a, B:146:0x01ec, B:148:0x01f0, B:158:0x0205, B:160:0x0209, B:171:0x0225, B:173:0x022d, B:175:0x0231, B:177:0x023c, B:181:0x024a, B:184:0x025a, B:186:0x0266, B:188:0x0269, B:189:0x027a, B:96:0x0148, B:98:0x014f, B:100:0x015b, B:104:0x016b, B:106:0x0171, B:107:0x0178, B:109:0x0180, B:110:0x0187, B:112:0x0191, B:116:0x019a, B:16:0x0042, B:18:0x004d, B:32:0x007b, B:43:0x0098, B:41:0x008f, B:48:0x00a8, B:56:0x00bc, B:76:0x00fc, B:67:0x00de, B:74:0x00f4), top: B:198:0x002e }] */
    /* JADX WARN: Code duplicated, block: B:124:0x01aa A[Catch: Exception -> 0x0078, TryCatch #0 {Exception -> 0x0078, blocks: (B:10:0x002e, B:28:0x006c, B:87:0x011c, B:89:0x0120, B:91:0x012d, B:95:0x013b, B:122:0x01a6, B:124:0x01aa, B:128:0x01b7, B:130:0x01bd, B:167:0x021a, B:146:0x01ec, B:148:0x01f0, B:158:0x0205, B:160:0x0209, B:171:0x0225, B:173:0x022d, B:175:0x0231, B:177:0x023c, B:181:0x024a, B:184:0x025a, B:186:0x0266, B:188:0x0269, B:189:0x027a, B:96:0x0148, B:98:0x014f, B:100:0x015b, B:104:0x016b, B:106:0x0171, B:107:0x0178, B:109:0x0180, B:110:0x0187, B:112:0x0191, B:116:0x019a, B:16:0x0042, B:18:0x004d, B:32:0x007b, B:43:0x0098, B:41:0x008f, B:48:0x00a8, B:56:0x00bc, B:76:0x00fc, B:67:0x00de, B:74:0x00f4), top: B:198:0x002e }] */
    /* JADX WARN: Code duplicated, block: B:126:0x01b3  */
    /* JADX WARN: Code duplicated, block: B:128:0x01b7 A[Catch: Exception -> 0x0078, TryCatch #0 {Exception -> 0x0078, blocks: (B:10:0x002e, B:28:0x006c, B:87:0x011c, B:89:0x0120, B:91:0x012d, B:95:0x013b, B:122:0x01a6, B:124:0x01aa, B:128:0x01b7, B:130:0x01bd, B:167:0x021a, B:146:0x01ec, B:148:0x01f0, B:158:0x0205, B:160:0x0209, B:171:0x0225, B:173:0x022d, B:175:0x0231, B:177:0x023c, B:181:0x024a, B:184:0x025a, B:186:0x0266, B:188:0x0269, B:189:0x027a, B:96:0x0148, B:98:0x014f, B:100:0x015b, B:104:0x016b, B:106:0x0171, B:107:0x0178, B:109:0x0180, B:110:0x0187, B:112:0x0191, B:116:0x019a, B:16:0x0042, B:18:0x004d, B:32:0x007b, B:43:0x0098, B:41:0x008f, B:48:0x00a8, B:56:0x00bc, B:76:0x00fc, B:67:0x00de, B:74:0x00f4), top: B:198:0x002e }] */
    /* JADX WARN: Code duplicated, block: B:131:0x01cb  */
    /* JADX WARN: Code duplicated, block: B:133:0x01cf  */
    /* JADX WARN: Code duplicated, block: B:139:0x01db  */
    /* JADX WARN: Code duplicated, block: B:156:0x0200  */
    /* JADX WARN: Code duplicated, block: B:158:0x0205 A[Catch: Exception -> 0x0078, TryCatch #0 {Exception -> 0x0078, blocks: (B:10:0x002e, B:28:0x006c, B:87:0x011c, B:89:0x0120, B:91:0x012d, B:95:0x013b, B:122:0x01a6, B:124:0x01aa, B:128:0x01b7, B:130:0x01bd, B:167:0x021a, B:146:0x01ec, B:148:0x01f0, B:158:0x0205, B:160:0x0209, B:171:0x0225, B:173:0x022d, B:175:0x0231, B:177:0x023c, B:181:0x024a, B:184:0x025a, B:186:0x0266, B:188:0x0269, B:189:0x027a, B:96:0x0148, B:98:0x014f, B:100:0x015b, B:104:0x016b, B:106:0x0171, B:107:0x0178, B:109:0x0180, B:110:0x0187, B:112:0x0191, B:116:0x019a, B:16:0x0042, B:18:0x004d, B:32:0x007b, B:43:0x0098, B:41:0x008f, B:48:0x00a8, B:56:0x00bc, B:76:0x00fc, B:67:0x00de, B:74:0x00f4), top: B:198:0x002e }] */
    /* JADX WARN: Code duplicated, block: B:160:0x0209 A[Catch: Exception -> 0x0078, TryCatch #0 {Exception -> 0x0078, blocks: (B:10:0x002e, B:28:0x006c, B:87:0x011c, B:89:0x0120, B:91:0x012d, B:95:0x013b, B:122:0x01a6, B:124:0x01aa, B:128:0x01b7, B:130:0x01bd, B:167:0x021a, B:146:0x01ec, B:148:0x01f0, B:158:0x0205, B:160:0x0209, B:171:0x0225, B:173:0x022d, B:175:0x0231, B:177:0x023c, B:181:0x024a, B:184:0x025a, B:186:0x0266, B:188:0x0269, B:189:0x027a, B:96:0x0148, B:98:0x014f, B:100:0x015b, B:104:0x016b, B:106:0x0171, B:107:0x0178, B:109:0x0180, B:110:0x0187, B:112:0x0191, B:116:0x019a, B:16:0x0042, B:18:0x004d, B:32:0x007b, B:43:0x0098, B:41:0x008f, B:48:0x00a8, B:56:0x00bc, B:76:0x00fc, B:67:0x00de, B:74:0x00f4), top: B:198:0x002e }] */
    /* JADX WARN: Code duplicated, block: B:162:0x020d  */
    /* JADX WARN: Code duplicated, block: B:163:0x020f  */
    /* JADX WARN: Code duplicated, block: B:166:0x0217  */
    /* JADX WARN: Code duplicated, block: B:170:0x0223 A[ADDED_TO_REGION] */
    /* JADX WARN: Code duplicated, block: B:173:0x022d A[Catch: Exception -> 0x0078, TryCatch #0 {Exception -> 0x0078, blocks: (B:10:0x002e, B:28:0x006c, B:87:0x011c, B:89:0x0120, B:91:0x012d, B:95:0x013b, B:122:0x01a6, B:124:0x01aa, B:128:0x01b7, B:130:0x01bd, B:167:0x021a, B:146:0x01ec, B:148:0x01f0, B:158:0x0205, B:160:0x0209, B:171:0x0225, B:173:0x022d, B:175:0x0231, B:177:0x023c, B:181:0x024a, B:184:0x025a, B:186:0x0266, B:188:0x0269, B:189:0x027a, B:96:0x0148, B:98:0x014f, B:100:0x015b, B:104:0x016b, B:106:0x0171, B:107:0x0178, B:109:0x0180, B:110:0x0187, B:112:0x0191, B:116:0x019a, B:16:0x0042, B:18:0x004d, B:32:0x007b, B:43:0x0098, B:41:0x008f, B:48:0x00a8, B:56:0x00bc, B:76:0x00fc, B:67:0x00de, B:74:0x00f4), top: B:198:0x002e }] */
    /* JADX WARN: Code duplicated, block: B:177:0x023c A[Catch: Exception -> 0x0078, TryCatch #0 {Exception -> 0x0078, blocks: (B:10:0x002e, B:28:0x006c, B:87:0x011c, B:89:0x0120, B:91:0x012d, B:95:0x013b, B:122:0x01a6, B:124:0x01aa, B:128:0x01b7, B:130:0x01bd, B:167:0x021a, B:146:0x01ec, B:148:0x01f0, B:158:0x0205, B:160:0x0209, B:171:0x0225, B:173:0x022d, B:175:0x0231, B:177:0x023c, B:181:0x024a, B:184:0x025a, B:186:0x0266, B:188:0x0269, B:189:0x027a, B:96:0x0148, B:98:0x014f, B:100:0x015b, B:104:0x016b, B:106:0x0171, B:107:0x0178, B:109:0x0180, B:110:0x0187, B:112:0x0191, B:116:0x019a, B:16:0x0042, B:18:0x004d, B:32:0x007b, B:43:0x0098, B:41:0x008f, B:48:0x00a8, B:56:0x00bc, B:76:0x00fc, B:67:0x00de, B:74:0x00f4), top: B:198:0x002e }] */
    /* JADX WARN: Code duplicated, block: B:183:0x0258 A[DONT_INVERT] */
    /* JADX WARN: Code duplicated, block: B:184:0x025a A[Catch: Exception -> 0x0078, TryCatch #0 {Exception -> 0x0078, blocks: (B:10:0x002e, B:28:0x006c, B:87:0x011c, B:89:0x0120, B:91:0x012d, B:95:0x013b, B:122:0x01a6, B:124:0x01aa, B:128:0x01b7, B:130:0x01bd, B:167:0x021a, B:146:0x01ec, B:148:0x01f0, B:158:0x0205, B:160:0x0209, B:171:0x0225, B:173:0x022d, B:175:0x0231, B:177:0x023c, B:181:0x024a, B:184:0x025a, B:186:0x0266, B:188:0x0269, B:189:0x027a, B:96:0x0148, B:98:0x014f, B:100:0x015b, B:104:0x016b, B:106:0x0171, B:107:0x0178, B:109:0x0180, B:110:0x0187, B:112:0x0191, B:116:0x019a, B:16:0x0042, B:18:0x004d, B:32:0x007b, B:43:0x0098, B:41:0x008f, B:48:0x00a8, B:56:0x00bc, B:76:0x00fc, B:67:0x00de, B:74:0x00f4), top: B:198:0x002e }] */
    /* JADX WARN: Code duplicated, block: B:186:0x0266 A[Catch: Exception -> 0x0078, TryCatch #0 {Exception -> 0x0078, blocks: (B:10:0x002e, B:28:0x006c, B:87:0x011c, B:89:0x0120, B:91:0x012d, B:95:0x013b, B:122:0x01a6, B:124:0x01aa, B:128:0x01b7, B:130:0x01bd, B:167:0x021a, B:146:0x01ec, B:148:0x01f0, B:158:0x0205, B:160:0x0209, B:171:0x0225, B:173:0x022d, B:175:0x0231, B:177:0x023c, B:181:0x024a, B:184:0x025a, B:186:0x0266, B:188:0x0269, B:189:0x027a, B:96:0x0148, B:98:0x014f, B:100:0x015b, B:104:0x016b, B:106:0x0171, B:107:0x0178, B:109:0x0180, B:110:0x0187, B:112:0x0191, B:116:0x019a, B:16:0x0042, B:18:0x004d, B:32:0x007b, B:43:0x0098, B:41:0x008f, B:48:0x00a8, B:56:0x00bc, B:76:0x00fc, B:67:0x00de, B:74:0x00f4), top: B:198:0x002e }] */
    /* JADX WARN: Code duplicated, block: B:202:0x0283 A[SYNTHETIC] */
    /* JADX WARN: Code duplicated, block: B:207:0x021a A[SYNTHETIC] */
    /* JADX WARN: Code duplicated, block: B:87:0x011c A[Catch: Exception -> 0x0078, TryCatch #0 {Exception -> 0x0078, blocks: (B:10:0x002e, B:28:0x006c, B:87:0x011c, B:89:0x0120, B:91:0x012d, B:95:0x013b, B:122:0x01a6, B:124:0x01aa, B:128:0x01b7, B:130:0x01bd, B:167:0x021a, B:146:0x01ec, B:148:0x01f0, B:158:0x0205, B:160:0x0209, B:171:0x0225, B:173:0x022d, B:175:0x0231, B:177:0x023c, B:181:0x024a, B:184:0x025a, B:186:0x0266, B:188:0x0269, B:189:0x027a, B:96:0x0148, B:98:0x014f, B:100:0x015b, B:104:0x016b, B:106:0x0171, B:107:0x0178, B:109:0x0180, B:110:0x0187, B:112:0x0191, B:116:0x019a, B:16:0x0042, B:18:0x004d, B:32:0x007b, B:43:0x0098, B:41:0x008f, B:48:0x00a8, B:56:0x00bc, B:76:0x00fc, B:67:0x00de, B:74:0x00f4), top: B:198:0x002e }] */
    public static ArrayList<EmojiSpanRange> parseEmojis(CharSequence charSequence, int[] iArr) {
        boolean z;
        char cCharAt;
        boolean z2;
        int i;
        int i2;
        int i3;
        int i4;
        int i5;
        int i6;
        char cCharAt2;
        int i7;
        int i8;
        char cCharAt3;
        boolean z3;
        int i9;
        ArrayList<EmojiSpanRange> arrayList = new ArrayList<>();
        if (charSequence != null && charSequence.length() > 0) {
            StringBuilder sb = new StringBuilder(16);
            int length = charSequence.length();
            int i10 = -1;
            int[] iArr2 = iArr;
            int i11 = -1;
            int i12 = 0;
            int i13 = 0;
            long j = 0;
            int i14 = 0;
            boolean z4 = false;
            boolean z5 = false;
            while (i13 < length) {
                try {
                    char cCharAt4 = charSequence.charAt(i13);
                    if ((cCharAt4 >= 55356 && cCharAt4 <= 55358) || (j != 0 && (j & (-4294967296L)) == 0 && (j & WebSocketProtocol.PAYLOAD_SHORT_MAX) == 55356 && cCharAt4 >= 56806 && cCharAt4 <= 56831)) {
                        if (i11 == i10) {
                            i11 = i13;
                        } else if (z4) {
                            i11 = i13;
                            i14 = 0;
                            z4 = false;
                        }
                        sb.append(cCharAt4);
                        i14++;
                        j = (j << 16) | ((long) cCharAt4);
                    } else {
                        if ((sb.length() > 0 && (cCharAt4 == 9792 || cCharAt4 == 9794 || cCharAt4 == 9877)) || (j > 0 && (61440 & cCharAt4) == 53248)) {
                            sb.append(cCharAt4);
                            i14++;
                            j = 0;
                        } else if (cCharAt4 != 8419) {
                            if (cCharAt4 == 169 || cCharAt4 == 174 || (cCharAt4 >= 8252 && cCharAt4 <= 12953)) {
                                if (EmojiData.dataCharsMap.containsKey(Character.valueOf(cCharAt4))) {
                                    if (i11 == -1) {
                                        i11 = i13;
                                    } else if (z4) {
                                        i11 = i13;
                                        i14 = 0;
                                        z4 = false;
                                    }
                                    i14++;
                                    sb.append(cCharAt4);
                                }
                                if (z5 || (i9 = i13 + 2) >= length) {
                                    z2 = z;
                                } else {
                                    int i15 = i13 + 1;
                                    char cCharAt5 = charSequence.charAt(i15);
                                    z2 = z;
                                    if (cCharAt5 == 55356) {
                                        char cCharAt6 = charSequence.charAt(i9);
                                        if (cCharAt6 >= 57339 && cCharAt6 <= 57343) {
                                            sb.append(charSequence.subSequence(i15, i13 + 3));
                                            i14 += 2;
                                            i = i9;
                                        }
                                        i2 = i;
                                        i3 = 0;
                                        while (i3 < 3) {
                                            i7 = i2 + 1;
                                            if (i7 < length) {
                                                cCharAt3 = charSequence.charAt(i7);
                                                i8 = i;
                                                if (i3 == 1) {
                                                    if (cCharAt3 != 8205 && sb.length() > 0) {
                                                        sb.append(cCharAt3);
                                                        i14++;
                                                        i2 = i7;
                                                        z5 = false;
                                                        z2 = false;
                                                    }
                                                } else if (cCharAt4 != '*' || cCharAt4 == '#' || (cCharAt4 >= '0' && cCharAt4 <= '9')) {
                                                    if (cCharAt3 >= 65024) {
                                                        if (cCharAt3 <= 65039) {
                                                            i14++;
                                                            if (!z5) {
                                                                if (i2 + 2 >= length) {
                                                                    z3 = true;
                                                                } else {
                                                                    z3 = false;
                                                                }
                                                                z5 = z3;
                                                            }
                                                            i2 = i7;
                                                            i11 = i8;
                                                            z4 = true;
                                                        }
                                                    }
                                                } else if (i11 != -1 && cCharAt3 >= 65024) {
                                                    if (cCharAt3 <= 65039) {
                                                        i14++;
                                                        if (!z5) {
                                                            z5 = i2 + 2 >= length;
                                                        }
                                                        i2 = i7;
                                                    }
                                                }
                                                i3++;
                                                i = i8;
                                            } else {
                                                i8 = i;
                                            }
                                            i3++;
                                            i = i8;
                                        }
                                        int i16 = i;
                                        if (z2 && iArr2 != null) {
                                            iArr2[0] = 0;
                                            iArr2 = null;
                                        }
                                        if (z5 && (i5 = i2 + 2) < length) {
                                            i6 = i2 + 1;
                                            if (charSequence.charAt(i6) == 55356 && (cCharAt2 = charSequence.charAt(i5)) >= 57339 && cCharAt2 <= 57343) {
                                                sb.append(charSequence.subSequence(i6, i2 + 3));
                                                i14 += 2;
                                                i2 = i5;
                                            }
                                        }
                                        if (!z5) {
                                            if (iArr2 != null) {
                                                iArr2[0] = iArr2[0] + 1;
                                            }
                                            if (i11 >= 0 && (i4 = i14 + i11) <= length) {
                                                arrayList.add(new EmojiSpanRange(i11, i4, sb.subSequence(0, sb.length())));
                                            }
                                            sb.setLength(0);
                                            i11 = -1;
                                            i14 = 0;
                                            z4 = false;
                                            z5 = false;
                                        }
                                        i13 = i2 + 1;
                                        i12 = i16;
                                        i10 = -1;
                                    } else if (sb.length() >= 2 && sb.charAt(0) == 55356 && sb.charAt(1) == 57332 && cCharAt5 == 56128) {
                                        while (true) {
                                            if (i15 < charSequence.length()) {
                                                sb.append(charSequence.charAt(i15));
                                            }
                                            int i17 = i15 + 1;
                                            if (i17 < charSequence.length()) {
                                                sb.append(charSequence.charAt(i17));
                                            }
                                            i14 += 2;
                                            int i18 = i15 + 2;
                                            if (i18 >= charSequence.length() || charSequence.charAt(i18) != 56128) {
                                                break;
                                                break;
                                            }
                                            i15 = i18;
                                        }
                                        i13 = i15 + 1;
                                    }
                                }
                                i = i13;
                                i2 = i;
                                i3 = 0;
                                while (i3 < 3) {
                                    i7 = i2 + 1;
                                    if (i7 < length) {
                                        cCharAt3 = charSequence.charAt(i7);
                                        i8 = i;
                                        if (i3 == 1) {
                                            if (cCharAt3 != 8205) {
                                            }
                                        } else if (cCharAt4 != '*') {
                                            if (cCharAt3 >= 65024) {
                                                if (cCharAt3 <= 65039) {
                                                    i14++;
                                                    if (!z5) {
                                                        if (i2 + 2 >= length) {
                                                            z3 = true;
                                                        } else {
                                                            z3 = false;
                                                        }
                                                        z5 = z3;
                                                    }
                                                    i2 = i7;
                                                    i11 = i8;
                                                    z4 = true;
                                                }
                                            }
                                        } else if (cCharAt3 >= 65024) {
                                            if (cCharAt3 <= 65039) {
                                                i14++;
                                                if (!z5) {
                                                    if (i2 + 2 >= length) {
                                                        z3 = true;
                                                    } else {
                                                        z3 = false;
                                                    }
                                                    z5 = z3;
                                                }
                                                i2 = i7;
                                                i11 = i8;
                                                z4 = true;
                                            }
                                        }
                                        i3++;
                                        i = i8;
                                    } else {
                                        i8 = i;
                                    }
                                    i3++;
                                    i = i8;
                                }
                                int i19 = i;
                                if (z2) {
                                    iArr2[0] = 0;
                                    iArr2 = null;
                                }
                                if (z5) {
                                    i6 = i2 + 1;
                                    if (charSequence.charAt(i6) == 55356) {
                                        sb.append(charSequence.subSequence(i6, i2 + 3));
                                        i14 += 2;
                                        i2 = i5;
                                    }
                                }
                                if (!z5) {
                                    if (iArr2 != null) {
                                        iArr2[0] = iArr2[0] + 1;
                                    }
                                    if (i11 >= 0) {
                                        arrayList.add(new EmojiSpanRange(i11, i4, sb.subSequence(0, sb.length())));
                                    }
                                    sb.setLength(0);
                                    i11 = -1;
                                    i14 = 0;
                                    z4 = false;
                                    z5 = false;
                                }
                                i13 = i2 + 1;
                                i12 = i19;
                                i10 = -1;
                            }
                            if (i11 != -1) {
                                sb.setLength(0);
                                z = false;
                                i11 = -1;
                                i14 = 0;
                                z4 = false;
                                z5 = false;
                            } else if (cCharAt4 != 65039 && cCharAt4 != '\n' && cCharAt4 != ' ' && cCharAt4 != '\t') {
                                z = true;
                            }
                            if (z5) {
                                z2 = z;
                                i = i13;
                            } else {
                                z2 = z;
                                i = i13;
                            }
                            i2 = i;
                            i3 = 0;
                            while (i3 < 3) {
                                i7 = i2 + 1;
                                if (i7 < length) {
                                    cCharAt3 = charSequence.charAt(i7);
                                    i8 = i;
                                    if (i3 == 1) {
                                        if (cCharAt3 != 8205) {
                                        }
                                    } else if (cCharAt4 != '*') {
                                        if (cCharAt3 >= 65024) {
                                            if (cCharAt3 <= 65039) {
                                                i14++;
                                                if (!z5) {
                                                    if (i2 + 2 >= length) {
                                                        z3 = true;
                                                    } else {
                                                        z3 = false;
                                                    }
                                                    z5 = z3;
                                                }
                                                i2 = i7;
                                                i11 = i8;
                                                z4 = true;
                                            }
                                        }
                                    } else if (cCharAt3 >= 65024) {
                                        if (cCharAt3 <= 65039) {
                                            i14++;
                                            if (!z5) {
                                                if (i2 + 2 >= length) {
                                                    z3 = true;
                                                } else {
                                                    z3 = false;
                                                }
                                                z5 = z3;
                                            }
                                            i2 = i7;
                                            i11 = i8;
                                            z4 = true;
                                        }
                                    }
                                    i3++;
                                    i = i8;
                                } else {
                                    i8 = i;
                                }
                                i3++;
                                i = i8;
                            }
                            int i110 = i;
                            if (z2) {
                                iArr2[0] = 0;
                                iArr2 = null;
                            }
                            if (z5) {
                                i6 = i2 + 1;
                                if (charSequence.charAt(i6) == 55356) {
                                    sb.append(charSequence.subSequence(i6, i2 + 3));
                                    i14 += 2;
                                    i2 = i5;
                                }
                            }
                            if (!z5) {
                                if (iArr2 != null) {
                                    iArr2[0] = iArr2[0] + 1;
                                }
                                if (i11 >= 0) {
                                    arrayList.add(new EmojiSpanRange(i11, i4, sb.subSequence(0, sb.length())));
                                }
                                sb.setLength(0);
                                i11 = -1;
                                i14 = 0;
                                z4 = false;
                                z5 = false;
                            }
                            i13 = i2 + 1;
                            i12 = i110;
                            i10 = -1;
                        } else if (i13 > 0 && (((cCharAt = charSequence.charAt(i12)) >= '0' && cCharAt <= '9') || cCharAt == '#' || cCharAt == '*')) {
                            i14 = (i13 - i12) + 1;
                            sb.append(cCharAt);
                            sb.append(cCharAt4);
                            i11 = i12;
                            z5 = true;
                            z4 = false;
                        }
                        z5 = true;
                    }
                    z = false;
                    if (z5) {
                        z2 = z;
                        i = i13;
                    } else {
                        z2 = z;
                        i = i13;
                    }
                    i2 = i;
                    i3 = 0;
                    while (i3 < 3) {
                        i7 = i2 + 1;
                        if (i7 < length) {
                            cCharAt3 = charSequence.charAt(i7);
                            i8 = i;
                            if (i3 == 1) {
                                if (cCharAt3 != 8205) {
                                }
                            } else if (cCharAt4 != '*') {
                                if (cCharAt3 >= 65024) {
                                    if (cCharAt3 <= 65039) {
                                        i14++;
                                        if (!z5) {
                                            if (i2 + 2 >= length) {
                                                z3 = true;
                                            } else {
                                                z3 = false;
                                            }
                                            z5 = z3;
                                        }
                                        i2 = i7;
                                        i11 = i8;
                                        z4 = true;
                                    }
                                }
                            } else if (cCharAt3 >= 65024) {
                                if (cCharAt3 <= 65039) {
                                    i14++;
                                    if (!z5) {
                                        if (i2 + 2 >= length) {
                                            z3 = true;
                                        } else {
                                            z3 = false;
                                        }
                                        z5 = z3;
                                    }
                                    i2 = i7;
                                    i11 = i8;
                                    z4 = true;
                                }
                            }
                            i3++;
                            i = i8;
                        } else {
                            i8 = i;
                        }
                        i3++;
                        i = i8;
                    }
                    int i111 = i;
                    if (z2) {
                        iArr2[0] = 0;
                        iArr2 = null;
                    }
                    if (z5) {
                        i6 = i2 + 1;
                        if (charSequence.charAt(i6) == 55356) {
                            sb.append(charSequence.subSequence(i6, i2 + 3));
                            i14 += 2;
                            i2 = i5;
                        }
                    }
                    if (!z5) {
                        if (iArr2 != null) {
                            iArr2[0] = iArr2[0] + 1;
                        }
                        if (i11 >= 0) {
                            arrayList.add(new EmojiSpanRange(i11, i4, sb.subSequence(0, sb.length())));
                        }
                        sb.setLength(0);
                        i11 = -1;
                        i14 = 0;
                        z4 = false;
                        z5 = false;
                    }
                    i13 = i2 + 1;
                    i12 = i111;
                    i10 = -1;
                } catch (Exception e) {
                    FileLog.e(e);
                }
            }
            if (iArr2 != null && sb.length() != 0) {
                iArr2[0] = 0;
            }
        }
        return arrayList;
    }

    public static CharSequence replaceEmoji(CharSequence charSequence, Paint.FontMetricsInt fontMetricsInt, boolean z) {
        return replaceEmoji(charSequence, fontMetricsInt, z, (int[]) null);
    }

    public static CharSequence replaceEmoji(CharSequence charSequence, Paint.FontMetricsInt fontMetricsInt, boolean z, float f) {
        return replaceEmoji(charSequence, fontMetricsInt, z, null, 0, f, 0);
    }

    public static CharSequence replaceEmoji(CharSequence charSequence, Paint.FontMetricsInt fontMetricsInt, boolean z, int[] iArr) {
        return replaceEmoji(charSequence, fontMetricsInt, z, iArr, 0);
    }

    public static CharSequence replaceEmoji(CharSequence charSequence, Paint.FontMetricsInt fontMetricsInt, boolean z, int[] iArr, int i) {
        return replaceEmoji(charSequence, fontMetricsInt, z, iArr, i, 1.0f, 0);
    }

    /* JADX WARN: Code duplicated, block: B:44:0x008e A[Catch: Exception -> 0x0080, TryCatch #0 {Exception -> 0x0080, blocks: (B:22:0x005c, B:24:0x0064, B:26:0x0067, B:28:0x006b, B:30:0x006f, B:32:0x0077, B:37:0x0082, B:39:0x0087, B:42:0x008b, B:44:0x008e, B:46:0x0092, B:48:0x009a, B:51:0x00a3, B:52:0x00a6, B:54:0x00ae, B:58:0x00bd, B:57:0x00b9), top: B:69:0x005c }] */
    /* JADX WARN: Code duplicated, block: B:54:0x00ae A[Catch: Exception -> 0x0080, TryCatch #0 {Exception -> 0x0080, blocks: (B:22:0x005c, B:24:0x0064, B:26:0x0067, B:28:0x006b, B:30:0x006f, B:32:0x0077, B:37:0x0082, B:39:0x0087, B:42:0x008b, B:44:0x008e, B:46:0x0092, B:48:0x009a, B:51:0x00a3, B:52:0x00a6, B:54:0x00ae, B:58:0x00bd, B:57:0x00b9), top: B:69:0x005c }] */
    /* JADX WARN: Code duplicated, block: B:56:0x00b7  */
    /* JADX WARN: Code duplicated, block: B:57:0x00b9 A[Catch: Exception -> 0x0080, TryCatch #0 {Exception -> 0x0080, blocks: (B:22:0x005c, B:24:0x0064, B:26:0x0067, B:28:0x006b, B:30:0x006f, B:32:0x0077, B:37:0x0082, B:39:0x0087, B:42:0x008b, B:44:0x008e, B:46:0x0092, B:48:0x009a, B:51:0x00a3, B:52:0x00a6, B:54:0x00ae, B:58:0x00bd, B:57:0x00b9), top: B:69:0x005c }] */
    /* JADX WARN: Code duplicated, block: B:83:0x00a6 A[SYNTHETIC] */
    public static CharSequence replaceEmoji(CharSequence charSequence, Paint.FontMetricsInt fontMetricsInt, boolean z, int[] iArr, int i, float f, int i2) {
        Spannable spannableNewSpannable;
        EmojiDrawable emojiDrawable;
        CharSequence charSequence2;
        String string;
        int i3;
        ColoredImageSpan coloredImageSpan;
        if (charSequence == null || charSequence.length() == 0) {
            return charSequence;
        }
        if (!z && (charSequence instanceof Spannable)) {
            spannableNewSpannable = (Spannable) charSequence;
        } else {
            spannableNewSpannable = Spannable.Factory.getInstance().newSpannable(charSequence.toString());
        }
        ArrayList<EmojiSpanRange> emojis = parseEmojis(spannableNewSpannable, iArr);
        if (emojis.isEmpty()) {
            return LocaleUtils.filterSpannable(charSequence);
        }
        AnimatedEmojiSpan[] animatedEmojiSpanArr = (AnimatedEmojiSpan[]) spannableNewSpannable.getSpans(0, spannableNewSpannable.length(), AnimatedEmojiSpan.class);
        ColoredImageSpan[] coloredImageSpanArr = (ColoredImageSpan[]) spannableNewSpannable.getSpans(0, spannableNewSpannable.length(), ColoredImageSpan.class);
        int i4 = (SharedConfig.getDevicePerformanceClass() >= 2 ? 100 : 50) - i2;
        for (int i5 = 0; i5 < emojis.size(); i5++) {
            try {
                EmojiSpanRange emojiSpanRange = emojis.get(i5);
                if (animatedEmojiSpanArr != null && animatedEmojiSpanArr.length > 0) {
                    int length = animatedEmojiSpanArr.length;
                    int i6 = 0;
                    while (true) {
                        if (i6 < length) {
                            AnimatedEmojiSpan animatedEmojiSpan = animatedEmojiSpanArr[i6];
                            if (animatedEmojiSpan == null || spannableNewSpannable.getSpanStart(animatedEmojiSpan) != emojiSpanRange.start || spannableNewSpannable.getSpanEnd(animatedEmojiSpan) != emojiSpanRange.end) {
                                i6++;
                            }
                        } else {
                            if (coloredImageSpanArr != null) {
                                i3 = 0;
                                while (true) {
                                    if (i3 < coloredImageSpanArr.length) {
                                        coloredImageSpan = coloredImageSpanArr[i3];
                                        if (coloredImageSpan != null) {
                                        }
                                        i3++;
                                    }
                                }
                            }
                            emojiDrawable = getEmojiDrawable(emojiSpanRange.code);
                            if (emojiDrawable != null) {
                                EmojiSpan emojiSpan = new EmojiSpan(emojiDrawable, i, fontMetricsInt);
                                charSequence2 = emojiSpanRange.code;
                                if (charSequence2 == null) {
                                    string = null;
                                } else {
                                    string = charSequence2.toString();
                                }
                                emojiSpan.emoji = string;
                                emojiSpan.scale = f;
                                spannableNewSpannable.setSpan(emojiSpan, emojiSpanRange.start, emojiSpanRange.end, 33);
                            }
                            if (Build.VERSION.SDK_INT >= 29) {
                                continue;
                            }
                        }
                    }
                } else {
                    if (coloredImageSpanArr != null && coloredImageSpanArr.length > 0) {
                        i3 = 0;
                        while (true) {
                            if (i3 < coloredImageSpanArr.length) {
                                coloredImageSpan = coloredImageSpanArr[i3];
                                if (coloredImageSpan != null || spannableNewSpannable.getSpanStart(coloredImageSpan) != emojiSpanRange.start || spannableNewSpannable.getSpanEnd(coloredImageSpan) != emojiSpanRange.end) {
                                    i3++;
                                }
                            }
                        }
                    }
                    emojiDrawable = getEmojiDrawable(emojiSpanRange.code);
                    if (emojiDrawable != null) {
                        EmojiSpan emojiSpan2 = new EmojiSpan(emojiDrawable, i, fontMetricsInt);
                        charSequence2 = emojiSpanRange.code;
                        if (charSequence2 == null) {
                            string = null;
                        } else {
                            string = charSequence2.toString();
                        }
                        emojiSpan2.emoji = string;
                        emojiSpan2.scale = f;
                        spannableNewSpannable.setSpan(emojiSpan2, emojiSpanRange.start, emojiSpanRange.end, 33);
                    }
                    if (Build.VERSION.SDK_INT >= 29 && i5 + 1 >= i4) {
                        break;
                    }
                }
            } catch (Exception e) {
                FileLog.e(e);
            }
        }
        return LocaleUtils.filterSpannable(spannableNewSpannable);
    }

    public static CharSequence replaceWithRestrictedEmoji(CharSequence charSequence, TextView textView, Runnable runnable) {
        return replaceWithRestrictedEmoji(charSequence, textView.getPaint().getFontMetricsInt(), runnable);
    }

    public static CharSequence replaceWithRestrictedEmoji(CharSequence charSequence, Paint.FontMetricsInt fontMetricsInt, final Runnable runnable) {
        Spannable spannableNewSpannable;
        TLRPC.Document document;
        AnimatedEmojiSpan animatedEmojiSpan;
        if (SharedConfig.useSystemEmoji || charSequence == null || charSequence.length() == 0) {
            return charSequence;
        }
        int i = UserConfig.selectedAccount;
        TLRPC.TL_inputStickerSetShortName tL_inputStickerSetShortName = new TLRPC.TL_inputStickerSetShortName();
        tL_inputStickerSetShortName.short_name = "RestrictedEmoji";
        TLRPC.TL_messages_stickerSet stickerSet = MediaDataController.getInstance(i).getStickerSet(tL_inputStickerSetShortName, 0, false, true, runnable == null ? null : new Utilities.Callback() { // from class: org.telegram.messenger.Emoji$$ExternalSyntheticLambda1
            @Override // org.telegram.messenger.Utilities.Callback
            public final void run(Object obj) {
                runnable.run();
            }
        });
        if (charSequence instanceof Spannable) {
            spannableNewSpannable = (Spannable) charSequence;
        } else {
            spannableNewSpannable = Spannable.Factory.getInstance().newSpannable(charSequence.toString());
        }
        Spannable spannable = spannableNewSpannable;
        ArrayList<EmojiSpanRange> emojis = parseEmojis(spannable, null);
        if (emojis.isEmpty()) {
            return charSequence;
        }
        AnimatedEmojiSpan[] animatedEmojiSpanArr = (AnimatedEmojiSpan[]) spannable.getSpans(0, spannable.length(), AnimatedEmojiSpan.class);
        int i2 = SharedConfig.getDevicePerformanceClass() >= 2 ? 100 : 50;
        for (int i3 = 0; i3 < emojis.size(); i3++) {
            try {
                EmojiSpanRange emojiSpanRange = emojis.get(i3);
                if (animatedEmojiSpanArr != null) {
                    int i4 = 0;
                    while (true) {
                        if (i4 < animatedEmojiSpanArr.length) {
                            AnimatedEmojiSpan animatedEmojiSpan2 = animatedEmojiSpanArr[i4];
                            if (animatedEmojiSpan2 == null || spannable.getSpanStart(animatedEmojiSpan2) != emojiSpanRange.start || spannable.getSpanEnd(animatedEmojiSpan2) != emojiSpanRange.end) {
                                i4++;
                            }
                        }
                    }
                }
                if (stickerSet == null) {
                    document = null;
                    break;
                }
                ArrayList arrayList = stickerSet.documents;
                int size = arrayList.size();
                int i5 = 0;
                do {
                    if (i5 >= size) {
                        document = null;
                        break;
                    }
                    Object obj = arrayList.get(i5);
                    i5++;
                    document = (TLRPC.Document) obj;
                } while (!MessageObject.findAnimatedEmojiEmoticon(document, null).contains(emojiSpanRange.code));
                if (document != null) {
                    animatedEmojiSpan = new AnimatedEmojiSpan(document, fontMetricsInt);
                } else {
                    animatedEmojiSpan = new AnimatedEmojiSpan(0L, fontMetricsInt);
                }
                animatedEmojiSpan.emoji = emojiSpanRange.code.toString();
                animatedEmojiSpan.cacheType = 20;
                spannable.setSpan(animatedEmojiSpan, emojiSpanRange.start, emojiSpanRange.end, 33);
            } catch (Exception e) {
                FileLog.e(e);
            }
            if (Build.VERSION.SDK_INT >= 29 && i3 + 1 >= i2) {
                break;
            }
        }
        return spannable;
    }

    public static class EmojiSpan extends ImageSpan {
        public boolean drawn;
        public String emoji;
        public Paint.FontMetricsInt fontMetrics;
        public float lastDrawX;
        public float lastDrawY;
        public float scale;
        public int size;

        public EmojiSpan(Drawable drawable, int i, Paint.FontMetricsInt fontMetricsInt) {
            super(drawable, i);
            this.scale = 1.0f;
            this.size = AndroidUtilities.dp(20.0f);
            this.fontMetrics = fontMetricsInt;
            if (fontMetricsInt != null) {
                int iAbs = Math.abs(fontMetricsInt.descent) + Math.abs(this.fontMetrics.ascent);
                this.size = iAbs;
                if (iAbs == 0) {
                    this.size = AndroidUtilities.dp(20.0f);
                }
            }
        }

        public void replaceFontMetrics(Paint.FontMetricsInt fontMetricsInt, int i) {
            this.fontMetrics = fontMetricsInt;
            this.size = i;
        }

        public void replaceFontMetrics(Paint.FontMetricsInt fontMetricsInt) {
            this.fontMetrics = fontMetricsInt;
            if (fontMetricsInt != null) {
                int iAbs = Math.abs(fontMetricsInt.descent) + Math.abs(this.fontMetrics.ascent);
                this.size = iAbs;
                if (iAbs == 0) {
                    this.size = AndroidUtilities.dp(20.0f);
                }
            }
        }

        @Override // android.text.style.DynamicDrawableSpan, android.text.style.ReplacementSpan
        public int getSize(Paint paint, CharSequence charSequence, int i, int i2, Paint.FontMetricsInt fontMetricsInt) {
            if (fontMetricsInt == null) {
                fontMetricsInt = new Paint.FontMetricsInt();
            }
            Paint.FontMetricsInt fontMetricsInt2 = fontMetricsInt;
            int i3 = (int) (this.scale * this.size);
            Paint.FontMetricsInt fontMetricsInt3 = this.fontMetrics;
            if (fontMetricsInt3 == null) {
                int size = super.getSize(paint, charSequence, i, i2, fontMetricsInt2);
                int iDp = AndroidUtilities.dp(8.0f);
                int iDp2 = AndroidUtilities.dp(10.0f);
                int i4 = (-iDp2) - iDp;
                fontMetricsInt2.top = i4;
                int i5 = iDp2 - iDp;
                fontMetricsInt2.bottom = i5;
                fontMetricsInt2.ascent = i4;
                fontMetricsInt2.leading = 0;
                fontMetricsInt2.descent = i5;
                return size;
            }
            fontMetricsInt2.ascent = fontMetricsInt3.ascent;
            fontMetricsInt2.descent = fontMetricsInt3.descent;
            fontMetricsInt2.top = fontMetricsInt3.top;
            fontMetricsInt2.bottom = fontMetricsInt3.bottom;
            if (getDrawable() != null) {
                getDrawable().setBounds(0, 0, i3, i3);
            }
            return i3;
        }

        @Override // android.text.style.DynamicDrawableSpan, android.text.style.ReplacementSpan
        public void draw(Canvas canvas, CharSequence charSequence, int i, int i2, float f, int i3, int i4, int i5, Paint paint) {
            boolean z;
            this.lastDrawX = ((this.scale * this.size) / 2.0f) + f;
            this.lastDrawY = i3 + ((i5 - i3) / 2.0f);
            boolean z2 = true;
            this.drawn = true;
            if (paint.getAlpha() == 255 || !Emoji.emojiDrawingUseAlpha) {
                z = false;
            } else {
                getDrawable().setAlpha(paint.getAlpha());
                z = true;
            }
            float f2 = Emoji.emojiDrawingYOffset;
            int i6 = this.size;
            float f3 = f2 - ((i6 - (this.scale * i6)) / 2.0f);
            if (f3 != 0.0f) {
                canvas.save();
                canvas.translate(0.0f, f3);
            } else {
                z2 = false;
            }
            super.draw(canvas, charSequence, i, i2, f, i3, i4, i5, paint);
            if (z2) {
                canvas.restore();
            }
            if (z) {
                getDrawable().setAlpha(255);
            }
        }

        @Override // android.text.style.ReplacementSpan, android.text.style.CharacterStyle
        public void updateDrawState(TextPaint textPaint) {
            if (getDrawable() instanceof EmojiDrawable) {
                ((EmojiDrawable) getDrawable()).placeholderColor = 285212671 & textPaint.getColor();
            }
            super.updateDrawState(textPaint);
        }

        public boolean equals(Object obj) {
            if (this == obj) {
                return true;
            }
            if (obj != null && getClass() == obj.getClass()) {
                EmojiSpan emojiSpan = (EmojiSpan) obj;
                if (Float.compare(this.scale, emojiSpan.scale) == 0 && this.size == emojiSpan.size && Objects.equals(this.emoji, emojiSpan.emoji)) {
                    return true;
                }
            }
            return false;
        }
    }

    public static void addRecentEmoji(String str) {
        HashMap<String, Integer> map = emojiUseHistory;
        Integer num = map.get(str);
        if (num == null) {
            num = 0;
        }
        if (num.intValue() == 0 && map.size() >= 48) {
            ArrayList<String> arrayList = recentEmoji;
            map.remove(arrayList.get(arrayList.size() - 1));
            arrayList.set(arrayList.size() - 1, str);
        }
        map.put(str, Integer.valueOf(num.intValue() + 1));
    }

    public static void removeRecentEmoji(String str) {
        HashMap<String, Integer> map = emojiUseHistory;
        map.remove(str);
        ArrayList<String> arrayList = recentEmoji;
        arrayList.remove(str);
        if (map.isEmpty() || arrayList.isEmpty()) {
            addRecentEmoji(DEFAULT_RECENT[0]);
        }
    }

    public static void sortEmoji() {
        recentEmoji.clear();
        Iterator<Map.Entry<String, Integer>> it = emojiUseHistory.entrySet().iterator();
        while (it.hasNext()) {
            recentEmoji.add(it.next().getKey());
        }
        Collections.sort(recentEmoji, new Comparator() { // from class: org.telegram.messenger.Emoji$$ExternalSyntheticLambda0
            @Override // java.util.Comparator
            public final int compare(Object obj, Object obj2) {
                return Emoji.m2854$r8$lambda$tEFea3Q0DiBPnoNT35a881s2dE((String) obj, (String) obj2);
            }
        });
        while (true) {
            ArrayList<String> arrayList = recentEmoji;
            if (arrayList.size() <= 48) {
                return;
            } else {
                arrayList.remove(arrayList.size() - 1);
            }
        }
    }

    /* JADX INFO: renamed from: $r8$lambda$tEFea3Q0DiBPnoNT-35a881s2dE, reason: not valid java name */
    public static /* synthetic */ int m2854$r8$lambda$tEFea3Q0DiBPnoNT35a881s2dE(String str, String str2) {
        Integer num = 0;
        HashMap<String, Integer> map = emojiUseHistory;
        Integer num2 = map.get(str);
        Integer num3 = map.get(str2);
        if (num2 == null) {
            num2 = num;
        }
        num = num3 != null ? num3 : 0;
        if (num2.intValue() > num.intValue()) {
            return -1;
        }
        return num2.intValue() < num.intValue() ? 1 : 0;
    }

    public static void saveRecentEmoji() {
        SharedPreferences globalEmojiSettings = MessagesController.getGlobalEmojiSettings();
        StringBuilder sb = new StringBuilder();
        for (Map.Entry<String, Integer> entry : emojiUseHistory.entrySet()) {
            if (sb.length() != 0) {
                sb.append(",");
            }
            sb.append(entry.getKey());
            sb.append("=");
            sb.append(entry.getValue());
        }
        globalEmojiSettings.edit().putString("emojis2", sb.toString()).apply();
    }

    public static void clearRecentEmoji() {
        MessagesController.getGlobalEmojiSettings().edit().putBoolean("filled_default", true).apply();
        emojiUseHistory.clear();
        recentEmoji.clear();
        saveRecentEmoji();
    }

    /* JADX WARN: Multi-variable type inference failed */
    public static void loadRecentEmoji() {
        char c;
        if (recentEmojiLoaded) {
            return;
        }
        char c2 = 1;
        recentEmojiLoaded = true;
        SharedPreferences globalEmojiSettings = MessagesController.getGlobalEmojiSettings();
        try {
            emojiUseHistory.clear();
            if (globalEmojiSettings.contains("emojis")) {
                try {
                    String string = globalEmojiSettings.getString("emojis", _UrlKt.FRAGMENT_ENCODE_SET);
                    if (string != null && string.length() > 0) {
                        String[] strArrSplit = string.split(",");
                        int length = strArrSplit.length;
                        int i = 0;
                        while (i < length) {
                            String[] strArrSplit2 = strArrSplit[i].split("=");
                            long jLongValue = Utilities.parseLong(strArrSplit2[0]).longValue();
                            StringBuilder sb = new StringBuilder();
                            char c3 = c2;
                            for (int i2 = 0; i2 < 4; i2++) {
                                sb.insert(0, (char) jLongValue);
                                jLongValue >>= 16;
                                if (jLongValue == 0) {
                                    break;
                                }
                            }
                            if (sb.length() > 0) {
                                emojiUseHistory.put(sb.toString(), Utilities.parseInt((CharSequence) strArrSplit2[c3]));
                            }
                            i++;
                            c2 = c3;
                        }
                    }
                    c = c2;
                    globalEmojiSettings.edit().remove("emojis").apply();
                    saveRecentEmoji();
                } catch (Exception e) {
                    e = e;
                    FileLog.e(e);
                }
            } else {
                c = 1;
                String string2 = globalEmojiSettings.getString("emojis2", _UrlKt.FRAGMENT_ENCODE_SET);
                if (string2 != null && string2.length() > 0) {
                    for (String str : string2.split(",")) {
                        String[] strArrSplit3 = str.split("=");
                        emojiUseHistory.put(strArrSplit3[0], Utilities.parseInt((CharSequence) strArrSplit3[1]));
                    }
                }
            }
            if (emojiUseHistory.isEmpty() && !globalEmojiSettings.getBoolean("filled_default", false)) {
                int i3 = 0;
                while (true) {
                    String[] strArr = DEFAULT_RECENT;
                    if (i3 >= strArr.length) {
                        break;
                    }
                    emojiUseHistory.put(strArr[i3], Integer.valueOf(strArr.length - i3));
                    i3++;
                }
                globalEmojiSettings.edit().putBoolean("filled_default", c).apply();
                saveRecentEmoji();
            }
            sortEmoji();
        } catch (Exception e2) {
            e = e2;
        }
        try {
            String string3 = globalEmojiSettings.getString("color", _UrlKt.FRAGMENT_ENCODE_SET);
            if (string3 == null || string3.length() <= 0) {
                return;
            }
            for (String str2 : string3.split(",")) {
                String[] strArrSplit4 = str2.split("=");
                emojiColor.put(strArrSplit4[0], strArrSplit4[1]);
            }
        } catch (Exception e3) {
            FileLog.e(e3);
        }
    }

    public static void saveEmojiColors() {
        SharedPreferences globalEmojiSettings = MessagesController.getGlobalEmojiSettings();
        StringBuilder sb = new StringBuilder();
        for (Map.Entry<String, String> entry : emojiColor.entrySet()) {
            if (sb.length() != 0) {
                sb.append(",");
            }
            sb.append(entry.getKey());
            sb.append("=");
            sb.append(entry.getValue());
        }
        globalEmojiSettings.edit().putString("color", sb.toString()).apply();
    }
}
