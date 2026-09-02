package org.telegram.ui.Cells;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Path;
import android.graphics.Rect;
import android.graphics.Region;
import android.net.Uri;
import android.text.Layout;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.SpannableStringBuilder;
import android.text.StaticLayout;
import android.text.TextPaint;
import android.text.TextUtils;
import android.util.SparseArray;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewConfiguration;
import android.view.accessibility.AccessibilityNodeInfo;
import android.widget.FrameLayout;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;
import java.util.Stack;
import java.util.concurrent.atomic.AtomicReference;
import org.telegram.messenger.AndroidUtilities;
import org.telegram.messenger.Emoji;
import org.telegram.messenger.FileLoader;
import org.telegram.messenger.FileLog;
import org.telegram.messenger.ImageLocation;
import org.telegram.messenger.ImageReceiver;
import org.telegram.messenger.LocaleController;
import org.telegram.messenger.MediaDataController;
import org.telegram.messenger.MessageObject;
import org.telegram.tgnet.TLObject;
import org.telegram.tgnet.TLRPC;
import org.telegram.ui.ActionBar.Theme;
import org.telegram.ui.Components.AnimatedEmojiSpan;
import org.telegram.ui.Components.CheckBox2;
import org.telegram.ui.Components.LayoutHelper;
import org.telegram.ui.Components.LetterDrawable;
import org.telegram.ui.Components.LinkPath;
import org.telegram.ui.Components.LinkSpanDrawable;
import org.telegram.ui.Components.TextStyleSpan;
import org.telegram.ui.Components.spoilers.SpoilerEffect;
import org.telegram.ui.FilteredSearchView;

public class SharedLinkCell extends FrameLayout {
    private StaticLayout captionLayout;
    private TextPaint captionTextPaint;
    private int captionY;
    private CheckBox2 checkBox;
    private boolean checkingForLongPress;
    private StaticLayout dateLayout;
    private int dateLayoutX;
    private SharedLinkCellDelegate delegate;
    private TextPaint description2TextPaint;
    private int description2Y;
    private StaticLayout descriptionLayout;
    private StaticLayout descriptionLayout2;
    private List descriptionLayout2Spoilers;
    private List descriptionLayoutSpoilers;
    private TextPaint descriptionTextPaint;
    private int descriptionY;
    private boolean drawLinkImageView;
    private StaticLayout fromInfoLayout;
    private AnimatedEmojiSpan.EmojiGroupedSpans fromInfoLayoutEmojis;
    private int fromInfoLayoutY;
    private LetterDrawable letterDrawable;
    private ImageReceiver linkImageView;
    private ArrayList linkLayout;
    private boolean linkPreviewPressed;
    private SparseArray linkSpoilers;
    private int linkY;
    ArrayList links;
    private LinkSpanDrawable.LinkCollector linksCollector;
    private MessageObject message;
    private boolean needDivider;
    private AtomicReference patchedDescriptionLayout;
    private AtomicReference patchedDescriptionLayout2;
    private Path path;
    private CheckForLongPress pendingCheckForLongPress;
    private CheckForTap pendingCheckForTap;
    private int pressCount;
    private LinkSpanDrawable pressedLink;
    private int pressedLinkIndex;
    private Theme.ResourcesProvider resourcesProvider;
    private SpoilerEffect spoilerPressed;
    private int spoilerTypePressed;
    private Stack spoilersPool;
    private StaticLayout titleLayout;
    private TextPaint titleTextPaint;
    private int titleY;
    private int viewType;

    public interface SharedLinkCellDelegate {
        boolean canPerformActions();

        void needOpenWebView(TLRPC.WebPage webPage, MessageObject messageObject);

        void onLinkPress(String str, boolean z);
    }

    private final class CheckForTap implements Runnable {
        private CheckForTap() {
        }

        @Override // java.lang.Runnable
        public void run() {
            if (SharedLinkCell.this.pendingCheckForLongPress == null) {
                SharedLinkCell sharedLinkCell = SharedLinkCell.this;
                sharedLinkCell.pendingCheckForLongPress = sharedLinkCell.new CheckForLongPress();
            }
            CheckForLongPress checkForLongPress = SharedLinkCell.this.pendingCheckForLongPress;
            SharedLinkCell sharedLinkCell2 = SharedLinkCell.this;
            int i = sharedLinkCell2.pressCount + 1;
            sharedLinkCell2.pressCount = i;
            checkForLongPress.currentPressCount = i;
            SharedLinkCell sharedLinkCell3 = SharedLinkCell.this;
            sharedLinkCell3.postDelayed(sharedLinkCell3.pendingCheckForLongPress, ViewConfiguration.getLongPressTimeout() - ViewConfiguration.getTapTimeout());
        }
    }

    class CheckForLongPress implements Runnable {
        public int currentPressCount;

        CheckForLongPress() {
        }

        @Override // java.lang.Runnable
        public void run() {
            if (SharedLinkCell.this.checkingForLongPress && SharedLinkCell.this.getParent() != null && this.currentPressCount == SharedLinkCell.this.pressCount) {
                SharedLinkCell.this.checkingForLongPress = false;
                try {
                    SharedLinkCell.this.performHapticFeedback(0);
                } catch (Exception unused) {
                }
                if (SharedLinkCell.this.pressedLinkIndex >= 0) {
                    SharedLinkCellDelegate sharedLinkCellDelegate = SharedLinkCell.this.delegate;
                    SharedLinkCell sharedLinkCell = SharedLinkCell.this;
                    sharedLinkCellDelegate.onLinkPress(((CharSequence) sharedLinkCell.links.get(sharedLinkCell.pressedLinkIndex)).toString(), true);
                }
                MotionEvent motionEventObtain = MotionEvent.obtain(0L, 0L, 3, 0.0f, 0.0f, 0);
                SharedLinkCell.this.onTouchEvent(motionEventObtain);
                motionEventObtain.recycle();
            }
        }
    }

    protected void startCheckLongPress() {
        if (this.checkingForLongPress) {
            return;
        }
        this.checkingForLongPress = true;
        if (this.pendingCheckForTap == null) {
            this.pendingCheckForTap = new CheckForTap();
        }
        postDelayed(this.pendingCheckForTap, ViewConfiguration.getTapTimeout());
    }

    protected void cancelCheckLongPress() {
        this.checkingForLongPress = false;
        CheckForLongPress checkForLongPress = this.pendingCheckForLongPress;
        if (checkForLongPress != null) {
            removeCallbacks(checkForLongPress);
        }
        CheckForTap checkForTap = this.pendingCheckForTap;
        if (checkForTap != null) {
            removeCallbacks(checkForTap);
        }
    }

    public SharedLinkCell(Context context, int i) {
        this(context, i, null);
    }

    public SharedLinkCell(Context context, int i, Theme.ResourcesProvider resourcesProvider) {
        super(context);
        this.checkingForLongPress = false;
        this.pendingCheckForLongPress = null;
        this.pressCount = 0;
        this.pendingCheckForTap = null;
        this.linksCollector = new LinkSpanDrawable.LinkCollector(this);
        this.links = new ArrayList();
        this.linkLayout = new ArrayList();
        this.linkSpoilers = new SparseArray();
        this.descriptionLayoutSpoilers = new ArrayList();
        this.descriptionLayout2Spoilers = new ArrayList();
        this.spoilersPool = new Stack();
        this.path = new Path();
        this.spoilerTypePressed = -1;
        this.titleY = AndroidUtilities.dp(10.0f);
        this.descriptionY = AndroidUtilities.dp(30.0f);
        this.patchedDescriptionLayout = new AtomicReference();
        this.description2Y = AndroidUtilities.dp(30.0f);
        this.patchedDescriptionLayout2 = new AtomicReference();
        this.captionY = AndroidUtilities.dp(30.0f);
        this.fromInfoLayoutY = AndroidUtilities.dp(30.0f);
        this.resourcesProvider = resourcesProvider;
        this.viewType = i;
        setFocusable(true);
        TextPaint textPaint = new TextPaint(1);
        this.titleTextPaint = textPaint;
        textPaint.setTypeface(AndroidUtilities.bold());
        this.titleTextPaint.setColor(Theme.getColor(Theme.key_windowBackgroundWhiteBlackText, resourcesProvider));
        this.descriptionTextPaint = new TextPaint(1);
        this.titleTextPaint.setTextSize(AndroidUtilities.dp(14.0f));
        this.descriptionTextPaint.setTextSize(AndroidUtilities.dp(14.0f));
        setWillNotDraw(false);
        ImageReceiver imageReceiver = new ImageReceiver(this);
        this.linkImageView = imageReceiver;
        imageReceiver.setRoundRadius(AndroidUtilities.dp(8.0f));
        this.letterDrawable = new LetterDrawable(resourcesProvider, 0);
        CheckBox2 checkBox2 = new CheckBox2(context, 21, resourcesProvider);
        this.checkBox = checkBox2;
        checkBox2.setVisibility(4);
        this.checkBox.setColor(-1, Theme.key_windowBackgroundWhite, Theme.key_checkboxCheck);
        this.checkBox.setDrawUnchecked(false);
        this.checkBox.setDrawBackgroundAsArc(2);
        CheckBox2 checkBox3 = this.checkBox;
        boolean z = LocaleController.isRTL;
        addView(checkBox3, LayoutHelper.createFrame(24, 24.0f, (z ? 5 : 3) | 48, z ? 0.0f : 44.0f, 44.0f, z ? 44.0f : 0.0f, 0.0f));
        if (i == 1) {
            TextPaint textPaint2 = new TextPaint(1);
            this.description2TextPaint = textPaint2;
            textPaint2.setTextSize(AndroidUtilities.dp(13.0f));
        }
        TextPaint textPaint3 = new TextPaint(1);
        this.captionTextPaint = textPaint3;
        textPaint3.setTextSize(AndroidUtilities.dp(13.0f));
    }

    /* JADX WARN: Code duplicated, block: B:104:0x022c A[Catch: Exception -> 0x0176, TryCatch #8 {Exception -> 0x0176, blocks: (B:67:0x0146, B:69:0x0167, B:104:0x022c, B:106:0x0234, B:108:0x0244, B:110:0x0254, B:112:0x0268, B:114:0x027e, B:118:0x0299, B:121:0x02c5, B:74:0x0179, B:77:0x018e, B:79:0x0192, B:82:0x01a6, B:86:0x01b7, B:88:0x01bd, B:90:0x01cb, B:92:0x01d2, B:94:0x01da, B:96:0x01e4, B:97:0x01eb, B:98:0x0207, B:100:0x020b, B:102:0x0219, B:80:0x01a2), top: B:314:0x0146 }] */
    /* JADX WARN: Code duplicated, block: B:111:0x0267  */
    /* JADX WARN: Code duplicated, block: B:114:0x027e A[Catch: Exception -> 0x0176, TryCatch #8 {Exception -> 0x0176, blocks: (B:67:0x0146, B:69:0x0167, B:104:0x022c, B:106:0x0234, B:108:0x0244, B:110:0x0254, B:112:0x0268, B:114:0x027e, B:118:0x0299, B:121:0x02c5, B:74:0x0179, B:77:0x018e, B:79:0x0192, B:82:0x01a6, B:86:0x01b7, B:88:0x01bd, B:90:0x01cb, B:92:0x01d2, B:94:0x01da, B:96:0x01e4, B:97:0x01eb, B:98:0x0207, B:100:0x020b, B:102:0x0219, B:80:0x01a2), top: B:314:0x0146 }] */
    /* JADX WARN: Code duplicated, block: B:119:0x02bb  */
    /* JADX WARN: Code duplicated, block: B:21:0x0066  */
    /* JADX WARN: Multi-variable type inference failed */
    /* JADX WARN: Type inference failed for: r3v41 */
    /* JADX WARN: Type inference failed for: r3v7 */
    @Override // android.widget.FrameLayout, android.view.View
    protected void onMeasure(int i, int i2) {
        boolean z;
        String str;
        String str2;
        String str3;
        float f;
        CharSequence charSequence;
        SpannableStringBuilder spannableStringBuilder;
        String host;
        int iDp;
        CharSequence charSequence2;
        int i3;
        TLRPC.PhotoSize photoSize;
        int i4;
        int lineBottom;
        int lineBottom2;
        float f2;
        int i5;
        float f3;
        int i6;
        String strSubstring;
        SpannableStringBuilder spannableStringBuilderValueOf;
        int iLastIndexOf;
        int i7;
        ArrayList arrayList;
        int size;
        int i8;
        TLRPC.MessageEntity messageEntity;
        int i9;
        int i10 = 0;
        this.drawLinkImageView = false;
        String str4 = null;
        this.descriptionLayout = null;
        this.titleLayout = null;
        this.descriptionLayout2 = null;
        this.captionLayout = null;
        this.linkLayout.clear();
        this.links.clear();
        float f4 = 8.0f;
        int size2 = (View.MeasureSpec.getSize(i) - AndroidUtilities.dp(AndroidUtilities.leftBaseline)) - AndroidUtilities.dp(8.0f);
        MessageObject messageObject = this.message;
        TLRPC.MessageMedia messageMedia = messageObject.messageOwner.media;
        int i11 = 1;
        if (messageMedia instanceof TLRPC.TL_messageMediaWebPage) {
            TLRPC.WebPage webPage = messageMedia.webpage;
            if (webPage instanceof TLRPC.TL_webPage) {
                if (messageObject.photoThumbs == null && webPage.photo != null) {
                    messageObject.generateThumbs(true);
                }
                boolean z2 = (webPage.photo == null || this.message.photoThumbs == null) ? false : true;
                String str5 = webPage.title;
                if (str5 == null) {
                    str5 = webPage.site_name;
                }
                str3 = webPage.description;
                String str6 = webPage.url;
                z = z2;
                str = str5;
                str2 = str6;
            } else {
                z = false;
                str = null;
                str2 = null;
                str3 = null;
            }
        } else {
            z = false;
            str = null;
            str2 = null;
            str3 = null;
        }
        MessageObject messageObject2 = this.message;
        if (messageObject2 == null || messageObject2.messageOwner.entities.isEmpty()) {
            f = 8.0f;
            charSequence = str3;
            spannableStringBuilder = null;
            host = str;
        } else {
            int i12 = 0;
            CharSequence charSequence3 = str3;
            host = str;
            SpannableStringBuilder spannableStringBuilderValueOf2 = null;
            while (i12 < this.message.messageOwner.entities.size()) {
                TLRPC.MessageEntity messageEntity2 = (TLRPC.MessageEntity) this.message.messageOwner.entities.get(i12);
                if (messageEntity2.length <= 0 || (i6 = messageEntity2.offset) < 0 || i6 >= this.message.messageOwner.message.length()) {
                    f3 = f4;
                } else {
                    if (messageEntity2.offset + messageEntity2.length > this.message.messageOwner.message.length()) {
                        messageEntity2.length = this.message.messageOwner.message.length() - messageEntity2.offset;
                    }
                    if (i12 == 0 && str2 != null && (messageEntity2.offset != 0 || messageEntity2.length != this.message.messageOwner.message.length())) {
                        if (this.message.messageOwner.entities.size() != i11) {
                            spannableStringBuilderValueOf2 = SpannableStringBuilder.valueOf(this.message.messageOwner.message);
                            MediaDataController.addTextStyleRuns(this.message, spannableStringBuilderValueOf2);
                        } else if (charSequence3 == null) {
                            spannableStringBuilderValueOf2 = SpannableStringBuilder.valueOf(this.message.messageOwner.message);
                            MediaDataController.addTextStyleRuns(this.message, spannableStringBuilderValueOf2);
                        }
                    }
                    SpannableStringBuilder spannableStringBuilder2 = spannableStringBuilderValueOf2;
                    try {
                        if ((messageEntity2 instanceof TLRPC.TL_messageEntityTextUrl) || (messageEntity2 instanceof TLRPC.TL_messageEntityUrl)) {
                            f3 = f4;
                            if (messageEntity2 instanceof TLRPC.TL_messageEntityUrl) {
                                String str7 = this.message.messageOwner.message;
                                int i13 = messageEntity2.offset;
                                strSubstring = str7.substring(i13, messageEntity2.length + i13);
                            } else {
                                strSubstring = messageEntity2.url;
                            }
                            if (strSubstring == null || !strSubstring.toString().startsWith("tg://emoji?id=")) {
                                if (host == null || host.length() == 0) {
                                    charSequence3 = charSequence3;
                                    host = Uri.parse(strSubstring.toString()).getHost();
                                    if (host == null) {
                                        host = strSubstring.toString();
                                    }
                                    if (host != null && (iLastIndexOf = host.lastIndexOf(46)) >= 0) {
                                        String strSubstring2 = host.substring(i10, iLastIndexOf);
                                        int iLastIndexOf2 = strSubstring2.lastIndexOf(46);
                                        if (iLastIndexOf2 >= 0) {
                                            strSubstring2 = strSubstring2.substring(iLastIndexOf2 + 1);
                                        }
                                        host = strSubstring2.substring(i10, i11).toUpperCase() + strSubstring2.substring(i11);
                                    }
                                    if (messageEntity2.offset != 0 || messageEntity2.length != this.message.messageOwner.message.length()) {
                                        charSequence3 = charSequence3;
                                        spannableStringBuilderValueOf = SpannableStringBuilder.valueOf(this.message.messageOwner.message);
                                        MediaDataController.addTextStyleRuns(this.message, spannableStringBuilderValueOf);
                                        charSequence3 = spannableStringBuilderValueOf;
                                    }
                                }
                                if (strSubstring != null) {
                                    if (!AndroidUtilities.charSequenceContains(strSubstring, "://") || strSubstring.toString().toLowerCase().indexOf("http") == 0 || strSubstring.toString().toLowerCase().indexOf("mailto") == 0) {
                                        i7 = i10;
                                    } else {
                                        strSubstring = "http://" + ((Object) strSubstring);
                                        i7 = 7;
                                    }
                                    SpannableString spannableStringValueOf = SpannableString.valueOf(strSubstring);
                                    int i14 = messageEntity2.offset;
                                    int i15 = messageEntity2.length + i14;
                                    arrayList = this.message.messageOwner.entities;
                                    size = arrayList.size();
                                    i8 = 0;
                                    while (i8 < size) {
                                        Object obj = arrayList.get(i8);
                                        int i16 = i8 + 1;
                                        messageEntity = (TLRPC.MessageEntity) obj;
                                        int i17 = size;
                                        int i18 = messageEntity.offset;
                                        int i19 = messageEntity.length + i18;
                                        if ((messageEntity instanceof TLRPC.TL_messageEntitySpoiler) || i14 > i19 || i15 < i18) {
                                            i9 = i7;
                                        } else {
                                            TextStyleSpan.TextStyleRun textStyleRun = new TextStyleSpan.TextStyleRun();
                                            i9 = i7;
                                            textStyleRun.flags |= 256;
                                            spannableStringValueOf.setSpan(new TextStyleSpan(textStyleRun), Math.max(i14, i18), Math.min(i15, i19) + i9, 33);
                                        }
                                        size = i17;
                                        i8 = i16;
                                        i7 = i9;
                                    }
                                    this.links.add(spannableStringValueOf);
                                }
                            }
                        } else {
                            if ((messageEntity2 instanceof TLRPC.TL_messageEntityEmail) && (host == null || host.length() == 0)) {
                                StringBuilder sb = new StringBuilder();
                                sb.append("mailto:");
                                String str8 = this.message.messageOwner.message;
                                int i20 = messageEntity2.offset;
                                f3 = f4;
                                try {
                                    sb.append(str8.substring(i20, messageEntity2.length + i20));
                                    strSubstring = sb.toString();
                                    String str9 = this.message.messageOwner.message;
                                    int i21 = messageEntity2.offset;
                                    host = str9.substring(i21, messageEntity2.length + i21);
                                    if (messageEntity2.offset == 0) {
                                        if (messageEntity2.length != this.message.messageOwner.message.length()) {
                                            charSequence3 = charSequence3;
                                        }
                                    }
                                    spannableStringBuilderValueOf = SpannableStringBuilder.valueOf(this.message.messageOwner.message);
                                    MediaDataController.addTextStyleRuns(this.message, spannableStringBuilderValueOf);
                                    charSequence3 = spannableStringBuilderValueOf;
                                } catch (Exception e) {
                                    e = e;
                                    FileLog.e(e);
                                }
                            } else {
                                f3 = f4;
                                strSubstring = str4;
                                charSequence3 = charSequence3;
                            }
                            if (strSubstring != null) {
                                if (AndroidUtilities.charSequenceContains(strSubstring, "://")) {
                                    i7 = i10;
                                } else {
                                    i7 = i10;
                                }
                                SpannableString spannableStringValueOf2 = SpannableString.valueOf(strSubstring);
                                int i110 = messageEntity2.offset;
                                int i111 = messageEntity2.length + i110;
                                arrayList = this.message.messageOwner.entities;
                                size = arrayList.size();
                                i8 = 0;
                                while (i8 < size) {
                                    Object obj2 = arrayList.get(i8);
                                    int i112 = i8 + 1;
                                    messageEntity = (TLRPC.MessageEntity) obj2;
                                    int i113 = size;
                                    int i114 = messageEntity.offset;
                                    int i115 = messageEntity.length + i114;
                                    if (messageEntity instanceof TLRPC.TL_messageEntitySpoiler) {
                                        i9 = i7;
                                    } else {
                                        i9 = i7;
                                    }
                                    size = i113;
                                    i8 = i112;
                                    i7 = i9;
                                }
                                this.links.add(spannableStringValueOf2);
                            }
                        }
                    } catch (Exception e2) {
                        e = e2;
                        f3 = f4;
                    }
                    spannableStringBuilderValueOf2 = spannableStringBuilder2;
                }
                i12++;
                f4 = f3;
                i10 = 0;
                str4 = null;
                i11 = 1;
                charSequence3 = charSequence3;
            }
            f = f4;
            spannableStringBuilder = spannableStringBuilderValueOf2;
            charSequence = charSequence3;
        }
        if (str2 != null && this.links.isEmpty()) {
            this.links.add(str2);
        }
        if (this.viewType == 1) {
            String strStringForMessageListDate = LocaleController.stringForMessageListDate(this.message.messageOwner.date);
            int iCeil = (int) Math.ceil(this.description2TextPaint.measureText(strStringForMessageListDate));
            this.dateLayout = ChatMessageCell.generateStaticLayout(strStringForMessageListDate, this.description2TextPaint, iCeil, iCeil, 0, 1);
            this.dateLayoutX = (size2 - iCeil) - AndroidUtilities.dp(f);
            iDp = AndroidUtilities.dp(12.0f) + iCeil;
        } else {
            iDp = 0;
        }
        float f5 = 4.0f;
        if (host != null) {
            try {
                CharSequence charSequenceHighlightText = AndroidUtilities.highlightText(host, this.message.highlightedWords, (Theme.ResourcesProvider) null);
                int i22 = size2 - iDp;
                StaticLayout staticLayoutGenerateStaticLayout = ChatMessageCell.generateStaticLayout(charSequenceHighlightText != null ? charSequenceHighlightText : host, this.titleTextPaint, i22 - AndroidUtilities.dp(4.0f), i22 - AndroidUtilities.dp(4.0f), 0, 3);
                this.titleLayout = staticLayoutGenerateStaticLayout;
                if (staticLayoutGenerateStaticLayout.getLineCount() > 0) {
                    int i23 = this.titleY;
                    StaticLayout staticLayout = this.titleLayout;
                    this.descriptionY = i23 + staticLayout.getLineBottom(staticLayout.getLineCount() - 1) + AndroidUtilities.dp(4.0f);
                }
            } catch (Exception e3) {
                FileLog.e(e3);
            }
            this.letterDrawable.setTitle(host);
        }
        this.description2Y = this.descriptionY;
        StaticLayout staticLayout2 = this.titleLayout;
        int iMax = Math.max(1, 4 - (staticLayout2 != null ? staticLayout2.getLineCount() : 0));
        if (this.viewType == 1) {
            spannableStringBuilder = null;
            charSequence2 = null;
        } else {
            charSequence2 = charSequence;
        }
        if (charSequence2 != null) {
            try {
                StaticLayout staticLayoutGenerateStaticLayout2 = ChatMessageCell.generateStaticLayout(charSequence2, this.descriptionTextPaint, size2, size2, 0, iMax);
                this.descriptionLayout = staticLayoutGenerateStaticLayout2;
                if (staticLayoutGenerateStaticLayout2.getLineCount() > 0) {
                    int i24 = this.descriptionY;
                    StaticLayout staticLayout3 = this.descriptionLayout;
                    this.description2Y = i24 + staticLayout3.getLineBottom(staticLayout3.getLineCount() - 1) + AndroidUtilities.dp(5.0f);
                }
                this.spoilersPool.addAll(this.descriptionLayoutSpoilers);
                this.descriptionLayoutSpoilers.clear();
                if (!this.message.isSpoilersRevealed) {
                    SpoilerEffect.addSpoilers(this, this.descriptionLayout, this.spoilersPool, this.descriptionLayoutSpoilers);
                }
            } catch (Exception e4) {
                FileLog.e(e4);
            }
        }
        if (spannableStringBuilder != null) {
            try {
                SpannableStringBuilder spannableStringBuilder3 = spannableStringBuilder;
                i3 = iMax;
                try {
                    this.descriptionLayout2 = ChatMessageCell.generateStaticLayout(spannableStringBuilder3, this.descriptionTextPaint, size2, size2, 0, iMax);
                    if (this.descriptionLayout != null) {
                        this.description2Y += AndroidUtilities.dp(10.0f);
                    }
                    this.spoilersPool.addAll(this.descriptionLayout2Spoilers);
                    this.descriptionLayout2Spoilers.clear();
                    if (!this.message.isSpoilersRevealed) {
                        SpoilerEffect.addSpoilers(this, this.descriptionLayout2, this.spoilersPool, this.descriptionLayout2Spoilers);
                    }
                } catch (Exception e5) {
                    e = e5;
                    FileLog.e(e);
                }
            } catch (Exception e6) {
                e = e6;
                i3 = iMax;
            }
        } else {
            i3 = iMax;
        }
        MessageObject messageObject3 = this.message;
        if (messageObject3 == null || TextUtils.isEmpty(messageObject3.messageOwner.message)) {
            photoSize = null;
        } else {
            photoSize = null;
            CharSequence charSequenceHighlightText2 = AndroidUtilities.highlightText(Emoji.replaceEmoji(this.message.messageOwner.message.replace("\n", " ").replaceAll(" +", " ").trim(), Theme.chat_msgTextPaint.getFontMetricsInt(), false), this.message.highlightedWords, (Theme.ResourcesProvider) null);
            if (charSequenceHighlightText2 != null) {
                this.captionLayout = new StaticLayout(TextUtils.ellipsize(AndroidUtilities.ellipsizeCenterEnd(charSequenceHighlightText2, this.message.highlightedWords.get(0), size2, this.captionTextPaint, 130), this.captionTextPaint, size2, TextUtils.TruncateAt.END), this.captionTextPaint, size2 + AndroidUtilities.dp(4.0f), Layout.Alignment.ALIGN_NORMAL, 1.0f, 0.0f, false);
            }
        }
        StaticLayout staticLayout4 = this.captionLayout;
        if (staticLayout4 != null) {
            int i25 = this.descriptionY;
            this.captionY = i25;
            int lineBottom3 = i25 + staticLayout4.getLineBottom(staticLayout4.getLineCount() - 1) + AndroidUtilities.dp(5.0f);
            this.descriptionY = lineBottom3;
            this.description2Y = lineBottom3;
        }
        if (!this.links.isEmpty()) {
            for (int i26 = 0; i26 < this.linkSpoilers.size(); i26++) {
                this.spoilersPool.addAll((Collection) this.linkSpoilers.get(i26));
            }
            this.linkSpoilers.clear();
            int i27 = 0;
            while (i27 < this.links.size()) {
                try {
                    CharSequence charSequence4 = (CharSequence) this.links.get(i27);
                    CharSequence charSequenceEllipsize = TextUtils.ellipsize(AndroidUtilities.replaceNewLines(SpannableStringBuilder.valueOf(charSequence4)), this.descriptionTextPaint, Math.min((int) Math.ceil(this.descriptionTextPaint.measureText(charSequence4, 0, charSequence4.length())), size2), TextUtils.TruncateAt.MIDDLE);
                    int i28 = i27;
                    try {
                        int i29 = size2;
                        try {
                            try {
                                f2 = f5;
                                i5 = i28;
                                try {
                                    StaticLayout staticLayout5 = new StaticLayout(charSequenceEllipsize, this.descriptionTextPaint, i29, Layout.Alignment.ALIGN_NORMAL, 1.0f, 0.0f, false);
                                    size2 = i29;
                                    try {
                                        this.linkY = this.description2Y;
                                        StaticLayout staticLayout6 = this.descriptionLayout2;
                                        if (staticLayout6 != null && staticLayout6.getLineCount() != 0) {
                                            int i30 = this.linkY;
                                            StaticLayout staticLayout7 = this.descriptionLayout2;
                                            this.linkY = i30 + staticLayout7.getLineBottom(staticLayout7.getLineCount() - 1) + AndroidUtilities.dp(5.0f);
                                        }
                                        if (!this.message.isSpoilersRevealed) {
                                            ArrayList arrayList2 = new ArrayList();
                                            if (charSequenceEllipsize instanceof Spannable) {
                                                SpoilerEffect.addSpoilers(this, staticLayout5, (Spannable) charSequenceEllipsize, this.spoilersPool, arrayList2);
                                            }
                                            this.linkSpoilers.put(i5, arrayList2);
                                        }
                                        this.linkLayout.add(staticLayout5);
                                    } catch (Exception e7) {
                                        e = e7;
                                        FileLog.e(e);
                                    }
                                } catch (Exception e8) {
                                    e = e8;
                                    size2 = i29;
                                }
                            } catch (Exception e9) {
                                e = e9;
                                f2 = f5;
                                size2 = i29;
                                i5 = i28;
                            }
                        } catch (Exception e10) {
                            e = e10;
                            f2 = f5;
                            size2 = i29;
                            i5 = i28;
                        }
                    } catch (Exception e11) {
                        e = e11;
                        f2 = f5;
                        i5 = i28;
                    }
                } catch (Exception e12) {
                    e = e12;
                    f2 = f5;
                    i5 = i27;
                }
                i27 = i5 + 1;
                f5 = f2;
            }
        }
        float f6 = f5;
        int iDp2 = AndroidUtilities.dp(52.0f);
        int size3 = LocaleController.isRTL ? (View.MeasureSpec.getSize(i) - AndroidUtilities.dp(10.0f)) - iDp2 : AndroidUtilities.dp(10.0f);
        this.letterDrawable.setBounds(size3, AndroidUtilities.dp(11.0f), size3 + iDp2, AndroidUtilities.dp(63.0f));
        if (z) {
            TLRPC.PhotoSize closestPhotoSizeWithSize = FileLoader.getClosestPhotoSizeWithSize(this.message.photoThumbs, iDp2, true);
            TLRPC.PhotoSize closestPhotoSizeWithSize2 = FileLoader.getClosestPhotoSizeWithSize(this.message.photoThumbs, 80);
            if (closestPhotoSizeWithSize2 != closestPhotoSizeWithSize) {
                photoSize = closestPhotoSizeWithSize2;
            }
            if (closestPhotoSizeWithSize != null) {
                closestPhotoSizeWithSize.size = -1;
            }
            if (photoSize != null) {
                photoSize.size = -1;
            }
            float f7 = iDp2;
            this.linkImageView.setImageCoords(size3, AndroidUtilities.dp(11.0f), f7, f7);
            FileLoader.getAttachFileName(closestPhotoSizeWithSize);
            Locale locale = Locale.US;
            this.linkImageView.setImage(ImageLocation.getForObject(closestPhotoSizeWithSize, this.message.photoThumbsObject), String.format(locale, "%d_%d", Integer.valueOf(iDp2), Integer.valueOf(iDp2)), ImageLocation.getForObject(photoSize, this.message.photoThumbsObject), String.format(locale, "%d_%d_b", Integer.valueOf(iDp2), Integer.valueOf(iDp2)), 0L, null, this.message, 0);
            i4 = 1;
            this.drawLinkImageView = true;
        } else {
            i4 = 1;
        }
        if (this.viewType == i4) {
            StaticLayout staticLayoutGenerateStaticLayout3 = ChatMessageCell.generateStaticLayout(FilteredSearchView.createFromInfoString(this.message, i4, 2, this.description2TextPaint), this.description2TextPaint, size2, size2, 0, i3);
            this.fromInfoLayout = staticLayoutGenerateStaticLayout3;
            AnimatedEmojiSpan.EmojiGroupedSpans emojiGroupedSpans = this.fromInfoLayoutEmojis;
            Layout[] layoutArr = new Layout[i4];
            lineBottom = 0;
            layoutArr[0] = staticLayoutGenerateStaticLayout3;
            this.fromInfoLayoutEmojis = AnimatedEmojiSpan.update(0, this, emojiGroupedSpans, layoutArr);
        } else {
            lineBottom = 0;
        }
        StaticLayout staticLayout8 = this.titleLayout;
        if (staticLayout8 == null || staticLayout8.getLineCount() == 0) {
            lineBottom2 = lineBottom;
        } else {
            StaticLayout staticLayout9 = this.titleLayout;
            lineBottom2 = staticLayout9.getLineBottom(staticLayout9.getLineCount() - 1) + AndroidUtilities.dp(f6);
        }
        StaticLayout staticLayout10 = this.captionLayout;
        if (staticLayout10 != null && staticLayout10.getLineCount() != 0) {
            StaticLayout staticLayout11 = this.captionLayout;
            lineBottom2 += staticLayout11.getLineBottom(staticLayout11.getLineCount() - 1) + AndroidUtilities.dp(5.0f);
        }
        StaticLayout staticLayout12 = this.descriptionLayout;
        if (staticLayout12 != null && staticLayout12.getLineCount() != 0) {
            StaticLayout staticLayout13 = this.descriptionLayout;
            lineBottom2 += staticLayout13.getLineBottom(staticLayout13.getLineCount() - 1) + AndroidUtilities.dp(5.0f);
        }
        StaticLayout staticLayout14 = this.descriptionLayout2;
        if (staticLayout14 != null && staticLayout14.getLineCount() != 0) {
            StaticLayout staticLayout15 = this.descriptionLayout2;
            lineBottom2 += staticLayout15.getLineBottom(staticLayout15.getLineCount() - 1) + AndroidUtilities.dp(5.0f);
            if (this.descriptionLayout != null) {
                lineBottom2 += AndroidUtilities.dp(10.0f);
            }
        }
        for (int i31 = lineBottom; i31 < this.linkLayout.size(); i31++) {
            StaticLayout staticLayout16 = (StaticLayout) this.linkLayout.get(i31);
            if (staticLayout16.getLineCount() > 0) {
                lineBottom += staticLayout16.getLineBottom(staticLayout16.getLineCount() - 1);
            }
        }
        int lineBottom4 = lineBottom2 + lineBottom;
        if (this.fromInfoLayout != null) {
            this.fromInfoLayoutY = this.linkY + lineBottom + AndroidUtilities.dp(5.0f);
            StaticLayout staticLayout17 = this.fromInfoLayout;
            lineBottom4 += staticLayout17.getLineBottom(staticLayout17.getLineCount() - 1) + AndroidUtilities.dp(5.0f);
        }
        this.checkBox.measure(View.MeasureSpec.makeMeasureSpec(AndroidUtilities.dp(24.0f), TLObject.FLAG_30), View.MeasureSpec.makeMeasureSpec(AndroidUtilities.dp(24.0f), TLObject.FLAG_30));
        setMeasuredDimension(View.MeasureSpec.getSize(i), Math.max(AndroidUtilities.dp(76.0f), lineBottom4 + AndroidUtilities.dp(17.0f)) + (this.needDivider ? 1 : 0));
    }

    public void setLink(MessageObject messageObject, boolean z) {
        this.needDivider = z;
        resetPressedLink();
        this.message = messageObject;
        requestLayout();
    }

    public ImageReceiver getLinkImageView() {
        return this.linkImageView;
    }

    public void setDelegate(SharedLinkCellDelegate sharedLinkCellDelegate) {
        this.delegate = sharedLinkCellDelegate;
    }

    public MessageObject getMessage() {
        return this.message;
    }

    @Override // android.view.ViewGroup, android.view.View
    protected void onDetachedFromWindow() {
        super.onDetachedFromWindow();
        if (this.drawLinkImageView) {
            this.linkImageView.onDetachedFromWindow();
        }
        AnimatedEmojiSpan.release(this, this.fromInfoLayoutEmojis);
        ImageReceiver imageReceiver = this.linkImageView;
        if (imageReceiver != null) {
            imageReceiver.onDetachedFromWindow();
        }
    }

    @Override // android.view.ViewGroup, android.view.View
    protected void onAttachedToWindow() {
        super.onAttachedToWindow();
        if (this.drawLinkImageView) {
            this.linkImageView.onAttachedToWindow();
        }
        this.fromInfoLayoutEmojis = AnimatedEmojiSpan.update(0, this, this.fromInfoLayoutEmojis, this.fromInfoLayout);
        ImageReceiver imageReceiver = this.linkImageView;
        if (imageReceiver != null) {
            imageReceiver.onAttachedToWindow();
        }
    }

    @Override // android.view.View
    public boolean onTouchEvent(MotionEvent motionEvent) {
        boolean z;
        SharedLinkCellDelegate sharedLinkCellDelegate;
        boolean z2;
        int i;
        int i2;
        String str;
        TLRPC.MessageMedia messageMedia;
        if (this.message != null && !this.linkLayout.isEmpty() && (sharedLinkCellDelegate = this.delegate) != null && sharedLinkCellDelegate.canPerformActions()) {
            if (motionEvent.getAction() == 0 || ((this.linkPreviewPressed || this.spoilerPressed != null) && motionEvent.getAction() == 1)) {
                int x = (int) motionEvent.getX();
                int y = (int) motionEvent.getY();
                int i3 = 0;
                int i4 = 0;
                while (true) {
                    if (i3 < this.linkLayout.size()) {
                        StaticLayout staticLayout = (StaticLayout) this.linkLayout.get(i3);
                        if (staticLayout.getLineCount() > 0) {
                            int lineBottom = staticLayout.getLineBottom(staticLayout.getLineCount() - 1);
                            int iDp = AndroidUtilities.dp(LocaleController.isRTL ? 8.0f : AndroidUtilities.leftBaseline);
                            float f = x;
                            float f2 = iDp;
                            if (f >= staticLayout.getLineLeft(0) + f2 && f <= staticLayout.getLineWidth(0) + f2) {
                                int i5 = this.linkY;
                                if (y >= i5 + i4 && y <= i5 + i4 + lineBottom) {
                                    TLRPC.WebPage webPage = null;
                                    if (motionEvent.getAction() == 0) {
                                        this.spoilerPressed = null;
                                        if (this.linkSpoilers.get(i3, null) != null) {
                                            for (SpoilerEffect spoilerEffect : (List) this.linkSpoilers.get(i3)) {
                                                if (spoilerEffect.getBounds().contains(x - iDp, (y - this.linkY) - i4)) {
                                                    resetPressedLink();
                                                    this.spoilerPressed = spoilerEffect;
                                                    this.spoilerTypePressed = 0;
                                                    break;
                                                }
                                            }
                                        }
                                        if (this.spoilerPressed == null && (this.pressedLinkIndex != i3 || this.pressedLink == null || !this.linkPreviewPressed)) {
                                            resetPressedLink();
                                            this.pressedLinkIndex = i3;
                                            LinkSpanDrawable linkSpanDrawable = new LinkSpanDrawable(null, this.resourcesProvider, x - iDp, (y - this.linkY) - i4);
                                            this.pressedLink = linkSpanDrawable;
                                            LinkPath linkPathObtainNewPath = linkSpanDrawable.obtainNewPath();
                                            this.linkPreviewPressed = true;
                                            this.linksCollector.addLink(this.pressedLink);
                                            startCheckLongPress();
                                            try {
                                                linkPathObtainNewPath.setCurrentLayout(staticLayout, 0, f2, this.linkY + i4);
                                                staticLayout.getSelectionPath(0, staticLayout.getText().length(), linkPathObtainNewPath);
                                            } catch (Exception e) {
                                                FileLog.e(e);
                                            }
                                        }
                                    } else if (this.linkPreviewPressed) {
                                        try {
                                            if (this.pressedLinkIndex == 0 && (messageMedia = this.message.messageOwner.media) != null) {
                                                webPage = messageMedia.webpage;
                                            }
                                            if (webPage != null && (str = webPage.embed_url) != null && str.length() != 0) {
                                                this.delegate.needOpenWebView(webPage, this.message);
                                            } else {
                                                this.delegate.onLinkPress(((CharSequence) this.links.get(this.pressedLinkIndex)).toString(), false);
                                            }
                                        } catch (Exception e2) {
                                            FileLog.e(e2);
                                        }
                                        resetPressedLink();
                                    } else {
                                        if (this.spoilerPressed == null) {
                                            z2 = true;
                                            z = false;
                                            break;
                                        }
                                        startSpoilerRipples(x, y, i4);
                                    }
                                    z = true;
                                }
                            }
                            i4 += lineBottom;
                        }
                        i3++;
                    } else {
                        z = false;
                    }
                    z2 = z;
                    break;
                }
                if (motionEvent.getAction() == 0) {
                    int iDp2 = AndroidUtilities.dp(LocaleController.isRTL ? 8.0f : AndroidUtilities.leftBaseline);
                    StaticLayout staticLayout2 = this.descriptionLayout;
                    if (staticLayout2 != null && x >= iDp2 && x <= staticLayout2.getWidth() + iDp2 && y >= (i2 = this.descriptionY) && y <= i2 + this.descriptionLayout.getHeight()) {
                        for (SpoilerEffect spoilerEffect2 : this.descriptionLayoutSpoilers) {
                            if (spoilerEffect2.getBounds().contains(x - iDp2, y - this.descriptionY)) {
                                this.spoilerPressed = spoilerEffect2;
                                this.spoilerTypePressed = 1;
                                z = true;
                                z2 = true;
                                break;
                            }
                        }
                    }
                    StaticLayout staticLayout3 = this.descriptionLayout2;
                    if (staticLayout3 != null && x >= iDp2 && x <= staticLayout3.getWidth() + iDp2 && y >= (i = this.description2Y) && y <= i + this.descriptionLayout2.getHeight()) {
                        for (SpoilerEffect spoilerEffect3 : this.descriptionLayout2Spoilers) {
                            if (spoilerEffect3.getBounds().contains(x - iDp2, y - this.description2Y)) {
                                this.spoilerPressed = spoilerEffect3;
                                this.spoilerTypePressed = 2;
                                z = true;
                                z2 = true;
                                break;
                            }
                        }
                    }
                } else {
                    if (motionEvent.getAction() == 1 && this.spoilerPressed != null) {
                        startSpoilerRipples(x, y, 0);
                        z = true;
                        z2 = true;
                        break;
                    }
                    break;
                }
                if (!z2) {
                    resetPressedLink();
                }
            } else if (motionEvent.getAction() == 3) {
                resetPressedLink();
            }
            return z || super.onTouchEvent(motionEvent);
        }
        resetPressedLink();
        z = false;
        if (z) {
            return true;
        }
    }

    private void startSpoilerRipples(int i, int i2, int i3) {
        int iDp = AndroidUtilities.dp(LocaleController.isRTL ? 8.0f : AndroidUtilities.leftBaseline);
        resetPressedLink();
        this.spoilerPressed.setOnRippleEndCallback(new Runnable() { // from class: org.telegram.ui.Cells.SharedLinkCell$$ExternalSyntheticLambda0
            @Override // java.lang.Runnable
            public final void run() {
                this.f$0.lambda$startSpoilerRipples$1();
            }
        });
        int i4 = i - iDp;
        float fSqrt = (float) Math.sqrt(Math.pow(getWidth(), 2.0d) + Math.pow(getHeight(), 2.0d));
        int i5 = this.spoilerTypePressed;
        if (i5 == 0) {
            float lineBottom = 0.0f;
            for (int i6 = 0; i6 < this.linkLayout.size(); i6++) {
                Layout layout = (Layout) this.linkLayout.get(i6);
                lineBottom += layout.getLineBottom(layout.getLineCount() - 1);
                Iterator it = ((List) this.linkSpoilers.get(i6)).iterator();
                while (it.hasNext()) {
                    ((SpoilerEffect) it.next()).startRipple(i4, ((i2 - getYOffsetForType(0)) - i3) + lineBottom, fSqrt);
                }
            }
        } else if (i5 == 1) {
            Iterator it2 = this.descriptionLayoutSpoilers.iterator();
            while (it2.hasNext()) {
                ((SpoilerEffect) it2.next()).startRipple(i4, i2 - getYOffsetForType(1), fSqrt);
            }
        } else if (i5 == 2) {
            Iterator it3 = this.descriptionLayout2Spoilers.iterator();
            while (it3.hasNext()) {
                ((SpoilerEffect) it3.next()).startRipple(i4, i2 - getYOffsetForType(2), fSqrt);
            }
        }
        for (int i7 = 0; i7 <= 2; i7++) {
            if (i7 != this.spoilerTypePressed) {
                if (i7 == 0) {
                    for (int i8 = 0; i8 < this.linkLayout.size(); i8++) {
                        Layout layout2 = (Layout) this.linkLayout.get(i8);
                        layout2.getLineBottom(layout2.getLineCount() - 1);
                        for (SpoilerEffect spoilerEffect : (List) this.linkSpoilers.get(i8)) {
                            spoilerEffect.startRipple(spoilerEffect.getBounds().centerX(), spoilerEffect.getBounds().centerY(), fSqrt);
                        }
                    }
                } else if (i7 == 1) {
                    for (SpoilerEffect spoilerEffect2 : this.descriptionLayoutSpoilers) {
                        spoilerEffect2.startRipple(spoilerEffect2.getBounds().centerX(), spoilerEffect2.getBounds().centerY(), fSqrt);
                    }
                } else if (i7 == 2) {
                    for (SpoilerEffect spoilerEffect3 : this.descriptionLayout2Spoilers) {
                        spoilerEffect3.startRipple(spoilerEffect3.getBounds().centerX(), spoilerEffect3.getBounds().centerY(), fSqrt);
                    }
                }
            }
        }
        this.spoilerTypePressed = -1;
        this.spoilerPressed = null;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$startSpoilerRipples$1() {
        post(new Runnable() { // from class: org.telegram.ui.Cells.SharedLinkCell$$ExternalSyntheticLambda1
            @Override // java.lang.Runnable
            public final void run() {
                this.f$0.lambda$startSpoilerRipples$0();
            }
        });
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$startSpoilerRipples$0() {
        this.message.isSpoilersRevealed = true;
        this.linkSpoilers.clear();
        this.descriptionLayoutSpoilers.clear();
        this.descriptionLayout2Spoilers.clear();
        invalidate();
    }

    private int getYOffsetForType(int i) {
        if (i == 1) {
            return this.descriptionY;
        }
        if (i != 2) {
            return this.linkY;
        }
        return this.description2Y;
    }

    public String getLink(int i) {
        if (i < 0 || i >= this.links.size()) {
            return null;
        }
        return ((CharSequence) this.links.get(i)).toString();
    }

    protected void resetPressedLink() {
        this.linksCollector.clear(true);
        this.pressedLinkIndex = -1;
        this.pressedLink = null;
        this.linkPreviewPressed = false;
        cancelCheckLongPress();
        invalidate();
    }

    public void setChecked(boolean z, boolean z2) {
        if (this.checkBox.getVisibility() != 0) {
            this.checkBox.setVisibility(0);
        }
        this.checkBox.setChecked(z, z2);
    }

    @Override // android.view.View
    protected void onDraw(Canvas canvas) {
        Canvas canvas2 = canvas;
        if (this.viewType == 1) {
            this.description2TextPaint.setColor(Theme.getColor(Theme.key_windowBackgroundWhiteGrayText3, this.resourcesProvider));
        }
        if (this.dateLayout != null) {
            canvas2.save();
            canvas2.translate(AndroidUtilities.dp(LocaleController.isRTL ? 8.0f : AndroidUtilities.leftBaseline) + (LocaleController.isRTL ? 0 : this.dateLayoutX), this.titleY);
            this.dateLayout.draw(canvas2);
            canvas2.restore();
        }
        if (this.titleLayout != null) {
            canvas2.save();
            float fDp = AndroidUtilities.dp(LocaleController.isRTL ? 8.0f : AndroidUtilities.leftBaseline);
            if (LocaleController.isRTL) {
                StaticLayout staticLayout = this.dateLayout;
                fDp += staticLayout == null ? 0.0f : staticLayout.getWidth() + AndroidUtilities.dp(4.0f);
            }
            canvas2.translate(fDp, this.titleY);
            this.titleLayout.draw(canvas2);
            canvas2.restore();
        }
        if (this.captionLayout != null) {
            this.captionTextPaint.setColor(Theme.getColor(Theme.key_windowBackgroundWhiteBlackText, this.resourcesProvider));
            canvas2.save();
            canvas2.translate(AndroidUtilities.dp(LocaleController.isRTL ? 8.0f : AndroidUtilities.leftBaseline), this.captionY);
            this.captionLayout.draw(canvas2);
            canvas2.restore();
        }
        if (this.descriptionLayout != null) {
            this.descriptionTextPaint.setColor(Theme.getColor(Theme.key_windowBackgroundWhiteBlackText, this.resourcesProvider));
            canvas2.save();
            canvas2.translate(AndroidUtilities.dp(LocaleController.isRTL ? 8.0f : AndroidUtilities.leftBaseline), this.descriptionY);
            SpoilerEffect.renderWithRipple(this, false, this.descriptionTextPaint.getColor(), -AndroidUtilities.dp(2.0f), this.patchedDescriptionLayout, 0, this.descriptionLayout, this.descriptionLayoutSpoilers, canvas2, false);
            canvas2.restore();
        }
        if (this.descriptionLayout2 != null) {
            this.descriptionTextPaint.setColor(Theme.getColor(Theme.key_windowBackgroundWhiteBlackText, this.resourcesProvider));
            canvas2.save();
            canvas2.translate(AndroidUtilities.dp(LocaleController.isRTL ? 8.0f : AndroidUtilities.leftBaseline), this.description2Y);
            SpoilerEffect.renderWithRipple(this, false, this.descriptionTextPaint.getColor(), -AndroidUtilities.dp(2.0f), this.patchedDescriptionLayout2, 0, this.descriptionLayout2, this.descriptionLayout2Spoilers, canvas2, false);
            canvas2.restore();
        }
        if (!this.linkLayout.isEmpty()) {
            this.descriptionTextPaint.setColor(Theme.getColor(Theme.key_windowBackgroundWhiteLinkText, this.resourcesProvider));
            int lineBottom = 0;
            for (int i = 0; i < this.linkLayout.size(); i++) {
                StaticLayout staticLayout2 = (StaticLayout) this.linkLayout.get(i);
                List list = (List) this.linkSpoilers.get(i);
                if (staticLayout2.getLineCount() > 0) {
                    canvas2.save();
                    canvas2.translate(AndroidUtilities.dp(LocaleController.isRTL ? 8.0f : AndroidUtilities.leftBaseline), this.linkY + lineBottom);
                    this.path.rewind();
                    if (list != null) {
                        Iterator it = list.iterator();
                        while (it.hasNext()) {
                            Rect bounds = ((SpoilerEffect) it.next()).getBounds();
                            this.path.addRect(bounds.left, bounds.top, bounds.right, bounds.bottom, Path.Direction.CW);
                        }
                    }
                    canvas2.save();
                    canvas2.clipPath(this.path, Region.Op.DIFFERENCE);
                    staticLayout2.draw(canvas2);
                    canvas2.restore();
                    canvas2.save();
                    canvas2.clipPath(this.path);
                    this.path.rewind();
                    if (list != null && !list.isEmpty()) {
                        ((SpoilerEffect) list.get(0)).getRipplePath(this.path);
                    }
                    canvas2.clipPath(this.path);
                    staticLayout2.draw(canvas2);
                    canvas2.restore();
                    if (list != null) {
                        Iterator it2 = list.iterator();
                        while (it2.hasNext()) {
                            ((SpoilerEffect) it2.next()).draw(canvas2);
                        }
                    }
                    canvas2.restore();
                    lineBottom += staticLayout2.getLineBottom(staticLayout2.getLineCount() - 1);
                }
            }
            if (this.linksCollector.draw(canvas2)) {
                invalidate();
            }
        }
        if (this.fromInfoLayout != null) {
            canvas2.save();
            canvas2.translate(AndroidUtilities.dp(LocaleController.isRTL ? 8.0f : AndroidUtilities.leftBaseline), this.fromInfoLayoutY);
            this.fromInfoLayout.draw(canvas2);
            AnimatedEmojiSpan.drawAnimatedEmojis(canvas, this.fromInfoLayout, this.fromInfoLayoutEmojis, 0.0f, null, 0.0f, 0.0f, 0.0f, 1.0f);
            canvas2 = canvas;
            canvas2.restore();
        }
        this.letterDrawable.draw(canvas2);
        if (this.drawLinkImageView) {
            this.linkImageView.draw(canvas2);
        }
        if (this.needDivider) {
            if (LocaleController.isRTL) {
                canvas2.drawLine(0.0f, getMeasuredHeight() - 1, getMeasuredWidth() - AndroidUtilities.dp(AndroidUtilities.leftBaseline), getMeasuredHeight() - 1, Theme.dividerPaint);
            } else {
                canvas.drawLine(AndroidUtilities.dp(AndroidUtilities.leftBaseline), getMeasuredHeight() - 1, getMeasuredWidth(), getMeasuredHeight() - 1, Theme.dividerPaint);
            }
        }
    }

    @Override // android.view.View
    public void onInitializeAccessibilityNodeInfo(AccessibilityNodeInfo accessibilityNodeInfo) {
        super.onInitializeAccessibilityNodeInfo(accessibilityNodeInfo);
        StringBuilder sb = new StringBuilder();
        StaticLayout staticLayout = this.titleLayout;
        if (staticLayout != null) {
            sb.append(staticLayout.getText());
        }
        if (this.descriptionLayout != null) {
            sb.append(", ");
            sb.append(this.descriptionLayout.getText());
        }
        if (this.descriptionLayout2 != null) {
            sb.append(", ");
            sb.append(this.descriptionLayout2.getText());
        }
        accessibilityNodeInfo.setText(sb.toString());
        if (this.checkBox.isChecked()) {
            accessibilityNodeInfo.setChecked(true);
            accessibilityNodeInfo.setCheckable(true);
        }
    }
}
