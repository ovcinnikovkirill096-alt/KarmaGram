package com.exteragram.messenger.components;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.drawable.Drawable;
import android.media.MediaMetadataRetriever;
import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import androidx.core.content.ContextCompat;
import androidx.core.content.FileProvider;
import androidx.exifinterface.media.ExifInterface;
import com.exteragram.messenger.ExteraConfig;
import com.exteragram.messenger.utils.MediaUtils;
import com.exteragram.messenger.utils.chats.ChatUtils;
import com.google.zxing.Dimension;
import com.radolyn.ayugram.AyuConfig;
import com.radolyn.ayugram.controllers.AyuAttachments;
import com.radolyn.ayugram.controllers.AyuMessagesController;
import com.radolyn.ayugram.controllers.AyuSpyController;
import com.radolyn.ayugram.database.entities.DeletedMessage;
import com.radolyn.ayugram.database.entities.DeletedMessageFull;
import com.radolyn.ayugram.database.entities.SpyMessageContentsRead;
import com.radolyn.ayugram.database.entities.SpyMessageRead;
import com.radolyn.ayugram.utils.AyuMessageUtils;
import j$.util.Objects;
import java.io.File;
import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;
import kotlin.Triple;
import org.telegram.messenger.AndroidUtilities;
import org.telegram.messenger.ApplicationLoader;
import org.telegram.messenger.ContactsController;
import org.telegram.messenger.DialogObject;
import org.telegram.messenger.FileLoader;
import org.telegram.messenger.FileLog;
import org.telegram.messenger.LocaleController;
import org.telegram.messenger.MediaDataController;
import org.telegram.messenger.MessageObject;
import org.telegram.messenger.R;
import org.telegram.messenger.UserConfig;
import org.telegram.messenger.UserObject;
import org.telegram.messenger.Utilities;
import org.telegram.messenger.browser.Browser;
import org.telegram.tgnet.TLRPC;
import org.telegram.ui.ActionBar.ActionBarMenuSubItem;
import org.telegram.ui.ActionBar.BaseFragment;
import org.telegram.ui.ActionBar.Theme;
import org.telegram.ui.Components.AnimatedFloat;
import org.telegram.ui.Components.CubicBezierInterpolator;
import org.telegram.ui.Components.LayoutHelper;
import org.telegram.ui.Components.PopupSwipeBackLayout;
import org.telegram.ui.ProfileActivity;

public abstract class MessageDetailsPopupWrapper {
    private final int AYU_CONTENTS_READ_DATE;
    private final int AYU_DELETED_DATE;
    private final int AYU_READ_DATE;
    private final int BITRATE;
    private final int FILE_PATH;
    private final int LOCATION;
    private final int PLATFORM;
    private final int RESOLUTION;
    private final int SET_OWNER;
    private String filePath;
    private final BaseFragment fragment;
    private String[] geo;
    private long ownerId;
    private final Theme.ResourcesProvider resourcesProvider;
    public LinearLayout swipeBack;

    protected void closeMenu() {
    }

    protected abstract void copy(String str);

    /* JADX WARN: Code duplicated, block: B:187:0x0513  */
    /* JADX WARN: Code duplicated, block: B:231:0x0677  */
    /* JADX WARN: Code duplicated, block: B:233:0x0685  */
    /* JADX WARN: Code duplicated, block: B:235:0x0688  */
    /* JADX WARN: Code duplicated, block: B:236:0x0693  */
    /* JADX WARN: Code duplicated, block: B:238:0x0696  */
    /* JADX WARN: Code duplicated, block: B:41:0x0187  */
    /* JADX WARN: Multi-variable type inference failed */
    /* JADX WARN: Type inference failed for: r1v0, types: [com.exteragram.messenger.components.MessageDetailsPopupWrapper, java.lang.Object] */
    /* JADX WARN: Type inference failed for: r1v1, types: [com.exteragram.messenger.components.MessageDetailsPopupWrapper] */
    /* JADX WARN: Type inference failed for: r1v2 */
    /* JADX WARN: Type inference failed for: r1v5, types: [com.exteragram.messenger.components.MessageDetailsPopupWrapper] */
    public MessageDetailsPopupWrapper(final BaseFragment baseFragment, PopupSwipeBackLayout popupSwipeBackLayout, final MessageObject messageObject, Theme.ResourcesProvider resourcesProvider) {
        int i;
        final Item item;
        ScrollView scrollView;
        int i2;
        TLRPC.InputStickerSet inputStickerSet;
        int i3;
        final PopupSwipeBackLayout popupSwipeBackLayout2 = popupSwipeBackLayout;
        final MessageObject messageObject2 = messageObject;
        final ?? obj = new Object();
        obj.SET_OWNER = 0;
        obj.FILE_PATH = 1;
        obj.LOCATION = 2;
        obj.BITRATE = 3;
        obj.RESOLUTION = 4;
        obj.PLATFORM = 5;
        obj.AYU_DELETED_DATE = 50;
        obj.AYU_READ_DATE = 51;
        obj.AYU_CONTENTS_READ_DATE = 52;
        obj.ownerId = 0L;
        obj.fragment = baseFragment;
        obj.resourcesProvider = resourcesProvider;
        final Activity parentActivity = baseFragment.getParentActivity();
        LinearLayout linearLayout = new LinearLayout(parentActivity);
        obj.swipeBack = linearLayout;
        linearLayout.setOrientation(1);
        ScrollView scrollView2 = new ScrollView(parentActivity) { // from class: com.exteragram.messenger.components.MessageDetailsPopupWrapper.1
            final AnimatedFloat alphaFloat = new AnimatedFloat(this, 350, CubicBezierInterpolator.EASE_OUT_QUINT);
            Drawable topShadowDrawable;
            private boolean wasCanScrollVertically;

            @Override // android.widget.ScrollView, android.view.ViewGroup, android.view.ViewParent
            public void onNestedScroll(View view, int i4, int i5, int i6, int i7) {
                super.onNestedScroll(view, i4, i5, i6, i7);
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
        scrollView2.addView(linearLayout2);
        linearLayout2.setOrientation(1);
        ActionBarMenuSubItem actionBarMenuSubItem = new ActionBarMenuSubItem((Context) baseFragment.getParentActivity(), true, false, resourcesProvider);
        actionBarMenuSubItem.setItemHeight(44);
        actionBarMenuSubItem.setTextAndIcon(LocaleController.getString(R.string.Back), R.drawable.msg_arrow_back);
        actionBarMenuSubItem.getTextView().setPadding(LocaleController.isRTL ? 0 : AndroidUtilities.dp(40.0f), 0, LocaleController.isRTL ? AndroidUtilities.dp(40.0f) : 0, 0);
        actionBarMenuSubItem.setOnClickListener(new View.OnClickListener() { // from class: com.exteragram.messenger.components.MessageDetailsPopupWrapper$$ExternalSyntheticLambda0
            @Override // android.view.View.OnClickListener
            public final void onClick(View view) {
                popupSwipeBackLayout2.closeForeground();
            }
        });
        obj.swipeBack.addView(actionBarMenuSubItem, LayoutHelper.createLinear(-1, -2));
        linearLayout2.addView(obj.createGap(), LayoutHelper.createLinear(-1, 8));
        ArrayList arrayList = new ArrayList();
        int i4 = messageObject2.messageOwner.views;
        if (i4 > 0) {
            arrayList.add(new Item(R.drawable.msg_view_file, String.format(LocaleController.getPluralString("Views", i4), AndroidUtilities.formatCount(messageObject2.messageOwner.views)), (String) null));
        }
        int i5 = messageObject2.messageOwner.forwards;
        if (i5 > 0) {
            arrayList.add(new Item(R.drawable.msg_forward, String.format(LocaleController.getPluralString("Shares", i5), AndroidUtilities.formatCount(messageObject2.messageOwner.forwards)), (String) null));
        }
        if (!arrayList.isEmpty()) {
            arrayList.add(null);
        }
        arrayList.add(new Item(R.drawable.msg_info, "ID", messageObject2.messageOwner.id));
        if (messageObject2.messageOwner.date > 0) {
            arrayList.add(new Item(R.drawable.msg_calendar2, LocaleController.getString(R.string.Date), obj.formatTime(messageObject2.messageOwner.date, true)));
        }
        if (AyuConfig.saveDeletedMessages && messageObject2.messageOwner.ayuDeleted) {
            arrayList.add(new Item(50, R.drawable.msg_delete, LocaleController.getString(R.string.DeleteDateMenuText), "..."));
        }
        if (AyuConfig.saveReadDate) {
            TLRPC.Message message = messageObject2.messageOwner;
            if (!message.unread && !DialogObject.isUserDialog(message.dialog_id)) {
                arrayList.add(new Item(51, R.drawable.msg_seen, LocaleController.getString(R.string.ReadDateMenuText), "..."));
            }
        }
        if (AyuConfig.saveReadDate) {
            TLRPC.Message message2 = messageObject2.messageOwner;
            if (!message2.media_unread || message2.ayuDeleted) {
                arrayList.add(new Item(52, R.drawable.msg_help_14, LocaleController.getString(R.string.ContentsReadDateMenuText), "..."));
            }
        }
        TLRPC.Message message3 = messageObject2.messageOwner;
        TLRPC.MessageFwdHeader messageFwdHeader = message3.fwd_from;
        if (messageFwdHeader != null && (i3 = messageFwdHeader.date) > 0 && i3 != message3.date) {
            arrayList.add(new Item(R.drawable.msg_recent, LocaleController.getString(R.string.ForwardedDate), obj.formatTime(messageObject2.messageOwner.fwd_from.date, true)));
        }
        TLRPC.Message message4 = messageObject2.messageOwner;
        int i6 = message4.edit_date;
        if (i6 > 0 && i6 != message4.date && !message4.edit_hide) {
            arrayList.add(new Item(R.drawable.msg_edit, LocaleController.getString(R.string.EditedDate), obj.formatTime(messageObject2.messageOwner.edit_date, true)));
        }
        arrayList.add(null);
        if (messageObject2.messageOwner.ttl > 0) {
            Triple ttl = AyuMessageUtils.formatTTL(messageObject2, false);
            CharSequence charSequence = ttl != null ? (CharSequence) ttl.getFirst() : null;
            if (!TextUtils.isEmpty(charSequence)) {
                arrayList.add(new Item(R.drawable.msg_autodelete, "TTL", String.valueOf(charSequence)));
                arrayList.add(null);
            }
        }
        if (messageObject2.getSize() > 0) {
            arrayList.add(new Item(R.drawable.msg_sendfile, LocaleController.getString(R.string.FileSize), AndroidUtilities.formatFileSize(messageObject2.getSize())));
        }
        if (messageObject2.getMimeType() != null && !messageObject2.getMimeType().isEmpty()) {
            arrayList.add(new Item(R.drawable.msg_media, LocaleController.getString(R.string.MimeType), messageObject2.getMimeType()));
        }
        if (MessageObject.getMedia(messageObject2.messageOwner) != null && MessageObject.getMedia(messageObject2.messageOwner).document != null) {
            ArrayList<TLRPC.DocumentAttribute> arrayList2 = MessageObject.getMedia(messageObject2.messageOwner).document.attributes;
            int size = arrayList2.size();
            int i7 = 0;
            while (i7 < size) {
                TLRPC.DocumentAttribute documentAttribute = arrayList2.get(i7);
                i7++;
                TLRPC.DocumentAttribute documentAttribute2 = documentAttribute;
                if (documentAttribute2 instanceof TLRPC.TL_documentAttributeFilename) {
                    arrayList.add(new Item(R.drawable.msg_log, LocaleController.getString(R.string.FileName), documentAttribute2.file_name));
                }
                if ((documentAttribute2 instanceof TLRPC.TL_documentAttributeSticker) && (inputStickerSet = documentAttribute2.stickerset) != null) {
                    long jExtractOwnerId = ChatUtils.extractOwnerId(inputStickerSet.id);
                    obj.ownerId = jExtractOwnerId;
                    if (jExtractOwnerId > 0) {
                        arrayList.add(new Item(0, R.drawable.msg_sticker, LocaleController.getString(R.string.ChannelCreator), String.valueOf(obj.ownerId)));
                    }
                }
                arrayList2 = arrayList2;
            }
        }
        String str = messageObject2.messageOwner.attachPath;
        obj.filePath = str;
        if (!TextUtils.isEmpty(str) && !new File(obj.filePath).exists()) {
            obj.filePath = null;
        }
        if (TextUtils.isEmpty(obj.filePath)) {
            obj.filePath = FileLoader.getInstance(UserConfig.selectedAccount).getPathToMessage(messageObject2.messageOwner).toString();
            if (!new File(obj.filePath).exists()) {
                obj.filePath = null;
            }
        }
        if (TextUtils.isEmpty(obj.filePath)) {
            obj.filePath = FileLoader.getInstance(UserConfig.selectedAccount).getPathToAttach(messageObject2.getDocument(), true).toString();
            if (!new File(obj.filePath).isFile()) {
                obj.filePath = null;
            }
        }
        if (TextUtils.isEmpty(obj.filePath) && AyuMessageUtils.getMedia(messageObject2.messageOwner) != null) {
            obj.filePath = AyuAttachments.getInstance(UserConfig.selectedAccount).getExistingPath(messageObject2, false);
            if (!new File(obj.filePath).isFile()) {
                obj.filePath = null;
            }
        }
        if (!TextUtils.isEmpty(obj.filePath)) {
            arrayList.add(new Item(1, R.drawable.msg_map, LocaleController.getString(R.string.FilePath), LocaleController.getString(R.string.Open)));
        }
        boolean z = messageObject2.isVoice() || messageObject2.isMusic();
        final boolean z2 = messageObject2.isVideo() || messageObject2.isRoundVideo() || messageObject2.isVideoSticker() || messageObject2.isGif();
        boolean zIsPhotoAsDocument = obj.isPhotoAsDocument(messageObject2);
        boolean z3 = zIsPhotoAsDocument || messageObject2.isPhoto() || messageObject2.isSticker();
        if (z3 && !TextUtils.isEmpty(obj.filePath)) {
            arrayList.add(new Item(5, R.drawable.menu_devices, LocaleController.getString(R.string.Platform), LocaleController.getString(R.string.NumberUnknown)));
        }
        if (z2 || z3) {
            arrayList.add(new Item(4, R.drawable.msg_photo_crop, LocaleController.getString(R.string.Resolution), "0x0"));
        }
        if (zIsPhotoAsDocument && !TextUtils.isEmpty(obj.filePath)) {
            arrayList.add(new Item(2, R.drawable.msg_location, LocaleController.getString(R.string.ShareLocation), "0.0, 0.0"));
        }
        if (z2 || z) {
            arrayList.add(new Item(3, R.drawable.msg_noise_on, LocaleController.getString(R.string.Bitrate), "0 Kbps"));
            int duration = (int) messageObject2.getDuration();
            if (duration > 0) {
                arrayList.add(new Item(R.drawable.msg2_animations, LocaleController.getString(R.string.Duration), AndroidUtilities.formatShortDuration(duration)));
            }
        }
        if (MessageObject.getMedia(messageObject2.messageOwner) == null) {
            i = 0;
        } else if (MessageObject.getMedia(messageObject2.messageOwner).photo != null && MessageObject.getMedia(messageObject2.messageOwner).photo.dc_id > 0) {
            i = MessageObject.getMedia(messageObject2.messageOwner).photo.dc_id;
        } else if (MessageObject.getMedia(messageObject2.messageOwner).document != null && MessageObject.getMedia(messageObject2.messageOwner).document.dc_id > 0) {
            i = MessageObject.getMedia(messageObject2.messageOwner).document.dc_id;
        } else if (MessageObject.getMedia(messageObject2.messageOwner).webpage != null && MessageObject.getMedia(messageObject2.messageOwner).webpage.photo != null && MessageObject.getMedia(messageObject2.messageOwner).webpage.photo.dc_id > 0) {
            i = MessageObject.getMedia(messageObject2.messageOwner).webpage.photo.dc_id;
        } else if (MessageObject.getMedia(messageObject2.messageOwner).webpage == null || MessageObject.getMedia(messageObject2.messageOwner).webpage.document == null || MessageObject.getMedia(messageObject2.messageOwner).webpage.document.dc_id <= 0) {
            i = 0;
        } else {
            i = MessageObject.getMedia(messageObject2.messageOwner).webpage.document.dc_id;
        }
        if (i != 0) {
            arrayList.add(new Item(R.drawable.msg_satellite, LocaleController.getString(R.string.Datacenter), String.format(Locale.ROOT, "DC%d, %s", Integer.valueOf(i), ChatUtils.getDCName(i))));
        }
        if (arrayList.get(arrayList.size() - 1) == null) {
            arrayList.remove(arrayList.size() - 1);
        }
        int size2 = arrayList.size();
        int i8 = 0;
        int i9 = 0;
        while (i9 < size2) {
            i9++;
            final Item item2 = (Item) arrayList.get(i9);
            if (item2 == null) {
                linearLayout2.addView(obj.createGap(), LayoutHelper.createLinear(-1, 8));
                i8 += 8;
            } else {
                ScrollView scrollView3 = scrollView2;
                final ActionBarMenuSubItem actionBarMenuSubItem2 = new ActionBarMenuSubItem((Context) baseFragment.getParentActivity(), false, false, resourcesProvider);
                actionBarMenuSubItem2.setTextAndIcon(item2.title, item2.resId);
                actionBarMenuSubItem2.setMinimumWidth(AndroidUtilities.dp(196.0f));
                actionBarMenuSubItem2.setOnClickListener(new View.OnClickListener() { // from class: com.exteragram.messenger.components.MessageDetailsPopupWrapper$$ExternalSyntheticLambda6
                    @Override // android.view.View.OnClickListener
                    public final void onClick(View view) {
                        popupSwipeBackLayout2.closeForeground();
                    }
                });
                linearLayout2.addView(actionBarMenuSubItem2, LayoutHelper.createLinear(-1, 48));
                int i10 = i8 + 48;
                String str2 = item2.subtitle;
                if (str2 != null) {
                    actionBarMenuSubItem2.setSubtext(str2);
                    actionBarMenuSubItem2.subtextView.setEllipsize(TextUtils.TruncateAt.MARQUEE);
                    actionBarMenuSubItem2.subtextView.setMarqueeRepeatLimit(-1);
                    actionBarMenuSubItem2.subtextView.setSelected(true);
                    actionBarMenuSubItem2.setItemHeight(56);
                    i10 = i8 + 56;
                }
                int i11 = item2.id;
                LinearLayout linearLayout3 = linearLayout2;
                if (i11 == 0 && obj.ownerId > 0) {
                    ChatUtils.getInstance().searchUserById(Long.valueOf(obj.ownerId), new Utilities.Callback() { // from class: com.exteragram.messenger.components.MessageDetailsPopupWrapper$$ExternalSyntheticLambda7
                        @Override // org.telegram.messenger.Utilities.Callback
                        public final void run(Object obj2) {
                            MessageDetailsPopupWrapper.$r8$lambda$dE2BmWFMp3ZO7Tcpe0bsPv1CgxY(item2, actionBarMenuSubItem2, (TLRPC.User) obj2);
                        }
                    });
                } else if (i11 == 2) {
                    ChatUtils.utilsQueue.postRunnable(new Runnable() { // from class: com.exteragram.messenger.components.MessageDetailsPopupWrapper$$ExternalSyntheticLambda8
                        @Override // java.lang.Runnable
                        public final void run() {
                            this.f$0.lambda$new$4(actionBarMenuSubItem2, item2);
                        }
                    });
                } else {
                    if (i11 == 3) {
                        ChatUtils.utilsQueue.postRunnable(new Runnable() { // from class: com.exteragram.messenger.components.MessageDetailsPopupWrapper$$ExternalSyntheticLambda9
                            @Override // java.lang.Runnable
                            public final void run() {
                                this.f$0.lambda$new$6(messageObject2, actionBarMenuSubItem2, item2);
                            }
                        });
                    } else if (i11 == 4) {
                        item = item2;
                        z2 = z2;
                        scrollView = scrollView3;
                        ChatUtils.utilsQueue.postRunnable(new Runnable() { // from class: com.exteragram.messenger.components.MessageDetailsPopupWrapper$$ExternalSyntheticLambda10
                            @Override // java.lang.Runnable
                            public final void run() {
                                this.f$0.lambda$new$8(z2, messageObject2, actionBarMenuSubItem2, item);
                            }
                        });
                    } else {
                        item = item2;
                        z2 = z2;
                        scrollView = scrollView3;
                        if (i11 == 5) {
                            ChatUtils.utilsQueue.postRunnable(new Runnable() { // from class: com.exteragram.messenger.components.MessageDetailsPopupWrapper$$ExternalSyntheticLambda11
                                @Override // java.lang.Runnable
                                public final void run() {
                                    this.f$0.lambda$new$10(actionBarMenuSubItem2, item);
                                }
                            });
                        } else if (i11 == 50) {
                            ChatUtils.utilsQueue.postRunnable(new Runnable() { // from class: com.exteragram.messenger.components.MessageDetailsPopupWrapper$$ExternalSyntheticLambda12
                                @Override // java.lang.Runnable
                                public final void run() {
                                    this.f$0.lambda$new$12(messageObject2, actionBarMenuSubItem2, item);
                                }
                            });
                        } else if (i11 == 51) {
                            ChatUtils.utilsQueue.postRunnable(new Runnable() { // from class: com.exteragram.messenger.components.MessageDetailsPopupWrapper$$ExternalSyntheticLambda13
                                @Override // java.lang.Runnable
                                public final void run() {
                                    this.f$0.lambda$new$14(messageObject2, actionBarMenuSubItem2, item);
                                }
                            });
                        } else if (i11 == 52) {
                            ChatUtils.utilsQueue.postRunnable(new Runnable() { // from class: com.exteragram.messenger.components.MessageDetailsPopupWrapper$$ExternalSyntheticLambda14
                                @Override // java.lang.Runnable
                                public final void run() {
                                    this.f$0.lambda$new$16(messageObject2, actionBarMenuSubItem2, item);
                                }
                            });
                        }
                    }
                    i2 = item.id;
                    if (i2 == 2) {
                        ChatUtils.utilsQueue.postRunnable(new Runnable() { // from class: com.exteragram.messenger.components.MessageDetailsPopupWrapper$$ExternalSyntheticLambda1
                            @Override // java.lang.Runnable
                            public final void run() {
                                this.f$0.lambda$new$18(actionBarMenuSubItem2, item);
                            }
                        });
                    } else if (i2 == 3) {
                        ChatUtils.utilsQueue.postRunnable(new Runnable() { // from class: com.exteragram.messenger.components.MessageDetailsPopupWrapper$$ExternalSyntheticLambda2
                            @Override // java.lang.Runnable
                            public final void run() {
                                this.f$0.lambda$new$20(messageObject2, actionBarMenuSubItem2, item);
                            }
                        });
                    } else {
                        if (i2 == 4) {
                            ChatUtils.utilsQueue.postRunnable(new Runnable() { // from class: com.exteragram.messenger.components.MessageDetailsPopupWrapper$$ExternalSyntheticLambda3
                                @Override // java.lang.Runnable
                                public final void run() {
                                    this.f$0.lambda$new$22(z2, messageObject2, actionBarMenuSubItem2, item);
                                }
                            });
                        }
                        actionBarMenuSubItem2.setTag(item);
                        final boolean z4 = z3;
                        obj = this;
                        final Item item3 = item;
                        actionBarMenuSubItem2.setOnClickListener(new View.OnClickListener() { // from class: com.exteragram.messenger.components.MessageDetailsPopupWrapper$$ExternalSyntheticLambda4
                            @Override // android.view.View.OnClickListener
                            public final void onClick(View view) {
                                this.f$0.lambda$new$23(item3, parentActivity, z4, messageObject, baseFragment, view);
                            }
                        });
                        actionBarMenuSubItem2.setOnLongClickListener(new View.OnLongClickListener() { // from class: com.exteragram.messenger.components.MessageDetailsPopupWrapper$$ExternalSyntheticLambda5
                            @Override // android.view.View.OnLongClickListener
                            public final boolean onLongClick(View view) {
                                return this.f$0.lambda$new$24(item3, view);
                            }
                        });
                        popupSwipeBackLayout2 = popupSwipeBackLayout;
                        z3 = z4;
                        i8 = i10;
                        scrollView2 = scrollView;
                        linearLayout2 = linearLayout3;
                        messageObject2 = messageObject;
                    }
                    actionBarMenuSubItem2.setTag(item);
                    final boolean z5 = z3;
                    obj = this;
                    final Item item4 = item;
                    actionBarMenuSubItem2.setOnClickListener(new View.OnClickListener() { // from class: com.exteragram.messenger.components.MessageDetailsPopupWrapper$$ExternalSyntheticLambda4
                        @Override // android.view.View.OnClickListener
                        public final void onClick(View view) {
                            this.f$0.lambda$new$23(item4, parentActivity, z5, messageObject, baseFragment, view);
                        }
                    });
                    actionBarMenuSubItem2.setOnLongClickListener(new View.OnLongClickListener() { // from class: com.exteragram.messenger.components.MessageDetailsPopupWrapper$$ExternalSyntheticLambda5
                        @Override // android.view.View.OnLongClickListener
                        public final boolean onLongClick(View view) {
                            return this.f$0.lambda$new$24(item4, view);
                        }
                    });
                    popupSwipeBackLayout2 = popupSwipeBackLayout;
                    z3 = z5;
                    i8 = i10;
                    scrollView2 = scrollView;
                    linearLayout2 = linearLayout3;
                    messageObject2 = messageObject;
                }
                item = item2;
                z2 = z2;
                scrollView = scrollView3;
                i2 = item.id;
                if (i2 == 2) {
                    ChatUtils.utilsQueue.postRunnable(new Runnable() { // from class: com.exteragram.messenger.components.MessageDetailsPopupWrapper$$ExternalSyntheticLambda1
                        @Override // java.lang.Runnable
                        public final void run() {
                            this.f$0.lambda$new$18(actionBarMenuSubItem2, item);
                        }
                    });
                } else if (i2 == 3) {
                    ChatUtils.utilsQueue.postRunnable(new Runnable() { // from class: com.exteragram.messenger.components.MessageDetailsPopupWrapper$$ExternalSyntheticLambda2
                        @Override // java.lang.Runnable
                        public final void run() {
                            this.f$0.lambda$new$20(messageObject2, actionBarMenuSubItem2, item);
                        }
                    });
                } else {
                    if (i2 == 4) {
                        ChatUtils.utilsQueue.postRunnable(new Runnable() { // from class: com.exteragram.messenger.components.MessageDetailsPopupWrapper$$ExternalSyntheticLambda3
                            @Override // java.lang.Runnable
                            public final void run() {
                                this.f$0.lambda$new$22(z2, messageObject2, actionBarMenuSubItem2, item);
                            }
                        });
                    }
                    actionBarMenuSubItem2.setTag(item);
                    final boolean z6 = z3;
                    obj = this;
                    final Item item5 = item;
                    actionBarMenuSubItem2.setOnClickListener(new View.OnClickListener() { // from class: com.exteragram.messenger.components.MessageDetailsPopupWrapper$$ExternalSyntheticLambda4
                        @Override // android.view.View.OnClickListener
                        public final void onClick(View view) {
                            this.f$0.lambda$new$23(item5, parentActivity, z6, messageObject, baseFragment, view);
                        }
                    });
                    actionBarMenuSubItem2.setOnLongClickListener(new View.OnLongClickListener() { // from class: com.exteragram.messenger.components.MessageDetailsPopupWrapper$$ExternalSyntheticLambda5
                        @Override // android.view.View.OnLongClickListener
                        public final boolean onLongClick(View view) {
                            return this.f$0.lambda$new$24(item5, view);
                        }
                    });
                    popupSwipeBackLayout2 = popupSwipeBackLayout;
                    z3 = z6;
                    i8 = i10;
                    scrollView2 = scrollView;
                    linearLayout2 = linearLayout3;
                    messageObject2 = messageObject;
                }
                actionBarMenuSubItem2.setTag(item);
                final boolean z7 = z3;
                obj = this;
                final Item item6 = item;
                actionBarMenuSubItem2.setOnClickListener(new View.OnClickListener() { // from class: com.exteragram.messenger.components.MessageDetailsPopupWrapper$$ExternalSyntheticLambda4
                    @Override // android.view.View.OnClickListener
                    public final void onClick(View view) {
                        this.f$0.lambda$new$23(item6, parentActivity, z7, messageObject, baseFragment, view);
                    }
                });
                actionBarMenuSubItem2.setOnLongClickListener(new View.OnLongClickListener() { // from class: com.exteragram.messenger.components.MessageDetailsPopupWrapper$$ExternalSyntheticLambda5
                    @Override // android.view.View.OnLongClickListener
                    public final boolean onLongClick(View view) {
                        return this.f$0.lambda$new$24(item6, view);
                    }
                });
                popupSwipeBackLayout2 = popupSwipeBackLayout;
                z3 = z7;
                i8 = i10;
                scrollView2 = scrollView;
                linearLayout2 = linearLayout3;
                messageObject2 = messageObject;
            }
        }
        ScrollView scrollView4 = scrollView2;
        if (i8 > 380 && Math.abs(i8 - 380) > 112) {
            obj.swipeBack.addView(scrollView4, LayoutHelper.createLinear(-1, 380));
        } else {
            obj.swipeBack.addView(scrollView4, LayoutHelper.createLinear(-1, -2));
        }
    }

    public static /* synthetic */ void $r8$lambda$dE2BmWFMp3ZO7Tcpe0bsPv1CgxY(Item item, ActionBarMenuSubItem actionBarMenuSubItem, TLRPC.User user) {
        if (user != null) {
            if (!TextUtils.isEmpty(UserObject.getPublicUsername(user))) {
                item.subtitle = "@" + UserObject.getPublicUsername(user);
            } else {
                item.subtitle = ContactsController.formatName(user);
            }
            actionBarMenuSubItem.setSubtext(item.subtitle);
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$new$4(final ActionBarMenuSubItem actionBarMenuSubItem, final Item item) {
        this.geo = getLatLongFromPhoto(new File(this.filePath));
        AndroidUtilities.runOnUIThread(new Runnable() { // from class: com.exteragram.messenger.components.MessageDetailsPopupWrapper$$ExternalSyntheticLambda22
            @Override // java.lang.Runnable
            public final void run() {
                this.f$0.lambda$new$3(actionBarMenuSubItem, item);
            }
        });
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$new$3(ActionBarMenuSubItem actionBarMenuSubItem, Item item) {
        if (this.geo != null) {
            actionBarMenuSubItem.setSubtext(this.geo[0] + ", " + this.geo[1]);
            item.subtitle = this.geo[0] + ", " + this.geo[1];
            return;
        }
        actionBarMenuSubItem.setVisibility(8);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$new$6(MessageObject messageObject, final ActionBarMenuSubItem actionBarMenuSubItem, final Item item) {
        final int bitrate = getBitrate(messageObject, this.filePath);
        AndroidUtilities.runOnUIThread(new Runnable() { // from class: com.exteragram.messenger.components.MessageDetailsPopupWrapper$$ExternalSyntheticLambda23
            @Override // java.lang.Runnable
            public final void run() {
                MessageDetailsPopupWrapper.$r8$lambda$f_aN3ZZNn5AFFp878etwVxDefTs(bitrate, actionBarMenuSubItem, item);
            }
        });
    }

    public static /* synthetic */ void $r8$lambda$f_aN3ZZNn5AFFp878etwVxDefTs(int i, ActionBarMenuSubItem actionBarMenuSubItem, Item item) {
        if (i > 0) {
            actionBarMenuSubItem.setSubtext(i + " Kbps");
            item.subtitle = i + " Kbps";
            return;
        }
        actionBarMenuSubItem.setVisibility(8);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$new$8(boolean z, MessageObject messageObject, final ActionBarMenuSubItem actionBarMenuSubItem, final Item item) {
        final Dimension videoResolution = z ? getVideoResolution(messageObject, this.filePath) : getPhotoResolution(messageObject, this.filePath);
        AndroidUtilities.runOnUIThread(new Runnable() { // from class: com.exteragram.messenger.components.MessageDetailsPopupWrapper$$ExternalSyntheticLambda24
            @Override // java.lang.Runnable
            public final void run() {
                MessageDetailsPopupWrapper.$r8$lambda$nKLUxPKKYKjJIhJVpxjuwM7e7_4(videoResolution, actionBarMenuSubItem, item);
            }
        });
    }

    public static /* synthetic */ void $r8$lambda$nKLUxPKKYKjJIhJVpxjuwM7e7_4(Dimension dimension, ActionBarMenuSubItem actionBarMenuSubItem, Item item) {
        if (dimension != null) {
            actionBarMenuSubItem.setSubtext(dimension.toString());
            item.subtitle = dimension.toString();
        } else {
            actionBarMenuSubItem.setVisibility(8);
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$new$10(final ActionBarMenuSubItem actionBarMenuSubItem, final Item item) {
        final String photoPlatform = MediaUtils.getPhotoPlatform(this.filePath);
        AndroidUtilities.runOnUIThread(new Runnable() { // from class: com.exteragram.messenger.components.MessageDetailsPopupWrapper$$ExternalSyntheticLambda19
            @Override // java.lang.Runnable
            public final void run() {
                MessageDetailsPopupWrapper.m950$r8$lambda$8KY68BRgfBrUh0xLJdGa54yNS4(photoPlatform, actionBarMenuSubItem, item);
            }
        });
    }

    /* JADX INFO: renamed from: $r8$lambda$8KY68BRgfBrUh0xLJ-dGa54yNS4, reason: not valid java name */
    public static /* synthetic */ void m950$r8$lambda$8KY68BRgfBrUh0xLJdGa54yNS4(String str, ActionBarMenuSubItem actionBarMenuSubItem, Item item) {
        if (!TextUtils.isEmpty(str)) {
            actionBarMenuSubItem.setSubtext(str);
            item.subtitle = str;
        } else {
            actionBarMenuSubItem.setVisibility(8);
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$new$12(MessageObject messageObject, final ActionBarMenuSubItem actionBarMenuSubItem, final Item item) {
        AyuMessagesController ayuMessagesController = AyuMessagesController.getInstance(UserConfig.selectedAccount);
        TLRPC.Message message = messageObject.messageOwner;
        final DeletedMessageFull message2 = ayuMessagesController.getMessage(message.dialog_id, message.id);
        AndroidUtilities.runOnUIThread(new Runnable() { // from class: com.exteragram.messenger.components.MessageDetailsPopupWrapper$$ExternalSyntheticLambda15
            @Override // java.lang.Runnable
            public final void run() {
                this.f$0.lambda$new$11(message2, actionBarMenuSubItem, item);
            }
        });
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$new$11(DeletedMessageFull deletedMessageFull, ActionBarMenuSubItem actionBarMenuSubItem, Item item) {
        DeletedMessage deletedMessage;
        if (deletedMessageFull != null && (deletedMessage = deletedMessageFull.message) != null) {
            String time = formatTime(deletedMessage.entityCreateDate, true);
            actionBarMenuSubItem.setSubtext(time);
            item.subtitle = time;
            return;
        }
        actionBarMenuSubItem.setVisibility(8);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$new$14(MessageObject messageObject, final ActionBarMenuSubItem actionBarMenuSubItem, final Item item) {
        AyuSpyController ayuSpyController = AyuSpyController.getInstance(UserConfig.selectedAccount);
        TLRPC.Message message = messageObject.messageOwner;
        final SpyMessageRead messageRead = ayuSpyController.getMessageRead(message.dialog_id, message.id);
        AndroidUtilities.runOnUIThread(new Runnable() { // from class: com.exteragram.messenger.components.MessageDetailsPopupWrapper$$ExternalSyntheticLambda20
            @Override // java.lang.Runnable
            public final void run() {
                this.f$0.lambda$new$13(messageRead, actionBarMenuSubItem, item);
            }
        });
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$new$13(SpyMessageRead spyMessageRead, ActionBarMenuSubItem actionBarMenuSubItem, Item item) {
        if (spyMessageRead != null) {
            String time = formatTime(spyMessageRead.entityCreateDate, true);
            actionBarMenuSubItem.setSubtext(time);
            item.subtitle = time;
            return;
        }
        actionBarMenuSubItem.setVisibility(8);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$new$16(MessageObject messageObject, final ActionBarMenuSubItem actionBarMenuSubItem, final Item item) {
        AyuSpyController ayuSpyController = AyuSpyController.getInstance(UserConfig.selectedAccount);
        TLRPC.Message message = messageObject.messageOwner;
        final SpyMessageContentsRead messageContentsRead = ayuSpyController.getMessageContentsRead(message.dialog_id, message.id);
        AndroidUtilities.runOnUIThread(new Runnable() { // from class: com.exteragram.messenger.components.MessageDetailsPopupWrapper$$ExternalSyntheticLambda16
            @Override // java.lang.Runnable
            public final void run() {
                this.f$0.lambda$new$15(messageContentsRead, actionBarMenuSubItem, item);
            }
        });
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$new$15(SpyMessageContentsRead spyMessageContentsRead, ActionBarMenuSubItem actionBarMenuSubItem, Item item) {
        if (spyMessageContentsRead != null) {
            String time = formatTime(spyMessageContentsRead.entityCreateDate, true);
            actionBarMenuSubItem.setSubtext(time);
            item.subtitle = time;
            return;
        }
        actionBarMenuSubItem.setVisibility(8);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$new$18(final ActionBarMenuSubItem actionBarMenuSubItem, final Item item) {
        this.geo = getLatLongFromPhoto(new File(this.filePath));
        AndroidUtilities.runOnUIThread(new Runnable() { // from class: com.exteragram.messenger.components.MessageDetailsPopupWrapper$$ExternalSyntheticLambda21
            @Override // java.lang.Runnable
            public final void run() {
                this.f$0.lambda$new$17(actionBarMenuSubItem, item);
            }
        });
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$new$17(ActionBarMenuSubItem actionBarMenuSubItem, Item item) {
        if (this.geo != null) {
            actionBarMenuSubItem.setSubtext(this.geo[0] + ", " + this.geo[1]);
            item.subtitle = this.geo[0] + ", " + this.geo[1];
            return;
        }
        actionBarMenuSubItem.setVisibility(8);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$new$20(MessageObject messageObject, final ActionBarMenuSubItem actionBarMenuSubItem, final Item item) {
        final int bitrate = getBitrate(messageObject, this.filePath);
        AndroidUtilities.runOnUIThread(new Runnable() { // from class: com.exteragram.messenger.components.MessageDetailsPopupWrapper$$ExternalSyntheticLambda17
            @Override // java.lang.Runnable
            public final void run() {
                MessageDetailsPopupWrapper.$r8$lambda$_qUnzygCAZab47dgMaRfG8BMGMo(bitrate, actionBarMenuSubItem, item);
            }
        });
    }

    public static /* synthetic */ void $r8$lambda$_qUnzygCAZab47dgMaRfG8BMGMo(int i, ActionBarMenuSubItem actionBarMenuSubItem, Item item) {
        if (i > 0) {
            actionBarMenuSubItem.setSubtext(i + " Kbps");
            item.subtitle = i + " Kbps";
            return;
        }
        actionBarMenuSubItem.setVisibility(8);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$new$22(boolean z, MessageObject messageObject, final ActionBarMenuSubItem actionBarMenuSubItem, final Item item) {
        final Dimension videoResolution = z ? getVideoResolution(messageObject, this.filePath) : getPhotoResolution(messageObject, this.filePath);
        AndroidUtilities.runOnUIThread(new Runnable() { // from class: com.exteragram.messenger.components.MessageDetailsPopupWrapper$$ExternalSyntheticLambda18
            @Override // java.lang.Runnable
            public final void run() {
                MessageDetailsPopupWrapper.$r8$lambda$bmq9ZccwWwKetgt6ID4U964YUcI(videoResolution, actionBarMenuSubItem, item);
            }
        });
    }

    public static /* synthetic */ void $r8$lambda$bmq9ZccwWwKetgt6ID4U964YUcI(Dimension dimension, ActionBarMenuSubItem actionBarMenuSubItem, Item item) {
        if (dimension != null) {
            actionBarMenuSubItem.setSubtext(dimension.toString());
            item.subtitle = dimension.toString();
        } else {
            actionBarMenuSubItem.setVisibility(8);
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$new$23(Item item, Activity activity, boolean z, MessageObject messageObject, BaseFragment baseFragment, View view) {
        closeMenu();
        if (item.id == 1 && !TextUtils.isEmpty(this.filePath)) {
            try {
                Uri uriForFile = FileProvider.getUriForFile(activity, ApplicationLoader.getApplicationId() + ".provider", new File(this.filePath));
                if (z) {
                    Intent intent = new Intent("android.intent.action.VIEW");
                    intent.setFlags(1);
                    intent.setDataAndType(uriForFile, messageObject.getMimeType());
                    if (!activity.getPackageManager().queryIntentActivities(intent, 0).isEmpty()) {
                        activity.startActivity(intent);
                        return;
                    }
                }
                Intent intent2 = new Intent("android.intent.action.SEND");
                intent2.setFlags(1);
                intent2.putExtra("android.intent.extra.STREAM", uriForFile);
                intent2.setDataAndType(uriForFile, messageObject.getMimeType());
                activity.startActivityForResult(Intent.createChooser(intent2, LocaleController.getString(R.string.ShareFile)), 500);
                return;
            } catch (IllegalArgumentException e) {
                FileLog.e(e);
                return;
            }
        }
        int i = item.id;
        if (i == 0) {
            if (item.subtitle.startsWith("@")) {
                Bundle bundle = new Bundle();
                bundle.putLong("user_id", this.ownerId);
                baseFragment.presentFragment(new ProfileActivity(bundle));
                return;
            }
            copy(String.valueOf(this.ownerId));
            return;
        }
        if (i == 2) {
            String str = ExteraConfig.canUseYandexMaps() ? "http://maps.yandex.ru/?text=%s,%s" : "https://maps.google.com/?q=%s,%s";
            Activity parentActivity = baseFragment.getParentActivity();
            String[] strArr = this.geo;
            Browser.openUrl(parentActivity, String.format(str, strArr[0], strArr[1]));
            return;
        }
        String str2 = item.subtitle;
        if (str2 == null) {
            str2 = item.title;
        }
        copy(str2);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ boolean lambda$new$24(Item item, View view) {
        String strValueOf;
        if (item.id == 1 && !TextUtils.isEmpty(this.filePath)) {
            strValueOf = this.filePath;
        } else if (item.id == 0) {
            strValueOf = String.valueOf(this.ownerId);
        } else {
            String str = item.subtitle;
            strValueOf = str != null ? str : item.title;
        }
        copy(strValueOf);
        return true;
    }

    public static int getBitrate(MessageObject messageObject, String str) {
        int bitrateFromPath;
        if (TextUtils.isEmpty(str)) {
            bitrateFromPath = -1;
        } else {
            try {
                bitrateFromPath = getBitrateFromPath(str);
            } catch (Exception e) {
                FileLog.e(e);
                bitrateFromPath = -1;
            }
        }
        if (bitrateFromPath != -1) {
            return bitrateFromPath;
        }
        try {
            return getBitrateFromAttributes(messageObject);
        } catch (Exception e2) {
            FileLog.e(e2);
            return bitrateFromPath;
        }
    }

    public static int getBitrateFromPath(String str) {
        int i;
        MediaMetadataRetriever mediaMetadataRetriever = new MediaMetadataRetriever();
        try {
            mediaMetadataRetriever.setDataSource(str);
            String strExtractMetadata = mediaMetadataRetriever.extractMetadata(20);
            Objects.requireNonNull(strExtractMetadata);
            i = Integer.parseInt(strExtractMetadata) / MediaDataController.MAX_STYLE_RUNS_COUNT;
        } catch (Exception e) {
            FileLog.e(e);
            i = -1;
        }
        try {
            mediaMetadataRetriever.release();
        } catch (Throwable th) {
            FileLog.e(th);
        }
        return i;
    }

    public static int getBitrateFromAttributes(MessageObject messageObject) {
        long messageSize = MessageObject.getMessageSize(messageObject.messageOwner);
        if (messageSize > 0 && MessageObject.getMedia(messageObject.messageOwner) != null && MessageObject.getMedia(messageObject.messageOwner).document != null) {
            ArrayList<TLRPC.DocumentAttribute> arrayList = MessageObject.getMedia(messageObject.messageOwner).document.attributes;
            int size = arrayList.size();
            int i = 0;
            while (i < size) {
                TLRPC.DocumentAttribute documentAttribute = arrayList.get(i);
                i++;
                TLRPC.DocumentAttribute documentAttribute2 = documentAttribute;
                if (documentAttribute2 instanceof TLRPC.TL_documentAttributeAudio) {
                    double d = documentAttribute2.duration;
                    if (d > 0.0d) {
                        return (int) (((messageSize / d) * 8.0d) / 1000.0d);
                    }
                }
            }
        }
        return -1;
    }

    public static Dimension getPhotoResolution(MessageObject messageObject, String str) {
        Dimension photoResolutionFromPath;
        if (TextUtils.isEmpty(str)) {
            photoResolutionFromPath = null;
        } else {
            try {
                photoResolutionFromPath = getPhotoResolutionFromPath(str);
            } catch (Exception e) {
                FileLog.e(e);
                photoResolutionFromPath = null;
            }
        }
        if (photoResolutionFromPath != null) {
            return photoResolutionFromPath;
        }
        try {
            return getPhotoResolutionFromAttributes(messageObject);
        } catch (Exception e2) {
            FileLog.e(e2);
            return photoResolutionFromPath;
        }
    }

    public static Dimension getPhotoResolutionFromPath(String str) {
        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = true;
        BitmapFactory.decodeFile(str, options);
        return new Dimension(options.outWidth, options.outHeight);
    }

    public static Dimension getPhotoResolutionFromAttributes(MessageObject messageObject) {
        int i;
        int i2;
        TLRPC.VideoSize closestVideoSizeWithSize;
        int i3;
        int i4;
        int i5;
        int i6;
        int i7 = 0;
        Dimension dimension = null;
        if (MessageObject.getMedia(messageObject.messageOwner) != null && MessageObject.getMedia(messageObject.messageOwner).photo != null) {
            TLRPC.PhotoSize closestPhotoSizeWithSize = FileLoader.getClosestPhotoSizeWithSize(MessageObject.getMedia(messageObject.messageOwner).photo.sizes, AndroidUtilities.getPhotoSize(), false, null, true);
            if (closestPhotoSizeWithSize != null && (i5 = closestPhotoSizeWithSize.w) > 0 && (i6 = closestPhotoSizeWithSize.h) > 0) {
                dimension = new Dimension(i5, i6);
            }
            return (dimension != null || (closestVideoSizeWithSize = FileLoader.getClosestVideoSizeWithSize(MessageObject.getMedia(messageObject.messageOwner).photo.video_sizes, AndroidUtilities.getPhotoSize(), false, true)) == null || (i3 = closestVideoSizeWithSize.w) <= 0 || (i4 = closestVideoSizeWithSize.h) <= 0) ? dimension : new Dimension(i3, i4);
        }
        if (MessageObject.getMedia(messageObject.messageOwner) != null && MessageObject.getMedia(messageObject.messageOwner).document != null) {
            ArrayList<TLRPC.DocumentAttribute> arrayList = MessageObject.getMedia(messageObject.messageOwner).document.attributes;
            int size = arrayList.size();
            while (i7 < size) {
                TLRPC.DocumentAttribute documentAttribute = arrayList.get(i7);
                i7++;
                TLRPC.DocumentAttribute documentAttribute2 = documentAttribute;
                if ((documentAttribute2 instanceof TLRPC.TL_documentAttributeImageSize) && (i = documentAttribute2.w) > 0 && (i2 = documentAttribute2.h) > 0) {
                    return new Dimension(i, i2);
                }
            }
        }
        return null;
    }

    public static Dimension getVideoResolution(MessageObject messageObject, String str) {
        Dimension videoResolutionFromPath;
        if (TextUtils.isEmpty(str)) {
            videoResolutionFromPath = null;
        } else {
            try {
                videoResolutionFromPath = getVideoResolutionFromPath(str);
            } catch (Exception e) {
                FileLog.e(e);
                videoResolutionFromPath = null;
            }
        }
        if (videoResolutionFromPath != null) {
            return videoResolutionFromPath;
        }
        try {
            return getVideoResolutionFromAttributes(messageObject);
        } catch (Exception e2) {
            FileLog.e(e2);
            return videoResolutionFromPath;
        }
    }

    public static Dimension getVideoResolutionFromPath(String str) {
        int i;
        MediaMetadataRetriever mediaMetadataRetriever = new MediaMetadataRetriever();
        int i2 = 0;
        try {
            mediaMetadataRetriever.setDataSource(str);
            String strExtractMetadata = mediaMetadataRetriever.extractMetadata(18);
            Objects.requireNonNull(strExtractMetadata);
            i = Integer.parseInt(strExtractMetadata);
            try {
                String strExtractMetadata2 = mediaMetadataRetriever.extractMetadata(19);
                Objects.requireNonNull(strExtractMetadata2);
                i2 = Integer.parseInt(strExtractMetadata2);
            } catch (Exception e) {
                e = e;
                FileLog.e(e);
            }
        } catch (Exception e2) {
            e = e2;
            i = 0;
        }
        try {
            mediaMetadataRetriever.release();
        } catch (Throwable th) {
            FileLog.e(th);
        }
        return new Dimension(i, i2);
    }

    public static Dimension getVideoResolutionFromAttributes(MessageObject messageObject) {
        int i;
        int i2;
        if (MessageObject.getMedia(messageObject.messageOwner) == null || MessageObject.getMedia(messageObject.messageOwner).document == null) {
            return null;
        }
        ArrayList<TLRPC.DocumentAttribute> arrayList = MessageObject.getMedia(messageObject.messageOwner).document.attributes;
        int size = arrayList.size();
        int i3 = 0;
        while (i3 < size) {
            TLRPC.DocumentAttribute documentAttribute = arrayList.get(i3);
            i3++;
            TLRPC.DocumentAttribute documentAttribute2 = documentAttribute;
            if ((documentAttribute2 instanceof TLRPC.TL_documentAttributeVideo) && (i = documentAttribute2.w) > 0 && (i2 = documentAttribute2.h) > 0) {
                return new Dimension(i, i2);
            }
        }
        return null;
    }

    public static String[] getLatLongFromPhoto(File file) {
        try {
            ExifInterface exifInterface = new ExifInterface(file.getAbsolutePath());
            String attribute = exifInterface.getAttribute("GPSLatitude");
            String attribute2 = exifInterface.getAttribute("GPSLongitude");
            String attribute3 = exifInterface.getAttribute("GPSLatitudeRef");
            String attribute4 = exifInterface.getAttribute("GPSLongitudeRef");
            if (attribute == null || attribute2 == null || attribute3 == null || attribute4 == null) {
                return null;
            }
            double dConvertToDegrees = convertToDegrees(attribute);
            if ("S".equalsIgnoreCase(attribute3)) {
                dConvertToDegrees = -dConvertToDegrees;
            }
            double dConvertToDegrees2 = convertToDegrees(attribute2);
            if ("W".equalsIgnoreCase(attribute4)) {
                dConvertToDegrees2 = -dConvertToDegrees2;
            }
            DecimalFormat decimalFormat = new DecimalFormat("#.######");
            decimalFormat.setDecimalFormatSymbols(DecimalFormatSymbols.getInstance(Locale.ENGLISH));
            return new String[]{decimalFormat.format(dConvertToDegrees), decimalFormat.format(dConvertToDegrees2)};
        } catch (Exception e) {
            FileLog.e(e);
            return null;
        }
    }

    private static double convertToDegrees(String str) {
        String[] strArrSplit = str.split(",");
        return convertToDouble(strArrSplit[0]) + (convertToDouble(strArrSplit[1]) / 60.0d) + (convertToDouble(strArrSplit[2]) / 3600.0d);
    }

    private static double convertToDouble(String str) {
        String[] strArrSplit = str.split("/");
        if (strArrSplit.length == 1) {
            return Double.parseDouble(strArrSplit[0]);
        }
        if (strArrSplit.length == 2) {
            double d = Double.parseDouble(strArrSplit[0]);
            double d2 = Double.parseDouble(strArrSplit[1]);
            if (d2 != 0.0d) {
                return d / d2;
            }
            FileLog.e("Division by zero in GPS data");
            return 0.0d;
        }
        FileLog.e("Invalid rational number format: " + str);
        return 0.0d;
    }

    private boolean isPhotoAsDocument(MessageObject messageObject) {
        try {
            if (MessageObject.getMedia(messageObject.messageOwner) != null && MessageObject.getMedia(messageObject.messageOwner).document != null) {
                ArrayList<TLRPC.DocumentAttribute> arrayList = MessageObject.getMedia(messageObject.messageOwner).document.attributes;
                int size = arrayList.size();
                int i = 0;
                while (i < size) {
                    TLRPC.DocumentAttribute documentAttribute = arrayList.get(i);
                    i++;
                    TLRPC.DocumentAttribute documentAttribute2 = documentAttribute;
                    if ((documentAttribute2 instanceof TLRPC.TL_documentAttributeImageSize) && documentAttribute2.w > 0 && documentAttribute2.h > 0) {
                        return true;
                    }
                }
            }
        } catch (Exception e) {
            FileLog.e(e);
        }
        return false;
    }

    private String formatTime(int i, boolean z) {
        if (i == 2147483646) {
            return LocaleController.getString(R.string.SendWhenOnline);
        }
        if (z) {
            long j = ((long) i) * 1000;
            return LocaleController.formatString("formatDateAtTime", R.string.formatDateAtTime, LocaleController.getInstance().getFormatterYear().format(new Date(j)), LocaleController.getInstance().getFormatterDayWithSeconds().format(new Date(j)));
        }
        return LocaleController.formatDateAudio(i, true);
    }

    private View createGap() {
        FrameLayout frameLayout = new FrameLayout(this.fragment.getContext());
        frameLayout.setBackgroundColor(Theme.getColor(Theme.key_actionBarDefaultSubmenuSeparator, this.resourcesProvider));
        return frameLayout;
    }

    /* JADX INFO: Access modifiers changed from: private */
    static class Item {
        int id;
        int resId;
        String subtitle;
        String title;

        Item(int i, String str, String str2) {
            this(-1, i, str, str2);
        }

        Item(int i, String str, int i2) {
            this(-1, i, str, String.valueOf(i2));
        }

        Item(int i, int i2, String str, String str2) {
            this.id = i;
            this.resId = i2;
            this.title = str;
            this.subtitle = str2;
        }
    }
}
