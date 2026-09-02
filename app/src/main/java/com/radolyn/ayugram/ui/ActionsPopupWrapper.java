package com.radolyn.ayugram.ui;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Canvas;
import android.graphics.drawable.Drawable;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import androidx.core.content.ContextCompat;
import com.exteragram.messenger.utils.system.VibratorUtils;
import com.radolyn.ayugram.AyuConfig;
import com.radolyn.ayugram.AyuConstants;
import com.radolyn.ayugram.AyuGhostExclusions;
import com.radolyn.ayugram.preferences.FiltersListPreferencesActivity;
import com.radolyn.ayugram.utils.AyuLocalDatabaseUtils;
import java.util.ArrayList;
import org.telegram.messenger.AndroidUtilities;
import org.telegram.messenger.ChatObject;
import org.telegram.messenger.LocaleController;
import org.telegram.messenger.NotificationCenter;
import org.telegram.messenger.R;
import org.telegram.messenger.SendMessagesHelper;
import org.telegram.messenger.UserObject;
import org.telegram.tgnet.TLRPC;
import org.telegram.ui.ActionBar.ActionBarMenuSubItem;
import org.telegram.ui.ActionBar.AlertDialog;
import org.telegram.ui.ActionBar.BaseFragment;
import org.telegram.ui.ActionBar.BottomSheet;
import org.telegram.ui.ActionBar.Theme;
import org.telegram.ui.ChatActivity;
import org.telegram.ui.Components.AlertsCreator;
import org.telegram.ui.Components.AnimatedFloat;
import org.telegram.ui.Components.CubicBezierInterpolator;
import org.telegram.ui.Components.LayoutHelper;
import org.telegram.ui.Components.PopupSwipeBackLayout;

public class ActionsPopupWrapper {
    private boolean filteringSwitched = false;
    private final ChatActivity fragment;
    private final Theme.ResourcesProvider resourcesProvider;
    public LinearLayout swipeBack;
    private ActionBarMenuSubItem switchFilteringItem;

    public ActionsPopupWrapper(ChatActivity chatActivity, final PopupSwipeBackLayout popupSwipeBackLayout, TLRPC.User user, final TLRPC.Chat chat, final long j, final long j2, final Runnable runnable, ChatActivity.ThemeDelegate themeDelegate) {
        int i;
        final TLRPC.User user2 = user;
        final ChatActivity.ThemeDelegate themeDelegate2 = themeDelegate;
        boolean z = false;
        final ChatActivity chatActivity2 = chatActivity;
        this.fragment = chatActivity2;
        this.resourcesProvider = themeDelegate2;
        Activity parentActivity = chatActivity2.getParentActivity();
        LinearLayout linearLayout = new LinearLayout(parentActivity);
        this.swipeBack = linearLayout;
        linearLayout.setOrientation(1);
        ScrollView scrollView = new ScrollView(parentActivity) { // from class: com.radolyn.ayugram.ui.ActionsPopupWrapper.1
            final AnimatedFloat alphaFloat = new AnimatedFloat(this, 350, CubicBezierInterpolator.EASE_OUT_QUINT);
            Drawable topShadowDrawable;
            private boolean wasCanScrollVertically;

            @Override // android.widget.ScrollView, android.view.ViewGroup, android.view.ViewParent
            public void onNestedScroll(View view, int i2, int i3, int i4, int i5) {
                super.onNestedScroll(view, i2, i3, i4, i5);
                boolean zCanScrollVertically = canScrollVertically(-1);
                if (this.wasCanScrollVertically != zCanScrollVertically) {
                    invalidate();
                    this.wasCanScrollVertically = zCanScrollVertically;
                }
            }

            @Override // android.view.ViewGroup, android.view.View
            protected void dispatchDraw(Canvas canvas) {
                super.dispatchDraw(canvas);
                float f = this.alphaFloat.set(canScrollVertically(-1) ? 1.0f : 0.0f) * 0.5f;
                if (f > 0.0f) {
                    if (this.topShadowDrawable == null) {
                        this.topShadowDrawable = ContextCompat.getDrawable(getContext(), R.drawable.header_shadow);
                    }
                    Drawable drawable = this.topShadowDrawable;
                    if (drawable != null) {
                        drawable.setBounds(0, getScrollY(), getWidth(), getScrollY() + this.topShadowDrawable.getIntrinsicHeight());
                        this.topShadowDrawable.setAlpha((int) (f * 255.0f));
                        this.topShadowDrawable.draw(canvas);
                    }
                }
            }
        };
        LinearLayout linearLayout2 = new LinearLayout(parentActivity);
        scrollView.addView(linearLayout2);
        linearLayout2.setOrientation(1);
        ActionBarMenuSubItem actionBarMenuSubItem = new ActionBarMenuSubItem((Context) chatActivity2.getParentActivity(), true, false, (Theme.ResourcesProvider) themeDelegate2);
        actionBarMenuSubItem.setItemHeight(44);
        actionBarMenuSubItem.setTextAndIcon(LocaleController.getString(R.string.Back), R.drawable.msg_arrow_back);
        actionBarMenuSubItem.getTextView().setPadding(LocaleController.isRTL ? 0 : AndroidUtilities.dp(40.0f), 0, LocaleController.isRTL ? AndroidUtilities.dp(40.0f) : 0, 0);
        actionBarMenuSubItem.setOnClickListener(new View.OnClickListener() { // from class: com.radolyn.ayugram.ui.ActionsPopupWrapper$$ExternalSyntheticLambda0
            @Override // android.view.View.OnClickListener
            public final void onClick(View view) {
                popupSwipeBackLayout.closeForeground();
            }
        });
        int i2 = -1;
        this.swipeBack.addView(actionBarMenuSubItem, LayoutHelper.createLinear(-1, -2));
        int i3 = 8;
        linearLayout2.addView(createGap(), LayoutHelper.createLinear(-1, 8));
        ArrayList arrayList = new ArrayList();
        if (AyuConfig.filtersEnabled) {
            if (user2 == null || user2.bot) {
                arrayList.add(new Item(AyuConstants.OPTION_VIEW_FILTERS, R.drawable.msg_addfolder, LocaleController.getString(R.string.ViewFiltersMenuText), null));
            }
            if (AyuConfig.regexFiltersInChats || user2 == null || user2.bot) {
                arrayList.add(new Item(AyuConstants.OPTION_SWITCH_FILTERING, R.drawable.msg_clear_recent, LocaleController.getString(R.string.ShowFilteredMessagesMenuText), null));
            }
        }
        arrayList.add(new Item(AyuConstants.OPTION_DELETED_HISTORY, R.drawable.msg_archive, LocaleController.getString(R.string.ViewDeletedMenuText), null));
        if (!ChatObject.isChannelAndNotMegaGroup(chat) && !UserObject.isUserSelf(user2)) {
            arrayList.add(new Item(AyuConstants.OPTION_GHOST_READ_EXCLUSION, R.drawable.msg_view_file, LocaleController.getString(R.string.ReadExclusionMenuText), null));
            arrayList.add(new Item(AyuConstants.OPTION_GHOST_TYPING_EXCLUSION, R.drawable.msg_edit, LocaleController.getString(R.string.TypingExclusionMenuText), null));
        }
        if (user2 != null && AyuConfig.showScreenshot) {
            arrayList.add(new Item(AyuConstants.OPTION_DEBUG_SEND_SCREENSHOT, R.drawable.msg_camera, "Send Screenshot", null));
        }
        arrayList.add(null);
        arrayList.add(new Item(AyuConstants.OPTION_CLEAR_DELETED, R.drawable.msg_clear, LocaleController.getString(R.string.ClearDeletedMenuText), null, Theme.getColor(Theme.key_text_RedBold)));
        if (arrayList.get(arrayList.size() - 1) == null) {
            arrayList.remove(arrayList.size() - 1);
        }
        int size = arrayList.size();
        int i4 = 0;
        int i5 = 0;
        while (i5 < size) {
            i5++;
            final Item item = (Item) arrayList.get(i5);
            if (item == null) {
                linearLayout2.addView(createGap(), LayoutHelper.createLinear(i2, i3));
                i4 += 8;
            } else {
                final ActionBarMenuSubItem actionBarMenuSubItem2 = new ActionBarMenuSubItem(chatActivity2.getParentActivity(), z, z, themeDelegate2);
                actionBarMenuSubItem2.setTextAndIcon(item.title, item.resId);
                actionBarMenuSubItem2.setMinimumWidth(AndroidUtilities.dp(196.0f));
                int i6 = item.color;
                if (i6 != 0) {
                    actionBarMenuSubItem2.setColors(i6, i6);
                }
                linearLayout2.addView(actionBarMenuSubItem2, LayoutHelper.createLinear(i2, 48));
                int i7 = i4 + 48;
                String str = item.subtitle;
                if (str != null) {
                    actionBarMenuSubItem2.setSubtext(str);
                    actionBarMenuSubItem2.setItemHeight(56);
                    i = i4 + 56;
                } else {
                    i = i7;
                }
                ArrayList arrayList2 = arrayList;
                int i8 = size;
                actionBarMenuSubItem2.setOnClickListener(new View.OnClickListener() { // from class: com.radolyn.ayugram.ui.ActionsPopupWrapper$$ExternalSyntheticLambda1
                    @Override // android.view.View.OnClickListener
                    public final void onClick(View view) {
                        ActionsPopupWrapper.$r8$lambda$UAceuZCL_NhGFSW93nhq4_nEqpI(runnable, item, chatActivity2, j, j2, themeDelegate2, actionBarMenuSubItem2, chat, user2, view);
                    }
                });
                if (item.id == AyuConstants.OPTION_SWITCH_FILTERING) {
                    actionBarMenuSubItem2.setVisibility(8);
                    this.switchFilteringItem = actionBarMenuSubItem2;
                }
                actionBarMenuSubItem2.setTag(item);
                chatActivity2 = chatActivity;
                user2 = user;
                themeDelegate2 = themeDelegate;
                i3 = 8;
                i4 = i;
                arrayList = arrayList2;
                size = i8;
                i2 = -1;
                z = false;
            }
        }
        if (i4 > 436 && Math.abs(i4 - 436) > 112) {
            this.swipeBack.addView(scrollView, LayoutHelper.createLinear(-1, 436));
        } else {
            this.swipeBack.addView(scrollView, LayoutHelper.createLinear(-1, -2));
        }
    }

    public static /* synthetic */ void $r8$lambda$UAceuZCL_NhGFSW93nhq4_nEqpI(Runnable runnable, Item item, final ChatActivity chatActivity, final long j, final long j2, ChatActivity.ThemeDelegate themeDelegate, final ActionBarMenuSubItem actionBarMenuSubItem, TLRPC.Chat chat, TLRPC.User user, View view) {
        runnable.run();
        int i = item.id;
        if (i == AyuConstants.OPTION_CLEAR_DELETED) {
            AlertDialog alertDialogCreate = AlertsCreator.createSimpleAlert(chatActivity.getContext(), LocaleController.getString(R.string.ClearDeletedMessagesTitle), LocaleController.getString(R.string.ClearDeletedMessagesText), LocaleController.getString(R.string.ClearDeletedMessagesActionText), new Runnable() { // from class: com.radolyn.ayugram.ui.ActionsPopupWrapper$$ExternalSyntheticLambda2
                @Override // java.lang.Runnable
                public final void run() {
                    ActionsPopupWrapper.$r8$lambda$VoWTsq0ePyI3ieB4UwnLMLJYxzA(chatActivity, j, j2);
                }
            }, themeDelegate).create();
            alertDialogCreate.show();
            TextView textView = (TextView) alertDialogCreate.getButton(-1);
            if (textView != null) {
                textView.setTextColor(Theme.getColor(Theme.key_text_RedBold));
                return;
            }
            return;
        }
        if (i == AyuConstants.OPTION_VIEW_FILTERS) {
            chatActivity.presentFragment(new FiltersListPreferencesActivity(Long.valueOf(j)));
            return;
        }
        if (i == AyuConstants.OPTION_SWITCH_FILTERING) {
            chatActivity.switchHideFilteredMessages();
            AndroidUtilities.runOnUIThread(new Runnable() { // from class: com.radolyn.ayugram.ui.ActionsPopupWrapper$$ExternalSyntheticLambda3
                @Override // java.lang.Runnable
                public final void run() {
                    actionBarMenuSubItem.setText(LocaleController.getString(chatActivity.isHideFilteredMessages() ? R.string.ShowFilteredMessagesMenuText : R.string.HideFilteredMessagesMenuText));
                }
            }, 200L);
            return;
        }
        if (i == AyuConstants.OPTION_DELETED_HISTORY) {
            chatActivity.presentFragment(new AyuMessageHistory(chatActivity, chat, user, chatActivity.getCurrentEncryptedChat(), j2, themeDelegate));
            return;
        }
        if (i == AyuConstants.OPTION_GHOST_READ_EXCLUSION) {
            BottomSheet.Builder builder = new BottomSheet.Builder(chatActivity.getContext());
            builder.setTitle(LocaleController.getString(R.string.ExclusionTitle));
            builder.setItems(new CharSequence[]{LocaleController.getString(R.string.ExclusionUseDefault), LocaleController.getString(R.string.ExclusionDontRead), LocaleController.getString(R.string.ExclusionAlwaysRead)}, new DialogInterface.OnClickListener() { // from class: com.radolyn.ayugram.ui.ActionsPopupWrapper$$ExternalSyntheticLambda4
                @Override // android.content.DialogInterface.OnClickListener
                public final void onClick(DialogInterface dialogInterface, int i2) {
                    ActionsPopupWrapper.$r8$lambda$HT2ylmYRjPxQJF6rvE2NVsNdE6A(j, chatActivity, dialogInterface, i2);
                }
            });
            BottomSheet bottomSheetCreate = builder.create();
            bottomSheetCreate.show();
            bottomSheetCreate.getItemViews().get(AyuGhostExclusions.getReadSettingsType(Long.valueOf(j))).setChecked(true);
            return;
        }
        if (i == AyuConstants.OPTION_GHOST_TYPING_EXCLUSION) {
            BottomSheet.Builder builder2 = new BottomSheet.Builder(chatActivity.getContext());
            builder2.setTitle(LocaleController.getString(R.string.ExclusionTitle));
            builder2.setItems(new CharSequence[]{LocaleController.getString(R.string.ExclusionUseDefault), LocaleController.getString(R.string.ExclusionDontType), LocaleController.getString(R.string.ExclusionAlwaysType)}, new DialogInterface.OnClickListener() { // from class: com.radolyn.ayugram.ui.ActionsPopupWrapper$$ExternalSyntheticLambda5
                @Override // android.content.DialogInterface.OnClickListener
                public final void onClick(DialogInterface dialogInterface, int i2) {
                    ActionsPopupWrapper.$r8$lambda$eBpYHgioU5laZsjHBvAM0cg00dI(j, chatActivity, dialogInterface, i2);
                }
            });
            BottomSheet bottomSheetCreate2 = builder2.create();
            bottomSheetCreate2.show();
            bottomSheetCreate2.getItemViews().get(AyuGhostExclusions.getTypingSettingsType(Long.valueOf(j))).setChecked(true);
            return;
        }
        if (i == AyuConstants.OPTION_DEBUG_SEND_SCREENSHOT) {
            SendMessagesHelper.getInstance(chatActivity.getCurrentAccount()).sendScreenshotMessage2(user, ((Integer) AyuLocalDatabaseUtils.getMinAndMaxForDialog(chatActivity.getCurrentAccount(), j).second).intValue(), null);
        }
    }

    public static /* synthetic */ void $r8$lambda$VoWTsq0ePyI3ieB4UwnLMLJYxzA(ChatActivity chatActivity, long j, long j2) {
        chatActivity.getAyuMessagesController().clearDeletedFromDialog(j, chatActivity.getMergeDialogId(), j2 == 0 ? null : chatActivity.getUserConfig().clientUserId == j ? null : Long.valueOf(j2));
        chatActivity.getMessagesController().sortDialogs(null);
        chatActivity.getNotificationCenter().lambda$postNotificationNameOnUIThread$1(NotificationCenter.dialogsNeedReload, new Object[0]);
        chatActivity.finishFragment();
    }

    public static /* synthetic */ void $r8$lambda$HT2ylmYRjPxQJF6rvE2NVsNdE6A(long j, ChatActivity chatActivity, DialogInterface dialogInterface, int i) {
        if (i == AyuGhostExclusions.getReadSettingsType(Long.valueOf(j))) {
            return;
        }
        AyuGhostExclusions.saveReadException(Long.valueOf(j), Integer.valueOf(i));
        vibrate(chatActivity);
    }

    public static /* synthetic */ void $r8$lambda$eBpYHgioU5laZsjHBvAM0cg00dI(long j, ChatActivity chatActivity, DialogInterface dialogInterface, int i) {
        if (i == AyuGhostExclusions.getTypingSettingsType(Long.valueOf(j))) {
            return;
        }
        AyuGhostExclusions.saveTypingException(Long.valueOf(j), Integer.valueOf(i));
        vibrate(chatActivity);
    }

    private static void vibrate(BaseFragment baseFragment) {
        try {
            baseFragment.getFragmentView().performHapticFeedback(VibratorUtils.getType(3), 1);
        } catch (Exception unused) {
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    static class Item {
        int color;
        int id;
        int resId;
        String subtitle;
        String title;

        Item(int i, int i2, String str, String str2) {
            this.id = i;
            this.resId = i2;
            this.title = str;
            this.subtitle = str2;
        }

        Item(int i, int i2, String str, String str2, int i3) {
            this.id = i;
            this.resId = i2;
            this.title = str;
            this.subtitle = str2;
            this.color = i3;
        }
    }

    private View createGap() {
        FrameLayout frameLayout = new FrameLayout(this.fragment.getContext());
        frameLayout.setBackgroundColor(Theme.getColor(Theme.key_actionBarDefaultSubmenuSeparator, this.resourcesProvider));
        return frameLayout;
    }

    public void updateSwitchFilteringVisibility() {
        ActionBarMenuSubItem actionBarMenuSubItem;
        if (this.filteringSwitched || (actionBarMenuSubItem = this.switchFilteringItem) == null) {
            return;
        }
        actionBarMenuSubItem.setVisibility(0);
        this.filteringSwitched = true;
    }
}
