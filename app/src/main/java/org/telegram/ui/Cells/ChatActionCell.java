package org.telegram.ui.Cells;

import android.R;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.ColorFilter;
import android.graphics.ColorMatrixColorFilter;
import android.graphics.CornerPathEffect;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffColorFilter;
import android.graphics.Rect;
import android.graphics.RectF;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.text.Layout;
import android.text.Spannable;
import android.text.SpannableStringBuilder;
import android.text.StaticLayout;
import android.text.TextPaint;
import android.text.TextUtils;
import android.text.style.AlignmentSpan;
import android.text.style.CharacterStyle;
import android.text.style.ClickableSpan;
import android.text.style.URLSpan;
import android.util.StateSet;
import android.util.TypedValue;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.accessibility.AccessibilityNodeInfo;
import androidx.core.graphics.ColorUtils;
import com.exteragram.messenger.ExteraConfig;
import com.exteragram.messenger.utils.chats.ChatUtils;
import j$.util.Objects;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Stack;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicReference;
import me.vkryl.core.BitwiseUtils;
import okhttp3.internal.url._UrlKt;
import org.telegram.messenger.AndroidUtilities;
import org.telegram.messenger.ApplicationLoader;
import org.telegram.messenger.BotInlineKeyboard;
import org.telegram.messenger.BuildVars;
import org.telegram.messenger.ChatObject;
import org.telegram.messenger.ChatThemeController;
import org.telegram.messenger.DialogObject;
import org.telegram.messenger.DocumentObject;
import org.telegram.messenger.DownloadController;
import org.telegram.messenger.Emoji;
import org.telegram.messenger.FileLoader;
import org.telegram.messenger.FileLog;
import org.telegram.messenger.ImageLoader;
import org.telegram.messenger.ImageLocation;
import org.telegram.messenger.ImageReceiver;
import org.telegram.messenger.LocaleController;
import org.telegram.messenger.MediaDataController;
import org.telegram.messenger.MessageObject;
import org.telegram.messenger.MessageSuggestionParams;
import org.telegram.messenger.MessagesController;
import org.telegram.messenger.NotificationCenter;
import org.telegram.messenger.SharedConfig;
import org.telegram.messenger.SvgHelper;
import org.telegram.messenger.UserConfig;
import org.telegram.messenger.UserObject;
import org.telegram.messenger.Utilities;
import org.telegram.messenger.browser.Browser;
import org.telegram.messenger.utils.tlutils.AmountUtils$Amount;
import org.telegram.messenger.utils.tlutils.AmountUtils$Currency;
import org.telegram.messenger.utils.tlutils.TlUtils;
import org.telegram.tgnet.ConnectionsManager;
import org.telegram.tgnet.TLObject;
import org.telegram.tgnet.TLRPC;
import org.telegram.tgnet.tl.TL_payments;
import org.telegram.tgnet.tl.TL_stars;
import org.telegram.tgnet.tl.TL_stories;
import org.telegram.ui.ActionBar.BaseFragment;
import org.telegram.ui.ActionBar.Theme;
import org.telegram.ui.AvatarSpan;
import org.telegram.ui.ChannelAdminLogActivity;
import org.telegram.ui.ChatBackgroundDrawable;
import org.telegram.ui.Components.AlertsCreator;
import org.telegram.ui.Components.AnimatedEmojiSpan;
import org.telegram.ui.Components.AnimatedFloat;
import org.telegram.ui.Components.AvatarDrawable;
import org.telegram.ui.Components.BulletinFactory;
import org.telegram.ui.Components.ButtonBounce;
import org.telegram.ui.Components.ColoredImageSpan;
import org.telegram.ui.Components.CubicBezierInterpolator;
import org.telegram.ui.Components.Forum.ForumUtilities;
import org.telegram.ui.Components.ImageUpdater;
import org.telegram.ui.Components.LoadingDrawable;
import org.telegram.ui.Components.Premium.StarParticlesView;
import org.telegram.ui.Components.RLottieDrawable;
import org.telegram.ui.Components.RadialProgress2;
import org.telegram.ui.Components.RadialProgressView;
import org.telegram.ui.Components.Reactions.ReactionsLayoutInBubble;
import org.telegram.ui.Components.RecyclerListView;
import org.telegram.ui.Components.ScaleStateListAnimator;
import org.telegram.ui.Components.SuggestBirthdayActionLayout;
import org.telegram.ui.Components.Text;
import org.telegram.ui.Components.TopicSeparator;
import org.telegram.ui.Components.TypefaceSpan;
import org.telegram.ui.Components.URLSpanNoUnderline;
import org.telegram.ui.Components.spoilers.SpoilerEffect;
import org.telegram.ui.Gifts.GiftSheet;
import org.telegram.ui.GradientClip;
import org.telegram.ui.LaunchActivity;
import org.telegram.ui.PhotoViewer;
import org.telegram.ui.ProfileActivity;
import org.telegram.ui.Stars.GiftOfferSheet;
import org.telegram.ui.Stars.StarGiftSheet;
import org.telegram.ui.Stars.StarGiftUniqueActionLayout;
import org.telegram.ui.Stars.StarsController;
import org.telegram.ui.Stars.StarsIntroActivity;
import org.telegram.ui.Stories.StoriesUtilities;
import org.telegram.ui.Stories.recorder.HintView2;
import org.telegram.ui.Stories.recorder.PreviewView;

public class ChatActionCell extends BaseCell implements DownloadController.FileDownloadProgressListener, NotificationCenter.NotificationCenterDelegate {
    private static Map monthsToEmoticon;
    private int TAG;
    private SpannableStringBuilder accessibilityText;
    private boolean actionPressed;
    private int adaptiveEmojiColor;
    private ColorFilter adaptiveEmojiColorFilter;
    private AnimatedEmojiSpan.EmojiGroupedSpans animatedEmojiStack;
    private boolean attachedToWindow;
    private AvatarDrawable avatarDrawable;
    StoriesUtilities.AvatarStoryParams avatarStoryParams;
    private int backgroundButtonTop;
    private int backgroundHeight;
    private int backgroundLeft;
    private Path backgroundPath;
    private final Path backgroundPath2;
    private RectF backgroundRect;
    private int backgroundRectHeight;
    private int backgroundRight;
    public SuggestBirthdayActionLayout birthdayLayout;
    private final Path botButtonPath;
    private final float[] botButtonRadii;
    private ArrayList botButtons;
    private BotInlineKeyboard.Source botInlineButtons;
    private final ButtonBounce bounce;
    private boolean buttonClickableAsImage;
    private boolean canDrawInParent;
    private GiftSheet.CardBackground cardBackground;
    private Path clipPath;
    private int currentAccount;
    private MessageObject currentMessageObject;
    private ImageLocation currentVideoLocation;
    private int customDate;
    private CharSequence customText;
    private ChatActionCellDelegate delegate;
    private float dimAmount;
    private final Paint dimPaint;
    public boolean firstInChat;
    private boolean forceWasUnread;
    private boolean giftButtonPressed;
    private RectF giftButtonRect;
    private TLRPC.VideoSize giftEffectAnimation;
    private int giftPremiumAdditionalHeight;
    private StaticLayout giftPremiumButtonLayout;
    private float giftPremiumButtonWidth;
    private Text giftPremiumReleasedText;
    private StaticLayout giftPremiumSubtitleLayout;
    private TextLayout giftPremiumText;
    private GradientClip giftPremiumTextClip;
    private boolean giftPremiumTextCollapsed;
    private int giftPremiumTextCollapsedHeight;
    private AnimatedFloat giftPremiumTextExpandedAnimated;
    private Text giftPremiumTextMore;
    private int giftPremiumTextMoreH;
    private int giftPremiumTextMoreX;
    private int giftPremiumTextMoreY;
    private boolean giftPremiumTextUncollapsed;
    private StaticLayout giftPremiumTitleLayout;
    private boolean giftRectEmpty;
    private int giftRectSize;
    private Paint giftReleasedBackgroundPaint;
    private CornerPathEffect giftRibbonPaintEffect;
    private ColorMatrixColorFilter giftRibbonPaintFilter;
    private boolean giftRibbonPaintFilterDark;
    private Path giftRibbonPath;
    private Text giftRibbonText;
    private TLRPC.Document giftSticker;
    private ImageReceiver.ImageReceiverDelegate giftStickerDelegate;
    private TextPaint giftSubtitlePaint;
    private TextPaint giftTextPaint;
    private TextPaint giftTitlePaint;
    private boolean hasReplyMessage;
    private boolean imagePressed;
    private ImageReceiver imageReceiver;
    private boolean invalidateColors;
    private Runnable invalidateListener;
    private boolean invalidatePath;
    private View invalidateWithParent;
    private boolean invalidatesParent;
    public boolean isAllChats;
    public boolean isForum;
    public boolean isMonoForum;
    public boolean isSideMenuEnabled;
    public boolean isSideMenued;
    private boolean isSpoilerRevealing;
    private float lastTouchX;
    private float lastTouchY;
    private ArrayList lineHeights;
    private ArrayList lineWidths;
    private LoadingDrawable loadingDrawable;
    private boolean offerExpired;
    private View.OnClickListener onActionClick;
    private View.OnLongClickListener onActionLongClick;
    private int overriddenMaxWidth;
    private int overrideBackground;
    private Paint overrideBackgroundPaint;
    private int overrideText;
    private TextPaint overrideTextPaint;
    private int pressedBotButton;
    private URLSpan pressedLink;
    private final int[] pressedState;
    private int previousWidth;
    float progressToProgress;
    RadialProgressView progressView;
    private RadialProgress2 radialProgress;
    private final float[] radii;
    public final ReactionsLayoutInBubble reactionsLayoutInBubble;
    private RectF rect;
    private View rippleView;
    private StaticLayout settingWallpaperLayout;
    TextPaint settingWallpaperPaint;
    private float settingWallpaperProgress;
    private StaticLayout settingWallpaperProgressTextLayout;
    public boolean showTopicSeparator;
    public float sideMenuAlpha;
    public int sideMenuWidth;
    private SpoilerEffect spoilerPressed;
    public List spoilers;
    private Stack spoilersPool;
    public final StarGiftUniqueActionLayout starGiftLayout;
    public float starGiftLayoutX;
    public float starGiftLayoutY;
    private StarParticlesView.Drawable starParticlesDrawable;
    private Path starsPath;
    private int starsSize;
    private int stickerSize;
    private int textHeight;
    private StaticLayout textLayout;
    TextPaint textPaint;
    private boolean textPressed;
    private int textWidth;
    private int textX;
    private int textXLeft;
    private int textY;
    private Theme.ResourcesProvider themeDelegate;
    private int titleHeight;
    private StaticLayout titleLayout;
    private int titleXLeft;
    public TopicSeparator topicSeparator;
    private int topicSeparatorTopPadding;
    public final TransitionParams transitionParams;
    private float viewTop;
    private float viewTranslationX;
    private boolean visiblePartSet;
    private Drawable wallpaperPreviewDrawable;
    private boolean wasLayout;

    public boolean isFloating() {
        return false;
    }

    @Override // org.telegram.messenger.DownloadController.FileDownloadProgressListener
    public void onFailedDownload(String str, boolean z) {
    }

    @Override // org.telegram.messenger.DownloadController.FileDownloadProgressListener
    public void onProgressDownload(String str, long j, long j2) {
    }

    @Override // org.telegram.messenger.DownloadController.FileDownloadProgressListener
    public void onProgressUpload(String str, long j, long j2, boolean z) {
    }

    static {
        HashMap map = new HashMap();
        monthsToEmoticon = map;
        map.put(1, "1⃣");
        monthsToEmoticon.put(3, "2⃣");
        monthsToEmoticon.put(6, "3⃣");
        monthsToEmoticon.put(12, "4⃣");
        monthsToEmoticon.put(24, "5⃣");
    }

    @Override // org.telegram.messenger.NotificationCenter.NotificationCenterDelegate
    public void didReceivedNotification(int i, int i2, Object... objArr) {
        MessageObject messageObject;
        if (i == NotificationCenter.startSpoilers) {
            setSpoilersSuppressed(false);
            return;
        }
        if (i == NotificationCenter.stopSpoilers) {
            setSpoilersSuppressed(true);
            return;
        }
        if (i == NotificationCenter.didUpdatePremiumGiftStickers || i == NotificationCenter.starGiftsLoaded || i == NotificationCenter.didUpdateTonGiftStickers) {
            MessageObject messageObject2 = this.currentMessageObject;
            if (messageObject2 != null) {
                setMessageObject(messageObject2, true);
                return;
            }
            return;
        }
        if (i == NotificationCenter.diceStickersDidLoad && Objects.equals(objArr[0], UserConfig.getInstance(this.currentAccount).premiumGiftsStickerPack) && (messageObject = this.currentMessageObject) != null) {
            setMessageObject(messageObject, true);
        }
    }

    public void setSpoilersSuppressed(boolean z) {
        Iterator it = this.spoilers.iterator();
        while (it.hasNext()) {
            ((SpoilerEffect) it.next()).setSuppressUpdates(z);
        }
    }

    public void setInvalidateWithParent(View view) {
        this.invalidateWithParent = view;
    }

    public boolean hasButton() {
        MessageObject messageObject = this.currentMessageObject;
        return (messageObject == null || !isButtonLayout(messageObject) || this.giftPremiumButtonLayout == null) ? false : true;
    }

    public interface ChatActionCellDelegate {
        boolean canDrawOutboundsContent();

        void didClickButton(ChatActionCell chatActionCell);

        void didClickImage(ChatActionCell chatActionCell);

        boolean didLongPress(ChatActionCell chatActionCell, float f, float f2);

        void didOpenPremiumGift(ChatActionCell chatActionCell, TLRPC.TL_premiumGiftOption tL_premiumGiftOption, String str, boolean z);

        void didOpenPremiumGiftChannel(ChatActionCell chatActionCell, String str, boolean z);

        void didPressReaction(ChatActionCell chatActionCell, TLRPC.ReactionCount reactionCount, boolean z, float f, float f2);

        void didPressReplyMessage(ChatActionCell chatActionCell, int i);

        void didPressTaskLink(ChatActionCell chatActionCell, int i, int i2);

        void forceUpdate(ChatActionCell chatActionCell, boolean z);

        BaseFragment getBaseFragment();

        long getDialogId();

        long getTopicId();

        void needOpenInviteLink(TLRPC.TL_chatInviteExported tL_chatInviteExported);

        void needOpenUserProfile(long j);

        void needShowEffectOverlay(ChatActionCell chatActionCell, TLRPC.Document document, TLRPC.VideoSize videoSize);

        void onTopicClick(ChatActionCell chatActionCell);

        /* JADX INFO: renamed from: org.telegram.ui.Cells.ChatActionCell$ChatActionCellDelegate$-CC, reason: invalid class name */
        public abstract /* synthetic */ class CC {
            public static void $default$didClickImage(ChatActionCellDelegate chatActionCellDelegate, ChatActionCell chatActionCell) {
            }

            public static void $default$didClickButton(ChatActionCellDelegate chatActionCellDelegate, ChatActionCell chatActionCell) {
            }

            public static void $default$didOpenPremiumGift(ChatActionCellDelegate chatActionCellDelegate, ChatActionCell chatActionCell, TLRPC.TL_premiumGiftOption tL_premiumGiftOption, String str, boolean z) {
            }

            public static void $default$didOpenPremiumGiftChannel(ChatActionCellDelegate chatActionCellDelegate, ChatActionCell chatActionCell, String str, boolean z) {
            }

            public static boolean $default$didLongPress(ChatActionCellDelegate chatActionCellDelegate, ChatActionCell chatActionCell, float f, float f2) {
                return false;
            }

            public static void $default$needOpenUserProfile(ChatActionCellDelegate chatActionCellDelegate, long j) {
            }

            public static void $default$didPressReplyMessage(ChatActionCellDelegate chatActionCellDelegate, ChatActionCell chatActionCell, int i) {
            }

            public static void $default$didPressTaskLink(ChatActionCellDelegate chatActionCellDelegate, ChatActionCell chatActionCell, int i, int i2) {
            }

            public static void $default$didPressReaction(ChatActionCellDelegate chatActionCellDelegate, ChatActionCell chatActionCell, TLRPC.ReactionCount reactionCount, boolean z, float f, float f2) {
            }

            public static void $default$needOpenInviteLink(ChatActionCellDelegate chatActionCellDelegate, TLRPC.TL_chatInviteExported tL_chatInviteExported) {
            }

            public static void $default$needShowEffectOverlay(ChatActionCellDelegate chatActionCellDelegate, ChatActionCell chatActionCell, TLRPC.Document document, TLRPC.VideoSize videoSize) {
            }

            public static void $default$onTopicClick(ChatActionCellDelegate chatActionCellDelegate, ChatActionCell chatActionCell) {
            }

            public static BaseFragment $default$getBaseFragment(ChatActionCellDelegate chatActionCellDelegate) {
                return null;
            }

            public static long $default$getDialogId(ChatActionCellDelegate chatActionCellDelegate) {
                return 0L;
            }

            public static long $default$getTopicId(ChatActionCellDelegate chatActionCellDelegate) {
                return 0L;
            }

            public static boolean $default$canDrawOutboundsContent(ChatActionCellDelegate chatActionCellDelegate) {
                return true;
            }

            public static void $default$forceUpdate(ChatActionCellDelegate chatActionCellDelegate, ChatActionCell chatActionCell, boolean z) {
            }
        }
    }

    public void setShowTopic(boolean z) {
        if (this.showTopicSeparator != z) {
            this.showTopicSeparator = z;
            invalidateOutbounds();
            invalidate();
        }
    }

    class TextLayout {
        public AnimatedEmojiSpan.EmojiGroupedSpans emoji;
        public StaticLayout layout;
        public TextPaint paint;
        public int width;
        public float x;
        public float y;
        public List spoilers = new ArrayList();
        public final AtomicReference patchedLayout = new AtomicReference();

        TextLayout() {
        }

        public void setText(CharSequence charSequence, TextPaint textPaint, int i) {
            this.paint = textPaint;
            this.width = i;
            this.layout = new StaticLayout(charSequence, textPaint, i, Layout.Alignment.ALIGN_CENTER, 1.1f, 0.0f, false);
            if (ChatActionCell.this.currentMessageObject == null || !ChatActionCell.this.currentMessageObject.isSpoilersRevealed) {
                SpoilerEffect.addSpoilers(ChatActionCell.this, this.layout, -1, i, null, this.spoilers);
            } else {
                List list = this.spoilers;
                if (list != null) {
                    list.clear();
                }
            }
            attach();
        }

        public void attach() {
            this.emoji = AnimatedEmojiSpan.update(0, (View) ChatActionCell.this, false, this.emoji, this.layout);
        }

        public void detach() {
            AnimatedEmojiSpan.release(ChatActionCell.this, this.emoji);
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$new$0(ImageReceiver imageReceiver, boolean z, boolean z2, boolean z3) {
        RLottieDrawable lottieAnimation;
        ChatActionCellDelegate chatActionCellDelegate;
        if (!z || (lottieAnimation = this.imageReceiver.getLottieAnimation()) == null) {
            return;
        }
        MessageObject messageObject = this.currentMessageObject;
        if (messageObject != null && !messageObject.playedGiftAnimation) {
            messageObject.playedGiftAnimation = true;
            lottieAnimation.setCurrentFrame(0, false);
            AndroidUtilities.runOnUIThread(new ChatActionCell$$ExternalSyntheticLambda13(lottieAnimation));
            if (messageObject.wasUnread || this.forceWasUnread) {
                messageObject.wasUnread = false;
                this.forceWasUnread = false;
                try {
                    performHapticFeedback(3, 2);
                } catch (Exception unused) {
                }
                if (getContext() instanceof LaunchActivity) {
                    ((LaunchActivity) getContext()).getFireworksOverlay().start();
                }
                TLRPC.VideoSize videoSize = this.giftEffectAnimation;
                if (videoSize == null || (chatActionCellDelegate = this.delegate) == null) {
                    return;
                }
                chatActionCellDelegate.needShowEffectOverlay(this, this.giftSticker, videoSize);
                return;
            }
            return;
        }
        if (lottieAnimation.getCurrentFrame() < 1) {
            lottieAnimation.stop();
            lottieAnimation.setCurrentFrame(lottieAnimation.getFramesCount() - 1, false);
        }
    }

    public ChatActionCell(Context context) {
        this(context, false, null);
    }

    public ChatActionCell(Context context, boolean z, Theme.ResourcesProvider resourcesProvider) {
        super(context);
        this.bounce = new ButtonBounce(this);
        this.currentAccount = UserConfig.selectedAccount;
        this.avatarStoryParams = new StoriesUtilities.AvatarStoryParams(false);
        this.showTopicSeparator = true;
        this.giftButtonRect = new RectF();
        this.spoilers = new ArrayList();
        this.spoilersPool = new Stack();
        this.reactionsLayoutInBubble = new ReactionsLayoutInBubble(this);
        this.overrideBackground = -1;
        this.overrideText = -1;
        this.lineWidths = new ArrayList();
        this.lineHeights = new ArrayList();
        this.backgroundPath = new Path();
        this.rect = new RectF();
        this.invalidatePath = true;
        this.invalidateColors = false;
        this.giftPremiumTextUncollapsed = false;
        this.giftPremiumTextCollapsed = false;
        this.giftPremiumTextExpandedAnimated = new AnimatedFloat(this, 0L, 320L, CubicBezierInterpolator.EASE_OUT_QUINT);
        this.buttonClickableAsImage = true;
        this.giftTitlePaint = new TextPaint(1);
        this.giftTextPaint = new TextPaint(1);
        this.giftSubtitlePaint = new TextPaint(1);
        this.radialProgress = new RadialProgress2(this);
        this.giftStickerDelegate = new ImageReceiver.ImageReceiverDelegate() { // from class: org.telegram.ui.Cells.ChatActionCell$$ExternalSyntheticLambda5
            @Override // org.telegram.messenger.ImageReceiver.ImageReceiverDelegate
            public final void didSetImage(ImageReceiver imageReceiver, boolean z2, boolean z3, boolean z4) {
                this.f$0.lambda$new$0(imageReceiver, z2, z3, z4);
            }

            @Override // org.telegram.messenger.ImageReceiver.ImageReceiverDelegate
            public /* synthetic */ void didSetImageBitmap(int i, String str, Drawable drawable) {
                ImageReceiver.ImageReceiverDelegate.CC.$default$didSetImageBitmap(this, i, str, drawable);
            }

            @Override // org.telegram.messenger.ImageReceiver.ImageReceiverDelegate
            public /* synthetic */ void onAnimationReady(ImageReceiver imageReceiver) {
                ImageReceiver.ImageReceiverDelegate.CC.$default$onAnimationReady(this, imageReceiver);
            }
        };
        this.starsPath = new Path();
        this.botButtons = new ArrayList();
        this.dimPaint = new Paint(1);
        this.backgroundPath2 = new Path();
        this.radii = new float[8];
        this.botButtonRadii = new float[8];
        this.botButtonPath = new Path();
        this.pressedState = new int[]{R.attr.state_enabled, R.attr.state_pressed};
        this.transitionParams = new TransitionParams();
        this.avatarStoryParams.drawSegments = false;
        this.canDrawInParent = z;
        this.themeDelegate = resourcesProvider;
        ImageReceiver imageReceiver = new ImageReceiver(this);
        this.imageReceiver = imageReceiver;
        imageReceiver.setRoundRadius(ExteraConfig.getAvatarCorners(AndroidUtilities.roundMessageSize, true));
        this.avatarDrawable = new AvatarDrawable();
        this.TAG = DownloadController.getInstance(this.currentAccount).generateObserverTag();
        this.starGiftLayout = new StarGiftUniqueActionLayout(this.currentAccount, this, resourcesProvider);
        this.giftTitlePaint.setTextSize(TypedValue.applyDimension(1, 16.0f, getResources().getDisplayMetrics()));
        this.giftSubtitlePaint.setTextSize(TypedValue.applyDimension(1, 15.0f, getResources().getDisplayMetrics()));
        this.giftTextPaint.setTextSize(TypedValue.applyDimension(1, 15.0f, getResources().getDisplayMetrics()));
        View view = new View(context);
        this.rippleView = view;
        view.setBackground(Theme.createSelectorDrawable(Theme.multAlpha(-16777216, 0.1f), 7, AndroidUtilities.dp(16.0f)));
        this.rippleView.setVisibility(8);
        addView(this.rippleView);
        StarParticlesView.Drawable drawable = new StarParticlesView.Drawable(10);
        this.starParticlesDrawable = drawable;
        drawable.type = 100;
        drawable.isCircle = false;
        drawable.roundEffect = true;
        drawable.useRotate = false;
        drawable.useBlur = true;
        drawable.checkBounds = true;
        drawable.size1 = 1;
        drawable.k3 = 0.98f;
        drawable.k2 = 0.98f;
        drawable.k1 = 0.98f;
        drawable.paused = false;
        drawable.speedScale = 0.0f;
        drawable.minLifeTime = 750L;
        drawable.randLifeTime = 750;
        drawable.init();
    }

    public void setDelegate(ChatActionCellDelegate chatActionCellDelegate) {
        this.delegate = chatActionCellDelegate;
    }

    public ChatActionCellDelegate getDelegate() {
        return this.delegate;
    }

    public void setCustomDate(int i, boolean z, boolean z2) {
        String dateChat;
        int i2 = this.customDate;
        if (i2 == i || i2 / 3600 == i / 3600) {
            return;
        }
        if (!z) {
            dateChat = LocaleController.formatDateChat(i);
        } else if (i == 2147483646) {
            dateChat = LocaleController.getString("MessageScheduledUntilOnline", org.telegram.messenger.R.string.MessageScheduledUntilOnline);
        } else {
            dateChat = LocaleController.formatString("MessageScheduledOn", org.telegram.messenger.R.string.MessageScheduledOn, LocaleController.formatDateChat(i));
        }
        this.customDate = i;
        CharSequence charSequence = this.customText;
        if (charSequence == null || !TextUtils.equals(dateChat, charSequence)) {
            this.customText = dateChat;
            this.accessibilityText = null;
            updateTextInternal(z2);
        }
    }

    private void updateTextInternal(boolean z) {
        if (getMeasuredWidth() != 0) {
            createLayout(this.customText, getMeasuredWidth());
            invalidate();
        }
        if (this.wasLayout) {
            buildLayout();
        } else if (z) {
            AndroidUtilities.runOnUIThread(new Runnable() { // from class: org.telegram.ui.Cells.ChatActionCell$$ExternalSyntheticLambda6
                @Override // java.lang.Runnable
                public final void run() {
                    this.f$0.requestLayout();
                }
            });
        } else {
            requestLayout();
        }
    }

    public void setCustomText(CharSequence charSequence) {
        this.customText = charSequence;
        if (charSequence != null) {
            updateTextInternal(false);
        }
    }

    public void setOverrideColor(int i, int i2) {
        this.overrideBackground = i;
        this.overrideText = i2;
    }

    public void setMessageObject(MessageObject messageObject) {
        setMessageObject(messageObject, false);
    }

    /* JADX WARN: Code duplicated, block: B:102:0x01e2  */
    /* JADX WARN: Code duplicated, block: B:107:0x01ed  */
    /* JADX WARN: Code duplicated, block: B:263:0x0622  */
    /* JADX WARN: Code duplicated, block: B:347:0x07b8  */
    /* JADX WARN: Code duplicated, block: B:351:0x07ca  */
    /* JADX WARN: Code duplicated, block: B:353:0x07d1  */
    /* JADX WARN: Code duplicated, block: B:355:0x07da  */
    /* JADX WARN: Code duplicated, block: B:357:0x07f5  */
    /* JADX WARN: Code duplicated, block: B:360:0x081a  */
    /* JADX WARN: Code duplicated, block: B:362:0x081d  */
    /* JADX WARN: Code duplicated, block: B:365:0x0827  */
    /* JADX WARN: Code duplicated, block: B:366:0x0829  */
    /* JADX WARN: Code duplicated, block: B:371:0x0856  */
    /* JADX WARN: Code duplicated, block: B:379:0x0879  */
    /* JADX WARN: Code duplicated, block: B:382:0x0896 A[LOOP:5: B:377:0x0871->B:382:0x0896, LOOP_END] */
    /* JADX WARN: Code duplicated, block: B:386:0x08a1  */
    /* JADX WARN: Code duplicated, block: B:387:0x08da A[DONT_INVERT] */
    /* JADX WARN: Code duplicated, block: B:388:0x08dc  */
    /* JADX WARN: Code duplicated, block: B:390:0x08e4  */
    /* JADX WARN: Code duplicated, block: B:392:0x08e7  */
    /* JADX WARN: Code duplicated, block: B:440:0x088b A[SYNTHETIC] */
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
    public void setMessageObject(MessageObject messageObject, boolean z) {
        TLRPC.TL_messageReactions tL_messageReactions;
        String str;
        String str2;
        TLRPC.Document document;
        TLRPC.Document document2;
        String str3;
        long j;
        String tonGiftEmoji;
        TLRPC.TL_messages_stickerSet stickerSetByEmojiOrName;
        Object obj;
        TLRPC.TL_messages_stickerSet tL_messages_stickerSet;
        TLRPC.Document giftDocument;
        TLRPC.Document giftDocument2;
        BotInlineKeyboard.Source source;
        boolean z2;
        int i;
        int i2;
        int rowsCount;
        int i3;
        int columnsCount;
        int i4;
        BotButton botButton;
        int iconRes;
        boolean z3;
        boolean z4;
        TLRPC.TL_messages_stickerSet tL_messages_stickerSet2;
        TLRPC.PhotoSize photoSize;
        TLRPC.VideoSize closestVideoSizeWithSize;
        TLRPC.PhotoSize photoSize2;
        boolean z5;
        float f;
        TLRPC.Message message;
        TLRPC.WallPaper wallPaper;
        TLRPC.MessageAction messageAction;
        TLRPC.Document document3;
        String str4;
        int i5;
        StaticLayout staticLayout;
        MessageObject messageObject2 = messageObject;
        Integer num = 160;
        if (messageObject2 == null) {
            return;
        }
        if (this.currentMessageObject != messageObject2 || (!((staticLayout = this.textLayout) == null || TextUtils.equals(staticLayout.getText(), messageObject2.messageText)) || (!(this.hasReplyMessage || messageObject2.replyMessageObject == null) || z || messageObject2.type == 21 || messageObject2.forceUpdate))) {
            if (BuildVars.DEBUG_PRIVATE_VERSION && Thread.currentThread() != ApplicationLoader.applicationHandler.getLooper().getThread()) {
                FileLog.e(new IllegalStateException("Wrong thread!!!"));
            }
            this.botButtons.clear();
            this.botInlineButtons = null;
            this.accessibilityText = null;
            MessageObject messageObject3 = this.currentMessageObject;
            boolean z6 = messageObject3 == null || messageObject3.stableId != messageObject2.stableId;
            if (messageObject3 != null) {
                messageObject2.playedGiftAnimation = messageObject3.playedGiftAnimation;
            }
            this.currentMessageObject = messageObject2;
            messageObject2.forceUpdate = false;
            this.hasReplyMessage = messageObject2.replyMessageObject != null;
            DownloadController.getInstance(this.currentAccount).removeLoadingFileObserver(this);
            this.previousWidth = 0;
            this.isSpoilerRevealing = false;
            TextLayout textLayout = this.giftPremiumText;
            if (textLayout != null && z6) {
                textLayout.detach();
                this.giftPremiumText = null;
                this.giftPremiumTextUncollapsed = false;
            }
            if (z6 || messageObject2.reactionsChanged) {
                messageObject2.reactionsChanged = false;
                TLRPC.Message message2 = messageObject2.messageOwner;
                boolean z7 = (message2 == null || (tL_messageReactions = message2.reactions) == null || !tL_messageReactions.reactions_as_tags) ? false : true;
                if (messageObject2.shouldDrawReactions()) {
                    this.reactionsLayoutInBubble.setMessage(messageObject2, !messageObject2.shouldDrawReactionsInLayout(), z7, this.themeDelegate);
                } else {
                    this.reactionsLayoutInBubble.setMessage(null, false, false, this.themeDelegate);
                }
            }
            if (messageObject2.type == 32) {
                if (this.birthdayLayout == null) {
                    this.birthdayLayout = new SuggestBirthdayActionLayout(this.currentAccount, this, this.themeDelegate);
                    if (isCellAttachedToWindow()) {
                        this.birthdayLayout.attach();
                    }
                }
                this.birthdayLayout.set(messageObject2);
            } else {
                SuggestBirthdayActionLayout suggestBirthdayActionLayout = this.birthdayLayout;
                if (suggestBirthdayActionLayout != null) {
                    suggestBirthdayActionLayout.detach();
                    this.birthdayLayout = null;
                }
            }
            this.starGiftLayout.set(messageObject2, !z6);
            this.imageReceiver.setAutoRepeatCount(0);
            this.imageReceiver.clearDecorators();
            if (messageObject2.type != 22) {
                this.wallpaperPreviewDrawable = null;
            }
            if (messageObject2.actionDeleteGroupEventId != -1) {
                ScaleStateListAnimator.apply(this, 0.02f, 1.2f);
                this.overriddenMaxWidth = Math.max(AndroidUtilities.dp(250.0f), HintView2.cutInFancyHalf(messageObject2.messageText, (TextPaint) getThemedPaint("paintChatActionText")));
                ProfileActivity.ShowDrawable showDrawableFindDrawable = ChannelAdminLogActivity.findDrawable(messageObject2.messageText);
                if (showDrawableFindDrawable != null) {
                    showDrawableFindDrawable.setView(this);
                }
            } else {
                ScaleStateListAnimator.reset(this);
                this.overriddenMaxWidth = 0;
            }
            if (messageObject2.isStoryMention()) {
                TLRPC.User user = MessagesController.getInstance(this.currentAccount).getUser(Long.valueOf(messageObject2.messageOwner.media.user_id));
                this.avatarDrawable.setInfo(this.currentAccount, user);
                TL_stories.StoryItem storyItem = messageObject2.messageOwner.media.storyItem;
                if (storyItem != null && storyItem.noforwards) {
                    this.imageReceiver.setForUserOrChat(user, this.avatarDrawable, null, true, 0, true);
                } else {
                    StoriesUtilities.setImage(this.imageReceiver, storyItem);
                }
                this.imageReceiver.setRoundRadius(ExteraConfig.getAvatarCorners(this.stickerSize * 0.7f, true));
            } else {
                int i6 = messageObject2.type;
                if (i6 == 22) {
                    if (messageObject2.strippedThumb == null) {
                        int size = messageObject2.photoThumbs.size();
                        for (int i7 = 0; i7 < size && !(messageObject2.photoThumbs.get(i7) instanceof TLRPC.TL_photoStrippedSize); i7++) {
                        }
                    }
                    TLRPC.TL_channelAdminLogEvent tL_channelAdminLogEvent = messageObject2.currentEvent;
                    if (tL_channelAdminLogEvent != null) {
                        TLRPC.ChannelAdminLogEventAction channelAdminLogEventAction = tL_channelAdminLogEvent.action;
                        if (channelAdminLogEventAction instanceof TLRPC.TL_channelAdminLogEventActionChangeWallpaper) {
                            wallPaper = ((TLRPC.TL_channelAdminLogEventActionChangeWallpaper) channelAdminLogEventAction).new_value;
                        } else {
                            message = messageObject2.messageOwner;
                            if (message != null || (messageAction = message.action) == null) {
                                wallPaper = null;
                            } else {
                                wallPaper = messageAction.wallpaper;
                            }
                        }
                    } else {
                        message = messageObject2.messageOwner;
                        if (message != null) {
                            wallPaper = null;
                        } else {
                            wallPaper = null;
                        }
                    }
                    if (!TextUtils.isEmpty(ChatThemeController.getWallpaperEmoticon(wallPaper))) {
                        Theme.ResourcesProvider resourcesProvider = this.themeDelegate;
                        boolean zIsDark = resourcesProvider != null ? resourcesProvider.isDark() : Theme.isCurrentThemeDark();
                        this.imageReceiver.clearImage();
                        Drawable backgroundDrawableFromTheme = PreviewView.getBackgroundDrawableFromTheme(this.currentAccount, ChatThemeController.getWallpaperEmoticon(wallPaper), zIsDark, false);
                        this.wallpaperPreviewDrawable = backgroundDrawableFromTheme;
                        if (backgroundDrawableFromTheme != null) {
                            backgroundDrawableFromTheme.setCallback(this);
                        }
                    } else if (wallPaper != null && (str4 = wallPaper.uploadingImage) != null) {
                        this.imageReceiver.setImage(ImageLocation.getForPath(str4), "150_150_wallpaper" + wallPaper.id + ChatBackgroundDrawable.hash(wallPaper.settings), null, null, ChatBackgroundDrawable.createThumb(wallPaper), 0L, null, wallPaper, 1);
                        this.wallpaperPreviewDrawable = null;
                    } else if (wallPaper != null) {
                        TLObject tLObject = messageObject2.photoThumbsObject;
                        if (tLObject instanceof TLRPC.Document) {
                            document3 = (TLRPC.Document) tLObject;
                        } else {
                            document3 = wallPaper.document;
                        }
                        this.imageReceiver.setImage(ImageLocation.getForDocument(document3), "150_150_wallpaper" + wallPaper.id + ChatBackgroundDrawable.hash(wallPaper.settings), null, null, ChatBackgroundDrawable.createThumb(wallPaper), 0L, null, wallPaper, 1);
                        this.wallpaperPreviewDrawable = null;
                    } else {
                        this.wallpaperPreviewDrawable = null;
                    }
                    this.imageReceiver.setRoundRadius(ExteraConfig.getAvatarCorners(this.stickerSize * 0.7f, true));
                    if (getUploadingInfoProgress(messageObject) == 1.0f) {
                        this.radialProgress.setProgress(1.0f, !z6);
                        this.radialProgress.setIcon(4, !z6, !z6);
                    } else {
                        this.radialProgress.setIcon(3, !z6, !z6);
                    }
                } else if (i6 == 21) {
                    this.imageReceiver.setRoundRadius(ExteraConfig.getAvatarCorners(this.stickerSize * 0.7f, true));
                    this.imageReceiver.setAllowStartLottieAnimation(true);
                    this.imageReceiver.setDelegate(null);
                    TLRPC.TL_messageActionSuggestProfilePhoto tL_messageActionSuggestProfilePhoto = (TLRPC.TL_messageActionSuggestProfilePhoto) messageObject2.messageOwner.action;
                    TLRPC.VideoSize closestVideoSizeWithSize2 = FileLoader.getClosestVideoSizeWithSize(tL_messageActionSuggestProfilePhoto.photo.video_sizes, MediaDataController.MAX_STYLE_RUNS_COUNT);
                    ArrayList arrayList = tL_messageActionSuggestProfilePhoto.photo.video_sizes;
                    ImageLocation forPhoto = (arrayList == null || arrayList.isEmpty()) ? null : ImageLocation.getForPhoto(closestVideoSizeWithSize2, tL_messageActionSuggestProfilePhoto.photo);
                    TLRPC.Photo photo = messageObject2.messageOwner.action.photo;
                    if (messageObject2.strippedThumb != null) {
                        photoSize2 = null;
                        break;
                    }
                    int size2 = messageObject2.photoThumbs.size();
                    int i8 = 0;
                    while (true) {
                        if (i8 >= size2) {
                            photoSize2 = null;
                            break;
                        }
                        photoSize2 = messageObject2.photoThumbs.get(i8);
                        if (photoSize2 instanceof TLRPC.TL_photoStrippedSize) {
                            break;
                        } else {
                            i8++;
                        }
                    }
                    TLRPC.PhotoSize closestPhotoSizeWithSize = FileLoader.getClosestPhotoSizeWithSize(messageObject2.photoThumbs, MediaDataController.MAX_STYLE_RUNS_COUNT);
                    if (closestPhotoSizeWithSize == null) {
                        z5 = false;
                        f = 1.0f;
                    } else if (closestVideoSizeWithSize2 != null) {
                        z5 = false;
                        f = 1.0f;
                        this.imageReceiver.setImage(forPhoto, ImageLoader.AUTOPLAY_FILTER, ImageLocation.getForPhoto(closestPhotoSizeWithSize, photo), "150_150", ImageLocation.getForObject(photoSize2, messageObject2.photoThumbsObject), "50_50_b", messageObject2.strippedThumb, 0L, null, messageObject, 0);
                        messageObject2 = messageObject;
                    } else {
                        z5 = false;
                        f = 1.0f;
                        this.imageReceiver.setImage(ImageLocation.getForPhoto(closestPhotoSizeWithSize, photo), "150_150", ImageLocation.getForObject(photoSize2, messageObject2.photoThumbsObject), "50_50_b", messageObject2.strippedThumb, 0L, null, messageObject2, 0);
                    }
                    this.imageReceiver.setAllowStartLottieAnimation(z5);
                    ImageUpdater imageUpdater = MessagesController.getInstance(this.currentAccount).photoSuggestion.get(messageObject2.messageOwner.local_id);
                    if (imageUpdater == null || imageUpdater.getCurrentImageProgress() == f) {
                        this.radialProgress.setProgress(f, !z6);
                        this.radialProgress.setIcon(4, !z6, !z6);
                    } else {
                        this.radialProgress.setIcon(3, !z6, !z6);
                    }
                } else {
                    int i9 = 0;
                    if (i6 == 31 || i6 == 33 || i6 == 30 || i6 == 18 || i6 == 25 || i6 == 35) {
                        this.imageReceiver.setRoundRadius(0);
                        TLRPC.MessageAction messageAction2 = messageObject2.messageOwner.action;
                        if (messageAction2 instanceof TLRPC.TL_messageActionNoForwardsRequest) {
                            TLRPC.TL_messageActionNoForwardsRequest tL_messageActionNoForwardsRequest = (TLRPC.TL_messageActionNoForwardsRequest) messageAction2;
                            this.offerExpired = tL_messageActionNoForwardsRequest.expired || ((long) messageObject2.messageOwner.date) + MessagesController.getInstance(this.currentAccount).config.noForwardsRequestExpirePeriod.get(TimeUnit.SECONDS) < ((long) ConnectionsManager.getInstance(this.currentAccount).getCurrentTime());
                            if (!messageObject2.isOut() && !tL_messageActionNoForwardsRequest.expired && !this.offerExpired) {
                                BotInlineKeyboard.Builder builder = new BotInlineKeyboard.Builder();
                                builder.addSharingOfferKeyboard();
                                this.botInlineButtons = builder.build();
                            }
                            num = num;
                            str2 = null;
                            document = null;
                        } else {
                            if (messageAction2 instanceof TLRPC.TL_messageActionStarGiftPurchaseOffer) {
                                TLRPC.TL_messageActionStarGiftPurchaseOffer tL_messageActionStarGiftPurchaseOffer = (TLRPC.TL_messageActionStarGiftPurchaseOffer) messageAction2;
                                TL_stars.StarGift starGift = tL_messageActionStarGiftPurchaseOffer.gift;
                                if (starGift != null) {
                                    giftDocument2 = TlUtils.getGiftDocument(starGift);
                                    if (this.cardBackground == null) {
                                        this.cardBackground = new GiftSheet.CardBackground(this, this.themeDelegate, false);
                                    }
                                    this.cardBackground.setBackdrop((TL_stars.starGiftAttributeBackdrop) StarsController.findAttribute(starGift.attributes, TL_stars.starGiftAttributeBackdrop.class));
                                    this.cardBackground.setPattern((TL_stars.starGiftAttributePattern) StarsController.findAttribute(starGift.attributes, TL_stars.starGiftAttributePattern.class));
                                } else {
                                    giftDocument2 = null;
                                }
                                this.offerExpired = tL_messageActionStarGiftPurchaseOffer.expires_at < ConnectionsManager.getInstance(this.currentAccount).getCurrentTime();
                                if (!messageObject2.isOut() && !tL_messageActionStarGiftPurchaseOffer.accepted && !tL_messageActionStarGiftPurchaseOffer.declined && !this.offerExpired) {
                                    BotInlineKeyboard.Builder builder2 = new BotInlineKeyboard.Builder();
                                    builder2.addGiftOfferKeyboard();
                                    this.botInlineButtons = builder2.build();
                                }
                                document = giftDocument2;
                            } else {
                                if (messageAction2 instanceof TLRPC.TL_messageActionSetChatTheme) {
                                    TL_stars.StarGift starGift2 = ((TLRPC.TL_chatThemeUniqueGift) ((TLRPC.TL_messageActionSetChatTheme) messageAction2).theme).gift;
                                    if (starGift2 != null) {
                                        giftDocument = TlUtils.getGiftDocument(starGift2);
                                        if (this.cardBackground == null) {
                                            this.cardBackground = new GiftSheet.CardBackground(this, this.themeDelegate, false);
                                        }
                                        this.cardBackground.setBackdrop((TL_stars.starGiftAttributeBackdrop) StarsController.findAttribute(starGift2.attributes, TL_stars.starGiftAttributeBackdrop.class));
                                        this.cardBackground.setPattern((TL_stars.starGiftAttributePattern) StarsController.findAttribute(starGift2.attributes, TL_stars.starGiftAttributePattern.class));
                                    } else {
                                        giftDocument = null;
                                    }
                                    document = giftDocument;
                                } else if (messageAction2 instanceof TLRPC.TL_messageActionStarGift) {
                                    TL_stars.StarGift starGift3 = ((TLRPC.TL_messageActionStarGift) messageAction2).gift;
                                    if (starGift3 != null) {
                                        document = starGift3.sticker;
                                    } else {
                                        document = null;
                                    }
                                    num = num;
                                    obj = messageObject2;
                                    str2 = null;
                                    tL_messages_stickerSet = null;
                                } else if ((messageAction2 instanceof TLRPC.TL_messageActionStarGiftUnique) && ((TLRPC.TL_messageActionStarGiftUnique) messageAction2).refunded) {
                                    TL_stars.StarGift starGift4 = ((TLRPC.TL_messageActionStarGiftUnique) messageAction2).gift;
                                    if (starGift4 != null) {
                                        document = starGift4.getDocument();
                                    } else {
                                        document = null;
                                    }
                                    num = num;
                                    obj = messageObject2;
                                    str2 = null;
                                    tL_messages_stickerSet = null;
                                } else {
                                    if (messageAction2 instanceof TLRPC.TL_messageActionGiftTon) {
                                        str = UserConfig.getInstance(this.currentAccount).premiumTonStickerPack;
                                        if (str == null) {
                                            MediaDataController.getInstance(this.currentAccount).checkTonGiftStickers();
                                            return;
                                        }
                                    } else {
                                        str = UserConfig.getInstance(this.currentAccount).premiumGiftsStickerPack;
                                        if (str == null) {
                                            MediaDataController.getInstance(this.currentAccount).checkPremiumGiftStickers();
                                            return;
                                        }
                                    }
                                    TLRPC.TL_messages_stickerSet stickerSetByName = MediaDataController.getInstance(this.currentAccount).getStickerSetByName(str);
                                    TLRPC.TL_messages_stickerSet tL_messages_stickerSet3 = stickerSetByName;
                                    if (stickerSetByName == null) {
                                        stickerSetByEmojiOrName = MediaDataController.getInstance(this.currentAccount).getStickerSetByEmojiOrName(str);
                                    }
                                    if (tL_messages_stickerSet3 != null) {
                                        TLRPC.MessageAction messageAction3 = messageObject2.messageOwner.action;
                                        int i10 = messageAction3.months;
                                        if (messageObject2.type == 30) {
                                            if (messageAction3 instanceof TLRPC.TL_messageActionGiftTon) {
                                                tL_messages_stickerSet3 = stickerSetByEmojiOrName;
                                                tonGiftEmoji = StarsIntroActivity.getTonGiftEmoji(messageAction3.cryptoAmount);
                                            } else {
                                                if (messageAction3 instanceof TLRPC.TL_messageActionGiftStars) {
                                                    tL_messages_stickerSet3 = stickerSetByEmojiOrName;
                                                    j = ((TLRPC.TL_messageActionGiftStars) messageAction3).stars;
                                                } else {
                                                    tL_messages_stickerSet3 = stickerSetByEmojiOrName;
                                                    j = ((TLRPC.TL_messageActionPrizeStars) messageAction3).stars;
                                                }
                                                if (j <= 1000) {
                                                    tonGiftEmoji = "2⃣";
                                                } else if (j < 2500) {
                                                    tonGiftEmoji = "3⃣";
                                                } else {
                                                    tonGiftEmoji = "4⃣";
                                                }
                                            }
                                            int i11 = 0;
                                            while (true) {
                                                if (i11 < tL_messages_stickerSet3.packs.size()) {
                                                    TLRPC.TL_stickerPack tL_stickerPack = (TLRPC.TL_stickerPack) tL_messages_stickerSet3.packs.get(i11);
                                                    if (!TextUtils.equals(tL_stickerPack.emoticon, tonGiftEmoji) || tL_stickerPack.documents.isEmpty()) {
                                                        i11++;
                                                    } else {
                                                        long jLongValue = ((Long) tL_stickerPack.documents.get(0)).longValue();
                                                        int i12 = 0;
                                                        while (true) {
                                                            if (i12 < tL_messages_stickerSet3.documents.size()) {
                                                                document2 = (TLRPC.Document) tL_messages_stickerSet3.documents.get(i12);
                                                                if (document2 != null && document2.id == jLongValue) {
                                                                    break;
                                                                } else {
                                                                    i12++;
                                                                }
                                                            }
                                                        }
                                                    }
                                                }
                                                document2 = null;
                                                break;
                                            }
                                        }
                                        String str5 = (String) monthsToEmoticon.get(Integer.valueOf(i10));
                                        ArrayList arrayList2 = tL_messages_stickerSet3.packs;
                                        int size3 = arrayList2.size();
                                        int i13 = 0;
                                        document2 = null;
                                        while (true) {
                                            if (i13 < size3) {
                                                Object obj2 = arrayList2.get(i13);
                                                i13++;
                                                TLRPC.TL_stickerPack tL_stickerPack2 = (TLRPC.TL_stickerPack) obj2;
                                                if (Objects.equals(tL_stickerPack2.emoticon, str5)) {
                                                    ArrayList arrayList3 = tL_stickerPack2.documents;
                                                    int size4 = arrayList3.size();
                                                    TLRPC.Document document4 = document2;
                                                    int i14 = i9;
                                                    while (true) {
                                                        if (i14 >= size4) {
                                                            tL_messages_stickerSet3 = stickerSetByEmojiOrName;
                                                            num = num;
                                                            str3 = str;
                                                            break;
                                                        }
                                                        Object obj3 = arrayList3.get(i14);
                                                        i14++;
                                                        long jLongValue2 = ((Long) obj3).longValue();
                                                        ArrayList arrayList4 = tL_messages_stickerSet3.documents;
                                                        int size5 = arrayList4.size();
                                                        int i15 = i9;
                                                        while (true) {
                                                            if (i15 >= size5) {
                                                                tL_messages_stickerSet3 = stickerSetByEmojiOrName;
                                                                num = num;
                                                                str3 = str;
                                                                break;
                                                            }
                                                            Object obj4 = arrayList4.get(i15);
                                                            i15++;
                                                            TLRPC.Document document5 = (TLRPC.Document) obj4;
                                                            num = num;
                                                            str3 = str;
                                                            if (document5.id == jLongValue2) {
                                                                tL_messages_stickerSet3 = stickerSetByEmojiOrName;
                                                                document4 = document5;
                                                                break;
                                                            } else {
                                                                tL_messages_stickerSet3 = stickerSetByEmojiOrName;
                                                                num = num;
                                                                str = str3;
                                                            }
                                                        }
                                                        if (document4 != null) {
                                                            break;
                                                        }
                                                        num = num;
                                                        str = str3;
                                                        i9 = 0;
                                                    }
                                                    document2 = document4;
                                                } else {
                                                    num = num;
                                                    str3 = str;
                                                }
                                                if (document2 != null) {
                                                    tL_messages_stickerSet3 = stickerSetByEmojiOrName;
                                                    break;
                                                }
                                                tL_messages_stickerSet3 = stickerSetByEmojiOrName;
                                                num = num;
                                                str = str3;
                                                i9 = 0;
                                            } else {
                                                tL_messages_stickerSet3 = stickerSetByEmojiOrName;
                                            }
                                        }
                                        if (document2 == null || tL_messages_stickerSet3.documents.isEmpty()) {
                                            document = document2;
                                        } else {
                                            document = (TLRPC.Document) tL_messages_stickerSet3.documents.get(0);
                                        }
                                        str2 = str3;
                                        tL_messages_stickerSet = tL_messages_stickerSet3;
                                        obj = tL_messages_stickerSet3;
                                        num = num;
                                        str3 = str;
                                        if (document2 == null) {
                                            document = document2;
                                        } else {
                                            document = document2;
                                        }
                                        str2 = str3;
                                        tL_messages_stickerSet = tL_messages_stickerSet3;
                                        obj = tL_messages_stickerSet3;
                                    } else {
                                        tL_messages_stickerSet3 = stickerSetByEmojiOrName;
                                        num = num;
                                        str2 = str;
                                        document = null;
                                        tL_messages_stickerSet2 = tL_messages_stickerSet3;
                                        obj = null;
                                        tL_messages_stickerSet = tL_messages_stickerSet2;
                                    }
                                }
                                source = this.botInlineButtons;
                                if (source != null) {
                                    rowsCount = source.getRowsCount();
                                    for (i3 = 0; i3 < rowsCount; i3++) {
                                        columnsCount = this.botInlineButtons.getColumnsCount(i3);
                                        for (i4 = 0; i4 < columnsCount; i4++) {
                                            BotInlineKeyboard.Button button = this.botInlineButtons.getButton(i3, i4);
                                            botButton = new BotButton(new Runnable() { // from class: org.telegram.ui.Cells.ChatActionCell$$ExternalSyntheticLambda2
                                                @Override // java.lang.Runnable
                                                public final void run() {
                                                    this.f$0.invalidateOutbounds();
                                                }
                                            });
                                            botButton.buttonCustom = (BotInlineKeyboard.ButtonCustom) button;
                                            iconRes = button.getIconRes();
                                            if (iconRes != 0) {
                                                Drawable drawable = getResources().getDrawable(iconRes);
                                                botButton.iconDrawable = drawable;
                                                drawable.setColorFilter(new PorterDuffColorFilter(-1, PorterDuff.Mode.SRC_IN));
                                            }
                                            botButton.height = AndroidUtilities.dp(40.0f);
                                            int i16 = botButton.positionFlags | 8;
                                            botButton.positionFlags = i16;
                                            if (i4 == 0) {
                                                z3 = true;
                                            } else {
                                                z3 = false;
                                            }
                                            int flag = BitwiseUtils.setFlag(i16, 1, z3);
                                            botButton.positionFlags = flag;
                                            if (i4 == 1) {
                                                z4 = true;
                                            } else {
                                                z4 = false;
                                            }
                                            botButton.positionFlags = BitwiseUtils.setFlag(flag, 2, z4);
                                            botButton.title = new Text(button.getText(), (TextPaint) getThemedPaint("paintChatBotButton"));
                                            this.botButtons.add(botButton);
                                        }
                                    }
                                }
                                this.forceWasUnread = messageObject2.wasUnread;
                                this.giftSticker = document;
                                if (document != null) {
                                    this.imageReceiver.setAllowStartLottieAnimation(true);
                                    i = messageObject2.type;
                                    if (i != 31 && i != 33) {
                                        this.imageReceiver.setDelegate(this.giftStickerDelegate);
                                    }
                                    this.giftEffectAnimation = null;
                                    for (i2 = 0; i2 < document.video_thumbs.size(); i2++) {
                                        if ("f".equals(document.video_thumbs.get(i2).type)) {
                                            this.giftEffectAnimation = document.video_thumbs.get(i2);
                                            break;
                                        }
                                    }
                                    if (!z6 || messageObject2.type != 18) {
                                        SvgHelper.SvgDrawable svgThumb = DocumentObject.getSvgThumb(document, Theme.key_windowBackgroundGray, 0.3f);
                                        this.imageReceiver.setAutoRepeat(0);
                                        this.imageReceiver.setImage(ImageLocation.getForDocument(document), String.format(Locale.US, "%d_%d_nr_messageId=%d", num, num, Integer.valueOf(messageObject2.stableId)), svgThumb, "tgs", obj, 1);
                                    }
                                } else if (str2 != null) {
                                    MediaDataController mediaDataController = MediaDataController.getInstance(this.currentAccount);
                                    if (tL_messages_stickerSet == null) {
                                        z2 = true;
                                    } else {
                                        z2 = false;
                                    }
                                    mediaDataController.loadStickersByEmojiOrName(str2, false, z2);
                                }
                            }
                            str2 = null;
                        }
                        tL_messages_stickerSet2 = null;
                        obj = null;
                        tL_messages_stickerSet = tL_messages_stickerSet2;
                        source = this.botInlineButtons;
                        if (source != null) {
                            rowsCount = source.getRowsCount();
                            while (i3 < rowsCount) {
                                columnsCount = this.botInlineButtons.getColumnsCount(i3);
                                while (i4 < columnsCount) {
                                    BotInlineKeyboard.Button button2 = this.botInlineButtons.getButton(i3, i4);
                                    botButton = new BotButton(new Runnable() { // from class: org.telegram.ui.Cells.ChatActionCell$$ExternalSyntheticLambda2
                                        @Override // java.lang.Runnable
                                        public final void run() {
                                            this.f$0.invalidateOutbounds();
                                        }
                                    });
                                    botButton.buttonCustom = (BotInlineKeyboard.ButtonCustom) button2;
                                    iconRes = button2.getIconRes();
                                    if (iconRes != 0) {
                                        Drawable drawable2 = getResources().getDrawable(iconRes);
                                        botButton.iconDrawable = drawable2;
                                        drawable2.setColorFilter(new PorterDuffColorFilter(-1, PorterDuff.Mode.SRC_IN));
                                    }
                                    botButton.height = AndroidUtilities.dp(40.0f);
                                    int i17 = botButton.positionFlags | 8;
                                    botButton.positionFlags = i17;
                                    if (i4 == 0) {
                                        z3 = true;
                                    } else {
                                        z3 = false;
                                    }
                                    int flag2 = BitwiseUtils.setFlag(i17, 1, z3);
                                    botButton.positionFlags = flag2;
                                    if (i4 == 1) {
                                        z4 = true;
                                    } else {
                                        z4 = false;
                                    }
                                    botButton.positionFlags = BitwiseUtils.setFlag(flag2, 2, z4);
                                    botButton.title = new Text(button2.getText(), (TextPaint) getThemedPaint("paintChatBotButton"));
                                    this.botButtons.add(botButton);
                                }
                            }
                        }
                        this.forceWasUnread = messageObject2.wasUnread;
                        this.giftSticker = document;
                        if (document != null) {
                            this.imageReceiver.setAllowStartLottieAnimation(true);
                            i = messageObject2.type;
                            if (i != 31) {
                                this.imageReceiver.setDelegate(this.giftStickerDelegate);
                            }
                            this.giftEffectAnimation = null;
                            while (i2 < document.video_thumbs.size()) {
                                if ("f".equals(document.video_thumbs.get(i2).type)) {
                                    this.giftEffectAnimation = document.video_thumbs.get(i2);
                                    break;
                                }
                            }
                            if (!z6) {
                                SvgHelper.SvgDrawable svgThumb2 = DocumentObject.getSvgThumb(document, Theme.key_windowBackgroundGray, 0.3f);
                                this.imageReceiver.setAutoRepeat(0);
                                this.imageReceiver.setImage(ImageLocation.getForDocument(document), String.format(Locale.US, "%d_%d_nr_messageId=%d", num, num, Integer.valueOf(messageObject2.stableId)), svgThumb2, "tgs", obj, 1);
                            } else {
                                SvgHelper.SvgDrawable svgThumb3 = DocumentObject.getSvgThumb(document, Theme.key_windowBackgroundGray, 0.3f);
                                this.imageReceiver.setAutoRepeat(0);
                                this.imageReceiver.setImage(ImageLocation.getForDocument(document), String.format(Locale.US, "%d_%d_nr_messageId=%d", num, num, Integer.valueOf(messageObject2.stableId)), svgThumb3, "tgs", obj, 1);
                            }
                        } else if (str2 != null) {
                            MediaDataController mediaDataController2 = MediaDataController.getInstance(this.currentAccount);
                            if (tL_messages_stickerSet == null) {
                                z2 = true;
                            } else {
                                z2 = false;
                            }
                            mediaDataController2.loadStickersByEmojiOrName(str2, false, z2);
                        }
                    } else if (i6 == 11) {
                        this.imageReceiver.setAllowStartLottieAnimation(true);
                        this.imageReceiver.setDelegate(null);
                        this.imageReceiver.setRoundRadius(ExteraConfig.getAvatarCorners(AndroidUtilities.roundMessageSize, true));
                        this.imageReceiver.setAutoRepeatCount(1);
                        this.avatarDrawable.setInfo(messageObject2.getDialogId(), null, null);
                        if (messageObject2.messageOwner.action instanceof TLRPC.TL_messageActionUserUpdatedPhoto) {
                            this.imageReceiver.setImage(null, null, this.avatarDrawable, null, messageObject2, 0);
                        } else {
                            if (messageObject2.strippedThumb != null) {
                                photoSize = null;
                                break;
                            }
                            int size6 = messageObject2.photoThumbs.size();
                            int i18 = 0;
                            while (true) {
                                if (i18 >= size6) {
                                    photoSize = null;
                                    break;
                                }
                                photoSize = messageObject2.photoThumbs.get(i18);
                                if (photoSize instanceof TLRPC.TL_photoStrippedSize) {
                                    break;
                                } else {
                                    i18++;
                                }
                            }
                            TLRPC.PhotoSize closestPhotoSizeWithSize2 = FileLoader.getClosestPhotoSizeWithSize(messageObject2.photoThumbs, 640);
                            if (closestPhotoSizeWithSize2 != null) {
                                TLRPC.Photo photo2 = messageObject2.messageOwner.action.photo;
                                if (photo2.video_sizes.isEmpty() || !SharedConfig.isAutoplayGifs()) {
                                    closestVideoSizeWithSize = null;
                                } else {
                                    closestVideoSizeWithSize = FileLoader.getClosestVideoSizeWithSize(photo2.video_sizes, MediaDataController.MAX_STYLE_RUNS_COUNT);
                                    if (!messageObject2.mediaExists && !DownloadController.getInstance(this.currentAccount).canDownloadMedia(4, closestVideoSizeWithSize.size)) {
                                        this.currentVideoLocation = ImageLocation.getForPhoto(closestVideoSizeWithSize, photo2);
                                        DownloadController.getInstance(this.currentAccount).addLoadingFileObserver(FileLoader.getAttachFileName(closestVideoSizeWithSize), messageObject2, this);
                                        closestVideoSizeWithSize = null;
                                    }
                                }
                                if (closestVideoSizeWithSize != null) {
                                    this.imageReceiver.setImage(ImageLocation.getForPhoto(closestVideoSizeWithSize, photo2), ImageLoader.AUTOPLAY_FILTER, ImageLocation.getForObject(photoSize, messageObject2.photoThumbsObject), "50_50_b", messageObject2.strippedThumb, 0L, null, messageObject2, 1);
                                } else {
                                    this.imageReceiver.setImage(ImageLocation.getForObject(closestPhotoSizeWithSize2, messageObject2.photoThumbsObject), "150_150", ImageLocation.getForObject(photoSize, messageObject2.photoThumbsObject), "50_50_b", messageObject2.strippedThumb, 0L, null, messageObject2, 1);
                                }
                            } else {
                                this.imageReceiver.setImageBitmap(this.avatarDrawable);
                            }
                        }
                        this.imageReceiver.setVisible(!PhotoViewer.isShowingImage(messageObject2), false);
                    } else {
                        this.imageReceiver.setAllowStartLottieAnimation(true);
                        this.imageReceiver.setDelegate(null);
                        this.imageReceiver.setImageBitmap((Bitmap) null);
                    }
                }
            }
            if (this.firstInChat && this.isAllChats && this.isSideMenued && (this.isForum || this.isMonoForum)) {
                this.topicSeparatorTopPadding = AndroidUtilities.dp(33.0f);
                if (this.topicSeparator == null) {
                    TopicSeparator topicSeparator = new TopicSeparator(this.currentAccount, this, this.themeDelegate, true);
                    this.topicSeparator = topicSeparator;
                    topicSeparator.setOnClickListener(new Runnable() { // from class: org.telegram.ui.Cells.ChatActionCell$$ExternalSyntheticLambda3
                        @Override // java.lang.Runnable
                        public final void run() {
                            this.f$0.lambda$setMessageObject$1();
                        }
                    });
                }
                if (!this.topicSeparator.update(this.currentMessageObject)) {
                    this.topicSeparator.detach();
                    this.topicSeparator = null;
                    i5 = 0;
                    this.topicSeparatorTopPadding = 0;
                } else {
                    if (this.attachedToWindow) {
                        this.topicSeparator.attach();
                    }
                    i5 = 0;
                }
            } else {
                TopicSeparator topicSeparator2 = this.topicSeparator;
                if (topicSeparator2 != null) {
                    topicSeparator2.detach();
                    this.topicSeparator = null;
                }
                i5 = 0;
                this.topicSeparatorTopPadding = 0;
            }
            int paddingTop = getPaddingTop();
            int i19 = this.topicSeparatorTopPadding;
            if (paddingTop != i19) {
                setPadding(i5, i19, i5, i5);
            }
            this.rippleView.setVisibility((!isButtonLayout(messageObject) || this.starGiftLayout.has()) ? 8 : i5);
            ForumUtilities.applyTopicToMessage(messageObject2);
            requestLayout();
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$setMessageObject$1() {
        ChatActionCellDelegate chatActionCellDelegate = this.delegate;
        if (chatActionCellDelegate != null) {
            chatActionCellDelegate.onTopicClick(this);
        }
    }

    private float getUploadingInfoProgress(MessageObject messageObject) {
        MessagesController messagesController;
        String str;
        if (messageObject == null) {
            return 1.0f;
        }
        try {
            if (messageObject.type == 22 && (str = (messagesController = MessagesController.getInstance(this.currentAccount)).uploadingWallpaper) != null && TextUtils.equals(messageObject.messageOwner.action.wallpaper.uploadingImage, str)) {
                return messagesController.uploadingWallpaperInfo.uploadingProgress;
            }
            return 1.0f;
        } catch (Exception e) {
            FileLog.e(e);
            return 1.0f;
        }
    }

    public MessageObject getMessageObject() {
        return this.currentMessageObject;
    }

    public ImageReceiver getPhotoImage() {
        return this.imageReceiver;
    }

    public void setVisiblePart(float f, int i) {
        this.visiblePartSet = true;
        this.backgroundHeight = i;
        this.viewTop = f;
        this.viewTranslationX = 0.0f;
    }

    public void setVisiblePart(float f, float f2, int i, float f3) {
        this.visiblePartSet = true;
        this.backgroundHeight = i;
        this.viewTop = f;
        this.viewTranslationX = f2;
        this.dimAmount = f3;
        this.dimPaint.setColor(ColorUtils.setAlphaComponent(-16777216, (int) (f3 * 255.0f)));
        invalidate();
    }

    @Override // org.telegram.ui.Cells.BaseCell
    protected boolean onLongPress() {
        View.OnLongClickListener onLongClickListener;
        if (this.actionPressed && (onLongClickListener = this.onActionLongClick) != null && onLongClickListener.onLongClick(this)) {
            this.actionPressed = false;
            return true;
        }
        ChatActionCellDelegate chatActionCellDelegate = this.delegate;
        if (chatActionCellDelegate != null) {
            return chatActionCellDelegate.didLongPress(this, this.lastTouchX, this.lastTouchY);
        }
        return false;
    }

    @Override // android.view.ViewGroup, android.view.View
    protected void onLayout(boolean z, int i, int i2, int i3, int i4) {
        View view = this.rippleView;
        RectF rectF = this.giftButtonRect;
        view.layout((int) rectF.left, (int) rectF.top, (int) rectF.right, (int) rectF.bottom);
    }

    @Override // android.view.ViewGroup, android.view.View
    protected void onDetachedFromWindow() {
        super.onDetachedFromWindow();
        this.attachedToWindow = false;
        DownloadController.getInstance(this.currentAccount).removeLoadingFileObserver(this);
        this.imageReceiver.onDetachedFromWindow();
        setStarsPaused(true);
        this.wasLayout = false;
        AnimatedEmojiSpan.release(this, this.animatedEmojiStack);
        TextLayout textLayout = this.giftPremiumText;
        if (textLayout != null) {
            textLayout.detach();
        }
        NotificationCenter.getInstance(this.currentAccount).removeObserver(this, NotificationCenter.didUpdatePremiumGiftStickers);
        NotificationCenter.getInstance(this.currentAccount).removeObserver(this, NotificationCenter.didUpdateTonGiftStickers);
        NotificationCenter.getInstance(this.currentAccount).removeObserver(this, NotificationCenter.starGiftsLoaded);
        NotificationCenter.getInstance(this.currentAccount).removeObserver(this, NotificationCenter.diceStickersDidLoad);
        this.avatarStoryParams.onDetachFromWindow();
        this.transitionParams.onDetach();
        this.starGiftLayout.detach();
        this.reactionsLayoutInBubble.onDetachFromWindow();
        TopicSeparator topicSeparator = this.topicSeparator;
        if (topicSeparator != null) {
            topicSeparator.detach();
        }
        SuggestBirthdayActionLayout suggestBirthdayActionLayout = this.birthdayLayout;
        if (suggestBirthdayActionLayout != null) {
            suggestBirthdayActionLayout.detach();
        }
    }

    public boolean isCellAttachedToWindow() {
        return this.attachedToWindow;
    }

    @Override // android.view.ViewGroup, android.view.View
    protected void onAttachedToWindow() {
        ChatActionCellDelegate chatActionCellDelegate;
        super.onAttachedToWindow();
        this.attachedToWindow = true;
        this.imageReceiver.onAttachedToWindow();
        setStarsPaused(false);
        this.animatedEmojiStack = AnimatedEmojiSpan.update(0, this, (!this.canDrawInParent || (chatActionCellDelegate = this.delegate) == null || chatActionCellDelegate.canDrawOutboundsContent()) ? false : true, this.animatedEmojiStack, this.textLayout);
        TextLayout textLayout = this.giftPremiumText;
        if (textLayout != null) {
            textLayout.attach();
        }
        NotificationCenter.getInstance(this.currentAccount).addObserver(this, NotificationCenter.didUpdatePremiumGiftStickers);
        NotificationCenter.getInstance(this.currentAccount).addObserver(this, NotificationCenter.didUpdateTonGiftStickers);
        NotificationCenter.getInstance(this.currentAccount).addObserver(this, NotificationCenter.starGiftsLoaded);
        NotificationCenter.getInstance(this.currentAccount).addObserver(this, NotificationCenter.diceStickersDidLoad);
        MessageObject messageObject = this.currentMessageObject;
        if (messageObject != null && messageObject.type == 21) {
            setMessageObject(messageObject, true);
        }
        this.starGiftLayout.attach();
        this.reactionsLayoutInBubble.onAttachToWindow();
        TopicSeparator topicSeparator = this.topicSeparator;
        if (topicSeparator != null) {
            topicSeparator.attach();
        }
        SuggestBirthdayActionLayout suggestBirthdayActionLayout = this.birthdayLayout;
        if (suggestBirthdayActionLayout != null) {
            suggestBirthdayActionLayout.attach();
        }
    }

    private void setStarsPaused(boolean z) {
        StarParticlesView.Drawable drawable = this.starParticlesDrawable;
        if (z == drawable.paused) {
            return;
        }
        drawable.paused = z;
        if (z) {
            drawable.pausedTime = System.currentTimeMillis();
            return;
        }
        for (int i = 0; i < this.starParticlesDrawable.particles.size(); i++) {
            ((StarParticlesView.Drawable.Particle) this.starParticlesDrawable.particles.get(i)).lifeTime += System.currentTimeMillis() - this.starParticlesDrawable.pausedTime;
        }
        invalidate();
    }

    /* JADX WARN: Code duplicated, block: B:210:0x0362  */
    /* JADX WARN: Code duplicated, block: B:212:0x0376  */
    /* JADX WARN: Code duplicated, block: B:214:0x037a  */
    /* JADX WARN: Code duplicated, block: B:215:0x0381  */
    /* JADX WARN: Code duplicated, block: B:320:0x055f  */
    /* JADX WARN: Code duplicated, block: B:321:0x0563  */
    @Override // android.view.View
    public boolean onTouchEvent(MotionEvent motionEvent) {
        ImageUpdater imageUpdater;
        TextLayout textLayout;
        TextLayout textLayout2;
        TLRPC.Message message;
        boolean zCheckBotButtonMotionEvent;
        StaticLayout staticLayout;
        List list;
        char c;
        TLRPC.Message message2;
        int i;
        MessageObject messageObject = this.currentMessageObject;
        float x = motionEvent.getX() - (this.sideMenuWidth / 2.0f);
        this.lastTouchX = x;
        float y = motionEvent.getY() + getPaddingTop();
        this.lastTouchY = y;
        boolean z = true;
        boolean z2 = false;
        if (messageObject == null) {
            if (this.onActionClick != null) {
                if (motionEvent.getAction() == 0) {
                    if (x >= this.backgroundLeft && x <= this.backgroundRight) {
                        this.actionPressed = true;
                        startCheckLongPress();
                        z2 = true;
                    }
                } else if (this.actionPressed) {
                    if (motionEvent.getAction() == 1) {
                        this.onActionClick.onClick(this);
                        this.actionPressed = false;
                    } else if (motionEvent.getAction() == 3) {
                        this.actionPressed = false;
                    } else if (motionEvent.getAction() == 2 && (x < this.backgroundLeft || x > this.backgroundRight)) {
                        this.actionPressed = false;
                        cancelCheckLongPress();
                    }
                }
            }
            if (motionEvent.getAction() != 0 && motionEvent.getAction() != 2) {
                cancelCheckLongPress();
            }
            if (z2) {
                return true;
            }
            return super.onTouchEvent(motionEvent);
        }
        TopicSeparator topicSeparator = this.topicSeparator;
        if (topicSeparator != null && topicSeparator.onTouchEvent(motionEvent, false)) {
            return true;
        }
        SuggestBirthdayActionLayout suggestBirthdayActionLayout = this.birthdayLayout;
        if (suggestBirthdayActionLayout != null && suggestBirthdayActionLayout.onTouchEvent(motionEvent)) {
            return true;
        }
        if ((this.starGiftLayout.has() && this.starGiftLayout.onTouchEvent(this.starGiftLayoutX, this.starGiftLayoutY, motionEvent)) || this.reactionsLayoutInBubble.checkTouchEvent(motionEvent)) {
            return true;
        }
        if (motionEvent.getAction() == 0) {
            if (this.delegate != null) {
                if ((messageObject.type == 11 || isButtonLayout(messageObject)) && this.imageReceiver.isInsideImage(x, y)) {
                    this.imagePressed = true;
                    zCheckBotButtonMotionEvent = true;
                } else {
                    zCheckBotButtonMotionEvent = false;
                }
                if (this.radialProgress.getIcon() == 4 && (((i = messageObject.type) == 21 || i == 22) && this.backgroundRect.contains(x, y))) {
                    this.imagePressed = true;
                    zCheckBotButtonMotionEvent = true;
                }
                TextLayout textLayout3 = this.giftPremiumText;
                if (textLayout3 == null || !this.giftPremiumTextCollapsed) {
                    c = 0;
                } else {
                    RectF rectF = AndroidUtilities.rectTmp;
                    float f = textLayout3.x;
                    float f2 = textLayout3.y;
                    float width = textLayout3.layout.getWidth() + f;
                    TextLayout textLayout4 = this.giftPremiumText;
                    c = 0;
                    rectF.set(f, f2, width, textLayout4.y + textLayout4.layout.getHeight());
                    if (rectF.contains(x, y)) {
                        this.textPressed = true;
                        zCheckBotButtonMotionEvent = true;
                    }
                }
                if (isButtonLayout(messageObject) && this.giftPremiumButtonLayout != null && (this.giftButtonRect.contains(x, y) || (this.buttonClickableAsImage && this.backgroundRect.contains(x, y)))) {
                    View view = this.rippleView;
                    this.giftButtonPressed = true;
                    view.setPressed(true);
                    this.bounce.setPressed(true);
                    zCheckBotButtonMotionEvent = true;
                }
                if (!zCheckBotButtonMotionEvent && isMessageActionSuggestedPostApproval()) {
                    this.textPressed = true;
                    zCheckBotButtonMotionEvent = true;
                }
                if (!zCheckBotButtonMotionEvent) {
                    MessageObject messageObject2 = this.currentMessageObject;
                    TLRPC.MessageAction messageAction = (messageObject2 == null || (message2 = messageObject2.messageOwner) == null) ? null : message2.action;
                    Class[] clsArr = new Class[2];
                    clsArr[c] = TLRPC.TL_messageActionSuggestedPostRefund.class;
                    clsArr[1] = TLRPC.TL_messageActionSuggestedPostSuccess.class;
                    if (TlUtils.isInstance(messageAction, clsArr)) {
                        this.textPressed = true;
                        zCheckBotButtonMotionEvent = true;
                    }
                }
                if (zCheckBotButtonMotionEvent) {
                    startCheckLongPress();
                }
            } else {
                zCheckBotButtonMotionEvent = false;
            }
        } else {
            if (motionEvent.getAction() != 2) {
                cancelCheckLongPress();
            }
            if (this.actionPressed) {
                if (motionEvent.getAction() == 2) {
                    if (x < this.backgroundLeft || x > this.backgroundRight) {
                        this.actionPressed = false;
                        zCheckBotButtonMotionEvent = false;
                    }
                } else if (motionEvent.getAction() == 1) {
                    View.OnClickListener onClickListener = this.onActionClick;
                    if (onClickListener != null) {
                        onClickListener.onClick(this);
                    }
                    this.actionPressed = false;
                } else if (motionEvent.getAction() == 3) {
                    this.actionPressed = false;
                }
                zCheckBotButtonMotionEvent = false;
            } else {
                if (this.textPressed) {
                    int action = motionEvent.getAction();
                    if (action == 1) {
                        View view2 = this.rippleView;
                        this.textPressed = false;
                        view2.setPressed(false);
                        this.bounce.setPressed(false);
                        if (this.delegate != null && messageObject.replyMessageObject != null && (message = messageObject.messageOwner) != null && TlUtils.isInstance(message.action, TLRPC.TL_messageActionTodoAppendTasks.class, TLRPC.TL_messageActionTodoCompletions.class, TLRPC.TL_messageActionSuggestedPostApproval.class, TLRPC.TL_messageActionSuggestedPostRefund.class, TLRPC.TL_messageActionSuggestedPostSuccess.class)) {
                            this.delegate.didPressReplyMessage(this, this.currentMessageObject.getReplyMsgId());
                        } else {
                            if (this.giftPremiumTextCollapsed && !this.giftPremiumTextUncollapsed && (textLayout2 = this.giftPremiumText) != null) {
                                int height = textLayout2.layout.getHeight() - this.giftPremiumTextCollapsedHeight;
                                this.giftPremiumTextUncollapsed = true;
                                ChatActionCellDelegate chatActionCellDelegate = this.delegate;
                                if (chatActionCellDelegate != null) {
                                    chatActionCellDelegate.forceUpdate(this, false);
                                    if (getParent() instanceof RecyclerListView) {
                                        ((RecyclerListView) getParent()).smoothScrollBy(0, height + AndroidUtilities.dp(24.0f));
                                    }
                                }
                                return true;
                            }
                            if (this.birthdayLayout != null && this.backgroundRect.contains(motionEvent.getX(), motionEvent.getY())) {
                                this.birthdayLayout.open();
                                return true;
                            }
                        }
                    } else if (action == 2) {
                        TextLayout textLayout5 = this.giftPremiumText;
                        if (textLayout5 == null || !this.giftPremiumTextCollapsed) {
                            this.textPressed = false;
                        } else {
                            RectF rectF2 = AndroidUtilities.rectTmp;
                            float f3 = textLayout5.x;
                            float f4 = textLayout5.y;
                            float width2 = textLayout5.layout.getWidth() + f3;
                            TextLayout textLayout6 = this.giftPremiumText;
                            rectF2.set(f3, f4, width2, textLayout6.y + textLayout6.layout.getHeight());
                            if (!rectF2.contains(x, y)) {
                                this.textPressed = false;
                            }
                        }
                        zCheckBotButtonMotionEvent = true;
                    } else if (action == 3) {
                        this.textPressed = false;
                        this.bounce.setPressed(false);
                    }
                } else if (this.giftButtonPressed) {
                    int action2 = motionEvent.getAction();
                    if (action2 == 1) {
                        this.imagePressed = false;
                        View view3 = this.rippleView;
                        this.giftButtonPressed = false;
                        view3.setPressed(false);
                        this.bounce.setPressed(false);
                        if (this.delegate != null) {
                            int i2 = messageObject.type;
                            if (i2 == 31) {
                                playSoundEffect(0);
                                openStarsGiftTransaction();
                            } else if (i2 == 25) {
                                playSoundEffect(0);
                                openPremiumGiftChannel();
                            } else if (i2 == 18) {
                                playSoundEffect(0);
                                openPremiumGiftPreview();
                            } else if (i2 == 30) {
                                playSoundEffect(0);
                                openStarsGiftTransaction();
                            } else {
                                TLRPC.Message message3 = messageObject.messageOwner;
                                if (message3 != null) {
                                    TLRPC.MessageAction messageAction2 = message3.action;
                                    if ((messageAction2 instanceof TLRPC.TL_messageActionSuggestedPostApproval) && ((TLRPC.TL_messageActionSuggestedPostApproval) messageAction2).balance_too_low) {
                                        playSoundEffect(0);
                                        openStarsNeedSheet();
                                    } else if (MessagesController.getInstance(this.currentAccount).photoSuggestion.get(messageObject.messageOwner.local_id) == null) {
                                        if (this.buttonClickableAsImage) {
                                            this.delegate.didClickImage(this);
                                        } else {
                                            this.delegate.didClickButton(this);
                                        }
                                    }
                                } else if (MessagesController.getInstance(this.currentAccount).photoSuggestion.get(messageObject.messageOwner.local_id) == null) {
                                    if (this.buttonClickableAsImage) {
                                        this.delegate.didClickImage(this);
                                    } else {
                                        this.delegate.didClickButton(this);
                                    }
                                }
                            }
                        }
                    } else if (action2 != 2) {
                        if (action2 == 3) {
                            this.imagePressed = false;
                            View view4 = this.rippleView;
                            this.giftButtonPressed = false;
                            view4.setPressed(false);
                            this.bounce.setPressed(false);
                        }
                    } else if (!isButtonLayout(messageObject) || (!this.giftButtonRect.contains(x, y) && !this.backgroundRect.contains(x, y))) {
                        View view5 = this.rippleView;
                        this.giftButtonPressed = false;
                        view5.setPressed(false);
                        this.bounce.setPressed(false);
                    }
                } else if (this.imagePressed) {
                    int action3 = motionEvent.getAction();
                    if (action3 == 1) {
                        this.imagePressed = false;
                        if (this.giftPremiumTextCollapsed && !this.giftPremiumTextUncollapsed && (textLayout = this.giftPremiumText) != null) {
                            int height2 = textLayout.layout.getHeight() - this.giftPremiumTextCollapsedHeight;
                            this.giftPremiumTextUncollapsed = true;
                            ChatActionCellDelegate chatActionCellDelegate2 = this.delegate;
                            if (chatActionCellDelegate2 != null) {
                                chatActionCellDelegate2.forceUpdate(this, false);
                                if (getParent() instanceof RecyclerListView) {
                                    ((RecyclerListView) getParent()).smoothScrollBy(0, height2 + AndroidUtilities.dp(16.0f));
                                }
                            }
                            return true;
                        }
                        int i3 = messageObject.type;
                        if (i3 == 31) {
                            openStarsGiftTransaction();
                        } else if (i3 == 25) {
                            openPremiumGiftChannel();
                        } else if (i3 == 18) {
                            openPremiumGiftPreview();
                        } else if (i3 == 30) {
                            openStarsGiftTransaction();
                        } else if (this.delegate != null) {
                            if (i3 == 21 && (imageUpdater = MessagesController.getInstance(this.currentAccount).photoSuggestion.get(messageObject.messageOwner.local_id)) != null) {
                                imageUpdater.cancel();
                            } else {
                                this.delegate.didClickImage(this);
                                playSoundEffect(0);
                            }
                        }
                    } else if (action3 != 2) {
                        if (action3 == 3) {
                            this.imagePressed = false;
                        }
                    } else if (isNewStyleButtonLayout()) {
                        if (!this.backgroundRect.contains(x, y)) {
                            this.imagePressed = false;
                        }
                    } else if (!this.imageReceiver.isInsideImage(x, y)) {
                        this.imagePressed = false;
                    }
                }
                zCheckBotButtonMotionEvent = false;
            }
        }
        if (!zCheckBotButtonMotionEvent && (motionEvent.getAction() == 0 || ((this.pressedLink != null || this.spoilerPressed != null) && motionEvent.getAction() == 1))) {
            TextLayout textLayout7 = this.giftPremiumText;
            if (textLayout7 != null && (list = textLayout7.spoilers) != null && !list.isEmpty() && !this.isSpoilerRevealing) {
                for (SpoilerEffect spoilerEffect : this.giftPremiumText.spoilers) {
                    Rect bounds = spoilerEffect.getBounds();
                    TextLayout textLayout8 = this.giftPremiumText;
                    if (bounds.contains((int) (x - textLayout8.x), (int) (y - textLayout8.y))) {
                        this.pressedLink = null;
                        if (motionEvent.getAction() == 0) {
                            this.spoilerPressed = spoilerEffect;
                        } else {
                            SpoilerEffect spoilerEffect2 = this.spoilerPressed;
                            if (spoilerEffect == spoilerEffect2) {
                                this.isSpoilerRevealing = true;
                                spoilerEffect2.setOnRippleEndCallback(new Runnable() { // from class: org.telegram.ui.Cells.ChatActionCell$$ExternalSyntheticLambda4
                                    @Override // java.lang.Runnable
                                    public final void run() {
                                        this.f$0.lambda$onTouchEvent$3();
                                    }
                                });
                                float fSqrt = (float) Math.sqrt(Math.pow(this.giftPremiumText.layout.getWidth(), 2.0d) + Math.pow(this.giftPremiumText.layout.getHeight(), 2.0d));
                                SpoilerEffect spoilerEffect3 = this.spoilerPressed;
                                TextLayout textLayout9 = this.giftPremiumText;
                                spoilerEffect3.startRipple((int) (x - textLayout9.x), (int) (y - textLayout9.y), fSqrt);
                                invalidate();
                            }
                        }
                        zCheckBotButtonMotionEvent = true;
                        break;
                    }
                }
            }
            if (zCheckBotButtonMotionEvent || (staticLayout = this.textLayout) == null) {
                this.pressedLink = null;
            } else {
                int i4 = this.textX;
                if (x >= i4) {
                    int i5 = this.textY;
                    if (y >= i5 && x <= i4 + this.textWidth && y <= this.textHeight + i5) {
                        float f5 = y - i5;
                        float f6 = x - this.textXLeft;
                        if (!zCheckBotButtonMotionEvent) {
                            int lineForVertical = staticLayout.getLineForVertical((int) f5);
                            int offsetForHorizontal = this.textLayout.getOffsetForHorizontal(lineForVertical, f6);
                            float lineLeft = this.textLayout.getLineLeft(lineForVertical);
                            if (lineLeft > f6 || lineLeft + this.textLayout.getLineWidth(lineForVertical) < f6) {
                                this.pressedLink = null;
                            } else {
                                CharSequence charSequence = messageObject.messageText;
                                if (charSequence instanceof Spannable) {
                                    URLSpan[] uRLSpanArr = (URLSpan[]) ((Spannable) charSequence).getSpans(offsetForHorizontal, offsetForHorizontal, URLSpan.class);
                                    if (uRLSpanArr.length != 0) {
                                        if (motionEvent.getAction() == 0) {
                                            this.pressedLink = uRLSpanArr[0];
                                        } else {
                                            URLSpan uRLSpan = uRLSpanArr[0];
                                            URLSpan uRLSpan2 = this.pressedLink;
                                            if (uRLSpan == uRLSpan2) {
                                                openLink(uRLSpan2);
                                            }
                                        }
                                        zCheckBotButtonMotionEvent = z;
                                    } else {
                                        this.pressedLink = null;
                                    }
                                    z = zCheckBotButtonMotionEvent;
                                    zCheckBotButtonMotionEvent = z;
                                } else {
                                    this.pressedLink = null;
                                }
                            }
                        }
                    } else {
                        this.pressedLink = null;
                    }
                } else {
                    this.pressedLink = null;
                }
            }
        }
        if (!zCheckBotButtonMotionEvent) {
            zCheckBotButtonMotionEvent = checkBotButtonMotionEvent(motionEvent);
        }
        return !zCheckBotButtonMotionEvent ? super.onTouchEvent(motionEvent) : zCheckBotButtonMotionEvent;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$onTouchEvent$3() {
        post(new Runnable() { // from class: org.telegram.ui.Cells.ChatActionCell$$ExternalSyntheticLambda7
            @Override // java.lang.Runnable
            public final void run() {
                this.f$0.lambda$onTouchEvent$2();
            }
        });
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$onTouchEvent$2() {
        this.isSpoilerRevealing = false;
        getMessageObject().isSpoilersRevealed = true;
        List list = this.giftPremiumText.spoilers;
        if (list != null) {
            list.clear();
        }
        invalidate();
    }

    private void openPremiumGiftChannel() {
        if (this.delegate != null) {
            final TLRPC.TL_messageActionGiftCode tL_messageActionGiftCode = (TLRPC.TL_messageActionGiftCode) this.currentMessageObject.messageOwner.action;
            AndroidUtilities.runOnUIThread(new Runnable() { // from class: org.telegram.ui.Cells.ChatActionCell$$ExternalSyntheticLambda8
                @Override // java.lang.Runnable
                public final void run() {
                    this.f$0.lambda$openPremiumGiftChannel$4(tL_messageActionGiftCode);
                }
            });
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$openPremiumGiftChannel$4(TLRPC.TL_messageActionGiftCode tL_messageActionGiftCode) {
        this.delegate.didOpenPremiumGiftChannel(this, tL_messageActionGiftCode.slug, false);
    }

    private boolean isSelfGiftCode() {
        MessageObject messageObject = this.currentMessageObject;
        if (messageObject == null) {
            return false;
        }
        TLRPC.Message message = messageObject.messageOwner;
        TLRPC.MessageAction messageAction = message.action;
        if (((messageAction instanceof TLRPC.TL_messageActionGiftCode) || (messageAction instanceof TLRPC.TL_messageActionGiftStars)) && (message.from_id instanceof TLRPC.TL_peerUser)) {
            return UserObject.isUserSelf(MessagesController.getInstance(this.currentAccount).getUser(Long.valueOf(this.currentMessageObject.messageOwner.from_id.user_id)));
        }
        return false;
    }

    public void setOnActionClickListener(View.OnClickListener onClickListener) {
        this.onActionClick = onClickListener;
    }

    public void setOnActionLongClickListener(View.OnLongClickListener onLongClickListener) {
        this.onActionLongClick = onLongClickListener;
    }

    private boolean isGiftCode() {
        MessageObject messageObject = this.currentMessageObject;
        return messageObject != null && (messageObject.messageOwner.action instanceof TLRPC.TL_messageActionGiftCode);
    }

    private void openPremiumGiftPreview() {
        final TLRPC.TL_premiumGiftOption tL_premiumGiftOption = new TLRPC.TL_premiumGiftOption();
        TLRPC.MessageAction messageAction = this.currentMessageObject.messageOwner.action;
        tL_premiumGiftOption.amount = messageAction.amount;
        tL_premiumGiftOption.months = messageAction.months;
        tL_premiumGiftOption.currency = messageAction.currency;
        final String str = null;
        if (isGiftCode() && !isSelfGiftCode()) {
            str = ((TLRPC.TL_messageActionGiftCode) this.currentMessageObject.messageOwner.action).slug;
        }
        if (this.delegate != null) {
            AndroidUtilities.runOnUIThread(new Runnable() { // from class: org.telegram.ui.Cells.ChatActionCell$$ExternalSyntheticLambda9
                @Override // java.lang.Runnable
                public final void run() {
                    this.f$0.lambda$openPremiumGiftPreview$5(tL_premiumGiftOption, str);
                }
            });
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$openPremiumGiftPreview$5(TLRPC.TL_premiumGiftOption tL_premiumGiftOption, String str) {
        this.delegate.didOpenPremiumGift(this, tL_premiumGiftOption, str, false);
    }

    private void openStarsGiftTransaction() {
        TLRPC.Message message;
        MessageObject messageObject = this.currentMessageObject;
        if (messageObject == null || (message = messageObject.messageOwner) == null) {
            return;
        }
        TLRPC.MessageAction messageAction = message.action;
        if (messageAction instanceof TLRPC.TL_messageActionGiftStars) {
            Context context = getContext();
            int i = this.currentAccount;
            TLRPC.Message message2 = this.currentMessageObject.messageOwner;
            StarsIntroActivity.showTransactionSheet(context, i, message2.date, message2.from_id, message2.peer_id, (TLRPC.TL_messageActionGiftStars) message2.action, this.avatarStoryParams.resourcesProvider);
            return;
        }
        if (messageAction instanceof TLRPC.TL_messageActionPrizeStars) {
            Context context2 = getContext();
            int i2 = this.currentAccount;
            TLRPC.Message message3 = this.currentMessageObject.messageOwner;
            StarsIntroActivity.showTransactionSheet(context2, i2, message3.date, message3.from_id, message3.peer_id, (TLRPC.TL_messageActionPrizeStars) message3.action, this.avatarStoryParams.resourcesProvider);
            return;
        }
        if (messageAction instanceof TLRPC.TL_messageActionGiftTon) {
            Context context3 = getContext();
            int i3 = this.currentAccount;
            TLRPC.Message message4 = this.currentMessageObject.messageOwner;
            StarsIntroActivity.showTransactionSheet(context3, i3, message4.date, message4.from_id, message4.peer_id, (TLRPC.TL_messageActionGiftTon) message4.action, this.avatarStoryParams.resourcesProvider);
            return;
        }
        if (messageAction instanceof TLRPC.TL_messageActionStarGift) {
            if (((TLRPC.TL_messageActionStarGift) messageAction).forceIn) {
                return;
            }
            new StarGiftSheet(getContext(), this.currentAccount, this.currentMessageObject.getDialogId(), this.themeDelegate).set(this.currentMessageObject).show();
            return;
        }
        if (messageAction instanceof TLRPC.TL_messageActionStarGiftUnique) {
            if (((TLRPC.TL_messageActionStarGiftUnique) messageAction).gift.burned) {
                BaseFragment safeLastFragment = LaunchActivity.getSafeLastFragment();
                if (safeLastFragment == null) {
                    return;
                }
                BulletinFactory.of(safeLastFragment).createSimpleBulletin(org.telegram.messenger.R.raw.fire_on, LocaleController.getString(org.telegram.messenger.R.string.UniqueGiftNotFoundBurned)).show();
                return;
            }
            new StarGiftSheet(getContext(), this.currentAccount, this.currentMessageObject.getDialogId(), this.themeDelegate).set(this.currentMessageObject).show();
            return;
        }
        if (messageAction instanceof TLRPC.TL_messageActionSetChatTheme) {
            TLRPC.ChatTheme chatTheme = ((TLRPC.TL_messageActionSetChatTheme) messageAction).theme;
            if (chatTheme instanceof TLRPC.TL_chatThemeUniqueGift) {
                TL_stars.StarGift starGift = ((TLRPC.TL_chatThemeUniqueGift) chatTheme).gift;
                if (starGift instanceof TL_stars.TL_starGiftUnique) {
                    new StarGiftSheet(getContext(), this.currentAccount, this.currentMessageObject.getDialogId(), this.themeDelegate).set(starGift.slug, (TL_stars.TL_starGiftUnique) starGift, null).show();
                }
            }
        }
    }

    private void openStarsNeedSheet() {
        MessageSuggestionParams messageSuggestionParamsObtainSuggestionOffer = this.currentMessageObject.obtainSuggestionOffer();
        AmountUtils$Amount amountUtils$Amount = messageSuggestionParamsObtainSuggestionOffer.amount;
        if (amountUtils$Amount == null || amountUtils$Amount.currency != AmountUtils$Currency.STARS) {
            return;
        }
        new StarsIntroActivity.StarsNeededSheet(getContext(), this.themeDelegate, messageSuggestionParamsObtainSuggestionOffer.amount.asDecimal(), 13, ForumUtilities.getMonoForumTitle(this.currentAccount, this.currentMessageObject.getDialogId(), true), null, this.currentMessageObject.getDialogId()).show();
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void openLink(CharacterStyle characterStyle) {
        if (this.delegate == null || !(characterStyle instanceof URLSpan)) {
            return;
        }
        String url = ((URLSpan) characterStyle).getURL();
        if (url.startsWith("task")) {
            this.delegate.didPressTaskLink(this, this.currentMessageObject.getReplyMsgId(), Integer.parseInt(url.substring(5)));
            return;
        }
        if (url.startsWith("topic")) {
            URLSpan uRLSpan = this.pressedLink;
            if (uRLSpan instanceof URLSpanNoUnderline) {
                TLObject object = ((URLSpanNoUnderline) uRLSpan).getObject();
                if (object instanceof TLRPC.TL_forumTopic) {
                    ForumUtilities.openTopic(this.delegate.getBaseFragment(), -this.delegate.getDialogId(), (TLRPC.TL_forumTopic) object, 0);
                    return;
                }
                return;
            }
        }
        if (url.startsWith("invite")) {
            URLSpan uRLSpan2 = this.pressedLink;
            if (uRLSpan2 instanceof URLSpanNoUnderline) {
                TLObject object2 = ((URLSpanNoUnderline) uRLSpan2).getObject();
                if (object2 instanceof TLRPC.TL_chatInviteExported) {
                    this.delegate.needOpenInviteLink((TLRPC.TL_chatInviteExported) object2);
                    return;
                }
                return;
            }
        }
        if (url.startsWith("game")) {
            this.delegate.didPressReplyMessage(this, this.currentMessageObject.getReplyMsgId());
        } else if (url.startsWith("http")) {
            Browser.openUrl(getContext(), url);
        } else {
            this.delegate.needOpenUserProfile(Long.parseLong(url));
        }
    }

    public void setOverrideTextMaxWidth(int i) {
        this.overriddenMaxWidth = i;
    }

    private boolean isMessageActionSuggestedPostApproval() {
        TLRPC.Message message;
        MessageObject messageObject = this.currentMessageObject;
        return (messageObject == null || (message = messageObject.messageOwner) == null || !(message.action instanceof TLRPC.TL_messageActionSuggestedPostApproval)) ? false : true;
    }

    private void createLayout(CharSequence charSequence, int i) {
        TextPaint textPaint;
        CharSequence charSequenceReplaceEmoji;
        ChatActionCellDelegate chatActionCellDelegate;
        TLRPC.Message message;
        MessageObject messageObject;
        int i2;
        int iDp = i - AndroidUtilities.dp(30.0f);
        if (this.isSideMenued) {
            iDp -= AndroidUtilities.dp(64.0f);
        }
        if (isMessageActionSuggestedPostApproval()) {
            iDp = Math.min(iDp - AndroidUtilities.dp(this.isSideMenued ? 28.0f : 82.0f), AndroidUtilities.dp(272.0f));
        }
        if (iDp < 0) {
            return;
        }
        int i3 = this.overriddenMaxWidth;
        if (i3 > 0) {
            iDp = Math.min(i3, iDp);
        }
        int i4 = iDp;
        this.invalidatePath = true;
        if (isMessageActionSuggestedPostApproval() || ((messageObject = this.currentMessageObject) != null && ((i2 = messageObject.type) == 34 || i2 == 35))) {
            textPaint = (TextPaint) getThemedPaint("paintChatActionText3");
        } else if (messageObject != null && messageObject.drawServiceWithDefaultTypeface) {
            textPaint = (TextPaint) getThemedPaint("paintChatActionText2");
        } else {
            textPaint = (TextPaint) getThemedPaint("paintChatActionText");
        }
        TextPaint textPaint2 = textPaint;
        textPaint2.linkColor = textPaint2.getColor();
        if (isMessageActionSuggestedPostApproval()) {
            if (charSequence instanceof Spannable) {
                Spannable spannable = (Spannable) charSequence;
                for (Emoji.EmojiSpan emojiSpan : (Emoji.EmojiSpan[]) spannable.getSpans(0, spannable.length(), Emoji.EmojiSpan.class)) {
                    spannable.removeSpan(emojiSpan);
                }
            }
            charSequenceReplaceEmoji = Emoji.replaceEmoji(charSequence, textPaint2.getFontMetricsInt(), false, null, 0, 0.85f, 0);
        } else {
            charSequenceReplaceEmoji = charSequence;
        }
        StaticLayout staticLayout = new StaticLayout(charSequenceReplaceEmoji, textPaint2, i4, isMessageActionSuggestedPostApproval() ? Layout.Alignment.ALIGN_NORMAL : Layout.Alignment.ALIGN_CENTER, 1.0f, 0.0f, false);
        CharSequence charSequence2 = charSequenceReplaceEmoji;
        this.textLayout = staticLayout;
        this.titleLayout = null;
        MessageObject messageObject2 = this.currentMessageObject;
        if (messageObject2 != null && (message = messageObject2.messageOwner) != null) {
            TLRPC.MessageAction messageAction = message.action;
            if ((messageAction instanceof TLRPC.TL_messageActionSuggestedPostApproval) && !((TLRPC.TL_messageActionSuggestedPostApproval) messageAction).rejected && !((TLRPC.TL_messageActionSuggestedPostApproval) messageAction).balance_too_low) {
                this.titleLayout = new StaticLayout(Emoji.replaceEmoji(AndroidUtilities.replaceTags(LocaleController.getString(org.telegram.messenger.R.string.SuggestionAgreementReached)), textPaint2.getFontMetricsInt(), false, null, 0, 1.0f, 0), textPaint2, i4, Layout.Alignment.ALIGN_CENTER, 1.0f, 0.0f, false);
            }
        }
        this.animatedEmojiStack = AnimatedEmojiSpan.update(0, this, (!this.canDrawInParent || (chatActionCellDelegate = this.delegate) == null || chatActionCellDelegate.canDrawOutboundsContent()) ? false : true, this.animatedEmojiStack, this.textLayout);
        this.textHeight = 0;
        this.textWidth = 0;
        this.titleHeight = 0;
        StaticLayout staticLayout2 = this.titleLayout;
        if (staticLayout2 != null) {
            int height = staticLayout2.getHeight();
            this.titleHeight = height;
            this.titleHeight = height + AndroidUtilities.dp(12.0f);
        }
        MessageObject messageObject3 = this.currentMessageObject;
        if (messageObject3 == null || !messageObject3.isRepostPreview) {
            try {
                int lineCount = this.textLayout.getLineCount();
                for (int i5 = 0; i5 < lineCount; i5++) {
                    try {
                        float lineWidth = this.textLayout.getLineWidth(i5);
                        float f = i4;
                        if (lineWidth > f) {
                            lineWidth = f;
                        }
                        this.textHeight = (int) Math.max(this.textHeight, Math.ceil(this.textLayout.getLineBottom(i5)));
                        this.textWidth = (int) Math.max(this.textWidth, Math.ceil(lineWidth));
                    } catch (Exception e) {
                        FileLog.e(e);
                        return;
                    }
                }
            } catch (Exception e2) {
                FileLog.e(e2);
            }
        }
        this.textX = (i - this.textWidth) / 2;
        int iDp2 = AndroidUtilities.dp(7.0f);
        this.textY = iDp2;
        if (this.titleLayout != null) {
            this.textY = iDp2 + this.titleHeight + AndroidUtilities.dp(11.0f);
        }
        this.textXLeft = (i - (isMessageActionSuggestedPostApproval() ? this.textWidth : this.textLayout.getWidth())) / 2;
        this.titleXLeft = (i - i4) / 2;
        this.spoilersPool.addAll(this.spoilers);
        this.spoilers.clear();
        if (charSequence2 instanceof Spannable) {
            StaticLayout staticLayout3 = this.textLayout;
            int i6 = this.textX;
            SpoilerEffect.addSpoilers(this, staticLayout3, i6, i6 + this.textWidth, (Spannable) charSequence2, this.spoilersPool, this.spoilers, null);
        }
    }

    /* JADX WARN: Failed to calculate best type for var: r7v2 ??
    jadx.core.utils.exceptions.JadxRuntimeException: Type update failed for variable: r7v2 ??, new type: float
    	at jadx.core.dex.visitors.typeinference.TypeUpdate.apply(TypeUpdate.java:109)
    	at jadx.core.dex.visitors.typeinference.TypeUpdate.apply(TypeUpdate.java:59)
    	at jadx.core.dex.visitors.typeinference.FixTypesVisitor.calculateFromBounds(FixTypesVisitor.java:159)
    	at jadx.core.dex.visitors.typeinference.FixTypesVisitor.setBestType(FixTypesVisitor.java:136)
    	at jadx.core.dex.visitors.typeinference.FixTypesVisitor.deduceType(FixTypesVisitor.java:241)
    	at jadx.core.dex.visitors.typeinference.FixTypesVisitor.tryDeduceTypes(FixTypesVisitor.java:224)
    	at jadx.core.dex.visitors.typeinference.FixTypesVisitor.visit(FixTypesVisitor.java:94)
    Caused by: java.lang.NullPointerException
     */
    /* JADX WARN: Failed to calculate best type for var: r7v2 ??
    jadx.core.utils.exceptions.JadxRuntimeException: Type update failed for variable: r7v2 ??, new type: float
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
    /* JADX WARN: Failed to calculate best type for var: r7v3 ??
    jadx.core.utils.exceptions.JadxRuntimeException: Type update failed for variable: r7v3 ??, new type: float
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
    /* JADX WARN: Failed to calculate best type for var: r7v5 ??
    jadx.core.utils.exceptions.JadxRuntimeException: Type update failed for variable: r7v5 ??, new type: float
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
    /*  JADX ERROR: Types fix failed
        jadx.core.utils.exceptions.JadxRuntimeException: Type update failed for variable: r7v2 ??, new type: char
        	at jadx.core.dex.visitors.typeinference.TypeUpdate.apply(TypeUpdate.java:109)
        	at jadx.core.dex.visitors.typeinference.TypeUpdate.apply(TypeUpdate.java:59)
        	at jadx.core.dex.visitors.typeinference.FixTypesVisitor.tryPossibleTypes(FixTypesVisitor.java:186)
        	at jadx.core.dex.visitors.typeinference.FixTypesVisitor.deduceType(FixTypesVisitor.java:245)
        	at jadx.core.dex.visitors.typeinference.FixTypesVisitor.tryDeduceTypes(FixTypesVisitor.java:224)
        	at jadx.core.dex.visitors.typeinference.FixTypesVisitor.visit(FixTypesVisitor.java:94)
        Caused by: java.lang.NullPointerException
        */
    @Override // android.view.View
    protected void onMeasure(int r25, int r26) {
        /*
            Method dump skipped, instruction units count: 1342
            To view this dump add '--comments-level debug' option
        */
        throw new UnsupportedOperationException("Method not decompiled: org.telegram.ui.Cells.ChatActionCell.onMeasure(int, int):void");
    }

    private boolean isNewStyleButtonLayout() {
        MessageObject messageObject;
        int i;
        if (this.starGiftLayout.has() || this.birthdayLayout != null || (i = (messageObject = this.currentMessageObject).type) == 31 || i == 33 || i == 35 || i == 34 || i == 21 || i == 22 || messageObject.isStoryMention()) {
            return true;
        }
        TLRPC.Message message = this.currentMessageObject.messageOwner;
        if (message == null) {
            return false;
        }
        TLRPC.MessageAction messageAction = message.action;
        if (messageAction instanceof TLRPC.TL_messageActionSuggestedPostApproval) {
            return ((TLRPC.TL_messageActionSuggestedPostApproval) messageAction).balance_too_low || ((TLRPC.TL_messageActionSuggestedPostApproval) messageAction).rejected;
        }
        return false;
    }

    private int getImageSize(MessageObject messageObject) {
        int i;
        int iDp = this.stickerSize;
        if (messageObject.type == 21 || isNewStyleButtonLayout()) {
            iDp = AndroidUtilities.dp(78.0f);
        }
        if (isMessageActionSuggestedPostApproval() || (i = messageObject.type) == 34 || i == 35) {
            return 0;
        }
        return iDp;
    }

    /* JADX WARN: Code duplicated, block: B:101:0x0216  */
    /* JADX WARN: Code duplicated, block: B:103:0x0229  */
    /* JADX WARN: Code duplicated, block: B:104:0x0238  */
    /* JADX WARN: Code duplicated, block: B:106:0x0257  */
    /* JADX WARN: Code duplicated, block: B:108:0x0263  */
    /* JADX WARN: Code duplicated, block: B:124:0x02fb  */
    /* JADX WARN: Code duplicated, block: B:126:0x02ff  */
    /* JADX WARN: Code duplicated, block: B:128:0x0315  */
    /* JADX WARN: Code duplicated, block: B:133:0x0320  */
    /* JADX WARN: Code duplicated, block: B:136:0x0329 A[ADDED_TO_REGION] */
    /* JADX WARN: Code duplicated, block: B:138:0x032e  */
    /* JADX WARN: Code duplicated, block: B:141:0x0339  */
    /* JADX WARN: Code duplicated, block: B:146:0x0368  */
    /* JADX WARN: Code duplicated, block: B:153:0x037b  */
    /* JADX WARN: Code duplicated, block: B:157:0x0382  */
    /* JADX WARN: Code duplicated, block: B:164:0x03c3 A[DONT_INVERT] */
    /* JADX WARN: Code duplicated, block: B:165:0x03c5  */
    /* JADX WARN: Code duplicated, block: B:167:0x03c9  */
    /* JADX WARN: Code duplicated, block: B:172:0x03e6  */
    /* JADX WARN: Code duplicated, block: B:173:0x03f0  */
    /* JADX WARN: Code duplicated, block: B:175:0x03f4  */
    /* JADX WARN: Code duplicated, block: B:176:0x03f7  */
    /* JADX WARN: Code duplicated, block: B:179:0x040a  */
    /* JADX WARN: Code duplicated, block: B:183:0x044f A[ADDED_TO_REGION] */
    /* JADX WARN: Code duplicated, block: B:184:0x0451  */
    /* JADX WARN: Code duplicated, block: B:195:0x0469  */
    /* JADX WARN: Code duplicated, block: B:196:0x0471  */
    /* JADX WARN: Code duplicated, block: B:198:0x0475  */
    /* JADX WARN: Code duplicated, block: B:201:0x04bf  */
    /* JADX WARN: Code duplicated, block: B:203:0x04c3  */
    /* JADX WARN: Code duplicated, block: B:204:0x04e0  */
    /* JADX WARN: Code duplicated, block: B:206:0x04e4  */
    /* JADX WARN: Code duplicated, block: B:208:0x04e8  */
    /* JADX WARN: Code duplicated, block: B:209:0x04ef  */
    /* JADX WARN: Code duplicated, block: B:210:0x04f1  */
    /* JADX WARN: Code duplicated, block: B:213:0x0502  */
    /* JADX WARN: Code duplicated, block: B:214:0x050e A[DONT_INVERT] */
    /* JADX WARN: Code duplicated, block: B:215:0x0510  */
    /* JADX WARN: Code duplicated, block: B:217:0x0514  */
    /* JADX WARN: Code duplicated, block: B:220:0x051f  */
    /* JADX WARN: Code duplicated, block: B:222:0x0523  */
    /* JADX WARN: Code duplicated, block: B:223:0x052f  */
    /* JADX WARN: Code duplicated, block: B:224:0x053b A[DONT_INVERT] */
    /* JADX WARN: Code duplicated, block: B:225:0x053d  */
    /* JADX WARN: Code duplicated, block: B:227:0x0543  */
    /* JADX WARN: Code duplicated, block: B:228:0x0555  */
    /* JADX WARN: Code duplicated, block: B:230:0x0561  */
    /* JADX WARN: Code duplicated, block: B:232:0x0567  */
    /* JADX WARN: Code duplicated, block: B:233:0x0569  */
    /* JADX WARN: Code duplicated, block: B:236:0x0584  */
    /* JADX WARN: Code duplicated, block: B:238:0x0588  */
    /* JADX WARN: Code duplicated, block: B:239:0x059e  */
    /* JADX WARN: Code duplicated, block: B:240:0x05b4  */
    /* JADX WARN: Code duplicated, block: B:242:0x05b8  */
    /* JADX WARN: Code duplicated, block: B:243:0x05be  */
    /* JADX WARN: Code duplicated, block: B:245:0x05c2 A[DONT_INVERT] */
    /* JADX WARN: Code duplicated, block: B:246:0x05c4  */
    /* JADX WARN: Code duplicated, block: B:247:0x05cb  */
    /* JADX WARN: Code duplicated, block: B:248:0x05d2 A[DONT_INVERT] */
    /* JADX WARN: Code duplicated, block: B:249:0x05d4  */
    /* JADX WARN: Code duplicated, block: B:250:0x05db  */
    /* JADX WARN: Code duplicated, block: B:253:0x05ea  */
    /* JADX WARN: Code duplicated, block: B:261:0x060d  */
    /* JADX WARN: Code duplicated, block: B:264:0x061a  */
    /* JADX WARN: Code duplicated, block: B:267:0x0620  */
    /* JADX WARN: Code duplicated, block: B:269:0x0626 A[ADDED_TO_REGION] */
    /* JADX WARN: Code duplicated, block: B:273:0x0654  */
    /* JADX WARN: Code duplicated, block: B:278:0x0695  */
    /* JADX WARN: Code duplicated, block: B:280:0x06a5  */
    /* JADX WARN: Code duplicated, block: B:282:0x06a9  */
    /* JADX WARN: Code duplicated, block: B:283:0x06d4  */
    /* JADX WARN: Code duplicated, block: B:284:0x06ff  */
    /* JADX WARN: Code duplicated, block: B:286:0x070b  */
    /* JADX WARN: Code duplicated, block: B:288:0x071b  */
    /* JADX WARN: Code duplicated, block: B:289:0x0729  */
    /* JADX WARN: Code duplicated, block: B:291:0x072d  */
    /* JADX WARN: Code duplicated, block: B:292:0x073b  */
    /* JADX WARN: Code duplicated, block: B:294:0x074f  */
    /* JADX WARN: Code duplicated, block: B:295:0x075d  */
    /* JADX WARN: Code duplicated, block: B:297:0x0769  */
    /* JADX WARN: Code duplicated, block: B:298:0x0775  */
    /* JADX WARN: Code duplicated, block: B:301:0x07a8  */
    /* JADX WARN: Code duplicated, block: B:303:0x07ac  */
    /* JADX WARN: Code duplicated, block: B:304:0x07cc  */
    /* JADX WARN: Code duplicated, block: B:306:0x07d0  */
    /* JADX WARN: Code duplicated, block: B:308:0x07f3  */
    /* JADX WARN: Code duplicated, block: B:310:0x07f9  */
    /* JADX WARN: Code duplicated, block: B:311:0x0800  */
    /* JADX WARN: Code duplicated, block: B:313:0x0815  */
    /* JADX WARN: Code duplicated, block: B:315:0x081b  */
    /* JADX WARN: Code duplicated, block: B:316:0x0822  */
    /* JADX WARN: Code duplicated, block: B:320:0x083a  */
    /* JADX WARN: Code duplicated, block: B:321:0x0883  */
    /* JADX WARN: Code duplicated, block: B:323:0x08eb  */
    /* JADX WARN: Code duplicated, block: B:325:0x08ef  */
    /* JADX WARN: Code duplicated, block: B:327:0x0928  */
    /* JADX WARN: Code duplicated, block: B:328:0x0936  */
    /* JADX WARN: Code duplicated, block: B:330:0x0973  */
    /* JADX WARN: Code duplicated, block: B:332:0x0977  */
    /* JADX WARN: Code duplicated, block: B:334:0x097d  */
    /* JADX WARN: Code duplicated, block: B:335:0x0982  */
    /* JADX WARN: Code duplicated, block: B:337:0x0986  */
    /* JADX WARN: Code duplicated, block: B:338:0x098b  */
    /* JADX WARN: Code duplicated, block: B:340:0x098e  */
    /* JADX WARN: Code duplicated, block: B:343:0x09c9  */
    /* JADX WARN: Code duplicated, block: B:345:0x09cc  */
    /* JADX WARN: Code duplicated, block: B:348:0x09d9  */
    /* JADX WARN: Code duplicated, block: B:351:0x09e2  */
    /* JADX WARN: Code duplicated, block: B:353:0x0a03  */
    /* JADX WARN: Code duplicated, block: B:355:0x0a07  */
    /* JADX WARN: Code duplicated, block: B:358:0x0a18  */
    /* JADX WARN: Code duplicated, block: B:361:0x0a2a  */
    /* JADX WARN: Code duplicated, block: B:369:0x0a3b  */
    /* JADX WARN: Code duplicated, block: B:372:0x0a4a  */
    /* JADX WARN: Code duplicated, block: B:374:0x0a5e  */
    /* JADX WARN: Code duplicated, block: B:375:0x0a6e  */
    /* JADX WARN: Code duplicated, block: B:377:0x0a7f  */
    /* JADX WARN: Code duplicated, block: B:379:0x0a84  */
    /* JADX WARN: Code duplicated, block: B:380:0x0a91  */
    /* JADX WARN: Code duplicated, block: B:383:0x0aa2  */
    /* JADX WARN: Code duplicated, block: B:390:0x0ab7  */
    /* JADX WARN: Code duplicated, block: B:392:0x0ada  */
    /* JADX WARN: Code duplicated, block: B:394:0x0ade  */
    /* JADX WARN: Code duplicated, block: B:396:0x0aea  */
    /* JADX WARN: Code duplicated, block: B:397:0x0aed  */
    /* JADX WARN: Code duplicated, block: B:400:0x0b01  */
    /* JADX WARN: Code duplicated, block: B:403:0x0b07  */
    /* JADX WARN: Code duplicated, block: B:405:0x0b0d  */
    /* JADX WARN: Code duplicated, block: B:411:0x0b27  */
    /* JADX WARN: Code duplicated, block: B:416:0x0b5e  */
    /* JADX WARN: Code duplicated, block: B:418:0x0b64  */
    /* JADX WARN: Code duplicated, block: B:420:0x0b7c  */
    /* JADX WARN: Code duplicated, block: B:422:0x0ba3  */
    /* JADX WARN: Code duplicated, block: B:85:0x0198 A[DONT_INVERT] */
    /* JADX WARN: Code duplicated, block: B:86:0x019a  */
    /* JADX WARN: Code duplicated, block: B:91:0x01c6  */
    /* JADX WARN: Code duplicated, block: B:93:0x01cc  */
    /* JADX WARN: Code duplicated, block: B:94:0x01ea  */
    /* JADX WARN: Code duplicated, block: B:96:0x01ee  */
    /* JADX WARN: Code duplicated, block: B:97:0x01f3  */
    /* JADX WARN: Code duplicated, block: B:99:0x01fa  */
    /* JADX WARN: Instruction removed from duplicated block: B:325:0x08ef, please report this as an issue */
    private void buildLayout() {
        CharSequence charSequenceCreateActionTextWithTopic;
        int i;
        long dialogId;
        TLRPC.User user;
        CharSequence charSequenceReplaceTags;
        long dialogId2;
        TLRPC.User user2;
        CharSequence charSequence;
        CharSequence string;
        CharSequence charSequence2;
        boolean z;
        TLRPC.TL_messageActionSuggestProfilePhoto tL_messageActionSuggestProfilePhoto;
        TLRPC.User user3;
        boolean z2;
        CharSequence string2;
        CharSequence string3;
        ArrayList arrayList;
        TLRPC.User user4;
        TLRPC.Photo photo;
        ArrayList arrayList2;
        TLRPC.MessageAction messageAction;
        TLRPC.TL_textWithEntities tL_textWithEntities;
        CharSequence string4;
        int i2;
        String str;
        long fromChatId;
        String string5;
        TLRPC.TL_messageActionNoForwardsRequest tL_messageActionNoForwardsRequest;
        SpannableStringBuilder spannableStringBuilder;
        String shortName;
        CharSequence charSequenceReplaceTags2;
        CharSequence charSequenceReplaceTags3;
        TLRPC.TL_messageActionStarGiftPurchaseOffer tL_messageActionStarGiftPurchaseOffer;
        SpannableStringBuilder spannableStringBuilder2;
        int iMax;
        String shortDuration2;
        int i3;
        char c;
        TLRPC.User user5;
        TLRPC.MessageAction messageAction2;
        TLRPC.TL_messageActionStarGift tL_messageActionStarGift;
        long j;
        long clientUserId;
        TLRPC.Peer peer;
        boolean z3;
        boolean z4;
        long fromChatId2;
        SpannableStringBuilder spannableStringBuilder3;
        TLObject userOrChat;
        long peerDialogId;
        long j2;
        boolean z5;
        int i4;
        TL_stars.StarGift starGift;
        String str2;
        boolean z6;
        TLRPC.TL_textWithEntities tL_textWithEntities2;
        CharSequence charSequenceReplaceTags4;
        String string6;
        TL_stars.StarGift starGift2;
        CharSequence string7;
        CharSequence string8;
        CharSequence charSequence3;
        TL_stars.StarGift starGift3;
        CharSequence charSequenceReplaceSingleTagToLink;
        String publicUsername;
        char c2;
        Object objValueOf;
        TLRPC.Peer peer2;
        String string9;
        TLRPC.MessageAction messageAction3;
        TLRPC.Message message;
        TLRPC.MessageAction messageAction4;
        int i5;
        TLRPC.MessageMedia messageMedia;
        this.giftRectEmpty = false;
        MessageObject messageObject = this.currentMessageObject;
        if (messageObject != null) {
            if (!messageObject.isExpiredStory()) {
                ChatActionCellDelegate chatActionCellDelegate = this.delegate;
                charSequenceCreateActionTextWithTopic = (chatActionCellDelegate != null && chatActionCellDelegate.getTopicId() == 0 && MessageObject.isTopicActionMessage(messageObject)) ? ForumUtilities.createActionTextWithTopic(MessagesController.getInstance(this.currentAccount).getTopicsController().findTopic(-messageObject.getDialogId(), MessageObject.getTopicId(this.currentAccount, messageObject.messageOwner, true)), messageObject) : null;
            } else if (messageObject.messageOwner.media.user_id != UserConfig.getInstance(this.currentAccount).getClientUserId()) {
                charSequenceCreateActionTextWithTopic = StoriesUtilities.createExpiredStoryString(true, org.telegram.messenger.R.string.ExpiredStoryMention, new Object[0]);
            } else {
                charSequenceCreateActionTextWithTopic = StoriesUtilities.createExpiredStoryString(true, org.telegram.messenger.R.string.ExpiredStoryMentioned, MessagesController.getInstance(this.currentAccount).getUser(Long.valueOf(messageObject.getDialogId())).first_name);
            }
            if (charSequenceCreateActionTextWithTopic == null) {
                TLRPC.Message message2 = messageObject.messageOwner;
                if (message2 != null && (messageMedia = message2.media) != null && messageMedia.ttl_seconds != 0) {
                    if (messageMedia.photo != null) {
                        charSequenceCreateActionTextWithTopic = LocaleController.getString(org.telegram.messenger.R.string.AttachPhotoExpired);
                    } else {
                        TLRPC.Document document = messageMedia.document;
                        if ((document instanceof TLRPC.TL_documentEmpty) || ((messageMedia instanceof TLRPC.TL_messageMediaDocument) && document == null)) {
                            if (messageMedia.voice) {
                                charSequenceCreateActionTextWithTopic = LocaleController.getString(org.telegram.messenger.R.string.AttachVoiceExpired);
                            } else if (messageMedia.round) {
                                charSequenceCreateActionTextWithTopic = LocaleController.getString(org.telegram.messenger.R.string.AttachRoundExpired);
                            } else {
                                charSequenceCreateActionTextWithTopic = LocaleController.getString(org.telegram.messenger.R.string.AttachVideoExpired);
                            }
                        } else {
                            charSequenceCreateActionTextWithTopic = AnimatedEmojiSpan.cloneSpans(messageObject.messageText);
                        }
                    }
                } else {
                    charSequenceCreateActionTextWithTopic = AnimatedEmojiSpan.cloneSpans(messageObject.messageText);
                }
                if (ChatUtils.getInstance().shouldAddTimestamp(this.currentMessageObject, charSequenceCreateActionTextWithTopic)) {
                    charSequenceCreateActionTextWithTopic = ChatUtils.getInstance().addTimestamp(charSequenceCreateActionTextWithTopic, this.currentMessageObject.messageOwner.date, this.themeDelegate);
                }
            }
        } else {
            charSequenceCreateActionTextWithTopic = this.customText;
        }
        MessageObject messageObject2 = this.currentMessageObject;
        if (messageObject2 != null && messageObject2.isRepostPreview) {
            charSequenceCreateActionTextWithTopic = _UrlKt.FRAGMENT_ENCODE_SET;
        }
        if (messageObject2 != null && (message = messageObject2.messageOwner) != null && (messageAction4 = message.action) != null) {
            if (messageAction4 instanceof TLRPC.TL_messageActionTodoAppendTasks) {
                i5 = org.telegram.messenger.R.drawable.mini_checklist_add;
            } else if (messageAction4 instanceof TLRPC.TL_messageActionTodoCompletions) {
                TLRPC.TL_messageActionTodoCompletions tL_messageActionTodoCompletions = (TLRPC.TL_messageActionTodoCompletions) messageAction4;
                if (tL_messageActionTodoCompletions.incompleted.size() > tL_messageActionTodoCompletions.completed.size()) {
                    i5 = org.telegram.messenger.R.drawable.mini_checklist_undone;
                } else {
                    i5 = org.telegram.messenger.R.drawable.mini_checklist_done;
                }
            } else {
                i5 = 0;
            }
            if (i5 != 0) {
                SpannableStringBuilder spannableStringBuilder4 = new SpannableStringBuilder(charSequenceCreateActionTextWithTopic);
                spannableStringBuilder4.insert(0, (CharSequence) "i ");
                spannableStringBuilder4.setSpan(new ColoredImageSpan(i5), 0, 1, 33);
                charSequenceCreateActionTextWithTopic = spannableStringBuilder4;
            }
        }
        createLayout(charSequenceCreateActionTextWithTopic, this.previousWidth);
        if (this.birthdayLayout != null) {
            this.textLayout = null;
            this.textHeight = 0;
            this.titleLayout = null;
            this.titleHeight = 0;
            this.textY = 0;
        }
        if (messageObject != null) {
            TLRPC.Message message3 = messageObject.messageOwner;
            if (message3 != null) {
                TLRPC.MessageAction messageAction5 = message3.action;
                if ((messageAction5 instanceof TLRPC.TL_messageActionSuggestedPostApproval) && ((TLRPC.TL_messageActionSuggestedPostApproval) messageAction5).balance_too_low) {
                    createGiftPremiumLayouts(null, null, null, charSequenceCreateActionTextWithTopic, false, !ChatObject.canManageMonoForum(this.currentAccount, messageObject.getDialogId()) ? LocaleController.getString(org.telegram.messenger.R.string.StarsBuy) : null, 11, null, this.giftRectSize, false, true);
                    this.textLayout = null;
                    this.textHeight = 0;
                    this.titleLayout = null;
                    this.titleHeight = 0;
                    this.textY = 0;
                    this.giftRectEmpty = true;
                } else if (message3 != null) {
                    messageAction3 = message3.action;
                    if (!(messageAction3 instanceof TLRPC.TL_messageActionSuggestedPostApproval) && ((TLRPC.TL_messageActionSuggestedPostApproval) messageAction3).rejected) {
                        createGiftPremiumLayouts(null, null, null, charSequenceCreateActionTextWithTopic, false, null, 11, null, this.giftRectSize, false, true);
                        this.textLayout = null;
                        this.textHeight = 0;
                        this.titleLayout = null;
                        this.titleHeight = 0;
                        this.textY = 0;
                        this.giftRectEmpty = true;
                    } else {
                        i = messageObject.type;
                        if (i == 11) {
                            ImageReceiver imageReceiver = this.imageReceiver;
                            float f = (this.previousWidth - AndroidUtilities.roundMessageSize) / 2.0f;
                            float fDp = this.textHeight + AndroidUtilities.dp(19.0f);
                            int i6 = AndroidUtilities.roundMessageSize;
                            imageReceiver.setImageCoords(f, fDp, i6, i6);
                        } else if (i == 25) {
                            createGiftPremiumChannelLayouts();
                        } else if (i == 30) {
                            user5 = MessagesController.getInstance(this.currentAccount).getUser(Long.valueOf(this.currentMessageObject.getDialogId()));
                            messageAction2 = messageObject.messageOwner.action;
                            if (messageAction2 instanceof TLRPC.TL_messageActionGiftStars) {
                                CharSequence pluralStringComma = LocaleController.formatPluralStringComma("ActionGiftStarsTitle", (int) ((TLRPC.TL_messageActionGiftStars) messageAction2).stars);
                                if (this.currentMessageObject.isOutOwner()) {
                                    string9 = LocaleController.formatString(org.telegram.messenger.R.string.ActionGiftStarsSubtitle, UserObject.getForcedFirstName(user5));
                                } else {
                                    string9 = LocaleController.getString(org.telegram.messenger.R.string.ActionGiftStarsSubtitleYou);
                                }
                                createGiftPremiumLayouts(pluralStringComma, null, null, AndroidUtilities.replaceTags(string9), false, LocaleController.getString(org.telegram.messenger.R.string.ActionGiftStarsView), 11, null, this.giftRectSize, true, false);
                            } else if (!(messageAction2 instanceof TLRPC.TL_messageActionStarGiftUnique) && ((TLRPC.TL_messageActionStarGiftUnique) messageAction2).refunded) {
                                long clientUserId2 = UserConfig.getInstance(this.currentAccount).getClientUserId();
                                TLRPC.TL_messageActionStarGiftUnique tL_messageActionStarGiftUnique = (TLRPC.TL_messageActionStarGiftUnique) messageObject.messageOwner.action;
                                if (messageObject.isOutOwner() != (!tL_messageActionStarGiftUnique.upgrade)) {
                                    clientUserId2 = messageObject.getDialogId();
                                }
                                TLRPC.User user6 = MessagesController.getInstance(this.currentAccount).getUser(Long.valueOf(clientUserId2));
                                SpannableStringBuilder spannableStringBuilder5 = new SpannableStringBuilder();
                                spannableStringBuilder5.append((CharSequence) LocaleController.getString(tL_messageActionStarGiftUnique.prepaid_upgrade ? org.telegram.messenger.R.string.Gift2ActionUpgradeTitle : org.telegram.messenger.R.string.Gift2ActionTitle)).append((CharSequence) " ");
                                if (user6 != null && user6.photo != null) {
                                    spannableStringBuilder5.append((CharSequence) "a ");
                                    AvatarSpan avatarSpan = new AvatarSpan(this, this.currentAccount, 18.0f);
                                    avatarSpan.setUser(user6);
                                    spannableStringBuilder5.setSpan(avatarSpan, spannableStringBuilder5.length() - 2, spannableStringBuilder5.length() - 1, 33);
                                }
                                spannableStringBuilder5.append((CharSequence) UserObject.getForcedFirstName(user6));
                                createGiftPremiumLayouts(spannableStringBuilder5, null, null, LocaleController.getString(org.telegram.messenger.R.string.Gift2ActionUpgradeRefundedText), false, LocaleController.getString(org.telegram.messenger.R.string.ActionGiftStarsView), 12, LocaleController.getString(org.telegram.messenger.R.string.Gift2UniqueRibbon), this.giftRectSize, true, false);
                            } else if (messageAction2 instanceof TLRPC.TL_messageActionStarGift) {
                                tL_messageActionStarGift = (TLRPC.TL_messageActionStarGift) messageAction2;
                                j = tL_messageActionStarGift.convert_stars;
                                clientUserId = UserConfig.getInstance(this.currentAccount).getClientUserId();
                                peer = tL_messageActionStarGift.peer;
                                if (peer != null || (tL_messageActionStarGift.prepaid_upgrade && !(peer instanceof TLRPC.TL_peerChannel))) {
                                    z3 = false;
                                } else {
                                    z3 = true;
                                }
                                if (messageObject.getDialogId() == clientUserId || z3) {
                                    z4 = false;
                                } else {
                                    z4 = true;
                                }
                                fromChatId2 = messageObject.getFromChatId();
                                if (!tL_messageActionStarGift.prepaid_upgrade && (peer2 = tL_messageActionStarGift.from_id) != null) {
                                    fromChatId2 = DialogObject.getPeerDialogId(peer2);
                                }
                                spannableStringBuilder3 = new SpannableStringBuilder();
                                userOrChat = MessagesController.getInstance(this.currentAccount).getUserOrChat(fromChatId2);
                                peerDialogId = DialogObject.getPeerDialogId(tL_messageActionStarGift.to_id);
                                TLObject userOrChat2 = MessagesController.getInstance(this.currentAccount).getUserOrChat(peerDialogId);
                                if (tL_messageActionStarGift.can_upgrade || tL_messageActionStarGift.converted) {
                                    j2 = peerDialogId;
                                } else {
                                    j2 = peerDialogId;
                                    if (tL_messageActionStarGift.upgrade_stars > 0 && !tL_messageActionStarGift.upgraded) {
                                        z5 = true;
                                    }
                                    if (j2 == 0 && tL_messageActionStarGift.auction_acquired && userOrChat2 != null) {
                                        spannableStringBuilder3.append((CharSequence) LocaleController.getString(org.telegram.messenger.R.string.Gift2ActionTitleTo)).append((CharSequence) " ");
                                        if (DialogObject.hasPhoto(userOrChat2)) {
                                            spannableStringBuilder3.append((CharSequence) "a ");
                                            AvatarSpan avatarSpan2 = new AvatarSpan(this, this.currentAccount, 18.0f);
                                            avatarSpan2.setObject(userOrChat2);
                                            spannableStringBuilder3.setSpan(avatarSpan2, spannableStringBuilder3.length() - 2, spannableStringBuilder3.length() - 1, 33);
                                        }
                                        spannableStringBuilder3.append((CharSequence) DialogObject.getShortName(userOrChat2));
                                    } else if (z4) {
                                        if (tL_messageActionStarGift.gift_num <= 0 && (starGift = tL_messageActionStarGift.gift) != null && (str2 = starGift.title) != null) {
                                            spannableStringBuilder3.append((CharSequence) str2).append((CharSequence) " #").append((CharSequence) LocaleController.formatNumber(tL_messageActionStarGift.gift_num, ','));
                                        } else {
                                            spannableStringBuilder3.append((CharSequence) LocaleController.getString(org.telegram.messenger.R.string.Gift2ActionSelfTitle));
                                        }
                                    } else {
                                        if (tL_messageActionStarGift.prepaid_upgrade) {
                                            i4 = org.telegram.messenger.R.string.Gift2ActionUpgradeTitle;
                                        } else {
                                            i4 = org.telegram.messenger.R.string.Gift2ActionTitle;
                                        }
                                        spannableStringBuilder3.append((CharSequence) LocaleController.getString(i4)).append((CharSequence) " ");
                                        if (DialogObject.hasPhoto(userOrChat)) {
                                            spannableStringBuilder3.append((CharSequence) "a ");
                                            AvatarSpan avatarSpan3 = new AvatarSpan(this, this.currentAccount, 18.0f);
                                            avatarSpan3.setObject(userOrChat);
                                            spannableStringBuilder3.setSpan(avatarSpan3, spannableStringBuilder3.length() - 2, spannableStringBuilder3.length() - 1, 33);
                                        }
                                        spannableStringBuilder3.append((CharSequence) DialogObject.getShortName(userOrChat));
                                    }
                                    z6 = !((messageObject.isOutOwner() || z4) && tL_messageActionStarGift.converted) && tL_messageActionStarGift.convert_stars > 0 && MessagesController.getInstance(this.currentAccount).stargiftsConvertPeriodMax - (ConnectionsManager.getInstance(this.currentAccount).getCurrentTime() - messageObject.messageOwner.date) > 0 && !tL_messageActionStarGift.refunded;
                                    if (tL_messageActionStarGift.refunded) {
                                        charSequenceReplaceTags4 = LocaleController.getString(org.telegram.messenger.R.string.Gift2ActionConvertRefundedText);
                                    } else {
                                        tL_textWithEntities2 = tL_messageActionStarGift.message;
                                        if (tL_textWithEntities2 == null && !TextUtils.isEmpty(tL_textWithEntities2.text)) {
                                            SpannableStringBuilder spannableStringBuilder6 = new SpannableStringBuilder(tL_messageActionStarGift.message.text);
                                            this.giftTextPaint.setTextSize(AndroidUtilities.dp(13.0f));
                                            MessageObject.addEntitiesToText(spannableStringBuilder6, tL_messageActionStarGift.message.entities, false, false, true, true);
                                            charSequenceReplaceTags4 = MessageObject.replaceAnimatedEmoji(Emoji.replaceEmoji((CharSequence) spannableStringBuilder6, this.giftTextPaint.getFontMetricsInt(), false, (int[]) null), tL_messageActionStarGift.message.entities, this.giftTextPaint.getFontMetricsInt());
                                        } else if (tL_messageActionStarGift.auction_acquired) {
                                            charSequenceReplaceTags4 = LocaleController.formatString(org.telegram.messenger.R.string.Gift2ActionWonActionText, LocaleController.formatNumber(tL_messageActionStarGift.gift.stars + tL_messageActionStarGift.upgrade_stars, ','));
                                        } else if (z3) {
                                            if (tL_messageActionStarGift.converted) {
                                                charSequenceReplaceTags4 = LocaleController.formatPluralStringComma("Gift2ActionConvertedInfo", (int) j);
                                            } else if (!z6 && j > 0) {
                                                charSequenceReplaceTags4 = AndroidUtilities.replaceTags(LocaleController.formatPluralStringComma("Gift2ActionInfoChannel", (int) j));
                                            } else {
                                                charSequenceReplaceTags4 = AndroidUtilities.replaceTags(LocaleController.getString(org.telegram.messenger.R.string.Gift2ActionInfoChannelNoConvert));
                                            }
                                        } else if (z4) {
                                            if (!tL_messageActionStarGift.converted && j > 0) {
                                                charSequenceReplaceTags4 = LocaleController.formatPluralStringComma("Gift2ActionConvertedInfo", (int) j);
                                            } else if (tL_messageActionStarGift.can_upgrade) {
                                                charSequenceReplaceTags4 = AndroidUtilities.replaceTags(LocaleController.getString(org.telegram.messenger.R.string.Gift2ActionSelfInfoUpgrade));
                                            } else {
                                                charSequenceReplaceTags4 = AndroidUtilities.replaceTags(LocaleController.getString(org.telegram.messenger.R.string.Gift2ActionSelfInfoNoConvert));
                                            }
                                        } else if (z5) {
                                            if (messageObject.isOutOwner()) {
                                                string6 = LocaleController.formatString(org.telegram.messenger.R.string.Gift2ActionUpgradeOut, UserObject.getForcedFirstName(user5));
                                            } else {
                                                string6 = LocaleController.getString(org.telegram.messenger.R.string.Gift2ActionUpgrade);
                                            }
                                            charSequenceReplaceTags4 = AndroidUtilities.replaceTags(string6);
                                        } else if (messageObject.isOutOwner()) {
                                            if (!z6 && j > 0) {
                                                charSequenceReplaceTags4 = AndroidUtilities.replaceTags(LocaleController.formatPluralStringComma("Gift2ActionOutInfo", (int) j, UserObject.getForcedFirstName(user5)));
                                            } else if (tL_messageActionStarGift.can_upgrade) {
                                                charSequenceReplaceTags4 = AndroidUtilities.replaceTags(LocaleController.formatString(org.telegram.messenger.R.string.Gift2ActionOutInfoUpgrade, UserObject.getForcedFirstName(user5)));
                                            } else {
                                                charSequenceReplaceTags4 = AndroidUtilities.replaceTags(LocaleController.formatString(org.telegram.messenger.R.string.Gift2ActionOutInfoNoConvert, UserObject.getForcedFirstName(user5)));
                                            }
                                        } else if (tL_messageActionStarGift.converted) {
                                            charSequenceReplaceTags4 = LocaleController.formatPluralStringComma("Gift2ActionConvertedInfo", (int) j);
                                        } else if (tL_messageActionStarGift.saved) {
                                            if (!z6) {
                                                charSequenceReplaceTags4 = LocaleController.getString(org.telegram.messenger.R.string.Gift2ActionBotSavedInfo);
                                            } else {
                                                charSequenceReplaceTags4 = LocaleController.getString(org.telegram.messenger.R.string.Gift2ActionSavedInfo);
                                            }
                                        } else if (!z6) {
                                            charSequenceReplaceTags4 = LocaleController.getString(org.telegram.messenger.R.string.Gift2ActionBotInfo);
                                        } else {
                                            charSequenceReplaceTags4 = AndroidUtilities.replaceTags(LocaleController.formatPluralStringComma("Gift2ActionInfo", (int) j));
                                        }
                                    }
                                    starGift2 = tL_messageActionStarGift.gift;
                                    if (starGift2 == null && starGift2.limited) {
                                        int i7 = org.telegram.messenger.R.string.Gift2Limited1OfRibbon;
                                        int i8 = starGift2.availability_total;
                                        if (i8 > 1500) {
                                            c2 = 0;
                                            objValueOf = AndroidUtilities.formatWholeNumber(i8, 0);
                                        } else {
                                            c2 = 0;
                                            objValueOf = Integer.valueOf(i8);
                                        }
                                        Object[] objArr = new Object[1];
                                        objArr[c2] = objValueOf;
                                        string7 = LocaleController.formatString(i7, objArr);
                                    } else {
                                        string7 = null;
                                    }
                                    string8 = LocaleController.getString(org.telegram.messenger.R.string.ActionGiftStarsView);
                                    if (messageObject.isOutOwner() || tL_messageActionStarGift.forceIn || z5) {
                                        charSequence3 = string8;
                                        charSequence3 = string8;
                                        charSequence3 = string8;
                                        if (!messageObject.isOutOwner() && z5) {
                                            SpannableStringBuilder spannableStringBuilder7 = new SpannableStringBuilder();
                                            spannableStringBuilder7.append((CharSequence) "^  ");
                                            ColoredImageSpan coloredImageSpan = new ColoredImageSpan(org.telegram.messenger.R.drawable.gift_unpack);
                                            coloredImageSpan.setScale(0.8f, 0.8f);
                                            spannableStringBuilder7.setSpan(coloredImageSpan, 0, 1, 33);
                                            spannableStringBuilder7.append((CharSequence) LocaleController.getString(org.telegram.messenger.R.string.Gift2Unpack));
                                            charSequence3 = spannableStringBuilder7;
                                        }
                                    }
                                    CharSequence charSequence4 = charSequence3;
                                    starGift3 = tL_messageActionStarGift.gift;
                                    if (starGift3 != null || starGift3.released_by == null || (publicUsername = DialogObject.getPublicUsername(MessagesController.getInstance(this.currentAccount).getUserOrChat(DialogObject.getPeerDialogId(tL_messageActionStarGift.gift.released_by)))) == null) {
                                        charSequenceReplaceSingleTagToLink = null;
                                    } else {
                                        charSequenceReplaceSingleTagToLink = StarGiftSheet.replaceSingleTagToLink(LocaleController.formatString(org.telegram.messenger.R.string.Gift2ActionReleasedBy, "@" + publicUsername), null);
                                    }
                                    createGiftPremiumLayouts(spannableStringBuilder3, null, charSequenceReplaceSingleTagToLink, charSequenceReplaceTags4, false, charSequence4, 11, string7, this.giftRectSize, true, false);
                                }
                                z5 = false;
                                if (j2 == 0) {
                                    if (z4) {
                                        if (tL_messageActionStarGift.gift_num <= 0) {
                                            spannableStringBuilder3.append((CharSequence) LocaleController.getString(org.telegram.messenger.R.string.Gift2ActionSelfTitle));
                                        } else {
                                            spannableStringBuilder3.append((CharSequence) LocaleController.getString(org.telegram.messenger.R.string.Gift2ActionSelfTitle));
                                        }
                                    } else {
                                        if (tL_messageActionStarGift.prepaid_upgrade) {
                                            i4 = org.telegram.messenger.R.string.Gift2ActionUpgradeTitle;
                                        } else {
                                            i4 = org.telegram.messenger.R.string.Gift2ActionTitle;
                                        }
                                        spannableStringBuilder3.append((CharSequence) LocaleController.getString(i4)).append((CharSequence) " ");
                                        if (DialogObject.hasPhoto(userOrChat)) {
                                            spannableStringBuilder3.append((CharSequence) "a ");
                                            AvatarSpan avatarSpan4 = new AvatarSpan(this, this.currentAccount, 18.0f);
                                            avatarSpan4.setObject(userOrChat);
                                            spannableStringBuilder3.setSpan(avatarSpan4, spannableStringBuilder3.length() - 2, spannableStringBuilder3.length() - 1, 33);
                                        }
                                        spannableStringBuilder3.append((CharSequence) DialogObject.getShortName(userOrChat));
                                    }
                                } else if (z4) {
                                    if (tL_messageActionStarGift.gift_num <= 0) {
                                        spannableStringBuilder3.append((CharSequence) LocaleController.getString(org.telegram.messenger.R.string.Gift2ActionSelfTitle));
                                    } else {
                                        spannableStringBuilder3.append((CharSequence) LocaleController.getString(org.telegram.messenger.R.string.Gift2ActionSelfTitle));
                                    }
                                } else {
                                    if (tL_messageActionStarGift.prepaid_upgrade) {
                                        i4 = org.telegram.messenger.R.string.Gift2ActionUpgradeTitle;
                                    } else {
                                        i4 = org.telegram.messenger.R.string.Gift2ActionTitle;
                                    }
                                    spannableStringBuilder3.append((CharSequence) LocaleController.getString(i4)).append((CharSequence) " ");
                                    if (DialogObject.hasPhoto(userOrChat)) {
                                        spannableStringBuilder3.append((CharSequence) "a ");
                                        AvatarSpan avatarSpan5 = new AvatarSpan(this, this.currentAccount, 18.0f);
                                        avatarSpan5.setObject(userOrChat);
                                        spannableStringBuilder3.setSpan(avatarSpan5, spannableStringBuilder3.length() - 2, spannableStringBuilder3.length() - 1, 33);
                                    }
                                    spannableStringBuilder3.append((CharSequence) DialogObject.getShortName(userOrChat));
                                }
                                if (messageObject.isOutOwner()) {
                                }
                                if (tL_messageActionStarGift.refunded) {
                                    charSequenceReplaceTags4 = LocaleController.getString(org.telegram.messenger.R.string.Gift2ActionConvertRefundedText);
                                } else {
                                    tL_textWithEntities2 = tL_messageActionStarGift.message;
                                    if (tL_textWithEntities2 == null) {
                                        if (tL_messageActionStarGift.auction_acquired) {
                                            charSequenceReplaceTags4 = LocaleController.formatString(org.telegram.messenger.R.string.Gift2ActionWonActionText, LocaleController.formatNumber(tL_messageActionStarGift.gift.stars + tL_messageActionStarGift.upgrade_stars, ','));
                                        } else if (z3) {
                                            if (tL_messageActionStarGift.converted) {
                                                charSequenceReplaceTags4 = LocaleController.formatPluralStringComma("Gift2ActionConvertedInfo", (int) j);
                                            } else if (!z6) {
                                                charSequenceReplaceTags4 = AndroidUtilities.replaceTags(LocaleController.getString(org.telegram.messenger.R.string.Gift2ActionInfoChannelNoConvert));
                                            } else {
                                                charSequenceReplaceTags4 = AndroidUtilities.replaceTags(LocaleController.getString(org.telegram.messenger.R.string.Gift2ActionInfoChannelNoConvert));
                                            }
                                        } else if (z4) {
                                            if (!tL_messageActionStarGift.converted) {
                                                if (tL_messageActionStarGift.can_upgrade) {
                                                    charSequenceReplaceTags4 = AndroidUtilities.replaceTags(LocaleController.getString(org.telegram.messenger.R.string.Gift2ActionSelfInfoUpgrade));
                                                } else {
                                                    charSequenceReplaceTags4 = AndroidUtilities.replaceTags(LocaleController.getString(org.telegram.messenger.R.string.Gift2ActionSelfInfoNoConvert));
                                                }
                                            } else if (tL_messageActionStarGift.can_upgrade) {
                                                charSequenceReplaceTags4 = AndroidUtilities.replaceTags(LocaleController.getString(org.telegram.messenger.R.string.Gift2ActionSelfInfoUpgrade));
                                            } else {
                                                charSequenceReplaceTags4 = AndroidUtilities.replaceTags(LocaleController.getString(org.telegram.messenger.R.string.Gift2ActionSelfInfoNoConvert));
                                            }
                                        } else if (z5) {
                                            if (messageObject.isOutOwner()) {
                                                string6 = LocaleController.formatString(org.telegram.messenger.R.string.Gift2ActionUpgradeOut, UserObject.getForcedFirstName(user5));
                                            } else {
                                                string6 = LocaleController.getString(org.telegram.messenger.R.string.Gift2ActionUpgrade);
                                            }
                                            charSequenceReplaceTags4 = AndroidUtilities.replaceTags(string6);
                                        } else if (messageObject.isOutOwner()) {
                                            if (!z6) {
                                                if (tL_messageActionStarGift.can_upgrade) {
                                                    charSequenceReplaceTags4 = AndroidUtilities.replaceTags(LocaleController.formatString(org.telegram.messenger.R.string.Gift2ActionOutInfoUpgrade, UserObject.getForcedFirstName(user5)));
                                                } else {
                                                    charSequenceReplaceTags4 = AndroidUtilities.replaceTags(LocaleController.formatString(org.telegram.messenger.R.string.Gift2ActionOutInfoNoConvert, UserObject.getForcedFirstName(user5)));
                                                }
                                            } else if (tL_messageActionStarGift.can_upgrade) {
                                                charSequenceReplaceTags4 = AndroidUtilities.replaceTags(LocaleController.formatString(org.telegram.messenger.R.string.Gift2ActionOutInfoUpgrade, UserObject.getForcedFirstName(user5)));
                                            } else {
                                                charSequenceReplaceTags4 = AndroidUtilities.replaceTags(LocaleController.formatString(org.telegram.messenger.R.string.Gift2ActionOutInfoNoConvert, UserObject.getForcedFirstName(user5)));
                                            }
                                        } else if (tL_messageActionStarGift.converted) {
                                            charSequenceReplaceTags4 = LocaleController.formatPluralStringComma("Gift2ActionConvertedInfo", (int) j);
                                        } else if (tL_messageActionStarGift.saved) {
                                            if (!z6) {
                                                charSequenceReplaceTags4 = LocaleController.getString(org.telegram.messenger.R.string.Gift2ActionBotSavedInfo);
                                            } else {
                                                charSequenceReplaceTags4 = LocaleController.getString(org.telegram.messenger.R.string.Gift2ActionSavedInfo);
                                            }
                                        } else if (!z6) {
                                            charSequenceReplaceTags4 = LocaleController.getString(org.telegram.messenger.R.string.Gift2ActionBotInfo);
                                        } else {
                                            charSequenceReplaceTags4 = AndroidUtilities.replaceTags(LocaleController.formatPluralStringComma("Gift2ActionInfo", (int) j));
                                        }
                                    } else if (tL_messageActionStarGift.auction_acquired) {
                                        charSequenceReplaceTags4 = LocaleController.formatString(org.telegram.messenger.R.string.Gift2ActionWonActionText, LocaleController.formatNumber(tL_messageActionStarGift.gift.stars + tL_messageActionStarGift.upgrade_stars, ','));
                                    } else if (z3) {
                                        if (tL_messageActionStarGift.converted) {
                                            charSequenceReplaceTags4 = LocaleController.formatPluralStringComma("Gift2ActionConvertedInfo", (int) j);
                                        } else if (!z6) {
                                            charSequenceReplaceTags4 = AndroidUtilities.replaceTags(LocaleController.getString(org.telegram.messenger.R.string.Gift2ActionInfoChannelNoConvert));
                                        } else {
                                            charSequenceReplaceTags4 = AndroidUtilities.replaceTags(LocaleController.getString(org.telegram.messenger.R.string.Gift2ActionInfoChannelNoConvert));
                                        }
                                    } else if (z4) {
                                        if (!tL_messageActionStarGift.converted) {
                                            if (tL_messageActionStarGift.can_upgrade) {
                                                charSequenceReplaceTags4 = AndroidUtilities.replaceTags(LocaleController.getString(org.telegram.messenger.R.string.Gift2ActionSelfInfoUpgrade));
                                            } else {
                                                charSequenceReplaceTags4 = AndroidUtilities.replaceTags(LocaleController.getString(org.telegram.messenger.R.string.Gift2ActionSelfInfoNoConvert));
                                            }
                                        } else if (tL_messageActionStarGift.can_upgrade) {
                                            charSequenceReplaceTags4 = AndroidUtilities.replaceTags(LocaleController.getString(org.telegram.messenger.R.string.Gift2ActionSelfInfoUpgrade));
                                        } else {
                                            charSequenceReplaceTags4 = AndroidUtilities.replaceTags(LocaleController.getString(org.telegram.messenger.R.string.Gift2ActionSelfInfoNoConvert));
                                        }
                                    } else if (z5) {
                                        if (messageObject.isOutOwner()) {
                                            string6 = LocaleController.formatString(org.telegram.messenger.R.string.Gift2ActionUpgradeOut, UserObject.getForcedFirstName(user5));
                                        } else {
                                            string6 = LocaleController.getString(org.telegram.messenger.R.string.Gift2ActionUpgrade);
                                        }
                                        charSequenceReplaceTags4 = AndroidUtilities.replaceTags(string6);
                                    } else if (messageObject.isOutOwner()) {
                                        if (!z6) {
                                            if (tL_messageActionStarGift.can_upgrade) {
                                                charSequenceReplaceTags4 = AndroidUtilities.replaceTags(LocaleController.formatString(org.telegram.messenger.R.string.Gift2ActionOutInfoUpgrade, UserObject.getForcedFirstName(user5)));
                                            } else {
                                                charSequenceReplaceTags4 = AndroidUtilities.replaceTags(LocaleController.formatString(org.telegram.messenger.R.string.Gift2ActionOutInfoNoConvert, UserObject.getForcedFirstName(user5)));
                                            }
                                        } else if (tL_messageActionStarGift.can_upgrade) {
                                            charSequenceReplaceTags4 = AndroidUtilities.replaceTags(LocaleController.formatString(org.telegram.messenger.R.string.Gift2ActionOutInfoUpgrade, UserObject.getForcedFirstName(user5)));
                                        } else {
                                            charSequenceReplaceTags4 = AndroidUtilities.replaceTags(LocaleController.formatString(org.telegram.messenger.R.string.Gift2ActionOutInfoNoConvert, UserObject.getForcedFirstName(user5)));
                                        }
                                    } else if (tL_messageActionStarGift.converted) {
                                        charSequenceReplaceTags4 = LocaleController.formatPluralStringComma("Gift2ActionConvertedInfo", (int) j);
                                    } else if (tL_messageActionStarGift.saved) {
                                        if (!z6) {
                                            charSequenceReplaceTags4 = LocaleController.getString(org.telegram.messenger.R.string.Gift2ActionBotSavedInfo);
                                        } else {
                                            charSequenceReplaceTags4 = LocaleController.getString(org.telegram.messenger.R.string.Gift2ActionSavedInfo);
                                        }
                                    } else if (!z6) {
                                        charSequenceReplaceTags4 = LocaleController.getString(org.telegram.messenger.R.string.Gift2ActionBotInfo);
                                    } else {
                                        charSequenceReplaceTags4 = AndroidUtilities.replaceTags(LocaleController.formatPluralStringComma("Gift2ActionInfo", (int) j));
                                    }
                                }
                                starGift2 = tL_messageActionStarGift.gift;
                                if (starGift2 == null) {
                                    string7 = null;
                                } else {
                                    string7 = null;
                                }
                                string8 = LocaleController.getString(org.telegram.messenger.R.string.ActionGiftStarsView);
                                if (messageObject.isOutOwner()) {
                                    charSequence3 = string8;
                                    charSequence3 = string8;
                                    charSequence3 = string8;
                                    if (!messageObject.isOutOwner()) {
                                        SpannableStringBuilder spannableStringBuilder8 = new SpannableStringBuilder();
                                        spannableStringBuilder8.append((CharSequence) "^  ");
                                        ColoredImageSpan coloredImageSpan2 = new ColoredImageSpan(org.telegram.messenger.R.drawable.gift_unpack);
                                        coloredImageSpan2.setScale(0.8f, 0.8f);
                                        spannableStringBuilder8.setSpan(coloredImageSpan2, 0, 1, 33);
                                        spannableStringBuilder8.append((CharSequence) LocaleController.getString(org.telegram.messenger.R.string.Gift2Unpack));
                                        charSequence3 = spannableStringBuilder8;
                                    }
                                } else {
                                    charSequence3 = string8;
                                    charSequence3 = string8;
                                    charSequence3 = string8;
                                    if (!messageObject.isOutOwner()) {
                                        SpannableStringBuilder spannableStringBuilder9 = new SpannableStringBuilder();
                                        spannableStringBuilder9.append((CharSequence) "^  ");
                                        ColoredImageSpan coloredImageSpan3 = new ColoredImageSpan(org.telegram.messenger.R.drawable.gift_unpack);
                                        coloredImageSpan3.setScale(0.8f, 0.8f);
                                        spannableStringBuilder9.setSpan(coloredImageSpan3, 0, 1, 33);
                                        spannableStringBuilder9.append((CharSequence) LocaleController.getString(org.telegram.messenger.R.string.Gift2Unpack));
                                        charSequence3 = spannableStringBuilder9;
                                    }
                                }
                                CharSequence charSequence5 = charSequence3;
                                starGift3 = tL_messageActionStarGift.gift;
                                if (starGift3 != null) {
                                    charSequenceReplaceSingleTagToLink = null;
                                } else {
                                    charSequenceReplaceSingleTagToLink = null;
                                }
                                createGiftPremiumLayouts(spannableStringBuilder3, null, charSequenceReplaceSingleTagToLink, charSequenceReplaceTags4, false, charSequence5, 11, string7, this.giftRectSize, true, false);
                            } else if (messageAction2 instanceof TLRPC.TL_messageActionGiftTon) {
                                createGiftPremiumLayouts(LocaleController.getString(org.telegram.messenger.R.string.ActionGiftTonTitle), null, null, this.currentMessageObject.messageText, false, LocaleController.getString(org.telegram.messenger.R.string.ActionGiftStarsView), 11, null, this.giftRectSize, true, false);
                                this.textLayout = null;
                                this.textHeight = 0;
                                this.titleLayout = null;
                                this.titleHeight = 0;
                                this.textY = 0;
                            } else {
                                createGiftPremiumLayouts(LocaleController.getString(org.telegram.messenger.R.string.ActionStarGiveawayPrizeTitle), null, null, this.currentMessageObject.messageText, false, LocaleController.getString(org.telegram.messenger.R.string.ActionGiftStarsView), 11, null, this.giftRectSize, true, false);
                                this.textLayout = null;
                                this.textHeight = 0;
                                this.titleLayout = null;
                                this.titleHeight = 0;
                                this.textY = 0;
                            }
                        } else {
                            dialogId = 0;
                            if (i == 33) {
                                tL_messageActionStarGiftPurchaseOffer = (TLRPC.TL_messageActionStarGiftPurchaseOffer) message3.action;
                                spannableStringBuilder2 = new SpannableStringBuilder(charSequenceCreateActionTextWithTopic);
                                spannableStringBuilder2.append((CharSequence) "\n\n");
                                if (tL_messageActionStarGiftPurchaseOffer.accepted) {
                                    spannableStringBuilder2.append((CharSequence) AndroidUtilities.replaceTags(LocaleController.getString(org.telegram.messenger.R.string.GiftOfferStatusAccepted)));
                                } else if (tL_messageActionStarGiftPurchaseOffer.declined) {
                                    spannableStringBuilder2.append((CharSequence) AndroidUtilities.replaceTags(LocaleController.getString(org.telegram.messenger.R.string.GiftOfferStatusRejected)));
                                } else {
                                    iMax = Math.max(0, tL_messageActionStarGiftPurchaseOffer.expires_at - ConnectionsManager.getInstance(this.currentAccount).getCurrentTime());
                                    if (iMax == 0) {
                                        spannableStringBuilder2.append((CharSequence) AndroidUtilities.replaceTags(LocaleController.getString(org.telegram.messenger.R.string.GiftOfferStatusExpired)));
                                    } else {
                                        shortDuration2 = LocaleController.formatShortDuration2(iMax);
                                        if (shortDuration2.endsWith(".")) {
                                            i3 = 1;
                                            c = 0;
                                            shortDuration2 = shortDuration2.substring(0, shortDuration2.length() - 1);
                                        } else {
                                            i3 = 1;
                                            c = 0;
                                        }
                                        int i9 = org.telegram.messenger.R.string.GiftOfferStatusPending;
                                        Object[] objArr2 = new Object[i3];
                                        objArr2[c] = shortDuration2;
                                        spannableStringBuilder2.append((CharSequence) AndroidUtilities.replaceTags(LocaleController.formatString(i9, objArr2)));
                                    }
                                }
                                createGiftPremiumLayouts(null, null, null, spannableStringBuilder2, false, null, 11, null, this.giftRectSize, false, false);
                                this.textLayout = null;
                                this.textHeight = 0;
                                this.titleLayout = null;
                                this.titleHeight = 0;
                                this.textY = 0;
                                this.giftRectEmpty = false;
                            } else if (i == 34) {
                                createGiftPremiumLayouts(null, null, null, charSequenceCreateActionTextWithTopic, false, null, 11, null, this.giftRectSize, false, true);
                                this.textLayout = null;
                                this.textHeight = 0;
                                this.titleLayout = null;
                                this.titleHeight = 0;
                                this.textY = 0;
                                this.giftRectEmpty = true;
                            } else if (i == 35) {
                                tL_messageActionNoForwardsRequest = (TLRPC.TL_messageActionNoForwardsRequest) message3.action;
                                spannableStringBuilder = new SpannableStringBuilder();
                                shortName = DialogObject.getShortName(MessagesController.getInstance(this.currentAccount).getUser(Long.valueOf(messageObject.getDialogId())));
                                if (tL_messageActionNoForwardsRequest.new_value) {
                                    if (messageObject.isOut()) {
                                        charSequenceReplaceTags3 = LocaleController.getString(org.telegram.messenger.R.string.SharingOfferDisableHeaderYou);
                                    } else {
                                        charSequenceReplaceTags3 = AndroidUtilities.replaceTags(LocaleController.formatString(org.telegram.messenger.R.string.SharingOfferDisableHeaderOther, shortName));
                                    }
                                    spannableStringBuilder.append(charSequenceReplaceTags3);
                                } else {
                                    if (messageObject.isOut()) {
                                        charSequenceReplaceTags2 = LocaleController.getString(org.telegram.messenger.R.string.SharingOfferEnableHeaderYou);
                                    } else {
                                        charSequenceReplaceTags2 = AndroidUtilities.replaceTags(LocaleController.formatString(org.telegram.messenger.R.string.SharingOfferEnableHeaderOther, shortName));
                                    }
                                    spannableStringBuilder.append(charSequenceReplaceTags2);
                                }
                                if (tL_messageActionNoForwardsRequest.new_value) {
                                    spannableStringBuilder.append((CharSequence) "\n\n");
                                    spannableStringBuilder.append(createOption(LocaleController.getString(org.telegram.messenger.R.string.SharingOfferDisable1), org.telegram.messenger.R.drawable.floating_check));
                                    spannableStringBuilder.append((CharSequence) "\n\n");
                                    spannableStringBuilder.append(createOption(LocaleController.getString(org.telegram.messenger.R.string.SharingOfferDisable2), org.telegram.messenger.R.drawable.floating_check));
                                    spannableStringBuilder.append((CharSequence) "\n\n");
                                    spannableStringBuilder.append(createOption(LocaleController.getString(org.telegram.messenger.R.string.SharingOfferDisable3), org.telegram.messenger.R.drawable.floating_check));
                                    spannableStringBuilder.append((CharSequence) "\n\n");
                                    spannableStringBuilder.append(createOption(LocaleController.getString(org.telegram.messenger.R.string.SharingOfferDisable4), org.telegram.messenger.R.drawable.floating_check));
                                } else {
                                    spannableStringBuilder.append((CharSequence) "\n\n");
                                    spannableStringBuilder.append(createOption(LocaleController.getString(org.telegram.messenger.R.string.SharingOfferEnable1), org.telegram.messenger.R.drawable.floating_check));
                                    spannableStringBuilder.append((CharSequence) "\n\n");
                                    spannableStringBuilder.append(createOption(LocaleController.getString(org.telegram.messenger.R.string.SharingOfferEnable2), org.telegram.messenger.R.drawable.floating_check));
                                    spannableStringBuilder.append((CharSequence) "\n\n");
                                    spannableStringBuilder.append(createOption(LocaleController.getString(org.telegram.messenger.R.string.SharingOfferEnable3), org.telegram.messenger.R.drawable.floating_check));
                                    spannableStringBuilder.append((CharSequence) "\n\n");
                                    spannableStringBuilder.append(createOption(LocaleController.getString(org.telegram.messenger.R.string.SharingOfferEnable4), org.telegram.messenger.R.drawable.floating_check));
                                }
                                createGiftPremiumLayouts(null, null, null, spannableStringBuilder, false, null, 11, null, this.giftRectSize, false, true);
                                this.textLayout = null;
                                this.textHeight = 0;
                                this.titleLayout = null;
                                this.titleHeight = 0;
                                this.textY = 0;
                                this.giftRectEmpty = true;
                            } else if (i == 31) {
                                TL_stars.StarGift starGift4 = ((TLRPC.TL_chatThemeUniqueGift) ((TLRPC.TL_messageActionSetChatTheme) message3.action).theme).gift;
                                str = starGift4.title + " #" + LocaleController.formatNumber(starGift4.num, ',');
                                fromChatId = messageObject.getFromChatId();
                                if (UserConfig.getInstance(this.currentAccount).getClientUserId() == fromChatId) {
                                    string5 = LocaleController.formatString(org.telegram.messenger.R.string.GiftThemesSetByYou, str);
                                } else {
                                    string5 = LocaleController.formatString(org.telegram.messenger.R.string.GiftThemesSetByOther, DialogObject.getShortName(this.currentAccount, fromChatId), str);
                                }
                                createGiftPremiumLayouts(null, null, null, AndroidUtilities.replaceTags(string5), false, LocaleController.getString(org.telegram.messenger.R.string.GiftThemesSetActionView), 11, null, this.giftRectSize, true, false);
                                this.textLayout = null;
                                this.textHeight = 0;
                                this.titleLayout = null;
                                this.titleHeight = 0;
                                this.textY = 0;
                            } else if (i == 18) {
                                messageAction = message3.action;
                                if (messageAction instanceof TLRPC.TL_messageActionGiftPremium) {
                                    tL_textWithEntities = ((TLRPC.TL_messageActionGiftPremium) messageAction).message;
                                } else if (messageAction instanceof TLRPC.TL_messageActionGiftCode) {
                                    tL_textWithEntities = ((TLRPC.TL_messageActionGiftCode) messageAction).message;
                                } else {
                                    tL_textWithEntities = null;
                                }
                                if (tL_textWithEntities != null || TextUtils.isEmpty(tL_textWithEntities.text)) {
                                    string4 = null;
                                } else {
                                    SpannableStringBuilder spannableStringBuilder10 = new SpannableStringBuilder(tL_textWithEntities.text);
                                    this.giftTextPaint.setTextSize(AndroidUtilities.dp(13.0f));
                                    MessageObject.addEntitiesToText(spannableStringBuilder10, tL_textWithEntities.entities, false, false, true, true);
                                    string4 = MessageObject.replaceAnimatedEmoji(Emoji.replaceEmoji((CharSequence) spannableStringBuilder10, this.giftTextPaint.getFontMetricsInt(), false, (int[]) null), tL_textWithEntities.entities, this.giftTextPaint.getFontMetricsInt());
                                }
                                if (string4 == null) {
                                    string4 = LocaleController.getString(org.telegram.messenger.R.string.ActionGiftPremiumText);
                                }
                                CharSequence charSequence6 = string4;
                                if (isGiftCode() || isSelfGiftCode()) {
                                    i2 = org.telegram.messenger.R.string.ActionGiftPremiumView;
                                } else {
                                    i2 = org.telegram.messenger.R.string.GiftPremiumUseGiftBtn;
                                }
                                createGiftPremiumLayouts(LocaleController.formatPluralStringComma("ActionGiftPremiumTitle2", messageObject.messageOwner.action.months), null, null, charSequence6, true, LocaleController.getString(i2), 11, null, this.giftRectSize, false, false);
                            } else if (i == 21) {
                                tL_messageActionSuggestProfilePhoto = (TLRPC.TL_messageActionSuggestProfilePhoto) message3.action;
                                MessagesController messagesController = MessagesController.getInstance(this.currentAccount);
                                if (!messageObject.isOutOwner()) {
                                    dialogId = messageObject.getDialogId();
                                }
                                user3 = messagesController.getUser(Long.valueOf(dialogId));
                                if (tL_messageActionSuggestProfilePhoto.video && ((photo = tL_messageActionSuggestProfilePhoto.photo) == null || (arrayList2 = photo.video_sizes) == null || arrayList2.isEmpty())) {
                                    z2 = false;
                                } else {
                                    z2 = true;
                                }
                                if (user3.id == UserConfig.getInstance(this.currentAccount).clientUserId) {
                                    user4 = MessagesController.getInstance(this.currentAccount).getUser(Long.valueOf(messageObject.getDialogId()));
                                    if (z2) {
                                        string2 = LocaleController.formatString(org.telegram.messenger.R.string.ActionSuggestVideoFromYouDescription, user4.first_name);
                                    } else {
                                        string2 = LocaleController.formatString(org.telegram.messenger.R.string.ActionSuggestPhotoFromYouDescription, user4.first_name);
                                    }
                                } else if (z2) {
                                    string2 = LocaleController.formatString(org.telegram.messenger.R.string.ActionSuggestVideoToYouDescription, user3.first_name);
                                } else {
                                    string2 = LocaleController.formatString(org.telegram.messenger.R.string.ActionSuggestPhotoToYouDescription, user3.first_name);
                                }
                                CharSequence charSequence7 = string2;
                                if (!tL_messageActionSuggestProfilePhoto.video || ((arrayList = tL_messageActionSuggestProfilePhoto.photo.video_sizes) != null && !arrayList.isEmpty())) {
                                    string3 = LocaleController.getString(org.telegram.messenger.R.string.ViewVideoAction);
                                } else {
                                    string3 = LocaleController.getString(org.telegram.messenger.R.string.ViewPhotoAction);
                                }
                                createGiftPremiumLayouts(null, null, null, charSequence7, false, string3, 11, null, this.giftRectSize, true, false);
                                this.textLayout = null;
                                this.textHeight = 0;
                                this.titleLayout = null;
                                this.titleHeight = 0;
                                this.textY = 0;
                            } else if (i == 22) {
                                MessagesController messagesController2 = MessagesController.getInstance(this.currentAccount);
                                if (messageObject.isOutOwner()) {
                                    dialogId2 = 0;
                                } else {
                                    dialogId2 = messageObject.getDialogId();
                                }
                                user2 = messagesController2.getUser(Long.valueOf(dialogId2));
                                if (messageObject.getDialogId() < 0) {
                                    charSequence2 = messageObject.messageText;
                                } else {
                                    if (messageObject.isOutOwner() && messageObject.isWallpaperForBoth() && messageObject.isCurrentWallpaper()) {
                                        charSequence = messageObject.messageText;
                                        string = LocaleController.getString(org.telegram.messenger.R.string.RemoveWallpaperAction);
                                        z = false;
                                    } else {
                                        if (user2 == null && user2.id == UserConfig.getInstance(this.currentAccount).clientUserId) {
                                            charSequence2 = messageObject.messageText;
                                        } else {
                                            charSequence = messageObject.messageText;
                                            string = LocaleController.getString(org.telegram.messenger.R.string.ViewWallpaperAction);
                                        }
                                        z = true;
                                    }
                                    createGiftPremiumLayouts(null, null, null, charSequence, false, string, 11, null, this.giftRectSize, z, false);
                                    this.textLayout = null;
                                    this.textHeight = 0;
                                    this.titleLayout = null;
                                    this.titleHeight = 0;
                                    this.textY = 0;
                                }
                                charSequence = charSequence2;
                                string = null;
                                z = true;
                                createGiftPremiumLayouts(null, null, null, charSequence, false, string, 11, null, this.giftRectSize, z, false);
                                this.textLayout = null;
                                this.textHeight = 0;
                                this.titleLayout = null;
                                this.titleHeight = 0;
                                this.textY = 0;
                            } else if (messageObject.isStoryMention()) {
                                user = MessagesController.getInstance(this.currentAccount).getUser(Long.valueOf(messageObject.messageOwner.media.user_id));
                                if (user.self) {
                                    charSequenceReplaceTags = AndroidUtilities.replaceTags(LocaleController.formatString(org.telegram.messenger.R.string.StoryYouMentionedTitle, MessagesController.getInstance(this.currentAccount).getUser(Long.valueOf(messageObject.getDialogId())).first_name));
                                } else {
                                    charSequenceReplaceTags = AndroidUtilities.replaceTags(LocaleController.formatString(org.telegram.messenger.R.string.StoryMentionedTitle, user.first_name));
                                }
                                createGiftPremiumLayouts(null, null, null, charSequenceReplaceTags, false, LocaleController.getString(org.telegram.messenger.R.string.StoryMentionedAction), 11, null, this.giftRectSize, true, false);
                                this.textLayout = null;
                                this.textHeight = 0;
                                this.titleLayout = null;
                                this.titleHeight = 0;
                                this.textY = 0;
                            }
                        }
                    }
                } else {
                    i = messageObject.type;
                    if (i == 11) {
                        ImageReceiver imageReceiver2 = this.imageReceiver;
                        float f2 = (this.previousWidth - AndroidUtilities.roundMessageSize) / 2.0f;
                        float fDp2 = this.textHeight + AndroidUtilities.dp(19.0f);
                        int i10 = AndroidUtilities.roundMessageSize;
                        imageReceiver2.setImageCoords(f2, fDp2, i10, i10);
                    } else if (i == 25) {
                        createGiftPremiumChannelLayouts();
                    } else if (i == 30) {
                        user5 = MessagesController.getInstance(this.currentAccount).getUser(Long.valueOf(this.currentMessageObject.getDialogId()));
                        messageAction2 = messageObject.messageOwner.action;
                        if (messageAction2 instanceof TLRPC.TL_messageActionGiftStars) {
                            CharSequence pluralStringComma2 = LocaleController.formatPluralStringComma("ActionGiftStarsTitle", (int) ((TLRPC.TL_messageActionGiftStars) messageAction2).stars);
                            if (this.currentMessageObject.isOutOwner()) {
                                string9 = LocaleController.formatString(org.telegram.messenger.R.string.ActionGiftStarsSubtitle, UserObject.getForcedFirstName(user5));
                            } else {
                                string9 = LocaleController.getString(org.telegram.messenger.R.string.ActionGiftStarsSubtitleYou);
                            }
                            createGiftPremiumLayouts(pluralStringComma2, null, null, AndroidUtilities.replaceTags(string9), false, LocaleController.getString(org.telegram.messenger.R.string.ActionGiftStarsView), 11, null, this.giftRectSize, true, false);
                        } else if (!(messageAction2 instanceof TLRPC.TL_messageActionStarGiftUnique)) {
                            if (messageAction2 instanceof TLRPC.TL_messageActionStarGift) {
                                tL_messageActionStarGift = (TLRPC.TL_messageActionStarGift) messageAction2;
                                j = tL_messageActionStarGift.convert_stars;
                                clientUserId = UserConfig.getInstance(this.currentAccount).getClientUserId();
                                peer = tL_messageActionStarGift.peer;
                                if (peer != null) {
                                    z3 = false;
                                } else {
                                    z3 = false;
                                }
                                if (messageObject.getDialogId() == clientUserId) {
                                    z4 = false;
                                } else {
                                    z4 = false;
                                }
                                fromChatId2 = messageObject.getFromChatId();
                                if (!tL_messageActionStarGift.prepaid_upgrade) {
                                    fromChatId2 = DialogObject.getPeerDialogId(peer2);
                                }
                                spannableStringBuilder3 = new SpannableStringBuilder();
                                userOrChat = MessagesController.getInstance(this.currentAccount).getUserOrChat(fromChatId2);
                                peerDialogId = DialogObject.getPeerDialogId(tL_messageActionStarGift.to_id);
                                TLObject userOrChat3 = MessagesController.getInstance(this.currentAccount).getUserOrChat(peerDialogId);
                                if (tL_messageActionStarGift.can_upgrade) {
                                    j2 = peerDialogId;
                                    z5 = false;
                                } else {
                                    j2 = peerDialogId;
                                    z5 = false;
                                }
                                if (j2 == 0) {
                                    if (z4) {
                                        if (tL_messageActionStarGift.gift_num <= 0) {
                                            spannableStringBuilder3.append((CharSequence) LocaleController.getString(org.telegram.messenger.R.string.Gift2ActionSelfTitle));
                                        } else {
                                            spannableStringBuilder3.append((CharSequence) LocaleController.getString(org.telegram.messenger.R.string.Gift2ActionSelfTitle));
                                        }
                                    } else {
                                        if (tL_messageActionStarGift.prepaid_upgrade) {
                                            i4 = org.telegram.messenger.R.string.Gift2ActionUpgradeTitle;
                                        } else {
                                            i4 = org.telegram.messenger.R.string.Gift2ActionTitle;
                                        }
                                        spannableStringBuilder3.append((CharSequence) LocaleController.getString(i4)).append((CharSequence) " ");
                                        if (DialogObject.hasPhoto(userOrChat)) {
                                            spannableStringBuilder3.append((CharSequence) "a ");
                                            AvatarSpan avatarSpan6 = new AvatarSpan(this, this.currentAccount, 18.0f);
                                            avatarSpan6.setObject(userOrChat);
                                            spannableStringBuilder3.setSpan(avatarSpan6, spannableStringBuilder3.length() - 2, spannableStringBuilder3.length() - 1, 33);
                                        }
                                        spannableStringBuilder3.append((CharSequence) DialogObject.getShortName(userOrChat));
                                    }
                                } else if (z4) {
                                    if (tL_messageActionStarGift.gift_num <= 0) {
                                        spannableStringBuilder3.append((CharSequence) LocaleController.getString(org.telegram.messenger.R.string.Gift2ActionSelfTitle));
                                    } else {
                                        spannableStringBuilder3.append((CharSequence) LocaleController.getString(org.telegram.messenger.R.string.Gift2ActionSelfTitle));
                                    }
                                } else {
                                    if (tL_messageActionStarGift.prepaid_upgrade) {
                                        i4 = org.telegram.messenger.R.string.Gift2ActionUpgradeTitle;
                                    } else {
                                        i4 = org.telegram.messenger.R.string.Gift2ActionTitle;
                                    }
                                    spannableStringBuilder3.append((CharSequence) LocaleController.getString(i4)).append((CharSequence) " ");
                                    if (DialogObject.hasPhoto(userOrChat)) {
                                        spannableStringBuilder3.append((CharSequence) "a ");
                                        AvatarSpan avatarSpan7 = new AvatarSpan(this, this.currentAccount, 18.0f);
                                        avatarSpan7.setObject(userOrChat);
                                        spannableStringBuilder3.setSpan(avatarSpan7, spannableStringBuilder3.length() - 2, spannableStringBuilder3.length() - 1, 33);
                                    }
                                    spannableStringBuilder3.append((CharSequence) DialogObject.getShortName(userOrChat));
                                }
                                if (messageObject.isOutOwner()) {
                                }
                                if (tL_messageActionStarGift.refunded) {
                                    charSequenceReplaceTags4 = LocaleController.getString(org.telegram.messenger.R.string.Gift2ActionConvertRefundedText);
                                } else {
                                    tL_textWithEntities2 = tL_messageActionStarGift.message;
                                    if (tL_textWithEntities2 == null) {
                                        if (tL_messageActionStarGift.auction_acquired) {
                                            charSequenceReplaceTags4 = LocaleController.formatString(org.telegram.messenger.R.string.Gift2ActionWonActionText, LocaleController.formatNumber(tL_messageActionStarGift.gift.stars + tL_messageActionStarGift.upgrade_stars, ','));
                                        } else if (z3) {
                                            if (tL_messageActionStarGift.converted) {
                                                charSequenceReplaceTags4 = LocaleController.formatPluralStringComma("Gift2ActionConvertedInfo", (int) j);
                                            } else if (!z6) {
                                                charSequenceReplaceTags4 = AndroidUtilities.replaceTags(LocaleController.getString(org.telegram.messenger.R.string.Gift2ActionInfoChannelNoConvert));
                                            } else {
                                                charSequenceReplaceTags4 = AndroidUtilities.replaceTags(LocaleController.getString(org.telegram.messenger.R.string.Gift2ActionInfoChannelNoConvert));
                                            }
                                        } else if (z4) {
                                            if (!tL_messageActionStarGift.converted) {
                                                if (tL_messageActionStarGift.can_upgrade) {
                                                    charSequenceReplaceTags4 = AndroidUtilities.replaceTags(LocaleController.getString(org.telegram.messenger.R.string.Gift2ActionSelfInfoUpgrade));
                                                } else {
                                                    charSequenceReplaceTags4 = AndroidUtilities.replaceTags(LocaleController.getString(org.telegram.messenger.R.string.Gift2ActionSelfInfoNoConvert));
                                                }
                                            } else if (tL_messageActionStarGift.can_upgrade) {
                                                charSequenceReplaceTags4 = AndroidUtilities.replaceTags(LocaleController.getString(org.telegram.messenger.R.string.Gift2ActionSelfInfoUpgrade));
                                            } else {
                                                charSequenceReplaceTags4 = AndroidUtilities.replaceTags(LocaleController.getString(org.telegram.messenger.R.string.Gift2ActionSelfInfoNoConvert));
                                            }
                                        } else if (z5) {
                                            if (messageObject.isOutOwner()) {
                                                string6 = LocaleController.formatString(org.telegram.messenger.R.string.Gift2ActionUpgradeOut, UserObject.getForcedFirstName(user5));
                                            } else {
                                                string6 = LocaleController.getString(org.telegram.messenger.R.string.Gift2ActionUpgrade);
                                            }
                                            charSequenceReplaceTags4 = AndroidUtilities.replaceTags(string6);
                                        } else if (messageObject.isOutOwner()) {
                                            if (!z6) {
                                                if (tL_messageActionStarGift.can_upgrade) {
                                                    charSequenceReplaceTags4 = AndroidUtilities.replaceTags(LocaleController.formatString(org.telegram.messenger.R.string.Gift2ActionOutInfoUpgrade, UserObject.getForcedFirstName(user5)));
                                                } else {
                                                    charSequenceReplaceTags4 = AndroidUtilities.replaceTags(LocaleController.formatString(org.telegram.messenger.R.string.Gift2ActionOutInfoNoConvert, UserObject.getForcedFirstName(user5)));
                                                }
                                            } else if (tL_messageActionStarGift.can_upgrade) {
                                                charSequenceReplaceTags4 = AndroidUtilities.replaceTags(LocaleController.formatString(org.telegram.messenger.R.string.Gift2ActionOutInfoUpgrade, UserObject.getForcedFirstName(user5)));
                                            } else {
                                                charSequenceReplaceTags4 = AndroidUtilities.replaceTags(LocaleController.formatString(org.telegram.messenger.R.string.Gift2ActionOutInfoNoConvert, UserObject.getForcedFirstName(user5)));
                                            }
                                        } else if (tL_messageActionStarGift.converted) {
                                            charSequenceReplaceTags4 = LocaleController.formatPluralStringComma("Gift2ActionConvertedInfo", (int) j);
                                        } else if (tL_messageActionStarGift.saved) {
                                            if (!z6) {
                                                charSequenceReplaceTags4 = LocaleController.getString(org.telegram.messenger.R.string.Gift2ActionBotSavedInfo);
                                            } else {
                                                charSequenceReplaceTags4 = LocaleController.getString(org.telegram.messenger.R.string.Gift2ActionSavedInfo);
                                            }
                                        } else if (!z6) {
                                            charSequenceReplaceTags4 = LocaleController.getString(org.telegram.messenger.R.string.Gift2ActionBotInfo);
                                        } else {
                                            charSequenceReplaceTags4 = AndroidUtilities.replaceTags(LocaleController.formatPluralStringComma("Gift2ActionInfo", (int) j));
                                        }
                                    } else if (tL_messageActionStarGift.auction_acquired) {
                                        charSequenceReplaceTags4 = LocaleController.formatString(org.telegram.messenger.R.string.Gift2ActionWonActionText, LocaleController.formatNumber(tL_messageActionStarGift.gift.stars + tL_messageActionStarGift.upgrade_stars, ','));
                                    } else if (z3) {
                                        if (tL_messageActionStarGift.converted) {
                                            charSequenceReplaceTags4 = LocaleController.formatPluralStringComma("Gift2ActionConvertedInfo", (int) j);
                                        } else if (!z6) {
                                            charSequenceReplaceTags4 = AndroidUtilities.replaceTags(LocaleController.getString(org.telegram.messenger.R.string.Gift2ActionInfoChannelNoConvert));
                                        } else {
                                            charSequenceReplaceTags4 = AndroidUtilities.replaceTags(LocaleController.getString(org.telegram.messenger.R.string.Gift2ActionInfoChannelNoConvert));
                                        }
                                    } else if (z4) {
                                        if (!tL_messageActionStarGift.converted) {
                                            if (tL_messageActionStarGift.can_upgrade) {
                                                charSequenceReplaceTags4 = AndroidUtilities.replaceTags(LocaleController.getString(org.telegram.messenger.R.string.Gift2ActionSelfInfoUpgrade));
                                            } else {
                                                charSequenceReplaceTags4 = AndroidUtilities.replaceTags(LocaleController.getString(org.telegram.messenger.R.string.Gift2ActionSelfInfoNoConvert));
                                            }
                                        } else if (tL_messageActionStarGift.can_upgrade) {
                                            charSequenceReplaceTags4 = AndroidUtilities.replaceTags(LocaleController.getString(org.telegram.messenger.R.string.Gift2ActionSelfInfoUpgrade));
                                        } else {
                                            charSequenceReplaceTags4 = AndroidUtilities.replaceTags(LocaleController.getString(org.telegram.messenger.R.string.Gift2ActionSelfInfoNoConvert));
                                        }
                                    } else if (z5) {
                                        if (messageObject.isOutOwner()) {
                                            string6 = LocaleController.formatString(org.telegram.messenger.R.string.Gift2ActionUpgradeOut, UserObject.getForcedFirstName(user5));
                                        } else {
                                            string6 = LocaleController.getString(org.telegram.messenger.R.string.Gift2ActionUpgrade);
                                        }
                                        charSequenceReplaceTags4 = AndroidUtilities.replaceTags(string6);
                                    } else if (messageObject.isOutOwner()) {
                                        if (!z6) {
                                            if (tL_messageActionStarGift.can_upgrade) {
                                                charSequenceReplaceTags4 = AndroidUtilities.replaceTags(LocaleController.formatString(org.telegram.messenger.R.string.Gift2ActionOutInfoUpgrade, UserObject.getForcedFirstName(user5)));
                                            } else {
                                                charSequenceReplaceTags4 = AndroidUtilities.replaceTags(LocaleController.formatString(org.telegram.messenger.R.string.Gift2ActionOutInfoNoConvert, UserObject.getForcedFirstName(user5)));
                                            }
                                        } else if (tL_messageActionStarGift.can_upgrade) {
                                            charSequenceReplaceTags4 = AndroidUtilities.replaceTags(LocaleController.formatString(org.telegram.messenger.R.string.Gift2ActionOutInfoUpgrade, UserObject.getForcedFirstName(user5)));
                                        } else {
                                            charSequenceReplaceTags4 = AndroidUtilities.replaceTags(LocaleController.formatString(org.telegram.messenger.R.string.Gift2ActionOutInfoNoConvert, UserObject.getForcedFirstName(user5)));
                                        }
                                    } else if (tL_messageActionStarGift.converted) {
                                        charSequenceReplaceTags4 = LocaleController.formatPluralStringComma("Gift2ActionConvertedInfo", (int) j);
                                    } else if (tL_messageActionStarGift.saved) {
                                        if (!z6) {
                                            charSequenceReplaceTags4 = LocaleController.getString(org.telegram.messenger.R.string.Gift2ActionBotSavedInfo);
                                        } else {
                                            charSequenceReplaceTags4 = LocaleController.getString(org.telegram.messenger.R.string.Gift2ActionSavedInfo);
                                        }
                                    } else if (!z6) {
                                        charSequenceReplaceTags4 = LocaleController.getString(org.telegram.messenger.R.string.Gift2ActionBotInfo);
                                    } else {
                                        charSequenceReplaceTags4 = AndroidUtilities.replaceTags(LocaleController.formatPluralStringComma("Gift2ActionInfo", (int) j));
                                    }
                                }
                                starGift2 = tL_messageActionStarGift.gift;
                                if (starGift2 == null) {
                                    string7 = null;
                                } else {
                                    string7 = null;
                                }
                                string8 = LocaleController.getString(org.telegram.messenger.R.string.ActionGiftStarsView);
                                if (messageObject.isOutOwner()) {
                                    charSequence3 = string8;
                                    charSequence3 = string8;
                                    charSequence3 = string8;
                                    if (!messageObject.isOutOwner()) {
                                        SpannableStringBuilder spannableStringBuilder11 = new SpannableStringBuilder();
                                        spannableStringBuilder11.append((CharSequence) "^  ");
                                        ColoredImageSpan coloredImageSpan4 = new ColoredImageSpan(org.telegram.messenger.R.drawable.gift_unpack);
                                        coloredImageSpan4.setScale(0.8f, 0.8f);
                                        spannableStringBuilder11.setSpan(coloredImageSpan4, 0, 1, 33);
                                        spannableStringBuilder11.append((CharSequence) LocaleController.getString(org.telegram.messenger.R.string.Gift2Unpack));
                                        charSequence3 = spannableStringBuilder11;
                                    }
                                } else {
                                    charSequence3 = string8;
                                    charSequence3 = string8;
                                    charSequence3 = string8;
                                    if (!messageObject.isOutOwner()) {
                                        SpannableStringBuilder spannableStringBuilder12 = new SpannableStringBuilder();
                                        spannableStringBuilder12.append((CharSequence) "^  ");
                                        ColoredImageSpan coloredImageSpan5 = new ColoredImageSpan(org.telegram.messenger.R.drawable.gift_unpack);
                                        coloredImageSpan5.setScale(0.8f, 0.8f);
                                        spannableStringBuilder12.setSpan(coloredImageSpan5, 0, 1, 33);
                                        spannableStringBuilder12.append((CharSequence) LocaleController.getString(org.telegram.messenger.R.string.Gift2Unpack));
                                        charSequence3 = spannableStringBuilder12;
                                    }
                                }
                                CharSequence charSequence8 = charSequence3;
                                starGift3 = tL_messageActionStarGift.gift;
                                if (starGift3 != null) {
                                    charSequenceReplaceSingleTagToLink = null;
                                } else {
                                    charSequenceReplaceSingleTagToLink = null;
                                }
                                createGiftPremiumLayouts(spannableStringBuilder3, null, charSequenceReplaceSingleTagToLink, charSequenceReplaceTags4, false, charSequence8, 11, string7, this.giftRectSize, true, false);
                            } else if (messageAction2 instanceof TLRPC.TL_messageActionGiftTon) {
                                createGiftPremiumLayouts(LocaleController.getString(org.telegram.messenger.R.string.ActionGiftTonTitle), null, null, this.currentMessageObject.messageText, false, LocaleController.getString(org.telegram.messenger.R.string.ActionGiftStarsView), 11, null, this.giftRectSize, true, false);
                                this.textLayout = null;
                                this.textHeight = 0;
                                this.titleLayout = null;
                                this.titleHeight = 0;
                                this.textY = 0;
                            } else {
                                createGiftPremiumLayouts(LocaleController.getString(org.telegram.messenger.R.string.ActionStarGiveawayPrizeTitle), null, null, this.currentMessageObject.messageText, false, LocaleController.getString(org.telegram.messenger.R.string.ActionGiftStarsView), 11, null, this.giftRectSize, true, false);
                                this.textLayout = null;
                                this.textHeight = 0;
                                this.titleLayout = null;
                                this.titleHeight = 0;
                                this.textY = 0;
                            }
                        } else if (messageAction2 instanceof TLRPC.TL_messageActionStarGift) {
                            tL_messageActionStarGift = (TLRPC.TL_messageActionStarGift) messageAction2;
                            j = tL_messageActionStarGift.convert_stars;
                            clientUserId = UserConfig.getInstance(this.currentAccount).getClientUserId();
                            peer = tL_messageActionStarGift.peer;
                            if (peer != null) {
                                z3 = false;
                            } else {
                                z3 = false;
                            }
                            if (messageObject.getDialogId() == clientUserId) {
                                z4 = false;
                            } else {
                                z4 = false;
                            }
                            fromChatId2 = messageObject.getFromChatId();
                            if (!tL_messageActionStarGift.prepaid_upgrade) {
                                fromChatId2 = DialogObject.getPeerDialogId(peer2);
                            }
                            spannableStringBuilder3 = new SpannableStringBuilder();
                            userOrChat = MessagesController.getInstance(this.currentAccount).getUserOrChat(fromChatId2);
                            peerDialogId = DialogObject.getPeerDialogId(tL_messageActionStarGift.to_id);
                            TLObject userOrChat4 = MessagesController.getInstance(this.currentAccount).getUserOrChat(peerDialogId);
                            if (tL_messageActionStarGift.can_upgrade) {
                                j2 = peerDialogId;
                                z5 = false;
                            } else {
                                j2 = peerDialogId;
                                z5 = false;
                            }
                            if (j2 == 0) {
                                if (z4) {
                                    if (tL_messageActionStarGift.gift_num <= 0) {
                                        spannableStringBuilder3.append((CharSequence) LocaleController.getString(org.telegram.messenger.R.string.Gift2ActionSelfTitle));
                                    } else {
                                        spannableStringBuilder3.append((CharSequence) LocaleController.getString(org.telegram.messenger.R.string.Gift2ActionSelfTitle));
                                    }
                                } else {
                                    if (tL_messageActionStarGift.prepaid_upgrade) {
                                        i4 = org.telegram.messenger.R.string.Gift2ActionUpgradeTitle;
                                    } else {
                                        i4 = org.telegram.messenger.R.string.Gift2ActionTitle;
                                    }
                                    spannableStringBuilder3.append((CharSequence) LocaleController.getString(i4)).append((CharSequence) " ");
                                    if (DialogObject.hasPhoto(userOrChat)) {
                                        spannableStringBuilder3.append((CharSequence) "a ");
                                        AvatarSpan avatarSpan8 = new AvatarSpan(this, this.currentAccount, 18.0f);
                                        avatarSpan8.setObject(userOrChat);
                                        spannableStringBuilder3.setSpan(avatarSpan8, spannableStringBuilder3.length() - 2, spannableStringBuilder3.length() - 1, 33);
                                    }
                                    spannableStringBuilder3.append((CharSequence) DialogObject.getShortName(userOrChat));
                                }
                            } else if (z4) {
                                if (tL_messageActionStarGift.gift_num <= 0) {
                                    spannableStringBuilder3.append((CharSequence) LocaleController.getString(org.telegram.messenger.R.string.Gift2ActionSelfTitle));
                                } else {
                                    spannableStringBuilder3.append((CharSequence) LocaleController.getString(org.telegram.messenger.R.string.Gift2ActionSelfTitle));
                                }
                            } else {
                                if (tL_messageActionStarGift.prepaid_upgrade) {
                                    i4 = org.telegram.messenger.R.string.Gift2ActionUpgradeTitle;
                                } else {
                                    i4 = org.telegram.messenger.R.string.Gift2ActionTitle;
                                }
                                spannableStringBuilder3.append((CharSequence) LocaleController.getString(i4)).append((CharSequence) " ");
                                if (DialogObject.hasPhoto(userOrChat)) {
                                    spannableStringBuilder3.append((CharSequence) "a ");
                                    AvatarSpan avatarSpan9 = new AvatarSpan(this, this.currentAccount, 18.0f);
                                    avatarSpan9.setObject(userOrChat);
                                    spannableStringBuilder3.setSpan(avatarSpan9, spannableStringBuilder3.length() - 2, spannableStringBuilder3.length() - 1, 33);
                                }
                                spannableStringBuilder3.append((CharSequence) DialogObject.getShortName(userOrChat));
                            }
                            if (messageObject.isOutOwner()) {
                            }
                            if (tL_messageActionStarGift.refunded) {
                                charSequenceReplaceTags4 = LocaleController.getString(org.telegram.messenger.R.string.Gift2ActionConvertRefundedText);
                            } else {
                                tL_textWithEntities2 = tL_messageActionStarGift.message;
                                if (tL_textWithEntities2 == null) {
                                    if (tL_messageActionStarGift.auction_acquired) {
                                        charSequenceReplaceTags4 = LocaleController.formatString(org.telegram.messenger.R.string.Gift2ActionWonActionText, LocaleController.formatNumber(tL_messageActionStarGift.gift.stars + tL_messageActionStarGift.upgrade_stars, ','));
                                    } else if (z3) {
                                        if (tL_messageActionStarGift.converted) {
                                            charSequenceReplaceTags4 = LocaleController.formatPluralStringComma("Gift2ActionConvertedInfo", (int) j);
                                        } else if (!z6) {
                                            charSequenceReplaceTags4 = AndroidUtilities.replaceTags(LocaleController.getString(org.telegram.messenger.R.string.Gift2ActionInfoChannelNoConvert));
                                        } else {
                                            charSequenceReplaceTags4 = AndroidUtilities.replaceTags(LocaleController.getString(org.telegram.messenger.R.string.Gift2ActionInfoChannelNoConvert));
                                        }
                                    } else if (z4) {
                                        if (!tL_messageActionStarGift.converted) {
                                            if (tL_messageActionStarGift.can_upgrade) {
                                                charSequenceReplaceTags4 = AndroidUtilities.replaceTags(LocaleController.getString(org.telegram.messenger.R.string.Gift2ActionSelfInfoUpgrade));
                                            } else {
                                                charSequenceReplaceTags4 = AndroidUtilities.replaceTags(LocaleController.getString(org.telegram.messenger.R.string.Gift2ActionSelfInfoNoConvert));
                                            }
                                        } else if (tL_messageActionStarGift.can_upgrade) {
                                            charSequenceReplaceTags4 = AndroidUtilities.replaceTags(LocaleController.getString(org.telegram.messenger.R.string.Gift2ActionSelfInfoUpgrade));
                                        } else {
                                            charSequenceReplaceTags4 = AndroidUtilities.replaceTags(LocaleController.getString(org.telegram.messenger.R.string.Gift2ActionSelfInfoNoConvert));
                                        }
                                    } else if (z5) {
                                        if (messageObject.isOutOwner()) {
                                            string6 = LocaleController.formatString(org.telegram.messenger.R.string.Gift2ActionUpgradeOut, UserObject.getForcedFirstName(user5));
                                        } else {
                                            string6 = LocaleController.getString(org.telegram.messenger.R.string.Gift2ActionUpgrade);
                                        }
                                        charSequenceReplaceTags4 = AndroidUtilities.replaceTags(string6);
                                    } else if (messageObject.isOutOwner()) {
                                        if (!z6) {
                                            if (tL_messageActionStarGift.can_upgrade) {
                                                charSequenceReplaceTags4 = AndroidUtilities.replaceTags(LocaleController.formatString(org.telegram.messenger.R.string.Gift2ActionOutInfoUpgrade, UserObject.getForcedFirstName(user5)));
                                            } else {
                                                charSequenceReplaceTags4 = AndroidUtilities.replaceTags(LocaleController.formatString(org.telegram.messenger.R.string.Gift2ActionOutInfoNoConvert, UserObject.getForcedFirstName(user5)));
                                            }
                                        } else if (tL_messageActionStarGift.can_upgrade) {
                                            charSequenceReplaceTags4 = AndroidUtilities.replaceTags(LocaleController.formatString(org.telegram.messenger.R.string.Gift2ActionOutInfoUpgrade, UserObject.getForcedFirstName(user5)));
                                        } else {
                                            charSequenceReplaceTags4 = AndroidUtilities.replaceTags(LocaleController.formatString(org.telegram.messenger.R.string.Gift2ActionOutInfoNoConvert, UserObject.getForcedFirstName(user5)));
                                        }
                                    } else if (tL_messageActionStarGift.converted) {
                                        charSequenceReplaceTags4 = LocaleController.formatPluralStringComma("Gift2ActionConvertedInfo", (int) j);
                                    } else if (tL_messageActionStarGift.saved) {
                                        if (!z6) {
                                            charSequenceReplaceTags4 = LocaleController.getString(org.telegram.messenger.R.string.Gift2ActionBotSavedInfo);
                                        } else {
                                            charSequenceReplaceTags4 = LocaleController.getString(org.telegram.messenger.R.string.Gift2ActionSavedInfo);
                                        }
                                    } else if (!z6) {
                                        charSequenceReplaceTags4 = LocaleController.getString(org.telegram.messenger.R.string.Gift2ActionBotInfo);
                                    } else {
                                        charSequenceReplaceTags4 = AndroidUtilities.replaceTags(LocaleController.formatPluralStringComma("Gift2ActionInfo", (int) j));
                                    }
                                } else if (tL_messageActionStarGift.auction_acquired) {
                                    charSequenceReplaceTags4 = LocaleController.formatString(org.telegram.messenger.R.string.Gift2ActionWonActionText, LocaleController.formatNumber(tL_messageActionStarGift.gift.stars + tL_messageActionStarGift.upgrade_stars, ','));
                                } else if (z3) {
                                    if (tL_messageActionStarGift.converted) {
                                        charSequenceReplaceTags4 = LocaleController.formatPluralStringComma("Gift2ActionConvertedInfo", (int) j);
                                    } else if (!z6) {
                                        charSequenceReplaceTags4 = AndroidUtilities.replaceTags(LocaleController.getString(org.telegram.messenger.R.string.Gift2ActionInfoChannelNoConvert));
                                    } else {
                                        charSequenceReplaceTags4 = AndroidUtilities.replaceTags(LocaleController.getString(org.telegram.messenger.R.string.Gift2ActionInfoChannelNoConvert));
                                    }
                                } else if (z4) {
                                    if (!tL_messageActionStarGift.converted) {
                                        if (tL_messageActionStarGift.can_upgrade) {
                                            charSequenceReplaceTags4 = AndroidUtilities.replaceTags(LocaleController.getString(org.telegram.messenger.R.string.Gift2ActionSelfInfoUpgrade));
                                        } else {
                                            charSequenceReplaceTags4 = AndroidUtilities.replaceTags(LocaleController.getString(org.telegram.messenger.R.string.Gift2ActionSelfInfoNoConvert));
                                        }
                                    } else if (tL_messageActionStarGift.can_upgrade) {
                                        charSequenceReplaceTags4 = AndroidUtilities.replaceTags(LocaleController.getString(org.telegram.messenger.R.string.Gift2ActionSelfInfoUpgrade));
                                    } else {
                                        charSequenceReplaceTags4 = AndroidUtilities.replaceTags(LocaleController.getString(org.telegram.messenger.R.string.Gift2ActionSelfInfoNoConvert));
                                    }
                                } else if (z5) {
                                    if (messageObject.isOutOwner()) {
                                        string6 = LocaleController.formatString(org.telegram.messenger.R.string.Gift2ActionUpgradeOut, UserObject.getForcedFirstName(user5));
                                    } else {
                                        string6 = LocaleController.getString(org.telegram.messenger.R.string.Gift2ActionUpgrade);
                                    }
                                    charSequenceReplaceTags4 = AndroidUtilities.replaceTags(string6);
                                } else if (messageObject.isOutOwner()) {
                                    if (!z6) {
                                        if (tL_messageActionStarGift.can_upgrade) {
                                            charSequenceReplaceTags4 = AndroidUtilities.replaceTags(LocaleController.formatString(org.telegram.messenger.R.string.Gift2ActionOutInfoUpgrade, UserObject.getForcedFirstName(user5)));
                                        } else {
                                            charSequenceReplaceTags4 = AndroidUtilities.replaceTags(LocaleController.formatString(org.telegram.messenger.R.string.Gift2ActionOutInfoNoConvert, UserObject.getForcedFirstName(user5)));
                                        }
                                    } else if (tL_messageActionStarGift.can_upgrade) {
                                        charSequenceReplaceTags4 = AndroidUtilities.replaceTags(LocaleController.formatString(org.telegram.messenger.R.string.Gift2ActionOutInfoUpgrade, UserObject.getForcedFirstName(user5)));
                                    } else {
                                        charSequenceReplaceTags4 = AndroidUtilities.replaceTags(LocaleController.formatString(org.telegram.messenger.R.string.Gift2ActionOutInfoNoConvert, UserObject.getForcedFirstName(user5)));
                                    }
                                } else if (tL_messageActionStarGift.converted) {
                                    charSequenceReplaceTags4 = LocaleController.formatPluralStringComma("Gift2ActionConvertedInfo", (int) j);
                                } else if (tL_messageActionStarGift.saved) {
                                    if (!z6) {
                                        charSequenceReplaceTags4 = LocaleController.getString(org.telegram.messenger.R.string.Gift2ActionBotSavedInfo);
                                    } else {
                                        charSequenceReplaceTags4 = LocaleController.getString(org.telegram.messenger.R.string.Gift2ActionSavedInfo);
                                    }
                                } else if (!z6) {
                                    charSequenceReplaceTags4 = LocaleController.getString(org.telegram.messenger.R.string.Gift2ActionBotInfo);
                                } else {
                                    charSequenceReplaceTags4 = AndroidUtilities.replaceTags(LocaleController.formatPluralStringComma("Gift2ActionInfo", (int) j));
                                }
                            }
                            starGift2 = tL_messageActionStarGift.gift;
                            if (starGift2 == null) {
                                string7 = null;
                            } else {
                                string7 = null;
                            }
                            string8 = LocaleController.getString(org.telegram.messenger.R.string.ActionGiftStarsView);
                            if (messageObject.isOutOwner()) {
                                charSequence3 = string8;
                                charSequence3 = string8;
                                charSequence3 = string8;
                                if (!messageObject.isOutOwner()) {
                                    SpannableStringBuilder spannableStringBuilder13 = new SpannableStringBuilder();
                                    spannableStringBuilder13.append((CharSequence) "^  ");
                                    ColoredImageSpan coloredImageSpan6 = new ColoredImageSpan(org.telegram.messenger.R.drawable.gift_unpack);
                                    coloredImageSpan6.setScale(0.8f, 0.8f);
                                    spannableStringBuilder13.setSpan(coloredImageSpan6, 0, 1, 33);
                                    spannableStringBuilder13.append((CharSequence) LocaleController.getString(org.telegram.messenger.R.string.Gift2Unpack));
                                    charSequence3 = spannableStringBuilder13;
                                }
                            } else {
                                charSequence3 = string8;
                                charSequence3 = string8;
                                charSequence3 = string8;
                                if (!messageObject.isOutOwner()) {
                                    SpannableStringBuilder spannableStringBuilder14 = new SpannableStringBuilder();
                                    spannableStringBuilder14.append((CharSequence) "^  ");
                                    ColoredImageSpan coloredImageSpan7 = new ColoredImageSpan(org.telegram.messenger.R.drawable.gift_unpack);
                                    coloredImageSpan7.setScale(0.8f, 0.8f);
                                    spannableStringBuilder14.setSpan(coloredImageSpan7, 0, 1, 33);
                                    spannableStringBuilder14.append((CharSequence) LocaleController.getString(org.telegram.messenger.R.string.Gift2Unpack));
                                    charSequence3 = spannableStringBuilder14;
                                }
                            }
                            CharSequence charSequence9 = charSequence3;
                            starGift3 = tL_messageActionStarGift.gift;
                            if (starGift3 != null) {
                                charSequenceReplaceSingleTagToLink = null;
                            } else {
                                charSequenceReplaceSingleTagToLink = null;
                            }
                            createGiftPremiumLayouts(spannableStringBuilder3, null, charSequenceReplaceSingleTagToLink, charSequenceReplaceTags4, false, charSequence9, 11, string7, this.giftRectSize, true, false);
                        } else if (messageAction2 instanceof TLRPC.TL_messageActionGiftTon) {
                            createGiftPremiumLayouts(LocaleController.getString(org.telegram.messenger.R.string.ActionGiftTonTitle), null, null, this.currentMessageObject.messageText, false, LocaleController.getString(org.telegram.messenger.R.string.ActionGiftStarsView), 11, null, this.giftRectSize, true, false);
                            this.textLayout = null;
                            this.textHeight = 0;
                            this.titleLayout = null;
                            this.titleHeight = 0;
                            this.textY = 0;
                        } else {
                            createGiftPremiumLayouts(LocaleController.getString(org.telegram.messenger.R.string.ActionStarGiveawayPrizeTitle), null, null, this.currentMessageObject.messageText, false, LocaleController.getString(org.telegram.messenger.R.string.ActionGiftStarsView), 11, null, this.giftRectSize, true, false);
                            this.textLayout = null;
                            this.textHeight = 0;
                            this.titleLayout = null;
                            this.titleHeight = 0;
                            this.textY = 0;
                        }
                    } else {
                        dialogId = 0;
                        if (i == 33) {
                            tL_messageActionStarGiftPurchaseOffer = (TLRPC.TL_messageActionStarGiftPurchaseOffer) message3.action;
                            spannableStringBuilder2 = new SpannableStringBuilder(charSequenceCreateActionTextWithTopic);
                            spannableStringBuilder2.append((CharSequence) "\n\n");
                            if (tL_messageActionStarGiftPurchaseOffer.accepted) {
                                spannableStringBuilder2.append((CharSequence) AndroidUtilities.replaceTags(LocaleController.getString(org.telegram.messenger.R.string.GiftOfferStatusAccepted)));
                            } else if (tL_messageActionStarGiftPurchaseOffer.declined) {
                                spannableStringBuilder2.append((CharSequence) AndroidUtilities.replaceTags(LocaleController.getString(org.telegram.messenger.R.string.GiftOfferStatusRejected)));
                            } else {
                                iMax = Math.max(0, tL_messageActionStarGiftPurchaseOffer.expires_at - ConnectionsManager.getInstance(this.currentAccount).getCurrentTime());
                                if (iMax == 0) {
                                    spannableStringBuilder2.append((CharSequence) AndroidUtilities.replaceTags(LocaleController.getString(org.telegram.messenger.R.string.GiftOfferStatusExpired)));
                                } else {
                                    shortDuration2 = LocaleController.formatShortDuration2(iMax);
                                    if (shortDuration2.endsWith(".")) {
                                        i3 = 1;
                                        c = 0;
                                        shortDuration2 = shortDuration2.substring(0, shortDuration2.length() - 1);
                                    } else {
                                        i3 = 1;
                                        c = 0;
                                    }
                                    int i11 = org.telegram.messenger.R.string.GiftOfferStatusPending;
                                    Object[] objArr3 = new Object[i3];
                                    objArr3[c] = shortDuration2;
                                    spannableStringBuilder2.append((CharSequence) AndroidUtilities.replaceTags(LocaleController.formatString(i11, objArr3)));
                                }
                            }
                            createGiftPremiumLayouts(null, null, null, spannableStringBuilder2, false, null, 11, null, this.giftRectSize, false, false);
                            this.textLayout = null;
                            this.textHeight = 0;
                            this.titleLayout = null;
                            this.titleHeight = 0;
                            this.textY = 0;
                            this.giftRectEmpty = false;
                        } else if (i == 34) {
                            createGiftPremiumLayouts(null, null, null, charSequenceCreateActionTextWithTopic, false, null, 11, null, this.giftRectSize, false, true);
                            this.textLayout = null;
                            this.textHeight = 0;
                            this.titleLayout = null;
                            this.titleHeight = 0;
                            this.textY = 0;
                            this.giftRectEmpty = true;
                        } else if (i == 35) {
                            tL_messageActionNoForwardsRequest = (TLRPC.TL_messageActionNoForwardsRequest) message3.action;
                            spannableStringBuilder = new SpannableStringBuilder();
                            shortName = DialogObject.getShortName(MessagesController.getInstance(this.currentAccount).getUser(Long.valueOf(messageObject.getDialogId())));
                            if (tL_messageActionNoForwardsRequest.new_value) {
                                if (messageObject.isOut()) {
                                    charSequenceReplaceTags3 = LocaleController.getString(org.telegram.messenger.R.string.SharingOfferDisableHeaderYou);
                                } else {
                                    charSequenceReplaceTags3 = AndroidUtilities.replaceTags(LocaleController.formatString(org.telegram.messenger.R.string.SharingOfferDisableHeaderOther, shortName));
                                }
                                spannableStringBuilder.append(charSequenceReplaceTags3);
                            } else {
                                if (messageObject.isOut()) {
                                    charSequenceReplaceTags2 = LocaleController.getString(org.telegram.messenger.R.string.SharingOfferEnableHeaderYou);
                                } else {
                                    charSequenceReplaceTags2 = AndroidUtilities.replaceTags(LocaleController.formatString(org.telegram.messenger.R.string.SharingOfferEnableHeaderOther, shortName));
                                }
                                spannableStringBuilder.append(charSequenceReplaceTags2);
                            }
                            if (tL_messageActionNoForwardsRequest.new_value) {
                                spannableStringBuilder.append((CharSequence) "\n\n");
                                spannableStringBuilder.append(createOption(LocaleController.getString(org.telegram.messenger.R.string.SharingOfferDisable1), org.telegram.messenger.R.drawable.floating_check));
                                spannableStringBuilder.append((CharSequence) "\n\n");
                                spannableStringBuilder.append(createOption(LocaleController.getString(org.telegram.messenger.R.string.SharingOfferDisable2), org.telegram.messenger.R.drawable.floating_check));
                                spannableStringBuilder.append((CharSequence) "\n\n");
                                spannableStringBuilder.append(createOption(LocaleController.getString(org.telegram.messenger.R.string.SharingOfferDisable3), org.telegram.messenger.R.drawable.floating_check));
                                spannableStringBuilder.append((CharSequence) "\n\n");
                                spannableStringBuilder.append(createOption(LocaleController.getString(org.telegram.messenger.R.string.SharingOfferDisable4), org.telegram.messenger.R.drawable.floating_check));
                            } else {
                                spannableStringBuilder.append((CharSequence) "\n\n");
                                spannableStringBuilder.append(createOption(LocaleController.getString(org.telegram.messenger.R.string.SharingOfferEnable1), org.telegram.messenger.R.drawable.floating_check));
                                spannableStringBuilder.append((CharSequence) "\n\n");
                                spannableStringBuilder.append(createOption(LocaleController.getString(org.telegram.messenger.R.string.SharingOfferEnable2), org.telegram.messenger.R.drawable.floating_check));
                                spannableStringBuilder.append((CharSequence) "\n\n");
                                spannableStringBuilder.append(createOption(LocaleController.getString(org.telegram.messenger.R.string.SharingOfferEnable3), org.telegram.messenger.R.drawable.floating_check));
                                spannableStringBuilder.append((CharSequence) "\n\n");
                                spannableStringBuilder.append(createOption(LocaleController.getString(org.telegram.messenger.R.string.SharingOfferEnable4), org.telegram.messenger.R.drawable.floating_check));
                            }
                            createGiftPremiumLayouts(null, null, null, spannableStringBuilder, false, null, 11, null, this.giftRectSize, false, true);
                            this.textLayout = null;
                            this.textHeight = 0;
                            this.titleLayout = null;
                            this.titleHeight = 0;
                            this.textY = 0;
                            this.giftRectEmpty = true;
                        } else if (i == 31) {
                            TL_stars.StarGift starGift5 = ((TLRPC.TL_chatThemeUniqueGift) ((TLRPC.TL_messageActionSetChatTheme) message3.action).theme).gift;
                            str = starGift5.title + " #" + LocaleController.formatNumber(starGift5.num, ',');
                            fromChatId = messageObject.getFromChatId();
                            if (UserConfig.getInstance(this.currentAccount).getClientUserId() == fromChatId) {
                                string5 = LocaleController.formatString(org.telegram.messenger.R.string.GiftThemesSetByYou, str);
                            } else {
                                string5 = LocaleController.formatString(org.telegram.messenger.R.string.GiftThemesSetByOther, DialogObject.getShortName(this.currentAccount, fromChatId), str);
                            }
                            createGiftPremiumLayouts(null, null, null, AndroidUtilities.replaceTags(string5), false, LocaleController.getString(org.telegram.messenger.R.string.GiftThemesSetActionView), 11, null, this.giftRectSize, true, false);
                            this.textLayout = null;
                            this.textHeight = 0;
                            this.titleLayout = null;
                            this.titleHeight = 0;
                            this.textY = 0;
                        } else if (i == 18) {
                            messageAction = message3.action;
                            if (messageAction instanceof TLRPC.TL_messageActionGiftPremium) {
                                tL_textWithEntities = ((TLRPC.TL_messageActionGiftPremium) messageAction).message;
                            } else if (messageAction instanceof TLRPC.TL_messageActionGiftCode) {
                                tL_textWithEntities = ((TLRPC.TL_messageActionGiftCode) messageAction).message;
                            } else {
                                tL_textWithEntities = null;
                            }
                            if (tL_textWithEntities != null) {
                                string4 = null;
                            } else {
                                string4 = null;
                            }
                            if (string4 == null) {
                                string4 = LocaleController.getString(org.telegram.messenger.R.string.ActionGiftPremiumText);
                            }
                            CharSequence charSequence10 = string4;
                            if (isGiftCode()) {
                                i2 = org.telegram.messenger.R.string.ActionGiftPremiumView;
                            } else {
                                i2 = org.telegram.messenger.R.string.ActionGiftPremiumView;
                            }
                            createGiftPremiumLayouts(LocaleController.formatPluralStringComma("ActionGiftPremiumTitle2", messageObject.messageOwner.action.months), null, null, charSequence10, true, LocaleController.getString(i2), 11, null, this.giftRectSize, false, false);
                        } else if (i == 21) {
                            tL_messageActionSuggestProfilePhoto = (TLRPC.TL_messageActionSuggestProfilePhoto) message3.action;
                            MessagesController messagesController3 = MessagesController.getInstance(this.currentAccount);
                            if (!messageObject.isOutOwner()) {
                                dialogId = messageObject.getDialogId();
                            }
                            user3 = messagesController3.getUser(Long.valueOf(dialogId));
                            if (tL_messageActionSuggestProfilePhoto.video) {
                                z2 = true;
                            } else {
                                z2 = true;
                            }
                            if (user3.id == UserConfig.getInstance(this.currentAccount).clientUserId) {
                                user4 = MessagesController.getInstance(this.currentAccount).getUser(Long.valueOf(messageObject.getDialogId()));
                                if (z2) {
                                    string2 = LocaleController.formatString(org.telegram.messenger.R.string.ActionSuggestVideoFromYouDescription, user4.first_name);
                                } else {
                                    string2 = LocaleController.formatString(org.telegram.messenger.R.string.ActionSuggestPhotoFromYouDescription, user4.first_name);
                                }
                            } else if (z2) {
                                string2 = LocaleController.formatString(org.telegram.messenger.R.string.ActionSuggestVideoToYouDescription, user3.first_name);
                            } else {
                                string2 = LocaleController.formatString(org.telegram.messenger.R.string.ActionSuggestPhotoToYouDescription, user3.first_name);
                            }
                            CharSequence charSequence11 = string2;
                            if (!tL_messageActionSuggestProfilePhoto.video) {
                                string3 = LocaleController.getString(org.telegram.messenger.R.string.ViewVideoAction);
                            } else {
                                string3 = LocaleController.getString(org.telegram.messenger.R.string.ViewVideoAction);
                            }
                            createGiftPremiumLayouts(null, null, null, charSequence11, false, string3, 11, null, this.giftRectSize, true, false);
                            this.textLayout = null;
                            this.textHeight = 0;
                            this.titleLayout = null;
                            this.titleHeight = 0;
                            this.textY = 0;
                        } else if (i == 22) {
                            MessagesController messagesController4 = MessagesController.getInstance(this.currentAccount);
                            if (messageObject.isOutOwner()) {
                                dialogId2 = 0;
                            } else {
                                dialogId2 = messageObject.getDialogId();
                            }
                            user2 = messagesController4.getUser(Long.valueOf(dialogId2));
                            if (messageObject.getDialogId() < 0) {
                                charSequence2 = messageObject.messageText;
                            } else {
                                if (messageObject.isOutOwner()) {
                                }
                                if (user2 == null) {
                                }
                                charSequence = messageObject.messageText;
                                string = LocaleController.getString(org.telegram.messenger.R.string.ViewWallpaperAction);
                                z = true;
                                createGiftPremiumLayouts(null, null, null, charSequence, false, string, 11, null, this.giftRectSize, z, false);
                                this.textLayout = null;
                                this.textHeight = 0;
                                this.titleLayout = null;
                                this.titleHeight = 0;
                                this.textY = 0;
                            }
                            charSequence = charSequence2;
                            string = null;
                            z = true;
                            createGiftPremiumLayouts(null, null, null, charSequence, false, string, 11, null, this.giftRectSize, z, false);
                            this.textLayout = null;
                            this.textHeight = 0;
                            this.titleLayout = null;
                            this.titleHeight = 0;
                            this.textY = 0;
                        } else if (messageObject.isStoryMention()) {
                            user = MessagesController.getInstance(this.currentAccount).getUser(Long.valueOf(messageObject.messageOwner.media.user_id));
                            if (user.self) {
                                charSequenceReplaceTags = AndroidUtilities.replaceTags(LocaleController.formatString(org.telegram.messenger.R.string.StoryYouMentionedTitle, MessagesController.getInstance(this.currentAccount).getUser(Long.valueOf(messageObject.getDialogId())).first_name));
                            } else {
                                charSequenceReplaceTags = AndroidUtilities.replaceTags(LocaleController.formatString(org.telegram.messenger.R.string.StoryMentionedTitle, user.first_name));
                            }
                            createGiftPremiumLayouts(null, null, null, charSequenceReplaceTags, false, LocaleController.getString(org.telegram.messenger.R.string.StoryMentionedAction), 11, null, this.giftRectSize, true, false);
                            this.textLayout = null;
                            this.textHeight = 0;
                            this.titleLayout = null;
                            this.titleHeight = 0;
                            this.textY = 0;
                        }
                    }
                }
            } else if (message3 != null) {
                messageAction3 = message3.action;
                if (!(messageAction3 instanceof TLRPC.TL_messageActionSuggestedPostApproval)) {
                    i = messageObject.type;
                    if (i == 11) {
                        ImageReceiver imageReceiver3 = this.imageReceiver;
                        float f3 = (this.previousWidth - AndroidUtilities.roundMessageSize) / 2.0f;
                        float fDp3 = this.textHeight + AndroidUtilities.dp(19.0f);
                        int i12 = AndroidUtilities.roundMessageSize;
                        imageReceiver3.setImageCoords(f3, fDp3, i12, i12);
                    } else if (i == 25) {
                        createGiftPremiumChannelLayouts();
                    } else if (i == 30) {
                        user5 = MessagesController.getInstance(this.currentAccount).getUser(Long.valueOf(this.currentMessageObject.getDialogId()));
                        messageAction2 = messageObject.messageOwner.action;
                        if (messageAction2 instanceof TLRPC.TL_messageActionGiftStars) {
                            CharSequence pluralStringComma3 = LocaleController.formatPluralStringComma("ActionGiftStarsTitle", (int) ((TLRPC.TL_messageActionGiftStars) messageAction2).stars);
                            if (this.currentMessageObject.isOutOwner()) {
                                string9 = LocaleController.formatString(org.telegram.messenger.R.string.ActionGiftStarsSubtitle, UserObject.getForcedFirstName(user5));
                            } else {
                                string9 = LocaleController.getString(org.telegram.messenger.R.string.ActionGiftStarsSubtitleYou);
                            }
                            createGiftPremiumLayouts(pluralStringComma3, null, null, AndroidUtilities.replaceTags(string9), false, LocaleController.getString(org.telegram.messenger.R.string.ActionGiftStarsView), 11, null, this.giftRectSize, true, false);
                        } else if (!(messageAction2 instanceof TLRPC.TL_messageActionStarGiftUnique)) {
                            if (messageAction2 instanceof TLRPC.TL_messageActionStarGift) {
                                tL_messageActionStarGift = (TLRPC.TL_messageActionStarGift) messageAction2;
                                j = tL_messageActionStarGift.convert_stars;
                                clientUserId = UserConfig.getInstance(this.currentAccount).getClientUserId();
                                peer = tL_messageActionStarGift.peer;
                                if (peer != null) {
                                    z3 = false;
                                } else {
                                    z3 = false;
                                }
                                if (messageObject.getDialogId() == clientUserId) {
                                    z4 = false;
                                } else {
                                    z4 = false;
                                }
                                fromChatId2 = messageObject.getFromChatId();
                                if (!tL_messageActionStarGift.prepaid_upgrade) {
                                    fromChatId2 = DialogObject.getPeerDialogId(peer2);
                                }
                                spannableStringBuilder3 = new SpannableStringBuilder();
                                userOrChat = MessagesController.getInstance(this.currentAccount).getUserOrChat(fromChatId2);
                                peerDialogId = DialogObject.getPeerDialogId(tL_messageActionStarGift.to_id);
                                TLObject userOrChat5 = MessagesController.getInstance(this.currentAccount).getUserOrChat(peerDialogId);
                                if (tL_messageActionStarGift.can_upgrade) {
                                    j2 = peerDialogId;
                                    z5 = false;
                                } else {
                                    j2 = peerDialogId;
                                    z5 = false;
                                }
                                if (j2 == 0) {
                                    if (z4) {
                                        if (tL_messageActionStarGift.gift_num <= 0) {
                                            spannableStringBuilder3.append((CharSequence) LocaleController.getString(org.telegram.messenger.R.string.Gift2ActionSelfTitle));
                                        } else {
                                            spannableStringBuilder3.append((CharSequence) LocaleController.getString(org.telegram.messenger.R.string.Gift2ActionSelfTitle));
                                        }
                                    } else {
                                        if (tL_messageActionStarGift.prepaid_upgrade) {
                                            i4 = org.telegram.messenger.R.string.Gift2ActionUpgradeTitle;
                                        } else {
                                            i4 = org.telegram.messenger.R.string.Gift2ActionTitle;
                                        }
                                        spannableStringBuilder3.append((CharSequence) LocaleController.getString(i4)).append((CharSequence) " ");
                                        if (DialogObject.hasPhoto(userOrChat)) {
                                            spannableStringBuilder3.append((CharSequence) "a ");
                                            AvatarSpan avatarSpan10 = new AvatarSpan(this, this.currentAccount, 18.0f);
                                            avatarSpan10.setObject(userOrChat);
                                            spannableStringBuilder3.setSpan(avatarSpan10, spannableStringBuilder3.length() - 2, spannableStringBuilder3.length() - 1, 33);
                                        }
                                        spannableStringBuilder3.append((CharSequence) DialogObject.getShortName(userOrChat));
                                    }
                                } else if (z4) {
                                    if (tL_messageActionStarGift.gift_num <= 0) {
                                        spannableStringBuilder3.append((CharSequence) LocaleController.getString(org.telegram.messenger.R.string.Gift2ActionSelfTitle));
                                    } else {
                                        spannableStringBuilder3.append((CharSequence) LocaleController.getString(org.telegram.messenger.R.string.Gift2ActionSelfTitle));
                                    }
                                } else {
                                    if (tL_messageActionStarGift.prepaid_upgrade) {
                                        i4 = org.telegram.messenger.R.string.Gift2ActionUpgradeTitle;
                                    } else {
                                        i4 = org.telegram.messenger.R.string.Gift2ActionTitle;
                                    }
                                    spannableStringBuilder3.append((CharSequence) LocaleController.getString(i4)).append((CharSequence) " ");
                                    if (DialogObject.hasPhoto(userOrChat)) {
                                        spannableStringBuilder3.append((CharSequence) "a ");
                                        AvatarSpan avatarSpan11 = new AvatarSpan(this, this.currentAccount, 18.0f);
                                        avatarSpan11.setObject(userOrChat);
                                        spannableStringBuilder3.setSpan(avatarSpan11, spannableStringBuilder3.length() - 2, spannableStringBuilder3.length() - 1, 33);
                                    }
                                    spannableStringBuilder3.append((CharSequence) DialogObject.getShortName(userOrChat));
                                }
                                if (messageObject.isOutOwner()) {
                                }
                                if (tL_messageActionStarGift.refunded) {
                                    charSequenceReplaceTags4 = LocaleController.getString(org.telegram.messenger.R.string.Gift2ActionConvertRefundedText);
                                } else {
                                    tL_textWithEntities2 = tL_messageActionStarGift.message;
                                    if (tL_textWithEntities2 == null) {
                                        if (tL_messageActionStarGift.auction_acquired) {
                                            charSequenceReplaceTags4 = LocaleController.formatString(org.telegram.messenger.R.string.Gift2ActionWonActionText, LocaleController.formatNumber(tL_messageActionStarGift.gift.stars + tL_messageActionStarGift.upgrade_stars, ','));
                                        } else if (z3) {
                                            if (tL_messageActionStarGift.converted) {
                                                charSequenceReplaceTags4 = LocaleController.formatPluralStringComma("Gift2ActionConvertedInfo", (int) j);
                                            } else if (!z6) {
                                                charSequenceReplaceTags4 = AndroidUtilities.replaceTags(LocaleController.getString(org.telegram.messenger.R.string.Gift2ActionInfoChannelNoConvert));
                                            } else {
                                                charSequenceReplaceTags4 = AndroidUtilities.replaceTags(LocaleController.getString(org.telegram.messenger.R.string.Gift2ActionInfoChannelNoConvert));
                                            }
                                        } else if (z4) {
                                            if (!tL_messageActionStarGift.converted) {
                                                if (tL_messageActionStarGift.can_upgrade) {
                                                    charSequenceReplaceTags4 = AndroidUtilities.replaceTags(LocaleController.getString(org.telegram.messenger.R.string.Gift2ActionSelfInfoUpgrade));
                                                } else {
                                                    charSequenceReplaceTags4 = AndroidUtilities.replaceTags(LocaleController.getString(org.telegram.messenger.R.string.Gift2ActionSelfInfoNoConvert));
                                                }
                                            } else if (tL_messageActionStarGift.can_upgrade) {
                                                charSequenceReplaceTags4 = AndroidUtilities.replaceTags(LocaleController.getString(org.telegram.messenger.R.string.Gift2ActionSelfInfoUpgrade));
                                            } else {
                                                charSequenceReplaceTags4 = AndroidUtilities.replaceTags(LocaleController.getString(org.telegram.messenger.R.string.Gift2ActionSelfInfoNoConvert));
                                            }
                                        } else if (z5) {
                                            if (messageObject.isOutOwner()) {
                                                string6 = LocaleController.formatString(org.telegram.messenger.R.string.Gift2ActionUpgradeOut, UserObject.getForcedFirstName(user5));
                                            } else {
                                                string6 = LocaleController.getString(org.telegram.messenger.R.string.Gift2ActionUpgrade);
                                            }
                                            charSequenceReplaceTags4 = AndroidUtilities.replaceTags(string6);
                                        } else if (messageObject.isOutOwner()) {
                                            if (!z6) {
                                                if (tL_messageActionStarGift.can_upgrade) {
                                                    charSequenceReplaceTags4 = AndroidUtilities.replaceTags(LocaleController.formatString(org.telegram.messenger.R.string.Gift2ActionOutInfoUpgrade, UserObject.getForcedFirstName(user5)));
                                                } else {
                                                    charSequenceReplaceTags4 = AndroidUtilities.replaceTags(LocaleController.formatString(org.telegram.messenger.R.string.Gift2ActionOutInfoNoConvert, UserObject.getForcedFirstName(user5)));
                                                }
                                            } else if (tL_messageActionStarGift.can_upgrade) {
                                                charSequenceReplaceTags4 = AndroidUtilities.replaceTags(LocaleController.formatString(org.telegram.messenger.R.string.Gift2ActionOutInfoUpgrade, UserObject.getForcedFirstName(user5)));
                                            } else {
                                                charSequenceReplaceTags4 = AndroidUtilities.replaceTags(LocaleController.formatString(org.telegram.messenger.R.string.Gift2ActionOutInfoNoConvert, UserObject.getForcedFirstName(user5)));
                                            }
                                        } else if (tL_messageActionStarGift.converted) {
                                            charSequenceReplaceTags4 = LocaleController.formatPluralStringComma("Gift2ActionConvertedInfo", (int) j);
                                        } else if (tL_messageActionStarGift.saved) {
                                            if (!z6) {
                                                charSequenceReplaceTags4 = LocaleController.getString(org.telegram.messenger.R.string.Gift2ActionBotSavedInfo);
                                            } else {
                                                charSequenceReplaceTags4 = LocaleController.getString(org.telegram.messenger.R.string.Gift2ActionSavedInfo);
                                            }
                                        } else if (!z6) {
                                            charSequenceReplaceTags4 = LocaleController.getString(org.telegram.messenger.R.string.Gift2ActionBotInfo);
                                        } else {
                                            charSequenceReplaceTags4 = AndroidUtilities.replaceTags(LocaleController.formatPluralStringComma("Gift2ActionInfo", (int) j));
                                        }
                                    } else if (tL_messageActionStarGift.auction_acquired) {
                                        charSequenceReplaceTags4 = LocaleController.formatString(org.telegram.messenger.R.string.Gift2ActionWonActionText, LocaleController.formatNumber(tL_messageActionStarGift.gift.stars + tL_messageActionStarGift.upgrade_stars, ','));
                                    } else if (z3) {
                                        if (tL_messageActionStarGift.converted) {
                                            charSequenceReplaceTags4 = LocaleController.formatPluralStringComma("Gift2ActionConvertedInfo", (int) j);
                                        } else if (!z6) {
                                            charSequenceReplaceTags4 = AndroidUtilities.replaceTags(LocaleController.getString(org.telegram.messenger.R.string.Gift2ActionInfoChannelNoConvert));
                                        } else {
                                            charSequenceReplaceTags4 = AndroidUtilities.replaceTags(LocaleController.getString(org.telegram.messenger.R.string.Gift2ActionInfoChannelNoConvert));
                                        }
                                    } else if (z4) {
                                        if (!tL_messageActionStarGift.converted) {
                                            if (tL_messageActionStarGift.can_upgrade) {
                                                charSequenceReplaceTags4 = AndroidUtilities.replaceTags(LocaleController.getString(org.telegram.messenger.R.string.Gift2ActionSelfInfoUpgrade));
                                            } else {
                                                charSequenceReplaceTags4 = AndroidUtilities.replaceTags(LocaleController.getString(org.telegram.messenger.R.string.Gift2ActionSelfInfoNoConvert));
                                            }
                                        } else if (tL_messageActionStarGift.can_upgrade) {
                                            charSequenceReplaceTags4 = AndroidUtilities.replaceTags(LocaleController.getString(org.telegram.messenger.R.string.Gift2ActionSelfInfoUpgrade));
                                        } else {
                                            charSequenceReplaceTags4 = AndroidUtilities.replaceTags(LocaleController.getString(org.telegram.messenger.R.string.Gift2ActionSelfInfoNoConvert));
                                        }
                                    } else if (z5) {
                                        if (messageObject.isOutOwner()) {
                                            string6 = LocaleController.formatString(org.telegram.messenger.R.string.Gift2ActionUpgradeOut, UserObject.getForcedFirstName(user5));
                                        } else {
                                            string6 = LocaleController.getString(org.telegram.messenger.R.string.Gift2ActionUpgrade);
                                        }
                                        charSequenceReplaceTags4 = AndroidUtilities.replaceTags(string6);
                                    } else if (messageObject.isOutOwner()) {
                                        if (!z6) {
                                            if (tL_messageActionStarGift.can_upgrade) {
                                                charSequenceReplaceTags4 = AndroidUtilities.replaceTags(LocaleController.formatString(org.telegram.messenger.R.string.Gift2ActionOutInfoUpgrade, UserObject.getForcedFirstName(user5)));
                                            } else {
                                                charSequenceReplaceTags4 = AndroidUtilities.replaceTags(LocaleController.formatString(org.telegram.messenger.R.string.Gift2ActionOutInfoNoConvert, UserObject.getForcedFirstName(user5)));
                                            }
                                        } else if (tL_messageActionStarGift.can_upgrade) {
                                            charSequenceReplaceTags4 = AndroidUtilities.replaceTags(LocaleController.formatString(org.telegram.messenger.R.string.Gift2ActionOutInfoUpgrade, UserObject.getForcedFirstName(user5)));
                                        } else {
                                            charSequenceReplaceTags4 = AndroidUtilities.replaceTags(LocaleController.formatString(org.telegram.messenger.R.string.Gift2ActionOutInfoNoConvert, UserObject.getForcedFirstName(user5)));
                                        }
                                    } else if (tL_messageActionStarGift.converted) {
                                        charSequenceReplaceTags4 = LocaleController.formatPluralStringComma("Gift2ActionConvertedInfo", (int) j);
                                    } else if (tL_messageActionStarGift.saved) {
                                        if (!z6) {
                                            charSequenceReplaceTags4 = LocaleController.getString(org.telegram.messenger.R.string.Gift2ActionBotSavedInfo);
                                        } else {
                                            charSequenceReplaceTags4 = LocaleController.getString(org.telegram.messenger.R.string.Gift2ActionSavedInfo);
                                        }
                                    } else if (!z6) {
                                        charSequenceReplaceTags4 = LocaleController.getString(org.telegram.messenger.R.string.Gift2ActionBotInfo);
                                    } else {
                                        charSequenceReplaceTags4 = AndroidUtilities.replaceTags(LocaleController.formatPluralStringComma("Gift2ActionInfo", (int) j));
                                    }
                                }
                                starGift2 = tL_messageActionStarGift.gift;
                                if (starGift2 == null) {
                                    string7 = null;
                                } else {
                                    string7 = null;
                                }
                                string8 = LocaleController.getString(org.telegram.messenger.R.string.ActionGiftStarsView);
                                if (messageObject.isOutOwner()) {
                                    charSequence3 = string8;
                                    charSequence3 = string8;
                                    charSequence3 = string8;
                                    if (!messageObject.isOutOwner()) {
                                        SpannableStringBuilder spannableStringBuilder15 = new SpannableStringBuilder();
                                        spannableStringBuilder15.append((CharSequence) "^  ");
                                        ColoredImageSpan coloredImageSpan8 = new ColoredImageSpan(org.telegram.messenger.R.drawable.gift_unpack);
                                        coloredImageSpan8.setScale(0.8f, 0.8f);
                                        spannableStringBuilder15.setSpan(coloredImageSpan8, 0, 1, 33);
                                        spannableStringBuilder15.append((CharSequence) LocaleController.getString(org.telegram.messenger.R.string.Gift2Unpack));
                                        charSequence3 = spannableStringBuilder15;
                                    }
                                } else {
                                    charSequence3 = string8;
                                    charSequence3 = string8;
                                    charSequence3 = string8;
                                    if (!messageObject.isOutOwner()) {
                                        SpannableStringBuilder spannableStringBuilder16 = new SpannableStringBuilder();
                                        spannableStringBuilder16.append((CharSequence) "^  ");
                                        ColoredImageSpan coloredImageSpan9 = new ColoredImageSpan(org.telegram.messenger.R.drawable.gift_unpack);
                                        coloredImageSpan9.setScale(0.8f, 0.8f);
                                        spannableStringBuilder16.setSpan(coloredImageSpan9, 0, 1, 33);
                                        spannableStringBuilder16.append((CharSequence) LocaleController.getString(org.telegram.messenger.R.string.Gift2Unpack));
                                        charSequence3 = spannableStringBuilder16;
                                    }
                                }
                                CharSequence charSequence12 = charSequence3;
                                starGift3 = tL_messageActionStarGift.gift;
                                if (starGift3 != null) {
                                    charSequenceReplaceSingleTagToLink = null;
                                } else {
                                    charSequenceReplaceSingleTagToLink = null;
                                }
                                createGiftPremiumLayouts(spannableStringBuilder3, null, charSequenceReplaceSingleTagToLink, charSequenceReplaceTags4, false, charSequence12, 11, string7, this.giftRectSize, true, false);
                            } else if (messageAction2 instanceof TLRPC.TL_messageActionGiftTon) {
                                createGiftPremiumLayouts(LocaleController.getString(org.telegram.messenger.R.string.ActionGiftTonTitle), null, null, this.currentMessageObject.messageText, false, LocaleController.getString(org.telegram.messenger.R.string.ActionGiftStarsView), 11, null, this.giftRectSize, true, false);
                                this.textLayout = null;
                                this.textHeight = 0;
                                this.titleLayout = null;
                                this.titleHeight = 0;
                                this.textY = 0;
                            } else {
                                createGiftPremiumLayouts(LocaleController.getString(org.telegram.messenger.R.string.ActionStarGiveawayPrizeTitle), null, null, this.currentMessageObject.messageText, false, LocaleController.getString(org.telegram.messenger.R.string.ActionGiftStarsView), 11, null, this.giftRectSize, true, false);
                                this.textLayout = null;
                                this.textHeight = 0;
                                this.titleLayout = null;
                                this.titleHeight = 0;
                                this.textY = 0;
                            }
                        } else if (messageAction2 instanceof TLRPC.TL_messageActionStarGift) {
                            tL_messageActionStarGift = (TLRPC.TL_messageActionStarGift) messageAction2;
                            j = tL_messageActionStarGift.convert_stars;
                            clientUserId = UserConfig.getInstance(this.currentAccount).getClientUserId();
                            peer = tL_messageActionStarGift.peer;
                            if (peer != null) {
                                z3 = false;
                            } else {
                                z3 = false;
                            }
                            if (messageObject.getDialogId() == clientUserId) {
                                z4 = false;
                            } else {
                                z4 = false;
                            }
                            fromChatId2 = messageObject.getFromChatId();
                            if (!tL_messageActionStarGift.prepaid_upgrade) {
                                fromChatId2 = DialogObject.getPeerDialogId(peer2);
                            }
                            spannableStringBuilder3 = new SpannableStringBuilder();
                            userOrChat = MessagesController.getInstance(this.currentAccount).getUserOrChat(fromChatId2);
                            peerDialogId = DialogObject.getPeerDialogId(tL_messageActionStarGift.to_id);
                            TLObject userOrChat6 = MessagesController.getInstance(this.currentAccount).getUserOrChat(peerDialogId);
                            if (tL_messageActionStarGift.can_upgrade) {
                                j2 = peerDialogId;
                                z5 = false;
                            } else {
                                j2 = peerDialogId;
                                z5 = false;
                            }
                            if (j2 == 0) {
                                if (z4) {
                                    if (tL_messageActionStarGift.gift_num <= 0) {
                                        spannableStringBuilder3.append((CharSequence) LocaleController.getString(org.telegram.messenger.R.string.Gift2ActionSelfTitle));
                                    } else {
                                        spannableStringBuilder3.append((CharSequence) LocaleController.getString(org.telegram.messenger.R.string.Gift2ActionSelfTitle));
                                    }
                                } else {
                                    if (tL_messageActionStarGift.prepaid_upgrade) {
                                        i4 = org.telegram.messenger.R.string.Gift2ActionUpgradeTitle;
                                    } else {
                                        i4 = org.telegram.messenger.R.string.Gift2ActionTitle;
                                    }
                                    spannableStringBuilder3.append((CharSequence) LocaleController.getString(i4)).append((CharSequence) " ");
                                    if (DialogObject.hasPhoto(userOrChat)) {
                                        spannableStringBuilder3.append((CharSequence) "a ");
                                        AvatarSpan avatarSpan12 = new AvatarSpan(this, this.currentAccount, 18.0f);
                                        avatarSpan12.setObject(userOrChat);
                                        spannableStringBuilder3.setSpan(avatarSpan12, spannableStringBuilder3.length() - 2, spannableStringBuilder3.length() - 1, 33);
                                    }
                                    spannableStringBuilder3.append((CharSequence) DialogObject.getShortName(userOrChat));
                                }
                            } else if (z4) {
                                if (tL_messageActionStarGift.gift_num <= 0) {
                                    spannableStringBuilder3.append((CharSequence) LocaleController.getString(org.telegram.messenger.R.string.Gift2ActionSelfTitle));
                                } else {
                                    spannableStringBuilder3.append((CharSequence) LocaleController.getString(org.telegram.messenger.R.string.Gift2ActionSelfTitle));
                                }
                            } else {
                                if (tL_messageActionStarGift.prepaid_upgrade) {
                                    i4 = org.telegram.messenger.R.string.Gift2ActionUpgradeTitle;
                                } else {
                                    i4 = org.telegram.messenger.R.string.Gift2ActionTitle;
                                }
                                spannableStringBuilder3.append((CharSequence) LocaleController.getString(i4)).append((CharSequence) " ");
                                if (DialogObject.hasPhoto(userOrChat)) {
                                    spannableStringBuilder3.append((CharSequence) "a ");
                                    AvatarSpan avatarSpan13 = new AvatarSpan(this, this.currentAccount, 18.0f);
                                    avatarSpan13.setObject(userOrChat);
                                    spannableStringBuilder3.setSpan(avatarSpan13, spannableStringBuilder3.length() - 2, spannableStringBuilder3.length() - 1, 33);
                                }
                                spannableStringBuilder3.append((CharSequence) DialogObject.getShortName(userOrChat));
                            }
                            if (messageObject.isOutOwner()) {
                            }
                            if (tL_messageActionStarGift.refunded) {
                                charSequenceReplaceTags4 = LocaleController.getString(org.telegram.messenger.R.string.Gift2ActionConvertRefundedText);
                            } else {
                                tL_textWithEntities2 = tL_messageActionStarGift.message;
                                if (tL_textWithEntities2 == null) {
                                    if (tL_messageActionStarGift.auction_acquired) {
                                        charSequenceReplaceTags4 = LocaleController.formatString(org.telegram.messenger.R.string.Gift2ActionWonActionText, LocaleController.formatNumber(tL_messageActionStarGift.gift.stars + tL_messageActionStarGift.upgrade_stars, ','));
                                    } else if (z3) {
                                        if (tL_messageActionStarGift.converted) {
                                            charSequenceReplaceTags4 = LocaleController.formatPluralStringComma("Gift2ActionConvertedInfo", (int) j);
                                        } else if (!z6) {
                                            charSequenceReplaceTags4 = AndroidUtilities.replaceTags(LocaleController.getString(org.telegram.messenger.R.string.Gift2ActionInfoChannelNoConvert));
                                        } else {
                                            charSequenceReplaceTags4 = AndroidUtilities.replaceTags(LocaleController.getString(org.telegram.messenger.R.string.Gift2ActionInfoChannelNoConvert));
                                        }
                                    } else if (z4) {
                                        if (!tL_messageActionStarGift.converted) {
                                            if (tL_messageActionStarGift.can_upgrade) {
                                                charSequenceReplaceTags4 = AndroidUtilities.replaceTags(LocaleController.getString(org.telegram.messenger.R.string.Gift2ActionSelfInfoUpgrade));
                                            } else {
                                                charSequenceReplaceTags4 = AndroidUtilities.replaceTags(LocaleController.getString(org.telegram.messenger.R.string.Gift2ActionSelfInfoNoConvert));
                                            }
                                        } else if (tL_messageActionStarGift.can_upgrade) {
                                            charSequenceReplaceTags4 = AndroidUtilities.replaceTags(LocaleController.getString(org.telegram.messenger.R.string.Gift2ActionSelfInfoUpgrade));
                                        } else {
                                            charSequenceReplaceTags4 = AndroidUtilities.replaceTags(LocaleController.getString(org.telegram.messenger.R.string.Gift2ActionSelfInfoNoConvert));
                                        }
                                    } else if (z5) {
                                        if (messageObject.isOutOwner()) {
                                            string6 = LocaleController.formatString(org.telegram.messenger.R.string.Gift2ActionUpgradeOut, UserObject.getForcedFirstName(user5));
                                        } else {
                                            string6 = LocaleController.getString(org.telegram.messenger.R.string.Gift2ActionUpgrade);
                                        }
                                        charSequenceReplaceTags4 = AndroidUtilities.replaceTags(string6);
                                    } else if (messageObject.isOutOwner()) {
                                        if (!z6) {
                                            if (tL_messageActionStarGift.can_upgrade) {
                                                charSequenceReplaceTags4 = AndroidUtilities.replaceTags(LocaleController.formatString(org.telegram.messenger.R.string.Gift2ActionOutInfoUpgrade, UserObject.getForcedFirstName(user5)));
                                            } else {
                                                charSequenceReplaceTags4 = AndroidUtilities.replaceTags(LocaleController.formatString(org.telegram.messenger.R.string.Gift2ActionOutInfoNoConvert, UserObject.getForcedFirstName(user5)));
                                            }
                                        } else if (tL_messageActionStarGift.can_upgrade) {
                                            charSequenceReplaceTags4 = AndroidUtilities.replaceTags(LocaleController.formatString(org.telegram.messenger.R.string.Gift2ActionOutInfoUpgrade, UserObject.getForcedFirstName(user5)));
                                        } else {
                                            charSequenceReplaceTags4 = AndroidUtilities.replaceTags(LocaleController.formatString(org.telegram.messenger.R.string.Gift2ActionOutInfoNoConvert, UserObject.getForcedFirstName(user5)));
                                        }
                                    } else if (tL_messageActionStarGift.converted) {
                                        charSequenceReplaceTags4 = LocaleController.formatPluralStringComma("Gift2ActionConvertedInfo", (int) j);
                                    } else if (tL_messageActionStarGift.saved) {
                                        if (!z6) {
                                            charSequenceReplaceTags4 = LocaleController.getString(org.telegram.messenger.R.string.Gift2ActionBotSavedInfo);
                                        } else {
                                            charSequenceReplaceTags4 = LocaleController.getString(org.telegram.messenger.R.string.Gift2ActionSavedInfo);
                                        }
                                    } else if (!z6) {
                                        charSequenceReplaceTags4 = LocaleController.getString(org.telegram.messenger.R.string.Gift2ActionBotInfo);
                                    } else {
                                        charSequenceReplaceTags4 = AndroidUtilities.replaceTags(LocaleController.formatPluralStringComma("Gift2ActionInfo", (int) j));
                                    }
                                } else if (tL_messageActionStarGift.auction_acquired) {
                                    charSequenceReplaceTags4 = LocaleController.formatString(org.telegram.messenger.R.string.Gift2ActionWonActionText, LocaleController.formatNumber(tL_messageActionStarGift.gift.stars + tL_messageActionStarGift.upgrade_stars, ','));
                                } else if (z3) {
                                    if (tL_messageActionStarGift.converted) {
                                        charSequenceReplaceTags4 = LocaleController.formatPluralStringComma("Gift2ActionConvertedInfo", (int) j);
                                    } else if (!z6) {
                                        charSequenceReplaceTags4 = AndroidUtilities.replaceTags(LocaleController.getString(org.telegram.messenger.R.string.Gift2ActionInfoChannelNoConvert));
                                    } else {
                                        charSequenceReplaceTags4 = AndroidUtilities.replaceTags(LocaleController.getString(org.telegram.messenger.R.string.Gift2ActionInfoChannelNoConvert));
                                    }
                                } else if (z4) {
                                    if (!tL_messageActionStarGift.converted) {
                                        if (tL_messageActionStarGift.can_upgrade) {
                                            charSequenceReplaceTags4 = AndroidUtilities.replaceTags(LocaleController.getString(org.telegram.messenger.R.string.Gift2ActionSelfInfoUpgrade));
                                        } else {
                                            charSequenceReplaceTags4 = AndroidUtilities.replaceTags(LocaleController.getString(org.telegram.messenger.R.string.Gift2ActionSelfInfoNoConvert));
                                        }
                                    } else if (tL_messageActionStarGift.can_upgrade) {
                                        charSequenceReplaceTags4 = AndroidUtilities.replaceTags(LocaleController.getString(org.telegram.messenger.R.string.Gift2ActionSelfInfoUpgrade));
                                    } else {
                                        charSequenceReplaceTags4 = AndroidUtilities.replaceTags(LocaleController.getString(org.telegram.messenger.R.string.Gift2ActionSelfInfoNoConvert));
                                    }
                                } else if (z5) {
                                    if (messageObject.isOutOwner()) {
                                        string6 = LocaleController.formatString(org.telegram.messenger.R.string.Gift2ActionUpgradeOut, UserObject.getForcedFirstName(user5));
                                    } else {
                                        string6 = LocaleController.getString(org.telegram.messenger.R.string.Gift2ActionUpgrade);
                                    }
                                    charSequenceReplaceTags4 = AndroidUtilities.replaceTags(string6);
                                } else if (messageObject.isOutOwner()) {
                                    if (!z6) {
                                        if (tL_messageActionStarGift.can_upgrade) {
                                            charSequenceReplaceTags4 = AndroidUtilities.replaceTags(LocaleController.formatString(org.telegram.messenger.R.string.Gift2ActionOutInfoUpgrade, UserObject.getForcedFirstName(user5)));
                                        } else {
                                            charSequenceReplaceTags4 = AndroidUtilities.replaceTags(LocaleController.formatString(org.telegram.messenger.R.string.Gift2ActionOutInfoNoConvert, UserObject.getForcedFirstName(user5)));
                                        }
                                    } else if (tL_messageActionStarGift.can_upgrade) {
                                        charSequenceReplaceTags4 = AndroidUtilities.replaceTags(LocaleController.formatString(org.telegram.messenger.R.string.Gift2ActionOutInfoUpgrade, UserObject.getForcedFirstName(user5)));
                                    } else {
                                        charSequenceReplaceTags4 = AndroidUtilities.replaceTags(LocaleController.formatString(org.telegram.messenger.R.string.Gift2ActionOutInfoNoConvert, UserObject.getForcedFirstName(user5)));
                                    }
                                } else if (tL_messageActionStarGift.converted) {
                                    charSequenceReplaceTags4 = LocaleController.formatPluralStringComma("Gift2ActionConvertedInfo", (int) j);
                                } else if (tL_messageActionStarGift.saved) {
                                    if (!z6) {
                                        charSequenceReplaceTags4 = LocaleController.getString(org.telegram.messenger.R.string.Gift2ActionBotSavedInfo);
                                    } else {
                                        charSequenceReplaceTags4 = LocaleController.getString(org.telegram.messenger.R.string.Gift2ActionSavedInfo);
                                    }
                                } else if (!z6) {
                                    charSequenceReplaceTags4 = LocaleController.getString(org.telegram.messenger.R.string.Gift2ActionBotInfo);
                                } else {
                                    charSequenceReplaceTags4 = AndroidUtilities.replaceTags(LocaleController.formatPluralStringComma("Gift2ActionInfo", (int) j));
                                }
                            }
                            starGift2 = tL_messageActionStarGift.gift;
                            if (starGift2 == null) {
                                string7 = null;
                            } else {
                                string7 = null;
                            }
                            string8 = LocaleController.getString(org.telegram.messenger.R.string.ActionGiftStarsView);
                            if (messageObject.isOutOwner()) {
                                charSequence3 = string8;
                                charSequence3 = string8;
                                charSequence3 = string8;
                                if (!messageObject.isOutOwner()) {
                                    SpannableStringBuilder spannableStringBuilder17 = new SpannableStringBuilder();
                                    spannableStringBuilder17.append((CharSequence) "^  ");
                                    ColoredImageSpan coloredImageSpan10 = new ColoredImageSpan(org.telegram.messenger.R.drawable.gift_unpack);
                                    coloredImageSpan10.setScale(0.8f, 0.8f);
                                    spannableStringBuilder17.setSpan(coloredImageSpan10, 0, 1, 33);
                                    spannableStringBuilder17.append((CharSequence) LocaleController.getString(org.telegram.messenger.R.string.Gift2Unpack));
                                    charSequence3 = spannableStringBuilder17;
                                }
                            } else {
                                charSequence3 = string8;
                                charSequence3 = string8;
                                charSequence3 = string8;
                                if (!messageObject.isOutOwner()) {
                                    SpannableStringBuilder spannableStringBuilder18 = new SpannableStringBuilder();
                                    spannableStringBuilder18.append((CharSequence) "^  ");
                                    ColoredImageSpan coloredImageSpan11 = new ColoredImageSpan(org.telegram.messenger.R.drawable.gift_unpack);
                                    coloredImageSpan11.setScale(0.8f, 0.8f);
                                    spannableStringBuilder18.setSpan(coloredImageSpan11, 0, 1, 33);
                                    spannableStringBuilder18.append((CharSequence) LocaleController.getString(org.telegram.messenger.R.string.Gift2Unpack));
                                    charSequence3 = spannableStringBuilder18;
                                }
                            }
                            CharSequence charSequence13 = charSequence3;
                            starGift3 = tL_messageActionStarGift.gift;
                            if (starGift3 != null) {
                                charSequenceReplaceSingleTagToLink = null;
                            } else {
                                charSequenceReplaceSingleTagToLink = null;
                            }
                            createGiftPremiumLayouts(spannableStringBuilder3, null, charSequenceReplaceSingleTagToLink, charSequenceReplaceTags4, false, charSequence13, 11, string7, this.giftRectSize, true, false);
                        } else if (messageAction2 instanceof TLRPC.TL_messageActionGiftTon) {
                            createGiftPremiumLayouts(LocaleController.getString(org.telegram.messenger.R.string.ActionGiftTonTitle), null, null, this.currentMessageObject.messageText, false, LocaleController.getString(org.telegram.messenger.R.string.ActionGiftStarsView), 11, null, this.giftRectSize, true, false);
                            this.textLayout = null;
                            this.textHeight = 0;
                            this.titleLayout = null;
                            this.titleHeight = 0;
                            this.textY = 0;
                        } else {
                            createGiftPremiumLayouts(LocaleController.getString(org.telegram.messenger.R.string.ActionStarGiveawayPrizeTitle), null, null, this.currentMessageObject.messageText, false, LocaleController.getString(org.telegram.messenger.R.string.ActionGiftStarsView), 11, null, this.giftRectSize, true, false);
                            this.textLayout = null;
                            this.textHeight = 0;
                            this.titleLayout = null;
                            this.titleHeight = 0;
                            this.textY = 0;
                        }
                    } else {
                        dialogId = 0;
                        if (i == 33) {
                            tL_messageActionStarGiftPurchaseOffer = (TLRPC.TL_messageActionStarGiftPurchaseOffer) message3.action;
                            spannableStringBuilder2 = new SpannableStringBuilder(charSequenceCreateActionTextWithTopic);
                            spannableStringBuilder2.append((CharSequence) "\n\n");
                            if (tL_messageActionStarGiftPurchaseOffer.accepted) {
                                spannableStringBuilder2.append((CharSequence) AndroidUtilities.replaceTags(LocaleController.getString(org.telegram.messenger.R.string.GiftOfferStatusAccepted)));
                            } else if (tL_messageActionStarGiftPurchaseOffer.declined) {
                                spannableStringBuilder2.append((CharSequence) AndroidUtilities.replaceTags(LocaleController.getString(org.telegram.messenger.R.string.GiftOfferStatusRejected)));
                            } else {
                                iMax = Math.max(0, tL_messageActionStarGiftPurchaseOffer.expires_at - ConnectionsManager.getInstance(this.currentAccount).getCurrentTime());
                                if (iMax == 0) {
                                    spannableStringBuilder2.append((CharSequence) AndroidUtilities.replaceTags(LocaleController.getString(org.telegram.messenger.R.string.GiftOfferStatusExpired)));
                                } else {
                                    shortDuration2 = LocaleController.formatShortDuration2(iMax);
                                    if (shortDuration2.endsWith(".")) {
                                        i3 = 1;
                                        c = 0;
                                        shortDuration2 = shortDuration2.substring(0, shortDuration2.length() - 1);
                                    } else {
                                        i3 = 1;
                                        c = 0;
                                    }
                                    int i13 = org.telegram.messenger.R.string.GiftOfferStatusPending;
                                    Object[] objArr4 = new Object[i3];
                                    objArr4[c] = shortDuration2;
                                    spannableStringBuilder2.append((CharSequence) AndroidUtilities.replaceTags(LocaleController.formatString(i13, objArr4)));
                                }
                            }
                            createGiftPremiumLayouts(null, null, null, spannableStringBuilder2, false, null, 11, null, this.giftRectSize, false, false);
                            this.textLayout = null;
                            this.textHeight = 0;
                            this.titleLayout = null;
                            this.titleHeight = 0;
                            this.textY = 0;
                            this.giftRectEmpty = false;
                        } else if (i == 34) {
                            createGiftPremiumLayouts(null, null, null, charSequenceCreateActionTextWithTopic, false, null, 11, null, this.giftRectSize, false, true);
                            this.textLayout = null;
                            this.textHeight = 0;
                            this.titleLayout = null;
                            this.titleHeight = 0;
                            this.textY = 0;
                            this.giftRectEmpty = true;
                        } else if (i == 35) {
                            tL_messageActionNoForwardsRequest = (TLRPC.TL_messageActionNoForwardsRequest) message3.action;
                            spannableStringBuilder = new SpannableStringBuilder();
                            shortName = DialogObject.getShortName(MessagesController.getInstance(this.currentAccount).getUser(Long.valueOf(messageObject.getDialogId())));
                            if (tL_messageActionNoForwardsRequest.new_value) {
                                if (messageObject.isOut()) {
                                    charSequenceReplaceTags3 = LocaleController.getString(org.telegram.messenger.R.string.SharingOfferDisableHeaderYou);
                                } else {
                                    charSequenceReplaceTags3 = AndroidUtilities.replaceTags(LocaleController.formatString(org.telegram.messenger.R.string.SharingOfferDisableHeaderOther, shortName));
                                }
                                spannableStringBuilder.append(charSequenceReplaceTags3);
                            } else {
                                if (messageObject.isOut()) {
                                    charSequenceReplaceTags2 = LocaleController.getString(org.telegram.messenger.R.string.SharingOfferEnableHeaderYou);
                                } else {
                                    charSequenceReplaceTags2 = AndroidUtilities.replaceTags(LocaleController.formatString(org.telegram.messenger.R.string.SharingOfferEnableHeaderOther, shortName));
                                }
                                spannableStringBuilder.append(charSequenceReplaceTags2);
                            }
                            if (tL_messageActionNoForwardsRequest.new_value) {
                                spannableStringBuilder.append((CharSequence) "\n\n");
                                spannableStringBuilder.append(createOption(LocaleController.getString(org.telegram.messenger.R.string.SharingOfferDisable1), org.telegram.messenger.R.drawable.floating_check));
                                spannableStringBuilder.append((CharSequence) "\n\n");
                                spannableStringBuilder.append(createOption(LocaleController.getString(org.telegram.messenger.R.string.SharingOfferDisable2), org.telegram.messenger.R.drawable.floating_check));
                                spannableStringBuilder.append((CharSequence) "\n\n");
                                spannableStringBuilder.append(createOption(LocaleController.getString(org.telegram.messenger.R.string.SharingOfferDisable3), org.telegram.messenger.R.drawable.floating_check));
                                spannableStringBuilder.append((CharSequence) "\n\n");
                                spannableStringBuilder.append(createOption(LocaleController.getString(org.telegram.messenger.R.string.SharingOfferDisable4), org.telegram.messenger.R.drawable.floating_check));
                            } else {
                                spannableStringBuilder.append((CharSequence) "\n\n");
                                spannableStringBuilder.append(createOption(LocaleController.getString(org.telegram.messenger.R.string.SharingOfferEnable1), org.telegram.messenger.R.drawable.floating_check));
                                spannableStringBuilder.append((CharSequence) "\n\n");
                                spannableStringBuilder.append(createOption(LocaleController.getString(org.telegram.messenger.R.string.SharingOfferEnable2), org.telegram.messenger.R.drawable.floating_check));
                                spannableStringBuilder.append((CharSequence) "\n\n");
                                spannableStringBuilder.append(createOption(LocaleController.getString(org.telegram.messenger.R.string.SharingOfferEnable3), org.telegram.messenger.R.drawable.floating_check));
                                spannableStringBuilder.append((CharSequence) "\n\n");
                                spannableStringBuilder.append(createOption(LocaleController.getString(org.telegram.messenger.R.string.SharingOfferEnable4), org.telegram.messenger.R.drawable.floating_check));
                            }
                            createGiftPremiumLayouts(null, null, null, spannableStringBuilder, false, null, 11, null, this.giftRectSize, false, true);
                            this.textLayout = null;
                            this.textHeight = 0;
                            this.titleLayout = null;
                            this.titleHeight = 0;
                            this.textY = 0;
                            this.giftRectEmpty = true;
                        } else if (i == 31) {
                            TL_stars.StarGift starGift6 = ((TLRPC.TL_chatThemeUniqueGift) ((TLRPC.TL_messageActionSetChatTheme) message3.action).theme).gift;
                            str = starGift6.title + " #" + LocaleController.formatNumber(starGift6.num, ',');
                            fromChatId = messageObject.getFromChatId();
                            if (UserConfig.getInstance(this.currentAccount).getClientUserId() == fromChatId) {
                                string5 = LocaleController.formatString(org.telegram.messenger.R.string.GiftThemesSetByYou, str);
                            } else {
                                string5 = LocaleController.formatString(org.telegram.messenger.R.string.GiftThemesSetByOther, DialogObject.getShortName(this.currentAccount, fromChatId), str);
                            }
                            createGiftPremiumLayouts(null, null, null, AndroidUtilities.replaceTags(string5), false, LocaleController.getString(org.telegram.messenger.R.string.GiftThemesSetActionView), 11, null, this.giftRectSize, true, false);
                            this.textLayout = null;
                            this.textHeight = 0;
                            this.titleLayout = null;
                            this.titleHeight = 0;
                            this.textY = 0;
                        } else if (i == 18) {
                            messageAction = message3.action;
                            if (messageAction instanceof TLRPC.TL_messageActionGiftPremium) {
                                tL_textWithEntities = ((TLRPC.TL_messageActionGiftPremium) messageAction).message;
                            } else if (messageAction instanceof TLRPC.TL_messageActionGiftCode) {
                                tL_textWithEntities = ((TLRPC.TL_messageActionGiftCode) messageAction).message;
                            } else {
                                tL_textWithEntities = null;
                            }
                            if (tL_textWithEntities != null) {
                                string4 = null;
                            } else {
                                string4 = null;
                            }
                            if (string4 == null) {
                                string4 = LocaleController.getString(org.telegram.messenger.R.string.ActionGiftPremiumText);
                            }
                            CharSequence charSequence14 = string4;
                            if (isGiftCode()) {
                                i2 = org.telegram.messenger.R.string.ActionGiftPremiumView;
                            } else {
                                i2 = org.telegram.messenger.R.string.ActionGiftPremiumView;
                            }
                            createGiftPremiumLayouts(LocaleController.formatPluralStringComma("ActionGiftPremiumTitle2", messageObject.messageOwner.action.months), null, null, charSequence14, true, LocaleController.getString(i2), 11, null, this.giftRectSize, false, false);
                        } else if (i == 21) {
                            tL_messageActionSuggestProfilePhoto = (TLRPC.TL_messageActionSuggestProfilePhoto) message3.action;
                            MessagesController messagesController5 = MessagesController.getInstance(this.currentAccount);
                            if (!messageObject.isOutOwner()) {
                                dialogId = messageObject.getDialogId();
                            }
                            user3 = messagesController5.getUser(Long.valueOf(dialogId));
                            if (tL_messageActionSuggestProfilePhoto.video) {
                                z2 = true;
                            } else {
                                z2 = true;
                            }
                            if (user3.id == UserConfig.getInstance(this.currentAccount).clientUserId) {
                                user4 = MessagesController.getInstance(this.currentAccount).getUser(Long.valueOf(messageObject.getDialogId()));
                                if (z2) {
                                    string2 = LocaleController.formatString(org.telegram.messenger.R.string.ActionSuggestVideoFromYouDescription, user4.first_name);
                                } else {
                                    string2 = LocaleController.formatString(org.telegram.messenger.R.string.ActionSuggestPhotoFromYouDescription, user4.first_name);
                                }
                            } else if (z2) {
                                string2 = LocaleController.formatString(org.telegram.messenger.R.string.ActionSuggestVideoToYouDescription, user3.first_name);
                            } else {
                                string2 = LocaleController.formatString(org.telegram.messenger.R.string.ActionSuggestPhotoToYouDescription, user3.first_name);
                            }
                            CharSequence charSequence15 = string2;
                            if (!tL_messageActionSuggestProfilePhoto.video) {
                                string3 = LocaleController.getString(org.telegram.messenger.R.string.ViewVideoAction);
                            } else {
                                string3 = LocaleController.getString(org.telegram.messenger.R.string.ViewVideoAction);
                            }
                            createGiftPremiumLayouts(null, null, null, charSequence15, false, string3, 11, null, this.giftRectSize, true, false);
                            this.textLayout = null;
                            this.textHeight = 0;
                            this.titleLayout = null;
                            this.titleHeight = 0;
                            this.textY = 0;
                        } else if (i == 22) {
                            MessagesController messagesController6 = MessagesController.getInstance(this.currentAccount);
                            if (messageObject.isOutOwner()) {
                                dialogId2 = 0;
                            } else {
                                dialogId2 = messageObject.getDialogId();
                            }
                            user2 = messagesController6.getUser(Long.valueOf(dialogId2));
                            if (messageObject.getDialogId() < 0) {
                                charSequence2 = messageObject.messageText;
                            } else {
                                if (messageObject.isOutOwner()) {
                                }
                                if (user2 == null) {
                                }
                                charSequence = messageObject.messageText;
                                string = LocaleController.getString(org.telegram.messenger.R.string.ViewWallpaperAction);
                                z = true;
                                createGiftPremiumLayouts(null, null, null, charSequence, false, string, 11, null, this.giftRectSize, z, false);
                                this.textLayout = null;
                                this.textHeight = 0;
                                this.titleLayout = null;
                                this.titleHeight = 0;
                                this.textY = 0;
                            }
                            charSequence = charSequence2;
                            string = null;
                            z = true;
                            createGiftPremiumLayouts(null, null, null, charSequence, false, string, 11, null, this.giftRectSize, z, false);
                            this.textLayout = null;
                            this.textHeight = 0;
                            this.titleLayout = null;
                            this.titleHeight = 0;
                            this.textY = 0;
                        } else if (messageObject.isStoryMention()) {
                            user = MessagesController.getInstance(this.currentAccount).getUser(Long.valueOf(messageObject.messageOwner.media.user_id));
                            if (user.self) {
                                charSequenceReplaceTags = AndroidUtilities.replaceTags(LocaleController.formatString(org.telegram.messenger.R.string.StoryYouMentionedTitle, MessagesController.getInstance(this.currentAccount).getUser(Long.valueOf(messageObject.getDialogId())).first_name));
                            } else {
                                charSequenceReplaceTags = AndroidUtilities.replaceTags(LocaleController.formatString(org.telegram.messenger.R.string.StoryMentionedTitle, user.first_name));
                            }
                            createGiftPremiumLayouts(null, null, null, charSequenceReplaceTags, false, LocaleController.getString(org.telegram.messenger.R.string.StoryMentionedAction), 11, null, this.giftRectSize, true, false);
                            this.textLayout = null;
                            this.textHeight = 0;
                            this.titleLayout = null;
                            this.titleHeight = 0;
                            this.textY = 0;
                        }
                    }
                } else {
                    i = messageObject.type;
                    if (i == 11) {
                        ImageReceiver imageReceiver4 = this.imageReceiver;
                        float f4 = (this.previousWidth - AndroidUtilities.roundMessageSize) / 2.0f;
                        float fDp4 = this.textHeight + AndroidUtilities.dp(19.0f);
                        int i14 = AndroidUtilities.roundMessageSize;
                        imageReceiver4.setImageCoords(f4, fDp4, i14, i14);
                    } else if (i == 25) {
                        createGiftPremiumChannelLayouts();
                    } else if (i == 30) {
                        user5 = MessagesController.getInstance(this.currentAccount).getUser(Long.valueOf(this.currentMessageObject.getDialogId()));
                        messageAction2 = messageObject.messageOwner.action;
                        if (messageAction2 instanceof TLRPC.TL_messageActionGiftStars) {
                            CharSequence pluralStringComma4 = LocaleController.formatPluralStringComma("ActionGiftStarsTitle", (int) ((TLRPC.TL_messageActionGiftStars) messageAction2).stars);
                            if (this.currentMessageObject.isOutOwner()) {
                                string9 = LocaleController.formatString(org.telegram.messenger.R.string.ActionGiftStarsSubtitle, UserObject.getForcedFirstName(user5));
                            } else {
                                string9 = LocaleController.getString(org.telegram.messenger.R.string.ActionGiftStarsSubtitleYou);
                            }
                            createGiftPremiumLayouts(pluralStringComma4, null, null, AndroidUtilities.replaceTags(string9), false, LocaleController.getString(org.telegram.messenger.R.string.ActionGiftStarsView), 11, null, this.giftRectSize, true, false);
                        } else if (!(messageAction2 instanceof TLRPC.TL_messageActionStarGiftUnique)) {
                            if (messageAction2 instanceof TLRPC.TL_messageActionStarGift) {
                                tL_messageActionStarGift = (TLRPC.TL_messageActionStarGift) messageAction2;
                                j = tL_messageActionStarGift.convert_stars;
                                clientUserId = UserConfig.getInstance(this.currentAccount).getClientUserId();
                                peer = tL_messageActionStarGift.peer;
                                if (peer != null) {
                                    z3 = false;
                                } else {
                                    z3 = false;
                                }
                                if (messageObject.getDialogId() == clientUserId) {
                                    z4 = false;
                                } else {
                                    z4 = false;
                                }
                                fromChatId2 = messageObject.getFromChatId();
                                if (!tL_messageActionStarGift.prepaid_upgrade) {
                                    fromChatId2 = DialogObject.getPeerDialogId(peer2);
                                }
                                spannableStringBuilder3 = new SpannableStringBuilder();
                                userOrChat = MessagesController.getInstance(this.currentAccount).getUserOrChat(fromChatId2);
                                peerDialogId = DialogObject.getPeerDialogId(tL_messageActionStarGift.to_id);
                                TLObject userOrChat7 = MessagesController.getInstance(this.currentAccount).getUserOrChat(peerDialogId);
                                if (tL_messageActionStarGift.can_upgrade) {
                                    j2 = peerDialogId;
                                    z5 = false;
                                } else {
                                    j2 = peerDialogId;
                                    z5 = false;
                                }
                                if (j2 == 0) {
                                    if (z4) {
                                        if (tL_messageActionStarGift.gift_num <= 0) {
                                            spannableStringBuilder3.append((CharSequence) LocaleController.getString(org.telegram.messenger.R.string.Gift2ActionSelfTitle));
                                        } else {
                                            spannableStringBuilder3.append((CharSequence) LocaleController.getString(org.telegram.messenger.R.string.Gift2ActionSelfTitle));
                                        }
                                    } else {
                                        if (tL_messageActionStarGift.prepaid_upgrade) {
                                            i4 = org.telegram.messenger.R.string.Gift2ActionUpgradeTitle;
                                        } else {
                                            i4 = org.telegram.messenger.R.string.Gift2ActionTitle;
                                        }
                                        spannableStringBuilder3.append((CharSequence) LocaleController.getString(i4)).append((CharSequence) " ");
                                        if (DialogObject.hasPhoto(userOrChat)) {
                                            spannableStringBuilder3.append((CharSequence) "a ");
                                            AvatarSpan avatarSpan14 = new AvatarSpan(this, this.currentAccount, 18.0f);
                                            avatarSpan14.setObject(userOrChat);
                                            spannableStringBuilder3.setSpan(avatarSpan14, spannableStringBuilder3.length() - 2, spannableStringBuilder3.length() - 1, 33);
                                        }
                                        spannableStringBuilder3.append((CharSequence) DialogObject.getShortName(userOrChat));
                                    }
                                } else if (z4) {
                                    if (tL_messageActionStarGift.gift_num <= 0) {
                                        spannableStringBuilder3.append((CharSequence) LocaleController.getString(org.telegram.messenger.R.string.Gift2ActionSelfTitle));
                                    } else {
                                        spannableStringBuilder3.append((CharSequence) LocaleController.getString(org.telegram.messenger.R.string.Gift2ActionSelfTitle));
                                    }
                                } else {
                                    if (tL_messageActionStarGift.prepaid_upgrade) {
                                        i4 = org.telegram.messenger.R.string.Gift2ActionUpgradeTitle;
                                    } else {
                                        i4 = org.telegram.messenger.R.string.Gift2ActionTitle;
                                    }
                                    spannableStringBuilder3.append((CharSequence) LocaleController.getString(i4)).append((CharSequence) " ");
                                    if (DialogObject.hasPhoto(userOrChat)) {
                                        spannableStringBuilder3.append((CharSequence) "a ");
                                        AvatarSpan avatarSpan15 = new AvatarSpan(this, this.currentAccount, 18.0f);
                                        avatarSpan15.setObject(userOrChat);
                                        spannableStringBuilder3.setSpan(avatarSpan15, spannableStringBuilder3.length() - 2, spannableStringBuilder3.length() - 1, 33);
                                    }
                                    spannableStringBuilder3.append((CharSequence) DialogObject.getShortName(userOrChat));
                                }
                                if (messageObject.isOutOwner()) {
                                }
                                if (tL_messageActionStarGift.refunded) {
                                    charSequenceReplaceTags4 = LocaleController.getString(org.telegram.messenger.R.string.Gift2ActionConvertRefundedText);
                                } else {
                                    tL_textWithEntities2 = tL_messageActionStarGift.message;
                                    if (tL_textWithEntities2 == null) {
                                        if (tL_messageActionStarGift.auction_acquired) {
                                            charSequenceReplaceTags4 = LocaleController.formatString(org.telegram.messenger.R.string.Gift2ActionWonActionText, LocaleController.formatNumber(tL_messageActionStarGift.gift.stars + tL_messageActionStarGift.upgrade_stars, ','));
                                        } else if (z3) {
                                            if (tL_messageActionStarGift.converted) {
                                                charSequenceReplaceTags4 = LocaleController.formatPluralStringComma("Gift2ActionConvertedInfo", (int) j);
                                            } else if (!z6) {
                                                charSequenceReplaceTags4 = AndroidUtilities.replaceTags(LocaleController.getString(org.telegram.messenger.R.string.Gift2ActionInfoChannelNoConvert));
                                            } else {
                                                charSequenceReplaceTags4 = AndroidUtilities.replaceTags(LocaleController.getString(org.telegram.messenger.R.string.Gift2ActionInfoChannelNoConvert));
                                            }
                                        } else if (z4) {
                                            if (!tL_messageActionStarGift.converted) {
                                                if (tL_messageActionStarGift.can_upgrade) {
                                                    charSequenceReplaceTags4 = AndroidUtilities.replaceTags(LocaleController.getString(org.telegram.messenger.R.string.Gift2ActionSelfInfoUpgrade));
                                                } else {
                                                    charSequenceReplaceTags4 = AndroidUtilities.replaceTags(LocaleController.getString(org.telegram.messenger.R.string.Gift2ActionSelfInfoNoConvert));
                                                }
                                            } else if (tL_messageActionStarGift.can_upgrade) {
                                                charSequenceReplaceTags4 = AndroidUtilities.replaceTags(LocaleController.getString(org.telegram.messenger.R.string.Gift2ActionSelfInfoUpgrade));
                                            } else {
                                                charSequenceReplaceTags4 = AndroidUtilities.replaceTags(LocaleController.getString(org.telegram.messenger.R.string.Gift2ActionSelfInfoNoConvert));
                                            }
                                        } else if (z5) {
                                            if (messageObject.isOutOwner()) {
                                                string6 = LocaleController.formatString(org.telegram.messenger.R.string.Gift2ActionUpgradeOut, UserObject.getForcedFirstName(user5));
                                            } else {
                                                string6 = LocaleController.getString(org.telegram.messenger.R.string.Gift2ActionUpgrade);
                                            }
                                            charSequenceReplaceTags4 = AndroidUtilities.replaceTags(string6);
                                        } else if (messageObject.isOutOwner()) {
                                            if (!z6) {
                                                if (tL_messageActionStarGift.can_upgrade) {
                                                    charSequenceReplaceTags4 = AndroidUtilities.replaceTags(LocaleController.formatString(org.telegram.messenger.R.string.Gift2ActionOutInfoUpgrade, UserObject.getForcedFirstName(user5)));
                                                } else {
                                                    charSequenceReplaceTags4 = AndroidUtilities.replaceTags(LocaleController.formatString(org.telegram.messenger.R.string.Gift2ActionOutInfoNoConvert, UserObject.getForcedFirstName(user5)));
                                                }
                                            } else if (tL_messageActionStarGift.can_upgrade) {
                                                charSequenceReplaceTags4 = AndroidUtilities.replaceTags(LocaleController.formatString(org.telegram.messenger.R.string.Gift2ActionOutInfoUpgrade, UserObject.getForcedFirstName(user5)));
                                            } else {
                                                charSequenceReplaceTags4 = AndroidUtilities.replaceTags(LocaleController.formatString(org.telegram.messenger.R.string.Gift2ActionOutInfoNoConvert, UserObject.getForcedFirstName(user5)));
                                            }
                                        } else if (tL_messageActionStarGift.converted) {
                                            charSequenceReplaceTags4 = LocaleController.formatPluralStringComma("Gift2ActionConvertedInfo", (int) j);
                                        } else if (tL_messageActionStarGift.saved) {
                                            if (!z6) {
                                                charSequenceReplaceTags4 = LocaleController.getString(org.telegram.messenger.R.string.Gift2ActionBotSavedInfo);
                                            } else {
                                                charSequenceReplaceTags4 = LocaleController.getString(org.telegram.messenger.R.string.Gift2ActionSavedInfo);
                                            }
                                        } else if (!z6) {
                                            charSequenceReplaceTags4 = LocaleController.getString(org.telegram.messenger.R.string.Gift2ActionBotInfo);
                                        } else {
                                            charSequenceReplaceTags4 = AndroidUtilities.replaceTags(LocaleController.formatPluralStringComma("Gift2ActionInfo", (int) j));
                                        }
                                    } else if (tL_messageActionStarGift.auction_acquired) {
                                        charSequenceReplaceTags4 = LocaleController.formatString(org.telegram.messenger.R.string.Gift2ActionWonActionText, LocaleController.formatNumber(tL_messageActionStarGift.gift.stars + tL_messageActionStarGift.upgrade_stars, ','));
                                    } else if (z3) {
                                        if (tL_messageActionStarGift.converted) {
                                            charSequenceReplaceTags4 = LocaleController.formatPluralStringComma("Gift2ActionConvertedInfo", (int) j);
                                        } else if (!z6) {
                                            charSequenceReplaceTags4 = AndroidUtilities.replaceTags(LocaleController.getString(org.telegram.messenger.R.string.Gift2ActionInfoChannelNoConvert));
                                        } else {
                                            charSequenceReplaceTags4 = AndroidUtilities.replaceTags(LocaleController.getString(org.telegram.messenger.R.string.Gift2ActionInfoChannelNoConvert));
                                        }
                                    } else if (z4) {
                                        if (!tL_messageActionStarGift.converted) {
                                            if (tL_messageActionStarGift.can_upgrade) {
                                                charSequenceReplaceTags4 = AndroidUtilities.replaceTags(LocaleController.getString(org.telegram.messenger.R.string.Gift2ActionSelfInfoUpgrade));
                                            } else {
                                                charSequenceReplaceTags4 = AndroidUtilities.replaceTags(LocaleController.getString(org.telegram.messenger.R.string.Gift2ActionSelfInfoNoConvert));
                                            }
                                        } else if (tL_messageActionStarGift.can_upgrade) {
                                            charSequenceReplaceTags4 = AndroidUtilities.replaceTags(LocaleController.getString(org.telegram.messenger.R.string.Gift2ActionSelfInfoUpgrade));
                                        } else {
                                            charSequenceReplaceTags4 = AndroidUtilities.replaceTags(LocaleController.getString(org.telegram.messenger.R.string.Gift2ActionSelfInfoNoConvert));
                                        }
                                    } else if (z5) {
                                        if (messageObject.isOutOwner()) {
                                            string6 = LocaleController.formatString(org.telegram.messenger.R.string.Gift2ActionUpgradeOut, UserObject.getForcedFirstName(user5));
                                        } else {
                                            string6 = LocaleController.getString(org.telegram.messenger.R.string.Gift2ActionUpgrade);
                                        }
                                        charSequenceReplaceTags4 = AndroidUtilities.replaceTags(string6);
                                    } else if (messageObject.isOutOwner()) {
                                        if (!z6) {
                                            if (tL_messageActionStarGift.can_upgrade) {
                                                charSequenceReplaceTags4 = AndroidUtilities.replaceTags(LocaleController.formatString(org.telegram.messenger.R.string.Gift2ActionOutInfoUpgrade, UserObject.getForcedFirstName(user5)));
                                            } else {
                                                charSequenceReplaceTags4 = AndroidUtilities.replaceTags(LocaleController.formatString(org.telegram.messenger.R.string.Gift2ActionOutInfoNoConvert, UserObject.getForcedFirstName(user5)));
                                            }
                                        } else if (tL_messageActionStarGift.can_upgrade) {
                                            charSequenceReplaceTags4 = AndroidUtilities.replaceTags(LocaleController.formatString(org.telegram.messenger.R.string.Gift2ActionOutInfoUpgrade, UserObject.getForcedFirstName(user5)));
                                        } else {
                                            charSequenceReplaceTags4 = AndroidUtilities.replaceTags(LocaleController.formatString(org.telegram.messenger.R.string.Gift2ActionOutInfoNoConvert, UserObject.getForcedFirstName(user5)));
                                        }
                                    } else if (tL_messageActionStarGift.converted) {
                                        charSequenceReplaceTags4 = LocaleController.formatPluralStringComma("Gift2ActionConvertedInfo", (int) j);
                                    } else if (tL_messageActionStarGift.saved) {
                                        if (!z6) {
                                            charSequenceReplaceTags4 = LocaleController.getString(org.telegram.messenger.R.string.Gift2ActionBotSavedInfo);
                                        } else {
                                            charSequenceReplaceTags4 = LocaleController.getString(org.telegram.messenger.R.string.Gift2ActionSavedInfo);
                                        }
                                    } else if (!z6) {
                                        charSequenceReplaceTags4 = LocaleController.getString(org.telegram.messenger.R.string.Gift2ActionBotInfo);
                                    } else {
                                        charSequenceReplaceTags4 = AndroidUtilities.replaceTags(LocaleController.formatPluralStringComma("Gift2ActionInfo", (int) j));
                                    }
                                }
                                starGift2 = tL_messageActionStarGift.gift;
                                if (starGift2 == null) {
                                    string7 = null;
                                } else {
                                    string7 = null;
                                }
                                string8 = LocaleController.getString(org.telegram.messenger.R.string.ActionGiftStarsView);
                                if (messageObject.isOutOwner()) {
                                    charSequence3 = string8;
                                    charSequence3 = string8;
                                    charSequence3 = string8;
                                    if (!messageObject.isOutOwner()) {
                                        SpannableStringBuilder spannableStringBuilder19 = new SpannableStringBuilder();
                                        spannableStringBuilder19.append((CharSequence) "^  ");
                                        ColoredImageSpan coloredImageSpan12 = new ColoredImageSpan(org.telegram.messenger.R.drawable.gift_unpack);
                                        coloredImageSpan12.setScale(0.8f, 0.8f);
                                        spannableStringBuilder19.setSpan(coloredImageSpan12, 0, 1, 33);
                                        spannableStringBuilder19.append((CharSequence) LocaleController.getString(org.telegram.messenger.R.string.Gift2Unpack));
                                        charSequence3 = spannableStringBuilder19;
                                    }
                                } else {
                                    charSequence3 = string8;
                                    charSequence3 = string8;
                                    charSequence3 = string8;
                                    if (!messageObject.isOutOwner()) {
                                        SpannableStringBuilder spannableStringBuilder110 = new SpannableStringBuilder();
                                        spannableStringBuilder110.append((CharSequence) "^  ");
                                        ColoredImageSpan coloredImageSpan13 = new ColoredImageSpan(org.telegram.messenger.R.drawable.gift_unpack);
                                        coloredImageSpan13.setScale(0.8f, 0.8f);
                                        spannableStringBuilder110.setSpan(coloredImageSpan13, 0, 1, 33);
                                        spannableStringBuilder110.append((CharSequence) LocaleController.getString(org.telegram.messenger.R.string.Gift2Unpack));
                                        charSequence3 = spannableStringBuilder110;
                                    }
                                }
                                CharSequence charSequence16 = charSequence3;
                                starGift3 = tL_messageActionStarGift.gift;
                                if (starGift3 != null) {
                                    charSequenceReplaceSingleTagToLink = null;
                                } else {
                                    charSequenceReplaceSingleTagToLink = null;
                                }
                                createGiftPremiumLayouts(spannableStringBuilder3, null, charSequenceReplaceSingleTagToLink, charSequenceReplaceTags4, false, charSequence16, 11, string7, this.giftRectSize, true, false);
                            } else if (messageAction2 instanceof TLRPC.TL_messageActionGiftTon) {
                                createGiftPremiumLayouts(LocaleController.getString(org.telegram.messenger.R.string.ActionGiftTonTitle), null, null, this.currentMessageObject.messageText, false, LocaleController.getString(org.telegram.messenger.R.string.ActionGiftStarsView), 11, null, this.giftRectSize, true, false);
                                this.textLayout = null;
                                this.textHeight = 0;
                                this.titleLayout = null;
                                this.titleHeight = 0;
                                this.textY = 0;
                            } else {
                                createGiftPremiumLayouts(LocaleController.getString(org.telegram.messenger.R.string.ActionStarGiveawayPrizeTitle), null, null, this.currentMessageObject.messageText, false, LocaleController.getString(org.telegram.messenger.R.string.ActionGiftStarsView), 11, null, this.giftRectSize, true, false);
                                this.textLayout = null;
                                this.textHeight = 0;
                                this.titleLayout = null;
                                this.titleHeight = 0;
                                this.textY = 0;
                            }
                        } else if (messageAction2 instanceof TLRPC.TL_messageActionStarGift) {
                            tL_messageActionStarGift = (TLRPC.TL_messageActionStarGift) messageAction2;
                            j = tL_messageActionStarGift.convert_stars;
                            clientUserId = UserConfig.getInstance(this.currentAccount).getClientUserId();
                            peer = tL_messageActionStarGift.peer;
                            if (peer != null) {
                                z3 = false;
                            } else {
                                z3 = false;
                            }
                            if (messageObject.getDialogId() == clientUserId) {
                                z4 = false;
                            } else {
                                z4 = false;
                            }
                            fromChatId2 = messageObject.getFromChatId();
                            if (!tL_messageActionStarGift.prepaid_upgrade) {
                                fromChatId2 = DialogObject.getPeerDialogId(peer2);
                            }
                            spannableStringBuilder3 = new SpannableStringBuilder();
                            userOrChat = MessagesController.getInstance(this.currentAccount).getUserOrChat(fromChatId2);
                            peerDialogId = DialogObject.getPeerDialogId(tL_messageActionStarGift.to_id);
                            TLObject userOrChat8 = MessagesController.getInstance(this.currentAccount).getUserOrChat(peerDialogId);
                            if (tL_messageActionStarGift.can_upgrade) {
                                j2 = peerDialogId;
                                z5 = false;
                            } else {
                                j2 = peerDialogId;
                                z5 = false;
                            }
                            if (j2 == 0) {
                                if (z4) {
                                    if (tL_messageActionStarGift.gift_num <= 0) {
                                        spannableStringBuilder3.append((CharSequence) LocaleController.getString(org.telegram.messenger.R.string.Gift2ActionSelfTitle));
                                    } else {
                                        spannableStringBuilder3.append((CharSequence) LocaleController.getString(org.telegram.messenger.R.string.Gift2ActionSelfTitle));
                                    }
                                } else {
                                    if (tL_messageActionStarGift.prepaid_upgrade) {
                                        i4 = org.telegram.messenger.R.string.Gift2ActionUpgradeTitle;
                                    } else {
                                        i4 = org.telegram.messenger.R.string.Gift2ActionTitle;
                                    }
                                    spannableStringBuilder3.append((CharSequence) LocaleController.getString(i4)).append((CharSequence) " ");
                                    if (DialogObject.hasPhoto(userOrChat)) {
                                        spannableStringBuilder3.append((CharSequence) "a ");
                                        AvatarSpan avatarSpan16 = new AvatarSpan(this, this.currentAccount, 18.0f);
                                        avatarSpan16.setObject(userOrChat);
                                        spannableStringBuilder3.setSpan(avatarSpan16, spannableStringBuilder3.length() - 2, spannableStringBuilder3.length() - 1, 33);
                                    }
                                    spannableStringBuilder3.append((CharSequence) DialogObject.getShortName(userOrChat));
                                }
                            } else if (z4) {
                                if (tL_messageActionStarGift.gift_num <= 0) {
                                    spannableStringBuilder3.append((CharSequence) LocaleController.getString(org.telegram.messenger.R.string.Gift2ActionSelfTitle));
                                } else {
                                    spannableStringBuilder3.append((CharSequence) LocaleController.getString(org.telegram.messenger.R.string.Gift2ActionSelfTitle));
                                }
                            } else {
                                if (tL_messageActionStarGift.prepaid_upgrade) {
                                    i4 = org.telegram.messenger.R.string.Gift2ActionUpgradeTitle;
                                } else {
                                    i4 = org.telegram.messenger.R.string.Gift2ActionTitle;
                                }
                                spannableStringBuilder3.append((CharSequence) LocaleController.getString(i4)).append((CharSequence) " ");
                                if (DialogObject.hasPhoto(userOrChat)) {
                                    spannableStringBuilder3.append((CharSequence) "a ");
                                    AvatarSpan avatarSpan17 = new AvatarSpan(this, this.currentAccount, 18.0f);
                                    avatarSpan17.setObject(userOrChat);
                                    spannableStringBuilder3.setSpan(avatarSpan17, spannableStringBuilder3.length() - 2, spannableStringBuilder3.length() - 1, 33);
                                }
                                spannableStringBuilder3.append((CharSequence) DialogObject.getShortName(userOrChat));
                            }
                            if (messageObject.isOutOwner()) {
                            }
                            if (tL_messageActionStarGift.refunded) {
                                charSequenceReplaceTags4 = LocaleController.getString(org.telegram.messenger.R.string.Gift2ActionConvertRefundedText);
                            } else {
                                tL_textWithEntities2 = tL_messageActionStarGift.message;
                                if (tL_textWithEntities2 == null) {
                                    if (tL_messageActionStarGift.auction_acquired) {
                                        charSequenceReplaceTags4 = LocaleController.formatString(org.telegram.messenger.R.string.Gift2ActionWonActionText, LocaleController.formatNumber(tL_messageActionStarGift.gift.stars + tL_messageActionStarGift.upgrade_stars, ','));
                                    } else if (z3) {
                                        if (tL_messageActionStarGift.converted) {
                                            charSequenceReplaceTags4 = LocaleController.formatPluralStringComma("Gift2ActionConvertedInfo", (int) j);
                                        } else if (!z6) {
                                            charSequenceReplaceTags4 = AndroidUtilities.replaceTags(LocaleController.getString(org.telegram.messenger.R.string.Gift2ActionInfoChannelNoConvert));
                                        } else {
                                            charSequenceReplaceTags4 = AndroidUtilities.replaceTags(LocaleController.getString(org.telegram.messenger.R.string.Gift2ActionInfoChannelNoConvert));
                                        }
                                    } else if (z4) {
                                        if (!tL_messageActionStarGift.converted) {
                                            if (tL_messageActionStarGift.can_upgrade) {
                                                charSequenceReplaceTags4 = AndroidUtilities.replaceTags(LocaleController.getString(org.telegram.messenger.R.string.Gift2ActionSelfInfoUpgrade));
                                            } else {
                                                charSequenceReplaceTags4 = AndroidUtilities.replaceTags(LocaleController.getString(org.telegram.messenger.R.string.Gift2ActionSelfInfoNoConvert));
                                            }
                                        } else if (tL_messageActionStarGift.can_upgrade) {
                                            charSequenceReplaceTags4 = AndroidUtilities.replaceTags(LocaleController.getString(org.telegram.messenger.R.string.Gift2ActionSelfInfoUpgrade));
                                        } else {
                                            charSequenceReplaceTags4 = AndroidUtilities.replaceTags(LocaleController.getString(org.telegram.messenger.R.string.Gift2ActionSelfInfoNoConvert));
                                        }
                                    } else if (z5) {
                                        if (messageObject.isOutOwner()) {
                                            string6 = LocaleController.formatString(org.telegram.messenger.R.string.Gift2ActionUpgradeOut, UserObject.getForcedFirstName(user5));
                                        } else {
                                            string6 = LocaleController.getString(org.telegram.messenger.R.string.Gift2ActionUpgrade);
                                        }
                                        charSequenceReplaceTags4 = AndroidUtilities.replaceTags(string6);
                                    } else if (messageObject.isOutOwner()) {
                                        if (!z6) {
                                            if (tL_messageActionStarGift.can_upgrade) {
                                                charSequenceReplaceTags4 = AndroidUtilities.replaceTags(LocaleController.formatString(org.telegram.messenger.R.string.Gift2ActionOutInfoUpgrade, UserObject.getForcedFirstName(user5)));
                                            } else {
                                                charSequenceReplaceTags4 = AndroidUtilities.replaceTags(LocaleController.formatString(org.telegram.messenger.R.string.Gift2ActionOutInfoNoConvert, UserObject.getForcedFirstName(user5)));
                                            }
                                        } else if (tL_messageActionStarGift.can_upgrade) {
                                            charSequenceReplaceTags4 = AndroidUtilities.replaceTags(LocaleController.formatString(org.telegram.messenger.R.string.Gift2ActionOutInfoUpgrade, UserObject.getForcedFirstName(user5)));
                                        } else {
                                            charSequenceReplaceTags4 = AndroidUtilities.replaceTags(LocaleController.formatString(org.telegram.messenger.R.string.Gift2ActionOutInfoNoConvert, UserObject.getForcedFirstName(user5)));
                                        }
                                    } else if (tL_messageActionStarGift.converted) {
                                        charSequenceReplaceTags4 = LocaleController.formatPluralStringComma("Gift2ActionConvertedInfo", (int) j);
                                    } else if (tL_messageActionStarGift.saved) {
                                        if (!z6) {
                                            charSequenceReplaceTags4 = LocaleController.getString(org.telegram.messenger.R.string.Gift2ActionBotSavedInfo);
                                        } else {
                                            charSequenceReplaceTags4 = LocaleController.getString(org.telegram.messenger.R.string.Gift2ActionSavedInfo);
                                        }
                                    } else if (!z6) {
                                        charSequenceReplaceTags4 = LocaleController.getString(org.telegram.messenger.R.string.Gift2ActionBotInfo);
                                    } else {
                                        charSequenceReplaceTags4 = AndroidUtilities.replaceTags(LocaleController.formatPluralStringComma("Gift2ActionInfo", (int) j));
                                    }
                                } else if (tL_messageActionStarGift.auction_acquired) {
                                    charSequenceReplaceTags4 = LocaleController.formatString(org.telegram.messenger.R.string.Gift2ActionWonActionText, LocaleController.formatNumber(tL_messageActionStarGift.gift.stars + tL_messageActionStarGift.upgrade_stars, ','));
                                } else if (z3) {
                                    if (tL_messageActionStarGift.converted) {
                                        charSequenceReplaceTags4 = LocaleController.formatPluralStringComma("Gift2ActionConvertedInfo", (int) j);
                                    } else if (!z6) {
                                        charSequenceReplaceTags4 = AndroidUtilities.replaceTags(LocaleController.getString(org.telegram.messenger.R.string.Gift2ActionInfoChannelNoConvert));
                                    } else {
                                        charSequenceReplaceTags4 = AndroidUtilities.replaceTags(LocaleController.getString(org.telegram.messenger.R.string.Gift2ActionInfoChannelNoConvert));
                                    }
                                } else if (z4) {
                                    if (!tL_messageActionStarGift.converted) {
                                        if (tL_messageActionStarGift.can_upgrade) {
                                            charSequenceReplaceTags4 = AndroidUtilities.replaceTags(LocaleController.getString(org.telegram.messenger.R.string.Gift2ActionSelfInfoUpgrade));
                                        } else {
                                            charSequenceReplaceTags4 = AndroidUtilities.replaceTags(LocaleController.getString(org.telegram.messenger.R.string.Gift2ActionSelfInfoNoConvert));
                                        }
                                    } else if (tL_messageActionStarGift.can_upgrade) {
                                        charSequenceReplaceTags4 = AndroidUtilities.replaceTags(LocaleController.getString(org.telegram.messenger.R.string.Gift2ActionSelfInfoUpgrade));
                                    } else {
                                        charSequenceReplaceTags4 = AndroidUtilities.replaceTags(LocaleController.getString(org.telegram.messenger.R.string.Gift2ActionSelfInfoNoConvert));
                                    }
                                } else if (z5) {
                                    if (messageObject.isOutOwner()) {
                                        string6 = LocaleController.formatString(org.telegram.messenger.R.string.Gift2ActionUpgradeOut, UserObject.getForcedFirstName(user5));
                                    } else {
                                        string6 = LocaleController.getString(org.telegram.messenger.R.string.Gift2ActionUpgrade);
                                    }
                                    charSequenceReplaceTags4 = AndroidUtilities.replaceTags(string6);
                                } else if (messageObject.isOutOwner()) {
                                    if (!z6) {
                                        if (tL_messageActionStarGift.can_upgrade) {
                                            charSequenceReplaceTags4 = AndroidUtilities.replaceTags(LocaleController.formatString(org.telegram.messenger.R.string.Gift2ActionOutInfoUpgrade, UserObject.getForcedFirstName(user5)));
                                        } else {
                                            charSequenceReplaceTags4 = AndroidUtilities.replaceTags(LocaleController.formatString(org.telegram.messenger.R.string.Gift2ActionOutInfoNoConvert, UserObject.getForcedFirstName(user5)));
                                        }
                                    } else if (tL_messageActionStarGift.can_upgrade) {
                                        charSequenceReplaceTags4 = AndroidUtilities.replaceTags(LocaleController.formatString(org.telegram.messenger.R.string.Gift2ActionOutInfoUpgrade, UserObject.getForcedFirstName(user5)));
                                    } else {
                                        charSequenceReplaceTags4 = AndroidUtilities.replaceTags(LocaleController.formatString(org.telegram.messenger.R.string.Gift2ActionOutInfoNoConvert, UserObject.getForcedFirstName(user5)));
                                    }
                                } else if (tL_messageActionStarGift.converted) {
                                    charSequenceReplaceTags4 = LocaleController.formatPluralStringComma("Gift2ActionConvertedInfo", (int) j);
                                } else if (tL_messageActionStarGift.saved) {
                                    if (!z6) {
                                        charSequenceReplaceTags4 = LocaleController.getString(org.telegram.messenger.R.string.Gift2ActionBotSavedInfo);
                                    } else {
                                        charSequenceReplaceTags4 = LocaleController.getString(org.telegram.messenger.R.string.Gift2ActionSavedInfo);
                                    }
                                } else if (!z6) {
                                    charSequenceReplaceTags4 = LocaleController.getString(org.telegram.messenger.R.string.Gift2ActionBotInfo);
                                } else {
                                    charSequenceReplaceTags4 = AndroidUtilities.replaceTags(LocaleController.formatPluralStringComma("Gift2ActionInfo", (int) j));
                                }
                            }
                            starGift2 = tL_messageActionStarGift.gift;
                            if (starGift2 == null) {
                                string7 = null;
                            } else {
                                string7 = null;
                            }
                            string8 = LocaleController.getString(org.telegram.messenger.R.string.ActionGiftStarsView);
                            if (messageObject.isOutOwner()) {
                                charSequence3 = string8;
                                charSequence3 = string8;
                                charSequence3 = string8;
                                if (!messageObject.isOutOwner()) {
                                    SpannableStringBuilder spannableStringBuilder111 = new SpannableStringBuilder();
                                    spannableStringBuilder111.append((CharSequence) "^  ");
                                    ColoredImageSpan coloredImageSpan14 = new ColoredImageSpan(org.telegram.messenger.R.drawable.gift_unpack);
                                    coloredImageSpan14.setScale(0.8f, 0.8f);
                                    spannableStringBuilder111.setSpan(coloredImageSpan14, 0, 1, 33);
                                    spannableStringBuilder111.append((CharSequence) LocaleController.getString(org.telegram.messenger.R.string.Gift2Unpack));
                                    charSequence3 = spannableStringBuilder111;
                                }
                            } else {
                                charSequence3 = string8;
                                charSequence3 = string8;
                                charSequence3 = string8;
                                if (!messageObject.isOutOwner()) {
                                    SpannableStringBuilder spannableStringBuilder112 = new SpannableStringBuilder();
                                    spannableStringBuilder112.append((CharSequence) "^  ");
                                    ColoredImageSpan coloredImageSpan15 = new ColoredImageSpan(org.telegram.messenger.R.drawable.gift_unpack);
                                    coloredImageSpan15.setScale(0.8f, 0.8f);
                                    spannableStringBuilder112.setSpan(coloredImageSpan15, 0, 1, 33);
                                    spannableStringBuilder112.append((CharSequence) LocaleController.getString(org.telegram.messenger.R.string.Gift2Unpack));
                                    charSequence3 = spannableStringBuilder112;
                                }
                            }
                            CharSequence charSequence17 = charSequence3;
                            starGift3 = tL_messageActionStarGift.gift;
                            if (starGift3 != null) {
                                charSequenceReplaceSingleTagToLink = null;
                            } else {
                                charSequenceReplaceSingleTagToLink = null;
                            }
                            createGiftPremiumLayouts(spannableStringBuilder3, null, charSequenceReplaceSingleTagToLink, charSequenceReplaceTags4, false, charSequence17, 11, string7, this.giftRectSize, true, false);
                        } else if (messageAction2 instanceof TLRPC.TL_messageActionGiftTon) {
                            createGiftPremiumLayouts(LocaleController.getString(org.telegram.messenger.R.string.ActionGiftTonTitle), null, null, this.currentMessageObject.messageText, false, LocaleController.getString(org.telegram.messenger.R.string.ActionGiftStarsView), 11, null, this.giftRectSize, true, false);
                            this.textLayout = null;
                            this.textHeight = 0;
                            this.titleLayout = null;
                            this.titleHeight = 0;
                            this.textY = 0;
                        } else {
                            createGiftPremiumLayouts(LocaleController.getString(org.telegram.messenger.R.string.ActionStarGiveawayPrizeTitle), null, null, this.currentMessageObject.messageText, false, LocaleController.getString(org.telegram.messenger.R.string.ActionGiftStarsView), 11, null, this.giftRectSize, true, false);
                            this.textLayout = null;
                            this.textHeight = 0;
                            this.titleLayout = null;
                            this.titleHeight = 0;
                            this.textY = 0;
                        }
                    } else {
                        dialogId = 0;
                        if (i == 33) {
                            tL_messageActionStarGiftPurchaseOffer = (TLRPC.TL_messageActionStarGiftPurchaseOffer) message3.action;
                            spannableStringBuilder2 = new SpannableStringBuilder(charSequenceCreateActionTextWithTopic);
                            spannableStringBuilder2.append((CharSequence) "\n\n");
                            if (tL_messageActionStarGiftPurchaseOffer.accepted) {
                                spannableStringBuilder2.append((CharSequence) AndroidUtilities.replaceTags(LocaleController.getString(org.telegram.messenger.R.string.GiftOfferStatusAccepted)));
                            } else if (tL_messageActionStarGiftPurchaseOffer.declined) {
                                spannableStringBuilder2.append((CharSequence) AndroidUtilities.replaceTags(LocaleController.getString(org.telegram.messenger.R.string.GiftOfferStatusRejected)));
                            } else {
                                iMax = Math.max(0, tL_messageActionStarGiftPurchaseOffer.expires_at - ConnectionsManager.getInstance(this.currentAccount).getCurrentTime());
                                if (iMax == 0) {
                                    spannableStringBuilder2.append((CharSequence) AndroidUtilities.replaceTags(LocaleController.getString(org.telegram.messenger.R.string.GiftOfferStatusExpired)));
                                } else {
                                    shortDuration2 = LocaleController.formatShortDuration2(iMax);
                                    if (shortDuration2.endsWith(".")) {
                                        i3 = 1;
                                        c = 0;
                                        shortDuration2 = shortDuration2.substring(0, shortDuration2.length() - 1);
                                    } else {
                                        i3 = 1;
                                        c = 0;
                                    }
                                    int i15 = org.telegram.messenger.R.string.GiftOfferStatusPending;
                                    Object[] objArr5 = new Object[i3];
                                    objArr5[c] = shortDuration2;
                                    spannableStringBuilder2.append((CharSequence) AndroidUtilities.replaceTags(LocaleController.formatString(i15, objArr5)));
                                }
                            }
                            createGiftPremiumLayouts(null, null, null, spannableStringBuilder2, false, null, 11, null, this.giftRectSize, false, false);
                            this.textLayout = null;
                            this.textHeight = 0;
                            this.titleLayout = null;
                            this.titleHeight = 0;
                            this.textY = 0;
                            this.giftRectEmpty = false;
                        } else if (i == 34) {
                            createGiftPremiumLayouts(null, null, null, charSequenceCreateActionTextWithTopic, false, null, 11, null, this.giftRectSize, false, true);
                            this.textLayout = null;
                            this.textHeight = 0;
                            this.titleLayout = null;
                            this.titleHeight = 0;
                            this.textY = 0;
                            this.giftRectEmpty = true;
                        } else if (i == 35) {
                            tL_messageActionNoForwardsRequest = (TLRPC.TL_messageActionNoForwardsRequest) message3.action;
                            spannableStringBuilder = new SpannableStringBuilder();
                            shortName = DialogObject.getShortName(MessagesController.getInstance(this.currentAccount).getUser(Long.valueOf(messageObject.getDialogId())));
                            if (tL_messageActionNoForwardsRequest.new_value) {
                                if (messageObject.isOut()) {
                                    charSequenceReplaceTags3 = LocaleController.getString(org.telegram.messenger.R.string.SharingOfferDisableHeaderYou);
                                } else {
                                    charSequenceReplaceTags3 = AndroidUtilities.replaceTags(LocaleController.formatString(org.telegram.messenger.R.string.SharingOfferDisableHeaderOther, shortName));
                                }
                                spannableStringBuilder.append(charSequenceReplaceTags3);
                            } else {
                                if (messageObject.isOut()) {
                                    charSequenceReplaceTags2 = LocaleController.getString(org.telegram.messenger.R.string.SharingOfferEnableHeaderYou);
                                } else {
                                    charSequenceReplaceTags2 = AndroidUtilities.replaceTags(LocaleController.formatString(org.telegram.messenger.R.string.SharingOfferEnableHeaderOther, shortName));
                                }
                                spannableStringBuilder.append(charSequenceReplaceTags2);
                            }
                            if (tL_messageActionNoForwardsRequest.new_value) {
                                spannableStringBuilder.append((CharSequence) "\n\n");
                                spannableStringBuilder.append(createOption(LocaleController.getString(org.telegram.messenger.R.string.SharingOfferDisable1), org.telegram.messenger.R.drawable.floating_check));
                                spannableStringBuilder.append((CharSequence) "\n\n");
                                spannableStringBuilder.append(createOption(LocaleController.getString(org.telegram.messenger.R.string.SharingOfferDisable2), org.telegram.messenger.R.drawable.floating_check));
                                spannableStringBuilder.append((CharSequence) "\n\n");
                                spannableStringBuilder.append(createOption(LocaleController.getString(org.telegram.messenger.R.string.SharingOfferDisable3), org.telegram.messenger.R.drawable.floating_check));
                                spannableStringBuilder.append((CharSequence) "\n\n");
                                spannableStringBuilder.append(createOption(LocaleController.getString(org.telegram.messenger.R.string.SharingOfferDisable4), org.telegram.messenger.R.drawable.floating_check));
                            } else {
                                spannableStringBuilder.append((CharSequence) "\n\n");
                                spannableStringBuilder.append(createOption(LocaleController.getString(org.telegram.messenger.R.string.SharingOfferEnable1), org.telegram.messenger.R.drawable.floating_check));
                                spannableStringBuilder.append((CharSequence) "\n\n");
                                spannableStringBuilder.append(createOption(LocaleController.getString(org.telegram.messenger.R.string.SharingOfferEnable2), org.telegram.messenger.R.drawable.floating_check));
                                spannableStringBuilder.append((CharSequence) "\n\n");
                                spannableStringBuilder.append(createOption(LocaleController.getString(org.telegram.messenger.R.string.SharingOfferEnable3), org.telegram.messenger.R.drawable.floating_check));
                                spannableStringBuilder.append((CharSequence) "\n\n");
                                spannableStringBuilder.append(createOption(LocaleController.getString(org.telegram.messenger.R.string.SharingOfferEnable4), org.telegram.messenger.R.drawable.floating_check));
                            }
                            createGiftPremiumLayouts(null, null, null, spannableStringBuilder, false, null, 11, null, this.giftRectSize, false, true);
                            this.textLayout = null;
                            this.textHeight = 0;
                            this.titleLayout = null;
                            this.titleHeight = 0;
                            this.textY = 0;
                            this.giftRectEmpty = true;
                        } else if (i == 31) {
                            TL_stars.StarGift starGift7 = ((TLRPC.TL_chatThemeUniqueGift) ((TLRPC.TL_messageActionSetChatTheme) message3.action).theme).gift;
                            str = starGift7.title + " #" + LocaleController.formatNumber(starGift7.num, ',');
                            fromChatId = messageObject.getFromChatId();
                            if (UserConfig.getInstance(this.currentAccount).getClientUserId() == fromChatId) {
                                string5 = LocaleController.formatString(org.telegram.messenger.R.string.GiftThemesSetByYou, str);
                            } else {
                                string5 = LocaleController.formatString(org.telegram.messenger.R.string.GiftThemesSetByOther, DialogObject.getShortName(this.currentAccount, fromChatId), str);
                            }
                            createGiftPremiumLayouts(null, null, null, AndroidUtilities.replaceTags(string5), false, LocaleController.getString(org.telegram.messenger.R.string.GiftThemesSetActionView), 11, null, this.giftRectSize, true, false);
                            this.textLayout = null;
                            this.textHeight = 0;
                            this.titleLayout = null;
                            this.titleHeight = 0;
                            this.textY = 0;
                        } else if (i == 18) {
                            messageAction = message3.action;
                            if (messageAction instanceof TLRPC.TL_messageActionGiftPremium) {
                                tL_textWithEntities = ((TLRPC.TL_messageActionGiftPremium) messageAction).message;
                            } else if (messageAction instanceof TLRPC.TL_messageActionGiftCode) {
                                tL_textWithEntities = ((TLRPC.TL_messageActionGiftCode) messageAction).message;
                            } else {
                                tL_textWithEntities = null;
                            }
                            if (tL_textWithEntities != null) {
                                string4 = null;
                            } else {
                                string4 = null;
                            }
                            if (string4 == null) {
                                string4 = LocaleController.getString(org.telegram.messenger.R.string.ActionGiftPremiumText);
                            }
                            CharSequence charSequence18 = string4;
                            if (isGiftCode()) {
                                i2 = org.telegram.messenger.R.string.ActionGiftPremiumView;
                            } else {
                                i2 = org.telegram.messenger.R.string.ActionGiftPremiumView;
                            }
                            createGiftPremiumLayouts(LocaleController.formatPluralStringComma("ActionGiftPremiumTitle2", messageObject.messageOwner.action.months), null, null, charSequence18, true, LocaleController.getString(i2), 11, null, this.giftRectSize, false, false);
                        } else if (i == 21) {
                            tL_messageActionSuggestProfilePhoto = (TLRPC.TL_messageActionSuggestProfilePhoto) message3.action;
                            MessagesController messagesController7 = MessagesController.getInstance(this.currentAccount);
                            if (!messageObject.isOutOwner()) {
                                dialogId = messageObject.getDialogId();
                            }
                            user3 = messagesController7.getUser(Long.valueOf(dialogId));
                            if (tL_messageActionSuggestProfilePhoto.video) {
                                z2 = true;
                            } else {
                                z2 = true;
                            }
                            if (user3.id == UserConfig.getInstance(this.currentAccount).clientUserId) {
                                user4 = MessagesController.getInstance(this.currentAccount).getUser(Long.valueOf(messageObject.getDialogId()));
                                if (z2) {
                                    string2 = LocaleController.formatString(org.telegram.messenger.R.string.ActionSuggestVideoFromYouDescription, user4.first_name);
                                } else {
                                    string2 = LocaleController.formatString(org.telegram.messenger.R.string.ActionSuggestPhotoFromYouDescription, user4.first_name);
                                }
                            } else if (z2) {
                                string2 = LocaleController.formatString(org.telegram.messenger.R.string.ActionSuggestVideoToYouDescription, user3.first_name);
                            } else {
                                string2 = LocaleController.formatString(org.telegram.messenger.R.string.ActionSuggestPhotoToYouDescription, user3.first_name);
                            }
                            CharSequence charSequence19 = string2;
                            if (!tL_messageActionSuggestProfilePhoto.video) {
                                string3 = LocaleController.getString(org.telegram.messenger.R.string.ViewVideoAction);
                            } else {
                                string3 = LocaleController.getString(org.telegram.messenger.R.string.ViewVideoAction);
                            }
                            createGiftPremiumLayouts(null, null, null, charSequence19, false, string3, 11, null, this.giftRectSize, true, false);
                            this.textLayout = null;
                            this.textHeight = 0;
                            this.titleLayout = null;
                            this.titleHeight = 0;
                            this.textY = 0;
                        } else if (i == 22) {
                            MessagesController messagesController8 = MessagesController.getInstance(this.currentAccount);
                            if (messageObject.isOutOwner()) {
                                dialogId2 = 0;
                            } else {
                                dialogId2 = messageObject.getDialogId();
                            }
                            user2 = messagesController8.getUser(Long.valueOf(dialogId2));
                            if (messageObject.getDialogId() < 0) {
                                charSequence2 = messageObject.messageText;
                            } else {
                                if (messageObject.isOutOwner()) {
                                }
                                if (user2 == null) {
                                }
                                charSequence = messageObject.messageText;
                                string = LocaleController.getString(org.telegram.messenger.R.string.ViewWallpaperAction);
                                z = true;
                                createGiftPremiumLayouts(null, null, null, charSequence, false, string, 11, null, this.giftRectSize, z, false);
                                this.textLayout = null;
                                this.textHeight = 0;
                                this.titleLayout = null;
                                this.titleHeight = 0;
                                this.textY = 0;
                            }
                            charSequence = charSequence2;
                            string = null;
                            z = true;
                            createGiftPremiumLayouts(null, null, null, charSequence, false, string, 11, null, this.giftRectSize, z, false);
                            this.textLayout = null;
                            this.textHeight = 0;
                            this.titleLayout = null;
                            this.titleHeight = 0;
                            this.textY = 0;
                        } else if (messageObject.isStoryMention()) {
                            user = MessagesController.getInstance(this.currentAccount).getUser(Long.valueOf(messageObject.messageOwner.media.user_id));
                            if (user.self) {
                                charSequenceReplaceTags = AndroidUtilities.replaceTags(LocaleController.formatString(org.telegram.messenger.R.string.StoryYouMentionedTitle, MessagesController.getInstance(this.currentAccount).getUser(Long.valueOf(messageObject.getDialogId())).first_name));
                            } else {
                                charSequenceReplaceTags = AndroidUtilities.replaceTags(LocaleController.formatString(org.telegram.messenger.R.string.StoryMentionedTitle, user.first_name));
                            }
                            createGiftPremiumLayouts(null, null, null, charSequenceReplaceTags, false, LocaleController.getString(org.telegram.messenger.R.string.StoryMentionedAction), 11, null, this.giftRectSize, true, false);
                            this.textLayout = null;
                            this.textHeight = 0;
                            this.titleLayout = null;
                            this.titleHeight = 0;
                            this.textY = 0;
                        }
                    }
                }
            } else {
                i = messageObject.type;
                if (i == 11) {
                    ImageReceiver imageReceiver5 = this.imageReceiver;
                    float f5 = (this.previousWidth - AndroidUtilities.roundMessageSize) / 2.0f;
                    float fDp5 = this.textHeight + AndroidUtilities.dp(19.0f);
                    int i16 = AndroidUtilities.roundMessageSize;
                    imageReceiver5.setImageCoords(f5, fDp5, i16, i16);
                } else if (i == 25) {
                    createGiftPremiumChannelLayouts();
                } else if (i == 30) {
                    user5 = MessagesController.getInstance(this.currentAccount).getUser(Long.valueOf(this.currentMessageObject.getDialogId()));
                    messageAction2 = messageObject.messageOwner.action;
                    if (messageAction2 instanceof TLRPC.TL_messageActionGiftStars) {
                        CharSequence pluralStringComma5 = LocaleController.formatPluralStringComma("ActionGiftStarsTitle", (int) ((TLRPC.TL_messageActionGiftStars) messageAction2).stars);
                        if (this.currentMessageObject.isOutOwner()) {
                            string9 = LocaleController.formatString(org.telegram.messenger.R.string.ActionGiftStarsSubtitle, UserObject.getForcedFirstName(user5));
                        } else {
                            string9 = LocaleController.getString(org.telegram.messenger.R.string.ActionGiftStarsSubtitleYou);
                        }
                        createGiftPremiumLayouts(pluralStringComma5, null, null, AndroidUtilities.replaceTags(string9), false, LocaleController.getString(org.telegram.messenger.R.string.ActionGiftStarsView), 11, null, this.giftRectSize, true, false);
                    } else if (!(messageAction2 instanceof TLRPC.TL_messageActionStarGiftUnique)) {
                        if (messageAction2 instanceof TLRPC.TL_messageActionStarGift) {
                            tL_messageActionStarGift = (TLRPC.TL_messageActionStarGift) messageAction2;
                            j = tL_messageActionStarGift.convert_stars;
                            clientUserId = UserConfig.getInstance(this.currentAccount).getClientUserId();
                            peer = tL_messageActionStarGift.peer;
                            if (peer != null) {
                                z3 = false;
                            } else {
                                z3 = false;
                            }
                            if (messageObject.getDialogId() == clientUserId) {
                                z4 = false;
                            } else {
                                z4 = false;
                            }
                            fromChatId2 = messageObject.getFromChatId();
                            if (!tL_messageActionStarGift.prepaid_upgrade) {
                                fromChatId2 = DialogObject.getPeerDialogId(peer2);
                            }
                            spannableStringBuilder3 = new SpannableStringBuilder();
                            userOrChat = MessagesController.getInstance(this.currentAccount).getUserOrChat(fromChatId2);
                            peerDialogId = DialogObject.getPeerDialogId(tL_messageActionStarGift.to_id);
                            TLObject userOrChat9 = MessagesController.getInstance(this.currentAccount).getUserOrChat(peerDialogId);
                            if (tL_messageActionStarGift.can_upgrade) {
                                j2 = peerDialogId;
                                z5 = false;
                            } else {
                                j2 = peerDialogId;
                                z5 = false;
                            }
                            if (j2 == 0) {
                                if (z4) {
                                    if (tL_messageActionStarGift.gift_num <= 0) {
                                        spannableStringBuilder3.append((CharSequence) LocaleController.getString(org.telegram.messenger.R.string.Gift2ActionSelfTitle));
                                    } else {
                                        spannableStringBuilder3.append((CharSequence) LocaleController.getString(org.telegram.messenger.R.string.Gift2ActionSelfTitle));
                                    }
                                } else {
                                    if (tL_messageActionStarGift.prepaid_upgrade) {
                                        i4 = org.telegram.messenger.R.string.Gift2ActionUpgradeTitle;
                                    } else {
                                        i4 = org.telegram.messenger.R.string.Gift2ActionTitle;
                                    }
                                    spannableStringBuilder3.append((CharSequence) LocaleController.getString(i4)).append((CharSequence) " ");
                                    if (DialogObject.hasPhoto(userOrChat)) {
                                        spannableStringBuilder3.append((CharSequence) "a ");
                                        AvatarSpan avatarSpan18 = new AvatarSpan(this, this.currentAccount, 18.0f);
                                        avatarSpan18.setObject(userOrChat);
                                        spannableStringBuilder3.setSpan(avatarSpan18, spannableStringBuilder3.length() - 2, spannableStringBuilder3.length() - 1, 33);
                                    }
                                    spannableStringBuilder3.append((CharSequence) DialogObject.getShortName(userOrChat));
                                }
                            } else if (z4) {
                                if (tL_messageActionStarGift.gift_num <= 0) {
                                    spannableStringBuilder3.append((CharSequence) LocaleController.getString(org.telegram.messenger.R.string.Gift2ActionSelfTitle));
                                } else {
                                    spannableStringBuilder3.append((CharSequence) LocaleController.getString(org.telegram.messenger.R.string.Gift2ActionSelfTitle));
                                }
                            } else {
                                if (tL_messageActionStarGift.prepaid_upgrade) {
                                    i4 = org.telegram.messenger.R.string.Gift2ActionUpgradeTitle;
                                } else {
                                    i4 = org.telegram.messenger.R.string.Gift2ActionTitle;
                                }
                                spannableStringBuilder3.append((CharSequence) LocaleController.getString(i4)).append((CharSequence) " ");
                                if (DialogObject.hasPhoto(userOrChat)) {
                                    spannableStringBuilder3.append((CharSequence) "a ");
                                    AvatarSpan avatarSpan19 = new AvatarSpan(this, this.currentAccount, 18.0f);
                                    avatarSpan19.setObject(userOrChat);
                                    spannableStringBuilder3.setSpan(avatarSpan19, spannableStringBuilder3.length() - 2, spannableStringBuilder3.length() - 1, 33);
                                }
                                spannableStringBuilder3.append((CharSequence) DialogObject.getShortName(userOrChat));
                            }
                            if (messageObject.isOutOwner()) {
                            }
                            if (tL_messageActionStarGift.refunded) {
                                charSequenceReplaceTags4 = LocaleController.getString(org.telegram.messenger.R.string.Gift2ActionConvertRefundedText);
                            } else {
                                tL_textWithEntities2 = tL_messageActionStarGift.message;
                                if (tL_textWithEntities2 == null) {
                                    if (tL_messageActionStarGift.auction_acquired) {
                                        charSequenceReplaceTags4 = LocaleController.formatString(org.telegram.messenger.R.string.Gift2ActionWonActionText, LocaleController.formatNumber(tL_messageActionStarGift.gift.stars + tL_messageActionStarGift.upgrade_stars, ','));
                                    } else if (z3) {
                                        if (tL_messageActionStarGift.converted) {
                                            charSequenceReplaceTags4 = LocaleController.formatPluralStringComma("Gift2ActionConvertedInfo", (int) j);
                                        } else if (!z6) {
                                            charSequenceReplaceTags4 = AndroidUtilities.replaceTags(LocaleController.getString(org.telegram.messenger.R.string.Gift2ActionInfoChannelNoConvert));
                                        } else {
                                            charSequenceReplaceTags4 = AndroidUtilities.replaceTags(LocaleController.getString(org.telegram.messenger.R.string.Gift2ActionInfoChannelNoConvert));
                                        }
                                    } else if (z4) {
                                        if (!tL_messageActionStarGift.converted) {
                                            if (tL_messageActionStarGift.can_upgrade) {
                                                charSequenceReplaceTags4 = AndroidUtilities.replaceTags(LocaleController.getString(org.telegram.messenger.R.string.Gift2ActionSelfInfoUpgrade));
                                            } else {
                                                charSequenceReplaceTags4 = AndroidUtilities.replaceTags(LocaleController.getString(org.telegram.messenger.R.string.Gift2ActionSelfInfoNoConvert));
                                            }
                                        } else if (tL_messageActionStarGift.can_upgrade) {
                                            charSequenceReplaceTags4 = AndroidUtilities.replaceTags(LocaleController.getString(org.telegram.messenger.R.string.Gift2ActionSelfInfoUpgrade));
                                        } else {
                                            charSequenceReplaceTags4 = AndroidUtilities.replaceTags(LocaleController.getString(org.telegram.messenger.R.string.Gift2ActionSelfInfoNoConvert));
                                        }
                                    } else if (z5) {
                                        if (messageObject.isOutOwner()) {
                                            string6 = LocaleController.formatString(org.telegram.messenger.R.string.Gift2ActionUpgradeOut, UserObject.getForcedFirstName(user5));
                                        } else {
                                            string6 = LocaleController.getString(org.telegram.messenger.R.string.Gift2ActionUpgrade);
                                        }
                                        charSequenceReplaceTags4 = AndroidUtilities.replaceTags(string6);
                                    } else if (messageObject.isOutOwner()) {
                                        if (!z6) {
                                            if (tL_messageActionStarGift.can_upgrade) {
                                                charSequenceReplaceTags4 = AndroidUtilities.replaceTags(LocaleController.formatString(org.telegram.messenger.R.string.Gift2ActionOutInfoUpgrade, UserObject.getForcedFirstName(user5)));
                                            } else {
                                                charSequenceReplaceTags4 = AndroidUtilities.replaceTags(LocaleController.formatString(org.telegram.messenger.R.string.Gift2ActionOutInfoNoConvert, UserObject.getForcedFirstName(user5)));
                                            }
                                        } else if (tL_messageActionStarGift.can_upgrade) {
                                            charSequenceReplaceTags4 = AndroidUtilities.replaceTags(LocaleController.formatString(org.telegram.messenger.R.string.Gift2ActionOutInfoUpgrade, UserObject.getForcedFirstName(user5)));
                                        } else {
                                            charSequenceReplaceTags4 = AndroidUtilities.replaceTags(LocaleController.formatString(org.telegram.messenger.R.string.Gift2ActionOutInfoNoConvert, UserObject.getForcedFirstName(user5)));
                                        }
                                    } else if (tL_messageActionStarGift.converted) {
                                        charSequenceReplaceTags4 = LocaleController.formatPluralStringComma("Gift2ActionConvertedInfo", (int) j);
                                    } else if (tL_messageActionStarGift.saved) {
                                        if (!z6) {
                                            charSequenceReplaceTags4 = LocaleController.getString(org.telegram.messenger.R.string.Gift2ActionBotSavedInfo);
                                        } else {
                                            charSequenceReplaceTags4 = LocaleController.getString(org.telegram.messenger.R.string.Gift2ActionSavedInfo);
                                        }
                                    } else if (!z6) {
                                        charSequenceReplaceTags4 = LocaleController.getString(org.telegram.messenger.R.string.Gift2ActionBotInfo);
                                    } else {
                                        charSequenceReplaceTags4 = AndroidUtilities.replaceTags(LocaleController.formatPluralStringComma("Gift2ActionInfo", (int) j));
                                    }
                                } else if (tL_messageActionStarGift.auction_acquired) {
                                    charSequenceReplaceTags4 = LocaleController.formatString(org.telegram.messenger.R.string.Gift2ActionWonActionText, LocaleController.formatNumber(tL_messageActionStarGift.gift.stars + tL_messageActionStarGift.upgrade_stars, ','));
                                } else if (z3) {
                                    if (tL_messageActionStarGift.converted) {
                                        charSequenceReplaceTags4 = LocaleController.formatPluralStringComma("Gift2ActionConvertedInfo", (int) j);
                                    } else if (!z6) {
                                        charSequenceReplaceTags4 = AndroidUtilities.replaceTags(LocaleController.getString(org.telegram.messenger.R.string.Gift2ActionInfoChannelNoConvert));
                                    } else {
                                        charSequenceReplaceTags4 = AndroidUtilities.replaceTags(LocaleController.getString(org.telegram.messenger.R.string.Gift2ActionInfoChannelNoConvert));
                                    }
                                } else if (z4) {
                                    if (!tL_messageActionStarGift.converted) {
                                        if (tL_messageActionStarGift.can_upgrade) {
                                            charSequenceReplaceTags4 = AndroidUtilities.replaceTags(LocaleController.getString(org.telegram.messenger.R.string.Gift2ActionSelfInfoUpgrade));
                                        } else {
                                            charSequenceReplaceTags4 = AndroidUtilities.replaceTags(LocaleController.getString(org.telegram.messenger.R.string.Gift2ActionSelfInfoNoConvert));
                                        }
                                    } else if (tL_messageActionStarGift.can_upgrade) {
                                        charSequenceReplaceTags4 = AndroidUtilities.replaceTags(LocaleController.getString(org.telegram.messenger.R.string.Gift2ActionSelfInfoUpgrade));
                                    } else {
                                        charSequenceReplaceTags4 = AndroidUtilities.replaceTags(LocaleController.getString(org.telegram.messenger.R.string.Gift2ActionSelfInfoNoConvert));
                                    }
                                } else if (z5) {
                                    if (messageObject.isOutOwner()) {
                                        string6 = LocaleController.formatString(org.telegram.messenger.R.string.Gift2ActionUpgradeOut, UserObject.getForcedFirstName(user5));
                                    } else {
                                        string6 = LocaleController.getString(org.telegram.messenger.R.string.Gift2ActionUpgrade);
                                    }
                                    charSequenceReplaceTags4 = AndroidUtilities.replaceTags(string6);
                                } else if (messageObject.isOutOwner()) {
                                    if (!z6) {
                                        if (tL_messageActionStarGift.can_upgrade) {
                                            charSequenceReplaceTags4 = AndroidUtilities.replaceTags(LocaleController.formatString(org.telegram.messenger.R.string.Gift2ActionOutInfoUpgrade, UserObject.getForcedFirstName(user5)));
                                        } else {
                                            charSequenceReplaceTags4 = AndroidUtilities.replaceTags(LocaleController.formatString(org.telegram.messenger.R.string.Gift2ActionOutInfoNoConvert, UserObject.getForcedFirstName(user5)));
                                        }
                                    } else if (tL_messageActionStarGift.can_upgrade) {
                                        charSequenceReplaceTags4 = AndroidUtilities.replaceTags(LocaleController.formatString(org.telegram.messenger.R.string.Gift2ActionOutInfoUpgrade, UserObject.getForcedFirstName(user5)));
                                    } else {
                                        charSequenceReplaceTags4 = AndroidUtilities.replaceTags(LocaleController.formatString(org.telegram.messenger.R.string.Gift2ActionOutInfoNoConvert, UserObject.getForcedFirstName(user5)));
                                    }
                                } else if (tL_messageActionStarGift.converted) {
                                    charSequenceReplaceTags4 = LocaleController.formatPluralStringComma("Gift2ActionConvertedInfo", (int) j);
                                } else if (tL_messageActionStarGift.saved) {
                                    if (!z6) {
                                        charSequenceReplaceTags4 = LocaleController.getString(org.telegram.messenger.R.string.Gift2ActionBotSavedInfo);
                                    } else {
                                        charSequenceReplaceTags4 = LocaleController.getString(org.telegram.messenger.R.string.Gift2ActionSavedInfo);
                                    }
                                } else if (!z6) {
                                    charSequenceReplaceTags4 = LocaleController.getString(org.telegram.messenger.R.string.Gift2ActionBotInfo);
                                } else {
                                    charSequenceReplaceTags4 = AndroidUtilities.replaceTags(LocaleController.formatPluralStringComma("Gift2ActionInfo", (int) j));
                                }
                            }
                            starGift2 = tL_messageActionStarGift.gift;
                            if (starGift2 == null) {
                                string7 = null;
                            } else {
                                string7 = null;
                            }
                            string8 = LocaleController.getString(org.telegram.messenger.R.string.ActionGiftStarsView);
                            if (messageObject.isOutOwner()) {
                                charSequence3 = string8;
                                charSequence3 = string8;
                                charSequence3 = string8;
                                if (!messageObject.isOutOwner()) {
                                    SpannableStringBuilder spannableStringBuilder113 = new SpannableStringBuilder();
                                    spannableStringBuilder113.append((CharSequence) "^  ");
                                    ColoredImageSpan coloredImageSpan16 = new ColoredImageSpan(org.telegram.messenger.R.drawable.gift_unpack);
                                    coloredImageSpan16.setScale(0.8f, 0.8f);
                                    spannableStringBuilder113.setSpan(coloredImageSpan16, 0, 1, 33);
                                    spannableStringBuilder113.append((CharSequence) LocaleController.getString(org.telegram.messenger.R.string.Gift2Unpack));
                                    charSequence3 = spannableStringBuilder113;
                                }
                            } else {
                                charSequence3 = string8;
                                charSequence3 = string8;
                                charSequence3 = string8;
                                if (!messageObject.isOutOwner()) {
                                    SpannableStringBuilder spannableStringBuilder114 = new SpannableStringBuilder();
                                    spannableStringBuilder114.append((CharSequence) "^  ");
                                    ColoredImageSpan coloredImageSpan17 = new ColoredImageSpan(org.telegram.messenger.R.drawable.gift_unpack);
                                    coloredImageSpan17.setScale(0.8f, 0.8f);
                                    spannableStringBuilder114.setSpan(coloredImageSpan17, 0, 1, 33);
                                    spannableStringBuilder114.append((CharSequence) LocaleController.getString(org.telegram.messenger.R.string.Gift2Unpack));
                                    charSequence3 = spannableStringBuilder114;
                                }
                            }
                            CharSequence charSequence110 = charSequence3;
                            starGift3 = tL_messageActionStarGift.gift;
                            if (starGift3 != null) {
                                charSequenceReplaceSingleTagToLink = null;
                            } else {
                                charSequenceReplaceSingleTagToLink = null;
                            }
                            createGiftPremiumLayouts(spannableStringBuilder3, null, charSequenceReplaceSingleTagToLink, charSequenceReplaceTags4, false, charSequence110, 11, string7, this.giftRectSize, true, false);
                        } else if (messageAction2 instanceof TLRPC.TL_messageActionGiftTon) {
                            createGiftPremiumLayouts(LocaleController.getString(org.telegram.messenger.R.string.ActionGiftTonTitle), null, null, this.currentMessageObject.messageText, false, LocaleController.getString(org.telegram.messenger.R.string.ActionGiftStarsView), 11, null, this.giftRectSize, true, false);
                            this.textLayout = null;
                            this.textHeight = 0;
                            this.titleLayout = null;
                            this.titleHeight = 0;
                            this.textY = 0;
                        } else {
                            createGiftPremiumLayouts(LocaleController.getString(org.telegram.messenger.R.string.ActionStarGiveawayPrizeTitle), null, null, this.currentMessageObject.messageText, false, LocaleController.getString(org.telegram.messenger.R.string.ActionGiftStarsView), 11, null, this.giftRectSize, true, false);
                            this.textLayout = null;
                            this.textHeight = 0;
                            this.titleLayout = null;
                            this.titleHeight = 0;
                            this.textY = 0;
                        }
                    } else if (messageAction2 instanceof TLRPC.TL_messageActionStarGift) {
                        tL_messageActionStarGift = (TLRPC.TL_messageActionStarGift) messageAction2;
                        j = tL_messageActionStarGift.convert_stars;
                        clientUserId = UserConfig.getInstance(this.currentAccount).getClientUserId();
                        peer = tL_messageActionStarGift.peer;
                        if (peer != null) {
                            z3 = false;
                        } else {
                            z3 = false;
                        }
                        if (messageObject.getDialogId() == clientUserId) {
                            z4 = false;
                        } else {
                            z4 = false;
                        }
                        fromChatId2 = messageObject.getFromChatId();
                        if (!tL_messageActionStarGift.prepaid_upgrade) {
                            fromChatId2 = DialogObject.getPeerDialogId(peer2);
                        }
                        spannableStringBuilder3 = new SpannableStringBuilder();
                        userOrChat = MessagesController.getInstance(this.currentAccount).getUserOrChat(fromChatId2);
                        peerDialogId = DialogObject.getPeerDialogId(tL_messageActionStarGift.to_id);
                        TLObject userOrChat10 = MessagesController.getInstance(this.currentAccount).getUserOrChat(peerDialogId);
                        if (tL_messageActionStarGift.can_upgrade) {
                            j2 = peerDialogId;
                            z5 = false;
                        } else {
                            j2 = peerDialogId;
                            z5 = false;
                        }
                        if (j2 == 0) {
                            if (z4) {
                                if (tL_messageActionStarGift.gift_num <= 0) {
                                    spannableStringBuilder3.append((CharSequence) LocaleController.getString(org.telegram.messenger.R.string.Gift2ActionSelfTitle));
                                } else {
                                    spannableStringBuilder3.append((CharSequence) LocaleController.getString(org.telegram.messenger.R.string.Gift2ActionSelfTitle));
                                }
                            } else {
                                if (tL_messageActionStarGift.prepaid_upgrade) {
                                    i4 = org.telegram.messenger.R.string.Gift2ActionUpgradeTitle;
                                } else {
                                    i4 = org.telegram.messenger.R.string.Gift2ActionTitle;
                                }
                                spannableStringBuilder3.append((CharSequence) LocaleController.getString(i4)).append((CharSequence) " ");
                                if (DialogObject.hasPhoto(userOrChat)) {
                                    spannableStringBuilder3.append((CharSequence) "a ");
                                    AvatarSpan avatarSpan110 = new AvatarSpan(this, this.currentAccount, 18.0f);
                                    avatarSpan110.setObject(userOrChat);
                                    spannableStringBuilder3.setSpan(avatarSpan110, spannableStringBuilder3.length() - 2, spannableStringBuilder3.length() - 1, 33);
                                }
                                spannableStringBuilder3.append((CharSequence) DialogObject.getShortName(userOrChat));
                            }
                        } else if (z4) {
                            if (tL_messageActionStarGift.gift_num <= 0) {
                                spannableStringBuilder3.append((CharSequence) LocaleController.getString(org.telegram.messenger.R.string.Gift2ActionSelfTitle));
                            } else {
                                spannableStringBuilder3.append((CharSequence) LocaleController.getString(org.telegram.messenger.R.string.Gift2ActionSelfTitle));
                            }
                        } else {
                            if (tL_messageActionStarGift.prepaid_upgrade) {
                                i4 = org.telegram.messenger.R.string.Gift2ActionUpgradeTitle;
                            } else {
                                i4 = org.telegram.messenger.R.string.Gift2ActionTitle;
                            }
                            spannableStringBuilder3.append((CharSequence) LocaleController.getString(i4)).append((CharSequence) " ");
                            if (DialogObject.hasPhoto(userOrChat)) {
                                spannableStringBuilder3.append((CharSequence) "a ");
                                AvatarSpan avatarSpan111 = new AvatarSpan(this, this.currentAccount, 18.0f);
                                avatarSpan111.setObject(userOrChat);
                                spannableStringBuilder3.setSpan(avatarSpan111, spannableStringBuilder3.length() - 2, spannableStringBuilder3.length() - 1, 33);
                            }
                            spannableStringBuilder3.append((CharSequence) DialogObject.getShortName(userOrChat));
                        }
                        if (messageObject.isOutOwner()) {
                        }
                        if (tL_messageActionStarGift.refunded) {
                            charSequenceReplaceTags4 = LocaleController.getString(org.telegram.messenger.R.string.Gift2ActionConvertRefundedText);
                        } else {
                            tL_textWithEntities2 = tL_messageActionStarGift.message;
                            if (tL_textWithEntities2 == null) {
                                if (tL_messageActionStarGift.auction_acquired) {
                                    charSequenceReplaceTags4 = LocaleController.formatString(org.telegram.messenger.R.string.Gift2ActionWonActionText, LocaleController.formatNumber(tL_messageActionStarGift.gift.stars + tL_messageActionStarGift.upgrade_stars, ','));
                                } else if (z3) {
                                    if (tL_messageActionStarGift.converted) {
                                        charSequenceReplaceTags4 = LocaleController.formatPluralStringComma("Gift2ActionConvertedInfo", (int) j);
                                    } else if (!z6) {
                                        charSequenceReplaceTags4 = AndroidUtilities.replaceTags(LocaleController.getString(org.telegram.messenger.R.string.Gift2ActionInfoChannelNoConvert));
                                    } else {
                                        charSequenceReplaceTags4 = AndroidUtilities.replaceTags(LocaleController.getString(org.telegram.messenger.R.string.Gift2ActionInfoChannelNoConvert));
                                    }
                                } else if (z4) {
                                    if (!tL_messageActionStarGift.converted) {
                                        if (tL_messageActionStarGift.can_upgrade) {
                                            charSequenceReplaceTags4 = AndroidUtilities.replaceTags(LocaleController.getString(org.telegram.messenger.R.string.Gift2ActionSelfInfoUpgrade));
                                        } else {
                                            charSequenceReplaceTags4 = AndroidUtilities.replaceTags(LocaleController.getString(org.telegram.messenger.R.string.Gift2ActionSelfInfoNoConvert));
                                        }
                                    } else if (tL_messageActionStarGift.can_upgrade) {
                                        charSequenceReplaceTags4 = AndroidUtilities.replaceTags(LocaleController.getString(org.telegram.messenger.R.string.Gift2ActionSelfInfoUpgrade));
                                    } else {
                                        charSequenceReplaceTags4 = AndroidUtilities.replaceTags(LocaleController.getString(org.telegram.messenger.R.string.Gift2ActionSelfInfoNoConvert));
                                    }
                                } else if (z5) {
                                    if (messageObject.isOutOwner()) {
                                        string6 = LocaleController.formatString(org.telegram.messenger.R.string.Gift2ActionUpgradeOut, UserObject.getForcedFirstName(user5));
                                    } else {
                                        string6 = LocaleController.getString(org.telegram.messenger.R.string.Gift2ActionUpgrade);
                                    }
                                    charSequenceReplaceTags4 = AndroidUtilities.replaceTags(string6);
                                } else if (messageObject.isOutOwner()) {
                                    if (!z6) {
                                        if (tL_messageActionStarGift.can_upgrade) {
                                            charSequenceReplaceTags4 = AndroidUtilities.replaceTags(LocaleController.formatString(org.telegram.messenger.R.string.Gift2ActionOutInfoUpgrade, UserObject.getForcedFirstName(user5)));
                                        } else {
                                            charSequenceReplaceTags4 = AndroidUtilities.replaceTags(LocaleController.formatString(org.telegram.messenger.R.string.Gift2ActionOutInfoNoConvert, UserObject.getForcedFirstName(user5)));
                                        }
                                    } else if (tL_messageActionStarGift.can_upgrade) {
                                        charSequenceReplaceTags4 = AndroidUtilities.replaceTags(LocaleController.formatString(org.telegram.messenger.R.string.Gift2ActionOutInfoUpgrade, UserObject.getForcedFirstName(user5)));
                                    } else {
                                        charSequenceReplaceTags4 = AndroidUtilities.replaceTags(LocaleController.formatString(org.telegram.messenger.R.string.Gift2ActionOutInfoNoConvert, UserObject.getForcedFirstName(user5)));
                                    }
                                } else if (tL_messageActionStarGift.converted) {
                                    charSequenceReplaceTags4 = LocaleController.formatPluralStringComma("Gift2ActionConvertedInfo", (int) j);
                                } else if (tL_messageActionStarGift.saved) {
                                    if (!z6) {
                                        charSequenceReplaceTags4 = LocaleController.getString(org.telegram.messenger.R.string.Gift2ActionBotSavedInfo);
                                    } else {
                                        charSequenceReplaceTags4 = LocaleController.getString(org.telegram.messenger.R.string.Gift2ActionSavedInfo);
                                    }
                                } else if (!z6) {
                                    charSequenceReplaceTags4 = LocaleController.getString(org.telegram.messenger.R.string.Gift2ActionBotInfo);
                                } else {
                                    charSequenceReplaceTags4 = AndroidUtilities.replaceTags(LocaleController.formatPluralStringComma("Gift2ActionInfo", (int) j));
                                }
                            } else if (tL_messageActionStarGift.auction_acquired) {
                                charSequenceReplaceTags4 = LocaleController.formatString(org.telegram.messenger.R.string.Gift2ActionWonActionText, LocaleController.formatNumber(tL_messageActionStarGift.gift.stars + tL_messageActionStarGift.upgrade_stars, ','));
                            } else if (z3) {
                                if (tL_messageActionStarGift.converted) {
                                    charSequenceReplaceTags4 = LocaleController.formatPluralStringComma("Gift2ActionConvertedInfo", (int) j);
                                } else if (!z6) {
                                    charSequenceReplaceTags4 = AndroidUtilities.replaceTags(LocaleController.getString(org.telegram.messenger.R.string.Gift2ActionInfoChannelNoConvert));
                                } else {
                                    charSequenceReplaceTags4 = AndroidUtilities.replaceTags(LocaleController.getString(org.telegram.messenger.R.string.Gift2ActionInfoChannelNoConvert));
                                }
                            } else if (z4) {
                                if (!tL_messageActionStarGift.converted) {
                                    if (tL_messageActionStarGift.can_upgrade) {
                                        charSequenceReplaceTags4 = AndroidUtilities.replaceTags(LocaleController.getString(org.telegram.messenger.R.string.Gift2ActionSelfInfoUpgrade));
                                    } else {
                                        charSequenceReplaceTags4 = AndroidUtilities.replaceTags(LocaleController.getString(org.telegram.messenger.R.string.Gift2ActionSelfInfoNoConvert));
                                    }
                                } else if (tL_messageActionStarGift.can_upgrade) {
                                    charSequenceReplaceTags4 = AndroidUtilities.replaceTags(LocaleController.getString(org.telegram.messenger.R.string.Gift2ActionSelfInfoUpgrade));
                                } else {
                                    charSequenceReplaceTags4 = AndroidUtilities.replaceTags(LocaleController.getString(org.telegram.messenger.R.string.Gift2ActionSelfInfoNoConvert));
                                }
                            } else if (z5) {
                                if (messageObject.isOutOwner()) {
                                    string6 = LocaleController.formatString(org.telegram.messenger.R.string.Gift2ActionUpgradeOut, UserObject.getForcedFirstName(user5));
                                } else {
                                    string6 = LocaleController.getString(org.telegram.messenger.R.string.Gift2ActionUpgrade);
                                }
                                charSequenceReplaceTags4 = AndroidUtilities.replaceTags(string6);
                            } else if (messageObject.isOutOwner()) {
                                if (!z6) {
                                    if (tL_messageActionStarGift.can_upgrade) {
                                        charSequenceReplaceTags4 = AndroidUtilities.replaceTags(LocaleController.formatString(org.telegram.messenger.R.string.Gift2ActionOutInfoUpgrade, UserObject.getForcedFirstName(user5)));
                                    } else {
                                        charSequenceReplaceTags4 = AndroidUtilities.replaceTags(LocaleController.formatString(org.telegram.messenger.R.string.Gift2ActionOutInfoNoConvert, UserObject.getForcedFirstName(user5)));
                                    }
                                } else if (tL_messageActionStarGift.can_upgrade) {
                                    charSequenceReplaceTags4 = AndroidUtilities.replaceTags(LocaleController.formatString(org.telegram.messenger.R.string.Gift2ActionOutInfoUpgrade, UserObject.getForcedFirstName(user5)));
                                } else {
                                    charSequenceReplaceTags4 = AndroidUtilities.replaceTags(LocaleController.formatString(org.telegram.messenger.R.string.Gift2ActionOutInfoNoConvert, UserObject.getForcedFirstName(user5)));
                                }
                            } else if (tL_messageActionStarGift.converted) {
                                charSequenceReplaceTags4 = LocaleController.formatPluralStringComma("Gift2ActionConvertedInfo", (int) j);
                            } else if (tL_messageActionStarGift.saved) {
                                if (!z6) {
                                    charSequenceReplaceTags4 = LocaleController.getString(org.telegram.messenger.R.string.Gift2ActionBotSavedInfo);
                                } else {
                                    charSequenceReplaceTags4 = LocaleController.getString(org.telegram.messenger.R.string.Gift2ActionSavedInfo);
                                }
                            } else if (!z6) {
                                charSequenceReplaceTags4 = LocaleController.getString(org.telegram.messenger.R.string.Gift2ActionBotInfo);
                            } else {
                                charSequenceReplaceTags4 = AndroidUtilities.replaceTags(LocaleController.formatPluralStringComma("Gift2ActionInfo", (int) j));
                            }
                        }
                        starGift2 = tL_messageActionStarGift.gift;
                        if (starGift2 == null) {
                            string7 = null;
                        } else {
                            string7 = null;
                        }
                        string8 = LocaleController.getString(org.telegram.messenger.R.string.ActionGiftStarsView);
                        if (messageObject.isOutOwner()) {
                            charSequence3 = string8;
                            charSequence3 = string8;
                            charSequence3 = string8;
                            if (!messageObject.isOutOwner()) {
                                SpannableStringBuilder spannableStringBuilder115 = new SpannableStringBuilder();
                                spannableStringBuilder115.append((CharSequence) "^  ");
                                ColoredImageSpan coloredImageSpan18 = new ColoredImageSpan(org.telegram.messenger.R.drawable.gift_unpack);
                                coloredImageSpan18.setScale(0.8f, 0.8f);
                                spannableStringBuilder115.setSpan(coloredImageSpan18, 0, 1, 33);
                                spannableStringBuilder115.append((CharSequence) LocaleController.getString(org.telegram.messenger.R.string.Gift2Unpack));
                                charSequence3 = spannableStringBuilder115;
                            }
                        } else {
                            charSequence3 = string8;
                            charSequence3 = string8;
                            charSequence3 = string8;
                            if (!messageObject.isOutOwner()) {
                                SpannableStringBuilder spannableStringBuilder116 = new SpannableStringBuilder();
                                spannableStringBuilder116.append((CharSequence) "^  ");
                                ColoredImageSpan coloredImageSpan19 = new ColoredImageSpan(org.telegram.messenger.R.drawable.gift_unpack);
                                coloredImageSpan19.setScale(0.8f, 0.8f);
                                spannableStringBuilder116.setSpan(coloredImageSpan19, 0, 1, 33);
                                spannableStringBuilder116.append((CharSequence) LocaleController.getString(org.telegram.messenger.R.string.Gift2Unpack));
                                charSequence3 = spannableStringBuilder116;
                            }
                        }
                        CharSequence charSequence111 = charSequence3;
                        starGift3 = tL_messageActionStarGift.gift;
                        if (starGift3 != null) {
                            charSequenceReplaceSingleTagToLink = null;
                        } else {
                            charSequenceReplaceSingleTagToLink = null;
                        }
                        createGiftPremiumLayouts(spannableStringBuilder3, null, charSequenceReplaceSingleTagToLink, charSequenceReplaceTags4, false, charSequence111, 11, string7, this.giftRectSize, true, false);
                    } else if (messageAction2 instanceof TLRPC.TL_messageActionGiftTon) {
                        createGiftPremiumLayouts(LocaleController.getString(org.telegram.messenger.R.string.ActionGiftTonTitle), null, null, this.currentMessageObject.messageText, false, LocaleController.getString(org.telegram.messenger.R.string.ActionGiftStarsView), 11, null, this.giftRectSize, true, false);
                        this.textLayout = null;
                        this.textHeight = 0;
                        this.titleLayout = null;
                        this.titleHeight = 0;
                        this.textY = 0;
                    } else {
                        createGiftPremiumLayouts(LocaleController.getString(org.telegram.messenger.R.string.ActionStarGiveawayPrizeTitle), null, null, this.currentMessageObject.messageText, false, LocaleController.getString(org.telegram.messenger.R.string.ActionGiftStarsView), 11, null, this.giftRectSize, true, false);
                        this.textLayout = null;
                        this.textHeight = 0;
                        this.titleLayout = null;
                        this.titleHeight = 0;
                        this.textY = 0;
                    }
                } else {
                    dialogId = 0;
                    if (i == 33) {
                        tL_messageActionStarGiftPurchaseOffer = (TLRPC.TL_messageActionStarGiftPurchaseOffer) message3.action;
                        spannableStringBuilder2 = new SpannableStringBuilder(charSequenceCreateActionTextWithTopic);
                        spannableStringBuilder2.append((CharSequence) "\n\n");
                        if (tL_messageActionStarGiftPurchaseOffer.accepted) {
                            spannableStringBuilder2.append((CharSequence) AndroidUtilities.replaceTags(LocaleController.getString(org.telegram.messenger.R.string.GiftOfferStatusAccepted)));
                        } else if (tL_messageActionStarGiftPurchaseOffer.declined) {
                            spannableStringBuilder2.append((CharSequence) AndroidUtilities.replaceTags(LocaleController.getString(org.telegram.messenger.R.string.GiftOfferStatusRejected)));
                        } else {
                            iMax = Math.max(0, tL_messageActionStarGiftPurchaseOffer.expires_at - ConnectionsManager.getInstance(this.currentAccount).getCurrentTime());
                            if (iMax == 0) {
                                spannableStringBuilder2.append((CharSequence) AndroidUtilities.replaceTags(LocaleController.getString(org.telegram.messenger.R.string.GiftOfferStatusExpired)));
                            } else {
                                shortDuration2 = LocaleController.formatShortDuration2(iMax);
                                if (shortDuration2.endsWith(".")) {
                                    i3 = 1;
                                    c = 0;
                                    shortDuration2 = shortDuration2.substring(0, shortDuration2.length() - 1);
                                } else {
                                    i3 = 1;
                                    c = 0;
                                }
                                int i17 = org.telegram.messenger.R.string.GiftOfferStatusPending;
                                Object[] objArr6 = new Object[i3];
                                objArr6[c] = shortDuration2;
                                spannableStringBuilder2.append((CharSequence) AndroidUtilities.replaceTags(LocaleController.formatString(i17, objArr6)));
                            }
                        }
                        createGiftPremiumLayouts(null, null, null, spannableStringBuilder2, false, null, 11, null, this.giftRectSize, false, false);
                        this.textLayout = null;
                        this.textHeight = 0;
                        this.titleLayout = null;
                        this.titleHeight = 0;
                        this.textY = 0;
                        this.giftRectEmpty = false;
                    } else if (i == 34) {
                        createGiftPremiumLayouts(null, null, null, charSequenceCreateActionTextWithTopic, false, null, 11, null, this.giftRectSize, false, true);
                        this.textLayout = null;
                        this.textHeight = 0;
                        this.titleLayout = null;
                        this.titleHeight = 0;
                        this.textY = 0;
                        this.giftRectEmpty = true;
                    } else if (i == 35) {
                        tL_messageActionNoForwardsRequest = (TLRPC.TL_messageActionNoForwardsRequest) message3.action;
                        spannableStringBuilder = new SpannableStringBuilder();
                        shortName = DialogObject.getShortName(MessagesController.getInstance(this.currentAccount).getUser(Long.valueOf(messageObject.getDialogId())));
                        if (tL_messageActionNoForwardsRequest.new_value) {
                            if (messageObject.isOut()) {
                                charSequenceReplaceTags3 = LocaleController.getString(org.telegram.messenger.R.string.SharingOfferDisableHeaderYou);
                            } else {
                                charSequenceReplaceTags3 = AndroidUtilities.replaceTags(LocaleController.formatString(org.telegram.messenger.R.string.SharingOfferDisableHeaderOther, shortName));
                            }
                            spannableStringBuilder.append(charSequenceReplaceTags3);
                        } else {
                            if (messageObject.isOut()) {
                                charSequenceReplaceTags2 = LocaleController.getString(org.telegram.messenger.R.string.SharingOfferEnableHeaderYou);
                            } else {
                                charSequenceReplaceTags2 = AndroidUtilities.replaceTags(LocaleController.formatString(org.telegram.messenger.R.string.SharingOfferEnableHeaderOther, shortName));
                            }
                            spannableStringBuilder.append(charSequenceReplaceTags2);
                        }
                        if (tL_messageActionNoForwardsRequest.new_value) {
                            spannableStringBuilder.append((CharSequence) "\n\n");
                            spannableStringBuilder.append(createOption(LocaleController.getString(org.telegram.messenger.R.string.SharingOfferDisable1), org.telegram.messenger.R.drawable.floating_check));
                            spannableStringBuilder.append((CharSequence) "\n\n");
                            spannableStringBuilder.append(createOption(LocaleController.getString(org.telegram.messenger.R.string.SharingOfferDisable2), org.telegram.messenger.R.drawable.floating_check));
                            spannableStringBuilder.append((CharSequence) "\n\n");
                            spannableStringBuilder.append(createOption(LocaleController.getString(org.telegram.messenger.R.string.SharingOfferDisable3), org.telegram.messenger.R.drawable.floating_check));
                            spannableStringBuilder.append((CharSequence) "\n\n");
                            spannableStringBuilder.append(createOption(LocaleController.getString(org.telegram.messenger.R.string.SharingOfferDisable4), org.telegram.messenger.R.drawable.floating_check));
                        } else {
                            spannableStringBuilder.append((CharSequence) "\n\n");
                            spannableStringBuilder.append(createOption(LocaleController.getString(org.telegram.messenger.R.string.SharingOfferEnable1), org.telegram.messenger.R.drawable.floating_check));
                            spannableStringBuilder.append((CharSequence) "\n\n");
                            spannableStringBuilder.append(createOption(LocaleController.getString(org.telegram.messenger.R.string.SharingOfferEnable2), org.telegram.messenger.R.drawable.floating_check));
                            spannableStringBuilder.append((CharSequence) "\n\n");
                            spannableStringBuilder.append(createOption(LocaleController.getString(org.telegram.messenger.R.string.SharingOfferEnable3), org.telegram.messenger.R.drawable.floating_check));
                            spannableStringBuilder.append((CharSequence) "\n\n");
                            spannableStringBuilder.append(createOption(LocaleController.getString(org.telegram.messenger.R.string.SharingOfferEnable4), org.telegram.messenger.R.drawable.floating_check));
                        }
                        createGiftPremiumLayouts(null, null, null, spannableStringBuilder, false, null, 11, null, this.giftRectSize, false, true);
                        this.textLayout = null;
                        this.textHeight = 0;
                        this.titleLayout = null;
                        this.titleHeight = 0;
                        this.textY = 0;
                        this.giftRectEmpty = true;
                    } else if (i == 31) {
                        TL_stars.StarGift starGift8 = ((TLRPC.TL_chatThemeUniqueGift) ((TLRPC.TL_messageActionSetChatTheme) message3.action).theme).gift;
                        str = starGift8.title + " #" + LocaleController.formatNumber(starGift8.num, ',');
                        fromChatId = messageObject.getFromChatId();
                        if (UserConfig.getInstance(this.currentAccount).getClientUserId() == fromChatId) {
                            string5 = LocaleController.formatString(org.telegram.messenger.R.string.GiftThemesSetByYou, str);
                        } else {
                            string5 = LocaleController.formatString(org.telegram.messenger.R.string.GiftThemesSetByOther, DialogObject.getShortName(this.currentAccount, fromChatId), str);
                        }
                        createGiftPremiumLayouts(null, null, null, AndroidUtilities.replaceTags(string5), false, LocaleController.getString(org.telegram.messenger.R.string.GiftThemesSetActionView), 11, null, this.giftRectSize, true, false);
                        this.textLayout = null;
                        this.textHeight = 0;
                        this.titleLayout = null;
                        this.titleHeight = 0;
                        this.textY = 0;
                    } else if (i == 18) {
                        messageAction = message3.action;
                        if (messageAction instanceof TLRPC.TL_messageActionGiftPremium) {
                            tL_textWithEntities = ((TLRPC.TL_messageActionGiftPremium) messageAction).message;
                        } else if (messageAction instanceof TLRPC.TL_messageActionGiftCode) {
                            tL_textWithEntities = ((TLRPC.TL_messageActionGiftCode) messageAction).message;
                        } else {
                            tL_textWithEntities = null;
                        }
                        if (tL_textWithEntities != null) {
                            string4 = null;
                        } else {
                            string4 = null;
                        }
                        if (string4 == null) {
                            string4 = LocaleController.getString(org.telegram.messenger.R.string.ActionGiftPremiumText);
                        }
                        CharSequence charSequence112 = string4;
                        if (isGiftCode()) {
                            i2 = org.telegram.messenger.R.string.ActionGiftPremiumView;
                        } else {
                            i2 = org.telegram.messenger.R.string.ActionGiftPremiumView;
                        }
                        createGiftPremiumLayouts(LocaleController.formatPluralStringComma("ActionGiftPremiumTitle2", messageObject.messageOwner.action.months), null, null, charSequence112, true, LocaleController.getString(i2), 11, null, this.giftRectSize, false, false);
                    } else if (i == 21) {
                        tL_messageActionSuggestProfilePhoto = (TLRPC.TL_messageActionSuggestProfilePhoto) message3.action;
                        MessagesController messagesController9 = MessagesController.getInstance(this.currentAccount);
                        if (!messageObject.isOutOwner()) {
                            dialogId = messageObject.getDialogId();
                        }
                        user3 = messagesController9.getUser(Long.valueOf(dialogId));
                        if (tL_messageActionSuggestProfilePhoto.video) {
                            z2 = true;
                        } else {
                            z2 = true;
                        }
                        if (user3.id == UserConfig.getInstance(this.currentAccount).clientUserId) {
                            user4 = MessagesController.getInstance(this.currentAccount).getUser(Long.valueOf(messageObject.getDialogId()));
                            if (z2) {
                                string2 = LocaleController.formatString(org.telegram.messenger.R.string.ActionSuggestVideoFromYouDescription, user4.first_name);
                            } else {
                                string2 = LocaleController.formatString(org.telegram.messenger.R.string.ActionSuggestPhotoFromYouDescription, user4.first_name);
                            }
                        } else if (z2) {
                            string2 = LocaleController.formatString(org.telegram.messenger.R.string.ActionSuggestVideoToYouDescription, user3.first_name);
                        } else {
                            string2 = LocaleController.formatString(org.telegram.messenger.R.string.ActionSuggestPhotoToYouDescription, user3.first_name);
                        }
                        CharSequence charSequence113 = string2;
                        if (!tL_messageActionSuggestProfilePhoto.video) {
                            string3 = LocaleController.getString(org.telegram.messenger.R.string.ViewVideoAction);
                        } else {
                            string3 = LocaleController.getString(org.telegram.messenger.R.string.ViewVideoAction);
                        }
                        createGiftPremiumLayouts(null, null, null, charSequence113, false, string3, 11, null, this.giftRectSize, true, false);
                        this.textLayout = null;
                        this.textHeight = 0;
                        this.titleLayout = null;
                        this.titleHeight = 0;
                        this.textY = 0;
                    } else if (i == 22) {
                        MessagesController messagesController10 = MessagesController.getInstance(this.currentAccount);
                        if (messageObject.isOutOwner()) {
                            dialogId2 = 0;
                        } else {
                            dialogId2 = messageObject.getDialogId();
                        }
                        user2 = messagesController10.getUser(Long.valueOf(dialogId2));
                        if (messageObject.getDialogId() < 0) {
                            charSequence2 = messageObject.messageText;
                        } else {
                            if (messageObject.isOutOwner()) {
                            }
                            if (user2 == null) {
                            }
                            charSequence = messageObject.messageText;
                            string = LocaleController.getString(org.telegram.messenger.R.string.ViewWallpaperAction);
                            z = true;
                            createGiftPremiumLayouts(null, null, null, charSequence, false, string, 11, null, this.giftRectSize, z, false);
                            this.textLayout = null;
                            this.textHeight = 0;
                            this.titleLayout = null;
                            this.titleHeight = 0;
                            this.textY = 0;
                        }
                        charSequence = charSequence2;
                        string = null;
                        z = true;
                        createGiftPremiumLayouts(null, null, null, charSequence, false, string, 11, null, this.giftRectSize, z, false);
                        this.textLayout = null;
                        this.textHeight = 0;
                        this.titleLayout = null;
                        this.titleHeight = 0;
                        this.textY = 0;
                    } else if (messageObject.isStoryMention()) {
                        user = MessagesController.getInstance(this.currentAccount).getUser(Long.valueOf(messageObject.messageOwner.media.user_id));
                        if (user.self) {
                            charSequenceReplaceTags = AndroidUtilities.replaceTags(LocaleController.formatString(org.telegram.messenger.R.string.StoryYouMentionedTitle, MessagesController.getInstance(this.currentAccount).getUser(Long.valueOf(messageObject.getDialogId())).first_name));
                        } else {
                            charSequenceReplaceTags = AndroidUtilities.replaceTags(LocaleController.formatString(org.telegram.messenger.R.string.StoryMentionedTitle, user.first_name));
                        }
                        createGiftPremiumLayouts(null, null, null, charSequenceReplaceTags, false, LocaleController.getString(org.telegram.messenger.R.string.StoryMentionedAction), 11, null, this.giftRectSize, true, false);
                        this.textLayout = null;
                        this.textHeight = 0;
                        this.titleLayout = null;
                        this.titleHeight = 0;
                        this.textY = 0;
                    }
                }
            }
        }
        this.reactionsLayoutInBubble.x = AndroidUtilities.dp(12.0f);
        this.reactionsLayoutInBubble.measure(this.previousWidth - AndroidUtilities.dp(24.0f), 1);
    }

    private CharSequence createOption(String str, int i) {
        SpannableStringBuilder spannableStringBuilder = new SpannableStringBuilder(AndroidUtilities.replaceArrows(str, false, AndroidUtilities.dp(6.0f), -AndroidUtilities.dp(1.3f), 0.8f, i));
        spannableStringBuilder.insert(0, (CharSequence) "*");
        spannableStringBuilder.setSpan(new DialogCell.FixedWidthSpan(AndroidUtilities.dp(18.0f)), 0, 1, 33);
        if (Build.VERSION.SDK_INT >= 29) {
            ChatActionCell$$ExternalSyntheticApiModelOutline1.m();
            spannableStringBuilder.setSpan(ChatActionCell$$ExternalSyntheticApiModelOutline0.m(AndroidUtilities.dp(12.0f)), 0, spannableStringBuilder.length(), 33);
        }
        spannableStringBuilder.setSpan(new AlignmentSpan.Standard(Layout.Alignment.ALIGN_NORMAL), 0, spannableStringBuilder.length(), 33);
        return spannableStringBuilder;
    }

    private void createGiftPremiumChannelLayouts() {
        String string;
        SpannableStringBuilder spannableStringBuilder;
        int iDp = this.giftRectSize - AndroidUtilities.dp(16.0f);
        this.giftTitlePaint.setTextSize(AndroidUtilities.dp(14.0f));
        this.giftTextPaint.setTextSize(AndroidUtilities.dp(13.0f));
        TLRPC.TL_messageActionGiftCode tL_messageActionGiftCode = (TLRPC.TL_messageActionGiftCode) this.currentMessageObject.messageOwner.action;
        int i = tL_messageActionGiftCode.months;
        TLRPC.Chat chat = MessagesController.getInstance(this.currentAccount).getChat(Long.valueOf(-DialogObject.getPeerDialogId(tL_messageActionGiftCode.boost_peer)));
        String str = chat == null ? null : chat.title;
        boolean z = tL_messageActionGiftCode.via_giveaway;
        if (tL_messageActionGiftCode.unclaimed) {
            string = LocaleController.getString("BoostingUnclaimedPrize", org.telegram.messenger.R.string.BoostingUnclaimedPrize);
        } else {
            string = LocaleController.getString("BoostingCongratulations", org.telegram.messenger.R.string.BoostingCongratulations);
        }
        String pluralString = i == 12 ? LocaleController.formatPluralString("BoldYears", 1, new Object[0]) : LocaleController.formatPluralString("BoldMonths", i, new Object[0]);
        if (z) {
            if (tL_messageActionGiftCode.unclaimed) {
                spannableStringBuilder = new SpannableStringBuilder(AndroidUtilities.replaceTags(LocaleController.formatString(org.telegram.messenger.R.string.BoostingYouHaveUnclaimedPrize, str)));
                spannableStringBuilder.append((CharSequence) "\n\n");
                spannableStringBuilder.append((CharSequence) AndroidUtilities.replaceTags(LocaleController.formatString(org.telegram.messenger.R.string.BoostingUnclaimedPrizeDuration, pluralString)));
            } else {
                spannableStringBuilder = new SpannableStringBuilder(AndroidUtilities.replaceTags(LocaleController.formatString(org.telegram.messenger.R.string.BoostingReceivedPrizeFrom, str)));
                spannableStringBuilder.append((CharSequence) "\n\n");
                spannableStringBuilder.append((CharSequence) AndroidUtilities.replaceTags(LocaleController.formatString(org.telegram.messenger.R.string.BoostingReceivedPrizeDuration, pluralString)));
            }
        } else {
            spannableStringBuilder = new SpannableStringBuilder(AndroidUtilities.replaceTags(str == null ? LocaleController.getString(org.telegram.messenger.R.string.BoostingReceivedGiftNoName) : LocaleController.formatString("BoostingReceivedGiftFrom", org.telegram.messenger.R.string.BoostingReceivedGiftFrom, str)));
            spannableStringBuilder.append((CharSequence) "\n\n");
            spannableStringBuilder.append((CharSequence) AndroidUtilities.replaceTags(LocaleController.formatString(org.telegram.messenger.R.string.BoostingReceivedGiftDuration, pluralString)));
        }
        String string2 = LocaleController.getString("BoostingReceivedGiftOpenBtn", org.telegram.messenger.R.string.BoostingReceivedGiftOpenBtn);
        SpannableStringBuilder spannableStringBuilderValueOf = SpannableStringBuilder.valueOf(string);
        spannableStringBuilderValueOf.setSpan(new TypefaceSpan(AndroidUtilities.bold()), 0, spannableStringBuilderValueOf.length(), 33);
        TextPaint textPaint = this.giftTitlePaint;
        Layout.Alignment alignment = Layout.Alignment.ALIGN_CENTER;
        this.giftPremiumTitleLayout = new StaticLayout(spannableStringBuilderValueOf, textPaint, iDp, alignment, 1.1f, 0.0f, false);
        this.giftPremiumSubtitleLayout = null;
        this.giftPremiumReleasedText = null;
        TextLayout textLayout = this.giftPremiumText;
        if (textLayout != null) {
            textLayout.detach();
        }
        TextLayout textLayout2 = new TextLayout();
        this.giftPremiumText = textLayout2;
        textLayout2.setText(spannableStringBuilder, this.giftTextPaint, iDp);
        SpannableStringBuilder spannableStringBuilderValueOf2 = SpannableStringBuilder.valueOf(string2);
        spannableStringBuilderValueOf2.setSpan(new TypefaceSpan(AndroidUtilities.bold()), 0, spannableStringBuilderValueOf2.length(), 33);
        this.giftPremiumTextCollapsed = false;
        this.giftPremiumTextCollapsedHeight = 0;
        this.giftPremiumTextMore = null;
        StaticLayout staticLayout = new StaticLayout(spannableStringBuilderValueOf2, (TextPaint) getThemedPaint("paintChatActionText"), iDp, alignment, 1.0f, 0.0f, false);
        this.giftPremiumButtonLayout = staticLayout;
        this.buttonClickableAsImage = true;
        this.giftPremiumButtonWidth = measureLayoutWidth(staticLayout);
    }

    /* JADX WARN: Failed to calculate best type for var: r21v0 ??
    jadx.core.utils.exceptions.JadxRuntimeException: Type update failed for variable: r21v0 ??, new type: java.lang.CharSequence
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
    /* JADX WARN: Failed to calculate best type for var: r2v0 ??
    jadx.core.utils.exceptions.JadxRuntimeException: Type update failed for variable: r2v0 ??, new type: java.lang.CharSequence
    	at jadx.core.dex.visitors.typeinference.TypeUpdate.apply(TypeUpdate.java:109)
    	at jadx.core.dex.visitors.typeinference.TypeUpdate.apply(TypeUpdate.java:59)
    	at jadx.core.dex.visitors.typeinference.FixTypesVisitor.calculateFromBounds(FixTypesVisitor.java:159)
    	at jadx.core.dex.visitors.typeinference.FixTypesVisitor.setBestType(FixTypesVisitor.java:136)
    	at jadx.core.dex.visitors.typeinference.FixTypesVisitor.deduceType(FixTypesVisitor.java:241)
    	at jadx.core.dex.visitors.typeinference.FixTypesVisitor.tryDeduceTypes(FixTypesVisitor.java:224)
    	at jadx.core.dex.visitors.typeinference.FixTypesVisitor.visit(FixTypesVisitor.java:94)
    Caused by: java.lang.NullPointerException
     */
    /* JADX WARN: Failed to calculate best type for var: r2v0 ??
    jadx.core.utils.exceptions.JadxRuntimeException: Type update failed for variable: r2v0 ??, new type: java.lang.CharSequence
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
    /* JADX WARN: Failed to calculate best type for var: r2v2 ??
    jadx.core.utils.exceptions.JadxRuntimeException: Type update failed for variable: r2v2 ??, new type: java.lang.CharSequence
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
    /* JADX WARN: Failed to calculate best type for var: r2v3 ??
    jadx.core.utils.exceptions.JadxRuntimeException: Type update failed for variable: r2v3 ??, new type: java.lang.CharSequence
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
    /* JADX WARN: Failed to calculate best type for var: r2v4 ??
    jadx.core.utils.exceptions.JadxRuntimeException: Type update failed for variable: r2v4 ??, new type: java.lang.CharSequence
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
    /* JADX WARN: Failed to calculate best type for var: r2v5 ??
    jadx.core.utils.exceptions.JadxRuntimeException: Type update failed for variable: r2v5 ??, new type: java.lang.CharSequence
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
    /* JADX WARN: Failed to calculate best type for var: r7v10 ??
    jadx.core.utils.exceptions.JadxRuntimeException: Type update failed for variable: r7v10 ??, new type: boolean
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
    /* JADX WARN: Failed to calculate best type for var: r7v11 ??
    jadx.core.utils.exceptions.JadxRuntimeException: Type update failed for variable: r7v11 ??, new type: boolean
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
    /* JADX WARN: Failed to calculate best type for var: r7v9 ??
    jadx.core.utils.exceptions.JadxRuntimeException: Type update failed for variable: r7v9 ??, new type: boolean
    	at jadx.core.dex.visitors.typeinference.TypeUpdate.apply(TypeUpdate.java:109)
    	at jadx.core.dex.visitors.typeinference.TypeUpdate.apply(TypeUpdate.java:59)
    	at jadx.core.dex.visitors.typeinference.FixTypesVisitor.calculateFromBounds(FixTypesVisitor.java:159)
    	at jadx.core.dex.visitors.typeinference.FixTypesVisitor.setBestType(FixTypesVisitor.java:136)
    	at jadx.core.dex.visitors.typeinference.FixTypesVisitor.deduceType(FixTypesVisitor.java:241)
    	at jadx.core.dex.visitors.typeinference.FixTypesVisitor.tryDeduceTypes(FixTypesVisitor.java:224)
    	at jadx.core.dex.visitors.typeinference.FixTypesVisitor.visit(FixTypesVisitor.java:94)
    Caused by: java.lang.NullPointerException
     */
    /* JADX WARN: Failed to calculate best type for var: r7v9 ??
    jadx.core.utils.exceptions.JadxRuntimeException: Type update failed for variable: r7v9 ??, new type: boolean
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
    /* JADX WARN: Failed to calculate best type for var: r8v2 ??
    jadx.core.utils.exceptions.JadxRuntimeException: Type update failed for variable: r8v2 ??, new type: org.telegram.ui.Cells.ChatActionCell$TextLayout
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
    /* JADX WARN: Failed to calculate best type for var: r9v23 ??
    jadx.core.utils.exceptions.JadxRuntimeException: Type update failed for variable: r9v23 ??, new type: android.text.SpannableStringBuilder
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
    /* JADX WARN: Failed to calculate best type for var: r9v5 ??
    jadx.core.utils.exceptions.JadxRuntimeException: Type update failed for variable: r9v5 ??, new type: org.telegram.ui.Cells.ChatActionCell$TextLayout
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
    /* JADX WARN: Failed to set immutable type for var: r21v0 ??
    jadx.core.utils.exceptions.JadxRuntimeException: Type update failed for variable: r21v0 ??, new type: java.lang.CharSequence
    	at jadx.core.dex.visitors.typeinference.TypeUpdate.apply(TypeUpdate.java:109)
    	at jadx.core.dex.visitors.typeinference.TypeUpdate.applyWithWiderIgnSame(TypeUpdate.java:73)
    	at jadx.core.dex.visitors.typeinference.TypeInferenceVisitor.setImmutableType(TypeInferenceVisitor.java:111)
    	at jadx.core.dex.visitors.typeinference.TypeInferenceVisitor.lambda$runTypePropagation$1(TypeInferenceVisitor.java:102)
    	at java.base/java.util.ArrayList.forEach(ArrayList.java:1596)
    	at jadx.core.dex.visitors.typeinference.TypeInferenceVisitor.runTypePropagation(TypeInferenceVisitor.java:102)
    	at jadx.core.dex.visitors.typeinference.TypeInferenceVisitor.visit(TypeInferenceVisitor.java:75)
    Caused by: java.lang.NullPointerException
     */
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
    /*  JADX ERROR: Types fix failed
        jadx.core.utils.exceptions.JadxRuntimeException: Type update failed for variable: r7v9 ??, new type: boolean
        	at jadx.core.dex.visitors.typeinference.TypeUpdate.apply(TypeUpdate.java:109)
        	at jadx.core.dex.visitors.typeinference.TypeUpdate.apply(TypeUpdate.java:59)
        	at jadx.core.dex.visitors.typeinference.FixTypesVisitor.tryPossibleTypes(FixTypesVisitor.java:186)
        	at jadx.core.dex.visitors.typeinference.FixTypesVisitor.deduceType(FixTypesVisitor.java:245)
        	at jadx.core.dex.visitors.typeinference.FixTypesVisitor.tryDeduceTypes(FixTypesVisitor.java:224)
        	at jadx.core.dex.visitors.typeinference.FixTypesVisitor.visit(FixTypesVisitor.java:94)
        Caused by: java.lang.NullPointerException
        */
    private void createGiftPremiumLayouts(java.lang.CharSequence r18, java.lang.CharSequence r19, java.lang.CharSequence r20, java.lang.CharSequence r21, boolean r22, java.lang.CharSequence r23, int r24, java.lang.CharSequence r25, int r26, boolean r27, boolean r28) {
        /*
            Method dump skipped, instruction units count: 580
            To view this dump add '--comments-level debug' option
        */
        throw new UnsupportedOperationException("Method not decompiled: org.telegram.ui.Cells.ChatActionCell.createGiftPremiumLayouts(java.lang.CharSequence, java.lang.CharSequence, java.lang.CharSequence, java.lang.CharSequence, boolean, java.lang.CharSequence, int, java.lang.CharSequence, int, boolean, boolean):void");
    }

    private float measureLayoutWidth(Layout layout) {
        float f = 0.0f;
        for (int i = 0; i < layout.getLineCount(); i++) {
            float fCeil = (int) Math.ceil(layout.getLineWidth(i));
            if (fCeil > f) {
                f = fCeil;
            }
        }
        return f;
    }

    public boolean showingCancelButton() {
        RadialProgress2 radialProgress2 = this.radialProgress;
        return radialProgress2 != null && radialProgress2.getIcon() == 3;
    }

    public int getCustomDate() {
        return this.customDate;
    }

    /* JADX WARN: Failed to calculate best type for var: r0v10 ??
    jadx.core.utils.exceptions.JadxRuntimeException: Type update failed for variable: r0v10 ??, new type: org.telegram.ui.Components.Text
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
    /* JADX WARN: Failed to calculate best type for var: r0v115 ??
    jadx.core.utils.exceptions.JadxRuntimeException: Type update failed for variable: r0v115 ??, new type: org.telegram.ui.Components.Premium.StarParticlesView$Drawable
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
    /* JADX WARN: Failed to calculate best type for var: r0v136 ??
    jadx.core.utils.exceptions.JadxRuntimeException: Type update failed for variable: r0v136 ??, new type: float
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
    /* JADX WARN: Failed to calculate best type for var: r0v137 ??
    jadx.core.utils.exceptions.JadxRuntimeException: Type update failed for variable: r0v137 ??, new type: float
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
    /* JADX WARN: Failed to calculate best type for var: r0v138 ??
    jadx.core.utils.exceptions.JadxRuntimeException: Type update failed for variable: r0v138 ??, new type: android.text.StaticLayout
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
    /* JADX WARN: Failed to calculate best type for var: r0v141 ??
    jadx.core.utils.exceptions.JadxRuntimeException: Type update failed for variable: r0v141 ??, new type: float
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
    /* JADX WARN: Failed to calculate best type for var: r0v142 ??
    jadx.core.utils.exceptions.JadxRuntimeException: Type update failed for variable: r0v142 ??, new type: float
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
    /* JADX WARN: Failed to calculate best type for var: r0v152 ??
    jadx.core.utils.exceptions.JadxRuntimeException: Type update failed for variable: r0v152 ??, new type: float
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
    /* JADX WARN: Failed to calculate best type for var: r0v153 ??
    jadx.core.utils.exceptions.JadxRuntimeException: Type update failed for variable: r0v153 ??, new type: float
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
    /* JADX WARN: Failed to calculate best type for var: r0v161 ??
    jadx.core.utils.exceptions.JadxRuntimeException: Type update failed for variable: r0v161 ??, new type: float
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
    /* JADX WARN: Failed to calculate best type for var: r0v162 ??
    jadx.core.utils.exceptions.JadxRuntimeException: Type update failed for variable: r0v162 ??, new type: float
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
    /* JADX WARN: Failed to calculate best type for var: r0v17 ??
    jadx.core.utils.exceptions.JadxRuntimeException: Type update failed for variable: r0v17 ??, new type: float
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
    /* JADX WARN: Failed to calculate best type for var: r0v18 ??
    jadx.core.utils.exceptions.JadxRuntimeException: Type update failed for variable: r0v18 ??, new type: float
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
    /* JADX WARN: Failed to calculate best type for var: r0v42 ??
    jadx.core.utils.exceptions.JadxRuntimeException: Type update failed for variable: r0v42 ??, new type: org.telegram.ui.Components.LoadingDrawable
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
    /* JADX WARN: Failed to calculate best type for var: r0v55 ??
    jadx.core.utils.exceptions.JadxRuntimeException: Type update failed for variable: r0v55 ??, new type: org.telegram.ui.Components.Text
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
    /* JADX WARN: Failed to calculate best type for var: r0v61 ??
    jadx.core.utils.exceptions.JadxRuntimeException: Type update failed for variable: r0v61 ??, new type: org.telegram.ui.Components.LoadingDrawable
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
    /* JADX WARN: Failed to calculate best type for var: r0v62 ??
    jadx.core.utils.exceptions.JadxRuntimeException: Type update failed for variable: r0v62 ??, new type: org.telegram.ui.Components.LoadingDrawable
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
    /* JADX WARN: Failed to calculate best type for var: r0v77 ??
    jadx.core.utils.exceptions.JadxRuntimeException: Type update failed for variable: r0v77 ??, new type: float
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
    /* JADX WARN: Failed to calculate best type for var: r0v78 ??
    jadx.core.utils.exceptions.JadxRuntimeException: Type update failed for variable: r0v78 ??, new type: float
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
    /* JADX WARN: Failed to calculate best type for var: r0v79 ??
    jadx.core.utils.exceptions.JadxRuntimeException: Type update failed for variable: r0v79 ??, new type: android.text.StaticLayout
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
    /* JADX WARN: Failed to calculate best type for var: r0v83 ??
    jadx.core.utils.exceptions.JadxRuntimeException: Type update failed for variable: r0v83 ??, new type: org.telegram.ui.Components.RadialProgressView
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
    /* JADX WARN: Failed to calculate best type for var: r10v17 ??
    jadx.core.utils.exceptions.JadxRuntimeException: Type update failed for variable: r10v17 ??, new type: float
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
    /* JADX WARN: Failed to calculate best type for var: r10v18 ??
    jadx.core.utils.exceptions.JadxRuntimeException: Type update failed for variable: r10v18 ??, new type: float
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
    /* JADX WARN: Failed to calculate best type for var: r10v19 ??
    jadx.core.utils.exceptions.JadxRuntimeException: Type update failed for variable: r10v19 ??, new type: float
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
    /* JADX WARN: Failed to calculate best type for var: r10v22 ??
    jadx.core.utils.exceptions.JadxRuntimeException: Type update failed for variable: r10v22 ??, new type: float
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
    /* JADX WARN: Failed to calculate best type for var: r10v23 ??
    jadx.core.utils.exceptions.JadxRuntimeException: Type update failed for variable: r10v23 ??, new type: float
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
    /* JADX WARN: Failed to calculate best type for var: r10v24 ??
    jadx.core.utils.exceptions.JadxRuntimeException: Type update failed for variable: r10v24 ??, new type: float
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
    /* JADX WARN: Failed to calculate best type for var: r13v10 ??
    jadx.core.utils.exceptions.JadxRuntimeException: Type update failed for variable: r13v10 ??, new type: org.telegram.ui.Cells.ChatActionCell
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
    /* JADX WARN: Failed to calculate best type for var: r13v3 ??
    jadx.core.utils.exceptions.JadxRuntimeException: Type update failed for variable: r13v3 ??, new type: org.telegram.ui.Cells.ChatActionCell
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
    /* JADX WARN: Failed to calculate best type for var: r13v6 ??
    jadx.core.utils.exceptions.JadxRuntimeException: Type update failed for variable: r13v6 ??, new type: org.telegram.ui.Cells.ChatActionCell
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
    /* JADX WARN: Failed to calculate best type for var: r13v7 ??
    jadx.core.utils.exceptions.JadxRuntimeException: Type update failed for variable: r13v7 ??, new type: org.telegram.ui.Cells.ChatActionCell
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
    /* JADX WARN: Failed to calculate best type for var: r13v8 ??
    jadx.core.utils.exceptions.JadxRuntimeException: Type update failed for variable: r13v8 ??, new type: org.telegram.ui.Cells.ChatActionCell
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
    /* JADX WARN: Failed to calculate best type for var: r15v21 ??
    jadx.core.utils.exceptions.JadxRuntimeException: Type update failed for variable: r15v21 ??, new type: org.telegram.ui.Cells.ChatActionCell
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
    /* JADX WARN: Failed to calculate best type for var: r15v22 ??
    jadx.core.utils.exceptions.JadxRuntimeException: Type update failed for variable: r15v22 ??, new type: org.telegram.ui.Cells.ChatActionCell
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
    /* JADX WARN: Failed to calculate best type for var: r15v25 ??
    jadx.core.utils.exceptions.JadxRuntimeException: Type update failed for variable: r15v25 ??, new type: org.telegram.ui.Cells.ChatActionCell
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
    /* JADX WARN: Failed to calculate best type for var: r15v26 ??
    jadx.core.utils.exceptions.JadxRuntimeException: Type update failed for variable: r15v26 ??, new type: org.telegram.ui.Cells.ChatActionCell
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
    /* JADX WARN: Failed to calculate best type for var: r15v27 ??
    jadx.core.utils.exceptions.JadxRuntimeException: Type update failed for variable: r15v27 ??, new type: org.telegram.ui.Cells.ChatActionCell
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
    /* JADX WARN: Failed to calculate best type for var: r15v29 ??
    jadx.core.utils.exceptions.JadxRuntimeException: Type update failed for variable: r15v29 ??, new type: org.telegram.ui.Cells.ChatActionCell
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
    /* JADX WARN: Failed to calculate best type for var: r15v31 ??
    jadx.core.utils.exceptions.JadxRuntimeException: Type update failed for variable: r15v31 ??, new type: org.telegram.ui.Cells.ChatActionCell
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
    /* JADX WARN: Failed to calculate best type for var: r1v0 ??
    jadx.core.utils.exceptions.JadxRuntimeException: Type update failed for variable: r1v0 ??, new type: android.graphics.Canvas
    	at jadx.core.dex.visitors.typeinference.TypeUpdate.apply(TypeUpdate.java:109)
    	at jadx.core.dex.visitors.typeinference.TypeUpdate.apply(TypeUpdate.java:59)
    	at jadx.core.dex.visitors.typeinference.FixTypesVisitor.calculateFromBounds(FixTypesVisitor.java:159)
    	at jadx.core.dex.visitors.typeinference.FixTypesVisitor.setBestType(FixTypesVisitor.java:136)
    	at jadx.core.dex.visitors.typeinference.FixTypesVisitor.deduceType(FixTypesVisitor.java:241)
    	at jadx.core.dex.visitors.typeinference.FixTypesVisitor.tryDeduceTypes(FixTypesVisitor.java:224)
    	at jadx.core.dex.visitors.typeinference.FixTypesVisitor.visit(FixTypesVisitor.java:94)
    Caused by: java.lang.NullPointerException
     */
    /* JADX WARN: Failed to calculate best type for var: r1v0 ??
    jadx.core.utils.exceptions.JadxRuntimeException: Type update failed for variable: r1v0 ??, new type: android.graphics.Canvas
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
    /* JADX WARN: Failed to calculate best type for var: r1v1 ??
    jadx.core.utils.exceptions.JadxRuntimeException: Type update failed for variable: r1v1 ??, new type: android.graphics.Canvas
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
    /* JADX WARN: Failed to calculate best type for var: r1v10 ??
    jadx.core.utils.exceptions.JadxRuntimeException: Type update failed for variable: r1v10 ??, new type: android.graphics.Canvas
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
    /* JADX WARN: Failed to calculate best type for var: r1v15 ??
    jadx.core.utils.exceptions.JadxRuntimeException: Type update failed for variable: r1v15 ??, new type: android.graphics.Canvas
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
    /* JADX WARN: Failed to calculate best type for var: r1v2 ??
    jadx.core.utils.exceptions.JadxRuntimeException: Type update failed for variable: r1v2 ??, new type: android.graphics.Canvas
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
    /* JADX WARN: Failed to calculate best type for var: r1v20 ??
    jadx.core.utils.exceptions.JadxRuntimeException: Type update failed for variable: r1v20 ??, new type: android.graphics.Canvas
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
    /* JADX WARN: Failed to calculate best type for var: r1v21 ??
    jadx.core.utils.exceptions.JadxRuntimeException: Type update failed for variable: r1v21 ??, new type: org.telegram.ui.Components.Text
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
    /* JADX WARN: Failed to calculate best type for var: r1v22 ??
    jadx.core.utils.exceptions.JadxRuntimeException: Type update failed for variable: r1v22 ??, new type: android.graphics.Canvas
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
    /* JADX WARN: Failed to calculate best type for var: r1v7 ??
    jadx.core.utils.exceptions.JadxRuntimeException: Type update failed for variable: r1v7 ??, new type: android.graphics.Canvas
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
    /* JADX WARN: Failed to calculate best type for var: r1v8 ??
    jadx.core.utils.exceptions.JadxRuntimeException: Type update failed for variable: r1v8 ??, new type: android.graphics.Canvas
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
    /* JADX WARN: Failed to calculate best type for var: r1v9 ??
    jadx.core.utils.exceptions.JadxRuntimeException: Type update failed for variable: r1v9 ??, new type: android.graphics.Canvas
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
    /* JADX WARN: Failed to calculate best type for var: r25v3 ??
    jadx.core.utils.exceptions.JadxRuntimeException: Type update failed for variable: r25v3 ??, new type: float
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
    /* JADX WARN: Failed to calculate best type for var: r25v6 ??
    jadx.core.utils.exceptions.JadxRuntimeException: Type update failed for variable: r25v6 ??, new type: float
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
    /* JADX WARN: Failed to calculate best type for var: r2v102 ??
    jadx.core.utils.exceptions.JadxRuntimeException: Type update failed for variable: r2v102 ??, new type: org.telegram.ui.GradientClip
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
    /* JADX WARN: Failed to calculate best type for var: r2v106 ??
    jadx.core.utils.exceptions.JadxRuntimeException: Type update failed for variable: r2v106 ??, new type: org.telegram.ui.GradientClip
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
    /* JADX WARN: Failed to calculate best type for var: r2v129 ??
    jadx.core.utils.exceptions.JadxRuntimeException: Type update failed for variable: r2v129 ??, new type: float
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
    /* JADX WARN: Failed to calculate best type for var: r2v13 ??
    jadx.core.utils.exceptions.JadxRuntimeException: Type update failed for variable: r2v13 ??, new type: org.telegram.messenger.ImageReceiver
    	at jadx.core.dex.visitors.typeinference.TypeUpdate.apply(TypeUpdate.java:109)
    	at jadx.core.dex.visitors.typeinference.TypeUpdate.apply(TypeUpdate.java:59)
    	at jadx.core.dex.visitors.typeinference.FixTypesVisitor.calculateFromBounds(FixTypesVisitor.java:159)
    	at jadx.core.dex.visitors.typeinference.FixTypesVisitor.setBestType(FixTypesVisitor.java:136)
    	at jadx.core.dex.visitors.typeinference.FixTypesVisitor.deduceType(FixTypesVisitor.java:241)
    	at jadx.core.dex.visitors.typeinference.FixTypesVisitor.tryDeduceTypes(FixTypesVisitor.java:224)
    	at jadx.core.dex.visitors.typeinference.FixTypesVisitor.visit(FixTypesVisitor.java:94)
    Caused by: java.lang.NullPointerException
     */
    /* JADX WARN: Failed to calculate best type for var: r2v13 ??
    jadx.core.utils.exceptions.JadxRuntimeException: Type update failed for variable: r2v13 ??, new type: org.telegram.messenger.ImageReceiver
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
    /* JADX WARN: Failed to calculate best type for var: r2v187 ??
    jadx.core.utils.exceptions.JadxRuntimeException: Type update failed for variable: r2v187 ??, new type: float
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
    /* JADX WARN: Failed to calculate best type for var: r2v188 ??
    jadx.core.utils.exceptions.JadxRuntimeException: Type update failed for variable: r2v188 ??, new type: float
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
    /* JADX WARN: Failed to calculate best type for var: r2v191 ??
    jadx.core.utils.exceptions.JadxRuntimeException: Type update failed for variable: r2v191 ??, new type: float
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
    /* JADX WARN: Failed to calculate best type for var: r2v192 ??
    jadx.core.utils.exceptions.JadxRuntimeException: Type update failed for variable: r2v192 ??, new type: float
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
    /* JADX WARN: Failed to calculate best type for var: r2v199 ??
    jadx.core.utils.exceptions.JadxRuntimeException: Type update failed for variable: r2v199 ??, new type: float
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
    /* JADX WARN: Failed to calculate best type for var: r2v200 ??
    jadx.core.utils.exceptions.JadxRuntimeException: Type update failed for variable: r2v200 ??, new type: float
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
    /* JADX WARN: Failed to calculate best type for var: r2v224 ??
    jadx.core.utils.exceptions.JadxRuntimeException: Type update failed for variable: r2v224 ??, new type: float
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
    /* JADX WARN: Failed to calculate best type for var: r2v225 ??
    jadx.core.utils.exceptions.JadxRuntimeException: Type update failed for variable: r2v225 ??, new type: float
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
    /* JADX WARN: Failed to calculate best type for var: r2v23 ??
    jadx.core.utils.exceptions.JadxRuntimeException: Type update failed for variable: r2v23 ??, new type: org.telegram.ui.Components.RadialProgress2
    	at jadx.core.dex.visitors.typeinference.TypeUpdate.apply(TypeUpdate.java:109)
    	at jadx.core.dex.visitors.typeinference.TypeUpdate.apply(TypeUpdate.java:59)
    	at jadx.core.dex.visitors.typeinference.FixTypesVisitor.calculateFromBounds(FixTypesVisitor.java:159)
    	at jadx.core.dex.visitors.typeinference.FixTypesVisitor.setBestType(FixTypesVisitor.java:136)
    	at jadx.core.dex.visitors.typeinference.FixTypesVisitor.deduceType(FixTypesVisitor.java:241)
    	at jadx.core.dex.visitors.typeinference.FixTypesVisitor.tryDeduceTypes(FixTypesVisitor.java:224)
    	at jadx.core.dex.visitors.typeinference.FixTypesVisitor.visit(FixTypesVisitor.java:94)
    Caused by: java.lang.NullPointerException
     */
    /* JADX WARN: Failed to calculate best type for var: r2v23 ??
    jadx.core.utils.exceptions.JadxRuntimeException: Type update failed for variable: r2v23 ??, new type: org.telegram.ui.Components.RadialProgress2
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
    /* JADX WARN: Failed to calculate best type for var: r2v249 ??
    jadx.core.utils.exceptions.JadxRuntimeException: Type update failed for variable: r2v249 ??, new type: float
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
    /* JADX WARN: Failed to calculate best type for var: r2v278 ??
    jadx.core.utils.exceptions.JadxRuntimeException: Type update failed for variable: r2v278 ??, new type: org.telegram.ui.Stars.StarGiftUniqueActionLayout
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
    /* JADX WARN: Failed to calculate best type for var: r2v280 ??
    jadx.core.utils.exceptions.JadxRuntimeException: Type update failed for variable: r2v280 ??, new type: org.telegram.ui.Stars.StarGiftUniqueActionLayout
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
    /* JADX WARN: Failed to calculate best type for var: r2v30 ??
    jadx.core.utils.exceptions.JadxRuntimeException: Type update failed for variable: r2v30 ??, new type: org.telegram.ui.Components.RadialProgress2
    	at jadx.core.dex.visitors.typeinference.TypeUpdate.apply(TypeUpdate.java:109)
    	at jadx.core.dex.visitors.typeinference.TypeUpdate.apply(TypeUpdate.java:59)
    	at jadx.core.dex.visitors.typeinference.FixTypesVisitor.calculateFromBounds(FixTypesVisitor.java:159)
    	at jadx.core.dex.visitors.typeinference.FixTypesVisitor.setBestType(FixTypesVisitor.java:136)
    	at jadx.core.dex.visitors.typeinference.FixTypesVisitor.deduceType(FixTypesVisitor.java:241)
    	at jadx.core.dex.visitors.typeinference.FixTypesVisitor.tryDeduceTypes(FixTypesVisitor.java:224)
    	at jadx.core.dex.visitors.typeinference.FixTypesVisitor.visit(FixTypesVisitor.java:94)
    Caused by: java.lang.NullPointerException
     */
    /* JADX WARN: Failed to calculate best type for var: r2v30 ??
    jadx.core.utils.exceptions.JadxRuntimeException: Type update failed for variable: r2v30 ??, new type: org.telegram.ui.Components.RadialProgress2
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
    /* JADX WARN: Failed to calculate best type for var: r2v38 ??
    jadx.core.utils.exceptions.JadxRuntimeException: Type update failed for variable: r2v38 ??, new type: android.graphics.Path
    	at jadx.core.dex.visitors.typeinference.TypeUpdate.apply(TypeUpdate.java:109)
    	at jadx.core.dex.visitors.typeinference.TypeUpdate.apply(TypeUpdate.java:59)
    	at jadx.core.dex.visitors.typeinference.FixTypesVisitor.calculateFromBounds(FixTypesVisitor.java:159)
    	at jadx.core.dex.visitors.typeinference.FixTypesVisitor.setBestType(FixTypesVisitor.java:136)
    	at jadx.core.dex.visitors.typeinference.FixTypesVisitor.deduceType(FixTypesVisitor.java:241)
    	at jadx.core.dex.visitors.typeinference.FixTypesVisitor.tryDeduceTypes(FixTypesVisitor.java:224)
    	at jadx.core.dex.visitors.typeinference.FixTypesVisitor.visit(FixTypesVisitor.java:94)
    Caused by: java.lang.NullPointerException
     */
    /* JADX WARN: Failed to calculate best type for var: r2v38 ??
    jadx.core.utils.exceptions.JadxRuntimeException: Type update failed for variable: r2v38 ??, new type: android.graphics.Path
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
    /* JADX WARN: Failed to calculate best type for var: r2v41 ??
    jadx.core.utils.exceptions.JadxRuntimeException: Type update failed for variable: r2v41 ??, new type: android.graphics.drawable.Drawable
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
    /* JADX WARN: Failed to calculate best type for var: r2v43 ??
    jadx.core.utils.exceptions.JadxRuntimeException: Type update failed for variable: r2v43 ??, new type: org.telegram.ui.Gifts.GiftSheet$CardBackground
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
    /* JADX WARN: Failed to calculate best type for var: r2v45 ??
    jadx.core.utils.exceptions.JadxRuntimeException: Type update failed for variable: r2v45 ??, new type: org.telegram.ui.Components.SuggestBirthdayActionLayout
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
    /* JADX WARN: Failed to calculate best type for var: r2v53 ??
    jadx.core.utils.exceptions.JadxRuntimeException: Type update failed for variable: r2v53 ??, new type: float
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
    /* JADX WARN: Failed to calculate best type for var: r2v54 ??
    jadx.core.utils.exceptions.JadxRuntimeException: Type update failed for variable: r2v54 ??, new type: float
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
    /* JADX WARN: Failed to calculate best type for var: r2v76 ??
    jadx.core.utils.exceptions.JadxRuntimeException: Type update failed for variable: r2v76 ??, new type: float
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
    /* JADX WARN: Failed to calculate best type for var: r2v77 ??
    jadx.core.utils.exceptions.JadxRuntimeException: Type update failed for variable: r2v77 ??, new type: float
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
    /* JADX WARN: Failed to calculate best type for var: r2v97 ??
    jadx.core.utils.exceptions.JadxRuntimeException: Type update failed for variable: r2v97 ??, new type: org.telegram.ui.GradientClip
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
    /* JADX WARN: Failed to calculate best type for var: r39v0 'this'  ??
    jadx.core.utils.exceptions.JadxRuntimeException: Type update failed for variable: r39v0 'this'  ??, new type: org.telegram.ui.Cells.ChatActionCell
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
    /* JADX WARN: Failed to calculate best type for var: r3v137 ??
    jadx.core.utils.exceptions.JadxRuntimeException: Type update failed for variable: r3v137 ??, new type: float
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
    /* JADX WARN: Failed to calculate best type for var: r3v138 ??
    jadx.core.utils.exceptions.JadxRuntimeException: Type update failed for variable: r3v138 ??, new type: float
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
    /* JADX WARN: Failed to calculate best type for var: r3v144 ??
    jadx.core.utils.exceptions.JadxRuntimeException: Type update failed for variable: r3v144 ??, new type: float
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
    /* JADX WARN: Failed to calculate best type for var: r3v145 ??
    jadx.core.utils.exceptions.JadxRuntimeException: Type update failed for variable: r3v145 ??, new type: float
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
    /* JADX WARN: Failed to calculate best type for var: r3v148 ??
    jadx.core.utils.exceptions.JadxRuntimeException: Type update failed for variable: r3v148 ??, new type: float
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
    /* JADX WARN: Failed to calculate best type for var: r3v149 ??
    jadx.core.utils.exceptions.JadxRuntimeException: Type update failed for variable: r3v149 ??, new type: float
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
    /* JADX WARN: Failed to calculate best type for var: r3v155 ??
    jadx.core.utils.exceptions.JadxRuntimeException: Type update failed for variable: r3v155 ??, new type: float
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
    /* JADX WARN: Failed to calculate best type for var: r3v156 ??
    jadx.core.utils.exceptions.JadxRuntimeException: Type update failed for variable: r3v156 ??, new type: float
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
    /* JADX WARN: Failed to calculate best type for var: r3v172 ??
    jadx.core.utils.exceptions.JadxRuntimeException: Type update failed for variable: r3v172 ??, new type: float
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
    /* JADX WARN: Failed to calculate best type for var: r3v173 ??
    jadx.core.utils.exceptions.JadxRuntimeException: Type update failed for variable: r3v173 ??, new type: float
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
    /* JADX WARN: Failed to calculate best type for var: r3v174 ??
    jadx.core.utils.exceptions.JadxRuntimeException: Type update failed for variable: r3v174 ??, new type: float
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
    /* JADX WARN: Failed to calculate best type for var: r3v181 ??
    jadx.core.utils.exceptions.JadxRuntimeException: Type update failed for variable: r3v181 ??, new type: org.telegram.ui.Components.Text
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
    /* JADX WARN: Failed to calculate best type for var: r3v182 ??
    jadx.core.utils.exceptions.JadxRuntimeException: Type update failed for variable: r3v182 ??, new type: float
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
    /* JADX WARN: Failed to calculate best type for var: r3v183 ??
    jadx.core.utils.exceptions.JadxRuntimeException: Type update failed for variable: r3v183 ??, new type: float
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
    /* JADX WARN: Failed to calculate best type for var: r3v184 ??
    jadx.core.utils.exceptions.JadxRuntimeException: Type update failed for variable: r3v184 ??, new type: float
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
    /* JADX WARN: Failed to calculate best type for var: r3v188 ??
    jadx.core.utils.exceptions.JadxRuntimeException: Type update failed for variable: r3v188 ??, new type: float
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
    /* JADX WARN: Failed to calculate best type for var: r3v189 ??
    jadx.core.utils.exceptions.JadxRuntimeException: Type update failed for variable: r3v189 ??, new type: float
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
    /* JADX WARN: Failed to calculate best type for var: r3v193 ??
    jadx.core.utils.exceptions.JadxRuntimeException: Type update failed for variable: r3v193 ??, new type: float
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
    /* JADX WARN: Failed to calculate best type for var: r3v194 ??
    jadx.core.utils.exceptions.JadxRuntimeException: Type update failed for variable: r3v194 ??, new type: float
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
    /* JADX WARN: Failed to calculate best type for var: r3v195 ??
    jadx.core.utils.exceptions.JadxRuntimeException: Type update failed for variable: r3v195 ??, new type: android.text.StaticLayout
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
    /* JADX WARN: Failed to calculate best type for var: r3v211 ??
    jadx.core.utils.exceptions.JadxRuntimeException: Type update failed for variable: r3v211 ??, new type: float
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
    /* JADX WARN: Failed to calculate best type for var: r3v219 ??
    jadx.core.utils.exceptions.JadxRuntimeException: Type update failed for variable: r3v219 ??, new type: org.telegram.ui.Components.spoilers.SpoilerEffect
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
    /* JADX WARN: Failed to calculate best type for var: r3v223 ??
    jadx.core.utils.exceptions.JadxRuntimeException: Type update failed for variable: r3v223 ??, new type: org.telegram.ui.Components.spoilers.SpoilerEffect
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
    /* JADX WARN: Failed to calculate best type for var: r3v48 ??
    jadx.core.utils.exceptions.JadxRuntimeException: Type update failed for variable: r3v48 ??, new type: float
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
    /* JADX WARN: Failed to calculate best type for var: r3v49 ??
    jadx.core.utils.exceptions.JadxRuntimeException: Type update failed for variable: r3v49 ??, new type: float
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
    /* JADX WARN: Failed to calculate best type for var: r3v60 ??
    jadx.core.utils.exceptions.JadxRuntimeException: Type update failed for variable: r3v60 ??, new type: float
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
    /* JADX WARN: Failed to calculate best type for var: r3v61 ??
    jadx.core.utils.exceptions.JadxRuntimeException: Type update failed for variable: r3v61 ??, new type: float
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
    /* JADX WARN: Failed to calculate best type for var: r3v62 ??
    jadx.core.utils.exceptions.JadxRuntimeException: Type update failed for variable: r3v62 ??, new type: float
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
    /* JADX WARN: Failed to calculate best type for var: r40v0 ??
    jadx.core.utils.exceptions.JadxRuntimeException: Type update failed for variable: r40v0 ??, new type: android.graphics.Canvas
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
    /* JADX WARN: Failed to calculate best type for var: r4v100 ??
    jadx.core.utils.exceptions.JadxRuntimeException: Type update failed for variable: r4v100 ??, new type: float
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
    /* JADX WARN: Failed to calculate best type for var: r4v112 ??
    jadx.core.utils.exceptions.JadxRuntimeException: Type update failed for variable: r4v112 ??, new type: float
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
    /* JADX WARN: Failed to calculate best type for var: r4v115 ??
    jadx.core.utils.exceptions.JadxRuntimeException: Type update failed for variable: r4v115 ??, new type: float
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
    /* JADX WARN: Failed to calculate best type for var: r4v116 ??
    jadx.core.utils.exceptions.JadxRuntimeException: Type update failed for variable: r4v116 ??, new type: float
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
    /* JADX WARN: Failed to calculate best type for var: r4v117 ??
    jadx.core.utils.exceptions.JadxRuntimeException: Type update failed for variable: r4v117 ??, new type: float
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
    /* JADX WARN: Failed to calculate best type for var: r4v118 ??
    jadx.core.utils.exceptions.JadxRuntimeException: Type update failed for variable: r4v118 ??, new type: float
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
    /* JADX WARN: Failed to calculate best type for var: r4v133 ??
    jadx.core.utils.exceptions.JadxRuntimeException: Type update failed for variable: r4v133 ??, new type: float
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
    /* JADX WARN: Failed to calculate best type for var: r4v134 ??
    jadx.core.utils.exceptions.JadxRuntimeException: Type update failed for variable: r4v134 ??, new type: float
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
    /* JADX WARN: Failed to calculate best type for var: r4v135 ??
    jadx.core.utils.exceptions.JadxRuntimeException: Type update failed for variable: r4v135 ??, new type: android.text.StaticLayout
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
    /* JADX WARN: Failed to calculate best type for var: r4v16 ??
    jadx.core.utils.exceptions.JadxRuntimeException: Type update failed for variable: r4v16 ??, new type: org.telegram.messenger.ImageReceiver
    	at jadx.core.dex.visitors.typeinference.TypeUpdate.apply(TypeUpdate.java:109)
    	at jadx.core.dex.visitors.typeinference.TypeUpdate.apply(TypeUpdate.java:59)
    	at jadx.core.dex.visitors.typeinference.FixTypesVisitor.calculateFromBounds(FixTypesVisitor.java:159)
    	at jadx.core.dex.visitors.typeinference.FixTypesVisitor.setBestType(FixTypesVisitor.java:136)
    	at jadx.core.dex.visitors.typeinference.FixTypesVisitor.deduceType(FixTypesVisitor.java:241)
    	at jadx.core.dex.visitors.typeinference.FixTypesVisitor.tryDeduceTypes(FixTypesVisitor.java:224)
    	at jadx.core.dex.visitors.typeinference.FixTypesVisitor.visit(FixTypesVisitor.java:94)
    Caused by: java.lang.NullPointerException
     */
    /* JADX WARN: Failed to calculate best type for var: r4v16 ??
    jadx.core.utils.exceptions.JadxRuntimeException: Type update failed for variable: r4v16 ??, new type: org.telegram.messenger.ImageReceiver
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
    /* JADX WARN: Failed to calculate best type for var: r4v17 ??
    jadx.core.utils.exceptions.JadxRuntimeException: Type update failed for variable: r4v17 ??, new type: float
    	at jadx.core.dex.visitors.typeinference.TypeUpdate.apply(TypeUpdate.java:109)
    	at jadx.core.dex.visitors.typeinference.TypeUpdate.apply(TypeUpdate.java:59)
    	at jadx.core.dex.visitors.typeinference.FixTypesVisitor.calculateFromBounds(FixTypesVisitor.java:159)
    	at jadx.core.dex.visitors.typeinference.FixTypesVisitor.setBestType(FixTypesVisitor.java:136)
    	at jadx.core.dex.visitors.typeinference.FixTypesVisitor.deduceType(FixTypesVisitor.java:241)
    	at jadx.core.dex.visitors.typeinference.FixTypesVisitor.tryDeduceTypes(FixTypesVisitor.java:224)
    	at jadx.core.dex.visitors.typeinference.FixTypesVisitor.visit(FixTypesVisitor.java:94)
    Caused by: java.lang.NullPointerException
     */
    /* JADX WARN: Failed to calculate best type for var: r4v17 ??
    jadx.core.utils.exceptions.JadxRuntimeException: Type update failed for variable: r4v17 ??, new type: float
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
    /* JADX WARN: Failed to calculate best type for var: r4v18 ??
    jadx.core.utils.exceptions.JadxRuntimeException: Type update failed for variable: r4v18 ??, new type: float
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
    /* JADX WARN: Failed to calculate best type for var: r4v33 ??
    jadx.core.utils.exceptions.JadxRuntimeException: Type update failed for variable: r4v33 ??, new type: float
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
    /* JADX WARN: Failed to calculate best type for var: r4v34 ??
    jadx.core.utils.exceptions.JadxRuntimeException: Type update failed for variable: r4v34 ??, new type: float
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
    /* JADX WARN: Failed to calculate best type for var: r4v36 ??
    jadx.core.utils.exceptions.JadxRuntimeException: Type update failed for variable: r4v36 ??, new type: float
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
    /* JADX WARN: Failed to calculate best type for var: r4v99 ??
    jadx.core.utils.exceptions.JadxRuntimeException: Type update failed for variable: r4v99 ??, new type: float
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
    /* JADX WARN: Failed to calculate best type for var: r5v17 ??
    jadx.core.utils.exceptions.JadxRuntimeException: Type update failed for variable: r5v17 ??, new type: org.telegram.messenger.ImageReceiver
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
    /* JADX WARN: Failed to calculate best type for var: r5v18 ??
    jadx.core.utils.exceptions.JadxRuntimeException: Type update failed for variable: r5v18 ??, new type: float
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
    /* JADX WARN: Failed to calculate best type for var: r5v19 ??
    jadx.core.utils.exceptions.JadxRuntimeException: Type update failed for variable: r5v19 ??, new type: float
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
    /* JADX WARN: Failed to calculate best type for var: r5v79 ??
    jadx.core.utils.exceptions.JadxRuntimeException: Type update failed for variable: r5v79 ??, new type: android.graphics.RectF
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
    /* JADX WARN: Failed to calculate best type for var: r7v1 ??
    jadx.core.utils.exceptions.JadxRuntimeException: Type update failed for variable: r7v1 ??, new type: float
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
    /* JADX WARN: Failed to calculate best type for var: r7v2 ??
    jadx.core.utils.exceptions.JadxRuntimeException: Type update failed for variable: r7v2 ??, new type: float
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
    /* JADX WARN: Failed to calculate best type for var: r7v23 ??
    jadx.core.utils.exceptions.JadxRuntimeException: Type update failed for variable: r7v23 ??, new type: float
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
    /* JADX WARN: Failed to set immutable type for var: r39v0 'this'  ??
    jadx.core.utils.exceptions.JadxRuntimeException: Type update failed for variable: r39v0 'this'  ??, new type: org.telegram.ui.Cells.ChatActionCell
    	at jadx.core.dex.visitors.typeinference.TypeUpdate.apply(TypeUpdate.java:109)
    	at jadx.core.dex.visitors.typeinference.TypeUpdate.applyWithWiderIgnSame(TypeUpdate.java:73)
    	at jadx.core.dex.visitors.typeinference.TypeInferenceVisitor.setImmutableType(TypeInferenceVisitor.java:111)
    	at jadx.core.dex.visitors.typeinference.TypeInferenceVisitor.lambda$runTypePropagation$1(TypeInferenceVisitor.java:102)
    	at java.base/java.util.ArrayList.forEach(ArrayList.java:1596)
    	at jadx.core.dex.visitors.typeinference.TypeInferenceVisitor.runTypePropagation(TypeInferenceVisitor.java:102)
    	at jadx.core.dex.visitors.typeinference.TypeInferenceVisitor.visit(TypeInferenceVisitor.java:75)
    Caused by: java.lang.NullPointerException
     */
    /* JADX WARN: Failed to set immutable type for var: r40v0 ??
    jadx.core.utils.exceptions.JadxRuntimeException: Type update failed for variable: r40v0 ??, new type: android.graphics.Canvas
    	at jadx.core.dex.visitors.typeinference.TypeUpdate.apply(TypeUpdate.java:109)
    	at jadx.core.dex.visitors.typeinference.TypeUpdate.applyWithWiderIgnSame(TypeUpdate.java:73)
    	at jadx.core.dex.visitors.typeinference.TypeInferenceVisitor.setImmutableType(TypeInferenceVisitor.java:111)
    	at jadx.core.dex.visitors.typeinference.TypeInferenceVisitor.lambda$runTypePropagation$1(TypeInferenceVisitor.java:102)
    	at java.base/java.util.ArrayList.forEach(ArrayList.java:1596)
    	at jadx.core.dex.visitors.typeinference.TypeInferenceVisitor.runTypePropagation(TypeInferenceVisitor.java:102)
    	at jadx.core.dex.visitors.typeinference.TypeInferenceVisitor.visit(TypeInferenceVisitor.java:75)
    Caused by: java.lang.NullPointerException
     */
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
    /*  JADX ERROR: Types fix failed
        jadx.core.utils.exceptions.JadxRuntimeException: Type update failed for variable: r4v17 ??, new type: float
        	at jadx.core.dex.visitors.typeinference.TypeUpdate.apply(TypeUpdate.java:109)
        	at jadx.core.dex.visitors.typeinference.TypeUpdate.apply(TypeUpdate.java:59)
        	at jadx.core.dex.visitors.typeinference.FixTypesVisitor.tryPossibleTypes(FixTypesVisitor.java:186)
        	at jadx.core.dex.visitors.typeinference.FixTypesVisitor.deduceType(FixTypesVisitor.java:245)
        	at jadx.core.dex.visitors.typeinference.FixTypesVisitor.tryDeduceTypes(FixTypesVisitor.java:224)
        	at jadx.core.dex.visitors.typeinference.FixTypesVisitor.visit(FixTypesVisitor.java:94)
        Caused by: java.lang.NullPointerException
        */
    @Override // android.view.View
    protected void onDraw(android.graphics.Canvas r40) {
        /*
            Method dump skipped, instruction units count: 4093
            To view this dump add '--comments-level debug' option
        */
        throw new UnsupportedOperationException("Method not decompiled: org.telegram.ui.Cells.ChatActionCell.onDraw(android.graphics.Canvas):void");
    }

    @Override // android.view.ViewGroup
    protected boolean drawChild(Canvas canvas, View view, long j) {
        if (view == this.rippleView) {
            float scale = this.bounce.getScale(0.02f);
            canvas.save();
            canvas.scale(scale, scale, view.getX() + (view.getMeasuredWidth() / 2.0f), view.getY() + (view.getMeasuredHeight() / 2.0f));
            boolean zDrawChild = super.drawChild(canvas, view, j);
            canvas.restore();
            return zDrawChild;
        }
        return super.drawChild(canvas, view, j);
    }

    private void checkLeftRightBounds() {
        this.backgroundLeft = (int) Math.min(this.backgroundLeft, this.rect.left);
        this.backgroundRight = (int) Math.max(this.backgroundRight, this.rect.right);
    }

    public void drawBackground(Canvas canvas, boolean z) {
        Paint paint;
        float f;
        float f2;
        float f3;
        float f4;
        Paint paint2;
        int alpha;
        int alpha2;
        Canvas canvas2;
        float f5;
        int i;
        TextLayout textLayout;
        float f6;
        float f7;
        int iDp;
        float f8;
        int i2;
        if (this.canDrawInParent) {
            if (hasGradientService() && !z) {
                return;
            }
            if (!hasGradientService() && z) {
                return;
            }
        }
        Paint themedPaint = getThemedPaint("paintChatActionBackground");
        Paint themedPaint2 = getThemedPaint("paintChatActionBackgroundDarken");
        this.textPaint = (TextPaint) getThemedPaint("paintChatActionText");
        int i3 = this.overrideBackground;
        boolean z2 = true;
        if (i3 >= 0) {
            int themedColor = getThemedColor(i3);
            if (this.overrideBackgroundPaint == null) {
                Paint paint3 = new Paint(1);
                this.overrideBackgroundPaint = paint3;
                paint3.setColor(themedColor);
                TextPaint textPaint = new TextPaint(1);
                this.overrideTextPaint = textPaint;
                textPaint.setTypeface(AndroidUtilities.bold());
                this.overrideTextPaint.setTextSize(AndroidUtilities.dp(Math.max(16, SharedConfig.fontSize) - 2));
                this.overrideTextPaint.setColor(getThemedColor(this.overrideText));
            }
            themedPaint = this.overrideBackgroundPaint;
            this.textPaint = this.overrideTextPaint;
        }
        float f9 = 8.0f;
        if (this.invalidatePath) {
            this.invalidatePath = false;
            this.backgroundLeft = getWidth();
            this.backgroundRight = 0;
            this.lineWidths.clear();
            StaticLayout staticLayout = this.textLayout;
            int lineCount = staticLayout == null ? 0 : staticLayout.getLineCount();
            int iDp2 = AndroidUtilities.dp(11.0f);
            int iDp3 = AndroidUtilities.dp(8.0f);
            int i4 = 0;
            int i5 = 0;
            while (i4 < lineCount) {
                boolean z3 = z2;
                int iCeil = (int) Math.ceil(this.textLayout.getLineWidth(i4));
                if (i4 == 0 || (i2 = i5 - iCeil) <= 0) {
                    f8 = f9;
                } else {
                    f8 = f9;
                    if (i2 <= (iDp2 * 1.5f) + iDp3) {
                    }
                    this.lineWidths.add(Integer.valueOf(i5));
                    i4++;
                    z2 = z3;
                    f9 = f8;
                }
                i5 = iCeil;
                this.lineWidths.add(Integer.valueOf(i5));
                i4++;
                z2 = z3;
                f9 = f8;
            }
            f = f9;
            f3 = 6.0f;
            for (int i6 = lineCount - 2; i6 >= 0; i6--) {
                int iIntValue = ((Integer) this.lineWidths.get(i6)).intValue();
                int i7 = i5 - iIntValue;
                if (i7 <= 0 || i7 > (iDp2 * 1.5f) + iDp3) {
                    i5 = iIntValue;
                }
                this.lineWidths.set(i6, Integer.valueOf(i5));
            }
            int iDp4 = AndroidUtilities.dp(4.0f);
            int measuredWidth = getMeasuredWidth() / 2;
            int iDp5 = AndroidUtilities.dp(3.0f);
            int iDp6 = AndroidUtilities.dp(6.0f);
            int i8 = iDp2 - iDp5;
            f4 = 2.0f;
            this.lineHeights.clear();
            this.backgroundPath.reset();
            f2 = 4.0f;
            float f10 = measuredWidth;
            this.backgroundPath.moveTo(f10, iDp4);
            int i9 = 0;
            int i10 = 0;
            while (i9 < lineCount) {
                int iIntValue2 = ((Integer) this.lineWidths.get(i9)).intValue();
                int i11 = i10;
                int lineBottom = this.textLayout.getLineBottom(i9);
                int i12 = lineCount - 1;
                float f11 = f10;
                int iIntValue3 = i9 < i12 ? ((Integer) this.lineWidths.get(i9 + 1)).intValue() : 0;
                int iDp7 = lineBottom - i11;
                if (i9 == 0 || iIntValue2 > i5) {
                    iDp7 += AndroidUtilities.dp(3.0f);
                }
                if (i9 == i12 || iIntValue2 > iIntValue3) {
                    iDp7 += AndroidUtilities.dp(3.0f);
                }
                int i13 = iDp7;
                float f12 = f11 + (iIntValue2 / 2.0f);
                int i14 = (i9 == i12 || iIntValue2 >= iIntValue3 || i9 == 0 || iIntValue2 >= i5) ? iDp3 : iDp6;
                if (i9 == 0 || iIntValue2 > i5) {
                    f7 = f12;
                    this.rect.set((f7 - iDp5) - iDp2, iDp4, f7 + i8, (iDp2 * 2) + iDp4);
                    checkLeftRightBounds();
                    this.backgroundPath.arcTo(this.rect, -90.0f, 90.0f);
                } else if (iIntValue2 < i5) {
                    f7 = f12;
                    float f13 = f7 + i8;
                    int i15 = i14 * 2;
                    this.rect.set(f13, iDp4, i15 + f13, i15 + iDp4);
                    checkLeftRightBounds();
                    this.backgroundPath.arcTo(this.rect, -90.0f, -90.0f);
                } else {
                    f7 = f12;
                }
                iDp4 += i13;
                if (i9 == i12 || iIntValue2 >= iIntValue3) {
                    iDp = i13;
                } else {
                    iDp4 -= AndroidUtilities.dp(3.0f);
                    iDp = i13 - AndroidUtilities.dp(3.0f);
                }
                if (i9 != 0 && iIntValue2 < i5) {
                    iDp4 -= AndroidUtilities.dp(3.0f);
                    iDp -= AndroidUtilities.dp(3.0f);
                }
                this.lineHeights.add(Integer.valueOf(iDp));
                if (i9 == i12 || iIntValue2 > iIntValue3) {
                    this.rect.set((f7 - iDp5) - iDp2, iDp4 - (iDp2 * 2), f7 + i8, iDp4);
                    checkLeftRightBounds();
                    this.backgroundPath.arcTo(this.rect, 0.0f, 90.0f);
                } else if (iIntValue2 < iIntValue3) {
                    float f14 = f7 + i8;
                    int i16 = i14 * 2;
                    this.rect.set(f14, iDp4 - i16, i16 + f14, iDp4);
                    checkLeftRightBounds();
                    this.backgroundPath.arcTo(this.rect, 180.0f, -90.0f);
                }
                i9++;
                i5 = iIntValue2;
                i10 = lineBottom;
                f10 = f11;
                lineCount = lineCount;
                iDp3 = iDp3;
                iDp6 = iDp6;
                themedPaint2 = themedPaint2;
            }
            paint = themedPaint2;
            float f15 = f10;
            int i17 = iDp3;
            int i18 = iDp6;
            int i19 = lineCount - 1;
            int i20 = i19;
            while (i20 >= 0) {
                int iIntValue4 = i20 != 0 ? ((Integer) this.lineWidths.get(i20 - 1)).intValue() : 0;
                int iIntValue5 = ((Integer) this.lineWidths.get(i20)).intValue();
                int iIntValue6 = i20 != i19 ? ((Integer) this.lineWidths.get(i20 + 1)).intValue() : 0;
                this.textLayout.getLineBottom(i20);
                float f16 = measuredWidth - (iIntValue5 / 2);
                int i21 = (i20 == i19 || iIntValue5 >= iIntValue6 || i20 == 0 || iIntValue5 >= iIntValue4) ? i17 : i18;
                if (i20 == i19 || iIntValue5 > iIntValue6) {
                    f6 = f16;
                    this.rect.set(f6 - i8, iDp4 - (iDp2 * 2), f6 + iDp5 + iDp2, iDp4);
                    checkLeftRightBounds();
                    this.backgroundPath.arcTo(this.rect, 90.0f, 90.0f);
                } else if (iIntValue5 < iIntValue6) {
                    float f17 = f16 - i8;
                    int i22 = i21 * 2;
                    f6 = f16;
                    this.rect.set(f17 - i22, iDp4 - i22, f17, iDp4);
                    checkLeftRightBounds();
                    this.backgroundPath.arcTo(this.rect, 90.0f, -90.0f);
                } else {
                    f6 = f16;
                }
                iDp4 -= ((Integer) this.lineHeights.get(i20)).intValue();
                if (i20 == 0 || iIntValue5 > iIntValue4) {
                    this.rect.set(f6 - i8, iDp4, f6 + iDp5 + iDp2, (iDp2 * 2) + iDp4);
                    checkLeftRightBounds();
                    this.backgroundPath.arcTo(this.rect, 180.0f, 90.0f);
                } else if (iIntValue5 < iIntValue4) {
                    float f18 = f6 - i8;
                    int i23 = i21 * 2;
                    this.rect.set(f18 - i23, iDp4, f18, i23 + iDp4);
                    checkLeftRightBounds();
                    this.backgroundPath.arcTo(this.rect, 0.0f, -90.0f);
                }
                i20--;
            }
            this.backgroundPath.close();
            if (isMessageActionSuggestedPostApproval() && !isNewStyleButtonLayout()) {
                this.rect.left = (f15 - (this.textWidth / 2.0f)) - AndroidUtilities.dp(17.0f);
                RectF rectF = this.rect;
                rectF.top = iDp4;
                rectF.right = f15 + (this.textWidth / 2.0f) + AndroidUtilities.dp(17.0f);
                this.rect.bottom = iDp4 + this.textHeight + this.titleHeight + AndroidUtilities.dp(28.0f);
                this.backgroundPath.reset();
                this.backgroundPath.addRoundRect(this.rect, AndroidUtilities.dp(15.0f), AndroidUtilities.dp(15.0f), Path.Direction.CW);
                this.backgroundPath.close();
            }
        } else {
            paint = themedPaint2;
            f = 8.0f;
            f2 = 4.0f;
            f3 = 6.0f;
            f4 = 2.0f;
        }
        if (!this.visiblePartSet) {
            this.backgroundHeight = ((ViewGroup) getParent()).getMeasuredHeight();
        }
        Theme.ResourcesProvider resourcesProvider = this.themeDelegate;
        if (resourcesProvider != null) {
            resourcesProvider.applyServiceShaderMatrix(getMeasuredWidth(), this.backgroundHeight, this.viewTranslationX, this.viewTop + AndroidUtilities.dp(f2));
        } else {
            Theme.applyServiceShaderMatrix(getMeasuredWidth(), this.backgroundHeight, this.viewTranslationX, this.viewTop + AndroidUtilities.dp(f2));
        }
        if (z && (getAlpha() != 1.0f || isFloating())) {
            alpha = themedPaint.getAlpha();
            alpha2 = paint.getAlpha();
            themedPaint.setAlpha((int) (alpha * getAlpha() * (isFloating() ? 0.75f : 1.0f)));
            paint2 = paint;
            paint2.setAlpha((int) (alpha2 * getAlpha() * (isFloating() ? 0.75f : 1.0f)));
        } else {
            paint2 = paint;
            if (isFloating()) {
                alpha = themedPaint.getAlpha();
                alpha2 = paint2.getAlpha();
                themedPaint.setAlpha((int) (alpha * (isFloating() ? 0.75f : 1.0f)));
                paint2.setAlpha((int) (alpha2 * (isFloating() ? 0.75f : 1.0f)));
            } else {
                alpha = -1;
                alpha2 = -1;
            }
        }
        MessageObject messageObject = this.currentMessageObject;
        if (messageObject == null || !messageObject.isRepostPreview) {
            canvas2 = canvas;
            canvas2.drawPath(this.backgroundPath, themedPaint);
            if (hasGradientService()) {
                canvas2.drawPath(this.backgroundPath, paint2);
            }
            f5 = 0.0f;
            if (this.dimAmount > 0.0f) {
                int alpha3 = this.dimPaint.getAlpha();
                if (z) {
                    this.dimPaint.setAlpha((int) (alpha3 * getAlpha()));
                }
                canvas2.drawPath(this.backgroundPath, this.dimPaint);
                this.dimPaint.setAlpha(alpha3);
            }
        } else {
            canvas2 = canvas;
            f5 = 0.0f;
        }
        MessageObject messageObject2 = this.currentMessageObject;
        if (this.starGiftLayout.has()) {
            float width = this.starGiftLayout.getWidth() + AndroidUtilities.dp(f);
            float width2 = (getWidth() - width) / f4;
            float fDp = this.starGiftLayout.repost ? f5 : this.textY + this.textHeight + AndroidUtilities.dp(12.0f);
            RectF rectF2 = AndroidUtilities.rectTmp;
            rectF2.set(width2, fDp, width + width2, this.starGiftLayout.getHeight() + fDp + AndroidUtilities.dp(f));
            if (this.backgroundRect == null) {
                this.backgroundRect = new RectF();
            }
            this.backgroundRect.set(rectF2);
            canvas2.drawRoundRect(this.backgroundRect, AndroidUtilities.dp(16.0f), AndroidUtilities.dp(16.0f), themedPaint);
            if (hasGradientService()) {
                canvas2.drawRoundRect(this.backgroundRect, AndroidUtilities.dp(16.0f), AndroidUtilities.dp(16.0f), paint2);
            }
        } else {
            SuggestBirthdayActionLayout suggestBirthdayActionLayout = this.birthdayLayout;
            if (suggestBirthdayActionLayout != null) {
                float fWidth = suggestBirthdayActionLayout.width();
                float fHeight = this.birthdayLayout.height();
                float width3 = (getWidth() - fWidth) / f4;
                if (this.backgroundRect == null) {
                    this.backgroundRect = new RectF();
                }
                this.backgroundRect.set(width3, AndroidUtilities.dp(f2), fWidth + width3, AndroidUtilities.dp(f2) + fHeight);
                canvas2.drawRoundRect(this.backgroundRect, AndroidUtilities.dp(16.0f), AndroidUtilities.dp(16.0f), themedPaint);
                if (hasGradientService()) {
                    canvas2.drawRoundRect(this.backgroundRect, AndroidUtilities.dp(16.0f), AndroidUtilities.dp(16.0f), paint2);
                }
            } else if (isButtonLayout(messageObject2)) {
                float width4 = (getWidth() - this.giftRectSize) / f4;
                float f19 = this.textY + this.textHeight;
                if (isNewStyleButtonLayout()) {
                    float fDp2 = f19 + AndroidUtilities.dp(f2);
                    AndroidUtilities.rectTmp.set(width4, fDp2, this.giftRectSize + width4, this.backgroundRectHeight + fDp2);
                } else {
                    float fDp3 = f19 + AndroidUtilities.dp(12.0f);
                    RectF rectF3 = AndroidUtilities.rectTmp;
                    int i24 = this.giftRectSize;
                    rectF3.set(width4, fDp3, i24 + width4, i24 + fDp3 + this.giftPremiumAdditionalHeight);
                }
                if (messageObject2 != null && messageObject2.type == 18 && !this.giftPremiumTextCollapsed && (textLayout = this.giftPremiumText) != null && this.giftPremiumTextCollapsedHeight > 0) {
                    AndroidUtilities.rectTmp.bottom -= (textLayout.layout.getHeight() - this.giftPremiumTextCollapsedHeight) * (1.0f - this.giftPremiumTextExpandedAnimated.get());
                }
                if (this.backgroundRect == null) {
                    this.backgroundRect = new RectF();
                }
                this.backgroundRect.set(AndroidUtilities.rectTmp);
                if (messageObject2 != null && (((i = messageObject2.type) == 33 || i == 35) && this.botInlineButtons != null)) {
                    Arrays.fill(this.radii, AndroidUtilities.dp(16.0f));
                    float[] fArr = this.radii;
                    float fDp4 = AndroidUtilities.dp(f3);
                    fArr[7] = fDp4;
                    fArr[6] = fDp4;
                    fArr[5] = fDp4;
                    fArr[4] = fDp4;
                    this.backgroundPath2.rewind();
                    this.backgroundPath2.addRoundRect(this.backgroundRect, this.radii, Path.Direction.CW);
                    canvas2.drawPath(this.backgroundPath2, themedPaint);
                    if (hasGradientService()) {
                        canvas2.drawPath(this.backgroundPath2, paint2);
                    }
                } else {
                    canvas2.drawRoundRect(this.backgroundRect, AndroidUtilities.dp(16.0f), AndroidUtilities.dp(16.0f), themedPaint);
                    if (hasGradientService()) {
                        canvas2.drawRoundRect(this.backgroundRect, AndroidUtilities.dp(16.0f), AndroidUtilities.dp(16.0f), paint2);
                    }
                }
            }
        }
        if (alpha >= 0) {
            themedPaint.setAlpha(alpha);
            paint2.setAlpha(alpha2);
        }
    }

    private void drawBotButtons(Canvas canvas, ArrayList arrayList) {
        if (arrayList == null || arrayList.isEmpty()) {
            return;
        }
        Theme.ResourcesProvider resourcesProvider = this.themeDelegate;
        float f = 4.0f;
        if (resourcesProvider != null) {
            resourcesProvider.applyServiceShaderMatrix(getMeasuredWidth(), this.backgroundHeight, this.viewTranslationX, this.viewTop + AndroidUtilities.dp(4.0f));
        } else {
            Theme.applyServiceShaderMatrix(getMeasuredWidth(), this.backgroundHeight, this.viewTranslationX, this.viewTop + AndroidUtilities.dp(4.0f));
        }
        float f2 = 2.0f;
        float width = (getWidth() - this.giftRectSize) / 2.0f;
        float fDp = this.textY + this.textHeight + AndroidUtilities.dp(4.0f) + this.backgroundRectHeight + AndroidUtilities.dp(4.0f);
        float fDp2 = (this.giftRectSize - AndroidUtilities.dp(4.0f)) / 2.0f;
        int i = 0;
        while (i < arrayList.size()) {
            BotButton botButton = (BotButton) arrayList.get(i);
            float pressScale = botButton.getPressScale();
            float fDp3 = ((AndroidUtilities.dp(f) + fDp2) * i) + width;
            float f3 = fDp3 + fDp2;
            this.rect.set(fDp3, fDp, f3, botButton.height + fDp);
            canvas.save();
            if (pressScale != 1.0f) {
                canvas.scale(pressScale, pressScale, this.rect.centerX(), this.rect.centerY());
            }
            Arrays.fill(this.botButtonRadii, AndroidUtilities.dp(Math.min(6.75f, SharedConfig.bubbleRadius)));
            if (botButton.hasPositionFlag(9)) {
                float[] fArr = this.botButtonRadii;
                float fDp4 = AndroidUtilities.dp(SharedConfig.bubbleRadius);
                fArr[7] = fDp4;
                fArr[6] = fDp4;
            }
            if (botButton.hasPositionFlag(10)) {
                float[] fArr2 = this.botButtonRadii;
                float fDp5 = AndroidUtilities.dp(SharedConfig.bubbleRadius);
                fArr2[5] = fDp5;
                fArr2[4] = fDp5;
            }
            this.botButtonPath.rewind();
            float f4 = f2;
            this.botButtonPath.addRoundRect(this.rect, this.botButtonRadii, Path.Direction.CW);
            canvas.drawPath(this.botButtonPath, getThemedPaint("paintChatActionBackground"));
            if (hasGradientService()) {
                canvas.drawPath(this.botButtonPath, Theme.chat_actionBackgroundGradientDarkenPaint);
            }
            canvas.save();
            canvas.clipPath(this.botButtonPath);
            Drawable drawable = botButton.selectorDrawable;
            if (drawable != null) {
                int i2 = (int) fDp;
                drawable.setBounds((int) fDp3, i2, (int) f3, botButton.height + i2);
                botButton.selectorDrawable.setAlpha(255);
                botButton.selectorDrawable.draw(canvas);
            }
            canvas.restore();
            canvas.save();
            int iDp = botButton.iconDrawable != null ? AndroidUtilities.dp(26.0f) : 0;
            float f5 = iDp;
            float width2 = fDp3 + (((fDp2 - (botButton.title.getWidth() + (botButton.iconDrawable != null ? AndroidUtilities.dp(f) : 0))) - f5) / f4);
            Drawable drawable2 = botButton.iconDrawable;
            if (drawable2 != null) {
                int i3 = (int) width2;
                drawable2.setBounds(i3, (int) (((botButton.height - AndroidUtilities.dp(24.0f)) / f4) + fDp), i3 + AndroidUtilities.dp(24.0f), ((int) (((botButton.height - AndroidUtilities.dp(24.0f)) / f4) + fDp)) + AndroidUtilities.dp(24.0f));
                botButton.iconDrawable.setAlpha(botButton.isLocked ? 128 : 255);
                botButton.iconDrawable.draw(canvas);
                width2 += f5;
            }
            botButton.title.ellipsize(Math.max(1, (((int) fDp2) - AndroidUtilities.dp(15.0f)) - iDp));
            botButton.title.draw(canvas, width2, (AndroidUtilities.dp(40.0f) / f4) + fDp, botButton.isLocked ? 0.5f : 1.0f);
            canvas.restore();
            canvas.restore();
            i++;
            f = f;
            f2 = f4;
        }
    }

    private boolean checkBotButtonMotionEvent(MotionEvent motionEvent) {
        int i;
        BotInlineKeyboard.ButtonCustom buttonCustom;
        if (this.botButtons.isEmpty()) {
            return false;
        }
        int x = (int) motionEvent.getX();
        int y = (int) motionEvent.getY();
        float width = (getWidth() - this.giftRectSize) / 2.0f;
        float fDp = this.textY + this.textHeight + AndroidUtilities.dp(4.0f) + this.backgroundRectHeight + AndroidUtilities.dp(4.0f);
        float fDp2 = (this.giftRectSize - AndroidUtilities.dp(4.0f)) / 2.0f;
        if (motionEvent.getAction() == 0) {
            this.pressedBotButton = -1;
            for (int i2 = 0; i2 < this.botButtons.size(); i2++) {
                BotButton botButton = (BotButton) this.botButtons.get(i2);
                float fDp3 = ((AndroidUtilities.dp(4.0f) + fDp2) * i2) + width;
                RectF rectF = AndroidUtilities.rectTmp;
                rectF.set(fDp3, fDp, fDp3 + fDp2, botButton.height + fDp);
                float f = x;
                float f2 = y;
                if (rectF.contains(f, f2)) {
                    this.pressedBotButton = i2;
                    invalidateOutbounds();
                    if (botButton.selectorDrawable == null) {
                        Drawable drawableCreateRadSelectorDrawable = Theme.createRadSelectorDrawable(getThemedColor(Theme.key_chat_serviceBackgroundSelector), 6, 6);
                        botButton.selectorDrawable = drawableCreateRadSelectorDrawable;
                        drawableCreateRadSelectorDrawable.setCallback(this);
                    }
                    botButton.selectorDrawable.setHotspot(f, f2);
                    botButton.selectorDrawable.setState(this.pressedState);
                    botButton.setPressed(!botButton.isLocked);
                    return true;
                }
            }
        } else if (motionEvent.getAction() == 1) {
            if (this.pressedBotButton != -1) {
                playSoundEffect(0);
                BotButton botButton2 = (BotButton) this.botButtons.get(this.pressedBotButton);
                Drawable drawable = botButton2.selectorDrawable;
                if (drawable != null) {
                    drawable.setState(StateSet.NOTHING);
                }
                botButton2.setPressed(false);
                if (this.delegate != null && !botButton2.isLocked && (buttonCustom = botButton2.buttonCustom) != null) {
                    didPressCustomBotButton(buttonCustom);
                }
                this.pressedBotButton = -1;
                invalidateOutbounds();
                return false;
            }
        } else if (motionEvent.getAction() == 3 && (i = this.pressedBotButton) != -1) {
            BotButton botButton3 = (BotButton) this.botButtons.get(i);
            Drawable drawable2 = botButton3.selectorDrawable;
            if (drawable2 != null) {
                drawable2.setState(StateSet.NOTHING);
            }
            botButton3.setPressed(false);
            this.pressedBotButton = -1;
            invalidateOutbounds();
        }
        return false;
    }

    private void didPressCustomBotButton(BotInlineKeyboard.ButtonCustom buttonCustom) {
        MessageObject messageObject;
        TLRPC.Message message;
        MessageObject messageObject2;
        TLRPC.Message message2;
        TLRPC.Message message3;
        if (getMessageObject() == null) {
            return;
        }
        int i = buttonCustom.id;
        if (i == 5) {
            ChatActionCellDelegate chatActionCellDelegate = this.delegate;
            final BaseFragment baseFragment = chatActionCellDelegate != null ? chatActionCellDelegate.getBaseFragment() : null;
            if (baseFragment == null || this.currentMessageObject == null) {
                return;
            }
            AlertsCreator.showSimpleConfirmAlert(baseFragment, LocaleController.getString(org.telegram.messenger.R.string.GiftOfferRejectConfirmTitle), AndroidUtilities.replaceTags(LocaleController.formatString(org.telegram.messenger.R.string.GiftOfferRejectConfirmText, DialogObject.getShortName(this.currentMessageObject.getDialogId()))), LocaleController.getString(org.telegram.messenger.R.string.GiftOfferRejectConfirmConfirm), true, new Runnable() { // from class: org.telegram.ui.Cells.ChatActionCell$$ExternalSyntheticLambda10
                @Override // java.lang.Runnable
                public final void run() {
                    this.f$0.lambda$didPressCustomBotButton$8(baseFragment);
                }
            });
            return;
        }
        if (i == 6) {
            MessageObject messageObject3 = this.currentMessageObject;
            if (messageObject3 == null || (message3 = messageObject3.messageOwner) == null) {
                return;
            }
            TLRPC.MessageAction messageAction = message3.action;
            if (messageAction instanceof TLRPC.TL_messageActionStarGiftPurchaseOffer) {
                GiftOfferSheet.openOfferAcceptAlert(LaunchActivity.getLastFragment(), getContext(), this.themeDelegate, this.currentAccount, this.currentMessageObject.getDialogId(), this.currentMessageObject.getId(), (TLRPC.TL_messageActionStarGiftPurchaseOffer) messageAction);
                return;
            }
            return;
        }
        if (i == 7) {
            ChatActionCellDelegate chatActionCellDelegate2 = this.delegate;
            BaseFragment baseFragment2 = chatActionCellDelegate2 != null ? chatActionCellDelegate2.getBaseFragment() : null;
            if (baseFragment2 == null || (messageObject2 = this.currentMessageObject) == null || (message2 = messageObject2.messageOwner) == null) {
                return;
            }
            TLRPC.MessageAction messageAction2 = message2.action;
            if (messageAction2 instanceof TLRPC.TL_messageActionNoForwardsRequest) {
                final TLRPC.TL_messageActionNoForwardsRequest tL_messageActionNoForwardsRequest = (TLRPC.TL_messageActionNoForwardsRequest) messageAction2;
                AlertsCreator.showSimpleConfirmAlert(baseFragment2, LocaleController.getString(tL_messageActionNoForwardsRequest.prev_value ? org.telegram.messenger.R.string.SharingOfferDisableCancelTitle : org.telegram.messenger.R.string.SharingOfferEnableCancelTitle), LocaleController.getString(tL_messageActionNoForwardsRequest.prev_value ? org.telegram.messenger.R.string.SharingOfferDisableCancelText : org.telegram.messenger.R.string.SharingOfferEnableCancelText), LocaleController.getString(org.telegram.messenger.R.string.SharingOfferCancelYes), false, new Runnable() { // from class: org.telegram.ui.Cells.ChatActionCell$$ExternalSyntheticLambda11
                    @Override // java.lang.Runnable
                    public final void run() {
                        this.f$0.lambda$didPressCustomBotButton$9(tL_messageActionNoForwardsRequest);
                    }
                });
                return;
            }
            return;
        }
        if (i == 8) {
            ChatActionCellDelegate chatActionCellDelegate3 = this.delegate;
            BaseFragment baseFragment3 = chatActionCellDelegate3 != null ? chatActionCellDelegate3.getBaseFragment() : null;
            if (baseFragment3 == null || (messageObject = this.currentMessageObject) == null || (message = messageObject.messageOwner) == null) {
                return;
            }
            TLRPC.MessageAction messageAction3 = message.action;
            if (messageAction3 instanceof TLRPC.TL_messageActionNoForwardsRequest) {
                final TLRPC.TL_messageActionNoForwardsRequest tL_messageActionNoForwardsRequest2 = (TLRPC.TL_messageActionNoForwardsRequest) messageAction3;
                AlertsCreator.showSimpleConfirmAlert(baseFragment3, LocaleController.getString(tL_messageActionNoForwardsRequest2.new_value ? org.telegram.messenger.R.string.SharingOfferDisableCancelTitle : org.telegram.messenger.R.string.SharingOfferEnableCancelTitle), LocaleController.getString(tL_messageActionNoForwardsRequest2.new_value ? org.telegram.messenger.R.string.SharingOfferDisableConfirmText : org.telegram.messenger.R.string.SharingOfferEnableConfirmText), LocaleController.getString(org.telegram.messenger.R.string.SharingOfferCancelYes), false, new Runnable() { // from class: org.telegram.ui.Cells.ChatActionCell$$ExternalSyntheticLambda12
                    @Override // java.lang.Runnable
                    public final void run() {
                        this.f$0.lambda$didPressCustomBotButton$10(tL_messageActionNoForwardsRequest2);
                    }
                });
            }
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$didPressCustomBotButton$8(final BaseFragment baseFragment) {
        TL_payments.TL_resolveStarGiftOffer tL_resolveStarGiftOffer = new TL_payments.TL_resolveStarGiftOffer();
        tL_resolveStarGiftOffer.offer_msg_id = getMessageObject().getId();
        tL_resolveStarGiftOffer.decline = true;
        ConnectionsManager.getInstance(this.currentAccount).sendRequestTyped(tL_resolveStarGiftOffer, new Utilities.Callback2() { // from class: org.telegram.ui.Cells.ChatActionCell$$ExternalSyntheticLambda14
            @Override // org.telegram.messenger.Utilities.Callback2
            public final void run(Object obj, Object obj2) {
                this.f$0.lambda$didPressCustomBotButton$7(baseFragment, (TLRPC.Updates) obj, (TLRPC.TL_error) obj2);
            }
        });
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$didPressCustomBotButton$7(final BaseFragment baseFragment, TLRPC.Updates updates, final TLRPC.TL_error tL_error) {
        if (updates != null) {
            MessagesController.getInstance(this.currentAccount).processUpdates(updates, false);
        }
        if (tL_error != null) {
            AndroidUtilities.runOnUIThread(new Runnable() { // from class: org.telegram.ui.Cells.ChatActionCell$$ExternalSyntheticLambda15
                @Override // java.lang.Runnable
                public final void run() {
                    BulletinFactory.of(baseFragment).showForError(tL_error);
                }
            });
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$didPressCustomBotButton$9(TLRPC.TL_messageActionNoForwardsRequest tL_messageActionNoForwardsRequest) {
        MessagesController.getInstance(this.currentAccount).toggleChatNoForwards(this.currentMessageObject.getDialogId(), this.currentMessageObject.getId(), tL_messageActionNoForwardsRequest.prev_value, null);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$didPressCustomBotButton$10(TLRPC.TL_messageActionNoForwardsRequest tL_messageActionNoForwardsRequest) {
        MessagesController.getInstance(this.currentAccount).toggleChatNoForwards(this.currentMessageObject.getDialogId(), this.currentMessageObject.getId(), tL_messageActionNoForwardsRequest.new_value, null);
    }

    public void drawReactions(Canvas canvas, boolean z, Integer num) {
        if (this.canDrawInParent) {
            if (hasGradientService() && !z) {
                return;
            }
            if (!hasGradientService() && z) {
                return;
            }
        }
        drawReactionsLayout(canvas, z, num);
    }

    public void drawReactionsLayout(Canvas canvas, boolean z, Integer num) {
        Canvas canvas2;
        float alpha = z ? getAlpha() : 1.0f;
        Theme.ResourcesProvider resourcesProvider = this.themeDelegate;
        if (resourcesProvider != null) {
            resourcesProvider.applyServiceShaderMatrix(getMeasuredWidth(), this.backgroundHeight, this.viewTranslationX, this.viewTop + AndroidUtilities.dp(4.0f));
        } else {
            Theme.applyServiceShaderMatrix(getMeasuredWidth(), this.backgroundHeight, this.viewTranslationX, this.viewTop + AndroidUtilities.dp(4.0f));
        }
        MessageObject messageObject = this.currentMessageObject;
        if (messageObject == null || !messageObject.shouldDrawReactions()) {
            return;
        }
        ReactionsLayoutInBubble reactionsLayoutInBubble = this.reactionsLayoutInBubble;
        if (!reactionsLayoutInBubble.isSmall || (this.transitionParams.animateChange && reactionsLayoutInBubble.animateHeight)) {
            reactionsLayoutInBubble.drawServiceShaderBackground = 1.0f;
            if (alpha < 1.0f) {
                canvas2 = canvas;
                canvas2.saveLayerAlpha(0.0f, 0.0f, getWidth(), getHeight(), (int) (alpha * 255.0f), 31);
            } else {
                canvas2 = canvas;
            }
            ReactionsLayoutInBubble reactionsLayoutInBubble2 = this.reactionsLayoutInBubble;
            TransitionParams transitionParams = this.transitionParams;
            reactionsLayoutInBubble2.draw(canvas2, transitionParams.animateChange ? transitionParams.animateChangeProgress : 1.0f, num);
            if (alpha < 1.0f) {
                canvas2.restore();
            }
        }
    }

    public void drawReactionsLayoutOverlay(Canvas canvas, boolean z) {
        Canvas canvas2;
        float alpha = z ? getAlpha() : 1.0f;
        Theme.ResourcesProvider resourcesProvider = this.themeDelegate;
        if (resourcesProvider != null) {
            resourcesProvider.applyServiceShaderMatrix(getMeasuredWidth(), this.backgroundHeight, this.viewTranslationX, this.viewTop + AndroidUtilities.dp(4.0f));
        } else {
            Theme.applyServiceShaderMatrix(getMeasuredWidth(), this.backgroundHeight, this.viewTranslationX, this.viewTop + AndroidUtilities.dp(4.0f));
        }
        MessageObject messageObject = this.currentMessageObject;
        if (messageObject == null || !messageObject.shouldDrawReactions()) {
            return;
        }
        ReactionsLayoutInBubble reactionsLayoutInBubble = this.reactionsLayoutInBubble;
        if (!reactionsLayoutInBubble.isSmall || (this.transitionParams.animateChange && reactionsLayoutInBubble.animateHeight)) {
            reactionsLayoutInBubble.drawServiceShaderBackground = 1.0f;
            if (alpha < 1.0f) {
                canvas2 = canvas;
                canvas2.saveLayerAlpha(0.0f, 0.0f, getWidth(), getHeight(), (int) (alpha * 255.0f), 31);
            } else {
                canvas2 = canvas;
            }
            ReactionsLayoutInBubble reactionsLayoutInBubble2 = this.reactionsLayoutInBubble;
            TransitionParams transitionParams = this.transitionParams;
            reactionsLayoutInBubble2.drawOverlay(canvas2, transitionParams.animateChange ? transitionParams.animateChangeProgress : 1.0f);
            if (alpha < 1.0f) {
                canvas2.restore();
            }
        }
    }

    @Override // org.telegram.ui.Cells.BaseCell
    public int getBoundsLeft() {
        if (this.starGiftLayout.has()) {
            int width = ((int) (getWidth() - (this.starGiftLayout.getWidth() + AndroidUtilities.dp(8.0f)))) / 2;
            return this.starGiftLayout.repost ? width : Math.min(this.backgroundLeft, width);
        }
        if (isButtonLayout(this.currentMessageObject)) {
            return (this.sideMenuWidth / 2) + ((getWidth() - this.giftRectSize) / 2);
        }
        int iMin = this.backgroundLeft;
        ImageReceiver imageReceiver = this.imageReceiver;
        if (imageReceiver != null && imageReceiver.getVisible()) {
            iMin = Math.min((int) this.imageReceiver.getImageX(), iMin);
        }
        return (this.sideMenuWidth / 2) + iMin;
    }

    @Override // org.telegram.ui.Cells.BaseCell
    public int getBoundsRight() {
        if (this.starGiftLayout.has()) {
            int width = ((int) (getWidth() + (this.starGiftLayout.getWidth() + AndroidUtilities.dp(8.0f)))) / 2;
            return this.starGiftLayout.repost ? width : Math.max(this.backgroundRight, width);
        }
        if (isButtonLayout(this.currentMessageObject)) {
            return (this.sideMenuWidth / 2) + ((getWidth() + this.giftRectSize) / 2);
        }
        int iMax = this.backgroundRight;
        ImageReceiver imageReceiver = this.imageReceiver;
        if (imageReceiver != null && imageReceiver.getVisible()) {
            iMax = Math.max((int) this.imageReceiver.getImageX2(), iMax);
        }
        return (this.sideMenuWidth / 2) + iMax;
    }

    public boolean hasGradientService() {
        if (this.overrideBackgroundPaint != null) {
            return false;
        }
        Theme.ResourcesProvider resourcesProvider = this.themeDelegate;
        if (resourcesProvider != null) {
            return resourcesProvider.hasGradientService();
        }
        return Theme.hasGradientService();
    }

    @Override // org.telegram.messenger.DownloadController.FileDownloadProgressListener
    public void onSuccessDownload(String str) {
        TLRPC.PhotoSize photoSize;
        MessageObject messageObject = this.currentMessageObject;
        if (messageObject == null || messageObject.type != 11) {
            return;
        }
        int size = messageObject.photoThumbs.size();
        for (int i = 0; i < size; i++) {
            photoSize = messageObject.photoThumbs.get(i);
            if (photoSize instanceof TLRPC.TL_photoStrippedSize) {
                this.imageReceiver.setImage(this.currentVideoLocation, ImageLoader.AUTOPLAY_FILTER, ImageLocation.getForObject(photoSize, messageObject.photoThumbsObject), "50_50_b", this.avatarDrawable, 0L, null, messageObject, 1);
                DownloadController.getInstance(this.currentAccount).removeLoadingFileObserver(this);
            }
        }
        photoSize = null;
        this.imageReceiver.setImage(this.currentVideoLocation, ImageLoader.AUTOPLAY_FILTER, ImageLocation.getForObject(photoSize, messageObject.photoThumbsObject), "50_50_b", this.avatarDrawable, 0L, null, messageObject, 1);
        DownloadController.getInstance(this.currentAccount).removeLoadingFileObserver(this);
    }

    @Override // org.telegram.messenger.DownloadController.FileDownloadProgressListener
    public int getObserverTag() {
        return this.TAG;
    }

    @Override // android.view.View
    public void onInitializeAccessibilityNodeInfo(AccessibilityNodeInfo accessibilityNodeInfo) {
        super.onInitializeAccessibilityNodeInfo(accessibilityNodeInfo);
        MessageObject messageObject = this.currentMessageObject;
        if (TextUtils.isEmpty(this.customText) && messageObject == null) {
            return;
        }
        if (this.accessibilityText == null) {
            SpannableStringBuilder spannableStringBuilder = new SpannableStringBuilder(!TextUtils.isEmpty(this.customText) ? this.customText : messageObject.messageText);
            for (final CharacterStyle characterStyle : (CharacterStyle[]) spannableStringBuilder.getSpans(0, spannableStringBuilder.length(), ClickableSpan.class)) {
                int spanStart = spannableStringBuilder.getSpanStart(characterStyle);
                int spanEnd = spannableStringBuilder.getSpanEnd(characterStyle);
                spannableStringBuilder.removeSpan(characterStyle);
                spannableStringBuilder.setSpan(new ClickableSpan() { // from class: org.telegram.ui.Cells.ChatActionCell.1
                    @Override // android.text.style.ClickableSpan
                    public void onClick(View view) {
                        if (ChatActionCell.this.delegate != null) {
                            ChatActionCell.this.openLink(characterStyle);
                        }
                    }
                }, spanStart, spanEnd, 33);
            }
            this.accessibilityText = spannableStringBuilder;
        }
        if (Build.VERSION.SDK_INT < 24) {
            accessibilityNodeInfo.setContentDescription(this.accessibilityText.toString());
        } else {
            accessibilityNodeInfo.setText(this.accessibilityText);
        }
        accessibilityNodeInfo.setEnabled(true);
    }

    public void setInvalidateColors(boolean z) {
        if (this.invalidateColors == z) {
            return;
        }
        this.invalidateColors = z;
        invalidate();
    }

    private int getThemedColor(int i) {
        return Theme.getColor(i, this.themeDelegate);
    }

    protected Paint getThemedPaint(String str) {
        Theme.ResourcesProvider resourcesProvider = this.themeDelegate;
        Paint paint = resourcesProvider != null ? resourcesProvider.getPaint(str) : null;
        return paint != null ? paint : Theme.getThemePaint(str);
    }

    public void drawOutboundsContent(Canvas canvas) {
        canvas.save();
        canvas.translate(this.sideMenuWidth / 2.0f, getPaddingTop());
        canvas.save();
        canvas.translate(this.textXLeft, this.textY);
        StaticLayout staticLayout = this.textLayout;
        AnimatedEmojiSpan.drawAnimatedEmojis(canvas, staticLayout, this.animatedEmojiStack, 0.0f, this.spoilers, 0.0f, 0.0f, 0.0f, 1.0f, staticLayout != null ? getAdaptiveEmojiColorFilter(staticLayout.getPaint().getColor()) : null);
        canvas.restore();
        if (this.starGiftLayout.has()) {
            canvas.save();
            canvas.translate((getWidth() - this.starGiftLayout.getWidth()) / 2.0f, this.starGiftLayout.repost ? AndroidUtilities.dp(4.0f) : this.textY + this.textHeight + AndroidUtilities.dp(16.0f));
            this.starGiftLayout.drawOutbounds(canvas);
            canvas.restore();
        }
        canvas.restore();
        if (this.topicSeparator != null) {
            float alpha = getAlpha();
            Theme.ResourcesProvider resourcesProvider = this.themeDelegate;
            if (resourcesProvider != null) {
                resourcesProvider.applyServiceShaderMatrix(getMeasuredWidth(), this.backgroundHeight, this.viewTranslationX, this.viewTop + 0.0f);
            } else {
                Theme.applyServiceShaderMatrix(getMeasuredWidth(), this.backgroundHeight, this.viewTranslationX, this.viewTop + 0.0f);
            }
            this.topicSeparator.draw(canvas, getWidth(), this.sideMenuWidth, 0.0f, 1.0f, alpha, this.showTopicSeparator);
        }
        drawBotButtons(canvas, this.botButtons);
    }

    private boolean isButtonLayout(MessageObject messageObject) {
        if (messageObject == null) {
            return false;
        }
        int i = messageObject.type;
        return i == 30 || i == 18 || i == 25 || isNewStyleButtonLayout();
    }

    private boolean isGiftChannel(MessageObject messageObject) {
        return messageObject != null && messageObject.type == 25;
    }

    public void setInvalidatesParent(boolean z) {
        this.invalidatesParent = z;
    }

    public void setInvalidateListener(Runnable runnable) {
        this.invalidateListener = runnable;
    }

    @Override // org.telegram.ui.Cells.BaseCell, android.view.View
    public void invalidate() {
        super.invalidate();
        View view = this.invalidateWithParent;
        if (view != null) {
            view.invalidate();
        }
        Runnable runnable = this.invalidateListener;
        if (runnable != null) {
            runnable.run();
        }
        if (!this.invalidatesParent || getParent() == null) {
            return;
        }
        View view2 = (View) getParent();
        if (view2.getParent() != null) {
            view2.invalidate();
            ((View) view2.getParent()).invalidate();
        }
    }

    public void invalidateOutbounds() {
        ChatActionCellDelegate chatActionCellDelegate = this.delegate;
        if (chatActionCellDelegate == null || !chatActionCellDelegate.canDrawOutboundsContent()) {
            if (getParent() instanceof View) {
                ((View) getParent()).invalidate();
                return;
            }
            return;
        }
        super.invalidate();
    }

    @Override // android.view.View
    public void invalidate(Rect rect) {
        super.invalidate(rect);
        View view = this.invalidateWithParent;
        if (view != null) {
            view.invalidate();
        }
        if (!this.invalidatesParent || getParent() == null) {
            return;
        }
        View view2 = (View) getParent();
        if (view2.getParent() != null) {
            view2.invalidate();
            ((View) view2.getParent()).invalidate();
        }
    }

    @Override // android.view.View
    public void invalidate(int i, int i2, int i3, int i4) {
        super.invalidate(i, i2, i3, i4);
        View view = this.invalidateWithParent;
        if (view != null) {
            view.invalidate();
        }
        if (!this.invalidatesParent || getParent() == null) {
            return;
        }
        View view2 = (View) getParent();
        if (view2.getParent() != null) {
            view2.invalidate();
            ((View) view2.getParent()).invalidate();
        }
    }

    @Override // android.view.View
    protected boolean verifyDrawable(Drawable drawable) {
        return drawable == this.wallpaperPreviewDrawable || super.verifyDrawable(drawable);
    }

    private ColorFilter getAdaptiveEmojiColorFilter(int i) {
        if (i != this.adaptiveEmojiColor || this.adaptiveEmojiColorFilter == null) {
            this.adaptiveEmojiColor = i;
            this.adaptiveEmojiColorFilter = new PorterDuffColorFilter(i, PorterDuff.Mode.SRC_IN);
        }
        return this.adaptiveEmojiColorFilter;
    }

    public ReactionsLayoutInBubble.ReactionButton getReactionButton(ReactionsLayoutInBubble.VisibleReaction visibleReaction) {
        return this.reactionsLayoutInBubble.getReactionButton(visibleReaction);
    }

    public class TransitionParams {
        public boolean animateChange;
        public float animateChangeProgress = 1.0f;
        public boolean wasDraw;

        public boolean supportChangeAnimation() {
            return true;
        }

        public TransitionParams() {
        }

        public void recordDrawingState() {
            this.wasDraw = true;
            ChatActionCell.this.reactionsLayoutInBubble.recordDrawingState();
        }

        public boolean animateChange() {
            if (this.wasDraw) {
                return ChatActionCell.this.reactionsLayoutInBubble.animateChange();
            }
            return false;
        }

        public void onDetach() {
            this.wasDraw = false;
        }

        public void resetAnimation() {
            this.animateChange = false;
            this.animateChangeProgress = 1.0f;
        }
    }

    public TransitionParams getTransitionParams() {
        return this.transitionParams;
    }

    public void setScrimReaction(Integer num) {
        this.reactionsLayoutInBubble.setScrimReaction(num);
    }

    public void drawScrimReaction(Canvas canvas, Integer num, float f, boolean z) {
        if (this.reactionsLayoutInBubble.isSmall) {
            return;
        }
        Theme.ResourcesProvider resourcesProvider = this.themeDelegate;
        if (resourcesProvider != null) {
            resourcesProvider.applyServiceShaderMatrix(getMeasuredWidth(), this.backgroundHeight, this.viewTranslationX, this.viewTop + AndroidUtilities.dp(4.0f));
        } else {
            Theme.applyServiceShaderMatrix(getMeasuredWidth(), this.backgroundHeight, this.viewTranslationX, this.viewTop + AndroidUtilities.dp(4.0f));
        }
        this.reactionsLayoutInBubble.setScrimProgress(f, z);
        this.reactionsLayoutInBubble.draw(canvas, this.transitionParams.animateChangeProgress, num);
    }

    public void drawScrimReactionPreview(View view, Canvas canvas, int i, Integer num, float f) {
        if (this.reactionsLayoutInBubble.isSmall) {
            return;
        }
        Theme.ResourcesProvider resourcesProvider = this.themeDelegate;
        if (resourcesProvider != null) {
            resourcesProvider.applyServiceShaderMatrix(getMeasuredWidth(), this.backgroundHeight, this.viewTranslationX, this.viewTop + AndroidUtilities.dp(4.0f));
        } else {
            Theme.applyServiceShaderMatrix(getMeasuredWidth(), this.backgroundHeight, this.viewTranslationX, this.viewTop + AndroidUtilities.dp(4.0f));
        }
        this.reactionsLayoutInBubble.setScrimProgress(f);
        this.reactionsLayoutInBubble.drawPreview(view, canvas, i, num);
    }

    public boolean checkUnreadReactions(float f, int i) {
        if (!this.reactionsLayoutInBubble.hasUnreadReactions) {
            return false;
        }
        float y = getY();
        ReactionsLayoutInBubble reactionsLayoutInBubble = this.reactionsLayoutInBubble;
        float f2 = y + reactionsLayoutInBubble.y;
        return f2 > f && (f2 + ((float) reactionsLayoutInBubble.height)) - ((float) AndroidUtilities.dp(16.0f)) < ((float) i);
    }

    public void markReactionsAsRead() {
        this.reactionsLayoutInBubble.hasUnreadReactions = false;
        MessageObject messageObject = this.currentMessageObject;
        if (messageObject == null) {
            return;
        }
        messageObject.markReactionsAsRead();
    }
}
