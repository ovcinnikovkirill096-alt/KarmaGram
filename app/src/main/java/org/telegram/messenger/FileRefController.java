package org.telegram.messenger;

import android.os.SystemClock;
import android.util.Pair;
import com.exteragram.messenger.updater.UpdaterUtils;
import com.radolyn.ayugram.AyuConstants;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import okhttp3.internal.url._UrlKt;
import okhttp3.internal.ws.RealWebSocket;
import org.telegram.tgnet.RequestDelegate;
import org.telegram.tgnet.TLObject;
import org.telegram.tgnet.TLRPC;
import org.telegram.tgnet.Vector;
import org.telegram.tgnet.tl.TL_account;
import org.telegram.tgnet.tl.TL_bots;
import org.telegram.tgnet.tl.TL_stories;
import org.telegram.ui.ActionBar.Theme;
import org.telegram.ui.Stories.StoriesController;

public class FileRefController extends BaseController {
    private static volatile FileRefController[] Instance = new FileRefController[16];
    private ArrayList<Waiter> favStickersWaiter;
    private long lastCleanupTime;
    private HashMap<String, ArrayList<Requester>> locationRequester;
    private HashMap<TLObject, Object[]> multiMediaCache;
    private HashMap<String, ArrayList<Requester>> parentRequester;
    private ArrayList<Waiter> recentStickersWaiter;
    private HashMap<String, CachedResult> responseCache;
    private ArrayList<Waiter> savedGifsWaiters;
    private ArrayList<Waiter> wallpaperWaiters;

    public static /* synthetic */ void $r8$lambda$EY1qS7VyTjRp4fSjfQEL0VDJDao(TLObject tLObject, TLRPC.TL_error tL_error) {
    }

    public static /* synthetic */ void $r8$lambda$LN1JdHjG3sV_jPNS2hg2hjBBeKc(TLObject tLObject, TLRPC.TL_error tL_error) {
    }

    public static /* synthetic */ void $r8$lambda$b5qZPw2Lh2oskQb_eey51J3RR1M(TLObject tLObject, TLRPC.TL_error tL_error) {
    }

    public static /* synthetic */ void $r8$lambda$bP6KBJG6eRnD1L1Fw9rrtHGm3Ic(TLObject tLObject, TLRPC.TL_error tL_error) {
    }

    public static /* synthetic */ void $r8$lambda$zLpXUCCmmkTcmIXKtdR8GqrnykY(TLObject tLObject, TLRPC.TL_error tL_error) {
    }

    /* JADX INFO: Access modifiers changed from: private */
    static class Requester {
        private Object[] args;
        private boolean completed;
        private TLRPC.InputFileLocation location;
        private String locationKey;

        private Requester() {
        }
    }

    private static class CachedResult {
        private long firstQueryTime;
        private TLObject response;

        private CachedResult() {
        }
    }

    private static class Waiter {
        private String locationKey;
        private String parentKey;

        public Waiter(String str, String str2) {
            this.locationKey = str;
            this.parentKey = str2;
        }
    }

    public static FileRefController getInstance(int i) {
        FileRefController fileRefController;
        FileRefController fileRefController2 = Instance[i];
        if (fileRefController2 != null) {
            return fileRefController2;
        }
        synchronized (FileRefController.class) {
            try {
                fileRefController = Instance[i];
                if (fileRefController == null) {
                    FileRefController[] fileRefControllerArr = Instance;
                    FileRefController fileRefController3 = new FileRefController(i);
                    fileRefControllerArr[i] = fileRefController3;
                    fileRefController = fileRefController3;
                }
            } catch (Throwable th) {
                throw th;
            }
        }
        return fileRefController;
    }

    public FileRefController(int i) {
        super(i);
        this.locationRequester = new HashMap<>();
        this.parentRequester = new HashMap<>();
        this.responseCache = new HashMap<>();
        this.multiMediaCache = new HashMap<>();
        this.lastCleanupTime = SystemClock.elapsedRealtime();
        this.wallpaperWaiters = new ArrayList<>();
        this.savedGifsWaiters = new ArrayList<>();
        this.recentStickersWaiter = new ArrayList<>();
        this.favStickersWaiter = new ArrayList<>();
    }

    public static String getKeyForParentObject(Object obj) {
        TLRPC.Message message;
        TLRPC.MessageFwdHeader messageFwdHeader;
        TLRPC.Peer peer;
        if (obj instanceof StoriesController.BotPreview) {
            StoriesController.BotPreview botPreview = (StoriesController.BotPreview) obj;
            if (botPreview.list == null) {
                FileLog.d("failed request reference can't find list in botpreview");
                return null;
            }
            TLRPC.MessageMedia messageMedia = botPreview.media;
            if (messageMedia.document != null) {
                return "botstory_doc_" + botPreview.media.document.id;
            }
            if (messageMedia.photo != null) {
                return "botstory_photo_" + botPreview.media.photo.id;
            }
            return "botstory_" + botPreview.id;
        }
        if (obj instanceof TL_stories.StoryItem) {
            TL_stories.StoryItem storyItem = (TL_stories.StoryItem) obj;
            if (storyItem.dialogId == 0) {
                FileLog.d("failed request reference can't find dialogId");
                return null;
            }
            return "story_" + storyItem.dialogId + "_" + storyItem.id;
        }
        if (obj instanceof TLRPC.TL_help_premiumPromo) {
            return "premium_promo";
        }
        if (obj instanceof TLRPC.TL_availableReaction) {
            return "available_reaction_" + ((TLRPC.TL_availableReaction) obj).reaction;
        }
        if (obj instanceof TL_bots.BotInfo) {
            return "bot_info_" + ((TL_bots.BotInfo) obj).user_id;
        }
        if (obj instanceof TLRPC.TL_attachMenuBot) {
            return "attach_menu_bot_" + ((TLRPC.TL_attachMenuBot) obj).bot_id;
        }
        if (obj instanceof MessageObject) {
            MessageObject messageObject = (MessageObject) obj;
            long channelId = messageObject.getChannelId();
            if (messageObject.type == 29 && (message = messageObject.messageOwner) != null && (messageFwdHeader = message.fwd_from) != null && (peer = messageFwdHeader.from_id) != null) {
                channelId = DialogObject.getPeerDialogId(peer);
            }
            return "message" + messageObject.getRealId() + "_" + channelId + "_" + messageObject.scheduled + "_" + messageObject.getQuickReplyId();
        }
        if (obj instanceof TLRPC.Message) {
            TLRPC.Message message2 = (TLRPC.Message) obj;
            TLRPC.Peer peer2 = message2.peer_id;
            return "message" + message2.id + "_" + (peer2 != null ? peer2.channel_id : 0L) + "_" + message2.from_scheduled;
        }
        if (obj instanceof TLRPC.WebPage) {
            return "webpage" + ((TLRPC.WebPage) obj).id;
        }
        if (obj instanceof TLRPC.User) {
            return "user" + ((TLRPC.User) obj).id;
        }
        if (obj instanceof TLRPC.Chat) {
            return "chat" + ((TLRPC.Chat) obj).id;
        }
        if (obj instanceof String) {
            return "str" + ((String) obj);
        }
        if (obj instanceof TLRPC.TL_messages_stickerSet) {
            return "set" + ((TLRPC.TL_messages_stickerSet) obj).set.id;
        }
        if (obj instanceof TLRPC.StickerSetCovered) {
            return "set" + ((TLRPC.StickerSetCovered) obj).set.id;
        }
        if (obj instanceof TLRPC.InputStickerSet) {
            return "set" + ((TLRPC.InputStickerSet) obj).id;
        }
        if (obj instanceof TLRPC.TL_wallPaper) {
            return "wallpaper" + ((TLRPC.TL_wallPaper) obj).id;
        }
        if (obj instanceof TLRPC.TL_theme) {
            return "theme" + ((TLRPC.TL_theme) obj).id;
        }
        if (obj == null) {
            return null;
        }
        return _UrlKt.FRAGMENT_ENCODE_SET + obj;
    }

    public Pair<TLRPC.InputFileLocation, String> getLocationAndKey(Object obj, Object... objArr) {
        Object obj2 = objArr[0];
        if (obj2 instanceof TLRPC.TL_messages_sendMultiMedia) {
            return null;
        }
        if ((obj2 instanceof TLRPC.TL_messages_sendMedia) && (((TLRPC.TL_messages_sendMedia) obj2).media instanceof TLRPC.TL_inputMediaPaidMedia) && (obj instanceof ArrayList)) {
            return null;
        }
        if (obj2 instanceof StoriesController.BotPreview) {
            StoriesController.BotPreview botPreview = (StoriesController.BotPreview) obj2;
            TLRPC.MessageMedia messageMedia = botPreview.media;
            if (messageMedia.document != null) {
                TLRPC.TL_inputDocumentFileLocation tL_inputDocumentFileLocation = new TLRPC.TL_inputDocumentFileLocation();
                tL_inputDocumentFileLocation.id = botPreview.media.document.id;
                return new Pair<>(tL_inputDocumentFileLocation, "botstory_doc_" + botPreview.media.document.id);
            }
            if (messageMedia.photo != null) {
                TLRPC.TL_inputPhotoFileLocation tL_inputPhotoFileLocation = new TLRPC.TL_inputPhotoFileLocation();
                tL_inputPhotoFileLocation.id = botPreview.media.photo.id;
                return new Pair<>(tL_inputPhotoFileLocation, "botstory_photo_" + botPreview.media.photo.id);
            }
            return new Pair<>(new TLRPC.TL_inputDocumentFileLocation(), "botstory_" + botPreview.id);
        }
        if (obj2 instanceof TL_stories.TL_storyItem) {
            TL_stories.TL_storyItem tL_storyItem = (TL_stories.TL_storyItem) obj2;
            TLRPC.TL_inputDocumentFileLocation tL_inputDocumentFileLocation2 = new TLRPC.TL_inputDocumentFileLocation();
            tL_inputDocumentFileLocation2.id = tL_storyItem.media.document.id;
            return new Pair<>(tL_inputDocumentFileLocation2, "story_" + tL_storyItem.id);
        }
        if (obj2 instanceof TLRPC.TL_inputSingleMedia) {
            TLRPC.InputMedia inputMedia = ((TLRPC.TL_inputSingleMedia) obj2).media;
            if (inputMedia instanceof TLRPC.TL_inputMediaDocument) {
                TLRPC.TL_inputMediaDocument tL_inputMediaDocument = (TLRPC.TL_inputMediaDocument) inputMedia;
                TLRPC.TL_inputDocumentFileLocation tL_inputDocumentFileLocation3 = new TLRPC.TL_inputDocumentFileLocation();
                tL_inputDocumentFileLocation3.id = tL_inputMediaDocument.id.id;
                return new Pair<>(tL_inputDocumentFileLocation3, "file_" + tL_inputMediaDocument.id.id);
            }
            if (inputMedia instanceof TLRPC.TL_inputMediaPhoto) {
                TLRPC.TL_inputMediaPhoto tL_inputMediaPhoto = (TLRPC.TL_inputMediaPhoto) inputMedia;
                TLRPC.TL_inputPhotoFileLocation tL_inputPhotoFileLocation2 = new TLRPC.TL_inputPhotoFileLocation();
                tL_inputPhotoFileLocation2.id = tL_inputMediaPhoto.id.id;
                return new Pair<>(tL_inputPhotoFileLocation2, "photo_" + tL_inputMediaPhoto.id.id);
            }
        } else {
            if (obj2 instanceof TLRPC.TL_inputMediaDocument) {
                TLRPC.TL_inputMediaDocument tL_inputMediaDocument2 = (TLRPC.TL_inputMediaDocument) obj2;
                TLRPC.TL_inputDocumentFileLocation tL_inputDocumentFileLocation4 = new TLRPC.TL_inputDocumentFileLocation();
                tL_inputDocumentFileLocation4.id = tL_inputMediaDocument2.id.id;
                return new Pair<>(tL_inputDocumentFileLocation4, "file_" + tL_inputMediaDocument2.id.id);
            }
            if (obj2 instanceof TLRPC.TL_inputMediaPhoto) {
                TLRPC.TL_inputMediaPhoto tL_inputMediaPhoto2 = (TLRPC.TL_inputMediaPhoto) obj2;
                TLRPC.TL_inputPhotoFileLocation tL_inputPhotoFileLocation3 = new TLRPC.TL_inputPhotoFileLocation();
                tL_inputPhotoFileLocation3.id = tL_inputMediaPhoto2.id.id;
                return new Pair<>(tL_inputPhotoFileLocation3, "photo_" + tL_inputMediaPhoto2.id.id);
            }
            if (obj2 instanceof TLRPC.TL_messages_sendMedia) {
                TLRPC.InputMedia inputMedia2 = ((TLRPC.TL_messages_sendMedia) obj2).media;
                if (inputMedia2 instanceof TLRPC.TL_inputMediaDocument) {
                    TLRPC.TL_inputMediaDocument tL_inputMediaDocument3 = (TLRPC.TL_inputMediaDocument) inputMedia2;
                    TLRPC.TL_inputDocumentFileLocation tL_inputDocumentFileLocation5 = new TLRPC.TL_inputDocumentFileLocation();
                    tL_inputDocumentFileLocation5.id = tL_inputMediaDocument3.id.id;
                    return new Pair<>(tL_inputDocumentFileLocation5, "file_" + tL_inputMediaDocument3.id.id);
                }
                if (inputMedia2 instanceof TLRPC.TL_inputMediaPhoto) {
                    TLRPC.TL_inputMediaPhoto tL_inputMediaPhoto3 = (TLRPC.TL_inputMediaPhoto) inputMedia2;
                    TLRPC.TL_inputPhotoFileLocation tL_inputPhotoFileLocation4 = new TLRPC.TL_inputPhotoFileLocation();
                    tL_inputPhotoFileLocation4.id = tL_inputMediaPhoto3.id.id;
                    return new Pair<>(tL_inputPhotoFileLocation4, "photo_" + tL_inputMediaPhoto3.id.id);
                }
                if (inputMedia2 instanceof TLRPC.TL_inputMediaPaidMedia) {
                    TLRPC.TL_inputMediaPaidMedia tL_inputMediaPaidMedia = (TLRPC.TL_inputMediaPaidMedia) inputMedia2;
                    if (!(obj instanceof ArrayList) && tL_inputMediaPaidMedia.extended_media.size() == 1) {
                        TLRPC.InputMedia inputMedia3 = (TLRPC.InputMedia) tL_inputMediaPaidMedia.extended_media.get(0);
                        if (inputMedia3 instanceof TLRPC.TL_inputMediaDocument) {
                            TLRPC.TL_inputMediaDocument tL_inputMediaDocument4 = (TLRPC.TL_inputMediaDocument) inputMedia3;
                            TLRPC.TL_inputDocumentFileLocation tL_inputDocumentFileLocation6 = new TLRPC.TL_inputDocumentFileLocation();
                            tL_inputDocumentFileLocation6.id = tL_inputMediaDocument4.id.id;
                            return new Pair<>(tL_inputDocumentFileLocation6, "file_" + tL_inputMediaDocument4.id.id);
                        }
                        if (inputMedia3 instanceof TLRPC.TL_inputMediaPhoto) {
                            TLRPC.TL_inputMediaPhoto tL_inputMediaPhoto4 = (TLRPC.TL_inputMediaPhoto) inputMedia3;
                            TLRPC.TL_inputPhotoFileLocation tL_inputPhotoFileLocation5 = new TLRPC.TL_inputPhotoFileLocation();
                            tL_inputPhotoFileLocation5.id = tL_inputMediaPhoto4.id.id;
                            return new Pair<>(tL_inputPhotoFileLocation5, "photo_" + tL_inputMediaPhoto4.id.id);
                        }
                    }
                }
            } else if (obj2 instanceof TLRPC.TL_messages_editMessage) {
                TLRPC.InputMedia inputMedia4 = ((TLRPC.TL_messages_editMessage) obj2).media;
                if (inputMedia4 instanceof TLRPC.TL_inputMediaDocument) {
                    TLRPC.TL_inputMediaDocument tL_inputMediaDocument5 = (TLRPC.TL_inputMediaDocument) inputMedia4;
                    TLRPC.TL_inputDocumentFileLocation tL_inputDocumentFileLocation7 = new TLRPC.TL_inputDocumentFileLocation();
                    tL_inputDocumentFileLocation7.id = tL_inputMediaDocument5.id.id;
                    return new Pair<>(tL_inputDocumentFileLocation7, "file_" + tL_inputMediaDocument5.id.id);
                }
                if (inputMedia4 instanceof TLRPC.TL_inputMediaPhoto) {
                    TLRPC.TL_inputMediaPhoto tL_inputMediaPhoto5 = (TLRPC.TL_inputMediaPhoto) inputMedia4;
                    TLRPC.TL_inputPhotoFileLocation tL_inputPhotoFileLocation6 = new TLRPC.TL_inputPhotoFileLocation();
                    tL_inputPhotoFileLocation6.id = tL_inputMediaPhoto5.id.id;
                    return new Pair<>(tL_inputPhotoFileLocation6, "photo_" + tL_inputMediaPhoto5.id.id);
                }
            } else {
                if (obj2 instanceof TLRPC.TL_messages_saveGif) {
                    TLRPC.TL_messages_saveGif tL_messages_saveGif = (TLRPC.TL_messages_saveGif) obj2;
                    TLRPC.TL_inputDocumentFileLocation tL_inputDocumentFileLocation8 = new TLRPC.TL_inputDocumentFileLocation();
                    tL_inputDocumentFileLocation8.id = tL_messages_saveGif.id.id;
                    return new Pair<>(tL_inputDocumentFileLocation8, "file_" + tL_messages_saveGif.id.id);
                }
                if (obj2 instanceof TLRPC.TL_messages_saveRecentSticker) {
                    TLRPC.TL_messages_saveRecentSticker tL_messages_saveRecentSticker = (TLRPC.TL_messages_saveRecentSticker) obj2;
                    TLRPC.TL_inputDocumentFileLocation tL_inputDocumentFileLocation9 = new TLRPC.TL_inputDocumentFileLocation();
                    tL_inputDocumentFileLocation9.id = tL_messages_saveRecentSticker.id.id;
                    return new Pair<>(tL_inputDocumentFileLocation9, "file_" + tL_messages_saveRecentSticker.id.id);
                }
                if (obj2 instanceof TLRPC.TL_stickers_addStickerToSet) {
                    TLRPC.TL_stickers_addStickerToSet tL_stickers_addStickerToSet = (TLRPC.TL_stickers_addStickerToSet) obj2;
                    TLRPC.TL_inputDocumentFileLocation tL_inputDocumentFileLocation10 = new TLRPC.TL_inputDocumentFileLocation();
                    tL_inputDocumentFileLocation10.id = tL_stickers_addStickerToSet.sticker.document.id;
                    return new Pair<>(tL_inputDocumentFileLocation10, "file_" + tL_stickers_addStickerToSet.sticker.document.id);
                }
                if (obj2 instanceof TLRPC.TL_messages_faveSticker) {
                    TLRPC.TL_messages_faveSticker tL_messages_faveSticker = (TLRPC.TL_messages_faveSticker) obj2;
                    TLRPC.TL_inputDocumentFileLocation tL_inputDocumentFileLocation11 = new TLRPC.TL_inputDocumentFileLocation();
                    tL_inputDocumentFileLocation11.id = tL_messages_faveSticker.id.id;
                    return new Pair<>(tL_inputDocumentFileLocation11, "file_" + tL_messages_faveSticker.id.id);
                }
                if (obj2 instanceof TLRPC.TL_messages_getAttachedStickers) {
                    TLRPC.InputStickeredMedia inputStickeredMedia = ((TLRPC.TL_messages_getAttachedStickers) obj2).media;
                    if (inputStickeredMedia instanceof TLRPC.TL_inputStickeredMediaDocument) {
                        TLRPC.TL_inputStickeredMediaDocument tL_inputStickeredMediaDocument = (TLRPC.TL_inputStickeredMediaDocument) inputStickeredMedia;
                        TLRPC.TL_inputDocumentFileLocation tL_inputDocumentFileLocation12 = new TLRPC.TL_inputDocumentFileLocation();
                        tL_inputDocumentFileLocation12.id = tL_inputStickeredMediaDocument.id.id;
                        return new Pair<>(tL_inputDocumentFileLocation12, "file_" + tL_inputStickeredMediaDocument.id.id);
                    }
                    if (inputStickeredMedia instanceof TLRPC.TL_inputStickeredMediaPhoto) {
                        TLRPC.TL_inputStickeredMediaPhoto tL_inputStickeredMediaPhoto = (TLRPC.TL_inputStickeredMediaPhoto) inputStickeredMedia;
                        TLRPC.TL_inputPhotoFileLocation tL_inputPhotoFileLocation7 = new TLRPC.TL_inputPhotoFileLocation();
                        tL_inputPhotoFileLocation7.id = tL_inputStickeredMediaPhoto.id.id;
                        return new Pair<>(tL_inputPhotoFileLocation7, "photo_" + tL_inputStickeredMediaPhoto.id.id);
                    }
                } else {
                    if (obj2 instanceof TLRPC.TL_inputFileLocation) {
                        TLRPC.TL_inputFileLocation tL_inputFileLocation = (TLRPC.TL_inputFileLocation) obj2;
                        return new Pair<>(tL_inputFileLocation, "loc_" + tL_inputFileLocation.local_id + "_" + tL_inputFileLocation.volume_id);
                    }
                    if (obj2 instanceof TLRPC.TL_inputDocumentFileLocation) {
                        TLRPC.TL_inputDocumentFileLocation tL_inputDocumentFileLocation13 = (TLRPC.TL_inputDocumentFileLocation) obj2;
                        return new Pair<>(tL_inputDocumentFileLocation13, "file_" + tL_inputDocumentFileLocation13.id);
                    }
                    if (obj2 instanceof TLRPC.TL_inputPhotoFileLocation) {
                        TLRPC.TL_inputPhotoFileLocation tL_inputPhotoFileLocation8 = (TLRPC.TL_inputPhotoFileLocation) obj2;
                        return new Pair<>(tL_inputPhotoFileLocation8, "photo_" + tL_inputPhotoFileLocation8.id);
                    }
                    if (obj2 instanceof TLRPC.TL_inputPeerPhotoFileLocation) {
                        TLRPC.TL_inputPeerPhotoFileLocation tL_inputPeerPhotoFileLocation = (TLRPC.TL_inputPeerPhotoFileLocation) obj2;
                        return new Pair<>(tL_inputPeerPhotoFileLocation, "avatar_" + tL_inputPeerPhotoFileLocation.id);
                    }
                }
            }
        }
        return null;
    }

    /* JADX WARN: Code duplicated, block: B:73:0x0154  */
    public void requestReference(Object obj, Object... objArr) {
        String str;
        TLRPC.Message message;
        TLRPC.MessageMedia messageMedia;
        Object obj2;
        int i = 0;
        if (BuildVars.LOGS_ENABLED) {
            FileLog.d("start loading request reference parent " + getObjectString(obj) + " args = " + objArr[0]);
        }
        Object obj3 = objArr[0];
        if (obj3 instanceof TLRPC.TL_messages_sendMultiMedia) {
            TLRPC.TL_messages_sendMultiMedia tL_messages_sendMultiMedia = (TLRPC.TL_messages_sendMultiMedia) obj3;
            ArrayList arrayList = (ArrayList) obj;
            this.multiMediaCache.put(tL_messages_sendMultiMedia, objArr);
            int size = tL_messages_sendMultiMedia.multi_media.size();
            for (int i2 = 0; i2 < size; i2++) {
                Object obj4 = (TLRPC.TL_inputSingleMedia) tL_messages_sendMultiMedia.multi_media.get(i2);
                Object obj5 = arrayList.get(i2);
                if (obj5 != null) {
                    requestReference(obj5, obj4, tL_messages_sendMultiMedia);
                }
            }
        } else if ((obj3 instanceof TLRPC.TL_messages_sendMedia) && (((TLRPC.TL_messages_sendMedia) obj3).media instanceof TLRPC.TL_inputMediaPaidMedia) && (obj instanceof ArrayList)) {
            TLRPC.TL_messages_sendMedia tL_messages_sendMedia = (TLRPC.TL_messages_sendMedia) obj3;
            TLRPC.TL_inputMediaPaidMedia tL_inputMediaPaidMedia = (TLRPC.TL_inputMediaPaidMedia) tL_messages_sendMedia.media;
            ArrayList arrayList2 = (ArrayList) obj;
            this.multiMediaCache.put(tL_messages_sendMedia, objArr);
            int size2 = tL_inputMediaPaidMedia.extended_media.size();
            for (int i3 = 0; i3 < size2; i3++) {
                Object obj6 = (TLRPC.InputMedia) tL_inputMediaPaidMedia.extended_media.get(i3);
                Object obj7 = arrayList2.get(i3);
                if (obj7 != null) {
                    requestReference(obj7, obj6, tL_messages_sendMedia);
                }
            }
        } else {
            Pair<TLRPC.InputFileLocation, String> locationAndKey = getLocationAndKey(obj, objArr);
            if (locationAndKey == null) {
                sendErrorToObject(objArr, 0);
                return;
            }
            TLRPC.InputFileLocation inputFileLocation = (TLRPC.InputFileLocation) locationAndKey.first;
            String str2 = (String) locationAndKey.second;
            if (obj instanceof MessageObject) {
                MessageObject messageObject = (MessageObject) obj;
                if (messageObject.getRealId() < 0 && (message = messageObject.messageOwner) != null && (messageMedia = message.media) != null && (obj2 = messageMedia.webpage) != null) {
                    obj = obj2;
                }
            }
            String keyForParentObject = getKeyForParentObject(obj);
            if (keyForParentObject == null) {
                sendErrorToObject(objArr, 0);
                return;
            }
            Requester requester = new Requester();
            requester.args = objArr;
            requester.location = inputFileLocation;
            requester.locationKey = str2;
            ArrayList<Requester> arrayList3 = this.locationRequester.get(str2);
            if (arrayList3 == null) {
                arrayList3 = new ArrayList<>();
                this.locationRequester.put(str2, arrayList3);
                i = 1;
            }
            arrayList3.add(requester);
            ArrayList<Requester> arrayList4 = this.parentRequester.get(keyForParentObject);
            if (arrayList4 == null) {
                arrayList4 = new ArrayList<>();
                this.parentRequester.put(keyForParentObject, arrayList4);
                i++;
            }
            arrayList4.add(requester);
            if (i == 2) {
                if (obj instanceof String) {
                    String str3 = (String) obj;
                    str = "wallpaper";
                    if (!"wallpaper".equals(str3)) {
                        str = "gif";
                        if (!str3.startsWith("gif")) {
                            str = "recent";
                            if (!"recent".equals(str3)) {
                                str = "fav";
                                if (!"fav".equals(str3)) {
                                    str = "update";
                                    if (!"update".equals(str3)) {
                                        str = str2;
                                    }
                                }
                            }
                        }
                    }
                } else {
                    str = str2;
                }
                cleanupCache();
                CachedResult cachedResponse = getCachedResponse(str);
                if (cachedResponse != null) {
                    if (onRequestComplete(str2, keyForParentObject, cachedResponse.response, null, false, true)) {
                        return;
                    } else {
                        this.responseCache.remove(str2);
                    }
                } else {
                    CachedResult cachedResponse2 = getCachedResponse(keyForParentObject);
                    if (cachedResponse2 != null) {
                        if (onRequestComplete(str2, keyForParentObject, cachedResponse2.response, null, false, true)) {
                            return;
                        } else {
                            this.responseCache.remove(keyForParentObject);
                        }
                    }
                }
                requestReferenceFromServer(obj, str2, keyForParentObject, objArr);
            }
        }
    }

    private String getObjectString(Object obj) {
        if (obj instanceof String) {
            return (String) obj;
        }
        if (obj instanceof TL_stories.StoryItem) {
            TL_stories.StoryItem storyItem = (TL_stories.StoryItem) obj;
            return "story(dialogId=" + storyItem.dialogId + " id=" + storyItem.id + ")";
        }
        if (!(obj instanceof MessageObject)) {
            if (obj == null) {
                return null;
            }
            return obj.getClass().getSimpleName();
        }
        MessageObject messageObject = (MessageObject) obj;
        return "message(dialogId=" + messageObject.getDialogId() + "messageId" + messageObject.getId() + ")";
    }

    private void broadcastWaitersData(ArrayList<Waiter> arrayList, TLObject tLObject, TLRPC.TL_error tL_error) {
        int size = arrayList.size();
        int i = 0;
        while (i < size) {
            Waiter waiter = arrayList.get(i);
            onRequestComplete(waiter.locationKey, waiter.parentKey, tLObject, tL_error, i == size + (-1), false);
            i++;
        }
        arrayList.clear();
    }

    private void requestReferenceFromServer(Object obj, final String str, final String str2, Object[] objArr) {
        if (obj instanceof StoriesController.BotPreview) {
            StoriesController.BotPreview botPreview = (StoriesController.BotPreview) obj;
            StoriesController.BotPreviewsList botPreviewsList = botPreview.list;
            if (botPreviewsList == null) {
                sendErrorToObject(objArr, 0);
                return;
            } else {
                botPreviewsList.requestReference(botPreview, new Utilities.Callback() { // from class: org.telegram.messenger.FileRefController$$ExternalSyntheticLambda16
                    @Override // org.telegram.messenger.Utilities.Callback
                    public final void run(Object obj2) {
                        this.f$0.lambda$requestReferenceFromServer$1(str, str2, (StoriesController.BotPreview) obj2);
                    }
                });
                return;
            }
        }
        if (obj instanceof TL_stories.StoryItem) {
            TL_stories.StoryItem storyItem = (TL_stories.StoryItem) obj;
            TL_stories.TL_stories_getStoriesByID tL_stories_getStoriesByID = new TL_stories.TL_stories_getStoriesByID();
            tL_stories_getStoriesByID.peer = getMessagesController().getInputPeer(storyItem.dialogId);
            tL_stories_getStoriesByID.id.add(Integer.valueOf(storyItem.id));
            getConnectionsManager().sendRequest(tL_stories_getStoriesByID, new RequestDelegate() { // from class: org.telegram.messenger.FileRefController$$ExternalSyntheticLambda27
                @Override // org.telegram.tgnet.RequestDelegate
                public final void run(TLObject tLObject, TLRPC.TL_error tL_error) {
                    this.f$0.lambda$requestReferenceFromServer$2(str, str2, tLObject, tL_error);
                }
            });
            return;
        }
        if (obj instanceof TLRPC.TL_help_premiumPromo) {
            getConnectionsManager().sendRequest(new TLRPC.TL_help_getPremiumPromo(), new RequestDelegate() { // from class: org.telegram.messenger.FileRefController$$ExternalSyntheticLambda37
                @Override // org.telegram.tgnet.RequestDelegate
                public final void run(TLObject tLObject, TLRPC.TL_error tL_error) {
                    this.f$0.lambda$requestReferenceFromServer$3(str, str2, tLObject, tL_error);
                }
            });
            return;
        }
        if (obj instanceof TLRPC.TL_availableReaction) {
            TLRPC.TL_messages_getAvailableReactions tL_messages_getAvailableReactions = new TLRPC.TL_messages_getAvailableReactions();
            tL_messages_getAvailableReactions.hash = 0;
            getConnectionsManager().sendRequest(tL_messages_getAvailableReactions, new RequestDelegate() { // from class: org.telegram.messenger.FileRefController$$ExternalSyntheticLambda38
                @Override // org.telegram.tgnet.RequestDelegate
                public final void run(TLObject tLObject, TLRPC.TL_error tL_error) {
                    this.f$0.lambda$requestReferenceFromServer$4(str, str2, tLObject, tL_error);
                }
            });
            return;
        }
        if (obj instanceof TL_bots.BotInfo) {
            TLRPC.TL_users_getFullUser tL_users_getFullUser = new TLRPC.TL_users_getFullUser();
            tL_users_getFullUser.id = getMessagesController().getInputUser(((TL_bots.BotInfo) obj).user_id);
            getConnectionsManager().sendRequest(tL_users_getFullUser, new RequestDelegate() { // from class: org.telegram.messenger.FileRefController$$ExternalSyntheticLambda39
                @Override // org.telegram.tgnet.RequestDelegate
                public final void run(TLObject tLObject, TLRPC.TL_error tL_error) {
                    this.f$0.lambda$requestReferenceFromServer$5(str, str2, tLObject, tL_error);
                }
            });
            return;
        }
        if (obj instanceof TLRPC.TL_attachMenuBot) {
            TLRPC.TL_messages_getAttachMenuBot tL_messages_getAttachMenuBot = new TLRPC.TL_messages_getAttachMenuBot();
            tL_messages_getAttachMenuBot.bot = getMessagesController().getInputUser(((TLRPC.TL_attachMenuBot) obj).bot_id);
            getConnectionsManager().sendRequest(tL_messages_getAttachMenuBot, new RequestDelegate() { // from class: org.telegram.messenger.FileRefController$$ExternalSyntheticLambda40
                @Override // org.telegram.tgnet.RequestDelegate
                public final void run(TLObject tLObject, TLRPC.TL_error tL_error) {
                    this.f$0.lambda$requestReferenceFromServer$6(str, str2, tLObject, tL_error);
                }
            });
            return;
        }
        if (obj instanceof MessageObject) {
            MessageObject messageObject = (MessageObject) obj;
            long channelId = messageObject.getChannelId();
            if (messageObject.scheduled) {
                TLRPC.TL_messages_getScheduledMessages tL_messages_getScheduledMessages = new TLRPC.TL_messages_getScheduledMessages();
                tL_messages_getScheduledMessages.peer = getMessagesController().getInputPeer(messageObject.getDialogId());
                tL_messages_getScheduledMessages.id.add(Integer.valueOf(messageObject.getRealId()));
                getConnectionsManager().sendRequest(tL_messages_getScheduledMessages, new RequestDelegate() { // from class: org.telegram.messenger.FileRefController$$ExternalSyntheticLambda41
                    @Override // org.telegram.tgnet.RequestDelegate
                    public final void run(TLObject tLObject, TLRPC.TL_error tL_error) {
                        this.f$0.lambda$requestReferenceFromServer$7(str, str2, tLObject, tL_error);
                    }
                });
                return;
            }
            if (messageObject.isQuickReply()) {
                TLRPC.TL_messages_getQuickReplyMessages tL_messages_getQuickReplyMessages = new TLRPC.TL_messages_getQuickReplyMessages();
                tL_messages_getQuickReplyMessages.shortcut_id = messageObject.getQuickReplyId();
                tL_messages_getQuickReplyMessages.flags |= 1;
                tL_messages_getQuickReplyMessages.id.add(Integer.valueOf(messageObject.getRealId()));
                getConnectionsManager().sendRequest(tL_messages_getQuickReplyMessages, new RequestDelegate() { // from class: org.telegram.messenger.FileRefController$$ExternalSyntheticLambda42
                    @Override // org.telegram.tgnet.RequestDelegate
                    public final void run(TLObject tLObject, TLRPC.TL_error tL_error) {
                        this.f$0.lambda$requestReferenceFromServer$8(str, str2, tLObject, tL_error);
                    }
                });
                return;
            }
            if (channelId != 0) {
                TLRPC.TL_channels_getMessages tL_channels_getMessages = new TLRPC.TL_channels_getMessages();
                tL_channels_getMessages.channel = getMessagesController().getInputChannel(channelId);
                tL_channels_getMessages.id.add(Integer.valueOf(messageObject.getRealId()));
                getConnectionsManager().sendRequest(tL_channels_getMessages, new RequestDelegate() { // from class: org.telegram.messenger.FileRefController$$ExternalSyntheticLambda43
                    @Override // org.telegram.tgnet.RequestDelegate
                    public final void run(TLObject tLObject, TLRPC.TL_error tL_error) {
                        this.f$0.lambda$requestReferenceFromServer$9(str, str2, tLObject, tL_error);
                    }
                });
                return;
            }
            TLRPC.TL_messages_getMessages tL_messages_getMessages = new TLRPC.TL_messages_getMessages();
            tL_messages_getMessages.id.add(Integer.valueOf(messageObject.getRealId()));
            getConnectionsManager().sendRequest(tL_messages_getMessages, new RequestDelegate() { // from class: org.telegram.messenger.FileRefController$$ExternalSyntheticLambda44
                @Override // org.telegram.tgnet.RequestDelegate
                public final void run(TLObject tLObject, TLRPC.TL_error tL_error) {
                    this.f$0.lambda$requestReferenceFromServer$10(str, str2, tLObject, tL_error);
                }
            });
            return;
        }
        if (obj instanceof TLRPC.TL_wallPaper) {
            TLRPC.TL_wallPaper tL_wallPaper = (TLRPC.TL_wallPaper) obj;
            TL_account.getWallPaper getwallpaper = new TL_account.getWallPaper();
            TLRPC.TL_inputWallPaper tL_inputWallPaper = new TLRPC.TL_inputWallPaper();
            tL_inputWallPaper.id = tL_wallPaper.id;
            tL_inputWallPaper.access_hash = tL_wallPaper.access_hash;
            getwallpaper.wallpaper = tL_inputWallPaper;
            getConnectionsManager().sendRequest(getwallpaper, new RequestDelegate() { // from class: org.telegram.messenger.FileRefController$$ExternalSyntheticLambda17
                @Override // org.telegram.tgnet.RequestDelegate
                public final void run(TLObject tLObject, TLRPC.TL_error tL_error) {
                    this.f$0.lambda$requestReferenceFromServer$11(str, str2, tLObject, tL_error);
                }
            });
            return;
        }
        if (obj instanceof TLRPC.TL_theme) {
            TLRPC.TL_theme tL_theme = (TLRPC.TL_theme) obj;
            TL_account.getTheme gettheme = new TL_account.getTheme();
            TLRPC.TL_inputTheme tL_inputTheme = new TLRPC.TL_inputTheme();
            tL_inputTheme.id = tL_theme.id;
            tL_inputTheme.access_hash = tL_theme.access_hash;
            gettheme.theme = tL_inputTheme;
            gettheme.format = "android";
            getConnectionsManager().sendRequest(gettheme, new RequestDelegate() { // from class: org.telegram.messenger.FileRefController$$ExternalSyntheticLambda18
                @Override // org.telegram.tgnet.RequestDelegate
                public final void run(TLObject tLObject, TLRPC.TL_error tL_error) {
                    this.f$0.lambda$requestReferenceFromServer$12(str, str2, tLObject, tL_error);
                }
            });
            return;
        }
        if (obj instanceof TLRPC.WebPage) {
            TLRPC.TL_messages_getWebPage tL_messages_getWebPage = new TLRPC.TL_messages_getWebPage();
            tL_messages_getWebPage.url = ((TLRPC.WebPage) obj).url;
            tL_messages_getWebPage.hash = 0;
            getConnectionsManager().sendRequest(tL_messages_getWebPage, new RequestDelegate() { // from class: org.telegram.messenger.FileRefController$$ExternalSyntheticLambda19
                @Override // org.telegram.tgnet.RequestDelegate
                public final void run(TLObject tLObject, TLRPC.TL_error tL_error) {
                    this.f$0.lambda$requestReferenceFromServer$13(str, str2, tLObject, tL_error);
                }
            });
            return;
        }
        if (obj instanceof TLRPC.User) {
            TLRPC.TL_users_getUsers tL_users_getUsers = new TLRPC.TL_users_getUsers();
            tL_users_getUsers.id.add(getMessagesController().getInputUser((TLRPC.User) obj));
            getConnectionsManager().sendRequest(tL_users_getUsers, new RequestDelegate() { // from class: org.telegram.messenger.FileRefController$$ExternalSyntheticLambda20
                @Override // org.telegram.tgnet.RequestDelegate
                public final void run(TLObject tLObject, TLRPC.TL_error tL_error) {
                    this.f$0.lambda$requestReferenceFromServer$14(str, str2, tLObject, tL_error);
                }
            });
            return;
        }
        if (obj instanceof TLRPC.Chat) {
            TLRPC.Chat chat = (TLRPC.Chat) obj;
            if (chat instanceof TLRPC.TL_chat) {
                TLRPC.TL_messages_getChats tL_messages_getChats = new TLRPC.TL_messages_getChats();
                tL_messages_getChats.id.add(Long.valueOf(chat.id));
                getConnectionsManager().sendRequest(tL_messages_getChats, new RequestDelegate() { // from class: org.telegram.messenger.FileRefController$$ExternalSyntheticLambda21
                    @Override // org.telegram.tgnet.RequestDelegate
                    public final void run(TLObject tLObject, TLRPC.TL_error tL_error) {
                        this.f$0.lambda$requestReferenceFromServer$15(str, str2, tLObject, tL_error);
                    }
                });
                return;
            } else {
                if (chat instanceof TLRPC.TL_channel) {
                    TLRPC.TL_channels_getChannels tL_channels_getChannels = new TLRPC.TL_channels_getChannels();
                    tL_channels_getChannels.id.add(MessagesController.getInputChannel(chat));
                    getConnectionsManager().sendRequest(tL_channels_getChannels, new RequestDelegate() { // from class: org.telegram.messenger.FileRefController$$ExternalSyntheticLambda22
                        @Override // org.telegram.tgnet.RequestDelegate
                        public final void run(TLObject tLObject, TLRPC.TL_error tL_error) {
                            this.f$0.lambda$requestReferenceFromServer$16(str, str2, tLObject, tL_error);
                        }
                    });
                    return;
                }
                return;
            }
        }
        if (obj instanceof String) {
            String str3 = (String) obj;
            if ("wallpaper".equals(str3)) {
                if (this.wallpaperWaiters.isEmpty()) {
                    getConnectionsManager().sendRequest(new TL_account.getWallPapers(), new RequestDelegate() { // from class: org.telegram.messenger.FileRefController$$ExternalSyntheticLambda23
                        @Override // org.telegram.tgnet.RequestDelegate
                        public final void run(TLObject tLObject, TLRPC.TL_error tL_error) {
                            this.f$0.lambda$requestReferenceFromServer$17(tLObject, tL_error);
                        }
                    });
                }
                this.wallpaperWaiters.add(new Waiter(str, str2));
                return;
            }
            if (str3.startsWith("gif")) {
                if (this.savedGifsWaiters.isEmpty()) {
                    getConnectionsManager().sendRequest(new TLRPC.TL_messages_getSavedGifs(), new RequestDelegate() { // from class: org.telegram.messenger.FileRefController$$ExternalSyntheticLambda24
                        @Override // org.telegram.tgnet.RequestDelegate
                        public final void run(TLObject tLObject, TLRPC.TL_error tL_error) {
                            this.f$0.lambda$requestReferenceFromServer$18(tLObject, tL_error);
                        }
                    });
                }
                this.savedGifsWaiters.add(new Waiter(str, str2));
                return;
            }
            if ("recent".equals(str3)) {
                if (this.recentStickersWaiter.isEmpty()) {
                    getConnectionsManager().sendRequest(new TLRPC.TL_messages_getRecentStickers(), new RequestDelegate() { // from class: org.telegram.messenger.FileRefController$$ExternalSyntheticLambda25
                        @Override // org.telegram.tgnet.RequestDelegate
                        public final void run(TLObject tLObject, TLRPC.TL_error tL_error) {
                            this.f$0.lambda$requestReferenceFromServer$19(tLObject, tL_error);
                        }
                    });
                }
                this.recentStickersWaiter.add(new Waiter(str, str2));
                return;
            }
            if ("fav".equals(str3)) {
                if (this.favStickersWaiter.isEmpty()) {
                    getConnectionsManager().sendRequest(new TLRPC.TL_messages_getFavedStickers(), new RequestDelegate() { // from class: org.telegram.messenger.FileRefController$$ExternalSyntheticLambda26
                        @Override // org.telegram.tgnet.RequestDelegate
                        public final void run(TLObject tLObject, TLRPC.TL_error tL_error) {
                            this.f$0.lambda$requestReferenceFromServer$20(tLObject, tL_error);
                        }
                    });
                }
                this.favStickersWaiter.add(new Waiter(str, str2));
                return;
            }
            if ("update".equals(str3)) {
                UpdaterUtils.getAppUpdate(new Utilities.Callback2() { // from class: org.telegram.messenger.FileRefController$$ExternalSyntheticLambda28
                    @Override // org.telegram.messenger.Utilities.Callback2
                    public final void run(Object obj2, Object obj3) {
                        this.f$0.lambda$requestReferenceFromServer$21(str, str2, (TLRPC.TL_help_appUpdate) obj2, (TLRPC.TL_error) obj3);
                    }
                });
                TLRPC.TL_help_getAppUpdate tL_help_getAppUpdate = new TLRPC.TL_help_getAppUpdate();
                try {
                    tL_help_getAppUpdate.source = AyuConstants.BUILD_STORE_PACKAGE;
                } catch (Exception unused) {
                }
                if (tL_help_getAppUpdate.source == null) {
                    tL_help_getAppUpdate.source = _UrlKt.FRAGMENT_ENCODE_SET;
                }
                getConnectionsManager().sendRequest(tL_help_getAppUpdate, new RequestDelegate() { // from class: org.telegram.messenger.FileRefController$$ExternalSyntheticLambda29
                    @Override // org.telegram.tgnet.RequestDelegate
                    public final void run(TLObject tLObject, TLRPC.TL_error tL_error) {
                        FileRefController.$r8$lambda$b5qZPw2Lh2oskQb_eey51J3RR1M(tLObject, tL_error);
                    }
                });
                return;
            }
            if (str3.startsWith("avatar_")) {
                long jLongValue = Utilities.parseLong(str3).longValue();
                if (jLongValue > 0) {
                    TLRPC.TL_photos_getUserPhotos tL_photos_getUserPhotos = new TLRPC.TL_photos_getUserPhotos();
                    tL_photos_getUserPhotos.limit = 80;
                    tL_photos_getUserPhotos.offset = 0;
                    tL_photos_getUserPhotos.max_id = 0L;
                    tL_photos_getUserPhotos.user_id = getMessagesController().getInputUser(jLongValue);
                    getConnectionsManager().sendRequest(tL_photos_getUserPhotos, new RequestDelegate() { // from class: org.telegram.messenger.FileRefController$$ExternalSyntheticLambda30
                        @Override // org.telegram.tgnet.RequestDelegate
                        public final void run(TLObject tLObject, TLRPC.TL_error tL_error) {
                            this.f$0.lambda$requestReferenceFromServer$23(str, str2, tLObject, tL_error);
                        }
                    });
                    return;
                }
                TLRPC.TL_messages_search tL_messages_search = new TLRPC.TL_messages_search();
                tL_messages_search.filter = new TLRPC.TL_inputMessagesFilterChatPhotos();
                tL_messages_search.limit = 80;
                tL_messages_search.offset_id = 0;
                tL_messages_search.q = _UrlKt.FRAGMENT_ENCODE_SET;
                tL_messages_search.peer = getMessagesController().getInputPeer(jLongValue);
                getConnectionsManager().sendRequest(tL_messages_search, new RequestDelegate() { // from class: org.telegram.messenger.FileRefController$$ExternalSyntheticLambda31
                    @Override // org.telegram.tgnet.RequestDelegate
                    public final void run(TLObject tLObject, TLRPC.TL_error tL_error) {
                        this.f$0.lambda$requestReferenceFromServer$24(str, str2, tLObject, tL_error);
                    }
                });
                return;
            }
            if (str3.startsWith("sent_")) {
                String[] strArrSplit = str3.split("_");
                if (strArrSplit.length >= 3) {
                    long jLongValue2 = Utilities.parseLong(strArrSplit[1]).longValue();
                    if (jLongValue2 != 0) {
                        TLRPC.TL_channels_getMessages tL_channels_getMessages2 = new TLRPC.TL_channels_getMessages();
                        tL_channels_getMessages2.channel = getMessagesController().getInputChannel(jLongValue2);
                        tL_channels_getMessages2.id.add(Utilities.parseInt((CharSequence) strArrSplit[2]));
                        getConnectionsManager().sendRequest(tL_channels_getMessages2, new RequestDelegate() { // from class: org.telegram.messenger.FileRefController$$ExternalSyntheticLambda32
                            @Override // org.telegram.tgnet.RequestDelegate
                            public final void run(TLObject tLObject, TLRPC.TL_error tL_error) {
                                this.f$0.lambda$requestReferenceFromServer$25(str, str2, tLObject, tL_error);
                            }
                        });
                        return;
                    }
                    TLRPC.TL_messages_getMessages tL_messages_getMessages2 = new TLRPC.TL_messages_getMessages();
                    tL_messages_getMessages2.id.add(Utilities.parseInt((CharSequence) strArrSplit[2]));
                    getConnectionsManager().sendRequest(tL_messages_getMessages2, new RequestDelegate() { // from class: org.telegram.messenger.FileRefController$$ExternalSyntheticLambda33
                        @Override // org.telegram.tgnet.RequestDelegate
                        public final void run(TLObject tLObject, TLRPC.TL_error tL_error) {
                            this.f$0.lambda$requestReferenceFromServer$26(str, str2, tLObject, tL_error);
                        }
                    });
                    return;
                }
                sendErrorToObject(objArr, 0);
                return;
            }
            sendErrorToObject(objArr, 0);
            return;
        }
        if (obj instanceof TLRPC.TL_messages_stickerSet) {
            TLRPC.TL_messages_getStickerSet tL_messages_getStickerSet = new TLRPC.TL_messages_getStickerSet();
            TLRPC.TL_inputStickerSetID tL_inputStickerSetID = new TLRPC.TL_inputStickerSetID();
            tL_messages_getStickerSet.stickerset = tL_inputStickerSetID;
            TLRPC.StickerSet stickerSet = ((TLRPC.TL_messages_stickerSet) obj).set;
            tL_inputStickerSetID.id = stickerSet.id;
            tL_inputStickerSetID.access_hash = stickerSet.access_hash;
            getConnectionsManager().sendRequest(tL_messages_getStickerSet, new RequestDelegate() { // from class: org.telegram.messenger.FileRefController$$ExternalSyntheticLambda34
                @Override // org.telegram.tgnet.RequestDelegate
                public final void run(TLObject tLObject, TLRPC.TL_error tL_error) {
                    this.f$0.lambda$requestReferenceFromServer$27(str, str2, tLObject, tL_error);
                }
            });
            return;
        }
        if (obj instanceof TLRPC.StickerSetCovered) {
            TLRPC.TL_messages_getStickerSet tL_messages_getStickerSet2 = new TLRPC.TL_messages_getStickerSet();
            TLRPC.TL_inputStickerSetID tL_inputStickerSetID2 = new TLRPC.TL_inputStickerSetID();
            tL_messages_getStickerSet2.stickerset = tL_inputStickerSetID2;
            TLRPC.StickerSet stickerSet2 = ((TLRPC.StickerSetCovered) obj).set;
            tL_inputStickerSetID2.id = stickerSet2.id;
            tL_inputStickerSetID2.access_hash = stickerSet2.access_hash;
            getConnectionsManager().sendRequest(tL_messages_getStickerSet2, new RequestDelegate() { // from class: org.telegram.messenger.FileRefController$$ExternalSyntheticLambda35
                @Override // org.telegram.tgnet.RequestDelegate
                public final void run(TLObject tLObject, TLRPC.TL_error tL_error) {
                    this.f$0.lambda$requestReferenceFromServer$28(str, str2, tLObject, tL_error);
                }
            });
            return;
        }
        if (obj instanceof TLRPC.InputStickerSet) {
            TLRPC.TL_messages_getStickerSet tL_messages_getStickerSet3 = new TLRPC.TL_messages_getStickerSet();
            tL_messages_getStickerSet3.stickerset = (TLRPC.InputStickerSet) obj;
            getConnectionsManager().sendRequest(tL_messages_getStickerSet3, new RequestDelegate() { // from class: org.telegram.messenger.FileRefController$$ExternalSyntheticLambda36
                @Override // org.telegram.tgnet.RequestDelegate
                public final void run(TLObject tLObject, TLRPC.TL_error tL_error) {
                    this.f$0.lambda$requestReferenceFromServer$29(str, str2, tLObject, tL_error);
                }
            });
            return;
        }
        sendErrorToObject(objArr, 0);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$requestReferenceFromServer$1(final String str, final String str2, final StoriesController.BotPreview botPreview) {
        Utilities.stageQueue.postRunnable(new Runnable() { // from class: org.telegram.messenger.FileRefController$$ExternalSyntheticLambda45
            @Override // java.lang.Runnable
            public final void run() {
                this.f$0.lambda$requestReferenceFromServer$0(str, str2, botPreview);
            }
        });
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$requestReferenceFromServer$0(String str, String str2, StoriesController.BotPreview botPreview) {
        onRequestComplete(str, str2, botPreview, null, true, false);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$requestReferenceFromServer$2(String str, String str2, TLObject tLObject, TLRPC.TL_error tL_error) {
        onRequestComplete(str, str2, tLObject, tL_error, true, false);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$requestReferenceFromServer$3(String str, String str2, TLObject tLObject, TLRPC.TL_error tL_error) {
        int iCurrentTimeMillis = (int) (System.currentTimeMillis() / 1000);
        if (tLObject instanceof TLRPC.TL_help_premiumPromo) {
            getMediaDataController().processLoadedPremiumPromo((TLRPC.TL_help_premiumPromo) tLObject, iCurrentTimeMillis, false);
        }
        onRequestComplete(str, str2, tLObject, tL_error, true, false);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$requestReferenceFromServer$4(String str, String str2, TLObject tLObject, TLRPC.TL_error tL_error) {
        onRequestComplete(str, str2, tLObject, tL_error, true, false);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$requestReferenceFromServer$5(String str, String str2, TLObject tLObject, TLRPC.TL_error tL_error) {
        onRequestComplete(str, str2, tLObject, tL_error, true, false);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$requestReferenceFromServer$6(String str, String str2, TLObject tLObject, TLRPC.TL_error tL_error) {
        onRequestComplete(str, str2, tLObject, tL_error, true, false);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$requestReferenceFromServer$7(String str, String str2, TLObject tLObject, TLRPC.TL_error tL_error) {
        onRequestComplete(str, str2, tLObject, tL_error, true, false);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$requestReferenceFromServer$8(String str, String str2, TLObject tLObject, TLRPC.TL_error tL_error) {
        onRequestComplete(str, str2, tLObject, tL_error, true, false);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$requestReferenceFromServer$9(String str, String str2, TLObject tLObject, TLRPC.TL_error tL_error) {
        onRequestComplete(str, str2, tLObject, tL_error, true, false);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$requestReferenceFromServer$10(String str, String str2, TLObject tLObject, TLRPC.TL_error tL_error) {
        onRequestComplete(str, str2, tLObject, tL_error, true, false);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$requestReferenceFromServer$11(String str, String str2, TLObject tLObject, TLRPC.TL_error tL_error) {
        onRequestComplete(str, str2, tLObject, tL_error, true, false);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$requestReferenceFromServer$12(String str, String str2, TLObject tLObject, TLRPC.TL_error tL_error) {
        onRequestComplete(str, str2, tLObject, tL_error, true, false);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$requestReferenceFromServer$13(String str, String str2, TLObject tLObject, TLRPC.TL_error tL_error) {
        onRequestComplete(str, str2, tLObject, tL_error, true, false);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$requestReferenceFromServer$14(String str, String str2, TLObject tLObject, TLRPC.TL_error tL_error) {
        onRequestComplete(str, str2, tLObject, tL_error, true, false);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$requestReferenceFromServer$15(String str, String str2, TLObject tLObject, TLRPC.TL_error tL_error) {
        onRequestComplete(str, str2, tLObject, tL_error, true, false);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$requestReferenceFromServer$16(String str, String str2, TLObject tLObject, TLRPC.TL_error tL_error) {
        onRequestComplete(str, str2, tLObject, tL_error, true, false);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$requestReferenceFromServer$17(TLObject tLObject, TLRPC.TL_error tL_error) {
        broadcastWaitersData(this.wallpaperWaiters, tLObject, tL_error);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$requestReferenceFromServer$18(TLObject tLObject, TLRPC.TL_error tL_error) {
        broadcastWaitersData(this.savedGifsWaiters, tLObject, tL_error);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$requestReferenceFromServer$19(TLObject tLObject, TLRPC.TL_error tL_error) {
        broadcastWaitersData(this.recentStickersWaiter, tLObject, tL_error);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$requestReferenceFromServer$20(TLObject tLObject, TLRPC.TL_error tL_error) {
        broadcastWaitersData(this.favStickersWaiter, tLObject, tL_error);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$requestReferenceFromServer$21(String str, String str2, TLRPC.TL_help_appUpdate tL_help_appUpdate, TLRPC.TL_error tL_error) {
        onRequestComplete(str, str2, tL_help_appUpdate, tL_error, true, false);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$requestReferenceFromServer$23(String str, String str2, TLObject tLObject, TLRPC.TL_error tL_error) {
        onRequestComplete(str, str2, tLObject, tL_error, true, false);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$requestReferenceFromServer$24(String str, String str2, TLObject tLObject, TLRPC.TL_error tL_error) {
        onRequestComplete(str, str2, tLObject, tL_error, true, false);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$requestReferenceFromServer$25(String str, String str2, TLObject tLObject, TLRPC.TL_error tL_error) {
        onRequestComplete(str, str2, tLObject, tL_error, false, false);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$requestReferenceFromServer$26(String str, String str2, TLObject tLObject, TLRPC.TL_error tL_error) {
        onRequestComplete(str, str2, tLObject, tL_error, false, false);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$requestReferenceFromServer$27(String str, String str2, TLObject tLObject, TLRPC.TL_error tL_error) {
        onRequestComplete(str, str2, tLObject, tL_error, true, false);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$requestReferenceFromServer$28(String str, String str2, TLObject tLObject, TLRPC.TL_error tL_error) {
        onRequestComplete(str, str2, tLObject, tL_error, true, false);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$requestReferenceFromServer$29(String str, String str2, TLObject tLObject, TLRPC.TL_error tL_error) {
        onRequestComplete(str, str2, tLObject, tL_error, true, false);
    }

    private boolean isSameReference(byte[] bArr, byte[] bArr2) {
        return Arrays.equals(bArr, bArr2);
    }

    private boolean onUpdateObjectReference(final Requester requester, byte[] bArr, TLRPC.InputFileLocation inputFileLocation, boolean z) {
        String strBytesToHex;
        Object obj;
        if (BuildVars.DEBUG_VERSION) {
            FileLog.d("fileref updated for " + requester.args[0] + " " + requester.locationKey);
        }
        if (requester.args[0] instanceof TL_stories.TL_storyItem) {
            ((TL_stories.TL_storyItem) requester.args[0]).media.document.file_reference = bArr;
            return true;
        }
        String strBytesToHex2 = null;
        if (requester.args[0] instanceof TLRPC.TL_inputSingleMedia) {
            final TLRPC.TL_messages_sendMultiMedia tL_messages_sendMultiMedia = (TLRPC.TL_messages_sendMultiMedia) requester.args[1];
            final Object[] objArr = this.multiMediaCache.get(tL_messages_sendMultiMedia);
            if (objArr == null) {
                return true;
            }
            TLRPC.TL_inputSingleMedia tL_inputSingleMedia = (TLRPC.TL_inputSingleMedia) requester.args[0];
            TLRPC.InputMedia inputMedia = tL_inputSingleMedia.media;
            if (inputMedia instanceof TLRPC.TL_inputMediaDocument) {
                TLRPC.TL_inputMediaDocument tL_inputMediaDocument = (TLRPC.TL_inputMediaDocument) inputMedia;
                if (z && isSameReference(tL_inputMediaDocument.id.file_reference, bArr)) {
                    return false;
                }
                tL_inputMediaDocument.id.file_reference = bArr;
            } else if (inputMedia instanceof TLRPC.TL_inputMediaPhoto) {
                TLRPC.TL_inputMediaPhoto tL_inputMediaPhoto = (TLRPC.TL_inputMediaPhoto) inputMedia;
                if (z && isSameReference(tL_inputMediaPhoto.id.file_reference, bArr)) {
                    return false;
                }
                tL_inputMediaPhoto.id.file_reference = bArr;
            }
            int iIndexOf = tL_messages_sendMultiMedia.multi_media.indexOf(tL_inputSingleMedia);
            if (iIndexOf < 0) {
                return true;
            }
            ArrayList arrayList = (ArrayList) objArr[3];
            arrayList.set(iIndexOf, null);
            boolean z2 = true;
            for (int i = 0; i < arrayList.size(); i++) {
                if (arrayList.get(i) != null) {
                    z2 = false;
                }
            }
            if (z2) {
                this.multiMediaCache.remove(tL_messages_sendMultiMedia);
                AndroidUtilities.runOnUIThread(new Runnable() { // from class: org.telegram.messenger.FileRefController$$ExternalSyntheticLambda8
                    @Override // java.lang.Runnable
                    public final void run() {
                        this.f$0.lambda$onUpdateObjectReference$30(tL_messages_sendMultiMedia, objArr);
                    }
                });
            }
        } else if (requester.args.length >= 2 && (requester.args[1] instanceof TLRPC.TL_messages_sendMedia) && (((TLRPC.TL_messages_sendMedia) requester.args[1]).media instanceof TLRPC.TL_inputMediaPaidMedia) && ((requester.args[0] instanceof TLRPC.TL_inputMediaPhoto) || (requester.args[0] instanceof TLRPC.TL_inputMediaDocument))) {
            final TLRPC.TL_messages_sendMedia tL_messages_sendMedia = (TLRPC.TL_messages_sendMedia) requester.args[1];
            final Object[] objArr2 = this.multiMediaCache.get(tL_messages_sendMedia);
            if (objArr2 == null) {
                return true;
            }
            if (requester.args[0] instanceof TLRPC.TL_inputMediaDocument) {
                TLRPC.TL_inputMediaDocument tL_inputMediaDocument2 = (TLRPC.TL_inputMediaDocument) requester.args[0];
                if (z && isSameReference(tL_inputMediaDocument2.id.file_reference, bArr)) {
                    return false;
                }
                tL_inputMediaDocument2.id.file_reference = bArr;
                obj = tL_inputMediaDocument2;
            } else if (requester.args[0] instanceof TLRPC.TL_inputMediaPhoto) {
                TLRPC.TL_inputMediaPhoto tL_inputMediaPhoto2 = (TLRPC.TL_inputMediaPhoto) requester.args[0];
                if (z && isSameReference(tL_inputMediaPhoto2.id.file_reference, bArr)) {
                    return false;
                }
                tL_inputMediaPhoto2.id.file_reference = bArr;
                obj = tL_inputMediaPhoto2;
            } else {
                obj = null;
            }
            int iIndexOf2 = ((TLRPC.TL_inputMediaPaidMedia) tL_messages_sendMedia.media).extended_media.indexOf(obj);
            if (iIndexOf2 < 0) {
                return true;
            }
            ArrayList arrayList2 = (ArrayList) objArr2[3];
            arrayList2.set(iIndexOf2, null);
            boolean z3 = true;
            for (int i2 = 0; i2 < arrayList2.size(); i2++) {
                if (arrayList2.get(i2) != null) {
                    z3 = false;
                }
            }
            if (z3) {
                this.multiMediaCache.remove(tL_messages_sendMedia);
                AndroidUtilities.runOnUIThread(new Runnable() { // from class: org.telegram.messenger.FileRefController$$ExternalSyntheticLambda9
                    @Override // java.lang.Runnable
                    public final void run() {
                        this.f$0.lambda$onUpdateObjectReference$31(tL_messages_sendMedia, objArr2);
                    }
                });
            }
        } else if (requester.args[0] instanceof TLRPC.TL_messages_sendMedia) {
            TLRPC.InputMedia inputMedia2 = ((TLRPC.TL_messages_sendMedia) requester.args[0]).media;
            if (inputMedia2 instanceof TLRPC.TL_inputMediaDocument) {
                TLRPC.TL_inputMediaDocument tL_inputMediaDocument3 = (TLRPC.TL_inputMediaDocument) inputMedia2;
                if (z && isSameReference(tL_inputMediaDocument3.id.file_reference, bArr)) {
                    return false;
                }
                tL_inputMediaDocument3.id.file_reference = bArr;
            } else if (inputMedia2 instanceof TLRPC.TL_inputMediaPhoto) {
                TLRPC.TL_inputMediaPhoto tL_inputMediaPhoto3 = (TLRPC.TL_inputMediaPhoto) inputMedia2;
                if (z && isSameReference(tL_inputMediaPhoto3.id.file_reference, bArr)) {
                    return false;
                }
                tL_inputMediaPhoto3.id.file_reference = bArr;
            }
            AndroidUtilities.runOnUIThread(new Runnable() { // from class: org.telegram.messenger.FileRefController$$ExternalSyntheticLambda10
                @Override // java.lang.Runnable
                public final void run() {
                    this.f$0.lambda$onUpdateObjectReference$32(requester);
                }
            });
        } else if (requester.args[0] instanceof TLRPC.TL_messages_editMessage) {
            TLRPC.InputMedia inputMedia3 = ((TLRPC.TL_messages_editMessage) requester.args[0]).media;
            if (inputMedia3 instanceof TLRPC.TL_inputMediaDocument) {
                TLRPC.TL_inputMediaDocument tL_inputMediaDocument4 = (TLRPC.TL_inputMediaDocument) inputMedia3;
                if (z && isSameReference(tL_inputMediaDocument4.id.file_reference, bArr)) {
                    return false;
                }
                tL_inputMediaDocument4.id.file_reference = bArr;
            } else if (inputMedia3 instanceof TLRPC.TL_inputMediaPhoto) {
                TLRPC.TL_inputMediaPhoto tL_inputMediaPhoto4 = (TLRPC.TL_inputMediaPhoto) inputMedia3;
                if (z && isSameReference(tL_inputMediaPhoto4.id.file_reference, bArr)) {
                    return false;
                }
                tL_inputMediaPhoto4.id.file_reference = bArr;
            }
            AndroidUtilities.runOnUIThread(new Runnable() { // from class: org.telegram.messenger.FileRefController$$ExternalSyntheticLambda11
                @Override // java.lang.Runnable
                public final void run() {
                    this.f$0.lambda$onUpdateObjectReference$33(requester);
                }
            });
        } else if (requester.args[0] instanceof TLRPC.TL_messages_saveGif) {
            TLRPC.TL_messages_saveGif tL_messages_saveGif = (TLRPC.TL_messages_saveGif) requester.args[0];
            if (z && isSameReference(tL_messages_saveGif.id.file_reference, bArr)) {
                return false;
            }
            tL_messages_saveGif.id.file_reference = bArr;
            getConnectionsManager().sendRequest(tL_messages_saveGif, new RequestDelegate() { // from class: org.telegram.messenger.FileRefController$$ExternalSyntheticLambda12
                @Override // org.telegram.tgnet.RequestDelegate
                public final void run(TLObject tLObject, TLRPC.TL_error tL_error) {
                    FileRefController.$r8$lambda$EY1qS7VyTjRp4fSjfQEL0VDJDao(tLObject, tL_error);
                }
            });
        } else if (requester.args[0] instanceof TLRPC.TL_messages_saveRecentSticker) {
            TLRPC.TL_messages_saveRecentSticker tL_messages_saveRecentSticker = (TLRPC.TL_messages_saveRecentSticker) requester.args[0];
            if (z && isSameReference(tL_messages_saveRecentSticker.id.file_reference, bArr)) {
                return false;
            }
            tL_messages_saveRecentSticker.id.file_reference = bArr;
            getConnectionsManager().sendRequest(tL_messages_saveRecentSticker, new RequestDelegate() { // from class: org.telegram.messenger.FileRefController$$ExternalSyntheticLambda13
                @Override // org.telegram.tgnet.RequestDelegate
                public final void run(TLObject tLObject, TLRPC.TL_error tL_error) {
                    FileRefController.$r8$lambda$LN1JdHjG3sV_jPNS2hg2hjBBeKc(tLObject, tL_error);
                }
            });
        } else if (requester.args[0] instanceof TLRPC.TL_stickers_addStickerToSet) {
            TLRPC.TL_stickers_addStickerToSet tL_stickers_addStickerToSet = (TLRPC.TL_stickers_addStickerToSet) requester.args[0];
            if (z && isSameReference(tL_stickers_addStickerToSet.sticker.document.file_reference, bArr)) {
                return false;
            }
            tL_stickers_addStickerToSet.sticker.document.file_reference = bArr;
            getConnectionsManager().sendRequest(tL_stickers_addStickerToSet, new RequestDelegate() { // from class: org.telegram.messenger.FileRefController$$ExternalSyntheticLambda14
                @Override // org.telegram.tgnet.RequestDelegate
                public final void run(TLObject tLObject, TLRPC.TL_error tL_error) {
                    FileRefController.$r8$lambda$zLpXUCCmmkTcmIXKtdR8GqrnykY(tLObject, tL_error);
                }
            });
        } else if (requester.args[0] instanceof TLRPC.TL_messages_faveSticker) {
            TLRPC.TL_messages_faveSticker tL_messages_faveSticker = (TLRPC.TL_messages_faveSticker) requester.args[0];
            if (z && isSameReference(tL_messages_faveSticker.id.file_reference, bArr)) {
                return false;
            }
            tL_messages_faveSticker.id.file_reference = bArr;
            getConnectionsManager().sendRequest(tL_messages_faveSticker, new RequestDelegate() { // from class: org.telegram.messenger.FileRefController$$ExternalSyntheticLambda15
                @Override // org.telegram.tgnet.RequestDelegate
                public final void run(TLObject tLObject, TLRPC.TL_error tL_error) {
                    FileRefController.$r8$lambda$bP6KBJG6eRnD1L1Fw9rrtHGm3Ic(tLObject, tL_error);
                }
            });
        } else if (requester.args[0] instanceof TLRPC.TL_messages_getAttachedStickers) {
            TLRPC.TL_messages_getAttachedStickers tL_messages_getAttachedStickers = (TLRPC.TL_messages_getAttachedStickers) requester.args[0];
            TLRPC.InputStickeredMedia inputStickeredMedia = tL_messages_getAttachedStickers.media;
            if (inputStickeredMedia instanceof TLRPC.TL_inputStickeredMediaDocument) {
                TLRPC.TL_inputStickeredMediaDocument tL_inputStickeredMediaDocument = (TLRPC.TL_inputStickeredMediaDocument) inputStickeredMedia;
                if (z && isSameReference(tL_inputStickeredMediaDocument.id.file_reference, bArr)) {
                    return false;
                }
                tL_inputStickeredMediaDocument.id.file_reference = bArr;
            } else if (inputStickeredMedia instanceof TLRPC.TL_inputStickeredMediaPhoto) {
                TLRPC.TL_inputStickeredMediaPhoto tL_inputStickeredMediaPhoto = (TLRPC.TL_inputStickeredMediaPhoto) inputStickeredMedia;
                if (z && isSameReference(tL_inputStickeredMediaPhoto.id.file_reference, bArr)) {
                    return false;
                }
                tL_inputStickeredMediaPhoto.id.file_reference = bArr;
            }
            getConnectionsManager().sendRequest(tL_messages_getAttachedStickers, (RequestDelegate) requester.args[1]);
        } else if (requester.args[1] instanceof FileLoadOperation) {
            FileLoadOperation fileLoadOperation = (FileLoadOperation) requester.args[1];
            if (inputFileLocation != null) {
                if (z && isSameReference(fileLoadOperation.location.file_reference, inputFileLocation.file_reference)) {
                    return false;
                }
                strBytesToHex = BuildVars.LOGS_ENABLED ? Utilities.bytesToHex(fileLoadOperation.location.file_reference) : null;
                fileLoadOperation.location = inputFileLocation;
                if (BuildVars.LOGS_ENABLED) {
                    strBytesToHex2 = Utilities.bytesToHex(inputFileLocation.file_reference);
                }
            } else {
                if (z && isSameReference(requester.location.file_reference, bArr)) {
                    return false;
                }
                String strBytesToHex3 = BuildVars.LOGS_ENABLED ? Utilities.bytesToHex(fileLoadOperation.location.file_reference) : null;
                TLRPC.InputFileLocation inputFileLocation2 = fileLoadOperation.location;
                requester.location.file_reference = bArr;
                inputFileLocation2.file_reference = bArr;
                strBytesToHex2 = BuildVars.LOGS_ENABLED ? Utilities.bytesToHex(fileLoadOperation.location.file_reference) : null;
                strBytesToHex = strBytesToHex3;
            }
            fileLoadOperation.requestingReference = false;
            if (BuildVars.LOGS_ENABLED) {
                FileLog.d("debug_loading: " + fileLoadOperation.getCacheFileFinal().getName() + " " + strBytesToHex + " " + strBytesToHex2 + " reference updated resume download");
            }
            fileLoadOperation.startDownloadRequest(-1);
        }
        return true;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$onUpdateObjectReference$30(TLRPC.TL_messages_sendMultiMedia tL_messages_sendMultiMedia, Object[] objArr) {
        getSendMessagesHelper().lambda$performSendMessageRequestMulti$57(tL_messages_sendMultiMedia, (ArrayList) objArr[1], (ArrayList) objArr[2], null, (SendMessagesHelper.DelayedMessage) objArr[4], ((Boolean) objArr[5]).booleanValue());
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$onUpdateObjectReference$31(TLRPC.TL_messages_sendMedia tL_messages_sendMedia, Object[] objArr) {
        getSendMessagesHelper().lambda$performSendMessageRequestMulti$57(tL_messages_sendMedia, (ArrayList) objArr[1], (ArrayList) objArr[2], null, (SendMessagesHelper.DelayedMessage) objArr[4], ((Boolean) objArr[5]).booleanValue());
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$onUpdateObjectReference$32(Requester requester) {
        getSendMessagesHelper().lambda$performSendMessageRequest$71((TLObject) requester.args[0], (MessageObject) requester.args[1], (String) requester.args[2], (SendMessagesHelper.DelayedMessage) requester.args[3], ((Boolean) requester.args[4]).booleanValue(), (SendMessagesHelper.DelayedMessage) requester.args[5], null, null, ((Boolean) requester.args[6]).booleanValue());
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$onUpdateObjectReference$33(Requester requester) {
        getSendMessagesHelper().lambda$performSendMessageRequest$71((TLObject) requester.args[0], (MessageObject) requester.args[1], (String) requester.args[2], (SendMessagesHelper.DelayedMessage) requester.args[3], ((Boolean) requester.args[4]).booleanValue(), (SendMessagesHelper.DelayedMessage) requester.args[5], null, null, ((Boolean) requester.args[6]).booleanValue());
    }

    private void sendErrorToObject(final Object[] objArr, int i) {
        Object obj = objArr[0];
        if (obj instanceof TLRPC.TL_inputSingleMedia) {
            final TLRPC.TL_messages_sendMultiMedia tL_messages_sendMultiMedia = (TLRPC.TL_messages_sendMultiMedia) objArr[1];
            final Object[] objArr2 = this.multiMediaCache.get(tL_messages_sendMultiMedia);
            if (objArr2 != null) {
                this.multiMediaCache.remove(tL_messages_sendMultiMedia);
                AndroidUtilities.runOnUIThread(new Runnable() { // from class: org.telegram.messenger.FileRefController$$ExternalSyntheticLambda5
                    @Override // java.lang.Runnable
                    public final void run() {
                        this.f$0.lambda$sendErrorToObject$38(tL_messages_sendMultiMedia, objArr2);
                    }
                });
                return;
            }
            return;
        }
        if ((obj instanceof TLRPC.TL_inputMediaDocument) || (obj instanceof TLRPC.TL_inputMediaPhoto)) {
            Object obj2 = objArr[1];
            if (obj2 instanceof TLRPC.TL_messages_sendMedia) {
                final TLRPC.TL_messages_sendMedia tL_messages_sendMedia = (TLRPC.TL_messages_sendMedia) obj2;
                final Object[] objArr3 = this.multiMediaCache.get(tL_messages_sendMedia);
                if (objArr3 != null) {
                    this.multiMediaCache.remove(tL_messages_sendMedia);
                    AndroidUtilities.runOnUIThread(new Runnable() { // from class: org.telegram.messenger.FileRefController$$ExternalSyntheticLambda6
                        @Override // java.lang.Runnable
                        public final void run() {
                            this.f$0.lambda$sendErrorToObject$39(tL_messages_sendMedia, objArr3);
                        }
                    });
                    return;
                }
                return;
            }
        }
        if (((obj instanceof TLRPC.TL_messages_sendMedia) && !(((TLRPC.TL_messages_sendMedia) obj).media instanceof TLRPC.TL_inputMediaPaidMedia)) || (obj instanceof TLRPC.TL_messages_editMessage)) {
            AndroidUtilities.runOnUIThread(new Runnable() { // from class: org.telegram.messenger.FileRefController$$ExternalSyntheticLambda7
                @Override // java.lang.Runnable
                public final void run() {
                    this.f$0.lambda$sendErrorToObject$40(objArr);
                }
            });
            return;
        }
        if (obj instanceof TLRPC.TL_messages_saveGif) {
            return;
        }
        if (obj instanceof TLRPC.TL_messages_saveRecentSticker) {
            return;
        }
        if (obj instanceof TLRPC.TL_stickers_addStickerToSet) {
            return;
        }
        if (obj instanceof TLRPC.TL_messages_faveSticker) {
            return;
        }
        if (obj instanceof TLRPC.TL_messages_getAttachedStickers) {
            getConnectionsManager().sendRequest((TLRPC.TL_messages_getAttachedStickers) obj, (RequestDelegate) objArr[1]);
            return;
        }
        Object obj3 = objArr[1];
        if (obj3 instanceof FileLoadOperation) {
            FileLoadOperation fileLoadOperation = (FileLoadOperation) obj3;
            fileLoadOperation.requestingReference = false;
            FileLog.e("debug_loading: " + fileLoadOperation.getCacheFileFinal().getName() + " reference can't update: fail operation ");
            fileLoadOperation.onFail(false, 0);
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$sendErrorToObject$38(TLRPC.TL_messages_sendMultiMedia tL_messages_sendMultiMedia, Object[] objArr) {
        getSendMessagesHelper().lambda$performSendMessageRequestMulti$57(tL_messages_sendMultiMedia, (ArrayList) objArr[1], (ArrayList) objArr[2], null, (SendMessagesHelper.DelayedMessage) objArr[4], ((Boolean) objArr[5]).booleanValue());
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$sendErrorToObject$39(TLRPC.TL_messages_sendMedia tL_messages_sendMedia, Object[] objArr) {
        getSendMessagesHelper().lambda$performSendMessageRequestMulti$57(tL_messages_sendMedia, (ArrayList) objArr[1], (ArrayList) objArr[2], null, (SendMessagesHelper.DelayedMessage) objArr[4], ((Boolean) objArr[5]).booleanValue());
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$sendErrorToObject$40(Object[] objArr) {
        getSendMessagesHelper().lambda$performSendMessageRequest$71((TLObject) objArr[0], (MessageObject) objArr[1], (String) objArr[2], (SendMessagesHelper.DelayedMessage) objArr[3], ((Boolean) objArr[4]).booleanValue(), (SendMessagesHelper.DelayedMessage) objArr[5], null, null, ((Boolean) objArr[6]).booleanValue());
    }

    /* JADX WARN: Code duplicated, block: B:109:0x0209  */
    /* JADX WARN: Code duplicated, block: B:112:0x0216 A[LOOP:3: B:85:0x017c->B:112:0x0216, LOOP_END] */
    /* JADX WARN: Code duplicated, block: B:114:0x0229  */
    /* JADX WARN: Code duplicated, block: B:116:0x0234  */
    /* JADX WARN: Code duplicated, block: B:118:0x0238  */
    /* JADX WARN: Code duplicated, block: B:119:0x0245  */
    /* JADX WARN: Code duplicated, block: B:121:0x0249  */
    /* JADX WARN: Code duplicated, block: B:123:0x0258  */
    /* JADX WARN: Code duplicated, block: B:124:0x0267  */
    /* JADX WARN: Code duplicated, block: B:126:0x026d  */
    /* JADX WARN: Code duplicated, block: B:127:0x0276  */
    /* JADX WARN: Code duplicated, block: B:129:0x027a  */
    /* JADX WARN: Code duplicated, block: B:135:0x0294  */
    /* JADX WARN: Code duplicated, block: B:142:0x02ad A[EDGE_INSN: B:142:0x02ad->B:146:0x02c9 BREAK  A[LOOP:2: B:81:0x0167->B:143:0x02ba]] */
    /* JADX WARN: Code duplicated, block: B:143:0x02ba A[LOOP:2: B:81:0x0167->B:143:0x02ba, LOOP_END] */
    /* JADX WARN: Code duplicated, block: B:147:0x02cb  */
    /* JADX WARN: Code duplicated, block: B:149:0x02e3  */
    /* JADX WARN: Code duplicated, block: B:150:0x02e9  */
    /* JADX WARN: Code duplicated, block: B:152:0x02f1  */
    /* JADX WARN: Code duplicated, block: B:154:0x02f9  */
    /* JADX WARN: Code duplicated, block: B:156:0x02ff  */
    /* JADX WARN: Code duplicated, block: B:158:0x030b  */
    /* JADX WARN: Code duplicated, block: B:161:0x0320 A[LOOP:4: B:157:0x0309->B:161:0x0320, LOOP_END] */
    /* JADX WARN: Code duplicated, block: B:165:0x032a  */
    /* JADX WARN: Code duplicated, block: B:167:0x0330  */
    /* JADX WARN: Code duplicated, block: B:169:0x034f  */
    /* JADX WARN: Code duplicated, block: B:190:0x03ca A[LOOP:5: B:168:0x034d->B:190:0x03ca, LOOP_END] */
    /* JADX WARN: Code duplicated, block: B:191:0x03cc  */
    /* JADX WARN: Code duplicated, block: B:193:0x03d0  */
    /* JADX WARN: Code duplicated, block: B:195:0x03ec  */
    /* JADX WARN: Code duplicated, block: B:197:0x0403  */
    /* JADX WARN: Code duplicated, block: B:198:0x040a  */
    /* JADX WARN: Code duplicated, block: B:199:0x0416 A[PHI: r4 r5 r6
  0x0416: PHI (r4v74 byte[]) = (r4v58 byte[]), (r4v2 byte[]) binds: [B:240:0x0507, B:194:0x03ea] A[DONT_GENERATE, DONT_INLINE]
  0x0416: PHI (r5v10 boolean[]) = (r5v5 boolean[]), (r5v2 boolean[]) binds: [B:240:0x0507, B:194:0x03ea] A[DONT_GENERATE, DONT_INLINE]
  0x0416: PHI (r6v6 org.telegram.tgnet.TLRPC$InputFileLocation[]) = (r6v4 org.telegram.tgnet.TLRPC$InputFileLocation[]), (r6v3 org.telegram.tgnet.TLRPC$InputFileLocation[]) binds: [B:240:0x0507, B:194:0x03ea] A[DONT_GENERATE, DONT_INLINE]] */
    /* JADX WARN: Code duplicated, block: B:200:0x041a  */
    /* JADX WARN: Code duplicated, block: B:202:0x0420  */
    /* JADX WARN: Code duplicated, block: B:204:0x042e  */
    /* JADX WARN: Code duplicated, block: B:207:0x0444 A[LOOP:6: B:203:0x042c->B:207:0x0444, LOOP_END] */
    /* JADX WARN: Code duplicated, block: B:209:0x044b  */
    /* JADX WARN: Code duplicated, block: B:212:0x0461  */
    /* JADX WARN: Code duplicated, block: B:215:0x0477 A[LOOP:7: B:210:0x045b->B:215:0x0477, LOOP_END] */
    /* JADX WARN: Code duplicated, block: B:218:0x049e  */
    /* JADX WARN: Code duplicated, block: B:220:0x04a7  */
    /* JADX WARN: Code duplicated, block: B:222:0x04ac  */
    /* JADX WARN: Code duplicated, block: B:233:0x04ce A[Catch: Exception -> 0x04f0, TRY_LEAVE, TryCatch #0 {Exception -> 0x04f0, blocks: (B:231:0x04ca, B:233:0x04ce), top: B:411:0x04ca }] */
    /* JADX WARN: Code duplicated, block: B:238:0x04f7  */
    /* JADX WARN: Code duplicated, block: B:239:0x0506  */
    /* JADX WARN: Code duplicated, block: B:241:0x0509  */
    /* JADX WARN: Code duplicated, block: B:242:0x0519  */
    /* JADX WARN: Code duplicated, block: B:244:0x0520  */
    /* JADX WARN: Code duplicated, block: B:245:0x0542  */
    /* JADX WARN: Code duplicated, block: B:247:0x0546  */
    /* JADX WARN: Code duplicated, block: B:248:0x0553  */
    /* JADX WARN: Code duplicated, block: B:250:0x0557  */
    /* JADX WARN: Code duplicated, block: B:252:0x0563  */
    /* JADX WARN: Code duplicated, block: B:255:0x0579 A[LOOP:8: B:251:0x0561->B:255:0x0579, LOOP_END] */
    /* JADX WARN: Code duplicated, block: B:259:0x058e  */
    /* JADX WARN: Code duplicated, block: B:261:0x0592  */
    /* JADX WARN: Code duplicated, block: B:265:0x05b8  */
    /* JADX WARN: Code duplicated, block: B:267:0x05bc  */
    /* JADX WARN: Code duplicated, block: B:271:0x05da  */
    /* JADX WARN: Code duplicated, block: B:273:0x05e0  */
    /* JADX WARN: Code duplicated, block: B:275:0x05eb  */
    /* JADX WARN: Code duplicated, block: B:277:0x05f4  */
    /* JADX WARN: Code duplicated, block: B:279:0x05fe  */
    /* JADX WARN: Code duplicated, block: B:283:0x0628  */
    /* JADX WARN: Code duplicated, block: B:284:0x062b  */
    /* JADX WARN: Code duplicated, block: B:286:0x0631  */
    /* JADX WARN: Code duplicated, block: B:293:0x065d A[LOOP:9: B:276:0x05f2->B:293:0x065d, LOOP_END] */
    /* JADX WARN: Code duplicated, block: B:294:0x0662  */
    /* JADX WARN: Code duplicated, block: B:296:0x0666  */
    /* JADX WARN: Code duplicated, block: B:298:0x0671  */
    /* JADX WARN: Code duplicated, block: B:300:0x067a  */
    /* JADX WARN: Code duplicated, block: B:303:0x068e  */
    /* JADX WARN: Code duplicated, block: B:304:0x06a8  */
    /* JADX WARN: Code duplicated, block: B:306:0x06ac A[LOOP:10: B:299:0x0678->B:306:0x06ac, LOOP_END] */
    /* JADX WARN: Code duplicated, block: B:307:0x06b1 A[EDGE_INSN: B:307:0x06b1->B:164:0x0327 BREAK  A[LOOP:10: B:299:0x0678->B:306:0x06ac], PHI: r4
  0x06b1: PHI (r4v32 byte[]) = (r4v2 byte[]), (r4v33 byte[]) binds: [B:297:0x066f, B:447:0x06b1] A[DONT_GENERATE, DONT_INLINE]] */
    /* JADX WARN: Code duplicated, block: B:308:0x06b4  */
    /* JADX WARN: Code duplicated, block: B:310:0x06b9  */
    /* JADX WARN: Code duplicated, block: B:312:0x06c5  */
    /* JADX WARN: Code duplicated, block: B:315:0x06d9 A[LOOP:11: B:311:0x06c3->B:315:0x06d9, LOOP_END] */
    /* JADX WARN: Code duplicated, block: B:317:0x06e0  */
    /* JADX WARN: Code duplicated, block: B:318:0x06f5  */
    /* JADX WARN: Code duplicated, block: B:320:0x06f9  */
    /* JADX WARN: Code duplicated, block: B:322:0x06fe  */
    /* JADX WARN: Code duplicated, block: B:324:0x0707  */
    /* JADX WARN: Code duplicated, block: B:327:0x071e A[LOOP:12: B:323:0x0705->B:327:0x071e, LOOP_END] */
    /* JADX WARN: Code duplicated, block: B:328:0x0721 A[EDGE_INSN: B:328:0x0721->B:329:0x0723 BREAK  A[LOOP:12: B:323:0x0705->B:327:0x071e], PHI: r4
  0x0721: PHI (r4v23 byte[]) = (r4v2 byte[]), (r4v25 byte[]) binds: [B:321:0x06fc, B:451:0x0721] A[DONT_GENERATE, DONT_INLINE]] */
    /* JADX WARN: Code duplicated, block: B:330:0x0725  */
    /* JADX WARN: Code duplicated, block: B:331:0x072f  */
    /* JADX WARN: Code duplicated, block: B:333:0x0735  */
    /* JADX WARN: Code duplicated, block: B:335:0x0741  */
    /* JADX WARN: Code duplicated, block: B:338:0x0755 A[LOOP:13: B:334:0x073f->B:338:0x0755, LOOP_END] */
    /* JADX WARN: Code duplicated, block: B:340:0x075c  */
    /* JADX WARN: Code duplicated, block: B:341:0x0771  */
    /* JADX WARN: Code duplicated, block: B:343:0x0775  */
    /* JADX WARN: Code duplicated, block: B:345:0x0781  */
    /* JADX WARN: Code duplicated, block: B:348:0x0798 A[LOOP:14: B:344:0x077f->B:348:0x0798, LOOP_END] */
    /* JADX WARN: Code duplicated, block: B:351:0x079f  */
    /* JADX WARN: Code duplicated, block: B:352:0x07b4  */
    /* JADX WARN: Code duplicated, block: B:354:0x07ba  */
    /* JADX WARN: Code duplicated, block: B:356:0x07c6  */
    /* JADX WARN: Code duplicated, block: B:359:0x07da A[LOOP:15: B:355:0x07c4->B:359:0x07da, LOOP_END] */
    /* JADX WARN: Code duplicated, block: B:360:0x07dd  */
    /* JADX WARN: Code duplicated, block: B:362:0x07e1  */
    /* JADX WARN: Code duplicated, block: B:378:0x0830  */
    /* JADX WARN: Code duplicated, block: B:381:0x083d  */
    /* JADX WARN: Code duplicated, block: B:383:0x084b  */
    /* JADX WARN: Code duplicated, block: B:385:0x084f  */
    /* JADX WARN: Code duplicated, block: B:386:0x0885  */
    /* JADX WARN: Code duplicated, block: B:395:0x08e4  */
    /* JADX WARN: Code duplicated, block: B:397:0x08e8  */
    /* JADX WARN: Code duplicated, block: B:399:0x08ed  */
    /* JADX WARN: Code duplicated, block: B:402:0x08f5  */
    /* JADX WARN: Code duplicated, block: B:403:0x08fa  */
    /* JADX WARN: Code duplicated, block: B:404:0x08fc  */
    /* JADX WARN: Code duplicated, block: B:409:0x091b  */
    /* JADX WARN: Code duplicated, block: B:41:0x0083  */
    /* JADX WARN: Code duplicated, block: B:425:0x02ab A[SYNTHETIC] */
    /* JADX WARN: Code duplicated, block: B:426:0x02c4 A[SYNTHETIC] */
    /* JADX WARN: Code duplicated, block: B:427:0x0220 A[SYNTHETIC] */
    /* JADX WARN: Code duplicated, block: B:428:0x02a9 A[EDGE_INSN: B:428:0x02a9->B:140:0x02a9 BREAK  A[LOOP:3: B:85:0x017c->B:112:0x0216], SYNTHETIC] */
    /* JADX WARN: Code duplicated, block: B:430:0x0324 A[EDGE_INSN: B:430:0x0324->B:162:0x0324 BREAK  A[LOOP:4: B:157:0x0309->B:161:0x0320], SYNTHETIC] */
    /* JADX WARN: Code duplicated, block: B:438:0x0324 A[EDGE_INSN: B:438:0x0324->B:162:0x0324 BREAK  A[LOOP:5: B:168:0x034d->B:190:0x03ca], SYNTHETIC] */
    /* JADX WARN: Code duplicated, block: B:440:0x0449 A[EDGE_INSN: B:440:0x0449->B:208:0x0449 BREAK  A[LOOP:6: B:203:0x042c->B:207:0x0444], SYNTHETIC] */
    /* JADX WARN: Code duplicated, block: B:441:0x047e A[SYNTHETIC] */
    /* JADX WARN: Code duplicated, block: B:442:0x0473 A[SYNTHETIC] */
    /* JADX WARN: Code duplicated, block: B:444:0x057e A[EDGE_INSN: B:444:0x057e->B:256:0x057e BREAK  A[LOOP:8: B:251:0x0561->B:255:0x0579], SYNTHETIC] */
    /* JADX WARN: Code duplicated, block: B:445:0x02f6 A[EDGE_INSN: B:445:0x02f6->B:153:0x02f6 BREAK  A[LOOP:9: B:276:0x05f2->B:293:0x065d], SYNTHETIC] */
    /* JADX WARN: Code duplicated, block: B:446:0x02f6 A[EDGE_INSN: B:446:0x02f6->B:153:0x02f6 BREAK  A[LOOP:9: B:276:0x05f2->B:293:0x065d], SYNTHETIC] */
    /* JADX WARN: Code duplicated, block: B:447:0x06b1 A[SYNTHETIC] */
    /* JADX WARN: Code duplicated, block: B:448:0x068c A[SYNTHETIC] */
    /* JADX WARN: Code duplicated, block: B:44:0x0093 A[RETURN] */
    /* JADX WARN: Code duplicated, block: B:450:0x06de A[EDGE_INSN: B:450:0x06de->B:316:0x06de BREAK  A[LOOP:11: B:311:0x06c3->B:315:0x06d9], SYNTHETIC] */
    /* JADX WARN: Code duplicated, block: B:451:0x0721 A[SYNTHETIC] */
    /* JADX WARN: Code duplicated, block: B:452:0x0723 A[EDGE_INSN: B:452:0x0723->B:329:0x0723 BREAK  A[LOOP:12: B:323:0x0705->B:327:0x071e], SYNTHETIC] */
    /* JADX WARN: Code duplicated, block: B:453:0x075a A[EDGE_INSN: B:453:0x075a->B:339:0x075a BREAK  A[LOOP:13: B:334:0x073f->B:338:0x0755], SYNTHETIC] */
    /* JADX WARN: Code duplicated, block: B:455:0x079b A[SYNTHETIC] */
    /* JADX WARN: Code duplicated, block: B:456:0x079d A[EDGE_INSN: B:456:0x079d->B:350:0x079d BREAK  A[LOOP:14: B:344:0x077f->B:348:0x0798], SYNTHETIC] */
    /* JADX WARN: Code duplicated, block: B:457:0x0327 A[EDGE_INSN: B:457:0x0327->B:164:0x0327 BREAK  A[LOOP:15: B:355:0x07c4->B:359:0x07da], SYNTHETIC] */
    /* JADX WARN: Code duplicated, block: B:458:0x0327 A[EDGE_INSN: B:458:0x0327->B:164:0x0327 BREAK  A[LOOP:15: B:355:0x07c4->B:359:0x07da], SYNTHETIC] */
    /* JADX WARN: Code duplicated, block: B:45:0x0094  */
    /* JADX WARN: Code duplicated, block: B:47:0x009e  */
    /* JADX WARN: Code duplicated, block: B:49:0x00aa  */
    /* JADX WARN: Code duplicated, block: B:51:0x00b6  */
    /* JADX WARN: Code duplicated, block: B:65:0x0119  */
    /* JADX WARN: Code duplicated, block: B:68:0x0125  */
    /* JADX WARN: Code duplicated, block: B:70:0x0130  */
    /* JADX WARN: Code duplicated, block: B:71:0x013b  */
    /* JADX WARN: Code duplicated, block: B:73:0x013f  */
    /* JADX WARN: Code duplicated, block: B:76:0x014f  */
    /* JADX WARN: Code duplicated, block: B:78:0x0155  */
    /* JADX WARN: Code duplicated, block: B:80:0x0160  */
    /* JADX WARN: Code duplicated, block: B:82:0x0169  */
    /* JADX WARN: Code duplicated, block: B:84:0x0177  */
    /* JADX WARN: Code duplicated, block: B:87:0x0184  */
    /* JADX WARN: Multi-variable type inference failed */
    /* JADX WARN: Type inference failed for: r10v30 */
    /* JADX WARN: Type inference failed for: r10v42 */
    /* JADX WARN: Type inference failed for: r10v43 */
    /* JADX WARN: Type inference failed for: r10v44 */
    /* JADX WARN: Type inference failed for: r10v45 */
    /* JADX WARN: Type inference failed for: r10v46 */
    /* JADX WARN: Type inference failed for: r10v48 */
    /* JADX WARN: Type inference failed for: r10v49 */
    /* JADX WARN: Type inference failed for: r11v0 */
    /* JADX WARN: Type inference failed for: r11v28 */
    private boolean onRequestComplete(String str, String str2, TLObject tLObject, TLRPC.TL_error tL_error, boolean z, boolean z2) {
        String str3;
        String str4;
        int i;
        TLObject tLObject2;
        boolean z3;
        ArrayList<Requester> arrayList;
        int size;
        boolean[] zArr;
        TLRPC.InputFileLocation[] inputFileLocationArr;
        byte[] fileReference;
        int i2;
        Requester requester;
        TLRPC.InputFileLocation[] inputFileLocationArr2;
        boolean[] zArr2;
        Requester requester2;
        boolean[] zArr3;
        boolean z4;
        ArrayList<Requester> arrayList2;
        long j;
        TLRPC.InputFileLocation inputFileLocation;
        TL_stories.TL_stories_stories tL_stories_stories;
        TL_stories.StoryItem storyItem;
        Object obj;
        TL_stories.StoryItem storyItem2;
        TLRPC.User user;
        TLRPC.MessageMedia messageMedia;
        TLRPC.MessageMedia messageMedia2;
        TLRPC.Document document;
        TLRPC.Photo photo;
        TLRPC.Photo photo2;
        TLRPC.photos_Photos photos_photos;
        int size2;
        int i3;
        TLRPC.TL_messages_favedStickers tL_messages_favedStickers;
        int size3;
        int i4;
        TLRPC.TL_messages_recentStickers tL_messages_recentStickers;
        int size4;
        int i5;
        final TLRPC.TL_messages_stickerSet tL_messages_stickerSet;
        int size5;
        int i6;
        TLRPC.TL_messages_savedGifs tL_messages_savedGifs;
        int size6;
        int i7;
        TLRPC.TL_messages_chats tL_messages_chats;
        int size7;
        int i8;
        final TLRPC.Chat chat;
        byte[] fileReference2;
        Vector vector;
        int size8;
        int i9;
        Object obj2;
        int i10;
        final TLRPC.Chat chat2;
        final TLRPC.TL_theme tL_theme;
        TLRPC.TL_wallPaper tL_wallPaper;
        TL_account.TL_wallPapers tL_wallPapers;
        int size9;
        int i11;
        TLRPC.TL_help_appUpdate tL_help_appUpdate;
        TLRPC.Document document2;
        TLRPC.TL_attachMenuBot tL_attachMenuBot;
        ArrayList arrayList3;
        int size10;
        int i12;
        byte[] bArr;
        boolean[] zArr4;
        ArrayList arrayList4;
        int i13;
        long j2;
        int i14;
        TLRPC.UserFull userFull;
        TL_bots.BotInfo botInfo;
        ?? r10;
        ArrayList arrayList5;
        int size11;
        int i15;
        int i16;
        TLRPC.TL_availableReaction tL_availableReaction;
        byte[] fileReference3;
        ArrayList arrayList6;
        int size12;
        int i17;
        int i18;
        TLRPC.messages_Messages messages_messages;
        int size13;
        int i19;
        int i20;
        TLRPC.Message message;
        TLRPC.MessageMedia messageMedia3;
        int i21;
        TLRPC.Message message2;
        TLRPC.MessageAction messageAction;
        byte[] fileReference4;
        TLRPC.Document document3;
        TLRPC.TL_game tL_game;
        TLRPC.Photo photo3;
        TLRPC.WebPage webPage;
        TLRPC.Photo photo4;
        TLRPC.TL_messageMediaPaidMedia tL_messageMediaPaidMedia;
        int i22;
        TLRPC.MessageExtendedMedia messageExtendedMedia;
        int i23;
        int i24;
        TLRPC.MessageMedia messageMedia4;
        TLRPC.MessageMedia messageMedia5;
        TLRPC.Photo photo5;
        TLRPC.InputFileLocation inputFileLocation2;
        TLRPC.MessageMedia messageMedia6;
        TLRPC.Document document4;
        TLRPC.Photo photo6;
        ArrayList<Requester> arrayList7;
        TLObject tLObject3;
        final FileRefController fileRefController = this;
        TLObject tLObject4 = tLObject;
        boolean z5 = tLObject4 instanceof TLRPC.TL_help_premiumPromo;
        if (z5) {
            str4 = "premium_promo";
        } else if (tLObject4 instanceof TL_account.TL_wallPapers) {
            str4 = "wallpaper";
        } else if (tLObject4 instanceof TLRPC.TL_messages_savedGifs) {
            str4 = "gif";
        } else if (tLObject4 instanceof TLRPC.TL_messages_recentStickers) {
            str4 = "recent";
        } else {
            if (tLObject4 instanceof TLRPC.TL_messages_favedStickers) {
                str4 = "fav";
            } else {
                str3 = str2;
            }
            i = 1;
            if (str2 != null || (arrayList7 = fileRefController.parentRequester.get(str2)) == null) {
                tLObject2 = tLObject4;
                z3 = false;
            } else {
                int size14 = arrayList7.size();
                int i25 = 0;
                z3 = false;
                while (i25 < size14) {
                    Requester requester3 = arrayList7.get(i25);
                    if (requester3.completed) {
                        tLObject3 = tLObject4;
                    } else {
                        tLObject3 = tLObject4;
                        if (fileRefController.onRequestComplete(requester3.locationKey, null, tLObject4, tL_error, z && !z3, z2)) {
                            z3 = true;
                        }
                    }
                    i25++;
                    tLObject4 = tLObject3;
                }
                tLObject2 = tLObject4;
                if (z3) {
                    fileRefController.putReponseToCache(str3, tLObject2);
                }
                fileRefController.parentRequester.remove(str2);
            }
            arrayList = fileRefController.locationRequester.get(str);
            if (arrayList == null) {
                return z3;
            }
            size = arrayList.size();
            zArr = null;
            inputFileLocationArr = null;
            fileReference = null;
            i2 = 0;
            while (i2 < size) {
                requester = arrayList.get(i2);
                if (requester.completed) {
                    z4 = z5;
                    arrayList2 = arrayList;
                    r10 = i;
                } else {
                    if (tL_error != null && BuildVars.LOGS_ENABLED && requester.args.length > i && (requester.args[i] instanceof FileLoadOperation)) {
                        FileLog.e("debug_loading: " + ((FileLoadOperation) requester.args[i]).getCacheFileFinal().getName() + " can't update file reference: " + tL_error.code + " " + tL_error.text);
                    }
                    if (!(requester.location instanceof TLRPC.TL_inputFileLocation) || (requester.location instanceof TLRPC.TL_inputPeerPhotoFileLocation)) {
                        inputFileLocationArr = new TLRPC.InputFileLocation[i];
                        zArr = new boolean[i];
                    }
                    inputFileLocationArr2 = inputFileLocationArr;
                    zArr2 = zArr;
                    requester.completed = i;
                    if (tLObject2 instanceof StoriesController.BotPreview) {
                        messageMedia6 = ((StoriesController.BotPreview) tLObject2).media;
                        requester2 = requester;
                        zArr3 = zArr2;
                        document4 = messageMedia6.document;
                        if (document4 != null) {
                            fileReference = fileRefController.getFileReference(document4, messageMedia6.alt_documents, requester2.location, zArr3, inputFileLocationArr2);
                        } else {
                            photo6 = messageMedia6.photo;
                            if (photo6 != null) {
                                fileReference = fileRefController.getFileReference(photo6, requester2.location, zArr3, inputFileLocationArr2);
                            }
                        }
                        inputFileLocationArr = inputFileLocationArr2;
                        z4 = z5;
                        arrayList2 = arrayList;
                    } else {
                        requester2 = requester;
                        zArr3 = zArr2;
                        if (tLObject2 instanceof TLRPC.messages_Messages) {
                            messages_messages = (TLRPC.messages_Messages) tLObject2;
                            if (!messages_messages.messages.isEmpty()) {
                                size13 = messages_messages.messages.size();
                                i19 = 0;
                                while (true) {
                                    if (i19 < size13) {
                                        message = (TLRPC.Message) messages_messages.messages.get(i19);
                                        messageMedia3 = message.media;
                                        if (messageMedia3 instanceof TLRPC.TL_messageMediaPaidMedia) {
                                            tL_messageMediaPaidMedia = (TLRPC.TL_messageMediaPaidMedia) messageMedia3;
                                            i21 = i19;
                                            i22 = 0;
                                            while (true) {
                                                if (i22 < tL_messageMediaPaidMedia.extended_media.size()) {
                                                    fileRefController = this;
                                                    z4 = z5;
                                                    arrayList2 = arrayList;
                                                    message2 = message;
                                                    break;
                                                }
                                                messageExtendedMedia = tL_messageMediaPaidMedia.extended_media.get(i22);
                                                i23 = i22;
                                                if ((messageExtendedMedia instanceof TLRPC.TL_messageExtendedMedia) || (messageMedia4 = ((TLRPC.TL_messageExtendedMedia) messageExtendedMedia).media) == null) {
                                                    fileRefController = this;
                                                    arrayList2 = arrayList;
                                                    i24 = i23;
                                                    z4 = z5;
                                                    message2 = message;
                                                } else {
                                                    TLRPC.Document document5 = messageMedia4.document;
                                                    if (document5 != null) {
                                                        z4 = z5;
                                                        message2 = message;
                                                        i24 = i23;
                                                        arrayList2 = arrayList;
                                                        messageMedia5 = messageMedia4;
                                                        fileRefController = this;
                                                        fileReference = fileRefController.getFileReference(document5, messageMedia4.alt_documents, requester2.location, zArr3, inputFileLocationArr2);
                                                    } else {
                                                        arrayList2 = arrayList;
                                                        i24 = i23;
                                                        messageMedia5 = messageMedia4;
                                                        z4 = z5;
                                                        message2 = message;
                                                        TLRPC.TL_game tL_game2 = messageMedia5.game;
                                                        if (tL_game2 != null) {
                                                            fileRefController = this;
                                                            fileReference = fileRefController.getFileReference(tL_game2.document, null, requester2.location, zArr3, inputFileLocationArr2);
                                                            if (fileReference == null) {
                                                                fileReference = fileRefController.getFileReference(messageMedia5.game.photo, requester2.location, zArr3, inputFileLocationArr2);
                                                            }
                                                        } else {
                                                            fileRefController = this;
                                                            TLRPC.Photo photo7 = messageMedia5.photo;
                                                            if (photo7 != null) {
                                                                fileReference = fileRefController.getFileReference(photo7, requester2.location, zArr3, inputFileLocationArr2);
                                                            } else {
                                                                TLRPC.WebPage webPage2 = messageMedia5.webpage;
                                                                if (webPage2 != null) {
                                                                    fileReference = fileRefController.getFileReference(webPage2, requester2.location, zArr3, inputFileLocationArr2);
                                                                }
                                                            }
                                                        }
                                                    }
                                                    if (fileReference == null && (photo5 = messageMedia5.video_cover) != null) {
                                                        fileReference = fileRefController.getFileReference(photo5, requester2.location, zArr3, inputFileLocationArr2);
                                                    }
                                                }
                                                if (fileReference != null) {
                                                    break;
                                                }
                                                i22 = i24 + 1;
                                                message = message2;
                                                z5 = z4;
                                                arrayList = arrayList2;
                                            }
                                        } else {
                                            fileRefController = this;
                                            i21 = i19;
                                            z4 = z5;
                                            arrayList2 = arrayList;
                                            message2 = message;
                                            if (messageMedia3 != null) {
                                                document3 = messageMedia3.document;
                                                if (document3 != null) {
                                                    fileReference = fileRefController.getFileReference(document3, messageMedia3.alt_documents, requester2.location, zArr3, inputFileLocationArr2);
                                                    fileRefController = this;
                                                } else {
                                                    tL_game = messageMedia3.game;
                                                    if (tL_game != null) {
                                                        fileRefController = this;
                                                        fileReference = fileRefController.getFileReference(tL_game.document, null, requester2.location, zArr3, inputFileLocationArr2);
                                                        if (fileReference == null) {
                                                            fileReference = fileRefController.getFileReference(message2.media.game.photo, requester2.location, zArr3, inputFileLocationArr2);
                                                        }
                                                    } else {
                                                        fileRefController = this;
                                                        photo3 = messageMedia3.photo;
                                                        if (photo3 != null) {
                                                            fileReference = fileRefController.getFileReference(photo3, requester2.location, zArr3, inputFileLocationArr2);
                                                        } else {
                                                            webPage = messageMedia3.webpage;
                                                            if (webPage != null) {
                                                                fileReference = fileRefController.getFileReference(webPage, requester2.location, zArr3, inputFileLocationArr2);
                                                            }
                                                        }
                                                    }
                                                }
                                                if (fileReference == null || (photo4 = message2.media.video_cover) == null) {
                                                    break;
                                                }
                                                fileReference4 = fileRefController.getFileReference(photo4, requester2.location, zArr3, inputFileLocationArr2);
                                                fileReference = fileReference4;
                                            } else {
                                                messageAction = message2.action;
                                                if (!(messageAction instanceof TLRPC.TL_messageActionChatEditPhoto) || (messageAction instanceof TLRPC.TL_messageActionSuggestProfilePhoto)) {
                                                    fileReference4 = fileRefController.getFileReference(messageAction.photo, requester2.location, zArr3, inputFileLocationArr2);
                                                    fileReference = fileReference4;
                                                }
                                            }
                                        }
                                        if (fileReference != null) {
                                            if (z) {
                                                i20 = 0;
                                                fileRefController.getMessagesStorage().replaceMessageIfExists(message2, messages_messages.users, messages_messages.chats, false);
                                                break;
                                            }
                                        } else {
                                            i19 = i21 + 1;
                                            z5 = z4;
                                            arrayList = arrayList2;
                                        }
                                    } else {
                                        z4 = z5;
                                        arrayList2 = arrayList;
                                    }
                                    i20 = 0;
                                    break;
                                }
                                if (fileReference == null) {
                                    fileRefController.getMessagesStorage().replaceMessageIfExists((TLRPC.Message) messages_messages.messages.get(i20), messages_messages.users, messages_messages.chats, true);
                                    if (BuildVars.DEBUG_VERSION) {
                                        FileLog.d("file ref not found in messages, replacing message");
                                    }
                                }
                            } else {
                                z4 = z5;
                                arrayList2 = arrayList;
                                if (BuildVars.DEBUG_VERSION) {
                                    FileLog.d("empty messages, file ref not found");
                                }
                            }
                        } else {
                            z4 = z5;
                            arrayList2 = arrayList;
                            if (z4) {
                                arrayList6 = ((TLRPC.TL_help_premiumPromo) tLObject2).videos;
                                size12 = arrayList6.size();
                                i17 = 0;
                                while (i17 < size12) {
                                    i18 = i17 + 1;
                                    fileReference = fileRefController.getFileReference((TLRPC.Document) arrayList6.get(i17), null, requester2.location, zArr3, inputFileLocationArr2);
                                    if (fileReference != null) {
                                        break;
                                    }
                                    fileRefController = this;
                                    i17 = i18;
                                }
                            } else {
                                j = 1000;
                                if (tLObject2 instanceof TLRPC.TL_messages_availableReactions) {
                                    TLRPC.TL_messages_availableReactions tL_messages_availableReactions = (TLRPC.TL_messages_availableReactions) tLObject2;
                                    getMediaDataController().processLoadedReactions(tL_messages_availableReactions.reactions, tL_messages_availableReactions.hash, (int) (System.currentTimeMillis() / 1000), false);
                                    arrayList5 = tL_messages_availableReactions.reactions;
                                    size11 = arrayList5.size();
                                    i15 = 0;
                                    while (i15 < size11) {
                                        i16 = i15 + 1;
                                        tL_availableReaction = (TLRPC.TL_availableReaction) arrayList5.get(i15);
                                        fileReference3 = getFileReference(tL_availableReaction.static_icon, null, requester2.location, zArr3, inputFileLocationArr2);
                                        if (fileReference3 != null || (fileReference3 = getFileReference(tL_availableReaction.appear_animation, null, requester2.location, zArr3, inputFileLocationArr2)) != null || (fileReference3 = getFileReference(tL_availableReaction.select_animation, null, requester2.location, zArr3, inputFileLocationArr2)) != null || (fileReference3 = getFileReference(tL_availableReaction.activate_animation, null, requester2.location, zArr3, inputFileLocationArr2)) != null || (fileReference3 = getFileReference(tL_availableReaction.effect_animation, null, requester2.location, zArr3, inputFileLocationArr2)) != null || (fileReference3 = getFileReference(tL_availableReaction.around_animation, null, requester2.location, zArr3, inputFileLocationArr2)) != null) {
                                            fileReference = fileReference3;
                                            break;
                                        }
                                        fileReference = getFileReference(tL_availableReaction.center_icon, null, requester2.location, zArr3, inputFileLocationArr2);
                                        if (fileReference != null) {
                                            break;
                                        }
                                        i15 = i16;
                                    }
                                } else if (tLObject2 instanceof TLRPC.TL_users_userFull) {
                                    TLRPC.TL_users_userFull tL_users_userFull = (TLRPC.TL_users_userFull) tLObject2;
                                    getMessagesController().putUsers(tL_users_userFull.users, false);
                                    getMessagesController().putChats(tL_users_userFull.chats, false);
                                    userFull = tL_users_userFull.full_user;
                                    botInfo = userFull.bot_info;
                                    if (botInfo != null) {
                                        getMessagesStorage().updateUserInfo(userFull, true);
                                        fileRefController = this;
                                        fileReference = fileRefController.getFileReference(botInfo.description_document, null, requester2.location, zArr3, inputFileLocationArr2);
                                        if (fileReference != null) {
                                            zArr = zArr3;
                                            inputFileLocationArr = inputFileLocationArr2;
                                            r10 = 1;
                                        } else {
                                            fileReference = fileRefController.getFileReference(botInfo.description_photo, requester2.location, zArr3, inputFileLocationArr2);
                                        }
                                    } else {
                                        zArr3 = zArr3;
                                        zArr3 = zArr3;
                                        fileRefController = this;
                                    }
                                } else if (tLObject2 instanceof TLRPC.TL_attachMenuBotsBot) {
                                    tL_attachMenuBot = ((TLRPC.TL_attachMenuBotsBot) tLObject2).bot;
                                    arrayList3 = tL_attachMenuBot.icons;
                                    size10 = arrayList3.size();
                                    i12 = 0;
                                    while (i12 < size10) {
                                        i14 = i12 + 1;
                                        fileReference = getFileReference(((TLRPC.TL_attachMenuBotIcon) arrayList3.get(i12)).icon, null, requester2.location, zArr3, inputFileLocationArr2);
                                        if (fileReference != null) {
                                            break;
                                        }
                                        i12 = i14;
                                    }
                                    if (z) {
                                        TLRPC.TL_attachMenuBots attachMenuBots = getMediaDataController().getAttachMenuBots();
                                        arrayList4 = new ArrayList(attachMenuBots.bots);
                                        i13 = 0;
                                        while (true) {
                                            if (i13 < arrayList4.size()) {
                                                bArr = fileReference;
                                                zArr4 = zArr3;
                                                j2 = j;
                                                break;
                                            }
                                            j2 = j;
                                            bArr = fileReference;
                                            zArr4 = zArr3;
                                            if (((TLRPC.TL_attachMenuBot) arrayList4.get(i13)).bot_id == tL_attachMenuBot.bot_id) {
                                                arrayList4.set(i13, tL_attachMenuBot);
                                                break;
                                            }
                                            i13++;
                                            zArr3 = zArr4;
                                            fileReference = bArr;
                                            j = j2;
                                        }
                                        attachMenuBots.bots = arrayList4;
                                        getMediaDataController().processLoadedMenuBots(attachMenuBots, attachMenuBots.hash, (int) (System.currentTimeMillis() / j2), false);
                                    } else {
                                        bArr = fileReference;
                                        zArr4 = zArr3;
                                    }
                                    fileRefController = this;
                                    inputFileLocationArr = inputFileLocationArr2;
                                    zArr3 = zArr4;
                                    fileReference = bArr;
                                } else if (tLObject2 instanceof TLRPC.TL_help_appUpdate) {
                                    tL_help_appUpdate = (TLRPC.TL_help_appUpdate) tLObject2;
                                    try {
                                        SharedConfig.pendingAppUpdate = tL_help_appUpdate;
                                        SharedConfig.saveConfig();
                                    } catch (Exception e) {
                                        FileLog.e(e);
                                    }
                                    try {
                                        NotificationCenter.getGlobalInstance().lambda$postNotificationNameOnUIThread$1(NotificationCenter.appUpdateAvailable, new Object[0]);
                                    } catch (Exception e2) {
                                        FileLog.e(e2);
                                    }
                                    try {
                                        document2 = tL_help_appUpdate.document;
                                        if (document2 != null) {
                                            fileReference = document2.file_reference;
                                            TLRPC.TL_inputDocumentFileLocation tL_inputDocumentFileLocation = new TLRPC.TL_inputDocumentFileLocation();
                                            TLRPC.Document document6 = tL_help_appUpdate.document;
                                            tL_inputDocumentFileLocation.id = document6.id;
                                            tL_inputDocumentFileLocation.access_hash = document6.access_hash;
                                            tL_inputDocumentFileLocation.file_reference = document6.file_reference;
                                            tL_inputDocumentFileLocation.thumb_size = _UrlKt.FRAGMENT_ENCODE_SET;
                                            inputFileLocationArr2 = new TLRPC.InputFileLocation[]{tL_inputDocumentFileLocation};
                                        }
                                    } catch (Exception e3) {
                                        FileLog.e(e3);
                                        fileReference = null;
                                    }
                                    if (fileReference == null) {
                                        fileReference = getFileReference(tL_help_appUpdate.document, null, requester2.location, zArr3, inputFileLocationArr2);
                                    }
                                    if (fileReference == null) {
                                        zArr3 = zArr3;
                                        zArr3 = zArr3;
                                        fileRefController = this;
                                        fileReference = fileRefController.getFileReference(tL_help_appUpdate.sticker, null, requester2.location, zArr3, inputFileLocationArr2);
                                    } else {
                                        zArr3 = zArr3;
                                        zArr3 = zArr3;
                                        fileRefController = this;
                                    }
                                } else {
                                    fileRefController = this;
                                    if (tLObject2 instanceof TLRPC.TL_messages_webPage) {
                                        zArr3 = zArr3;
                                        TLRPC.TL_messages_webPage tL_messages_webPage = (TLRPC.TL_messages_webPage) tLObject2;
                                        fileRefController.getMessagesController().putChats(tL_messages_webPage.chats, false);
                                        fileRefController.getMessagesController().putUsers(tL_messages_webPage.users, false);
                                        fileReference = fileRefController.getFileReference(tL_messages_webPage.webpage, requester2.location, zArr3, inputFileLocationArr2);
                                    } else if (tLObject2 instanceof TLRPC.WebPage) {
                                        zArr3 = zArr3;
                                        fileReference = fileRefController.getFileReference((TLRPC.WebPage) tLObject2, requester2.location, zArr3, inputFileLocationArr2);
                                    } else if (tLObject2 instanceof TL_account.TL_wallPapers) {
                                        tL_wallPapers = (TL_account.TL_wallPapers) tLObject2;
                                        size9 = tL_wallPapers.wallpapers.size();
                                        i11 = 0;
                                        while (i11 < size9) {
                                            fileReference = fileRefController.getFileReference(tL_wallPapers.wallpapers.get(i11).document, null, requester2.location, zArr3, inputFileLocationArr2);
                                            if (fileReference != null) {
                                                break;
                                            }
                                            zArr3 = zArr3;
                                            i11++;
                                            fileRefController = this;
                                        }
                                        if (fileReference != null && z) {
                                            getMessagesStorage().putWallpapers(tL_wallPapers.wallpapers, 1);
                                        }
                                    } else {
                                        if (tLObject2 instanceof TLRPC.TL_wallPaper) {
                                            tL_wallPaper = (TLRPC.TL_wallPaper) tLObject2;
                                            fileReference = getFileReference(tL_wallPaper.document, null, requester2.location, zArr3, inputFileLocationArr2);
                                            if (fileReference != null && z) {
                                                ArrayList<TLRPC.WallPaper> arrayList8 = new ArrayList<>();
                                                arrayList8.add(tL_wallPaper);
                                                getMessagesStorage().putWallpapers(arrayList8, 0);
                                            }
                                        } else if (tLObject2 instanceof TLRPC.TL_theme) {
                                            tL_theme = (TLRPC.TL_theme) tLObject2;
                                            fileRefController = this;
                                            fileReference = fileRefController.getFileReference(tL_theme.document, null, requester2.location, zArr3, inputFileLocationArr2);
                                            if (fileReference != null && z) {
                                                AndroidUtilities.runOnUIThread(new Runnable() { // from class: org.telegram.messenger.FileRefController$$ExternalSyntheticLambda0
                                                    @Override // java.lang.Runnable
                                                    public final void run() {
                                                        Theme.setThemeFileReference(tL_theme);
                                                    }
                                                });
                                            }
                                        } else {
                                            fileRefController = this;
                                            if (tLObject2 instanceof Vector) {
                                                vector = (Vector) tLObject2;
                                                if (!vector.objects.isEmpty()) {
                                                    size8 = vector.objects.size();
                                                    i9 = 0;
                                                    while (true) {
                                                        if (i9 < size8) {
                                                            zArr3 = zArr3;
                                                            break;
                                                        }
                                                        obj2 = vector.objects.get(i9);
                                                        if (obj2 instanceof TLRPC.User) {
                                                            final TLRPC.User user2 = (TLRPC.User) obj2;
                                                            fileReference = fileRefController.getFileReference(user2, requester2.location, zArr3, inputFileLocationArr2);
                                                            if (z || fileReference == null) {
                                                                zArr3 = zArr3;
                                                                i10 = size8;
                                                            } else {
                                                                ArrayList arrayList9 = new ArrayList();
                                                                arrayList9.add(user2);
                                                                i10 = size8;
                                                                fileRefController.getMessagesStorage().putUsersAndChats(arrayList9, null, true, true);
                                                                AndroidUtilities.runOnUIThread(new Runnable() { // from class: org.telegram.messenger.FileRefController$$ExternalSyntheticLambda1
                                                                    @Override // java.lang.Runnable
                                                                    public final void run() {
                                                                        this.f$0.lambda$onRequestComplete$42(user2);
                                                                    }
                                                                });
                                                            }
                                                        } else {
                                                            i10 = size8;
                                                            if (obj2 instanceof TLRPC.Chat) {
                                                                chat2 = (TLRPC.Chat) obj2;
                                                                byte[] fileReference5 = fileRefController.getFileReference(chat2, requester2.location, zArr3, inputFileLocationArr2);
                                                                if (z && fileReference5 != null) {
                                                                    ArrayList arrayList10 = new ArrayList();
                                                                    arrayList10.add(chat2);
                                                                    fileRefController.getMessagesStorage().putUsersAndChats(null, arrayList10, true, true);
                                                                    AndroidUtilities.runOnUIThread(new Runnable() { // from class: org.telegram.messenger.FileRefController$$ExternalSyntheticLambda2
                                                                        @Override // java.lang.Runnable
                                                                        public final void run() {
                                                                            this.f$0.lambda$onRequestComplete$43(chat2);
                                                                        }
                                                                    });
                                                                }
                                                                zArr3 = zArr3;
                                                                fileReference = fileReference5;
                                                            }
                                                        }
                                                        if (fileReference != null) {
                                                            break;
                                                        }
                                                        i9++;
                                                        size8 = i10;
                                                    }
                                                }
                                            } else if (tLObject2 instanceof TLRPC.TL_messages_chats) {
                                                tL_messages_chats = (TLRPC.TL_messages_chats) tLObject2;
                                                if (!tL_messages_chats.chats.isEmpty()) {
                                                    zArr3 = zArr3;
                                                    inputFileLocation = null;
                                                    break;
                                                }
                                                size7 = tL_messages_chats.chats.size();
                                                i8 = 0;
                                                while (true) {
                                                    if (i8 >= size7) {
                                                        zArr3 = zArr3;
                                                        zArr3 = zArr3;
                                                        inputFileLocation = null;
                                                        break;
                                                    }
                                                    chat = (TLRPC.Chat) tL_messages_chats.chats.get(i8);
                                                    fileReference2 = fileRefController.getFileReference(chat, requester2.location, zArr3, inputFileLocationArr2);
                                                    if (fileReference2 != null) {
                                                        if (z) {
                                                            zArr3 = zArr3;
                                                            ArrayList arrayList11 = new ArrayList();
                                                            arrayList11.add(chat);
                                                            inputFileLocation = null;
                                                            fileRefController.getMessagesStorage().putUsersAndChats(null, arrayList11, true, true);
                                                            AndroidUtilities.runOnUIThread(new Runnable() { // from class: org.telegram.messenger.FileRefController$$ExternalSyntheticLambda3
                                                                @Override // java.lang.Runnable
                                                                public final void run() {
                                                                    this.f$0.lambda$onRequestComplete$44(chat);
                                                                }
                                                            });
                                                        } else {
                                                            zArr3 = zArr3;
                                                            inputFileLocation = null;
                                                        }
                                                        fileReference = fileReference2;
                                                        break;
                                                    }
                                                    zArr3 = zArr3;
                                                    i8++;
                                                    fileReference = fileReference2;
                                                }
                                            } else {
                                                inputFileLocation = null;
                                                if (tLObject2 instanceof TLRPC.TL_messages_savedGifs) {
                                                    tL_messages_savedGifs = (TLRPC.TL_messages_savedGifs) tLObject2;
                                                    size6 = tL_messages_savedGifs.gifs.size();
                                                    i7 = 0;
                                                    while (i7 < size6) {
                                                        fileReference = fileRefController.getFileReference((TLRPC.Document) tL_messages_savedGifs.gifs.get(i7), null, requester2.location, zArr3, inputFileLocationArr2);
                                                        if (fileReference != null) {
                                                            break;
                                                        }
                                                        zArr3 = zArr3;
                                                        i7++;
                                                        fileRefController = this;
                                                    }
                                                    if (z) {
                                                        zArr3 = zArr3;
                                                        zArr3 = zArr3;
                                                        getMediaDataController().processLoadedRecentDocuments(0, tL_messages_savedGifs.gifs, true, 0, true);
                                                    }
                                                } else if (tLObject2 instanceof TLRPC.TL_messages_stickerSet) {
                                                    tL_messages_stickerSet = (TLRPC.TL_messages_stickerSet) tLObject2;
                                                    if (fileReference == null) {
                                                        zArr3 = zArr3;
                                                        fileRefController = this;
                                                        break;
                                                    }
                                                    size5 = tL_messages_stickerSet.documents.size();
                                                    i6 = 0;
                                                    while (true) {
                                                        if (i6 >= size5) {
                                                            zArr3 = zArr3;
                                                            zArr3 = zArr3;
                                                            fileRefController = this;
                                                            break;
                                                        }
                                                        fileRefController = this;
                                                        fileReference = fileRefController.getFileReference((TLRPC.Document) tL_messages_stickerSet.documents.get(i6), null, requester2.location, zArr3, inputFileLocationArr2);
                                                        if (fileReference != null) {
                                                            break;
                                                        }
                                                        zArr3 = zArr3;
                                                        i6++;
                                                    }
                                                    if (z) {
                                                        zArr3 = zArr3;
                                                        AndroidUtilities.runOnUIThread(new Runnable() { // from class: org.telegram.messenger.FileRefController$$ExternalSyntheticLambda4
                                                            @Override // java.lang.Runnable
                                                            public final void run() {
                                                                this.f$0.lambda$onRequestComplete$45(tL_messages_stickerSet);
                                                            }
                                                        });
                                                    }
                                                } else if (tLObject2 instanceof TLRPC.TL_messages_recentStickers) {
                                                    tL_messages_recentStickers = (TLRPC.TL_messages_recentStickers) tLObject2;
                                                    size4 = tL_messages_recentStickers.stickers.size();
                                                    for (i5 = 0; i5 < size4; i5++) {
                                                        fileReference = getFileReference((TLRPC.Document) tL_messages_recentStickers.stickers.get(i5), null, requester2.location, zArr3, inputFileLocationArr2);
                                                        if (fileReference != null) {
                                                            break;
                                                        }
                                                        zArr3 = zArr3;
                                                    }
                                                    if (z) {
                                                        zArr3 = zArr3;
                                                        zArr3 = zArr3;
                                                        getMediaDataController().processLoadedRecentDocuments(0, tL_messages_recentStickers.stickers, false, 0, true);
                                                    }
                                                } else if (tLObject2 instanceof TLRPC.TL_messages_favedStickers) {
                                                    tL_messages_favedStickers = (TLRPC.TL_messages_favedStickers) tLObject2;
                                                    size3 = tL_messages_favedStickers.stickers.size();
                                                    i4 = 0;
                                                    while (true) {
                                                        if (i4 >= size3) {
                                                            zArr3 = zArr3;
                                                            fileRefController = this;
                                                            break;
                                                        }
                                                        fileRefController = this;
                                                        fileReference = fileRefController.getFileReference((TLRPC.Document) tL_messages_favedStickers.stickers.get(i4), null, requester2.location, zArr3, inputFileLocationArr2);
                                                        if (fileReference != null) {
                                                            break;
                                                        }
                                                        zArr3 = zArr3;
                                                        i4++;
                                                    }
                                                    if (z) {
                                                        zArr3 = zArr3;
                                                        fileRefController.getMediaDataController().processLoadedRecentDocuments(2, tL_messages_favedStickers.stickers, false, 0, true);
                                                    }
                                                } else {
                                                    fileRefController = this;
                                                    if (tLObject2 instanceof TLRPC.photos_Photos) {
                                                        photos_photos = (TLRPC.photos_Photos) tLObject2;
                                                        size2 = photos_photos.photos.size();
                                                        i3 = 0;
                                                        while (true) {
                                                            if (i3 >= size2) {
                                                                zArr3 = zArr3;
                                                                break;
                                                            }
                                                            fileReference = fileRefController.getFileReference((TLRPC.Photo) photos_photos.photos.get(i3), requester2.location, zArr3, inputFileLocationArr2);
                                                            if (fileReference != null) {
                                                                zArr3 = zArr3;
                                                                break;
                                                            }
                                                            zArr3 = zArr3;
                                                            i3++;
                                                        }
                                                    } else if (tLObject2 instanceof TL_stories.TL_stories_stories) {
                                                        tL_stories_stories = (TL_stories.TL_stories_stories) tLObject2;
                                                        if (!tL_stories_stories.stories.isEmpty() || (messageMedia = (storyItem = tL_stories_stories.stories.get(0)).media) == null) {
                                                            zArr3 = zArr3;
                                                            zArr3 = zArr3;
                                                            storyItem = null;
                                                        } else {
                                                            if (fileReference == null && (photo2 = messageMedia.photo) != null) {
                                                                fileReference = fileRefController.getFileReference(photo2, requester2.location, zArr3, inputFileLocationArr2);
                                                            }
                                                            if (fileReference == null && (photo = storyItem.media.video_cover) != null) {
                                                                fileReference = fileRefController.getFileReference(photo, requester2.location, zArr3, inputFileLocationArr2);
                                                            }
                                                            if (fileReference == null && (document = (messageMedia2 = storyItem.media).document) != null) {
                                                                fileReference = fileRefController.getFileReference(document, messageMedia2.alt_documents, requester2.location, zArr3, inputFileLocationArr2);
                                                            }
                                                        }
                                                        if (requester2.args[1] instanceof FileLoadOperation) {
                                                            obj = ((FileLoadOperation) requester2.args[1]).parentObject;
                                                            if (obj instanceof TL_stories.StoryItem) {
                                                                storyItem2 = (TL_stories.StoryItem) obj;
                                                                if (storyItem == null) {
                                                                    TL_stories.TL_updateStory tL_updateStory = new TL_stories.TL_updateStory();
                                                                    tL_updateStory.peer = fileRefController.getMessagesController().getPeer(storyItem2.dialogId);
                                                                    TL_stories.TL_storyItemDeleted tL_storyItemDeleted = new TL_stories.TL_storyItemDeleted();
                                                                    tL_updateStory.story = tL_storyItemDeleted;
                                                                    tL_storyItemDeleted.id = storyItem2.id;
                                                                    ArrayList<TLRPC.Update> arrayList12 = new ArrayList<>();
                                                                    arrayList12.add(tL_updateStory);
                                                                    fileRefController.getMessagesController().processUpdateArray(arrayList12, null, null, false, 0);
                                                                } else {
                                                                    user = fileRefController.getMessagesController().getUser(Long.valueOf(storyItem2.dialogId));
                                                                    if (user != null && user.contact) {
                                                                        MessagesController.getInstance(fileRefController.currentAccount).getStoriesController().getStoriesStorage().updateStoryItem(storyItem2.dialogId, storyItem);
                                                                    }
                                                                }
                                                                if (storyItem != null && fileReference == null) {
                                                                    TL_stories.TL_updateStory tL_updateStory2 = new TL_stories.TL_updateStory();
                                                                    tL_updateStory2.peer = MessagesController.getInstance(fileRefController.currentAccount).getPeer(storyItem2.dialogId);
                                                                    tL_updateStory2.story = storyItem;
                                                                    ArrayList<TLRPC.Update> arrayList13 = new ArrayList<>();
                                                                    arrayList13.add(tL_updateStory2);
                                                                    MessagesController.getInstance(fileRefController.currentAccount).processUpdateArray(arrayList13, null, null, false, 0);
                                                                }
                                                            }
                                                        }
                                                    }
                                                }
                                                zArr3 = zArr3;
                                                zArr3 = zArr3;
                                                zArr3 = zArr3;
                                                zArr3 = zArr3;
                                                fileRefController = this;
                                            }
                                        }
                                        zArr3 = zArr3;
                                        zArr3 = zArr3;
                                        zArr3 = zArr3;
                                        inputFileLocationArr = inputFileLocationArr2;
                                        if (fileReference != null) {
                                            if (inputFileLocationArr != null) {
                                                inputFileLocation2 = inputFileLocationArr[0];
                                            } else {
                                                inputFileLocation2 = inputFileLocation;
                                            }
                                            if (fileRefController.onUpdateObjectReference(requester2, fileReference, inputFileLocation2, z2)) {
                                                zArr = zArr3;
                                                r10 = 1;
                                                z3 = true;
                                            } else {
                                                r10 = 1;
                                            }
                                            i2++;
                                            tL_error = tL_error;
                                            i = r10;
                                            z5 = z4;
                                            arrayList = arrayList2;
                                        } else {
                                            r10 = 1;
                                            fileRefController.sendErrorToObject(requester2.args, 1);
                                        }
                                        zArr = zArr3;
                                        i2++;
                                        tL_error = tL_error;
                                        i = r10;
                                        z5 = z4;
                                        arrayList = arrayList2;
                                    }
                                }
                            }
                            zArr3 = zArr3;
                            zArr3 = zArr3;
                            zArr3 = zArr3;
                            inputFileLocation = null;
                            zArr3 = zArr3;
                            zArr3 = zArr3;
                            zArr3 = zArr3;
                            zArr3 = zArr3;
                            fileRefController = this;
                            zArr3 = zArr3;
                            zArr3 = zArr3;
                            zArr3 = zArr3;
                            inputFileLocationArr = inputFileLocationArr2;
                            if (fileReference != null) {
                                if (inputFileLocationArr != null) {
                                    inputFileLocation2 = inputFileLocationArr[0];
                                } else {
                                    inputFileLocation2 = inputFileLocation;
                                }
                                if (fileRefController.onUpdateObjectReference(requester2, fileReference, inputFileLocation2, z2)) {
                                    zArr = zArr3;
                                    r10 = 1;
                                    z3 = true;
                                } else {
                                    r10 = 1;
                                }
                                i2++;
                                tL_error = tL_error;
                                i = r10;
                                z5 = z4;
                                arrayList = arrayList2;
                            } else {
                                r10 = 1;
                                fileRefController.sendErrorToObject(requester2.args, 1);
                            }
                            zArr = zArr3;
                            i2++;
                            tL_error = tL_error;
                            i = r10;
                            z5 = z4;
                            arrayList = arrayList2;
                        }
                        zArr3 = zArr3;
                        zArr3 = zArr3;
                        inputFileLocationArr = inputFileLocationArr2;
                    }
                    inputFileLocation = null;
                    if (fileReference != null) {
                        if (inputFileLocationArr != null) {
                            inputFileLocation2 = inputFileLocationArr[0];
                        } else {
                            inputFileLocation2 = inputFileLocation;
                        }
                        if (fileRefController.onUpdateObjectReference(requester2, fileReference, inputFileLocation2, z2)) {
                            zArr = zArr3;
                            r10 = 1;
                            z3 = true;
                        } else {
                            r10 = 1;
                        }
                        i2++;
                        tL_error = tL_error;
                        i = r10;
                        z5 = z4;
                        arrayList = arrayList2;
                    } else {
                        r10 = 1;
                        fileRefController.sendErrorToObject(requester2.args, 1);
                    }
                    zArr = zArr3;
                    i2++;
                    tL_error = tL_error;
                    i = r10;
                    z5 = z4;
                    arrayList = arrayList2;
                }
                i2++;
                tL_error = tL_error;
                i = r10;
                z5 = z4;
                arrayList = arrayList2;
            }
            fileRefController.locationRequester.remove(str);
            if (z3) {
                fileRefController.putReponseToCache(str, tLObject2);
            }
            return z3;
        }
        str3 = str4;
        i = 1;
        if (str2 != null) {
            tLObject2 = tLObject4;
            z3 = false;
        } else {
            tLObject2 = tLObject4;
            z3 = false;
        }
        arrayList = fileRefController.locationRequester.get(str);
        if (arrayList == null) {
            return z3;
        }
        size = arrayList.size();
        zArr = null;
        inputFileLocationArr = null;
        fileReference = null;
        i2 = 0;
        while (i2 < size) {
            requester = arrayList.get(i2);
            if (requester.completed) {
                z4 = z5;
                arrayList2 = arrayList;
                r10 = i;
            } else {
                if (tL_error != null) {
                    FileLog.e("debug_loading: " + ((FileLoadOperation) requester.args[i]).getCacheFileFinal().getName() + " can't update file reference: " + tL_error.code + " " + tL_error.text);
                }
                if (!(requester.location instanceof TLRPC.TL_inputFileLocation)) {
                    inputFileLocationArr = new TLRPC.InputFileLocation[i];
                    zArr = new boolean[i];
                } else {
                    inputFileLocationArr = new TLRPC.InputFileLocation[i];
                    zArr = new boolean[i];
                }
                inputFileLocationArr2 = inputFileLocationArr;
                zArr2 = zArr;
                requester.completed = i;
                if (tLObject2 instanceof StoriesController.BotPreview) {
                    messageMedia6 = ((StoriesController.BotPreview) tLObject2).media;
                    requester2 = requester;
                    zArr3 = zArr2;
                    document4 = messageMedia6.document;
                    if (document4 != null) {
                        fileReference = fileRefController.getFileReference(document4, messageMedia6.alt_documents, requester2.location, zArr3, inputFileLocationArr2);
                    } else {
                        photo6 = messageMedia6.photo;
                        if (photo6 != null) {
                            fileReference = fileRefController.getFileReference(photo6, requester2.location, zArr3, inputFileLocationArr2);
                        }
                    }
                    inputFileLocationArr = inputFileLocationArr2;
                    z4 = z5;
                    arrayList2 = arrayList;
                } else {
                    requester2 = requester;
                    zArr3 = zArr2;
                    if (tLObject2 instanceof TLRPC.messages_Messages) {
                        messages_messages = (TLRPC.messages_Messages) tLObject2;
                        if (!messages_messages.messages.isEmpty()) {
                            size13 = messages_messages.messages.size();
                            i19 = 0;
                            while (true) {
                                if (i19 < size13) {
                                    message = (TLRPC.Message) messages_messages.messages.get(i19);
                                    messageMedia3 = message.media;
                                    if (messageMedia3 instanceof TLRPC.TL_messageMediaPaidMedia) {
                                        tL_messageMediaPaidMedia = (TLRPC.TL_messageMediaPaidMedia) messageMedia3;
                                        i21 = i19;
                                        i22 = 0;
                                        while (true) {
                                            if (i22 < tL_messageMediaPaidMedia.extended_media.size()) {
                                                fileRefController = this;
                                                z4 = z5;
                                                arrayList2 = arrayList;
                                                message2 = message;
                                                break;
                                            }
                                            messageExtendedMedia = tL_messageMediaPaidMedia.extended_media.get(i22);
                                            i23 = i22;
                                            if (messageExtendedMedia instanceof TLRPC.TL_messageExtendedMedia) {
                                                fileRefController = this;
                                                arrayList2 = arrayList;
                                                i24 = i23;
                                                z4 = z5;
                                                message2 = message;
                                            } else {
                                                fileRefController = this;
                                                arrayList2 = arrayList;
                                                i24 = i23;
                                                z4 = z5;
                                                message2 = message;
                                            }
                                            if (fileReference != null) {
                                                break;
                                                break;
                                            }
                                            i22 = i24 + 1;
                                            message = message2;
                                            z5 = z4;
                                            arrayList = arrayList2;
                                        }
                                    } else {
                                        fileRefController = this;
                                        i21 = i19;
                                        z4 = z5;
                                        arrayList2 = arrayList;
                                        message2 = message;
                                        if (messageMedia3 != null) {
                                            document3 = messageMedia3.document;
                                            if (document3 != null) {
                                                fileReference = fileRefController.getFileReference(document3, messageMedia3.alt_documents, requester2.location, zArr3, inputFileLocationArr2);
                                                fileRefController = this;
                                            } else {
                                                tL_game = messageMedia3.game;
                                                if (tL_game != null) {
                                                    fileRefController = this;
                                                    fileReference = fileRefController.getFileReference(tL_game.document, null, requester2.location, zArr3, inputFileLocationArr2);
                                                    if (fileReference == null) {
                                                        fileReference = fileRefController.getFileReference(message2.media.game.photo, requester2.location, zArr3, inputFileLocationArr2);
                                                    }
                                                } else {
                                                    fileRefController = this;
                                                    photo3 = messageMedia3.photo;
                                                    if (photo3 != null) {
                                                        fileReference = fileRefController.getFileReference(photo3, requester2.location, zArr3, inputFileLocationArr2);
                                                    } else {
                                                        webPage = messageMedia3.webpage;
                                                        if (webPage != null) {
                                                            fileReference = fileRefController.getFileReference(webPage, requester2.location, zArr3, inputFileLocationArr2);
                                                        }
                                                    }
                                                }
                                            }
                                            if (fileReference == null) {
                                            }
                                        } else {
                                            messageAction = message2.action;
                                            if (!(messageAction instanceof TLRPC.TL_messageActionChatEditPhoto)) {
                                            }
                                            fileReference4 = fileRefController.getFileReference(messageAction.photo, requester2.location, zArr3, inputFileLocationArr2);
                                            fileReference = fileReference4;
                                        }
                                    }
                                    if (fileReference != null) {
                                        if (z) {
                                            i20 = 0;
                                            fileRefController.getMessagesStorage().replaceMessageIfExists(message2, messages_messages.users, messages_messages.chats, false);
                                            break;
                                        }
                                    } else {
                                        i19 = i21 + 1;
                                        z5 = z4;
                                        arrayList = arrayList2;
                                    }
                                } else {
                                    z4 = z5;
                                    arrayList2 = arrayList;
                                }
                                i20 = 0;
                                break;
                            }
                            if (fileReference == null) {
                                fileRefController.getMessagesStorage().replaceMessageIfExists((TLRPC.Message) messages_messages.messages.get(i20), messages_messages.users, messages_messages.chats, true);
                                if (BuildVars.DEBUG_VERSION) {
                                    FileLog.d("file ref not found in messages, replacing message");
                                }
                            }
                        } else {
                            z4 = z5;
                            arrayList2 = arrayList;
                            if (BuildVars.DEBUG_VERSION) {
                                FileLog.d("empty messages, file ref not found");
                            }
                        }
                    } else {
                        z4 = z5;
                        arrayList2 = arrayList;
                        if (z4) {
                            arrayList6 = ((TLRPC.TL_help_premiumPromo) tLObject2).videos;
                            size12 = arrayList6.size();
                            i17 = 0;
                            while (i17 < size12) {
                                i18 = i17 + 1;
                                fileReference = fileRefController.getFileReference((TLRPC.Document) arrayList6.get(i17), null, requester2.location, zArr3, inputFileLocationArr2);
                                if (fileReference != null) {
                                    break;
                                    break;
                                }
                                fileRefController = this;
                                i17 = i18;
                            }
                        } else {
                            j = 1000;
                            if (tLObject2 instanceof TLRPC.TL_messages_availableReactions) {
                                TLRPC.TL_messages_availableReactions tL_messages_availableReactions2 = (TLRPC.TL_messages_availableReactions) tLObject2;
                                getMediaDataController().processLoadedReactions(tL_messages_availableReactions2.reactions, tL_messages_availableReactions2.hash, (int) (System.currentTimeMillis() / 1000), false);
                                arrayList5 = tL_messages_availableReactions2.reactions;
                                size11 = arrayList5.size();
                                i15 = 0;
                                while (i15 < size11) {
                                    i16 = i15 + 1;
                                    tL_availableReaction = (TLRPC.TL_availableReaction) arrayList5.get(i15);
                                    fileReference3 = getFileReference(tL_availableReaction.static_icon, null, requester2.location, zArr3, inputFileLocationArr2);
                                    if (fileReference3 != null) {
                                        fileReference = getFileReference(tL_availableReaction.center_icon, null, requester2.location, zArr3, inputFileLocationArr2);
                                        if (fileReference != null) {
                                            break;
                                            break;
                                        }
                                        i15 = i16;
                                    }
                                    fileReference = fileReference3;
                                    break;
                                }
                            } else if (tLObject2 instanceof TLRPC.TL_users_userFull) {
                                TLRPC.TL_users_userFull tL_users_userFull2 = (TLRPC.TL_users_userFull) tLObject2;
                                getMessagesController().putUsers(tL_users_userFull2.users, false);
                                getMessagesController().putChats(tL_users_userFull2.chats, false);
                                userFull = tL_users_userFull2.full_user;
                                botInfo = userFull.bot_info;
                                if (botInfo != null) {
                                    getMessagesStorage().updateUserInfo(userFull, true);
                                    fileRefController = this;
                                    fileReference = fileRefController.getFileReference(botInfo.description_document, null, requester2.location, zArr3, inputFileLocationArr2);
                                    if (fileReference != null) {
                                        zArr = zArr3;
                                        inputFileLocationArr = inputFileLocationArr2;
                                        r10 = 1;
                                    } else {
                                        fileReference = fileRefController.getFileReference(botInfo.description_photo, requester2.location, zArr3, inputFileLocationArr2);
                                    }
                                } else {
                                    zArr3 = zArr3;
                                    zArr3 = zArr3;
                                    fileRefController = this;
                                }
                            } else if (tLObject2 instanceof TLRPC.TL_attachMenuBotsBot) {
                                tL_attachMenuBot = ((TLRPC.TL_attachMenuBotsBot) tLObject2).bot;
                                arrayList3 = tL_attachMenuBot.icons;
                                size10 = arrayList3.size();
                                i12 = 0;
                                while (i12 < size10) {
                                    i14 = i12 + 1;
                                    fileReference = getFileReference(((TLRPC.TL_attachMenuBotIcon) arrayList3.get(i12)).icon, null, requester2.location, zArr3, inputFileLocationArr2);
                                    if (fileReference != null) {
                                        break;
                                        break;
                                    }
                                    i12 = i14;
                                }
                                if (z) {
                                    TLRPC.TL_attachMenuBots attachMenuBots2 = getMediaDataController().getAttachMenuBots();
                                    arrayList4 = new ArrayList(attachMenuBots2.bots);
                                    i13 = 0;
                                    while (true) {
                                        if (i13 < arrayList4.size()) {
                                            bArr = fileReference;
                                            zArr4 = zArr3;
                                            j2 = j;
                                            break;
                                        }
                                        j2 = j;
                                        bArr = fileReference;
                                        zArr4 = zArr3;
                                        if (((TLRPC.TL_attachMenuBot) arrayList4.get(i13)).bot_id == tL_attachMenuBot.bot_id) {
                                            arrayList4.set(i13, tL_attachMenuBot);
                                            break;
                                        }
                                        i13++;
                                        zArr3 = zArr4;
                                        fileReference = bArr;
                                        j = j2;
                                    }
                                    attachMenuBots2.bots = arrayList4;
                                    getMediaDataController().processLoadedMenuBots(attachMenuBots2, attachMenuBots2.hash, (int) (System.currentTimeMillis() / j2), false);
                                } else {
                                    bArr = fileReference;
                                    zArr4 = zArr3;
                                }
                                fileRefController = this;
                                inputFileLocationArr = inputFileLocationArr2;
                                zArr3 = zArr4;
                                fileReference = bArr;
                            } else if (tLObject2 instanceof TLRPC.TL_help_appUpdate) {
                                tL_help_appUpdate = (TLRPC.TL_help_appUpdate) tLObject2;
                                SharedConfig.pendingAppUpdate = tL_help_appUpdate;
                                SharedConfig.saveConfig();
                                NotificationCenter.getGlobalInstance().lambda$postNotificationNameOnUIThread$1(NotificationCenter.appUpdateAvailable, new Object[0]);
                                document2 = tL_help_appUpdate.document;
                                if (document2 != null) {
                                    fileReference = document2.file_reference;
                                    TLRPC.TL_inputDocumentFileLocation tL_inputDocumentFileLocation2 = new TLRPC.TL_inputDocumentFileLocation();
                                    TLRPC.Document document7 = tL_help_appUpdate.document;
                                    tL_inputDocumentFileLocation2.id = document7.id;
                                    tL_inputDocumentFileLocation2.access_hash = document7.access_hash;
                                    tL_inputDocumentFileLocation2.file_reference = document7.file_reference;
                                    tL_inputDocumentFileLocation2.thumb_size = _UrlKt.FRAGMENT_ENCODE_SET;
                                    inputFileLocationArr2 = new TLRPC.InputFileLocation[]{tL_inputDocumentFileLocation2};
                                }
                                if (fileReference == null) {
                                    fileReference = getFileReference(tL_help_appUpdate.document, null, requester2.location, zArr3, inputFileLocationArr2);
                                }
                                if (fileReference == null) {
                                    zArr3 = zArr3;
                                    zArr3 = zArr3;
                                    fileRefController = this;
                                    fileReference = fileRefController.getFileReference(tL_help_appUpdate.sticker, null, requester2.location, zArr3, inputFileLocationArr2);
                                } else {
                                    zArr3 = zArr3;
                                    zArr3 = zArr3;
                                    fileRefController = this;
                                }
                            } else {
                                fileRefController = this;
                                if (tLObject2 instanceof TLRPC.TL_messages_webPage) {
                                    zArr3 = zArr3;
                                    TLRPC.TL_messages_webPage tL_messages_webPage2 = (TLRPC.TL_messages_webPage) tLObject2;
                                    fileRefController.getMessagesController().putChats(tL_messages_webPage2.chats, false);
                                    fileRefController.getMessagesController().putUsers(tL_messages_webPage2.users, false);
                                    fileReference = fileRefController.getFileReference(tL_messages_webPage2.webpage, requester2.location, zArr3, inputFileLocationArr2);
                                } else if (tLObject2 instanceof TLRPC.WebPage) {
                                    zArr3 = zArr3;
                                    fileReference = fileRefController.getFileReference((TLRPC.WebPage) tLObject2, requester2.location, zArr3, inputFileLocationArr2);
                                } else if (tLObject2 instanceof TL_account.TL_wallPapers) {
                                    tL_wallPapers = (TL_account.TL_wallPapers) tLObject2;
                                    size9 = tL_wallPapers.wallpapers.size();
                                    i11 = 0;
                                    while (i11 < size9) {
                                        fileReference = fileRefController.getFileReference(tL_wallPapers.wallpapers.get(i11).document, null, requester2.location, zArr3, inputFileLocationArr2);
                                        if (fileReference != null) {
                                            break;
                                            break;
                                        }
                                        zArr3 = zArr3;
                                        i11++;
                                        fileRefController = this;
                                    }
                                    if (fileReference != null) {
                                        getMessagesStorage().putWallpapers(tL_wallPapers.wallpapers, 1);
                                    }
                                } else {
                                    if (tLObject2 instanceof TLRPC.TL_wallPaper) {
                                        tL_wallPaper = (TLRPC.TL_wallPaper) tLObject2;
                                        fileReference = getFileReference(tL_wallPaper.document, null, requester2.location, zArr3, inputFileLocationArr2);
                                        if (fileReference != null) {
                                            ArrayList<TLRPC.WallPaper> arrayList14 = new ArrayList<>();
                                            arrayList14.add(tL_wallPaper);
                                            getMessagesStorage().putWallpapers(arrayList14, 0);
                                        }
                                    } else if (tLObject2 instanceof TLRPC.TL_theme) {
                                        tL_theme = (TLRPC.TL_theme) tLObject2;
                                        fileRefController = this;
                                        fileReference = fileRefController.getFileReference(tL_theme.document, null, requester2.location, zArr3, inputFileLocationArr2);
                                        if (fileReference != null) {
                                            AndroidUtilities.runOnUIThread(new Runnable() { // from class: org.telegram.messenger.FileRefController$$ExternalSyntheticLambda0
                                                @Override // java.lang.Runnable
                                                public final void run() {
                                                    Theme.setThemeFileReference(tL_theme);
                                                }
                                            });
                                        }
                                    } else {
                                        fileRefController = this;
                                        if (tLObject2 instanceof Vector) {
                                            vector = (Vector) tLObject2;
                                            if (!vector.objects.isEmpty()) {
                                                size8 = vector.objects.size();
                                                i9 = 0;
                                                while (true) {
                                                    if (i9 < size8) {
                                                        zArr3 = zArr3;
                                                        break;
                                                    }
                                                    obj2 = vector.objects.get(i9);
                                                    if (obj2 instanceof TLRPC.User) {
                                                        final TLRPC.User user3 = (TLRPC.User) obj2;
                                                        fileReference = fileRefController.getFileReference(user3, requester2.location, zArr3, inputFileLocationArr2);
                                                        if (z) {
                                                            zArr3 = zArr3;
                                                            i10 = size8;
                                                        } else {
                                                            zArr3 = zArr3;
                                                            i10 = size8;
                                                        }
                                                    } else {
                                                        i10 = size8;
                                                        if (obj2 instanceof TLRPC.Chat) {
                                                            chat2 = (TLRPC.Chat) obj2;
                                                            byte[] fileReference6 = fileRefController.getFileReference(chat2, requester2.location, zArr3, inputFileLocationArr2);
                                                            if (z) {
                                                                ArrayList arrayList15 = new ArrayList();
                                                                arrayList15.add(chat2);
                                                                fileRefController.getMessagesStorage().putUsersAndChats(null, arrayList15, true, true);
                                                                AndroidUtilities.runOnUIThread(new Runnable() { // from class: org.telegram.messenger.FileRefController$$ExternalSyntheticLambda2
                                                                    @Override // java.lang.Runnable
                                                                    public final void run() {
                                                                        this.f$0.lambda$onRequestComplete$43(chat2);
                                                                    }
                                                                });
                                                            }
                                                            zArr3 = zArr3;
                                                            fileReference = fileReference6;
                                                        }
                                                    }
                                                    if (fileReference != null) {
                                                        break;
                                                        break;
                                                    }
                                                    i9++;
                                                    size8 = i10;
                                                }
                                            }
                                        } else if (tLObject2 instanceof TLRPC.TL_messages_chats) {
                                            tL_messages_chats = (TLRPC.TL_messages_chats) tLObject2;
                                            if (!tL_messages_chats.chats.isEmpty()) {
                                                zArr3 = zArr3;
                                                inputFileLocation = null;
                                                break;
                                            }
                                            size7 = tL_messages_chats.chats.size();
                                            i8 = 0;
                                            while (true) {
                                                if (i8 >= size7) {
                                                    zArr3 = zArr3;
                                                    zArr3 = zArr3;
                                                    inputFileLocation = null;
                                                    break;
                                                }
                                                chat = (TLRPC.Chat) tL_messages_chats.chats.get(i8);
                                                fileReference2 = fileRefController.getFileReference(chat, requester2.location, zArr3, inputFileLocationArr2);
                                                if (fileReference2 != null) {
                                                    if (z) {
                                                        zArr3 = zArr3;
                                                        ArrayList arrayList16 = new ArrayList();
                                                        arrayList16.add(chat);
                                                        inputFileLocation = null;
                                                        fileRefController.getMessagesStorage().putUsersAndChats(null, arrayList16, true, true);
                                                        AndroidUtilities.runOnUIThread(new Runnable() { // from class: org.telegram.messenger.FileRefController$$ExternalSyntheticLambda3
                                                            @Override // java.lang.Runnable
                                                            public final void run() {
                                                                this.f$0.lambda$onRequestComplete$44(chat);
                                                            }
                                                        });
                                                    } else {
                                                        zArr3 = zArr3;
                                                        inputFileLocation = null;
                                                    }
                                                    fileReference = fileReference2;
                                                    break;
                                                }
                                                zArr3 = zArr3;
                                                i8++;
                                                fileReference = fileReference2;
                                            }
                                        } else {
                                            inputFileLocation = null;
                                            if (tLObject2 instanceof TLRPC.TL_messages_savedGifs) {
                                                tL_messages_savedGifs = (TLRPC.TL_messages_savedGifs) tLObject2;
                                                size6 = tL_messages_savedGifs.gifs.size();
                                                i7 = 0;
                                                while (i7 < size6) {
                                                    fileReference = fileRefController.getFileReference((TLRPC.Document) tL_messages_savedGifs.gifs.get(i7), null, requester2.location, zArr3, inputFileLocationArr2);
                                                    if (fileReference != null) {
                                                        break;
                                                        break;
                                                    }
                                                    zArr3 = zArr3;
                                                    i7++;
                                                    fileRefController = this;
                                                }
                                                if (z) {
                                                    zArr3 = zArr3;
                                                    zArr3 = zArr3;
                                                    getMediaDataController().processLoadedRecentDocuments(0, tL_messages_savedGifs.gifs, true, 0, true);
                                                }
                                            } else if (tLObject2 instanceof TLRPC.TL_messages_stickerSet) {
                                                tL_messages_stickerSet = (TLRPC.TL_messages_stickerSet) tLObject2;
                                                if (fileReference == null) {
                                                    zArr3 = zArr3;
                                                    fileRefController = this;
                                                    break;
                                                }
                                                size5 = tL_messages_stickerSet.documents.size();
                                                i6 = 0;
                                                while (true) {
                                                    if (i6 >= size5) {
                                                        zArr3 = zArr3;
                                                        zArr3 = zArr3;
                                                        fileRefController = this;
                                                        break;
                                                    }
                                                    fileRefController = this;
                                                    fileReference = fileRefController.getFileReference((TLRPC.Document) tL_messages_stickerSet.documents.get(i6), null, requester2.location, zArr3, inputFileLocationArr2);
                                                    if (fileReference != null) {
                                                        break;
                                                        break;
                                                    }
                                                    zArr3 = zArr3;
                                                    i6++;
                                                }
                                                if (z) {
                                                    zArr3 = zArr3;
                                                    AndroidUtilities.runOnUIThread(new Runnable() { // from class: org.telegram.messenger.FileRefController$$ExternalSyntheticLambda4
                                                        @Override // java.lang.Runnable
                                                        public final void run() {
                                                            this.f$0.lambda$onRequestComplete$45(tL_messages_stickerSet);
                                                        }
                                                    });
                                                }
                                            } else if (tLObject2 instanceof TLRPC.TL_messages_recentStickers) {
                                                tL_messages_recentStickers = (TLRPC.TL_messages_recentStickers) tLObject2;
                                                size4 = tL_messages_recentStickers.stickers.size();
                                                while (i5 < size4) {
                                                    fileReference = getFileReference((TLRPC.Document) tL_messages_recentStickers.stickers.get(i5), null, requester2.location, zArr3, inputFileLocationArr2);
                                                    if (fileReference != null) {
                                                        break;
                                                        break;
                                                    }
                                                    zArr3 = zArr3;
                                                }
                                                if (z) {
                                                    zArr3 = zArr3;
                                                    zArr3 = zArr3;
                                                    getMediaDataController().processLoadedRecentDocuments(0, tL_messages_recentStickers.stickers, false, 0, true);
                                                }
                                            } else if (tLObject2 instanceof TLRPC.TL_messages_favedStickers) {
                                                tL_messages_favedStickers = (TLRPC.TL_messages_favedStickers) tLObject2;
                                                size3 = tL_messages_favedStickers.stickers.size();
                                                i4 = 0;
                                                while (true) {
                                                    if (i4 >= size3) {
                                                        zArr3 = zArr3;
                                                        fileRefController = this;
                                                        break;
                                                    }
                                                    fileRefController = this;
                                                    fileReference = fileRefController.getFileReference((TLRPC.Document) tL_messages_favedStickers.stickers.get(i4), null, requester2.location, zArr3, inputFileLocationArr2);
                                                    if (fileReference != null) {
                                                        break;
                                                        break;
                                                    }
                                                    zArr3 = zArr3;
                                                    i4++;
                                                }
                                                if (z) {
                                                    zArr3 = zArr3;
                                                    fileRefController.getMediaDataController().processLoadedRecentDocuments(2, tL_messages_favedStickers.stickers, false, 0, true);
                                                }
                                            } else {
                                                fileRefController = this;
                                                if (tLObject2 instanceof TLRPC.photos_Photos) {
                                                    photos_photos = (TLRPC.photos_Photos) tLObject2;
                                                    size2 = photos_photos.photos.size();
                                                    i3 = 0;
                                                    while (true) {
                                                        if (i3 >= size2) {
                                                            zArr3 = zArr3;
                                                            break;
                                                        }
                                                        fileReference = fileRefController.getFileReference((TLRPC.Photo) photos_photos.photos.get(i3), requester2.location, zArr3, inputFileLocationArr2);
                                                        if (fileReference != null) {
                                                            zArr3 = zArr3;
                                                            break;
                                                        }
                                                        zArr3 = zArr3;
                                                        i3++;
                                                    }
                                                } else if (tLObject2 instanceof TL_stories.TL_stories_stories) {
                                                    tL_stories_stories = (TL_stories.TL_stories_stories) tLObject2;
                                                    if (tL_stories_stories.stories.isEmpty()) {
                                                        zArr3 = zArr3;
                                                        zArr3 = zArr3;
                                                        storyItem = null;
                                                    } else {
                                                        zArr3 = zArr3;
                                                        zArr3 = zArr3;
                                                        storyItem = null;
                                                    }
                                                    if (requester2.args[1] instanceof FileLoadOperation) {
                                                        obj = ((FileLoadOperation) requester2.args[1]).parentObject;
                                                        if (obj instanceof TL_stories.StoryItem) {
                                                            storyItem2 = (TL_stories.StoryItem) obj;
                                                            if (storyItem == null) {
                                                                TL_stories.TL_updateStory tL_updateStory3 = new TL_stories.TL_updateStory();
                                                                tL_updateStory3.peer = fileRefController.getMessagesController().getPeer(storyItem2.dialogId);
                                                                TL_stories.TL_storyItemDeleted tL_storyItemDeleted2 = new TL_stories.TL_storyItemDeleted();
                                                                tL_updateStory3.story = tL_storyItemDeleted2;
                                                                tL_storyItemDeleted2.id = storyItem2.id;
                                                                ArrayList<TLRPC.Update> arrayList17 = new ArrayList<>();
                                                                arrayList17.add(tL_updateStory3);
                                                                fileRefController.getMessagesController().processUpdateArray(arrayList17, null, null, false, 0);
                                                            } else {
                                                                user = fileRefController.getMessagesController().getUser(Long.valueOf(storyItem2.dialogId));
                                                                if (user != null) {
                                                                    MessagesController.getInstance(fileRefController.currentAccount).getStoriesController().getStoriesStorage().updateStoryItem(storyItem2.dialogId, storyItem);
                                                                }
                                                            }
                                                            if (storyItem != null) {
                                                                TL_stories.TL_updateStory tL_updateStory4 = new TL_stories.TL_updateStory();
                                                                tL_updateStory4.peer = MessagesController.getInstance(fileRefController.currentAccount).getPeer(storyItem2.dialogId);
                                                                tL_updateStory4.story = storyItem;
                                                                ArrayList<TLRPC.Update> arrayList18 = new ArrayList<>();
                                                                arrayList18.add(tL_updateStory4);
                                                                MessagesController.getInstance(fileRefController.currentAccount).processUpdateArray(arrayList18, null, null, false, 0);
                                                            }
                                                        }
                                                    }
                                                }
                                            }
                                            zArr3 = zArr3;
                                            zArr3 = zArr3;
                                            zArr3 = zArr3;
                                            zArr3 = zArr3;
                                            fileRefController = this;
                                        }
                                    }
                                    zArr3 = zArr3;
                                    zArr3 = zArr3;
                                    zArr3 = zArr3;
                                    inputFileLocationArr = inputFileLocationArr2;
                                    if (fileReference != null) {
                                        if (inputFileLocationArr != null) {
                                            inputFileLocation2 = inputFileLocationArr[0];
                                        } else {
                                            inputFileLocation2 = inputFileLocation;
                                        }
                                        if (fileRefController.onUpdateObjectReference(requester2, fileReference, inputFileLocation2, z2)) {
                                            zArr = zArr3;
                                            r10 = 1;
                                            z3 = true;
                                        } else {
                                            r10 = 1;
                                        }
                                        i2++;
                                        tL_error = tL_error;
                                        i = r10;
                                        z5 = z4;
                                        arrayList = arrayList2;
                                    } else {
                                        r10 = 1;
                                        fileRefController.sendErrorToObject(requester2.args, 1);
                                    }
                                    zArr = zArr3;
                                    i2++;
                                    tL_error = tL_error;
                                    i = r10;
                                    z5 = z4;
                                    arrayList = arrayList2;
                                }
                            }
                        }
                        zArr3 = zArr3;
                        zArr3 = zArr3;
                        zArr3 = zArr3;
                        inputFileLocation = null;
                        zArr3 = zArr3;
                        zArr3 = zArr3;
                        zArr3 = zArr3;
                        zArr3 = zArr3;
                        fileRefController = this;
                        zArr3 = zArr3;
                        zArr3 = zArr3;
                        zArr3 = zArr3;
                        inputFileLocationArr = inputFileLocationArr2;
                        if (fileReference != null) {
                            if (inputFileLocationArr != null) {
                                inputFileLocation2 = inputFileLocationArr[0];
                            } else {
                                inputFileLocation2 = inputFileLocation;
                            }
                            if (fileRefController.onUpdateObjectReference(requester2, fileReference, inputFileLocation2, z2)) {
                                zArr = zArr3;
                                r10 = 1;
                                z3 = true;
                            } else {
                                r10 = 1;
                            }
                            i2++;
                            tL_error = tL_error;
                            i = r10;
                            z5 = z4;
                            arrayList = arrayList2;
                        } else {
                            r10 = 1;
                            fileRefController.sendErrorToObject(requester2.args, 1);
                        }
                        zArr = zArr3;
                        i2++;
                        tL_error = tL_error;
                        i = r10;
                        z5 = z4;
                        arrayList = arrayList2;
                    }
                    zArr3 = zArr3;
                    zArr3 = zArr3;
                    inputFileLocationArr = inputFileLocationArr2;
                }
                inputFileLocation = null;
                if (fileReference != null) {
                    if (inputFileLocationArr != null) {
                        inputFileLocation2 = inputFileLocationArr[0];
                    } else {
                        inputFileLocation2 = inputFileLocation;
                    }
                    if (fileRefController.onUpdateObjectReference(requester2, fileReference, inputFileLocation2, z2)) {
                        zArr = zArr3;
                        r10 = 1;
                        z3 = true;
                    } else {
                        r10 = 1;
                    }
                    i2++;
                    tL_error = tL_error;
                    i = r10;
                    z5 = z4;
                    arrayList = arrayList2;
                } else {
                    r10 = 1;
                    fileRefController.sendErrorToObject(requester2.args, 1);
                }
                zArr = zArr3;
                i2++;
                tL_error = tL_error;
                i = r10;
                z5 = z4;
                arrayList = arrayList2;
            }
            i2++;
            tL_error = tL_error;
            i = r10;
            z5 = z4;
            arrayList = arrayList2;
        }
        fileRefController.locationRequester.remove(str);
        if (z3) {
            fileRefController.putReponseToCache(str, tLObject2);
        }
        return z3;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$onRequestComplete$42(TLRPC.User user) {
        getMessagesController().putUser(user, false);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$onRequestComplete$43(TLRPC.Chat chat) {
        getMessagesController().putChat(chat, false);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$onRequestComplete$44(TLRPC.Chat chat) {
        getMessagesController().putChat(chat, false);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$onRequestComplete$45(TLRPC.TL_messages_stickerSet tL_messages_stickerSet) {
        getMediaDataController().replaceStickerSet(tL_messages_stickerSet);
    }

    /* JADX WARN: Code duplicated, block: B:100:0x01a6  */
    /* JADX WARN: Code duplicated, block: B:102:0x01ab  */
    /* JADX WARN: Code duplicated, block: B:103:0x01b2  */
    /* JADX WARN: Code duplicated, block: B:105:0x01b8  */
    /* JADX WARN: Code duplicated, block: B:107:0x01c4  */
    /* JADX WARN: Code duplicated, block: B:110:0x01d9 A[LOOP:2: B:106:0x01c2->B:110:0x01d9, LOOP_END] */
    /* JADX WARN: Code duplicated, block: B:111:0x01e0 A[EDGE_INSN: B:109:0x01d7->B:111:0x01e0 BREAK  A[LOOP:2: B:106:0x01c2->B:110:0x01d9], PHI: r1 r6
  0x01e0: PHI (r1v87 byte[]) = 
  (r1v16 byte[])
  (r1v22 byte[])
  (r1v2 byte[])
  (r1v27 byte[])
  (r1v32 byte[])
  (r1v49 byte[])
  (r1v62 byte[])
  (r1v68 byte[])
  (r1v2 byte[])
  (r1v77 byte[])
  (r1v88 byte[])
  (r1v89 byte[])
 binds: [B:352:0x01e0, B:350:0x01e0, B:238:0x0489, B:348:0x01e0, B:346:0x01e0, B:341:0x01e0, B:179:0x034c, B:338:0x01e0, B:141:0x0294, B:333:0x01e0, B:109:0x01d7, B:328:0x01e0] A[DONT_GENERATE, DONT_INLINE]
  0x01e0: PHI (r6v4 org.telegram.tgnet.TLRPC$InputFileLocation[]) = 
  (r6v0 org.telegram.tgnet.TLRPC$InputFileLocation[])
  (r6v0 org.telegram.tgnet.TLRPC$InputFileLocation[])
  (r6v0 org.telegram.tgnet.TLRPC$InputFileLocation[])
  (r6v0 org.telegram.tgnet.TLRPC$InputFileLocation[])
  (r6v0 org.telegram.tgnet.TLRPC$InputFileLocation[])
  (r6v0 org.telegram.tgnet.TLRPC$InputFileLocation[])
  (r6v2 org.telegram.tgnet.TLRPC$InputFileLocation[])
  (r6v0 org.telegram.tgnet.TLRPC$InputFileLocation[])
  (r6v0 org.telegram.tgnet.TLRPC$InputFileLocation[])
  (r6v0 org.telegram.tgnet.TLRPC$InputFileLocation[])
  (r6v0 org.telegram.tgnet.TLRPC$InputFileLocation[])
  (r6v0 org.telegram.tgnet.TLRPC$InputFileLocation[])
 binds: [B:352:0x01e0, B:350:0x01e0, B:238:0x0489, B:348:0x01e0, B:346:0x01e0, B:341:0x01e0, B:179:0x034c, B:338:0x01e0, B:141:0x0294, B:333:0x01e0, B:109:0x01d7, B:328:0x01e0] A[DONT_GENERATE, DONT_INLINE]] */
    /* JADX WARN: Code duplicated, block: B:112:0x01e4  */
    /* JADX WARN: Code duplicated, block: B:114:0x01e8  */
    /* JADX WARN: Code duplicated, block: B:116:0x0207  */
    /* JADX WARN: Code duplicated, block: B:137:0x0274 A[LOOP:3: B:115:0x0205->B:137:0x0274, LOOP_END] */
    /* JADX WARN: Code duplicated, block: B:138:0x0277  */
    /* JADX WARN: Code duplicated, block: B:140:0x027b  */
    /* JADX WARN: Code duplicated, block: B:142:0x0296  */
    /* JADX WARN: Code duplicated, block: B:144:0x029f  */
    /* JADX WARN: Code duplicated, block: B:145:0x02ae  */
    /* JADX WARN: Code duplicated, block: B:147:0x02b4  */
    /* JADX WARN: Code duplicated, block: B:148:0x02bc  */
    /* JADX WARN: Code duplicated, block: B:150:0x02c4  */
    /* JADX WARN: Code duplicated, block: B:152:0x02d2  */
    /* JADX WARN: Code duplicated, block: B:155:0x02e8 A[LOOP:4: B:151:0x02d0->B:155:0x02e8, LOOP_END] */
    /* JADX WARN: Code duplicated, block: B:156:0x02ef  */
    /* JADX WARN: Code duplicated, block: B:158:0x02f3  */
    /* JADX WARN: Code duplicated, block: B:169:0x0314 A[Catch: Exception -> 0x0332, TRY_LEAVE, TryCatch #2 {Exception -> 0x0332, blocks: (B:167:0x0310, B:169:0x0314), top: B:317:0x0310 }] */
    /* JADX WARN: Code duplicated, block: B:173:0x0334  */
    /* JADX WARN: Code duplicated, block: B:177:0x033d  */
    /* JADX WARN: Code duplicated, block: B:178:0x034b  */
    /* JADX WARN: Code duplicated, block: B:180:0x034e  */
    /* JADX WARN: Code duplicated, block: B:181:0x035c  */
    /* JADX WARN: Code duplicated, block: B:183:0x0364  */
    /* JADX WARN: Code duplicated, block: B:184:0x0381  */
    /* JADX WARN: Code duplicated, block: B:186:0x0385  */
    /* JADX WARN: Code duplicated, block: B:187:0x038e  */
    /* JADX WARN: Code duplicated, block: B:189:0x0392  */
    /* JADX WARN: Code duplicated, block: B:191:0x039e  */
    /* JADX WARN: Code duplicated, block: B:194:0x03b4 A[LOOP:5: B:190:0x039c->B:194:0x03b4, LOOP_END] */
    /* JADX WARN: Code duplicated, block: B:195:0x03bc  */
    /* JADX WARN: Code duplicated, block: B:197:0x03c0  */
    /* JADX WARN: Code duplicated, block: B:199:0x03d4  */
    /* JADX WARN: Code duplicated, block: B:201:0x03d8  */
    /* JADX WARN: Code duplicated, block: B:202:0x03ec  */
    /* JADX WARN: Code duplicated, block: B:204:0x03f4  */
    /* JADX WARN: Code duplicated, block: B:206:0x03ff  */
    /* JADX WARN: Code duplicated, block: B:208:0x0408  */
    /* JADX WARN: Code duplicated, block: B:210:0x0412  */
    /* JADX WARN: Code duplicated, block: B:211:0x0419  */
    /* JADX WARN: Code duplicated, block: B:213:0x041d  */
    /* JADX WARN: Code duplicated, block: B:216:0x0427 A[LOOP:6: B:207:0x0406->B:216:0x0427, LOOP_END] */
    /* JADX WARN: Code duplicated, block: B:217:0x042a  */
    /* JADX WARN: Code duplicated, block: B:219:0x042e  */
    /* JADX WARN: Code duplicated, block: B:21:0x0040  */
    /* JADX WARN: Code duplicated, block: B:221:0x0439  */
    /* JADX WARN: Code duplicated, block: B:223:0x0442  */
    /* JADX WARN: Code duplicated, block: B:226:0x0452 A[LOOP:7: B:222:0x0440->B:226:0x0452, LOOP_END] */
    /* JADX WARN: Code duplicated, block: B:227:0x0455  */
    /* JADX WARN: Code duplicated, block: B:229:0x0459  */
    /* JADX WARN: Code duplicated, block: B:231:0x0465  */
    /* JADX WARN: Code duplicated, block: B:234:0x047a A[LOOP:8: B:230:0x0463->B:234:0x047a, LOOP_END] */
    /* JADX WARN: Code duplicated, block: B:235:0x0482  */
    /* JADX WARN: Code duplicated, block: B:237:0x0486  */
    /* JADX WARN: Code duplicated, block: B:239:0x048b  */
    /* JADX WARN: Code duplicated, block: B:23:0x0049  */
    /* JADX WARN: Code duplicated, block: B:241:0x0494  */
    /* JADX WARN: Code duplicated, block: B:244:0x04ab A[LOOP:9: B:240:0x0492->B:244:0x04ab, LOOP_END] */
    /* JADX WARN: Code duplicated, block: B:245:0x04af  */
    /* JADX WARN: Code duplicated, block: B:247:0x04b3  */
    /* JADX WARN: Code duplicated, block: B:249:0x04bf  */
    /* JADX WARN: Code duplicated, block: B:24:0x0058  */
    /* JADX WARN: Code duplicated, block: B:252:0x04d6 A[LOOP:10: B:248:0x04bd->B:252:0x04d6, LOOP_END] */
    /* JADX WARN: Code duplicated, block: B:253:0x04da  */
    /* JADX WARN: Code duplicated, block: B:255:0x04de  */
    /* JADX WARN: Code duplicated, block: B:257:0x04ea  */
    /* JADX WARN: Code duplicated, block: B:260:0x0503 A[LOOP:11: B:256:0x04e8->B:260:0x0503, LOOP_END] */
    /* JADX WARN: Code duplicated, block: B:261:0x0507  */
    /* JADX WARN: Code duplicated, block: B:263:0x050f  */
    /* JADX WARN: Code duplicated, block: B:265:0x051b  */
    /* JADX WARN: Code duplicated, block: B:268:0x052b A[LOOP:12: B:264:0x0519->B:268:0x052b, LOOP_END] */
    /* JADX WARN: Code duplicated, block: B:269:0x052e  */
    /* JADX WARN: Code duplicated, block: B:26:0x0062  */
    /* JADX WARN: Code duplicated, block: B:271:0x0532  */
    /* JADX WARN: Code duplicated, block: B:287:0x0575  */
    /* JADX WARN: Code duplicated, block: B:290:0x057c  */
    /* JADX WARN: Code duplicated, block: B:292:0x0584  */
    /* JADX WARN: Code duplicated, block: B:294:0x0588  */
    /* JADX WARN: Code duplicated, block: B:295:0x05ba  */
    /* JADX WARN: Code duplicated, block: B:29:0x006a  */
    /* JADX WARN: Code duplicated, block: B:304:0x0615 A[RETURN] */
    /* JADX WARN: Code duplicated, block: B:305:0x0616  */
    /* JADX WARN: Code duplicated, block: B:307:0x061a  */
    /* JADX WARN: Code duplicated, block: B:31:0x0074  */
    /* JADX WARN: Code duplicated, block: B:322:0x017f A[ADDED_TO_REGION, REMOVE, SYNTHETIC] */
    /* JADX WARN: Code duplicated, block: B:326:0x017f A[EDGE_INSN: B:326:0x017f->B:94:0x017f BREAK  A[LOOP:1: B:38:0x009b->B:66:0x010f], SYNTHETIC] */
    /* JADX WARN: Code duplicated, block: B:327:0x0114 A[SYNTHETIC] */
    /* JADX WARN: Code duplicated, block: B:329:0x01d7 A[SYNTHETIC] */
    /* JADX WARN: Code duplicated, block: B:332:0x01d7 A[SYNTHETIC] */
    /* JADX WARN: Code duplicated, block: B:339:0x01d7 A[SYNTHETIC] */
    /* JADX WARN: Code duplicated, block: B:33:0x007f  */
    /* JADX WARN: Code duplicated, block: B:340:0x01d7 A[SYNTHETIC] */
    /* JADX WARN: Code duplicated, block: B:342:0x0067 A[EDGE_INSN: B:342:0x0067->B:28:0x0067 BREAK  A[LOOP:6: B:207:0x0406->B:216:0x0427], SYNTHETIC] */
    /* JADX WARN: Code duplicated, block: B:344:0x0067 A[EDGE_INSN: B:344:0x0067->B:28:0x0067 BREAK  A[LOOP:7: B:222:0x0440->B:226:0x0452], SYNTHETIC] */
    /* JADX WARN: Code duplicated, block: B:347:0x01d7 A[SYNTHETIC] */
    /* JADX WARN: Code duplicated, block: B:349:0x01d7 A[SYNTHETIC] */
    /* JADX WARN: Code duplicated, block: B:351:0x01d7 A[SYNTHETIC] */
    /* JADX WARN: Code duplicated, block: B:352:0x01e0 A[SYNTHETIC] */
    /* JADX WARN: Code duplicated, block: B:353:0x0500 A[SYNTHETIC] */
    /* JADX WARN: Code duplicated, block: B:355:0x0067 A[EDGE_INSN: B:355:0x0067->B:28:0x0067 BREAK  A[LOOP:12: B:264:0x0519->B:268:0x052b], SYNTHETIC] */
    /* JADX WARN: Code duplicated, block: B:35:0x0088  */
    /* JADX WARN: Code duplicated, block: B:37:0x0097  */
    /* JADX WARN: Code duplicated, block: B:40:0x00a3  */
    /* JADX WARN: Code duplicated, block: B:63:0x0109  */
    /* JADX WARN: Code duplicated, block: B:66:0x010f A[LOOP:1: B:38:0x009b->B:66:0x010f, LOOP_END] */
    /* JADX WARN: Code duplicated, block: B:68:0x0118  */
    /* JADX WARN: Code duplicated, block: B:70:0x011c  */
    /* JADX WARN: Code duplicated, block: B:72:0x0120  */
    /* JADX WARN: Code duplicated, block: B:74:0x012f  */
    /* JADX WARN: Code duplicated, block: B:76:0x0133  */
    /* JADX WARN: Code duplicated, block: B:78:0x0142  */
    /* JADX WARN: Code duplicated, block: B:79:0x014d  */
    /* JADX WARN: Code duplicated, block: B:81:0x0155  */
    /* JADX WARN: Code duplicated, block: B:82:0x015a  */
    /* JADX WARN: Code duplicated, block: B:84:0x015e  */
    /* JADX WARN: Code duplicated, block: B:86:0x0164  */
    /* JADX WARN: Code duplicated, block: B:89:0x016f  */
    /* JADX WARN: Code duplicated, block: B:93:0x0179  */
    /* JADX WARN: Code duplicated, block: B:97:0x0188  */
    /* JADX WARN: Code duplicated, block: B:99:0x019f  */
    private Pair<byte[], TLRPC.InputFileLocation> getFileReferenceFromResponse(TLRPC.InputFileLocation inputFileLocation, String str, String str2, TLObject tLObject, Object... objArr) {
        TLRPC.InputFileLocation[] inputFileLocationArr;
        boolean[] zArr;
        TLObject tLObject2;
        byte[] fileReference;
        FileRefController fileRefController;
        TLRPC.InputFileLocation inputFileLocation2;
        TLRPC.InputFileLocation[] inputFileLocationArr2;
        boolean[] zArr2;
        boolean[] zArr3;
        TLRPC.InputFileLocation inputFileLocation3;
        TLRPC.InputFileLocation inputFileLocation4;
        FileRefController fileRefController2;
        TL_stories.TL_stories_stories tL_stories_stories;
        TL_stories.StoryItem storyItem;
        Object obj;
        Object obj2;
        TL_stories.StoryItem storyItem2;
        TLRPC.User user;
        TLRPC.MessageMedia messageMedia;
        TLRPC.MessageMedia messageMedia2;
        TLRPC.Document document;
        TLRPC.Photo photo;
        TLRPC.Photo photo2;
        TLRPC.photos_Photos photos_photos;
        int size;
        int i;
        TLRPC.TL_messages_favedStickers tL_messages_favedStickers;
        int size2;
        int i2;
        byte[] fileReference2;
        TLRPC.TL_messages_recentStickers tL_messages_recentStickers;
        int size3;
        int i3;
        byte[] fileReference3;
        TLRPC.TL_messages_stickerSet tL_messages_stickerSet;
        int size4;
        int i4;
        TLRPC.TL_messages_savedGifs tL_messages_savedGifs;
        int size5;
        int i5;
        TLRPC.TL_messages_chats tL_messages_chats;
        int size6;
        int i6;
        Vector vector;
        int size7;
        int i7;
        Object obj3;
        TLRPC.InputFileLocation[] inputFileLocationArr3;
        byte[] fileReference4;
        TL_account.TL_wallPapers tL_wallPapers;
        int size8;
        int i8;
        TLRPC.TL_help_appUpdate tL_help_appUpdate;
        boolean[] zArr4;
        TLRPC.Document document2;
        TLRPC.InputFileLocation[] inputFileLocationArr4;
        ArrayList arrayList;
        int size9;
        int i9;
        int i10;
        TLRPC.UserFull userFull;
        TL_bots.BotInfo botInfo;
        FileRefController fileRefController3;
        TLRPC.InputFileLocation inputFileLocation5;
        ArrayList arrayList2;
        int size10;
        int i11;
        int i12;
        TLRPC.TL_availableReaction tL_availableReaction;
        boolean[] zArr5;
        ArrayList arrayList3;
        int size11;
        int i13;
        int i14;
        TLRPC.messages_Messages messages_messages;
        int size12;
        int i15;
        FileRefController fileRefController4;
        TLRPC.Message message;
        TLRPC.MessageMedia messageMedia3;
        boolean[] zArr6;
        FileRefController fileRefController5;
        TLRPC.MessageAction messageAction;
        TLRPC.Document document3;
        TLRPC.TL_game tL_game;
        TLRPC.Photo photo3;
        TLRPC.WebPage webPage;
        byte[] fileReference5;
        TLRPC.Photo photo4;
        TLRPC.TL_messageMediaPaidMedia tL_messageMediaPaidMedia;
        int i16;
        TLRPC.MessageExtendedMedia messageExtendedMedia;
        TLRPC.MessageMedia messageMedia4;
        TLRPC.Photo photo5;
        TLRPC.InputFileLocation inputFileLocation6;
        TLRPC.MessageMedia messageMedia5;
        TLRPC.Document document4;
        FileRefController fileRefController6;
        boolean[] zArr7;
        TLRPC.Photo photo6;
        TLRPC.InputFileLocation inputFileLocation7 = null;
        if ((inputFileLocation instanceof TLRPC.TL_inputFileLocation) || (inputFileLocation instanceof TLRPC.TL_inputPeerPhotoFileLocation)) {
            inputFileLocationArr = new TLRPC.InputFileLocation[1];
            zArr = new boolean[1];
        } else {
            inputFileLocationArr = null;
            zArr = null;
        }
        if (str2 != null) {
            tLObject2 = tLObject;
            Pair<byte[], TLRPC.InputFileLocation> fileReferenceFromResponse = getFileReferenceFromResponse(inputFileLocation, str, null, tLObject2, objArr);
            if (fileReferenceFromResponse != null) {
                fileReference = (byte[]) fileReferenceFromResponse.first;
                Object obj4 = fileReferenceFromResponse.second;
                if (obj4 != null && inputFileLocationArr != null) {
                    inputFileLocationArr[0] = (TLRPC.InputFileLocation) obj4;
                }
            }
            if (tLObject2 instanceof StoriesController.BotPreview) {
                messageMedia5 = ((StoriesController.BotPreview) tLObject2).media;
                document4 = messageMedia5.document;
                if (document4 != null) {
                    inputFileLocationArr2 = inputFileLocationArr;
                    byte[] fileReference6 = getFileReference(document4, messageMedia5.alt_documents, inputFileLocation, zArr, inputFileLocationArr2);
                    fileRefController6 = this;
                    fileReference = fileReference6;
                } else {
                    fileRefController6 = this;
                    inputFileLocationArr2 = inputFileLocationArr;
                    zArr7 = zArr;
                    photo6 = messageMedia5.photo;
                    if (photo6 != null) {
                        fileReference = fileRefController6.getFileReference(photo6, inputFileLocation, zArr7, inputFileLocationArr2);
                    }
                }
            } else {
                fileRefController = this;
                inputFileLocation2 = inputFileLocation;
                inputFileLocationArr2 = inputFileLocationArr;
                zArr2 = zArr;
                if (tLObject2 instanceof TLRPC.messages_Messages) {
                    messages_messages = (TLRPC.messages_Messages) tLObject2;
                    if (!messages_messages.messages.isEmpty()) {
                        size12 = messages_messages.messages.size();
                        i15 = 0;
                        while (i15 < size12) {
                            message = (TLRPC.Message) messages_messages.messages.get(i15);
                            messageMedia3 = message.media;
                            if (messageMedia3 instanceof TLRPC.TL_messageMediaPaidMedia) {
                                tL_messageMediaPaidMedia = (TLRPC.TL_messageMediaPaidMedia) messageMedia3;
                                i16 = 0;
                                while (true) {
                                    if (i16 < tL_messageMediaPaidMedia.extended_media.size()) {
                                        zArr6 = zArr2;
                                        fileRefController5 = fileRefController;
                                        break;
                                    }
                                    messageExtendedMedia = tL_messageMediaPaidMedia.extended_media.get(i16);
                                    if ((messageExtendedMedia instanceof TLRPC.TL_messageExtendedMedia) || (messageMedia4 = ((TLRPC.TL_messageExtendedMedia) messageExtendedMedia).media) == null) {
                                        zArr6 = zArr2;
                                        fileRefController5 = fileRefController;
                                    } else {
                                        TLRPC.Document document5 = messageMedia4.document;
                                        if (document5 != null) {
                                            boolean[] zArr8 = zArr2;
                                            byte[] fileReference7 = getFileReference(document5, messageMedia4.alt_documents, inputFileLocation2, zArr8, inputFileLocationArr2);
                                            fileRefController5 = this;
                                            inputFileLocation2 = inputFileLocation;
                                            fileReference = fileReference7;
                                            zArr6 = zArr8;
                                        } else {
                                            boolean[] zArr9 = zArr2;
                                            TLRPC.TL_game tL_game2 = messageMedia4.game;
                                            if (tL_game2 != null) {
                                                inputFileLocation2 = inputFileLocation;
                                                byte[] fileReference8 = getFileReference(tL_game2.document, null, inputFileLocation2, zArr9, inputFileLocationArr2);
                                                fileRefController5 = this;
                                                zArr6 = zArr9;
                                                fileReference = fileReference8 == null ? fileRefController5.getFileReference(messageMedia4.game.photo, inputFileLocation2, zArr6, inputFileLocationArr2) : fileReference8;
                                            } else {
                                                fileRefController5 = this;
                                                inputFileLocation2 = inputFileLocation;
                                                zArr6 = zArr9;
                                                TLRPC.Photo photo7 = messageMedia4.photo;
                                                if (photo7 != null) {
                                                    fileReference = fileRefController5.getFileReference(photo7, inputFileLocation2, zArr6, inputFileLocationArr2);
                                                } else {
                                                    TLRPC.WebPage webPage2 = messageMedia4.webpage;
                                                    if (webPage2 != null) {
                                                        fileReference = fileRefController5.getFileReference(webPage2, inputFileLocation2, zArr6, inputFileLocationArr2);
                                                    }
                                                }
                                            }
                                        }
                                        if (fileReference == null && (photo5 = messageMedia4.video_cover) != null) {
                                            fileReference = fileRefController5.getFileReference(photo5, inputFileLocation2, zArr6, inputFileLocationArr2);
                                        }
                                    }
                                    if (fileReference != null) {
                                        break;
                                    }
                                    i16++;
                                    fileRefController = fileRefController5;
                                    zArr2 = zArr6;
                                }
                            } else {
                                zArr6 = zArr2;
                                fileRefController5 = fileRefController;
                                if (messageMedia3 != null) {
                                    document3 = messageMedia3.document;
                                    if (document3 != null) {
                                        fileReference5 = getFileReference(document3, messageMedia3.alt_documents, inputFileLocation2, zArr6, inputFileLocationArr2);
                                        fileRefController5 = this;
                                        inputFileLocation2 = inputFileLocation;
                                    } else {
                                        tL_game = messageMedia3.game;
                                        if (tL_game != null) {
                                            inputFileLocation2 = inputFileLocation;
                                            fileReference5 = getFileReference(tL_game.document, null, inputFileLocation2, zArr6, inputFileLocationArr2);
                                            fileRefController5 = this;
                                            if (fileReference5 == null) {
                                                fileReference = fileRefController5.getFileReference(message.media.game.photo, inputFileLocation2, zArr6, inputFileLocationArr2);
                                            }
                                        } else {
                                            fileRefController5 = this;
                                            inputFileLocation2 = inputFileLocation;
                                            photo3 = messageMedia3.photo;
                                            if (photo3 != null) {
                                                fileReference = fileRefController5.getFileReference(photo3, inputFileLocation2, zArr6, inputFileLocationArr2);
                                            } else {
                                                webPage = messageMedia3.webpage;
                                                if (webPage != null) {
                                                    fileReference = fileRefController5.getFileReference(webPage, inputFileLocation2, zArr6, inputFileLocationArr2);
                                                }
                                            }
                                        }
                                        if (fileReference != null && (photo4 = message.media.video_cover) != null) {
                                            fileReference = fileRefController5.getFileReference(photo4, inputFileLocation2, zArr6, inputFileLocationArr2);
                                        }
                                    }
                                    fileReference = fileReference5;
                                    if (fileReference != null) {
                                    }
                                } else {
                                    messageAction = message.action;
                                    if (!(messageAction instanceof TLRPC.TL_messageActionChatEditPhoto) || (messageAction instanceof TLRPC.TL_messageActionSuggestProfilePhoto)) {
                                        fileReference = fileRefController5.getFileReference(messageAction.photo, inputFileLocation2, zArr6, inputFileLocationArr2);
                                    }
                                }
                            }
                            i15++;
                            fileRefController = fileRefController5;
                            zArr2 = zArr6;
                        }
                        fileRefController4 = fileRefController;
                        if (fileReference == null) {
                            fileRefController4.getMessagesStorage().replaceMessageIfExists((TLRPC.Message) messages_messages.messages.get(0), messages_messages.users, messages_messages.chats, true);
                            if (BuildVars.DEBUG_VERSION) {
                                FileLog.d("file ref not found in messages, replacing message");
                            }
                        }
                    } else if (BuildVars.DEBUG_VERSION) {
                        FileLog.d("empty messages, file ref not found");
                    }
                } else {
                    zArr3 = zArr2;
                    if (tLObject2 instanceof TLRPC.TL_help_premiumPromo) {
                        arrayList3 = ((TLRPC.TL_help_premiumPromo) tLObject2).videos;
                        size11 = arrayList3.size();
                        i13 = 0;
                        while (i13 < size11) {
                            i14 = i13 + 1;
                            fileReference3 = getFileReference((TLRPC.Document) arrayList3.get(i13), null, inputFileLocation2, zArr3, inputFileLocationArr2);
                            if (fileReference3 != null) {
                                fileReference = fileReference3;
                                break;
                            }
                            inputFileLocation2 = inputFileLocation;
                            fileReference = fileReference3;
                            i13 = i14;
                        }
                    } else {
                        if (tLObject2 instanceof TLRPC.TL_messages_availableReactions) {
                            TLRPC.TL_messages_availableReactions tL_messages_availableReactions = (TLRPC.TL_messages_availableReactions) tLObject2;
                            getMediaDataController().processLoadedReactions(tL_messages_availableReactions.reactions, tL_messages_availableReactions.hash, (int) (System.currentTimeMillis() / 1000), false);
                            arrayList2 = tL_messages_availableReactions.reactions;
                            size10 = arrayList2.size();
                            i11 = 0;
                            while (i11 < size10) {
                                i12 = i11 + 1;
                                tL_availableReaction = (TLRPC.TL_availableReaction) arrayList2.get(i11);
                                zArr5 = zArr3;
                                fileReference3 = getFileReference(tL_availableReaction.static_icon, null, inputFileLocation, zArr5, inputFileLocationArr2);
                                if (fileReference3 == null && (fileReference3 = getFileReference(tL_availableReaction.appear_animation, null, inputFileLocation, zArr5, inputFileLocationArr2)) == null && (fileReference3 = getFileReference(tL_availableReaction.select_animation, null, inputFileLocation, zArr5, inputFileLocationArr2)) == null && (fileReference3 = getFileReference(tL_availableReaction.activate_animation, null, inputFileLocation, zArr5, inputFileLocationArr2)) == null && (fileReference3 = getFileReference(tL_availableReaction.effect_animation, null, inputFileLocation, zArr5, inputFileLocationArr2)) == null && (fileReference3 = getFileReference(tL_availableReaction.around_animation, null, inputFileLocation, zArr5, inputFileLocationArr2)) == null) {
                                    fileReference3 = getFileReference(tL_availableReaction.center_icon, null, inputFileLocation, zArr5, inputFileLocationArr2);
                                    zArr3 = zArr5;
                                    if (fileReference3 != null) {
                                        fileReference = fileReference3;
                                        i11 = i12;
                                    }
                                }
                                fileReference = fileReference3;
                                break;
                            }
                        }
                        if (tLObject2 instanceof TLRPC.TL_users_userFull) {
                            TLRPC.TL_users_userFull tL_users_userFull = (TLRPC.TL_users_userFull) tLObject2;
                            getMessagesController().putUsers(tL_users_userFull.users, false);
                            getMessagesController().putChats(tL_users_userFull.chats, false);
                            userFull = tL_users_userFull.full_user;
                            botInfo = userFull.bot_info;
                            if (botInfo != null) {
                                getMessagesStorage().updateUserInfo(userFull, true);
                                if (fileReference == null) {
                                    inputFileLocation5 = inputFileLocation;
                                    fileRefController3 = this;
                                    fileReference = getFileReference(botInfo.description_document, null, inputFileLocation5, zArr3, inputFileLocationArr2);
                                } else {
                                    fileRefController3 = this;
                                    inputFileLocation5 = inputFileLocation;
                                }
                                if (fileReference == null) {
                                    fileReference = fileRefController3.getFileReference(botInfo.description_photo, inputFileLocation5, zArr3, inputFileLocationArr2);
                                }
                            }
                        } else if (tLObject2 instanceof TLRPC.TL_attachMenuBotsBot) {
                            arrayList = ((TLRPC.TL_attachMenuBotsBot) tLObject2).bot.icons;
                            size9 = arrayList.size();
                            i9 = 0;
                            while (i9 < size9) {
                                i10 = i9 + 1;
                                fileReference3 = getFileReference(((TLRPC.TL_attachMenuBotIcon) arrayList.get(i9)).icon, null, inputFileLocation, zArr3, inputFileLocationArr2);
                                if (fileReference3 != null) {
                                    fileReference = fileReference3;
                                    break;
                                }
                                fileReference = fileReference3;
                                i9 = i10;
                            }
                        } else if (tLObject2 instanceof TLRPC.TL_help_appUpdate) {
                            tL_help_appUpdate = (TLRPC.TL_help_appUpdate) tLObject2;
                            try {
                                SharedConfig.pendingAppUpdate = tL_help_appUpdate;
                                SharedConfig.saveConfig();
                            } catch (Exception e) {
                                FileLog.e(e);
                            }
                            try {
                                NotificationCenter.getGlobalInstance().lambda$postNotificationNameOnUIThread$1(NotificationCenter.appUpdateAvailable, new Object[0]);
                            } catch (Exception e2) {
                                FileLog.e(e2);
                            }
                            try {
                                document2 = tL_help_appUpdate.document;
                                if (document2 != null) {
                                    fileReference = document2.file_reference;
                                    TLRPC.TL_inputDocumentFileLocation tL_inputDocumentFileLocation = new TLRPC.TL_inputDocumentFileLocation();
                                    TLRPC.Document document6 = tL_help_appUpdate.document;
                                    tL_inputDocumentFileLocation.id = document6.id;
                                    tL_inputDocumentFileLocation.access_hash = document6.access_hash;
                                    tL_inputDocumentFileLocation.file_reference = document6.file_reference;
                                    tL_inputDocumentFileLocation.thumb_size = _UrlKt.FRAGMENT_ENCODE_SET;
                                    inputFileLocationArr4 = new TLRPC.InputFileLocation[]{tL_inputDocumentFileLocation};
                                } else {
                                    inputFileLocationArr4 = inputFileLocationArr2;
                                }
                                inputFileLocationArr2 = inputFileLocationArr4;
                            } catch (Exception e3) {
                                FileLog.e(e3);
                                fileReference = null;
                            }
                            if (fileReference == null) {
                                zArr4 = zArr3;
                                fileReference = getFileReference(tL_help_appUpdate.document, null, inputFileLocation, zArr4, inputFileLocationArr2);
                            } else {
                                zArr4 = zArr3;
                            }
                            if (fileReference == null) {
                                fileReference4 = getFileReference(tL_help_appUpdate.sticker, null, inputFileLocation, zArr4, inputFileLocationArr2);
                                fileReference = fileReference4;
                            }
                        } else {
                            inputFileLocation3 = inputFileLocation;
                            if (tLObject2 instanceof TLRPC.TL_messages_webPage) {
                                TLRPC.TL_messages_webPage tL_messages_webPage = (TLRPC.TL_messages_webPage) tLObject2;
                                getMessagesController().putChats(tL_messages_webPage.chats, false);
                                getMessagesController().putUsers(tL_messages_webPage.users, false);
                                fileReference = getFileReference(tL_messages_webPage.webpage, inputFileLocation3, zArr3, inputFileLocationArr2);
                            } else if (tLObject2 instanceof TLRPC.WebPage) {
                                fileReference = getFileReference((TLRPC.WebPage) tLObject2, inputFileLocation3, zArr3, inputFileLocationArr2);
                            } else if (tLObject2 instanceof TL_account.TL_wallPapers) {
                                tL_wallPapers = (TL_account.TL_wallPapers) tLObject2;
                                size8 = tL_wallPapers.wallpapers.size();
                                i8 = 0;
                                while (i8 < size8) {
                                    fileReference3 = getFileReference(tL_wallPapers.wallpapers.get(i8).document, null, inputFileLocation3, zArr3, inputFileLocationArr2);
                                    if (fileReference3 != null) {
                                        fileReference = fileReference3;
                                        break;
                                    }
                                    i8++;
                                    inputFileLocation3 = inputFileLocation;
                                    fileReference = fileReference3;
                                }
                            } else if (tLObject2 instanceof TLRPC.TL_wallPaper) {
                                fileReference4 = getFileReference(((TLRPC.TL_wallPaper) tLObject2).document, null, inputFileLocation, zArr3, inputFileLocationArr2);
                                fileReference = fileReference4;
                            } else if (tLObject2 instanceof TLRPC.TL_theme) {
                                inputFileLocationArr3 = inputFileLocationArr2;
                                fileReference = getFileReference(((TLRPC.TL_theme) tLObject2).document, null, inputFileLocation, zArr3, inputFileLocationArr2);
                            } else {
                                inputFileLocation4 = inputFileLocation;
                                if (tLObject2 instanceof Vector) {
                                    vector = (Vector) tLObject2;
                                    if (!vector.objects.isEmpty()) {
                                        size7 = vector.objects.size();
                                        for (i7 = 0; i7 < size7; i7++) {
                                            obj3 = vector.objects.get(i7);
                                            if (obj3 instanceof TLRPC.User) {
                                                fileReference = getFileReference((TLRPC.User) obj3, inputFileLocation4, zArr3, inputFileLocationArr2);
                                            } else if (obj3 instanceof TLRPC.Chat) {
                                                fileReference = getFileReference((TLRPC.Chat) obj3, inputFileLocation4, zArr3, inputFileLocationArr2);
                                            }
                                            if (fileReference != null) {
                                                break;
                                            }
                                        }
                                    }
                                } else if (tLObject2 instanceof TLRPC.TL_messages_chats) {
                                    tL_messages_chats = (TLRPC.TL_messages_chats) tLObject2;
                                    if (!tL_messages_chats.chats.isEmpty()) {
                                        size6 = tL_messages_chats.chats.size();
                                        for (i6 = 0; i6 < size6; i6++) {
                                            fileReference = getFileReference((TLRPC.Chat) tL_messages_chats.chats.get(i6), inputFileLocation4, zArr3, inputFileLocationArr2);
                                            if (fileReference != null) {
                                                break;
                                            }
                                        }
                                    }
                                } else if (tLObject2 instanceof TLRPC.TL_messages_savedGifs) {
                                    tL_messages_savedGifs = (TLRPC.TL_messages_savedGifs) tLObject2;
                                    size5 = tL_messages_savedGifs.gifs.size();
                                    i5 = 0;
                                    while (i5 < size5) {
                                        fileReference3 = getFileReference((TLRPC.Document) tL_messages_savedGifs.gifs.get(i5), null, inputFileLocation4, zArr3, inputFileLocationArr2);
                                        if (fileReference3 != null) {
                                            fileReference = fileReference3;
                                            break;
                                        }
                                        i5++;
                                        inputFileLocation4 = inputFileLocation;
                                        fileReference = fileReference3;
                                    }
                                } else if (tLObject2 instanceof TLRPC.TL_messages_stickerSet) {
                                    tL_messages_stickerSet = (TLRPC.TL_messages_stickerSet) tLObject2;
                                    if (fileReference == null) {
                                        size4 = tL_messages_stickerSet.documents.size();
                                        i4 = 0;
                                        while (i4 < size4) {
                                            fileReference3 = getFileReference((TLRPC.Document) tL_messages_stickerSet.documents.get(i4), null, inputFileLocation, zArr3, inputFileLocationArr2);
                                            if (fileReference3 != null) {
                                                fileReference = fileReference3;
                                                break;
                                            }
                                            i4++;
                                            fileReference = fileReference3;
                                        }
                                    }
                                } else if (tLObject2 instanceof TLRPC.TL_messages_recentStickers) {
                                    tL_messages_recentStickers = (TLRPC.TL_messages_recentStickers) tLObject2;
                                    size3 = tL_messages_recentStickers.stickers.size();
                                    i3 = 0;
                                    while (i3 < size3) {
                                        fileReference3 = getFileReference((TLRPC.Document) tL_messages_recentStickers.stickers.get(i3), null, inputFileLocation, zArr3, inputFileLocationArr2);
                                        if (fileReference3 != null) {
                                            fileReference = fileReference3;
                                            break;
                                        }
                                        i3++;
                                        fileReference = fileReference3;
                                    }
                                } else if (tLObject2 instanceof TLRPC.TL_messages_favedStickers) {
                                    tL_messages_favedStickers = (TLRPC.TL_messages_favedStickers) tLObject2;
                                    size2 = tL_messages_favedStickers.stickers.size();
                                    i2 = 0;
                                    while (true) {
                                        if (i2 >= size2) {
                                            fileReference2 = getFileReference((TLRPC.Document) tL_messages_favedStickers.stickers.get(i2), null, inputFileLocation, zArr3, inputFileLocationArr2);
                                            if (fileReference2 != null) {
                                                fileReference = fileReference2;
                                                break;
                                            }
                                            i2++;
                                            fileReference = fileReference2;
                                        }
                                    }
                                } else {
                                    fileRefController2 = this;
                                    if (tLObject2 instanceof TLRPC.photos_Photos) {
                                        photos_photos = (TLRPC.photos_Photos) tLObject2;
                                        size = photos_photos.photos.size();
                                        for (i = 0; i < size; i++) {
                                            fileReference = fileRefController2.getFileReference((TLRPC.Photo) photos_photos.photos.get(i), inputFileLocation, zArr3, inputFileLocationArr2);
                                            if (fileReference != null) {
                                                break;
                                            }
                                        }
                                    } else if (tLObject2 instanceof TL_stories.TL_stories_stories) {
                                        tL_stories_stories = (TL_stories.TL_stories_stories) tLObject2;
                                        if (!tL_stories_stories.stories.isEmpty() || (messageMedia = (storyItem = tL_stories_stories.stories.get(0)).media) == null) {
                                            storyItem = null;
                                        } else {
                                            if (fileReference == null && (photo2 = messageMedia.photo) != null) {
                                                fileReference = fileRefController2.getFileReference(photo2, inputFileLocation, zArr3, inputFileLocationArr2);
                                            }
                                            if (fileReference == null && (photo = storyItem.media.video_cover) != null) {
                                                fileReference = fileRefController2.getFileReference(photo, inputFileLocation, zArr3, inputFileLocationArr2);
                                            }
                                            if (fileReference == null && (document = (messageMedia2 = storyItem.media).document) != null) {
                                                byte[] fileReference9 = fileRefController2.getFileReference(document, messageMedia2.alt_documents, inputFileLocation, zArr3, inputFileLocationArr2);
                                                fileRefController2 = fileRefController2;
                                                fileReference = fileReference9;
                                            }
                                        }
                                        obj = objArr[1];
                                        if (obj instanceof FileLoadOperation) {
                                            obj2 = ((FileLoadOperation) obj).parentObject;
                                            if (obj2 instanceof TL_stories.StoryItem) {
                                                storyItem2 = (TL_stories.StoryItem) obj2;
                                                if (storyItem == null) {
                                                    TL_stories.TL_updateStory tL_updateStory = new TL_stories.TL_updateStory();
                                                    tL_updateStory.peer = fileRefController2.getMessagesController().getPeer(storyItem2.dialogId);
                                                    TL_stories.TL_storyItemDeleted tL_storyItemDeleted = new TL_stories.TL_storyItemDeleted();
                                                    tL_updateStory.story = tL_storyItemDeleted;
                                                    tL_storyItemDeleted.id = storyItem2.id;
                                                    ArrayList<TLRPC.Update> arrayList4 = new ArrayList<>();
                                                    arrayList4.add(tL_updateStory);
                                                    fileRefController2.getMessagesController().processUpdateArray(arrayList4, null, null, false, 0);
                                                } else {
                                                    user = fileRefController2.getMessagesController().getUser(Long.valueOf(storyItem2.dialogId));
                                                    if (user != null && user.contact) {
                                                        MessagesController.getInstance(fileRefController2.currentAccount).getStoriesController().getStoriesStorage().updateStoryItem(storyItem2.dialogId, storyItem);
                                                    }
                                                }
                                                if (storyItem != null && fileReference == null) {
                                                    TL_stories.TL_updateStory tL_updateStory2 = new TL_stories.TL_updateStory();
                                                    tL_updateStory2.peer = MessagesController.getInstance(fileRefController2.currentAccount).getPeer(storyItem2.dialogId);
                                                    tL_updateStory2.story = storyItem;
                                                    ArrayList<TLRPC.Update> arrayList5 = new ArrayList<>();
                                                    arrayList5.add(tL_updateStory2);
                                                    MessagesController.getInstance(fileRefController2.currentAccount).processUpdateArray(arrayList5, null, null, false, 0);
                                                }
                                            }
                                        }
                                    }
                                }
                            }
                        }
                    }
                    if (fileReference == null) {
                        return null;
                    }
                    if (inputFileLocationArr3 != null && (inputFileLocation6 = inputFileLocationArr3[0]) != null) {
                        inputFileLocation7 = inputFileLocation6;
                    }
                    return new Pair<>(fileReference, inputFileLocation7);
                }
            }
            inputFileLocationArr3 = inputFileLocationArr2;
            if (fileReference == null) {
                return null;
            }
            if (inputFileLocationArr3 != null) {
                inputFileLocation7 = inputFileLocation6;
            }
            return new Pair<>(fileReference, inputFileLocation7);
        }
        tLObject2 = tLObject;
        fileReference = null;
        if (tLObject2 instanceof StoriesController.BotPreview) {
            messageMedia5 = ((StoriesController.BotPreview) tLObject2).media;
            document4 = messageMedia5.document;
            if (document4 != null) {
                inputFileLocationArr2 = inputFileLocationArr;
                byte[] fileReference10 = getFileReference(document4, messageMedia5.alt_documents, inputFileLocation, zArr, inputFileLocationArr2);
                fileRefController6 = this;
                fileReference = fileReference10;
            } else {
                fileRefController6 = this;
                inputFileLocationArr2 = inputFileLocationArr;
                zArr7 = zArr;
                photo6 = messageMedia5.photo;
                if (photo6 != null) {
                    fileReference = fileRefController6.getFileReference(photo6, inputFileLocation, zArr7, inputFileLocationArr2);
                }
            }
        } else {
            fileRefController = this;
            inputFileLocation2 = inputFileLocation;
            inputFileLocationArr2 = inputFileLocationArr;
            zArr2 = zArr;
            if (tLObject2 instanceof TLRPC.messages_Messages) {
                messages_messages = (TLRPC.messages_Messages) tLObject2;
                if (!messages_messages.messages.isEmpty()) {
                    size12 = messages_messages.messages.size();
                    i15 = 0;
                    while (i15 < size12) {
                        message = (TLRPC.Message) messages_messages.messages.get(i15);
                        messageMedia3 = message.media;
                        if (messageMedia3 instanceof TLRPC.TL_messageMediaPaidMedia) {
                            tL_messageMediaPaidMedia = (TLRPC.TL_messageMediaPaidMedia) messageMedia3;
                            i16 = 0;
                            while (true) {
                                if (i16 < tL_messageMediaPaidMedia.extended_media.size()) {
                                    zArr6 = zArr2;
                                    fileRefController5 = fileRefController;
                                    break;
                                    break;
                                }
                                messageExtendedMedia = tL_messageMediaPaidMedia.extended_media.get(i16);
                                if (messageExtendedMedia instanceof TLRPC.TL_messageExtendedMedia) {
                                    zArr6 = zArr2;
                                    fileRefController5 = fileRefController;
                                } else {
                                    zArr6 = zArr2;
                                    fileRefController5 = fileRefController;
                                }
                                if (fileReference != null) {
                                    break;
                                    break;
                                }
                                i16++;
                                fileRefController = fileRefController5;
                                zArr2 = zArr6;
                            }
                        } else {
                            zArr6 = zArr2;
                            fileRefController5 = fileRefController;
                            if (messageMedia3 != null) {
                                document3 = messageMedia3.document;
                                if (document3 != null) {
                                    fileReference5 = getFileReference(document3, messageMedia3.alt_documents, inputFileLocation2, zArr6, inputFileLocationArr2);
                                    fileRefController5 = this;
                                    inputFileLocation2 = inputFileLocation;
                                } else {
                                    tL_game = messageMedia3.game;
                                    if (tL_game != null) {
                                        inputFileLocation2 = inputFileLocation;
                                        fileReference5 = getFileReference(tL_game.document, null, inputFileLocation2, zArr6, inputFileLocationArr2);
                                        fileRefController5 = this;
                                        if (fileReference5 == null) {
                                            fileReference = fileRefController5.getFileReference(message.media.game.photo, inputFileLocation2, zArr6, inputFileLocationArr2);
                                        }
                                    } else {
                                        fileRefController5 = this;
                                        inputFileLocation2 = inputFileLocation;
                                        photo3 = messageMedia3.photo;
                                        if (photo3 != null) {
                                            fileReference = fileRefController5.getFileReference(photo3, inputFileLocation2, zArr6, inputFileLocationArr2);
                                        } else {
                                            webPage = messageMedia3.webpage;
                                            if (webPage != null) {
                                                fileReference = fileRefController5.getFileReference(webPage, inputFileLocation2, zArr6, inputFileLocationArr2);
                                            }
                                        }
                                    }
                                    if (fileReference != null) {
                                    }
                                }
                                fileReference = fileReference5;
                                if (fileReference != null) {
                                }
                            } else {
                                messageAction = message.action;
                                if (!(messageAction instanceof TLRPC.TL_messageActionChatEditPhoto)) {
                                    fileReference = fileRefController5.getFileReference(messageAction.photo, inputFileLocation2, zArr6, inputFileLocationArr2);
                                } else {
                                    fileReference = fileRefController5.getFileReference(messageAction.photo, inputFileLocation2, zArr6, inputFileLocationArr2);
                                }
                            }
                        }
                        i15++;
                        fileRefController = fileRefController5;
                        zArr2 = zArr6;
                    }
                    fileRefController4 = fileRefController;
                    if (fileReference == null) {
                        fileRefController4.getMessagesStorage().replaceMessageIfExists((TLRPC.Message) messages_messages.messages.get(0), messages_messages.users, messages_messages.chats, true);
                        if (BuildVars.DEBUG_VERSION) {
                            FileLog.d("file ref not found in messages, replacing message");
                        }
                    }
                } else if (BuildVars.DEBUG_VERSION) {
                    FileLog.d("empty messages, file ref not found");
                }
            } else {
                zArr3 = zArr2;
                if (tLObject2 instanceof TLRPC.TL_help_premiumPromo) {
                    arrayList3 = ((TLRPC.TL_help_premiumPromo) tLObject2).videos;
                    size11 = arrayList3.size();
                    i13 = 0;
                    while (i13 < size11) {
                        i14 = i13 + 1;
                        fileReference3 = getFileReference((TLRPC.Document) arrayList3.get(i13), null, inputFileLocation2, zArr3, inputFileLocationArr2);
                        if (fileReference3 != null) {
                            fileReference = fileReference3;
                            break;
                        }
                        inputFileLocation2 = inputFileLocation;
                        fileReference = fileReference3;
                        i13 = i14;
                    }
                } else {
                    if (tLObject2 instanceof TLRPC.TL_messages_availableReactions) {
                        TLRPC.TL_messages_availableReactions tL_messages_availableReactions2 = (TLRPC.TL_messages_availableReactions) tLObject2;
                        getMediaDataController().processLoadedReactions(tL_messages_availableReactions2.reactions, tL_messages_availableReactions2.hash, (int) (System.currentTimeMillis() / 1000), false);
                        arrayList2 = tL_messages_availableReactions2.reactions;
                        size10 = arrayList2.size();
                        i11 = 0;
                        while (i11 < size10) {
                            i12 = i11 + 1;
                            tL_availableReaction = (TLRPC.TL_availableReaction) arrayList2.get(i11);
                            zArr5 = zArr3;
                            fileReference3 = getFileReference(tL_availableReaction.static_icon, null, inputFileLocation, zArr5, inputFileLocationArr2);
                            if (fileReference3 == null) {
                                fileReference3 = getFileReference(tL_availableReaction.center_icon, null, inputFileLocation, zArr5, inputFileLocationArr2);
                                zArr3 = zArr5;
                                if (fileReference3 != null) {
                                    fileReference = fileReference3;
                                    i11 = i12;
                                }
                            }
                            fileReference = fileReference3;
                            break;
                        }
                    }
                    if (tLObject2 instanceof TLRPC.TL_users_userFull) {
                        TLRPC.TL_users_userFull tL_users_userFull2 = (TLRPC.TL_users_userFull) tLObject2;
                        getMessagesController().putUsers(tL_users_userFull2.users, false);
                        getMessagesController().putChats(tL_users_userFull2.chats, false);
                        userFull = tL_users_userFull2.full_user;
                        botInfo = userFull.bot_info;
                        if (botInfo != null) {
                            getMessagesStorage().updateUserInfo(userFull, true);
                            if (fileReference == null) {
                                inputFileLocation5 = inputFileLocation;
                                fileRefController3 = this;
                                fileReference = getFileReference(botInfo.description_document, null, inputFileLocation5, zArr3, inputFileLocationArr2);
                            } else {
                                fileRefController3 = this;
                                inputFileLocation5 = inputFileLocation;
                            }
                            if (fileReference == null) {
                                fileReference = fileRefController3.getFileReference(botInfo.description_photo, inputFileLocation5, zArr3, inputFileLocationArr2);
                            }
                        }
                    } else if (tLObject2 instanceof TLRPC.TL_attachMenuBotsBot) {
                        arrayList = ((TLRPC.TL_attachMenuBotsBot) tLObject2).bot.icons;
                        size9 = arrayList.size();
                        i9 = 0;
                        while (i9 < size9) {
                            i10 = i9 + 1;
                            fileReference3 = getFileReference(((TLRPC.TL_attachMenuBotIcon) arrayList.get(i9)).icon, null, inputFileLocation, zArr3, inputFileLocationArr2);
                            if (fileReference3 != null) {
                                fileReference = fileReference3;
                                break;
                            }
                            fileReference = fileReference3;
                            i9 = i10;
                        }
                    } else if (tLObject2 instanceof TLRPC.TL_help_appUpdate) {
                        tL_help_appUpdate = (TLRPC.TL_help_appUpdate) tLObject2;
                        SharedConfig.pendingAppUpdate = tL_help_appUpdate;
                        SharedConfig.saveConfig();
                        NotificationCenter.getGlobalInstance().lambda$postNotificationNameOnUIThread$1(NotificationCenter.appUpdateAvailable, new Object[0]);
                        document2 = tL_help_appUpdate.document;
                        if (document2 != null) {
                            fileReference = document2.file_reference;
                            TLRPC.TL_inputDocumentFileLocation tL_inputDocumentFileLocation2 = new TLRPC.TL_inputDocumentFileLocation();
                            TLRPC.Document document7 = tL_help_appUpdate.document;
                            tL_inputDocumentFileLocation2.id = document7.id;
                            tL_inputDocumentFileLocation2.access_hash = document7.access_hash;
                            tL_inputDocumentFileLocation2.file_reference = document7.file_reference;
                            tL_inputDocumentFileLocation2.thumb_size = _UrlKt.FRAGMENT_ENCODE_SET;
                            inputFileLocationArr4 = new TLRPC.InputFileLocation[]{tL_inputDocumentFileLocation2};
                        } else {
                            inputFileLocationArr4 = inputFileLocationArr2;
                        }
                        inputFileLocationArr2 = inputFileLocationArr4;
                        if (fileReference == null) {
                            zArr4 = zArr3;
                            fileReference = getFileReference(tL_help_appUpdate.document, null, inputFileLocation, zArr4, inputFileLocationArr2);
                        } else {
                            zArr4 = zArr3;
                        }
                        if (fileReference == null) {
                            fileReference4 = getFileReference(tL_help_appUpdate.sticker, null, inputFileLocation, zArr4, inputFileLocationArr2);
                            fileReference = fileReference4;
                        }
                    } else {
                        inputFileLocation3 = inputFileLocation;
                        if (tLObject2 instanceof TLRPC.TL_messages_webPage) {
                            TLRPC.TL_messages_webPage tL_messages_webPage2 = (TLRPC.TL_messages_webPage) tLObject2;
                            getMessagesController().putChats(tL_messages_webPage2.chats, false);
                            getMessagesController().putUsers(tL_messages_webPage2.users, false);
                            fileReference = getFileReference(tL_messages_webPage2.webpage, inputFileLocation3, zArr3, inputFileLocationArr2);
                        } else if (tLObject2 instanceof TLRPC.WebPage) {
                            fileReference = getFileReference((TLRPC.WebPage) tLObject2, inputFileLocation3, zArr3, inputFileLocationArr2);
                        } else if (tLObject2 instanceof TL_account.TL_wallPapers) {
                            tL_wallPapers = (TL_account.TL_wallPapers) tLObject2;
                            size8 = tL_wallPapers.wallpapers.size();
                            i8 = 0;
                            while (i8 < size8) {
                                fileReference3 = getFileReference(tL_wallPapers.wallpapers.get(i8).document, null, inputFileLocation3, zArr3, inputFileLocationArr2);
                                if (fileReference3 != null) {
                                    fileReference = fileReference3;
                                    break;
                                }
                                i8++;
                                inputFileLocation3 = inputFileLocation;
                                fileReference = fileReference3;
                            }
                        } else if (tLObject2 instanceof TLRPC.TL_wallPaper) {
                            fileReference4 = getFileReference(((TLRPC.TL_wallPaper) tLObject2).document, null, inputFileLocation, zArr3, inputFileLocationArr2);
                            fileReference = fileReference4;
                        } else if (tLObject2 instanceof TLRPC.TL_theme) {
                            inputFileLocationArr3 = inputFileLocationArr2;
                            fileReference = getFileReference(((TLRPC.TL_theme) tLObject2).document, null, inputFileLocation, zArr3, inputFileLocationArr2);
                        } else {
                            inputFileLocation4 = inputFileLocation;
                            if (tLObject2 instanceof Vector) {
                                vector = (Vector) tLObject2;
                                if (!vector.objects.isEmpty()) {
                                    size7 = vector.objects.size();
                                    while (i7 < size7) {
                                        obj3 = vector.objects.get(i7);
                                        if (obj3 instanceof TLRPC.User) {
                                            fileReference = getFileReference((TLRPC.User) obj3, inputFileLocation4, zArr3, inputFileLocationArr2);
                                        } else if (obj3 instanceof TLRPC.Chat) {
                                            fileReference = getFileReference((TLRPC.Chat) obj3, inputFileLocation4, zArr3, inputFileLocationArr2);
                                        }
                                        if (fileReference != null) {
                                            break;
                                            break;
                                        }
                                    }
                                }
                            } else if (tLObject2 instanceof TLRPC.TL_messages_chats) {
                                tL_messages_chats = (TLRPC.TL_messages_chats) tLObject2;
                                if (!tL_messages_chats.chats.isEmpty()) {
                                    size6 = tL_messages_chats.chats.size();
                                    while (i6 < size6) {
                                        fileReference = getFileReference((TLRPC.Chat) tL_messages_chats.chats.get(i6), inputFileLocation4, zArr3, inputFileLocationArr2);
                                        if (fileReference != null) {
                                            break;
                                            break;
                                        }
                                    }
                                }
                            } else if (tLObject2 instanceof TLRPC.TL_messages_savedGifs) {
                                tL_messages_savedGifs = (TLRPC.TL_messages_savedGifs) tLObject2;
                                size5 = tL_messages_savedGifs.gifs.size();
                                i5 = 0;
                                while (i5 < size5) {
                                    fileReference3 = getFileReference((TLRPC.Document) tL_messages_savedGifs.gifs.get(i5), null, inputFileLocation4, zArr3, inputFileLocationArr2);
                                    if (fileReference3 != null) {
                                        fileReference = fileReference3;
                                        break;
                                    }
                                    i5++;
                                    inputFileLocation4 = inputFileLocation;
                                    fileReference = fileReference3;
                                }
                            } else if (tLObject2 instanceof TLRPC.TL_messages_stickerSet) {
                                tL_messages_stickerSet = (TLRPC.TL_messages_stickerSet) tLObject2;
                                if (fileReference == null) {
                                    size4 = tL_messages_stickerSet.documents.size();
                                    i4 = 0;
                                    while (i4 < size4) {
                                        fileReference3 = getFileReference((TLRPC.Document) tL_messages_stickerSet.documents.get(i4), null, inputFileLocation, zArr3, inputFileLocationArr2);
                                        if (fileReference3 != null) {
                                            fileReference = fileReference3;
                                            break;
                                        }
                                        i4++;
                                        fileReference = fileReference3;
                                    }
                                }
                            } else if (tLObject2 instanceof TLRPC.TL_messages_recentStickers) {
                                tL_messages_recentStickers = (TLRPC.TL_messages_recentStickers) tLObject2;
                                size3 = tL_messages_recentStickers.stickers.size();
                                i3 = 0;
                                while (i3 < size3) {
                                    fileReference3 = getFileReference((TLRPC.Document) tL_messages_recentStickers.stickers.get(i3), null, inputFileLocation, zArr3, inputFileLocationArr2);
                                    if (fileReference3 != null) {
                                        fileReference = fileReference3;
                                        break;
                                    }
                                    i3++;
                                    fileReference = fileReference3;
                                }
                            } else if (tLObject2 instanceof TLRPC.TL_messages_favedStickers) {
                                tL_messages_favedStickers = (TLRPC.TL_messages_favedStickers) tLObject2;
                                size2 = tL_messages_favedStickers.stickers.size();
                                i2 = 0;
                                while (true) {
                                    if (i2 >= size2) {
                                        fileReference2 = getFileReference((TLRPC.Document) tL_messages_favedStickers.stickers.get(i2), null, inputFileLocation, zArr3, inputFileLocationArr2);
                                        if (fileReference2 != null) {
                                            fileReference = fileReference2;
                                            break;
                                        }
                                        i2++;
                                        fileReference = fileReference2;
                                    }
                                }
                            } else {
                                fileRefController2 = this;
                                if (tLObject2 instanceof TLRPC.photos_Photos) {
                                    photos_photos = (TLRPC.photos_Photos) tLObject2;
                                    size = photos_photos.photos.size();
                                    while (i < size) {
                                        fileReference = fileRefController2.getFileReference((TLRPC.Photo) photos_photos.photos.get(i), inputFileLocation, zArr3, inputFileLocationArr2);
                                        if (fileReference != null) {
                                            break;
                                            break;
                                        }
                                    }
                                } else if (tLObject2 instanceof TL_stories.TL_stories_stories) {
                                    tL_stories_stories = (TL_stories.TL_stories_stories) tLObject2;
                                    if (tL_stories_stories.stories.isEmpty()) {
                                        storyItem = null;
                                    } else {
                                        storyItem = null;
                                    }
                                    obj = objArr[1];
                                    if (obj instanceof FileLoadOperation) {
                                        obj2 = ((FileLoadOperation) obj).parentObject;
                                        if (obj2 instanceof TL_stories.StoryItem) {
                                            storyItem2 = (TL_stories.StoryItem) obj2;
                                            if (storyItem == null) {
                                                TL_stories.TL_updateStory tL_updateStory3 = new TL_stories.TL_updateStory();
                                                tL_updateStory3.peer = fileRefController2.getMessagesController().getPeer(storyItem2.dialogId);
                                                TL_stories.TL_storyItemDeleted tL_storyItemDeleted2 = new TL_stories.TL_storyItemDeleted();
                                                tL_updateStory3.story = tL_storyItemDeleted2;
                                                tL_storyItemDeleted2.id = storyItem2.id;
                                                ArrayList<TLRPC.Update> arrayList6 = new ArrayList<>();
                                                arrayList6.add(tL_updateStory3);
                                                fileRefController2.getMessagesController().processUpdateArray(arrayList6, null, null, false, 0);
                                            } else {
                                                user = fileRefController2.getMessagesController().getUser(Long.valueOf(storyItem2.dialogId));
                                                if (user != null) {
                                                    MessagesController.getInstance(fileRefController2.currentAccount).getStoriesController().getStoriesStorage().updateStoryItem(storyItem2.dialogId, storyItem);
                                                }
                                            }
                                            if (storyItem != null) {
                                                TL_stories.TL_updateStory tL_updateStory4 = new TL_stories.TL_updateStory();
                                                tL_updateStory4.peer = MessagesController.getInstance(fileRefController2.currentAccount).getPeer(storyItem2.dialogId);
                                                tL_updateStory4.story = storyItem;
                                                ArrayList<TLRPC.Update> arrayList7 = new ArrayList<>();
                                                arrayList7.add(tL_updateStory4);
                                                MessagesController.getInstance(fileRefController2.currentAccount).processUpdateArray(arrayList7, null, null, false, 0);
                                            }
                                        }
                                    }
                                }
                            }
                        }
                    }
                }
                if (fileReference == null) {
                    return null;
                }
                if (inputFileLocationArr3 != null) {
                    inputFileLocation7 = inputFileLocation6;
                }
                return new Pair<>(fileReference, inputFileLocation7);
            }
        }
        inputFileLocationArr3 = inputFileLocationArr2;
        if (fileReference == null) {
            return null;
        }
        if (inputFileLocationArr3 != null) {
            inputFileLocation7 = inputFileLocation6;
        }
        return new Pair<>(fileReference, inputFileLocation7);
    }

    private boolean updateFileReferenceFromCache(byte[] bArr, TLRPC.InputFileLocation inputFileLocation, TLRPC.InputFileLocation inputFileLocation2, String str, Object... objArr) {
        String strBytesToHex;
        Object obj = objArr[0];
        if (obj instanceof TL_stories.TL_storyItem) {
            ((TL_stories.TL_storyItem) obj).media.document.file_reference = bArr;
            return true;
        }
        if (obj instanceof TLRPC.TL_inputSingleMedia) {
            return false;
        }
        if (objArr.length >= 2) {
            Object obj2 = objArr[1];
            if ((obj2 instanceof TLRPC.TL_messages_sendMedia) && (((TLRPC.TL_messages_sendMedia) obj2).media instanceof TLRPC.TL_inputMediaPaidMedia) && ((obj instanceof TLRPC.TL_inputMediaPhoto) || (obj instanceof TLRPC.TL_inputMediaDocument))) {
                return false;
            }
        }
        if (obj instanceof TLRPC.TL_messages_sendMedia) {
            TLRPC.InputMedia inputMedia = ((TLRPC.TL_messages_sendMedia) obj).media;
            if (inputMedia instanceof TLRPC.TL_inputMediaDocument) {
                TLRPC.TL_inputMediaDocument tL_inputMediaDocument = (TLRPC.TL_inputMediaDocument) inputMedia;
                if (isSameReference(tL_inputMediaDocument.id.file_reference, bArr)) {
                    return false;
                }
                tL_inputMediaDocument.id.file_reference = bArr;
            } else if (inputMedia instanceof TLRPC.TL_inputMediaPhoto) {
                TLRPC.TL_inputMediaPhoto tL_inputMediaPhoto = (TLRPC.TL_inputMediaPhoto) inputMedia;
                if (isSameReference(tL_inputMediaPhoto.id.file_reference, bArr)) {
                    return false;
                }
                tL_inputMediaPhoto.id.file_reference = bArr;
            }
        } else if (obj instanceof TLRPC.TL_messages_editMessage) {
            TLRPC.InputMedia inputMedia2 = ((TLRPC.TL_messages_editMessage) obj).media;
            if (inputMedia2 instanceof TLRPC.TL_inputMediaDocument) {
                TLRPC.TL_inputMediaDocument tL_inputMediaDocument2 = (TLRPC.TL_inputMediaDocument) inputMedia2;
                if (isSameReference(tL_inputMediaDocument2.id.file_reference, bArr)) {
                    return false;
                }
                tL_inputMediaDocument2.id.file_reference = bArr;
            } else if (inputMedia2 instanceof TLRPC.TL_inputMediaPhoto) {
                TLRPC.TL_inputMediaPhoto tL_inputMediaPhoto2 = (TLRPC.TL_inputMediaPhoto) inputMedia2;
                if (isSameReference(tL_inputMediaPhoto2.id.file_reference, bArr)) {
                    return false;
                }
                tL_inputMediaPhoto2.id.file_reference = bArr;
            }
        } else if (obj instanceof TLRPC.TL_messages_saveGif) {
            TLRPC.TL_messages_saveGif tL_messages_saveGif = (TLRPC.TL_messages_saveGif) obj;
            if (isSameReference(tL_messages_saveGif.id.file_reference, bArr)) {
                return false;
            }
            tL_messages_saveGif.id.file_reference = bArr;
        } else if (obj instanceof TLRPC.TL_messages_saveRecentSticker) {
            TLRPC.TL_messages_saveRecentSticker tL_messages_saveRecentSticker = (TLRPC.TL_messages_saveRecentSticker) obj;
            if (isSameReference(tL_messages_saveRecentSticker.id.file_reference, bArr)) {
                return false;
            }
            tL_messages_saveRecentSticker.id.file_reference = bArr;
        } else if (obj instanceof TLRPC.TL_stickers_addStickerToSet) {
            TLRPC.TL_stickers_addStickerToSet tL_stickers_addStickerToSet = (TLRPC.TL_stickers_addStickerToSet) obj;
            if (isSameReference(tL_stickers_addStickerToSet.sticker.document.file_reference, bArr)) {
                return false;
            }
            tL_stickers_addStickerToSet.sticker.document.file_reference = bArr;
        } else if (obj instanceof TLRPC.TL_messages_faveSticker) {
            TLRPC.TL_messages_faveSticker tL_messages_faveSticker = (TLRPC.TL_messages_faveSticker) obj;
            if (isSameReference(tL_messages_faveSticker.id.file_reference, bArr)) {
                return false;
            }
            tL_messages_faveSticker.id.file_reference = bArr;
        } else if (obj instanceof TLRPC.TL_messages_getAttachedStickers) {
            TLRPC.InputStickeredMedia inputStickeredMedia = ((TLRPC.TL_messages_getAttachedStickers) obj).media;
            if (inputStickeredMedia instanceof TLRPC.TL_inputStickeredMediaDocument) {
                TLRPC.TL_inputStickeredMediaDocument tL_inputStickeredMediaDocument = (TLRPC.TL_inputStickeredMediaDocument) inputStickeredMedia;
                if (isSameReference(tL_inputStickeredMediaDocument.id.file_reference, bArr)) {
                    return false;
                }
                tL_inputStickeredMediaDocument.id.file_reference = bArr;
            } else if (inputStickeredMedia instanceof TLRPC.TL_inputStickeredMediaPhoto) {
                TLRPC.TL_inputStickeredMediaPhoto tL_inputStickeredMediaPhoto = (TLRPC.TL_inputStickeredMediaPhoto) inputStickeredMedia;
                if (isSameReference(tL_inputStickeredMediaPhoto.id.file_reference, bArr)) {
                    return false;
                }
                tL_inputStickeredMediaPhoto.id.file_reference = bArr;
            }
        } else {
            Object obj3 = objArr[1];
            if (obj3 instanceof FileLoadOperation) {
                FileLoadOperation fileLoadOperation = (FileLoadOperation) obj3;
                String strBytesToHex2 = null;
                if (inputFileLocation != null) {
                    if (isSameReference(fileLoadOperation.location.file_reference, inputFileLocation.file_reference)) {
                        return false;
                    }
                    strBytesToHex = BuildVars.LOGS_ENABLED ? Utilities.bytesToHex(fileLoadOperation.location.file_reference) : null;
                    fileLoadOperation.location = inputFileLocation;
                    if (BuildVars.LOGS_ENABLED) {
                        strBytesToHex2 = Utilities.bytesToHex(inputFileLocation.file_reference);
                    }
                } else {
                    if (isSameReference(inputFileLocation2.file_reference, bArr)) {
                        return false;
                    }
                    String strBytesToHex3 = BuildVars.LOGS_ENABLED ? Utilities.bytesToHex(fileLoadOperation.location.file_reference) : null;
                    TLRPC.InputFileLocation inputFileLocation3 = fileLoadOperation.location;
                    inputFileLocation2.file_reference = bArr;
                    inputFileLocation3.file_reference = bArr;
                    strBytesToHex2 = BuildVars.LOGS_ENABLED ? Utilities.bytesToHex(bArr) : null;
                    strBytesToHex = strBytesToHex3;
                }
                if (BuildVars.LOGS_ENABLED) {
                    FileLog.d("debug_loading: from fileref cache updated fileref from " + strBytesToHex + " to " + strBytesToHex2);
                }
            }
        }
        return true;
    }

    private void cleanupCache() {
        if (Math.abs(SystemClock.elapsedRealtime() - this.lastCleanupTime) < 600000) {
            return;
        }
        this.lastCleanupTime = SystemClock.elapsedRealtime();
        ArrayList arrayList = null;
        for (Map.Entry<String, CachedResult> entry : this.responseCache.entrySet()) {
            if (Math.abs(System.currentTimeMillis() - entry.getValue().firstQueryTime) >= RealWebSocket.CANCEL_AFTER_CLOSE_MILLIS) {
                if (arrayList == null) {
                    arrayList = new ArrayList();
                }
                arrayList.add(entry.getKey());
            }
        }
        if (arrayList != null) {
            int size = arrayList.size();
            for (int i = 0; i < size; i++) {
                this.responseCache.remove(arrayList.get(i));
            }
        }
    }

    /* JADX WARN: Code duplicated, block: B:25:0x004f  */
    public boolean applyCachedFileReference(Object obj, Object... objArr) {
        String str;
        Object[] objArr2;
        Pair<byte[], TLRPC.InputFileLocation> fileReferenceFromResponse;
        Pair<TLRPC.InputFileLocation, String> locationAndKey = getLocationAndKey(obj, objArr);
        if (locationAndKey == null) {
            return false;
        }
        TLRPC.InputFileLocation inputFileLocation = (TLRPC.InputFileLocation) locationAndKey.first;
        String str2 = (String) locationAndKey.second;
        String keyForParentObject = getKeyForParentObject(obj);
        if (keyForParentObject == null) {
            return false;
        }
        if (obj instanceof String) {
            String str3 = (String) obj;
            str = "wallpaper";
            if (!"wallpaper".equals(str3)) {
                str = "gif";
                if (!str3.startsWith("gif")) {
                    str = "recent";
                    if (!"recent".equals(str3)) {
                        str = "fav";
                        if (!"fav".equals(str3)) {
                            str = "update";
                            if (!"update".equals(str3)) {
                                str = str2;
                            }
                        }
                    }
                }
            }
        } else {
            str = str2;
        }
        CachedResult cachedResponse = getCachedResponse(str);
        if (cachedResponse != null) {
            objArr2 = objArr;
            Pair<byte[], TLRPC.InputFileLocation> fileReferenceFromResponse2 = getFileReferenceFromResponse(inputFileLocation, str2, keyForParentObject, cachedResponse.response, objArr2);
            if (fileReferenceFromResponse2 != null) {
                return updateFileReferenceFromCache((byte[]) fileReferenceFromResponse2.first, (TLRPC.InputFileLocation) fileReferenceFromResponse2.second, inputFileLocation, str2, objArr2);
            }
        } else {
            objArr2 = objArr;
        }
        CachedResult cachedResponse2 = getCachedResponse(keyForParentObject);
        if (cachedResponse2 == null || (fileReferenceFromResponse = getFileReferenceFromResponse(inputFileLocation, keyForParentObject, null, cachedResponse2.response, objArr2)) == null) {
            return false;
        }
        return updateFileReferenceFromCache((byte[]) fileReferenceFromResponse.first, (TLRPC.InputFileLocation) fileReferenceFromResponse.second, inputFileLocation, keyForParentObject, objArr2);
    }

    private CachedResult getCachedResponse(String str) {
        CachedResult cachedResult = this.responseCache.get(str);
        if (cachedResult == null || Math.abs(System.currentTimeMillis() - cachedResult.firstQueryTime) < RealWebSocket.CANCEL_AFTER_CLOSE_MILLIS) {
            return cachedResult;
        }
        this.responseCache.remove(str);
        return null;
    }

    private void putReponseToCache(String str, TLObject tLObject) {
        if (this.responseCache.get(str) == null) {
            CachedResult cachedResult = new CachedResult();
            cachedResult.response = tLObject;
            cachedResult.firstQueryTime = System.currentTimeMillis();
            this.responseCache.put(str, cachedResult);
        }
    }

    private byte[] getFileReference(TLRPC.Document document, ArrayList<TLRPC.Document> arrayList, TLRPC.InputFileLocation inputFileLocation, boolean[] zArr, TLRPC.InputFileLocation[] inputFileLocationArr) {
        if (document != null && inputFileLocation != null) {
            int i = 0;
            if (!(inputFileLocation instanceof TLRPC.TL_inputDocumentFileLocation)) {
                int size = document.thumbs.size();
                for (int i2 = 0; i2 < size; i2++) {
                    TLRPC.PhotoSize photoSize = document.thumbs.get(i2);
                    byte[] fileReference = getFileReference(photoSize, inputFileLocation, zArr);
                    if (zArr != null && zArr[0]) {
                        TLRPC.TL_inputDocumentFileLocation tL_inputDocumentFileLocation = new TLRPC.TL_inputDocumentFileLocation();
                        inputFileLocationArr[0] = tL_inputDocumentFileLocation;
                        tL_inputDocumentFileLocation.id = document.id;
                        tL_inputDocumentFileLocation.volume_id = inputFileLocation.volume_id;
                        tL_inputDocumentFileLocation.local_id = inputFileLocation.local_id;
                        tL_inputDocumentFileLocation.access_hash = document.access_hash;
                        byte[] bArr = document.file_reference;
                        tL_inputDocumentFileLocation.file_reference = bArr;
                        tL_inputDocumentFileLocation.thumb_size = photoSize.type;
                        return bArr;
                    }
                    if (fileReference != null) {
                        return fileReference;
                    }
                }
            } else if (document.id == inputFileLocation.id) {
                return document.file_reference;
            }
            if (arrayList != null) {
                while (i < arrayList.size()) {
                    TLRPC.InputFileLocation inputFileLocation2 = inputFileLocation;
                    boolean[] zArr2 = zArr;
                    TLRPC.InputFileLocation[] inputFileLocationArr2 = inputFileLocationArr;
                    byte[] fileReference2 = getFileReference(arrayList.get(i), null, inputFileLocation2, zArr2, inputFileLocationArr2);
                    if (fileReference2 != null) {
                        return fileReference2;
                    }
                    i++;
                    inputFileLocation = inputFileLocation2;
                    zArr = zArr2;
                    inputFileLocationArr = inputFileLocationArr2;
                }
            }
        }
        return null;
    }

    private boolean getPeerReferenceReplacement(TLRPC.User user, TLRPC.Chat chat, boolean z, TLRPC.InputFileLocation inputFileLocation, TLRPC.InputFileLocation[] inputFileLocationArr, boolean[] zArr) {
        TLRPC.InputPeer tL_inputPeerChat;
        TLRPC.InputPeer tL_inputPeerUser;
        if (zArr == null || !zArr[0]) {
            return false;
        }
        TLRPC.TL_inputPeerPhotoFileLocation tL_inputPeerPhotoFileLocation = new TLRPC.TL_inputPeerPhotoFileLocation();
        long j = inputFileLocation.volume_id;
        tL_inputPeerPhotoFileLocation.id = j;
        tL_inputPeerPhotoFileLocation.volume_id = j;
        tL_inputPeerPhotoFileLocation.local_id = inputFileLocation.local_id;
        tL_inputPeerPhotoFileLocation.big = z;
        if (user != null) {
            tL_inputPeerUser = new TLRPC.TL_inputPeerUser();
            tL_inputPeerUser.user_id = user.id;
            tL_inputPeerUser.access_hash = user.access_hash;
            tL_inputPeerPhotoFileLocation.photo_id = user.photo.photo_id;
        } else {
            if (ChatObject.isChannel(chat)) {
                tL_inputPeerChat = new TLRPC.TL_inputPeerChannel();
                tL_inputPeerChat.channel_id = chat.id;
                tL_inputPeerChat.access_hash = chat.access_hash;
            } else {
                tL_inputPeerChat = new TLRPC.TL_inputPeerChat();
                tL_inputPeerChat.chat_id = chat.id;
            }
            tL_inputPeerPhotoFileLocation.photo_id = chat.photo.photo_id;
            tL_inputPeerUser = tL_inputPeerChat;
        }
        tL_inputPeerPhotoFileLocation.peer = tL_inputPeerUser;
        inputFileLocationArr[0] = tL_inputPeerPhotoFileLocation;
        return true;
    }

    private byte[] getFileReference(TLRPC.User user, TLRPC.InputFileLocation inputFileLocation, boolean[] zArr, TLRPC.InputFileLocation[] inputFileLocationArr) {
        TLRPC.UserProfilePhoto userProfilePhoto;
        if (user == null || (userProfilePhoto = user.photo) == null || !(inputFileLocation instanceof TLRPC.TL_inputFileLocation)) {
            return null;
        }
        byte[] fileReference = getFileReference(userProfilePhoto.photo_small, inputFileLocation, zArr);
        if (getPeerReferenceReplacement(user, null, false, inputFileLocation, inputFileLocationArr, zArr)) {
            return new byte[0];
        }
        if (fileReference == null) {
            return getPeerReferenceReplacement(user, null, true, inputFileLocation, inputFileLocationArr, zArr) ? new byte[0] : getFileReference(user.photo.photo_big, inputFileLocation, zArr);
        }
        return fileReference;
    }

    private byte[] getFileReference(TLRPC.Chat chat, TLRPC.InputFileLocation inputFileLocation, boolean[] zArr, TLRPC.InputFileLocation[] inputFileLocationArr) {
        TLRPC.ChatPhoto chatPhoto;
        if (chat == null || (chatPhoto = chat.photo) == null || !((inputFileLocation instanceof TLRPC.TL_inputFileLocation) || (inputFileLocation instanceof TLRPC.TL_inputPeerPhotoFileLocation))) {
            return null;
        }
        if (inputFileLocation instanceof TLRPC.TL_inputPeerPhotoFileLocation) {
            zArr[0] = true;
            if (getPeerReferenceReplacement(null, chat, false, inputFileLocation, inputFileLocationArr, zArr)) {
                return new byte[0];
            }
            return null;
        }
        byte[] fileReference = getFileReference(chatPhoto.photo_small, inputFileLocation, zArr);
        if (getPeerReferenceReplacement(null, chat, false, inputFileLocation, inputFileLocationArr, zArr)) {
            return new byte[0];
        }
        if (fileReference == null) {
            return getPeerReferenceReplacement(null, chat, true, inputFileLocation, inputFileLocationArr, zArr) ? new byte[0] : getFileReference(chat.photo.photo_big, inputFileLocation, zArr);
        }
        return fileReference;
    }

    private byte[] getFileReference(TLRPC.Photo photo, TLRPC.InputFileLocation inputFileLocation, boolean[] zArr, TLRPC.InputFileLocation[] inputFileLocationArr) {
        if (photo == null) {
            return null;
        }
        if (inputFileLocation instanceof TLRPC.TL_inputPhotoFileLocation) {
            if (photo.id == inputFileLocation.id) {
                return photo.file_reference;
            }
            return null;
        }
        if (inputFileLocation instanceof TLRPC.TL_inputFileLocation) {
            int size = photo.sizes.size();
            for (int i = 0; i < size; i++) {
                TLRPC.PhotoSize photoSize = (TLRPC.PhotoSize) photo.sizes.get(i);
                byte[] fileReference = getFileReference(photoSize, inputFileLocation, zArr);
                if (zArr != null && zArr[0]) {
                    TLRPC.TL_inputPhotoFileLocation tL_inputPhotoFileLocation = new TLRPC.TL_inputPhotoFileLocation();
                    inputFileLocationArr[0] = tL_inputPhotoFileLocation;
                    tL_inputPhotoFileLocation.id = photo.id;
                    tL_inputPhotoFileLocation.volume_id = inputFileLocation.volume_id;
                    tL_inputPhotoFileLocation.local_id = inputFileLocation.local_id;
                    tL_inputPhotoFileLocation.access_hash = photo.access_hash;
                    byte[] bArr = photo.file_reference;
                    tL_inputPhotoFileLocation.file_reference = bArr;
                    tL_inputPhotoFileLocation.thumb_size = photoSize.type;
                    return bArr;
                }
                if (fileReference != null) {
                    return fileReference;
                }
            }
        }
        return null;
    }

    private byte[] getFileReference(TLRPC.PhotoSize photoSize, TLRPC.InputFileLocation inputFileLocation, boolean[] zArr) {
        if (photoSize == null || !(inputFileLocation instanceof TLRPC.TL_inputFileLocation)) {
            return null;
        }
        return getFileReference(photoSize.location, inputFileLocation, zArr);
    }

    private byte[] getFileReference(TLRPC.FileLocation fileLocation, TLRPC.InputFileLocation inputFileLocation, boolean[] zArr) {
        if (fileLocation == null || !(inputFileLocation instanceof TLRPC.TL_inputFileLocation) || fileLocation.local_id != inputFileLocation.local_id || fileLocation.volume_id != inputFileLocation.volume_id) {
            return null;
        }
        byte[] bArr = fileLocation.file_reference;
        if (bArr == null && zArr != null) {
            zArr[0] = true;
        }
        return bArr;
    }

    private byte[] getFileReference(TLRPC.WebPage webPage, TLRPC.InputFileLocation inputFileLocation, boolean[] zArr, TLRPC.InputFileLocation[] inputFileLocationArr) {
        FileRefController fileRefController = this;
        byte[] fileReference = fileRefController.getFileReference(webPage.document, null, inputFileLocation, zArr, inputFileLocationArr);
        if (fileReference != null) {
            return fileReference;
        }
        byte[] fileReference2 = getFileReference(webPage.photo, inputFileLocation, zArr, inputFileLocationArr);
        if (fileReference2 != null) {
            return fileReference2;
        }
        if (!webPage.attributes.isEmpty()) {
            int size = webPage.attributes.size();
            int i = 0;
            while (i < size) {
                TLRPC.WebPageAttribute webPageAttribute = (TLRPC.WebPageAttribute) webPage.attributes.get(i);
                if (webPageAttribute instanceof TLRPC.TL_webPageAttributeTheme) {
                    TLRPC.TL_webPageAttributeTheme tL_webPageAttributeTheme = (TLRPC.TL_webPageAttributeTheme) webPageAttribute;
                    int size2 = tL_webPageAttributeTheme.documents.size();
                    int i2 = 0;
                    while (i2 < size2) {
                        byte[] fileReference3 = fileRefController.getFileReference((TLRPC.Document) tL_webPageAttributeTheme.documents.get(i2), null, inputFileLocation, zArr, inputFileLocationArr);
                        if (fileReference3 != null) {
                            return fileReference3;
                        }
                        i2++;
                        fileRefController = this;
                    }
                }
                i++;
                fileRefController = this;
            }
        }
        TLRPC.Page page = webPage.cached_page;
        if (page == null) {
            return null;
        }
        int size3 = page.documents.size();
        for (int i3 = 0; i3 < size3; i3++) {
            byte[] fileReference4 = getFileReference((TLRPC.Document) webPage.cached_page.documents.get(i3), null, inputFileLocation, zArr, inputFileLocationArr);
            if (fileReference4 != null) {
                return fileReference4;
            }
        }
        int size4 = webPage.cached_page.photos.size();
        for (int i4 = 0; i4 < size4; i4++) {
            byte[] fileReference5 = getFileReference((TLRPC.Photo) webPage.cached_page.photos.get(i4), inputFileLocation, zArr, inputFileLocationArr);
            if (fileReference5 != null) {
                return fileReference5;
            }
        }
        return null;
    }

    public static boolean isFileRefError(String str) {
        if ("FILEREF_EXPIRED".equals(str) || "FILE_REFERENCE_EXPIRED".equals(str) || "FILE_REFERENCE_EMPTY".equals(str)) {
            return true;
        }
        return str != null && str.startsWith("FILE_REFERENCE_");
    }

    public static int getFileRefErrorIndex(String str) {
        if (str != null && str.startsWith("FILE_REFERENCE_") && str.endsWith("_EXPIRED")) {
            try {
                return Integer.parseInt(str.substring(15, str.length() - 8));
            } catch (Exception unused) {
            }
        }
        return -1;
    }

    public static boolean isFileRefErrorCover(String str) {
        return str != null && isFileRefError(str) && str.endsWith("COVER_EXPIRED");
    }
}
