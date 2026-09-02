package com.radolyn.ayugram.ui;

import android.content.Context;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffColorFilter;
import android.os.SystemClock;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import com.exteragram.messenger.pillstack.core.PillStackConfig;
import com.exteragram.messenger.pillstack.ui.PillStackPreferencesActivity;
import com.exteragram.messenger.pillstack.ui.pills.BasePill;
import com.google.android.exoplayer2.util.Consumer;
import com.radolyn.ayugram.AyuConstants;
import com.radolyn.ayugram.AyuWorker;
import com.radolyn.ayugram.utils.network.AyuRequestUtils;
import java.util.Calendar;
import java.util.Date;
import okhttp3.internal.url._UrlKt;
import okhttp3.internal.ws.RealWebSocket;
import org.telegram.messenger.AndroidUtilities;
import org.telegram.messenger.LocaleController;
import org.telegram.messenger.NotificationCenter;
import org.telegram.messenger.R;
import org.telegram.messenger.UserConfig;
import org.telegram.tgnet.ConnectionsManager;
import org.telegram.tgnet.TLRPC;
import org.telegram.ui.ActionBar.ActionBarMenuSubItem;
import org.telegram.ui.ActionBar.BaseFragment;
import org.telegram.ui.ActionBar.Theme;
import org.telegram.ui.Components.AnimatedTextView;
import org.telegram.ui.Components.ItemOptions;
import org.telegram.ui.Components.LayoutHelper;
import org.telegram.ui.Components.ScaleStateListAnimator;
import org.telegram.ui.LaunchActivity;

public class LastSeenPill extends BasePill implements NotificationCenter.NotificationCenterDelegate {
    private static final String[] cachedStatusText = new String[16];
    private static final long[] cachedStatusUpdatedAt = new long[16];
    private static final long[] dialogsLifecycleUpdatedAt = new long[16];
    private final Runnable externalUpdateRunnable;
    private final Runnable finishHideRunnable;
    private final Runnable hideStatusRunnable;
    private final ImageView iconView;
    private final LinearLayout layout;
    private boolean pendingPersistentRequest;
    private boolean requestInFlight;
    private boolean statusExpanded;
    private final View textSpacer;
    private final AnimatedTextView textView;
    private boolean waitingForWorkerFetch;

    public LastSeenPill(Context context, Theme.ResourcesProvider resourcesProvider) {
        super(context, resourcesProvider);
        LinearLayout linearLayout = new LinearLayout(context);
        this.layout = linearLayout;
        linearLayout.setOrientation(0);
        linearLayout.setGravity(17);
        linearLayout.setMinimumWidth(AndroidUtilities.dp(48.0f));
        linearLayout.setPadding(AndroidUtilities.dp(8.0f), 0, AndroidUtilities.dp(8.0f), 0);
        addView(linearLayout, LayoutHelper.createFrame(-2, 28, (LocaleController.isRTL ? 3 : 5) | 16));
        ImageView imageView = new ImageView(context);
        this.iconView = imageView;
        imageView.setScaleType(ImageView.ScaleType.CENTER_INSIDE);
        imageView.setImageResource(R.drawable.msg_view_file);
        linearLayout.addView(imageView, LayoutHelper.createLinear(20, 20, 16, 0, 0, 0, 0));
        View view = new View(context);
        this.textSpacer = view;
        linearLayout.addView(view, LayoutHelper.createLinear(0, 0, 16));
        AnimatedTextView animatedTextView = new AnimatedTextView(context, true, true, true);
        this.textView = animatedTextView;
        animatedTextView.setTextSize(AndroidUtilities.dp(13.0f));
        animatedTextView.setTypeface(AndroidUtilities.bold());
        animatedTextView.setIncludeFontPadding(false);
        animatedTextView.adaptWidth = true;
        animatedTextView.setText(_UrlKt.FRAGMENT_ENCODE_SET, false);
        animatedTextView.setVisibility(8);
        linearLayout.addView(animatedTextView, LayoutHelper.createLinear(-2, -2, 16));
        this.hideStatusRunnable = new Runnable() { // from class: com.radolyn.ayugram.ui.LastSeenPill$$ExternalSyntheticLambda0
            @Override // java.lang.Runnable
            public final void run() {
                this.f$0.lambda$new$0();
            }
        };
        this.finishHideRunnable = new Runnable() { // from class: com.radolyn.ayugram.ui.LastSeenPill.1
            @Override // java.lang.Runnable
            public void run() {
                if (LastSeenPill.this.textView.isAnimating()) {
                    LastSeenPill.this.postDelayed(this, 16L);
                } else {
                    if (LastSeenPill.this.statusExpanded || !TextUtils.isEmpty(LastSeenPill.this.textView.getText())) {
                        return;
                    }
                    LastSeenPill.this.textView.setVisibility(8);
                }
            }
        };
        this.externalUpdateRunnable = new Runnable() { // from class: com.radolyn.ayugram.ui.LastSeenPill$$ExternalSyntheticLambda1
            @Override // java.lang.Runnable
            public final void run() {
                this.f$0.lambda$new$1();
            }
        };
        setLoadingTargetView(linearLayout);
        updateColors();
        ScaleStateListAnimator.apply(linearLayout);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$new$0() {
        hideStatusText(true);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$new$1() {
        if (isPeriodicOnlineEnabled()) {
            requestStatus(true);
        }
    }

    @Override // com.exteragram.messenger.pillstack.ui.pills.BasePill
    public long getRefreshInterval() {
        if (isPeriodicOnlineEnabled()) {
            return RealWebSocket.CANCEL_AFTER_CLOSE_MILLIS;
        }
        return 0L;
    }

    @Override // com.exteragram.messenger.pillstack.ui.pills.BasePill
    public int getPillId() {
        return PillStackConfig.PillType.LAST_SEEN.id;
    }

    public static void markDialogsLifecycleRefresh(int i) {
        if (i >= 0) {
            long[] jArr = dialogsLifecycleUpdatedAt;
            if (i >= jArr.length) {
                return;
            }
            jArr[i] = SystemClock.elapsedRealtime();
        }
    }

    @Override // com.exteragram.messenger.pillstack.ui.pills.BasePill
    public void onPillClicked() {
        if (this.loading || this.requestInFlight) {
            return;
        }
        if (isPeriodicOnlineEnabled()) {
            requestStatus(true);
        } else {
            if (this.statusExpanded || this.textView.isAnimating()) {
                return;
            }
            requestStatus(false);
        }
    }

    @Override // com.exteragram.messenger.pillstack.ui.pills.BasePill
    public void onUpdateData(boolean z) {
        if (!isPeriodicOnlineEnabled()) {
            hideStatusText(z);
            markDataUpdated();
        } else {
            if (TextUtils.isEmpty(this.textView.getText())) {
                showCachedStatus();
            }
            requestStatus(true);
        }
    }

    private void requestStatus(boolean z) {
        if (this.requestInFlight || this.waitingForWorkerFetch) {
            return;
        }
        int account = getAccount();
        if (UserConfig.getInstance(account).getClientUserId() == 0) {
            return;
        }
        this.pendingPersistentRequest = z;
        this.waitingForWorkerFetch = true;
        ensureLoadingStarted();
        AyuWorker.requestLastSeenUpdate(account);
    }

    private void requestResolvedStatus() {
        if (this.waitingForWorkerFetch || !this.requestInFlight) {
            int account = getAccount();
            long clientUserId = UserConfig.getInstance(account).getClientUserId();
            if (clientUserId == 0) {
                lambda$requestResolvedStatus$2(null);
                return;
            }
            this.waitingForWorkerFetch = false;
            this.requestInFlight = true;
            AyuRequestUtils.requestUser(account, clientUserId, new Consumer() { // from class: com.radolyn.ayugram.ui.LastSeenPill$$ExternalSyntheticLambda2
                @Override // com.google.android.exoplayer2.util.Consumer
                public final void accept(Object obj) {
                    this.f$0.lambda$requestResolvedStatus$3((TLRPC.User) obj);
                }
            });
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$requestResolvedStatus$3(final TLRPC.User user) {
        AndroidUtilities.runOnUIThread(new Runnable() { // from class: com.radolyn.ayugram.ui.LastSeenPill$$ExternalSyntheticLambda3
            @Override // java.lang.Runnable
            public final void run() {
                this.f$0.lambda$requestResolvedStatus$2(user);
            }
        });
    }

    /* JADX INFO: Access modifiers changed from: private */
    /* JADX INFO: renamed from: finishRequest, reason: merged with bridge method [inline-methods] */
    public void lambda$requestResolvedStatus$2(TLRPC.User user) {
        String cachedStatusText2;
        boolean z = this.pendingPersistentRequest || isPeriodicOnlineEnabled();
        this.pendingPersistentRequest = false;
        this.waitingForWorkerFetch = false;
        this.requestInFlight = false;
        stopLoading();
        if (isAttachedToWindow()) {
            if (!z || isPeriodicOnlineEnabled()) {
                if (user != null) {
                    cachedStatusText2 = getStatusText(user);
                    cacheStatusText(getAccount(), cachedStatusText2);
                } else {
                    cachedStatusText2 = getCachedStatusText(getAccount());
                }
                if (TextUtils.isEmpty(cachedStatusText2)) {
                    return;
                }
                showStatusText(cachedStatusText2, z, true);
                if (z) {
                    markDataUpdated();
                }
            }
        }
    }

    private void showStatusText(CharSequence charSequence, boolean z, boolean z2) {
        removeCallbacks(this.hideStatusRunnable);
        removeCallbacks(this.finishHideRunnable);
        boolean z3 = z2 && !(this.statusExpanded && this.textView.getVisibility() == 0 && TextUtils.equals(this.textView.getText(), charSequence));
        this.statusExpanded = true;
        if (z3) {
            animateSizeChange();
        }
        setTextSpacing(AndroidUtilities.dp(4.0f));
        this.textView.setVisibility(0);
        this.textView.setText(charSequence, z2);
        if (z) {
            return;
        }
        postDelayed(this.hideStatusRunnable, 3000L);
    }

    private void ensureLoadingStarted() {
        if (this.loading) {
            return;
        }
        startLoading();
    }

    private void hideStatusText(boolean z) {
        removeCallbacks(this.hideStatusRunnable);
        removeCallbacks(this.finishHideRunnable);
        if (!this.statusExpanded && this.textView.getVisibility() == 8 && TextUtils.isEmpty(this.textView.getText())) {
            return;
        }
        this.statusExpanded = false;
        if (z) {
            animateSizeChange();
        }
        setTextSpacing(0);
        this.textView.setVisibility(0);
        this.textView.setText(_UrlKt.FRAGMENT_ENCODE_SET, z);
        if (z) {
            post(this.finishHideRunnable);
        } else {
            this.textView.setVisibility(8);
        }
    }

    private void setTextSpacing(int i) {
        LinearLayout.LayoutParams layoutParams = (LinearLayout.LayoutParams) this.textSpacer.getLayoutParams();
        if (layoutParams.width == i) {
            return;
        }
        layoutParams.width = i;
        this.textSpacer.setLayoutParams(layoutParams);
    }

    private int getAccount() {
        return UserConfig.selectedAccount;
    }

    private boolean isPeriodicOnlineEnabled() {
        return PillStackConfig.isLastSeenPeriodicOnlineEnabled(getAccount());
    }

    private boolean showCachedStatus() {
        String cachedStatusText2 = getCachedStatusText(getAccount());
        if (TextUtils.isEmpty(cachedStatusText2)) {
            return false;
        }
        showStatusText(cachedStatusText2, true, false);
        return true;
    }

    private static void cacheStatusText(int i, String str) {
        if (i >= 0) {
            String[] strArr = cachedStatusText;
            if (i >= strArr.length || TextUtils.isEmpty(str)) {
                return;
            }
            strArr[i] = str;
            cachedStatusUpdatedAt[i] = SystemClock.elapsedRealtime();
        }
    }

    private static String getCachedStatusText(int i) {
        if (i < 0) {
            return null;
        }
        String[] strArr = cachedStatusText;
        if (i >= strArr.length) {
            return null;
        }
        return strArr[i];
    }

    private static boolean wasStatusUpdatedRecently(int i) {
        if (i >= 0) {
            long[] jArr = cachedStatusUpdatedAt;
            if (i < jArr.length && !TextUtils.isEmpty(cachedStatusText[i]) && SystemClock.elapsedRealtime() - jArr[i] < 6500) {
                return true;
            }
        }
        return false;
    }

    private static boolean hadRecentDialogsLifecycleRefresh(int i) {
        if (i >= 0) {
            long[] jArr = dialogsLifecycleUpdatedAt;
            if (i < jArr.length && SystemClock.elapsedRealtime() - jArr[i] < 6500) {
                return true;
            }
        }
        return false;
    }

    private boolean tryApplyDialogsLifecycleDebounceOnAttach() {
        int account = getAccount();
        if (!isPeriodicOnlineEnabled() || !hadRecentDialogsLifecycleRefresh(account) || !wasStatusUpdatedRecently(account) || !showCachedStatus()) {
            return false;
        }
        markDataUpdated();
        return true;
    }

    private String getStatusText(TLRPC.User user) {
        int currentTime = ConnectionsManager.getInstance(getAccount()).getCurrentTime();
        TLRPC.UserStatus userStatus = user.status;
        if ((userStatus instanceof TLRPC.TL_userStatusOnline) && userStatus.expires > currentTime) {
            return LocaleController.getString(R.string.Online);
        }
        return formatLastSeen(user);
    }

    private String formatLastSeen(TLRPC.User user) {
        TLRPC.UserStatus userStatus = user.status;
        int i = ((userStatus instanceof TLRPC.TL_userStatusOffline) || (userStatus instanceof TLRPC.TL_userStatusOnline)) ? userStatus.expires : 0;
        if (i <= 0) {
            return LocaleController.getString(R.string.ALongTimeAgo);
        }
        long j = i;
        long j2 = 1000 * j;
        Calendar calendar = Calendar.getInstance();
        Calendar calendar2 = Calendar.getInstance();
        calendar2.setTimeInMillis(j2);
        if (calendar.get(6) == calendar2.get(6) && calendar.get(1) == calendar2.get(1)) {
            return LocaleController.getInstance().getFormatterDay().format(new Date(j2));
        }
        return LocaleController.formatDateTime(j, true);
    }

    @Override // com.exteragram.messenger.pillstack.ui.pills.BasePill
    public boolean onPillLongClicked() {
        final BaseFragment safeLastFragment = LaunchActivity.getSafeLastFragment();
        if (safeLastFragment == null) {
            return false;
        }
        final int account = getAccount();
        final boolean zIsLastSeenPeriodicOnlineEnabled = PillStackConfig.isLastSeenPeriodicOnlineEnabled(account);
        ActionBarMenuSubItem actionBarMenuSubItem = new ActionBarMenuSubItem(safeLastFragment.getContext(), false, false, safeLastFragment.getResourceProvider());
        actionBarMenuSubItem.setTextAndIcon(LocaleController.getString(R.string.LastSeenPillAutoRefresh), R.drawable.menu_clear_recent);
        actionBarMenuSubItem.setSubtext(LocaleController.getString(zIsLastSeenPeriodicOnlineEnabled ? R.string.PasswordOn : R.string.PasswordOff));
        final ItemOptions itemOptionsShow = ItemOptions.makeOptions(safeLastFragment, this).add(actionBarMenuSubItem).addGap().add(R.drawable.msg_retry, LocaleController.getString(R.string.Refresh), new Runnable() { // from class: com.radolyn.ayugram.ui.LastSeenPill$$ExternalSyntheticLambda4
            @Override // java.lang.Runnable
            public final void run() {
                this.f$0.onPillClicked();
            }
        }).add(R.drawable.msg_settings, LocaleController.getString(R.string.Settings), new Runnable() { // from class: com.radolyn.ayugram.ui.LastSeenPill$$ExternalSyntheticLambda5
            @Override // java.lang.Runnable
            public final void run() {
                safeLastFragment.presentFragment(new PillStackPreferencesActivity());
            }
        }).setDrawScrim(false).setDimAlpha(0).show();
        actionBarMenuSubItem.setOnClickListener(new View.OnClickListener() { // from class: com.radolyn.ayugram.ui.LastSeenPill$$ExternalSyntheticLambda6
            @Override // android.view.View.OnClickListener
            public final void onClick(View view) {
                LastSeenPill.m2401$r8$lambda$P6dQxXk80q80TOYhsBbUvW5iiw(account, zIsLastSeenPeriodicOnlineEnabled, itemOptionsShow, view);
            }
        });
        return true;
    }

    /* JADX INFO: renamed from: $r8$lambda$P6dQxXk80q80TOYhsBbUv-W5iiw, reason: not valid java name */
    public static /* synthetic */ void m2401$r8$lambda$P6dQxXk80q80TOYhsBbUvW5iiw(int i, boolean z, ItemOptions itemOptions, View view) {
        PillStackConfig.setLastSeenPeriodicOnlineEnabled(i, !z);
        if (itemOptions != null) {
            itemOptions.dismiss();
        }
    }

    @Override // com.exteragram.messenger.pillstack.ui.pills.BasePill, android.view.ViewGroup, android.view.View
    protected void onAttachedToWindow() {
        super.onAttachedToWindow();
        NotificationCenter.getGlobalInstance().addObserver(this, NotificationCenter.pillStackSettingsChanged);
        NotificationCenter.getGlobalInstance().addObserver(this, AyuConstants.LAST_SEEN_PILL_UPDATE);
        NotificationCenter.getGlobalInstance().addObserver(this, AyuConstants.LAST_SEEN_PILL_FETCH);
        boolean zCheckAndClearPendingUpdate = PillStackConfig.checkAndClearPendingUpdate(getPillId());
        if (tryApplyDialogsLifecycleDebounceOnAttach()) {
            return;
        }
        onUpdateData(zCheckAndClearPendingUpdate);
    }

    @Override // com.exteragram.messenger.pillstack.ui.pills.BasePill, android.view.ViewGroup, android.view.View
    protected void onDetachedFromWindow() {
        super.onDetachedFromWindow();
        NotificationCenter.getGlobalInstance().removeObserver(this, NotificationCenter.pillStackSettingsChanged);
        NotificationCenter.getGlobalInstance().removeObserver(this, AyuConstants.LAST_SEEN_PILL_UPDATE);
        NotificationCenter.getGlobalInstance().removeObserver(this, AyuConstants.LAST_SEEN_PILL_FETCH);
        removeCallbacks(this.hideStatusRunnable);
        removeCallbacks(this.finishHideRunnable);
        removeCallbacks(this.externalUpdateRunnable);
        this.statusExpanded = false;
        this.requestInFlight = false;
        this.waitingForWorkerFetch = false;
        this.pendingPersistentRequest = false;
        setTextSpacing(0);
        stopLoading();
        this.textView.cancelAnimation();
        this.textView.setText(_UrlKt.FRAGMENT_ENCODE_SET, false);
        this.textView.setVisibility(8);
    }

    @Override // org.telegram.messenger.NotificationCenter.NotificationCenterDelegate
    public void didReceivedNotification(int i, int i2, Object... objArr) {
        if (i == NotificationCenter.pillStackSettingsChanged) {
            if (PillStackConfig.shouldUpdatePill(objArr, getPillId())) {
                PillStackConfig.checkAndClearPendingUpdate(getPillId());
                onUpdateData(true);
                return;
            }
            return;
        }
        if (i == AyuConstants.LAST_SEEN_PILL_UPDATE) {
            if (!isPeriodicOnlineEnabled() || objArr == null || objArr.length == 0) {
                return;
            }
            Object obj = objArr[0];
            if ((obj instanceof Integer) && ((Integer) obj).intValue() == getAccount()) {
                removeCallbacks(this.externalUpdateRunnable);
                if (objArr.length > 1) {
                    Object obj2 = objArr[1];
                    if ((obj2 instanceof Boolean) && ((Boolean) obj2).booleanValue()) {
                        if (this.waitingForWorkerFetch || this.requestInFlight || this.loading) {
                            return;
                        }
                        if (wasStatusUpdatedRecently(getAccount()) && showCachedStatus()) {
                            return;
                        }
                    }
                }
                postDelayed(this.externalUpdateRunnable, 250L);
                return;
            }
            return;
        }
        if (i != AyuConstants.LAST_SEEN_PILL_FETCH || objArr == null || objArr.length == 0) {
            return;
        }
        Object obj3 = objArr[0];
        if ((obj3 instanceof Integer) && ((Integer) obj3).intValue() == getAccount()) {
            if (this.waitingForWorkerFetch) {
                requestResolvedStatus();
            } else {
                if (!isPeriodicOnlineEnabled() || this.requestInFlight) {
                    return;
                }
                this.pendingPersistentRequest = true;
                ensureLoadingStarted();
                requestResolvedStatus();
            }
        }
    }

    @Override // android.view.View
    public void setPressed(boolean z) {
        if (this.loading) {
            z = false;
        }
        super.setPressed(z);
        this.layout.setPressed(z);
    }

    @Override // android.view.View
    public void drawableHotspotChanged(float f, float f2) {
        if (this.loading) {
            return;
        }
        super.drawableHotspotChanged(f, f2);
        LinearLayout linearLayout = this.layout;
        linearLayout.drawableHotspotChanged(f - linearLayout.getLeft(), f2 - this.layout.getTop());
    }

    @Override // com.exteragram.messenger.pillstack.ui.pills.BasePill
    public void updateColors() {
        int themedColor = getThemedColor(Theme.key_windowBackgroundWhiteBlackText, 0.75f);
        this.layout.setBackground(Theme.createSimpleSelectorRoundRectDrawable(AndroidUtilities.dp(14.0f), Theme.isCurrentThemeDark() ? getThemedColor(Theme.key_windowBackgroundWhite) : Theme.multAlpha(themedColor, 0.09f), Theme.multAlpha(themedColor, 0.1f)));
        this.textView.setTextColor(themedColor);
        this.iconView.setColorFilter(new PorterDuffColorFilter(themedColor, PorterDuff.Mode.MULTIPLY));
        updateLoadingColors();
    }
}
