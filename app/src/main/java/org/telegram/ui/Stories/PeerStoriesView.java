package org.telegram.ui.Stories;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.LayoutTransition;
import android.animation.ValueAnimator;
import android.app.Activity;
import android.app.Dialog;
import android.content.ClipData;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffColorFilter;
import android.graphics.RecordingCanvas;
import android.graphics.Rect;
import android.graphics.RectF;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.GradientDrawable;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.text.Layout;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.SpannableStringBuilder;
import android.text.Spanned;
import android.text.TextPaint;
import android.text.TextUtils;
import android.text.style.CharacterStyle;
import android.text.style.ClickableSpan;
import android.text.style.ReplacementSpan;
import android.text.style.URLSpan;
import android.view.Menu;
import android.view.MotionEvent;
import android.view.SurfaceView;
import android.view.TextureView;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewPropertyAnimator;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import androidx.camera.camera2.pipe.CameraTimestamp$$ExternalSyntheticBackport0;
import androidx.collection.LongSparseArray;
import androidx.core.content.ContextCompat;
import androidx.core.graphics.ColorUtils;
import androidx.core.math.MathUtils;
import androidx.recyclerview.widget.ChatListItemAnimator;
import com.exteragram.messenger.ExteraConfig;
import com.exteragram.messenger.utils.system.VibratorUtils;
import com.google.android.exoplayer2.util.Consumer;
import j$.util.Objects;
import java.io.File;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;
import java.util.concurrent.CountDownLatch;
import okhttp3.internal.url._UrlKt;
import org.telegram.messenger.AccountInstance;
import org.telegram.messenger.AndroidUtilities;
import org.telegram.messenger.AnimationNotificationsLocker;
import org.telegram.messenger.BotWebViewVibrationEffect;
import org.telegram.messenger.BuildVars;
import org.telegram.messenger.ChannelBoostsController;
import org.telegram.messenger.ChatObject;
import org.telegram.messenger.ContactsController;
import org.telegram.messenger.DialogObject;
import org.telegram.messenger.Emoji;
import org.telegram.messenger.FileLoader;
import org.telegram.messenger.FileLog;
import org.telegram.messenger.ImageLoader;
import org.telegram.messenger.ImageLocation;
import org.telegram.messenger.ImageReceiver;
import org.telegram.messenger.LocaleController;
import org.telegram.messenger.MediaController;
import org.telegram.messenger.MediaDataController;
import org.telegram.messenger.MessageObject;
import org.telegram.messenger.MessagesController;
import org.telegram.messenger.MessagesStorage;
import org.telegram.messenger.NotificationCenter;
import org.telegram.messenger.NotificationsController;
import org.telegram.messenger.NotificationsSettingsFacade;
import org.telegram.messenger.R;
import org.telegram.messenger.SendMessagesHelper;
import org.telegram.messenger.SharedConfig;
import org.telegram.messenger.UserConfig;
import org.telegram.messenger.UserObject;
import org.telegram.messenger.Utilities;
import org.telegram.messenger.VideoEditedInfo;
import org.telegram.messenger.browser.Browser;
import org.telegram.messenger.camera.CameraView;
import org.telegram.tgnet.ConnectionsManager;
import org.telegram.tgnet.RequestDelegate;
import org.telegram.tgnet.TLObject;
import org.telegram.tgnet.TLRPC;
import org.telegram.tgnet.tl.TL_account;
import org.telegram.tgnet.tl.TL_phone;
import org.telegram.tgnet.tl.TL_stories;
import org.telegram.ui.AccountFrozenAlert;
import org.telegram.ui.ActionBar.ActionBar;
import org.telegram.ui.ActionBar.ActionBarMenuItem;
import org.telegram.ui.ActionBar.ActionBarMenuSubItem;
import org.telegram.ui.ActionBar.ActionBarPopupWindow;
import org.telegram.ui.ActionBar.AdjustPanLayoutHelper;
import org.telegram.ui.ActionBar.AlertDialog;
import org.telegram.ui.ActionBar.BaseFragment;
import org.telegram.ui.ActionBar.BottomSheet;
import org.telegram.ui.ActionBar.INavigationLayout;
import org.telegram.ui.ActionBar.SimpleTextView;
import org.telegram.ui.ActionBar.Theme;
import org.telegram.ui.AvatarSpan;
import org.telegram.ui.Cells.TextSelectionHelper;
import org.telegram.ui.ChatActivity;
import org.telegram.ui.ChooseSpeedLayout;
import org.telegram.ui.Components.AlertsCreator;
import org.telegram.ui.Components.AnimatedEmojiDrawable;
import org.telegram.ui.Components.AnimatedEmojiSpan;
import org.telegram.ui.Components.AnimatedFloat;
import org.telegram.ui.Components.AnimatedTextView;
import org.telegram.ui.Components.AvatarDrawable;
import org.telegram.ui.Components.AvatarsImageView;
import org.telegram.ui.Components.BackupImageView;
import org.telegram.ui.Components.BitmapShaderTools;
import org.telegram.ui.Components.Bulletin;
import org.telegram.ui.Components.BulletinFactory;
import org.telegram.ui.Components.ChatActivityEnterView;
import org.telegram.ui.Components.ChatAttachAlert;
import org.telegram.ui.Components.ChatAttachAlertDocumentLayout;
import org.telegram.ui.Components.ColoredImageSpan;
import org.telegram.ui.Components.CombinedDrawable;
import org.telegram.ui.Components.CubicBezierInterpolator;
import org.telegram.ui.Components.CustomPopupMenu;
import org.telegram.ui.Components.DotDividerSpan;
import org.telegram.ui.Components.EditTextCaption;
import org.telegram.ui.Components.EmojiPacksAlert;
import org.telegram.ui.Components.HashtagActivity;
import org.telegram.ui.Components.HintView;
import org.telegram.ui.Components.InstantCameraView;
import org.telegram.ui.Components.ItemOptions;
import org.telegram.ui.Components.LayoutHelper;
import org.telegram.ui.Components.LoadingDrawable;
import org.telegram.ui.Components.MediaActivity;
import org.telegram.ui.Components.MentionsContainerView;
import org.telegram.ui.Components.NumberTextView;
import org.telegram.ui.Components.Premium.LimitReachedBottomSheet;
import org.telegram.ui.Components.RLottieDrawable;
import org.telegram.ui.Components.RLottieImageView;
import org.telegram.ui.Components.RadialProgress;
import org.telegram.ui.Components.Reactions.AnimatedEmojiEffect;
import org.telegram.ui.Components.Reactions.ReactionImageHolder;
import org.telegram.ui.Components.Reactions.ReactionsEffectOverlay;
import org.telegram.ui.Components.Reactions.ReactionsLayoutInBubble;
import org.telegram.ui.Components.Reactions.ReactionsUtils;
import org.telegram.ui.Components.ReactionsContainerLayout;
import org.telegram.ui.Components.ScaleStateListAnimator;
import org.telegram.ui.Components.SenderSelectView;
import org.telegram.ui.Components.ShareAlert;
import org.telegram.ui.Components.SizeNotifierFrameLayout;
import org.telegram.ui.Components.SpeedIconDrawable;
import org.telegram.ui.Components.Text;
import org.telegram.ui.Components.TranslateAlert2;
import org.telegram.ui.Components.URLSpanMono;
import org.telegram.ui.Components.URLSpanNoUnderline;
import org.telegram.ui.Components.URLSpanReplacement;
import org.telegram.ui.Components.URLSpanUserMention;
import org.telegram.ui.Components.blur3.BlurredBackgroundDrawableViewFactory;
import org.telegram.ui.Components.blur3.drawable.BlurredBackgroundDrawable;
import org.telegram.ui.Components.blur3.drawable.color.BlurredBackgroundColorProviderThemed;
import org.telegram.ui.Components.blur3.source.BlurredBackgroundSource;
import org.telegram.ui.Components.blur3.source.BlurredBackgroundSourceColor;
import org.telegram.ui.Components.blur3.source.BlurredBackgroundSourceRenderNode;
import org.telegram.ui.Components.chat.ViewPositionWatcher;
import org.telegram.ui.Components.voip.CellFlickerDrawable;
import org.telegram.ui.DialogsActivity;
import org.telegram.ui.EmojiAnimationsOverlay;
import org.telegram.ui.LaunchActivity;
import org.telegram.ui.MessageStatisticActivity;
import org.telegram.ui.NotificationsCustomSettingsActivity;
import org.telegram.ui.PinchToZoomHelper;
import org.telegram.ui.PremiumPreviewFragment;
import org.telegram.ui.ProfileActivity;
import org.telegram.ui.ReportBottomSheet;
import org.telegram.ui.Stars.StarsController;
import org.telegram.ui.Stars.StarsIntroActivity;
import org.telegram.ui.Stories.recorder.CaptionContainerView;
import org.telegram.ui.Stories.recorder.DraftsController;
import org.telegram.ui.Stories.recorder.HintView2;
import org.telegram.ui.Stories.recorder.LivePlayerView;
import org.telegram.ui.Stories.recorder.StoryEntry;
import org.telegram.ui.Stories.recorder.StoryPrivacyBottomSheet;
import org.telegram.ui.Stories.recorder.StoryRecorder;
import org.telegram.ui.WrappedResourceProvider;

public abstract class PeerStoriesView extends SizeNotifierFrameLayout implements NotificationCenter.NotificationCenterDelegate {
    public static boolean DISABLE_STORY_REPOSTING = false;
    private static int activeCount;
    private boolean BIG_SCREEN;
    private ActionBarMenuSubItem albumItem;
    private ViewGroup albumLayout;
    private boolean allowDrawSurface;
    Runnable allowDrawSurfaceRunnable;
    private boolean allowRepost;
    private boolean allowShare;
    private boolean allowShareLink;
    private float alpha;
    boolean animateKeyboardOpening;
    private float animatingKeyboardHeight;
    boolean areLiveCommentsDisabled;
    private boolean attachedToWindow;
    private final AvatarDrawable avatarDrawable;
    private final BitmapShaderTools bitmapShaderTools;
    private BlurredBackgroundColorProviderThemed blurredBackgroundColorProvider;
    private BlurredBackgroundDrawableViewFactory blurredBackgroundDrawableFactory;
    private final BlurredBackgroundSourceColor blurredBackgroundSourceFallback;
    private final BlurredBackgroundSourceRenderNode blurredBackgroundSourceRenderNodeWithSaturation;
    private final BlurredBackgroundSource blurredBackgroundSourceWithSaturation;
    private TL_stories.TL_premium_boostsStatus boostsStatus;
    private final LinearLayout bottomActionsLinearLayout;
    private ChannelBoostsController.CanApplyBoost canApplyBoost;
    private Runnable cancellableViews;
    private ValueAnimator changeBoundAnimator;
    ChatActivityEnterView chatActivityEnterView;
    private ChatAttachAlert chatAttachAlert;
    boolean checkBlackoutMode;
    private int classGuid;
    private final Path clipPath;
    private CommentButton commentButton;
    int count;
    private int currentAccount;
    private long currentImageTime;
    public final StoryItemHolder currentStory;
    ArrayList day;
    Delegate delegate;
    private boolean deletedPeer;
    private long dialogId;
    ArrayList documentsToPrepare;
    private boolean drawAnimatedEmojiAsMovingReaction;
    private boolean drawReactionEffect;
    public boolean editOpened;
    ActionBarMenuSubItem editStoryItem;
    private boolean editedPrivacy;
    private EmojiAnimationsOverlay emojiAnimationsOverlay;
    BlurredBackgroundDrawable emojiKeyboardBackground;
    private AnimatedEmojiEffect emojiReactionEffect;
    private int enterViewBottomOffset;
    private StoryFailView failView;
    private ViewPropertyAnimator failViewAnimator;
    public boolean forceUpdateOffsets;
    PeerHeaderView headerView;
    HintView2 highlightMessageHintView;
    private boolean imageChanged;
    private final ImageReceiver imageReceiver;
    boolean inBlackoutMode;
    Paint inputBackgroundPaint;
    Paint inputBottomBorderPaint;
    BlurredBackgroundDrawable inputFieldBackground;
    Paint inputTopBorderPaint;
    private InstantCameraView instantCameraView;
    boolean isActive;
    private boolean isCaptionPartVisible;
    boolean isChannel;
    private boolean isEditing;
    private boolean isFailed;
    boolean isGroup;
    private boolean isLongPressed;
    boolean isPremiumBlocked;
    private boolean isRecording;
    boolean isSelf;
    private boolean isUploading;
    private boolean isVisible;
    ValueAnimator keyboardAnimator;
    public boolean keyboardVisible;
    float lastAnimatingKeyboardHeight;
    private long lastDrawTime;
    int lastKeyboardHeight;
    private boolean lastNoThumb;
    int lastOpenedKeyboardHeight;
    private final ImageReceiver leftPreloadImageReceiver;
    private final FrameLayout likeButtonContainer;
    private ReactionsContainerLayout likesReactionLayout;
    private float likesReactionShowProgress;
    private boolean likesReactionShowing;
    private AnimatedFloat linesAlpha;
    private int linesCount;
    private int linesPosition;
    private int listPosition;
    public final View liveCommentsShadowView;
    public final LiveCommentsView liveCommentsView;
    private HintView mediaBanTooltip;
    private MentionsContainerView mentionContainer;
    private MentionsContainerView.Delegate mentionsDelegate;
    private boolean messageSent;
    private long messageStars;
    private boolean movingReaction;
    private int movingReactionFromSize;
    private int movingReactionFromX;
    private int movingReactionFromY;
    private float movingReactionProgress;
    private MuteButton muteButton;
    private final FrameLayout muteIconContainer;
    private final RLottieImageView muteIconView;
    private float muteIconViewAlpha;
    private final ImageView noSoundIconView;
    final AnimationNotificationsLocker notificationsLocker;
    private Runnable onImageReceiverThumbLoaded;
    private final ImageView optionsIconView;
    private ValueAnimator outAnimator;
    private float outT;
    RoundRectOutlineProvider outlineProvider;
    private boolean paused;
    public PinchToZoomHelper pinchToZoomHelper;
    private final ImageView pipIconView;
    final VideoPlayerSharedScope playerSharedScope;
    CustomPopupMenu popupMenu;
    private final ArrayList preloadReactionHolders;
    private LinearLayout premiumBlockedText;
    private TextView premiumBlockedText1;
    private TextView premiumBlockedText2;
    private float prevToHideProgress;
    private int previousSelectedPotision;
    private final StoryPrivacyButton privacyButton;
    private HintView2 privacyHint;
    float progressToDismiss;
    private AnimatedFloat progressToHideInterface;
    float progressToKeyboard;
    AnimatedFloat progressToRecording;
    float progressToReply;
    AnimatedFloat progressToStickerExpanded;
    AnimatedFloat progressToTextA;
    private ImageReceiver reactionEffectImageReceiver;
    private AnimatedEmojiDrawable reactionMoveDrawable;
    private ImageReceiver reactionMoveImageReceiver;
    private int reactionsContainerIndex;
    ReactionsContainerLayout reactionsContainerLayout;
    private AnimatedTextView.AnimatedTextDrawable reactionsCounter;
    private AnimatedFloat reactionsCounterProgress;
    private boolean reactionsCounterVisible;
    private HintView2 reactionsLongpressTooltip;
    private Runnable reactionsTooltipRunnable;
    private int realKeyboardHeight;
    private TextView replyDisabledTextView;
    private ImageView repostButton;
    private FrameLayout repostButtonContainer;
    private AnimatedTextView.AnimatedTextDrawable repostCounter;
    private AnimatedFloat repostCounterProgress;
    private boolean repostCounterVisible;
    private final Theme.ResourcesProvider resourcesProvider;
    private final ImageReceiver rightPreloadImageReceiver;
    private int selectedPosition;
    private View selfAvatarsContainer;
    private AvatarsImageView selfAvatarsView;
    private TextView selfStatusView;
    private FrameLayout selfView;
    private TLRPC.TL_channels_sendAsPeers sendAsPeersObj;
    public ShareAlert shareAlert;
    private final ImageView shareButton;
    final SharedResources sharedResources;
    private int shiftDp;
    private final Runnable showTapToSoundHint;
    boolean showViewsProgress;
    private HintView2 soundTooltip;
    private ActionBarMenuSubItem speedItem;
    private ChooseSpeedLayout speedLayout;
    private PaidReactionButton starsButton;
    private PaidReactionButton.PaidReactionButtonEffectsView starsButtonEffectsView;
    long starsPriceBlocked;
    private boolean stealthModeIsActive;
    StoriesController storiesController;
    private StoriesLikeButton storiesLikeButton;
    private StoryMediaAreasView storyAreasView;
    private final StoryCaptionView storyCaptionView;
    public FrameLayout storyContainer;
    private CaptionContainerView storyEditCaptionView;
    final ArrayList storyItems;
    private final StoryLinesDrawable storyLines;
    private StoryPositionView storyPositionView;
    private final StoryViewer storyViewer;
    private boolean switchEventSent;
    private long titleLastDialogId;
    private boolean titleLastLive;
    public FrameLayout topBulletinContainer;
    private int totalStoriesCount;
    public boolean unsupported;
    private FrameLayout unsupportedContainer;
    Runnable updateStealthModeTimer;
    final ArrayList uploadingStories;
    ArrayList uriesToPrepare;
    private boolean userCanSeeViews;
    TL_stories.PeerStories userStories;
    public long videoDuration;
    private float viewsThumbAlpha;
    private SelfStoriesPreviewView.ImageHolder viewsThumbImageReceiver;
    private float viewsThumbPivotY;
    private float viewsThumbScale;
    private boolean wasBigScreen;
    private int watchersCount;

    public interface Delegate {
        int getKeyboardHeight();

        float getProgressToDismiss();

        boolean isClosed();

        void onPeerSelected(long j, int i);

        void preparePlayer(ArrayList arrayList, ArrayList arrayList2);

        boolean releasePlayer(Runnable runnable);

        void requestAdjust(boolean z);

        void requestPlayer(TLRPC.Document document, Uri uri, long j, VideoPlayerSharedScope videoPlayerSharedScope);

        void requestPlayer(TL_stories.StoryItem storyItem, long j, int i, boolean z, TLRPC.InputGroupCall inputGroupCall, VideoPlayerSharedScope videoPlayerSharedScope);

        void setAllowTouchesByViewPager(boolean z);

        void setBulletinIsVisible(boolean z);

        void setHideEnterViewProgress(float f);

        void setIsCaption(boolean z);

        void setIsCaptionPartVisible(boolean z);

        void setIsHintVisible(boolean z);

        void setIsInPinchToZoom(boolean z);

        void setIsInSelectionMode(boolean z);

        void setIsLikesReaction(boolean z);

        void setIsRecording(boolean z);

        void setIsSwiping(boolean z);

        void setIsWaiting(boolean z);

        void setKeyboardVisible(boolean z);

        void setPopupIsVisible(boolean z);

        void setTranslating(boolean z);

        void shouldSwitchToNext();

        void showDialog(Dialog dialog);

        void switchToNextAndRemoveCurrentPeer();
    }

    /* JADX INFO: Access modifiers changed from: private */
    public boolean drawLinesAsCounter() {
        return false;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public boolean hideCaptionWithInterface() {
        return true;
    }

    public abstract boolean isSelectedPeer();

    public PeerStoriesView(final Context context, final StoryViewer storyViewer, final SharedResources sharedResources, final Theme.ResourcesProvider resourcesProvider) {
        super(context);
        this.allowDrawSurface = true;
        this.preloadReactionHolders = new ArrayList();
        this.shiftDp = -5;
        this.alpha = 1.0f;
        this.previousSelectedPotision = -1;
        StoryItemHolder storyItemHolder = new StoryItemHolder();
        this.currentStory = storyItemHolder;
        this.progressToKeyboard = -1.0f;
        this.progressToDismiss = -1.0f;
        this.lastAnimatingKeyboardHeight = -1.0f;
        this.classGuid = ConnectionsManager.generateClassGuid();
        this.progressToHideInterface = new AnimatedFloat(this);
        this.linesAlpha = new AnimatedFloat(this);
        this.pinchToZoomHelper = new PinchToZoomHelper();
        this.muteIconViewAlpha = 1.0f;
        this.updateStealthModeTimer = new Runnable() { // from class: org.telegram.ui.Stories.PeerStoriesView$$ExternalSyntheticLambda7
            @Override // java.lang.Runnable
            public final void run() {
                this.f$0.lambda$new$43();
            }
        };
        this.showTapToSoundHint = new Runnable() { // from class: org.telegram.ui.Stories.PeerStoriesView$$ExternalSyntheticLambda10
            @Override // java.lang.Runnable
            public final void run() {
                this.f$0.lambda$new$51();
            }
        };
        this.uriesToPrepare = new ArrayList();
        this.documentsToPrepare = new ArrayList();
        this.allowDrawSurfaceRunnable = new Runnable() { // from class: org.telegram.ui.Stories.PeerStoriesView.34
            @Override // java.lang.Runnable
            public void run() {
                PeerStoriesView peerStoriesView = PeerStoriesView.this;
                if (peerStoriesView.isActive && peerStoriesView.allowDrawSurface) {
                    PeerStoriesView.this.delegate.setIsSwiping(false);
                }
            }
        };
        this.progressToRecording = new AnimatedFloat(this);
        this.progressToTextA = new AnimatedFloat(this);
        this.progressToStickerExpanded = new AnimatedFloat(this);
        this.clipPath = new Path();
        this.pinchToZoomHelper.setCallback(new PinchToZoomHelper.Callback() { // from class: org.telegram.ui.Stories.PeerStoriesView.1
            @Override // org.telegram.ui.PinchToZoomHelper.Callback
            public /* synthetic */ TextureView getCurrentTextureView() {
                return PinchToZoomHelper.Callback.CC.$default$getCurrentTextureView(this);
            }

            @Override // org.telegram.ui.PinchToZoomHelper.Callback
            public void onZoomStarted(MessageObject messageObject) {
                PeerStoriesView.this.delegate.setIsInPinchToZoom(true);
            }

            @Override // org.telegram.ui.PinchToZoomHelper.Callback
            public void onZoomFinished(MessageObject messageObject) {
                PeerStoriesView.this.delegate.setIsInPinchToZoom(false);
            }
        });
        this.playerSharedScope = new VideoPlayerSharedScope();
        this.notificationsLocker = new AnimationNotificationsLocker();
        this.storyItems = new ArrayList();
        this.uploadingStories = new ArrayList();
        ImageReceiver imageReceiver = new ImageReceiver() { // from class: org.telegram.ui.Stories.PeerStoriesView.2
            @Override // org.telegram.messenger.ImageReceiver
            protected boolean setImageBitmapByKey(Drawable drawable, String str, int i, boolean z, int i2) {
                boolean imageBitmapByKey = super.setImageBitmapByKey(drawable, str, i, z, i2);
                if (i == 1 && PeerStoriesView.this.onImageReceiverThumbLoaded != null) {
                    PeerStoriesView.this.onImageReceiverThumbLoaded.run();
                    PeerStoriesView.this.onImageReceiverThumbLoaded = null;
                }
                return imageBitmapByKey;
            }
        };
        this.imageReceiver = imageReceiver;
        imageReceiver.setCrossfadeWithOldImage(false);
        imageReceiver.setAllowLoadingOnAttachedOnly(true);
        imageReceiver.ignoreNotifications = true;
        imageReceiver.setFileLoadingPriority(0);
        ImageReceiver imageReceiver2 = new ImageReceiver(this);
        this.reactionEffectImageReceiver = imageReceiver2;
        imageReceiver2.setAllowLoadingOnAttachedOnly(true);
        ImageReceiver imageReceiver3 = this.reactionEffectImageReceiver;
        imageReceiver3.ignoreNotifications = true;
        imageReceiver3.setFileLoadingPriority(3);
        ImageReceiver imageReceiver4 = new ImageReceiver(this);
        this.reactionMoveImageReceiver = imageReceiver4;
        imageReceiver4.setAllowLoadingOnAttachedOnly(true);
        ImageReceiver imageReceiver5 = this.reactionMoveImageReceiver;
        imageReceiver5.ignoreNotifications = true;
        imageReceiver5.setFileLoadingPriority(3);
        ImageReceiver imageReceiver6 = new ImageReceiver();
        this.leftPreloadImageReceiver = imageReceiver6;
        imageReceiver6.setAllowLoadingOnAttachedOnly(true);
        imageReceiver6.ignoreNotifications = true;
        imageReceiver6.setFileLoadingPriority(0);
        ImageReceiver imageReceiver7 = new ImageReceiver();
        this.rightPreloadImageReceiver = imageReceiver7;
        imageReceiver7.setAllowLoadingOnAttachedOnly(true);
        imageReceiver7.ignoreNotifications = true;
        imageReceiver7.setFileLoadingPriority(0);
        imageReceiver.setPreloadingReceivers(Arrays.asList(imageReceiver6, imageReceiver7));
        this.avatarDrawable = new AvatarDrawable();
        this.storyViewer = storyViewer;
        this.sharedResources = sharedResources;
        this.bitmapShaderTools = sharedResources.bitmapShaderTools;
        this.storiesController = MessagesController.getInstance(UserConfig.selectedAccount).getStoriesController();
        sharedResources.dimPaint.setColor(-16777216);
        this.inputBackgroundPaint = new Paint(1);
        Paint paint = new Paint(1);
        this.inputTopBorderPaint = paint;
        Paint.Style style = Paint.Style.STROKE;
        paint.setStyle(style);
        this.inputTopBorderPaint.setColor(687865855);
        Paint paint2 = new Paint(1);
        this.inputBottomBorderPaint = paint2;
        paint2.setStyle(style);
        this.inputBottomBorderPaint.setColor(352321535);
        this.resourcesProvider = resourcesProvider;
        setClipChildren(false);
        this.storyAreasView = new StoryMediaAreasView(context, this.storyContainer, resourcesProvider) { // from class: org.telegram.ui.Stories.PeerStoriesView.3
            @Override // org.telegram.ui.Stories.StoryMediaAreasView
            protected void onHintVisible(boolean z) {
                Delegate delegate = PeerStoriesView.this.delegate;
                if (delegate != null) {
                    delegate.setIsHintVisible(z);
                }
            }

            @Override // org.telegram.ui.Stories.StoryMediaAreasView
            protected void presentFragment(BaseFragment baseFragment) {
                StoryViewer storyViewer2 = storyViewer;
                if (storyViewer2 != null) {
                    storyViewer2.presentFragment(baseFragment);
                }
            }

            @Override // org.telegram.ui.Stories.StoryMediaAreasView, android.view.View
            public boolean onTouchEvent(MotionEvent motionEvent) {
                return super.onTouchEvent(motionEvent);
            }

            @Override // android.view.ViewGroup, android.view.View
            public boolean dispatchTouchEvent(MotionEvent motionEvent) {
                return super.dispatchTouchEvent(motionEvent);
            }

            @Override // org.telegram.ui.Stories.StoryMediaAreasView
            public void showEffect(StoryReactionWidgetView storyReactionWidgetView) {
                PeerStoriesView peerStoriesView = PeerStoriesView.this;
                if (!peerStoriesView.isSelf && peerStoriesView.currentStory.storyItem != null) {
                    ReactionsLayoutInBubble.VisibleReaction visibleReactionFromTL = ReactionsLayoutInBubble.VisibleReaction.fromTL(storyReactionWidgetView.mediaArea.reaction);
                    if (!Objects.equals(visibleReactionFromTL, ReactionsLayoutInBubble.VisibleReaction.fromTL(PeerStoriesView.this.currentStory.storyItem.sent_reaction))) {
                        PeerStoriesView.this.likeStory(visibleReactionFromTL);
                    }
                }
                storyReactionWidgetView.performHapticFeedback(3);
                storyReactionWidgetView.playAnimation();
                PeerStoriesView.this.emojiAnimationsOverlay.showAnimationForWidget(storyReactionWidgetView);
            }

            @Override // org.telegram.ui.Stories.StoryMediaAreasView
            protected Bitmap getPlayingBitmap() {
                return PeerStoriesView.this.getPlayingBitmap();
            }
        };
        this.blurredBackgroundColorProvider = new BlurredBackgroundColorProviderThemed(resourcesProvider, Theme.key_chat_messagePanelBackground, 0.8f);
        BlurredBackgroundSourceColor blurredBackgroundSourceColor = new BlurredBackgroundSourceColor();
        this.blurredBackgroundSourceFallback = blurredBackgroundSourceColor;
        blurredBackgroundSourceColor.setColor(ColorUtils.blendARGB(-16777216, -1, 0.2f));
        if (Build.VERSION.SDK_INT >= 31 && SharedConfig.canBlurChat()) {
            BlurredBackgroundSourceRenderNode blurredBackgroundSourceRenderNode = new BlurredBackgroundSourceRenderNode(blurredBackgroundSourceColor);
            this.blurredBackgroundSourceRenderNodeWithSaturation = blurredBackgroundSourceRenderNode;
            blurredBackgroundSourceRenderNode.setBlur(AndroidUtilities.dp(8.0f));
            this.blurredBackgroundSourceWithSaturation = blurredBackgroundSourceRenderNode;
        } else {
            this.blurredBackgroundSourceRenderNodeWithSaturation = null;
            this.blurredBackgroundSourceWithSaturation = blurredBackgroundSourceColor;
        }
        BlurredBackgroundDrawableViewFactory blurredBackgroundDrawableViewFactory = new BlurredBackgroundDrawableViewFactory(new ViewPositionWatcher(this), this, this.blurredBackgroundSourceWithSaturation);
        this.blurredBackgroundDrawableFactory = blurredBackgroundDrawableViewFactory;
        this.inputFieldBackground = blurredBackgroundDrawableViewFactory.create(this, this.blurredBackgroundColorProvider);
        BlurredBackgroundDrawable blurredBackgroundDrawableCreate = this.blurredBackgroundDrawableFactory.create(this, this.blurredBackgroundColorProvider);
        this.emojiKeyboardBackground = blurredBackgroundDrawableCreate;
        blurredBackgroundDrawableCreate.setThickness(AndroidUtilities.dp(32.0f));
        AnonymousClass4 anonymousClass4 = new AnonymousClass4(context, sharedResources, storyViewer);
        this.storyContainer = anonymousClass4;
        anonymousClass4.setClipChildren(false);
        this.emojiAnimationsOverlay = new EmojiAnimationsOverlay(this.storyContainer, this.currentAccount);
        this.storyContainer.addView(this.storyAreasView, LayoutHelper.createFrame(-1, -1.0f));
        AnonymousClass5 anonymousClass5 = new AnonymousClass5(getContext(), storyViewer.resourcesProvider, storyViewer, resourcesProvider);
        this.storyCaptionView = anonymousClass5;
        anonymousClass5.captionTextview.setOnClickListener(new View.OnClickListener() { // from class: org.telegram.ui.Stories.PeerStoriesView$$ExternalSyntheticLambda11
            @Override // android.view.View.OnClickListener
            public final void onClick(View view) {
                this.f$0.lambda$new$0(view);
            }
        });
        ImageView imageView = new ImageView(context);
        this.shareButton = imageView;
        imageView.setImageDrawable(sharedResources.shareDrawable);
        int iDp = AndroidUtilities.dp(8.0f);
        imageView.setPadding(iDp, iDp, iDp, iDp);
        imageView.setOnClickListener(new View.OnClickListener() { // from class: org.telegram.ui.Stories.PeerStoriesView$$ExternalSyntheticLambda12
            @Override // android.view.View.OnClickListener
            public final void onClick(View view) {
                this.f$0.lambda$new$1(view);
            }
        });
        ScaleStateListAnimator.apply(imageView);
        if (!DISABLE_STORY_REPOSTING) {
            ImageView imageView2 = new ImageView(context);
            this.repostButton = imageView2;
            imageView2.setImageDrawable(sharedResources.repostDrawable);
            this.repostButton.setPadding(iDp, iDp, iDp, iDp);
            FrameLayout frameLayout = new FrameLayout(getContext()) { // from class: org.telegram.ui.Stories.PeerStoriesView.6
                @Override // android.view.ViewGroup, android.view.View
                protected void dispatchDraw(Canvas canvas) {
                    super.dispatchDraw(canvas);
                    PeerStoriesView peerStoriesView = PeerStoriesView.this;
                    if (!peerStoriesView.isChannel || peerStoriesView.repostCounter == null) {
                        return;
                    }
                    canvas.save();
                    canvas.translate((getMeasuredWidth() - PeerStoriesView.this.repostCounter.getCurrentWidth()) - AndroidUtilities.dp(6.0f), 0.0f);
                    float f = PeerStoriesView.this.repostCounterProgress.set(PeerStoriesView.this.repostCounterVisible ? 1.0f : 0.0f);
                    canvas.scale(f, f, PeerStoriesView.this.repostCounter.getCurrentWidth() / 2.0f, AndroidUtilities.dp(20.0f));
                    PeerStoriesView.this.repostCounter.setAlpha(255);
                    PeerStoriesView.this.repostCounter.draw(canvas);
                    canvas.restore();
                }

                @Override // android.view.View
                protected boolean verifyDrawable(Drawable drawable) {
                    return drawable == PeerStoriesView.this.repostCounter || super.verifyDrawable(drawable);
                }
            };
            this.repostButtonContainer = frameLayout;
            AnimatedTextView.AnimatedTextDrawable animatedTextDrawable = this.repostCounter;
            if (animatedTextDrawable != null) {
                animatedTextDrawable.setCallback(frameLayout);
            }
            this.repostButtonContainer.setWillNotDraw(false);
            this.repostButtonContainer.setOnClickListener(new View.OnClickListener() { // from class: org.telegram.ui.Stories.PeerStoriesView$$ExternalSyntheticLambda13
                @Override // android.view.View.OnClickListener
                public final void onClick(View view) {
                    this.f$0.lambda$new$2(view);
                }
            });
        }
        FrameLayout frameLayout2 = new FrameLayout(getContext()) { // from class: org.telegram.ui.Stories.PeerStoriesView.7
            @Override // android.view.ViewGroup, android.view.View
            protected void dispatchDraw(Canvas canvas) {
                super.dispatchDraw(canvas);
                PeerStoriesView peerStoriesView = PeerStoriesView.this;
                if (!peerStoriesView.isChannel || peerStoriesView.reactionsCounter == null) {
                    return;
                }
                canvas.save();
                canvas.translate((getMeasuredWidth() - PeerStoriesView.this.reactionsCounter.getCurrentWidth()) - AndroidUtilities.dp(6.0f), 0.0f);
                float f = PeerStoriesView.this.reactionsCounterProgress.set(PeerStoriesView.this.reactionsCounterVisible ? 1.0f : 0.0f);
                canvas.scale(f, f, PeerStoriesView.this.reactionsCounter.getCurrentWidth() / 2.0f, AndroidUtilities.dp(20.0f));
                PeerStoriesView.this.reactionsCounter.setAlpha(255);
                PeerStoriesView.this.reactionsCounter.draw(canvas);
                canvas.restore();
            }

            @Override // android.view.View
            protected boolean verifyDrawable(Drawable drawable) {
                return drawable == PeerStoriesView.this.reactionsCounter || super.verifyDrawable(drawable);
            }
        };
        this.likeButtonContainer = frameLayout2;
        AnimatedTextView.AnimatedTextDrawable animatedTextDrawable2 = this.reactionsCounter;
        if (animatedTextDrawable2 != null) {
            animatedTextDrawable2.setCallback(frameLayout2);
        }
        frameLayout2.setWillNotDraw(false);
        frameLayout2.setOnClickListener(new View.OnClickListener() { // from class: org.telegram.ui.Stories.PeerStoriesView$$ExternalSyntheticLambda14
            @Override // android.view.View.OnClickListener
            public final void onClick(View view) {
                this.f$0.lambda$new$4(view);
            }
        });
        frameLayout2.setOnLongClickListener(new View.OnLongClickListener() { // from class: org.telegram.ui.Stories.PeerStoriesView$$ExternalSyntheticLambda15
            @Override // android.view.View.OnLongClickListener
            public final boolean onLongClick(View view) {
                return this.f$0.lambda$new$5(storyViewer, view);
            }
        });
        StoriesLikeButton storiesLikeButton = new StoriesLikeButton(context, sharedResources);
        this.storiesLikeButton = storiesLikeButton;
        storiesLikeButton.setPadding(iDp, iDp, iDp, iDp);
        frameLayout2.addView(this.storiesLikeButton, LayoutHelper.createFrame(40, 40, 3));
        FrameLayout frameLayout3 = this.repostButtonContainer;
        if (frameLayout3 != null) {
            frameLayout3.addView(this.repostButton, LayoutHelper.createFrame(40, 40, 3));
        }
        ScaleStateListAnimator.apply(frameLayout2, 0.3f, 5.0f);
        FrameLayout frameLayout4 = this.repostButtonContainer;
        if (frameLayout4 != null) {
            ScaleStateListAnimator.apply(frameLayout4, 0.3f, 5.0f);
        }
        imageReceiver.setAllowLoadingOnAttachedOnly(true);
        imageReceiver.setParentView(this.storyContainer);
        RoundRectOutlineProvider roundRectOutlineProvider = new RoundRectOutlineProvider(10);
        this.outlineProvider = roundRectOutlineProvider;
        this.storyContainer.setOutlineProvider(roundRectOutlineProvider);
        this.storyContainer.setClipToOutline(true);
        addView(this.storyContainer);
        PeerHeaderView peerHeaderView = new PeerHeaderView(context, storyItemHolder);
        this.headerView = peerHeaderView;
        peerHeaderView.setOnClickListener(new View.OnClickListener() { // from class: org.telegram.ui.Stories.PeerStoriesView$$ExternalSyntheticLambda16
            @Override // android.view.View.OnClickListener
            public final void onClick(View view) {
                this.f$0.lambda$new$6(storyViewer, view);
            }
        });
        this.storyContainer.addView(this.headerView, LayoutHelper.createFrame(-1, -2.0f, 0, 0.0f, 17.0f, 0.0f, 0.0f));
        this.topBulletinContainer = new FrameLayout(context);
        LayoutTransition layoutTransition = new LayoutTransition();
        layoutTransition.setDuration(150L);
        layoutTransition.disableTransitionType(2);
        layoutTransition.enableTransitionType(4);
        LinearLayout linearLayout = new LinearLayout(context);
        this.bottomActionsLinearLayout = linearLayout;
        linearLayout.setOrientation(0);
        linearLayout.setLayoutTransition(layoutTransition);
        linearLayout.addView(imageView, LayoutHelper.createLinear(40, 40, 5));
        FrameLayout frameLayout5 = this.repostButtonContainer;
        if (frameLayout5 != null) {
            linearLayout.addView(frameLayout5, LayoutHelper.createLinear(40, 40, 5));
        }
        linearLayout.addView(frameLayout2, LayoutHelper.createLinear(40, 40, 5));
        addView(linearLayout, LayoutHelper.createFrame(-2, -2.0f, 5, 0.0f, 0.0f, 4.0f, 0.0f));
        ImageView imageView3 = new ImageView(context);
        this.optionsIconView = imageView3;
        imageView3.setImageDrawable(sharedResources.optionsDrawable);
        imageView3.setPadding(AndroidUtilities.dp(8.0f), AndroidUtilities.dp(8.0f), AndroidUtilities.dp(8.0f), AndroidUtilities.dp(8.0f));
        imageView3.setBackground(Theme.createSelectorDrawable(-1));
        this.storyContainer.addView(imageView3, LayoutHelper.createFrame(40, 40.0f, 53, 2.0f, 15.0f, 2.0f, 0.0f));
        ImageView imageView4 = new ImageView(context);
        this.pipIconView = imageView4;
        imageView4.setImageDrawable(sharedResources.pipDrawable);
        imageView4.setPadding(AndroidUtilities.dp(8.0f), AndroidUtilities.dp(8.0f), AndroidUtilities.dp(8.0f), AndroidUtilities.dp(8.0f));
        imageView4.setBackground(Theme.createSelectorDrawable(-1));
        this.storyContainer.addView(imageView4, LayoutHelper.createFrame(40, 40.0f, 53, 2.0f, 15.0f, 42.0f, 0.0f));
        imageView4.setOnClickListener(new View.OnClickListener() { // from class: org.telegram.ui.Stories.PeerStoriesView$$ExternalSyntheticLambda17
            @Override // android.view.View.OnClickListener
            public final void onClick(View view) {
                PeerStoriesView.$r8$lambda$yJHJR1bijBsghFqqIA0QqbKSXoo(storyViewer, view);
            }
        });
        imageView3.setOnClickListener(new View.OnClickListener() { // from class: org.telegram.ui.Stories.PeerStoriesView$$ExternalSyntheticLambda18
            @Override // android.view.View.OnClickListener
            public final void onClick(View view) {
                this.f$0.lambda$new$8(resourcesProvider, storyViewer, context, sharedResources, view);
            }
        });
        FrameLayout frameLayout6 = new FrameLayout(context) { // from class: org.telegram.ui.Stories.PeerStoriesView.9
            @Override // android.view.View
            public boolean onTouchEvent(MotionEvent motionEvent) {
                return super.onTouchEvent(motionEvent);
            }

            @Override // android.view.ViewGroup, android.view.View
            public boolean dispatchTouchEvent(MotionEvent motionEvent) {
                return super.dispatchTouchEvent(motionEvent);
            }
        };
        this.muteIconContainer = frameLayout6;
        this.storyContainer.addView(frameLayout6, LayoutHelper.createFrame(40, 40.0f, 53, 2.0f, 15.0f, 42.0f, 0.0f));
        RLottieImageView rLottieImageView = new RLottieImageView(context);
        this.muteIconView = rLottieImageView;
        rLottieImageView.setPadding(AndroidUtilities.dp(6.0f), AndroidUtilities.dp(6.0f), AndroidUtilities.dp(6.0f), AndroidUtilities.dp(6.0f));
        frameLayout6.addView(rLottieImageView);
        ImageView imageView5 = new ImageView(context);
        this.noSoundIconView = imageView5;
        imageView5.setPadding(AndroidUtilities.dp(6.0f), AndroidUtilities.dp(6.0f), AndroidUtilities.dp(6.0f), AndroidUtilities.dp(6.0f));
        imageView5.setImageDrawable(sharedResources.noSoundDrawable);
        frameLayout6.addView(imageView5);
        imageView5.setVisibility(8);
        StoryPrivacyButton storyPrivacyButton = new StoryPrivacyButton(context);
        this.privacyButton = storyPrivacyButton;
        storyPrivacyButton.setOnClickListener(new View.OnClickListener() { // from class: org.telegram.ui.Stories.PeerStoriesView$$ExternalSyntheticLambda8
            @Override // android.view.View.OnClickListener
            public final void onClick(View view) {
                this.f$0.lambda$new$10(view);
            }
        });
        this.storyContainer.addView(storyPrivacyButton, LayoutHelper.createFrame(60, 40.0f, 53, 2.0f, 15.0f, 42.0f, 0.0f));
        frameLayout6.setOnClickListener(new View.OnClickListener() { // from class: org.telegram.ui.Stories.PeerStoriesView$$ExternalSyntheticLambda9
            @Override // android.view.View.OnClickListener
            public final void onClick(View view) {
                this.f$0.lambda$new$11(storyViewer, view);
            }
        });
        this.storyLines = new StoryLinesDrawable(this, sharedResources);
        this.storyContainer.addView(anonymousClass5, LayoutHelper.createFrame(-1, -1.0f, 0, 0.0f, 64.0f, 0.0f, 0.0f));
        View view = new View(context);
        this.liveCommentsShadowView = view;
        view.setBackground(new GradientDrawable(GradientDrawable.Orientation.TOP_BOTTOM, new int[]{0, -16777216}));
        LiveCommentsView liveCommentsView = new LiveCommentsView(context, storyViewer, storyViewer.containerView, view, this.topBulletinContainer) { // from class: org.telegram.ui.Stories.PeerStoriesView.10
            @Override // android.view.View
            public void setVisibility(int i) {
                super.setVisibility(i);
                PeerStoriesView.this.liveCommentsShadowView.setVisibility(i);
            }

            @Override // org.telegram.ui.Stories.LiveCommentsView
            protected TLRPC.Peer getDefaultSendAs() {
                LivePlayer livePlayer = storyViewer.livePlayer;
                if (livePlayer != null) {
                    return livePlayer.getDefaultSendAs();
                }
                return null;
            }

            @Override // org.telegram.ui.Stories.LiveCommentsView
            protected boolean isMe(long j) {
                if (j == UserConfig.getInstance(PeerStoriesView.this.currentAccount).getClientUserId()) {
                    return true;
                }
                LivePlayer livePlayer = storyViewer.livePlayer;
                if (livePlayer != null && j == DialogObject.getPeerDialogId(livePlayer.getDefaultSendAs())) {
                    return true;
                }
                if (PeerStoriesView.this.sendAsPeersObj != null) {
                    for (int i = 0; i < PeerStoriesView.this.sendAsPeersObj.peers.size(); i++) {
                        if (j == DialogObject.getPeerDialogId(((TLRPC.TL_sendAsPeer) PeerStoriesView.this.sendAsPeersObj.peers.get(i)).peer)) {
                            return true;
                        }
                    }
                }
                return false;
            }

            @Override // org.telegram.ui.Stories.LiveCommentsView
            public void setCollapsed(boolean z, boolean z2) {
                super.setCollapsed(z, z2);
                if (PeerStoriesView.this.commentButton != null) {
                    PeerStoriesView.this.commentButton.setCollapsed(z, z2);
                }
            }

            @Override // org.telegram.ui.Stories.LiveCommentsView
            protected void onMessagesCountUpdated() {
                if (PeerStoriesView.this.commentButton != null) {
                    PeerStoriesView.this.commentButton.setCount(getUnreadMessagesCount());
                }
            }

            @Override // org.telegram.ui.Stories.LiveCommentsView
            protected void onStarsCountUpdated() {
                PeerStoriesView.this.starsButton.setCount((int) getStarsCount());
                PeerStoriesView.this.starsButton.setFilled(areSendingStars());
            }

            @Override // org.telegram.ui.Stories.LiveCommentsView
            protected void onStarsButtonPressed(long j, boolean z) {
                if (z) {
                    PeerStoriesView.this.starsButton.playEffect(j);
                } else {
                    PeerStoriesView.this.starsButton.stopEffects();
                }
            }

            @Override // org.telegram.ui.Stories.LiveCommentsView
            protected void onStarReaction(long j, int i, int i2) {
                if (PeerStoriesView.this.starsButtonEffectsView == null) {
                    return;
                }
                PeerStoriesView.this.starsButtonEffectsView.pushChip(j, i, i2);
            }

            @Override // org.telegram.ui.Stories.LiveCommentsView
            protected void onCancelledStarReaction(long j) {
                if (PeerStoriesView.this.starsButtonEffectsView == null) {
                    return;
                }
                PeerStoriesView.this.starsButtonEffectsView.removeChipsFrom(j);
            }

            @Override // org.telegram.ui.Stories.LiveCommentsView
            protected void onStarsButtonCancelled() {
                PeerStoriesView.this.starsButton.stopEffects();
            }
        };
        this.liveCommentsView = liveCommentsView;
        this.storyContainer.addView(view, LayoutHelper.createFrame(-1, 200, 87));
        this.storyContainer.addView(liveCommentsView, LayoutHelper.createFrame(-1, -1.0f, 0, 0.0f, 64.0f, 0.0f, 0.0f));
        this.storyContainer.addView(this.topBulletinContainer, LayoutHelper.createFrame(-1, 100.0f, 0, 0.0f, 55.0f, 0.0f, 0.0f));
        frameLayout6.setBackground(Theme.createSimpleSelectorRoundRectDrawable(AndroidUtilities.dp(20.0f), 0, ColorUtils.setAlphaComponent(-1, 100)));
        imageView3.setBackground(Theme.createSimpleSelectorRoundRectDrawable(AndroidUtilities.dp(20.0f), 0, ColorUtils.setAlphaComponent(-1, 100)));
        imageView4.setBackground(Theme.createSimpleSelectorRoundRectDrawable(AndroidUtilities.dp(20.0f), 0, ColorUtils.setAlphaComponent(-1, 100)));
        imageView.setBackground(Theme.createSimpleSelectorRoundRectDrawable(AndroidUtilities.dp(20.0f), 0, ColorUtils.setAlphaComponent(-1, 100)));
        frameLayout2.setBackground(Theme.createSimpleSelectorRoundRectDrawable(AndroidUtilities.dp(20.0f), 0, ColorUtils.setAlphaComponent(-1, 100)));
        FrameLayout frameLayout7 = this.repostButtonContainer;
        if (frameLayout7 != null) {
            frameLayout7.setBackground(Theme.createSimpleSelectorRoundRectDrawable(AndroidUtilities.dp(20.0f), 0, ColorUtils.setAlphaComponent(-1, 100)));
        }
        View overlayView = anonymousClass5.textSelectionHelper.getOverlayView(context);
        if (overlayView != null) {
            AndroidUtilities.removeFromParent(overlayView);
            addView(overlayView);
        }
        anonymousClass5.textSelectionHelper.setCallback(new TextSelectionHelper.Callback() { // from class: org.telegram.ui.Stories.PeerStoriesView.11
            @Override // org.telegram.ui.Cells.TextSelectionHelper.Callback
            public void onStateChanged(boolean z) {
                PeerStoriesView peerStoriesView = PeerStoriesView.this;
                peerStoriesView.delegate.setIsInSelectionMode(peerStoriesView.storyCaptionView.textSelectionHelper.isInSelectionMode());
            }
        });
        anonymousClass5.textSelectionHelper.setParentView(this);
    }

    /* JADX INFO: renamed from: org.telegram.ui.Stories.PeerStoriesView$4, reason: invalid class name */
    class AnonymousClass4 extends HwFrameLayout {
        boolean drawOverlayed;
        final CellFlickerDrawable loadingDrawable;
        final AnimatedFloat loadingDrawableAlpha;
        final AnimatedFloat loadingDrawableAlpha2;
        final AnimatedFloat progressToAudio;
        final AnimatedFloat progressToFullBlackoutA;
        boolean splitDrawing;
        final /* synthetic */ SharedResources val$sharedResources;
        final /* synthetic */ StoryViewer val$storyViewer;

        /* JADX WARN: 'super' call moved to the top of the method (can break code semantics) */
        AnonymousClass4(Context context, SharedResources sharedResources, StoryViewer storyViewer) {
            super(context);
            this.val$sharedResources = sharedResources;
            this.val$storyViewer = storyViewer;
            CubicBezierInterpolator cubicBezierInterpolator = CubicBezierInterpolator.DEFAULT;
            this.progressToAudio = new AnimatedFloat(this, 150L, cubicBezierInterpolator);
            this.progressToFullBlackoutA = new AnimatedFloat(this, 150L, cubicBezierInterpolator);
            this.loadingDrawable = new CellFlickerDrawable(32, 102, 240);
            AnimatedFloat animatedFloat = new AnimatedFloat(this);
            this.loadingDrawableAlpha2 = animatedFloat;
            AnimatedFloat animatedFloat2 = new AnimatedFloat(this);
            this.loadingDrawableAlpha = animatedFloat2;
            animatedFloat.setDuration(500L);
            animatedFloat2.setDuration(100L);
        }

        /* JADX WARN: Code duplicated, block: B:210:0x0606  */
        /* JADX WARN: Code duplicated, block: B:74:0x01c6  */
        /* JADX WARN: Code duplicated, block: B:75:0x01c8  */
        @Override // android.view.ViewGroup, android.view.View
        protected void dispatchDraw(Canvas canvas) {
            float f;
            boolean z;
            boolean zHasNotThumb;
            StoryViewer.VideoPlayerHolder videoPlayerHolder;
            StoryViewer.VideoPlayerHolder videoPlayerHolder2;
            Canvas canvas2 = canvas;
            PeerStoriesView peerStoriesView = PeerStoriesView.this;
            if (!peerStoriesView.isActive) {
                peerStoriesView.headerView.backupImageView.getImageReceiver().setVisible(true, true);
            }
            PeerStoriesView peerStoriesView2 = PeerStoriesView.this;
            if (!peerStoriesView2.unsupported) {
                if (peerStoriesView2.playerSharedScope.renderView != null || (peerStoriesView2.storyAreasView != null && (PeerStoriesView.this.storyAreasView.hasSelectedForScale() || PeerStoriesView.this.storyAreasView.parentHighlightScaleAlpha.isInProgress()))) {
                    invalidate();
                }
                canvas2.save();
                PeerStoriesView.this.pinchToZoomHelper.applyTransform(canvas2);
                PeerStoriesView peerStoriesView3 = PeerStoriesView.this;
                VideoPlayerSharedScope videoPlayerSharedScope = peerStoriesView3.playerSharedScope;
                View view = videoPlayerSharedScope.renderView;
                if (view != null && (videoPlayerSharedScope.firstFrameRendered || videoPlayerSharedScope.livePlayer != null)) {
                    if (!peerStoriesView3.imageReceiver.hasBitmapImage()) {
                        this.val$sharedResources.imageBackgroundDrawable.setBounds(0, 0, getMeasuredWidth(), getMeasuredHeight() + 1);
                        this.val$sharedResources.imageBackgroundDrawable.draw(canvas2);
                    }
                    PeerStoriesView.this.imageReceiver.setImageCoords(0.0f, 0.0f, getMeasuredWidth(), getMeasuredHeight() + 1);
                    PeerStoriesView.this.imageReceiver.draw(canvas2);
                    PeerStoriesView peerStoriesView4 = PeerStoriesView.this;
                    if (peerStoriesView4.isActive) {
                        if (this.val$storyViewer.USE_SURFACE_VIEW && (videoPlayerHolder2 = peerStoriesView4.playerSharedScope.player) != null && videoPlayerHolder2.paused && videoPlayerHolder2.playerStubBitmap != null && videoPlayerHolder2.stubAvailable) {
                            float measuredWidth = getMeasuredWidth() / PeerStoriesView.this.playerSharedScope.player.playerStubBitmap.getWidth();
                            float measuredHeight = getMeasuredHeight() / PeerStoriesView.this.playerSharedScope.player.playerStubBitmap.getHeight();
                            canvas2.save();
                            canvas2.scale(measuredWidth, measuredHeight);
                            StoryViewer.VideoPlayerHolder videoPlayerHolder3 = PeerStoriesView.this.playerSharedScope.player;
                            canvas2.drawBitmap(videoPlayerHolder3.playerStubBitmap, 0.0f, 0.0f, videoPlayerHolder3.playerStubPaint);
                            canvas2.restore();
                        } else {
                            boolean z2 = Build.VERSION.SDK_INT >= 29 && peerStoriesView4.blurredBackgroundSourceRenderNodeWithSaturation != null && PeerStoriesView.this.blurredBackgroundSourceRenderNodeWithSaturation.isRecordingCanvas(canvas2);
                            if (!this.val$storyViewer.USE_SURFACE_VIEW || (PeerStoriesView.this.allowDrawSurface && this.val$storyViewer.isShown() && !z2)) {
                                PeerStoriesView.this.playerSharedScope.renderView.draw(canvas2);
                            }
                        }
                    }
                } else {
                    if (view != null) {
                        invalidate();
                    }
                    PeerStoriesView peerStoriesView5 = PeerStoriesView.this;
                    if (peerStoriesView5.currentStory.skipped) {
                        canvas2.drawColor(ColorUtils.blendARGB(-16777216, -1, 0.2f));
                    } else {
                        if (!peerStoriesView5.imageReceiver.hasBitmapImage()) {
                            this.val$sharedResources.imageBackgroundDrawable.setBounds(0, 0, getMeasuredWidth(), getMeasuredHeight() + 1);
                            this.val$sharedResources.imageBackgroundDrawable.draw(canvas2);
                        }
                        PeerStoriesView.this.imageReceiver.setImageCoords(0.0f, 0.0f, getMeasuredWidth(), getMeasuredHeight() + 1);
                        PeerStoriesView.this.imageReceiver.draw(canvas2);
                    }
                }
                canvas2.restore();
                if (PeerStoriesView.this.imageChanged) {
                    this.loadingDrawableAlpha2.set(0.0f, true);
                    this.loadingDrawableAlpha.set(0.0f, true);
                }
                if (PeerStoriesView.this.currentStory.isLive) {
                    VideoPlayerSharedScope videoPlayerSharedScope2 = PeerStoriesView.this.playerSharedScope;
                    LivePlayer livePlayer = videoPlayerSharedScope2.livePlayer;
                    if (livePlayer == null || !(videoPlayerSharedScope2.firstFrameRendered || livePlayer.isEmptyStream())) {
                        zHasNotThumb = false;
                    } else {
                        zHasNotThumb = true;
                    }
                } else if (PeerStoriesView.this.currentStory.isVideo) {
                    VideoPlayerSharedScope videoPlayerSharedScope3 = PeerStoriesView.this.playerSharedScope;
                    if (videoPlayerSharedScope3.renderView == null || (videoPlayerHolder = videoPlayerSharedScope3.player) == null || !videoPlayerSharedScope3.firstFrameRendered || (videoPlayerHolder.progress == 0.0f && videoPlayerSharedScope3.isBuffering() && !PeerStoriesView.this.playerSharedScope.player.paused)) {
                        zHasNotThumb = false;
                    } else {
                        zHasNotThumb = true;
                    }
                } else {
                    zHasNotThumb = PeerStoriesView.this.imageReceiver.hasNotThumb();
                }
                AnimatedFloat animatedFloat = this.loadingDrawableAlpha2;
                PeerStoriesView peerStoriesView6 = PeerStoriesView.this;
                animatedFloat.set((peerStoriesView6.isActive && !zHasNotThumb && peerStoriesView6.currentStory.uploadingStory == null) ? 1.0f : 0.0f);
                this.loadingDrawableAlpha.set(this.loadingDrawableAlpha2.get() == 1.0f ? 1.0f : 0.0f);
                if (this.loadingDrawableAlpha.get() > 0.0f) {
                    RectF rectF = AndroidUtilities.rectTmp;
                    rectF.set(0.0f, 0.0f, getMeasuredWidth(), getMeasuredHeight());
                    this.loadingDrawable.setAlpha((int) (this.loadingDrawableAlpha.get() * 255.0f));
                    this.loadingDrawable.setParentWidth(getMeasuredWidth() * 2);
                    CellFlickerDrawable cellFlickerDrawable = this.loadingDrawable;
                    cellFlickerDrawable.animationSpeedScale = 1.3f;
                    cellFlickerDrawable.draw(canvas2, rectF, AndroidUtilities.dp(10.0f), this);
                }
                PeerStoriesView.this.imageChanged = false;
            } else {
                canvas2.drawColor(ColorUtils.blendARGB(-16777216, -1, 0.2f));
            }
            if (PeerStoriesView.this.storyCaptionView.getAlpha() > 0.0f) {
                if (PeerStoriesView.this.storyCaptionView.getAlpha() == 1.0f) {
                    canvas2.save();
                } else {
                    canvas2.saveLayerAlpha(0.0f, 0.0f, PeerStoriesView.this.storyCaptionView.getMeasuredWidth(), PeerStoriesView.this.storyCaptionView.getMeasuredHeight(), (int) (PeerStoriesView.this.storyCaptionView.getAlpha() * 255.0f), 31);
                }
                PeerStoriesView.this.storyAreasView.draw(canvas2);
                canvas2.restore();
            }
            if (!PeerStoriesView.this.lastNoThumb && PeerStoriesView.this.imageReceiver.hasNotThumb()) {
                PeerStoriesView.this.lastNoThumb = true;
                PeerStoriesView.this.invalidate();
            }
            float hideInterfaceAlpha = PeerStoriesView.this.getHideInterfaceAlpha();
            this.val$sharedResources.topOverlayGradient.setAlpha(255);
            this.val$sharedResources.topOverlayGradient.draw(canvas2);
            PeerStoriesView peerStoriesView7 = PeerStoriesView.this;
            if (!peerStoriesView7.isSelf && peerStoriesView7.BIG_SCREEN && PeerStoriesView.this.storyCaptionView.getVisibility() != 0) {
                f = 0.0f;
            } else if (PeerStoriesView.this.storyCaptionView.getVisibility() == 0) {
                int iDp = AndroidUtilities.dp(72.0f);
                int textTop = ((int) (PeerStoriesView.this.storyCaptionView.getTextTop() - AndroidUtilities.dp(24.0f))) + PeerStoriesView.this.storyCaptionView.getTop();
                int i = iDp + textTop;
                float measuredHeight2 = getMeasuredHeight() * 0.65f;
                boolean zHideCaptionWithInterface = PeerStoriesView.this.hideCaptionWithInterface();
                if ((measuredHeight2 - textTop) / AndroidUtilities.dp(60.0f) <= 0.0f || !PeerStoriesView.this.storyCaptionView.isTouched() || !PeerStoriesView.this.storyCaptionView.hasScroll()) {
                    PeerStoriesView peerStoriesView8 = PeerStoriesView.this;
                    if (peerStoriesView8.checkBlackoutMode) {
                        peerStoriesView8.checkBlackoutMode = false;
                        if ((measuredHeight2 - (((int) (peerStoriesView8.storyCaptionView.getMaxTop() - AndroidUtilities.dp(24.0f))) + PeerStoriesView.this.storyCaptionView.getTop())) / AndroidUtilities.dp(60.0f) > 0.0f) {
                            PeerStoriesView.this.inBlackoutMode = true;
                        }
                    } else if (peerStoriesView8.storyCaptionView.getProgressToBlackout() == 0.0f) {
                        PeerStoriesView.this.inBlackoutMode = false;
                    }
                } else if ((measuredHeight2 - (((int) (PeerStoriesView.this.storyCaptionView.getMaxTop() - AndroidUtilities.dp(24.0f))) + PeerStoriesView.this.storyCaptionView.getTop())) / AndroidUtilities.dp(60.0f) > 0.0f) {
                    PeerStoriesView.this.inBlackoutMode = true;
                }
                if (!zHideCaptionWithInterface) {
                    hideInterfaceAlpha = 1.0f;
                }
                f = this.progressToFullBlackoutA.set(PeerStoriesView.this.inBlackoutMode ? 1.0f : 0.0f);
                if (f > 0.0f) {
                    this.splitDrawing = true;
                    this.drawOverlayed = false;
                    super.dispatchDraw(canvas);
                    this.splitDrawing = false;
                    drawLines(canvas);
                    this.val$sharedResources.gradientBackgroundPaint.setColor(ColorUtils.setAlphaComponent(-16777216, (int) (153.0f * f * hideInterfaceAlpha)));
                    canvas2.drawPaint(this.val$sharedResources.gradientBackgroundPaint);
                }
                if (f < 1.0f && !PeerStoriesView.this.currentStory.isLive) {
                    canvas2.save();
                    float f2 = 1.0f - f;
                    this.val$sharedResources.gradientBackgroundPaint.setColor(ColorUtils.setAlphaComponent(-16777216, (int) (129.03f * f2 * hideInterfaceAlpha)));
                    this.val$sharedResources.bottomOverlayGradient.setAlpha((int) (f2 * 255.0f * hideInterfaceAlpha));
                    this.val$sharedResources.bottomOverlayGradient.setBounds(0, textTop, getMeasuredWidth(), i);
                    this.val$sharedResources.bottomOverlayGradient.draw(canvas2);
                    canvas2.drawRect(0.0f, i, getMeasuredWidth(), getMeasuredHeight(), this.val$sharedResources.gradientBackgroundPaint);
                    canvas.restore();
                }
                if (f <= 0.0f || PeerStoriesView.this.storyCaptionView.getAlpha() <= 0.0f) {
                    canvas2 = canvas;
                } else {
                    PeerStoriesView.this.storyCaptionView.disableDraw(false);
                    if (PeerStoriesView.this.storyCaptionView.getAlpha() != 1.0f) {
                        canvas2 = canvas;
                        canvas2.saveLayerAlpha(0.0f, 0.0f, getMeasuredWidth(), getMeasuredHeight(), (int) (PeerStoriesView.this.storyCaptionView.getAlpha() * 255.0f), 31);
                    } else {
                        canvas2 = canvas;
                        canvas2.save();
                    }
                    canvas2.translate(PeerStoriesView.this.storyCaptionView.getX(), PeerStoriesView.this.storyCaptionView.getY() - PeerStoriesView.this.storyCaptionView.getScrollY());
                    PeerStoriesView.this.storyCaptionView.draw(canvas2);
                    canvas2.restore();
                }
                PeerStoriesView.this.storyCaptionView.disableDraw(f > 0.0f);
                if (f > 0.0f) {
                    this.splitDrawing = true;
                    this.drawOverlayed = true;
                    super.dispatchDraw(canvas);
                    this.splitDrawing = false;
                }
            } else {
                if (!PeerStoriesView.this.currentStory.isLive) {
                    int iDp2 = AndroidUtilities.dp(PeerStoriesView.this.BIG_SCREEN ? 56.0f : 110.0f);
                    PeerStoriesView peerStoriesView9 = PeerStoriesView.this;
                    if ((peerStoriesView9.isSelf || !peerStoriesView9.BIG_SCREEN) && PeerStoriesView.this.storyCaptionView.getVisibility() == 0) {
                        iDp2 = (int) (iDp2 * 2.5f);
                    }
                    this.val$sharedResources.bottomOverlayGradient.setBounds(0, PeerStoriesView.this.storyContainer.getMeasuredHeight() - iDp2, getMeasuredWidth(), PeerStoriesView.this.storyContainer.getMeasuredHeight());
                    this.val$sharedResources.bottomOverlayGradient.setAlpha((int) (hideInterfaceAlpha * 255.0f));
                    this.val$sharedResources.bottomOverlayGradient.draw(canvas2);
                }
                f = 0.0f;
            }
            if (PeerStoriesView.this.viewsThumbAlpha != 0.0f && PeerStoriesView.this.viewsThumbImageReceiver != null) {
                Canvas canvas3 = canvas2;
                PeerStoriesView.this.viewsThumbImageReceiver.draw(canvas3, PeerStoriesView.this.viewsThumbAlpha, PeerStoriesView.this.viewsThumbScale, 0, 0, getMeasuredWidth(), getMeasuredHeight() + 1);
                canvas2 = canvas3;
            }
            this.progressToAudio.set(PeerStoriesView.this.isRecording ? 1.0f : 0.0f);
            PeerStoriesView peerStoriesView10 = PeerStoriesView.this;
            if (peerStoriesView10.isActive) {
                if (peerStoriesView10.storyCaptionView.getVisibility() == 0) {
                    PeerStoriesView peerStoriesView11 = PeerStoriesView.this;
                    if (peerStoriesView11.inBlackoutMode || peerStoriesView11.storyCaptionView.isTouched()) {
                        z = true;
                    } else {
                        z = false;
                    }
                } else {
                    z = false;
                }
                PeerStoriesView peerStoriesView12 = PeerStoriesView.this;
                peerStoriesView12.isCaptionPartVisible = peerStoriesView12.storyCaptionView.getVisibility() == 0 && PeerStoriesView.this.storyCaptionView.getProgressToBlackout() > 0.0f;
                PeerStoriesView.this.delegate.setIsCaption(z);
                PeerStoriesView peerStoriesView13 = PeerStoriesView.this;
                peerStoriesView13.delegate.setIsCaptionPartVisible(peerStoriesView13.isCaptionPartVisible);
            }
            if (f <= 0.0f) {
                super.dispatchDraw(canvas);
                drawLines(canvas);
            }
            if (PeerStoriesView.this.emojiAnimationsOverlay != null) {
                PeerStoriesView.this.emojiAnimationsOverlay.draw(canvas2);
            }
        }

        @Override // android.view.ViewGroup
        protected boolean drawChild(Canvas canvas, View view, long j) {
            if (view == PeerStoriesView.this.storyAreasView) {
                return true;
            }
            if (this.splitDrawing) {
                if (Bulletin.getVisibleBulletin() != null && view == Bulletin.getVisibleBulletin().getLayout()) {
                    if (this.drawOverlayed) {
                        return super.drawChild(canvas, view, j);
                    }
                    return true;
                }
                return super.drawChild(canvas, view, j);
            }
            return super.drawChild(canvas, view, j);
        }

        /* JADX WARN: Code duplicated, block: B:50:0x0117  */
        /* JADX WARN: Code duplicated, block: B:58:0x0138  */
        private void drawLines(Canvas canvas) {
            float fClamp;
            float f;
            StoryItemHolder storyItemHolder;
            StoryViewer storyViewer;
            StoryViewer.VideoPlayerHolder videoPlayerHolder;
            if (PeerStoriesView.this.imageReceiver.hasNotThumb() || ((PeerStoriesView.this.currentStory.isVideo && PeerStoriesView.this.playerSharedScope.firstFrameRendered) || (PeerStoriesView.this.currentStory.isLive && PeerStoriesView.this.playerSharedScope.firstFrameRendered))) {
                PeerStoriesView.this.currentStory.checkSendView();
            }
            float hideInterfaceAlpha = PeerStoriesView.this.getHideInterfaceAlpha();
            if (PeerStoriesView.this.currentStory.isVideo()) {
                PeerStoriesView peerStoriesView = PeerStoriesView.this;
                StoryViewer.VideoPlayerHolder videoPlayerHolder2 = peerStoriesView.playerSharedScope.player;
                if (videoPlayerHolder2 != null) {
                    fClamp = Utilities.clamp(videoPlayerHolder2.getPlaybackProgress(peerStoriesView.videoDuration), 1.0f, 0.0f);
                    PeerStoriesView peerStoriesView2 = PeerStoriesView.this;
                    if (peerStoriesView2.playerSharedScope.firstFrameRendered && peerStoriesView2.storyAreasView != null) {
                        PeerStoriesView.this.storyAreasView.shine();
                    }
                } else {
                    fClamp = 0.0f;
                }
                invalidate();
            } else if (PeerStoriesView.this.paused) {
                fClamp = Utilities.clamp(PeerStoriesView.this.currentImageTime / 10000.0f, 1.0f, 0.0f);
            } else {
                PeerStoriesView peerStoriesView3 = PeerStoriesView.this;
                if (peerStoriesView3.isActive && !peerStoriesView3.isUploading && !PeerStoriesView.this.isEditing && !PeerStoriesView.this.isFailed && PeerStoriesView.this.imageReceiver.hasNotThumb()) {
                    long jCurrentTimeMillis = System.currentTimeMillis();
                    if (PeerStoriesView.this.lastDrawTime != 0 && !PeerStoriesView.this.isCaptionPartVisible) {
                        if (PeerStoriesView.this.currentImageTime <= 0 && jCurrentTimeMillis - PeerStoriesView.this.lastDrawTime > 0 && PeerStoriesView.this.storyAreasView != null) {
                            PeerStoriesView.this.storyAreasView.shine();
                        }
                        PeerStoriesView.this.currentImageTime += jCurrentTimeMillis - PeerStoriesView.this.lastDrawTime;
                    }
                    PeerStoriesView.this.lastDrawTime = jCurrentTimeMillis;
                    fClamp = Utilities.clamp(PeerStoriesView.this.currentImageTime / 10000.0f, 1.0f, 0.0f);
                    invalidate();
                } else {
                    fClamp = Utilities.clamp(PeerStoriesView.this.currentImageTime / 10000.0f, 1.0f, 0.0f);
                }
            }
            float f2 = fClamp;
            PeerStoriesView peerStoriesView4 = PeerStoriesView.this;
            VideoPlayerSharedScope videoPlayerSharedScope = peerStoriesView4.playerSharedScope;
            if (videoPlayerSharedScope == null || (videoPlayerHolder = videoPlayerSharedScope.player) == null) {
                f = f2;
            } else {
                float f3 = videoPlayerHolder.currentSeek;
                if (f3 >= 0.0f) {
                    f = f3;
                } else {
                    f = f2;
                }
            }
            boolean z = true;
            if (!peerStoriesView4.switchEventSent && f2 == 1.0f && ((!PeerStoriesView.this.currentStory.isVideo || !PeerStoriesView.this.isCaptionPartVisible) && !PeerStoriesView.this.isLongPressed)) {
                PeerStoriesView.this.switchEventSent = true;
                post(new Runnable() { // from class: org.telegram.ui.Stories.PeerStoriesView$4$$ExternalSyntheticLambda0
                    @Override // java.lang.Runnable
                    public final void run() {
                        this.f$0.lambda$drawLines$0();
                    }
                });
            }
            StoriesController.StoriesList storiesList = this.val$storyViewer.storiesList;
            if (storiesList != null && storiesList.type != 3) {
                if (PeerStoriesView.this.storyPositionView == null) {
                    PeerStoriesView.this.storyPositionView = new StoryPositionView();
                }
                PeerStoriesView.this.storyPositionView.draw(canvas, (1.0f - PeerStoriesView.this.outT) * hideInterfaceAlpha * PeerStoriesView.this.alpha, PeerStoriesView.this.listPosition, this.val$storyViewer.storiesList.getCount(), this, PeerStoriesView.this.headerView);
            }
            canvas.save();
            canvas.translate(0.0f, AndroidUtilities.dp(8.0f) - (AndroidUtilities.dp(8.0f) * PeerStoriesView.this.outT));
            boolean z2 = PeerStoriesView.this.currentStory.isVideo() && PeerStoriesView.this.playerSharedScope.isBuffering();
            boolean z3 = PeerStoriesView.this.isLongPressed && (storyItemHolder = PeerStoriesView.this.currentStory) != null && storyItemHolder.isVideo && (storyViewer = this.val$storyViewer) != null && storyViewer.inSeekingMode;
            AnimatedFloat animatedFloat = PeerStoriesView.this.linesAlpha;
            if (PeerStoriesView.this.isLongPressed && !z3) {
                z = false;
            }
            PeerStoriesView.this.storyLines.draw(canvas, getMeasuredWidth(), PeerStoriesView.this.linesPosition, f2, PeerStoriesView.this.linesCount, animatedFloat.set(z), PeerStoriesView.this.alpha * (1.0f - PeerStoriesView.this.outT), z2, z3, f);
            canvas.restore();
        }

        /* JADX INFO: Access modifiers changed from: private */
        public /* synthetic */ void lambda$drawLines$0() {
            PeerStoriesView peerStoriesView = PeerStoriesView.this;
            if (peerStoriesView.delegate != null) {
                if (peerStoriesView.isUploading || PeerStoriesView.this.isEditing || PeerStoriesView.this.isFailed) {
                    if (PeerStoriesView.this.currentStory.isVideo()) {
                        PeerStoriesView.this.playerSharedScope.player.loopBack();
                        return;
                    } else {
                        PeerStoriesView.this.currentImageTime = 0L;
                        return;
                    }
                }
                PeerStoriesView.this.delegate.shouldSwitchToNext();
            }
        }

        @Override // android.view.ViewGroup, android.view.View
        protected void onAttachedToWindow() {
            super.onAttachedToWindow();
            PeerStoriesView.this.emojiAnimationsOverlay.onAttachedToWindow();
            Bulletin.addDelegate(this, new Bulletin.Delegate() { // from class: org.telegram.ui.Stories.PeerStoriesView.4.1
                @Override // org.telegram.ui.Components.Bulletin.Delegate
                public /* synthetic */ boolean allowLayoutChanges() {
                    return Bulletin.Delegate.CC.$default$allowLayoutChanges(this);
                }

                @Override // org.telegram.ui.Components.Bulletin.Delegate
                public /* synthetic */ boolean bottomOffsetAnimated() {
                    return Bulletin.Delegate.CC.$default$bottomOffsetAnimated(this);
                }

                @Override // org.telegram.ui.Components.Bulletin.Delegate
                public boolean clipWithGradient(int i) {
                    return i == 1 || i == 2 || i == 3;
                }

                @Override // org.telegram.ui.Components.Bulletin.Delegate
                public /* synthetic */ void onBottomOffsetChange(float f) {
                    Bulletin.Delegate.CC.$default$onBottomOffsetChange(this, f);
                }

                @Override // org.telegram.ui.Components.Bulletin.Delegate
                public int getTopOffset(int i) {
                    return AndroidUtilities.dp(58.0f);
                }

                @Override // org.telegram.ui.Components.Bulletin.Delegate
                public void onShow(Bulletin bulletin) {
                    Delegate delegate;
                    if (bulletin == null || bulletin.tag != 2 || (delegate = PeerStoriesView.this.delegate) == null) {
                        return;
                    }
                    delegate.setBulletinIsVisible(true);
                }

                @Override // org.telegram.ui.Components.Bulletin.Delegate
                public void onHide(Bulletin bulletin) {
                    Delegate delegate;
                    if (bulletin == null || bulletin.tag != 2 || (delegate = PeerStoriesView.this.delegate) == null) {
                        return;
                    }
                    delegate.setBulletinIsVisible(false);
                }

                @Override // org.telegram.ui.Components.Bulletin.Delegate
                public int getBottomOffset(int i) {
                    if (PeerStoriesView.this.BIG_SCREEN) {
                        return 0;
                    }
                    return AndroidUtilities.dp(64.0f);
                }
            });
        }

        @Override // android.view.ViewGroup, android.view.View
        protected void onDetachedFromWindow() {
            super.onDetachedFromWindow();
            PeerStoriesView.this.emojiAnimationsOverlay.onDetachedFromWindow();
            Bulletin.removeDelegate(this);
            Delegate delegate = PeerStoriesView.this.delegate;
            if (delegate != null) {
                delegate.setBulletinIsVisible(false);
            }
        }

        @Override // android.widget.FrameLayout, android.view.View
        protected void onMeasure(int i, int i2) {
            FrameLayout.LayoutParams layoutParams = (FrameLayout.LayoutParams) PeerStoriesView.this.muteIconContainer.getLayoutParams();
            if (PeerStoriesView.this.drawLinesAsCounter()) {
                layoutParams.rightMargin = AndroidUtilities.dp(2.0f);
                layoutParams.topMargin = AndroidUtilities.dp(55.0f);
            } else {
                layoutParams.rightMargin = AndroidUtilities.dp(42.0f);
                layoutParams.topMargin = AndroidUtilities.dp(15.0f);
            }
            super.onMeasure(i, i2);
        }

        @Override // android.view.ViewGroup, android.view.View
        public boolean dispatchTouchEvent(MotionEvent motionEvent) {
            LivePlayer livePlayer;
            PeerStoriesView peerStoriesView = PeerStoriesView.this;
            if (peerStoriesView.isActive && !peerStoriesView.unsupported) {
                VideoPlayerSharedScope videoPlayerSharedScope = peerStoriesView.playerSharedScope;
                if (videoPlayerSharedScope.renderView != null && videoPlayerSharedScope != null && (livePlayer = videoPlayerSharedScope.livePlayer) != null && livePlayer.isEmptyStream() && PeerStoriesView.this.playerSharedScope.renderView.dispatchTouchEvent(motionEvent)) {
                    return true;
                }
            }
            return super.dispatchTouchEvent(motionEvent);
        }
    }

    /* JADX INFO: renamed from: org.telegram.ui.Stories.PeerStoriesView$5, reason: invalid class name */
    class AnonymousClass5 extends StoryCaptionView {
        final /* synthetic */ Theme.ResourcesProvider val$resourcesProvider;
        final /* synthetic */ StoryViewer val$storyViewer;

        /* JADX WARN: 'super' call moved to the top of the method (can break code semantics) */
        AnonymousClass5(Context context, Theme.ResourcesProvider resourcesProvider, StoryViewer storyViewer, Theme.ResourcesProvider resourcesProvider2) {
            super(context, resourcesProvider);
            this.val$storyViewer = storyViewer;
            this.val$resourcesProvider = resourcesProvider2;
        }

        @Override // org.telegram.ui.Stories.StoryCaptionView, androidx.core.widget.NestedScrollView, android.view.View
        public boolean onTouchEvent(MotionEvent motionEvent) {
            return super.onTouchEvent(motionEvent);
        }

        @Override // android.view.ViewGroup, android.view.View
        public boolean dispatchTouchEvent(MotionEvent motionEvent) {
            return super.dispatchTouchEvent(motionEvent);
        }

        @Override // org.telegram.ui.Stories.StoryCaptionView
        public void onLinkClick(CharacterStyle characterStyle, View view) {
            if (characterStyle instanceof URLSpanUserMention) {
                TLRPC.User user = MessagesController.getInstance(PeerStoriesView.this.currentAccount).getUser(Utilities.parseLong(((URLSpanUserMention) characterStyle).getURL()));
                if (user != null) {
                    MessagesController.getInstance(PeerStoriesView.this.currentAccount).openChatOrProfileWith(user, null, this.val$storyViewer.fragment, 0, false);
                    return;
                }
                return;
            }
            if (characterStyle instanceof URLSpanNoUnderline) {
                String url = ((URLSpanNoUnderline) characterStyle).getURL();
                if (url != null && (url.startsWith("#") || url.startsWith("$"))) {
                    if (url.contains("@")) {
                        StoryViewer storyViewer = this.val$storyViewer;
                        if (storyViewer != null) {
                            storyViewer.presentFragment(new HashtagActivity(url));
                            return;
                        }
                        return;
                    }
                    Bundle bundle = new Bundle();
                    bundle.putInt("type", 3);
                    bundle.putString("hashtag", url);
                    StoryViewer storyViewer2 = this.val$storyViewer;
                    if (storyViewer2 != null) {
                        storyViewer2.presentFragment(new MediaActivity(bundle, null));
                        return;
                    }
                    return;
                }
                String strExtractUsername = Browser.extractUsername(url);
                if (strExtractUsername != null) {
                    String lowerCase = strExtractUsername.toLowerCase();
                    if (url.startsWith("@")) {
                        MessagesController.getInstance(PeerStoriesView.this.currentAccount).openByUserName(lowerCase, this.val$storyViewer.fragment, 0, null);
                        return;
                    } else {
                        processExternalUrl(0, url, characterStyle, false);
                        return;
                    }
                }
                processExternalUrl(0, url, characterStyle, false);
                return;
            }
            if (characterStyle instanceof URLSpan) {
                processExternalUrl(2, ((URLSpan) characterStyle).getURL(), characterStyle, characterStyle instanceof URLSpanReplacement);
                return;
            }
            if (characterStyle instanceof URLSpanMono) {
                ((URLSpanMono) characterStyle).copyToClipboard();
                BulletinFactory.of(PeerStoriesView.this.storyContainer, this.val$resourcesProvider).createCopyBulletin(LocaleController.getString(R.string.TextCopied)).show();
            } else if (characterStyle instanceof ClickableSpan) {
                ((ClickableSpan) characterStyle).onClick(view);
            }
        }

        /* JADX WARN: Code duplicated, block: B:30:0x006d  */
        private void processExternalUrl(int i, String str, CharacterStyle characterStyle, boolean z) {
            boolean z2;
            if (!z && !AndroidUtilities.shouldShowUrlInAlert(str)) {
                if (i == 0) {
                    Browser.openUrl(getContext(), Uri.parse(str), true, true, null);
                    return;
                } else if (i == 1) {
                    Browser.openUrl(getContext(), Uri.parse(str), false, false, null);
                    return;
                } else {
                    if (i == 2) {
                        Browser.openUrl(getContext(), Uri.parse(str), false, true, null);
                        return;
                    }
                    return;
                }
            }
            if (i != 0 && i != 2) {
                if (i == 1) {
                    AlertsCreator.showOpenUrlAlert(this.val$storyViewer.fragment, str, true, true, false, null, this.val$resourcesProvider);
                    return;
                }
                return;
            }
            if (characterStyle instanceof URLSpanReplacement) {
                URLSpanReplacement uRLSpanReplacement = (URLSpanReplacement) characterStyle;
                if (uRLSpanReplacement.getTextStyleRun() == null || (uRLSpanReplacement.getTextStyleRun().flags & 1024) == 0) {
                    z2 = false;
                } else {
                    z2 = true;
                }
            } else {
                z2 = false;
            }
            AlertsCreator.showOpenUrlAlert(this.val$storyViewer.fragment, str, true, true, true, z2, null, this.val$resourcesProvider);
        }

        @Override // org.telegram.ui.Stories.StoryCaptionView
        public void onLinkLongPress(final URLSpan uRLSpan, final View view, final Runnable runnable) {
            String strDecode;
            final String url = uRLSpan.getURL();
            String url2 = uRLSpan.getURL();
            try {
                try {
                    Uri uri = Uri.parse(url2);
                    url2 = Browser.replaceHostname(uri, Browser.IDN_toUnicode(uri.getHost()), null);
                } catch (Exception e) {
                    FileLog.e(e);
                    strDecode = url2;
                }
            } catch (Exception e2) {
                FileLog.e(e2);
            }
            strDecode = URLDecoder.decode(url2.replaceAll("\\+", "%2b"), "UTF-8");
            try {
                performHapticFeedback(VibratorUtils.getType(0), 1);
            } catch (Exception unused) {
            }
            BottomSheet.Builder builder = new BottomSheet.Builder(getContext(), false, this.val$resourcesProvider);
            builder.setTitle(strDecode);
            builder.setTitleMultipleLines(true);
            StoryItemHolder storyItemHolder = PeerStoriesView.this.currentStory;
            CharSequence[] charSequenceArr = (storyItemHolder == null || storyItemHolder.allowScreenshots()) ? new CharSequence[]{LocaleController.getString(R.string.Open), LocaleController.getString(R.string.Copy)} : new CharSequence[]{LocaleController.getString(R.string.Open)};
            final Theme.ResourcesProvider resourcesProvider = this.val$resourcesProvider;
            builder.setItems(charSequenceArr, new DialogInterface.OnClickListener() { // from class: org.telegram.ui.Stories.PeerStoriesView$5$$ExternalSyntheticLambda1
                @Override // android.content.DialogInterface.OnClickListener
                public final void onClick(DialogInterface dialogInterface, int i) {
                    this.f$0.lambda$onLinkLongPress$0(uRLSpan, view, url, resourcesProvider, dialogInterface, i);
                }
            });
            builder.setOnPreDismissListener(new DialogInterface.OnDismissListener() { // from class: org.telegram.ui.Stories.PeerStoriesView$5$$ExternalSyntheticLambda2
                @Override // android.content.DialogInterface.OnDismissListener
                public final void onDismiss(DialogInterface dialogInterface) {
                    runnable.run();
                }
            });
            BottomSheet bottomSheetCreate = builder.create();
            bottomSheetCreate.fixNavigationBar(Theme.getColor(Theme.key_dialogBackground, this.val$resourcesProvider));
            PeerStoriesView.this.delegate.showDialog(bottomSheetCreate);
        }

        /* JADX INFO: Access modifiers changed from: private */
        public /* synthetic */ void lambda$onLinkLongPress$0(URLSpan uRLSpan, View view, String str, Theme.ResourcesProvider resourcesProvider, DialogInterface dialogInterface, int i) {
            if (i == 0) {
                onLinkClick(uRLSpan, view);
            } else if (i == 1) {
                AndroidUtilities.addToClipboard(str);
                BulletinFactory.of(PeerStoriesView.this.storyContainer, resourcesProvider).createCopyLinkBulletin().show();
            }
        }

        @Override // org.telegram.ui.Stories.StoryCaptionView
        public void onReplyClick(final StoryCaptionView.Reply reply) {
            if (reply == null) {
                return;
            }
            if (reply.isRepostMessage && reply.peerId != null && reply.messageId != null) {
                Bundle bundle = new Bundle();
                if (reply.peerId.longValue() >= 0) {
                    bundle.putLong("user_id", reply.peerId.longValue());
                } else {
                    bundle.putLong("chat_id", -reply.peerId.longValue());
                }
                bundle.putInt("message_id", reply.messageId.intValue());
                this.val$storyViewer.presentFragment(new ChatActivity(bundle));
                return;
            }
            if (reply.peerId == null || reply.storyId == null) {
                BulletinFactory.of(PeerStoriesView.this.storyContainer, this.val$resourcesProvider).createSimpleBulletin(R.raw.error, LocaleController.getString(R.string.StoryHidAccount)).setTag(3).show(true);
                return;
            }
            StoriesController storiesController = MessagesController.getInstance(PeerStoriesView.this.currentAccount).getStoriesController();
            long jLongValue = reply.peerId.longValue();
            int iIntValue = reply.storyId.intValue();
            final StoryViewer storyViewer = this.val$storyViewer;
            final Theme.ResourcesProvider resourcesProvider = this.val$resourcesProvider;
            storiesController.resolveStoryLink(jLongValue, iIntValue, new Consumer() { // from class: org.telegram.ui.Stories.PeerStoriesView$5$$ExternalSyntheticLambda3
                @Override // com.google.android.exoplayer2.util.Consumer
                public final void accept(Object obj) {
                    this.f$0.lambda$onReplyClick$3(reply, storyViewer, resourcesProvider, (TL_stories.StoryItem) obj);
                }
            });
        }

        /* JADX INFO: Access modifiers changed from: private */
        public /* synthetic */ void lambda$onReplyClick$3(StoryCaptionView.Reply reply, final StoryViewer storyViewer, Theme.ResourcesProvider resourcesProvider, TL_stories.StoryItem storyItem) {
            if (storyItem != null) {
                BaseFragment lastFragment = LaunchActivity.getLastFragment();
                if (lastFragment == null) {
                    return;
                }
                storyItem.dialogId = reply.peerId.longValue();
                StoryViewer storyViewerCreateOverlayStoryViewer = lastFragment.createOverlayStoryViewer();
                storyViewerCreateOverlayStoryViewer.open(getContext(), storyItem, (StoryViewer.PlaceProvider) null);
                storyViewerCreateOverlayStoryViewer.setOnCloseListener(new Runnable() { // from class: org.telegram.ui.Stories.PeerStoriesView$5$$ExternalSyntheticLambda4
                    @Override // java.lang.Runnable
                    public final void run() {
                        storyViewer.updatePlayingMode();
                    }
                });
                storyViewer.updatePlayingMode();
                return;
            }
            BulletinFactory.of(PeerStoriesView.this.storyContainer, resourcesProvider).createSimpleBulletin(R.raw.story_bomb2, LocaleController.getString(R.string.StoryNotFound)).setTag(3).show(true);
        }

        @Override // org.telegram.ui.Stories.StoryCaptionView
        public void onEmojiClick(AnimatedEmojiSpan animatedEmojiSpan) {
            if (animatedEmojiSpan != null) {
                PeerStoriesView peerStoriesView = PeerStoriesView.this;
                if (peerStoriesView.delegate == null) {
                    return;
                }
                TLRPC.Document documentFindDocument = animatedEmojiSpan.document;
                if (documentFindDocument == null) {
                    documentFindDocument = AnimatedEmojiDrawable.findDocument(peerStoriesView.currentAccount, animatedEmojiSpan.documentId);
                }
                if (documentFindDocument == null) {
                    return;
                }
                BulletinFactory bulletinFactoryOf = BulletinFactory.of(PeerStoriesView.this.storyContainer, this.val$resourcesProvider);
                final StoryViewer storyViewer = this.val$storyViewer;
                final Theme.ResourcesProvider resourcesProvider = this.val$resourcesProvider;
                Bulletin bulletinCreateContainsEmojiBulletin = bulletinFactoryOf.createContainsEmojiBulletin(documentFindDocument, 2, new Utilities.Callback() { // from class: org.telegram.ui.Stories.PeerStoriesView$5$$ExternalSyntheticLambda0
                    @Override // org.telegram.messenger.Utilities.Callback
                    public final void run(Object obj) {
                        this.f$0.lambda$onEmojiClick$4(storyViewer, resourcesProvider, (TLRPC.InputStickerSet) obj);
                    }
                });
                if (bulletinCreateContainsEmojiBulletin == null) {
                    return;
                }
                bulletinCreateContainsEmojiBulletin.tag = 1;
                bulletinCreateContainsEmojiBulletin.show(true);
            }
        }

        /* JADX INFO: Access modifiers changed from: private */
        public /* synthetic */ void lambda$onEmojiClick$4(StoryViewer storyViewer, Theme.ResourcesProvider resourcesProvider, TLRPC.InputStickerSet inputStickerSet) {
            ArrayList arrayList = new ArrayList(1);
            arrayList.add(inputStickerSet);
            EmojiPacksAlert emojiPacksAlert = new EmojiPacksAlert(storyViewer.fragment, getContext(), resourcesProvider, arrayList);
            Delegate delegate = PeerStoriesView.this.delegate;
            if (delegate != null) {
                delegate.showDialog(emojiPacksAlert);
            }
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$new$0(View view) {
        StoryCaptionView storyCaptionView = this.storyCaptionView;
        if (storyCaptionView.expanded) {
            if (!storyCaptionView.textSelectionHelper.isInSelectionMode()) {
                this.storyCaptionView.collapse();
                return;
            } else {
                this.storyCaptionView.checkCancelTextSelection();
                return;
            }
        }
        this.checkBlackoutMode = true;
        storyCaptionView.expand();
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$new$1(View view) {
        shareStory(true);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$new$2(View view) {
        tryToOpenRepostStory();
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$new$4(View view) {
        TL_stories.StoryItem storyItem = this.currentStory.storyItem;
        if (storyItem != null && storyItem.sent_reaction == null) {
            applyMessageToChat(new Runnable() { // from class: org.telegram.ui.Stories.PeerStoriesView$$ExternalSyntheticLambda38
                @Override // java.lang.Runnable
                public final void run() {
                    this.f$0.lambda$new$3();
                }
            });
        } else {
            likeStory(null);
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$new$3() {
        likeStory(null);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ boolean lambda$new$5(StoryViewer storyViewer, View view) {
        Runnable runnable = this.reactionsTooltipRunnable;
        if (runnable != null) {
            AndroidUtilities.cancelRunOnUIThread(runnable);
            this.reactionsTooltipRunnable = null;
        }
        SharedConfig.setStoriesReactionsLongPressHintUsed(true);
        HintView2 hintView2 = this.reactionsLongpressTooltip;
        if (hintView2 != null) {
            hintView2.hide();
        }
        checkReactionsLayoutForLike();
        storyViewer.windowView.dispatchTouchEvent(AndroidUtilities.emptyMotionEvent());
        showLikesReaction(true);
        return true;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$new$6(StoryViewer storyViewer, View view) {
        long j = UserConfig.getInstance(this.currentAccount).clientUserId;
        long j2 = this.dialogId;
        if (j == j2) {
            Bundle bundle = new Bundle();
            bundle.putInt("type", 1);
            bundle.putLong("dialog_id", this.dialogId);
            storyViewer.presentFragment(new MediaActivity(bundle, null));
            return;
        }
        if (j2 > 0) {
            storyViewer.presentFragment(ProfileActivity.of(j2));
        } else {
            storyViewer.presentFragment(ChatActivity.of(j2));
        }
    }

    public static /* synthetic */ void $r8$lambda$yJHJR1bijBsghFqqIA0QqbKSXoo(StoryViewer storyViewer, View view) {
        if (storyViewer != null) {
            storyViewer.switchToPip();
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$new$8(Theme.ResourcesProvider resourcesProvider, StoryViewer storyViewer, Context context, SharedResources sharedResources, View view) {
        this.delegate.setPopupIsVisible(true);
        this.editStoryItem = null;
        boolean[] zArr = {false};
        if (this.isSelf) {
            MessagesController.getInstance(this.currentAccount).getStoriesController().loadBlocklistAtFirst();
            MessagesController.getInstance(this.currentAccount).getStoriesController().loadSendAs();
            MessagesController.getInstance(this.currentAccount).getStoriesController().getDraftsController().load();
        }
        boolean z = this.isSelf || MessagesController.getInstance(this.currentAccount).getStoriesController().canEditStory(this.currentStory.storyItem);
        AnonymousClass8 anonymousClass8 = new AnonymousClass8(getContext(), resourcesProvider, true, resourcesProvider, storyViewer, this.currentStory.isVideo, this.isSelf || ((this.isChannel || isBotsPreview()) && z), z, context, sharedResources, zArr);
        this.popupMenu = anonymousClass8;
        anonymousClass8.show(this.optionsIconView, 0, (-ActionBar.getCurrentActionBarHeight()) + AndroidUtilities.dp(6.0f));
    }

    /* JADX INFO: renamed from: org.telegram.ui.Stories.PeerStoriesView$8, reason: invalid class name */
    class AnonymousClass8 extends CustomPopupMenu {
        private boolean edit;
        final /* synthetic */ boolean val$canEditStory;
        final /* synthetic */ Context val$context;
        final /* synthetic */ boolean[] val$popupStillVisible;
        final /* synthetic */ Theme.ResourcesProvider val$resourcesProvider;
        final /* synthetic */ SharedResources val$sharedResources;
        final /* synthetic */ boolean val$speedControl;
        final /* synthetic */ StoryViewer val$storyViewer;
        final /* synthetic */ boolean val$userCanEditStory;

        /* JADX WARN: 'super' call moved to the top of the method (can break code semantics) */
        AnonymousClass8(Context context, Theme.ResourcesProvider resourcesProvider, boolean z, Theme.ResourcesProvider resourcesProvider2, StoryViewer storyViewer, boolean z2, boolean z3, boolean z4, Context context2, SharedResources sharedResources, boolean[] zArr) {
            super(context, resourcesProvider, z);
            this.val$resourcesProvider = resourcesProvider2;
            this.val$storyViewer = storyViewer;
            this.val$speedControl = z2;
            this.val$canEditStory = z3;
            this.val$userCanEditStory = z4;
            this.val$context = context2;
            this.val$sharedResources = sharedResources;
            this.val$popupStillVisible = zArr;
        }

        private void addViewStatistics(ActionBarPopupWindow.ActionBarPopupWindowLayout actionBarPopupWindowLayout, final TL_stories.StoryItem storyItem) {
            final TLRPC.Chat chat;
            PeerStoriesView peerStoriesView = PeerStoriesView.this;
            if (!peerStoriesView.isChannel || storyItem == null || (storyItem.media instanceof TLRPC.TL_messageMediaVideoStream) || (chat = MessagesController.getInstance(peerStoriesView.currentAccount).getChat(Long.valueOf(-PeerStoriesView.this.dialogId))) == null) {
                return;
            }
            TLRPC.ChatFull chatFull = MessagesController.getInstance(PeerStoriesView.this.currentAccount).getChatFull(chat.id);
            if (chatFull == null) {
                chatFull = MessagesStorage.getInstance(PeerStoriesView.this.currentAccount).loadChatInfo(chat.id, true, new CountDownLatch(1), false, false);
            }
            if (chatFull == null || !chatFull.can_view_stats) {
                return;
            }
            ActionBarMenuSubItem actionBarMenuSubItemAddItem = ActionBarMenuItem.addItem(actionBarPopupWindowLayout, R.drawable.msg_stats, LocaleController.getString(R.string.ViewStatistics), false, this.val$resourcesProvider);
            final StoryViewer storyViewer = this.val$storyViewer;
            actionBarMenuSubItemAddItem.setOnClickListener(new View.OnClickListener() { // from class: org.telegram.ui.Stories.PeerStoriesView$8$$ExternalSyntheticLambda59
                @Override // android.view.View.OnClickListener
                public final void onClick(View view) {
                    this.f$0.lambda$addViewStatistics$0(storyItem, storyViewer, chat, view);
                }
            });
        }

        /* JADX INFO: Access modifiers changed from: private */
        public /* synthetic */ void lambda$addViewStatistics$0(TL_stories.StoryItem storyItem, StoryViewer storyViewer, TLRPC.Chat chat, View view) {
            CustomPopupMenu customPopupMenu = PeerStoriesView.this.popupMenu;
            if (customPopupMenu != null) {
                customPopupMenu.dismiss();
            }
            storyItem.dialogId = PeerStoriesView.this.dialogId;
            storyItem.messageId = storyItem.id;
            MessageObject messageObject = new MessageObject(PeerStoriesView.this.currentAccount, storyItem);
            messageObject.generateThumbs(false);
            storyViewer.presentFragment(new MessageStatisticActivity(messageObject, chat.id, false) { // from class: org.telegram.ui.Stories.PeerStoriesView.8.1
                @Override // org.telegram.ui.MessageStatisticActivity, org.telegram.ui.ActionBar.BaseFragment
                public boolean isLightStatusBar() {
                    return false;
                }

                @Override // org.telegram.ui.ActionBar.BaseFragment
                public Theme.ResourcesProvider getResourceProvider() {
                    return new DarkThemeResourceProvider();
                }
            });
        }

        private void addSpeedLayout(final ActionBarPopupWindow.ActionBarPopupWindowLayout actionBarPopupWindowLayout, boolean z) {
            PeerStoriesView peerStoriesView;
            StoryItemHolder storyItemHolder;
            if (!this.val$speedControl || ((storyItemHolder = (peerStoriesView = PeerStoriesView.this).currentStory) != null && storyItemHolder.uploadingStory != null)) {
                PeerStoriesView.this.speedLayout = null;
                PeerStoriesView.this.speedItem = null;
                return;
            }
            peerStoriesView.speedLayout = new ChooseSpeedLayout(peerStoriesView.getContext(), actionBarPopupWindowLayout.getSwipeBack(), new ChooseSpeedLayout.Callback() { // from class: org.telegram.ui.Stories.PeerStoriesView.8.2
                @Override // org.telegram.ui.ChooseSpeedLayout.Callback
                public void onSpeedSelected(float f, boolean z2, boolean z3) {
                    StoryViewer storyViewer = AnonymousClass8.this.val$storyViewer;
                    if (storyViewer != null) {
                        storyViewer.setSpeed(f);
                    }
                    PeerStoriesView.this.updateSpeedItem(z2);
                    if (!z3 || actionBarPopupWindowLayout.getSwipeBack() == null) {
                        return;
                    }
                    actionBarPopupWindowLayout.getSwipeBack().closeForeground();
                }
            });
            PeerStoriesView.this.speedLayout.update(StoryViewer.currentSpeed, true);
            PeerStoriesView.this.speedItem = new ActionBarMenuSubItem(PeerStoriesView.this.getContext(), false, false, false, this.val$resourcesProvider);
            PeerStoriesView.this.speedItem.setTextAndIcon(LocaleController.getString(R.string.Speed), R.drawable.msg_speed, null);
            PeerStoriesView.this.updateSpeedItem(true);
            PeerStoriesView.this.speedItem.setMinimumWidth(AndroidUtilities.dp(196.0f));
            PeerStoriesView.this.speedItem.setRightIcon(R.drawable.msg_arrowright);
            actionBarPopupWindowLayout.addView(PeerStoriesView.this.speedItem);
            LinearLayout.LayoutParams layoutParams = (LinearLayout.LayoutParams) PeerStoriesView.this.speedItem.getLayoutParams();
            if (LocaleController.isRTL) {
                layoutParams.gravity = 5;
            }
            layoutParams.width = -1;
            layoutParams.height = AndroidUtilities.dp(48.0f);
            PeerStoriesView.this.speedItem.setLayoutParams(layoutParams);
            final int iAddViewToSwipeBack = actionBarPopupWindowLayout.addViewToSwipeBack(PeerStoriesView.this.speedLayout.speedSwipeBackLayout);
            PeerStoriesView.this.speedItem.openSwipeBackLayout = new Runnable() { // from class: org.telegram.ui.Stories.PeerStoriesView$8$$ExternalSyntheticLambda46
                @Override // java.lang.Runnable
                public final void run() {
                    PeerStoriesView.AnonymousClass8.$r8$lambda$f2mS9OHREAaY0zfxPk0fkyfF6ck(actionBarPopupWindowLayout, iAddViewToSwipeBack);
                }
            };
            PeerStoriesView.this.speedItem.setOnClickListener(new View.OnClickListener() { // from class: org.telegram.ui.Stories.PeerStoriesView$8$$ExternalSyntheticLambda47
                @Override // android.view.View.OnClickListener
                public final void onClick(View view) {
                    this.f$0.lambda$addSpeedLayout$2(view);
                }
            });
            actionBarPopupWindowLayout.swipeBackGravityRight = true;
            if (z) {
                ActionBarPopupWindow.GapView gapView = new ActionBarPopupWindow.GapView(PeerStoriesView.this.getContext(), this.val$resourcesProvider, Theme.key_actionBarDefaultSubmenuSeparator);
                gapView.setTag(R.id.fit_width_tag, 1);
                actionBarPopupWindowLayout.addView((View) gapView, LayoutHelper.createLinear(-1, 8));
            }
        }

        public static /* synthetic */ void $r8$lambda$f2mS9OHREAaY0zfxPk0fkyfF6ck(ActionBarPopupWindow.ActionBarPopupWindowLayout actionBarPopupWindowLayout, int i) {
            if (actionBarPopupWindowLayout.getSwipeBack() != null) {
                actionBarPopupWindowLayout.getSwipeBack().openForeground(i);
            }
        }

        /* JADX INFO: Access modifiers changed from: private */
        public /* synthetic */ void lambda$addSpeedLayout$2(View view) {
            PeerStoriesView.this.speedItem.openSwipeBack();
        }

        private void addAlbumsLayout(final ActionBarPopupWindow.ActionBarPopupWindowLayout actionBarPopupWindowLayout, boolean z) {
            HashSet hashSet;
            final TL_stories.StoryItem storyItem = PeerStoriesView.this.currentStory.storyItem;
            if (storyItem == null) {
                return;
            }
            if (storyItem.albums != null) {
                hashSet = new HashSet(storyItem.albums);
            } else {
                hashSet = new HashSet();
            }
            final HashSet hashSet2 = hashSet;
            ItemOptions itemOptionsSwipeback = ItemOptions.swipeback(actionBarPopupWindowLayout, this.val$resourcesProvider);
            itemOptionsSwipeback.add(R.drawable.ic_ab_back, LocaleController.getString(R.string.Back), new Runnable() { // from class: org.telegram.ui.Stories.PeerStoriesView$8$$ExternalSyntheticLambda48
                @Override // java.lang.Runnable
                public final void run() {
                    PeerStoriesView.AnonymousClass8.$r8$lambda$rVsuPxLsudFzomSQr9kQ1d0unxs(actionBarPopupWindowLayout);
                }
            });
            itemOptionsSwipeback.addGap();
            StoriesController.StoriesCollections storyAlbumsList = PeerStoriesView.this.getStoriesController().getStoryAlbumsList(PeerStoriesView.this.dialogId);
            PeerStoriesView peerStoriesView = PeerStoriesView.this;
            boolean zCanCreateNewAlbum = peerStoriesView.storiesController.canCreateNewAlbum(peerStoriesView.dialogId);
            final Theme.ResourcesProvider resourcesProvider = this.val$resourcesProvider;
            Runnable runnable = new Runnable() { // from class: org.telegram.ui.Stories.PeerStoriesView$8$$ExternalSyntheticLambda49
                @Override // java.lang.Runnable
                public final void run() {
                    this.f$0.lambda$addAlbumsLayout$6(resourcesProvider, storyItem);
                }
            };
            final Theme.ResourcesProvider resourcesProvider2 = this.val$resourcesProvider;
            ItemOptions.addAlbumsItemOptions(itemOptionsSwipeback, storyAlbumsList, hashSet2, zCanCreateNewAlbum, runnable, new Utilities.Callback() { // from class: org.telegram.ui.Stories.PeerStoriesView$8$$ExternalSyntheticLambda50
                @Override // org.telegram.messenger.Utilities.Callback
                public final void run(Object obj) {
                    this.f$0.lambda$addAlbumsLayout$7(hashSet2, storyItem, resourcesProvider2, (StoriesController.StoryAlbum) obj);
                }
            });
            PeerStoriesView.this.albumLayout = itemOptionsSwipeback.getLinearLayout();
            final int iAddViewToSwipeBack = actionBarPopupWindowLayout.addViewToSwipeBack(PeerStoriesView.this.albumLayout);
            PeerStoriesView.this.albumItem = new ActionBarMenuSubItem(PeerStoriesView.this.getContext(), false, false, false, this.val$resourcesProvider);
            PeerStoriesView.this.albumItem.setTextAndIcon(LocaleController.getString(R.string.StoriesAlbumAddToAlbum), R.drawable.menu_album_add, null);
            PeerStoriesView.this.albumItem.openSwipeBackLayout = new Runnable() { // from class: org.telegram.ui.Stories.PeerStoriesView$8$$ExternalSyntheticLambda51
                @Override // java.lang.Runnable
                public final void run() {
                    PeerStoriesView.AnonymousClass8.$r8$lambda$kkQekJEdPFrNqPha5yZIXlapoB4(actionBarPopupWindowLayout, iAddViewToSwipeBack);
                }
            };
            PeerStoriesView.this.albumItem.setOnClickListener(new View.OnClickListener() { // from class: org.telegram.ui.Stories.PeerStoriesView$8$$ExternalSyntheticLambda52
                @Override // android.view.View.OnClickListener
                public final void onClick(View view) {
                    this.f$0.lambda$addAlbumsLayout$9(view);
                }
            });
            actionBarPopupWindowLayout.addView(PeerStoriesView.this.albumItem);
            actionBarPopupWindowLayout.swipeBackGravityRight = true;
            if (z) {
                ActionBarPopupWindow.GapView gapView = new ActionBarPopupWindow.GapView(PeerStoriesView.this.getContext(), this.val$resourcesProvider, Theme.key_actionBarDefaultSubmenuSeparator);
                gapView.setTag(R.id.fit_width_tag, 1);
                actionBarPopupWindowLayout.addView((View) gapView, LayoutHelper.createLinear(-1, 8));
            }
        }

        public static /* synthetic */ void $r8$lambda$rVsuPxLsudFzomSQr9kQ1d0unxs(ActionBarPopupWindow.ActionBarPopupWindowLayout actionBarPopupWindowLayout) {
            if (actionBarPopupWindowLayout.getSwipeBack() != null) {
                actionBarPopupWindowLayout.getSwipeBack().closeForeground();
            }
        }

        /* JADX INFO: Access modifiers changed from: private */
        public /* synthetic */ void lambda$addAlbumsLayout$6(final Theme.ResourcesProvider resourcesProvider, final TL_stories.StoryItem storyItem) {
            AlertsCreator.createStoriesAlbumEnterNameForCreate(PeerStoriesView.this.getContext(), null, resourcesProvider, new MessagesStorage.StringCallback() { // from class: org.telegram.ui.Stories.PeerStoriesView$8$$ExternalSyntheticLambda6
                @Override // org.telegram.messenger.MessagesStorage.StringCallback
                public final void run(String str) {
                    this.f$0.lambda$addAlbumsLayout$5(storyItem, resourcesProvider, str);
                }
            });
            CustomPopupMenu customPopupMenu = PeerStoriesView.this.popupMenu;
            if (customPopupMenu != null) {
                customPopupMenu.dismiss();
            }
        }

        /* JADX INFO: Access modifiers changed from: private */
        public /* synthetic */ void lambda$addAlbumsLayout$5(final TL_stories.StoryItem storyItem, final Theme.ResourcesProvider resourcesProvider, String str) {
            PeerStoriesView.this.getStoriesController().createAlbum(PeerStoriesView.this.dialogId, str, new Utilities.Callback() { // from class: org.telegram.ui.Stories.PeerStoriesView$8$$ExternalSyntheticLambda14
                @Override // org.telegram.messenger.Utilities.Callback
                public final void run(Object obj) {
                    this.f$0.lambda$addAlbumsLayout$4(storyItem, resourcesProvider, (StoriesController.StoryAlbum) obj);
                }
            });
        }

        /* JADX INFO: Access modifiers changed from: private */
        public /* synthetic */ void lambda$addAlbumsLayout$4(TL_stories.StoryItem storyItem, Theme.ResourcesProvider resourcesProvider, StoriesController.StoryAlbum storyAlbum) {
            PeerStoriesView peerStoriesView = PeerStoriesView.this;
            peerStoriesView.storiesController.addStoryToAlbum(peerStoriesView.dialogId, storyAlbum.album_id, storyItem);
            BulletinFactory.of(PeerStoriesView.this.storyContainer, resourcesProvider).createSimpleBulletin(R.raw.contact_check, AndroidUtilities.replaceTags(LocaleController.formatString(R.string.StoryAddedToAlbumX, storyAlbum.title))).show();
        }

        /* JADX INFO: Access modifiers changed from: private */
        public /* synthetic */ void lambda$addAlbumsLayout$7(HashSet hashSet, TL_stories.StoryItem storyItem, Theme.ResourcesProvider resourcesProvider, StoriesController.StoryAlbum storyAlbum) {
            String string;
            if (hashSet.contains(Integer.valueOf(storyAlbum.album_id))) {
                PeerStoriesView peerStoriesView = PeerStoriesView.this;
                peerStoriesView.storiesController.addStoryToAlbum(peerStoriesView.dialogId, storyAlbum.album_id, storyItem);
                string = LocaleController.formatString(R.string.StoryAddedToAlbumX, storyAlbum.title);
            } else {
                PeerStoriesView peerStoriesView2 = PeerStoriesView.this;
                peerStoriesView2.storiesController.removeStoryFromAlbum(peerStoriesView2.dialogId, storyAlbum.album_id, storyItem);
                string = LocaleController.formatString(R.string.StoryRemovedFromAlbumX, storyAlbum.title);
            }
            BulletinFactory.of(PeerStoriesView.this.storyContainer, resourcesProvider).createSimpleBulletin(R.raw.contact_check, AndroidUtilities.replaceTags(string)).show();
            CustomPopupMenu customPopupMenu = PeerStoriesView.this.popupMenu;
            if (customPopupMenu != null) {
                customPopupMenu.dismiss();
            }
        }

        public static /* synthetic */ void $r8$lambda$kkQekJEdPFrNqPha5yZIXlapoB4(ActionBarPopupWindow.ActionBarPopupWindowLayout actionBarPopupWindowLayout, int i) {
            if (actionBarPopupWindowLayout.getSwipeBack() != null) {
                actionBarPopupWindowLayout.getSwipeBack().openForeground(i);
            }
        }

        /* JADX INFO: Access modifiers changed from: private */
        public /* synthetic */ void lambda$addAlbumsLayout$9(View view) {
            PeerStoriesView.this.albumItem.openSwipeBack();
        }

        /* JADX INFO: Access modifiers changed from: private */
        public /* synthetic */ void lambda$onCreate$10(View view) {
            StoriesController.UploadingStory uploadingStory = PeerStoriesView.this.currentStory.uploadingStory;
            if (uploadingStory != null) {
                uploadingStory.cancel();
                PeerStoriesView.this.updateStoryItems();
            }
            CustomPopupMenu customPopupMenu = PeerStoriesView.this.popupMenu;
            if (customPopupMenu != null) {
                customPopupMenu.dismiss();
            }
        }

        /* JADX INFO: Access modifiers changed from: private */
        public /* synthetic */ void lambda$onCreate$11(StoryPrivacyBottomSheet.StoryPrivacy storyPrivacy, TL_stories.StoryItem storyItem, View view) {
            PeerStoriesView.this.editPrivacy(storyPrivacy, storyItem);
            CustomPopupMenu customPopupMenu = PeerStoriesView.this.popupMenu;
            if (customPopupMenu != null) {
                customPopupMenu.dismiss();
            }
        }

        /* JADX WARN: Code duplicated, block: B:158:0x044b  */
        /* JADX WARN: Code duplicated, block: B:44:0x0132 A[PHI: r2
  0x0132: PHI (r2v127 boolean) = (r2v113 boolean), (r2v129 boolean) binds: [B:53:0x0145, B:43:0x0130] A[DONT_GENERATE, DONT_INLINE]] */
        /* JADX WARN: Code duplicated, block: B:45:0x0134 A[PHI: r2
  0x0134: PHI (r2v116 boolean) = (r2v113 boolean), (r2v113 boolean), (r2v129 boolean), (r2v129 boolean) binds: [B:51:0x0141, B:53:0x0145, B:41:0x012c, B:43:0x0130] A[DONT_GENERATE, DONT_INLINE]] */
        @Override // org.telegram.ui.Components.CustomPopupMenu
        protected void onCreate(ActionBarPopupWindow.ActionBarPopupWindowLayout actionBarPopupWindowLayout) {
            final TL_stories.StoryItem storyItem;
            String string;
            LivePlayer livePlayer;
            LivePlayer livePlayer2;
            final StoryPrivacyBottomSheet.StoryPrivacy storyPrivacy;
            TL_stories.StoryItem storyItem2;
            TLRPC.MessageMedia messageMedia;
            TLRPC.Photo photo;
            TLRPC.Chat chat;
            TLRPC.User user;
            final TLObject tLObject;
            String strTrim;
            boolean z;
            boolean z2;
            if (this.val$canEditStory || PeerStoriesView.this.currentStory.uploadingStory != null) {
                StoryItemHolder storyItemHolder = PeerStoriesView.this.currentStory;
                final TL_stories.StoryItem storyItem3 = storyItemHolder.storyItem;
                if (storyItemHolder.uploadingStory != null) {
                    ActionBarMenuItem.addItem(actionBarPopupWindowLayout, R.drawable.msg_cancel, LocaleController.getString(R.string.Cancel), false, this.val$resourcesProvider).setOnClickListener(new View.OnClickListener() { // from class: org.telegram.ui.Stories.PeerStoriesView$8$$ExternalSyntheticLambda16
                        @Override // android.view.View.OnClickListener
                        public final void onClick(View view) {
                            this.f$0.lambda$onCreate$10(view);
                        }
                    });
                }
                if (storyItem3 == null) {
                    return;
                }
                PeerStoriesView peerStoriesView = PeerStoriesView.this;
                if ((peerStoriesView.isSelf || peerStoriesView.storiesController.canEditStories(peerStoriesView.dialogId)) && !PeerStoriesView.this.currentStory.isLive) {
                    addAlbumsLayout(actionBarPopupWindowLayout, true);
                }
                if (PeerStoriesView.this.isSelf) {
                    if (storyItem3.privacy.isEmpty()) {
                        storyPrivacy = new StoryPrivacyBottomSheet.StoryPrivacy(3, PeerStoriesView.this.currentAccount, new ArrayList());
                    } else {
                        storyPrivacy = new StoryPrivacyBottomSheet.StoryPrivacy(PeerStoriesView.this.currentAccount, storyItem3.privacy);
                    }
                    ActionBarMenuSubItem actionBarMenuSubItemAddItem = ActionBarMenuItem.addItem(actionBarPopupWindowLayout, R.drawable.msg_view_file, LocaleController.getString(R.string.WhoCanSee), false, this.val$resourcesProvider);
                    actionBarMenuSubItemAddItem.setSubtext(storyPrivacy.toString());
                    actionBarMenuSubItemAddItem.setOnClickListener(new View.OnClickListener() { // from class: org.telegram.ui.Stories.PeerStoriesView$8$$ExternalSyntheticLambda27
                        @Override // android.view.View.OnClickListener
                        public final void onClick(View view) {
                            this.f$0.lambda$onCreate$11(storyPrivacy, storyItem3, view);
                        }
                    });
                    actionBarMenuSubItemAddItem.setItemHeight(56);
                }
                addSpeedLayout(actionBarPopupWindowLayout, false);
                if (PeerStoriesView.this.isSelf || this.val$speedControl) {
                    View gapView = new ActionBarPopupWindow.GapView(PeerStoriesView.this.getContext(), this.val$resourcesProvider, Theme.key_actionBarDefaultSubmenuSeparator);
                    gapView.setTag(R.id.fit_width_tag, 1);
                    actionBarPopupWindowLayout.addView(gapView, LayoutHelper.createLinear(-1, 8));
                }
                PeerStoriesView peerStoriesView2 = PeerStoriesView.this;
                if (!peerStoriesView2.unsupported && !peerStoriesView2.currentStory.isLive && ((PeerStoriesView.this.isBotsPreview() || MessagesController.getInstance(PeerStoriesView.this.currentAccount).storiesEnabled()) && this.val$userCanEditStory)) {
                    PeerStoriesView peerStoriesView3 = PeerStoriesView.this;
                    peerStoriesView3.editStoryItem = ActionBarMenuItem.addItem(actionBarPopupWindowLayout, R.drawable.msg_edit, LocaleController.getString(peerStoriesView3.isBotsPreview() ? R.string.EditBotPreview : R.string.EditStory), false, this.val$resourcesProvider);
                    ActionBarMenuSubItem actionBarMenuSubItem = PeerStoriesView.this.editStoryItem;
                    final Theme.ResourcesProvider resourcesProvider = this.val$resourcesProvider;
                    final Context context = this.val$context;
                    final StoryViewer storyViewer = this.val$storyViewer;
                    final SharedResources sharedResources = this.val$sharedResources;
                    actionBarMenuSubItem.setOnClickListener(new View.OnClickListener() { // from class: org.telegram.ui.Stories.PeerStoriesView$8$$ExternalSyntheticLambda35
                        @Override // android.view.View.OnClickListener
                        public final void onClick(View view) {
                            this.f$0.lambda$onCreate$17(resourcesProvider, context, storyViewer, sharedResources, view);
                        }
                    });
                    PeerStoriesView peerStoriesView4 = PeerStoriesView.this;
                    if (peerStoriesView4.storiesController.hasUploadingStories(peerStoriesView4.dialogId) && PeerStoriesView.this.currentStory.isVideo && !SharedConfig.allowPreparingHevcPlayers()) {
                        PeerStoriesView.this.editStoryItem.setAlpha(0.5f);
                    }
                }
                StoryItemHolder storyItemHolder2 = PeerStoriesView.this.currentStory;
                if (storyItemHolder2.storyItem == null || !storyItemHolder2.isVideo || PeerStoriesView.this.currentStory.isLive) {
                    storyItem = storyItem3;
                } else {
                    PeerStoriesView peerStoriesView5 = PeerStoriesView.this;
                    if (peerStoriesView5.currentStory.storyItem.pinned || peerStoriesView5.isEditBotsPreview()) {
                        ActionBarMenuSubItem actionBarMenuSubItemAddItem2 = ActionBarMenuItem.addItem(actionBarPopupWindowLayout, R.drawable.menu_cover_stories, LocaleController.getString(R.string.StoryEditCoverMenu), false, this.val$resourcesProvider);
                        final Context context2 = this.val$context;
                        final StoryViewer storyViewer2 = this.val$storyViewer;
                        final SharedResources sharedResources2 = this.val$sharedResources;
                        storyItem = storyItem3;
                        actionBarMenuSubItemAddItem2.setOnClickListener(new View.OnClickListener() { // from class: org.telegram.ui.Stories.PeerStoriesView$8$$ExternalSyntheticLambda36
                            @Override // android.view.View.OnClickListener
                            public final void onClick(View view) {
                                this.f$0.lambda$onCreate$27(context2, storyItem, storyViewer2, sharedResources2, view);
                            }
                        });
                    } else {
                        storyItem = storyItem3;
                    }
                }
                PeerStoriesView peerStoriesView6 = PeerStoriesView.this;
                if ((peerStoriesView6.isSelf || (peerStoriesView6.isChannel && MessagesController.getInstance(peerStoriesView6.currentAccount).getStoriesController().canEditStories(storyItem.dialogId))) && !PeerStoriesView.this.currentStory.isLive) {
                    boolean z3 = storyItem.pinned;
                    final boolean z4 = !z3;
                    if (PeerStoriesView.this.isSelf) {
                        string = LocaleController.getString(!z3 ? R.string.SaveToProfile : R.string.ArchiveStory);
                    } else {
                        string = LocaleController.getString(!z3 ? R.string.SaveToPosts : R.string.RemoveFromPosts);
                    }
                    ActionBarMenuSubItem actionBarMenuSubItemAddItem3 = ActionBarMenuItem.addItem(actionBarPopupWindowLayout, !z3 ? R.drawable.msg_save_story : R.drawable.menu_unsave_story, string, false, this.val$resourcesProvider);
                    final Theme.ResourcesProvider resourcesProvider2 = this.val$resourcesProvider;
                    actionBarMenuSubItemAddItem3.setOnClickListener(new View.OnClickListener() { // from class: org.telegram.ui.Stories.PeerStoriesView$8$$ExternalSyntheticLambda37
                        @Override // android.view.View.OnClickListener
                        public final void onClick(View view) {
                            this.f$0.lambda$onCreate$29(storyItem, z4, resourcesProvider2, view);
                        }
                    });
                }
                addViewStatistics(actionBarPopupWindowLayout, storyItem);
                PeerStoriesView peerStoriesView7 = PeerStoriesView.this;
                if (!peerStoriesView7.unsupported && !peerStoriesView7.currentStory.isLive) {
                    ActionBarMenuItem.addItem(actionBarPopupWindowLayout, R.drawable.msg_gallery, LocaleController.getString(PeerStoriesView.this.currentStory.isVideo() ? R.string.SaveVideo : R.string.SaveImage), false, this.val$resourcesProvider).setOnClickListener(new View.OnClickListener() { // from class: org.telegram.ui.Stories.PeerStoriesView$8$$ExternalSyntheticLambda38
                        @Override // android.view.View.OnClickListener
                        public final void onClick(View view) {
                            this.f$0.lambda$onCreate$30(view);
                        }
                    });
                }
                if (!MessagesController.getInstance(PeerStoriesView.this.currentAccount).premiumFeaturesBlocked() && !PeerStoriesView.this.currentStory.isLive) {
                    PeerStoriesView peerStoriesView8 = PeerStoriesView.this;
                    if (!peerStoriesView8.isChannel) {
                        peerStoriesView8.createStealthModeItem(actionBarPopupWindowLayout);
                    }
                }
                PeerStoriesView peerStoriesView9 = PeerStoriesView.this;
                if (peerStoriesView9.isChannel && peerStoriesView9.allowShareLink) {
                    ActionBarMenuItem.addItem(actionBarPopupWindowLayout, R.drawable.msg_link, LocaleController.getString(R.string.CopyLink), false, this.val$resourcesProvider).setOnClickListener(new View.OnClickListener() { // from class: org.telegram.ui.Stories.PeerStoriesView$8$$ExternalSyntheticLambda39
                        @Override // android.view.View.OnClickListener
                        public final void onClick(View view) {
                            this.f$0.lambda$onCreate$31(view);
                        }
                    });
                }
                if (PeerStoriesView.this.allowShareLink) {
                    ActionBarMenuItem.addItem(actionBarPopupWindowLayout, R.drawable.msg_shareout, LocaleController.getString(R.string.BotShare), false, this.val$resourcesProvider).setOnClickListener(new View.OnClickListener() { // from class: org.telegram.ui.Stories.PeerStoriesView$8$$ExternalSyntheticLambda40
                        @Override // android.view.View.OnClickListener
                        public final void onClick(View view) {
                            this.f$0.lambda$onCreate$32(view);
                        }
                    });
                }
                TL_stories.StoryItem storyItem4 = PeerStoriesView.this.currentStory.storyItem;
                if (storyItem4 != null) {
                    TLRPC.MessageMedia messageMedia2 = storyItem4.media;
                    if ((messageMedia2 instanceof TLRPC.TL_messageMediaVideoStream) && (livePlayer2 = LivePlayer.recording) != null && livePlayer2.equals(((TLRPC.TL_messageMediaVideoStream) messageMedia2).call)) {
                        LivePlayer livePlayer3 = LivePlayer.recording;
                        final boolean z5 = livePlayer3 != null && livePlayer3.isMuted();
                        ActionBarMenuItem.addItem(actionBarPopupWindowLayout, z5 ? R.drawable.msg_voice_unmuted : R.drawable.msg_voice_muted, LocaleController.getString(z5 ? R.string.Unmute : R.string.Mute), false, this.val$resourcesProvider).setOnClickListener(new View.OnClickListener() { // from class: org.telegram.ui.Stories.PeerStoriesView$8$$ExternalSyntheticLambda41
                            @Override // android.view.View.OnClickListener
                            public final void onClick(View view) {
                                this.f$0.lambda$onCreate$33(z5, view);
                            }
                        });
                        ActionBarMenuItem.addItem(actionBarPopupWindowLayout, R.drawable.menu_camera_retake, LocaleController.getString(R.string.AccDescrSwitchCamera), false, this.val$resourcesProvider).setOnClickListener(new View.OnClickListener() { // from class: org.telegram.ui.Stories.PeerStoriesView$8$$ExternalSyntheticLambda42
                            @Override // android.view.View.OnClickListener
                            public final void onClick(View view) {
                                this.f$0.lambda$onCreate$34(view);
                            }
                        });
                    }
                }
                if (PeerStoriesView.this.currentStory.isLive) {
                    ActionBarMenuSubItem actionBarMenuSubItemAddItem4 = ActionBarMenuItem.addItem(actionBarPopupWindowLayout, R.drawable.menu_video_pip, LocaleController.getString(R.string.PipMinimize), false, this.val$resourcesProvider);
                    final StoryViewer storyViewer3 = this.val$storyViewer;
                    actionBarMenuSubItemAddItem4.setOnClickListener(new View.OnClickListener() { // from class: org.telegram.ui.Stories.PeerStoriesView$8$$ExternalSyntheticLambda17
                        @Override // android.view.View.OnClickListener
                        public final void onClick(View view) {
                            this.f$0.lambda$onCreate$35(storyViewer3, view);
                        }
                    });
                }
                if (PeerStoriesView.this.currentStory.isLive && (PeerStoriesView.this.dialogId == UserConfig.getInstance(PeerStoriesView.this.currentAccount).getClientUserId() || ChatObject.canManageCalls(MessagesController.getInstance(PeerStoriesView.this.currentAccount).getChat(Long.valueOf(-PeerStoriesView.this.dialogId))) || ((livePlayer = this.val$storyViewer.livePlayer) != null && PeerStoriesView.this.currentStory.isThisCall(livePlayer.getCallId()) && this.val$storyViewer.livePlayer.isCreator()))) {
                    ActionBarMenuSubItem actionBarMenuSubItemAddItem5 = ActionBarMenuItem.addItem(actionBarPopupWindowLayout, R.drawable.msg_settings_old, LocaleController.getString(R.string.LiveStorySettings), false, this.val$resourcesProvider);
                    final Theme.ResourcesProvider resourcesProvider3 = this.val$resourcesProvider;
                    final StoryViewer storyViewer4 = this.val$storyViewer;
                    actionBarMenuSubItemAddItem5.setOnClickListener(new View.OnClickListener() { // from class: org.telegram.ui.Stories.PeerStoriesView$8$$ExternalSyntheticLambda18
                        @Override // android.view.View.OnClickListener
                        public final void onClick(View view) {
                            this.f$0.lambda$onCreate$39(resourcesProvider3, storyViewer4, view);
                        }
                    });
                }
                if (PeerStoriesView.this.currentStory.isLive) {
                    ActionBarMenuSubItem actionBarMenuSubItemAddItem6 = ActionBarMenuItem.addItem(actionBarPopupWindowLayout, R.drawable.msg_remove, LocaleController.getString(R.string.LiveStoryEnd), false, this.val$resourcesProvider);
                    int i = Theme.key_text_RedBold;
                    actionBarMenuSubItemAddItem6.setSelectorColor(Theme.multAlpha(Theme.getColor(i, this.val$resourcesProvider), 0.12f));
                    actionBarMenuSubItemAddItem6.setColors(this.val$resourcesProvider.getColor(i), this.val$resourcesProvider.getColor(i));
                    final Theme.ResourcesProvider resourcesProvider4 = this.val$resourcesProvider;
                    final StoryViewer storyViewer5 = this.val$storyViewer;
                    actionBarMenuSubItemAddItem6.setOnClickListener(new View.OnClickListener() { // from class: org.telegram.ui.Stories.PeerStoriesView$8$$ExternalSyntheticLambda19
                        @Override // android.view.View.OnClickListener
                        public final void onClick(View view) {
                            this.f$0.lambda$onCreate$41(resourcesProvider4, storyViewer5, view);
                        }
                    });
                }
                if (!PeerStoriesView.this.currentStory.isLive) {
                    PeerStoriesView peerStoriesView10 = PeerStoriesView.this;
                    if (peerStoriesView10.isSelf || MessagesController.getInstance(peerStoriesView10.currentAccount).getStoriesController().canDeleteStory(PeerStoriesView.this.currentStory.storyItem)) {
                        ActionBarMenuSubItem actionBarMenuSubItemAddItem7 = ActionBarMenuItem.addItem(actionBarPopupWindowLayout, R.drawable.msg_delete, LocaleController.getString(R.string.Delete), false, this.val$resourcesProvider);
                        int i2 = Theme.key_text_RedBold;
                        actionBarMenuSubItemAddItem7.setSelectorColor(Theme.multAlpha(Theme.getColor(i2, this.val$resourcesProvider), 0.12f));
                        actionBarMenuSubItemAddItem7.setColors(this.val$resourcesProvider.getColor(i2), this.val$resourcesProvider.getColor(i2));
                        actionBarMenuSubItemAddItem7.setOnClickListener(new View.OnClickListener() { // from class: org.telegram.ui.Stories.PeerStoriesView$8$$ExternalSyntheticLambda20
                            @Override // android.view.View.OnClickListener
                            public final void onClick(View view) {
                                this.f$0.lambda$onCreate$42(view);
                            }
                        });
                    }
                }
            } else {
                addSpeedLayout(actionBarPopupWindowLayout, true);
                final String sharedPrefKey = NotificationsController.getSharedPrefKey(PeerStoriesView.this.dialogId, 0L);
                boolean zAreStoriesNotMuted = NotificationsCustomSettingsActivity.areStoriesNotMuted(PeerStoriesView.this.currentAccount, PeerStoriesView.this.dialogId);
                if (PeerStoriesView.this.dialogId > 0) {
                    user = MessagesController.getInstance(PeerStoriesView.this.currentAccount).getUser(Long.valueOf(PeerStoriesView.this.dialogId));
                    chat = null;
                    tLObject = user;
                } else {
                    chat = MessagesController.getInstance(PeerStoriesView.this.currentAccount).getChat(Long.valueOf(-PeerStoriesView.this.dialogId));
                    user = null;
                    tLObject = chat;
                }
                if (user == null) {
                    strTrim = chat == null ? _UrlKt.FRAGMENT_ENCODE_SET : chat.title;
                } else {
                    strTrim = UserObject.getFirstName(user).trim();
                }
                int iIndexOf = strTrim.indexOf(" ");
                if (iIndexOf > 0) {
                    strTrim = strTrim.substring(0, iIndexOf);
                }
                final String str = strTrim;
                if (!UserObject.isService(PeerStoriesView.this.dialogId) && !PeerStoriesView.this.isBotsPreview()) {
                    if (zAreStoriesNotMuted) {
                        ActionBarMenuSubItem actionBarMenuSubItemAddItem8 = ActionBarMenuItem.addItem(actionBarPopupWindowLayout, R.drawable.msg_mute, LocaleController.getString(R.string.NotificationsStoryMute2), false, this.val$resourcesProvider);
                        final Theme.ResourcesProvider resourcesProvider5 = this.val$resourcesProvider;
                        actionBarMenuSubItemAddItem8.setOnClickListener(new View.OnClickListener() { // from class: org.telegram.ui.Stories.PeerStoriesView$8$$ExternalSyntheticLambda21
                            @Override // android.view.View.OnClickListener
                            public final void onClick(View view) {
                                this.f$0.lambda$onCreate$43(sharedPrefKey, resourcesProvider5, tLObject, str, view);
                            }
                        });
                        actionBarMenuSubItemAddItem8.setMultiline(false);
                    } else {
                        ActionBarMenuSubItem actionBarMenuSubItemAddItem9 = ActionBarMenuItem.addItem(actionBarPopupWindowLayout, R.drawable.msg_unmute, LocaleController.getString(R.string.NotificationsStoryUnmute2), false, this.val$resourcesProvider);
                        final Theme.ResourcesProvider resourcesProvider6 = this.val$resourcesProvider;
                        actionBarMenuSubItemAddItem9.setOnClickListener(new View.OnClickListener() { // from class: org.telegram.ui.Stories.PeerStoriesView$8$$ExternalSyntheticLambda22
                            @Override // android.view.View.OnClickListener
                            public final void onClick(View view) {
                                this.f$0.lambda$onCreate$44(sharedPrefKey, resourcesProvider6, tLObject, str, view);
                            }
                        });
                        actionBarMenuSubItemAddItem9.setMultiline(false);
                    }
                    MediaDataController.getInstance(PeerStoriesView.this.currentAccount).loadHints(true);
                    boolean z6 = (user == null || user.contact || !MediaDataController.getInstance(PeerStoriesView.this.currentAccount).containsTopPeer(PeerStoriesView.this.dialogId)) ? false : true;
                    if (PeerStoriesView.this.dialogId > 0) {
                        z = user != null && user.contact;
                        if (user == null || !user.stories_hidden) {
                            z2 = false;
                        } else {
                            z2 = true;
                        }
                    } else {
                        z = (chat == null || ChatObject.isNotInChat(chat)) ? false : true;
                        if (chat == null || !chat.stories_hidden) {
                            z2 = false;
                        } else {
                            z2 = true;
                        }
                    }
                    if (z6) {
                        ActionBarMenuItem.addItem(actionBarPopupWindowLayout, R.drawable.msg_delete, LocaleController.getString(R.string.StoriesRemoveFromRecent), false, this.val$resourcesProvider).setOnClickListener(new View.OnClickListener() { // from class: org.telegram.ui.Stories.PeerStoriesView$8$$ExternalSyntheticLambda23
                            @Override // android.view.View.OnClickListener
                            public final void onClick(View view) {
                                this.f$0.lambda$onCreate$45(view);
                            }
                        });
                    } else if (z) {
                        if (!z2) {
                            ActionBarMenuItem.addItem(actionBarPopupWindowLayout, R.drawable.msg_archive, LocaleController.getString(R.string.ArchivePeerStories), false, this.val$resourcesProvider).setOnClickListener(new View.OnClickListener() { // from class: org.telegram.ui.Stories.PeerStoriesView$8$$ExternalSyntheticLambda24
                                @Override // android.view.View.OnClickListener
                                public final void onClick(View view) {
                                    this.f$0.lambda$onCreate$46(view);
                                }
                            });
                        } else {
                            ActionBarMenuItem.addItem(actionBarPopupWindowLayout, R.drawable.msg_unarchive, LocaleController.getString(R.string.UnarchiveStories), false, this.val$resourcesProvider).setOnClickListener(new View.OnClickListener() { // from class: org.telegram.ui.Stories.PeerStoriesView$8$$ExternalSyntheticLambda25
                                @Override // android.view.View.OnClickListener
                                public final void onClick(View view) {
                                    this.f$0.lambda$onCreate$47(view);
                                }
                            });
                        }
                    }
                }
                if (PeerStoriesView.this.currentStory.isLive) {
                    ActionBarMenuSubItem actionBarMenuSubItemAddItem10 = ActionBarMenuItem.addItem(actionBarPopupWindowLayout, R.drawable.menu_video_pip, LocaleController.getString(R.string.PipMinimize), false, this.val$resourcesProvider);
                    final StoryViewer storyViewer6 = this.val$storyViewer;
                    actionBarMenuSubItemAddItem10.setOnClickListener(new View.OnClickListener() { // from class: org.telegram.ui.Stories.PeerStoriesView$8$$ExternalSyntheticLambda26
                        @Override // android.view.View.OnClickListener
                        public final void onClick(View view) {
                            this.f$0.lambda$onCreate$48(storyViewer6, view);
                        }
                    });
                }
                if (!MessagesController.getInstance(PeerStoriesView.this.currentAccount).premiumFeaturesBlocked() && PeerStoriesView.this.currentStory.isVideo) {
                    PeerStoriesView.this.createQualityItem(actionBarPopupWindowLayout);
                }
                PeerStoriesView peerStoriesView11 = PeerStoriesView.this;
                if (!peerStoriesView11.unsupported && !peerStoriesView11.currentStory.isLive) {
                    ActionBarMenuItem.addItem(actionBarPopupWindowLayout, R.drawable.msg_gallery, LocaleController.getString(R.string.SaveToGallery), false, this.val$resourcesProvider).setOnClickListener(new View.OnClickListener() { // from class: org.telegram.ui.Stories.PeerStoriesView$8$$ExternalSyntheticLambda28
                        @Override // android.view.View.OnClickListener
                        public final void onClick(View view) {
                            this.f$0.lambda$onCreate$49(view);
                        }
                    });
                }
                if (!MessagesController.getInstance(PeerStoriesView.this.currentAccount).premiumFeaturesBlocked()) {
                    PeerStoriesView peerStoriesView12 = PeerStoriesView.this;
                    if (!peerStoriesView12.isChannel) {
                        peerStoriesView12.createStealthModeItem(actionBarPopupWindowLayout);
                    }
                }
                if (PeerStoriesView.this.allowShareLink) {
                    ActionBarMenuItem.addItem(actionBarPopupWindowLayout, R.drawable.msg_link2, LocaleController.getString(R.string.CopyLink), false, this.val$resourcesProvider).setOnClickListener(new View.OnClickListener() { // from class: org.telegram.ui.Stories.PeerStoriesView$8$$ExternalSyntheticLambda29
                        @Override // android.view.View.OnClickListener
                        public final void onClick(View view) {
                            this.f$0.lambda$onCreate$50(view);
                        }
                    });
                }
                if (PeerStoriesView.this.allowShareLink) {
                    ActionBarMenuItem.addItem(actionBarPopupWindowLayout, R.drawable.msg_shareout, LocaleController.getString(R.string.BotShare), false, this.val$resourcesProvider).setOnClickListener(new View.OnClickListener() { // from class: org.telegram.ui.Stories.PeerStoriesView$8$$ExternalSyntheticLambda30
                        @Override // android.view.View.OnClickListener
                        public final void onClick(View view) {
                            this.f$0.lambda$onCreate$51(view);
                        }
                    });
                }
                TL_stories.StoryItem storyItem5 = PeerStoriesView.this.currentStory.storyItem;
                if (storyItem5 != null) {
                    if (storyItem5.translated && TextUtils.equals(storyItem5.translatedLng, TranslateAlert2.getToLanguage())) {
                        ActionBarMenuItem.addItem(actionBarPopupWindowLayout, R.drawable.msg_translate, LocaleController.getString(R.string.HideTranslation), false, this.val$resourcesProvider).setOnClickListener(new View.OnClickListener() { // from class: org.telegram.ui.Stories.PeerStoriesView$8$$ExternalSyntheticLambda31
                            @Override // android.view.View.OnClickListener
                            public final void onClick(View view) {
                                this.f$0.lambda$onCreate$52(view);
                            }
                        });
                    } else if (MessagesController.getInstance(PeerStoriesView.this.currentAccount).getTranslateController().canTranslateStory(PeerStoriesView.this.currentStory.storyItem)) {
                        ActionBarMenuItem.addItem(actionBarPopupWindowLayout, R.drawable.msg_translate, LocaleController.getString(R.string.TranslateMessage), false, this.val$resourcesProvider).setOnClickListener(new View.OnClickListener() { // from class: org.telegram.ui.Stories.PeerStoriesView$8$$ExternalSyntheticLambda32
                            @Override // android.view.View.OnClickListener
                            public final void onClick(View view) {
                                this.f$0.lambda$onCreate$55(view);
                            }
                        });
                    }
                }
                addViewStatistics(actionBarPopupWindowLayout, PeerStoriesView.this.currentStory.storyItem);
                PeerStoriesView peerStoriesView13 = PeerStoriesView.this;
                if (!peerStoriesView13.unsupported && !UserObject.isService(peerStoriesView13.dialogId) && !PeerStoriesView.this.isBotsPreview()) {
                    ActionBarMenuSubItem actionBarMenuSubItemAddItem11 = ActionBarMenuItem.addItem(actionBarPopupWindowLayout, R.drawable.msg_report, LocaleController.getString(R.string.ReportChat), false, this.val$resourcesProvider);
                    final StoryViewer storyViewer7 = this.val$storyViewer;
                    final Theme.ResourcesProvider resourcesProvider7 = this.val$resourcesProvider;
                    actionBarMenuSubItemAddItem11.setOnClickListener(new View.OnClickListener() { // from class: org.telegram.ui.Stories.PeerStoriesView$8$$ExternalSyntheticLambda33
                        @Override // android.view.View.OnClickListener
                        public final void onClick(View view) {
                            this.f$0.lambda$onCreate$57(storyViewer7, resourcesProvider7, view);
                        }
                    });
                }
            }
            StoryItemHolder storyItemHolder3 = PeerStoriesView.this.currentStory;
            boolean z7 = (storyItemHolder3 == null || (storyItem2 = storyItemHolder3.storyItem) == null || (messageMedia = storyItem2.media) == null || (!MessageObject.isDocumentHasAttachedStickers(messageMedia.document) && ((photo = PeerStoriesView.this.currentStory.storyItem.media.photo) == null || !photo.has_stickers))) ? false : true;
            PeerStoriesView peerStoriesView14 = PeerStoriesView.this;
            ArrayList animatedEmojiSets = peerStoriesView14.getAnimatedEmojiSets(peerStoriesView14.currentStory);
            boolean z8 = (animatedEmojiSets == null || animatedEmojiSets.isEmpty()) ? false : true;
            if (z7 || z8) {
                View gapView2 = new ActionBarPopupWindow.GapView(this.val$context, this.val$resourcesProvider, Theme.key_actionBarDefaultSubmenuSeparator);
                gapView2.setTag(R.id.fit_width_tag, 1);
                actionBarPopupWindowLayout.addView(gapView2, LayoutHelper.createLinear(-1, 8));
                TLRPC.MessageMedia messageMedia3 = PeerStoriesView.this.currentStory.storyItem.media;
                TLObject tLObject2 = messageMedia3.document;
                if (tLObject2 == null) {
                    tLObject2 = messageMedia3.photo;
                }
                final StoryContainsEmojiButton storyContainsEmojiButton = new StoryContainsEmojiButton(this.val$context, PeerStoriesView.this.currentAccount, tLObject2, PeerStoriesView.this.currentStory.storyItem, z7, animatedEmojiSets, this.val$resourcesProvider);
                storyContainsEmojiButton.setOnClickListener(new View.OnClickListener() { // from class: org.telegram.ui.Stories.PeerStoriesView$8$$ExternalSyntheticLambda34
                    @Override // android.view.View.OnClickListener
                    public final void onClick(View view) {
                        this.f$0.lambda$onCreate$58(storyContainsEmojiButton, view);
                    }
                });
                storyContainsEmojiButton.setTag(R.id.fit_width_tag, 1);
                actionBarPopupWindowLayout.addView((View) storyContainsEmojiButton, LayoutHelper.createLinear(-1, -2));
            }
        }

        /* JADX INFO: Access modifiers changed from: private */
        public /* synthetic */ void lambda$onCreate$17(Theme.ResourcesProvider resourcesProvider, Context context, final StoryViewer storyViewer, final SharedResources sharedResources, View view) {
            if (view.getAlpha() < 1.0f) {
                PeerStoriesView peerStoriesView = PeerStoriesView.this;
                int i = -peerStoriesView.shiftDp;
                peerStoriesView.shiftDp = i;
                AndroidUtilities.shakeViewSpring(view, i);
                BulletinFactory.of(PeerStoriesView.this.storyContainer, resourcesProvider).createErrorBulletin("Wait until current upload is complete").show();
                return;
            }
            final Activity activityFindActivity = AndroidUtilities.findActivity(context);
            if (activityFindActivity == null) {
                return;
            }
            this.edit = true;
            CustomPopupMenu customPopupMenu = PeerStoriesView.this.popupMenu;
            if (customPopupMenu != null) {
                customPopupMenu.dismiss();
            }
            Runnable runnable = new Runnable() { // from class: org.telegram.ui.Stories.PeerStoriesView$8$$ExternalSyntheticLambda56
                @Override // java.lang.Runnable
                public final void run() {
                    this.f$0.lambda$onCreate$16(activityFindActivity, storyViewer, sharedResources);
                }
            };
            if (PeerStoriesView.this.delegate.releasePlayer(runnable)) {
                return;
            }
            runnable.run();
        }

        /* JADX INFO: Access modifiers changed from: private */
        public /* synthetic */ void lambda$onCreate$16(Activity activity, StoryViewer storyViewer, final SharedResources sharedResources) {
            File file;
            StoryViewer.VideoPlayerHolder videoPlayerHolder;
            StoryRecorder storyRecorder = StoryRecorder.getInstance(activity, PeerStoriesView.this.currentAccount);
            VideoPlayerSharedScope videoPlayerSharedScope = PeerStoriesView.this.playerSharedScope;
            long j = (videoPlayerSharedScope == null || (videoPlayerHolder = videoPlayerSharedScope.player) == null) ? 0L : videoPlayerHolder.currentPosition;
            DraftsController draftsController = MessagesController.getInstance(PeerStoriesView.this.currentAccount).getStoriesController().getDraftsController();
            TL_stories.StoryItem storyItem = PeerStoriesView.this.currentStory.storyItem;
            StoryEntry forEdit = draftsController.getForEdit(storyItem.dialogId, storyItem);
            if (forEdit == null || forEdit.isRepostMessage || (file = forEdit.file) == null || !file.exists()) {
                forEdit = StoryEntry.fromStoryItem(PeerStoriesView.this.currentStory.getPath(), PeerStoriesView.this.currentStory.storyItem);
                forEdit.editStoryPeerId = PeerStoriesView.this.dialogId;
            }
            StoryEntry storyEntryCopy = forEdit.copy();
            if (PeerStoriesView.this.isBotsPreview()) {
                storyEntryCopy.botId = PeerStoriesView.this.dialogId;
                storyEntryCopy.editingBotPreview = MessagesController.toInputMedia(PeerStoriesView.this.currentStory.storyItem.media);
                StoriesController.StoriesList storiesList = storyViewer.storiesList;
                if (storiesList instanceof StoriesController.BotPreviewsList) {
                    storyEntryCopy.botLang = ((StoriesController.BotPreviewsList) storiesList).lang_code;
                }
            }
            storyRecorder.openEdit(StoryRecorder.SourceView.fromStoryViewer(storyViewer), storyEntryCopy, j, true);
            storyRecorder.setOnFullyOpenListener(new Runnable() { // from class: org.telegram.ui.Stories.PeerStoriesView$8$$ExternalSyntheticLambda4
                @Override // java.lang.Runnable
                public final void run() {
                    this.f$0.lambda$onCreate$12();
                }
            });
            storyRecorder.setOnPrepareCloseListener(new Utilities.Callback4() { // from class: org.telegram.ui.Stories.PeerStoriesView$8$$ExternalSyntheticLambda5
                @Override // org.telegram.messenger.Utilities.Callback4
                public final void run(Object obj, Object obj2, Object obj3, Object obj4) {
                    this.f$0.lambda$onCreate$15(sharedResources, (Long) obj, (Runnable) obj2, (Boolean) obj3, (Long) obj4);
                }
            });
        }

        /* JADX INFO: Access modifiers changed from: private */
        public /* synthetic */ void lambda$onCreate$12() {
            PeerStoriesView peerStoriesView = PeerStoriesView.this;
            peerStoriesView.editOpened = true;
            peerStoriesView.setActive(false);
        }

        /* JADX INFO: Access modifiers changed from: private */
        public /* synthetic */ void lambda$onCreate$15(SharedResources sharedResources, Long l, final Runnable runnable, Boolean bool, Long l2) {
            final long jCurrentTimeMillis = System.currentTimeMillis();
            PeerStoriesView peerStoriesView = PeerStoriesView.this;
            VideoPlayerSharedScope videoPlayerSharedScope = peerStoriesView.playerSharedScope;
            StoryViewer.VideoPlayerHolder videoPlayerHolder = videoPlayerSharedScope.player;
            if (videoPlayerHolder == null) {
                peerStoriesView.delegate.setPopupIsVisible(false);
                PeerStoriesView.this.setActive(true);
                PeerStoriesView peerStoriesView2 = PeerStoriesView.this;
                peerStoriesView2.editOpened = false;
                peerStoriesView2.onImageReceiverThumbLoaded = new Runnable() { // from class: org.telegram.ui.Stories.PeerStoriesView$8$$ExternalSyntheticLambda10
                    @Override // java.lang.Runnable
                    public final void run() {
                        PeerStoriesView.AnonymousClass8.$r8$lambda$44cxg47EmO7wGipUuO4ZEp56bsM(runnable);
                    }
                };
                if (bool.booleanValue()) {
                    PeerStoriesView.this.updatePosition();
                }
                AndroidUtilities.runOnUIThread(runnable, 400L);
                return;
            }
            videoPlayerHolder.firstFrameRendered = false;
            videoPlayerSharedScope.firstFrameRendered = false;
            videoPlayerHolder.setOnReadyListener(new Runnable() { // from class: org.telegram.ui.Stories.PeerStoriesView$8$$ExternalSyntheticLambda11
                @Override // java.lang.Runnable
                public final void run() {
                    PeerStoriesView.AnonymousClass8.$r8$lambda$93s1RGoVHBMKE1kd2Jx9tC2wRPw(runnable, jCurrentTimeMillis);
                }
            });
            PeerStoriesView.this.delegate.setPopupIsVisible(false);
            if (PeerStoriesView.this.muteIconView != null) {
                PeerStoriesView.this.muteIconView.setAnimation(sharedResources.muteDrawable);
            }
            PeerStoriesView.this.setActive(((PeerStoriesView.this.videoDuration <= 0 || l.longValue() <= PeerStoriesView.this.videoDuration - 1400) ? l : 0L).longValue(), true);
            PeerStoriesView.this.editOpened = false;
            AndroidUtilities.runOnUIThread(runnable, 400L);
            if (bool.booleanValue()) {
                PeerStoriesView.this.updatePosition();
            }
        }

        public static /* synthetic */ void $r8$lambda$44cxg47EmO7wGipUuO4ZEp56bsM(Runnable runnable) {
            AndroidUtilities.cancelRunOnUIThread(runnable);
            AndroidUtilities.runOnUIThread(runnable);
        }

        public static /* synthetic */ void $r8$lambda$93s1RGoVHBMKE1kd2Jx9tC2wRPw(Runnable runnable, long j) {
            AndroidUtilities.cancelRunOnUIThread(runnable);
            AndroidUtilities.runOnUIThread(runnable, Math.max(0L, 32 - (System.currentTimeMillis() - j)));
        }

        /* JADX INFO: Access modifiers changed from: private */
        public /* synthetic */ void lambda$onCreate$27(Context context, final TL_stories.StoryItem storyItem, final StoryViewer storyViewer, final SharedResources sharedResources, View view) {
            File path = PeerStoriesView.this.currentStory.getPath();
            if (path == null || !path.exists()) {
                PeerStoriesView.this.showDownloadAlert();
                return;
            }
            final Activity activityFindActivity = AndroidUtilities.findActivity(context);
            if (activityFindActivity == null) {
                return;
            }
            this.edit = true;
            CustomPopupMenu customPopupMenu = PeerStoriesView.this.popupMenu;
            if (customPopupMenu != null) {
                customPopupMenu.dismiss();
            }
            Runnable runnable = new Runnable() { // from class: org.telegram.ui.Stories.PeerStoriesView$8$$ExternalSyntheticLambda53
                @Override // java.lang.Runnable
                public final void run() {
                    this.f$0.lambda$onCreate$26(activityFindActivity, storyItem, storyViewer, sharedResources);
                }
            };
            if (PeerStoriesView.this.delegate.releasePlayer(runnable)) {
                return;
            }
            runnable.run();
        }

        /* JADX INFO: Access modifiers changed from: private */
        public /* synthetic */ void lambda$onCreate$26(Activity activity, final TL_stories.StoryItem storyItem, StoryViewer storyViewer, final SharedResources sharedResources) {
            StoryViewer.VideoPlayerHolder videoPlayerHolder;
            StoryRecorder storyRecorder = StoryRecorder.getInstance(activity, PeerStoriesView.this.currentAccount);
            VideoPlayerSharedScope videoPlayerSharedScope = PeerStoriesView.this.playerSharedScope;
            long j = (videoPlayerSharedScope == null || (videoPlayerHolder = videoPlayerSharedScope.player) == null) ? 0L : videoPlayerHolder.currentPosition;
            StoryEntry storyEntryFromStoryItem = StoryEntry.fromStoryItem(PeerStoriesView.this.currentStory.getPath(), PeerStoriesView.this.currentStory.storyItem);
            storyEntryFromStoryItem.editStoryPeerId = PeerStoriesView.this.dialogId;
            storyEntryFromStoryItem.cover = StoryEntry.getCoverTime(PeerStoriesView.this.currentStory.storyItem);
            StoryEntry storyEntryCopy = storyEntryFromStoryItem.copy();
            storyEntryCopy.isEditingCover = true;
            final TL_stories.StoryItem storyItem2 = PeerStoriesView.this.currentStory.storyItem;
            storyEntryCopy.editingCoverDocument = storyItem2.media.document;
            storyEntryCopy.updateDocumentRef = new Utilities.Callback() { // from class: org.telegram.ui.Stories.PeerStoriesView$8$$ExternalSyntheticLambda1
                @Override // org.telegram.messenger.Utilities.Callback
                public final void run(Object obj) {
                    this.f$0.lambda$onCreate$21(storyItem2, storyItem, (Utilities.Callback) obj);
                }
            };
            if (PeerStoriesView.this.isBotsPreview()) {
                storyEntryCopy.botId = PeerStoriesView.this.dialogId;
                storyEntryCopy.editingBotPreview = MessagesController.toInputMedia(PeerStoriesView.this.currentStory.storyItem.media);
                StoriesController.StoriesList storiesList = storyViewer.storiesList;
                if (storiesList instanceof StoriesController.BotPreviewsList) {
                    storyEntryCopy.botLang = ((StoriesController.BotPreviewsList) storiesList).lang_code;
                }
            }
            storyRecorder.openEdit(StoryRecorder.SourceView.fromStoryViewer(storyViewer), storyEntryCopy, j, true);
            storyRecorder.setOnFullyOpenListener(new Runnable() { // from class: org.telegram.ui.Stories.PeerStoriesView$8$$ExternalSyntheticLambda2
                @Override // java.lang.Runnable
                public final void run() {
                    this.f$0.lambda$onCreate$22();
                }
            });
            storyRecorder.setOnPrepareCloseListener(new Utilities.Callback4() { // from class: org.telegram.ui.Stories.PeerStoriesView$8$$ExternalSyntheticLambda3
                @Override // org.telegram.messenger.Utilities.Callback4
                public final void run(Object obj, Object obj2, Object obj3, Object obj4) {
                    this.f$0.lambda$onCreate$25(sharedResources, (Long) obj, (Runnable) obj2, (Boolean) obj3, (Long) obj4);
                }
            });
        }

        /* JADX INFO: Access modifiers changed from: private */
        public /* synthetic */ void lambda$onCreate$21(final TL_stories.StoryItem storyItem, final TL_stories.StoryItem storyItem2, final Utilities.Callback callback) {
            final StoriesController.BotPreviewsList botPreviewsList;
            if ((storyItem instanceof StoriesController.BotPreview) && (botPreviewsList = ((StoriesController.BotPreview) storyItem).list) != null) {
                botPreviewsList.reload(new Runnable() { // from class: org.telegram.ui.Stories.PeerStoriesView$8$$ExternalSyntheticLambda12
                    @Override // java.lang.Runnable
                    public final void run() {
                        PeerStoriesView.AnonymousClass8.m17755$r8$lambda$_yNBXYv7gOPVoHtzsMoD1J64fI(botPreviewsList, storyItem2, callback);
                    }
                });
                return;
            }
            TL_stories.TL_stories_getStoriesByID tL_stories_getStoriesByID = new TL_stories.TL_stories_getStoriesByID();
            tL_stories_getStoriesByID.peer = MessagesController.getInstance(PeerStoriesView.this.currentAccount).getInputPeer(storyItem.dialogId);
            tL_stories_getStoriesByID.id.add(Integer.valueOf(storyItem.id));
            ConnectionsManager.getInstance(PeerStoriesView.this.currentAccount).sendRequest(tL_stories_getStoriesByID, new RequestDelegate() { // from class: org.telegram.ui.Stories.PeerStoriesView$8$$ExternalSyntheticLambda13
                @Override // org.telegram.tgnet.RequestDelegate
                public final void run(TLObject tLObject, TLRPC.TL_error tL_error) {
                    this.f$0.lambda$onCreate$20(storyItem, callback, tLObject, tL_error);
                }
            });
        }

        /* JADX INFO: renamed from: $r8$lambda$_yN-BXYv7gOPVoHtzsMoD1J64fI, reason: not valid java name */
        public static /* synthetic */ void m17755$r8$lambda$_yNBXYv7gOPVoHtzsMoD1J64fI(StoriesController.BotPreviewsList botPreviewsList, TL_stories.StoryItem storyItem, Utilities.Callback callback) {
            TL_stories.StoryItem storyItem2;
            TLRPC.MessageMedia messageMedia;
            TLRPC.Document document;
            TLRPC.Document document2;
            for (int i = 0; i < botPreviewsList.messageObjects.size(); i++) {
                MessageObject messageObject = (MessageObject) botPreviewsList.messageObjects.get(i);
                if (messageObject != null && (storyItem2 = messageObject.storyItem) != null && (messageMedia = storyItem2.media) != null && (document = storyItem.media.document) != null && (document2 = messageMedia.document) != null && document2.id == document.id) {
                    callback.run(document2);
                    return;
                }
            }
            callback.run(null);
        }

        /* JADX INFO: Access modifiers changed from: private */
        public /* synthetic */ void lambda$onCreate$20(final TL_stories.StoryItem storyItem, final Utilities.Callback callback, final TLObject tLObject, TLRPC.TL_error tL_error) {
            AndroidUtilities.runOnUIThread(new Runnable() { // from class: org.telegram.ui.Stories.PeerStoriesView$8$$ExternalSyntheticLambda15
                @Override // java.lang.Runnable
                public final void run() {
                    this.f$0.lambda$onCreate$19(tLObject, storyItem, callback);
                }
            });
        }

        /* JADX INFO: Access modifiers changed from: private */
        public /* synthetic */ void lambda$onCreate$19(TLObject tLObject, TL_stories.StoryItem storyItem, Utilities.Callback callback) {
            if (tLObject instanceof TL_stories.TL_stories_stories) {
                TL_stories.TL_stories_stories tL_stories_stories = (TL_stories.TL_stories_stories) tLObject;
                MessagesController.getInstance(PeerStoriesView.this.currentAccount).putUsers(tL_stories_stories.users, false);
                MessagesController.getInstance(PeerStoriesView.this.currentAccount).putChats(tL_stories_stories.chats, false);
                for (int i = 0; i < tL_stories_stories.stories.size(); i++) {
                    if (tL_stories_stories.stories.get(i).id == storyItem.id) {
                        callback.run(tL_stories_stories.stories.get(i).media.document);
                        return;
                    }
                }
            }
            callback.run(null);
        }

        /* JADX INFO: Access modifiers changed from: private */
        public /* synthetic */ void lambda$onCreate$22() {
            PeerStoriesView peerStoriesView = PeerStoriesView.this;
            peerStoriesView.editOpened = true;
            peerStoriesView.setActive(false);
        }

        /* JADX INFO: Access modifiers changed from: private */
        public /* synthetic */ void lambda$onCreate$25(SharedResources sharedResources, Long l, final Runnable runnable, Boolean bool, Long l2) {
            final long jCurrentTimeMillis = System.currentTimeMillis();
            PeerStoriesView peerStoriesView = PeerStoriesView.this;
            VideoPlayerSharedScope videoPlayerSharedScope = peerStoriesView.playerSharedScope;
            StoryViewer.VideoPlayerHolder videoPlayerHolder = videoPlayerSharedScope.player;
            if (videoPlayerHolder == null) {
                peerStoriesView.delegate.setPopupIsVisible(false);
                PeerStoriesView.this.setActive(true);
                PeerStoriesView peerStoriesView2 = PeerStoriesView.this;
                peerStoriesView2.editOpened = false;
                peerStoriesView2.onImageReceiverThumbLoaded = new Runnable() { // from class: org.telegram.ui.Stories.PeerStoriesView$8$$ExternalSyntheticLambda8
                    @Override // java.lang.Runnable
                    public final void run() {
                        PeerStoriesView.AnonymousClass8.$r8$lambda$NV2WjvVVPH_8msYrDZ0Kte2fosg(runnable);
                    }
                };
                if (bool.booleanValue()) {
                    PeerStoriesView.this.updatePosition();
                }
                AndroidUtilities.runOnUIThread(runnable, 400L);
                return;
            }
            videoPlayerHolder.firstFrameRendered = false;
            videoPlayerSharedScope.firstFrameRendered = false;
            videoPlayerHolder.setOnReadyListener(new Runnable() { // from class: org.telegram.ui.Stories.PeerStoriesView$8$$ExternalSyntheticLambda9
                @Override // java.lang.Runnable
                public final void run() {
                    PeerStoriesView.AnonymousClass8.$r8$lambda$VU5a7tw08M0tpjO9YNLN2d5ydoQ(runnable, jCurrentTimeMillis);
                }
            });
            PeerStoriesView.this.delegate.setPopupIsVisible(false);
            if (PeerStoriesView.this.muteIconView != null) {
                PeerStoriesView.this.muteIconView.setAnimation(sharedResources.muteDrawable);
            }
            PeerStoriesView.this.setActive(((PeerStoriesView.this.videoDuration <= 0 || l.longValue() <= PeerStoriesView.this.videoDuration - 1400) ? l : 0L).longValue(), true);
            PeerStoriesView.this.editOpened = false;
            AndroidUtilities.runOnUIThread(runnable, 400L);
            if (bool.booleanValue()) {
                PeerStoriesView.this.updatePosition();
            }
        }

        public static /* synthetic */ void $r8$lambda$NV2WjvVVPH_8msYrDZ0Kte2fosg(Runnable runnable) {
            AndroidUtilities.cancelRunOnUIThread(runnable);
            AndroidUtilities.runOnUIThread(runnable);
        }

        public static /* synthetic */ void $r8$lambda$VU5a7tw08M0tpjO9YNLN2d5ydoQ(Runnable runnable, long j) {
            AndroidUtilities.cancelRunOnUIThread(runnable);
            AndroidUtilities.runOnUIThread(runnable, Math.max(0L, 32 - (System.currentTimeMillis() - j)));
        }

        /* JADX INFO: Access modifiers changed from: private */
        public /* synthetic */ void lambda$onCreate$29(final TL_stories.StoryItem storyItem, final boolean z, final Theme.ResourcesProvider resourcesProvider, View view) {
            ArrayList arrayList = new ArrayList();
            arrayList.add(storyItem);
            MessagesController.getInstance(PeerStoriesView.this.currentAccount).getStoriesController().updateStoriesPinned(PeerStoriesView.this.dialogId, arrayList, z, new Utilities.Callback() { // from class: org.telegram.ui.Stories.PeerStoriesView$8$$ExternalSyntheticLambda45
                @Override // org.telegram.messenger.Utilities.Callback
                public final void run(Object obj) {
                    this.f$0.lambda$onCreate$28(storyItem, z, resourcesProvider, (Boolean) obj);
                }
            });
            CustomPopupMenu customPopupMenu = PeerStoriesView.this.popupMenu;
            if (customPopupMenu != null) {
                customPopupMenu.dismiss();
            }
        }

        /* JADX INFO: Access modifiers changed from: private */
        public /* synthetic */ void lambda$onCreate$28(TL_stories.StoryItem storyItem, boolean z, Theme.ResourcesProvider resourcesProvider, Boolean bool) {
            if (bool.booleanValue()) {
                storyItem.pinned = z;
                PeerStoriesView peerStoriesView = PeerStoriesView.this;
                if (peerStoriesView.isSelf) {
                    BulletinFactory.of(peerStoriesView.storyContainer, resourcesProvider).createSimpleBulletin(z ? R.raw.contact_check : R.raw.chats_archived, LocaleController.getString(z ? R.string.StoryPinnedToProfile : R.string.StoryArchivedFromProfile)).show();
                    return;
                } else if (z) {
                    BulletinFactory.of(peerStoriesView.storyContainer, resourcesProvider).createSimpleBulletin(R.raw.contact_check, LocaleController.getString(R.string.StoryPinnedToPosts), LocaleController.getString(R.string.StoryPinnedToPostsDescription)).show();
                    return;
                } else {
                    BulletinFactory.of(peerStoriesView.storyContainer, resourcesProvider).createSimpleBulletin(R.raw.chats_archived, LocaleController.getString(R.string.StoryUnpinnedFromPosts)).show();
                    return;
                }
            }
            BulletinFactory.of(PeerStoriesView.this.storyContainer, resourcesProvider).createSimpleBulletin(R.raw.error, LocaleController.getString(R.string.UnknownError)).show();
        }

        /* JADX INFO: Access modifiers changed from: private */
        public /* synthetic */ void lambda$onCreate$30(View view) {
            PeerStoriesView.this.saveToGallery();
            CustomPopupMenu customPopupMenu = PeerStoriesView.this.popupMenu;
            if (customPopupMenu != null) {
                customPopupMenu.dismiss();
            }
        }

        /* JADX INFO: Access modifiers changed from: private */
        public /* synthetic */ void lambda$onCreate$31(View view) {
            AndroidUtilities.addToClipboard(PeerStoriesView.this.currentStory.createLink());
            PeerStoriesView.this.onLinkCopied();
            CustomPopupMenu customPopupMenu = PeerStoriesView.this.popupMenu;
            if (customPopupMenu != null) {
                customPopupMenu.dismiss();
            }
        }

        /* JADX INFO: Access modifiers changed from: private */
        public /* synthetic */ void lambda$onCreate$32(View view) {
            PeerStoriesView.this.shareStory(false);
            CustomPopupMenu customPopupMenu = PeerStoriesView.this.popupMenu;
            if (customPopupMenu != null) {
                customPopupMenu.dismiss();
            }
        }

        /* JADX INFO: Access modifiers changed from: private */
        public /* synthetic */ void lambda$onCreate$33(boolean z, View view) {
            LivePlayer livePlayer = LivePlayer.recording;
            if (livePlayer != null) {
                livePlayer.setMuted(!z);
            }
            CustomPopupMenu customPopupMenu = PeerStoriesView.this.popupMenu;
            if (customPopupMenu != null) {
                customPopupMenu.dismiss();
            }
        }

        /* JADX INFO: Access modifiers changed from: private */
        public /* synthetic */ void lambda$onCreate$34(View view) {
            LivePlayer livePlayer = LivePlayer.recording;
            if (livePlayer != null) {
                livePlayer.switchCamera();
            }
            CustomPopupMenu customPopupMenu = PeerStoriesView.this.popupMenu;
            if (customPopupMenu != null) {
                customPopupMenu.dismiss();
            }
        }

        /* JADX INFO: Access modifiers changed from: private */
        public /* synthetic */ void lambda$onCreate$35(StoryViewer storyViewer, View view) {
            CustomPopupMenu customPopupMenu = PeerStoriesView.this.popupMenu;
            if (customPopupMenu != null) {
                customPopupMenu.dismiss();
            }
            if (storyViewer != null) {
                storyViewer.switchToPip();
            }
        }

        /* JADX INFO: Access modifiers changed from: private */
        public /* synthetic */ void lambda$onCreate$39(Theme.ResourcesProvider resourcesProvider, StoryViewer storyViewer, View view) {
            CustomPopupMenu customPopupMenu = PeerStoriesView.this.popupMenu;
            if (customPopupMenu != null) {
                customPopupMenu.dismiss();
            }
            StoryPrivacyBottomSheet storyPrivacyBottomSheetIsEdit = new StoryPrivacyBottomSheet(PeerStoriesView.this.getContext(), 86400, resourcesProvider).setLive(true).setPeer(MessagesController.getInstance(PeerStoriesView.this.currentAccount).getInputPeer(PeerStoriesView.this.dialogId)).setLiveSettings(true).allowCover(false).setCount(1).isEdit(false);
            LivePlayer livePlayer = storyViewer.livePlayer;
            boolean z = livePlayer != null && livePlayer.areMessagesEnabled();
            boolean zAllowScreenshots = PeerStoriesView.this.currentStory.allowScreenshots();
            TL_stories.StoryItem storyItem = PeerStoriesView.this.currentStory.storyItem;
            boolean z2 = storyItem != null && storyItem.pinned;
            LivePlayer livePlayer2 = storyViewer.livePlayer;
            final StoryPrivacyBottomSheet storyPrivacyBottomSheet = storyPrivacyBottomSheetIsEdit.set(z, zAllowScreenshots, z2, livePlayer2 == null ? 0 : (int) livePlayer2.getSendPaidMessagesStars());
            storyPrivacyBottomSheet.whenSelectedRules(new StoryPrivacyBottomSheet.DoneCallback() { // from class: org.telegram.ui.Stories.PeerStoriesView$8$$ExternalSyntheticLambda44
                @Override // org.telegram.ui.Stories.recorder.StoryPrivacyBottomSheet.DoneCallback
                public final void done(StoryPrivacyBottomSheet.StoryPrivacy storyPrivacy, boolean z3, boolean z4, boolean z5, boolean z6, TLRPC.InputPeer inputPeer, int i, Runnable runnable, Runnable runnable2) {
                    this.f$0.lambda$onCreate$38(storyPrivacyBottomSheet, storyPrivacy, z3, z4, z5, z6, inputPeer, i, runnable, runnable2);
                }
            }, false);
            storyPrivacyBottomSheet.show();
        }

        /* JADX INFO: Access modifiers changed from: private */
        public /* synthetic */ void lambda$onCreate$38(final StoryPrivacyBottomSheet storyPrivacyBottomSheet, StoryPrivacyBottomSheet.StoryPrivacy storyPrivacy, boolean z, boolean z2, boolean z3, boolean z4, TLRPC.InputPeer inputPeer, int i, Runnable runnable, Runnable runnable2) {
            PeerStoriesView peerStoriesView = PeerStoriesView.this;
            TL_stories.StoryItem storyItem = peerStoriesView.currentStory.storyItem;
            if ((storyItem != null && storyItem.pinned) != z3) {
                MessagesController.getInstance(peerStoriesView.currentAccount).getStoriesController().updateStoriesPinned(PeerStoriesView.this.dialogId, PeerStoriesView.this.storyItems, z3, null);
            }
            TL_stories.StoryItem storyItem2 = PeerStoriesView.this.currentStory.storyItem;
            if (storyItem2 != null) {
                TLRPC.MessageMedia messageMedia = storyItem2.media;
                if (messageMedia instanceof TLRPC.TL_messageMediaVideoStream) {
                    TLRPC.InputGroupCall inputGroupCall = ((TLRPC.TL_messageMediaVideoStream) messageMedia).call;
                    TL_phone.toggleGroupCallSettings togglegroupcallsettings = new TL_phone.toggleGroupCallSettings();
                    togglegroupcallsettings.call = inputGroupCall;
                    togglegroupcallsettings.messages_enabled = Boolean.valueOf(z);
                    togglegroupcallsettings.send_paid_messages_stars = Long.valueOf(i);
                    ConnectionsManager.getInstance(PeerStoriesView.this.currentAccount).sendRequest(togglegroupcallsettings, new RequestDelegate() { // from class: org.telegram.ui.Stories.PeerStoriesView$8$$ExternalSyntheticLambda0
                        @Override // org.telegram.tgnet.RequestDelegate
                        public final void run(TLObject tLObject, TLRPC.TL_error tL_error) {
                            this.f$0.lambda$onCreate$37(storyPrivacyBottomSheet, tLObject, tL_error);
                        }
                    });
                }
            }
        }

        /* JADX INFO: Access modifiers changed from: private */
        public /* synthetic */ void lambda$onCreate$37(final StoryPrivacyBottomSheet storyPrivacyBottomSheet, TLObject tLObject, TLRPC.TL_error tL_error) {
            if (tLObject instanceof TLRPC.Updates) {
                MessagesController.getInstance(PeerStoriesView.this.currentAccount).processUpdates((TLRPC.Updates) tLObject, false);
            }
            AndroidUtilities.runOnUIThread(new Runnable() { // from class: org.telegram.ui.Stories.PeerStoriesView$8$$ExternalSyntheticLambda7
                @Override // java.lang.Runnable
                public final void run() {
                    storyPrivacyBottomSheet.dismiss();
                }
            });
        }

        /* JADX INFO: Access modifiers changed from: private */
        public /* synthetic */ void lambda$onCreate$41(Theme.ResourcesProvider resourcesProvider, final StoryViewer storyViewer, View view) {
            CustomPopupMenu customPopupMenu = PeerStoriesView.this.popupMenu;
            if (customPopupMenu != null) {
                customPopupMenu.dismiss();
            }
            new AlertDialog.Builder(PeerStoriesView.this.getContext(), resourcesProvider).setTitle(LocaleController.getString(R.string.LiveStoryEndAlertTitle)).setMessage(LocaleController.getString(R.string.LiveStoryEndAlertText)).setPositiveButton(LocaleController.getString(R.string.LiveStoryEndAlertButton), new AlertDialog.OnButtonClickListener() { // from class: org.telegram.ui.Stories.PeerStoriesView$8$$ExternalSyntheticLambda55
                @Override // org.telegram.ui.ActionBar.AlertDialog.OnButtonClickListener
                public final void onClick(AlertDialog alertDialog, int i) {
                    this.f$0.lambda$onCreate$40(storyViewer, alertDialog, i);
                }
            }).setNegativeButton(LocaleController.getString(R.string.Cancel), null).makeRed(-1).show();
        }

        /* JADX INFO: Access modifiers changed from: private */
        public /* synthetic */ void lambda$onCreate$40(StoryViewer storyViewer, AlertDialog alertDialog, int i) {
            LivePlayer livePlayer = storyViewer.livePlayer;
            if (livePlayer != null) {
                livePlayer.end();
            } else {
                PeerStoriesView.this.deleteStory();
            }
        }

        /* JADX INFO: Access modifiers changed from: private */
        public /* synthetic */ void lambda$onCreate$42(View view) {
            PeerStoriesView.this.deleteStory();
            CustomPopupMenu customPopupMenu = PeerStoriesView.this.popupMenu;
            if (customPopupMenu != null) {
                customPopupMenu.dismiss();
            }
        }

        /* JADX INFO: Access modifiers changed from: private */
        public /* synthetic */ void lambda$onCreate$43(String str, Theme.ResourcesProvider resourcesProvider, TLObject tLObject, String str2, View view) {
            MessagesController.getNotificationsSettings(PeerStoriesView.this.currentAccount).edit().putBoolean(NotificationsSettingsFacade.PROPERTY_STORIES_NOTIFY + str, false).apply();
            NotificationsController.getInstance(PeerStoriesView.this.currentAccount).updateServerNotificationsSettings(PeerStoriesView.this.dialogId, 0L);
            BulletinFactory.of(PeerStoriesView.this.storyContainer, resourcesProvider).createUsersBulletin(Arrays.asList(tLObject), AndroidUtilities.replaceTags(LocaleController.formatString("NotificationsStoryMutedHint", R.string.NotificationsStoryMutedHint, str2))).setTag(2).show();
            CustomPopupMenu customPopupMenu = PeerStoriesView.this.popupMenu;
            if (customPopupMenu != null) {
                customPopupMenu.dismiss();
            }
        }

        /* JADX INFO: Access modifiers changed from: private */
        public /* synthetic */ void lambda$onCreate$44(String str, Theme.ResourcesProvider resourcesProvider, TLObject tLObject, String str2, View view) {
            MessagesController.getNotificationsSettings(PeerStoriesView.this.currentAccount).edit().putBoolean(NotificationsSettingsFacade.PROPERTY_STORIES_NOTIFY + str, true).apply();
            NotificationsController.getInstance(PeerStoriesView.this.currentAccount).updateServerNotificationsSettings(PeerStoriesView.this.dialogId, 0L);
            BulletinFactory.of(PeerStoriesView.this.storyContainer, resourcesProvider).createUsersBulletin(Arrays.asList(tLObject), AndroidUtilities.replaceTags(LocaleController.formatString("NotificationsStoryUnmutedHint", R.string.NotificationsStoryUnmutedHint, str2))).setTag(2).show();
            CustomPopupMenu customPopupMenu = PeerStoriesView.this.popupMenu;
            if (customPopupMenu != null) {
                customPopupMenu.dismiss();
            }
        }

        /* JADX INFO: Access modifiers changed from: private */
        public /* synthetic */ void lambda$onCreate$45(View view) {
            MediaDataController.getInstance(PeerStoriesView.this.currentAccount).removePeer(PeerStoriesView.this.dialogId);
            PeerStoriesView peerStoriesView = PeerStoriesView.this;
            peerStoriesView.storiesController.toggleHidden(peerStoriesView.dialogId, true, false, true);
            CustomPopupMenu customPopupMenu = PeerStoriesView.this.popupMenu;
            if (customPopupMenu != null) {
                customPopupMenu.dismiss();
            }
        }

        /* JADX INFO: Access modifiers changed from: private */
        public /* synthetic */ void lambda$onCreate$46(View view) {
            PeerStoriesView peerStoriesView = PeerStoriesView.this;
            peerStoriesView.toggleArchiveForStory(peerStoriesView.dialogId);
            CustomPopupMenu customPopupMenu = PeerStoriesView.this.popupMenu;
            if (customPopupMenu != null) {
                customPopupMenu.dismiss();
            }
        }

        /* JADX INFO: Access modifiers changed from: private */
        public /* synthetic */ void lambda$onCreate$47(View view) {
            PeerStoriesView peerStoriesView = PeerStoriesView.this;
            peerStoriesView.toggleArchiveForStory(peerStoriesView.dialogId);
            CustomPopupMenu customPopupMenu = PeerStoriesView.this.popupMenu;
            if (customPopupMenu != null) {
                customPopupMenu.dismiss();
            }
        }

        /* JADX INFO: Access modifiers changed from: private */
        public /* synthetic */ void lambda$onCreate$48(StoryViewer storyViewer, View view) {
            CustomPopupMenu customPopupMenu = PeerStoriesView.this.popupMenu;
            if (customPopupMenu != null) {
                customPopupMenu.dismiss();
            }
            if (storyViewer != null) {
                storyViewer.switchToPip();
            }
        }

        /* JADX INFO: Access modifiers changed from: private */
        public /* synthetic */ void lambda$onCreate$49(View view) {
            PeerStoriesView.this.saveToGallery();
            CustomPopupMenu customPopupMenu = PeerStoriesView.this.popupMenu;
            if (customPopupMenu != null) {
                customPopupMenu.dismiss();
            }
        }

        /* JADX INFO: Access modifiers changed from: private */
        public /* synthetic */ void lambda$onCreate$50(View view) {
            AndroidUtilities.addToClipboard(PeerStoriesView.this.currentStory.createLink());
            PeerStoriesView.this.onLinkCopied();
            CustomPopupMenu customPopupMenu = PeerStoriesView.this.popupMenu;
            if (customPopupMenu != null) {
                customPopupMenu.dismiss();
            }
        }

        /* JADX INFO: Access modifiers changed from: private */
        public /* synthetic */ void lambda$onCreate$51(View view) {
            PeerStoriesView.this.shareStory(false);
            CustomPopupMenu customPopupMenu = PeerStoriesView.this.popupMenu;
            if (customPopupMenu != null) {
                customPopupMenu.dismiss();
            }
        }

        /* JADX INFO: Access modifiers changed from: private */
        public /* synthetic */ void lambda$onCreate$52(View view) {
            PeerStoriesView peerStoriesView = PeerStoriesView.this;
            peerStoriesView.currentStory.storyItem.translated = false;
            StoriesStorage storiesStorage = MessagesController.getInstance(peerStoriesView.currentAccount).getStoriesController().getStoriesStorage();
            TL_stories.StoryItem storyItem = PeerStoriesView.this.currentStory.storyItem;
            storiesStorage.updateStoryItem(storyItem.dialogId, storyItem);
            PeerStoriesView.this.cancelTextSelection();
            PeerStoriesView.this.updatePosition();
            CustomPopupMenu customPopupMenu = PeerStoriesView.this.popupMenu;
            if (customPopupMenu != null) {
                customPopupMenu.dismiss();
            }
        }

        /* JADX INFO: Access modifiers changed from: private */
        public /* synthetic */ void lambda$onCreate$55(View view) {
            PeerStoriesView peerStoriesView = PeerStoriesView.this;
            peerStoriesView.currentStory.storyItem.translated = true;
            peerStoriesView.cancelTextSelection();
            Delegate delegate = PeerStoriesView.this.delegate;
            if (delegate != null) {
                delegate.setTranslating(true);
            }
            StoriesStorage storiesStorage = MessagesController.getInstance(PeerStoriesView.this.currentAccount).getStoriesController().getStoriesStorage();
            TL_stories.StoryItem storyItem = PeerStoriesView.this.currentStory.storyItem;
            storiesStorage.updateStoryItem(storyItem.dialogId, storyItem);
            final long jCurrentTimeMillis = System.currentTimeMillis();
            final Runnable runnable = new Runnable() { // from class: org.telegram.ui.Stories.PeerStoriesView$8$$ExternalSyntheticLambda57
                @Override // java.lang.Runnable
                public final void run() {
                    this.f$0.lambda$onCreate$53();
                }
            };
            MessagesController.getInstance(PeerStoriesView.this.currentAccount).getTranslateController().translateStory(PeerStoriesView.this.currentStory.storyItem, new Runnable() { // from class: org.telegram.ui.Stories.PeerStoriesView$8$$ExternalSyntheticLambda58
                @Override // java.lang.Runnable
                public final void run() {
                    AndroidUtilities.runOnUIThread(runnable, Math.max(0L, 500 - (System.currentTimeMillis() - jCurrentTimeMillis)));
                }
            });
            PeerStoriesView.this.updatePosition();
            PeerStoriesView peerStoriesView2 = PeerStoriesView.this;
            peerStoriesView2.checkBlackoutMode = true;
            peerStoriesView2.storyCaptionView.expand(true);
            CustomPopupMenu customPopupMenu = PeerStoriesView.this.popupMenu;
            if (customPopupMenu != null) {
                customPopupMenu.dismiss();
            }
        }

        /* JADX INFO: Access modifiers changed from: private */
        public /* synthetic */ void lambda$onCreate$53() {
            Delegate delegate = PeerStoriesView.this.delegate;
            if (delegate != null) {
                delegate.setTranslating(false);
            }
            PeerStoriesView.this.updatePosition();
            PeerStoriesView peerStoriesView = PeerStoriesView.this;
            peerStoriesView.checkBlackoutMode = true;
            peerStoriesView.storyCaptionView.expand(true);
        }

        /* JADX INFO: Access modifiers changed from: private */
        public /* synthetic */ void lambda$onCreate$57(final StoryViewer storyViewer, Theme.ResourcesProvider resourcesProvider, View view) {
            if (storyViewer != null) {
                storyViewer.setOverlayVisible(true);
            }
            int i = PeerStoriesView.this.currentAccount;
            Context context = PeerStoriesView.this.getContext();
            PeerStoriesView peerStoriesView = PeerStoriesView.this;
            ReportBottomSheet.openStory(i, context, peerStoriesView.currentStory.storyItem, BulletinFactory.of(peerStoriesView.storyContainer, resourcesProvider), resourcesProvider, new Utilities.Callback() { // from class: org.telegram.ui.Stories.PeerStoriesView$8$$ExternalSyntheticLambda54
                @Override // org.telegram.messenger.Utilities.Callback
                public final void run(Object obj) {
                    PeerStoriesView.AnonymousClass8.$r8$lambda$GWFre759WXK6QooStt637D0eyLs(storyViewer, (Boolean) obj);
                }
            });
            CustomPopupMenu customPopupMenu = PeerStoriesView.this.popupMenu;
            if (customPopupMenu != null) {
                customPopupMenu.dismiss();
            }
        }

        public static /* synthetic */ void $r8$lambda$GWFre759WXK6QooStt637D0eyLs(StoryViewer storyViewer, Boolean bool) {
            if (storyViewer != null) {
                storyViewer.setOverlayVisible(false);
            }
        }

        /* JADX INFO: Access modifiers changed from: private */
        public /* synthetic */ void lambda$onCreate$58(StoryContainsEmojiButton storyContainsEmojiButton, View view) {
            Delegate delegate;
            EmojiPacksAlert alert = storyContainsEmojiButton.getAlert();
            if (alert == null || (delegate = PeerStoriesView.this.delegate) == null) {
                return;
            }
            delegate.showDialog(alert);
            PeerStoriesView.this.popupMenu.dismiss();
        }

        @Override // org.telegram.ui.Components.CustomPopupMenu
        protected void onDismissed() {
            if (!this.edit && !this.val$popupStillVisible[0]) {
                AndroidUtilities.runOnUIThread(new Runnable() { // from class: org.telegram.ui.Stories.PeerStoriesView$8$$ExternalSyntheticLambda43
                    @Override // java.lang.Runnable
                    public final void run() {
                        this.f$0.lambda$onDismissed$59();
                    }
                });
            }
            PeerStoriesView peerStoriesView = PeerStoriesView.this;
            peerStoriesView.popupMenu = null;
            peerStoriesView.editStoryItem = null;
        }

        /* JADX INFO: Access modifiers changed from: private */
        public /* synthetic */ void lambda$onDismissed$59() {
            PeerStoriesView.this.delegate.setPopupIsVisible(false);
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    /* JADX WARN: Code duplicated, block: B:34:0x0101  */
    /* JADX WARN: Code duplicated, block: B:35:0x010a  */
    /* JADX WARN: Code duplicated, block: B:38:0x013e  */
    public /* synthetic */ void lambda$new$10(View view) {
        SpannableStringBuilder spannableStringBuilderReplaceTags;
        boolean z;
        CharSequence charSequenceReplaceEmoji;
        HintView2 hintView2;
        int measuredWidth;
        StoryPrivacyBottomSheet.StoryPrivacy storyPrivacy;
        TL_stories.StoryItem storyItem = this.currentStory.storyItem;
        if (storyItem == null) {
            return;
        }
        if (this.isSelf) {
            if (storyItem.privacy.isEmpty()) {
                storyPrivacy = new StoryPrivacyBottomSheet.StoryPrivacy(3, this.currentAccount, new ArrayList());
            } else {
                storyPrivacy = new StoryPrivacyBottomSheet.StoryPrivacy(this.currentAccount, storyItem.privacy);
            }
            editPrivacy(storyPrivacy, storyItem);
            return;
        }
        if (this.privacyHint == null) {
            HintView2 onHiddenListener = new HintView2(getContext(), 1).setMultilineText(true).setTextAlign(Layout.Alignment.ALIGN_CENTER).setOnHiddenListener(new Runnable() { // from class: org.telegram.ui.Stories.PeerStoriesView$$ExternalSyntheticLambda36
                @Override // java.lang.Runnable
                public final void run() {
                    this.f$0.lambda$new$9();
                }
            });
            this.privacyHint = onHiddenListener;
            onHiddenListener.setPadding(AndroidUtilities.dp(8.0f), 0, AndroidUtilities.dp(8.0f), 0);
            this.storyContainer.addView(this.privacyHint, LayoutHelper.createFrame(-1, 60.0f, 55, 0.0f, 52.0f, 0.0f, 0.0f));
        }
        TLRPC.User user = MessagesController.getInstance(this.currentAccount).getUser(Long.valueOf(this.dialogId));
        if (user == null) {
            return;
        }
        String strSubstring = user.first_name;
        int iIndexOf = strSubstring.indexOf(32);
        if (iIndexOf > 0) {
            strSubstring = strSubstring.substring(0, iIndexOf);
        }
        if (storyItem.close_friends) {
            this.privacyHint.setInnerPadding(15.0f, 8.0f, 15.0f, 8.0f);
            spannableStringBuilderReplaceTags = AndroidUtilities.replaceTags(LocaleController.formatString("StoryCloseFriendsHint", R.string.StoryCloseFriendsHint, strSubstring));
        } else {
            if (storyItem.contacts) {
                this.privacyHint.setInnerPadding(11.0f, 6.0f, 11.0f, 7.0f);
                spannableStringBuilderReplaceTags = AndroidUtilities.replaceTags(LocaleController.formatString("StoryContactsHint", R.string.StoryContactsHint, strSubstring));
                z = false;
            } else {
                if (!storyItem.selected_contacts) {
                    return;
                }
                this.privacyHint.setInnerPadding(15.0f, 8.0f, 15.0f, 8.0f);
                spannableStringBuilderReplaceTags = AndroidUtilities.replaceTags(LocaleController.formatString("StorySelectedContactsHint", R.string.StorySelectedContactsHint, strSubstring));
            }
            charSequenceReplaceEmoji = Emoji.replaceEmoji(spannableStringBuilderReplaceTags, this.privacyHint.getTextPaint().getFontMetricsInt(), false);
            hintView2 = this.privacyHint;
            if (z) {
                measuredWidth = HintView2.cutInFancyHalf(charSequenceReplaceEmoji, hintView2.getTextPaint());
            } else {
                measuredWidth = this.storyContainer.getMeasuredWidth();
            }
            hintView2.setMaxWidthPx(measuredWidth);
            this.privacyHint.setText(charSequenceReplaceEmoji);
            this.privacyHint.setJoint(1.0f, (-(this.storyContainer.getWidth() - this.privacyButton.getCenterX())) / AndroidUtilities.density);
            this.delegate.setIsHintVisible(true);
            if (this.privacyHint.shown()) {
                BotWebViewVibrationEffect.IMPACT_LIGHT.vibrate();
            }
            this.privacyHint.show();
        }
        z = true;
        charSequenceReplaceEmoji = Emoji.replaceEmoji(spannableStringBuilderReplaceTags, this.privacyHint.getTextPaint().getFontMetricsInt(), false);
        hintView2 = this.privacyHint;
        if (z) {
            measuredWidth = HintView2.cutInFancyHalf(charSequenceReplaceEmoji, hintView2.getTextPaint());
        } else {
            measuredWidth = this.storyContainer.getMeasuredWidth();
        }
        hintView2.setMaxWidthPx(measuredWidth);
        this.privacyHint.setText(charSequenceReplaceEmoji);
        this.privacyHint.setJoint(1.0f, (-(this.storyContainer.getWidth() - this.privacyButton.getCenterX())) / AndroidUtilities.density);
        this.delegate.setIsHintVisible(true);
        if (this.privacyHint.shown()) {
            BotWebViewVibrationEffect.IMPACT_LIGHT.vibrate();
        }
        this.privacyHint.show();
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$new$9() {
        this.delegate.setIsHintVisible(false);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$new$11(StoryViewer storyViewer, View view) {
        if (this.currentStory.hasSound()) {
            storyViewer.toggleSilentMode();
            if (storyViewer.soundEnabled()) {
                MessagesController.getGlobalMainSettings().edit().putInt("taptostorysoundhint", 3).apply();
                return;
            }
            return;
        }
        showNoSoundHint(true);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void createStealthModeItem(ActionBarPopupWindow.ActionBarPopupWindowLayout actionBarPopupWindowLayout) {
        if (isBotsPreview() || this.currentStory.isLive) {
            return;
        }
        ActionBarMenuItem.addItem(actionBarPopupWindowLayout, R.drawable.msg_stories_stealth2, LocaleController.getString(UserConfig.getInstance(this.currentAccount).isPremiumReal() ? R.string.StealthModeButton : R.string.GhostModeToggle), false, this.resourcesProvider).setOnClickListener(new View.OnClickListener() { // from class: org.telegram.ui.Stories.PeerStoriesView$$ExternalSyntheticLambda58
            @Override // android.view.View.OnClickListener
            public final void onClick(View view) {
                this.f$0.lambda$createStealthModeItem$12(view);
            }
        });
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$createStealthModeItem$12(View view) {
        if (this.stealthModeIsActive) {
            StealthModeAlert.showStealthModeEnabledBulletin();
        } else {
            this.delegate.showDialog(new StealthModeAlert(getContext(), getY() + this.storyContainer.getY(), 0, this.resourcesProvider));
        }
        CustomPopupMenu customPopupMenu = this.popupMenu;
        if (customPopupMenu != null) {
            customPopupMenu.dismiss();
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void createQualityItem(ActionBarPopupWindow.ActionBarPopupWindowLayout actionBarPopupWindowLayout) {
        final boolean z = MessagesController.getInstance(this.currentAccount).storyQualityFull;
        if (UserConfig.getInstance(this.currentAccount).isPremium()) {
            ActionBarMenuItem.addItem(actionBarPopupWindowLayout, z ? R.drawable.menu_quality_sd : R.drawable.menu_quality_hd, LocaleController.getString(z ? R.string.StoryQualityDecrease : R.string.StoryQualityIncrease), false, this.resourcesProvider).setOnClickListener(new View.OnClickListener() { // from class: org.telegram.ui.Stories.PeerStoriesView$$ExternalSyntheticLambda56
                @Override // android.view.View.OnClickListener
                public final void onClick(View view) {
                    this.f$0.lambda$createQualityItem$14(z, view);
                }
            });
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$createQualityItem$14(boolean z, View view) {
        MessagesController.getInstance(this.currentAccount).setStoryQuality(!z);
        BulletinFactory.of(this.storyContainer, this.resourcesProvider).createSimpleBulletin(R.raw.chats_infotip, LocaleController.getString(!z ? R.string.StoryQualityIncreasedTitle : R.string.StoryQualityDecreasedTitle), LocaleController.getString(!z ? R.string.StoryQualityIncreasedMessage : R.string.StoryQualityDecreasedMessage)).show();
        CustomPopupMenu customPopupMenu = this.popupMenu;
        if (customPopupMenu != null) {
            customPopupMenu.dismiss();
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void showLikesReaction(final boolean z) {
        if (this.likesReactionShowing == z || this.currentStory.storyItem == null) {
            return;
        }
        this.likesReactionShowing = z;
        if (z) {
            this.likesReactionLayout.setVisibility(0);
        }
        this.likesReactionLayout.setStoryItem(this.currentStory.storyItem);
        this.delegate.setIsLikesReaction(z);
        if (z) {
            ValueAnimator valueAnimatorOfFloat = ValueAnimator.ofFloat(this.likesReactionShowProgress, z ? 1.0f : 0.0f);
            this.likesReactionLayout.setTransitionProgress(this.likesReactionShowProgress);
            valueAnimatorOfFloat.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() { // from class: org.telegram.ui.Stories.PeerStoriesView$$ExternalSyntheticLambda27
                @Override // android.animation.ValueAnimator.AnimatorUpdateListener
                public final void onAnimationUpdate(ValueAnimator valueAnimator) {
                    this.f$0.lambda$showLikesReaction$17(valueAnimator);
                }
            });
            valueAnimatorOfFloat.addListener(new AnimatorListenerAdapter() { // from class: org.telegram.ui.Stories.PeerStoriesView.14
                @Override // android.animation.AnimatorListenerAdapter, android.animation.Animator.AnimatorListener
                public void onAnimationEnd(Animator animator) {
                    if (z) {
                        return;
                    }
                    PeerStoriesView.this.likesReactionLayout.setVisibility(8);
                    PeerStoriesView.this.likesReactionLayout.reset();
                }
            });
            valueAnimatorOfFloat.setDuration(200L);
            valueAnimatorOfFloat.setInterpolator(CubicBezierInterpolator.EASE_OUT);
            valueAnimatorOfFloat.start();
            return;
        }
        if (this.likesReactionLayout.getReactionsWindow() != null) {
            this.likesReactionLayout.getReactionsWindow().dismissWithAlpha();
        }
        this.likesReactionLayout.animate().alpha(0.0f).setDuration(150L).setListener(new AnimatorListenerAdapter() { // from class: org.telegram.ui.Stories.PeerStoriesView.15
            @Override // android.animation.AnimatorListenerAdapter, android.animation.Animator.AnimatorListener
            public void onAnimationEnd(Animator animator) {
                PeerStoriesView.this.likesReactionShowProgress = 0.0f;
                PeerStoriesView.this.likesReactionLayout.setAlpha(1.0f);
                PeerStoriesView.this.likesReactionLayout.setVisibility(8);
                PeerStoriesView.this.likesReactionLayout.reset();
            }
        }).start();
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$showLikesReaction$17(ValueAnimator valueAnimator) {
        float fFloatValue = ((Float) valueAnimator.getAnimatedValue()).floatValue();
        this.likesReactionShowProgress = fFloatValue;
        this.likesReactionLayout.setTransitionProgress(fFloatValue);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void likeStory(ReactionsLayoutInBubble.VisibleReaction visibleReaction) {
        boolean z;
        TLRPC.Reaction reaction;
        TL_stories.StoryItem storyItem = this.currentStory.storyItem;
        if (storyItem == null) {
            return;
        }
        boolean z2 = (storyItem == null || storyItem.sent_reaction == null) ? false : true;
        TLRPC.Reaction reaction2 = storyItem.sent_reaction;
        if (reaction2 != null && visibleReaction == null) {
            animateLikeButton();
            this.storiesController.setStoryReaction(this.dialogId, this.currentStory.storyItem, null);
        } else if (visibleReaction == null) {
            TLRPC.TL_availableReaction tL_availableReaction = MediaDataController.getInstance(this.currentAccount).getReactionsMap().get("❤");
            if (tL_availableReaction != null) {
                this.drawAnimatedEmojiAsMovingReaction = false;
                this.reactionEffectImageReceiver.setImage(ImageLocation.getForDocument(tL_availableReaction.around_animation), ReactionsEffectOverlay.getFilterForAroundAnimation(), null, null, null, 0);
                if (this.reactionEffectImageReceiver.getLottieAnimation() != null) {
                    this.reactionEffectImageReceiver.getLottieAnimation().setCurrentFrame(0, false, true);
                }
                this.drawReactionEffect = true;
                this.storiesController.setStoryReaction(this.dialogId, this.currentStory.storyItem, ReactionsLayoutInBubble.VisibleReaction.fromEmojicon(tL_availableReaction));
            }
        } else {
            animateLikeButton();
            this.storiesController.setStoryReaction(this.dialogId, this.currentStory.storyItem, visibleReaction);
        }
        TL_stories.StoryItem storyItem2 = this.currentStory.storyItem;
        if (storyItem2 == null || (reaction = storyItem2.sent_reaction) == null) {
            this.storiesLikeButton.setReaction(null);
            z = false;
        } else {
            z2 = !z2;
            this.storiesLikeButton.setReaction(ReactionsLayoutInBubble.VisibleReaction.fromTL(reaction));
            try {
                performHapticFeedback(3);
            } catch (Exception unused) {
            }
            z = true;
        }
        if (this.isChannel && z2) {
            TL_stories.StoryItem storyItem3 = this.currentStory.storyItem;
            if (storyItem3.views == null) {
                storyItem3.views = new TL_stories.TL_storyViews();
            }
            TL_stories.StoryViews storyViews = this.currentStory.storyItem.views;
            int i = storyViews.reactions_count + (z ? 1 : -1);
            storyViews.reactions_count = i;
            if (i < 0) {
                storyViews.reactions_count = 0;
            }
        }
        TL_stories.StoryItem storyItem4 = this.currentStory.storyItem;
        ReactionsUtils.applyForStoryViews(reaction2, storyItem4.sent_reaction, storyItem4.views);
        updateUserViews(true);
    }

    private void animateLikeButton() {
        final StoriesLikeButton storiesLikeButton = this.storiesLikeButton;
        storiesLikeButton.animate().alpha(0.0f).scaleX(0.8f).scaleY(0.8f).setListener(new AnimatorListenerAdapter() { // from class: org.telegram.ui.Stories.PeerStoriesView.16
            @Override // android.animation.AnimatorListenerAdapter, android.animation.Animator.AnimatorListener
            public void onAnimationEnd(Animator animator) {
                AndroidUtilities.removeFromParent(storiesLikeButton);
            }
        }).setDuration(150L).start();
        int iDp = AndroidUtilities.dp(8.0f);
        StoriesLikeButton storiesLikeButton2 = new StoriesLikeButton(getContext(), this.sharedResources);
        this.storiesLikeButton = storiesLikeButton2;
        storiesLikeButton2.setPadding(iDp, iDp, iDp, iDp);
        this.storiesLikeButton.setAlpha(0.0f);
        this.storiesLikeButton.setScaleX(0.8f);
        this.storiesLikeButton.setScaleY(0.8f);
        this.storiesLikeButton.animate().alpha(1.0f).scaleX(1.0f).scaleY(1.0f).setDuration(150L);
        this.likeButtonContainer.addView(this.storiesLikeButton, LayoutHelper.createFrame(40, 40, 3));
        this.drawReactionEffect = false;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public ArrayList getAnimatedEmojiSets(StoryItemHolder storyItemHolder) {
        StoryEntry storyEntry;
        AnimatedEmojiSpan[] animatedEmojiSpanArr;
        TL_stories.MediaArea mediaArea;
        TLRPC.InputStickerSet inputStickerSet;
        ArrayList<TLRPC.MessageEntity> arrayList;
        TLRPC.InputStickerSet inputStickerSet2;
        if (storyItemHolder == null) {
            return null;
        }
        HashSet hashSet = new HashSet();
        ArrayList arrayList2 = new ArrayList();
        TL_stories.StoryItem storyItem = storyItemHolder.storyItem;
        int i = 0;
        if (storyItem != null && storyItem.media_areas != null) {
            for (int i2 = 0; i2 < storyItemHolder.storyItem.media_areas.size(); i2++) {
                TL_stories.MediaArea mediaArea2 = storyItemHolder.storyItem.media_areas.get(i2);
                if (mediaArea2 instanceof TL_stories.TL_mediaAreaSuggestedReaction) {
                    TLRPC.Reaction reaction = mediaArea2.reaction;
                    if (reaction instanceof TLRPC.TL_reactionCustomEmoji) {
                        TLRPC.Document documentFindDocument = AnimatedEmojiDrawable.findDocument(this.currentAccount, ((TLRPC.TL_reactionCustomEmoji) reaction).document_id);
                        if (documentFindDocument != null && (inputStickerSet2 = MessageObject.getInputStickerSet(documentFindDocument)) != null && !hashSet.contains(Long.valueOf(inputStickerSet2.id))) {
                            hashSet.add(Long.valueOf(inputStickerSet2.id));
                            arrayList2.add(inputStickerSet2);
                        }
                    }
                }
            }
        }
        TL_stories.StoryItem storyItem2 = storyItemHolder.storyItem;
        if (storyItem2 != null && (arrayList = storyItem2.entities) != null && !arrayList.isEmpty()) {
            while (i < storyItemHolder.storyItem.entities.size()) {
                TLRPC.MessageEntity messageEntity = storyItemHolder.storyItem.entities.get(i);
                if (messageEntity instanceof TLRPC.TL_messageEntityCustomEmoji) {
                    TLRPC.TL_messageEntityCustomEmoji tL_messageEntityCustomEmoji = (TLRPC.TL_messageEntityCustomEmoji) messageEntity;
                    TLRPC.Document documentFindDocument2 = tL_messageEntityCustomEmoji.document;
                    if (documentFindDocument2 == null) {
                        documentFindDocument2 = AnimatedEmojiDrawable.findDocument(this.currentAccount, tL_messageEntityCustomEmoji.document_id);
                    }
                    if (documentFindDocument2 != null) {
                        TLRPC.InputStickerSet inputStickerSet3 = MessageObject.getInputStickerSet(documentFindDocument2);
                        if (!hashSet.contains(Long.valueOf(inputStickerSet3.id))) {
                            hashSet.add(Long.valueOf(inputStickerSet3.id));
                            arrayList2.add(inputStickerSet3);
                        }
                    }
                }
                i++;
            }
        } else {
            StoriesController.UploadingStory uploadingStory = storyItemHolder.uploadingStory;
            if (uploadingStory != null && (storyEntry = uploadingStory.entry) != null) {
                if (storyEntry.mediaEntities != null) {
                    for (int i3 = 0; i3 < storyItemHolder.uploadingStory.entry.mediaEntities.size(); i3++) {
                        VideoEditedInfo.MediaEntity mediaEntity = (VideoEditedInfo.MediaEntity) storyItemHolder.uploadingStory.entry.mediaEntities.get(i3);
                        if (mediaEntity.type == 4 && (mediaArea = mediaEntity.mediaArea) != null) {
                            TLRPC.Reaction reaction2 = mediaArea.reaction;
                            if (reaction2 instanceof TLRPC.TL_reactionCustomEmoji) {
                                TLRPC.Document documentFindDocument3 = AnimatedEmojiDrawable.findDocument(this.currentAccount, ((TLRPC.TL_reactionCustomEmoji) reaction2).document_id);
                                if (documentFindDocument3 != null && (inputStickerSet = MessageObject.getInputStickerSet(documentFindDocument3)) != null && !hashSet.contains(Long.valueOf(inputStickerSet.id))) {
                                    hashSet.add(Long.valueOf(inputStickerSet.id));
                                    arrayList2.add(inputStickerSet);
                                }
                            }
                        }
                    }
                }
                CharSequence charSequence = storyItemHolder.uploadingStory.entry.caption;
                if ((charSequence instanceof Spanned) && (animatedEmojiSpanArr = (AnimatedEmojiSpan[]) ((Spanned) charSequence).getSpans(0, charSequence.length(), AnimatedEmojiSpan.class)) != null) {
                    while (i < animatedEmojiSpanArr.length) {
                        AnimatedEmojiSpan animatedEmojiSpan = animatedEmojiSpanArr[i];
                        TLRPC.Document documentFindDocument4 = animatedEmojiSpan.document;
                        if (documentFindDocument4 == null) {
                            documentFindDocument4 = AnimatedEmojiDrawable.findDocument(this.currentAccount, animatedEmojiSpan.documentId);
                        }
                        if (documentFindDocument4 != null) {
                            TLRPC.InputStickerSet inputStickerSet4 = MessageObject.getInputStickerSet(documentFindDocument4);
                            if (!hashSet.contains(Long.valueOf(inputStickerSet4.id))) {
                                hashSet.add(Long.valueOf(inputStickerSet4.id));
                                arrayList2.add(inputStickerSet4);
                            }
                        }
                        i++;
                    }
                }
            }
        }
        return arrayList2;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void toggleArchiveForStory(final long j) {
        String str;
        boolean z;
        TLObject tLObject;
        if (j > 0) {
            TLRPC.User user = MessagesController.getInstance(this.currentAccount).getUser(Long.valueOf(j));
            str = user.first_name;
            z = user.stories_hidden;
            tLObject = user;
        } else {
            TLRPC.Chat chat = MessagesController.getInstance(this.currentAccount).getChat(Long.valueOf(-j));
            str = chat.title;
            z = chat.stories_hidden;
            tLObject = chat;
        }
        final boolean z2 = !z;
        final TLObject tLObject2 = tLObject;
        final String str2 = str;
        final MessagesController messagesController = MessagesController.getInstance(this.currentAccount);
        AndroidUtilities.runOnUIThread(new Runnable() { // from class: org.telegram.ui.Stories.PeerStoriesView$$ExternalSyntheticLambda2
            @Override // java.lang.Runnable
            public final void run() {
                this.f$0.lambda$toggleArchiveForStory$20(messagesController, j, z2, str2, tLObject2);
            }
        }, 200L);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$toggleArchiveForStory$20(final MessagesController messagesController, final long j, final boolean z, String str, TLObject tLObject) {
        messagesController.getStoriesController().toggleHidden(j, z, false, true);
        BulletinFactory.UndoObject undoObject = new BulletinFactory.UndoObject();
        undoObject.onUndo = new Runnable() { // from class: org.telegram.ui.Stories.PeerStoriesView$$ExternalSyntheticLambda4
            @Override // java.lang.Runnable
            public final void run() {
                messagesController.getStoriesController().toggleHidden(j, !z, false, true);
            }
        };
        undoObject.onAction = new Runnable() { // from class: org.telegram.ui.Stories.PeerStoriesView$$ExternalSyntheticLambda5
            @Override // java.lang.Runnable
            public final void run() {
                messagesController.getStoriesController().toggleHidden(j, z, true, true);
            }
        };
        BulletinFactory.of(this.topBulletinContainer, this.resourcesProvider).createUsersBulletin(Arrays.asList(tLObject), !z ? AndroidUtilities.replaceTags(LocaleController.formatString(R.string.StoriesMovedToDialogs, ContactsController.formatName(str, null, 10))) : AndroidUtilities.replaceTags(LocaleController.formatString(R.string.StoriesMovedToContacts, ContactsController.formatName(str, null, 10))), null, undoObject).setTag(2).show(true);
    }

    private void createFailView() {
        if (this.failView != null) {
            return;
        }
        StoryFailView storyFailView = new StoryFailView(getContext(), this.resourcesProvider);
        this.failView = storyFailView;
        storyFailView.setOnClickListener(new View.OnClickListener() { // from class: org.telegram.ui.Stories.PeerStoriesView$$ExternalSyntheticLambda34
            @Override // android.view.View.OnClickListener
            public final void onClick(View view) {
                this.f$0.lambda$createFailView$21(view);
            }
        });
        this.failView.setAlpha(0.0f);
        this.failView.setVisibility(8);
        addView(this.failView, LayoutHelper.createFrame(-1, -2.0f, 83, 0.0f, 0.0f, 0.0f, 0.0f));
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$createFailView$21(View view) {
        StoriesController.UploadingStory uploadingStory;
        StoryItemHolder storyItemHolder = this.currentStory;
        if (storyItemHolder == null || (uploadingStory = storyItemHolder.uploadingStory) == null) {
            return;
        }
        uploadingStory.tryAgain();
        updatePosition();
    }

    private void createPremiumBlockedText() {
        if (this.premiumBlockedText != null) {
            return;
        }
        if (this.chatActivityEnterView == null) {
            createEnterView();
        }
        LinearLayout linearLayout = new LinearLayout(getContext());
        this.premiumBlockedText = linearLayout;
        linearLayout.setOrientation(0);
        ImageView imageView = new ImageView(getContext());
        imageView.setScaleType(ImageView.ScaleType.CENTER);
        imageView.setScaleX(1.35f);
        imageView.setScaleY(1.35f);
        imageView.setImageResource(R.drawable.mini_switch_lock);
        imageView.setColorFilter(new PorterDuffColorFilter(-8026747, PorterDuff.Mode.SRC_IN));
        TextView textView = new TextView(getContext());
        this.premiumBlockedText1 = textView;
        textView.setTextColor(-8026747);
        this.premiumBlockedText1.setTextSize(1, 16.0f);
        this.premiumBlockedText1.setText(LocaleController.getString(this.isGroup ? R.string.StoryGroupRepliesLocked : R.string.StoryRepliesLocked));
        TextView textView2 = new TextView(getContext());
        this.premiumBlockedText2 = textView2;
        textView2.setTextColor(-1);
        this.premiumBlockedText2.setTextSize(1, 12.0f);
        this.premiumBlockedText2.setBackground(Theme.createSimpleSelectorRoundRectDrawable(AndroidUtilities.dp(40.0f), 452984831, 855638015));
        this.premiumBlockedText2.setGravity(17);
        ScaleStateListAnimator.apply(this.premiumBlockedText2);
        this.premiumBlockedText2.setText(LocaleController.getString(R.string.StoryRepliesLockedButton));
        this.premiumBlockedText2.setPadding(AndroidUtilities.dp(7.0f), 0, AndroidUtilities.dp(7.0f), 0);
        this.premiumBlockedText.addView(imageView, LayoutHelper.createLinear(22, 22, 16, 12, 1, 4, 0));
        this.premiumBlockedText.addView(this.premiumBlockedText1, LayoutHelper.createLinear(-2, -2, 16, 0.0f, -0.33f, 0.0f, 0.0f));
        this.premiumBlockedText.addView(this.premiumBlockedText2, LayoutHelper.createLinear(-2, 19, 16, 5.0f, -0.33f, 0.0f, 0.0f));
        this.chatActivityEnterView.addView(this.premiumBlockedText, LayoutHelper.createFrame(-1, -1.0f, 119, 14.0f, 0.0f, 8.0f, 0.0f));
    }

    private void updatePremiumBlockedText() {
        if (this.areLiveCommentsDisabled) {
            TextView textView = this.premiumBlockedText1;
            if (textView != null) {
                textView.setText(LocaleController.getString(R.string.LiveStoryCommentsDisabled));
            }
            TextView textView2 = this.premiumBlockedText2;
            if (textView2 != null) {
                textView2.setVisibility(8);
                return;
            }
            return;
        }
        TextView textView3 = this.premiumBlockedText1;
        if (textView3 != null) {
            textView3.setText(LocaleController.getString(this.isGroup ? R.string.StoryGroupRepliesLocked : R.string.StoryRepliesLocked));
        }
        TextView textView4 = this.premiumBlockedText2;
        if (textView4 != null) {
            textView4.setVisibility(0);
            this.premiumBlockedText2.setText(LocaleController.getString(R.string.StoryRepliesLockedButton));
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public Activity findActivity() {
        Activity activityFindActivity;
        StoryViewer storyViewer = this.storyViewer;
        if (storyViewer == null || (activityFindActivity = storyViewer.parentActivity) == null) {
            activityFindActivity = AndroidUtilities.findActivity(getContext());
        }
        return activityFindActivity == null ? LaunchActivity.instance : activityFindActivity;
    }

    private BaseFragment fragmentForLimit() {
        return new BaseFragment() { // from class: org.telegram.ui.Stories.PeerStoriesView.17
            @Override // org.telegram.ui.ActionBar.BaseFragment
            public boolean isLightStatusBar() {
                return false;
            }

            @Override // org.telegram.ui.ActionBar.BaseFragment
            public Activity getParentActivity() {
                return PeerStoriesView.this.findActivity();
            }

            @Override // org.telegram.ui.ActionBar.BaseFragment
            public Theme.ResourcesProvider getResourceProvider() {
                return new WrappedResourceProvider(PeerStoriesView.this.resourcesProvider) { // from class: org.telegram.ui.Stories.PeerStoriesView.17.1
                    @Override // org.telegram.ui.WrappedResourceProvider
                    public void appendColors() {
                        this.sparseIntArray.append(Theme.key_dialogBackground, -14737633);
                        this.sparseIntArray.append(Theme.key_windowBackgroundGray, -13421773);
                    }
                };
            }

            @Override // org.telegram.ui.ActionBar.BaseFragment
            public boolean presentFragment(BaseFragment baseFragment) {
                if (PeerStoriesView.this.storyViewer == null) {
                    return true;
                }
                PeerStoriesView.this.storyViewer.presentFragment(baseFragment);
                return true;
            }

            @Override // org.telegram.ui.ActionBar.BaseFragment
            public Dialog showDialog(Dialog dialog) {
                if (PeerStoriesView.this.storyViewer != null) {
                    PeerStoriesView.this.storyViewer.showDialog(dialog);
                    return dialog;
                }
                if (dialog != null) {
                    dialog.show();
                }
                return dialog;
            }
        };
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void showPremiumBlockedToast() {
        String userName;
        Bulletin bulletinCreateSimpleBulletin;
        if (this.areLiveCommentsDisabled) {
            return;
        }
        if (this.isGroup) {
            if (this.boostsStatus != null && this.canApplyBoost != null) {
                LimitReachedBottomSheet.openBoostsForRemoveRestrictions(fragmentForLimit(), this.boostsStatus, this.canApplyBoost, this.dialogId, true);
                return;
            }
            StoryViewer storyViewer = this.storyViewer;
            if (storyViewer != null) {
                storyViewer.setOverlayVisible(true);
            }
            MessagesController.getInstance(this.currentAccount).getBoostsController().getBoostsStats(this.dialogId, new Consumer() { // from class: org.telegram.ui.Stories.PeerStoriesView$$ExternalSyntheticLambda46
                @Override // com.google.android.exoplayer2.util.Consumer
                public final void accept(Object obj) {
                    this.f$0.lambda$showPremiumBlockedToast$23((TL_stories.TL_premium_boostsStatus) obj);
                }
            });
            return;
        }
        ChatActivityEnterView chatActivityEnterView = this.chatActivityEnterView;
        int i = -this.shiftDp;
        this.shiftDp = i;
        AndroidUtilities.shakeViewSpring(chatActivityEnterView, i);
        BotWebViewVibrationEffect.APP_ERROR.vibrate();
        if (this.dialogId < 0) {
            userName = _UrlKt.FRAGMENT_ENCODE_SET;
        } else {
            userName = UserObject.getUserName(MessagesController.getInstance(this.currentAccount).getUser(Long.valueOf(this.dialogId)));
        }
        if (MessagesController.getInstance(this.currentAccount).premiumFeaturesBlocked()) {
            bulletinCreateSimpleBulletin = BulletinFactory.of(this.storyContainer, this.resourcesProvider).createSimpleBulletin(R.raw.star_premium_2, AndroidUtilities.replaceTags(LocaleController.formatString(R.string.UserBlockedRepliesNonPremium, userName)));
        } else {
            bulletinCreateSimpleBulletin = BulletinFactory.of(this.storyContainer, this.resourcesProvider).createSimpleBulletin(R.raw.star_premium_2, AndroidUtilities.replaceTags(LocaleController.formatString(R.string.UserBlockedRepliesNonPremium, userName)), LocaleController.getString(R.string.UserBlockedNonPremiumButton), new Runnable() { // from class: org.telegram.ui.Stories.PeerStoriesView$$ExternalSyntheticLambda47
                @Override // java.lang.Runnable
                public final void run() {
                    this.f$0.lambda$showPremiumBlockedToast$24();
                }
            });
        }
        bulletinCreateSimpleBulletin.show();
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$showPremiumBlockedToast$23(final TL_stories.TL_premium_boostsStatus tL_premium_boostsStatus) {
        if (tL_premium_boostsStatus == null) {
            StoryViewer storyViewer = this.storyViewer;
            if (storyViewer != null) {
                storyViewer.setOverlayVisible(false);
                return;
            }
            return;
        }
        this.boostsStatus = tL_premium_boostsStatus;
        MessagesController.getInstance(this.currentAccount).getBoostsController().userCanBoostChannel(this.dialogId, tL_premium_boostsStatus, new Consumer() { // from class: org.telegram.ui.Stories.PeerStoriesView$$ExternalSyntheticLambda51
            @Override // com.google.android.exoplayer2.util.Consumer
            public final void accept(Object obj) {
                this.f$0.lambda$showPremiumBlockedToast$22(tL_premium_boostsStatus, (ChannelBoostsController.CanApplyBoost) obj);
            }
        });
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$showPremiumBlockedToast$22(TL_stories.TL_premium_boostsStatus tL_premium_boostsStatus, ChannelBoostsController.CanApplyBoost canApplyBoost) {
        this.canApplyBoost = canApplyBoost;
        LimitReachedBottomSheet.openBoostsForRemoveRestrictions(fragmentForLimit(), tL_premium_boostsStatus, canApplyBoost, this.dialogId, true);
        StoryViewer storyViewer = this.storyViewer;
        if (storyViewer != null) {
            storyViewer.setOverlayVisible(false);
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$showPremiumBlockedToast$24() {
        StoryViewer storyViewer = this.storyViewer;
        if (storyViewer != null) {
            storyViewer.presentFragment(new PremiumPreviewFragment("noncontacts"));
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void updateSpeedItem(boolean z) {
        ActionBarMenuSubItem actionBarMenuSubItem = this.speedItem;
        if (actionBarMenuSubItem == null || this.speedLayout == null || actionBarMenuSubItem.getVisibility() != 0) {
            return;
        }
        if (z) {
            if (Math.abs(StoryViewer.currentSpeed - 0.2f) < 0.05f) {
                this.speedItem.setSubtext(LocaleController.getString(R.string.VideoSpeedVerySlow));
            } else if (Math.abs(StoryViewer.currentSpeed - 0.5f) < 0.05f) {
                this.speedItem.setSubtext(LocaleController.getString(R.string.VideoSpeedSlow));
            } else if (Math.abs(StoryViewer.currentSpeed - 1.0f) < 0.05f) {
                this.speedItem.setSubtext(LocaleController.getString(R.string.VideoSpeedNormal));
            } else if (Math.abs(StoryViewer.currentSpeed - 1.5f) < 0.05f) {
                this.speedItem.setSubtext(LocaleController.getString(R.string.VideoSpeedFast));
            } else if (Math.abs(StoryViewer.currentSpeed - 2.0f) < 0.05f) {
                this.speedItem.setSubtext(LocaleController.getString(R.string.VideoSpeedVeryFast));
            } else {
                this.speedItem.setSubtext(LocaleController.formatString(R.string.VideoSpeedCustom, SpeedIconDrawable.formatNumber(StoryViewer.currentSpeed) + "x"));
            }
        }
        this.speedLayout.update(StoryViewer.currentSpeed, z);
    }

    private void createCommentButton() {
        if (this.commentButton != null || getContext() == null) {
            return;
        }
        CommentButton commentButton = new CommentButton(getContext(), this.blurredBackgroundColorProvider);
        this.commentButton = commentButton;
        commentButton.setOnClickListener(new View.OnClickListener() { // from class: org.telegram.ui.Stories.PeerStoriesView$$ExternalSyntheticLambda29
            @Override // android.view.View.OnClickListener
            public final void onClick(View view) {
                this.f$0.lambda$createCommentButton$25(view);
            }
        });
        addView(this.commentButton, LayoutHelper.createFrame(46, 42.0f, 83, 7.0f, 0.0f, 7.0f, 3.0f));
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$createCommentButton$25(View view) {
        LiveCommentsView liveCommentsView = this.liveCommentsView;
        liveCommentsView.setCollapsed(!liveCommentsView.isCollapsed(), true);
    }

    private void createPaidReactionsButton() {
        if (this.starsButton != null || getContext() == null) {
            return;
        }
        this.starsButtonEffectsView = new PaidReactionButton.PaidReactionButtonEffectsView(getContext(), this.currentAccount);
        PaidReactionButton paidReactionButton = new PaidReactionButton(getContext(), this.starsButtonEffectsView, this.blurredBackgroundColorProvider);
        this.starsButton = paidReactionButton;
        paidReactionButton.setOnClickListener(new View.OnClickListener() { // from class: org.telegram.ui.Stories.PeerStoriesView$$ExternalSyntheticLambda39
            @Override // android.view.View.OnClickListener
            public final void onClick(View view) {
                this.f$0.lambda$createPaidReactionsButton$26(view);
            }
        });
        this.starsButton.setOnLongClickListener(new View.OnLongClickListener() { // from class: org.telegram.ui.Stories.PeerStoriesView$$ExternalSyntheticLambda40
            @Override // android.view.View.OnLongClickListener
            public final boolean onLongClick(View view) {
                return this.f$0.lambda$createPaidReactionsButton$27(view);
            }
        });
        addView(this.starsButton, LayoutHelper.createFrame(46, 42.0f, 85, 7.0f, 0.0f, 7.0f, 3.0f));
        addView(this.starsButtonEffectsView, LayoutHelper.createFrame(200, 200.0f, 85, 0.0f, 0.0f, 0.0f, 0.0f));
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$createPaidReactionsButton$26(View view) {
        if (disabledPaidFeatures(false)) {
            this.liveCommentsView.openStarsSheet(disabledPaidFeatures(false));
            return;
        }
        StarsController starsController = StarsController.getInstance(this.currentAccount);
        if (starsController.balanceAvailable() && starsController.balance.amount <= 0) {
            this.liveCommentsView.openStarsSheet(disabledPaidFeatures(false));
        } else {
            this.liveCommentsView.sendStars(1L, true);
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ boolean lambda$createPaidReactionsButton$27(View view) {
        this.liveCommentsView.openStarsSheet(disabledPaidFeatures(false));
        return true;
    }

    private void createMuteButton() {
        if (this.muteButton != null || getContext() == null) {
            return;
        }
        MuteButton muteButton = new MuteButton(getContext(), this.blurredBackgroundColorProvider);
        this.muteButton = muteButton;
        muteButton.setOnClickListener(new View.OnClickListener() { // from class: org.telegram.ui.Stories.PeerStoriesView$$ExternalSyntheticLambda37
            @Override // android.view.View.OnClickListener
            public final void onClick(View view) {
                this.f$0.lambda$createMuteButton$28(view);
            }
        });
        MuteButton muteButton2 = this.muteButton;
        LivePlayer livePlayer = LivePlayer.recording;
        boolean z = true;
        muteButton2.setMuted(livePlayer != null && livePlayer.isMuted(), false);
        MuteButton muteButton3 = this.muteButton;
        LivePlayer livePlayer2 = LivePlayer.recording;
        if (livePlayer2 != null && !livePlayer2.isConnected()) {
            z = false;
        }
        muteButton3.setConnected(z, false);
        addView(this.muteButton, LayoutHelper.createFrame(46, 42.0f, 85, 7.0f, 0.0f, 7.0f, 3.0f));
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$createMuteButton$28(View view) {
        LivePlayer livePlayer = LivePlayer.recording;
        if (livePlayer == null) {
            return;
        }
        boolean zIsMuted = livePlayer.isMuted();
        LivePlayer.recording.setMuted(!zIsMuted);
        this.muteButton.setMuted(!zIsMuted, true);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public boolean disabledPaidFeatures(boolean z) {
        StoryViewer storyViewer;
        LivePlayer livePlayer;
        TLRPC.Peer defaultSendAs;
        LivePlayer livePlayer2;
        long clientUserId = UserConfig.getInstance(this.currentAccount).getClientUserId();
        long j = this.dialogId;
        if (j >= 0 || (livePlayer2 = this.storyViewer.livePlayer) == null) {
            return j >= 0 && (storyViewer = this.storyViewer) != null && (livePlayer = storyViewer.livePlayer) != null && livePlayer.isAdmin() && (!z || (defaultSendAs = this.storyViewer.livePlayer.getDefaultSendAs()) == null || this.dialogId == DialogObject.getPeerDialogId(defaultSendAs) || DialogObject.getPeerDialogId(defaultSendAs) == clientUserId || this.dialogId == clientUserId);
        }
        if (!z) {
            return false;
        }
        TLRPC.Peer defaultSendAs2 = livePlayer2.getDefaultSendAs();
        return (this.storyViewer.livePlayer.isAdmin() || ChatObject.canManageCalls(MessagesController.getInstance(this.currentAccount).getChat(Long.valueOf(-this.dialogId)))) && (defaultSendAs2 == null || this.dialogId == DialogObject.getPeerDialogId(defaultSendAs2) || DialogObject.getPeerDialogId(defaultSendAs2) == UserConfig.getInstance(this.currentAccount).getClientUserId());
    }

    private void showPaidMessageHint() {
        HintView2 hintView2 = this.highlightMessageHintView;
        if (hintView2 != null) {
            if (hintView2.shown()) {
                return;
            } else {
                removeView(this.highlightMessageHintView);
            }
        }
        if (!disabledPaidFeatures(true) && MessagesController.getGlobalMainSettings().getInt("taptostoryhighlighthint", 0) < 3) {
            MessagesController.getGlobalMainSettings().edit().putInt("taptostoryhighlighthint", MessagesController.getGlobalMainSettings().getInt("taptostoryhighlighthint", 0) + 1).apply();
            final HintView2 hintView3 = new HintView2(getContext(), 3);
            this.highlightMessageHintView = hintView3;
            hintView3.setText(LocaleController.getString(R.string.LiveStoryHighlightHint));
            this.highlightMessageHintView.setPadding(AndroidUtilities.dp(8.0f), 0, AndroidUtilities.dp(8.0f), 0);
            this.highlightMessageHintView.setTextAlign(Layout.Alignment.ALIGN_OPPOSITE);
            this.highlightMessageHintView.setOnHiddenListener(new Runnable() { // from class: org.telegram.ui.Stories.PeerStoriesView$$ExternalSyntheticLambda28
                @Override // java.lang.Runnable
                public final void run() {
                    this.f$0.lambda$showPaidMessageHint$29(hintView3);
                }
            });
            addView(this.highlightMessageHintView, LayoutHelper.createFrame(-1, 100, 87));
            this.highlightMessageHintView.show();
            updateViewOffsets();
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$showPaidMessageHint$29(HintView2 hintView2) {
        removeView(hintView2);
        if (this.highlightMessageHintView == hintView2) {
            this.highlightMessageHintView = null;
        }
    }

    private void createEnterView() {
        AnonymousClass19 anonymousClass19 = new AnonymousClass19(AndroidUtilities.findActivity(getContext()), this, null, true, new WrappedResourceProvider(this.resourcesProvider) { // from class: org.telegram.ui.Stories.PeerStoriesView.18
            @Override // org.telegram.ui.WrappedResourceProvider
            public void appendColors() {
                this.sparseIntArray.put(Theme.key_chat_emojiPanelBackground, -1071635414);
            }
        });
        this.chatActivityEnterView = anonymousClass19;
        anonymousClass19.getEditField().useAnimatedTextDrawable();
        this.chatActivityEnterView.getEditField().setScaleX(0.0f);
        this.chatActivityEnterView.setOverrideKeyboardAnimation(true);
        this.chatActivityEnterView.setClipChildren(false);
        this.chatActivityEnterView.setDelegate(new AnonymousClass20());
        setDelegate(this.chatActivityEnterView);
        ChatActivityEnterView chatActivityEnterView = this.chatActivityEnterView;
        chatActivityEnterView.shouldDrawBackground = false;
        chatActivityEnterView.shouldDrawRecordedAudioPanelInParent = true;
        if (this.currentStory.isLive) {
            this.chatActivityEnterView.setAllowStickersAndGifs(true, false, false);
        } else {
            this.chatActivityEnterView.setAllowStickersAndGifs(true, true, true);
        }
        this.chatActivityEnterView.updateColors();
        ChatActivityEnterView chatActivityEnterView2 = this.chatActivityEnterView;
        chatActivityEnterView2.isStories = true;
        addView(chatActivityEnterView2, LayoutHelper.createFrame(-1, -2.0f, 83, 7.0f, 0.0f, 7.0f, 0.0f));
        if (this.sendAsPeersObj != null) {
            this.chatActivityEnterView.updateSendAsButton(false);
        }
        this.chatActivityEnterView.recordingGuid = this.classGuid;
        this.playerSharedScope.viewsToInvalidate.add(this.storyContainer);
        this.playerSharedScope.viewsToInvalidate.add(this);
        if (this.attachedToWindow) {
            this.chatActivityEnterView.onResume();
        }
        checkStealthMode(false);
        if (isBotsPreview()) {
            this.chatActivityEnterView.setVisibility(8);
        }
        this.reactionsContainerIndex = getChildCount();
    }

    /* JADX INFO: renamed from: org.telegram.ui.Stories.PeerStoriesView$19, reason: invalid class name */
    class AnonymousClass19 extends ChatActivityEnterView {
        private int chatActivityEnterViewAnimateFromTop;
        int lastContentViewHeight;
        private Animator messageEditTextAnimator;
        int messageEditTextPredrawHeigth;
        int messageEditTextPredrawScrollY;

        AnonymousClass19(Activity activity, SizeNotifierFrameLayout sizeNotifierFrameLayout, ChatActivity chatActivity, boolean z, Theme.ResourcesProvider resourcesProvider) {
            super(activity, sizeNotifierFrameLayout, chatActivity, z, resourcesProvider);
        }

        @Override // org.telegram.ui.Components.ChatActivityEnterView
        public void updateSendAsButton(boolean z) {
            PeerStoriesView peerStoriesView = PeerStoriesView.this;
            super.updateSendAsButton(peerStoriesView.isPremiumBlocked || peerStoriesView.areLiveCommentsDisabled, z);
        }

        @Override // org.telegram.ui.Components.ChatActivityEnterView
        public boolean sendMessage() {
            int length;
            if (this.sendButtonContainer.getAlpha() < 0.5f) {
                openKeyboard();
                return false;
            }
            if (PeerStoriesView.this.currentStory.isLive) {
                long jMax = Math.max(PeerStoriesView.this.messageStars, PeerStoriesView.this.getMessageMinPrice());
                TLRPC.TL_textWithEntities textWithEntities = getTextWithEntities();
                CharSequence textWithEntities2 = MessageObject.formatTextWithEntities(textWithEntities, false, new TextPaint());
                if (textWithEntities2.length() > HighlightMessageSheet.getMaxLength(PeerStoriesView.this.currentAccount)) {
                    NumberTextView numberTextView = this.captionLimitView;
                    if (numberTextView != null) {
                        AndroidUtilities.shakeViewSpring(numberTextView, 3.5f);
                        try {
                            this.captionLimitView.performHapticFeedback(3, 2);
                        } catch (Exception unused) {
                        }
                    }
                    return false;
                }
                if (!PeerStoriesView.this.disabledPaidFeatures(true)) {
                    if (textWithEntities2 instanceof Spannable) {
                        Spannable spannable = (Spannable) textWithEntities2;
                        length = ((AnimatedEmojiSpan[]) spannable.getSpans(0, textWithEntities2.length(), AnimatedEmojiSpan.class)).length + ((Emoji.EmojiSpan[]) spannable.getSpans(0, textWithEntities2.length(), Emoji.EmojiSpan.class)).length;
                    } else {
                        length = 0;
                    }
                    int i = (int) jMax;
                    if (length > HighlightMessageSheet.getTierOption(PeerStoriesView.this.currentAccount, i, HighlightMessageSheet.TIER_EMOJIS) || textWithEntities2.length() > HighlightMessageSheet.getTierOption(PeerStoriesView.this.currentAccount, i, HighlightMessageSheet.TIER_LENGTH)) {
                        PeerStoriesView.this.lambda$updatePosition$46();
                        return false;
                    }
                }
                PeerStoriesView.this.liveCommentsView.send(textWithEntities, jMax);
                this.messageEditText.setText(_UrlKt.FRAGMENT_ENCODE_SET);
                AndroidUtilities.hideKeyboard(this);
                PeerStoriesView.this.messageStars = 0L;
                PeerStoriesView.this.checkStealthMode(true);
                checkSendButton(true);
                return true;
            }
            return super.sendMessage();
        }

        @Override // android.view.ViewGroup, android.view.View
        public boolean dispatchTouchEvent(MotionEvent motionEvent) {
            if (!isEnabled()) {
                RectF rectF = AndroidUtilities.rectTmp;
                rectF.set(0.0f, 0.0f, getWidth() + (PeerStoriesView.this.premiumBlockedText2 != null ? this.attachLayoutPaddingTranslationX * 1.5f : 0.0f), getHeight());
                boolean zContains = rectF.contains(motionEvent.getX(), motionEvent.getY());
                if (motionEvent.getAction() == 0) {
                    if (zContains && PeerStoriesView.this.premiumBlockedText2 != null) {
                        PeerStoriesView.this.premiumBlockedText2.setPressed(true);
                    }
                } else if (motionEvent.getAction() == 1) {
                    if (PeerStoriesView.this.premiumBlockedText2 != null) {
                        if (zContains && PeerStoriesView.this.premiumBlockedText2.isPressed()) {
                            PeerStoriesView.this.showPremiumBlockedToast();
                        }
                        PeerStoriesView.this.premiumBlockedText2.setPressed(false);
                    }
                } else if (motionEvent.getAction() == 3 && PeerStoriesView.this.premiumBlockedText2 != null) {
                    PeerStoriesView.this.premiumBlockedText2.setPressed(false);
                }
                return PeerStoriesView.this.premiumBlockedText2 != null && PeerStoriesView.this.premiumBlockedText2.isPressed();
            }
            return super.dispatchTouchEvent(motionEvent);
        }

        @Override // org.telegram.ui.Components.ChatActivityEnterView
        public void setHorizontalPadding(float f, float f2, float f3, boolean z) {
            if (PeerStoriesView.this.premiumBlockedText != null) {
                PeerStoriesView.this.premiumBlockedText.setTranslationX((1.0f - f3) * f);
            }
            super.setHorizontalPadding(f, f2, f3, z);
        }

        @Override // org.telegram.ui.Components.ChatActivityEnterView
        protected boolean showConfirmAlert(Runnable runnable) {
            return PeerStoriesView.this.applyMessageToChat(runnable);
        }

        @Override // org.telegram.ui.Components.ChatActivityEnterView
        public void checkAnimation() {
            int backgroundTop = getBackgroundTop();
            int i = this.chatActivityEnterViewAnimateFromTop;
            if (i != 0 && backgroundTop != i) {
                int i2 = (this.animatedTop + i) - backgroundTop;
                setAnimatedTop(i2);
                PeerStoriesView peerStoriesView = PeerStoriesView.this;
                peerStoriesView.forceUpdateOffsets = true;
                if (peerStoriesView.changeBoundAnimator != null) {
                    PeerStoriesView.this.changeBoundAnimator.removeAllListeners();
                    PeerStoriesView.this.changeBoundAnimator.cancel();
                }
                View view = this.topView;
                if (view != null && view.getVisibility() == 0) {
                    this.topView.setTranslationY(this.animatedTop + ((1.0f - getTopViewEnterProgress()) * this.topView.getLayoutParams().height));
                }
                PeerStoriesView.this.invalidate();
                PeerStoriesView.this.changeBoundAnimator = ValueAnimator.ofFloat(i2, 0.0f);
                PeerStoriesView.this.changeBoundAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() { // from class: org.telegram.ui.Stories.PeerStoriesView$19$$ExternalSyntheticLambda0
                    @Override // android.animation.ValueAnimator.AnimatorUpdateListener
                    public final void onAnimationUpdate(ValueAnimator valueAnimator) {
                        this.f$0.lambda$checkAnimation$0(valueAnimator);
                    }
                });
                PeerStoriesView.this.changeBoundAnimator.addListener(new AnimatorListenerAdapter() { // from class: org.telegram.ui.Stories.PeerStoriesView.19.1
                    @Override // android.animation.AnimatorListenerAdapter, android.animation.Animator.AnimatorListener
                    public void onAnimationEnd(Animator animator) {
                        PeerStoriesView.this.invalidate();
                        AnonymousClass19.this.setAnimatedTop(0);
                        AnonymousClass19 anonymousClass19 = AnonymousClass19.this;
                        PeerStoriesView.this.forceUpdateOffsets = true;
                        if (((ChatActivityEnterView) anonymousClass19).topView != null && ((ChatActivityEnterView) AnonymousClass19.this).topView.getVisibility() == 0) {
                            ((ChatActivityEnterView) AnonymousClass19.this).topView.setTranslationY(((ChatActivityEnterView) AnonymousClass19.this).animatedTop + ((1.0f - AnonymousClass19.this.getTopViewEnterProgress()) * ((ChatActivityEnterView) AnonymousClass19.this).topView.getLayoutParams().height));
                        }
                        PeerStoriesView.this.changeBoundAnimator = null;
                    }
                });
                PeerStoriesView.this.changeBoundAnimator.setDuration(250L);
                PeerStoriesView.this.changeBoundAnimator.setInterpolator(ChatListItemAnimator.DEFAULT_INTERPOLATOR);
                PeerStoriesView.this.changeBoundAnimator.start();
                this.chatActivityEnterViewAnimateFromTop = 0;
            }
            if (this.shouldAnimateEditTextWithBounds) {
                float measuredHeight = (this.messageEditTextPredrawHeigth - this.messageEditText.getMeasuredHeight()) + (this.messageEditTextPredrawScrollY - this.messageEditText.getScrollY());
                EditTextCaption editTextCaption = this.messageEditText;
                editTextCaption.setOffsetY(editTextCaption.getOffsetY() - measuredHeight);
                ValueAnimator valueAnimatorOfFloat = ValueAnimator.ofFloat(this.messageEditText.getOffsetY(), 0.0f);
                valueAnimatorOfFloat.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() { // from class: org.telegram.ui.Stories.PeerStoriesView$19$$ExternalSyntheticLambda1
                    @Override // android.animation.ValueAnimator.AnimatorUpdateListener
                    public final void onAnimationUpdate(ValueAnimator valueAnimator) {
                        this.f$0.lambda$checkAnimation$1(valueAnimator);
                    }
                });
                Animator animator = this.messageEditTextAnimator;
                if (animator != null) {
                    animator.cancel();
                }
                this.messageEditTextAnimator = valueAnimatorOfFloat;
                valueAnimatorOfFloat.setDuration(250L);
                valueAnimatorOfFloat.setInterpolator(ChatListItemAnimator.DEFAULT_INTERPOLATOR);
                valueAnimatorOfFloat.start();
                this.shouldAnimateEditTextWithBounds = false;
                PeerStoriesView.this.updateViewOffsets();
            }
            this.lastContentViewHeight = getMeasuredHeight();
        }

        /* JADX INFO: Access modifiers changed from: private */
        public /* synthetic */ void lambda$checkAnimation$0(ValueAnimator valueAnimator) {
            setAnimatedTop((int) ((Float) valueAnimator.getAnimatedValue()).floatValue());
            PeerStoriesView peerStoriesView = PeerStoriesView.this;
            peerStoriesView.forceUpdateOffsets = true;
            peerStoriesView.invalidate();
            invalidate();
        }

        /* JADX INFO: Access modifiers changed from: private */
        public /* synthetic */ void lambda$checkAnimation$1(ValueAnimator valueAnimator) {
            this.messageEditText.setOffsetY(((Float) valueAnimator.getAnimatedValue()).floatValue());
        }

        @Override // org.telegram.ui.Components.ChatActivityEnterView
        protected void onLineCountChanged(int i, int i2) {
            if (PeerStoriesView.this.chatActivityEnterView != null) {
                this.shouldAnimateEditTextWithBounds = true;
                this.messageEditTextPredrawHeigth = this.messageEditText.getMeasuredHeight();
                this.messageEditTextPredrawScrollY = this.messageEditText.getScrollY();
                invalidate();
                PeerStoriesView.this.invalidate();
                this.chatActivityEnterViewAnimateFromTop = PeerStoriesView.this.chatActivityEnterView.getBackgroundTop();
            }
        }

        @Override // org.telegram.ui.Components.ChatActivityEnterView
        protected void updateRecordInterface(int i, boolean z) {
            super.updateRecordInterface(i, z);
            checkRecording();
        }

        @Override // org.telegram.ui.Components.ChatActivityEnterView
        protected void isRecordingStateChanged() {
            super.isRecordingStateChanged();
            checkRecording();
        }

        private void checkRecording() {
            FrameLayout frameLayout;
            boolean z = PeerStoriesView.this.isRecording;
            PeerStoriesView peerStoriesView = PeerStoriesView.this;
            peerStoriesView.isRecording = peerStoriesView.chatActivityEnterView.isRecordingAudioVideo() || PeerStoriesView.this.chatActivityEnterView.seekbarVisible() || ((frameLayout = this.recordedAudioPanel) != null && frameLayout.getVisibility() == 0);
            if (z != PeerStoriesView.this.isRecording) {
                PeerStoriesView peerStoriesView2 = PeerStoriesView.this;
                if (peerStoriesView2.isActive) {
                    peerStoriesView2.delegate.setIsRecording(peerStoriesView2.isRecording);
                }
                invalidate();
                PeerStoriesView.this.storyContainer.invalidate();
            }
        }

        @Override // org.telegram.ui.Components.ChatActivityEnterView
        public void extendActionMode(Menu menu) {
            ChatActivity.fillActionModeMenu(menu, null, false, !PeerStoriesView.this.currentStory.isLive());
        }

        @Override // org.telegram.ui.Components.ChatActivityEnterView
        protected boolean sendMessageInternal(boolean z, int i, int i2, long j, boolean z2) {
            if (MessagesController.getInstance(PeerStoriesView.this.currentAccount).isFrozen()) {
                AccountFrozenAlert.show(PeerStoriesView.this.currentAccount);
                return false;
            }
            return super.sendMessageInternal(z, i, i2, j, z2);
        }

        @Override // org.telegram.ui.Components.ChatActivityEnterView
        public int getMessagesCount() {
            if (PeerStoriesView.this.currentStory.isLive) {
                return 1;
            }
            return super.getMessagesCount();
        }

        @Override // org.telegram.ui.Components.ChatActivityEnterView
        public long getStarsPrice() {
            if (PeerStoriesView.this.currentStory.isLive) {
                return Math.max(PeerStoriesView.this.getMessageMinPrice(), PeerStoriesView.this.messageStars);
            }
            return super.getStarsPrice();
        }

        @Override // org.telegram.ui.Components.ChatActivityEnterView
        public boolean areLiveCommentsFree() {
            return PeerStoriesView.this.disabledPaidFeatures(true);
        }
    }

    /* JADX INFO: renamed from: org.telegram.ui.Stories.PeerStoriesView$20, reason: invalid class name */
    class AnonymousClass20 implements ChatActivityEnterView.ChatActivityEnterViewDelegate {
        @Override // org.telegram.ui.Components.ChatActivityEnterView.ChatActivityEnterViewDelegate
        public /* synthetic */ void bottomPanelTranslationYChanged(float f) {
            ChatActivityEnterView.ChatActivityEnterViewDelegate.CC.$default$bottomPanelTranslationYChanged(this, f);
        }

        @Override // org.telegram.ui.Components.ChatActivityEnterView.ChatActivityEnterViewDelegate
        public /* synthetic */ boolean checkCanRemoveRestrictionsByBoosts() {
            return ChatActivityEnterView.ChatActivityEnterViewDelegate.CC.$default$checkCanRemoveRestrictionsByBoosts(this);
        }

        @Override // org.telegram.ui.Components.ChatActivityEnterView.ChatActivityEnterViewDelegate
        public /* synthetic */ ChatActivity.ReplyQuote getReplyQuote() {
            return ChatActivityEnterView.ChatActivityEnterViewDelegate.CC.$default$getReplyQuote(this);
        }

        @Override // org.telegram.ui.Components.ChatActivityEnterView.ChatActivityEnterViewDelegate
        public /* synthetic */ boolean hasForwardingMessages() {
            return ChatActivityEnterView.ChatActivityEnterViewDelegate.CC.$default$hasForwardingMessages(this);
        }

        @Override // org.telegram.ui.Components.ChatActivityEnterView.ChatActivityEnterViewDelegate
        public /* synthetic */ boolean hasScheduledMessages() {
            return ChatActivityEnterView.ChatActivityEnterViewDelegate.CC.$default$hasScheduledMessages(this);
        }

        @Override // org.telegram.ui.Components.ChatActivityEnterView.ChatActivityEnterViewDelegate
        public /* synthetic */ int measureKeyboardHeight() {
            return ChatActivityEnterView.ChatActivityEnterViewDelegate.CC.$default$measureKeyboardHeight(this);
        }

        @Override // org.telegram.ui.Components.ChatActivityEnterView.ChatActivityEnterViewDelegate
        public void needSendTyping() {
        }

        @Override // org.telegram.ui.Components.ChatActivityEnterView.ChatActivityEnterViewDelegate
        public void needStartRecordAudio(int i) {
        }

        @Override // org.telegram.ui.Components.ChatActivityEnterView.ChatActivityEnterViewDelegate
        public void onAttachButtonHidden() {
        }

        @Override // org.telegram.ui.Components.ChatActivityEnterView.ChatActivityEnterViewDelegate
        public void onAttachButtonShow() {
        }

        @Override // org.telegram.ui.Components.ChatActivityEnterView.ChatActivityEnterViewDelegate
        public void onAudioVideoInterfaceUpdated() {
        }

        @Override // org.telegram.ui.Components.ChatActivityEnterView.ChatActivityEnterViewDelegate
        public /* synthetic */ void onContextMenuClose() {
            ChatActivityEnterView.ChatActivityEnterViewDelegate.CC.$default$onContextMenuClose(this);
        }

        @Override // org.telegram.ui.Components.ChatActivityEnterView.ChatActivityEnterViewDelegate
        public /* synthetic */ void onContextMenuOpen() {
            ChatActivityEnterView.ChatActivityEnterViewDelegate.CC.$default$onContextMenuOpen(this);
        }

        @Override // org.telegram.ui.Components.ChatActivityEnterView.ChatActivityEnterViewDelegate
        public /* synthetic */ void onEditTextScroll() {
            ChatActivityEnterView.ChatActivityEnterViewDelegate.CC.$default$onEditTextScroll(this);
        }

        @Override // org.telegram.ui.Components.ChatActivityEnterView.ChatActivityEnterViewDelegate
        public /* synthetic */ void onKeyboardRequested() {
            ChatActivityEnterView.ChatActivityEnterViewDelegate.CC.$default$onKeyboardRequested(this);
        }

        @Override // org.telegram.ui.Components.ChatActivityEnterView.ChatActivityEnterViewDelegate
        public void onMessageEditEnd(boolean z) {
        }

        @Override // org.telegram.ui.Components.ChatActivityEnterView.ChatActivityEnterViewDelegate
        public void onPreAudioVideoRecord() {
        }

        @Override // org.telegram.ui.Components.ChatActivityEnterView.ChatActivityEnterViewDelegate
        public void onSendLongClick() {
        }

        @Override // org.telegram.ui.Components.ChatActivityEnterView.ChatActivityEnterViewDelegate
        public void onStickersTab(boolean z) {
        }

        @Override // org.telegram.ui.Components.ChatActivityEnterView.ChatActivityEnterViewDelegate
        public void onSwitchRecordMode(boolean z) {
        }

        @Override // org.telegram.ui.Components.ChatActivityEnterView.ChatActivityEnterViewDelegate
        public void onTextSelectionChanged(int i, int i2) {
        }

        @Override // org.telegram.ui.Components.ChatActivityEnterView.ChatActivityEnterViewDelegate
        public void onTextSpansChanged(CharSequence charSequence) {
        }

        @Override // org.telegram.ui.Components.ChatActivityEnterView.ChatActivityEnterViewDelegate
        public /* synthetic */ void onTrendingStickersShowed(boolean z) {
            ChatActivityEnterView.ChatActivityEnterViewDelegate.CC.$default$onTrendingStickersShowed(this, z);
        }

        @Override // org.telegram.ui.Components.ChatActivityEnterView.ChatActivityEnterViewDelegate
        public void onUpdateSlowModeButton(View view, boolean z, CharSequence charSequence) {
        }

        @Override // org.telegram.ui.Components.ChatActivityEnterView.ChatActivityEnterViewDelegate
        public void onWindowSizeChanged(int i) {
        }

        @Override // org.telegram.ui.Components.ChatActivityEnterView.ChatActivityEnterViewDelegate
        public /* synthetic */ void openScheduledMessages() {
            ChatActivityEnterView.ChatActivityEnterViewDelegate.CC.$default$openScheduledMessages(this);
        }

        @Override // org.telegram.ui.Components.ChatActivityEnterView.ChatActivityEnterViewDelegate
        public /* synthetic */ void prepareMessageSending() {
            ChatActivityEnterView.ChatActivityEnterViewDelegate.CC.$default$prepareMessageSending(this);
        }

        @Override // org.telegram.ui.Components.ChatActivityEnterView.ChatActivityEnterViewDelegate
        public /* synthetic */ void scrollToSendingMessage() {
            ChatActivityEnterView.ChatActivityEnterViewDelegate.CC.$default$scrollToSendingMessage(this);
        }

        AnonymousClass20() {
        }

        @Override // org.telegram.ui.Components.ChatActivityEnterView.ChatActivityEnterViewDelegate
        public TLRPC.TL_channels_sendAsPeers getSendAsPeers() {
            if (!PeerStoriesView.this.currentStory.isLive) {
                return null;
            }
            if (PeerStoriesView.this.storyViewer == null || PeerStoriesView.this.storyViewer.livePlayer == null || !PeerStoriesView.this.storyViewer.livePlayer.sendAsDisabled()) {
                return PeerStoriesView.this.sendAsPeersObj;
            }
            return null;
        }

        @Override // org.telegram.ui.Components.ChatActivityEnterView.ChatActivityEnterViewDelegate
        public TLRPC.Peer getDefaultSendAs() {
            if (PeerStoriesView.this.storyViewer == null || PeerStoriesView.this.storyViewer.livePlayer == null || PeerStoriesView.this.storyViewer.livePlayer.sendAsDisabled()) {
                return null;
            }
            return PeerStoriesView.this.storyViewer.livePlayer.getDefaultSendAs();
        }

        @Override // org.telegram.ui.Components.ChatActivityEnterView.ChatActivityEnterViewDelegate
        public boolean setDefaultSendAs(long j, long j2) {
            TL_stories.StoryItem storyItem = PeerStoriesView.this.currentStory.storyItem;
            if (storyItem != null && (storyItem.media instanceof TLRPC.TL_messageMediaVideoStream)) {
                TL_phone.saveDefaultSendAs savedefaultsendas = new TL_phone.saveDefaultSendAs();
                PeerStoriesView peerStoriesView = PeerStoriesView.this;
                savedefaultsendas.call = ((TLRPC.TL_messageMediaVideoStream) peerStoriesView.currentStory.storyItem.media).call;
                savedefaultsendas.send_as = MessagesController.getInstance(peerStoriesView.currentAccount).getInputPeer(j2);
                ConnectionsManager.getInstance(PeerStoriesView.this.currentAccount).sendRequest(savedefaultsendas, null);
                if (PeerStoriesView.this.storyViewer.livePlayer != null) {
                    PeerStoriesView.this.storyViewer.livePlayer.setDefaultSendAs(MessagesController.getInstance(PeerStoriesView.this.currentAccount).getPeer(j2));
                }
                PeerStoriesView.this.checkStealthMode(true);
                PeerStoriesView.this.chatActivityEnterView.updateSendAsButton(true);
                PeerStoriesView.this.chatActivityEnterView.checkSendButton(true);
                PeerStoriesView.this.updatePosition();
            }
            return true;
        }

        @Override // org.telegram.ui.Components.ChatActivityEnterView.ChatActivityEnterViewDelegate
        public int getContentViewHeight() {
            return PeerStoriesView.this.getHeight();
        }

        @Override // org.telegram.ui.Components.ChatActivityEnterView.ChatActivityEnterViewDelegate
        public void onMessageSend(CharSequence charSequence, boolean z, int i, int i2, final long j) {
            if (PeerStoriesView.this.isRecording) {
                AndroidUtilities.runOnUIThread(new Runnable() { // from class: org.telegram.ui.Stories.PeerStoriesView$20$$ExternalSyntheticLambda0
                    @Override // java.lang.Runnable
                    public final void run() {
                        this.f$0.lambda$onMessageSend$0(j);
                    }
                }, 200L);
            } else {
                PeerStoriesView.this.afterMessageSend(j <= 0);
            }
        }

        /* JADX INFO: Access modifiers changed from: private */
        public /* synthetic */ void lambda$onMessageSend$0(long j) {
            PeerStoriesView.this.afterMessageSend(j <= 0);
        }

        @Override // org.telegram.ui.Components.ChatActivityEnterView.ChatActivityEnterViewDelegate
        public void onTextChanged(CharSequence charSequence, boolean z, boolean z2) {
            if (PeerStoriesView.this.mentionContainer == null) {
                PeerStoriesView.this.createMentionsContainer();
            }
            if (PeerStoriesView.this.mentionContainer.getAdapter() != null) {
                PeerStoriesView.this.mentionContainer.setDialogId(PeerStoriesView.this.dialogId);
                if (PeerStoriesView.this.currentStory.isLive) {
                    PeerStoriesView.this.mentionContainer.getAdapter().clear(true);
                } else {
                    PeerStoriesView.this.mentionContainer.getAdapter().setUserOrChat(MessagesController.getInstance(PeerStoriesView.this.currentAccount).getUser(Long.valueOf(PeerStoriesView.this.dialogId)), MessagesController.getInstance(PeerStoriesView.this.currentAccount).getChat(Long.valueOf(-PeerStoriesView.this.dialogId)));
                    PeerStoriesView.this.mentionContainer.getAdapter().lambda$searchUsernameOrHashtag$7(charSequence, PeerStoriesView.this.chatActivityEnterView.getCursorPosition(), null, false, false);
                }
            }
            PeerStoriesView.this.invalidate();
        }

        @Override // org.telegram.ui.Components.ChatActivityEnterView.ChatActivityEnterViewDelegate
        public void didPressAttachButton() {
            PeerStoriesView.this.openAttachMenu();
        }

        @Override // org.telegram.ui.Components.ChatActivityEnterView.ChatActivityEnterViewDelegate
        public void didPressSuggestionButton() {
            PeerStoriesView.this.lambda$updatePosition$46();
        }

        @Override // org.telegram.ui.Components.ChatActivityEnterView.ChatActivityEnterViewDelegate
        public void needStartRecordVideo(int i, boolean z, int i2, int i3, int i4, long j, long j2) {
            PeerStoriesView.this.checkInstantCameraView();
            if (PeerStoriesView.this.instantCameraView != null) {
                if (i == 0) {
                    PeerStoriesView.this.instantCameraView.showCamera(false);
                    return;
                }
                if (i == 1 || i == 3 || i == 4) {
                    PeerStoriesView.this.instantCameraView.send(i, z, i2, 0, i4, j, j2);
                } else if (i == 2 || i == 5) {
                    PeerStoriesView.this.instantCameraView.cancel(i == 2);
                }
            }
        }

        @Override // org.telegram.ui.Components.ChatActivityEnterView.ChatActivityEnterViewDelegate
        public void setFrontface(boolean z) {
            if (PeerStoriesView.this.instantCameraView != null) {
                PeerStoriesView.this.instantCameraView.setFrontface(z);
            }
        }

        @Override // org.telegram.ui.Components.ChatActivityEnterView.ChatActivityEnterViewDelegate
        public void toggleVideoRecordingPause() {
            if (PeerStoriesView.this.instantCameraView != null) {
                PeerStoriesView.this.instantCameraView.togglePause();
            }
        }

        @Override // org.telegram.ui.Components.ChatActivityEnterView.ChatActivityEnterViewDelegate
        public boolean isVideoRecordingPaused() {
            return PeerStoriesView.this.instantCameraView != null && PeerStoriesView.this.instantCameraView.isPaused();
        }

        @Override // org.telegram.ui.Components.ChatActivityEnterView.ChatActivityEnterViewDelegate
        public void needChangeVideoPreviewState(int i, float f) {
            if (PeerStoriesView.this.instantCameraView != null) {
                PeerStoriesView.this.instantCameraView.changeVideoPreviewState(i, f);
            }
        }

        @Override // org.telegram.ui.Components.ChatActivityEnterView.ChatActivityEnterViewDelegate
        public void needShowMediaBanHint() {
            String firstName;
            PeerStoriesView peerStoriesView = PeerStoriesView.this;
            if (peerStoriesView.isGroup) {
                peerStoriesView.showPremiumBlockedToast();
                return;
            }
            if (peerStoriesView.mediaBanTooltip == null) {
                PeerStoriesView.this.mediaBanTooltip = new HintView(PeerStoriesView.this.getContext(), 9, PeerStoriesView.this.resourcesProvider);
                PeerStoriesView.this.mediaBanTooltip.setVisibility(8);
                PeerStoriesView peerStoriesView2 = PeerStoriesView.this;
                peerStoriesView2.addView(peerStoriesView2.mediaBanTooltip, LayoutHelper.createFrame(-2, -2.0f, 51, 10.0f, 0.0f, 10.0f, 0.0f));
            }
            if (PeerStoriesView.this.dialogId >= 0) {
                firstName = UserObject.getFirstName(MessagesController.getInstance(PeerStoriesView.this.currentAccount).getUser(Long.valueOf(PeerStoriesView.this.dialogId)));
            } else {
                TLRPC.Chat chat = MessagesController.getInstance(PeerStoriesView.this.currentAccount).getChat(Long.valueOf(-PeerStoriesView.this.dialogId));
                firstName = chat != null ? chat.title : _UrlKt.FRAGMENT_ENCODE_SET;
            }
            PeerStoriesView.this.mediaBanTooltip.setText(AndroidUtilities.replaceTags(LocaleController.formatString(PeerStoriesView.this.chatActivityEnterView.isInVideoMode() ? R.string.VideoMessagesRestrictedByPrivacy : R.string.VoiceMessagesRestrictedByPrivacy, firstName)));
            PeerStoriesView.this.mediaBanTooltip.showForView(PeerStoriesView.this.chatActivityEnterView.getAudioVideoButtonContainer(), true);
        }

        @Override // org.telegram.ui.Components.ChatActivityEnterView.ChatActivityEnterViewDelegate
        public void onStickersExpandedChange() {
            PeerStoriesView.this.requestLayout();
        }

        @Override // org.telegram.ui.Components.ChatActivityEnterView.ChatActivityEnterViewDelegate
        public TL_stories.StoryItem getReplyToStory() {
            return PeerStoriesView.this.currentStory.storyItem;
        }

        @Override // org.telegram.ui.Components.ChatActivityEnterView.ChatActivityEnterViewDelegate
        public boolean onceVoiceAvailable() {
            TLRPC.User user;
            return (PeerStoriesView.this.dialogId < 0 || (user = MessagesController.getInstance(PeerStoriesView.this.currentAccount).getUser(Long.valueOf(PeerStoriesView.this.dialogId))) == null || UserObject.isUserSelf(user) || user.bot) ? false : true;
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void createMentionsContainer() {
        this.mentionContainer = new MentionsContainerView(getContext(), this.dialogId, 0L, this.storyViewer.fragment, this.resourcesProvider) { // from class: org.telegram.ui.Stories.PeerStoriesView.21
            @Override // org.telegram.ui.Components.MentionsContainerView
            protected boolean isStories() {
                return true;
            }

            @Override // org.telegram.ui.Components.MentionsContainerView
            public void drawRoundRect(Canvas canvas, Rect rect, float f) {
                PeerStoriesView.this.bitmapShaderTools.setBounds(getX(), -getY(), getX() + getMeasuredWidth(), (-getY()) + getMeasuredHeight());
                RectF rectF = AndroidUtilities.rectTmp;
                rectF.set(rect);
                rectF.offset(0.0f, 0.0f);
                canvas.drawRoundRect(rectF, f, f, PeerStoriesView.this.bitmapShaderTools.paint);
                canvas.drawRoundRect(rectF, f, f, PeerStoriesView.this.inputBackgroundPaint);
                if (rectF.top < getMeasuredHeight() - 1) {
                    canvas.drawRect(0.0f, getMeasuredHeight(), getMeasuredWidth(), getMeasuredHeight() - 1, PeerStoriesView.this.resourcesProvider.getPaint("paintDivider"));
                }
            }
        };
        AnonymousClass22 anonymousClass22 = new AnonymousClass22();
        this.mentionsDelegate = anonymousClass22;
        this.mentionContainer.withDelegate(anonymousClass22);
        addView(this.mentionContainer, LayoutHelper.createFrame(-1, -1, 83));
    }

    /* JADX INFO: renamed from: org.telegram.ui.Stories.PeerStoriesView$22, reason: invalid class name */
    class AnonymousClass22 implements MentionsContainerView.Delegate {
        AnonymousClass22() {
        }

        @Override // org.telegram.ui.Components.MentionsContainerView.Delegate
        public void onStickerSelected(final TLRPC.TL_document tL_document, final String str, final Object obj) {
            AlertsCreator.ensurePaidMessageConfirmation(PeerStoriesView.this.currentAccount, PeerStoriesView.this.dialogId, 1, new Utilities.Callback() { // from class: org.telegram.ui.Stories.PeerStoriesView$22$$ExternalSyntheticLambda1
                @Override // org.telegram.messenger.Utilities.Callback
                public final void run(Object obj2) {
                    this.f$0.lambda$onStickerSelected$0(tL_document, str, obj, (Long) obj2);
                }
            });
        }

        /* JADX INFO: Access modifiers changed from: private */
        public /* synthetic */ void lambda$onStickerSelected$0(TLRPC.TL_document tL_document, String str, Object obj, Long l) {
            SendMessagesHelper.getInstance(PeerStoriesView.this.currentAccount).sendSticker(tL_document, str, PeerStoriesView.this.dialogId, null, null, PeerStoriesView.this.currentStory.storyItem, null, null, true, 0, 0, false, obj, null, 0, l.longValue(), PeerStoriesView.this.chatActivityEnterView.getSendMonoForumPeerId(), PeerStoriesView.this.chatActivityEnterView.getSendMessageSuggestionParams());
            PeerStoriesView.this.chatActivityEnterView.addStickerToRecent(tL_document);
            PeerStoriesView.this.chatActivityEnterView.setFieldText(_UrlKt.FRAGMENT_ENCODE_SET);
            PeerStoriesView.this.afterMessageSend(l.longValue() <= 0);
        }

        @Override // org.telegram.ui.Components.MentionsContainerView.Delegate
        public void replaceText(int i, int i2, CharSequence charSequence, boolean z) {
            PeerStoriesView.this.chatActivityEnterView.replaceWithText(i, i2, charSequence, z);
        }

        @Override // org.telegram.ui.Components.MentionsContainerView.Delegate
        public Paint.FontMetricsInt getFontMetrics() {
            return PeerStoriesView.this.chatActivityEnterView.getEditField().getPaint().getFontMetricsInt();
        }

        @Override // org.telegram.ui.Components.MentionsContainerView.Delegate
        public void addEmojiToRecent(String str) {
            PeerStoriesView.this.chatActivityEnterView.addEmojiToRecent(str);
        }

        @Override // org.telegram.ui.Components.MentionsContainerView.Delegate
        public void sendBotInlineResult(final TLRPC.BotInlineResult botInlineResult, final boolean z, final int i) {
            AlertsCreator.ensurePaidMessageConfirmation(PeerStoriesView.this.currentAccount, PeerStoriesView.this.dialogId, 1, new Utilities.Callback() { // from class: org.telegram.ui.Stories.PeerStoriesView$22$$ExternalSyntheticLambda0
                @Override // org.telegram.messenger.Utilities.Callback
                public final void run(Object obj) {
                    this.f$0.lambda$sendBotInlineResult$1(botInlineResult, z, i, (Long) obj);
                }
            });
        }

        /* JADX INFO: Access modifiers changed from: private */
        public /* synthetic */ void lambda$sendBotInlineResult$1(TLRPC.BotInlineResult botInlineResult, boolean z, int i, Long l) {
            long contextBotId = PeerStoriesView.this.mentionContainer.getAdapter().getContextBotId();
            HashMap map = new HashMap();
            map.put("id", botInlineResult.id);
            map.put("query_id", _UrlKt.FRAGMENT_ENCODE_SET + botInlineResult.query_id);
            map.put("bot", _UrlKt.FRAGMENT_ENCODE_SET + contextBotId);
            map.put("bot_name", PeerStoriesView.this.mentionContainer.getAdapter().getContextBotName());
            SendMessagesHelper.prepareSendingBotContextResult(PeerStoriesView.this.storyViewer.fragment, PeerStoriesView.this.getAccountInstance(), botInlineResult, map, PeerStoriesView.this.dialogId, null, null, PeerStoriesView.this.currentStory.storyItem, null, z, i, 0, null, 0, l.longValue());
            PeerStoriesView.this.chatActivityEnterView.setFieldText(_UrlKt.FRAGMENT_ENCODE_SET);
            PeerStoriesView.this.afterMessageSend(l.longValue() <= 0);
            MediaDataController.getInstance(PeerStoriesView.this.currentAccount).increaseInlineRating(contextBotId);
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public boolean applyMessageToChat(final Runnable runnable) {
        if (MessagesController.getInstance(this.currentAccount).isFrozen()) {
            AccountFrozenAlert.show(this.currentAccount);
            return true;
        }
        int i = SharedConfig.stealthModeSendMessageConfirm;
        if (i > 0 && this.stealthModeIsActive) {
            int i2 = i - 1;
            SharedConfig.stealthModeSendMessageConfirm = i2;
            SharedConfig.updateStealthModeSendMessageConfirm(i2);
            AlertDialog alertDialog = new AlertDialog(getContext(), 0, this.resourcesProvider);
            alertDialog.setTitle(LocaleController.getString(R.string.StealthModeConfirmTitle));
            alertDialog.setMessage(LocaleController.getString(R.string.StealthModeConfirmMessage));
            alertDialog.setPositiveButton(LocaleController.getString(R.string.Proceed), new AlertDialog.OnButtonClickListener() { // from class: org.telegram.ui.Stories.PeerStoriesView$$ExternalSyntheticLambda41
                @Override // org.telegram.ui.ActionBar.AlertDialog.OnButtonClickListener
                public final void onClick(AlertDialog alertDialog2, int i3) {
                    runnable.run();
                }
            });
            alertDialog.setNegativeButton(LocaleController.getString(R.string.Cancel), new AlertDialog.OnButtonClickListener() { // from class: org.telegram.ui.Stories.PeerStoriesView$$ExternalSyntheticLambda42
                @Override // org.telegram.ui.ActionBar.AlertDialog.OnButtonClickListener
                public final void onClick(AlertDialog alertDialog2, int i3) {
                    alertDialog2.dismiss();
                }
            });
            alertDialog.show();
        } else {
            runnable.run();
        }
        return true;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void saveToGallery() {
        StoryItemHolder storyItemHolder = this.currentStory;
        TL_stories.StoryItem storyItem = storyItemHolder.storyItem;
        if ((storyItem == null && storyItemHolder.uploadingStory == null) || (storyItem instanceof TL_stories.TL_storyItemSkipped)) {
            return;
        }
        File path = storyItemHolder.getPath();
        final boolean zIsVideo = this.currentStory.isVideo();
        if (path != null && path.exists()) {
            MediaController.saveFile(path.toString(), getContext(), zIsVideo ? 1 : 0, null, null, new Utilities.Callback() { // from class: org.telegram.ui.Stories.PeerStoriesView$$ExternalSyntheticLambda3
                @Override // org.telegram.messenger.Utilities.Callback
                public final void run(Object obj) {
                    this.f$0.lambda$saveToGallery$32(zIsVideo, (Uri) obj);
                }
            });
            return;
        }
        showDownloadAlert();
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$saveToGallery$32(boolean z, Uri uri) {
        BulletinFactory.createSaveToGalleryBulletin(this.storyContainer, z, this.resourcesProvider).show();
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void showDownloadAlert() {
        AlertDialog.Builder builder = new AlertDialog.Builder(getContext(), this.resourcesProvider);
        builder.setTitle(LocaleController.getString(R.string.AppName));
        builder.setPositiveButton(LocaleController.getString(R.string.OK), null);
        builder.setMessage(LocaleController.getString(R.string.PleaseDownload));
        this.delegate.showDialog(builder.create());
    }

    /* JADX INFO: Access modifiers changed from: private */
    public long getMessageMinPrice() {
        StoryViewer storyViewer;
        if (!this.currentStory.isLive || (storyViewer = this.storyViewer) == null || storyViewer.livePlayer == null || disabledPaidFeatures(true)) {
            return 0L;
        }
        return this.storyViewer.livePlayer.getSendPaidMessagesStars();
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void openAttachMenu() {
        if (this.chatActivityEnterView == null) {
            return;
        }
        createChatAttachView();
        this.chatAttachAlert.getPhotoLayout().loadGalleryPhotos();
        this.chatAttachAlert.setMaxSelectedPhotos(-1, true);
        this.chatAttachAlert.setDialogId(this.dialogId);
        this.chatAttachAlert.init();
        this.chatAttachAlert.getCommentView().setText(this.chatActivityEnterView.getFieldText());
        this.delegate.showDialog(this.chatAttachAlert);
    }

    private void createChatAttachView() {
        if (this.chatAttachAlert == null) {
            ChatAttachAlert chatAttachAlert = new ChatAttachAlert(getContext(), null, false, false, true, this.resourcesProvider) { // from class: org.telegram.ui.Stories.PeerStoriesView.23
                @Override // org.telegram.ui.ActionBar.BottomSheet
                public void onDismissAnimationStart() {
                    if (PeerStoriesView.this.chatAttachAlert != null) {
                        PeerStoriesView.this.chatAttachAlert.setFocusable(false);
                    }
                    ChatActivityEnterView chatActivityEnterView = PeerStoriesView.this.chatActivityEnterView;
                    if (chatActivityEnterView == null || chatActivityEnterView.getEditField() == null) {
                        return;
                    }
                    PeerStoriesView.this.chatActivityEnterView.getEditField().requestFocus();
                }
            };
            this.chatAttachAlert = chatAttachAlert;
            chatAttachAlert.setDelegate(new ChatAttachAlert.ChatAttachViewDelegate() { // from class: org.telegram.ui.Stories.PeerStoriesView.24
                @Override // org.telegram.ui.Components.ChatAttachAlert.ChatAttachViewDelegate
                public /* synthetic */ void didSelectBot(TLRPC.User user) {
                    ChatAttachAlert.ChatAttachViewDelegate.CC.$default$didSelectBot(this, user);
                }

                @Override // org.telegram.ui.Components.ChatAttachAlert.ChatAttachViewDelegate
                public /* synthetic */ void onWallpaperSelected(Object obj) {
                    ChatAttachAlert.ChatAttachViewDelegate.CC.$default$onWallpaperSelected(this, obj);
                }

                @Override // org.telegram.ui.Components.ChatAttachAlert.ChatAttachViewDelegate
                public /* synthetic */ void openAvatarsSearch() {
                    ChatAttachAlert.ChatAttachViewDelegate.CC.$default$openAvatarsSearch(this);
                }

                @Override // org.telegram.ui.Components.ChatAttachAlert.ChatAttachViewDelegate
                public /* synthetic */ boolean selectItemOnClicking() {
                    return ChatAttachAlert.ChatAttachViewDelegate.CC.$default$selectItemOnClicking(this);
                }

                /* JADX WARN: Multi-variable type inference failed */
                @Override // org.telegram.ui.Components.ChatAttachAlert.ChatAttachViewDelegate
                public void didPressedButton(int i, boolean z, boolean z2, int i2, int i3, long j, boolean z3, boolean z4, long j2) {
                    String str;
                    if (PeerStoriesView.this.storyViewer.isShowing) {
                        PeerStoriesView peerStoriesView = PeerStoriesView.this;
                        TL_stories.StoryItem storyItem = peerStoriesView.currentStory.storyItem;
                        if (storyItem == null || (storyItem instanceof TL_stories.TL_storyItemSkipped)) {
                            return;
                        }
                        if (i == 8 || i == 7 || (i == 4 && !peerStoriesView.chatAttachAlert.getPhotoLayout().getSelectedPhotos().isEmpty())) {
                            if (i != 8) {
                                PeerStoriesView.this.chatAttachAlert.dismiss(true);
                            }
                            HashMap<Object, Object> selectedPhotos = PeerStoriesView.this.chatAttachAlert.getPhotoLayout().getSelectedPhotos();
                            ArrayList<Object> selectedPhotosOrder = PeerStoriesView.this.chatAttachAlert.getPhotoLayout().getSelectedPhotosOrder();
                            if (selectedPhotos.isEmpty()) {
                                return;
                            }
                            int i4 = 0;
                            int i5 = 0;
                            while (i5 < Math.ceil(selectedPhotos.size() / 10.0f)) {
                                int i6 = i5 * 10;
                                int iMin = Math.min(10, selectedPhotos.size() - i6);
                                ArrayList arrayList = new ArrayList();
                                for (int i7 = i4; i7 < iMin; i7++) {
                                    int i8 = i6 + i7;
                                    if (i8 < selectedPhotosOrder.size()) {
                                        MediaController.PhotoEntry photoEntry = (MediaController.PhotoEntry) selectedPhotos.get(selectedPhotosOrder.get(i8));
                                        SendMessagesHelper.SendingMediaInfo sendingMediaInfo = new SendMessagesHelper.SendingMediaInfo();
                                        boolean z5 = photoEntry.isVideo;
                                        if (!z5 && (str = photoEntry.imagePath) != null) {
                                            sendingMediaInfo.path = str;
                                        } else {
                                            String str2 = photoEntry.path;
                                            if (str2 != null) {
                                                sendingMediaInfo.path = str2;
                                            }
                                        }
                                        sendingMediaInfo.thumbPath = photoEntry.thumbPath;
                                        sendingMediaInfo.coverPath = photoEntry.coverPath;
                                        sendingMediaInfo.isVideo = z5;
                                        CharSequence charSequence = photoEntry.caption;
                                        sendingMediaInfo.caption = charSequence != null ? charSequence.toString() : null;
                                        sendingMediaInfo.entities = photoEntry.entities;
                                        sendingMediaInfo.masks = photoEntry.stickers;
                                        sendingMediaInfo.ttl = photoEntry.ttl;
                                        sendingMediaInfo.videoEditedInfo = photoEntry.editedInfo;
                                        sendingMediaInfo.canDeleteAfter = photoEntry.canDeleteAfter;
                                        sendingMediaInfo.updateStickersOrder = SendMessagesHelper.checkUpdateStickersOrder(photoEntry.caption);
                                        sendingMediaInfo.hasMediaSpoilers = photoEntry.hasSpoiler;
                                        arrayList.add(sendingMediaInfo);
                                        photoEntry.reset();
                                    }
                                }
                                SendMessagesHelper.prepareSendingMedia(PeerStoriesView.this.getAccountInstance(), arrayList, PeerStoriesView.this.dialogId, null, null, storyItem, null, (i == 4 || z4) ? 1 : i4, z, null, z2, i2, i3, 0, i5 == 0 ? ((SendMessagesHelper.SendingMediaInfo) arrayList.get(i4)).updateStickersOrder : i4, null, null, 0, 0L, false, 0L, PeerStoriesView.this.chatActivityEnterView.getSendMonoForumPeerId(), PeerStoriesView.this.chatActivityEnterView.getSendMessageSuggestionParams());
                                i5++;
                                selectedPhotos = selectedPhotos;
                                selectedPhotosOrder = selectedPhotosOrder;
                                i4 = i4;
                            }
                            int i9 = i4;
                            PeerStoriesView.this.chatActivityEnterView.setFieldText(_UrlKt.FRAGMENT_ENCODE_SET);
                            PeerStoriesView.this.afterMessageSend(j2 <= 0 ? 1 : i9);
                            return;
                        }
                        if (PeerStoriesView.this.chatAttachAlert != null) {
                            PeerStoriesView.this.chatAttachAlert.dismissWithButtonClick(i);
                        }
                    }
                }

                @Override // org.telegram.ui.Components.ChatAttachAlert.ChatAttachViewDelegate
                public void onCameraOpened() {
                    PeerStoriesView.this.chatActivityEnterView.closeKeyboard();
                }

                @Override // org.telegram.ui.Components.ChatAttachAlert.ChatAttachViewDelegate
                public void doOnIdle(Runnable runnable) {
                    NotificationCenter.getInstance(PeerStoriesView.this.currentAccount).doOnIdle(runnable);
                }

                @Override // org.telegram.ui.Components.ChatAttachAlert.ChatAttachViewDelegate
                public void sendAudio(ArrayList arrayList, CharSequence charSequence, boolean z, int i, int i2, long j, boolean z2, long j2) {
                    PeerStoriesView peerStoriesView = PeerStoriesView.this;
                    TL_stories.StoryItem storyItem = peerStoriesView.currentStory.storyItem;
                    if (storyItem == null || (storyItem instanceof TL_stories.TL_storyItemSkipped)) {
                        return;
                    }
                    SendMessagesHelper.prepareSendingAudioDocuments(peerStoriesView.getAccountInstance(), arrayList, charSequence != null ? charSequence : null, PeerStoriesView.this.dialogId, null, null, storyItem, z, i, i2, null, null, 0, j, z2, j2);
                    PeerStoriesView.this.afterMessageSend(j2 <= 0);
                }

                @Override // org.telegram.ui.Components.ChatAttachAlert.ChatAttachViewDelegate
                public boolean needEnterComment() {
                    return PeerStoriesView.this.needEnterText();
                }
            });
            this.chatAttachAlert.getPhotoLayout().loadGalleryPhotos();
            this.chatAttachAlert.setAllowEnterCaption(true);
            this.chatAttachAlert.init();
            this.chatAttachAlert.setDocumentsDelegate(new ChatAttachAlertDocumentLayout.DocumentSelectActivityDelegate() { // from class: org.telegram.ui.Stories.PeerStoriesView.25
                @Override // org.telegram.ui.Components.ChatAttachAlertDocumentLayout.DocumentSelectActivityDelegate
                public /* synthetic */ void didSelectPhotos(ArrayList arrayList, boolean z, int i, int i2, long j) {
                    ChatAttachAlertDocumentLayout.DocumentSelectActivityDelegate.CC.$default$didSelectPhotos(this, arrayList, z, i, i2, j);
                }

                @Override // org.telegram.ui.Components.ChatAttachAlertDocumentLayout.DocumentSelectActivityDelegate
                public /* synthetic */ void startMusicSelectActivity() {
                    ChatAttachAlertDocumentLayout.DocumentSelectActivityDelegate.CC.$default$startMusicSelectActivity(this);
                }

                @Override // org.telegram.ui.Components.ChatAttachAlertDocumentLayout.DocumentSelectActivityDelegate
                public void didSelectFiles(ArrayList arrayList, String str, ArrayList arrayList2, ArrayList arrayList3, boolean z, int i, int i2, long j, boolean z2, long j2) {
                    PeerStoriesView peerStoriesView = PeerStoriesView.this;
                    TL_stories.StoryItem storyItem = peerStoriesView.currentStory.storyItem;
                    if (storyItem == null || (storyItem instanceof TL_stories.TL_storyItemSkipped)) {
                        return;
                    }
                    SendMessagesHelper.prepareSendingDocuments(peerStoriesView.getAccountInstance(), arrayList, arrayList, null, str, null, PeerStoriesView.this.dialogId, null, null, storyItem, null, null, z, i, null, null, 0, 0L, false, j2);
                    PeerStoriesView.this.afterMessageSend(j2 <= 0);
                }

                @Override // org.telegram.ui.Components.ChatAttachAlertDocumentLayout.DocumentSelectActivityDelegate
                public void startDocumentSelectActivity() {
                    try {
                        Intent intent = new Intent("android.intent.action.GET_CONTENT");
                        intent.putExtra("android.intent.extra.ALLOW_MULTIPLE", true);
                        intent.setType("*/*");
                        PeerStoriesView.this.storyViewer.startActivityForResult(intent, 21);
                    } catch (Exception e) {
                        FileLog.e(e);
                    }
                }
            });
            this.chatAttachAlert.getCommentView().setText(this.chatActivityEnterView.getFieldText());
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void tryToOpenRepostStory() {
        if (MessagesController.getInstance(this.currentAccount).storiesEnabled()) {
            File path = this.currentStory.getPath();
            if (path != null && path.exists()) {
                ShareAlert shareAlert = this.shareAlert;
                if (shareAlert != null) {
                    shareAlert.dismiss();
                }
                AndroidUtilities.runOnUIThread(new Runnable() { // from class: org.telegram.ui.Stories.PeerStoriesView$$ExternalSyntheticLambda33
                    @Override // java.lang.Runnable
                    public final void run() {
                        this.f$0.openRepostStory();
                    }
                }, 120L);
                return;
            }
            showDownloadAlert();
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void shareStory(boolean z) {
        StoryItemHolder storyItemHolder = this.currentStory;
        if (storyItemHolder.storyItem == null || this.storyViewer.fragment == null) {
            return;
        }
        String strCreateLink = storyItemHolder.createLink();
        if (z) {
            ShareAlert shareAlert = new ShareAlert(this.storyViewer.fragment.getContext(), null, null, null, strCreateLink, null, false, strCreateLink, null, false, false, !DISABLE_STORY_REPOSTING && MessagesController.getInstance(this.currentAccount).storiesEnabled() && (!(this.isChannel || UserObject.isService(this.dialogId)) || ChatObject.isPublic(this.isChannel ? MessagesController.getInstance(this.currentAccount).getChat(Long.valueOf(-this.dialogId)) : null)), null, new WrappedResourceProvider(this.resourcesProvider) { // from class: org.telegram.ui.Stories.PeerStoriesView.26
                @Override // org.telegram.ui.WrappedResourceProvider
                public void appendColors() {
                    this.sparseIntArray.put(Theme.key_chat_emojiPanelBackground, ColorUtils.blendARGB(-16777216, -1, 0.2f));
                    this.sparseIntArray.put(Theme.key_chat_messagePanelIcons, ColorUtils.blendARGB(-16777216, -1, 0.5f));
                }
            }) { // from class: org.telegram.ui.Stories.PeerStoriesView.27
                @Override // org.telegram.ui.Components.ShareAlert, org.telegram.ui.ActionBar.BottomSheet
                public void dismissInternal() {
                    super.dismissInternal();
                    PeerStoriesView.this.shareAlert = null;
                }

                @Override // org.telegram.ui.Components.ShareAlert
                protected void onShareStory(View view) {
                    PeerStoriesView.this.tryToOpenRepostStory();
                }

                @Override // org.telegram.ui.Components.ShareAlert
                protected void onSend(LongSparseArray longSparseArray, int i, TLRPC.TL_forumTopic tL_forumTopic, boolean z2) {
                    if (z2) {
                        super.onSend(longSparseArray, i, tL_forumTopic, z2);
                        BulletinFactory bulletinFactoryOf = BulletinFactory.of(PeerStoriesView.this.storyContainer, this.resourcesProvider);
                        if (bulletinFactoryOf != null) {
                            if (longSparseArray.size() == 1) {
                                long jKeyAt = longSparseArray.keyAt(0);
                                if (jKeyAt == UserConfig.getInstance(this.currentAccount).clientUserId) {
                                    bulletinFactoryOf.createSimpleBulletin(R.raw.saved_messages, AndroidUtilities.replaceTags(LocaleController.formatString(R.string.StorySharedToSavedMessages, new Object[0])), 5000).hideAfterBottomSheet(false).show();
                                } else if (jKeyAt < 0) {
                                    bulletinFactoryOf.createSimpleBulletin(R.raw.forward, AndroidUtilities.replaceTags(LocaleController.formatString(R.string.StorySharedTo, tL_forumTopic != null ? tL_forumTopic.title : MessagesController.getInstance(this.currentAccount).getChat(Long.valueOf(-jKeyAt)).title)), 5000).hideAfterBottomSheet(false).show();
                                } else {
                                    bulletinFactoryOf.createSimpleBulletin(R.raw.forward, AndroidUtilities.replaceTags(LocaleController.formatString(R.string.StorySharedTo, MessagesController.getInstance(this.currentAccount).getUser(Long.valueOf(jKeyAt)).first_name)), 5000).hideAfterBottomSheet(false).show();
                                }
                            } else {
                                bulletinFactoryOf.createSimpleBulletin(R.raw.forward, AndroidUtilities.replaceTags(LocaleController.formatPluralString("StorySharedToManyChats", longSparseArray.size(), Integer.valueOf(longSparseArray.size())))).hideAfterBottomSheet(false).show();
                            }
                            try {
                                PeerStoriesView.this.performHapticFeedback(3);
                            } catch (Exception unused) {
                            }
                        }
                    }
                }
            };
            this.shareAlert = shareAlert;
            shareAlert.forceDarkThemeForHint = true;
            TL_stories.StoryItem storyItem = this.currentStory.storyItem;
            storyItem.dialogId = this.dialogId;
            shareAlert.setStoryToShare(storyItem);
            this.shareAlert.setDelegate(new ShareAlert.ShareAlertDelegate() { // from class: org.telegram.ui.Stories.PeerStoriesView.28
                @Override // org.telegram.ui.Components.ShareAlert.ShareAlertDelegate
                public /* synthetic */ void didShare() {
                    ShareAlert.ShareAlertDelegate.CC.$default$didShare(this);
                }

                @Override // org.telegram.ui.Components.ShareAlert.ShareAlertDelegate
                public boolean didCopy() {
                    PeerStoriesView.this.onLinkCopied();
                    return true;
                }
            });
            this.delegate.showDialog(this.shareAlert);
            return;
        }
        Intent intent = new Intent("android.intent.action.SEND");
        intent.setType("text/plain");
        intent.putExtra("android.intent.extra.TEXT", strCreateLink);
        LaunchActivity.instance.startActivityForResult(Intent.createChooser(intent, LocaleController.getString(R.string.StickersShare)), 500);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void openRepostStory() {
        final Activity activityFindActivity = AndroidUtilities.findActivity(getContext());
        if (activityFindActivity == null) {
            return;
        }
        Runnable runnable = new Runnable() { // from class: org.telegram.ui.Stories.PeerStoriesView$$ExternalSyntheticLambda44
            @Override // java.lang.Runnable
            public final void run() {
                this.f$0.lambda$openRepostStory$38(activityFindActivity);
            }
        };
        if (this.delegate.releasePlayer(runnable)) {
            return;
        }
        AndroidUtilities.runOnUIThread(runnable, 80L);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$openRepostStory$38(Activity activity) {
        StoryViewer.VideoPlayerHolder videoPlayerHolder;
        final StoryRecorder storyRecorder = StoryRecorder.getInstance(activity, this.currentAccount);
        VideoPlayerSharedScope videoPlayerSharedScope = this.playerSharedScope;
        storyRecorder.openForward(StoryRecorder.SourceView.fromStoryViewer(this.storyViewer), StoryEntry.repostStoryItem(this.currentStory.getPath(), this.currentStory.storyItem), (videoPlayerSharedScope == null || (videoPlayerHolder = videoPlayerSharedScope.player) == null) ? 0L : videoPlayerHolder.currentPosition, true);
        storyRecorder.setOnFullyOpenListener(new Runnable() { // from class: org.telegram.ui.Stories.PeerStoriesView$$ExternalSyntheticLambda49
            @Override // java.lang.Runnable
            public final void run() {
                this.f$0.lambda$openRepostStory$33();
            }
        });
        storyRecorder.setOnPrepareCloseListener(new Utilities.Callback4() { // from class: org.telegram.ui.Stories.PeerStoriesView$$ExternalSyntheticLambda50
            @Override // org.telegram.messenger.Utilities.Callback4
            public final void run(Object obj, Object obj2, Object obj3, Object obj4) {
                this.f$0.lambda$openRepostStory$37(storyRecorder, (Long) obj, (Runnable) obj2, (Boolean) obj3, (Long) obj4);
            }
        });
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$openRepostStory$33() {
        this.editOpened = true;
        setActive(false);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$openRepostStory$37(final StoryRecorder storyRecorder, Long l, final Runnable runnable, Boolean bool, final Long l2) {
        final DialogStoriesCell dialogStoriesCell;
        INavigationLayout parentLayout;
        if (bool.booleanValue()) {
            BaseFragment baseFragment = this.storyViewer.fragment;
            DialogStoriesCell.StoryCell storyCellFindStoryCell = null;
            if (baseFragment == null || (parentLayout = baseFragment.getParentLayout()) == null) {
                dialogStoriesCell = null;
                break;
            }
            List fragmentStack = parentLayout.getFragmentStack();
            ArrayList arrayList = new ArrayList();
            int size = fragmentStack.size() - 1;
            while (true) {
                if (size < 0) {
                    dialogStoriesCell = null;
                    break;
                }
                BaseFragment baseFragment2 = (BaseFragment) fragmentStack.get(size);
                if (baseFragment2 instanceof DialogsActivity) {
                    DialogsActivity dialogsActivity = (DialogsActivity) baseFragment2;
                    dialogsActivity.closeSearching();
                    DialogStoriesCell dialogStoriesCell2 = dialogsActivity.dialogStoriesCell;
                    storyCellFindStoryCell = dialogStoriesCell2 != null ? dialogStoriesCell2.findStoryCell(l2.longValue()) : null;
                    for (int i = 0; i < arrayList.size(); i++) {
                        parentLayout.removeFragmentFromStack((BaseFragment) arrayList.get(i));
                    }
                    dialogStoriesCell = dialogStoriesCell2;
                    break;
                }
                arrayList.add(baseFragment2);
                size--;
            }
            BaseFragment baseFragment3 = this.storyViewer.fragment;
            if (baseFragment3 != null) {
                baseFragment3.clearSheets();
            }
            this.storyViewer.instantClose();
            this.editOpened = false;
            if (dialogStoriesCell != null && dialogStoriesCell.scrollTo(l2.longValue())) {
                final DialogStoriesCell.StoryCell storyCell = storyCellFindStoryCell;
                dialogStoriesCell.afterNextLayout(new Runnable() { // from class: org.telegram.ui.Stories.PeerStoriesView$$ExternalSyntheticLambda53
                    @Override // java.lang.Runnable
                    public final void run() {
                        PeerStoriesView.$r8$lambda$uMZr2oFJQo_6IPHQMyJ80NyBLRA(storyCell, dialogStoriesCell, l2, storyRecorder, runnable);
                    }
                });
                return;
            } else {
                storyRecorder.replaceSourceView(StoryRecorder.SourceView.fromStoryCell(storyCellFindStoryCell));
                AndroidUtilities.runOnUIThread(runnable, 400L);
                return;
            }
        }
        final long jCurrentTimeMillis = System.currentTimeMillis();
        VideoPlayerSharedScope videoPlayerSharedScope = this.playerSharedScope;
        if (videoPlayerSharedScope != null && videoPlayerSharedScope.player == null) {
            this.delegate.setPopupIsVisible(false);
            setActive(true);
            this.editOpened = false;
            this.onImageReceiverThumbLoaded = new Runnable() { // from class: org.telegram.ui.Stories.PeerStoriesView$$ExternalSyntheticLambda54
                @Override // java.lang.Runnable
                public final void run() {
                    PeerStoriesView.m17586$r8$lambda$dGoH3A6sklctbRIH2lSj7RSHyg(runnable);
                }
            };
            if (bool.booleanValue()) {
                updatePosition();
            }
            AndroidUtilities.runOnUIThread(runnable, 400L);
            return;
        }
        StoryViewer.VideoPlayerHolder videoPlayerHolder = videoPlayerSharedScope.player;
        videoPlayerHolder.firstFrameRendered = false;
        videoPlayerSharedScope.firstFrameRendered = false;
        videoPlayerHolder.setOnReadyListener(new Runnable() { // from class: org.telegram.ui.Stories.PeerStoriesView$$ExternalSyntheticLambda55
            @Override // java.lang.Runnable
            public final void run() {
                PeerStoriesView.m17577$r8$lambda$5LKFz3khgZaUzGgbmsjLCOKKIU(runnable, jCurrentTimeMillis);
            }
        });
        this.delegate.setPopupIsVisible(false);
        RLottieImageView rLottieImageView = this.muteIconView;
        if (rLottieImageView != null) {
            rLottieImageView.setAnimation(this.sharedResources.muteDrawable);
        }
        setActive(((this.videoDuration <= 0 || l.longValue() <= this.videoDuration - 1400) ? l : 0L).longValue(), true);
        this.editOpened = false;
        AndroidUtilities.runOnUIThread(runnable, 400L);
        if (bool.booleanValue()) {
            updatePosition();
        }
    }

    public static /* synthetic */ void $r8$lambda$uMZr2oFJQo_6IPHQMyJ80NyBLRA(DialogStoriesCell.StoryCell storyCell, DialogStoriesCell dialogStoriesCell, Long l, StoryRecorder storyRecorder, Runnable runnable) {
        if (storyCell == null) {
            storyCell = dialogStoriesCell.findStoryCell(l.longValue());
        }
        storyRecorder.replaceSourceView(StoryRecorder.SourceView.fromStoryCell(storyCell));
        runnable.run();
    }

    /* JADX INFO: renamed from: $r8$lambda$dGoH3A6sk-lctbRIH2lSj7RSHyg, reason: not valid java name */
    public static /* synthetic */ void m17586$r8$lambda$dGoH3A6sklctbRIH2lSj7RSHyg(Runnable runnable) {
        AndroidUtilities.cancelRunOnUIThread(runnable);
        AndroidUtilities.runOnUIThread(runnable);
    }

    /* JADX INFO: renamed from: $r8$lambda$5LKF-z3khgZaUzGgbmsjLCOKKIU, reason: not valid java name */
    public static /* synthetic */ void m17577$r8$lambda$5LKFz3khgZaUzGgbmsjLCOKKIU(Runnable runnable, long j) {
        AndroidUtilities.cancelRunOnUIThread(runnable);
        AndroidUtilities.runOnUIThread(runnable, Math.max(0L, 32 - (System.currentTimeMillis() - j)));
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void onLinkCopied() {
        if (this.currentStory.storyItem == null) {
            return;
        }
        TL_stories.TL_stories_exportStoryLink tL_stories_exportStoryLink = new TL_stories.TL_stories_exportStoryLink();
        tL_stories_exportStoryLink.id = this.currentStory.storyItem.id;
        tL_stories_exportStoryLink.peer = MessagesController.getInstance(this.currentAccount).getInputPeer(this.dialogId);
        ConnectionsManager.getInstance(this.currentAccount).sendRequest(tL_stories_exportStoryLink, new RequestDelegate() { // from class: org.telegram.ui.Stories.PeerStoriesView.29
            @Override // org.telegram.tgnet.RequestDelegate
            public void run(TLObject tLObject, TLRPC.TL_error tL_error) {
            }
        });
    }

    public void setDay(long j, ArrayList arrayList, int i) {
        this.dialogId = j;
        this.day = arrayList;
        bindInternal(i);
    }

    public void setDialogId(long j, int i) {
        if (this.dialogId != j) {
            this.currentStory.clear();
        }
        this.dialogId = j;
        this.day = null;
        bindInternal(i);
        TL_stories.PeerStories peerStories = this.storyViewer.overrideUserStories;
        if (peerStories != null) {
            this.storiesController.loadSkippedStories(peerStories, true);
        } else {
            this.storiesController.loadSkippedStories(j);
        }
    }

    private void setTitle(boolean z, long j, boolean z2) {
        if (!z && j == this.titleLastDialogId && this.titleLastLive == z2) {
            return;
        }
        this.titleLastDialogId = j;
        this.titleLastLive = z2;
        if (j >= 0) {
            if (this.isSelf && !z2) {
                this.headerView.titleView.setText(LocaleController.getString(R.string.SelfStoryTitle));
                this.headerView.titleView.setRightDrawable((Drawable) null);
                return;
            }
            TLRPC.User user = MessagesController.getInstance(this.currentAccount).getUser(Long.valueOf(j));
            if (user != null && user.verified) {
                Drawable drawableMutate = ContextCompat.getDrawable(getContext(), R.drawable.verified_profile).mutate();
                drawableMutate.setAlpha(255);
                CombinedDrawable combinedDrawable = new CombinedDrawable(drawableMutate, null);
                combinedDrawable.setFullsize(true);
                combinedDrawable.setCustomSize(AndroidUtilities.dp(16.0f), AndroidUtilities.dp(16.0f));
                this.headerView.titleView.setRightDrawable(combinedDrawable);
            } else {
                this.headerView.titleView.setRightDrawable((Drawable) null);
            }
            if (user != null) {
                this.headerView.titleView.setText(Emoji.replaceEmoji(AndroidUtilities.removeDiacritics(ContactsController.formatName(user)), this.headerView.titleView.getPaint().getFontMetricsInt(), false));
                return;
            } else {
                this.headerView.titleView.setText(null);
                return;
            }
        }
        TLRPC.Chat chat = MessagesController.getInstance(this.currentAccount).getChat(Long.valueOf(-j));
        this.headerView.titleView.setText(AndroidUtilities.removeDiacritics(chat == null ? _UrlKt.FRAGMENT_ENCODE_SET : chat.title));
        if (chat != null && chat.verified) {
            Drawable drawableMutate2 = ContextCompat.getDrawable(getContext(), R.drawable.verified_profile).mutate();
            drawableMutate2.setAlpha(255);
            CombinedDrawable combinedDrawable2 = new CombinedDrawable(drawableMutate2, null);
            combinedDrawable2.setFullsize(true);
            combinedDrawable2.setCustomSize(AndroidUtilities.dp(16.0f), AndroidUtilities.dp(16.0f));
            this.headerView.titleView.setRightDrawable(combinedDrawable2);
            return;
        }
        this.headerView.titleView.setRightDrawable((Drawable) null);
    }

    private void bindInternal(int i) {
        this.deletedPeer = false;
        this.forceUpdateOffsets = true;
        this.userCanSeeViews = false;
        this.isChannel = false;
        this.isGroup = false;
        long j = this.dialogId;
        if (j >= 0) {
            this.isSelf = j == UserConfig.getInstance(this.currentAccount).getClientUserId();
            TLRPC.User user = MessagesController.getInstance(this.currentAccount).getUser(Long.valueOf(this.dialogId));
            TL_account.RequirementToContact requirementToContactIsUserContactBlocked = MessagesController.getInstance(this.currentAccount).isUserContactBlocked(this.dialogId);
            this.isPremiumBlocked = !UserConfig.getInstance(this.currentAccount).isPremium() && DialogObject.isPremiumBlocked(requirementToContactIsUserContactBlocked);
            this.starsPriceBlocked = DialogObject.getMessagesStarsPrice(requirementToContactIsUserContactBlocked);
            this.avatarDrawable.setInfo(this.currentAccount, user);
            this.headerView.backupImageView.getImageReceiver().setForUserOrChat(user, this.avatarDrawable);
            setTitle(true, this.dialogId, false);
        } else {
            this.isSelf = false;
            this.isChannel = true;
            if (this.storiesController.canEditStories(j) || BuildVars.DEBUG_PRIVATE_VERSION) {
                this.userCanSeeViews = true;
            }
            TLRPC.Chat chat = MessagesController.getInstance(this.currentAccount).getChat(Long.valueOf(-this.dialogId));
            boolean zIsChannelAndNotMegaGroup = ChatObject.isChannelAndNotMegaGroup(chat);
            this.isGroup = !zIsChannelAndNotMegaGroup;
            if (!zIsChannelAndNotMegaGroup && MessagesController.getInstance(this.currentAccount).getChatFull(-this.dialogId) == null) {
                MessagesStorage.getInstance(this.currentAccount).loadChatInfo(-this.dialogId, true, new CountDownLatch(1), false, false);
            }
            this.isPremiumBlocked = this.isGroup && !ChatObject.canSendPlain(chat);
            this.starsPriceBlocked = MessagesController.getInstance(this.currentAccount).getSendPaidMessagesStars(this.dialogId);
            this.avatarDrawable.setInfo(this.currentAccount, chat);
            this.headerView.backupImageView.getImageReceiver().setForUserOrChat(chat, this.avatarDrawable);
            setTitle(true, this.dialogId, false);
        }
        if (this.isActive && (this.isSelf || this.isChannel)) {
            this.storiesController.pollViewsForSelfStories(this.dialogId, true);
        }
        updateStoryItems();
        this.selectedPosition = i;
        if (i < 0) {
            this.selectedPosition = 0;
        }
        this.currentImageTime = 0L;
        this.switchEventSent = false;
        this.boostsStatus = null;
        this.canApplyBoost = null;
        int i2 = 8;
        if (this.isChannel) {
            createSelfPeerView();
            if (this.chatActivityEnterView == null && (this.isGroup || this.currentStory.isLive)) {
                createEnterView();
            }
            if (this.chatActivityEnterView != null) {
                TLRPC.Chat chat2 = MessagesController.getInstance(this.currentAccount).getChat(Long.valueOf(-this.dialogId));
                ChatActivityEnterView chatActivityEnterView = this.chatActivityEnterView;
                if (this.currentStory.isLive || (!isBotsPreview() && this.isGroup && (ChatObject.canSendPlain(chat2) || ChatObject.isPossibleRemoveChatRestrictionsByBoosts(chat2)))) {
                    i2 = 0;
                }
                chatActivityEnterView.setVisibility(i2);
                this.chatActivityEnterView.setLiveComment(this.currentStory.isLive, disabledPaidFeatures(true));
                this.chatActivityEnterView.setSuggestionButtonVisible(this.currentStory.isLive && !disabledPaidFeatures(true) && (this.keyboardVisible || this.chatActivityEnterView.emojiViewVisible), true);
                this.chatActivityEnterView.getEditField().setText(this.storyViewer.getDraft(this.dialogId, this.currentStory.storyItem));
                this.chatActivityEnterView.setDialogId(this.dialogId, this.currentAccount);
                this.chatActivityEnterView.updateRecordButton(chat2, null);
            }
            if (this.reactionsCounter == null) {
                AnimatedTextView.AnimatedTextDrawable animatedTextDrawable = new AnimatedTextView.AnimatedTextDrawable();
                this.reactionsCounter = animatedTextDrawable;
                animatedTextDrawable.setCallback(this.likeButtonContainer);
                this.reactionsCounter.setTextColor(this.resourcesProvider.getColor(Theme.key_windowBackgroundWhiteBlackText));
                this.reactionsCounter.setTextSize(AndroidUtilities.dp(14.0f));
                this.reactionsCounterProgress = new AnimatedFloat(this.likeButtonContainer);
            }
            if (this.repostButtonContainer != null && this.repostCounter == null) {
                AnimatedTextView.AnimatedTextDrawable animatedTextDrawable2 = new AnimatedTextView.AnimatedTextDrawable();
                this.repostCounter = animatedTextDrawable2;
                animatedTextDrawable2.setCallback(this.repostButtonContainer);
                this.repostCounter.setTextColor(this.resourcesProvider.getColor(Theme.key_windowBackgroundWhiteBlackText));
                this.repostCounter.setTextSize(AndroidUtilities.dp(14.0f));
                this.repostCounterProgress = new AnimatedFloat(this.repostButtonContainer);
            }
            if (i == -1) {
                updateSelectedPosition();
            }
            updatePosition();
            this.count = getStoriesCount();
            this.storyContainer.invalidate();
            invalidate();
        } else if (this.isSelf) {
            createSelfPeerView();
            if (this.currentStory.isLive) {
                this.selfView.setVisibility(8);
                if (this.chatActivityEnterView == null) {
                    createEnterView();
                }
                this.chatActivityEnterView.setVisibility(0);
            } else {
                this.selfView.setVisibility(0);
                ChatActivityEnterView chatActivityEnterView2 = this.chatActivityEnterView;
                if (chatActivityEnterView2 != null) {
                    chatActivityEnterView2.setVisibility(8);
                }
            }
            ChatActivityEnterView chatActivityEnterView3 = this.chatActivityEnterView;
            if (chatActivityEnterView3 != null) {
                chatActivityEnterView3.setLiveComment(this.currentStory.isLive, disabledPaidFeatures(true));
                this.chatActivityEnterView.setSuggestionButtonVisible(this.currentStory.isLive && !disabledPaidFeatures(true) && (this.keyboardVisible || this.chatActivityEnterView.emojiViewVisible), true);
            }
            if (i == -1) {
                ArrayList arrayList = this.day;
                if (arrayList != null) {
                    int iIndexOf = arrayList.indexOf(Integer.valueOf(this.storyViewer.dayStoryId));
                    if (iIndexOf < 0 && !this.day.isEmpty()) {
                        if (this.storyViewer.dayStoryId > ((Integer) this.day.get(0)).intValue()) {
                            iIndexOf = 0;
                        } else {
                            int i3 = this.storyViewer.dayStoryId;
                            ArrayList arrayList2 = this.day;
                            if (i3 < ((Integer) arrayList2.get(arrayList2.size() - 1)).intValue()) {
                                iIndexOf = this.day.size() - 1;
                            }
                        }
                    }
                    this.selectedPosition = Math.max(0, iIndexOf);
                } else if (!this.uploadingStories.isEmpty()) {
                    this.selectedPosition = this.storyItems.size();
                } else {
                    for (int i4 = 0; i4 < this.storyItems.size(); i4++) {
                        if (((TL_stories.StoryItem) this.storyItems.get(i4)).justUploaded || ((TL_stories.StoryItem) this.storyItems.get(i4)).id > this.storiesController.dialogIdToMaxReadId.get(this.dialogId)) {
                            this.selectedPosition = i4;
                            break;
                        }
                    }
                }
            }
            updatePosition();
            this.storyContainer.invalidate();
            invalidate();
        } else {
            if (this.chatActivityEnterView == null) {
                createEnterView();
            }
            if (this.isPremiumBlocked && this.premiumBlockedText == null) {
                createPremiumBlockedText();
            }
            if (this.premiumBlockedText != null) {
                if (this.isPremiumBlocked || this.areLiveCommentsDisabled) {
                    updatePremiumBlockedText();
                }
                this.premiumBlockedText.setVisibility(((!this.isPremiumBlocked || this.currentStory.isLive) && !this.areLiveCommentsDisabled) ? 8 : 0);
            }
            StoryFailView storyFailView = this.failView;
            if (storyFailView != null) {
                storyFailView.setVisibility(8);
            }
            if (i == -1) {
                updateSelectedPosition();
            }
            updatePosition();
            ChatActivityEnterView chatActivityEnterView4 = this.chatActivityEnterView;
            if (chatActivityEnterView4 != null) {
                chatActivityEnterView4.setVisibility((isBotsPreview() || UserObject.isService(this.dialogId)) ? 8 : 0);
                this.chatActivityEnterView.setLiveComment(this.currentStory.isLive, disabledPaidFeatures(true));
                this.chatActivityEnterView.setSuggestionButtonVisible(this.currentStory.isLive && !disabledPaidFeatures(true) && (this.keyboardVisible || this.chatActivityEnterView.emojiViewVisible), true);
                this.chatActivityEnterView.getEditField().setText(this.storyViewer.getDraft(this.dialogId, this.currentStory.storyItem));
                this.chatActivityEnterView.setDialogId(this.dialogId, this.currentAccount);
                TLRPC.UserFull userFull = MessagesController.getInstance(this.currentAccount).getUserFull(this.dialogId);
                if (userFull != null) {
                    this.chatActivityEnterView.updateRecordButton(null, userFull);
                } else {
                    MessagesController.getInstance(this.currentAccount).loadFullUser(MessagesController.getInstance(this.currentAccount).getUser(Long.valueOf(this.dialogId)), this.classGuid, false);
                }
            }
            this.count = getStoriesCount();
            FrameLayout frameLayout = this.selfView;
            if (frameLayout != null) {
                frameLayout.setVisibility(8);
            }
            this.storyContainer.invalidate();
            invalidate();
        }
        checkStealthMode(false);
    }

    private void createUnsupportedContainer() {
        if (this.unsupportedContainer != null) {
            return;
        }
        FrameLayout frameLayout = new FrameLayout(getContext());
        LinearLayout linearLayout = new LinearLayout(getContext());
        linearLayout.setOrientation(1);
        TextView textView = new TextView(getContext());
        textView.setTypeface(AndroidUtilities.bold());
        textView.setGravity(1);
        textView.setTextSize(1, 16.0f);
        textView.setText(LocaleController.getString(R.string.StoryUnsupported));
        textView.setTextColor(Theme.getColor(Theme.key_windowBackgroundWhiteBlackText, this.resourcesProvider));
        TextView textView2 = new TextView(getContext());
        ScaleStateListAnimator.apply(textView2);
        textView2.setText(LocaleController.getString(R.string.AppUpdate));
        int i = Theme.key_featuredStickers_buttonText;
        textView2.setTextColor(Theme.getColor(i, this.resourcesProvider));
        textView2.setPadding(AndroidUtilities.dp(16.0f), AndroidUtilities.dp(12.0f), AndroidUtilities.dp(16.0f), AndroidUtilities.dp(12.0f));
        textView2.setGravity(17);
        textView2.setTypeface(AndroidUtilities.bold());
        textView2.setTextSize(1, 15.0f);
        textView2.setBackground(Theme.createSimpleSelectorRoundRectDrawable(AndroidUtilities.dp(8.0f), Theme.getColor(Theme.key_featuredStickers_addButton, this.resourcesProvider), ColorUtils.setAlphaComponent(Theme.getColor(i, this.resourcesProvider), 30)));
        textView2.setOnClickListener(new View.OnClickListener() { // from class: org.telegram.ui.Stories.PeerStoriesView$$ExternalSyntheticLambda32
            @Override // android.view.View.OnClickListener
            public final void onClick(View view) {
                this.f$0.lambda$createUnsupportedContainer$39(view);
            }
        });
        linearLayout.addView(textView, LayoutHelper.createLinear(-1, -2));
        linearLayout.addView(textView2, LayoutHelper.createLinear(-1, -2, 0.0f, 24.0f, 0.0f, 0.0f));
        frameLayout.addView(linearLayout, LayoutHelper.createFrame(-1, -2.0f, 17, 72.0f, 0.0f, 72.0f, 0.0f));
        this.storyContainer.addView(frameLayout);
        this.unsupportedContainer = frameLayout;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$createUnsupportedContainer$39(View view) {
        Browser.openUrl(getContext(), BuildVars.GITHUB_APP_URL);
    }

    public void preloadMainImage(long j) {
        if (this.dialogId == j && this.day == null) {
            return;
        }
        this.dialogId = j;
        updateStoryItems();
        updateSelectedPosition();
        updatePosition(true);
        TL_stories.PeerStories peerStories = this.storyViewer.overrideUserStories;
        if (peerStories != null) {
            this.storiesController.loadSkippedStories(peerStories, true);
        } else {
            this.storiesController.loadSkippedStories(j);
        }
    }

    private void updateSelectedPosition() {
        TL_stories.PeerStories peerStories;
        int size;
        if (this.day != null) {
            ArrayList arrayList = this.uploadingStories;
            if (arrayList == null || arrayList.isEmpty()) {
                size = 0;
            } else {
                size = this.uploadingStories.size();
                for (int i = 0; i < this.uploadingStories.size(); i++) {
                    if (CameraTimestamp$$ExternalSyntheticBackport0.m(((StoriesController.UploadingStory) this.uploadingStories.get(i)).random_id) == this.storyViewer.dayStoryId) {
                        this.selectedPosition = i;
                        return;
                    }
                }
            }
            int iIndexOf = this.day.indexOf(Integer.valueOf(this.storyViewer.dayStoryId));
            if (iIndexOf < 0 && !this.day.isEmpty()) {
                if (this.storyViewer.dayStoryId > ((Integer) this.day.get(0)).intValue()) {
                    iIndexOf = 0;
                } else {
                    int i2 = this.storyViewer.dayStoryId;
                    ArrayList arrayList2 = this.day;
                    if (i2 < ((Integer) arrayList2.get(arrayList2.size() - 1)).intValue()) {
                        iIndexOf = this.day.size() - 1;
                    }
                }
            }
            this.selectedPosition = size + iIndexOf;
        } else {
            int i3 = this.storyViewer.savedPositions.get(this.dialogId, -1);
            this.selectedPosition = i3;
            if (i3 == -1 && !this.storyViewer.isSingleStory && (peerStories = this.userStories) != null && peerStories.max_read_id > 0) {
                for (int i4 = 0; i4 < this.storyItems.size(); i4++) {
                    if (((TL_stories.StoryItem) this.storyItems.get(i4)).id > this.userStories.max_read_id) {
                        this.selectedPosition = i4;
                        break;
                    }
                }
            }
        }
        if (this.selectedPosition == -1) {
            this.selectedPosition = 0;
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void updateStoryItems() {
        StoriesController.StoriesList storiesList;
        TL_stories.StoryItem storyItem;
        this.storyItems.clear();
        StoryViewer storyViewer = this.storyViewer;
        if (storyViewer.isSingleStory) {
            if (!storyViewer.singleStoryDeleted) {
                this.storyItems.add(storyViewer.singleStory);
            }
        } else {
            int i = 0;
            if (this.day != null && (storiesList = storyViewer.storiesList) != null) {
                if (storiesList instanceof StoriesController.BotPreviewsList) {
                    this.uploadingStories.clear();
                    ArrayList uploadingStories = MessagesController.getInstance(this.currentAccount).getStoriesController().getUploadingStories(this.dialogId);
                    String str = ((StoriesController.BotPreviewsList) this.storyViewer.storiesList).lang_code;
                    if (uploadingStories != null) {
                        for (int i2 = 0; i2 < uploadingStories.size(); i2++) {
                            StoriesController.UploadingStory uploadingStory = (StoriesController.UploadingStory) uploadingStories.get(i2);
                            StoryEntry storyEntry = uploadingStory.entry;
                            if (storyEntry != null && !storyEntry.isEdit && TextUtils.equals(storyEntry.botLang, str)) {
                                this.uploadingStories.add(uploadingStory);
                            }
                        }
                    }
                }
                ArrayList arrayList = this.day;
                int size = arrayList.size();
                while (i < size) {
                    Object obj = arrayList.get(i);
                    i++;
                    MessageObject messageObjectFindMessageObject = this.storyViewer.storiesList.findMessageObject(((Integer) obj).intValue());
                    if (messageObjectFindMessageObject != null && (storyItem = messageObjectFindMessageObject.storyItem) != null) {
                        this.storyItems.add(storyItem);
                    }
                }
            } else if (storyViewer.storiesList != null) {
                while (i < this.storyViewer.storiesList.messageObjects.size()) {
                    this.storyItems.add(((MessageObject) this.storyViewer.storiesList.messageObjects.get(i)).storyItem);
                    i++;
                }
            } else {
                TL_stories.PeerStories peerStories = storyViewer.overrideUserStories;
                if (peerStories != null && DialogObject.getPeerDialogId(peerStories.peer) == this.dialogId) {
                    this.userStories = this.storyViewer.overrideUserStories;
                } else {
                    TL_stories.PeerStories stories = this.storiesController.getStories(this.dialogId);
                    this.userStories = stories;
                    if (stories == null) {
                        this.userStories = this.storiesController.getStoriesFromFullPeer(this.dialogId);
                    }
                }
                this.totalStoriesCount = 0;
                TL_stories.PeerStories peerStories2 = this.userStories;
                if (peerStories2 != null) {
                    this.totalStoriesCount = peerStories2.stories.size();
                    this.storyItems.addAll(this.userStories.stories);
                }
                this.uploadingStories.clear();
                ArrayList uploadingStories2 = this.storiesController.getUploadingStories(this.dialogId);
                if (uploadingStories2 != null) {
                    this.uploadingStories.addAll(uploadingStories2);
                }
            }
        }
        this.count = getStoriesCount();
    }

    private void createSelfPeerView() {
        if (this.selfView != null) {
            return;
        }
        FrameLayout frameLayout = new FrameLayout(getContext()) { // from class: org.telegram.ui.Stories.PeerStoriesView.30
            @Override // android.view.ViewGroup, android.view.View
            protected void dispatchDraw(Canvas canvas) {
                int x;
                if (PeerStoriesView.this.selfAvatarsContainer.getVisibility() == 0 && PeerStoriesView.this.selfAvatarsContainer.getLayoutParams().width != (x = (int) (((PeerStoriesView.this.selfStatusView.getX() + PeerStoriesView.this.selfStatusView.getMeasuredWidth()) - PeerStoriesView.this.selfAvatarsContainer.getX()) + AndroidUtilities.dp(10.0f)))) {
                    PeerStoriesView.this.selfAvatarsContainer.getLayoutParams().width = x;
                    PeerStoriesView.this.selfAvatarsContainer.invalidate();
                    PeerStoriesView.this.selfAvatarsContainer.requestLayout();
                }
                super.dispatchDraw(canvas);
            }
        };
        this.selfView = frameLayout;
        frameLayout.setClickable(true);
        addView(this.selfView, LayoutHelper.createFrame(-1, 48.0f, 48, 0.0f, 0.0f, 136.0f, 0.0f));
        View view = new View(getContext()) { // from class: org.telegram.ui.Stories.PeerStoriesView.31
            LoadingDrawable loadingDrawable = new LoadingDrawable();
            AnimatedFloat animatedFloat = new AnimatedFloat(250, CubicBezierInterpolator.DEFAULT);

            @Override // android.view.View
            protected void onDraw(Canvas canvas) {
                Canvas canvas2;
                super.onDraw(canvas);
                this.animatedFloat.setParent(this);
                this.animatedFloat.set(PeerStoriesView.this.showViewsProgress ? 1.0f : 0.0f, false);
                if (this.animatedFloat.get() != 0.0f) {
                    if (this.animatedFloat.get() != 1.0f) {
                        canvas2 = canvas;
                        canvas2.saveLayerAlpha(0.0f, 0.0f, getLayoutParams().width, getMeasuredHeight(), (int) (this.animatedFloat.get() * 255.0f), 31);
                    } else {
                        canvas2 = canvas;
                        canvas2.save();
                    }
                    RectF rectF = AndroidUtilities.rectTmp;
                    rectF.set(0.0f, 0.0f, getLayoutParams().width, getMeasuredHeight());
                    this.loadingDrawable.setBounds(rectF);
                    this.loadingDrawable.setRadiiDp(24.0f);
                    this.loadingDrawable.setColors(ColorUtils.setAlphaComponent(-1, 20), ColorUtils.setAlphaComponent(-1, 50), ColorUtils.setAlphaComponent(-1, 50), ColorUtils.setAlphaComponent(-1, 70));
                    this.loadingDrawable.draw(canvas2);
                    invalidate();
                    canvas2.restore();
                }
            }
        };
        this.selfAvatarsContainer = view;
        view.setOnClickListener(new View.OnClickListener() { // from class: org.telegram.ui.Stories.PeerStoriesView$$ExternalSyntheticLambda35
            @Override // android.view.View.OnClickListener
            public final void onClick(View view2) {
                this.f$0.lambda$createSelfPeerView$40(view2);
            }
        });
        this.selfView.addView(this.selfAvatarsContainer, LayoutHelper.createFrame(-1, 32.0f, 0, 9.0f, 11.0f, 0.0f, 0.0f));
        HwAvatarsImageView hwAvatarsImageView = new HwAvatarsImageView(getContext(), false);
        this.selfAvatarsView = hwAvatarsImageView;
        hwAvatarsImageView.setAvatarsTextSize(AndroidUtilities.dp(18.0f));
        this.selfView.addView(this.selfAvatarsView, LayoutHelper.createFrame(-1, 28.0f, 0, 13.0f, 13.0f, 0.0f, 0.0f));
        TextView textView = new TextView(getContext());
        this.selfStatusView = textView;
        textView.setTextSize(1, 14.0f);
        this.selfStatusView.setTextColor(-1);
        this.selfView.addView(this.selfStatusView, LayoutHelper.createFrame(-2, -2.0f, 0, 0.0f, 16.0f, 0.0f, 9.0f));
        ImageView imageView = new ImageView(getContext());
        imageView.setImageDrawable(this.sharedResources.deleteDrawable);
        this.selfAvatarsContainer.setBackground(Theme.createSimpleSelectorRoundRectDrawable(AndroidUtilities.dp(15.0f), 0, ColorUtils.setAlphaComponent(-1, 120)));
        imageView.setBackground(Theme.createCircleSelectorDrawable(ColorUtils.setAlphaComponent(-1, 120), -AndroidUtilities.dp(2.0f), -AndroidUtilities.dp(2.0f)));
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$createSelfPeerView$40(View view) {
        showUserViewsDialog();
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void deleteStory() {
        AlertDialog.Builder builder = new AlertDialog.Builder(getContext(), this.resourcesProvider);
        builder.setTitle(LocaleController.getString(isBotsPreview() ? R.string.DeleteBotPreviewTitle : R.string.DeleteStoryTitle));
        builder.setMessage(LocaleController.getString(isBotsPreview() ? R.string.DeleteBotPreviewSubtitle : R.string.DeleteStorySubtitle));
        builder.setPositiveButton(LocaleController.getString(R.string.Delete), new AlertDialog.OnButtonClickListener() { // from class: org.telegram.ui.Stories.PeerStoriesView$$ExternalSyntheticLambda0
            @Override // org.telegram.ui.ActionBar.AlertDialog.OnButtonClickListener
            public final void onClick(AlertDialog alertDialog, int i) {
                this.f$0.lambda$deleteStory$41(alertDialog, i);
            }
        });
        builder.setNegativeButton(LocaleController.getString(R.string.Cancel), new AlertDialog.OnButtonClickListener() { // from class: org.telegram.ui.Stories.PeerStoriesView$$ExternalSyntheticLambda1
            @Override // org.telegram.ui.ActionBar.AlertDialog.OnButtonClickListener
            public final void onClick(AlertDialog alertDialog, int i) {
                alertDialog.dismiss();
            }
        });
        AlertDialog alertDialogCreate = builder.create();
        this.delegate.showDialog(alertDialogCreate);
        alertDialogCreate.redPositive();
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$deleteStory$41(AlertDialog alertDialog, int i) {
        TL_stories.StoryItem storyItem;
        if (this.currentStory.isLive && (storyItem = this.currentStory.storyItem) != null) {
            TLRPC.MessageMedia messageMedia = storyItem.media;
            if (messageMedia instanceof TLRPC.TL_messageMediaVideoStream) {
                TLRPC.InputGroupCall inputGroupCall = ((TLRPC.TL_messageMediaVideoStream) messageMedia).call;
                LivePlayer livePlayer = LivePlayer.recording;
                if (livePlayer != null && livePlayer.equals(inputGroupCall)) {
                    LivePlayer.recording.destroy();
                    if (LivePlayer.recording != null) {
                        LivePlayer.recording = null;
                        NotificationCenter.getInstance(this.currentAccount).lambda$postNotificationNameOnUIThread$1(NotificationCenter.liveStoryUpdated, Long.valueOf(LivePlayer.recording.getCallId()));
                    }
                }
            }
        }
        this.currentStory.cancelOrDelete();
        updateStoryItems();
        if (this.isActive && this.count == 0) {
            this.delegate.switchToNextAndRemoveCurrentPeer();
            return;
        }
        int i2 = this.selectedPosition;
        int i3 = this.count;
        if (i2 >= i3) {
            this.selectedPosition = i3 - 1;
        } else if (i2 < 0) {
            this.selectedPosition = 0;
        }
        updatePosition();
        StoryViewer storyViewer = this.storyViewer;
        if (storyViewer != null) {
            storyViewer.checkSelfStoriesView();
        }
    }

    private void showUserViewsDialog() {
        this.storyViewer.openViews();
    }

    @Override // org.telegram.ui.Components.SizeNotifierFrameLayout, android.widget.FrameLayout, android.view.ViewGroup, android.view.View
    protected void onLayout(boolean z, int i, int i2, int i3, int i4) {
        super.onLayout(z, i, i2, i3, i4);
        this.sharedResources.topOverlayGradient.setBounds(0, 0, getMeasuredWidth(), AndroidUtilities.dp(72.0f));
    }

    @Override // org.telegram.ui.Components.SizeNotifierFrameLayout, android.view.ViewGroup, android.view.View
    protected void dispatchDraw(Canvas canvas) {
        AnimatedTextView.AnimatedTextDrawable animatedTextDrawable;
        AnimatedTextView.AnimatedTextDrawable animatedTextDrawable2;
        updateViewOffsets();
        if (this.isChannel && (animatedTextDrawable2 = this.reactionsCounter) != null) {
            animatedTextDrawable2.setBounds(0, 0, getMeasuredWidth(), AndroidUtilities.dp(40.0f));
        }
        if (this.isChannel && (animatedTextDrawable = this.repostCounter) != null) {
            animatedTextDrawable.setBounds(0, 0, getMeasuredWidth(), AndroidUtilities.dp(40.0f));
        }
        super.dispatchDraw(canvas);
        if (this.movingReaction) {
            float x = this.bottomActionsLinearLayout.getX() + this.likeButtonContainer.getX() + (this.likeButtonContainer.getMeasuredWidth() / 2.0f);
            float y = this.bottomActionsLinearLayout.getY() + this.likeButtonContainer.getY() + (this.likeButtonContainer.getMeasuredHeight() / 2.0f);
            int iDp = AndroidUtilities.dp(24.0f);
            float f = iDp / 2.0f;
            float fLerp = AndroidUtilities.lerp(this.movingReactionFromX, x - f, CubicBezierInterpolator.EASE_OUT.getInterpolation(this.movingReactionProgress));
            float fLerp2 = AndroidUtilities.lerp(this.movingReactionFromY, y - f, this.movingReactionProgress);
            int iLerp = AndroidUtilities.lerp(this.movingReactionFromSize, iDp, this.movingReactionProgress);
            if (this.drawAnimatedEmojiAsMovingReaction) {
                AnimatedEmojiDrawable animatedEmojiDrawable = this.reactionMoveDrawable;
                if (animatedEmojiDrawable != null) {
                    float f2 = iLerp;
                    animatedEmojiDrawable.setBounds((int) fLerp, (int) fLerp2, (int) (fLerp + f2), (int) (fLerp2 + f2));
                    this.reactionMoveDrawable.draw(canvas);
                }
            } else {
                float f3 = iLerp;
                this.reactionMoveImageReceiver.setImageCoords(fLerp, fLerp2, f3, f3);
                this.reactionMoveImageReceiver.draw(canvas);
            }
        }
        if (this.drawReactionEffect) {
            float x2 = this.bottomActionsLinearLayout.getX() + this.likeButtonContainer.getX() + (this.likeButtonContainer.getMeasuredWidth() / 2.0f);
            float y2 = this.bottomActionsLinearLayout.getY() + this.likeButtonContainer.getY() + (this.likeButtonContainer.getMeasuredHeight() / 2.0f);
            int iDp2 = AndroidUtilities.dp(120.0f);
            if (!this.drawAnimatedEmojiAsMovingReaction) {
                float f4 = iDp2;
                float f5 = f4 / 2.0f;
                this.reactionEffectImageReceiver.setImageCoords(x2 - f5, y2 - f5, f4, f4);
                this.reactionEffectImageReceiver.draw(canvas);
                if (this.reactionEffectImageReceiver.getLottieAnimation() != null && this.reactionEffectImageReceiver.getLottieAnimation().isLastFrame()) {
                    this.drawReactionEffect = false;
                }
            } else {
                AnimatedEmojiEffect animatedEmojiEffect = this.emojiReactionEffect;
                if (animatedEmojiEffect != null) {
                    float f6 = iDp2 / 2.0f;
                    animatedEmojiEffect.setBounds((int) (x2 - f6), (int) (y2 - f6), (int) (x2 + f6), (int) (y2 + f6));
                    this.emojiReactionEffect.draw(canvas);
                    if (this.emojiReactionEffect.isDone()) {
                        this.emojiReactionEffect.removeView(this);
                        this.emojiReactionEffect = null;
                        this.drawReactionEffect = false;
                    }
                } else {
                    this.drawReactionEffect = false;
                }
            }
        }
        ChatActivityEnterView chatActivityEnterView = this.chatActivityEnterView;
        if (chatActivityEnterView != null) {
            chatActivityEnterView.drawRecordedPannel(canvas);
        }
    }

    @Override // org.telegram.ui.Components.SizeNotifierFrameLayout, android.view.ViewGroup, android.view.View
    protected void onAttachedToWindow() {
        super.onAttachedToWindow();
        this.attachedToWindow = true;
        this.imageReceiver.onAttachedToWindow();
        this.rightPreloadImageReceiver.onAttachedToWindow();
        this.leftPreloadImageReceiver.onAttachedToWindow();
        this.reactionEffectImageReceiver.onAttachedToWindow();
        this.reactionMoveImageReceiver.onAttachedToWindow();
        ChatActivityEnterView chatActivityEnterView = this.chatActivityEnterView;
        if (chatActivityEnterView != null) {
            chatActivityEnterView.onResume();
        }
        for (int i = 0; i < this.preloadReactionHolders.size(); i++) {
            ((ReactionImageHolder) this.preloadReactionHolders.get(i)).onAttachedToWindow(true);
        }
        NotificationCenter.getInstance(this.currentAccount).addObserver(this, NotificationCenter.chatInfoDidLoad);
        NotificationCenter.getInstance(this.currentAccount).addObserver(this, NotificationCenter.liveStoryUpdated);
        NotificationCenter.getInstance(this.currentAccount).addObserver(this, NotificationCenter.storiesUpdated);
        NotificationCenter.getInstance(this.currentAccount).addObserver(this, NotificationCenter.storyQualityUpdate);
        NotificationCenter.getInstance(this.currentAccount).addObserver(this, NotificationCenter.storiesListUpdated);
        NotificationCenter.getInstance(this.currentAccount).addObserver(this, NotificationCenter.stealthModeChanged);
        NotificationCenter.getInstance(this.currentAccount).addObserver(this, NotificationCenter.storiesLimitUpdate);
        NotificationCenter.getInstance(this.currentAccount).addObserver(this, NotificationCenter.userIsPremiumBlockedUpadted);
        NotificationCenter.getInstance(this.currentAccount).addObserver(this, NotificationCenter.didLoadSendAsPeers);
        NotificationCenter.getGlobalInstance().addObserver(this, NotificationCenter.emojiLoaded);
    }

    @Override // org.telegram.ui.Components.SizeNotifierFrameLayout, android.view.ViewGroup, android.view.View
    protected void onDetachedFromWindow() {
        super.onDetachedFromWindow();
        this.attachedToWindow = false;
        this.imageReceiver.onDetachedFromWindow();
        this.rightPreloadImageReceiver.onDetachedFromWindow();
        this.leftPreloadImageReceiver.onDetachedFromWindow();
        this.reactionEffectImageReceiver.onDetachedFromWindow();
        this.reactionMoveImageReceiver.onDetachedFromWindow();
        ChatActivityEnterView chatActivityEnterView = this.chatActivityEnterView;
        if (chatActivityEnterView != null) {
            chatActivityEnterView.onPause();
            this.chatActivityEnterView.onDestroy();
        }
        AnimatedEmojiDrawable animatedEmojiDrawable = this.reactionMoveDrawable;
        if (animatedEmojiDrawable != null) {
            animatedEmojiDrawable.removeView(this);
            this.reactionMoveDrawable = null;
        }
        AnimatedEmojiEffect animatedEmojiEffect = this.emojiReactionEffect;
        if (animatedEmojiEffect != null) {
            animatedEmojiEffect.removeView(this);
            this.emojiReactionEffect = null;
        }
        for (int i = 0; i < this.preloadReactionHolders.size(); i++) {
            ((ReactionImageHolder) this.preloadReactionHolders.get(i)).onAttachedToWindow(false);
        }
        NotificationCenter.getInstance(this.currentAccount).removeObserver(this, NotificationCenter.chatInfoDidLoad);
        NotificationCenter.getInstance(this.currentAccount).removeObserver(this, NotificationCenter.liveStoryUpdated);
        NotificationCenter.getInstance(this.currentAccount).removeObserver(this, NotificationCenter.storiesUpdated);
        NotificationCenter.getInstance(this.currentAccount).removeObserver(this, NotificationCenter.storyQualityUpdate);
        NotificationCenter.getInstance(this.currentAccount).removeObserver(this, NotificationCenter.storiesListUpdated);
        NotificationCenter.getInstance(this.currentAccount).removeObserver(this, NotificationCenter.stealthModeChanged);
        NotificationCenter.getInstance(this.currentAccount).removeObserver(this, NotificationCenter.storiesLimitUpdate);
        NotificationCenter.getInstance(this.currentAccount).removeObserver(this, NotificationCenter.userIsPremiumBlockedUpadted);
        NotificationCenter.getInstance(this.currentAccount).removeObserver(this, NotificationCenter.didLoadSendAsPeers);
        NotificationCenter.getGlobalInstance().removeObserver(this, NotificationCenter.emojiLoaded);
    }

    @Override // org.telegram.messenger.NotificationCenter.NotificationCenterDelegate
    public void didReceivedNotification(int i, int i2, Object... objArr) {
        LivePlayer livePlayer;
        if (i != NotificationCenter.storiesUpdated) {
            boolean z = false;
            if (i != NotificationCenter.storiesListUpdated || this.storyViewer.storiesList != objArr[0]) {
                if (i == NotificationCenter.storyQualityUpdate) {
                    updatePosition();
                    return;
                }
                if (i == NotificationCenter.emojiLoaded) {
                    this.storyCaptionView.captionTextview.invalidate();
                    return;
                }
                if (i == NotificationCenter.stealthModeChanged) {
                    checkStealthMode(true);
                    return;
                }
                if (i == NotificationCenter.storiesLimitUpdate) {
                    StoriesController.StoryLimit storyLimitCheckStoryLimit = MessagesController.getInstance(this.currentAccount).getStoriesController().checkStoryLimit();
                    if (storyLimitCheckStoryLimit == null || !storyLimitCheckStoryLimit.active(this.currentAccount) || this.delegate == null) {
                        return;
                    }
                    this.delegate.showDialog(new LimitReachedBottomSheet(fragmentForLimit(), findActivity(), storyLimitCheckStoryLimit.getLimitReachedType(), this.currentAccount, null));
                    return;
                }
                if (i == NotificationCenter.userIsPremiumBlockedUpadted) {
                    TL_account.RequirementToContact requirementToContactIsUserContactBlocked = MessagesController.getInstance(this.currentAccount).isUserContactBlocked(this.dialogId);
                    if (this.dialogId >= 0 && !UserConfig.getInstance(this.currentAccount).isPremium() && DialogObject.isPremiumBlocked(requirementToContactIsUserContactBlocked)) {
                        z = true;
                    }
                    if (this.isPremiumBlocked == z && this.starsPriceBlocked == DialogObject.getMessagesStarsPrice(requirementToContactIsUserContactBlocked)) {
                        return;
                    }
                    this.isPremiumBlocked = z;
                    this.starsPriceBlocked = DialogObject.getMessagesStarsPrice(requirementToContactIsUserContactBlocked);
                    updatePosition();
                    checkStealthMode(true);
                    return;
                }
                if (i == NotificationCenter.chatInfoDidLoad) {
                    Object obj = objArr[0];
                    if ((obj instanceof TLRPC.ChatFull) && this.dialogId == (-((TLRPC.ChatFull) obj).id)) {
                        updatePosition();
                        return;
                    }
                    return;
                }
                if (i == NotificationCenter.liveStoryUpdated) {
                    long jLongValue = ((Long) objArr[0]).longValue();
                    StoryViewer storyViewer = this.storyViewer;
                    if (storyViewer == null || (livePlayer = storyViewer.livePlayer) == null || livePlayer.getCallId() != jLongValue) {
                        return;
                    }
                    updatePosition();
                    ChatActivityEnterView chatActivityEnterView = this.chatActivityEnterView;
                    if (chatActivityEnterView != null) {
                        chatActivityEnterView.checkSendButton(true);
                        this.chatActivityEnterView.updateSendAsButton(true);
                        checkStealthMode(true);
                    }
                    LiveCommentsView liveCommentsView = this.liveCommentsView;
                    if (liveCommentsView != null) {
                        liveCommentsView.updatedMinStars();
                        return;
                    }
                    return;
                }
                if (i == NotificationCenter.didLoadSendAsPeers && ((Boolean) objArr[2]).booleanValue()) {
                    loadSendAsPeers(true);
                    return;
                }
                return;
            }
        }
        Delegate delegate = this.delegate;
        if (delegate == null || !delegate.isClosed()) {
            if (this.isActive) {
                updateStoryItems();
                if (this.count == 0) {
                    if (this.deletedPeer) {
                        return;
                    }
                    this.deletedPeer = true;
                    this.delegate.switchToNextAndRemoveCurrentPeer();
                    return;
                }
                if (this.selectedPosition >= this.storyItems.size() + this.uploadingStories.size()) {
                    this.selectedPosition = (this.storyItems.size() + this.uploadingStories.size()) - 1;
                }
                updatePosition();
                if (this.isSelf || this.isChannel) {
                    updateUserViews(true);
                }
            }
            TL_stories.PeerStories peerStories = this.storyViewer.overrideUserStories;
            if (peerStories != null) {
                this.storiesController.loadSkippedStories(peerStories, true);
            } else {
                long j = this.dialogId;
                if (j != 0) {
                    this.storiesController.loadSkippedStories(j);
                }
            }
            ActionBarMenuSubItem actionBarMenuSubItem = this.editStoryItem;
            if (actionBarMenuSubItem != null) {
                actionBarMenuSubItem.animate().alpha((this.storiesController.hasUploadingStories(this.dialogId) && this.currentStory.isVideo && !SharedConfig.allowPreparingHevcPlayers()) ? 0.5f : 1.0f).start();
            }
        }
    }

    private void loadSendAsPeers(boolean z) {
        LivePlayer livePlayer;
        if (this.sendAsPeersObj != null) {
            return;
        }
        StoryViewer storyViewer = this.storyViewer;
        if (storyViewer == null || (livePlayer = storyViewer.livePlayer) == null || !livePlayer.sendAsDisabled()) {
            TLRPC.TL_channels_sendAsPeers sendAsPeers = MessagesController.getInstance(this.currentAccount).getSendAsPeers(this.dialogId, true);
            this.sendAsPeersObj = sendAsPeers;
            ChatActivityEnterView chatActivityEnterView = this.chatActivityEnterView;
            if (chatActivityEnterView == null || sendAsPeers == null) {
                return;
            }
            chatActivityEnterView.updateSendAsButton(z);
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$new$43() {
        checkStealthMode(true);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void checkStealthMode(boolean z) {
        if (this.chatActivityEnterView != null && this.isVisible && this.attachedToWindow) {
            AndroidUtilities.cancelRunOnUIThread(this.updateStealthModeTimer);
            TL_stories.TL_storiesStealthMode stealthMode = this.storiesController.getStealthMode();
            this.chatActivityEnterView.checkSendButton(true);
            if ((this.isPremiumBlocked && !this.currentStory.isLive) || this.areLiveCommentsDisabled) {
                this.stealthModeIsActive = false;
                this.chatActivityEnterView.setEnabled(false);
                this.chatActivityEnterView.setOverrideHint(" ", z);
                return;
            }
            if (this.starsPriceBlocked > 0) {
                this.stealthModeIsActive = false;
                this.chatActivityEnterView.setEnabled(true);
                this.chatActivityEnterView.setOverrideHint(StarsIntroActivity.replaceStars(LocaleController.formatString(R.string.TypeMessageForStars, LocaleController.formatNumber(this.starsPriceBlocked, ','))), z);
                return;
            }
            if (!this.currentStory.isLive && stealthMode != null) {
                int currentTime = ConnectionsManager.getInstance(this.currentAccount).getCurrentTime();
                int i = stealthMode.active_until_date;
                if (currentTime < i) {
                    this.stealthModeIsActive = true;
                    int currentTime2 = i - ConnectionsManager.getInstance(this.currentAccount).getCurrentTime();
                    int i2 = currentTime2 / 60;
                    int i3 = currentTime2 % 60;
                    int i4 = R.string.StealthModeActiveHintShort;
                    Locale locale = Locale.US;
                    int iMeasureText = (int) this.chatActivityEnterView.getEditField().getPaint().measureText(LocaleController.formatString(i4, String.format(locale, "%02d:%02d", 99, 99)));
                    this.chatActivityEnterView.setEnabled(true);
                    if (iMeasureText * 1.2f >= this.chatActivityEnterView.getEditField().getMeasuredWidth()) {
                        this.chatActivityEnterView.setOverrideHint(LocaleController.formatString(R.string.StealthModeActiveHintShort, _UrlKt.FRAGMENT_ENCODE_SET), String.format(locale, "%02d:%02d", Integer.valueOf(i2), Integer.valueOf(i3)), z);
                    } else {
                        this.chatActivityEnterView.setOverrideHint(LocaleController.formatString(R.string.StealthModeActiveHint, String.format(locale, "%02d:%02d", Integer.valueOf(i2), Integer.valueOf(i3))), z);
                    }
                    AndroidUtilities.runOnUIThread(this.updateStealthModeTimer, 1000L);
                    return;
                }
            }
            this.stealthModeIsActive = false;
            this.chatActivityEnterView.setEnabled(true);
            if (this.currentStory.isLive) {
                long starsPrice = this.chatActivityEnterView.getStarsPrice();
                if (starsPrice > 0) {
                    this.chatActivityEnterView.setOverrideHint(StarsIntroActivity.replaceStars(LocaleController.formatString(R.string.CommentFor, LocaleController.formatNumber((int) starsPrice, ',')), this.chatActivityEnterView.spans), z);
                    ColoredImageSpan coloredImageSpan = this.chatActivityEnterView.spans[0];
                    if (coloredImageSpan != null) {
                        coloredImageSpan.spaceScaleX = 0.9f;
                        return;
                    }
                    return;
                }
                this.chatActivityEnterView.setOverrideHint(LocaleController.getString(R.string.Comment), z);
                return;
            }
            this.chatActivityEnterView.setOverrideHint(LocaleController.getString(this.isGroup ? R.string.ReplyToGroupStory : R.string.ReplyPrivately), z);
        }
    }

    public void updatePosition() {
        updatePosition(false);
    }

    /* JADX WARN: Code duplicated, block: B:131:0x03b9  */
    /* JADX WARN: Code duplicated, block: B:205:0x04be  */
    /* JADX WARN: Code duplicated, block: B:215:0x04e7  */
    /* JADX WARN: Code duplicated, block: B:224:0x04fe  */
    /* JADX WARN: Code duplicated, block: B:232:0x0511  */
    /* JADX WARN: Code duplicated, block: B:246:0x0539  */
    /* JADX WARN: Code duplicated, block: B:247:0x053b  */
    /* JADX WARN: Code duplicated, block: B:252:0x0577  */
    /* JADX WARN: Code duplicated, block: B:253:0x057f  */
    /* JADX WARN: Code duplicated, block: B:254:0x0586  */
    /* JADX WARN: Code duplicated, block: B:257:0x058f  */
    /* JADX WARN: Code duplicated, block: B:260:0x05a2  */
    /* JADX WARN: Code duplicated, block: B:262:0x05a8  */
    /* JADX WARN: Code duplicated, block: B:264:0x05b5 A[DONT_INVERT] */
    /* JADX WARN: Code duplicated, block: B:265:0x05b7  */
    /* JADX WARN: Code duplicated, block: B:274:0x05dd  */
    /* JADX WARN: Code duplicated, block: B:276:0x05f9  */
    /* JADX WARN: Code duplicated, block: B:278:0x05fd  */
    /* JADX WARN: Code duplicated, block: B:281:0x0609  */
    /* JADX WARN: Code duplicated, block: B:282:0x0619  */
    /* JADX WARN: Code duplicated, block: B:284:0x061f  */
    /* JADX WARN: Code duplicated, block: B:286:0x0627  */
    /* JADX WARN: Code duplicated, block: B:291:0x0630  */
    /* JADX WARN: Code duplicated, block: B:292:0x0638  */
    /* JADX WARN: Code duplicated, block: B:294:0x063c  */
    /* JADX WARN: Code duplicated, block: B:296:0x0648  */
    /* JADX WARN: Code duplicated, block: B:298:0x064e  */
    /* JADX WARN: Code duplicated, block: B:300:0x0654  */
    /* JADX WARN: Code duplicated, block: B:302:0x0675  */
    /* JADX WARN: Code duplicated, block: B:306:0x0691  */
    /* JADX WARN: Code duplicated, block: B:308:0x0696  */
    /* JADX WARN: Code duplicated, block: B:309:0x069e  */
    /* JADX WARN: Code duplicated, block: B:311:0x06ae  */
    /* JADX WARN: Code duplicated, block: B:313:0x06dc  */
    /* JADX WARN: Code duplicated, block: B:315:0x0705  */
    /* JADX WARN: Code duplicated, block: B:316:0x071c  */
    /* JADX WARN: Code duplicated, block: B:318:0x0736  */
    /* JADX WARN: Code duplicated, block: B:319:0x073c  */
    /* JADX WARN: Code duplicated, block: B:321:0x0746  */
    /* JADX WARN: Code duplicated, block: B:324:0x078e  */
    /* JADX WARN: Code duplicated, block: B:326:0x0794  */
    /* JADX WARN: Code duplicated, block: B:339:0x085b  */
    /* JADX WARN: Code duplicated, block: B:341:0x0893  */
    /* JADX WARN: Code duplicated, block: B:343:0x089a  */
    /* JADX WARN: Code duplicated, block: B:345:0x089e  */
    /* JADX WARN: Code duplicated, block: B:353:0x08b4  */
    /* JADX WARN: Code duplicated, block: B:358:0x08dd  */
    /* JADX WARN: Code duplicated, block: B:361:0x08e4  */
    /* JADX WARN: Code duplicated, block: B:377:0x090e  */
    /* JADX WARN: Code duplicated, block: B:396:0x094d  */
    /* JADX WARN: Code duplicated, block: B:399:0x0952  */
    /* JADX WARN: Code duplicated, block: B:401:0x0956  */
    /* JADX WARN: Code duplicated, block: B:411:0x096c  */
    /* JADX WARN: Code duplicated, block: B:423:0x0991  */
    /* JADX WARN: Code duplicated, block: B:434:0x09ab  */
    /* JADX WARN: Code duplicated, block: B:436:0x09c6  */
    /* JADX WARN: Code duplicated, block: B:439:0x09cd  */
    /* JADX WARN: Code duplicated, block: B:442:0x09d4  */
    /* JADX WARN: Code duplicated, block: B:443:0x09da  */
    /* JADX WARN: Code duplicated, block: B:445:0x09e0  */
    /* JADX WARN: Code duplicated, block: B:446:0x09f2  */
    /* JADX WARN: Code duplicated, block: B:449:0x09fb  */
    /* JADX WARN: Code duplicated, block: B:451:0x09ff  */
    /* JADX WARN: Code duplicated, block: B:453:0x0a12  */
    /* JADX WARN: Code duplicated, block: B:477:0x0a53  */
    /* JADX WARN: Code duplicated, block: B:479:0x0a5b  */
    /* JADX WARN: Code duplicated, block: B:480:0x0a61  */
    /* JADX WARN: Code duplicated, block: B:491:0x0a92  */
    /* JADX WARN: Code duplicated, block: B:500:0x0aa6  */
    /* JADX WARN: Code duplicated, block: B:504:0x0aae  */
    /* JADX WARN: Code duplicated, block: B:516:0x0acd  */
    /* JADX WARN: Code duplicated, block: B:527:0x0ae7  */
    /* JADX WARN: Code duplicated, block: B:532:0x0af5  */
    /* JADX WARN: Code duplicated, block: B:536:0x0afd  */
    /* JADX WARN: Code duplicated, block: B:539:0x0b08  */
    /* JADX WARN: Code duplicated, block: B:540:0x0b12  */
    /* JADX WARN: Code duplicated, block: B:542:0x0b16  */
    /* JADX WARN: Code duplicated, block: B:545:0x0b1d  */
    /* JADX WARN: Code duplicated, block: B:547:0x0b23  */
    /* JADX WARN: Code duplicated, block: B:548:0x0b25  */
    /* JADX WARN: Code duplicated, block: B:552:0x0b2d  */
    /* JADX WARN: Code duplicated, block: B:557:0x0b3b  */
    /* JADX WARN: Code duplicated, block: B:561:0x0b5a  */
    /* JADX WARN: Code duplicated, block: B:570:0x0b78  */
    /* JADX WARN: Code duplicated, block: B:577:0x0b8b  */
    /* JADX WARN: Code duplicated, block: B:584:0x0b9f  */
    /* JADX WARN: Code duplicated, block: B:588:0x0ba7  */
    /* JADX WARN: Code duplicated, block: B:593:0x0bb7  */
    /* JADX WARN: Code duplicated, block: B:599:0x0bcb  */
    /* JADX WARN: Code duplicated, block: B:606:0x0be8  */
    /* JADX WARN: Code duplicated, block: B:609:0x0bef  */
    /* JADX WARN: Code duplicated, block: B:612:0x0bfe  */
    /* JADX WARN: Code duplicated, block: B:617:0x0c0b  */
    /* JADX WARN: Code duplicated, block: B:622:0x0c23  */
    /* JADX WARN: Code duplicated, block: B:624:0x0c29  */
    /* JADX WARN: Code duplicated, block: B:629:0x0c33  */
    /* JADX WARN: Code duplicated, block: B:632:0x0c3a  */
    /* JADX WARN: Code duplicated, block: B:633:0x0c3c  */
    /* JADX WARN: Code duplicated, block: B:637:0x0c4b  */
    /* JADX WARN: Code duplicated, block: B:641:0x0c62  */
    /* JADX WARN: Code duplicated, block: B:643:0x0c68  */
    /* JADX WARN: Code duplicated, block: B:645:0x0c76  */
    /* JADX WARN: Code duplicated, block: B:647:0x0c84  */
    /* JADX WARN: Code duplicated, block: B:649:0x0c97  */
    /* JADX WARN: Code duplicated, block: B:657:0x0cc0  */
    /* JADX WARN: Code duplicated, block: B:662:0x0cd0  */
    /* JADX WARN: Code duplicated, block: B:665:0x0cd9  */
    /* JADX WARN: Code duplicated, block: B:670:0x0ce7  */
    /* JADX WARN: Code duplicated, block: B:677:0x0cfb  */
    /* JADX WARN: Code duplicated, block: B:679:0x0d00  */
    /* JADX WARN: Code duplicated, block: B:684:0x0d10  */
    /* JADX WARN: Code duplicated, block: B:687:0x0d19  */
    /* JADX WARN: Code duplicated, block: B:693:0x0d2c  */
    /* JADX WARN: Code duplicated, block: B:697:0x0d52  */
    /* JADX WARN: Code duplicated, block: B:705:0x0d86  */
    /* JADX WARN: Code duplicated, block: B:713:0x0dae  */
    /* JADX WARN: Code duplicated, block: B:716:0x0dbf  */
    /* JADX WARN: Code duplicated, block: B:718:0x0dcd  */
    /* JADX WARN: Code duplicated, block: B:719:0x0dcf  */
    /* JADX WARN: Code duplicated, block: B:722:0x0ddb  */
    /* JADX WARN: Code duplicated, block: B:723:0x0de7  */
    /* JADX WARN: Code duplicated, block: B:725:0x0dff  */
    /* JADX WARN: Code duplicated, block: B:728:0x0e0a  */
    /* JADX WARN: Code duplicated, block: B:733:0x0e16  */
    /* JADX WARN: Code duplicated, block: B:736:0x0e1c  */
    /* JADX WARN: Code duplicated, block: B:738:0x0e20  */
    /* JADX WARN: Code duplicated, block: B:743:0x0e2c  */
    /* JADX WARN: Code duplicated, block: B:745:0x0e31  */
    /* JADX WARN: Code duplicated, block: B:751:0x0e3e  */
    /* JADX WARN: Code duplicated, block: B:755:0x0e50  */
    /* JADX WARN: Code duplicated, block: B:756:0x0e59  */
    /* JADX WARN: Code duplicated, block: B:759:0x0e5f  */
    /* JADX WARN: Code duplicated, block: B:765:0x0e77  */
    /* JADX WARN: Code duplicated, block: B:776:0x0ecb  */
    /* JADX WARN: Code duplicated, block: B:778:0x0ecf  */
    /* JADX WARN: Code duplicated, block: B:780:0x0ed3  */
    /* JADX WARN: Code duplicated, block: B:782:0x0edb  */
    /* JADX WARN: Code duplicated, block: B:785:0x0f07  */
    /* JADX WARN: Code duplicated, block: B:793:0x0f60  */
    /* JADX WARN: Code duplicated, block: B:800:0x0f85  */
    /* JADX WARN: Code duplicated, block: B:824:0x0fe5  */
    /* JADX WARN: Code duplicated, block: B:827:0x0ff5  */
    /* JADX WARN: Code duplicated, block: B:836:0x100d  */
    /* JADX WARN: Code duplicated, block: B:840:0x1015  */
    /* JADX WARN: Code duplicated, block: B:845:0x1025  */
    /* JADX WARN: Code duplicated, block: B:853:? A[RETURN, SYNTHETIC] */
    /* JADX WARN: Code duplicated, block: B:92:0x0292  */
    /* JADX WARN: Instruction removed from duplicated block: B:92:0x0292, please report this as an issue */
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
    private void updatePosition(boolean z) {
        TL_stories.StoryItem storyItem;
        StoriesController.UploadingStory uploadingStory;
        int i;
        boolean z2;
        boolean z3;
        boolean z4;
        boolean z5;
        StoryViewer.TransitionViewHolder transitionViewHolder;
        ImageReceiver imageReceiver;
        TL_stories.StoryItem storyItem2;
        TL_stories.StoryItem storyItem3;
        TL_stories.StoryItem storyItem4;
        StoryItemHolder storyItemHolder;
        boolean z6;
        boolean z7;
        StoryViewer storyViewer;
        boolean z8;
        long j;
        ChatActivityEnterView chatActivityEnterView;
        boolean z9;
        StoriesController.UploadingStory uploadingStory2;
        RadialProgress radialProgress;
        TL_stories.StoryItem storyItem5;
        StoriesController.UploadingStory uploadingStory3;
        StoryItemHolder storyItemHolder2;
        TL_stories.StoryItem storyItem6;
        int i2;
        CharSequence charSequence;
        String storyDate;
        CharSequence charSequence2;
        TL_stories.StoryItem storyItem7;
        SpannableStringBuilder spannableStringBuilder;
        final StoryCaptionView.Reply reply;
        SpannableStringBuilder spannableStringBuilder2;
        String str;
        AvatarSpan avatarSpan;
        TLRPC.Chat chat;
        StoryViewer storyViewer2;
        LivePlayer livePlayer;
        TL_stories.StoryItem storyItem8;
        CharSequence charSequence3;
        TLRPC.MessageMedia messageMedia;
        TLRPC.Document document;
        TLRPC.Photo photo;
        HintView2 hintView2;
        HintView2 hintView3;
        StoryViewer storyViewer3;
        CharSequence charSequence4;
        StoriesController.StoriesList storiesList;
        TL_stories.StoryItem storyItem9;
        CharSequence spannableStringBuilder3;
        CharSequence string;
        TL_stories.StoryItem storyItem10;
        StoryItemHolder storyItemHolder3;
        Delegate delegate;
        boolean z10;
        LinearLayout linearLayout;
        TLRPC.Chat chat2;
        ChatActivityEnterView chatActivityEnterView2;
        ChatActivityEnterView chatActivityEnterView3;
        ChatActivityEnterView chatActivityEnterView4;
        ChatActivityEnterView chatActivityEnterView5;
        FrameLayout frameLayout;
        FrameLayout frameLayout2;
        TextView textView;
        LinearLayout linearLayout2;
        int i3;
        int i4;
        View.OnLongClickListener onLongClickListener;
        boolean z11;
        CommentButton commentButton;
        MuteButton muteButton;
        TL_stories.StoryItem storyItem11;
        int i5;
        FrameLayout frameLayout3;
        int i6;
        int i7;
        int i8;
        StoryItemHolder storyItemHolder4;
        StoriesController.UploadingStory uploadingStory4;
        TL_stories.StoryItem storyItem12;
        boolean z12;
        boolean z13;
        float f;
        StoriesController.UploadingStory uploadingStory5;
        ViewPropertyAnimator viewPropertyAnimator;
        StoriesController.StoriesList storiesList2;
        int iDp;
        HintView2 hintView4;
        int i9;
        ImageView imageView;
        ImageView imageView2;
        int i10;
        int i11;
        StoryItemHolder storyItemHolder5;
        TL_stories.StoryItem storyItem13;
        TLRPC.Reaction reaction;
        boolean z14;
        float f2;
        TL_stories.StoryItem storyItem14;
        int i12;
        MessageObject messageObject;
        int i13;
        FrameLayout frameLayout4;
        int i14;
        int i15;
        TLRPC.MessageMedia messageMedia2;
        ChatActivityEnterView chatActivityEnterView6;
        StoryItemHolder storyItemHolder6;
        boolean z15;
        boolean z16;
        StoryItemHolder storyItemHolder7;
        TL_stories.StoryItem storyItem15;
        int i16;
        int i17;
        FrameLayout.LayoutParams layoutParams;
        MuteButton muteButton2;
        float f3;
        int iDp2;
        int i18;
        LivePlayer livePlayer2;
        boolean z17;
        LivePlayer livePlayer3;
        boolean z18;
        LivePlayer livePlayer4;
        int i19;
        ChatActivityEnterView chatActivityEnterView7;
        FrameLayout frameLayout5;
        LinearLayout linearLayout3;
        ChatActivityEnterView chatActivityEnterView8;
        StoryViewer storyViewer4;
        LivePlayer livePlayer5;
        String str2;
        LivePlayer livePlayer6;
        TL_stories.StoryItem storyItem16;
        BitmapDrawable bitmapDrawable;
        StoriesController.UploadingStory uploadingStory6;
        StoriesController.StoriesList storiesList3;
        if (this.storyItems.isEmpty() && this.uploadingStories.isEmpty()) {
            return;
        }
        this.forceUpdateOffsets = true;
        StoryItemHolder storyItemHolder8 = this.currentStory;
        TL_stories.StoryItem storyItem17 = storyItemHolder8.storyItem;
        StoriesController.UploadingStory uploadingStory7 = storyItemHolder8.uploadingStory;
        String storyImageFilter = StoriesUtilities.getStoryImageFilter();
        this.lastNoThumb = false;
        this.unsupported = false;
        int i20 = this.selectedPosition;
        boolean z19 = this.isUploading;
        boolean z20 = this.isEditing;
        boolean z21 = this.isFailed;
        StoryViewer storyViewer5 = this.storyViewer;
        int i21 = 4;
        if (storyViewer5 != null && (storiesList3 = storyViewer5.storiesList) != null && storiesList3.type == 4) {
            uploadingStory = (i20 < 0 || i20 >= this.uploadingStories.size()) ? null : (StoriesController.UploadingStory) this.uploadingStories.get(i20);
            int size = i20 - this.uploadingStories.size();
            storyItem = (size < 0 || size >= this.storyItems.size()) ? null : (TL_stories.StoryItem) this.storyItems.get(size);
        } else {
            storyItem = (i20 < 0 || i20 >= this.storyItems.size()) ? null : (TL_stories.StoryItem) this.storyItems.get(i20);
            int size2 = i20 - this.storyItems.size();
            uploadingStory = (size2 < 0 || size2 >= this.uploadingStories.size()) ? null : (StoriesController.UploadingStory) this.uploadingStories.get(size2);
        }
        TL_stories.StoryItem storyItem18 = storyItem;
        this.currentStory.editingSourceItem = null;
        if (uploadingStory != null) {
            this.isEditing = false;
            boolean z22 = uploadingStory.failed;
            this.isFailed = z22;
            this.isUploading = !z22;
            this.imageReceiver.setCrossfadeWithOldImage(false);
            this.imageReceiver.setCrossfadeDuration(150);
            Bitmap bitmap = uploadingStory.entry.thumbBitmap;
            if (bitmap != null) {
                Bitmap bitmapCreateBitmap = Bitmap.createBitmap(bitmap);
                Utilities.blurBitmap(bitmapCreateBitmap, 3, 1, bitmapCreateBitmap.getWidth(), bitmapCreateBitmap.getHeight(), bitmapCreateBitmap.getRowBytes());
                bitmapDrawable = new BitmapDrawable(bitmapCreateBitmap);
            } else {
                bitmapDrawable = null;
            }
            if (uploadingStory.isVideo || uploadingStory.hadFailed) {
                i = i20;
                z2 = z19;
                z3 = z20;
                z4 = z21;
                uploadingStory6 = uploadingStory;
                this.imageReceiver.setImage(null, null, ImageLocation.getForPath(uploadingStory6.firstFramePath), storyImageFilter, null, null, bitmapDrawable, 0L, null, null, 0);
            } else {
                z3 = z20;
                i = i20;
                z2 = z19;
                z4 = z21;
                uploadingStory6 = uploadingStory;
                this.imageReceiver.setImage(null, null, ImageLocation.getForPath(uploadingStory.path), storyImageFilter, null, null, bitmapDrawable, 0L, null, null, 0);
            }
            this.currentStory.set(uploadingStory6);
            this.storyAreasView.set(null, StoryMediaAreasView.getMediaAreasFor(uploadingStory6.entry), this.emojiAnimationsOverlay);
            this.allowShareLink = false;
            this.allowRepost = false;
            this.allowShare = false;
        } else {
            i = i20;
            z2 = z19;
            z3 = z20;
            z4 = z21;
            i21 = 4;
            this.isUploading = false;
            this.isEditing = false;
            this.isFailed = false;
            if (storyItem18 == null) {
                StoryViewer storyViewer6 = this.storyViewer;
                if (storyViewer6 != null) {
                    storyViewer6.close(true);
                    return;
                }
                return;
            }
            StoriesController.UploadingStory uploadingStoryFindEditingStory = this.storiesController.findEditingStory(this.dialogId, storyItem18);
            if (uploadingStoryFindEditingStory != null) {
                this.isEditing = true;
                this.imageReceiver.setCrossfadeWithOldImage(false);
                this.imageReceiver.setCrossfadeDuration(this.onImageReceiverThumbLoaded != null ? 0 : 150);
                if (uploadingStoryFindEditingStory.isVideo) {
                    this.imageReceiver.setImage(null, null, ImageLocation.getForPath(uploadingStoryFindEditingStory.firstFramePath), storyImageFilter, null, 0L, null, null, 0);
                } else {
                    this.imageReceiver.setImage(null, null, ImageLocation.getForPath(uploadingStoryFindEditingStory.firstFramePath), storyImageFilter, null, 0L, null, null, 0);
                }
                this.currentStory.set(uploadingStoryFindEditingStory);
                this.storyAreasView.set(null, StoryMediaAreasView.getMediaAreasFor(uploadingStoryFindEditingStory.entry), this.emojiAnimationsOverlay);
                this.currentStory.editingSourceItem = storyItem18;
                this.allowShareLink = false;
                this.allowRepost = false;
                this.allowShare = false;
            } else {
                TLRPC.MessageMedia messageMedia3 = storyItem18.media;
                boolean zEndsWith = messageMedia3 != null && MessageObject.isVideoDocument(messageMedia3.getDocument());
                storyItem18.dialogId = this.dialogId;
                this.imageReceiver.setCrossfadeWithOldImage(z3);
                this.imageReceiver.setCrossfadeDuration(150);
                TLRPC.MessageMedia messageMedia4 = storyItem18.media;
                if (messageMedia4 instanceof TLRPC.TL_messageMediaUnsupported) {
                    this.unsupported = true;
                    MessagesController.getInstance(this.currentAccount).getStoriesController().checkUnsupportedStory(this.dialogId, storyItem18.id);
                    z5 = z3;
                } else {
                    String str3 = storyItem18.attachPath;
                    if (str3 != null) {
                        if (messageMedia4 == null) {
                            zEndsWith = str3.toLowerCase().endsWith(".mp4");
                        }
                        if (zEndsWith) {
                            TLRPC.MessageMedia messageMedia5 = storyItem18.media;
                            Drawable drawableCreateStripedBitmap = messageMedia5 != null ? ImageLoader.createStripedBitmap(messageMedia5.getDocument().thumbs) : null;
                            if (storyItem18.firstFramePath != null) {
                                if (ImageLoader.getInstance().isInMemCache(ImageLocation.getForPath(storyItem18.firstFramePath).getKey(null, null, false) + "@" + storyImageFilter, false)) {
                                    z5 = z3;
                                    storyItem2 = storyItem18;
                                    this.imageReceiver.setImage(null, null, ImageLocation.getForPath(storyItem18.firstFramePath), storyImageFilter, null, null, drawableCreateStripedBitmap, 0L, null, null, 0);
                                } else {
                                    z5 = z3;
                                    storyItem2 = storyItem18;
                                    this.imageReceiver.setImage(null, null, ImageLocation.getForPath(storyItem2.attachPath), storyImageFilter + "_pframe", null, null, drawableCreateStripedBitmap, 0L, null, null, 0);
                                }
                            } else {
                                z5 = z3;
                                storyItem2 = storyItem18;
                                this.imageReceiver.setImage(null, null, ImageLocation.getForPath(storyItem2.attachPath), storyImageFilter + "_pframe", null, null, drawableCreateStripedBitmap, 0L, null, null, 0);
                            }
                        } else {
                            z5 = z3;
                            storyItem2 = storyItem18;
                            TLRPC.MessageMedia messageMedia6 = storyItem2.media;
                            TLRPC.Photo photo2 = messageMedia6 != null ? messageMedia6.photo : null;
                            Drawable drawableCreateStripedBitmap2 = photo2 != null ? ImageLoader.createStripedBitmap(photo2.sizes) : null;
                            if (z5) {
                                this.imageReceiver.setImage(ImageLocation.getForPath(storyItem2.attachPath), storyImageFilter, ImageLocation.getForPath(storyItem2.firstFramePath), storyImageFilter, drawableCreateStripedBitmap2, 0L, null, null, 0);
                            } else {
                                this.imageReceiver.setImage(ImageLocation.getForPath(storyItem2.attachPath), storyImageFilter, null, null, drawableCreateStripedBitmap2, 0L, null, null, 0);
                            }
                        }
                        storyItem18 = storyItem2;
                    } else {
                        z5 = z3;
                        StoryViewer storyViewer7 = this.storyViewer;
                        Drawable drawable = ((storyViewer7.storiesList != null || storyViewer7.isSingleStory) && (transitionViewHolder = storyViewer7.transitionViewHolder) != null && (imageReceiver = transitionViewHolder.storyImage) != null && transitionViewHolder.storyId == storyItem18.id) ? imageReceiver.getDrawable() : null;
                        storyItem18.dialogId = this.dialogId;
                        if (zEndsWith) {
                            TLRPC.PhotoSize closestPhotoSizeWithSize = FileLoader.getClosestPhotoSizeWithSize(storyItem18.media.getDocument().thumbs, MediaDataController.MAX_STYLE_RUNS_COUNT);
                            if (drawable == null) {
                                drawable = ImageLoader.createStripedBitmap(storyItem18.media.getDocument().thumbs);
                            }
                            ImageLocation forDocument = ImageLocation.getForDocument(closestPhotoSizeWithSize, storyItem18.media.getDocument());
                            storyItem18 = storyItem18;
                            this.imageReceiver.setImage(null, null, ImageLocation.getForDocument(storyItem18.media.getDocument()), storyImageFilter + "_pframe", forDocument, storyImageFilter, drawable, 0L, null, storyItem18, 0);
                        } else {
                            TLRPC.MessageMedia messageMedia7 = storyItem18.media;
                            TLRPC.Photo photo3 = messageMedia7 != null ? messageMedia7.photo : null;
                            if (photo3 != null) {
                                storyItem18 = storyItem18;
                                ArrayList arrayList = photo3.sizes;
                                if (arrayList != null) {
                                    if (drawable == null) {
                                        drawable = ImageLoader.createStripedBitmap(arrayList);
                                    }
                                    TLRPC.PhotoSize closestPhotoSizeWithSize2 = FileLoader.getClosestPhotoSizeWithSize(photo3.sizes, Integer.MAX_VALUE);
                                    FileLoader.getClosestPhotoSizeWithSize(photo3.sizes, 800);
                                    this.imageReceiver.setImage(null, null, ImageLocation.getForPhoto(closestPhotoSizeWithSize2, photo3), storyImageFilter, null, null, drawable, 0L, null, storyItem18, 0);
                                } else {
                                    storyItem18 = storyItem18;
                                    storyItem18 = storyItem18;
                                    storyItem18 = storyItem18;
                                    this.imageReceiver.clearImage();
                                }
                            } else {
                                storyItem18 = storyItem18;
                                storyItem18 = storyItem18;
                                storyItem18 = storyItem18;
                                this.imageReceiver.clearImage();
                            }
                        }
                    }
                }
                storyItem18.dialogId = this.dialogId;
                this.storyAreasView.set(z ? null : storyItem18, this.emojiAnimationsOverlay);
                this.currentStory.set(storyItem18);
                boolean z23 = (this.unsupported || (storyItem3 = this.currentStory.storyItem) == null || (storyItem3 instanceof TL_stories.TL_storyItemDeleted) || (storyItem3 instanceof TL_stories.TL_storyItemSkipped)) ? false : true;
                this.allowShareLink = z23;
                this.allowShare = z23;
                if (z23) {
                    this.allowShare = this.currentStory.allowScreenshots() && this.currentStory.storyItem.isPublic;
                }
                if (this.allowShare) {
                    TL_stories.StoryItem storyItem19 = this.currentStory.storyItem;
                    this.allowShare = storyItem19.pinned || !StoriesUtilities.isExpired(this.currentAccount, storyItem19);
                }
                boolean z24 = this.allowShare;
                this.allowRepost = z24;
                if (z24 && this.isChannel) {
                    TLRPC.Chat chat3 = MessagesController.getInstance(this.currentAccount).getChat(Long.valueOf(-this.dialogId));
                    this.allowRepost = chat3 != null && ChatObject.isPublic(chat3);
                }
                if (this.allowShareLink) {
                    if (this.isChannel) {
                        TLRPC.Chat chat4 = MessagesController.getInstance(this.currentAccount).getChat(Long.valueOf(-this.dialogId));
                        this.allowShareLink = (chat4 == null || ChatObject.getPublicUsername(chat4) == null) ? false : true;
                    } else {
                        TLRPC.User user = MessagesController.getInstance(this.currentAccount).getUser(Long.valueOf(this.dialogId));
                        this.allowShareLink = (user == null || UserObject.getPublicUsername(user) == null || !this.currentStory.storyItem.isPublic) ? false : true;
                    }
                }
                NotificationsController.getInstance(this.currentAccount).processReadStories(this.dialogId, storyItem18.id);
            }
            storyItem4 = this.currentStory.storyItem;
            if (storyItem4 != null && !z) {
                this.storyViewer.dayStoryId = storyItem4.id;
            }
            this.storyViewer.storiesViewPager.checkAllowScreenshots();
            this.imageChanged = true;
            if (!this.isSelf || this.isChannel) {
                updateUserViews(false);
            }
            storyItemHolder = this.currentStory;
            if (getStoryId(storyItemHolder.storyItem, storyItemHolder.uploadingStory) == getStoryId(storyItem17, uploadingStory7) && (uploadingStory7 == null || (storyItem16 = this.currentStory.storyItem) == null || !TextUtils.equals(uploadingStory7.path, storyItem16.attachPath))) {
                z6 = false;
            } else {
                z6 = true;
            }
            if (z6 || (this.isEditing == z5 && this.isUploading == z2 && this.isFailed == z4)) {
                z7 = false;
            } else {
                z7 = true;
            }
            storyViewer = this.storyViewer;
            if (storyViewer != null || (livePlayer6 = storyViewer.livePlayer) == null || this.watchersCount == livePlayer6.getWatchersCount()) {
                z8 = false;
            } else {
                z8 = true;
            }
            j = 0;
            if ((uploadingStory7 != null || (str2 = uploadingStory7.path) == null || !str2.equals(this.currentStory.getLocalPath())) && (storyItem17 == null || (storyItem5 = this.currentStory.storyItem) == null || storyItem17.id != storyItem5.id)) {
                chatActivityEnterView = this.chatActivityEnterView;
                if (chatActivityEnterView != null) {
                    if (storyItem17 != null && !TextUtils.isEmpty(chatActivityEnterView.getEditField().getText())) {
                        this.storyViewer.saveDraft(storyItem17.dialogId, storyItem17, this.chatActivityEnterView.getEditField().getText());
                    }
                    this.chatActivityEnterView.getEditField().setText(this.storyViewer.getDraft(this.dialogId, this.currentStory.storyItem));
                    if (this.currentStory.isLive) {
                        z9 = true;
                        this.chatActivityEnterView.setAllowStickersAndGifs(true, false, false);
                    } else {
                        z9 = true;
                        this.chatActivityEnterView.setAllowStickersAndGifs(true, true, true);
                    }
                } else {
                    z9 = true;
                }
                if (this.currentStory.isLive) {
                    loadSendAsPeers(z9);
                }
                this.emojiAnimationsOverlay.clear();
                this.currentImageTime = 0L;
                this.switchEventSent = false;
                uploadingStory2 = this.currentStory.uploadingStory;
                if (uploadingStory2 != null) {
                    radialProgress = this.headerView.radialProgress;
                    if (radialProgress != null) {
                        radialProgress.setProgress(uploadingStory2.progress, false);
                    }
                    this.headerView.backupImageView.invalidate();
                } else if (!z7) {
                    this.headerView.progressToUploading = 0.0f;
                }
                Bulletin.hideVisible(this.storyContainer);
                this.storyCaptionView.reset();
                cancelWaiting();
                z8 = true;
            }
            if (!z8 || (uploadingStory7 != null && this.currentStory.uploadingStory == null)) {
                this.headerView.setOnSubtitleClick(null);
                this.watchersCount = 0;
                setTitle(false, this.dialogId, this.currentStory.isLive);
                uploadingStory3 = this.currentStory.uploadingStory;
                if (uploadingStory3 != null) {
                    if (uploadingStory3.failed) {
                        string = LocaleController.getString(R.string.FailedToUploadStory);
                    } else {
                        string = StoriesUtilities.getUploadingStr(this.headerView.subtitleView[0], false, this.isEditing);
                    }
                } else if (isBotsPreview()) {
                    storyItem8 = this.currentStory.storyItem;
                    if (storyItem8 != null && (messageMedia = storyItem8.media) != null) {
                        document = messageMedia.document;
                        if (document != null) {
                            string = LocaleController.formatStoryDate(document.date);
                        } else {
                            photo = messageMedia.photo;
                            if (photo != null) {
                                string = LocaleController.formatStoryDate(photo.date);
                            }
                            i2 = 2;
                            charSequence = charSequence3;
                            if (charSequence != null) {
                                storyViewer3 = this.storyViewer;
                                if (storyViewer3 != null && (storiesList = storyViewer3.storiesList) != null && (storyItem9 = this.currentStory.storyItem) != null && storiesList.isPinned(storyItem9.id)) {
                                    if (!(charSequence instanceof SpannableStringBuilder)) {
                                        charSequence4 = charSequence;
                                        charSequence4 = charSequence;
                                        charSequence4 = charSequence;
                                        charSequence4 = charSequence;
                                        spannableStringBuilder3 = charSequence;
                                        spannableStringBuilder3 = new SpannableStringBuilder(charSequence);
                                    }
                                    charSequence4 = charSequence;
                                    charSequence4 = charSequence;
                                    charSequence4 = charSequence;
                                    charSequence4 = charSequence;
                                    spannableStringBuilder3 = charSequence;
                                    SpannableString spannableString = new SpannableString("p ");
                                    spannableString.setSpan(new ColoredImageSpan(R.drawable.msg_pin_mini), 0, 1, 33);
                                    ((SpannableStringBuilder) spannableStringBuilder3).insert(0, (CharSequence) spannableString);
                                    charSequence4 = spannableStringBuilder3;
                                }
                                charSequence4 = charSequence;
                                charSequence4 = charSequence;
                                charSequence4 = charSequence;
                                charSequence4 = charSequence;
                                charSequence4 = charSequence;
                                charSequence4 = charSequence;
                                charSequence4 = charSequence;
                                charSequence4 = charSequence;
                                charSequence4 = charSequence;
                                charSequence4 = charSequence;
                                this.headerView.setSubtitle(charSequence4, z7);
                            }
                            hintView2 = this.privacyHint;
                            if (hintView2 != null) {
                                hintView2.hide(false);
                            }
                            hintView3 = this.soundTooltip;
                            if (hintView3 != null) {
                                hintView3.hide(false);
                            }
                        }
                    }
                    charSequence3 = _UrlKt.FRAGMENT_ENCODE_SET;
                    i2 = 2;
                    charSequence = charSequence3;
                    if (charSequence != null) {
                        storyViewer3 = this.storyViewer;
                        if (storyViewer3 != null) {
                            if (!(charSequence instanceof SpannableStringBuilder)) {
                                charSequence4 = charSequence;
                                charSequence4 = charSequence;
                                charSequence4 = charSequence;
                                charSequence4 = charSequence;
                                spannableStringBuilder3 = charSequence;
                                spannableStringBuilder3 = new SpannableStringBuilder(charSequence);
                            }
                            charSequence4 = charSequence;
                            charSequence4 = charSequence;
                            charSequence4 = charSequence;
                            charSequence4 = charSequence;
                            spannableStringBuilder3 = charSequence;
                            SpannableString spannableString2 = new SpannableString("p ");
                            spannableString2.setSpan(new ColoredImageSpan(R.drawable.msg_pin_mini), 0, 1, 33);
                            ((SpannableStringBuilder) spannableStringBuilder3).insert(0, (CharSequence) spannableString2);
                            charSequence4 = spannableStringBuilder3;
                        }
                        charSequence4 = charSequence;
                        charSequence4 = charSequence;
                        charSequence4 = charSequence;
                        charSequence4 = charSequence;
                        charSequence4 = charSequence;
                        charSequence4 = charSequence;
                        charSequence4 = charSequence;
                        charSequence4 = charSequence;
                        charSequence4 = charSequence;
                        charSequence4 = charSequence;
                        this.headerView.setSubtitle(charSequence4, z7);
                    }
                    hintView2 = this.privacyHint;
                    if (hintView2 != null) {
                        hintView2.hide(false);
                    }
                    hintView3 = this.soundTooltip;
                    if (hintView3 != null) {
                        hintView3.hide(false);
                    }
                } else {
                    storyItemHolder2 = this.currentStory;
                    storyItem6 = storyItemHolder2.storyItem;
                    if (storyItem6 == null) {
                        j = 0;
                        i2 = 2;
                        charSequence = null;
                    } else if (storyItem6.media instanceof TLRPC.TL_messageMediaVideoStream) {
                        SpannableStringBuilder spannableStringBuilder4 = new SpannableStringBuilder(LocaleController.getString(R.string.LiveStoryBadge));
                        spannableStringBuilder4.setSpan(new ReplacementSpan() { // from class: org.telegram.ui.Stories.PeerStoriesView.32
                            private final RectF rect = new RectF();
                            private final Paint bg = new Paint(1);
                            private final Text text = new Text(LocaleController.getString(R.string.LiveStoryBadge), 9.0f, AndroidUtilities.bold());

                            @Override // android.text.style.ReplacementSpan
                            public int getSize(Paint paint, CharSequence charSequence5, int i22, int i23, Paint.FontMetricsInt fontMetricsInt) {
                                return (int) (this.text.getWidth() + AndroidUtilities.dp(12.0f));
                            }

                            @Override // android.text.style.ReplacementSpan
                            public void draw(Canvas canvas, CharSequence charSequence5, int i22, int i23, float f4, int i24, int i25, int i26, Paint paint) {
                                float fDp = ((i24 + i26) / 2.0f) + AndroidUtilities.dp(1.33f);
                                this.rect.set(f4, fDp - AndroidUtilities.dp(7.0f), this.text.getWidth() + f4 + AndroidUtilities.dp(12.0f), AndroidUtilities.dp(7.0f) + fDp);
                                this.bg.setColor(-572850);
                                RectF rectF = this.rect;
                                canvas.drawRoundRect(rectF, rectF.height() / 2.0f, this.rect.height() / 2.0f, this.bg);
                                this.text.draw(canvas, AndroidUtilities.dp(6.0f) + f4, fDp, -1, 1.0f);
                            }
                        }, 0, spannableStringBuilder4.length(), 33);
                        spannableStringBuilder4.append((CharSequence) "  ");
                        storyViewer2 = this.storyViewer;
                        if (storyViewer2 != null && (livePlayer = storyViewer2.livePlayer) != null) {
                            this.watchersCount = livePlayer.getWatchersCount();
                        }
                        spannableStringBuilder4.append((CharSequence) LocaleController.formatPluralStringComma("LiveStoryWatching", Math.max(1, this.watchersCount)));
                        string = spannableStringBuilder4;
                    } else if (storyItem6.date == -1) {
                        string = LocaleController.getString(R.string.CachedStory);
                    } else {
                        i2 = 2;
                        if (storyItemHolder2.getReply() != null) {
                            reply = this.currentStory.getReply();
                            spannableStringBuilder2 = new SpannableStringBuilder();
                            j = 0;
                            SpannableString spannableString3 = new SpannableString("r");
                            spannableString3.setSpan(new ColoredImageSpan(R.drawable.mini_repost_story), 0, spannableString3.length(), 33);
                            spannableStringBuilder2.append((CharSequence) spannableString3).append((CharSequence) " ");
                            if (reply.peerId != null) {
                                avatarSpan = new AvatarSpan(this.headerView.subtitleView[0], this.currentAccount, 15.0f);
                                SpannableString spannableString4 = new SpannableString("a");
                                spannableString4.setSpan(avatarSpan, 0, 1, 33);
                                spannableStringBuilder2.append((CharSequence) spannableString4).append((CharSequence) " ");
                                if (reply.peerId.longValue() > 0) {
                                    TLRPC.User user2 = MessagesController.getInstance(this.currentAccount).getUser(reply.peerId);
                                    avatarSpan.setUser(user2);
                                    spannableStringBuilder2.append((CharSequence) UserObject.getUserName(user2));
                                } else {
                                    chat = MessagesController.getInstance(this.currentAccount).getChat(Long.valueOf(-reply.peerId.longValue()));
                                    avatarSpan.setChat(chat);
                                    if (chat != null) {
                                        spannableStringBuilder2.append((CharSequence) chat.title);
                                    }
                                }
                            } else {
                                str = this.currentStory.storyItem.fwd_from.from_name;
                                if (str != null) {
                                    spannableStringBuilder2.append((CharSequence) str);
                                }
                            }
                            this.headerView.setOnSubtitleClick(new View.OnClickListener() { // from class: org.telegram.ui.Stories.PeerStoriesView$$ExternalSyntheticLambda19
                                @Override // android.view.View.OnClickListener
                                public final void onClick(View view) {
                                    this.f$0.lambda$updatePosition$44(reply, view);
                                }
                            });
                            SpannableString spannableString5 = new SpannableString(".");
                            DotDividerSpan dotDividerSpan = new DotDividerSpan();
                            dotDividerSpan.setTopPadding(AndroidUtilities.dp(1.5f));
                            dotDividerSpan.setSize(5.0f);
                            spannableString5.setSpan(dotDividerSpan, 0, spannableString5.length(), 33);
                            spannableStringBuilder2.append((CharSequence) " ").append((CharSequence) spannableString5).append((CharSequence) " ").append((CharSequence) LocaleController.formatShortDate(this.currentStory.storyItem.date));
                            spannableStringBuilder = spannableStringBuilder2;
                        } else {
                            j = 0;
                            if (!this.isGroup && (storyItem7 = this.currentStory.storyItem) != null && storyItem7.from_id != null) {
                                SpannableStringBuilder spannableStringBuilder5 = new SpannableStringBuilder();
                                AvatarSpan avatarSpan2 = new AvatarSpan(this.headerView.subtitleView[0], this.currentAccount, 15.0f);
                                SpannableString spannableString6 = new SpannableString("a");
                                spannableString6.setSpan(avatarSpan2, 0, 1, 33);
                                spannableStringBuilder5.append((CharSequence) spannableString6).append((CharSequence) " ");
                                final long peerDialogId = DialogObject.getPeerDialogId(this.currentStory.storyItem.from_id);
                                if (peerDialogId > 0) {
                                    TLRPC.User user3 = MessagesController.getInstance(this.currentAccount).getUser(Long.valueOf(peerDialogId));
                                    avatarSpan2.setUser(user3);
                                    spannableStringBuilder5.append((CharSequence) UserObject.getUserName(user3));
                                } else {
                                    TLRPC.Chat chat5 = MessagesController.getInstance(this.currentAccount).getChat(Long.valueOf(-peerDialogId));
                                    avatarSpan2.setChat(chat5);
                                    if (chat5 != null) {
                                        spannableStringBuilder5.append((CharSequence) chat5.title);
                                    }
                                }
                                this.headerView.setOnSubtitleClick(new View.OnClickListener() { // from class: org.telegram.ui.Stories.PeerStoriesView$$ExternalSyntheticLambda20
                                    @Override // android.view.View.OnClickListener
                                    public final void onClick(View view) {
                                        this.f$0.lambda$updatePosition$45(peerDialogId, view);
                                    }
                                });
                                SpannableString spannableString7 = new SpannableString(".");
                                DotDividerSpan dotDividerSpan2 = new DotDividerSpan();
                                dotDividerSpan2.setTopPadding(AndroidUtilities.dp(1.5f));
                                dotDividerSpan2.setSize(5.0f);
                                spannableString7.setSpan(dotDividerSpan2, 0, spannableString7.length(), 33);
                                spannableStringBuilder5.append((CharSequence) " ").append((CharSequence) spannableString7).append((CharSequence) " ").append((CharSequence) LocaleController.formatShortDate(this.currentStory.storyItem.date));
                                spannableStringBuilder = spannableStringBuilder5;
                            } else {
                                storyDate = LocaleController.formatStoryDate(this.currentStory.storyItem.date);
                                charSequence2 = storyDate;
                                if (this.currentStory.storyItem.edited) {
                                    SpannableStringBuilder spannableStringBuilderValueOf = SpannableStringBuilder.valueOf(storyDate);
                                    DotDividerSpan dotDividerSpan3 = new DotDividerSpan();
                                    dotDividerSpan3.setTopPadding(AndroidUtilities.dp(1.5f));
                                    dotDividerSpan3.setSize(5.0f);
                                    spannableStringBuilderValueOf.append((CharSequence) " . ").setSpan(dotDividerSpan3, spannableStringBuilderValueOf.length() - 2, spannableStringBuilderValueOf.length() - 1, 0);
                                    spannableStringBuilderValueOf.append((CharSequence) LocaleController.getString(R.string.EditedMessage));
                                    charSequence2 = spannableStringBuilderValueOf;
                                }
                                charSequence = charSequence2;
                            }
                        }
                        z7 = false;
                        charSequence = spannableStringBuilder;
                    }
                    if (charSequence != null) {
                        storyViewer3 = this.storyViewer;
                        if (storyViewer3 != null) {
                            if (!(charSequence instanceof SpannableStringBuilder)) {
                                charSequence4 = charSequence;
                                charSequence4 = charSequence;
                                charSequence4 = charSequence;
                                charSequence4 = charSequence;
                                spannableStringBuilder3 = charSequence;
                                spannableStringBuilder3 = new SpannableStringBuilder(charSequence);
                            }
                            charSequence4 = charSequence;
                            charSequence4 = charSequence;
                            charSequence4 = charSequence;
                            charSequence4 = charSequence;
                            spannableStringBuilder3 = charSequence;
                            SpannableString spannableString8 = new SpannableString("p ");
                            spannableString8.setSpan(new ColoredImageSpan(R.drawable.msg_pin_mini), 0, 1, 33);
                            ((SpannableStringBuilder) spannableStringBuilder3).insert(0, (CharSequence) spannableString8);
                            charSequence4 = spannableStringBuilder3;
                        }
                        charSequence4 = charSequence;
                        charSequence4 = charSequence;
                        charSequence4 = charSequence;
                        charSequence4 = charSequence;
                        charSequence4 = charSequence;
                        charSequence4 = charSequence;
                        charSequence4 = charSequence;
                        charSequence4 = charSequence;
                        charSequence4 = charSequence;
                        charSequence4 = charSequence;
                        this.headerView.setSubtitle(charSequence4, z7);
                    }
                    hintView2 = this.privacyHint;
                    if (hintView2 != null) {
                        hintView2.hide(false);
                    }
                    hintView3 = this.soundTooltip;
                    if (hintView3 != null) {
                        hintView3.hide(false);
                    }
                }
                charSequence3 = string;
                i2 = 2;
                charSequence = charSequence3;
                if (charSequence != null) {
                    storyViewer3 = this.storyViewer;
                    if (storyViewer3 != null) {
                        if (!(charSequence instanceof SpannableStringBuilder)) {
                            charSequence4 = charSequence;
                            charSequence4 = charSequence;
                            charSequence4 = charSequence;
                            charSequence4 = charSequence;
                            spannableStringBuilder3 = charSequence;
                            spannableStringBuilder3 = new SpannableStringBuilder(charSequence);
                        }
                        charSequence4 = charSequence;
                        charSequence4 = charSequence;
                        charSequence4 = charSequence;
                        charSequence4 = charSequence;
                        spannableStringBuilder3 = charSequence;
                        SpannableString spannableString9 = new SpannableString("p ");
                        spannableString9.setSpan(new ColoredImageSpan(R.drawable.msg_pin_mini), 0, 1, 33);
                        ((SpannableStringBuilder) spannableStringBuilder3).insert(0, (CharSequence) spannableString9);
                        charSequence4 = spannableStringBuilder3;
                    }
                    charSequence4 = charSequence;
                    charSequence4 = charSequence;
                    charSequence4 = charSequence;
                    charSequence4 = charSequence;
                    charSequence4 = charSequence;
                    charSequence4 = charSequence;
                    charSequence4 = charSequence;
                    charSequence4 = charSequence;
                    charSequence4 = charSequence;
                    charSequence4 = charSequence;
                    this.headerView.setSubtitle(charSequence4, z7);
                }
                hintView2 = this.privacyHint;
                if (hintView2 != null) {
                    hintView2.hide(false);
                }
                hintView3 = this.soundTooltip;
                if (hintView3 != null) {
                    hintView3.hide(false);
                }
            } else {
                j = 0;
                i2 = 2;
            }
            StoryItemHolder storyItemHolder9 = this.currentStory;
            storyItem10 = storyItemHolder9.storyItem;
            if (storyItem17 != storyItem10 && uploadingStory7 == storyItemHolder9.uploadingStory) {
                if (storyItemHolder9.captionTranslated != (storyItem10 != null && storyItem10.translated && storyItem10.translatedText != null && TextUtils.equals(storyItem10.translatedLng, TranslateAlert2.getToLanguage()))) {
                    this.currentStory.updateCaption();
                }
            } else {
                this.currentStory.updateCaption();
            }
            storyItemHolder3 = this.currentStory;
            if ((!storyItemHolder3.captionTranslated || storyItem17 != storyItemHolder3.storyItem) && (delegate = this.delegate) != null) {
                delegate.setTranslating(false);
            }
            if (this.currentStory.isLive || (storyViewer4 = this.storyViewer) == null || (livePlayer5 = storyViewer4.livePlayer) == null || !this.currentStory.isThisCall(livePlayer5.getCallId()) || !this.storyViewer.livePlayer.commentsDisabled()) {
                z10 = false;
            } else {
                z10 = true;
            }
            if (z10 != this.areLiveCommentsDisabled) {
                this.areLiveCommentsDisabled = z10;
                if (z10) {
                    createPremiumBlockedText();
                }
                if (this.premiumBlockedText != null && (this.isPremiumBlocked || this.areLiveCommentsDisabled)) {
                    updatePremiumBlockedText();
                }
                chatActivityEnterView8 = this.chatActivityEnterView;
                if (chatActivityEnterView8 != null) {
                    chatActivityEnterView8.setEnabled((this.isPremiumBlocked || this.currentStory.isLive) && !this.areLiveCommentsDisabled);
                    this.chatActivityEnterView.updateSendAsButton();
                }
                checkStealthMode(true);
            }
            linearLayout = this.premiumBlockedText;
            if (linearLayout != null) {
                linearLayout.setVisibility(((this.isPremiumBlocked || this.currentStory.isLive) && !this.areLiveCommentsDisabled) ? 8 : 0);
            }
            if (this.unsupported) {
                createUnsupportedContainer();
                createReplyDisabledView();
                this.unsupportedContainer.setVisibility(0);
                this.replyDisabledTextView.setVisibility(0);
                this.allowShareLink = false;
                this.allowRepost = false;
                this.allowShare = false;
                chatActivityEnterView7 = this.chatActivityEnterView;
                if (chatActivityEnterView7 != null) {
                    chatActivityEnterView7.setVisibility(8);
                }
                frameLayout5 = this.selfView;
                if (frameLayout5 != null) {
                    frameLayout5.setVisibility(8);
                }
                linearLayout3 = this.bottomActionsLinearLayout;
                if (linearLayout3 != null) {
                    linearLayout3.setVisibility(0);
                }
            } else {
                if (this.dialogId < j) {
                    chat2 = MessagesController.getInstance(this.currentAccount).getChat(Long.valueOf(-this.dialogId));
                } else {
                    chat2 = null;
                }
                if (this.currentStory.isLive) {
                    if (this.chatActivityEnterView == null) {
                        createEnterView();
                    }
                    createCommentButton();
                    createPaidReactionsButton();
                    createMuteButton();
                    this.chatActivityEnterView.setVisibility(0);
                } else if ((!UserObject.isService(this.dialogId) || isBotsPreview()) && (chatActivityEnterView2 = this.chatActivityEnterView) != null) {
                    chatActivityEnterView2.setVisibility(8);
                } else if (!this.isSelf && ((!this.isChannel || (this.isGroup && (ChatObject.canSendPlain(chat2) || ChatObject.isPossibleRemoveChatRestrictionsByBoosts(chat2)))) && (chatActivityEnterView3 = this.chatActivityEnterView) != null)) {
                    if (chatActivityEnterView3 == null) {
                        createEnterView();
                    }
                    this.chatActivityEnterView.setVisibility(0);
                }
                chatActivityEnterView4 = this.chatActivityEnterView;
                if (chatActivityEnterView4 != null) {
                    if (this.currentStory.isLive) {
                        onLongClickListener = new View.OnLongClickListener() { // from class: org.telegram.ui.Stories.PeerStoriesView$$ExternalSyntheticLambda21
                            @Override // android.view.View.OnLongClickListener
                            public final boolean onLongClick(View view) {
                                return this.f$0.lambda$updatePosition$48(view);
                            }
                        };
                    } else {
                        onLongClickListener = null;
                    }
                    chatActivityEnterView4.setOnSendButtonLongClick(onLongClickListener);
                    this.chatActivityEnterView.setLiveComment(this.currentStory.isLive, disabledPaidFeatures(true));
                    ChatActivityEnterView chatActivityEnterView9 = this.chatActivityEnterView;
                    if (this.currentStory.isLive || disabledPaidFeatures(true) || !(this.keyboardVisible || this.chatActivityEnterView.emojiViewVisible)) {
                        z11 = false;
                    } else {
                        z11 = true;
                    }
                    chatActivityEnterView9.setSuggestionButtonVisible(z11, true);
                }
                if (this.isPremiumBlocked && this.premiumBlockedText == null) {
                    createPremiumBlockedText();
                }
                if (this.premiumBlockedText != null) {
                    if (!this.isPremiumBlocked || this.areLiveCommentsDisabled) {
                        updatePremiumBlockedText();
                    }
                    this.premiumBlockedText.setVisibility(((this.isPremiumBlocked || this.currentStory.isLive) && !this.areLiveCommentsDisabled) ? 8 : 0);
                }
                chatActivityEnterView5 = this.chatActivityEnterView;
                if (chatActivityEnterView5 != null) {
                    chatActivityEnterView5.setEnabled((this.isPremiumBlocked || this.currentStory.isLive) && !this.areLiveCommentsDisabled);
                }
                frameLayout = this.selfView;
                if (frameLayout != null) {
                    if (this.isSelf || this.currentStory.isLive) {
                        i4 = 8;
                    } else {
                        i4 = 0;
                    }
                    frameLayout.setVisibility(i4);
                }
                frameLayout2 = this.unsupportedContainer;
                if (frameLayout2 != null) {
                    frameLayout2.setVisibility(8);
                }
                if (UserObject.isService(this.dialogId)) {
                    createReplyDisabledView();
                    this.replyDisabledTextView.setVisibility(0);
                } else {
                    textView = this.replyDisabledTextView;
                    if (textView != null) {
                        textView.setVisibility(8);
                    }
                }
                linearLayout2 = this.bottomActionsLinearLayout;
                if (linearLayout2 != null) {
                    if (isBotsPreview()) {
                        i3 = 8;
                    } else {
                        i3 = 0;
                    }
                    linearLayout2.setVisibility(i3);
                }
            }
            commentButton = this.commentButton;
            if (commentButton != null) {
                if (this.unsupported && this.currentStory.isLive) {
                    i19 = 0;
                } else {
                    i19 = 8;
                }
                commentButton.setVisibility(i19);
                this.commentButton.setCollapsed(this.liveCommentsView.isCollapsed(), false);
                this.commentButton.setCount(this.liveCommentsView.getUnreadMessagesCount());
            }
            muteButton = this.muteButton;
            if (muteButton != null) {
                if (this.unsupported && this.currentStory.isLive && (livePlayer4 = LivePlayer.recording) != null && this.currentStory.isThisCall(livePlayer4.getCallId())) {
                    i18 = 0;
                } else {
                    i18 = 8;
                }
                muteButton.setVisibility(i18);
                MuteButton muteButton3 = this.muteButton;
                livePlayer2 = LivePlayer.recording;
                if (livePlayer2 == null && livePlayer2.isMuted()) {
                    z17 = true;
                } else {
                    z17 = false;
                }
                muteButton3.setMuted(z17, true);
                MuteButton muteButton4 = this.muteButton;
                livePlayer3 = LivePlayer.recording;
                if (livePlayer3 != null || livePlayer3.isConnected()) {
                    z18 = true;
                } else {
                    z18 = false;
                }
                muteButton4.setConnected(z18, true);
            }
            if (this.starsButton != null) {
                PaidReactionButton.PaidReactionButtonEffectsView paidReactionButtonEffectsView = this.starsButtonEffectsView;
                if (this.unsupported && this.currentStory.isLive) {
                    i16 = 0;
                } else {
                    i16 = 8;
                }
                paidReactionButtonEffectsView.setVisibility(i16);
                PaidReactionButton paidReactionButton = this.starsButton;
                if (this.unsupported && this.currentStory.isLive) {
                    i17 = 0;
                } else {
                    i17 = 8;
                }
                paidReactionButton.setVisibility(i17);
                layoutParams = (FrameLayout.LayoutParams) this.starsButton.getLayoutParams();
                muteButton2 = this.muteButton;
                if (muteButton2 == null && muteButton2.getVisibility() == 0) {
                    f3 = 54.0f;
                } else {
                    f3 = 7.0f;
                }
                iDp2 = AndroidUtilities.dp(f3);
                if (layoutParams.rightMargin != iDp2) {
                    layoutParams.rightMargin = iDp2;
                    this.starsButton.setLayoutParams(layoutParams);
                }
            }
            if (this.currentStory.isLive) {
                if (this.isActive) {
                    this.delegate.setIsCaption(false);
                    Delegate delegate2 = this.delegate;
                    this.isCaptionPartVisible = false;
                    delegate2.setIsCaptionPartVisible(false);
                }
                this.storyCaptionView.setVisibility(8);
            } else {
                storyItemHolder6 = this.currentStory;
                if ((storyItemHolder6.caption == null || storyItemHolder6.getReply() != null) && !this.unsupported) {
                    StoryCaptionView.StoryCaptionTextView storyCaptionTextView = this.storyCaptionView.captionTextview;
                    StoryItemHolder storyItemHolder10 = this.currentStory;
                    CharSequence charSequence5 = storyItemHolder10.caption;
                    StoryCaptionView.Reply reply2 = storyItemHolder10.getReply();
                    if (this.storyViewer.isTranslating) {
                        storyItemHolder7 = this.currentStory;
                        if (storyItemHolder7.captionTranslated && (storyItem15 = storyItemHolder7.storyItem) != null && storyItem15.translated) {
                            z15 = true;
                        } else {
                            z15 = false;
                        }
                    } else {
                        z15 = false;
                    }
                    if (storyItem17 == this.currentStory.storyItem) {
                        z16 = true;
                    } else {
                        z16 = false;
                    }
                    storyCaptionTextView.setText(charSequence5, reply2, z15, z16);
                    this.storyCaptionView.setVisibility(0);
                } else {
                    if (this.isActive) {
                        this.delegate.setIsCaption(false);
                        Delegate delegate3 = this.delegate;
                        this.isCaptionPartVisible = false;
                        delegate3.setIsCaptionPartVisible(false);
                    }
                    this.storyCaptionView.setVisibility(8);
                }
            }
            storyItem11 = this.currentStory.storyItem;
            if (storyItem11 != null) {
                messageMedia2 = storyItem11.media;
                if (messageMedia2 instanceof TLRPC.TL_messageMediaVideoStream) {
                    if (this.liveCommentsView.setup(this.dialogId, ((TLRPC.TL_messageMediaVideoStream) messageMedia2).call)) {
                        this.liveCommentsView.setCollapsed(false, false);
                        this.messageStars = j;
                        chatActivityEnterView6 = this.chatActivityEnterView;
                        if (chatActivityEnterView6 != null) {
                            chatActivityEnterView6.checkSendButton(true);
                            this.chatActivityEnterView.updateSendButtonPaid();
                            checkStealthMode(true);
                        }
                    }
                    this.liveCommentsView.setVisibility(0);
                } else {
                    this.liveCommentsView.setup(this.dialogId, null);
                    this.liveCommentsView.setVisibility(8);
                }
            } else {
                this.liveCommentsView.setup(this.dialogId, null);
                this.liveCommentsView.setVisibility(8);
            }
            this.storyContainer.invalidate();
            if (this.delegate != null && isSelectedPeer()) {
                this.delegate.onPeerSelected(this.dialogId, this.selectedPosition);
            }
            if (this.isChannel) {
                ImageView imageView3 = this.shareButton;
                if (this.allowShare || this.currentStory.isLive) {
                    i13 = i21;
                } else {
                    i13 = 0;
                }
                imageView3.setVisibility(i13);
                frameLayout4 = this.repostButtonContainer;
                if (frameLayout4 != null) {
                    if (this.allowRepost || this.currentStory.isLive) {
                        i15 = 8;
                    } else {
                        i15 = 0;
                    }
                    frameLayout4.setVisibility(i15);
                }
                FrameLayout frameLayout6 = this.likeButtonContainer;
                if (!this.isFailed || this.currentStory.isLive) {
                    i14 = 8;
                } else {
                    i14 = 0;
                }
                frameLayout6.setVisibility(i14);
            } else {
                ImageView imageView4 = this.shareButton;
                if (this.allowShare || this.currentStory.isLive) {
                    i5 = i21;
                } else {
                    i5 = 0;
                }
                imageView4.setVisibility(i5);
                frameLayout3 = this.repostButtonContainer;
                if (frameLayout3 != null) {
                    frameLayout3.setVisibility(8);
                }
                FrameLayout frameLayout7 = this.likeButtonContainer;
                if (!this.isSelf || this.currentStory.isLive) {
                    i6 = 8;
                } else {
                    i6 = 0;
                }
                frameLayout7.setVisibility(i6);
                this.likeButtonContainer.getLayoutParams().width = AndroidUtilities.dp(40.0f);
            }
            this.likeButtonContainer.requestLayout();
            this.storyViewer.savedPositions.append(this.dialogId, i);
            if (this.isActive) {
                requestVideoPlayer(0L);
                updatePreloadImages();
                this.imageReceiver.bumpPriority();
            }
            this.liveCommentsView.setLivePlayer(this.storyViewer.livePlayer);
            this.listPosition = 0;
            if (this.storyViewer.storiesList != null && (storyItem14 = this.currentStory.storyItem) != null) {
                int i22 = storyItem14.id;
                for (i12 = 0; i12 < this.storyViewer.storiesList.messageObjects.size(); i12++) {
                    messageObject = (MessageObject) this.storyViewer.storiesList.messageObjects.get(i12);
                    if (messageObject == null && messageObject.getId() == i22) {
                        this.listPosition = i12;
                        break;
                    }
                }
            }
            i7 = this.selectedPosition;
            this.linesPosition = i7;
            i8 = this.count;
            this.linesCount = i8;
            if (this.storyViewer.reversed) {
                this.linesPosition = (i8 - 1) - i7;
            }
            if (this.currentStory.isVideo()) {
                this.muteIconContainer.setVisibility(0);
                if (this.currentStory.hasSound()) {
                    f2 = 1.0f;
                } else {
                    f2 = 0.5f;
                }
                this.muteIconViewAlpha = f2;
                if (this.currentStory.hasSound()) {
                    this.muteIconView.setVisibility(0);
                    this.noSoundIconView.setVisibility(8);
                } else {
                    this.muteIconView.setVisibility(8);
                    this.noSoundIconView.setVisibility(0);
                }
                this.muteIconContainer.setAlpha(this.muteIconViewAlpha * (1.0f - this.outT));
            } else {
                this.muteIconContainer.setVisibility(8);
            }
            storyItemHolder4 = this.currentStory;
            uploadingStory4 = storyItemHolder4.uploadingStory;
            if (uploadingStory4 != null) {
                StoryPrivacyButton storyPrivacyButton = this.privacyButton;
                boolean z25 = this.isSelf;
                if (z6 || !this.editedPrivacy) {
                    z14 = false;
                } else {
                    z14 = true;
                }
                storyPrivacyButton.set(z25, uploadingStory4, z14);
            } else {
                storyItem12 = storyItemHolder4.storyItem;
                if (storyItem12 != null) {
                    StoryPrivacyButton storyPrivacyButton2 = this.privacyButton;
                    boolean z26 = this.isSelf;
                    if (z6 || !this.editedPrivacy) {
                        z13 = false;
                    } else {
                        z13 = true;
                    }
                    storyPrivacyButton2.set(z26, storyItem12, z13);
                } else {
                    StoryPrivacyButton storyPrivacyButton3 = this.privacyButton;
                    boolean z27 = this.isSelf;
                    if (z6 || !this.editedPrivacy) {
                        z12 = false;
                    } else {
                        z12 = true;
                    }
                    storyPrivacyButton3.set(z27, (TL_stories.StoryItem) null, z12);
                }
            }
            this.editedPrivacy = false;
            StoryPrivacyButton storyPrivacyButton4 = this.privacyButton;
            if (this.muteIconContainer.getVisibility() == 0) {
                f = -AndroidUtilities.dp(44.0f);
            } else {
                f = 0.0f;
            }
            storyPrivacyButton4.setTranslationX(f);
            if (z8) {
                this.drawReactionEffect = false;
                storyItem13 = this.currentStory.storyItem;
                if (storyItem13 != null || (reaction = storyItem13.sent_reaction) == null) {
                    this.storiesLikeButton.setReaction(null);
                } else {
                    this.storiesLikeButton.setReaction(ReactionsLayoutInBubble.VisibleReaction.fromTL(reaction));
                }
            }
            uploadingStory5 = this.currentStory.uploadingStory;
            if (uploadingStory5 == null && uploadingStory5.failed) {
                createFailView();
                this.failView.set(this.currentStory.uploadingStory.entry.error);
                this.failView.setVisibility(0);
                ViewPropertyAnimator viewPropertyAnimator2 = this.failViewAnimator;
                if (viewPropertyAnimator2 != null) {
                    viewPropertyAnimator2.cancel();
                    this.failViewAnimator = null;
                }
                if (z6) {
                    ViewPropertyAnimator interpolator = this.failView.animate().alpha(1.0f).setDuration(180L).setInterpolator(CubicBezierInterpolator.EASE_OUT_QUINT);
                    this.failViewAnimator = interpolator;
                    interpolator.start();
                } else {
                    this.failView.setAlpha(1.0f);
                }
            } else if (this.failView != null) {
                viewPropertyAnimator = this.failViewAnimator;
                if (viewPropertyAnimator != null) {
                    viewPropertyAnimator.cancel();
                    this.failViewAnimator = null;
                }
                if (!z6 && this.failView.getVisibility() == 0) {
                    ViewPropertyAnimator viewPropertyAnimatorWithEndAction = this.failView.animate().alpha(0.0f).setDuration(180L).setInterpolator(CubicBezierInterpolator.EASE_OUT_QUINT).withEndAction(new Runnable() { // from class: org.telegram.ui.Stories.PeerStoriesView$$ExternalSyntheticLambda22
                        @Override // java.lang.Runnable
                        public final void run() {
                            this.f$0.lambda$updatePosition$49();
                        }
                    });
                    this.failViewAnimator = viewPropertyAnimatorWithEndAction;
                    viewPropertyAnimatorWithEndAction.start();
                } else {
                    this.failView.setAlpha(0.0f);
                    this.failView.setVisibility(8);
                }
            }
            this.sharedResources.setIconMuted(!this.storyViewer.soundEnabled(), false);
            if (this.isActive && this.currentStory.storyItem != null) {
                FileLog.d("StoryViewer displayed story dialogId=" + this.dialogId + " storyId=" + this.currentStory.storyItem.id + " " + this.currentStory.getMediaDebugString());
            }
            if (this.isSelf) {
                SelfStoryViewsPage.preload(this.currentAccount, this.dialogId, this.currentStory.storyItem);
            }
            SimpleTextView simpleTextView = this.headerView.titleView;
            storiesList2 = this.storyViewer.storiesList;
            if (storiesList2 != null || storiesList2.getCount() == this.linesCount) {
                iDp = 0;
            } else {
                iDp = AndroidUtilities.dp(56.0f);
            }
            simpleTextView.setPadding(0, 0, iDp, 0);
            MessagesController.getInstance(this.currentAccount).getTranslateController().detectStoryLanguage(this.currentStory.storyItem);
            if (!z && !this.isSelf && this.reactionsTooltipRunnable == null && !SharedConfig.storyReactionsLongPressHint && SharedConfig.storiesIntroShown) {
                Runnable runnable = new Runnable() { // from class: org.telegram.ui.Stories.PeerStoriesView$$ExternalSyntheticLambda23
                    @Override // java.lang.Runnable
                    public final void run() {
                        this.f$0.lambda$updatePosition$50();
                    }
                };
                this.reactionsTooltipRunnable = runnable;
                AndroidUtilities.runOnUIThread(runnable, 500L);
            }
            hintView4 = this.soundTooltip;
            if ((hintView4 == null && hintView4.shown()) || !this.currentStory.hasSound() || this.storyViewer.soundEnabled()) {
                i9 = 0;
            } else {
                i9 = 0;
                if (MessagesController.getGlobalMainSettings().getInt("taptostorysoundhint", 0) < i2) {
                    AndroidUtilities.cancelRunOnUIThread(this.showTapToSoundHint);
                    AndroidUtilities.runOnUIThread(this.showTapToSoundHint, 250L);
                }
            }
            imageView = this.optionsIconView;
            if (imageView != null) {
                if (isBotsPreview() || isEditBotsPreview() || ((storyItemHolder5 = this.currentStory) != null && storyItemHolder5.isVideo)) {
                    i11 = i9;
                } else {
                    i11 = 8;
                }
                imageView.setVisibility(i11);
            }
            imageView2 = this.pipIconView;
            if (imageView2 != null) {
                if (this.currentStory.isLive || this.privacyButton.draw) {
                    i10 = 8;
                } else {
                    i10 = i9;
                }
                imageView2.setVisibility(i10);
            }
        }
        z5 = z3;
        storyItem4 = this.currentStory.storyItem;
        if (storyItem4 != null) {
            this.storyViewer.dayStoryId = storyItem4.id;
        }
        this.storyViewer.storiesViewPager.checkAllowScreenshots();
        this.imageChanged = true;
        if (!this.isSelf) {
            updateUserViews(false);
        } else {
            updateUserViews(false);
        }
        storyItemHolder = this.currentStory;
        if (getStoryId(storyItemHolder.storyItem, storyItemHolder.uploadingStory) == getStoryId(storyItem17, uploadingStory7)) {
            z6 = true;
        } else {
            z6 = true;
        }
        if (z6) {
            z7 = false;
        } else {
            z7 = false;
        }
        storyViewer = this.storyViewer;
        if (storyViewer != null) {
            z8 = false;
        } else {
            z8 = false;
        }
        j = 0;
        if (uploadingStory7 != null) {
            chatActivityEnterView = this.chatActivityEnterView;
            if (chatActivityEnterView != null) {
                if (storyItem17 != null) {
                    this.storyViewer.saveDraft(storyItem17.dialogId, storyItem17, this.chatActivityEnterView.getEditField().getText());
                }
                this.chatActivityEnterView.getEditField().setText(this.storyViewer.getDraft(this.dialogId, this.currentStory.storyItem));
                if (this.currentStory.isLive) {
                    z9 = true;
                    this.chatActivityEnterView.setAllowStickersAndGifs(true, false, false);
                } else {
                    z9 = true;
                    this.chatActivityEnterView.setAllowStickersAndGifs(true, true, true);
                }
            } else {
                z9 = true;
            }
            if (this.currentStory.isLive) {
                loadSendAsPeers(z9);
            }
            this.emojiAnimationsOverlay.clear();
            this.currentImageTime = 0L;
            this.switchEventSent = false;
            uploadingStory2 = this.currentStory.uploadingStory;
            if (uploadingStory2 != null) {
                radialProgress = this.headerView.radialProgress;
                if (radialProgress != null) {
                    radialProgress.setProgress(uploadingStory2.progress, false);
                }
                this.headerView.backupImageView.invalidate();
            } else if (!z7) {
                this.headerView.progressToUploading = 0.0f;
            }
            Bulletin.hideVisible(this.storyContainer);
            this.storyCaptionView.reset();
            cancelWaiting();
            z8 = true;
        } else {
            chatActivityEnterView = this.chatActivityEnterView;
            if (chatActivityEnterView != null) {
                if (storyItem17 != null) {
                    this.storyViewer.saveDraft(storyItem17.dialogId, storyItem17, this.chatActivityEnterView.getEditField().getText());
                }
                this.chatActivityEnterView.getEditField().setText(this.storyViewer.getDraft(this.dialogId, this.currentStory.storyItem));
                if (this.currentStory.isLive) {
                    z9 = true;
                    this.chatActivityEnterView.setAllowStickersAndGifs(true, false, false);
                } else {
                    z9 = true;
                    this.chatActivityEnterView.setAllowStickersAndGifs(true, true, true);
                }
            } else {
                z9 = true;
            }
            if (this.currentStory.isLive) {
                loadSendAsPeers(z9);
            }
            this.emojiAnimationsOverlay.clear();
            this.currentImageTime = 0L;
            this.switchEventSent = false;
            uploadingStory2 = this.currentStory.uploadingStory;
            if (uploadingStory2 != null) {
                radialProgress = this.headerView.radialProgress;
                if (radialProgress != null) {
                    radialProgress.setProgress(uploadingStory2.progress, false);
                }
                this.headerView.backupImageView.invalidate();
            } else if (!z7) {
                this.headerView.progressToUploading = 0.0f;
            }
            Bulletin.hideVisible(this.storyContainer);
            this.storyCaptionView.reset();
            cancelWaiting();
            z8 = true;
        }
        if (z8) {
            this.headerView.setOnSubtitleClick(null);
            this.watchersCount = 0;
            setTitle(false, this.dialogId, this.currentStory.isLive);
            uploadingStory3 = this.currentStory.uploadingStory;
            if (uploadingStory3 != null) {
                if (uploadingStory3.failed) {
                    string = LocaleController.getString(R.string.FailedToUploadStory);
                } else {
                    string = StoriesUtilities.getUploadingStr(this.headerView.subtitleView[0], false, this.isEditing);
                }
            } else if (isBotsPreview()) {
                storyItem8 = this.currentStory.storyItem;
                if (storyItem8 != null) {
                    document = messageMedia.document;
                    if (document != null) {
                        string = LocaleController.formatStoryDate(document.date);
                    } else {
                        photo = messageMedia.photo;
                        if (photo != null) {
                            string = LocaleController.formatStoryDate(photo.date);
                        }
                        i2 = 2;
                        charSequence = charSequence3;
                        if (charSequence != null) {
                            storyViewer3 = this.storyViewer;
                            if (storyViewer3 != null) {
                                if (!(charSequence instanceof SpannableStringBuilder)) {
                                    charSequence4 = charSequence;
                                    charSequence4 = charSequence;
                                    charSequence4 = charSequence;
                                    charSequence4 = charSequence;
                                    spannableStringBuilder3 = charSequence;
                                    spannableStringBuilder3 = new SpannableStringBuilder(charSequence);
                                }
                                charSequence4 = charSequence;
                                charSequence4 = charSequence;
                                charSequence4 = charSequence;
                                charSequence4 = charSequence;
                                spannableStringBuilder3 = charSequence;
                                SpannableString spannableString10 = new SpannableString("p ");
                                spannableString10.setSpan(new ColoredImageSpan(R.drawable.msg_pin_mini), 0, 1, 33);
                                ((SpannableStringBuilder) spannableStringBuilder3).insert(0, (CharSequence) spannableString10);
                                charSequence4 = spannableStringBuilder3;
                            }
                            charSequence4 = charSequence;
                            charSequence4 = charSequence;
                            charSequence4 = charSequence;
                            charSequence4 = charSequence;
                            charSequence4 = charSequence;
                            charSequence4 = charSequence;
                            charSequence4 = charSequence;
                            charSequence4 = charSequence;
                            charSequence4 = charSequence;
                            charSequence4 = charSequence;
                            this.headerView.setSubtitle(charSequence4, z7);
                        }
                        hintView2 = this.privacyHint;
                        if (hintView2 != null) {
                            hintView2.hide(false);
                        }
                        hintView3 = this.soundTooltip;
                        if (hintView3 != null) {
                            hintView3.hide(false);
                        }
                    }
                }
                charSequence3 = _UrlKt.FRAGMENT_ENCODE_SET;
                i2 = 2;
                charSequence = charSequence3;
                if (charSequence != null) {
                    storyViewer3 = this.storyViewer;
                    if (storyViewer3 != null) {
                        if (!(charSequence instanceof SpannableStringBuilder)) {
                            charSequence4 = charSequence;
                            charSequence4 = charSequence;
                            charSequence4 = charSequence;
                            charSequence4 = charSequence;
                            spannableStringBuilder3 = charSequence;
                            spannableStringBuilder3 = new SpannableStringBuilder(charSequence);
                        }
                        charSequence4 = charSequence;
                        charSequence4 = charSequence;
                        charSequence4 = charSequence;
                        charSequence4 = charSequence;
                        spannableStringBuilder3 = charSequence;
                        SpannableString spannableString11 = new SpannableString("p ");
                        spannableString11.setSpan(new ColoredImageSpan(R.drawable.msg_pin_mini), 0, 1, 33);
                        ((SpannableStringBuilder) spannableStringBuilder3).insert(0, (CharSequence) spannableString11);
                        charSequence4 = spannableStringBuilder3;
                    }
                    charSequence4 = charSequence;
                    charSequence4 = charSequence;
                    charSequence4 = charSequence;
                    charSequence4 = charSequence;
                    charSequence4 = charSequence;
                    charSequence4 = charSequence;
                    charSequence4 = charSequence;
                    charSequence4 = charSequence;
                    charSequence4 = charSequence;
                    charSequence4 = charSequence;
                    this.headerView.setSubtitle(charSequence4, z7);
                }
                hintView2 = this.privacyHint;
                if (hintView2 != null) {
                    hintView2.hide(false);
                }
                hintView3 = this.soundTooltip;
                if (hintView3 != null) {
                    hintView3.hide(false);
                }
            } else {
                storyItemHolder2 = this.currentStory;
                storyItem6 = storyItemHolder2.storyItem;
                if (storyItem6 == null) {
                    j = 0;
                    i2 = 2;
                    charSequence = null;
                } else if (storyItem6.media instanceof TLRPC.TL_messageMediaVideoStream) {
                    SpannableStringBuilder spannableStringBuilder6 = new SpannableStringBuilder(LocaleController.getString(R.string.LiveStoryBadge));
                    spannableStringBuilder6.setSpan(new ReplacementSpan() { // from class: org.telegram.ui.Stories.PeerStoriesView.32
                        private final RectF rect = new RectF();
                        private final Paint bg = new Paint(1);
                        private final Text text = new Text(LocaleController.getString(R.string.LiveStoryBadge), 9.0f, AndroidUtilities.bold());

                        @Override // android.text.style.ReplacementSpan
                        public int getSize(Paint paint, CharSequence charSequence6, int i23, int i24, Paint.FontMetricsInt fontMetricsInt) {
                            return (int) (this.text.getWidth() + AndroidUtilities.dp(12.0f));
                        }

                        @Override // android.text.style.ReplacementSpan
                        public void draw(Canvas canvas, CharSequence charSequence6, int i23, int i24, float f4, int i25, int i26, int i27, Paint paint) {
                            float fDp = ((i25 + i27) / 2.0f) + AndroidUtilities.dp(1.33f);
                            this.rect.set(f4, fDp - AndroidUtilities.dp(7.0f), this.text.getWidth() + f4 + AndroidUtilities.dp(12.0f), AndroidUtilities.dp(7.0f) + fDp);
                            this.bg.setColor(-572850);
                            RectF rectF = this.rect;
                            canvas.drawRoundRect(rectF, rectF.height() / 2.0f, this.rect.height() / 2.0f, this.bg);
                            this.text.draw(canvas, AndroidUtilities.dp(6.0f) + f4, fDp, -1, 1.0f);
                        }
                    }, 0, spannableStringBuilder6.length(), 33);
                    spannableStringBuilder6.append((CharSequence) "  ");
                    storyViewer2 = this.storyViewer;
                    if (storyViewer2 != null) {
                        this.watchersCount = livePlayer.getWatchersCount();
                    }
                    spannableStringBuilder6.append((CharSequence) LocaleController.formatPluralStringComma("LiveStoryWatching", Math.max(1, this.watchersCount)));
                    string = spannableStringBuilder6;
                } else if (storyItem6.date == -1) {
                    string = LocaleController.getString(R.string.CachedStory);
                } else {
                    i2 = 2;
                    if (storyItemHolder2.getReply() != null) {
                        reply = this.currentStory.getReply();
                        spannableStringBuilder2 = new SpannableStringBuilder();
                        j = 0;
                        SpannableString spannableString12 = new SpannableString("r");
                        spannableString12.setSpan(new ColoredImageSpan(R.drawable.mini_repost_story), 0, spannableString12.length(), 33);
                        spannableStringBuilder2.append((CharSequence) spannableString12).append((CharSequence) " ");
                        if (reply.peerId != null) {
                            avatarSpan = new AvatarSpan(this.headerView.subtitleView[0], this.currentAccount, 15.0f);
                            SpannableString spannableString13 = new SpannableString("a");
                            spannableString13.setSpan(avatarSpan, 0, 1, 33);
                            spannableStringBuilder2.append((CharSequence) spannableString13).append((CharSequence) " ");
                            if (reply.peerId.longValue() > 0) {
                                TLRPC.User user4 = MessagesController.getInstance(this.currentAccount).getUser(reply.peerId);
                                avatarSpan.setUser(user4);
                                spannableStringBuilder2.append((CharSequence) UserObject.getUserName(user4));
                            } else {
                                chat = MessagesController.getInstance(this.currentAccount).getChat(Long.valueOf(-reply.peerId.longValue()));
                                avatarSpan.setChat(chat);
                                if (chat != null) {
                                    spannableStringBuilder2.append((CharSequence) chat.title);
                                }
                            }
                        } else {
                            str = this.currentStory.storyItem.fwd_from.from_name;
                            if (str != null) {
                                spannableStringBuilder2.append((CharSequence) str);
                            }
                        }
                        this.headerView.setOnSubtitleClick(new View.OnClickListener() { // from class: org.telegram.ui.Stories.PeerStoriesView$$ExternalSyntheticLambda19
                            @Override // android.view.View.OnClickListener
                            public final void onClick(View view) {
                                this.f$0.lambda$updatePosition$44(reply, view);
                            }
                        });
                        SpannableString spannableString14 = new SpannableString(".");
                        DotDividerSpan dotDividerSpan4 = new DotDividerSpan();
                        dotDividerSpan4.setTopPadding(AndroidUtilities.dp(1.5f));
                        dotDividerSpan4.setSize(5.0f);
                        spannableString14.setSpan(dotDividerSpan4, 0, spannableString14.length(), 33);
                        spannableStringBuilder2.append((CharSequence) " ").append((CharSequence) spannableString14).append((CharSequence) " ").append((CharSequence) LocaleController.formatShortDate(this.currentStory.storyItem.date));
                        spannableStringBuilder = spannableStringBuilder2;
                    } else {
                        j = 0;
                        if (!this.isGroup) {
                        }
                        storyDate = LocaleController.formatStoryDate(this.currentStory.storyItem.date);
                        charSequence2 = storyDate;
                        if (this.currentStory.storyItem.edited) {
                            SpannableStringBuilder spannableStringBuilderValueOf2 = SpannableStringBuilder.valueOf(storyDate);
                            DotDividerSpan dotDividerSpan5 = new DotDividerSpan();
                            dotDividerSpan5.setTopPadding(AndroidUtilities.dp(1.5f));
                            dotDividerSpan5.setSize(5.0f);
                            spannableStringBuilderValueOf2.append((CharSequence) " . ").setSpan(dotDividerSpan5, spannableStringBuilderValueOf2.length() - 2, spannableStringBuilderValueOf2.length() - 1, 0);
                            spannableStringBuilderValueOf2.append((CharSequence) LocaleController.getString(R.string.EditedMessage));
                            charSequence2 = spannableStringBuilderValueOf2;
                        }
                        charSequence = charSequence2;
                    }
                    z7 = false;
                    charSequence = spannableStringBuilder;
                }
                if (charSequence != null) {
                    storyViewer3 = this.storyViewer;
                    if (storyViewer3 != null) {
                        if (!(charSequence instanceof SpannableStringBuilder)) {
                            charSequence4 = charSequence;
                            charSequence4 = charSequence;
                            charSequence4 = charSequence;
                            charSequence4 = charSequence;
                            spannableStringBuilder3 = charSequence;
                            spannableStringBuilder3 = new SpannableStringBuilder(charSequence);
                        }
                        charSequence4 = charSequence;
                        charSequence4 = charSequence;
                        charSequence4 = charSequence;
                        charSequence4 = charSequence;
                        spannableStringBuilder3 = charSequence;
                        SpannableString spannableString15 = new SpannableString("p ");
                        spannableString15.setSpan(new ColoredImageSpan(R.drawable.msg_pin_mini), 0, 1, 33);
                        ((SpannableStringBuilder) spannableStringBuilder3).insert(0, (CharSequence) spannableString15);
                        charSequence4 = spannableStringBuilder3;
                    }
                    charSequence4 = charSequence;
                    charSequence4 = charSequence;
                    charSequence4 = charSequence;
                    charSequence4 = charSequence;
                    charSequence4 = charSequence;
                    charSequence4 = charSequence;
                    charSequence4 = charSequence;
                    charSequence4 = charSequence;
                    charSequence4 = charSequence;
                    charSequence4 = charSequence;
                    this.headerView.setSubtitle(charSequence4, z7);
                }
                hintView2 = this.privacyHint;
                if (hintView2 != null) {
                    hintView2.hide(false);
                }
                hintView3 = this.soundTooltip;
                if (hintView3 != null) {
                    hintView3.hide(false);
                }
            }
            charSequence3 = string;
            i2 = 2;
            charSequence = charSequence3;
            if (charSequence != null) {
                storyViewer3 = this.storyViewer;
                if (storyViewer3 != null) {
                    if (!(charSequence instanceof SpannableStringBuilder)) {
                        charSequence4 = charSequence;
                        charSequence4 = charSequence;
                        charSequence4 = charSequence;
                        charSequence4 = charSequence;
                        spannableStringBuilder3 = charSequence;
                        spannableStringBuilder3 = new SpannableStringBuilder(charSequence);
                    }
                    charSequence4 = charSequence;
                    charSequence4 = charSequence;
                    charSequence4 = charSequence;
                    charSequence4 = charSequence;
                    spannableStringBuilder3 = charSequence;
                    SpannableString spannableString16 = new SpannableString("p ");
                    spannableString16.setSpan(new ColoredImageSpan(R.drawable.msg_pin_mini), 0, 1, 33);
                    ((SpannableStringBuilder) spannableStringBuilder3).insert(0, (CharSequence) spannableString16);
                    charSequence4 = spannableStringBuilder3;
                }
                charSequence4 = charSequence;
                charSequence4 = charSequence;
                charSequence4 = charSequence;
                charSequence4 = charSequence;
                charSequence4 = charSequence;
                charSequence4 = charSequence;
                charSequence4 = charSequence;
                charSequence4 = charSequence;
                charSequence4 = charSequence;
                charSequence4 = charSequence;
                this.headerView.setSubtitle(charSequence4, z7);
            }
            hintView2 = this.privacyHint;
            if (hintView2 != null) {
                hintView2.hide(false);
            }
            hintView3 = this.soundTooltip;
            if (hintView3 != null) {
                hintView3.hide(false);
            }
        } else {
            this.headerView.setOnSubtitleClick(null);
            this.watchersCount = 0;
            setTitle(false, this.dialogId, this.currentStory.isLive);
            uploadingStory3 = this.currentStory.uploadingStory;
            if (uploadingStory3 != null) {
                if (uploadingStory3.failed) {
                    string = LocaleController.getString(R.string.FailedToUploadStory);
                } else {
                    string = StoriesUtilities.getUploadingStr(this.headerView.subtitleView[0], false, this.isEditing);
                }
            } else if (isBotsPreview()) {
                storyItem8 = this.currentStory.storyItem;
                if (storyItem8 != null) {
                    document = messageMedia.document;
                    if (document != null) {
                        string = LocaleController.formatStoryDate(document.date);
                    } else {
                        photo = messageMedia.photo;
                        if (photo != null) {
                            string = LocaleController.formatStoryDate(photo.date);
                        }
                        i2 = 2;
                        charSequence = charSequence3;
                        if (charSequence != null) {
                            storyViewer3 = this.storyViewer;
                            if (storyViewer3 != null) {
                                if (!(charSequence instanceof SpannableStringBuilder)) {
                                    charSequence4 = charSequence;
                                    charSequence4 = charSequence;
                                    charSequence4 = charSequence;
                                    charSequence4 = charSequence;
                                    spannableStringBuilder3 = charSequence;
                                    spannableStringBuilder3 = new SpannableStringBuilder(charSequence);
                                }
                                charSequence4 = charSequence;
                                charSequence4 = charSequence;
                                charSequence4 = charSequence;
                                charSequence4 = charSequence;
                                spannableStringBuilder3 = charSequence;
                                SpannableString spannableString17 = new SpannableString("p ");
                                spannableString17.setSpan(new ColoredImageSpan(R.drawable.msg_pin_mini), 0, 1, 33);
                                ((SpannableStringBuilder) spannableStringBuilder3).insert(0, (CharSequence) spannableString17);
                                charSequence4 = spannableStringBuilder3;
                            }
                            charSequence4 = charSequence;
                            charSequence4 = charSequence;
                            charSequence4 = charSequence;
                            charSequence4 = charSequence;
                            charSequence4 = charSequence;
                            charSequence4 = charSequence;
                            charSequence4 = charSequence;
                            charSequence4 = charSequence;
                            charSequence4 = charSequence;
                            charSequence4 = charSequence;
                            this.headerView.setSubtitle(charSequence4, z7);
                        }
                        hintView2 = this.privacyHint;
                        if (hintView2 != null) {
                            hintView2.hide(false);
                        }
                        hintView3 = this.soundTooltip;
                        if (hintView3 != null) {
                            hintView3.hide(false);
                        }
                    }
                }
                charSequence3 = _UrlKt.FRAGMENT_ENCODE_SET;
                i2 = 2;
                charSequence = charSequence3;
                if (charSequence != null) {
                    storyViewer3 = this.storyViewer;
                    if (storyViewer3 != null) {
                        if (!(charSequence instanceof SpannableStringBuilder)) {
                            charSequence4 = charSequence;
                            charSequence4 = charSequence;
                            charSequence4 = charSequence;
                            charSequence4 = charSequence;
                            spannableStringBuilder3 = charSequence;
                            spannableStringBuilder3 = new SpannableStringBuilder(charSequence);
                        }
                        charSequence4 = charSequence;
                        charSequence4 = charSequence;
                        charSequence4 = charSequence;
                        charSequence4 = charSequence;
                        spannableStringBuilder3 = charSequence;
                        SpannableString spannableString18 = new SpannableString("p ");
                        spannableString18.setSpan(new ColoredImageSpan(R.drawable.msg_pin_mini), 0, 1, 33);
                        ((SpannableStringBuilder) spannableStringBuilder3).insert(0, (CharSequence) spannableString18);
                        charSequence4 = spannableStringBuilder3;
                    }
                    charSequence4 = charSequence;
                    charSequence4 = charSequence;
                    charSequence4 = charSequence;
                    charSequence4 = charSequence;
                    charSequence4 = charSequence;
                    charSequence4 = charSequence;
                    charSequence4 = charSequence;
                    charSequence4 = charSequence;
                    charSequence4 = charSequence;
                    charSequence4 = charSequence;
                    this.headerView.setSubtitle(charSequence4, z7);
                }
                hintView2 = this.privacyHint;
                if (hintView2 != null) {
                    hintView2.hide(false);
                }
                hintView3 = this.soundTooltip;
                if (hintView3 != null) {
                    hintView3.hide(false);
                }
            } else {
                storyItemHolder2 = this.currentStory;
                storyItem6 = storyItemHolder2.storyItem;
                if (storyItem6 == null) {
                    j = 0;
                    i2 = 2;
                    charSequence = null;
                } else if (storyItem6.media instanceof TLRPC.TL_messageMediaVideoStream) {
                    SpannableStringBuilder spannableStringBuilder7 = new SpannableStringBuilder(LocaleController.getString(R.string.LiveStoryBadge));
                    spannableStringBuilder7.setSpan(new ReplacementSpan() { // from class: org.telegram.ui.Stories.PeerStoriesView.32
                        private final RectF rect = new RectF();
                        private final Paint bg = new Paint(1);
                        private final Text text = new Text(LocaleController.getString(R.string.LiveStoryBadge), 9.0f, AndroidUtilities.bold());

                        @Override // android.text.style.ReplacementSpan
                        public int getSize(Paint paint, CharSequence charSequence6, int i23, int i24, Paint.FontMetricsInt fontMetricsInt) {
                            return (int) (this.text.getWidth() + AndroidUtilities.dp(12.0f));
                        }

                        @Override // android.text.style.ReplacementSpan
                        public void draw(Canvas canvas, CharSequence charSequence6, int i23, int i24, float f4, int i25, int i26, int i27, Paint paint) {
                            float fDp = ((i25 + i27) / 2.0f) + AndroidUtilities.dp(1.33f);
                            this.rect.set(f4, fDp - AndroidUtilities.dp(7.0f), this.text.getWidth() + f4 + AndroidUtilities.dp(12.0f), AndroidUtilities.dp(7.0f) + fDp);
                            this.bg.setColor(-572850);
                            RectF rectF = this.rect;
                            canvas.drawRoundRect(rectF, rectF.height() / 2.0f, this.rect.height() / 2.0f, this.bg);
                            this.text.draw(canvas, AndroidUtilities.dp(6.0f) + f4, fDp, -1, 1.0f);
                        }
                    }, 0, spannableStringBuilder7.length(), 33);
                    spannableStringBuilder7.append((CharSequence) "  ");
                    storyViewer2 = this.storyViewer;
                    if (storyViewer2 != null) {
                        this.watchersCount = livePlayer.getWatchersCount();
                    }
                    spannableStringBuilder7.append((CharSequence) LocaleController.formatPluralStringComma("LiveStoryWatching", Math.max(1, this.watchersCount)));
                    string = spannableStringBuilder7;
                } else if (storyItem6.date == -1) {
                    string = LocaleController.getString(R.string.CachedStory);
                } else {
                    i2 = 2;
                    if (storyItemHolder2.getReply() != null) {
                        reply = this.currentStory.getReply();
                        spannableStringBuilder2 = new SpannableStringBuilder();
                        j = 0;
                        SpannableString spannableString19 = new SpannableString("r");
                        spannableString19.setSpan(new ColoredImageSpan(R.drawable.mini_repost_story), 0, spannableString19.length(), 33);
                        spannableStringBuilder2.append((CharSequence) spannableString19).append((CharSequence) " ");
                        if (reply.peerId != null) {
                            avatarSpan = new AvatarSpan(this.headerView.subtitleView[0], this.currentAccount, 15.0f);
                            SpannableString spannableString110 = new SpannableString("a");
                            spannableString110.setSpan(avatarSpan, 0, 1, 33);
                            spannableStringBuilder2.append((CharSequence) spannableString110).append((CharSequence) " ");
                            if (reply.peerId.longValue() > 0) {
                                TLRPC.User user5 = MessagesController.getInstance(this.currentAccount).getUser(reply.peerId);
                                avatarSpan.setUser(user5);
                                spannableStringBuilder2.append((CharSequence) UserObject.getUserName(user5));
                            } else {
                                chat = MessagesController.getInstance(this.currentAccount).getChat(Long.valueOf(-reply.peerId.longValue()));
                                avatarSpan.setChat(chat);
                                if (chat != null) {
                                    spannableStringBuilder2.append((CharSequence) chat.title);
                                }
                            }
                        } else {
                            str = this.currentStory.storyItem.fwd_from.from_name;
                            if (str != null) {
                                spannableStringBuilder2.append((CharSequence) str);
                            }
                        }
                        this.headerView.setOnSubtitleClick(new View.OnClickListener() { // from class: org.telegram.ui.Stories.PeerStoriesView$$ExternalSyntheticLambda19
                            @Override // android.view.View.OnClickListener
                            public final void onClick(View view) {
                                this.f$0.lambda$updatePosition$44(reply, view);
                            }
                        });
                        SpannableString spannableString111 = new SpannableString(".");
                        DotDividerSpan dotDividerSpan6 = new DotDividerSpan();
                        dotDividerSpan6.setTopPadding(AndroidUtilities.dp(1.5f));
                        dotDividerSpan6.setSize(5.0f);
                        spannableString111.setSpan(dotDividerSpan6, 0, spannableString111.length(), 33);
                        spannableStringBuilder2.append((CharSequence) " ").append((CharSequence) spannableString111).append((CharSequence) " ").append((CharSequence) LocaleController.formatShortDate(this.currentStory.storyItem.date));
                        spannableStringBuilder = spannableStringBuilder2;
                    } else {
                        j = 0;
                        if (!this.isGroup) {
                        }
                        storyDate = LocaleController.formatStoryDate(this.currentStory.storyItem.date);
                        charSequence2 = storyDate;
                        if (this.currentStory.storyItem.edited) {
                            SpannableStringBuilder spannableStringBuilderValueOf3 = SpannableStringBuilder.valueOf(storyDate);
                            DotDividerSpan dotDividerSpan7 = new DotDividerSpan();
                            dotDividerSpan7.setTopPadding(AndroidUtilities.dp(1.5f));
                            dotDividerSpan7.setSize(5.0f);
                            spannableStringBuilderValueOf3.append((CharSequence) " . ").setSpan(dotDividerSpan7, spannableStringBuilderValueOf3.length() - 2, spannableStringBuilderValueOf3.length() - 1, 0);
                            spannableStringBuilderValueOf3.append((CharSequence) LocaleController.getString(R.string.EditedMessage));
                            charSequence2 = spannableStringBuilderValueOf3;
                        }
                        charSequence = charSequence2;
                    }
                    z7 = false;
                    charSequence = spannableStringBuilder;
                }
                if (charSequence != null) {
                    storyViewer3 = this.storyViewer;
                    if (storyViewer3 != null) {
                        if (!(charSequence instanceof SpannableStringBuilder)) {
                            charSequence4 = charSequence;
                            charSequence4 = charSequence;
                            charSequence4 = charSequence;
                            charSequence4 = charSequence;
                            spannableStringBuilder3 = charSequence;
                            spannableStringBuilder3 = new SpannableStringBuilder(charSequence);
                        }
                        charSequence4 = charSequence;
                        charSequence4 = charSequence;
                        charSequence4 = charSequence;
                        charSequence4 = charSequence;
                        spannableStringBuilder3 = charSequence;
                        SpannableString spannableString112 = new SpannableString("p ");
                        spannableString112.setSpan(new ColoredImageSpan(R.drawable.msg_pin_mini), 0, 1, 33);
                        ((SpannableStringBuilder) spannableStringBuilder3).insert(0, (CharSequence) spannableString112);
                        charSequence4 = spannableStringBuilder3;
                    }
                    charSequence4 = charSequence;
                    charSequence4 = charSequence;
                    charSequence4 = charSequence;
                    charSequence4 = charSequence;
                    charSequence4 = charSequence;
                    charSequence4 = charSequence;
                    charSequence4 = charSequence;
                    charSequence4 = charSequence;
                    charSequence4 = charSequence;
                    charSequence4 = charSequence;
                    this.headerView.setSubtitle(charSequence4, z7);
                }
                hintView2 = this.privacyHint;
                if (hintView2 != null) {
                    hintView2.hide(false);
                }
                hintView3 = this.soundTooltip;
                if (hintView3 != null) {
                    hintView3.hide(false);
                }
            }
            charSequence3 = string;
            i2 = 2;
            charSequence = charSequence3;
            if (charSequence != null) {
                storyViewer3 = this.storyViewer;
                if (storyViewer3 != null) {
                    if (!(charSequence instanceof SpannableStringBuilder)) {
                        charSequence4 = charSequence;
                        charSequence4 = charSequence;
                        charSequence4 = charSequence;
                        charSequence4 = charSequence;
                        spannableStringBuilder3 = charSequence;
                        spannableStringBuilder3 = new SpannableStringBuilder(charSequence);
                    }
                    charSequence4 = charSequence;
                    charSequence4 = charSequence;
                    charSequence4 = charSequence;
                    charSequence4 = charSequence;
                    spannableStringBuilder3 = charSequence;
                    SpannableString spannableString113 = new SpannableString("p ");
                    spannableString113.setSpan(new ColoredImageSpan(R.drawable.msg_pin_mini), 0, 1, 33);
                    ((SpannableStringBuilder) spannableStringBuilder3).insert(0, (CharSequence) spannableString113);
                    charSequence4 = spannableStringBuilder3;
                }
                charSequence4 = charSequence;
                charSequence4 = charSequence;
                charSequence4 = charSequence;
                charSequence4 = charSequence;
                charSequence4 = charSequence;
                charSequence4 = charSequence;
                charSequence4 = charSequence;
                charSequence4 = charSequence;
                charSequence4 = charSequence;
                charSequence4 = charSequence;
                this.headerView.setSubtitle(charSequence4, z7);
            }
            hintView2 = this.privacyHint;
            if (hintView2 != null) {
                hintView2.hide(false);
            }
            hintView3 = this.soundTooltip;
            if (hintView3 != null) {
                hintView3.hide(false);
            }
        }
        StoryItemHolder storyItemHolder11 = this.currentStory;
        storyItem10 = storyItemHolder11.storyItem;
        if (storyItem17 != storyItem10) {
            this.currentStory.updateCaption();
        } else {
            this.currentStory.updateCaption();
        }
        storyItemHolder3 = this.currentStory;
        if (!storyItemHolder3.captionTranslated) {
            delegate.setTranslating(false);
        } else {
            delegate.setTranslating(false);
        }
        if (this.currentStory.isLive) {
            z10 = false;
        } else {
            z10 = false;
        }
        if (z10 != this.areLiveCommentsDisabled) {
            this.areLiveCommentsDisabled = z10;
            if (z10) {
                createPremiumBlockedText();
            }
            if (this.premiumBlockedText != null) {
                updatePremiumBlockedText();
            }
            chatActivityEnterView8 = this.chatActivityEnterView;
            if (chatActivityEnterView8 != null) {
                chatActivityEnterView8.setEnabled((this.isPremiumBlocked || this.currentStory.isLive) && !this.areLiveCommentsDisabled);
                this.chatActivityEnterView.updateSendAsButton();
            }
            checkStealthMode(true);
        }
        linearLayout = this.premiumBlockedText;
        if (linearLayout != null) {
            linearLayout.setVisibility(((this.isPremiumBlocked || this.currentStory.isLive) && !this.areLiveCommentsDisabled) ? 8 : 0);
        }
        if (this.unsupported) {
            createUnsupportedContainer();
            createReplyDisabledView();
            this.unsupportedContainer.setVisibility(0);
            this.replyDisabledTextView.setVisibility(0);
            this.allowShareLink = false;
            this.allowRepost = false;
            this.allowShare = false;
            chatActivityEnterView7 = this.chatActivityEnterView;
            if (chatActivityEnterView7 != null) {
                chatActivityEnterView7.setVisibility(8);
            }
            frameLayout5 = this.selfView;
            if (frameLayout5 != null) {
                frameLayout5.setVisibility(8);
            }
            linearLayout3 = this.bottomActionsLinearLayout;
            if (linearLayout3 != null) {
                linearLayout3.setVisibility(0);
            }
        } else {
            if (this.dialogId < j) {
                chat2 = MessagesController.getInstance(this.currentAccount).getChat(Long.valueOf(-this.dialogId));
            } else {
                chat2 = null;
            }
            if (this.currentStory.isLive) {
                if (this.chatActivityEnterView == null) {
                    createEnterView();
                }
                createCommentButton();
                createPaidReactionsButton();
                createMuteButton();
                this.chatActivityEnterView.setVisibility(0);
            } else if (!UserObject.isService(this.dialogId)) {
                chatActivityEnterView2.setVisibility(8);
            } else {
                chatActivityEnterView2.setVisibility(8);
            }
            chatActivityEnterView4 = this.chatActivityEnterView;
            if (chatActivityEnterView4 != null) {
                if (this.currentStory.isLive) {
                    onLongClickListener = new View.OnLongClickListener() { // from class: org.telegram.ui.Stories.PeerStoriesView$$ExternalSyntheticLambda21
                        @Override // android.view.View.OnLongClickListener
                        public final boolean onLongClick(View view) {
                            return this.f$0.lambda$updatePosition$48(view);
                        }
                    };
                } else {
                    onLongClickListener = null;
                }
                chatActivityEnterView4.setOnSendButtonLongClick(onLongClickListener);
                this.chatActivityEnterView.setLiveComment(this.currentStory.isLive, disabledPaidFeatures(true));
                ChatActivityEnterView chatActivityEnterView10 = this.chatActivityEnterView;
                if (this.currentStory.isLive) {
                    z11 = false;
                } else {
                    z11 = false;
                }
                chatActivityEnterView10.setSuggestionButtonVisible(z11, true);
            }
            if (this.isPremiumBlocked) {
                createPremiumBlockedText();
            }
            if (this.premiumBlockedText != null) {
                if (!this.isPremiumBlocked) {
                    updatePremiumBlockedText();
                } else {
                    updatePremiumBlockedText();
                }
                this.premiumBlockedText.setVisibility(((this.isPremiumBlocked || this.currentStory.isLive) && !this.areLiveCommentsDisabled) ? 8 : 0);
            }
            chatActivityEnterView5 = this.chatActivityEnterView;
            if (chatActivityEnterView5 != null) {
                chatActivityEnterView5.setEnabled((this.isPremiumBlocked || this.currentStory.isLive) && !this.areLiveCommentsDisabled);
            }
            frameLayout = this.selfView;
            if (frameLayout != null) {
                if (this.isSelf) {
                    i4 = 8;
                } else {
                    i4 = 8;
                }
                frameLayout.setVisibility(i4);
            }
            frameLayout2 = this.unsupportedContainer;
            if (frameLayout2 != null) {
                frameLayout2.setVisibility(8);
            }
            if (UserObject.isService(this.dialogId)) {
                createReplyDisabledView();
                this.replyDisabledTextView.setVisibility(0);
            } else {
                textView = this.replyDisabledTextView;
                if (textView != null) {
                    textView.setVisibility(8);
                }
            }
            linearLayout2 = this.bottomActionsLinearLayout;
            if (linearLayout2 != null) {
                if (isBotsPreview()) {
                    i3 = 8;
                } else {
                    i3 = 0;
                }
                linearLayout2.setVisibility(i3);
            }
        }
        commentButton = this.commentButton;
        if (commentButton != null) {
            if (this.unsupported) {
                i19 = 8;
            } else {
                i19 = 8;
            }
            commentButton.setVisibility(i19);
            this.commentButton.setCollapsed(this.liveCommentsView.isCollapsed(), false);
            this.commentButton.setCount(this.liveCommentsView.getUnreadMessagesCount());
        }
        muteButton = this.muteButton;
        if (muteButton != null) {
            if (this.unsupported) {
                i18 = 8;
            } else {
                i18 = 8;
            }
            muteButton.setVisibility(i18);
            MuteButton muteButton5 = this.muteButton;
            livePlayer2 = LivePlayer.recording;
            if (livePlayer2 == null) {
                z17 = false;
            } else {
                z17 = false;
            }
            muteButton5.setMuted(z17, true);
            MuteButton muteButton6 = this.muteButton;
            livePlayer3 = LivePlayer.recording;
            if (livePlayer3 != null) {
                z18 = true;
            } else {
                z18 = true;
            }
            muteButton6.setConnected(z18, true);
        }
        if (this.starsButton != null) {
            PaidReactionButton.PaidReactionButtonEffectsView paidReactionButtonEffectsView2 = this.starsButtonEffectsView;
            if (this.unsupported) {
                i16 = 8;
            } else {
                i16 = 8;
            }
            paidReactionButtonEffectsView2.setVisibility(i16);
            PaidReactionButton paidReactionButton2 = this.starsButton;
            if (this.unsupported) {
                i17 = 8;
            } else {
                i17 = 8;
            }
            paidReactionButton2.setVisibility(i17);
            layoutParams = (FrameLayout.LayoutParams) this.starsButton.getLayoutParams();
            muteButton2 = this.muteButton;
            if (muteButton2 == null) {
                f3 = 7.0f;
            } else {
                f3 = 7.0f;
            }
            iDp2 = AndroidUtilities.dp(f3);
            if (layoutParams.rightMargin != iDp2) {
                layoutParams.rightMargin = iDp2;
                this.starsButton.setLayoutParams(layoutParams);
            }
        }
        if (this.currentStory.isLive) {
            storyItemHolder6 = this.currentStory;
            if (storyItemHolder6.caption == null) {
                StoryCaptionView.StoryCaptionTextView storyCaptionTextView2 = this.storyCaptionView.captionTextview;
                StoryItemHolder storyItemHolder12 = this.currentStory;
                CharSequence charSequence6 = storyItemHolder12.caption;
                StoryCaptionView.Reply reply3 = storyItemHolder12.getReply();
                if (this.storyViewer.isTranslating) {
                    storyItemHolder7 = this.currentStory;
                    if (storyItemHolder7.captionTranslated) {
                        z15 = false;
                    } else {
                        z15 = false;
                    }
                } else {
                    z15 = false;
                }
                if (storyItem17 == this.currentStory.storyItem) {
                    z16 = true;
                } else {
                    z16 = false;
                }
                storyCaptionTextView2.setText(charSequence6, reply3, z15, z16);
                this.storyCaptionView.setVisibility(0);
            } else {
                StoryCaptionView.StoryCaptionTextView storyCaptionTextView3 = this.storyCaptionView.captionTextview;
                StoryItemHolder storyItemHolder13 = this.currentStory;
                CharSequence charSequence7 = storyItemHolder13.caption;
                StoryCaptionView.Reply reply4 = storyItemHolder13.getReply();
                if (this.storyViewer.isTranslating) {
                    storyItemHolder7 = this.currentStory;
                    if (storyItemHolder7.captionTranslated) {
                        z15 = false;
                    } else {
                        z15 = false;
                    }
                } else {
                    z15 = false;
                }
                if (storyItem17 == this.currentStory.storyItem) {
                    z16 = true;
                } else {
                    z16 = false;
                }
                storyCaptionTextView3.setText(charSequence7, reply4, z15, z16);
                this.storyCaptionView.setVisibility(0);
            }
        } else {
            if (this.isActive) {
                this.delegate.setIsCaption(false);
                Delegate delegate4 = this.delegate;
                this.isCaptionPartVisible = false;
                delegate4.setIsCaptionPartVisible(false);
            }
            this.storyCaptionView.setVisibility(8);
        }
        storyItem11 = this.currentStory.storyItem;
        if (storyItem11 != null) {
            messageMedia2 = storyItem11.media;
            if (messageMedia2 instanceof TLRPC.TL_messageMediaVideoStream) {
                if (this.liveCommentsView.setup(this.dialogId, ((TLRPC.TL_messageMediaVideoStream) messageMedia2).call)) {
                    this.liveCommentsView.setCollapsed(false, false);
                    this.messageStars = j;
                    chatActivityEnterView6 = this.chatActivityEnterView;
                    if (chatActivityEnterView6 != null) {
                        chatActivityEnterView6.checkSendButton(true);
                        this.chatActivityEnterView.updateSendButtonPaid();
                        checkStealthMode(true);
                    }
                }
                this.liveCommentsView.setVisibility(0);
            } else {
                this.liveCommentsView.setup(this.dialogId, null);
                this.liveCommentsView.setVisibility(8);
            }
        } else {
            this.liveCommentsView.setup(this.dialogId, null);
            this.liveCommentsView.setVisibility(8);
        }
        this.storyContainer.invalidate();
        if (this.delegate != null) {
            this.delegate.onPeerSelected(this.dialogId, this.selectedPosition);
        }
        if (this.isChannel) {
            ImageView imageView5 = this.shareButton;
            if (this.allowShare) {
                i13 = i21;
            } else {
                i13 = i21;
            }
            imageView5.setVisibility(i13);
            frameLayout4 = this.repostButtonContainer;
            if (frameLayout4 != null) {
                if (this.allowRepost) {
                    i15 = 8;
                } else {
                    i15 = 8;
                }
                frameLayout4.setVisibility(i15);
            }
            FrameLayout frameLayout8 = this.likeButtonContainer;
            if (this.isFailed) {
                i14 = 8;
            } else {
                i14 = 8;
            }
            frameLayout8.setVisibility(i14);
        } else {
            ImageView imageView6 = this.shareButton;
            if (this.allowShare) {
                i5 = i21;
            } else {
                i5 = i21;
            }
            imageView6.setVisibility(i5);
            frameLayout3 = this.repostButtonContainer;
            if (frameLayout3 != null) {
                frameLayout3.setVisibility(8);
            }
            FrameLayout frameLayout9 = this.likeButtonContainer;
            if (this.isSelf) {
                i6 = 8;
            } else {
                i6 = 8;
            }
            frameLayout9.setVisibility(i6);
            this.likeButtonContainer.getLayoutParams().width = AndroidUtilities.dp(40.0f);
        }
        this.likeButtonContainer.requestLayout();
        this.storyViewer.savedPositions.append(this.dialogId, i);
        if (this.isActive) {
            requestVideoPlayer(0L);
            updatePreloadImages();
            this.imageReceiver.bumpPriority();
        }
        this.liveCommentsView.setLivePlayer(this.storyViewer.livePlayer);
        this.listPosition = 0;
        if (this.storyViewer.storiesList != null) {
            int i23 = storyItem14.id;
            while (i12 < this.storyViewer.storiesList.messageObjects.size()) {
                messageObject = (MessageObject) this.storyViewer.storiesList.messageObjects.get(i12);
                if (messageObject == null) {
                }
            }
        }
        i7 = this.selectedPosition;
        this.linesPosition = i7;
        i8 = this.count;
        this.linesCount = i8;
        if (this.storyViewer.reversed) {
            this.linesPosition = (i8 - 1) - i7;
        }
        if (this.currentStory.isVideo()) {
            this.muteIconContainer.setVisibility(0);
            if (this.currentStory.hasSound()) {
                f2 = 1.0f;
            } else {
                f2 = 0.5f;
            }
            this.muteIconViewAlpha = f2;
            if (this.currentStory.hasSound()) {
                this.muteIconView.setVisibility(0);
                this.noSoundIconView.setVisibility(8);
            } else {
                this.muteIconView.setVisibility(8);
                this.noSoundIconView.setVisibility(0);
            }
            this.muteIconContainer.setAlpha(this.muteIconViewAlpha * (1.0f - this.outT));
        } else {
            this.muteIconContainer.setVisibility(8);
        }
        storyItemHolder4 = this.currentStory;
        uploadingStory4 = storyItemHolder4.uploadingStory;
        if (uploadingStory4 != null) {
            StoryPrivacyButton storyPrivacyButton5 = this.privacyButton;
            boolean z28 = this.isSelf;
            if (z6) {
                z14 = false;
            } else {
                z14 = false;
            }
            storyPrivacyButton5.set(z28, uploadingStory4, z14);
        } else {
            storyItem12 = storyItemHolder4.storyItem;
            if (storyItem12 != null) {
                StoryPrivacyButton storyPrivacyButton6 = this.privacyButton;
                boolean z29 = this.isSelf;
                if (z6) {
                    z13 = false;
                } else {
                    z13 = false;
                }
                storyPrivacyButton6.set(z29, storyItem12, z13);
            } else {
                StoryPrivacyButton storyPrivacyButton7 = this.privacyButton;
                boolean z210 = this.isSelf;
                if (z6) {
                    z12 = false;
                } else {
                    z12 = false;
                }
                storyPrivacyButton7.set(z210, (TL_stories.StoryItem) null, z12);
            }
        }
        this.editedPrivacy = false;
        StoryPrivacyButton storyPrivacyButton8 = this.privacyButton;
        if (this.muteIconContainer.getVisibility() == 0) {
            f = -AndroidUtilities.dp(44.0f);
        } else {
            f = 0.0f;
        }
        storyPrivacyButton8.setTranslationX(f);
        if (z8) {
            this.drawReactionEffect = false;
            storyItem13 = this.currentStory.storyItem;
            if (storyItem13 != null) {
                this.storiesLikeButton.setReaction(null);
            } else {
                this.storiesLikeButton.setReaction(null);
            }
        }
        uploadingStory5 = this.currentStory.uploadingStory;
        if (uploadingStory5 == null) {
            if (this.failView != null) {
                viewPropertyAnimator = this.failViewAnimator;
                if (viewPropertyAnimator != null) {
                    viewPropertyAnimator.cancel();
                    this.failViewAnimator = null;
                }
                if (!z6) {
                    this.failView.setAlpha(0.0f);
                    this.failView.setVisibility(8);
                } else {
                    this.failView.setAlpha(0.0f);
                    this.failView.setVisibility(8);
                }
            }
        } else if (this.failView != null) {
            viewPropertyAnimator = this.failViewAnimator;
            if (viewPropertyAnimator != null) {
                viewPropertyAnimator.cancel();
                this.failViewAnimator = null;
            }
            if (!z6) {
                this.failView.setAlpha(0.0f);
                this.failView.setVisibility(8);
            } else {
                this.failView.setAlpha(0.0f);
                this.failView.setVisibility(8);
            }
        }
        this.sharedResources.setIconMuted(!this.storyViewer.soundEnabled(), false);
        if (this.isActive) {
            FileLog.d("StoryViewer displayed story dialogId=" + this.dialogId + " storyId=" + this.currentStory.storyItem.id + " " + this.currentStory.getMediaDebugString());
        }
        if (this.isSelf) {
            SelfStoryViewsPage.preload(this.currentAccount, this.dialogId, this.currentStory.storyItem);
        }
        SimpleTextView simpleTextView2 = this.headerView.titleView;
        storiesList2 = this.storyViewer.storiesList;
        if (storiesList2 != null) {
            iDp = 0;
        } else {
            iDp = 0;
        }
        simpleTextView2.setPadding(0, 0, iDp, 0);
        MessagesController.getInstance(this.currentAccount).getTranslateController().detectStoryLanguage(this.currentStory.storyItem);
        if (!z) {
            Runnable runnable2 = new Runnable() { // from class: org.telegram.ui.Stories.PeerStoriesView$$ExternalSyntheticLambda23
                @Override // java.lang.Runnable
                public final void run() {
                    this.f$0.lambda$updatePosition$50();
                }
            };
            this.reactionsTooltipRunnable = runnable2;
            AndroidUtilities.runOnUIThread(runnable2, 500L);
        }
        hintView4 = this.soundTooltip;
        if (hintView4 == null) {
            i9 = 0;
            if (MessagesController.getGlobalMainSettings().getInt("taptostorysoundhint", 0) < i2) {
                AndroidUtilities.cancelRunOnUIThread(this.showTapToSoundHint);
                AndroidUtilities.runOnUIThread(this.showTapToSoundHint, 250L);
            }
        } else {
            i9 = 0;
            if (MessagesController.getGlobalMainSettings().getInt("taptostorysoundhint", 0) < i2) {
                AndroidUtilities.cancelRunOnUIThread(this.showTapToSoundHint);
                AndroidUtilities.runOnUIThread(this.showTapToSoundHint, 250L);
            }
        }
        imageView = this.optionsIconView;
        if (imageView != null) {
            if (isBotsPreview()) {
                i11 = i9;
            } else {
                i11 = i9;
            }
            imageView.setVisibility(i11);
        }
        imageView2 = this.pipIconView;
        if (imageView2 != null) {
            if (this.currentStory.isLive) {
                i10 = 8;
            } else {
                i10 = 8;
            }
            imageView2.setVisibility(i10);
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$updatePosition$44(StoryCaptionView.Reply reply, View view) {
        Integer num;
        if (reply.peerId != null) {
            Bundle bundle = new Bundle();
            if (reply.peerId.longValue() >= 0) {
                bundle.putLong("user_id", reply.peerId.longValue());
            } else {
                bundle.putLong("chat_id", -reply.peerId.longValue());
            }
            if (reply.isRepostMessage && (num = reply.messageId) != null) {
                bundle.putInt("message_id", num.intValue());
                this.storyViewer.presentFragment(new ChatActivity(bundle));
                return;
            } else {
                this.storyViewer.presentFragment(new ProfileActivity(bundle));
                return;
            }
        }
        BulletinFactory.of(this.storyContainer, this.resourcesProvider).createSimpleBulletin(R.raw.error, LocaleController.getString(R.string.StoryHidAccount)).setTag(3).show(true);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$updatePosition$45(long j, View view) {
        Bundle bundle = new Bundle();
        if (j >= 0) {
            bundle.putLong("user_id", j);
        } else {
            bundle.putLong("chat_id", -j);
        }
        this.storyViewer.presentFragment(new ProfileActivity(bundle));
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ boolean lambda$updatePosition$48(View view) {
        if (disabledPaidFeatures(true)) {
            return false;
        }
        ItemOptions.makeOptions(this.storyViewer.containerView, this.resourcesProvider, view).add(R.drawable.msg_edit, LocaleController.getString(R.string.LiveStoryMessageEditStars), new Runnable() { // from class: org.telegram.ui.Stories.PeerStoriesView$$ExternalSyntheticLambda30
            @Override // java.lang.Runnable
            public final void run() {
                this.f$0.lambda$updatePosition$46();
            }
        }).addIf(this.messageStars > 0, R.drawable.menu_delete_paid, LocaleController.getString(R.string.LiveStoryMessageRemoveStars), new Runnable() { // from class: org.telegram.ui.Stories.PeerStoriesView$$ExternalSyntheticLambda31
            @Override // java.lang.Runnable
            public final void run() {
                this.f$0.lambda$updatePosition$47();
            }
        }).setGravity(5).forceTop(true).show();
        return true;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$updatePosition$47() {
        this.messageStars = 0L;
        ChatActivityEnterView chatActivityEnterView = this.chatActivityEnterView;
        if (chatActivityEnterView != null) {
            chatActivityEnterView.checkSendButton(true);
            this.chatActivityEnterView.updateSendButtonPaid();
            checkStealthMode(true);
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$updatePosition$49() {
        this.failView.setVisibility(8);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$updatePosition$50() {
        if (this.storyViewer.isShown()) {
            this.reactionsTooltipRunnable = null;
            if (this.reactionsLongpressTooltip == null) {
                HintView2 joint = new HintView2(getContext(), 3).setJoint(1.0f, -22.0f);
                this.reactionsLongpressTooltip = joint;
                joint.setBgColor(ColorUtils.setAlphaComponent(ColorUtils.blendARGB(-16777216, -1, 0.13f), 240));
                this.reactionsLongpressTooltip.setBounce(false);
                this.reactionsLongpressTooltip.setText(LocaleController.getString(R.string.ReactionLongTapHint));
                this.reactionsLongpressTooltip.setPadding(AndroidUtilities.dp(8.0f), 0, AndroidUtilities.dp(8.0f), AndroidUtilities.dp(1.0f));
                this.storyContainer.addView(this.reactionsLongpressTooltip, LayoutHelper.createFrame(-1, -2.0f, 85, 0.0f, 0.0f, 0.0f, this.BIG_SCREEN ? 0.0f : 56.0f));
            }
            this.reactionsLongpressTooltip.show();
            SharedConfig.setStoriesReactionsLongPressHintUsed(true);
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public boolean isEditBotsPreview() {
        TLRPC.User user;
        return isBotsPreview() && (user = MessagesController.getInstance(this.currentAccount).getUser(Long.valueOf(this.storyViewer.storiesList.dialogId))) != null && user.bot && user.bot_can_edit;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public boolean isBotsPreview() {
        StoriesController.StoriesList storiesList;
        StoryViewer storyViewer = this.storyViewer;
        return (storyViewer == null || (storiesList = storyViewer.storiesList) == null || storiesList.type != 4) ? false : true;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$new$51() {
        showNoSoundHint(false);
        MessagesController.getGlobalMainSettings().edit().putInt("taptostorysoundhint", MessagesController.getGlobalMainSettings().getInt("taptostorysoundhint", 0) + 1).apply();
    }

    /* JADX INFO: Access modifiers changed from: private */
    /* JADX INFO: renamed from: onHighlightLiveMessage, reason: merged with bridge method [inline-methods] */
    public void lambda$updatePosition$46() {
        MessagesController.getGlobalMainSettings().edit().putInt("taptostoryhighlighthint", 3).apply();
        TLRPC.TL_textWithEntities textWithEntities = this.chatActivityEnterView.getTextWithEntities();
        long clientUserId = UserConfig.getInstance(this.currentAccount).getClientUserId();
        TLRPC.Peer defaultSendAs = this.storyViewer.livePlayer.getDefaultSendAs();
        if (defaultSendAs != null) {
            clientUserId = DialogObject.getPeerDialogId(defaultSendAs);
        }
        Context context = getContext();
        int i = this.currentAccount;
        HighlightMessageSheet.open(context, i, clientUserId, DialogObject.getShortName(i, this.dialogId), textWithEntities, getMessageMinPrice(), this.messageStars, new Utilities.Callback() { // from class: org.telegram.ui.Stories.PeerStoriesView$$ExternalSyntheticLambda45
            @Override // org.telegram.messenger.Utilities.Callback
            public final void run(Object obj) {
                this.f$0.lambda$onHighlightLiveMessage$52((Long) obj);
            }
        }, new DarkThemeResourceProvider());
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$onHighlightLiveMessage$52(Long l) {
        this.messageStars = l.longValue();
        ChatActivityEnterView chatActivityEnterView = this.chatActivityEnterView;
        if (chatActivityEnterView != null) {
            chatActivityEnterView.checkSendButton(true);
            this.chatActivityEnterView.updateSendButtonPaid();
        }
        checkStealthMode(true);
    }

    private void createReplyDisabledView() {
        if (this.replyDisabledTextView != null) {
            return;
        }
        TextView textView = new TextView(getContext()) { // from class: org.telegram.ui.Stories.PeerStoriesView.33
            @Override // android.view.View
            public void setTranslationY(float f) {
                super.setTranslationY(f);
            }
        };
        this.replyDisabledTextView = textView;
        textView.setTextSize(1, 14.0f);
        this.replyDisabledTextView.setTextColor(ColorUtils.blendARGB(-16777216, -1, 0.5f));
        this.replyDisabledTextView.setGravity(19);
        this.replyDisabledTextView.setText(LocaleController.getString(R.string.StoryReplyDisabled));
        addView(this.replyDisabledTextView, LayoutHelper.createFrame(-2, 40.0f, 3, 16.0f, 0.0f, 16.0f, 0.0f));
    }

    /* JADX WARN: Code duplicated, block: B:17:0x0074 A[PHI: r4 r5
  0x0074: PHI (r4v5 int) = (r4v4 int), (r4v17 int) binds: [B:15:0x006d, B:12:0x005e] A[DONT_GENERATE, DONT_INLINE]
  0x0074: PHI (r5v1 org.telegram.messenger.ImageReceiver) = (r5v0 org.telegram.messenger.ImageReceiver), (r5v12 org.telegram.messenger.ImageReceiver) binds: [B:15:0x006d, B:12:0x005e] A[DONT_GENERATE, DONT_INLINE]] */
    /* JADX WARN: Code duplicated, block: B:22:0x0098  */
    /* JADX WARN: Code duplicated, block: B:25:0x00a2 A[DONT_INVERT] */
    /* JADX WARN: Code duplicated, block: B:26:0x00a4  */
    /* JADX WARN: Code duplicated, block: B:29:0x00ad  */
    /* JADX WARN: Code duplicated, block: B:32:0x00c8  */
    /* JADX WARN: Code duplicated, block: B:36:0x00dc  */
    /* JADX WARN: Code duplicated, block: B:39:0x0150  */
    /* JADX WARN: Code duplicated, block: B:40:0x0151 A[Catch: UnsupportedEncodingException -> 0x0199, TryCatch #0 {UnsupportedEncodingException -> 0x0199, blocks: (B:37:0x00e8, B:41:0x0153, B:40:0x0151), top: B:57:0x00e8 }] */
    /* JADX WARN: Code duplicated, block: B:47:0x01a1  */
    /* JADX WARN: Code duplicated, block: B:50:0x01aa  */
    /* JADX WARN: Code duplicated, block: B:52:0x01b4  */
    /* JADX WARN: Code duplicated, block: B:64:0x01d7 A[SYNTHETIC] */
    /* JADX WARN: Code duplicated, block: B:66:0x01d7 A[SYNTHETIC] */
    /* JADX WARN: Code duplicated, block: B:68:0x01d4 A[SYNTHETIC] */
    private void updatePreloadImages() {
        int size;
        ImageReceiver imageReceiver;
        TL_stories.StoryItem storyItem;
        TLRPC.MessageMedia messageMedia;
        int i;
        byte[] bArr;
        int iMax = (int) (Math.max(AndroidUtilities.getRealScreenSize().x, AndroidUtilities.getRealScreenSize().y) / AndroidUtilities.density);
        String str = iMax + "_" + iMax;
        this.uriesToPrepare.clear();
        this.documentsToPrepare.clear();
        for (int i2 = 0; i2 < this.preloadReactionHolders.size(); i2++) {
            ((ReactionImageHolder) this.preloadReactionHolders.get(i2)).onAttachedToWindow(false);
        }
        this.preloadReactionHolders.clear();
        for (int i3 = 0; i3 < 2; i3++) {
            int i4 = this.selectedPosition;
            if (i3 == 0) {
                size = i4 - 1;
                imageReceiver = this.leftPreloadImageReceiver;
                if (size < 0) {
                    imageReceiver.clearImage();
                } else if (this.uploadingStories.isEmpty() && size >= this.storyItems.size()) {
                    setStoryImage((StoriesController.UploadingStory) this.uploadingStories.get(size - this.storyItems.size()), imageReceiver, str);
                } else if (!this.storyItems.isEmpty()) {
                    if (size < 0) {
                        size = 0;
                    }
                    if (size >= this.storyItems.size()) {
                        size = this.storyItems.size() - 1;
                    }
                    storyItem = (TL_stories.StoryItem) this.storyItems.get(size);
                    storyItem.dialogId = this.dialogId;
                    setStoryImage(storyItem, imageReceiver, str);
                    messageMedia = storyItem.media;
                    if (messageMedia != null && MessageObject.isVideoDocument(messageMedia.getDocument())) {
                        TLRPC.Document document = storyItem.media.getDocument();
                        if (storyItem.fileReference == 0) {
                            storyItem.fileReference = FileLoader.getInstance(this.currentAccount).getFileReference(storyItem);
                        }
                        try {
                            StringBuilder sb = new StringBuilder();
                            sb.append("?account=");
                            sb.append(this.currentAccount);
                            sb.append("&id=");
                            sb.append(document.id);
                            sb.append("&hash=");
                            sb.append(document.access_hash);
                            sb.append("&dc=");
                            sb.append(document.dc_id);
                            sb.append("&size=");
                            sb.append(document.size);
                            sb.append("&mime=");
                            sb.append(URLEncoder.encode(document.mime_type, "UTF-8"));
                            sb.append("&rid=");
                            sb.append(storyItem.fileReference);
                            sb.append("&name=");
                            sb.append(URLEncoder.encode(FileLoader.getDocumentFileName(document), "UTF-8"));
                            sb.append("&reference=");
                            bArr = document.file_reference;
                            if (bArr == null) {
                                bArr = new byte[0];
                            }
                            sb.append(Utilities.bytesToHex(bArr));
                            sb.append("&sid=");
                            sb.append(storyItem.id);
                            sb.append("&did=");
                            sb.append(storyItem.dialogId);
                            String string = sb.toString();
                            this.uriesToPrepare.add(Uri.parse("tg://" + FileLoader.getAttachFileName(document) + string));
                            this.documentsToPrepare.add(document);
                        } catch (UnsupportedEncodingException e) {
                            e.printStackTrace();
                        }
                    }
                    if (storyItem.media_areas != null) {
                        for (i = 0; i < storyItem.media_areas.size(); i++) {
                            if (storyItem.media_areas.get(i) instanceof TL_stories.TL_mediaAreaSuggestedReaction) {
                                TL_stories.TL_mediaAreaSuggestedReaction tL_mediaAreaSuggestedReaction = (TL_stories.TL_mediaAreaSuggestedReaction) storyItem.media_areas.get(i);
                                ReactionImageHolder reactionImageHolder = new ReactionImageHolder(this);
                                reactionImageHolder.setVisibleReaction(ReactionsLayoutInBubble.VisibleReaction.fromTL(tL_mediaAreaSuggestedReaction.reaction));
                                reactionImageHolder.onAttachedToWindow(this.attachedToWindow);
                                this.preloadReactionHolders.add(reactionImageHolder);
                            }
                        }
                    }
                }
            } else {
                size = i4 + 1;
                imageReceiver = this.rightPreloadImageReceiver;
                if (size >= getStoriesCount()) {
                    imageReceiver.clearImage();
                } else if (this.uploadingStories.isEmpty()) {
                    if (!this.storyItems.isEmpty()) {
                        if (size < 0) {
                            size = 0;
                        }
                        if (size >= this.storyItems.size()) {
                            size = this.storyItems.size() - 1;
                        }
                        storyItem = (TL_stories.StoryItem) this.storyItems.get(size);
                        storyItem.dialogId = this.dialogId;
                        setStoryImage(storyItem, imageReceiver, str);
                        messageMedia = storyItem.media;
                        if (messageMedia != null) {
                            TLRPC.Document document2 = storyItem.media.getDocument();
                            if (storyItem.fileReference == 0) {
                                storyItem.fileReference = FileLoader.getInstance(this.currentAccount).getFileReference(storyItem);
                            }
                            StringBuilder sb2 = new StringBuilder();
                            sb2.append("?account=");
                            sb2.append(this.currentAccount);
                            sb2.append("&id=");
                            sb2.append(document2.id);
                            sb2.append("&hash=");
                            sb2.append(document2.access_hash);
                            sb2.append("&dc=");
                            sb2.append(document2.dc_id);
                            sb2.append("&size=");
                            sb2.append(document2.size);
                            sb2.append("&mime=");
                            sb2.append(URLEncoder.encode(document2.mime_type, "UTF-8"));
                            sb2.append("&rid=");
                            sb2.append(storyItem.fileReference);
                            sb2.append("&name=");
                            sb2.append(URLEncoder.encode(FileLoader.getDocumentFileName(document2), "UTF-8"));
                            sb2.append("&reference=");
                            bArr = document2.file_reference;
                            if (bArr == null) {
                                bArr = new byte[0];
                            }
                            sb2.append(Utilities.bytesToHex(bArr));
                            sb2.append("&sid=");
                            sb2.append(storyItem.id);
                            sb2.append("&did=");
                            sb2.append(storyItem.dialogId);
                            String string2 = sb2.toString();
                            this.uriesToPrepare.add(Uri.parse("tg://" + FileLoader.getAttachFileName(document2) + string2));
                            this.documentsToPrepare.add(document2);
                        }
                        if (storyItem.media_areas != null) {
                            while (i < storyItem.media_areas.size()) {
                                if (storyItem.media_areas.get(i) instanceof TL_stories.TL_mediaAreaSuggestedReaction) {
                                    TL_stories.TL_mediaAreaSuggestedReaction tL_mediaAreaSuggestedReaction2 = (TL_stories.TL_mediaAreaSuggestedReaction) storyItem.media_areas.get(i);
                                    ReactionImageHolder reactionImageHolder2 = new ReactionImageHolder(this);
                                    reactionImageHolder2.setVisibleReaction(ReactionsLayoutInBubble.VisibleReaction.fromTL(tL_mediaAreaSuggestedReaction2.reaction));
                                    reactionImageHolder2.onAttachedToWindow(this.attachedToWindow);
                                    this.preloadReactionHolders.add(reactionImageHolder2);
                                }
                            }
                        }
                    }
                } else if (!this.storyItems.isEmpty()) {
                    if (size < 0) {
                        size = 0;
                    }
                    if (size >= this.storyItems.size()) {
                        size = this.storyItems.size() - 1;
                    }
                    storyItem = (TL_stories.StoryItem) this.storyItems.get(size);
                    storyItem.dialogId = this.dialogId;
                    setStoryImage(storyItem, imageReceiver, str);
                    messageMedia = storyItem.media;
                    if (messageMedia != null) {
                        TLRPC.Document document3 = storyItem.media.getDocument();
                        if (storyItem.fileReference == 0) {
                            storyItem.fileReference = FileLoader.getInstance(this.currentAccount).getFileReference(storyItem);
                        }
                        StringBuilder sb3 = new StringBuilder();
                        sb3.append("?account=");
                        sb3.append(this.currentAccount);
                        sb3.append("&id=");
                        sb3.append(document3.id);
                        sb3.append("&hash=");
                        sb3.append(document3.access_hash);
                        sb3.append("&dc=");
                        sb3.append(document3.dc_id);
                        sb3.append("&size=");
                        sb3.append(document3.size);
                        sb3.append("&mime=");
                        sb3.append(URLEncoder.encode(document3.mime_type, "UTF-8"));
                        sb3.append("&rid=");
                        sb3.append(storyItem.fileReference);
                        sb3.append("&name=");
                        sb3.append(URLEncoder.encode(FileLoader.getDocumentFileName(document3), "UTF-8"));
                        sb3.append("&reference=");
                        bArr = document3.file_reference;
                        if (bArr == null) {
                            bArr = new byte[0];
                        }
                        sb3.append(Utilities.bytesToHex(bArr));
                        sb3.append("&sid=");
                        sb3.append(storyItem.id);
                        sb3.append("&did=");
                        sb3.append(storyItem.dialogId);
                        String string3 = sb3.toString();
                        this.uriesToPrepare.add(Uri.parse("tg://" + FileLoader.getAttachFileName(document3) + string3));
                        this.documentsToPrepare.add(document3);
                    }
                    if (storyItem.media_areas != null) {
                        while (i < storyItem.media_areas.size()) {
                            if (storyItem.media_areas.get(i) instanceof TL_stories.TL_mediaAreaSuggestedReaction) {
                                TL_stories.TL_mediaAreaSuggestedReaction tL_mediaAreaSuggestedReaction3 = (TL_stories.TL_mediaAreaSuggestedReaction) storyItem.media_areas.get(i);
                                ReactionImageHolder reactionImageHolder3 = new ReactionImageHolder(this);
                                reactionImageHolder3.setVisibleReaction(ReactionsLayoutInBubble.VisibleReaction.fromTL(tL_mediaAreaSuggestedReaction3.reaction));
                                reactionImageHolder3.onAttachedToWindow(this.attachedToWindow);
                                this.preloadReactionHolders.add(reactionImageHolder3);
                            }
                        }
                    }
                }
            }
        }
        this.delegate.preparePlayer(this.documentsToPrepare, this.uriesToPrepare);
    }

    private void setStoryImage(TL_stories.StoryItem storyItem, ImageReceiver imageReceiver, String str) {
        ArrayList arrayList;
        StoriesController.UploadingStory uploadingStoryFindEditingStory = this.storiesController.findEditingStory(this.dialogId, storyItem);
        if (uploadingStoryFindEditingStory != null) {
            setStoryImage(uploadingStoryFindEditingStory, imageReceiver, str);
            return;
        }
        TLRPC.MessageMedia messageMedia = storyItem.media;
        boolean zEndsWith = messageMedia != null && MessageObject.isVideoDocument(messageMedia.getDocument());
        String str2 = storyItem.attachPath;
        if (str2 != null) {
            if (storyItem.media == null) {
                zEndsWith = str2.toLowerCase().endsWith(".mp4");
            }
            if (zEndsWith) {
                imageReceiver.setImage(ImageLocation.getForPath(storyItem.attachPath), str + "_pframe", ImageLocation.getForPath(storyItem.firstFramePath), str, null, null, null, 0L, null, null, 0);
                return;
            }
            imageReceiver.setImage(ImageLocation.getForPath(storyItem.attachPath), str, null, null, null, 0L, null, null, 0);
            return;
        }
        if (zEndsWith) {
            TLRPC.PhotoSize closestPhotoSizeWithSize = FileLoader.getClosestPhotoSizeWithSize(storyItem.media.getDocument().thumbs, MediaDataController.MAX_STYLE_RUNS_COUNT);
            imageReceiver.setImage(ImageLocation.getForDocument(storyItem.media.getDocument()), str + "_pframe", ImageLocation.getForDocument(closestPhotoSizeWithSize, storyItem.media.getDocument()), str, null, null, null, 0L, null, storyItem, 0);
            return;
        }
        TLRPC.MessageMedia messageMedia2 = storyItem.media;
        TLRPC.Photo photo = messageMedia2 != null ? messageMedia2.photo : null;
        if (photo != null && (arrayList = photo.sizes) != null) {
            TLRPC.PhotoSize closestPhotoSizeWithSize2 = FileLoader.getClosestPhotoSizeWithSize(arrayList, Integer.MAX_VALUE);
            FileLoader.getClosestPhotoSizeWithSize(photo.sizes, 800);
            imageReceiver.setImage(null, null, ImageLocation.getForPhoto(closestPhotoSizeWithSize2, photo), str, null, null, null, 0L, null, storyItem, 0);
            return;
        }
        imageReceiver.clearImage();
    }

    private void setStoryImage(StoriesController.UploadingStory uploadingStory, ImageReceiver imageReceiver, String str) {
        if (uploadingStory.isVideo) {
            imageReceiver.setImage(null, null, ImageLocation.getForPath(uploadingStory.firstFramePath), str, null, null, null, 0L, null, null, 0);
        } else {
            imageReceiver.setImage(ImageLocation.getForPath(uploadingStory.path), str, null, null, null, 0L, null, null, 0);
        }
    }

    private void cancelWaiting() {
        Runnable runnable = this.cancellableViews;
        if (runnable != null) {
            runnable.run();
            this.cancellableViews = null;
        }
        this.showViewsProgress = false;
        if (this.isActive) {
            this.delegate.setIsWaiting(false);
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void updateUserViews(boolean z) {
        int i;
        StoryItemHolder storyItemHolder = this.currentStory;
        TL_stories.StoryItem storyItem = storyItemHolder.storyItem;
        if (storyItem == null) {
            storyItem = storyItemHolder.editingSourceItem;
        }
        boolean z2 = this.isChannel;
        if (z2 || this.isSelf) {
            if (storyItem == null) {
                this.selfStatusView.setText(_UrlKt.FRAGMENT_ENCODE_SET);
                this.selfAvatarsContainer.setVisibility(8);
                this.selfAvatarsView.setVisibility(8);
                return;
            }
            if (z2) {
                if (storyItem.views == null) {
                    storyItem.views = new TL_stories.TL_storyViews();
                }
                TL_stories.StoryViews storyViews = storyItem.views;
                if (storyViews.views_count <= 0) {
                    storyViews.views_count = 1;
                }
                AnimatedTextView.AnimatedTextDrawable animatedTextDrawable = this.repostCounter;
                if (animatedTextDrawable != null && (i = storyViews.forwards_count) > 0) {
                    animatedTextDrawable.setText(Integer.toString(i), z && this.repostCounterVisible);
                    this.repostCounterVisible = true;
                } else {
                    this.repostCounterVisible = false;
                }
                int i2 = storyItem.views.reactions_count;
                if (i2 > 0) {
                    this.reactionsCounter.setText(Integer.toString(i2), z && this.reactionsCounterVisible);
                    this.reactionsCounterVisible = true;
                } else {
                    this.reactionsCounterVisible = false;
                }
                if (!z) {
                    this.reactionsCounterProgress.set(this.reactionsCounterVisible ? 1.0f : 0.0f, true);
                    AnimatedFloat animatedFloat = this.repostCounterProgress;
                    if (animatedFloat != null) {
                        animatedFloat.set(this.repostCounterVisible ? 1.0f : 0.0f, true);
                    }
                }
                TLRPC.Chat chat = MessagesController.getInstance(this.currentAccount).getChat(Long.valueOf(-this.dialogId));
                if ((this.isGroup && (ChatObject.canSendPlain(chat) || ChatObject.isPossibleRemoveChatRestrictionsByBoosts(chat))) || storyItem.views.views_count <= 0) {
                    this.selfStatusView.setText(_UrlKt.FRAGMENT_ENCODE_SET);
                } else {
                    this.selfStatusView.setText(LocaleController.getString(this.storyViewer.storiesList == null ? R.string.NobodyViews : R.string.NobodyViewsArchived));
                    this.selfStatusView.setTranslationX(AndroidUtilities.dp(16.0f));
                    SpannableStringBuilder spannableStringBuilder = new SpannableStringBuilder();
                    spannableStringBuilder.append((CharSequence) "d  ");
                    spannableStringBuilder.setSpan(new ColoredImageSpan(R.drawable.filled_views), spannableStringBuilder.length() - 3, spannableStringBuilder.length() - 2, 0);
                    spannableStringBuilder.append((CharSequence) AndroidUtilities.formatWholeNumber(storyItem.views.views_count, 0));
                    this.selfStatusView.setText(spannableStringBuilder);
                    FrameLayout frameLayout = this.selfView;
                    if (frameLayout != null) {
                        ((FrameLayout.LayoutParams) frameLayout.getLayoutParams()).rightMargin = (int) (AndroidUtilities.dp(80.0f) + this.selfStatusView.getPaint().measureText(spannableStringBuilder.toString()));
                    }
                }
                this.likeButtonContainer.getLayoutParams().width = (int) (AndroidUtilities.dp(40.0f) + (this.reactionsCounterVisible ? this.reactionsCounter.getAnimateToWidth() + AndroidUtilities.dp(4.0f) : 0.0f));
                ((ViewGroup.MarginLayoutParams) this.selfView.getLayoutParams()).rightMargin = AndroidUtilities.dp(40.0f) + this.likeButtonContainer.getLayoutParams().width;
                FrameLayout frameLayout2 = this.repostButtonContainer;
                if (frameLayout2 != null) {
                    frameLayout2.getLayoutParams().width = (int) (AndroidUtilities.dp(40.0f) + (this.repostCounterVisible ? this.repostCounter.getAnimateToWidth() + AndroidUtilities.dp(4.0f) : 0.0f));
                    ((ViewGroup.MarginLayoutParams) this.selfView.getLayoutParams()).rightMargin += this.repostButtonContainer.getLayoutParams().width;
                    this.repostButtonContainer.requestLayout();
                }
                this.selfView.requestLayout();
                this.likeButtonContainer.requestLayout();
                this.selfAvatarsView.setVisibility(8);
                this.selfAvatarsContainer.setVisibility(8);
                this.storyAreasView.onStoryItemUpdated(this.currentStory.storyItem, z);
                return;
            }
            TL_stories.StoryViews storyViews2 = storyItem.views;
            if (storyViews2 != null && storyViews2.views_count > 0) {
                int i3 = 0;
                for (int i4 = 0; i4 < storyItem.views.recent_viewers.size(); i4++) {
                    TLObject userOrChat = MessagesController.getInstance(this.currentAccount).getUserOrChat(storyItem.views.recent_viewers.get(i4).longValue());
                    if (userOrChat != null) {
                        this.selfAvatarsView.setObject(i3, this.currentAccount, userOrChat);
                        i3++;
                    }
                    if (i3 >= 3) {
                        break;
                    }
                }
                for (int i5 = i3; i5 < 3; i5++) {
                    this.selfAvatarsView.setObject(i5, this.currentAccount, null);
                }
                this.selfAvatarsView.commitTransition(false);
                SpannableStringBuilder spannableStringBuilder2 = new SpannableStringBuilder(LocaleController.formatPluralStringComma("Views", storyItem.views.views_count));
                if (storyItem.views.reactions_count > 0) {
                    spannableStringBuilder2.append((CharSequence) "  d ");
                    ColoredImageSpan coloredImageSpan = new ColoredImageSpan(R.drawable.mini_views_likes);
                    coloredImageSpan.setOverrideColor(-53704);
                    coloredImageSpan.setTopOffset(AndroidUtilities.dp(0.2f));
                    spannableStringBuilder2.setSpan(coloredImageSpan, spannableStringBuilder2.length() - 2, spannableStringBuilder2.length() - 1, 0);
                    spannableStringBuilder2.append((CharSequence) String.valueOf(storyItem.views.reactions_count));
                }
                if (storyItem.views.forwards_count > 0) {
                    spannableStringBuilder2.append((CharSequence) "  d ");
                    ColoredImageSpan coloredImageSpan2 = new ColoredImageSpan(R.drawable.mini_repost_story);
                    coloredImageSpan2.setOverrideColor(-14161823);
                    coloredImageSpan2.setTopOffset(AndroidUtilities.dp(0.2f));
                    spannableStringBuilder2.setSpan(coloredImageSpan2, spannableStringBuilder2.length() - 2, spannableStringBuilder2.length() - 1, 0);
                    spannableStringBuilder2.append((CharSequence) String.valueOf(storyItem.views.forwards_count));
                }
                this.selfStatusView.setText(spannableStringBuilder2);
                if (i3 == 0) {
                    this.selfAvatarsView.setVisibility(8);
                    this.selfStatusView.setTranslationX(AndroidUtilities.dp(16.0f));
                } else {
                    this.selfAvatarsView.setVisibility(0);
                    this.selfStatusView.setTranslationX(AndroidUtilities.dp(13.0f) + AndroidUtilities.dp(24.0f) + (AndroidUtilities.dp(20.0f) * (i3 - 1)) + AndroidUtilities.dp(10.0f));
                }
                this.selfAvatarsContainer.setVisibility(0);
            } else {
                this.selfStatusView.setText(LocaleController.getString(this.storyViewer.storiesList == null ? R.string.NobodyViews : R.string.NobodyViewsArchived));
                this.selfStatusView.setTranslationX(AndroidUtilities.dp(16.0f));
                this.selfAvatarsView.setVisibility(8);
                this.selfAvatarsContainer.setVisibility(8);
            }
            this.likeButtonContainer.getLayoutParams().width = AndroidUtilities.dp(40.0f);
            this.bottomActionsLinearLayout.requestLayout();
        }
    }

    private void requestVideoPlayer(long j) {
        TLRPC.Document document;
        Uri uri;
        TLRPC.Document document2;
        if (this.isActive) {
            if (this.currentStory.isLive()) {
                Delegate delegate = this.delegate;
                TL_stories.StoryItem storyItem = this.currentStory.storyItem;
                long j2 = this.dialogId;
                int i = storyItem.id;
                TLRPC.MessageMedia messageMedia = storyItem.media;
                delegate.requestPlayer(storyItem, j2, i, ((TLRPC.TL_messageMediaVideoStream) messageMedia).rtmp_stream, ((TLRPC.TL_messageMediaVideoStream) messageMedia).call, this.playerSharedScope);
                this.storyContainer.invalidate();
                return;
            }
            if (this.currentStory.isVideo()) {
                if (this.currentStory.getLocalPath() != null && new File(this.currentStory.getLocalPath()).exists()) {
                    Uri uriFromFile = Uri.fromFile(new File(this.currentStory.getLocalPath()));
                    FileLog.d("StoryViewer requestVideoPlayer(" + j + "): playing from attachPath " + uriFromFile);
                    this.videoDuration = 0L;
                    uri = uriFromFile;
                    document = null;
                } else {
                    TL_stories.StoryItem storyItem2 = this.currentStory.storyItem;
                    if (storyItem2 != null) {
                        storyItem2.dialogId = this.dialogId;
                        try {
                            document2 = storyItem2.media.getDocument();
                            try {
                                TL_stories.StoryItem storyItem3 = this.currentStory.storyItem;
                                if (storyItem3.fileReference == 0) {
                                    storyItem3.fileReference = FileLoader.getInstance(this.currentAccount).getFileReference(this.currentStory.storyItem);
                                }
                                StringBuilder sb = new StringBuilder();
                                sb.append("?account=");
                                sb.append(this.currentAccount);
                                sb.append("&id=");
                                sb.append(document2.id);
                                sb.append("&hash=");
                                sb.append(document2.access_hash);
                                sb.append("&dc=");
                                sb.append(document2.dc_id);
                                sb.append("&size=");
                                sb.append(document2.size);
                                sb.append("&mime=");
                                sb.append(URLEncoder.encode(document2.mime_type, "UTF-8"));
                                sb.append("&rid=");
                                sb.append(this.currentStory.storyItem.fileReference);
                                sb.append("&name=");
                                sb.append(URLEncoder.encode(FileLoader.getDocumentFileName(document2), "UTF-8"));
                                sb.append("&reference=");
                                byte[] bArr = document2.file_reference;
                                if (bArr == null) {
                                    bArr = new byte[0];
                                }
                                sb.append(Utilities.bytesToHex(bArr));
                                sb.append("&sid=");
                                sb.append(this.currentStory.storyItem.id);
                                sb.append("&did=");
                                sb.append(this.currentStory.storyItem.dialogId);
                                Uri uri2 = Uri.parse("tg://" + FileLoader.getAttachFileName(document2) + sb.toString());
                                FileLog.d("StoryViewer requestVideoPlayer(" + j + "): playing from " + uri2);
                                this.videoDuration = (long) (MessageObject.getDocumentDuration(document2) * 1000.0d);
                                uri = uri2;
                                document = document2;
                            } catch (Exception unused) {
                                document = document2;
                                uri = null;
                            }
                        } catch (Exception unused2) {
                            document2 = null;
                        }
                    } else {
                        document = null;
                        uri = null;
                    }
                }
                if (uri == null) {
                    FileLog.d("PeerStoriesView.requestVideoPlayer(" + j + "): playing from null?");
                }
                this.delegate.requestPlayer(document, uri, j, this.playerSharedScope);
                this.storyContainer.invalidate();
                return;
            }
            FileLog.d("PeerStoriesView.requestVideoPlayer(" + j + "): null, not a video");
            this.delegate.requestPlayer(null, null, 0L, this.playerSharedScope);
            VideoPlayerSharedScope videoPlayerSharedScope = this.playerSharedScope;
            videoPlayerSharedScope.renderView = null;
            videoPlayerSharedScope.firstFrameRendered = false;
            return;
        }
        this.playerSharedScope.renderView = null;
    }

    public boolean switchToNext(boolean z) {
        if (this.storyViewer.reversed) {
            z = !z;
        }
        if (z) {
            if (this.selectedPosition >= getStoriesCount() - 1) {
                return false;
            }
            this.selectedPosition++;
            updatePosition();
            return true;
        }
        int i = this.selectedPosition;
        if (i <= 0) {
            return false;
        }
        this.selectedPosition = i - 1;
        updatePosition();
        return true;
    }

    public void setDelegate(Delegate delegate) {
        this.delegate = delegate;
    }

    public void drawPlayingBitmap(int i, int i2, Canvas canvas) {
        TextureView textureView;
        VideoPlayerSharedScope videoPlayerSharedScope = this.playerSharedScope;
        View view = videoPlayerSharedScope.renderView;
        if (view != null && videoPlayerSharedScope.surfaceView != null) {
            Bitmap bitmapCreateBitmap = Bitmap.createBitmap(i, i2, Bitmap.Config.ARGB_8888);
            if (Build.VERSION.SDK_INT >= 24) {
                AndroidUtilities.getBitmapFromSurface(this.playerSharedScope.surfaceView, bitmapCreateBitmap);
            }
            if (bitmapCreateBitmap != null) {
                canvas.drawBitmap(bitmapCreateBitmap, 0.0f, 0.0f, (Paint) null);
                return;
            }
            return;
        }
        if (view != null && (textureView = videoPlayerSharedScope.textureView) != null) {
            Bitmap bitmap = textureView.getBitmap(i, i2);
            if (bitmap != null) {
                canvas.drawBitmap(bitmap, 0.0f, 0.0f, (Paint) null);
                return;
            }
            return;
        }
        canvas.save();
        canvas.scale(i / this.storyContainer.getMeasuredWidth(), i2 / this.storyContainer.getMeasuredHeight());
        this.imageReceiver.draw(canvas);
        canvas.restore();
    }

    public Bitmap getPlayingBitmap() {
        Bitmap bitmapCreateBitmap = Bitmap.createBitmap(this.storyContainer.getWidth(), this.storyContainer.getHeight(), Bitmap.Config.ARGB_8888);
        drawPlayingBitmap(bitmapCreateBitmap.getWidth(), bitmapCreateBitmap.getHeight(), new Canvas(bitmapCreateBitmap));
        return bitmapCreateBitmap;
    }

    public void createBlurredBitmap(Canvas canvas, Bitmap bitmap) {
        drawPlayingBitmap(bitmap.getWidth(), bitmap.getHeight(), canvas);
        if (AndroidUtilities.computePerceivedBrightness(AndroidUtilities.getDominantColor(bitmap)) < 0.15f) {
            canvas.drawColor(ColorUtils.setAlphaComponent(-1, 102));
        }
        Utilities.blurBitmap(bitmap, 3, 1, bitmap.getWidth(), bitmap.getHeight(), bitmap.getRowBytes());
        Utilities.blurBitmap(bitmap, 3, 1, bitmap.getWidth(), bitmap.getHeight(), bitmap.getRowBytes());
    }

    public void stopPlaying(boolean z) {
        if (z) {
            this.imageReceiver.stopAnimation();
            this.imageReceiver.setAllowStartAnimation(false);
        } else {
            this.imageReceiver.startAnimation();
            this.imageReceiver.setAllowStartAnimation(true);
        }
    }

    public long getCurrentPeer() {
        return this.dialogId;
    }

    public ArrayList<Integer> getCurrentDay() {
        return this.day;
    }

    public void setPaused(boolean z) {
        if (this.paused != z) {
            this.paused = z;
            stopPlaying(z);
            this.lastDrawTime = 0L;
            this.storyContainer.invalidate();
        }
    }

    public int getSelectedPosition() {
        return this.selectedPosition;
    }

    public boolean closeKeyboardOrEmoji() {
        if (this.likesReactionShowing) {
            if (this.likesReactionLayout.getReactionsWindow() != null) {
                if (this.realKeyboardHeight > 0) {
                    AndroidUtilities.hideKeyboard(this.likesReactionLayout.getReactionsWindow().windowView);
                } else {
                    this.likesReactionLayout.getReactionsWindow().dismiss();
                }
                return true;
            }
            showLikesReaction(false);
            return true;
        }
        StoryMediaAreasView storyMediaAreasView = this.storyAreasView;
        if (storyMediaAreasView != null) {
            storyMediaAreasView.closeHint();
        }
        if (this.storyCaptionView.textSelectionHelper.isInSelectionMode()) {
            this.storyCaptionView.textSelectionHelper.clear(false);
            return true;
        }
        HintView2 hintView2 = this.privacyHint;
        if (hintView2 != null) {
            hintView2.hide();
        }
        HintView2 hintView3 = this.soundTooltip;
        if (hintView3 != null) {
            hintView3.hide();
        }
        HintView hintView = this.mediaBanTooltip;
        if (hintView != null) {
            hintView.hide(true);
        }
        CaptionContainerView captionContainerView = this.storyEditCaptionView;
        if (captionContainerView != null && captionContainerView.onBackPressed()) {
            return true;
        }
        CustomPopupMenu customPopupMenu = this.popupMenu;
        if (customPopupMenu != null && customPopupMenu.isShowing()) {
            this.popupMenu.dismiss();
            return true;
        }
        if (checkRecordLocked(false)) {
            return true;
        }
        ReactionsContainerLayout reactionsContainerLayout = this.reactionsContainerLayout;
        if (reactionsContainerLayout != null && reactionsContainerLayout.getReactionsWindow() != null && this.reactionsContainerLayout.getReactionsWindow().isShowing()) {
            this.reactionsContainerLayout.getReactionsWindow().dismiss();
            return true;
        }
        ChatActivityEnterView chatActivityEnterView = this.chatActivityEnterView;
        if (chatActivityEnterView != null && chatActivityEnterView.isPopupShowing()) {
            if (this.realKeyboardHeight > 0) {
                AndroidUtilities.hideKeyboard(this.chatActivityEnterView.getEmojiView());
            } else {
                this.chatActivityEnterView.hidePopup(true, false);
            }
            return true;
        }
        if (getKeyboardHeight() >= AndroidUtilities.dp(20.0f)) {
            ChatActivityEnterView chatActivityEnterView2 = this.chatActivityEnterView;
            if (chatActivityEnterView2 != null) {
                this.storyViewer.saveDraft(this.dialogId, this.currentStory.storyItem, chatActivityEnterView2.getEditText());
            }
            AndroidUtilities.hideKeyboard(this.chatActivityEnterView);
            return true;
        }
        if (this.storyCaptionView.getVisibility() != 0 || this.storyCaptionView.getProgressToBlackout() <= 0.0f) {
            return false;
        }
        this.storyCaptionView.collapse();
        this.inBlackoutMode = false;
        this.storyContainer.invalidate();
        return true;
    }

    public boolean findClickableView(ViewGroup viewGroup, float f, float f2, boolean z) {
        ChatActivityEnterView chatActivityEnterView;
        VideoPlayerSharedScope videoPlayerSharedScope;
        if (viewGroup == null) {
            return false;
        }
        HintView2 hintView2 = this.privacyHint;
        if (hintView2 != null && hintView2.shown()) {
            return true;
        }
        HintView2 hintView3 = this.soundTooltip;
        if (hintView3 != null && hintView3.shown()) {
            return true;
        }
        for (int i = 0; i < viewGroup.getChildCount(); i++) {
            View childAt = viewGroup.getChildAt(i);
            if (childAt.getVisibility() == 0) {
                if (childAt == this.storyCaptionView) {
                    Rect rect = AndroidUtilities.rectTmp2;
                    childAt.getHitRect(rect);
                    if (rect.contains((int) f, (int) f2) && this.storyCaptionView.allowInterceptTouchEvent(f, f2 - childAt.getTop())) {
                        return true;
                    }
                }
                Rect rect2 = AndroidUtilities.rectTmp2;
                childAt.getHitRect(rect2);
                if (childAt == this.storyContainer && (videoPlayerSharedScope = this.playerSharedScope) != null) {
                    View view = videoPlayerSharedScope.renderView;
                    if ((view instanceof ViewGroup) && findClickableView((ViewGroup) view, f - childAt.getX(), f2 - childAt.getY(), z)) {
                        return true;
                    }
                }
                if (childAt.isClickable() && rect2.contains((int) f, (int) f2)) {
                    return true;
                }
                StoryMediaAreasView storyMediaAreasView = this.storyAreasView;
                if (childAt == storyMediaAreasView && !storyMediaAreasView.hasSelected() && (f < AndroidUtilities.dp(60.0f) || f > viewGroup.getMeasuredWidth() - AndroidUtilities.dp(60.0f))) {
                    if (this.storyAreasView.hasClickableViews(f, f2)) {
                        return true;
                    }
                } else {
                    LiveCommentsView liveCommentsView = this.liveCommentsView;
                    if (childAt == liveCommentsView) {
                        liveCommentsView.topListView.getHitRect(rect2);
                        if (rect2.contains((int) ((f - this.liveCommentsView.getX()) - this.liveCommentsView.topListView.getX()), (int) ((f2 - this.liveCommentsView.getY()) - this.liveCommentsView.topListView.getY()))) {
                            return true;
                        }
                        if (!this.liveCommentsView.isCollapsed()) {
                            if (!this.keyboardVisible && f2 <= this.liveCommentsView.getY() + this.liveCommentsView.top()) {
                                LiveCommentsView liveCommentsView2 = this.liveCommentsView;
                                if (liveCommentsView2.listView.findChildViewUnder(f, (f2 - liveCommentsView2.getY()) - this.liveCommentsView.listView.getY()) != null) {
                                }
                            }
                            return true;
                        }
                        continue;
                    } else {
                        if (this.keyboardVisible && childAt == this.chatActivityEnterView && f2 > rect2.top) {
                            return true;
                        }
                        if (!z && rect2.contains((int) f, (int) f2) && (((childAt.isClickable() || childAt == this.reactionsContainerLayout) && childAt.isEnabled()) || ((chatActivityEnterView = this.chatActivityEnterView) != null && childAt == chatActivityEnterView.getRecordCircle()))) {
                            return true;
                        }
                        if (childAt.isEnabled() && (childAt instanceof ViewGroup) && findClickableView((ViewGroup) childAt, f - childAt.getX(), f2 - childAt.getY(), z)) {
                            return true;
                        }
                    }
                }
            }
        }
        return false;
    }

    public void setAccount(int i) {
        this.currentAccount = i;
        this.storiesController = MessagesController.getInstance(i).storiesController;
        this.emojiAnimationsOverlay.setAccount(i);
        ReactionsContainerLayout reactionsContainerLayout = this.reactionsContainerLayout;
        if (reactionsContainerLayout != null) {
            reactionsContainerLayout.setCurrentAccount(i);
            this.reactionsContainerLayout.setMessage(null, null, true);
        }
        ReactionsContainerLayout reactionsContainerLayout2 = this.likesReactionLayout;
        if (reactionsContainerLayout2 != null) {
            reactionsContainerLayout2.setCurrentAccount(i);
        }
    }

    public void setActive(boolean z) {
        setActive(0L, z);
    }

    public void setActive(long j, boolean z) {
        if (this.isActive != z) {
            activeCount += z ? 1 : -1;
            this.isActive = z;
            if (z) {
                if (useSurfaceInViewPagerWorkAround()) {
                    this.delegate.setIsSwiping(true);
                    AndroidUtilities.cancelRunOnUIThread(this.allowDrawSurfaceRunnable);
                    AndroidUtilities.runOnUIThread(this.allowDrawSurfaceRunnable, 100L);
                }
                requestVideoPlayer(j);
                updatePreloadImages();
                this.muteIconView.setAnimation(this.sharedResources.muteDrawable);
                this.isActive = true;
                this.headerView.backupImageView.getImageReceiver().setVisible(true, true);
                if (this.currentStory.storyItem != null) {
                    FileLog.d("StoryViewer displayed story dialogId=" + this.dialogId + " storyId=" + this.currentStory.storyItem.id + " " + this.currentStory.getMediaDebugString());
                }
            } else {
                cancelTextSelection();
                this.muteIconView.clearAnimationDrawable();
                this.viewsThumbImageReceiver = null;
                this.isLongPressed = false;
                this.progressToHideInterface.set(0.0f, true);
                this.storyContainer.invalidate();
                invalidate();
                cancelWaiting();
                this.delegate.setIsRecording(false);
            }
            this.imageReceiver.setFileLoadingPriority(this.isActive ? 3 : 2);
            this.leftPreloadImageReceiver.setFileLoadingPriority(this.isActive ? 2 : 0);
            this.rightPreloadImageReceiver.setFileLoadingPriority(this.isActive ? 2 : 0);
            if (this.isSelf || this.isChannel) {
                this.storiesController.pollViewsForSelfStories(this.dialogId, this.isActive);
            }
        }
    }

    public void progressToDismissUpdated() {
        if (this.BIG_SCREEN) {
            invalidate();
        }
    }

    public void reset() {
        this.headerView.backupImageView.getImageReceiver().setVisible(true, true);
        if (this.changeBoundAnimator != null) {
            this.chatActivityEnterView.reset();
            this.chatActivityEnterView.setAlpha(1.0f - this.outT);
        }
        ReactionsContainerLayout reactionsContainerLayout = this.reactionsContainerLayout;
        if (reactionsContainerLayout != null) {
            reactionsContainerLayout.reset();
        }
        ReactionsContainerLayout reactionsContainerLayout2 = this.likesReactionLayout;
        if (reactionsContainerLayout2 != null) {
            reactionsContainerLayout2.reset();
        }
        InstantCameraView instantCameraView = this.instantCameraView;
        if (instantCameraView != null) {
            AndroidUtilities.removeFromParent(instantCameraView);
            this.instantCameraView.hideCamera(true);
            this.instantCameraView = null;
        }
        setActive(false);
        setIsVisible(false);
        this.isLongPressed = false;
        this.progressToHideInterface.set(0.0f, false);
        this.viewsThumbImageReceiver = null;
        this.messageSent = false;
        cancelTextSelection();
    }

    public void onActivityResult(int i, int i2, Intent intent) {
        if (i2 == -1) {
            if (i == 0 || i == 2) {
                createChatAttachView();
                ChatAttachAlert chatAttachAlert = this.chatAttachAlert;
                if (chatAttachAlert != null) {
                    chatAttachAlert.getPhotoLayout().onActivityResultFragment(i, intent, null);
                    return;
                }
                return;
            }
            if (i == 21) {
                if (intent == null) {
                    showAttachmentError();
                    return;
                }
                if (intent.getData() != null) {
                    sendUriAsDocument(intent.getData());
                } else if (intent.getClipData() != null) {
                    ClipData clipData = intent.getClipData();
                    for (int i3 = 0; i3 < clipData.getItemCount(); i3++) {
                        sendUriAsDocument(clipData.getItemAt(i3).getUri());
                    }
                } else {
                    showAttachmentError();
                }
                ChatAttachAlert chatAttachAlert2 = this.chatAttachAlert;
                if (chatAttachAlert2 != null) {
                    chatAttachAlert2.dismiss();
                }
                afterMessageSend(true);
            }
        }
    }

    private void sendUriAsDocument(Uri uri) {
        TL_stories.StoryItem storyItem;
        Uri uri2;
        String str;
        boolean z;
        String str2;
        if (uri == null || (storyItem = this.currentStory.storyItem) == null || (storyItem instanceof TL_stories.TL_storyItemSkipped)) {
            return;
        }
        String string = uri.toString();
        if (string.contains("com.google.android.apps.photos.contentprovider")) {
            try {
                String str3 = string.split("/1/")[1];
                int iIndexOf = str3.indexOf("/ACTUAL");
                uri2 = iIndexOf != -1 ? Uri.parse(URLDecoder.decode(str3.substring(0, iIndexOf), "UTF-8")) : uri;
            } catch (Exception e) {
                FileLog.e(e);
                uri2 = uri;
            }
        } else {
            uri2 = uri;
        }
        String path = AndroidUtilities.getPath(uri2);
        if (!BuildVars.NO_SCOPED_STORAGE) {
            str2 = path;
            z = true;
            str = str2;
        } else if (path == null) {
            String string2 = uri2.toString();
            String strCopyFileToCache = MediaController.copyFileToCache(uri2, "file");
            if (strCopyFileToCache == null) {
                showAttachmentError();
                return;
            } else {
                str = string2;
                z = false;
                str2 = strCopyFileToCache;
            }
        } else {
            str = path;
            z = false;
            str2 = str;
        }
        if (z) {
            SendMessagesHelper.prepareSendingDocument(getAccountInstance(), null, null, uri2, null, null, this.dialogId, null, null, storyItem, null, null, true, 0, null, null, 0, false);
        } else {
            SendMessagesHelper.prepareSendingDocument(getAccountInstance(), str2, str, null, null, null, this.dialogId, null, null, storyItem, null, null, true, 0, null, null, 0, false);
        }
    }

    private void showAttachmentError() {
        BulletinFactory.of(this.storyContainer, this.resourcesProvider).createErrorBulletin(LocaleController.getString(R.string.UnsupportedAttachment), this.resourcesProvider).show();
    }

    public void setLongpressed(boolean z) {
        if (this.isActive) {
            this.isLongPressed = z;
            invalidate();
        }
    }

    public boolean showKeyboard() {
        TextView textView;
        EditTextCaption editField;
        if (this.chatActivityEnterView == null || (((textView = this.replyDisabledTextView) != null && textView.getVisibility() == 0) || (editField = this.chatActivityEnterView.getEditField()) == null)) {
            return false;
        }
        editField.requestFocus();
        AndroidUtilities.showKeyboard(editField);
        return true;
    }

    public void checkPinchToZoom(MotionEvent motionEvent) {
        this.pinchToZoomHelper.checkPinchToZoom(motionEvent, this.storyContainer, null, null, null, null);
    }

    public void setIsVisible(boolean z) {
        if (this.isVisible == z) {
            return;
        }
        this.isVisible = z;
        if (z) {
            this.imageReceiver.setCurrentAlpha(1.0f);
            checkStealthMode(false);
        }
    }

    public ArrayList<TL_stories.StoryItem> getStoryItems() {
        return this.storyItems;
    }

    public void selectPosition(int i) {
        if (this.selectedPosition != i) {
            this.selectedPosition = i;
            updatePosition();
        }
    }

    public void cancelTouch() {
        this.storyCaptionView.cancelTouch();
    }

    public void onActionDown(MotionEvent motionEvent) {
        HintView2 hintView2 = this.privacyHint;
        if (hintView2 != null && hintView2.shown() && this.privacyButton != null && !this.privacyHint.containsTouch(motionEvent, getX() + this.storyContainer.getX() + this.privacyHint.getX(), getY() + this.storyContainer.getY() + this.privacyHint.getY()) && !hitButton(this.privacyButton, motionEvent)) {
            this.privacyHint.hide();
        }
        HintView2 hintView3 = this.soundTooltip;
        if (hintView3 == null || !hintView3.shown() || this.muteIconContainer == null || this.soundTooltip.containsTouch(motionEvent, getX() + this.storyContainer.getX() + this.soundTooltip.getX(), getY() + this.storyContainer.getY() + this.soundTooltip.getY()) || hitButton(this.muteIconContainer, motionEvent)) {
            return;
        }
        this.soundTooltip.hide();
    }

    private boolean hitButton(View view, MotionEvent motionEvent) {
        float x = getX() + this.storyContainer.getX() + view.getX();
        float y = getY() + this.storyContainer.getY() + view.getY();
        return motionEvent.getX() >= x && motionEvent.getX() <= x + ((float) view.getWidth()) && motionEvent.getY() >= y && motionEvent.getY() <= y + ((float) view.getHeight());
    }

    public void setOffset(float f) {
        boolean z = f == 0.0f;
        if (this.allowDrawSurface != z) {
            this.allowDrawSurface = z;
            this.storyContainer.invalidate();
            if (this.isActive && useSurfaceInViewPagerWorkAround()) {
                if (z) {
                    AndroidUtilities.cancelRunOnUIThread(this.allowDrawSurfaceRunnable);
                    AndroidUtilities.runOnUIThread(this.allowDrawSurfaceRunnable, 250L);
                } else {
                    AndroidUtilities.cancelRunOnUIThread(this.allowDrawSurfaceRunnable);
                    this.delegate.setIsSwiping(true);
                }
            }
        }
    }

    public boolean useSurfaceInViewPagerWorkAround() {
        return this.storyViewer.USE_SURFACE_VIEW && Build.VERSION.SDK_INT < 33;
    }

    public void showNoSoundHint(boolean z) {
        if (this.soundTooltip == null) {
            HintView2 joint = new HintView2(getContext(), 1).setJoint(1.0f, -56.0f);
            this.soundTooltip = joint;
            joint.setPadding(AndroidUtilities.dp(8.0f), 0, AndroidUtilities.dp(8.0f), 0);
            this.storyContainer.addView(this.soundTooltip, LayoutHelper.createFrame(-1, -2.0f, 55, 0.0f, 52.0f, 0.0f, 0.0f));
        }
        this.soundTooltip.setText(LocaleController.getString(z ? R.string.StoryNoSound : R.string.StoryTapToSound));
        this.soundTooltip.show();
    }

    public boolean checkTextSelectionEvent(MotionEvent motionEvent) {
        if (!this.storyCaptionView.textSelectionHelper.isInSelectionMode()) {
            return false;
        }
        float x = getX();
        float y = getY() + ((View) getParent()).getY();
        motionEvent.offsetLocation(-x, -y);
        if (this.storyCaptionView.textSelectionHelper.getOverlayView(getContext()).onTouchEvent(motionEvent)) {
            return true;
        }
        motionEvent.offsetLocation(x, y);
        return false;
    }

    public void cancelTextSelection() {
        if (this.storyCaptionView.textSelectionHelper.isInSelectionMode()) {
            this.storyCaptionView.textSelectionHelper.clear();
        }
    }

    public boolean checkReactionEvent(MotionEvent motionEvent) {
        ReactionsContainerLayout reactionsContainerLayout = this.likesReactionLayout;
        if (reactionsContainerLayout == null) {
            return false;
        }
        float x = 0.0f;
        float y = 0.0f;
        for (View view = this; view != null && (view.getParent() instanceof View); view = (View) view.getParent()) {
            x += view.getX();
            y += view.getY();
        }
        if (this.likesReactionLayout.getReactionsWindow() != null && this.likesReactionLayout.getReactionsWindow().windowView != null) {
            motionEvent.offsetLocation(-x, (-y) - this.likesReactionLayout.getReactionsWindow().windowView.getTranslationY());
            this.likesReactionLayout.getReactionsWindow().windowView.dispatchTouchEvent(motionEvent);
            return true;
        }
        Rect rect = AndroidUtilities.rectTmp2;
        reactionsContainerLayout.getHitRect(rect);
        rect.offset((int) x, (int) y);
        if (motionEvent.getAction() == 0 && !rect.contains((int) motionEvent.getX(), (int) motionEvent.getY())) {
            showLikesReaction(false);
            return true;
        }
        motionEvent.offsetLocation(-rect.left, -rect.top);
        reactionsContainerLayout.dispatchTouchEvent(motionEvent);
        return true;
    }

    public boolean viewsAllowed() {
        if (this.currentStory.isLive) {
            return false;
        }
        if (this.isSelf) {
            return true;
        }
        return this.isChannel && this.userCanSeeViews;
    }

    public static class PeerHeaderView extends FrameLayout {
        public BackupImageView backupImageView;
        private float progressToUploading;
        RadialProgress radialProgress;
        Paint radialProgressPaint;
        StoryItemHolder storyItemHolder;
        private ValueAnimator subtitleAnimator;
        private TextView[] subtitleView;
        public SimpleTextView titleView;
        private boolean uploadedTooFast;
        private boolean uploading;

        public PeerHeaderView(Context context, StoryItemHolder storyItemHolder) {
            super(context);
            this.subtitleView = new TextView[2];
            this.storyItemHolder = storyItemHolder;
            BackupImageView backupImageView = new BackupImageView(context) { // from class: org.telegram.ui.Stories.PeerStoriesView.PeerHeaderView.1
                @Override // org.telegram.ui.Components.BackupImageView, android.view.View
                protected void onDraw(Canvas canvas) {
                    if (this.imageReceiver.getVisible()) {
                        RectF rectF = AndroidUtilities.rectTmp;
                        rectF.set(0.0f, 0.0f, getMeasuredWidth(), getMeasuredHeight());
                        PeerHeaderView.this.drawUploadingProgress(canvas, rectF, true, 1.0f);
                    }
                    super.onDraw(canvas);
                }
            };
            this.backupImageView = backupImageView;
            backupImageView.setRoundRadius(ExteraConfig.getAvatarCorners(32.0f));
            addView(this.backupImageView, LayoutHelper.createFrame(32, 32.0f, 0, 12.0f, 2.0f, 0.0f, 0.0f));
            setClipChildren(false);
            SimpleTextView simpleTextView = new SimpleTextView(context) { // from class: org.telegram.ui.Stories.PeerStoriesView.PeerHeaderView.2
                @Override // org.telegram.ui.ActionBar.SimpleTextView, android.view.View
                protected void onMeasure(int i, int i2) {
                    super.onMeasure(i, i2);
                    setPivotY(getMeasuredHeight() / 2.0f);
                }
            };
            this.titleView = simpleTextView;
            simpleTextView.setTextSize(14);
            this.titleView.setTypeface(AndroidUtilities.bold());
            this.titleView.setMaxLines(1);
            this.titleView.setEllipsizeByGradient(AndroidUtilities.dp(4.0f));
            this.titleView.setPivotX(0.0f);
            NotificationCenter.listenEmojiLoading(this.titleView);
            addView(this.titleView, LayoutHelper.createFrame(-2, -2.0f, 0, 54.0f, 0.0f, 86.0f, 0.0f));
            for (int i = 0; i < 2; i++) {
                this.subtitleView[i] = new TextView(context);
                this.subtitleView[i].setTextSize(1, 12.0f);
                this.subtitleView[i].setMaxLines(1);
                this.subtitleView[i].setSingleLine(true);
                this.subtitleView[i].setEllipsize(TextUtils.TruncateAt.MIDDLE);
                this.subtitleView[i].setTextColor(-1);
                this.subtitleView[i].setPadding(AndroidUtilities.dp(3.0f), 0, AndroidUtilities.dp(3.0f), AndroidUtilities.dp(1.0f));
                addView(this.subtitleView[i], LayoutHelper.createFrame(-2, -2.0f, 0, 51.0f, 18.0f, 83.0f, 0.0f));
            }
            this.titleView.setTextColor(-1);
        }

        public void setSubtitle(CharSequence charSequence) {
            setSubtitle(charSequence, false);
        }

        public void setOnSubtitleClick(View.OnClickListener onClickListener) {
            this.subtitleView[0].setOnClickListener(onClickListener);
            this.subtitleView[0].setClickable(onClickListener != null);
            this.subtitleView[0].setBackground(onClickListener == null ? null : Theme.createSelectorDrawable(822083583, 7));
        }

        public void setSubtitle(CharSequence charSequence, boolean z) {
            ValueAnimator valueAnimator = this.subtitleAnimator;
            if (valueAnimator != null) {
                valueAnimator.cancel();
                this.subtitleAnimator = null;
            }
            if (z) {
                this.subtitleView[1].setOnClickListener(null);
                TextView[] textViewArr = this.subtitleView;
                textViewArr[1].setText(textViewArr[0].getText());
                this.subtitleView[1].setVisibility(0);
                this.subtitleView[1].setAlpha(1.0f);
                this.subtitleView[1].setTranslationY(0.0f);
                this.subtitleView[0].setText(charSequence);
                this.subtitleView[0].setVisibility(0);
                this.subtitleView[0].setAlpha(0.0f);
                this.subtitleView[0].setTranslationY(-AndroidUtilities.dp(4.0f));
                ValueAnimator valueAnimatorOfFloat = ValueAnimator.ofFloat(0.0f, 1.0f);
                this.subtitleAnimator = valueAnimatorOfFloat;
                valueAnimatorOfFloat.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() { // from class: org.telegram.ui.Stories.PeerStoriesView$PeerHeaderView$$ExternalSyntheticLambda0
                    @Override // android.animation.ValueAnimator.AnimatorUpdateListener
                    public final void onAnimationUpdate(ValueAnimator valueAnimator2) {
                        this.f$0.lambda$setSubtitle$0(valueAnimator2);
                    }
                });
                this.subtitleAnimator.addListener(new AnimatorListenerAdapter() { // from class: org.telegram.ui.Stories.PeerStoriesView.PeerHeaderView.3
                    @Override // android.animation.AnimatorListenerAdapter, android.animation.Animator.AnimatorListener
                    public void onAnimationEnd(Animator animator) {
                        PeerHeaderView.this.subtitleView[1].setVisibility(8);
                        PeerHeaderView.this.subtitleView[0].setAlpha(1.0f);
                        PeerHeaderView.this.subtitleView[0].setTranslationY(0.0f);
                    }
                });
                this.subtitleAnimator.setInterpolator(CubicBezierInterpolator.EASE_OUT_QUINT);
                this.subtitleAnimator.setDuration(340L);
                this.subtitleAnimator.start();
                return;
            }
            this.subtitleView[0].setVisibility(0);
            this.subtitleView[0].setAlpha(1.0f);
            this.subtitleView[0].setText(charSequence);
            this.subtitleView[1].setVisibility(8);
            this.subtitleView[1].setAlpha(0.0f);
        }

        /* JADX INFO: Access modifiers changed from: private */
        public /* synthetic */ void lambda$setSubtitle$0(ValueAnimator valueAnimator) {
            float fFloatValue = ((Float) valueAnimator.getAnimatedValue()).floatValue();
            this.subtitleView[0].setAlpha(fFloatValue);
            float f = 1.0f - fFloatValue;
            this.subtitleView[0].setTranslationY((-AndroidUtilities.dp(4.0f)) * f);
            this.subtitleView[1].setAlpha(f);
            this.subtitleView[1].setTranslationY(fFloatValue * AndroidUtilities.dp(4.0f));
        }

        @Override // android.view.ViewGroup, android.view.View
        public boolean dispatchTouchEvent(MotionEvent motionEvent) {
            if (isEnabled()) {
                return super.dispatchTouchEvent(motionEvent);
            }
            return false;
        }

        public void drawUploadingProgress(Canvas canvas, RectF rectF, boolean z, float f) {
            boolean z2;
            float f2;
            StoriesController.UploadingStory uploadingStory;
            StoryItemHolder storyItemHolder = this.storyItemHolder;
            if ((storyItemHolder == null || storyItemHolder.uploadingStory == null) && this.progressToUploading == 0.0f) {
                return;
            }
            if (storyItemHolder != null && (uploadingStory = storyItemHolder.uploadingStory) != null && !uploadingStory.failed) {
                this.progressToUploading = 1.0f;
                f2 = uploadingStory.progress;
                if (!this.uploading) {
                    this.uploading = true;
                }
                z2 = false;
            } else {
                if (this.uploading) {
                    this.uploading = false;
                    this.uploadedTooFast = this.radialProgress.getAnimatedProgress() < 0.2f;
                }
                if (!this.uploadedTooFast) {
                    this.progressToUploading = Utilities.clamp(this.progressToUploading - ((1000.0f / AndroidUtilities.screenRefreshRate) / 300.0f), 1.0f, 0.0f);
                }
                z2 = true;
                f2 = 1.0f;
            }
            if (this.radialProgress == null) {
                RadialProgress radialProgress = new RadialProgress(this.backupImageView);
                this.radialProgress = radialProgress;
                radialProgress.setBackground(null, true, false);
            }
            this.radialProgress.setDiff(0);
            ImageReceiver imageReceiver = this.backupImageView.getImageReceiver();
            float fDp = AndroidUtilities.dp(3.0f) - (AndroidUtilities.dp(6.0f) * (1.0f - this.progressToUploading));
            this.radialProgress.setProgressRect((int) (rectF.left - fDp), (int) (rectF.top - fDp), (int) (rectF.right + fDp), (int) (rectF.bottom + fDp));
            this.radialProgress.setProgress(z2 ? 1.0f : Utilities.clamp(f2, 1.0f, 0.0f), true);
            if (this.uploadedTooFast && z2 && this.radialProgress.getAnimatedProgress() >= 0.9f) {
                this.progressToUploading = Utilities.clamp(this.progressToUploading - ((1000.0f / AndroidUtilities.screenRefreshRate) / 300.0f), 1.0f, 0.0f);
            }
            if (z) {
                if (f != 1.0f) {
                    Paint unreadCirclePaint = StoriesUtilities.getUnreadCirclePaint(imageReceiver, false);
                    unreadCirclePaint.setAlpha((int) (this.progressToUploading * 255.0f));
                    this.radialProgress.setPaint(unreadCirclePaint);
                    this.radialProgress.draw(canvas);
                }
                if (this.radialProgressPaint == null) {
                    Paint paint = new Paint(1);
                    this.radialProgressPaint = paint;
                    paint.setColor(-1);
                    this.radialProgressPaint.setStrokeWidth(AndroidUtilities.dp(2.0f));
                    this.radialProgressPaint.setStyle(Paint.Style.STROKE);
                    this.radialProgressPaint.setStrokeCap(Paint.Cap.ROUND);
                }
                this.radialProgressPaint.setAlpha((int) (255.0f * f * this.progressToUploading));
                this.radialProgress.setPaint(this.radialProgressPaint);
                this.radialProgress.draw(canvas);
            }
        }
    }

    public int getStoriesCount() {
        return this.uploadingStories.size() + Math.max(this.totalStoriesCount, this.storyItems.size());
    }

    public class StoryItemHolder {
        public CharSequence caption;
        public boolean captionTranslated;
        public TL_stories.StoryItem editingSourceItem;
        private boolean isLive;
        private boolean isVideo;
        private StoryCaptionView.Reply reply;
        boolean skipped;
        public TL_stories.StoryItem storyItem = null;
        public StoriesController.UploadingStory uploadingStory = null;

        public StoryItemHolder() {
        }

        boolean isLive() {
            return this.isLive;
        }

        boolean isThisCall(long j) {
            TL_stories.StoryItem storyItem = this.storyItem;
            if (storyItem == null) {
                return false;
            }
            TLRPC.MessageMedia messageMedia = storyItem.media;
            return (messageMedia instanceof TLRPC.TL_messageMediaVideoStream) && j == ((TLRPC.TL_messageMediaVideoStream) messageMedia).call.id;
        }

        /* JADX INFO: Access modifiers changed from: private */
        public String getMediaDebugString() {
            TLRPC.MessageMedia messageMedia;
            TL_stories.StoryItem storyItem = this.storyItem;
            if (storyItem != null && (messageMedia = storyItem.media) != null) {
                if (messageMedia.photo != null) {
                    return "photo#" + this.storyItem.media.photo.id + "at" + this.storyItem.media.photo.dc_id + "dc";
                }
                if (messageMedia.document != null) {
                    return "doc#" + this.storyItem.media.document.id + "at" + this.storyItem.media.document.dc_id + "dc";
                }
                return "unknown";
            }
            if (this.uploadingStory != null) {
                return "uploading from " + this.uploadingStory.path;
            }
            return "unknown";
        }

        public StoryCaptionView.Reply getReply() {
            if (this.reply == null) {
                if (this.storyItem != null) {
                    this.reply = StoryCaptionView.Reply.from(PeerStoriesView.this.currentAccount, this.storyItem);
                } else {
                    StoriesController.UploadingStory uploadingStory = this.uploadingStory;
                    if (uploadingStory != null) {
                        this.reply = StoryCaptionView.Reply.from(uploadingStory);
                    }
                }
            }
            return this.reply;
        }

        public void updateCaption() {
            int i;
            this.captionTranslated = false;
            PeerStoriesView peerStoriesView = PeerStoriesView.this;
            StoryItemHolder storyItemHolder = peerStoriesView.currentStory;
            StoriesController.UploadingStory uploadingStory = storyItemHolder.uploadingStory;
            if (uploadingStory != null) {
                CharSequence charSequence = uploadingStory.entry.caption;
                this.caption = charSequence;
                CharSequence charSequenceReplaceEmoji = Emoji.replaceEmoji(charSequence, peerStoriesView.storyCaptionView.captionTextview.getPaint().getFontMetricsInt(), false);
                this.caption = charSequenceReplaceEmoji;
                SpannableStringBuilder spannableStringBuilder = charSequenceReplaceEmoji == null ? new SpannableStringBuilder() : SpannableStringBuilder.valueOf(charSequenceReplaceEmoji);
                TLRPC.User user = MessagesController.getInstance(PeerStoriesView.this.currentAccount).getUser(Long.valueOf(PeerStoriesView.this.dialogId));
                if (PeerStoriesView.this.dialogId < 0 || MessagesController.getInstance(PeerStoriesView.this.currentAccount).storyEntitiesAllowed(user)) {
                    MessageObject.addLinks(true, spannableStringBuilder);
                    return;
                }
                return;
            }
            TL_stories.StoryItem storyItem = storyItemHolder.storyItem;
            if (storyItem != null) {
                if (storyItem.translated && storyItem.translatedText != null && TextUtils.equals(storyItem.translatedLng, TranslateAlert2.getToLanguage())) {
                    this.captionTranslated = true;
                    PeerStoriesView peerStoriesView2 = PeerStoriesView.this;
                    TLRPC.TL_textWithEntities tL_textWithEntities = peerStoriesView2.currentStory.storyItem.translatedText;
                    String str = tL_textWithEntities.text;
                    this.caption = str;
                    CharSequence charSequenceReplaceEmoji2 = Emoji.replaceEmoji(str, peerStoriesView2.storyCaptionView.captionTextview.getPaint().getFontMetricsInt(), false);
                    this.caption = charSequenceReplaceEmoji2;
                    if (charSequenceReplaceEmoji2 == null || tL_textWithEntities.entities == null) {
                        return;
                    }
                    SpannableStringBuilder spannableStringBuilderValueOf = SpannableStringBuilder.valueOf(MessageObject.replaceAnimatedEmoji(new SpannableStringBuilder(tL_textWithEntities.text), tL_textWithEntities.entities, PeerStoriesView.this.storyCaptionView.captionTextview.getPaint().getFontMetricsInt(), false));
                    SpannableStringBuilder.valueOf(Emoji.replaceEmoji(spannableStringBuilderValueOf, PeerStoriesView.this.storyCaptionView.captionTextview.getPaint().getFontMetricsInt(), false));
                    i = (PeerStoriesView.this.dialogId < 0 || MessagesController.getInstance(PeerStoriesView.this.currentAccount).storyEntitiesAllowed(MessagesController.getInstance(PeerStoriesView.this.currentAccount).getUser(Long.valueOf(PeerStoriesView.this.dialogId)))) ? 1 : 0;
                    if (i != 0) {
                        MessageObject.addLinks(true, spannableStringBuilderValueOf);
                    }
                    MessageObject.addEntitiesToText(spannableStringBuilderValueOf, tL_textWithEntities.entities, false, true, true, false, i ^ 1);
                    this.caption = spannableStringBuilderValueOf;
                    return;
                }
                PeerStoriesView peerStoriesView3 = PeerStoriesView.this;
                String str2 = peerStoriesView3.currentStory.storyItem.caption;
                this.caption = str2;
                CharSequence charSequenceReplaceEmoji3 = Emoji.replaceEmoji(str2, peerStoriesView3.storyCaptionView.captionTextview.getPaint().getFontMetricsInt(), false);
                this.caption = charSequenceReplaceEmoji3;
                if (charSequenceReplaceEmoji3 == null || PeerStoriesView.this.currentStory.storyItem.entities == null) {
                    return;
                }
                SpannableStringBuilder spannableStringBuilder2 = new SpannableStringBuilder(PeerStoriesView.this.currentStory.storyItem.caption);
                PeerStoriesView peerStoriesView4 = PeerStoriesView.this;
                SpannableStringBuilder spannableStringBuilderValueOf2 = SpannableStringBuilder.valueOf(MessageObject.replaceAnimatedEmoji(spannableStringBuilder2, peerStoriesView4.currentStory.storyItem.entities, peerStoriesView4.storyCaptionView.captionTextview.getPaint().getFontMetricsInt(), false));
                SpannableStringBuilder.valueOf(Emoji.replaceEmoji(spannableStringBuilderValueOf2, PeerStoriesView.this.storyCaptionView.captionTextview.getPaint().getFontMetricsInt(), false));
                i = (PeerStoriesView.this.dialogId < 0 || MessagesController.getInstance(PeerStoriesView.this.currentAccount).storyEntitiesAllowed(MessagesController.getInstance(PeerStoriesView.this.currentAccount).getUser(Long.valueOf(PeerStoriesView.this.dialogId)))) ? 1 : 0;
                if (i != 0) {
                    MessageObject.addLinks(true, spannableStringBuilderValueOf2);
                }
                MessageObject.addEntitiesToText(spannableStringBuilderValueOf2, PeerStoriesView.this.currentStory.storyItem.entities, false, true, true, false, i ^ 1);
                this.caption = spannableStringBuilderValueOf2;
            }
        }

        void set(TL_stories.StoryItem storyItem) {
            this.storyItem = storyItem;
            this.reply = null;
            this.uploadingStory = null;
            this.skipped = storyItem instanceof TL_stories.TL_storyItemSkipped;
            this.isVideo = isVideoInternal();
            this.isLive = isLiveInternal();
        }

        private boolean isLiveInternal() {
            TLRPC.MessageMedia messageMedia;
            TL_stories.StoryItem storyItem = this.storyItem;
            return (storyItem == null || (messageMedia = storyItem.media) == null || !(messageMedia instanceof TLRPC.TL_messageMediaVideoStream)) ? false : true;
        }

        private boolean isVideoInternal() {
            String str;
            TLRPC.MessageMedia messageMedia;
            StoriesController.UploadingStory uploadingStory = this.uploadingStory;
            if (uploadingStory != null) {
                return uploadingStory.isVideo;
            }
            TL_stories.StoryItem storyItem = this.storyItem;
            if (storyItem != null && (messageMedia = storyItem.media) != null && messageMedia.getDocument() != null) {
                TLRPC.Document document = this.storyItem.media.getDocument();
                return MessageObject.isVideoDocument(document) || "video/mp4".equals(document.mime_type);
            }
            TL_stories.StoryItem storyItem2 = this.storyItem;
            if (storyItem2 == null || storyItem2.media != null || (str = storyItem2.attachPath) == null) {
                return false;
            }
            return str.toLowerCase().endsWith(".mp4");
        }

        void set(StoriesController.UploadingStory uploadingStory) {
            this.uploadingStory = uploadingStory;
            this.reply = null;
            this.storyItem = null;
            this.skipped = false;
            this.isVideo = isVideoInternal();
            this.isLive = isLiveInternal();
        }

        public void clear() {
            this.uploadingStory = null;
            this.storyItem = null;
        }

        void cancelOrDelete() {
            TL_stories.StoryItem storyItem = this.storyItem;
            if (storyItem instanceof StoriesController.BotPreview) {
                ((StoriesController.BotPreview) storyItem).list.delete(storyItem.media);
                return;
            }
            if (storyItem != null) {
                PeerStoriesView peerStoriesView = PeerStoriesView.this;
                peerStoriesView.storiesController.deleteStory(peerStoriesView.dialogId, this.storyItem);
            } else {
                StoriesController.UploadingStory uploadingStory = this.uploadingStory;
                if (uploadingStory != null) {
                    uploadingStory.cancel();
                }
            }
        }

        /* JADX WARN: Code duplicated, block: B:20:0x0056  */
        /* JADX WARN: Code restructure failed: missing block: B:21:0x005a, code lost:
        
            if (r6.this$0.isSelf != false) goto L22;
         */
        /*
            Code decompiled incorrectly, please refer to instructions dump.
        */
        public void checkSendView() {
            TL_stories.StoryItem storyItem;
            TLRPC.UserFull userFull;
            PeerStoriesView peerStoriesView = PeerStoriesView.this;
            TL_stories.PeerStories stories = peerStoriesView.userStories;
            if (stories == null && (stories = peerStoriesView.storiesController.getStories(peerStoriesView.dialogId)) == null && (userFull = MessagesController.getInstance(PeerStoriesView.this.currentAccount).getUserFull(PeerStoriesView.this.dialogId)) != null) {
                stories = userFull.stories;
            }
            if (PeerStoriesView.this.isActive && (storyItem = this.storyItem) != null && stories != null) {
                if (!StoriesUtilities.hasExpiredViews(storyItem)) {
                    int i = this.storyItem.id;
                    if (i <= stories.max_read_id) {
                        PeerStoriesView peerStoriesView2 = PeerStoriesView.this;
                        if (i <= peerStoriesView2.storiesController.dialogIdToMaxReadId.get(peerStoriesView2.dialogId, 0)) {
                        }
                    }
                }
                if (PeerStoriesView.this.storyViewer.overrideUserStories != null) {
                    PeerStoriesView peerStoriesView3 = PeerStoriesView.this;
                    if (peerStoriesView3.storiesController.markStoryAsRead(peerStoriesView3.storyViewer.overrideUserStories, this.storyItem, true)) {
                        PeerStoriesView.this.storyViewer.unreadStateChanged = true;
                        return;
                    }
                    return;
                }
                PeerStoriesView peerStoriesView4 = PeerStoriesView.this;
                if (peerStoriesView4.storiesController.markStoryAsRead(peerStoriesView4.dialogId, this.storyItem)) {
                    PeerStoriesView.this.storyViewer.unreadStateChanged = true;
                    return;
                }
                return;
            }
            PeerStoriesView peerStoriesView5 = PeerStoriesView.this;
            if (!peerStoriesView5.isActive || this.storyItem == null || peerStoriesView5.storyViewer.storiesList == null || !PeerStoriesView.this.storyViewer.storiesList.markAsRead(this.storyItem.id)) {
                return;
            }
            PeerStoriesView.this.storyViewer.unreadStateChanged = true;
        }

        public String getLocalPath() {
            TL_stories.StoryItem storyItem = this.storyItem;
            if (storyItem != null) {
                return storyItem.attachPath;
            }
            return null;
        }

        boolean isVideo() {
            return this.isVideo;
        }

        boolean hasSound() {
            TLRPC.MessageMedia messageMedia;
            TLRPC.Document document;
            if (!this.isVideo) {
                return false;
            }
            TL_stories.StoryItem storyItem = this.storyItem;
            if (storyItem != null && (messageMedia = storyItem.media) != null && (document = messageMedia.getDocument()) != null) {
                for (int i = 0; i < document.attributes.size(); i++) {
                    TLRPC.DocumentAttribute documentAttribute = document.attributes.get(i);
                    if ((documentAttribute instanceof TLRPC.TL_documentAttributeVideo) && documentAttribute.nosound) {
                        return false;
                    }
                }
                return true;
            }
            StoriesController.UploadingStory uploadingStory = this.uploadingStory;
            if (uploadingStory != null) {
                return !uploadingStory.entry.muted;
            }
            return true;
        }

        public String createLink() {
            PeerStoriesView peerStoriesView = PeerStoriesView.this;
            if (peerStoriesView.currentStory.storyItem == null) {
                return null;
            }
            if (peerStoriesView.dialogId > 0) {
                TLRPC.User user = MessagesController.getInstance(PeerStoriesView.this.currentAccount).getUser(Long.valueOf(PeerStoriesView.this.dialogId));
                if (UserObject.getPublicUsername(user) == null) {
                    return null;
                }
                return PeerStoriesView.this.currentStory.isLive ? String.format(Locale.US, "https://t.me/%1$s/s/live", UserObject.getPublicUsername(user)) : String.format(Locale.US, "https://t.me/%1$s/s/%2$s", UserObject.getPublicUsername(user), Integer.valueOf(PeerStoriesView.this.currentStory.storyItem.id));
            }
            TLRPC.Chat chat = MessagesController.getInstance(PeerStoriesView.this.currentAccount).getChat(Long.valueOf(-PeerStoriesView.this.dialogId));
            if (ChatObject.getPublicUsername(chat) == null) {
                return null;
            }
            return PeerStoriesView.this.currentStory.isLive ? String.format(Locale.US, "https://t.me/%1$s/s/live", ChatObject.getPublicUsername(chat)) : String.format(Locale.US, "https://t.me/%1$s/s/%2$s", ChatObject.getPublicUsername(chat), Integer.valueOf(PeerStoriesView.this.currentStory.storyItem.id));
        }

        public File getPath() {
            TLRPC.Photo photo;
            if (getLocalPath() != null) {
                return new File(getLocalPath());
            }
            TL_stories.StoryItem storyItem = this.storyItem;
            if (storyItem == null) {
                return null;
            }
            TLRPC.MessageMedia messageMedia = storyItem.media;
            if (messageMedia != null && messageMedia.getDocument() != null) {
                return FileLoader.getInstance(PeerStoriesView.this.currentAccount).getPathToAttach(this.storyItem.media.getDocument());
            }
            TLRPC.MessageMedia messageMedia2 = this.storyItem.media;
            if (messageMedia2 == null || (photo = messageMedia2.photo) == null) {
                return null;
            }
            TLRPC.PhotoSize closestPhotoSizeWithSize = FileLoader.getClosestPhotoSizeWithSize(photo.sizes, Integer.MAX_VALUE);
            File pathToAttach = FileLoader.getInstance(PeerStoriesView.this.currentAccount).getPathToAttach(closestPhotoSizeWithSize, true);
            return !pathToAttach.exists() ? FileLoader.getInstance(PeerStoriesView.this.currentAccount).getPathToAttach(closestPhotoSizeWithSize, false) : pathToAttach;
        }

        public boolean allowScreenshots() {
            StoriesController.UploadingStory uploadingStory = this.uploadingStory;
            if (uploadingStory != null) {
                return uploadingStory.entry.allowScreenshots;
            }
            TL_stories.StoryItem storyItem = this.storyItem;
            if (storyItem == null) {
                return true;
            }
            if (storyItem.noforwards) {
                return false;
            }
            if (!storyItem.pinned) {
                return true;
            }
            TLRPC.Chat chat = MessagesController.getInstance(PeerStoriesView.this.currentAccount).getChat(Long.valueOf(-storyItem.dialogId));
            return chat == null || !chat.noforwards;
        }
    }

    public static int getStoryId(TL_stories.StoryItem storyItem, StoriesController.UploadingStory uploadingStory) {
        StoryEntry storyEntry;
        if (storyItem != null) {
            return storyItem.id;
        }
        if (uploadingStory == null || (storyEntry = uploadingStory.entry) == null) {
            return 0;
        }
        return storyEntry.editStoryId;
    }

    @Override // android.widget.FrameLayout, android.view.View
    protected void onMeasure(int i, int i2) {
        int size;
        ReactionsContainerLayout reactionsContainerLayout;
        MentionsContainerView mentionsContainerView;
        if (this.storyViewer.ATTACH_TO_FRAGMENT) {
            ((FrameLayout.LayoutParams) getLayoutParams()).topMargin = AndroidUtilities.statusBarHeight;
        }
        if (this.isActive && this.shareAlert == null) {
            this.realKeyboardHeight = this.delegate.getKeyboardHeight();
        } else {
            this.realKeyboardHeight = 0;
        }
        if (this.storyViewer.ATTACH_TO_FRAGMENT) {
            size = View.MeasureSpec.getSize(i2);
        } else {
            size = View.MeasureSpec.getSize(i2) + this.realKeyboardHeight;
        }
        int size2 = (int) ((View.MeasureSpec.getSize(i) * 16.0f) / 9.0f);
        if (size <= size2 || size2 > size) {
            size2 = size;
        }
        if (this.realKeyboardHeight < AndroidUtilities.dp(20.0f)) {
            this.realKeyboardHeight = 0;
        }
        int visibleEmojiPadding = this.realKeyboardHeight;
        ReactionsContainerLayout reactionsContainerLayout2 = this.likesReactionLayout;
        if (reactionsContainerLayout2 != null && reactionsContainerLayout2.getReactionsWindow() != null && this.likesReactionLayout.getReactionsWindow().isShowing()) {
            this.likesReactionLayout.getReactionsWindow().windowView.animate().translationY(-this.realKeyboardHeight).setDuration(250L).setInterpolator(AdjustPanLayoutHelper.keyboardInterpolator).start();
            visibleEmojiPadding = 0;
        } else {
            ChatActivityEnterView chatActivityEnterView = this.chatActivityEnterView;
            if (chatActivityEnterView != null && (chatActivityEnterView.isPopupShowing() || this.chatActivityEnterView.isWaitingForKeyboard())) {
                if (this.chatActivityEnterView.getEmojiView().getMeasuredHeight() == 0) {
                    visibleEmojiPadding = this.chatActivityEnterView.getEmojiPadding();
                } else if (this.chatActivityEnterView.isStickersExpanded()) {
                    this.chatActivityEnterView.checkStickresExpandHeight();
                    visibleEmojiPadding = this.chatActivityEnterView.getStickersExpandedHeight();
                } else {
                    visibleEmojiPadding = this.chatActivityEnterView.getVisibleEmojiPadding();
                }
            }
        }
        boolean z = this.keyboardVisible;
        if (this.lastKeyboardHeight != visibleEmojiPadding) {
            this.keyboardVisible = false;
            if (visibleEmojiPadding > 0 && this.isActive) {
                this.keyboardVisible = true;
                this.messageSent = false;
                this.lastOpenedKeyboardHeight = visibleEmojiPadding;
                checkReactionsLayout();
                ReactionsEffectOverlay.dismissAll();
            } else {
                ChatActivityEnterView chatActivityEnterView2 = this.chatActivityEnterView;
                if (chatActivityEnterView2 != null) {
                    this.storyViewer.saveDraft(this.dialogId, this.currentStory.storyItem, chatActivityEnterView2.getEditText());
                }
            }
            ChatActivityEnterView chatActivityEnterView3 = this.chatActivityEnterView;
            if (chatActivityEnterView3 != null) {
                chatActivityEnterView3.setSuggestionButtonVisible(this.currentStory.isLive && !disabledPaidFeatures(true) && this.keyboardVisible, true);
            }
            if (this.keyboardVisible && (mentionsContainerView = this.mentionContainer) != null) {
                mentionsContainerView.setVisibility(0);
            }
            if (!this.keyboardVisible && (reactionsContainerLayout = this.reactionsContainerLayout) != null) {
                reactionsContainerLayout.reset();
            }
            this.headerView.setEnabled(!this.keyboardVisible);
            ChatActivityEnterView chatActivityEnterView4 = this.chatActivityEnterView;
            if (chatActivityEnterView4 != null) {
                chatActivityEnterView4.checkReactionsButton(!this.keyboardVisible);
            }
            if (this.isActive && this.keyboardVisible) {
                this.delegate.setKeyboardVisible(true);
            }
            this.lastKeyboardHeight = visibleEmojiPadding;
            ValueAnimator valueAnimator = this.keyboardAnimator;
            if (valueAnimator != null) {
                valueAnimator.cancel();
            }
            this.notificationsLocker.lock();
            ValueAnimator valueAnimatorOfFloat = ValueAnimator.ofFloat(this.animatingKeyboardHeight, visibleEmojiPadding);
            this.keyboardAnimator = valueAnimatorOfFloat;
            valueAnimatorOfFloat.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() { // from class: org.telegram.ui.Stories.PeerStoriesView$$ExternalSyntheticLambda26
                @Override // android.animation.ValueAnimator.AnimatorUpdateListener
                public final void onAnimationUpdate(ValueAnimator valueAnimator2) {
                    this.f$0.lambda$onMeasure$53(valueAnimator2);
                }
            });
            this.keyboardAnimator.addListener(new AnimatorListenerAdapter() { // from class: org.telegram.ui.Stories.PeerStoriesView.35
                @Override // android.animation.AnimatorListenerAdapter, android.animation.Animator.AnimatorListener
                public void onAnimationEnd(Animator animator) {
                    super.onAnimationEnd(animator);
                    PeerStoriesView.this.notificationsLocker.unlock();
                    PeerStoriesView peerStoriesView = PeerStoriesView.this;
                    peerStoriesView.animatingKeyboardHeight = peerStoriesView.lastKeyboardHeight;
                    ChatActivityEnterView chatActivityEnterView5 = PeerStoriesView.this.chatActivityEnterView;
                    if (chatActivityEnterView5 != null) {
                        chatActivityEnterView5.onOverrideAnimationEnd();
                    }
                    PeerStoriesView peerStoriesView2 = PeerStoriesView.this;
                    if (peerStoriesView2.isActive && !peerStoriesView2.keyboardVisible) {
                        peerStoriesView2.delegate.setKeyboardVisible(false);
                    }
                    PeerStoriesView peerStoriesView3 = PeerStoriesView.this;
                    if (!peerStoriesView3.keyboardVisible && peerStoriesView3.mentionContainer != null) {
                        PeerStoriesView.this.mentionContainer.setVisibility(8);
                    }
                    PeerStoriesView peerStoriesView4 = PeerStoriesView.this;
                    peerStoriesView4.forceUpdateOffsets = true;
                    peerStoriesView4.invalidate();
                }
            });
            if (this.keyboardVisible) {
                this.keyboardAnimator.setDuration(250L);
                this.keyboardAnimator.setInterpolator(AdjustPanLayoutHelper.keyboardInterpolator);
                this.storyViewer.cancelSwipeToReply();
            } else {
                this.keyboardAnimator.setDuration(500L);
                this.keyboardAnimator.setInterpolator(CubicBezierInterpolator.EASE_OUT_QUINT);
            }
            this.keyboardAnimator.start();
            boolean z2 = this.keyboardVisible;
            if (z2 != z) {
                if (z2) {
                    createBlurredBitmap(this.bitmapShaderTools.getCanvas(), this.bitmapShaderTools.getBitmap());
                    if (this.currentStory.isLive) {
                        showPaidMessageHint();
                    }
                } else {
                    ChatActivityEnterView chatActivityEnterView5 = this.chatActivityEnterView;
                    if (chatActivityEnterView5 != null) {
                        chatActivityEnterView5.getEditField().clearFocus();
                    }
                    HintView2 hintView2 = this.highlightMessageHintView;
                    if (hintView2 != null) {
                        hintView2.hide();
                    }
                }
                this.animateKeyboardOpening = true;
            } else {
                this.animateKeyboardOpening = false;
            }
        }
        ChatActivityEnterView chatActivityEnterView6 = this.chatActivityEnterView;
        if (chatActivityEnterView6 != null && chatActivityEnterView6.getEmojiView() != null) {
            ((FrameLayout.LayoutParams) this.chatActivityEnterView.getEmojiView().getLayoutParams()).gravity = 80;
        }
        FrameLayout.LayoutParams layoutParams = (FrameLayout.LayoutParams) this.storyContainer.getLayoutParams();
        layoutParams.height = size2;
        boolean z3 = size - size2 > AndroidUtilities.dp(64.0f);
        this.BIG_SCREEN = z3;
        int iDp = (size - ((z3 ? AndroidUtilities.dp(64.0f) : 0) + size2)) >> 1;
        layoutParams.topMargin = iDp;
        if (this.BIG_SCREEN) {
            this.enterViewBottomOffset = (((-iDp) + size) - size2) - AndroidUtilities.dp(64.0f);
        } else {
            this.enterViewBottomOffset = ((-iDp) + size) - size2;
        }
        if (this.BIG_SCREEN != this.wasBigScreen) {
            this.storyContainer.setLayoutParams(layoutParams);
        }
        FrameLayout frameLayout = this.selfView;
        if (frameLayout != null) {
            FrameLayout.LayoutParams layoutParams2 = (FrameLayout.LayoutParams) frameLayout.getLayoutParams();
            if (this.BIG_SCREEN) {
                layoutParams2.topMargin = iDp + size2 + AndroidUtilities.dp(8.0f);
            } else {
                layoutParams2.topMargin = (iDp + size2) - AndroidUtilities.dp(48.0f);
            }
        }
        TextView textView = this.replyDisabledTextView;
        if (textView != null) {
            FrameLayout.LayoutParams layoutParams3 = (FrameLayout.LayoutParams) textView.getLayoutParams();
            if (!this.BIG_SCREEN) {
                this.replyDisabledTextView.setTextColor(ColorUtils.setAlphaComponent(-1, 191));
                layoutParams3.topMargin = ((iDp + size2) - AndroidUtilities.dp(12.0f)) - AndroidUtilities.dp(40.0f);
            } else {
                this.replyDisabledTextView.setTextColor(ColorUtils.blendARGB(-16777216, -1, 0.5f));
                layoutParams3.topMargin = iDp + size2 + AndroidUtilities.dp(12.0f);
            }
        }
        InstantCameraView instantCameraView = this.instantCameraView;
        if (instantCameraView != null) {
            FrameLayout.LayoutParams layoutParams4 = (FrameLayout.LayoutParams) instantCameraView.getLayoutParams();
            if (visibleEmojiPadding == 0) {
                layoutParams4.bottomMargin = size - ((iDp + size2) - AndroidUtilities.dp(64.0f));
            } else {
                layoutParams4.bottomMargin = visibleEmojiPadding + AndroidUtilities.dp(64.0f);
            }
        }
        if (!this.BIG_SCREEN) {
            ((FrameLayout.LayoutParams) this.bottomActionsLinearLayout.getLayoutParams()).topMargin = ((iDp + size2) - AndroidUtilities.dp(12.0f)) - AndroidUtilities.dp(40.0f);
            int iDp2 = this.isSelf ? AndroidUtilities.dp(40.0f) : AndroidUtilities.dp(56.0f);
            ((FrameLayout.LayoutParams) this.storyCaptionView.getLayoutParams()).bottomMargin = iDp2;
            if (this.wasBigScreen != this.BIG_SCREEN) {
                StoryCaptionView storyCaptionView = this.storyCaptionView;
                storyCaptionView.setLayoutParams((FrameLayout.LayoutParams) storyCaptionView.getLayoutParams());
            }
            this.storyCaptionView.blackoutBottomOffset = iDp2;
        } else {
            ((FrameLayout.LayoutParams) this.bottomActionsLinearLayout.getLayoutParams()).topMargin = iDp + size2 + AndroidUtilities.dp(12.0f);
            ((FrameLayout.LayoutParams) this.storyCaptionView.getLayoutParams()).bottomMargin = AndroidUtilities.dp(8.0f);
            if (this.wasBigScreen != this.BIG_SCREEN) {
                StoryCaptionView storyCaptionView2 = this.storyCaptionView;
                storyCaptionView2.setLayoutParams((FrameLayout.LayoutParams) storyCaptionView2.getLayoutParams());
            }
            this.storyCaptionView.blackoutBottomOffset = AndroidUtilities.dp(8.0f);
        }
        this.forceUpdateOffsets = true;
        float fDp = AndroidUtilities.dp(48.0f);
        if (this.privacyButton.getVisibility() == 0) {
            fDp += AndroidUtilities.dp(60.0f);
        }
        if (this.muteIconContainer.getVisibility() == 0) {
            fDp += AndroidUtilities.dp(40.0f);
        }
        FrameLayout.LayoutParams layoutParams5 = (FrameLayout.LayoutParams) this.headerView.titleView.getLayoutParams();
        if (layoutParams5.rightMargin != fDp) {
            int i3 = (int) fDp;
            layoutParams5.rightMargin = i3;
            ((FrameLayout.LayoutParams) this.headerView.subtitleView[0].getLayoutParams()).rightMargin = i3;
            ((FrameLayout.LayoutParams) this.headerView.subtitleView[1].getLayoutParams()).rightMargin = i3;
            this.headerView.forceLayout();
        }
        super.onMeasure(i, View.MeasureSpec.makeMeasureSpec(size, TLObject.FLAG_30));
        this.wasBigScreen = this.BIG_SCREEN;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$onMeasure$53(ValueAnimator valueAnimator) {
        this.animatingKeyboardHeight = ((Float) valueAnimator.getAnimatedValue()).floatValue();
        invalidate();
    }

    @Override // android.view.View
    protected void onSizeChanged(int i, int i2, int i3, int i4) {
        super.onSizeChanged(i, i2, i3, i4);
        this.progressToKeyboard = -1.0f;
        this.forceUpdateOffsets = true;
        invalidate();
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void updateViewOffsets() {
        float fClamp;
        int iDp;
        StoryItemHolder storyItemHolder;
        ReactionsContainerLayout reactionsContainerLayout;
        StoryItemHolder storyItemHolder2;
        LivePlayerView livePlayerView;
        float progressToDismiss = this.delegate.getProgressToDismiss();
        float fDp = 0.0f;
        this.progressToHideInterface.set(this.isLongPressed ? 1.0f : 0.0f);
        int i = this.lastOpenedKeyboardHeight;
        if (i != 0 && this.animateKeyboardOpening) {
            fClamp = MathUtils.clamp(this.animatingKeyboardHeight / i, 0.0f, 1.0f);
        } else {
            fClamp = this.keyboardVisible ? 1.0f : 0.0f;
        }
        float f = this.progressToRecording.get();
        float f2 = this.progressToTextA.get();
        float f3 = this.progressToStickerExpanded.get();
        this.progressToRecording.set(this.isRecording ? 1.0f : 0.0f);
        if (!this.messageSent) {
            AnimatedFloat animatedFloat = this.progressToTextA;
            ChatActivityEnterView chatActivityEnterView = this.chatActivityEnterView;
            animatedFloat.set((chatActivityEnterView == null || TextUtils.isEmpty(chatActivityEnterView.getFieldText())) ? 0.0f : 1.0f);
        }
        AnimatedFloat animatedFloat2 = this.progressToStickerExpanded;
        ChatActivityEnterView chatActivityEnterView2 = this.chatActivityEnterView;
        animatedFloat2.set((chatActivityEnterView2 == null || !chatActivityEnterView2.isStickersExpanded()) ? 0.0f : 1.0f);
        ChatActivityEnterView chatActivityEnterView3 = this.chatActivityEnterView;
        if (chatActivityEnterView3 != null) {
            chatActivityEnterView3.checkAnimation();
        }
        ChatActivityEnterView chatActivityEnterView4 = this.chatActivityEnterView;
        boolean z = true;
        boolean z2 = chatActivityEnterView4 != null && chatActivityEnterView4.isPopupShowing();
        float hideInterfaceAlpha = getHideInterfaceAlpha();
        if (this.BIG_SCREEN) {
            this.inputBackgroundPaint.setColor(ColorUtils.blendARGB(-14933463, Theme.multAlpha(-16777216, 0.44f), this.progressToKeyboard));
            Paint paint = this.inputBackgroundPaint;
            paint.setAlpha((int) (paint.getAlpha() * (1.0f - this.progressToDismiss) * hideInterfaceAlpha * (1.0f - this.outT)));
        } else {
            this.inputBackgroundPaint.setColor(ColorUtils.setAlphaComponent(-16777216, (int) (137.70001f * hideInterfaceAlpha * (1.0f - this.outT))));
        }
        if (!this.forceUpdateOffsets && this.progressToReply == this.storyViewer.swipeToReplyProgress && this.progressToHideInterface.get() == this.prevToHideProgress && this.lastAnimatingKeyboardHeight == this.animatingKeyboardHeight && fClamp == this.progressToKeyboard && progressToDismiss == this.progressToDismiss && f == this.progressToRecording.get() && !z2 && f3 == this.progressToStickerExpanded.get() && f2 == this.progressToTextA.get()) {
            return;
        }
        this.forceUpdateOffsets = false;
        this.lastAnimatingKeyboardHeight = this.animatingKeyboardHeight;
        if (this.progressToHideInterface.get() != this.prevToHideProgress) {
            this.storyContainer.invalidate();
        }
        if (progressToDismiss != 0.0f) {
            this.storyContainer.setLayerType(2, null);
        } else {
            this.storyContainer.setLayerType(0, null);
        }
        this.prevToHideProgress = this.progressToHideInterface.get();
        this.progressToDismiss = progressToDismiss;
        this.progressToKeyboard = fClamp;
        this.progressToReply = this.storyViewer.swipeToReplyProgress;
        float height = this.animatingKeyboardHeight - (getHeight() - this.storyContainer.getBottom());
        ChatActivityEnterView chatActivityEnterView5 = this.chatActivityEnterView;
        float fMax = Math.max(0.0f, height + (chatActivityEnterView5 == null ? 0.0f : chatActivityEnterView5.getHeightWithTopView() * fClamp));
        float height2 = this.lastKeyboardHeight - (getHeight() - this.storyContainer.getBottom());
        ChatActivityEnterView chatActivityEnterView6 = this.chatActivityEnterView;
        float fMax2 = Math.max(0.0f, height2 + (chatActivityEnterView6 == null ? 0.0f : chatActivityEnterView6.getHeightWithTopView() * (this.keyboardVisible ? 1.0f : 0.0f)));
        ChatActivityEnterView chatActivityEnterView7 = this.chatActivityEnterView;
        if (chatActivityEnterView7 != null) {
            if (this.highlightMessageHintView != null) {
                ImageView suggestButton = chatActivityEnterView7.getSuggestButton();
                if (suggestButton != null) {
                    this.highlightMessageHintView.setJointPx(1.0f, -(AndroidUtilities.dp(74.0f) - suggestButton.getTranslationX()));
                }
                this.highlightMessageHintView.setTranslationY(-((getHeight() - this.chatActivityEnterView.getY()) + AndroidUtilities.dp(6.0f)));
            }
            EditTextCaption editField = this.chatActivityEnterView.getEditField();
            SenderSelectView senderSelectView = this.chatActivityEnterView.getSenderSelectView();
            if (editField != null) {
                float fLerp = AndroidUtilities.lerp(0.88f, 1.0f, fClamp);
                editField.setScaleX(fLerp);
                editField.setScaleY(fLerp);
            }
            if (senderSelectView != null) {
                float fLerp2 = AndroidUtilities.lerp(0.8f, 1.0f, fClamp);
                senderSelectView.setScaleX(fLerp2);
                senderSelectView.setScaleY(fLerp2);
                senderSelectView.setTranslationY(AndroidUtilities.lerp(-AndroidUtilities.dp(2.0f), 0, fClamp));
            }
        }
        this.liveCommentsView.setKeyboardOffset(fClamp, Math.max(0.0f, (this.BIG_SCREEN ? 0.0f : AndroidUtilities.dp(58.0f) * (1.0f - fClamp)) + fMax) + (AndroidUtilities.dp(6.0f) * fClamp), Math.max(0.0f, fMax2 + ((this.BIG_SCREEN || this.keyboardVisible) ? 0 : AndroidUtilities.dp(58.0f))));
        StoryViewer storyViewer = this.storyViewer;
        if (storyViewer != null && (livePlayerView = storyViewer.liveView) != null) {
            livePlayerView.setKeyboardOffset(fMax);
        }
        ReactionsContainerLayout reactionsContainerLayout2 = this.reactionsContainerLayout;
        if (reactionsContainerLayout2 != null) {
            reactionsContainerLayout2.setVisibility((this.progressToKeyboard <= 0.0f || ((storyItemHolder2 = this.currentStory) != null && storyItemHolder2.isLive)) ? 8 : 0);
        }
        for (int i2 = 0; i2 < getChildCount(); i2++) {
            View childAt = getChildAt(i2);
            if (childAt.getVisibility() != 0 || childAt == this.selfView || childAt.getTag(R.id.parent_tag) != null || childAt == this.storyCaptionView.textSelectionHelper.getOverlayView(getContext())) {
                if (childAt == this.selfView) {
                    if (this.BIG_SCREEN) {
                        childAt.setAlpha((1.0f - this.progressToDismiss) * hideInterfaceAlpha * (1.0f - this.outT));
                    } else {
                        childAt.setAlpha((1.0f - this.outT) * hideInterfaceAlpha);
                    }
                }
            } else {
                ChatActivityEnterView chatActivityEnterView8 = this.chatActivityEnterView;
                if (chatActivityEnterView8 != null && childAt == chatActivityEnterView8.getEmojiView()) {
                    childAt.setTranslationY(this.chatActivityEnterView.getEmojiView().getMeasuredHeight() - this.animatingKeyboardHeight);
                } else if (childAt instanceof HintView) {
                    ((HintView) childAt).updatePosition();
                } else if (childAt != this.instantCameraView && childAt != this.storyContainer && childAt != this.shareButton && childAt != this.bottomActionsLinearLayout && childAt != this.repostButtonContainer && childAt != this.mediaBanTooltip && childAt != this.highlightMessageHintView && childAt != this.likeButtonContainer && ((reactionsContainerLayout = this.likesReactionLayout) == null || reactionsContainerLayout.getReactionsWindow() == null || childAt != this.likesReactionLayout.getReactionsWindow().windowView)) {
                    float f4 = this.progressToKeyboard;
                    if (childAt == this.commentButton || childAt == this.starsButton || childAt == this.muteButton || childAt == this.starsButtonEffectsView) {
                        f4 = 0.0f;
                    }
                    float f5 = 1.0f - f4;
                    float fDp2 = (((((-this.enterViewBottomOffset) * f5) - (AndroidUtilities.dp(7.0f) * f4)) - this.animatingKeyboardHeight) - (AndroidUtilities.dp(8.0f) * f5)) - (AndroidUtilities.dp(20.0f) * this.storyViewer.swipeToReplyProgress);
                    if (childAt == this.commentButton || childAt == this.starsButton || childAt == this.muteButton || childAt == this.starsButtonEffectsView) {
                        fDp2 += this.animatingKeyboardHeight;
                    }
                    float f6 = this.BIG_SCREEN ? (1.0f - this.progressToDismiss) * hideInterfaceAlpha : hideInterfaceAlpha * 1.0f;
                    if (childAt == this.replyDisabledTextView) {
                        fDp2 = this.storyViewer.swipeToReplyProgress * (-AndroidUtilities.dp(20.0f));
                    }
                    if (childAt == this.mentionContainer) {
                        fDp2 -= this.chatActivityEnterView.getMeasuredHeight() - this.chatActivityEnterView.getAnimatedTop();
                        f6 = this.progressToKeyboard;
                        childAt.invalidate();
                    }
                    if (childAt == this.reactionsContainerLayout) {
                        float f7 = this.progressToKeyboard * (1.0f - this.progressToRecording.get()) * (1.0f - f3) * (1.0f - this.progressToTextA.get());
                        float f8 = f6 * f7 * 1.0f;
                        if (childAt.getAlpha() != 0.0f && f8 == 0.0f) {
                            this.reactionsContainerLayout.reset();
                        }
                        childAt.setAlpha(f8);
                        float f9 = (f7 * 0.2f) + 0.8f;
                        childAt.setScaleX(f9);
                        childAt.setScaleY(f9);
                    } else {
                        childAt.setTranslationY(fDp2);
                        ChatActivityEnterView chatActivityEnterView9 = this.chatActivityEnterView;
                        if (chatActivityEnterView9 == null || childAt != chatActivityEnterView9.controlsView) {
                            childAt.setAlpha(f6);
                        }
                    }
                }
            }
        }
        float f10 = (1.0f - progressToDismiss) * hideInterfaceAlpha;
        this.shareButton.setAlpha((1.0f - this.outT) * f10);
        this.likeButtonContainer.setAlpha((1.0f - this.outT) * f10);
        FrameLayout frameLayout = this.repostButtonContainer;
        if (frameLayout != null) {
            frameLayout.setAlpha(f10 * (1.0f - this.outT));
        }
        for (int i3 = 0; i3 < this.storyContainer.getChildCount(); i3++) {
            View childAt2 = this.storyContainer.getChildAt(i3);
            if (childAt2 != null && childAt2 != this.liveCommentsShadowView) {
                if (childAt2 == this.headerView || childAt2 == this.optionsIconView || childAt2 == this.pipIconView || childAt2 == this.muteIconContainer || childAt2 == this.selfView || childAt2 == this.storyCaptionView || childAt2 == this.privacyButton) {
                    float f11 = childAt2 == this.muteIconContainer ? this.muteIconViewAlpha : 1.0f;
                    if (childAt2 == this.storyCaptionView) {
                        childAt2.setAlpha(f11 * (hideCaptionWithInterface() ? hideInterfaceAlpha : 1.0f) * (1.0f - this.outT));
                    } else {
                        childAt2.setAlpha(f11 * hideInterfaceAlpha * (1.0f - this.outT));
                    }
                } else {
                    childAt2.setAlpha(hideInterfaceAlpha);
                }
            }
        }
        if (this.chatActivityEnterView != null) {
            float fDp3 = AndroidUtilities.dp(40.0f);
            if (!this.currentStory.isLive) {
                if (this.allowShare) {
                    fDp3 += AndroidUtilities.dp(46.0f);
                }
                if (this.allowRepost && this.isChannel) {
                    fDp3 += AndroidUtilities.dp(46.0f);
                }
                FrameLayout frameLayout2 = this.likeButtonContainer;
                if (frameLayout2 != null && frameLayout2.getVisibility() == 0) {
                    fDp3 -= AndroidUtilities.dp(40.0f);
                    iDp = this.likeButtonContainer.getLayoutParams().width;
                    fDp3 += iDp;
                }
            } else {
                fDp = AndroidUtilities.dp(46.0f);
                fDp3 = AndroidUtilities.dp(46.0f);
                MuteButton muteButton = this.muteButton;
                if (muteButton != null && muteButton.getVisibility() == 0) {
                    iDp = AndroidUtilities.dp(46.0f);
                    fDp3 += iDp;
                }
            }
            ChatActivityEnterView chatActivityEnterView10 = this.chatActivityEnterView;
            float f12 = -AndroidUtilities.dp(10.0f);
            float f13 = -(AndroidUtilities.dp(10.0f) + fDp3 + fDp);
            float f14 = this.progressToKeyboard;
            if ((!this.allowShare && !this.isGroup) || ((storyItemHolder = this.currentStory) != null && storyItemHolder.isLive)) {
                z = false;
            }
            chatActivityEnterView10.setHorizontalPadding(f12, f13, f14, z);
            if (this.chatActivityEnterView.getEmojiView() != null) {
                this.chatActivityEnterView.getEmojiView().setAlpha(this.progressToKeyboard);
            }
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public float getHideInterfaceAlpha() {
        return (1.0f - this.progressToHideInterface.get()) * (1.0f - this.storyViewer.getProgressToSelfViews());
    }

    @Override // android.view.ViewGroup
    protected boolean drawChild(Canvas canvas, View view, long j) {
        Canvas canvas2;
        BlurredBackgroundSourceRenderNode blurredBackgroundSourceRenderNode;
        float fDp;
        if (view == this.mentionContainer) {
            canvas.save();
            canvas.clipRect(0.0f, this.mentionContainer.getY(), getMeasuredWidth(), this.mentionContainer.getY() + this.mentionContainer.getMeasuredHeight());
            boolean zDrawChild = super.drawChild(canvas, view, j);
            canvas.restore();
            return zDrawChild;
        }
        ChatActivityEnterView chatActivityEnterView = this.chatActivityEnterView;
        if (view == chatActivityEnterView) {
            if (this.progressToKeyboard <= 0.0f || this.currentStory.isLive) {
                canvas2 = canvas;
            } else {
                this.sharedResources.dimPaint.setAlpha((int) (this.progressToKeyboard * 63.75f));
                canvas2 = canvas;
                canvas2.drawRect(0.0f, 0.0f, getMeasuredWidth(), getMeasuredHeight(), this.sharedResources.dimPaint);
            }
            this.sharedResources.rect1.set(this.chatActivityEnterView.getX(), this.chatActivityEnterView.getY() + this.chatActivityEnterView.getAnimatedTop() + AndroidUtilities.dp(1.33f), this.chatActivityEnterView.getX() + this.chatActivityEnterView.getMeasuredWidth(), this.chatActivityEnterView.getY() + this.chatActivityEnterView.getMeasuredHeight());
            float fDp2 = AndroidUtilities.dp(40.0f);
            if (!this.currentStory.isLive) {
                if (this.allowShare) {
                    fDp2 += AndroidUtilities.dp(46.0f);
                }
                if (this.allowRepost && this.isChannel) {
                    fDp2 += AndroidUtilities.dp(46.0f);
                }
                FrameLayout frameLayout = this.likeButtonContainer;
                if (frameLayout != null && frameLayout.getVisibility() == 0) {
                    fDp2 = (fDp2 - AndroidUtilities.dp(40.0f)) + this.likeButtonContainer.getLayoutParams().width;
                }
                fDp = 0.0f;
            } else {
                fDp = AndroidUtilities.dp(46.0f);
                fDp2 = AndroidUtilities.dp(46.0f);
                MuteButton muteButton = this.muteButton;
                if (muteButton != null && muteButton.getVisibility() == 0) {
                    fDp2 += AndroidUtilities.dp(46.0f);
                }
            }
            this.sharedResources.rect2.set(AndroidUtilities.dp(10.0f) + fDp, ((this.chatActivityEnterView.getY() + this.chatActivityEnterView.getMeasuredHeight()) - AndroidUtilities.dp(5.0f)) - AndroidUtilities.dp(38.0f), (getMeasuredWidth() - AndroidUtilities.dp(10.0f)) - fDp2, (this.chatActivityEnterView.getY() + this.chatActivityEnterView.getMeasuredHeight()) - AndroidUtilities.dp(5.0f));
            this.chatActivityEnterView.setTranslationX(fDp * (1.0f - this.progressToKeyboard));
            this.chatActivityEnterView.getEditField().setTranslationY((this.chatActivityEnterView.getMeasuredHeight() > AndroidUtilities.dp(50.0f) ? 0.0f + ((1.0f - this.progressToKeyboard) * (this.chatActivityEnterView.getMeasuredHeight() - AndroidUtilities.dp(50.0f))) : 0.0f) + ((-AndroidUtilities.dp(2.0f)) * (1.0f - this.progressToKeyboard)));
            float fDp3 = AndroidUtilities.dp(50.0f) / 2.0f;
            AndroidUtilities.lerp(this.sharedResources.rect2, this.sharedResources.rect1, this.progressToKeyboard, this.sharedResources.finalRect);
            BlurredBackgroundDrawable blurredBackgroundDrawable = this.inputFieldBackground;
            if (blurredBackgroundDrawable != null) {
                blurredBackgroundDrawable.setBounds((int) this.sharedResources.finalRect.left, (int) this.sharedResources.finalRect.top, (int) this.sharedResources.finalRect.right, (int) this.sharedResources.finalRect.bottom);
                this.inputFieldBackground.setRadius(fDp3);
                this.inputFieldBackground.setAlpha((int) ((1.0f - this.progressToDismiss) * 255.0f * getHideInterfaceAlpha() * (1.0f - this.outT)));
                this.inputFieldBackground.draw(canvas2);
            } else {
                canvas2.drawRoundRect(this.sharedResources.finalRect, fDp3, fDp3, this.inputBackgroundPaint);
            }
            if (this.progressToKeyboard < 0.5f) {
                canvas2.save();
                canvas2.clipRect(this.sharedResources.finalRect);
                boolean zDrawChild2 = super.drawChild(canvas2, view, j);
                canvas2.restore();
                return zDrawChild2;
            }
        } else {
            canvas2 = canvas;
            if (chatActivityEnterView != null && chatActivityEnterView.isPopupView(view)) {
                float fDp4 = AndroidUtilities.dp(30.0f);
                this.sharedResources.popupRect.set(0.0f, view.getY() + AndroidUtilities.dp(1.0f), getWidth(), getHeight() + AndroidUtilities.dp(20.0f));
                this.clipPath.rewind();
                this.clipPath.addRoundRect(this.sharedResources.popupRect, fDp4, fDp4, Path.Direction.CW);
                canvas2.save();
                canvas2.clipPath(this.clipPath);
                BlurredBackgroundDrawable blurredBackgroundDrawable2 = this.emojiKeyboardBackground;
                if (blurredBackgroundDrawable2 != null) {
                    blurredBackgroundDrawable2.setBounds((int) this.sharedResources.popupRect.left, (int) this.sharedResources.popupRect.top, (int) this.sharedResources.popupRect.right, (int) this.sharedResources.popupRect.bottom);
                    this.emojiKeyboardBackground.setRadius(fDp4, fDp4, fDp4, fDp4);
                    this.emojiKeyboardBackground.setAlpha(255);
                    this.emojiKeyboardBackground.draw(canvas2);
                } else {
                    canvas2.drawRoundRect(this.sharedResources.popupRect, fDp4, fDp4, this.inputBackgroundPaint);
                }
                boolean zDrawChild3 = super.drawChild(canvas2, view, j);
                canvas2.restore();
                return zDrawChild3;
            }
            ReactionsContainerLayout reactionsContainerLayout = this.reactionsContainerLayout;
            if (view == reactionsContainerLayout && this.chatActivityEnterView != null) {
                view.setTranslationY(((-reactionsContainerLayout.getMeasuredHeight()) + (this.chatActivityEnterView.getY() + this.chatActivityEnterView.getAnimatedTop())) - AndroidUtilities.dp(18.0f));
            } else {
                ReactionsContainerLayout reactionsContainerLayout2 = this.likesReactionLayout;
                if (view == reactionsContainerLayout2) {
                    view.setTranslationY((((-(reactionsContainerLayout2.getMeasuredHeight() - this.likesReactionLayout.getPaddingBottom())) + this.likeButtonContainer.getY()) + this.bottomActionsLinearLayout.getY()) - AndroidUtilities.dp(18.0f));
                } else if (view == this.storyContainer && Build.VERSION.SDK_INT >= 31 && canvas2.isHardwareAccelerated() && (blurredBackgroundSourceRenderNode = this.blurredBackgroundSourceRenderNodeWithSaturation) != null && !blurredBackgroundSourceRenderNode.inRecording()) {
                    RecordingCanvas recordingCanvasBeginRecording = this.blurredBackgroundSourceRenderNodeWithSaturation.beginRecording(getMeasuredWidth(), getMeasuredHeight());
                    recordingCanvasBeginRecording.drawColor(ColorUtils.blendARGB(-16777216, -1, 0.2f));
                    recordingCanvasBeginRecording.translate(this.storyContainer.getX(), this.storyContainer.getY());
                    view.draw(recordingCanvasBeginRecording);
                    this.blurredBackgroundSourceRenderNodeWithSaturation.endRecording();
                }
            }
        }
        return super.drawChild(canvas2, view, j);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void checkInstantCameraView() {
        if (this.instantCameraView == null && CameraView.isCameraAllowed()) {
            this.instantCameraView = new InstantCameraView(getContext(), new InstantCameraView.Delegate() { // from class: org.telegram.ui.Stories.PeerStoriesView.36
                @Override // org.telegram.ui.Components.InstantCameraView.Delegate
                public /* synthetic */ boolean isInScheduleMode() {
                    return InstantCameraView.Delegate.CC.$default$isInScheduleMode(this);
                }

                @Override // org.telegram.ui.Components.InstantCameraView.Delegate
                public /* synthetic */ boolean isSecretChat() {
                    return InstantCameraView.Delegate.CC.$default$isSecretChat(this);
                }

                @Override // org.telegram.ui.Components.InstantCameraView.Delegate
                public View getFragmentView() {
                    return PeerStoriesView.this;
                }

                @Override // org.telegram.ui.Components.InstantCameraView.Delegate
                public void sendMedia(MediaController.PhotoEntry photoEntry, VideoEditedInfo videoEditedInfo, boolean z, int i, int i2, boolean z2, long j) {
                    if (photoEntry == null) {
                        return;
                    }
                    PeerStoriesView peerStoriesView = PeerStoriesView.this;
                    TL_stories.StoryItem storyItem = peerStoriesView.currentStory.storyItem;
                    if (storyItem == null || (storyItem instanceof TL_stories.TL_storyItemSkipped)) {
                        return;
                    }
                    storyItem.dialogId = peerStoriesView.dialogId;
                    if (photoEntry.isVideo) {
                        if (videoEditedInfo != null) {
                            SendMessagesHelper.prepareSendingVideo(PeerStoriesView.this.getAccountInstance(), photoEntry.path, videoEditedInfo, null, null, PeerStoriesView.this.dialogId, null, null, storyItem, null, photoEntry.entities, photoEntry.ttl, null, z, i, i2, z2, photoEntry.hasSpoiler, photoEntry.caption, null, 0, 0L, j);
                        } else {
                            SendMessagesHelper.prepareSendingVideo(PeerStoriesView.this.getAccountInstance(), photoEntry.path, null, null, null, PeerStoriesView.this.dialogId, null, null, storyItem, null, photoEntry.entities, photoEntry.ttl, null, z, i, i2, z2, photoEntry.hasSpoiler, photoEntry.caption, null, 0, 0L, j);
                        }
                    } else if (photoEntry.imagePath != null) {
                        SendMessagesHelper.prepareSendingPhoto(PeerStoriesView.this.getAccountInstance(), photoEntry.imagePath, photoEntry.thumbPath, null, PeerStoriesView.this.dialogId, null, null, storyItem, null, photoEntry.entities, photoEntry.stickers, null, photoEntry.ttl, null, videoEditedInfo, z, i, i2, z2, photoEntry.caption, null, 0, 0L, j);
                    } else if (photoEntry.path != null) {
                        SendMessagesHelper.prepareSendingPhoto(PeerStoriesView.this.getAccountInstance(), photoEntry.path, photoEntry.thumbPath, null, PeerStoriesView.this.dialogId, null, null, storyItem, null, photoEntry.entities, photoEntry.stickers, null, photoEntry.ttl, null, videoEditedInfo, z, i, i2, z2, photoEntry.caption, null, 0, 0L, j);
                    }
                    PeerStoriesView.this.afterMessageSend(j <= 0);
                }

                @Override // org.telegram.ui.Components.InstantCameraView.Delegate
                public Activity getParentActivity() {
                    return AndroidUtilities.findActivity(PeerStoriesView.this.getContext());
                }

                @Override // org.telegram.ui.Components.InstantCameraView.Delegate
                public int getClassGuid() {
                    return PeerStoriesView.this.classGuid;
                }

                @Override // org.telegram.ui.Components.InstantCameraView.Delegate
                public long getDialogId() {
                    return PeerStoriesView.this.dialogId;
                }
            }, this.resourcesProvider, false);
            addView(this.instantCameraView, Math.min(indexOfChild(this.chatActivityEnterView.getRecordCircle()), indexOfChild(this.chatActivityEnterView.controlsView)), LayoutHelper.createFrame(-1, -1, 51));
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void afterMessageSend(boolean z) {
        BulletinFactory bulletinFactoryOf;
        InstantCameraView instantCameraView = this.instantCameraView;
        if (instantCameraView != null) {
            instantCameraView.resetCameraFile();
            this.instantCameraView.cancel(false);
        }
        this.storyViewer.clearDraft(this.dialogId, this.currentStory.storyItem);
        this.messageSent = true;
        this.storyViewer.closeKeyboardOrEmoji();
        if (z && (bulletinFactoryOf = BulletinFactory.of(this.storyContainer, this.resourcesProvider)) != null) {
            bulletinFactoryOf.createSimpleBulletin(R.raw.forward, LocaleController.getString(R.string.MessageSent), LocaleController.getString(R.string.ViewInChat), 5000, new Runnable() { // from class: org.telegram.ui.Stories.PeerStoriesView$$ExternalSyntheticLambda24
                @Override // java.lang.Runnable
                public final void run() {
                    this.f$0.openChat();
                }
            }).hideAfterBottomSheet(false).show(false);
        }
        MessagesController.getInstance(this.currentAccount).ensureMessagesLoaded(this.dialogId, 0, null);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void openChat() {
        Bundle bundle = new Bundle();
        long j = this.dialogId;
        if (j < 0) {
            bundle.putLong("chat_id", -j);
        } else {
            bundle.putLong("user_id", j);
        }
        TLRPC.Dialog dialog = MessagesController.getInstance(this.currentAccount).getDialog(this.dialogId);
        if (dialog != null) {
            bundle.putInt("message_id", dialog.top_message);
        }
        this.storyViewer.presentFragment(new ChatActivity(bundle));
    }

    /* JADX INFO: Access modifiers changed from: private */
    public AccountInstance getAccountInstance() {
        return AccountInstance.getInstance(this.currentAccount);
    }

    public static class VideoPlayerSharedScope {
        public boolean firstFrameRendered;
        public LivePlayer livePlayer;
        public StoryViewer.VideoPlayerHolder player;
        public View renderView;
        public SurfaceView surfaceView;
        public TextureView textureView;
        public ArrayList viewsToInvalidate = new ArrayList();

        public void invalidate() {
            for (int i = 0; i < this.viewsToInvalidate.size(); i++) {
                ((View) this.viewsToInvalidate.get(i)).invalidate();
            }
        }

        public boolean isBuffering() {
            StoryViewer.VideoPlayerHolder videoPlayerHolder = this.player;
            return videoPlayerHolder != null && videoPlayerHolder.isBuffering();
        }
    }

    void checkReactionsLayout() {
        if (this.reactionsContainerLayout == null) {
            ReactionsContainerLayout reactionsContainerLayout = new ReactionsContainerLayout(1, LaunchActivity.getLastFragment(), getContext(), this.currentAccount, new WrappedResourceProvider(this.resourcesProvider) { // from class: org.telegram.ui.Stories.PeerStoriesView.37
                @Override // org.telegram.ui.WrappedResourceProvider
                public void appendColors() {
                    this.sparseIntArray.put(Theme.key_chat_emojiPanelBackground, ColorUtils.setAlphaComponent(-1, 30));
                }
            });
            this.reactionsContainerLayout = reactionsContainerLayout;
            reactionsContainerLayout.setHint(LocaleController.getString(this.isGroup ? R.string.StoryGroupReactionsHint : R.string.StoryReactionsHint));
            ReactionsContainerLayout reactionsContainerLayout2 = this.reactionsContainerLayout;
            reactionsContainerLayout2.skipEnterAnimation = true;
            addView(reactionsContainerLayout2, this.reactionsContainerIndex, LayoutHelper.createFrame(-2, 72.0f, 49, 0.0f, 0.0f, 0.0f, 64.0f));
            this.reactionsContainerLayout.setDelegate(new AnonymousClass38());
            this.reactionsContainerLayout.setMessage(null, null, true);
        }
        this.reactionsContainerLayout.setFragment(LaunchActivity.getLastFragment());
        this.reactionsContainerLayout.setHint(LocaleController.getString(this.isGroup ? R.string.StoryGroupReactionsHint : R.string.StoryReactionsHint));
    }

    /* JADX INFO: renamed from: org.telegram.ui.Stories.PeerStoriesView$38, reason: invalid class name */
    class AnonymousClass38 implements ReactionsContainerLayout.ReactionsContainerDelegate {
        @Override // org.telegram.ui.Components.ReactionsContainerLayout.ReactionsContainerDelegate
        public /* synthetic */ boolean allowLongPress() {
            return ReactionsContainerLayout.ReactionsContainerDelegate.CC.$default$allowLongPress(this);
        }

        @Override // org.telegram.ui.Components.ReactionsContainerLayout.ReactionsContainerDelegate
        public /* synthetic */ boolean drawBackground() {
            return ReactionsContainerLayout.ReactionsContainerDelegate.CC.$default$drawBackground(this);
        }

        AnonymousClass38() {
        }

        @Override // org.telegram.ui.Components.ReactionsContainerLayout.ReactionsContainerDelegate
        public void onReactionClicked(View view, ReactionsLayoutInBubble.VisibleReaction visibleReaction, boolean z, boolean z2) {
            onReactionClickedInternal(view, visibleReaction, z, z2, !z);
        }

        void onReactionClickedInternal(View view, ReactionsLayoutInBubble.VisibleReaction visibleReaction, boolean z, final boolean z2, boolean z3) {
            final AnonymousClass38 anonymousClass38;
            final View view2;
            final ReactionsLayoutInBubble.VisibleReaction visibleReaction2;
            final boolean z4;
            if (z3) {
                anonymousClass38 = this;
                view2 = view;
                visibleReaction2 = visibleReaction;
                z4 = z;
                if (PeerStoriesView.this.applyMessageToChat(new Runnable() { // from class: org.telegram.ui.Stories.PeerStoriesView$38$$ExternalSyntheticLambda0
                    @Override // java.lang.Runnable
                    public final void run() {
                        this.f$0.lambda$onReactionClickedInternal$0(view2, visibleReaction2, z4, z2);
                    }
                })) {
                    return;
                }
            } else {
                anonymousClass38 = this;
                view2 = view;
                visibleReaction2 = visibleReaction;
                z4 = z;
            }
            AlertsCreator.ensurePaidMessageConfirmation(PeerStoriesView.this.currentAccount, PeerStoriesView.this.dialogId, 1, new Utilities.Callback() { // from class: org.telegram.ui.Stories.PeerStoriesView$38$$ExternalSyntheticLambda1
                @Override // org.telegram.messenger.Utilities.Callback
                public final void run(Object obj) {
                    this.f$0.lambda$onReactionClickedInternal$2(z4, visibleReaction2, view2, (Long) obj);
                }
            });
        }

        /* JADX INFO: Access modifiers changed from: private */
        public /* synthetic */ void lambda$onReactionClickedInternal$0(View view, ReactionsLayoutInBubble.VisibleReaction visibleReaction, boolean z, boolean z2) {
            onReactionClickedInternal(view, visibleReaction, z, z2, false);
        }

        /* JADX INFO: Access modifiers changed from: private */
        public /* synthetic */ void lambda$onReactionClickedInternal$2(boolean z, ReactionsLayoutInBubble.VisibleReaction visibleReaction, View view, Long l) {
            ReactionsEffectOverlay reactionsEffectOverlay;
            ReactionsLayoutInBubble.VisibleReaction visibleReaction2;
            TLRPC.Document documentFindDocument;
            if (z && visibleReaction.emojicon != null) {
                try {
                    PeerStoriesView.this.performHapticFeedback(0);
                } catch (Exception unused) {
                }
                Context context = view.getContext();
                PeerStoriesView peerStoriesView = PeerStoriesView.this;
                reactionsEffectOverlay = new ReactionsEffectOverlay(context, null, peerStoriesView.reactionsContainerLayout, null, view, peerStoriesView.getMeasuredWidth() / 2.0f, PeerStoriesView.this.getMeasuredHeight() / 2.0f, visibleReaction, PeerStoriesView.this.currentAccount, 0, true);
                visibleReaction2 = visibleReaction;
            } else {
                Context context2 = view.getContext();
                PeerStoriesView peerStoriesView2 = PeerStoriesView.this;
                visibleReaction2 = visibleReaction;
                reactionsEffectOverlay = new ReactionsEffectOverlay(context2, null, peerStoriesView2.reactionsContainerLayout, null, view, peerStoriesView2.getMeasuredWidth() / 2.0f, PeerStoriesView.this.getMeasuredHeight() / 2.0f, visibleReaction2, PeerStoriesView.this.currentAccount, 2, true);
            }
            ReactionsEffectOverlay.currentOverlay = reactionsEffectOverlay;
            reactionsEffectOverlay.windowView.setTag(R.id.parent_tag, 1);
            PeerStoriesView.this.addView(reactionsEffectOverlay.windowView);
            reactionsEffectOverlay.started = true;
            reactionsEffectOverlay.startTime = System.currentTimeMillis();
            if (visibleReaction2.emojicon != null) {
                documentFindDocument = MediaDataController.getInstance(PeerStoriesView.this.currentAccount).getEmojiAnimatedSticker(visibleReaction2.emojicon);
                SendMessagesHelper.SendMessageParams sendMessageParamsOf = SendMessagesHelper.SendMessageParams.of(visibleReaction2.emojicon, PeerStoriesView.this.dialogId);
                sendMessageParamsOf.replyToStoryItem = PeerStoriesView.this.currentStory.storyItem;
                sendMessageParamsOf.payStars = l.longValue();
                SendMessagesHelper.getInstance(PeerStoriesView.this.currentAccount).sendMessage(sendMessageParamsOf);
            } else {
                documentFindDocument = AnimatedEmojiDrawable.findDocument(PeerStoriesView.this.currentAccount, visibleReaction2.documentId);
                String strFindAnimatedEmojiEmoticon = MessageObject.findAnimatedEmojiEmoticon(documentFindDocument, null);
                if (strFindAnimatedEmojiEmoticon == null) {
                    if (PeerStoriesView.this.reactionsContainerLayout.getReactionsWindow() != null) {
                        PeerStoriesView.this.reactionsContainerLayout.getReactionsWindow().dismissWithAlpha();
                    }
                    PeerStoriesView.this.closeKeyboardOrEmoji();
                    return;
                }
                SendMessagesHelper.SendMessageParams sendMessageParamsOf2 = SendMessagesHelper.SendMessageParams.of(strFindAnimatedEmojiEmoticon, PeerStoriesView.this.dialogId);
                sendMessageParamsOf2.entities = new ArrayList<>();
                TLRPC.TL_messageEntityCustomEmoji tL_messageEntityCustomEmoji = new TLRPC.TL_messageEntityCustomEmoji();
                tL_messageEntityCustomEmoji.document_id = visibleReaction2.documentId;
                tL_messageEntityCustomEmoji.offset = 0;
                tL_messageEntityCustomEmoji.length = strFindAnimatedEmojiEmoticon.length();
                sendMessageParamsOf2.entities.add(tL_messageEntityCustomEmoji);
                sendMessageParamsOf2.replyToStoryItem = PeerStoriesView.this.currentStory.storyItem;
                sendMessageParamsOf2.payStars = l.longValue();
                SendMessagesHelper.getInstance(PeerStoriesView.this.currentAccount).sendMessage(sendMessageParamsOf2);
            }
            if (l.longValue() <= 0) {
                PeerStoriesView peerStoriesView3 = PeerStoriesView.this;
                BulletinFactory.of(peerStoriesView3.storyContainer, peerStoriesView3.resourcesProvider).createEmojiBulletin(documentFindDocument, LocaleController.getString(R.string.ReactionSent), LocaleController.getString(R.string.ViewInChat), new Runnable() { // from class: org.telegram.ui.Stories.PeerStoriesView$38$$ExternalSyntheticLambda2
                    @Override // java.lang.Runnable
                    public final void run() {
                        this.f$0.lambda$onReactionClickedInternal$1();
                    }
                }).setDuration(5000).show();
            }
            if (PeerStoriesView.this.reactionsContainerLayout.getReactionsWindow() != null) {
                PeerStoriesView.this.reactionsContainerLayout.getReactionsWindow().dismissWithAlpha();
            }
            PeerStoriesView.this.closeKeyboardOrEmoji();
        }

        /* JADX INFO: Access modifiers changed from: private */
        public /* synthetic */ void lambda$onReactionClickedInternal$1() {
            PeerStoriesView.this.openChat();
        }

        @Override // org.telegram.ui.Components.ReactionsContainerLayout.ReactionsContainerDelegate
        public void drawRoundRect(Canvas canvas, RectF rectF, float f, float f2, float f3, int i, boolean z) {
            float f4 = -f2;
            float f5 = -f3;
            PeerStoriesView.this.bitmapShaderTools.setBounds(f4, f5, PeerStoriesView.this.getMeasuredWidth() + f4, PeerStoriesView.this.getMeasuredHeight() + f5);
            if (f > 0.0f) {
                canvas.drawRoundRect(rectF, f, f, PeerStoriesView.this.bitmapShaderTools.paint);
                canvas.drawRoundRect(rectF, f, f, PeerStoriesView.this.inputBackgroundPaint);
            } else {
                canvas.drawRect(rectF, PeerStoriesView.this.bitmapShaderTools.paint);
                canvas.drawRect(rectF, PeerStoriesView.this.inputBackgroundPaint);
            }
        }

        @Override // org.telegram.ui.Components.ReactionsContainerLayout.ReactionsContainerDelegate
        public boolean needEnterText() {
            return PeerStoriesView.this.needEnterText();
        }

        @Override // org.telegram.ui.Components.ReactionsContainerLayout.ReactionsContainerDelegate
        public void onEmojiWindowDismissed() {
            PeerStoriesView.this.delegate.requestAdjust(false);
        }
    }

    void checkReactionsLayoutForLike() {
        ReactionsContainerLayout reactionsContainerLayout = this.likesReactionLayout;
        if (reactionsContainerLayout == null) {
            ReactionsContainerLayout reactionsContainerLayout2 = new ReactionsContainerLayout(2, LaunchActivity.getLastFragment(), getContext(), this.currentAccount, new WrappedResourceProvider(this.resourcesProvider) { // from class: org.telegram.ui.Stories.PeerStoriesView.39
                @Override // org.telegram.ui.WrappedResourceProvider
                public void appendColors() {
                    this.sparseIntArray.put(Theme.key_chat_emojiPanelBackground, ColorUtils.setAlphaComponent(-1, 30));
                }
            });
            this.likesReactionLayout = reactionsContainerLayout2;
            reactionsContainerLayout2.setPadding(0, 0, 0, AndroidUtilities.dp(22.0f));
            addView(this.likesReactionLayout, getChildCount() - 1, LayoutHelper.createFrame(-2, 74.0f, 53, 0.0f, 0.0f, 12.0f, 64.0f));
            this.likesReactionLayout.setVisibility(8);
            this.likesReactionLayout.setDelegate(new AnonymousClass40());
            this.likesReactionLayout.setMessage(null, null, true);
        } else {
            bringChildToFront(reactionsContainerLayout);
            this.likesReactionLayout.reset();
        }
        this.likesReactionLayout.setFragment(LaunchActivity.getLastFragment());
    }

    /* JADX INFO: renamed from: org.telegram.ui.Stories.PeerStoriesView$40, reason: invalid class name */
    class AnonymousClass40 implements ReactionsContainerLayout.ReactionsContainerDelegate {
        @Override // org.telegram.ui.Components.ReactionsContainerLayout.ReactionsContainerDelegate
        public /* synthetic */ boolean allowLongPress() {
            return ReactionsContainerLayout.ReactionsContainerDelegate.CC.$default$allowLongPress(this);
        }

        @Override // org.telegram.ui.Components.ReactionsContainerLayout.ReactionsContainerDelegate
        public /* synthetic */ boolean drawBackground() {
            return ReactionsContainerLayout.ReactionsContainerDelegate.CC.$default$drawBackground(this);
        }

        @Override // org.telegram.ui.Components.ReactionsContainerLayout.ReactionsContainerDelegate
        public /* synthetic */ void drawRoundRect(Canvas canvas, RectF rectF, float f, float f2, float f3, int i, boolean z) {
            ReactionsContainerLayout.ReactionsContainerDelegate.CC.$default$drawRoundRect(this, canvas, rectF, f, f2, f3, i, z);
        }

        @Override // org.telegram.ui.Components.ReactionsContainerLayout.ReactionsContainerDelegate
        public /* synthetic */ void onEmojiWindowDismissed() {
            ReactionsContainerLayout.ReactionsContainerDelegate.CC.$default$onEmojiWindowDismissed(this);
        }

        AnonymousClass40() {
        }

        @Override // org.telegram.ui.Components.ReactionsContainerLayout.ReactionsContainerDelegate
        public void onReactionClicked(final View view, final ReactionsLayoutInBubble.VisibleReaction visibleReaction, boolean z, boolean z2) {
            Runnable runnable = new Runnable() { // from class: org.telegram.ui.Stories.PeerStoriesView$40$$ExternalSyntheticLambda0
                @Override // java.lang.Runnable
                public final void run() {
                    this.f$0.lambda$onReactionClicked$1(visibleReaction, view);
                }
            };
            if (!z) {
                PeerStoriesView.this.applyMessageToChat(runnable);
            } else {
                runnable.run();
            }
        }

        /* JADX INFO: Access modifiers changed from: private */
        public /* synthetic */ void lambda$onReactionClicked$1(ReactionsLayoutInBubble.VisibleReaction visibleReaction, View view) {
            TLRPC.TL_availableReaction tL_availableReaction;
            PeerStoriesView.this.movingReaction = true;
            final boolean[] zArr = {false};
            final StoriesLikeButton storiesLikeButton = PeerStoriesView.this.storiesLikeButton;
            storiesLikeButton.animate().alpha(0.0f).scaleX(0.8f).scaleY(0.8f).setListener(new AnimatorListenerAdapter() { // from class: org.telegram.ui.Stories.PeerStoriesView.40.1
                @Override // android.animation.AnimatorListenerAdapter, android.animation.Animator.AnimatorListener
                public void onAnimationEnd(Animator animator) {
                    AndroidUtilities.removeFromParent(storiesLikeButton);
                }
            }).setDuration(150L).start();
            int iDp = AndroidUtilities.dp(8.0f);
            PeerStoriesView.this.storiesLikeButton = new StoriesLikeButton(PeerStoriesView.this.getContext(), PeerStoriesView.this.sharedResources);
            PeerStoriesView.this.storiesLikeButton.setPadding(iDp, iDp, iDp, iDp);
            PeerStoriesView.this.likeButtonContainer.addView(PeerStoriesView.this.storiesLikeButton, LayoutHelper.createFrame(40, 40, 3));
            if (PeerStoriesView.this.reactionMoveDrawable != null) {
                PeerStoriesView.this.reactionMoveDrawable.removeView(PeerStoriesView.this);
                PeerStoriesView.this.reactionMoveDrawable = null;
            }
            if (PeerStoriesView.this.emojiReactionEffect != null) {
                PeerStoriesView.this.emojiReactionEffect.removeView(PeerStoriesView.this);
                PeerStoriesView.this.emojiReactionEffect = null;
            }
            PeerStoriesView.this.drawAnimatedEmojiAsMovingReaction = false;
            if (visibleReaction.documentId != 0) {
                PeerStoriesView.this.drawAnimatedEmojiAsMovingReaction = true;
                PeerStoriesView.this.reactionMoveDrawable = new AnimatedEmojiDrawable(2, PeerStoriesView.this.currentAccount, visibleReaction.documentId);
                PeerStoriesView.this.reactionMoveDrawable.addView(PeerStoriesView.this);
            } else if (visibleReaction.emojicon != null && (tL_availableReaction = MediaDataController.getInstance(PeerStoriesView.this.currentAccount).getReactionsMap().get(visibleReaction.emojicon)) != null) {
                PeerStoriesView.this.reactionMoveImageReceiver.setImage(null, null, ImageLocation.getForDocument(tL_availableReaction.select_animation), "60_60", null, null, null, 0L, null, null, 0);
                PeerStoriesView.this.reactionEffectImageReceiver.setImage(ImageLocation.getForDocument(tL_availableReaction.around_animation), ReactionsEffectOverlay.getFilterForAroundAnimation(), null, null, null, 0);
                if (PeerStoriesView.this.reactionEffectImageReceiver.getLottieAnimation() != null) {
                    PeerStoriesView.this.reactionEffectImageReceiver.getLottieAnimation().setCurrentFrame(0, false, true);
                }
            }
            PeerStoriesView.this.storiesLikeButton.setReaction(visibleReaction);
            PeerStoriesView peerStoriesView = PeerStoriesView.this;
            if (peerStoriesView.isChannel) {
                TL_stories.StoryItem storyItem = peerStoriesView.currentStory.storyItem;
                if (storyItem.sent_reaction == null) {
                    if (storyItem.views == null) {
                        storyItem.views = new TL_stories.TL_storyViews();
                    }
                    TL_stories.StoryItem storyItem2 = PeerStoriesView.this.currentStory.storyItem;
                    TL_stories.StoryViews storyViews = storyItem2.views;
                    storyViews.reactions_count++;
                    ReactionsUtils.applyForStoryViews(null, storyItem2.sent_reaction, storyViews);
                    PeerStoriesView.this.updateUserViews(true);
                }
            }
            if (visibleReaction.documentId != 0 && PeerStoriesView.this.storiesLikeButton.emojiDrawable != null) {
                PeerStoriesView peerStoriesView2 = PeerStoriesView.this;
                peerStoriesView2.emojiReactionEffect = AnimatedEmojiEffect.createFrom(peerStoriesView2.storiesLikeButton.emojiDrawable, false, true);
                PeerStoriesView.this.emojiReactionEffect.setView(PeerStoriesView.this);
            }
            PeerStoriesView peerStoriesView3 = PeerStoriesView.this;
            peerStoriesView3.storiesController.setStoryReaction(peerStoriesView3.dialogId, PeerStoriesView.this.currentStory.storyItem, visibleReaction);
            int[] iArr = new int[2];
            view.getLocationInWindow(iArr);
            int[] iArr2 = new int[2];
            PeerStoriesView.this.getLocationInWindow(iArr2);
            PeerStoriesView.this.movingReactionFromX = iArr[0] - iArr2[0];
            PeerStoriesView.this.movingReactionFromY = iArr[1] - iArr2[1];
            PeerStoriesView.this.movingReactionFromSize = view.getMeasuredHeight();
            final ValueAnimator valueAnimatorOfFloat = ValueAnimator.ofFloat(0.0f, 1.0f);
            PeerStoriesView.this.movingReactionProgress = 0.0f;
            PeerStoriesView.this.invalidate();
            final StoriesLikeButton storiesLikeButton2 = PeerStoriesView.this.storiesLikeButton;
            storiesLikeButton2.setAllowDrawReaction(false);
            storiesLikeButton2.prepareAnimateReaction(visibleReaction);
            valueAnimatorOfFloat.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() { // from class: org.telegram.ui.Stories.PeerStoriesView$40$$ExternalSyntheticLambda1
                @Override // android.animation.ValueAnimator.AnimatorUpdateListener
                public final void onAnimationUpdate(ValueAnimator valueAnimator) {
                    this.f$0.lambda$onReactionClicked$0(valueAnimatorOfFloat, zArr, valueAnimator);
                }
            });
            valueAnimatorOfFloat.addListener(new AnimatorListenerAdapter() { // from class: org.telegram.ui.Stories.PeerStoriesView.40.2
                @Override // android.animation.AnimatorListenerAdapter, android.animation.Animator.AnimatorListener
                public void onAnimationEnd(Animator animator) {
                    PeerStoriesView.this.movingReaction = false;
                    PeerStoriesView.this.movingReactionProgress = 1.0f;
                    PeerStoriesView.this.invalidate();
                    boolean[] zArr2 = zArr;
                    if (!zArr2[0]) {
                        zArr2[0] = true;
                        PeerStoriesView.this.drawReactionEffect = true;
                        try {
                            PeerStoriesView.this.performHapticFeedback(3);
                        } catch (Exception unused) {
                        }
                    }
                    storiesLikeButton2.setAllowDrawReaction(true);
                    storiesLikeButton2.animateVisibleReaction();
                    if (PeerStoriesView.this.reactionMoveDrawable != null) {
                        PeerStoriesView.this.reactionMoveDrawable.removeView(PeerStoriesView.this);
                        PeerStoriesView.this.reactionMoveDrawable = null;
                    }
                }
            });
            valueAnimatorOfFloat.setDuration(220L);
            valueAnimatorOfFloat.start();
            PeerStoriesView.this.showLikesReaction(false);
        }

        /* JADX INFO: Access modifiers changed from: private */
        public /* synthetic */ void lambda$onReactionClicked$0(ValueAnimator valueAnimator, boolean[] zArr, ValueAnimator valueAnimator2) {
            PeerStoriesView.this.movingReactionProgress = ((Float) valueAnimator.getAnimatedValue()).floatValue();
            PeerStoriesView.this.invalidate();
            if (PeerStoriesView.this.movingReactionProgress <= 0.8f || zArr[0]) {
                return;
            }
            zArr[0] = true;
            PeerStoriesView.this.drawReactionEffect = true;
            try {
                PeerStoriesView.this.performHapticFeedback(3);
            } catch (Exception unused) {
            }
        }

        @Override // org.telegram.ui.Components.ReactionsContainerLayout.ReactionsContainerDelegate
        public boolean needEnterText() {
            PeerStoriesView.this.delegate.requestAdjust(false);
            return false;
        }
    }

    public boolean needEnterText() {
        ChatActivityEnterView chatActivityEnterView = this.chatActivityEnterView;
        if (chatActivityEnterView == null) {
            return false;
        }
        boolean zIsKeyboardVisible = chatActivityEnterView.isKeyboardVisible();
        if (zIsKeyboardVisible) {
            this.chatActivityEnterView.showEmojiView();
        }
        AndroidUtilities.runOnUIThread(new Runnable() { // from class: org.telegram.ui.Stories.PeerStoriesView$$ExternalSyntheticLambda25
            @Override // java.lang.Runnable
            public final void run() {
                this.f$0.lambda$needEnterText$54();
            }
        }, 300L);
        return zIsKeyboardVisible;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$needEnterText$54() {
        this.delegate.requestAdjust(true);
    }

    public void setViewsThumbImageReceiver(float f, float f2, float f3, SelfStoriesPreviewView.ImageHolder imageHolder) {
        this.viewsThumbAlpha = f;
        this.viewsThumbScale = 1.0f / f2;
        this.viewsThumbPivotY = f3;
        if (this.viewsThumbImageReceiver == imageHolder) {
            return;
        }
        this.viewsThumbImageReceiver = imageHolder;
        if (imageHolder == null || imageHolder.receiver.getBitmap() == null) {
            return;
        }
        this.imageReceiver.updateStaticDrawableThump(imageHolder.receiver.getBitmap().copy(Bitmap.Config.ARGB_8888, false));
    }

    public static class SharedResources {
        public final Paint barPaint;
        private final Drawable bottomOverlayGradient;
        public Drawable deleteDrawable;
        private final Paint gradientBackgroundPaint;
        public final Drawable imageBackgroundDrawable;
        public Drawable likeDrawable;
        public Drawable likeDrawableFilled;
        public RLottieDrawable muteDrawable;
        public RLottieDrawable noSoundDrawable;
        public Drawable optionsDrawable;
        public Drawable pipDrawable;
        public Drawable repostDrawable;
        public final Paint selectedBarPaint;
        public Drawable shareDrawable;
        private final Drawable topOverlayGradient;
        public final BitmapShaderTools bitmapShaderTools = new BitmapShaderTools();
        private final RectF rect1 = new RectF();
        private final RectF rect2 = new RectF();
        private final RectF finalRect = new RectF();
        private final RectF borderRect = new RectF();
        private final RectF popupRect = new RectF();
        private final Paint dimPaint = new Paint();

        SharedResources(Context context) {
            this.shareDrawable = ContextCompat.getDrawable(context, R.drawable.media_share);
            this.likeDrawable = ContextCompat.getDrawable(context, R.drawable.media_like);
            this.repostDrawable = ContextCompat.getDrawable(context, R.drawable.media_repost);
            Drawable drawable = ContextCompat.getDrawable(context, R.drawable.media_like_active);
            this.likeDrawableFilled = drawable;
            drawable.setColorFilter(new PorterDuffColorFilter(-53704, PorterDuff.Mode.MULTIPLY));
            this.optionsDrawable = ContextCompat.getDrawable(context, R.drawable.media_more);
            this.pipDrawable = ContextCompat.getDrawable(context, R.drawable.menu_stream_pip);
            this.deleteDrawable = ContextCompat.getDrawable(context, R.drawable.msg_delete);
            this.muteDrawable = new RLottieDrawable(R.raw.media_mute_unmute, "media_mute_unmute", AndroidUtilities.dp(28.0f), AndroidUtilities.dp(28.0f), true, null);
            RLottieDrawable rLottieDrawable = new RLottieDrawable(R.raw.media_mute_unmute, "media_mute_unmute", AndroidUtilities.dp(28.0f), AndroidUtilities.dp(28.0f), true, null);
            this.noSoundDrawable = rLottieDrawable;
            rLottieDrawable.setCurrentFrame(20, false, true);
            this.noSoundDrawable.stop();
            Paint paint = new Paint(1);
            this.barPaint = paint;
            paint.setColor(1442840575);
            Paint paint2 = new Paint(1);
            this.selectedBarPaint = paint2;
            paint2.setColor(-1);
            int alphaComponent = ColorUtils.setAlphaComponent(-16777216, 102);
            this.topOverlayGradient = ContextCompat.getDrawable(context, R.drawable.shadow_story_top);
            this.bottomOverlayGradient = ContextCompat.getDrawable(context, R.drawable.shadow_story_bottom);
            Paint paint3 = new Paint();
            this.gradientBackgroundPaint = paint3;
            paint3.setColor(alphaComponent);
            this.imageBackgroundDrawable = new ColorDrawable(ColorUtils.blendARGB(-16777216, -1, 0.1f));
        }

        public void setIconMuted(boolean z, boolean z2) {
            if (!z2) {
                this.muteDrawable.setCurrentFrame(z ? 20 : 0, false);
                this.muteDrawable.setCustomEndFrame(z ? 20 : 0);
                return;
            }
            if (z) {
                if (this.muteDrawable.getCurrentFrame() > 20) {
                    this.muteDrawable.setCurrentFrame(0, false);
                }
                this.muteDrawable.setCustomEndFrame(20);
                this.muteDrawable.start();
                return;
            }
            if (this.muteDrawable.getCurrentFrame() == 0 || this.muteDrawable.getCurrentFrame() >= 43) {
                return;
            }
            this.muteDrawable.setCustomEndFrame(43);
            this.muteDrawable.start();
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void editPrivacy(StoryPrivacyBottomSheet.StoryPrivacy storyPrivacy, final TL_stories.StoryItem storyItem) {
        this.delegate.showDialog(new StoryPrivacyBottomSheet(getContext(), storyItem.pinned ? Integer.MAX_VALUE : storyItem.expire_date - storyItem.date, this.resourcesProvider).setValue(storyPrivacy).enableSharing(false).isEdit(true).whenSelectedRules(new StoryPrivacyBottomSheet.DoneCallback() { // from class: org.telegram.ui.Stories.PeerStoriesView$$ExternalSyntheticLambda43
            @Override // org.telegram.ui.Stories.recorder.StoryPrivacyBottomSheet.DoneCallback
            public final void done(StoryPrivacyBottomSheet.StoryPrivacy storyPrivacy2, boolean z, boolean z2, boolean z3, boolean z4, TLRPC.InputPeer inputPeer, int i, Runnable runnable, Runnable runnable2) {
                this.f$0.lambda$editPrivacy$57(storyItem, storyPrivacy2, z, z2, z3, z4, inputPeer, i, runnable, runnable2);
            }
        }, false));
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$editPrivacy$57(final TL_stories.StoryItem storyItem, final StoryPrivacyBottomSheet.StoryPrivacy storyPrivacy, boolean z, boolean z2, boolean z3, boolean z4, TLRPC.InputPeer inputPeer, int i, final Runnable runnable, Runnable runnable2) {
        TL_stories.TL_stories_editStory tL_stories_editStory = new TL_stories.TL_stories_editStory();
        tL_stories_editStory.peer = MessagesController.getInstance(this.currentAccount).getInputPeer(storyItem.dialogId);
        tL_stories_editStory.id = storyItem.id;
        tL_stories_editStory.flags |= 4;
        tL_stories_editStory.privacy_rules = storyPrivacy.rules;
        ConnectionsManager.getInstance(this.currentAccount).sendRequest(tL_stories_editStory, new RequestDelegate() { // from class: org.telegram.ui.Stories.PeerStoriesView$$ExternalSyntheticLambda48
            @Override // org.telegram.tgnet.RequestDelegate
            public final void run(TLObject tLObject, TLRPC.TL_error tL_error) {
                this.f$0.lambda$editPrivacy$56(runnable, storyItem, storyPrivacy, tLObject, tL_error);
            }
        });
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$editPrivacy$56(final Runnable runnable, final TL_stories.StoryItem storyItem, final StoryPrivacyBottomSheet.StoryPrivacy storyPrivacy, TLObject tLObject, final TLRPC.TL_error tL_error) {
        AndroidUtilities.runOnUIThread(new Runnable() { // from class: org.telegram.ui.Stories.PeerStoriesView$$ExternalSyntheticLambda52
            @Override // java.lang.Runnable
            public final void run() {
                this.f$0.lambda$editPrivacy$55(runnable, tL_error, storyItem, storyPrivacy);
            }
        });
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$editPrivacy$55(Runnable runnable, TLRPC.TL_error tL_error, TL_stories.StoryItem storyItem, StoryPrivacyBottomSheet.StoryPrivacy storyPrivacy) {
        if (runnable != null) {
            runnable.run();
        }
        if (tL_error == null || "STORY_NOT_MODIFIED".equals(tL_error.text)) {
            storyItem.parsedPrivacy = storyPrivacy;
            storyItem.privacy = storyPrivacy.toValue();
            int i = storyPrivacy.type;
            storyItem.close_friends = i == 1;
            storyItem.contacts = i == 2;
            storyItem.selected_contacts = i == 3;
            MessagesController.getInstance(this.currentAccount).getStoriesController().updateStoryItem(storyItem.dialogId, storyItem, true, true);
            this.editedPrivacy = true;
            int i2 = storyPrivacy.type;
            if (i2 == 4) {
                BulletinFactory.of(this.storyContainer, this.resourcesProvider).createSimpleBulletin(R.raw.contact_check, LocaleController.getString("StorySharedToEveryone")).show();
            } else if (i2 == 1) {
                BulletinFactory.of(this.storyContainer, this.resourcesProvider).createSimpleBulletin(R.raw.contact_check, LocaleController.getString("StorySharedToCloseFriends")).show();
            } else if (i2 == 2) {
                if (storyPrivacy.selectedUserIds.isEmpty()) {
                    BulletinFactory.of(this.storyContainer, this.resourcesProvider).createSimpleBulletin(R.raw.contact_check, LocaleController.getString("StorySharedToAllContacts")).show();
                } else {
                    BulletinFactory.of(this.storyContainer, this.resourcesProvider).createSimpleBulletin(R.raw.contact_check, LocaleController.formatPluralString("StorySharedToAllContactsExcluded", storyPrivacy.selectedUserIds.size(), new Object[0])).show();
                }
            } else if (i2 == 3) {
                HashSet hashSet = new HashSet();
                hashSet.addAll(storyPrivacy.selectedUserIds);
                Iterator it = storyPrivacy.selectedUserIdsByGroup.values().iterator();
                while (it.hasNext()) {
                    hashSet.addAll((ArrayList) it.next());
                }
                BulletinFactory.of(this.storyContainer, this.resourcesProvider).createSimpleBulletin(R.raw.contact_check, LocaleController.formatPluralString("StorySharedToContacts", hashSet.size(), new Object[0])).show();
            }
        } else {
            BulletinFactory.of(this.storyContainer, this.resourcesProvider).createSimpleBulletin(R.raw.error, LocaleController.getString(R.string.UnknownError)).show();
        }
        updatePosition();
    }

    public boolean checkRecordLocked(final boolean z) {
        ChatActivityEnterView chatActivityEnterView = this.chatActivityEnterView;
        if (chatActivityEnterView == null || !chatActivityEnterView.isRecordLocked()) {
            return false;
        }
        AlertDialog.Builder builder = new AlertDialog.Builder(getContext(), this.resourcesProvider);
        if (this.chatActivityEnterView.isInVideoMode()) {
            builder.setTitle(LocaleController.getString(R.string.DiscardVideoMessageTitle));
            builder.setMessage(LocaleController.getString(R.string.DiscardVideoMessageDescription));
        } else {
            builder.setTitle(LocaleController.getString(R.string.DiscardVoiceMessageTitle));
            builder.setMessage(LocaleController.getString(R.string.DiscardVoiceMessageDescription));
        }
        builder.setPositiveButton(LocaleController.getString(R.string.DiscardVoiceMessageAction), new AlertDialog.OnButtonClickListener() { // from class: org.telegram.ui.Stories.PeerStoriesView$$ExternalSyntheticLambda6
            @Override // org.telegram.ui.ActionBar.AlertDialog.OnButtonClickListener
            public final void onClick(AlertDialog alertDialog, int i) {
                this.f$0.lambda$checkRecordLocked$58(z, alertDialog, i);
            }
        });
        builder.setNegativeButton(LocaleController.getString(R.string.Continue), null);
        this.delegate.showDialog(builder.create());
        return true;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$checkRecordLocked$58(boolean z, AlertDialog alertDialog, int i) {
        ChatActivityEnterView chatActivityEnterView = this.chatActivityEnterView;
        if (chatActivityEnterView != null) {
            if (z) {
                this.storyViewer.close(true);
            } else {
                chatActivityEnterView.cancelRecordingAudioVideo();
            }
        }
    }

    public void animateOut(final boolean z) {
        ValueAnimator valueAnimator = this.outAnimator;
        if (valueAnimator != null) {
            valueAnimator.cancel();
        }
        ValueAnimator valueAnimatorOfFloat = ValueAnimator.ofFloat(this.outT, z ? 1.0f : 0.0f);
        this.outAnimator = valueAnimatorOfFloat;
        valueAnimatorOfFloat.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() { // from class: org.telegram.ui.Stories.PeerStoriesView$$ExternalSyntheticLambda57
            @Override // android.animation.ValueAnimator.AnimatorUpdateListener
            public final void onAnimationUpdate(ValueAnimator valueAnimator2) {
                this.f$0.lambda$animateOut$59(valueAnimator2);
            }
        });
        this.outAnimator.addListener(new AnimatorListenerAdapter() { // from class: org.telegram.ui.Stories.PeerStoriesView.41
            @Override // android.animation.AnimatorListenerAdapter, android.animation.Animator.AnimatorListener
            public void onAnimationEnd(Animator animator) {
                PeerStoriesView.this.outT = z ? 1.0f : 0.0f;
                PeerStoriesView.this.headerView.setTranslationY((-AndroidUtilities.dp(8.0f)) * PeerStoriesView.this.outT);
                PeerStoriesView peerStoriesView = PeerStoriesView.this;
                peerStoriesView.headerView.setAlpha(1.0f - peerStoriesView.outT);
                PeerStoriesView.this.optionsIconView.setTranslationY((-AndroidUtilities.dp(8.0f)) * PeerStoriesView.this.outT);
                PeerStoriesView.this.optionsIconView.setAlpha(1.0f - PeerStoriesView.this.outT);
                PeerStoriesView.this.pipIconView.setTranslationY((-AndroidUtilities.dp(8.0f)) * PeerStoriesView.this.outT);
                PeerStoriesView.this.pipIconView.setAlpha(1.0f - PeerStoriesView.this.outT);
                PeerStoriesView.this.muteIconContainer.setTranslationY((-AndroidUtilities.dp(8.0f)) * PeerStoriesView.this.outT);
                PeerStoriesView.this.muteIconContainer.setAlpha(PeerStoriesView.this.muteIconViewAlpha * (1.0f - PeerStoriesView.this.outT));
                if (PeerStoriesView.this.selfView != null) {
                    PeerStoriesView.this.selfView.setTranslationY(AndroidUtilities.dp(8.0f) * PeerStoriesView.this.outT);
                    PeerStoriesView.this.selfView.setAlpha(1.0f - PeerStoriesView.this.outT);
                }
                if (PeerStoriesView.this.privacyButton != null) {
                    PeerStoriesView.this.privacyButton.setTranslationY((-AndroidUtilities.dp(8.0f)) * PeerStoriesView.this.outT);
                    PeerStoriesView.this.privacyButton.setAlpha(1.0f - PeerStoriesView.this.outT);
                }
                PeerStoriesView.this.storyCaptionView.setAlpha(1.0f - PeerStoriesView.this.outT);
                Delegate delegate = PeerStoriesView.this.delegate;
                float progressToDismiss = delegate != null ? delegate.getProgressToDismiss() : 0.0f;
                float hideInterfaceAlpha = PeerStoriesView.this.getHideInterfaceAlpha();
                if (PeerStoriesView.this.likeButtonContainer != null) {
                    PeerStoriesView.this.likeButtonContainer.setAlpha((1.0f - progressToDismiss) * hideInterfaceAlpha * (1.0f - PeerStoriesView.this.outT));
                }
                if (PeerStoriesView.this.shareButton != null) {
                    PeerStoriesView.this.shareButton.setAlpha((1.0f - progressToDismiss) * hideInterfaceAlpha * (1.0f - PeerStoriesView.this.outT));
                }
                if (PeerStoriesView.this.repostButtonContainer != null) {
                    PeerStoriesView.this.repostButtonContainer.setAlpha(hideInterfaceAlpha * (1.0f - progressToDismiss) * (1.0f - PeerStoriesView.this.outT));
                }
                PeerStoriesView peerStoriesView2 = PeerStoriesView.this;
                ChatActivityEnterView chatActivityEnterView = peerStoriesView2.chatActivityEnterView;
                if (chatActivityEnterView != null) {
                    chatActivityEnterView.setAlpha(1.0f - peerStoriesView2.outT);
                    PeerStoriesView.this.invalidate();
                }
                PeerStoriesView.this.storyContainer.invalidate();
            }
        });
        this.outAnimator.setDuration(420L);
        this.outAnimator.setInterpolator(CubicBezierInterpolator.EASE_OUT_QUINT);
        this.outAnimator.start();
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$animateOut$59(ValueAnimator valueAnimator) {
        this.outT = ((Float) valueAnimator.getAnimatedValue()).floatValue();
        this.headerView.setTranslationY((-AndroidUtilities.dp(8.0f)) * this.outT);
        this.headerView.setAlpha(1.0f - this.outT);
        this.optionsIconView.setTranslationY((-AndroidUtilities.dp(8.0f)) * this.outT);
        this.optionsIconView.setAlpha(1.0f - this.outT);
        this.pipIconView.setTranslationY((-AndroidUtilities.dp(8.0f)) * this.outT);
        this.pipIconView.setAlpha(1.0f - this.outT);
        this.muteIconContainer.setTranslationY((-AndroidUtilities.dp(8.0f)) * this.outT);
        this.muteIconContainer.setAlpha(this.muteIconViewAlpha * (1.0f - this.outT));
        FrameLayout frameLayout = this.selfView;
        if (frameLayout != null) {
            frameLayout.setTranslationY(AndroidUtilities.dp(8.0f) * this.outT);
            this.selfView.setAlpha(1.0f - this.outT);
        }
        StoryPrivacyButton storyPrivacyButton = this.privacyButton;
        if (storyPrivacyButton != null) {
            storyPrivacyButton.setTranslationY((-AndroidUtilities.dp(8.0f)) * this.outT);
            this.privacyButton.setAlpha(1.0f - this.outT);
        }
        this.storyCaptionView.setAlpha(1.0f - this.outT);
        Delegate delegate = this.delegate;
        float progressToDismiss = delegate == null ? 0.0f : delegate.getProgressToDismiss();
        float hideInterfaceAlpha = getHideInterfaceAlpha();
        FrameLayout frameLayout2 = this.likeButtonContainer;
        if (frameLayout2 != null) {
            frameLayout2.setAlpha((1.0f - progressToDismiss) * hideInterfaceAlpha * (1.0f - this.outT));
        }
        ImageView imageView = this.shareButton;
        if (imageView != null) {
            imageView.setAlpha((1.0f - progressToDismiss) * hideInterfaceAlpha * (1.0f - this.outT));
        }
        FrameLayout frameLayout3 = this.repostButtonContainer;
        if (frameLayout3 != null) {
            frameLayout3.setAlpha(hideInterfaceAlpha * (1.0f - progressToDismiss) * (1.0f - this.outT));
        }
        ChatActivityEnterView chatActivityEnterView = this.chatActivityEnterView;
        if (chatActivityEnterView != null) {
            chatActivityEnterView.setAlpha(1.0f - this.outT);
            invalidate();
        }
        this.storyContainer.invalidate();
    }

    public int getListPosition() {
        return this.listPosition;
    }

    public StoriesController getStoriesController() {
        return MessagesController.getInstance(this.currentAccount).getStoriesController();
    }
}
