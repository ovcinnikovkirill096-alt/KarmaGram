package org.telegram.ui.Cells;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.text.Layout;
import android.text.StaticLayout;
import android.text.TextUtils;
import android.util.Property;
import android.view.MotionEvent;
import android.view.View;
import android.view.accessibility.AccessibilityNodeInfo;
import android.view.animation.AccelerateInterpolator;
import android.widget.FrameLayout;
import java.io.File;
import java.util.ArrayList;
import java.util.Locale;
import okhttp3.internal.url._UrlKt;
import org.telegram.messenger.AndroidUtilities;
import org.telegram.messenger.DocumentObject;
import org.telegram.messenger.DownloadController;
import org.telegram.messenger.Emoji;
import org.telegram.messenger.FileLoader;
import org.telegram.messenger.FileLog;
import org.telegram.messenger.ImageLoader;
import org.telegram.messenger.ImageLocation;
import org.telegram.messenger.ImageReceiver;
import org.telegram.messenger.LocaleController;
import org.telegram.messenger.MediaController;
import org.telegram.messenger.MessageObject;
import org.telegram.messenger.MessagesController;
import org.telegram.messenger.R;
import org.telegram.messenger.SharedConfig;
import org.telegram.messenger.SvgHelper;
import org.telegram.messenger.UserConfig;
import org.telegram.messenger.Utilities;
import org.telegram.messenger.WebFile;
import org.telegram.tgnet.TLRPC;
import org.telegram.ui.ActionBar.Theme;
import org.telegram.ui.Components.AnimationProperties;
import org.telegram.ui.Components.ButtonBounce;
import org.telegram.ui.Components.CheckBox2;
import org.telegram.ui.Components.LayoutHelper;
import org.telegram.ui.Components.LetterDrawable;
import org.telegram.ui.Components.RadialProgress2;
import org.telegram.ui.PhotoViewer;
import org.webrtc.MediaStreamTrack;

public class ContextLinkCell extends FrameLayout implements DownloadController.FileDownloadProgressListener {
    private static AccelerateInterpolator interpolator = new AccelerateInterpolator(0.5f);
    public final Property IMAGE_SCALE;
    private int TAG;
    private AnimatorSet animator;
    private Paint backgroundPaint;
    private ButtonBounce buttonBounce;
    private boolean buttonPressed;
    private int buttonState;
    File cacheFile;
    private boolean canPreviewGif;
    private CheckBox2 checkBox;
    private int currentAccount;
    private int currentDate;
    private MessageObject currentMessageObject;
    private TLRPC.PhotoSize currentPhotoObject;
    private ContextLinkCellDelegate delegate;
    private StaticLayout descriptionLayout;
    private int descriptionY;
    private TLRPC.Document documentAttach;
    private int documentAttachType;
    private boolean drawLinkImageView;
    boolean fileExist;
    String fileName;
    private float imageScale;
    private TLRPC.User inlineBot;
    private TLRPC.BotInlineResult inlineResult;
    private boolean isForceGif;
    private boolean isKeyboard;
    private LetterDrawable letterDrawable;
    private ImageReceiver linkImageView;
    private StaticLayout linkLayout;
    private int linkY;
    private boolean mediaWebpage;
    private boolean needDivider;
    private boolean needShadow;
    private Object parentObject;
    private TLRPC.Photo photoAttach;
    private RadialProgress2 radialProgress;
    int resolveFileNameId;
    boolean resolvingFileName;
    private Theme.ResourcesProvider resourcesProvider;
    private boolean scaled;
    private StaticLayout titleLayout;
    private int titleY;

    public interface ContextLinkCellDelegate {
        void didPressedImage(ContextLinkCell contextLinkCell);
    }

    @Override // org.telegram.messenger.DownloadController.FileDownloadProgressListener
    public void onProgressUpload(String str, long j, long j2, boolean z) {
    }

    public ContextLinkCell(Context context) {
        this(context, false, null);
    }

    public ContextLinkCell(Context context, boolean z, Theme.ResourcesProvider resourcesProvider) {
        super(context);
        this.currentAccount = UserConfig.selectedAccount;
        this.titleY = AndroidUtilities.dp(7.0f);
        this.descriptionY = AndroidUtilities.dp(27.0f);
        this.cacheFile = null;
        this.imageScale = 1.0f;
        this.IMAGE_SCALE = new AnimationProperties.FloatProperty("animationValue") { // from class: org.telegram.ui.Cells.ContextLinkCell.2
            @Override // org.telegram.ui.Components.AnimationProperties.FloatProperty
            public void setValue(ContextLinkCell contextLinkCell, float f) {
                ContextLinkCell.this.imageScale = f;
                ContextLinkCell.this.invalidate();
            }

            @Override // android.util.Property
            public Float get(ContextLinkCell contextLinkCell) {
                return Float.valueOf(ContextLinkCell.this.imageScale);
            }
        };
        this.resourcesProvider = resourcesProvider;
        ImageReceiver imageReceiver = new ImageReceiver(this);
        this.linkImageView = imageReceiver;
        imageReceiver.setAllowLoadingOnAttachedOnly(true);
        this.linkImageView.setLayerNum(1);
        this.linkImageView.setUseSharedAnimationQueue(true);
        this.letterDrawable = new LetterDrawable(resourcesProvider, 0);
        this.radialProgress = new RadialProgress2(this);
        this.TAG = DownloadController.getInstance(this.currentAccount).generateObserverTag();
        setFocusable(true);
        if (z) {
            Paint paint = new Paint();
            this.backgroundPaint = paint;
            int i = Theme.key_sharedMedia_photoPlaceholder;
            paint.setColor(Theme.getColor(i, resourcesProvider));
            CheckBox2 checkBox2 = new CheckBox2(context, 21, resourcesProvider);
            this.checkBox = checkBox2;
            checkBox2.setVisibility(4);
            this.checkBox.setColor(-1, i, Theme.key_checkboxCheck);
            this.checkBox.setDrawUnchecked(false);
            this.checkBox.setDrawBackgroundAsArc(1);
            addView(this.checkBox, LayoutHelper.createFrame(24, 24.0f, 53, 0.0f, 1.0f, 1.0f, 0.0f));
        }
        setWillNotDraw(false);
    }

    public void allowButtonBounce(boolean z) {
        if (z != (this.buttonBounce != null)) {
            this.buttonBounce = z ? new ButtonBounce(this, 1.0f, 3.0f).setReleaseDelay(120L) : null;
        }
    }

    /* JADX WARN: Code duplicated, block: B:108:0x024a  */
    /* JADX WARN: Code duplicated, block: B:110:0x024d  */
    /* JADX WARN: Code duplicated, block: B:112:0x0255  */
    /* JADX WARN: Code duplicated, block: B:114:0x025a  */
    /* JADX WARN: Code duplicated, block: B:126:0x02b3  */
    /* JADX WARN: Code duplicated, block: B:128:0x02b7  */
    /* JADX WARN: Code duplicated, block: B:129:0x02be  */
    /* JADX WARN: Code duplicated, block: B:132:0x02c5  */
    /* JADX WARN: Code duplicated, block: B:135:0x02d1  */
    /* JADX WARN: Code duplicated, block: B:137:0x02df  */
    /* JADX WARN: Code duplicated, block: B:142:0x02ec  */
    /* JADX WARN: Code duplicated, block: B:144:0x02f1 A[ADDED_TO_REGION] */
    /* JADX WARN: Code duplicated, block: B:145:0x02f3  */
    /* JADX WARN: Code duplicated, block: B:147:0x02f7 A[DONT_INVERT] */
    /* JADX WARN: Code duplicated, block: B:148:0x02f9  */
    /* JADX WARN: Code duplicated, block: B:150:0x0301  */
    /* JADX WARN: Code duplicated, block: B:152:0x0305  */
    /* JADX WARN: Code duplicated, block: B:155:0x0316 A[ADDED_TO_REGION] */
    /* JADX WARN: Code duplicated, block: B:156:0x0318  */
    /* JADX WARN: Code duplicated, block: B:159:0x0321  */
    /* JADX WARN: Code duplicated, block: B:165:0x032f  */
    /* JADX WARN: Code duplicated, block: B:167:0x0335  */
    /* JADX WARN: Code duplicated, block: B:169:0x0343  */
    /* JADX WARN: Code duplicated, block: B:171:0x0360  */
    /* JADX WARN: Code duplicated, block: B:176:0x038c  */
    /* JADX WARN: Code duplicated, block: B:177:0x03b5  */
    /* JADX WARN: Code duplicated, block: B:180:0x03c4  */
    /* JADX WARN: Code duplicated, block: B:181:0x03c6  */
    /* JADX WARN: Code duplicated, block: B:184:0x03cf  */
    /* JADX WARN: Code duplicated, block: B:186:0x03d3  */
    /* JADX WARN: Code duplicated, block: B:188:0x03dd  */
    /* JADX WARN: Code duplicated, block: B:190:0x03f3  */
    /* JADX WARN: Code duplicated, block: B:193:0x03f8  */
    /* JADX WARN: Code duplicated, block: B:195:0x041a  */
    /* JADX WARN: Code duplicated, block: B:197:0x0424  */
    /* JADX WARN: Code duplicated, block: B:200:0x0436  */
    /* JADX WARN: Code duplicated, block: B:203:0x043b  */
    /* JADX WARN: Code duplicated, block: B:206:0x0465 A[DONT_INVERT] */
    /* JADX WARN: Code duplicated, block: B:207:0x0467  */
    /* JADX WARN: Code duplicated, block: B:208:0x0487  */
    /* JADX WARN: Code duplicated, block: B:209:0x04a7  */
    /* JADX WARN: Code duplicated, block: B:211:0x04ab  */
    /* JADX WARN: Code duplicated, block: B:213:0x04bb A[DONT_INVERT] */
    /* JADX WARN: Code duplicated, block: B:214:0x04bd  */
    /* JADX WARN: Code duplicated, block: B:215:0x04db  */
    /* JADX WARN: Code duplicated, block: B:216:0x0502  */
    /* JADX WARN: Code duplicated, block: B:218:0x0507 A[DONT_INVERT] */
    /* JADX WARN: Code duplicated, block: B:219:0x0509  */
    /* JADX WARN: Code duplicated, block: B:220:0x0525  */
    /* JADX WARN: Code duplicated, block: B:221:0x0546  */
    /* JADX WARN: Code duplicated, block: B:222:0x0569  */
    /* JADX WARN: Code duplicated, block: B:224:0x056c  */
    /* JADX WARN: Code duplicated, block: B:225:0x0586  */
    /* JADX WARN: Code duplicated, block: B:228:0x05a5  */
    /* JADX WARN: Code duplicated, block: B:230:0x05a9  */
    /* JADX WARN: Code duplicated, block: B:236:0x05cd  */
    /* JADX WARN: Code duplicated, block: B:238:0x05d3  */
    /* JADX WARN: Code duplicated, block: B:240:0x060a  */
    /* JADX WARN: Code duplicated, block: B:242:0x060e  */
    /* JADX WARN: Code duplicated, block: B:245:0x0620  */
    /* JADX WARN: Code duplicated, block: B:248:0x0625  */
    /* JADX WARN: Code duplicated, block: B:253:0x063b  */
    /* JADX WARN: Code duplicated, block: B:258:0x067a  */
    /* JADX WARN: Code duplicated, block: B:259:0x0685  */
    /* JADX WARN: Code duplicated, block: B:262:0x06aa A[ADDED_TO_REGION] */
    /* JADX WARN: Code duplicated, block: B:263:0x06ac  */
    /* JADX WARN: Code duplicated, block: B:266:0x06d4  */
    /* JADX WARN: Code duplicated, block: B:272:0x0146 A[EXC_TOP_SPLITTER, SYNTHETIC] */
    /* JADX WARN: Code duplicated, block: B:280:0x02ec A[SYNTHETIC] */
    /* JADX WARN: Code duplicated, block: B:281:0x02e7 A[ADDED_TO_REGION, REMOVE, SYNTHETIC] */
    /* JADX WARN: Code duplicated, block: B:283:? A[RETURN, SYNTHETIC] */
    /* JADX WARN: Code duplicated, block: B:60:0x017f  */
    /* JADX WARN: Code duplicated, block: B:63:0x018d  */
    /* JADX WARN: Code duplicated, block: B:65:0x0193  */
    /* JADX WARN: Code duplicated, block: B:78:0x01cc  */
    /* JADX WARN: Code duplicated, block: B:79:0x01d7  */
    /* JADX WARN: Code duplicated, block: B:81:0x01db  */
    /* JADX WARN: Code duplicated, block: B:89:0x01f9  */
    /* JADX WARN: Code duplicated, block: B:91:0x01ff  */
    /* JADX WARN: Instruction removed from duplicated block: B:165:0x032f, please report this as an issue */
    /* JADX WARN: Instruction removed from duplicated block: B:176:0x038c, please report this as an issue */
    /* JADX WARN: Multi-variable type inference failed */
    /* JADX WARN: Type inference failed for: r0v126 */
    /* JADX WARN: Type inference failed for: r0v127 */
    /* JADX WARN: Type inference failed for: r0v128 */
    /* JADX WARN: Type inference failed for: r0v129 */
    /* JADX WARN: Type inference failed for: r0v130 */
    /* JADX WARN: Type inference failed for: r0v131 */
    /* JADX WARN: Type inference failed for: r0v132 */
    /* JADX WARN: Type inference failed for: r0v34, types: [org.telegram.messenger.ImageReceiver] */
    /* JADX WARN: Type inference failed for: r0v37, types: [org.telegram.messenger.ImageReceiver] */
    /* JADX WARN: Type inference failed for: r0v49 */
    /* JADX WARN: Type inference failed for: r0v50, types: [int] */
    /* JADX WARN: Type inference failed for: r0v51, types: [int] */
    /* JADX WARN: Type inference failed for: r0v52, types: [int] */
    /* JADX WARN: Type inference failed for: r11v10 */
    /* JADX WARN: Type inference failed for: r11v11, types: [boolean] */
    /* JADX WARN: Type inference failed for: r11v14 */
    /* JADX WARN: Type inference failed for: r2v15, types: [org.telegram.messenger.ImageReceiver] */
    /* JADX WARN: Type inference failed for: r2v74, types: [int] */
    /* JADX WARN: Type inference failed for: r3v14 */
    /* JADX WARN: Type inference failed for: r3v15 */
    /* JADX WARN: Type inference failed for: r3v16, types: [boolean] */
    /* JADX WARN: Type inference failed for: r3v35 */
    /* JADX WARN: Type inference failed for: r3v45, types: [int] */
    /* JADX WARN: Type inference failed for: r3v47, types: [int] */
    /* JADX WARN: Type inference failed for: r3v54 */
    /* JADX WARN: Type inference failed for: r3v55 */
    /* JADX WARN: Type inference failed for: r3v56 */
    /* JADX WARN: Type inference failed for: r5v1 */
    /* JADX WARN: Type inference failed for: r5v2, types: [boolean, int] */
    /* JADX WARN: Type inference failed for: r5v35 */
    /* JADX WARN: Type inference failed for: r5v36 */
    /* JADX WARN: Type inference failed for: r5v37 */
    /* JADX WARN: Type inference fix 'apply assigned field type' failed
    java.lang.UnsupportedOperationException: ArgType.getObject(), call class: class jadx.core.dex.instructions.args.ArgType$UnknownArg
    	at jadx.core.dex.instructions.args.ArgType.getObject(ArgType.java:596)
    	at jadx.core.dex.attributes.nodes.ClassTypeVarsAttr.getTypeVarsMapFor(ClassTypeVarsAttr.java:35)
    	at jadx.core.dex.nodes.utils.TypeUtils.replaceClassGenerics(TypeUtils.java:177)
    	at jadx.core.dex.visitors.typeinference.FixTypesVisitor.insertExplicitUseCast(FixTypesVisitor.java:397)
    	at jadx.core.dex.visitors.typeinference.FixTypesVisitor.tryFieldTypeWithNewCasts(FixTypesVisitor.java:359)
    	at jadx.core.dex.visitors.typeinference.FixTypesVisitor.applyFieldType(FixTypesVisitor.java:309)
    	at jadx.core.dex.visitors.typeinference.FixTypesVisitor.visit(FixTypesVisitor.java:94)
     */
    @Override // android.widget.FrameLayout, android.view.View
    protected void onMeasure(int i, int i2) {
        ArrayList arrayList;
        ArrayList arrayList2;
        float f;
        float f2;
        ?? r5;
        TLRPC.Document document;
        TLRPC.BotInlineResult botInlineResult;
        TLRPC.PhotoSize closestPhotoSizeWithSize;
        String str;
        TLRPC.BotInlineResult botInlineResult2;
        String strFormapMapUrl;
        WebFile webFileCreateWithWebDocument;
        int iDp;
        int i3;
        TLRPC.PhotoSize photoSize;
        TLRPC.BotInlineResult botInlineResult3;
        String str2;
        String str3;
        ?? r11;
        int i4;
        SvgHelper.SvgDrawable svgThumb;
        TLRPC.Document document2;
        ?? r3;
        TLRPC.Document document3;
        TLRPC.VideoSize documentVideoThumb;
        ImageLocation forDocument;
        int iDp2;
        String str4;
        String str5;
        String str6;
        ?? r4;
        StaticLayout staticLayout;
        ?? lineBottom;
        StaticLayout staticLayout2;
        ?? lineBottom2;
        StaticLayout staticLayout3;
        ?? lineBottom3;
        int iDp3;
        int iDp4;
        int i5;
        CheckBox2 checkBox2;
        int size;
        int i6;
        TLRPC.DocumentAttribute documentAttribute;
        TLRPC.TL_webDocument tL_webDocument;
        TLRPC.WebDocument webDocument;
        String str7;
        TLRPC.BotInlineResult botInlineResult4;
        boolean z;
        char c;
        int i7;
        char c2;
        boolean z2;
        String str8;
        int i8 = 0;
        this.drawLinkImageView = false;
        this.descriptionLayout = null;
        this.titleLayout = null;
        this.linkLayout = null;
        this.currentPhotoObject = null;
        this.linkY = AndroidUtilities.dp(27.0f);
        if (this.inlineResult == null && this.documentAttach == null) {
            setMeasuredDimension(AndroidUtilities.dp(100.0f), AndroidUtilities.dp(100.0f));
            return;
        }
        int size2 = View.MeasureSpec.getSize(i);
        float f3 = 8.0f;
        int iDp5 = (size2 - AndroidUtilities.dp(AndroidUtilities.leftBaseline)) - AndroidUtilities.dp(8.0f);
        if (this.documentAttach != null) {
            arrayList2 = new ArrayList(this.documentAttach.thumbs);
        } else {
            TLRPC.BotInlineResult botInlineResult5 = this.inlineResult;
            if (botInlineResult5 == null || botInlineResult5.photo == null) {
                arrayList = null;
            } else {
                arrayList2 = new ArrayList(this.inlineResult.photo.sizes);
            }
            f = 1.0f;
            if (!this.mediaWebpage || (botInlineResult4 = this.inlineResult) == null) {
                i8 = 0;
                f2 = 100.0f;
                f3 = 8.0f;
                f = 1.0f;
                r5 = 1;
            } else {
                String str9 = botInlineResult4.title;
                if (str9 != null) {
                    try {
                        f2 = 100.0f;
                        try {
                            this.titleLayout = new StaticLayout(TextUtils.ellipsize(Emoji.replaceEmoji(this.inlineResult.title.replace('\n', ' '), Theme.chat_contextResult_titleTextPaint.getFontMetricsInt(), false), Theme.chat_contextResult_titleTextPaint, Math.min((int) Math.ceil(Theme.chat_contextResult_titleTextPaint.measureText(str9)), iDp5), TextUtils.TruncateAt.END), Theme.chat_contextResult_titleTextPaint, iDp5 + AndroidUtilities.dp(4.0f), Layout.Alignment.ALIGN_NORMAL, 1.0f, 0.0f, false);
                        } catch (Exception e) {
                            e = e;
                            FileLog.e(e);
                        }
                    } catch (Exception e2) {
                        e = e2;
                        f2 = 100.0f;
                    }
                    this.letterDrawable.setTitle(this.inlineResult.title);
                } else {
                    f2 = 100.0f;
                }
                String str10 = this.inlineResult.description;
                if (str10 != null) {
                    try {
                        z = true;
                        try {
                            i7 = iDp5;
                            i8 = 0;
                            c2 = '\n';
                            f3 = 8.0f;
                            f = 1.0f;
                            c = ' ';
                            try {
                                StaticLayout staticLayoutGenerateStaticLayout = ChatMessageCell.generateStaticLayout(Emoji.replaceEmoji(str10, Theme.chat_contextResult_descriptionTextPaint.getFontMetricsInt(), false), Theme.chat_contextResult_descriptionTextPaint, i7, iDp5, 0, 3);
                                this.descriptionLayout = staticLayoutGenerateStaticLayout;
                                z2 = true;
                                if (staticLayoutGenerateStaticLayout.getLineCount() > 0) {
                                    int i9 = this.descriptionY;
                                    StaticLayout staticLayout4 = this.descriptionLayout;
                                    this.linkY = i9 + staticLayout4.getLineBottom(staticLayout4.getLineCount() - 1) + AndroidUtilities.dp(1.0f);
                                    z2 = true;
                                }
                            } catch (Exception e3) {
                                e = e3;
                                FileLog.e(e);
                                z2 = z;
                            }
                        } catch (Exception e4) {
                            e = e4;
                            z = true;
                            c = ' ';
                            i7 = iDp5;
                            c2 = '\n';
                            FileLog.e(e);
                            z2 = z;
                            str8 = this.inlineResult.url;
                            r5 = z2;
                            if (str8 != null) {
                                try {
                                    this.linkLayout = new StaticLayout(TextUtils.ellipsize(this.inlineResult.url.replace(c2, c), Theme.chat_contextResult_descriptionTextPaint, Math.min((int) Math.ceil(Theme.chat_contextResult_descriptionTextPaint.measureText(str8)), i7), TextUtils.TruncateAt.MIDDLE), Theme.chat_contextResult_descriptionTextPaint, i7, Layout.Alignment.ALIGN_NORMAL, 1.0f, 0.0f, false);
                                    r5 = z2;
                                } catch (Exception e5) {
                                    FileLog.e(e5);
                                    r5 = z2;
                                }
                            }
                            document = this.documentAttach;
                            if (document != null) {
                                if (!this.isForceGif) {
                                    this.currentPhotoObject = FileLoader.getClosestPhotoSizeWithSize(this.documentAttach.thumbs, 90);
                                } else {
                                    this.currentPhotoObject = FileLoader.getClosestPhotoSizeWithSize(this.documentAttach.thumbs, 90);
                                }
                                botInlineResult2 = this.inlineResult;
                                if (botInlineResult2 != null) {
                                    if (botInlineResult2.content instanceof TLRPC.TL_webDocument) {
                                        tL_webDocument = null;
                                    } else {
                                        tL_webDocument = null;
                                    }
                                    if (tL_webDocument == null) {
                                        webDocument = this.inlineResult.thumb;
                                        if (webDocument instanceof TLRPC.TL_webDocument) {
                                            tL_webDocument = (TLRPC.TL_webDocument) webDocument;
                                        }
                                    }
                                    if (tL_webDocument != null) {
                                        strFormapMapUrl = null;
                                        webFileCreateWithWebDocument = null;
                                    } else {
                                        strFormapMapUrl = null;
                                        webFileCreateWithWebDocument = null;
                                    }
                                    if (tL_webDocument != null) {
                                        webFileCreateWithWebDocument = WebFile.createWithWebDocument(tL_webDocument);
                                    }
                                } else {
                                    strFormapMapUrl = null;
                                    webFileCreateWithWebDocument = null;
                                }
                                if (this.documentAttach != null) {
                                    i6 = i8;
                                    while (true) {
                                        if (i6 < this.documentAttach.attributes.size()) {
                                            documentAttribute = this.documentAttach.attributes.get(i6);
                                            if (documentAttribute instanceof TLRPC.TL_documentAttributeImageSize) {
                                            }
                                            iDp = documentAttribute.w;
                                            i3 = documentAttribute.h;
                                        } else {
                                            iDp = i8;
                                            i3 = iDp;
                                        }
                                        i6++;
                                    }
                                } else {
                                    iDp = i8;
                                    i3 = iDp;
                                }
                                if (iDp != 0) {
                                    photoSize = this.currentPhotoObject;
                                    if (photoSize != null) {
                                        if (closestPhotoSizeWithSize != null) {
                                            closestPhotoSizeWithSize.size = -1;
                                        }
                                        iDp = photoSize.w;
                                        i3 = photoSize.h;
                                    } else {
                                        botInlineResult3 = this.inlineResult;
                                        if (botInlineResult3 != null) {
                                            int[] inlineResultWidthAndHeight = MessageObject.getInlineResultWidthAndHeight(botInlineResult3);
                                            int i10 = inlineResultWidthAndHeight[i8];
                                            i3 = inlineResultWidthAndHeight[r5];
                                            iDp = i10;
                                        }
                                    }
                                } else {
                                    photoSize = this.currentPhotoObject;
                                    if (photoSize != null) {
                                        if (closestPhotoSizeWithSize != null) {
                                            closestPhotoSizeWithSize.size = -1;
                                        }
                                        iDp = photoSize.w;
                                        i3 = photoSize.h;
                                    } else {
                                        botInlineResult3 = this.inlineResult;
                                        if (botInlineResult3 != null) {
                                            int[] inlineResultWidthAndHeight2 = MessageObject.getInlineResultWidthAndHeight(botInlineResult3);
                                            int i11 = inlineResultWidthAndHeight2[i8];
                                            i3 = inlineResultWidthAndHeight2[r5];
                                            iDp = i11;
                                        }
                                    }
                                }
                                if (iDp != 0) {
                                    iDp = AndroidUtilities.dp(80.0f);
                                    i3 = iDp;
                                } else {
                                    iDp = AndroidUtilities.dp(80.0f);
                                    i3 = iDp;
                                }
                                if (this.documentAttach != null) {
                                    if (this.mediaWebpage) {
                                        iDp2 = (int) (iDp / (i3 / AndroidUtilities.dp(80.0f)));
                                        if (this.documentAttachType == 2) {
                                            Locale locale = Locale.US;
                                            Object[] objArr = new Object[2];
                                            objArr[i8] = Integer.valueOf((int) (iDp2 / AndroidUtilities.density));
                                            objArr[r5] = 80;
                                            str6 = String.format(locale, "%d_%d_b", objArr);
                                            if (SharedConfig.isAutoplayGifs()) {
                                            }
                                            str3 = str6;
                                            str2 = str3;
                                        } else {
                                            Locale locale2 = Locale.US;
                                            Object[] objArr2 = new Object[2];
                                            objArr2[i8] = Integer.valueOf((int) (iDp2 / AndroidUtilities.density));
                                            objArr2[r5] = 80;
                                            str4 = String.format(locale2, "%d_%d", objArr2);
                                            str5 = str4 + "_b";
                                        }
                                        str3 = str4;
                                        str2 = str5;
                                    } else {
                                        str2 = "52_52_b";
                                        str3 = "52_52";
                                    }
                                    ?? r2 = this.linkImageView;
                                    if (this.documentAttachType == 6) {
                                        r11 = r5;
                                    } else {
                                        r11 = i8;
                                    }
                                    r2.setAspectFit(r11);
                                    if (this.documentAttachType == 2) {
                                        document3 = this.documentAttach;
                                        if (document3 != null) {
                                            documentVideoThumb = MessageObject.getDocumentVideoThumb(document3);
                                            if (documentVideoThumb != null) {
                                                ImageReceiver imageReceiver = this.linkImageView;
                                                ImageLocation forDocument2 = ImageLocation.getForDocument(documentVideoThumb, this.documentAttach);
                                                StringBuilder sb = new StringBuilder();
                                                sb.append("100_100");
                                                sb.append((!SharedConfig.isAutoplayGifs() || this.isKeyboard) ? _UrlKt.FRAGMENT_ENCODE_SET : "_firstframe");
                                                imageReceiver.setImage(forDocument2, sb.toString(), ImageLocation.getForDocument(this.currentPhotoObject, this.documentAttach), str3, -1L, str, this.parentObject, 1);
                                            } else {
                                                forDocument = ImageLocation.getForDocument(this.documentAttach);
                                                if (this.isForceGif) {
                                                    forDocument.imageType = 2;
                                                }
                                                ImageReceiver imageReceiver2 = this.linkImageView;
                                                StringBuilder sb2 = new StringBuilder();
                                                sb2.append("100_100");
                                                sb2.append((!SharedConfig.isAutoplayGifs() || this.isKeyboard) ? _UrlKt.FRAGMENT_ENCODE_SET : "_firstframe");
                                                imageReceiver2.setImage(forDocument, sb2.toString(), ImageLocation.getForDocument(this.currentPhotoObject, this.documentAttach), str3, this.documentAttach.size, str, this.parentObject, 0);
                                            }
                                        } else if (webFileCreateWithWebDocument != null) {
                                            this.linkImageView.setImage(ImageLocation.getForWebFile(webFileCreateWithWebDocument), "100_100", ImageLocation.getForPhoto(this.currentPhotoObject, this.photoAttach), str3, -1L, str, this.parentObject, 1);
                                        } else {
                                            this.linkImageView.setImage(ImageLocation.getForPath(strFormapMapUrl), "100_100", ImageLocation.getForPhoto(this.currentPhotoObject, this.photoAttach), str3, -1L, str, this.parentObject, 1);
                                        }
                                        i4 = 2;
                                    } else if (this.currentPhotoObject != null) {
                                        svgThumb = DocumentObject.getSvgThumb(this.documentAttach, Theme.key_windowBackgroundGray, f);
                                        if (!MessageObject.canAutoplayAnimatedSticker(this.documentAttach)) {
                                            i4 = 2;
                                            document2 = this.documentAttach;
                                            if (document2 == null) {
                                                this.linkImageView.setImage(ImageLocation.getForPhoto(this.currentPhotoObject, this.photoAttach), str3, ImageLocation.getForPhoto(closestPhotoSizeWithSize, this.photoAttach), str2, this.currentPhotoObject.size, str, this.parentObject, 0);
                                            } else if (svgThumb != null) {
                                                this.linkImageView.setImage(ImageLocation.getForDocument(this.currentPhotoObject, document2), str3, svgThumb, this.currentPhotoObject.size, str, this.parentObject, 0);
                                            } else {
                                                this.linkImageView.setImage(ImageLocation.getForDocument(this.currentPhotoObject, document2), str3, ImageLocation.getForPhoto(closestPhotoSizeWithSize, this.photoAttach), str2, this.currentPhotoObject.size, str, this.parentObject, 0);
                                            }
                                        } else if (svgThumb != null) {
                                            i4 = 2;
                                            this.linkImageView.setImage(ImageLocation.getForDocument(this.documentAttach), "80_80", svgThumb, this.currentPhotoObject.size, str, this.parentObject, 0);
                                        } else {
                                            i4 = 2;
                                            this.linkImageView.setImage(ImageLocation.getForDocument(this.documentAttach), "80_80", ImageLocation.getForDocument(this.currentPhotoObject, this.documentAttach), str2, this.currentPhotoObject.size, str, this.parentObject, 0);
                                        }
                                    } else {
                                        i4 = 2;
                                        if (webFileCreateWithWebDocument != null) {
                                            this.linkImageView.setImage(ImageLocation.getForWebFile(webFileCreateWithWebDocument), str3, ImageLocation.getForPhoto(closestPhotoSizeWithSize, this.photoAttach), str2, -1L, str, this.parentObject, 1);
                                        } else {
                                            this.linkImageView.setImage(ImageLocation.getForPath(strFormapMapUrl), str3, ImageLocation.getForPhoto(closestPhotoSizeWithSize, this.photoAttach), str2, -1L, str, this.parentObject, 1);
                                        }
                                    }
                                    if (!SharedConfig.isAutoplayGifs()) {
                                        r3 = i8;
                                        this.linkImageView.setAllowStartAnimation(r5);
                                        this.linkImageView.startAnimation();
                                    } else {
                                        r3 = i8;
                                        this.linkImageView.setAllowStartAnimation(r5);
                                        this.linkImageView.startAnimation();
                                    }
                                    this.drawLinkImageView = r5;
                                    r4 = r3;
                                } else {
                                    if (this.mediaWebpage) {
                                        iDp2 = (int) (iDp / (i3 / AndroidUtilities.dp(80.0f)));
                                        if (this.documentAttachType == 2) {
                                            Locale locale3 = Locale.US;
                                            Object[] objArr3 = new Object[2];
                                            objArr3[i8] = Integer.valueOf((int) (iDp2 / AndroidUtilities.density));
                                            objArr3[r5] = 80;
                                            str6 = String.format(locale3, "%d_%d_b", objArr3);
                                            if (SharedConfig.isAutoplayGifs()) {
                                            }
                                            str3 = str6;
                                            str2 = str3;
                                        } else {
                                            Locale locale4 = Locale.US;
                                            Object[] objArr4 = new Object[2];
                                            objArr4[i8] = Integer.valueOf((int) (iDp2 / AndroidUtilities.density));
                                            objArr4[r5] = 80;
                                            str4 = String.format(locale4, "%d_%d", objArr4);
                                            str5 = str4 + "_b";
                                        }
                                        str3 = str4;
                                        str2 = str5;
                                    } else {
                                        str2 = "52_52_b";
                                        str3 = "52_52";
                                    }
                                    ?? r6 = this.linkImageView;
                                    if (this.documentAttachType == 6) {
                                        r11 = r5;
                                    } else {
                                        r11 = i8;
                                    }
                                    r6.setAspectFit(r11);
                                    if (this.documentAttachType == 2) {
                                        document3 = this.documentAttach;
                                        if (document3 != null) {
                                            documentVideoThumb = MessageObject.getDocumentVideoThumb(document3);
                                            if (documentVideoThumb != null) {
                                                ImageReceiver imageReceiver3 = this.linkImageView;
                                                ImageLocation forDocument3 = ImageLocation.getForDocument(documentVideoThumb, this.documentAttach);
                                                StringBuilder sb3 = new StringBuilder();
                                                sb3.append("100_100");
                                                sb3.append((!SharedConfig.isAutoplayGifs() || this.isKeyboard) ? _UrlKt.FRAGMENT_ENCODE_SET : "_firstframe");
                                                imageReceiver3.setImage(forDocument3, sb3.toString(), ImageLocation.getForDocument(this.currentPhotoObject, this.documentAttach), str3, -1L, str, this.parentObject, 1);
                                            } else {
                                                forDocument = ImageLocation.getForDocument(this.documentAttach);
                                                if (this.isForceGif) {
                                                    forDocument.imageType = 2;
                                                }
                                                ImageReceiver imageReceiver4 = this.linkImageView;
                                                StringBuilder sb4 = new StringBuilder();
                                                sb4.append("100_100");
                                                sb4.append((!SharedConfig.isAutoplayGifs() || this.isKeyboard) ? _UrlKt.FRAGMENT_ENCODE_SET : "_firstframe");
                                                imageReceiver4.setImage(forDocument, sb4.toString(), ImageLocation.getForDocument(this.currentPhotoObject, this.documentAttach), str3, this.documentAttach.size, str, this.parentObject, 0);
                                            }
                                        } else if (webFileCreateWithWebDocument != null) {
                                            this.linkImageView.setImage(ImageLocation.getForWebFile(webFileCreateWithWebDocument), "100_100", ImageLocation.getForPhoto(this.currentPhotoObject, this.photoAttach), str3, -1L, str, this.parentObject, 1);
                                        } else {
                                            this.linkImageView.setImage(ImageLocation.getForPath(strFormapMapUrl), "100_100", ImageLocation.getForPhoto(this.currentPhotoObject, this.photoAttach), str3, -1L, str, this.parentObject, 1);
                                        }
                                        i4 = 2;
                                    } else if (this.currentPhotoObject != null) {
                                        svgThumb = DocumentObject.getSvgThumb(this.documentAttach, Theme.key_windowBackgroundGray, f);
                                        if (!MessageObject.canAutoplayAnimatedSticker(this.documentAttach)) {
                                            i4 = 2;
                                            document2 = this.documentAttach;
                                            if (document2 == null) {
                                                this.linkImageView.setImage(ImageLocation.getForPhoto(this.currentPhotoObject, this.photoAttach), str3, ImageLocation.getForPhoto(closestPhotoSizeWithSize, this.photoAttach), str2, this.currentPhotoObject.size, str, this.parentObject, 0);
                                            } else if (svgThumb != null) {
                                                this.linkImageView.setImage(ImageLocation.getForDocument(this.currentPhotoObject, document2), str3, svgThumb, this.currentPhotoObject.size, str, this.parentObject, 0);
                                            } else {
                                                this.linkImageView.setImage(ImageLocation.getForDocument(this.currentPhotoObject, document2), str3, ImageLocation.getForPhoto(closestPhotoSizeWithSize, this.photoAttach), str2, this.currentPhotoObject.size, str, this.parentObject, 0);
                                            }
                                        } else if (svgThumb != null) {
                                            i4 = 2;
                                            this.linkImageView.setImage(ImageLocation.getForDocument(this.documentAttach), "80_80", svgThumb, this.currentPhotoObject.size, str, this.parentObject, 0);
                                        } else {
                                            i4 = 2;
                                            this.linkImageView.setImage(ImageLocation.getForDocument(this.documentAttach), "80_80", ImageLocation.getForDocument(this.currentPhotoObject, this.documentAttach), str2, this.currentPhotoObject.size, str, this.parentObject, 0);
                                        }
                                    } else {
                                        i4 = 2;
                                        if (webFileCreateWithWebDocument != null) {
                                            this.linkImageView.setImage(ImageLocation.getForWebFile(webFileCreateWithWebDocument), str3, ImageLocation.getForPhoto(closestPhotoSizeWithSize, this.photoAttach), str2, -1L, str, this.parentObject, 1);
                                        } else {
                                            this.linkImageView.setImage(ImageLocation.getForPath(strFormapMapUrl), str3, ImageLocation.getForPhoto(closestPhotoSizeWithSize, this.photoAttach), str2, -1L, str, this.parentObject, 1);
                                        }
                                    }
                                    if (!SharedConfig.isAutoplayGifs()) {
                                        r3 = i8;
                                        this.linkImageView.setAllowStartAnimation(r5);
                                        this.linkImageView.startAnimation();
                                    } else {
                                        r3 = i8;
                                        this.linkImageView.setAllowStartAnimation(r5);
                                        this.linkImageView.startAnimation();
                                    }
                                    this.drawLinkImageView = r5;
                                    r4 = r3;
                                }
                                if (this.mediaWebpage) {
                                    size = View.MeasureSpec.getSize(i2);
                                    if (size == 0) {
                                        size = AndroidUtilities.dp(f2);
                                    }
                                    setMeasuredDimension(size2, size);
                                    int iDp6 = (size2 - AndroidUtilities.dp(24.0f)) / i4;
                                    int iDp7 = (size - AndroidUtilities.dp(24.0f)) / i4;
                                    this.radialProgress.setProgressRect(iDp6, iDp7, AndroidUtilities.dp(24.0f) + iDp6, AndroidUtilities.dp(24.0f) + iDp7);
                                    this.radialProgress.setCircleRadius(AndroidUtilities.dp(12.0f));
                                    this.linkImageView.setImageCoords(0.0f, 0.0f, size2, size);
                                } else {
                                    staticLayout = this.titleLayout;
                                    if (staticLayout != null) {
                                        lineBottom = r4;
                                    } else {
                                        lineBottom = r4;
                                    }
                                    staticLayout2 = this.descriptionLayout;
                                    lineBottom2 = lineBottom;
                                    if (staticLayout2 != null) {
                                        lineBottom2 = lineBottom;
                                        StaticLayout staticLayout5 = this.descriptionLayout;
                                        lineBottom2 = lineBottom + staticLayout5.getLineBottom(staticLayout5.getLineCount() - r5);
                                    }
                                    lineBottom2 = lineBottom;
                                    staticLayout3 = this.linkLayout;
                                    lineBottom3 = lineBottom2;
                                    if (staticLayout3 != null) {
                                        lineBottom3 = lineBottom2;
                                        StaticLayout staticLayout6 = this.linkLayout;
                                        lineBottom3 = lineBottom2 + staticLayout6.getLineBottom(staticLayout6.getLineCount() - r5);
                                    }
                                    lineBottom3 = lineBottom2;
                                    setMeasuredDimension(View.MeasureSpec.getSize(i), Math.max(AndroidUtilities.dp(68.0f), Math.max(AndroidUtilities.dp(52.0f), (int) lineBottom3) + AndroidUtilities.dp(16.0f)) + (this.needDivider ? 1 : 0));
                                    iDp3 = AndroidUtilities.dp(52.0f);
                                    if (LocaleController.isRTL) {
                                        iDp4 = (View.MeasureSpec.getSize(i) - AndroidUtilities.dp(f3)) - iDp3;
                                    } else {
                                        iDp4 = AndroidUtilities.dp(f3);
                                    }
                                    this.letterDrawable.setBounds(iDp4, AndroidUtilities.dp(f3), iDp4 + iDp3, AndroidUtilities.dp(60.0f));
                                    float f4 = iDp3;
                                    this.linkImageView.setImageCoords(iDp4, AndroidUtilities.dp(f3), f4, f4);
                                    i5 = this.documentAttachType;
                                    if (i5 != 3) {
                                        this.radialProgress.setCircleRadius(AndroidUtilities.dp(24.0f));
                                        this.radialProgress.setProgressRect(AndroidUtilities.dp(4.0f) + iDp4, AndroidUtilities.dp(12.0f), iDp4 + AndroidUtilities.dp(48.0f), AndroidUtilities.dp(56.0f));
                                    } else {
                                        this.radialProgress.setCircleRadius(AndroidUtilities.dp(24.0f));
                                        this.radialProgress.setProgressRect(AndroidUtilities.dp(4.0f) + iDp4, AndroidUtilities.dp(12.0f), iDp4 + AndroidUtilities.dp(48.0f), AndroidUtilities.dp(56.0f));
                                    }
                                }
                                checkBox2 = this.checkBox;
                                if (checkBox2 != null) {
                                    measureChildWithMargins(checkBox2, i, 0, i2, 0);
                                }
                            }
                            botInlineResult = this.inlineResult;
                            if (botInlineResult == null) {
                            }
                            str = null;
                            botInlineResult2 = this.inlineResult;
                            if (botInlineResult2 != null) {
                                if (botInlineResult2.content instanceof TLRPC.TL_webDocument) {
                                    tL_webDocument = null;
                                } else {
                                    tL_webDocument = null;
                                }
                                if (tL_webDocument == null) {
                                    webDocument = this.inlineResult.thumb;
                                    if (webDocument instanceof TLRPC.TL_webDocument) {
                                        tL_webDocument = (TLRPC.TL_webDocument) webDocument;
                                    }
                                }
                                if (tL_webDocument != null) {
                                    strFormapMapUrl = null;
                                    webFileCreateWithWebDocument = null;
                                } else {
                                    strFormapMapUrl = null;
                                    webFileCreateWithWebDocument = null;
                                }
                                if (tL_webDocument != null) {
                                    webFileCreateWithWebDocument = WebFile.createWithWebDocument(tL_webDocument);
                                }
                            } else {
                                strFormapMapUrl = null;
                                webFileCreateWithWebDocument = null;
                            }
                            if (this.documentAttach != null) {
                                i6 = i8;
                                while (true) {
                                    if (i6 < this.documentAttach.attributes.size()) {
                                        documentAttribute = this.documentAttach.attributes.get(i6);
                                        if (documentAttribute instanceof TLRPC.TL_documentAttributeImageSize) {
                                        }
                                        iDp = documentAttribute.w;
                                        i3 = documentAttribute.h;
                                    } else {
                                        iDp = i8;
                                        i3 = iDp;
                                    }
                                    i6++;
                                }
                            } else {
                                iDp = i8;
                                i3 = iDp;
                            }
                            if (iDp != 0) {
                                photoSize = this.currentPhotoObject;
                                if (photoSize != null) {
                                    if (closestPhotoSizeWithSize != null) {
                                        closestPhotoSizeWithSize.size = -1;
                                    }
                                    iDp = photoSize.w;
                                    i3 = photoSize.h;
                                } else {
                                    botInlineResult3 = this.inlineResult;
                                    if (botInlineResult3 != null) {
                                        int[] inlineResultWidthAndHeight3 = MessageObject.getInlineResultWidthAndHeight(botInlineResult3);
                                        int i12 = inlineResultWidthAndHeight3[i8];
                                        i3 = inlineResultWidthAndHeight3[r5];
                                        iDp = i12;
                                    }
                                }
                            } else {
                                photoSize = this.currentPhotoObject;
                                if (photoSize != null) {
                                    if (closestPhotoSizeWithSize != null) {
                                        closestPhotoSizeWithSize.size = -1;
                                    }
                                    iDp = photoSize.w;
                                    i3 = photoSize.h;
                                } else {
                                    botInlineResult3 = this.inlineResult;
                                    if (botInlineResult3 != null) {
                                        int[] inlineResultWidthAndHeight4 = MessageObject.getInlineResultWidthAndHeight(botInlineResult3);
                                        int i13 = inlineResultWidthAndHeight4[i8];
                                        i3 = inlineResultWidthAndHeight4[r5];
                                        iDp = i13;
                                    }
                                }
                            }
                            if (iDp != 0) {
                                iDp = AndroidUtilities.dp(80.0f);
                                i3 = iDp;
                            } else {
                                iDp = AndroidUtilities.dp(80.0f);
                                i3 = iDp;
                            }
                            if (this.documentAttach != null) {
                                if (this.mediaWebpage) {
                                    iDp2 = (int) (iDp / (i3 / AndroidUtilities.dp(80.0f)));
                                    if (this.documentAttachType == 2) {
                                        Locale locale5 = Locale.US;
                                        Object[] objArr5 = new Object[2];
                                        objArr5[i8] = Integer.valueOf((int) (iDp2 / AndroidUtilities.density));
                                        objArr5[r5] = 80;
                                        str6 = String.format(locale5, "%d_%d_b", objArr5);
                                        if (SharedConfig.isAutoplayGifs()) {
                                        }
                                        str3 = str6;
                                        str2 = str3;
                                    } else {
                                        Locale locale6 = Locale.US;
                                        Object[] objArr6 = new Object[2];
                                        objArr6[i8] = Integer.valueOf((int) (iDp2 / AndroidUtilities.density));
                                        objArr6[r5] = 80;
                                        str4 = String.format(locale6, "%d_%d", objArr6);
                                        str5 = str4 + "_b";
                                    }
                                    str3 = str4;
                                    str2 = str5;
                                } else {
                                    str2 = "52_52_b";
                                    str3 = "52_52";
                                }
                                ?? r7 = this.linkImageView;
                                if (this.documentAttachType == 6) {
                                    r11 = r5;
                                } else {
                                    r11 = i8;
                                }
                                r7.setAspectFit(r11);
                                if (this.documentAttachType == 2) {
                                    document3 = this.documentAttach;
                                    if (document3 != null) {
                                        documentVideoThumb = MessageObject.getDocumentVideoThumb(document3);
                                        if (documentVideoThumb != null) {
                                            ImageReceiver imageReceiver5 = this.linkImageView;
                                            ImageLocation forDocument4 = ImageLocation.getForDocument(documentVideoThumb, this.documentAttach);
                                            StringBuilder sb5 = new StringBuilder();
                                            sb5.append("100_100");
                                            sb5.append((!SharedConfig.isAutoplayGifs() || this.isKeyboard) ? _UrlKt.FRAGMENT_ENCODE_SET : "_firstframe");
                                            imageReceiver5.setImage(forDocument4, sb5.toString(), ImageLocation.getForDocument(this.currentPhotoObject, this.documentAttach), str3, -1L, str, this.parentObject, 1);
                                        } else {
                                            forDocument = ImageLocation.getForDocument(this.documentAttach);
                                            if (this.isForceGif) {
                                                forDocument.imageType = 2;
                                            }
                                            ImageReceiver imageReceiver6 = this.linkImageView;
                                            StringBuilder sb6 = new StringBuilder();
                                            sb6.append("100_100");
                                            sb6.append((!SharedConfig.isAutoplayGifs() || this.isKeyboard) ? _UrlKt.FRAGMENT_ENCODE_SET : "_firstframe");
                                            imageReceiver6.setImage(forDocument, sb6.toString(), ImageLocation.getForDocument(this.currentPhotoObject, this.documentAttach), str3, this.documentAttach.size, str, this.parentObject, 0);
                                        }
                                    } else if (webFileCreateWithWebDocument != null) {
                                        this.linkImageView.setImage(ImageLocation.getForWebFile(webFileCreateWithWebDocument), "100_100", ImageLocation.getForPhoto(this.currentPhotoObject, this.photoAttach), str3, -1L, str, this.parentObject, 1);
                                    } else {
                                        this.linkImageView.setImage(ImageLocation.getForPath(strFormapMapUrl), "100_100", ImageLocation.getForPhoto(this.currentPhotoObject, this.photoAttach), str3, -1L, str, this.parentObject, 1);
                                    }
                                    i4 = 2;
                                } else if (this.currentPhotoObject != null) {
                                    svgThumb = DocumentObject.getSvgThumb(this.documentAttach, Theme.key_windowBackgroundGray, f);
                                    if (!MessageObject.canAutoplayAnimatedSticker(this.documentAttach)) {
                                        i4 = 2;
                                        document2 = this.documentAttach;
                                        if (document2 == null) {
                                            this.linkImageView.setImage(ImageLocation.getForPhoto(this.currentPhotoObject, this.photoAttach), str3, ImageLocation.getForPhoto(closestPhotoSizeWithSize, this.photoAttach), str2, this.currentPhotoObject.size, str, this.parentObject, 0);
                                        } else if (svgThumb != null) {
                                            this.linkImageView.setImage(ImageLocation.getForDocument(this.currentPhotoObject, document2), str3, svgThumb, this.currentPhotoObject.size, str, this.parentObject, 0);
                                        } else {
                                            this.linkImageView.setImage(ImageLocation.getForDocument(this.currentPhotoObject, document2), str3, ImageLocation.getForPhoto(closestPhotoSizeWithSize, this.photoAttach), str2, this.currentPhotoObject.size, str, this.parentObject, 0);
                                        }
                                    } else if (svgThumb != null) {
                                        i4 = 2;
                                        this.linkImageView.setImage(ImageLocation.getForDocument(this.documentAttach), "80_80", svgThumb, this.currentPhotoObject.size, str, this.parentObject, 0);
                                    } else {
                                        i4 = 2;
                                        this.linkImageView.setImage(ImageLocation.getForDocument(this.documentAttach), "80_80", ImageLocation.getForDocument(this.currentPhotoObject, this.documentAttach), str2, this.currentPhotoObject.size, str, this.parentObject, 0);
                                    }
                                } else {
                                    i4 = 2;
                                    if (webFileCreateWithWebDocument != null) {
                                        this.linkImageView.setImage(ImageLocation.getForWebFile(webFileCreateWithWebDocument), str3, ImageLocation.getForPhoto(closestPhotoSizeWithSize, this.photoAttach), str2, -1L, str, this.parentObject, 1);
                                    } else {
                                        this.linkImageView.setImage(ImageLocation.getForPath(strFormapMapUrl), str3, ImageLocation.getForPhoto(closestPhotoSizeWithSize, this.photoAttach), str2, -1L, str, this.parentObject, 1);
                                    }
                                }
                                if (!SharedConfig.isAutoplayGifs()) {
                                    r3 = i8;
                                    this.linkImageView.setAllowStartAnimation(r5);
                                    this.linkImageView.startAnimation();
                                } else {
                                    r3 = i8;
                                    this.linkImageView.setAllowStartAnimation(r5);
                                    this.linkImageView.startAnimation();
                                }
                                this.drawLinkImageView = r5;
                                r4 = r3;
                            } else {
                                if (this.mediaWebpage) {
                                    iDp2 = (int) (iDp / (i3 / AndroidUtilities.dp(80.0f)));
                                    if (this.documentAttachType == 2) {
                                        Locale locale7 = Locale.US;
                                        Object[] objArr7 = new Object[2];
                                        objArr7[i8] = Integer.valueOf((int) (iDp2 / AndroidUtilities.density));
                                        objArr7[r5] = 80;
                                        str6 = String.format(locale7, "%d_%d_b", objArr7);
                                        if (SharedConfig.isAutoplayGifs()) {
                                        }
                                        str3 = str6;
                                        str2 = str3;
                                    } else {
                                        Locale locale8 = Locale.US;
                                        Object[] objArr8 = new Object[2];
                                        objArr8[i8] = Integer.valueOf((int) (iDp2 / AndroidUtilities.density));
                                        objArr8[r5] = 80;
                                        str4 = String.format(locale8, "%d_%d", objArr8);
                                        str5 = str4 + "_b";
                                    }
                                    str3 = str4;
                                    str2 = str5;
                                } else {
                                    str2 = "52_52_b";
                                    str3 = "52_52";
                                }
                                ?? r8 = this.linkImageView;
                                if (this.documentAttachType == 6) {
                                    r11 = r5;
                                } else {
                                    r11 = i8;
                                }
                                r8.setAspectFit(r11);
                                if (this.documentAttachType == 2) {
                                    document3 = this.documentAttach;
                                    if (document3 != null) {
                                        documentVideoThumb = MessageObject.getDocumentVideoThumb(document3);
                                        if (documentVideoThumb != null) {
                                            ImageReceiver imageReceiver7 = this.linkImageView;
                                            ImageLocation forDocument5 = ImageLocation.getForDocument(documentVideoThumb, this.documentAttach);
                                            StringBuilder sb7 = new StringBuilder();
                                            sb7.append("100_100");
                                            sb7.append((!SharedConfig.isAutoplayGifs() || this.isKeyboard) ? _UrlKt.FRAGMENT_ENCODE_SET : "_firstframe");
                                            imageReceiver7.setImage(forDocument5, sb7.toString(), ImageLocation.getForDocument(this.currentPhotoObject, this.documentAttach), str3, -1L, str, this.parentObject, 1);
                                        } else {
                                            forDocument = ImageLocation.getForDocument(this.documentAttach);
                                            if (this.isForceGif) {
                                                forDocument.imageType = 2;
                                            }
                                            ImageReceiver imageReceiver8 = this.linkImageView;
                                            StringBuilder sb8 = new StringBuilder();
                                            sb8.append("100_100");
                                            sb8.append((!SharedConfig.isAutoplayGifs() || this.isKeyboard) ? _UrlKt.FRAGMENT_ENCODE_SET : "_firstframe");
                                            imageReceiver8.setImage(forDocument, sb8.toString(), ImageLocation.getForDocument(this.currentPhotoObject, this.documentAttach), str3, this.documentAttach.size, str, this.parentObject, 0);
                                        }
                                    } else if (webFileCreateWithWebDocument != null) {
                                        this.linkImageView.setImage(ImageLocation.getForWebFile(webFileCreateWithWebDocument), "100_100", ImageLocation.getForPhoto(this.currentPhotoObject, this.photoAttach), str3, -1L, str, this.parentObject, 1);
                                    } else {
                                        this.linkImageView.setImage(ImageLocation.getForPath(strFormapMapUrl), "100_100", ImageLocation.getForPhoto(this.currentPhotoObject, this.photoAttach), str3, -1L, str, this.parentObject, 1);
                                    }
                                    i4 = 2;
                                } else if (this.currentPhotoObject != null) {
                                    svgThumb = DocumentObject.getSvgThumb(this.documentAttach, Theme.key_windowBackgroundGray, f);
                                    if (!MessageObject.canAutoplayAnimatedSticker(this.documentAttach)) {
                                        i4 = 2;
                                        document2 = this.documentAttach;
                                        if (document2 == null) {
                                            this.linkImageView.setImage(ImageLocation.getForPhoto(this.currentPhotoObject, this.photoAttach), str3, ImageLocation.getForPhoto(closestPhotoSizeWithSize, this.photoAttach), str2, this.currentPhotoObject.size, str, this.parentObject, 0);
                                        } else if (svgThumb != null) {
                                            this.linkImageView.setImage(ImageLocation.getForDocument(this.currentPhotoObject, document2), str3, svgThumb, this.currentPhotoObject.size, str, this.parentObject, 0);
                                        } else {
                                            this.linkImageView.setImage(ImageLocation.getForDocument(this.currentPhotoObject, document2), str3, ImageLocation.getForPhoto(closestPhotoSizeWithSize, this.photoAttach), str2, this.currentPhotoObject.size, str, this.parentObject, 0);
                                        }
                                    } else if (svgThumb != null) {
                                        i4 = 2;
                                        this.linkImageView.setImage(ImageLocation.getForDocument(this.documentAttach), "80_80", svgThumb, this.currentPhotoObject.size, str, this.parentObject, 0);
                                    } else {
                                        i4 = 2;
                                        this.linkImageView.setImage(ImageLocation.getForDocument(this.documentAttach), "80_80", ImageLocation.getForDocument(this.currentPhotoObject, this.documentAttach), str2, this.currentPhotoObject.size, str, this.parentObject, 0);
                                    }
                                } else {
                                    i4 = 2;
                                    if (webFileCreateWithWebDocument != null) {
                                        this.linkImageView.setImage(ImageLocation.getForWebFile(webFileCreateWithWebDocument), str3, ImageLocation.getForPhoto(closestPhotoSizeWithSize, this.photoAttach), str2, -1L, str, this.parentObject, 1);
                                    } else {
                                        this.linkImageView.setImage(ImageLocation.getForPath(strFormapMapUrl), str3, ImageLocation.getForPhoto(closestPhotoSizeWithSize, this.photoAttach), str2, -1L, str, this.parentObject, 1);
                                    }
                                }
                                if (!SharedConfig.isAutoplayGifs()) {
                                    r3 = i8;
                                    this.linkImageView.setAllowStartAnimation(r5);
                                    this.linkImageView.startAnimation();
                                } else {
                                    r3 = i8;
                                    this.linkImageView.setAllowStartAnimation(r5);
                                    this.linkImageView.startAnimation();
                                }
                                this.drawLinkImageView = r5;
                                r4 = r3;
                            }
                            if (this.mediaWebpage) {
                                size = View.MeasureSpec.getSize(i2);
                                if (size == 0) {
                                    size = AndroidUtilities.dp(f2);
                                }
                                setMeasuredDimension(size2, size);
                                int iDp8 = (size2 - AndroidUtilities.dp(24.0f)) / i4;
                                int iDp9 = (size - AndroidUtilities.dp(24.0f)) / i4;
                                this.radialProgress.setProgressRect(iDp8, iDp9, AndroidUtilities.dp(24.0f) + iDp8, AndroidUtilities.dp(24.0f) + iDp9);
                                this.radialProgress.setCircleRadius(AndroidUtilities.dp(12.0f));
                                this.linkImageView.setImageCoords(0.0f, 0.0f, size2, size);
                            } else {
                                staticLayout = this.titleLayout;
                                if (staticLayout != null) {
                                    lineBottom = r4;
                                } else {
                                    lineBottom = r4;
                                }
                                staticLayout2 = this.descriptionLayout;
                                lineBottom2 = lineBottom;
                                if (staticLayout2 != null) {
                                    lineBottom2 = lineBottom;
                                    StaticLayout staticLayout7 = this.descriptionLayout;
                                    lineBottom2 = lineBottom + staticLayout7.getLineBottom(staticLayout7.getLineCount() - r5);
                                }
                                lineBottom2 = lineBottom;
                                staticLayout3 = this.linkLayout;
                                lineBottom3 = lineBottom2;
                                if (staticLayout3 != null) {
                                    lineBottom3 = lineBottom2;
                                    StaticLayout staticLayout8 = this.linkLayout;
                                    lineBottom3 = lineBottom2 + staticLayout8.getLineBottom(staticLayout8.getLineCount() - r5);
                                }
                                lineBottom3 = lineBottom2;
                                setMeasuredDimension(View.MeasureSpec.getSize(i), Math.max(AndroidUtilities.dp(68.0f), Math.max(AndroidUtilities.dp(52.0f), (int) lineBottom3) + AndroidUtilities.dp(16.0f)) + (this.needDivider ? 1 : 0));
                                iDp3 = AndroidUtilities.dp(52.0f);
                                if (LocaleController.isRTL) {
                                    iDp4 = (View.MeasureSpec.getSize(i) - AndroidUtilities.dp(f3)) - iDp3;
                                } else {
                                    iDp4 = AndroidUtilities.dp(f3);
                                }
                                this.letterDrawable.setBounds(iDp4, AndroidUtilities.dp(f3), iDp4 + iDp3, AndroidUtilities.dp(60.0f));
                                float f5 = iDp3;
                                this.linkImageView.setImageCoords(iDp4, AndroidUtilities.dp(f3), f5, f5);
                                i5 = this.documentAttachType;
                                if (i5 != 3) {
                                    this.radialProgress.setCircleRadius(AndroidUtilities.dp(24.0f));
                                    this.radialProgress.setProgressRect(AndroidUtilities.dp(4.0f) + iDp4, AndroidUtilities.dp(12.0f), iDp4 + AndroidUtilities.dp(48.0f), AndroidUtilities.dp(56.0f));
                                } else {
                                    this.radialProgress.setCircleRadius(AndroidUtilities.dp(24.0f));
                                    this.radialProgress.setProgressRect(AndroidUtilities.dp(4.0f) + iDp4, AndroidUtilities.dp(12.0f), iDp4 + AndroidUtilities.dp(48.0f), AndroidUtilities.dp(56.0f));
                                }
                            }
                            checkBox2 = this.checkBox;
                            if (checkBox2 != null) {
                                measureChildWithMargins(checkBox2, i, 0, i2, 0);
                            }
                            closestPhotoSizeWithSize = null;
                            str = null;
                            botInlineResult2 = this.inlineResult;
                            if (botInlineResult2 != null) {
                                if (botInlineResult2.content instanceof TLRPC.TL_webDocument) {
                                    tL_webDocument = null;
                                } else {
                                    tL_webDocument = null;
                                }
                                if (tL_webDocument == null) {
                                    webDocument = this.inlineResult.thumb;
                                    if (webDocument instanceof TLRPC.TL_webDocument) {
                                        tL_webDocument = (TLRPC.TL_webDocument) webDocument;
                                    }
                                }
                                if (tL_webDocument != null) {
                                    strFormapMapUrl = null;
                                    webFileCreateWithWebDocument = null;
                                } else {
                                    strFormapMapUrl = null;
                                    webFileCreateWithWebDocument = null;
                                }
                                if (tL_webDocument != null) {
                                    webFileCreateWithWebDocument = WebFile.createWithWebDocument(tL_webDocument);
                                }
                            } else {
                                strFormapMapUrl = null;
                                webFileCreateWithWebDocument = null;
                            }
                            if (this.documentAttach != null) {
                                i6 = i8;
                                while (true) {
                                    if (i6 < this.documentAttach.attributes.size()) {
                                        documentAttribute = this.documentAttach.attributes.get(i6);
                                        if (documentAttribute instanceof TLRPC.TL_documentAttributeImageSize) {
                                        }
                                        iDp = documentAttribute.w;
                                        i3 = documentAttribute.h;
                                    } else {
                                        iDp = i8;
                                        i3 = iDp;
                                    }
                                    i6++;
                                }
                            } else {
                                iDp = i8;
                                i3 = iDp;
                            }
                            if (iDp != 0) {
                                photoSize = this.currentPhotoObject;
                                if (photoSize != null) {
                                    if (closestPhotoSizeWithSize != null) {
                                        closestPhotoSizeWithSize.size = -1;
                                    }
                                    iDp = photoSize.w;
                                    i3 = photoSize.h;
                                } else {
                                    botInlineResult3 = this.inlineResult;
                                    if (botInlineResult3 != null) {
                                        int[] inlineResultWidthAndHeight5 = MessageObject.getInlineResultWidthAndHeight(botInlineResult3);
                                        int i14 = inlineResultWidthAndHeight5[i8];
                                        i3 = inlineResultWidthAndHeight5[r5];
                                        iDp = i14;
                                    }
                                }
                            } else {
                                photoSize = this.currentPhotoObject;
                                if (photoSize != null) {
                                    if (closestPhotoSizeWithSize != null) {
                                        closestPhotoSizeWithSize.size = -1;
                                    }
                                    iDp = photoSize.w;
                                    i3 = photoSize.h;
                                } else {
                                    botInlineResult3 = this.inlineResult;
                                    if (botInlineResult3 != null) {
                                        int[] inlineResultWidthAndHeight6 = MessageObject.getInlineResultWidthAndHeight(botInlineResult3);
                                        int i15 = inlineResultWidthAndHeight6[i8];
                                        i3 = inlineResultWidthAndHeight6[r5];
                                        iDp = i15;
                                    }
                                }
                            }
                            if (iDp != 0) {
                                iDp = AndroidUtilities.dp(80.0f);
                                i3 = iDp;
                            } else {
                                iDp = AndroidUtilities.dp(80.0f);
                                i3 = iDp;
                            }
                            if (this.documentAttach != null) {
                                if (this.mediaWebpage) {
                                    iDp2 = (int) (iDp / (i3 / AndroidUtilities.dp(80.0f)));
                                    if (this.documentAttachType == 2) {
                                        Locale locale9 = Locale.US;
                                        Object[] objArr9 = new Object[2];
                                        objArr9[i8] = Integer.valueOf((int) (iDp2 / AndroidUtilities.density));
                                        objArr9[r5] = 80;
                                        str6 = String.format(locale9, "%d_%d_b", objArr9);
                                        if (SharedConfig.isAutoplayGifs()) {
                                        }
                                        str3 = str6;
                                        str2 = str3;
                                    } else {
                                        Locale locale10 = Locale.US;
                                        Object[] objArr10 = new Object[2];
                                        objArr10[i8] = Integer.valueOf((int) (iDp2 / AndroidUtilities.density));
                                        objArr10[r5] = 80;
                                        str4 = String.format(locale10, "%d_%d", objArr10);
                                        str5 = str4 + "_b";
                                    }
                                    str3 = str4;
                                    str2 = str5;
                                } else {
                                    str2 = "52_52_b";
                                    str3 = "52_52";
                                }
                                ?? r9 = this.linkImageView;
                                if (this.documentAttachType == 6) {
                                    r11 = r5;
                                } else {
                                    r11 = i8;
                                }
                                r9.setAspectFit(r11);
                                if (this.documentAttachType == 2) {
                                    document3 = this.documentAttach;
                                    if (document3 != null) {
                                        documentVideoThumb = MessageObject.getDocumentVideoThumb(document3);
                                        if (documentVideoThumb != null) {
                                            ImageReceiver imageReceiver9 = this.linkImageView;
                                            ImageLocation forDocument6 = ImageLocation.getForDocument(documentVideoThumb, this.documentAttach);
                                            StringBuilder sb9 = new StringBuilder();
                                            sb9.append("100_100");
                                            sb9.append((!SharedConfig.isAutoplayGifs() || this.isKeyboard) ? _UrlKt.FRAGMENT_ENCODE_SET : "_firstframe");
                                            imageReceiver9.setImage(forDocument6, sb9.toString(), ImageLocation.getForDocument(this.currentPhotoObject, this.documentAttach), str3, -1L, str, this.parentObject, 1);
                                        } else {
                                            forDocument = ImageLocation.getForDocument(this.documentAttach);
                                            if (this.isForceGif) {
                                                forDocument.imageType = 2;
                                            }
                                            ImageReceiver imageReceiver10 = this.linkImageView;
                                            StringBuilder sb10 = new StringBuilder();
                                            sb10.append("100_100");
                                            sb10.append((!SharedConfig.isAutoplayGifs() || this.isKeyboard) ? _UrlKt.FRAGMENT_ENCODE_SET : "_firstframe");
                                            imageReceiver10.setImage(forDocument, sb10.toString(), ImageLocation.getForDocument(this.currentPhotoObject, this.documentAttach), str3, this.documentAttach.size, str, this.parentObject, 0);
                                        }
                                    } else if (webFileCreateWithWebDocument != null) {
                                        this.linkImageView.setImage(ImageLocation.getForWebFile(webFileCreateWithWebDocument), "100_100", ImageLocation.getForPhoto(this.currentPhotoObject, this.photoAttach), str3, -1L, str, this.parentObject, 1);
                                    } else {
                                        this.linkImageView.setImage(ImageLocation.getForPath(strFormapMapUrl), "100_100", ImageLocation.getForPhoto(this.currentPhotoObject, this.photoAttach), str3, -1L, str, this.parentObject, 1);
                                    }
                                    i4 = 2;
                                } else if (this.currentPhotoObject != null) {
                                    svgThumb = DocumentObject.getSvgThumb(this.documentAttach, Theme.key_windowBackgroundGray, f);
                                    if (!MessageObject.canAutoplayAnimatedSticker(this.documentAttach)) {
                                        i4 = 2;
                                        document2 = this.documentAttach;
                                        if (document2 == null) {
                                            this.linkImageView.setImage(ImageLocation.getForPhoto(this.currentPhotoObject, this.photoAttach), str3, ImageLocation.getForPhoto(closestPhotoSizeWithSize, this.photoAttach), str2, this.currentPhotoObject.size, str, this.parentObject, 0);
                                        } else if (svgThumb != null) {
                                            this.linkImageView.setImage(ImageLocation.getForDocument(this.currentPhotoObject, document2), str3, svgThumb, this.currentPhotoObject.size, str, this.parentObject, 0);
                                        } else {
                                            this.linkImageView.setImage(ImageLocation.getForDocument(this.currentPhotoObject, document2), str3, ImageLocation.getForPhoto(closestPhotoSizeWithSize, this.photoAttach), str2, this.currentPhotoObject.size, str, this.parentObject, 0);
                                        }
                                    } else if (svgThumb != null) {
                                        i4 = 2;
                                        this.linkImageView.setImage(ImageLocation.getForDocument(this.documentAttach), "80_80", svgThumb, this.currentPhotoObject.size, str, this.parentObject, 0);
                                    } else {
                                        i4 = 2;
                                        this.linkImageView.setImage(ImageLocation.getForDocument(this.documentAttach), "80_80", ImageLocation.getForDocument(this.currentPhotoObject, this.documentAttach), str2, this.currentPhotoObject.size, str, this.parentObject, 0);
                                    }
                                } else {
                                    i4 = 2;
                                    if (webFileCreateWithWebDocument != null) {
                                        this.linkImageView.setImage(ImageLocation.getForWebFile(webFileCreateWithWebDocument), str3, ImageLocation.getForPhoto(closestPhotoSizeWithSize, this.photoAttach), str2, -1L, str, this.parentObject, 1);
                                    } else {
                                        this.linkImageView.setImage(ImageLocation.getForPath(strFormapMapUrl), str3, ImageLocation.getForPhoto(closestPhotoSizeWithSize, this.photoAttach), str2, -1L, str, this.parentObject, 1);
                                    }
                                }
                                if (!SharedConfig.isAutoplayGifs()) {
                                    r3 = i8;
                                    this.linkImageView.setAllowStartAnimation(r5);
                                    this.linkImageView.startAnimation();
                                } else {
                                    r3 = i8;
                                    this.linkImageView.setAllowStartAnimation(r5);
                                    this.linkImageView.startAnimation();
                                }
                                this.drawLinkImageView = r5;
                                r4 = r3;
                            } else {
                                if (this.mediaWebpage) {
                                    iDp2 = (int) (iDp / (i3 / AndroidUtilities.dp(80.0f)));
                                    if (this.documentAttachType == 2) {
                                        Locale locale11 = Locale.US;
                                        Object[] objArr11 = new Object[2];
                                        objArr11[i8] = Integer.valueOf((int) (iDp2 / AndroidUtilities.density));
                                        objArr11[r5] = 80;
                                        str6 = String.format(locale11, "%d_%d_b", objArr11);
                                        if (SharedConfig.isAutoplayGifs()) {
                                        }
                                        str3 = str6;
                                        str2 = str3;
                                    } else {
                                        Locale locale12 = Locale.US;
                                        Object[] objArr12 = new Object[2];
                                        objArr12[i8] = Integer.valueOf((int) (iDp2 / AndroidUtilities.density));
                                        objArr12[r5] = 80;
                                        str4 = String.format(locale12, "%d_%d", objArr12);
                                        str5 = str4 + "_b";
                                    }
                                    str3 = str4;
                                    str2 = str5;
                                } else {
                                    str2 = "52_52_b";
                                    str3 = "52_52";
                                }
                                ?? r10 = this.linkImageView;
                                if (this.documentAttachType == 6) {
                                    r11 = r5;
                                } else {
                                    r11 = i8;
                                }
                                r10.setAspectFit(r11);
                                if (this.documentAttachType == 2) {
                                    document3 = this.documentAttach;
                                    if (document3 != null) {
                                        documentVideoThumb = MessageObject.getDocumentVideoThumb(document3);
                                        if (documentVideoThumb != null) {
                                            ImageReceiver imageReceiver11 = this.linkImageView;
                                            ImageLocation forDocument7 = ImageLocation.getForDocument(documentVideoThumb, this.documentAttach);
                                            StringBuilder sb11 = new StringBuilder();
                                            sb11.append("100_100");
                                            sb11.append((!SharedConfig.isAutoplayGifs() || this.isKeyboard) ? _UrlKt.FRAGMENT_ENCODE_SET : "_firstframe");
                                            imageReceiver11.setImage(forDocument7, sb11.toString(), ImageLocation.getForDocument(this.currentPhotoObject, this.documentAttach), str3, -1L, str, this.parentObject, 1);
                                        } else {
                                            forDocument = ImageLocation.getForDocument(this.documentAttach);
                                            if (this.isForceGif) {
                                                forDocument.imageType = 2;
                                            }
                                            ImageReceiver imageReceiver12 = this.linkImageView;
                                            StringBuilder sb12 = new StringBuilder();
                                            sb12.append("100_100");
                                            sb12.append((!SharedConfig.isAutoplayGifs() || this.isKeyboard) ? _UrlKt.FRAGMENT_ENCODE_SET : "_firstframe");
                                            imageReceiver12.setImage(forDocument, sb12.toString(), ImageLocation.getForDocument(this.currentPhotoObject, this.documentAttach), str3, this.documentAttach.size, str, this.parentObject, 0);
                                        }
                                    } else if (webFileCreateWithWebDocument != null) {
                                        this.linkImageView.setImage(ImageLocation.getForWebFile(webFileCreateWithWebDocument), "100_100", ImageLocation.getForPhoto(this.currentPhotoObject, this.photoAttach), str3, -1L, str, this.parentObject, 1);
                                    } else {
                                        this.linkImageView.setImage(ImageLocation.getForPath(strFormapMapUrl), "100_100", ImageLocation.getForPhoto(this.currentPhotoObject, this.photoAttach), str3, -1L, str, this.parentObject, 1);
                                    }
                                    i4 = 2;
                                } else if (this.currentPhotoObject != null) {
                                    svgThumb = DocumentObject.getSvgThumb(this.documentAttach, Theme.key_windowBackgroundGray, f);
                                    if (!MessageObject.canAutoplayAnimatedSticker(this.documentAttach)) {
                                        i4 = 2;
                                        document2 = this.documentAttach;
                                        if (document2 == null) {
                                            this.linkImageView.setImage(ImageLocation.getForPhoto(this.currentPhotoObject, this.photoAttach), str3, ImageLocation.getForPhoto(closestPhotoSizeWithSize, this.photoAttach), str2, this.currentPhotoObject.size, str, this.parentObject, 0);
                                        } else if (svgThumb != null) {
                                            this.linkImageView.setImage(ImageLocation.getForDocument(this.currentPhotoObject, document2), str3, svgThumb, this.currentPhotoObject.size, str, this.parentObject, 0);
                                        } else {
                                            this.linkImageView.setImage(ImageLocation.getForDocument(this.currentPhotoObject, document2), str3, ImageLocation.getForPhoto(closestPhotoSizeWithSize, this.photoAttach), str2, this.currentPhotoObject.size, str, this.parentObject, 0);
                                        }
                                    } else if (svgThumb != null) {
                                        i4 = 2;
                                        this.linkImageView.setImage(ImageLocation.getForDocument(this.documentAttach), "80_80", svgThumb, this.currentPhotoObject.size, str, this.parentObject, 0);
                                    } else {
                                        i4 = 2;
                                        this.linkImageView.setImage(ImageLocation.getForDocument(this.documentAttach), "80_80", ImageLocation.getForDocument(this.currentPhotoObject, this.documentAttach), str2, this.currentPhotoObject.size, str, this.parentObject, 0);
                                    }
                                } else {
                                    i4 = 2;
                                    if (webFileCreateWithWebDocument != null) {
                                        this.linkImageView.setImage(ImageLocation.getForWebFile(webFileCreateWithWebDocument), str3, ImageLocation.getForPhoto(closestPhotoSizeWithSize, this.photoAttach), str2, -1L, str, this.parentObject, 1);
                                    } else {
                                        this.linkImageView.setImage(ImageLocation.getForPath(strFormapMapUrl), str3, ImageLocation.getForPhoto(closestPhotoSizeWithSize, this.photoAttach), str2, -1L, str, this.parentObject, 1);
                                    }
                                }
                                if (!SharedConfig.isAutoplayGifs()) {
                                    r3 = i8;
                                    this.linkImageView.setAllowStartAnimation(r5);
                                    this.linkImageView.startAnimation();
                                } else {
                                    r3 = i8;
                                    this.linkImageView.setAllowStartAnimation(r5);
                                    this.linkImageView.startAnimation();
                                }
                                this.drawLinkImageView = r5;
                                r4 = r3;
                            }
                            if (this.mediaWebpage) {
                                size = View.MeasureSpec.getSize(i2);
                                if (size == 0) {
                                    size = AndroidUtilities.dp(f2);
                                }
                                setMeasuredDimension(size2, size);
                                int iDp10 = (size2 - AndroidUtilities.dp(24.0f)) / i4;
                                int iDp11 = (size - AndroidUtilities.dp(24.0f)) / i4;
                                this.radialProgress.setProgressRect(iDp10, iDp11, AndroidUtilities.dp(24.0f) + iDp10, AndroidUtilities.dp(24.0f) + iDp11);
                                this.radialProgress.setCircleRadius(AndroidUtilities.dp(12.0f));
                                this.linkImageView.setImageCoords(0.0f, 0.0f, size2, size);
                            } else {
                                staticLayout = this.titleLayout;
                                if (staticLayout != null) {
                                    lineBottom = r4;
                                } else {
                                    lineBottom = r4;
                                }
                                staticLayout2 = this.descriptionLayout;
                                lineBottom2 = lineBottom;
                                if (staticLayout2 != null) {
                                    lineBottom2 = lineBottom;
                                    StaticLayout staticLayout9 = this.descriptionLayout;
                                    lineBottom2 = lineBottom + staticLayout9.getLineBottom(staticLayout9.getLineCount() - r5);
                                }
                                lineBottom2 = lineBottom;
                                staticLayout3 = this.linkLayout;
                                lineBottom3 = lineBottom2;
                                if (staticLayout3 != null) {
                                    lineBottom3 = lineBottom2;
                                    StaticLayout staticLayout10 = this.linkLayout;
                                    lineBottom3 = lineBottom2 + staticLayout10.getLineBottom(staticLayout10.getLineCount() - r5);
                                }
                                lineBottom3 = lineBottom2;
                                setMeasuredDimension(View.MeasureSpec.getSize(i), Math.max(AndroidUtilities.dp(68.0f), Math.max(AndroidUtilities.dp(52.0f), (int) lineBottom3) + AndroidUtilities.dp(16.0f)) + (this.needDivider ? 1 : 0));
                                iDp3 = AndroidUtilities.dp(52.0f);
                                if (LocaleController.isRTL) {
                                    iDp4 = (View.MeasureSpec.getSize(i) - AndroidUtilities.dp(f3)) - iDp3;
                                } else {
                                    iDp4 = AndroidUtilities.dp(f3);
                                }
                                this.letterDrawable.setBounds(iDp4, AndroidUtilities.dp(f3), iDp4 + iDp3, AndroidUtilities.dp(60.0f));
                                float f6 = iDp3;
                                this.linkImageView.setImageCoords(iDp4, AndroidUtilities.dp(f3), f6, f6);
                                i5 = this.documentAttachType;
                                if (i5 != 3) {
                                    this.radialProgress.setCircleRadius(AndroidUtilities.dp(24.0f));
                                    this.radialProgress.setProgressRect(AndroidUtilities.dp(4.0f) + iDp4, AndroidUtilities.dp(12.0f), iDp4 + AndroidUtilities.dp(48.0f), AndroidUtilities.dp(56.0f));
                                } else {
                                    this.radialProgress.setCircleRadius(AndroidUtilities.dp(24.0f));
                                    this.radialProgress.setProgressRect(AndroidUtilities.dp(4.0f) + iDp4, AndroidUtilities.dp(12.0f), iDp4 + AndroidUtilities.dp(48.0f), AndroidUtilities.dp(56.0f));
                                }
                            }
                            checkBox2 = this.checkBox;
                            if (checkBox2 != null) {
                                measureChildWithMargins(checkBox2, i, 0, i2, 0);
                            }
                        }
                    } catch (Exception e6) {
                        e = e6;
                        z = true;
                    }
                } else {
                    i8 = 0;
                    f3 = 8.0f;
                    f = 1.0f;
                    z2 = true;
                    c = ' ';
                    i7 = iDp5;
                    c2 = '\n';
                }
                str8 = this.inlineResult.url;
                r5 = z2;
                if (str8 != null) {
                    this.linkLayout = new StaticLayout(TextUtils.ellipsize(this.inlineResult.url.replace(c2, c), Theme.chat_contextResult_descriptionTextPaint, Math.min((int) Math.ceil(Theme.chat_contextResult_descriptionTextPaint.measureText(str8)), i7), TextUtils.TruncateAt.MIDDLE), Theme.chat_contextResult_descriptionTextPaint, i7, Layout.Alignment.ALIGN_NORMAL, 1.0f, 0.0f, false);
                    r5 = z2;
                }
            }
            document = this.documentAttach;
            if (document != null) {
                if (!this.isForceGif || MessageObject.isGifDocument(document)) {
                    this.currentPhotoObject = FileLoader.getClosestPhotoSizeWithSize(this.documentAttach.thumbs, 90);
                } else {
                    if (MessageObject.isStickerDocument(this.documentAttach) || MessageObject.isAnimatedStickerDocument(this.documentAttach, r5)) {
                        this.currentPhotoObject = FileLoader.getClosestPhotoSizeWithSize(this.documentAttach.thumbs, 90);
                        str = "webp";
                        closestPhotoSizeWithSize = null;
                    } else {
                        int i16 = this.documentAttachType;
                        if (i16 != 5 && i16 != 3) {
                            this.currentPhotoObject = FileLoader.getClosestPhotoSizeWithSize(this.documentAttach.thumbs, 90);
                        }
                    }
                    botInlineResult2 = this.inlineResult;
                    if (botInlineResult2 != null) {
                        if ((botInlineResult2.content instanceof TLRPC.TL_webDocument) || (str7 = botInlineResult2.type) == null) {
                            tL_webDocument = null;
                        } else if (str7.startsWith("gif")) {
                            TLRPC.WebDocument webDocument2 = this.inlineResult.thumb;
                            if ((webDocument2 instanceof TLRPC.TL_webDocument) && "video/mp4".equals(webDocument2.mime_type)) {
                                tL_webDocument = (TLRPC.TL_webDocument) this.inlineResult.thumb;
                            } else {
                                tL_webDocument = (TLRPC.TL_webDocument) this.inlineResult.content;
                            }
                            this.documentAttachType = 2;
                        } else if (this.inlineResult.type.equals("photo")) {
                            TLRPC.BotInlineResult botInlineResult6 = this.inlineResult;
                            TLRPC.WebDocument webDocument3 = botInlineResult6.thumb;
                            if (webDocument3 instanceof TLRPC.TL_webDocument) {
                                tL_webDocument = (TLRPC.TL_webDocument) webDocument3;
                            } else {
                                tL_webDocument = (TLRPC.TL_webDocument) botInlineResult6.content;
                            }
                        } else {
                            tL_webDocument = null;
                        }
                        if (tL_webDocument == null) {
                            webDocument = this.inlineResult.thumb;
                            if (webDocument instanceof TLRPC.TL_webDocument) {
                                tL_webDocument = (TLRPC.TL_webDocument) webDocument;
                            }
                        }
                        if (tL_webDocument != null && this.currentPhotoObject == null && closestPhotoSizeWithSize == null) {
                            TLRPC.BotInlineMessage botInlineMessage = this.inlineResult.send_message;
                            if ((botInlineMessage instanceof TLRPC.TL_botInlineMessageMediaVenue) || (botInlineMessage instanceof TLRPC.TL_botInlineMessageMediaGeo)) {
                                TLRPC.GeoPoint geoPoint = botInlineMessage.geo;
                                double d = geoPoint.lat;
                                double d2 = geoPoint._long;
                                if (MessagesController.getInstance(this.currentAccount).mapProvider == 2) {
                                    webFileCreateWithWebDocument = WebFile.createWithGeoPoint(this.inlineResult.send_message.geo, 72, 72, 15, Math.min(2, (int) Math.ceil(AndroidUtilities.density)));
                                    strFormapMapUrl = null;
                                } else {
                                    strFormapMapUrl = AndroidUtilities.formapMapUrl(this.currentAccount, d, d2, 72, 72, true, 15, -1);
                                }
                            } else {
                                strFormapMapUrl = null;
                            }
                            webFileCreateWithWebDocument = null;
                        } else {
                            strFormapMapUrl = null;
                            webFileCreateWithWebDocument = null;
                        }
                        if (tL_webDocument != null) {
                            webFileCreateWithWebDocument = WebFile.createWithWebDocument(tL_webDocument);
                        }
                    } else {
                        strFormapMapUrl = null;
                        webFileCreateWithWebDocument = null;
                    }
                    if (this.documentAttach != null) {
                        i6 = i8;
                        while (true) {
                            if (i6 < this.documentAttach.attributes.size()) {
                                documentAttribute = this.documentAttach.attributes.get(i6);
                                if (!(documentAttribute instanceof TLRPC.TL_documentAttributeImageSize) || (documentAttribute instanceof TLRPC.TL_documentAttributeVideo)) {
                                    iDp = documentAttribute.w;
                                    i3 = documentAttribute.h;
                                } else {
                                    i6++;
                                }
                            } else {
                                iDp = i8;
                                i3 = iDp;
                            }
                        }
                    } else {
                        iDp = i8;
                        i3 = iDp;
                    }
                    if (iDp != 0 || i3 == 0) {
                        photoSize = this.currentPhotoObject;
                        if (photoSize != null) {
                            if (closestPhotoSizeWithSize != null) {
                                closestPhotoSizeWithSize.size = -1;
                            }
                            iDp = photoSize.w;
                            i3 = photoSize.h;
                        } else {
                            botInlineResult3 = this.inlineResult;
                            if (botInlineResult3 != null) {
                                int[] inlineResultWidthAndHeight7 = MessageObject.getInlineResultWidthAndHeight(botInlineResult3);
                                int i17 = inlineResultWidthAndHeight7[i8];
                                i3 = inlineResultWidthAndHeight7[r5];
                                iDp = i17;
                            }
                        }
                    }
                    if (iDp != 0 || i3 == 0) {
                        iDp = AndroidUtilities.dp(80.0f);
                        i3 = iDp;
                    }
                    if (this.documentAttach != null && this.currentPhotoObject == null && webFileCreateWithWebDocument == null && strFormapMapUrl == null) {
                        i4 = 2;
                        r4 = i8;
                    } else {
                        if (this.mediaWebpage) {
                            iDp2 = (int) (iDp / (i3 / AndroidUtilities.dp(80.0f)));
                            if (this.documentAttachType == 2) {
                                Locale locale13 = Locale.US;
                                Object[] objArr13 = new Object[2];
                                objArr13[i8] = Integer.valueOf((int) (iDp2 / AndroidUtilities.density));
                                objArr13[r5] = 80;
                                str6 = String.format(locale13, "%d_%d_b", objArr13);
                                if (!SharedConfig.isAutoplayGifs() || this.isKeyboard) {
                                    str3 = str6;
                                    str2 = str3;
                                } else {
                                    str5 = str6 + "_firstframe";
                                    str4 = str6 + "_firstframe";
                                }
                            } else {
                                Locale locale14 = Locale.US;
                                Object[] objArr14 = new Object[2];
                                objArr14[i8] = Integer.valueOf((int) (iDp2 / AndroidUtilities.density));
                                objArr14[r5] = 80;
                                str4 = String.format(locale14, "%d_%d", objArr14);
                                str5 = str4 + "_b";
                            }
                            str3 = str4;
                            str2 = str5;
                        } else {
                            str2 = "52_52_b";
                            str3 = "52_52";
                        }
                        ?? r12 = this.linkImageView;
                        if (this.documentAttachType == 6) {
                            r11 = r5;
                        } else {
                            r11 = i8;
                        }
                        r12.setAspectFit(r11);
                        if (this.documentAttachType == 2) {
                            document3 = this.documentAttach;
                            if (document3 != null) {
                                documentVideoThumb = MessageObject.getDocumentVideoThumb(document3);
                                if (documentVideoThumb != null) {
                                    ImageReceiver imageReceiver13 = this.linkImageView;
                                    ImageLocation forDocument8 = ImageLocation.getForDocument(documentVideoThumb, this.documentAttach);
                                    StringBuilder sb13 = new StringBuilder();
                                    sb13.append("100_100");
                                    sb13.append((!SharedConfig.isAutoplayGifs() || this.isKeyboard) ? _UrlKt.FRAGMENT_ENCODE_SET : "_firstframe");
                                    imageReceiver13.setImage(forDocument8, sb13.toString(), ImageLocation.getForDocument(this.currentPhotoObject, this.documentAttach), str3, -1L, str, this.parentObject, 1);
                                } else {
                                    forDocument = ImageLocation.getForDocument(this.documentAttach);
                                    if (this.isForceGif) {
                                        forDocument.imageType = 2;
                                    }
                                    ImageReceiver imageReceiver14 = this.linkImageView;
                                    StringBuilder sb14 = new StringBuilder();
                                    sb14.append("100_100");
                                    sb14.append((!SharedConfig.isAutoplayGifs() || this.isKeyboard) ? _UrlKt.FRAGMENT_ENCODE_SET : "_firstframe");
                                    imageReceiver14.setImage(forDocument, sb14.toString(), ImageLocation.getForDocument(this.currentPhotoObject, this.documentAttach), str3, this.documentAttach.size, str, this.parentObject, 0);
                                }
                            } else if (webFileCreateWithWebDocument != null) {
                                this.linkImageView.setImage(ImageLocation.getForWebFile(webFileCreateWithWebDocument), "100_100", ImageLocation.getForPhoto(this.currentPhotoObject, this.photoAttach), str3, -1L, str, this.parentObject, 1);
                            } else {
                                this.linkImageView.setImage(ImageLocation.getForPath(strFormapMapUrl), "100_100", ImageLocation.getForPhoto(this.currentPhotoObject, this.photoAttach), str3, -1L, str, this.parentObject, 1);
                            }
                            i4 = 2;
                        } else if (this.currentPhotoObject != null) {
                            svgThumb = DocumentObject.getSvgThumb(this.documentAttach, Theme.key_windowBackgroundGray, f);
                            if (!MessageObject.canAutoplayAnimatedSticker(this.documentAttach)) {
                                i4 = 2;
                                document2 = this.documentAttach;
                                if (document2 == null) {
                                    this.linkImageView.setImage(ImageLocation.getForPhoto(this.currentPhotoObject, this.photoAttach), str3, ImageLocation.getForPhoto(closestPhotoSizeWithSize, this.photoAttach), str2, this.currentPhotoObject.size, str, this.parentObject, 0);
                                } else if (svgThumb != null) {
                                    this.linkImageView.setImage(ImageLocation.getForDocument(this.currentPhotoObject, document2), str3, svgThumb, this.currentPhotoObject.size, str, this.parentObject, 0);
                                } else {
                                    this.linkImageView.setImage(ImageLocation.getForDocument(this.currentPhotoObject, document2), str3, ImageLocation.getForPhoto(closestPhotoSizeWithSize, this.photoAttach), str2, this.currentPhotoObject.size, str, this.parentObject, 0);
                                }
                            } else if (svgThumb != null) {
                                i4 = 2;
                                this.linkImageView.setImage(ImageLocation.getForDocument(this.documentAttach), "80_80", svgThumb, this.currentPhotoObject.size, str, this.parentObject, 0);
                            } else {
                                i4 = 2;
                                this.linkImageView.setImage(ImageLocation.getForDocument(this.documentAttach), "80_80", ImageLocation.getForDocument(this.currentPhotoObject, this.documentAttach), str2, this.currentPhotoObject.size, str, this.parentObject, 0);
                            }
                        } else {
                            i4 = 2;
                            if (webFileCreateWithWebDocument != null) {
                                this.linkImageView.setImage(ImageLocation.getForWebFile(webFileCreateWithWebDocument), str3, ImageLocation.getForPhoto(closestPhotoSizeWithSize, this.photoAttach), str2, -1L, str, this.parentObject, 1);
                            } else {
                                this.linkImageView.setImage(ImageLocation.getForPath(strFormapMapUrl), str3, ImageLocation.getForPhoto(closestPhotoSizeWithSize, this.photoAttach), str2, -1L, str, this.parentObject, 1);
                            }
                        }
                        if (!SharedConfig.isAutoplayGifs() || this.isKeyboard) {
                            r3 = i8;
                            this.linkImageView.setAllowStartAnimation(r5);
                            this.linkImageView.startAnimation();
                        } else {
                            ?? r13 = i8;
                            this.linkImageView.setAllowStartAnimation(r13);
                            this.linkImageView.stopAnimation();
                            r3 = r13;
                        }
                        this.drawLinkImageView = r5;
                        r4 = r3;
                    }
                    if (this.mediaWebpage) {
                        size = View.MeasureSpec.getSize(i2);
                        if (size == 0) {
                            size = AndroidUtilities.dp(f2);
                        }
                        setMeasuredDimension(size2, size);
                        int iDp12 = (size2 - AndroidUtilities.dp(24.0f)) / i4;
                        int iDp13 = (size - AndroidUtilities.dp(24.0f)) / i4;
                        this.radialProgress.setProgressRect(iDp12, iDp13, AndroidUtilities.dp(24.0f) + iDp12, AndroidUtilities.dp(24.0f) + iDp13);
                        this.radialProgress.setCircleRadius(AndroidUtilities.dp(12.0f));
                        this.linkImageView.setImageCoords(0.0f, 0.0f, size2, size);
                    } else {
                        staticLayout = this.titleLayout;
                        if (staticLayout != null || staticLayout.getLineCount() == 0) {
                            lineBottom = r4;
                        } else {
                            StaticLayout staticLayout11 = this.titleLayout;
                            lineBottom = staticLayout11.getLineBottom(staticLayout11.getLineCount() - r5);
                        }
                        staticLayout2 = this.descriptionLayout;
                        lineBottom2 = lineBottom;
                        if (staticLayout2 != null && staticLayout2.getLineCount() != 0) {
                            lineBottom2 = lineBottom;
                            StaticLayout staticLayout12 = this.descriptionLayout;
                            lineBottom2 = lineBottom + staticLayout12.getLineBottom(staticLayout12.getLineCount() - r5);
                        }
                        lineBottom2 = lineBottom;
                        staticLayout3 = this.linkLayout;
                        lineBottom3 = lineBottom2;
                        if (staticLayout3 != null && staticLayout3.getLineCount() > 0) {
                            lineBottom3 = lineBottom2;
                            StaticLayout staticLayout13 = this.linkLayout;
                            lineBottom3 = lineBottom2 + staticLayout13.getLineBottom(staticLayout13.getLineCount() - r5);
                        }
                        lineBottom3 = lineBottom2;
                        setMeasuredDimension(View.MeasureSpec.getSize(i), Math.max(AndroidUtilities.dp(68.0f), Math.max(AndroidUtilities.dp(52.0f), (int) lineBottom3) + AndroidUtilities.dp(16.0f)) + (this.needDivider ? 1 : 0));
                        iDp3 = AndroidUtilities.dp(52.0f);
                        if (LocaleController.isRTL) {
                            iDp4 = (View.MeasureSpec.getSize(i) - AndroidUtilities.dp(f3)) - iDp3;
                        } else {
                            iDp4 = AndroidUtilities.dp(f3);
                        }
                        this.letterDrawable.setBounds(iDp4, AndroidUtilities.dp(f3), iDp4 + iDp3, AndroidUtilities.dp(60.0f));
                        float f7 = iDp3;
                        this.linkImageView.setImageCoords(iDp4, AndroidUtilities.dp(f3), f7, f7);
                        i5 = this.documentAttachType;
                        if (i5 != 3 || i5 == 5) {
                            this.radialProgress.setCircleRadius(AndroidUtilities.dp(24.0f));
                            this.radialProgress.setProgressRect(AndroidUtilities.dp(4.0f) + iDp4, AndroidUtilities.dp(12.0f), iDp4 + AndroidUtilities.dp(48.0f), AndroidUtilities.dp(56.0f));
                        }
                    }
                    checkBox2 = this.checkBox;
                    if (checkBox2 != null) {
                        measureChildWithMargins(checkBox2, i, 0, i2, 0);
                    }
                }
            } else {
                botInlineResult = this.inlineResult;
                if (botInlineResult == null && botInlineResult.photo != null) {
                    this.currentPhotoObject = FileLoader.getClosestPhotoSizeWithSize(arrayList, AndroidUtilities.getPhotoSize(), r5);
                    closestPhotoSizeWithSize = FileLoader.getClosestPhotoSizeWithSize(arrayList, 80);
                    if (closestPhotoSizeWithSize == this.currentPhotoObject) {
                    }
                }
                str = null;
                botInlineResult2 = this.inlineResult;
                if (botInlineResult2 != null) {
                    if (botInlineResult2.content instanceof TLRPC.TL_webDocument) {
                        tL_webDocument = null;
                    } else {
                        tL_webDocument = null;
                    }
                    if (tL_webDocument == null) {
                        webDocument = this.inlineResult.thumb;
                        if (webDocument instanceof TLRPC.TL_webDocument) {
                            tL_webDocument = (TLRPC.TL_webDocument) webDocument;
                        }
                    }
                    if (tL_webDocument != null) {
                        strFormapMapUrl = null;
                        webFileCreateWithWebDocument = null;
                    } else {
                        strFormapMapUrl = null;
                        webFileCreateWithWebDocument = null;
                    }
                    if (tL_webDocument != null) {
                        webFileCreateWithWebDocument = WebFile.createWithWebDocument(tL_webDocument);
                    }
                } else {
                    strFormapMapUrl = null;
                    webFileCreateWithWebDocument = null;
                }
                if (this.documentAttach != null) {
                    i6 = i8;
                    while (true) {
                        if (i6 < this.documentAttach.attributes.size()) {
                            documentAttribute = this.documentAttach.attributes.get(i6);
                            if (documentAttribute instanceof TLRPC.TL_documentAttributeImageSize) {
                            }
                            iDp = documentAttribute.w;
                            i3 = documentAttribute.h;
                        } else {
                            iDp = i8;
                            i3 = iDp;
                        }
                        i6++;
                    }
                } else {
                    iDp = i8;
                    i3 = iDp;
                }
                if (iDp != 0) {
                    photoSize = this.currentPhotoObject;
                    if (photoSize != null) {
                        if (closestPhotoSizeWithSize != null) {
                            closestPhotoSizeWithSize.size = -1;
                        }
                        iDp = photoSize.w;
                        i3 = photoSize.h;
                    } else {
                        botInlineResult3 = this.inlineResult;
                        if (botInlineResult3 != null) {
                            int[] inlineResultWidthAndHeight8 = MessageObject.getInlineResultWidthAndHeight(botInlineResult3);
                            int i18 = inlineResultWidthAndHeight8[i8];
                            i3 = inlineResultWidthAndHeight8[r5];
                            iDp = i18;
                        }
                    }
                } else {
                    photoSize = this.currentPhotoObject;
                    if (photoSize != null) {
                        if (closestPhotoSizeWithSize != null) {
                            closestPhotoSizeWithSize.size = -1;
                        }
                        iDp = photoSize.w;
                        i3 = photoSize.h;
                    } else {
                        botInlineResult3 = this.inlineResult;
                        if (botInlineResult3 != null) {
                            int[] inlineResultWidthAndHeight9 = MessageObject.getInlineResultWidthAndHeight(botInlineResult3);
                            int i19 = inlineResultWidthAndHeight9[i8];
                            i3 = inlineResultWidthAndHeight9[r5];
                            iDp = i19;
                        }
                    }
                }
                if (iDp != 0) {
                    iDp = AndroidUtilities.dp(80.0f);
                    i3 = iDp;
                } else {
                    iDp = AndroidUtilities.dp(80.0f);
                    i3 = iDp;
                }
                if (this.documentAttach != null) {
                    if (this.mediaWebpage) {
                        iDp2 = (int) (iDp / (i3 / AndroidUtilities.dp(80.0f)));
                        if (this.documentAttachType == 2) {
                            Locale locale15 = Locale.US;
                            Object[] objArr15 = new Object[2];
                            objArr15[i8] = Integer.valueOf((int) (iDp2 / AndroidUtilities.density));
                            objArr15[r5] = 80;
                            str6 = String.format(locale15, "%d_%d_b", objArr15);
                            if (SharedConfig.isAutoplayGifs()) {
                            }
                            str3 = str6;
                            str2 = str3;
                        } else {
                            Locale locale16 = Locale.US;
                            Object[] objArr16 = new Object[2];
                            objArr16[i8] = Integer.valueOf((int) (iDp2 / AndroidUtilities.density));
                            objArr16[r5] = 80;
                            str4 = String.format(locale16, "%d_%d", objArr16);
                            str5 = str4 + "_b";
                        }
                        str3 = str4;
                        str2 = str5;
                    } else {
                        str2 = "52_52_b";
                        str3 = "52_52";
                    }
                    ?? r14 = this.linkImageView;
                    if (this.documentAttachType == 6) {
                        r11 = r5;
                    } else {
                        r11 = i8;
                    }
                    r14.setAspectFit(r11);
                    if (this.documentAttachType == 2) {
                        document3 = this.documentAttach;
                        if (document3 != null) {
                            documentVideoThumb = MessageObject.getDocumentVideoThumb(document3);
                            if (documentVideoThumb != null) {
                                ImageReceiver imageReceiver15 = this.linkImageView;
                                ImageLocation forDocument9 = ImageLocation.getForDocument(documentVideoThumb, this.documentAttach);
                                StringBuilder sb15 = new StringBuilder();
                                sb15.append("100_100");
                                sb15.append((!SharedConfig.isAutoplayGifs() || this.isKeyboard) ? _UrlKt.FRAGMENT_ENCODE_SET : "_firstframe");
                                imageReceiver15.setImage(forDocument9, sb15.toString(), ImageLocation.getForDocument(this.currentPhotoObject, this.documentAttach), str3, -1L, str, this.parentObject, 1);
                            } else {
                                forDocument = ImageLocation.getForDocument(this.documentAttach);
                                if (this.isForceGif) {
                                    forDocument.imageType = 2;
                                }
                                ImageReceiver imageReceiver16 = this.linkImageView;
                                StringBuilder sb16 = new StringBuilder();
                                sb16.append("100_100");
                                sb16.append((!SharedConfig.isAutoplayGifs() || this.isKeyboard) ? _UrlKt.FRAGMENT_ENCODE_SET : "_firstframe");
                                imageReceiver16.setImage(forDocument, sb16.toString(), ImageLocation.getForDocument(this.currentPhotoObject, this.documentAttach), str3, this.documentAttach.size, str, this.parentObject, 0);
                            }
                        } else if (webFileCreateWithWebDocument != null) {
                            this.linkImageView.setImage(ImageLocation.getForWebFile(webFileCreateWithWebDocument), "100_100", ImageLocation.getForPhoto(this.currentPhotoObject, this.photoAttach), str3, -1L, str, this.parentObject, 1);
                        } else {
                            this.linkImageView.setImage(ImageLocation.getForPath(strFormapMapUrl), "100_100", ImageLocation.getForPhoto(this.currentPhotoObject, this.photoAttach), str3, -1L, str, this.parentObject, 1);
                        }
                        i4 = 2;
                    } else if (this.currentPhotoObject != null) {
                        svgThumb = DocumentObject.getSvgThumb(this.documentAttach, Theme.key_windowBackgroundGray, f);
                        if (!MessageObject.canAutoplayAnimatedSticker(this.documentAttach)) {
                            i4 = 2;
                            document2 = this.documentAttach;
                            if (document2 == null) {
                                this.linkImageView.setImage(ImageLocation.getForPhoto(this.currentPhotoObject, this.photoAttach), str3, ImageLocation.getForPhoto(closestPhotoSizeWithSize, this.photoAttach), str2, this.currentPhotoObject.size, str, this.parentObject, 0);
                            } else if (svgThumb != null) {
                                this.linkImageView.setImage(ImageLocation.getForDocument(this.currentPhotoObject, document2), str3, svgThumb, this.currentPhotoObject.size, str, this.parentObject, 0);
                            } else {
                                this.linkImageView.setImage(ImageLocation.getForDocument(this.currentPhotoObject, document2), str3, ImageLocation.getForPhoto(closestPhotoSizeWithSize, this.photoAttach), str2, this.currentPhotoObject.size, str, this.parentObject, 0);
                            }
                        } else if (svgThumb != null) {
                            i4 = 2;
                            this.linkImageView.setImage(ImageLocation.getForDocument(this.documentAttach), "80_80", svgThumb, this.currentPhotoObject.size, str, this.parentObject, 0);
                        } else {
                            i4 = 2;
                            this.linkImageView.setImage(ImageLocation.getForDocument(this.documentAttach), "80_80", ImageLocation.getForDocument(this.currentPhotoObject, this.documentAttach), str2, this.currentPhotoObject.size, str, this.parentObject, 0);
                        }
                    } else {
                        i4 = 2;
                        if (webFileCreateWithWebDocument != null) {
                            this.linkImageView.setImage(ImageLocation.getForWebFile(webFileCreateWithWebDocument), str3, ImageLocation.getForPhoto(closestPhotoSizeWithSize, this.photoAttach), str2, -1L, str, this.parentObject, 1);
                        } else {
                            this.linkImageView.setImage(ImageLocation.getForPath(strFormapMapUrl), str3, ImageLocation.getForPhoto(closestPhotoSizeWithSize, this.photoAttach), str2, -1L, str, this.parentObject, 1);
                        }
                    }
                    if (!SharedConfig.isAutoplayGifs()) {
                        r3 = i8;
                        this.linkImageView.setAllowStartAnimation(r5);
                        this.linkImageView.startAnimation();
                    } else {
                        r3 = i8;
                        this.linkImageView.setAllowStartAnimation(r5);
                        this.linkImageView.startAnimation();
                    }
                    this.drawLinkImageView = r5;
                    r4 = r3;
                } else {
                    if (this.mediaWebpage) {
                        iDp2 = (int) (iDp / (i3 / AndroidUtilities.dp(80.0f)));
                        if (this.documentAttachType == 2) {
                            Locale locale17 = Locale.US;
                            Object[] objArr17 = new Object[2];
                            objArr17[i8] = Integer.valueOf((int) (iDp2 / AndroidUtilities.density));
                            objArr17[r5] = 80;
                            str6 = String.format(locale17, "%d_%d_b", objArr17);
                            if (SharedConfig.isAutoplayGifs()) {
                            }
                            str3 = str6;
                            str2 = str3;
                        } else {
                            Locale locale18 = Locale.US;
                            Object[] objArr18 = new Object[2];
                            objArr18[i8] = Integer.valueOf((int) (iDp2 / AndroidUtilities.density));
                            objArr18[r5] = 80;
                            str4 = String.format(locale18, "%d_%d", objArr18);
                            str5 = str4 + "_b";
                        }
                        str3 = str4;
                        str2 = str5;
                    } else {
                        str2 = "52_52_b";
                        str3 = "52_52";
                    }
                    ?? r15 = this.linkImageView;
                    if (this.documentAttachType == 6) {
                        r11 = r5;
                    } else {
                        r11 = i8;
                    }
                    r15.setAspectFit(r11);
                    if (this.documentAttachType == 2) {
                        document3 = this.documentAttach;
                        if (document3 != null) {
                            documentVideoThumb = MessageObject.getDocumentVideoThumb(document3);
                            if (documentVideoThumb != null) {
                                ImageReceiver imageReceiver17 = this.linkImageView;
                                ImageLocation forDocument10 = ImageLocation.getForDocument(documentVideoThumb, this.documentAttach);
                                StringBuilder sb17 = new StringBuilder();
                                sb17.append("100_100");
                                sb17.append((!SharedConfig.isAutoplayGifs() || this.isKeyboard) ? _UrlKt.FRAGMENT_ENCODE_SET : "_firstframe");
                                imageReceiver17.setImage(forDocument10, sb17.toString(), ImageLocation.getForDocument(this.currentPhotoObject, this.documentAttach), str3, -1L, str, this.parentObject, 1);
                            } else {
                                forDocument = ImageLocation.getForDocument(this.documentAttach);
                                if (this.isForceGif) {
                                    forDocument.imageType = 2;
                                }
                                ImageReceiver imageReceiver18 = this.linkImageView;
                                StringBuilder sb18 = new StringBuilder();
                                sb18.append("100_100");
                                sb18.append((!SharedConfig.isAutoplayGifs() || this.isKeyboard) ? _UrlKt.FRAGMENT_ENCODE_SET : "_firstframe");
                                imageReceiver18.setImage(forDocument, sb18.toString(), ImageLocation.getForDocument(this.currentPhotoObject, this.documentAttach), str3, this.documentAttach.size, str, this.parentObject, 0);
                            }
                        } else if (webFileCreateWithWebDocument != null) {
                            this.linkImageView.setImage(ImageLocation.getForWebFile(webFileCreateWithWebDocument), "100_100", ImageLocation.getForPhoto(this.currentPhotoObject, this.photoAttach), str3, -1L, str, this.parentObject, 1);
                        } else {
                            this.linkImageView.setImage(ImageLocation.getForPath(strFormapMapUrl), "100_100", ImageLocation.getForPhoto(this.currentPhotoObject, this.photoAttach), str3, -1L, str, this.parentObject, 1);
                        }
                        i4 = 2;
                    } else if (this.currentPhotoObject != null) {
                        svgThumb = DocumentObject.getSvgThumb(this.documentAttach, Theme.key_windowBackgroundGray, f);
                        if (!MessageObject.canAutoplayAnimatedSticker(this.documentAttach)) {
                            i4 = 2;
                            document2 = this.documentAttach;
                            if (document2 == null) {
                                this.linkImageView.setImage(ImageLocation.getForPhoto(this.currentPhotoObject, this.photoAttach), str3, ImageLocation.getForPhoto(closestPhotoSizeWithSize, this.photoAttach), str2, this.currentPhotoObject.size, str, this.parentObject, 0);
                            } else if (svgThumb != null) {
                                this.linkImageView.setImage(ImageLocation.getForDocument(this.currentPhotoObject, document2), str3, svgThumb, this.currentPhotoObject.size, str, this.parentObject, 0);
                            } else {
                                this.linkImageView.setImage(ImageLocation.getForDocument(this.currentPhotoObject, document2), str3, ImageLocation.getForPhoto(closestPhotoSizeWithSize, this.photoAttach), str2, this.currentPhotoObject.size, str, this.parentObject, 0);
                            }
                        } else if (svgThumb != null) {
                            i4 = 2;
                            this.linkImageView.setImage(ImageLocation.getForDocument(this.documentAttach), "80_80", svgThumb, this.currentPhotoObject.size, str, this.parentObject, 0);
                        } else {
                            i4 = 2;
                            this.linkImageView.setImage(ImageLocation.getForDocument(this.documentAttach), "80_80", ImageLocation.getForDocument(this.currentPhotoObject, this.documentAttach), str2, this.currentPhotoObject.size, str, this.parentObject, 0);
                        }
                    } else {
                        i4 = 2;
                        if (webFileCreateWithWebDocument != null) {
                            this.linkImageView.setImage(ImageLocation.getForWebFile(webFileCreateWithWebDocument), str3, ImageLocation.getForPhoto(closestPhotoSizeWithSize, this.photoAttach), str2, -1L, str, this.parentObject, 1);
                        } else {
                            this.linkImageView.setImage(ImageLocation.getForPath(strFormapMapUrl), str3, ImageLocation.getForPhoto(closestPhotoSizeWithSize, this.photoAttach), str2, -1L, str, this.parentObject, 1);
                        }
                    }
                    if (!SharedConfig.isAutoplayGifs()) {
                        r3 = i8;
                        this.linkImageView.setAllowStartAnimation(r5);
                        this.linkImageView.startAnimation();
                    } else {
                        r3 = i8;
                        this.linkImageView.setAllowStartAnimation(r5);
                        this.linkImageView.startAnimation();
                    }
                    this.drawLinkImageView = r5;
                    r4 = r3;
                }
                if (this.mediaWebpage) {
                    size = View.MeasureSpec.getSize(i2);
                    if (size == 0) {
                        size = AndroidUtilities.dp(f2);
                    }
                    setMeasuredDimension(size2, size);
                    int iDp14 = (size2 - AndroidUtilities.dp(24.0f)) / i4;
                    int iDp15 = (size - AndroidUtilities.dp(24.0f)) / i4;
                    this.radialProgress.setProgressRect(iDp14, iDp15, AndroidUtilities.dp(24.0f) + iDp14, AndroidUtilities.dp(24.0f) + iDp15);
                    this.radialProgress.setCircleRadius(AndroidUtilities.dp(12.0f));
                    this.linkImageView.setImageCoords(0.0f, 0.0f, size2, size);
                } else {
                    staticLayout = this.titleLayout;
                    if (staticLayout != null) {
                        lineBottom = r4;
                    } else {
                        lineBottom = r4;
                    }
                    staticLayout2 = this.descriptionLayout;
                    lineBottom2 = lineBottom;
                    if (staticLayout2 != null) {
                        lineBottom2 = lineBottom;
                        StaticLayout staticLayout14 = this.descriptionLayout;
                        lineBottom2 = lineBottom + staticLayout14.getLineBottom(staticLayout14.getLineCount() - r5);
                    }
                    lineBottom2 = lineBottom;
                    staticLayout3 = this.linkLayout;
                    lineBottom3 = lineBottom2;
                    if (staticLayout3 != null) {
                        lineBottom3 = lineBottom2;
                        StaticLayout staticLayout15 = this.linkLayout;
                        lineBottom3 = lineBottom2 + staticLayout15.getLineBottom(staticLayout15.getLineCount() - r5);
                    }
                    lineBottom3 = lineBottom2;
                    setMeasuredDimension(View.MeasureSpec.getSize(i), Math.max(AndroidUtilities.dp(68.0f), Math.max(AndroidUtilities.dp(52.0f), (int) lineBottom3) + AndroidUtilities.dp(16.0f)) + (this.needDivider ? 1 : 0));
                    iDp3 = AndroidUtilities.dp(52.0f);
                    if (LocaleController.isRTL) {
                        iDp4 = (View.MeasureSpec.getSize(i) - AndroidUtilities.dp(f3)) - iDp3;
                    } else {
                        iDp4 = AndroidUtilities.dp(f3);
                    }
                    this.letterDrawable.setBounds(iDp4, AndroidUtilities.dp(f3), iDp4 + iDp3, AndroidUtilities.dp(60.0f));
                    float f8 = iDp3;
                    this.linkImageView.setImageCoords(iDp4, AndroidUtilities.dp(f3), f8, f8);
                    i5 = this.documentAttachType;
                    if (i5 != 3) {
                        this.radialProgress.setCircleRadius(AndroidUtilities.dp(24.0f));
                        this.radialProgress.setProgressRect(AndroidUtilities.dp(4.0f) + iDp4, AndroidUtilities.dp(12.0f), iDp4 + AndroidUtilities.dp(48.0f), AndroidUtilities.dp(56.0f));
                    } else {
                        this.radialProgress.setCircleRadius(AndroidUtilities.dp(24.0f));
                        this.radialProgress.setProgressRect(AndroidUtilities.dp(4.0f) + iDp4, AndroidUtilities.dp(12.0f), iDp4 + AndroidUtilities.dp(48.0f), AndroidUtilities.dp(56.0f));
                    }
                }
                checkBox2 = this.checkBox;
                if (checkBox2 != null) {
                    measureChildWithMargins(checkBox2, i, 0, i2, 0);
                }
            }
            closestPhotoSizeWithSize = null;
            str = null;
            botInlineResult2 = this.inlineResult;
            if (botInlineResult2 != null) {
                if (botInlineResult2.content instanceof TLRPC.TL_webDocument) {
                    tL_webDocument = null;
                } else {
                    tL_webDocument = null;
                }
                if (tL_webDocument == null) {
                    webDocument = this.inlineResult.thumb;
                    if (webDocument instanceof TLRPC.TL_webDocument) {
                        tL_webDocument = (TLRPC.TL_webDocument) webDocument;
                    }
                }
                if (tL_webDocument != null) {
                    strFormapMapUrl = null;
                    webFileCreateWithWebDocument = null;
                } else {
                    strFormapMapUrl = null;
                    webFileCreateWithWebDocument = null;
                }
                if (tL_webDocument != null) {
                    webFileCreateWithWebDocument = WebFile.createWithWebDocument(tL_webDocument);
                }
            } else {
                strFormapMapUrl = null;
                webFileCreateWithWebDocument = null;
            }
            if (this.documentAttach != null) {
                i6 = i8;
                while (true) {
                    if (i6 < this.documentAttach.attributes.size()) {
                        documentAttribute = this.documentAttach.attributes.get(i6);
                        if (documentAttribute instanceof TLRPC.TL_documentAttributeImageSize) {
                        }
                        iDp = documentAttribute.w;
                        i3 = documentAttribute.h;
                    } else {
                        iDp = i8;
                        i3 = iDp;
                    }
                    i6++;
                }
            } else {
                iDp = i8;
                i3 = iDp;
            }
            if (iDp != 0) {
                photoSize = this.currentPhotoObject;
                if (photoSize != null) {
                    if (closestPhotoSizeWithSize != null) {
                        closestPhotoSizeWithSize.size = -1;
                    }
                    iDp = photoSize.w;
                    i3 = photoSize.h;
                } else {
                    botInlineResult3 = this.inlineResult;
                    if (botInlineResult3 != null) {
                        int[] inlineResultWidthAndHeight10 = MessageObject.getInlineResultWidthAndHeight(botInlineResult3);
                        int i110 = inlineResultWidthAndHeight10[i8];
                        i3 = inlineResultWidthAndHeight10[r5];
                        iDp = i110;
                    }
                }
            } else {
                photoSize = this.currentPhotoObject;
                if (photoSize != null) {
                    if (closestPhotoSizeWithSize != null) {
                        closestPhotoSizeWithSize.size = -1;
                    }
                    iDp = photoSize.w;
                    i3 = photoSize.h;
                } else {
                    botInlineResult3 = this.inlineResult;
                    if (botInlineResult3 != null) {
                        int[] inlineResultWidthAndHeight11 = MessageObject.getInlineResultWidthAndHeight(botInlineResult3);
                        int i111 = inlineResultWidthAndHeight11[i8];
                        i3 = inlineResultWidthAndHeight11[r5];
                        iDp = i111;
                    }
                }
            }
            if (iDp != 0) {
                iDp = AndroidUtilities.dp(80.0f);
                i3 = iDp;
            } else {
                iDp = AndroidUtilities.dp(80.0f);
                i3 = iDp;
            }
            if (this.documentAttach != null) {
                if (this.mediaWebpage) {
                    iDp2 = (int) (iDp / (i3 / AndroidUtilities.dp(80.0f)));
                    if (this.documentAttachType == 2) {
                        Locale locale19 = Locale.US;
                        Object[] objArr19 = new Object[2];
                        objArr19[i8] = Integer.valueOf((int) (iDp2 / AndroidUtilities.density));
                        objArr19[r5] = 80;
                        str6 = String.format(locale19, "%d_%d_b", objArr19);
                        if (SharedConfig.isAutoplayGifs()) {
                        }
                        str3 = str6;
                        str2 = str3;
                    } else {
                        Locale locale110 = Locale.US;
                        Object[] objArr110 = new Object[2];
                        objArr110[i8] = Integer.valueOf((int) (iDp2 / AndroidUtilities.density));
                        objArr110[r5] = 80;
                        str4 = String.format(locale110, "%d_%d", objArr110);
                        str5 = str4 + "_b";
                    }
                    str3 = str4;
                    str2 = str5;
                } else {
                    str2 = "52_52_b";
                    str3 = "52_52";
                }
                ?? r16 = this.linkImageView;
                if (this.documentAttachType == 6) {
                    r11 = r5;
                } else {
                    r11 = i8;
                }
                r16.setAspectFit(r11);
                if (this.documentAttachType == 2) {
                    document3 = this.documentAttach;
                    if (document3 != null) {
                        documentVideoThumb = MessageObject.getDocumentVideoThumb(document3);
                        if (documentVideoThumb != null) {
                            ImageReceiver imageReceiver19 = this.linkImageView;
                            ImageLocation forDocument11 = ImageLocation.getForDocument(documentVideoThumb, this.documentAttach);
                            StringBuilder sb19 = new StringBuilder();
                            sb19.append("100_100");
                            sb19.append((!SharedConfig.isAutoplayGifs() || this.isKeyboard) ? _UrlKt.FRAGMENT_ENCODE_SET : "_firstframe");
                            imageReceiver19.setImage(forDocument11, sb19.toString(), ImageLocation.getForDocument(this.currentPhotoObject, this.documentAttach), str3, -1L, str, this.parentObject, 1);
                        } else {
                            forDocument = ImageLocation.getForDocument(this.documentAttach);
                            if (this.isForceGif) {
                                forDocument.imageType = 2;
                            }
                            ImageReceiver imageReceiver110 = this.linkImageView;
                            StringBuilder sb110 = new StringBuilder();
                            sb110.append("100_100");
                            sb110.append((!SharedConfig.isAutoplayGifs() || this.isKeyboard) ? _UrlKt.FRAGMENT_ENCODE_SET : "_firstframe");
                            imageReceiver110.setImage(forDocument, sb110.toString(), ImageLocation.getForDocument(this.currentPhotoObject, this.documentAttach), str3, this.documentAttach.size, str, this.parentObject, 0);
                        }
                    } else if (webFileCreateWithWebDocument != null) {
                        this.linkImageView.setImage(ImageLocation.getForWebFile(webFileCreateWithWebDocument), "100_100", ImageLocation.getForPhoto(this.currentPhotoObject, this.photoAttach), str3, -1L, str, this.parentObject, 1);
                    } else {
                        this.linkImageView.setImage(ImageLocation.getForPath(strFormapMapUrl), "100_100", ImageLocation.getForPhoto(this.currentPhotoObject, this.photoAttach), str3, -1L, str, this.parentObject, 1);
                    }
                    i4 = 2;
                } else if (this.currentPhotoObject != null) {
                    svgThumb = DocumentObject.getSvgThumb(this.documentAttach, Theme.key_windowBackgroundGray, f);
                    if (!MessageObject.canAutoplayAnimatedSticker(this.documentAttach)) {
                        i4 = 2;
                        document2 = this.documentAttach;
                        if (document2 == null) {
                            this.linkImageView.setImage(ImageLocation.getForPhoto(this.currentPhotoObject, this.photoAttach), str3, ImageLocation.getForPhoto(closestPhotoSizeWithSize, this.photoAttach), str2, this.currentPhotoObject.size, str, this.parentObject, 0);
                        } else if (svgThumb != null) {
                            this.linkImageView.setImage(ImageLocation.getForDocument(this.currentPhotoObject, document2), str3, svgThumb, this.currentPhotoObject.size, str, this.parentObject, 0);
                        } else {
                            this.linkImageView.setImage(ImageLocation.getForDocument(this.currentPhotoObject, document2), str3, ImageLocation.getForPhoto(closestPhotoSizeWithSize, this.photoAttach), str2, this.currentPhotoObject.size, str, this.parentObject, 0);
                        }
                    } else if (svgThumb != null) {
                        i4 = 2;
                        this.linkImageView.setImage(ImageLocation.getForDocument(this.documentAttach), "80_80", svgThumb, this.currentPhotoObject.size, str, this.parentObject, 0);
                    } else {
                        i4 = 2;
                        this.linkImageView.setImage(ImageLocation.getForDocument(this.documentAttach), "80_80", ImageLocation.getForDocument(this.currentPhotoObject, this.documentAttach), str2, this.currentPhotoObject.size, str, this.parentObject, 0);
                    }
                } else {
                    i4 = 2;
                    if (webFileCreateWithWebDocument != null) {
                        this.linkImageView.setImage(ImageLocation.getForWebFile(webFileCreateWithWebDocument), str3, ImageLocation.getForPhoto(closestPhotoSizeWithSize, this.photoAttach), str2, -1L, str, this.parentObject, 1);
                    } else {
                        this.linkImageView.setImage(ImageLocation.getForPath(strFormapMapUrl), str3, ImageLocation.getForPhoto(closestPhotoSizeWithSize, this.photoAttach), str2, -1L, str, this.parentObject, 1);
                    }
                }
                if (!SharedConfig.isAutoplayGifs()) {
                    r3 = i8;
                    this.linkImageView.setAllowStartAnimation(r5);
                    this.linkImageView.startAnimation();
                } else {
                    r3 = i8;
                    this.linkImageView.setAllowStartAnimation(r5);
                    this.linkImageView.startAnimation();
                }
                this.drawLinkImageView = r5;
                r4 = r3;
            } else {
                if (this.mediaWebpage) {
                    iDp2 = (int) (iDp / (i3 / AndroidUtilities.dp(80.0f)));
                    if (this.documentAttachType == 2) {
                        Locale locale111 = Locale.US;
                        Object[] objArr111 = new Object[2];
                        objArr111[i8] = Integer.valueOf((int) (iDp2 / AndroidUtilities.density));
                        objArr111[r5] = 80;
                        str6 = String.format(locale111, "%d_%d_b", objArr111);
                        if (SharedConfig.isAutoplayGifs()) {
                        }
                        str3 = str6;
                        str2 = str3;
                    } else {
                        Locale locale112 = Locale.US;
                        Object[] objArr112 = new Object[2];
                        objArr112[i8] = Integer.valueOf((int) (iDp2 / AndroidUtilities.density));
                        objArr112[r5] = 80;
                        str4 = String.format(locale112, "%d_%d", objArr112);
                        str5 = str4 + "_b";
                    }
                    str3 = str4;
                    str2 = str5;
                } else {
                    str2 = "52_52_b";
                    str3 = "52_52";
                }
                ?? r17 = this.linkImageView;
                if (this.documentAttachType == 6) {
                    r11 = r5;
                } else {
                    r11 = i8;
                }
                r17.setAspectFit(r11);
                if (this.documentAttachType == 2) {
                    document3 = this.documentAttach;
                    if (document3 != null) {
                        documentVideoThumb = MessageObject.getDocumentVideoThumb(document3);
                        if (documentVideoThumb != null) {
                            ImageReceiver imageReceiver111 = this.linkImageView;
                            ImageLocation forDocument12 = ImageLocation.getForDocument(documentVideoThumb, this.documentAttach);
                            StringBuilder sb111 = new StringBuilder();
                            sb111.append("100_100");
                            sb111.append((!SharedConfig.isAutoplayGifs() || this.isKeyboard) ? _UrlKt.FRAGMENT_ENCODE_SET : "_firstframe");
                            imageReceiver111.setImage(forDocument12, sb111.toString(), ImageLocation.getForDocument(this.currentPhotoObject, this.documentAttach), str3, -1L, str, this.parentObject, 1);
                        } else {
                            forDocument = ImageLocation.getForDocument(this.documentAttach);
                            if (this.isForceGif) {
                                forDocument.imageType = 2;
                            }
                            ImageReceiver imageReceiver112 = this.linkImageView;
                            StringBuilder sb112 = new StringBuilder();
                            sb112.append("100_100");
                            sb112.append((!SharedConfig.isAutoplayGifs() || this.isKeyboard) ? _UrlKt.FRAGMENT_ENCODE_SET : "_firstframe");
                            imageReceiver112.setImage(forDocument, sb112.toString(), ImageLocation.getForDocument(this.currentPhotoObject, this.documentAttach), str3, this.documentAttach.size, str, this.parentObject, 0);
                        }
                    } else if (webFileCreateWithWebDocument != null) {
                        this.linkImageView.setImage(ImageLocation.getForWebFile(webFileCreateWithWebDocument), "100_100", ImageLocation.getForPhoto(this.currentPhotoObject, this.photoAttach), str3, -1L, str, this.parentObject, 1);
                    } else {
                        this.linkImageView.setImage(ImageLocation.getForPath(strFormapMapUrl), "100_100", ImageLocation.getForPhoto(this.currentPhotoObject, this.photoAttach), str3, -1L, str, this.parentObject, 1);
                    }
                    i4 = 2;
                } else if (this.currentPhotoObject != null) {
                    svgThumb = DocumentObject.getSvgThumb(this.documentAttach, Theme.key_windowBackgroundGray, f);
                    if (!MessageObject.canAutoplayAnimatedSticker(this.documentAttach)) {
                        i4 = 2;
                        document2 = this.documentAttach;
                        if (document2 == null) {
                            this.linkImageView.setImage(ImageLocation.getForPhoto(this.currentPhotoObject, this.photoAttach), str3, ImageLocation.getForPhoto(closestPhotoSizeWithSize, this.photoAttach), str2, this.currentPhotoObject.size, str, this.parentObject, 0);
                        } else if (svgThumb != null) {
                            this.linkImageView.setImage(ImageLocation.getForDocument(this.currentPhotoObject, document2), str3, svgThumb, this.currentPhotoObject.size, str, this.parentObject, 0);
                        } else {
                            this.linkImageView.setImage(ImageLocation.getForDocument(this.currentPhotoObject, document2), str3, ImageLocation.getForPhoto(closestPhotoSizeWithSize, this.photoAttach), str2, this.currentPhotoObject.size, str, this.parentObject, 0);
                        }
                    } else if (svgThumb != null) {
                        i4 = 2;
                        this.linkImageView.setImage(ImageLocation.getForDocument(this.documentAttach), "80_80", svgThumb, this.currentPhotoObject.size, str, this.parentObject, 0);
                    } else {
                        i4 = 2;
                        this.linkImageView.setImage(ImageLocation.getForDocument(this.documentAttach), "80_80", ImageLocation.getForDocument(this.currentPhotoObject, this.documentAttach), str2, this.currentPhotoObject.size, str, this.parentObject, 0);
                    }
                } else {
                    i4 = 2;
                    if (webFileCreateWithWebDocument != null) {
                        this.linkImageView.setImage(ImageLocation.getForWebFile(webFileCreateWithWebDocument), str3, ImageLocation.getForPhoto(closestPhotoSizeWithSize, this.photoAttach), str2, -1L, str, this.parentObject, 1);
                    } else {
                        this.linkImageView.setImage(ImageLocation.getForPath(strFormapMapUrl), str3, ImageLocation.getForPhoto(closestPhotoSizeWithSize, this.photoAttach), str2, -1L, str, this.parentObject, 1);
                    }
                }
                if (!SharedConfig.isAutoplayGifs()) {
                    r3 = i8;
                    this.linkImageView.setAllowStartAnimation(r5);
                    this.linkImageView.startAnimation();
                } else {
                    r3 = i8;
                    this.linkImageView.setAllowStartAnimation(r5);
                    this.linkImageView.startAnimation();
                }
                this.drawLinkImageView = r5;
                r4 = r3;
            }
            if (this.mediaWebpage) {
                size = View.MeasureSpec.getSize(i2);
                if (size == 0) {
                    size = AndroidUtilities.dp(f2);
                }
                setMeasuredDimension(size2, size);
                int iDp16 = (size2 - AndroidUtilities.dp(24.0f)) / i4;
                int iDp17 = (size - AndroidUtilities.dp(24.0f)) / i4;
                this.radialProgress.setProgressRect(iDp16, iDp17, AndroidUtilities.dp(24.0f) + iDp16, AndroidUtilities.dp(24.0f) + iDp17);
                this.radialProgress.setCircleRadius(AndroidUtilities.dp(12.0f));
                this.linkImageView.setImageCoords(0.0f, 0.0f, size2, size);
            } else {
                staticLayout = this.titleLayout;
                if (staticLayout != null) {
                    lineBottom = r4;
                } else {
                    lineBottom = r4;
                }
                staticLayout2 = this.descriptionLayout;
                lineBottom2 = lineBottom;
                if (staticLayout2 != null) {
                    lineBottom2 = lineBottom;
                    StaticLayout staticLayout16 = this.descriptionLayout;
                    lineBottom2 = lineBottom + staticLayout16.getLineBottom(staticLayout16.getLineCount() - r5);
                }
                lineBottom2 = lineBottom;
                staticLayout3 = this.linkLayout;
                lineBottom3 = lineBottom2;
                if (staticLayout3 != null) {
                    lineBottom3 = lineBottom2;
                    StaticLayout staticLayout17 = this.linkLayout;
                    lineBottom3 = lineBottom2 + staticLayout17.getLineBottom(staticLayout17.getLineCount() - r5);
                }
                lineBottom3 = lineBottom2;
                setMeasuredDimension(View.MeasureSpec.getSize(i), Math.max(AndroidUtilities.dp(68.0f), Math.max(AndroidUtilities.dp(52.0f), (int) lineBottom3) + AndroidUtilities.dp(16.0f)) + (this.needDivider ? 1 : 0));
                iDp3 = AndroidUtilities.dp(52.0f);
                if (LocaleController.isRTL) {
                    iDp4 = (View.MeasureSpec.getSize(i) - AndroidUtilities.dp(f3)) - iDp3;
                } else {
                    iDp4 = AndroidUtilities.dp(f3);
                }
                this.letterDrawable.setBounds(iDp4, AndroidUtilities.dp(f3), iDp4 + iDp3, AndroidUtilities.dp(60.0f));
                float f9 = iDp3;
                this.linkImageView.setImageCoords(iDp4, AndroidUtilities.dp(f3), f9, f9);
                i5 = this.documentAttachType;
                if (i5 != 3) {
                    this.radialProgress.setCircleRadius(AndroidUtilities.dp(24.0f));
                    this.radialProgress.setProgressRect(AndroidUtilities.dp(4.0f) + iDp4, AndroidUtilities.dp(12.0f), iDp4 + AndroidUtilities.dp(48.0f), AndroidUtilities.dp(56.0f));
                } else {
                    this.radialProgress.setCircleRadius(AndroidUtilities.dp(24.0f));
                    this.radialProgress.setProgressRect(AndroidUtilities.dp(4.0f) + iDp4, AndroidUtilities.dp(12.0f), iDp4 + AndroidUtilities.dp(48.0f), AndroidUtilities.dp(56.0f));
                }
            }
            checkBox2 = this.checkBox;
            if (checkBox2 != null) {
                measureChildWithMargins(checkBox2, i, 0, i2, 0);
            }
        }
        arrayList = arrayList2;
        f = 1.0f;
        if (this.mediaWebpage) {
            i8 = 0;
            f2 = 100.0f;
            f3 = 8.0f;
            f = 1.0f;
            r5 = 1;
        } else {
            i8 = 0;
            f2 = 100.0f;
            f3 = 8.0f;
            f = 1.0f;
            r5 = 1;
        }
        document = this.documentAttach;
        if (document != null) {
            if (!this.isForceGif) {
                this.currentPhotoObject = FileLoader.getClosestPhotoSizeWithSize(this.documentAttach.thumbs, 90);
            } else {
                this.currentPhotoObject = FileLoader.getClosestPhotoSizeWithSize(this.documentAttach.thumbs, 90);
            }
            botInlineResult2 = this.inlineResult;
            if (botInlineResult2 != null) {
                if (botInlineResult2.content instanceof TLRPC.TL_webDocument) {
                    tL_webDocument = null;
                } else {
                    tL_webDocument = null;
                }
                if (tL_webDocument == null) {
                    webDocument = this.inlineResult.thumb;
                    if (webDocument instanceof TLRPC.TL_webDocument) {
                        tL_webDocument = (TLRPC.TL_webDocument) webDocument;
                    }
                }
                if (tL_webDocument != null) {
                    strFormapMapUrl = null;
                    webFileCreateWithWebDocument = null;
                } else {
                    strFormapMapUrl = null;
                    webFileCreateWithWebDocument = null;
                }
                if (tL_webDocument != null) {
                    webFileCreateWithWebDocument = WebFile.createWithWebDocument(tL_webDocument);
                }
            } else {
                strFormapMapUrl = null;
                webFileCreateWithWebDocument = null;
            }
            if (this.documentAttach != null) {
                i6 = i8;
                while (true) {
                    if (i6 < this.documentAttach.attributes.size()) {
                        documentAttribute = this.documentAttach.attributes.get(i6);
                        if (documentAttribute instanceof TLRPC.TL_documentAttributeImageSize) {
                        }
                        iDp = documentAttribute.w;
                        i3 = documentAttribute.h;
                    } else {
                        iDp = i8;
                        i3 = iDp;
                    }
                    i6++;
                }
            } else {
                iDp = i8;
                i3 = iDp;
            }
            if (iDp != 0) {
                photoSize = this.currentPhotoObject;
                if (photoSize != null) {
                    if (closestPhotoSizeWithSize != null) {
                        closestPhotoSizeWithSize.size = -1;
                    }
                    iDp = photoSize.w;
                    i3 = photoSize.h;
                } else {
                    botInlineResult3 = this.inlineResult;
                    if (botInlineResult3 != null) {
                        int[] inlineResultWidthAndHeight12 = MessageObject.getInlineResultWidthAndHeight(botInlineResult3);
                        int i112 = inlineResultWidthAndHeight12[i8];
                        i3 = inlineResultWidthAndHeight12[r5];
                        iDp = i112;
                    }
                }
            } else {
                photoSize = this.currentPhotoObject;
                if (photoSize != null) {
                    if (closestPhotoSizeWithSize != null) {
                        closestPhotoSizeWithSize.size = -1;
                    }
                    iDp = photoSize.w;
                    i3 = photoSize.h;
                } else {
                    botInlineResult3 = this.inlineResult;
                    if (botInlineResult3 != null) {
                        int[] inlineResultWidthAndHeight13 = MessageObject.getInlineResultWidthAndHeight(botInlineResult3);
                        int i113 = inlineResultWidthAndHeight13[i8];
                        i3 = inlineResultWidthAndHeight13[r5];
                        iDp = i113;
                    }
                }
            }
            if (iDp != 0) {
                iDp = AndroidUtilities.dp(80.0f);
                i3 = iDp;
            } else {
                iDp = AndroidUtilities.dp(80.0f);
                i3 = iDp;
            }
            if (this.documentAttach != null) {
                if (this.mediaWebpage) {
                    iDp2 = (int) (iDp / (i3 / AndroidUtilities.dp(80.0f)));
                    if (this.documentAttachType == 2) {
                        Locale locale113 = Locale.US;
                        Object[] objArr113 = new Object[2];
                        objArr113[i8] = Integer.valueOf((int) (iDp2 / AndroidUtilities.density));
                        objArr113[r5] = 80;
                        str6 = String.format(locale113, "%d_%d_b", objArr113);
                        if (SharedConfig.isAutoplayGifs()) {
                        }
                        str3 = str6;
                        str2 = str3;
                    } else {
                        Locale locale114 = Locale.US;
                        Object[] objArr114 = new Object[2];
                        objArr114[i8] = Integer.valueOf((int) (iDp2 / AndroidUtilities.density));
                        objArr114[r5] = 80;
                        str4 = String.format(locale114, "%d_%d", objArr114);
                        str5 = str4 + "_b";
                    }
                    str3 = str4;
                    str2 = str5;
                } else {
                    str2 = "52_52_b";
                    str3 = "52_52";
                }
                ?? r18 = this.linkImageView;
                if (this.documentAttachType == 6) {
                    r11 = r5;
                } else {
                    r11 = i8;
                }
                r18.setAspectFit(r11);
                if (this.documentAttachType == 2) {
                    document3 = this.documentAttach;
                    if (document3 != null) {
                        documentVideoThumb = MessageObject.getDocumentVideoThumb(document3);
                        if (documentVideoThumb != null) {
                            ImageReceiver imageReceiver113 = this.linkImageView;
                            ImageLocation forDocument13 = ImageLocation.getForDocument(documentVideoThumb, this.documentAttach);
                            StringBuilder sb113 = new StringBuilder();
                            sb113.append("100_100");
                            sb113.append((!SharedConfig.isAutoplayGifs() || this.isKeyboard) ? _UrlKt.FRAGMENT_ENCODE_SET : "_firstframe");
                            imageReceiver113.setImage(forDocument13, sb113.toString(), ImageLocation.getForDocument(this.currentPhotoObject, this.documentAttach), str3, -1L, str, this.parentObject, 1);
                        } else {
                            forDocument = ImageLocation.getForDocument(this.documentAttach);
                            if (this.isForceGif) {
                                forDocument.imageType = 2;
                            }
                            ImageReceiver imageReceiver114 = this.linkImageView;
                            StringBuilder sb114 = new StringBuilder();
                            sb114.append("100_100");
                            sb114.append((!SharedConfig.isAutoplayGifs() || this.isKeyboard) ? _UrlKt.FRAGMENT_ENCODE_SET : "_firstframe");
                            imageReceiver114.setImage(forDocument, sb114.toString(), ImageLocation.getForDocument(this.currentPhotoObject, this.documentAttach), str3, this.documentAttach.size, str, this.parentObject, 0);
                        }
                    } else if (webFileCreateWithWebDocument != null) {
                        this.linkImageView.setImage(ImageLocation.getForWebFile(webFileCreateWithWebDocument), "100_100", ImageLocation.getForPhoto(this.currentPhotoObject, this.photoAttach), str3, -1L, str, this.parentObject, 1);
                    } else {
                        this.linkImageView.setImage(ImageLocation.getForPath(strFormapMapUrl), "100_100", ImageLocation.getForPhoto(this.currentPhotoObject, this.photoAttach), str3, -1L, str, this.parentObject, 1);
                    }
                    i4 = 2;
                } else if (this.currentPhotoObject != null) {
                    svgThumb = DocumentObject.getSvgThumb(this.documentAttach, Theme.key_windowBackgroundGray, f);
                    if (!MessageObject.canAutoplayAnimatedSticker(this.documentAttach)) {
                        i4 = 2;
                        document2 = this.documentAttach;
                        if (document2 == null) {
                            this.linkImageView.setImage(ImageLocation.getForPhoto(this.currentPhotoObject, this.photoAttach), str3, ImageLocation.getForPhoto(closestPhotoSizeWithSize, this.photoAttach), str2, this.currentPhotoObject.size, str, this.parentObject, 0);
                        } else if (svgThumb != null) {
                            this.linkImageView.setImage(ImageLocation.getForDocument(this.currentPhotoObject, document2), str3, svgThumb, this.currentPhotoObject.size, str, this.parentObject, 0);
                        } else {
                            this.linkImageView.setImage(ImageLocation.getForDocument(this.currentPhotoObject, document2), str3, ImageLocation.getForPhoto(closestPhotoSizeWithSize, this.photoAttach), str2, this.currentPhotoObject.size, str, this.parentObject, 0);
                        }
                    } else if (svgThumb != null) {
                        i4 = 2;
                        this.linkImageView.setImage(ImageLocation.getForDocument(this.documentAttach), "80_80", svgThumb, this.currentPhotoObject.size, str, this.parentObject, 0);
                    } else {
                        i4 = 2;
                        this.linkImageView.setImage(ImageLocation.getForDocument(this.documentAttach), "80_80", ImageLocation.getForDocument(this.currentPhotoObject, this.documentAttach), str2, this.currentPhotoObject.size, str, this.parentObject, 0);
                    }
                } else {
                    i4 = 2;
                    if (webFileCreateWithWebDocument != null) {
                        this.linkImageView.setImage(ImageLocation.getForWebFile(webFileCreateWithWebDocument), str3, ImageLocation.getForPhoto(closestPhotoSizeWithSize, this.photoAttach), str2, -1L, str, this.parentObject, 1);
                    } else {
                        this.linkImageView.setImage(ImageLocation.getForPath(strFormapMapUrl), str3, ImageLocation.getForPhoto(closestPhotoSizeWithSize, this.photoAttach), str2, -1L, str, this.parentObject, 1);
                    }
                }
                if (!SharedConfig.isAutoplayGifs()) {
                    r3 = i8;
                    this.linkImageView.setAllowStartAnimation(r5);
                    this.linkImageView.startAnimation();
                } else {
                    r3 = i8;
                    this.linkImageView.setAllowStartAnimation(r5);
                    this.linkImageView.startAnimation();
                }
                this.drawLinkImageView = r5;
                r4 = r3;
            } else {
                if (this.mediaWebpage) {
                    iDp2 = (int) (iDp / (i3 / AndroidUtilities.dp(80.0f)));
                    if (this.documentAttachType == 2) {
                        Locale locale115 = Locale.US;
                        Object[] objArr115 = new Object[2];
                        objArr115[i8] = Integer.valueOf((int) (iDp2 / AndroidUtilities.density));
                        objArr115[r5] = 80;
                        str6 = String.format(locale115, "%d_%d_b", objArr115);
                        if (SharedConfig.isAutoplayGifs()) {
                        }
                        str3 = str6;
                        str2 = str3;
                    } else {
                        Locale locale116 = Locale.US;
                        Object[] objArr116 = new Object[2];
                        objArr116[i8] = Integer.valueOf((int) (iDp2 / AndroidUtilities.density));
                        objArr116[r5] = 80;
                        str4 = String.format(locale116, "%d_%d", objArr116);
                        str5 = str4 + "_b";
                    }
                    str3 = str4;
                    str2 = str5;
                } else {
                    str2 = "52_52_b";
                    str3 = "52_52";
                }
                ?? r19 = this.linkImageView;
                if (this.documentAttachType == 6) {
                    r11 = r5;
                } else {
                    r11 = i8;
                }
                r19.setAspectFit(r11);
                if (this.documentAttachType == 2) {
                    document3 = this.documentAttach;
                    if (document3 != null) {
                        documentVideoThumb = MessageObject.getDocumentVideoThumb(document3);
                        if (documentVideoThumb != null) {
                            ImageReceiver imageReceiver115 = this.linkImageView;
                            ImageLocation forDocument14 = ImageLocation.getForDocument(documentVideoThumb, this.documentAttach);
                            StringBuilder sb115 = new StringBuilder();
                            sb115.append("100_100");
                            sb115.append((!SharedConfig.isAutoplayGifs() || this.isKeyboard) ? _UrlKt.FRAGMENT_ENCODE_SET : "_firstframe");
                            imageReceiver115.setImage(forDocument14, sb115.toString(), ImageLocation.getForDocument(this.currentPhotoObject, this.documentAttach), str3, -1L, str, this.parentObject, 1);
                        } else {
                            forDocument = ImageLocation.getForDocument(this.documentAttach);
                            if (this.isForceGif) {
                                forDocument.imageType = 2;
                            }
                            ImageReceiver imageReceiver116 = this.linkImageView;
                            StringBuilder sb116 = new StringBuilder();
                            sb116.append("100_100");
                            sb116.append((!SharedConfig.isAutoplayGifs() || this.isKeyboard) ? _UrlKt.FRAGMENT_ENCODE_SET : "_firstframe");
                            imageReceiver116.setImage(forDocument, sb116.toString(), ImageLocation.getForDocument(this.currentPhotoObject, this.documentAttach), str3, this.documentAttach.size, str, this.parentObject, 0);
                        }
                    } else if (webFileCreateWithWebDocument != null) {
                        this.linkImageView.setImage(ImageLocation.getForWebFile(webFileCreateWithWebDocument), "100_100", ImageLocation.getForPhoto(this.currentPhotoObject, this.photoAttach), str3, -1L, str, this.parentObject, 1);
                    } else {
                        this.linkImageView.setImage(ImageLocation.getForPath(strFormapMapUrl), "100_100", ImageLocation.getForPhoto(this.currentPhotoObject, this.photoAttach), str3, -1L, str, this.parentObject, 1);
                    }
                    i4 = 2;
                } else if (this.currentPhotoObject != null) {
                    svgThumb = DocumentObject.getSvgThumb(this.documentAttach, Theme.key_windowBackgroundGray, f);
                    if (!MessageObject.canAutoplayAnimatedSticker(this.documentAttach)) {
                        i4 = 2;
                        document2 = this.documentAttach;
                        if (document2 == null) {
                            this.linkImageView.setImage(ImageLocation.getForPhoto(this.currentPhotoObject, this.photoAttach), str3, ImageLocation.getForPhoto(closestPhotoSizeWithSize, this.photoAttach), str2, this.currentPhotoObject.size, str, this.parentObject, 0);
                        } else if (svgThumb != null) {
                            this.linkImageView.setImage(ImageLocation.getForDocument(this.currentPhotoObject, document2), str3, svgThumb, this.currentPhotoObject.size, str, this.parentObject, 0);
                        } else {
                            this.linkImageView.setImage(ImageLocation.getForDocument(this.currentPhotoObject, document2), str3, ImageLocation.getForPhoto(closestPhotoSizeWithSize, this.photoAttach), str2, this.currentPhotoObject.size, str, this.parentObject, 0);
                        }
                    } else if (svgThumb != null) {
                        i4 = 2;
                        this.linkImageView.setImage(ImageLocation.getForDocument(this.documentAttach), "80_80", svgThumb, this.currentPhotoObject.size, str, this.parentObject, 0);
                    } else {
                        i4 = 2;
                        this.linkImageView.setImage(ImageLocation.getForDocument(this.documentAttach), "80_80", ImageLocation.getForDocument(this.currentPhotoObject, this.documentAttach), str2, this.currentPhotoObject.size, str, this.parentObject, 0);
                    }
                } else {
                    i4 = 2;
                    if (webFileCreateWithWebDocument != null) {
                        this.linkImageView.setImage(ImageLocation.getForWebFile(webFileCreateWithWebDocument), str3, ImageLocation.getForPhoto(closestPhotoSizeWithSize, this.photoAttach), str2, -1L, str, this.parentObject, 1);
                    } else {
                        this.linkImageView.setImage(ImageLocation.getForPath(strFormapMapUrl), str3, ImageLocation.getForPhoto(closestPhotoSizeWithSize, this.photoAttach), str2, -1L, str, this.parentObject, 1);
                    }
                }
                if (!SharedConfig.isAutoplayGifs()) {
                    r3 = i8;
                    this.linkImageView.setAllowStartAnimation(r5);
                    this.linkImageView.startAnimation();
                } else {
                    r3 = i8;
                    this.linkImageView.setAllowStartAnimation(r5);
                    this.linkImageView.startAnimation();
                }
                this.drawLinkImageView = r5;
                r4 = r3;
            }
            if (this.mediaWebpage) {
                size = View.MeasureSpec.getSize(i2);
                if (size == 0) {
                    size = AndroidUtilities.dp(f2);
                }
                setMeasuredDimension(size2, size);
                int iDp18 = (size2 - AndroidUtilities.dp(24.0f)) / i4;
                int iDp19 = (size - AndroidUtilities.dp(24.0f)) / i4;
                this.radialProgress.setProgressRect(iDp18, iDp19, AndroidUtilities.dp(24.0f) + iDp18, AndroidUtilities.dp(24.0f) + iDp19);
                this.radialProgress.setCircleRadius(AndroidUtilities.dp(12.0f));
                this.linkImageView.setImageCoords(0.0f, 0.0f, size2, size);
            } else {
                staticLayout = this.titleLayout;
                if (staticLayout != null) {
                    lineBottom = r4;
                } else {
                    lineBottom = r4;
                }
                staticLayout2 = this.descriptionLayout;
                lineBottom2 = lineBottom;
                if (staticLayout2 != null) {
                    lineBottom2 = lineBottom;
                    StaticLayout staticLayout18 = this.descriptionLayout;
                    lineBottom2 = lineBottom + staticLayout18.getLineBottom(staticLayout18.getLineCount() - r5);
                }
                lineBottom2 = lineBottom;
                staticLayout3 = this.linkLayout;
                lineBottom3 = lineBottom2;
                if (staticLayout3 != null) {
                    lineBottom3 = lineBottom2;
                    StaticLayout staticLayout19 = this.linkLayout;
                    lineBottom3 = lineBottom2 + staticLayout19.getLineBottom(staticLayout19.getLineCount() - r5);
                }
                lineBottom3 = lineBottom2;
                setMeasuredDimension(View.MeasureSpec.getSize(i), Math.max(AndroidUtilities.dp(68.0f), Math.max(AndroidUtilities.dp(52.0f), (int) lineBottom3) + AndroidUtilities.dp(16.0f)) + (this.needDivider ? 1 : 0));
                iDp3 = AndroidUtilities.dp(52.0f);
                if (LocaleController.isRTL) {
                    iDp4 = (View.MeasureSpec.getSize(i) - AndroidUtilities.dp(f3)) - iDp3;
                } else {
                    iDp4 = AndroidUtilities.dp(f3);
                }
                this.letterDrawable.setBounds(iDp4, AndroidUtilities.dp(f3), iDp4 + iDp3, AndroidUtilities.dp(60.0f));
                float f10 = iDp3;
                this.linkImageView.setImageCoords(iDp4, AndroidUtilities.dp(f3), f10, f10);
                i5 = this.documentAttachType;
                if (i5 != 3) {
                    this.radialProgress.setCircleRadius(AndroidUtilities.dp(24.0f));
                    this.radialProgress.setProgressRect(AndroidUtilities.dp(4.0f) + iDp4, AndroidUtilities.dp(12.0f), iDp4 + AndroidUtilities.dp(48.0f), AndroidUtilities.dp(56.0f));
                } else {
                    this.radialProgress.setCircleRadius(AndroidUtilities.dp(24.0f));
                    this.radialProgress.setProgressRect(AndroidUtilities.dp(4.0f) + iDp4, AndroidUtilities.dp(12.0f), iDp4 + AndroidUtilities.dp(48.0f), AndroidUtilities.dp(56.0f));
                }
            }
            checkBox2 = this.checkBox;
            if (checkBox2 != null) {
                measureChildWithMargins(checkBox2, i, 0, i2, 0);
            }
        }
        botInlineResult = this.inlineResult;
        if (botInlineResult == null) {
        }
        str = null;
        botInlineResult2 = this.inlineResult;
        if (botInlineResult2 != null) {
            if (botInlineResult2.content instanceof TLRPC.TL_webDocument) {
                tL_webDocument = null;
            } else {
                tL_webDocument = null;
            }
            if (tL_webDocument == null) {
                webDocument = this.inlineResult.thumb;
                if (webDocument instanceof TLRPC.TL_webDocument) {
                    tL_webDocument = (TLRPC.TL_webDocument) webDocument;
                }
            }
            if (tL_webDocument != null) {
                strFormapMapUrl = null;
                webFileCreateWithWebDocument = null;
            } else {
                strFormapMapUrl = null;
                webFileCreateWithWebDocument = null;
            }
            if (tL_webDocument != null) {
                webFileCreateWithWebDocument = WebFile.createWithWebDocument(tL_webDocument);
            }
        } else {
            strFormapMapUrl = null;
            webFileCreateWithWebDocument = null;
        }
        if (this.documentAttach != null) {
            i6 = i8;
            while (true) {
                if (i6 < this.documentAttach.attributes.size()) {
                    documentAttribute = this.documentAttach.attributes.get(i6);
                    if (documentAttribute instanceof TLRPC.TL_documentAttributeImageSize) {
                    }
                    iDp = documentAttribute.w;
                    i3 = documentAttribute.h;
                } else {
                    iDp = i8;
                    i3 = iDp;
                }
                i6++;
            }
        } else {
            iDp = i8;
            i3 = iDp;
        }
        if (iDp != 0) {
            photoSize = this.currentPhotoObject;
            if (photoSize != null) {
                if (closestPhotoSizeWithSize != null) {
                    closestPhotoSizeWithSize.size = -1;
                }
                iDp = photoSize.w;
                i3 = photoSize.h;
            } else {
                botInlineResult3 = this.inlineResult;
                if (botInlineResult3 != null) {
                    int[] inlineResultWidthAndHeight14 = MessageObject.getInlineResultWidthAndHeight(botInlineResult3);
                    int i114 = inlineResultWidthAndHeight14[i8];
                    i3 = inlineResultWidthAndHeight14[r5];
                    iDp = i114;
                }
            }
        } else {
            photoSize = this.currentPhotoObject;
            if (photoSize != null) {
                if (closestPhotoSizeWithSize != null) {
                    closestPhotoSizeWithSize.size = -1;
                }
                iDp = photoSize.w;
                i3 = photoSize.h;
            } else {
                botInlineResult3 = this.inlineResult;
                if (botInlineResult3 != null) {
                    int[] inlineResultWidthAndHeight15 = MessageObject.getInlineResultWidthAndHeight(botInlineResult3);
                    int i115 = inlineResultWidthAndHeight15[i8];
                    i3 = inlineResultWidthAndHeight15[r5];
                    iDp = i115;
                }
            }
        }
        if (iDp != 0) {
            iDp = AndroidUtilities.dp(80.0f);
            i3 = iDp;
        } else {
            iDp = AndroidUtilities.dp(80.0f);
            i3 = iDp;
        }
        if (this.documentAttach != null) {
            if (this.mediaWebpage) {
                iDp2 = (int) (iDp / (i3 / AndroidUtilities.dp(80.0f)));
                if (this.documentAttachType == 2) {
                    Locale locale117 = Locale.US;
                    Object[] objArr117 = new Object[2];
                    objArr117[i8] = Integer.valueOf((int) (iDp2 / AndroidUtilities.density));
                    objArr117[r5] = 80;
                    str6 = String.format(locale117, "%d_%d_b", objArr117);
                    if (SharedConfig.isAutoplayGifs()) {
                    }
                    str3 = str6;
                    str2 = str3;
                } else {
                    Locale locale118 = Locale.US;
                    Object[] objArr118 = new Object[2];
                    objArr118[i8] = Integer.valueOf((int) (iDp2 / AndroidUtilities.density));
                    objArr118[r5] = 80;
                    str4 = String.format(locale118, "%d_%d", objArr118);
                    str5 = str4 + "_b";
                }
                str3 = str4;
                str2 = str5;
            } else {
                str2 = "52_52_b";
                str3 = "52_52";
            }
            ?? r110 = this.linkImageView;
            if (this.documentAttachType == 6) {
                r11 = r5;
            } else {
                r11 = i8;
            }
            r110.setAspectFit(r11);
            if (this.documentAttachType == 2) {
                document3 = this.documentAttach;
                if (document3 != null) {
                    documentVideoThumb = MessageObject.getDocumentVideoThumb(document3);
                    if (documentVideoThumb != null) {
                        ImageReceiver imageReceiver117 = this.linkImageView;
                        ImageLocation forDocument15 = ImageLocation.getForDocument(documentVideoThumb, this.documentAttach);
                        StringBuilder sb117 = new StringBuilder();
                        sb117.append("100_100");
                        sb117.append((!SharedConfig.isAutoplayGifs() || this.isKeyboard) ? _UrlKt.FRAGMENT_ENCODE_SET : "_firstframe");
                        imageReceiver117.setImage(forDocument15, sb117.toString(), ImageLocation.getForDocument(this.currentPhotoObject, this.documentAttach), str3, -1L, str, this.parentObject, 1);
                    } else {
                        forDocument = ImageLocation.getForDocument(this.documentAttach);
                        if (this.isForceGif) {
                            forDocument.imageType = 2;
                        }
                        ImageReceiver imageReceiver118 = this.linkImageView;
                        StringBuilder sb118 = new StringBuilder();
                        sb118.append("100_100");
                        sb118.append((!SharedConfig.isAutoplayGifs() || this.isKeyboard) ? _UrlKt.FRAGMENT_ENCODE_SET : "_firstframe");
                        imageReceiver118.setImage(forDocument, sb118.toString(), ImageLocation.getForDocument(this.currentPhotoObject, this.documentAttach), str3, this.documentAttach.size, str, this.parentObject, 0);
                    }
                } else if (webFileCreateWithWebDocument != null) {
                    this.linkImageView.setImage(ImageLocation.getForWebFile(webFileCreateWithWebDocument), "100_100", ImageLocation.getForPhoto(this.currentPhotoObject, this.photoAttach), str3, -1L, str, this.parentObject, 1);
                } else {
                    this.linkImageView.setImage(ImageLocation.getForPath(strFormapMapUrl), "100_100", ImageLocation.getForPhoto(this.currentPhotoObject, this.photoAttach), str3, -1L, str, this.parentObject, 1);
                }
                i4 = 2;
            } else if (this.currentPhotoObject != null) {
                svgThumb = DocumentObject.getSvgThumb(this.documentAttach, Theme.key_windowBackgroundGray, f);
                if (!MessageObject.canAutoplayAnimatedSticker(this.documentAttach)) {
                    i4 = 2;
                    document2 = this.documentAttach;
                    if (document2 == null) {
                        this.linkImageView.setImage(ImageLocation.getForPhoto(this.currentPhotoObject, this.photoAttach), str3, ImageLocation.getForPhoto(closestPhotoSizeWithSize, this.photoAttach), str2, this.currentPhotoObject.size, str, this.parentObject, 0);
                    } else if (svgThumb != null) {
                        this.linkImageView.setImage(ImageLocation.getForDocument(this.currentPhotoObject, document2), str3, svgThumb, this.currentPhotoObject.size, str, this.parentObject, 0);
                    } else {
                        this.linkImageView.setImage(ImageLocation.getForDocument(this.currentPhotoObject, document2), str3, ImageLocation.getForPhoto(closestPhotoSizeWithSize, this.photoAttach), str2, this.currentPhotoObject.size, str, this.parentObject, 0);
                    }
                } else if (svgThumb != null) {
                    i4 = 2;
                    this.linkImageView.setImage(ImageLocation.getForDocument(this.documentAttach), "80_80", svgThumb, this.currentPhotoObject.size, str, this.parentObject, 0);
                } else {
                    i4 = 2;
                    this.linkImageView.setImage(ImageLocation.getForDocument(this.documentAttach), "80_80", ImageLocation.getForDocument(this.currentPhotoObject, this.documentAttach), str2, this.currentPhotoObject.size, str, this.parentObject, 0);
                }
            } else {
                i4 = 2;
                if (webFileCreateWithWebDocument != null) {
                    this.linkImageView.setImage(ImageLocation.getForWebFile(webFileCreateWithWebDocument), str3, ImageLocation.getForPhoto(closestPhotoSizeWithSize, this.photoAttach), str2, -1L, str, this.parentObject, 1);
                } else {
                    this.linkImageView.setImage(ImageLocation.getForPath(strFormapMapUrl), str3, ImageLocation.getForPhoto(closestPhotoSizeWithSize, this.photoAttach), str2, -1L, str, this.parentObject, 1);
                }
            }
            if (!SharedConfig.isAutoplayGifs()) {
                r3 = i8;
                this.linkImageView.setAllowStartAnimation(r5);
                this.linkImageView.startAnimation();
            } else {
                r3 = i8;
                this.linkImageView.setAllowStartAnimation(r5);
                this.linkImageView.startAnimation();
            }
            this.drawLinkImageView = r5;
            r4 = r3;
        } else {
            if (this.mediaWebpage) {
                iDp2 = (int) (iDp / (i3 / AndroidUtilities.dp(80.0f)));
                if (this.documentAttachType == 2) {
                    Locale locale119 = Locale.US;
                    Object[] objArr119 = new Object[2];
                    objArr119[i8] = Integer.valueOf((int) (iDp2 / AndroidUtilities.density));
                    objArr119[r5] = 80;
                    str6 = String.format(locale119, "%d_%d_b", objArr119);
                    if (SharedConfig.isAutoplayGifs()) {
                    }
                    str3 = str6;
                    str2 = str3;
                } else {
                    Locale locale1110 = Locale.US;
                    Object[] objArr1110 = new Object[2];
                    objArr1110[i8] = Integer.valueOf((int) (iDp2 / AndroidUtilities.density));
                    objArr1110[r5] = 80;
                    str4 = String.format(locale1110, "%d_%d", objArr1110);
                    str5 = str4 + "_b";
                }
                str3 = str4;
                str2 = str5;
            } else {
                str2 = "52_52_b";
                str3 = "52_52";
            }
            ?? r111 = this.linkImageView;
            if (this.documentAttachType == 6) {
                r11 = r5;
            } else {
                r11 = i8;
            }
            r111.setAspectFit(r11);
            if (this.documentAttachType == 2) {
                document3 = this.documentAttach;
                if (document3 != null) {
                    documentVideoThumb = MessageObject.getDocumentVideoThumb(document3);
                    if (documentVideoThumb != null) {
                        ImageReceiver imageReceiver119 = this.linkImageView;
                        ImageLocation forDocument16 = ImageLocation.getForDocument(documentVideoThumb, this.documentAttach);
                        StringBuilder sb119 = new StringBuilder();
                        sb119.append("100_100");
                        sb119.append((!SharedConfig.isAutoplayGifs() || this.isKeyboard) ? _UrlKt.FRAGMENT_ENCODE_SET : "_firstframe");
                        imageReceiver119.setImage(forDocument16, sb119.toString(), ImageLocation.getForDocument(this.currentPhotoObject, this.documentAttach), str3, -1L, str, this.parentObject, 1);
                    } else {
                        forDocument = ImageLocation.getForDocument(this.documentAttach);
                        if (this.isForceGif) {
                            forDocument.imageType = 2;
                        }
                        ImageReceiver imageReceiver1110 = this.linkImageView;
                        StringBuilder sb1110 = new StringBuilder();
                        sb1110.append("100_100");
                        sb1110.append((!SharedConfig.isAutoplayGifs() || this.isKeyboard) ? _UrlKt.FRAGMENT_ENCODE_SET : "_firstframe");
                        imageReceiver1110.setImage(forDocument, sb1110.toString(), ImageLocation.getForDocument(this.currentPhotoObject, this.documentAttach), str3, this.documentAttach.size, str, this.parentObject, 0);
                    }
                } else if (webFileCreateWithWebDocument != null) {
                    this.linkImageView.setImage(ImageLocation.getForWebFile(webFileCreateWithWebDocument), "100_100", ImageLocation.getForPhoto(this.currentPhotoObject, this.photoAttach), str3, -1L, str, this.parentObject, 1);
                } else {
                    this.linkImageView.setImage(ImageLocation.getForPath(strFormapMapUrl), "100_100", ImageLocation.getForPhoto(this.currentPhotoObject, this.photoAttach), str3, -1L, str, this.parentObject, 1);
                }
                i4 = 2;
            } else if (this.currentPhotoObject != null) {
                svgThumb = DocumentObject.getSvgThumb(this.documentAttach, Theme.key_windowBackgroundGray, f);
                if (!MessageObject.canAutoplayAnimatedSticker(this.documentAttach)) {
                    i4 = 2;
                    document2 = this.documentAttach;
                    if (document2 == null) {
                        this.linkImageView.setImage(ImageLocation.getForPhoto(this.currentPhotoObject, this.photoAttach), str3, ImageLocation.getForPhoto(closestPhotoSizeWithSize, this.photoAttach), str2, this.currentPhotoObject.size, str, this.parentObject, 0);
                    } else if (svgThumb != null) {
                        this.linkImageView.setImage(ImageLocation.getForDocument(this.currentPhotoObject, document2), str3, svgThumb, this.currentPhotoObject.size, str, this.parentObject, 0);
                    } else {
                        this.linkImageView.setImage(ImageLocation.getForDocument(this.currentPhotoObject, document2), str3, ImageLocation.getForPhoto(closestPhotoSizeWithSize, this.photoAttach), str2, this.currentPhotoObject.size, str, this.parentObject, 0);
                    }
                } else if (svgThumb != null) {
                    i4 = 2;
                    this.linkImageView.setImage(ImageLocation.getForDocument(this.documentAttach), "80_80", svgThumb, this.currentPhotoObject.size, str, this.parentObject, 0);
                } else {
                    i4 = 2;
                    this.linkImageView.setImage(ImageLocation.getForDocument(this.documentAttach), "80_80", ImageLocation.getForDocument(this.currentPhotoObject, this.documentAttach), str2, this.currentPhotoObject.size, str, this.parentObject, 0);
                }
            } else {
                i4 = 2;
                if (webFileCreateWithWebDocument != null) {
                    this.linkImageView.setImage(ImageLocation.getForWebFile(webFileCreateWithWebDocument), str3, ImageLocation.getForPhoto(closestPhotoSizeWithSize, this.photoAttach), str2, -1L, str, this.parentObject, 1);
                } else {
                    this.linkImageView.setImage(ImageLocation.getForPath(strFormapMapUrl), str3, ImageLocation.getForPhoto(closestPhotoSizeWithSize, this.photoAttach), str2, -1L, str, this.parentObject, 1);
                }
            }
            if (!SharedConfig.isAutoplayGifs()) {
                r3 = i8;
                this.linkImageView.setAllowStartAnimation(r5);
                this.linkImageView.startAnimation();
            } else {
                r3 = i8;
                this.linkImageView.setAllowStartAnimation(r5);
                this.linkImageView.startAnimation();
            }
            this.drawLinkImageView = r5;
            r4 = r3;
        }
        if (this.mediaWebpage) {
            size = View.MeasureSpec.getSize(i2);
            if (size == 0) {
                size = AndroidUtilities.dp(f2);
            }
            setMeasuredDimension(size2, size);
            int iDp110 = (size2 - AndroidUtilities.dp(24.0f)) / i4;
            int iDp111 = (size - AndroidUtilities.dp(24.0f)) / i4;
            this.radialProgress.setProgressRect(iDp110, iDp111, AndroidUtilities.dp(24.0f) + iDp110, AndroidUtilities.dp(24.0f) + iDp111);
            this.radialProgress.setCircleRadius(AndroidUtilities.dp(12.0f));
            this.linkImageView.setImageCoords(0.0f, 0.0f, size2, size);
        } else {
            staticLayout = this.titleLayout;
            if (staticLayout != null) {
                lineBottom = r4;
            } else {
                lineBottom = r4;
            }
            staticLayout2 = this.descriptionLayout;
            lineBottom2 = lineBottom;
            if (staticLayout2 != null) {
                lineBottom2 = lineBottom;
                StaticLayout staticLayout110 = this.descriptionLayout;
                lineBottom2 = lineBottom + staticLayout110.getLineBottom(staticLayout110.getLineCount() - r5);
            }
            lineBottom2 = lineBottom;
            staticLayout3 = this.linkLayout;
            lineBottom3 = lineBottom2;
            if (staticLayout3 != null) {
                lineBottom3 = lineBottom2;
                StaticLayout staticLayout111 = this.linkLayout;
                lineBottom3 = lineBottom2 + staticLayout111.getLineBottom(staticLayout111.getLineCount() - r5);
            }
            lineBottom3 = lineBottom2;
            setMeasuredDimension(View.MeasureSpec.getSize(i), Math.max(AndroidUtilities.dp(68.0f), Math.max(AndroidUtilities.dp(52.0f), (int) lineBottom3) + AndroidUtilities.dp(16.0f)) + (this.needDivider ? 1 : 0));
            iDp3 = AndroidUtilities.dp(52.0f);
            if (LocaleController.isRTL) {
                iDp4 = (View.MeasureSpec.getSize(i) - AndroidUtilities.dp(f3)) - iDp3;
            } else {
                iDp4 = AndroidUtilities.dp(f3);
            }
            this.letterDrawable.setBounds(iDp4, AndroidUtilities.dp(f3), iDp4 + iDp3, AndroidUtilities.dp(60.0f));
            float f11 = iDp3;
            this.linkImageView.setImageCoords(iDp4, AndroidUtilities.dp(f3), f11, f11);
            i5 = this.documentAttachType;
            if (i5 != 3) {
                this.radialProgress.setCircleRadius(AndroidUtilities.dp(24.0f));
                this.radialProgress.setProgressRect(AndroidUtilities.dp(4.0f) + iDp4, AndroidUtilities.dp(12.0f), iDp4 + AndroidUtilities.dp(48.0f), AndroidUtilities.dp(56.0f));
            } else {
                this.radialProgress.setCircleRadius(AndroidUtilities.dp(24.0f));
                this.radialProgress.setProgressRect(AndroidUtilities.dp(4.0f) + iDp4, AndroidUtilities.dp(12.0f), iDp4 + AndroidUtilities.dp(48.0f), AndroidUtilities.dp(56.0f));
            }
        }
        checkBox2 = this.checkBox;
        if (checkBox2 != null) {
            measureChildWithMargins(checkBox2, i, 0, i2, 0);
        }
        closestPhotoSizeWithSize = null;
        str = null;
        botInlineResult2 = this.inlineResult;
        if (botInlineResult2 != null) {
            if (botInlineResult2.content instanceof TLRPC.TL_webDocument) {
                tL_webDocument = null;
            } else {
                tL_webDocument = null;
            }
            if (tL_webDocument == null) {
                webDocument = this.inlineResult.thumb;
                if (webDocument instanceof TLRPC.TL_webDocument) {
                    tL_webDocument = (TLRPC.TL_webDocument) webDocument;
                }
            }
            if (tL_webDocument != null) {
                strFormapMapUrl = null;
                webFileCreateWithWebDocument = null;
            } else {
                strFormapMapUrl = null;
                webFileCreateWithWebDocument = null;
            }
            if (tL_webDocument != null) {
                webFileCreateWithWebDocument = WebFile.createWithWebDocument(tL_webDocument);
            }
        } else {
            strFormapMapUrl = null;
            webFileCreateWithWebDocument = null;
        }
        if (this.documentAttach != null) {
            i6 = i8;
            while (true) {
                if (i6 < this.documentAttach.attributes.size()) {
                    documentAttribute = this.documentAttach.attributes.get(i6);
                    if (documentAttribute instanceof TLRPC.TL_documentAttributeImageSize) {
                    }
                    iDp = documentAttribute.w;
                    i3 = documentAttribute.h;
                } else {
                    iDp = i8;
                    i3 = iDp;
                }
                i6++;
            }
        } else {
            iDp = i8;
            i3 = iDp;
        }
        if (iDp != 0) {
            photoSize = this.currentPhotoObject;
            if (photoSize != null) {
                if (closestPhotoSizeWithSize != null) {
                    closestPhotoSizeWithSize.size = -1;
                }
                iDp = photoSize.w;
                i3 = photoSize.h;
            } else {
                botInlineResult3 = this.inlineResult;
                if (botInlineResult3 != null) {
                    int[] inlineResultWidthAndHeight16 = MessageObject.getInlineResultWidthAndHeight(botInlineResult3);
                    int i116 = inlineResultWidthAndHeight16[i8];
                    i3 = inlineResultWidthAndHeight16[r5];
                    iDp = i116;
                }
            }
        } else {
            photoSize = this.currentPhotoObject;
            if (photoSize != null) {
                if (closestPhotoSizeWithSize != null) {
                    closestPhotoSizeWithSize.size = -1;
                }
                iDp = photoSize.w;
                i3 = photoSize.h;
            } else {
                botInlineResult3 = this.inlineResult;
                if (botInlineResult3 != null) {
                    int[] inlineResultWidthAndHeight17 = MessageObject.getInlineResultWidthAndHeight(botInlineResult3);
                    int i117 = inlineResultWidthAndHeight17[i8];
                    i3 = inlineResultWidthAndHeight17[r5];
                    iDp = i117;
                }
            }
        }
        if (iDp != 0) {
            iDp = AndroidUtilities.dp(80.0f);
            i3 = iDp;
        } else {
            iDp = AndroidUtilities.dp(80.0f);
            i3 = iDp;
        }
        if (this.documentAttach != null) {
            if (this.mediaWebpage) {
                iDp2 = (int) (iDp / (i3 / AndroidUtilities.dp(80.0f)));
                if (this.documentAttachType == 2) {
                    Locale locale1111 = Locale.US;
                    Object[] objArr1111 = new Object[2];
                    objArr1111[i8] = Integer.valueOf((int) (iDp2 / AndroidUtilities.density));
                    objArr1111[r5] = 80;
                    str6 = String.format(locale1111, "%d_%d_b", objArr1111);
                    if (SharedConfig.isAutoplayGifs()) {
                    }
                    str3 = str6;
                    str2 = str3;
                } else {
                    Locale locale1112 = Locale.US;
                    Object[] objArr1112 = new Object[2];
                    objArr1112[i8] = Integer.valueOf((int) (iDp2 / AndroidUtilities.density));
                    objArr1112[r5] = 80;
                    str4 = String.format(locale1112, "%d_%d", objArr1112);
                    str5 = str4 + "_b";
                }
                str3 = str4;
                str2 = str5;
            } else {
                str2 = "52_52_b";
                str3 = "52_52";
            }
            ?? r112 = this.linkImageView;
            if (this.documentAttachType == 6) {
                r11 = r5;
            } else {
                r11 = i8;
            }
            r112.setAspectFit(r11);
            if (this.documentAttachType == 2) {
                document3 = this.documentAttach;
                if (document3 != null) {
                    documentVideoThumb = MessageObject.getDocumentVideoThumb(document3);
                    if (documentVideoThumb != null) {
                        ImageReceiver imageReceiver1111 = this.linkImageView;
                        ImageLocation forDocument17 = ImageLocation.getForDocument(documentVideoThumb, this.documentAttach);
                        StringBuilder sb1111 = new StringBuilder();
                        sb1111.append("100_100");
                        sb1111.append((!SharedConfig.isAutoplayGifs() || this.isKeyboard) ? _UrlKt.FRAGMENT_ENCODE_SET : "_firstframe");
                        imageReceiver1111.setImage(forDocument17, sb1111.toString(), ImageLocation.getForDocument(this.currentPhotoObject, this.documentAttach), str3, -1L, str, this.parentObject, 1);
                    } else {
                        forDocument = ImageLocation.getForDocument(this.documentAttach);
                        if (this.isForceGif) {
                            forDocument.imageType = 2;
                        }
                        ImageReceiver imageReceiver1112 = this.linkImageView;
                        StringBuilder sb1112 = new StringBuilder();
                        sb1112.append("100_100");
                        sb1112.append((!SharedConfig.isAutoplayGifs() || this.isKeyboard) ? _UrlKt.FRAGMENT_ENCODE_SET : "_firstframe");
                        imageReceiver1112.setImage(forDocument, sb1112.toString(), ImageLocation.getForDocument(this.currentPhotoObject, this.documentAttach), str3, this.documentAttach.size, str, this.parentObject, 0);
                    }
                } else if (webFileCreateWithWebDocument != null) {
                    this.linkImageView.setImage(ImageLocation.getForWebFile(webFileCreateWithWebDocument), "100_100", ImageLocation.getForPhoto(this.currentPhotoObject, this.photoAttach), str3, -1L, str, this.parentObject, 1);
                } else {
                    this.linkImageView.setImage(ImageLocation.getForPath(strFormapMapUrl), "100_100", ImageLocation.getForPhoto(this.currentPhotoObject, this.photoAttach), str3, -1L, str, this.parentObject, 1);
                }
                i4 = 2;
            } else if (this.currentPhotoObject != null) {
                svgThumb = DocumentObject.getSvgThumb(this.documentAttach, Theme.key_windowBackgroundGray, f);
                if (!MessageObject.canAutoplayAnimatedSticker(this.documentAttach)) {
                    i4 = 2;
                    document2 = this.documentAttach;
                    if (document2 == null) {
                        this.linkImageView.setImage(ImageLocation.getForPhoto(this.currentPhotoObject, this.photoAttach), str3, ImageLocation.getForPhoto(closestPhotoSizeWithSize, this.photoAttach), str2, this.currentPhotoObject.size, str, this.parentObject, 0);
                    } else if (svgThumb != null) {
                        this.linkImageView.setImage(ImageLocation.getForDocument(this.currentPhotoObject, document2), str3, svgThumb, this.currentPhotoObject.size, str, this.parentObject, 0);
                    } else {
                        this.linkImageView.setImage(ImageLocation.getForDocument(this.currentPhotoObject, document2), str3, ImageLocation.getForPhoto(closestPhotoSizeWithSize, this.photoAttach), str2, this.currentPhotoObject.size, str, this.parentObject, 0);
                    }
                } else if (svgThumb != null) {
                    i4 = 2;
                    this.linkImageView.setImage(ImageLocation.getForDocument(this.documentAttach), "80_80", svgThumb, this.currentPhotoObject.size, str, this.parentObject, 0);
                } else {
                    i4 = 2;
                    this.linkImageView.setImage(ImageLocation.getForDocument(this.documentAttach), "80_80", ImageLocation.getForDocument(this.currentPhotoObject, this.documentAttach), str2, this.currentPhotoObject.size, str, this.parentObject, 0);
                }
            } else {
                i4 = 2;
                if (webFileCreateWithWebDocument != null) {
                    this.linkImageView.setImage(ImageLocation.getForWebFile(webFileCreateWithWebDocument), str3, ImageLocation.getForPhoto(closestPhotoSizeWithSize, this.photoAttach), str2, -1L, str, this.parentObject, 1);
                } else {
                    this.linkImageView.setImage(ImageLocation.getForPath(strFormapMapUrl), str3, ImageLocation.getForPhoto(closestPhotoSizeWithSize, this.photoAttach), str2, -1L, str, this.parentObject, 1);
                }
            }
            if (!SharedConfig.isAutoplayGifs()) {
                r3 = i8;
                this.linkImageView.setAllowStartAnimation(r5);
                this.linkImageView.startAnimation();
            } else {
                r3 = i8;
                this.linkImageView.setAllowStartAnimation(r5);
                this.linkImageView.startAnimation();
            }
            this.drawLinkImageView = r5;
            r4 = r3;
        } else {
            if (this.mediaWebpage) {
                iDp2 = (int) (iDp / (i3 / AndroidUtilities.dp(80.0f)));
                if (this.documentAttachType == 2) {
                    Locale locale1113 = Locale.US;
                    Object[] objArr1113 = new Object[2];
                    objArr1113[i8] = Integer.valueOf((int) (iDp2 / AndroidUtilities.density));
                    objArr1113[r5] = 80;
                    str6 = String.format(locale1113, "%d_%d_b", objArr1113);
                    if (SharedConfig.isAutoplayGifs()) {
                    }
                    str3 = str6;
                    str2 = str3;
                } else {
                    Locale locale1114 = Locale.US;
                    Object[] objArr1114 = new Object[2];
                    objArr1114[i8] = Integer.valueOf((int) (iDp2 / AndroidUtilities.density));
                    objArr1114[r5] = 80;
                    str4 = String.format(locale1114, "%d_%d", objArr1114);
                    str5 = str4 + "_b";
                }
                str3 = str4;
                str2 = str5;
            } else {
                str2 = "52_52_b";
                str3 = "52_52";
            }
            ?? r113 = this.linkImageView;
            if (this.documentAttachType == 6) {
                r11 = r5;
            } else {
                r11 = i8;
            }
            r113.setAspectFit(r11);
            if (this.documentAttachType == 2) {
                document3 = this.documentAttach;
                if (document3 != null) {
                    documentVideoThumb = MessageObject.getDocumentVideoThumb(document3);
                    if (documentVideoThumb != null) {
                        ImageReceiver imageReceiver1113 = this.linkImageView;
                        ImageLocation forDocument18 = ImageLocation.getForDocument(documentVideoThumb, this.documentAttach);
                        StringBuilder sb1113 = new StringBuilder();
                        sb1113.append("100_100");
                        sb1113.append((!SharedConfig.isAutoplayGifs() || this.isKeyboard) ? _UrlKt.FRAGMENT_ENCODE_SET : "_firstframe");
                        imageReceiver1113.setImage(forDocument18, sb1113.toString(), ImageLocation.getForDocument(this.currentPhotoObject, this.documentAttach), str3, -1L, str, this.parentObject, 1);
                    } else {
                        forDocument = ImageLocation.getForDocument(this.documentAttach);
                        if (this.isForceGif) {
                            forDocument.imageType = 2;
                        }
                        ImageReceiver imageReceiver1114 = this.linkImageView;
                        StringBuilder sb1114 = new StringBuilder();
                        sb1114.append("100_100");
                        sb1114.append((!SharedConfig.isAutoplayGifs() || this.isKeyboard) ? _UrlKt.FRAGMENT_ENCODE_SET : "_firstframe");
                        imageReceiver1114.setImage(forDocument, sb1114.toString(), ImageLocation.getForDocument(this.currentPhotoObject, this.documentAttach), str3, this.documentAttach.size, str, this.parentObject, 0);
                    }
                } else if (webFileCreateWithWebDocument != null) {
                    this.linkImageView.setImage(ImageLocation.getForWebFile(webFileCreateWithWebDocument), "100_100", ImageLocation.getForPhoto(this.currentPhotoObject, this.photoAttach), str3, -1L, str, this.parentObject, 1);
                } else {
                    this.linkImageView.setImage(ImageLocation.getForPath(strFormapMapUrl), "100_100", ImageLocation.getForPhoto(this.currentPhotoObject, this.photoAttach), str3, -1L, str, this.parentObject, 1);
                }
                i4 = 2;
            } else if (this.currentPhotoObject != null) {
                svgThumb = DocumentObject.getSvgThumb(this.documentAttach, Theme.key_windowBackgroundGray, f);
                if (!MessageObject.canAutoplayAnimatedSticker(this.documentAttach)) {
                    i4 = 2;
                    document2 = this.documentAttach;
                    if (document2 == null) {
                        this.linkImageView.setImage(ImageLocation.getForPhoto(this.currentPhotoObject, this.photoAttach), str3, ImageLocation.getForPhoto(closestPhotoSizeWithSize, this.photoAttach), str2, this.currentPhotoObject.size, str, this.parentObject, 0);
                    } else if (svgThumb != null) {
                        this.linkImageView.setImage(ImageLocation.getForDocument(this.currentPhotoObject, document2), str3, svgThumb, this.currentPhotoObject.size, str, this.parentObject, 0);
                    } else {
                        this.linkImageView.setImage(ImageLocation.getForDocument(this.currentPhotoObject, document2), str3, ImageLocation.getForPhoto(closestPhotoSizeWithSize, this.photoAttach), str2, this.currentPhotoObject.size, str, this.parentObject, 0);
                    }
                } else if (svgThumb != null) {
                    i4 = 2;
                    this.linkImageView.setImage(ImageLocation.getForDocument(this.documentAttach), "80_80", svgThumb, this.currentPhotoObject.size, str, this.parentObject, 0);
                } else {
                    i4 = 2;
                    this.linkImageView.setImage(ImageLocation.getForDocument(this.documentAttach), "80_80", ImageLocation.getForDocument(this.currentPhotoObject, this.documentAttach), str2, this.currentPhotoObject.size, str, this.parentObject, 0);
                }
            } else {
                i4 = 2;
                if (webFileCreateWithWebDocument != null) {
                    this.linkImageView.setImage(ImageLocation.getForWebFile(webFileCreateWithWebDocument), str3, ImageLocation.getForPhoto(closestPhotoSizeWithSize, this.photoAttach), str2, -1L, str, this.parentObject, 1);
                } else {
                    this.linkImageView.setImage(ImageLocation.getForPath(strFormapMapUrl), str3, ImageLocation.getForPhoto(closestPhotoSizeWithSize, this.photoAttach), str2, -1L, str, this.parentObject, 1);
                }
            }
            if (!SharedConfig.isAutoplayGifs()) {
                r3 = i8;
                this.linkImageView.setAllowStartAnimation(r5);
                this.linkImageView.startAnimation();
            } else {
                r3 = i8;
                this.linkImageView.setAllowStartAnimation(r5);
                this.linkImageView.startAnimation();
            }
            this.drawLinkImageView = r5;
            r4 = r3;
        }
        if (this.mediaWebpage) {
            size = View.MeasureSpec.getSize(i2);
            if (size == 0) {
                size = AndroidUtilities.dp(f2);
            }
            setMeasuredDimension(size2, size);
            int iDp112 = (size2 - AndroidUtilities.dp(24.0f)) / i4;
            int iDp113 = (size - AndroidUtilities.dp(24.0f)) / i4;
            this.radialProgress.setProgressRect(iDp112, iDp113, AndroidUtilities.dp(24.0f) + iDp112, AndroidUtilities.dp(24.0f) + iDp113);
            this.radialProgress.setCircleRadius(AndroidUtilities.dp(12.0f));
            this.linkImageView.setImageCoords(0.0f, 0.0f, size2, size);
        } else {
            staticLayout = this.titleLayout;
            if (staticLayout != null) {
                lineBottom = r4;
            } else {
                lineBottom = r4;
            }
            staticLayout2 = this.descriptionLayout;
            lineBottom2 = lineBottom;
            if (staticLayout2 != null) {
                lineBottom2 = lineBottom;
                StaticLayout staticLayout112 = this.descriptionLayout;
                lineBottom2 = lineBottom + staticLayout112.getLineBottom(staticLayout112.getLineCount() - r5);
            }
            lineBottom2 = lineBottom;
            staticLayout3 = this.linkLayout;
            lineBottom3 = lineBottom2;
            if (staticLayout3 != null) {
                lineBottom3 = lineBottom2;
                StaticLayout staticLayout113 = this.linkLayout;
                lineBottom3 = lineBottom2 + staticLayout113.getLineBottom(staticLayout113.getLineCount() - r5);
            }
            lineBottom3 = lineBottom2;
            setMeasuredDimension(View.MeasureSpec.getSize(i), Math.max(AndroidUtilities.dp(68.0f), Math.max(AndroidUtilities.dp(52.0f), (int) lineBottom3) + AndroidUtilities.dp(16.0f)) + (this.needDivider ? 1 : 0));
            iDp3 = AndroidUtilities.dp(52.0f);
            if (LocaleController.isRTL) {
                iDp4 = (View.MeasureSpec.getSize(i) - AndroidUtilities.dp(f3)) - iDp3;
            } else {
                iDp4 = AndroidUtilities.dp(f3);
            }
            this.letterDrawable.setBounds(iDp4, AndroidUtilities.dp(f3), iDp4 + iDp3, AndroidUtilities.dp(60.0f));
            float f12 = iDp3;
            this.linkImageView.setImageCoords(iDp4, AndroidUtilities.dp(f3), f12, f12);
            i5 = this.documentAttachType;
            if (i5 != 3) {
                this.radialProgress.setCircleRadius(AndroidUtilities.dp(24.0f));
                this.radialProgress.setProgressRect(AndroidUtilities.dp(4.0f) + iDp4, AndroidUtilities.dp(12.0f), iDp4 + AndroidUtilities.dp(48.0f), AndroidUtilities.dp(56.0f));
            } else {
                this.radialProgress.setCircleRadius(AndroidUtilities.dp(24.0f));
                this.radialProgress.setProgressRect(AndroidUtilities.dp(4.0f) + iDp4, AndroidUtilities.dp(12.0f), iDp4 + AndroidUtilities.dp(48.0f), AndroidUtilities.dp(56.0f));
            }
        }
        checkBox2 = this.checkBox;
        if (checkBox2 != null) {
            measureChildWithMargins(checkBox2, i, 0, i2, 0);
        }
    }

    private void setAttachType() {
        this.currentMessageObject = null;
        this.documentAttachType = 0;
        TLRPC.Document document = this.documentAttach;
        if (document != null) {
            if (MessageObject.isGifDocument(document)) {
                this.documentAttachType = 2;
            } else if (MessageObject.isStickerDocument(this.documentAttach) || MessageObject.isAnimatedStickerDocument(this.documentAttach, true)) {
                this.documentAttachType = 6;
            } else if (MessageObject.isMusicDocument(this.documentAttach)) {
                this.documentAttachType = 5;
            } else if (MessageObject.isVoiceDocument(this.documentAttach)) {
                this.documentAttachType = 3;
            }
        } else {
            TLRPC.BotInlineResult botInlineResult = this.inlineResult;
            if (botInlineResult != null) {
                if (botInlineResult.photo != null) {
                    this.documentAttachType = 7;
                } else if (botInlineResult.type.equals(MediaStreamTrack.AUDIO_TRACK_KIND)) {
                    this.documentAttachType = 5;
                } else if (this.inlineResult.type.equals("voice")) {
                    this.documentAttachType = 3;
                }
            }
        }
        int i = this.documentAttachType;
        if (i == 3 || i == 5) {
            TLRPC.TL_message tL_message = new TLRPC.TL_message();
            tL_message.out = true;
            tL_message.id = -Utilities.random.nextInt();
            tL_message.peer_id = new TLRPC.TL_peerUser();
            TLRPC.TL_peerUser tL_peerUser = new TLRPC.TL_peerUser();
            tL_message.from_id = tL_peerUser;
            TLRPC.Peer peer = tL_message.peer_id;
            long clientUserId = UserConfig.getInstance(this.currentAccount).getClientUserId();
            tL_peerUser.user_id = clientUserId;
            peer.user_id = clientUserId;
            tL_message.date = (int) (System.currentTimeMillis() / 1000);
            String str = _UrlKt.FRAGMENT_ENCODE_SET;
            tL_message.message = _UrlKt.FRAGMENT_ENCODE_SET;
            TLRPC.TL_messageMediaDocument tL_messageMediaDocument = new TLRPC.TL_messageMediaDocument();
            tL_message.media = tL_messageMediaDocument;
            tL_messageMediaDocument.flags |= 3;
            tL_messageMediaDocument.document = new TLRPC.TL_document();
            TLRPC.MessageMedia messageMedia = tL_message.media;
            messageMedia.document.file_reference = new byte[0];
            tL_message.flags |= 768;
            TLRPC.Document document2 = this.documentAttach;
            if (document2 != null) {
                messageMedia.document = document2;
                tL_message.attachPath = _UrlKt.FRAGMENT_ENCODE_SET;
            } else {
                String httpUrlExtension = ImageLoader.getHttpUrlExtension(this.inlineResult.content.url, this.documentAttachType == 5 ? "mp3" : "ogg");
                TLRPC.Document document3 = tL_message.media.document;
                document3.id = 0L;
                document3.access_hash = 0L;
                document3.date = tL_message.date;
                document3.mime_type = "audio/" + httpUrlExtension;
                TLRPC.Document document4 = tL_message.media.document;
                document4.size = 0L;
                document4.dc_id = 0;
                TLRPC.TL_documentAttributeAudio tL_documentAttributeAudio = new TLRPC.TL_documentAttributeAudio();
                tL_documentAttributeAudio.duration = MessageObject.getInlineResultDuration(this.inlineResult);
                TLRPC.BotInlineResult botInlineResult2 = this.inlineResult;
                String str2 = botInlineResult2.title;
                if (str2 == null) {
                    str2 = _UrlKt.FRAGMENT_ENCODE_SET;
                }
                tL_documentAttributeAudio.title = str2;
                String str3 = botInlineResult2.description;
                if (str3 != null) {
                    str = str3;
                }
                tL_documentAttributeAudio.performer = str;
                tL_documentAttributeAudio.flags |= 3;
                if (this.documentAttachType == 3) {
                    tL_documentAttributeAudio.voice = true;
                }
                tL_message.media.document.attributes.add(tL_documentAttributeAudio);
                TLRPC.TL_documentAttributeFilename tL_documentAttributeFilename = new TLRPC.TL_documentAttributeFilename();
                StringBuilder sb = new StringBuilder();
                sb.append(Utilities.MD5(this.inlineResult.content.url));
                sb.append(".");
                sb.append(ImageLoader.getHttpUrlExtension(this.inlineResult.content.url, this.documentAttachType == 5 ? "mp3" : "ogg"));
                tL_documentAttributeFilename.file_name = sb.toString();
                tL_message.media.document.attributes.add(tL_documentAttributeFilename);
                File directory = FileLoader.getDirectory(4);
                StringBuilder sb2 = new StringBuilder();
                sb2.append(Utilities.MD5(this.inlineResult.content.url));
                sb2.append(".");
                sb2.append(ImageLoader.getHttpUrlExtension(this.inlineResult.content.url, this.documentAttachType == 5 ? "mp3" : "ogg"));
                tL_message.attachPath = new File(directory, sb2.toString()).getAbsolutePath();
            }
            this.currentMessageObject = new MessageObject(this.currentAccount, tL_message, false, true);
        }
    }

    public void setLink(TLRPC.BotInlineResult botInlineResult, TLRPC.User user, boolean z, boolean z2, boolean z3, boolean z4) {
        this.needDivider = z2;
        this.needShadow = z3;
        this.inlineBot = user;
        this.inlineResult = botInlineResult;
        this.parentObject = botInlineResult;
        if (botInlineResult != null) {
            this.documentAttach = botInlineResult.document;
            this.photoAttach = botInlineResult.photo;
        } else {
            this.documentAttach = null;
            this.photoAttach = null;
        }
        this.mediaWebpage = z;
        this.isForceGif = z4;
        setAttachType();
        if (z4) {
            this.documentAttachType = 2;
        }
        requestLayout();
        this.fileName = null;
        this.cacheFile = null;
        this.fileExist = false;
        this.resolvingFileName = false;
        updateButtonState(false, false);
    }

    public TLRPC.User getInlineBot() {
        return this.inlineBot;
    }

    public Object getParentObject() {
        return this.parentObject;
    }

    public void setGif(TLRPC.Document document, boolean z) {
        setGif(document, "gif" + document, 0, z);
    }

    public void setGif(TLRPC.Document document, Object obj, int i, boolean z) {
        this.needDivider = z;
        this.needShadow = false;
        this.currentDate = i;
        this.inlineResult = null;
        this.parentObject = obj;
        this.documentAttach = document;
        this.photoAttach = null;
        this.mediaWebpage = true;
        this.isForceGif = true;
        setAttachType();
        this.documentAttachType = 2;
        requestLayout();
        this.fileName = null;
        this.cacheFile = null;
        this.fileExist = false;
        this.resolvingFileName = false;
        updateButtonState(false, false);
    }

    public boolean isSticker() {
        return this.documentAttachType == 6;
    }

    public boolean isGif() {
        return this.documentAttachType == 2 && this.canPreviewGif;
    }

    public boolean showingBitmap() {
        return this.linkImageView.getBitmap() != null;
    }

    public int getDate() {
        return this.currentDate;
    }

    public TLRPC.Document getDocument() {
        return this.documentAttach;
    }

    public TLRPC.BotInlineResult getBotInlineResult() {
        return this.inlineResult;
    }

    public ImageReceiver getPhotoImage() {
        return this.linkImageView;
    }

    public void setScaled(boolean z) {
        this.scaled = z;
        ButtonBounce buttonBounce = this.buttonBounce;
        if (buttonBounce != null) {
            buttonBounce.setPressed(isPressed() || this.scaled);
        }
    }

    public void setCanPreviewGif(boolean z) {
        this.canPreviewGif = z;
    }

    public void setIsKeyboard(boolean z) {
        this.isKeyboard = z;
    }

    @Override // android.view.ViewGroup, android.view.View
    protected void onDetachedFromWindow() {
        super.onDetachedFromWindow();
        this.linkImageView.onDetachedFromWindow();
        this.radialProgress.onDetachedFromWindow();
        DownloadController.getInstance(this.currentAccount).removeLoadingFileObserver(this);
    }

    @Override // android.view.ViewGroup, android.view.View
    protected void onAttachedToWindow() {
        super.onAttachedToWindow();
        if (this.linkImageView.onAttachedToWindow()) {
            updateButtonState(false, false);
        }
        this.radialProgress.onAttachedToWindow();
    }

    public MessageObject getMessageObject() {
        return this.currentMessageObject;
    }

    @Override // android.view.View
    public boolean onTouchEvent(MotionEvent motionEvent) {
        TLRPC.WebDocument webDocument;
        if (this.mediaWebpage || this.delegate == null || this.inlineResult == null) {
            return super.onTouchEvent(motionEvent);
        }
        int x = (int) motionEvent.getX();
        int y = (int) motionEvent.getY();
        AndroidUtilities.dp(48.0f);
        int i = this.documentAttachType;
        boolean z = true;
        if (i == 3 || i == 5) {
            boolean zContains = this.letterDrawable.getBounds().contains(x, y);
            if (motionEvent.getAction() == 0) {
                if (zContains) {
                    this.buttonPressed = true;
                    this.radialProgress.setPressed(true, false);
                    invalidate();
                }
            } else if (this.buttonPressed) {
                if (motionEvent.getAction() == 1) {
                    this.buttonPressed = false;
                    playSoundEffect(0);
                    didPressedButton();
                    invalidate();
                } else if (motionEvent.getAction() == 3) {
                    this.buttonPressed = false;
                    invalidate();
                } else if (motionEvent.getAction() == 2 && !zContains) {
                    this.buttonPressed = false;
                    invalidate();
                }
                this.radialProgress.setPressed(this.buttonPressed, false);
            }
            z = false;
        } else {
            TLRPC.BotInlineResult botInlineResult = this.inlineResult;
            if (botInlineResult == null || (webDocument = botInlineResult.content) == null || TextUtils.isEmpty(webDocument.url)) {
                z = false;
            } else {
                if (motionEvent.getAction() == 0) {
                    if (this.letterDrawable.getBounds().contains(x, y)) {
                        this.buttonPressed = true;
                    }
                } else if (this.buttonPressed) {
                    if (motionEvent.getAction() == 1) {
                        this.buttonPressed = false;
                        playSoundEffect(0);
                        this.delegate.didPressedImage(this);
                    } else if (motionEvent.getAction() == 3) {
                        this.buttonPressed = false;
                    } else if (motionEvent.getAction() == 2 && !this.letterDrawable.getBounds().contains(x, y)) {
                        this.buttonPressed = false;
                    }
                }
                z = false;
            }
        }
        return !z ? super.onTouchEvent(motionEvent) : z;
    }

    private void didPressedButton() {
        int i = this.documentAttachType;
        if (i == 3 || i == 5) {
            int i2 = this.buttonState;
            if (i2 == 0) {
                if (MediaController.getInstance().playMessage(this.currentMessageObject)) {
                    this.buttonState = 1;
                    this.radialProgress.setIcon(getIconForCurrentState(), false, true);
                    invalidate();
                    return;
                }
                return;
            }
            if (i2 == 1) {
                if (MediaController.getInstance().lambda$startAudioAgain$7(this.currentMessageObject)) {
                    this.buttonState = 0;
                    this.radialProgress.setIcon(getIconForCurrentState(), false, true);
                    invalidate();
                    return;
                }
                return;
            }
            if (i2 == 2) {
                this.radialProgress.setProgress(0.0f, false);
                if (this.documentAttach != null) {
                    FileLoader.getInstance(this.currentAccount).loadFile(this.documentAttach, this.inlineResult, 1, 0);
                } else if (this.inlineResult.content instanceof TLRPC.TL_webDocument) {
                    FileLoader.getInstance(this.currentAccount).loadFile(WebFile.createWithWebDocument(this.inlineResult.content), 3, 1);
                }
                this.buttonState = 4;
                this.radialProgress.setIcon(getIconForCurrentState(), false, true);
                invalidate();
                return;
            }
            if (i2 == 4) {
                if (this.documentAttach != null) {
                    FileLoader.getInstance(this.currentAccount).cancelLoadFile(this.documentAttach);
                } else if (this.inlineResult.content instanceof TLRPC.TL_webDocument) {
                    FileLoader.getInstance(this.currentAccount).cancelLoadFile(WebFile.createWithWebDocument(this.inlineResult.content));
                }
                this.buttonState = 2;
                this.radialProgress.setIcon(getIconForCurrentState(), false, true);
                invalidate();
            }
        }
    }

    @Override // android.view.View
    protected void onDraw(Canvas canvas) {
        Canvas canvas2;
        int i;
        CheckBox2 checkBox2 = this.checkBox;
        if (checkBox2 == null || (!checkBox2.isChecked() && this.linkImageView.hasBitmapImage() && this.linkImageView.getCurrentAlpha() == 1.0f && !PhotoViewer.isShowingImage((MessageObject) this.parentObject))) {
            canvas2 = canvas;
        } else {
            canvas.drawRect(0.0f, 0.0f, getMeasuredWidth(), getMeasuredHeight(), this.backgroundPaint);
            canvas2 = canvas;
        }
        if (this.titleLayout != null) {
            canvas2.save();
            canvas2.translate(AndroidUtilities.dp(LocaleController.isRTL ? 8.0f : AndroidUtilities.leftBaseline), this.titleY);
            this.titleLayout.draw(canvas2);
            canvas2.restore();
        }
        if (this.descriptionLayout != null) {
            Theme.chat_contextResult_descriptionTextPaint.setColor(Theme.getColor(Theme.key_windowBackgroundWhiteGrayText2, this.resourcesProvider));
            canvas2.save();
            canvas2.translate(AndroidUtilities.dp(LocaleController.isRTL ? 8.0f : AndroidUtilities.leftBaseline), this.descriptionY);
            this.descriptionLayout.draw(canvas2);
            canvas2.restore();
        }
        if (this.linkLayout != null) {
            Theme.chat_contextResult_descriptionTextPaint.setColor(Theme.getColor(Theme.key_windowBackgroundWhiteLinkText, this.resourcesProvider));
            canvas2.save();
            canvas2.translate(AndroidUtilities.dp(LocaleController.isRTL ? 8.0f : AndroidUtilities.leftBaseline), this.linkY);
            this.linkLayout.draw(canvas2);
            canvas2.restore();
        }
        if (!this.mediaWebpage) {
            if (this.drawLinkImageView && !PhotoViewer.isShowingImage(this.inlineResult)) {
                this.letterDrawable.setAlpha((int) ((1.0f - this.linkImageView.getCurrentAlpha()) * 255.0f));
            } else {
                this.letterDrawable.setAlpha(255);
            }
            int i2 = this.documentAttachType;
            if (i2 == 3 || i2 == 5) {
                this.radialProgress.setProgressColor(Theme.getColor(this.buttonPressed ? Theme.key_chat_inAudioSelectedProgress : Theme.key_chat_inAudioProgress, this.resourcesProvider));
                this.radialProgress.draw(canvas2);
            } else {
                TLRPC.BotInlineResult botInlineResult = this.inlineResult;
                if (botInlineResult != null && botInlineResult.type.equals("file")) {
                    int intrinsicWidth = Theme.chat_inlineResultFile.getIntrinsicWidth();
                    int intrinsicHeight = Theme.chat_inlineResultFile.getIntrinsicHeight();
                    int imageX = (int) (this.linkImageView.getImageX() + ((AndroidUtilities.dp(52.0f) - intrinsicWidth) / 2));
                    int imageY = (int) (this.linkImageView.getImageY() + ((AndroidUtilities.dp(52.0f) - intrinsicHeight) / 2));
                    canvas2.drawRect(this.linkImageView.getImageX(), this.linkImageView.getImageY(), this.linkImageView.getImageX() + AndroidUtilities.dp(52.0f), this.linkImageView.getImageY() + AndroidUtilities.dp(52.0f), LetterDrawable.paint);
                    Theme.chat_inlineResultFile.setBounds(imageX, imageY, intrinsicWidth + imageX, intrinsicHeight + imageY);
                    Theme.chat_inlineResultFile.draw(canvas2);
                } else {
                    TLRPC.BotInlineResult botInlineResult2 = this.inlineResult;
                    if (botInlineResult2 != null && (botInlineResult2.type.equals(MediaStreamTrack.AUDIO_TRACK_KIND) || this.inlineResult.type.equals("voice"))) {
                        int intrinsicWidth2 = Theme.chat_inlineResultAudio.getIntrinsicWidth();
                        int intrinsicHeight2 = Theme.chat_inlineResultAudio.getIntrinsicHeight();
                        int imageX2 = (int) (this.linkImageView.getImageX() + ((AndroidUtilities.dp(52.0f) - intrinsicWidth2) / 2));
                        int imageY2 = (int) (this.linkImageView.getImageY() + ((AndroidUtilities.dp(52.0f) - intrinsicHeight2) / 2));
                        canvas2.drawRect(this.linkImageView.getImageX(), this.linkImageView.getImageY(), this.linkImageView.getImageX() + AndroidUtilities.dp(52.0f), this.linkImageView.getImageY() + AndroidUtilities.dp(52.0f), LetterDrawable.paint);
                        Theme.chat_inlineResultAudio.setBounds(imageX2, imageY2, intrinsicWidth2 + imageX2, intrinsicHeight2 + imageY2);
                        Theme.chat_inlineResultAudio.draw(canvas2);
                    } else {
                        TLRPC.BotInlineResult botInlineResult3 = this.inlineResult;
                        if (botInlineResult3 != null && (botInlineResult3.type.equals("venue") || this.inlineResult.type.equals("geo"))) {
                            int intrinsicWidth3 = Theme.chat_inlineResultLocation.getIntrinsicWidth();
                            int intrinsicHeight3 = Theme.chat_inlineResultLocation.getIntrinsicHeight();
                            int imageX3 = (int) (this.linkImageView.getImageX() + ((AndroidUtilities.dp(52.0f) - intrinsicWidth3) / 2));
                            int imageY3 = (int) (this.linkImageView.getImageY() + ((AndroidUtilities.dp(52.0f) - intrinsicHeight3) / 2));
                            canvas2.drawRect(this.linkImageView.getImageX(), this.linkImageView.getImageY(), this.linkImageView.getImageX() + AndroidUtilities.dp(52.0f), this.linkImageView.getImageY() + AndroidUtilities.dp(52.0f), LetterDrawable.paint);
                            Theme.chat_inlineResultLocation.setBounds(imageX3, imageY3, intrinsicWidth3 + imageX3, intrinsicHeight3 + imageY3);
                            Theme.chat_inlineResultLocation.draw(canvas2);
                        } else {
                            this.letterDrawable.draw(canvas2);
                        }
                    }
                }
            }
        } else {
            TLRPC.BotInlineResult botInlineResult4 = this.inlineResult;
            if (botInlineResult4 != null) {
                TLRPC.BotInlineMessage botInlineMessage = botInlineResult4.send_message;
                if ((botInlineMessage instanceof TLRPC.TL_botInlineMessageMediaGeo) || (botInlineMessage instanceof TLRPC.TL_botInlineMessageMediaVenue)) {
                    int intrinsicWidth4 = Theme.chat_inlineResultLocation.getIntrinsicWidth();
                    int intrinsicHeight4 = Theme.chat_inlineResultLocation.getIntrinsicHeight();
                    int imageX4 = (int) (this.linkImageView.getImageX() + ((this.linkImageView.getImageWidth() - intrinsicWidth4) / 2.0f));
                    int imageY4 = (int) (this.linkImageView.getImageY() + ((this.linkImageView.getImageHeight() - intrinsicHeight4) / 2.0f));
                    canvas2.drawRect(this.linkImageView.getImageX(), this.linkImageView.getImageY(), this.linkImageView.getImageX() + this.linkImageView.getImageWidth(), this.linkImageView.getImageY() + this.linkImageView.getImageHeight(), LetterDrawable.paint);
                    Theme.chat_inlineResultLocation.setBounds(imageX4, imageY4, intrinsicWidth4 + imageX4, intrinsicHeight4 + imageY4);
                    Theme.chat_inlineResultLocation.draw(canvas2);
                }
            }
        }
        if (this.drawLinkImageView) {
            TLRPC.BotInlineResult botInlineResult5 = this.inlineResult;
            if (botInlineResult5 != null) {
                this.linkImageView.setVisible(!PhotoViewer.isShowingImage(botInlineResult5), false);
            }
            canvas2.save();
            float scale = this.imageScale;
            ButtonBounce buttonBounce = this.buttonBounce;
            if (buttonBounce != null) {
                scale *= buttonBounce.getScale(0.1f);
            }
            canvas2.scale(scale, scale, getMeasuredWidth() / 2, getMeasuredHeight() / 2);
            this.linkImageView.draw(canvas2);
            canvas2.restore();
        }
        if (this.mediaWebpage && ((i = this.documentAttachType) == 7 || i == 2)) {
            this.radialProgress.draw(canvas2);
        }
        if (this.needDivider && !this.mediaWebpage) {
            if (LocaleController.isRTL) {
                canvas2.drawLine(0.0f, getMeasuredHeight() - 1, getMeasuredWidth() - AndroidUtilities.dp(AndroidUtilities.leftBaseline), getMeasuredHeight() - 1, Theme.dividerPaint);
            } else {
                canvas2.drawLine(AndroidUtilities.dp(AndroidUtilities.leftBaseline), getMeasuredHeight() - 1, getMeasuredWidth(), getMeasuredHeight() - 1, Theme.dividerPaint);
            }
        }
        if (this.needShadow) {
            Theme.chat_contextResult_shadowUnderSwitchDrawable.setBounds(0, 0, getMeasuredWidth(), AndroidUtilities.dp(3.0f));
            Theme.chat_contextResult_shadowUnderSwitchDrawable.draw(canvas2);
        }
    }

    private int getIconForCurrentState() {
        int i = this.documentAttachType;
        if (i == 3 || i == 5) {
            this.radialProgress.setColorKeys(Theme.key_chat_inLoader, Theme.key_chat_inLoaderSelected, Theme.key_chat_inMediaIcon, Theme.key_chat_inMediaIconSelected);
            int i2 = this.buttonState;
            if (i2 == 1) {
                return 1;
            }
            if (i2 == 2) {
                return 2;
            }
            return i2 == 4 ? 3 : 0;
        }
        this.radialProgress.setColorKeys(Theme.key_chat_mediaLoaderPhoto, Theme.key_chat_mediaLoaderPhotoSelected, Theme.key_chat_mediaLoaderPhotoIcon, Theme.key_chat_mediaLoaderPhotoIconSelected);
        return this.buttonState == 1 ? 10 : 4;
    }

    public void updateButtonState(boolean z, boolean z2) {
        boolean zIsLoadingHttpFile;
        String str = this.fileName;
        if (str == null && !this.resolvingFileName) {
            this.resolvingFileName = true;
            int i = this.resolveFileNameId;
            this.resolveFileNameId = i;
            Utilities.searchQueue.postRunnable(new AnonymousClass1(i, z));
            this.radialProgress.setIcon(4, z, false);
            return;
        }
        if (TextUtils.isEmpty(str)) {
            this.buttonState = -1;
            this.radialProgress.setIcon(4, z, false);
            return;
        }
        if (this.documentAttach != null) {
            zIsLoadingHttpFile = FileLoader.getInstance(this.currentAccount).isLoadingFile(this.fileName);
        } else {
            zIsLoadingHttpFile = ImageLoader.getInstance().isLoadingHttpFile(this.fileName);
        }
        if (zIsLoadingHttpFile || !this.fileExist) {
            DownloadController.getInstance(this.currentAccount).addLoadingFileObserver(this.fileName, this);
            int i2 = this.documentAttachType;
            if (i2 != 5 && i2 != 3) {
                this.buttonState = 1;
                Float fileProgress = ImageLoader.getInstance().getFileProgress(this.fileName);
                this.radialProgress.setProgress(fileProgress != null ? fileProgress.floatValue() : 0.0f, false);
            } else if (!zIsLoadingHttpFile) {
                this.buttonState = 2;
            } else {
                this.buttonState = 4;
                Float fileProgress2 = ImageLoader.getInstance().getFileProgress(this.fileName);
                if (fileProgress2 != null) {
                    this.radialProgress.setProgress(fileProgress2.floatValue(), z2);
                } else {
                    this.radialProgress.setProgress(0.0f, z2);
                }
            }
        } else {
            DownloadController.getInstance(this.currentAccount).removeLoadingFileObserver(this);
            int i3 = this.documentAttachType;
            if (i3 == 5 || i3 == 3) {
                boolean zIsPlayingMessage = MediaController.getInstance().isPlayingMessage(this.currentMessageObject);
                if (!zIsPlayingMessage || (zIsPlayingMessage && MediaController.getInstance().isMessagePaused())) {
                    this.buttonState = 0;
                } else {
                    this.buttonState = 1;
                }
                this.radialProgress.setProgress(1.0f, z2);
            } else {
                this.buttonState = -1;
            }
        }
        this.radialProgress.setIcon(getIconForCurrentState(), z, z2);
        invalidate();
    }

    /* JADX INFO: renamed from: org.telegram.ui.Cells.ContextLinkCell$1, reason: invalid class name */
    class AnonymousClass1 implements Runnable {
        final /* synthetic */ boolean val$ifSame;
        final /* synthetic */ int val$localId;

        AnonymousClass1(int i, boolean z) {
            this.val$localId = i;
            this.val$ifSame = z;
        }

        /* JADX WARN: Code duplicated, block: B:33:0x0192  */
        /* JADX WARN: Code duplicated, block: B:54:0x0231  */
        @Override // java.lang.Runnable
        public void run() {
            File file;
            final File file2;
            final String str;
            String attachFileName;
            File pathToAttach;
            File file3;
            String string = null;
            if (ContextLinkCell.this.documentAttachType == 5 || ContextLinkCell.this.documentAttachType == 3) {
                if (ContextLinkCell.this.documentAttach != null) {
                    string = FileLoader.getAttachFileName(ContextLinkCell.this.documentAttach);
                    file = FileLoader.getInstance(ContextLinkCell.this.currentAccount).getPathToAttach(ContextLinkCell.this.documentAttach);
                } else if (ContextLinkCell.this.inlineResult.content instanceof TLRPC.TL_webDocument) {
                    StringBuilder sb = new StringBuilder();
                    sb.append(Utilities.MD5(ContextLinkCell.this.inlineResult.content.url));
                    sb.append(".");
                    sb.append(ImageLoader.getHttpUrlExtension(ContextLinkCell.this.inlineResult.content.url, ContextLinkCell.this.documentAttachType == 5 ? "mp3" : "ogg"));
                    string = sb.toString();
                    file = new File(FileLoader.getDirectory(4), string);
                } else {
                    str = null;
                    file2 = null;
                }
                file2 = file;
                str = string;
            } else if (ContextLinkCell.this.mediaWebpage) {
                if (ContextLinkCell.this.inlineResult != null) {
                    if (ContextLinkCell.this.inlineResult.document instanceof TLRPC.TL_document) {
                        attachFileName = FileLoader.getAttachFileName(ContextLinkCell.this.inlineResult.document);
                        pathToAttach = FileLoader.getInstance(ContextLinkCell.this.currentAccount).getPathToAttach(ContextLinkCell.this.inlineResult.document);
                    } else if (ContextLinkCell.this.inlineResult.photo instanceof TLRPC.TL_photo) {
                        ContextLinkCell contextLinkCell = ContextLinkCell.this;
                        contextLinkCell.currentPhotoObject = FileLoader.getClosestPhotoSizeWithSize(contextLinkCell.inlineResult.photo.sizes, AndroidUtilities.getPhotoSize(), true);
                        attachFileName = FileLoader.getAttachFileName(ContextLinkCell.this.currentPhotoObject);
                        pathToAttach = FileLoader.getInstance(ContextLinkCell.this.currentAccount).getPathToAttach(ContextLinkCell.this.currentPhotoObject);
                    } else {
                        if (ContextLinkCell.this.inlineResult.content instanceof TLRPC.TL_webDocument) {
                            attachFileName = Utilities.MD5(ContextLinkCell.this.inlineResult.content.url) + "." + ImageLoader.getHttpUrlExtension(ContextLinkCell.this.inlineResult.content.url, FileLoader.getMimeTypePart(ContextLinkCell.this.inlineResult.content.mime_type));
                            file3 = new File(FileLoader.getDirectory(4), attachFileName);
                            if (ContextLinkCell.this.documentAttachType == 2 && (ContextLinkCell.this.inlineResult.thumb instanceof TLRPC.TL_webDocument) && "video/mp4".equals(ContextLinkCell.this.inlineResult.thumb.mime_type)) {
                                pathToAttach = file3;
                                attachFileName = null;
                            }
                        } else if (ContextLinkCell.this.inlineResult.thumb instanceof TLRPC.TL_webDocument) {
                            attachFileName = Utilities.MD5(ContextLinkCell.this.inlineResult.thumb.url) + "." + ImageLoader.getHttpUrlExtension(ContextLinkCell.this.inlineResult.thumb.url, FileLoader.getMimeTypePart(ContextLinkCell.this.inlineResult.thumb.mime_type));
                            file3 = new File(FileLoader.getDirectory(4), attachFileName);
                        } else {
                            attachFileName = null;
                            pathToAttach = null;
                        }
                        pathToAttach = file3;
                    }
                } else if (ContextLinkCell.this.documentAttach != null) {
                    attachFileName = FileLoader.getAttachFileName(ContextLinkCell.this.documentAttach);
                    pathToAttach = FileLoader.getInstance(ContextLinkCell.this.currentAccount).getPathToAttach(ContextLinkCell.this.documentAttach);
                } else {
                    attachFileName = null;
                    pathToAttach = null;
                }
                if (ContextLinkCell.this.documentAttach == null || ContextLinkCell.this.documentAttachType != 2 || MessageObject.getDocumentVideoThumb(ContextLinkCell.this.documentAttach) == null) {
                    str = attachFileName;
                    file2 = pathToAttach;
                } else {
                    file2 = pathToAttach;
                    str = string;
                }
            } else {
                str = null;
                file2 = null;
            }
            final boolean z = !TextUtils.isEmpty(str) && file2.exists();
            final int i = this.val$localId;
            final boolean z2 = this.val$ifSame;
            AndroidUtilities.runOnUIThread(new Runnable() { // from class: org.telegram.ui.Cells.ContextLinkCell$1$$ExternalSyntheticLambda0
                @Override // java.lang.Runnable
                public final void run() {
                    this.f$0.lambda$run$0(i, str, file2, z, z2);
                }
            });
        }

        /* JADX INFO: Access modifiers changed from: private */
        public /* synthetic */ void lambda$run$0(int i, String str, File file, boolean z, boolean z2) {
            ContextLinkCell contextLinkCell = ContextLinkCell.this;
            contextLinkCell.resolvingFileName = false;
            if (contextLinkCell.resolveFileNameId == i) {
                contextLinkCell.fileName = str;
                if (str == null) {
                    contextLinkCell.fileName = _UrlKt.FRAGMENT_ENCODE_SET;
                }
                contextLinkCell.cacheFile = file;
                contextLinkCell.fileExist = z;
            }
            contextLinkCell.updateButtonState(z2, true);
        }
    }

    public void setDelegate(ContextLinkCellDelegate contextLinkCellDelegate) {
        this.delegate = contextLinkCellDelegate;
    }

    public TLRPC.BotInlineResult getResult() {
        return this.inlineResult;
    }

    @Override // org.telegram.messenger.DownloadController.FileDownloadProgressListener
    public void onFailedDownload(String str, boolean z) {
        updateButtonState(true, z);
    }

    @Override // org.telegram.messenger.DownloadController.FileDownloadProgressListener
    public void onSuccessDownload(String str) {
        this.fileExist = true;
        this.radialProgress.setProgress(1.0f, true);
        updateButtonState(false, true);
    }

    @Override // org.telegram.messenger.DownloadController.FileDownloadProgressListener
    public void onProgressDownload(String str, long j, long j2) {
        this.radialProgress.setProgress(Math.min(1.0f, j / j2), true);
        int i = this.documentAttachType;
        if (i == 3 || i == 5) {
            if (this.buttonState != 4) {
                updateButtonState(false, true);
            }
        } else if (this.buttonState != 1) {
            updateButtonState(false, true);
        }
    }

    @Override // org.telegram.messenger.DownloadController.FileDownloadProgressListener
    public int getObserverTag() {
        return this.TAG;
    }

    @Override // android.view.View
    public void onInitializeAccessibilityNodeInfo(AccessibilityNodeInfo accessibilityNodeInfo) {
        super.onInitializeAccessibilityNodeInfo(accessibilityNodeInfo);
        StringBuilder sb = new StringBuilder();
        switch (this.documentAttachType) {
            case 1:
                sb.append(LocaleController.getString(R.string.AttachDocument));
                break;
            case 2:
                sb.append(LocaleController.getString(R.string.AttachGif));
                break;
            case 3:
                sb.append(LocaleController.getString(R.string.AttachAudio));
                break;
            case 4:
                sb.append(LocaleController.getString(R.string.AttachVideo));
                break;
            case 5:
                sb.append(LocaleController.getString(R.string.AttachMusic));
                break;
            case 6:
                sb.append(LocaleController.getString(R.string.AttachSticker));
                break;
            case 7:
                sb.append(LocaleController.getString(R.string.AttachPhoto));
                break;
            case 8:
                sb.append(LocaleController.getString(R.string.AttachLocation));
                break;
        }
        StaticLayout staticLayout = this.titleLayout;
        boolean z = (staticLayout == null || TextUtils.isEmpty(staticLayout.getText())) ? false : true;
        StaticLayout staticLayout2 = this.descriptionLayout;
        boolean z2 = (staticLayout2 == null || TextUtils.isEmpty(staticLayout2.getText())) ? false : true;
        if (this.documentAttachType == 5 && z && z2) {
            sb.append(", ");
            sb.append(LocaleController.formatString("AccDescrMusicInfo", R.string.AccDescrMusicInfo, this.descriptionLayout.getText(), this.titleLayout.getText()));
        } else {
            if (z) {
                if (sb.length() > 0) {
                    sb.append(", ");
                }
                sb.append(this.titleLayout.getText());
            }
            if (z2) {
                if (sb.length() > 0) {
                    sb.append(", ");
                }
                sb.append(this.descriptionLayout.getText());
            }
        }
        accessibilityNodeInfo.setText(sb);
        CheckBox2 checkBox2 = this.checkBox;
        if (checkBox2 == null || !checkBox2.isChecked()) {
            return;
        }
        accessibilityNodeInfo.setCheckable(true);
        accessibilityNodeInfo.setChecked(true);
    }

    public void setChecked(final boolean z, boolean z2) {
        CheckBox2 checkBox2 = this.checkBox;
        if (checkBox2 == null) {
            return;
        }
        if (checkBox2.getVisibility() != 0) {
            this.checkBox.setVisibility(0);
        }
        this.checkBox.setChecked(z, z2);
        AnimatorSet animatorSet = this.animator;
        if (animatorSet != null) {
            animatorSet.cancel();
            this.animator = null;
        }
        if (z2) {
            AnimatorSet animatorSet2 = new AnimatorSet();
            this.animator = animatorSet2;
            animatorSet2.playTogether(ObjectAnimator.ofFloat(this, (Property<ContextLinkCell, Float>) this.IMAGE_SCALE, z ? 0.81f : 1.0f));
            this.animator.setDuration(200L);
            this.animator.addListener(new AnimatorListenerAdapter() { // from class: org.telegram.ui.Cells.ContextLinkCell.3
                @Override // android.animation.AnimatorListenerAdapter, android.animation.Animator.AnimatorListener
                public void onAnimationEnd(Animator animator) {
                    if (ContextLinkCell.this.animator == null || !ContextLinkCell.this.animator.equals(animator)) {
                        return;
                    }
                    ContextLinkCell.this.animator = null;
                    if (z) {
                        return;
                    }
                    ContextLinkCell.this.setBackgroundColor(0);
                }

                @Override // android.animation.AnimatorListenerAdapter, android.animation.Animator.AnimatorListener
                public void onAnimationCancel(Animator animator) {
                    if (ContextLinkCell.this.animator == null || !ContextLinkCell.this.animator.equals(animator)) {
                        return;
                    }
                    ContextLinkCell.this.animator = null;
                }
            });
            this.animator.start();
            return;
        }
        this.imageScale = z ? 0.85f : 1.0f;
        invalidate();
    }

    @Override // android.view.View
    public void setPressed(boolean z) {
        super.setPressed(z);
        ButtonBounce buttonBounce = this.buttonBounce;
        if (buttonBounce != null) {
            buttonBounce.setPressed(z || this.scaled);
        }
    }
}
