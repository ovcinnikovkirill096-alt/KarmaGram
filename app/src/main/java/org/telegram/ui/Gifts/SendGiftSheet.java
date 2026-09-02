package org.telegram.ui.Gifts;

import android.app.Activity;
import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.text.SpannableStringBuilder;
import android.text.TextUtils;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.TextView;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Collections;
import org.telegram.messenger.AndroidUtilities;
import org.telegram.messenger.AnimationNotificationsLocker;
import org.telegram.messenger.BillingController;
import org.telegram.messenger.BuildVars;
import org.telegram.messenger.GiftAuctionController;
import org.telegram.messenger.LocaleController;
import org.telegram.messenger.MessageObject;
import org.telegram.messenger.MessagesController;
import org.telegram.messenger.NotificationCenter;
import org.telegram.messenger.R;
import org.telegram.messenger.UserConfig;
import org.telegram.messenger.Utilities;
import org.telegram.messenger.browser.Browser;
import org.telegram.tgnet.ConnectionsManager;
import org.telegram.tgnet.TLObject;
import org.telegram.tgnet.TLRPC;
import org.telegram.tgnet.tl.TL_stars;
import org.telegram.ui.ActionBar.BaseFragment;
import org.telegram.ui.ActionBar.BottomSheet;
import org.telegram.ui.ActionBar.INavigationLayout;
import org.telegram.ui.ActionBar.Theme;
import org.telegram.ui.Cells.ChatActionCell;
import org.telegram.ui.Cells.EditEmojiTextCell;
import org.telegram.ui.ChatActivity;
import org.telegram.ui.Components.BottomSheetWithRecyclerListView;
import org.telegram.ui.Components.BulletinFactory;
import org.telegram.ui.Components.ColoredImageSpan;
import org.telegram.ui.Components.Premium.GiftPremiumBottomSheet$GiftTier;
import org.telegram.ui.Components.Premium.boosts.BoostDialogs;
import org.telegram.ui.Components.Premium.boosts.BoostRepository;
import org.telegram.ui.Components.Premium.boosts.PremiumPreviewGiftSentBottomSheet;
import org.telegram.ui.Components.RecyclerListView;
import org.telegram.ui.Components.SizeNotifierFrameLayout;
import org.telegram.ui.Components.TypefaceSpan;
import org.telegram.ui.Components.UItem;
import org.telegram.ui.Components.UniversalAdapter;
import org.telegram.ui.LaunchActivity;
import org.telegram.ui.ProfileActivity;
import org.telegram.ui.Stars.StarGiftSheet;
import org.telegram.ui.Stars.StarsController;
import org.telegram.ui.Stars.StarsIntroActivity;
import org.telegram.ui.Stories.recorder.ButtonWithCounterView;

public class SendGiftSheet extends BottomSheetWithRecyclerListView implements NotificationCenter.NotificationCenterDelegate, GiftAuctionController.OnAuctionUpdateListener {
    private final TLRPC.MessageAction action;
    private final ChatActionCell actionCell;
    private UniversalAdapter adapter;
    public final AnimationNotificationsLocker animationsLock;
    public boolean anonymous;
    private GiftAuctionController.Auction auction;
    private final ButtonWithCounterView button;
    private final LinearLayout buttonContainer;
    private final ColoredImageSpan[] cachedStarSpan;
    private final LinearLayout chatLinearLayout;
    private final SizeNotifierFrameLayout chatView;
    private final Runnable closeParentSheet;
    private final int currentAccount;
    private final long dialogId;
    private final boolean forceNotUpgrade;
    private final boolean forceUpgrade;
    boolean isDismissed;
    private final TextView leftTextView;
    private final TextView leftTextView2;
    private final FrameLayout limitContainer;
    private final FrameLayout limitContainerWrapper;
    private final View limitProgressView;
    private EditEmojiTextCell messageEdit;
    private final MessageObject messageObject;
    private final String name;
    private final GiftPremiumBottomSheet$GiftTier premiumTier;
    private final boolean self;
    private final long send_paid_messages_stars;
    private int shakeDp;
    private final TextView soldTextView;
    private final TextView soldTextView2;
    private final TL_stars.StarGift starGift;
    public boolean upgrade;
    public boolean useStars;
    private final FrameLayout valueContainerView;

    public SendGiftSheet(Context context, int i, TL_stars.StarGift starGift, long j, Runnable runnable, boolean z, boolean z2) {
        this(context, i, starGift, null, j, runnable, z, z2);
    }

    public SendGiftSheet(Context context, int i, GiftPremiumBottomSheet$GiftTier giftPremiumBottomSheet$GiftTier, long j, Runnable runnable) {
        this(context, i, null, giftPremiumBottomSheet$GiftTier, j, runnable, false, false);
    }

    /* JADX WARN: Failed to calculate best type for var: r7v23 ??
    jadx.core.utils.exceptions.JadxRuntimeException: Type update failed for variable: r7v23 ??, new type: long
    	at jadx.core.dex.visitors.typeinference.TypeUpdate.apply(TypeUpdate.java:109)
    	at jadx.core.dex.visitors.typeinference.TypeUpdate.apply(TypeUpdate.java:59)
    	at jadx.core.dex.visitors.typeinference.FixTypesVisitor.calculateFromBounds(FixTypesVisitor.java:159)
    	at jadx.core.dex.visitors.typeinference.FixTypesVisitor.setBestType(FixTypesVisitor.java:136)
    	at jadx.core.dex.visitors.typeinference.FixTypesVisitor.deduceType(FixTypesVisitor.java:241)
    	at jadx.core.dex.visitors.typeinference.FixTypesVisitor.tryDeduceTypes(FixTypesVisitor.java:224)
    	at jadx.core.dex.visitors.typeinference.FixTypesVisitor.visit(FixTypesVisitor.java:94)
    Caused by: java.lang.NullPointerException
     */
    /* JADX WARN: Failed to calculate best type for var: r7v23 ??
    jadx.core.utils.exceptions.JadxRuntimeException: Type update failed for variable: r7v23 ??, new type: long
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
    /* JADX WARN: Failed to calculate best type for var: r7v24 ??
    jadx.core.utils.exceptions.JadxRuntimeException: Type update failed for variable: r7v24 ??, new type: long
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
    /* JADX WARN: Failed to calculate best type for var: r7v38 ??
    jadx.core.utils.exceptions.JadxRuntimeException: Type update failed for variable: r7v38 ??, new type: org.telegram.messenger.MessagesController
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
    /* JADX WARN: Failed to calculate best type for var: r7v39 ??
    jadx.core.utils.exceptions.JadxRuntimeException: Type update failed for variable: r7v39 ??, new type: long
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
    /* JADX WARN: Failed to calculate best type for var: r7v41 ??
    jadx.core.utils.exceptions.JadxRuntimeException: Type update failed for variable: r7v41 ??, new type: long
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
    /* JADX WARN: Failed to calculate best type for var: r7v42 ??
    jadx.core.utils.exceptions.JadxRuntimeException: Type update failed for variable: r7v42 ??, new type: long
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
    /* JADX WARN: Failed to calculate best type for var: r7v43 ??
    jadx.core.utils.exceptions.JadxRuntimeException: Type update failed for variable: r7v43 ??, new type: long
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
        jadx.core.utils.exceptions.JadxRuntimeException: Type update failed for variable: r7v23 ??, new type: long
        	at jadx.core.dex.visitors.typeinference.TypeUpdate.apply(TypeUpdate.java:109)
        	at jadx.core.dex.visitors.typeinference.TypeUpdate.apply(TypeUpdate.java:59)
        	at jadx.core.dex.visitors.typeinference.FixTypesVisitor.tryPossibleTypes(FixTypesVisitor.java:186)
        	at jadx.core.dex.visitors.typeinference.FixTypesVisitor.deduceType(FixTypesVisitor.java:245)
        	at jadx.core.dex.visitors.typeinference.FixTypesVisitor.tryDeduceTypes(FixTypesVisitor.java:224)
        	at jadx.core.dex.visitors.typeinference.FixTypesVisitor.visit(FixTypesVisitor.java:94)
        Caused by: java.lang.NullPointerException
        */
    private SendGiftSheet(android.content.Context r33, int r34, org.telegram.tgnet.tl.TL_stars.StarGift r35, org.telegram.ui.Components.Premium.GiftPremiumBottomSheet$GiftTier r36, long r37, java.lang.Runnable r39, boolean r40, boolean r41) {
        /*
            Method dump skipped, instruction units count: 1461
            To view this dump add '--comments-level debug' option
        */
        throw new UnsupportedOperationException("Method not decompiled: org.telegram.ui.Gifts.SendGiftSheet.<init>(android.content.Context, int, org.telegram.tgnet.tl.TL_stars$StarGift, org.telegram.ui.Components.Premium.GiftPremiumBottomSheet$GiftTier, long, java.lang.Runnable, boolean, boolean):void");
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$new$0(long j, Context context, Runnable runnable, TL_stars.StarGift starGift, View view) {
        if (this.button.isLoading()) {
            return;
        }
        if (this.auction != null) {
            AuctionBidSheet auctionBidSheet = new AuctionBidSheet(context, this.resourcesProvider, new AuctionBidSheet.Params(j, this.anonymous, getMessage()), this.auction);
            auctionBidSheet.show();
            auctionBidSheet.setCloseParentSheet(runnable);
            AndroidUtilities.hideKeyboard(this.messageEdit);
            lambda$new$0();
            if (this.isDismissed) {
                return;
            }
            AndroidUtilities.runOnUIThread(new Runnable() { // from class: org.telegram.ui.Gifts.SendGiftSheet$$ExternalSyntheticLambda6
                @Override // java.lang.Runnable
                public final void run() {
                    this.f$0.lambda$new$0();
                }
            }, 500L);
            return;
        }
        this.button.setLoading(true);
        if (this.messageEdit.editTextEmoji.getEmojiPadding() > 0) {
            this.messageEdit.editTextEmoji.hidePopup(true);
        } else if (this.messageEdit.editTextEmoji.isKeyboardVisible()) {
            this.messageEdit.editTextEmoji.closeKeyboard();
        }
        if (starGift != null) {
            buyStarGift();
        } else {
            buyPremiumTier();
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$new$1(boolean z, boolean z2, TL_stars.StarGift starGift, GiftPremiumBottomSheet$GiftTier giftPremiumBottomSheet$GiftTier, View view, int i) {
        UniversalAdapter universalAdapter = this.adapter;
        if (!this.reverseLayout) {
            i--;
        }
        UItem item = universalAdapter.getItem(i);
        if (item == null) {
            return;
        }
        int i2 = item.id;
        if (i2 == 1) {
            boolean z3 = !this.anonymous;
            this.anonymous = z3;
            TLRPC.MessageAction messageAction = this.action;
            if (messageAction instanceof TLRPC.TL_messageActionStarGift) {
                ((TLRPC.TL_messageActionStarGift) messageAction).name_hidden = z3;
            }
            this.messageObject.updateMessageText();
            this.actionCell.setMessageObject(this.messageObject, true);
            this.adapter.update(true);
            return;
        }
        if (i2 == 2) {
            if (z || z2) {
                int i3 = -this.shakeDp;
                this.shakeDp = i3;
                AndroidUtilities.shakeViewSpring(view, i3);
                return;
            }
            boolean z4 = this.upgrade;
            this.upgrade = !z4;
            TLRPC.MessageAction messageAction2 = this.action;
            if (messageAction2 instanceof TLRPC.TL_messageActionStarGift) {
                TLRPC.TL_messageActionStarGift tL_messageActionStarGift = (TLRPC.TL_messageActionStarGift) messageAction2;
                tL_messageActionStarGift.can_upgrade = !z4 || (this.self && starGift != null && starGift.can_upgrade);
                tL_messageActionStarGift.upgrade_stars = (this.self || z4) ? 0L : this.starGift.upgrade_stars;
                tL_messageActionStarGift.convert_stars = z4 ? this.starGift.convert_stars : 0L;
            }
            this.messageObject.updateMessageText();
            this.actionCell.setMessageObject(this.messageObject, true);
            this.adapter.update(true);
            setButtonText(true);
            return;
        }
        if (i2 == 3) {
            boolean z5 = this.useStars;
            this.useStars = !z5;
            TLRPC.MessageAction messageAction3 = this.action;
            if (messageAction3 instanceof TLRPC.TL_messageActionGiftPremium) {
                TLRPC.TL_messageActionGiftPremium tL_messageActionGiftPremium = (TLRPC.TL_messageActionGiftPremium) messageAction3;
                if (!z5) {
                    tL_messageActionGiftPremium.currency = "XTR";
                    tL_messageActionGiftPremium.amount = giftPremiumBottomSheet$GiftTier.getStarsPrice();
                } else {
                    tL_messageActionGiftPremium.currency = giftPremiumBottomSheet$GiftTier.getCurrency();
                    tL_messageActionGiftPremium.amount = giftPremiumBottomSheet$GiftTier.getPrice();
                }
            } else if (messageAction3 instanceof TLRPC.TL_messageActionGiftCode) {
                TLRPC.TL_messageActionGiftCode tL_messageActionGiftCode = (TLRPC.TL_messageActionGiftCode) messageAction3;
                if (!z5) {
                    tL_messageActionGiftCode.currency = "XTR";
                    tL_messageActionGiftCode.amount = giftPremiumBottomSheet$GiftTier.getStarsPrice();
                } else {
                    tL_messageActionGiftCode.currency = giftPremiumBottomSheet$GiftTier.getCurrency();
                    tL_messageActionGiftCode.amount = giftPremiumBottomSheet$GiftTier.getPrice();
                }
            }
            this.messageObject.updateMessageText();
            this.actionCell.setMessageObject(this.messageObject, true);
            this.adapter.update(true);
            setButtonText(true);
        }
    }

    @Override // org.telegram.messenger.NotificationCenter.NotificationCenterDelegate
    public void didReceivedNotification(int i, int i2, Object... objArr) {
        if (i == NotificationCenter.starBalanceUpdated) {
            setButtonText(true);
            UniversalAdapter universalAdapter = this.adapter;
            if (universalAdapter == null || this.premiumTier == null) {
                return;
            }
            universalAdapter.update(true);
        }
    }

    @Override // android.app.Dialog, android.view.Window.Callback
    public void onAttachedToWindow() {
        super.onAttachedToWindow();
        NotificationCenter.getInstance(this.currentAccount).addObserver(this, NotificationCenter.starBalanceUpdated);
    }

    @Override // android.app.Dialog, android.view.Window.Callback
    public void onDetachedFromWindow() {
        super.onDetachedFromWindow();
        NotificationCenter.getInstance(this.currentAccount).removeObserver(this, NotificationCenter.starBalanceUpdated);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void setButtonText(boolean z) {
        if (this.auction != null) {
            int currentTime = ConnectionsManager.getInstance(this.currentAccount).getCurrentTime();
            if (this.auction.isUpcoming(currentTime)) {
                int i = this.auction.gift.auction_start_date - currentTime;
                this.button.setText(LocaleController.getString(R.string.Gift2AuctionPlaceAEarlyBid), z);
                this.button.setSubText(LocaleController.formatString(R.string.Gift2AuctionStartsIn, LocaleController.formatTTLString(i)), z);
                return;
            }
            TL_stars.TL_starGiftAuctionState tL_starGiftAuctionState = this.auction.auctionStateActive;
            if (tL_starGiftAuctionState != null) {
                int i2 = tL_starGiftAuctionState.end_date - currentTime;
                this.button.setText(LocaleController.getString(R.string.Gift2AuctionPlaceABid), z);
                this.button.setSubText(LocaleController.formatString(R.string.Gift2AuctionTimeLeft, LocaleController.formatTTLString(i2)), z);
                return;
            } else {
                this.button.setText(LocaleController.getString(R.string.Gift2AuctionPlaceABid), z);
                this.button.setSubText(null, z);
                return;
            }
        }
        if (this.starGift != null) {
            long j = StarsController.getInstance(this.currentAccount).getBalance().amount;
            TL_stars.StarGift starGift = this.starGift;
            long j2 = starGift.stars + (this.upgrade ? starGift.upgrade_stars : 0L) + (TextUtils.isEmpty(this.messageEdit.getText()) ? 0L : this.send_paid_messages_stars);
            this.button.setText(StarsIntroActivity.replaceStars(LocaleController.formatPluralStringComma(this.self ? "Gift2SendSelf" : "Gift2Send", (int) j2), this.cachedStarSpan), z);
            if (StarsController.getInstance(this.currentAccount).balanceAvailable() && j2 > j) {
                this.button.setSubText(LocaleController.formatPluralStringComma("Gift2SendYourBalance", (int) j), z);
                return;
            } else {
                this.button.setSubText(null, z);
                return;
            }
        }
        GiftPremiumBottomSheet$GiftTier giftPremiumBottomSheet$GiftTier = this.premiumTier;
        if (giftPremiumBottomSheet$GiftTier != null) {
            if (this.useStars) {
                this.button.setText(StarsIntroActivity.replaceStars(LocaleController.formatString(R.string.Gift2SendPremiumStars, LocaleController.formatNumber(giftPremiumBottomSheet$GiftTier.getStarsPrice(), ',')), 1.0f, this.cachedStarSpan), z);
                this.cachedStarSpan[0].spaceScaleX = 0.85f;
            } else {
                this.button.setText(new SpannableStringBuilder(LocaleController.formatString(R.string.Gift2SendPremium, this.premiumTier.getFormattedPrice())), z);
            }
            this.button.setSubText(null, z);
        }
    }

    @Override // org.telegram.messenger.GiftAuctionController.OnAuctionUpdateListener
    public void onUpdate(GiftAuctionController.Auction auction) {
        this.auction = auction;
    }

    protected BulletinFactory getParentBulletinFactory() {
        BaseFragment safeLastFragment = LaunchActivity.getSafeLastFragment();
        if (safeLastFragment == null) {
            return null;
        }
        return BulletinFactory.of(safeLastFragment);
    }

    private TLRPC.TL_textWithEntities getMessage() {
        if (MessagesController.getInstance(this.currentAccount).getSendPaidMessagesStars(this.dialogId) > 0) {
            return null;
        }
        TLRPC.MessageAction messageAction = this.action;
        if (messageAction instanceof TLRPC.TL_messageActionStarGift) {
            return ((TLRPC.TL_messageActionStarGift) messageAction).message;
        }
        if (messageAction instanceof TLRPC.TL_messageActionGiftCode) {
            return ((TLRPC.TL_messageActionGiftCode) messageAction).message;
        }
        if (messageAction instanceof TLRPC.TL_messageActionGiftPremium) {
            return ((TLRPC.TL_messageActionGiftPremium) messageAction).message;
        }
        return null;
    }

    private void buyStarGift() {
        StarsController.getInstance(this.currentAccount).buyStarGift(this.starGift, this.anonymous, this.upgrade, this.dialogId, getMessage(), new Utilities.Callback2() { // from class: org.telegram.ui.Gifts.SendGiftSheet$$ExternalSyntheticLambda12
            @Override // org.telegram.messenger.Utilities.Callback2
            public final void run(Object obj, Object obj2) {
                this.f$0.lambda$buyStarGift$2((Boolean) obj, (String) obj2);
            }
        });
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$buyStarGift$2(Boolean bool, String str) {
        TL_stars.StarGift starGift;
        if (bool.booleanValue()) {
            Runnable runnable = this.closeParentSheet;
            if (runnable != null) {
                runnable.run();
            }
            AndroidUtilities.hideKeyboard(this.messageEdit);
            lambda$new$0();
        } else if ("STARGIFT_USAGE_LIMITED".equalsIgnoreCase(str)) {
            AndroidUtilities.hideKeyboard(this.messageEdit);
            lambda$new$0();
            StarsController.getInstance(this.currentAccount).makeStarGiftSoldOut(this.starGift);
            return;
        } else if ("STARGIFT_USER_USAGE_LIMITED".equalsIgnoreCase(str)) {
            AndroidUtilities.hideKeyboard(this.messageEdit);
            lambda$new$0();
            BulletinFactory parentBulletinFactory = getParentBulletinFactory();
            if (parentBulletinFactory == null || (starGift = this.starGift) == null || !starGift.limited_per_user) {
                return;
            }
            parentBulletinFactory.createSimpleMultiBulletin(starGift.getDocument(), AndroidUtilities.replaceTags(LocaleController.formatPluralStringComma("Gift2PerUserLimit", this.starGift.per_user_total))).show();
            return;
        }
        this.button.setLoading(false);
    }

    @Override // org.telegram.ui.ActionBar.BottomSheet
    public void onOpenAnimationEnd() {
        super.onOpenAnimationEnd();
        this.recyclerListView.invalidateItemDecorations();
    }

    private void buyPremiumTier() {
        Object starsOption;
        final TLRPC.User user = MessagesController.getInstance(this.currentAccount).getUser(Long.valueOf(this.dialogId));
        if (user == null) {
            this.button.setLoading(false);
            return;
        }
        if (this.useStars && this.premiumTier.isStarsPaymentAvailable()) {
            starsOption = this.premiumTier.getStarsOption();
        } else {
            GiftPremiumBottomSheet$GiftTier giftPremiumBottomSheet$GiftTier = this.premiumTier;
            TLRPC.TL_premiumGiftCodeOption tL_premiumGiftCodeOption = giftPremiumBottomSheet$GiftTier.giftCodeOption;
            if (tL_premiumGiftCodeOption != null) {
                starsOption = tL_premiumGiftCodeOption;
            } else {
                starsOption = giftPremiumBottomSheet$GiftTier.giftOption;
                if (starsOption == null) {
                    this.button.setLoading(false);
                    return;
                }
            }
        }
        if (starsOption instanceof TLRPC.TL_premiumGiftCodeOption) {
            TLRPC.TL_premiumGiftCodeOption tL_premiumGiftCodeOption2 = (TLRPC.TL_premiumGiftCodeOption) starsOption;
            if ("XTR".equalsIgnoreCase(tL_premiumGiftCodeOption2.currency)) {
                StarsController.getInstance(this.currentAccount).buyPremiumGift(this.dialogId, tL_premiumGiftCodeOption2, getMessage(), new Utilities.Callback2() { // from class: org.telegram.ui.Gifts.SendGiftSheet$$ExternalSyntheticLambda7
                    @Override // org.telegram.messenger.Utilities.Callback2
                    public final void run(Object obj, Object obj2) {
                        this.f$0.lambda$buyPremiumTier$4(user, (Boolean) obj, (String) obj2);
                    }
                });
                return;
            } else {
                BoostRepository.payGiftCode(new ArrayList(Arrays.asList(user)), tL_premiumGiftCodeOption2, null, getMessage(), new BaseFragment() { // from class: org.telegram.ui.Gifts.SendGiftSheet.8
                    @Override // org.telegram.ui.ActionBar.BaseFragment
                    public Activity getParentActivity() {
                        Activity ownerActivity = SendGiftSheet.this.getOwnerActivity();
                        if (ownerActivity == null) {
                            ownerActivity = LaunchActivity.instance;
                        }
                        return ownerActivity == null ? AndroidUtilities.findActivity(SendGiftSheet.this.getContext()) : ownerActivity;
                    }

                    @Override // org.telegram.ui.ActionBar.BaseFragment
                    public Theme.ResourcesProvider getResourceProvider() {
                        return ((BottomSheet) SendGiftSheet.this).resourcesProvider;
                    }
                }, new Utilities.Callback() { // from class: org.telegram.ui.Gifts.SendGiftSheet$$ExternalSyntheticLambda8
                    @Override // org.telegram.messenger.Utilities.Callback
                    public final void run(Object obj) {
                        this.f$0.lambda$buyPremiumTier$6(user, (Void) obj);
                    }
                }, new Utilities.Callback() { // from class: org.telegram.ui.Gifts.SendGiftSheet$$ExternalSyntheticLambda9
                    @Override // org.telegram.messenger.Utilities.Callback
                    public final void run(Object obj) {
                        this.f$0.lambda$buyPremiumTier$7((TLRPC.TL_error) obj);
                    }
                });
                return;
            }
        }
        if (starsOption instanceof TLRPC.TL_premiumGiftOption) {
            TLRPC.TL_premiumGiftOption tL_premiumGiftOption = (TLRPC.TL_premiumGiftOption) starsOption;
            if ("XTR".equalsIgnoreCase(tL_premiumGiftOption.currency)) {
                StarsController.getInstance(this.currentAccount).buyPremiumGift(this.dialogId, tL_premiumGiftOption, getMessage(), new Utilities.Callback2() { // from class: org.telegram.ui.Gifts.SendGiftSheet$$ExternalSyntheticLambda10
                    @Override // org.telegram.messenger.Utilities.Callback2
                    public final void run(Object obj, Object obj2) {
                        this.f$0.lambda$buyPremiumTier$9(user, (Boolean) obj, (String) obj2);
                    }
                });
                return;
            }
            if (BuildVars.useInvoiceBilling()) {
                LaunchActivity launchActivity = LaunchActivity.instance;
                if (launchActivity != null) {
                    Uri uri = Uri.parse(tL_premiumGiftOption.bot_url);
                    if (uri.getHost().equals("t.me")) {
                        if (!uri.getPath().startsWith("/$") && !uri.getPath().startsWith("/invoice/")) {
                            launchActivity.setNavigateToPremiumBot(true);
                        } else {
                            launchActivity.setNavigateToPremiumGiftCallback(new Runnable() { // from class: org.telegram.ui.Gifts.SendGiftSheet$$ExternalSyntheticLambda11
                                @Override // java.lang.Runnable
                                public final void run() {
                                    this.f$0.lambda$buyPremiumTier$10();
                                }
                            });
                        }
                    }
                    Browser.openUrl(launchActivity, this.premiumTier.giftOption.bot_url);
                    lambda$new$0();
                    return;
                }
                return;
            }
            if (BillingController.getInstance().isReady()) {
                this.premiumTier.getClass();
            }
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$buyPremiumTier$4(final TLRPC.User user, Boolean bool, String str) {
        if (bool.booleanValue()) {
            Runnable runnable = this.closeParentSheet;
            if (runnable != null) {
                runnable.run();
            }
            AndroidUtilities.hideKeyboard(this.messageEdit);
            lambda$new$0();
            AndroidUtilities.runOnUIThread(new Runnable() { // from class: org.telegram.ui.Gifts.SendGiftSheet$$ExternalSyntheticLambda15
                @Override // java.lang.Runnable
                public final void run() {
                    PremiumPreviewGiftSentBottomSheet.show(new ArrayList(Arrays.asList(user)));
                }
            }, 250L);
        } else if (!TextUtils.isEmpty(str)) {
            BulletinFactory.of(this.topBulletinContainer, this.resourcesProvider).createSimpleBulletin(R.raw.error, LocaleController.formatString(R.string.UnknownErrorCode, str)).show();
        }
        this.button.setLoading(false);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$buyPremiumTier$6(final TLRPC.User user, Void r6) {
        Runnable runnable = this.closeParentSheet;
        if (runnable != null) {
            runnable.run();
        }
        lambda$new$0();
        NotificationCenter.getInstance(UserConfig.selectedAccount).lambda$postNotificationNameOnUIThread$1(NotificationCenter.giftsToUserSent, new Object[0]);
        AndroidUtilities.runOnUIThread(new Runnable() { // from class: org.telegram.ui.Gifts.SendGiftSheet$$ExternalSyntheticLambda13
            @Override // java.lang.Runnable
            public final void run() {
                PremiumPreviewGiftSentBottomSheet.show(new ArrayList(Arrays.asList(user)));
            }
        }, 250L);
        MessagesController.getInstance(this.currentAccount).getMainSettings().edit().putBoolean("show_gift_for_" + this.dialogId, true).putBoolean(Calendar.getInstance().get(1) + "show_gift_for_" + this.dialogId, true).apply();
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$buyPremiumTier$7(TLRPC.TL_error tL_error) {
        BoostDialogs.showToastError(getContext(), tL_error);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$buyPremiumTier$9(final TLRPC.User user, Boolean bool, String str) {
        if (bool.booleanValue()) {
            Runnable runnable = this.closeParentSheet;
            if (runnable != null) {
                runnable.run();
            }
            AndroidUtilities.hideKeyboard(this.messageEdit);
            lambda$new$0();
            AndroidUtilities.runOnUIThread(new Runnable() { // from class: org.telegram.ui.Gifts.SendGiftSheet$$ExternalSyntheticLambda14
                @Override // java.lang.Runnable
                public final void run() {
                    PremiumPreviewGiftSentBottomSheet.show(new ArrayList(Arrays.asList(user)));
                }
            }, 250L);
        } else if (!TextUtils.isEmpty(str)) {
            BulletinFactory.of(this.topBulletinContainer, this.resourcesProvider).createSimpleBulletin(R.raw.error, LocaleController.formatString(R.string.UnknownErrorCode, str)).show();
        }
        this.button.setLoading(false);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$buyPremiumTier$10() {
        onGiftSuccess(false);
    }

    private void onGiftSuccess(boolean z) {
        TLRPC.UserFull userFull = MessagesController.getInstance(this.currentAccount).getUserFull(this.dialogId);
        TLObject userOrChat = MessagesController.getInstance(this.currentAccount).getUserOrChat(this.dialogId);
        int i = 0;
        if (userFull != null && (userOrChat instanceof TLRPC.User)) {
            TLRPC.User user = (TLRPC.User) userOrChat;
            user.premium = true;
            MessagesController.getInstance(this.currentAccount).putUser(user, true);
            NotificationCenter.getInstance(this.currentAccount).lambda$postNotificationNameOnUIThread$1(NotificationCenter.userInfoDidLoad, Long.valueOf(user.id), userFull);
        }
        if (getBaseFragment() != null) {
            ArrayList arrayList = new ArrayList(((LaunchActivity) getBaseFragment().getParentActivity()).getActionBarLayout().getFragmentStack());
            INavigationLayout parentLayout = getBaseFragment().getParentLayout();
            int size = arrayList.size();
            ChatActivity chatActivity = null;
            while (i < size) {
                Object obj = arrayList.get(i);
                i++;
                BaseFragment baseFragment = (BaseFragment) obj;
                if (baseFragment instanceof ChatActivity) {
                    chatActivity = (ChatActivity) baseFragment;
                    if (chatActivity.getDialogId() != this.dialogId) {
                        baseFragment.removeSelfFromStack();
                    }
                } else if (baseFragment instanceof ProfileActivity) {
                    if (z && parentLayout.getLastFragment() == baseFragment) {
                        baseFragment.finishFragment();
                    } else {
                        baseFragment.removeSelfFromStack();
                    }
                }
            }
            if (chatActivity == null || chatActivity.getDialogId() != this.dialogId) {
                Bundle bundle = new Bundle();
                bundle.putLong("user_id", this.dialogId);
                parentLayout.presentFragment(new ChatActivity(bundle), true);
            }
        }
        lambda$new$0();
    }

    @Override // org.telegram.ui.Components.BottomSheetWithRecyclerListView
    protected CharSequence getTitle() {
        return LocaleController.getString(this.self ? R.string.Gift2TitleSelf2 : R.string.Gift2Title);
    }

    @Override // org.telegram.ui.Components.BottomSheetWithRecyclerListView
    protected RecyclerListView.SelectionAdapter createAdapter(RecyclerListView recyclerListView) {
        UniversalAdapter universalAdapter = new UniversalAdapter(this.recyclerListView, getContext(), this.currentAccount, 0, true, new Utilities.Callback2() { // from class: org.telegram.ui.Gifts.SendGiftSheet$$ExternalSyntheticLambda0
            @Override // org.telegram.messenger.Utilities.Callback2
            public final void run(Object obj, Object obj2) {
                this.f$0.fillItems((ArrayList) obj, (UniversalAdapter) obj2);
            }
        }, this.resourcesProvider);
        this.adapter = universalAdapter;
        universalAdapter.setApplyBackground(false);
        return this.adapter;
    }

    /* JADX WARN: Code duplicated, block: B:60:0x013f  */
    public void fillItems(ArrayList arrayList, UniversalAdapter universalAdapter) {
        String string;
        int i;
        String string2;
        CharSequence charSequenceReplaceArrows;
        long sendPaidMessagesStars = MessagesController.getInstance(this.currentAccount).getSendPaidMessagesStars(this.dialogId);
        arrayList.add(UItem.asCustom(-1, this.chatView));
        if (sendPaidMessagesStars <= 0) {
            arrayList.add(UItem.asCustom(-2, this.messageEdit));
            arrayList.add(UItem.asSpace(AndroidUtilities.dp(12.0f)));
        }
        TL_stars.StarGift starGift = this.starGift;
        if (starGift != null) {
            if (starGift.can_upgrade && !this.self) {
                arrayList.add(UItem.asShadow(-3, null));
                arrayList.add(UItem.asCheck(2, StarsIntroActivity.replaceStarsWithPlain(LocaleController.formatString(this.self ? R.string.Gift2UpgradeSelf : R.string.Gift2Upgrade, Integer.valueOf((int) this.starGift.upgrade_stars)), 0.78f)).setChecked(this.upgrade));
                if (this.forceNotUpgrade) {
                    charSequenceReplaceArrows = LocaleController.formatString(this.dialogId < 0 ? R.string.Gift2NoUpgradeChannelForcedInfo : R.string.Gift2NoUpgradeForcedInfo, this.name);
                } else if (this.forceUpgrade) {
                    charSequenceReplaceArrows = LocaleController.formatString(this.dialogId < 0 ? R.string.Gift2UpgradeChannelForcedInfo : R.string.Gift2UpgradeForcedInfo, this.name);
                } else {
                    if (this.self) {
                        string2 = LocaleController.getString(R.string.Gift2UpgradeSelfInfo);
                    } else {
                        string2 = LocaleController.formatString(this.dialogId >= 0 ? R.string.Gift2UpgradeInfo : R.string.Gift2UpgradeChannelInfo, this.name);
                    }
                    charSequenceReplaceArrows = AndroidUtilities.replaceArrows(AndroidUtilities.replaceSingleTag(string2, new Runnable() { // from class: org.telegram.ui.Gifts.SendGiftSheet$$ExternalSyntheticLambda3
                        @Override // java.lang.Runnable
                        public final void run() {
                            this.f$0.lambda$fillItems$15();
                        }
                    }), true);
                }
                arrayList.add(UItem.asShadow(-5, charSequenceReplaceArrows).setEnabled((this.forceUpgrade || this.forceNotUpgrade) ? false : true));
            } else {
                arrayList.add(UItem.asShadow(-5, null));
            }
            arrayList.add(UItem.asCheck(1, LocaleController.getString(this.self ? R.string.Gift2HideSelf : R.string.Gift2Hide)).setChecked(this.anonymous));
            if (this.self) {
                i = R.string.Gift2HideSelfInfo;
            } else {
                if (this.dialogId < 0) {
                    i = R.string.Gift2HideChannelInfo;
                } else {
                    string = LocaleController.formatString(R.string.Gift2HideInfo, this.name);
                }
                arrayList.add(UItem.asShadow(-6, string));
                if (this.limitContainerWrapper != null) {
                    int i2 = this.starGift.gifts_per_round;
                    CharSequence charSequenceReplaceArrows2 = AndroidUtilities.replaceArrows(AndroidUtilities.replaceSingleTag(LocaleController.formatPluralString("Gift2AuctionInfoLearnMore2", i2, Integer.valueOf(i2)), new Runnable() { // from class: org.telegram.ui.Gifts.SendGiftSheet$$ExternalSyntheticLambda4
                        @Override // java.lang.Runnable
                        public final void run() {
                            this.f$0.lambda$fillItems$16();
                        }
                    }), true);
                    arrayList.add(UItem.asCustom(-43, this.limitContainerWrapper));
                    arrayList.add(UItem.asShadow(-44, charSequenceReplaceArrows2));
                }
            }
            string = LocaleController.getString(i);
            arrayList.add(UItem.asShadow(-6, string));
            if (this.limitContainerWrapper != null) {
                int i3 = this.starGift.gifts_per_round;
                CharSequence charSequenceReplaceArrows3 = AndroidUtilities.replaceArrows(AndroidUtilities.replaceSingleTag(LocaleController.formatPluralString("Gift2AuctionInfoLearnMore2", i3, Integer.valueOf(i3)), new Runnable() { // from class: org.telegram.ui.Gifts.SendGiftSheet$$ExternalSyntheticLambda4
                    @Override // java.lang.Runnable
                    public final void run() {
                        this.f$0.lambda$fillItems$16();
                    }
                }), true);
                arrayList.add(UItem.asCustom(-43, this.limitContainerWrapper));
                arrayList.add(UItem.asShadow(-44, charSequenceReplaceArrows3));
            }
        } else {
            if (sendPaidMessagesStars <= 0) {
                arrayList.add(UItem.asShadow(-3, LocaleController.formatString(R.string.Gift2MessagePremiumInfo, this.name)));
            }
            GiftPremiumBottomSheet$GiftTier giftPremiumBottomSheet$GiftTier = this.premiumTier;
            if (giftPremiumBottomSheet$GiftTier != null && giftPremiumBottomSheet$GiftTier.isStarsPaymentAvailable()) {
                arrayList.add(UItem.asCheck(3, StarsIntroActivity.replaceStarsWithPlain(LocaleController.formatString(R.string.Gift2MessageStars, Integer.valueOf((int) this.premiumTier.getStarsPrice())), 0.78f)).setChecked(this.useStars));
                SpannableStringBuilder spannableStringBuilder = new SpannableStringBuilder(LocaleController.formatNumber(StarsController.getInstance(this.currentAccount).getBalance().amount, ','));
                spannableStringBuilder.setSpan(new TypefaceSpan(AndroidUtilities.bold()), 0, spannableStringBuilder.length(), 33);
                arrayList.add(UItem.asShadow(-7, TextUtils.concat(StarsIntroActivity.replaceStarsWithPlain(LocaleController.formatSpannable(R.string.Gift2MessageStarsInfo, spannableStringBuilder), 0.66f), " ", AndroidUtilities.replaceArrows(AndroidUtilities.replaceSingleTag(LocaleController.getString(R.string.Gift2MessageStarsInfoLink), new Runnable() { // from class: org.telegram.ui.Gifts.SendGiftSheet$$ExternalSyntheticLambda5
                    @Override // java.lang.Runnable
                    public final void run() {
                        this.f$0.lambda$fillItems$17();
                    }
                }), true, AndroidUtilities.dp(2.6666667f), AndroidUtilities.dp(1.0f)))));
            }
        }
        if (this.reverseLayout) {
            Collections.reverse(arrayList);
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$fillItems$15() {
        new StarGiftSheet(getContext(), this.currentAccount, this.dialogId, this.resourcesProvider).openAsLearnMore(this.starGift.id, this.name);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$fillItems$16() {
        AuctionJoinSheet.showMoreInfo(getContext(), this.resourcesProvider, this.starGift);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$fillItems$17() {
        new StarsIntroActivity.StarsOptionsSheet(getContext(), this.resourcesProvider).show();
    }

    @Override // org.telegram.ui.ActionBar.BottomSheet, android.app.Dialog
    public void show() {
        EditEmojiTextCell editEmojiTextCell = this.messageEdit;
        if (editEmojiTextCell != null) {
            editEmojiTextCell.editTextEmoji.onResume();
        }
        super.show();
    }

    @Override // org.telegram.ui.ActionBar.BottomSheet, android.app.Dialog, android.content.DialogInterface, org.telegram.ui.ActionBar.BaseFragment.AttachedSheet
    /* JADX INFO: renamed from: dismiss */
    public void lambda$new$0() {
        if (this.messageEdit.editTextEmoji.getEmojiPadding() > 0) {
            this.messageEdit.editTextEmoji.hidePopup(true);
            return;
        }
        if (this.messageEdit.editTextEmoji.isKeyboardVisible()) {
            this.messageEdit.editTextEmoji.closeKeyboard();
            return;
        }
        EditEmojiTextCell editEmojiTextCell = this.messageEdit;
        if (editEmojiTextCell != null) {
            editEmojiTextCell.editTextEmoji.onPause();
        }
        if (this.auction != null) {
            GiftAuctionController.getInstance(this.currentAccount).unsubscribeFromGiftAuction(this.auction.giftId, this);
        }
        this.isDismissed = true;
        super.lambda$new$0();
    }

    @Override // org.telegram.ui.ActionBar.BottomSheet, android.app.Dialog
    /* JADX INFO: renamed from: onBackPressed */
    public void lambda$openCrafting$8() {
        if (this.messageEdit.editTextEmoji.getEmojiPadding() > 0) {
            this.messageEdit.editTextEmoji.hidePopup(true);
        } else if (this.messageEdit.editTextEmoji.isKeyboardVisible()) {
            this.messageEdit.editTextEmoji.closeKeyboard();
        } else {
            super.lambda$openCrafting$8();
        }
    }
}
