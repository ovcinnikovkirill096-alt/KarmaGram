package com.radolyn.ayugram.ui;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.animation.ValueAnimator;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.ContentValues;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.Configuration;
import android.graphics.Canvas;
import android.graphics.Outline;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Parcelable;
import android.telephony.TelephonyManager;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.StaticLayout;
import android.text.TextUtils;
import android.text.style.CharacterStyle;
import android.text.style.URLSpan;
import android.util.Property;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.TextureView;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewOutlineProvider;
import android.view.ViewTreeObserver;
import android.view.accessibility.AccessibilityNodeInfo;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.TextView;
import android.widget.Toast;
import androidx.collection.LongSparseArray;
import androidx.core.content.FileProvider;
import androidx.core.util.Pair;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.ChatListItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.LinearSmoothScrollerCustom;
import androidx.recyclerview.widget.RecyclerView;
import com.android.dx.io.Opcodes;
import com.android.tools.r8.RecordTag;
import com.exteragram.messenger.ai.ui.GenerateFromMessageBottomSheet$GenerationData$$ExternalSyntheticRecord0;
import com.exteragram.messenger.components.MessageDetailsPopupWrapper;
import com.exteragram.messenger.pillstack.ui.pills.crypto.RatePill$$ExternalSyntheticLambda1;
import com.exteragram.messenger.utils.chats.ChatUtils;
import com.exteragram.messenger.utils.system.VibratorUtils;
import com.google.android.exoplayer2.ui.AspectRatioFrameLayout;
import com.radolyn.ayugram.database.entities.AyuMessageBase;
import com.radolyn.ayugram.database.entities.DeletedMessageFull;
import com.radolyn.ayugram.database.entities.EditedMessage;
import com.radolyn.ayugram.utils.AyuHistoryHook;
import j$.util.Objects;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import okhttp3.internal.url._UrlKt;
import org.mvel2.MVEL;
import org.telegram.PhoneFormat.PhoneFormat;
import org.telegram.messenger.AndroidUtilities;
import org.telegram.messenger.AnimationNotificationsLocker;
import org.telegram.messenger.ApplicationLoader;
import org.telegram.messenger.BotInlineKeyboard;
import org.telegram.messenger.BuildVars;
import org.telegram.messenger.ChatMessageSharedResources;
import org.telegram.messenger.ChatObject;
import org.telegram.messenger.CodeHighlighting;
import org.telegram.messenger.ContactsController;
import org.telegram.messenger.DialogObject;
import org.telegram.messenger.FileLoader;
import org.telegram.messenger.FileLog;
import org.telegram.messenger.ImageLocation;
import org.telegram.messenger.ImageReceiver;
import org.telegram.messenger.LocaleController;
import org.telegram.messenger.MediaController;
import org.telegram.messenger.MediaDataController;
import org.telegram.messenger.MessageObject;
import org.telegram.messenger.MessagesController;
import org.telegram.messenger.MessagesStorage;
import org.telegram.messenger.NotificationCenter;
import org.telegram.messenger.R;
import org.telegram.messenger.SharedConfig;
import org.telegram.messenger.UserConfig;
import org.telegram.messenger.UserObject;
import org.telegram.messenger.Utilities;
import org.telegram.messenger.browser.Browser;
import org.telegram.tgnet.RequestDelegate;
import org.telegram.tgnet.TLObject;
import org.telegram.tgnet.TLRPC;
import org.telegram.ui.ActionBar.ActionBar;
import org.telegram.ui.ActionBar.ActionBarMenuItem;
import org.telegram.ui.ActionBar.ActionBarMenuSubItem;
import org.telegram.ui.ActionBar.ActionBarPopupWindow;
import org.telegram.ui.ActionBar.AlertDialog;
import org.telegram.ui.ActionBar.BackDrawable;
import org.telegram.ui.ActionBar.BaseFragment;
import org.telegram.ui.ActionBar.BottomSheet;
import org.telegram.ui.ActionBar.Theme;
import org.telegram.ui.ArticleViewer;
import org.telegram.ui.AvatarPreviewer;
import org.telegram.ui.Cells.ChatActionCell;
import org.telegram.ui.Cells.ChatLoadingCell;
import org.telegram.ui.Cells.ChatMessageCell;
import org.telegram.ui.Cells.ChatUnreadCell;
import org.telegram.ui.Cells.TextSelectionHelper;
import org.telegram.ui.ChatActivity;
import org.telegram.ui.Components.AnimatedEmojiDrawable;
import org.telegram.ui.Components.AnimatedEmojiSpan;
import org.telegram.ui.Components.Bulletin;
import org.telegram.ui.Components.BulletinFactory;
import org.telegram.ui.Components.ChatAvatarContainer;
import org.telegram.ui.Components.ChatScrimPopupContainerLayout;
import org.telegram.ui.Components.CubicBezierInterpolator;
import org.telegram.ui.Components.EmbedBottomSheet;
import org.telegram.ui.Components.EmojiPacksAlert;
import org.telegram.ui.Components.ItemOptions;
import org.telegram.ui.Components.LayoutHelper;
import org.telegram.ui.Components.MessageContainsEmojiButton;
import org.telegram.ui.Components.PhonebookShareAlert;
import org.telegram.ui.Components.PipRoundVideoView;
import org.telegram.ui.Components.RadialProgressView;
import org.telegram.ui.Components.RecyclerAnimationScrollHelper;
import org.telegram.ui.Components.RecyclerListView;
import org.telegram.ui.Components.ScrimOptions;
import org.telegram.ui.Components.ShareAlert;
import org.telegram.ui.Components.SizeNotifierFrameLayout;
import org.telegram.ui.Components.StickersAlert;
import org.telegram.ui.Components.URLSpanMono;
import org.telegram.ui.Components.URLSpanNoUnderline;
import org.telegram.ui.Components.URLSpanReplacement;
import org.telegram.ui.Components.URLSpanUserMention;
import org.telegram.ui.Components.UndoView;
import org.telegram.ui.Components.blur3.BlurredBackgroundDrawableViewFactory;
import org.telegram.ui.Components.blur3.drawable.color.BlurredBackgroundColorProviderThemed;
import org.telegram.ui.Components.blur3.source.BlurredBackgroundSourceColor;
import org.telegram.ui.Components.chat.layouts.ButtonOnClickListener;
import org.telegram.ui.Components.chat.layouts.ChatActivitySideControlsButtonsLayout;
import org.telegram.ui.Components.inset.WindowInsetsStateHolder;
import org.telegram.ui.Components.voip.VoIPHelper;
import org.telegram.ui.ContactAddActivity;
import org.telegram.ui.CountrySelectActivity;
import org.telegram.ui.DialogsActivity;
import org.telegram.ui.LanguageSelectActivity;
import org.telegram.ui.LaunchActivity;
import org.telegram.ui.LocationActivity;
import org.telegram.ui.NewContactBottomSheet;
import org.telegram.ui.PhotoViewer;
import org.telegram.ui.PinchToZoomHelper;
import org.telegram.ui.ProfileActivity;
import org.telegram.ui.ThemePreviewActivity;

public class AyuMessageHistory extends BaseFragment implements NotificationCenter.NotificationCenterDelegate {
    private AspectRatioFrameLayout aspectRatioFrameLayout;
    private ChatAvatarContainer avatarContainer;
    private ChatActivity chatActivity;
    private ChatActivityAdapter chatAdapter;
    private LinearLayoutManager chatLayoutManager;
    private ChatListItemAnimator chatListItemAnimator;
    private RecyclerListView chatListView;
    private final ArrayList chatMessageCellsCache;
    private RecyclerAnimationScrollHelper chatScrollHelper;
    private boolean checkTextureViewPosition;
    private SizeNotifierFrameLayout contentView;
    protected TLRPC.Chat currentChat;
    protected TLRPC.EncryptedChat currentEncryptedChat;
    private boolean currentFloatingDateOnScreen;
    private boolean currentFloatingTopIsNotMessage;
    private final MessageObject currentMessageObject;
    protected TLRPC.User currentUser;
    private TextView emptyView;
    private FrameLayout emptyViewContainer;
    private boolean endReached;
    private AnimatorSet floatingDateAnimation;
    private ChatActionCell floatingDateView;
    private int highlightMessageId;
    private boolean loading;
    protected ArrayList messages;
    private final HashMap messagesByDays;
    private final LongSparseArray messagesDict;
    private long minEventId;
    private final int mode;
    private final AnimationNotificationsLocker notificationsLocker;
    private boolean pagedownHidden;
    private int pagedownScrollAccumulator;
    private boolean paused;
    private RadialProgressView progressBar;
    private FrameLayout progressView;
    private View progressView2;
    private final PhotoViewer.PhotoViewerProvider provider;
    private FrameLayout roundVideoContainer;
    private long savedScrollMsgId;
    private int savedScrollOffset;
    private int savedScrollPosition;
    private AnimatorSet scrimAnimatorSet;
    private Paint scrimPaint;
    private float scrimPaintAlpha;
    private ActionBarPopupWindow scrimPopupWindow;
    private View scrimView;
    private int scrollToOffsetOnRecreate;
    private int scrollToPositionOnRecreate;
    private boolean scrollingFloatingDate;
    private boolean scrollingToHighlight;
    private ChatActivitySideControlsButtonsLayout searchButtonLayout;
    private ActionBarMenuItem searchItem;
    private String searchQuery;
    private boolean searchWas;
    private MessageObject selectedObject;
    public ChatMessageSharedResources sharedResources;
    private final ChatActivity.ThemeDelegate theme;
    private final Long topicId;
    private UndoView undoView;
    private Runnable unselectRunnable;
    private TextureView videoTextureView;
    private boolean wasPaused;
    private final WindowInsetsStateHolder windowInsetsStateHolder;

    @Override // org.telegram.ui.ActionBar.BaseFragment
    public boolean isSupportEdgeToEdge() {
        return true;
    }

    public AyuMessageHistory(ChatActivity chatActivity, TLRPC.Chat chat, TLRPC.User user, MessageObject messageObject, ChatActivity.ThemeDelegate themeDelegate) {
        this.chatMessageCellsCache = new ArrayList();
        this.notificationsLocker = new AnimationNotificationsLocker(new int[]{NotificationCenter.chatInfoDidLoad, NotificationCenter.dialogsNeedReload, NotificationCenter.closeChats, NotificationCenter.messagesDidLoad, NotificationCenter.botKeyboardDidLoad});
        this.messagesDict = new LongSparseArray();
        this.messagesByDays = new HashMap();
        this.messages = new ArrayList();
        this.provider = new PhotoViewer.EmptyPhotoViewerProvider() { // from class: com.radolyn.ayugram.ui.AyuMessageHistory.1
            @Override // org.telegram.ui.PhotoViewer.EmptyPhotoViewerProvider, org.telegram.ui.PhotoViewer.PhotoViewerProvider
            public PhotoViewer.PlaceProviderObject getPlaceForPhoto(MessageObject messageObject2, TLRPC.FileLocation fileLocation, int i, boolean z, boolean z2) {
                ChatActionCell chatActionCell;
                MessageObject messageObject3;
                MessageObject messageObject4;
                int childCount = AyuMessageHistory.this.chatListView.getChildCount();
                int i2 = 0;
                while (true) {
                    ImageReceiver photoImage = null;
                    if (i2 >= childCount) {
                        return null;
                    }
                    View childAt = AyuMessageHistory.this.chatListView.getChildAt(i2);
                    if (childAt instanceof ChatMessageCell) {
                        ChatMessageCell chatMessageCell = (ChatMessageCell) childAt;
                        if (messageObject2 != null && (messageObject4 = chatMessageCell.getMessageObject()) != null && messageObject4.getId() == messageObject2.getId()) {
                            photoImage = chatMessageCell.getPhotoImage();
                        }
                    } else if ((childAt instanceof ChatActionCell) && (messageObject3 = (chatActionCell = (ChatActionCell) childAt).getMessageObject()) != null) {
                        if (messageObject2 != null) {
                            if (messageObject3.getId() == messageObject2.getId()) {
                                photoImage = chatActionCell.getPhotoImage();
                            }
                        } else if (fileLocation != null && messageObject3.photoThumbs != null) {
                            for (int i3 = 0; i3 < messageObject3.photoThumbs.size(); i3++) {
                                TLRPC.FileLocation fileLocation2 = messageObject3.photoThumbs.get(i3).location;
                                if (fileLocation2.volume_id == fileLocation.volume_id && fileLocation2.local_id == fileLocation.local_id) {
                                    photoImage = chatActionCell.getPhotoImage();
                                    break;
                                }
                            }
                        }
                    }
                    if (photoImage != null) {
                        int[] iArr = new int[2];
                        childAt.getLocationInWindow(iArr);
                        PhotoViewer.PlaceProviderObject placeProviderObject = new PhotoViewer.PlaceProviderObject();
                        placeProviderObject.viewX = iArr[0];
                        placeProviderObject.viewY = iArr[1];
                        placeProviderObject.parentView = AyuMessageHistory.this.chatListView;
                        placeProviderObject.imageReceiver = photoImage;
                        placeProviderObject.thumb = photoImage.getBitmapSafe();
                        placeProviderObject.radius = photoImage.getRoundRadius(true);
                        placeProviderObject.isEvent = true;
                        return placeProviderObject;
                    }
                    i2++;
                }
            }
        };
        this.highlightMessageId = Integer.MAX_VALUE;
        this.scrollToPositionOnRecreate = -1;
        this.scrollToOffsetOnRecreate = 0;
        this.paused = true;
        this.wasPaused = false;
        this.searchQuery = _UrlKt.FRAGMENT_ENCODE_SET;
        this.scrimPaintAlpha = 0.0f;
        this.windowInsetsStateHolder = new WindowInsetsStateHolder(new Runnable() { // from class: com.radolyn.ayugram.ui.AyuMessageHistory$$ExternalSyntheticLambda0
            @Override // java.lang.Runnable
            public final void run() {
                this.f$0.checkInsets();
            }
        });
        this.savedScrollPosition = -1;
        this.chatActivity = chatActivity;
        this.currentChat = chat;
        this.currentUser = user;
        this.currentMessageObject = messageObject;
        this.topicId = null;
        this.mode = 1;
        this.theme = themeDelegate;
    }

    public AyuMessageHistory(ChatActivity chatActivity, TLRPC.Chat chat, TLRPC.User user, TLRPC.EncryptedChat encryptedChat, long j, ChatActivity.ThemeDelegate themeDelegate) {
        this.chatMessageCellsCache = new ArrayList();
        this.notificationsLocker = new AnimationNotificationsLocker(new int[]{NotificationCenter.chatInfoDidLoad, NotificationCenter.dialogsNeedReload, NotificationCenter.closeChats, NotificationCenter.messagesDidLoad, NotificationCenter.botKeyboardDidLoad});
        this.messagesDict = new LongSparseArray();
        this.messagesByDays = new HashMap();
        this.messages = new ArrayList();
        this.provider = new PhotoViewer.EmptyPhotoViewerProvider() { // from class: com.radolyn.ayugram.ui.AyuMessageHistory.1
            @Override // org.telegram.ui.PhotoViewer.EmptyPhotoViewerProvider, org.telegram.ui.PhotoViewer.PhotoViewerProvider
            public PhotoViewer.PlaceProviderObject getPlaceForPhoto(MessageObject messageObject2, TLRPC.FileLocation fileLocation, int i, boolean z, boolean z2) {
                ChatActionCell chatActionCell;
                MessageObject messageObject3;
                MessageObject messageObject4;
                int childCount = AyuMessageHistory.this.chatListView.getChildCount();
                int i2 = 0;
                while (true) {
                    ImageReceiver photoImage = null;
                    if (i2 >= childCount) {
                        return null;
                    }
                    View childAt = AyuMessageHistory.this.chatListView.getChildAt(i2);
                    if (childAt instanceof ChatMessageCell) {
                        ChatMessageCell chatMessageCell = (ChatMessageCell) childAt;
                        if (messageObject2 != null && (messageObject4 = chatMessageCell.getMessageObject()) != null && messageObject4.getId() == messageObject2.getId()) {
                            photoImage = chatMessageCell.getPhotoImage();
                        }
                    } else if ((childAt instanceof ChatActionCell) && (messageObject3 = (chatActionCell = (ChatActionCell) childAt).getMessageObject()) != null) {
                        if (messageObject2 != null) {
                            if (messageObject3.getId() == messageObject2.getId()) {
                                photoImage = chatActionCell.getPhotoImage();
                            }
                        } else if (fileLocation != null && messageObject3.photoThumbs != null) {
                            for (int i3 = 0; i3 < messageObject3.photoThumbs.size(); i3++) {
                                TLRPC.FileLocation fileLocation2 = messageObject3.photoThumbs.get(i3).location;
                                if (fileLocation2.volume_id == fileLocation.volume_id && fileLocation2.local_id == fileLocation.local_id) {
                                    photoImage = chatActionCell.getPhotoImage();
                                    break;
                                }
                            }
                        }
                    }
                    if (photoImage != null) {
                        int[] iArr = new int[2];
                        childAt.getLocationInWindow(iArr);
                        PhotoViewer.PlaceProviderObject placeProviderObject = new PhotoViewer.PlaceProviderObject();
                        placeProviderObject.viewX = iArr[0];
                        placeProviderObject.viewY = iArr[1];
                        placeProviderObject.parentView = AyuMessageHistory.this.chatListView;
                        placeProviderObject.imageReceiver = photoImage;
                        placeProviderObject.thumb = photoImage.getBitmapSafe();
                        placeProviderObject.radius = photoImage.getRoundRadius(true);
                        placeProviderObject.isEvent = true;
                        return placeProviderObject;
                    }
                    i2++;
                }
            }
        };
        this.highlightMessageId = Integer.MAX_VALUE;
        this.scrollToPositionOnRecreate = -1;
        this.scrollToOffsetOnRecreate = 0;
        this.paused = true;
        this.wasPaused = false;
        this.searchQuery = _UrlKt.FRAGMENT_ENCODE_SET;
        this.scrimPaintAlpha = 0.0f;
        this.windowInsetsStateHolder = new WindowInsetsStateHolder(new Runnable() { // from class: com.radolyn.ayugram.ui.AyuMessageHistory$$ExternalSyntheticLambda0
            @Override // java.lang.Runnable
            public final void run() {
                this.f$0.checkInsets();
            }
        });
        this.savedScrollPosition = -1;
        this.chatActivity = chatActivity;
        this.currentChat = chat;
        this.currentUser = user;
        this.currentEncryptedChat = encryptedChat;
        this.currentMessageObject = null;
        this.topicId = Long.valueOf(j);
        this.mode = 2;
        this.theme = themeDelegate;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public long getDialogId() {
        int i = this.mode;
        if (i == 1) {
            return this.currentMessageObject.messageOwner.dialog_id;
        }
        if (i == 2) {
            TLRPC.EncryptedChat encryptedChat = this.currentEncryptedChat;
            if (encryptedChat != null) {
                return DialogObject.makeEncryptedDialogId(encryptedChat.id);
            }
            TLRPC.Chat chat = this.currentChat;
            return chat != null ? -chat.id : this.currentUser.id;
        }
        throw new IllegalStateException("Unknown mode");
    }

    @Override // org.telegram.ui.ActionBar.BaseFragment
    public boolean onFragmentCreate() {
        super.onFragmentCreate();
        NotificationCenter.getGlobalInstance().addObserver(this, NotificationCenter.emojiLoaded);
        NotificationCenter.getInstance(this.currentAccount).addObserver(this, NotificationCenter.messagePlayingDidStart);
        NotificationCenter.getInstance(this.currentAccount).addObserver(this, NotificationCenter.messagePlayingPlayStateChanged);
        NotificationCenter.getInstance(this.currentAccount).addObserver(this, NotificationCenter.messagePlayingDidReset);
        NotificationCenter.getInstance(this.currentAccount).addObserver(this, NotificationCenter.messagePlayingProgressDidChanged);
        NotificationCenter.getGlobalInstance().addObserver(this, NotificationCenter.didSetNewWallpapper);
        loadMessages(true);
        return true;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void checkInsets() {
        this.chatListView.setPadding(0, AndroidUtilities.dp(4.0f), 0, (int) this.windowInsetsStateHolder.getAnimatedMaxBottomInset());
        updatePagedownButtonPosition();
    }

    private void fillMessagesCount() {
        if (this.mode != 2) {
            return;
        }
        Utilities.globalQueue.postRunnable(new Runnable() { // from class: com.radolyn.ayugram.ui.AyuMessageHistory$$ExternalSyntheticLambda6
            @Override // java.lang.Runnable
            public final void run() {
                this.f$0.lambda$fillMessagesCount$1();
            }
        });
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$fillMessagesCount$1() {
        final String pluralString = LocaleController.formatPluralString("messages", getAyuMessagesController().getDeletedCount(getDialogId(), this.topicId.longValue(), this.searchQuery), new Object[0]);
        AndroidUtilities.runOnUIThread(new Runnable() { // from class: com.radolyn.ayugram.ui.AyuMessageHistory$$ExternalSyntheticLambda10
            @Override // java.lang.Runnable
            public final void run() {
                this.f$0.lambda$fillMessagesCount$0(pluralString);
            }
        });
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$fillMessagesCount$0(String str) {
        ChatAvatarContainer chatAvatarContainer = this.avatarContainer;
        if (chatAvatarContainer != null) {
            chatAvatarContainer.setSubtitle(str);
        }
    }

    @Override // org.telegram.ui.ActionBar.BaseFragment
    public int getThemedColor(int i) {
        return Theme.getColor(i, this.theme);
    }

    @Override // org.telegram.ui.ActionBar.BaseFragment
    public int getNavigationBarColor() {
        return getThemedColor(Theme.key_chat_messagePanelBackground);
    }

    @Override // org.telegram.ui.ActionBar.BaseFragment
    public void onFragmentDestroy() {
        super.onFragmentDestroy();
        dimBehindView(null, false);
        NotificationCenter.getGlobalInstance().removeObserver(this, NotificationCenter.emojiLoaded);
        NotificationCenter.getInstance(this.currentAccount).removeObserver(this, NotificationCenter.messagePlayingDidStart);
        NotificationCenter.getInstance(this.currentAccount).removeObserver(this, NotificationCenter.messagePlayingPlayStateChanged);
        NotificationCenter.getInstance(this.currentAccount).removeObserver(this, NotificationCenter.messagePlayingDidReset);
        NotificationCenter.getInstance(this.currentAccount).removeObserver(this, NotificationCenter.messagePlayingProgressDidChanged);
        NotificationCenter.getGlobalInstance().removeObserver(this, NotificationCenter.didSetNewWallpapper);
        this.notificationsLocker.unlock();
        this.chatActivity = null;
    }

    private void updateEmptyPlaceholder() {
        TextView textView = this.emptyView;
        if (textView == null) {
            return;
        }
        textView.setPadding(AndroidUtilities.dp(8.0f), AndroidUtilities.dp(5.0f), AndroidUtilities.dp(8.0f), AndroidUtilities.dp(5.0f));
        this.emptyView.setText(AndroidUtilities.replaceTags(LocaleController.getString(R.string.NoResult)));
    }

    private MessageObject createMessageObjectForEdited(AyuMessageBase ayuMessageBase) {
        TLRPC.TL_message tL_message = new TLRPC.TL_message();
        getAyuMapper().map(ayuMessageBase, tL_message);
        getAyuMapper().mapMedia(ayuMessageBase, tL_message);
        tL_message.date = ayuMessageBase.entityCreateDate;
        tL_message.edit_hide = this.mode == 1;
        if (ayuMessageBase.documentType == 3 && Objects.equals(ayuMessageBase.mediaPath, FileLoader.getInstance(this.currentAccount).getPathToMessage(this.currentMessageObject.messageOwner).getAbsolutePath())) {
            tL_message.media.document = this.currentMessageObject.messageOwner.media.document;
        }
        TLRPC.Message message = this.currentMessageObject.messageOwner;
        TLRPC.MessageReplyHeader messageReplyHeader = message.reply_to;
        if (messageReplyHeader != null) {
            tL_message.replyMessage = message.replyMessage;
            tL_message.reply_to = messageReplyHeader;
        }
        int i = ayuMessageBase.messageId;
        tL_message.id = i;
        tL_message.local_id = i;
        MessageObject messageObject = new MessageObject(getCurrentAccount(), tL_message, false, true);
        messageObject.updateTranslation();
        return messageObject;
    }

    private ArrayList loadEdits(int i) {
        ArrayList arrayList = new ArrayList();
        for (EditedMessage editedMessage : getAyuMessagesController().getRevisions(getDialogId(), this.currentMessageObject.messageOwner.id, i, 25)) {
            MessageObject messageObjectCreateMessageObjectForEdited = createMessageObjectForEdited(editedMessage);
            if (messageObjectCreateMessageObjectForEdited.contentType >= 0) {
                arrayList.add(new AyuLoadedMessage(messageObjectCreateMessageObjectForEdited, editedMessage.fakeId, editedMessage.entityCreateDate));
            }
        }
        return arrayList;
    }

    private ArrayList loadDeleted(int i) {
        final MessagesController messagesController = getMessagesController();
        MessagesStorage messagesStorage = getMessagesStorage();
        List<DeletedMessageFull> messagesForScroll = getAyuMessagesController().getMessagesForScroll(getDialogId(), this.topicId.longValue(), this.searchQuery, i, 25);
        if (messagesForScroll.isEmpty()) {
            return new ArrayList();
        }
        LongSparseArray longSparseArray = new LongSparseArray();
        ArrayList arrayList = new ArrayList();
        ArrayList arrayList2 = new ArrayList();
        for (DeletedMessageFull deletedMessageFull : messagesForScroll) {
            if (!AyuHistoryHook.isEmpty(deletedMessageFull.message)) {
                TLRPC.TL_message map = AyuHistoryHook.map(deletedMessageFull, this.currentAccount);
                MessagesStorage.addUsersAndChatsFromMessage(map, arrayList, arrayList2, null);
                longSparseArray.put(deletedMessageFull.message.messageId, map);
            }
        }
        AyuHistoryHook.QuadroResult entities = AyuHistoryHook.getEntities(messagesStorage, arrayList, arrayList2);
        Pair dicts = entities.getDicts();
        final ArrayList users = entities.getUsers();
        final ArrayList chats = entities.getChats();
        AndroidUtilities.runOnUIThread(new Runnable() { // from class: com.radolyn.ayugram.ui.AyuMessageHistory$$ExternalSyntheticLambda11
            @Override // java.lang.Runnable
            public final void run() {
                AyuMessageHistory.$r8$lambda$LNyo2l6S9XtUtn4o6yomJs_7b0I(users, messagesController, chats);
            }
        });
        ArrayList arrayList3 = new ArrayList();
        ArrayList arrayList4 = new ArrayList();
        for (int i2 = 0; i2 < longSparseArray.size(); i2++) {
            long jKeyAt = longSparseArray.keyAt(i2);
            MessageObject messageObject = new MessageObject(this.currentAccount, (TLRPC.Message) longSparseArray.get(jKeyAt), (LongSparseArray) dicts.first, (LongSparseArray) dicts.second, false, true);
            messageObject.updateTranslation();
            arrayList3.add(new AyuLoadedMessage(messageObject, jKeyAt, messageObject.messageOwner.date));
            arrayList4.add(messageObject);
        }
        Collections.sort(arrayList3, new Comparator() { // from class: com.radolyn.ayugram.ui.AyuMessageHistory$$ExternalSyntheticLambda12
            @Override // java.util.Comparator
            public final int compare(Object obj, Object obj2) {
                return Long.compare(((AyuMessageHistory.AyuLoadedMessage) obj2).key, ((AyuMessageHistory.AyuLoadedMessage) obj).key);
            }
        });
        AyuHistoryHook.fixReplies(this.currentAccount, arrayList4, null, getAyuMessagesController(), entities.getDicts(), getMessagesStorage());
        return arrayList3;
    }

    public static /* synthetic */ void $r8$lambda$LNyo2l6S9XtUtn4o6yomJs_7b0I(ArrayList arrayList, MessagesController messagesController, ArrayList arrayList2) {
        if (!arrayList.isEmpty()) {
            messagesController.putUsers(arrayList, true);
        }
        if (arrayList2.isEmpty()) {
            return;
        }
        messagesController.putChats(arrayList2, true);
    }

    private String getDateKey(AyuLoadedMessage ayuLoadedMessage) {
        GregorianCalendar gregorianCalendar = new GregorianCalendar();
        gregorianCalendar.setTimeInMillis(((long) ayuLoadedMessage.dateKey) * 1000);
        int i = gregorianCalendar.get(6);
        return String.format("%d_%02d_%02d", Integer.valueOf(gregorianCalendar.get(1)), Integer.valueOf(gregorianCalendar.get(2)), Integer.valueOf(i));
    }

    private String getDateKey(MessageObject messageObject) {
        GregorianCalendar gregorianCalendar = new GregorianCalendar();
        gregorianCalendar.setTimeInMillis(((long) messageObject.messageOwner.date) * 1000);
        int i = gregorianCalendar.get(6);
        return String.format("%d_%02d_%02d", Integer.valueOf(gregorianCalendar.get(1)), Integer.valueOf(gregorianCalendar.get(2)), Integer.valueOf(i));
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void loadMessages(boolean z) {
        ChatActivityAdapter chatActivityAdapter;
        if (this.loading) {
            return;
        }
        this.loading = true;
        if (z) {
            this.minEventId = Long.MAX_VALUE;
            FrameLayout frameLayout = this.progressView;
            if (frameLayout != null) {
                AndroidUtilities.updateViewVisibilityAnimated(frameLayout, true, 0.3f, true);
                this.emptyViewContainer.setVisibility(4);
                this.chatListView.setEmptyView(null);
            }
            this.messagesDict.clear();
            this.messages.clear();
            this.messagesByDays.clear();
            fillMessagesCount();
        }
        updateEmptyPlaceholder();
        if (z && (chatActivityAdapter = this.chatAdapter) != null) {
            chatActivityAdapter.notifyDataSetChanged();
        }
        final int i = (z || this.messages.isEmpty()) ? Integer.MAX_VALUE : (int) this.minEventId;
        ChatUtils.utilsQueue.postRunnable(new Runnable() { // from class: com.radolyn.ayugram.ui.AyuMessageHistory$$ExternalSyntheticLambda4
            @Override // java.lang.Runnable
            public final void run() {
                this.f$0.lambda$loadMessages$6(i);
            }
        });
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$loadMessages$6(int i) {
        final ArrayList arrayListLoadEdits = this.mode == 1 ? loadEdits(i) : loadDeleted(i);
        AndroidUtilities.runOnUIThread(new Runnable() { // from class: com.radolyn.ayugram.ui.AyuMessageHistory$$ExternalSyntheticLambda8
            @Override // java.lang.Runnable
            public final void run() {
                this.f$0.lambda$loadMessages$5(arrayListLoadEdits);
            }
        });
    }

    /* JADX INFO: Access modifiers changed from: private */
    /* JADX WARN: Code duplicated, block: B:19:0x00de  */
    public /* synthetic */ void lambda$loadMessages$5(ArrayList arrayList) {
        int i;
        TLRPC.Message message;
        TLRPC.MessageReplyHeader messageReplyHeader;
        MessageObject messageObject;
        this.chatListItemAnimator.setShouldAnimateEnterFromBottom(false);
        int size = this.messages.size();
        int size2 = arrayList.size();
        boolean z = false;
        int i2 = 0;
        while (i2 < size2) {
            Object obj = arrayList.get(i2);
            i2++;
            AyuLoadedMessage ayuLoadedMessage = (AyuLoadedMessage) obj;
            if (this.messagesDict.indexOfKey(ayuLoadedMessage.key) < 0) {
                this.minEventId = Math.min(this.minEventId, ayuLoadedMessage.key);
                if (ayuLoadedMessage.messageObject.contentType >= 0) {
                    this.messagesDict.put(ayuLoadedMessage.key, ayuLoadedMessage.messageObject);
                    String dateKey = getDateKey(ayuLoadedMessage);
                    ArrayList arrayList2 = (ArrayList) this.messagesByDays.get(dateKey);
                    if (arrayList2 == null) {
                        arrayList2 = new ArrayList();
                        this.messagesByDays.put(dateKey, arrayList2);
                        TLRPC.TL_message tL_message = new TLRPC.TL_message();
                        tL_message.message = LocaleController.formatDateChat(ayuLoadedMessage.dateKey);
                        tL_message.id = 0;
                        tL_message.date = ayuLoadedMessage.dateKey;
                        MessageObject messageObject2 = new MessageObject(this.currentAccount, tL_message, false, false);
                        messageObject2.type = 10;
                        messageObject2.contentType = 1;
                        messageObject2.isDateObject = true;
                        this.messages.add(ayuLoadedMessage.messageObject);
                        this.messages.add(messageObject2);
                    } else if (this.messages.isEmpty()) {
                        this.messages.add(ayuLoadedMessage.messageObject);
                    } else {
                        ArrayList arrayList3 = this.messages;
                        if (((MessageObject) arrayList3.get(arrayList3.size() - 1)).isDateObject) {
                            ArrayList arrayList4 = this.messages;
                            MessageObject messageObject3 = (MessageObject) arrayList4.get(arrayList4.size() - 1);
                            ArrayList arrayList5 = this.messages;
                            arrayList5.remove(arrayList5.size() - 1);
                            this.messages.add(ayuLoadedMessage.messageObject);
                            this.messages.add(messageObject3);
                        } else {
                            this.messages.add(ayuLoadedMessage.messageObject);
                        }
                    }
                    arrayList2.add(ayuLoadedMessage.messageObject);
                }
                z = true;
            }
        }
        ArrayList<MessageObject> arrayList6 = new ArrayList<>();
        for (int i3 = size; i3 < this.messages.size(); i3++) {
            MessageObject messageObject4 = (MessageObject) this.messages.get(i3);
            if (messageObject4 != null && (message = messageObject4.messageOwner) != null && (messageReplyHeader = message.reply_to) != null) {
                if (messageReplyHeader.reply_to_peer_id == null) {
                    int i4 = 0;
                    while (true) {
                        if (i4 >= this.messages.size()) {
                            messageObject = null;
                            break;
                        }
                        if (i3 != i4) {
                            messageObject = (MessageObject) this.messages.get(i4);
                            if (messageObject.contentType != 1 && messageObject.getRealId() == messageReplyHeader.reply_to_msg_id) {
                                break;
                            }
                        }
                        i4++;
                    }
                    if (messageObject != null) {
                        messageObject4.replyMessageObject = messageObject;
                    }
                }
                arrayList6.add(messageObject4);
            }
        }
        if (!arrayList6.isEmpty()) {
            MediaDataController.getInstance(this.currentAccount).loadReplyMessagesForMessages(arrayList6, getDialogId(), 0, 0L, new Runnable() { // from class: com.radolyn.ayugram.ui.AyuMessageHistory$$ExternalSyntheticLambda9
                @Override // java.lang.Runnable
                public final void run() {
                    this.f$0.lambda$loadMessages$4();
                }
            }, getClassGuid(), null);
        }
        int size3 = this.messages.size() - size;
        this.loading = false;
        if (!z) {
            this.endReached = true;
        }
        AndroidUtilities.updateViewVisibilityAnimated(this.progressView, false, 0.3f, true);
        this.chatListView.setEmptyView(this.emptyViewContainer);
        if (size3 != 0) {
            if (this.endReached) {
                this.chatAdapter.notifyItemRangeChanged(0, 2);
                i = 1;
            } else {
                i = 0;
            }
            int iFindLastVisibleItemPosition = this.chatLayoutManager.findLastVisibleItemPosition();
            View viewFindViewByPosition = this.chatLayoutManager.findViewByPosition(iFindLastVisibleItemPosition);
            int top = (viewFindViewByPosition != null ? viewFindViewByPosition.getTop() : 0) - this.chatListView.getPaddingTop();
            if (size3 - i > 0) {
                int i5 = (i ^ 1) + 1;
                this.chatAdapter.notifyItemChanged(i5);
                this.chatAdapter.notifyItemRangeInserted(i5, size3 - i);
            }
            if (iFindLastVisibleItemPosition != -1) {
                this.chatLayoutManager.scrollToPositionWithOffset((iFindLastVisibleItemPosition + size3) - i, top);
                return;
            }
            return;
        }
        if (this.endReached) {
            this.chatAdapter.notifyItemRemoved(0);
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$loadMessages$4() {
        saveScrollPosition(false);
        this.chatAdapter.notifyDataSetChanged();
    }

    @Override // org.telegram.messenger.NotificationCenter.NotificationCenterDelegate
    public void didReceivedNotification(int i, int i2, Object... objArr) {
        ChatMessageCell chatMessageCell;
        MessageObject messageObject;
        ChatMessageCell chatMessageCell2;
        MessageObject messageObject2;
        ChatMessageCell chatMessageCell3;
        MessageObject messageObject3;
        if (i == NotificationCenter.emojiLoaded) {
            RecyclerListView recyclerListView = this.chatListView;
            if (recyclerListView != null) {
                recyclerListView.invalidateViews();
                return;
            }
            return;
        }
        if (i == NotificationCenter.messagePlayingDidStart) {
            if (((MessageObject) objArr[0]).isRoundVideo()) {
                MediaController.getInstance().setTextureView(createTextureView(true), this.aspectRatioFrameLayout, this.roundVideoContainer, true);
                updateTextureViewPosition();
            }
            RecyclerListView recyclerListView2 = this.chatListView;
            if (recyclerListView2 != null) {
                int childCount = recyclerListView2.getChildCount();
                for (int i3 = 0; i3 < childCount; i3++) {
                    View childAt = this.chatListView.getChildAt(i3);
                    if ((childAt instanceof ChatMessageCell) && (messageObject3 = (chatMessageCell3 = (ChatMessageCell) childAt).getMessageObject()) != null) {
                        if (messageObject3.isVoice() || messageObject3.isMusic()) {
                            chatMessageCell3.updateButtonState(false, true, false);
                        } else if (messageObject3.isRoundVideo()) {
                            chatMessageCell3.checkVideoPlayback(false, null);
                            if (!MediaController.getInstance().isPlayingMessage(messageObject3) && messageObject3.audioProgress != 0.0f) {
                                messageObject3.resetPlayingProgress();
                                chatMessageCell3.invalidate();
                            }
                        }
                    }
                }
                return;
            }
            return;
        }
        if (i == NotificationCenter.messagePlayingDidReset || i == NotificationCenter.messagePlayingPlayStateChanged) {
            RecyclerListView recyclerListView3 = this.chatListView;
            if (recyclerListView3 != null) {
                int childCount2 = recyclerListView3.getChildCount();
                for (int i4 = 0; i4 < childCount2; i4++) {
                    View childAt2 = this.chatListView.getChildAt(i4);
                    if ((childAt2 instanceof ChatMessageCell) && (messageObject = (chatMessageCell = (ChatMessageCell) childAt2).getMessageObject()) != null) {
                        if (messageObject.isVoice() || messageObject.isMusic()) {
                            chatMessageCell.updateButtonState(false, true, false);
                        } else if (messageObject.isRoundVideo() && !MediaController.getInstance().isPlayingMessage(messageObject)) {
                            chatMessageCell.checkVideoPlayback(true, null);
                        }
                    }
                }
                return;
            }
            return;
        }
        if (i == NotificationCenter.messagePlayingProgressDidChanged) {
            Integer num = (Integer) objArr[0];
            RecyclerListView recyclerListView4 = this.chatListView;
            if (recyclerListView4 != null) {
                int childCount3 = recyclerListView4.getChildCount();
                for (int i5 = 0; i5 < childCount3; i5++) {
                    View childAt3 = this.chatListView.getChildAt(i5);
                    if ((childAt3 instanceof ChatMessageCell) && (messageObject2 = (chatMessageCell2 = (ChatMessageCell) childAt3).getMessageObject()) != null && messageObject2.getId() == num.intValue()) {
                        MessageObject playingMessageObject = MediaController.getInstance().getPlayingMessageObject();
                        if (playingMessageObject != null) {
                            messageObject2.audioProgress = playingMessageObject.audioProgress;
                            messageObject2.audioProgressSec = playingMessageObject.audioProgressSec;
                            messageObject2.audioPlayerDuration = playingMessageObject.audioPlayerDuration;
                            chatMessageCell2.updatePlayingMessageProgress();
                            return;
                        }
                        return;
                    }
                }
                return;
            }
            return;
        }
        if (i != NotificationCenter.didSetNewWallpapper || this.fragmentView == null) {
            return;
        }
        this.contentView.setBackgroundImage(Theme.getCachedWallpaper(), Theme.isWallpaperMotion());
        this.progressView2.invalidate();
        TextView textView = this.emptyView;
        if (textView != null) {
            textView.invalidate();
        }
        this.chatListView.invalidateViews();
    }

    @Override // org.telegram.ui.ActionBar.BaseFragment
    public View createView(Context context) {
        Paint paint = new Paint();
        this.scrimPaint = paint;
        paint.setColor(-16777216);
        this.sharedResources = new ChatMessageSharedResources(context);
        if (this.chatMessageCellsCache.isEmpty()) {
            for (int i = 0; i < 8; i++) {
                this.chatMessageCellsCache.add(new ChatMessageCell(context, this.currentAccount));
            }
        }
        this.searchWas = false;
        this.hasOwnBackground = true;
        Theme.createChatResources(context, false);
        this.actionBar.setAddToContainer(false);
        this.actionBar.setOccupyStatusBar(!AndroidUtilities.isTablet());
        this.actionBar.setBackButtonDrawable(new BackDrawable(false));
        this.actionBar.setActionBarMenuOnItemClick(new ActionBar.ActionBarMenuOnItemClick() { // from class: com.radolyn.ayugram.ui.AyuMessageHistory.2
            @Override // org.telegram.ui.ActionBar.ActionBar.ActionBarMenuOnItemClick
            public void onItemClick(int i2) {
                if (i2 == -1) {
                    AyuMessageHistory.this.finishFragment();
                }
            }
        });
        ChatAvatarContainer chatAvatarContainer = new ChatAvatarContainer(context, null, false, this.theme) { // from class: com.radolyn.ayugram.ui.AyuMessageHistory.3
            @Override // org.telegram.ui.Components.ChatAvatarContainer
            protected boolean canSearch() {
                return TextUtils.isEmpty(AyuMessageHistory.this.searchQuery) && AyuMessageHistory.this.mode == 2;
            }

            @Override // org.telegram.ui.Components.ChatAvatarContainer
            protected void openSearch() {
                AyuMessageHistory.this.searchItem.openSearch(true);
                performHapticFeedback(0);
            }
        };
        this.avatarContainer = chatAvatarContainer;
        chatAvatarContainer.setOccupyStatusBar(!AndroidUtilities.isTablet());
        this.avatarContainer.setEnabled(false);
        this.actionBar.addView(this.avatarContainer, 0, LayoutHelper.createFrame(-2, -1.0f, 51, 56.0f, 0.0f, 40.0f, 0.0f));
        if (this.mode == 2) {
            ActionBarMenuItem actionBarMenuItemSearchListener = this.actionBar.createMenu().addItem(0, R.drawable.ic_ab_search).setIsSearchField(true).setActionBarMenuItemSearchListener(new ActionBarMenuItem.ActionBarMenuItemSearchListener() { // from class: com.radolyn.ayugram.ui.AyuMessageHistory.4
                @Override // org.telegram.ui.ActionBar.ActionBarMenuItem.ActionBarMenuItemSearchListener
                public void onSearchCollapse() {
                    AyuMessageHistory.this.searchQuery = _UrlKt.FRAGMENT_ENCODE_SET;
                    AyuMessageHistory.this.avatarContainer.setVisibility(0);
                    if (AyuMessageHistory.this.searchWas) {
                        AyuMessageHistory.this.searchWas = false;
                        AyuMessageHistory.this.loadMessages(true);
                    }
                }

                @Override // org.telegram.ui.ActionBar.ActionBarMenuItem.ActionBarMenuItemSearchListener
                public void onSearchExpand() {
                    AyuMessageHistory.this.avatarContainer.setVisibility(8);
                }

                @Override // org.telegram.ui.ActionBar.ActionBarMenuItem.ActionBarMenuItemSearchListener
                public void onSearchPressed(EditText editText) {
                    AyuMessageHistory.this.searchWas = true;
                    AyuMessageHistory.this.searchQuery = editText.getText().toString();
                    AyuMessageHistory.this.loadMessages(true);
                }
            });
            this.searchItem = actionBarMenuItemSearchListener;
            actionBarMenuItemSearchListener.setSearchFieldHint(LocaleController.getString(R.string.Search));
        }
        TLRPC.Chat chat = this.currentChat;
        if (chat != null) {
            this.avatarContainer.setTitle(chat.title);
            int i2 = this.mode;
            if (i2 == 1) {
                String name = ContactsController.formatName(getMessagesController().getUserOrChat(this.currentMessageObject.getSenderId()));
                if (Objects.equals(name, this.currentChat.title)) {
                    if (!TextUtils.isEmpty(this.currentMessageObject.messageOwner.post_author)) {
                        name = this.currentMessageObject.messageOwner.post_author;
                    } else {
                        name = LocaleController.formatDateTime(this.currentMessageObject.messageOwner.date, true);
                    }
                }
                this.avatarContainer.setSubtitle(name + " (" + this.currentMessageObject.getId() + ")");
            } else if (i2 == 2) {
                fillMessagesCount();
            }
            this.avatarContainer.setChatAvatar(this.currentChat);
        } else {
            TLRPC.User user = this.currentUser;
            if (user != null) {
                this.avatarContainer.setTitle(ContactsController.formatName(user));
                int i3 = this.mode;
                if (i3 == 1) {
                    this.avatarContainer.setSubtitle(LocaleController.formatDateTime(this.currentMessageObject.messageOwner.date, true));
                } else if (i3 == 2) {
                    fillMessagesCount();
                }
                this.avatarContainer.setUserAvatar(this.currentUser);
            }
        }
        SizeNotifierFrameLayout sizeNotifierFrameLayout = new SizeNotifierFrameLayout(context) { // from class: com.radolyn.ayugram.ui.AyuMessageHistory.5
            @Override // org.telegram.ui.Components.SizeNotifierFrameLayout, android.view.ViewGroup, android.view.View
            protected void onAttachedToWindow() {
                super.onAttachedToWindow();
                MessageObject playingMessageObject = MediaController.getInstance().getPlayingMessageObject();
                if (playingMessageObject == null || !playingMessageObject.isRoundVideo() || playingMessageObject.eventId == 0 || playingMessageObject.getDialogId() != AyuMessageHistory.this.getDialogId()) {
                    return;
                }
                MediaController.getInstance().setTextureView(AyuMessageHistory.this.createTextureView(false), AyuMessageHistory.this.aspectRatioFrameLayout, AyuMessageHistory.this.roundVideoContainer, true);
            }

            @Override // android.view.ViewGroup
            protected boolean drawChild(Canvas canvas, View view, long j) {
                boolean zDrawChild = super.drawChild(canvas, view, j);
                if (view == ((BaseFragment) AyuMessageHistory.this).actionBar && ((BaseFragment) AyuMessageHistory.this).parentLayout != null) {
                    ((BaseFragment) AyuMessageHistory.this).parentLayout.drawHeaderShadow(canvas, ((BaseFragment) AyuMessageHistory.this).actionBar.getVisibility() == 0 ? ((BaseFragment) AyuMessageHistory.this).actionBar.getMeasuredHeight() : 0);
                }
                return zDrawChild;
            }

            @Override // org.telegram.ui.Components.SizeNotifierFrameLayout
            protected boolean isActionBarVisible() {
                return ((BaseFragment) AyuMessageHistory.this).actionBar.getVisibility() == 0;
            }

            @Override // org.telegram.ui.Components.SizeNotifierFrameLayout
            protected void drawList(Canvas canvas, boolean z, ArrayList arrayList) {
                if (this.backgroundView != null) {
                    int iSave = canvas.save();
                    canvas.translate(this.backgroundView.getX(), this.backgroundView.getY());
                    this.backgroundView.draw(canvas);
                    canvas.restoreToCount(iSave);
                }
                for (int i4 = 0; i4 < AyuMessageHistory.this.chatListView.getChildCount(); i4++) {
                    View childAt = AyuMessageHistory.this.chatListView.getChildAt(i4);
                    if (childAt.getY() < AndroidUtilities.dp(100.0f) && childAt.getVisibility() == 0) {
                        int iSave2 = canvas.save();
                        canvas.translate(AyuMessageHistory.this.chatListView.getX() + childAt.getX(), getY() + AyuMessageHistory.this.chatListView.getY() + childAt.getY());
                        childAt.draw(canvas);
                        canvas.restoreToCount(iSave2);
                    }
                }
            }

            @Override // android.widget.FrameLayout, android.view.View
            protected void onMeasure(int i4, int i5) {
                int size = View.MeasureSpec.getSize(i4);
                int size2 = View.MeasureSpec.getSize(i5);
                setMeasuredDimension(size, size2);
                int paddingTop = size2 - getPaddingTop();
                measureChildWithMargins(((BaseFragment) AyuMessageHistory.this).actionBar, i4, 0, i5, 0);
                int measuredHeight = ((BaseFragment) AyuMessageHistory.this).actionBar.getMeasuredHeight();
                if (((BaseFragment) AyuMessageHistory.this).actionBar.getVisibility() == 0) {
                    paddingTop -= measuredHeight;
                }
                int childCount = getChildCount();
                for (int i6 = 0; i6 < childCount; i6++) {
                    View childAt = getChildAt(i6);
                    if (childAt != null && childAt.getVisibility() != 8 && childAt != ((BaseFragment) AyuMessageHistory.this).actionBar) {
                        if (childAt == AyuMessageHistory.this.chatListView || childAt == AyuMessageHistory.this.progressView) {
                            childAt.measure(View.MeasureSpec.makeMeasureSpec(size, TLObject.FLAG_30), View.MeasureSpec.makeMeasureSpec(Math.max(AndroidUtilities.dp(10.0f), paddingTop), TLObject.FLAG_30));
                        } else if (childAt == AyuMessageHistory.this.emptyViewContainer) {
                            childAt.measure(View.MeasureSpec.makeMeasureSpec(size, TLObject.FLAG_30), View.MeasureSpec.makeMeasureSpec(paddingTop, TLObject.FLAG_30));
                        } else {
                            measureChildWithMargins(childAt, i4, 0, i5, 0);
                        }
                    }
                }
            }

            /* JADX WARN: Code duplicated, block: B:20:0x004d  */
            /* JADX WARN: Code duplicated, block: B:22:0x0051  */
            /* JADX WARN: Code duplicated, block: B:24:0x0055  */
            /* JADX WARN: Code duplicated, block: B:25:0x0058  */
            /* JADX WARN: Code duplicated, block: B:27:0x0060  */
            /* JADX WARN: Code duplicated, block: B:32:0x0087  */
            /* JADX WARN: Code duplicated, block: B:35:0x009a  */
            /* JADX WARN: Code duplicated, block: B:37:0x00ac  */
            /* JADX WARN: Code duplicated, block: B:38:0x00b9  */
            /* JADX WARN: Code duplicated, block: B:41:0x00bd  */
            /* JADX WARN: Code duplicated, block: B:43:0x00c5  */
            /* JADX WARN: Code duplicated, block: B:44:0x00ca  */
            /* JADX WARN: Code duplicated, block: B:46:0x00ce  */
            @Override // org.telegram.ui.Components.SizeNotifierFrameLayout, android.widget.FrameLayout, android.view.ViewGroup, android.view.View
            protected void onLayout(boolean z, int i4, int i5, int i6, int i7) {
                int i8;
                int i9;
                int i10;
                int i11;
                int i12;
                int paddingTop;
                int paddingTop2;
                int measuredHeight;
                int childCount = getChildCount();
                for (int i13 = 0; i13 < childCount; i13++) {
                    View childAt = getChildAt(i13);
                    if (childAt.getVisibility() != 8) {
                        FrameLayout.LayoutParams layoutParams = (FrameLayout.LayoutParams) childAt.getLayoutParams();
                        int measuredWidth = childAt.getMeasuredWidth();
                        int measuredHeight2 = childAt.getMeasuredHeight();
                        int i14 = layoutParams.gravity;
                        if (i14 == -1) {
                            i14 = 51;
                        }
                        int i15 = i14 & 112;
                        int i16 = i14 & 7;
                        if (i16 == 1) {
                            i8 = (((i6 - i4) - measuredWidth) / 2) + layoutParams.leftMargin;
                            i9 = layoutParams.rightMargin;
                        } else {
                            if (i16 == 5) {
                                i8 = i6 - measuredWidth;
                                i9 = layoutParams.rightMargin;
                            } else {
                                i10 = layoutParams.leftMargin;
                            }
                            if (i15 != 16) {
                                i11 = (((i7 - i5) - measuredHeight2) / 2) + layoutParams.topMargin;
                                i12 = layoutParams.bottomMargin;
                            } else {
                                if (i15 != 48) {
                                    paddingTop = layoutParams.topMargin + getPaddingTop();
                                    if (childAt != ((BaseFragment) AyuMessageHistory.this).actionBar && ((BaseFragment) AyuMessageHistory.this).actionBar.getVisibility() == 0) {
                                        paddingTop += ((BaseFragment) AyuMessageHistory.this).actionBar.getMeasuredHeight();
                                    }
                                } else if (i15 != 80) {
                                    i11 = (i7 - i5) - measuredHeight2;
                                    i12 = layoutParams.bottomMargin;
                                } else {
                                    paddingTop = layoutParams.topMargin;
                                }
                                if (childAt == AyuMessageHistory.this.emptyViewContainer) {
                                    if (childAt == ((BaseFragment) AyuMessageHistory.this).actionBar) {
                                        paddingTop2 = getPaddingTop();
                                    } else if (childAt == this.backgroundView) {
                                        paddingTop = 0;
                                    }
                                    childAt.layout(i10, paddingTop, measuredWidth + i10, measuredHeight2 + paddingTop);
                                } else {
                                    int iDp = AndroidUtilities.dp(24.0f);
                                    if (((BaseFragment) AyuMessageHistory.this).actionBar.getVisibility() == 0) {
                                        measuredHeight = ((BaseFragment) AyuMessageHistory.this).actionBar.getMeasuredHeight() / 2;
                                    } else {
                                        measuredHeight = 0;
                                    }
                                    paddingTop2 = iDp - measuredHeight;
                                }
                                paddingTop -= paddingTop2;
                                childAt.layout(i10, paddingTop, measuredWidth + i10, measuredHeight2 + paddingTop);
                            }
                            paddingTop = i11 - i12;
                            if (childAt == AyuMessageHistory.this.emptyViewContainer) {
                                if (childAt == ((BaseFragment) AyuMessageHistory.this).actionBar) {
                                    paddingTop2 = getPaddingTop();
                                } else if (childAt == this.backgroundView) {
                                    paddingTop = 0;
                                }
                                childAt.layout(i10, paddingTop, measuredWidth + i10, measuredHeight2 + paddingTop);
                            } else {
                                int iDp2 = AndroidUtilities.dp(24.0f);
                                if (((BaseFragment) AyuMessageHistory.this).actionBar.getVisibility() == 0) {
                                    measuredHeight = ((BaseFragment) AyuMessageHistory.this).actionBar.getMeasuredHeight() / 2;
                                } else {
                                    measuredHeight = 0;
                                }
                                paddingTop2 = iDp2 - measuredHeight;
                            }
                            paddingTop -= paddingTop2;
                            childAt.layout(i10, paddingTop, measuredWidth + i10, measuredHeight2 + paddingTop);
                        }
                        i10 = i8 - i9;
                        if (i15 != 16) {
                            i11 = (((i7 - i5) - measuredHeight2) / 2) + layoutParams.topMargin;
                            i12 = layoutParams.bottomMargin;
                        } else {
                            if (i15 != 48) {
                                paddingTop = layoutParams.topMargin + getPaddingTop();
                                if (childAt != ((BaseFragment) AyuMessageHistory.this).actionBar) {
                                    paddingTop += ((BaseFragment) AyuMessageHistory.this).actionBar.getMeasuredHeight();
                                }
                            } else if (i15 != 80) {
                                i11 = (i7 - i5) - measuredHeight2;
                                i12 = layoutParams.bottomMargin;
                            } else {
                                paddingTop = layoutParams.topMargin;
                            }
                            if (childAt == AyuMessageHistory.this.emptyViewContainer) {
                                if (childAt == ((BaseFragment) AyuMessageHistory.this).actionBar) {
                                    paddingTop2 = getPaddingTop();
                                } else if (childAt == this.backgroundView) {
                                    paddingTop = 0;
                                }
                                childAt.layout(i10, paddingTop, measuredWidth + i10, measuredHeight2 + paddingTop);
                            } else {
                                int iDp3 = AndroidUtilities.dp(24.0f);
                                if (((BaseFragment) AyuMessageHistory.this).actionBar.getVisibility() == 0) {
                                    measuredHeight = ((BaseFragment) AyuMessageHistory.this).actionBar.getMeasuredHeight() / 2;
                                } else {
                                    measuredHeight = 0;
                                }
                                paddingTop2 = iDp3 - measuredHeight;
                            }
                            paddingTop -= paddingTop2;
                            childAt.layout(i10, paddingTop, measuredWidth + i10, measuredHeight2 + paddingTop);
                        }
                        paddingTop = i11 - i12;
                        if (childAt == AyuMessageHistory.this.emptyViewContainer) {
                            if (childAt == ((BaseFragment) AyuMessageHistory.this).actionBar) {
                                paddingTop2 = getPaddingTop();
                            } else if (childAt == this.backgroundView) {
                                paddingTop = 0;
                            }
                            childAt.layout(i10, paddingTop, measuredWidth + i10, measuredHeight2 + paddingTop);
                        } else {
                            int iDp4 = AndroidUtilities.dp(24.0f);
                            if (((BaseFragment) AyuMessageHistory.this).actionBar.getVisibility() == 0) {
                                measuredHeight = ((BaseFragment) AyuMessageHistory.this).actionBar.getMeasuredHeight() / 2;
                            } else {
                                measuredHeight = 0;
                            }
                            paddingTop2 = iDp4 - measuredHeight;
                        }
                        paddingTop -= paddingTop2;
                        childAt.layout(i10, paddingTop, measuredWidth + i10, measuredHeight2 + paddingTop);
                    }
                }
                AyuMessageHistory.this.updateMessagesVisiblePart();
                notifyHeightChanged();
            }

            @Override // android.view.ViewGroup, android.view.View
            public boolean dispatchTouchEvent(MotionEvent motionEvent) {
                if (AvatarPreviewer.hasVisibleInstance()) {
                    AvatarPreviewer.getInstance().onTouchEvent(motionEvent);
                    return true;
                }
                return super.dispatchTouchEvent(motionEvent);
            }

            @Override // org.telegram.ui.Components.SizeNotifierFrameLayout
            protected Theme.ResourcesProvider getResourceProvider() {
                return AyuMessageHistory.this.theme;
            }

            @Override // org.telegram.ui.Components.SizeNotifierFrameLayout
            protected Drawable getNewDrawable() {
                Drawable wallpaperDrawable = AyuMessageHistory.this.theme.getWallpaperDrawable();
                return wallpaperDrawable != null ? wallpaperDrawable : super.getNewDrawable();
            }

            @Override // org.telegram.ui.Components.SizeNotifierFrameLayout, android.view.ViewGroup, android.view.View
            protected void dispatchDraw(Canvas canvas) {
                super.dispatchDraw(canvas);
                if (AyuMessageHistory.this.scrimPaintAlpha > 0.0f) {
                    AyuMessageHistory.this.scrimPaint.setAlpha((int) (AyuMessageHistory.this.scrimPaintAlpha * 255.0f));
                    canvas.drawRect(0.0f, 0.0f, getMeasuredWidth(), getMeasuredHeight(), AyuMessageHistory.this.scrimPaint);
                    if (AyuMessageHistory.this.scrimView != null) {
                        int childCount = AyuMessageHistory.this.chatListView.getChildCount();
                        for (int i4 = 0; i4 < childCount; i4++) {
                            View childAt = AyuMessageHistory.this.chatListView.getChildAt(i4);
                            if (childAt == AyuMessageHistory.this.scrimView) {
                                int iSave = canvas.save();
                                canvas.clipRect(AyuMessageHistory.this.chatListView.getLeft(), AyuMessageHistory.this.chatListView.getTop(), AyuMessageHistory.this.chatListView.getRight(), AyuMessageHistory.this.chatListView.getBottom());
                                canvas.translate(AyuMessageHistory.this.chatListView.getX() + childAt.getX(), AyuMessageHistory.this.chatListView.getY() + childAt.getY());
                                childAt.draw(canvas);
                                canvas.restoreToCount(iSave);
                                break;
                            }
                        }
                    }
                    if (AyuMessageHistory.this.searchButtonLayout == null || AyuMessageHistory.this.searchButtonLayout.getAlpha() <= 0.0f) {
                        return;
                    }
                    int iSaveLayerAlpha = canvas.saveLayerAlpha(AyuMessageHistory.this.searchButtonLayout.getX(), AyuMessageHistory.this.searchButtonLayout.getY(), AyuMessageHistory.this.searchButtonLayout.getX() + AyuMessageHistory.this.searchButtonLayout.getWidth(), AyuMessageHistory.this.searchButtonLayout.getY() + AyuMessageHistory.this.searchButtonLayout.getHeight(), (int) (AyuMessageHistory.this.searchButtonLayout.getAlpha() * 255.0f), 31);
                    canvas.translate(AyuMessageHistory.this.searchButtonLayout.getX(), AyuMessageHistory.this.searchButtonLayout.getY());
                    AyuMessageHistory.this.searchButtonLayout.draw(canvas);
                    canvas.restoreToCount(iSaveLayerAlpha);
                }
            }
        };
        this.fragmentView = sizeNotifierFrameLayout;
        SizeNotifierFrameLayout sizeNotifierFrameLayout2 = sizeNotifierFrameLayout;
        this.contentView = sizeNotifierFrameLayout2;
        sizeNotifierFrameLayout2.needBlur = true;
        this.actionBar.setBackgroundColor(getThemedColor(Theme.key_actionBarDefault));
        this.actionBar.setDrawBlurBackground(this.contentView);
        this.contentView.setOccupyStatusBar(!AndroidUtilities.isTablet());
        this.contentView.setBackgroundImage(this.theme.getWallpaperDrawable(), this.theme.isWallpaperMotion());
        FrameLayout frameLayout = new FrameLayout(context);
        this.emptyViewContainer = frameLayout;
        frameLayout.setVisibility(4);
        this.contentView.addView(this.emptyViewContainer, LayoutHelper.createFrame(-1, -2, 17));
        this.emptyViewContainer.setOnTouchListener(new View.OnTouchListener() { // from class: com.radolyn.ayugram.ui.AyuMessageHistory$$ExternalSyntheticLambda1
            @Override // android.view.View.OnTouchListener
            public final boolean onTouch(View view, MotionEvent motionEvent) {
                return AyuMessageHistory.$r8$lambda$jLYav_im1uXVZODKnGhEINd9Csg(view, motionEvent);
            }
        });
        TextView textView = new TextView(context);
        this.emptyView = textView;
        textView.setTextSize(1, 14.0f);
        this.emptyView.setGravity(17);
        TextView textView2 = this.emptyView;
        int i4 = Theme.key_chat_serviceText;
        textView2.setTextColor(getThemedColor(i4));
        this.emptyView.setBackground(Theme.createServiceDrawable(AndroidUtilities.dp(6.0f), this.emptyView, this.contentView));
        this.emptyView.setPadding(AndroidUtilities.dp(16.0f), AndroidUtilities.dp(16.0f), AndroidUtilities.dp(16.0f), AndroidUtilities.dp(16.0f));
        this.emptyViewContainer.addView(this.emptyView, LayoutHelper.createFrame(-2, -2.0f, 17, 16.0f, 0.0f, 16.0f, 0.0f));
        RecyclerListView recyclerListView = new RecyclerListView(context, this.theme) { // from class: com.radolyn.ayugram.ui.AyuMessageHistory.6
            @Override // org.telegram.ui.Components.RecyclerListView, androidx.recyclerview.widget.RecyclerView, android.view.ViewGroup, android.view.View
            protected void onLayout(boolean z, int i5, int i6, int i7, int i8) {
                AyuMessageHistory.this.applyScrolledPosition();
                super.onLayout(z, i5, i6, i7, i8);
            }

            @Override // org.telegram.ui.Components.RecyclerListView, android.view.View
            public void setTranslationY(float f) {
                if (f != getTranslationY()) {
                    super.setTranslationY(f);
                    AyuMessageHistory.this.updateMessagesVisiblePart();
                }
            }

            /* JADX WARN: Code duplicated, block: B:7:0x0026  */
            /* JADX WARN: Code duplicated, block: B:9:0x002a  */
            @Override // org.telegram.ui.Components.RecyclerListView, androidx.recyclerview.widget.RecyclerView, android.view.ViewGroup
            public boolean drawChild(Canvas canvas, View view, long j) {
                ChatMessageCell chatMessageCell;
                ImageReceiver avatarImage;
                int y;
                int adapterPosition;
                boolean zDrawChild = super.drawChild(canvas, view, j);
                boolean z = view instanceof ChatMessageCell;
                if (z) {
                    ChatMessageCell chatMessageCell2 = (ChatMessageCell) view;
                    if (chatMessageCell2.hasOutboundsContent()) {
                        canvas.save();
                        canvas.translate(chatMessageCell2.getX(), chatMessageCell2.getY());
                        chatMessageCell2.drawOutboundsContent(canvas);
                        canvas.restore();
                    } else if (view instanceof ChatActionCell) {
                        ChatActionCell chatActionCell = (ChatActionCell) view;
                        canvas.save();
                        canvas.translate(chatActionCell.getX(), chatActionCell.getY());
                        chatActionCell.drawOutboundsContent(canvas);
                        canvas.restore();
                    }
                } else if (view instanceof ChatActionCell) {
                    ChatActionCell chatActionCell2 = (ChatActionCell) view;
                    canvas.save();
                    canvas.translate(chatActionCell2.getX(), chatActionCell2.getY());
                    chatActionCell2.drawOutboundsContent(canvas);
                    canvas.restore();
                }
                if (z) {
                    ChatMessageCell chatMessageCell3 = (ChatMessageCell) view;
                    if (chatMessageCell3.getTransitionParams().animateBackgroundBoundsInner) {
                        canvas.save();
                        canvas.translate(chatMessageCell3.getLeft(), chatMessageCell3.getY());
                        chatMessageCell3.drawTime(canvas, chatMessageCell3.shouldDrawAlphaLayer() ? chatMessageCell3.getAlpha() : 1.0f, true);
                        canvas.restore();
                    }
                }
                if (z && (avatarImage = (chatMessageCell = (ChatMessageCell) view).getAvatarImage()) != null) {
                    if (chatMessageCell.getMessageObject().deleted) {
                        avatarImage.setVisible(false, false);
                        return zDrawChild;
                    }
                    int y2 = (int) view.getY();
                    if (chatMessageCell.drawPinnedBottom() && (adapterPosition = AyuMessageHistory.this.chatListView.getChildViewHolder(view).getAdapterPosition()) >= 0) {
                        if (AyuMessageHistory.this.chatListView.findViewHolderForAdapterPosition(adapterPosition + 1) != null) {
                            avatarImage.setVisible(false, false);
                            return zDrawChild;
                        }
                    }
                    float slidingOffsetX = chatMessageCell.getSlidingOffsetX() + chatMessageCell.getCheckBoxTranslation();
                    int y3 = ((int) view.getY()) + chatMessageCell.getLayoutHeight();
                    int measuredHeight = AyuMessageHistory.this.chatListView.getMeasuredHeight() - AyuMessageHistory.this.chatListView.getPaddingBottom();
                    if (y3 > measuredHeight) {
                        y3 = measuredHeight;
                    }
                    if (chatMessageCell.drawPinnedTop() && (adapterPosition = AyuMessageHistory.this.chatListView.getChildViewHolder(view).getAdapterPosition()) >= 0) {
                        int i5 = 0;
                        while (i5 < 20) {
                            i5++;
                            int adapterPosition2 = adapterPosition2 - 1;
                            RecyclerView.ViewHolder viewHolderFindViewHolderForAdapterPosition = AyuMessageHistory.this.chatListView.findViewHolderForAdapterPosition(adapterPosition2);
                            if (viewHolderFindViewHolderForAdapterPosition == null) {
                                break;
                            }
                            y2 = viewHolderFindViewHolderForAdapterPosition.itemView.getTop();
                            View view2 = viewHolderFindViewHolderForAdapterPosition.itemView;
                            if (!(view2 instanceof ChatMessageCell)) {
                                break;
                            }
                            chatMessageCell = (ChatMessageCell) view2;
                            if (!chatMessageCell.drawPinnedTop()) {
                                break;
                            }
                        }
                    }
                    if (y3 - AndroidUtilities.dp(48.0f) < y2) {
                        y3 = y2 + AndroidUtilities.dp(48.0f);
                    }
                    if (!chatMessageCell.drawPinnedBottom() && y3 > (y = (int) (chatMessageCell.getY() + chatMessageCell.getMeasuredHeight()))) {
                        y3 = y;
                    }
                    canvas.save();
                    if (slidingOffsetX != 0.0f) {
                        canvas.translate(slidingOffsetX, 0.0f);
                    }
                    if (chatMessageCell.getCurrentMessagesGroup() != null && chatMessageCell.getCurrentMessagesGroup().transitionParams.backgroundChangeBounds) {
                        y3 = (int) (y3 - chatMessageCell.getTranslationY());
                    }
                    avatarImage.setImageY(y3 - AndroidUtilities.dp(44.0f));
                    if (chatMessageCell.shouldDrawAlphaLayer()) {
                        avatarImage.setAlpha(chatMessageCell.getAlpha());
                        canvas.scale(chatMessageCell.getScaleX(), chatMessageCell.getScaleY(), chatMessageCell.getX() + chatMessageCell.getPivotX(), chatMessageCell.getY() + (chatMessageCell.getHeight() >> 1));
                    } else {
                        avatarImage.setAlpha(1.0f);
                    }
                    avatarImage.setVisible(true, false);
                    avatarImage.draw(canvas);
                    canvas.restore();
                }
                return zDrawChild;
            }
        };
        this.chatListView = recyclerListView;
        recyclerListView.setOnItemClickListener(new RecyclerListView.OnItemClickListenerExtended() { // from class: com.radolyn.ayugram.ui.AyuMessageHistory$$ExternalSyntheticLambda2
            @Override // org.telegram.ui.Components.RecyclerListView.OnItemClickListenerExtended
            public /* synthetic */ boolean hasDoubleTap(View view, int i5) {
                return RecyclerListView.OnItemClickListenerExtended.CC.$default$hasDoubleTap(this, view, i5);
            }

            @Override // org.telegram.ui.Components.RecyclerListView.OnItemClickListenerExtended
            public /* synthetic */ void onDoubleTap(View view, int i5, float f, float f2) {
                RecyclerListView.OnItemClickListenerExtended.CC.$default$onDoubleTap(this, view, i5, f, f2);
            }

            @Override // org.telegram.ui.Components.RecyclerListView.OnItemClickListenerExtended
            public final void onItemClick(View view, int i5, float f, float f2) {
                this.f$0.lambda$createView$8(view, i5, f, f2);
            }
        });
        this.chatListView.setTag(1);
        this.chatListView.setVerticalScrollBarEnabled(!SharedConfig.chatBlurEnabled());
        this.chatListView.setScrollBarStyle(33554432);
        this.chatListView.setDisableHighlightState(true);
        RecyclerListView recyclerListView2 = this.chatListView;
        ChatActivityAdapter chatActivityAdapter = new ChatActivityAdapter(context);
        this.chatAdapter = chatActivityAdapter;
        recyclerListView2.setAdapter(chatActivityAdapter);
        this.chatListView.setClipToPadding(false);
        this.chatListView.setPadding(0, AndroidUtilities.dp(4.0f), 0, AndroidUtilities.dp(3.0f));
        RecyclerListView recyclerListView3 = this.chatListView;
        AnonymousClass7 anonymousClass7 = new AnonymousClass7(null, this.chatListView, this.theme);
        this.chatListItemAnimator = anonymousClass7;
        recyclerListView3.setItemAnimator(anonymousClass7);
        this.chatListItemAnimator.setReversePositions(true);
        this.chatListView.setLayoutAnimation(null);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(context) { // from class: com.radolyn.ayugram.ui.AyuMessageHistory.8
            @Override // androidx.recyclerview.widget.LinearLayoutManager, androidx.recyclerview.widget.RecyclerView.LayoutManager
            public boolean supportsPredictiveItemAnimations() {
                return true;
            }

            @Override // androidx.recyclerview.widget.LinearLayoutManager
            protected void calculateExtraLayoutSpace(RecyclerView.State state, int[] iArr) {
                super.calculateExtraLayoutSpace(state, iArr);
                iArr[0] = Math.max(iArr[0], ActionBar.getCurrentActionBarHeight() + AndroidUtilities.statusBarHeight);
            }

            @Override // androidx.recyclerview.widget.LinearLayoutManager, androidx.recyclerview.widget.RecyclerView.LayoutManager
            public void smoothScrollToPosition(RecyclerView recyclerView, RecyclerView.State state, int i5) {
                LinearSmoothScrollerCustom linearSmoothScrollerCustom = new LinearSmoothScrollerCustom(recyclerView.getContext(), 1);
                linearSmoothScrollerCustom.setTargetPosition(i5);
                startSmoothScroll(linearSmoothScrollerCustom);
            }
        };
        this.chatLayoutManager = linearLayoutManager;
        linearLayoutManager.setOrientation(1);
        this.chatLayoutManager.setStackFromEnd(true);
        this.chatListView.setLayoutManager(this.chatLayoutManager);
        this.chatScrollHelper = new RecyclerAnimationScrollHelper(this.chatListView, this.chatLayoutManager);
        this.contentView.addView(this.chatListView, LayoutHelper.createFrame(-1, -1.0f));
        this.chatListView.setOnScrollListener(new RecyclerView.OnScrollListener() { // from class: com.radolyn.ayugram.ui.AyuMessageHistory.9
            @Override // androidx.recyclerview.widget.RecyclerView.OnScrollListener
            public void onScrollStateChanged(RecyclerView recyclerView, int i5) {
                if (i5 == 1) {
                    AyuMessageHistory.this.scrollingFloatingDate = true;
                    AyuMessageHistory.this.checkTextureViewPosition = true;
                } else if (i5 == 0) {
                    AyuMessageHistory.this.scrollingFloatingDate = false;
                    AyuMessageHistory.this.checkTextureViewPosition = false;
                    AyuMessageHistory.this.hideFloatingDateView(true);
                }
            }

            @Override // androidx.recyclerview.widget.RecyclerView.OnScrollListener
            public void onScrolled(RecyclerView recyclerView, int i5, int i6) {
                AyuMessageHistory.this.chatListView.invalidate();
                if (i6 != 0) {
                    AyuMessageHistory.this.contentView.invalidateBlur();
                }
                if (i6 != 0 && AyuMessageHistory.this.scrollingFloatingDate && !AyuMessageHistory.this.currentFloatingTopIsNotMessage && AyuMessageHistory.this.floatingDateView.getTag() == null) {
                    if (AyuMessageHistory.this.floatingDateAnimation != null) {
                        AyuMessageHistory.this.floatingDateAnimation.cancel();
                    }
                    AyuMessageHistory.this.floatingDateView.setTag(1);
                    AyuMessageHistory.this.floatingDateAnimation = new AnimatorSet();
                    AyuMessageHistory.this.floatingDateAnimation.setDuration(150L);
                    AyuMessageHistory.this.floatingDateAnimation.playTogether(ObjectAnimator.ofFloat(AyuMessageHistory.this.floatingDateView, "alpha", 1.0f));
                    AyuMessageHistory.this.floatingDateAnimation.addListener(new AnimatorListenerAdapter() { // from class: com.radolyn.ayugram.ui.AyuMessageHistory.9.1
                        @Override // android.animation.AnimatorListenerAdapter, android.animation.Animator.AnimatorListener
                        public void onAnimationEnd(Animator animator) {
                            if (animator.equals(AyuMessageHistory.this.floatingDateAnimation)) {
                                AyuMessageHistory.this.floatingDateAnimation = null;
                            }
                        }
                    });
                    AyuMessageHistory.this.floatingDateAnimation.start();
                }
                if (i6 > 0) {
                    AyuMessageHistory.this.pagedownScrollAccumulator = 0;
                    AyuMessageHistory.this.pagedownHidden = false;
                    AyuMessageHistory.this.updatePagedownButtonVisibility(true);
                } else if (i6 < 0) {
                    AyuMessageHistory.this.pagedownScrollAccumulator -= i6;
                    if (AyuMessageHistory.this.pagedownScrollAccumulator > AndroidUtilities.dp(42.0f)) {
                        AyuMessageHistory.this.pagedownHidden = true;
                        AyuMessageHistory.this.updatePagedownButtonVisibility(true);
                    }
                }
                AyuMessageHistory.this.checkScrollForLoad(true);
                AyuMessageHistory.this.updateMessagesVisiblePart();
            }
        });
        int i5 = this.scrollToPositionOnRecreate;
        if (i5 != -1) {
            this.chatLayoutManager.scrollToPositionWithOffset(i5, this.scrollToOffsetOnRecreate);
            this.scrollToPositionOnRecreate = -1;
        }
        FrameLayout frameLayout2 = new FrameLayout(context);
        this.progressView = frameLayout2;
        frameLayout2.setVisibility(4);
        this.contentView.addView(this.progressView, LayoutHelper.createFrame(-1, -1, 51));
        View view = new View(context);
        this.progressView2 = view;
        view.setBackground(Theme.createServiceDrawable(AndroidUtilities.dp(18.0f), this.progressView2, this.contentView));
        this.progressView.addView(this.progressView2, LayoutHelper.createFrame(36, 36, 17));
        RadialProgressView radialProgressView = new RadialProgressView(context, this.theme);
        this.progressBar = radialProgressView;
        radialProgressView.setSize(AndroidUtilities.dp(28.0f));
        this.progressBar.setProgressColor(getThemedColor(i4));
        this.progressView.addView(this.progressBar, LayoutHelper.createFrame(32, 32, 17));
        ChatActionCell chatActionCell = new ChatActionCell(context, false, this.theme);
        this.floatingDateView = chatActionCell;
        chatActionCell.setAlpha(0.0f);
        this.floatingDateView.setImportantForAccessibility(2);
        this.contentView.addView(this.floatingDateView, LayoutHelper.createFrame(-2, -2.0f, 49, 0.0f, 4.0f, 0.0f, 0.0f));
        BlurredBackgroundSourceColor blurredBackgroundSourceColor = new BlurredBackgroundSourceColor();
        blurredBackgroundSourceColor.setColor(0);
        ChatActivitySideControlsButtonsLayout chatActivitySideControlsButtonsLayout = new ChatActivitySideControlsButtonsLayout(context, getResourceProvider(), new BlurredBackgroundColorProviderThemed(getResourceProvider(), Theme.key_chat_messagePanelBackground), new BlurredBackgroundDrawableViewFactory(blurredBackgroundSourceColor));
        this.searchButtonLayout = chatActivitySideControlsButtonsLayout;
        chatActivitySideControlsButtonsLayout.setOnClickListener(new ButtonOnClickListener() { // from class: com.radolyn.ayugram.ui.AyuMessageHistory$$ExternalSyntheticLambda3
            @Override // org.telegram.ui.Components.chat.layouts.ButtonOnClickListener
            public final void onClick(int i6, View view2) {
                this.f$0.lambda$createView$9(i6, view2);
            }
        });
        this.searchButtonLayout.setClipChildren(false);
        this.searchButtonLayout.setClipToPadding(false);
        this.contentView.setClipChildren(false);
        this.contentView.setClipToPadding(false);
        this.contentView.addView(this.searchButtonLayout, LayoutHelper.createFrame(57, 300.0f, 85, 0.0f, 0.0f, 0.0f, 0.0f));
        updatePagedownButtonPosition();
        this.contentView.addView(this.actionBar);
        this.chatAdapter.updateRows();
        if (this.loading && this.messages.isEmpty()) {
            AndroidUtilities.updateViewVisibilityAnimated(this.progressView, true, 0.3f, true);
            this.chatListView.setEmptyView(null);
        } else {
            AndroidUtilities.updateViewVisibilityAnimated(this.progressView, false, 0.3f, true);
            this.chatListView.setEmptyView(this.emptyViewContainer);
        }
        this.chatListView.setAnimateEmptyView(true, 1);
        UndoView undoView = new UndoView(context, null, false, this.theme);
        this.undoView = undoView;
        this.contentView.addView(undoView, LayoutHelper.createFrame(-1, -2.0f, 83, 8.0f, 0.0f, 8.0f, 8.0f));
        updateEmptyPlaceholder();
        return this.fragmentView;
    }

    public static /* synthetic */ boolean $r8$lambda$jLYav_im1uXVZODKnGhEINd9Csg(View view, MotionEvent motionEvent) {
        return true;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$createView$8(View view, int i, float f, float f2) {
        createMenu(view, f, f2);
    }

    /* JADX INFO: renamed from: com.radolyn.ayugram.ui.AyuMessageHistory$7, reason: invalid class name */
    class AnonymousClass7 extends ChatListItemAnimator {
        Runnable finishRunnable;
        int scrollAnimationIndex;

        AnonymousClass7(ChatActivity chatActivity, RecyclerListView recyclerListView, Theme.ResourcesProvider resourcesProvider) {
            super(chatActivity, recyclerListView, resourcesProvider);
            this.scrollAnimationIndex = -1;
        }

        @Override // androidx.recyclerview.widget.ChatListItemAnimator
        public void onAnimationStart() {
            if (this.scrollAnimationIndex == -1) {
                this.scrollAnimationIndex = AyuMessageHistory.this.getNotificationCenter().setAnimationInProgress(this.scrollAnimationIndex, null, false);
            }
            Runnable runnable = this.finishRunnable;
            if (runnable != null) {
                AndroidUtilities.cancelRunOnUIThread(runnable);
                this.finishRunnable = null;
            }
            if (BuildVars.LOGS_ENABLED) {
                FileLog.d("admin logs chatItemAnimator disable notifications");
            }
            AyuMessageHistory.this.updateMessagesVisiblePart();
        }

        @Override // androidx.recyclerview.widget.ChatListItemAnimator, androidx.recyclerview.widget.DefaultItemAnimator
        protected void onAllAnimationsDone() {
            super.onAllAnimationsDone();
            Runnable runnable = this.finishRunnable;
            if (runnable != null) {
                AndroidUtilities.cancelRunOnUIThread(runnable);
            }
            Runnable runnable2 = new Runnable() { // from class: com.radolyn.ayugram.ui.AyuMessageHistory$7$$ExternalSyntheticLambda0
                @Override // java.lang.Runnable
                public final void run() {
                    this.f$0.lambda$onAllAnimationsDone$0();
                }
            };
            this.finishRunnable = runnable2;
            AndroidUtilities.runOnUIThread(runnable2);
        }

        /* JADX INFO: Access modifiers changed from: private */
        public /* synthetic */ void lambda$onAllAnimationsDone$0() {
            if (this.scrollAnimationIndex != -1) {
                AyuMessageHistory.this.getNotificationCenter().onAnimationFinish(this.scrollAnimationIndex);
                this.scrollAnimationIndex = -1;
            }
            if (BuildVars.LOGS_ENABLED) {
                FileLog.d("admin logs chatItemAnimator enable notifications");
            }
            AyuMessageHistory.this.updateMessagesVisiblePart();
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$createView$9(int i, View view) {
        if (i == 1) {
            onPageDownClicked();
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void closeMenu() {
        ActionBarPopupWindow actionBarPopupWindow = this.scrimPopupWindow;
        if (actionBarPopupWindow != null) {
            actionBarPopupWindow.dismiss();
        }
        dimBehindView(null, false);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void dimBehindView(View view, final boolean z) {
        if (z) {
            this.scrimView = view;
        }
        this.contentView.invalidate();
        this.chatListView.invalidate();
        AnimatorSet animatorSet = this.scrimAnimatorSet;
        if (animatorSet != null) {
            animatorSet.removeAllListeners();
            this.scrimAnimatorSet.cancel();
        }
        float f = z ? 0.2f : 0.0f;
        this.scrimAnimatorSet = new AnimatorSet();
        ValueAnimator valueAnimatorOfFloat = ValueAnimator.ofFloat(this.scrimPaintAlpha, f);
        valueAnimatorOfFloat.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() { // from class: com.radolyn.ayugram.ui.AyuMessageHistory$$ExternalSyntheticLambda5
            @Override // android.animation.ValueAnimator.AnimatorUpdateListener
            public final void onAnimationUpdate(ValueAnimator valueAnimator) {
                this.f$0.lambda$dimBehindView$10(valueAnimator);
            }
        });
        ChatActivitySideControlsButtonsLayout chatActivitySideControlsButtonsLayout = this.searchButtonLayout;
        if (chatActivitySideControlsButtonsLayout != null) {
            this.scrimAnimatorSet.playTogether(valueAnimatorOfFloat, ObjectAnimator.ofFloat(chatActivitySideControlsButtonsLayout, (Property<ChatActivitySideControlsButtonsLayout, Float>) View.ALPHA, z ? 0.0f : 1.0f));
        } else {
            this.scrimAnimatorSet.playTogether(valueAnimatorOfFloat);
        }
        this.scrimAnimatorSet.setInterpolator(CubicBezierInterpolator.DEFAULT);
        this.scrimAnimatorSet.setDuration(z ? 150L : 220L);
        this.scrimAnimatorSet.addListener(new AnimatorListenerAdapter() { // from class: com.radolyn.ayugram.ui.AyuMessageHistory.10
            @Override // android.animation.AnimatorListenerAdapter, android.animation.Animator.AnimatorListener
            public void onAnimationEnd(Animator animator) {
                if (z) {
                    return;
                }
                AyuMessageHistory.this.scrimView = null;
                AyuMessageHistory.this.contentView.invalidate();
                AyuMessageHistory.this.chatListView.invalidate();
            }
        });
        this.scrimAnimatorSet.start();
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$dimBehindView$10(ValueAnimator valueAnimator) {
        this.scrimPaintAlpha = ((Float) valueAnimator.getAnimatedValue()).floatValue();
        SizeNotifierFrameLayout sizeNotifierFrameLayout = this.contentView;
        if (sizeNotifierFrameLayout != null) {
            sizeNotifierFrameLayout.invalidate();
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public boolean createMenu(View view) {
        return createMenu(view, 0.0f, 0.0f);
    }

    private boolean createMenu(final View view, final float f, final float f2) {
        MessageObject messageObject;
        if (view instanceof ChatMessageCell) {
            messageObject = ((ChatMessageCell) view).getMessageObject();
        } else {
            messageObject = view instanceof ChatActionCell ? ((ChatActionCell) view).getMessageObject() : null;
        }
        if (messageObject == null || messageObject.type == 10) {
            return false;
        }
        int messageType = getMessageType(messageObject);
        this.selectedObject = messageObject;
        if (getParentActivity() == null) {
            return false;
        }
        if (this.scrimPopupWindow != null) {
            closeMenu();
            return false;
        }
        final ArrayList arrayList = new ArrayList();
        final ArrayList arrayList2 = new ArrayList();
        final ArrayList arrayList3 = new ArrayList();
        if (this.chatActivity.canSendMessage()) {
            arrayList.add(LocaleController.getString(R.string.Reply));
            arrayList2.add(20);
            arrayList3.add(Integer.valueOf(R.drawable.menu_reply));
        }
        arrayList.add(LocaleController.getString(R.string.Forward));
        arrayList2.add(21);
        arrayList3.add(Integer.valueOf(R.drawable.msg_forward));
        MessageObject messageObject2 = this.selectedObject;
        if (messageObject2.type == 0 || messageObject2.caption != null) {
            arrayList.add(LocaleController.getString(R.string.Copy));
            arrayList3.add(Integer.valueOf(R.drawable.msg_copy));
            arrayList2.add(3);
        }
        if (messageType == 3) {
            TLRPC.MessageMedia messageMedia = this.selectedObject.messageOwner.media;
            if ((messageMedia instanceof TLRPC.TL_messageMediaWebPage) && MessageObject.isNewGifDocument(messageMedia.webpage.document)) {
                arrayList.add(LocaleController.getString(R.string.SaveToGIFs));
                arrayList3.add(Integer.valueOf(R.drawable.msg_gif));
                arrayList2.add(11);
            }
        } else if (messageType == 4) {
            if (this.selectedObject.isVideo()) {
                arrayList.add(LocaleController.getString(R.string.SaveToGallery));
                arrayList3.add(Integer.valueOf(R.drawable.msg_gallery));
                arrayList2.add(4);
                arrayList.add(LocaleController.getString(R.string.ShareFile));
                arrayList3.add(Integer.valueOf(R.drawable.msg_share));
                arrayList2.add(6);
            } else if (this.selectedObject.isMusic()) {
                arrayList.add(LocaleController.getString(R.string.SaveToMusic));
                arrayList3.add(Integer.valueOf(R.drawable.msg_download));
                arrayList2.add(10);
                arrayList.add(LocaleController.getString(R.string.ShareFile));
                arrayList3.add(Integer.valueOf(R.drawable.msg_share));
                arrayList2.add(6);
            } else if (this.selectedObject.getDocument() != null) {
                if (MessageObject.isNewGifDocument(this.selectedObject.getDocument())) {
                    arrayList.add(LocaleController.getString(R.string.SaveToGIFs));
                    arrayList3.add(Integer.valueOf(R.drawable.msg_gif));
                    arrayList2.add(11);
                }
                arrayList.add(LocaleController.getString(R.string.SaveToDownloads));
                arrayList3.add(Integer.valueOf(R.drawable.msg_download));
                arrayList2.add(10);
                arrayList.add(LocaleController.getString(R.string.ShareFile));
                arrayList3.add(Integer.valueOf(R.drawable.msg_share));
                arrayList2.add(6);
            } else {
                arrayList.add(LocaleController.getString(R.string.SaveToGallery));
                arrayList3.add(Integer.valueOf(R.drawable.msg_gallery));
                arrayList2.add(4);
            }
        } else if (messageType == 5) {
            arrayList.add(LocaleController.getString(R.string.ApplyLocalizationFile));
            arrayList3.add(Integer.valueOf(R.drawable.msg_language));
            arrayList2.add(5);
            arrayList.add(LocaleController.getString(R.string.SaveToDownloads));
            arrayList3.add(Integer.valueOf(R.drawable.msg_download));
            arrayList2.add(10);
            arrayList.add(LocaleController.getString(R.string.ShareFile));
            arrayList3.add(Integer.valueOf(R.drawable.msg_share));
            arrayList2.add(6);
        } else if (messageType == 10) {
            arrayList.add(LocaleController.getString(R.string.ApplyThemeFile));
            arrayList3.add(Integer.valueOf(R.drawable.msg_theme));
            arrayList2.add(5);
            arrayList.add(LocaleController.getString(R.string.SaveToDownloads));
            arrayList3.add(Integer.valueOf(R.drawable.msg_download));
            arrayList2.add(10);
            arrayList.add(LocaleController.getString(R.string.ShareFile));
            arrayList3.add(Integer.valueOf(R.drawable.msg_share));
            arrayList2.add(6);
        } else if (messageType == 6) {
            arrayList.add(LocaleController.getString(R.string.SaveToGallery));
            arrayList3.add(Integer.valueOf(R.drawable.msg_gallery));
            arrayList2.add(7);
            arrayList.add(LocaleController.getString(R.string.SaveToDownloads));
            arrayList3.add(Integer.valueOf(R.drawable.msg_download));
            arrayList2.add(10);
            arrayList.add(LocaleController.getString(R.string.ShareFile));
            arrayList3.add(Integer.valueOf(R.drawable.msg_share));
            arrayList2.add(6);
        } else if (messageType == 7) {
            if (this.selectedObject.isMask()) {
                arrayList.add(LocaleController.getString(R.string.AddToMasks));
            } else {
                arrayList.add(LocaleController.getString(R.string.AddToStickers));
            }
            arrayList3.add(Integer.valueOf(R.drawable.msg_sticker));
            arrayList2.add(9);
        } else if (messageType == 8) {
            long j = this.selectedObject.messageOwner.media.user_id;
            TLRPC.User user = j != 0 ? MessagesController.getInstance(this.currentAccount).getUser(Long.valueOf(j)) : null;
            if (user != null && user.id != UserConfig.getInstance(this.currentAccount).getClientUserId() && ContactsController.getInstance(this.currentAccount).contactsDict.get(Long.valueOf(user.id)) == null) {
                arrayList.add(LocaleController.getString(R.string.AddContactTitle));
                arrayList3.add(Integer.valueOf(R.drawable.msg_addcontact));
                arrayList2.add(15);
            }
            if (!TextUtils.isEmpty(this.selectedObject.messageOwner.media.phone_number)) {
                arrayList.add(LocaleController.getString(R.string.Copy));
                arrayList3.add(Integer.valueOf(R.drawable.msg_copy));
                arrayList2.add(16);
                arrayList.add(LocaleController.getString(R.string.Call));
                arrayList3.add(Integer.valueOf(R.drawable.msg_calls));
                arrayList2.add(17);
            }
        }
        if (this.mode == 2) {
            arrayList.add(LocaleController.getString(R.string.ShowInChat));
            arrayList2.add(18);
            arrayList3.add(Integer.valueOf(R.drawable.msg_view_file));
            arrayList.add(LocaleController.getString(R.string.Delete));
            arrayList2.add(19);
            arrayList3.add(Integer.valueOf(R.drawable.msg_delete));
        }
        arrayList.add(LocaleController.getString(R.string.Details));
        arrayList2.add(Integer.valueOf(Opcodes.SUB_DOUBLE_2ADDR));
        arrayList3.add(Integer.valueOf(R.drawable.msg_info));
        new Runnable() { // from class: com.radolyn.ayugram.ui.AyuMessageHistory$$ExternalSyntheticLambda7
            @Override // java.lang.Runnable
            public final void run() {
                this.f$0.lambda$createMenu$15(arrayList2, arrayList, arrayList3, view, f, f2);
            }
        }.run();
        return true;
    }

    /* JADX INFO: Access modifiers changed from: private */
    /* JADX WARN: Code duplicated, block: B:74:0x01f2  */
    public /* synthetic */ void lambda$createMenu$15(ArrayList arrayList, ArrayList arrayList2, ArrayList arrayList3, View view, float f, float f2) {
        int i;
        int y;
        AnimatedEmojiSpan[] animatedEmojiSpanArr;
        int i2;
        final ActionBarPopupWindow.ActionBarPopupWindowLayout actionBarPopupWindowLayout;
        final ArrayList arrayList4 = arrayList;
        if (arrayList4.isEmpty()) {
            return;
        }
        final ActionBarPopupWindow.ActionBarPopupWindowLayout actionBarPopupWindowLayout2 = new ActionBarPopupWindow.ActionBarPopupWindowLayout(getParentActivity(), R.drawable.popup_fixed_alert, this.theme, 1);
        actionBarPopupWindowLayout2.setMinimumWidth(AndroidUtilities.dp(200.0f));
        Rect rect = new Rect();
        getParentActivity().getResources().getDrawable(R.drawable.popup_fixed_alert).mutate().getPadding(rect);
        actionBarPopupWindowLayout2.setBackgroundColor(getThemedColor(Theme.key_actionBarDefaultSubmenuBackground));
        int size = arrayList2.size();
        int i3 = 0;
        final int i4 = 0;
        while (i4 < size) {
            if (arrayList4.get(i4) == null) {
                actionBarPopupWindowLayout2.addView((View) new ActionBarPopupWindow.GapView(getContext(), this.theme), LayoutHelper.createLinear(-1, 8));
                i2 = i4;
                actionBarPopupWindowLayout = actionBarPopupWindowLayout2;
            } else {
                final ActionBarMenuSubItem actionBarMenuSubItem = new ActionBarMenuSubItem(getParentActivity(), i4 == 0, i4 == size + (-1), this.theme);
                actionBarMenuSubItem.setMinimumWidth(AndroidUtilities.dp(200.0f));
                actionBarMenuSubItem.setTextAndIcon((CharSequence) arrayList2.get(i4), ((Integer) arrayList3.get(i4)).intValue());
                final Integer num = (Integer) arrayList4.get(i4);
                actionBarPopupWindowLayout2.addView(actionBarMenuSubItem);
                View.OnClickListener onClickListener = new View.OnClickListener() { // from class: com.radolyn.ayugram.ui.AyuMessageHistory$$ExternalSyntheticLambda13
                    @Override // android.view.View.OnClickListener
                    public final void onClick(View view2) {
                        this.f$0.lambda$createMenu$11(i4, arrayList4, num, actionBarPopupWindowLayout2, actionBarMenuSubItem, view2);
                    }
                };
                i2 = i4;
                actionBarPopupWindowLayout = actionBarPopupWindowLayout2;
                actionBarMenuSubItem.setOnClickListener(onClickListener);
                if (num.intValue() == 204) {
                    final int iAddViewToSwipeBack = actionBarPopupWindowLayout.addViewToSwipeBack(new MessageDetailsPopupWrapper(this, actionBarPopupWindowLayout.getSwipeBack(), this.selectedObject, this.theme) { // from class: com.radolyn.ayugram.ui.AyuMessageHistory.11
                        @Override // com.exteragram.messenger.components.MessageDetailsPopupWrapper
                        protected void copy(String str) {
                            if (AndroidUtilities.addToClipboard(str)) {
                                BulletinFactory.of(AyuMessageHistory.this).createCopyBulletin(LocaleController.getString(R.string.TextCopied)).show();
                            }
                        }
                    }.swipeBack);
                    actionBarMenuSubItem.setRightIcon(R.drawable.msg_arrowright);
                    actionBarMenuSubItem.setOnClickListener(new View.OnClickListener() { // from class: com.radolyn.ayugram.ui.AyuMessageHistory$$ExternalSyntheticLambda14
                        @Override // android.view.View.OnClickListener
                        public final void onClick(View view2) {
                            this.f$0.lambda$createMenu$12(actionBarPopupWindowLayout, iAddViewToSwipeBack, view2);
                        }
                    });
                }
            }
            i4 = i2 + 1;
            arrayList4 = arrayList;
            actionBarPopupWindowLayout2 = actionBarPopupWindowLayout;
        }
        ActionBarPopupWindow.ActionBarPopupWindowLayout actionBarPopupWindowLayout3 = actionBarPopupWindowLayout2;
        MessageObject messageObject = this.selectedObject;
        int i5 = -2;
        if (messageObject.contentType == 0) {
            CharSequence charSequence = messageObject.messageText;
            AnimatedEmojiSpan[] animatedEmojiSpanArr2 = charSequence instanceof Spanned ? (AnimatedEmojiSpan[]) ((Spanned) charSequence).getSpans(0, charSequence.length(), AnimatedEmojiSpan.class) : null;
            CharSequence charSequence2 = this.selectedObject.caption;
            AnimatedEmojiSpan[] animatedEmojiSpanArr3 = charSequence2 instanceof Spanned ? (AnimatedEmojiSpan[]) ((Spanned) charSequence2).getSpans(0, charSequence2.length(), AnimatedEmojiSpan.class) : null;
            int length = (animatedEmojiSpanArr2 == null ? 0 : animatedEmojiSpanArr2.length) + (animatedEmojiSpanArr3 == null ? 0 : animatedEmojiSpanArr3.length);
            if (length > 0) {
                final ArrayList arrayList5 = new ArrayList();
                int length2 = animatedEmojiSpanArr2 == null ? 0 : animatedEmojiSpanArr2.length;
                int i6 = 0;
                while (i6 < length) {
                    AnimatedEmojiSpan animatedEmojiSpan = i6 < length2 ? animatedEmojiSpanArr2[i6] : animatedEmojiSpanArr3[i6 - length2];
                    if (animatedEmojiSpan != null && !animatedEmojiSpan.standard) {
                        TLRPC.Document documentFindDocument = animatedEmojiSpan.document;
                        if (documentFindDocument == null) {
                            documentFindDocument = AnimatedEmojiDrawable.findDocument(this.currentAccount, animatedEmojiSpan.documentId);
                        }
                        TLRPC.InputStickerSet inputStickerSet = MessageObject.getInputStickerSet(documentFindDocument);
                        if (inputStickerSet != null) {
                            int i7 = i3;
                            while (true) {
                                if (i7 >= arrayList5.size()) {
                                    animatedEmojiSpanArr = animatedEmojiSpanArr3;
                                    arrayList5.add(inputStickerSet);
                                    break;
                                } else {
                                    animatedEmojiSpanArr = animatedEmojiSpanArr3;
                                    if (((TLRPC.InputStickerSet) arrayList5.get(i7)).id == inputStickerSet.id) {
                                        break;
                                    }
                                    i7++;
                                    animatedEmojiSpanArr3 = animatedEmojiSpanArr;
                                }
                            }
                        } else {
                            animatedEmojiSpanArr = animatedEmojiSpanArr3;
                        }
                    } else {
                        animatedEmojiSpanArr = animatedEmojiSpanArr3;
                        i3 = i3;
                        length2 = length2;
                    }
                    i6++;
                    animatedEmojiSpanArr3 = animatedEmojiSpanArr;
                    length2 = length2;
                    i3 = i3;
                }
                i = i3;
                if (!arrayList5.isEmpty() && !getMessagesController().premiumFeaturesBlocked()) {
                    FrameLayout frameLayout = new FrameLayout(this.contentView.getContext());
                    frameLayout.setBackgroundColor(getThemedColor(Theme.key_actionBarDefaultSubmenuSeparator));
                    actionBarPopupWindowLayout3.addView((View) frameLayout, LayoutHelper.createLinear(-1, 8));
                    MessageContainsEmojiButton messageContainsEmojiButton = new MessageContainsEmojiButton(this.currentAccount, this.contentView.getContext(), this.theme, arrayList5, 0);
                    messageContainsEmojiButton.setOnClickListener(new View.OnClickListener() { // from class: com.radolyn.ayugram.ui.AyuMessageHistory$$ExternalSyntheticLambda15
                        @Override // android.view.View.OnClickListener
                        public final void onClick(View view2) {
                            this.f$0.lambda$createMenu$13(arrayList5, view2);
                        }
                    });
                    actionBarPopupWindowLayout3.addView((View) messageContainsEmojiButton, LayoutHelper.createLinear(-1, -2));
                    actionBarPopupWindowLayout3.precalculateHeight();
                }
            } else {
                i = 0;
            }
        } else {
            i = 0;
        }
        if (actionBarPopupWindowLayout3.getSwipeBack() != null) {
            actionBarPopupWindowLayout3.getSwipeBack().setOnClickListener(new View.OnClickListener() { // from class: com.radolyn.ayugram.ui.AyuMessageHistory$$ExternalSyntheticLambda16
                @Override // android.view.View.OnClickListener
                public final void onClick(View view2) {
                    this.f$0.lambda$createMenu$14(view2);
                }
            });
        }
        ChatScrimPopupContainerLayout chatScrimPopupContainerLayout = new ChatScrimPopupContainerLayout(this.contentView.getContext()) { // from class: com.radolyn.ayugram.ui.AyuMessageHistory.12
            @Override // android.view.ViewGroup, android.view.View
            public boolean dispatchKeyEvent(KeyEvent keyEvent) {
                if (keyEvent.getKeyCode() == 4 && keyEvent.getRepeatCount() == 0) {
                    AyuMessageHistory.this.closeMenu();
                }
                return super.dispatchKeyEvent(keyEvent);
            }

            @Override // android.view.ViewGroup, android.view.View
            public boolean dispatchTouchEvent(MotionEvent motionEvent) {
                boolean zDispatchTouchEvent = super.dispatchTouchEvent(motionEvent);
                if (motionEvent.getAction() == 0 && !zDispatchTouchEvent) {
                    AyuMessageHistory.this.closeMenu();
                }
                return zDispatchTouchEvent;
            }
        };
        chatScrimPopupContainerLayout.addView(actionBarPopupWindowLayout3, LayoutHelper.createLinearRelatively(-2.0f, -2.0f, 3, 0.0f, 0.0f, 0.0f, 0.0f));
        chatScrimPopupContainerLayout.setPopupWindowLayout(actionBarPopupWindowLayout3);
        ActionBarPopupWindow actionBarPopupWindow = new ActionBarPopupWindow(chatScrimPopupContainerLayout, i5, i5) { // from class: com.radolyn.ayugram.ui.AyuMessageHistory.13
            @Override // org.telegram.ui.ActionBar.ActionBarPopupWindow, android.widget.PopupWindow
            public void dismiss() {
                super.dismiss();
                if (AyuMessageHistory.this.scrimPopupWindow != this) {
                    return;
                }
                Bulletin.hideVisible();
                AyuMessageHistory.this.scrimPopupWindow = null;
                AyuMessageHistory.this.dimBehindView(null, false);
            }
        };
        this.scrimPopupWindow = actionBarPopupWindow;
        actionBarPopupWindow.setPauseNotifications(true);
        this.scrimPopupWindow.setDismissAnimationDuration(Opcodes.REM_INT_LIT8);
        this.scrimPopupWindow.setOutsideTouchable(true);
        this.scrimPopupWindow.setClippingEnabled(true);
        this.scrimPopupWindow.setAnimationStyle(R.style.PopupContextAnimation);
        this.scrimPopupWindow.setFocusable(true);
        chatScrimPopupContainerLayout.measure(View.MeasureSpec.makeMeasureSpec(AndroidUtilities.dp(1000.0f), Integer.MIN_VALUE), View.MeasureSpec.makeMeasureSpec(AndroidUtilities.dp(1000.0f), Integer.MIN_VALUE));
        this.scrimPopupWindow.setInputMethodMode(2);
        this.scrimPopupWindow.setSoftInputMode(48);
        this.scrimPopupWindow.getContentView().setFocusableInTouchMode(true);
        actionBarPopupWindowLayout3.setFitItems(true);
        int left = (((view.getLeft() + ((int) f)) - chatScrimPopupContainerLayout.getMeasuredWidth()) + rect.left) - AndroidUtilities.dp(28.0f);
        if (left < AndroidUtilities.dp(6.0f)) {
            left = AndroidUtilities.dp(6.0f);
        } else if (left > (this.chatListView.getMeasuredWidth() - AndroidUtilities.dp(6.0f)) - chatScrimPopupContainerLayout.getMeasuredWidth()) {
            left = (this.chatListView.getMeasuredWidth() - AndroidUtilities.dp(6.0f)) - chatScrimPopupContainerLayout.getMeasuredWidth();
        }
        if (AndroidUtilities.isTablet()) {
            int[] iArr = new int[2];
            this.fragmentView.getLocationInWindow(iArr);
            left += iArr[i];
        }
        int height = this.contentView.getHeight();
        int measuredHeight = chatScrimPopupContainerLayout.getMeasuredHeight() + AndroidUtilities.dp(48.0f);
        int iMeasureKeyboardHeight = this.contentView.measureKeyboardHeight();
        if (iMeasureKeyboardHeight > AndroidUtilities.dp(20.0f)) {
            height += iMeasureKeyboardHeight;
        }
        if (measuredHeight < height) {
            y = (int) (this.chatListView.getY() + view.getTop() + f2);
            if ((measuredHeight - rect.top) - rect.bottom > AndroidUtilities.dp(240.0f)) {
                y += AndroidUtilities.dp(240.0f) - measuredHeight;
            }
            if (y < this.chatListView.getY() + AndroidUtilities.dp(24.0f)) {
                y = (int) (this.chatListView.getY() + AndroidUtilities.dp(24.0f));
            } else {
                int i8 = height - measuredHeight;
                if (y > i8 - AndroidUtilities.dp(8.0f)) {
                    y = i8 - AndroidUtilities.dp(8.0f);
                }
            }
        } else {
            y = this.inBubbleMode ? i : AndroidUtilities.statusBarHeight;
        }
        chatScrimPopupContainerLayout.setMaxHeight(height - y);
        this.scrimPopupWindow.showAtLocation(this.chatListView, 51, left, y);
        dimBehindView(view, true);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$createMenu$11(int i, ArrayList arrayList, Integer num, ActionBarPopupWindow.ActionBarPopupWindowLayout actionBarPopupWindowLayout, ActionBarMenuSubItem actionBarMenuSubItem, View view) {
        if (this.selectedObject == null || i >= arrayList.size()) {
            return;
        }
        processSelectedOption(num.intValue(), arrayList, actionBarPopupWindowLayout, actionBarMenuSubItem);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$createMenu$12(ActionBarPopupWindow.ActionBarPopupWindowLayout actionBarPopupWindowLayout, int i, View view) {
        if (this.selectedObject == null || getParentActivity() == null) {
            return;
        }
        actionBarPopupWindowLayout.getSwipeBack().openForeground(i);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$createMenu$13(ArrayList arrayList, View view) {
        EmojiPacksAlert emojiPacksAlert = new EmojiPacksAlert(this, getParentActivity(), this.theme, arrayList);
        closeMenu();
        showDialog(emojiPacksAlert);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$createMenu$14(View view) {
        closeMenu();
    }

    /* JADX INFO: Access modifiers changed from: private */
    public TextureView createTextureView(boolean z) {
        if (this.parentLayout == null) {
            return null;
        }
        if (this.roundVideoContainer == null) {
            FrameLayout frameLayout = new FrameLayout(getParentActivity()) { // from class: com.radolyn.ayugram.ui.AyuMessageHistory.14
                @Override // android.view.View
                public void setTranslationY(float f) {
                    super.setTranslationY(f);
                    AyuMessageHistory.this.contentView.invalidate();
                }
            };
            this.roundVideoContainer = frameLayout;
            frameLayout.setOutlineProvider(new ViewOutlineProvider() { // from class: com.radolyn.ayugram.ui.AyuMessageHistory.15
                @Override // android.view.ViewOutlineProvider
                public void getOutline(View view, Outline outline) {
                    int i = AndroidUtilities.roundMessageSize;
                    outline.setOval(0, 0, i, i);
                }
            });
            this.roundVideoContainer.setClipToOutline(true);
            this.roundVideoContainer.setWillNotDraw(false);
            this.roundVideoContainer.setVisibility(4);
            AspectRatioFrameLayout aspectRatioFrameLayout = new AspectRatioFrameLayout(getParentActivity());
            this.aspectRatioFrameLayout = aspectRatioFrameLayout;
            aspectRatioFrameLayout.setBackgroundColor(0);
            if (z) {
                this.roundVideoContainer.addView(this.aspectRatioFrameLayout, LayoutHelper.createFrame(-1, -1.0f));
            }
            TextureView textureView = new TextureView(getParentActivity());
            this.videoTextureView = textureView;
            textureView.setOpaque(false);
            this.aspectRatioFrameLayout.addView(this.videoTextureView, LayoutHelper.createFrame(-1, -1.0f));
        }
        if (this.roundVideoContainer.getParent() == null) {
            SizeNotifierFrameLayout sizeNotifierFrameLayout = this.contentView;
            FrameLayout frameLayout2 = this.roundVideoContainer;
            int i = AndroidUtilities.roundMessageSize;
            sizeNotifierFrameLayout.addView(frameLayout2, 1, new FrameLayout.LayoutParams(i, i));
        }
        this.roundVideoContainer.setVisibility(4);
        this.aspectRatioFrameLayout.setDrawingReady(false);
        return this.videoTextureView;
    }

    /* JADX WARN: Code duplicated, block: B:117:0x035e  */
    private void processSelectedOption(int i, ArrayList arrayList, ActionBarPopupWindow.ActionBarPopupWindowLayout actionBarPopupWindowLayout, ActionBarMenuSubItem actionBarMenuSubItem) {
        File file;
        final MessageObject messageObject = this.selectedObject;
        if (messageObject == null) {
            return;
        }
        int i2 = 0;
        switch (i) {
            case 3:
                AndroidUtilities.addToClipboard(ChatUtils.getInstance().getMessageText(this.selectedObject, null));
                BulletinFactory.of(this).createCopyBulletin(LocaleController.getString(R.string.MessageCopied)).show();
                break;
            case 4:
                String string = messageObject.messageOwner.attachPath;
                if (string != null && string.length() > 0 && !new File(string).exists()) {
                    string = null;
                }
                if (string == null || string.length() == 0) {
                    string = getFileLoader().getPathToMessage(this.selectedObject.messageOwner).toString();
                }
                int i3 = this.selectedObject.type;
                if (i3 == 3 || i3 == 1) {
                    if ((Build.VERSION.SDK_INT <= 28 || BuildVars.NO_SCOPED_STORAGE) && getParentActivity().checkSelfPermission("android.permission.WRITE_EXTERNAL_STORAGE") != 0) {
                        getParentActivity().requestPermissions(new String[]{"android.permission.WRITE_EXTERNAL_STORAGE"}, 4);
                        this.selectedObject = null;
                        return;
                    }
                    MediaController.saveFile(string, getParentActivity(), this.selectedObject.type == 3 ? 1 : 0, null, null);
                }
                break;
            case 5:
                String str = messageObject.messageOwner.attachPath;
                if (str == null || str.length() == 0) {
                    file = null;
                } else {
                    file = new File(this.selectedObject.messageOwner.attachPath);
                    if (!file.exists()) {
                        file = null;
                    }
                }
                if (file == null) {
                    File pathToMessage = getFileLoader().getPathToMessage(this.selectedObject.messageOwner);
                    if (pathToMessage.exists()) {
                        file = pathToMessage;
                    }
                }
                if (file != null) {
                    if (file.getName().toLowerCase().endsWith("attheme")) {
                        LinearLayoutManager linearLayoutManager = this.chatLayoutManager;
                        if (linearLayoutManager != null) {
                            if (linearLayoutManager.findLastVisibleItemPosition() < this.chatLayoutManager.getItemCount() - 1) {
                                int iFindFirstVisibleItemPosition = this.chatLayoutManager.findFirstVisibleItemPosition();
                                this.scrollToPositionOnRecreate = iFindFirstVisibleItemPosition;
                                RecyclerListView.Holder holder = (RecyclerListView.Holder) this.chatListView.findViewHolderForAdapterPosition(iFindFirstVisibleItemPosition);
                                if (holder != null) {
                                    this.scrollToOffsetOnRecreate = holder.itemView.getTop();
                                } else {
                                    this.scrollToPositionOnRecreate = -1;
                                }
                            } else {
                                this.scrollToPositionOnRecreate = -1;
                            }
                        }
                        Theme.ThemeInfo themeInfoApplyThemeFile = Theme.applyThemeFile(file, this.selectedObject.getDocumentName(), null, true);
                        if (themeInfoApplyThemeFile != null) {
                            presentFragment(new ThemePreviewActivity(themeInfoApplyThemeFile));
                        } else {
                            this.scrollToPositionOnRecreate = -1;
                            if (getParentActivity() == null) {
                                this.selectedObject = null;
                                return;
                            }
                            AlertDialog.Builder builder = new AlertDialog.Builder(getParentActivity());
                            builder.setTitle(LocaleController.getString(R.string.AppName));
                            builder.setMessage(LocaleController.getString(R.string.IncorrectTheme));
                            builder.setPositiveButton(LocaleController.getString(R.string.OK), null);
                            showDialog(builder.create());
                        }
                    } else if (LocaleController.getInstance().applyLanguageFile(file, this.currentAccount)) {
                        presentFragment(new LanguageSelectActivity());
                    } else {
                        if (getParentActivity() == null) {
                            this.selectedObject = null;
                            return;
                        }
                        AlertDialog.Builder builder2 = new AlertDialog.Builder(getParentActivity());
                        builder2.setTitle(LocaleController.getString(R.string.AppName));
                        builder2.setMessage(LocaleController.getString(R.string.IncorrectLocalization));
                        builder2.setPositiveButton(LocaleController.getString(R.string.OK), null);
                        showDialog(builder2.create());
                    }
                }
                break;
            case 6:
                String string2 = messageObject.messageOwner.attachPath;
                if (string2 != null && string2.length() > 0 && !new File(string2).exists()) {
                    string2 = null;
                }
                if (string2 == null || string2.length() == 0) {
                    string2 = getFileLoader().getPathToMessage(this.selectedObject.messageOwner).toString();
                }
                Intent intent = new Intent("android.intent.action.SEND");
                intent.setType(this.selectedObject.getDocument().mime_type);
                if (Build.VERSION.SDK_INT >= 24) {
                    try {
                        intent.putExtra("android.intent.extra.STREAM", FileProvider.getUriForFile(getParentActivity(), ApplicationLoader.getApplicationId() + ".provider", new File(string2)));
                        intent.setFlags(1);
                    } catch (Exception unused) {
                        intent.putExtra("android.intent.extra.STREAM", Uri.fromFile(new File(string2)));
                    }
                } else {
                    intent.putExtra("android.intent.extra.STREAM", Uri.fromFile(new File(string2)));
                }
                try {
                    getParentActivity().startActivityForResult(Intent.createChooser(intent, LocaleController.getString(R.string.ShareFile)), 500);
                } catch (Exception unused2) {
                }
                break;
            case 7:
                String string3 = messageObject.messageOwner.attachPath;
                if (string3 != null && string3.length() > 0 && !new File(string3).exists()) {
                    string3 = null;
                }
                if (string3 == null || string3.length() == 0) {
                    string3 = getFileLoader().getPathToMessage(this.selectedObject.messageOwner).toString();
                }
                if ((Build.VERSION.SDK_INT <= 28 || BuildVars.NO_SCOPED_STORAGE) && getParentActivity().checkSelfPermission("android.permission.WRITE_EXTERNAL_STORAGE") != 0) {
                    getParentActivity().requestPermissions(new String[]{"android.permission.WRITE_EXTERNAL_STORAGE"}, 4);
                    this.selectedObject = null;
                    return;
                }
                MediaController.saveFile(string3, getParentActivity(), 0, null, null);
                break;
            case 9:
                showDialog(new StickersAlert(getParentActivity(), this, this.selectedObject.getInputStickerSet(), null, null, false));
                break;
            case 10:
                if ((Build.VERSION.SDK_INT <= 28 || BuildVars.NO_SCOPED_STORAGE) && getParentActivity().checkSelfPermission("android.permission.WRITE_EXTERNAL_STORAGE") != 0) {
                    getParentActivity().requestPermissions(new String[]{"android.permission.WRITE_EXTERNAL_STORAGE"}, 4);
                    this.selectedObject = null;
                    return;
                }
                String documentFileName = FileLoader.getDocumentFileName(this.selectedObject.getDocument());
                if (TextUtils.isEmpty(documentFileName)) {
                    documentFileName = this.selectedObject.getFileName();
                }
                String string4 = this.selectedObject.messageOwner.attachPath;
                if (string4 != null && string4.length() > 0 && !new File(string4).exists()) {
                    string4 = null;
                }
                if (string4 == null || string4.length() == 0) {
                    string4 = getFileLoader().getPathToMessage(this.selectedObject.messageOwner).toString();
                }
                MediaController.saveFile(string4, getParentActivity(), this.selectedObject.isMusic() ? 3 : 2, documentFileName, this.selectedObject.getDocument() != null ? this.selectedObject.getDocument().mime_type : _UrlKt.FRAGMENT_ENCODE_SET);
                break;
                break;
            case 11:
                MessagesController.getInstance(this.currentAccount).saveGif(this.selectedObject, messageObject.getDocument());
                break;
            case 15:
                Bundle bundle = new Bundle();
                bundle.putLong("user_id", this.selectedObject.messageOwner.media.user_id);
                bundle.putString("phone", this.selectedObject.messageOwner.media.phone_number);
                bundle.putBoolean("addContact", true);
                presentFragment(new ContactAddActivity(bundle));
                break;
            case 16:
                AndroidUtilities.addToClipboard(messageObject.messageOwner.media.phone_number);
                BulletinFactory.of(this).createCopyBulletin(LocaleController.getString(R.string.PhoneCopied)).show();
                break;
            case 17:
                try {
                    Intent intent2 = new Intent("android.intent.action.DIAL", Uri.parse("tel:" + this.selectedObject.messageOwner.media.phone_number));
                    intent2.addFlags(268435456);
                    getParentActivity().startActivityForResult(intent2, 500);
                } catch (Exception e) {
                    FileLog.e(e);
                }
                break;
            case 18:
                final int id = messageObject.getId();
                final ChatActivity chatActivity = this.chatActivity;
                AndroidUtilities.runOnUIThread(new Runnable() { // from class: com.radolyn.ayugram.ui.AyuMessageHistory$$ExternalSyntheticLambda19
                    @Override // java.lang.Runnable
                    public final void run() {
                        this.f$0.lambda$processSelectedOption$16(chatActivity, id);
                    }
                }, 300L);
                finishPreviewFragment();
                break;
            case 19:
                getAyuMessagesController().wrapDelete().deleteMessage(getDialogId(), this.selectedObject.getId()).commit();
                int iIndexOf = this.messages.indexOf(this.selectedObject);
                if (iIndexOf >= 0) {
                    ArrayList arrayList2 = (ArrayList) this.messagesByDays.get(getDateKey(this.selectedObject));
                    if (arrayList2 != null && arrayList2.size() == 1) {
                        i2 = 1;
                    }
                    this.messages.remove(iIndexOf);
                    if (arrayList2 != null) {
                        arrayList2.remove(this.selectedObject);
                    }
                    this.chatAdapter.notifyItemRemoved(this.messages.size() - (iIndexOf - this.chatAdapter.messagesStartRow));
                    if (i2 != 0) {
                        this.messages.remove(iIndexOf);
                        this.chatAdapter.notifyItemRemoved(this.messages.size() - (iIndexOf - this.chatAdapter.messagesStartRow));
                    }
                    fillMessagesCount();
                }
                break;
            case 20:
                final ChatActivity chatActivity2 = this.chatActivity;
                AndroidUtilities.runOnUIThread(new Runnable() { // from class: com.radolyn.ayugram.ui.AyuMessageHistory$$ExternalSyntheticLambda20
                    @Override // java.lang.Runnable
                    public final void run() {
                        this.f$0.lambda$processSelectedOption$17(messageObject, chatActivity2);
                    }
                }, 300L);
                finishPreviewFragment();
                break;
            case 21:
                this.chatActivity.forwardingMessage = messageObject;
                messageObject.messageOwner.ayuDeleted = true;
                Bundle bundle2 = new Bundle();
                bundle2.putBoolean("onlySelect", true);
                bundle2.putInt("dialogsType", 3);
                bundle2.putInt("messagesCount", 1);
                bundle2.putInt("hasPoll", 0);
                if (ChatObject.isMonoForum(this.currentChat) && ChatObject.canManageMonoForum(this.currentAccount, this.currentChat)) {
                    long j = this.currentChat.linked_monoforum_id;
                    if (j != 0) {
                        bundle2.putLong("forward_into_channel", -j);
                    }
                }
                bundle2.putBoolean("hasInvoice", false);
                bundle2.putBoolean("canSelectTopics", true);
                DialogsActivity dialogsActivity = new DialogsActivity(bundle2);
                dialogsActivity.setDelegate(this.chatActivity);
                presentFragment(dialogsActivity, true);
                break;
        }
        if (i != 204) {
            closeMenu();
            this.selectedObject = null;
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$processSelectedOption$16(ChatActivity chatActivity, int i) {
        finishFragment();
        chatActivity.scrollToMessageId(i, 0, true, 0, true, 0);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$processSelectedOption$17(MessageObject messageObject, ChatActivity chatActivity) {
        finishFragment();
        messageObject.messageOwner.ayuDeleted = true;
        chatActivity.showFieldPanelForReply(messageObject);
    }

    private int getMessageType(MessageObject messageObject) {
        int i;
        String str;
        if (messageObject == null || (i = messageObject.type) == 6) {
            return -1;
        }
        if (i == 10 || i == 11 || i == 16) {
            return messageObject.getId() == 0 ? -1 : 1;
        }
        if (messageObject.isVoice()) {
            return 2;
        }
        if (messageObject.isSticker() || messageObject.isAnimatedSticker()) {
            TLRPC.InputStickerSet inputStickerSet = messageObject.getInputStickerSet();
            if (inputStickerSet instanceof TLRPC.TL_inputStickerSetID) {
                if (!MediaDataController.getInstance(this.currentAccount).isStickerPackInstalled(inputStickerSet.id)) {
                    return 7;
                }
            } else if ((inputStickerSet instanceof TLRPC.TL_inputStickerSetShortName) && !MediaDataController.getInstance(this.currentAccount).isStickerPackInstalled(inputStickerSet.short_name)) {
                return 7;
            }
        } else if ((!messageObject.isRoundVideo() || (messageObject.isRoundVideo() && BuildVars.DEBUG_VERSION)) && ((messageObject.messageOwner.media instanceof TLRPC.TL_messageMediaPhoto) || messageObject.getDocument() != null || messageObject.isMusic() || messageObject.isVideo())) {
            String str2 = messageObject.messageOwner.attachPath;
            boolean z = (str2 == null || str2.length() == 0 || !new File(messageObject.messageOwner.attachPath).exists()) ? false : true;
            if ((z || !getFileLoader().getPathToMessage(messageObject.messageOwner).exists()) ? z : true) {
                if (messageObject.getDocument() == null || (str = messageObject.getDocument().mime_type) == null) {
                    return 4;
                }
                if (messageObject.getDocumentName().toLowerCase().endsWith("attheme")) {
                    return 10;
                }
                if (str.endsWith("/xml")) {
                    return 5;
                }
                return (str.endsWith("/png") || str.endsWith("/jpg") || str.endsWith("/jpeg")) ? 6 : 4;
            }
        } else {
            if (messageObject.type == 12) {
                return 8;
            }
            if (messageObject.isMediaEmpty()) {
                return 3;
            }
        }
        return 2;
    }

    @Override // org.telegram.ui.ActionBar.BaseFragment
    public void onRemoveFromParent() {
        MediaController.getInstance().setTextureView(this.videoTextureView, null, null, false);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void hideFloatingDateView(boolean z) {
        if (this.floatingDateView.getTag() == null || this.currentFloatingDateOnScreen) {
            return;
        }
        if (!this.scrollingFloatingDate || this.currentFloatingTopIsNotMessage) {
            this.floatingDateView.setTag(null);
            if (z) {
                AnimatorSet animatorSet = new AnimatorSet();
                this.floatingDateAnimation = animatorSet;
                animatorSet.setDuration(150L);
                this.floatingDateAnimation.playTogether(ObjectAnimator.ofFloat(this.floatingDateView, "alpha", 0.0f));
                this.floatingDateAnimation.addListener(new AnimatorListenerAdapter() { // from class: com.radolyn.ayugram.ui.AyuMessageHistory.16
                    @Override // android.animation.AnimatorListenerAdapter, android.animation.Animator.AnimatorListener
                    public void onAnimationEnd(Animator animator) {
                        if (animator.equals(AyuMessageHistory.this.floatingDateAnimation)) {
                            AyuMessageHistory.this.floatingDateAnimation = null;
                        }
                    }
                });
                this.floatingDateAnimation.setStartDelay(500L);
                this.floatingDateAnimation.start();
                return;
            }
            AnimatorSet animatorSet2 = this.floatingDateAnimation;
            if (animatorSet2 != null) {
                animatorSet2.cancel();
                this.floatingDateAnimation = null;
            }
            this.floatingDateView.setAlpha(0.0f);
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void checkScrollForLoad(boolean z) {
        LinearLayoutManager linearLayoutManager = this.chatLayoutManager;
        if (linearLayoutManager == null || this.paused) {
            return;
        }
        int iFindFirstVisibleItemPosition = linearLayoutManager.findFirstVisibleItemPosition();
        if ((iFindFirstVisibleItemPosition == -1 ? 0 : Math.abs(this.chatLayoutManager.findLastVisibleItemPosition() - iFindFirstVisibleItemPosition) + 1) > 0) {
            if (iFindFirstVisibleItemPosition > (z ? 25 : 5) || this.loading || this.endReached || this.scrollingToHighlight) {
                return;
            }
            loadMessages(false);
        }
    }

    private void updateTextureViewPosition() {
        boolean z;
        int childCount = this.chatListView.getChildCount();
        int i = 0;
        while (true) {
            if (i >= childCount) {
                z = false;
                break;
            }
            View childAt = this.chatListView.getChildAt(i);
            if (childAt instanceof ChatMessageCell) {
                ChatMessageCell chatMessageCell = (ChatMessageCell) childAt;
                MessageObject messageObject = chatMessageCell.getMessageObject();
                if (this.roundVideoContainer != null && messageObject.isRoundVideo() && MediaController.getInstance().isPlayingMessage(messageObject)) {
                    ImageReceiver photoImage = chatMessageCell.getPhotoImage();
                    this.roundVideoContainer.setTranslationX(photoImage.getImageX());
                    this.roundVideoContainer.setTranslationY(this.fragmentView.getPaddingTop() + chatMessageCell.getTop() + photoImage.getImageY());
                    this.fragmentView.invalidate();
                    this.roundVideoContainer.invalidate();
                    z = true;
                    break;
                }
            }
            i++;
        }
        if (this.roundVideoContainer != null) {
            MessageObject playingMessageObject = MediaController.getInstance().getPlayingMessageObject();
            if (!z) {
                this.roundVideoContainer.setTranslationY((-AndroidUtilities.roundMessageSize) - 100);
                this.fragmentView.invalidate();
                if (playingMessageObject == null || !playingMessageObject.isRoundVideo()) {
                    return;
                }
                if (this.checkTextureViewPosition || PipRoundVideoView.getInstance() != null) {
                    MediaController.getInstance().setCurrentVideoVisible(false);
                    return;
                }
                return;
            }
            MediaController.getInstance().setCurrentVideoVisible(true);
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void updateMessagesVisiblePart() {
        MessageObject messageObject;
        RecyclerListView recyclerListView = this.chatListView;
        if (recyclerListView == null) {
            return;
        }
        int childCount = recyclerListView.getChildCount();
        int measuredHeight = this.chatListView.getMeasuredHeight();
        int i = Integer.MAX_VALUE;
        boolean z = false;
        int i2 = Integer.MAX_VALUE;
        boolean z2 = false;
        View view = null;
        View view2 = null;
        View view3 = null;
        for (int i3 = 0; i3 < childCount; i3++) {
            View childAt = this.chatListView.getChildAt(i3);
            if (childAt instanceof ChatMessageCell) {
                ChatMessageCell chatMessageCell = (ChatMessageCell) childAt;
                int top = chatMessageCell.getTop();
                chatMessageCell.getBottom();
                int i4 = top >= 0 ? 0 : -top;
                int measuredHeight2 = chatMessageCell.getMeasuredHeight();
                if (measuredHeight2 > measuredHeight) {
                    measuredHeight2 = i4 + measuredHeight;
                }
                chatMessageCell.setVisiblePart(i4, measuredHeight2 - i4, this.contentView.getHeightWithKeyboard() - this.chatListView.getTop(), 0.0f, (childAt.getY() + this.actionBar.getMeasuredHeight()) - this.contentView.getBackgroundTranslationY(), this.contentView.getMeasuredWidth(), this.contentView.getBackgroundSizeY(), 0, 0, 0);
                chatMessageCell.invalidate();
                MessageObject messageObject2 = chatMessageCell.getMessageObject();
                if (this.roundVideoContainer != null && messageObject2.isRoundVideo() && MediaController.getInstance().isPlayingMessage(messageObject2)) {
                    ImageReceiver photoImage = chatMessageCell.getPhotoImage();
                    this.roundVideoContainer.setTranslationX(photoImage.getImageX());
                    this.roundVideoContainer.setTranslationY(this.fragmentView.getPaddingTop() + top + photoImage.getImageY());
                    this.fragmentView.invalidate();
                    this.roundVideoContainer.invalidate();
                    z2 = true;
                }
            } else if (childAt instanceof ChatActionCell) {
                ChatActionCell chatActionCell = (ChatActionCell) childAt;
                chatActionCell.setVisiblePart((childAt.getY() + this.actionBar.getMeasuredHeight()) - this.contentView.getBackgroundTranslationY(), this.contentView.getBackgroundSizeY());
                if (chatActionCell.hasGradientService()) {
                    chatActionCell.invalidate();
                }
            }
            if (childAt.getBottom() > this.chatListView.getPaddingTop()) {
                int bottom = childAt.getBottom();
                if (bottom < i) {
                    if ((childAt instanceof ChatMessageCell) || (childAt instanceof ChatActionCell)) {
                        view = childAt;
                    }
                    i = bottom;
                    view3 = childAt;
                }
                ChatListItemAnimator chatListItemAnimator = this.chatListItemAnimator;
                if ((chatListItemAnimator == null || (!chatListItemAnimator.willRemoved(childAt) && !this.chatListItemAnimator.willAddedFromAlpha(childAt))) && (childAt instanceof ChatActionCell) && ((ChatActionCell) childAt).getMessageObject().isDateObject) {
                    if (childAt.getAlpha() != 1.0f) {
                        childAt.setAlpha(1.0f);
                    }
                    if (bottom < i2) {
                        i2 = bottom;
                        view2 = childAt;
                    }
                }
            }
        }
        FrameLayout frameLayout = this.roundVideoContainer;
        if (frameLayout != null) {
            if (!z2) {
                frameLayout.setTranslationY((-AndroidUtilities.roundMessageSize) - 100);
                this.fragmentView.invalidate();
                MessageObject playingMessageObject = MediaController.getInstance().getPlayingMessageObject();
                if (playingMessageObject != null && playingMessageObject.isRoundVideo() && this.checkTextureViewPosition) {
                    MediaController.getInstance().setCurrentVideoVisible(false);
                }
            } else {
                MediaController.getInstance().setCurrentVideoVisible(true);
            }
        }
        if (view != null) {
            if (view instanceof ChatMessageCell) {
                messageObject = ((ChatMessageCell) view).getMessageObject();
            } else {
                messageObject = ((ChatActionCell) view).getMessageObject();
            }
            this.floatingDateView.setCustomDate(messageObject.messageOwner.date, false, true);
        }
        this.currentFloatingDateOnScreen = false;
        if (!(view3 instanceof ChatMessageCell) && !(view3 instanceof ChatActionCell)) {
            z = true;
        }
        this.currentFloatingTopIsNotMessage = z;
        if (view2 != null) {
            if (view2.getTop() > this.chatListView.getPaddingTop() || this.currentFloatingTopIsNotMessage) {
                if (view2.getAlpha() != 1.0f) {
                    view2.setAlpha(1.0f);
                }
                hideFloatingDateView(true ^ this.currentFloatingTopIsNotMessage);
            } else {
                if (view2.getAlpha() != 0.0f) {
                    view2.setAlpha(0.0f);
                }
                AnimatorSet animatorSet = this.floatingDateAnimation;
                if (animatorSet != null) {
                    animatorSet.cancel();
                    this.floatingDateAnimation = null;
                }
                if (this.floatingDateView.getTag() == null) {
                    this.floatingDateView.setTag(1);
                }
                if (this.floatingDateView.getAlpha() != 1.0f) {
                    this.floatingDateView.setAlpha(1.0f);
                }
                this.currentFloatingDateOnScreen = true;
            }
            int bottom2 = view2.getBottom() - this.chatListView.getPaddingTop();
            if (bottom2 > this.floatingDateView.getMeasuredHeight() && bottom2 < this.floatingDateView.getMeasuredHeight() * 2) {
                ChatActionCell chatActionCell2 = this.floatingDateView;
                chatActionCell2.setTranslationY(((-chatActionCell2.getMeasuredHeight()) * 2) + bottom2);
                return;
            } else {
                this.floatingDateView.setTranslationY(0.0f);
                return;
            }
        }
        hideFloatingDateView(true);
        this.floatingDateView.setTranslationY(0.0f);
    }

    private void updatePagedownButtonPosition() {
        ChatActivitySideControlsButtonsLayout chatActivitySideControlsButtonsLayout = this.searchButtonLayout;
        if (chatActivitySideControlsButtonsLayout == null) {
            return;
        }
        chatActivitySideControlsButtonsLayout.setTranslationY(-this.windowInsetsStateHolder.getAnimatedMaxBottomInset());
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void updatePagedownButtonVisibility(boolean z) {
        if (this.searchButtonLayout == null || this.chatListView == null) {
            return;
        }
        boolean z2 = false;
        boolean z3 = !this.messages.isEmpty() && this.chatListView.canScrollVertically(1);
        if (!z3) {
            this.pagedownHidden = false;
        }
        if (z3 && !this.pagedownHidden) {
            z2 = true;
        }
        this.searchButtonLayout.showButton(1, z2, z);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void highlightMessageOnScreen(int i) {
        ChatMessageCell chatMessageCell;
        MessageObject messageObject;
        for (int i2 = 0; i2 < this.chatListView.getChildCount(); i2++) {
            View childAt = this.chatListView.getChildAt(i2);
            if ((childAt instanceof ChatMessageCell) && (messageObject = (chatMessageCell = (ChatMessageCell) childAt).getMessageObject()) != null && messageObject.getId() == i) {
                chatMessageCell.setHighlighted(true);
                startMessageUnhighlight();
                return;
            }
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void startMessageUnhighlight() {
        Runnable runnable = this.unselectRunnable;
        if (runnable != null) {
            AndroidUtilities.cancelRunOnUIThread(runnable);
        }
        Runnable runnable2 = new Runnable() { // from class: com.radolyn.ayugram.ui.AyuMessageHistory$$ExternalSyntheticLambda18
            @Override // java.lang.Runnable
            public final void run() {
                this.f$0.lambda$startMessageUnhighlight$18();
            }
        };
        this.unselectRunnable = runnable2;
        AndroidUtilities.runOnUIThread(runnable2, 1000L);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$startMessageUnhighlight$18() {
        this.highlightMessageId = Integer.MAX_VALUE;
        for (int i = 0; i < this.chatListView.getChildCount(); i++) {
            View childAt = this.chatListView.getChildAt(i);
            if (childAt instanceof ChatMessageCell) {
                ((ChatMessageCell) childAt).setHighlighted(false);
            }
        }
        this.unselectRunnable = null;
    }

    private void onPageDownClicked() {
        if (this.chatListView == null || this.chatAdapter == null || this.messages.isEmpty()) {
            return;
        }
        this.pagedownHidden = true;
        updatePagedownButtonVisibility(true);
        this.chatScrollHelper.setScrollDirection(0);
        this.chatScrollHelper.scrollToPosition(this.messages.size() - 1, 0, true, true);
    }

    @Override // org.telegram.ui.ActionBar.BaseFragment
    public void onTransitionAnimationStart(boolean z, boolean z2) {
        if (z) {
            this.notificationsLocker.lock();
        }
    }

    @Override // org.telegram.ui.ActionBar.BaseFragment
    public void onTransitionAnimationEnd(boolean z, boolean z2) {
        if (z) {
            this.notificationsLocker.unlock();
        }
    }

    @Override // org.telegram.ui.ActionBar.BaseFragment
    public void onResume() {
        super.onResume();
        SizeNotifierFrameLayout sizeNotifierFrameLayout = this.contentView;
        if (sizeNotifierFrameLayout != null) {
            sizeNotifierFrameLayout.onResume();
        }
        this.paused = false;
        checkScrollForLoad(false);
        if (this.wasPaused) {
            this.wasPaused = false;
            ChatActivityAdapter chatActivityAdapter = this.chatAdapter;
            if (chatActivityAdapter != null) {
                chatActivityAdapter.notifyDataSetChanged();
            }
        }
        Bulletin.addDelegate(this, new Bulletin.Delegate() { // from class: com.radolyn.ayugram.ui.AyuMessageHistory.17
            @Override // org.telegram.ui.Components.Bulletin.Delegate
            public /* synthetic */ boolean allowLayoutChanges() {
                return Bulletin.Delegate.CC.$default$allowLayoutChanges(this);
            }

            @Override // org.telegram.ui.Components.Bulletin.Delegate
            public /* synthetic */ boolean bottomOffsetAnimated() {
                return Bulletin.Delegate.CC.$default$bottomOffsetAnimated(this);
            }

            @Override // org.telegram.ui.Components.Bulletin.Delegate
            public /* synthetic */ boolean clipWithGradient(int i) {
                return Bulletin.Delegate.CC.$default$clipWithGradient(this, i);
            }

            @Override // org.telegram.ui.Components.Bulletin.Delegate
            public /* synthetic */ int getTopOffset(int i) {
                return Bulletin.Delegate.CC.$default$getTopOffset(this, i);
            }

            @Override // org.telegram.ui.Components.Bulletin.Delegate
            public /* synthetic */ void onBottomOffsetChange(float f) {
                Bulletin.Delegate.CC.$default$onBottomOffsetChange(this, f);
            }

            @Override // org.telegram.ui.Components.Bulletin.Delegate
            public /* synthetic */ void onHide(Bulletin bulletin) {
                Bulletin.Delegate.CC.$default$onHide(this, bulletin);
            }

            @Override // org.telegram.ui.Components.Bulletin.Delegate
            public /* synthetic */ void onShow(Bulletin bulletin) {
                Bulletin.Delegate.CC.$default$onShow(this, bulletin);
            }

            @Override // org.telegram.ui.Components.Bulletin.Delegate
            public int getBottomOffset(int i) {
                return AyuMessageHistory.this.windowInsetsStateHolder.getCurrentNavigationBarInset();
            }
        });
    }

    @Override // org.telegram.ui.ActionBar.BaseFragment
    public void onPause() {
        super.onPause();
        dimBehindView(null, false);
        SizeNotifierFrameLayout sizeNotifierFrameLayout = this.contentView;
        if (sizeNotifierFrameLayout != null) {
            sizeNotifierFrameLayout.onPause();
        }
        UndoView undoView = this.undoView;
        if (undoView != null) {
            undoView.hide(true, 0);
        }
        this.paused = true;
        this.wasPaused = true;
        if (AvatarPreviewer.hasVisibleInstance()) {
            AvatarPreviewer.getInstance().close();
        }
        Bulletin.removeDelegate(this);
    }

    @Override // org.telegram.ui.ActionBar.BaseFragment
    public void onBecomeFullyHidden() {
        UndoView undoView = this.undoView;
        if (undoView != null) {
            undoView.hide(true, 0);
        }
    }

    public void openVCard(TLRPC.User user, String str, String str2, String str3) {
        try {
            File sharingDirectory = AndroidUtilities.getSharingDirectory();
            sharingDirectory.mkdirs();
            File file = new File(sharingDirectory, "vcard.vcf");
            BufferedWriter bufferedWriter = new BufferedWriter(new FileWriter(file));
            bufferedWriter.write(str);
            bufferedWriter.close();
            try {
                showDialog(new PhonebookShareAlert(this, null, user, null, file, str2, str3));
            } catch (Exception e) {
                e = e;
                FileLog.e(e);
            }
        } catch (Exception e2) {
            e = e2;
        }
    }

    @Override // org.telegram.ui.ActionBar.BaseFragment
    public void onConfigurationChanged(Configuration configuration) {
        Dialog dialog = this.visibleDialog;
        if (dialog instanceof DatePickerDialog) {
            dialog.dismiss();
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void alertUserOpenError(MessageObject messageObject) {
        if (getParentActivity() == null) {
            return;
        }
        AlertDialog.Builder builder = new AlertDialog.Builder(getParentActivity());
        builder.setTitle(LocaleController.getString(R.string.AppName));
        builder.setPositiveButton(LocaleController.getString(R.string.OK), null);
        if (messageObject.type == 3) {
            builder.setMessage(LocaleController.getString(R.string.NoPlayerInstalled));
        } else {
            builder.setMessage(LocaleController.formatString(R.string.NoHandleAppInstalled, messageObject.getDocument().mime_type));
        }
        showDialog(builder.create());
    }

    public void showOpenUrlAlert(final String str, boolean z) {
        if (Browser.isInternalUrl(str, null) || !z) {
            Browser.openUrl((Context) getParentActivity(), str, true);
            return;
        }
        AlertDialog.Builder builder = new AlertDialog.Builder(getParentActivity());
        builder.setTitle(LocaleController.getString(R.string.OpenUrlTitle));
        builder.setMessage(LocaleController.formatString(R.string.OpenUrlAlert2, str));
        builder.setPositiveButton(LocaleController.getString(R.string.Open), new AlertDialog.OnButtonClickListener() { // from class: com.radolyn.ayugram.ui.AyuMessageHistory$$ExternalSyntheticLambda17
            @Override // org.telegram.ui.ActionBar.AlertDialog.OnButtonClickListener
            public final void onClick(AlertDialog alertDialog, int i) {
                this.f$0.lambda$showOpenUrlAlert$19(str, alertDialog, i);
            }
        });
        builder.setNegativeButton(LocaleController.getString(R.string.Cancel), null);
        showDialog(builder.create());
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$showOpenUrlAlert$19(String str, AlertDialog alertDialog, int i) {
        Browser.openUrl((Context) getParentActivity(), str, true);
    }

    static final class AyuLoadedMessage extends RecordTag {
        private final int dateKey;
        private final long key;
        private final MessageObject messageObject;

        private /* synthetic */ boolean $record$equals(Object obj) {
            if (!(obj instanceof AyuLoadedMessage)) {
                return false;
            }
            AyuLoadedMessage ayuLoadedMessage = (AyuLoadedMessage) obj;
            return this.dateKey == ayuLoadedMessage.dateKey && this.key == ayuLoadedMessage.key && Objects.equals(this.messageObject, ayuLoadedMessage.messageObject);
        }

        private /* synthetic */ Object[] $record$getFieldsAsObjects() {
            return new Object[]{this.messageObject, Long.valueOf(this.key), Integer.valueOf(this.dateKey)};
        }

        public final boolean equals(Object obj) {
            return $record$equals(obj);
        }

        public final int hashCode() {
            return AyuMessageHistory$AyuLoadedMessage$$ExternalSyntheticRecord0.m(this.dateKey, this.key, this.messageObject);
        }

        public final String toString() {
            return GenerateFromMessageBottomSheet$GenerationData$$ExternalSyntheticRecord0.m($record$getFieldsAsObjects(), AyuLoadedMessage.class, "messageObject;key;dateKey");
        }

        AyuLoadedMessage(MessageObject messageObject, long j, int i) {
            this.messageObject = messageObject;
            this.key = j;
            this.dateKey = i;
            messageObject.skipAyuFiltering = true;
        }
    }

    public class ChatActivityAdapter extends RecyclerView.Adapter {
        private int loadingUpRow;
        private final Context mContext;
        private int messagesEndRow;
        private int messagesStartRow;
        private int progressDialogAtMessageId;
        private int progressDialogAtMessageType;
        private String progressDialogBotButtonUrl;
        private Browser.Progress progressDialogCurrent;
        private CharacterStyle progressDialogLinkSpan;
        private int rowCount;

        @Override // androidx.recyclerview.widget.RecyclerView.Adapter
        public long getItemId(int i) {
            return -1L;
        }

        public ChatActivityAdapter(Context context) {
            this.mContext = context;
        }

        public void updateRows() {
            this.rowCount = 0;
            if (!AyuMessageHistory.this.messages.isEmpty()) {
                if (!AyuMessageHistory.this.endReached) {
                    int i = this.rowCount;
                    this.rowCount = i + 1;
                    this.loadingUpRow = i;
                } else {
                    this.loadingUpRow = -1;
                }
                int i2 = this.rowCount;
                this.messagesStartRow = i2;
                int size = i2 + AyuMessageHistory.this.messages.size();
                this.rowCount = size;
                this.messagesEndRow = size;
                return;
            }
            this.loadingUpRow = -1;
            this.messagesStartRow = -1;
            this.messagesEndRow = -1;
        }

        @Override // androidx.recyclerview.widget.RecyclerView.Adapter
        public int getItemCount() {
            return this.rowCount;
        }

        public MessageObject getMessageObject(int i) {
            if (i < this.messagesStartRow || i >= this.messagesEndRow) {
                return null;
            }
            ArrayList arrayList = AyuMessageHistory.this.messages;
            return (MessageObject) arrayList.get((arrayList.size() - (i - this.messagesStartRow)) - 1);
        }

        /* JADX INFO: Access modifiers changed from: private */
        public void resetProgressDialogLoading() {
            this.progressDialogLinkSpan = null;
            this.progressDialogAtMessageId = 0;
            this.progressDialogAtMessageType = -1;
            this.progressDialogBotButtonUrl = null;
            this.progressDialogCurrent = null;
        }

        /* JADX INFO: Access modifiers changed from: private */
        public Browser.Progress makeProgressForLink(ChatMessageCell chatMessageCell, CharacterStyle characterStyle) {
            Browser.Progress progress = this.progressDialogCurrent;
            if (progress != null) {
                progress.cancel(true);
                this.progressDialogCurrent = null;
            }
            if (characterStyle == null || chatMessageCell == null || chatMessageCell.getMessageObject() == null) {
                this.progressDialogCurrent = null;
                return null;
            }
            AnonymousClass1 anonymousClass1 = new AnonymousClass1(chatMessageCell.getMessageObject().getId(), characterStyle, chatMessageCell);
            this.progressDialogCurrent = anonymousClass1;
            return anonymousClass1;
        }

        /* JADX INFO: renamed from: com.radolyn.ayugram.ui.AyuMessageHistory$ChatActivityAdapter$1, reason: invalid class name */
        class AnonymousClass1 extends Browser.Progress {
            final /* synthetic */ ChatMessageCell val$cell;
            final /* synthetic */ int val$id;
            final /* synthetic */ CharacterStyle val$span;

            AnonymousClass1(int i, CharacterStyle characterStyle, ChatMessageCell chatMessageCell) {
                this.val$id = i;
                this.val$span = characterStyle;
                this.val$cell = chatMessageCell;
            }

            @Override // org.telegram.messenger.browser.Browser.Progress
            public void init() {
                ChatActivityAdapter.this.progressDialogAtMessageId = this.val$id;
                ChatActivityAdapter.this.progressDialogAtMessageType = 1;
                ChatActivityAdapter.this.progressDialogLinkSpan = this.val$span;
                this.val$cell.invalidate();
            }

            @Override // org.telegram.messenger.browser.Browser.Progress
            public void end(boolean z) {
                if (z) {
                    return;
                }
                final int i = this.val$id;
                AndroidUtilities.runOnUIThread(new Runnable() { // from class: com.radolyn.ayugram.ui.AyuMessageHistory$ChatActivityAdapter$1$$ExternalSyntheticLambda0
                    @Override // java.lang.Runnable
                    public final void run() {
                        this.f$0.lambda$end$0(i);
                    }
                }, 240L);
            }

            /* JADX INFO: Access modifiers changed from: private */
            public /* synthetic */ void lambda$end$0(int i) {
                if (ChatActivityAdapter.this.progressDialogAtMessageId == i) {
                    ChatActivityAdapter.this.resetProgressDialogLoading();
                }
            }
        }

        @Override // androidx.recyclerview.widget.RecyclerView.Adapter
        public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
            View chatLoadingCell;
            View chatMessageCell;
            boolean z = false;
            if (i == 0) {
                if (!AyuMessageHistory.this.chatMessageCellsCache.isEmpty()) {
                    View view = (View) AyuMessageHistory.this.chatMessageCellsCache.get(0);
                    AyuMessageHistory.this.chatMessageCellsCache.remove(0);
                    chatMessageCell = view;
                } else {
                    chatMessageCell = new ChatMessageCell(this.mContext, ((BaseFragment) AyuMessageHistory.this).currentAccount);
                }
                ChatMessageCell chatMessageCell2 = (ChatMessageCell) chatMessageCell;
                chatMessageCell2.setIgnoreDeletedAlpha(true);
                chatMessageCell2.setResourcesProvider(AyuMessageHistory.this.theme);
                chatMessageCell2.setDelegate(new AnonymousClass2());
                chatMessageCell2.setAllowAssistant(true);
                chatLoadingCell = chatMessageCell;
            } else if (i == 1) {
                ChatActionCell chatActionCell = new ChatActionCell(this.mContext, z, AyuMessageHistory.this.theme) { // from class: com.radolyn.ayugram.ui.AyuMessageHistory.ChatActivityAdapter.3
                    @Override // org.telegram.ui.Cells.ChatActionCell, android.view.View
                    public void onInitializeAccessibilityNodeInfo(AccessibilityNodeInfo accessibilityNodeInfo) {
                        super.onInitializeAccessibilityNodeInfo(accessibilityNodeInfo);
                        accessibilityNodeInfo.setVisibleToUser(true);
                    }
                };
                chatActionCell.setDelegate(new ChatActionCell.ChatActionCellDelegate() { // from class: com.radolyn.ayugram.ui.AyuMessageHistory.ChatActivityAdapter.4
                    @Override // org.telegram.ui.Cells.ChatActionCell.ChatActionCellDelegate
                    public /* synthetic */ boolean canDrawOutboundsContent() {
                        return ChatActionCell.ChatActionCellDelegate.CC.$default$canDrawOutboundsContent(this);
                    }

                    @Override // org.telegram.ui.Cells.ChatActionCell.ChatActionCellDelegate
                    public /* synthetic */ void didClickButton(ChatActionCell chatActionCell2) {
                        ChatActionCell.ChatActionCellDelegate.CC.$default$didClickButton(this, chatActionCell2);
                    }

                    @Override // org.telegram.ui.Cells.ChatActionCell.ChatActionCellDelegate
                    public /* synthetic */ void didOpenPremiumGift(ChatActionCell chatActionCell2, TLRPC.TL_premiumGiftOption tL_premiumGiftOption, String str, boolean z2) {
                        ChatActionCell.ChatActionCellDelegate.CC.$default$didOpenPremiumGift(this, chatActionCell2, tL_premiumGiftOption, str, z2);
                    }

                    @Override // org.telegram.ui.Cells.ChatActionCell.ChatActionCellDelegate
                    public /* synthetic */ void didOpenPremiumGiftChannel(ChatActionCell chatActionCell2, String str, boolean z2) {
                        ChatActionCell.ChatActionCellDelegate.CC.$default$didOpenPremiumGiftChannel(this, chatActionCell2, str, z2);
                    }

                    @Override // org.telegram.ui.Cells.ChatActionCell.ChatActionCellDelegate
                    public /* synthetic */ void didPressReaction(ChatActionCell chatActionCell2, TLRPC.ReactionCount reactionCount, boolean z2, float f, float f2) {
                        ChatActionCell.ChatActionCellDelegate.CC.$default$didPressReaction(this, chatActionCell2, reactionCount, z2, f, f2);
                    }

                    @Override // org.telegram.ui.Cells.ChatActionCell.ChatActionCellDelegate
                    public void didPressReplyMessage(ChatActionCell chatActionCell2, int i2) {
                    }

                    @Override // org.telegram.ui.Cells.ChatActionCell.ChatActionCellDelegate
                    public /* synthetic */ void didPressTaskLink(ChatActionCell chatActionCell2, int i2, int i3) {
                        ChatActionCell.ChatActionCellDelegate.CC.$default$didPressTaskLink(this, chatActionCell2, i2, i3);
                    }

                    @Override // org.telegram.ui.Cells.ChatActionCell.ChatActionCellDelegate
                    public /* synthetic */ void forceUpdate(ChatActionCell chatActionCell2, boolean z2) {
                        ChatActionCell.ChatActionCellDelegate.CC.$default$forceUpdate(this, chatActionCell2, z2);
                    }

                    @Override // org.telegram.ui.Cells.ChatActionCell.ChatActionCellDelegate
                    public /* synthetic */ long getTopicId() {
                        return ChatActionCell.ChatActionCellDelegate.CC.$default$getTopicId(this);
                    }

                    @Override // org.telegram.ui.Cells.ChatActionCell.ChatActionCellDelegate
                    public /* synthetic */ void needOpenInviteLink(TLRPC.TL_chatInviteExported tL_chatInviteExported) {
                        ChatActionCell.ChatActionCellDelegate.CC.$default$needOpenInviteLink(this, tL_chatInviteExported);
                    }

                    @Override // org.telegram.ui.Cells.ChatActionCell.ChatActionCellDelegate
                    public /* synthetic */ void needShowEffectOverlay(ChatActionCell chatActionCell2, TLRPC.Document document, TLRPC.VideoSize videoSize) {
                        ChatActionCell.ChatActionCellDelegate.CC.$default$needShowEffectOverlay(this, chatActionCell2, document, videoSize);
                    }

                    @Override // org.telegram.ui.Cells.ChatActionCell.ChatActionCellDelegate
                    public /* synthetic */ void onTopicClick(ChatActionCell chatActionCell2) {
                        ChatActionCell.ChatActionCellDelegate.CC.$default$onTopicClick(this, chatActionCell2);
                    }

                    @Override // org.telegram.ui.Cells.ChatActionCell.ChatActionCellDelegate
                    public void didClickImage(ChatActionCell chatActionCell2) {
                        MessageObject messageObject = chatActionCell2.getMessageObject();
                        PhotoViewer.getInstance().setParentActivity(AyuMessageHistory.this);
                        TLRPC.PhotoSize closestPhotoSizeWithSize = FileLoader.getClosestPhotoSizeWithSize(messageObject.photoThumbs, 640);
                        if (closestPhotoSizeWithSize != null) {
                            PhotoViewer.getInstance().openPhoto(closestPhotoSizeWithSize.location, ImageLocation.getForPhoto(closestPhotoSizeWithSize, messageObject.messageOwner.action.photo), AyuMessageHistory.this.provider);
                        } else {
                            PhotoViewer.getInstance().openPhoto(messageObject, (ChatActivity) null, 0L, 0L, 0L, AyuMessageHistory.this.provider);
                        }
                    }

                    @Override // org.telegram.ui.Cells.ChatActionCell.ChatActionCellDelegate
                    public boolean didLongPress(ChatActionCell chatActionCell2, float f, float f2) {
                        return AyuMessageHistory.this.createMenu(chatActionCell2);
                    }

                    @Override // org.telegram.ui.Cells.ChatActionCell.ChatActionCellDelegate
                    public void needOpenUserProfile(long j) {
                        if (j < 0) {
                            Bundle bundle = new Bundle();
                            bundle.putLong("chat_id", -j);
                            if (MessagesController.getInstance(((BaseFragment) AyuMessageHistory.this).currentAccount).checkCanOpenChat(bundle, AyuMessageHistory.this)) {
                                AyuMessageHistory.this.presentFragment(new ChatActivity(bundle), true);
                                return;
                            }
                            return;
                        }
                        if (j != UserConfig.getInstance(((BaseFragment) AyuMessageHistory.this).currentAccount).getClientUserId()) {
                            ProfileActivity profileActivity = new ProfileActivity(new Bundle());
                            profileActivity.setPlayProfileAnimation(0);
                            AyuMessageHistory.this.presentFragment(profileActivity);
                        }
                    }

                    @Override // org.telegram.ui.Cells.ChatActionCell.ChatActionCellDelegate
                    public BaseFragment getBaseFragment() {
                        return AyuMessageHistory.this;
                    }

                    @Override // org.telegram.ui.Cells.ChatActionCell.ChatActionCellDelegate
                    public long getDialogId() {
                        return AyuMessageHistory.this.getDialogId();
                    }
                });
                chatLoadingCell = chatActionCell;
            } else if (i == 2) {
                chatLoadingCell = new ChatUnreadCell(this.mContext, AyuMessageHistory.this.theme);
            } else {
                chatLoadingCell = new ChatLoadingCell(this.mContext, AyuMessageHistory.this.contentView, AyuMessageHistory.this.theme);
            }
            chatLoadingCell.setLayoutParams(new RecyclerView.LayoutParams(-1, -2));
            return new RecyclerListView.Holder(chatLoadingCell);
        }

        /* JADX INFO: renamed from: com.radolyn.ayugram.ui.AyuMessageHistory$ChatActivityAdapter$2, reason: invalid class name */
        class AnonymousClass2 implements ChatMessageCell.ChatMessageCellDelegate {
            @Override // org.telegram.ui.Cells.ChatMessageCell.ChatMessageCellDelegate
            public boolean canDrawOutboundsContent() {
                return false;
            }

            @Override // org.telegram.ui.Cells.ChatMessageCell.ChatMessageCellDelegate
            public boolean canPerformActions() {
                return true;
            }

            @Override // org.telegram.ui.Cells.ChatMessageCell.ChatMessageCellDelegate
            public boolean canPerformReply() {
                return true;
            }

            @Override // org.telegram.ui.Cells.ChatMessageCell.ChatMessageCellDelegate
            public /* synthetic */ void didLongPress(ChatMessageCell chatMessageCell, float f, float f2) {
                ChatMessageCell.ChatMessageCellDelegate.CC.$default$didLongPress(this, chatMessageCell, f, f2);
            }

            @Override // org.telegram.ui.Cells.ChatMessageCell.ChatMessageCellDelegate
            public /* synthetic */ boolean didLongPressChannelAvatar(ChatMessageCell chatMessageCell, TLRPC.Chat chat, int i, float f, float f2) {
                return ChatMessageCell.ChatMessageCellDelegate.CC.$default$didLongPressChannelAvatar(this, chatMessageCell, chat, i, f, f2);
            }

            @Override // org.telegram.ui.Cells.ChatMessageCell.ChatMessageCellDelegate
            public /* synthetic */ void didLongPressCustomBotButton(ChatMessageCell chatMessageCell, BotInlineKeyboard.ButtonCustom buttonCustom) {
                ChatMessageCell.ChatMessageCellDelegate.CC.$default$didLongPressCustomBotButton(this, chatMessageCell, buttonCustom);
            }

            @Override // org.telegram.ui.Cells.ChatMessageCell.ChatMessageCellDelegate
            public /* synthetic */ boolean didLongPressToDoButton(ChatMessageCell chatMessageCell, TLRPC.TodoItem todoItem) {
                return ChatMessageCell.ChatMessageCellDelegate.CC.$default$didLongPressToDoButton(this, chatMessageCell, todoItem);
            }

            @Override // org.telegram.ui.Cells.ChatMessageCell.ChatMessageCellDelegate
            public /* synthetic */ void didPressAboutRevenueSharingAds() {
                ChatMessageCell.ChatMessageCellDelegate.CC.$default$didPressAboutRevenueSharingAds(this);
            }

            @Override // org.telegram.ui.Cells.ChatMessageCell.ChatMessageCellDelegate
            public /* synthetic */ void didPressAdmin(ChatMessageCell chatMessageCell) {
                ChatMessageCell.ChatMessageCellDelegate.CC.$default$didPressAdmin(this, chatMessageCell);
            }

            @Override // org.telegram.ui.Cells.ChatMessageCell.ChatMessageCellDelegate
            public /* synthetic */ void didPressBoostCounter(ChatMessageCell chatMessageCell) {
                ChatMessageCell.ChatMessageCellDelegate.CC.$default$didPressBoostCounter(this, chatMessageCell);
            }

            @Override // org.telegram.ui.Cells.ChatMessageCell.ChatMessageCellDelegate
            public /* synthetic */ void didPressBotButton(ChatMessageCell chatMessageCell, TLRPC.KeyboardButton keyboardButton) {
                ChatMessageCell.ChatMessageCellDelegate.CC.$default$didPressBotButton(this, chatMessageCell, keyboardButton);
            }

            @Override // org.telegram.ui.Cells.ChatMessageCell.ChatMessageCellDelegate
            public /* synthetic */ void didPressCancelSendButton(ChatMessageCell chatMessageCell) {
                ChatMessageCell.ChatMessageCellDelegate.CC.$default$didPressCancelSendButton(this, chatMessageCell);
            }

            @Override // org.telegram.ui.Cells.ChatMessageCell.ChatMessageCellDelegate
            public /* synthetic */ void didPressChannelRecommendation(ChatMessageCell chatMessageCell, TLObject tLObject, boolean z) {
                ChatMessageCell.ChatMessageCellDelegate.CC.$default$didPressChannelRecommendation(this, chatMessageCell, tLObject, z);
            }

            @Override // org.telegram.ui.Cells.ChatMessageCell.ChatMessageCellDelegate
            public /* synthetic */ void didPressChannelRecommendationsClose(ChatMessageCell chatMessageCell) {
                ChatMessageCell.ChatMessageCellDelegate.CC.$default$didPressChannelRecommendationsClose(this, chatMessageCell);
            }

            @Override // org.telegram.ui.Cells.ChatMessageCell.ChatMessageCellDelegate
            public /* synthetic */ void didPressCommentButton(ChatMessageCell chatMessageCell) {
                ChatMessageCell.ChatMessageCellDelegate.CC.$default$didPressCommentButton(this, chatMessageCell);
            }

            @Override // org.telegram.ui.Cells.ChatMessageCell.ChatMessageCellDelegate
            public /* synthetic */ void didPressCustomBotButton(ChatMessageCell chatMessageCell, BotInlineKeyboard.ButtonCustom buttonCustom) {
                ChatMessageCell.ChatMessageCellDelegate.CC.$default$didPressCustomBotButton(this, chatMessageCell, buttonCustom);
            }

            @Override // org.telegram.ui.Cells.ChatMessageCell.ChatMessageCellDelegate
            public /* synthetic */ void didPressEffect(ChatMessageCell chatMessageCell) {
                ChatMessageCell.ChatMessageCellDelegate.CC.$default$didPressEffect(this, chatMessageCell);
            }

            @Override // org.telegram.ui.Cells.ChatMessageCell.ChatMessageCellDelegate
            public /* synthetic */ void didPressExtendedMediaPreview(ChatMessageCell chatMessageCell, TLRPC.KeyboardButton keyboardButton) {
                ChatMessageCell.ChatMessageCellDelegate.CC.$default$didPressExtendedMediaPreview(this, chatMessageCell, keyboardButton);
            }

            @Override // org.telegram.ui.Cells.ChatMessageCell.ChatMessageCellDelegate
            public /* synthetic */ void didPressFactCheck(ChatMessageCell chatMessageCell) {
                ChatMessageCell.ChatMessageCellDelegate.CC.$default$didPressFactCheck(this, chatMessageCell);
            }

            @Override // org.telegram.ui.Cells.ChatMessageCell.ChatMessageCellDelegate
            public /* synthetic */ void didPressFactCheckWhat(ChatMessageCell chatMessageCell, int i, int i2) {
                ChatMessageCell.ChatMessageCellDelegate.CC.$default$didPressFactCheckWhat(this, chatMessageCell, i, i2);
            }

            @Override // org.telegram.ui.Cells.ChatMessageCell.ChatMessageCellDelegate
            public /* synthetic */ void didPressGiveawayChatButton(ChatMessageCell chatMessageCell, int i) {
                ChatMessageCell.ChatMessageCellDelegate.CC.$default$didPressGiveawayChatButton(this, chatMessageCell, i);
            }

            @Override // org.telegram.ui.Cells.ChatMessageCell.ChatMessageCellDelegate
            public /* synthetic */ void didPressGroupImage(ChatMessageCell chatMessageCell, ImageReceiver imageReceiver, TLRPC.MessageExtendedMedia messageExtendedMedia, float f, float f2) {
                ChatMessageCell.ChatMessageCellDelegate.CC.$default$didPressGroupImage(this, chatMessageCell, imageReceiver, messageExtendedMedia, f, f2);
            }

            @Override // org.telegram.ui.Cells.ChatMessageCell.ChatMessageCellDelegate
            public /* synthetic */ void didPressHiddenForward(ChatMessageCell chatMessageCell) {
                ChatMessageCell.ChatMessageCellDelegate.CC.$default$didPressHiddenForward(this, chatMessageCell);
            }

            @Override // org.telegram.ui.Cells.ChatMessageCell.ChatMessageCellDelegate
            public /* synthetic */ void didPressHint(ChatMessageCell chatMessageCell, int i) {
                ChatMessageCell.ChatMessageCellDelegate.CC.$default$didPressHint(this, chatMessageCell, i);
            }

            @Override // org.telegram.ui.Cells.ChatMessageCell.ChatMessageCellDelegate
            public /* synthetic */ void didPressMoreChannelRecommendations(ChatMessageCell chatMessageCell) {
                ChatMessageCell.ChatMessageCellDelegate.CC.$default$didPressMoreChannelRecommendations(this, chatMessageCell);
            }

            @Override // org.telegram.ui.Cells.ChatMessageCell.ChatMessageCellDelegate
            public /* synthetic */ void didPressReaction(ChatMessageCell chatMessageCell, TLRPC.ReactionCount reactionCount, boolean z, float f, float f2) {
                ChatMessageCell.ChatMessageCellDelegate.CC.$default$didPressReaction(this, chatMessageCell, reactionCount, z, f, f2);
            }

            @Override // org.telegram.ui.Cells.ChatMessageCell.ChatMessageCellDelegate
            public /* synthetic */ void didPressRevealSensitiveContent(ChatMessageCell chatMessageCell) {
                ChatMessageCell.ChatMessageCellDelegate.CC.$default$didPressRevealSensitiveContent(this, chatMessageCell);
            }

            @Override // org.telegram.ui.Cells.ChatMessageCell.ChatMessageCellDelegate
            public /* synthetic */ void didPressSponsoredClose(ChatMessageCell chatMessageCell) {
                ChatMessageCell.ChatMessageCellDelegate.CC.$default$didPressSponsoredClose(this, chatMessageCell);
            }

            @Override // org.telegram.ui.Cells.ChatMessageCell.ChatMessageCellDelegate
            public /* synthetic */ void didPressSponsoredInfo(ChatMessageCell chatMessageCell, float f, float f2) {
                ChatMessageCell.ChatMessageCellDelegate.CC.$default$didPressSponsoredInfo(this, chatMessageCell, f, f2);
            }

            @Override // org.telegram.ui.Cells.ChatMessageCell.ChatMessageCellDelegate
            public /* synthetic */ void didPressSummarize(ChatMessageCell chatMessageCell, boolean z) {
                ChatMessageCell.ChatMessageCellDelegate.CC.$default$didPressSummarize(this, chatMessageCell, z);
            }

            @Override // org.telegram.ui.Cells.ChatMessageCell.ChatMessageCellDelegate
            public /* synthetic */ void didPressTime(ChatMessageCell chatMessageCell) {
                ChatMessageCell.ChatMessageCellDelegate.CC.$default$didPressTime(this, chatMessageCell);
            }

            @Override // org.telegram.ui.Cells.ChatMessageCell.ChatMessageCellDelegate
            public /* synthetic */ boolean didPressToDoButton(ChatMessageCell chatMessageCell, TLRPC.TodoItem todoItem, boolean z) {
                return ChatMessageCell.ChatMessageCellDelegate.CC.$default$didPressToDoButton(this, chatMessageCell, todoItem, z);
            }

            @Override // org.telegram.ui.Cells.ChatMessageCell.ChatMessageCellDelegate
            public /* synthetic */ void didPressUserStatus(ChatMessageCell chatMessageCell, TLRPC.User user, TLRPC.Document document, String str) {
                ChatMessageCell.ChatMessageCellDelegate.CC.$default$didPressUserStatus(this, chatMessageCell, user, document, str);
            }

            @Override // org.telegram.ui.Cells.ChatMessageCell.ChatMessageCellDelegate
            public /* synthetic */ void didPressViaBot(ChatMessageCell chatMessageCell, String str) {
                ChatMessageCell.ChatMessageCellDelegate.CC.$default$didPressViaBot(this, chatMessageCell, str);
            }

            @Override // org.telegram.ui.Cells.ChatMessageCell.ChatMessageCellDelegate
            public /* synthetic */ void didPressViaBotNotInline(ChatMessageCell chatMessageCell, long j) {
                ChatMessageCell.ChatMessageCellDelegate.CC.$default$didPressViaBotNotInline(this, chatMessageCell, j);
            }

            @Override // org.telegram.ui.Cells.ChatMessageCell.ChatMessageCellDelegate
            public /* synthetic */ void didPressVoteButtons(ChatMessageCell chatMessageCell, ArrayList arrayList, int i, int i2, int i3) {
                ChatMessageCell.ChatMessageCellDelegate.CC.$default$didPressVoteButtons(this, chatMessageCell, arrayList, i, i2, i3);
            }

            @Override // org.telegram.ui.Cells.ChatMessageCell.ChatMessageCellDelegate
            public /* synthetic */ void didPressWebPage(ChatMessageCell chatMessageCell, TLRPC.WebPage webPage, String str, boolean z) {
                Browser.openUrl(chatMessageCell.getContext(), str);
            }

            @Override // org.telegram.ui.Cells.ChatMessageCell.ChatMessageCellDelegate
            public /* synthetic */ void didQuickShareEnd(ChatMessageCell chatMessageCell, float f, float f2) {
                ChatMessageCell.ChatMessageCellDelegate.CC.$default$didQuickShareEnd(this, chatMessageCell, f, f2);
            }

            @Override // org.telegram.ui.Cells.ChatMessageCell.ChatMessageCellDelegate
            public /* synthetic */ void didQuickShareMove(ChatMessageCell chatMessageCell, float f, float f2) {
                ChatMessageCell.ChatMessageCellDelegate.CC.$default$didQuickShareMove(this, chatMessageCell, f, f2);
            }

            @Override // org.telegram.ui.Cells.ChatMessageCell.ChatMessageCellDelegate
            public /* synthetic */ void didQuickShareStart(ChatMessageCell chatMessageCell, float f, float f2) {
                ChatMessageCell.ChatMessageCellDelegate.CC.$default$didQuickShareStart(this, chatMessageCell, f, f2);
            }

            @Override // org.telegram.ui.Cells.ChatMessageCell.ChatMessageCellDelegate
            public /* synthetic */ void didStartVideoStream(MessageObject messageObject) {
                ChatMessageCell.ChatMessageCellDelegate.CC.$default$didStartVideoStream(this, messageObject);
            }

            @Override // org.telegram.ui.Cells.ChatMessageCell.ChatMessageCellDelegate
            public boolean doNotShowLoadingReply(MessageObject messageObject) {
                return true;
            }

            @Override // org.telegram.ui.Cells.ChatMessageCell.ChatMessageCellDelegate
            public /* synthetic */ void forceUpdateNoAnimation(ChatMessageCell chatMessageCell, boolean z) {
                ChatMessageCell.ChatMessageCellDelegate.CC.$default$forceUpdateNoAnimation(this, chatMessageCell, z);
            }

            @Override // org.telegram.ui.Cells.ChatMessageCell.ChatMessageCellDelegate
            public /* synthetic */ String getAdminRank(long j) {
                return ChatMessageCell.ChatMessageCellDelegate.CC.$default$getAdminRank(this, j);
            }

            @Override // org.telegram.ui.Cells.ChatMessageCell.ChatMessageCellDelegate
            public /* synthetic */ PinchToZoomHelper getPinchToZoomHelper() {
                return ChatMessageCell.ChatMessageCellDelegate.CC.$default$getPinchToZoomHelper(this);
            }

            @Override // org.telegram.ui.Cells.ChatMessageCell.ChatMessageCellDelegate
            public /* synthetic */ String getProgressLoadingBotButtonUrl(ChatMessageCell chatMessageCell) {
                return ChatMessageCell.ChatMessageCellDelegate.CC.$default$getProgressLoadingBotButtonUrl(this, chatMessageCell);
            }

            @Override // org.telegram.ui.Cells.ChatMessageCell.ChatMessageCellDelegate
            public /* synthetic */ CharacterStyle getProgressLoadingLink(ChatMessageCell chatMessageCell) {
                return ChatMessageCell.ChatMessageCellDelegate.CC.$default$getProgressLoadingLink(this, chatMessageCell);
            }

            @Override // org.telegram.ui.Cells.ChatMessageCell.ChatMessageCellDelegate
            public /* synthetic */ TextSelectionHelper.ChatListTextSelectionHelper getTextSelectionHelper() {
                return ChatMessageCell.ChatMessageCellDelegate.CC.$default$getTextSelectionHelper(this);
            }

            @Override // org.telegram.ui.Cells.ChatMessageCell.ChatMessageCellDelegate
            public /* synthetic */ boolean hasSelectedMessages() {
                return ChatMessageCell.ChatMessageCellDelegate.CC.$default$hasSelectedMessages(this);
            }

            @Override // org.telegram.ui.Cells.ChatMessageCell.ChatMessageCellDelegate
            public /* synthetic */ void invalidateBlur() {
                ChatMessageCell.ChatMessageCellDelegate.CC.$default$invalidateBlur(this);
            }

            @Override // org.telegram.ui.Cells.ChatMessageCell.ChatMessageCellDelegate
            public /* synthetic */ boolean isAdmin(long j) {
                return ChatMessageCell.ChatMessageCellDelegate.CC.$default$isAdmin(this, j);
            }

            @Override // org.telegram.ui.Cells.ChatMessageCell.ChatMessageCellDelegate
            public /* synthetic */ boolean isLandscape() {
                return ChatMessageCell.ChatMessageCellDelegate.CC.$default$isLandscape(this);
            }

            @Override // org.telegram.ui.Cells.ChatMessageCell.ChatMessageCellDelegate
            public /* synthetic */ boolean isOwner(long j) {
                return ChatMessageCell.ChatMessageCellDelegate.CC.$default$isOwner(this, j);
            }

            @Override // org.telegram.ui.Cells.ChatMessageCell.ChatMessageCellDelegate
            public /* synthetic */ boolean isProgressLoading(ChatMessageCell chatMessageCell, int i) {
                return ChatMessageCell.ChatMessageCellDelegate.CC.$default$isProgressLoading(this, chatMessageCell, i);
            }

            @Override // org.telegram.ui.Cells.ChatMessageCell.ChatMessageCellDelegate
            public /* synthetic */ boolean isReplyOrSelf() {
                return ChatMessageCell.ChatMessageCellDelegate.CC.$default$isReplyOrSelf(this);
            }

            @Override // org.telegram.ui.Cells.ChatMessageCell.ChatMessageCellDelegate
            public /* synthetic */ boolean keyboardIsOpened() {
                return ChatMessageCell.ChatMessageCellDelegate.CC.$default$keyboardIsOpened(this);
            }

            @Override // org.telegram.ui.Cells.ChatMessageCell.ChatMessageCellDelegate
            public /* synthetic */ void needReloadPolls() {
                ChatMessageCell.ChatMessageCellDelegate.CC.$default$needReloadPolls(this);
            }

            @Override // org.telegram.ui.Cells.ChatMessageCell.ChatMessageCellDelegate
            public /* synthetic */ void needShowPremiumBulletin(int i) {
                ChatMessageCell.ChatMessageCellDelegate.CC.$default$needShowPremiumBulletin(this, i);
            }

            @Override // org.telegram.ui.Cells.ChatMessageCell.ChatMessageCellDelegate
            public /* synthetic */ boolean onAccessibilityAction(int i, Bundle bundle) {
                return ChatMessageCell.ChatMessageCellDelegate.CC.$default$onAccessibilityAction(this, i, bundle);
            }

            @Override // org.telegram.ui.Cells.ChatMessageCell.ChatMessageCellDelegate
            public /* synthetic */ void onDiceFinished() {
                ChatMessageCell.ChatMessageCellDelegate.CC.$default$onDiceFinished(this);
            }

            @Override // org.telegram.ui.Cells.ChatMessageCell.ChatMessageCellDelegate
            public /* synthetic */ void setShouldNotRepeatSticker(MessageObject messageObject) {
                ChatMessageCell.ChatMessageCellDelegate.CC.$default$setShouldNotRepeatSticker(this, messageObject);
            }

            @Override // org.telegram.ui.Cells.ChatMessageCell.ChatMessageCellDelegate
            public /* synthetic */ boolean shouldDrawAvatarOnlineStatus(ChatMessageCell chatMessageCell) {
                return ChatMessageCell.ChatMessageCellDelegate.CC.$default$shouldDrawAvatarOnlineStatus(this, chatMessageCell);
            }

            @Override // org.telegram.ui.Cells.ChatMessageCell.ChatMessageCellDelegate
            public /* synthetic */ boolean shouldDrawThreadProgress(ChatMessageCell chatMessageCell, boolean z) {
                return ChatMessageCell.ChatMessageCellDelegate.CC.$default$shouldDrawThreadProgress(this, chatMessageCell, z);
            }

            @Override // org.telegram.ui.Cells.ChatMessageCell.ChatMessageCellDelegate
            public /* synthetic */ boolean shouldRepeatSticker(MessageObject messageObject) {
                return ChatMessageCell.ChatMessageCellDelegate.CC.$default$shouldRepeatSticker(this, messageObject);
            }

            @Override // org.telegram.ui.Cells.ChatMessageCell.ChatMessageCellDelegate
            public /* synthetic */ void videoTimerReached() {
                ChatMessageCell.ChatMessageCellDelegate.CC.$default$videoTimerReached(this);
            }

            AnonymousClass2() {
            }

            @Override // org.telegram.ui.Cells.ChatMessageCell.ChatMessageCellDelegate
            public void didPressReplyMessage(ChatMessageCell chatMessageCell, final int i, float f, float f2, boolean z) {
                MessageObject messageObject;
                TLRPC.Message message;
                ChatMessageCell chatMessageCell2;
                MessageObject messageObject2;
                if (z || (messageObject = chatMessageCell.getMessageObject()) == null || (message = messageObject.messageOwner) == null || message.reply_to == null) {
                    return;
                }
                long dialogId = AyuMessageHistory.this.getDialogId();
                TLRPC.Peer peer = messageObject.messageOwner.reply_to.reply_to_peer_id;
                if (peer != null) {
                    dialogId = DialogObject.getPeerDialogId(peer);
                }
                long j = dialogId;
                if (j != AyuMessageHistory.this.getDialogId()) {
                    LaunchActivity launchActivity = LaunchActivity.instance;
                    if (launchActivity != null) {
                        launchActivity.openMessage(j, i, null, null, messageObject.getId(), -1, null);
                        return;
                    }
                    return;
                }
                int i2 = 0;
                while (true) {
                    if (i2 >= AyuMessageHistory.this.messages.size()) {
                        i2 = -1;
                        break;
                    } else if (((MessageObject) AyuMessageHistory.this.messages.get(i2)).getId() == i) {
                        break;
                    } else {
                        i2++;
                    }
                }
                if (i2 >= 0) {
                    AyuMessageHistory.this.highlightMessageId = i;
                    int size = ((AyuMessageHistory.this.chatAdapter.messagesStartRow + AyuMessageHistory.this.messages.size()) - i2) - 1;
                    for (int i3 = 0; i3 < AyuMessageHistory.this.chatListView.getChildCount(); i3++) {
                        View childAt = AyuMessageHistory.this.chatListView.getChildAt(i3);
                        if ((childAt instanceof ChatMessageCell) && (messageObject2 = (chatMessageCell2 = (ChatMessageCell) childAt).getMessageObject()) != null && messageObject2.getId() == i) {
                            chatMessageCell2.setHighlighted(true);
                            int top = (childAt.getTop() - AyuMessageHistory.this.chatListView.getPaddingTop()) - ((((AyuMessageHistory.this.chatListView.getMeasuredHeight() - AyuMessageHistory.this.chatListView.getPaddingTop()) - AyuMessageHistory.this.chatListView.getPaddingBottom()) - childAt.getHeight()) / 2);
                            if (top != 0) {
                                AyuMessageHistory.this.chatListView.smoothScrollBy(0, top);
                            }
                            AyuMessageHistory.this.startMessageUnhighlight();
                            return;
                        }
                    }
                    AyuMessageHistory.this.scrollingToHighlight = true;
                    LinearSmoothScrollerCustom linearSmoothScrollerCustom = new LinearSmoothScrollerCustom(AyuMessageHistory.this.chatListView.getContext(), 0);
                    linearSmoothScrollerCustom.setTargetPosition(size);
                    AyuMessageHistory.this.chatLayoutManager.startSmoothScroll(linearSmoothScrollerCustom);
                    AyuMessageHistory.this.chatListView.addOnScrollListener(new RecyclerView.OnScrollListener() { // from class: com.radolyn.ayugram.ui.AyuMessageHistory.ChatActivityAdapter.2.1
                        @Override // androidx.recyclerview.widget.RecyclerView.OnScrollListener
                        public void onScrollStateChanged(RecyclerView recyclerView, int i4) {
                            if (i4 != 0) {
                                return;
                            }
                            AyuMessageHistory.this.chatListView.removeOnScrollListener(this);
                            AyuMessageHistory.this.scrollingToHighlight = false;
                            AyuMessageHistory.this.highlightMessageOnScreen(i);
                        }
                    });
                    return;
                }
                final ChatActivity chatActivity = AyuMessageHistory.this.chatActivity;
                if (chatActivity == null) {
                    return;
                }
                AndroidUtilities.runOnUIThread(new Runnable() { // from class: com.radolyn.ayugram.ui.AyuMessageHistory$ChatActivityAdapter$2$$ExternalSyntheticLambda7
                    @Override // java.lang.Runnable
                    public final void run() {
                        this.f$0.lambda$didPressReplyMessage$0(chatActivity, i);
                    }
                }, 300L);
                AyuMessageHistory.this.finishPreviewFragment();
            }

            /* JADX INFO: Access modifiers changed from: private */
            public /* synthetic */ void lambda$didPressReplyMessage$0(ChatActivity chatActivity, int i) {
                AyuMessageHistory.this.finishFragment();
                chatActivity.scrollToMessageId(i, 0, true, 0, true, 0);
            }

            @Override // org.telegram.ui.Cells.ChatMessageCell.ChatMessageCellDelegate
            public void forceUpdate(ChatMessageCell chatMessageCell, boolean z) {
                MessageObject primaryMessageObject;
                if (chatMessageCell == null || (primaryMessageObject = chatMessageCell.getPrimaryMessageObject()) == null) {
                    return;
                }
                primaryMessageObject.forceUpdate = true;
                AyuMessageHistory.this.updateMessageAnimatedInternal(primaryMessageObject, false);
            }

            @Override // org.telegram.ui.Cells.ChatMessageCell.ChatMessageCellDelegate
            public void didPressCodeCopy(ChatMessageCell chatMessageCell, MessageObject.TextLayoutBlock textLayoutBlock) {
                StaticLayout staticLayout;
                if (textLayoutBlock == null || (staticLayout = textLayoutBlock.textLayout) == null || staticLayout.getText() == null) {
                    return;
                }
                String string = textLayoutBlock.textLayout.getText().toString();
                SpannableString spannableString = new SpannableString(string);
                spannableString.setSpan(new CodeHighlighting.Span(false, 0, null, textLayoutBlock.language, string), 0, spannableString.length(), 33);
                AndroidUtilities.addToClipboard(spannableString);
                BulletinFactory.of(AyuMessageHistory.this).createCopyBulletin(LocaleController.getString(R.string.CodeCopied)).show();
            }

            @Override // org.telegram.ui.Cells.ChatMessageCell.ChatMessageCellDelegate
            public void didPressSideButton(ChatMessageCell chatMessageCell) {
                if (AyuMessageHistory.this.getParentActivity() == null) {
                    return;
                }
                final MessageObject messageObject = chatMessageCell.getMessageObject();
                TLRPC.Message message = messageObject.messageOwner;
                final boolean z = message.ayuDeleted;
                message.ayuDeleted = true;
                ShareAlert shareAlertCreateShareAlert = ShareAlert.createShareAlert(ChatActivityAdapter.this.mContext, messageObject, null, ChatObject.isChannel(AyuMessageHistory.this.currentChat) && !AyuMessageHistory.this.currentChat.megagroup, null, false);
                AyuMessageHistory.this.showDialog(shareAlertCreateShareAlert);
                final Runnable runnable = new Runnable() { // from class: com.radolyn.ayugram.ui.AyuMessageHistory$ChatActivityAdapter$2$$ExternalSyntheticLambda1
                    @Override // java.lang.Runnable
                    public final void run() {
                        messageObject.messageOwner.ayuDeleted = z;
                    }
                };
                shareAlertCreateShareAlert.setOnDismissListener(new DialogInterface.OnDismissListener() { // from class: com.radolyn.ayugram.ui.AyuMessageHistory$ChatActivityAdapter$2$$ExternalSyntheticLambda2
                    @Override // android.content.DialogInterface.OnDismissListener
                    public final void onDismiss(DialogInterface dialogInterface) {
                        AndroidUtilities.runOnUIThread(runnable);
                    }
                });
                shareAlertCreateShareAlert.setOnHideListener(new DialogInterface.OnDismissListener() { // from class: com.radolyn.ayugram.ui.AyuMessageHistory$ChatActivityAdapter$2$$ExternalSyntheticLambda3
                    @Override // android.content.DialogInterface.OnDismissListener
                    public final void onDismiss(DialogInterface dialogInterface) {
                        AndroidUtilities.runOnUIThread(runnable);
                    }
                });
                shareAlertCreateShareAlert.setOnCancelListener(new DialogInterface.OnCancelListener() { // from class: com.radolyn.ayugram.ui.AyuMessageHistory$ChatActivityAdapter$2$$ExternalSyntheticLambda4
                    @Override // android.content.DialogInterface.OnCancelListener
                    public final void onCancel(DialogInterface dialogInterface) {
                        AndroidUtilities.runOnUIThread(runnable);
                    }
                });
            }

            @Override // org.telegram.ui.Cells.ChatMessageCell.ChatMessageCellDelegate
            public boolean needPlayMessage(ChatMessageCell chatMessageCell, MessageObject messageObject, boolean z) {
                if (messageObject.isVoice() || messageObject.isRoundVideo()) {
                    boolean zPlayMessage = MediaController.getInstance().playMessage(messageObject, z);
                    MediaController.getInstance().setVoiceMessagesPlaylist(null, false);
                    return zPlayMessage;
                }
                if (messageObject.isMusic()) {
                    return MediaController.getInstance().setPlaylist(AyuMessageHistory.this.messages, messageObject, 0L);
                }
                return false;
            }

            @Override // org.telegram.ui.Cells.ChatMessageCell.ChatMessageCellDelegate
            public void didPressChannelAvatar(ChatMessageCell chatMessageCell, TLRPC.Chat chat, int i, float f, float f2, boolean z) {
                if (chat == null || chat == AyuMessageHistory.this.currentChat) {
                    return;
                }
                Bundle bundle = new Bundle();
                bundle.putLong("chat_id", chat.id);
                if (i != 0) {
                    bundle.putInt("message_id", i);
                }
                if (MessagesController.getInstance(((BaseFragment) AyuMessageHistory.this).currentAccount).checkCanOpenChat(bundle, AyuMessageHistory.this)) {
                    AyuMessageHistory.this.presentFragment(new ChatActivity(bundle), true);
                }
            }

            @Override // org.telegram.ui.Cells.ChatMessageCell.ChatMessageCellDelegate
            public void didPressOther(ChatMessageCell chatMessageCell, float f, float f2) {
                AyuMessageHistory.this.createMenu(chatMessageCell);
            }

            @Override // org.telegram.ui.Cells.ChatMessageCell.ChatMessageCellDelegate
            public void didPressUserAvatar(ChatMessageCell chatMessageCell, TLRPC.User user, float f, float f2, boolean z) {
                if (user == null || user.id == UserConfig.getInstance(((BaseFragment) AyuMessageHistory.this).currentAccount).getClientUserId()) {
                    return;
                }
                openProfile(user);
            }

            @Override // org.telegram.ui.Cells.ChatMessageCell.ChatMessageCellDelegate
            public boolean didLongPressUserAvatar(final ChatMessageCell chatMessageCell, final TLRPC.User user, float f, float f2) {
                AvatarPreviewer.Data dataOf;
                if (user != null && user.id != UserConfig.getInstance(((BaseFragment) AyuMessageHistory.this).currentAccount).getClientUserId()) {
                    AvatarPreviewer.MenuItem[] menuItemArr = {AvatarPreviewer.MenuItem.OPEN_PROFILE, AvatarPreviewer.MenuItem.SEND_MESSAGE};
                    TLRPC.UserFull userFull = AyuMessageHistory.this.getMessagesController().getUserFull(user.id);
                    if (userFull == null) {
                        dataOf = AvatarPreviewer.Data.of(user, ((BaseFragment) AyuMessageHistory.this).classGuid, menuItemArr);
                    } else {
                        dataOf = AvatarPreviewer.Data.of(user, userFull, menuItemArr);
                    }
                    if (AvatarPreviewer.canPreview(dataOf)) {
                        AvatarPreviewer avatarPreviewer = AvatarPreviewer.getInstance();
                        AyuMessageHistory ayuMessageHistory = AyuMessageHistory.this;
                        avatarPreviewer.show((ViewGroup) ayuMessageHistory.fragmentView, ayuMessageHistory.theme, dataOf, new AvatarPreviewer.Callback() { // from class: com.radolyn.ayugram.ui.AyuMessageHistory$ChatActivityAdapter$2$$ExternalSyntheticLambda6
                            @Override // org.telegram.ui.AvatarPreviewer.Callback
                            public final void onMenuClick(AvatarPreviewer.MenuItem menuItem) {
                                this.f$0.lambda$didLongPressUserAvatar$5(chatMessageCell, user, menuItem);
                            }
                        });
                        return true;
                    }
                }
                return false;
            }

            /* JADX INFO: Access modifiers changed from: private */
            public /* synthetic */ void lambda$didLongPressUserAvatar$5(ChatMessageCell chatMessageCell, TLRPC.User user, AvatarPreviewer.MenuItem menuItem) {
                int i = AnonymousClass18.$SwitchMap$org$telegram$ui$AvatarPreviewer$MenuItem[menuItem.ordinal()];
                if (i == 1) {
                    openDialog(chatMessageCell, user);
                } else {
                    if (i != 2) {
                        return;
                    }
                    openProfile(user);
                }
            }

            private void openProfile(TLRPC.User user) {
                Bundle bundle = new Bundle();
                bundle.putLong("user_id", user.id);
                ProfileActivity profileActivity = new ProfileActivity(bundle);
                profileActivity.setPlayProfileAnimation(0);
                AyuMessageHistory.this.presentFragment(profileActivity);
            }

            private void openDialog(ChatMessageCell chatMessageCell, TLRPC.User user) {
                if (user != null) {
                    Bundle bundle = new Bundle();
                    bundle.putLong("user_id", user.id);
                    if (AyuMessageHistory.this.getMessagesController().checkCanOpenChat(bundle, AyuMessageHistory.this)) {
                        AyuMessageHistory.this.presentFragment(new ChatActivity(bundle));
                    }
                }
            }

            @Override // org.telegram.ui.Cells.ChatMessageCell.ChatMessageCellDelegate
            public void didPressUrl(ChatMessageCell chatMessageCell, CharacterStyle characterStyle, boolean z) {
                TLRPC.WebPage webPage;
                if (characterStyle == null) {
                    return;
                }
                MessageObject messageObject = chatMessageCell.getMessageObject();
                if (characterStyle instanceof URLSpanMono) {
                    ((URLSpanMono) characterStyle).copyToClipboard();
                    if (AndroidUtilities.shouldShowClipboardToast()) {
                        Toast.makeText(AyuMessageHistory.this.getParentActivity(), LocaleController.getString(R.string.TextCopied), 0).show();
                    }
                } else if (characterStyle instanceof URLSpanUserMention) {
                    Long l = Utilities.parseLong(((URLSpanUserMention) characterStyle).getURL());
                    long jLongValue = l.longValue();
                    if (jLongValue > 0) {
                        TLRPC.User user = MessagesController.getInstance(((BaseFragment) AyuMessageHistory.this).currentAccount).getUser(l);
                        if (user != null) {
                            MessagesController.getInstance(((BaseFragment) AyuMessageHistory.this).currentAccount).openChatOrProfileWith(user, null, AyuMessageHistory.this, 0, false);
                        }
                    } else {
                        TLRPC.Chat chat = MessagesController.getInstance(((BaseFragment) AyuMessageHistory.this).currentAccount).getChat(Long.valueOf(-jLongValue));
                        if (chat != null) {
                            MessagesController.getInstance(((BaseFragment) AyuMessageHistory.this).currentAccount).openChatOrProfileWith(null, chat, AyuMessageHistory.this, 0, false);
                        }
                    }
                } else if (characterStyle instanceof URLSpanNoUnderline) {
                    String url = ((URLSpanNoUnderline) characterStyle).getURL();
                    if (url != null && url.startsWith("tel:")) {
                        didPressPhoneNumber(chatMessageCell, characterStyle, url.substring(4));
                        if (z) {
                            chatMessageCell.resetPressedLink(-1);
                        }
                    } else if (url.startsWith("@")) {
                        MessagesController.getInstance(((BaseFragment) AyuMessageHistory.this).currentAccount).openByUserName(url.substring(1), AyuMessageHistory.this, 0);
                    } else if (url.startsWith("#")) {
                        DialogsActivity dialogsActivity = new DialogsActivity(null);
                        dialogsActivity.setSearchString(url);
                        AyuMessageHistory.this.presentFragment(dialogsActivity);
                    }
                } else {
                    final String url2 = ((URLSpan) characterStyle).getURL();
                    if (z) {
                        BottomSheet.Builder builder = new BottomSheet.Builder(AyuMessageHistory.this.getParentActivity());
                        builder.setTitle(url2);
                        builder.setItems(new CharSequence[]{LocaleController.getString(R.string.Open), LocaleController.getString(R.string.Copy)}, new DialogInterface.OnClickListener() { // from class: com.radolyn.ayugram.ui.AyuMessageHistory$ChatActivityAdapter$2$$ExternalSyntheticLambda5
                            @Override // android.content.DialogInterface.OnClickListener
                            public final void onClick(DialogInterface dialogInterface, int i) {
                                this.f$0.lambda$didPressUrl$6(url2, dialogInterface, i);
                            }
                        });
                        AyuMessageHistory.this.showDialog(builder.create());
                    } else if (characterStyle instanceof URLSpanReplacement) {
                        AyuMessageHistory.this.showOpenUrlAlert(((URLSpanReplacement) characterStyle).getURL(), true);
                    } else {
                        TLRPC.MessageMedia messageMedia = messageObject.messageOwner.media;
                        if ((messageMedia instanceof TLRPC.TL_messageMediaWebPage) && (webPage = messageMedia.webpage) != null && webPage.cached_page != null) {
                            String lowerCase = url2.toLowerCase();
                            String lowerCase2 = messageObject.messageOwner.media.webpage.url.toLowerCase();
                            if ((Browser.isTelegraphUrl(lowerCase, false) || lowerCase.contains("t.me/iv")) && (lowerCase.contains(lowerCase2) || lowerCase2.contains(lowerCase))) {
                                ArticleViewer.getInstance().setParentActivity(AyuMessageHistory.this.getParentActivity(), AyuMessageHistory.this);
                                ArticleViewer.getInstance().open(messageObject);
                                return;
                            }
                        }
                        Browser.openUrl((Context) AyuMessageHistory.this.getParentActivity(), url2, true);
                    }
                }
                if (z) {
                    chatMessageCell.resetPressedLink(-1);
                }
            }

            /* JADX INFO: Access modifiers changed from: private */
            public /* synthetic */ void lambda$didPressUrl$6(String str, DialogInterface dialogInterface, int i) {
                if (i == 0) {
                    Browser.openUrl((Context) AyuMessageHistory.this.getParentActivity(), str, true);
                    return;
                }
                if (i == 1) {
                    if (str.startsWith("mailto:")) {
                        str = str.substring(7);
                    } else if (str.startsWith("tel:")) {
                        str = str.substring(4);
                    }
                    AndroidUtilities.addToClipboard(str);
                }
            }

            public void didPressPhoneNumber(final ChatMessageCell chatMessageCell, final CharacterStyle characterStyle, final String str) {
                final Browser.Progress progressMakeProgressForLink = ChatActivityAdapter.this.makeProgressForLink(chatMessageCell, characterStyle);
                final TLRPC.TL_contact tL_contact = AyuMessageHistory.this.getContactsController().contactsByPhone.get(PhoneFormat.stripExceptNumbers(str));
                final Utilities.Callback callback = new Utilities.Callback() { // from class: com.radolyn.ayugram.ui.AyuMessageHistory$ChatActivityAdapter$2$$ExternalSyntheticLambda8
                    @Override // org.telegram.messenger.Utilities.Callback
                    public final void run(Object obj) {
                        this.f$0.lambda$didPressPhoneNumber$20(chatMessageCell, str, tL_contact, characterStyle, (TLRPC.User) obj);
                    }
                };
                if (tL_contact != null) {
                    TLRPC.User user = AyuMessageHistory.this.getMessagesController().getUser(Long.valueOf(tL_contact.user_id));
                    if (user != null) {
                        callback.run(user);
                        return;
                    } else {
                        AyuMessageHistory.this.getMessagesStorage().getStorageQueue().postRunnable(new Runnable() { // from class: com.radolyn.ayugram.ui.AyuMessageHistory$ChatActivityAdapter$2$$ExternalSyntheticLambda9
                            @Override // java.lang.Runnable
                            public final void run() {
                                this.f$0.lambda$didPressPhoneNumber$22(tL_contact, callback);
                            }
                        });
                        return;
                    }
                }
                TLRPC.TL_contacts_resolvePhone tL_contacts_resolvePhone = new TLRPC.TL_contacts_resolvePhone();
                tL_contacts_resolvePhone.phone = PhoneFormat.stripExceptNumbers(str);
                final int iSendRequest = AyuMessageHistory.this.getConnectionsManager().sendRequest(tL_contacts_resolvePhone, new RequestDelegate() { // from class: com.radolyn.ayugram.ui.AyuMessageHistory$ChatActivityAdapter$2$$ExternalSyntheticLambda10
                    @Override // org.telegram.tgnet.RequestDelegate
                    public final void run(TLObject tLObject, TLRPC.TL_error tL_error) {
                        this.f$0.lambda$didPressPhoneNumber$25(str, progressMakeProgressForLink, callback, tLObject, tL_error);
                    }
                });
                progressMakeProgressForLink.onCancel(new Runnable() { // from class: com.radolyn.ayugram.ui.AyuMessageHistory$ChatActivityAdapter$2$$ExternalSyntheticLambda11
                    @Override // java.lang.Runnable
                    public final void run() {
                        this.f$0.lambda$didPressPhoneNumber$26(iSendRequest);
                    }
                });
                progressMakeProgressForLink.init();
            }

            /* JADX INFO: Access modifiers changed from: private */
            public /* synthetic */ void lambda$didPressPhoneNumber$20(ChatMessageCell chatMessageCell, final String str, TLRPC.TL_contact tL_contact, CharacterStyle characterStyle, final TLRPC.User user) {
                final TLRPC.UserFull userFull = user != null ? AyuMessageHistory.this.getMessagesController().getUserFull(user.id) : null;
                final ItemOptions itemOptionsMakeOptions = ItemOptions.makeOptions((BaseFragment) AyuMessageHistory.this, (View) chatMessageCell, true);
                final ScrimOptions scrimOptions = new ScrimOptions(AyuMessageHistory.this.getContext(), AyuMessageHistory.this.theme);
                itemOptionsMakeOptions.setOnDismiss(new AyuMessageHistory$ChatActivityAdapter$2$$ExternalSyntheticLambda15(scrimOptions));
                final Utilities.Callback callback = new Utilities.Callback() { // from class: com.radolyn.ayugram.ui.AyuMessageHistory$ChatActivityAdapter$2$$ExternalSyntheticLambda21
                    @Override // org.telegram.messenger.Utilities.Callback
                    public final void run(Object obj) {
                        this.f$0.lambda$didPressPhoneNumber$7(user, str, (Boolean) obj);
                    }
                };
                final ItemOptions itemOptionsMakeSwipeback = itemOptionsMakeOptions.makeSwipeback();
                itemOptionsMakeSwipeback.add(R.drawable.ic_ab_back, LocaleController.getString(R.string.Back), new RatePill$$ExternalSyntheticLambda1(itemOptionsMakeOptions));
                itemOptionsMakeSwipeback.addGap();
                itemOptionsMakeSwipeback.add(R.drawable.msg_addbot, LocaleController.getString(R.string.CreateNewContact), new Runnable() { // from class: com.radolyn.ayugram.ui.AyuMessageHistory$ChatActivityAdapter$2$$ExternalSyntheticLambda22
                    @Override // java.lang.Runnable
                    public final void run() {
                        this.f$0.lambda$didPressPhoneNumber$8(itemOptionsMakeOptions, str);
                    }
                });
                itemOptionsMakeSwipeback.add(R.drawable.menu_contact_existing, LocaleController.getString(R.string.AddToExistingContact), new Runnable() { // from class: com.radolyn.ayugram.ui.AyuMessageHistory$ChatActivityAdapter$2$$ExternalSyntheticLambda23
                    @Override // java.lang.Runnable
                    public final void run() {
                        callback.run(Boolean.FALSE);
                    }
                });
                if (tL_contact == null && (user == null || !AyuMessageHistory.this.getContactsController().contactsDict.containsKey(Long.valueOf(user.id)))) {
                    itemOptionsMakeOptions.add(R.drawable.msg_contact_add, LocaleController.getString(R.string.AddToContacts), new Runnable() { // from class: com.radolyn.ayugram.ui.AyuMessageHistory$ChatActivityAdapter$2$$ExternalSyntheticLambda24
                        @Override // java.lang.Runnable
                        public final void run() {
                            itemOptionsMakeOptions.openSwipeback(itemOptionsMakeSwipeback);
                        }
                    });
                    itemOptionsMakeOptions.addGap();
                }
                if (user == null) {
                    itemOptionsMakeOptions.add(R.drawable.menu_invit_telegram, LocaleController.getString(R.string.InviteToTelegramShort), new Runnable() { // from class: com.radolyn.ayugram.ui.AyuMessageHistory$ChatActivityAdapter$2$$ExternalSyntheticLambda25
                        @Override // java.lang.Runnable
                        public final void run() {
                            this.f$0.lambda$didPressPhoneNumber$11(str);
                        }
                    });
                    itemOptionsMakeOptions.add(R.drawable.msg_calls_regular, LocaleController.getString(R.string.VoiceCallViaCarrier), new Runnable() { // from class: com.radolyn.ayugram.ui.AyuMessageHistory$ChatActivityAdapter$2$$ExternalSyntheticLambda26
                        @Override // java.lang.Runnable
                        public final void run() {
                            this.f$0.lambda$didPressPhoneNumber$12(str);
                        }
                    });
                    itemOptionsMakeOptions.add(R.drawable.msg_copy, LocaleController.getString(R.string.CopyNumber), new Runnable() { // from class: com.radolyn.ayugram.ui.AyuMessageHistory$ChatActivityAdapter$2$$ExternalSyntheticLambda27
                        @Override // java.lang.Runnable
                        public final void run() {
                            this.f$0.lambda$didPressPhoneNumber$13(str);
                        }
                    });
                    itemOptionsMakeOptions.addGap();
                    itemOptionsMakeOptions.addText(LocaleController.getString(R.string.NumberNotOnTelegram), 13);
                } else {
                    itemOptionsMakeOptions.add(R.drawable.msg_discussion, LocaleController.getString(R.string.SendMessage), new Runnable() { // from class: com.radolyn.ayugram.ui.AyuMessageHistory$ChatActivityAdapter$2$$ExternalSyntheticLambda28
                        @Override // java.lang.Runnable
                        public final void run() {
                            this.f$0.lambda$didPressPhoneNumber$14(user);
                        }
                    });
                    if (!UserObject.isUserSelf(user)) {
                        itemOptionsMakeOptions.add(R.drawable.msg_calls, LocaleController.getString(R.string.VoiceCallViaTelegram), new Runnable() { // from class: com.radolyn.ayugram.ui.AyuMessageHistory$ChatActivityAdapter$2$$ExternalSyntheticLambda16
                            @Override // java.lang.Runnable
                            public final void run() {
                                this.f$0.lambda$didPressPhoneNumber$15(user, userFull);
                            }
                        });
                        itemOptionsMakeOptions.add(R.drawable.msg_videocall, LocaleController.getString(R.string.VideoCallViaTelegram), new Runnable() { // from class: com.radolyn.ayugram.ui.AyuMessageHistory$ChatActivityAdapter$2$$ExternalSyntheticLambda17
                            @Override // java.lang.Runnable
                            public final void run() {
                                this.f$0.lambda$didPressPhoneNumber$16(user, userFull);
                            }
                        });
                    }
                    itemOptionsMakeOptions.add(R.drawable.msg_calls_regular, LocaleController.getString(R.string.VoiceCallViaCarrier), new Runnable() { // from class: com.radolyn.ayugram.ui.AyuMessageHistory$ChatActivityAdapter$2$$ExternalSyntheticLambda18
                        @Override // java.lang.Runnable
                        public final void run() {
                            this.f$0.lambda$didPressPhoneNumber$17(str);
                        }
                    });
                    itemOptionsMakeOptions.add(R.drawable.msg_copy, LocaleController.getString(R.string.CopyNumber), new Runnable() { // from class: com.radolyn.ayugram.ui.AyuMessageHistory$ChatActivityAdapter$2$$ExternalSyntheticLambda19
                        @Override // java.lang.Runnable
                        public final void run() {
                            this.f$0.lambda$didPressPhoneNumber$18(str);
                        }
                    });
                    itemOptionsMakeOptions.addGap();
                    itemOptionsMakeOptions.addProfile(user, LocaleController.getString(R.string.ViewProfile), new Runnable() { // from class: com.radolyn.ayugram.ui.AyuMessageHistory$ChatActivityAdapter$2$$ExternalSyntheticLambda20
                        @Override // java.lang.Runnable
                        public final void run() {
                            this.f$0.lambda$didPressPhoneNumber$19(scrimOptions, user);
                        }
                    });
                }
                scrimOptions.setItemOptions(itemOptionsMakeOptions);
                scrimOptions.setScrim(chatMessageCell, characterStyle, null);
                AyuMessageHistory.this.showDialog(scrimOptions);
            }

            /* JADX INFO: Access modifiers changed from: private */
            public /* synthetic */ void lambda$didPressPhoneNumber$7(TLRPC.User user, String str, Boolean bool) {
                Intent intent;
                if (AyuMessageHistory.this.getParentActivity() == null) {
                    return;
                }
                if (bool.booleanValue()) {
                    intent = new Intent("android.intent.action.INSERT");
                    intent.setType("vnd.android.cursor.dir/raw_contact");
                } else {
                    intent = new Intent("android.intent.action.INSERT_OR_EDIT");
                    intent.setType("vnd.android.cursor.item/contact");
                }
                if (user != null) {
                    intent.putExtra("name", ContactsController.formatName(user.first_name, user.last_name));
                }
                ArrayList<? extends Parcelable> arrayList = new ArrayList<>();
                ContentValues contentValues = new ContentValues();
                contentValues.put("mimetype", "vnd.android.cursor.item/phone_v2");
                if (!str.startsWith("+")) {
                    TLRPC.User currentUser = AyuMessageHistory.this.getUserConfig().getCurrentUser();
                    HashMap map = new HashMap();
                    boolean z = false;
                    try {
                        BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(ApplicationLoader.applicationContext.getResources().getAssets().open("countries.txt")));
                        while (true) {
                            String line = bufferedReader.readLine();
                            if (line == null) {
                                break;
                            }
                            String[] strArrSplit = line.split(";");
                            CountrySelectActivity.Country country = new CountrySelectActivity.Country();
                            country.name = strArrSplit[2];
                            String str2 = strArrSplit[0];
                            country.code = str2;
                            country.shortname = strArrSplit[1];
                            List arrayList2 = (List) map.get(str2);
                            if (arrayList2 == null) {
                                String str3 = strArrSplit[0];
                                arrayList2 = new ArrayList();
                                map.put(str3, arrayList2);
                            }
                            arrayList2.add(country);
                        }
                        bufferedReader.close();
                    } catch (Exception e) {
                        FileLog.e(e);
                    }
                    String str4 = currentUser.phone;
                    for (int i = 4; i >= 1; i--) {
                        List list = (List) map.get(str4.substring(0, i));
                        if (list != null && list.size() > 0) {
                            String str5 = ((CountrySelectActivity.Country) list.get(0)).code;
                            if (str5.endsWith(MVEL.VERSION_SUB) && str.startsWith(MVEL.VERSION_SUB)) {
                                str = str.substring(1);
                            }
                            str = "+" + str5 + str;
                            z = true;
                            break;
                        }
                    }
                    if (!z) {
                        Context context = ApplicationLoader.applicationContext;
                        String upperCase = context != null ? ((TelephonyManager) context.getSystemService(TelephonyManager.class)).getSimCountryIso().toUpperCase(Locale.US) : Locale.getDefault().getCountry();
                        if (upperCase.endsWith(MVEL.VERSION_SUB) && str.startsWith(MVEL.VERSION_SUB)) {
                            str = str.substring(1);
                        }
                        str = "+" + upperCase + str;
                    }
                }
                contentValues.put("data1", str);
                contentValues.put("data2", (Integer) 2);
                arrayList.add(contentValues);
                intent.putExtra("finishActivityOnSaveCompleted", true);
                intent.putParcelableArrayListExtra("data", arrayList);
                AyuMessageHistory.this.getParentActivity().startActivity(intent);
            }

            /* JADX INFO: Access modifiers changed from: private */
            public /* synthetic */ void lambda$didPressPhoneNumber$8(ItemOptions itemOptions, String str) {
                itemOptions.dismiss();
                AyuMessageHistory ayuMessageHistory = AyuMessageHistory.this;
                new NewContactBottomSheet(ayuMessageHistory, ayuMessageHistory.getContext()).setInitialPhoneNumber(str, false).show();
            }

            /* JADX INFO: Access modifiers changed from: private */
            public /* synthetic */ void lambda$didPressPhoneNumber$11(String str) {
                if (AyuMessageHistory.this.getParentActivity() == null) {
                    return;
                }
                try {
                    Intent intent = new Intent("android.intent.action.VIEW", Uri.fromParts("sms", str, null));
                    intent.putExtra("sms_body", ContactsController.getInstance(((BaseFragment) AyuMessageHistory.this).currentAccount).getInviteText(1));
                    AyuMessageHistory.this.getParentActivity().startActivityForResult(intent, 500);
                } catch (Exception e) {
                    FileLog.e(e);
                }
            }

            /* JADX INFO: Access modifiers changed from: private */
            public /* synthetic */ void lambda$didPressPhoneNumber$12(String str) {
                Browser.openUrl(AyuMessageHistory.this.getContext(), "tel:" + str);
            }

            /* JADX INFO: Access modifiers changed from: private */
            public /* synthetic */ void lambda$didPressPhoneNumber$13(String str) {
                AndroidUtilities.addToClipboard(str);
                BulletinFactory.of(AyuMessageHistory.this).createCopyBulletin(LocaleController.getString(R.string.PhoneCopied)).show();
            }

            /* JADX INFO: Access modifiers changed from: private */
            public /* synthetic */ void lambda$didPressPhoneNumber$14(TLRPC.User user) {
                AyuMessageHistory.this.presentFragment(ChatActivity.of(user.id));
            }

            /* JADX INFO: Access modifiers changed from: private */
            public /* synthetic */ void lambda$didPressPhoneNumber$15(TLRPC.User user, TLRPC.UserFull userFull) {
                VoIPHelper.startCall(user, false, userFull != null && userFull.video_calls_available, AyuMessageHistory.this.getParentActivity(), userFull, AyuMessageHistory.this.getAccountInstance());
            }

            /* JADX INFO: Access modifiers changed from: private */
            public /* synthetic */ void lambda$didPressPhoneNumber$16(TLRPC.User user, TLRPC.UserFull userFull) {
                VoIPHelper.startCall(user, true, userFull != null && userFull.video_calls_available, AyuMessageHistory.this.getParentActivity(), userFull, AyuMessageHistory.this.getAccountInstance());
            }

            /* JADX INFO: Access modifiers changed from: private */
            public /* synthetic */ void lambda$didPressPhoneNumber$17(String str) {
                Browser.openUrl(AyuMessageHistory.this.getContext(), "tel:" + str);
            }

            /* JADX INFO: Access modifiers changed from: private */
            public /* synthetic */ void lambda$didPressPhoneNumber$18(String str) {
                AndroidUtilities.addToClipboard(str);
                BulletinFactory.of(AyuMessageHistory.this).createCopyBulletin(LocaleController.getString(R.string.PhoneCopied)).show();
            }

            /* JADX INFO: Access modifiers changed from: private */
            public /* synthetic */ void lambda$didPressPhoneNumber$19(ScrimOptions scrimOptions, TLRPC.User user) {
                scrimOptions.dismiss();
                AyuMessageHistory.this.presentFragment(ProfileActivity.of(user.id));
            }

            /* JADX INFO: Access modifiers changed from: private */
            public /* synthetic */ void lambda$didPressPhoneNumber$22(TLRPC.TL_contact tL_contact, final Utilities.Callback callback) {
                final TLRPC.User user = AyuMessageHistory.this.getMessagesStorage().getUser(tL_contact.user_id);
                AndroidUtilities.runOnUIThread(new Runnable() { // from class: com.radolyn.ayugram.ui.AyuMessageHistory$ChatActivityAdapter$2$$ExternalSyntheticLambda14
                    @Override // java.lang.Runnable
                    public final void run() {
                        callback.run(user);
                    }
                });
            }

            /* JADX INFO: Access modifiers changed from: private */
            public /* synthetic */ void lambda$didPressPhoneNumber$25(final String str, final Browser.Progress progress, final Utilities.Callback callback, final TLObject tLObject, TLRPC.TL_error tL_error) {
                AndroidUtilities.runOnUIThread(new Runnable() { // from class: com.radolyn.ayugram.ui.AyuMessageHistory$ChatActivityAdapter$2$$ExternalSyntheticLambda13
                    @Override // java.lang.Runnable
                    public final void run() {
                        this.f$0.lambda$didPressPhoneNumber$24(tLObject, str, progress, callback);
                    }
                });
            }

            /* JADX INFO: Access modifiers changed from: private */
            /* JADX WARN: Code duplicated, block: B:7:0x003e  */
            public /* synthetic */ void lambda$didPressPhoneNumber$24(TLObject tLObject, String str, final Browser.Progress progress, final Utilities.Callback callback) {
                TLRPC.User user;
                if (tLObject instanceof TLRPC.TL_contacts_resolvedPeer) {
                    TLRPC.TL_contacts_resolvedPeer tL_contacts_resolvedPeer = (TLRPC.TL_contacts_resolvedPeer) tLObject;
                    AyuMessageHistory.this.getMessagesController().putUsers(tL_contacts_resolvedPeer.users, false);
                    AyuMessageHistory.this.getMessagesController().putChats(tL_contacts_resolvedPeer.chats, false);
                    long peerDialogId = DialogObject.getPeerDialogId(tL_contacts_resolvedPeer.peer);
                    if (peerDialogId >= 0) {
                        user = AyuMessageHistory.this.getMessagesController().getUser(Long.valueOf(peerDialogId));
                    } else {
                        user = null;
                    }
                } else {
                    user = null;
                }
                if (user == null) {
                    Long l = Utilities.parseLong(str);
                    if (l.longValue() != 0) {
                        ChatUtils.getInstance().searchUserById(l, new Utilities.Callback() { // from class: com.radolyn.ayugram.ui.AyuMessageHistory$ChatActivityAdapter$2$$ExternalSyntheticLambda29
                            @Override // org.telegram.messenger.Utilities.Callback
                            public final void run(Object obj) {
                                AyuMessageHistory.ChatActivityAdapter.AnonymousClass2.m2395$r8$lambda$_drkwOUNT5ovaN3A30S86cf3O8(progress, callback, (TLRPC.User) obj);
                            }
                        });
                        return;
                    }
                }
                progress.end();
                callback.run(user);
            }

            /* JADX INFO: renamed from: $r8$lambda$_dr-kwOUNT5ovaN3A30S86cf3O8, reason: not valid java name */
            public static /* synthetic */ void m2395$r8$lambda$_drkwOUNT5ovaN3A30S86cf3O8(Browser.Progress progress, Utilities.Callback callback, TLRPC.User user) {
                progress.end();
                callback.run(user);
            }

            /* JADX INFO: Access modifiers changed from: private */
            public /* synthetic */ void lambda$didPressPhoneNumber$26(int i) {
                AyuMessageHistory.this.getConnectionsManager().cancelRequest(i, true);
            }

            @Override // org.telegram.ui.Cells.ChatMessageCell.ChatMessageCellDelegate
            public void needOpenWebView(MessageObject messageObject, String str, String str2, String str3, String str4, int i, int i2) {
                AyuMessageHistory ayuMessageHistory = AyuMessageHistory.this;
                EmbedBottomSheet.show(ayuMessageHistory, messageObject, ayuMessageHistory.provider, str2, str3, str4, str, i, i2, false);
            }

            /* JADX WARN: Code duplicated, block: B:54:0x011d  */
            @Override // org.telegram.ui.Cells.ChatMessageCell.ChatMessageCellDelegate
            public void didPressImage(ChatMessageCell chatMessageCell, float f, float f2, boolean z) {
                int i;
                File file;
                MessageObject messageObject = chatMessageCell.getMessageObject();
                if (messageObject.getInputStickerSet() != null) {
                    AyuMessageHistory.this.showDialog(new StickersAlert(AyuMessageHistory.this.getParentActivity(), AyuMessageHistory.this, messageObject.getInputStickerSet(), null, null, false));
                    return;
                }
                if (messageObject.isVideo() || (i = messageObject.type) == 1 || ((i == 0 && !messageObject.isWebpageDocument()) || messageObject.isGif())) {
                    PhotoViewer.getInstance().setParentActivity(AyuMessageHistory.this);
                    PhotoViewer.getInstance().openPhoto(messageObject, (ChatActivity) null, 0L, 0L, 0L, AyuMessageHistory.this.provider);
                    return;
                }
                int i2 = messageObject.type;
                File pathToMessage = null;
                if (i2 == 3) {
                    try {
                        String str = messageObject.messageOwner.attachPath;
                        if (str != null && str.length() != 0) {
                            pathToMessage = new File(messageObject.messageOwner.attachPath);
                        }
                        if (pathToMessage == null || !pathToMessage.exists()) {
                            pathToMessage = AyuMessageHistory.this.getFileLoader().getPathToMessage(messageObject.messageOwner);
                        }
                        Intent intent = new Intent("android.intent.action.VIEW");
                        if (Build.VERSION.SDK_INT >= 24) {
                            intent.setFlags(1);
                            intent.setDataAndType(FileProvider.getUriForFile(AyuMessageHistory.this.getParentActivity(), ApplicationLoader.getApplicationId() + ".provider", pathToMessage), "video/mp4");
                        } else {
                            intent.setDataAndType(Uri.fromFile(pathToMessage), "video/mp4");
                        }
                        AyuMessageHistory.this.getParentActivity().startActivityForResult(intent, 500);
                        return;
                    } catch (Exception unused) {
                        AyuMessageHistory.this.alertUserOpenError(messageObject);
                        return;
                    }
                }
                if (i2 == 4) {
                    if (AndroidUtilities.isMapsInstalled(AyuMessageHistory.this)) {
                        LocationActivity locationActivity = new LocationActivity(0);
                        locationActivity.setMessageObject(messageObject);
                        AyuMessageHistory.this.presentFragment(locationActivity);
                        return;
                    }
                    return;
                }
                if (i2 == 9 || i2 == 0) {
                    if (messageObject.getDocumentName().toLowerCase().endsWith("attheme")) {
                        String str2 = messageObject.messageOwner.attachPath;
                        if (str2 == null || str2.length() == 0) {
                            file = null;
                        } else {
                            file = new File(messageObject.messageOwner.attachPath);
                            if (!file.exists()) {
                                file = null;
                            }
                        }
                        if (file == null) {
                            File pathToMessage2 = AyuMessageHistory.this.getFileLoader().getPathToMessage(messageObject.messageOwner);
                            if (pathToMessage2.exists()) {
                                file = pathToMessage2;
                            }
                        }
                        if (AyuMessageHistory.this.chatLayoutManager != null) {
                            if (AyuMessageHistory.this.chatLayoutManager.findLastVisibleItemPosition() < AyuMessageHistory.this.chatLayoutManager.getItemCount() - 1) {
                                AyuMessageHistory ayuMessageHistory = AyuMessageHistory.this;
                                ayuMessageHistory.scrollToPositionOnRecreate = ayuMessageHistory.chatLayoutManager.findFirstVisibleItemPosition();
                                RecyclerListView.Holder holder = (RecyclerListView.Holder) AyuMessageHistory.this.chatListView.findViewHolderForAdapterPosition(AyuMessageHistory.this.scrollToPositionOnRecreate);
                                if (holder != null) {
                                    AyuMessageHistory.this.scrollToOffsetOnRecreate = holder.itemView.getTop();
                                } else {
                                    AyuMessageHistory.this.scrollToPositionOnRecreate = -1;
                                }
                            } else {
                                AyuMessageHistory.this.scrollToPositionOnRecreate = -1;
                            }
                        }
                        Theme.ThemeInfo themeInfoApplyThemeFile = Theme.applyThemeFile(file, messageObject.getDocumentName(), null, true);
                        if (themeInfoApplyThemeFile != null) {
                            AyuMessageHistory.this.presentFragment(new ThemePreviewActivity(themeInfoApplyThemeFile));
                            return;
                        }
                        AyuMessageHistory.this.scrollToPositionOnRecreate = -1;
                    }
                    try {
                        AndroidUtilities.openForView(messageObject, AyuMessageHistory.this.getParentActivity(), null, false);
                    } catch (Exception unused2) {
                        AyuMessageHistory.this.alertUserOpenError(messageObject);
                    }
                }
            }

            @Override // org.telegram.ui.Cells.ChatMessageCell.ChatMessageCellDelegate
            public void didPressInstantButton(ChatMessageCell chatMessageCell, int i) {
                TLRPC.WebPage webPage;
                MessageObject messageObject = chatMessageCell.getMessageObject();
                if (i == 0) {
                    TLRPC.MessageMedia messageMedia = messageObject.messageOwner.media;
                    if (messageMedia == null || (webPage = messageMedia.webpage) == null || webPage.cached_page == null) {
                        return;
                    }
                    ArticleViewer.getInstance().setParentActivity(AyuMessageHistory.this.getParentActivity(), AyuMessageHistory.this);
                    ArticleViewer.getInstance().open(messageObject);
                    return;
                }
                if (i == 5) {
                    AyuMessageHistory ayuMessageHistory = AyuMessageHistory.this;
                    TLRPC.User user = ayuMessageHistory.getMessagesController().getUser(Long.valueOf(messageObject.messageOwner.media.user_id));
                    TLRPC.MessageMedia messageMedia2 = messageObject.messageOwner.media;
                    ayuMessageHistory.openVCard(user, messageMedia2.vcard, messageMedia2.first_name, messageMedia2.last_name);
                    return;
                }
                TLRPC.MessageMedia messageMedia3 = messageObject.messageOwner.media;
                if (messageMedia3 == null || messageMedia3.webpage == null) {
                    return;
                }
                Browser.openUrl(AyuMessageHistory.this.getParentActivity(), messageObject.messageOwner.media.webpage.url);
            }

            @Override // org.telegram.ui.Cells.ChatMessageCell.ChatMessageCellDelegate
            public boolean didPressAnimatedEmoji(ChatMessageCell chatMessageCell, AnimatedEmojiSpan animatedEmojiSpan) {
                TLRPC.InputStickerSet inputStickerSet;
                if (AyuMessageHistory.this.getMessagesController().premiumFeaturesBlocked() || animatedEmojiSpan == null || animatedEmojiSpan.standard) {
                    return false;
                }
                long documentId = animatedEmojiSpan.getDocumentId();
                TLRPC.Document documentFindDocument = animatedEmojiSpan.document;
                if (documentFindDocument == null) {
                    documentFindDocument = AnimatedEmojiDrawable.findDocument(((BaseFragment) AyuMessageHistory.this).currentAccount, documentId);
                }
                if (documentFindDocument == null || (inputStickerSet = MessageObject.getInputStickerSet(documentFindDocument)) == null) {
                    return false;
                }
                ArrayList arrayList = new ArrayList(1);
                arrayList.add(inputStickerSet);
                AyuMessageHistory ayuMessageHistory = AyuMessageHistory.this;
                EmojiPacksAlert emojiPacksAlert = new EmojiPacksAlert(ayuMessageHistory, ayuMessageHistory.getParentActivity(), AyuMessageHistory.this.theme, arrayList);
                emojiPacksAlert.setPreviewEmoji(documentFindDocument);
                emojiPacksAlert.setCalcMandatoryInsets(AyuMessageHistory.this.contentView.getKeyboardHeight() > AndroidUtilities.dp(20.0f));
                AyuMessageHistory.this.showDialog(emojiPacksAlert);
                return true;
            }

            @Override // org.telegram.ui.Cells.ChatMessageCell.ChatMessageCellDelegate
            public void didLongPressBotButton(ChatMessageCell chatMessageCell, final TLRPC.KeyboardButton keyboardButton) {
                if ((keyboardButton instanceof TLRPC.TL_keyboardButtonSwitchInline) || (keyboardButton instanceof TLRPC.TL_keyboardButtonCallback) || (keyboardButton instanceof TLRPC.TL_keyboardButtonGame) || (keyboardButton instanceof TLRPC.TL_keyboardButtonUrl) || (keyboardButton instanceof TLRPC.TL_keyboardButtonBuy) || (keyboardButton instanceof TLRPC.TL_keyboardButtonUrlAuth) || (keyboardButton instanceof TLRPC.TL_keyboardButtonUserProfile) || (keyboardButton instanceof TLRPC.TL_keyboardButtonCopy) || (keyboardButton instanceof TLRPC.TL_keyboardButtonWebView)) {
                    if (keyboardButton instanceof TLRPC.TL_keyboardButtonCopy) {
                        didLongPressCopyButton(((TLRPC.TL_keyboardButtonCopy) keyboardButton).copy_text);
                        return;
                    }
                    BottomSheet.Builder builder = new BottomSheet.Builder(AyuMessageHistory.this.getParentActivity(), false, AyuMessageHistory.this.theme);
                    builder.setTitle(keyboardButton.text);
                    builder.setItems(new CharSequence[]{LocaleController.getString(R.string.CopyTitle), keyboardButton.data != null ? LocaleController.getString(R.string.CopyCallback) : null, !TextUtils.isEmpty(keyboardButton.url) ? LocaleController.getString(R.string.CopyLink) : null, keyboardButton.query != null ? LocaleController.getString(R.string.CopyInlineQuery) : null, keyboardButton.user_id != 0 ? LocaleController.getString(R.string.CopyID) : null}, new DialogInterface.OnClickListener() { // from class: com.radolyn.ayugram.ui.AyuMessageHistory$ChatActivityAdapter$2$$ExternalSyntheticLambda0
                        @Override // android.content.DialogInterface.OnClickListener
                        public final void onClick(DialogInterface dialogInterface, int i) {
                            this.f$0.lambda$didLongPressBotButton$27(keyboardButton, dialogInterface, i);
                        }
                    });
                    AyuMessageHistory.this.showDialog(builder.create());
                    try {
                        chatMessageCell.performHapticFeedback(VibratorUtils.getType(0), 1);
                    } catch (Exception unused) {
                    }
                }
            }

            /* JADX INFO: Access modifiers changed from: private */
            public /* synthetic */ void lambda$didLongPressBotButton$27(TLRPC.KeyboardButton keyboardButton, DialogInterface dialogInterface, int i) {
                if (i == 0) {
                    AndroidUtilities.addToClipboard(keyboardButton.text);
                } else if (i == 1) {
                    AndroidUtilities.addToClipboard(ChatUtils.getInstance().getTextFromCallback(keyboardButton.data));
                } else if (i == 2) {
                    AndroidUtilities.addToClipboard(keyboardButton.url);
                } else if (i == 3) {
                    AndroidUtilities.addToClipboard(keyboardButton.query);
                } else if (i == 4) {
                    AndroidUtilities.addToClipboard(String.valueOf(keyboardButton.user_id));
                }
                if (AyuMessageHistory.this.undoView != null) {
                    AyuMessageHistory.this.undoView.showWithAction(0L, 58, (Runnable) null);
                }
            }

            public void didLongPressCopyButton(final String str) {
                BottomSheet.Builder builder = new BottomSheet.Builder(AyuMessageHistory.this.getParentActivity(), false, AyuMessageHistory.this.theme);
                builder.setTitle(str);
                builder.setTitleMultipleLines(true);
                builder.setItems(new CharSequence[]{LocaleController.getString(R.string.Copy)}, new DialogInterface.OnClickListener() { // from class: com.radolyn.ayugram.ui.AyuMessageHistory$ChatActivityAdapter$2$$ExternalSyntheticLambda12
                    @Override // android.content.DialogInterface.OnClickListener
                    public final void onClick(DialogInterface dialogInterface, int i) {
                        this.f$0.lambda$didLongPressCopyButton$28(str, dialogInterface, i);
                    }
                });
                AyuMessageHistory.this.showDialog(builder.create());
            }

            /* JADX INFO: Access modifiers changed from: private */
            public /* synthetic */ void lambda$didLongPressCopyButton$28(String str, DialogInterface dialogInterface, int i) {
                AndroidUtilities.addToClipboard(str);
                BulletinFactory.of(AyuMessageHistory.this).createCopyBulletin(LocaleController.formatString(R.string.ExactTextCopied, str)).show();
            }
        }

        @Override // androidx.recyclerview.widget.RecyclerView.Adapter
        public void onBindViewHolder(RecyclerView.ViewHolder viewHolder, int i) {
            boolean z;
            boolean z2;
            boolean z3 = false;
            if (i == this.loadingUpRow) {
                ((ChatLoadingCell) viewHolder.itemView).setProgressVisible(false);
                return;
            }
            if (i < this.messagesStartRow || i >= this.messagesEndRow) {
                return;
            }
            ArrayList arrayList = AyuMessageHistory.this.messages;
            MessageObject messageObject = (MessageObject) arrayList.get((arrayList.size() - (i - this.messagesStartRow)) - 1);
            View view = viewHolder.itemView;
            if (view instanceof ChatMessageCell) {
                ChatMessageCell chatMessageCell = (ChatMessageCell) view;
                chatMessageCell.isChat = AyuMessageHistory.this.mode == 2 && AyuMessageHistory.this.currentChat != null;
                TLRPC.User user = AyuMessageHistory.this.currentUser;
                chatMessageCell.isBot = user != null && user.bot;
                int i2 = i + 1;
                int itemViewType = getItemViewType(i2);
                int itemViewType2 = getItemViewType(i - 1);
                if ((messageObject.messageOwner.reply_markup instanceof TLRPC.TL_replyInlineMarkup) || itemViewType != viewHolder.getItemViewType()) {
                    z = false;
                } else {
                    ArrayList arrayList2 = AyuMessageHistory.this.messages;
                    MessageObject messageObject2 = (MessageObject) arrayList2.get((arrayList2.size() - (i2 - this.messagesStartRow)) - 1);
                    z = messageObject2.isOutOwner() == messageObject.isOutOwner() && messageObject2.getFromChatId() == messageObject.getFromChatId() && Math.abs(messageObject2.messageOwner.date - messageObject.messageOwner.date) <= 300;
                }
                if (itemViewType2 == viewHolder.getItemViewType()) {
                    ArrayList arrayList3 = AyuMessageHistory.this.messages;
                    MessageObject messageObject3 = (MessageObject) arrayList3.get(arrayList3.size() - (i - this.messagesStartRow));
                    z2 = !(messageObject3.messageOwner.reply_markup instanceof TLRPC.TL_replyInlineMarkup) && messageObject3.isOutOwner() == messageObject.isOutOwner() && messageObject3.getFromChatId() == messageObject.getFromChatId() && Math.abs(messageObject3.messageOwner.date - messageObject.messageOwner.date) <= 300;
                } else {
                    z2 = false;
                }
                chatMessageCell.setMessageObject(messageObject, null, z, z2, false);
                if (AyuMessageHistory.this.highlightMessageId != Integer.MAX_VALUE && messageObject.getId() == AyuMessageHistory.this.highlightMessageId) {
                    z3 = true;
                }
                chatMessageCell.setHighlighted(z3);
                chatMessageCell.setHighlightedText(null);
                return;
            }
            if (view instanceof ChatActionCell) {
                ChatActionCell chatActionCell = (ChatActionCell) view;
                chatActionCell.setMessageObject(messageObject);
                chatActionCell.setAlpha(1.0f);
            }
        }

        @Override // androidx.recyclerview.widget.RecyclerView.Adapter
        public int getItemViewType(int i) {
            if (i < this.messagesStartRow || i >= this.messagesEndRow) {
                return 4;
            }
            ArrayList arrayList = AyuMessageHistory.this.messages;
            return ((MessageObject) arrayList.get((arrayList.size() - (i - this.messagesStartRow)) - 1)).contentType;
        }

        @Override // androidx.recyclerview.widget.RecyclerView.Adapter
        public void onViewAttachedToWindow(final RecyclerView.ViewHolder viewHolder) {
            final View view = viewHolder.itemView;
            if ((view instanceof ChatMessageCell) || (view instanceof ChatActionCell)) {
                view.getViewTreeObserver().addOnPreDrawListener(new ViewTreeObserver.OnPreDrawListener() { // from class: com.radolyn.ayugram.ui.AyuMessageHistory.ChatActivityAdapter.5
                    @Override // android.view.ViewTreeObserver.OnPreDrawListener
                    public boolean onPreDraw() {
                        view.getViewTreeObserver().removeOnPreDrawListener(this);
                        int measuredHeight = AyuMessageHistory.this.chatListView.getMeasuredHeight();
                        int top = view.getTop();
                        view.getBottom();
                        int i = top >= 0 ? 0 : -top;
                        int measuredHeight2 = view.getMeasuredHeight();
                        if (measuredHeight2 > measuredHeight) {
                            measuredHeight2 = i + measuredHeight;
                        }
                        View view2 = viewHolder.itemView;
                        if (view2 instanceof ChatMessageCell) {
                            ((ChatMessageCell) view).setVisiblePart(i, measuredHeight2 - i, AyuMessageHistory.this.contentView.getHeightWithKeyboard() - AyuMessageHistory.this.chatListView.getTop(), 0.0f, (view.getY() + ((BaseFragment) AyuMessageHistory.this).actionBar.getMeasuredHeight()) - AyuMessageHistory.this.contentView.getBackgroundTranslationY(), AyuMessageHistory.this.contentView.getMeasuredWidth(), AyuMessageHistory.this.contentView.getBackgroundSizeY(), 0, 0, 0);
                        } else if ((view2 instanceof ChatActionCell) && ((BaseFragment) AyuMessageHistory.this).actionBar != null && AyuMessageHistory.this.contentView != null) {
                            View view3 = view;
                            ((ChatActionCell) view3).setVisiblePart((view3.getY() + ((BaseFragment) AyuMessageHistory.this).actionBar.getMeasuredHeight()) - AyuMessageHistory.this.contentView.getBackgroundTranslationY(), AyuMessageHistory.this.contentView.getBackgroundSizeY());
                        }
                        AyuMessageHistory.this.updateMessagesVisiblePart();
                        return true;
                    }
                });
            }
            View view2 = viewHolder.itemView;
            if (view2 instanceof ChatMessageCell) {
                ChatMessageCell chatMessageCell = (ChatMessageCell) view2;
                chatMessageCell.setBackgroundDrawable(null);
                chatMessageCell.setCheckPressed(true, false);
                chatMessageCell.setHighlighted(false);
            }
        }

        @Override // androidx.recyclerview.widget.RecyclerView.Adapter
        public void notifyDataSetChanged() {
            updateRows();
            try {
                super.notifyDataSetChanged();
            } catch (Exception e) {
                FileLog.e(e);
            }
        }

        @Override // androidx.recyclerview.widget.RecyclerView.Adapter
        public void notifyItemChanged(int i) {
            updateRows();
            try {
                super.notifyItemChanged(i);
            } catch (Exception e) {
                FileLog.e(e);
            }
        }

        @Override // androidx.recyclerview.widget.RecyclerView.Adapter
        public void notifyItemRangeChanged(int i, int i2) {
            updateRows();
            try {
                super.notifyItemRangeChanged(i, i2);
            } catch (Exception e) {
                FileLog.e(e);
            }
        }

        @Override // androidx.recyclerview.widget.RecyclerView.Adapter
        public void notifyItemInserted(int i) {
            updateRows();
            try {
                super.notifyItemInserted(i);
            } catch (Exception e) {
                FileLog.e(e);
            }
        }

        @Override // androidx.recyclerview.widget.RecyclerView.Adapter
        public void notifyItemMoved(int i, int i2) {
            updateRows();
            try {
                super.notifyItemMoved(i, i2);
            } catch (Exception e) {
                FileLog.e(e);
            }
        }

        @Override // androidx.recyclerview.widget.RecyclerView.Adapter
        public void notifyItemRangeInserted(int i, int i2) {
            updateRows();
            try {
                super.notifyItemRangeInserted(i, i2);
            } catch (Exception e) {
                FileLog.e(e);
            }
        }

        @Override // androidx.recyclerview.widget.RecyclerView.Adapter
        public void notifyItemRemoved(int i) {
            updateRows();
            try {
                super.notifyItemRemoved(i);
            } catch (Exception e) {
                FileLog.e(e);
            }
        }

        @Override // androidx.recyclerview.widget.RecyclerView.Adapter
        public void notifyItemRangeRemoved(int i, int i2) {
            updateRows();
            try {
                super.notifyItemRangeRemoved(i, i2);
            } catch (Exception e) {
                FileLog.e(e);
            }
        }
    }

    /* JADX INFO: renamed from: com.radolyn.ayugram.ui.AyuMessageHistory$18, reason: invalid class name */
    static /* synthetic */ class AnonymousClass18 {
        static final /* synthetic */ int[] $SwitchMap$org$telegram$ui$AvatarPreviewer$MenuItem;

        static {
            int[] iArr = new int[AvatarPreviewer.MenuItem.values().length];
            $SwitchMap$org$telegram$ui$AvatarPreviewer$MenuItem = iArr;
            try {
                iArr[AvatarPreviewer.MenuItem.SEND_MESSAGE.ordinal()] = 1;
            } catch (NoSuchFieldError unused) {
            }
            try {
                $SwitchMap$org$telegram$ui$AvatarPreviewer$MenuItem[AvatarPreviewer.MenuItem.OPEN_PROFILE.ordinal()] = 2;
            } catch (NoSuchFieldError unused2) {
            }
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void updateMessageAnimatedInternal(MessageObject messageObject, boolean z) {
        if (this.chatAdapter == null || this.fragmentView == null) {
            return;
        }
        if (z) {
            messageObject.forceUpdate = true;
            messageObject.reactionsChanged = true;
        }
        int iIndexOf = this.messages.indexOf(messageObject);
        if (iIndexOf >= 0) {
            this.chatAdapter.notifyItemChanged((this.messages.size() - (iIndexOf - this.chatAdapter.messagesStartRow)) - 1);
        }
    }

    /* JADX WARN: Code duplicated, block: B:21:0x003a  */
    public void saveScrollPosition(boolean z) {
        long j;
        int id;
        RecyclerListView recyclerListView = this.chatListView;
        if (recyclerListView == null || this.chatLayoutManager == null || recyclerListView.getChildCount() <= 0) {
            return;
        }
        int top = z ? Integer.MAX_VALUE : Integer.MIN_VALUE;
        View view = null;
        int i = -1;
        for (int i2 = 0; i2 < this.chatListView.getChildCount(); i2++) {
            View childAt = this.chatListView.getChildAt(i2);
            int childAdapterPosition = this.chatListView.getChildAdapterPosition(childAt);
            if (childAdapterPosition >= 0) {
                int top2 = childAt.getTop();
                if (z) {
                    if (top2 < top) {
                        top = childAt.getTop();
                        view = childAt;
                        i = childAdapterPosition;
                    }
                } else if (top2 > top) {
                    top = childAt.getTop();
                    view = childAt;
                    i = childAdapterPosition;
                }
            }
        }
        if (view != null) {
            if (view instanceof ChatMessageCell) {
                id = ((ChatMessageCell) view).getMessageObject().getId();
            } else {
                if (view instanceof ChatActionCell) {
                    id = ((ChatActionCell) view).getMessageObject().getId();
                } else {
                    j = 0;
                }
                this.savedScrollMsgId = j;
                this.savedScrollPosition = i;
                this.savedScrollOffset = getScrollingOffsetForView(view);
            }
            j = id;
            this.savedScrollMsgId = j;
            this.savedScrollPosition = i;
            this.savedScrollOffset = getScrollingOffsetForView(view);
        }
    }

    private int getScrollingOffsetForView(View view) {
        return (this.chatListView.getMeasuredHeight() - view.getBottom()) - this.chatListView.getPaddingBottom();
    }

    public void applyScrolledPosition() {
        int i;
        if (this.chatListView == null || this.chatLayoutManager == null || (i = this.savedScrollPosition) < 0) {
            return;
        }
        if (this.savedScrollMsgId != 0) {
            for (int i2 = 0; i2 < this.chatAdapter.getItemCount(); i2++) {
                MessageObject messageObject = this.chatAdapter.getMessageObject(i2);
                if (messageObject != null && messageObject.getId() == this.savedScrollMsgId) {
                    i = i2;
                    break;
                }
            }
        }
        this.chatLayoutManager.scrollToPositionWithOffset(i, this.savedScrollOffset, true);
        this.savedScrollPosition = -1;
        this.savedScrollMsgId = 0L;
    }

    @Override // org.telegram.ui.ActionBar.BaseFragment
    public WindowInsetsCompat onInsetsInternal(View view, WindowInsetsCompat windowInsetsCompat) {
        this.windowInsetsStateHolder.setInsets(windowInsetsCompat);
        return WindowInsetsCompat.CONSUMED;
    }
}
