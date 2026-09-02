package com.radolyn.ayugram.controllers;

import android.util.LruCache;
import com.exteragram.messenger.utils.chats.ChatUtils;
import com.radolyn.ayugram.AyuConfig;
import com.radolyn.ayugram.database.AyuData;
import com.radolyn.ayugram.database.entities.SpyLastSeen;
import com.radolyn.ayugram.database.entities.SpyMessageContentsRead;
import com.radolyn.ayugram.database.entities.SpyMessageRead;
import com.radolyn.ayugram.utils.AyuLocalDatabaseUtils;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.Locale;
import org.lsposed.lsparanoid.Deobfuscator$AyuGram4A$TMessagesProj;
import org.telegram.messenger.AndroidUtilities;
import org.telegram.messenger.BaseController;
import org.telegram.messenger.DialogObject;
import org.telegram.messenger.MediaDataController;
import org.telegram.messenger.MessageObject;
import org.telegram.messenger.MessagesController;
import org.telegram.messenger.NotificationCenter;
import org.telegram.messenger.UserConfig;
import org.telegram.tgnet.TLRPC;
import org.telegram.tgnet.tl.TL_stories;

public class AyuSpyController extends BaseController {
    private static final AyuSpyController[] Instance = new AyuSpyController[16];
    private final LruCache onlineCache;

    public AyuSpyController(int i) {
        super(i);
        this.onlineCache = new LruCache(MediaDataController.MAX_STYLE_RUNS_COUNT);
    }

    public static AyuSpyController getInstance(int i) {
        AyuSpyController ayuSpyController;
        AyuSpyController[] ayuSpyControllerArr = Instance;
        AyuSpyController ayuSpyController2 = ayuSpyControllerArr[i];
        if (ayuSpyController2 != null) {
            return ayuSpyController2;
        }
        synchronized (AyuSpyController.class) {
            try {
                ayuSpyController = ayuSpyControllerArr[i];
                if (ayuSpyController == null) {
                    ayuSpyController = new AyuSpyController(i);
                    ayuSpyControllerArr[i] = ayuSpyController;
                }
            } catch (Throwable th) {
                throw th;
            }
        }
        return ayuSpyController;
    }

    public void onMessageRead(long j, int i) {
        if (AyuConfig.saveReadDate) {
            try {
                onMessageReadInner(j, String.format(Locale.US, "SELECT mid FROM messages_v2 WHERE uid = %d AND mid > 0 AND mid <= %d AND read_state IN(0,2) AND out = 1", Long.valueOf(j), Integer.valueOf(i)));
            } catch (Throwable unused) {
            }
        }
    }

    public void onEncryptedMessageRead(long j, int i) {
        if (AyuConfig.saveReadDate) {
            try {
                onMessageReadInner(j, String.format(Locale.US, "SELECT mid FROM messages_v2 WHERE uid = %d AND date <= %d AND read_state IN(0,2) AND out = 1", Long.valueOf(j), Integer.valueOf(i)));
            } catch (Throwable unused) {
            }
        }
    }

    private void onMessageReadInner(long j, String str) {
        int currentTime = getConnectionsManager().getCurrentTime();
        Iterator itIterateThroughMessageIds = AyuLocalDatabaseUtils.iterateThroughMessageIds(this.currentAccount, str);
        while (itIterateThroughMessageIds.hasNext()) {
            Integer num = (Integer) itIterateThroughMessageIds.next();
            SpyMessageRead spyMessageRead = new SpyMessageRead();
            spyMessageRead.userId = getUserConfig().getClientUserId();
            spyMessageRead.dialogId = j;
            spyMessageRead.messageId = num.intValue();
            spyMessageRead.entityCreateDate = currentTime;
            AyuData.getSpyDao().insert(spyMessageRead);
        }
    }

    public void onMessageContentsRead(long j, String str) {
        if (AyuConfig.saveReadDate) {
            try {
                onMessageContentsReadInner(j, str);
            } catch (Throwable unused) {
            }
        }
    }

    private void onMessageContentsReadInner(long j, String str) {
        int currentTime = getConnectionsManager().getCurrentTime();
        Iterator itIterateThroughMessageIds = AyuLocalDatabaseUtils.iterateThroughMessageIds(this.currentAccount, String.format(Locale.US, "SELECT mid FROM messages_v2 WHERE mid IN (%s) AND uid = %d", str, Long.valueOf(j)));
        while (itIterateThroughMessageIds.hasNext()) {
            Integer num = (Integer) itIterateThroughMessageIds.next();
            SpyMessageContentsRead spyMessageContentsRead = new SpyMessageContentsRead();
            spyMessageContentsRead.userId = getUserConfig().getClientUserId();
            spyMessageContentsRead.dialogId = j;
            spyMessageContentsRead.messageId = num.intValue();
            spyMessageContentsRead.entityCreateDate = currentTime;
            AyuData.getSpyDao().insert(spyMessageContentsRead);
        }
    }

    public SpyMessageRead getMessageRead(long j, int i) {
        if (AyuConfig.saveReadDate) {
            return AyuData.getSpyDao().getMessageRead(getUserConfig().getClientUserId(), j, i);
        }
        return null;
    }

    public SpyMessageContentsRead getMessageContentsRead(long j, int i) {
        if (AyuConfig.saveReadDate) {
            return AyuData.getSpyDao().getMessageContentsRead(getUserConfig().getClientUserId(), j, i);
        }
        return null;
    }

    /* JADX WARN: Code restructure failed: missing block: B:53:0x009d, code lost:
    
        if (r4 == 0) goto L54;
     */
    /* JADX WARN: Code restructure failed: missing block: B:60:0x00ae, code lost:
    
        if (r4 == 0) goto L61;
     */
    /* JADX WARN: Code restructure failed: missing block: B:67:0x00bf, code lost:
    
        if (r4 == 0) goto L68;
     */
    /* JADX WARN: Code restructure failed: missing block: B:74:0x00d0, code lost:
    
        if (r4 == 0) goto L75;
     */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
    */
    public boolean saveOnlineActivity(TLRPC.Update update, int i) {
        long peerDialogId;
        int i2;
        boolean zSaveOnlineActivity = false;
        if (AyuConfig.saveLocalOnline && update != null) {
            if (update instanceof TLRPC.TL_updateNewMessage) {
                TLRPC.Message message = ((TLRPC.TL_updateNewMessage) update).message;
                if (message != null && !(message instanceof TLRPC.TL_messageEmpty)) {
                    peerDialogId = message.from_id.user_id;
                    if (peerDialogId != 0) {
                        i2 = message.date;
                        i = i2;
                    }
                    i = 0;
                    peerDialogId = 0;
                    break;
                }
                return false;
            }
            if (update instanceof TLRPC.TL_updateNewChannelMessage) {
                TLRPC.Message message2 = ((TLRPC.TL_updateNewChannelMessage) update).message;
                if (message2 == null || (message2 instanceof TLRPC.TL_messageEmpty)) {
                    return false;
                }
                peerDialogId = message2.from_id.user_id;
                if (peerDialogId != 0) {
                    i2 = message2.date;
                    i = i2;
                }
            } else if (update instanceof TLRPC.TL_updateReadMessagesContents) {
                TLRPC.TL_updateReadMessagesContents tL_updateReadMessagesContents = (TLRPC.TL_updateReadMessagesContents) update;
                int i3 = tL_updateReadMessagesContents.date;
                if (i3 != 0) {
                    i = i3;
                }
                ArrayList arrayList = tL_updateReadMessagesContents.messages;
                int size = arrayList.size();
                int i4 = 0;
                while (true) {
                    if (i4 >= size) {
                        peerDialogId = 0;
                        break;
                    }
                    Object obj = arrayList.get(i4);
                    i4++;
                    MessageObject existingMessageInAnyWay = MessagesController.getInstance(this.currentAccount).getExistingMessageInAnyWay(0L, (Integer) obj);
                    if (existingMessageInAnyWay != null) {
                        peerDialogId = existingMessageInAnyWay.getDialogId();
                        break;
                    }
                }
            } else if (update instanceof TLRPC.TL_updateReadHistoryOutbox) {
                peerDialogId = ((TLRPC.TL_updateReadHistoryOutbox) update).peer.user_id;
                if (peerDialogId == 0) {
                    return false;
                }
            } else if (update instanceof TLRPC.TL_updateUserTyping) {
                peerDialogId = ((TLRPC.TL_updateUserTyping) update).user_id;
            } else {
                if (update instanceof TLRPC.TL_updateChatUserTyping) {
                    TLRPC.Peer peer = ((TLRPC.TL_updateChatUserTyping) update).from_id;
                    if (peer != null) {
                        peerDialogId = peer.user_id;
                    }
                    return false;
                }
                if (update instanceof TLRPC.TL_updateChannelUserTyping) {
                    TLRPC.Peer peer2 = ((TLRPC.TL_updateChannelUserTyping) update).from_id;
                    if (peer2 != null) {
                        peerDialogId = peer2.user_id;
                    }
                    return false;
                }
                if (update instanceof TL_stories.TL_updateStory) {
                    TLRPC.Peer peer3 = ((TL_stories.TL_updateStory) update).peer;
                    if (peer3 != null) {
                        peerDialogId = peer3.user_id;
                    }
                    return false;
                }
                if (update instanceof TLRPC.TL_updatePeerWallpaper) {
                    TLRPC.Peer peer4 = ((TLRPC.TL_updatePeerWallpaper) update).peer;
                    if (peer4 != null) {
                        peerDialogId = peer4.user_id;
                    }
                    return false;
                }
                if (update instanceof TLRPC.TL_updateUserEmojiStatus) {
                    peerDialogId = ((TLRPC.TL_updateUserEmojiStatus) update).user_id;
                    if (peerDialogId == 0) {
                        return false;
                    }
                } else if (update instanceof TLRPC.TL_updateUserName) {
                    peerDialogId = ((TLRPC.TL_updateUserName) update).user_id;
                    if (peerDialogId == 0) {
                        return false;
                    }
                } else if (update instanceof TLRPC.TL_updateUserPhoto) {
                    peerDialogId = ((TLRPC.TL_updateUserPhoto) update).user_id;
                    if (peerDialogId == 0) {
                        return false;
                    }
                } else if (update instanceof TLRPC.TL_updateUserPhone) {
                    peerDialogId = ((TLRPC.TL_updateUserPhone) update).user_id;
                    if (peerDialogId == 0) {
                        return false;
                    }
                } else if (update instanceof TLRPC.TL_updateUserStatus) {
                    TLRPC.TL_updateUserStatus tL_updateUserStatus = (TLRPC.TL_updateUserStatus) update;
                    if (tL_updateUserStatus.user_id == 0 || isBadStatus(tL_updateUserStatus.status)) {
                        return false;
                    }
                    if (i == 0) {
                        i = tL_updateUserStatus.status.expires;
                    }
                    peerDialogId = tL_updateUserStatus.user_id;
                } else if (update instanceof TLRPC.TL_updateNewStoryReaction) {
                    peerDialogId = DialogObject.getPeerDialogId(((TLRPC.TL_updateNewStoryReaction) update).peer);
                    if (peerDialogId == 0 || getMessagesController().getUser(Long.valueOf(peerDialogId)) == null) {
                        return false;
                    }
                } else {
                    if (update instanceof TLRPC.TL_updateGroupCallParticipants) {
                        ArrayList arrayList2 = ((TLRPC.TL_updateGroupCallParticipants) update).participants;
                        int size2 = arrayList2.size();
                        int i5 = 0;
                        while (i5 < size2) {
                            Object obj2 = arrayList2.get(i5);
                            i5++;
                            TLRPC.Peer peer5 = ((TLRPC.GroupCallParticipant) obj2).peer;
                            if (peer5 != null) {
                                long j = peer5.user_id;
                                if (j != 0) {
                                    zSaveOnlineActivity |= saveOnlineActivity(j, i);
                                }
                            }
                        }
                        return zSaveOnlineActivity;
                    }
                    if (update instanceof TLRPC.TL_updatePeerHistoryTTL) {
                        peerDialogId = ((TLRPC.TL_updatePeerHistoryTTL) update).peer.user_id;
                        if (peerDialogId == 0) {
                            return false;
                        }
                    }
                }
            }
            i = 0;
            peerDialogId = 0;
            break;
            if (i != 0 && peerDialogId != 0) {
                return saveOnlineActivity(peerDialogId, i);
            }
        }
        return false;
    }

    public boolean saveOnlineActivity(long j, int i) {
        if (!AyuConfig.saveLocalOnline || UserConfig.getInstance(this.currentAccount).getClientUserId() == j || i < 1397411401) {
            return false;
        }
        Integer lastSeen = getLastSeen(j);
        if (lastSeen != null && lastSeen.intValue() >= i) {
            return false;
        }
        SpyLastSeen spyLastSeen = new SpyLastSeen();
        spyLastSeen.userId = j;
        spyLastSeen.lastSeenDate = i;
        AyuData.getSpyDao().insert(spyLastSeen);
        MessagesController messagesController = MessagesController.getInstance(this.currentAccount);
        messagesController.onlinePrivacy.put(Long.valueOf(j), Integer.valueOf(i));
        this.onlineCache.put(Long.valueOf(j), Integer.valueOf(i));
        TLRPC.User user = messagesController.getUser(Long.valueOf(j));
        return (user == null || user.bot || !isBadStatus(user.status)) ? false : true;
    }

    public Integer getLastSeenCached(long j) {
        if (AyuConfig.saveLocalOnline) {
            return (Integer) this.onlineCache.get(Long.valueOf(j));
        }
        return null;
    }

    public Integer getLastSeen(long j) {
        if (!AyuConfig.saveLocalOnline) {
            return null;
        }
        Integer num = (Integer) this.onlineCache.get(Long.valueOf(j));
        if (num != null && num.intValue() >= 1397411401) {
            return num;
        }
        SpyLastSeen lastSeen = AyuData.getSpyDao().getLastSeen(j);
        if (lastSeen == null || lastSeen.lastSeenDate < 1397411401) {
            return null;
        }
        this.onlineCache.put(Long.valueOf(j), Integer.valueOf(lastSeen.lastSeenDate));
        return Integer.valueOf(lastSeen.lastSeenDate);
    }

    public void loadLastSeen(final long j) {
        if (AyuConfig.saveLocalOnline) {
            Integer num = (Integer) this.onlineCache.get(Long.valueOf(j));
            if (num == null || num.intValue() <= 1397411401) {
                ChatUtils.utilsQueue.postRunnable(new Runnable() { // from class: com.radolyn.ayugram.controllers.AyuSpyController$$ExternalSyntheticLambda1
                    @Override // java.lang.Runnable
                    public final void run() {
                        this.f$0.lambda$loadLastSeen$0(j);
                    }
                });
            }
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$loadLastSeen$0(long j) {
        if (getLastSeen(j) != null) {
            updateInterfaces();
        }
    }

    public void updateInterfaces() {
        AndroidUtilities.runOnUIThread(new Runnable() { // from class: com.radolyn.ayugram.controllers.AyuSpyController$$ExternalSyntheticLambda0
            @Override // java.lang.Runnable
            public final void run() {
                this.f$0.lambda$updateInterfaces$1();
            }
        });
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$updateInterfaces$1() {
        NotificationCenter.getInstance(this.currentAccount).lambda$postNotificationNameOnUIThread$1(NotificationCenter.updateInterfaces, Integer.valueOf(MessagesController.UPDATE_MASK_STATUS));
    }

    public static boolean isBadStatus(TLRPC.UserStatus userStatus) {
        int i;
        return userStatus == null || (userStatus instanceof TLRPC.TL_userStatusRecently) || (userStatus instanceof TLRPC.TL_userStatusLastWeek) || (userStatus instanceof TLRPC.TL_userStatusLastMonth) || (i = userStatus.expires) == -1 || i == -100 || i == -101 || i == -102 || i == -1000 || i == -1001 || i == -1002;
    }
}
