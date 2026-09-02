package com.radolyn.ayugram.preferences;

import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.drawable.Drawable;
import android.text.Layout;
import android.text.SpannableStringBuilder;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import androidx.recyclerview.widget.RecyclerView;
import com.exteragram.messenger.pillstack.ui.PillStackPreferencesActivity;
import com.exteragram.messenger.preferences.BasePreferencesActivity;
import com.exteragram.messenger.preferences.appearance.AppNavigationPreferencesActivity;
import com.exteragram.messenger.utils.network.RemoteUtils;
import com.exteragram.messenger.utils.ui.PopupUtils;
import com.google.android.exoplayer2.util.Consumer;
import com.radolyn.ayugram.AyuConfig;
import com.radolyn.ayugram.preferences.components.DeletedMessagePreviewCell;
import com.radolyn.ayugram.ui.AlertUtils;
import com.radolyn.ayugram.utils.AyuMessageUtils;
import com.radolyn.ayugram.utils.fcm.CloudMessagingUtils;
import java.util.ArrayList;
import okhttp3.internal.url._UrlKt;
import org.telegram.messenger.AndroidUtilities;
import org.telegram.messenger.LocaleController;
import org.telegram.messenger.NotificationCenter;
import org.telegram.messenger.R;
import org.telegram.messenger.UserConfig;
import org.telegram.messenger.Utilities;
import org.telegram.ui.ActionBar.BaseFragment;
import org.telegram.ui.ActionBar.Theme;
import org.telegram.ui.Cells.TextCheckCell;
import org.telegram.ui.Components.ColoredImageSpan;
import org.telegram.ui.Components.LayoutHelper;
import org.telegram.ui.Components.Switch;
import org.telegram.ui.Components.UItem;
import org.telegram.ui.Components.UniversalAdapter;
import org.telegram.ui.LaunchActivity;
import org.telegram.ui.PeerColorActivity;
import org.telegram.ui.Stories.recorder.HintView2;

public class CustomizationPreferencesActivity extends BasePreferencesActivity {
    private static ArrayList deletedMarks;
    private PeerColorActivity.PeerColorGrid colorPicker;
    private DeletedMessagesCell deletedMessagesCell;
    private HintView2 keepAliveServiceHint;

    @Override // com.exteragram.messenger.preferences.BasePreferencesActivity
    protected void initializeOptionStrings() {
        deletedMarks = new ArrayList() { // from class: com.radolyn.ayugram.preferences.CustomizationPreferencesActivity.1
            {
                add(LocaleController.getString(R.string.DeletedMarkNothing));
                add(LocaleController.getString(R.string.DeletedMarkTrashBin));
                add(LocaleController.getString(R.string.DeletedMarkCross));
                add(LocaleController.getString(R.string.DeletedMarkEyeCrossed));
            }
        };
    }

    @Override // com.exteragram.messenger.preferences.BasePreferencesActivity, org.telegram.ui.ActionBar.BaseFragment
    public View createView(Context context) {
        CharSequence charSequenceReplaceSingleTag;
        View viewCreateView = super.createView(context);
        FrameLayout frameLayout = (FrameLayout) viewCreateView;
        this.keepAliveServiceHint = new HintView2(getContext(), 1).setMultilineText(true).setTextAlign(Layout.Alignment.ALIGN_NORMAL).setDuration(-1L).setHideByTouch(true).useScale(true).setCloseButton(false).setRounding(8.0f);
        if (CloudMessagingUtils.spoofingNeeded()) {
            charSequenceReplaceSingleTag = _UrlKt.FRAGMENT_ENCODE_SET;
        } else {
            charSequenceReplaceSingleTag = AndroidUtilities.replaceSingleTag(LocaleController.getString(R.string.KeepAliveServiceNote), Theme.key_undo_cancelColor, 0, new Runnable() { // from class: com.radolyn.ayugram.preferences.CustomizationPreferencesActivity$$ExternalSyntheticLambda5
                @Override // java.lang.Runnable
                public final void run() {
                    this.f$0.lambda$createView$0();
                }
            });
        }
        this.keepAliveServiceHint.setText(charSequenceReplaceSingleTag);
        HintView2 hintView2 = this.keepAliveServiceHint;
        hintView2.setMaxWidthPx((int) HintView2.measureCorrectly(hintView2.getText(), this.keepAliveServiceHint.getTextPaint()));
        frameLayout.addView(this.keepAliveServiceHint, LayoutHelper.createFrame(-1, 120.0f, 55, 16.0f, 0.0f, 0.0f, 0.0f));
        this.listView.addOnScrollListener(new RecyclerView.OnScrollListener() { // from class: com.radolyn.ayugram.preferences.CustomizationPreferencesActivity.2
            @Override // androidx.recyclerview.widget.RecyclerView.OnScrollListener
            public void onScrolled(RecyclerView recyclerView, int i, int i2) {
                if (CustomizationPreferencesActivity.this.keepAliveServiceHint == null || !CustomizationPreferencesActivity.this.keepAliveServiceHint.isShown()) {
                    return;
                }
                CustomizationPreferencesActivity.this.keepAliveServiceHint.setTranslationY(Math.max(0.0f, CustomizationPreferencesActivity.this.keepAliveServiceHint.getTranslationY() - i2));
                CustomizationPreferencesActivity.this.hideKeepAliveServiceHint();
            }
        });
        return viewCreateView;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$createView$0() {
        hideKeepAliveServiceHint();
        getMessagesController().openByUserName(RemoteUtils.getStringConfigValue("fcm_channel", "ayugramfcm"), this, 1);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void hideKeepAliveServiceHint() {
        if (this.keepAliveServiceHint.shown()) {
            this.keepAliveServiceHint.hide();
        }
    }

    @Override // com.exteragram.messenger.preferences.BasePreferencesActivity, org.telegram.ui.ActionBar.BaseFragment
    public void onPause() {
        super.onPause();
        hideKeepAliveServiceHint();
    }

    @Override // org.telegram.ui.ActionBar.BaseFragment
    public void onFragmentDestroy() {
        super.onFragmentDestroy();
        hideKeepAliveServiceHint();
    }

    private enum ItemId {
        MESSAGES_PREVIEW,
        SEMI_TRANSPARENT,
        DELETED_ICON,
        DELETED_ICON_COLOR,
        APP_NAVIGATION_SETTINGS,
        PILL_STACK,
        KEEP_ALIVE,
        LOCAL_PREMIUM,
        DISPLAY_GHOST_STATUS,
        DISABLE_ADS;

        public int getId() {
            return ordinal() + 1;
        }
    }

    @Override // com.exteragram.messenger.preferences.BasePreferencesActivity
    protected void fillItems(ArrayList arrayList, UniversalAdapter universalAdapter) {
        arrayList.add(UItem.asHeader(LocaleController.getString(R.string.CategoryCustomization)));
        if (this.deletedMessagesCell == null) {
            this.deletedMessagesCell = new DeletedMessagesCell(getContext());
        }
        arrayList.add(UItem.asCustom(ItemId.MESSAGES_PREVIEW.getId(), this.deletedMessagesCell));
        arrayList.add(UItem.asCheck(ItemId.SEMI_TRANSPARENT.getId(), LocaleController.getString(R.string.SemiTransparentDeletedMessages)).setChecked(AyuConfig.semiTransparentDeletedMessages).setSearchable(this).setLinkAlias("translucentDeletedMessages", this));
        Drawable deletedIconPreviewDrawable = AyuMessageUtils.getDeletedIconPreviewDrawable();
        SpannableStringBuilder spannableStringBuilder = new SpannableStringBuilder("\u200b");
        if (deletedIconPreviewDrawable != null) {
            ColoredImageSpan coloredImageSpan = new ColoredImageSpan(deletedIconPreviewDrawable);
            coloredImageSpan.setOverrideColor(Theme.getColor(Theme.key_chat_inTimeText));
            coloredImageSpan.setTranslateX(-AndroidUtilities.dp(7.0f));
            spannableStringBuilder.setSpan(coloredImageSpan, 0, 1, 33);
        }
        arrayList.add(UItem.asButton(ItemId.DELETED_ICON.getId(), LocaleController.getString(R.string.DeletedMarkText), spannableStringBuilder).setSearchable(this).setLinkAlias("deletedMark", this));
        if (AyuConfig.deletedIcon != 0) {
            if (this.colorPicker == null) {
                this.colorPicker = new PeerColorActivity.PeerColorGrid(getContext(), 10, this.currentAccount, this.resourceProvider);
                int color = Theme.getColor(Theme.key_chat_inTimeText);
                int[] iArr = AyuMessageUtils.deletedColors;
                this.colorPicker.setOverrideColors(new int[]{color, iArr[0], iArr[1], iArr[2], iArr[3], iArr[4], iArr[5], iArr[6]});
                this.colorPicker.setBackgroundColor(getThemedColor(Theme.key_windowBackgroundWhite));
                this.colorPicker.setSelected(AyuConfig.deletedIconColor, false);
                this.colorPicker.setOnColorClick(new Utilities.Callback() { // from class: com.radolyn.ayugram.preferences.CustomizationPreferencesActivity$$ExternalSyntheticLambda6
                    @Override // org.telegram.messenger.Utilities.Callback
                    public final void run(Object obj) {
                        this.f$0.lambda$fillItems$1((Integer) obj);
                    }
                });
                this.colorPicker.setDivider(false);
            }
            arrayList.add(UItem.asCustom(ItemId.DELETED_ICON_COLOR.getId(), this.colorPicker).setLinkAlias("deletedMarkColor", this));
        }
        arrayList.add(UItem.asShadow());
        arrayList.add(UItem.asButtonWithSubtext(ItemId.APP_NAVIGATION_SETTINGS.getId(), R.drawable.msg_newphone, LocaleController.getString(R.string.AppNavigation), LocaleController.getString(R.string.AppNavigationInfo), 64, 60));
        arrayList.add(UItem.asButtonWithSubtext(ItemId.PILL_STACK.getId(), R.drawable.ic_ab_search, LocaleController.getString(R.string.PillStackPills), LocaleController.getString(R.string.PillStackPillsInfo), 64, 60));
        arrayList.add(UItem.asShadow());
        arrayList.add(UItem.asHeader(LocaleController.getString(R.string.QoLTogglesHeader)));
        arrayList.add(UItem.asCheck(ItemId.KEEP_ALIVE.getId(), LocaleController.getString(R.string.KeepAliveService)).setChecked(AyuConfig.keepAliveService).setSearchable(this).setLinkAlias("keepAliveService", this));
        arrayList.add(UItem.asCheck(ItemId.LOCAL_PREMIUM.getId(), LocaleController.getString(R.string.LocalPremium)).setChecked(AyuConfig.localPremium).setSearchable(this).setLinkAlias("localPremium", this));
        arrayList.add(UItem.asCheck(ItemId.DISABLE_ADS.getId(), LocaleController.getString(R.string.DisableAds)).setChecked(AyuConfig.disableAds).setSearchable(this).setLinkAlias("disableAds", this));
        arrayList.add(UItem.asCheck(ItemId.DISPLAY_GHOST_STATUS.getId(), LocaleController.getString(R.string.DisplayGhostStatus)).setChecked(AyuConfig.displayGhostStatus).setSearchable(this).setLinkAlias("displayGhostStatus", this));
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$fillItems$1(Integer num) {
        this.colorPicker.setSelected(num.intValue(), true);
        SharedPreferences.Editor editor = AyuConfig.editor;
        int iIntValue = num.intValue();
        AyuConfig.deletedIconColor = iIntValue;
        editor.putInt("deletedIconColor", iIntValue).apply();
        AyuMessageUtils.reinitializeIcons();
        this.deletedMessagesCell.invalidateTime();
    }

    private void toggleLocalPremium(UItem uItem, View view, float f, float f2) {
        toggleBooleanSettingAndRefresh(AyuConfig.preferences, "localPremium", uItem, new Consumer() { // from class: com.radolyn.ayugram.preferences.CustomizationPreferencesActivity$$ExternalSyntheticLambda7
            @Override // com.google.android.exoplayer2.util.Consumer
            public final void accept(Object obj) {
                AyuConfig.localPremium = ((Boolean) obj).booleanValue();
            }
        });
        getMessagesController().updatePremium(AyuConfig.localPremium);
        boolean z = false;
        NotificationCenter.getInstance(this.currentAccount).lambda$postNotificationNameOnUIThread$1(NotificationCenter.currentUserPremiumStatusChanged, new Object[0]);
        NotificationCenter.getGlobalInstance().lambda$postNotificationNameOnUIThread$1(NotificationCenter.premiumStatusChangedGlobal, new Object[0]);
        getMediaDataController().loadPremiumPromo(false);
        getMediaDataController().loadReactions(false, null);
        if (AyuConfig.localPremium) {
            while (view != getFragmentView()) {
                if (view.getParent() == null) {
                    z = true;
                    break;
                } else {
                    f += view.getX();
                    f2 += view.getY();
                    view = (View) view.getParent();
                }
            }
            float top = f2 + getFragmentView().getTop();
            if (AndroidUtilities.isTablet()) {
                ViewGroup view2 = getParentLayout().getView();
                f += view2.getX() + view2.getPaddingLeft();
                top += view2.getY() + view2.getPaddingTop();
            }
            if (!z) {
                LaunchActivity.makeRipple(f, top, 0.9f);
            }
            if (AyuConfig.sawLocalPremiumAlert) {
                return;
            }
            AlertUtils.showLocalPremiumAlert(this);
        }
    }

    @Override // com.exteragram.messenger.preferences.BasePreferencesActivity
    protected void onClick(UItem uItem, View view, int i, float f, float f2) {
        int i2 = uItem.id;
        if (i2 <= 0 || i2 > ItemId.values().length) {
            return;
        }
        switch (AnonymousClass3.$SwitchMap$com$radolyn$ayugram$preferences$CustomizationPreferencesActivity$ItemId[ItemId.values()[uItem.id - 1].ordinal()]) {
            case 1:
                toggleBooleanSettingAndRefresh(AyuConfig.preferences, "semiTransparentDeletedMessages", uItem, new Consumer() { // from class: com.radolyn.ayugram.preferences.CustomizationPreferencesActivity$$ExternalSyntheticLambda0
                    @Override // com.google.android.exoplayer2.util.Consumer
                    public final void accept(Object obj) {
                        AyuConfig.semiTransparentDeletedMessages = ((Boolean) obj).booleanValue();
                    }
                });
                DeletedMessagesCell deletedMessagesCell = this.deletedMessagesCell;
                if (deletedMessagesCell != null) {
                    deletedMessagesCell.animateSemiTransparency(AyuConfig.semiTransparentDeletedMessages);
                }
                break;
            case 2:
                showListDialog(uItem, (CharSequence[]) deletedMarks.toArray(new CharSequence[0]), LocaleController.getString(R.string.DeletedMarkText), AyuConfig.deletedIcon, new PopupUtils.OnItemClickListener() { // from class: com.radolyn.ayugram.preferences.CustomizationPreferencesActivity$$ExternalSyntheticLambda1
                    @Override // com.exteragram.messenger.utils.ui.PopupUtils.OnItemClickListener
                    public final void onClick(int i3) {
                        this.f$0.lambda$onClick$4(i3);
                    }
                });
                break;
            case 3:
                presentFragment(new AppNavigationPreferencesActivity());
                break;
            case 4:
                presentFragment(new PillStackPreferencesActivity());
                break;
            case 5:
                toggleBooleanSettingAndRefresh(AyuConfig.preferences, "keepAliveService", uItem, new Consumer() { // from class: com.radolyn.ayugram.preferences.CustomizationPreferencesActivity$$ExternalSyntheticLambda2
                    @Override // com.google.android.exoplayer2.util.Consumer
                    public final void accept(Object obj) {
                        AyuConfig.keepAliveService = ((Boolean) obj).booleanValue();
                    }
                });
                if (!CloudMessagingUtils.spoofingNeeded()) {
                    Switch checkBox = ((TextCheckCell) view).getCheckBox();
                    int[] iArr = new int[2];
                    checkBox.getLocationInWindow(iArr);
                    if (AndroidUtilities.isTablet()) {
                        ViewGroup view2 = getParentLayout().getView();
                        iArr[0] = iArr[0] + ((int) (view2.getX() + view2.getPaddingLeft()));
                        iArr[1] = iArr[1] - ((int) (view2.getY() + view2.getPaddingTop()));
                    }
                    this.keepAliveServiceHint.setTranslationY(((iArr[1] + checkBox.getHeight()) - this.fragmentView.getTop()) + AndroidUtilities.dp(4.0f));
                    this.keepAliveServiceHint.setJointPx(0.0f, this.fragmentView.getWidth() - (checkBox.getWidth() * 1.5f));
                    this.keepAliveServiceHint.show();
                }
                break;
            case 6:
                toggleLocalPremium(uItem, view, f, f2);
                break;
            case 7:
                toggleBooleanSettingAndRefresh(AyuConfig.preferences, "displayGhostStatus", uItem, new Consumer() { // from class: com.radolyn.ayugram.preferences.CustomizationPreferencesActivity$$ExternalSyntheticLambda3
                    @Override // com.google.android.exoplayer2.util.Consumer
                    public final void accept(Object obj) {
                        AyuConfig.displayGhostStatus = ((Boolean) obj).booleanValue();
                    }
                });
                NotificationCenter.getInstance(UserConfig.selectedAccount).lambda$postNotificationNameOnUIThread$1(NotificationCenter.currentUserPremiumStatusChanged, new Object[0]);
                break;
            case 8:
                toggleBooleanSettingAndRefresh(AyuConfig.preferences, "disableAds", uItem, new Consumer() { // from class: com.radolyn.ayugram.preferences.CustomizationPreferencesActivity$$ExternalSyntheticLambda4
                    @Override // com.google.android.exoplayer2.util.Consumer
                    public final void accept(Object obj) {
                        AyuConfig.disableAds = ((Boolean) obj).booleanValue();
                    }
                });
                break;
            default:
                break;
        }
    }

    /* JADX INFO: renamed from: com.radolyn.ayugram.preferences.CustomizationPreferencesActivity$3, reason: invalid class name */
    static /* synthetic */ class AnonymousClass3 {
        static final /* synthetic */ int[] $SwitchMap$com$radolyn$ayugram$preferences$CustomizationPreferencesActivity$ItemId;

        static {
            int[] iArr = new int[ItemId.values().length];
            $SwitchMap$com$radolyn$ayugram$preferences$CustomizationPreferencesActivity$ItemId = iArr;
            try {
                iArr[ItemId.SEMI_TRANSPARENT.ordinal()] = 1;
            } catch (NoSuchFieldError unused) {
            }
            try {
                $SwitchMap$com$radolyn$ayugram$preferences$CustomizationPreferencesActivity$ItemId[ItemId.DELETED_ICON.ordinal()] = 2;
            } catch (NoSuchFieldError unused2) {
            }
            try {
                $SwitchMap$com$radolyn$ayugram$preferences$CustomizationPreferencesActivity$ItemId[ItemId.APP_NAVIGATION_SETTINGS.ordinal()] = 3;
            } catch (NoSuchFieldError unused3) {
            }
            try {
                $SwitchMap$com$radolyn$ayugram$preferences$CustomizationPreferencesActivity$ItemId[ItemId.PILL_STACK.ordinal()] = 4;
            } catch (NoSuchFieldError unused4) {
            }
            try {
                $SwitchMap$com$radolyn$ayugram$preferences$CustomizationPreferencesActivity$ItemId[ItemId.KEEP_ALIVE.ordinal()] = 5;
            } catch (NoSuchFieldError unused5) {
            }
            try {
                $SwitchMap$com$radolyn$ayugram$preferences$CustomizationPreferencesActivity$ItemId[ItemId.LOCAL_PREMIUM.ordinal()] = 6;
            } catch (NoSuchFieldError unused6) {
            }
            try {
                $SwitchMap$com$radolyn$ayugram$preferences$CustomizationPreferencesActivity$ItemId[ItemId.DISPLAY_GHOST_STATUS.ordinal()] = 7;
            } catch (NoSuchFieldError unused7) {
            }
            try {
                $SwitchMap$com$radolyn$ayugram$preferences$CustomizationPreferencesActivity$ItemId[ItemId.DISABLE_ADS.ordinal()] = 8;
            } catch (NoSuchFieldError unused8) {
            }
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$onClick$4(int i) {
        SharedPreferences.Editor editor = AyuConfig.editor;
        AyuConfig.deletedIcon = i;
        editor.putInt("deletedIcon", i).apply();
        this.parentLayout.rebuildAllFragmentViews(false, false);
        DeletedMessagesCell deletedMessagesCell = this.deletedMessagesCell;
        if (deletedMessagesCell != null) {
            deletedMessagesCell.invalidateTime();
        }
    }

    @Override // com.exteragram.messenger.preferences.BasePreferencesActivity
    public String getTitle() {
        return LocaleController.getString(R.string.CategoryCustomization);
    }

    private class DeletedMessagesCell extends FrameLayout {
        private final DeletedMessagePreviewCell messagesCell;

        public DeletedMessagesCell(Context context) {
            super(context);
            setBackgroundColor(Theme.getColor(Theme.key_windowBackgroundWhite));
            setWillNotDraw(false);
            DeletedMessagePreviewCell deletedMessagePreviewCell = new DeletedMessagePreviewCell(context, ((BaseFragment) CustomizationPreferencesActivity.this).parentLayout);
            this.messagesCell = deletedMessagePreviewCell;
            deletedMessagePreviewCell.setImportantForAccessibility(4);
            addView(deletedMessagePreviewCell, LayoutHelper.createFrame(-1, -2.0f, 51, 0.0f, 0.0f, 0.0f, 0.0f));
        }

        @Override // android.view.View
        public void invalidate() {
            super.invalidate();
            this.messagesCell.invalidate();
        }

        public void invalidateTime() {
            this.messagesCell.invalidateTime();
        }

        public void animateSemiTransparency(boolean z) {
            this.messagesCell.animateSemiTransparency(z);
        }
    }
}
