package org.telegram.ui.Components;

import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.RectF;
import android.text.Layout;
import android.text.SpannableStringBuilder;
import android.text.StaticLayout;
import android.text.TextPaint;
import android.view.View;
import java.util.ArrayList;
import org.telegram.messenger.AndroidUtilities;
import org.telegram.messenger.DialogObject;
import org.telegram.messenger.LocaleController;
import org.telegram.messenger.MessageObject;
import org.telegram.messenger.MessageSuggestionParams;
import org.telegram.messenger.R;
import org.telegram.messenger.utils.tlutils.AmountUtils$Amount;
import org.telegram.tgnet.TLRPC;
import org.telegram.ui.ActionBar.Theme;

public class SuggestionOffer {
    public int height;
    private final Theme.ResourcesProvider resourcesProvider;
    public ArrayList rows = new ArrayList(2);
    private int rowsInfoX;
    private int rowsTitleX;
    public StaticLayout title;
    private int titleX;
    public int width;

    public static class Row {
        public final Text info;
        public final Text title;

        public Row(Text text, Text text2) {
            this.title = text;
            this.info = text2;
        }

        public int getHeight() {
            return (int) this.title.getHeight();
        }
    }

    public SuggestionOffer(int i, View view, Theme.ResourcesProvider resourcesProvider) {
        this.resourcesProvider = resourcesProvider;
    }

    /* JADX WARN: Multi-variable type inference failed */
    /* JADX WARN: Type inference failed for: r14v3 */
    /* JADX WARN: Type inference failed for: r14v4, types: [int] */
    /* JADX WARN: Type inference failed for: r14v6 */
    /* JADX WARN: Type inference failed for: r1v24 */
    /* JADX WARN: Type inference failed for: r1v25 */
    /* JADX WARN: Type inference failed for: r1v7, types: [int] */
    /* JADX WARN: Type inference failed for: r2v31, types: [android.text.Layout, android.text.StaticLayout] */
    public void update(MessageObject messageObject) {
        int i;
        TLRPC.Message message;
        TLRPC.SuggestedPost suggestedPost = (messageObject == null || (message = messageObject.messageOwner) == null) ? null : message.suggested_post;
        if (suggestedPost == null) {
            return;
        }
        MessageSuggestionParams messageSuggestionParamsOf = MessageSuggestionParams.of(suggestedPost);
        TextPaint textPaint = (TextPaint) getThemedPaint("paintChatActionText3");
        this.height = AndroidUtilities.dp(14.0f) * 2;
        this.rows.clear();
        AmountUtils$Amount amountUtils$Amount = messageSuggestionParamsOf.amount;
        if (amountUtils$Amount != null && !amountUtils$Amount.isZero()) {
            this.rows.add(new Row(new Text(LocaleController.getString(R.string.SuggestionOfferInfoPrice), textPaint), new Text(LocaleController.bold(messageSuggestionParamsOf.amount.formatAsDecimalSpaced()), textPaint)));
        }
        if (suggestedPost.schedule_date > 0) {
            this.rows.add(new Row(new Text(LocaleController.getString(R.string.SuggestionOfferInfoTime), textPaint), new Text(LocaleController.bold(LocaleController.formatDateTime(suggestedPost.schedule_date, true)), textPaint)));
        }
        ArrayList arrayList = this.rows;
        int size = arrayList.size();
        float fMax = 0.0f;
        boolean z = false;
        float fMax2 = 0.0f;
        int i2 = 0;
        while (i2 < size) {
            Object obj = arrayList.get(i2);
            i2++;
            Row row = (Row) obj;
            fMax2 = Math.max(fMax2, row.title.getWidth());
            fMax = Math.max(fMax, row.info.getWidth());
            int height = this.height + row.getHeight();
            this.height = height;
            this.height = height + AndroidUtilities.dp(7.0f);
        }
        float f = 11.0f;
        int iDp = (int) (fMax + fMax2 + AndroidUtilities.dp(11.0f));
        int iMax = Math.max(iDp, AndroidUtilities.dp(160.0f));
        String name = DialogObject.getName(messageObject.getFromChatId());
        int editedSuggestionFlags = messageObject.getEditedSuggestionFlags();
        SpannableStringBuilder spannableStringBuilder = new SpannableStringBuilder();
        if (editedSuggestionFlags == 0) {
            if (messageObject.isOutOwner()) {
                spannableStringBuilder.append((CharSequence) LocaleController.getString(R.string.SuggestionOfferInfoTitleYou));
            } else {
                spannableStringBuilder.append((CharSequence) LocaleController.formatString(R.string.SuggestionOfferInfoTitle, name));
            }
            f = 11.0f;
            z = false;
        } else {
            MessageObject messageObject2 = messageObject.replyMessageObject;
            if (messageObject2 != null) {
                DialogObject.getName(messageObject2.getFromChatId());
            }
            StringBuilder sb = new StringBuilder();
            int i3 = editedSuggestionFlags & 4;
            int i4 = editedSuggestionFlags & 2;
            int i5 = editedSuggestionFlags & 8;
            int i6 = editedSuggestionFlags & 1;
            int i7 = (i3 != 0 ? 1 : 0) + (i4 != 0 ? 1 : 0) + (i5 != 0 ? 1 : 0) + (i6 != 0 ? 1 : 0);
            if (i6 != 0) {
                updateBuildTitleStep(sb, R.string.SuggestionOfferInfoTitleEditedPrice, i7 == 1);
                i = 1;
            } else {
                i = 0;
            }
            if (i4 != 0) {
                i++;
                updateBuildTitleStep(sb, R.string.SuggestionOfferInfoTitleEditedTime, i7 == i);
            }
            if (i3 != 0) {
                i++;
                updateBuildTitleStep(sb, R.string.SuggestionOfferInfoTitleEditedText, i7 == i);
            }
            if (i5 != 0) {
                updateBuildTitleStep(sb, R.string.SuggestionOfferInfoTitleEditedMedia, i7 == i + 1 ? true : z);
            }
            if (messageObject.isOutOwner()) {
                spannableStringBuilder.append((CharSequence) LocaleController.formatString(R.string.SuggestionOfferInfoTitleEditedFromYou, sb));
            } else {
                spannableStringBuilder.append((CharSequence) LocaleController.formatString(R.string.SuggestionOfferInfoTitleEditedFromX, name, sb));
            }
        }
        this.title = new StaticLayout(AndroidUtilities.replaceTags(spannableStringBuilder), textPaint, iMax, Layout.Alignment.ALIGN_CENTER, 1.0f, 0.0f, false);
        boolean z2 = z;
        ?? Max = z2;
        for (?? r14 = z2; r14 < this.title.getLineCount(); r14++) {
            Max = (int) Math.max((float) Max, this.title.getLineWidth(r14));
        }
        int height2 = this.height + this.title.getHeight();
        this.height = height2;
        this.height = height2 + AndroidUtilities.dp(5.0f);
        int iMax2 = Math.max(iDp, (int) Max) + (AndroidUtilities.dp(24.0f) * 2);
        this.width = iMax2;
        this.titleX = (iMax2 - iMax) / 2;
        int i8 = (iMax2 - iDp) / 2;
        this.rowsTitleX = i8;
        this.rowsInfoX = (int) (i8 + AndroidUtilities.dp(f) + fMax2);
    }

    private void updateBuildTitleStep(StringBuilder sb, int i, boolean z) {
        if (sb.length() > 0) {
            if (z) {
                sb.append(' ');
                sb.append(LocaleController.getString(R.string.SuggestionOfferInfoTitleEditedAnd));
                sb.append(' ');
            } else {
                sb.append(", ");
            }
        }
        sb.append(LocaleController.getString(i));
    }

    public int getHeight() {
        return this.height;
    }

    public int getWidth() {
        return this.width;
    }

    public void draw(Canvas canvas, int i, float f, float f2, float f3, float f4, boolean z) {
        int i2 = this.width;
        int i3 = (i - i2) / 2;
        RectF rectF = AndroidUtilities.rectTmp;
        rectF.set(i3, 0.0f, i2 + i3, this.height);
        canvas.save();
        canvas.translate(f / 2.0f, f2);
        Paint themePaint = Theme.getThemePaint("paintChatActionBackground", this.resourcesProvider);
        int alpha = themePaint.getAlpha();
        themePaint.setAlpha((int) (alpha * f4 * f3));
        canvas.drawRoundRect(rectF, AndroidUtilities.dp(15.0f), AndroidUtilities.dp(15.0f), themePaint);
        themePaint.setAlpha(alpha);
        Theme.ResourcesProvider resourcesProvider = this.resourcesProvider;
        if (resourcesProvider != null ? resourcesProvider.hasGradientService() : Theme.hasGradientService()) {
            Paint themePaint2 = Theme.getThemePaint("paintChatActionBackgroundDarken", this.resourcesProvider);
            int alpha2 = themePaint2.getAlpha();
            themePaint2.setAlpha((int) (alpha2 * f4 * f3));
            canvas.drawRect(rectF, themePaint2);
            themePaint2.setAlpha(alpha2);
        }
        int iDp = AndroidUtilities.dp(14.0f);
        if (this.title != null) {
            canvas.save();
            canvas.translate(this.titleX + i3, iDp);
            this.title.draw(canvas);
            canvas.restore();
            iDp += this.title.getHeight() + AndroidUtilities.dp(12.0f);
        }
        ArrayList arrayList = this.rows;
        int size = arrayList.size();
        int i4 = 0;
        while (i4 < size) {
            Object obj = arrayList.get(i4);
            i4++;
            Row row = (Row) obj;
            float f5 = iDp;
            row.title.draw(canvas, this.rowsTitleX + i3, (row.getHeight() / 2.0f) + f5, 0.85f);
            row.info.draw(canvas, this.rowsInfoX + i3, f5 + (row.getHeight() / 2.0f));
            iDp += row.getHeight() + AndroidUtilities.dp(7.0f);
        }
        canvas.restore();
    }

    protected Paint getThemedPaint(String str) {
        Theme.ResourcesProvider resourcesProvider = this.resourcesProvider;
        Paint paint = resourcesProvider != null ? resourcesProvider.getPaint(str) : null;
        return paint != null ? paint : Theme.getThemePaint(str);
    }
}
