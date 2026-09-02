package com.radolyn.ayugram.utils;

import android.graphics.drawable.Drawable;
import android.text.SpannableStringBuilder;
import android.text.TextUtils;
import android.util.Pair;
import androidx.collection.LongSparseArray;
import com.exteragram.messenger.utils.chats.ChatUtils;
import com.radolyn.ayugram.AyuConfig;
import com.radolyn.ayugram.AyuState;
import com.radolyn.ayugram.controllers.AyuGhostController;
import java.util.ArrayList;
import kotlin.Triple;
import okhttp3.internal.url._UrlKt;
import org.mvel2.asm.Opcodes;
import org.telegram.messenger.AndroidUtilities;
import org.telegram.messenger.ContactsController;
import org.telegram.messenger.DialogObject;
import org.telegram.messenger.FileLoader;
import org.telegram.messenger.LocaleController;
import org.telegram.messenger.MessageObject;
import org.telegram.messenger.MessagesController;
import org.telegram.messenger.MessagesStorage;
import org.telegram.messenger.R;
import org.telegram.messenger.UserConfig;
import org.telegram.tgnet.TLObject;
import org.telegram.tgnet.TLRPC;
import org.telegram.ui.ActionBar.Theme;
import org.telegram.ui.ChatActivity;
import org.telegram.ui.Components.ColoredImageSpan;
import org.telegram.ui.Components.TranscribeButton;

public abstract class AyuMessageUtils {
    private static SpannableStringBuilder burntIcon;
    private static SpannableStringBuilder crossIcon;
    public static int[] deletedColors = {Opcodes.V_PREVIEW, -2349530, -2414729, -4184365, -7130134, -11581723, -14326805};
    private static SpannableStringBuilder expiringIcon;
    private static SpannableStringBuilder eyeCrossedIcon;
    private static SpannableStringBuilder oneViewIcon;
    private static SpannableStringBuilder trashBinIcon;

    public static void reinitializeIcons() {
        trashBinIcon = null;
        initializeIcons();
    }

    private static void initializeIcons() {
        if (trashBinIcon != null) {
            return;
        }
        trashBinIcon = new SpannableStringBuilder("\u200b");
        ColoredImageSpan coloredImageSpan = new ColoredImageSpan(Theme.chat_trashBinDrawable);
        int i = AyuConfig.deletedIconColor;
        if (i > 0) {
            coloredImageSpan.setOverrideColor(deletedColors[i - 1]);
        }
        trashBinIcon.setSpan(coloredImageSpan, 0, 1, 33);
        crossIcon = new SpannableStringBuilder("\u200b");
        ColoredImageSpan coloredImageSpan2 = new ColoredImageSpan(Theme.chat_crossDrawable);
        int i2 = AyuConfig.deletedIconColor;
        if (i2 > 0) {
            coloredImageSpan2.setOverrideColor(deletedColors[i2 - 1]);
        }
        crossIcon.setSpan(coloredImageSpan2, 0, 1, 33);
        eyeCrossedIcon = new SpannableStringBuilder("\u200b");
        ColoredImageSpan coloredImageSpan3 = new ColoredImageSpan(Theme.chat_eyeCrossedDrawable);
        coloredImageSpan3.setTranslateX(-AndroidUtilities.dp(1.0f));
        int i3 = AyuConfig.deletedIconColor;
        if (i3 > 0) {
            coloredImageSpan3.setOverrideColor(deletedColors[i3 - 1]);
        }
        eyeCrossedIcon.setSpan(coloredImageSpan3, 0, 1, 33);
        SpannableStringBuilder spannableStringBuilder = new SpannableStringBuilder("\u200b");
        oneViewIcon = spannableStringBuilder;
        spannableStringBuilder.setSpan(new ColoredImageSpan(Theme.chat_oneViewDrawable), 0, 1, 33);
        SpannableStringBuilder spannableStringBuilder2 = new SpannableStringBuilder("\u200b");
        expiringIcon = spannableStringBuilder2;
        spannableStringBuilder2.setSpan(new ColoredImageSpan(Theme.chat_expiringDrawable), 0, 1, 33);
        SpannableStringBuilder spannableStringBuilder3 = new SpannableStringBuilder("\u200b");
        burntIcon = spannableStringBuilder3;
        spannableStringBuilder3.setSpan(new ColoredImageSpan(Theme.chat_burntDrawable), 0, 1, 33);
    }

    public static CharSequence getDeletedIcon() {
        SpannableStringBuilder spannableStringBuilder;
        initializeIcons();
        if (AyuConfig.deletedIcon == 0) {
            return new SpannableStringBuilder();
        }
        SpannableStringBuilder spannableStringBuilder2 = new SpannableStringBuilder();
        int i = AyuConfig.deletedIcon;
        if (i == 1) {
            spannableStringBuilder = trashBinIcon;
        } else if (i == 2) {
            spannableStringBuilder = crossIcon;
        } else {
            spannableStringBuilder = eyeCrossedIcon;
        }
        return spannableStringBuilder2.append((CharSequence) spannableStringBuilder);
    }

    public static int getDeletedIconWidth() {
        initializeIcons();
        int i = AyuConfig.deletedIcon;
        if (i == 0) {
            return 0;
        }
        if (i == 1) {
            return Theme.chat_trashBinDrawable.getIntrinsicWidth();
        }
        if (i == 2) {
            return Theme.chat_crossDrawable.getIntrinsicWidth();
        }
        return Theme.chat_eyeCrossedDrawable.getIntrinsicWidth();
    }

    private static CharSequence getBurntIcon() {
        initializeIcons();
        return new SpannableStringBuilder().append((CharSequence) burntIcon);
    }

    private static int getBurntIconWidth() {
        initializeIcons();
        return Theme.chat_burntDrawable.getIntrinsicWidth();
    }

    public static Drawable getDeletedIconPreviewDrawable() {
        int i = AyuConfig.deletedIcon;
        if (i == 0) {
            return null;
        }
        if (i == 1) {
            return Theme.chat_trashBinPreviewDrawable;
        }
        if (i == 2) {
            return Theme.chat_crossPreviewDrawable;
        }
        return Theme.chat_eyeCrossedPreviewDrawable;
    }

    public static Pair prependPseudoReply(String str, String str2, TLRPC.TL_photo tL_photo, long j, ChatActivity.ReplyQuote replyQuote, MessageObject messageObject, ArrayList arrayList) {
        int length;
        int length2;
        if (TextUtils.isEmpty(str) && TextUtils.isEmpty(str2) && tL_photo == null) {
            return new Pair(str, str2);
        }
        if (TextUtils.isEmpty(messageObject.messageText) || "null".contentEquals(messageObject.messageText)) {
            try {
                messageObject.updateMessageText();
            } catch (Exception unused) {
            }
            if (TextUtils.isEmpty(messageObject.messageText) || "null".contentEquals(messageObject.messageText)) {
                return new Pair(str, str2);
            }
        }
        TLObject fromPeerObject = messageObject.getFromPeerObject();
        boolean zIsUserDialog = DialogObject.isUserDialog(j);
        String name = _UrlKt.FRAGMENT_ENCODE_SET;
        if (!zIsUserDialog || Math.abs(messageObject.getDialogId()) != Math.abs(j)) {
            if (fromPeerObject instanceof TLRPC.Chat) {
                name = ((TLRPC.Chat) fromPeerObject).title;
            } else if (fromPeerObject instanceof TLRPC.User) {
                TLRPC.User user = (TLRPC.User) fromPeerObject;
                name = ContactsController.formatName(user.first_name, user.last_name);
            }
            if (!TextUtils.isEmpty(name)) {
                name = name + "\n";
            }
        }
        long senderId = replyQuote != null ? replyQuote.peerId : messageObject.getSenderId();
        String str3 = name + ((Object) shortify(replyQuote != null ? replyQuote.getText() : messageObject.messageText, 100));
        if (!TextUtils.isEmpty(str)) {
            str = str3 + "\n" + str;
            length2 = str3.length();
        } else {
            if (!TextUtils.isEmpty(str2)) {
                str2 = str3 + "\n" + str2;
                length2 = str3.length();
            } else if (tL_photo != null) {
                length = str3.length();
                str2 = str3;
            } else {
                length = 0;
            }
            shiftEntities(arrayList, length);
            TLRPC.TL_messageEntityBold tL_messageEntityBold = new TLRPC.TL_messageEntityBold();
            tL_messageEntityBold.length = name.length();
            arrayList.add(tL_messageEntityBold);
            TLRPC.TL_inputMessageEntityMentionName tL_inputMessageEntityMentionName = new TLRPC.TL_inputMessageEntityMentionName();
            tL_inputMessageEntityMentionName.user_id = ChatUtils.getInstance().getMessagesController().getInputUser(senderId);
            tL_inputMessageEntityMentionName.length = name.length();
            arrayList.add(tL_inputMessageEntityMentionName);
            TLRPC.TL_messageEntityBlockquote tL_messageEntityBlockquote = new TLRPC.TL_messageEntityBlockquote();
            tL_messageEntityBlockquote.length = str3.length();
            arrayList.add(tL_messageEntityBlockquote);
            return new Pair(str, str2);
        }
        length = length2 + 1;
        shiftEntities(arrayList, length);
        TLRPC.TL_messageEntityBold tL_messageEntityBold2 = new TLRPC.TL_messageEntityBold();
        tL_messageEntityBold2.length = name.length();
        arrayList.add(tL_messageEntityBold2);
        TLRPC.TL_inputMessageEntityMentionName tL_inputMessageEntityMentionName2 = new TLRPC.TL_inputMessageEntityMentionName();
        tL_inputMessageEntityMentionName2.user_id = ChatUtils.getInstance().getMessagesController().getInputUser(senderId);
        tL_inputMessageEntityMentionName2.length = name.length();
        arrayList.add(tL_inputMessageEntityMentionName2);
        TLRPC.TL_messageEntityBlockquote tL_messageEntityBlockquote2 = new TLRPC.TL_messageEntityBlockquote();
        tL_messageEntityBlockquote2.length = str3.length();
        arrayList.add(tL_messageEntityBlockquote2);
        return new Pair(str, str2);
    }

    public static Triple formatTTL(MessageObject messageObject, boolean z) {
        TLRPC.Message message;
        int i;
        TLRPC.MessageMedia messageMedia;
        if (messageObject == null || (message = messageObject.messageOwner) == null || (i = message.ttl) == 0 || (messageMedia = message.media) == null || (messageMedia instanceof TLRPC.TL_messageMediaEmpty) || message.ayuDeleted) {
            return null;
        }
        if (i < 0) {
            i = 0;
        }
        initializeIcons();
        SpannableStringBuilder spannableStringBuilder = new SpannableStringBuilder();
        boolean zIsMessageBurned = AyuState.isMessageBurned(messageObject.currentAccount, messageObject.getDialogId(), messageObject.getId());
        if (i == Integer.MAX_VALUE) {
            if (messageObject.isVoice() || messageObject.isRoundVideo()) {
                if (zIsMessageBurned && z) {
                    return new Triple(spannableStringBuilder.append(getBurntIcon()).append((CharSequence) " "), Integer.valueOf(getBurntIconWidth()), Boolean.FALSE);
                }
                return null;
            }
            CharSequence burntIcon2 = zIsMessageBurned ? getBurntIcon() : oneViewIcon;
            int burntIconWidth = zIsMessageBurned ? getBurntIconWidth() : Theme.chat_oneViewDrawable.getIntrinsicWidth();
            if (z) {
                spannableStringBuilder = spannableStringBuilder.append(burntIcon2).append((CharSequence) " ");
            }
            return new Triple(spannableStringBuilder.append((CharSequence) LocaleController.getString(R.string.OneViewTTL)), Integer.valueOf(burntIconWidth), Boolean.TRUE);
        }
        CharSequence burntIcon3 = zIsMessageBurned ? getBurntIcon() : expiringIcon;
        int burntIconWidth2 = zIsMessageBurned ? getBurntIconWidth() : Theme.chat_expiringDrawable.getIntrinsicWidth();
        if (z) {
            spannableStringBuilder = spannableStringBuilder.append(burntIcon3).append((CharSequence) " ");
        }
        return new Triple(spannableStringBuilder.append((CharSequence) String.valueOf(i)).append((CharSequence) "s"), Integer.valueOf(burntIconWidth2), Boolean.TRUE);
    }

    public static int getMinRealId(ArrayList arrayList) {
        for (int size = arrayList.size() - 1; size > 0; size--) {
            MessageObject messageObject = (MessageObject) arrayList.get(size);
            if (messageObject.getId() > 0 && messageObject.isSent()) {
                return messageObject.getId();
            }
        }
        return Integer.MAX_VALUE;
    }

    public static void shiftEntities(ArrayList arrayList, int i) {
        if (arrayList == null || arrayList.isEmpty() || i == 0) {
            return;
        }
        int size = arrayList.size();
        int i2 = 0;
        while (i2 < size) {
            Object obj = arrayList.get(i2);
            i2++;
            ((TLRPC.MessageEntity) obj).offset += i;
        }
    }

    public static CharSequence shortify(CharSequence charSequence, int i) {
        if (TextUtils.isEmpty(charSequence) || charSequence.length() <= i) {
            return charSequence;
        }
        return ((Object) charSequence.subSequence(0, i - 1)) + "…";
    }

    public static boolean isMediaDownloadable(MessageObject messageObject, boolean z) {
        TLRPC.Message message;
        TLRPC.MessageMedia messageMedia;
        if (messageObject == null || (message = messageObject.messageOwner) == null || (messageMedia = message.media) == null || (messageMedia.photo instanceof TLRPC.TL_photoEmpty) || (messageMedia.document instanceof TLRPC.TL_documentEmpty) || MessageObject.isMediaEmpty(message)) {
            return false;
        }
        TLRPC.MessageMedia messageMedia2 = messageObject.messageOwner.media;
        if ((messageMedia2 instanceof TLRPC.TL_messageMediaPaidMedia) && ((TLRPC.TL_messageMediaPaidMedia) messageMedia2).stars_amount != 0) {
            return true;
        }
        boolean z2 = (messageObject.isSecretMedia() && !messageObject.isVoice()) || messageObject.isGif() || messageObject.isNewGif() || messageObject.isRoundVideo() || messageObject.isVideo() || messageObject.isPhoto() || messageObject.isSticker() || messageObject.isAnimatedSticker();
        if (z || z2) {
            return z2;
        }
        return messageObject.isDocument() || messageObject.isMusic() || messageObject.isVoice();
    }

    private static CharSequence extract(MessageObject messageObject) {
        StringBuilder sb = new StringBuilder();
        if (!TextUtils.isEmpty(messageObject.messageText) && messageObject.messageText != LocaleController.getString(R.string.AttachVideo) && messageObject.messageText != LocaleController.getString(R.string.AttachPhoto) && messageObject.messageText != LocaleController.getString(R.string.Album)) {
            sb.append(messageObject.messageText);
            sb.append("\n");
        }
        if (!TextUtils.isEmpty(messageObject.caption)) {
            sb.append(messageObject.caption);
            sb.append("\n");
        }
        if (messageObject.isVoiceTranscriptionOpen() && !TranscribeButton.isTranscribing(messageObject)) {
            sb.append(messageObject.getVoiceTranscription());
            sb.append("\n");
        }
        String restrictionReason = MessagesController.getInstance(messageObject.currentAccount).getRestrictionReason(messageObject.messageOwner.restriction_reason);
        if (!TextUtils.isEmpty(restrictionReason)) {
            sb.append(restrictionReason);
            sb.append("\n");
        }
        return sb;
    }

    private static CharSequence extract(MessageObject messageObject, MessageObject.GroupedMessages groupedMessages) {
        if (groupedMessages == null) {
            return extract(messageObject);
        }
        StringBuilder sb = new StringBuilder();
        int size = groupedMessages.messages.size();
        for (int i = 0; i < size; i++) {
            sb.append(extract(groupedMessages.messages.get(i)));
        }
        return sb;
    }

    public static CharSequence extractAllText(MessageObject messageObject, MessageObject.GroupedMessages groupedMessages) {
        ArrayList arrayList;
        StringBuilder sb = new StringBuilder(extract(messageObject, groupedMessages));
        ArrayList arrayList2 = messageObject.messageOwner.entities;
        if (arrayList2 != null && !arrayList2.isEmpty()) {
            ArrayList arrayList3 = messageObject.messageOwner.entities;
            int size = arrayList3.size();
            int i = 0;
            while (i < size) {
                Object obj = arrayList3.get(i);
                i++;
                TLRPC.MessageEntity messageEntity = (TLRPC.MessageEntity) obj;
                if (messageEntity instanceof TLRPC.TL_messageEntityTextUrl) {
                    sb.append("\n");
                    sb.append(messageEntity.url);
                }
            }
            sb.append("\n");
        }
        TLRPC.ReplyMarkup replyMarkup = messageObject.messageOwner.reply_markup;
        if (replyMarkup != null && (arrayList = replyMarkup.rows) != null && !arrayList.isEmpty()) {
            sb.append("\n");
            ArrayList arrayList4 = messageObject.messageOwner.reply_markup.rows;
            int size2 = arrayList4.size();
            int i2 = 0;
            while (i2 < size2) {
                Object obj2 = arrayList4.get(i2);
                i2++;
                ArrayList arrayList5 = ((TLRPC.TL_keyboardButtonRow) obj2).buttons;
                int size3 = arrayList5.size();
                int i3 = 0;
                while (i3 < size3) {
                    Object obj3 = arrayList5.get(i3);
                    i3++;
                    TLRPC.KeyboardButton keyboardButton = (TLRPC.KeyboardButton) obj3;
                    sb.append("<button>");
                    sb.append(keyboardButton.text);
                    sb.append(" ");
                    sb.append(keyboardButton.url);
                    sb.append("</button>");
                    sb.append("\n");
                }
            }
        }
        sb.append("\n");
        sb.append("<type>");
        sb.append(messageObject.type);
        sb.append("</type>");
        return sb;
    }

    public static long getMessageSize(MessageObject messageObject) {
        long size = messageObject.getSize();
        if (size != 0) {
            return size;
        }
        if (messageObject.isPhoto()) {
            return getPhotoSize(MessageObject.getPhoto(messageObject.messageOwner));
        }
        return 0L;
    }

    public static long getPhotoSize(TLRPC.Photo photo) {
        TLRPC.PhotoSize closestPhotoSizeWithSize;
        if (photo == null || (closestPhotoSizeWithSize = FileLoader.getClosestPhotoSizeWithSize(photo.sizes, AndroidUtilities.getPhotoSize(true))) == null) {
            return 0L;
        }
        return closestPhotoSizeWithSize.size;
    }

    public static TLObject getDialogInAnyWay(long j, Integer num) {
        return getDialogInAnyWay(j, num, true);
    }

    public static TLObject getDialogInAnyWay(long j, Integer num, boolean z) {
        TLObject dialogFromAccountNumber;
        TLObject dialogFromAccountNumber2 = getDialogFromAccountNumber(num.intValue(), j);
        if (dialogFromAccountNumber2 != null) {
            return dialogFromAccountNumber2;
        }
        for (int i = 0; i < 16; i++) {
            if (i != num.intValue() && UserConfig.isValidAccount(i) && (dialogFromAccountNumber = getDialogFromAccountNumber(i, j)) != null) {
                return dialogFromAccountNumber;
            }
        }
        if (!z) {
            return null;
        }
        TLRPC.TL_chat tL_chat = new TLRPC.TL_chat();
        tL_chat.id = j;
        tL_chat.title = "Unknown (ID: " + j + ")";
        return tL_chat;
    }

    private static TLObject getDialogFromAccountNumber(int i, long j) {
        TLObject userOrChat = MessagesController.getInstance(i).getUserOrChat(j);
        if (userOrChat != null) {
            return userOrChat;
        }
        TLRPC.User userSync = MessagesStorage.getInstance(i).getUserSync(j);
        if (userSync != null) {
            return userSync;
        }
        TLRPC.Chat chatSync = MessagesStorage.getInstance(i).getChatSync(j);
        return chatSync != null ? chatSync : MessagesStorage.getInstance(i).getChatSync(Math.abs(j));
    }

    public static TLObject getMedia(TLRPC.Message message) {
        if (message == null) {
            return null;
        }
        TLRPC.Document document = MessageObject.getDocument(message);
        if (document != null) {
            return document;
        }
        TLRPC.Photo photo = MessageObject.getPhoto(message);
        if (photo != null) {
            return photo;
        }
        TLRPC.MessageMedia media = MessageObject.getMedia(message);
        if (media != null && (media instanceof TLRPC.TL_messageMediaPaidMedia)) {
            TLRPC.TL_messageMediaPaidMedia tL_messageMediaPaidMedia = (TLRPC.TL_messageMediaPaidMedia) media;
            if (!tL_messageMediaPaidMedia.extended_media.isEmpty()) {
                TLRPC.MessageExtendedMedia messageExtendedMedia = tL_messageMediaPaidMedia.extended_media.get(0);
                if (messageExtendedMedia instanceof TLRPC.TL_messageExtendedMedia) {
                    TLRPC.MessageMedia messageMedia = ((TLRPC.TL_messageExtendedMedia) messageExtendedMedia).media;
                    TLRPC.Document document2 = messageMedia.document;
                    if (document2 != null) {
                        return document2;
                    }
                    TLRPC.Photo photo2 = messageMedia.photo;
                    if (photo2 != null) {
                        return photo2;
                    }
                }
            }
        }
        return null;
    }

    public static TLRPC.Photo getPhoto(TLRPC.Message message) {
        if (message == null) {
            return null;
        }
        TLObject media = getMedia(message);
        if (media instanceof TLRPC.Photo) {
            return (TLRPC.Photo) media;
        }
        return null;
    }

    public static TLRPC.Document getDocument(TLRPC.Message message) {
        if (message == null) {
            return null;
        }
        TLObject media = getMedia(message);
        if (media instanceof TLRPC.Document) {
            return (TLRPC.Document) media;
        }
        return null;
    }

    public static boolean isAyuDeleted(MessageObject messageObject) {
        return messageObject != null && isAyuDeleted(messageObject.messageOwner);
    }

    public static boolean isAyuDeleted(TLRPC.Message message) {
        return message != null && message.ayuDeleted;
    }

    public static boolean isUnsaveable(MessageObject messageObject) {
        TLRPC.Message message;
        if (messageObject == null || (message = messageObject.messageOwner) == null) {
            return false;
        }
        if (message.ayuDeleted || message.ayuNoforwards || message.ttl != 0 || messageObject.isVoice() || messageObject.isRoundVideo()) {
            return true;
        }
        TLRPC.MessageMedia messageMedia = messageObject.messageOwner.media;
        return (messageMedia instanceof TLRPC.TL_messageMediaPaidMedia) && ((TLRPC.TL_messageMediaPaidMedia) messageMedia).stars_amount != 0;
    }

    public static boolean isUnforwardable(MessageObject messageObject) {
        TLRPC.Message message;
        if (messageObject == null || (message = messageObject.messageOwner) == null) {
            return false;
        }
        if ((message instanceof TLRPC.TL_message_secret) || (message instanceof TLRPC.TL_message_secret_layer72) || message.ayuDeleted || message.ayuNoforwards || message.ttl != 0) {
            return true;
        }
        TLRPC.MessageMedia messageMedia = message.media;
        return (messageMedia instanceof TLRPC.TL_messageMediaPaidMedia) && ((TLRPC.TL_messageMediaPaidMedia) messageMedia).stars_amount != 0;
    }

    public static boolean isUnrepliable(MessageObject messageObject) {
        TLRPC.Message message;
        if (messageObject == null || (message = messageObject.messageOwner) == null) {
            return false;
        }
        if (message.ayuDeleted || message.ayuNoforwards) {
            return true;
        }
        return isChatNoForwards(messageObject.currentAccount, messageObject.getDialogId());
    }

    public static boolean isChatNoForwards(int i, long j) {
        TLRPC.TL_chatBannedRights tL_chatBannedRights;
        TLRPC.Chat chat;
        TLRPC.TL_chatBannedRights tL_chatBannedRights2;
        TLRPC.UserFull userFull;
        MessagesController messagesController = MessagesController.getInstance(i);
        TLRPC.Chat chat2 = messagesController.getChat(Long.valueOf(j));
        if (chat2 == null) {
            chat2 = messagesController.getChat(Long.valueOf(-j));
        }
        if (chat2 == null) {
            if (DialogObject.isUserDialog(j) && (userFull = messagesController.getUserFull(j)) != null) {
                return userFull.ayuNoforwards_peer_enabled || userFull.ayuNoforwards_my_enabled;
            }
            return false;
        }
        if ((chat2 instanceof TLRPC.TL_channelForbidden) || (chat2 instanceof TLRPC.TL_chatForbidden) || ((tL_chatBannedRights = chat2.banned_rights) != null && tL_chatBannedRights.view_messages)) {
            return true;
        }
        TLRPC.InputChannel inputChannel = chat2.migrated_to;
        if (inputChannel != null && (chat = messagesController.getChat(Long.valueOf(inputChannel.channel_id))) != null) {
            if ((chat instanceof TLRPC.TL_channelForbidden) || (chat instanceof TLRPC.TL_chatForbidden) || ((tL_chatBannedRights2 = chat2.banned_rights) != null && tL_chatBannedRights2.view_messages)) {
                return true;
            }
            return chat.ayuNoforwards;
        }
        return chat2.ayuNoforwards;
    }

    public static boolean isExpiredMessage(MessageObject messageObject) {
        TLRPC.Message message;
        if (messageObject != null && (message = messageObject.messageOwner) != null) {
            TLRPC.MessageMedia messageMedia = message.media;
            if (messageMedia instanceof TLRPC.TL_messageMediaEmpty) {
                return true;
            }
            if (messageMedia instanceof TLRPC.TL_messageMediaDocument) {
                TLRPC.Document document = messageMedia.document;
                if (((document instanceof TLRPC.TL_documentEmpty) || document == null) && messageMedia.ttl_seconds != 0) {
                    return true;
                }
            }
            if ((messageMedia instanceof TLRPC.TL_messageMediaPhoto) && (messageMedia.photo instanceof TLRPC.TL_photoEmpty) && messageMedia.ttl_seconds != 0) {
                return true;
            }
        }
        return false;
    }

    public static int compareMessages(TLRPC.Message message, TLRPC.Message message2) {
        int i;
        int i2;
        if (message != null && message2 != null) {
            int i3 = message.id;
            if (i3 <= 0 || (i2 = message2.id) <= 0) {
                if (i3 >= 0 || (i = message2.id) >= 0) {
                    int i4 = message.date;
                    int i5 = message2.date;
                    if (i4 > i5) {
                        return -1;
                    }
                    if (i4 < i5) {
                        return 1;
                    }
                } else {
                    if (i3 < i) {
                        return -1;
                    }
                    if (i3 > i) {
                        return 1;
                    }
                }
            } else {
                if (i3 > i2) {
                    return -1;
                }
                if (i3 < i2) {
                    return 1;
                }
            }
        }
        return 0;
    }

    public static MessageObject extractFromUpdates(int i, long j, Integer num, LongSparseArray longSparseArray, ArrayList arrayList, ArrayList arrayList2) {
        ArrayList arrayList3;
        int i2 = 0;
        if (longSparseArray != null && !longSparseArray.isEmpty() && (arrayList3 = (ArrayList) longSparseArray.get(j)) != null) {
            int size = arrayList3.size();
            int i3 = 0;
            while (i3 < size) {
                Object obj = arrayList3.get(i3);
                i3++;
                MessageObject messageObject = (MessageObject) obj;
                if (messageObject.getId() == num.intValue() && (messageObject.getDialogId() == j || (j == 0 && DialogObject.isUserDialog(messageObject.getDialogId())))) {
                    return messageObject;
                }
            }
        }
        if (arrayList != null && !arrayList.isEmpty()) {
            int size2 = arrayList.size();
            int i4 = 0;
            while (i4 < size2) {
                Object obj2 = arrayList.get(i4);
                i4++;
                TLRPC.Message message = (TLRPC.Message) obj2;
                if (message.id == num.intValue() && (MessageObject.getDialogId(message) == j || (j == 0 && DialogObject.isUserDialog(MessageObject.getDialogId(message))))) {
                    return new MessageObject(i, message, false, false);
                }
            }
        }
        if (arrayList2 == null || arrayList2.isEmpty()) {
            return null;
        }
        int size3 = arrayList2.size();
        while (i2 < size3) {
            Object obj3 = arrayList2.get(i2);
            i2++;
            MessageObject messageObject2 = (MessageObject) obj3;
            if (messageObject2.getId() == num.intValue() && (messageObject2.getDialogId() == j || (j == 0 && DialogObject.isUserDialog(messageObject2.getDialogId())))) {
                return messageObject2;
            }
        }
        return null;
    }

    public static int getScheduleTime(TLRPC.TL_photo tL_photo, TLRPC.TL_document tL_document) {
        double dCeil;
        if (tL_document != null && tL_document.access_hash != 0 && (MessageObject.isStickerDocument(tL_document) || MessageObject.isAnimatedStickerDocument(tL_document, true))) {
            dCeil = Math.ceil(12.0f);
        } else if (tL_document != null && tL_document.access_hash != 0 && MessageObject.isGifDocument(tL_document)) {
            dCeil = Math.ceil(12.0f);
        } else {
            long photoSize = getPhotoSize(tL_photo);
            long j = tL_document == null ? 0L : tL_document.size;
            float fMax = photoSize != 0 ? 12.0f + Math.max(6, (int) Math.ceil(((photoSize / 1024.0f) / 1024.0f) * 4.5f)) : 12.0f;
            if (j != 0) {
                fMax += Math.max(6, (int) Math.ceil(((j / 1024.0f) / 1024.0f) * 4.5f));
            }
            dCeil = Math.ceil(fMax);
        }
        return (int) dCeil;
    }

    public static boolean shouldIgnoreNotification(MessageObject messageObject) {
        TLRPC.Message message;
        if (messageObject == null) {
            return false;
        }
        int i = messageObject.currentAccount;
        return AyuGhostController.getInstance(i).isUseScheduledMessages() && (message = messageObject.messageOwner) != null && message.from_scheduled && UserConfig.getInstance(i).getClientUserId() != messageObject.getDialogId();
    }
}
