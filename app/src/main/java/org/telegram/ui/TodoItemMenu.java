package org.telegram.ui;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.ValueAnimator;
import android.app.Dialog;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapShader;
import android.graphics.Canvas;
import android.graphics.ColorMatrix;
import android.graphics.ColorMatrixColorFilter;
import android.graphics.Insets;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.RectF;
import android.graphics.Shader;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowInsets;
import android.view.WindowManager;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;
import androidx.core.view.WindowInsetsCompat;
import j$.util.Objects;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import org.telegram.messenger.AndroidUtilities;
import org.telegram.messenger.ChatObject;
import org.telegram.messenger.LocaleController;
import org.telegram.messenger.MessageObject;
import org.telegram.messenger.R;
import org.telegram.messenger.SharedConfig;
import org.telegram.messenger.UserObject;
import org.telegram.messenger.Utilities;
import org.telegram.tgnet.ConnectionsManager;
import org.telegram.tgnet.TLObject;
import org.telegram.tgnet.TLRPC;
import org.telegram.ui.ActionBar.ActionBarMenuSubItem;
import org.telegram.ui.ActionBar.ActionBarPopupWindow;
import org.telegram.ui.ActionBar.Theme;
import org.telegram.ui.Cells.BaseCell;
import org.telegram.ui.Cells.ChatActionCell;
import org.telegram.ui.Cells.ChatMessageCell;
import org.telegram.ui.Components.Bulletin;
import org.telegram.ui.Components.BulletinFactory;
import org.telegram.ui.Components.CubicBezierInterpolator;
import org.telegram.ui.Components.ItemOptions;
import org.telegram.ui.Components.LayoutHelper;
import org.telegram.ui.Components.MessagePreviewView;
import org.telegram.ui.Components.MessagePrivateSeenView;
import org.telegram.ui.Components.Reactions.ReactionsLayoutInBubble;
import org.telegram.ui.Components.ReactionsContainerLayout;
import org.telegram.ui.Components.RecyclerListView;
import org.telegram.ui.Components.ViewPagerFixed;

public class TodoItemMenu extends Dialog {
    private Bitmap blurBitmap;
    private Paint blurBitmapPaint;
    private BitmapShader blurBitmapShader;
    private Matrix blurMatrix;
    private ChatMessageCell cell;
    private float clipBottom;
    private float clipTop;
    private FrameLayout containerView;
    public final Context context;
    private Runnable dismissListener;
    private boolean dismissing;
    private boolean dismissingWithAlpha;
    private float dtx1;
    private float dtx2;
    private float dty1;
    private float dty2;
    private boolean hasDestTranslation;
    private boolean hasTranslation;
    private float heightdiff;
    private TextView hintTextView;
    private final Rect insets;
    private boolean isOut;
    private FrameLayout menuContainer;
    private MessageObject messageObject;
    private View messageOptionsView;
    private float messageOptionsViewMaxWidth;
    private ChatMessageCell myCell;
    private ChatMessageCell myTaskCell;
    private boolean open;
    private ValueAnimator open2Animator;
    private ValueAnimator openAnimator;
    private float openProgress;
    private float openProgress2;
    private ReactionsContainerLayout reactionsView;
    public final Theme.ResourcesProvider resourcesProvider;
    private boolean setCellInvisible;
    private boolean setTaskInvisible;
    private MessagePreviewView.TabsView tabsView;
    private int taskId;
    private View taskOptionsView;
    private float taskOptionsViewMaxWidth;
    private float tx;
    private float ty;
    private ViewPagerFixed viewPager;
    private FrameLayout windowView;

    public TodoItemMenu(Context context, Theme.ResourcesProvider resourcesProvider) {
        super(context, R.style.TransparentDialog);
        this.insets = new Rect();
        this.clipTop = 0.0f;
        this.clipBottom = 0.0f;
        this.taskOptionsViewMaxWidth = -1.0f;
        this.messageOptionsViewMaxWidth = -1.0f;
        this.dismissing = false;
        this.context = context;
        this.resourcesProvider = resourcesProvider;
        FrameLayout frameLayout = new FrameLayout(context) { // from class: org.telegram.ui.TodoItemMenu.1
            @Override // android.view.ViewGroup, android.view.View
            protected void dispatchDraw(Canvas canvas) {
                Canvas canvas2;
                if (TodoItemMenu.this.openProgress <= 0.0f || TodoItemMenu.this.blurBitmapPaint == null) {
                    canvas2 = canvas;
                } else {
                    TodoItemMenu.this.blurMatrix.reset();
                    float width = getWidth() / TodoItemMenu.this.blurBitmap.getWidth();
                    TodoItemMenu.this.blurMatrix.postScale(width, width);
                    TodoItemMenu.this.blurBitmapShader.setLocalMatrix(TodoItemMenu.this.blurMatrix);
                    TodoItemMenu.this.blurBitmapPaint.setAlpha((int) (TodoItemMenu.this.openProgress * 255.0f));
                    canvas2 = canvas;
                    canvas2.drawRect(0.0f, 0.0f, getWidth(), getHeight(), TodoItemMenu.this.blurBitmapPaint);
                }
                if (TodoItemMenu.this.setCellInvisible && TodoItemMenu.this.cell != null) {
                    TodoItemMenu.this.cell.setVisibility(4);
                    TodoItemMenu.this.setCellInvisible = false;
                }
                if (TodoItemMenu.this.setTaskInvisible && TodoItemMenu.this.cell != null) {
                    TodoItemMenu.this.cell.doNotDrawTaskId = TodoItemMenu.this.taskId;
                    TodoItemMenu.this.cell.invalidate();
                    TodoItemMenu.this.setTaskInvisible = false;
                }
                super.dispatchDraw(canvas2);
            }

            @Override // android.view.ViewGroup, android.view.View
            public boolean dispatchKeyEventPreIme(KeyEvent keyEvent) {
                if (keyEvent != null && keyEvent.getKeyCode() == 4 && keyEvent.getAction() == 1) {
                    TodoItemMenu.this.dismiss();
                    return true;
                }
                return super.dispatchKeyEventPreIme(keyEvent);
            }

            @Override // android.widget.FrameLayout, android.view.ViewGroup, android.view.View
            protected void onLayout(boolean z, int i, int i2, int i3, int i4) {
                super.onLayout(z, i, i2, i3, i4);
                TodoItemMenu.this.setupTranslation();
            }
        };
        this.windowView = frameLayout;
        frameLayout.setOnClickListener(new View.OnClickListener() { // from class: org.telegram.ui.TodoItemMenu$$ExternalSyntheticLambda16
            @Override // android.view.View.OnClickListener
            public final void onClick(View view) {
                this.f$0.lambda$new$0(view);
            }
        });
        FrameLayout frameLayout2 = new FrameLayout(context) { // from class: org.telegram.ui.TodoItemMenu.2
            @Override // android.view.ViewGroup
            protected boolean drawChild(Canvas canvas, View view, long j) {
                if (view == TodoItemMenu.this.myCell || view == TodoItemMenu.this.myTaskCell) {
                    canvas.save();
                    canvas.clipRect(0.0f, AndroidUtilities.lerp(TodoItemMenu.this.clipTop, 0.0f, TodoItemMenu.this.openProgress), getWidth(), AndroidUtilities.lerp(TodoItemMenu.this.clipBottom, getHeight(), TodoItemMenu.this.openProgress));
                    boolean zDrawChild = super.drawChild(canvas, view, j);
                    canvas.restore();
                    return zDrawChild;
                }
                return super.drawChild(canvas, view, j);
            }
        };
        this.containerView = frameLayout2;
        frameLayout2.setClipToPadding(false);
        this.windowView.addView(this.containerView, LayoutHelper.createFrame(-1, -1, 119));
        ViewPagerFixed viewPagerFixed = new ViewPagerFixed(context) { // from class: org.telegram.ui.TodoItemMenu.3
            @Override // org.telegram.ui.Components.ViewPagerFixed
            public void onTabAnimationUpdate(boolean z) {
                TodoItemMenu.this.updateTranslation();
            }
        };
        this.viewPager = viewPagerFixed;
        viewPagerFixed.setAdapter(new AnonymousClass4(context));
        this.containerView.addView(this.viewPager, LayoutHelper.createFrame(-1, -1, 119));
        FrameLayout frameLayout3 = new FrameLayout(context) { // from class: org.telegram.ui.TodoItemMenu.5
            @Override // android.widget.FrameLayout, android.view.View
            protected void onMeasure(int i, int i2) {
                int size = View.MeasureSpec.getSize(i);
                int size2 = View.MeasureSpec.getSize(i2);
                TodoItemMenu.this.updateTranslation();
                for (int i3 = 0; i3 < getChildCount(); i3++) {
                    View childAt = getChildAt(i3);
                    if (childAt == TodoItemMenu.this.messageOptionsView && TodoItemMenu.this.messageOptionsViewMaxWidth > 0.0f) {
                        TodoItemMenu.this.messageOptionsView.measure(View.MeasureSpec.makeMeasureSpec(Math.min(size, (int) TodoItemMenu.this.messageOptionsViewMaxWidth), Integer.MIN_VALUE), View.MeasureSpec.makeMeasureSpec(size2, Integer.MIN_VALUE));
                    } else if (childAt == TodoItemMenu.this.taskOptionsView && TodoItemMenu.this.taskOptionsViewMaxWidth > 0.0f) {
                        TodoItemMenu.this.taskOptionsView.measure(View.MeasureSpec.makeMeasureSpec(Math.min(size, (int) TodoItemMenu.this.taskOptionsViewMaxWidth), Integer.MIN_VALUE), View.MeasureSpec.makeMeasureSpec(size2, Integer.MIN_VALUE));
                    } else if (childAt == TodoItemMenu.this.reactionsView) {
                        childAt.measure(View.MeasureSpec.makeMeasureSpec(TodoItemMenu.this.reactionsView.getTotalWidth(), TLObject.FLAG_30), View.MeasureSpec.makeMeasureSpec(size2, Integer.MIN_VALUE));
                    } else {
                        childAt.measure(View.MeasureSpec.makeMeasureSpec(size, Integer.MIN_VALUE), View.MeasureSpec.makeMeasureSpec(size2, Integer.MIN_VALUE));
                    }
                }
                setMeasuredDimension(size, size2);
            }
        };
        this.menuContainer = frameLayout3;
        this.containerView.addView(frameLayout3, LayoutHelper.createFrame(-1, -1, 119));
        MessagePreviewView.TabsView tabsView = new MessagePreviewView.TabsView(context, resourcesProvider);
        this.tabsView = tabsView;
        tabsView.addTab(0, LocaleController.getString(R.string.TodoMenuTabTask));
        this.tabsView.addTab(1, LocaleController.getString(R.string.TodoMenuTabList));
        this.containerView.addView(this.tabsView, LayoutHelper.createFrame(-1, 66, 80));
        MessagePreviewView.TabsView tabsView2 = this.tabsView;
        final ViewPagerFixed viewPagerFixed2 = this.viewPager;
        Objects.requireNonNull(viewPagerFixed2);
        tabsView2.setOnTabClick(new Utilities.Callback() { // from class: org.telegram.ui.TodoItemMenu$$ExternalSyntheticLambda17
            @Override // org.telegram.messenger.Utilities.Callback
            public final void run(Object obj) {
                viewPagerFixed2.scrollToPosition(((Integer) obj).intValue());
            }
        });
        TextView textView = new TextView(context);
        this.hintTextView = textView;
        textView.setTextSize(1, 13.0f);
        this.hintTextView.setTextColor(this.tabsView.getColor());
        this.hintTextView.setText(LocaleController.getString(R.string.TodoMenuHint));
        this.hintTextView.setGravity(17);
        this.containerView.addView(this.hintTextView, LayoutHelper.createFrame(-1, -2.0f, 80, 0.0f, 0.0f, 0.0f, 66.0f));
        this.windowView.setFitsSystemWindows(true);
        this.windowView.setOnApplyWindowInsetsListener(new View.OnApplyWindowInsetsListener() { // from class: org.telegram.ui.TodoItemMenu.6
            @Override // android.view.View.OnApplyWindowInsetsListener
            public WindowInsets onApplyWindowInsets(View view, WindowInsets windowInsets) {
                int i = Build.VERSION.SDK_INT;
                if (i >= 30) {
                    Insets insets = windowInsets.getInsets(WindowInsetsCompat.Type.displayCutout() | WindowInsetsCompat.Type.systemBars());
                    TodoItemMenu.this.insets.set(insets.left, insets.top, insets.right, insets.bottom);
                } else {
                    TodoItemMenu.this.insets.set(windowInsets.getSystemWindowInsetLeft(), windowInsets.getSystemWindowInsetTop(), windowInsets.getSystemWindowInsetRight(), windowInsets.getSystemWindowInsetBottom());
                }
                TodoItemMenu.this.containerView.setPadding(TodoItemMenu.this.insets.left, TodoItemMenu.this.insets.top, TodoItemMenu.this.insets.right, TodoItemMenu.this.insets.bottom);
                TodoItemMenu.this.windowView.requestLayout();
                if (i >= 30) {
                    return WindowInsets.CONSUMED;
                }
                return windowInsets.consumeSystemWindowInsets();
            }
        });
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$new$0(View view) {
        dismiss();
    }

    /* JADX INFO: renamed from: org.telegram.ui.TodoItemMenu$4, reason: invalid class name */
    class AnonymousClass4 extends ViewPagerFixed.Adapter {
        final /* synthetic */ Context val$context;

        @Override // org.telegram.ui.Components.ViewPagerFixed.Adapter
        public void bindView(View view, int i, int i2) {
        }

        @Override // org.telegram.ui.Components.ViewPagerFixed.Adapter
        public int getItemCount() {
            return 2;
        }

        AnonymousClass4(Context context) {
            this.val$context = context;
        }

        @Override // org.telegram.ui.Components.ViewPagerFixed.Adapter
        public View createView(int i) {
            FrameLayout frameLayout = new FrameLayout(this.val$context);
            frameLayout.setOnClickListener(new View.OnClickListener() { // from class: org.telegram.ui.TodoItemMenu$4$$ExternalSyntheticLambda0
                @Override // android.view.View.OnClickListener
                public final void onClick(View view) {
                    this.f$0.lambda$createView$0(view);
                }
            });
            return frameLayout;
        }

        /* JADX INFO: Access modifiers changed from: private */
        public /* synthetic */ void lambda$createView$0(View view) {
            TodoItemMenu.this.dismiss(true);
        }
    }

    @Override // android.app.Dialog
    protected void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        Window window = getWindow();
        window.setWindowAnimations(R.style.DialogNoAnimation);
        setContentView(this.windowView, new ViewGroup.LayoutParams(-1, -1));
        WindowManager.LayoutParams attributes = window.getAttributes();
        attributes.width = -1;
        attributes.height = -1;
        attributes.gravity = 119;
        attributes.dimAmount = 0.0f;
        int i = attributes.flags;
        attributes.softInputMode = 48;
        int i2 = i & (-131075);
        attributes.flags = i2;
        int i3 = Build.VERSION.SDK_INT;
        attributes.flags = i2 | (-2013198976);
        if (i3 >= 28) {
            attributes.layoutInDisplayCutoutMode = 1;
        }
        window.setAttributes(attributes);
        this.windowView.setSystemUiVisibility(1284);
        AndroidUtilities.setLightNavigationBar(this.windowView, !Theme.isCurrentThemeDark());
    }

    /*  JADX ERROR: JadxRuntimeException in pass: ModVisitor
        jadx.core.utils.exceptions.JadxRuntimeException: Code variable not set in r7v0 int
        	at jadx.core.dex.instructions.args.SSAVar.getCodeVar(SSAVar.java:236)
        	at jadx.core.dex.visitors.ModVisitor.anonymousCallArgMod(ModVisitor.java:535)
        	at java.base/java.util.ArrayList.forEach(ArrayList.java:1596)
        	at jadx.core.dex.visitors.ModVisitor.processAnonymousConstructor(ModVisitor.java:528)
        	at jadx.core.dex.visitors.ModVisitor.replaceStep(ModVisitor.java:111)
        	at jadx.core.dex.visitors.ModVisitor.visit(ModVisitor.java:96)
        */
    public void setCell(final org.telegram.ui.ChatActivity r23, org.telegram.ui.Cells.ChatMessageCell r24, final int r25) {
        /*
            Method dump skipped, instruction units count: 728
            To view this dump add '--comments-level debug' option
        */
        throw new UnsupportedOperationException("Method not decompiled: org.telegram.ui.TodoItemMenu.setCell(org.telegram.ui.ChatActivity, org.telegram.ui.Cells.ChatMessageCell, int):void");
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$setCell$1(ChatActivity chatActivity, int i) {
        if (chatActivity.isInScheduleMode()) {
            Toast.makeText(getContext(), LocaleController.getString(R.string.MessageScheduledTodo), 1).show();
        } else {
            ChatMessageCell chatMessageCell = this.myTaskCell;
            chatMessageCell.toggleTodoCheck(chatMessageCell.getTodoIndex(i), false);
        }
        dismiss(true);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$setCell$2(ChatActivity chatActivity, int i) {
        if (chatActivity.isInScheduleMode()) {
            Toast.makeText(getContext(), LocaleController.getString(R.string.MessageScheduledTodo), 1).show();
        } else {
            ChatMessageCell chatMessageCell = this.myTaskCell;
            chatMessageCell.toggleTodoCheck(chatMessageCell.getTodoIndex(i), false);
        }
        dismiss(true);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$setCell$3(ChatActivity chatActivity, TLRPC.TodoItem todoItem) {
        MessageObject messageObject = this.messageObject;
        chatActivity.showFieldPanelForReplyQuote(messageObject, ChatActivity.ReplyQuote.from(messageObject, todoItem.id));
        dismiss(false);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$setCell$4(String str) {
        AndroidUtilities.addToClipboard(str);
        dismiss(true);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$setCell$5(TLRPC.TodoItem todoItem) {
        AndroidUtilities.addToClipboard(MessageObject.formatTextWithEntities(todoItem.title, false));
        dismiss(true);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$setCell$7(final ChatActivity chatActivity, int i) {
        PollCreateActivity pollCreateActivity = new PollCreateActivity(chatActivity, true, Boolean.FALSE);
        pollCreateActivity.setEditing(MessageObject.getMedia(this.messageObject), false, i);
        pollCreateActivity.setDelegate(new PollCreateActivity.PollCreateActivityDelegate() { // from class: org.telegram.ui.TodoItemMenu$$ExternalSyntheticLambda19
            @Override // org.telegram.ui.PollCreateActivity.PollCreateActivityDelegate
            public final void sendPoll(TLRPC.MessageMedia messageMedia, HashMap map, boolean z, int i2) {
                this.f$0.lambda$setCell$6(chatActivity, messageMedia, map, z, i2);
            }
        });
        chatActivity.presentFragment(pollCreateActivity);
        dismiss(false);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$setCell$6(ChatActivity chatActivity, TLRPC.MessageMedia messageMedia, HashMap map, boolean z, int i) {
        if (messageMedia instanceof TLRPC.TL_messageMediaToDo) {
            TLRPC.MessageMedia messageMedia2 = this.messageObject.messageOwner.media;
            if (messageMedia2 instanceof TLRPC.TL_messageMediaToDo) {
                ((TLRPC.TL_messageMediaToDo) messageMedia).completions = ((TLRPC.TL_messageMediaToDo) messageMedia2).completions;
            }
        }
        this.messageObject.messageOwner.media = messageMedia;
        chatActivity.getSendMessagesHelper().editMessage(this.messageObject, null, null, null, null, null, null, false, false, null);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$setCell$8(TLRPC.TL_messageMediaToDo tL_messageMediaToDo, int i, ChatActivity chatActivity) {
        int i2 = 0;
        while (i2 < tL_messageMediaToDo.todo.list.size()) {
            if (((TLRPC.TodoItem) tL_messageMediaToDo.todo.list.get(i2)).id == i) {
                tL_messageMediaToDo.todo.list.remove(i2);
                i2--;
            }
            i2++;
        }
        int i3 = 0;
        while (i3 < tL_messageMediaToDo.completions.size()) {
            if (((TLRPC.TodoCompletion) tL_messageMediaToDo.completions.get(i3)).id == i) {
                tL_messageMediaToDo.completions.remove(i3);
                if (tL_messageMediaToDo.completions.isEmpty()) {
                    tL_messageMediaToDo.flags &= -2;
                }
                i3--;
            }
            i3++;
        }
        this.messageObject.messageOwner.media = tL_messageMediaToDo;
        chatActivity.getSendMessagesHelper().editMessage(this.messageObject, null, null, null, null, null, null, false, false, null);
        chatActivity.updateVisibleRows();
        dismiss(false);
    }

    /* JADX WARN: Code duplicated, block: B:163:0x021f  */
    public void setupMessageOptions(final ChatActivity chatActivity, ArrayList arrayList, ArrayList arrayList2, ArrayList arrayList3, final Utilities.Callback callback) {
        TLRPC.ChatFull chatFull;
        boolean z;
        ItemOptions itemOptions;
        final TodoItemMenu todoItemMenu;
        TLRPC.User user;
        TLRPC.UserFull userFull;
        TLRPC.ChatFull chatFull2;
        TLRPC.ChatFull chatFull3;
        final MessageObject messageObject = this.messageObject;
        List<TLRPC.TL_availableReaction> enabledReactionsList = chatActivity.getMediaDataController().getEnabledReactionsList();
        boolean z2 = (chatActivity.isSecretChat() || chatActivity.isInScheduleMode() || chatActivity.currentUser != null || !messageObject.hasReactions() || (ChatObject.isChannel(chatActivity.currentChat) && !chatActivity.currentChat.megagroup) || ChatObject.isMonoForum(chatActivity.currentChat) || enabledReactionsList.isEmpty() || !messageObject.messageOwner.reactions.can_see_list || messageObject.isSecretMedia()) ? false : true;
        boolean z3 = !messageObject.isForwardedChannelPost() ? messageObject.isSecretMedia() || chatActivity.getChatMode() == 5 || chatActivity.isSecretChat() || chatActivity.isInScheduleMode() || !messageObject.isReactionsAvailable() || ((((chatFull = chatActivity.chatInfo) == null || ((chatFull.available_reactions instanceof TLRPC.TL_chatReactionsNone) && !chatFull.paid_reactions_available)) && ((chatFull != null || ChatObject.isChannel(chatActivity.currentChat)) && chatActivity.currentUser == null && !ChatObject.isMonoForum(chatActivity.currentChat))) || enabledReactionsList.isEmpty()) : (chatFull3 = chatActivity.getMessagesController().getChatFull(-messageObject.getFromChatId())) != null && (chatActivity.isSecretChat() || chatActivity.getChatMode() == 5 || chatActivity.isInScheduleMode() || !messageObject.isReactionsAvailable() || (((chatFull3.available_reactions instanceof TLRPC.TL_chatReactionsNone) && !chatFull3.paid_reactions_available) || enabledReactionsList.isEmpty()));
        boolean z4 = (z2 || chatActivity.isInScheduleMode() || chatActivity.currentChat == null || !messageObject.isOutOwner() || !messageObject.isSent() || messageObject.isEditing() || messageObject.isSending() || messageObject.isSendError() || messageObject.isContentUnread() || messageObject.isUnread() || ConnectionsManager.getInstance(chatActivity.getCurrentAccount()).getCurrentTime() - messageObject.messageOwner.date >= chatActivity.getMessagesController().chatReadMarkExpirePeriod || (!ChatObject.isMegagroup(chatActivity.currentChat) && ChatObject.isChannel(chatActivity.currentChat)) || (chatFull2 = chatActivity.chatInfo) == null || chatFull2.participants_count > chatActivity.getMessagesController().chatReadMarkSizeThreshold || (messageObject.messageOwner.action instanceof TLRPC.TL_messageActionChatJoinedByRequest) || chatActivity.getChatMode() == 3 || !messageObject.canSetReaction() || ChatObject.isMonoForum(chatActivity.currentChat)) ? false : true;
        if (chatActivity.currentChat != null && !messageObject.isOut() && ChatObject.isMonoForum(chatActivity.currentChat) && ChatObject.canManageMonoForum(chatActivity.getCurrentAccount(), chatActivity.currentChat)) {
            int i = ((-chatActivity.currentChat.linked_monoforum_id) > messageObject.getFromChatId() ? 1 : ((-chatActivity.currentChat.linked_monoforum_id) == messageObject.getFromChatId() ? 0 : -1));
        }
        if (z2 || chatActivity.currentChat != null || chatActivity.currentEncryptedChat != null || (user = chatActivity.currentUser) == null || UserObject.isUserSelf(user) || UserObject.isReplyUser(chatActivity.currentUser) || UserObject.isAnonymous(chatActivity.currentUser)) {
            z = false;
        } else {
            TLRPC.User user2 = chatActivity.currentUser;
            if (user2.bot || UserObject.isService(user2.id) || (((userFull = chatActivity.userInfo) != null && userFull.read_dates_private) || chatActivity.isInScheduleMode() || !messageObject.isOutOwner() || !messageObject.isSent() || messageObject.isEditing() || messageObject.isSending() || messageObject.isSendError() || messageObject.isContentUnread() || messageObject.isUnread() || chatActivity.getConnectionsManager().getCurrentTime() - messageObject.messageOwner.date >= chatActivity.getMessagesController().pmReadDateExpirePeriod || (messageObject.messageOwner.action instanceof TLRPC.TL_messageActionChatJoinedByRequest))) {
                z = false;
            } else {
                z = true;
            }
        }
        TLRPC.User user3 = chatActivity.currentUser;
        boolean z5 = (user3 == null || !(UserObject.isReplyUser(user3) || UserObject.isAnonymous(chatActivity.currentUser))) && !chatActivity.isInScheduleMode() && messageObject.isEdited() && !(messageObject.messageOwner.action instanceof TLRPC.TL_messageActionChatJoinedByRequest);
        final ItemOptions itemOptionsMakeOptions = ItemOptions.makeOptions(this.containerView, chatActivity.getResourceProvider(), (View) null, z2 || z4);
        if (z4) {
            final MessageSeenView messageSeenView = new MessageSeenView(getContext(), chatActivity.getCurrentAccount(), messageObject, chatActivity.currentChat);
            FrameLayout frameLayout = new FrameLayout(getContext());
            frameLayout.addView(messageSeenView, LayoutHelper.createFrame(-1, 36.0f));
            final ItemOptions itemOptionsMakeSwipeback = itemOptionsMakeOptions.makeSwipeback();
            ActionBarMenuSubItem actionBarMenuSubItem = new ActionBarMenuSubItem(getContext(), true, false, this.resourcesProvider);
            actionBarMenuSubItem.setItemHeight(44);
            actionBarMenuSubItem.setTextAndIcon(LocaleController.getString(R.string.Back), R.drawable.msg_arrow_back);
            actionBarMenuSubItem.getTextView().setPadding(LocaleController.isRTL ? 0 : AndroidUtilities.dp(40.0f), 0, LocaleController.isRTL ? AndroidUtilities.dp(40.0f) : 0, 0);
            FrameLayout frameLayout2 = new FrameLayout(getContext());
            final LinearLayout linearLayout = new LinearLayout(getContext());
            linearLayout.setBackgroundColor(Theme.getColor(Theme.key_actionBarDefaultSubmenuBackground, this.resourcesProvider));
            linearLayout.setOrientation(1);
            final RecyclerListView recyclerListViewCreateListView = messageSeenView.createListView();
            frameLayout2.addView(actionBarMenuSubItem);
            linearLayout.addView(frameLayout2);
            linearLayout.addView(new ActionBarPopupWindow.GapView(getContext(), this.resourcesProvider), LayoutHelper.createLinear(-1, 8));
            frameLayout2.setOnClickListener(new View.OnClickListener() { // from class: org.telegram.ui.TodoItemMenu.11
                @Override // android.view.View.OnClickListener
                public void onClick(View view) {
                    Bulletin.hideVisible();
                    itemOptionsMakeOptions.closeSwipeback();
                }
            });
            View.OnClickListener onClickListener = new View.OnClickListener() { // from class: org.telegram.ui.TodoItemMenu.12
                @Override // android.view.View.OnClickListener
                public void onClick(View view) {
                    if (messageSeenView.users.isEmpty()) {
                        return;
                    }
                    if (messageSeenView.users.size() == 1 && (messageSeenView.dates.size() <= 0 || ((Integer) messageSeenView.dates.get(0)).intValue() <= 0)) {
                        TLObject tLObject = (TLObject) messageSeenView.users.get(0);
                        if (tLObject == null) {
                            return;
                        }
                        Bundle bundle = new Bundle();
                        if (tLObject instanceof TLRPC.User) {
                            bundle.putLong("user_id", ((TLRPC.User) tLObject).id);
                        } else if (tLObject instanceof TLRPC.Chat) {
                            bundle.putLong("chat_id", ((TLRPC.Chat) tLObject).id);
                        }
                        chatActivity.presentFragment(new ProfileActivity(bundle));
                        TodoItemMenu.this.dismiss(false);
                        return;
                    }
                    if (SharedConfig.messageSeenHintCount > 0 && chatActivity.contentView.getKeyboardHeight() < AndroidUtilities.dp(20.0f)) {
                        chatActivity.messageSeenPrivacyBulletin = BulletinFactory.of(Bulletin.BulletinWindow.make(TodoItemMenu.this.getContext()), TodoItemMenu.this.resourcesProvider).createErrorBulletin(AndroidUtilities.replaceTags(LocaleController.getString(R.string.MessageSeenTooltipMessage)));
                        chatActivity.messageSeenPrivacyBulletin.setDuration(4000);
                        chatActivity.messageSeenPrivacyBulletin.show();
                        SharedConfig.updateMessageSeenHintCount(SharedConfig.messageSeenHintCount - 1);
                    }
                    recyclerListViewCreateListView.requestLayout();
                    linearLayout.requestLayout();
                    recyclerListViewCreateListView.getAdapter().notifyDataSetChanged();
                    itemOptionsMakeOptions.openSwipeback(itemOptionsMakeSwipeback);
                }
            };
            itemOptions = itemOptionsMakeOptions;
            todoItemMenu = this;
            messageSeenView.setOnClickListener(onClickListener);
            linearLayout.addView(recyclerListViewCreateListView, LayoutHelper.createLinear(-1, -2));
            itemOptionsMakeSwipeback.addView(linearLayout);
            itemOptions.addView(frameLayout);
            itemOptions.addGap();
        } else {
            itemOptions = itemOptionsMakeOptions;
            todoItemMenu = this;
            if (z) {
                itemOptions.addView(new MessagePrivateSeenView(todoItemMenu.getContext(), 0, messageObject, new Runnable() { // from class: org.telegram.ui.TodoItemMenu$$ExternalSyntheticLambda0
                    @Override // java.lang.Runnable
                    public final void run() {
                        this.f$0.lambda$setupMessageOptions$9();
                    }
                }, todoItemMenu.resourcesProvider), LayoutHelper.createLinear(-1, 36));
                itemOptions.addGap();
            } else if (z5) {
                MessagePrivateSeenView messagePrivateSeenView = new MessagePrivateSeenView(todoItemMenu.getContext(), 1, messageObject, new Runnable() { // from class: org.telegram.ui.TodoItemMenu$$ExternalSyntheticLambda1
                    @Override // java.lang.Runnable
                    public final void run() {
                        this.f$0.lambda$setupMessageOptions$10();
                    }
                }, todoItemMenu.resourcesProvider);
                messageObject = messageObject;
                itemOptions.addView(messagePrivateSeenView, LayoutHelper.createLinear(-1, 36));
                itemOptions.addGap();
            } else {
                messageObject = messageObject;
            }
        }
        int size = arrayList.size();
        for (int i2 = 0; i2 < size; i2++) {
            final int iIntValue = ((Integer) arrayList3.get(i2)).intValue();
            itemOptions.add(((Integer) arrayList.get(i2)).intValue(), (CharSequence) arrayList2.get(i2), new Runnable() { // from class: org.telegram.ui.TodoItemMenu$$ExternalSyntheticLambda2
                @Override // java.lang.Runnable
                public final void run() {
                    this.f$0.lambda$setupMessageOptions$11(callback, iIntValue);
                }
            });
        }
        itemOptions.setupSelectors();
        ViewGroup layout = itemOptions.getLayout();
        todoItemMenu.messageOptionsView = layout;
        layout.setPivotX(0.0f);
        todoItemMenu.messageOptionsView.setPivotY(0.0f);
        todoItemMenu.menuContainer.addView(todoItemMenu.messageOptionsView, LayoutHelper.createFrame(-2, -2, 51));
        View view = todoItemMenu.messageOptionsView;
        if (view instanceof ActionBarPopupWindow.ActionBarPopupWindowLayout) {
            ((ActionBarPopupWindow.ActionBarPopupWindowLayout) view).setOnSizeChangedListener(new ActionBarPopupWindow.onSizeChangedListener() { // from class: org.telegram.ui.TodoItemMenu$$ExternalSyntheticLambda3
                @Override // org.telegram.ui.ActionBar.ActionBarPopupWindow.onSizeChangedListener
                public final void onSizeChanged() {
                    this.f$0.updateTranslation();
                }
            });
            todoItemMenu.messageOptionsView.setOnTouchListener(new View.OnTouchListener() { // from class: org.telegram.ui.TodoItemMenu$$ExternalSyntheticLambda4
                @Override // android.view.View.OnTouchListener
                public final boolean onTouch(View view2, MotionEvent motionEvent) {
                    return this.f$0.lambda$setupMessageOptions$12(view2, motionEvent);
                }
            });
        }
        if (z3) {
            final ReactionsContainerLayout reactionsContainerLayout = new ReactionsContainerLayout((chatActivity.getUserConfig().getClientUserId() > chatActivity.getDialogId() ? 1 : (chatActivity.getUserConfig().getClientUserId() == chatActivity.getDialogId() ? 0 : -1)) == 0 ? 3 : 0, chatActivity, todoItemMenu.getContext(), chatActivity.getCurrentAccount(), todoItemMenu.resourcesProvider);
            reactionsContainerLayout.forceAttachToParent = true;
            float f = 22;
            reactionsContainerLayout.setPadding(AndroidUtilities.dp(4.0f) + (LocaleController.isRTL ? 0 : 24), AndroidUtilities.dp(4.0f), AndroidUtilities.dp(4.0f) + (LocaleController.isRTL ? 24 : 0), AndroidUtilities.dp(f));
            reactionsContainerLayout.setDelegate(new ReactionsContainerLayout.ReactionsContainerDelegate() { // from class: org.telegram.ui.TodoItemMenu.13
                @Override // org.telegram.ui.Components.ReactionsContainerLayout.ReactionsContainerDelegate
                public /* synthetic */ boolean allowLongPress() {
                    return ReactionsContainerLayout.ReactionsContainerDelegate.CC.$default$allowLongPress(this);
                }

                @Override // org.telegram.ui.Components.ReactionsContainerLayout.ReactionsContainerDelegate
                public /* synthetic */ boolean drawBackground() {
                    return ReactionsContainerLayout.ReactionsContainerDelegate.CC.$default$drawBackground(this);
                }

                @Override // org.telegram.ui.Components.ReactionsContainerLayout.ReactionsContainerDelegate
                public /* synthetic */ void drawRoundRect(Canvas canvas, RectF rectF, float f2, float f3, float f4, int i3, boolean z6) {
                    ReactionsContainerLayout.ReactionsContainerDelegate.CC.$default$drawRoundRect(this, canvas, rectF, f2, f3, f4, i3, z6);
                }

                @Override // org.telegram.ui.Components.ReactionsContainerLayout.ReactionsContainerDelegate
                public /* synthetic */ boolean needEnterText() {
                    return ReactionsContainerLayout.ReactionsContainerDelegate.CC.$default$needEnterText(this);
                }

                @Override // org.telegram.ui.Components.ReactionsContainerLayout.ReactionsContainerDelegate
                public /* synthetic */ void onEmojiWindowDismissed() {
                    ReactionsContainerLayout.ReactionsContainerDelegate.CC.$default$onEmojiWindowDismissed(this);
                }

                /* JADX WARN: Code duplicated, block: B:17:0x0065  */
                /* JADX WARN: Code duplicated, block: B:20:0x006b  */
                @Override // org.telegram.ui.Components.ReactionsContainerLayout.ReactionsContainerDelegate
                public void onReactionClicked(View view2, ReactionsLayoutInBubble.VisibleReaction visibleReaction, boolean z6, boolean z7) {
                    float f2;
                    float f3;
                    float f4;
                    int i3;
                    boolean z8;
                    float f5;
                    BaseCell baseCellFindMessageCell = chatActivity.findMessageCell(messageObject.getId(), true);
                    float f6 = 0.0f;
                    if (baseCellFindMessageCell instanceof ChatMessageCell) {
                        ChatMessageCell chatMessageCell = (ChatMessageCell) baseCellFindMessageCell;
                        ReactionsLayoutInBubble.ReactionButton reactionButton = chatMessageCell.reactionsLayoutInBubble.getReactionButton(visibleReaction);
                        if (reactionButton != null) {
                            ReactionsLayoutInBubble reactionsLayoutInBubble = chatMessageCell.reactionsLayoutInBubble;
                            f6 = reactionsLayoutInBubble.x + reactionButton.x + (reactionButton.width / 2.0f);
                            f4 = reactionsLayoutInBubble.y + reactionButton.y;
                            i3 = reactionButton.height;
                        } else {
                            f5 = 0.0f;
                        }
                        f3 = f5;
                        f2 = f6;
                        if (visibleReaction == null && visibleReaction.isStar) {
                            z8 = true;
                        } else {
                            z8 = z6;
                        }
                        chatActivity.selectReaction(baseCellFindMessageCell, messageObject, reactionsContainerLayout, view2, f2, f3, visibleReaction, false, z8, z7, false);
                        TodoItemMenu.this.dismiss(false);
                    }
                    if (baseCellFindMessageCell instanceof ChatActionCell) {
                        ChatActionCell chatActionCell = (ChatActionCell) baseCellFindMessageCell;
                        ReactionsLayoutInBubble.ReactionButton reactionButton2 = chatActionCell.reactionsLayoutInBubble.getReactionButton(visibleReaction);
                        if (reactionButton2 != null) {
                            ReactionsLayoutInBubble reactionsLayoutInBubble2 = chatActionCell.reactionsLayoutInBubble;
                            f6 = reactionsLayoutInBubble2.x + reactionButton2.x + (reactionButton2.width / 2.0f);
                            f4 = reactionsLayoutInBubble2.y + reactionButton2.y;
                            i3 = reactionButton2.height;
                        }
                        if (visibleReaction == null) {
                            z8 = z6;
                        } else {
                            z8 = z6;
                        }
                        chatActivity.selectReaction(baseCellFindMessageCell, messageObject, reactionsContainerLayout, view2, f2, f3, visibleReaction, false, z8, z7, false);
                        TodoItemMenu.this.dismiss(false);
                    }
                    f2 = 0.0f;
                    f3 = 0.0f;
                    if (visibleReaction == null) {
                        z8 = z6;
                    } else {
                        z8 = z6;
                    }
                    chatActivity.selectReaction(baseCellFindMessageCell, messageObject, reactionsContainerLayout, view2, f2, f3, visibleReaction, false, z8, z7, false);
                    TodoItemMenu.this.dismiss(false);
                    f5 = f4 + (i3 / 2.0f);
                    f3 = f5;
                    f2 = f6;
                    if (visibleReaction == null) {
                        z8 = z6;
                    } else {
                        z8 = z6;
                    }
                    chatActivity.selectReaction(baseCellFindMessageCell, messageObject, reactionsContainerLayout, view2, f2, f3, visibleReaction, false, z8, z7, false);
                    TodoItemMenu.this.dismiss(false);
                }
            });
            FrameLayout frameLayout3 = todoItemMenu.menuContainer;
            todoItemMenu.reactionsView = reactionsContainerLayout;
            frameLayout3.addView(reactionsContainerLayout, LayoutHelper.createFrame(-2, (int) ((reactionsContainerLayout.getTopOffset() / AndroidUtilities.density) + 52.0f + f), 51));
            reactionsContainerLayout.setMessage(messageObject, chatActivity.chatInfo, true);
            todoItemMenu.reactionsView.setTransitionProgress(1.0f);
        }
        todoItemMenu.updateTranslation();
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$setupMessageOptions$9() {
        dismiss(false);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$setupMessageOptions$10() {
        dismiss(false);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$setupMessageOptions$11(Utilities.Callback callback, int i) {
        callback.run(Integer.valueOf(i));
        boolean z = true;
        if (i != 1 && i != 13) {
            z = false;
        }
        dismiss(z);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ boolean lambda$setupMessageOptions$12(View view, MotionEvent motionEvent) {
        if (this.messageOptionsView == null || motionEvent.getAction() != 0) {
            return false;
        }
        Drawable backgroundDrawable = ((ActionBarPopupWindow.ActionBarPopupWindowLayout) this.messageOptionsView).getBackgroundDrawable();
        RectF rectF = AndroidUtilities.rectTmp;
        rectF.set(backgroundDrawable.getBounds());
        rectF.offset(this.messageOptionsView.getX(), this.messageOptionsView.getY());
        if (rectF.contains(motionEvent.getX(), motionEvent.getY())) {
            return false;
        }
        dismiss(true);
        return true;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void setupTranslation() {
        if (this.hasTranslation || this.windowView.getWidth() <= 0) {
            return;
        }
        ChatMessageCell chatMessageCell = this.cell;
        if (chatMessageCell != null) {
            int[] iArr = new int[2];
            chatMessageCell.getLocationOnScreen(iArr);
            int i = iArr[0];
            Rect rect = this.insets;
            this.tx = i - rect.left;
            float f = iArr[1] - rect.top;
            this.ty = f;
            if (!this.hasDestTranslation) {
                this.hasDestTranslation = true;
                this.dtx1 = 0.0f;
                this.dty1 = f;
                if (this.messageOptionsView != null) {
                    float height = f + this.cell.getHeight() + this.messageOptionsView.getHeight();
                    int height2 = this.windowView.getHeight();
                    Rect rect2 = this.insets;
                    if (height > ((height2 - rect2.top) - rect2.bottom) - AndroidUtilities.dp(66.0f)) {
                        int height3 = this.windowView.getHeight();
                        Rect rect3 = this.insets;
                        this.dty1 = ((((height3 - rect3.top) - rect3.bottom) - AndroidUtilities.dp(66.0f)) - this.cell.getHeight()) - this.messageOptionsView.getHeight();
                    }
                }
                int todoIndex = this.myTaskCell.getTodoIndex(this.taskId);
                this.myTaskCell.getPollButtonTop(todoIndex);
                float pollButtonBottom = this.myTaskCell.getPollButtonBottom(todoIndex);
                this.dtx2 = 0.0f;
                float f2 = this.ty;
                this.dty2 = f2;
                int i2 = (int) pollButtonBottom;
                float f3 = i2;
                float f4 = f2 + f3;
                int height4 = this.windowView.getHeight();
                Rect rect4 = this.insets;
                if (f4 > (((height4 - rect4.top) - rect4.bottom) - AndroidUtilities.dp(78.0f)) - this.hintTextView.getHeight()) {
                    int height5 = this.windowView.getHeight();
                    Rect rect5 = this.insets;
                    this.dty2 = ((((height5 - rect5.top) - rect5.bottom) - AndroidUtilities.dp(78.0f)) - this.hintTextView.getHeight()) - i2;
                }
                View view = this.taskOptionsView;
                if (view != null) {
                    float height6 = this.dty2 + f3 + view.getHeight();
                    int height7 = this.windowView.getHeight();
                    Rect rect6 = this.insets;
                    if (height6 > (((height7 - rect6.top) - rect6.bottom) - AndroidUtilities.dp(78.0f)) - this.hintTextView.getHeight()) {
                        int height8 = this.windowView.getHeight();
                        Rect rect7 = this.insets;
                        this.dty2 = (((((height8 - rect7.top) - rect7.bottom) - AndroidUtilities.dp(78.0f)) - this.hintTextView.getHeight()) - i2) - this.taskOptionsView.getHeight();
                    }
                }
            }
            updateTranslation();
        } else {
            this.ty = 0.0f;
            this.tx = 0.0f;
        }
        this.hasTranslation = true;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void updateTranslation() {
        float positionAnimated = this.viewPager.getPositionAnimated();
        float fLerp = AndroidUtilities.lerp(0, -this.viewPager.getWidth(), positionAnimated);
        float fLerp2 = AndroidUtilities.lerp(this.viewPager.getWidth(), 0, positionAnimated);
        if (this.hasTranslation) {
            View view = this.messageOptionsView;
            if (view instanceof ActionBarPopupWindow.ActionBarPopupWindowLayout) {
                ActionBarPopupWindow.ActionBarPopupWindowLayout actionBarPopupWindowLayout = (ActionBarPopupWindow.ActionBarPopupWindowLayout) view;
                this.dtx1 = 0.0f;
                float f = this.ty;
                this.dty1 = f;
                if (view != null) {
                    float height = f + this.cell.getHeight() + actionBarPopupWindowLayout.getVisibleHeight();
                    int height2 = this.windowView.getHeight();
                    Rect rect = this.insets;
                    if (height > ((height2 - rect.top) - rect.bottom) - AndroidUtilities.dp(66.0f)) {
                        int height3 = this.windowView.getHeight();
                        Rect rect2 = this.insets;
                        this.dty1 = ((((height3 - rect2.top) - rect2.bottom) - AndroidUtilities.dp(66.0f)) - this.cell.getHeight()) - actionBarPopupWindowLayout.getVisibleHeight();
                    }
                }
            }
        }
        this.myCell.setTranslationX(AndroidUtilities.lerp(this.tx, this.dtx1, this.dismissingWithAlpha ? 1.0f : this.openProgress) + fLerp2);
        this.myCell.setTranslationY(AndroidUtilities.lerp(this.ty, this.dty1, this.dismissingWithAlpha ? 1.0f : this.openProgress));
        View view2 = this.messageOptionsView;
        if (view2 != null) {
            if (this.isOut) {
                view2.setTranslationX(((((this.dtx1 + fLerp2) + this.myCell.getLeft()) + this.myCell.getPollButtonsLeft()) - AndroidUtilities.dp(8.0f)) - this.messageOptionsView.getLeft());
            } else {
                view2.setTranslationX((((this.dtx1 + fLerp2) + (this.myCell.needDrawAvatar() ? AndroidUtilities.dp(48.0f) : 0)) + this.myCell.getLeft()) - this.messageOptionsView.getLeft());
            }
            this.messageOptionsViewMaxWidth = this.menuContainer.getMeasuredWidth() - (this.messageOptionsView.getX() - fLerp2);
            this.messageOptionsView.setTranslationY(((this.myCell.getY() + this.myCell.getHeight()) - this.messageOptionsView.getTop()) - this.menuContainer.getTop());
            this.messageOptionsView.setAlpha(this.openProgress);
            float fLerp3 = AndroidUtilities.lerp(0.75f, 1.0f, this.openProgress);
            this.messageOptionsView.setScaleX(fLerp3);
            this.messageOptionsView.setScaleY(fLerp3);
        }
        this.myTaskCell.setTranslationX(AndroidUtilities.lerp(this.tx, this.dtx2, this.dismissingWithAlpha ? 1.0f : this.openProgress) + fLerp);
        this.myTaskCell.setTranslationY(AndroidUtilities.lerp(this.ty, this.dty2, this.dismissingWithAlpha ? 1.0f : this.openProgress));
        if (this.taskOptionsView != null) {
            int todoIndex = this.myTaskCell.getTodoIndex(this.taskId);
            this.myTaskCell.getPollButtonTop(todoIndex);
            float pollButtonBottom = this.myTaskCell.getPollButtonBottom(todoIndex);
            if (this.isOut) {
                this.taskOptionsView.setTranslationX(((((this.dtx2 + fLerp) + this.myTaskCell.getLeft()) + this.myTaskCell.getPollButtonsLeft()) - AndroidUtilities.dp(8.0f)) - this.taskOptionsView.getLeft());
            } else {
                this.taskOptionsView.setTranslationX((((this.dtx2 + fLerp) + (this.myTaskCell.needDrawAvatar() ? AndroidUtilities.dp(48.0f) : 0)) + this.myTaskCell.getLeft()) - this.taskOptionsView.getLeft());
            }
            this.taskOptionsViewMaxWidth = this.menuContainer.getMeasuredWidth() - (this.taskOptionsView.getX() - fLerp2);
            this.taskOptionsView.setTranslationY(((this.myTaskCell.getY() + ((int) pollButtonBottom)) - this.taskOptionsView.getTop()) - this.menuContainer.getTop());
            this.taskOptionsView.setAlpha(this.openProgress);
            float fLerp4 = AndroidUtilities.lerp(0.75f, 1.0f, this.openProgress);
            this.taskOptionsView.setScaleX(fLerp4);
            this.taskOptionsView.setScaleY(fLerp4);
        }
        if (this.dismissingWithAlpha) {
            this.myCell.setAlpha(this.openProgress);
            this.myTaskCell.setAlpha(this.openProgress);
        }
        if (this.reactionsView != null) {
            float fMax = fLerp2 + Math.max(0.0f, ((this.myCell.getBoundsRight() + this.myCell.getBoundsLeft()) / 2.0f) - (this.reactionsView.getWidth() * 0.8f));
            this.reactionsView.setTranslationX(fMax);
            this.reactionsView.setTranslationY(Math.max(0.0f, ((this.myCell.getY() - this.reactionsView.getHeight()) + AndroidUtilities.dp(22.0f)) - this.menuContainer.getTop()));
            this.reactionsView.setAlpha(this.openProgress);
            View windowView = this.reactionsView.getWindowView();
            if (windowView != null) {
                windowView.setTranslationX(fMax);
                windowView.setAlpha(this.openProgress);
            }
        }
        this.hintTextView.setTranslationX(fLerp);
        this.hintTextView.setAlpha(this.openProgress);
        this.tabsView.setSelectedTab(positionAnimated);
        this.tabsView.setAlpha(this.openProgress);
    }

    private void prepareBlur(final View view) {
        if (view != null) {
            view.setVisibility(4);
        }
        AndroidUtilities.makeGlobalBlurBitmap(new Utilities.Callback() { // from class: org.telegram.ui.TodoItemMenu$$ExternalSyntheticLambda6
            @Override // org.telegram.messenger.Utilities.Callback
            public final void run(Object obj) {
                this.f$0.lambda$prepareBlur$13(view, (Bitmap) obj);
            }
        }, 14.0f);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$prepareBlur$13(View view, Bitmap bitmap) {
        if (view != null) {
            view.setVisibility(0);
        }
        this.blurBitmap = bitmap;
        Paint paint = new Paint(1);
        this.blurBitmapPaint = paint;
        Bitmap bitmap2 = this.blurBitmap;
        Shader.TileMode tileMode = Shader.TileMode.CLAMP;
        BitmapShader bitmapShader = new BitmapShader(bitmap2, tileMode, tileMode);
        this.blurBitmapShader = bitmapShader;
        paint.setShader(bitmapShader);
        ColorMatrix colorMatrix = new ColorMatrix();
        AndroidUtilities.adjustSaturationColorMatrix(colorMatrix, Theme.isCurrentThemeDark() ? 0.05f : 0.25f);
        AndroidUtilities.adjustBrightnessColorMatrix(colorMatrix, Theme.isCurrentThemeDark() ? -0.02f : -0.04f);
        this.blurBitmapPaint.setColorFilter(new ColorMatrixColorFilter(colorMatrix));
        this.blurMatrix = new Matrix();
    }

    @Override // android.app.Dialog
    public void show() {
        if (AndroidUtilities.isSafeToShow(getContext())) {
            super.show();
            prepareBlur(null);
            this.setTaskInvisible = true;
            this.open = true;
            animateOpenTo(true, null);
        }
    }

    @Override // android.app.Dialog, android.content.DialogInterface
    public void dismiss() {
        dismiss(true);
    }

    public void dismiss(boolean z) {
        ChatMessageCell chatMessageCell;
        ReactionsContainerLayout reactionsContainerLayout;
        if (z && (reactionsContainerLayout = this.reactionsView) != null && reactionsContainerLayout.getReactionsWindow() != null && this.reactionsView.getReactionsWindow().isShowing()) {
            this.reactionsView.dismissWindow();
            return;
        }
        if (this.dismissing) {
            return;
        }
        this.dismissing = true;
        this.hasTranslation = false;
        this.viewPager.cancelTouches();
        final boolean z2 = this.viewPager.getCurrentPosition() == 1;
        if (z && z2) {
            ChatMessageCell chatMessageCell2 = this.cell;
            if (chatMessageCell2 != null) {
                chatMessageCell2.setVisibility(4);
                this.cell.invalidate();
            }
        } else if (!z && (chatMessageCell = this.cell) != null) {
            chatMessageCell.setVisibility(0);
            ChatMessageCell chatMessageCell3 = this.cell;
            chatMessageCell3.doNotDrawTaskId = -1;
            chatMessageCell3.invalidate();
        }
        this.dismissingWithAlpha = !z;
        setupTranslation();
        this.open = false;
        animateOpenTo(false, new Runnable() { // from class: org.telegram.ui.TodoItemMenu$$ExternalSyntheticLambda5
            @Override // java.lang.Runnable
            public final void run() {
                this.f$0.lambda$dismiss$15(z2);
            }
        });
        this.windowView.invalidate();
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$dismiss$14() {
        super.dismiss();
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$dismiss$15(boolean z) {
        AndroidUtilities.runOnUIThread(new Runnable() { // from class: org.telegram.ui.TodoItemMenu$$ExternalSyntheticLambda18
            @Override // java.lang.Runnable
            public final void run() {
                this.f$0.lambda$dismiss$14();
            }
        });
        ChatMessageCell chatMessageCell = this.cell;
        if (chatMessageCell != null) {
            chatMessageCell.setVisibility(0);
            if (!z) {
                ChatMessageCell chatMessageCell2 = this.cell;
                chatMessageCell2.syncTodoCheck(chatMessageCell2.getTodoIndex(this.taskId), this.myTaskCell);
            }
            ChatMessageCell chatMessageCell3 = this.cell;
            chatMessageCell3.doNotDrawTaskId = -1;
            chatMessageCell3.invalidate();
        }
        Runnable runnable = this.dismissListener;
        if (runnable != null) {
            AndroidUtilities.runOnUIThread(runnable);
            this.dismissListener = null;
        }
    }

    public void setOnDismissListener(Runnable runnable) {
        this.dismissListener = runnable;
    }

    private void animateOpenTo(final boolean z, final Runnable runnable) {
        ValueAnimator valueAnimator = this.openAnimator;
        if (valueAnimator != null) {
            valueAnimator.cancel();
        }
        ValueAnimator valueAnimator2 = this.open2Animator;
        if (valueAnimator2 != null) {
            valueAnimator2.cancel();
        }
        setupTranslation();
        ValueAnimator valueAnimatorOfFloat = ValueAnimator.ofFloat(this.openProgress, z ? 1.0f : 0.0f);
        this.openAnimator = valueAnimatorOfFloat;
        valueAnimatorOfFloat.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() { // from class: org.telegram.ui.TodoItemMenu$$ExternalSyntheticLambda14
            @Override // android.animation.ValueAnimator.AnimatorUpdateListener
            public final void onAnimationUpdate(ValueAnimator valueAnimator3) {
                this.f$0.lambda$animateOpenTo$16(valueAnimator3);
            }
        });
        this.openAnimator.addListener(new AnimatorListenerAdapter() { // from class: org.telegram.ui.TodoItemMenu.14
            @Override // android.animation.AnimatorListenerAdapter, android.animation.Animator.AnimatorListener
            public void onAnimationEnd(Animator animator) {
                TodoItemMenu.this.openProgress = z ? 1.0f : 0.0f;
                TodoItemMenu.this.windowView.invalidate();
                TodoItemMenu.this.containerView.invalidate();
                TodoItemMenu.this.updateTranslation();
                Runnable runnable2 = runnable;
                if (runnable2 != null) {
                    runnable2.run();
                }
            }
        });
        long j = !z ? 330L : 520L;
        ValueAnimator valueAnimator3 = this.openAnimator;
        CubicBezierInterpolator cubicBezierInterpolator = CubicBezierInterpolator.EASE_OUT_QUINT;
        valueAnimator3.setInterpolator(cubicBezierInterpolator);
        this.openAnimator.setDuration(j);
        this.openAnimator.start();
        ValueAnimator valueAnimatorOfFloat2 = ValueAnimator.ofFloat(this.openProgress2, z ? 1.0f : 0.0f);
        this.open2Animator = valueAnimatorOfFloat2;
        valueAnimatorOfFloat2.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() { // from class: org.telegram.ui.TodoItemMenu$$ExternalSyntheticLambda15
            @Override // android.animation.ValueAnimator.AnimatorUpdateListener
            public final void onAnimationUpdate(ValueAnimator valueAnimator4) {
                this.f$0.lambda$animateOpenTo$17(valueAnimator4);
            }
        });
        this.open2Animator.addListener(new AnimatorListenerAdapter() { // from class: org.telegram.ui.TodoItemMenu.15
            @Override // android.animation.AnimatorListenerAdapter, android.animation.Animator.AnimatorListener
            public void onAnimationEnd(Animator animator) {
                TodoItemMenu.this.openProgress2 = z ? 1.0f : 0.0f;
            }
        });
        this.open2Animator.setDuration((long) (j * 1.5f));
        this.open2Animator.setInterpolator(cubicBezierInterpolator);
        this.open2Animator.start();
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$animateOpenTo$16(ValueAnimator valueAnimator) {
        this.openProgress = ((Float) valueAnimator.getAnimatedValue()).floatValue();
        this.windowView.invalidate();
        this.containerView.invalidate();
        updateTranslation();
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$animateOpenTo$17(ValueAnimator valueAnimator) {
        this.openProgress2 = ((Float) valueAnimator.getAnimatedValue()).floatValue();
    }
}
