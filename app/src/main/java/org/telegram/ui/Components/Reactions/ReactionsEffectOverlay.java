package org.telegram.ui.Components.Reactions;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Point;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffColorFilter;
import android.graphics.Rect;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.FrameLayout;
import com.exteragram.messenger.ExteraConfig;
import java.util.ArrayList;
import java.util.Random;
import org.telegram.messenger.AndroidUtilities;
import org.telegram.messenger.ChatObject;
import org.telegram.messenger.ImageLocation;
import org.telegram.messenger.ImageReceiver;
import org.telegram.messenger.LiteMode;
import org.telegram.messenger.MediaDataController;
import org.telegram.messenger.MessageObject;
import org.telegram.messenger.MessagesController;
import org.telegram.messenger.R;
import org.telegram.messenger.SharedConfig;
import org.telegram.tgnet.TLRPC;
import org.telegram.ui.ActionBar.ActionBarPopupWindow;
import org.telegram.ui.ActionBar.BaseFragment;
import org.telegram.ui.ActionBar.Theme;
import org.telegram.ui.Cells.ChatActionCell;
import org.telegram.ui.Cells.ChatMessageCell;
import org.telegram.ui.ChatActivity;
import org.telegram.ui.Components.AnimatedEmojiDrawable;
import org.telegram.ui.Components.AvatarDrawable;
import org.telegram.ui.Components.BackupImageView;
import org.telegram.ui.Components.CubicBezierInterpolator;
import org.telegram.ui.Components.RLottieDrawable;
import org.telegram.ui.Components.ReactionsContainerLayout;
import org.telegram.ui.Components.conference.message.GroupCallMessageCell;
import org.telegram.ui.SelectAnimatedEmojiDialog;

public class ReactionsEffectOverlay {
    public static ReactionsEffectOverlay currentOverlay;
    public static ReactionsEffectOverlay currentShortOverlay;
    private static long lastHapticTime;
    private static int uniqPrefix;
    float animateInProgress;
    float animateOutProgress;
    private final int animationType;
    private View cell;
    private final FrameLayout container;
    private final int currentAccount;
    private ViewGroup decorView;
    private float dismissProgress;
    private boolean dismissed;
    private final AnimationView effectImageView;
    private final AnimationView emojiImageView;
    private final AnimationView emojiStaticImageView;
    private final long groupId;
    private ReactionsContainerLayout.ReactionHolderView holderView;
    boolean isFinished;
    public boolean isStories;
    private float lastDrawnToX;
    private float lastDrawnToY;
    private final int messageId;
    private ReactionsEffectOverlay nextReactionOverlay;
    private final ReactionsLayoutInBubble.VisibleReaction reaction;
    public long startTime;
    public boolean started;
    private boolean useWindow;
    private boolean wasScrolled;
    private WindowManager windowManager;
    public FrameLayout windowView;
    int[] loc = new int[2];
    private SelectAnimatedEmojiDialog.ImageViewEmoji holderView2 = null;
    ArrayList avatars = new ArrayList();

    /* JADX WARN: Code duplicated, block: B:254:0x0280 A[SYNTHETIC] */
    /* JADX WARN: Code duplicated, block: B:257:0x0274 A[SYNTHETIC] */
    /* JADX WARN: Code duplicated, block: B:57:0x015d  */
    /* JADX WARN: Code duplicated, block: B:65:0x01ec  */
    /* JADX WARN: Code duplicated, block: B:66:0x0211  */
    /* JADX WARN: Code duplicated, block: B:69:0x021f  */
    /* JADX WARN: Code duplicated, block: B:72:0x0250  */
    /* JADX WARN: Code duplicated, block: B:74:0x0272  */
    /* JADX WARN: Code duplicated, block: B:78:0x027b  */
    /* JADX WARN: Code duplicated, block: B:86:0x02ae  */
    /* JADX WARN: Multi-variable type inference failed */
    /* JADX WARN: Type inference failed for: r7v36 */
    /* JADX WARN: Type inference failed for: r7v37 */
    /* JADX WARN: Type inference failed for: r7v38, types: [boolean, int] */
    /* JADX WARN: Type inference failed for: r7v55 */
    public ReactionsEffectOverlay(Context context, BaseFragment baseFragment, ReactionsContainerLayout reactionsContainerLayout, View view, View view2, float f, float f2, ReactionsLayoutInBubble.VisibleReaction visibleReaction, int i, int i2, boolean z) {
        MessageObject messageObject;
        ReactionsLayoutInBubble.ReactionButton reactionButton;
        Context context2;
        View view3;
        ReactionsContainerLayout reactionsContainerLayout2;
        View view4;
        BaseFragment baseFragment2;
        ChatActivity chatActivity;
        int i3;
        float f3;
        float f4;
        float f5;
        float imageHeight;
        float imageX;
        float imageY;
        int i4;
        int iRound;
        int iSizeForBigReaction;
        int i5;
        int i6;
        int color;
        ?? r7;
        int i7;
        boolean z2;
        boolean z3;
        ChatActivity chatActivity2;
        int i8;
        Random random;
        AvatarParticle avatarParticle;
        float f6;
        float f7;
        float f8;
        int i9;
        float fAbs;
        float fAbs2;
        float f9;
        int i10;
        float f10;
        TLRPC.TL_messageReactions tL_messageReactions;
        int i11 = i;
        this.holderView = null;
        this.isStories = z;
        boolean z4 = view instanceof ChatMessageCell;
        if (z4) {
            messageObject = ((ChatMessageCell) view).getMessageObject();
            this.messageId = messageObject.getId();
            this.groupId = messageObject.getGroupId();
        } else if (view instanceof ChatActionCell) {
            messageObject = ((ChatActionCell) view).getMessageObject();
            this.messageId = messageObject.getId();
            this.groupId = 0L;
        } else {
            this.messageId = 0;
            this.groupId = 0L;
            messageObject = null;
        }
        this.reaction = visibleReaction;
        this.animationType = i2;
        this.currentAccount = i11;
        this.cell = view;
        if (z4) {
            reactionButton = ((ChatMessageCell) view).getReactionButton(visibleReaction);
        } else {
            reactionButton = view instanceof ChatActionCell ? ((ChatActionCell) view).getReactionButton(visibleReaction) : null;
        }
        if (z && i2 == 2) {
            view3 = view2;
            reactionsContainerLayout2 = reactionsContainerLayout;
            view4 = view;
            baseFragment2 = baseFragment;
            ReactionsEffectOverlay reactionsEffectOverlay = new ReactionsEffectOverlay(context, baseFragment2, reactionsContainerLayout2, view4, view3, f, f2, visibleReaction, i11, 1, true);
            context2 = context;
            this.nextReactionOverlay = reactionsEffectOverlay;
            currentShortOverlay = reactionsEffectOverlay;
        } else {
            context2 = context;
            view3 = view2;
            reactionsContainerLayout2 = reactionsContainerLayout;
            view4 = view;
            baseFragment2 = baseFragment;
        }
        ChatActivity chatActivity3 = baseFragment2 instanceof ChatActivity ? (ChatActivity) baseFragment2 : null;
        if (reactionsContainerLayout2 != null) {
            for (int i12 = 0; i12 < reactionsContainerLayout2.recyclerListView.getChildCount(); i12++) {
                if ((reactionsContainerLayout2.recyclerListView.getChildAt(i12) instanceof ReactionsContainerLayout.ReactionHolderView) && ((ReactionsContainerLayout.ReactionHolderView) reactionsContainerLayout2.recyclerListView.getChildAt(i12)).currentReaction.equals(this.reaction)) {
                    this.holderView = (ReactionsContainerLayout.ReactionHolderView) reactionsContainerLayout2.recyclerListView.getChildAt(i12);
                    break;
                }
            }
        }
        int i13 = 1;
        if (i2 == 1) {
            Random random2 = new Random();
            ArrayList arrayList = (messageObject == 0 || (tL_messageReactions = messageObject.messageOwner.reactions) == null) ? null : tL_messageReactions.recent_reactions;
            if (arrayList == null || chatActivity3 == null || chatActivity3.getDialogId() >= r5) {
                chatActivity = chatActivity3;
                i3 = 1;
                f3 = 0.8f;
            } else {
                f3 = 0.8f;
                int i14 = 0;
                while (i14 < arrayList.size()) {
                    if (this.reaction.equals(((TLRPC.MessagePeerReaction) arrayList.get(i14)).reaction) && ((TLRPC.MessagePeerReaction) arrayList.get(i14)).unread) {
                        AvatarDrawable avatarDrawable = new AvatarDrawable();
                        ImageReceiver imageReceiver = new ImageReceiver();
                        i8 = i13;
                        random = random2;
                        long peerId = MessageObject.getPeerId(((TLRPC.MessagePeerReaction) arrayList.get(i14)).peer_id);
                        if (peerId < 0) {
                            TLRPC.Chat chat = MessagesController.getInstance(i11).getChat(Long.valueOf(-peerId));
                            if (chat == null) {
                                chatActivity2 = chatActivity3;
                            } else {
                                avatarDrawable.setInfo(i11, chat);
                                imageReceiver.setForUserOrChat(chat, avatarDrawable);
                                avatarParticle = new AvatarParticle();
                                avatarParticle.imageReceiver = imageReceiver;
                                avatarParticle.fromX = 0.5f;
                                avatarParticle.fromY = 0.5f;
                                avatarParticle.jumpY = ((Math.abs(random.nextInt() % 100) / 100.0f) * 0.1f) + 0.3f;
                                avatarParticle.randomScale = ((Math.abs(random.nextInt() % 100) / 100.0f) * 0.4f) + 0.8f;
                                avatarParticle.randomRotation = (Math.abs(random.nextInt() % 100) * 60) / 100.0f;
                                avatarParticle.leftTime = (int) (((Math.abs(random.nextInt() % 100) / 100.0f) * 200.0f) + 400.0f);
                                if (this.avatars.isEmpty()) {
                                    avatarParticle.toX = ((Math.abs(random.nextInt() % 100) * 0.6f) / 100.0f) + 0.2f;
                                    avatarParticle.toY = (Math.abs(random.nextInt() % 100) * 0.4f) / 100.0f;
                                    chatActivity2 = chatActivity3;
                                } else {
                                    f6 = 0.0f;
                                    f7 = 0.0f;
                                    f8 = 0.0f;
                                    i9 = 0;
                                    while (i9 < 10) {
                                        fAbs = ((Math.abs(random.nextInt() % 100) * 0.6f) / 100.0f) + 0.2f;
                                        fAbs2 = ((Math.abs(random.nextInt() % 100) * 0.4f) / 100.0f) + 0.2f;
                                        f9 = 2.1474836E9f;
                                        ChatActivity chatActivity4 = chatActivity3;
                                        for (i10 = 0; i10 < this.avatars.size(); i10++) {
                                            float f11 = ((AvatarParticle) this.avatars.get(i10)).toX - fAbs;
                                            float f12 = ((AvatarParticle) this.avatars.get(i10)).toY - fAbs2;
                                            f10 = (f11 * f11) + (f12 * f12);
                                            if (f10 < f9) {
                                                f9 = f10;
                                            }
                                        }
                                        if (f9 > f8) {
                                            f6 = fAbs;
                                            f8 = f9;
                                            f7 = fAbs2;
                                        }
                                        i9++;
                                        chatActivity3 = chatActivity4;
                                    }
                                    chatActivity2 = chatActivity3;
                                    avatarParticle.toX = f6;
                                    avatarParticle.toY = f7;
                                }
                                this.avatars.add(avatarParticle);
                            }
                        } else {
                            TLRPC.User user = MessagesController.getInstance(i11).getUser(Long.valueOf(peerId));
                            if (user == null) {
                                chatActivity2 = chatActivity3;
                            } else {
                                avatarDrawable.setInfo(i11, user);
                                imageReceiver.setForUserOrChat(user, avatarDrawable);
                                avatarParticle = new AvatarParticle();
                                avatarParticle.imageReceiver = imageReceiver;
                                avatarParticle.fromX = 0.5f;
                                avatarParticle.fromY = 0.5f;
                                avatarParticle.jumpY = ((Math.abs(random.nextInt() % 100) / 100.0f) * 0.1f) + 0.3f;
                                avatarParticle.randomScale = ((Math.abs(random.nextInt() % 100) / 100.0f) * 0.4f) + 0.8f;
                                avatarParticle.randomRotation = (Math.abs(random.nextInt() % 100) * 60) / 100.0f;
                                avatarParticle.leftTime = (int) (((Math.abs(random.nextInt() % 100) / 100.0f) * 200.0f) + 400.0f);
                                if (this.avatars.isEmpty()) {
                                    avatarParticle.toX = ((Math.abs(random.nextInt() % 100) * 0.6f) / 100.0f) + 0.2f;
                                    avatarParticle.toY = (Math.abs(random.nextInt() % 100) * 0.4f) / 100.0f;
                                    chatActivity2 = chatActivity3;
                                } else {
                                    f6 = 0.0f;
                                    f7 = 0.0f;
                                    f8 = 0.0f;
                                    i9 = 0;
                                    while (i9 < 10) {
                                        fAbs = ((Math.abs(random.nextInt() % 100) * 0.6f) / 100.0f) + 0.2f;
                                        fAbs2 = ((Math.abs(random.nextInt() % 100) * 0.4f) / 100.0f) + 0.2f;
                                        f9 = 2.1474836E9f;
                                        ChatActivity chatActivity5 = chatActivity3;
                                        while (i10 < this.avatars.size()) {
                                            float f13 = ((AvatarParticle) this.avatars.get(i10)).toX - fAbs;
                                            float f14 = ((AvatarParticle) this.avatars.get(i10)).toY - fAbs2;
                                            f10 = (f13 * f13) + (f14 * f14);
                                            if (f10 < f9) {
                                                f9 = f10;
                                            }
                                        }
                                        if (f9 > f8) {
                                            f6 = fAbs;
                                            f8 = f9;
                                            f7 = fAbs2;
                                        }
                                        i9++;
                                        chatActivity3 = chatActivity5;
                                    }
                                    chatActivity2 = chatActivity3;
                                    avatarParticle.toX = f6;
                                    avatarParticle.toY = f7;
                                }
                                this.avatars.add(avatarParticle);
                            }
                        }
                    } else {
                        chatActivity2 = chatActivity3;
                        i8 = i13;
                        random = random2;
                    }
                    i14++;
                    i11 = i;
                    random2 = random;
                    i13 = i8;
                    chatActivity3 = chatActivity2;
                }
                chatActivity = chatActivity3;
                i3 = i13;
            }
        } else {
            chatActivity = chatActivity3;
            i3 = 1;
            f3 = 0.8f;
        }
        ReactionsContainerLayout.ReactionHolderView reactionHolderView = this.holderView;
        boolean z5 = (reactionHolderView == null && (f == 0.0f || f2 == 0.0f)) ? 0 : i3;
        if (view3 != null) {
            view3.getLocationOnScreen(this.loc);
            int[] iArr = this.loc;
            float width = iArr[0];
            float width2 = iArr[i3];
            imageHeight = view3.getWidth() * view3.getScaleX();
            if (view3 instanceof SelectAnimatedEmojiDialog.ImageViewEmoji) {
                float f15 = ((SelectAnimatedEmojiDialog.ImageViewEmoji) view3).bigReactionSelectedProgress;
                if (f15 > 0.0f) {
                    imageHeight = view3.getWidth() * ((f15 * 2.0f) + 1.0f);
                    width -= (imageHeight - view3.getWidth()) / 2.0f;
                    width2 -= imageHeight - view3.getWidth();
                }
            }
            f5 = width2;
            f4 = width;
        } else {
            if (reactionHolderView != null) {
                reactionHolderView.getLocationOnScreen(this.loc);
                imageX = this.loc[0] + this.holderView.loopImageView.getX();
                imageY = this.loc[i3] + this.holderView.loopImageView.getY();
                imageHeight = this.holderView.getScaleX() * this.holderView.loopImageView.getWidth();
            } else if (reactionButton != 0) {
                view4.getLocationInWindow(this.loc);
                float f16 = this.loc[0];
                ImageReceiver imageReceiver2 = reactionButton.imageReceiver;
                imageX = f16 + (imageReceiver2 == null ? 0.0f : imageReceiver2.getImageX());
                float f17 = this.loc[i3];
                ImageReceiver imageReceiver3 = reactionButton.imageReceiver;
                imageY = f17 + (imageReceiver3 == null ? 0.0f : imageReceiver3.getImageY());
                ImageReceiver imageReceiver4 = reactionButton.imageReceiver;
                imageHeight = imageReceiver4 == null ? 0.0f : imageReceiver4.getImageHeight();
            } else {
                if (view4 != null) {
                    ((View) view4.getParent()).getLocationInWindow(this.loc);
                    int[] iArr2 = this.loc;
                    float f18 = iArr2[0] + f;
                    f5 = iArr2[i3] + f2 + (view4 instanceof ChatMessageCell ? ((ChatMessageCell) view4).starsPriceTopPadding : 0);
                    f4 = f18;
                } else {
                    f4 = f;
                    f5 = f2;
                }
                imageHeight = 0.0f;
            }
            f4 = imageX;
            f5 = imageY;
        }
        if (i2 == 2) {
            int iDp = AndroidUtilities.dp((z && SharedConfig.deviceIsHigh()) ? 60.0f : 34.0f);
            iSizeForBigReaction = (int) ((iDp * 2.0f) / AndroidUtilities.density);
            iRound = iDp;
            i4 = i3;
        } else {
            i4 = i3;
            if (i2 != i4) {
                int iDp2 = AndroidUtilities.dp(350.0f);
                Point point = AndroidUtilities.displaySize;
                iRound = Math.round(Math.min(iDp2, Math.min(point.x, point.y)) * f3);
                iSizeForBigReaction = sizeForBigReaction();
            } else if (z) {
                int iDp3 = AndroidUtilities.dp(SharedConfig.deviceIsHigh() ? 240.0f : 140.0f);
                iSizeForBigReaction = SharedConfig.deviceIsHigh() ? (int) ((AndroidUtilities.dp(80.0f) * 2.0f) / AndroidUtilities.density) : sizeForAroundReaction();
                iRound = iDp3;
            } else {
                iRound = AndroidUtilities.dp(80.0f);
                iSizeForBigReaction = sizeForAroundReaction();
            }
        }
        float f19 = imageHeight;
        int i15 = iRound >> 1;
        int i16 = iSizeForBigReaction >> 1;
        float f20 = f19 / i15;
        this.animateInProgress = 0.0f;
        this.animateOutProgress = 0.0f;
        FrameLayout frameLayout = new FrameLayout(context2);
        this.container = frameLayout;
        int i17 = iRound;
        int i18 = iSizeForBigReaction;
        View view5 = view4;
        MessageObject messageObject2 = messageObject;
        boolean z6 = i4;
        this.windowView = new AnonymousClass1(context2, baseFragment, view5, z, messageObject2, chatActivity, i15, i2, z5, f20, f4, f5, visibleReaction);
        AnimationView animationView = new AnimationView(context2);
        this.effectImageView = animationView;
        AnimationView animationView2 = new AnimationView(context2);
        this.emojiImageView = animationView2;
        AnimationView animationView3 = new AnimationView(context2);
        this.emojiStaticImageView = animationView3;
        TLRPC.TL_availableReaction tL_availableReaction = visibleReaction.emojicon != null ? MediaDataController.getInstance(i).getReactionsMap().get(this.reaction.emojicon) : null;
        if (tL_availableReaction != null || visibleReaction.documentId != r5) {
            if (tL_availableReaction != null) {
                int i19 = 2;
                if (i2 != 2) {
                    if ((i2 == z6 && LiteMode.isEnabled(LiteMode.FLAG_ANIMATED_EMOJI_CHAT)) || i2 == 0) {
                        TLRPC.Document document = i2 == z6 ? tL_availableReaction.around_animation : tL_availableReaction.effect_animation;
                        String filterForAroundAnimation = i2 != z6 ? i18 + "_" + i18 : getFilterForAroundAnimation();
                        ImageReceiver imageReceiver5 = animationView.getImageReceiver();
                        StringBuilder sb = new StringBuilder();
                        int i20 = uniqPrefix;
                        uniqPrefix = i20 + 1;
                        sb.append(i20);
                        sb.append("_");
                        sb.append(this.messageId);
                        sb.append("_");
                        imageReceiver5.setUniqKeyPrefix(sb.toString());
                        animationView.setImage(ImageLocation.getForDocument(document), filterForAroundAnimation, (ImageLocation) null, (String) null, 0, (Object) null);
                        z3 = false;
                        animationView.getImageReceiver().setAutoRepeat(0);
                        animationView.getImageReceiver().setAllowStartAnimation(false);
                    } else {
                        z3 = false;
                    }
                    if (animationView.getImageReceiver().getLottieAnimation() != null) {
                        animationView.getImageReceiver().getLottieAnimation().setCurrentFrame(z3 ? 1 : 0, z3);
                        animationView.getImageReceiver().getLottieAnimation().start();
                    }
                    i19 = 2;
                    z2 = z3;
                } else {
                    z2 = false;
                }
                if (i2 == i19) {
                    TLRPC.Document document2 = z ? tL_availableReaction.select_animation : tL_availableReaction.appear_animation;
                    ImageReceiver imageReceiver6 = animationView2.getImageReceiver();
                    StringBuilder sb2 = new StringBuilder();
                    int i21 = uniqPrefix;
                    uniqPrefix = i21 + 1;
                    sb2.append(i21);
                    sb2.append("_");
                    sb2.append(this.messageId);
                    sb2.append("_");
                    imageReceiver6.setUniqKeyPrefix(sb2.toString());
                    animationView2.setImage(ImageLocation.getForDocument(document2), i16 + "_" + i16, (ImageLocation) null, (String) null, 0, (Object) null);
                } else if (i2 == 0) {
                    TLRPC.Document document3 = tL_availableReaction.activate_animation;
                    ImageReceiver imageReceiver7 = animationView2.getImageReceiver();
                    StringBuilder sb3 = new StringBuilder();
                    int i22 = uniqPrefix;
                    uniqPrefix = i22 + 1;
                    sb3.append(i22);
                    sb3.append("_");
                    sb3.append(this.messageId);
                    sb3.append("_");
                    imageReceiver7.setUniqKeyPrefix(sb3.toString());
                    animationView2.setImage(ImageLocation.getForDocument(document3), i16 + "_" + i16, (ImageLocation) null, (String) null, 0, (Object) null);
                }
                i15 = i15;
                r7 = z2;
            } else {
                if (i2 == 0) {
                    i5 = i;
                    animationView2.setAnimatedReactionDrawable(new AnimatedEmojiDrawable(z6 ? 1 : 0, i5, visibleReaction.documentId));
                    i6 = 2;
                } else {
                    i5 = i;
                    i6 = 2;
                    if (i2 == 2) {
                        animationView2.setAnimatedReactionDrawable(new AnimatedEmojiDrawable(2, i5, visibleReaction.documentId));
                    }
                }
                if (i2 == 0 || i2 == z6) {
                    AnimatedEmojiDrawable animatedEmojiDrawable = new AnimatedEmojiDrawable(i6, i5, visibleReaction.documentId);
                    if (messageObject2 != null) {
                        if (messageObject2.shouldDrawWithoutBackground()) {
                            i7 = messageObject2.isOutOwner() ? Theme.key_chat_outReactionButtonBackground : Theme.key_chat_inReactionButtonBackground;
                        } else {
                            i7 = messageObject2.isOutOwner() ? Theme.key_chat_outReactionButtonTextSelected : Theme.key_chat_inReactionButtonTextSelected;
                        }
                        color = Theme.getColor(i7, baseFragment != null ? baseFragment.getResourceProvider() : null);
                    } else {
                        color = -1;
                    }
                    animatedEmojiDrawable.setColorFilter(new PorterDuffColorFilter(color, PorterDuff.Mode.SRC_IN));
                    boolean z7 = i2 == 0 ? z6 ? 1 : 0 : false;
                    animationView.setAnimatedEmojiEffect(AnimatedEmojiEffect.createFrom(animatedEmojiDrawable, z7, !z7));
                    r7 = 0;
                    this.windowView.setClipChildren(false);
                } else {
                    r7 = 0;
                }
            }
            animationView2.getImageReceiver().setAutoRepeat(r7);
            animationView2.getImageReceiver().setAllowStartAnimation(r7);
            if (animationView2.getImageReceiver().getLottieAnimation() != null) {
                if (i2 == 2) {
                    animationView2.getImageReceiver().getLottieAnimation().setCurrentFrame(animationView2.getImageReceiver().getLottieAnimation().getFramesCount() - (z6 ? 1 : 0), r7);
                } else {
                    animationView2.getImageReceiver().getLottieAnimation().setCurrentFrame(r7, r7);
                    animationView2.getImageReceiver().getLottieAnimation().start();
                }
            }
            int i23 = i17 - i15;
            int i24 = i23 >> 1;
            i23 = i2 == z6 ? i24 : i23;
            frameLayout.addView(animationView2);
            animationView2.getLayoutParams().width = i15;
            animationView2.getLayoutParams().height = i15;
            ((FrameLayout.LayoutParams) animationView2.getLayoutParams()).topMargin = i24;
            ((FrameLayout.LayoutParams) animationView2.getLayoutParams()).leftMargin = i23;
            if (i2 != z6 && !z) {
                if (tL_availableReaction != null) {
                    animationView3.getImageReceiver().setImage(ImageLocation.getForDocument(tL_availableReaction.center_icon), "40_40_lastreactframe", null, "webp", tL_availableReaction, 1);
                }
                frameLayout.addView(animationView3);
                animationView3.getLayoutParams().width = i15;
                animationView3.getLayoutParams().height = i15;
                ((FrameLayout.LayoutParams) animationView3.getLayoutParams()).topMargin = i24;
                ((FrameLayout.LayoutParams) animationView3.getLayoutParams()).leftMargin = i23;
            }
            this.windowView.addView(frameLayout);
            frameLayout.getLayoutParams().width = i17;
            frameLayout.getLayoutParams().height = i17;
            int i25 = -i24;
            ((FrameLayout.LayoutParams) frameLayout.getLayoutParams()).topMargin = i25;
            int i26 = -i23;
            ((FrameLayout.LayoutParams) frameLayout.getLayoutParams()).leftMargin = i26;
            this.windowView.addView(animationView);
            animationView.getLayoutParams().width = i17;
            animationView.getLayoutParams().height = i17;
            animationView.getLayoutParams().width = i17;
            animationView.getLayoutParams().height = i17;
            ((FrameLayout.LayoutParams) animationView.getLayoutParams()).topMargin = i25;
            ((FrameLayout.LayoutParams) animationView.getLayoutParams()).leftMargin = i26;
            frameLayout.setPivotX(i23);
            frameLayout.setPivotY(i24);
            return;
        }
        this.dismissed = z6;
    }

    /* JADX INFO: renamed from: org.telegram.ui.Components.Reactions.ReactionsEffectOverlay$1, reason: invalid class name */
    class AnonymousClass1 extends FrameLayout {
        final /* synthetic */ int val$animationType;
        final /* synthetic */ View val$cell;
        final /* synthetic */ ChatActivity val$chatActivity;
        final /* synthetic */ int val$emojiSize;
        final /* synthetic */ BaseFragment val$fragment;
        final /* synthetic */ boolean val$fromHolder;
        final /* synthetic */ float val$fromScale;
        final /* synthetic */ float val$fromX;
        final /* synthetic */ float val$fromY;
        final /* synthetic */ boolean val$isStories;
        final /* synthetic */ MessageObject val$messageObject;
        final /* synthetic */ ReactionsLayoutInBubble.VisibleReaction val$visibleReaction;

        /* JADX WARN: 'super' call moved to the top of the method (can break code semantics) */
        AnonymousClass1(Context context, BaseFragment baseFragment, View view, boolean z, MessageObject messageObject, ChatActivity chatActivity, int i, int i2, boolean z2, float f, float f2, float f3, ReactionsLayoutInBubble.VisibleReaction visibleReaction) {
            super(context);
            this.val$fragment = baseFragment;
            this.val$cell = view;
            this.val$isStories = z;
            this.val$messageObject = messageObject;
            this.val$chatActivity = chatActivity;
            this.val$emojiSize = i;
            this.val$animationType = i2;
            this.val$fromHolder = z2;
            this.val$fromScale = f;
            this.val$fromX = f2;
            this.val$fromY = f3;
            this.val$visibleReaction = visibleReaction;
        }

        /* JADX WARN: Code duplicated, block: B:125:0x02dd  */
        /* JADX WARN: Code duplicated, block: B:127:0x02e1  */
        /* JADX WARN: Code duplicated, block: B:129:0x02eb  */
        /* JADX WARN: Code duplicated, block: B:130:0x02f2  */
        /* JADX WARN: Code duplicated, block: B:132:0x02f8  */
        /* JADX WARN: Code duplicated, block: B:142:0x034c  */
        /* JADX WARN: Code duplicated, block: B:144:0x0356  */
        /* JADX WARN: Code duplicated, block: B:146:0x035a  */
        /* JADX WARN: Code duplicated, block: B:147:0x035e  */
        /* JADX WARN: Code duplicated, block: B:150:0x036c  */
        /* JADX WARN: Code duplicated, block: B:158:0x037f  */
        /* JADX WARN: Code duplicated, block: B:187:0x0427  */
        /* JADX WARN: Code duplicated, block: B:189:0x042b  */
        /* JADX WARN: Code duplicated, block: B:190:0x042e A[DONT_INVERT] */
        /* JADX WARN: Code duplicated, block: B:191:0x0430  */
        /* JADX WARN: Code duplicated, block: B:192:0x0433  */
        /* JADX WARN: Code duplicated, block: B:196:0x0440  */
        /* JADX WARN: Code duplicated, block: B:198:0x0444 A[ADDED_TO_REGION] */
        /* JADX WARN: Code duplicated, block: B:204:0x04b1  */
        /* JADX WARN: Code duplicated, block: B:207:0x04be  */
        /* JADX WARN: Code duplicated, block: B:209:0x04c2 A[ADDED_TO_REGION] */
        /* JADX WARN: Code duplicated, block: B:210:0x04c4  */
        /* JADX WARN: Code duplicated, block: B:212:0x04ca  */
        /* JADX WARN: Code duplicated, block: B:213:0x04d6  */
        /* JADX WARN: Code duplicated, block: B:215:0x04da  */
        /* JADX WARN: Code duplicated, block: B:218:0x04ef  */
        /* JADX WARN: Code duplicated, block: B:219:0x04f2  */
        /* JADX WARN: Code duplicated, block: B:222:0x04f8  */
        /* JADX WARN: Code duplicated, block: B:224:0x0501  */
        /* JADX WARN: Code duplicated, block: B:231:0x0520  */
        /* JADX WARN: Code duplicated, block: B:234:0x0525  */
        /* JADX WARN: Code duplicated, block: B:237:0x0537  */
        /* JADX WARN: Code duplicated, block: B:242:0x055a  */
        /* JADX WARN: Code duplicated, block: B:244:0x0568  */
        /* JADX WARN: Code duplicated, block: B:248:0x05af  */
        /* JADX WARN: Code duplicated, block: B:250:0x05b7  */
        /* JADX WARN: Code duplicated, block: B:252:0x05bf  */
        /* JADX WARN: Code duplicated, block: B:253:0x05d2  */
        /* JADX WARN: Code duplicated, block: B:255:0x05d8  */
        /* JADX WARN: Code duplicated, block: B:256:0x05dd  */
        /* JADX WARN: Code duplicated, block: B:259:0x069d  */
        /* JADX WARN: Code duplicated, block: B:261:0x06a5  */
        /* JADX WARN: Code duplicated, block: B:264:0x06ab  */
        /* JADX WARN: Code duplicated, block: B:267:0x06bf  */
        /* JADX WARN: Code duplicated, block: B:269:0x06cc  */
        /* JADX WARN: Code duplicated, block: B:270:0x06d0  */
        /* JADX WARN: Code duplicated, block: B:271:0x06d2  */
        /* JADX WARN: Code duplicated, block: B:273:0x06e1  */
        /* JADX WARN: Code duplicated, block: B:285:0x06e3 A[SYNTHETIC] */
        /* JADX WARN: Code duplicated, block: B:58:0x012b  */
        /* JADX WARN: Code duplicated, block: B:61:0x0139  */
        @Override // android.view.ViewGroup, android.view.View
        protected void dispatchDraw(Canvas canvas) {
            View viewFindCell;
            int iDp;
            float f;
            float measuredWidth;
            float measuredHeight;
            float measuredWidth2;
            float f2;
            float interpolation;
            float interpolation2;
            float f3;
            float f4;
            ReactionsEffectOverlay reactionsEffectOverlay;
            float f5;
            float f6;
            float f7;
            RLottieDrawable lottieAnimation;
            int i;
            AvatarParticle avatarParticle;
            float f8;
            float f9;
            float f10;
            float f11;
            float f12;
            float f13;
            float f14;
            float f15;
            float f16;
            float f17;
            float f18;
            float f19;
            ReactionsEffectOverlay reactionsEffectOverlay2;
            float f20;
            int i2;
            float f21;
            ReactionsEffectOverlay reactionsEffectOverlay3;
            int i3;
            View view;
            View view2;
            View view3;
            float f22;
            ReactionsLayoutInBubble.ReactionButton reactionButton;
            int paddingTop;
            ChatActivity chatActivity;
            if (ReactionsEffectOverlay.this.dismissed) {
                if (ReactionsEffectOverlay.this.dismissProgress != 1.0f) {
                    ReactionsEffectOverlay.this.dismissProgress += 0.10666667f;
                    if (ReactionsEffectOverlay.this.dismissProgress > 1.0f) {
                        ReactionsEffectOverlay.this.dismissProgress = 1.0f;
                        AndroidUtilities.runOnUIThread(new Runnable() { // from class: org.telegram.ui.Components.Reactions.ReactionsEffectOverlay$1$$ExternalSyntheticLambda0
                            @Override // java.lang.Runnable
                            public final void run() {
                                this.f$0.lambda$dispatchDraw$0();
                            }
                        });
                    }
                }
                if (ReactionsEffectOverlay.this.dismissProgress != 1.0f) {
                    setAlpha(1.0f - ReactionsEffectOverlay.this.dismissProgress);
                    super.dispatchDraw(canvas);
                }
                invalidate();
                return;
            }
            ReactionsEffectOverlay reactionsEffectOverlay4 = ReactionsEffectOverlay.this;
            if (!reactionsEffectOverlay4.started) {
                invalidate();
                return;
            }
            if (reactionsEffectOverlay4.holderView != null) {
                ReactionsEffectOverlay.this.holderView.enterImageView.setAlpha(0.0f);
                ReactionsEffectOverlay.this.holderView.pressedBackupImageView.setAlpha(0.0f);
            }
            BaseFragment baseFragment = this.val$fragment;
            if (baseFragment instanceof ChatActivity) {
                viewFindCell = ((ChatActivity) baseFragment).findCell(ReactionsEffectOverlay.this.messageId, false);
            } else {
                viewFindCell = this.val$cell;
            }
            if (this.val$isStories) {
                iDp = AndroidUtilities.dp(SharedConfig.deviceIsHigh() ? 120.0f : 50.0f);
            } else {
                MessageObject messageObject = this.val$messageObject;
                if (messageObject != null && messageObject.shouldDrawReactionsInLayout()) {
                    iDp = AndroidUtilities.dp(20.0f);
                } else {
                    iDp = AndroidUtilities.dp(14.0f);
                }
            }
            float f23 = iDp;
            if (viewFindCell != null) {
                viewFindCell.getLocationInWindow(ReactionsEffectOverlay.this.loc);
                ReactionsEffectOverlay reactionsEffectOverlay5 = ReactionsEffectOverlay.this;
                int[] iArr = reactionsEffectOverlay5.loc;
                measuredWidth = iArr[0];
                measuredHeight = iArr[1];
                if (viewFindCell instanceof ChatMessageCell) {
                    ChatMessageCell chatMessageCell = (ChatMessageCell) viewFindCell;
                    reactionButton = chatMessageCell.getReactionButton(reactionsEffectOverlay5.reaction);
                    f = 0.10666667f;
                    if (chatMessageCell.drawPinnedBottom && !chatMessageCell.shouldDrawTimeOnMedia()) {
                        measuredHeight += AndroidUtilities.dp(2.0f);
                    }
                    paddingTop = chatMessageCell.getPaddingTop();
                } else {
                    f = 0.10666667f;
                    if (viewFindCell instanceof ChatActionCell) {
                        reactionButton = ((ChatActionCell) viewFindCell).getReactionButton(reactionsEffectOverlay5.reaction);
                        paddingTop = viewFindCell.getPaddingTop();
                    } else {
                        if (viewFindCell instanceof GroupCallMessageCell) {
                            measuredWidth += ((GroupCallMessageCell) viewFindCell).getReactionCenterX();
                            measuredHeight += viewFindCell.getMeasuredHeight() / 2.0f;
                        }
                        reactionButton = null;
                    }
                    if (reactionButton != null) {
                        Rect rect = reactionButton.drawingImageRect;
                        measuredWidth += rect.left;
                        measuredHeight += rect.top;
                    }
                    chatActivity = this.val$chatActivity;
                    if (chatActivity != null) {
                        measuredHeight += chatActivity.drawingChatListViewYoffset;
                    }
                    ReactionsEffectOverlay.this.lastDrawnToX = measuredWidth;
                    ReactionsEffectOverlay.this.lastDrawnToY = measuredHeight;
                }
                measuredHeight += paddingTop;
                if (reactionButton != null) {
                    Rect rect2 = reactionButton.drawingImageRect;
                    measuredWidth += rect2.left;
                    measuredHeight += rect2.top;
                }
                chatActivity = this.val$chatActivity;
                if (chatActivity != null) {
                    measuredHeight += chatActivity.drawingChatListViewYoffset;
                }
                ReactionsEffectOverlay.this.lastDrawnToX = measuredWidth;
                ReactionsEffectOverlay.this.lastDrawnToY = measuredHeight;
            } else {
                f = 0.10666667f;
                if (this.val$isStories) {
                    float f24 = f23 / 2.0f;
                    measuredWidth = (getMeasuredWidth() / 2.0f) - f24;
                    measuredHeight = (getMeasuredHeight() / 2.0f) - f24;
                } else {
                    measuredWidth = ReactionsEffectOverlay.this.lastDrawnToX;
                    measuredHeight = ReactionsEffectOverlay.this.lastDrawnToY;
                }
            }
            BaseFragment baseFragment2 = this.val$fragment;
            if (baseFragment2 != null && baseFragment2.getParentActivity() != null && this.val$fragment.getFragmentView() != null && this.val$fragment.getFragmentView().getParent() != null && this.val$fragment.getFragmentView().getVisibility() == 0 && this.val$fragment.getFragmentView() != null) {
                this.val$fragment.getFragmentView().getLocationOnScreen(ReactionsEffectOverlay.this.loc);
                setAlpha(((View) this.val$fragment.getFragmentView().getParent()).getAlpha());
            } else if (!this.val$isStories && !(viewFindCell instanceof GroupCallMessageCell)) {
                return;
            }
            if (viewFindCell instanceof GroupCallMessageCell) {
                int i4 = this.val$emojiSize;
                measuredWidth2 = measuredWidth - (i4 / 2.0f);
                f2 = i4;
            } else {
                int i5 = this.val$emojiSize;
                measuredWidth2 = measuredWidth - ((i5 - f23) / 2.0f);
                f2 = i5 - f23;
            }
            float f25 = measuredHeight - (f2 / 2.0f);
            if (this.val$isStories && this.val$animationType == 0) {
                measuredWidth2 += AndroidUtilities.dp(40.0f);
            }
            if (this.val$animationType != 1 && !this.val$isStories) {
                int i6 = ReactionsEffectOverlay.this.loc[0];
                if (measuredWidth2 < i6) {
                    measuredWidth2 = i6;
                }
                if (this.val$emojiSize + measuredWidth2 > i6 + getMeasuredWidth()) {
                    measuredWidth2 = (ReactionsEffectOverlay.this.loc[0] + getMeasuredWidth()) - this.val$emojiSize;
                }
            }
            CubicBezierInterpolator cubicBezierInterpolator = CubicBezierInterpolator.DEFAULT;
            float interpolation3 = cubicBezierInterpolator.getInterpolation(ReactionsEffectOverlay.this.animateOutProgress);
            if (this.val$animationType == 2) {
                interpolation = CubicBezierInterpolator.EASE_OUT_QUINT.getInterpolation(interpolation3);
                interpolation2 = cubicBezierInterpolator.getInterpolation(interpolation3);
            } else if (this.val$fromHolder) {
                interpolation = CubicBezierInterpolator.EASE_OUT_QUINT.getInterpolation(ReactionsEffectOverlay.this.animateInProgress);
                interpolation2 = cubicBezierInterpolator.getInterpolation(ReactionsEffectOverlay.this.animateInProgress);
            } else {
                interpolation = ReactionsEffectOverlay.this.animateInProgress;
                interpolation2 = interpolation;
            }
            float f26 = 1.0f - interpolation;
            float f27 = (this.val$fromScale * f26) + interpolation;
            float f28 = f23 / this.val$emojiSize;
            if (this.val$animationType == 1) {
                f27 = 1.0f;
            } else {
                measuredWidth2 = (measuredWidth2 * interpolation) + (this.val$fromX * f26);
                f25 = (f25 * interpolation2) + (this.val$fromY * (1.0f - interpolation2));
            }
            ReactionsEffectOverlay.this.effectImageView.setTranslationX(measuredWidth2);
            ReactionsEffectOverlay.this.effectImageView.setTranslationY(f25);
            float f29 = 1.0f - interpolation3;
            ReactionsEffectOverlay.this.effectImageView.setAlpha(f29);
            ReactionsEffectOverlay.this.effectImageView.setScaleX(f27);
            ReactionsEffectOverlay.this.effectImageView.setScaleY(f27);
            int i7 = this.val$animationType;
            if (i7 == 2) {
                f27 = (this.val$fromScale * f26) + (f28 * interpolation);
                measuredWidth2 = (this.val$fromX * f26) + (measuredWidth * interpolation);
                f3 = this.val$fromY * (1.0f - interpolation2);
                f4 = measuredHeight * interpolation2;
            } else {
                if (interpolation3 != 0.0f) {
                    f27 = (f27 * f29) + (f28 * interpolation3);
                    measuredWidth2 = (measuredWidth2 * f29) + (measuredWidth * interpolation3);
                    f3 = f25 * f29;
                    f4 = measuredHeight * interpolation3;
                }
                if (i7 != 1) {
                    if (!this.val$isStories) {
                        AnimationView animationView = ReactionsEffectOverlay.this.emojiStaticImageView;
                        if (interpolation3 > 0.7f) {
                            f22 = (interpolation3 - 0.7f) / 0.3f;
                        } else {
                            f22 = 0.0f;
                        }
                        animationView.setAlpha(f22);
                    } else {
                        ReactionsEffectOverlay.this.emojiStaticImageView.setAlpha(1.0f);
                    }
                }
                if (this.val$animationType == 0 && this.val$isStories) {
                    ReactionsEffectOverlay.this.emojiImageView.setAlpha(f29);
                }
                ReactionsEffectOverlay.this.container.setTranslationX(measuredWidth2);
                ReactionsEffectOverlay.this.container.setTranslationY(f25);
                ReactionsEffectOverlay.this.container.setScaleX(f27);
                ReactionsEffectOverlay.this.container.setScaleY(f27);
                super.dispatchDraw(canvas);
                if (this.val$animationType != 1 || ReactionsEffectOverlay.this.emojiImageView.wasPlaying) {
                    reactionsEffectOverlay = ReactionsEffectOverlay.this;
                    f5 = reactionsEffectOverlay.animateInProgress;
                    if (f5 != 1.0f) {
                        if (this.val$fromHolder) {
                            reactionsEffectOverlay.animateInProgress = f5 + 0.045714285f;
                        } else {
                            reactionsEffectOverlay.animateInProgress = f5 + 0.07272727f;
                        }
                        if (reactionsEffectOverlay.animateInProgress > 1.0f) {
                            reactionsEffectOverlay.animateInProgress = 1.0f;
                        }
                    }
                }
                if (this.val$animationType == 2 && ((!ReactionsEffectOverlay.this.wasScrolled || this.val$animationType != 0) && (this.val$animationType == 1 || !ReactionsEffectOverlay.this.emojiImageView.wasPlaying || ReactionsEffectOverlay.this.emojiImageView.getImageReceiver().getLottieAnimation() == null || ReactionsEffectOverlay.this.emojiImageView.getImageReceiver().getLottieAnimation().isRunning()))) {
                    if (this.val$visibleReaction.documentId != 0) {
                        f6 = 0.045714285f;
                        f7 = 16.0f;
                        if (System.currentTimeMillis() - ReactionsEffectOverlay.this.startTime <= 2000) {
                        }
                        if (!ReactionsEffectOverlay.this.avatars.isEmpty() && ReactionsEffectOverlay.this.effectImageView.wasPlaying) {
                            lottieAnimation = ReactionsEffectOverlay.this.effectImageView.getImageReceiver().getLottieAnimation();
                            i = 0;
                            while (i < ReactionsEffectOverlay.this.avatars.size()) {
                                avatarParticle = (AvatarParticle) ReactionsEffectOverlay.this.avatars.get(i);
                                f8 = avatarParticle.progress;
                                if (lottieAnimation == null && lottieAnimation.isRunning()) {
                                    float duration = ReactionsEffectOverlay.this.effectImageView.getImageReceiver().getLottieAnimation().getDuration();
                                    if (((int) (duration - ((ReactionsEffectOverlay.this.effectImageView.getImageReceiver().getLottieAnimation().getCurrentFrame() / ReactionsEffectOverlay.this.effectImageView.getImageReceiver().getLottieAnimation().getFramesCount()) * duration))) < avatarParticle.leftTime) {
                                        f9 = avatarParticle.outProgress;
                                        if (f9 != 1.0f) {
                                            f10 = f9 + f;
                                            avatarParticle.outProgress = f10;
                                            if (f10 > 1.0f) {
                                                avatarParticle.outProgress = 1.0f;
                                                ReactionsEffectOverlay.this.avatars.remove(i);
                                                i--;
                                                f11 = f6;
                                            } else {
                                                if (f8 < 0.5f) {
                                                    f13 = f8 / 0.5f;
                                                    f12 = 1.0f;
                                                } else {
                                                    f12 = 1.0f;
                                                    f13 = 1.0f - ((f8 - 0.5f) / 0.5f);
                                                }
                                                float f30 = f12 - f8;
                                                float f31 = (avatarParticle.fromX * f30) + (avatarParticle.toX * f8);
                                                float f32 = ((avatarParticle.fromY * f30) + (avatarParticle.toY * f8)) - (avatarParticle.jumpY * f13);
                                                float f33 = avatarParticle.randomScale * f8 * (1.0f - avatarParticle.outProgress);
                                                float x = ReactionsEffectOverlay.this.effectImageView.getX() + (ReactionsEffectOverlay.this.effectImageView.getWidth() * ReactionsEffectOverlay.this.effectImageView.getScaleX() * f31);
                                                float y = ReactionsEffectOverlay.this.effectImageView.getY() + (ReactionsEffectOverlay.this.effectImageView.getHeight() * ReactionsEffectOverlay.this.effectImageView.getScaleY() * f32);
                                                int iDp2 = AndroidUtilities.dp(f7);
                                                float f34 = iDp2;
                                                float f35 = f34 / 2.0f;
                                                f11 = f6;
                                                ((AvatarParticle) ReactionsEffectOverlay.this.avatars.get(i)).imageReceiver.setImageCoords(x - f35, y - f35, f34, f34);
                                                ((AvatarParticle) ReactionsEffectOverlay.this.avatars.get(i)).imageReceiver.setRoundRadius(iDp2 >> 1);
                                                canvas.save();
                                                canvas.translate(0.0f, avatarParticle.globalTranslationY);
                                                canvas.scale(f33, f33, x, y);
                                                canvas.rotate(avatarParticle.currentRotation, x, y);
                                                ((AvatarParticle) ReactionsEffectOverlay.this.avatars.get(i)).imageReceiver.draw(canvas);
                                                canvas.restore();
                                                f14 = avatarParticle.progress;
                                                if (f14 < 1.0f) {
                                                    f19 = f14 + f11;
                                                    avatarParticle.progress = f19;
                                                    if (f19 > 1.0f) {
                                                        avatarParticle.progress = 1.0f;
                                                    }
                                                }
                                                if (f8 >= 1.0f) {
                                                    avatarParticle.globalTranslationY += (AndroidUtilities.dp(20.0f) * f7) / 500.0f;
                                                }
                                                if (avatarParticle.incrementRotation) {
                                                    float f36 = avatarParticle.currentRotation;
                                                    f17 = avatarParticle.randomRotation;
                                                    f18 = f36 + (f17 / 250.0f);
                                                    avatarParticle.currentRotation = f18;
                                                    if (f18 > f17) {
                                                        avatarParticle.incrementRotation = false;
                                                    }
                                                } else {
                                                    float f37 = avatarParticle.currentRotation;
                                                    f15 = avatarParticle.randomRotation;
                                                    f16 = f37 - (f15 / 250.0f);
                                                    avatarParticle.currentRotation = f16;
                                                    if (f16 < (-f15)) {
                                                        avatarParticle.incrementRotation = true;
                                                    }
                                                }
                                            }
                                        } else {
                                            if (f8 < 0.5f) {
                                                f13 = f8 / 0.5f;
                                                f12 = 1.0f;
                                            } else {
                                                f12 = 1.0f;
                                                f13 = 1.0f - ((f8 - 0.5f) / 0.5f);
                                            }
                                            float f38 = f12 - f8;
                                            float f39 = (avatarParticle.fromX * f38) + (avatarParticle.toX * f8);
                                            float f310 = ((avatarParticle.fromY * f38) + (avatarParticle.toY * f8)) - (avatarParticle.jumpY * f13);
                                            float f311 = avatarParticle.randomScale * f8 * (1.0f - avatarParticle.outProgress);
                                            float x2 = ReactionsEffectOverlay.this.effectImageView.getX() + (ReactionsEffectOverlay.this.effectImageView.getWidth() * ReactionsEffectOverlay.this.effectImageView.getScaleX() * f39);
                                            float y2 = ReactionsEffectOverlay.this.effectImageView.getY() + (ReactionsEffectOverlay.this.effectImageView.getHeight() * ReactionsEffectOverlay.this.effectImageView.getScaleY() * f310);
                                            int iDp3 = AndroidUtilities.dp(f7);
                                            float f312 = iDp3;
                                            float f313 = f312 / 2.0f;
                                            f11 = f6;
                                            ((AvatarParticle) ReactionsEffectOverlay.this.avatars.get(i)).imageReceiver.setImageCoords(x2 - f313, y2 - f313, f312, f312);
                                            ((AvatarParticle) ReactionsEffectOverlay.this.avatars.get(i)).imageReceiver.setRoundRadius(iDp3 >> 1);
                                            canvas.save();
                                            canvas.translate(0.0f, avatarParticle.globalTranslationY);
                                            canvas.scale(f311, f311, x2, y2);
                                            canvas.rotate(avatarParticle.currentRotation, x2, y2);
                                            ((AvatarParticle) ReactionsEffectOverlay.this.avatars.get(i)).imageReceiver.draw(canvas);
                                            canvas.restore();
                                            f14 = avatarParticle.progress;
                                            if (f14 < 1.0f) {
                                                f19 = f14 + f11;
                                                avatarParticle.progress = f19;
                                                if (f19 > 1.0f) {
                                                    avatarParticle.progress = 1.0f;
                                                }
                                            }
                                            if (f8 >= 1.0f) {
                                                avatarParticle.globalTranslationY += (AndroidUtilities.dp(20.0f) * f7) / 500.0f;
                                            }
                                            if (avatarParticle.incrementRotation) {
                                                float f314 = avatarParticle.currentRotation;
                                                f17 = avatarParticle.randomRotation;
                                                f18 = f314 + (f17 / 250.0f);
                                                avatarParticle.currentRotation = f18;
                                                if (f18 > f17) {
                                                    avatarParticle.incrementRotation = false;
                                                }
                                            } else {
                                                float f315 = avatarParticle.currentRotation;
                                                f15 = avatarParticle.randomRotation;
                                                f16 = f315 - (f15 / 250.0f);
                                                avatarParticle.currentRotation = f16;
                                                if (f16 < (-f15)) {
                                                    avatarParticle.incrementRotation = true;
                                                }
                                            }
                                        }
                                    } else {
                                        if (f8 < 0.5f) {
                                            f13 = f8 / 0.5f;
                                            f12 = 1.0f;
                                        } else {
                                            f12 = 1.0f;
                                            f13 = 1.0f - ((f8 - 0.5f) / 0.5f);
                                        }
                                        float f316 = f12 - f8;
                                        float f317 = (avatarParticle.fromX * f316) + (avatarParticle.toX * f8);
                                        float f318 = ((avatarParticle.fromY * f316) + (avatarParticle.toY * f8)) - (avatarParticle.jumpY * f13);
                                        float f319 = avatarParticle.randomScale * f8 * (1.0f - avatarParticle.outProgress);
                                        float x3 = ReactionsEffectOverlay.this.effectImageView.getX() + (ReactionsEffectOverlay.this.effectImageView.getWidth() * ReactionsEffectOverlay.this.effectImageView.getScaleX() * f317);
                                        float y3 = ReactionsEffectOverlay.this.effectImageView.getY() + (ReactionsEffectOverlay.this.effectImageView.getHeight() * ReactionsEffectOverlay.this.effectImageView.getScaleY() * f318);
                                        int iDp4 = AndroidUtilities.dp(f7);
                                        float f3110 = iDp4;
                                        float f3111 = f3110 / 2.0f;
                                        f11 = f6;
                                        ((AvatarParticle) ReactionsEffectOverlay.this.avatars.get(i)).imageReceiver.setImageCoords(x3 - f3111, y3 - f3111, f3110, f3110);
                                        ((AvatarParticle) ReactionsEffectOverlay.this.avatars.get(i)).imageReceiver.setRoundRadius(iDp4 >> 1);
                                        canvas.save();
                                        canvas.translate(0.0f, avatarParticle.globalTranslationY);
                                        canvas.scale(f319, f319, x3, y3);
                                        canvas.rotate(avatarParticle.currentRotation, x3, y3);
                                        ((AvatarParticle) ReactionsEffectOverlay.this.avatars.get(i)).imageReceiver.draw(canvas);
                                        canvas.restore();
                                        f14 = avatarParticle.progress;
                                        if (f14 < 1.0f) {
                                            f19 = f14 + f11;
                                            avatarParticle.progress = f19;
                                            if (f19 > 1.0f) {
                                                avatarParticle.progress = 1.0f;
                                            }
                                        }
                                        if (f8 >= 1.0f) {
                                            avatarParticle.globalTranslationY += (AndroidUtilities.dp(20.0f) * f7) / 500.0f;
                                        }
                                        if (avatarParticle.incrementRotation) {
                                            float f3112 = avatarParticle.currentRotation;
                                            f17 = avatarParticle.randomRotation;
                                            f18 = f3112 + (f17 / 250.0f);
                                            avatarParticle.currentRotation = f18;
                                            if (f18 > f17) {
                                                avatarParticle.incrementRotation = false;
                                            }
                                        } else {
                                            float f3113 = avatarParticle.currentRotation;
                                            f15 = avatarParticle.randomRotation;
                                            f16 = f3113 - (f15 / 250.0f);
                                            avatarParticle.currentRotation = f16;
                                            if (f16 < (-f15)) {
                                                avatarParticle.incrementRotation = true;
                                            }
                                        }
                                    }
                                } else {
                                    f9 = avatarParticle.outProgress;
                                    if (f9 != 1.0f) {
                                        f10 = f9 + f;
                                        avatarParticle.outProgress = f10;
                                        if (f10 > 1.0f) {
                                            avatarParticle.outProgress = 1.0f;
                                            ReactionsEffectOverlay.this.avatars.remove(i);
                                            i--;
                                            f11 = f6;
                                        } else {
                                            if (f8 < 0.5f) {
                                                f13 = f8 / 0.5f;
                                                f12 = 1.0f;
                                            } else {
                                                f12 = 1.0f;
                                                f13 = 1.0f - ((f8 - 0.5f) / 0.5f);
                                            }
                                            float f3114 = f12 - f8;
                                            float f3115 = (avatarParticle.fromX * f3114) + (avatarParticle.toX * f8);
                                            float f3116 = ((avatarParticle.fromY * f3114) + (avatarParticle.toY * f8)) - (avatarParticle.jumpY * f13);
                                            float f3117 = avatarParticle.randomScale * f8 * (1.0f - avatarParticle.outProgress);
                                            float x4 = ReactionsEffectOverlay.this.effectImageView.getX() + (ReactionsEffectOverlay.this.effectImageView.getWidth() * ReactionsEffectOverlay.this.effectImageView.getScaleX() * f3115);
                                            float y4 = ReactionsEffectOverlay.this.effectImageView.getY() + (ReactionsEffectOverlay.this.effectImageView.getHeight() * ReactionsEffectOverlay.this.effectImageView.getScaleY() * f3116);
                                            int iDp5 = AndroidUtilities.dp(f7);
                                            float f3118 = iDp5;
                                            float f3119 = f3118 / 2.0f;
                                            f11 = f6;
                                            ((AvatarParticle) ReactionsEffectOverlay.this.avatars.get(i)).imageReceiver.setImageCoords(x4 - f3119, y4 - f3119, f3118, f3118);
                                            ((AvatarParticle) ReactionsEffectOverlay.this.avatars.get(i)).imageReceiver.setRoundRadius(iDp5 >> 1);
                                            canvas.save();
                                            canvas.translate(0.0f, avatarParticle.globalTranslationY);
                                            canvas.scale(f3117, f3117, x4, y4);
                                            canvas.rotate(avatarParticle.currentRotation, x4, y4);
                                            ((AvatarParticle) ReactionsEffectOverlay.this.avatars.get(i)).imageReceiver.draw(canvas);
                                            canvas.restore();
                                            f14 = avatarParticle.progress;
                                            if (f14 < 1.0f) {
                                                f19 = f14 + f11;
                                                avatarParticle.progress = f19;
                                                if (f19 > 1.0f) {
                                                    avatarParticle.progress = 1.0f;
                                                }
                                            }
                                            if (f8 >= 1.0f) {
                                                avatarParticle.globalTranslationY += (AndroidUtilities.dp(20.0f) * f7) / 500.0f;
                                            }
                                            if (avatarParticle.incrementRotation) {
                                                float f31110 = avatarParticle.currentRotation;
                                                f17 = avatarParticle.randomRotation;
                                                f18 = f31110 + (f17 / 250.0f);
                                                avatarParticle.currentRotation = f18;
                                                if (f18 > f17) {
                                                    avatarParticle.incrementRotation = false;
                                                }
                                            } else {
                                                float f31111 = avatarParticle.currentRotation;
                                                f15 = avatarParticle.randomRotation;
                                                f16 = f31111 - (f15 / 250.0f);
                                                avatarParticle.currentRotation = f16;
                                                if (f16 < (-f15)) {
                                                    avatarParticle.incrementRotation = true;
                                                }
                                            }
                                        }
                                    } else {
                                        if (f8 < 0.5f) {
                                            f13 = f8 / 0.5f;
                                            f12 = 1.0f;
                                        } else {
                                            f12 = 1.0f;
                                            f13 = 1.0f - ((f8 - 0.5f) / 0.5f);
                                        }
                                        float f31112 = f12 - f8;
                                        float f31113 = (avatarParticle.fromX * f31112) + (avatarParticle.toX * f8);
                                        float f31114 = ((avatarParticle.fromY * f31112) + (avatarParticle.toY * f8)) - (avatarParticle.jumpY * f13);
                                        float f31115 = avatarParticle.randomScale * f8 * (1.0f - avatarParticle.outProgress);
                                        float x5 = ReactionsEffectOverlay.this.effectImageView.getX() + (ReactionsEffectOverlay.this.effectImageView.getWidth() * ReactionsEffectOverlay.this.effectImageView.getScaleX() * f31113);
                                        float y5 = ReactionsEffectOverlay.this.effectImageView.getY() + (ReactionsEffectOverlay.this.effectImageView.getHeight() * ReactionsEffectOverlay.this.effectImageView.getScaleY() * f31114);
                                        int iDp6 = AndroidUtilities.dp(f7);
                                        float f31116 = iDp6;
                                        float f31117 = f31116 / 2.0f;
                                        f11 = f6;
                                        ((AvatarParticle) ReactionsEffectOverlay.this.avatars.get(i)).imageReceiver.setImageCoords(x5 - f31117, y5 - f31117, f31116, f31116);
                                        ((AvatarParticle) ReactionsEffectOverlay.this.avatars.get(i)).imageReceiver.setRoundRadius(iDp6 >> 1);
                                        canvas.save();
                                        canvas.translate(0.0f, avatarParticle.globalTranslationY);
                                        canvas.scale(f31115, f31115, x5, y5);
                                        canvas.rotate(avatarParticle.currentRotation, x5, y5);
                                        ((AvatarParticle) ReactionsEffectOverlay.this.avatars.get(i)).imageReceiver.draw(canvas);
                                        canvas.restore();
                                        f14 = avatarParticle.progress;
                                        if (f14 < 1.0f) {
                                            f19 = f14 + f11;
                                            avatarParticle.progress = f19;
                                            if (f19 > 1.0f) {
                                                avatarParticle.progress = 1.0f;
                                            }
                                        }
                                        if (f8 >= 1.0f) {
                                            avatarParticle.globalTranslationY += (AndroidUtilities.dp(20.0f) * f7) / 500.0f;
                                        }
                                        if (avatarParticle.incrementRotation) {
                                            float f31118 = avatarParticle.currentRotation;
                                            f17 = avatarParticle.randomRotation;
                                            f18 = f31118 + (f17 / 250.0f);
                                            avatarParticle.currentRotation = f18;
                                            if (f18 > f17) {
                                                avatarParticle.incrementRotation = false;
                                            }
                                        } else {
                                            float f31119 = avatarParticle.currentRotation;
                                            f15 = avatarParticle.randomRotation;
                                            f16 = f31119 - (f15 / 250.0f);
                                            avatarParticle.currentRotation = f16;
                                            if (f16 < (-f15)) {
                                                avatarParticle.incrementRotation = true;
                                            }
                                        }
                                    }
                                }
                                i++;
                                f6 = f11;
                            }
                        }
                        invalidate();
                    }
                    f6 = 0.045714285f;
                    f7 = 16.0f;
                    if ((this.val$animationType == 1 && ReactionsEffectOverlay.this.effectImageView.wasPlaying && ReactionsEffectOverlay.this.effectImageView.getImageReceiver().getLottieAnimation() != null && !ReactionsEffectOverlay.this.effectImageView.getImageReceiver().getLottieAnimation().isRunning()) || (this.val$visibleReaction.documentId != 0 && System.currentTimeMillis() - ReactionsEffectOverlay.this.startTime > 2000)) {
                    }
                    if (!ReactionsEffectOverlay.this.avatars.isEmpty()) {
                        lottieAnimation = ReactionsEffectOverlay.this.effectImageView.getImageReceiver().getLottieAnimation();
                        i = 0;
                        while (i < ReactionsEffectOverlay.this.avatars.size()) {
                            avatarParticle = (AvatarParticle) ReactionsEffectOverlay.this.avatars.get(i);
                            f8 = avatarParticle.progress;
                            if (lottieAnimation == null) {
                                f9 = avatarParticle.outProgress;
                                if (f9 != 1.0f) {
                                    f10 = f9 + f;
                                    avatarParticle.outProgress = f10;
                                    if (f10 > 1.0f) {
                                        avatarParticle.outProgress = 1.0f;
                                        ReactionsEffectOverlay.this.avatars.remove(i);
                                        i--;
                                        f11 = f6;
                                    } else {
                                        if (f8 < 0.5f) {
                                            f13 = f8 / 0.5f;
                                            f12 = 1.0f;
                                        } else {
                                            f12 = 1.0f;
                                            f13 = 1.0f - ((f8 - 0.5f) / 0.5f);
                                        }
                                        float f311110 = f12 - f8;
                                        float f311111 = (avatarParticle.fromX * f311110) + (avatarParticle.toX * f8);
                                        float f311112 = ((avatarParticle.fromY * f311110) + (avatarParticle.toY * f8)) - (avatarParticle.jumpY * f13);
                                        float f311113 = avatarParticle.randomScale * f8 * (1.0f - avatarParticle.outProgress);
                                        float x6 = ReactionsEffectOverlay.this.effectImageView.getX() + (ReactionsEffectOverlay.this.effectImageView.getWidth() * ReactionsEffectOverlay.this.effectImageView.getScaleX() * f311111);
                                        float y6 = ReactionsEffectOverlay.this.effectImageView.getY() + (ReactionsEffectOverlay.this.effectImageView.getHeight() * ReactionsEffectOverlay.this.effectImageView.getScaleY() * f311112);
                                        int iDp7 = AndroidUtilities.dp(f7);
                                        float f311114 = iDp7;
                                        float f311115 = f311114 / 2.0f;
                                        f11 = f6;
                                        ((AvatarParticle) ReactionsEffectOverlay.this.avatars.get(i)).imageReceiver.setImageCoords(x6 - f311115, y6 - f311115, f311114, f311114);
                                        ((AvatarParticle) ReactionsEffectOverlay.this.avatars.get(i)).imageReceiver.setRoundRadius(iDp7 >> 1);
                                        canvas.save();
                                        canvas.translate(0.0f, avatarParticle.globalTranslationY);
                                        canvas.scale(f311113, f311113, x6, y6);
                                        canvas.rotate(avatarParticle.currentRotation, x6, y6);
                                        ((AvatarParticle) ReactionsEffectOverlay.this.avatars.get(i)).imageReceiver.draw(canvas);
                                        canvas.restore();
                                        f14 = avatarParticle.progress;
                                        if (f14 < 1.0f) {
                                            f19 = f14 + f11;
                                            avatarParticle.progress = f19;
                                            if (f19 > 1.0f) {
                                                avatarParticle.progress = 1.0f;
                                            }
                                        }
                                        if (f8 >= 1.0f) {
                                            avatarParticle.globalTranslationY += (AndroidUtilities.dp(20.0f) * f7) / 500.0f;
                                        }
                                        if (avatarParticle.incrementRotation) {
                                            float f311116 = avatarParticle.currentRotation;
                                            f17 = avatarParticle.randomRotation;
                                            f18 = f311116 + (f17 / 250.0f);
                                            avatarParticle.currentRotation = f18;
                                            if (f18 > f17) {
                                                avatarParticle.incrementRotation = false;
                                            }
                                        } else {
                                            float f311117 = avatarParticle.currentRotation;
                                            f15 = avatarParticle.randomRotation;
                                            f16 = f311117 - (f15 / 250.0f);
                                            avatarParticle.currentRotation = f16;
                                            if (f16 < (-f15)) {
                                                avatarParticle.incrementRotation = true;
                                            }
                                        }
                                    }
                                } else {
                                    if (f8 < 0.5f) {
                                        f13 = f8 / 0.5f;
                                        f12 = 1.0f;
                                    } else {
                                        f12 = 1.0f;
                                        f13 = 1.0f - ((f8 - 0.5f) / 0.5f);
                                    }
                                    float f311118 = f12 - f8;
                                    float f311119 = (avatarParticle.fromX * f311118) + (avatarParticle.toX * f8);
                                    float f3111110 = ((avatarParticle.fromY * f311118) + (avatarParticle.toY * f8)) - (avatarParticle.jumpY * f13);
                                    float f3111111 = avatarParticle.randomScale * f8 * (1.0f - avatarParticle.outProgress);
                                    float x7 = ReactionsEffectOverlay.this.effectImageView.getX() + (ReactionsEffectOverlay.this.effectImageView.getWidth() * ReactionsEffectOverlay.this.effectImageView.getScaleX() * f311119);
                                    float y7 = ReactionsEffectOverlay.this.effectImageView.getY() + (ReactionsEffectOverlay.this.effectImageView.getHeight() * ReactionsEffectOverlay.this.effectImageView.getScaleY() * f3111110);
                                    int iDp8 = AndroidUtilities.dp(f7);
                                    float f3111112 = iDp8;
                                    float f3111113 = f3111112 / 2.0f;
                                    f11 = f6;
                                    ((AvatarParticle) ReactionsEffectOverlay.this.avatars.get(i)).imageReceiver.setImageCoords(x7 - f3111113, y7 - f3111113, f3111112, f3111112);
                                    ((AvatarParticle) ReactionsEffectOverlay.this.avatars.get(i)).imageReceiver.setRoundRadius(iDp8 >> 1);
                                    canvas.save();
                                    canvas.translate(0.0f, avatarParticle.globalTranslationY);
                                    canvas.scale(f3111111, f3111111, x7, y7);
                                    canvas.rotate(avatarParticle.currentRotation, x7, y7);
                                    ((AvatarParticle) ReactionsEffectOverlay.this.avatars.get(i)).imageReceiver.draw(canvas);
                                    canvas.restore();
                                    f14 = avatarParticle.progress;
                                    if (f14 < 1.0f) {
                                        f19 = f14 + f11;
                                        avatarParticle.progress = f19;
                                        if (f19 > 1.0f) {
                                            avatarParticle.progress = 1.0f;
                                        }
                                    }
                                    if (f8 >= 1.0f) {
                                        avatarParticle.globalTranslationY += (AndroidUtilities.dp(20.0f) * f7) / 500.0f;
                                    }
                                    if (avatarParticle.incrementRotation) {
                                        float f3111114 = avatarParticle.currentRotation;
                                        f17 = avatarParticle.randomRotation;
                                        f18 = f3111114 + (f17 / 250.0f);
                                        avatarParticle.currentRotation = f18;
                                        if (f18 > f17) {
                                            avatarParticle.incrementRotation = false;
                                        }
                                    } else {
                                        float f3111115 = avatarParticle.currentRotation;
                                        f15 = avatarParticle.randomRotation;
                                        f16 = f3111115 - (f15 / 250.0f);
                                        avatarParticle.currentRotation = f16;
                                        if (f16 < (-f15)) {
                                            avatarParticle.incrementRotation = true;
                                        }
                                    }
                                }
                            } else {
                                f9 = avatarParticle.outProgress;
                                if (f9 != 1.0f) {
                                    f10 = f9 + f;
                                    avatarParticle.outProgress = f10;
                                    if (f10 > 1.0f) {
                                        avatarParticle.outProgress = 1.0f;
                                        ReactionsEffectOverlay.this.avatars.remove(i);
                                        i--;
                                        f11 = f6;
                                    } else {
                                        if (f8 < 0.5f) {
                                            f13 = f8 / 0.5f;
                                            f12 = 1.0f;
                                        } else {
                                            f12 = 1.0f;
                                            f13 = 1.0f - ((f8 - 0.5f) / 0.5f);
                                        }
                                        float f3111116 = f12 - f8;
                                        float f3111117 = (avatarParticle.fromX * f3111116) + (avatarParticle.toX * f8);
                                        float f3111118 = ((avatarParticle.fromY * f3111116) + (avatarParticle.toY * f8)) - (avatarParticle.jumpY * f13);
                                        float f3111119 = avatarParticle.randomScale * f8 * (1.0f - avatarParticle.outProgress);
                                        float x8 = ReactionsEffectOverlay.this.effectImageView.getX() + (ReactionsEffectOverlay.this.effectImageView.getWidth() * ReactionsEffectOverlay.this.effectImageView.getScaleX() * f3111117);
                                        float y8 = ReactionsEffectOverlay.this.effectImageView.getY() + (ReactionsEffectOverlay.this.effectImageView.getHeight() * ReactionsEffectOverlay.this.effectImageView.getScaleY() * f3111118);
                                        int iDp9 = AndroidUtilities.dp(f7);
                                        float f31111110 = iDp9;
                                        float f31111111 = f31111110 / 2.0f;
                                        f11 = f6;
                                        ((AvatarParticle) ReactionsEffectOverlay.this.avatars.get(i)).imageReceiver.setImageCoords(x8 - f31111111, y8 - f31111111, f31111110, f31111110);
                                        ((AvatarParticle) ReactionsEffectOverlay.this.avatars.get(i)).imageReceiver.setRoundRadius(iDp9 >> 1);
                                        canvas.save();
                                        canvas.translate(0.0f, avatarParticle.globalTranslationY);
                                        canvas.scale(f3111119, f3111119, x8, y8);
                                        canvas.rotate(avatarParticle.currentRotation, x8, y8);
                                        ((AvatarParticle) ReactionsEffectOverlay.this.avatars.get(i)).imageReceiver.draw(canvas);
                                        canvas.restore();
                                        f14 = avatarParticle.progress;
                                        if (f14 < 1.0f) {
                                            f19 = f14 + f11;
                                            avatarParticle.progress = f19;
                                            if (f19 > 1.0f) {
                                                avatarParticle.progress = 1.0f;
                                            }
                                        }
                                        if (f8 >= 1.0f) {
                                            avatarParticle.globalTranslationY += (AndroidUtilities.dp(20.0f) * f7) / 500.0f;
                                        }
                                        if (avatarParticle.incrementRotation) {
                                            float f31111112 = avatarParticle.currentRotation;
                                            f17 = avatarParticle.randomRotation;
                                            f18 = f31111112 + (f17 / 250.0f);
                                            avatarParticle.currentRotation = f18;
                                            if (f18 > f17) {
                                                avatarParticle.incrementRotation = false;
                                            }
                                        } else {
                                            float f31111113 = avatarParticle.currentRotation;
                                            f15 = avatarParticle.randomRotation;
                                            f16 = f31111113 - (f15 / 250.0f);
                                            avatarParticle.currentRotation = f16;
                                            if (f16 < (-f15)) {
                                                avatarParticle.incrementRotation = true;
                                            }
                                        }
                                    }
                                } else {
                                    if (f8 < 0.5f) {
                                        f13 = f8 / 0.5f;
                                        f12 = 1.0f;
                                    } else {
                                        f12 = 1.0f;
                                        f13 = 1.0f - ((f8 - 0.5f) / 0.5f);
                                    }
                                    float f31111114 = f12 - f8;
                                    float f31111115 = (avatarParticle.fromX * f31111114) + (avatarParticle.toX * f8);
                                    float f31111116 = ((avatarParticle.fromY * f31111114) + (avatarParticle.toY * f8)) - (avatarParticle.jumpY * f13);
                                    float f31111117 = avatarParticle.randomScale * f8 * (1.0f - avatarParticle.outProgress);
                                    float x9 = ReactionsEffectOverlay.this.effectImageView.getX() + (ReactionsEffectOverlay.this.effectImageView.getWidth() * ReactionsEffectOverlay.this.effectImageView.getScaleX() * f31111115);
                                    float y9 = ReactionsEffectOverlay.this.effectImageView.getY() + (ReactionsEffectOverlay.this.effectImageView.getHeight() * ReactionsEffectOverlay.this.effectImageView.getScaleY() * f31111116);
                                    int iDp10 = AndroidUtilities.dp(f7);
                                    float f31111118 = iDp10;
                                    float f31111119 = f31111118 / 2.0f;
                                    f11 = f6;
                                    ((AvatarParticle) ReactionsEffectOverlay.this.avatars.get(i)).imageReceiver.setImageCoords(x9 - f31111119, y9 - f31111119, f31111118, f31111118);
                                    ((AvatarParticle) ReactionsEffectOverlay.this.avatars.get(i)).imageReceiver.setRoundRadius(iDp10 >> 1);
                                    canvas.save();
                                    canvas.translate(0.0f, avatarParticle.globalTranslationY);
                                    canvas.scale(f31111117, f31111117, x9, y9);
                                    canvas.rotate(avatarParticle.currentRotation, x9, y9);
                                    ((AvatarParticle) ReactionsEffectOverlay.this.avatars.get(i)).imageReceiver.draw(canvas);
                                    canvas.restore();
                                    f14 = avatarParticle.progress;
                                    if (f14 < 1.0f) {
                                        f19 = f14 + f11;
                                        avatarParticle.progress = f19;
                                        if (f19 > 1.0f) {
                                            avatarParticle.progress = 1.0f;
                                        }
                                    }
                                    if (f8 >= 1.0f) {
                                        avatarParticle.globalTranslationY += (AndroidUtilities.dp(20.0f) * f7) / 500.0f;
                                    }
                                    if (avatarParticle.incrementRotation) {
                                        float f311111110 = avatarParticle.currentRotation;
                                        f17 = avatarParticle.randomRotation;
                                        f18 = f311111110 + (f17 / 250.0f);
                                        avatarParticle.currentRotation = f18;
                                        if (f18 > f17) {
                                            avatarParticle.incrementRotation = false;
                                        }
                                    } else {
                                        float f311111111 = avatarParticle.currentRotation;
                                        f15 = avatarParticle.randomRotation;
                                        f16 = f311111111 - (f15 / 250.0f);
                                        avatarParticle.currentRotation = f16;
                                        if (f16 < (-f15)) {
                                            avatarParticle.incrementRotation = true;
                                        }
                                    }
                                }
                            }
                            i++;
                            f6 = f11;
                        }
                    }
                    invalidate();
                }
                f6 = 0.045714285f;
                f7 = 16.0f;
                reactionsEffectOverlay2 = ReactionsEffectOverlay.this;
                f20 = reactionsEffectOverlay2.animateOutProgress;
                if (f20 != 1.0f) {
                    i2 = this.val$animationType;
                    if (i2 == 1) {
                        reactionsEffectOverlay2.animateOutProgress = 1.0f;
                    } else {
                        if (i2 == 2) {
                            f21 = 350.0f;
                        } else {
                            f21 = 220.0f;
                        }
                        reactionsEffectOverlay2.animateOutProgress = f20 + (f7 / f21);
                    }
                    if (reactionsEffectOverlay2.animateOutProgress > 0.7f) {
                        if (!this.val$isStories && i2 == 2) {
                            if (!reactionsEffectOverlay2.isFinished) {
                                reactionsEffectOverlay2.isFinished = true;
                                try {
                                    performHapticFeedback(0);
                                } catch (Exception unused) {
                                }
                                ((ViewGroup) getParent()).addView(ReactionsEffectOverlay.this.nextReactionOverlay.windowView);
                                ReactionsEffectOverlay.this.nextReactionOverlay.isStories = true;
                                ReactionsEffectOverlay.this.nextReactionOverlay.started = true;
                                ReactionsEffectOverlay.this.nextReactionOverlay.startTime = System.currentTimeMillis();
                                ReactionsEffectOverlay.this.nextReactionOverlay.windowView.setTag(R.id.parent_tag, 1);
                                animate().scaleX(0.0f).scaleY(0.0f).setStartDelay(1000L).setDuration(150L).setListener(new AnimatorListenerAdapter() { // from class: org.telegram.ui.Components.Reactions.ReactionsEffectOverlay.1.1
                                    @Override // android.animation.AnimatorListenerAdapter, android.animation.Animator.AnimatorListener
                                    public void onAnimationEnd(Animator animator) {
                                        ReactionsEffectOverlay.this.removeCurrentView();
                                    }
                                });
                            }
                        } else {
                            ReactionsEffectOverlay.startShortAnimation();
                        }
                    }
                    reactionsEffectOverlay3 = ReactionsEffectOverlay.this;
                    if (reactionsEffectOverlay3.animateOutProgress >= 1.0f) {
                        i3 = this.val$animationType;
                        if (i3 != 0 || i3 == 2) {
                            view = this.val$cell;
                            if (view instanceof ChatMessageCell) {
                                ((ChatMessageCell) view).reactionsLayoutInBubble.animateReaction(reactionsEffectOverlay3.reaction);
                            } else if (view instanceof ChatActionCell) {
                                ((ChatActionCell) view).reactionsLayoutInBubble.animateReaction(reactionsEffectOverlay3.reaction);
                            }
                        }
                        ReactionsEffectOverlay.this.animateOutProgress = 1.0f;
                        if (this.val$animationType == 1) {
                            ReactionsEffectOverlay.currentShortOverlay = null;
                        } else {
                            ReactionsEffectOverlay.currentOverlay = null;
                        }
                        view2 = this.val$cell;
                        if (view2 != null) {
                            view2.invalidate();
                            view3 = this.val$cell;
                            if ((view3 instanceof ChatMessageCell) && ((ChatMessageCell) view3).getCurrentMessagesGroup() != null && this.val$cell.getParent() != null) {
                                ((View) this.val$cell.getParent()).invalidate();
                            }
                        }
                        if (this.val$isStories || this.val$animationType != 2) {
                            AndroidUtilities.runOnUIThread(new Runnable() { // from class: org.telegram.ui.Components.Reactions.ReactionsEffectOverlay$1$$ExternalSyntheticLambda1
                                @Override // java.lang.Runnable
                                public final void run() {
                                    this.f$0.lambda$dispatchDraw$1();
                                }
                            });
                        }
                    }
                }
                if (!ReactionsEffectOverlay.this.avatars.isEmpty()) {
                    lottieAnimation = ReactionsEffectOverlay.this.effectImageView.getImageReceiver().getLottieAnimation();
                    i = 0;
                    while (i < ReactionsEffectOverlay.this.avatars.size()) {
                        avatarParticle = (AvatarParticle) ReactionsEffectOverlay.this.avatars.get(i);
                        f8 = avatarParticle.progress;
                        if (lottieAnimation == null) {
                            f9 = avatarParticle.outProgress;
                            if (f9 != 1.0f) {
                                f10 = f9 + f;
                                avatarParticle.outProgress = f10;
                                if (f10 > 1.0f) {
                                    avatarParticle.outProgress = 1.0f;
                                    ReactionsEffectOverlay.this.avatars.remove(i);
                                    i--;
                                    f11 = f6;
                                } else {
                                    if (f8 < 0.5f) {
                                        f13 = f8 / 0.5f;
                                        f12 = 1.0f;
                                    } else {
                                        f12 = 1.0f;
                                        f13 = 1.0f - ((f8 - 0.5f) / 0.5f);
                                    }
                                    float f311111112 = f12 - f8;
                                    float f311111113 = (avatarParticle.fromX * f311111112) + (avatarParticle.toX * f8);
                                    float f311111114 = ((avatarParticle.fromY * f311111112) + (avatarParticle.toY * f8)) - (avatarParticle.jumpY * f13);
                                    float f311111115 = avatarParticle.randomScale * f8 * (1.0f - avatarParticle.outProgress);
                                    float x10 = ReactionsEffectOverlay.this.effectImageView.getX() + (ReactionsEffectOverlay.this.effectImageView.getWidth() * ReactionsEffectOverlay.this.effectImageView.getScaleX() * f311111113);
                                    float y10 = ReactionsEffectOverlay.this.effectImageView.getY() + (ReactionsEffectOverlay.this.effectImageView.getHeight() * ReactionsEffectOverlay.this.effectImageView.getScaleY() * f311111114);
                                    int iDp11 = AndroidUtilities.dp(f7);
                                    float f311111116 = iDp11;
                                    float f311111117 = f311111116 / 2.0f;
                                    f11 = f6;
                                    ((AvatarParticle) ReactionsEffectOverlay.this.avatars.get(i)).imageReceiver.setImageCoords(x10 - f311111117, y10 - f311111117, f311111116, f311111116);
                                    ((AvatarParticle) ReactionsEffectOverlay.this.avatars.get(i)).imageReceiver.setRoundRadius(iDp11 >> 1);
                                    canvas.save();
                                    canvas.translate(0.0f, avatarParticle.globalTranslationY);
                                    canvas.scale(f311111115, f311111115, x10, y10);
                                    canvas.rotate(avatarParticle.currentRotation, x10, y10);
                                    ((AvatarParticle) ReactionsEffectOverlay.this.avatars.get(i)).imageReceiver.draw(canvas);
                                    canvas.restore();
                                    f14 = avatarParticle.progress;
                                    if (f14 < 1.0f) {
                                        f19 = f14 + f11;
                                        avatarParticle.progress = f19;
                                        if (f19 > 1.0f) {
                                            avatarParticle.progress = 1.0f;
                                        }
                                    }
                                    if (f8 >= 1.0f) {
                                        avatarParticle.globalTranslationY += (AndroidUtilities.dp(20.0f) * f7) / 500.0f;
                                    }
                                    if (avatarParticle.incrementRotation) {
                                        float f311111118 = avatarParticle.currentRotation;
                                        f17 = avatarParticle.randomRotation;
                                        f18 = f311111118 + (f17 / 250.0f);
                                        avatarParticle.currentRotation = f18;
                                        if (f18 > f17) {
                                            avatarParticle.incrementRotation = false;
                                        }
                                    } else {
                                        float f311111119 = avatarParticle.currentRotation;
                                        f15 = avatarParticle.randomRotation;
                                        f16 = f311111119 - (f15 / 250.0f);
                                        avatarParticle.currentRotation = f16;
                                        if (f16 < (-f15)) {
                                            avatarParticle.incrementRotation = true;
                                        }
                                    }
                                }
                            } else {
                                if (f8 < 0.5f) {
                                    f13 = f8 / 0.5f;
                                    f12 = 1.0f;
                                } else {
                                    f12 = 1.0f;
                                    f13 = 1.0f - ((f8 - 0.5f) / 0.5f);
                                }
                                float f3111111110 = f12 - f8;
                                float f3111111111 = (avatarParticle.fromX * f3111111110) + (avatarParticle.toX * f8);
                                float f3111111112 = ((avatarParticle.fromY * f3111111110) + (avatarParticle.toY * f8)) - (avatarParticle.jumpY * f13);
                                float f3111111113 = avatarParticle.randomScale * f8 * (1.0f - avatarParticle.outProgress);
                                float x11 = ReactionsEffectOverlay.this.effectImageView.getX() + (ReactionsEffectOverlay.this.effectImageView.getWidth() * ReactionsEffectOverlay.this.effectImageView.getScaleX() * f3111111111);
                                float y11 = ReactionsEffectOverlay.this.effectImageView.getY() + (ReactionsEffectOverlay.this.effectImageView.getHeight() * ReactionsEffectOverlay.this.effectImageView.getScaleY() * f3111111112);
                                int iDp12 = AndroidUtilities.dp(f7);
                                float f3111111114 = iDp12;
                                float f3111111115 = f3111111114 / 2.0f;
                                f11 = f6;
                                ((AvatarParticle) ReactionsEffectOverlay.this.avatars.get(i)).imageReceiver.setImageCoords(x11 - f3111111115, y11 - f3111111115, f3111111114, f3111111114);
                                ((AvatarParticle) ReactionsEffectOverlay.this.avatars.get(i)).imageReceiver.setRoundRadius(iDp12 >> 1);
                                canvas.save();
                                canvas.translate(0.0f, avatarParticle.globalTranslationY);
                                canvas.scale(f3111111113, f3111111113, x11, y11);
                                canvas.rotate(avatarParticle.currentRotation, x11, y11);
                                ((AvatarParticle) ReactionsEffectOverlay.this.avatars.get(i)).imageReceiver.draw(canvas);
                                canvas.restore();
                                f14 = avatarParticle.progress;
                                if (f14 < 1.0f) {
                                    f19 = f14 + f11;
                                    avatarParticle.progress = f19;
                                    if (f19 > 1.0f) {
                                        avatarParticle.progress = 1.0f;
                                    }
                                }
                                if (f8 >= 1.0f) {
                                    avatarParticle.globalTranslationY += (AndroidUtilities.dp(20.0f) * f7) / 500.0f;
                                }
                                if (avatarParticle.incrementRotation) {
                                    float f3111111116 = avatarParticle.currentRotation;
                                    f17 = avatarParticle.randomRotation;
                                    f18 = f3111111116 + (f17 / 250.0f);
                                    avatarParticle.currentRotation = f18;
                                    if (f18 > f17) {
                                        avatarParticle.incrementRotation = false;
                                    }
                                } else {
                                    float f3111111117 = avatarParticle.currentRotation;
                                    f15 = avatarParticle.randomRotation;
                                    f16 = f3111111117 - (f15 / 250.0f);
                                    avatarParticle.currentRotation = f16;
                                    if (f16 < (-f15)) {
                                        avatarParticle.incrementRotation = true;
                                    }
                                }
                            }
                        } else {
                            f9 = avatarParticle.outProgress;
                            if (f9 != 1.0f) {
                                f10 = f9 + f;
                                avatarParticle.outProgress = f10;
                                if (f10 > 1.0f) {
                                    avatarParticle.outProgress = 1.0f;
                                    ReactionsEffectOverlay.this.avatars.remove(i);
                                    i--;
                                    f11 = f6;
                                } else {
                                    if (f8 < 0.5f) {
                                        f13 = f8 / 0.5f;
                                        f12 = 1.0f;
                                    } else {
                                        f12 = 1.0f;
                                        f13 = 1.0f - ((f8 - 0.5f) / 0.5f);
                                    }
                                    float f3111111118 = f12 - f8;
                                    float f3111111119 = (avatarParticle.fromX * f3111111118) + (avatarParticle.toX * f8);
                                    float f31111111110 = ((avatarParticle.fromY * f3111111118) + (avatarParticle.toY * f8)) - (avatarParticle.jumpY * f13);
                                    float f31111111111 = avatarParticle.randomScale * f8 * (1.0f - avatarParticle.outProgress);
                                    float x12 = ReactionsEffectOverlay.this.effectImageView.getX() + (ReactionsEffectOverlay.this.effectImageView.getWidth() * ReactionsEffectOverlay.this.effectImageView.getScaleX() * f3111111119);
                                    float y12 = ReactionsEffectOverlay.this.effectImageView.getY() + (ReactionsEffectOverlay.this.effectImageView.getHeight() * ReactionsEffectOverlay.this.effectImageView.getScaleY() * f31111111110);
                                    int iDp13 = AndroidUtilities.dp(f7);
                                    float f31111111112 = iDp13;
                                    float f31111111113 = f31111111112 / 2.0f;
                                    f11 = f6;
                                    ((AvatarParticle) ReactionsEffectOverlay.this.avatars.get(i)).imageReceiver.setImageCoords(x12 - f31111111113, y12 - f31111111113, f31111111112, f31111111112);
                                    ((AvatarParticle) ReactionsEffectOverlay.this.avatars.get(i)).imageReceiver.setRoundRadius(iDp13 >> 1);
                                    canvas.save();
                                    canvas.translate(0.0f, avatarParticle.globalTranslationY);
                                    canvas.scale(f31111111111, f31111111111, x12, y12);
                                    canvas.rotate(avatarParticle.currentRotation, x12, y12);
                                    ((AvatarParticle) ReactionsEffectOverlay.this.avatars.get(i)).imageReceiver.draw(canvas);
                                    canvas.restore();
                                    f14 = avatarParticle.progress;
                                    if (f14 < 1.0f) {
                                        f19 = f14 + f11;
                                        avatarParticle.progress = f19;
                                        if (f19 > 1.0f) {
                                            avatarParticle.progress = 1.0f;
                                        }
                                    }
                                    if (f8 >= 1.0f) {
                                        avatarParticle.globalTranslationY += (AndroidUtilities.dp(20.0f) * f7) / 500.0f;
                                    }
                                    if (avatarParticle.incrementRotation) {
                                        float f31111111114 = avatarParticle.currentRotation;
                                        f17 = avatarParticle.randomRotation;
                                        f18 = f31111111114 + (f17 / 250.0f);
                                        avatarParticle.currentRotation = f18;
                                        if (f18 > f17) {
                                            avatarParticle.incrementRotation = false;
                                        }
                                    } else {
                                        float f31111111115 = avatarParticle.currentRotation;
                                        f15 = avatarParticle.randomRotation;
                                        f16 = f31111111115 - (f15 / 250.0f);
                                        avatarParticle.currentRotation = f16;
                                        if (f16 < (-f15)) {
                                            avatarParticle.incrementRotation = true;
                                        }
                                    }
                                }
                            } else {
                                if (f8 < 0.5f) {
                                    f13 = f8 / 0.5f;
                                    f12 = 1.0f;
                                } else {
                                    f12 = 1.0f;
                                    f13 = 1.0f - ((f8 - 0.5f) / 0.5f);
                                }
                                float f31111111116 = f12 - f8;
                                float f31111111117 = (avatarParticle.fromX * f31111111116) + (avatarParticle.toX * f8);
                                float f31111111118 = ((avatarParticle.fromY * f31111111116) + (avatarParticle.toY * f8)) - (avatarParticle.jumpY * f13);
                                float f31111111119 = avatarParticle.randomScale * f8 * (1.0f - avatarParticle.outProgress);
                                float x13 = ReactionsEffectOverlay.this.effectImageView.getX() + (ReactionsEffectOverlay.this.effectImageView.getWidth() * ReactionsEffectOverlay.this.effectImageView.getScaleX() * f31111111117);
                                float y13 = ReactionsEffectOverlay.this.effectImageView.getY() + (ReactionsEffectOverlay.this.effectImageView.getHeight() * ReactionsEffectOverlay.this.effectImageView.getScaleY() * f31111111118);
                                int iDp14 = AndroidUtilities.dp(f7);
                                float f311111111110 = iDp14;
                                float f311111111111 = f311111111110 / 2.0f;
                                f11 = f6;
                                ((AvatarParticle) ReactionsEffectOverlay.this.avatars.get(i)).imageReceiver.setImageCoords(x13 - f311111111111, y13 - f311111111111, f311111111110, f311111111110);
                                ((AvatarParticle) ReactionsEffectOverlay.this.avatars.get(i)).imageReceiver.setRoundRadius(iDp14 >> 1);
                                canvas.save();
                                canvas.translate(0.0f, avatarParticle.globalTranslationY);
                                canvas.scale(f31111111119, f31111111119, x13, y13);
                                canvas.rotate(avatarParticle.currentRotation, x13, y13);
                                ((AvatarParticle) ReactionsEffectOverlay.this.avatars.get(i)).imageReceiver.draw(canvas);
                                canvas.restore();
                                f14 = avatarParticle.progress;
                                if (f14 < 1.0f) {
                                    f19 = f14 + f11;
                                    avatarParticle.progress = f19;
                                    if (f19 > 1.0f) {
                                        avatarParticle.progress = 1.0f;
                                    }
                                }
                                if (f8 >= 1.0f) {
                                    avatarParticle.globalTranslationY += (AndroidUtilities.dp(20.0f) * f7) / 500.0f;
                                }
                                if (avatarParticle.incrementRotation) {
                                    float f311111111112 = avatarParticle.currentRotation;
                                    f17 = avatarParticle.randomRotation;
                                    f18 = f311111111112 + (f17 / 250.0f);
                                    avatarParticle.currentRotation = f18;
                                    if (f18 > f17) {
                                        avatarParticle.incrementRotation = false;
                                    }
                                } else {
                                    float f311111111113 = avatarParticle.currentRotation;
                                    f15 = avatarParticle.randomRotation;
                                    f16 = f311111111113 - (f15 / 250.0f);
                                    avatarParticle.currentRotation = f16;
                                    if (f16 < (-f15)) {
                                        avatarParticle.incrementRotation = true;
                                    }
                                }
                            }
                        }
                        i++;
                        f6 = f11;
                    }
                }
                invalidate();
            }
            f25 = f3 + f4;
            if (i7 != 1) {
                if (!this.val$isStories) {
                    AnimationView animationView2 = ReactionsEffectOverlay.this.emojiStaticImageView;
                    if (interpolation3 > 0.7f) {
                        f22 = (interpolation3 - 0.7f) / 0.3f;
                    } else {
                        f22 = 0.0f;
                    }
                    animationView2.setAlpha(f22);
                } else {
                    ReactionsEffectOverlay.this.emojiStaticImageView.setAlpha(1.0f);
                }
            }
            if (this.val$animationType == 0) {
                ReactionsEffectOverlay.this.emojiImageView.setAlpha(f29);
            }
            ReactionsEffectOverlay.this.container.setTranslationX(measuredWidth2);
            ReactionsEffectOverlay.this.container.setTranslationY(f25);
            ReactionsEffectOverlay.this.container.setScaleX(f27);
            ReactionsEffectOverlay.this.container.setScaleY(f27);
            super.dispatchDraw(canvas);
            if (this.val$animationType != 1) {
                reactionsEffectOverlay = ReactionsEffectOverlay.this;
                f5 = reactionsEffectOverlay.animateInProgress;
                if (f5 != 1.0f) {
                    if (this.val$fromHolder) {
                        reactionsEffectOverlay.animateInProgress = f5 + 0.045714285f;
                    } else {
                        reactionsEffectOverlay.animateInProgress = f5 + 0.07272727f;
                    }
                    if (reactionsEffectOverlay.animateInProgress > 1.0f) {
                        reactionsEffectOverlay.animateInProgress = 1.0f;
                    }
                }
            } else {
                reactionsEffectOverlay = ReactionsEffectOverlay.this;
                f5 = reactionsEffectOverlay.animateInProgress;
                if (f5 != 1.0f) {
                    if (this.val$fromHolder) {
                        reactionsEffectOverlay.animateInProgress = f5 + 0.045714285f;
                    } else {
                        reactionsEffectOverlay.animateInProgress = f5 + 0.07272727f;
                    }
                    if (reactionsEffectOverlay.animateInProgress > 1.0f) {
                        reactionsEffectOverlay.animateInProgress = 1.0f;
                    }
                }
            }
            if (this.val$animationType == 2) {
                f6 = 0.045714285f;
                f7 = 16.0f;
                reactionsEffectOverlay2 = ReactionsEffectOverlay.this;
                f20 = reactionsEffectOverlay2.animateOutProgress;
                if (f20 != 1.0f) {
                    i2 = this.val$animationType;
                    if (i2 == 1) {
                        reactionsEffectOverlay2.animateOutProgress = 1.0f;
                    } else {
                        if (i2 == 2) {
                            f21 = 350.0f;
                        } else {
                            f21 = 220.0f;
                        }
                        reactionsEffectOverlay2.animateOutProgress = f20 + (f7 / f21);
                    }
                    if (reactionsEffectOverlay2.animateOutProgress > 0.7f) {
                        if (!this.val$isStories) {
                            ReactionsEffectOverlay.startShortAnimation();
                        } else {
                            ReactionsEffectOverlay.startShortAnimation();
                        }
                    }
                    reactionsEffectOverlay3 = ReactionsEffectOverlay.this;
                    if (reactionsEffectOverlay3.animateOutProgress >= 1.0f) {
                        i3 = this.val$animationType;
                        if (i3 != 0) {
                            view = this.val$cell;
                            if (view instanceof ChatMessageCell) {
                                ((ChatMessageCell) view).reactionsLayoutInBubble.animateReaction(reactionsEffectOverlay3.reaction);
                            } else if (view instanceof ChatActionCell) {
                                ((ChatActionCell) view).reactionsLayoutInBubble.animateReaction(reactionsEffectOverlay3.reaction);
                            }
                        } else {
                            view = this.val$cell;
                            if (view instanceof ChatMessageCell) {
                                ((ChatMessageCell) view).reactionsLayoutInBubble.animateReaction(reactionsEffectOverlay3.reaction);
                            } else if (view instanceof ChatActionCell) {
                                ((ChatActionCell) view).reactionsLayoutInBubble.animateReaction(reactionsEffectOverlay3.reaction);
                            }
                        }
                        ReactionsEffectOverlay.this.animateOutProgress = 1.0f;
                        if (this.val$animationType == 1) {
                            ReactionsEffectOverlay.currentShortOverlay = null;
                        } else {
                            ReactionsEffectOverlay.currentOverlay = null;
                        }
                        view2 = this.val$cell;
                        if (view2 != null) {
                            view2.invalidate();
                            view3 = this.val$cell;
                            if (view3 instanceof ChatMessageCell) {
                                ((View) this.val$cell.getParent()).invalidate();
                            }
                        }
                        if (this.val$isStories) {
                            AndroidUtilities.runOnUIThread(new Runnable() { // from class: org.telegram.ui.Components.Reactions.ReactionsEffectOverlay$1$$ExternalSyntheticLambda1
                                @Override // java.lang.Runnable
                                public final void run() {
                                    this.f$0.lambda$dispatchDraw$1();
                                }
                            });
                        } else {
                            AndroidUtilities.runOnUIThread(new Runnable() { // from class: org.telegram.ui.Components.Reactions.ReactionsEffectOverlay$1$$ExternalSyntheticLambda1
                                @Override // java.lang.Runnable
                                public final void run() {
                                    this.f$0.lambda$dispatchDraw$1();
                                }
                            });
                        }
                    }
                }
            } else {
                f6 = 0.045714285f;
                f7 = 16.0f;
                reactionsEffectOverlay2 = ReactionsEffectOverlay.this;
                f20 = reactionsEffectOverlay2.animateOutProgress;
                if (f20 != 1.0f) {
                    i2 = this.val$animationType;
                    if (i2 == 1) {
                        reactionsEffectOverlay2.animateOutProgress = 1.0f;
                    } else {
                        if (i2 == 2) {
                            f21 = 350.0f;
                        } else {
                            f21 = 220.0f;
                        }
                        reactionsEffectOverlay2.animateOutProgress = f20 + (f7 / f21);
                    }
                    if (reactionsEffectOverlay2.animateOutProgress > 0.7f) {
                        if (!this.val$isStories) {
                            ReactionsEffectOverlay.startShortAnimation();
                        } else {
                            ReactionsEffectOverlay.startShortAnimation();
                        }
                    }
                    reactionsEffectOverlay3 = ReactionsEffectOverlay.this;
                    if (reactionsEffectOverlay3.animateOutProgress >= 1.0f) {
                        i3 = this.val$animationType;
                        if (i3 != 0) {
                            view = this.val$cell;
                            if (view instanceof ChatMessageCell) {
                                ((ChatMessageCell) view).reactionsLayoutInBubble.animateReaction(reactionsEffectOverlay3.reaction);
                            } else if (view instanceof ChatActionCell) {
                                ((ChatActionCell) view).reactionsLayoutInBubble.animateReaction(reactionsEffectOverlay3.reaction);
                            }
                        } else {
                            view = this.val$cell;
                            if (view instanceof ChatMessageCell) {
                                ((ChatMessageCell) view).reactionsLayoutInBubble.animateReaction(reactionsEffectOverlay3.reaction);
                            } else if (view instanceof ChatActionCell) {
                                ((ChatActionCell) view).reactionsLayoutInBubble.animateReaction(reactionsEffectOverlay3.reaction);
                            }
                        }
                        ReactionsEffectOverlay.this.animateOutProgress = 1.0f;
                        if (this.val$animationType == 1) {
                            ReactionsEffectOverlay.currentShortOverlay = null;
                        } else {
                            ReactionsEffectOverlay.currentOverlay = null;
                        }
                        view2 = this.val$cell;
                        if (view2 != null) {
                            view2.invalidate();
                            view3 = this.val$cell;
                            if (view3 instanceof ChatMessageCell) {
                                ((View) this.val$cell.getParent()).invalidate();
                            }
                        }
                        if (this.val$isStories) {
                            AndroidUtilities.runOnUIThread(new Runnable() { // from class: org.telegram.ui.Components.Reactions.ReactionsEffectOverlay$1$$ExternalSyntheticLambda1
                                @Override // java.lang.Runnable
                                public final void run() {
                                    this.f$0.lambda$dispatchDraw$1();
                                }
                            });
                        } else {
                            AndroidUtilities.runOnUIThread(new Runnable() { // from class: org.telegram.ui.Components.Reactions.ReactionsEffectOverlay$1$$ExternalSyntheticLambda1
                                @Override // java.lang.Runnable
                                public final void run() {
                                    this.f$0.lambda$dispatchDraw$1();
                                }
                            });
                        }
                    }
                }
            }
            if (!ReactionsEffectOverlay.this.avatars.isEmpty()) {
                lottieAnimation = ReactionsEffectOverlay.this.effectImageView.getImageReceiver().getLottieAnimation();
                i = 0;
                while (i < ReactionsEffectOverlay.this.avatars.size()) {
                    avatarParticle = (AvatarParticle) ReactionsEffectOverlay.this.avatars.get(i);
                    f8 = avatarParticle.progress;
                    if (lottieAnimation == null) {
                        f9 = avatarParticle.outProgress;
                        if (f9 != 1.0f) {
                            f10 = f9 + f;
                            avatarParticle.outProgress = f10;
                            if (f10 > 1.0f) {
                                avatarParticle.outProgress = 1.0f;
                                ReactionsEffectOverlay.this.avatars.remove(i);
                                i--;
                                f11 = f6;
                            } else {
                                if (f8 < 0.5f) {
                                    f13 = f8 / 0.5f;
                                    f12 = 1.0f;
                                } else {
                                    f12 = 1.0f;
                                    f13 = 1.0f - ((f8 - 0.5f) / 0.5f);
                                }
                                float f311111111114 = f12 - f8;
                                float f311111111115 = (avatarParticle.fromX * f311111111114) + (avatarParticle.toX * f8);
                                float f311111111116 = ((avatarParticle.fromY * f311111111114) + (avatarParticle.toY * f8)) - (avatarParticle.jumpY * f13);
                                float f311111111117 = avatarParticle.randomScale * f8 * (1.0f - avatarParticle.outProgress);
                                float x14 = ReactionsEffectOverlay.this.effectImageView.getX() + (ReactionsEffectOverlay.this.effectImageView.getWidth() * ReactionsEffectOverlay.this.effectImageView.getScaleX() * f311111111115);
                                float y14 = ReactionsEffectOverlay.this.effectImageView.getY() + (ReactionsEffectOverlay.this.effectImageView.getHeight() * ReactionsEffectOverlay.this.effectImageView.getScaleY() * f311111111116);
                                int iDp15 = AndroidUtilities.dp(f7);
                                float f311111111118 = iDp15;
                                float f311111111119 = f311111111118 / 2.0f;
                                f11 = f6;
                                ((AvatarParticle) ReactionsEffectOverlay.this.avatars.get(i)).imageReceiver.setImageCoords(x14 - f311111111119, y14 - f311111111119, f311111111118, f311111111118);
                                ((AvatarParticle) ReactionsEffectOverlay.this.avatars.get(i)).imageReceiver.setRoundRadius(iDp15 >> 1);
                                canvas.save();
                                canvas.translate(0.0f, avatarParticle.globalTranslationY);
                                canvas.scale(f311111111117, f311111111117, x14, y14);
                                canvas.rotate(avatarParticle.currentRotation, x14, y14);
                                ((AvatarParticle) ReactionsEffectOverlay.this.avatars.get(i)).imageReceiver.draw(canvas);
                                canvas.restore();
                                f14 = avatarParticle.progress;
                                if (f14 < 1.0f) {
                                    f19 = f14 + f11;
                                    avatarParticle.progress = f19;
                                    if (f19 > 1.0f) {
                                        avatarParticle.progress = 1.0f;
                                    }
                                }
                                if (f8 >= 1.0f) {
                                    avatarParticle.globalTranslationY += (AndroidUtilities.dp(20.0f) * f7) / 500.0f;
                                }
                                if (avatarParticle.incrementRotation) {
                                    float f3111111111110 = avatarParticle.currentRotation;
                                    f17 = avatarParticle.randomRotation;
                                    f18 = f3111111111110 + (f17 / 250.0f);
                                    avatarParticle.currentRotation = f18;
                                    if (f18 > f17) {
                                        avatarParticle.incrementRotation = false;
                                    }
                                } else {
                                    float f3111111111111 = avatarParticle.currentRotation;
                                    f15 = avatarParticle.randomRotation;
                                    f16 = f3111111111111 - (f15 / 250.0f);
                                    avatarParticle.currentRotation = f16;
                                    if (f16 < (-f15)) {
                                        avatarParticle.incrementRotation = true;
                                    }
                                }
                            }
                        } else {
                            if (f8 < 0.5f) {
                                f13 = f8 / 0.5f;
                                f12 = 1.0f;
                            } else {
                                f12 = 1.0f;
                                f13 = 1.0f - ((f8 - 0.5f) / 0.5f);
                            }
                            float f3111111111112 = f12 - f8;
                            float f3111111111113 = (avatarParticle.fromX * f3111111111112) + (avatarParticle.toX * f8);
                            float f3111111111114 = ((avatarParticle.fromY * f3111111111112) + (avatarParticle.toY * f8)) - (avatarParticle.jumpY * f13);
                            float f3111111111115 = avatarParticle.randomScale * f8 * (1.0f - avatarParticle.outProgress);
                            float x15 = ReactionsEffectOverlay.this.effectImageView.getX() + (ReactionsEffectOverlay.this.effectImageView.getWidth() * ReactionsEffectOverlay.this.effectImageView.getScaleX() * f3111111111113);
                            float y15 = ReactionsEffectOverlay.this.effectImageView.getY() + (ReactionsEffectOverlay.this.effectImageView.getHeight() * ReactionsEffectOverlay.this.effectImageView.getScaleY() * f3111111111114);
                            int iDp16 = AndroidUtilities.dp(f7);
                            float f3111111111116 = iDp16;
                            float f3111111111117 = f3111111111116 / 2.0f;
                            f11 = f6;
                            ((AvatarParticle) ReactionsEffectOverlay.this.avatars.get(i)).imageReceiver.setImageCoords(x15 - f3111111111117, y15 - f3111111111117, f3111111111116, f3111111111116);
                            ((AvatarParticle) ReactionsEffectOverlay.this.avatars.get(i)).imageReceiver.setRoundRadius(iDp16 >> 1);
                            canvas.save();
                            canvas.translate(0.0f, avatarParticle.globalTranslationY);
                            canvas.scale(f3111111111115, f3111111111115, x15, y15);
                            canvas.rotate(avatarParticle.currentRotation, x15, y15);
                            ((AvatarParticle) ReactionsEffectOverlay.this.avatars.get(i)).imageReceiver.draw(canvas);
                            canvas.restore();
                            f14 = avatarParticle.progress;
                            if (f14 < 1.0f) {
                                f19 = f14 + f11;
                                avatarParticle.progress = f19;
                                if (f19 > 1.0f) {
                                    avatarParticle.progress = 1.0f;
                                }
                            }
                            if (f8 >= 1.0f) {
                                avatarParticle.globalTranslationY += (AndroidUtilities.dp(20.0f) * f7) / 500.0f;
                            }
                            if (avatarParticle.incrementRotation) {
                                float f3111111111118 = avatarParticle.currentRotation;
                                f17 = avatarParticle.randomRotation;
                                f18 = f3111111111118 + (f17 / 250.0f);
                                avatarParticle.currentRotation = f18;
                                if (f18 > f17) {
                                    avatarParticle.incrementRotation = false;
                                }
                            } else {
                                float f3111111111119 = avatarParticle.currentRotation;
                                f15 = avatarParticle.randomRotation;
                                f16 = f3111111111119 - (f15 / 250.0f);
                                avatarParticle.currentRotation = f16;
                                if (f16 < (-f15)) {
                                    avatarParticle.incrementRotation = true;
                                }
                            }
                        }
                    } else {
                        f9 = avatarParticle.outProgress;
                        if (f9 != 1.0f) {
                            f10 = f9 + f;
                            avatarParticle.outProgress = f10;
                            if (f10 > 1.0f) {
                                avatarParticle.outProgress = 1.0f;
                                ReactionsEffectOverlay.this.avatars.remove(i);
                                i--;
                                f11 = f6;
                            } else {
                                if (f8 < 0.5f) {
                                    f13 = f8 / 0.5f;
                                    f12 = 1.0f;
                                } else {
                                    f12 = 1.0f;
                                    f13 = 1.0f - ((f8 - 0.5f) / 0.5f);
                                }
                                float f31111111111110 = f12 - f8;
                                float f31111111111111 = (avatarParticle.fromX * f31111111111110) + (avatarParticle.toX * f8);
                                float f31111111111112 = ((avatarParticle.fromY * f31111111111110) + (avatarParticle.toY * f8)) - (avatarParticle.jumpY * f13);
                                float f31111111111113 = avatarParticle.randomScale * f8 * (1.0f - avatarParticle.outProgress);
                                float x16 = ReactionsEffectOverlay.this.effectImageView.getX() + (ReactionsEffectOverlay.this.effectImageView.getWidth() * ReactionsEffectOverlay.this.effectImageView.getScaleX() * f31111111111111);
                                float y16 = ReactionsEffectOverlay.this.effectImageView.getY() + (ReactionsEffectOverlay.this.effectImageView.getHeight() * ReactionsEffectOverlay.this.effectImageView.getScaleY() * f31111111111112);
                                int iDp17 = AndroidUtilities.dp(f7);
                                float f31111111111114 = iDp17;
                                float f31111111111115 = f31111111111114 / 2.0f;
                                f11 = f6;
                                ((AvatarParticle) ReactionsEffectOverlay.this.avatars.get(i)).imageReceiver.setImageCoords(x16 - f31111111111115, y16 - f31111111111115, f31111111111114, f31111111111114);
                                ((AvatarParticle) ReactionsEffectOverlay.this.avatars.get(i)).imageReceiver.setRoundRadius(iDp17 >> 1);
                                canvas.save();
                                canvas.translate(0.0f, avatarParticle.globalTranslationY);
                                canvas.scale(f31111111111113, f31111111111113, x16, y16);
                                canvas.rotate(avatarParticle.currentRotation, x16, y16);
                                ((AvatarParticle) ReactionsEffectOverlay.this.avatars.get(i)).imageReceiver.draw(canvas);
                                canvas.restore();
                                f14 = avatarParticle.progress;
                                if (f14 < 1.0f) {
                                    f19 = f14 + f11;
                                    avatarParticle.progress = f19;
                                    if (f19 > 1.0f) {
                                        avatarParticle.progress = 1.0f;
                                    }
                                }
                                if (f8 >= 1.0f) {
                                    avatarParticle.globalTranslationY += (AndroidUtilities.dp(20.0f) * f7) / 500.0f;
                                }
                                if (avatarParticle.incrementRotation) {
                                    float f31111111111116 = avatarParticle.currentRotation;
                                    f17 = avatarParticle.randomRotation;
                                    f18 = f31111111111116 + (f17 / 250.0f);
                                    avatarParticle.currentRotation = f18;
                                    if (f18 > f17) {
                                        avatarParticle.incrementRotation = false;
                                    }
                                } else {
                                    float f31111111111117 = avatarParticle.currentRotation;
                                    f15 = avatarParticle.randomRotation;
                                    f16 = f31111111111117 - (f15 / 250.0f);
                                    avatarParticle.currentRotation = f16;
                                    if (f16 < (-f15)) {
                                        avatarParticle.incrementRotation = true;
                                    }
                                }
                            }
                        } else {
                            if (f8 < 0.5f) {
                                f13 = f8 / 0.5f;
                                f12 = 1.0f;
                            } else {
                                f12 = 1.0f;
                                f13 = 1.0f - ((f8 - 0.5f) / 0.5f);
                            }
                            float f31111111111118 = f12 - f8;
                            float f31111111111119 = (avatarParticle.fromX * f31111111111118) + (avatarParticle.toX * f8);
                            float f311111111111110 = ((avatarParticle.fromY * f31111111111118) + (avatarParticle.toY * f8)) - (avatarParticle.jumpY * f13);
                            float f311111111111111 = avatarParticle.randomScale * f8 * (1.0f - avatarParticle.outProgress);
                            float x17 = ReactionsEffectOverlay.this.effectImageView.getX() + (ReactionsEffectOverlay.this.effectImageView.getWidth() * ReactionsEffectOverlay.this.effectImageView.getScaleX() * f31111111111119);
                            float y17 = ReactionsEffectOverlay.this.effectImageView.getY() + (ReactionsEffectOverlay.this.effectImageView.getHeight() * ReactionsEffectOverlay.this.effectImageView.getScaleY() * f311111111111110);
                            int iDp18 = AndroidUtilities.dp(f7);
                            float f311111111111112 = iDp18;
                            float f311111111111113 = f311111111111112 / 2.0f;
                            f11 = f6;
                            ((AvatarParticle) ReactionsEffectOverlay.this.avatars.get(i)).imageReceiver.setImageCoords(x17 - f311111111111113, y17 - f311111111111113, f311111111111112, f311111111111112);
                            ((AvatarParticle) ReactionsEffectOverlay.this.avatars.get(i)).imageReceiver.setRoundRadius(iDp18 >> 1);
                            canvas.save();
                            canvas.translate(0.0f, avatarParticle.globalTranslationY);
                            canvas.scale(f311111111111111, f311111111111111, x17, y17);
                            canvas.rotate(avatarParticle.currentRotation, x17, y17);
                            ((AvatarParticle) ReactionsEffectOverlay.this.avatars.get(i)).imageReceiver.draw(canvas);
                            canvas.restore();
                            f14 = avatarParticle.progress;
                            if (f14 < 1.0f) {
                                f19 = f14 + f11;
                                avatarParticle.progress = f19;
                                if (f19 > 1.0f) {
                                    avatarParticle.progress = 1.0f;
                                }
                            }
                            if (f8 >= 1.0f) {
                                avatarParticle.globalTranslationY += (AndroidUtilities.dp(20.0f) * f7) / 500.0f;
                            }
                            if (avatarParticle.incrementRotation) {
                                float f311111111111114 = avatarParticle.currentRotation;
                                f17 = avatarParticle.randomRotation;
                                f18 = f311111111111114 + (f17 / 250.0f);
                                avatarParticle.currentRotation = f18;
                                if (f18 > f17) {
                                    avatarParticle.incrementRotation = false;
                                }
                            } else {
                                float f311111111111115 = avatarParticle.currentRotation;
                                f15 = avatarParticle.randomRotation;
                                f16 = f311111111111115 - (f15 / 250.0f);
                                avatarParticle.currentRotation = f16;
                                if (f16 < (-f15)) {
                                    avatarParticle.incrementRotation = true;
                                }
                            }
                        }
                    }
                    i++;
                    f6 = f11;
                }
            }
            invalidate();
        }

        /* JADX INFO: Access modifiers changed from: private */
        public /* synthetic */ void lambda$dispatchDraw$0() {
            ReactionsEffectOverlay.this.removeCurrentView();
        }

        /* JADX INFO: Access modifiers changed from: private */
        public /* synthetic */ void lambda$dispatchDraw$1() {
            ReactionsEffectOverlay.this.removeCurrentView();
        }

        @Override // android.view.ViewGroup, android.view.View
        protected void onAttachedToWindow() {
            super.onAttachedToWindow();
            for (int i = 0; i < ReactionsEffectOverlay.this.avatars.size(); i++) {
                ((AvatarParticle) ReactionsEffectOverlay.this.avatars.get(i)).imageReceiver.onAttachedToWindow();
            }
        }

        @Override // android.view.ViewGroup, android.view.View
        protected void onDetachedFromWindow() {
            super.onDetachedFromWindow();
            for (int i = 0; i < ReactionsEffectOverlay.this.avatars.size(); i++) {
                ((AvatarParticle) ReactionsEffectOverlay.this.avatars.get(i)).imageReceiver.onDetachedFromWindow();
            }
        }
    }

    public static String getFilterForAroundAnimation() {
        return sizeForAroundReaction() + "_" + sizeForAroundReaction() + "_nolimit_pcache";
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void removeCurrentView() {
        try {
            if (this.useWindow) {
                this.windowManager.removeView(this.windowView);
            } else {
                AndroidUtilities.removeFromParent(this.windowView);
            }
        } catch (Exception unused) {
        }
    }

    /* JADX WARN: Code duplicated, block: B:63:0x00ce  */
    public static void show(BaseFragment baseFragment, ReactionsContainerLayout reactionsContainerLayout, View view, View view2, float f, float f2, ReactionsLayoutInBubble.VisibleReaction visibleReaction, int i, int i2) {
        boolean z;
        ActionBarPopupWindow actionBarPopupWindow;
        if (view == null || visibleReaction == null || baseFragment == null || baseFragment.getParentActivity() == null) {
            return;
        }
        boolean z2 = baseFragment instanceof ChatActivity;
        if (z2) {
            ChatActivity chatActivity = (ChatActivity) baseFragment;
            if (ExteraConfig.hideReactionsInChannels && chatActivity.isChannel()) {
                return;
            }
            if (ExteraConfig.hideReactionsInGroups && !chatActivity.isChannel() && chatActivity.getCurrentChat() != null) {
                return;
            }
            if (ExteraConfig.hideReactionsInPrivateChats && chatActivity.getCurrentUser() != null) {
                return;
            }
        }
        boolean z3 = view instanceof ChatMessageCell;
        if (z3) {
            ChatMessageCell chatMessageCell = (ChatMessageCell) view;
            if (ExteraConfig.hideReactionsInChannels && ChatObject.isChannelAndNotMegaGroup(chatMessageCell.getCurrentChat())) {
                return;
            }
            if (ExteraConfig.hideReactionsInGroups && !ChatObject.isChannelAndNotMegaGroup(chatMessageCell.getCurrentChat()) && chatMessageCell.getCurrentChat() != null) {
                return;
            }
            if (ExteraConfig.hideReactionsInPrivateChats && chatMessageCell.getCurrentUser() != null) {
                return;
            }
        }
        if (MessagesController.getGlobalMainSettings().getBoolean("view_animations", true)) {
            if (i2 == 2 || i2 == 0) {
                show(baseFragment, null, view, view2, 0.0f, 0.0f, visibleReaction, i, 1);
            }
            ReactionsEffectOverlay reactionsEffectOverlay = new ReactionsEffectOverlay(baseFragment.getParentActivity(), baseFragment, reactionsContainerLayout, view, view2, f, f2, visibleReaction, i, i2, false);
            if (i2 == 1) {
                currentShortOverlay = reactionsEffectOverlay;
            } else {
                currentOverlay = reactionsEffectOverlay;
            }
            if (z2) {
                z = (i2 == 0 || i2 == 2) && (actionBarPopupWindow = ((ChatActivity) baseFragment).scrimPopupWindow) != null && actionBarPopupWindow.isShowing();
            }
            reactionsEffectOverlay.useWindow = z;
            if (z) {
                WindowManager.LayoutParams layoutParams = new WindowManager.LayoutParams();
                layoutParams.height = -1;
                layoutParams.width = -1;
                layoutParams.type = MediaDataController.MAX_STYLE_RUNS_COUNT;
                layoutParams.flags = 65816;
                layoutParams.format = -3;
                WindowManager windowManager = baseFragment.getParentActivity().getWindowManager();
                reactionsEffectOverlay.windowManager = windowManager;
                AndroidUtilities.setPreferredMaxRefreshRate(windowManager, reactionsEffectOverlay.windowView, layoutParams);
                reactionsEffectOverlay.windowManager.addView(reactionsEffectOverlay.windowView, layoutParams);
            } else {
                FrameLayout frameLayout = (FrameLayout) baseFragment.getParentActivity().getWindow().getDecorView();
                reactionsEffectOverlay.decorView = frameLayout;
                frameLayout.addView(reactionsEffectOverlay.windowView);
            }
            view.invalidate();
            if (!z3 || ((ChatMessageCell) view).getCurrentMessagesGroup() == null || view.getParent() == null) {
                return;
            }
            ((View) view.getParent()).invalidate();
        }
    }

    public static void startAnimation() {
        ReactionsEffectOverlay reactionsEffectOverlay = currentOverlay;
        if (reactionsEffectOverlay != null) {
            reactionsEffectOverlay.started = true;
            reactionsEffectOverlay.startTime = System.currentTimeMillis();
            if (currentOverlay.animationType != 0 || System.currentTimeMillis() - lastHapticTime <= 200) {
                return;
            }
            lastHapticTime = System.currentTimeMillis();
            currentOverlay.cell.performHapticFeedback(3);
            return;
        }
        startShortAnimation();
        ReactionsEffectOverlay reactionsEffectOverlay2 = currentShortOverlay;
        if (reactionsEffectOverlay2 != null) {
            View view = reactionsEffectOverlay2.cell;
            if (view instanceof ChatMessageCell) {
                ((ChatMessageCell) view).reactionsLayoutInBubble.animateReaction(reactionsEffectOverlay2.reaction);
            } else if (view instanceof ChatActionCell) {
                ((ChatActionCell) view).reactionsLayoutInBubble.animateReaction(reactionsEffectOverlay2.reaction);
            }
        }
    }

    public static void startShortAnimation() {
        ReactionsEffectOverlay reactionsEffectOverlay = currentShortOverlay;
        if (reactionsEffectOverlay == null || reactionsEffectOverlay.started) {
            return;
        }
        reactionsEffectOverlay.started = true;
        reactionsEffectOverlay.startTime = System.currentTimeMillis();
        if (currentShortOverlay.animationType != 1 || System.currentTimeMillis() - lastHapticTime <= 200) {
            return;
        }
        lastHapticTime = System.currentTimeMillis();
        View view = currentShortOverlay.cell;
        if (view != null) {
            view.performHapticFeedback(3);
        }
    }

    public static void removeCurrent(boolean z) {
        int i = 0;
        while (i < 2) {
            ReactionsEffectOverlay reactionsEffectOverlay = i == 0 ? currentOverlay : currentShortOverlay;
            if (reactionsEffectOverlay != null) {
                if (z) {
                    reactionsEffectOverlay.removeCurrentView();
                } else {
                    reactionsEffectOverlay.dismissed = true;
                }
            }
            i++;
        }
        currentShortOverlay = null;
        currentOverlay = null;
    }

    public static boolean isPlaying(int i, long j, ReactionsLayoutInBubble.VisibleReaction visibleReaction) {
        int i2;
        ReactionsEffectOverlay reactionsEffectOverlay = currentOverlay;
        if (reactionsEffectOverlay != null && ((i2 = reactionsEffectOverlay.animationType) == 2 || i2 == 0)) {
            long j2 = reactionsEffectOverlay.groupId;
            if (((j2 != 0 && j == j2) || i == reactionsEffectOverlay.messageId) && reactionsEffectOverlay.reaction.equals(visibleReaction)) {
                return true;
            }
        }
        return false;
    }

    private class AnimationView extends BackupImageView {
        AnimatedEmojiDrawable animatedEmojiDrawable;
        boolean attached;
        AnimatedEmojiEffect emojiEffect;
        boolean wasPlaying;

        public AnimationView(Context context) {
            super(context);
            getImageReceiver().setFileLoadingPriority(3);
        }

        @Override // org.telegram.ui.Components.BackupImageView, android.view.View
        protected void onDraw(Canvas canvas) {
            AnimatedEmojiDrawable animatedEmojiDrawable = this.animatedEmojiDrawable;
            if (animatedEmojiDrawable != null) {
                animatedEmojiDrawable.setBounds(0, 0, getMeasuredWidth(), getMeasuredHeight());
                this.animatedEmojiDrawable.setAlpha(255);
                this.animatedEmojiDrawable.draw(canvas);
                this.wasPlaying = true;
                return;
            }
            AnimatedEmojiEffect animatedEmojiEffect = this.emojiEffect;
            if (animatedEmojiEffect != null) {
                animatedEmojiEffect.setBounds(0, 0, getMeasuredWidth(), getMeasuredHeight());
                this.emojiEffect.draw(canvas);
                this.wasPlaying = true;
                return;
            }
            if (getImageReceiver().getLottieAnimation() != null && getImageReceiver().getLottieAnimation().isRunning()) {
                this.wasPlaying = true;
            }
            if (!this.wasPlaying && getImageReceiver().getLottieAnimation() != null && !getImageReceiver().getLottieAnimation().isRunning()) {
                if (ReactionsEffectOverlay.this.animationType == 2 && !ReactionsEffectOverlay.this.isStories) {
                    getImageReceiver().getLottieAnimation().setCurrentFrame(getImageReceiver().getLottieAnimation().getFramesCount() - 1, false);
                } else {
                    getImageReceiver().getLottieAnimation().setCurrentFrame(0, false);
                    getImageReceiver().getLottieAnimation().start();
                }
            }
            super.onDraw(canvas);
        }

        @Override // org.telegram.ui.Components.BackupImageView, android.view.View
        protected void onAttachedToWindow() {
            super.onAttachedToWindow();
            this.attached = true;
            AnimatedEmojiDrawable animatedEmojiDrawable = this.animatedEmojiDrawable;
            if (animatedEmojiDrawable != null) {
                animatedEmojiDrawable.addView(this);
            }
            AnimatedEmojiEffect animatedEmojiEffect = this.emojiEffect;
            if (animatedEmojiEffect != null) {
                animatedEmojiEffect.setView(this);
            }
        }

        @Override // org.telegram.ui.Components.BackupImageView, android.view.View
        protected void onDetachedFromWindow() {
            super.onDetachedFromWindow();
            this.attached = false;
            AnimatedEmojiDrawable animatedEmojiDrawable = this.animatedEmojiDrawable;
            if (animatedEmojiDrawable != null) {
                animatedEmojiDrawable.removeView(this);
            }
            AnimatedEmojiEffect animatedEmojiEffect = this.emojiEffect;
            if (animatedEmojiEffect != null) {
                animatedEmojiEffect.removeView(this);
            }
        }

        public void setAnimatedReactionDrawable(AnimatedEmojiDrawable animatedEmojiDrawable) {
            if (animatedEmojiDrawable != null) {
                animatedEmojiDrawable.removeView(this);
            }
            this.animatedEmojiDrawable = animatedEmojiDrawable;
            if (!this.attached || animatedEmojiDrawable == null) {
                return;
            }
            animatedEmojiDrawable.addView(this);
        }

        public void setAnimatedEmojiEffect(AnimatedEmojiEffect animatedEmojiEffect) {
            this.emojiEffect = animatedEmojiEffect;
        }
    }

    public static void onScrolled(int i) {
        ReactionsEffectOverlay reactionsEffectOverlay = currentOverlay;
        if (reactionsEffectOverlay != null) {
            reactionsEffectOverlay.lastDrawnToY -= i;
            if (i != 0) {
                reactionsEffectOverlay.wasScrolled = true;
            }
        }
    }

    public static int sizeForBigReaction() {
        int iDp = AndroidUtilities.dp(350.0f);
        Point point = AndroidUtilities.displaySize;
        return (int) (Math.round(Math.min(iDp, Math.min(point.x, point.y)) * 0.7f) / AndroidUtilities.density);
    }

    public static int sizeForAroundReaction() {
        return (int) ((AndroidUtilities.dp(40.0f) * 2.0f) / AndroidUtilities.density);
    }

    public static void dismissAll() {
        ReactionsEffectOverlay reactionsEffectOverlay = currentOverlay;
        if (reactionsEffectOverlay != null) {
            reactionsEffectOverlay.dismissed = true;
        }
        ReactionsEffectOverlay reactionsEffectOverlay2 = currentShortOverlay;
        if (reactionsEffectOverlay2 != null) {
            reactionsEffectOverlay2.dismissed = true;
        }
    }

    private class AvatarParticle {
        float currentRotation;
        float fromX;
        float fromY;
        float globalTranslationY;
        ImageReceiver imageReceiver;
        boolean incrementRotation;
        float jumpY;
        public int leftTime;
        float outProgress;
        float progress;
        float randomRotation;
        float randomScale;
        float toX;
        float toY;

        private AvatarParticle() {
        }
    }
}
