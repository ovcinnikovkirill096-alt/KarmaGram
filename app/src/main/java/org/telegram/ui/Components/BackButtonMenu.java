package org.telegram.ui.Components;

import android.app.Activity;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffColorFilter;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.TextView;
import androidx.core.content.ContextCompat;
import com.android.dx.io.Opcodes;
import com.exteragram.messenger.ExteraConfig;
import com.google.android.material.navigation.NavigationBarView;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.concurrent.atomic.AtomicReference;
import org.telegram.messenger.AndroidUtilities;
import org.telegram.messenger.ChatObject;
import org.telegram.messenger.ImageLocation;
import org.telegram.messenger.LocaleController;
import org.telegram.messenger.MessagesController;
import org.telegram.messenger.R;
import org.telegram.messenger.UserObject;
import org.telegram.tgnet.TLRPC;
import org.telegram.ui.ActionBar.ActionBarPopupWindow;
import org.telegram.ui.ActionBar.BaseFragment;
import org.telegram.ui.ActionBar.INavigationLayout;
import org.telegram.ui.ActionBar.Theme;
import org.telegram.ui.ChatActivity;
import org.telegram.ui.Components.Forum.ForumUtilities;
import org.telegram.ui.DialogsActivity;
import org.telegram.ui.ProfileActivity;
import org.telegram.ui.TopicsFragment;

public abstract class BackButtonMenu {

    public static class PulledDialog {
        Class activity;
        TLRPC.Chat chat;
        long dialogId;
        int filterId;
        int folderId;
        int stackIndex;
        TLRPC.TL_forumTopic topic;
        TLRPC.User user;
    }

    /* JADX WARN: Code duplicated, block: B:78:0x0253  */
    /* JADX WARN: Code duplicated, block: B:91:0x0274 A[SYNTHETIC] */
    public static ActionBarPopupWindow show(final BaseFragment baseFragment, View view, long j, long j2, Theme.ResourcesProvider resourcesProvider) {
        ArrayList stackedHistoryDialogs;
        ActionBarPopupWindow actionBarPopupWindow;
        boolean z;
        boolean z2;
        Drawable drawable;
        String string;
        BitmapDrawable bitmapDrawable;
        Drawable drawable2;
        BitmapDrawable bitmapDrawable2;
        BaseFragment baseFragment2 = baseFragment;
        ActionBarPopupWindow actionBarPopupWindow2 = null;
        if (baseFragment2 == null) {
            return null;
        }
        INavigationLayout parentLayout = baseFragment2.getParentLayout();
        Activity parentActivity = baseFragment2.getParentActivity();
        View fragmentView = baseFragment2.getFragmentView();
        if (parentLayout == null || parentActivity == null || fragmentView == null) {
            return null;
        }
        long j3 = 0;
        if (j2 != 0 && !ChatObject.isMonoForum(baseFragment2.getCurrentAccount(), j)) {
            stackedHistoryDialogs = getStackedHistoryForTopic(baseFragment2, j, j2);
        } else {
            stackedHistoryDialogs = getStackedHistoryDialogs(baseFragment2, j);
        }
        ArrayList arrayList = stackedHistoryDialogs;
        if (arrayList.size() <= 0) {
            return null;
        }
        ActionBarPopupWindow.ActionBarPopupWindowLayout actionBarPopupWindowLayout = new ActionBarPopupWindow.ActionBarPopupWindowLayout(parentActivity, resourcesProvider);
        android.graphics.Rect rect = new android.graphics.Rect();
        baseFragment2.getParentActivity().getResources().getDrawable(R.drawable.popup_fixed_alert).mutate().getPadding(rect);
        actionBarPopupWindowLayout.setBackgroundColor(Theme.getColor(Theme.key_actionBarDefaultSubmenuBackground, resourcesProvider));
        final AtomicReference atomicReference = new AtomicReference();
        int i = 0;
        boolean z3 = false;
        while (true) {
            actionBarPopupWindow = actionBarPopupWindow2;
            if (i >= arrayList.size()) {
                break;
            }
            PulledDialog pulledDialog = (PulledDialog) arrayList.get(i);
            long j4 = j3;
            TLRPC.Chat chat = pulledDialog.chat;
            TLRPC.User user = pulledDialog.user;
            final INavigationLayout iNavigationLayout = parentLayout;
            final TLRPC.TL_forumTopic tL_forumTopic = pulledDialog.topic;
            FrameLayout frameLayout = new FrameLayout(parentActivity);
            frameLayout.setMinimumWidth(AndroidUtilities.dp(200.0f));
            BackupImageView backupImageView = new BackupImageView(parentActivity);
            if (chat == null && user == null) {
                backupImageView.setRoundRadius(0);
            } else {
                backupImageView.setRoundRadius(ExteraConfig.getAvatarCorners(32.0f, false, chat != null && chat.forum));
            }
            frameLayout.addView(backupImageView, LayoutHelper.createFrameRelatively(32.0f, 32.0f, NavigationBarView.ITEM_GRAVITY_START_CENTER, 13.0f, 0.0f, 0.0f, 0.0f));
            TextView textView = new TextView(parentActivity);
            textView.setLines(1);
            textView.setTextSize(1, 16.0f);
            textView.setTextColor(Theme.getColor(Theme.key_actionBarDefaultSubmenuItem, resourcesProvider));
            textView.setEllipsize(TextUtils.TruncateAt.END);
            frameLayout.addView(textView, LayoutHelper.createFrameRelatively(-1.0f, -2.0f, NavigationBarView.ITEM_GRAVITY_START_CENTER, 59.0f, 0.0f, 12.0f, 0.0f));
            AvatarDrawable avatarDrawable = new AvatarDrawable();
            avatarDrawable.setScaleSize(0.8f);
            if (tL_forumTopic != null) {
                if (tL_forumTopic.id == 1) {
                    backupImageView.setImageDrawable(ForumUtilities.createGeneralTopicDrawable(fragmentView.getContext(), 1.0f, Theme.getColor(Theme.key_chat_inMenu, resourcesProvider), false));
                } else if (tL_forumTopic.icon_emoji_id != j4) {
                    backupImageView.setAnimatedEmojiDrawable(new AnimatedEmojiDrawable(10, baseFragment2.getCurrentAccount(), tL_forumTopic.icon_emoji_id));
                } else {
                    backupImageView.setImageDrawable(ForumUtilities.createTopicDrawable(tL_forumTopic, false));
                }
                textView.setText(tL_forumTopic.title);
            } else {
                if (chat != null) {
                    avatarDrawable.setInfo(baseFragment.getCurrentAccount(), chat);
                    TLRPC.ChatPhoto chatPhoto = chat.photo;
                    if (chatPhoto != null && (bitmapDrawable2 = chatPhoto.strippedBitmap) != null) {
                        drawable2 = avatarDrawable;
                        drawable2 = avatarDrawable;
                        drawable2 = bitmapDrawable2;
                    }
                    drawable2 = avatarDrawable;
                    drawable2 = avatarDrawable;
                    drawable2 = avatarDrawable;
                    backupImageView.setImage(ImageLocation.getForChat(chat, 1), "50_50", drawable2, chat);
                    textView.setText(chat.title);
                } else if (user != null) {
                    TLRPC.UserProfilePhoto userProfilePhoto = user.photo;
                    if (userProfilePhoto == null || (bitmapDrawable = userProfilePhoto.strippedBitmap) == null) {
                        drawable = bitmapDrawable;
                        drawable = avatarDrawable;
                    }
                    drawable = bitmapDrawable;
                    if (pulledDialog.activity == ChatActivity.class && UserObject.isUserSelf(user)) {
                        string = LocaleController.getString(R.string.SavedMessages);
                        avatarDrawable.setAvatarType(1);
                        backupImageView.setImageDrawable(avatarDrawable);
                    } else if (UserObject.isReplyUser(user)) {
                        string = LocaleController.getString(R.string.RepliesTitle);
                        avatarDrawable.setAvatarType(12);
                        backupImageView.setImageDrawable(avatarDrawable);
                    } else if (UserObject.isDeleted(user)) {
                        String string2 = LocaleController.getString(R.string.HiddenName);
                        avatarDrawable.setInfo(baseFragment.getCurrentAccount(), user);
                        backupImageView.setImage(ImageLocation.getForUser(user, 1), "50_50", avatarDrawable, user);
                        string = string2;
                    } else {
                        String userName = UserObject.getUserName(user);
                        avatarDrawable.setInfo(baseFragment.getCurrentAccount(), user);
                        backupImageView.setImage(ImageLocation.getForUser(user, 1), "50_50", drawable, user);
                        string = userName;
                    }
                    textView.setText(string);
                    z2 = false;
                    z = true;
                } else {
                    pulledDialog = pulledDialog;
                    backupImageView.setImageDrawable(ContextCompat.getDrawable(parentActivity, R.drawable.msg_viewchats).mutate());
                    backupImageView.setSize(AndroidUtilities.dp(24.0f), AndroidUtilities.dp(24.0f));
                    backupImageView.setColorFilter(new PorterDuffColorFilter(Theme.getColor(Theme.key_actionBarDefaultSubmenuItemIcon, resourcesProvider), PorterDuff.Mode.MULTIPLY));
                    textView.setText(LocaleController.getString(R.string.AllChats));
                    z = z3;
                    z2 = true;
                }
                frameLayout.setBackground(Theme.getSelectorDrawable(Theme.getColor(Theme.key_listSelector, resourcesProvider), false));
                atomicReference = atomicReference;
                final PulledDialog pulledDialog2 = pulledDialog;
                frameLayout.setOnClickListener(new View.OnClickListener() { // from class: org.telegram.ui.Components.BackButtonMenu$$ExternalSyntheticLambda0
                    @Override // android.view.View.OnClickListener
                    public final void onClick(View view2) {
                        BackButtonMenu.$r8$lambda$LjwJnyIy4aTYeceLn6PSUidUlns(atomicReference, pulledDialog2, iNavigationLayout, tL_forumTopic, baseFragment, view2);
                    }
                });
                actionBarPopupWindowLayout.addView((View) frameLayout, LayoutHelper.createLinear(-1, 48));
                if (z2) {
                    View frameLayout2 = new FrameLayout(parentActivity);
                    frameLayout2.setBackgroundColor(Theme.getColor(Theme.key_actionBarDefaultSubmenuSeparator, resourcesProvider));
                    frameLayout2.setTag(R.id.fit_width_tag, 1);
                    actionBarPopupWindowLayout.addView(frameLayout2, LayoutHelper.createLinear(-1, 8));
                }
                i++;
                baseFragment2 = baseFragment;
                z3 = z;
                actionBarPopupWindow2 = actionBarPopupWindow;
                j3 = j4;
                parentLayout = iNavigationLayout;
            }
            z2 = false;
            z = true;
            frameLayout.setBackground(Theme.getSelectorDrawable(Theme.getColor(Theme.key_listSelector, resourcesProvider), false));
            atomicReference = atomicReference;
            final PulledDialog pulledDialog3 = pulledDialog;
            frameLayout.setOnClickListener(new View.OnClickListener() { // from class: org.telegram.ui.Components.BackButtonMenu$$ExternalSyntheticLambda0
                @Override // android.view.View.OnClickListener
                public final void onClick(View view2) {
                    BackButtonMenu.$r8$lambda$LjwJnyIy4aTYeceLn6PSUidUlns(atomicReference, pulledDialog3, iNavigationLayout, tL_forumTopic, baseFragment, view2);
                }
            });
            actionBarPopupWindowLayout.addView((View) frameLayout, LayoutHelper.createLinear(-1, 48));
            if (z2) {
                View frameLayout3 = new FrameLayout(parentActivity);
                frameLayout3.setBackgroundColor(Theme.getColor(Theme.key_actionBarDefaultSubmenuSeparator, resourcesProvider));
                frameLayout3.setTag(R.id.fit_width_tag, 1);
                actionBarPopupWindowLayout.addView(frameLayout3, LayoutHelper.createLinear(-1, 8));
            }
            i++;
            baseFragment2 = baseFragment;
            z3 = z;
            actionBarPopupWindow2 = actionBarPopupWindow;
            j3 = j4;
            parentLayout = iNavigationLayout;
        }
        if (!z3) {
            return actionBarPopupWindow;
        }
        ActionBarPopupWindow actionBarPopupWindow3 = new ActionBarPopupWindow(actionBarPopupWindowLayout, -2, -2);
        atomicReference.set(actionBarPopupWindow3);
        actionBarPopupWindow3.setPauseNotifications(true);
        actionBarPopupWindow3.setDismissAnimationDuration(Opcodes.REM_INT_LIT8);
        actionBarPopupWindow3.setOutsideTouchable(true);
        actionBarPopupWindow3.setClippingEnabled(true);
        actionBarPopupWindow3.setAnimationStyle(R.style.PopupContextAnimation);
        actionBarPopupWindow3.setFocusable(true);
        actionBarPopupWindowLayout.measure(View.MeasureSpec.makeMeasureSpec(AndroidUtilities.dp(1000.0f), Integer.MIN_VALUE), View.MeasureSpec.makeMeasureSpec(AndroidUtilities.dp(1000.0f), Integer.MIN_VALUE));
        actionBarPopupWindow3.setInputMethodMode(2);
        actionBarPopupWindow3.setSoftInputMode(0);
        actionBarPopupWindow3.getContentView().setFocusableInTouchMode(true);
        actionBarPopupWindowLayout.setFitItems(true);
        int iDp = AndroidUtilities.dp(8.0f) - rect.left;
        if (AndroidUtilities.isTablet()) {
            int[] iArr = new int[2];
            fragmentView.getLocationInWindow(iArr);
            iDp += iArr[0];
        }
        actionBarPopupWindow3.showAtLocation(fragmentView, 51, iDp, (view.getBottom() - rect.top) - AndroidUtilities.dp(8.0f));
        return actionBarPopupWindow3;
    }

    /* JADX WARN: Code duplicated, block: B:19:0x0063  */
    public static /* synthetic */ void $r8$lambda$LjwJnyIy4aTYeceLn6PSUidUlns(AtomicReference atomicReference, PulledDialog pulledDialog, INavigationLayout iNavigationLayout, TLRPC.TL_forumTopic tL_forumTopic, BaseFragment baseFragment, View view) {
        Long lValueOf;
        int i;
        Long lValueOf2 = null;
        if (atomicReference.get() != null) {
            ((ActionBarPopupWindow) atomicReference.getAndSet(null)).dismiss();
        }
        if (pulledDialog.stackIndex >= 0) {
            if (iNavigationLayout == null || iNavigationLayout.getFragmentStack() == null || pulledDialog.stackIndex >= iNavigationLayout.getFragmentStack().size()) {
                lValueOf = null;
            } else {
                BaseFragment baseFragment2 = (BaseFragment) iNavigationLayout.getFragmentStack().get(pulledDialog.stackIndex);
                if (baseFragment2 instanceof ChatActivity) {
                    ChatActivity chatActivity = (ChatActivity) baseFragment2;
                    lValueOf2 = Long.valueOf(chatActivity.getDialogId());
                    lValueOf = Long.valueOf(chatActivity.getTopicId());
                } else if (baseFragment2 instanceof ProfileActivity) {
                    ProfileActivity profileActivity = (ProfileActivity) baseFragment2;
                    lValueOf2 = Long.valueOf(profileActivity.getDialogId());
                    lValueOf = Long.valueOf(profileActivity.getTopicId());
                } else {
                    lValueOf = null;
                }
            }
            if ((lValueOf2 != null && lValueOf2.longValue() != pulledDialog.dialogId) || (tL_forumTopic != null && lValueOf != null && tL_forumTopic.id != lValueOf.longValue())) {
                for (int size = iNavigationLayout.getFragmentStack().size() - 2; size > pulledDialog.stackIndex; size--) {
                    iNavigationLayout.removeFragmentFromStack(size);
                }
            } else if (iNavigationLayout != null && iNavigationLayout.getFragmentStack() != null) {
                ArrayList arrayList = new ArrayList(iNavigationLayout.getFragmentStack());
                int size2 = arrayList.size() - 2;
                while (true) {
                    i = pulledDialog.stackIndex;
                    if (size2 <= i) {
                        break;
                    }
                    ((BaseFragment) arrayList.get(size2)).removeSelfFromStack();
                    size2--;
                }
                if (i < iNavigationLayout.getFragmentStack().size()) {
                    iNavigationLayout.closeLastFragment(true);
                    return;
                }
            }
        }
        goToPulledDialog(baseFragment, pulledDialog);
    }

    private static ArrayList getStackedHistoryForTopic(BaseFragment baseFragment, long j, long j2) {
        INavigationLayout parentLayout;
        int i;
        ArrayList arrayList = new ArrayList();
        if (baseFragment == null || (parentLayout = baseFragment.getParentLayout()) == null) {
            return arrayList;
        }
        List pulledDialogs = parentLayout.getPulledDialogs();
        if (pulledDialogs != null) {
            i = -1;
            for (int i2 = 0; i2 < pulledDialogs.size(); i2++) {
                PulledDialog pulledDialog = (PulledDialog) pulledDialogs.get(i2);
                TLRPC.TL_forumTopic tL_forumTopic = pulledDialog.topic;
                if (tL_forumTopic != null && tL_forumTopic.id != j2) {
                    int i3 = pulledDialog.stackIndex;
                    if (i3 >= i) {
                        i = i3;
                    }
                    arrayList.add(pulledDialog);
                }
            }
        } else {
            i = -1;
        }
        if (parentLayout.getFragmentStack().size() > 1 && (parentLayout.getFragmentStack().get(parentLayout.getFragmentStack().size() - 2) instanceof TopicsFragment)) {
            PulledDialog pulledDialog2 = new PulledDialog();
            arrayList.add(pulledDialog2);
            pulledDialog2.stackIndex = i + 1;
            pulledDialog2.activity = DialogsActivity.class;
            PulledDialog pulledDialog3 = new PulledDialog();
            arrayList.add(pulledDialog3);
            pulledDialog3.stackIndex = -1;
            pulledDialog3.activity = TopicsFragment.class;
            pulledDialog3.chat = MessagesController.getInstance(baseFragment.getCurrentAccount()).getChat(Long.valueOf(-j));
        } else {
            PulledDialog pulledDialog4 = new PulledDialog();
            arrayList.add(pulledDialog4);
            pulledDialog4.stackIndex = -1;
            pulledDialog4.activity = TopicsFragment.class;
            pulledDialog4.chat = MessagesController.getInstance(baseFragment.getCurrentAccount()).getChat(Long.valueOf(-j));
        }
        Collections.sort(arrayList, new Comparator() { // from class: org.telegram.ui.Components.BackButtonMenu$$ExternalSyntheticLambda2
            @Override // java.util.Comparator
            public final int compare(Object obj, Object obj2) {
                return BackButtonMenu.$r8$lambda$zwJbf49aukFzohFh_mJM0GlNr8Q((BackButtonMenu.PulledDialog) obj, (BackButtonMenu.PulledDialog) obj2);
            }
        });
        return arrayList;
    }

    public static /* synthetic */ int $r8$lambda$zwJbf49aukFzohFh_mJM0GlNr8Q(PulledDialog pulledDialog, PulledDialog pulledDialog2) {
        return pulledDialog2.stackIndex - pulledDialog.stackIndex;
    }

    public static void goToPulledDialog(BaseFragment baseFragment, PulledDialog pulledDialog) {
        BaseFragment baseFragment2;
        if (pulledDialog == null) {
            return;
        }
        Class cls = pulledDialog.activity;
        if (cls == ChatActivity.class) {
            Bundle bundle = new Bundle();
            TLRPC.Chat chat = pulledDialog.chat;
            if (chat != null) {
                bundle.putLong("chat_id", chat.id);
            } else {
                TLRPC.User user = pulledDialog.user;
                if (user != null) {
                    bundle.putLong("user_id", user.id);
                }
            }
            bundle.putInt("dialog_folder_id", pulledDialog.folderId);
            bundle.putInt("dialog_filter_id", pulledDialog.filterId);
            TLRPC.TL_forumTopic tL_forumTopic = pulledDialog.topic;
            if (tL_forumTopic != null) {
                baseFragment2 = baseFragment;
                baseFragment2.presentFragment(ForumUtilities.getChatActivityForTopic(baseFragment2, pulledDialog.chat.id, tL_forumTopic, 0, bundle), true);
            } else {
                baseFragment2 = baseFragment;
                baseFragment2.presentFragment(new ChatActivity(bundle), true);
            }
        } else {
            baseFragment2 = baseFragment;
            if (cls == ProfileActivity.class) {
                Bundle bundle2 = new Bundle();
                bundle2.putLong("dialog_id", pulledDialog.dialogId);
                baseFragment2.presentFragment(new ProfileActivity(bundle2), true);
            }
        }
        if (pulledDialog.activity == TopicsFragment.class) {
            Bundle bundle3 = new Bundle();
            bundle3.putLong("chat_id", pulledDialog.chat.id);
            baseFragment2.presentFragment(new TopicsFragment(bundle3), true);
        }
        if (pulledDialog.activity == DialogsActivity.class) {
            baseFragment2.presentFragment(new DialogsActivity(null), true);
        }
    }

    public static ArrayList getStackedHistoryDialogs(BaseFragment baseFragment, long j) {
        INavigationLayout parentLayout;
        TLRPC.Chat currentChat;
        TLRPC.User currentUser;
        long dialogId;
        Class cls;
        int dialogFilterId;
        int dialogFolderId;
        ArrayList arrayList = new ArrayList();
        if (baseFragment == null || (parentLayout = baseFragment.getParentLayout()) == null) {
            return arrayList;
        }
        List fragmentStack = parentLayout.getFragmentStack();
        List pulledDialogs = parentLayout.getPulledDialogs();
        if (fragmentStack != null) {
            int size = fragmentStack.size();
            for (int i = 0; i < size; i++) {
                BaseFragment baseFragment2 = (BaseFragment) fragmentStack.get(i);
                if (baseFragment2 instanceof ChatActivity) {
                    ChatActivity chatActivity = (ChatActivity) baseFragment2;
                    if (chatActivity.getChatMode() == 0 && !chatActivity.isReport()) {
                        currentChat = chatActivity.getCurrentChat();
                        currentUser = chatActivity.getCurrentUser();
                        dialogId = chatActivity.getDialogId();
                        dialogFolderId = chatActivity.getDialogFolderId();
                        dialogFilterId = chatActivity.getDialogFilterId();
                        cls = ChatActivity.class;
                        if (dialogId == j && (j != 0 || !UserObject.isUserSelf(currentUser))) {
                            int i2 = 0;
                            while (true) {
                                if (i2 >= arrayList.size()) {
                                    PulledDialog pulledDialog = new PulledDialog();
                                    pulledDialog.activity = cls;
                                    pulledDialog.stackIndex = i;
                                    pulledDialog.chat = currentChat;
                                    pulledDialog.user = currentUser;
                                    pulledDialog.dialogId = dialogId;
                                    pulledDialog.folderId = dialogFolderId;
                                    pulledDialog.filterId = dialogFilterId;
                                    if (currentChat != null || currentUser != null) {
                                        arrayList.add(pulledDialog);
                                        break;
                                    }
                                    break;
                                }
                                if (((PulledDialog) arrayList.get(i2)).dialogId == dialogId) {
                                    break;
                                }
                                i2++;
                            }
                        }
                    }
                } else if (baseFragment2 instanceof ProfileActivity) {
                    ProfileActivity profileActivity = (ProfileActivity) baseFragment2;
                    currentChat = profileActivity.getCurrentChat();
                    try {
                        currentUser = profileActivity.getUserInfo().user;
                    } catch (Exception unused) {
                        currentUser = null;
                    }
                    dialogId = profileActivity.getDialogId();
                    cls = ProfileActivity.class;
                    dialogFilterId = 0;
                    dialogFolderId = 0;
                    if (dialogId == j) {
                    }
                }
            }
        }
        if (pulledDialogs != null) {
            for (int size2 = pulledDialogs.size() - 1; size2 >= 0; size2--) {
                PulledDialog pulledDialog2 = (PulledDialog) pulledDialogs.get(size2);
                if (pulledDialog2.dialogId != j) {
                    int i3 = 0;
                    while (true) {
                        if (i3 >= arrayList.size()) {
                            arrayList.add(pulledDialog2);
                            break;
                        }
                        if (((PulledDialog) arrayList.get(i3)).dialogId == pulledDialog2.dialogId) {
                            break;
                        }
                        i3++;
                    }
                }
            }
        }
        Collections.sort(arrayList, new Comparator() { // from class: org.telegram.ui.Components.BackButtonMenu$$ExternalSyntheticLambda1
            @Override // java.util.Comparator
            public final int compare(Object obj, Object obj2) {
                return BackButtonMenu.m7652$r8$lambda$sPUzo9otFwawnC1lMMb0CzYuf8((BackButtonMenu.PulledDialog) obj, (BackButtonMenu.PulledDialog) obj2);
            }
        });
        return arrayList;
    }

    /* JADX INFO: renamed from: $r8$lambda$sPUzo9otFwawnC1-lMMb0CzYuf8, reason: not valid java name */
    public static /* synthetic */ int m7652$r8$lambda$sPUzo9otFwawnC1lMMb0CzYuf8(PulledDialog pulledDialog, PulledDialog pulledDialog2) {
        return pulledDialog2.stackIndex - pulledDialog.stackIndex;
    }

    public static void addToPulledDialogs(BaseFragment baseFragment, int i, TLRPC.Chat chat, TLRPC.User user, TLRPC.TL_forumTopic tL_forumTopic, long j, int i2, int i3) {
        INavigationLayout parentLayout;
        TLRPC.TL_forumTopic tL_forumTopic2;
        if ((chat == null && user == null) || baseFragment == null || (parentLayout = baseFragment.getParentLayout()) == null) {
            return;
        }
        if (parentLayout.getPulledDialogs() == null) {
            parentLayout.setPulledDialogs(new ArrayList());
        }
        for (PulledDialog pulledDialog : parentLayout.getPulledDialogs()) {
            if (tL_forumTopic == null && pulledDialog.dialogId == j) {
                return;
            }
            if (tL_forumTopic != null && (tL_forumTopic2 = pulledDialog.topic) != null && tL_forumTopic2.id == tL_forumTopic.id) {
                return;
            }
        }
        PulledDialog pulledDialog2 = new PulledDialog();
        pulledDialog2.activity = ChatActivity.class;
        pulledDialog2.stackIndex = i;
        pulledDialog2.dialogId = j;
        pulledDialog2.filterId = i3;
        pulledDialog2.folderId = i2;
        pulledDialog2.chat = chat;
        pulledDialog2.user = user;
        pulledDialog2.topic = tL_forumTopic;
        parentLayout.getPulledDialogs().add(pulledDialog2);
    }

    public static void clearPulledDialogs(BaseFragment baseFragment, int i) {
        INavigationLayout parentLayout;
        if (baseFragment == null || (parentLayout = baseFragment.getParentLayout()) == null || parentLayout.getPulledDialogs() == null) {
            return;
        }
        int i2 = 0;
        while (i2 < parentLayout.getPulledDialogs().size()) {
            if (((PulledDialog) parentLayout.getPulledDialogs().get(i2)).stackIndex > i) {
                parentLayout.getPulledDialogs().remove(i2);
                i2--;
            }
            i2++;
        }
    }
}
