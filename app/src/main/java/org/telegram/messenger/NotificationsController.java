package org.telegram.messenger;

import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.app.AlarmManager;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationChannelGroup;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.BitmapShader;
import android.graphics.Canvas;
import android.graphics.ImageDecoder;
import android.graphics.LinearGradient;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.Point;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.graphics.PostProcessor;
import android.graphics.Rect;
import android.graphics.Shader;
import android.graphics.drawable.BitmapDrawable;
import android.media.AudioAttributes;
import android.media.AudioManager;
import android.media.SoundPool;
import android.net.Uri;
import android.os.Build;
import android.os.PowerManager;
import android.os.SystemClock;
import android.provider.Settings;
import android.text.TextPaint;
import android.text.TextUtils;
import android.util.Pair;
import android.util.SparseArray;
import android.util.SparseBooleanArray;
import androidx.collection.LongSparseArray;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;
import androidx.core.app.Person;
import androidx.core.content.FileProvider;
import androidx.core.content.LocusIdCompat;
import androidx.core.content.pm.ShortcutInfoCompat;
import androidx.core.content.pm.ShortcutManagerCompat;
import androidx.core.graphics.drawable.IconCompat;
import com.exteragram.messenger.utils.AppUtils;
import com.google.android.gms.cast.framework.media.internal.zzo$$ExternalSyntheticApiModelOutline0;
import com.google.common.collect.Lists;
import com.radolyn.ayugram.AyuConfig;
import com.radolyn.ayugram.utils.AyuMessageUtils;
import j$.util.Comparator;
import java.io.File;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.concurrent.CountDownLatch;
import java.util.function.Consumer;
import java.util.function.ToLongFunction;
import okhttp3.internal.url._UrlKt;
import org.telegram.messenger.support.LongSparseIntArray;
import org.telegram.messenger.utils.tlutils.TlUtils;
import org.telegram.messenger.voip.VoIPGroupNotification;
import org.telegram.tgnet.ConnectionsManager;
import org.telegram.tgnet.RequestDelegate;
import org.telegram.tgnet.TLObject;
import org.telegram.tgnet.TLRPC;
import org.telegram.tgnet.tl.TL_account;
import org.telegram.ui.ActionBar.Theme;
import org.telegram.ui.BubbleActivity;
import org.telegram.ui.Components.AvatarDrawable;
import org.telegram.ui.Components.Forum.ForumUtilities;
import org.telegram.ui.Components.spoilers.SpoilerEffect;
import org.telegram.ui.LaunchActivity;
import org.telegram.ui.PopupNotificationActivity;
import org.telegram.ui.Stories.recorder.StoryEntry;
import org.webrtc.MediaStreamTrack;

public class NotificationsController extends BaseController {
    public static final String EXTRA_VOICE_REPLY = "extra_voice_reply";
    private static volatile NotificationsController[] Instance = null;
    public static String OTHER_NOTIFICATIONS_CHANNEL = null;
    public static final int SETTING_MUTE_2_DAYS = 2;
    public static final int SETTING_MUTE_8_HOURS = 1;
    public static final int SETTING_MUTE_CUSTOM = 5;
    public static final int SETTING_MUTE_FOREVER = 3;
    public static final int SETTING_MUTE_HOUR = 0;
    public static final int SETTING_MUTE_UNMUTE = 4;
    public static final int SETTING_SOUND_OFF = 1;
    public static final int SETTING_SOUND_ON = 0;
    public static final int TYPE_CHANNEL = 2;
    public static final int TYPE_GROUP = 0;
    public static final int TYPE_PRIVATE = 1;
    public static final int TYPE_REACTIONS_MESSAGES = 4;
    public static final int TYPE_REACTIONS_STORIES = 5;
    public static final int TYPE_STORIES = 3;
    protected static AudioManager audioManager;
    private static final Object[] lockObjects;
    private static NotificationManagerCompat notificationManager;
    private static final LongSparseArray sharedPrefCachedKeys;
    private static NotificationManager systemNotificationManager;
    private AlarmManager alarmManager;
    private boolean channelGroupsCreated;
    private Runnable checkStoryPushesRunnable;
    private final ArrayList<MessageObject> delayedPushMessages;
    NotificationsSettingsFacade dialogsNotificationsFacade;
    private final LongSparseArray fcmRandomMessagesDict;
    private Boolean groupsCreated;
    private boolean inChatSoundEnabled;
    private int lastBadgeCount;
    private int lastButtonId;
    public long lastNotificationChannelCreateTime;
    private int lastOnlineFromOtherDevice;
    private long lastSoundOutPlay;
    private long lastSoundPlay;
    private final LongSparseArray lastWearNotifiedMessageId;
    private String launcherClassName;
    private SpoilerEffect mediaSpoilerEffect;
    private Runnable notificationDelayRunnable;
    private PowerManager.WakeLock notificationDelayWakelock;
    private String notificationGroup;
    private int notificationId;
    private boolean notifyCheck;
    private long openedDialogId;
    private final HashSet<Long> openedInBubbleDialogs;
    private long openedTopicId;
    private int personalCount;
    public final ArrayList<MessageObject> popupMessages;
    public ArrayList<MessageObject> popupReplyMessages;
    private final LongSparseArray pushDialogs;
    private final LongSparseArray pushDialogsOverrideMention;
    public final ArrayList<MessageObject> pushMessages;
    private final LongSparseArray pushMessagesDict;
    public boolean showBadgeMessages;
    public boolean showBadgeMuted;
    public boolean showBadgeNumber;
    private final LongSparseArray smartNotificationsDialogs;
    private int soundIn;
    private boolean soundInLoaded;
    private int soundOut;
    private boolean soundOutLoaded;
    private SoundPool soundPool;
    private int soundRecord;
    private boolean soundRecordLoaded;
    char[] spoilerChars;
    private final ArrayList<StoryNotification> storyPushMessages;
    private final LongSparseArray storyPushMessagesDict;
    private int total_unread_count;
    private final LongSparseArray wearNotificationsIds;
    private static final DispatchQueue notificationsQueue = new DispatchQueue("notificationsQueue");
    public static long globalSecretChatId = DialogObject.makeEncryptedDialogId(1);

    /* JADX INFO: renamed from: $r8$lambda$1t1axbSYGQIU_GMVkHn-zrj3Llc, reason: not valid java name */
    public static /* synthetic */ void m3637$r8$lambda$1t1axbSYGQIU_GMVkHnzrj3Llc(TLObject tLObject, TLRPC.TL_error tL_error) {
    }

    public static /* synthetic */ void $r8$lambda$XCcar2pyUU3PNWJSRPBNWRsFJM0(TLObject tLObject, TLRPC.TL_error tL_error) {
    }

    public static /* synthetic */ void $r8$lambda$m8WhB6SNQirs3X5XQWhhP5mgO4s(TLObject tLObject, TLRPC.TL_error tL_error) {
    }

    public void processReadStories() {
    }

    static {
        notificationManager = null;
        systemNotificationManager = null;
        if (Build.VERSION.SDK_INT >= 26 && ApplicationLoader.applicationContext != null) {
            notificationManager = NotificationManagerCompat.from(ApplicationLoader.applicationContext);
            systemNotificationManager = (NotificationManager) ApplicationLoader.applicationContext.getSystemService("notification");
            checkOtherNotificationsChannel();
        }
        audioManager = (AudioManager) ApplicationLoader.applicationContext.getSystemService(MediaStreamTrack.AUDIO_TRACK_KIND);
        Instance = new NotificationsController[16];
        lockObjects = new Object[16];
        for (int i = 0; i < 16; i++) {
            lockObjects[i] = new Object();
        }
        sharedPrefCachedKeys = new LongSparseArray();
    }

    public static NotificationsController getInstance(int i) {
        NotificationsController notificationsController;
        NotificationsController notificationsController2 = Instance[i];
        if (notificationsController2 != null) {
            return notificationsController2;
        }
        synchronized (lockObjects[i]) {
            try {
                notificationsController = Instance[i];
                if (notificationsController == null) {
                    NotificationsController[] notificationsControllerArr = Instance;
                    NotificationsController notificationsController3 = new NotificationsController(i);
                    notificationsControllerArr[i] = notificationsController3;
                    notificationsController = notificationsController3;
                }
            } catch (Throwable th) {
                throw th;
            }
        }
        return notificationsController;
    }

    public NotificationsController(int i) {
        super(i);
        this.pushMessages = new ArrayList<>();
        this.delayedPushMessages = new ArrayList<>();
        this.pushMessagesDict = new LongSparseArray();
        this.fcmRandomMessagesDict = new LongSparseArray();
        this.smartNotificationsDialogs = new LongSparseArray();
        this.pushDialogs = new LongSparseArray();
        this.wearNotificationsIds = new LongSparseArray();
        this.lastWearNotifiedMessageId = new LongSparseArray();
        this.pushDialogsOverrideMention = new LongSparseArray();
        this.popupMessages = new ArrayList<>();
        this.popupReplyMessages = new ArrayList<>();
        this.openedInBubbleDialogs = new HashSet<>();
        this.storyPushMessages = new ArrayList<>();
        this.storyPushMessagesDict = new LongSparseArray();
        this.openedDialogId = 0L;
        this.openedTopicId = 0L;
        this.lastButtonId = 5000;
        this.total_unread_count = 0;
        this.personalCount = 0;
        this.notifyCheck = false;
        this.lastOnlineFromOtherDevice = 0;
        this.lastBadgeCount = -1;
        this.mediaSpoilerEffect = new SpoilerEffect();
        this.spoilerChars = new char[]{10252, 10338, 10385, 10280, 10277, 10286, 10321};
        this.checkStoryPushesRunnable = new Runnable() { // from class: org.telegram.messenger.NotificationsController$$ExternalSyntheticLambda49
            @Override // java.lang.Runnable
            public final void run() {
                this.f$0.checkStoryPushes();
            }
        };
        this.notificationId = this.currentAccount + 1;
        StringBuilder sb = new StringBuilder();
        sb.append("messages");
        int i2 = this.currentAccount;
        sb.append(i2 == 0 ? _UrlKt.FRAGMENT_ENCODE_SET : Integer.valueOf(i2));
        this.notificationGroup = sb.toString();
        SharedPreferences notificationsSettings = getAccountInstance().getNotificationsSettings();
        this.inChatSoundEnabled = notificationsSettings.getBoolean("EnableInChatSound", true);
        this.showBadgeNumber = notificationsSettings.getBoolean("badgeNumber", true);
        this.showBadgeMuted = notificationsSettings.getBoolean("badgeNumberMuted", false);
        this.showBadgeMessages = notificationsSettings.getBoolean("badgeNumberMessages", true);
        notificationManager = NotificationManagerCompat.from(ApplicationLoader.applicationContext);
        systemNotificationManager = (NotificationManager) ApplicationLoader.applicationContext.getSystemService("notification");
        try {
            audioManager = (AudioManager) ApplicationLoader.applicationContext.getSystemService(MediaStreamTrack.AUDIO_TRACK_KIND);
        } catch (Exception e) {
            FileLog.e(e);
        }
        try {
            this.alarmManager = (AlarmManager) ApplicationLoader.applicationContext.getSystemService("alarm");
        } catch (Exception e2) {
            FileLog.e(e2);
        }
        try {
            PowerManager.WakeLock wakeLockNewWakeLock = ((PowerManager) ApplicationLoader.applicationContext.getSystemService("power")).newWakeLock(1, "telegram:notification_delay_lock");
            this.notificationDelayWakelock = wakeLockNewWakeLock;
            wakeLockNewWakeLock.setReferenceCounted(false);
        } catch (Exception e3) {
            FileLog.e(e3);
        }
        this.notificationDelayRunnable = new Runnable() { // from class: org.telegram.messenger.NotificationsController$$ExternalSyntheticLambda50
            @Override // java.lang.Runnable
            public final void run() {
                this.f$0.lambda$new$0();
            }
        };
        this.dialogsNotificationsFacade = new NotificationsSettingsFacade(this.currentAccount);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$new$0() {
        if (BuildVars.LOGS_ENABLED) {
            FileLog.d("delay reached");
        }
        if (!this.delayedPushMessages.isEmpty()) {
            showOrUpdateNotification(true);
            this.delayedPushMessages.clear();
        }
        try {
            if (this.notificationDelayWakelock.isHeld()) {
                this.notificationDelayWakelock.release();
            }
        } catch (Exception e) {
            FileLog.e(e);
        }
    }

    public static void checkOtherNotificationsChannel() {
        SharedPreferences sharedPreferences;
        if (Build.VERSION.SDK_INT < 26) {
            return;
        }
        if (OTHER_NOTIFICATIONS_CHANNEL == null) {
            sharedPreferences = ApplicationLoader.applicationContext.getSharedPreferences("Notifications", 0);
            OTHER_NOTIFICATIONS_CHANNEL = sharedPreferences.getString("OtherKey", "Other3");
        } else {
            sharedPreferences = null;
        }
        NotificationChannel notificationChannel = systemNotificationManager.getNotificationChannel(OTHER_NOTIFICATIONS_CHANNEL);
        if (notificationChannel != null && notificationChannel.getImportance() == 0) {
            try {
                systemNotificationManager.deleteNotificationChannel(OTHER_NOTIFICATIONS_CHANNEL);
            } catch (Exception e) {
                FileLog.e(e);
            }
            OTHER_NOTIFICATIONS_CHANNEL = null;
            notificationChannel = null;
        }
        if (OTHER_NOTIFICATIONS_CHANNEL == null) {
            if (sharedPreferences == null) {
                sharedPreferences = ApplicationLoader.applicationContext.getSharedPreferences("Notifications", 0);
            }
            OTHER_NOTIFICATIONS_CHANNEL = "Other" + Utilities.random.nextLong();
            sharedPreferences.edit().putString("OtherKey", OTHER_NOTIFICATIONS_CHANNEL).apply();
        }
        if (notificationChannel == null) {
            NotificationsController$$ExternalSyntheticApiModelOutline0.m();
            NotificationChannel notificationChannelM = zzo$$ExternalSyntheticApiModelOutline0.m(OTHER_NOTIFICATIONS_CHANNEL, "Internal notifications", 3);
            notificationChannelM.enableLights(false);
            notificationChannelM.enableVibration(false);
            notificationChannelM.setSound(null, null);
            try {
                systemNotificationManager.createNotificationChannel(notificationChannelM);
            } catch (Exception e2) {
                FileLog.e(e2);
            }
        }
    }

    public static String getSharedPrefKey(long j, long j2) {
        return getSharedPrefKey(j, j2, false);
    }

    /* JADX WARN: Type inference fix 'apply assigned field type' failed
    java.lang.UnsupportedOperationException: ArgType.getObject(), call class: class jadx.core.dex.instructions.args.ArgType$PrimitiveArg
    	at jadx.core.dex.instructions.args.ArgType.getObject(ArgType.java:596)
    	at jadx.core.dex.attributes.nodes.ClassTypeVarsAttr.getTypeVarsMapFor(ClassTypeVarsAttr.java:35)
    	at jadx.core.dex.nodes.utils.TypeUtils.replaceClassGenerics(TypeUtils.java:177)
    	at jadx.core.dex.visitors.typeinference.FixTypesVisitor.insertExplicitUseCast(FixTypesVisitor.java:397)
    	at jadx.core.dex.visitors.typeinference.FixTypesVisitor.tryFieldTypeWithNewCasts(FixTypesVisitor.java:359)
    	at jadx.core.dex.visitors.typeinference.FixTypesVisitor.applyFieldType(FixTypesVisitor.java:309)
    	at jadx.core.dex.visitors.typeinference.FixTypesVisitor.visit(FixTypesVisitor.java:94)
     */
    public static String getSharedPrefKey(long j, long j2, boolean z) {
        String strValueOf;
        if (z) {
            if (j2 != 0) {
                return String.format(Locale.US, "%d_%d", Long.valueOf(j), Long.valueOf(j2));
            }
            return String.valueOf(j);
        }
        long j3 = (j2 << 12) + j;
        LongSparseArray longSparseArray = sharedPrefCachedKeys;
        int iIndexOfKey = longSparseArray.indexOfKey(j3);
        if (iIndexOfKey >= 0) {
            return (String) longSparseArray.valueAt(iIndexOfKey);
        }
        if (j2 != 0) {
            strValueOf = String.format(Locale.US, "%d_%d", Long.valueOf(j), Long.valueOf(j2));
        } else {
            strValueOf = String.valueOf(j);
        }
        longSparseArray.put(j3, strValueOf);
        return strValueOf;
    }

    public void muteUntil(long j, long j2, int i) {
        long j3;
        if (j != 0) {
            SharedPreferences.Editor editorEdit = MessagesController.getNotificationsSettings(this.currentAccount).edit();
            boolean z = j2 != 0;
            boolean zIsGlobalNotificationsEnabled = getInstance(this.currentAccount).isGlobalNotificationsEnabled(j, false, false);
            String sharedPrefKey = getSharedPrefKey(j, j2);
            if (i != Integer.MAX_VALUE) {
                editorEdit.putInt(NotificationsSettingsFacade.PROPERTY_NOTIFY + sharedPrefKey, 3);
                editorEdit.putInt(NotificationsSettingsFacade.PROPERTY_NOTIFY_UNTIL + sharedPrefKey, getConnectionsManager().getCurrentTime() + i);
                j3 = (((long) i) << 32) | 1;
            } else if (!zIsGlobalNotificationsEnabled && !z) {
                editorEdit.remove(NotificationsSettingsFacade.PROPERTY_NOTIFY + sharedPrefKey);
                j3 = 0;
            } else {
                editorEdit.putInt(NotificationsSettingsFacade.PROPERTY_NOTIFY + sharedPrefKey, 2);
                j3 = 1L;
            }
            editorEdit.apply();
            if (j2 == 0) {
                getInstance(this.currentAccount).removeNotificationsForDialog(j);
                MessagesStorage.getInstance(this.currentAccount).setDialogFlags(j, j3);
                TLRPC.Dialog dialog = (TLRPC.Dialog) MessagesController.getInstance(this.currentAccount).dialogs_dict.get(j);
                if (dialog != null) {
                    TLRPC.TL_peerNotifySettings tL_peerNotifySettings = new TLRPC.TL_peerNotifySettings();
                    dialog.notify_settings = tL_peerNotifySettings;
                    if (i != Integer.MAX_VALUE || zIsGlobalNotificationsEnabled) {
                        tL_peerNotifySettings.mute_until = i;
                    }
                }
            }
            getInstance(this.currentAccount).updateServerNotificationsSettings(j, j2);
        }
    }

    public void cleanup() {
        this.popupMessages.clear();
        this.popupReplyMessages.clear();
        this.channelGroupsCreated = false;
        notificationsQueue.postRunnable(new Runnable() { // from class: org.telegram.messenger.NotificationsController$$ExternalSyntheticLambda13
            @Override // java.lang.Runnable
            public final void run() {
                this.f$0.lambda$cleanup$1();
            }
        });
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$cleanup$1() {
        this.openedDialogId = 0L;
        this.openedTopicId = 0L;
        this.total_unread_count = 0;
        this.personalCount = 0;
        this.pushMessages.clear();
        this.pushMessagesDict.clear();
        this.fcmRandomMessagesDict.clear();
        this.pushDialogs.clear();
        this.wearNotificationsIds.clear();
        this.lastWearNotifiedMessageId.clear();
        this.openedInBubbleDialogs.clear();
        this.delayedPushMessages.clear();
        this.notifyCheck = false;
        this.lastBadgeCount = 0;
        try {
            if (this.notificationDelayWakelock.isHeld()) {
                this.notificationDelayWakelock.release();
            }
        } catch (Exception e) {
            FileLog.e(e);
        }
        dismissNotification();
        setBadge(getTotalAllUnreadCount());
        SharedPreferences.Editor editorEdit = getAccountInstance().getNotificationsSettings().edit();
        editorEdit.clear();
        editorEdit.apply();
        if (Build.VERSION.SDK_INT >= 26) {
            try {
                systemNotificationManager.deleteNotificationChannelGroup("channels" + this.currentAccount);
                systemNotificationManager.deleteNotificationChannelGroup("groups" + this.currentAccount);
                systemNotificationManager.deleteNotificationChannelGroup("private" + this.currentAccount);
                systemNotificationManager.deleteNotificationChannelGroup("stories" + this.currentAccount);
                systemNotificationManager.deleteNotificationChannelGroup("other" + this.currentAccount);
                String str = this.currentAccount + "channel";
                List<NotificationChannel> notificationChannels = systemNotificationManager.getNotificationChannels();
                int size = notificationChannels.size();
                for (int i = 0; i < size; i++) {
                    String id = NotificationsController$$ExternalSyntheticApiModelOutline3.m(notificationChannels.get(i)).getId();
                    if (id.startsWith(str)) {
                        try {
                            systemNotificationManager.deleteNotificationChannel(id);
                        } catch (Exception e2) {
                            FileLog.e(e2);
                        }
                        if (BuildVars.LOGS_ENABLED) {
                            FileLog.d("delete channel cleanup " + id);
                        }
                    }
                }
            } catch (Throwable th) {
                FileLog.e(th);
            }
        }
    }

    public void setInChatSoundEnabled(boolean z) {
        this.inChatSoundEnabled = z;
    }

    public void setOpenedDialogId(final long j, final long j2) {
        notificationsQueue.postRunnable(new Runnable() { // from class: org.telegram.messenger.NotificationsController$$ExternalSyntheticLambda14
            @Override // java.lang.Runnable
            public final void run() {
                this.f$0.lambda$setOpenedDialogId$2(j, j2);
            }
        });
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$setOpenedDialogId$2(long j, long j2) {
        this.openedDialogId = j;
        this.openedTopicId = j2;
    }

    public void setOpenedInBubble(final long j, final boolean z) {
        notificationsQueue.postRunnable(new Runnable() { // from class: org.telegram.messenger.NotificationsController$$ExternalSyntheticLambda39
            @Override // java.lang.Runnable
            public final void run() {
                this.f$0.lambda$setOpenedInBubble$3(z, j);
            }
        });
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$setOpenedInBubble$3(boolean z, long j) {
        if (z) {
            this.openedInBubbleDialogs.add(Long.valueOf(j));
        } else {
            this.openedInBubbleDialogs.remove(Long.valueOf(j));
        }
    }

    public void setLastOnlineFromOtherDevice(final int i) {
        notificationsQueue.postRunnable(new Runnable() { // from class: org.telegram.messenger.NotificationsController$$ExternalSyntheticLambda30
            @Override // java.lang.Runnable
            public final void run() {
                this.f$0.lambda$setLastOnlineFromOtherDevice$4(i);
            }
        });
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$setLastOnlineFromOtherDevice$4(int i) {
        if (BuildVars.LOGS_ENABLED) {
            FileLog.d("set last online from other device = " + i);
        }
        this.lastOnlineFromOtherDevice = i;
    }

    public void removeNotificationsForDialog(long j) {
        processReadMessages(null, j, 0, Integer.MAX_VALUE, false);
        LongSparseIntArray longSparseIntArray = new LongSparseIntArray();
        longSparseIntArray.put(j, 0);
        processDialogsUpdateRead(longSparseIntArray);
    }

    public boolean hasMessagesToReply() {
        for (int i = 0; i < this.pushMessages.size(); i++) {
            MessageObject messageObject = this.pushMessages.get(i);
            long dialogId = messageObject.getDialogId();
            if (!messageObject.isReactionPush) {
                TLRPC.Message message = messageObject.messageOwner;
                if ((!message.mentioned || !(message.action instanceof TLRPC.TL_messageActionPinMessage)) && !DialogObject.isEncryptedDialog(dialogId) && ((messageObject.messageOwner.peer_id.channel_id == 0 || messageObject.isSupergroup()) && dialogId != UserObject.VERIFY && dialogId != UserObject.OAUTH)) {
                    return true;
                }
            }
        }
        return false;
    }

    protected void forceShowPopupForReply() {
        notificationsQueue.postRunnable(new Runnable() { // from class: org.telegram.messenger.NotificationsController$$ExternalSyntheticLambda57
            @Override // java.lang.Runnable
            public final void run() {
                this.f$0.lambda$forceShowPopupForReply$6();
            }
        });
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$forceShowPopupForReply$6() {
        final ArrayList arrayList = new ArrayList();
        for (int i = 0; i < this.pushMessages.size(); i++) {
            MessageObject messageObject = this.pushMessages.get(i);
            long dialogId = messageObject.getDialogId();
            TLRPC.Message message = messageObject.messageOwner;
            if ((!message.mentioned || !(message.action instanceof TLRPC.TL_messageActionPinMessage)) && !DialogObject.isEncryptedDialog(dialogId) && (messageObject.messageOwner.peer_id.channel_id == 0 || messageObject.isSupergroup())) {
                arrayList.add(0, messageObject);
            }
        }
        if (arrayList.isEmpty() || AndroidUtilities.needShowPasscode() || SharedConfig.isWaitingForPasscodeEnter) {
            return;
        }
        AndroidUtilities.runOnUIThread(new Runnable() { // from class: org.telegram.messenger.NotificationsController$$ExternalSyntheticLambda18
            @Override // java.lang.Runnable
            public final void run() {
                this.f$0.lambda$forceShowPopupForReply$5(arrayList);
            }
        });
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$forceShowPopupForReply$5(ArrayList arrayList) {
        this.popupReplyMessages = arrayList;
        Intent intent = new Intent(ApplicationLoader.applicationContext, (Class<?>) PopupNotificationActivity.class);
        intent.putExtra("force", true);
        intent.putExtra("currentAccount", this.currentAccount);
        intent.setFlags(268763140);
        ApplicationLoader.applicationContext.startActivity(intent);
        ApplicationLoader.applicationContext.sendBroadcast(new Intent("android.intent.action.CLOSE_SYSTEM_DIALOGS"));
    }

    public void removeDeletedMessagesFromNotifications(final LongSparseArray longSparseArray, final boolean z) {
        if (!AyuConfig.saveDeletedMessages || z) {
            final ArrayList arrayList = new ArrayList(0);
            notificationsQueue.postRunnable(new Runnable() { // from class: org.telegram.messenger.NotificationsController$$ExternalSyntheticLambda53
                @Override // java.lang.Runnable
                public final void run() {
                    this.f$0.lambda$removeDeletedMessagesFromNotifications$9(longSparseArray, z, arrayList);
                }
            });
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$removeDeletedMessagesFromNotifications$9(LongSparseArray longSparseArray, boolean z, final ArrayList arrayList) {
        Integer num;
        int i;
        Integer num2;
        Integer num3;
        int i2 = this.total_unread_count;
        getAccountInstance().getNotificationsSettings();
        int i3 = 0;
        Integer num4 = 0;
        int i4 = 0;
        while (i4 < longSparseArray.size()) {
            long jKeyAt = longSparseArray.keyAt(i4);
            SparseArray sparseArray = (SparseArray) this.pushMessagesDict.get(jKeyAt);
            if (sparseArray == null) {
                num = num4;
                i = i4;
            } else {
                ArrayList arrayList2 = (ArrayList) longSparseArray.get(jKeyAt);
                int size = arrayList2.size();
                int i5 = i3;
                while (i5 < size) {
                    int iIntValue = ((Integer) arrayList2.get(i5)).intValue();
                    MessageObject messageObject = (MessageObject) sparseArray.get(iIntValue);
                    if (messageObject == null) {
                        num2 = num4;
                        i4 = i4;
                    } else if (!messageObject.isStoryReactionPush && (!z || messageObject.isReactionPush)) {
                        num2 = num4;
                        long dialogId = messageObject.getDialogId();
                        Integer num5 = (Integer) this.pushDialogs.get(dialogId);
                        if (num5 == null) {
                            num5 = num2;
                        }
                        int iIntValue2 = num5.intValue() - 1;
                        Integer numValueOf = Integer.valueOf(iIntValue2);
                        if (iIntValue2 <= 0) {
                            this.smartNotificationsDialogs.remove(dialogId);
                            num3 = num2;
                        } else {
                            num3 = numValueOf;
                        }
                        if (!num3.equals(num5)) {
                            if (getMessagesController().isForum(dialogId)) {
                                int i6 = this.total_unread_count - (num5.intValue() > 0 ? 1 : 0);
                                this.total_unread_count = i6;
                                this.total_unread_count = i6 + (num3.intValue() > 0 ? 1 : 0);
                            } else {
                                int iIntValue3 = this.total_unread_count - num5.intValue();
                                this.total_unread_count = iIntValue3;
                                this.total_unread_count = iIntValue3 + num3.intValue();
                            }
                            this.pushDialogs.put(dialogId, num3);
                        }
                        if (num3.intValue() == 0) {
                            this.pushDialogs.remove(dialogId);
                            this.pushDialogsOverrideMention.remove(dialogId);
                        }
                        sparseArray.remove(iIntValue);
                        this.delayedPushMessages.remove(messageObject);
                        this.pushMessages.remove(messageObject);
                        if (isPersonalMessage(messageObject)) {
                            this.personalCount--;
                        }
                        arrayList.add(messageObject);
                    } else {
                        num2 = num4;
                        i4 = i4;
                    }
                    i5++;
                    num4 = num2;
                    i4 = i4;
                }
                num = num4;
                i = i4;
                if (sparseArray.size() == 0) {
                    this.pushMessagesDict.remove(jKeyAt);
                }
            }
            i4 = i + 1;
            num4 = num;
            i3 = 0;
        }
        if (!arrayList.isEmpty()) {
            AndroidUtilities.runOnUIThread(new Runnable() { // from class: org.telegram.messenger.NotificationsController$$ExternalSyntheticLambda20
                @Override // java.lang.Runnable
                public final void run() {
                    this.f$0.lambda$removeDeletedMessagesFromNotifications$7(arrayList);
                }
            });
        }
        if (i2 != this.total_unread_count) {
            if (!this.notifyCheck) {
                this.delayedPushMessages.clear();
                showOrUpdateNotification(this.notifyCheck);
            } else {
                scheduleNotificationDelay(this.lastOnlineFromOtherDevice > getConnectionsManager().getCurrentTime());
            }
            final int size2 = this.pushDialogs.size();
            AndroidUtilities.runOnUIThread(new Runnable() { // from class: org.telegram.messenger.NotificationsController$$ExternalSyntheticLambda21
                @Override // java.lang.Runnable
                public final void run() {
                    this.f$0.lambda$removeDeletedMessagesFromNotifications$8(size2);
                }
            });
        }
        this.notifyCheck = false;
        if (this.showBadgeNumber) {
            setBadge(getTotalAllUnreadCount());
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$removeDeletedMessagesFromNotifications$7(ArrayList arrayList) {
        int size = arrayList.size();
        for (int i = 0; i < size; i++) {
            this.popupMessages.remove(arrayList.get(i));
        }
        NotificationCenter.getGlobalInstance().lambda$postNotificationNameOnUIThread$1(NotificationCenter.pushMessagesUpdated, new Object[0]);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$removeDeletedMessagesFromNotifications$8(int i) {
        NotificationCenter.getGlobalInstance().lambda$postNotificationNameOnUIThread$1(NotificationCenter.notificationsCountUpdated, Integer.valueOf(this.currentAccount));
        getNotificationCenter().lambda$postNotificationNameOnUIThread$1(NotificationCenter.dialogsUnreadCounterChanged, Integer.valueOf(i));
    }

    public void removeDeletedHisoryFromNotifications(final LongSparseIntArray longSparseIntArray) {
        if (AyuConfig.saveDeletedMessages) {
            return;
        }
        final ArrayList arrayList = new ArrayList(0);
        notificationsQueue.postRunnable(new Runnable() { // from class: org.telegram.messenger.NotificationsController$$ExternalSyntheticLambda8
            @Override // java.lang.Runnable
            public final void run() {
                this.f$0.lambda$removeDeletedHisoryFromNotifications$12(longSparseIntArray, arrayList);
            }
        });
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$removeDeletedHisoryFromNotifications$12(LongSparseIntArray longSparseIntArray, final ArrayList arrayList) {
        Integer num;
        int i = this.total_unread_count;
        getAccountInstance().getNotificationsSettings();
        int i2 = 0;
        Integer num2 = 0;
        int i3 = 0;
        while (i3 < longSparseIntArray.size()) {
            long jKeyAt = longSparseIntArray.keyAt(i3);
            long j = -jKeyAt;
            long j2 = longSparseIntArray.get(jKeyAt);
            Integer num3 = (Integer) this.pushDialogs.get(j);
            if (num3 == null) {
                num3 = num2;
            }
            int i4 = i2;
            Integer numValueOf = num3;
            while (i4 < this.pushMessages.size()) {
                MessageObject messageObject = this.pushMessages.get(i4);
                if (messageObject.getDialogId() == j) {
                    num = num2;
                    if (messageObject.getId() <= j2) {
                        SparseArray sparseArray = (SparseArray) this.pushMessagesDict.get(j);
                        if (sparseArray != null) {
                            sparseArray.remove(messageObject.getId());
                            if (sparseArray.size() == 0) {
                                this.pushMessagesDict.remove(j);
                            }
                        }
                        this.delayedPushMessages.remove(messageObject);
                        this.pushMessages.remove(messageObject);
                        i4--;
                        if (isPersonalMessage(messageObject)) {
                            this.personalCount--;
                        }
                        arrayList.add(messageObject);
                        numValueOf = Integer.valueOf(numValueOf.intValue() - 1);
                    }
                } else {
                    num = num2;
                }
                i4++;
                num2 = num;
            }
            Integer num4 = num2;
            if (numValueOf.intValue() <= 0) {
                this.smartNotificationsDialogs.remove(j);
                numValueOf = num4;
            }
            if (!numValueOf.equals(num3)) {
                if (getMessagesController().isForum(j)) {
                    int i5 = this.total_unread_count - (num3.intValue() > 0 ? 1 : 0);
                    this.total_unread_count = i5;
                    this.total_unread_count = i5 + (numValueOf.intValue() > 0 ? 1 : 0);
                } else {
                    int iIntValue = this.total_unread_count - num3.intValue();
                    this.total_unread_count = iIntValue;
                    this.total_unread_count = iIntValue + numValueOf.intValue();
                }
                this.pushDialogs.put(j, numValueOf);
            }
            if (numValueOf.intValue() == 0) {
                this.pushDialogs.remove(j);
                this.pushDialogsOverrideMention.remove(j);
            }
            i3++;
            num2 = num4;
            i2 = 0;
        }
        if (arrayList.isEmpty()) {
            AndroidUtilities.runOnUIThread(new Runnable() { // from class: org.telegram.messenger.NotificationsController$$ExternalSyntheticLambda10
                @Override // java.lang.Runnable
                public final void run() {
                    this.f$0.lambda$removeDeletedHisoryFromNotifications$10(arrayList);
                }
            });
        }
        if (i != this.total_unread_count) {
            if (!this.notifyCheck) {
                this.delayedPushMessages.clear();
                showOrUpdateNotification(this.notifyCheck);
            } else {
                scheduleNotificationDelay(this.lastOnlineFromOtherDevice > getConnectionsManager().getCurrentTime());
            }
            final int size = this.pushDialogs.size();
            AndroidUtilities.runOnUIThread(new Runnable() { // from class: org.telegram.messenger.NotificationsController$$ExternalSyntheticLambda11
                @Override // java.lang.Runnable
                public final void run() {
                    this.f$0.lambda$removeDeletedHisoryFromNotifications$11(size);
                }
            });
        }
        this.notifyCheck = false;
        if (this.showBadgeNumber) {
            setBadge(getTotalAllUnreadCount());
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$removeDeletedHisoryFromNotifications$10(ArrayList arrayList) {
        int size = arrayList.size();
        for (int i = 0; i < size; i++) {
            this.popupMessages.remove(arrayList.get(i));
        }
        NotificationCenter.getGlobalInstance().lambda$postNotificationNameOnUIThread$1(NotificationCenter.pushMessagesUpdated, new Object[0]);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$removeDeletedHisoryFromNotifications$11(int i) {
        NotificationCenter.getGlobalInstance().lambda$postNotificationNameOnUIThread$1(NotificationCenter.notificationsCountUpdated, Integer.valueOf(this.currentAccount));
        getNotificationCenter().lambda$postNotificationNameOnUIThread$1(NotificationCenter.dialogsUnreadCounterChanged, Integer.valueOf(i));
    }

    public void processSeenStoryReactions(long j, final int i) {
        if (j != getUserConfig().getClientUserId()) {
            return;
        }
        notificationsQueue.postRunnable(new Runnable() { // from class: org.telegram.messenger.NotificationsController$$ExternalSyntheticLambda26
            @Override // java.lang.Runnable
            public final void run() {
                this.f$0.lambda$processSeenStoryReactions$13(i);
            }
        });
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$processSeenStoryReactions$13(int i) {
        int i2 = 0;
        boolean z = false;
        while (i2 < this.pushMessages.size()) {
            MessageObject messageObject = this.pushMessages.get(i2);
            if (messageObject.isStoryReactionPush && Math.abs(messageObject.getId()) == i) {
                this.pushMessages.remove(i2);
                SparseArray sparseArray = (SparseArray) this.pushMessagesDict.get(messageObject.getDialogId());
                if (sparseArray != null) {
                    sparseArray.remove(messageObject.getId());
                }
                if (sparseArray != null && sparseArray.size() <= 0) {
                    this.pushMessagesDict.remove(messageObject.getDialogId());
                }
                ArrayList<Integer> arrayList = new ArrayList<>();
                arrayList.add(Integer.valueOf(messageObject.getId()));
                getMessagesStorage().deletePushMessages(messageObject.getDialogId(), arrayList);
                i2--;
                z = true;
            }
            i2++;
        }
        if (z) {
            showOrUpdateNotification(false);
        }
    }

    public void processDeleteStory(final long j, final int i) {
        notificationsQueue.postRunnable(new Runnable() { // from class: org.telegram.messenger.NotificationsController$$ExternalSyntheticLambda25
            @Override // java.lang.Runnable
            public final void run() {
                this.f$0.lambda$processDeleteStory$14(j, i);
            }
        });
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$processDeleteStory$14(long j, int i) {
        boolean z;
        StoryNotification storyNotification = (StoryNotification) this.storyPushMessagesDict.get(j);
        if (storyNotification != null) {
            storyNotification.dateByIds.remove(Integer.valueOf(i));
            if (storyNotification.dateByIds.isEmpty()) {
                this.storyPushMessagesDict.remove(j);
                this.storyPushMessages.remove(storyNotification);
                getMessagesStorage().deleteStoryPushMessage(j);
                z = true;
            } else {
                getMessagesStorage().putStoryPushMessage(storyNotification);
                z = false;
            }
        } else {
            z = false;
        }
        int i2 = 0;
        while (i2 < this.pushMessages.size()) {
            MessageObject messageObject = this.pushMessages.get(i2);
            if (messageObject != null && messageObject.isLiveStoryPush && messageObject.getId() == i) {
                this.pushMessages.remove(i2);
                i2--;
                SparseArray sparseArray = (SparseArray) this.pushMessagesDict.get(messageObject.getDialogId());
                if (sparseArray != null) {
                    sparseArray.remove(messageObject.getId());
                }
                if (sparseArray != null && sparseArray.size() <= 0) {
                    this.pushMessagesDict.remove(messageObject.getDialogId());
                }
                z = true;
            }
            i2++;
        }
        if (z) {
            showOrUpdateNotification(false);
        }
    }

    public void processReadStories(final long j, final int i) {
        notificationsQueue.postRunnable(new Runnable() { // from class: org.telegram.messenger.NotificationsController$$ExternalSyntheticLambda42
            @Override // java.lang.Runnable
            public final void run() {
                this.f$0.lambda$processReadStories$15(j, i);
            }
        });
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$processReadStories$15(long j, int i) {
        boolean z;
        StoryNotification storyNotification = (StoryNotification) this.storyPushMessagesDict.get(j);
        if (storyNotification != null) {
            this.storyPushMessagesDict.remove(j);
            this.storyPushMessages.remove(storyNotification);
            getMessagesStorage().deleteStoryPushMessage(j);
            z = true;
        } else {
            z = false;
        }
        int i2 = 0;
        while (i2 < this.pushMessages.size()) {
            MessageObject messageObject = this.pushMessages.get(i2);
            if (messageObject != null && messageObject.isLiveStoryPush && messageObject.getId() <= i) {
                this.pushMessages.remove(i2);
                i2--;
                SparseArray sparseArray = (SparseArray) this.pushMessagesDict.get(messageObject.getDialogId());
                if (sparseArray != null) {
                    sparseArray.remove(messageObject.getId());
                }
                if (sparseArray != null && sparseArray.size() <= 0) {
                    this.pushMessagesDict.remove(messageObject.getDialogId());
                }
                z = true;
            }
            i2++;
        }
        if (z) {
            showOrUpdateNotification(false);
            updateStoryPushesRunnable();
        }
    }

    public void processIgnoreStories() {
        notificationsQueue.postRunnable(new Runnable() { // from class: org.telegram.messenger.NotificationsController$$ExternalSyntheticLambda37
            @Override // java.lang.Runnable
            public final void run() {
                this.f$0.lambda$processIgnoreStories$16();
            }
        });
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$processIgnoreStories$16() {
        boolean zIsEmpty = this.storyPushMessages.isEmpty();
        this.storyPushMessages.clear();
        this.storyPushMessagesDict.clear();
        getMessagesStorage().deleteAllStoryPushMessages();
        if (zIsEmpty) {
            return;
        }
        showOrUpdateNotification(false);
    }

    public void processIgnoreStoryReactions() {
        notificationsQueue.postRunnable(new Runnable() { // from class: org.telegram.messenger.NotificationsController$$ExternalSyntheticLambda9
            @Override // java.lang.Runnable
            public final void run() {
                this.f$0.lambda$processIgnoreStoryReactions$17();
            }
        });
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$processIgnoreStoryReactions$17() {
        int i = 0;
        boolean z = false;
        while (i < this.pushMessages.size()) {
            MessageObject messageObject = this.pushMessages.get(i);
            if (messageObject != null && messageObject.isStoryReactionPush) {
                this.pushMessages.remove(i);
                i--;
                SparseArray sparseArray = (SparseArray) this.pushMessagesDict.get(messageObject.getDialogId());
                if (sparseArray != null) {
                    sparseArray.remove(messageObject.getId());
                }
                if (sparseArray != null && sparseArray.size() <= 0) {
                    this.pushMessagesDict.remove(messageObject.getDialogId());
                }
                z = true;
            }
            i++;
        }
        getMessagesStorage().deleteAllStoryReactionPushMessages();
        if (z) {
            showOrUpdateNotification(false);
        }
    }

    public void processIgnoreStories(final long j) {
        notificationsQueue.postRunnable(new Runnable() { // from class: org.telegram.messenger.NotificationsController$$ExternalSyntheticLambda22
            @Override // java.lang.Runnable
            public final void run() {
                this.f$0.lambda$processIgnoreStories$18(j);
            }
        });
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$processIgnoreStories$18(long j) {
        boolean zIsEmpty = this.storyPushMessages.isEmpty();
        this.storyPushMessages.clear();
        this.storyPushMessagesDict.clear();
        getMessagesStorage().deleteStoryPushMessage(j);
        if (zIsEmpty) {
            return;
        }
        showOrUpdateNotification(false);
    }

    public void processReadMessages(final LongSparseIntArray longSparseIntArray, final long j, final int i, final int i2, final boolean z) {
        final ArrayList arrayList = new ArrayList(0);
        notificationsQueue.postRunnable(new Runnable() { // from class: org.telegram.messenger.NotificationsController$$ExternalSyntheticLambda52
            @Override // java.lang.Runnable
            public final void run() {
                this.f$0.lambda$processReadMessages$20(longSparseIntArray, arrayList, j, i2, i, z);
            }
        });
    }

    /* JADX INFO: Access modifiers changed from: private */
    /* JADX WARN: Code duplicated, block: B:60:0x00e6  */
    /* JADX WARN: Code duplicated, block: B:62:0x00ec  */
    /* JADX WARN: Code duplicated, block: B:65:0x00f6  */
    /* JADX WARN: Code duplicated, block: B:66:0x00fb  */
    /* JADX WARN: Code duplicated, block: B:68:0x0105  */
    /* JADX WARN: Code duplicated, block: B:69:0x0107  */
    /* JADX WARN: Code duplicated, block: B:72:0x0113  */
    /* JADX WARN: Code duplicated, block: B:74:0x0120  */
    public /* synthetic */ void lambda$processReadMessages$20(LongSparseIntArray longSparseIntArray, final ArrayList arrayList, long j, int i, int i2, boolean z) {
        long j2;
        long dialogId;
        SparseArray sparseArray;
        long dialogId2;
        long j3 = 0;
        if (longSparseIntArray != null) {
            for (int i3 = 0; i3 < longSparseIntArray.size(); i3++) {
                long jKeyAt = longSparseIntArray.keyAt(i3);
                int i4 = longSparseIntArray.get(jKeyAt);
                int i5 = 0;
                while (i5 < this.pushMessages.size()) {
                    MessageObject messageObject = this.pushMessages.get(i5);
                    if (!messageObject.messageOwner.from_scheduled && messageObject.getDialogId() == jKeyAt && messageObject.getId() <= i4 && !messageObject.isStoryReactionPush) {
                        if (isPersonalMessage(messageObject)) {
                            this.personalCount--;
                        }
                        arrayList.add(messageObject);
                        if (messageObject.isStoryReactionPush) {
                            dialogId2 = messageObject.getDialogId();
                        } else {
                            long j4 = messageObject.messageOwner.peer_id.channel_id;
                            dialogId2 = j4 != j3 ? -j4 : j3;
                        }
                        SparseArray sparseArray2 = (SparseArray) this.pushMessagesDict.get(dialogId2);
                        if (sparseArray2 != null) {
                            sparseArray2.remove(messageObject.getId());
                            if (sparseArray2.size() == 0) {
                                this.pushMessagesDict.remove(dialogId2);
                            }
                        }
                        this.delayedPushMessages.remove(messageObject);
                        this.pushMessages.remove(i5);
                        i5--;
                    }
                    i5++;
                    j3 = j3;
                }
            }
        }
        long j5 = j3;
        if (j != j5 && (i != 0 || i2 != 0)) {
            int i6 = 0;
            while (i6 < this.pushMessages.size()) {
                MessageObject messageObject2 = this.pushMessages.get(i6);
                if (messageObject2.getDialogId() == j && !messageObject2.isStoryReactionPush) {
                    if (i2 != 0) {
                        if (messageObject2.messageOwner.date <= i2) {
                            if (isPersonalMessage(messageObject2)) {
                                this.personalCount--;
                            }
                            if (messageObject2.isStoryReactionPush) {
                                dialogId = messageObject2.getDialogId();
                            } else {
                                j2 = messageObject2.messageOwner.peer_id.channel_id;
                                if (j2 != j5) {
                                    dialogId = -j2;
                                } else {
                                    dialogId = j5;
                                }
                            }
                            sparseArray = (SparseArray) this.pushMessagesDict.get(dialogId);
                            if (sparseArray != null) {
                                sparseArray.remove(messageObject2.getId());
                                if (sparseArray.size() == 0) {
                                    this.pushMessagesDict.remove(dialogId);
                                }
                            }
                            this.pushMessages.remove(i6);
                            this.delayedPushMessages.remove(messageObject2);
                            arrayList.add(messageObject2);
                            i6--;
                        }
                    } else if (!z) {
                        if (messageObject2.getId() <= i || i < 0) {
                            if (isPersonalMessage(messageObject2)) {
                                this.personalCount--;
                            }
                            if (messageObject2.isStoryReactionPush) {
                                dialogId = messageObject2.getDialogId();
                            } else {
                                j2 = messageObject2.messageOwner.peer_id.channel_id;
                                if (j2 != j5) {
                                    dialogId = -j2;
                                } else {
                                    dialogId = j5;
                                }
                            }
                            sparseArray = (SparseArray) this.pushMessagesDict.get(dialogId);
                            if (sparseArray != null) {
                                sparseArray.remove(messageObject2.getId());
                                if (sparseArray.size() == 0) {
                                    this.pushMessagesDict.remove(dialogId);
                                }
                            }
                            this.pushMessages.remove(i6);
                            this.delayedPushMessages.remove(messageObject2);
                            arrayList.add(messageObject2);
                            i6--;
                        }
                    } else if (messageObject2.getId() == i || i < 0) {
                        if (isPersonalMessage(messageObject2)) {
                            this.personalCount--;
                        }
                        if (messageObject2.isStoryReactionPush) {
                            dialogId = messageObject2.getDialogId();
                        } else {
                            j2 = messageObject2.messageOwner.peer_id.channel_id;
                            if (j2 != j5) {
                                dialogId = -j2;
                            } else {
                                dialogId = j5;
                            }
                        }
                        sparseArray = (SparseArray) this.pushMessagesDict.get(dialogId);
                        if (sparseArray != null) {
                            sparseArray.remove(messageObject2.getId());
                            if (sparseArray.size() == 0) {
                                this.pushMessagesDict.remove(dialogId);
                            }
                        }
                        this.pushMessages.remove(i6);
                        this.delayedPushMessages.remove(messageObject2);
                        arrayList.add(messageObject2);
                        i6--;
                    }
                }
                i6++;
            }
        }
        if (arrayList.isEmpty()) {
            return;
        }
        AndroidUtilities.runOnUIThread(new Runnable() { // from class: org.telegram.messenger.NotificationsController$$ExternalSyntheticLambda45
            @Override // java.lang.Runnable
            public final void run() {
                this.f$0.lambda$processReadMessages$19(arrayList);
            }
        });
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$processReadMessages$19(ArrayList arrayList) {
        int size = arrayList.size();
        for (int i = 0; i < size; i++) {
            this.popupMessages.remove(arrayList.get(i));
        }
        NotificationCenter.getGlobalInstance().lambda$postNotificationNameOnUIThread$1(NotificationCenter.pushMessagesUpdated, new Object[0]);
    }

    /* JADX WARN: Code duplicated, block: B:24:0x0061  */
    private int addToPopupMessages(ArrayList<MessageObject> arrayList, MessageObject messageObject, long j, boolean z, SharedPreferences sharedPreferences) {
        int i;
        if (messageObject.isStoryReactionPush) {
            return 0;
        }
        if (DialogObject.isEncryptedDialog(j)) {
            i = 0;
        } else {
            if (sharedPreferences.getBoolean(NotificationsSettingsFacade.PROPERTY_CUSTOM + j, false)) {
                i = sharedPreferences.getInt("popup_" + j, 0);
            } else {
                i = 0;
            }
            if (i == 0) {
                if (z) {
                    i = sharedPreferences.getInt("popupChannel", 0);
                } else {
                    i = sharedPreferences.getInt(DialogObject.isChatDialog(j) ? "popupGroup" : "popupAll", 0);
                }
            } else if (i == 1) {
                i = 3;
            } else if (i == 2) {
                i = 0;
            }
        }
        if (i != 0 && messageObject.messageOwner.peer_id.channel_id != 0 && !messageObject.isSupergroup()) {
            i = 0;
        }
        if (i != 0) {
            arrayList.add(0, messageObject);
        }
        return i;
    }

    public void processEditedMessages(final LongSparseArray longSparseArray) {
        TLRPC.Message message;
        if (longSparseArray == null || longSparseArray.size() == 0) {
            return;
        }
        for (int i = 0; i < longSparseArray.size(); i++) {
            ArrayList arrayList = (ArrayList) longSparseArray.valueAt(i);
            if (arrayList != null) {
                for (int i2 = 0; i2 < arrayList.size(); i2++) {
                    MessageObject messageObject = (MessageObject) arrayList.get(i2);
                    if (messageObject != null && (message = messageObject.messageOwner) != null) {
                        TLRPC.MessageAction messageAction = message.action;
                        if (messageAction instanceof TLRPC.TL_messageActionConferenceCall) {
                            TLRPC.TL_messageActionConferenceCall tL_messageActionConferenceCall = (TLRPC.TL_messageActionConferenceCall) messageAction;
                            if (tL_messageActionConferenceCall.active || tL_messageActionConferenceCall.missed) {
                                VoIPGroupNotification.hide(ApplicationLoader.applicationContext, this.currentAccount, messageObject.getId());
                            }
                        }
                    }
                }
            }
        }
        new ArrayList(0);
        notificationsQueue.postRunnable(new Runnable() { // from class: org.telegram.messenger.NotificationsController$$ExternalSyntheticLambda58
            @Override // java.lang.Runnable
            public final void run() {
                this.f$0.lambda$processEditedMessages$21(longSparseArray);
            }
        });
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$processEditedMessages$21(LongSparseArray longSparseArray) {
        long dialogId;
        int size = longSparseArray.size();
        boolean z = false;
        for (int i = 0; i < size; i++) {
            longSparseArray.keyAt(i);
            ArrayList arrayList = (ArrayList) longSparseArray.valueAt(i);
            int size2 = arrayList.size();
            for (int i2 = 0; i2 < size2; i2++) {
                MessageObject messageObject = (MessageObject) arrayList.get(i2);
                if (messageObject.isStoryReactionPush) {
                    dialogId = messageObject.getDialogId();
                } else {
                    long j = messageObject.messageOwner.peer_id.channel_id;
                    dialogId = j != 0 ? -j : 0L;
                }
                SparseArray sparseArray = (SparseArray) this.pushMessagesDict.get(dialogId);
                if (sparseArray == null) {
                    break;
                }
                MessageObject messageObject2 = (MessageObject) sparseArray.get(messageObject.getId());
                if (messageObject2 != null && (messageObject2.isReactionPush || messageObject2.isStoryReactionPush)) {
                    messageObject2 = null;
                }
                if (messageObject2 != null) {
                    sparseArray.put(messageObject.getId(), messageObject);
                    int iIndexOf = this.pushMessages.indexOf(messageObject2);
                    if (iIndexOf >= 0) {
                        this.pushMessages.set(iIndexOf, messageObject);
                    }
                    int iIndexOf2 = this.delayedPushMessages.indexOf(messageObject2);
                    if (iIndexOf2 >= 0) {
                        this.delayedPushMessages.set(iIndexOf2, messageObject);
                    }
                    z = true;
                }
            }
        }
        if (z) {
            showOrUpdateNotification(false);
        }
    }

    public void processNewMessages(final ArrayList<MessageObject> arrayList, boolean z, boolean z2, final CountDownLatch countDownLatch) {
        final boolean z3;
        final boolean z4;
        if (BuildVars.LOGS_ENABLED) {
            StringBuilder sb = new StringBuilder();
            sb.append("NotificationsController: processNewMessages msgs.size()=");
            sb.append(arrayList == null ? "null" : Integer.valueOf(arrayList.size()));
            sb.append(" isLast=");
            z3 = z;
            sb.append(z3);
            sb.append(" isFcm=");
            z4 = z2;
            sb.append(z4);
            sb.append(")");
            FileLog.d(sb.toString());
        } else {
            z3 = z;
            z4 = z2;
        }
        if (arrayList != null) {
            int i = 0;
            while (i < arrayList.size()) {
                MessageObject messageObject = arrayList.get(i);
                if (messageObject != null && messageObject.messageOwner != null && !messageObject.isOutOwner()) {
                    TLRPC.MessageAction messageAction = messageObject.messageOwner.action;
                    if (messageAction instanceof TLRPC.TL_messageActionConferenceCall) {
                        TLRPC.TL_messageActionConferenceCall tL_messageActionConferenceCall = (TLRPC.TL_messageActionConferenceCall) messageAction;
                        if (!tL_messageActionConferenceCall.active && !tL_messageActionConferenceCall.missed && getConnectionsManager().getCurrentTime() - messageObject.messageOwner.date < ((long) getMessagesController().callRingTimeout) / 1000) {
                            HashSet hashSet = new HashSet();
                            hashSet.add(Long.valueOf(messageObject.getDialogId()));
                            ArrayList arrayList2 = tL_messageActionConferenceCall.other_participants;
                            int size = arrayList2.size();
                            int i2 = 0;
                            while (i2 < size) {
                                Object obj = arrayList2.get(i2);
                                i2++;
                                hashSet.add(Long.valueOf(DialogObject.getPeerDialogId((TLRPC.Peer) obj)));
                            }
                            StringBuilder sb2 = new StringBuilder();
                            Iterator it = hashSet.iterator();
                            while (it.hasNext()) {
                                long jLongValue = ((Long) it.next()).longValue();
                                if (sb2.length() > 0) {
                                    sb2.append(", ");
                                }
                                sb2.append(DialogObject.getShortName(this.currentAccount, jLongValue));
                            }
                            VoIPGroupNotification.request(ApplicationLoader.applicationContext, this.currentAccount, messageObject.getDialogId(), sb2.toString(), tL_messageActionConferenceCall.call_id, messageObject.getId(), tL_messageActionConferenceCall.video);
                            arrayList.remove(i);
                            i--;
                        } else {
                            VoIPGroupNotification.hide(ApplicationLoader.applicationContext, this.currentAccount, messageObject.getId());
                        }
                    }
                }
                i++;
            }
        }
        if (!arrayList.isEmpty()) {
            final ArrayList arrayList3 = new ArrayList(0);
            notificationsQueue.postRunnable(new Runnable() { // from class: org.telegram.messenger.NotificationsController$$ExternalSyntheticLambda51
                @Override // java.lang.Runnable
                public final void run() {
                    this.f$0.lambda$processNewMessages$26(arrayList, arrayList3, z4, z3, countDownLatch);
                }
            });
        } else if (countDownLatch != null) {
            countDownLatch.countDown();
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    /* JADX WARN: Code duplicated, block: B:100:0x0227  */
    /* JADX WARN: Code duplicated, block: B:102:0x0238  */
    /* JADX WARN: Code duplicated, block: B:107:0x0249  */
    /* JADX WARN: Code duplicated, block: B:110:0x024f  */
    /* JADX WARN: Code duplicated, block: B:113:0x0272  */
    /* JADX WARN: Code duplicated, block: B:115:0x0279  */
    /* JADX WARN: Code duplicated, block: B:117:0x027d  */
    /* JADX WARN: Code duplicated, block: B:118:0x0284  */
    /* JADX WARN: Code duplicated, block: B:135:0x02db  */
    /* JADX WARN: Code duplicated, block: B:137:0x02e1  */
    /* JADX WARN: Code duplicated, block: B:138:0x02e3  */
    /* JADX WARN: Code duplicated, block: B:144:0x02fe  */
    /* JADX WARN: Code duplicated, block: B:147:0x030a  */
    /* JADX WARN: Code duplicated, block: B:148:0x0313  */
    /* JADX WARN: Code duplicated, block: B:151:0x0322  */
    /* JADX WARN: Code duplicated, block: B:154:0x033c  */
    /* JADX WARN: Code duplicated, block: B:156:0x0354  */
    /* JADX WARN: Code duplicated, block: B:158:0x0366  */
    /* JADX WARN: Code duplicated, block: B:160:0x039a  */
    /* JADX WARN: Code duplicated, block: B:162:0x039d  */
    /* JADX WARN: Code duplicated, block: B:163:0x03a0  */
    /* JADX WARN: Code duplicated, block: B:167:0x03ac  */
    /* JADX WARN: Code duplicated, block: B:169:0x03e3  */
    /* JADX WARN: Code duplicated, block: B:171:0x03e9  */
    /* JADX WARN: Code duplicated, block: B:172:0x03f3  */
    /* JADX WARN: Code duplicated, block: B:174:0x03f7  */
    /* JADX WARN: Code duplicated, block: B:177:0x0407 A[DONT_INVERT] */
    /* JADX WARN: Code duplicated, block: B:178:0x0409  */
    /* JADX WARN: Code duplicated, block: B:179:0x0414  */
    /* JADX WARN: Code duplicated, block: B:17:0x004b  */
    /* JADX WARN: Code duplicated, block: B:182:0x041c  */
    /* JADX WARN: Code duplicated, block: B:184:0x0420  */
    /* JADX WARN: Code duplicated, block: B:187:0x042c  */
    /* JADX WARN: Code duplicated, block: B:189:0x0438  */
    /* JADX WARN: Code duplicated, block: B:190:0x043b  */
    /* JADX WARN: Code duplicated, block: B:192:0x0449  */
    /* JADX WARN: Code duplicated, block: B:195:0x0453  */
    /* JADX WARN: Code duplicated, block: B:21:0x0055  */
    /* JADX WARN: Code duplicated, block: B:24:0x0067  */
    /* JADX WARN: Code duplicated, block: B:32:0x008f  */
    /* JADX WARN: Code duplicated, block: B:34:0x0093  */
    /* JADX WARN: Code duplicated, block: B:36:0x009a  */
    /* JADX WARN: Code duplicated, block: B:39:0x00b2  */
    /* JADX WARN: Code duplicated, block: B:41:0x00d3  */
    /* JADX WARN: Code duplicated, block: B:43:0x00e6  */
    /* JADX WARN: Code duplicated, block: B:45:0x0121  */
    /* JADX WARN: Code duplicated, block: B:47:0x0125  */
    /* JADX WARN: Code duplicated, block: B:50:0x012b  */
    /* JADX WARN: Code duplicated, block: B:52:0x014a  */
    /* JADX WARN: Code duplicated, block: B:53:0x014e  */
    /* JADX WARN: Code duplicated, block: B:54:0x015a  */
    /* JADX WARN: Code duplicated, block: B:57:0x016a  */
    /* JADX WARN: Code duplicated, block: B:59:0x0171  */
    /* JADX WARN: Code duplicated, block: B:62:0x017e  */
    /* JADX WARN: Code duplicated, block: B:63:0x0188  */
    /* JADX WARN: Code duplicated, block: B:65:0x018e  */
    /* JADX WARN: Code duplicated, block: B:70:0x01aa  */
    /* JADX WARN: Code duplicated, block: B:72:0x01b1  */
    /* JADX WARN: Code duplicated, block: B:75:0x01b9  */
    /* JADX WARN: Code duplicated, block: B:76:0x01be  */
    /* JADX WARN: Code duplicated, block: B:78:0x01c8  */
    /* JADX WARN: Code duplicated, block: B:79:0x01ca  */
    /* JADX WARN: Code duplicated, block: B:82:0x01d6  */
    /* JADX WARN: Code duplicated, block: B:83:0x01dd  */
    /* JADX WARN: Code duplicated, block: B:86:0x01e3  */
    /* JADX WARN: Code duplicated, block: B:92:0x0205  */
    /* JADX WARN: Code duplicated, block: B:94:0x020a  */
    /* JADX WARN: Code duplicated, block: B:96:0x0210 A[DONT_INVERT] */
    /* JADX WARN: Code duplicated, block: B:97:0x0212  */
    /* JADX WARN: Instruction removed from duplicated block: B:110:0x024f, please report this as an issue */
    /* JADX WARN: Instruction removed from duplicated block: B:158:0x0366, please report this as an issue */
    /* JADX WARN: Instruction removed from duplicated block: B:167:0x03ac, please report this as an issue */
    /* JADX WARN: Multi-variable type inference failed */
    /* JADX WARN: Type inference failed for: r11v13 */
    /* JADX WARN: Type inference failed for: r11v7, types: [int] */
    /* JADX WARN: Type inference failed for: r11v8 */
    /* JADX WARN: Type inference failed for: r22v4 */
    /* JADX WARN: Type inference failed for: r22v5, types: [int] */
    /* JADX WARN: Type inference failed for: r22v6 */
    /* JADX WARN: Type inference failed for: r3v9, types: [int] */
    /* JADX WARN: Type inference failed for: r9v2 */
    /* JADX WARN: Type inference failed for: r9v3, types: [int] */
    /* JADX WARN: Type inference failed for: r9v4 */
    public /* synthetic */ void lambda$processNewMessages$26(ArrayList arrayList, final ArrayList arrayList2, boolean z, boolean z2, CountDownLatch countDownLatch) {
        long j;
        boolean zIsGlobalNotificationsEnabled;
        boolean z3;
        int iIntValue;
        Integer num;
        boolean z4;
        boolean z5;
        int i;
        int id;
        long j2;
        long dialogId;
        long j3;
        boolean z6;
        TLRPC.Chat chat;
        boolean z7;
        long j4;
        long dialogId2;
        SparseArray sparseArray;
        MessageObject messageObject;
        SharedPreferences sharedPreferences;
        long j5;
        MessageObject messageObject2;
        SharedPreferences sharedPreferences2;
        boolean z8;
        long topicId;
        TLRPC.Message message;
        long fromChatId;
        long j6;
        int iIndexOfKey;
        int notifyOverride;
        long j7;
        boolean zBooleanValue;
        boolean zIsGlobalNotificationsEnabled2;
        long j8;
        MessageObject messageObject3;
        long j9;
        Integer num2;
        ?? IntValue;
        SparseArray sparseArray2;
        int iIndexOf;
        long j10;
        TLRPC.Message message2;
        final int i2;
        long j11;
        long currentTime;
        TLRPC.Message message3;
        long jCurrentTimeMillis;
        long j12;
        long dialogId3;
        int id2;
        StoryNotification storyNotification;
        boolean z9;
        boolean z10;
        final NotificationsController notificationsController = this;
        LongSparseArray longSparseArray = new LongSparseArray();
        SharedPreferences notificationsSettings = notificationsController.getAccountInstance().getNotificationsSettings();
        boolean z11 = true;
        boolean z12 = notificationsSettings.getBoolean("PinnedMessages", true);
        int i3 = 0;
        boolean z13 = false;
        int iAddToPopupMessages = 0;
        boolean z14 = false;
        boolean z15 = false;
        boolean z16 = false;
        while (i3 < arrayList.size()) {
            MessageObject messageObject4 = (MessageObject) arrayList.get(i3);
            if (messageObject4.messageOwner == null) {
                if (!MessageObject.isTopicActionMessage(messageObject4)) {
                    if (BuildVars.LOGS_ENABLED) {
                        FileLog.d("skipped message because 1");
                    }
                } else if (!notificationsController.getAyuFilterController().isFiltered(notificationsController.getMessagesController().getChat(Long.valueOf(messageObject4.getChatId())), messageObject4, null) && !AyuMessageUtils.shouldIgnoreNotification(messageObject4)) {
                    if (messageObject4.isStoryPush) {
                        message3 = messageObject4.messageOwner;
                        if (message3 == null) {
                            jCurrentTimeMillis = System.currentTimeMillis();
                        } else {
                            jCurrentTimeMillis = ((long) message3.date) * 1000;
                        }
                        j12 = jCurrentTimeMillis;
                        dialogId3 = messageObject4.getDialogId();
                        id2 = messageObject4.getId();
                        storyNotification = (StoryNotification) notificationsController.storyPushMessagesDict.get(dialogId3);
                        if (storyNotification != null) {
                            storyNotification.dateByIds.put(Integer.valueOf(id2), new Pair<>(Long.valueOf(j12), Long.valueOf(j12 + 86400000)));
                            z9 = storyNotification.hidden;
                            z10 = messageObject4.isStoryPushHidden;
                            if (z9 != z10) {
                                storyNotification.hidden = z10;
                                z16 = z11;
                            }
                            storyNotification.date = storyNotification.getLeastDate();
                            notificationsController.getMessagesStorage().putStoryPushMessage(storyNotification);
                            z14 = z11;
                        } else {
                            StoryNotification storyNotification2 = new StoryNotification(dialogId3, messageObject4.localName, id2, j12);
                            storyNotification2.hidden = messageObject4.isStoryPushHidden;
                            notificationsController.storyPushMessages.add(storyNotification2);
                            notificationsController.storyPushMessagesDict.put(dialogId3, storyNotification2);
                            notificationsController.getMessagesStorage().putStoryPushMessage(storyNotification2);
                            z13 = z11;
                            z16 = z13;
                        }
                        Collections.sort(notificationsController.storyPushMessages, Comparator.CC.comparingLong(new ToLongFunction() { // from class: org.telegram.messenger.NotificationsController$$ExternalSyntheticLambda32
                            @Override // java.util.function.ToLongFunction
                            public final long applyAsLong(Object obj) {
                                return ((NotificationsController.StoryNotification) obj).date;
                            }
                        }));
                        notificationsSettings = notificationsSettings;
                        z5 = z12;
                        i = i3;
                    } else {
                        if (messageObject4.isOauthPush) {
                            message2 = messageObject4.messageOwner;
                            if (message2 == null) {
                                i2 = message2.id;
                                boolean z17 = z11;
                                z5 = z12;
                                j11 = ((long) message2.date) + 60;
                                z4 = z17;
                                i = i3;
                                currentTime = ConnectionsManager.getInstance(notificationsController.currentAccount).getCurrentTime();
                                if (currentTime > j11) {
                                    AndroidUtilities.runOnUIThread(new Runnable() { // from class: org.telegram.messenger.NotificationsController$$ExternalSyntheticLambda33
                                        @Override // java.lang.Runnable
                                        public final void run() {
                                            this.f$0.lambda$processNewMessages$23(i2);
                                        }
                                    }, (j11 - currentTime) * 1000);
                                }
                            }
                            z13 = z13;
                            z13 = z13;
                        } else {
                            z4 = z11;
                            z5 = z12;
                            i = i3;
                        }
                        id = messageObject4.getId();
                        if (messageObject4.isFcmMessage()) {
                            j2 = messageObject4.messageOwner.random_id;
                        } else {
                            j2 = 0;
                        }
                        dialogId = messageObject4.getDialogId();
                        if (messageObject4.isFcmMessage()) {
                            z6 = messageObject4.localChannel;
                            j3 = j2;
                        } else if (DialogObject.isChatDialog(dialogId)) {
                            chat = notificationsController.getMessagesController().getChat(Long.valueOf(-dialogId));
                            if (ChatObject.isChannel(chat) || chat.megagroup) {
                                z7 = false;
                            } else {
                                z7 = z4;
                            }
                            z6 = z7;
                            j3 = j2;
                        } else {
                            j3 = j2;
                            z6 = false;
                        }
                        if (messageObject4.isStoryReactionPush) {
                            dialogId2 = messageObject4.getDialogId();
                        } else {
                            j4 = messageObject4.messageOwner.peer_id.channel_id;
                            if (j4 != 0) {
                                dialogId2 = -j4;
                            } else {
                                dialogId2 = 0;
                            }
                        }
                        sparseArray = (SparseArray) notificationsController.pushMessagesDict.get(dialogId2);
                        if (sparseArray != null) {
                            messageObject = (MessageObject) sparseArray.get(id);
                        } else {
                            messageObject = null;
                        }
                        sharedPreferences = notificationsSettings;
                        if (messageObject == null) {
                            j5 = j3;
                            j10 = messageObject4.messageOwner.random_id;
                            if (j10 != 0 && (messageObject = (MessageObject) notificationsController.fcmRandomMessagesDict.get(j10)) != null) {
                                notificationsController.fcmRandomMessagesDict.remove(messageObject4.messageOwner.random_id);
                            }
                        } else {
                            j5 = j3;
                        }
                        messageObject2 = messageObject;
                        if (messageObject2 != null) {
                            if (messageObject2.isFcmMessage()) {
                                if (sparseArray == null) {
                                    sparseArray = new SparseArray();
                                    notificationsController.pushMessagesDict.put(dialogId2, sparseArray);
                                }
                                sparseArray.put(id, messageObject4);
                                iIndexOf = notificationsController.pushMessages.indexOf(messageObject2);
                                if (iIndexOf >= 0) {
                                    notificationsController.pushMessages.set(iIndexOf, messageObject4);
                                    iAddToPopupMessages = notificationsController.addToPopupMessages(arrayList2, messageObject4, dialogId, z6, sharedPreferences);
                                    sharedPreferences2 = sharedPreferences;
                                } else {
                                    sharedPreferences2 = sharedPreferences;
                                }
                                if (z && (z14 = messageObject4.localEdit)) {
                                    notificationsController.getMessagesStorage().putPushMessage(messageObject4);
                                }
                            } else {
                                sharedPreferences2 = sharedPreferences;
                            }
                            if (BuildVars.LOGS_ENABLED) {
                                FileLog.d("skipped message because old message with same dialog and message ids exist: did=" + dialogId2 + ", mid=" + id);
                            }
                        } else {
                            sharedPreferences2 = sharedPreferences;
                            z8 = z6;
                            if (z14) {
                                if (BuildVars.LOGS_ENABLED) {
                                    FileLog.d("skipped message because edited");
                                }
                            } else {
                                if (z && !messageObject4.isOauthPush) {
                                    notificationsController.getMessagesStorage().putPushMessage(messageObject4);
                                }
                                notificationsSettings = sharedPreferences2;
                                topicId = MessageObject.getTopicId(notificationsController.currentAccount, messageObject4.messageOwner, notificationsController.getMessagesController().isForum(messageObject4));
                                if (dialogId != notificationsController.openedDialogId && ApplicationLoader.isScreenOn && !messageObject4.isStoryReactionPush && !messageObject4.isOauthPush) {
                                    if (!z) {
                                        notificationsController.playInChatSound();
                                    }
                                    if (BuildVars.LOGS_ENABLED) {
                                        FileLog.d("skipped message because chat is already opened (openedDialogId = " + notificationsController.openedDialogId + ")");
                                    }
                                } else {
                                    message = messageObject4.messageOwner;
                                    if (!message.mentioned) {
                                        fromChatId = dialogId;
                                        j6 = fromChatId;
                                    } else if (z5 && (message.action instanceof TLRPC.TL_messageActionPinMessage)) {
                                        if (BuildVars.LOGS_ENABLED) {
                                            FileLog.d("skipped message because message is mention of pinned");
                                        }
                                    } else {
                                        j6 = dialogId;
                                        fromChatId = messageObject4.getFromChatId();
                                    }
                                    if (notificationsController.isPersonalMessage(messageObject4)) {
                                        notificationsController.personalCount++;
                                    }
                                    DialogObject.isChatDialog(fromChatId);
                                    iIndexOfKey = longSparseArray.indexOfKey(fromChatId);
                                    if (iIndexOfKey < 0 && topicId == 0) {
                                        zBooleanValue = ((Boolean) longSparseArray.valueAt(iIndexOfKey)).booleanValue();
                                        j7 = fromChatId;
                                    } else {
                                        notifyOverride = notificationsController.getNotifyOverride(notificationsSettings, fromChatId, topicId);
                                        notificationsSettings = notificationsSettings;
                                        j7 = fromChatId;
                                        topicId = topicId;
                                        if (notifyOverride == -1) {
                                            zIsGlobalNotificationsEnabled2 = isGlobalNotificationsEnabled(j7, Boolean.valueOf(z8), messageObject4.isReactionPush, messageObject4.isStoryReactionPush);
                                            if (BuildVars.LOGS_ENABLED) {
                                                FileLog.d("NotificationsController: process new messages, isGlobalNotificationsEnabled(" + j7 + ", " + z8 + ", " + messageObject4.isReactionPush + ", " + messageObject4.isStoryReactionPush + ") = " + zIsGlobalNotificationsEnabled2);
                                            }
                                            zBooleanValue = zIsGlobalNotificationsEnabled2;
                                        } else if (notifyOverride != 2) {
                                            zBooleanValue = z4;
                                        } else {
                                            zBooleanValue = false;
                                        }
                                        longSparseArray.put(j7, Boolean.valueOf(zBooleanValue));
                                    }
                                    if (BuildVars.LOGS_ENABLED) {
                                        FileLog.d("NotificationsController: process new messages, value is " + zBooleanValue + " (" + j7 + ", " + z8 + ", " + messageObject4.isReactionPush + ", " + messageObject4.isStoryReactionPush + ")");
                                    }
                                    if (zBooleanValue) {
                                        notificationsController = this;
                                        j8 = j7;
                                        if (!z) {
                                            iAddToPopupMessages = notificationsController.addToPopupMessages(arrayList2, messageObject3, j8, z8, notificationsSettings);
                                        }
                                        if (!z15) {
                                            messageObject3 = messageObject4;
                                            iAddToPopupMessages = iAddToPopupMessages;
                                            messageObject3 = messageObject4;
                                            z15 = messageObject3.messageOwner.from_scheduled;
                                        }
                                        messageObject3 = messageObject4;
                                        iAddToPopupMessages = iAddToPopupMessages;
                                        messageObject3 = messageObject4;
                                        notificationsController.delayedPushMessages.add(messageObject3);
                                        notificationsController.appendMessage(messageObject3);
                                        if (id != 0) {
                                            if (sparseArray == null) {
                                                sparseArray2 = new SparseArray();
                                                notificationsController.pushMessagesDict.put(dialogId2, sparseArray2);
                                            } else {
                                                sparseArray2 = sparseArray;
                                            }
                                            sparseArray2.put(id, messageObject3);
                                        } else if (j5 != 0) {
                                            notificationsController.fcmRandomMessagesDict.put(j5, messageObject3);
                                        }
                                        j9 = j6;
                                        if (j9 != j8) {
                                            num2 = (Integer) notificationsController.pushDialogsOverrideMention.get(j9);
                                            LongSparseArray longSparseArray2 = notificationsController.pushDialogsOverrideMention;
                                            if (num2 == null) {
                                                IntValue = z4;
                                            } else {
                                                IntValue = num2.intValue() + 1;
                                            }
                                            longSparseArray2.put(j9, Integer.valueOf((int) IntValue));
                                        }
                                    } else {
                                        notificationsController = this;
                                        j8 = j7;
                                        messageObject3 = messageObject4;
                                        iAddToPopupMessages = iAddToPopupMessages;
                                    }
                                    if (messageObject3.isReactionPush) {
                                        SparseBooleanArray sparseBooleanArray = new SparseBooleanArray();
                                        sparseBooleanArray.put(id, z4);
                                        notificationsController.getMessagesController().checkUnreadReactions(j8, topicId, sparseBooleanArray);
                                    }
                                    z14 = z14;
                                    z13 = true;
                                }
                                z13 = z13;
                            }
                        }
                        notificationsSettings = sharedPreferences2;
                        z13 = z13;
                    }
                }
                z5 = z12;
                i = i3;
                z13 = z13;
                z13 = z13;
            } else {
                if (!messageObject4.isImportedForward()) {
                    TLRPC.Message message4 = messageObject4.messageOwner;
                    TLRPC.MessageAction messageAction = message4.action;
                    if (!(messageAction instanceof TLRPC.TL_messageActionSetMessagesTTL) && (!message4.silent || (!(messageAction instanceof TLRPC.TL_messageActionContactSignUp) && !(messageAction instanceof TLRPC.TL_messageActionUserJoined)))) {
                        if (!MessageObject.isTopicActionMessage(messageObject4)) {
                            if (!notificationsController.getAyuFilterController().isFiltered(notificationsController.getMessagesController().getChat(Long.valueOf(messageObject4.getChatId())), messageObject4, null)) {
                                if (messageObject4.isStoryPush) {
                                    message3 = messageObject4.messageOwner;
                                    if (message3 == null) {
                                        jCurrentTimeMillis = System.currentTimeMillis();
                                    } else {
                                        jCurrentTimeMillis = ((long) message3.date) * 1000;
                                    }
                                    j12 = jCurrentTimeMillis;
                                    dialogId3 = messageObject4.getDialogId();
                                    id2 = messageObject4.getId();
                                    storyNotification = (StoryNotification) notificationsController.storyPushMessagesDict.get(dialogId3);
                                    if (storyNotification != null) {
                                        storyNotification.dateByIds.put(Integer.valueOf(id2), new Pair<>(Long.valueOf(j12), Long.valueOf(j12 + 86400000)));
                                        z9 = storyNotification.hidden;
                                        z10 = messageObject4.isStoryPushHidden;
                                        if (z9 != z10) {
                                            storyNotification.hidden = z10;
                                            z16 = z11;
                                        }
                                        storyNotification.date = storyNotification.getLeastDate();
                                        notificationsController.getMessagesStorage().putStoryPushMessage(storyNotification);
                                        z14 = z11;
                                    } else {
                                        StoryNotification storyNotification3 = new StoryNotification(dialogId3, messageObject4.localName, id2, j12);
                                        storyNotification3.hidden = messageObject4.isStoryPushHidden;
                                        notificationsController.storyPushMessages.add(storyNotification3);
                                        notificationsController.storyPushMessagesDict.put(dialogId3, storyNotification3);
                                        notificationsController.getMessagesStorage().putStoryPushMessage(storyNotification3);
                                        z13 = z11;
                                        z16 = z13;
                                    }
                                    Collections.sort(notificationsController.storyPushMessages, Comparator.CC.comparingLong(new ToLongFunction() { // from class: org.telegram.messenger.NotificationsController$$ExternalSyntheticLambda32
                                        @Override // java.util.function.ToLongFunction
                                        public final long applyAsLong(Object obj) {
                                            return ((NotificationsController.StoryNotification) obj).date;
                                        }
                                    }));
                                    notificationsSettings = notificationsSettings;
                                    z5 = z12;
                                    i = i3;
                                } else {
                                    if (messageObject4.isOauthPush) {
                                        message2 = messageObject4.messageOwner;
                                        if (message2 == null) {
                                            i2 = message2.id;
                                            boolean z18 = z11;
                                            z5 = z12;
                                            j11 = ((long) message2.date) + 60;
                                            z4 = z18;
                                            i = i3;
                                            currentTime = ConnectionsManager.getInstance(notificationsController.currentAccount).getCurrentTime();
                                            if (currentTime > j11) {
                                                AndroidUtilities.runOnUIThread(new Runnable() { // from class: org.telegram.messenger.NotificationsController$$ExternalSyntheticLambda33
                                                    @Override // java.lang.Runnable
                                                    public final void run() {
                                                        this.f$0.lambda$processNewMessages$23(i2);
                                                    }
                                                }, (j11 - currentTime) * 1000);
                                            }
                                        }
                                        z13 = z13;
                                        z13 = z13;
                                    } else {
                                        z4 = z11;
                                        z5 = z12;
                                        i = i3;
                                    }
                                    id = messageObject4.getId();
                                    if (messageObject4.isFcmMessage()) {
                                        j2 = messageObject4.messageOwner.random_id;
                                    } else {
                                        j2 = 0;
                                    }
                                    dialogId = messageObject4.getDialogId();
                                    if (messageObject4.isFcmMessage()) {
                                        z6 = messageObject4.localChannel;
                                        j3 = j2;
                                    } else if (DialogObject.isChatDialog(dialogId)) {
                                        chat = notificationsController.getMessagesController().getChat(Long.valueOf(-dialogId));
                                        if (ChatObject.isChannel(chat)) {
                                            z7 = false;
                                        } else {
                                            z7 = false;
                                        }
                                        z6 = z7;
                                        j3 = j2;
                                    } else {
                                        j3 = j2;
                                        z6 = false;
                                    }
                                    if (messageObject4.isStoryReactionPush) {
                                        dialogId2 = messageObject4.getDialogId();
                                    } else {
                                        j4 = messageObject4.messageOwner.peer_id.channel_id;
                                        if (j4 != 0) {
                                            dialogId2 = -j4;
                                        } else {
                                            dialogId2 = 0;
                                        }
                                    }
                                    sparseArray = (SparseArray) notificationsController.pushMessagesDict.get(dialogId2);
                                    if (sparseArray != null) {
                                        messageObject = (MessageObject) sparseArray.get(id);
                                    } else {
                                        messageObject = null;
                                    }
                                    sharedPreferences = notificationsSettings;
                                    if (messageObject == null) {
                                        j5 = j3;
                                        j10 = messageObject4.messageOwner.random_id;
                                        if (j10 != 0) {
                                            notificationsController.fcmRandomMessagesDict.remove(messageObject4.messageOwner.random_id);
                                        }
                                    } else {
                                        j5 = j3;
                                    }
                                    messageObject2 = messageObject;
                                    if (messageObject2 != null) {
                                        if (messageObject2.isFcmMessage()) {
                                            if (sparseArray == null) {
                                                sparseArray = new SparseArray();
                                                notificationsController.pushMessagesDict.put(dialogId2, sparseArray);
                                            }
                                            sparseArray.put(id, messageObject4);
                                            iIndexOf = notificationsController.pushMessages.indexOf(messageObject2);
                                            if (iIndexOf >= 0) {
                                                notificationsController.pushMessages.set(iIndexOf, messageObject4);
                                                iAddToPopupMessages = notificationsController.addToPopupMessages(arrayList2, messageObject4, dialogId, z6, sharedPreferences);
                                                sharedPreferences2 = sharedPreferences;
                                            } else {
                                                sharedPreferences2 = sharedPreferences;
                                            }
                                            if (z) {
                                                notificationsController.getMessagesStorage().putPushMessage(messageObject4);
                                            }
                                        } else {
                                            sharedPreferences2 = sharedPreferences;
                                        }
                                        if (BuildVars.LOGS_ENABLED) {
                                            FileLog.d("skipped message because old message with same dialog and message ids exist: did=" + dialogId2 + ", mid=" + id);
                                        }
                                    } else {
                                        sharedPreferences2 = sharedPreferences;
                                        z8 = z6;
                                        if (z14) {
                                            if (BuildVars.LOGS_ENABLED) {
                                                FileLog.d("skipped message because edited");
                                            }
                                        } else {
                                            if (z) {
                                                notificationsController.getMessagesStorage().putPushMessage(messageObject4);
                                            }
                                            notificationsSettings = sharedPreferences2;
                                            topicId = MessageObject.getTopicId(notificationsController.currentAccount, messageObject4.messageOwner, notificationsController.getMessagesController().isForum(messageObject4));
                                            if (dialogId != notificationsController.openedDialogId) {
                                                message = messageObject4.messageOwner;
                                                if (!message.mentioned) {
                                                    if (z5) {
                                                    }
                                                    j6 = dialogId;
                                                    fromChatId = messageObject4.getFromChatId();
                                                } else {
                                                    fromChatId = dialogId;
                                                    j6 = fromChatId;
                                                }
                                                if (notificationsController.isPersonalMessage(messageObject4)) {
                                                    notificationsController.personalCount++;
                                                }
                                                DialogObject.isChatDialog(fromChatId);
                                                iIndexOfKey = longSparseArray.indexOfKey(fromChatId);
                                                if (iIndexOfKey < 0) {
                                                    notifyOverride = notificationsController.getNotifyOverride(notificationsSettings, fromChatId, topicId);
                                                    notificationsSettings = notificationsSettings;
                                                    j7 = fromChatId;
                                                    topicId = topicId;
                                                    if (notifyOverride == -1) {
                                                        zIsGlobalNotificationsEnabled2 = isGlobalNotificationsEnabled(j7, Boolean.valueOf(z8), messageObject4.isReactionPush, messageObject4.isStoryReactionPush);
                                                        if (BuildVars.LOGS_ENABLED) {
                                                            FileLog.d("NotificationsController: process new messages, isGlobalNotificationsEnabled(" + j7 + ", " + z8 + ", " + messageObject4.isReactionPush + ", " + messageObject4.isStoryReactionPush + ") = " + zIsGlobalNotificationsEnabled2);
                                                        }
                                                        zBooleanValue = zIsGlobalNotificationsEnabled2;
                                                    } else if (notifyOverride != 2) {
                                                        zBooleanValue = z4;
                                                    } else {
                                                        zBooleanValue = false;
                                                    }
                                                    longSparseArray.put(j7, Boolean.valueOf(zBooleanValue));
                                                } else {
                                                    notifyOverride = notificationsController.getNotifyOverride(notificationsSettings, fromChatId, topicId);
                                                    notificationsSettings = notificationsSettings;
                                                    j7 = fromChatId;
                                                    topicId = topicId;
                                                    if (notifyOverride == -1) {
                                                        zIsGlobalNotificationsEnabled2 = isGlobalNotificationsEnabled(j7, Boolean.valueOf(z8), messageObject4.isReactionPush, messageObject4.isStoryReactionPush);
                                                        if (BuildVars.LOGS_ENABLED) {
                                                            FileLog.d("NotificationsController: process new messages, isGlobalNotificationsEnabled(" + j7 + ", " + z8 + ", " + messageObject4.isReactionPush + ", " + messageObject4.isStoryReactionPush + ") = " + zIsGlobalNotificationsEnabled2);
                                                        }
                                                        zBooleanValue = zIsGlobalNotificationsEnabled2;
                                                    } else if (notifyOverride != 2) {
                                                        zBooleanValue = z4;
                                                    } else {
                                                        zBooleanValue = false;
                                                    }
                                                    longSparseArray.put(j7, Boolean.valueOf(zBooleanValue));
                                                }
                                                if (BuildVars.LOGS_ENABLED) {
                                                    FileLog.d("NotificationsController: process new messages, value is " + zBooleanValue + " (" + j7 + ", " + z8 + ", " + messageObject4.isReactionPush + ", " + messageObject4.isStoryReactionPush + ")");
                                                }
                                                if (zBooleanValue) {
                                                    notificationsController = this;
                                                    j8 = j7;
                                                    if (!z) {
                                                        iAddToPopupMessages = notificationsController.addToPopupMessages(arrayList2, messageObject3, j8, z8, notificationsSettings);
                                                    }
                                                    if (!z15) {
                                                        messageObject3 = messageObject4;
                                                        iAddToPopupMessages = iAddToPopupMessages;
                                                        messageObject3 = messageObject4;
                                                        z15 = messageObject3.messageOwner.from_scheduled;
                                                    }
                                                    messageObject3 = messageObject4;
                                                    iAddToPopupMessages = iAddToPopupMessages;
                                                    messageObject3 = messageObject4;
                                                    notificationsController.delayedPushMessages.add(messageObject3);
                                                    notificationsController.appendMessage(messageObject3);
                                                    if (id != 0) {
                                                        if (sparseArray == null) {
                                                            sparseArray2 = new SparseArray();
                                                            notificationsController.pushMessagesDict.put(dialogId2, sparseArray2);
                                                        } else {
                                                            sparseArray2 = sparseArray;
                                                        }
                                                        sparseArray2.put(id, messageObject3);
                                                    } else if (j5 != 0) {
                                                        notificationsController.fcmRandomMessagesDict.put(j5, messageObject3);
                                                    }
                                                    j9 = j6;
                                                    if (j9 != j8) {
                                                        num2 = (Integer) notificationsController.pushDialogsOverrideMention.get(j9);
                                                        LongSparseArray longSparseArray3 = notificationsController.pushDialogsOverrideMention;
                                                        if (num2 == null) {
                                                            IntValue = z4;
                                                        } else {
                                                            IntValue = num2.intValue() + 1;
                                                        }
                                                        longSparseArray3.put(j9, Integer.valueOf((int) IntValue));
                                                    }
                                                } else {
                                                    notificationsController = this;
                                                    j8 = j7;
                                                    messageObject3 = messageObject4;
                                                    iAddToPopupMessages = iAddToPopupMessages;
                                                }
                                                if (messageObject3.isReactionPush) {
                                                    SparseBooleanArray sparseBooleanArray2 = new SparseBooleanArray();
                                                    sparseBooleanArray2.put(id, z4);
                                                    notificationsController.getMessagesController().checkUnreadReactions(j8, topicId, sparseBooleanArray2);
                                                }
                                                z14 = z14;
                                                z13 = true;
                                            } else {
                                                message = messageObject4.messageOwner;
                                                if (!message.mentioned) {
                                                    if (z5) {
                                                    }
                                                    j6 = dialogId;
                                                    fromChatId = messageObject4.getFromChatId();
                                                } else {
                                                    fromChatId = dialogId;
                                                    j6 = fromChatId;
                                                }
                                                if (notificationsController.isPersonalMessage(messageObject4)) {
                                                    notificationsController.personalCount++;
                                                }
                                                DialogObject.isChatDialog(fromChatId);
                                                iIndexOfKey = longSparseArray.indexOfKey(fromChatId);
                                                if (iIndexOfKey < 0) {
                                                    notifyOverride = notificationsController.getNotifyOverride(notificationsSettings, fromChatId, topicId);
                                                    notificationsSettings = notificationsSettings;
                                                    j7 = fromChatId;
                                                    topicId = topicId;
                                                    if (notifyOverride == -1) {
                                                        zIsGlobalNotificationsEnabled2 = isGlobalNotificationsEnabled(j7, Boolean.valueOf(z8), messageObject4.isReactionPush, messageObject4.isStoryReactionPush);
                                                        if (BuildVars.LOGS_ENABLED) {
                                                            FileLog.d("NotificationsController: process new messages, isGlobalNotificationsEnabled(" + j7 + ", " + z8 + ", " + messageObject4.isReactionPush + ", " + messageObject4.isStoryReactionPush + ") = " + zIsGlobalNotificationsEnabled2);
                                                        }
                                                        zBooleanValue = zIsGlobalNotificationsEnabled2;
                                                    } else if (notifyOverride != 2) {
                                                        zBooleanValue = z4;
                                                    } else {
                                                        zBooleanValue = false;
                                                    }
                                                    longSparseArray.put(j7, Boolean.valueOf(zBooleanValue));
                                                } else {
                                                    notifyOverride = notificationsController.getNotifyOverride(notificationsSettings, fromChatId, topicId);
                                                    notificationsSettings = notificationsSettings;
                                                    j7 = fromChatId;
                                                    topicId = topicId;
                                                    if (notifyOverride == -1) {
                                                        zIsGlobalNotificationsEnabled2 = isGlobalNotificationsEnabled(j7, Boolean.valueOf(z8), messageObject4.isReactionPush, messageObject4.isStoryReactionPush);
                                                        if (BuildVars.LOGS_ENABLED) {
                                                            FileLog.d("NotificationsController: process new messages, isGlobalNotificationsEnabled(" + j7 + ", " + z8 + ", " + messageObject4.isReactionPush + ", " + messageObject4.isStoryReactionPush + ") = " + zIsGlobalNotificationsEnabled2);
                                                        }
                                                        zBooleanValue = zIsGlobalNotificationsEnabled2;
                                                    } else if (notifyOverride != 2) {
                                                        zBooleanValue = z4;
                                                    } else {
                                                        zBooleanValue = false;
                                                    }
                                                    longSparseArray.put(j7, Boolean.valueOf(zBooleanValue));
                                                }
                                                if (BuildVars.LOGS_ENABLED) {
                                                    FileLog.d("NotificationsController: process new messages, value is " + zBooleanValue + " (" + j7 + ", " + z8 + ", " + messageObject4.isReactionPush + ", " + messageObject4.isStoryReactionPush + ")");
                                                }
                                                if (zBooleanValue) {
                                                    notificationsController = this;
                                                    j8 = j7;
                                                    if (!z) {
                                                        iAddToPopupMessages = notificationsController.addToPopupMessages(arrayList2, messageObject3, j8, z8, notificationsSettings);
                                                    }
                                                    if (!z15) {
                                                        messageObject3 = messageObject4;
                                                        iAddToPopupMessages = iAddToPopupMessages;
                                                        messageObject3 = messageObject4;
                                                        z15 = messageObject3.messageOwner.from_scheduled;
                                                    }
                                                    messageObject3 = messageObject4;
                                                    iAddToPopupMessages = iAddToPopupMessages;
                                                    messageObject3 = messageObject4;
                                                    notificationsController.delayedPushMessages.add(messageObject3);
                                                    notificationsController.appendMessage(messageObject3);
                                                    if (id != 0) {
                                                        if (sparseArray == null) {
                                                            sparseArray2 = new SparseArray();
                                                            notificationsController.pushMessagesDict.put(dialogId2, sparseArray2);
                                                        } else {
                                                            sparseArray2 = sparseArray;
                                                        }
                                                        sparseArray2.put(id, messageObject3);
                                                    } else if (j5 != 0) {
                                                        notificationsController.fcmRandomMessagesDict.put(j5, messageObject3);
                                                    }
                                                    j9 = j6;
                                                    if (j9 != j8) {
                                                        num2 = (Integer) notificationsController.pushDialogsOverrideMention.get(j9);
                                                        LongSparseArray longSparseArray4 = notificationsController.pushDialogsOverrideMention;
                                                        if (num2 == null) {
                                                            IntValue = z4;
                                                        } else {
                                                            IntValue = num2.intValue() + 1;
                                                        }
                                                        longSparseArray4.put(j9, Integer.valueOf((int) IntValue));
                                                    }
                                                } else {
                                                    notificationsController = this;
                                                    j8 = j7;
                                                    messageObject3 = messageObject4;
                                                    iAddToPopupMessages = iAddToPopupMessages;
                                                }
                                                if (messageObject3.isReactionPush) {
                                                    SparseBooleanArray sparseBooleanArray3 = new SparseBooleanArray();
                                                    sparseBooleanArray3.put(id, z4);
                                                    notificationsController.getMessagesController().checkUnreadReactions(j8, topicId, sparseBooleanArray3);
                                                }
                                                z14 = z14;
                                                z13 = true;
                                            }
                                        }
                                    }
                                    notificationsSettings = sharedPreferences2;
                                    z13 = z13;
                                }
                            }
                        }
                        z5 = z12;
                        i = i3;
                        z13 = z13;
                        z13 = z13;
                    }
                }
                if (BuildVars.LOGS_ENABLED) {
                    FileLog.d("skipped message because 1");
                }
                z5 = z12;
                i = i3;
                z13 = z13;
                z13 = z13;
            }
            i3 = i + 1;
            z12 = z5;
            notificationsSettings = notificationsSettings;
            z11 = true;
        }
        SharedPreferences sharedPreferences3 = notificationsSettings;
        boolean z19 = z13;
        final int i4 = iAddToPopupMessages;
        boolean z20 = z14;
        if (z19) {
            notificationsController.notifyCheck = z2;
        }
        if (!arrayList2.isEmpty() && !AndroidUtilities.needShowPasscode() && !SharedConfig.isWaitingForPasscodeEnter) {
            AndroidUtilities.runOnUIThread(new Runnable() { // from class: org.telegram.messenger.NotificationsController$$ExternalSyntheticLambda34
                @Override // java.lang.Runnable
                public final void run() {
                    this.f$0.lambda$processNewMessages$24(arrayList2, i4);
                }
            });
        }
        if (z || z15) {
            if (z20) {
                if (BuildVars.LOGS_ENABLED) {
                    FileLog.d("NotificationsController processNewMessages: edited branch, showOrUpdateNotification " + notificationsController.notifyCheck);
                }
                notificationsController.delayedPushMessages.clear();
                notificationsController.showOrUpdateNotification(notificationsController.notifyCheck);
            } else if (z19) {
                if (BuildVars.LOGS_ENABLED) {
                    FileLog.d("NotificationsController processNewMessages: added branch");
                }
                MessageObject messageObject5 = (MessageObject) arrayList.get(0);
                long dialogId4 = messageObject5.getDialogId();
                long topicId2 = MessageObject.getTopicId(notificationsController.currentAccount, messageObject5.messageOwner, notificationsController.getMessagesController().isForum(dialogId4));
                Boolean boolValueOf = messageObject5.isFcmMessage() ? Boolean.valueOf(messageObject5.localChannel) : null;
                int i5 = notificationsController.total_unread_count;
                int notifyOverride2 = notificationsController.getNotifyOverride(sharedPreferences3, dialogId4, topicId2);
                if (notifyOverride2 == -1) {
                    notificationsController = this;
                    j = dialogId4;
                    zIsGlobalNotificationsEnabled = notificationsController.isGlobalNotificationsEnabled(dialogId4, boolValueOf, messageObject5.isReactionPush, messageObject5.isStoryReactionPush);
                } else {
                    notificationsController = this;
                    j = dialogId4;
                    zIsGlobalNotificationsEnabled = notifyOverride2 != 2;
                }
                Integer num3 = (Integer) notificationsController.pushDialogs.get(j);
                if (num3 != null) {
                    z3 = true;
                    iIntValue = num3.intValue() + 1;
                } else {
                    z3 = true;
                    iIntValue = 1;
                }
                if (notificationsController.notifyCheck && !zIsGlobalNotificationsEnabled && (num = (Integer) notificationsController.pushDialogsOverrideMention.get(j)) != null && num.intValue() != 0) {
                    iIntValue = num.intValue();
                    zIsGlobalNotificationsEnabled = z3;
                }
                if (zIsGlobalNotificationsEnabled && !messageObject5.isStoryPush) {
                    if (notificationsController.getMessagesController().isForum(j)) {
                        ?? r3 = notificationsController.total_unread_count - ((num3 == null || num3.intValue() <= 0) ? 0 : z3);
                        notificationsController.total_unread_count = r3;
                        notificationsController.total_unread_count = r3 + (iIntValue > 0 ? z3 : 0);
                    } else {
                        if (num3 != null) {
                            notificationsController.total_unread_count -= num3.intValue();
                        }
                        notificationsController.total_unread_count += iIntValue;
                    }
                    notificationsController.pushDialogs.put(j, Integer.valueOf(iIntValue));
                }
                if (i5 != notificationsController.total_unread_count || z16) {
                    notificationsController.delayedPushMessages.clear();
                    if (BuildVars.LOGS_ENABLED) {
                        FileLog.d("NotificationsController processNewMessages: added branch: " + notificationsController.notifyCheck);
                    }
                    notificationsController.showOrUpdateNotification(notificationsController.notifyCheck);
                    final int size = notificationsController.pushDialogs.size();
                    AndroidUtilities.runOnUIThread(new Runnable() { // from class: org.telegram.messenger.NotificationsController$$ExternalSyntheticLambda35
                        @Override // java.lang.Runnable
                        public final void run() {
                            this.f$0.lambda$processNewMessages$25(size);
                        }
                    });
                }
                notificationsController.notifyCheck = false;
                if (notificationsController.showBadgeNumber) {
                    notificationsController.setBadge(notificationsController.getTotalAllUnreadCount());
                }
            }
        }
        if (z16) {
            notificationsController.updateStoryPushesRunnable();
        }
        if (countDownLatch != null) {
            countDownLatch.countDown();
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$processNewMessages$23(int i) {
        LongSparseArray longSparseArray = new LongSparseArray();
        longSparseArray.put(0L, Lists.newArrayList(Integer.valueOf(i)));
        removeDeletedMessagesFromNotifications(longSparseArray, false);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$processNewMessages$24(ArrayList arrayList, int i) {
        this.popupMessages.addAll(0, arrayList);
        if (ApplicationLoader.mainInterfacePaused || !ApplicationLoader.isScreenOn) {
            if (i == 3 || ((i == 1 && ApplicationLoader.isScreenOn) || (i == 2 && !ApplicationLoader.isScreenOn))) {
                Intent intent = new Intent(ApplicationLoader.applicationContext, (Class<?>) PopupNotificationActivity.class);
                intent.setFlags(268763140);
                try {
                    ApplicationLoader.applicationContext.startActivity(intent);
                } catch (Throwable unused) {
                }
            }
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$processNewMessages$25(int i) {
        NotificationCenter.getGlobalInstance().lambda$postNotificationNameOnUIThread$1(NotificationCenter.notificationsCountUpdated, Integer.valueOf(this.currentAccount));
        getNotificationCenter().lambda$postNotificationNameOnUIThread$1(NotificationCenter.dialogsUnreadCounterChanged, Integer.valueOf(i));
    }

    private void appendMessage(MessageObject messageObject) {
        for (int i = 0; i < this.pushMessages.size(); i++) {
            if (this.pushMessages.get(i).getId() == messageObject.getId() && this.pushMessages.get(i).getDialogId() == messageObject.getDialogId() && this.pushMessages.get(i).isStoryPush == messageObject.isStoryPush) {
                return;
            }
        }
        this.pushMessages.add(0, messageObject);
    }

    public int getTotalUnreadCount() {
        return this.total_unread_count;
    }

    public void processDialogsUpdateRead(final LongSparseIntArray longSparseIntArray) {
        final ArrayList arrayList = new ArrayList();
        notificationsQueue.postRunnable(new Runnable() { // from class: org.telegram.messenger.NotificationsController$$ExternalSyntheticLambda27
            @Override // java.lang.Runnable
            public final void run() {
                this.f$0.lambda$processDialogsUpdateRead$29(longSparseIntArray, arrayList);
            }
        });
    }

    /* JADX INFO: Access modifiers changed from: private */
    /* JADX WARN: Code duplicated, block: B:106:0x0156 A[SYNTHETIC] */
    /* JADX WARN: Code duplicated, block: B:16:0x0050 A[PHI: r4
  0x0050: PHI (r4v3 int) = (r4v2 int), (r4v27 int) binds: [B:6:0x002e, B:14:0x004a] A[DONT_GENERATE, DONT_INLINE]] */
    /* JADX WARN: Code duplicated, block: B:25:0x0068  */
    /* JADX WARN: Code duplicated, block: B:45:0x00a6  */
    /* JADX WARN: Code duplicated, block: B:47:0x00ae  */
    /* JADX WARN: Code duplicated, block: B:48:0x00b0  */
    /* JADX WARN: Code duplicated, block: B:50:0x00b5  */
    /* JADX WARN: Code duplicated, block: B:52:0x00c0  */
    /* JADX WARN: Code duplicated, block: B:55:0x00d3  */
    /* JADX WARN: Code duplicated, block: B:74:0x0132  */
    /* JADX WARN: Code duplicated, block: B:75:0x0134  */
    /* JADX WARN: Code duplicated, block: B:77:0x013e  */
    /* JADX WARN: Code duplicated, block: B:80:0x0143  */
    /* JADX WARN: Code duplicated, block: B:82:0x0148  */
    public /* synthetic */ void lambda$processDialogsUpdateRead$29(LongSparseIntArray longSparseIntArray, final ArrayList arrayList) {
        int iIntValue;
        boolean z;
        boolean zIsGlobalNotificationsEnabled;
        int i;
        int i2;
        MessageObject messageObject;
        Integer num;
        int i3 = this.total_unread_count;
        SharedPreferences notificationsSettings = getAccountInstance().getNotificationsSettings();
        int i4 = 0;
        while (true) {
            if (i4 >= longSparseIntArray.size()) {
                break;
            }
            long jKeyAt = longSparseIntArray.keyAt(i4);
            Integer num2 = (Integer) this.pushDialogs.get(jKeyAt);
            int i5 = longSparseIntArray.get(jKeyAt);
            if (DialogObject.isChatDialog(jKeyAt)) {
                TLRPC.Chat chat = getMessagesController().getChat(Long.valueOf(-jKeyAt));
                if (chat == null || chat.min || ChatObject.isNotInChat(chat)) {
                    i5 = 0;
                }
                if (chat != null) {
                    z = chat.forum;
                    iIntValue = i5;
                } else {
                    iIntValue = i5;
                    z = false;
                }
            } else {
                iIntValue = i5;
                z = false;
            }
            if (z) {
                zIsGlobalNotificationsEnabled = true;
            } else {
                int notifyOverride = getNotifyOverride(notificationsSettings, jKeyAt, 0L);
                if (notifyOverride == -1) {
                    zIsGlobalNotificationsEnabled = isGlobalNotificationsEnabled(jKeyAt, false, false);
                } else if (notifyOverride != 2) {
                    zIsGlobalNotificationsEnabled = true;
                } else {
                    zIsGlobalNotificationsEnabled = false;
                }
            }
            if (this.notifyCheck && !zIsGlobalNotificationsEnabled && (num = (Integer) this.pushDialogsOverrideMention.get(jKeyAt)) != null && num.intValue() != 0) {
                iIntValue = num.intValue();
                zIsGlobalNotificationsEnabled = true;
            }
            if (iIntValue == 0) {
                this.smartNotificationsDialogs.remove(jKeyAt);
            }
            if (iIntValue >= 0) {
                if ((!zIsGlobalNotificationsEnabled || iIntValue == 0) && num2 != null) {
                    if (getMessagesController().isForum(jKeyAt)) {
                        int i6 = this.total_unread_count;
                        if (num2.intValue() > 0) {
                            i = 1;
                        } else {
                            i = 0;
                        }
                        this.total_unread_count = i6 - i;
                    } else {
                        this.total_unread_count -= num2.intValue();
                    }
                }
                if (iIntValue == 0) {
                    this.pushDialogs.remove(jKeyAt);
                    this.pushDialogsOverrideMention.remove(jKeyAt);
                    i2 = 0;
                    while (i2 < this.pushMessages.size()) {
                        messageObject = this.pushMessages.get(i2);
                        if (messageObject.messageOwner.from_scheduled && messageObject.getDialogId() == jKeyAt && !messageObject.isStoryReactionPush) {
                            if (isPersonalMessage(messageObject)) {
                                this.personalCount--;
                            }
                            this.pushMessages.remove(i2);
                            i2--;
                            this.delayedPushMessages.remove(messageObject);
                            long j = messageObject.messageOwner.peer_id.channel_id;
                            long j2 = j != 0 ? -j : 0L;
                            SparseArray sparseArray = (SparseArray) this.pushMessagesDict.get(j2);
                            if (sparseArray != null) {
                                sparseArray.remove(messageObject.getId());
                                if (sparseArray.size() == 0) {
                                    this.pushMessagesDict.remove(j2);
                                }
                            }
                            arrayList.add(messageObject);
                        }
                        i2++;
                    }
                } else if (!zIsGlobalNotificationsEnabled) {
                    if (getMessagesController().isForum(jKeyAt)) {
                        this.total_unread_count += iIntValue <= 0 ? 0 : 1;
                    } else {
                        this.total_unread_count += iIntValue;
                    }
                    this.pushDialogs.put(jKeyAt, Integer.valueOf(iIntValue));
                }
            } else if (num2 != null) {
                iIntValue += num2.intValue();
                if (!zIsGlobalNotificationsEnabled) {
                    if (getMessagesController().isForum(jKeyAt)) {
                        int i7 = this.total_unread_count;
                        if (num2.intValue() > 0) {
                            i = 1;
                        } else {
                            i = 0;
                        }
                        this.total_unread_count = i7 - i;
                    } else {
                        this.total_unread_count -= num2.intValue();
                    }
                } else if (getMessagesController().isForum(jKeyAt)) {
                    int i8 = this.total_unread_count;
                    if (num2.intValue() > 0) {
                        i = 1;
                    } else {
                        i = 0;
                    }
                    this.total_unread_count = i8 - i;
                } else {
                    this.total_unread_count -= num2.intValue();
                }
                if (iIntValue == 0) {
                    this.pushDialogs.remove(jKeyAt);
                    this.pushDialogsOverrideMention.remove(jKeyAt);
                    i2 = 0;
                    while (i2 < this.pushMessages.size()) {
                        messageObject = this.pushMessages.get(i2);
                        if (messageObject.messageOwner.from_scheduled) {
                        }
                        i2++;
                    }
                } else if (!zIsGlobalNotificationsEnabled) {
                    if (getMessagesController().isForum(jKeyAt)) {
                        this.total_unread_count += iIntValue <= 0 ? 0 : 1;
                    } else {
                        this.total_unread_count += iIntValue;
                    }
                    this.pushDialogs.put(jKeyAt, Integer.valueOf(iIntValue));
                }
            }
            i4++;
        }
        if (!arrayList.isEmpty()) {
            AndroidUtilities.runOnUIThread(new Runnable() { // from class: org.telegram.messenger.NotificationsController$$ExternalSyntheticLambda5
                @Override // java.lang.Runnable
                public final void run() {
                    this.f$0.lambda$processDialogsUpdateRead$27(arrayList);
                }
            });
        }
        if (i3 != this.total_unread_count) {
            if (!this.notifyCheck) {
                this.delayedPushMessages.clear();
                showOrUpdateNotification(this.notifyCheck);
            } else {
                scheduleNotificationDelay(this.lastOnlineFromOtherDevice > getConnectionsManager().getCurrentTime());
            }
            final int size = this.pushDialogs.size();
            AndroidUtilities.runOnUIThread(new Runnable() { // from class: org.telegram.messenger.NotificationsController$$ExternalSyntheticLambda6
                @Override // java.lang.Runnable
                public final void run() {
                    this.f$0.lambda$processDialogsUpdateRead$28(size);
                }
            });
        }
        this.notifyCheck = false;
        if (this.showBadgeNumber) {
            setBadge(getTotalAllUnreadCount());
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$processDialogsUpdateRead$27(ArrayList arrayList) {
        int size = arrayList.size();
        for (int i = 0; i < size; i++) {
            this.popupMessages.remove(arrayList.get(i));
        }
        NotificationCenter.getGlobalInstance().lambda$postNotificationNameOnUIThread$1(NotificationCenter.pushMessagesUpdated, new Object[0]);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$processDialogsUpdateRead$28(int i) {
        NotificationCenter.getGlobalInstance().lambda$postNotificationNameOnUIThread$1(NotificationCenter.notificationsCountUpdated, Integer.valueOf(this.currentAccount));
        getNotificationCenter().lambda$postNotificationNameOnUIThread$1(NotificationCenter.dialogsUnreadCounterChanged, Integer.valueOf(i));
    }

    public void processLoadedUnreadMessages(final LongSparseArray longSparseArray, final ArrayList<TLRPC.Message> arrayList, final ArrayList<MessageObject> arrayList2, ArrayList<TLRPC.User> arrayList3, ArrayList<TLRPC.Chat> arrayList4, ArrayList<TLRPC.EncryptedChat> arrayList5, final Collection<StoryNotification> collection) {
        getMessagesController().putUsers(arrayList3, true);
        getMessagesController().putChats(arrayList4, true);
        getMessagesController().putEncryptedChats(arrayList5, true);
        notificationsQueue.postRunnable(new Runnable() { // from class: org.telegram.messenger.NotificationsController$$ExternalSyntheticLambda24
            @Override // java.lang.Runnable
            public final void run() {
                this.f$0.lambda$processLoadedUnreadMessages$32(arrayList, longSparseArray, arrayList2, collection);
            }
        });
    }

    /* JADX INFO: Access modifiers changed from: private */
    /* JADX WARN: Code duplicated, block: B:9:0x0044  */
    public /* synthetic */ void lambda$processLoadedUnreadMessages$32(ArrayList arrayList, LongSparseArray longSparseArray, ArrayList arrayList2, Collection collection) {
        MessageObject messageObject;
        boolean zIsGlobalNotificationsEnabled;
        long dialogId;
        SharedPreferences sharedPreferences;
        int i;
        boolean zIsGlobalNotificationsEnabled2;
        TLRPC.MessageFwdHeader messageFwdHeader;
        long j;
        long j2;
        boolean zIsGlobalNotificationsEnabled3;
        TLRPC.Message message;
        boolean zBooleanValue;
        final NotificationsController notificationsController = this;
        notificationsController.pushDialogs.clear();
        notificationsController.pushMessages.clear();
        notificationsController.pushMessagesDict.clear();
        notificationsController.storyPushMessages.clear();
        notificationsController.storyPushMessagesDict.clear();
        boolean z = false;
        notificationsController.total_unread_count = 0;
        notificationsController.personalCount = 0;
        SharedPreferences notificationsSettings = notificationsController.getAccountInstance().getNotificationsSettings();
        LongSparseArray longSparseArray2 = new LongSparseArray();
        long j3 = 0;
        if (arrayList != null) {
            int i2 = 0;
            while (i2 < arrayList.size()) {
                TLRPC.Message message2 = (TLRPC.Message) arrayList.get(i2);
                if (message2 != null && ((messageFwdHeader = message2.fwd_from) == null || !messageFwdHeader.imported)) {
                    TLRPC.MessageAction messageAction = message2.action;
                    if ((messageAction instanceof TLRPC.TL_messageActionSetMessagesTTL) || (message2.silent && ((messageAction instanceof TLRPC.TL_messageActionContactSignUp) || (messageAction instanceof TLRPC.TL_messageActionUserJoined)))) {
                        j2 = j3;
                    } else {
                        long j4 = message2.peer_id.channel_id;
                        if (j4 != j3) {
                            j = -j4;
                            j2 = j3;
                        } else {
                            j = j3;
                            j2 = j;
                        }
                        SparseArray sparseArray = (SparseArray) notificationsController.pushMessagesDict.get(j);
                        if (sparseArray == null || sparseArray.indexOfKey(message2.id) < 0) {
                            MessageObject messageObject2 = new MessageObject(notificationsController.currentAccount, message2, z, z);
                            if (notificationsController.isPersonalMessage(messageObject2)) {
                                notificationsController.personalCount++;
                            }
                            long dialogId2 = messageObject2.getDialogId();
                            long topicId = MessageObject.getTopicId(notificationsController.currentAccount, messageObject2.messageOwner, getMessagesController().isForum(messageObject2));
                            long fromChatId = messageObject2.messageOwner.mentioned ? messageObject2.getFromChatId() : dialogId2;
                            int iIndexOfKey = longSparseArray2.indexOfKey(fromChatId);
                            if (iIndexOfKey >= 0 && topicId == j2) {
                                zBooleanValue = ((Boolean) longSparseArray2.valueAt(iIndexOfKey)).booleanValue();
                                message = message2;
                                notificationsController = this;
                            } else {
                                notificationsController = this;
                                int notifyOverride = notificationsController.getNotifyOverride(notificationsSettings, fromChatId, topicId);
                                if (notifyOverride == -1) {
                                    zIsGlobalNotificationsEnabled3 = notificationsController.isGlobalNotificationsEnabled(fromChatId, messageObject2.isReactionPush, messageObject2.isStoryReactionPush);
                                } else {
                                    zIsGlobalNotificationsEnabled3 = notifyOverride != 2;
                                }
                                message = message2;
                                longSparseArray2.put(fromChatId, Boolean.valueOf(zIsGlobalNotificationsEnabled3));
                                zBooleanValue = zIsGlobalNotificationsEnabled3;
                            }
                            notificationsSettings = notificationsSettings;
                            if (zBooleanValue) {
                                long j5 = fromChatId;
                                if (j5 != notificationsController.openedDialogId || !ApplicationLoader.isScreenOn) {
                                    if (sparseArray == null) {
                                        sparseArray = new SparseArray();
                                        notificationsController.pushMessagesDict.put(j, sparseArray);
                                    }
                                    sparseArray.put(message.id, messageObject2);
                                    notificationsController.appendMessage(messageObject2);
                                    if (dialogId2 != j5) {
                                        Integer num = (Integer) notificationsController.pushDialogsOverrideMention.get(dialogId2);
                                        notificationsController.pushDialogsOverrideMention.put(dialogId2, Integer.valueOf(num == null ? 1 : num.intValue() + 1));
                                    }
                                }
                            }
                        }
                    }
                } else {
                    j2 = j3;
                }
                i2++;
                notificationsSettings = notificationsSettings;
                j3 = j2;
                z = false;
            }
        }
        SharedPreferences sharedPreferences2 = notificationsSettings;
        long j6 = j3;
        int i3 = 0;
        while (i3 < longSparseArray.size()) {
            long jKeyAt = longSparseArray.keyAt(i3);
            int iIndexOfKey2 = longSparseArray2.indexOfKey(jKeyAt);
            if (iIndexOfKey2 >= 0) {
                zIsGlobalNotificationsEnabled2 = ((Boolean) longSparseArray2.valueAt(iIndexOfKey2)).booleanValue();
                sharedPreferences = sharedPreferences2;
                i = 0;
            } else {
                sharedPreferences = sharedPreferences2;
                int notifyOverride2 = notificationsController.getNotifyOverride(sharedPreferences, jKeyAt, 0L);
                if (notifyOverride2 == -1) {
                    i = 0;
                    zIsGlobalNotificationsEnabled2 = notificationsController.isGlobalNotificationsEnabled(jKeyAt, false, false);
                } else {
                    i = 0;
                    zIsGlobalNotificationsEnabled2 = notifyOverride2 != 2;
                }
                longSparseArray2.put(jKeyAt, Boolean.valueOf(zIsGlobalNotificationsEnabled2));
            }
            if (zIsGlobalNotificationsEnabled2) {
                Integer num2 = (Integer) longSparseArray.valueAt(i3);
                int iIntValue = num2.intValue();
                notificationsController.pushDialogs.put(jKeyAt, num2);
                if (notificationsController.getMessagesController().isForum(jKeyAt)) {
                    notificationsController.total_unread_count += iIntValue > 0 ? 1 : i;
                } else {
                    notificationsController.total_unread_count += iIntValue;
                }
            }
            i3++;
            sharedPreferences2 = sharedPreferences;
        }
        SharedPreferences sharedPreferences3 = sharedPreferences2;
        if (arrayList2 != null) {
            for (int i4 = 0; i4 < arrayList2.size(); i4++) {
                MessageObject messageObject3 = (MessageObject) arrayList2.get(i4);
                int id = messageObject3.getId();
                if (notificationsController.pushMessagesDict.indexOfKey(id) < 0) {
                    if (notificationsController.isPersonalMessage(messageObject3)) {
                        notificationsController.personalCount++;
                    }
                    long dialogId3 = messageObject3.getDialogId();
                    long topicId2 = MessageObject.getTopicId(notificationsController.currentAccount, messageObject3.messageOwner, notificationsController.getMessagesController().isForum(messageObject3));
                    TLRPC.Message message3 = messageObject3.messageOwner;
                    long j7 = message3.random_id;
                    long fromChatId2 = message3.mentioned ? messageObject3.getFromChatId() : dialogId3;
                    int iIndexOfKey3 = longSparseArray2.indexOfKey(fromChatId2);
                    if (iIndexOfKey3 >= 0 && topicId2 == j6) {
                        zIsGlobalNotificationsEnabled = ((Boolean) longSparseArray2.valueAt(iIndexOfKey3)).booleanValue();
                        messageObject = messageObject3;
                    } else {
                        int notifyOverride3 = notificationsController.getNotifyOverride(sharedPreferences3, fromChatId2, topicId2);
                        if (notifyOverride3 == -1) {
                            messageObject = messageObject3;
                            zIsGlobalNotificationsEnabled = notificationsController.isGlobalNotificationsEnabled(fromChatId2, messageObject.isReactionPush, messageObject.isStoryReactionPush);
                        } else {
                            messageObject = messageObject3;
                            zIsGlobalNotificationsEnabled = notifyOverride3 != 2;
                        }
                        longSparseArray2.put(fromChatId2, Boolean.valueOf(zIsGlobalNotificationsEnabled));
                    }
                    if (zIsGlobalNotificationsEnabled && (fromChatId2 != notificationsController.openedDialogId || !ApplicationLoader.isScreenOn)) {
                        if (id != 0) {
                            if (messageObject.isStoryReactionPush) {
                                dialogId = messageObject.getDialogId();
                            } else {
                                long j8 = messageObject.messageOwner.peer_id.channel_id;
                                dialogId = j8 != j6 ? -j8 : j6;
                            }
                            SparseArray sparseArray2 = (SparseArray) notificationsController.pushMessagesDict.get(dialogId);
                            if (sparseArray2 == null) {
                                sparseArray2 = new SparseArray();
                                notificationsController.pushMessagesDict.put(dialogId, sparseArray2);
                            }
                            sparseArray2.put(id, messageObject);
                        } else if (j7 != j6) {
                            notificationsController.fcmRandomMessagesDict.put(j7, messageObject);
                        }
                        notificationsController.appendMessage(messageObject);
                        if (dialogId3 != fromChatId2) {
                            Integer num3 = (Integer) notificationsController.pushDialogsOverrideMention.get(dialogId3);
                            notificationsController.pushDialogsOverrideMention.put(dialogId3, Integer.valueOf(num3 == null ? 1 : num3.intValue() + 1));
                        }
                        Integer num4 = (Integer) notificationsController.pushDialogs.get(fromChatId2);
                        int iIntValue2 = num4 != null ? num4.intValue() + 1 : 1;
                        if (notificationsController.getMessagesController().isForum(fromChatId2)) {
                            if (num4 != null) {
                                notificationsController.total_unread_count -= num4.intValue() > 0 ? 1 : 0;
                            }
                            notificationsController.total_unread_count += iIntValue2 > 0 ? 1 : 0;
                        } else {
                            if (num4 != null) {
                                notificationsController.total_unread_count -= num4.intValue();
                            }
                            notificationsController.total_unread_count += iIntValue2;
                        }
                        notificationsController.pushDialogs.put(fromChatId2, Integer.valueOf(iIntValue2));
                    }
                }
            }
        }
        if (collection != null) {
            Iterator it = collection.iterator();
            while (it.hasNext()) {
                StoryNotification storyNotification = (StoryNotification) it.next();
                long j9 = storyNotification.dialogId;
                StoryNotification storyNotification2 = (StoryNotification) notificationsController.storyPushMessagesDict.get(j9);
                if (storyNotification2 != null) {
                    storyNotification2.dateByIds.putAll(storyNotification.dateByIds);
                } else {
                    notificationsController.storyPushMessages.add(storyNotification);
                    notificationsController.storyPushMessagesDict.put(j9, storyNotification);
                }
            }
            Collections.sort(notificationsController.storyPushMessages, Comparator.CC.comparingLong(new ToLongFunction() { // from class: org.telegram.messenger.NotificationsController$$ExternalSyntheticLambda47
                @Override // java.util.function.ToLongFunction
                public final long applyAsLong(Object obj) {
                    return ((NotificationsController.StoryNotification) obj).date;
                }
            }));
        }
        final int size = notificationsController.pushDialogs.size();
        AndroidUtilities.runOnUIThread(new Runnable() { // from class: org.telegram.messenger.NotificationsController$$ExternalSyntheticLambda48
            @Override // java.lang.Runnable
            public final void run() {
                this.f$0.lambda$processLoadedUnreadMessages$31(size);
            }
        });
        notificationsController.showOrUpdateNotification(SystemClock.elapsedRealtime() / 1000 < 60);
        if (notificationsController.showBadgeNumber) {
            notificationsController.setBadge(notificationsController.getTotalAllUnreadCount());
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$processLoadedUnreadMessages$31(int i) {
        if (this.total_unread_count == 0) {
            this.popupMessages.clear();
            NotificationCenter.getGlobalInstance().lambda$postNotificationNameOnUIThread$1(NotificationCenter.pushMessagesUpdated, new Object[0]);
        }
        NotificationCenter.getGlobalInstance().lambda$postNotificationNameOnUIThread$1(NotificationCenter.notificationsCountUpdated, Integer.valueOf(this.currentAccount));
        getNotificationCenter().lambda$postNotificationNameOnUIThread$1(NotificationCenter.dialogsUnreadCounterChanged, Integer.valueOf(i));
    }

    private int getTotalAllUnreadCount() {
        int size;
        int dialogUnreadCount = 0;
        for (int i = 0; i < 16; i++) {
            if (UserConfig.getInstance(i).isClientActivated() && (SharedConfig.showNotificationsForAllAccounts || UserConfig.selectedAccount == i)) {
                NotificationsController notificationsController = getInstance(i);
                if (notificationsController.showBadgeNumber) {
                    if (notificationsController.showBadgeMessages) {
                        if (notificationsController.showBadgeMuted) {
                            try {
                                ArrayList arrayList = new ArrayList(MessagesController.getInstance(i).allDialogs);
                                int size2 = arrayList.size();
                                for (int i2 = 0; i2 < size2; i2++) {
                                    TLRPC.Dialog dialog = (TLRPC.Dialog) arrayList.get(i2);
                                    if ((dialog == null || !DialogObject.isChatDialog(dialog.id) || !ChatObject.isNotInChat(getMessagesController().getChat(Long.valueOf(-dialog.id)))) && dialog != null) {
                                        dialogUnreadCount += MessagesController.getInstance(i).getDialogUnreadCount(dialog);
                                    }
                                }
                            } catch (Exception e) {
                                FileLog.e(e);
                            }
                        } else {
                            size = notificationsController.total_unread_count;
                            dialogUnreadCount += size;
                        }
                    } else if (notificationsController.showBadgeMuted) {
                        try {
                            int size3 = MessagesController.getInstance(i).allDialogs.size();
                            for (int i3 = 0; i3 < size3; i3++) {
                                TLRPC.Dialog dialog2 = MessagesController.getInstance(i).allDialogs.get(i3);
                                if ((!DialogObject.isChatDialog(dialog2.id) || !ChatObject.isNotInChat(getMessagesController().getChat(Long.valueOf(-dialog2.id)))) && MessagesController.getInstance(i).getDialogUnreadCount(dialog2) != 0) {
                                    dialogUnreadCount++;
                                }
                            }
                        } catch (Exception e2) {
                            FileLog.e(e2);
                        }
                    } else {
                        size = notificationsController.pushDialogs.size();
                        dialogUnreadCount += size;
                    }
                }
            }
        }
        return dialogUnreadCount;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$updateBadge$33() {
        setBadge(getTotalAllUnreadCount());
    }

    public void updateBadge() {
        notificationsQueue.postRunnable(new Runnable() { // from class: org.telegram.messenger.NotificationsController$$ExternalSyntheticLambda38
            @Override // java.lang.Runnable
            public final void run() {
                this.f$0.lambda$updateBadge$33();
            }
        });
    }

    private void setBadge(int i) {
        if (this.lastBadgeCount == i) {
            return;
        }
        FileLog.d("setBadge " + i);
        this.lastBadgeCount = i;
        NotificationBadge.applyCount(i);
    }

    /* JADX WARN: Code duplicated, block: B:158:0x0243  */
    /* JADX WARN: Code duplicated, block: B:160:0x024b  */
    /* JADX WARN: Code duplicated, block: B:162:0x0252  */
    /* JADX WARN: Code duplicated, block: B:164:0x0256  */
    /* JADX WARN: Code duplicated, block: B:166:0x025d  */
    /* JADX WARN: Code duplicated, block: B:168:0x0261  */
    /* JADX WARN: Code duplicated, block: B:170:0x0268  */
    /* JADX WARN: Code duplicated, block: B:733:0x1048  */
    /* JADX WARN: Code duplicated, block: B:735:0x104e  */
    /* JADX WARN: Code duplicated, block: B:737:0x1058  */
    /* JADX WARN: Code duplicated, block: B:739:0x105d  */
    /* JADX WARN: Code duplicated, block: B:741:0x1064  */
    /* JADX WARN: Code duplicated, block: B:753:0x109e  */
    /* JADX WARN: Code duplicated, block: B:754:0x10a0  */
    /* JADX WARN: Code duplicated, block: B:779:0x10e6  */
    /* JADX WARN: Code duplicated, block: B:781:0x10ed  */
    /* JADX WARN: Code duplicated, block: B:783:0x10f3  */
    /* JADX WARN: Code duplicated, block: B:785:0x10fa  */
    /* JADX WARN: Code duplicated, block: B:787:0x1102  */
    /* JADX WARN: Code duplicated, block: B:789:0x110a  */
    /* JADX WARN: Code duplicated, block: B:791:0x111e  */
    /* JADX WARN: Code duplicated, block: B:793:0x1126  */
    /* JADX WARN: Code duplicated, block: B:795:0x112d  */
    /* JADX WARN: Code duplicated, block: B:797:0x1134  */
    /* JADX WARN: Code duplicated, block: B:799:0x113a  */
    /* JADX WARN: Code duplicated, block: B:801:0x1144  */
    /* JADX WARN: Code duplicated, block: B:803:0x1158  */
    /* JADX WARN: Code duplicated, block: B:805:0x1160  */
    /* JADX WARN: Code duplicated, block: B:807:0x1167  */
    /* JADX WARN: Code duplicated, block: B:809:0x116e  */
    /* JADX WARN: Code duplicated, block: B:811:0x1174  */
    /* JADX WARN: Code duplicated, block: B:813:0x117b  */
    /* JADX WARN: Code duplicated, block: B:815:0x1181  */
    /* JADX WARN: Code duplicated, block: B:817:0x1188  */
    /* JADX WARN: Code duplicated, block: B:819:0x118e  */
    /* JADX WARN: Code duplicated, block: B:821:0x1195  */
    /* JADX WARN: Code duplicated, block: B:823:0x119b  */
    /* JADX WARN: Code duplicated, block: B:825:0x11a2  */
    /* JADX WARN: Code duplicated, block: B:827:0x11aa  */
    /* JADX WARN: Code duplicated, block: B:829:0x11b1  */
    /* JADX WARN: Code duplicated, block: B:831:0x11b5  */
    /* JADX WARN: Code duplicated, block: B:833:0x11bd  */
    /* JADX WARN: Code duplicated, block: B:835:0x11c4  */
    /* JADX WARN: Code duplicated, block: B:837:0x11cb  */
    /* JADX WARN: Code duplicated, block: B:839:0x11cf  */
    /* JADX WARN: Code duplicated, block: B:841:0x11d6  */
    /* JADX WARN: Code duplicated, block: B:843:0x11da  */
    /* JADX WARN: Code duplicated, block: B:845:0x11e1  */
    /* JADX WARN: Code duplicated, block: B:847:0x11e5  */
    /* JADX WARN: Code duplicated, block: B:849:0x11ec  */
    /* JADX WARN: Code restructure failed: missing block: B:155:0x022c, code lost:
    
        if (r12.getBoolean("EnablePreviewChannel", r6) != false) goto L156;
     */
    /* JADX WARN: Instruction removed from duplicated block: B:789:0x110a, please report this as an issue */
    /* JADX WARN: Instruction removed from duplicated block: B:801:0x1144, please report this as an issue */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
    */
    private String getShortStringForMessage(MessageObject messageObject, String[] strArr, boolean[] zArr) {
        long j;
        String title;
        TLRPC.Chat chat;
        boolean z;
        TLRPC.Message message;
        boolean z2;
        TLRPC.Message message2;
        TLRPC.MessageMedia messageMedia;
        char c;
        String pluralString;
        TLRPC.MessageAction messageAction;
        String forcedFirstName;
        char c2;
        char c3;
        char c4;
        TLRPC.MessageFwdHeader messageFwdHeader;
        TLRPC.Peer peer;
        TLRPC.Chat chat2;
        String str;
        TLRPC.MessageFwdHeader messageFwdHeader2;
        TLRPC.Peer peer2;
        if (AndroidUtilities.needShowPasscode() || SharedConfig.isWaitingForPasscodeEnter) {
            return LocaleController.getString(R.string.NotificationHiddenMessage);
        }
        TLRPC.Message message3 = messageObject.messageOwner;
        long j2 = message3.dialog_id;
        TLRPC.Peer peer3 = message3.peer_id;
        long j3 = peer3.chat_id;
        if (j3 == 0) {
            j3 = peer3.channel_id;
        }
        long fromChatId = peer3.user_id;
        if (zArr != null) {
            zArr[0] = true;
        }
        SharedPreferences notificationsSettings = getAccountInstance().getNotificationsSettings();
        boolean z3 = notificationsSettings.getBoolean(NotificationsSettingsFacade.PROPERTY_CONTENT_PREVIEW + j2, true);
        if (messageObject.isFcmMessage()) {
            if (j3 == 0 && fromChatId != 0) {
                if (Build.VERSION.SDK_INT > 27) {
                    strArr[0] = messageObject.localName;
                }
                if (!z3 || !notificationsSettings.getBoolean("EnablePreviewAll", true)) {
                    if (zArr != null) {
                        zArr[0] = false;
                    }
                    return LocaleController.getString(R.string.Message);
                }
            } else if (j3 != 0) {
                if (messageObject.messageOwner.peer_id.channel_id == 0 || messageObject.isSupergroup()) {
                    strArr[0] = messageObject.localUserName;
                } else if (Build.VERSION.SDK_INT > 27) {
                    strArr[0] = messageObject.localName;
                }
                if (!z3 || ((!messageObject.localChannel && !notificationsSettings.getBoolean("EnablePreviewGroup", true)) || (messageObject.localChannel && !notificationsSettings.getBoolean("EnablePreviewChannel", true)))) {
                    if (zArr != null) {
                        zArr[0] = false;
                    }
                    return (messageObject.messageOwner.peer_id.channel_id == 0 || messageObject.isSupergroup()) ? LocaleController.formatString(R.string.NotificationMessageGroupNoText, messageObject.localUserName, messageObject.localName) : LocaleController.formatString(R.string.ChannelMessageNoText, messageObject.localName);
                }
            }
            return replaceSpoilers(messageObject);
        }
        long clientUserId = getUserConfig().getClientUserId();
        if (fromChatId == 0) {
            fromChatId = messageObject.getFromChatId();
            if (fromChatId == 0) {
                fromChatId = -j3;
            }
        } else if (fromChatId == clientUserId) {
            fromChatId = messageObject.getFromChatId();
        }
        if (j2 == 0) {
            if (j3 != 0) {
                j2 = -j3;
            } else if (fromChatId != 0) {
                j2 = fromChatId;
            }
        }
        if (UserObject.isReplyUser(j2) && (messageFwdHeader2 = messageObject.messageOwner.fwd_from) != null && (peer2 = messageFwdHeader2.from_id) != null) {
            fromChatId = MessageObject.getPeerId(peer2);
        }
        if (fromChatId > 0) {
            TLRPC.User user = getMessagesController().getUser(Long.valueOf(fromChatId));
            if (user != null) {
                String userName = UserObject.getUserName(user);
                if (j3 != 0) {
                    strArr[0] = userName;
                    str = userName;
                } else {
                    str = userName;
                    if (Build.VERSION.SDK_INT > 27) {
                        strArr[0] = str;
                    } else {
                        strArr[0] = null;
                    }
                }
                title = str;
            } else {
                title = null;
            }
            j = j2;
        } else {
            j = j2;
            TLRPC.Chat chat3 = getMessagesController().getChat(Long.valueOf(-fromChatId));
            if (chat3 != null) {
                title = getTitle(chat3);
                strArr[0] = title;
            } else {
                title = null;
            }
        }
        if (title != null && fromChatId > 0 && UserObject.isReplyUser(j) && (messageFwdHeader = messageObject.messageOwner.fwd_from) != null && (peer = messageFwdHeader.saved_from_peer) != null) {
            long peerId = MessageObject.getPeerId(peer);
            if (DialogObject.isChatDialog(peerId) && (chat2 = getMessagesController().getChat(Long.valueOf(-peerId))) != null) {
                title = title + " @ " + getTitle(chat2);
                if (strArr[0] != null) {
                    strArr[0] = title;
                }
            }
        }
        if (title == null) {
            return null;
        }
        if (j3 != 0) {
            chat = getMessagesController().getChat(Long.valueOf(j3));
            if (chat == null) {
                return null;
            }
            if (ChatObject.isChannel(chat) && !chat.megagroup && Build.VERSION.SDK_INT <= 27) {
                strArr[0] = null;
            }
        } else {
            chat = null;
        }
        if (DialogObject.isEncryptedDialog(j)) {
            strArr[0] = null;
            return LocaleController.getString(R.string.NotificationHiddenMessage);
        }
        boolean z4 = ChatObject.isChannel(chat) && !chat.megagroup;
        if (z3) {
            if (j3 == 0 && fromChatId != 0) {
                z = true;
                if (!notificationsSettings.getBoolean("EnablePreviewAll", true)) {
                }
                message = messageObject.messageOwner;
                z2 = message instanceof TLRPC.TL_messageService;
                String title2 = _UrlKt.FRAGMENT_ENCODE_SET;
                if (z2) {
                    strArr[0] = null;
                    messageAction = message.action;
                    if (messageAction instanceof TLRPC.TL_messageActionSetSameChatWallPaper) {
                        return LocaleController.getString(R.string.WallpaperSameNotification);
                    }
                    if (messageAction instanceof TLRPC.TL_messageActionSetChatWallPaper) {
                        return LocaleController.getString(R.string.WallpaperNotification);
                    }
                    if (messageAction instanceof TLRPC.TL_messageActionGeoProximityReached) {
                        return messageObject.messageText.toString();
                    }
                    if (!(messageAction instanceof TLRPC.TL_messageActionUserJoined) || (messageAction instanceof TLRPC.TL_messageActionContactSignUp)) {
                        return LocaleController.formatString(R.string.NotificationContactJoined, title);
                    }
                    if (messageAction instanceof TLRPC.TL_messageActionUserUpdatedPhoto) {
                        return LocaleController.formatString(R.string.NotificationContactNewPhoto, title);
                    }
                    if (messageAction instanceof TLRPC.TL_messageActionLoginUnknownLocation) {
                        String string = LocaleController.formatString(R.string.formatDateAtTime, LocaleController.getInstance().getFormatterYear().format(((long) messageObject.messageOwner.date) * 1000), LocaleController.getInstance().getFormatterDay().format(((long) messageObject.messageOwner.date) * 1000));
                        int i = R.string.NotificationUnrecognizedDevice;
                        String str2 = getUserConfig().getCurrentUser().first_name;
                        TLRPC.MessageAction messageAction2 = messageObject.messageOwner.action;
                        return LocaleController.formatString(i, str2, string, messageAction2.title, messageAction2.address);
                    }
                    if (TlUtils.isInstance(messageAction, TLRPC.TL_messageActionGameScore.class, TLRPC.TL_messageActionPaymentSent.class, TLRPC.TL_messageActionPaymentSentMe.class, TLRPC.TL_messageActionStarGift.class, TLRPC.TL_messageActionGiftPremium.class, TLRPC.TL_messageActionStarGiftUnique.class, TLRPC.TL_messageActionPaidMessagesPrice.class, TLRPC.TL_messageActionPaidMessagesRefunded.class, TLRPC.TL_messageActionGiftTon.class)) {
                        return messageObject.messageText.toString();
                    }
                    TLRPC.Message message4 = messageObject.messageOwner;
                    TLRPC.MessageAction messageAction3 = message4.action;
                    if (messageAction3 instanceof TLRPC.TL_messageActionPhoneCall) {
                        if (messageAction3.video) {
                            return LocaleController.getString(R.string.CallMessageVideoIncomingMissed);
                        }
                        return LocaleController.getString(R.string.CallMessageIncomingMissed);
                    }
                    if (messageAction3 instanceof TLRPC.TL_messageActionConferenceCall) {
                        if (messageAction3.video) {
                            return LocaleController.getString(R.string.CallMessageVideoIncomingConferenceMissed);
                        }
                        return LocaleController.getString(R.string.CallMessageIncomingConferenceMissed);
                    }
                    if (messageAction3 instanceof TLRPC.TL_messageActionChatAddUser) {
                        long jLongValue = messageAction3.user_id;
                        if (jLongValue == 0 && messageAction3.users.size() == 1) {
                            jLongValue = ((Long) messageObject.messageOwner.action.users.get(0)).longValue();
                        }
                        if (jLongValue != 0) {
                            if (messageObject.messageOwner.peer_id.channel_id != 0 && !chat.megagroup) {
                                return LocaleController.formatString(R.string.ChannelAddedByNotification, title, getTitle(chat));
                            }
                            if (jLongValue == clientUserId) {
                                return LocaleController.formatString(R.string.NotificationInvitedToGroup, title, getTitle(chat));
                            }
                            TLRPC.User user2 = getMessagesController().getUser(Long.valueOf(jLongValue));
                            if (user2 == null) {
                                return null;
                            }
                            if (fromChatId == user2.id) {
                                return chat.megagroup ? LocaleController.formatString(R.string.NotificationGroupAddSelfMega, title, getTitle(chat)) : LocaleController.formatString(R.string.NotificationGroupAddSelf, title, getTitle(chat));
                            }
                            return LocaleController.formatString(R.string.NotificationGroupAddMember, title, getTitle(chat), UserObject.getUserName(user2));
                        }
                        StringBuilder sb = new StringBuilder();
                        for (int i2 = 0; i2 < messageObject.messageOwner.action.users.size(); i2++) {
                            TLRPC.User user3 = getMessagesController().getUser((Long) messageObject.messageOwner.action.users.get(i2));
                            if (user3 != null) {
                                String userName2 = UserObject.getUserName(user3);
                                if (sb.length() != 0) {
                                    sb.append(", ");
                                }
                                sb.append(userName2);
                            }
                        }
                        return LocaleController.formatString(R.string.NotificationGroupAddMember, title, getTitle(chat), sb.toString());
                    }
                    if (messageAction3 instanceof TLRPC.TL_messageActionGroupCall) {
                        return messageAction3.duration != 0 ? LocaleController.formatString(R.string.NotificationGroupEndedCall, title, getTitle(chat)) : LocaleController.formatString(R.string.NotificationGroupCreatedCall, title, getTitle(chat));
                    }
                    if (messageAction3 instanceof TLRPC.TL_messageActionGroupCallScheduled) {
                        return messageObject.messageText.toString();
                    }
                    if (messageAction3 instanceof TLRPC.TL_messageActionInviteToGroupCall) {
                        long jLongValue2 = messageAction3.user_id;
                        if (jLongValue2 == 0 && messageAction3.users.size() == 1) {
                            jLongValue2 = ((Long) messageObject.messageOwner.action.users.get(0)).longValue();
                        }
                        if (jLongValue2 != 0) {
                            if (jLongValue2 == clientUserId) {
                                return LocaleController.formatString(R.string.NotificationGroupInvitedYouToCall, title, getTitle(chat));
                            }
                            TLRPC.User user4 = getMessagesController().getUser(Long.valueOf(jLongValue2));
                            if (user4 == null) {
                                return null;
                            }
                            return LocaleController.formatString(R.string.NotificationGroupInvitedToCall, title, getTitle(chat), UserObject.getUserName(user4));
                        }
                        StringBuilder sb2 = new StringBuilder();
                        for (int i3 = 0; i3 < messageObject.messageOwner.action.users.size(); i3++) {
                            TLRPC.User user5 = getMessagesController().getUser((Long) messageObject.messageOwner.action.users.get(i3));
                            if (user5 != null) {
                                String userName3 = UserObject.getUserName(user5);
                                if (sb2.length() != 0) {
                                    sb2.append(", ");
                                }
                                sb2.append(userName3);
                            }
                        }
                        return LocaleController.formatString(R.string.NotificationGroupInvitedToCall, title, getTitle(chat), sb2.toString());
                    }
                    if (messageAction3 instanceof TLRPC.TL_messageActionGiftCode) {
                        return LocaleController.getString(R.string.BoostingReceivedGiftNoName);
                    }
                    if (messageAction3 instanceof TLRPC.TL_messageActionChatJoinedByLink) {
                        return LocaleController.formatString(R.string.NotificationInvitedToGroupByLink, title, getTitle(chat));
                    }
                    if (messageAction3 instanceof TLRPC.TL_messageActionChatEditTitle) {
                        return LocaleController.formatString(R.string.NotificationEditedGroupName, title, messageAction3.title);
                    }
                    if ((messageAction3 instanceof TLRPC.TL_messageActionChatEditPhoto) || (messageAction3 instanceof TLRPC.TL_messageActionChatDeletePhoto)) {
                        if (message4.peer_id.channel_id == 0 || chat.megagroup) {
                            return messageObject.isVideoAvatar() ? LocaleController.formatString(R.string.NotificationEditedGroupVideo, title, getTitle(chat)) : LocaleController.formatString(R.string.NotificationEditedGroupPhoto, title, getTitle(chat));
                        }
                        return messageObject.isVideoAvatar() ? LocaleController.formatString(R.string.ChannelVideoEditNotification, getTitle(chat)) : LocaleController.formatString(R.string.ChannelPhotoEditNotification, getTitle(chat));
                    }
                    if (messageAction3 instanceof TLRPC.TL_messageActionChatDeleteUser) {
                        long j4 = messageAction3.user_id;
                        if (j4 == clientUserId) {
                            return LocaleController.formatString(R.string.NotificationGroupKickYou, title, getTitle(chat));
                        }
                        if (j4 == fromChatId) {
                            return LocaleController.formatString(R.string.NotificationGroupLeftMember, title, getTitle(chat));
                        }
                        TLRPC.User user6 = getMessagesController().getUser(Long.valueOf(messageObject.messageOwner.action.user_id));
                        if (user6 == null) {
                            return null;
                        }
                        return LocaleController.formatString(R.string.NotificationGroupKickMember, title, getTitle(chat), UserObject.getUserName(user6));
                    }
                    if (messageAction3 instanceof TLRPC.TL_messageActionChatCreate) {
                        return messageObject.messageText.toString();
                    }
                    if (messageAction3 instanceof TLRPC.TL_messageActionChannelCreate) {
                        return messageObject.messageText.toString();
                    }
                    if (messageAction3 instanceof TLRPC.TL_messageActionChatMigrateTo) {
                        return LocaleController.formatString(R.string.ActionMigrateFromGroupNotify, getTitle(chat));
                    }
                    if (messageAction3 instanceof TLRPC.TL_messageActionChannelMigrateFrom) {
                        return LocaleController.formatString(R.string.ActionMigrateFromGroupNotify, messageAction3.title);
                    }
                    if (messageAction3 instanceof TLRPC.TL_messageActionScreenshotTaken) {
                        return messageObject.messageText.toString();
                    }
                    if (messageAction3 instanceof TLRPC.TL_messageActionGiveawayLaunch) {
                        return messageObject.messageText.toString();
                    }
                    if (messageAction3 instanceof TLRPC.TL_messageActionGiveawayResults) {
                        return messageObject.messageText.toString();
                    }
                    if (messageAction3 instanceof TLRPC.TL_messageActionSuggestBirthday) {
                        return messageObject.messageText.toString();
                    }
                    if (!(messageAction3 instanceof TLRPC.TL_messageActionPinMessage)) {
                        if (messageAction3 instanceof TLRPC.TL_messageActionSetChatTheme) {
                            String themeEmoticonOrGiftTitle = TlUtils.getThemeEmoticonOrGiftTitle(((TLRPC.TL_messageActionSetChatTheme) messageAction3).theme);
                            if (TextUtils.isEmpty(themeEmoticonOrGiftTitle)) {
                                return j == clientUserId ? LocaleController.formatString(R.string.ChatThemeDisabledYou, new Object[0]) : LocaleController.formatString(R.string.ChatThemeDisabled, title, themeEmoticonOrGiftTitle);
                            }
                            return j == clientUserId ? LocaleController.formatString(R.string.ChatThemeChangedYou, themeEmoticonOrGiftTitle) : LocaleController.formatString(R.string.ChatThemeChangedTo, title, themeEmoticonOrGiftTitle);
                        }
                        if (messageAction3 instanceof TLRPC.TL_messageActionChatJoinedByRequest) {
                            return messageObject.messageText.toString();
                        }
                        if (messageAction3 instanceof TLRPC.TL_messageActionPrizeStars) {
                            TLRPC.TL_messageActionPrizeStars tL_messageActionPrizeStars = (TLRPC.TL_messageActionPrizeStars) messageAction3;
                            long peerDialogId = DialogObject.getPeerDialogId(tL_messageActionPrizeStars.boost_peer);
                            if (peerDialogId >= 0) {
                                forcedFirstName = UserObject.getForcedFirstName(getMessagesController().getUser(Long.valueOf(peerDialogId)));
                            } else {
                                TLRPC.Chat chat4 = getMessagesController().getChat(Long.valueOf(-peerDialogId));
                                if (chat4 != null) {
                                    title2 = getTitle(chat4);
                                }
                                forcedFirstName = title2;
                            }
                            return LocaleController.formatPluralStringComma("BoostingReceivedStars", (int) tL_messageActionPrizeStars.stars, forcedFirstName);
                        }
                        if (messageAction3 instanceof TLRPC.TL_messageActionPaymentRefunded) {
                            return messageObject.messageText.toString();
                        }
                        if (messageAction3 instanceof TLRPC.TL_messageActionTodoCompletions) {
                            return messageObject.messageText.toString();
                        }
                        if (messageAction3 instanceof TLRPC.TL_messageActionTodoAppendTasks) {
                            return messageObject.messageText.toString();
                        }
                        return null;
                    }
                    if (chat != null && (!ChatObject.isChannel(chat) || chat.megagroup)) {
                        MessageObject messageObject2 = messageObject.replyMessageObject;
                        if (messageObject2 == null) {
                            return LocaleController.formatString(R.string.NotificationActionPinnedNoText, title, getTitle(chat));
                        }
                        if (messageObject2.isMusic()) {
                            return LocaleController.formatString(R.string.NotificationActionPinnedMusic, title, getTitle(chat));
                        }
                        if (messageObject2.isVideo()) {
                            if (TextUtils.isEmpty(messageObject2.messageOwner.message)) {
                                return LocaleController.formatString(R.string.NotificationActionPinnedVideo, title, getTitle(chat));
                            }
                            return LocaleController.formatString(R.string.NotificationActionPinnedText, title, "📹 " + messageObject2.messageOwner.message, getTitle(chat));
                        }
                        if (messageObject2.isGif()) {
                            if (TextUtils.isEmpty(messageObject2.messageOwner.message)) {
                                return LocaleController.formatString(R.string.NotificationActionPinnedGif, title, getTitle(chat));
                            }
                            return LocaleController.formatString(R.string.NotificationActionPinnedText, title, "🎬 " + messageObject2.messageOwner.message, getTitle(chat));
                        }
                        if (messageObject2.isVoice()) {
                            return LocaleController.formatString(R.string.NotificationActionPinnedVoice, title, getTitle(chat));
                        }
                        if (messageObject2.isRoundVideo()) {
                            return LocaleController.formatString(R.string.NotificationActionPinnedRound, title, getTitle(chat));
                        }
                        if (messageObject2.isSticker() || messageObject2.isAnimatedSticker()) {
                            String stickerEmoji = messageObject2.getStickerEmoji();
                            return stickerEmoji != null ? LocaleController.formatString(R.string.NotificationActionPinnedStickerEmoji, title, getTitle(chat), stickerEmoji) : LocaleController.formatString(R.string.NotificationActionPinnedSticker, title, getTitle(chat));
                        }
                        TLRPC.Message message5 = messageObject2.messageOwner;
                        TLRPC.MessageMedia messageMedia2 = message5.media;
                        if (messageMedia2 instanceof TLRPC.TL_messageMediaDocument) {
                            if (TextUtils.isEmpty(message5.message)) {
                                return LocaleController.formatString(R.string.NotificationActionPinnedFile, title, getTitle(chat));
                            }
                            return LocaleController.formatString(R.string.NotificationActionPinnedText, title, "📎 " + messageObject2.messageOwner.message, getTitle(chat));
                        }
                        if ((messageMedia2 instanceof TLRPC.TL_messageMediaGeo) || (messageMedia2 instanceof TLRPC.TL_messageMediaVenue)) {
                            return LocaleController.formatString(R.string.NotificationActionPinnedGeo, title, getTitle(chat));
                        }
                        if (messageMedia2 instanceof TLRPC.TL_messageMediaGeoLive) {
                            return LocaleController.formatString(R.string.NotificationActionPinnedGeoLive, title, getTitle(chat));
                        }
                        if (messageMedia2 instanceof TLRPC.TL_messageMediaContact) {
                            TLRPC.TL_messageMediaContact tL_messageMediaContact = (TLRPC.TL_messageMediaContact) messageMedia2;
                            return LocaleController.formatString(R.string.NotificationActionPinnedContact2, title, getTitle(chat), ContactsController.formatName(tL_messageMediaContact.first_name, tL_messageMediaContact.last_name));
                        }
                        if (messageMedia2 instanceof TLRPC.TL_messageMediaPoll) {
                            TLRPC.TL_messageMediaPoll tL_messageMediaPoll = (TLRPC.TL_messageMediaPoll) messageMedia2;
                            return tL_messageMediaPoll.poll.quiz ? LocaleController.formatString(R.string.NotificationActionPinnedQuiz2, title, getTitle(chat), tL_messageMediaPoll.poll.question.text) : LocaleController.formatString(R.string.NotificationActionPinnedPoll2, title, getTitle(chat), tL_messageMediaPoll.poll.question.text);
                        }
                        if (messageMedia2 instanceof TLRPC.TL_messageMediaToDo) {
                            return LocaleController.formatString(R.string.NotificationActionPinnedTodo2, title, getTitle(chat), ((TLRPC.TL_messageMediaToDo) messageMedia2).todo.title.text);
                        }
                        if (messageMedia2 instanceof TLRPC.TL_messageMediaPhoto) {
                            if (TextUtils.isEmpty(message5.message)) {
                                return LocaleController.formatString(R.string.NotificationActionPinnedPhoto, title, getTitle(chat));
                            }
                            return LocaleController.formatString(R.string.NotificationActionPinnedText, title, "🖼 " + messageObject2.messageOwner.message, getTitle(chat));
                        }
                        if (messageMedia2 instanceof TLRPC.TL_messageMediaGame) {
                            return LocaleController.formatString(R.string.NotificationActionPinnedGame, title, getTitle(chat));
                        }
                        CharSequence charSequence = messageObject2.messageText;
                        if (charSequence == null || charSequence.length() <= 0) {
                            return LocaleController.formatString(R.string.NotificationActionPinnedNoText, title, getTitle(chat));
                        }
                        CharSequence string2 = messageObject2.messageText;
                        if (string2.length() > 20) {
                            StringBuilder sb3 = new StringBuilder();
                            c4 = 0;
                            sb3.append((Object) string2.subSequence(0, 20));
                            sb3.append("...");
                            string2 = sb3.toString();
                        } else {
                            c4 = 0;
                        }
                        int i4 = R.string.NotificationActionPinnedText;
                        String title3 = getTitle(chat);
                        Object[] objArr = new Object[3];
                        objArr[c4] = title;
                        objArr[1] = string2;
                        objArr[2] = title3;
                        return LocaleController.formatString(i4, objArr);
                    }
                    if (chat != null) {
                        MessageObject messageObject3 = messageObject.replyMessageObject;
                        if (messageObject3 == null) {
                            return LocaleController.formatString(R.string.NotificationActionPinnedNoTextChannel, getTitle(chat));
                        }
                        if (messageObject3.isMusic()) {
                            return LocaleController.formatString(R.string.NotificationActionPinnedMusicChannel, getTitle(chat));
                        }
                        if (messageObject3.isVideo()) {
                            if (TextUtils.isEmpty(messageObject3.messageOwner.message)) {
                                return LocaleController.formatString(R.string.NotificationActionPinnedVideoChannel, getTitle(chat));
                            }
                            return LocaleController.formatString(R.string.NotificationActionPinnedTextChannel, getTitle(chat), "📹 " + messageObject3.messageOwner.message);
                        }
                        if (messageObject3.isGif()) {
                            if (TextUtils.isEmpty(messageObject3.messageOwner.message)) {
                                return LocaleController.formatString(R.string.NotificationActionPinnedGifChannel, getTitle(chat));
                            }
                            return LocaleController.formatString(R.string.NotificationActionPinnedTextChannel, getTitle(chat), "🎬 " + messageObject3.messageOwner.message);
                        }
                        if (messageObject3.isVoice()) {
                            return LocaleController.formatString(R.string.NotificationActionPinnedVoiceChannel, getTitle(chat));
                        }
                        if (messageObject3.isRoundVideo()) {
                            return LocaleController.formatString(R.string.NotificationActionPinnedRoundChannel, getTitle(chat));
                        }
                        if (messageObject3.isSticker() || messageObject3.isAnimatedSticker()) {
                            String stickerEmoji2 = messageObject3.getStickerEmoji();
                            return stickerEmoji2 != null ? LocaleController.formatString(R.string.NotificationActionPinnedStickerEmojiChannel, getTitle(chat), stickerEmoji2) : LocaleController.formatString(R.string.NotificationActionPinnedStickerChannel, getTitle(chat));
                        }
                        TLRPC.Message message6 = messageObject3.messageOwner;
                        TLRPC.MessageMedia messageMedia3 = message6.media;
                        if (messageMedia3 instanceof TLRPC.TL_messageMediaDocument) {
                            if (TextUtils.isEmpty(message6.message)) {
                                return LocaleController.formatString(R.string.NotificationActionPinnedFileChannel, getTitle(chat));
                            }
                            return LocaleController.formatString(R.string.NotificationActionPinnedTextChannel, getTitle(chat), "📎 " + messageObject3.messageOwner.message);
                        }
                        if ((messageMedia3 instanceof TLRPC.TL_messageMediaGeo) || (messageMedia3 instanceof TLRPC.TL_messageMediaVenue)) {
                            return LocaleController.formatString(R.string.NotificationActionPinnedGeoChannel, getTitle(chat));
                        }
                        if (messageMedia3 instanceof TLRPC.TL_messageMediaGeoLive) {
                            return LocaleController.formatString(R.string.NotificationActionPinnedGeoLiveChannel, getTitle(chat));
                        }
                        if (messageMedia3 instanceof TLRPC.TL_messageMediaContact) {
                            TLRPC.TL_messageMediaContact tL_messageMediaContact2 = (TLRPC.TL_messageMediaContact) messageMedia3;
                            return LocaleController.formatString(R.string.NotificationActionPinnedContactChannel2, getTitle(chat), ContactsController.formatName(tL_messageMediaContact2.first_name, tL_messageMediaContact2.last_name));
                        }
                        if (messageMedia3 instanceof TLRPC.TL_messageMediaPoll) {
                            TLRPC.TL_messageMediaPoll tL_messageMediaPoll2 = (TLRPC.TL_messageMediaPoll) messageMedia3;
                            return tL_messageMediaPoll2.poll.quiz ? LocaleController.formatString(R.string.NotificationActionPinnedQuizChannel2, getTitle(chat), tL_messageMediaPoll2.poll.question.text) : LocaleController.formatString(R.string.NotificationActionPinnedPollChannel2, getTitle(chat), tL_messageMediaPoll2.poll.question.text);
                        }
                        if (messageMedia3 instanceof TLRPC.TL_messageMediaToDo) {
                            return LocaleController.formatString(R.string.NotificationActionPinnedTodoChannel2, getTitle(chat), ((TLRPC.TL_messageMediaToDo) messageMedia3).todo.title.text);
                        }
                        if (messageMedia3 instanceof TLRPC.TL_messageMediaPhoto) {
                            if (TextUtils.isEmpty(message6.message)) {
                                return LocaleController.formatString(R.string.NotificationActionPinnedPhotoChannel, getTitle(chat));
                            }
                            return LocaleController.formatString(R.string.NotificationActionPinnedTextChannel, getTitle(chat), "🖼 " + messageObject3.messageOwner.message);
                        }
                        if (messageMedia3 instanceof TLRPC.TL_messageMediaGame) {
                            return LocaleController.formatString(R.string.NotificationActionPinnedGameChannel, getTitle(chat));
                        }
                        CharSequence charSequence2 = messageObject3.messageText;
                        if (charSequence2 == null || charSequence2.length() <= 0) {
                            return LocaleController.formatString(R.string.NotificationActionPinnedNoTextChannel, getTitle(chat));
                        }
                        CharSequence string3 = messageObject3.messageText;
                        if (string3.length() > 20) {
                            StringBuilder sb4 = new StringBuilder();
                            c3 = 0;
                            sb4.append((Object) string3.subSequence(0, 20));
                            sb4.append("...");
                            string3 = sb4.toString();
                        } else {
                            c3 = 0;
                        }
                        int i5 = R.string.NotificationActionPinnedTextChannel;
                        String title4 = getTitle(chat);
                        Object[] objArr2 = new Object[2];
                        objArr2[c3] = title4;
                        objArr2[1] = string3;
                        return LocaleController.formatString(i5, objArr2);
                    }
                    MessageObject messageObject4 = messageObject.replyMessageObject;
                    if (messageObject4 == null) {
                        return LocaleController.formatString(R.string.NotificationActionPinnedNoTextUser, title);
                    }
                    if (messageObject4.isMusic()) {
                        return LocaleController.formatString(R.string.NotificationActionPinnedMusicUser, title);
                    }
                    if (messageObject4.isVideo()) {
                        if (TextUtils.isEmpty(messageObject4.messageOwner.message)) {
                            return LocaleController.formatString(R.string.NotificationActionPinnedVideoUser, title);
                        }
                        return LocaleController.formatString(R.string.NotificationActionPinnedTextUser, title, "📹 " + messageObject4.messageOwner.message);
                    }
                    if (messageObject4.isGif()) {
                        if (TextUtils.isEmpty(messageObject4.messageOwner.message)) {
                            return LocaleController.formatString(R.string.NotificationActionPinnedGifUser, title);
                        }
                        return LocaleController.formatString(R.string.NotificationActionPinnedTextUser, title, "🎬 " + messageObject4.messageOwner.message);
                    }
                    if (messageObject4.isVoice()) {
                        return LocaleController.formatString(R.string.NotificationActionPinnedVoiceUser, title);
                    }
                    if (messageObject4.isRoundVideo()) {
                        return LocaleController.formatString(R.string.NotificationActionPinnedRoundUser, title);
                    }
                    if (messageObject4.isSticker() || messageObject4.isAnimatedSticker()) {
                        String stickerEmoji3 = messageObject4.getStickerEmoji();
                        return stickerEmoji3 != null ? LocaleController.formatString(R.string.NotificationActionPinnedStickerEmojiUser, title, stickerEmoji3) : LocaleController.formatString(R.string.NotificationActionPinnedStickerUser, title);
                    }
                    TLRPC.Message message7 = messageObject4.messageOwner;
                    TLRPC.MessageMedia messageMedia4 = message7.media;
                    if (messageMedia4 instanceof TLRPC.TL_messageMediaDocument) {
                        if (TextUtils.isEmpty(message7.message)) {
                            return LocaleController.formatString(R.string.NotificationActionPinnedFileUser, title);
                        }
                        return LocaleController.formatString(R.string.NotificationActionPinnedTextUser, title, "📎 " + messageObject4.messageOwner.message);
                    }
                    if ((messageMedia4 instanceof TLRPC.TL_messageMediaGeo) || (messageMedia4 instanceof TLRPC.TL_messageMediaVenue)) {
                        return LocaleController.formatString(R.string.NotificationActionPinnedGeoUser, title);
                    }
                    if (messageMedia4 instanceof TLRPC.TL_messageMediaGeoLive) {
                        return LocaleController.formatString(R.string.NotificationActionPinnedGeoLiveUser, title);
                    }
                    if (messageMedia4 instanceof TLRPC.TL_messageMediaContact) {
                        TLRPC.TL_messageMediaContact tL_messageMediaContact3 = (TLRPC.TL_messageMediaContact) messageMedia4;
                        return LocaleController.formatString(R.string.NotificationActionPinnedContactUser, title, ContactsController.formatName(tL_messageMediaContact3.first_name, tL_messageMediaContact3.last_name));
                    }
                    if (messageMedia4 instanceof TLRPC.TL_messageMediaPoll) {
                        TLRPC.Poll poll = ((TLRPC.TL_messageMediaPoll) messageMedia4).poll;
                        return poll.quiz ? LocaleController.formatString(R.string.NotificationActionPinnedQuizUser, title, poll.question.text) : LocaleController.formatString(R.string.NotificationActionPinnedPollUser, title, poll.question.text);
                    }
                    if (messageMedia4 instanceof TLRPC.TL_messageMediaToDo) {
                        return LocaleController.formatString(R.string.NotificationActionPinnedTodoUser, title, ((TLRPC.TL_messageMediaToDo) messageMedia4).todo.title.text);
                    }
                    if (messageMedia4 instanceof TLRPC.TL_messageMediaPhoto) {
                        if (TextUtils.isEmpty(message7.message)) {
                            return LocaleController.formatString(R.string.NotificationActionPinnedPhotoUser, title);
                        }
                        return LocaleController.formatString(R.string.NotificationActionPinnedTextUser, title, "🖼 " + messageObject4.messageOwner.message);
                    }
                    if (messageMedia4 instanceof TLRPC.TL_messageMediaGame) {
                        return LocaleController.formatString(R.string.NotificationActionPinnedGameUser, title);
                    }
                    CharSequence charSequence3 = messageObject4.messageText;
                    if (charSequence3 == null || charSequence3.length() <= 0) {
                        return LocaleController.formatString(R.string.NotificationActionPinnedNoTextUser, title);
                    }
                    CharSequence string4 = messageObject4.messageText;
                    if (string4.length() > 20) {
                        StringBuilder sb5 = new StringBuilder();
                        c2 = 0;
                        sb5.append((Object) string4.subSequence(0, 20));
                        sb5.append("...");
                        string4 = sb5.toString();
                    } else {
                        c2 = 0;
                    }
                    int i6 = R.string.NotificationActionPinnedTextUser;
                    Object[] objArr3 = new Object[2];
                    objArr3[c2] = title;
                    objArr3[1] = string4;
                    return LocaleController.formatString(i6, objArr3);
                }
                if (messageObject.isMediaEmpty()) {
                    if (!TextUtils.isEmpty(messageObject.messageOwner.message)) {
                        return replaceSpoilers(messageObject);
                    }
                    return LocaleController.getString(R.string.Message);
                }
                if (messageObject.type != 29 && (MessageObject.getMedia(messageObject) instanceof TLRPC.TL_messageMediaPaidMedia)) {
                    TLRPC.TL_messageMediaPaidMedia tL_messageMediaPaidMedia = (TLRPC.TL_messageMediaPaidMedia) MessageObject.getMedia(messageObject);
                    int size = tL_messageMediaPaidMedia.extended_media.size();
                    boolean z5 = false;
                    for (int i7 = 0; i7 < size; i7++) {
                        TLRPC.MessageExtendedMedia messageExtendedMedia = tL_messageMediaPaidMedia.extended_media.get(i7);
                        if (messageExtendedMedia instanceof TLRPC.TL_messageExtendedMedia) {
                            TLRPC.MessageMedia messageMedia5 = ((TLRPC.TL_messageExtendedMedia) messageExtendedMedia).media;
                            if ((messageMedia5 instanceof TLRPC.TL_messageMediaDocument) && MessageObject.isVideoDocument(messageMedia5.document)) {
                                z5 = true;
                            } else {
                                z5 = false;
                            }
                        } else if (messageExtendedMedia instanceof TLRPC.TL_messageExtendedMediaPreview) {
                            if ((((TLRPC.TL_messageExtendedMediaPreview) messageExtendedMedia).flags & 4) != 0) {
                                z5 = true;
                            } else {
                                z5 = false;
                            }
                        }
                        if (z5) {
                            break;
                        }
                    }
                    int i8 = R.string.AttachPaidMedia;
                    if (size == 1) {
                        pluralString = LocaleController.getString(z5 ? R.string.AttachVideo : R.string.AttachPhoto);
                        c = 0;
                    } else {
                        c = 0;
                        pluralString = LocaleController.formatPluralString(z5 ? "Media" : "Photos", size, new Object[0]);
                    }
                    Object[] objArr4 = new Object[1];
                    objArr4[c] = pluralString;
                    return LocaleController.formatString(i8, objArr4);
                }
                if (messageObject.isVoiceOnce()) {
                    return LocaleController.getString(R.string.AttachOnceAudio);
                }
                if (messageObject.isRoundOnce()) {
                    return LocaleController.getString(R.string.AttachOnceRound);
                }
                message2 = messageObject.messageOwner;
                if (message2.media instanceof TLRPC.TL_messageMediaPhoto) {
                    if (!TextUtils.isEmpty(message2.message)) {
                        return "🖼 " + replaceSpoilers(messageObject);
                    }
                    if (messageObject.messageOwner.media.ttl_seconds != 0) {
                        return LocaleController.getString(R.string.AttachDestructingPhoto);
                    }
                    return LocaleController.getString(R.string.AttachPhoto);
                }
                if (messageObject.isVideo()) {
                    if (!TextUtils.isEmpty(messageObject.messageOwner.message)) {
                        return "📹 " + replaceSpoilers(messageObject);
                    }
                    if (messageObject.messageOwner.media.ttl_seconds != 0) {
                        return LocaleController.getString(R.string.AttachDestructingVideo);
                    }
                    return LocaleController.getString(R.string.AttachVideo);
                }
                if (messageObject.isGame()) {
                    return LocaleController.getString(R.string.AttachGame);
                }
                if (messageObject.isVoice()) {
                    return LocaleController.getString(R.string.AttachAudio);
                }
                if (messageObject.isRoundVideo()) {
                    return LocaleController.getString(R.string.AttachRound);
                }
                if (messageObject.isMusic()) {
                    return LocaleController.getString(R.string.AttachMusic);
                }
                messageMedia = messageObject.messageOwner.media;
                if (messageMedia instanceof TLRPC.TL_messageMediaContact) {
                    return LocaleController.getString(R.string.AttachContact);
                }
                if (messageMedia instanceof TLRPC.TL_messageMediaPoll) {
                    if (((TLRPC.TL_messageMediaPoll) messageMedia).poll.quiz) {
                        return LocaleController.getString(R.string.QuizPoll);
                    }
                    return LocaleController.getString(R.string.Poll);
                }
                if (messageMedia instanceof TLRPC.TL_messageMediaToDo) {
                    return LocaleController.getString(R.string.Todo);
                }
                if (messageMedia instanceof TLRPC.TL_messageMediaGiveaway) {
                    return LocaleController.getString(R.string.BoostingGiveaway);
                }
                if (messageMedia instanceof TLRPC.TL_messageMediaGiveawayResults) {
                    return LocaleController.getString(R.string.BoostingGiveawayResults);
                }
                if (!(messageMedia instanceof TLRPC.TL_messageMediaGeo) || (messageMedia instanceof TLRPC.TL_messageMediaVenue)) {
                    return LocaleController.getString(R.string.AttachLocation);
                }
                if (messageMedia instanceof TLRPC.TL_messageMediaGeoLive) {
                    return LocaleController.getString(R.string.AttachLiveLocation);
                }
                if (messageMedia instanceof TLRPC.TL_messageMediaDocument) {
                    if (messageObject.isSticker() || messageObject.isAnimatedSticker()) {
                        String stickerEmoji4 = messageObject.getStickerEmoji();
                        if (stickerEmoji4 != null) {
                            return stickerEmoji4 + " " + LocaleController.getString(R.string.AttachSticker);
                        }
                        return LocaleController.getString(R.string.AttachSticker);
                    }
                    if (messageObject.isGif()) {
                        if (!TextUtils.isEmpty(messageObject.messageOwner.message)) {
                            return "🎬 " + replaceSpoilers(messageObject);
                        }
                        return LocaleController.getString(R.string.AttachGif);
                    }
                    if (!TextUtils.isEmpty(messageObject.messageOwner.message)) {
                        return "📎 " + replaceSpoilers(messageObject);
                    }
                    return LocaleController.getString(R.string.AttachDocument);
                }
                if (messageMedia instanceof TLRPC.TL_messageMediaStory) {
                    if (((TLRPC.TL_messageMediaStory) messageMedia).via_mention) {
                        int i9 = R.string.StoryNotificationMention;
                        String str3 = strArr[0];
                        if (str3 != null) {
                            title2 = str3;
                        }
                        return LocaleController.formatString(i9, title2);
                    }
                    return LocaleController.getString(R.string.Story);
                }
                if (!TextUtils.isEmpty(messageObject.messageText)) {
                    return replaceSpoilers(messageObject);
                }
                return LocaleController.getString(R.string.Message);
            }
            z = true;
            if (j3 != 0) {
                if (!z4) {
                    if (!notificationsSettings.getBoolean("EnablePreviewGroup", z)) {
                    }
                    message = messageObject.messageOwner;
                    z2 = message instanceof TLRPC.TL_messageService;
                    String title5 = _UrlKt.FRAGMENT_ENCODE_SET;
                    if (z2) {
                        strArr[0] = null;
                        messageAction = message.action;
                        if (messageAction instanceof TLRPC.TL_messageActionSetSameChatWallPaper) {
                            return LocaleController.getString(R.string.WallpaperSameNotification);
                        }
                        if (messageAction instanceof TLRPC.TL_messageActionSetChatWallPaper) {
                            return LocaleController.getString(R.string.WallpaperNotification);
                        }
                        if (messageAction instanceof TLRPC.TL_messageActionGeoProximityReached) {
                            return messageObject.messageText.toString();
                        }
                        if (messageAction instanceof TLRPC.TL_messageActionUserJoined) {
                        }
                        return LocaleController.formatString(R.string.NotificationContactJoined, title);
                    }
                    if (messageObject.isMediaEmpty()) {
                        if (!TextUtils.isEmpty(messageObject.messageOwner.message)) {
                            return replaceSpoilers(messageObject);
                        }
                        return LocaleController.getString(R.string.Message);
                    }
                    if (messageObject.type != 29) {
                    }
                    if (messageObject.isVoiceOnce()) {
                        return LocaleController.getString(R.string.AttachOnceAudio);
                    }
                    if (messageObject.isRoundOnce()) {
                        return LocaleController.getString(R.string.AttachOnceRound);
                    }
                    message2 = messageObject.messageOwner;
                    if (message2.media instanceof TLRPC.TL_messageMediaPhoto) {
                        if (!TextUtils.isEmpty(message2.message)) {
                            return "🖼 " + replaceSpoilers(messageObject);
                        }
                        if (messageObject.messageOwner.media.ttl_seconds != 0) {
                            return LocaleController.getString(R.string.AttachDestructingPhoto);
                        }
                        return LocaleController.getString(R.string.AttachPhoto);
                    }
                    if (messageObject.isVideo()) {
                        if (!TextUtils.isEmpty(messageObject.messageOwner.message)) {
                            return "📹 " + replaceSpoilers(messageObject);
                        }
                        if (messageObject.messageOwner.media.ttl_seconds != 0) {
                            return LocaleController.getString(R.string.AttachDestructingVideo);
                        }
                        return LocaleController.getString(R.string.AttachVideo);
                    }
                    if (messageObject.isGame()) {
                        return LocaleController.getString(R.string.AttachGame);
                    }
                    if (messageObject.isVoice()) {
                        return LocaleController.getString(R.string.AttachAudio);
                    }
                    if (messageObject.isRoundVideo()) {
                        return LocaleController.getString(R.string.AttachRound);
                    }
                    if (messageObject.isMusic()) {
                        return LocaleController.getString(R.string.AttachMusic);
                    }
                    messageMedia = messageObject.messageOwner.media;
                    if (messageMedia instanceof TLRPC.TL_messageMediaContact) {
                        return LocaleController.getString(R.string.AttachContact);
                    }
                    if (messageMedia instanceof TLRPC.TL_messageMediaPoll) {
                        if (((TLRPC.TL_messageMediaPoll) messageMedia).poll.quiz) {
                            return LocaleController.getString(R.string.QuizPoll);
                        }
                        return LocaleController.getString(R.string.Poll);
                    }
                    if (messageMedia instanceof TLRPC.TL_messageMediaToDo) {
                        return LocaleController.getString(R.string.Todo);
                    }
                    if (messageMedia instanceof TLRPC.TL_messageMediaGiveaway) {
                        return LocaleController.getString(R.string.BoostingGiveaway);
                    }
                    if (messageMedia instanceof TLRPC.TL_messageMediaGiveawayResults) {
                        return LocaleController.getString(R.string.BoostingGiveawayResults);
                    }
                    if (!(messageMedia instanceof TLRPC.TL_messageMediaGeo)) {
                    }
                    return LocaleController.getString(R.string.AttachLocation);
                }
                if (z4) {
                }
            }
        }
        if (zArr != null) {
            zArr[0] = false;
        }
        return LocaleController.getString(R.string.Message);
    }

    private String replaceSpoilers(MessageObject messageObject) {
        TLRPC.Message message;
        String str;
        if (messageObject == null || (message = messageObject.messageOwner) == null || (str = message.message) == null || message.entities == null) {
            return null;
        }
        StringBuilder sb = new StringBuilder(str);
        if (messageObject.didSpoilLoginCode()) {
            return sb.toString();
        }
        for (int i = 0; i < messageObject.messageOwner.entities.size(); i++) {
            if (messageObject.messageOwner.entities.get(i) instanceof TLRPC.TL_messageEntitySpoiler) {
                TLRPC.TL_messageEntitySpoiler tL_messageEntitySpoiler = (TLRPC.TL_messageEntitySpoiler) messageObject.messageOwner.entities.get(i);
                for (int i2 = 0; i2 < tL_messageEntitySpoiler.length; i2++) {
                    int i3 = tL_messageEntitySpoiler.offset + i2;
                    char[] cArr = this.spoilerChars;
                    sb.setCharAt(i3, cArr[i2 % cArr.length]);
                }
            }
        }
        return sb.toString();
    }

    private String getStringForMessage(MessageObject messageObject, boolean z, boolean[] zArr, boolean[] zArr2) {
        long j;
        String string;
        TLRPC.Chat chat;
        char c;
        char c2;
        char c3;
        boolean z2;
        String string2;
        if (AndroidUtilities.needShowPasscode() || SharedConfig.isWaitingForPasscodeEnter) {
            return LocaleController.getString(R.string.YouHaveNewMessage);
        }
        if (messageObject.isStoryPush || messageObject.isStoryMentionPush) {
            return "!" + messageObject.messageOwner.message;
        }
        TLRPC.Message message = messageObject.messageOwner;
        long j2 = message.dialog_id;
        TLRPC.Peer peer = message.peer_id;
        long j3 = peer.chat_id;
        if (j3 == 0) {
            j3 = peer.channel_id;
        }
        long fromChatId = peer.user_id;
        if (zArr2 != null) {
            zArr2[0] = true;
        }
        if (messageObject.getDialogId() == UserObject.VERIFY && messageObject.getForwardedFromId() != null) {
            fromChatId = messageObject.getForwardedFromId().longValue();
            j3 = fromChatId < 0 ? -fromChatId : 0L;
        }
        SharedPreferences notificationsSettings = getAccountInstance().getNotificationsSettings();
        boolean z3 = notificationsSettings.getBoolean(NotificationsSettingsFacade.PROPERTY_CONTENT_PREVIEW + j2, true);
        if (messageObject.isFcmMessage()) {
            if (j3 == 0 && fromChatId != 0) {
                if (!z3 || !notificationsSettings.getBoolean("EnablePreviewAll", true)) {
                    if (zArr2 != null) {
                        zArr2[0] = false;
                    }
                    return LocaleController.formatString(R.string.NotificationMessageNoText, messageObject.localName);
                }
            } else if (j3 != 0 && (!z3 || ((!messageObject.localChannel && !notificationsSettings.getBoolean("EnablePreviewGroup", true)) || (messageObject.localChannel && !notificationsSettings.getBoolean("EnablePreviewChannel", true))))) {
                if (zArr2 != null) {
                    zArr2[0] = false;
                }
                return (messageObject.messageOwner.peer_id.channel_id == 0 || messageObject.isSupergroup()) ? LocaleController.formatString(R.string.NotificationMessageGroupNoText, messageObject.localUserName, messageObject.localName) : LocaleController.formatString(R.string.ChannelMessageNoText, messageObject.localName);
            }
            zArr[0] = true;
            return (String) messageObject.messageText;
        }
        long clientUserId = getUserConfig().getClientUserId();
        if (fromChatId == 0) {
            fromChatId = messageObject.getFromChatId();
            if (fromChatId == 0) {
                fromChatId = -j3;
            }
        } else if (fromChatId == clientUserId) {
            fromChatId = messageObject.getFromChatId();
        }
        if (j2 == 0) {
            if (j3 != 0) {
                j2 = -j3;
            } else if (fromChatId != 0) {
                j2 = fromChatId;
            }
        }
        if (messageObject.getDialogId() == UserObject.OAUTH || messageObject.isOauthPush) {
            j = j2;
            string = LocaleController.getString(R.string.BotAuthNotificationTitle);
        } else if (fromChatId > 0) {
            if (!messageObject.messageOwner.from_scheduled) {
                TLRPC.User user = getMessagesController().getUser(Long.valueOf(fromChatId));
                string = user != null ? UserObject.getUserName(user) : null;
            } else if (j2 == clientUserId) {
                string = LocaleController.getString(R.string.MessageScheduledReminderNotification);
            } else {
                string = LocaleController.getString(R.string.NotificationMessageScheduledName);
            }
            j = j2;
        } else {
            j = j2;
            TLRPC.Chat chat2 = getMessagesController().getChat(Long.valueOf(-fromChatId));
            string = chat2 != null ? getTitle(chat2) : null;
        }
        if (string == null) {
            return null;
        }
        if (j3 != 0) {
            chat = getMessagesController().getChat(Long.valueOf(j3));
            if (chat == null) {
                return null;
            }
        } else {
            chat = null;
        }
        if (DialogObject.isEncryptedDialog(j)) {
            return LocaleController.getString(R.string.YouHaveNewMessage);
        }
        if (j3 == 0 && fromChatId != 0) {
            if (!z3 || !notificationsSettings.getBoolean("EnablePreviewAll", true)) {
                if (zArr2 != null) {
                    zArr2[0] = false;
                }
                return LocaleController.formatString(R.string.NotificationMessageNoText, string);
            }
            TLRPC.Message message2 = messageObject.messageOwner;
            if (message2 instanceof TLRPC.TL_messageService) {
                TLRPC.MessageAction messageAction = message2.action;
                if ((messageAction instanceof TLRPC.TL_messageActionChangeCreator) || (messageAction instanceof TLRPC.TL_messageActionNewCreatorPending)) {
                    return messageObject.messageText.toString();
                }
                if (messageAction instanceof TLRPC.TL_messageActionSetSameChatWallPaper) {
                    return LocaleController.getString(R.string.WallpaperSameNotification);
                }
                if (messageAction instanceof TLRPC.TL_messageActionSetChatWallPaper) {
                    return LocaleController.getString(R.string.WallpaperNotification);
                }
                if (messageAction instanceof TLRPC.TL_messageActionGeoProximityReached) {
                    return messageObject.messageText.toString();
                }
                if (messageAction instanceof TLRPC.TL_messageActionTodoCompletions) {
                    return messageObject.messageText.toString();
                }
                if (messageAction instanceof TLRPC.TL_messageActionTodoAppendTasks) {
                    return messageObject.messageText.toString();
                }
                if ((messageAction instanceof TLRPC.TL_messageActionUserJoined) || (messageAction instanceof TLRPC.TL_messageActionContactSignUp)) {
                    return LocaleController.formatString(R.string.NotificationContactJoined, string);
                }
                if (messageAction instanceof TLRPC.TL_messageActionUserUpdatedPhoto) {
                    return LocaleController.formatString(R.string.NotificationContactNewPhoto, string);
                }
                if (messageAction instanceof TLRPC.TL_messageActionLoginUnknownLocation) {
                    String string3 = LocaleController.formatString(R.string.formatDateAtTime, LocaleController.getInstance().getFormatterYear().format(((long) messageObject.messageOwner.date) * 1000), LocaleController.getInstance().getFormatterDay().format(((long) messageObject.messageOwner.date) * 1000));
                    int i = R.string.NotificationUnrecognizedDevice;
                    String str = getUserConfig().getCurrentUser().first_name;
                    TLRPC.MessageAction messageAction2 = messageObject.messageOwner.action;
                    return LocaleController.formatString(i, str, string3, messageAction2.title, messageAction2.address);
                }
                if ((messageAction instanceof TLRPC.TL_messageActionGameScore) || (messageAction instanceof TLRPC.TL_messageActionPaymentSent) || (messageAction instanceof TLRPC.TL_messageActionPaymentSentMe)) {
                    return messageObject.messageText.toString();
                }
                if ((messageAction instanceof TLRPC.TL_messageActionStarGift) || (messageAction instanceof TLRPC.TL_messageActionGiftPremium) || (messageAction instanceof TLRPC.TL_messageActionGiftTon)) {
                    return messageObject.messageText.toString();
                }
                if (messageAction instanceof TLRPC.TL_messageActionStarGiftUnique) {
                    return messageObject.messageText.toString();
                }
                if (messageAction instanceof TLRPC.TL_messageActionSuggestBirthday) {
                    return messageObject.messageText.toString();
                }
                if ((messageAction instanceof TLRPC.TL_messageActionPaidMessagesRefunded) || (messageAction instanceof TLRPC.TL_messageActionPaidMessagesPrice)) {
                    return messageObject.messageText.toString();
                }
                if (messageAction instanceof TLRPC.TL_messageActionPhoneCall) {
                    if (messageAction.video) {
                        return LocaleController.getString(R.string.CallMessageVideoIncomingMissed);
                    }
                    return LocaleController.getString(R.string.CallMessageIncomingMissed);
                }
                if (messageAction instanceof TLRPC.TL_messageActionConferenceCall) {
                    if (messageAction.video) {
                        return LocaleController.getString(R.string.CallMessageVideoIncomingConferenceMissed);
                    }
                    return LocaleController.getString(R.string.CallMessageIncomingConferenceMissed);
                }
                if (messageAction instanceof TLRPC.TL_messageActionSetChatTheme) {
                    String themeEmoticonOrGiftTitle = TlUtils.getThemeEmoticonOrGiftTitle(((TLRPC.TL_messageActionSetChatTheme) messageAction).theme);
                    if (!TextUtils.isEmpty(themeEmoticonOrGiftTitle)) {
                        c3 = 0;
                        z2 = true;
                        if (j == clientUserId) {
                            string2 = LocaleController.formatString(R.string.ChatThemeChangedYou, themeEmoticonOrGiftTitle);
                        } else {
                            string2 = LocaleController.formatString(R.string.ChatThemeChangedTo, string, themeEmoticonOrGiftTitle);
                        }
                    } else if (j == clientUserId) {
                        c3 = 0;
                        string2 = LocaleController.formatString(R.string.ChatThemeDisabledYou, new Object[0]);
                        z2 = true;
                    } else {
                        c3 = 0;
                        z2 = true;
                        string2 = LocaleController.formatString(R.string.ChatThemeDisabled, string, themeEmoticonOrGiftTitle);
                    }
                    zArr[c3] = z2;
                    return string2;
                }
            } else {
                if (messageObject.isMediaEmpty()) {
                    if (!z && !TextUtils.isEmpty(messageObject.messageOwner.message)) {
                        String string4 = LocaleController.formatString(R.string.NotificationMessageText, string, messageObject.messageOwner.message);
                        zArr[0] = true;
                        return string4;
                    }
                    return LocaleController.formatString(R.string.NotificationMessageNoText, string);
                }
                TLRPC.Message message3 = messageObject.messageOwner;
                if (message3.media instanceof TLRPC.TL_messageMediaPhoto) {
                    if (z || TextUtils.isEmpty(message3.message)) {
                        return messageObject.messageOwner.media.ttl_seconds != 0 ? LocaleController.formatString(R.string.NotificationMessageSDPhoto, string) : LocaleController.formatString(R.string.NotificationMessagePhoto, string);
                    }
                    String string5 = LocaleController.formatString(R.string.NotificationMessageText, string, "🖼 " + messageObject.messageOwner.message);
                    zArr[0] = true;
                    return string5;
                }
                if (messageObject.isVideo()) {
                    if (z || TextUtils.isEmpty(messageObject.messageOwner.message)) {
                        return messageObject.messageOwner.media.ttl_seconds != 0 ? LocaleController.formatString(R.string.NotificationMessageSDVideo, string) : LocaleController.formatString(R.string.NotificationMessageVideo, string);
                    }
                    String string6 = LocaleController.formatString(R.string.NotificationMessageText, string, "📹 " + messageObject.messageOwner.message);
                    zArr[0] = true;
                    return string6;
                }
                if (messageObject.isGame()) {
                    return LocaleController.formatString(R.string.NotificationMessageGame, string, messageObject.messageOwner.media.game.title);
                }
                if (messageObject.isVoice()) {
                    return LocaleController.formatString(R.string.NotificationMessageAudio, string);
                }
                if (messageObject.isRoundVideo()) {
                    return LocaleController.formatString(R.string.NotificationMessageRound, string);
                }
                if (messageObject.isMusic()) {
                    return LocaleController.formatString(R.string.NotificationMessageMusic, string);
                }
                TLRPC.MessageMedia messageMedia = messageObject.messageOwner.media;
                if (messageMedia instanceof TLRPC.TL_messageMediaContact) {
                    TLRPC.TL_messageMediaContact tL_messageMediaContact = (TLRPC.TL_messageMediaContact) messageMedia;
                    return LocaleController.formatString(R.string.NotificationMessageContact2, string, ContactsController.formatName(tL_messageMediaContact.first_name, tL_messageMediaContact.last_name));
                }
                if (messageMedia instanceof TLRPC.TL_messageMediaGiveaway) {
                    TLRPC.TL_messageMediaGiveaway tL_messageMediaGiveaway = (TLRPC.TL_messageMediaGiveaway) messageMedia;
                    return LocaleController.formatString(R.string.NotificationMessageChannelGiveaway, string, Integer.valueOf(tL_messageMediaGiveaway.quantity), Integer.valueOf(tL_messageMediaGiveaway.months));
                }
                if (messageMedia instanceof TLRPC.TL_messageMediaGiveawayResults) {
                    return LocaleController.formatString(R.string.BoostingGiveawayResults, new Object[0]);
                }
                if (messageMedia instanceof TLRPC.TL_messageMediaPoll) {
                    TLRPC.Poll poll = ((TLRPC.TL_messageMediaPoll) messageMedia).poll;
                    return poll.quiz ? LocaleController.formatString(R.string.NotificationMessageQuiz2, string, poll.question.text) : LocaleController.formatString(R.string.NotificationMessagePoll2, string, poll.question.text);
                }
                if (messageMedia instanceof TLRPC.TL_messageMediaToDo) {
                    return LocaleController.formatString(R.string.NotificationMessageTodo2, string, ((TLRPC.TL_messageMediaToDo) messageMedia).todo.title.text);
                }
                if ((messageMedia instanceof TLRPC.TL_messageMediaGeo) || (messageMedia instanceof TLRPC.TL_messageMediaVenue)) {
                    return LocaleController.formatString(R.string.NotificationMessageMap, string);
                }
                if (messageMedia instanceof TLRPC.TL_messageMediaGeoLive) {
                    return LocaleController.formatString(R.string.NotificationMessageLiveLocation, string);
                }
                if (messageMedia instanceof TLRPC.TL_messageMediaDocument) {
                    if (messageObject.isSticker() || messageObject.isAnimatedSticker()) {
                        String stickerEmoji = messageObject.getStickerEmoji();
                        return stickerEmoji != null ? LocaleController.formatString(R.string.NotificationMessageStickerEmoji, string, stickerEmoji) : LocaleController.formatString(R.string.NotificationMessageSticker, string);
                    }
                    if (messageObject.isGif()) {
                        if (z || TextUtils.isEmpty(messageObject.messageOwner.message)) {
                            return LocaleController.formatString(R.string.NotificationMessageGif, string);
                        }
                        String string7 = LocaleController.formatString(R.string.NotificationMessageText, string, "🎬 " + messageObject.messageOwner.message);
                        zArr[0] = true;
                        return string7;
                    }
                    if (z || TextUtils.isEmpty(messageObject.messageOwner.message)) {
                        return LocaleController.formatString(R.string.NotificationMessageDocument, string);
                    }
                    String string8 = LocaleController.formatString(R.string.NotificationMessageText, string, "📎 " + messageObject.messageOwner.message);
                    zArr[0] = true;
                    return string8;
                }
                if (z || TextUtils.isEmpty(messageObject.messageText)) {
                    return LocaleController.formatString(R.string.NotificationMessageNoText, string);
                }
                String string9 = LocaleController.formatString(R.string.NotificationMessageText, string, messageObject.messageText);
                zArr[0] = true;
                return string9;
            }
        } else if (j3 != 0) {
            boolean z4 = ChatObject.isChannel(chat) && !chat.megagroup;
            if (z3 && ((!z4 && notificationsSettings.getBoolean("EnablePreviewGroup", true)) || (z4 && notificationsSettings.getBoolean("EnablePreviewChannel", true)))) {
                TLRPC.Message message4 = messageObject.messageOwner;
                if (message4 instanceof TLRPC.TL_messageService) {
                    TLRPC.MessageAction messageAction3 = message4.action;
                    if (messageAction3 instanceof TLRPC.TL_messageActionChatAddUser) {
                        long jLongValue = messageAction3.user_id;
                        if (jLongValue == 0 && messageAction3.users.size() == 1) {
                            jLongValue = ((Long) messageObject.messageOwner.action.users.get(0)).longValue();
                        }
                        if (jLongValue != 0) {
                            if (messageObject.messageOwner.peer_id.channel_id != 0 && !chat.megagroup) {
                                return LocaleController.formatString(R.string.ChannelAddedByNotification, string, getTitle(chat));
                            }
                            if (jLongValue == clientUserId) {
                                return LocaleController.formatString(R.string.NotificationInvitedToGroup, string, getTitle(chat));
                            }
                            TLRPC.User user2 = getMessagesController().getUser(Long.valueOf(jLongValue));
                            if (user2 == null) {
                                return null;
                            }
                            if (fromChatId == user2.id) {
                                return chat.megagroup ? LocaleController.formatString(R.string.NotificationGroupAddSelfMega, string, getTitle(chat)) : LocaleController.formatString(R.string.NotificationGroupAddSelf, string, getTitle(chat));
                            }
                            return LocaleController.formatString(R.string.NotificationGroupAddMember, string, getTitle(chat), UserObject.getUserName(user2));
                        }
                        StringBuilder sb = new StringBuilder();
                        for (int i2 = 0; i2 < messageObject.messageOwner.action.users.size(); i2++) {
                            TLRPC.User user3 = getMessagesController().getUser((Long) messageObject.messageOwner.action.users.get(i2));
                            if (user3 != null) {
                                String userName = UserObject.getUserName(user3);
                                if (sb.length() != 0) {
                                    sb.append(", ");
                                }
                                sb.append(userName);
                            }
                        }
                        return LocaleController.formatString(R.string.NotificationGroupAddMember, string, getTitle(chat), sb.toString());
                    }
                    if (messageAction3 instanceof TLRPC.TL_messageActionGroupCall) {
                        return messageAction3.duration != 0 ? LocaleController.formatString(R.string.NotificationGroupEndedCall, string, getTitle(chat)) : LocaleController.formatString(R.string.NotificationGroupCreatedCall, string, getTitle(chat));
                    }
                    if (messageAction3 instanceof TLRPC.TL_messageActionGroupCallScheduled) {
                        return messageObject.messageText.toString();
                    }
                    if (messageAction3 instanceof TLRPC.TL_messageActionInviteToGroupCall) {
                        long jLongValue2 = messageAction3.user_id;
                        if (jLongValue2 == 0 && messageAction3.users.size() == 1) {
                            jLongValue2 = ((Long) messageObject.messageOwner.action.users.get(0)).longValue();
                        }
                        if (jLongValue2 != 0) {
                            if (jLongValue2 == clientUserId) {
                                return LocaleController.formatString(R.string.NotificationGroupInvitedYouToCall, string, getTitle(chat));
                            }
                            TLRPC.User user4 = getMessagesController().getUser(Long.valueOf(jLongValue2));
                            if (user4 == null) {
                                return null;
                            }
                            return LocaleController.formatString(R.string.NotificationGroupInvitedToCall, string, getTitle(chat), UserObject.getUserName(user4));
                        }
                        StringBuilder sb2 = new StringBuilder();
                        for (int i3 = 0; i3 < messageObject.messageOwner.action.users.size(); i3++) {
                            TLRPC.User user5 = getMessagesController().getUser((Long) messageObject.messageOwner.action.users.get(i3));
                            if (user5 != null) {
                                String userName2 = UserObject.getUserName(user5);
                                if (sb2.length() != 0) {
                                    sb2.append(", ");
                                }
                                sb2.append(userName2);
                            }
                        }
                        return LocaleController.formatString(R.string.NotificationGroupInvitedToCall, string, getTitle(chat), sb2.toString());
                    }
                    if (messageAction3 instanceof TLRPC.TL_messageActionGiftCode) {
                        TLRPC.TL_messageActionGiftCode tL_messageActionGiftCode = (TLRPC.TL_messageActionGiftCode) messageAction3;
                        TLRPC.Chat chat3 = MessagesController.getInstance(this.currentAccount).getChat(Long.valueOf(-DialogObject.getPeerDialogId(tL_messageActionGiftCode.boost_peer)));
                        String title = chat3 != null ? getTitle(chat3) : null;
                        return title == null ? LocaleController.getString(R.string.BoostingReceivedGiftNoName) : LocaleController.formatString(R.string.NotificationMessageGiftCode, title, LocaleController.formatPluralString("Months", tL_messageActionGiftCode.months, new Object[0]));
                    }
                    if (messageAction3 instanceof TLRPC.TL_messageActionChatJoinedByLink) {
                        return LocaleController.formatString(R.string.NotificationInvitedToGroupByLink, string, getTitle(chat));
                    }
                    if (messageAction3 instanceof TLRPC.TL_messageActionChatEditTitle) {
                        return LocaleController.formatString(R.string.NotificationEditedGroupName, string, messageAction3.title);
                    }
                    if (messageAction3 instanceof TLRPC.TL_messageActionTodoCompletions) {
                        return messageObject.messageText.toString();
                    }
                    if (messageAction3 instanceof TLRPC.TL_messageActionTodoAppendTasks) {
                        return messageObject.messageText.toString();
                    }
                    if ((messageAction3 instanceof TLRPC.TL_messageActionChatEditPhoto) || (messageAction3 instanceof TLRPC.TL_messageActionChatDeletePhoto)) {
                        if (message4.peer_id.channel_id == 0 || chat.megagroup) {
                            return messageObject.isVideoAvatar() ? LocaleController.formatString(R.string.NotificationEditedGroupVideo, string, getTitle(chat)) : LocaleController.formatString(R.string.NotificationEditedGroupPhoto, string, getTitle(chat));
                        }
                        return messageObject.isVideoAvatar() ? LocaleController.formatString(R.string.ChannelVideoEditNotification, getTitle(chat)) : LocaleController.formatString(R.string.ChannelPhotoEditNotification, getTitle(chat));
                    }
                    if (messageAction3 instanceof TLRPC.TL_messageActionChatDeleteUser) {
                        long j4 = messageAction3.user_id;
                        if (j4 == clientUserId) {
                            return LocaleController.formatString(R.string.NotificationGroupKickYou, string, getTitle(chat));
                        }
                        if (j4 == fromChatId) {
                            return LocaleController.formatString(R.string.NotificationGroupLeftMember, string, getTitle(chat));
                        }
                        TLRPC.User user6 = getMessagesController().getUser(Long.valueOf(messageObject.messageOwner.action.user_id));
                        if (user6 == null) {
                            return null;
                        }
                        return LocaleController.formatString(R.string.NotificationGroupKickMember, string, getTitle(chat), UserObject.getUserName(user6));
                    }
                    if (messageAction3 instanceof TLRPC.TL_messageActionChatCreate) {
                        return messageObject.messageText.toString();
                    }
                    if (messageAction3 instanceof TLRPC.TL_messageActionChannelCreate) {
                        return messageObject.messageText.toString();
                    }
                    if (messageAction3 instanceof TLRPC.TL_messageActionChatMigrateTo) {
                        return LocaleController.formatString(R.string.ActionMigrateFromGroupNotify, getTitle(chat));
                    }
                    if (messageAction3 instanceof TLRPC.TL_messageActionChannelMigrateFrom) {
                        return LocaleController.formatString(R.string.ActionMigrateFromGroupNotify, messageAction3.title);
                    }
                    if (messageAction3 instanceof TLRPC.TL_messageActionScreenshotTaken) {
                        return messageObject.messageText.toString();
                    }
                    if (messageAction3 instanceof TLRPC.TL_messageActionPinMessage) {
                        if (!ChatObject.isChannel(chat) || chat.megagroup) {
                            MessageObject messageObject2 = messageObject.replyMessageObject;
                            if (messageObject2 == null) {
                                return LocaleController.formatString(R.string.NotificationActionPinnedNoText, string, getTitle(chat));
                            }
                            if (messageObject2.isMusic()) {
                                return LocaleController.formatString(R.string.NotificationActionPinnedMusic, string, getTitle(chat));
                            }
                            if (messageObject2.isVideo()) {
                                if (TextUtils.isEmpty(messageObject2.messageOwner.message)) {
                                    return LocaleController.formatString(R.string.NotificationActionPinnedVideo, string, getTitle(chat));
                                }
                                return LocaleController.formatString(R.string.NotificationActionPinnedText, string, "📹 " + messageObject2.messageOwner.message, getTitle(chat));
                            }
                            if (messageObject2.isGif()) {
                                if (TextUtils.isEmpty(messageObject2.messageOwner.message)) {
                                    return LocaleController.formatString(R.string.NotificationActionPinnedGif, string, getTitle(chat));
                                }
                                return LocaleController.formatString(R.string.NotificationActionPinnedText, string, "🎬 " + messageObject2.messageOwner.message, getTitle(chat));
                            }
                            if (messageObject2.isVoice()) {
                                return LocaleController.formatString(R.string.NotificationActionPinnedVoice, string, getTitle(chat));
                            }
                            if (messageObject2.isRoundVideo()) {
                                return LocaleController.formatString(R.string.NotificationActionPinnedRound, string, getTitle(chat));
                            }
                            if (messageObject2.isSticker() || messageObject2.isAnimatedSticker()) {
                                String stickerEmoji2 = messageObject2.getStickerEmoji();
                                return stickerEmoji2 != null ? LocaleController.formatString(R.string.NotificationActionPinnedStickerEmoji, string, getTitle(chat), stickerEmoji2) : LocaleController.formatString(R.string.NotificationActionPinnedSticker, string, getTitle(chat));
                            }
                            TLRPC.Message message5 = messageObject2.messageOwner;
                            TLRPC.MessageMedia messageMedia2 = message5.media;
                            if (messageMedia2 instanceof TLRPC.TL_messageMediaDocument) {
                                if (TextUtils.isEmpty(message5.message)) {
                                    return LocaleController.formatString(R.string.NotificationActionPinnedFile, string, getTitle(chat));
                                }
                                return LocaleController.formatString(R.string.NotificationActionPinnedText, string, "📎 " + messageObject2.messageOwner.message, getTitle(chat));
                            }
                            if ((messageMedia2 instanceof TLRPC.TL_messageMediaGeo) || (messageMedia2 instanceof TLRPC.TL_messageMediaVenue)) {
                                return LocaleController.formatString(R.string.NotificationActionPinnedGeo, string, getTitle(chat));
                            }
                            if (messageMedia2 instanceof TLRPC.TL_messageMediaGeoLive) {
                                return LocaleController.formatString(R.string.NotificationActionPinnedGeoLive, string, getTitle(chat));
                            }
                            if (messageMedia2 instanceof TLRPC.TL_messageMediaContact) {
                                TLRPC.TL_messageMediaContact tL_messageMediaContact2 = (TLRPC.TL_messageMediaContact) messageObject.messageOwner.media;
                                return LocaleController.formatString(R.string.NotificationActionPinnedContact2, string, getTitle(chat), ContactsController.formatName(tL_messageMediaContact2.first_name, tL_messageMediaContact2.last_name));
                            }
                            if (messageMedia2 instanceof TLRPC.TL_messageMediaPoll) {
                                TLRPC.TL_messageMediaPoll tL_messageMediaPoll = (TLRPC.TL_messageMediaPoll) messageMedia2;
                                return tL_messageMediaPoll.poll.quiz ? LocaleController.formatString(R.string.NotificationActionPinnedQuiz2, string, getTitle(chat), tL_messageMediaPoll.poll.question.text) : LocaleController.formatString(R.string.NotificationActionPinnedPoll2, string, getTitle(chat), tL_messageMediaPoll.poll.question.text);
                            }
                            if (messageMedia2 instanceof TLRPC.TL_messageMediaToDo) {
                                return LocaleController.formatString(R.string.NotificationActionPinnedTodo2, string, getTitle(chat), ((TLRPC.TL_messageMediaToDo) messageMedia2).todo.title.text);
                            }
                            if (messageMedia2 instanceof TLRPC.TL_messageMediaPhoto) {
                                if (TextUtils.isEmpty(message5.message)) {
                                    return LocaleController.formatString(R.string.NotificationActionPinnedPhoto, string, getTitle(chat));
                                }
                                return LocaleController.formatString(R.string.NotificationActionPinnedText, string, "🖼 " + messageObject2.messageOwner.message, getTitle(chat));
                            }
                            if (messageMedia2 instanceof TLRPC.TL_messageMediaGame) {
                                return LocaleController.formatString(R.string.NotificationActionPinnedGame, string, getTitle(chat));
                            }
                            CharSequence charSequence = messageObject2.messageText;
                            if (charSequence == null || charSequence.length() <= 0) {
                                return LocaleController.formatString(R.string.NotificationActionPinnedNoText, string, getTitle(chat));
                            }
                            CharSequence string10 = messageObject2.messageText;
                            if (string10.length() > 20) {
                                StringBuilder sb3 = new StringBuilder();
                                c = 0;
                                sb3.append((Object) string10.subSequence(0, 20));
                                sb3.append("...");
                                string10 = sb3.toString();
                            } else {
                                c = 0;
                            }
                            int i4 = R.string.NotificationActionPinnedText;
                            String title2 = getTitle(chat);
                            Object[] objArr = new Object[3];
                            objArr[c] = string;
                            objArr[1] = string10;
                            objArr[2] = title2;
                            return LocaleController.formatString(i4, objArr);
                        }
                        MessageObject messageObject3 = messageObject.replyMessageObject;
                        if (messageObject3 == null) {
                            return LocaleController.formatString(R.string.NotificationActionPinnedNoTextChannel, getTitle(chat));
                        }
                        if (messageObject3.isMusic()) {
                            return LocaleController.formatString(R.string.NotificationActionPinnedMusicChannel, getTitle(chat));
                        }
                        if (messageObject3.isVideo()) {
                            if (TextUtils.isEmpty(messageObject3.messageOwner.message)) {
                                return LocaleController.formatString(R.string.NotificationActionPinnedVideoChannel, getTitle(chat));
                            }
                            return LocaleController.formatString(R.string.NotificationActionPinnedTextChannel, getTitle(chat), "📹 " + messageObject3.messageOwner.message);
                        }
                        if (messageObject3.isGif()) {
                            if (TextUtils.isEmpty(messageObject3.messageOwner.message)) {
                                return LocaleController.formatString(R.string.NotificationActionPinnedGifChannel, getTitle(chat));
                            }
                            return LocaleController.formatString(R.string.NotificationActionPinnedTextChannel, getTitle(chat), "🎬 " + messageObject3.messageOwner.message);
                        }
                        if (messageObject3.isVoice()) {
                            return LocaleController.formatString(R.string.NotificationActionPinnedVoiceChannel, getTitle(chat));
                        }
                        if (messageObject3.isRoundVideo()) {
                            return LocaleController.formatString(R.string.NotificationActionPinnedRoundChannel, getTitle(chat));
                        }
                        if (messageObject3.isSticker() || messageObject3.isAnimatedSticker()) {
                            String stickerEmoji3 = messageObject3.getStickerEmoji();
                            return stickerEmoji3 != null ? LocaleController.formatString(R.string.NotificationActionPinnedStickerEmojiChannel, getTitle(chat), stickerEmoji3) : LocaleController.formatString(R.string.NotificationActionPinnedStickerChannel, getTitle(chat));
                        }
                        TLRPC.Message message6 = messageObject3.messageOwner;
                        TLRPC.MessageMedia messageMedia3 = message6.media;
                        if (messageMedia3 instanceof TLRPC.TL_messageMediaDocument) {
                            if (TextUtils.isEmpty(message6.message)) {
                                return LocaleController.formatString(R.string.NotificationActionPinnedFileChannel, getTitle(chat));
                            }
                            return LocaleController.formatString(R.string.NotificationActionPinnedTextChannel, getTitle(chat), "📎 " + messageObject3.messageOwner.message);
                        }
                        if ((messageMedia3 instanceof TLRPC.TL_messageMediaGeo) || (messageMedia3 instanceof TLRPC.TL_messageMediaVenue)) {
                            return LocaleController.formatString(R.string.NotificationActionPinnedGeoChannel, getTitle(chat));
                        }
                        if (messageMedia3 instanceof TLRPC.TL_messageMediaGeoLive) {
                            return LocaleController.formatString(R.string.NotificationActionPinnedGeoLiveChannel, getTitle(chat));
                        }
                        if (messageMedia3 instanceof TLRPC.TL_messageMediaContact) {
                            TLRPC.TL_messageMediaContact tL_messageMediaContact3 = (TLRPC.TL_messageMediaContact) messageObject.messageOwner.media;
                            return LocaleController.formatString(R.string.NotificationActionPinnedContactChannel2, getTitle(chat), ContactsController.formatName(tL_messageMediaContact3.first_name, tL_messageMediaContact3.last_name));
                        }
                        if (messageMedia3 instanceof TLRPC.TL_messageMediaPoll) {
                            TLRPC.TL_messageMediaPoll tL_messageMediaPoll2 = (TLRPC.TL_messageMediaPoll) messageMedia3;
                            return tL_messageMediaPoll2.poll.quiz ? LocaleController.formatString(R.string.NotificationActionPinnedQuizChannel2, getTitle(chat), tL_messageMediaPoll2.poll.question.text) : LocaleController.formatString(R.string.NotificationActionPinnedPollChannel2, getTitle(chat), tL_messageMediaPoll2.poll.question.text);
                        }
                        if (messageMedia3 instanceof TLRPC.TL_messageMediaToDo) {
                            return LocaleController.formatString(R.string.NotificationActionPinnedTodoChannel2, getTitle(chat), ((TLRPC.TL_messageMediaToDo) messageMedia3).todo.title.text);
                        }
                        if (messageMedia3 instanceof TLRPC.TL_messageMediaPhoto) {
                            if (TextUtils.isEmpty(message6.message)) {
                                return LocaleController.formatString(R.string.NotificationActionPinnedPhotoChannel, getTitle(chat));
                            }
                            return LocaleController.formatString(R.string.NotificationActionPinnedTextChannel, getTitle(chat), "🖼 " + messageObject3.messageOwner.message);
                        }
                        if (messageMedia3 instanceof TLRPC.TL_messageMediaGame) {
                            return LocaleController.formatString(R.string.NotificationActionPinnedGameChannel, getTitle(chat));
                        }
                        CharSequence charSequence2 = messageObject3.messageText;
                        if (charSequence2 == null || charSequence2.length() <= 0) {
                            return LocaleController.formatString(R.string.NotificationActionPinnedNoTextChannel, getTitle(chat));
                        }
                        CharSequence string11 = messageObject3.messageText;
                        if (string11.length() > 20) {
                            StringBuilder sb4 = new StringBuilder();
                            c2 = 0;
                            sb4.append((Object) string11.subSequence(0, 20));
                            sb4.append("...");
                            string11 = sb4.toString();
                        } else {
                            c2 = 0;
                        }
                        int i5 = R.string.NotificationActionPinnedTextChannel;
                        Object[] objArr2 = new Object[2];
                        objArr2[c2] = getTitle(chat);
                        objArr2[1] = string11;
                        return LocaleController.formatString(i5, objArr2);
                    }
                    if (messageAction3 instanceof TLRPC.TL_messageActionGameScore) {
                        return messageObject.messageText.toString();
                    }
                    if (messageAction3 instanceof TLRPC.TL_messageActionSetChatTheme) {
                        String themeEmoticonOrGiftTitle2 = TlUtils.getThemeEmoticonOrGiftTitle(((TLRPC.TL_messageActionSetChatTheme) messageAction3).theme);
                        if (TextUtils.isEmpty(themeEmoticonOrGiftTitle2)) {
                            return j == clientUserId ? LocaleController.formatString(R.string.ChatThemeDisabledYou, new Object[0]) : LocaleController.formatString("ChatThemeDisabled", R.string.ChatThemeDisabled, string, themeEmoticonOrGiftTitle2);
                        }
                        return j == clientUserId ? LocaleController.formatString(R.string.ChatThemeChangedYou, themeEmoticonOrGiftTitle2) : LocaleController.formatString(R.string.ChatThemeChangedTo, string, themeEmoticonOrGiftTitle2);
                    }
                    if (messageAction3 instanceof TLRPC.TL_messageActionChatJoinedByRequest) {
                        return messageObject.messageText.toString();
                    }
                } else {
                    if (ChatObject.isChannel(chat) && !chat.megagroup) {
                        if (messageObject.isMediaEmpty()) {
                            if (z || TextUtils.isEmpty(messageObject.messageOwner.message)) {
                                return LocaleController.formatString(R.string.ChannelMessageNoText, string);
                            }
                            String string12 = LocaleController.formatString(R.string.NotificationMessageText, string, messageObject.messageOwner.message);
                            zArr[0] = true;
                            return string12;
                        }
                        if (messageObject.type == 29 && (MessageObject.getMedia(messageObject) instanceof TLRPC.TL_messageMediaPaidMedia)) {
                            return LocaleController.formatPluralString("NotificationChannelMessagePaidMedia", (int) ((TLRPC.TL_messageMediaPaidMedia) MessageObject.getMedia(messageObject)).stars_amount, getTitle(chat));
                        }
                        TLRPC.Message message7 = messageObject.messageOwner;
                        if (message7.media instanceof TLRPC.TL_messageMediaPhoto) {
                            if (z || TextUtils.isEmpty(message7.message)) {
                                return LocaleController.formatString(R.string.ChannelMessagePhoto, string);
                            }
                            String string13 = LocaleController.formatString(R.string.NotificationMessageText, string, "🖼 " + messageObject.messageOwner.message);
                            zArr[0] = true;
                            return string13;
                        }
                        if (messageObject.isVideo()) {
                            if (z || TextUtils.isEmpty(messageObject.messageOwner.message)) {
                                return LocaleController.formatString(R.string.ChannelMessageVideo, string);
                            }
                            String string14 = LocaleController.formatString(R.string.NotificationMessageText, string, "📹 " + messageObject.messageOwner.message);
                            zArr[0] = true;
                            return string14;
                        }
                        if (messageObject.isVoice()) {
                            return LocaleController.formatString(R.string.ChannelMessageAudio, string);
                        }
                        if (messageObject.isRoundVideo()) {
                            return LocaleController.formatString(R.string.ChannelMessageRound, string);
                        }
                        if (messageObject.isMusic()) {
                            return LocaleController.formatString(R.string.ChannelMessageMusic, string);
                        }
                        TLRPC.MessageMedia messageMedia4 = messageObject.messageOwner.media;
                        if (messageMedia4 instanceof TLRPC.TL_messageMediaContact) {
                            TLRPC.TL_messageMediaContact tL_messageMediaContact4 = (TLRPC.TL_messageMediaContact) messageMedia4;
                            return LocaleController.formatString(R.string.ChannelMessageContact2, string, ContactsController.formatName(tL_messageMediaContact4.first_name, tL_messageMediaContact4.last_name));
                        }
                        if (messageMedia4 instanceof TLRPC.TL_messageMediaPoll) {
                            TLRPC.Poll poll2 = ((TLRPC.TL_messageMediaPoll) messageMedia4).poll;
                            return poll2.quiz ? LocaleController.formatString(R.string.ChannelMessageQuiz2, string, poll2.question.text) : LocaleController.formatString(R.string.ChannelMessagePoll2, string, poll2.question.text);
                        }
                        if (messageMedia4 instanceof TLRPC.TL_messageMediaToDo) {
                            return LocaleController.formatString(R.string.ChannelMessageTodo2, string, ((TLRPC.TL_messageMediaToDo) messageMedia4).todo.title.text);
                        }
                        if (messageMedia4 instanceof TLRPC.TL_messageMediaGiveaway) {
                            TLRPC.TL_messageMediaGiveaway tL_messageMediaGiveaway2 = (TLRPC.TL_messageMediaGiveaway) messageMedia4;
                            return LocaleController.formatString(R.string.NotificationMessageChannelGiveaway, getTitle(chat), Integer.valueOf(tL_messageMediaGiveaway2.quantity), Integer.valueOf(tL_messageMediaGiveaway2.months));
                        }
                        if ((messageMedia4 instanceof TLRPC.TL_messageMediaGeo) || (messageMedia4 instanceof TLRPC.TL_messageMediaVenue)) {
                            return LocaleController.formatString(R.string.ChannelMessageMap, string);
                        }
                        if (messageMedia4 instanceof TLRPC.TL_messageMediaGeoLive) {
                            return LocaleController.formatString(R.string.ChannelMessageLiveLocation, string);
                        }
                        if (messageMedia4 instanceof TLRPC.TL_messageMediaDocument) {
                            if (messageObject.isSticker() || messageObject.isAnimatedSticker()) {
                                String stickerEmoji4 = messageObject.getStickerEmoji();
                                return stickerEmoji4 != null ? LocaleController.formatString(R.string.ChannelMessageStickerEmoji, string, stickerEmoji4) : LocaleController.formatString(R.string.ChannelMessageSticker, string);
                            }
                            if (messageObject.isGif()) {
                                if (z || TextUtils.isEmpty(messageObject.messageOwner.message)) {
                                    return LocaleController.formatString(R.string.ChannelMessageGIF, string);
                                }
                                String string15 = LocaleController.formatString(R.string.NotificationMessageText, string, "🎬 " + messageObject.messageOwner.message);
                                zArr[0] = true;
                                return string15;
                            }
                            if (z || TextUtils.isEmpty(messageObject.messageOwner.message)) {
                                return LocaleController.formatString(R.string.ChannelMessageDocument, string);
                            }
                            String string16 = LocaleController.formatString(R.string.NotificationMessageText, string, "📎 " + messageObject.messageOwner.message);
                            zArr[0] = true;
                            return string16;
                        }
                        if (z || TextUtils.isEmpty(messageObject.messageText)) {
                            return LocaleController.formatString(R.string.ChannelMessageNoText, string);
                        }
                        String string17 = LocaleController.formatString(R.string.NotificationMessageText, string, messageObject.messageText);
                        zArr[0] = true;
                        return string17;
                    }
                    if (messageObject.isMediaEmpty()) {
                        return (z || TextUtils.isEmpty(messageObject.messageOwner.message)) ? LocaleController.formatString(R.string.NotificationMessageGroupNoText, string, getTitle(chat)) : LocaleController.formatString(R.string.NotificationMessageGroupText, string, getTitle(chat), messageObject.messageOwner.message);
                    }
                    if (messageObject.type == 29 && (MessageObject.getMedia(messageObject) instanceof TLRPC.TL_messageMediaPaidMedia)) {
                        return LocaleController.formatPluralString("NotificationChatMessagePaidMedia", (int) ((TLRPC.TL_messageMediaPaidMedia) MessageObject.getMedia(messageObject)).stars_amount, string, getTitle(chat));
                    }
                    TLRPC.Message message8 = messageObject.messageOwner;
                    if (message8.media instanceof TLRPC.TL_messageMediaPhoto) {
                        if (z || TextUtils.isEmpty(message8.message)) {
                            return LocaleController.formatString(R.string.NotificationMessageGroupPhoto, string, getTitle(chat));
                        }
                        return LocaleController.formatString(R.string.NotificationMessageGroupText, string, getTitle(chat), "🖼 " + messageObject.messageOwner.message);
                    }
                    if (messageObject.isVideo()) {
                        if (z || TextUtils.isEmpty(messageObject.messageOwner.message)) {
                            return LocaleController.formatString(R.string.NotificationMessageGroupVideo, string, getTitle(chat));
                        }
                        return LocaleController.formatString(R.string.NotificationMessageGroupText, string, getTitle(chat), "📹 " + messageObject.messageOwner.message);
                    }
                    if (messageObject.isVoice()) {
                        return LocaleController.formatString(R.string.NotificationMessageGroupAudio, string, getTitle(chat));
                    }
                    if (messageObject.isRoundVideo()) {
                        return LocaleController.formatString(R.string.NotificationMessageGroupRound, string, getTitle(chat));
                    }
                    if (messageObject.isMusic()) {
                        return LocaleController.formatString(R.string.NotificationMessageGroupMusic, string, getTitle(chat));
                    }
                    TLRPC.MessageMedia messageMedia5 = messageObject.messageOwner.media;
                    if (messageMedia5 instanceof TLRPC.TL_messageMediaContact) {
                        TLRPC.TL_messageMediaContact tL_messageMediaContact5 = (TLRPC.TL_messageMediaContact) messageMedia5;
                        return LocaleController.formatString(R.string.NotificationMessageGroupContact2, string, getTitle(chat), ContactsController.formatName(tL_messageMediaContact5.first_name, tL_messageMediaContact5.last_name));
                    }
                    if (messageMedia5 instanceof TLRPC.TL_messageMediaPoll) {
                        TLRPC.TL_messageMediaPoll tL_messageMediaPoll3 = (TLRPC.TL_messageMediaPoll) messageMedia5;
                        return tL_messageMediaPoll3.poll.quiz ? LocaleController.formatString(R.string.NotificationMessageGroupQuiz2, string, getTitle(chat), tL_messageMediaPoll3.poll.question.text) : LocaleController.formatString(R.string.NotificationMessageGroupPoll2, string, getTitle(chat), tL_messageMediaPoll3.poll.question.text);
                    }
                    if (messageMedia5 instanceof TLRPC.TL_messageMediaToDo) {
                        return LocaleController.formatString(R.string.NotificationMessageGroupTodo2, string, getTitle(chat), ((TLRPC.TL_messageMediaToDo) messageMedia5).todo.title.text);
                    }
                    if (messageMedia5 instanceof TLRPC.TL_messageMediaGame) {
                        return LocaleController.formatString(R.string.NotificationMessageGroupGame, string, getTitle(chat), messageObject.messageOwner.media.game.title);
                    }
                    if (messageMedia5 instanceof TLRPC.TL_messageMediaGiveaway) {
                        TLRPC.TL_messageMediaGiveaway tL_messageMediaGiveaway3 = (TLRPC.TL_messageMediaGiveaway) messageMedia5;
                        return LocaleController.formatString(R.string.NotificationMessageChannelGiveaway, getTitle(chat), Integer.valueOf(tL_messageMediaGiveaway3.quantity), Integer.valueOf(tL_messageMediaGiveaway3.months));
                    }
                    if (messageMedia5 instanceof TLRPC.TL_messageMediaGiveawayResults) {
                        return LocaleController.formatString(R.string.BoostingGiveawayResults, new Object[0]);
                    }
                    if ((messageMedia5 instanceof TLRPC.TL_messageMediaGeo) || (messageMedia5 instanceof TLRPC.TL_messageMediaVenue)) {
                        return LocaleController.formatString("NotificationMessageGroupMap", R.string.NotificationMessageGroupMap, string, getTitle(chat));
                    }
                    if (messageMedia5 instanceof TLRPC.TL_messageMediaGeoLive) {
                        return LocaleController.formatString(R.string.NotificationMessageGroupLiveLocation, string, getTitle(chat));
                    }
                    if (!(messageMedia5 instanceof TLRPC.TL_messageMediaDocument)) {
                        return (z || TextUtils.isEmpty(messageObject.messageText)) ? LocaleController.formatString(R.string.NotificationMessageGroupNoText, string, getTitle(chat)) : LocaleController.formatString(R.string.NotificationMessageGroupText, string, getTitle(chat), messageObject.messageText);
                    }
                    if (messageObject.isSticker() || messageObject.isAnimatedSticker()) {
                        String stickerEmoji5 = messageObject.getStickerEmoji();
                        return stickerEmoji5 != null ? LocaleController.formatString(R.string.NotificationMessageGroupStickerEmoji, string, getTitle(chat), stickerEmoji5) : LocaleController.formatString(R.string.NotificationMessageGroupSticker, string, getTitle(chat));
                    }
                    if (messageObject.isGif()) {
                        if (z || TextUtils.isEmpty(messageObject.messageOwner.message)) {
                            return LocaleController.formatString(R.string.NotificationMessageGroupGif, string, getTitle(chat));
                        }
                        return LocaleController.formatString(R.string.NotificationMessageGroupText, string, getTitle(chat), "🎬 " + messageObject.messageOwner.message);
                    }
                    if (z || TextUtils.isEmpty(messageObject.messageOwner.message)) {
                        return LocaleController.formatString(R.string.NotificationMessageGroupDocument, string, getTitle(chat));
                    }
                    return LocaleController.formatString(R.string.NotificationMessageGroupText, string, getTitle(chat), "📎 " + messageObject.messageOwner.message);
                }
            } else {
                if (zArr2 != null) {
                    zArr2[0] = false;
                }
                if (!ChatObject.isChannel(chat) || chat.megagroup) {
                    return (messageObject.type == 29 && (MessageObject.getMedia(messageObject) instanceof TLRPC.TL_messageMediaPaidMedia)) ? LocaleController.formatPluralString("NotificationMessagePaidMedia", (int) ((TLRPC.TL_messageMediaPaidMedia) MessageObject.getMedia(messageObject)).stars_amount, string) : LocaleController.formatString(R.string.NotificationMessageGroupNoText, string, getTitle(chat));
                }
                return LocaleController.formatString(R.string.ChannelMessageNoText, string);
            }
        }
        return null;
    }

    private void scheduleNotificationRepeat() {
        try {
            Intent intent = new Intent(ApplicationLoader.applicationContext, (Class<?>) NotificationRepeat.class);
            intent.putExtra("currentAccount", this.currentAccount);
            PendingIntent service = PendingIntent.getService(ApplicationLoader.applicationContext, 0, intent, 33554432);
            int i = getAccountInstance().getNotificationsSettings().getInt("repeat_messages", 60);
            if (i > 0 && this.personalCount > 0) {
                this.alarmManager.set(2, SystemClock.elapsedRealtime() + ((long) (i * 60000)), service);
            } else {
                this.alarmManager.cancel(service);
            }
        } catch (Exception e) {
            FileLog.e(e);
        }
    }

    private boolean isPersonalMessage(MessageObject messageObject) {
        TLRPC.MessageAction messageAction;
        TLRPC.Message message = messageObject.messageOwner;
        TLRPC.Peer peer = message.peer_id;
        return (peer != null && peer.chat_id == 0 && peer.channel_id == 0 && ((messageAction = message.action) == null || (messageAction instanceof TLRPC.TL_messageActionEmpty))) || messageObject.isStoryReactionPush;
    }

    private int getNotifyOverride(SharedPreferences sharedPreferences, long j, long j2) {
        int property = this.dialogsNotificationsFacade.getProperty(NotificationsSettingsFacade.PROPERTY_NOTIFY, j, j2, -1);
        if (property != 3 || this.dialogsNotificationsFacade.getProperty(NotificationsSettingsFacade.PROPERTY_NOTIFY_UNTIL, j, j2, 0) < getConnectionsManager().getCurrentTime()) {
            return property;
        }
        return 2;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$showNotifications$34() {
        showOrUpdateNotification(false);
    }

    public void showNotifications() {
        notificationsQueue.postRunnable(new Runnable() { // from class: org.telegram.messenger.NotificationsController$$ExternalSyntheticLambda56
            @Override // java.lang.Runnable
            public final void run() {
                this.f$0.lambda$showNotifications$34();
            }
        });
    }

    public void hideNotifications() {
        notificationsQueue.postRunnable(new Runnable() { // from class: org.telegram.messenger.NotificationsController$$ExternalSyntheticLambda15
            @Override // java.lang.Runnable
            public final void run() {
                this.f$0.lambda$hideNotifications$35();
            }
        });
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$hideNotifications$35() {
        notificationManager.cancel(this.notificationId);
        this.lastWearNotifiedMessageId.clear();
        for (int i = 0; i < this.wearNotificationsIds.size(); i++) {
            notificationManager.cancel(((Integer) this.wearNotificationsIds.valueAt(i)).intValue());
        }
        this.wearNotificationsIds.clear();
    }

    private void dismissNotification() {
        FileLog.d("NotificationsController dismissNotification");
        try {
            notificationManager.cancel(this.notificationId);
            this.pushMessages.clear();
            this.pushMessagesDict.clear();
            this.lastWearNotifiedMessageId.clear();
            for (int i = 0; i < this.wearNotificationsIds.size(); i++) {
                if (!this.openedInBubbleDialogs.contains(Long.valueOf(this.wearNotificationsIds.keyAt(i)))) {
                    notificationManager.cancel(((Integer) this.wearNotificationsIds.valueAt(i)).intValue());
                }
            }
            this.wearNotificationsIds.clear();
            AndroidUtilities.runOnUIThread(new Runnable() { // from class: org.telegram.messenger.NotificationsController$$ExternalSyntheticLambda17
                @Override // java.lang.Runnable
                public final void run() {
                    NotificationCenter.getGlobalInstance().lambda$postNotificationNameOnUIThread$1(NotificationCenter.pushMessagesUpdated, new Object[0]);
                }
            });
        } catch (Exception e) {
            FileLog.e(e);
        }
    }

    /* JADX WARN: Code restructure failed: missing block: B:8:0x0016, code lost:
    
        if (org.telegram.messenger.NotificationsController.audioManager.getRingerMode() == 0) goto L6;
     */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
    */
    private void playInChatSound() {
        if (this.inChatSoundEnabled && !MediaController.getInstance().isRecordingAudio()) {
            try {
            } catch (Exception e) {
                FileLog.e(e);
            }
        }
        return;
        try {
            try {
                if (getNotifyOverride(getAccountInstance().getNotificationsSettings(), this.openedDialogId, this.openedTopicId) == 2) {
                    return;
                }
                notificationsQueue.postRunnable(new Runnable() { // from class: org.telegram.messenger.NotificationsController$$ExternalSyntheticLambda12
                    @Override // java.lang.Runnable
                    public final void run() {
                        this.f$0.lambda$playInChatSound$38();
                    }
                });
            } catch (Exception e2) {
                e = e2;
                FileLog.e(e);
            }
        } catch (Exception e3) {
            e = e3;
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$playInChatSound$38() {
        if (Math.abs(SystemClock.elapsedRealtime() - this.lastSoundPlay) <= 500) {
            return;
        }
        try {
            if (this.soundPool == null) {
                SoundPool soundPool = new SoundPool(3, 1, 0);
                this.soundPool = soundPool;
                soundPool.setOnLoadCompleteListener(new SoundPool.OnLoadCompleteListener() { // from class: org.telegram.messenger.NotificationsController$$ExternalSyntheticLambda46
                    @Override // android.media.SoundPool.OnLoadCompleteListener
                    public final void onLoadComplete(SoundPool soundPool2, int i, int i2) {
                        NotificationsController.m3648$r8$lambda$YWu30oJSAuR52YOOKH98OkETVA(soundPool2, i, i2);
                    }
                });
            }
            if (this.soundIn == 0 && !this.soundInLoaded) {
                this.soundInLoaded = true;
                this.soundIn = this.soundPool.load(ApplicationLoader.applicationContext, R.raw.sound_in, 1);
            }
            int i = this.soundIn;
            if (i != 0) {
                try {
                    this.soundPool.play(i, 1.0f, 1.0f, 1, 0, 1.0f);
                } catch (Exception e) {
                    FileLog.e(e);
                }
            }
        } catch (Exception e2) {
            FileLog.e(e2);
        }
    }

    /* JADX INFO: renamed from: $r8$lambda$YWu30oJSAuR52YOO-KH98OkETVA, reason: not valid java name */
    public static /* synthetic */ void m3648$r8$lambda$YWu30oJSAuR52YOOKH98OkETVA(SoundPool soundPool, int i, int i2) {
        if (i2 == 0) {
            try {
                soundPool.play(i, 1.0f, 1.0f, 1, 0, 1.0f);
            } catch (Exception e) {
                FileLog.e(e);
            }
        }
    }

    private void scheduleNotificationDelay(boolean z) {
        try {
            if (BuildVars.LOGS_ENABLED) {
                FileLog.d("delay notification start, onlineReason = " + z);
            }
            this.notificationDelayWakelock.acquire(10000L);
            DispatchQueue dispatchQueue = notificationsQueue;
            dispatchQueue.cancelRunnable(this.notificationDelayRunnable);
            dispatchQueue.postRunnable(this.notificationDelayRunnable, z ? 3000 : MediaDataController.MAX_STYLE_RUNS_COUNT);
        } catch (Exception e) {
            FileLog.e(e);
            showOrUpdateNotification(this.notifyCheck);
        }
    }

    /* JADX INFO: Access modifiers changed from: protected */
    public void repeatNotificationMaybe() {
        notificationsQueue.postRunnable(new Runnable() { // from class: org.telegram.messenger.NotificationsController$$ExternalSyntheticLambda16
            @Override // java.lang.Runnable
            public final void run() {
                this.f$0.lambda$repeatNotificationMaybe$39();
            }
        });
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$repeatNotificationMaybe$39() {
        int i = Calendar.getInstance().get(11);
        if (i >= 11 && i <= 22) {
            notificationManager.cancel(this.notificationId);
            showOrUpdateNotification(true);
        } else {
            scheduleNotificationRepeat();
        }
    }

    private boolean isEmptyVibration(long[] jArr) {
        if (jArr == null || jArr.length == 0) {
            return false;
        }
        for (long j : jArr) {
            if (j != 0) {
                return false;
            }
        }
        return true;
    }

    public void deleteNotificationChannel(long j, long j2) {
        deleteNotificationChannel(j, j2, -1);
    }

    /* JADX INFO: Access modifiers changed from: private */
    /* JADX INFO: renamed from: deleteNotificationChannelInternal, reason: merged with bridge method [inline-methods] */
    public void lambda$deleteNotificationChannel$40(long j, long j2, int i) {
        if (Build.VERSION.SDK_INT < 26) {
            return;
        }
        try {
            SharedPreferences notificationsSettings = getAccountInstance().getNotificationsSettings();
            SharedPreferences.Editor editorEdit = notificationsSettings.edit();
            if (i == 0 || i == -1) {
                String str = "org.telegram.key" + j;
                if (j2 != 0) {
                    str = str + ".topic" + j2;
                }
                String string = notificationsSettings.getString(str, null);
                if (string != null) {
                    editorEdit.remove(str).remove(str + "_s");
                    try {
                        systemNotificationManager.deleteNotificationChannel(string);
                    } catch (Exception e) {
                        FileLog.e(e);
                    }
                    if (BuildVars.LOGS_ENABLED) {
                        FileLog.d("delete channel internal " + string);
                    }
                }
            }
            if (i == 1 || i == -1) {
                String str2 = "org.telegram.keyia" + j;
                String string2 = notificationsSettings.getString(str2, null);
                if (string2 != null) {
                    editorEdit.remove(str2).remove(str2 + "_s");
                    try {
                        systemNotificationManager.deleteNotificationChannel(string2);
                    } catch (Exception e2) {
                        FileLog.e(e2);
                    }
                    if (BuildVars.LOGS_ENABLED) {
                        FileLog.d("delete channel internal " + string2);
                    }
                }
            }
            editorEdit.apply();
        } catch (Exception e3) {
            FileLog.e(e3);
        }
    }

    public void deleteNotificationChannel(final long j, final long j2, final int i) {
        if (Build.VERSION.SDK_INT < 26) {
            return;
        }
        notificationsQueue.postRunnable(new Runnable() { // from class: org.telegram.messenger.NotificationsController$$ExternalSyntheticLambda44
            @Override // java.lang.Runnable
            public final void run() {
                this.f$0.lambda$deleteNotificationChannel$40(j, j2, i);
            }
        });
    }

    public void deleteNotificationChannelGlobal(int i) {
        deleteNotificationChannelGlobal(i, -1);
    }

    /* JADX INFO: renamed from: deleteNotificationChannelGlobalInternal, reason: merged with bridge method [inline-methods] */
    public void lambda$deleteNotificationChannelGlobal$41(int i, int i2) {
        String str;
        String str2;
        String str3;
        if (Build.VERSION.SDK_INT < 26) {
            return;
        }
        try {
            SharedPreferences notificationsSettings = getAccountInstance().getNotificationsSettings();
            SharedPreferences.Editor editorEdit = notificationsSettings.edit();
            if (i2 == 0 || i2 == -1) {
                if (i == 2) {
                    str = "channels";
                } else if (i == 0) {
                    str = "groups";
                } else if (i == 3) {
                    str = "stories";
                } else if (i == 4 || i == 5) {
                    str = "reactions";
                } else {
                    str = "private";
                }
                String string = notificationsSettings.getString(str, null);
                if (string != null) {
                    editorEdit.remove(str).remove(str + "_s");
                    try {
                        systemNotificationManager.deleteNotificationChannel(string);
                    } catch (Exception e) {
                        FileLog.e(e);
                    }
                    if (BuildVars.LOGS_ENABLED) {
                        FileLog.d("delete channel global internal " + string);
                    }
                }
            }
            if (i2 == 1 || i2 == -1) {
                if (i == 2) {
                    str2 = "channels_ia";
                } else if (i == 0) {
                    str2 = "groups_ia";
                } else if (i == 3) {
                    str2 = "stories_ia";
                } else if (i == 4 || i == 5) {
                    str2 = "reactions_ia";
                } else {
                    str2 = "private_ia";
                }
                String string2 = notificationsSettings.getString(str2, null);
                if (string2 != null) {
                    editorEdit.remove(str2).remove(str2 + "_s");
                    try {
                        systemNotificationManager.deleteNotificationChannel(string2);
                    } catch (Exception e2) {
                        FileLog.e(e2);
                    }
                    if (BuildVars.LOGS_ENABLED) {
                        FileLog.d("delete channel global internal " + string2);
                    }
                }
            }
            if (i == 2) {
                str3 = "overwrite_channel";
            } else if (i == 0) {
                str3 = "overwrite_group";
            } else if (i == 3) {
                str3 = "overwrite_stories";
            } else if (i == 4 || i == 5) {
                str3 = "overwrite_reactions";
            } else {
                str3 = "overwrite_private";
            }
            editorEdit.remove(str3);
            editorEdit.apply();
        } catch (Exception e3) {
            FileLog.e(e3);
        }
    }

    public void deleteNotificationChannelGlobal(final int i, final int i2) {
        if (Build.VERSION.SDK_INT < 26) {
            return;
        }
        notificationsQueue.postRunnable(new Runnable() { // from class: org.telegram.messenger.NotificationsController$$ExternalSyntheticLambda40
            @Override // java.lang.Runnable
            public final void run() {
                this.f$0.lambda$deleteNotificationChannelGlobal$41(i, i2);
            }
        });
    }

    public void deleteAllNotificationChannels() {
        if (Build.VERSION.SDK_INT < 26) {
            return;
        }
        notificationsQueue.postRunnable(new Runnable() { // from class: org.telegram.messenger.NotificationsController$$ExternalSyntheticLambda41
            @Override // java.lang.Runnable
            public final void run() {
                this.f$0.lambda$deleteAllNotificationChannels$42();
            }
        });
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$deleteAllNotificationChannels$42() {
        try {
            SharedPreferences notificationsSettings = getAccountInstance().getNotificationsSettings();
            Map<String, ?> all = notificationsSettings.getAll();
            SharedPreferences.Editor editorEdit = notificationsSettings.edit();
            for (Map.Entry<String, ?> entry : all.entrySet()) {
                String key = entry.getKey();
                if (key.startsWith("org.telegram.key")) {
                    if (!key.endsWith("_s")) {
                        String str = (String) entry.getValue();
                        systemNotificationManager.deleteNotificationChannel(str);
                        if (BuildVars.LOGS_ENABLED) {
                            FileLog.d("delete all channel " + str);
                        }
                    }
                    editorEdit.remove(key);
                }
            }
            editorEdit.apply();
        } catch (Exception e) {
            FileLog.e(e);
        }
    }

    private boolean unsupportedNotificationShortcut() {
        return Build.VERSION.SDK_INT < 29 || !SharedConfig.chatBubbles;
    }

    /* JADX WARN: Code duplicated, block: B:25:0x00bb  */
    @SuppressLint({"RestrictedApi"})
    private String createNotificationShortcut(NotificationCompat.Builder builder, long j, String str, TLRPC.User user, TLRPC.Chat chat, Person person, boolean z) {
        Bitmap bitmap;
        IconCompat iconCompatCreateWithResource;
        if (!unsupportedNotificationShortcut() && (!ChatObject.isChannel(chat) || chat.megagroup)) {
            try {
                String str2 = "ndid_" + j;
                Intent intent = new Intent(ApplicationLoader.applicationContext, (Class<?>) OpenChatReceiver.class);
                intent.setAction("com.tmessages.openchat" + Math.random() + Integer.MAX_VALUE);
                if (j > 0) {
                    intent.putExtra("userId", j);
                } else {
                    intent.putExtra("chatId", -j);
                }
                ShortcutInfoCompat.Builder locusId = new ShortcutInfoCompat.Builder(ApplicationLoader.applicationContext, str2).setShortLabel(chat != null ? str : UserObject.getFirstName(user)).setLongLabel(str).setIntent(new Intent("android.intent.action.VIEW")).setIntent(intent).setLongLived(true).setLocusId(new LocusIdCompat(str2));
                if (person != null) {
                    locusId.setPerson(person);
                    locusId.setIcon(person.getIcon());
                    if (person.getIcon() != null) {
                        bitmap = person.getIcon().getBitmap();
                    } else {
                        bitmap = null;
                    }
                } else {
                    bitmap = null;
                }
                ShortcutInfoCompat shortcutInfoCompatBuild = locusId.build();
                ShortcutManagerCompat.pushDynamicShortcut(ApplicationLoader.applicationContext, shortcutInfoCompatBuild);
                builder.setShortcutInfo(shortcutInfoCompatBuild);
                Intent intent2 = new Intent(ApplicationLoader.applicationContext, (Class<?>) BubbleActivity.class);
                StringBuilder sb = new StringBuilder();
                sb.append("com.tmessages.openchat");
                Bitmap bitmap2 = bitmap;
                sb.append(Math.random());
                sb.append(Integer.MAX_VALUE);
                intent2.setAction(sb.toString());
                if (DialogObject.isUserDialog(j)) {
                    intent2.putExtra("userId", j);
                } else {
                    intent2.putExtra("chatId", -j);
                }
                intent2.putExtra("currentAccount", this.currentAccount);
                if (bitmap2 != null) {
                    iconCompatCreateWithResource = IconCompat.createWithAdaptiveBitmap(bitmap2);
                } else if (user != null) {
                    iconCompatCreateWithResource = IconCompat.createWithResource(ApplicationLoader.applicationContext, user.bot ? R.drawable.book_bot : R.drawable.book_user);
                } else {
                    iconCompatCreateWithResource = IconCompat.createWithResource(ApplicationLoader.applicationContext, R.drawable.book_group);
                }
                if (z) {
                    NotificationCompat.BubbleMetadata.Builder builder2 = new NotificationCompat.BubbleMetadata.Builder(PendingIntent.getActivity(ApplicationLoader.applicationContext, 0, intent2, 167772160), iconCompatCreateWithResource);
                    builder2.setSuppressNotification(this.openedDialogId == j);
                    builder2.setAutoExpandBubble(false);
                    builder2.setDesiredHeight(AndroidUtilities.dp(640.0f));
                    builder.setBubbleMetadata(builder2.build());
                    return str2;
                }
                builder.setBubbleMetadata(null);
                return str2;
            } catch (Exception e) {
                FileLog.e(e);
            }
        }
        return null;
    }

    @TargetApi(26)
    protected void ensureGroupsCreated() {
        SharedPreferences notificationsSettings = getAccountInstance().getNotificationsSettings();
        if (this.groupsCreated == null) {
            this.groupsCreated = Boolean.valueOf(notificationsSettings.getBoolean("groupsCreated5", false));
        }
        if (!this.groupsCreated.booleanValue()) {
            try {
                String str = this.currentAccount + "channel";
                List<NotificationChannel> notificationChannels = systemNotificationManager.getNotificationChannels();
                int size = notificationChannels.size();
                SharedPreferences.Editor editorEdit = null;
                for (int i = 0; i < size; i++) {
                    NotificationChannel notificationChannelM = NotificationsController$$ExternalSyntheticApiModelOutline3.m(notificationChannels.get(i));
                    String id = notificationChannelM.getId();
                    if (id.startsWith(str)) {
                        int importance = notificationChannelM.getImportance();
                        if (importance != 4 && importance != 5 && !id.contains("_ia_")) {
                            if (id.contains("_channels_")) {
                                if (editorEdit == null) {
                                    editorEdit = getAccountInstance().getNotificationsSettings().edit();
                                }
                                editorEdit.remove("priority_channel").remove("vibrate_channel").remove("ChannelSoundPath").remove("ChannelSound");
                            } else if (id.contains("_reactions_")) {
                                if (editorEdit == null) {
                                    editorEdit = getAccountInstance().getNotificationsSettings().edit();
                                }
                                editorEdit.remove("priority_react").remove("vibrate_react").remove("ReactionSoundPath").remove("ReactionSound");
                            } else if (id.contains("_groups_")) {
                                if (editorEdit == null) {
                                    editorEdit = getAccountInstance().getNotificationsSettings().edit();
                                }
                                editorEdit.remove("priority_group").remove("vibrate_group").remove("GroupSoundPath").remove("GroupSound");
                            } else if (id.contains("_private_")) {
                                if (editorEdit == null) {
                                    editorEdit = getAccountInstance().getNotificationsSettings().edit();
                                }
                                editorEdit.remove("priority_messages");
                                editorEdit.remove("priority_group").remove("vibrate_messages").remove("GlobalSoundPath").remove("GlobalSound");
                            } else {
                                long jLongValue = Utilities.parseLong(id.substring(9, id.indexOf(95, 9))).longValue();
                                if (jLongValue != 0) {
                                    if (editorEdit == null) {
                                        editorEdit = getAccountInstance().getNotificationsSettings().edit();
                                    }
                                    editorEdit.remove("priority_" + jLongValue).remove("vibrate_" + jLongValue).remove("sound_path_" + jLongValue).remove("sound_" + jLongValue);
                                }
                            }
                        }
                        systemNotificationManager.deleteNotificationChannel(id);
                    }
                }
                if (editorEdit != null) {
                    editorEdit.apply();
                }
            } catch (Exception e) {
                FileLog.e(e);
            }
            notificationsSettings.edit().putBoolean("groupsCreated5", true).apply();
            this.groupsCreated = Boolean.TRUE;
        }
        if (this.channelGroupsCreated) {
            return;
        }
        List<NotificationChannelGroup> notificationChannelGroups = systemNotificationManager.getNotificationChannelGroups();
        String str2 = "channels" + this.currentAccount;
        String str3 = "groups" + this.currentAccount;
        String str4 = "private" + this.currentAccount;
        String str5 = "stories" + this.currentAccount;
        String str6 = "reactions" + this.currentAccount;
        String str7 = "other" + this.currentAccount;
        int size2 = notificationChannelGroups.size();
        String str8 = str7;
        String str9 = str6;
        String str10 = str5;
        String str11 = str4;
        for (int i2 = 0; i2 < size2; i2++) {
            String id2 = NotificationsController$$ExternalSyntheticApiModelOutline4.m(notificationChannelGroups.get(i2)).getId();
            if (str2 != null && str2.equals(id2)) {
                str2 = null;
            } else if (str3 != null && str3.equals(id2)) {
                str3 = null;
            } else if (str10 != null && str10.equals(id2)) {
                str10 = null;
            } else if (str9 != null && str9.equals(id2)) {
                str9 = null;
            } else if (str11 != null && str11.equals(id2)) {
                str11 = null;
            } else if (str8 != null && str8.equals(id2)) {
                str8 = null;
            }
            if (str2 == null && str10 == null && str9 == null && str3 == null && str11 == null && str8 == null) {
                break;
            }
        }
        if (str2 != null || str3 != null || str9 != null || str10 != null || str11 != null || str8 != null) {
            TLRPC.User user = getMessagesController().getUser(Long.valueOf(getUserConfig().getClientUserId()));
            if (user == null) {
                getUserConfig().getCurrentUser();
            }
            String str12 = user != null ? " (" + ContactsController.formatName(user.first_name, user.last_name) + ")" : _UrlKt.FRAGMENT_ENCODE_SET;
            ArrayList arrayList = new ArrayList();
            if (str2 != null) {
                NotificationsController$$ExternalSyntheticApiModelOutline2.m();
                arrayList.add(NotificationsController$$ExternalSyntheticApiModelOutline1.m(str2, LocaleController.getString(R.string.NotificationsChannels) + str12));
            }
            if (str3 != null) {
                NotificationsController$$ExternalSyntheticApiModelOutline2.m();
                arrayList.add(NotificationsController$$ExternalSyntheticApiModelOutline1.m(str3, LocaleController.getString(R.string.NotificationsGroups) + str12));
            }
            if (str10 != null) {
                NotificationsController$$ExternalSyntheticApiModelOutline2.m();
                arrayList.add(NotificationsController$$ExternalSyntheticApiModelOutline1.m(str10, LocaleController.getString(R.string.NotificationsStories) + str12));
            }
            if (str9 != null) {
                NotificationsController$$ExternalSyntheticApiModelOutline2.m();
                arrayList.add(NotificationsController$$ExternalSyntheticApiModelOutline1.m(str9, LocaleController.getString(R.string.NotificationsReactions) + str12));
            }
            if (str11 != null) {
                NotificationsController$$ExternalSyntheticApiModelOutline2.m();
                arrayList.add(NotificationsController$$ExternalSyntheticApiModelOutline1.m(str11, LocaleController.getString(R.string.NotificationsPrivateChats) + str12));
            }
            if (str8 != null) {
                NotificationsController$$ExternalSyntheticApiModelOutline2.m();
                arrayList.add(NotificationsController$$ExternalSyntheticApiModelOutline1.m(str8, LocaleController.getString(R.string.NotificationsOther) + str12));
            }
            systemNotificationManager.createNotificationChannelGroups(arrayList);
        }
        this.channelGroupsCreated = true;
    }

    /* JADX WARN: Code duplicated, block: B:166:0x0392  */
    /* JADX WARN: Code duplicated, block: B:167:0x039a A[DONT_INVERT] */
    /* JADX WARN: Code duplicated, block: B:168:0x039c  */
    /* JADX WARN: Code duplicated, block: B:169:0x03a3  */
    /* JADX WARN: Code duplicated, block: B:171:0x03a6  */
    /* JADX WARN: Code duplicated, block: B:172:0x03ad  */
    /* JADX WARN: Code duplicated, block: B:178:0x03bb  */
    /* JADX WARN: Code duplicated, block: B:268:0x05ac  */
    /* JADX WARN: Code duplicated, block: B:270:0x05b0 A[ADDED_TO_REGION] */
    /* JADX WARN: Code duplicated, block: B:274:0x05b7 A[PHI: r4 r12
  0x05b7: PHI (r4v22 java.lang.String) = 
  (r4v13 java.lang.String)
  (r4v13 java.lang.String)
  (r4v13 java.lang.String)
  (r4v9 java.lang.String)
  (r4v9 java.lang.String)
  (r4v9 java.lang.String)
 binds: [B:287:0x05e6, B:288:0x05e8, B:291:0x05f0, B:272:0x05b4, B:266:0x0595, B:267:0x0597] A[DONT_GENERATE, DONT_INLINE]
  0x05b7: PHI (r12v6 java.lang.String) = 
  (r12v4 java.lang.String)
  (r12v4 java.lang.String)
  (r12v4 java.lang.String)
  (r12v4 java.lang.String)
  (r12v7 java.lang.String)
  (r12v7 java.lang.String)
 binds: [B:287:0x05e6, B:288:0x05e8, B:291:0x05f0, B:272:0x05b4, B:266:0x0595, B:267:0x0597] A[DONT_GENERATE, DONT_INLINE]] */
    /* JADX WARN: Code duplicated, block: B:275:0x05ba  */
    /* JADX WARN: Code duplicated, block: B:278:0x05be A[LOOP:1: B:276:0x05bb->B:278:0x05be, LOOP_END] */
    /* JADX WARN: Code duplicated, block: B:281:0x05cb  */
    /* JADX WARN: Code duplicated, block: B:284:0x05d7 A[ADDED_TO_REGION] */
    /* JADX WARN: Code duplicated, block: B:288:0x05e8 A[ADDED_TO_REGION] */
    /* JADX WARN: Code duplicated, block: B:301:0x0618  */
    /* JADX WARN: Code duplicated, block: B:303:0x061c  */
    /* JADX WARN: Code duplicated, block: B:305:0x0640  */
    /* JADX WARN: Code duplicated, block: B:308:0x0668  */
    /* JADX WARN: Code duplicated, block: B:309:0x066f  */
    /* JADX WARN: Code duplicated, block: B:312:0x067c  */
    /* JADX WARN: Code duplicated, block: B:313:0x0684  */
    /* JADX WARN: Code duplicated, block: B:316:0x068e  */
    /* JADX WARN: Code duplicated, block: B:318:0x0694  */
    /* JADX WARN: Code duplicated, block: B:319:0x0698  */
    /* JADX WARN: Code duplicated, block: B:322:0x06aa  */
    /* JADX WARN: Code duplicated, block: B:323:0x06b4  */
    /* JADX WARN: Code duplicated, block: B:326:0x06c0  */
    /* JADX WARN: Instruction removed from duplicated block: B:303:0x061c, please report this as an issue */
    /* JADX WARN: Instruction removed from duplicated block: B:305:0x0640, please report this as an issue */
    /* JADX WARN: Instruction removed from duplicated block: B:326:0x06c0, please report this as an issue */
    /* JADX WARN: Multi-variable type inference failed */
    /* JADX WARN: Type inference failed for: r4v10 */
    /* JADX WARN: Type inference failed for: r4v11, types: [int] */
    /* JADX WARN: Type inference failed for: r4v70 */
    @TargetApi(26)
    private String validateChannelId(long j, long j2, String str, long[] jArr, int i, Uri uri, int i2, boolean z, boolean z2, boolean z3, int i3) {
        String str2;
        String str3;
        boolean z4;
        String string;
        SharedPreferences sharedPreferences;
        String str4;
        int i4;
        StringBuilder sb;
        boolean z5;
        boolean z6;
        String str5;
        String str6;
        String strMD5;
        String str7;
        ?? r4;
        String str8;
        String str9;
        String str10;
        String string2;
        NotificationChannel notificationChannelM;
        boolean z7;
        AudioAttributes.Builder builder;
        long j3;
        long[] jArr2;
        boolean z8;
        String str11;
        String str12;
        String str13;
        boolean z9;
        long[] jArr3;
        boolean z10;
        SharedPreferences.Editor editorEdit;
        int i5;
        boolean z11;
        boolean z12;
        boolean z13;
        ensureGroupsCreated();
        SharedPreferences notificationsSettings = getAccountInstance().getNotificationsSettings();
        String str14 = "reactions";
        String string3 = "groups";
        if (z3) {
            str2 = "other" + this.currentAccount;
            str3 = null;
        } else if (i3 == 2) {
            str2 = "channels" + this.currentAccount;
            str3 = "overwrite_channel";
        } else if (i3 == 0) {
            str2 = "groups" + this.currentAccount;
            str3 = "overwrite_group";
        } else if (i3 == 3) {
            str2 = "stories" + this.currentAccount;
            str3 = "overwrite_stories";
        } else if (i3 == 4 || i3 == 5) {
            str2 = "reactions" + this.currentAccount;
            str3 = "overwrite_reactions";
        } else {
            str2 = "private" + this.currentAccount;
            str3 = "overwrite_private";
        }
        boolean z14 = !z && DialogObject.isEncryptedDialog(j);
        boolean z15 = (z2 || str3 == null || !notificationsSettings.getBoolean(str3, false)) ? false : true;
        String strMD6 = Utilities.MD5(uri == null ? "NoSound2" : uri.toString());
        if (strMD6 != null && strMD6.length() > 5) {
            strMD6 = strMD6.substring(0, 5);
        }
        String str15 = "_";
        if (z3) {
            string3 = "silent";
            z4 = false;
            string = LocaleController.getString(R.string.NotificationsSilent);
        } else if (z) {
            string = LocaleController.getString(z2 ? R.string.NotificationsInAppDefault : R.string.NotificationsDefault);
            z4 = false;
            if (i3 == 2) {
                string3 = z2 ? "channels_ia" : "channels";
            } else if (i3 == 0) {
                if (z2) {
                    string3 = "groups_ia";
                }
            } else if (i3 == 3) {
                string3 = z2 ? "stories_ia" : "stories";
            } else if (i3 == 4 || i3 == 5) {
                if (z2) {
                    str14 = "reactions_ia";
                }
                string3 = str14;
            } else {
                string3 = z2 ? "private_ia" : "private";
            }
        } else {
            z4 = false;
            String string4 = z2 ? LocaleController.formatString(R.string.NotificationsChatInApp, str) : str;
            StringBuilder sb2 = new StringBuilder();
            sb2.append(z2 ? "org.telegram.keyia" : "org.telegram.key");
            sb2.append(j);
            sb2.append("_");
            sb2.append(j2);
            string3 = sb2.toString();
            string = string4;
        }
        String str16 = string3 + "_" + strMD6;
        String string5 = notificationsSettings.getString(str16, null);
        String string6 = notificationsSettings.getString(str16 + "_s", null);
        StringBuilder sb3 = new StringBuilder();
        String str17 = "_s";
        String str18 = "secret";
        if (string5 != null) {
            sharedPreferences = notificationsSettings;
            NotificationChannel notificationChannel = systemNotificationManager.getNotificationChannel(string5);
            str4 = string;
            if (BuildVars.LOGS_ENABLED) {
                FileLog.d("current channel for " + string5 + " = " + notificationChannel);
            }
            if (notificationChannel == null) {
                str17 = str17;
                jArr = jArr;
                str18 = "secret";
                str16 = str16;
                str15 = "_";
                str2 = str2;
                z4 = z4 ? 1 : 0;
                i4 = i;
                sb = sb3;
                z5 = z4 ? 1 : 0;
                str6 = null;
                strMD5 = null;
                str5 = null;
            } else if (!z3 && z15 == 0) {
                int importance = notificationChannel.getImportance();
                Uri sound = notificationChannel.getSound();
                long[] vibrationPattern = notificationChannel.getVibrationPattern();
                boolean zShouldVibrate = notificationChannel.shouldVibrate();
                str2 = str2;
                if (zShouldVibrate || vibrationPattern != null) {
                    j3 = 0;
                    jArr2 = vibrationPattern;
                } else {
                    j3 = 0;
                    jArr2 = new long[2];
                    jArr2[z4 ? 1 : 0] = 0;
                    jArr2[1] = 0;
                }
                int lightColor = notificationChannel.getLightColor();
                str16 = str16;
                str15 = "_";
                if (jArr2 != null) {
                    int i6 = z4 ? 1 : 0;
                    while (true) {
                        z8 = zShouldVibrate;
                        if (i6 >= jArr2.length) {
                            break;
                        }
                        sb3.append(jArr2[i6]);
                        i6++;
                        zShouldVibrate = z8;
                    }
                } else {
                    z8 = zShouldVibrate;
                }
                sb3.append(lightColor);
                if (sound != null) {
                    sb3.append(sound.toString());
                }
                sb3.append(importance);
                if (!z && z14) {
                    sb3.append("secret");
                }
                if (BuildVars.LOGS_ENABLED) {
                    FileLog.d("current channel settings for " + string5 + " = " + ((Object) sb3) + " old = " + string6);
                }
                String strMD7 = Utilities.MD5(sb3.toString());
                sb3.setLength(z4 ? 1 : 0 ? 1 : 0);
                if (z2 && i2 != importance) {
                    jArr3 = jArr;
                    i4 = i;
                    str11 = string6;
                    str18 = "secret";
                    str12 = string5;
                    z4 = false;
                    z9 = false;
                    str17 = str17;
                    sb = sb3;
                    str13 = strMD7;
                    z10 = true;
                } else {
                    if (strMD7.equals(string6)) {
                        i4 = i;
                        str11 = string6;
                        str18 = "secret";
                        str12 = string5;
                        z4 = false;
                        str17 = str17;
                        sb = sb3;
                        str13 = strMD7;
                        z9 = false;
                        jArr3 = jArr;
                    } else {
                        str11 = string6;
                        if (importance != 0) {
                            str12 = string5;
                            jArr3 = jArr2;
                            str17 = str17;
                            str13 = strMD7;
                            str18 = "secret";
                            sb = sb3;
                            if (importance != i2) {
                                if (z2) {
                                    z4 = false;
                                    editorEdit = null;
                                } else {
                                    SharedPreferences.Editor editorEdit2 = sharedPreferences.edit();
                                    if (importance == 4 || importance == 5) {
                                        i5 = 1;
                                    } else if (importance == 1) {
                                        i5 = 4;
                                    } else {
                                        i5 = importance == 2 ? 5 : 0;
                                    }
                                    if (z) {
                                        if (i3 == 3) {
                                            editorEdit2.putBoolean("EnableAllStories", true);
                                        } else {
                                            if (i3 == 4) {
                                                editorEdit2.putBoolean("EnableReactionsMessages", true);
                                                editorEdit2.putBoolean("EnableReactionsStories", true);
                                            } else {
                                                z4 = false;
                                                editorEdit2.putInt(getGlobalNotificationsKey(i3), 0);
                                            }
                                            if (i3 == 2) {
                                                editorEdit2.putInt("priority_channel", i5);
                                            } else if (i3 == 0) {
                                                editorEdit2.putInt("priority_group", i5);
                                            } else if (i3 == 3) {
                                                editorEdit2.putInt("priority_stories", i5);
                                            } else if (i3 != 4 || i3 == 5) {
                                                editorEdit2.putInt("priority_react", i5);
                                            } else {
                                                editorEdit2.putInt("priority_messages", i5);
                                            }
                                        }
                                        z4 = false;
                                        if (i3 == 2) {
                                            editorEdit2.putInt("priority_channel", i5);
                                        } else if (i3 == 0) {
                                            editorEdit2.putInt("priority_group", i5);
                                        } else if (i3 == 3) {
                                            editorEdit2.putInt("priority_stories", i5);
                                        } else if (i3 != 4) {
                                            editorEdit2.putInt("priority_react", i5);
                                        } else {
                                            editorEdit2.putInt("priority_react", i5);
                                        }
                                    } else {
                                        z4 = false;
                                        z4 = false;
                                        if (i3 == 3) {
                                            editorEdit2.putBoolean(NotificationsSettingsFacade.PROPERTY_STORIES_NOTIFY + j, true);
                                        } else {
                                            editorEdit2.putInt(NotificationsSettingsFacade.PROPERTY_NOTIFY + j, 0);
                                            editorEdit2.remove(NotificationsSettingsFacade.PROPERTY_NOTIFY_UNTIL + j);
                                            editorEdit2.putInt("priority_" + j, i5);
                                        }
                                    }
                                    editorEdit = editorEdit2;
                                }
                                z9 = true;
                            } else {
                                str18 = str18;
                                z4 = false;
                                z9 = false;
                                editorEdit = null;
                            }
                        } else {
                            SharedPreferences.Editor editorEdit3 = sharedPreferences.edit();
                            if (z) {
                                if (z2) {
                                    z12 = true;
                                } else {
                                    if (i3 == 3) {
                                        editorEdit3.putBoolean("EnableAllStories", false);
                                        z13 = true;
                                    } else if (i3 == 4) {
                                        z13 = true;
                                        editorEdit3.putBoolean("EnableReactionsMessages", true);
                                        editorEdit3.putBoolean("EnableReactionsStories", true);
                                    } else {
                                        z13 = true;
                                        editorEdit3.putInt(getGlobalNotificationsKey(i3), Integer.MAX_VALUE);
                                    }
                                    updateServerNotificationsSettings(i3);
                                    z12 = z13;
                                }
                                z11 = false;
                            } else {
                                if (i3 == 3) {
                                    editorEdit3.putBoolean(NotificationsSettingsFacade.PROPERTY_STORIES_NOTIFY + getSharedPrefKey(j, j3), false);
                                    z11 = false;
                                } else {
                                    z11 = false;
                                    editorEdit3.putInt(NotificationsSettingsFacade.PROPERTY_NOTIFY + getSharedPrefKey(j, j3), 2);
                                }
                                z12 = true;
                                str18 = "secret";
                                str17 = str17;
                                updateServerNotificationsSettings(j, 0L, true);
                            }
                            editorEdit = editorEdit3;
                            str13 = strMD7;
                            str18 = str18;
                            z9 = z12;
                            z4 = z11;
                            sb = sb3;
                            str12 = string5;
                            str17 = str17;
                            jArr3 = jArr2;
                        }
                        boolean z16 = z8;
                        if ((!isEmptyVibration(jArr)) != z16) {
                            if (!z2) {
                                if (editorEdit == null) {
                                    editorEdit = sharedPreferences.edit();
                                }
                                if (!z) {
                                    editorEdit.putInt("vibrate_" + j, z16 ? z4 ? 1 : 0 : 2);
                                } else if (i3 == 2) {
                                    editorEdit.putInt("vibrate_channel", z16 ? z4 ? 1 : 0 : 2);
                                } else if (i3 == 0) {
                                    editorEdit.putInt("vibrate_group", z16 ? z4 ? 1 : 0 : 2);
                                } else if (i3 == 3) {
                                    editorEdit.putInt("vibrate_stories", z16 ? z4 ? 1 : 0 : 2);
                                } else if (i3 == 4 || i3 == 5) {
                                    editorEdit.putInt("vibrate_react", z16 ? z4 ? 1 : 0 : 2);
                                } else {
                                    editorEdit.putInt("vibrate_messages", z16 ? z4 ? 1 : 0 : 2);
                                }
                            }
                            z9 = true;
                        } else {
                            jArr3 = jArr;
                        }
                        i4 = i;
                        if (lightColor != i4) {
                            if (!z2) {
                                if (editorEdit == null) {
                                    editorEdit = sharedPreferences.edit();
                                }
                                if (!z) {
                                    editorEdit.putInt("color_" + j, lightColor);
                                } else if (i3 == 2) {
                                    editorEdit.putInt("ChannelLed", lightColor);
                                } else if (i3 == 0) {
                                    editorEdit.putInt("GroupLed", lightColor);
                                } else if (i3 == 3) {
                                    editorEdit.putInt("StoriesLed", lightColor);
                                } else if (i3 == 5 || i3 == 4) {
                                    editorEdit.putInt("ReactionsLed", lightColor);
                                } else {
                                    editorEdit.putInt("MessagesLed", lightColor);
                                }
                            }
                            i4 = lightColor;
                            z9 = true;
                        }
                        if (editorEdit != null) {
                            editorEdit.apply();
                        }
                    }
                    z10 = z15;
                }
                z6 = z10;
                z5 = z9;
                str5 = str12;
                jArr = jArr3;
                str6 = str11;
                strMD5 = str13;
            }
            if (z5 || strMD5 == null) {
                z6 = z15;
                str7 = str16;
                if (!z6 || strMD5 == null || !z2 || !z) {
                    for (r4 = z4; r4 < jArr.length; r4++) {
                        sb.append(jArr[r4]);
                    }
                    sb.append(i4);
                    if (uri != null) {
                        sb.append(uri.toString());
                    }
                    sb.append(i2);
                    if (!z && z14) {
                        sb.append(str18);
                    }
                    strMD5 = Utilities.MD5(sb.toString());
                    if (z3 && str5 != null && (z6 || !str6.equals(strMD5))) {
                        try {
                            systemNotificationManager.deleteNotificationChannel(str5);
                        } catch (Exception e) {
                            FileLog.e(e);
                        }
                        if (BuildVars.LOGS_ENABLED) {
                            FileLog.d("delete channel by settings change " + str5);
                        }
                        str8 = strMD5;
                        str9 = null;
                    }
                }
                if (str9 == null) {
                    if (z) {
                        str10 = this.currentAccount + "channel_" + str7 + str15 + Utilities.random.nextLong();
                    } else {
                        str10 = this.currentAccount + "channel_" + j + str15 + Utilities.random.nextLong();
                    }
                    str9 = str10;
                    NotificationsController$$ExternalSyntheticApiModelOutline0.m();
                    if (z14) {
                        string2 = LocaleController.getString(R.string.SecretChatName);
                    } else {
                        string2 = str4;
                    }
                    notificationChannelM = zzo$$ExternalSyntheticApiModelOutline0.m(str9, string2, i2);
                    notificationChannelM.setGroup(str2);
                    if (i4 != 0) {
                        z7 = true;
                        notificationChannelM.enableLights(true);
                        notificationChannelM.setLightColor(i4);
                    } else {
                        z7 = true;
                        notificationChannelM.enableLights(z4);
                    }
                    if (!isEmptyVibration(jArr)) {
                        notificationChannelM.enableVibration(z7);
                        if (jArr.length > 0) {
                            notificationChannelM.setVibrationPattern(jArr);
                        }
                    } else {
                        notificationChannelM.enableVibration(z4);
                    }
                    builder = new AudioAttributes.Builder();
                    builder.setContentType(4);
                    builder.setUsage(5);
                    if (uri != null) {
                        notificationChannelM.setSound(uri, builder.build());
                    } else {
                        notificationChannelM.setSound(null, builder.build());
                    }
                    if (BuildVars.LOGS_ENABLED) {
                        FileLog.d("create new channel " + str9);
                    }
                    this.lastNotificationChannelCreateTime = SystemClock.elapsedRealtime();
                    systemNotificationManager.createNotificationChannel(notificationChannelM);
                    sharedPreferences.edit().putString(str7, str9).putString(str7 + str17, str8).apply();
                }
                return str9;
            }
            str7 = str16;
            sharedPreferences.edit().putString(str7, str5).putString(str7 + str17, strMD5).apply();
            if (BuildVars.LOGS_ENABLED) {
                FileLog.d("change edited channel " + str5);
            }
            str8 = strMD5;
            str9 = str5;
            if (str9 == null) {
                if (z) {
                    str10 = this.currentAccount + "channel_" + str7 + str15 + Utilities.random.nextLong();
                } else {
                    str10 = this.currentAccount + "channel_" + j + str15 + Utilities.random.nextLong();
                }
                str9 = str10;
                NotificationsController$$ExternalSyntheticApiModelOutline0.m();
                if (z14) {
                    string2 = LocaleController.getString(R.string.SecretChatName);
                } else {
                    string2 = str4;
                }
                notificationChannelM = zzo$$ExternalSyntheticApiModelOutline0.m(str9, string2, i2);
                notificationChannelM.setGroup(str2);
                if (i4 != 0) {
                    z7 = true;
                    notificationChannelM.enableLights(true);
                    notificationChannelM.setLightColor(i4);
                } else {
                    z7 = true;
                    notificationChannelM.enableLights(z4);
                }
                if (!isEmptyVibration(jArr)) {
                    notificationChannelM.enableVibration(z7);
                    if (jArr.length > 0) {
                        notificationChannelM.setVibrationPattern(jArr);
                    }
                } else {
                    notificationChannelM.enableVibration(z4);
                }
                builder = new AudioAttributes.Builder();
                builder.setContentType(4);
                builder.setUsage(5);
                if (uri != null) {
                    notificationChannelM.setSound(uri, builder.build());
                } else {
                    notificationChannelM.setSound(null, builder.build());
                }
                if (BuildVars.LOGS_ENABLED) {
                    FileLog.d("create new channel " + str9);
                }
                this.lastNotificationChannelCreateTime = SystemClock.elapsedRealtime();
                systemNotificationManager.createNotificationChannel(notificationChannelM);
                sharedPreferences.edit().putString(str7, str9).putString(str7 + str17, str8).apply();
            }
            return str9;
        }
        sharedPreferences = notificationsSettings;
        str4 = string;
        z15 = z15;
        i4 = i;
        sb = sb3;
        z5 = z4;
        z6 = z15;
        str5 = string5;
        str6 = string6;
        strMD5 = null;
        if (z5) {
            z6 = z15;
            str7 = str16;
            if (!z6) {
                while (r4 < jArr.length) {
                    sb.append(jArr[r4]);
                }
                sb.append(i4);
                if (uri != null) {
                    sb.append(uri.toString());
                }
                sb.append(i2);
                if (!z) {
                    sb.append(str18);
                }
                strMD5 = Utilities.MD5(sb.toString());
                if (z3) {
                    str8 = strMD5;
                    str9 = str5;
                } else {
                    str8 = strMD5;
                    str9 = str5;
                }
            } else {
                while (r4 < jArr.length) {
                    sb.append(jArr[r4]);
                }
                sb.append(i4);
                if (uri != null) {
                    sb.append(uri.toString());
                }
                sb.append(i2);
                if (!z) {
                    sb.append(str18);
                }
                strMD5 = Utilities.MD5(sb.toString());
                if (z3) {
                    str8 = strMD5;
                    str9 = str5;
                } else {
                    str8 = strMD5;
                    str9 = str5;
                }
            }
        } else {
            z6 = z15;
            str7 = str16;
            if (!z6) {
                while (r4 < jArr.length) {
                    sb.append(jArr[r4]);
                }
                sb.append(i4);
                if (uri != null) {
                    sb.append(uri.toString());
                }
                sb.append(i2);
                if (!z) {
                    sb.append(str18);
                }
                strMD5 = Utilities.MD5(sb.toString());
                if (z3) {
                    str8 = strMD5;
                    str9 = str5;
                } else {
                    str8 = strMD5;
                    str9 = str5;
                }
            } else {
                while (r4 < jArr.length) {
                    sb.append(jArr[r4]);
                }
                sb.append(i4);
                if (uri != null) {
                    sb.append(uri.toString());
                }
                sb.append(i2);
                if (!z) {
                    sb.append(str18);
                }
                strMD5 = Utilities.MD5(sb.toString());
                if (z3) {
                    str8 = strMD5;
                    str9 = str5;
                } else {
                    str8 = strMD5;
                    str9 = str5;
                }
            }
        }
        if (str9 == null) {
            if (z) {
                str10 = this.currentAccount + "channel_" + str7 + str15 + Utilities.random.nextLong();
            } else {
                str10 = this.currentAccount + "channel_" + j + str15 + Utilities.random.nextLong();
            }
            str9 = str10;
            NotificationsController$$ExternalSyntheticApiModelOutline0.m();
            if (z14) {
                string2 = LocaleController.getString(R.string.SecretChatName);
            } else {
                string2 = str4;
            }
            notificationChannelM = zzo$$ExternalSyntheticApiModelOutline0.m(str9, string2, i2);
            notificationChannelM.setGroup(str2);
            if (i4 != 0) {
                z7 = true;
                notificationChannelM.enableLights(true);
                notificationChannelM.setLightColor(i4);
            } else {
                z7 = true;
                notificationChannelM.enableLights(z4);
            }
            if (!isEmptyVibration(jArr)) {
                notificationChannelM.enableVibration(z7);
                if (jArr.length > 0) {
                    notificationChannelM.setVibrationPattern(jArr);
                }
            } else {
                notificationChannelM.enableVibration(z4);
            }
            builder = new AudioAttributes.Builder();
            builder.setContentType(4);
            builder.setUsage(5);
            if (uri != null) {
                notificationChannelM.setSound(uri, builder.build());
            } else {
                notificationChannelM.setSound(null, builder.build());
            }
            if (BuildVars.LOGS_ENABLED) {
                FileLog.d("create new channel " + str9);
            }
            this.lastNotificationChannelCreateTime = SystemClock.elapsedRealtime();
            systemNotificationManager.createNotificationChannel(notificationChannelM);
            sharedPreferences.edit().putString(str7, str9).putString(str7 + str17, str8).apply();
        }
        return str9;
    }

    /* JADX WARN: Code duplicated, block: B:166:0x03d0 A[Catch: Exception -> 0x0056, TryCatch #2 {Exception -> 0x0056, blocks: (B:13:0x002c, B:14:0x0038, B:16:0x0040, B:18:0x0051, B:19:0x0053, B:23:0x005a, B:25:0x0062, B:27:0x0074, B:28:0x0077, B:32:0x0080, B:35:0x0088, B:36:0x009e, B:38:0x00a6, B:39:0x00dc, B:41:0x00fe, B:44:0x0106, B:46:0x010e, B:49:0x0115, B:53:0x0129, B:71:0x01ee, B:73:0x0220, B:75:0x0232, B:78:0x023a, B:80:0x023e, B:82:0x025a, B:84:0x0261, B:88:0x0274, B:92:0x0280, B:94:0x028c, B:95:0x0292, B:97:0x029d, B:99:0x02a3, B:101:0x02af, B:102:0x02bb, B:103:0x02c5, B:105:0x02d5, B:107:0x02e5, B:109:0x02eb, B:118:0x0321, B:123:0x033e, B:133:0x0361, B:135:0x0367, B:139:0x0375, B:141:0x037b, B:147:0x0386, B:150:0x0399, B:164:0x03cc, B:166:0x03d0, B:174:0x03eb, B:176:0x03f2, B:178:0x03fa, B:181:0x0428, B:190:0x049f, B:196:0x04c2, B:198:0x04e6, B:200:0x04fc, B:202:0x0500, B:207:0x050c, B:208:0x0512, B:212:0x051f, B:218:0x0567, B:219:0x056a, B:213:0x0535, B:215:0x053d, B:216:0x0551, B:220:0x0575, B:240:0x05f5, B:251:0x060e, B:253:0x062c, B:256:0x065d, B:258:0x0667, B:260:0x067d, B:262:0x068e, B:270:0x06b9, B:274:0x06dc, B:276:0x06fb, B:278:0x0726, B:280:0x0742, B:282:0x0752, B:286:0x0761, B:288:0x0767, B:293:0x0777, B:295:0x0789, B:297:0x079c, B:334:0x08c8, B:336:0x08ce, B:345:0x08e7, B:347:0x08ed, B:355:0x0900, B:358:0x090a, B:362:0x0915, B:376:0x0936, B:380:0x0946, B:382:0x0976, B:383:0x097e, B:386:0x0985, B:451:0x0a9b, B:453:0x0ae6, B:454:0x0aed, B:457:0x0af7, B:459:0x0afb, B:461:0x0b01, B:479:0x0b5f, B:502:0x0bbf, B:531:0x0c05, B:540:0x0c44, B:542:0x0c4c, B:545:0x0c54, B:547:0x0c5c, B:551:0x0c67, B:568:0x0cfb, B:572:0x0d0b, B:587:0x0d50, B:589:0x0d56, B:591:0x0d5a, B:593:0x0d65, B:595:0x0d6b, B:597:0x0d75, B:599:0x0d86, B:601:0x0d92, B:603:0x0db2, B:605:0x0dbc, B:607:0x0de8, B:608:0x0df5, B:612:0x0e14, B:614:0x0e1a, B:616:0x0e22, B:618:0x0e28, B:619:0x0e4a, B:575:0x0d1a, B:583:0x0d2f, B:585:0x0d3b, B:552:0x0c8d, B:553:0x0c92, B:554:0x0c95, B:556:0x0c9d, B:558:0x0ca6, B:560:0x0cae, B:564:0x0ce7, B:565:0x0cf0, B:534:0x0c0f, B:536:0x0c17, B:538:0x0c3f, B:586:0x0d42, B:512:0x0bd3, B:517:0x0be0, B:520:0x0bea, B:523:0x0bf3, B:482:0x0b68, B:484:0x0b75, B:477:0x0b5a, B:388:0x099a, B:393:0x09aa, B:397:0x09bc, B:396:0x09b7, B:398:0x09cc, B:400:0x09da, B:401:0x09e3, B:403:0x09eb, B:404:0x09fa, B:405:0x0a03, B:407:0x0a09, B:410:0x0a16, B:413:0x0a20, B:414:0x0a23, B:416:0x0a29, B:419:0x0a32, B:421:0x0a3b, B:424:0x0a43, B:426:0x0a49, B:428:0x0a4d, B:430:0x0a55, B:436:0x0a63, B:438:0x0a69, B:440:0x0a6d, B:442:0x0a75, B:446:0x0a7c, B:448:0x0a8b, B:450:0x0a91, B:296:0x0795, B:299:0x07c4, B:301:0x07d6, B:303:0x07e9, B:302:0x07e2, B:309:0x081d, B:311:0x0825, B:316:0x083d, B:315:0x0838, B:322:0x0879, B:324:0x0885, B:326:0x0898, B:325:0x0891, B:279:0x0733, B:263:0x069a, B:265:0x069e, B:222:0x0584, B:228:0x059a, B:234:0x05dd, B:237:0x05e3, B:229:0x05ae, B:231:0x05b4, B:232:0x05c8, B:184:0x0438, B:187:0x0444, B:188:0x045f, B:179:0x0407, B:170:0x03d8, B:172:0x03e3, B:160:0x03b6, B:161:0x03bd, B:162:0x03c4, B:137:0x036c, B:138:0x0371, B:111:0x0301, B:113:0x0307, B:87:0x0271, B:54:0x0137, B:56:0x013d, B:57:0x0143, B:60:0x014d, B:61:0x0157, B:62:0x0169, B:64:0x016f, B:65:0x0186, B:67:0x018d, B:69:0x0195, B:70:0x01c5, B:51:0x011e, B:72:0x020e, B:562:0x0cb8, B:370:0x0928), top: B:630:0x002c, inners: #1, #3 }] */
    /* JADX WARN: Code duplicated, block: B:170:0x03d8 A[Catch: Exception -> 0x0056, TryCatch #2 {Exception -> 0x0056, blocks: (B:13:0x002c, B:14:0x0038, B:16:0x0040, B:18:0x0051, B:19:0x0053, B:23:0x005a, B:25:0x0062, B:27:0x0074, B:28:0x0077, B:32:0x0080, B:35:0x0088, B:36:0x009e, B:38:0x00a6, B:39:0x00dc, B:41:0x00fe, B:44:0x0106, B:46:0x010e, B:49:0x0115, B:53:0x0129, B:71:0x01ee, B:73:0x0220, B:75:0x0232, B:78:0x023a, B:80:0x023e, B:82:0x025a, B:84:0x0261, B:88:0x0274, B:92:0x0280, B:94:0x028c, B:95:0x0292, B:97:0x029d, B:99:0x02a3, B:101:0x02af, B:102:0x02bb, B:103:0x02c5, B:105:0x02d5, B:107:0x02e5, B:109:0x02eb, B:118:0x0321, B:123:0x033e, B:133:0x0361, B:135:0x0367, B:139:0x0375, B:141:0x037b, B:147:0x0386, B:150:0x0399, B:164:0x03cc, B:166:0x03d0, B:174:0x03eb, B:176:0x03f2, B:178:0x03fa, B:181:0x0428, B:190:0x049f, B:196:0x04c2, B:198:0x04e6, B:200:0x04fc, B:202:0x0500, B:207:0x050c, B:208:0x0512, B:212:0x051f, B:218:0x0567, B:219:0x056a, B:213:0x0535, B:215:0x053d, B:216:0x0551, B:220:0x0575, B:240:0x05f5, B:251:0x060e, B:253:0x062c, B:256:0x065d, B:258:0x0667, B:260:0x067d, B:262:0x068e, B:270:0x06b9, B:274:0x06dc, B:276:0x06fb, B:278:0x0726, B:280:0x0742, B:282:0x0752, B:286:0x0761, B:288:0x0767, B:293:0x0777, B:295:0x0789, B:297:0x079c, B:334:0x08c8, B:336:0x08ce, B:345:0x08e7, B:347:0x08ed, B:355:0x0900, B:358:0x090a, B:362:0x0915, B:376:0x0936, B:380:0x0946, B:382:0x0976, B:383:0x097e, B:386:0x0985, B:451:0x0a9b, B:453:0x0ae6, B:454:0x0aed, B:457:0x0af7, B:459:0x0afb, B:461:0x0b01, B:479:0x0b5f, B:502:0x0bbf, B:531:0x0c05, B:540:0x0c44, B:542:0x0c4c, B:545:0x0c54, B:547:0x0c5c, B:551:0x0c67, B:568:0x0cfb, B:572:0x0d0b, B:587:0x0d50, B:589:0x0d56, B:591:0x0d5a, B:593:0x0d65, B:595:0x0d6b, B:597:0x0d75, B:599:0x0d86, B:601:0x0d92, B:603:0x0db2, B:605:0x0dbc, B:607:0x0de8, B:608:0x0df5, B:612:0x0e14, B:614:0x0e1a, B:616:0x0e22, B:618:0x0e28, B:619:0x0e4a, B:575:0x0d1a, B:583:0x0d2f, B:585:0x0d3b, B:552:0x0c8d, B:553:0x0c92, B:554:0x0c95, B:556:0x0c9d, B:558:0x0ca6, B:560:0x0cae, B:564:0x0ce7, B:565:0x0cf0, B:534:0x0c0f, B:536:0x0c17, B:538:0x0c3f, B:586:0x0d42, B:512:0x0bd3, B:517:0x0be0, B:520:0x0bea, B:523:0x0bf3, B:482:0x0b68, B:484:0x0b75, B:477:0x0b5a, B:388:0x099a, B:393:0x09aa, B:397:0x09bc, B:396:0x09b7, B:398:0x09cc, B:400:0x09da, B:401:0x09e3, B:403:0x09eb, B:404:0x09fa, B:405:0x0a03, B:407:0x0a09, B:410:0x0a16, B:413:0x0a20, B:414:0x0a23, B:416:0x0a29, B:419:0x0a32, B:421:0x0a3b, B:424:0x0a43, B:426:0x0a49, B:428:0x0a4d, B:430:0x0a55, B:436:0x0a63, B:438:0x0a69, B:440:0x0a6d, B:442:0x0a75, B:446:0x0a7c, B:448:0x0a8b, B:450:0x0a91, B:296:0x0795, B:299:0x07c4, B:301:0x07d6, B:303:0x07e9, B:302:0x07e2, B:309:0x081d, B:311:0x0825, B:316:0x083d, B:315:0x0838, B:322:0x0879, B:324:0x0885, B:326:0x0898, B:325:0x0891, B:279:0x0733, B:263:0x069a, B:265:0x069e, B:222:0x0584, B:228:0x059a, B:234:0x05dd, B:237:0x05e3, B:229:0x05ae, B:231:0x05b4, B:232:0x05c8, B:184:0x0438, B:187:0x0444, B:188:0x045f, B:179:0x0407, B:170:0x03d8, B:172:0x03e3, B:160:0x03b6, B:161:0x03bd, B:162:0x03c4, B:137:0x036c, B:138:0x0371, B:111:0x0301, B:113:0x0307, B:87:0x0271, B:54:0x0137, B:56:0x013d, B:57:0x0143, B:60:0x014d, B:61:0x0157, B:62:0x0169, B:64:0x016f, B:65:0x0186, B:67:0x018d, B:69:0x0195, B:70:0x01c5, B:51:0x011e, B:72:0x020e, B:562:0x0cb8, B:370:0x0928), top: B:630:0x002c, inners: #1, #3 }] */
    /* JADX WARN: Code duplicated, block: B:172:0x03e3 A[Catch: Exception -> 0x0056, TryCatch #2 {Exception -> 0x0056, blocks: (B:13:0x002c, B:14:0x0038, B:16:0x0040, B:18:0x0051, B:19:0x0053, B:23:0x005a, B:25:0x0062, B:27:0x0074, B:28:0x0077, B:32:0x0080, B:35:0x0088, B:36:0x009e, B:38:0x00a6, B:39:0x00dc, B:41:0x00fe, B:44:0x0106, B:46:0x010e, B:49:0x0115, B:53:0x0129, B:71:0x01ee, B:73:0x0220, B:75:0x0232, B:78:0x023a, B:80:0x023e, B:82:0x025a, B:84:0x0261, B:88:0x0274, B:92:0x0280, B:94:0x028c, B:95:0x0292, B:97:0x029d, B:99:0x02a3, B:101:0x02af, B:102:0x02bb, B:103:0x02c5, B:105:0x02d5, B:107:0x02e5, B:109:0x02eb, B:118:0x0321, B:123:0x033e, B:133:0x0361, B:135:0x0367, B:139:0x0375, B:141:0x037b, B:147:0x0386, B:150:0x0399, B:164:0x03cc, B:166:0x03d0, B:174:0x03eb, B:176:0x03f2, B:178:0x03fa, B:181:0x0428, B:190:0x049f, B:196:0x04c2, B:198:0x04e6, B:200:0x04fc, B:202:0x0500, B:207:0x050c, B:208:0x0512, B:212:0x051f, B:218:0x0567, B:219:0x056a, B:213:0x0535, B:215:0x053d, B:216:0x0551, B:220:0x0575, B:240:0x05f5, B:251:0x060e, B:253:0x062c, B:256:0x065d, B:258:0x0667, B:260:0x067d, B:262:0x068e, B:270:0x06b9, B:274:0x06dc, B:276:0x06fb, B:278:0x0726, B:280:0x0742, B:282:0x0752, B:286:0x0761, B:288:0x0767, B:293:0x0777, B:295:0x0789, B:297:0x079c, B:334:0x08c8, B:336:0x08ce, B:345:0x08e7, B:347:0x08ed, B:355:0x0900, B:358:0x090a, B:362:0x0915, B:376:0x0936, B:380:0x0946, B:382:0x0976, B:383:0x097e, B:386:0x0985, B:451:0x0a9b, B:453:0x0ae6, B:454:0x0aed, B:457:0x0af7, B:459:0x0afb, B:461:0x0b01, B:479:0x0b5f, B:502:0x0bbf, B:531:0x0c05, B:540:0x0c44, B:542:0x0c4c, B:545:0x0c54, B:547:0x0c5c, B:551:0x0c67, B:568:0x0cfb, B:572:0x0d0b, B:587:0x0d50, B:589:0x0d56, B:591:0x0d5a, B:593:0x0d65, B:595:0x0d6b, B:597:0x0d75, B:599:0x0d86, B:601:0x0d92, B:603:0x0db2, B:605:0x0dbc, B:607:0x0de8, B:608:0x0df5, B:612:0x0e14, B:614:0x0e1a, B:616:0x0e22, B:618:0x0e28, B:619:0x0e4a, B:575:0x0d1a, B:583:0x0d2f, B:585:0x0d3b, B:552:0x0c8d, B:553:0x0c92, B:554:0x0c95, B:556:0x0c9d, B:558:0x0ca6, B:560:0x0cae, B:564:0x0ce7, B:565:0x0cf0, B:534:0x0c0f, B:536:0x0c17, B:538:0x0c3f, B:586:0x0d42, B:512:0x0bd3, B:517:0x0be0, B:520:0x0bea, B:523:0x0bf3, B:482:0x0b68, B:484:0x0b75, B:477:0x0b5a, B:388:0x099a, B:393:0x09aa, B:397:0x09bc, B:396:0x09b7, B:398:0x09cc, B:400:0x09da, B:401:0x09e3, B:403:0x09eb, B:404:0x09fa, B:405:0x0a03, B:407:0x0a09, B:410:0x0a16, B:413:0x0a20, B:414:0x0a23, B:416:0x0a29, B:419:0x0a32, B:421:0x0a3b, B:424:0x0a43, B:426:0x0a49, B:428:0x0a4d, B:430:0x0a55, B:436:0x0a63, B:438:0x0a69, B:440:0x0a6d, B:442:0x0a75, B:446:0x0a7c, B:448:0x0a8b, B:450:0x0a91, B:296:0x0795, B:299:0x07c4, B:301:0x07d6, B:303:0x07e9, B:302:0x07e2, B:309:0x081d, B:311:0x0825, B:316:0x083d, B:315:0x0838, B:322:0x0879, B:324:0x0885, B:326:0x0898, B:325:0x0891, B:279:0x0733, B:263:0x069a, B:265:0x069e, B:222:0x0584, B:228:0x059a, B:234:0x05dd, B:237:0x05e3, B:229:0x05ae, B:231:0x05b4, B:232:0x05c8, B:184:0x0438, B:187:0x0444, B:188:0x045f, B:179:0x0407, B:170:0x03d8, B:172:0x03e3, B:160:0x03b6, B:161:0x03bd, B:162:0x03c4, B:137:0x036c, B:138:0x0371, B:111:0x0301, B:113:0x0307, B:87:0x0271, B:54:0x0137, B:56:0x013d, B:57:0x0143, B:60:0x014d, B:61:0x0157, B:62:0x0169, B:64:0x016f, B:65:0x0186, B:67:0x018d, B:69:0x0195, B:70:0x01c5, B:51:0x011e, B:72:0x020e, B:562:0x0cb8, B:370:0x0928), top: B:630:0x002c, inners: #1, #3 }] */
    /* JADX WARN: Code duplicated, block: B:174:0x03eb A[Catch: Exception -> 0x0056, TryCatch #2 {Exception -> 0x0056, blocks: (B:13:0x002c, B:14:0x0038, B:16:0x0040, B:18:0x0051, B:19:0x0053, B:23:0x005a, B:25:0x0062, B:27:0x0074, B:28:0x0077, B:32:0x0080, B:35:0x0088, B:36:0x009e, B:38:0x00a6, B:39:0x00dc, B:41:0x00fe, B:44:0x0106, B:46:0x010e, B:49:0x0115, B:53:0x0129, B:71:0x01ee, B:73:0x0220, B:75:0x0232, B:78:0x023a, B:80:0x023e, B:82:0x025a, B:84:0x0261, B:88:0x0274, B:92:0x0280, B:94:0x028c, B:95:0x0292, B:97:0x029d, B:99:0x02a3, B:101:0x02af, B:102:0x02bb, B:103:0x02c5, B:105:0x02d5, B:107:0x02e5, B:109:0x02eb, B:118:0x0321, B:123:0x033e, B:133:0x0361, B:135:0x0367, B:139:0x0375, B:141:0x037b, B:147:0x0386, B:150:0x0399, B:164:0x03cc, B:166:0x03d0, B:174:0x03eb, B:176:0x03f2, B:178:0x03fa, B:181:0x0428, B:190:0x049f, B:196:0x04c2, B:198:0x04e6, B:200:0x04fc, B:202:0x0500, B:207:0x050c, B:208:0x0512, B:212:0x051f, B:218:0x0567, B:219:0x056a, B:213:0x0535, B:215:0x053d, B:216:0x0551, B:220:0x0575, B:240:0x05f5, B:251:0x060e, B:253:0x062c, B:256:0x065d, B:258:0x0667, B:260:0x067d, B:262:0x068e, B:270:0x06b9, B:274:0x06dc, B:276:0x06fb, B:278:0x0726, B:280:0x0742, B:282:0x0752, B:286:0x0761, B:288:0x0767, B:293:0x0777, B:295:0x0789, B:297:0x079c, B:334:0x08c8, B:336:0x08ce, B:345:0x08e7, B:347:0x08ed, B:355:0x0900, B:358:0x090a, B:362:0x0915, B:376:0x0936, B:380:0x0946, B:382:0x0976, B:383:0x097e, B:386:0x0985, B:451:0x0a9b, B:453:0x0ae6, B:454:0x0aed, B:457:0x0af7, B:459:0x0afb, B:461:0x0b01, B:479:0x0b5f, B:502:0x0bbf, B:531:0x0c05, B:540:0x0c44, B:542:0x0c4c, B:545:0x0c54, B:547:0x0c5c, B:551:0x0c67, B:568:0x0cfb, B:572:0x0d0b, B:587:0x0d50, B:589:0x0d56, B:591:0x0d5a, B:593:0x0d65, B:595:0x0d6b, B:597:0x0d75, B:599:0x0d86, B:601:0x0d92, B:603:0x0db2, B:605:0x0dbc, B:607:0x0de8, B:608:0x0df5, B:612:0x0e14, B:614:0x0e1a, B:616:0x0e22, B:618:0x0e28, B:619:0x0e4a, B:575:0x0d1a, B:583:0x0d2f, B:585:0x0d3b, B:552:0x0c8d, B:553:0x0c92, B:554:0x0c95, B:556:0x0c9d, B:558:0x0ca6, B:560:0x0cae, B:564:0x0ce7, B:565:0x0cf0, B:534:0x0c0f, B:536:0x0c17, B:538:0x0c3f, B:586:0x0d42, B:512:0x0bd3, B:517:0x0be0, B:520:0x0bea, B:523:0x0bf3, B:482:0x0b68, B:484:0x0b75, B:477:0x0b5a, B:388:0x099a, B:393:0x09aa, B:397:0x09bc, B:396:0x09b7, B:398:0x09cc, B:400:0x09da, B:401:0x09e3, B:403:0x09eb, B:404:0x09fa, B:405:0x0a03, B:407:0x0a09, B:410:0x0a16, B:413:0x0a20, B:414:0x0a23, B:416:0x0a29, B:419:0x0a32, B:421:0x0a3b, B:424:0x0a43, B:426:0x0a49, B:428:0x0a4d, B:430:0x0a55, B:436:0x0a63, B:438:0x0a69, B:440:0x0a6d, B:442:0x0a75, B:446:0x0a7c, B:448:0x0a8b, B:450:0x0a91, B:296:0x0795, B:299:0x07c4, B:301:0x07d6, B:303:0x07e9, B:302:0x07e2, B:309:0x081d, B:311:0x0825, B:316:0x083d, B:315:0x0838, B:322:0x0879, B:324:0x0885, B:326:0x0898, B:325:0x0891, B:279:0x0733, B:263:0x069a, B:265:0x069e, B:222:0x0584, B:228:0x059a, B:234:0x05dd, B:237:0x05e3, B:229:0x05ae, B:231:0x05b4, B:232:0x05c8, B:184:0x0438, B:187:0x0444, B:188:0x045f, B:179:0x0407, B:170:0x03d8, B:172:0x03e3, B:160:0x03b6, B:161:0x03bd, B:162:0x03c4, B:137:0x036c, B:138:0x0371, B:111:0x0301, B:113:0x0307, B:87:0x0271, B:54:0x0137, B:56:0x013d, B:57:0x0143, B:60:0x014d, B:61:0x0157, B:62:0x0169, B:64:0x016f, B:65:0x0186, B:67:0x018d, B:69:0x0195, B:70:0x01c5, B:51:0x011e, B:72:0x020e, B:562:0x0cb8, B:370:0x0928), top: B:630:0x002c, inners: #1, #3 }] */
    /* JADX WARN: Code duplicated, block: B:176:0x03f2 A[Catch: Exception -> 0x0056, TryCatch #2 {Exception -> 0x0056, blocks: (B:13:0x002c, B:14:0x0038, B:16:0x0040, B:18:0x0051, B:19:0x0053, B:23:0x005a, B:25:0x0062, B:27:0x0074, B:28:0x0077, B:32:0x0080, B:35:0x0088, B:36:0x009e, B:38:0x00a6, B:39:0x00dc, B:41:0x00fe, B:44:0x0106, B:46:0x010e, B:49:0x0115, B:53:0x0129, B:71:0x01ee, B:73:0x0220, B:75:0x0232, B:78:0x023a, B:80:0x023e, B:82:0x025a, B:84:0x0261, B:88:0x0274, B:92:0x0280, B:94:0x028c, B:95:0x0292, B:97:0x029d, B:99:0x02a3, B:101:0x02af, B:102:0x02bb, B:103:0x02c5, B:105:0x02d5, B:107:0x02e5, B:109:0x02eb, B:118:0x0321, B:123:0x033e, B:133:0x0361, B:135:0x0367, B:139:0x0375, B:141:0x037b, B:147:0x0386, B:150:0x0399, B:164:0x03cc, B:166:0x03d0, B:174:0x03eb, B:176:0x03f2, B:178:0x03fa, B:181:0x0428, B:190:0x049f, B:196:0x04c2, B:198:0x04e6, B:200:0x04fc, B:202:0x0500, B:207:0x050c, B:208:0x0512, B:212:0x051f, B:218:0x0567, B:219:0x056a, B:213:0x0535, B:215:0x053d, B:216:0x0551, B:220:0x0575, B:240:0x05f5, B:251:0x060e, B:253:0x062c, B:256:0x065d, B:258:0x0667, B:260:0x067d, B:262:0x068e, B:270:0x06b9, B:274:0x06dc, B:276:0x06fb, B:278:0x0726, B:280:0x0742, B:282:0x0752, B:286:0x0761, B:288:0x0767, B:293:0x0777, B:295:0x0789, B:297:0x079c, B:334:0x08c8, B:336:0x08ce, B:345:0x08e7, B:347:0x08ed, B:355:0x0900, B:358:0x090a, B:362:0x0915, B:376:0x0936, B:380:0x0946, B:382:0x0976, B:383:0x097e, B:386:0x0985, B:451:0x0a9b, B:453:0x0ae6, B:454:0x0aed, B:457:0x0af7, B:459:0x0afb, B:461:0x0b01, B:479:0x0b5f, B:502:0x0bbf, B:531:0x0c05, B:540:0x0c44, B:542:0x0c4c, B:545:0x0c54, B:547:0x0c5c, B:551:0x0c67, B:568:0x0cfb, B:572:0x0d0b, B:587:0x0d50, B:589:0x0d56, B:591:0x0d5a, B:593:0x0d65, B:595:0x0d6b, B:597:0x0d75, B:599:0x0d86, B:601:0x0d92, B:603:0x0db2, B:605:0x0dbc, B:607:0x0de8, B:608:0x0df5, B:612:0x0e14, B:614:0x0e1a, B:616:0x0e22, B:618:0x0e28, B:619:0x0e4a, B:575:0x0d1a, B:583:0x0d2f, B:585:0x0d3b, B:552:0x0c8d, B:553:0x0c92, B:554:0x0c95, B:556:0x0c9d, B:558:0x0ca6, B:560:0x0cae, B:564:0x0ce7, B:565:0x0cf0, B:534:0x0c0f, B:536:0x0c17, B:538:0x0c3f, B:586:0x0d42, B:512:0x0bd3, B:517:0x0be0, B:520:0x0bea, B:523:0x0bf3, B:482:0x0b68, B:484:0x0b75, B:477:0x0b5a, B:388:0x099a, B:393:0x09aa, B:397:0x09bc, B:396:0x09b7, B:398:0x09cc, B:400:0x09da, B:401:0x09e3, B:403:0x09eb, B:404:0x09fa, B:405:0x0a03, B:407:0x0a09, B:410:0x0a16, B:413:0x0a20, B:414:0x0a23, B:416:0x0a29, B:419:0x0a32, B:421:0x0a3b, B:424:0x0a43, B:426:0x0a49, B:428:0x0a4d, B:430:0x0a55, B:436:0x0a63, B:438:0x0a69, B:440:0x0a6d, B:442:0x0a75, B:446:0x0a7c, B:448:0x0a8b, B:450:0x0a91, B:296:0x0795, B:299:0x07c4, B:301:0x07d6, B:303:0x07e9, B:302:0x07e2, B:309:0x081d, B:311:0x0825, B:316:0x083d, B:315:0x0838, B:322:0x0879, B:324:0x0885, B:326:0x0898, B:325:0x0891, B:279:0x0733, B:263:0x069a, B:265:0x069e, B:222:0x0584, B:228:0x059a, B:234:0x05dd, B:237:0x05e3, B:229:0x05ae, B:231:0x05b4, B:232:0x05c8, B:184:0x0438, B:187:0x0444, B:188:0x045f, B:179:0x0407, B:170:0x03d8, B:172:0x03e3, B:160:0x03b6, B:161:0x03bd, B:162:0x03c4, B:137:0x036c, B:138:0x0371, B:111:0x0301, B:113:0x0307, B:87:0x0271, B:54:0x0137, B:56:0x013d, B:57:0x0143, B:60:0x014d, B:61:0x0157, B:62:0x0169, B:64:0x016f, B:65:0x0186, B:67:0x018d, B:69:0x0195, B:70:0x01c5, B:51:0x011e, B:72:0x020e, B:562:0x0cb8, B:370:0x0928), top: B:630:0x002c, inners: #1, #3 }] */
    /* JADX WARN: Code duplicated, block: B:178:0x03fa A[Catch: Exception -> 0x0056, TryCatch #2 {Exception -> 0x0056, blocks: (B:13:0x002c, B:14:0x0038, B:16:0x0040, B:18:0x0051, B:19:0x0053, B:23:0x005a, B:25:0x0062, B:27:0x0074, B:28:0x0077, B:32:0x0080, B:35:0x0088, B:36:0x009e, B:38:0x00a6, B:39:0x00dc, B:41:0x00fe, B:44:0x0106, B:46:0x010e, B:49:0x0115, B:53:0x0129, B:71:0x01ee, B:73:0x0220, B:75:0x0232, B:78:0x023a, B:80:0x023e, B:82:0x025a, B:84:0x0261, B:88:0x0274, B:92:0x0280, B:94:0x028c, B:95:0x0292, B:97:0x029d, B:99:0x02a3, B:101:0x02af, B:102:0x02bb, B:103:0x02c5, B:105:0x02d5, B:107:0x02e5, B:109:0x02eb, B:118:0x0321, B:123:0x033e, B:133:0x0361, B:135:0x0367, B:139:0x0375, B:141:0x037b, B:147:0x0386, B:150:0x0399, B:164:0x03cc, B:166:0x03d0, B:174:0x03eb, B:176:0x03f2, B:178:0x03fa, B:181:0x0428, B:190:0x049f, B:196:0x04c2, B:198:0x04e6, B:200:0x04fc, B:202:0x0500, B:207:0x050c, B:208:0x0512, B:212:0x051f, B:218:0x0567, B:219:0x056a, B:213:0x0535, B:215:0x053d, B:216:0x0551, B:220:0x0575, B:240:0x05f5, B:251:0x060e, B:253:0x062c, B:256:0x065d, B:258:0x0667, B:260:0x067d, B:262:0x068e, B:270:0x06b9, B:274:0x06dc, B:276:0x06fb, B:278:0x0726, B:280:0x0742, B:282:0x0752, B:286:0x0761, B:288:0x0767, B:293:0x0777, B:295:0x0789, B:297:0x079c, B:334:0x08c8, B:336:0x08ce, B:345:0x08e7, B:347:0x08ed, B:355:0x0900, B:358:0x090a, B:362:0x0915, B:376:0x0936, B:380:0x0946, B:382:0x0976, B:383:0x097e, B:386:0x0985, B:451:0x0a9b, B:453:0x0ae6, B:454:0x0aed, B:457:0x0af7, B:459:0x0afb, B:461:0x0b01, B:479:0x0b5f, B:502:0x0bbf, B:531:0x0c05, B:540:0x0c44, B:542:0x0c4c, B:545:0x0c54, B:547:0x0c5c, B:551:0x0c67, B:568:0x0cfb, B:572:0x0d0b, B:587:0x0d50, B:589:0x0d56, B:591:0x0d5a, B:593:0x0d65, B:595:0x0d6b, B:597:0x0d75, B:599:0x0d86, B:601:0x0d92, B:603:0x0db2, B:605:0x0dbc, B:607:0x0de8, B:608:0x0df5, B:612:0x0e14, B:614:0x0e1a, B:616:0x0e22, B:618:0x0e28, B:619:0x0e4a, B:575:0x0d1a, B:583:0x0d2f, B:585:0x0d3b, B:552:0x0c8d, B:553:0x0c92, B:554:0x0c95, B:556:0x0c9d, B:558:0x0ca6, B:560:0x0cae, B:564:0x0ce7, B:565:0x0cf0, B:534:0x0c0f, B:536:0x0c17, B:538:0x0c3f, B:586:0x0d42, B:512:0x0bd3, B:517:0x0be0, B:520:0x0bea, B:523:0x0bf3, B:482:0x0b68, B:484:0x0b75, B:477:0x0b5a, B:388:0x099a, B:393:0x09aa, B:397:0x09bc, B:396:0x09b7, B:398:0x09cc, B:400:0x09da, B:401:0x09e3, B:403:0x09eb, B:404:0x09fa, B:405:0x0a03, B:407:0x0a09, B:410:0x0a16, B:413:0x0a20, B:414:0x0a23, B:416:0x0a29, B:419:0x0a32, B:421:0x0a3b, B:424:0x0a43, B:426:0x0a49, B:428:0x0a4d, B:430:0x0a55, B:436:0x0a63, B:438:0x0a69, B:440:0x0a6d, B:442:0x0a75, B:446:0x0a7c, B:448:0x0a8b, B:450:0x0a91, B:296:0x0795, B:299:0x07c4, B:301:0x07d6, B:303:0x07e9, B:302:0x07e2, B:309:0x081d, B:311:0x0825, B:316:0x083d, B:315:0x0838, B:322:0x0879, B:324:0x0885, B:326:0x0898, B:325:0x0891, B:279:0x0733, B:263:0x069a, B:265:0x069e, B:222:0x0584, B:228:0x059a, B:234:0x05dd, B:237:0x05e3, B:229:0x05ae, B:231:0x05b4, B:232:0x05c8, B:184:0x0438, B:187:0x0444, B:188:0x045f, B:179:0x0407, B:170:0x03d8, B:172:0x03e3, B:160:0x03b6, B:161:0x03bd, B:162:0x03c4, B:137:0x036c, B:138:0x0371, B:111:0x0301, B:113:0x0307, B:87:0x0271, B:54:0x0137, B:56:0x013d, B:57:0x0143, B:60:0x014d, B:61:0x0157, B:62:0x0169, B:64:0x016f, B:65:0x0186, B:67:0x018d, B:69:0x0195, B:70:0x01c5, B:51:0x011e, B:72:0x020e, B:562:0x0cb8, B:370:0x0928), top: B:630:0x002c, inners: #1, #3 }] */
    /* JADX WARN: Code duplicated, block: B:179:0x0407 A[Catch: Exception -> 0x0056, TryCatch #2 {Exception -> 0x0056, blocks: (B:13:0x002c, B:14:0x0038, B:16:0x0040, B:18:0x0051, B:19:0x0053, B:23:0x005a, B:25:0x0062, B:27:0x0074, B:28:0x0077, B:32:0x0080, B:35:0x0088, B:36:0x009e, B:38:0x00a6, B:39:0x00dc, B:41:0x00fe, B:44:0x0106, B:46:0x010e, B:49:0x0115, B:53:0x0129, B:71:0x01ee, B:73:0x0220, B:75:0x0232, B:78:0x023a, B:80:0x023e, B:82:0x025a, B:84:0x0261, B:88:0x0274, B:92:0x0280, B:94:0x028c, B:95:0x0292, B:97:0x029d, B:99:0x02a3, B:101:0x02af, B:102:0x02bb, B:103:0x02c5, B:105:0x02d5, B:107:0x02e5, B:109:0x02eb, B:118:0x0321, B:123:0x033e, B:133:0x0361, B:135:0x0367, B:139:0x0375, B:141:0x037b, B:147:0x0386, B:150:0x0399, B:164:0x03cc, B:166:0x03d0, B:174:0x03eb, B:176:0x03f2, B:178:0x03fa, B:181:0x0428, B:190:0x049f, B:196:0x04c2, B:198:0x04e6, B:200:0x04fc, B:202:0x0500, B:207:0x050c, B:208:0x0512, B:212:0x051f, B:218:0x0567, B:219:0x056a, B:213:0x0535, B:215:0x053d, B:216:0x0551, B:220:0x0575, B:240:0x05f5, B:251:0x060e, B:253:0x062c, B:256:0x065d, B:258:0x0667, B:260:0x067d, B:262:0x068e, B:270:0x06b9, B:274:0x06dc, B:276:0x06fb, B:278:0x0726, B:280:0x0742, B:282:0x0752, B:286:0x0761, B:288:0x0767, B:293:0x0777, B:295:0x0789, B:297:0x079c, B:334:0x08c8, B:336:0x08ce, B:345:0x08e7, B:347:0x08ed, B:355:0x0900, B:358:0x090a, B:362:0x0915, B:376:0x0936, B:380:0x0946, B:382:0x0976, B:383:0x097e, B:386:0x0985, B:451:0x0a9b, B:453:0x0ae6, B:454:0x0aed, B:457:0x0af7, B:459:0x0afb, B:461:0x0b01, B:479:0x0b5f, B:502:0x0bbf, B:531:0x0c05, B:540:0x0c44, B:542:0x0c4c, B:545:0x0c54, B:547:0x0c5c, B:551:0x0c67, B:568:0x0cfb, B:572:0x0d0b, B:587:0x0d50, B:589:0x0d56, B:591:0x0d5a, B:593:0x0d65, B:595:0x0d6b, B:597:0x0d75, B:599:0x0d86, B:601:0x0d92, B:603:0x0db2, B:605:0x0dbc, B:607:0x0de8, B:608:0x0df5, B:612:0x0e14, B:614:0x0e1a, B:616:0x0e22, B:618:0x0e28, B:619:0x0e4a, B:575:0x0d1a, B:583:0x0d2f, B:585:0x0d3b, B:552:0x0c8d, B:553:0x0c92, B:554:0x0c95, B:556:0x0c9d, B:558:0x0ca6, B:560:0x0cae, B:564:0x0ce7, B:565:0x0cf0, B:534:0x0c0f, B:536:0x0c17, B:538:0x0c3f, B:586:0x0d42, B:512:0x0bd3, B:517:0x0be0, B:520:0x0bea, B:523:0x0bf3, B:482:0x0b68, B:484:0x0b75, B:477:0x0b5a, B:388:0x099a, B:393:0x09aa, B:397:0x09bc, B:396:0x09b7, B:398:0x09cc, B:400:0x09da, B:401:0x09e3, B:403:0x09eb, B:404:0x09fa, B:405:0x0a03, B:407:0x0a09, B:410:0x0a16, B:413:0x0a20, B:414:0x0a23, B:416:0x0a29, B:419:0x0a32, B:421:0x0a3b, B:424:0x0a43, B:426:0x0a49, B:428:0x0a4d, B:430:0x0a55, B:436:0x0a63, B:438:0x0a69, B:440:0x0a6d, B:442:0x0a75, B:446:0x0a7c, B:448:0x0a8b, B:450:0x0a91, B:296:0x0795, B:299:0x07c4, B:301:0x07d6, B:303:0x07e9, B:302:0x07e2, B:309:0x081d, B:311:0x0825, B:316:0x083d, B:315:0x0838, B:322:0x0879, B:324:0x0885, B:326:0x0898, B:325:0x0891, B:279:0x0733, B:263:0x069a, B:265:0x069e, B:222:0x0584, B:228:0x059a, B:234:0x05dd, B:237:0x05e3, B:229:0x05ae, B:231:0x05b4, B:232:0x05c8, B:184:0x0438, B:187:0x0444, B:188:0x045f, B:179:0x0407, B:170:0x03d8, B:172:0x03e3, B:160:0x03b6, B:161:0x03bd, B:162:0x03c4, B:137:0x036c, B:138:0x0371, B:111:0x0301, B:113:0x0307, B:87:0x0271, B:54:0x0137, B:56:0x013d, B:57:0x0143, B:60:0x014d, B:61:0x0157, B:62:0x0169, B:64:0x016f, B:65:0x0186, B:67:0x018d, B:69:0x0195, B:70:0x01c5, B:51:0x011e, B:72:0x020e, B:562:0x0cb8, B:370:0x0928), top: B:630:0x002c, inners: #1, #3 }] */
    /* JADX WARN: Code duplicated, block: B:180:0x0426  */
    /* JADX WARN: Code duplicated, block: B:183:0x0433  */
    /* JADX WARN: Code duplicated, block: B:184:0x0438 A[Catch: Exception -> 0x0056, TRY_LEAVE, TryCatch #2 {Exception -> 0x0056, blocks: (B:13:0x002c, B:14:0x0038, B:16:0x0040, B:18:0x0051, B:19:0x0053, B:23:0x005a, B:25:0x0062, B:27:0x0074, B:28:0x0077, B:32:0x0080, B:35:0x0088, B:36:0x009e, B:38:0x00a6, B:39:0x00dc, B:41:0x00fe, B:44:0x0106, B:46:0x010e, B:49:0x0115, B:53:0x0129, B:71:0x01ee, B:73:0x0220, B:75:0x0232, B:78:0x023a, B:80:0x023e, B:82:0x025a, B:84:0x0261, B:88:0x0274, B:92:0x0280, B:94:0x028c, B:95:0x0292, B:97:0x029d, B:99:0x02a3, B:101:0x02af, B:102:0x02bb, B:103:0x02c5, B:105:0x02d5, B:107:0x02e5, B:109:0x02eb, B:118:0x0321, B:123:0x033e, B:133:0x0361, B:135:0x0367, B:139:0x0375, B:141:0x037b, B:147:0x0386, B:150:0x0399, B:164:0x03cc, B:166:0x03d0, B:174:0x03eb, B:176:0x03f2, B:178:0x03fa, B:181:0x0428, B:190:0x049f, B:196:0x04c2, B:198:0x04e6, B:200:0x04fc, B:202:0x0500, B:207:0x050c, B:208:0x0512, B:212:0x051f, B:218:0x0567, B:219:0x056a, B:213:0x0535, B:215:0x053d, B:216:0x0551, B:220:0x0575, B:240:0x05f5, B:251:0x060e, B:253:0x062c, B:256:0x065d, B:258:0x0667, B:260:0x067d, B:262:0x068e, B:270:0x06b9, B:274:0x06dc, B:276:0x06fb, B:278:0x0726, B:280:0x0742, B:282:0x0752, B:286:0x0761, B:288:0x0767, B:293:0x0777, B:295:0x0789, B:297:0x079c, B:334:0x08c8, B:336:0x08ce, B:345:0x08e7, B:347:0x08ed, B:355:0x0900, B:358:0x090a, B:362:0x0915, B:376:0x0936, B:380:0x0946, B:382:0x0976, B:383:0x097e, B:386:0x0985, B:451:0x0a9b, B:453:0x0ae6, B:454:0x0aed, B:457:0x0af7, B:459:0x0afb, B:461:0x0b01, B:479:0x0b5f, B:502:0x0bbf, B:531:0x0c05, B:540:0x0c44, B:542:0x0c4c, B:545:0x0c54, B:547:0x0c5c, B:551:0x0c67, B:568:0x0cfb, B:572:0x0d0b, B:587:0x0d50, B:589:0x0d56, B:591:0x0d5a, B:593:0x0d65, B:595:0x0d6b, B:597:0x0d75, B:599:0x0d86, B:601:0x0d92, B:603:0x0db2, B:605:0x0dbc, B:607:0x0de8, B:608:0x0df5, B:612:0x0e14, B:614:0x0e1a, B:616:0x0e22, B:618:0x0e28, B:619:0x0e4a, B:575:0x0d1a, B:583:0x0d2f, B:585:0x0d3b, B:552:0x0c8d, B:553:0x0c92, B:554:0x0c95, B:556:0x0c9d, B:558:0x0ca6, B:560:0x0cae, B:564:0x0ce7, B:565:0x0cf0, B:534:0x0c0f, B:536:0x0c17, B:538:0x0c3f, B:586:0x0d42, B:512:0x0bd3, B:517:0x0be0, B:520:0x0bea, B:523:0x0bf3, B:482:0x0b68, B:484:0x0b75, B:477:0x0b5a, B:388:0x099a, B:393:0x09aa, B:397:0x09bc, B:396:0x09b7, B:398:0x09cc, B:400:0x09da, B:401:0x09e3, B:403:0x09eb, B:404:0x09fa, B:405:0x0a03, B:407:0x0a09, B:410:0x0a16, B:413:0x0a20, B:414:0x0a23, B:416:0x0a29, B:419:0x0a32, B:421:0x0a3b, B:424:0x0a43, B:426:0x0a49, B:428:0x0a4d, B:430:0x0a55, B:436:0x0a63, B:438:0x0a69, B:440:0x0a6d, B:442:0x0a75, B:446:0x0a7c, B:448:0x0a8b, B:450:0x0a91, B:296:0x0795, B:299:0x07c4, B:301:0x07d6, B:303:0x07e9, B:302:0x07e2, B:309:0x081d, B:311:0x0825, B:316:0x083d, B:315:0x0838, B:322:0x0879, B:324:0x0885, B:326:0x0898, B:325:0x0891, B:279:0x0733, B:263:0x069a, B:265:0x069e, B:222:0x0584, B:228:0x059a, B:234:0x05dd, B:237:0x05e3, B:229:0x05ae, B:231:0x05b4, B:232:0x05c8, B:184:0x0438, B:187:0x0444, B:188:0x045f, B:179:0x0407, B:170:0x03d8, B:172:0x03e3, B:160:0x03b6, B:161:0x03bd, B:162:0x03c4, B:137:0x036c, B:138:0x0371, B:111:0x0301, B:113:0x0307, B:87:0x0271, B:54:0x0137, B:56:0x013d, B:57:0x0143, B:60:0x014d, B:61:0x0157, B:62:0x0169, B:64:0x016f, B:65:0x0186, B:67:0x018d, B:69:0x0195, B:70:0x01c5, B:51:0x011e, B:72:0x020e, B:562:0x0cb8, B:370:0x0928), top: B:630:0x002c, inners: #1, #3 }] */
    /* JADX WARN: Code duplicated, block: B:187:0x0444 A[Catch: Exception -> 0x0056, TRY_ENTER, TryCatch #2 {Exception -> 0x0056, blocks: (B:13:0x002c, B:14:0x0038, B:16:0x0040, B:18:0x0051, B:19:0x0053, B:23:0x005a, B:25:0x0062, B:27:0x0074, B:28:0x0077, B:32:0x0080, B:35:0x0088, B:36:0x009e, B:38:0x00a6, B:39:0x00dc, B:41:0x00fe, B:44:0x0106, B:46:0x010e, B:49:0x0115, B:53:0x0129, B:71:0x01ee, B:73:0x0220, B:75:0x0232, B:78:0x023a, B:80:0x023e, B:82:0x025a, B:84:0x0261, B:88:0x0274, B:92:0x0280, B:94:0x028c, B:95:0x0292, B:97:0x029d, B:99:0x02a3, B:101:0x02af, B:102:0x02bb, B:103:0x02c5, B:105:0x02d5, B:107:0x02e5, B:109:0x02eb, B:118:0x0321, B:123:0x033e, B:133:0x0361, B:135:0x0367, B:139:0x0375, B:141:0x037b, B:147:0x0386, B:150:0x0399, B:164:0x03cc, B:166:0x03d0, B:174:0x03eb, B:176:0x03f2, B:178:0x03fa, B:181:0x0428, B:190:0x049f, B:196:0x04c2, B:198:0x04e6, B:200:0x04fc, B:202:0x0500, B:207:0x050c, B:208:0x0512, B:212:0x051f, B:218:0x0567, B:219:0x056a, B:213:0x0535, B:215:0x053d, B:216:0x0551, B:220:0x0575, B:240:0x05f5, B:251:0x060e, B:253:0x062c, B:256:0x065d, B:258:0x0667, B:260:0x067d, B:262:0x068e, B:270:0x06b9, B:274:0x06dc, B:276:0x06fb, B:278:0x0726, B:280:0x0742, B:282:0x0752, B:286:0x0761, B:288:0x0767, B:293:0x0777, B:295:0x0789, B:297:0x079c, B:334:0x08c8, B:336:0x08ce, B:345:0x08e7, B:347:0x08ed, B:355:0x0900, B:358:0x090a, B:362:0x0915, B:376:0x0936, B:380:0x0946, B:382:0x0976, B:383:0x097e, B:386:0x0985, B:451:0x0a9b, B:453:0x0ae6, B:454:0x0aed, B:457:0x0af7, B:459:0x0afb, B:461:0x0b01, B:479:0x0b5f, B:502:0x0bbf, B:531:0x0c05, B:540:0x0c44, B:542:0x0c4c, B:545:0x0c54, B:547:0x0c5c, B:551:0x0c67, B:568:0x0cfb, B:572:0x0d0b, B:587:0x0d50, B:589:0x0d56, B:591:0x0d5a, B:593:0x0d65, B:595:0x0d6b, B:597:0x0d75, B:599:0x0d86, B:601:0x0d92, B:603:0x0db2, B:605:0x0dbc, B:607:0x0de8, B:608:0x0df5, B:612:0x0e14, B:614:0x0e1a, B:616:0x0e22, B:618:0x0e28, B:619:0x0e4a, B:575:0x0d1a, B:583:0x0d2f, B:585:0x0d3b, B:552:0x0c8d, B:553:0x0c92, B:554:0x0c95, B:556:0x0c9d, B:558:0x0ca6, B:560:0x0cae, B:564:0x0ce7, B:565:0x0cf0, B:534:0x0c0f, B:536:0x0c17, B:538:0x0c3f, B:586:0x0d42, B:512:0x0bd3, B:517:0x0be0, B:520:0x0bea, B:523:0x0bf3, B:482:0x0b68, B:484:0x0b75, B:477:0x0b5a, B:388:0x099a, B:393:0x09aa, B:397:0x09bc, B:396:0x09b7, B:398:0x09cc, B:400:0x09da, B:401:0x09e3, B:403:0x09eb, B:404:0x09fa, B:405:0x0a03, B:407:0x0a09, B:410:0x0a16, B:413:0x0a20, B:414:0x0a23, B:416:0x0a29, B:419:0x0a32, B:421:0x0a3b, B:424:0x0a43, B:426:0x0a49, B:428:0x0a4d, B:430:0x0a55, B:436:0x0a63, B:438:0x0a69, B:440:0x0a6d, B:442:0x0a75, B:446:0x0a7c, B:448:0x0a8b, B:450:0x0a91, B:296:0x0795, B:299:0x07c4, B:301:0x07d6, B:303:0x07e9, B:302:0x07e2, B:309:0x081d, B:311:0x0825, B:316:0x083d, B:315:0x0838, B:322:0x0879, B:324:0x0885, B:326:0x0898, B:325:0x0891, B:279:0x0733, B:263:0x069a, B:265:0x069e, B:222:0x0584, B:228:0x059a, B:234:0x05dd, B:237:0x05e3, B:229:0x05ae, B:231:0x05b4, B:232:0x05c8, B:184:0x0438, B:187:0x0444, B:188:0x045f, B:179:0x0407, B:170:0x03d8, B:172:0x03e3, B:160:0x03b6, B:161:0x03bd, B:162:0x03c4, B:137:0x036c, B:138:0x0371, B:111:0x0301, B:113:0x0307, B:87:0x0271, B:54:0x0137, B:56:0x013d, B:57:0x0143, B:60:0x014d, B:61:0x0157, B:62:0x0169, B:64:0x016f, B:65:0x0186, B:67:0x018d, B:69:0x0195, B:70:0x01c5, B:51:0x011e, B:72:0x020e, B:562:0x0cb8, B:370:0x0928), top: B:630:0x002c, inners: #1, #3 }] */
    /* JADX WARN: Code duplicated, block: B:188:0x045f A[Catch: Exception -> 0x0056, TryCatch #2 {Exception -> 0x0056, blocks: (B:13:0x002c, B:14:0x0038, B:16:0x0040, B:18:0x0051, B:19:0x0053, B:23:0x005a, B:25:0x0062, B:27:0x0074, B:28:0x0077, B:32:0x0080, B:35:0x0088, B:36:0x009e, B:38:0x00a6, B:39:0x00dc, B:41:0x00fe, B:44:0x0106, B:46:0x010e, B:49:0x0115, B:53:0x0129, B:71:0x01ee, B:73:0x0220, B:75:0x0232, B:78:0x023a, B:80:0x023e, B:82:0x025a, B:84:0x0261, B:88:0x0274, B:92:0x0280, B:94:0x028c, B:95:0x0292, B:97:0x029d, B:99:0x02a3, B:101:0x02af, B:102:0x02bb, B:103:0x02c5, B:105:0x02d5, B:107:0x02e5, B:109:0x02eb, B:118:0x0321, B:123:0x033e, B:133:0x0361, B:135:0x0367, B:139:0x0375, B:141:0x037b, B:147:0x0386, B:150:0x0399, B:164:0x03cc, B:166:0x03d0, B:174:0x03eb, B:176:0x03f2, B:178:0x03fa, B:181:0x0428, B:190:0x049f, B:196:0x04c2, B:198:0x04e6, B:200:0x04fc, B:202:0x0500, B:207:0x050c, B:208:0x0512, B:212:0x051f, B:218:0x0567, B:219:0x056a, B:213:0x0535, B:215:0x053d, B:216:0x0551, B:220:0x0575, B:240:0x05f5, B:251:0x060e, B:253:0x062c, B:256:0x065d, B:258:0x0667, B:260:0x067d, B:262:0x068e, B:270:0x06b9, B:274:0x06dc, B:276:0x06fb, B:278:0x0726, B:280:0x0742, B:282:0x0752, B:286:0x0761, B:288:0x0767, B:293:0x0777, B:295:0x0789, B:297:0x079c, B:334:0x08c8, B:336:0x08ce, B:345:0x08e7, B:347:0x08ed, B:355:0x0900, B:358:0x090a, B:362:0x0915, B:376:0x0936, B:380:0x0946, B:382:0x0976, B:383:0x097e, B:386:0x0985, B:451:0x0a9b, B:453:0x0ae6, B:454:0x0aed, B:457:0x0af7, B:459:0x0afb, B:461:0x0b01, B:479:0x0b5f, B:502:0x0bbf, B:531:0x0c05, B:540:0x0c44, B:542:0x0c4c, B:545:0x0c54, B:547:0x0c5c, B:551:0x0c67, B:568:0x0cfb, B:572:0x0d0b, B:587:0x0d50, B:589:0x0d56, B:591:0x0d5a, B:593:0x0d65, B:595:0x0d6b, B:597:0x0d75, B:599:0x0d86, B:601:0x0d92, B:603:0x0db2, B:605:0x0dbc, B:607:0x0de8, B:608:0x0df5, B:612:0x0e14, B:614:0x0e1a, B:616:0x0e22, B:618:0x0e28, B:619:0x0e4a, B:575:0x0d1a, B:583:0x0d2f, B:585:0x0d3b, B:552:0x0c8d, B:553:0x0c92, B:554:0x0c95, B:556:0x0c9d, B:558:0x0ca6, B:560:0x0cae, B:564:0x0ce7, B:565:0x0cf0, B:534:0x0c0f, B:536:0x0c17, B:538:0x0c3f, B:586:0x0d42, B:512:0x0bd3, B:517:0x0be0, B:520:0x0bea, B:523:0x0bf3, B:482:0x0b68, B:484:0x0b75, B:477:0x0b5a, B:388:0x099a, B:393:0x09aa, B:397:0x09bc, B:396:0x09b7, B:398:0x09cc, B:400:0x09da, B:401:0x09e3, B:403:0x09eb, B:404:0x09fa, B:405:0x0a03, B:407:0x0a09, B:410:0x0a16, B:413:0x0a20, B:414:0x0a23, B:416:0x0a29, B:419:0x0a32, B:421:0x0a3b, B:424:0x0a43, B:426:0x0a49, B:428:0x0a4d, B:430:0x0a55, B:436:0x0a63, B:438:0x0a69, B:440:0x0a6d, B:442:0x0a75, B:446:0x0a7c, B:448:0x0a8b, B:450:0x0a91, B:296:0x0795, B:299:0x07c4, B:301:0x07d6, B:303:0x07e9, B:302:0x07e2, B:309:0x081d, B:311:0x0825, B:316:0x083d, B:315:0x0838, B:322:0x0879, B:324:0x0885, B:326:0x0898, B:325:0x0891, B:279:0x0733, B:263:0x069a, B:265:0x069e, B:222:0x0584, B:228:0x059a, B:234:0x05dd, B:237:0x05e3, B:229:0x05ae, B:231:0x05b4, B:232:0x05c8, B:184:0x0438, B:187:0x0444, B:188:0x045f, B:179:0x0407, B:170:0x03d8, B:172:0x03e3, B:160:0x03b6, B:161:0x03bd, B:162:0x03c4, B:137:0x036c, B:138:0x0371, B:111:0x0301, B:113:0x0307, B:87:0x0271, B:54:0x0137, B:56:0x013d, B:57:0x0143, B:60:0x014d, B:61:0x0157, B:62:0x0169, B:64:0x016f, B:65:0x0186, B:67:0x018d, B:69:0x0195, B:70:0x01c5, B:51:0x011e, B:72:0x020e, B:562:0x0cb8, B:370:0x0928), top: B:630:0x002c, inners: #1, #3 }] */
    /* JADX WARN: Code duplicated, block: B:189:0x0497  */
    /* JADX WARN: Code duplicated, block: B:193:0x04b5 A[DONT_INVERT] */
    /* JADX WARN: Code duplicated, block: B:194:0x04b7  */
    /* JADX WARN: Code duplicated, block: B:195:0x04b8 A[PHI: r14
  0x04b8: PHI (r14v7 int A[IMMUTABLE_TYPE]) = (r14v6 int), (r14v28 int) binds: [B:192:0x04b3, B:194:0x04b7] A[DONT_GENERATE, DONT_INLINE]] */
    /* JADX WARN: Code duplicated, block: B:196:0x04c2 A[Catch: Exception -> 0x0056, TRY_ENTER, TryCatch #2 {Exception -> 0x0056, blocks: (B:13:0x002c, B:14:0x0038, B:16:0x0040, B:18:0x0051, B:19:0x0053, B:23:0x005a, B:25:0x0062, B:27:0x0074, B:28:0x0077, B:32:0x0080, B:35:0x0088, B:36:0x009e, B:38:0x00a6, B:39:0x00dc, B:41:0x00fe, B:44:0x0106, B:46:0x010e, B:49:0x0115, B:53:0x0129, B:71:0x01ee, B:73:0x0220, B:75:0x0232, B:78:0x023a, B:80:0x023e, B:82:0x025a, B:84:0x0261, B:88:0x0274, B:92:0x0280, B:94:0x028c, B:95:0x0292, B:97:0x029d, B:99:0x02a3, B:101:0x02af, B:102:0x02bb, B:103:0x02c5, B:105:0x02d5, B:107:0x02e5, B:109:0x02eb, B:118:0x0321, B:123:0x033e, B:133:0x0361, B:135:0x0367, B:139:0x0375, B:141:0x037b, B:147:0x0386, B:150:0x0399, B:164:0x03cc, B:166:0x03d0, B:174:0x03eb, B:176:0x03f2, B:178:0x03fa, B:181:0x0428, B:190:0x049f, B:196:0x04c2, B:198:0x04e6, B:200:0x04fc, B:202:0x0500, B:207:0x050c, B:208:0x0512, B:212:0x051f, B:218:0x0567, B:219:0x056a, B:213:0x0535, B:215:0x053d, B:216:0x0551, B:220:0x0575, B:240:0x05f5, B:251:0x060e, B:253:0x062c, B:256:0x065d, B:258:0x0667, B:260:0x067d, B:262:0x068e, B:270:0x06b9, B:274:0x06dc, B:276:0x06fb, B:278:0x0726, B:280:0x0742, B:282:0x0752, B:286:0x0761, B:288:0x0767, B:293:0x0777, B:295:0x0789, B:297:0x079c, B:334:0x08c8, B:336:0x08ce, B:345:0x08e7, B:347:0x08ed, B:355:0x0900, B:358:0x090a, B:362:0x0915, B:376:0x0936, B:380:0x0946, B:382:0x0976, B:383:0x097e, B:386:0x0985, B:451:0x0a9b, B:453:0x0ae6, B:454:0x0aed, B:457:0x0af7, B:459:0x0afb, B:461:0x0b01, B:479:0x0b5f, B:502:0x0bbf, B:531:0x0c05, B:540:0x0c44, B:542:0x0c4c, B:545:0x0c54, B:547:0x0c5c, B:551:0x0c67, B:568:0x0cfb, B:572:0x0d0b, B:587:0x0d50, B:589:0x0d56, B:591:0x0d5a, B:593:0x0d65, B:595:0x0d6b, B:597:0x0d75, B:599:0x0d86, B:601:0x0d92, B:603:0x0db2, B:605:0x0dbc, B:607:0x0de8, B:608:0x0df5, B:612:0x0e14, B:614:0x0e1a, B:616:0x0e22, B:618:0x0e28, B:619:0x0e4a, B:575:0x0d1a, B:583:0x0d2f, B:585:0x0d3b, B:552:0x0c8d, B:553:0x0c92, B:554:0x0c95, B:556:0x0c9d, B:558:0x0ca6, B:560:0x0cae, B:564:0x0ce7, B:565:0x0cf0, B:534:0x0c0f, B:536:0x0c17, B:538:0x0c3f, B:586:0x0d42, B:512:0x0bd3, B:517:0x0be0, B:520:0x0bea, B:523:0x0bf3, B:482:0x0b68, B:484:0x0b75, B:477:0x0b5a, B:388:0x099a, B:393:0x09aa, B:397:0x09bc, B:396:0x09b7, B:398:0x09cc, B:400:0x09da, B:401:0x09e3, B:403:0x09eb, B:404:0x09fa, B:405:0x0a03, B:407:0x0a09, B:410:0x0a16, B:413:0x0a20, B:414:0x0a23, B:416:0x0a29, B:419:0x0a32, B:421:0x0a3b, B:424:0x0a43, B:426:0x0a49, B:428:0x0a4d, B:430:0x0a55, B:436:0x0a63, B:438:0x0a69, B:440:0x0a6d, B:442:0x0a75, B:446:0x0a7c, B:448:0x0a8b, B:450:0x0a91, B:296:0x0795, B:299:0x07c4, B:301:0x07d6, B:303:0x07e9, B:302:0x07e2, B:309:0x081d, B:311:0x0825, B:316:0x083d, B:315:0x0838, B:322:0x0879, B:324:0x0885, B:326:0x0898, B:325:0x0891, B:279:0x0733, B:263:0x069a, B:265:0x069e, B:222:0x0584, B:228:0x059a, B:234:0x05dd, B:237:0x05e3, B:229:0x05ae, B:231:0x05b4, B:232:0x05c8, B:184:0x0438, B:187:0x0444, B:188:0x045f, B:179:0x0407, B:170:0x03d8, B:172:0x03e3, B:160:0x03b6, B:161:0x03bd, B:162:0x03c4, B:137:0x036c, B:138:0x0371, B:111:0x0301, B:113:0x0307, B:87:0x0271, B:54:0x0137, B:56:0x013d, B:57:0x0143, B:60:0x014d, B:61:0x0157, B:62:0x0169, B:64:0x016f, B:65:0x0186, B:67:0x018d, B:69:0x0195, B:70:0x01c5, B:51:0x011e, B:72:0x020e, B:562:0x0cb8, B:370:0x0928), top: B:630:0x002c, inners: #1, #3 }] */
    /* JADX WARN: Code duplicated, block: B:198:0x04e6 A[Catch: Exception -> 0x0056, TryCatch #2 {Exception -> 0x0056, blocks: (B:13:0x002c, B:14:0x0038, B:16:0x0040, B:18:0x0051, B:19:0x0053, B:23:0x005a, B:25:0x0062, B:27:0x0074, B:28:0x0077, B:32:0x0080, B:35:0x0088, B:36:0x009e, B:38:0x00a6, B:39:0x00dc, B:41:0x00fe, B:44:0x0106, B:46:0x010e, B:49:0x0115, B:53:0x0129, B:71:0x01ee, B:73:0x0220, B:75:0x0232, B:78:0x023a, B:80:0x023e, B:82:0x025a, B:84:0x0261, B:88:0x0274, B:92:0x0280, B:94:0x028c, B:95:0x0292, B:97:0x029d, B:99:0x02a3, B:101:0x02af, B:102:0x02bb, B:103:0x02c5, B:105:0x02d5, B:107:0x02e5, B:109:0x02eb, B:118:0x0321, B:123:0x033e, B:133:0x0361, B:135:0x0367, B:139:0x0375, B:141:0x037b, B:147:0x0386, B:150:0x0399, B:164:0x03cc, B:166:0x03d0, B:174:0x03eb, B:176:0x03f2, B:178:0x03fa, B:181:0x0428, B:190:0x049f, B:196:0x04c2, B:198:0x04e6, B:200:0x04fc, B:202:0x0500, B:207:0x050c, B:208:0x0512, B:212:0x051f, B:218:0x0567, B:219:0x056a, B:213:0x0535, B:215:0x053d, B:216:0x0551, B:220:0x0575, B:240:0x05f5, B:251:0x060e, B:253:0x062c, B:256:0x065d, B:258:0x0667, B:260:0x067d, B:262:0x068e, B:270:0x06b9, B:274:0x06dc, B:276:0x06fb, B:278:0x0726, B:280:0x0742, B:282:0x0752, B:286:0x0761, B:288:0x0767, B:293:0x0777, B:295:0x0789, B:297:0x079c, B:334:0x08c8, B:336:0x08ce, B:345:0x08e7, B:347:0x08ed, B:355:0x0900, B:358:0x090a, B:362:0x0915, B:376:0x0936, B:380:0x0946, B:382:0x0976, B:383:0x097e, B:386:0x0985, B:451:0x0a9b, B:453:0x0ae6, B:454:0x0aed, B:457:0x0af7, B:459:0x0afb, B:461:0x0b01, B:479:0x0b5f, B:502:0x0bbf, B:531:0x0c05, B:540:0x0c44, B:542:0x0c4c, B:545:0x0c54, B:547:0x0c5c, B:551:0x0c67, B:568:0x0cfb, B:572:0x0d0b, B:587:0x0d50, B:589:0x0d56, B:591:0x0d5a, B:593:0x0d65, B:595:0x0d6b, B:597:0x0d75, B:599:0x0d86, B:601:0x0d92, B:603:0x0db2, B:605:0x0dbc, B:607:0x0de8, B:608:0x0df5, B:612:0x0e14, B:614:0x0e1a, B:616:0x0e22, B:618:0x0e28, B:619:0x0e4a, B:575:0x0d1a, B:583:0x0d2f, B:585:0x0d3b, B:552:0x0c8d, B:553:0x0c92, B:554:0x0c95, B:556:0x0c9d, B:558:0x0ca6, B:560:0x0cae, B:564:0x0ce7, B:565:0x0cf0, B:534:0x0c0f, B:536:0x0c17, B:538:0x0c3f, B:586:0x0d42, B:512:0x0bd3, B:517:0x0be0, B:520:0x0bea, B:523:0x0bf3, B:482:0x0b68, B:484:0x0b75, B:477:0x0b5a, B:388:0x099a, B:393:0x09aa, B:397:0x09bc, B:396:0x09b7, B:398:0x09cc, B:400:0x09da, B:401:0x09e3, B:403:0x09eb, B:404:0x09fa, B:405:0x0a03, B:407:0x0a09, B:410:0x0a16, B:413:0x0a20, B:414:0x0a23, B:416:0x0a29, B:419:0x0a32, B:421:0x0a3b, B:424:0x0a43, B:426:0x0a49, B:428:0x0a4d, B:430:0x0a55, B:436:0x0a63, B:438:0x0a69, B:440:0x0a6d, B:442:0x0a75, B:446:0x0a7c, B:448:0x0a8b, B:450:0x0a91, B:296:0x0795, B:299:0x07c4, B:301:0x07d6, B:303:0x07e9, B:302:0x07e2, B:309:0x081d, B:311:0x0825, B:316:0x083d, B:315:0x0838, B:322:0x0879, B:324:0x0885, B:326:0x0898, B:325:0x0891, B:279:0x0733, B:263:0x069a, B:265:0x069e, B:222:0x0584, B:228:0x059a, B:234:0x05dd, B:237:0x05e3, B:229:0x05ae, B:231:0x05b4, B:232:0x05c8, B:184:0x0438, B:187:0x0444, B:188:0x045f, B:179:0x0407, B:170:0x03d8, B:172:0x03e3, B:160:0x03b6, B:161:0x03bd, B:162:0x03c4, B:137:0x036c, B:138:0x0371, B:111:0x0301, B:113:0x0307, B:87:0x0271, B:54:0x0137, B:56:0x013d, B:57:0x0143, B:60:0x014d, B:61:0x0157, B:62:0x0169, B:64:0x016f, B:65:0x0186, B:67:0x018d, B:69:0x0195, B:70:0x01c5, B:51:0x011e, B:72:0x020e, B:562:0x0cb8, B:370:0x0928), top: B:630:0x002c, inners: #1, #3 }] */
    /* JADX WARN: Code duplicated, block: B:200:0x04fc A[Catch: Exception -> 0x0056, TryCatch #2 {Exception -> 0x0056, blocks: (B:13:0x002c, B:14:0x0038, B:16:0x0040, B:18:0x0051, B:19:0x0053, B:23:0x005a, B:25:0x0062, B:27:0x0074, B:28:0x0077, B:32:0x0080, B:35:0x0088, B:36:0x009e, B:38:0x00a6, B:39:0x00dc, B:41:0x00fe, B:44:0x0106, B:46:0x010e, B:49:0x0115, B:53:0x0129, B:71:0x01ee, B:73:0x0220, B:75:0x0232, B:78:0x023a, B:80:0x023e, B:82:0x025a, B:84:0x0261, B:88:0x0274, B:92:0x0280, B:94:0x028c, B:95:0x0292, B:97:0x029d, B:99:0x02a3, B:101:0x02af, B:102:0x02bb, B:103:0x02c5, B:105:0x02d5, B:107:0x02e5, B:109:0x02eb, B:118:0x0321, B:123:0x033e, B:133:0x0361, B:135:0x0367, B:139:0x0375, B:141:0x037b, B:147:0x0386, B:150:0x0399, B:164:0x03cc, B:166:0x03d0, B:174:0x03eb, B:176:0x03f2, B:178:0x03fa, B:181:0x0428, B:190:0x049f, B:196:0x04c2, B:198:0x04e6, B:200:0x04fc, B:202:0x0500, B:207:0x050c, B:208:0x0512, B:212:0x051f, B:218:0x0567, B:219:0x056a, B:213:0x0535, B:215:0x053d, B:216:0x0551, B:220:0x0575, B:240:0x05f5, B:251:0x060e, B:253:0x062c, B:256:0x065d, B:258:0x0667, B:260:0x067d, B:262:0x068e, B:270:0x06b9, B:274:0x06dc, B:276:0x06fb, B:278:0x0726, B:280:0x0742, B:282:0x0752, B:286:0x0761, B:288:0x0767, B:293:0x0777, B:295:0x0789, B:297:0x079c, B:334:0x08c8, B:336:0x08ce, B:345:0x08e7, B:347:0x08ed, B:355:0x0900, B:358:0x090a, B:362:0x0915, B:376:0x0936, B:380:0x0946, B:382:0x0976, B:383:0x097e, B:386:0x0985, B:451:0x0a9b, B:453:0x0ae6, B:454:0x0aed, B:457:0x0af7, B:459:0x0afb, B:461:0x0b01, B:479:0x0b5f, B:502:0x0bbf, B:531:0x0c05, B:540:0x0c44, B:542:0x0c4c, B:545:0x0c54, B:547:0x0c5c, B:551:0x0c67, B:568:0x0cfb, B:572:0x0d0b, B:587:0x0d50, B:589:0x0d56, B:591:0x0d5a, B:593:0x0d65, B:595:0x0d6b, B:597:0x0d75, B:599:0x0d86, B:601:0x0d92, B:603:0x0db2, B:605:0x0dbc, B:607:0x0de8, B:608:0x0df5, B:612:0x0e14, B:614:0x0e1a, B:616:0x0e22, B:618:0x0e28, B:619:0x0e4a, B:575:0x0d1a, B:583:0x0d2f, B:585:0x0d3b, B:552:0x0c8d, B:553:0x0c92, B:554:0x0c95, B:556:0x0c9d, B:558:0x0ca6, B:560:0x0cae, B:564:0x0ce7, B:565:0x0cf0, B:534:0x0c0f, B:536:0x0c17, B:538:0x0c3f, B:586:0x0d42, B:512:0x0bd3, B:517:0x0be0, B:520:0x0bea, B:523:0x0bf3, B:482:0x0b68, B:484:0x0b75, B:477:0x0b5a, B:388:0x099a, B:393:0x09aa, B:397:0x09bc, B:396:0x09b7, B:398:0x09cc, B:400:0x09da, B:401:0x09e3, B:403:0x09eb, B:404:0x09fa, B:405:0x0a03, B:407:0x0a09, B:410:0x0a16, B:413:0x0a20, B:414:0x0a23, B:416:0x0a29, B:419:0x0a32, B:421:0x0a3b, B:424:0x0a43, B:426:0x0a49, B:428:0x0a4d, B:430:0x0a55, B:436:0x0a63, B:438:0x0a69, B:440:0x0a6d, B:442:0x0a75, B:446:0x0a7c, B:448:0x0a8b, B:450:0x0a91, B:296:0x0795, B:299:0x07c4, B:301:0x07d6, B:303:0x07e9, B:302:0x07e2, B:309:0x081d, B:311:0x0825, B:316:0x083d, B:315:0x0838, B:322:0x0879, B:324:0x0885, B:326:0x0898, B:325:0x0891, B:279:0x0733, B:263:0x069a, B:265:0x069e, B:222:0x0584, B:228:0x059a, B:234:0x05dd, B:237:0x05e3, B:229:0x05ae, B:231:0x05b4, B:232:0x05c8, B:184:0x0438, B:187:0x0444, B:188:0x045f, B:179:0x0407, B:170:0x03d8, B:172:0x03e3, B:160:0x03b6, B:161:0x03bd, B:162:0x03c4, B:137:0x036c, B:138:0x0371, B:111:0x0301, B:113:0x0307, B:87:0x0271, B:54:0x0137, B:56:0x013d, B:57:0x0143, B:60:0x014d, B:61:0x0157, B:62:0x0169, B:64:0x016f, B:65:0x0186, B:67:0x018d, B:69:0x0195, B:70:0x01c5, B:51:0x011e, B:72:0x020e, B:562:0x0cb8, B:370:0x0928), top: B:630:0x002c, inners: #1, #3 }] */
    /* JADX WARN: Code duplicated, block: B:204:0x0506  */
    /* JADX WARN: Code duplicated, block: B:224:0x0592  */
    /* JADX WARN: Code duplicated, block: B:225:0x0594 A[DONT_INVERT] */
    /* JADX WARN: Code duplicated, block: B:226:0x0596  */
    /* JADX WARN: Code duplicated, block: B:227:0x0598 A[ADDED_TO_REGION] */
    /* JADX WARN: Code duplicated, block: B:229:0x05ae A[Catch: Exception -> 0x0056, TryCatch #2 {Exception -> 0x0056, blocks: (B:13:0x002c, B:14:0x0038, B:16:0x0040, B:18:0x0051, B:19:0x0053, B:23:0x005a, B:25:0x0062, B:27:0x0074, B:28:0x0077, B:32:0x0080, B:35:0x0088, B:36:0x009e, B:38:0x00a6, B:39:0x00dc, B:41:0x00fe, B:44:0x0106, B:46:0x010e, B:49:0x0115, B:53:0x0129, B:71:0x01ee, B:73:0x0220, B:75:0x0232, B:78:0x023a, B:80:0x023e, B:82:0x025a, B:84:0x0261, B:88:0x0274, B:92:0x0280, B:94:0x028c, B:95:0x0292, B:97:0x029d, B:99:0x02a3, B:101:0x02af, B:102:0x02bb, B:103:0x02c5, B:105:0x02d5, B:107:0x02e5, B:109:0x02eb, B:118:0x0321, B:123:0x033e, B:133:0x0361, B:135:0x0367, B:139:0x0375, B:141:0x037b, B:147:0x0386, B:150:0x0399, B:164:0x03cc, B:166:0x03d0, B:174:0x03eb, B:176:0x03f2, B:178:0x03fa, B:181:0x0428, B:190:0x049f, B:196:0x04c2, B:198:0x04e6, B:200:0x04fc, B:202:0x0500, B:207:0x050c, B:208:0x0512, B:212:0x051f, B:218:0x0567, B:219:0x056a, B:213:0x0535, B:215:0x053d, B:216:0x0551, B:220:0x0575, B:240:0x05f5, B:251:0x060e, B:253:0x062c, B:256:0x065d, B:258:0x0667, B:260:0x067d, B:262:0x068e, B:270:0x06b9, B:274:0x06dc, B:276:0x06fb, B:278:0x0726, B:280:0x0742, B:282:0x0752, B:286:0x0761, B:288:0x0767, B:293:0x0777, B:295:0x0789, B:297:0x079c, B:334:0x08c8, B:336:0x08ce, B:345:0x08e7, B:347:0x08ed, B:355:0x0900, B:358:0x090a, B:362:0x0915, B:376:0x0936, B:380:0x0946, B:382:0x0976, B:383:0x097e, B:386:0x0985, B:451:0x0a9b, B:453:0x0ae6, B:454:0x0aed, B:457:0x0af7, B:459:0x0afb, B:461:0x0b01, B:479:0x0b5f, B:502:0x0bbf, B:531:0x0c05, B:540:0x0c44, B:542:0x0c4c, B:545:0x0c54, B:547:0x0c5c, B:551:0x0c67, B:568:0x0cfb, B:572:0x0d0b, B:587:0x0d50, B:589:0x0d56, B:591:0x0d5a, B:593:0x0d65, B:595:0x0d6b, B:597:0x0d75, B:599:0x0d86, B:601:0x0d92, B:603:0x0db2, B:605:0x0dbc, B:607:0x0de8, B:608:0x0df5, B:612:0x0e14, B:614:0x0e1a, B:616:0x0e22, B:618:0x0e28, B:619:0x0e4a, B:575:0x0d1a, B:583:0x0d2f, B:585:0x0d3b, B:552:0x0c8d, B:553:0x0c92, B:554:0x0c95, B:556:0x0c9d, B:558:0x0ca6, B:560:0x0cae, B:564:0x0ce7, B:565:0x0cf0, B:534:0x0c0f, B:536:0x0c17, B:538:0x0c3f, B:586:0x0d42, B:512:0x0bd3, B:517:0x0be0, B:520:0x0bea, B:523:0x0bf3, B:482:0x0b68, B:484:0x0b75, B:477:0x0b5a, B:388:0x099a, B:393:0x09aa, B:397:0x09bc, B:396:0x09b7, B:398:0x09cc, B:400:0x09da, B:401:0x09e3, B:403:0x09eb, B:404:0x09fa, B:405:0x0a03, B:407:0x0a09, B:410:0x0a16, B:413:0x0a20, B:414:0x0a23, B:416:0x0a29, B:419:0x0a32, B:421:0x0a3b, B:424:0x0a43, B:426:0x0a49, B:428:0x0a4d, B:430:0x0a55, B:436:0x0a63, B:438:0x0a69, B:440:0x0a6d, B:442:0x0a75, B:446:0x0a7c, B:448:0x0a8b, B:450:0x0a91, B:296:0x0795, B:299:0x07c4, B:301:0x07d6, B:303:0x07e9, B:302:0x07e2, B:309:0x081d, B:311:0x0825, B:316:0x083d, B:315:0x0838, B:322:0x0879, B:324:0x0885, B:326:0x0898, B:325:0x0891, B:279:0x0733, B:263:0x069a, B:265:0x069e, B:222:0x0584, B:228:0x059a, B:234:0x05dd, B:237:0x05e3, B:229:0x05ae, B:231:0x05b4, B:232:0x05c8, B:184:0x0438, B:187:0x0444, B:188:0x045f, B:179:0x0407, B:170:0x03d8, B:172:0x03e3, B:160:0x03b6, B:161:0x03bd, B:162:0x03c4, B:137:0x036c, B:138:0x0371, B:111:0x0301, B:113:0x0307, B:87:0x0271, B:54:0x0137, B:56:0x013d, B:57:0x0143, B:60:0x014d, B:61:0x0157, B:62:0x0169, B:64:0x016f, B:65:0x0186, B:67:0x018d, B:69:0x0195, B:70:0x01c5, B:51:0x011e, B:72:0x020e, B:562:0x0cb8, B:370:0x0928), top: B:630:0x002c, inners: #1, #3 }] */
    /* JADX WARN: Code duplicated, block: B:231:0x05b4 A[Catch: Exception -> 0x0056, TryCatch #2 {Exception -> 0x0056, blocks: (B:13:0x002c, B:14:0x0038, B:16:0x0040, B:18:0x0051, B:19:0x0053, B:23:0x005a, B:25:0x0062, B:27:0x0074, B:28:0x0077, B:32:0x0080, B:35:0x0088, B:36:0x009e, B:38:0x00a6, B:39:0x00dc, B:41:0x00fe, B:44:0x0106, B:46:0x010e, B:49:0x0115, B:53:0x0129, B:71:0x01ee, B:73:0x0220, B:75:0x0232, B:78:0x023a, B:80:0x023e, B:82:0x025a, B:84:0x0261, B:88:0x0274, B:92:0x0280, B:94:0x028c, B:95:0x0292, B:97:0x029d, B:99:0x02a3, B:101:0x02af, B:102:0x02bb, B:103:0x02c5, B:105:0x02d5, B:107:0x02e5, B:109:0x02eb, B:118:0x0321, B:123:0x033e, B:133:0x0361, B:135:0x0367, B:139:0x0375, B:141:0x037b, B:147:0x0386, B:150:0x0399, B:164:0x03cc, B:166:0x03d0, B:174:0x03eb, B:176:0x03f2, B:178:0x03fa, B:181:0x0428, B:190:0x049f, B:196:0x04c2, B:198:0x04e6, B:200:0x04fc, B:202:0x0500, B:207:0x050c, B:208:0x0512, B:212:0x051f, B:218:0x0567, B:219:0x056a, B:213:0x0535, B:215:0x053d, B:216:0x0551, B:220:0x0575, B:240:0x05f5, B:251:0x060e, B:253:0x062c, B:256:0x065d, B:258:0x0667, B:260:0x067d, B:262:0x068e, B:270:0x06b9, B:274:0x06dc, B:276:0x06fb, B:278:0x0726, B:280:0x0742, B:282:0x0752, B:286:0x0761, B:288:0x0767, B:293:0x0777, B:295:0x0789, B:297:0x079c, B:334:0x08c8, B:336:0x08ce, B:345:0x08e7, B:347:0x08ed, B:355:0x0900, B:358:0x090a, B:362:0x0915, B:376:0x0936, B:380:0x0946, B:382:0x0976, B:383:0x097e, B:386:0x0985, B:451:0x0a9b, B:453:0x0ae6, B:454:0x0aed, B:457:0x0af7, B:459:0x0afb, B:461:0x0b01, B:479:0x0b5f, B:502:0x0bbf, B:531:0x0c05, B:540:0x0c44, B:542:0x0c4c, B:545:0x0c54, B:547:0x0c5c, B:551:0x0c67, B:568:0x0cfb, B:572:0x0d0b, B:587:0x0d50, B:589:0x0d56, B:591:0x0d5a, B:593:0x0d65, B:595:0x0d6b, B:597:0x0d75, B:599:0x0d86, B:601:0x0d92, B:603:0x0db2, B:605:0x0dbc, B:607:0x0de8, B:608:0x0df5, B:612:0x0e14, B:614:0x0e1a, B:616:0x0e22, B:618:0x0e28, B:619:0x0e4a, B:575:0x0d1a, B:583:0x0d2f, B:585:0x0d3b, B:552:0x0c8d, B:553:0x0c92, B:554:0x0c95, B:556:0x0c9d, B:558:0x0ca6, B:560:0x0cae, B:564:0x0ce7, B:565:0x0cf0, B:534:0x0c0f, B:536:0x0c17, B:538:0x0c3f, B:586:0x0d42, B:512:0x0bd3, B:517:0x0be0, B:520:0x0bea, B:523:0x0bf3, B:482:0x0b68, B:484:0x0b75, B:477:0x0b5a, B:388:0x099a, B:393:0x09aa, B:397:0x09bc, B:396:0x09b7, B:398:0x09cc, B:400:0x09da, B:401:0x09e3, B:403:0x09eb, B:404:0x09fa, B:405:0x0a03, B:407:0x0a09, B:410:0x0a16, B:413:0x0a20, B:414:0x0a23, B:416:0x0a29, B:419:0x0a32, B:421:0x0a3b, B:424:0x0a43, B:426:0x0a49, B:428:0x0a4d, B:430:0x0a55, B:436:0x0a63, B:438:0x0a69, B:440:0x0a6d, B:442:0x0a75, B:446:0x0a7c, B:448:0x0a8b, B:450:0x0a91, B:296:0x0795, B:299:0x07c4, B:301:0x07d6, B:303:0x07e9, B:302:0x07e2, B:309:0x081d, B:311:0x0825, B:316:0x083d, B:315:0x0838, B:322:0x0879, B:324:0x0885, B:326:0x0898, B:325:0x0891, B:279:0x0733, B:263:0x069a, B:265:0x069e, B:222:0x0584, B:228:0x059a, B:234:0x05dd, B:237:0x05e3, B:229:0x05ae, B:231:0x05b4, B:232:0x05c8, B:184:0x0438, B:187:0x0444, B:188:0x045f, B:179:0x0407, B:170:0x03d8, B:172:0x03e3, B:160:0x03b6, B:161:0x03bd, B:162:0x03c4, B:137:0x036c, B:138:0x0371, B:111:0x0301, B:113:0x0307, B:87:0x0271, B:54:0x0137, B:56:0x013d, B:57:0x0143, B:60:0x014d, B:61:0x0157, B:62:0x0169, B:64:0x016f, B:65:0x0186, B:67:0x018d, B:69:0x0195, B:70:0x01c5, B:51:0x011e, B:72:0x020e, B:562:0x0cb8, B:370:0x0928), top: B:630:0x002c, inners: #1, #3 }] */
    /* JADX WARN: Code duplicated, block: B:232:0x05c8 A[Catch: Exception -> 0x0056, TryCatch #2 {Exception -> 0x0056, blocks: (B:13:0x002c, B:14:0x0038, B:16:0x0040, B:18:0x0051, B:19:0x0053, B:23:0x005a, B:25:0x0062, B:27:0x0074, B:28:0x0077, B:32:0x0080, B:35:0x0088, B:36:0x009e, B:38:0x00a6, B:39:0x00dc, B:41:0x00fe, B:44:0x0106, B:46:0x010e, B:49:0x0115, B:53:0x0129, B:71:0x01ee, B:73:0x0220, B:75:0x0232, B:78:0x023a, B:80:0x023e, B:82:0x025a, B:84:0x0261, B:88:0x0274, B:92:0x0280, B:94:0x028c, B:95:0x0292, B:97:0x029d, B:99:0x02a3, B:101:0x02af, B:102:0x02bb, B:103:0x02c5, B:105:0x02d5, B:107:0x02e5, B:109:0x02eb, B:118:0x0321, B:123:0x033e, B:133:0x0361, B:135:0x0367, B:139:0x0375, B:141:0x037b, B:147:0x0386, B:150:0x0399, B:164:0x03cc, B:166:0x03d0, B:174:0x03eb, B:176:0x03f2, B:178:0x03fa, B:181:0x0428, B:190:0x049f, B:196:0x04c2, B:198:0x04e6, B:200:0x04fc, B:202:0x0500, B:207:0x050c, B:208:0x0512, B:212:0x051f, B:218:0x0567, B:219:0x056a, B:213:0x0535, B:215:0x053d, B:216:0x0551, B:220:0x0575, B:240:0x05f5, B:251:0x060e, B:253:0x062c, B:256:0x065d, B:258:0x0667, B:260:0x067d, B:262:0x068e, B:270:0x06b9, B:274:0x06dc, B:276:0x06fb, B:278:0x0726, B:280:0x0742, B:282:0x0752, B:286:0x0761, B:288:0x0767, B:293:0x0777, B:295:0x0789, B:297:0x079c, B:334:0x08c8, B:336:0x08ce, B:345:0x08e7, B:347:0x08ed, B:355:0x0900, B:358:0x090a, B:362:0x0915, B:376:0x0936, B:380:0x0946, B:382:0x0976, B:383:0x097e, B:386:0x0985, B:451:0x0a9b, B:453:0x0ae6, B:454:0x0aed, B:457:0x0af7, B:459:0x0afb, B:461:0x0b01, B:479:0x0b5f, B:502:0x0bbf, B:531:0x0c05, B:540:0x0c44, B:542:0x0c4c, B:545:0x0c54, B:547:0x0c5c, B:551:0x0c67, B:568:0x0cfb, B:572:0x0d0b, B:587:0x0d50, B:589:0x0d56, B:591:0x0d5a, B:593:0x0d65, B:595:0x0d6b, B:597:0x0d75, B:599:0x0d86, B:601:0x0d92, B:603:0x0db2, B:605:0x0dbc, B:607:0x0de8, B:608:0x0df5, B:612:0x0e14, B:614:0x0e1a, B:616:0x0e22, B:618:0x0e28, B:619:0x0e4a, B:575:0x0d1a, B:583:0x0d2f, B:585:0x0d3b, B:552:0x0c8d, B:553:0x0c92, B:554:0x0c95, B:556:0x0c9d, B:558:0x0ca6, B:560:0x0cae, B:564:0x0ce7, B:565:0x0cf0, B:534:0x0c0f, B:536:0x0c17, B:538:0x0c3f, B:586:0x0d42, B:512:0x0bd3, B:517:0x0be0, B:520:0x0bea, B:523:0x0bf3, B:482:0x0b68, B:484:0x0b75, B:477:0x0b5a, B:388:0x099a, B:393:0x09aa, B:397:0x09bc, B:396:0x09b7, B:398:0x09cc, B:400:0x09da, B:401:0x09e3, B:403:0x09eb, B:404:0x09fa, B:405:0x0a03, B:407:0x0a09, B:410:0x0a16, B:413:0x0a20, B:414:0x0a23, B:416:0x0a29, B:419:0x0a32, B:421:0x0a3b, B:424:0x0a43, B:426:0x0a49, B:428:0x0a4d, B:430:0x0a55, B:436:0x0a63, B:438:0x0a69, B:440:0x0a6d, B:442:0x0a75, B:446:0x0a7c, B:448:0x0a8b, B:450:0x0a91, B:296:0x0795, B:299:0x07c4, B:301:0x07d6, B:303:0x07e9, B:302:0x07e2, B:309:0x081d, B:311:0x0825, B:316:0x083d, B:315:0x0838, B:322:0x0879, B:324:0x0885, B:326:0x0898, B:325:0x0891, B:279:0x0733, B:263:0x069a, B:265:0x069e, B:222:0x0584, B:228:0x059a, B:234:0x05dd, B:237:0x05e3, B:229:0x05ae, B:231:0x05b4, B:232:0x05c8, B:184:0x0438, B:187:0x0444, B:188:0x045f, B:179:0x0407, B:170:0x03d8, B:172:0x03e3, B:160:0x03b6, B:161:0x03bd, B:162:0x03c4, B:137:0x036c, B:138:0x0371, B:111:0x0301, B:113:0x0307, B:87:0x0271, B:54:0x0137, B:56:0x013d, B:57:0x0143, B:60:0x014d, B:61:0x0157, B:62:0x0169, B:64:0x016f, B:65:0x0186, B:67:0x018d, B:69:0x0195, B:70:0x01c5, B:51:0x011e, B:72:0x020e, B:562:0x0cb8, B:370:0x0928), top: B:630:0x002c, inners: #1, #3 }] */
    /* JADX WARN: Code duplicated, block: B:233:0x05dc  */
    /* JADX WARN: Code duplicated, block: B:236:0x05e2  */
    /* JADX WARN: Code duplicated, block: B:239:0x05f3 A[ADDED_TO_REGION] */
    /* JADX WARN: Code duplicated, block: B:246:0x0605  */
    /* JADX WARN: Code duplicated, block: B:248:0x0608  */
    /* JADX WARN: Code duplicated, block: B:267:0x06af  */
    /* JADX WARN: Code duplicated, block: B:270:0x06b9 A[Catch: Exception -> 0x0056, TryCatch #2 {Exception -> 0x0056, blocks: (B:13:0x002c, B:14:0x0038, B:16:0x0040, B:18:0x0051, B:19:0x0053, B:23:0x005a, B:25:0x0062, B:27:0x0074, B:28:0x0077, B:32:0x0080, B:35:0x0088, B:36:0x009e, B:38:0x00a6, B:39:0x00dc, B:41:0x00fe, B:44:0x0106, B:46:0x010e, B:49:0x0115, B:53:0x0129, B:71:0x01ee, B:73:0x0220, B:75:0x0232, B:78:0x023a, B:80:0x023e, B:82:0x025a, B:84:0x0261, B:88:0x0274, B:92:0x0280, B:94:0x028c, B:95:0x0292, B:97:0x029d, B:99:0x02a3, B:101:0x02af, B:102:0x02bb, B:103:0x02c5, B:105:0x02d5, B:107:0x02e5, B:109:0x02eb, B:118:0x0321, B:123:0x033e, B:133:0x0361, B:135:0x0367, B:139:0x0375, B:141:0x037b, B:147:0x0386, B:150:0x0399, B:164:0x03cc, B:166:0x03d0, B:174:0x03eb, B:176:0x03f2, B:178:0x03fa, B:181:0x0428, B:190:0x049f, B:196:0x04c2, B:198:0x04e6, B:200:0x04fc, B:202:0x0500, B:207:0x050c, B:208:0x0512, B:212:0x051f, B:218:0x0567, B:219:0x056a, B:213:0x0535, B:215:0x053d, B:216:0x0551, B:220:0x0575, B:240:0x05f5, B:251:0x060e, B:253:0x062c, B:256:0x065d, B:258:0x0667, B:260:0x067d, B:262:0x068e, B:270:0x06b9, B:274:0x06dc, B:276:0x06fb, B:278:0x0726, B:280:0x0742, B:282:0x0752, B:286:0x0761, B:288:0x0767, B:293:0x0777, B:295:0x0789, B:297:0x079c, B:334:0x08c8, B:336:0x08ce, B:345:0x08e7, B:347:0x08ed, B:355:0x0900, B:358:0x090a, B:362:0x0915, B:376:0x0936, B:380:0x0946, B:382:0x0976, B:383:0x097e, B:386:0x0985, B:451:0x0a9b, B:453:0x0ae6, B:454:0x0aed, B:457:0x0af7, B:459:0x0afb, B:461:0x0b01, B:479:0x0b5f, B:502:0x0bbf, B:531:0x0c05, B:540:0x0c44, B:542:0x0c4c, B:545:0x0c54, B:547:0x0c5c, B:551:0x0c67, B:568:0x0cfb, B:572:0x0d0b, B:587:0x0d50, B:589:0x0d56, B:591:0x0d5a, B:593:0x0d65, B:595:0x0d6b, B:597:0x0d75, B:599:0x0d86, B:601:0x0d92, B:603:0x0db2, B:605:0x0dbc, B:607:0x0de8, B:608:0x0df5, B:612:0x0e14, B:614:0x0e1a, B:616:0x0e22, B:618:0x0e28, B:619:0x0e4a, B:575:0x0d1a, B:583:0x0d2f, B:585:0x0d3b, B:552:0x0c8d, B:553:0x0c92, B:554:0x0c95, B:556:0x0c9d, B:558:0x0ca6, B:560:0x0cae, B:564:0x0ce7, B:565:0x0cf0, B:534:0x0c0f, B:536:0x0c17, B:538:0x0c3f, B:586:0x0d42, B:512:0x0bd3, B:517:0x0be0, B:520:0x0bea, B:523:0x0bf3, B:482:0x0b68, B:484:0x0b75, B:477:0x0b5a, B:388:0x099a, B:393:0x09aa, B:397:0x09bc, B:396:0x09b7, B:398:0x09cc, B:400:0x09da, B:401:0x09e3, B:403:0x09eb, B:404:0x09fa, B:405:0x0a03, B:407:0x0a09, B:410:0x0a16, B:413:0x0a20, B:414:0x0a23, B:416:0x0a29, B:419:0x0a32, B:421:0x0a3b, B:424:0x0a43, B:426:0x0a49, B:428:0x0a4d, B:430:0x0a55, B:436:0x0a63, B:438:0x0a69, B:440:0x0a6d, B:442:0x0a75, B:446:0x0a7c, B:448:0x0a8b, B:450:0x0a91, B:296:0x0795, B:299:0x07c4, B:301:0x07d6, B:303:0x07e9, B:302:0x07e2, B:309:0x081d, B:311:0x0825, B:316:0x083d, B:315:0x0838, B:322:0x0879, B:324:0x0885, B:326:0x0898, B:325:0x0891, B:279:0x0733, B:263:0x069a, B:265:0x069e, B:222:0x0584, B:228:0x059a, B:234:0x05dd, B:237:0x05e3, B:229:0x05ae, B:231:0x05b4, B:232:0x05c8, B:184:0x0438, B:187:0x0444, B:188:0x045f, B:179:0x0407, B:170:0x03d8, B:172:0x03e3, B:160:0x03b6, B:161:0x03bd, B:162:0x03c4, B:137:0x036c, B:138:0x0371, B:111:0x0301, B:113:0x0307, B:87:0x0271, B:54:0x0137, B:56:0x013d, B:57:0x0143, B:60:0x014d, B:61:0x0157, B:62:0x0169, B:64:0x016f, B:65:0x0186, B:67:0x018d, B:69:0x0195, B:70:0x01c5, B:51:0x011e, B:72:0x020e, B:562:0x0cb8, B:370:0x0928), top: B:630:0x002c, inners: #1, #3 }] */
    /* JADX WARN: Code duplicated, block: B:272:0x06d8  */
    /* JADX WARN: Code duplicated, block: B:273:0x06da  */
    /* JADX WARN: Code duplicated, block: B:276:0x06fb A[Catch: Exception -> 0x0056, TryCatch #2 {Exception -> 0x0056, blocks: (B:13:0x002c, B:14:0x0038, B:16:0x0040, B:18:0x0051, B:19:0x0053, B:23:0x005a, B:25:0x0062, B:27:0x0074, B:28:0x0077, B:32:0x0080, B:35:0x0088, B:36:0x009e, B:38:0x00a6, B:39:0x00dc, B:41:0x00fe, B:44:0x0106, B:46:0x010e, B:49:0x0115, B:53:0x0129, B:71:0x01ee, B:73:0x0220, B:75:0x0232, B:78:0x023a, B:80:0x023e, B:82:0x025a, B:84:0x0261, B:88:0x0274, B:92:0x0280, B:94:0x028c, B:95:0x0292, B:97:0x029d, B:99:0x02a3, B:101:0x02af, B:102:0x02bb, B:103:0x02c5, B:105:0x02d5, B:107:0x02e5, B:109:0x02eb, B:118:0x0321, B:123:0x033e, B:133:0x0361, B:135:0x0367, B:139:0x0375, B:141:0x037b, B:147:0x0386, B:150:0x0399, B:164:0x03cc, B:166:0x03d0, B:174:0x03eb, B:176:0x03f2, B:178:0x03fa, B:181:0x0428, B:190:0x049f, B:196:0x04c2, B:198:0x04e6, B:200:0x04fc, B:202:0x0500, B:207:0x050c, B:208:0x0512, B:212:0x051f, B:218:0x0567, B:219:0x056a, B:213:0x0535, B:215:0x053d, B:216:0x0551, B:220:0x0575, B:240:0x05f5, B:251:0x060e, B:253:0x062c, B:256:0x065d, B:258:0x0667, B:260:0x067d, B:262:0x068e, B:270:0x06b9, B:274:0x06dc, B:276:0x06fb, B:278:0x0726, B:280:0x0742, B:282:0x0752, B:286:0x0761, B:288:0x0767, B:293:0x0777, B:295:0x0789, B:297:0x079c, B:334:0x08c8, B:336:0x08ce, B:345:0x08e7, B:347:0x08ed, B:355:0x0900, B:358:0x090a, B:362:0x0915, B:376:0x0936, B:380:0x0946, B:382:0x0976, B:383:0x097e, B:386:0x0985, B:451:0x0a9b, B:453:0x0ae6, B:454:0x0aed, B:457:0x0af7, B:459:0x0afb, B:461:0x0b01, B:479:0x0b5f, B:502:0x0bbf, B:531:0x0c05, B:540:0x0c44, B:542:0x0c4c, B:545:0x0c54, B:547:0x0c5c, B:551:0x0c67, B:568:0x0cfb, B:572:0x0d0b, B:587:0x0d50, B:589:0x0d56, B:591:0x0d5a, B:593:0x0d65, B:595:0x0d6b, B:597:0x0d75, B:599:0x0d86, B:601:0x0d92, B:603:0x0db2, B:605:0x0dbc, B:607:0x0de8, B:608:0x0df5, B:612:0x0e14, B:614:0x0e1a, B:616:0x0e22, B:618:0x0e28, B:619:0x0e4a, B:575:0x0d1a, B:583:0x0d2f, B:585:0x0d3b, B:552:0x0c8d, B:553:0x0c92, B:554:0x0c95, B:556:0x0c9d, B:558:0x0ca6, B:560:0x0cae, B:564:0x0ce7, B:565:0x0cf0, B:534:0x0c0f, B:536:0x0c17, B:538:0x0c3f, B:586:0x0d42, B:512:0x0bd3, B:517:0x0be0, B:520:0x0bea, B:523:0x0bf3, B:482:0x0b68, B:484:0x0b75, B:477:0x0b5a, B:388:0x099a, B:393:0x09aa, B:397:0x09bc, B:396:0x09b7, B:398:0x09cc, B:400:0x09da, B:401:0x09e3, B:403:0x09eb, B:404:0x09fa, B:405:0x0a03, B:407:0x0a09, B:410:0x0a16, B:413:0x0a20, B:414:0x0a23, B:416:0x0a29, B:419:0x0a32, B:421:0x0a3b, B:424:0x0a43, B:426:0x0a49, B:428:0x0a4d, B:430:0x0a55, B:436:0x0a63, B:438:0x0a69, B:440:0x0a6d, B:442:0x0a75, B:446:0x0a7c, B:448:0x0a8b, B:450:0x0a91, B:296:0x0795, B:299:0x07c4, B:301:0x07d6, B:303:0x07e9, B:302:0x07e2, B:309:0x081d, B:311:0x0825, B:316:0x083d, B:315:0x0838, B:322:0x0879, B:324:0x0885, B:326:0x0898, B:325:0x0891, B:279:0x0733, B:263:0x069a, B:265:0x069e, B:222:0x0584, B:228:0x059a, B:234:0x05dd, B:237:0x05e3, B:229:0x05ae, B:231:0x05b4, B:232:0x05c8, B:184:0x0438, B:187:0x0444, B:188:0x045f, B:179:0x0407, B:170:0x03d8, B:172:0x03e3, B:160:0x03b6, B:161:0x03bd, B:162:0x03c4, B:137:0x036c, B:138:0x0371, B:111:0x0301, B:113:0x0307, B:87:0x0271, B:54:0x0137, B:56:0x013d, B:57:0x0143, B:60:0x014d, B:61:0x0157, B:62:0x0169, B:64:0x016f, B:65:0x0186, B:67:0x018d, B:69:0x0195, B:70:0x01c5, B:51:0x011e, B:72:0x020e, B:562:0x0cb8, B:370:0x0928), top: B:630:0x002c, inners: #1, #3 }] */
    /* JADX WARN: Code duplicated, block: B:278:0x0726 A[Catch: Exception -> 0x0056, TryCatch #2 {Exception -> 0x0056, blocks: (B:13:0x002c, B:14:0x0038, B:16:0x0040, B:18:0x0051, B:19:0x0053, B:23:0x005a, B:25:0x0062, B:27:0x0074, B:28:0x0077, B:32:0x0080, B:35:0x0088, B:36:0x009e, B:38:0x00a6, B:39:0x00dc, B:41:0x00fe, B:44:0x0106, B:46:0x010e, B:49:0x0115, B:53:0x0129, B:71:0x01ee, B:73:0x0220, B:75:0x0232, B:78:0x023a, B:80:0x023e, B:82:0x025a, B:84:0x0261, B:88:0x0274, B:92:0x0280, B:94:0x028c, B:95:0x0292, B:97:0x029d, B:99:0x02a3, B:101:0x02af, B:102:0x02bb, B:103:0x02c5, B:105:0x02d5, B:107:0x02e5, B:109:0x02eb, B:118:0x0321, B:123:0x033e, B:133:0x0361, B:135:0x0367, B:139:0x0375, B:141:0x037b, B:147:0x0386, B:150:0x0399, B:164:0x03cc, B:166:0x03d0, B:174:0x03eb, B:176:0x03f2, B:178:0x03fa, B:181:0x0428, B:190:0x049f, B:196:0x04c2, B:198:0x04e6, B:200:0x04fc, B:202:0x0500, B:207:0x050c, B:208:0x0512, B:212:0x051f, B:218:0x0567, B:219:0x056a, B:213:0x0535, B:215:0x053d, B:216:0x0551, B:220:0x0575, B:240:0x05f5, B:251:0x060e, B:253:0x062c, B:256:0x065d, B:258:0x0667, B:260:0x067d, B:262:0x068e, B:270:0x06b9, B:274:0x06dc, B:276:0x06fb, B:278:0x0726, B:280:0x0742, B:282:0x0752, B:286:0x0761, B:288:0x0767, B:293:0x0777, B:295:0x0789, B:297:0x079c, B:334:0x08c8, B:336:0x08ce, B:345:0x08e7, B:347:0x08ed, B:355:0x0900, B:358:0x090a, B:362:0x0915, B:376:0x0936, B:380:0x0946, B:382:0x0976, B:383:0x097e, B:386:0x0985, B:451:0x0a9b, B:453:0x0ae6, B:454:0x0aed, B:457:0x0af7, B:459:0x0afb, B:461:0x0b01, B:479:0x0b5f, B:502:0x0bbf, B:531:0x0c05, B:540:0x0c44, B:542:0x0c4c, B:545:0x0c54, B:547:0x0c5c, B:551:0x0c67, B:568:0x0cfb, B:572:0x0d0b, B:587:0x0d50, B:589:0x0d56, B:591:0x0d5a, B:593:0x0d65, B:595:0x0d6b, B:597:0x0d75, B:599:0x0d86, B:601:0x0d92, B:603:0x0db2, B:605:0x0dbc, B:607:0x0de8, B:608:0x0df5, B:612:0x0e14, B:614:0x0e1a, B:616:0x0e22, B:618:0x0e28, B:619:0x0e4a, B:575:0x0d1a, B:583:0x0d2f, B:585:0x0d3b, B:552:0x0c8d, B:553:0x0c92, B:554:0x0c95, B:556:0x0c9d, B:558:0x0ca6, B:560:0x0cae, B:564:0x0ce7, B:565:0x0cf0, B:534:0x0c0f, B:536:0x0c17, B:538:0x0c3f, B:586:0x0d42, B:512:0x0bd3, B:517:0x0be0, B:520:0x0bea, B:523:0x0bf3, B:482:0x0b68, B:484:0x0b75, B:477:0x0b5a, B:388:0x099a, B:393:0x09aa, B:397:0x09bc, B:396:0x09b7, B:398:0x09cc, B:400:0x09da, B:401:0x09e3, B:403:0x09eb, B:404:0x09fa, B:405:0x0a03, B:407:0x0a09, B:410:0x0a16, B:413:0x0a20, B:414:0x0a23, B:416:0x0a29, B:419:0x0a32, B:421:0x0a3b, B:424:0x0a43, B:426:0x0a49, B:428:0x0a4d, B:430:0x0a55, B:436:0x0a63, B:438:0x0a69, B:440:0x0a6d, B:442:0x0a75, B:446:0x0a7c, B:448:0x0a8b, B:450:0x0a91, B:296:0x0795, B:299:0x07c4, B:301:0x07d6, B:303:0x07e9, B:302:0x07e2, B:309:0x081d, B:311:0x0825, B:316:0x083d, B:315:0x0838, B:322:0x0879, B:324:0x0885, B:326:0x0898, B:325:0x0891, B:279:0x0733, B:263:0x069a, B:265:0x069e, B:222:0x0584, B:228:0x059a, B:234:0x05dd, B:237:0x05e3, B:229:0x05ae, B:231:0x05b4, B:232:0x05c8, B:184:0x0438, B:187:0x0444, B:188:0x045f, B:179:0x0407, B:170:0x03d8, B:172:0x03e3, B:160:0x03b6, B:161:0x03bd, B:162:0x03c4, B:137:0x036c, B:138:0x0371, B:111:0x0301, B:113:0x0307, B:87:0x0271, B:54:0x0137, B:56:0x013d, B:57:0x0143, B:60:0x014d, B:61:0x0157, B:62:0x0169, B:64:0x016f, B:65:0x0186, B:67:0x018d, B:69:0x0195, B:70:0x01c5, B:51:0x011e, B:72:0x020e, B:562:0x0cb8, B:370:0x0928), top: B:630:0x002c, inners: #1, #3 }] */
    /* JADX WARN: Code duplicated, block: B:279:0x0733 A[Catch: Exception -> 0x0056, TryCatch #2 {Exception -> 0x0056, blocks: (B:13:0x002c, B:14:0x0038, B:16:0x0040, B:18:0x0051, B:19:0x0053, B:23:0x005a, B:25:0x0062, B:27:0x0074, B:28:0x0077, B:32:0x0080, B:35:0x0088, B:36:0x009e, B:38:0x00a6, B:39:0x00dc, B:41:0x00fe, B:44:0x0106, B:46:0x010e, B:49:0x0115, B:53:0x0129, B:71:0x01ee, B:73:0x0220, B:75:0x0232, B:78:0x023a, B:80:0x023e, B:82:0x025a, B:84:0x0261, B:88:0x0274, B:92:0x0280, B:94:0x028c, B:95:0x0292, B:97:0x029d, B:99:0x02a3, B:101:0x02af, B:102:0x02bb, B:103:0x02c5, B:105:0x02d5, B:107:0x02e5, B:109:0x02eb, B:118:0x0321, B:123:0x033e, B:133:0x0361, B:135:0x0367, B:139:0x0375, B:141:0x037b, B:147:0x0386, B:150:0x0399, B:164:0x03cc, B:166:0x03d0, B:174:0x03eb, B:176:0x03f2, B:178:0x03fa, B:181:0x0428, B:190:0x049f, B:196:0x04c2, B:198:0x04e6, B:200:0x04fc, B:202:0x0500, B:207:0x050c, B:208:0x0512, B:212:0x051f, B:218:0x0567, B:219:0x056a, B:213:0x0535, B:215:0x053d, B:216:0x0551, B:220:0x0575, B:240:0x05f5, B:251:0x060e, B:253:0x062c, B:256:0x065d, B:258:0x0667, B:260:0x067d, B:262:0x068e, B:270:0x06b9, B:274:0x06dc, B:276:0x06fb, B:278:0x0726, B:280:0x0742, B:282:0x0752, B:286:0x0761, B:288:0x0767, B:293:0x0777, B:295:0x0789, B:297:0x079c, B:334:0x08c8, B:336:0x08ce, B:345:0x08e7, B:347:0x08ed, B:355:0x0900, B:358:0x090a, B:362:0x0915, B:376:0x0936, B:380:0x0946, B:382:0x0976, B:383:0x097e, B:386:0x0985, B:451:0x0a9b, B:453:0x0ae6, B:454:0x0aed, B:457:0x0af7, B:459:0x0afb, B:461:0x0b01, B:479:0x0b5f, B:502:0x0bbf, B:531:0x0c05, B:540:0x0c44, B:542:0x0c4c, B:545:0x0c54, B:547:0x0c5c, B:551:0x0c67, B:568:0x0cfb, B:572:0x0d0b, B:587:0x0d50, B:589:0x0d56, B:591:0x0d5a, B:593:0x0d65, B:595:0x0d6b, B:597:0x0d75, B:599:0x0d86, B:601:0x0d92, B:603:0x0db2, B:605:0x0dbc, B:607:0x0de8, B:608:0x0df5, B:612:0x0e14, B:614:0x0e1a, B:616:0x0e22, B:618:0x0e28, B:619:0x0e4a, B:575:0x0d1a, B:583:0x0d2f, B:585:0x0d3b, B:552:0x0c8d, B:553:0x0c92, B:554:0x0c95, B:556:0x0c9d, B:558:0x0ca6, B:560:0x0cae, B:564:0x0ce7, B:565:0x0cf0, B:534:0x0c0f, B:536:0x0c17, B:538:0x0c3f, B:586:0x0d42, B:512:0x0bd3, B:517:0x0be0, B:520:0x0bea, B:523:0x0bf3, B:482:0x0b68, B:484:0x0b75, B:477:0x0b5a, B:388:0x099a, B:393:0x09aa, B:397:0x09bc, B:396:0x09b7, B:398:0x09cc, B:400:0x09da, B:401:0x09e3, B:403:0x09eb, B:404:0x09fa, B:405:0x0a03, B:407:0x0a09, B:410:0x0a16, B:413:0x0a20, B:414:0x0a23, B:416:0x0a29, B:419:0x0a32, B:421:0x0a3b, B:424:0x0a43, B:426:0x0a49, B:428:0x0a4d, B:430:0x0a55, B:436:0x0a63, B:438:0x0a69, B:440:0x0a6d, B:442:0x0a75, B:446:0x0a7c, B:448:0x0a8b, B:450:0x0a91, B:296:0x0795, B:299:0x07c4, B:301:0x07d6, B:303:0x07e9, B:302:0x07e2, B:309:0x081d, B:311:0x0825, B:316:0x083d, B:315:0x0838, B:322:0x0879, B:324:0x0885, B:326:0x0898, B:325:0x0891, B:279:0x0733, B:263:0x069a, B:265:0x069e, B:222:0x0584, B:228:0x059a, B:234:0x05dd, B:237:0x05e3, B:229:0x05ae, B:231:0x05b4, B:232:0x05c8, B:184:0x0438, B:187:0x0444, B:188:0x045f, B:179:0x0407, B:170:0x03d8, B:172:0x03e3, B:160:0x03b6, B:161:0x03bd, B:162:0x03c4, B:137:0x036c, B:138:0x0371, B:111:0x0301, B:113:0x0307, B:87:0x0271, B:54:0x0137, B:56:0x013d, B:57:0x0143, B:60:0x014d, B:61:0x0157, B:62:0x0169, B:64:0x016f, B:65:0x0186, B:67:0x018d, B:69:0x0195, B:70:0x01c5, B:51:0x011e, B:72:0x020e, B:562:0x0cb8, B:370:0x0928), top: B:630:0x002c, inners: #1, #3 }] */
    /* JADX WARN: Code duplicated, block: B:282:0x0752 A[Catch: Exception -> 0x0056, TryCatch #2 {Exception -> 0x0056, blocks: (B:13:0x002c, B:14:0x0038, B:16:0x0040, B:18:0x0051, B:19:0x0053, B:23:0x005a, B:25:0x0062, B:27:0x0074, B:28:0x0077, B:32:0x0080, B:35:0x0088, B:36:0x009e, B:38:0x00a6, B:39:0x00dc, B:41:0x00fe, B:44:0x0106, B:46:0x010e, B:49:0x0115, B:53:0x0129, B:71:0x01ee, B:73:0x0220, B:75:0x0232, B:78:0x023a, B:80:0x023e, B:82:0x025a, B:84:0x0261, B:88:0x0274, B:92:0x0280, B:94:0x028c, B:95:0x0292, B:97:0x029d, B:99:0x02a3, B:101:0x02af, B:102:0x02bb, B:103:0x02c5, B:105:0x02d5, B:107:0x02e5, B:109:0x02eb, B:118:0x0321, B:123:0x033e, B:133:0x0361, B:135:0x0367, B:139:0x0375, B:141:0x037b, B:147:0x0386, B:150:0x0399, B:164:0x03cc, B:166:0x03d0, B:174:0x03eb, B:176:0x03f2, B:178:0x03fa, B:181:0x0428, B:190:0x049f, B:196:0x04c2, B:198:0x04e6, B:200:0x04fc, B:202:0x0500, B:207:0x050c, B:208:0x0512, B:212:0x051f, B:218:0x0567, B:219:0x056a, B:213:0x0535, B:215:0x053d, B:216:0x0551, B:220:0x0575, B:240:0x05f5, B:251:0x060e, B:253:0x062c, B:256:0x065d, B:258:0x0667, B:260:0x067d, B:262:0x068e, B:270:0x06b9, B:274:0x06dc, B:276:0x06fb, B:278:0x0726, B:280:0x0742, B:282:0x0752, B:286:0x0761, B:288:0x0767, B:293:0x0777, B:295:0x0789, B:297:0x079c, B:334:0x08c8, B:336:0x08ce, B:345:0x08e7, B:347:0x08ed, B:355:0x0900, B:358:0x090a, B:362:0x0915, B:376:0x0936, B:380:0x0946, B:382:0x0976, B:383:0x097e, B:386:0x0985, B:451:0x0a9b, B:453:0x0ae6, B:454:0x0aed, B:457:0x0af7, B:459:0x0afb, B:461:0x0b01, B:479:0x0b5f, B:502:0x0bbf, B:531:0x0c05, B:540:0x0c44, B:542:0x0c4c, B:545:0x0c54, B:547:0x0c5c, B:551:0x0c67, B:568:0x0cfb, B:572:0x0d0b, B:587:0x0d50, B:589:0x0d56, B:591:0x0d5a, B:593:0x0d65, B:595:0x0d6b, B:597:0x0d75, B:599:0x0d86, B:601:0x0d92, B:603:0x0db2, B:605:0x0dbc, B:607:0x0de8, B:608:0x0df5, B:612:0x0e14, B:614:0x0e1a, B:616:0x0e22, B:618:0x0e28, B:619:0x0e4a, B:575:0x0d1a, B:583:0x0d2f, B:585:0x0d3b, B:552:0x0c8d, B:553:0x0c92, B:554:0x0c95, B:556:0x0c9d, B:558:0x0ca6, B:560:0x0cae, B:564:0x0ce7, B:565:0x0cf0, B:534:0x0c0f, B:536:0x0c17, B:538:0x0c3f, B:586:0x0d42, B:512:0x0bd3, B:517:0x0be0, B:520:0x0bea, B:523:0x0bf3, B:482:0x0b68, B:484:0x0b75, B:477:0x0b5a, B:388:0x099a, B:393:0x09aa, B:397:0x09bc, B:396:0x09b7, B:398:0x09cc, B:400:0x09da, B:401:0x09e3, B:403:0x09eb, B:404:0x09fa, B:405:0x0a03, B:407:0x0a09, B:410:0x0a16, B:413:0x0a20, B:414:0x0a23, B:416:0x0a29, B:419:0x0a32, B:421:0x0a3b, B:424:0x0a43, B:426:0x0a49, B:428:0x0a4d, B:430:0x0a55, B:436:0x0a63, B:438:0x0a69, B:440:0x0a6d, B:442:0x0a75, B:446:0x0a7c, B:448:0x0a8b, B:450:0x0a91, B:296:0x0795, B:299:0x07c4, B:301:0x07d6, B:303:0x07e9, B:302:0x07e2, B:309:0x081d, B:311:0x0825, B:316:0x083d, B:315:0x0838, B:322:0x0879, B:324:0x0885, B:326:0x0898, B:325:0x0891, B:279:0x0733, B:263:0x069a, B:265:0x069e, B:222:0x0584, B:228:0x059a, B:234:0x05dd, B:237:0x05e3, B:229:0x05ae, B:231:0x05b4, B:232:0x05c8, B:184:0x0438, B:187:0x0444, B:188:0x045f, B:179:0x0407, B:170:0x03d8, B:172:0x03e3, B:160:0x03b6, B:161:0x03bd, B:162:0x03c4, B:137:0x036c, B:138:0x0371, B:111:0x0301, B:113:0x0307, B:87:0x0271, B:54:0x0137, B:56:0x013d, B:57:0x0143, B:60:0x014d, B:61:0x0157, B:62:0x0169, B:64:0x016f, B:65:0x0186, B:67:0x018d, B:69:0x0195, B:70:0x01c5, B:51:0x011e, B:72:0x020e, B:562:0x0cb8, B:370:0x0928), top: B:630:0x002c, inners: #1, #3 }] */
    /* JADX WARN: Code duplicated, block: B:284:0x0759  */
    /* JADX WARN: Code duplicated, block: B:288:0x0767 A[Catch: Exception -> 0x0056, TryCatch #2 {Exception -> 0x0056, blocks: (B:13:0x002c, B:14:0x0038, B:16:0x0040, B:18:0x0051, B:19:0x0053, B:23:0x005a, B:25:0x0062, B:27:0x0074, B:28:0x0077, B:32:0x0080, B:35:0x0088, B:36:0x009e, B:38:0x00a6, B:39:0x00dc, B:41:0x00fe, B:44:0x0106, B:46:0x010e, B:49:0x0115, B:53:0x0129, B:71:0x01ee, B:73:0x0220, B:75:0x0232, B:78:0x023a, B:80:0x023e, B:82:0x025a, B:84:0x0261, B:88:0x0274, B:92:0x0280, B:94:0x028c, B:95:0x0292, B:97:0x029d, B:99:0x02a3, B:101:0x02af, B:102:0x02bb, B:103:0x02c5, B:105:0x02d5, B:107:0x02e5, B:109:0x02eb, B:118:0x0321, B:123:0x033e, B:133:0x0361, B:135:0x0367, B:139:0x0375, B:141:0x037b, B:147:0x0386, B:150:0x0399, B:164:0x03cc, B:166:0x03d0, B:174:0x03eb, B:176:0x03f2, B:178:0x03fa, B:181:0x0428, B:190:0x049f, B:196:0x04c2, B:198:0x04e6, B:200:0x04fc, B:202:0x0500, B:207:0x050c, B:208:0x0512, B:212:0x051f, B:218:0x0567, B:219:0x056a, B:213:0x0535, B:215:0x053d, B:216:0x0551, B:220:0x0575, B:240:0x05f5, B:251:0x060e, B:253:0x062c, B:256:0x065d, B:258:0x0667, B:260:0x067d, B:262:0x068e, B:270:0x06b9, B:274:0x06dc, B:276:0x06fb, B:278:0x0726, B:280:0x0742, B:282:0x0752, B:286:0x0761, B:288:0x0767, B:293:0x0777, B:295:0x0789, B:297:0x079c, B:334:0x08c8, B:336:0x08ce, B:345:0x08e7, B:347:0x08ed, B:355:0x0900, B:358:0x090a, B:362:0x0915, B:376:0x0936, B:380:0x0946, B:382:0x0976, B:383:0x097e, B:386:0x0985, B:451:0x0a9b, B:453:0x0ae6, B:454:0x0aed, B:457:0x0af7, B:459:0x0afb, B:461:0x0b01, B:479:0x0b5f, B:502:0x0bbf, B:531:0x0c05, B:540:0x0c44, B:542:0x0c4c, B:545:0x0c54, B:547:0x0c5c, B:551:0x0c67, B:568:0x0cfb, B:572:0x0d0b, B:587:0x0d50, B:589:0x0d56, B:591:0x0d5a, B:593:0x0d65, B:595:0x0d6b, B:597:0x0d75, B:599:0x0d86, B:601:0x0d92, B:603:0x0db2, B:605:0x0dbc, B:607:0x0de8, B:608:0x0df5, B:612:0x0e14, B:614:0x0e1a, B:616:0x0e22, B:618:0x0e28, B:619:0x0e4a, B:575:0x0d1a, B:583:0x0d2f, B:585:0x0d3b, B:552:0x0c8d, B:553:0x0c92, B:554:0x0c95, B:556:0x0c9d, B:558:0x0ca6, B:560:0x0cae, B:564:0x0ce7, B:565:0x0cf0, B:534:0x0c0f, B:536:0x0c17, B:538:0x0c3f, B:586:0x0d42, B:512:0x0bd3, B:517:0x0be0, B:520:0x0bea, B:523:0x0bf3, B:482:0x0b68, B:484:0x0b75, B:477:0x0b5a, B:388:0x099a, B:393:0x09aa, B:397:0x09bc, B:396:0x09b7, B:398:0x09cc, B:400:0x09da, B:401:0x09e3, B:403:0x09eb, B:404:0x09fa, B:405:0x0a03, B:407:0x0a09, B:410:0x0a16, B:413:0x0a20, B:414:0x0a23, B:416:0x0a29, B:419:0x0a32, B:421:0x0a3b, B:424:0x0a43, B:426:0x0a49, B:428:0x0a4d, B:430:0x0a55, B:436:0x0a63, B:438:0x0a69, B:440:0x0a6d, B:442:0x0a75, B:446:0x0a7c, B:448:0x0a8b, B:450:0x0a91, B:296:0x0795, B:299:0x07c4, B:301:0x07d6, B:303:0x07e9, B:302:0x07e2, B:309:0x081d, B:311:0x0825, B:316:0x083d, B:315:0x0838, B:322:0x0879, B:324:0x0885, B:326:0x0898, B:325:0x0891, B:279:0x0733, B:263:0x069a, B:265:0x069e, B:222:0x0584, B:228:0x059a, B:234:0x05dd, B:237:0x05e3, B:229:0x05ae, B:231:0x05b4, B:232:0x05c8, B:184:0x0438, B:187:0x0444, B:188:0x045f, B:179:0x0407, B:170:0x03d8, B:172:0x03e3, B:160:0x03b6, B:161:0x03bd, B:162:0x03c4, B:137:0x036c, B:138:0x0371, B:111:0x0301, B:113:0x0307, B:87:0x0271, B:54:0x0137, B:56:0x013d, B:57:0x0143, B:60:0x014d, B:61:0x0157, B:62:0x0169, B:64:0x016f, B:65:0x0186, B:67:0x018d, B:69:0x0195, B:70:0x01c5, B:51:0x011e, B:72:0x020e, B:562:0x0cb8, B:370:0x0928), top: B:630:0x002c, inners: #1, #3 }] */
    /* JADX WARN: Code duplicated, block: B:290:0x076b  */
    /* JADX WARN: Code duplicated, block: B:324:0x0885 A[Catch: Exception -> 0x0056, TryCatch #2 {Exception -> 0x0056, blocks: (B:13:0x002c, B:14:0x0038, B:16:0x0040, B:18:0x0051, B:19:0x0053, B:23:0x005a, B:25:0x0062, B:27:0x0074, B:28:0x0077, B:32:0x0080, B:35:0x0088, B:36:0x009e, B:38:0x00a6, B:39:0x00dc, B:41:0x00fe, B:44:0x0106, B:46:0x010e, B:49:0x0115, B:53:0x0129, B:71:0x01ee, B:73:0x0220, B:75:0x0232, B:78:0x023a, B:80:0x023e, B:82:0x025a, B:84:0x0261, B:88:0x0274, B:92:0x0280, B:94:0x028c, B:95:0x0292, B:97:0x029d, B:99:0x02a3, B:101:0x02af, B:102:0x02bb, B:103:0x02c5, B:105:0x02d5, B:107:0x02e5, B:109:0x02eb, B:118:0x0321, B:123:0x033e, B:133:0x0361, B:135:0x0367, B:139:0x0375, B:141:0x037b, B:147:0x0386, B:150:0x0399, B:164:0x03cc, B:166:0x03d0, B:174:0x03eb, B:176:0x03f2, B:178:0x03fa, B:181:0x0428, B:190:0x049f, B:196:0x04c2, B:198:0x04e6, B:200:0x04fc, B:202:0x0500, B:207:0x050c, B:208:0x0512, B:212:0x051f, B:218:0x0567, B:219:0x056a, B:213:0x0535, B:215:0x053d, B:216:0x0551, B:220:0x0575, B:240:0x05f5, B:251:0x060e, B:253:0x062c, B:256:0x065d, B:258:0x0667, B:260:0x067d, B:262:0x068e, B:270:0x06b9, B:274:0x06dc, B:276:0x06fb, B:278:0x0726, B:280:0x0742, B:282:0x0752, B:286:0x0761, B:288:0x0767, B:293:0x0777, B:295:0x0789, B:297:0x079c, B:334:0x08c8, B:336:0x08ce, B:345:0x08e7, B:347:0x08ed, B:355:0x0900, B:358:0x090a, B:362:0x0915, B:376:0x0936, B:380:0x0946, B:382:0x0976, B:383:0x097e, B:386:0x0985, B:451:0x0a9b, B:453:0x0ae6, B:454:0x0aed, B:457:0x0af7, B:459:0x0afb, B:461:0x0b01, B:479:0x0b5f, B:502:0x0bbf, B:531:0x0c05, B:540:0x0c44, B:542:0x0c4c, B:545:0x0c54, B:547:0x0c5c, B:551:0x0c67, B:568:0x0cfb, B:572:0x0d0b, B:587:0x0d50, B:589:0x0d56, B:591:0x0d5a, B:593:0x0d65, B:595:0x0d6b, B:597:0x0d75, B:599:0x0d86, B:601:0x0d92, B:603:0x0db2, B:605:0x0dbc, B:607:0x0de8, B:608:0x0df5, B:612:0x0e14, B:614:0x0e1a, B:616:0x0e22, B:618:0x0e28, B:619:0x0e4a, B:575:0x0d1a, B:583:0x0d2f, B:585:0x0d3b, B:552:0x0c8d, B:553:0x0c92, B:554:0x0c95, B:556:0x0c9d, B:558:0x0ca6, B:560:0x0cae, B:564:0x0ce7, B:565:0x0cf0, B:534:0x0c0f, B:536:0x0c17, B:538:0x0c3f, B:586:0x0d42, B:512:0x0bd3, B:517:0x0be0, B:520:0x0bea, B:523:0x0bf3, B:482:0x0b68, B:484:0x0b75, B:477:0x0b5a, B:388:0x099a, B:393:0x09aa, B:397:0x09bc, B:396:0x09b7, B:398:0x09cc, B:400:0x09da, B:401:0x09e3, B:403:0x09eb, B:404:0x09fa, B:405:0x0a03, B:407:0x0a09, B:410:0x0a16, B:413:0x0a20, B:414:0x0a23, B:416:0x0a29, B:419:0x0a32, B:421:0x0a3b, B:424:0x0a43, B:426:0x0a49, B:428:0x0a4d, B:430:0x0a55, B:436:0x0a63, B:438:0x0a69, B:440:0x0a6d, B:442:0x0a75, B:446:0x0a7c, B:448:0x0a8b, B:450:0x0a91, B:296:0x0795, B:299:0x07c4, B:301:0x07d6, B:303:0x07e9, B:302:0x07e2, B:309:0x081d, B:311:0x0825, B:316:0x083d, B:315:0x0838, B:322:0x0879, B:324:0x0885, B:326:0x0898, B:325:0x0891, B:279:0x0733, B:263:0x069a, B:265:0x069e, B:222:0x0584, B:228:0x059a, B:234:0x05dd, B:237:0x05e3, B:229:0x05ae, B:231:0x05b4, B:232:0x05c8, B:184:0x0438, B:187:0x0444, B:188:0x045f, B:179:0x0407, B:170:0x03d8, B:172:0x03e3, B:160:0x03b6, B:161:0x03bd, B:162:0x03c4, B:137:0x036c, B:138:0x0371, B:111:0x0301, B:113:0x0307, B:87:0x0271, B:54:0x0137, B:56:0x013d, B:57:0x0143, B:60:0x014d, B:61:0x0157, B:62:0x0169, B:64:0x016f, B:65:0x0186, B:67:0x018d, B:69:0x0195, B:70:0x01c5, B:51:0x011e, B:72:0x020e, B:562:0x0cb8, B:370:0x0928), top: B:630:0x002c, inners: #1, #3 }] */
    /* JADX WARN: Code duplicated, block: B:325:0x0891 A[Catch: Exception -> 0x0056, TryCatch #2 {Exception -> 0x0056, blocks: (B:13:0x002c, B:14:0x0038, B:16:0x0040, B:18:0x0051, B:19:0x0053, B:23:0x005a, B:25:0x0062, B:27:0x0074, B:28:0x0077, B:32:0x0080, B:35:0x0088, B:36:0x009e, B:38:0x00a6, B:39:0x00dc, B:41:0x00fe, B:44:0x0106, B:46:0x010e, B:49:0x0115, B:53:0x0129, B:71:0x01ee, B:73:0x0220, B:75:0x0232, B:78:0x023a, B:80:0x023e, B:82:0x025a, B:84:0x0261, B:88:0x0274, B:92:0x0280, B:94:0x028c, B:95:0x0292, B:97:0x029d, B:99:0x02a3, B:101:0x02af, B:102:0x02bb, B:103:0x02c5, B:105:0x02d5, B:107:0x02e5, B:109:0x02eb, B:118:0x0321, B:123:0x033e, B:133:0x0361, B:135:0x0367, B:139:0x0375, B:141:0x037b, B:147:0x0386, B:150:0x0399, B:164:0x03cc, B:166:0x03d0, B:174:0x03eb, B:176:0x03f2, B:178:0x03fa, B:181:0x0428, B:190:0x049f, B:196:0x04c2, B:198:0x04e6, B:200:0x04fc, B:202:0x0500, B:207:0x050c, B:208:0x0512, B:212:0x051f, B:218:0x0567, B:219:0x056a, B:213:0x0535, B:215:0x053d, B:216:0x0551, B:220:0x0575, B:240:0x05f5, B:251:0x060e, B:253:0x062c, B:256:0x065d, B:258:0x0667, B:260:0x067d, B:262:0x068e, B:270:0x06b9, B:274:0x06dc, B:276:0x06fb, B:278:0x0726, B:280:0x0742, B:282:0x0752, B:286:0x0761, B:288:0x0767, B:293:0x0777, B:295:0x0789, B:297:0x079c, B:334:0x08c8, B:336:0x08ce, B:345:0x08e7, B:347:0x08ed, B:355:0x0900, B:358:0x090a, B:362:0x0915, B:376:0x0936, B:380:0x0946, B:382:0x0976, B:383:0x097e, B:386:0x0985, B:451:0x0a9b, B:453:0x0ae6, B:454:0x0aed, B:457:0x0af7, B:459:0x0afb, B:461:0x0b01, B:479:0x0b5f, B:502:0x0bbf, B:531:0x0c05, B:540:0x0c44, B:542:0x0c4c, B:545:0x0c54, B:547:0x0c5c, B:551:0x0c67, B:568:0x0cfb, B:572:0x0d0b, B:587:0x0d50, B:589:0x0d56, B:591:0x0d5a, B:593:0x0d65, B:595:0x0d6b, B:597:0x0d75, B:599:0x0d86, B:601:0x0d92, B:603:0x0db2, B:605:0x0dbc, B:607:0x0de8, B:608:0x0df5, B:612:0x0e14, B:614:0x0e1a, B:616:0x0e22, B:618:0x0e28, B:619:0x0e4a, B:575:0x0d1a, B:583:0x0d2f, B:585:0x0d3b, B:552:0x0c8d, B:553:0x0c92, B:554:0x0c95, B:556:0x0c9d, B:558:0x0ca6, B:560:0x0cae, B:564:0x0ce7, B:565:0x0cf0, B:534:0x0c0f, B:536:0x0c17, B:538:0x0c3f, B:586:0x0d42, B:512:0x0bd3, B:517:0x0be0, B:520:0x0bea, B:523:0x0bf3, B:482:0x0b68, B:484:0x0b75, B:477:0x0b5a, B:388:0x099a, B:393:0x09aa, B:397:0x09bc, B:396:0x09b7, B:398:0x09cc, B:400:0x09da, B:401:0x09e3, B:403:0x09eb, B:404:0x09fa, B:405:0x0a03, B:407:0x0a09, B:410:0x0a16, B:413:0x0a20, B:414:0x0a23, B:416:0x0a29, B:419:0x0a32, B:421:0x0a3b, B:424:0x0a43, B:426:0x0a49, B:428:0x0a4d, B:430:0x0a55, B:436:0x0a63, B:438:0x0a69, B:440:0x0a6d, B:442:0x0a75, B:446:0x0a7c, B:448:0x0a8b, B:450:0x0a91, B:296:0x0795, B:299:0x07c4, B:301:0x07d6, B:303:0x07e9, B:302:0x07e2, B:309:0x081d, B:311:0x0825, B:316:0x083d, B:315:0x0838, B:322:0x0879, B:324:0x0885, B:326:0x0898, B:325:0x0891, B:279:0x0733, B:263:0x069a, B:265:0x069e, B:222:0x0584, B:228:0x059a, B:234:0x05dd, B:237:0x05e3, B:229:0x05ae, B:231:0x05b4, B:232:0x05c8, B:184:0x0438, B:187:0x0444, B:188:0x045f, B:179:0x0407, B:170:0x03d8, B:172:0x03e3, B:160:0x03b6, B:161:0x03bd, B:162:0x03c4, B:137:0x036c, B:138:0x0371, B:111:0x0301, B:113:0x0307, B:87:0x0271, B:54:0x0137, B:56:0x013d, B:57:0x0143, B:60:0x014d, B:61:0x0157, B:62:0x0169, B:64:0x016f, B:65:0x0186, B:67:0x018d, B:69:0x0195, B:70:0x01c5, B:51:0x011e, B:72:0x020e, B:562:0x0cb8, B:370:0x0928), top: B:630:0x002c, inners: #1, #3 }] */
    /* JADX WARN: Code duplicated, block: B:328:0x08b9  */
    /* JADX WARN: Code duplicated, block: B:329:0x08bb  */
    /* JADX WARN: Code duplicated, block: B:332:0x08c3  */
    /* JADX WARN: Code duplicated, block: B:333:0x08c6  */
    /* JADX WARN: Code duplicated, block: B:336:0x08ce A[Catch: Exception -> 0x0056, TryCatch #2 {Exception -> 0x0056, blocks: (B:13:0x002c, B:14:0x0038, B:16:0x0040, B:18:0x0051, B:19:0x0053, B:23:0x005a, B:25:0x0062, B:27:0x0074, B:28:0x0077, B:32:0x0080, B:35:0x0088, B:36:0x009e, B:38:0x00a6, B:39:0x00dc, B:41:0x00fe, B:44:0x0106, B:46:0x010e, B:49:0x0115, B:53:0x0129, B:71:0x01ee, B:73:0x0220, B:75:0x0232, B:78:0x023a, B:80:0x023e, B:82:0x025a, B:84:0x0261, B:88:0x0274, B:92:0x0280, B:94:0x028c, B:95:0x0292, B:97:0x029d, B:99:0x02a3, B:101:0x02af, B:102:0x02bb, B:103:0x02c5, B:105:0x02d5, B:107:0x02e5, B:109:0x02eb, B:118:0x0321, B:123:0x033e, B:133:0x0361, B:135:0x0367, B:139:0x0375, B:141:0x037b, B:147:0x0386, B:150:0x0399, B:164:0x03cc, B:166:0x03d0, B:174:0x03eb, B:176:0x03f2, B:178:0x03fa, B:181:0x0428, B:190:0x049f, B:196:0x04c2, B:198:0x04e6, B:200:0x04fc, B:202:0x0500, B:207:0x050c, B:208:0x0512, B:212:0x051f, B:218:0x0567, B:219:0x056a, B:213:0x0535, B:215:0x053d, B:216:0x0551, B:220:0x0575, B:240:0x05f5, B:251:0x060e, B:253:0x062c, B:256:0x065d, B:258:0x0667, B:260:0x067d, B:262:0x068e, B:270:0x06b9, B:274:0x06dc, B:276:0x06fb, B:278:0x0726, B:280:0x0742, B:282:0x0752, B:286:0x0761, B:288:0x0767, B:293:0x0777, B:295:0x0789, B:297:0x079c, B:334:0x08c8, B:336:0x08ce, B:345:0x08e7, B:347:0x08ed, B:355:0x0900, B:358:0x090a, B:362:0x0915, B:376:0x0936, B:380:0x0946, B:382:0x0976, B:383:0x097e, B:386:0x0985, B:451:0x0a9b, B:453:0x0ae6, B:454:0x0aed, B:457:0x0af7, B:459:0x0afb, B:461:0x0b01, B:479:0x0b5f, B:502:0x0bbf, B:531:0x0c05, B:540:0x0c44, B:542:0x0c4c, B:545:0x0c54, B:547:0x0c5c, B:551:0x0c67, B:568:0x0cfb, B:572:0x0d0b, B:587:0x0d50, B:589:0x0d56, B:591:0x0d5a, B:593:0x0d65, B:595:0x0d6b, B:597:0x0d75, B:599:0x0d86, B:601:0x0d92, B:603:0x0db2, B:605:0x0dbc, B:607:0x0de8, B:608:0x0df5, B:612:0x0e14, B:614:0x0e1a, B:616:0x0e22, B:618:0x0e28, B:619:0x0e4a, B:575:0x0d1a, B:583:0x0d2f, B:585:0x0d3b, B:552:0x0c8d, B:553:0x0c92, B:554:0x0c95, B:556:0x0c9d, B:558:0x0ca6, B:560:0x0cae, B:564:0x0ce7, B:565:0x0cf0, B:534:0x0c0f, B:536:0x0c17, B:538:0x0c3f, B:586:0x0d42, B:512:0x0bd3, B:517:0x0be0, B:520:0x0bea, B:523:0x0bf3, B:482:0x0b68, B:484:0x0b75, B:477:0x0b5a, B:388:0x099a, B:393:0x09aa, B:397:0x09bc, B:396:0x09b7, B:398:0x09cc, B:400:0x09da, B:401:0x09e3, B:403:0x09eb, B:404:0x09fa, B:405:0x0a03, B:407:0x0a09, B:410:0x0a16, B:413:0x0a20, B:414:0x0a23, B:416:0x0a29, B:419:0x0a32, B:421:0x0a3b, B:424:0x0a43, B:426:0x0a49, B:428:0x0a4d, B:430:0x0a55, B:436:0x0a63, B:438:0x0a69, B:440:0x0a6d, B:442:0x0a75, B:446:0x0a7c, B:448:0x0a8b, B:450:0x0a91, B:296:0x0795, B:299:0x07c4, B:301:0x07d6, B:303:0x07e9, B:302:0x07e2, B:309:0x081d, B:311:0x0825, B:316:0x083d, B:315:0x0838, B:322:0x0879, B:324:0x0885, B:326:0x0898, B:325:0x0891, B:279:0x0733, B:263:0x069a, B:265:0x069e, B:222:0x0584, B:228:0x059a, B:234:0x05dd, B:237:0x05e3, B:229:0x05ae, B:231:0x05b4, B:232:0x05c8, B:184:0x0438, B:187:0x0444, B:188:0x045f, B:179:0x0407, B:170:0x03d8, B:172:0x03e3, B:160:0x03b6, B:161:0x03bd, B:162:0x03c4, B:137:0x036c, B:138:0x0371, B:111:0x0301, B:113:0x0307, B:87:0x0271, B:54:0x0137, B:56:0x013d, B:57:0x0143, B:60:0x014d, B:61:0x0157, B:62:0x0169, B:64:0x016f, B:65:0x0186, B:67:0x018d, B:69:0x0195, B:70:0x01c5, B:51:0x011e, B:72:0x020e, B:562:0x0cb8, B:370:0x0928), top: B:630:0x002c, inners: #1, #3 }] */
    /* JADX WARN: Code duplicated, block: B:340:0x08d8  */
    /* JADX WARN: Code duplicated, block: B:342:0x08e0 A[ADDED_TO_REGION] */
    /* JADX WARN: Code duplicated, block: B:345:0x08e7 A[Catch: Exception -> 0x0056, TryCatch #2 {Exception -> 0x0056, blocks: (B:13:0x002c, B:14:0x0038, B:16:0x0040, B:18:0x0051, B:19:0x0053, B:23:0x005a, B:25:0x0062, B:27:0x0074, B:28:0x0077, B:32:0x0080, B:35:0x0088, B:36:0x009e, B:38:0x00a6, B:39:0x00dc, B:41:0x00fe, B:44:0x0106, B:46:0x010e, B:49:0x0115, B:53:0x0129, B:71:0x01ee, B:73:0x0220, B:75:0x0232, B:78:0x023a, B:80:0x023e, B:82:0x025a, B:84:0x0261, B:88:0x0274, B:92:0x0280, B:94:0x028c, B:95:0x0292, B:97:0x029d, B:99:0x02a3, B:101:0x02af, B:102:0x02bb, B:103:0x02c5, B:105:0x02d5, B:107:0x02e5, B:109:0x02eb, B:118:0x0321, B:123:0x033e, B:133:0x0361, B:135:0x0367, B:139:0x0375, B:141:0x037b, B:147:0x0386, B:150:0x0399, B:164:0x03cc, B:166:0x03d0, B:174:0x03eb, B:176:0x03f2, B:178:0x03fa, B:181:0x0428, B:190:0x049f, B:196:0x04c2, B:198:0x04e6, B:200:0x04fc, B:202:0x0500, B:207:0x050c, B:208:0x0512, B:212:0x051f, B:218:0x0567, B:219:0x056a, B:213:0x0535, B:215:0x053d, B:216:0x0551, B:220:0x0575, B:240:0x05f5, B:251:0x060e, B:253:0x062c, B:256:0x065d, B:258:0x0667, B:260:0x067d, B:262:0x068e, B:270:0x06b9, B:274:0x06dc, B:276:0x06fb, B:278:0x0726, B:280:0x0742, B:282:0x0752, B:286:0x0761, B:288:0x0767, B:293:0x0777, B:295:0x0789, B:297:0x079c, B:334:0x08c8, B:336:0x08ce, B:345:0x08e7, B:347:0x08ed, B:355:0x0900, B:358:0x090a, B:362:0x0915, B:376:0x0936, B:380:0x0946, B:382:0x0976, B:383:0x097e, B:386:0x0985, B:451:0x0a9b, B:453:0x0ae6, B:454:0x0aed, B:457:0x0af7, B:459:0x0afb, B:461:0x0b01, B:479:0x0b5f, B:502:0x0bbf, B:531:0x0c05, B:540:0x0c44, B:542:0x0c4c, B:545:0x0c54, B:547:0x0c5c, B:551:0x0c67, B:568:0x0cfb, B:572:0x0d0b, B:587:0x0d50, B:589:0x0d56, B:591:0x0d5a, B:593:0x0d65, B:595:0x0d6b, B:597:0x0d75, B:599:0x0d86, B:601:0x0d92, B:603:0x0db2, B:605:0x0dbc, B:607:0x0de8, B:608:0x0df5, B:612:0x0e14, B:614:0x0e1a, B:616:0x0e22, B:618:0x0e28, B:619:0x0e4a, B:575:0x0d1a, B:583:0x0d2f, B:585:0x0d3b, B:552:0x0c8d, B:553:0x0c92, B:554:0x0c95, B:556:0x0c9d, B:558:0x0ca6, B:560:0x0cae, B:564:0x0ce7, B:565:0x0cf0, B:534:0x0c0f, B:536:0x0c17, B:538:0x0c3f, B:586:0x0d42, B:512:0x0bd3, B:517:0x0be0, B:520:0x0bea, B:523:0x0bf3, B:482:0x0b68, B:484:0x0b75, B:477:0x0b5a, B:388:0x099a, B:393:0x09aa, B:397:0x09bc, B:396:0x09b7, B:398:0x09cc, B:400:0x09da, B:401:0x09e3, B:403:0x09eb, B:404:0x09fa, B:405:0x0a03, B:407:0x0a09, B:410:0x0a16, B:413:0x0a20, B:414:0x0a23, B:416:0x0a29, B:419:0x0a32, B:421:0x0a3b, B:424:0x0a43, B:426:0x0a49, B:428:0x0a4d, B:430:0x0a55, B:436:0x0a63, B:438:0x0a69, B:440:0x0a6d, B:442:0x0a75, B:446:0x0a7c, B:448:0x0a8b, B:450:0x0a91, B:296:0x0795, B:299:0x07c4, B:301:0x07d6, B:303:0x07e9, B:302:0x07e2, B:309:0x081d, B:311:0x0825, B:316:0x083d, B:315:0x0838, B:322:0x0879, B:324:0x0885, B:326:0x0898, B:325:0x0891, B:279:0x0733, B:263:0x069a, B:265:0x069e, B:222:0x0584, B:228:0x059a, B:234:0x05dd, B:237:0x05e3, B:229:0x05ae, B:231:0x05b4, B:232:0x05c8, B:184:0x0438, B:187:0x0444, B:188:0x045f, B:179:0x0407, B:170:0x03d8, B:172:0x03e3, B:160:0x03b6, B:161:0x03bd, B:162:0x03c4, B:137:0x036c, B:138:0x0371, B:111:0x0301, B:113:0x0307, B:87:0x0271, B:54:0x0137, B:56:0x013d, B:57:0x0143, B:60:0x014d, B:61:0x0157, B:62:0x0169, B:64:0x016f, B:65:0x0186, B:67:0x018d, B:69:0x0195, B:70:0x01c5, B:51:0x011e, B:72:0x020e, B:562:0x0cb8, B:370:0x0928), top: B:630:0x002c, inners: #1, #3 }] */
    /* JADX WARN: Code duplicated, block: B:349:0x08f5  */
    /* JADX WARN: Code duplicated, block: B:353:0x08fd  */
    /* JADX WARN: Code duplicated, block: B:355:0x0900 A[Catch: Exception -> 0x0056, TryCatch #2 {Exception -> 0x0056, blocks: (B:13:0x002c, B:14:0x0038, B:16:0x0040, B:18:0x0051, B:19:0x0053, B:23:0x005a, B:25:0x0062, B:27:0x0074, B:28:0x0077, B:32:0x0080, B:35:0x0088, B:36:0x009e, B:38:0x00a6, B:39:0x00dc, B:41:0x00fe, B:44:0x0106, B:46:0x010e, B:49:0x0115, B:53:0x0129, B:71:0x01ee, B:73:0x0220, B:75:0x0232, B:78:0x023a, B:80:0x023e, B:82:0x025a, B:84:0x0261, B:88:0x0274, B:92:0x0280, B:94:0x028c, B:95:0x0292, B:97:0x029d, B:99:0x02a3, B:101:0x02af, B:102:0x02bb, B:103:0x02c5, B:105:0x02d5, B:107:0x02e5, B:109:0x02eb, B:118:0x0321, B:123:0x033e, B:133:0x0361, B:135:0x0367, B:139:0x0375, B:141:0x037b, B:147:0x0386, B:150:0x0399, B:164:0x03cc, B:166:0x03d0, B:174:0x03eb, B:176:0x03f2, B:178:0x03fa, B:181:0x0428, B:190:0x049f, B:196:0x04c2, B:198:0x04e6, B:200:0x04fc, B:202:0x0500, B:207:0x050c, B:208:0x0512, B:212:0x051f, B:218:0x0567, B:219:0x056a, B:213:0x0535, B:215:0x053d, B:216:0x0551, B:220:0x0575, B:240:0x05f5, B:251:0x060e, B:253:0x062c, B:256:0x065d, B:258:0x0667, B:260:0x067d, B:262:0x068e, B:270:0x06b9, B:274:0x06dc, B:276:0x06fb, B:278:0x0726, B:280:0x0742, B:282:0x0752, B:286:0x0761, B:288:0x0767, B:293:0x0777, B:295:0x0789, B:297:0x079c, B:334:0x08c8, B:336:0x08ce, B:345:0x08e7, B:347:0x08ed, B:355:0x0900, B:358:0x090a, B:362:0x0915, B:376:0x0936, B:380:0x0946, B:382:0x0976, B:383:0x097e, B:386:0x0985, B:451:0x0a9b, B:453:0x0ae6, B:454:0x0aed, B:457:0x0af7, B:459:0x0afb, B:461:0x0b01, B:479:0x0b5f, B:502:0x0bbf, B:531:0x0c05, B:540:0x0c44, B:542:0x0c4c, B:545:0x0c54, B:547:0x0c5c, B:551:0x0c67, B:568:0x0cfb, B:572:0x0d0b, B:587:0x0d50, B:589:0x0d56, B:591:0x0d5a, B:593:0x0d65, B:595:0x0d6b, B:597:0x0d75, B:599:0x0d86, B:601:0x0d92, B:603:0x0db2, B:605:0x0dbc, B:607:0x0de8, B:608:0x0df5, B:612:0x0e14, B:614:0x0e1a, B:616:0x0e22, B:618:0x0e28, B:619:0x0e4a, B:575:0x0d1a, B:583:0x0d2f, B:585:0x0d3b, B:552:0x0c8d, B:553:0x0c92, B:554:0x0c95, B:556:0x0c9d, B:558:0x0ca6, B:560:0x0cae, B:564:0x0ce7, B:565:0x0cf0, B:534:0x0c0f, B:536:0x0c17, B:538:0x0c3f, B:586:0x0d42, B:512:0x0bd3, B:517:0x0be0, B:520:0x0bea, B:523:0x0bf3, B:482:0x0b68, B:484:0x0b75, B:477:0x0b5a, B:388:0x099a, B:393:0x09aa, B:397:0x09bc, B:396:0x09b7, B:398:0x09cc, B:400:0x09da, B:401:0x09e3, B:403:0x09eb, B:404:0x09fa, B:405:0x0a03, B:407:0x0a09, B:410:0x0a16, B:413:0x0a20, B:414:0x0a23, B:416:0x0a29, B:419:0x0a32, B:421:0x0a3b, B:424:0x0a43, B:426:0x0a49, B:428:0x0a4d, B:430:0x0a55, B:436:0x0a63, B:438:0x0a69, B:440:0x0a6d, B:442:0x0a75, B:446:0x0a7c, B:448:0x0a8b, B:450:0x0a91, B:296:0x0795, B:299:0x07c4, B:301:0x07d6, B:303:0x07e9, B:302:0x07e2, B:309:0x081d, B:311:0x0825, B:316:0x083d, B:315:0x0838, B:322:0x0879, B:324:0x0885, B:326:0x0898, B:325:0x0891, B:279:0x0733, B:263:0x069a, B:265:0x069e, B:222:0x0584, B:228:0x059a, B:234:0x05dd, B:237:0x05e3, B:229:0x05ae, B:231:0x05b4, B:232:0x05c8, B:184:0x0438, B:187:0x0444, B:188:0x045f, B:179:0x0407, B:170:0x03d8, B:172:0x03e3, B:160:0x03b6, B:161:0x03bd, B:162:0x03c4, B:137:0x036c, B:138:0x0371, B:111:0x0301, B:113:0x0307, B:87:0x0271, B:54:0x0137, B:56:0x013d, B:57:0x0143, B:60:0x014d, B:61:0x0157, B:62:0x0169, B:64:0x016f, B:65:0x0186, B:67:0x018d, B:69:0x0195, B:70:0x01c5, B:51:0x011e, B:72:0x020e, B:562:0x0cb8, B:370:0x0928), top: B:630:0x002c, inners: #1, #3 }] */
    /* JADX WARN: Code duplicated, block: B:357:0x0909  */
    /* JADX WARN: Code duplicated, block: B:360:0x0912  */
    /* JADX WARN: Code duplicated, block: B:361:0x0914  */
    /* JADX WARN: Code duplicated, block: B:364:0x091d  */
    /* JADX WARN: Code duplicated, block: B:365:0x091f  */
    /* JADX WARN: Code duplicated, block: B:366:0x0921  */
    /* JADX WARN: Code duplicated, block: B:368:0x0925  */
    /* JADX WARN: Code duplicated, block: B:372:0x0930  */
    /* JADX WARN: Code duplicated, block: B:378:0x093b  */
    /* JADX WARN: Code duplicated, block: B:379:0x0941  */
    /* JADX WARN: Code duplicated, block: B:382:0x0976 A[Catch: Exception -> 0x0056, TryCatch #2 {Exception -> 0x0056, blocks: (B:13:0x002c, B:14:0x0038, B:16:0x0040, B:18:0x0051, B:19:0x0053, B:23:0x005a, B:25:0x0062, B:27:0x0074, B:28:0x0077, B:32:0x0080, B:35:0x0088, B:36:0x009e, B:38:0x00a6, B:39:0x00dc, B:41:0x00fe, B:44:0x0106, B:46:0x010e, B:49:0x0115, B:53:0x0129, B:71:0x01ee, B:73:0x0220, B:75:0x0232, B:78:0x023a, B:80:0x023e, B:82:0x025a, B:84:0x0261, B:88:0x0274, B:92:0x0280, B:94:0x028c, B:95:0x0292, B:97:0x029d, B:99:0x02a3, B:101:0x02af, B:102:0x02bb, B:103:0x02c5, B:105:0x02d5, B:107:0x02e5, B:109:0x02eb, B:118:0x0321, B:123:0x033e, B:133:0x0361, B:135:0x0367, B:139:0x0375, B:141:0x037b, B:147:0x0386, B:150:0x0399, B:164:0x03cc, B:166:0x03d0, B:174:0x03eb, B:176:0x03f2, B:178:0x03fa, B:181:0x0428, B:190:0x049f, B:196:0x04c2, B:198:0x04e6, B:200:0x04fc, B:202:0x0500, B:207:0x050c, B:208:0x0512, B:212:0x051f, B:218:0x0567, B:219:0x056a, B:213:0x0535, B:215:0x053d, B:216:0x0551, B:220:0x0575, B:240:0x05f5, B:251:0x060e, B:253:0x062c, B:256:0x065d, B:258:0x0667, B:260:0x067d, B:262:0x068e, B:270:0x06b9, B:274:0x06dc, B:276:0x06fb, B:278:0x0726, B:280:0x0742, B:282:0x0752, B:286:0x0761, B:288:0x0767, B:293:0x0777, B:295:0x0789, B:297:0x079c, B:334:0x08c8, B:336:0x08ce, B:345:0x08e7, B:347:0x08ed, B:355:0x0900, B:358:0x090a, B:362:0x0915, B:376:0x0936, B:380:0x0946, B:382:0x0976, B:383:0x097e, B:386:0x0985, B:451:0x0a9b, B:453:0x0ae6, B:454:0x0aed, B:457:0x0af7, B:459:0x0afb, B:461:0x0b01, B:479:0x0b5f, B:502:0x0bbf, B:531:0x0c05, B:540:0x0c44, B:542:0x0c4c, B:545:0x0c54, B:547:0x0c5c, B:551:0x0c67, B:568:0x0cfb, B:572:0x0d0b, B:587:0x0d50, B:589:0x0d56, B:591:0x0d5a, B:593:0x0d65, B:595:0x0d6b, B:597:0x0d75, B:599:0x0d86, B:601:0x0d92, B:603:0x0db2, B:605:0x0dbc, B:607:0x0de8, B:608:0x0df5, B:612:0x0e14, B:614:0x0e1a, B:616:0x0e22, B:618:0x0e28, B:619:0x0e4a, B:575:0x0d1a, B:583:0x0d2f, B:585:0x0d3b, B:552:0x0c8d, B:553:0x0c92, B:554:0x0c95, B:556:0x0c9d, B:558:0x0ca6, B:560:0x0cae, B:564:0x0ce7, B:565:0x0cf0, B:534:0x0c0f, B:536:0x0c17, B:538:0x0c3f, B:586:0x0d42, B:512:0x0bd3, B:517:0x0be0, B:520:0x0bea, B:523:0x0bf3, B:482:0x0b68, B:484:0x0b75, B:477:0x0b5a, B:388:0x099a, B:393:0x09aa, B:397:0x09bc, B:396:0x09b7, B:398:0x09cc, B:400:0x09da, B:401:0x09e3, B:403:0x09eb, B:404:0x09fa, B:405:0x0a03, B:407:0x0a09, B:410:0x0a16, B:413:0x0a20, B:414:0x0a23, B:416:0x0a29, B:419:0x0a32, B:421:0x0a3b, B:424:0x0a43, B:426:0x0a49, B:428:0x0a4d, B:430:0x0a55, B:436:0x0a63, B:438:0x0a69, B:440:0x0a6d, B:442:0x0a75, B:446:0x0a7c, B:448:0x0a8b, B:450:0x0a91, B:296:0x0795, B:299:0x07c4, B:301:0x07d6, B:303:0x07e9, B:302:0x07e2, B:309:0x081d, B:311:0x0825, B:316:0x083d, B:315:0x0838, B:322:0x0879, B:324:0x0885, B:326:0x0898, B:325:0x0891, B:279:0x0733, B:263:0x069a, B:265:0x069e, B:222:0x0584, B:228:0x059a, B:234:0x05dd, B:237:0x05e3, B:229:0x05ae, B:231:0x05b4, B:232:0x05c8, B:184:0x0438, B:187:0x0444, B:188:0x045f, B:179:0x0407, B:170:0x03d8, B:172:0x03e3, B:160:0x03b6, B:161:0x03bd, B:162:0x03c4, B:137:0x036c, B:138:0x0371, B:111:0x0301, B:113:0x0307, B:87:0x0271, B:54:0x0137, B:56:0x013d, B:57:0x0143, B:60:0x014d, B:61:0x0157, B:62:0x0169, B:64:0x016f, B:65:0x0186, B:67:0x018d, B:69:0x0195, B:70:0x01c5, B:51:0x011e, B:72:0x020e, B:562:0x0cb8, B:370:0x0928), top: B:630:0x002c, inners: #1, #3 }] */
    /* JADX WARN: Code duplicated, block: B:386:0x0985 A[Catch: Exception -> 0x0056, TRY_ENTER, TryCatch #2 {Exception -> 0x0056, blocks: (B:13:0x002c, B:14:0x0038, B:16:0x0040, B:18:0x0051, B:19:0x0053, B:23:0x005a, B:25:0x0062, B:27:0x0074, B:28:0x0077, B:32:0x0080, B:35:0x0088, B:36:0x009e, B:38:0x00a6, B:39:0x00dc, B:41:0x00fe, B:44:0x0106, B:46:0x010e, B:49:0x0115, B:53:0x0129, B:71:0x01ee, B:73:0x0220, B:75:0x0232, B:78:0x023a, B:80:0x023e, B:82:0x025a, B:84:0x0261, B:88:0x0274, B:92:0x0280, B:94:0x028c, B:95:0x0292, B:97:0x029d, B:99:0x02a3, B:101:0x02af, B:102:0x02bb, B:103:0x02c5, B:105:0x02d5, B:107:0x02e5, B:109:0x02eb, B:118:0x0321, B:123:0x033e, B:133:0x0361, B:135:0x0367, B:139:0x0375, B:141:0x037b, B:147:0x0386, B:150:0x0399, B:164:0x03cc, B:166:0x03d0, B:174:0x03eb, B:176:0x03f2, B:178:0x03fa, B:181:0x0428, B:190:0x049f, B:196:0x04c2, B:198:0x04e6, B:200:0x04fc, B:202:0x0500, B:207:0x050c, B:208:0x0512, B:212:0x051f, B:218:0x0567, B:219:0x056a, B:213:0x0535, B:215:0x053d, B:216:0x0551, B:220:0x0575, B:240:0x05f5, B:251:0x060e, B:253:0x062c, B:256:0x065d, B:258:0x0667, B:260:0x067d, B:262:0x068e, B:270:0x06b9, B:274:0x06dc, B:276:0x06fb, B:278:0x0726, B:280:0x0742, B:282:0x0752, B:286:0x0761, B:288:0x0767, B:293:0x0777, B:295:0x0789, B:297:0x079c, B:334:0x08c8, B:336:0x08ce, B:345:0x08e7, B:347:0x08ed, B:355:0x0900, B:358:0x090a, B:362:0x0915, B:376:0x0936, B:380:0x0946, B:382:0x0976, B:383:0x097e, B:386:0x0985, B:451:0x0a9b, B:453:0x0ae6, B:454:0x0aed, B:457:0x0af7, B:459:0x0afb, B:461:0x0b01, B:479:0x0b5f, B:502:0x0bbf, B:531:0x0c05, B:540:0x0c44, B:542:0x0c4c, B:545:0x0c54, B:547:0x0c5c, B:551:0x0c67, B:568:0x0cfb, B:572:0x0d0b, B:587:0x0d50, B:589:0x0d56, B:591:0x0d5a, B:593:0x0d65, B:595:0x0d6b, B:597:0x0d75, B:599:0x0d86, B:601:0x0d92, B:603:0x0db2, B:605:0x0dbc, B:607:0x0de8, B:608:0x0df5, B:612:0x0e14, B:614:0x0e1a, B:616:0x0e22, B:618:0x0e28, B:619:0x0e4a, B:575:0x0d1a, B:583:0x0d2f, B:585:0x0d3b, B:552:0x0c8d, B:553:0x0c92, B:554:0x0c95, B:556:0x0c9d, B:558:0x0ca6, B:560:0x0cae, B:564:0x0ce7, B:565:0x0cf0, B:534:0x0c0f, B:536:0x0c17, B:538:0x0c3f, B:586:0x0d42, B:512:0x0bd3, B:517:0x0be0, B:520:0x0bea, B:523:0x0bf3, B:482:0x0b68, B:484:0x0b75, B:477:0x0b5a, B:388:0x099a, B:393:0x09aa, B:397:0x09bc, B:396:0x09b7, B:398:0x09cc, B:400:0x09da, B:401:0x09e3, B:403:0x09eb, B:404:0x09fa, B:405:0x0a03, B:407:0x0a09, B:410:0x0a16, B:413:0x0a20, B:414:0x0a23, B:416:0x0a29, B:419:0x0a32, B:421:0x0a3b, B:424:0x0a43, B:426:0x0a49, B:428:0x0a4d, B:430:0x0a55, B:436:0x0a63, B:438:0x0a69, B:440:0x0a6d, B:442:0x0a75, B:446:0x0a7c, B:448:0x0a8b, B:450:0x0a91, B:296:0x0795, B:299:0x07c4, B:301:0x07d6, B:303:0x07e9, B:302:0x07e2, B:309:0x081d, B:311:0x0825, B:316:0x083d, B:315:0x0838, B:322:0x0879, B:324:0x0885, B:326:0x0898, B:325:0x0891, B:279:0x0733, B:263:0x069a, B:265:0x069e, B:222:0x0584, B:228:0x059a, B:234:0x05dd, B:237:0x05e3, B:229:0x05ae, B:231:0x05b4, B:232:0x05c8, B:184:0x0438, B:187:0x0444, B:188:0x045f, B:179:0x0407, B:170:0x03d8, B:172:0x03e3, B:160:0x03b6, B:161:0x03bd, B:162:0x03c4, B:137:0x036c, B:138:0x0371, B:111:0x0301, B:113:0x0307, B:87:0x0271, B:54:0x0137, B:56:0x013d, B:57:0x0143, B:60:0x014d, B:61:0x0157, B:62:0x0169, B:64:0x016f, B:65:0x0186, B:67:0x018d, B:69:0x0195, B:70:0x01c5, B:51:0x011e, B:72:0x020e, B:562:0x0cb8, B:370:0x0928), top: B:630:0x002c, inners: #1, #3 }] */
    /* JADX WARN: Code duplicated, block: B:388:0x099a A[Catch: Exception -> 0x0056, TRY_LEAVE, TryCatch #2 {Exception -> 0x0056, blocks: (B:13:0x002c, B:14:0x0038, B:16:0x0040, B:18:0x0051, B:19:0x0053, B:23:0x005a, B:25:0x0062, B:27:0x0074, B:28:0x0077, B:32:0x0080, B:35:0x0088, B:36:0x009e, B:38:0x00a6, B:39:0x00dc, B:41:0x00fe, B:44:0x0106, B:46:0x010e, B:49:0x0115, B:53:0x0129, B:71:0x01ee, B:73:0x0220, B:75:0x0232, B:78:0x023a, B:80:0x023e, B:82:0x025a, B:84:0x0261, B:88:0x0274, B:92:0x0280, B:94:0x028c, B:95:0x0292, B:97:0x029d, B:99:0x02a3, B:101:0x02af, B:102:0x02bb, B:103:0x02c5, B:105:0x02d5, B:107:0x02e5, B:109:0x02eb, B:118:0x0321, B:123:0x033e, B:133:0x0361, B:135:0x0367, B:139:0x0375, B:141:0x037b, B:147:0x0386, B:150:0x0399, B:164:0x03cc, B:166:0x03d0, B:174:0x03eb, B:176:0x03f2, B:178:0x03fa, B:181:0x0428, B:190:0x049f, B:196:0x04c2, B:198:0x04e6, B:200:0x04fc, B:202:0x0500, B:207:0x050c, B:208:0x0512, B:212:0x051f, B:218:0x0567, B:219:0x056a, B:213:0x0535, B:215:0x053d, B:216:0x0551, B:220:0x0575, B:240:0x05f5, B:251:0x060e, B:253:0x062c, B:256:0x065d, B:258:0x0667, B:260:0x067d, B:262:0x068e, B:270:0x06b9, B:274:0x06dc, B:276:0x06fb, B:278:0x0726, B:280:0x0742, B:282:0x0752, B:286:0x0761, B:288:0x0767, B:293:0x0777, B:295:0x0789, B:297:0x079c, B:334:0x08c8, B:336:0x08ce, B:345:0x08e7, B:347:0x08ed, B:355:0x0900, B:358:0x090a, B:362:0x0915, B:376:0x0936, B:380:0x0946, B:382:0x0976, B:383:0x097e, B:386:0x0985, B:451:0x0a9b, B:453:0x0ae6, B:454:0x0aed, B:457:0x0af7, B:459:0x0afb, B:461:0x0b01, B:479:0x0b5f, B:502:0x0bbf, B:531:0x0c05, B:540:0x0c44, B:542:0x0c4c, B:545:0x0c54, B:547:0x0c5c, B:551:0x0c67, B:568:0x0cfb, B:572:0x0d0b, B:587:0x0d50, B:589:0x0d56, B:591:0x0d5a, B:593:0x0d65, B:595:0x0d6b, B:597:0x0d75, B:599:0x0d86, B:601:0x0d92, B:603:0x0db2, B:605:0x0dbc, B:607:0x0de8, B:608:0x0df5, B:612:0x0e14, B:614:0x0e1a, B:616:0x0e22, B:618:0x0e28, B:619:0x0e4a, B:575:0x0d1a, B:583:0x0d2f, B:585:0x0d3b, B:552:0x0c8d, B:553:0x0c92, B:554:0x0c95, B:556:0x0c9d, B:558:0x0ca6, B:560:0x0cae, B:564:0x0ce7, B:565:0x0cf0, B:534:0x0c0f, B:536:0x0c17, B:538:0x0c3f, B:586:0x0d42, B:512:0x0bd3, B:517:0x0be0, B:520:0x0bea, B:523:0x0bf3, B:482:0x0b68, B:484:0x0b75, B:477:0x0b5a, B:388:0x099a, B:393:0x09aa, B:397:0x09bc, B:396:0x09b7, B:398:0x09cc, B:400:0x09da, B:401:0x09e3, B:403:0x09eb, B:404:0x09fa, B:405:0x0a03, B:407:0x0a09, B:410:0x0a16, B:413:0x0a20, B:414:0x0a23, B:416:0x0a29, B:419:0x0a32, B:421:0x0a3b, B:424:0x0a43, B:426:0x0a49, B:428:0x0a4d, B:430:0x0a55, B:436:0x0a63, B:438:0x0a69, B:440:0x0a6d, B:442:0x0a75, B:446:0x0a7c, B:448:0x0a8b, B:450:0x0a91, B:296:0x0795, B:299:0x07c4, B:301:0x07d6, B:303:0x07e9, B:302:0x07e2, B:309:0x081d, B:311:0x0825, B:316:0x083d, B:315:0x0838, B:322:0x0879, B:324:0x0885, B:326:0x0898, B:325:0x0891, B:279:0x0733, B:263:0x069a, B:265:0x069e, B:222:0x0584, B:228:0x059a, B:234:0x05dd, B:237:0x05e3, B:229:0x05ae, B:231:0x05b4, B:232:0x05c8, B:184:0x0438, B:187:0x0444, B:188:0x045f, B:179:0x0407, B:170:0x03d8, B:172:0x03e3, B:160:0x03b6, B:161:0x03bd, B:162:0x03c4, B:137:0x036c, B:138:0x0371, B:111:0x0301, B:113:0x0307, B:87:0x0271, B:54:0x0137, B:56:0x013d, B:57:0x0143, B:60:0x014d, B:61:0x0157, B:62:0x0169, B:64:0x016f, B:65:0x0186, B:67:0x018d, B:69:0x0195, B:70:0x01c5, B:51:0x011e, B:72:0x020e, B:562:0x0cb8, B:370:0x0928), top: B:630:0x002c, inners: #1, #3 }] */
    /* JADX WARN: Code duplicated, block: B:391:0x09a3 A[DONT_INVERT] */
    /* JADX WARN: Code duplicated, block: B:392:0x09a5  */
    /* JADX WARN: Code duplicated, block: B:394:0x09ae  */
    /* JADX WARN: Code duplicated, block: B:396:0x09b7 A[Catch: Exception -> 0x0056, TryCatch #2 {Exception -> 0x0056, blocks: (B:13:0x002c, B:14:0x0038, B:16:0x0040, B:18:0x0051, B:19:0x0053, B:23:0x005a, B:25:0x0062, B:27:0x0074, B:28:0x0077, B:32:0x0080, B:35:0x0088, B:36:0x009e, B:38:0x00a6, B:39:0x00dc, B:41:0x00fe, B:44:0x0106, B:46:0x010e, B:49:0x0115, B:53:0x0129, B:71:0x01ee, B:73:0x0220, B:75:0x0232, B:78:0x023a, B:80:0x023e, B:82:0x025a, B:84:0x0261, B:88:0x0274, B:92:0x0280, B:94:0x028c, B:95:0x0292, B:97:0x029d, B:99:0x02a3, B:101:0x02af, B:102:0x02bb, B:103:0x02c5, B:105:0x02d5, B:107:0x02e5, B:109:0x02eb, B:118:0x0321, B:123:0x033e, B:133:0x0361, B:135:0x0367, B:139:0x0375, B:141:0x037b, B:147:0x0386, B:150:0x0399, B:164:0x03cc, B:166:0x03d0, B:174:0x03eb, B:176:0x03f2, B:178:0x03fa, B:181:0x0428, B:190:0x049f, B:196:0x04c2, B:198:0x04e6, B:200:0x04fc, B:202:0x0500, B:207:0x050c, B:208:0x0512, B:212:0x051f, B:218:0x0567, B:219:0x056a, B:213:0x0535, B:215:0x053d, B:216:0x0551, B:220:0x0575, B:240:0x05f5, B:251:0x060e, B:253:0x062c, B:256:0x065d, B:258:0x0667, B:260:0x067d, B:262:0x068e, B:270:0x06b9, B:274:0x06dc, B:276:0x06fb, B:278:0x0726, B:280:0x0742, B:282:0x0752, B:286:0x0761, B:288:0x0767, B:293:0x0777, B:295:0x0789, B:297:0x079c, B:334:0x08c8, B:336:0x08ce, B:345:0x08e7, B:347:0x08ed, B:355:0x0900, B:358:0x090a, B:362:0x0915, B:376:0x0936, B:380:0x0946, B:382:0x0976, B:383:0x097e, B:386:0x0985, B:451:0x0a9b, B:453:0x0ae6, B:454:0x0aed, B:457:0x0af7, B:459:0x0afb, B:461:0x0b01, B:479:0x0b5f, B:502:0x0bbf, B:531:0x0c05, B:540:0x0c44, B:542:0x0c4c, B:545:0x0c54, B:547:0x0c5c, B:551:0x0c67, B:568:0x0cfb, B:572:0x0d0b, B:587:0x0d50, B:589:0x0d56, B:591:0x0d5a, B:593:0x0d65, B:595:0x0d6b, B:597:0x0d75, B:599:0x0d86, B:601:0x0d92, B:603:0x0db2, B:605:0x0dbc, B:607:0x0de8, B:608:0x0df5, B:612:0x0e14, B:614:0x0e1a, B:616:0x0e22, B:618:0x0e28, B:619:0x0e4a, B:575:0x0d1a, B:583:0x0d2f, B:585:0x0d3b, B:552:0x0c8d, B:553:0x0c92, B:554:0x0c95, B:556:0x0c9d, B:558:0x0ca6, B:560:0x0cae, B:564:0x0ce7, B:565:0x0cf0, B:534:0x0c0f, B:536:0x0c17, B:538:0x0c3f, B:586:0x0d42, B:512:0x0bd3, B:517:0x0be0, B:520:0x0bea, B:523:0x0bf3, B:482:0x0b68, B:484:0x0b75, B:477:0x0b5a, B:388:0x099a, B:393:0x09aa, B:397:0x09bc, B:396:0x09b7, B:398:0x09cc, B:400:0x09da, B:401:0x09e3, B:403:0x09eb, B:404:0x09fa, B:405:0x0a03, B:407:0x0a09, B:410:0x0a16, B:413:0x0a20, B:414:0x0a23, B:416:0x0a29, B:419:0x0a32, B:421:0x0a3b, B:424:0x0a43, B:426:0x0a49, B:428:0x0a4d, B:430:0x0a55, B:436:0x0a63, B:438:0x0a69, B:440:0x0a6d, B:442:0x0a75, B:446:0x0a7c, B:448:0x0a8b, B:450:0x0a91, B:296:0x0795, B:299:0x07c4, B:301:0x07d6, B:303:0x07e9, B:302:0x07e2, B:309:0x081d, B:311:0x0825, B:316:0x083d, B:315:0x0838, B:322:0x0879, B:324:0x0885, B:326:0x0898, B:325:0x0891, B:279:0x0733, B:263:0x069a, B:265:0x069e, B:222:0x0584, B:228:0x059a, B:234:0x05dd, B:237:0x05e3, B:229:0x05ae, B:231:0x05b4, B:232:0x05c8, B:184:0x0438, B:187:0x0444, B:188:0x045f, B:179:0x0407, B:170:0x03d8, B:172:0x03e3, B:160:0x03b6, B:161:0x03bd, B:162:0x03c4, B:137:0x036c, B:138:0x0371, B:111:0x0301, B:113:0x0307, B:87:0x0271, B:54:0x0137, B:56:0x013d, B:57:0x0143, B:60:0x014d, B:61:0x0157, B:62:0x0169, B:64:0x016f, B:65:0x0186, B:67:0x018d, B:69:0x0195, B:70:0x01c5, B:51:0x011e, B:72:0x020e, B:562:0x0cb8, B:370:0x0928), top: B:630:0x002c, inners: #1, #3 }] */
    /* JADX WARN: Code duplicated, block: B:398:0x09cc A[Catch: Exception -> 0x0056, TryCatch #2 {Exception -> 0x0056, blocks: (B:13:0x002c, B:14:0x0038, B:16:0x0040, B:18:0x0051, B:19:0x0053, B:23:0x005a, B:25:0x0062, B:27:0x0074, B:28:0x0077, B:32:0x0080, B:35:0x0088, B:36:0x009e, B:38:0x00a6, B:39:0x00dc, B:41:0x00fe, B:44:0x0106, B:46:0x010e, B:49:0x0115, B:53:0x0129, B:71:0x01ee, B:73:0x0220, B:75:0x0232, B:78:0x023a, B:80:0x023e, B:82:0x025a, B:84:0x0261, B:88:0x0274, B:92:0x0280, B:94:0x028c, B:95:0x0292, B:97:0x029d, B:99:0x02a3, B:101:0x02af, B:102:0x02bb, B:103:0x02c5, B:105:0x02d5, B:107:0x02e5, B:109:0x02eb, B:118:0x0321, B:123:0x033e, B:133:0x0361, B:135:0x0367, B:139:0x0375, B:141:0x037b, B:147:0x0386, B:150:0x0399, B:164:0x03cc, B:166:0x03d0, B:174:0x03eb, B:176:0x03f2, B:178:0x03fa, B:181:0x0428, B:190:0x049f, B:196:0x04c2, B:198:0x04e6, B:200:0x04fc, B:202:0x0500, B:207:0x050c, B:208:0x0512, B:212:0x051f, B:218:0x0567, B:219:0x056a, B:213:0x0535, B:215:0x053d, B:216:0x0551, B:220:0x0575, B:240:0x05f5, B:251:0x060e, B:253:0x062c, B:256:0x065d, B:258:0x0667, B:260:0x067d, B:262:0x068e, B:270:0x06b9, B:274:0x06dc, B:276:0x06fb, B:278:0x0726, B:280:0x0742, B:282:0x0752, B:286:0x0761, B:288:0x0767, B:293:0x0777, B:295:0x0789, B:297:0x079c, B:334:0x08c8, B:336:0x08ce, B:345:0x08e7, B:347:0x08ed, B:355:0x0900, B:358:0x090a, B:362:0x0915, B:376:0x0936, B:380:0x0946, B:382:0x0976, B:383:0x097e, B:386:0x0985, B:451:0x0a9b, B:453:0x0ae6, B:454:0x0aed, B:457:0x0af7, B:459:0x0afb, B:461:0x0b01, B:479:0x0b5f, B:502:0x0bbf, B:531:0x0c05, B:540:0x0c44, B:542:0x0c4c, B:545:0x0c54, B:547:0x0c5c, B:551:0x0c67, B:568:0x0cfb, B:572:0x0d0b, B:587:0x0d50, B:589:0x0d56, B:591:0x0d5a, B:593:0x0d65, B:595:0x0d6b, B:597:0x0d75, B:599:0x0d86, B:601:0x0d92, B:603:0x0db2, B:605:0x0dbc, B:607:0x0de8, B:608:0x0df5, B:612:0x0e14, B:614:0x0e1a, B:616:0x0e22, B:618:0x0e28, B:619:0x0e4a, B:575:0x0d1a, B:583:0x0d2f, B:585:0x0d3b, B:552:0x0c8d, B:553:0x0c92, B:554:0x0c95, B:556:0x0c9d, B:558:0x0ca6, B:560:0x0cae, B:564:0x0ce7, B:565:0x0cf0, B:534:0x0c0f, B:536:0x0c17, B:538:0x0c3f, B:586:0x0d42, B:512:0x0bd3, B:517:0x0be0, B:520:0x0bea, B:523:0x0bf3, B:482:0x0b68, B:484:0x0b75, B:477:0x0b5a, B:388:0x099a, B:393:0x09aa, B:397:0x09bc, B:396:0x09b7, B:398:0x09cc, B:400:0x09da, B:401:0x09e3, B:403:0x09eb, B:404:0x09fa, B:405:0x0a03, B:407:0x0a09, B:410:0x0a16, B:413:0x0a20, B:414:0x0a23, B:416:0x0a29, B:419:0x0a32, B:421:0x0a3b, B:424:0x0a43, B:426:0x0a49, B:428:0x0a4d, B:430:0x0a55, B:436:0x0a63, B:438:0x0a69, B:440:0x0a6d, B:442:0x0a75, B:446:0x0a7c, B:448:0x0a8b, B:450:0x0a91, B:296:0x0795, B:299:0x07c4, B:301:0x07d6, B:303:0x07e9, B:302:0x07e2, B:309:0x081d, B:311:0x0825, B:316:0x083d, B:315:0x0838, B:322:0x0879, B:324:0x0885, B:326:0x0898, B:325:0x0891, B:279:0x0733, B:263:0x069a, B:265:0x069e, B:222:0x0584, B:228:0x059a, B:234:0x05dd, B:237:0x05e3, B:229:0x05ae, B:231:0x05b4, B:232:0x05c8, B:184:0x0438, B:187:0x0444, B:188:0x045f, B:179:0x0407, B:170:0x03d8, B:172:0x03e3, B:160:0x03b6, B:161:0x03bd, B:162:0x03c4, B:137:0x036c, B:138:0x0371, B:111:0x0301, B:113:0x0307, B:87:0x0271, B:54:0x0137, B:56:0x013d, B:57:0x0143, B:60:0x014d, B:61:0x0157, B:62:0x0169, B:64:0x016f, B:65:0x0186, B:67:0x018d, B:69:0x0195, B:70:0x01c5, B:51:0x011e, B:72:0x020e, B:562:0x0cb8, B:370:0x0928), top: B:630:0x002c, inners: #1, #3 }] */
    /* JADX WARN: Code duplicated, block: B:400:0x09da A[Catch: Exception -> 0x0056, TryCatch #2 {Exception -> 0x0056, blocks: (B:13:0x002c, B:14:0x0038, B:16:0x0040, B:18:0x0051, B:19:0x0053, B:23:0x005a, B:25:0x0062, B:27:0x0074, B:28:0x0077, B:32:0x0080, B:35:0x0088, B:36:0x009e, B:38:0x00a6, B:39:0x00dc, B:41:0x00fe, B:44:0x0106, B:46:0x010e, B:49:0x0115, B:53:0x0129, B:71:0x01ee, B:73:0x0220, B:75:0x0232, B:78:0x023a, B:80:0x023e, B:82:0x025a, B:84:0x0261, B:88:0x0274, B:92:0x0280, B:94:0x028c, B:95:0x0292, B:97:0x029d, B:99:0x02a3, B:101:0x02af, B:102:0x02bb, B:103:0x02c5, B:105:0x02d5, B:107:0x02e5, B:109:0x02eb, B:118:0x0321, B:123:0x033e, B:133:0x0361, B:135:0x0367, B:139:0x0375, B:141:0x037b, B:147:0x0386, B:150:0x0399, B:164:0x03cc, B:166:0x03d0, B:174:0x03eb, B:176:0x03f2, B:178:0x03fa, B:181:0x0428, B:190:0x049f, B:196:0x04c2, B:198:0x04e6, B:200:0x04fc, B:202:0x0500, B:207:0x050c, B:208:0x0512, B:212:0x051f, B:218:0x0567, B:219:0x056a, B:213:0x0535, B:215:0x053d, B:216:0x0551, B:220:0x0575, B:240:0x05f5, B:251:0x060e, B:253:0x062c, B:256:0x065d, B:258:0x0667, B:260:0x067d, B:262:0x068e, B:270:0x06b9, B:274:0x06dc, B:276:0x06fb, B:278:0x0726, B:280:0x0742, B:282:0x0752, B:286:0x0761, B:288:0x0767, B:293:0x0777, B:295:0x0789, B:297:0x079c, B:334:0x08c8, B:336:0x08ce, B:345:0x08e7, B:347:0x08ed, B:355:0x0900, B:358:0x090a, B:362:0x0915, B:376:0x0936, B:380:0x0946, B:382:0x0976, B:383:0x097e, B:386:0x0985, B:451:0x0a9b, B:453:0x0ae6, B:454:0x0aed, B:457:0x0af7, B:459:0x0afb, B:461:0x0b01, B:479:0x0b5f, B:502:0x0bbf, B:531:0x0c05, B:540:0x0c44, B:542:0x0c4c, B:545:0x0c54, B:547:0x0c5c, B:551:0x0c67, B:568:0x0cfb, B:572:0x0d0b, B:587:0x0d50, B:589:0x0d56, B:591:0x0d5a, B:593:0x0d65, B:595:0x0d6b, B:597:0x0d75, B:599:0x0d86, B:601:0x0d92, B:603:0x0db2, B:605:0x0dbc, B:607:0x0de8, B:608:0x0df5, B:612:0x0e14, B:614:0x0e1a, B:616:0x0e22, B:618:0x0e28, B:619:0x0e4a, B:575:0x0d1a, B:583:0x0d2f, B:585:0x0d3b, B:552:0x0c8d, B:553:0x0c92, B:554:0x0c95, B:556:0x0c9d, B:558:0x0ca6, B:560:0x0cae, B:564:0x0ce7, B:565:0x0cf0, B:534:0x0c0f, B:536:0x0c17, B:538:0x0c3f, B:586:0x0d42, B:512:0x0bd3, B:517:0x0be0, B:520:0x0bea, B:523:0x0bf3, B:482:0x0b68, B:484:0x0b75, B:477:0x0b5a, B:388:0x099a, B:393:0x09aa, B:397:0x09bc, B:396:0x09b7, B:398:0x09cc, B:400:0x09da, B:401:0x09e3, B:403:0x09eb, B:404:0x09fa, B:405:0x0a03, B:407:0x0a09, B:410:0x0a16, B:413:0x0a20, B:414:0x0a23, B:416:0x0a29, B:419:0x0a32, B:421:0x0a3b, B:424:0x0a43, B:426:0x0a49, B:428:0x0a4d, B:430:0x0a55, B:436:0x0a63, B:438:0x0a69, B:440:0x0a6d, B:442:0x0a75, B:446:0x0a7c, B:448:0x0a8b, B:450:0x0a91, B:296:0x0795, B:299:0x07c4, B:301:0x07d6, B:303:0x07e9, B:302:0x07e2, B:309:0x081d, B:311:0x0825, B:316:0x083d, B:315:0x0838, B:322:0x0879, B:324:0x0885, B:326:0x0898, B:325:0x0891, B:279:0x0733, B:263:0x069a, B:265:0x069e, B:222:0x0584, B:228:0x059a, B:234:0x05dd, B:237:0x05e3, B:229:0x05ae, B:231:0x05b4, B:232:0x05c8, B:184:0x0438, B:187:0x0444, B:188:0x045f, B:179:0x0407, B:170:0x03d8, B:172:0x03e3, B:160:0x03b6, B:161:0x03bd, B:162:0x03c4, B:137:0x036c, B:138:0x0371, B:111:0x0301, B:113:0x0307, B:87:0x0271, B:54:0x0137, B:56:0x013d, B:57:0x0143, B:60:0x014d, B:61:0x0157, B:62:0x0169, B:64:0x016f, B:65:0x0186, B:67:0x018d, B:69:0x0195, B:70:0x01c5, B:51:0x011e, B:72:0x020e, B:562:0x0cb8, B:370:0x0928), top: B:630:0x002c, inners: #1, #3 }] */
    /* JADX WARN: Code duplicated, block: B:403:0x09eb A[Catch: Exception -> 0x0056, LOOP:6: B:401:0x09e3->B:403:0x09eb, LOOP_END, TryCatch #2 {Exception -> 0x0056, blocks: (B:13:0x002c, B:14:0x0038, B:16:0x0040, B:18:0x0051, B:19:0x0053, B:23:0x005a, B:25:0x0062, B:27:0x0074, B:28:0x0077, B:32:0x0080, B:35:0x0088, B:36:0x009e, B:38:0x00a6, B:39:0x00dc, B:41:0x00fe, B:44:0x0106, B:46:0x010e, B:49:0x0115, B:53:0x0129, B:71:0x01ee, B:73:0x0220, B:75:0x0232, B:78:0x023a, B:80:0x023e, B:82:0x025a, B:84:0x0261, B:88:0x0274, B:92:0x0280, B:94:0x028c, B:95:0x0292, B:97:0x029d, B:99:0x02a3, B:101:0x02af, B:102:0x02bb, B:103:0x02c5, B:105:0x02d5, B:107:0x02e5, B:109:0x02eb, B:118:0x0321, B:123:0x033e, B:133:0x0361, B:135:0x0367, B:139:0x0375, B:141:0x037b, B:147:0x0386, B:150:0x0399, B:164:0x03cc, B:166:0x03d0, B:174:0x03eb, B:176:0x03f2, B:178:0x03fa, B:181:0x0428, B:190:0x049f, B:196:0x04c2, B:198:0x04e6, B:200:0x04fc, B:202:0x0500, B:207:0x050c, B:208:0x0512, B:212:0x051f, B:218:0x0567, B:219:0x056a, B:213:0x0535, B:215:0x053d, B:216:0x0551, B:220:0x0575, B:240:0x05f5, B:251:0x060e, B:253:0x062c, B:256:0x065d, B:258:0x0667, B:260:0x067d, B:262:0x068e, B:270:0x06b9, B:274:0x06dc, B:276:0x06fb, B:278:0x0726, B:280:0x0742, B:282:0x0752, B:286:0x0761, B:288:0x0767, B:293:0x0777, B:295:0x0789, B:297:0x079c, B:334:0x08c8, B:336:0x08ce, B:345:0x08e7, B:347:0x08ed, B:355:0x0900, B:358:0x090a, B:362:0x0915, B:376:0x0936, B:380:0x0946, B:382:0x0976, B:383:0x097e, B:386:0x0985, B:451:0x0a9b, B:453:0x0ae6, B:454:0x0aed, B:457:0x0af7, B:459:0x0afb, B:461:0x0b01, B:479:0x0b5f, B:502:0x0bbf, B:531:0x0c05, B:540:0x0c44, B:542:0x0c4c, B:545:0x0c54, B:547:0x0c5c, B:551:0x0c67, B:568:0x0cfb, B:572:0x0d0b, B:587:0x0d50, B:589:0x0d56, B:591:0x0d5a, B:593:0x0d65, B:595:0x0d6b, B:597:0x0d75, B:599:0x0d86, B:601:0x0d92, B:603:0x0db2, B:605:0x0dbc, B:607:0x0de8, B:608:0x0df5, B:612:0x0e14, B:614:0x0e1a, B:616:0x0e22, B:618:0x0e28, B:619:0x0e4a, B:575:0x0d1a, B:583:0x0d2f, B:585:0x0d3b, B:552:0x0c8d, B:553:0x0c92, B:554:0x0c95, B:556:0x0c9d, B:558:0x0ca6, B:560:0x0cae, B:564:0x0ce7, B:565:0x0cf0, B:534:0x0c0f, B:536:0x0c17, B:538:0x0c3f, B:586:0x0d42, B:512:0x0bd3, B:517:0x0be0, B:520:0x0bea, B:523:0x0bf3, B:482:0x0b68, B:484:0x0b75, B:477:0x0b5a, B:388:0x099a, B:393:0x09aa, B:397:0x09bc, B:396:0x09b7, B:398:0x09cc, B:400:0x09da, B:401:0x09e3, B:403:0x09eb, B:404:0x09fa, B:405:0x0a03, B:407:0x0a09, B:410:0x0a16, B:413:0x0a20, B:414:0x0a23, B:416:0x0a29, B:419:0x0a32, B:421:0x0a3b, B:424:0x0a43, B:426:0x0a49, B:428:0x0a4d, B:430:0x0a55, B:436:0x0a63, B:438:0x0a69, B:440:0x0a6d, B:442:0x0a75, B:446:0x0a7c, B:448:0x0a8b, B:450:0x0a91, B:296:0x0795, B:299:0x07c4, B:301:0x07d6, B:303:0x07e9, B:302:0x07e2, B:309:0x081d, B:311:0x0825, B:316:0x083d, B:315:0x0838, B:322:0x0879, B:324:0x0885, B:326:0x0898, B:325:0x0891, B:279:0x0733, B:263:0x069a, B:265:0x069e, B:222:0x0584, B:228:0x059a, B:234:0x05dd, B:237:0x05e3, B:229:0x05ae, B:231:0x05b4, B:232:0x05c8, B:184:0x0438, B:187:0x0444, B:188:0x045f, B:179:0x0407, B:170:0x03d8, B:172:0x03e3, B:160:0x03b6, B:161:0x03bd, B:162:0x03c4, B:137:0x036c, B:138:0x0371, B:111:0x0301, B:113:0x0307, B:87:0x0271, B:54:0x0137, B:56:0x013d, B:57:0x0143, B:60:0x014d, B:61:0x0157, B:62:0x0169, B:64:0x016f, B:65:0x0186, B:67:0x018d, B:69:0x0195, B:70:0x01c5, B:51:0x011e, B:72:0x020e, B:562:0x0cb8, B:370:0x0928), top: B:630:0x002c, inners: #1, #3 }] */
    /* JADX WARN: Code duplicated, block: B:405:0x0a03 A[Catch: Exception -> 0x0056, TryCatch #2 {Exception -> 0x0056, blocks: (B:13:0x002c, B:14:0x0038, B:16:0x0040, B:18:0x0051, B:19:0x0053, B:23:0x005a, B:25:0x0062, B:27:0x0074, B:28:0x0077, B:32:0x0080, B:35:0x0088, B:36:0x009e, B:38:0x00a6, B:39:0x00dc, B:41:0x00fe, B:44:0x0106, B:46:0x010e, B:49:0x0115, B:53:0x0129, B:71:0x01ee, B:73:0x0220, B:75:0x0232, B:78:0x023a, B:80:0x023e, B:82:0x025a, B:84:0x0261, B:88:0x0274, B:92:0x0280, B:94:0x028c, B:95:0x0292, B:97:0x029d, B:99:0x02a3, B:101:0x02af, B:102:0x02bb, B:103:0x02c5, B:105:0x02d5, B:107:0x02e5, B:109:0x02eb, B:118:0x0321, B:123:0x033e, B:133:0x0361, B:135:0x0367, B:139:0x0375, B:141:0x037b, B:147:0x0386, B:150:0x0399, B:164:0x03cc, B:166:0x03d0, B:174:0x03eb, B:176:0x03f2, B:178:0x03fa, B:181:0x0428, B:190:0x049f, B:196:0x04c2, B:198:0x04e6, B:200:0x04fc, B:202:0x0500, B:207:0x050c, B:208:0x0512, B:212:0x051f, B:218:0x0567, B:219:0x056a, B:213:0x0535, B:215:0x053d, B:216:0x0551, B:220:0x0575, B:240:0x05f5, B:251:0x060e, B:253:0x062c, B:256:0x065d, B:258:0x0667, B:260:0x067d, B:262:0x068e, B:270:0x06b9, B:274:0x06dc, B:276:0x06fb, B:278:0x0726, B:280:0x0742, B:282:0x0752, B:286:0x0761, B:288:0x0767, B:293:0x0777, B:295:0x0789, B:297:0x079c, B:334:0x08c8, B:336:0x08ce, B:345:0x08e7, B:347:0x08ed, B:355:0x0900, B:358:0x090a, B:362:0x0915, B:376:0x0936, B:380:0x0946, B:382:0x0976, B:383:0x097e, B:386:0x0985, B:451:0x0a9b, B:453:0x0ae6, B:454:0x0aed, B:457:0x0af7, B:459:0x0afb, B:461:0x0b01, B:479:0x0b5f, B:502:0x0bbf, B:531:0x0c05, B:540:0x0c44, B:542:0x0c4c, B:545:0x0c54, B:547:0x0c5c, B:551:0x0c67, B:568:0x0cfb, B:572:0x0d0b, B:587:0x0d50, B:589:0x0d56, B:591:0x0d5a, B:593:0x0d65, B:595:0x0d6b, B:597:0x0d75, B:599:0x0d86, B:601:0x0d92, B:603:0x0db2, B:605:0x0dbc, B:607:0x0de8, B:608:0x0df5, B:612:0x0e14, B:614:0x0e1a, B:616:0x0e22, B:618:0x0e28, B:619:0x0e4a, B:575:0x0d1a, B:583:0x0d2f, B:585:0x0d3b, B:552:0x0c8d, B:553:0x0c92, B:554:0x0c95, B:556:0x0c9d, B:558:0x0ca6, B:560:0x0cae, B:564:0x0ce7, B:565:0x0cf0, B:534:0x0c0f, B:536:0x0c17, B:538:0x0c3f, B:586:0x0d42, B:512:0x0bd3, B:517:0x0be0, B:520:0x0bea, B:523:0x0bf3, B:482:0x0b68, B:484:0x0b75, B:477:0x0b5a, B:388:0x099a, B:393:0x09aa, B:397:0x09bc, B:396:0x09b7, B:398:0x09cc, B:400:0x09da, B:401:0x09e3, B:403:0x09eb, B:404:0x09fa, B:405:0x0a03, B:407:0x0a09, B:410:0x0a16, B:413:0x0a20, B:414:0x0a23, B:416:0x0a29, B:419:0x0a32, B:421:0x0a3b, B:424:0x0a43, B:426:0x0a49, B:428:0x0a4d, B:430:0x0a55, B:436:0x0a63, B:438:0x0a69, B:440:0x0a6d, B:442:0x0a75, B:446:0x0a7c, B:448:0x0a8b, B:450:0x0a91, B:296:0x0795, B:299:0x07c4, B:301:0x07d6, B:303:0x07e9, B:302:0x07e2, B:309:0x081d, B:311:0x0825, B:316:0x083d, B:315:0x0838, B:322:0x0879, B:324:0x0885, B:326:0x0898, B:325:0x0891, B:279:0x0733, B:263:0x069a, B:265:0x069e, B:222:0x0584, B:228:0x059a, B:234:0x05dd, B:237:0x05e3, B:229:0x05ae, B:231:0x05b4, B:232:0x05c8, B:184:0x0438, B:187:0x0444, B:188:0x045f, B:179:0x0407, B:170:0x03d8, B:172:0x03e3, B:160:0x03b6, B:161:0x03bd, B:162:0x03c4, B:137:0x036c, B:138:0x0371, B:111:0x0301, B:113:0x0307, B:87:0x0271, B:54:0x0137, B:56:0x013d, B:57:0x0143, B:60:0x014d, B:61:0x0157, B:62:0x0169, B:64:0x016f, B:65:0x0186, B:67:0x018d, B:69:0x0195, B:70:0x01c5, B:51:0x011e, B:72:0x020e, B:562:0x0cb8, B:370:0x0928), top: B:630:0x002c, inners: #1, #3 }] */
    /* JADX WARN: Code duplicated, block: B:407:0x0a09 A[Catch: Exception -> 0x0056, TryCatch #2 {Exception -> 0x0056, blocks: (B:13:0x002c, B:14:0x0038, B:16:0x0040, B:18:0x0051, B:19:0x0053, B:23:0x005a, B:25:0x0062, B:27:0x0074, B:28:0x0077, B:32:0x0080, B:35:0x0088, B:36:0x009e, B:38:0x00a6, B:39:0x00dc, B:41:0x00fe, B:44:0x0106, B:46:0x010e, B:49:0x0115, B:53:0x0129, B:71:0x01ee, B:73:0x0220, B:75:0x0232, B:78:0x023a, B:80:0x023e, B:82:0x025a, B:84:0x0261, B:88:0x0274, B:92:0x0280, B:94:0x028c, B:95:0x0292, B:97:0x029d, B:99:0x02a3, B:101:0x02af, B:102:0x02bb, B:103:0x02c5, B:105:0x02d5, B:107:0x02e5, B:109:0x02eb, B:118:0x0321, B:123:0x033e, B:133:0x0361, B:135:0x0367, B:139:0x0375, B:141:0x037b, B:147:0x0386, B:150:0x0399, B:164:0x03cc, B:166:0x03d0, B:174:0x03eb, B:176:0x03f2, B:178:0x03fa, B:181:0x0428, B:190:0x049f, B:196:0x04c2, B:198:0x04e6, B:200:0x04fc, B:202:0x0500, B:207:0x050c, B:208:0x0512, B:212:0x051f, B:218:0x0567, B:219:0x056a, B:213:0x0535, B:215:0x053d, B:216:0x0551, B:220:0x0575, B:240:0x05f5, B:251:0x060e, B:253:0x062c, B:256:0x065d, B:258:0x0667, B:260:0x067d, B:262:0x068e, B:270:0x06b9, B:274:0x06dc, B:276:0x06fb, B:278:0x0726, B:280:0x0742, B:282:0x0752, B:286:0x0761, B:288:0x0767, B:293:0x0777, B:295:0x0789, B:297:0x079c, B:334:0x08c8, B:336:0x08ce, B:345:0x08e7, B:347:0x08ed, B:355:0x0900, B:358:0x090a, B:362:0x0915, B:376:0x0936, B:380:0x0946, B:382:0x0976, B:383:0x097e, B:386:0x0985, B:451:0x0a9b, B:453:0x0ae6, B:454:0x0aed, B:457:0x0af7, B:459:0x0afb, B:461:0x0b01, B:479:0x0b5f, B:502:0x0bbf, B:531:0x0c05, B:540:0x0c44, B:542:0x0c4c, B:545:0x0c54, B:547:0x0c5c, B:551:0x0c67, B:568:0x0cfb, B:572:0x0d0b, B:587:0x0d50, B:589:0x0d56, B:591:0x0d5a, B:593:0x0d65, B:595:0x0d6b, B:597:0x0d75, B:599:0x0d86, B:601:0x0d92, B:603:0x0db2, B:605:0x0dbc, B:607:0x0de8, B:608:0x0df5, B:612:0x0e14, B:614:0x0e1a, B:616:0x0e22, B:618:0x0e28, B:619:0x0e4a, B:575:0x0d1a, B:583:0x0d2f, B:585:0x0d3b, B:552:0x0c8d, B:553:0x0c92, B:554:0x0c95, B:556:0x0c9d, B:558:0x0ca6, B:560:0x0cae, B:564:0x0ce7, B:565:0x0cf0, B:534:0x0c0f, B:536:0x0c17, B:538:0x0c3f, B:586:0x0d42, B:512:0x0bd3, B:517:0x0be0, B:520:0x0bea, B:523:0x0bf3, B:482:0x0b68, B:484:0x0b75, B:477:0x0b5a, B:388:0x099a, B:393:0x09aa, B:397:0x09bc, B:396:0x09b7, B:398:0x09cc, B:400:0x09da, B:401:0x09e3, B:403:0x09eb, B:404:0x09fa, B:405:0x0a03, B:407:0x0a09, B:410:0x0a16, B:413:0x0a20, B:414:0x0a23, B:416:0x0a29, B:419:0x0a32, B:421:0x0a3b, B:424:0x0a43, B:426:0x0a49, B:428:0x0a4d, B:430:0x0a55, B:436:0x0a63, B:438:0x0a69, B:440:0x0a6d, B:442:0x0a75, B:446:0x0a7c, B:448:0x0a8b, B:450:0x0a91, B:296:0x0795, B:299:0x07c4, B:301:0x07d6, B:303:0x07e9, B:302:0x07e2, B:309:0x081d, B:311:0x0825, B:316:0x083d, B:315:0x0838, B:322:0x0879, B:324:0x0885, B:326:0x0898, B:325:0x0891, B:279:0x0733, B:263:0x069a, B:265:0x069e, B:222:0x0584, B:228:0x059a, B:234:0x05dd, B:237:0x05e3, B:229:0x05ae, B:231:0x05b4, B:232:0x05c8, B:184:0x0438, B:187:0x0444, B:188:0x045f, B:179:0x0407, B:170:0x03d8, B:172:0x03e3, B:160:0x03b6, B:161:0x03bd, B:162:0x03c4, B:137:0x036c, B:138:0x0371, B:111:0x0301, B:113:0x0307, B:87:0x0271, B:54:0x0137, B:56:0x013d, B:57:0x0143, B:60:0x014d, B:61:0x0157, B:62:0x0169, B:64:0x016f, B:65:0x0186, B:67:0x018d, B:69:0x0195, B:70:0x01c5, B:51:0x011e, B:72:0x020e, B:562:0x0cb8, B:370:0x0928), top: B:630:0x002c, inners: #1, #3 }] */
    /* JADX WARN: Code duplicated, block: B:409:0x0a14 A[DONT_INVERT] */
    /* JADX WARN: Code duplicated, block: B:410:0x0a16 A[Catch: Exception -> 0x0056, TryCatch #2 {Exception -> 0x0056, blocks: (B:13:0x002c, B:14:0x0038, B:16:0x0040, B:18:0x0051, B:19:0x0053, B:23:0x005a, B:25:0x0062, B:27:0x0074, B:28:0x0077, B:32:0x0080, B:35:0x0088, B:36:0x009e, B:38:0x00a6, B:39:0x00dc, B:41:0x00fe, B:44:0x0106, B:46:0x010e, B:49:0x0115, B:53:0x0129, B:71:0x01ee, B:73:0x0220, B:75:0x0232, B:78:0x023a, B:80:0x023e, B:82:0x025a, B:84:0x0261, B:88:0x0274, B:92:0x0280, B:94:0x028c, B:95:0x0292, B:97:0x029d, B:99:0x02a3, B:101:0x02af, B:102:0x02bb, B:103:0x02c5, B:105:0x02d5, B:107:0x02e5, B:109:0x02eb, B:118:0x0321, B:123:0x033e, B:133:0x0361, B:135:0x0367, B:139:0x0375, B:141:0x037b, B:147:0x0386, B:150:0x0399, B:164:0x03cc, B:166:0x03d0, B:174:0x03eb, B:176:0x03f2, B:178:0x03fa, B:181:0x0428, B:190:0x049f, B:196:0x04c2, B:198:0x04e6, B:200:0x04fc, B:202:0x0500, B:207:0x050c, B:208:0x0512, B:212:0x051f, B:218:0x0567, B:219:0x056a, B:213:0x0535, B:215:0x053d, B:216:0x0551, B:220:0x0575, B:240:0x05f5, B:251:0x060e, B:253:0x062c, B:256:0x065d, B:258:0x0667, B:260:0x067d, B:262:0x068e, B:270:0x06b9, B:274:0x06dc, B:276:0x06fb, B:278:0x0726, B:280:0x0742, B:282:0x0752, B:286:0x0761, B:288:0x0767, B:293:0x0777, B:295:0x0789, B:297:0x079c, B:334:0x08c8, B:336:0x08ce, B:345:0x08e7, B:347:0x08ed, B:355:0x0900, B:358:0x090a, B:362:0x0915, B:376:0x0936, B:380:0x0946, B:382:0x0976, B:383:0x097e, B:386:0x0985, B:451:0x0a9b, B:453:0x0ae6, B:454:0x0aed, B:457:0x0af7, B:459:0x0afb, B:461:0x0b01, B:479:0x0b5f, B:502:0x0bbf, B:531:0x0c05, B:540:0x0c44, B:542:0x0c4c, B:545:0x0c54, B:547:0x0c5c, B:551:0x0c67, B:568:0x0cfb, B:572:0x0d0b, B:587:0x0d50, B:589:0x0d56, B:591:0x0d5a, B:593:0x0d65, B:595:0x0d6b, B:597:0x0d75, B:599:0x0d86, B:601:0x0d92, B:603:0x0db2, B:605:0x0dbc, B:607:0x0de8, B:608:0x0df5, B:612:0x0e14, B:614:0x0e1a, B:616:0x0e22, B:618:0x0e28, B:619:0x0e4a, B:575:0x0d1a, B:583:0x0d2f, B:585:0x0d3b, B:552:0x0c8d, B:553:0x0c92, B:554:0x0c95, B:556:0x0c9d, B:558:0x0ca6, B:560:0x0cae, B:564:0x0ce7, B:565:0x0cf0, B:534:0x0c0f, B:536:0x0c17, B:538:0x0c3f, B:586:0x0d42, B:512:0x0bd3, B:517:0x0be0, B:520:0x0bea, B:523:0x0bf3, B:482:0x0b68, B:484:0x0b75, B:477:0x0b5a, B:388:0x099a, B:393:0x09aa, B:397:0x09bc, B:396:0x09b7, B:398:0x09cc, B:400:0x09da, B:401:0x09e3, B:403:0x09eb, B:404:0x09fa, B:405:0x0a03, B:407:0x0a09, B:410:0x0a16, B:413:0x0a20, B:414:0x0a23, B:416:0x0a29, B:419:0x0a32, B:421:0x0a3b, B:424:0x0a43, B:426:0x0a49, B:428:0x0a4d, B:430:0x0a55, B:436:0x0a63, B:438:0x0a69, B:440:0x0a6d, B:442:0x0a75, B:446:0x0a7c, B:448:0x0a8b, B:450:0x0a91, B:296:0x0795, B:299:0x07c4, B:301:0x07d6, B:303:0x07e9, B:302:0x07e2, B:309:0x081d, B:311:0x0825, B:316:0x083d, B:315:0x0838, B:322:0x0879, B:324:0x0885, B:326:0x0898, B:325:0x0891, B:279:0x0733, B:263:0x069a, B:265:0x069e, B:222:0x0584, B:228:0x059a, B:234:0x05dd, B:237:0x05e3, B:229:0x05ae, B:231:0x05b4, B:232:0x05c8, B:184:0x0438, B:187:0x0444, B:188:0x045f, B:179:0x0407, B:170:0x03d8, B:172:0x03e3, B:160:0x03b6, B:161:0x03bd, B:162:0x03c4, B:137:0x036c, B:138:0x0371, B:111:0x0301, B:113:0x0307, B:87:0x0271, B:54:0x0137, B:56:0x013d, B:57:0x0143, B:60:0x014d, B:61:0x0157, B:62:0x0169, B:64:0x016f, B:65:0x0186, B:67:0x018d, B:69:0x0195, B:70:0x01c5, B:51:0x011e, B:72:0x020e, B:562:0x0cb8, B:370:0x0928), top: B:630:0x002c, inners: #1, #3 }] */
    /* JADX WARN: Code duplicated, block: B:411:0x0a1a  */
    /* JADX WARN: Code duplicated, block: B:413:0x0a20 A[Catch: Exception -> 0x0056, TryCatch #2 {Exception -> 0x0056, blocks: (B:13:0x002c, B:14:0x0038, B:16:0x0040, B:18:0x0051, B:19:0x0053, B:23:0x005a, B:25:0x0062, B:27:0x0074, B:28:0x0077, B:32:0x0080, B:35:0x0088, B:36:0x009e, B:38:0x00a6, B:39:0x00dc, B:41:0x00fe, B:44:0x0106, B:46:0x010e, B:49:0x0115, B:53:0x0129, B:71:0x01ee, B:73:0x0220, B:75:0x0232, B:78:0x023a, B:80:0x023e, B:82:0x025a, B:84:0x0261, B:88:0x0274, B:92:0x0280, B:94:0x028c, B:95:0x0292, B:97:0x029d, B:99:0x02a3, B:101:0x02af, B:102:0x02bb, B:103:0x02c5, B:105:0x02d5, B:107:0x02e5, B:109:0x02eb, B:118:0x0321, B:123:0x033e, B:133:0x0361, B:135:0x0367, B:139:0x0375, B:141:0x037b, B:147:0x0386, B:150:0x0399, B:164:0x03cc, B:166:0x03d0, B:174:0x03eb, B:176:0x03f2, B:178:0x03fa, B:181:0x0428, B:190:0x049f, B:196:0x04c2, B:198:0x04e6, B:200:0x04fc, B:202:0x0500, B:207:0x050c, B:208:0x0512, B:212:0x051f, B:218:0x0567, B:219:0x056a, B:213:0x0535, B:215:0x053d, B:216:0x0551, B:220:0x0575, B:240:0x05f5, B:251:0x060e, B:253:0x062c, B:256:0x065d, B:258:0x0667, B:260:0x067d, B:262:0x068e, B:270:0x06b9, B:274:0x06dc, B:276:0x06fb, B:278:0x0726, B:280:0x0742, B:282:0x0752, B:286:0x0761, B:288:0x0767, B:293:0x0777, B:295:0x0789, B:297:0x079c, B:334:0x08c8, B:336:0x08ce, B:345:0x08e7, B:347:0x08ed, B:355:0x0900, B:358:0x090a, B:362:0x0915, B:376:0x0936, B:380:0x0946, B:382:0x0976, B:383:0x097e, B:386:0x0985, B:451:0x0a9b, B:453:0x0ae6, B:454:0x0aed, B:457:0x0af7, B:459:0x0afb, B:461:0x0b01, B:479:0x0b5f, B:502:0x0bbf, B:531:0x0c05, B:540:0x0c44, B:542:0x0c4c, B:545:0x0c54, B:547:0x0c5c, B:551:0x0c67, B:568:0x0cfb, B:572:0x0d0b, B:587:0x0d50, B:589:0x0d56, B:591:0x0d5a, B:593:0x0d65, B:595:0x0d6b, B:597:0x0d75, B:599:0x0d86, B:601:0x0d92, B:603:0x0db2, B:605:0x0dbc, B:607:0x0de8, B:608:0x0df5, B:612:0x0e14, B:614:0x0e1a, B:616:0x0e22, B:618:0x0e28, B:619:0x0e4a, B:575:0x0d1a, B:583:0x0d2f, B:585:0x0d3b, B:552:0x0c8d, B:553:0x0c92, B:554:0x0c95, B:556:0x0c9d, B:558:0x0ca6, B:560:0x0cae, B:564:0x0ce7, B:565:0x0cf0, B:534:0x0c0f, B:536:0x0c17, B:538:0x0c3f, B:586:0x0d42, B:512:0x0bd3, B:517:0x0be0, B:520:0x0bea, B:523:0x0bf3, B:482:0x0b68, B:484:0x0b75, B:477:0x0b5a, B:388:0x099a, B:393:0x09aa, B:397:0x09bc, B:396:0x09b7, B:398:0x09cc, B:400:0x09da, B:401:0x09e3, B:403:0x09eb, B:404:0x09fa, B:405:0x0a03, B:407:0x0a09, B:410:0x0a16, B:413:0x0a20, B:414:0x0a23, B:416:0x0a29, B:419:0x0a32, B:421:0x0a3b, B:424:0x0a43, B:426:0x0a49, B:428:0x0a4d, B:430:0x0a55, B:436:0x0a63, B:438:0x0a69, B:440:0x0a6d, B:442:0x0a75, B:446:0x0a7c, B:448:0x0a8b, B:450:0x0a91, B:296:0x0795, B:299:0x07c4, B:301:0x07d6, B:303:0x07e9, B:302:0x07e2, B:309:0x081d, B:311:0x0825, B:316:0x083d, B:315:0x0838, B:322:0x0879, B:324:0x0885, B:326:0x0898, B:325:0x0891, B:279:0x0733, B:263:0x069a, B:265:0x069e, B:222:0x0584, B:228:0x059a, B:234:0x05dd, B:237:0x05e3, B:229:0x05ae, B:231:0x05b4, B:232:0x05c8, B:184:0x0438, B:187:0x0444, B:188:0x045f, B:179:0x0407, B:170:0x03d8, B:172:0x03e3, B:160:0x03b6, B:161:0x03bd, B:162:0x03c4, B:137:0x036c, B:138:0x0371, B:111:0x0301, B:113:0x0307, B:87:0x0271, B:54:0x0137, B:56:0x013d, B:57:0x0143, B:60:0x014d, B:61:0x0157, B:62:0x0169, B:64:0x016f, B:65:0x0186, B:67:0x018d, B:69:0x0195, B:70:0x01c5, B:51:0x011e, B:72:0x020e, B:562:0x0cb8, B:370:0x0928), top: B:630:0x002c, inners: #1, #3 }] */
    /* JADX WARN: Code duplicated, block: B:416:0x0a29 A[Catch: Exception -> 0x0056, TryCatch #2 {Exception -> 0x0056, blocks: (B:13:0x002c, B:14:0x0038, B:16:0x0040, B:18:0x0051, B:19:0x0053, B:23:0x005a, B:25:0x0062, B:27:0x0074, B:28:0x0077, B:32:0x0080, B:35:0x0088, B:36:0x009e, B:38:0x00a6, B:39:0x00dc, B:41:0x00fe, B:44:0x0106, B:46:0x010e, B:49:0x0115, B:53:0x0129, B:71:0x01ee, B:73:0x0220, B:75:0x0232, B:78:0x023a, B:80:0x023e, B:82:0x025a, B:84:0x0261, B:88:0x0274, B:92:0x0280, B:94:0x028c, B:95:0x0292, B:97:0x029d, B:99:0x02a3, B:101:0x02af, B:102:0x02bb, B:103:0x02c5, B:105:0x02d5, B:107:0x02e5, B:109:0x02eb, B:118:0x0321, B:123:0x033e, B:133:0x0361, B:135:0x0367, B:139:0x0375, B:141:0x037b, B:147:0x0386, B:150:0x0399, B:164:0x03cc, B:166:0x03d0, B:174:0x03eb, B:176:0x03f2, B:178:0x03fa, B:181:0x0428, B:190:0x049f, B:196:0x04c2, B:198:0x04e6, B:200:0x04fc, B:202:0x0500, B:207:0x050c, B:208:0x0512, B:212:0x051f, B:218:0x0567, B:219:0x056a, B:213:0x0535, B:215:0x053d, B:216:0x0551, B:220:0x0575, B:240:0x05f5, B:251:0x060e, B:253:0x062c, B:256:0x065d, B:258:0x0667, B:260:0x067d, B:262:0x068e, B:270:0x06b9, B:274:0x06dc, B:276:0x06fb, B:278:0x0726, B:280:0x0742, B:282:0x0752, B:286:0x0761, B:288:0x0767, B:293:0x0777, B:295:0x0789, B:297:0x079c, B:334:0x08c8, B:336:0x08ce, B:345:0x08e7, B:347:0x08ed, B:355:0x0900, B:358:0x090a, B:362:0x0915, B:376:0x0936, B:380:0x0946, B:382:0x0976, B:383:0x097e, B:386:0x0985, B:451:0x0a9b, B:453:0x0ae6, B:454:0x0aed, B:457:0x0af7, B:459:0x0afb, B:461:0x0b01, B:479:0x0b5f, B:502:0x0bbf, B:531:0x0c05, B:540:0x0c44, B:542:0x0c4c, B:545:0x0c54, B:547:0x0c5c, B:551:0x0c67, B:568:0x0cfb, B:572:0x0d0b, B:587:0x0d50, B:589:0x0d56, B:591:0x0d5a, B:593:0x0d65, B:595:0x0d6b, B:597:0x0d75, B:599:0x0d86, B:601:0x0d92, B:603:0x0db2, B:605:0x0dbc, B:607:0x0de8, B:608:0x0df5, B:612:0x0e14, B:614:0x0e1a, B:616:0x0e22, B:618:0x0e28, B:619:0x0e4a, B:575:0x0d1a, B:583:0x0d2f, B:585:0x0d3b, B:552:0x0c8d, B:553:0x0c92, B:554:0x0c95, B:556:0x0c9d, B:558:0x0ca6, B:560:0x0cae, B:564:0x0ce7, B:565:0x0cf0, B:534:0x0c0f, B:536:0x0c17, B:538:0x0c3f, B:586:0x0d42, B:512:0x0bd3, B:517:0x0be0, B:520:0x0bea, B:523:0x0bf3, B:482:0x0b68, B:484:0x0b75, B:477:0x0b5a, B:388:0x099a, B:393:0x09aa, B:397:0x09bc, B:396:0x09b7, B:398:0x09cc, B:400:0x09da, B:401:0x09e3, B:403:0x09eb, B:404:0x09fa, B:405:0x0a03, B:407:0x0a09, B:410:0x0a16, B:413:0x0a20, B:414:0x0a23, B:416:0x0a29, B:419:0x0a32, B:421:0x0a3b, B:424:0x0a43, B:426:0x0a49, B:428:0x0a4d, B:430:0x0a55, B:436:0x0a63, B:438:0x0a69, B:440:0x0a6d, B:442:0x0a75, B:446:0x0a7c, B:448:0x0a8b, B:450:0x0a91, B:296:0x0795, B:299:0x07c4, B:301:0x07d6, B:303:0x07e9, B:302:0x07e2, B:309:0x081d, B:311:0x0825, B:316:0x083d, B:315:0x0838, B:322:0x0879, B:324:0x0885, B:326:0x0898, B:325:0x0891, B:279:0x0733, B:263:0x069a, B:265:0x069e, B:222:0x0584, B:228:0x059a, B:234:0x05dd, B:237:0x05e3, B:229:0x05ae, B:231:0x05b4, B:232:0x05c8, B:184:0x0438, B:187:0x0444, B:188:0x045f, B:179:0x0407, B:170:0x03d8, B:172:0x03e3, B:160:0x03b6, B:161:0x03bd, B:162:0x03c4, B:137:0x036c, B:138:0x0371, B:111:0x0301, B:113:0x0307, B:87:0x0271, B:54:0x0137, B:56:0x013d, B:57:0x0143, B:60:0x014d, B:61:0x0157, B:62:0x0169, B:64:0x016f, B:65:0x0186, B:67:0x018d, B:69:0x0195, B:70:0x01c5, B:51:0x011e, B:72:0x020e, B:562:0x0cb8, B:370:0x0928), top: B:630:0x002c, inners: #1, #3 }] */
    /* JADX WARN: Code duplicated, block: B:433:0x0a5c A[PHI: r2
  0x0a5c: PHI (r2v43 org.telegram.tgnet.TLRPC$Chat) = 
  (r2v42 org.telegram.tgnet.TLRPC$Chat)
  (r2v44 org.telegram.tgnet.TLRPC$Chat)
  (r2v44 org.telegram.tgnet.TLRPC$Chat)
  (r2v44 org.telegram.tgnet.TLRPC$Chat)
  (r2v44 org.telegram.tgnet.TLRPC$Chat)
 binds: [B:435:0x0a61, B:425:0x0a47, B:427:0x0a4b, B:429:0x0a53, B:431:0x0a57] A[DONT_GENERATE, DONT_INLINE]] */
    /* JADX WARN: Code duplicated, block: B:446:0x0a7c A[Catch: Exception -> 0x0056, TryCatch #2 {Exception -> 0x0056, blocks: (B:13:0x002c, B:14:0x0038, B:16:0x0040, B:18:0x0051, B:19:0x0053, B:23:0x005a, B:25:0x0062, B:27:0x0074, B:28:0x0077, B:32:0x0080, B:35:0x0088, B:36:0x009e, B:38:0x00a6, B:39:0x00dc, B:41:0x00fe, B:44:0x0106, B:46:0x010e, B:49:0x0115, B:53:0x0129, B:71:0x01ee, B:73:0x0220, B:75:0x0232, B:78:0x023a, B:80:0x023e, B:82:0x025a, B:84:0x0261, B:88:0x0274, B:92:0x0280, B:94:0x028c, B:95:0x0292, B:97:0x029d, B:99:0x02a3, B:101:0x02af, B:102:0x02bb, B:103:0x02c5, B:105:0x02d5, B:107:0x02e5, B:109:0x02eb, B:118:0x0321, B:123:0x033e, B:133:0x0361, B:135:0x0367, B:139:0x0375, B:141:0x037b, B:147:0x0386, B:150:0x0399, B:164:0x03cc, B:166:0x03d0, B:174:0x03eb, B:176:0x03f2, B:178:0x03fa, B:181:0x0428, B:190:0x049f, B:196:0x04c2, B:198:0x04e6, B:200:0x04fc, B:202:0x0500, B:207:0x050c, B:208:0x0512, B:212:0x051f, B:218:0x0567, B:219:0x056a, B:213:0x0535, B:215:0x053d, B:216:0x0551, B:220:0x0575, B:240:0x05f5, B:251:0x060e, B:253:0x062c, B:256:0x065d, B:258:0x0667, B:260:0x067d, B:262:0x068e, B:270:0x06b9, B:274:0x06dc, B:276:0x06fb, B:278:0x0726, B:280:0x0742, B:282:0x0752, B:286:0x0761, B:288:0x0767, B:293:0x0777, B:295:0x0789, B:297:0x079c, B:334:0x08c8, B:336:0x08ce, B:345:0x08e7, B:347:0x08ed, B:355:0x0900, B:358:0x090a, B:362:0x0915, B:376:0x0936, B:380:0x0946, B:382:0x0976, B:383:0x097e, B:386:0x0985, B:451:0x0a9b, B:453:0x0ae6, B:454:0x0aed, B:457:0x0af7, B:459:0x0afb, B:461:0x0b01, B:479:0x0b5f, B:502:0x0bbf, B:531:0x0c05, B:540:0x0c44, B:542:0x0c4c, B:545:0x0c54, B:547:0x0c5c, B:551:0x0c67, B:568:0x0cfb, B:572:0x0d0b, B:587:0x0d50, B:589:0x0d56, B:591:0x0d5a, B:593:0x0d65, B:595:0x0d6b, B:597:0x0d75, B:599:0x0d86, B:601:0x0d92, B:603:0x0db2, B:605:0x0dbc, B:607:0x0de8, B:608:0x0df5, B:612:0x0e14, B:614:0x0e1a, B:616:0x0e22, B:618:0x0e28, B:619:0x0e4a, B:575:0x0d1a, B:583:0x0d2f, B:585:0x0d3b, B:552:0x0c8d, B:553:0x0c92, B:554:0x0c95, B:556:0x0c9d, B:558:0x0ca6, B:560:0x0cae, B:564:0x0ce7, B:565:0x0cf0, B:534:0x0c0f, B:536:0x0c17, B:538:0x0c3f, B:586:0x0d42, B:512:0x0bd3, B:517:0x0be0, B:520:0x0bea, B:523:0x0bf3, B:482:0x0b68, B:484:0x0b75, B:477:0x0b5a, B:388:0x099a, B:393:0x09aa, B:397:0x09bc, B:396:0x09b7, B:398:0x09cc, B:400:0x09da, B:401:0x09e3, B:403:0x09eb, B:404:0x09fa, B:405:0x0a03, B:407:0x0a09, B:410:0x0a16, B:413:0x0a20, B:414:0x0a23, B:416:0x0a29, B:419:0x0a32, B:421:0x0a3b, B:424:0x0a43, B:426:0x0a49, B:428:0x0a4d, B:430:0x0a55, B:436:0x0a63, B:438:0x0a69, B:440:0x0a6d, B:442:0x0a75, B:446:0x0a7c, B:448:0x0a8b, B:450:0x0a91, B:296:0x0795, B:299:0x07c4, B:301:0x07d6, B:303:0x07e9, B:302:0x07e2, B:309:0x081d, B:311:0x0825, B:316:0x083d, B:315:0x0838, B:322:0x0879, B:324:0x0885, B:326:0x0898, B:325:0x0891, B:279:0x0733, B:263:0x069a, B:265:0x069e, B:222:0x0584, B:228:0x059a, B:234:0x05dd, B:237:0x05e3, B:229:0x05ae, B:231:0x05b4, B:232:0x05c8, B:184:0x0438, B:187:0x0444, B:188:0x045f, B:179:0x0407, B:170:0x03d8, B:172:0x03e3, B:160:0x03b6, B:161:0x03bd, B:162:0x03c4, B:137:0x036c, B:138:0x0371, B:111:0x0301, B:113:0x0307, B:87:0x0271, B:54:0x0137, B:56:0x013d, B:57:0x0143, B:60:0x014d, B:61:0x0157, B:62:0x0169, B:64:0x016f, B:65:0x0186, B:67:0x018d, B:69:0x0195, B:70:0x01c5, B:51:0x011e, B:72:0x020e, B:562:0x0cb8, B:370:0x0928), top: B:630:0x002c, inners: #1, #3 }] */
    /* JADX WARN: Code duplicated, block: B:448:0x0a8b A[Catch: Exception -> 0x0056, TryCatch #2 {Exception -> 0x0056, blocks: (B:13:0x002c, B:14:0x0038, B:16:0x0040, B:18:0x0051, B:19:0x0053, B:23:0x005a, B:25:0x0062, B:27:0x0074, B:28:0x0077, B:32:0x0080, B:35:0x0088, B:36:0x009e, B:38:0x00a6, B:39:0x00dc, B:41:0x00fe, B:44:0x0106, B:46:0x010e, B:49:0x0115, B:53:0x0129, B:71:0x01ee, B:73:0x0220, B:75:0x0232, B:78:0x023a, B:80:0x023e, B:82:0x025a, B:84:0x0261, B:88:0x0274, B:92:0x0280, B:94:0x028c, B:95:0x0292, B:97:0x029d, B:99:0x02a3, B:101:0x02af, B:102:0x02bb, B:103:0x02c5, B:105:0x02d5, B:107:0x02e5, B:109:0x02eb, B:118:0x0321, B:123:0x033e, B:133:0x0361, B:135:0x0367, B:139:0x0375, B:141:0x037b, B:147:0x0386, B:150:0x0399, B:164:0x03cc, B:166:0x03d0, B:174:0x03eb, B:176:0x03f2, B:178:0x03fa, B:181:0x0428, B:190:0x049f, B:196:0x04c2, B:198:0x04e6, B:200:0x04fc, B:202:0x0500, B:207:0x050c, B:208:0x0512, B:212:0x051f, B:218:0x0567, B:219:0x056a, B:213:0x0535, B:215:0x053d, B:216:0x0551, B:220:0x0575, B:240:0x05f5, B:251:0x060e, B:253:0x062c, B:256:0x065d, B:258:0x0667, B:260:0x067d, B:262:0x068e, B:270:0x06b9, B:274:0x06dc, B:276:0x06fb, B:278:0x0726, B:280:0x0742, B:282:0x0752, B:286:0x0761, B:288:0x0767, B:293:0x0777, B:295:0x0789, B:297:0x079c, B:334:0x08c8, B:336:0x08ce, B:345:0x08e7, B:347:0x08ed, B:355:0x0900, B:358:0x090a, B:362:0x0915, B:376:0x0936, B:380:0x0946, B:382:0x0976, B:383:0x097e, B:386:0x0985, B:451:0x0a9b, B:453:0x0ae6, B:454:0x0aed, B:457:0x0af7, B:459:0x0afb, B:461:0x0b01, B:479:0x0b5f, B:502:0x0bbf, B:531:0x0c05, B:540:0x0c44, B:542:0x0c4c, B:545:0x0c54, B:547:0x0c5c, B:551:0x0c67, B:568:0x0cfb, B:572:0x0d0b, B:587:0x0d50, B:589:0x0d56, B:591:0x0d5a, B:593:0x0d65, B:595:0x0d6b, B:597:0x0d75, B:599:0x0d86, B:601:0x0d92, B:603:0x0db2, B:605:0x0dbc, B:607:0x0de8, B:608:0x0df5, B:612:0x0e14, B:614:0x0e1a, B:616:0x0e22, B:618:0x0e28, B:619:0x0e4a, B:575:0x0d1a, B:583:0x0d2f, B:585:0x0d3b, B:552:0x0c8d, B:553:0x0c92, B:554:0x0c95, B:556:0x0c9d, B:558:0x0ca6, B:560:0x0cae, B:564:0x0ce7, B:565:0x0cf0, B:534:0x0c0f, B:536:0x0c17, B:538:0x0c3f, B:586:0x0d42, B:512:0x0bd3, B:517:0x0be0, B:520:0x0bea, B:523:0x0bf3, B:482:0x0b68, B:484:0x0b75, B:477:0x0b5a, B:388:0x099a, B:393:0x09aa, B:397:0x09bc, B:396:0x09b7, B:398:0x09cc, B:400:0x09da, B:401:0x09e3, B:403:0x09eb, B:404:0x09fa, B:405:0x0a03, B:407:0x0a09, B:410:0x0a16, B:413:0x0a20, B:414:0x0a23, B:416:0x0a29, B:419:0x0a32, B:421:0x0a3b, B:424:0x0a43, B:426:0x0a49, B:428:0x0a4d, B:430:0x0a55, B:436:0x0a63, B:438:0x0a69, B:440:0x0a6d, B:442:0x0a75, B:446:0x0a7c, B:448:0x0a8b, B:450:0x0a91, B:296:0x0795, B:299:0x07c4, B:301:0x07d6, B:303:0x07e9, B:302:0x07e2, B:309:0x081d, B:311:0x0825, B:316:0x083d, B:315:0x0838, B:322:0x0879, B:324:0x0885, B:326:0x0898, B:325:0x0891, B:279:0x0733, B:263:0x069a, B:265:0x069e, B:222:0x0584, B:228:0x059a, B:234:0x05dd, B:237:0x05e3, B:229:0x05ae, B:231:0x05b4, B:232:0x05c8, B:184:0x0438, B:187:0x0444, B:188:0x045f, B:179:0x0407, B:170:0x03d8, B:172:0x03e3, B:160:0x03b6, B:161:0x03bd, B:162:0x03c4, B:137:0x036c, B:138:0x0371, B:111:0x0301, B:113:0x0307, B:87:0x0271, B:54:0x0137, B:56:0x013d, B:57:0x0143, B:60:0x014d, B:61:0x0157, B:62:0x0169, B:64:0x016f, B:65:0x0186, B:67:0x018d, B:69:0x0195, B:70:0x01c5, B:51:0x011e, B:72:0x020e, B:562:0x0cb8, B:370:0x0928), top: B:630:0x002c, inners: #1, #3 }] */
    /* JADX WARN: Code duplicated, block: B:453:0x0ae6 A[Catch: Exception -> 0x0056, TryCatch #2 {Exception -> 0x0056, blocks: (B:13:0x002c, B:14:0x0038, B:16:0x0040, B:18:0x0051, B:19:0x0053, B:23:0x005a, B:25:0x0062, B:27:0x0074, B:28:0x0077, B:32:0x0080, B:35:0x0088, B:36:0x009e, B:38:0x00a6, B:39:0x00dc, B:41:0x00fe, B:44:0x0106, B:46:0x010e, B:49:0x0115, B:53:0x0129, B:71:0x01ee, B:73:0x0220, B:75:0x0232, B:78:0x023a, B:80:0x023e, B:82:0x025a, B:84:0x0261, B:88:0x0274, B:92:0x0280, B:94:0x028c, B:95:0x0292, B:97:0x029d, B:99:0x02a3, B:101:0x02af, B:102:0x02bb, B:103:0x02c5, B:105:0x02d5, B:107:0x02e5, B:109:0x02eb, B:118:0x0321, B:123:0x033e, B:133:0x0361, B:135:0x0367, B:139:0x0375, B:141:0x037b, B:147:0x0386, B:150:0x0399, B:164:0x03cc, B:166:0x03d0, B:174:0x03eb, B:176:0x03f2, B:178:0x03fa, B:181:0x0428, B:190:0x049f, B:196:0x04c2, B:198:0x04e6, B:200:0x04fc, B:202:0x0500, B:207:0x050c, B:208:0x0512, B:212:0x051f, B:218:0x0567, B:219:0x056a, B:213:0x0535, B:215:0x053d, B:216:0x0551, B:220:0x0575, B:240:0x05f5, B:251:0x060e, B:253:0x062c, B:256:0x065d, B:258:0x0667, B:260:0x067d, B:262:0x068e, B:270:0x06b9, B:274:0x06dc, B:276:0x06fb, B:278:0x0726, B:280:0x0742, B:282:0x0752, B:286:0x0761, B:288:0x0767, B:293:0x0777, B:295:0x0789, B:297:0x079c, B:334:0x08c8, B:336:0x08ce, B:345:0x08e7, B:347:0x08ed, B:355:0x0900, B:358:0x090a, B:362:0x0915, B:376:0x0936, B:380:0x0946, B:382:0x0976, B:383:0x097e, B:386:0x0985, B:451:0x0a9b, B:453:0x0ae6, B:454:0x0aed, B:457:0x0af7, B:459:0x0afb, B:461:0x0b01, B:479:0x0b5f, B:502:0x0bbf, B:531:0x0c05, B:540:0x0c44, B:542:0x0c4c, B:545:0x0c54, B:547:0x0c5c, B:551:0x0c67, B:568:0x0cfb, B:572:0x0d0b, B:587:0x0d50, B:589:0x0d56, B:591:0x0d5a, B:593:0x0d65, B:595:0x0d6b, B:597:0x0d75, B:599:0x0d86, B:601:0x0d92, B:603:0x0db2, B:605:0x0dbc, B:607:0x0de8, B:608:0x0df5, B:612:0x0e14, B:614:0x0e1a, B:616:0x0e22, B:618:0x0e28, B:619:0x0e4a, B:575:0x0d1a, B:583:0x0d2f, B:585:0x0d3b, B:552:0x0c8d, B:553:0x0c92, B:554:0x0c95, B:556:0x0c9d, B:558:0x0ca6, B:560:0x0cae, B:564:0x0ce7, B:565:0x0cf0, B:534:0x0c0f, B:536:0x0c17, B:538:0x0c3f, B:586:0x0d42, B:512:0x0bd3, B:517:0x0be0, B:520:0x0bea, B:523:0x0bf3, B:482:0x0b68, B:484:0x0b75, B:477:0x0b5a, B:388:0x099a, B:393:0x09aa, B:397:0x09bc, B:396:0x09b7, B:398:0x09cc, B:400:0x09da, B:401:0x09e3, B:403:0x09eb, B:404:0x09fa, B:405:0x0a03, B:407:0x0a09, B:410:0x0a16, B:413:0x0a20, B:414:0x0a23, B:416:0x0a29, B:419:0x0a32, B:421:0x0a3b, B:424:0x0a43, B:426:0x0a49, B:428:0x0a4d, B:430:0x0a55, B:436:0x0a63, B:438:0x0a69, B:440:0x0a6d, B:442:0x0a75, B:446:0x0a7c, B:448:0x0a8b, B:450:0x0a91, B:296:0x0795, B:299:0x07c4, B:301:0x07d6, B:303:0x07e9, B:302:0x07e2, B:309:0x081d, B:311:0x0825, B:316:0x083d, B:315:0x0838, B:322:0x0879, B:324:0x0885, B:326:0x0898, B:325:0x0891, B:279:0x0733, B:263:0x069a, B:265:0x069e, B:222:0x0584, B:228:0x059a, B:234:0x05dd, B:237:0x05e3, B:229:0x05ae, B:231:0x05b4, B:232:0x05c8, B:184:0x0438, B:187:0x0444, B:188:0x045f, B:179:0x0407, B:170:0x03d8, B:172:0x03e3, B:160:0x03b6, B:161:0x03bd, B:162:0x03c4, B:137:0x036c, B:138:0x0371, B:111:0x0301, B:113:0x0307, B:87:0x0271, B:54:0x0137, B:56:0x013d, B:57:0x0143, B:60:0x014d, B:61:0x0157, B:62:0x0169, B:64:0x016f, B:65:0x0186, B:67:0x018d, B:69:0x0195, B:70:0x01c5, B:51:0x011e, B:72:0x020e, B:562:0x0cb8, B:370:0x0928), top: B:630:0x002c, inners: #1, #3 }] */
    /* JADX WARN: Code duplicated, block: B:456:0x0af5 A[ADDED_TO_REGION] */
    /* JADX WARN: Code duplicated, block: B:464:0x0b34 A[Catch: all -> 0x0b3c, TryCatch #5 {all -> 0x0b3c, blocks: (B:462:0x0b18, B:464:0x0b34, B:467:0x0b3e, B:471:0x0b46, B:475:0x0b4e), top: B:635:0x0b18 }] */
    /* JADX WARN: Code duplicated, block: B:469:0x0b42  */
    /* JADX WARN: Code duplicated, block: B:474:0x0b4d  */
    /* JADX WARN: Code duplicated, block: B:479:0x0b5f A[Catch: Exception -> 0x0056, TryCatch #2 {Exception -> 0x0056, blocks: (B:13:0x002c, B:14:0x0038, B:16:0x0040, B:18:0x0051, B:19:0x0053, B:23:0x005a, B:25:0x0062, B:27:0x0074, B:28:0x0077, B:32:0x0080, B:35:0x0088, B:36:0x009e, B:38:0x00a6, B:39:0x00dc, B:41:0x00fe, B:44:0x0106, B:46:0x010e, B:49:0x0115, B:53:0x0129, B:71:0x01ee, B:73:0x0220, B:75:0x0232, B:78:0x023a, B:80:0x023e, B:82:0x025a, B:84:0x0261, B:88:0x0274, B:92:0x0280, B:94:0x028c, B:95:0x0292, B:97:0x029d, B:99:0x02a3, B:101:0x02af, B:102:0x02bb, B:103:0x02c5, B:105:0x02d5, B:107:0x02e5, B:109:0x02eb, B:118:0x0321, B:123:0x033e, B:133:0x0361, B:135:0x0367, B:139:0x0375, B:141:0x037b, B:147:0x0386, B:150:0x0399, B:164:0x03cc, B:166:0x03d0, B:174:0x03eb, B:176:0x03f2, B:178:0x03fa, B:181:0x0428, B:190:0x049f, B:196:0x04c2, B:198:0x04e6, B:200:0x04fc, B:202:0x0500, B:207:0x050c, B:208:0x0512, B:212:0x051f, B:218:0x0567, B:219:0x056a, B:213:0x0535, B:215:0x053d, B:216:0x0551, B:220:0x0575, B:240:0x05f5, B:251:0x060e, B:253:0x062c, B:256:0x065d, B:258:0x0667, B:260:0x067d, B:262:0x068e, B:270:0x06b9, B:274:0x06dc, B:276:0x06fb, B:278:0x0726, B:280:0x0742, B:282:0x0752, B:286:0x0761, B:288:0x0767, B:293:0x0777, B:295:0x0789, B:297:0x079c, B:334:0x08c8, B:336:0x08ce, B:345:0x08e7, B:347:0x08ed, B:355:0x0900, B:358:0x090a, B:362:0x0915, B:376:0x0936, B:380:0x0946, B:382:0x0976, B:383:0x097e, B:386:0x0985, B:451:0x0a9b, B:453:0x0ae6, B:454:0x0aed, B:457:0x0af7, B:459:0x0afb, B:461:0x0b01, B:479:0x0b5f, B:502:0x0bbf, B:531:0x0c05, B:540:0x0c44, B:542:0x0c4c, B:545:0x0c54, B:547:0x0c5c, B:551:0x0c67, B:568:0x0cfb, B:572:0x0d0b, B:587:0x0d50, B:589:0x0d56, B:591:0x0d5a, B:593:0x0d65, B:595:0x0d6b, B:597:0x0d75, B:599:0x0d86, B:601:0x0d92, B:603:0x0db2, B:605:0x0dbc, B:607:0x0de8, B:608:0x0df5, B:612:0x0e14, B:614:0x0e1a, B:616:0x0e22, B:618:0x0e28, B:619:0x0e4a, B:575:0x0d1a, B:583:0x0d2f, B:585:0x0d3b, B:552:0x0c8d, B:553:0x0c92, B:554:0x0c95, B:556:0x0c9d, B:558:0x0ca6, B:560:0x0cae, B:564:0x0ce7, B:565:0x0cf0, B:534:0x0c0f, B:536:0x0c17, B:538:0x0c3f, B:586:0x0d42, B:512:0x0bd3, B:517:0x0be0, B:520:0x0bea, B:523:0x0bf3, B:482:0x0b68, B:484:0x0b75, B:477:0x0b5a, B:388:0x099a, B:393:0x09aa, B:397:0x09bc, B:396:0x09b7, B:398:0x09cc, B:400:0x09da, B:401:0x09e3, B:403:0x09eb, B:404:0x09fa, B:405:0x0a03, B:407:0x0a09, B:410:0x0a16, B:413:0x0a20, B:414:0x0a23, B:416:0x0a29, B:419:0x0a32, B:421:0x0a3b, B:424:0x0a43, B:426:0x0a49, B:428:0x0a4d, B:430:0x0a55, B:436:0x0a63, B:438:0x0a69, B:440:0x0a6d, B:442:0x0a75, B:446:0x0a7c, B:448:0x0a8b, B:450:0x0a91, B:296:0x0795, B:299:0x07c4, B:301:0x07d6, B:303:0x07e9, B:302:0x07e2, B:309:0x081d, B:311:0x0825, B:316:0x083d, B:315:0x0838, B:322:0x0879, B:324:0x0885, B:326:0x0898, B:325:0x0891, B:279:0x0733, B:263:0x069a, B:265:0x069e, B:222:0x0584, B:228:0x059a, B:234:0x05dd, B:237:0x05e3, B:229:0x05ae, B:231:0x05b4, B:232:0x05c8, B:184:0x0438, B:187:0x0444, B:188:0x045f, B:179:0x0407, B:170:0x03d8, B:172:0x03e3, B:160:0x03b6, B:161:0x03bd, B:162:0x03c4, B:137:0x036c, B:138:0x0371, B:111:0x0301, B:113:0x0307, B:87:0x0271, B:54:0x0137, B:56:0x013d, B:57:0x0143, B:60:0x014d, B:61:0x0157, B:62:0x0169, B:64:0x016f, B:65:0x0186, B:67:0x018d, B:69:0x0195, B:70:0x01c5, B:51:0x011e, B:72:0x020e, B:562:0x0cb8, B:370:0x0928), top: B:630:0x002c, inners: #1, #3 }] */
    /* JADX WARN: Code duplicated, block: B:481:0x0b66 A[DONT_INVERT] */
    /* JADX WARN: Code duplicated, block: B:482:0x0b68 A[Catch: Exception -> 0x0056, TryCatch #2 {Exception -> 0x0056, blocks: (B:13:0x002c, B:14:0x0038, B:16:0x0040, B:18:0x0051, B:19:0x0053, B:23:0x005a, B:25:0x0062, B:27:0x0074, B:28:0x0077, B:32:0x0080, B:35:0x0088, B:36:0x009e, B:38:0x00a6, B:39:0x00dc, B:41:0x00fe, B:44:0x0106, B:46:0x010e, B:49:0x0115, B:53:0x0129, B:71:0x01ee, B:73:0x0220, B:75:0x0232, B:78:0x023a, B:80:0x023e, B:82:0x025a, B:84:0x0261, B:88:0x0274, B:92:0x0280, B:94:0x028c, B:95:0x0292, B:97:0x029d, B:99:0x02a3, B:101:0x02af, B:102:0x02bb, B:103:0x02c5, B:105:0x02d5, B:107:0x02e5, B:109:0x02eb, B:118:0x0321, B:123:0x033e, B:133:0x0361, B:135:0x0367, B:139:0x0375, B:141:0x037b, B:147:0x0386, B:150:0x0399, B:164:0x03cc, B:166:0x03d0, B:174:0x03eb, B:176:0x03f2, B:178:0x03fa, B:181:0x0428, B:190:0x049f, B:196:0x04c2, B:198:0x04e6, B:200:0x04fc, B:202:0x0500, B:207:0x050c, B:208:0x0512, B:212:0x051f, B:218:0x0567, B:219:0x056a, B:213:0x0535, B:215:0x053d, B:216:0x0551, B:220:0x0575, B:240:0x05f5, B:251:0x060e, B:253:0x062c, B:256:0x065d, B:258:0x0667, B:260:0x067d, B:262:0x068e, B:270:0x06b9, B:274:0x06dc, B:276:0x06fb, B:278:0x0726, B:280:0x0742, B:282:0x0752, B:286:0x0761, B:288:0x0767, B:293:0x0777, B:295:0x0789, B:297:0x079c, B:334:0x08c8, B:336:0x08ce, B:345:0x08e7, B:347:0x08ed, B:355:0x0900, B:358:0x090a, B:362:0x0915, B:376:0x0936, B:380:0x0946, B:382:0x0976, B:383:0x097e, B:386:0x0985, B:451:0x0a9b, B:453:0x0ae6, B:454:0x0aed, B:457:0x0af7, B:459:0x0afb, B:461:0x0b01, B:479:0x0b5f, B:502:0x0bbf, B:531:0x0c05, B:540:0x0c44, B:542:0x0c4c, B:545:0x0c54, B:547:0x0c5c, B:551:0x0c67, B:568:0x0cfb, B:572:0x0d0b, B:587:0x0d50, B:589:0x0d56, B:591:0x0d5a, B:593:0x0d65, B:595:0x0d6b, B:597:0x0d75, B:599:0x0d86, B:601:0x0d92, B:603:0x0db2, B:605:0x0dbc, B:607:0x0de8, B:608:0x0df5, B:612:0x0e14, B:614:0x0e1a, B:616:0x0e22, B:618:0x0e28, B:619:0x0e4a, B:575:0x0d1a, B:583:0x0d2f, B:585:0x0d3b, B:552:0x0c8d, B:553:0x0c92, B:554:0x0c95, B:556:0x0c9d, B:558:0x0ca6, B:560:0x0cae, B:564:0x0ce7, B:565:0x0cf0, B:534:0x0c0f, B:536:0x0c17, B:538:0x0c3f, B:586:0x0d42, B:512:0x0bd3, B:517:0x0be0, B:520:0x0bea, B:523:0x0bf3, B:482:0x0b68, B:484:0x0b75, B:477:0x0b5a, B:388:0x099a, B:393:0x09aa, B:397:0x09bc, B:396:0x09b7, B:398:0x09cc, B:400:0x09da, B:401:0x09e3, B:403:0x09eb, B:404:0x09fa, B:405:0x0a03, B:407:0x0a09, B:410:0x0a16, B:413:0x0a20, B:414:0x0a23, B:416:0x0a29, B:419:0x0a32, B:421:0x0a3b, B:424:0x0a43, B:426:0x0a49, B:428:0x0a4d, B:430:0x0a55, B:436:0x0a63, B:438:0x0a69, B:440:0x0a6d, B:442:0x0a75, B:446:0x0a7c, B:448:0x0a8b, B:450:0x0a91, B:296:0x0795, B:299:0x07c4, B:301:0x07d6, B:303:0x07e9, B:302:0x07e2, B:309:0x081d, B:311:0x0825, B:316:0x083d, B:315:0x0838, B:322:0x0879, B:324:0x0885, B:326:0x0898, B:325:0x0891, B:279:0x0733, B:263:0x069a, B:265:0x069e, B:222:0x0584, B:228:0x059a, B:234:0x05dd, B:237:0x05e3, B:229:0x05ae, B:231:0x05b4, B:232:0x05c8, B:184:0x0438, B:187:0x0444, B:188:0x045f, B:179:0x0407, B:170:0x03d8, B:172:0x03e3, B:160:0x03b6, B:161:0x03bd, B:162:0x03c4, B:137:0x036c, B:138:0x0371, B:111:0x0301, B:113:0x0307, B:87:0x0271, B:54:0x0137, B:56:0x013d, B:57:0x0143, B:60:0x014d, B:61:0x0157, B:62:0x0169, B:64:0x016f, B:65:0x0186, B:67:0x018d, B:69:0x0195, B:70:0x01c5, B:51:0x011e, B:72:0x020e, B:562:0x0cb8, B:370:0x0928), top: B:630:0x002c, inners: #1, #3 }] */
    /* JADX WARN: Code duplicated, block: B:484:0x0b75 A[Catch: Exception -> 0x0056, TRY_LEAVE, TryCatch #2 {Exception -> 0x0056, blocks: (B:13:0x002c, B:14:0x0038, B:16:0x0040, B:18:0x0051, B:19:0x0053, B:23:0x005a, B:25:0x0062, B:27:0x0074, B:28:0x0077, B:32:0x0080, B:35:0x0088, B:36:0x009e, B:38:0x00a6, B:39:0x00dc, B:41:0x00fe, B:44:0x0106, B:46:0x010e, B:49:0x0115, B:53:0x0129, B:71:0x01ee, B:73:0x0220, B:75:0x0232, B:78:0x023a, B:80:0x023e, B:82:0x025a, B:84:0x0261, B:88:0x0274, B:92:0x0280, B:94:0x028c, B:95:0x0292, B:97:0x029d, B:99:0x02a3, B:101:0x02af, B:102:0x02bb, B:103:0x02c5, B:105:0x02d5, B:107:0x02e5, B:109:0x02eb, B:118:0x0321, B:123:0x033e, B:133:0x0361, B:135:0x0367, B:139:0x0375, B:141:0x037b, B:147:0x0386, B:150:0x0399, B:164:0x03cc, B:166:0x03d0, B:174:0x03eb, B:176:0x03f2, B:178:0x03fa, B:181:0x0428, B:190:0x049f, B:196:0x04c2, B:198:0x04e6, B:200:0x04fc, B:202:0x0500, B:207:0x050c, B:208:0x0512, B:212:0x051f, B:218:0x0567, B:219:0x056a, B:213:0x0535, B:215:0x053d, B:216:0x0551, B:220:0x0575, B:240:0x05f5, B:251:0x060e, B:253:0x062c, B:256:0x065d, B:258:0x0667, B:260:0x067d, B:262:0x068e, B:270:0x06b9, B:274:0x06dc, B:276:0x06fb, B:278:0x0726, B:280:0x0742, B:282:0x0752, B:286:0x0761, B:288:0x0767, B:293:0x0777, B:295:0x0789, B:297:0x079c, B:334:0x08c8, B:336:0x08ce, B:345:0x08e7, B:347:0x08ed, B:355:0x0900, B:358:0x090a, B:362:0x0915, B:376:0x0936, B:380:0x0946, B:382:0x0976, B:383:0x097e, B:386:0x0985, B:451:0x0a9b, B:453:0x0ae6, B:454:0x0aed, B:457:0x0af7, B:459:0x0afb, B:461:0x0b01, B:479:0x0b5f, B:502:0x0bbf, B:531:0x0c05, B:540:0x0c44, B:542:0x0c4c, B:545:0x0c54, B:547:0x0c5c, B:551:0x0c67, B:568:0x0cfb, B:572:0x0d0b, B:587:0x0d50, B:589:0x0d56, B:591:0x0d5a, B:593:0x0d65, B:595:0x0d6b, B:597:0x0d75, B:599:0x0d86, B:601:0x0d92, B:603:0x0db2, B:605:0x0dbc, B:607:0x0de8, B:608:0x0df5, B:612:0x0e14, B:614:0x0e1a, B:616:0x0e22, B:618:0x0e28, B:619:0x0e4a, B:575:0x0d1a, B:583:0x0d2f, B:585:0x0d3b, B:552:0x0c8d, B:553:0x0c92, B:554:0x0c95, B:556:0x0c9d, B:558:0x0ca6, B:560:0x0cae, B:564:0x0ce7, B:565:0x0cf0, B:534:0x0c0f, B:536:0x0c17, B:538:0x0c3f, B:586:0x0d42, B:512:0x0bd3, B:517:0x0be0, B:520:0x0bea, B:523:0x0bf3, B:482:0x0b68, B:484:0x0b75, B:477:0x0b5a, B:388:0x099a, B:393:0x09aa, B:397:0x09bc, B:396:0x09b7, B:398:0x09cc, B:400:0x09da, B:401:0x09e3, B:403:0x09eb, B:404:0x09fa, B:405:0x0a03, B:407:0x0a09, B:410:0x0a16, B:413:0x0a20, B:414:0x0a23, B:416:0x0a29, B:419:0x0a32, B:421:0x0a3b, B:424:0x0a43, B:426:0x0a49, B:428:0x0a4d, B:430:0x0a55, B:436:0x0a63, B:438:0x0a69, B:440:0x0a6d, B:442:0x0a75, B:446:0x0a7c, B:448:0x0a8b, B:450:0x0a91, B:296:0x0795, B:299:0x07c4, B:301:0x07d6, B:303:0x07e9, B:302:0x07e2, B:309:0x081d, B:311:0x0825, B:316:0x083d, B:315:0x0838, B:322:0x0879, B:324:0x0885, B:326:0x0898, B:325:0x0891, B:279:0x0733, B:263:0x069a, B:265:0x069e, B:222:0x0584, B:228:0x059a, B:234:0x05dd, B:237:0x05e3, B:229:0x05ae, B:231:0x05b4, B:232:0x05c8, B:184:0x0438, B:187:0x0444, B:188:0x045f, B:179:0x0407, B:170:0x03d8, B:172:0x03e3, B:160:0x03b6, B:161:0x03bd, B:162:0x03c4, B:137:0x036c, B:138:0x0371, B:111:0x0301, B:113:0x0307, B:87:0x0271, B:54:0x0137, B:56:0x013d, B:57:0x0143, B:60:0x014d, B:61:0x0157, B:62:0x0169, B:64:0x016f, B:65:0x0186, B:67:0x018d, B:69:0x0195, B:70:0x01c5, B:51:0x011e, B:72:0x020e, B:562:0x0cb8, B:370:0x0928), top: B:630:0x002c, inners: #1, #3 }] */
    /* JADX WARN: Code duplicated, block: B:488:0x0b8c A[Catch: all -> 0x0bb3, TryCatch #0 {all -> 0x0bb3, blocks: (B:486:0x0b7d, B:488:0x0b8c, B:492:0x0ba4, B:494:0x0bb0, B:491:0x0ba3), top: B:626:0x0b7d }] */
    /* JADX WARN: Code duplicated, block: B:490:0x0ba1  */
    /* JADX WARN: Code duplicated, block: B:491:0x0ba3 A[Catch: all -> 0x0bb3, TryCatch #0 {all -> 0x0bb3, blocks: (B:486:0x0b7d, B:488:0x0b8c, B:492:0x0ba4, B:494:0x0bb0, B:491:0x0ba3), top: B:626:0x0b7d }] */
    /* JADX WARN: Code duplicated, block: B:494:0x0bb0 A[Catch: all -> 0x0bb3, TRY_LEAVE, TryCatch #0 {all -> 0x0bb3, blocks: (B:486:0x0b7d, B:488:0x0b8c, B:492:0x0ba4, B:494:0x0bb0, B:491:0x0ba3), top: B:626:0x0b7d }] */
    /* JADX WARN: Code duplicated, block: B:497:0x0bb7  */
    /* JADX WARN: Code duplicated, block: B:499:0x0bba  */
    /* JADX WARN: Code duplicated, block: B:522:0x0bf1  */
    /* JADX WARN: Code duplicated, block: B:525:0x0bfa  */
    /* JADX WARN: Code duplicated, block: B:526:0x0bfb  */
    /* JADX WARN: Code duplicated, block: B:529:0x0c01 A[ADDED_TO_REGION] */
    /* JADX WARN: Code duplicated, block: B:586:0x0d42 A[Catch: Exception -> 0x0056, TryCatch #2 {Exception -> 0x0056, blocks: (B:13:0x002c, B:14:0x0038, B:16:0x0040, B:18:0x0051, B:19:0x0053, B:23:0x005a, B:25:0x0062, B:27:0x0074, B:28:0x0077, B:32:0x0080, B:35:0x0088, B:36:0x009e, B:38:0x00a6, B:39:0x00dc, B:41:0x00fe, B:44:0x0106, B:46:0x010e, B:49:0x0115, B:53:0x0129, B:71:0x01ee, B:73:0x0220, B:75:0x0232, B:78:0x023a, B:80:0x023e, B:82:0x025a, B:84:0x0261, B:88:0x0274, B:92:0x0280, B:94:0x028c, B:95:0x0292, B:97:0x029d, B:99:0x02a3, B:101:0x02af, B:102:0x02bb, B:103:0x02c5, B:105:0x02d5, B:107:0x02e5, B:109:0x02eb, B:118:0x0321, B:123:0x033e, B:133:0x0361, B:135:0x0367, B:139:0x0375, B:141:0x037b, B:147:0x0386, B:150:0x0399, B:164:0x03cc, B:166:0x03d0, B:174:0x03eb, B:176:0x03f2, B:178:0x03fa, B:181:0x0428, B:190:0x049f, B:196:0x04c2, B:198:0x04e6, B:200:0x04fc, B:202:0x0500, B:207:0x050c, B:208:0x0512, B:212:0x051f, B:218:0x0567, B:219:0x056a, B:213:0x0535, B:215:0x053d, B:216:0x0551, B:220:0x0575, B:240:0x05f5, B:251:0x060e, B:253:0x062c, B:256:0x065d, B:258:0x0667, B:260:0x067d, B:262:0x068e, B:270:0x06b9, B:274:0x06dc, B:276:0x06fb, B:278:0x0726, B:280:0x0742, B:282:0x0752, B:286:0x0761, B:288:0x0767, B:293:0x0777, B:295:0x0789, B:297:0x079c, B:334:0x08c8, B:336:0x08ce, B:345:0x08e7, B:347:0x08ed, B:355:0x0900, B:358:0x090a, B:362:0x0915, B:376:0x0936, B:380:0x0946, B:382:0x0976, B:383:0x097e, B:386:0x0985, B:451:0x0a9b, B:453:0x0ae6, B:454:0x0aed, B:457:0x0af7, B:459:0x0afb, B:461:0x0b01, B:479:0x0b5f, B:502:0x0bbf, B:531:0x0c05, B:540:0x0c44, B:542:0x0c4c, B:545:0x0c54, B:547:0x0c5c, B:551:0x0c67, B:568:0x0cfb, B:572:0x0d0b, B:587:0x0d50, B:589:0x0d56, B:591:0x0d5a, B:593:0x0d65, B:595:0x0d6b, B:597:0x0d75, B:599:0x0d86, B:601:0x0d92, B:603:0x0db2, B:605:0x0dbc, B:607:0x0de8, B:608:0x0df5, B:612:0x0e14, B:614:0x0e1a, B:616:0x0e22, B:618:0x0e28, B:619:0x0e4a, B:575:0x0d1a, B:583:0x0d2f, B:585:0x0d3b, B:552:0x0c8d, B:553:0x0c92, B:554:0x0c95, B:556:0x0c9d, B:558:0x0ca6, B:560:0x0cae, B:564:0x0ce7, B:565:0x0cf0, B:534:0x0c0f, B:536:0x0c17, B:538:0x0c3f, B:586:0x0d42, B:512:0x0bd3, B:517:0x0be0, B:520:0x0bea, B:523:0x0bf3, B:482:0x0b68, B:484:0x0b75, B:477:0x0b5a, B:388:0x099a, B:393:0x09aa, B:397:0x09bc, B:396:0x09b7, B:398:0x09cc, B:400:0x09da, B:401:0x09e3, B:403:0x09eb, B:404:0x09fa, B:405:0x0a03, B:407:0x0a09, B:410:0x0a16, B:413:0x0a20, B:414:0x0a23, B:416:0x0a29, B:419:0x0a32, B:421:0x0a3b, B:424:0x0a43, B:426:0x0a49, B:428:0x0a4d, B:430:0x0a55, B:436:0x0a63, B:438:0x0a69, B:440:0x0a6d, B:442:0x0a75, B:446:0x0a7c, B:448:0x0a8b, B:450:0x0a91, B:296:0x0795, B:299:0x07c4, B:301:0x07d6, B:303:0x07e9, B:302:0x07e2, B:309:0x081d, B:311:0x0825, B:316:0x083d, B:315:0x0838, B:322:0x0879, B:324:0x0885, B:326:0x0898, B:325:0x0891, B:279:0x0733, B:263:0x069a, B:265:0x069e, B:222:0x0584, B:228:0x059a, B:234:0x05dd, B:237:0x05e3, B:229:0x05ae, B:231:0x05b4, B:232:0x05c8, B:184:0x0438, B:187:0x0444, B:188:0x045f, B:179:0x0407, B:170:0x03d8, B:172:0x03e3, B:160:0x03b6, B:161:0x03bd, B:162:0x03c4, B:137:0x036c, B:138:0x0371, B:111:0x0301, B:113:0x0307, B:87:0x0271, B:54:0x0137, B:56:0x013d, B:57:0x0143, B:60:0x014d, B:61:0x0157, B:62:0x0169, B:64:0x016f, B:65:0x0186, B:67:0x018d, B:69:0x0195, B:70:0x01c5, B:51:0x011e, B:72:0x020e, B:562:0x0cb8, B:370:0x0928), top: B:630:0x002c, inners: #1, #3 }] */
    /* JADX WARN: Code duplicated, block: B:589:0x0d56 A[Catch: Exception -> 0x0056, TryCatch #2 {Exception -> 0x0056, blocks: (B:13:0x002c, B:14:0x0038, B:16:0x0040, B:18:0x0051, B:19:0x0053, B:23:0x005a, B:25:0x0062, B:27:0x0074, B:28:0x0077, B:32:0x0080, B:35:0x0088, B:36:0x009e, B:38:0x00a6, B:39:0x00dc, B:41:0x00fe, B:44:0x0106, B:46:0x010e, B:49:0x0115, B:53:0x0129, B:71:0x01ee, B:73:0x0220, B:75:0x0232, B:78:0x023a, B:80:0x023e, B:82:0x025a, B:84:0x0261, B:88:0x0274, B:92:0x0280, B:94:0x028c, B:95:0x0292, B:97:0x029d, B:99:0x02a3, B:101:0x02af, B:102:0x02bb, B:103:0x02c5, B:105:0x02d5, B:107:0x02e5, B:109:0x02eb, B:118:0x0321, B:123:0x033e, B:133:0x0361, B:135:0x0367, B:139:0x0375, B:141:0x037b, B:147:0x0386, B:150:0x0399, B:164:0x03cc, B:166:0x03d0, B:174:0x03eb, B:176:0x03f2, B:178:0x03fa, B:181:0x0428, B:190:0x049f, B:196:0x04c2, B:198:0x04e6, B:200:0x04fc, B:202:0x0500, B:207:0x050c, B:208:0x0512, B:212:0x051f, B:218:0x0567, B:219:0x056a, B:213:0x0535, B:215:0x053d, B:216:0x0551, B:220:0x0575, B:240:0x05f5, B:251:0x060e, B:253:0x062c, B:256:0x065d, B:258:0x0667, B:260:0x067d, B:262:0x068e, B:270:0x06b9, B:274:0x06dc, B:276:0x06fb, B:278:0x0726, B:280:0x0742, B:282:0x0752, B:286:0x0761, B:288:0x0767, B:293:0x0777, B:295:0x0789, B:297:0x079c, B:334:0x08c8, B:336:0x08ce, B:345:0x08e7, B:347:0x08ed, B:355:0x0900, B:358:0x090a, B:362:0x0915, B:376:0x0936, B:380:0x0946, B:382:0x0976, B:383:0x097e, B:386:0x0985, B:451:0x0a9b, B:453:0x0ae6, B:454:0x0aed, B:457:0x0af7, B:459:0x0afb, B:461:0x0b01, B:479:0x0b5f, B:502:0x0bbf, B:531:0x0c05, B:540:0x0c44, B:542:0x0c4c, B:545:0x0c54, B:547:0x0c5c, B:551:0x0c67, B:568:0x0cfb, B:572:0x0d0b, B:587:0x0d50, B:589:0x0d56, B:591:0x0d5a, B:593:0x0d65, B:595:0x0d6b, B:597:0x0d75, B:599:0x0d86, B:601:0x0d92, B:603:0x0db2, B:605:0x0dbc, B:607:0x0de8, B:608:0x0df5, B:612:0x0e14, B:614:0x0e1a, B:616:0x0e22, B:618:0x0e28, B:619:0x0e4a, B:575:0x0d1a, B:583:0x0d2f, B:585:0x0d3b, B:552:0x0c8d, B:553:0x0c92, B:554:0x0c95, B:556:0x0c9d, B:558:0x0ca6, B:560:0x0cae, B:564:0x0ce7, B:565:0x0cf0, B:534:0x0c0f, B:536:0x0c17, B:538:0x0c3f, B:586:0x0d42, B:512:0x0bd3, B:517:0x0be0, B:520:0x0bea, B:523:0x0bf3, B:482:0x0b68, B:484:0x0b75, B:477:0x0b5a, B:388:0x099a, B:393:0x09aa, B:397:0x09bc, B:396:0x09b7, B:398:0x09cc, B:400:0x09da, B:401:0x09e3, B:403:0x09eb, B:404:0x09fa, B:405:0x0a03, B:407:0x0a09, B:410:0x0a16, B:413:0x0a20, B:414:0x0a23, B:416:0x0a29, B:419:0x0a32, B:421:0x0a3b, B:424:0x0a43, B:426:0x0a49, B:428:0x0a4d, B:430:0x0a55, B:436:0x0a63, B:438:0x0a69, B:440:0x0a6d, B:442:0x0a75, B:446:0x0a7c, B:448:0x0a8b, B:450:0x0a91, B:296:0x0795, B:299:0x07c4, B:301:0x07d6, B:303:0x07e9, B:302:0x07e2, B:309:0x081d, B:311:0x0825, B:316:0x083d, B:315:0x0838, B:322:0x0879, B:324:0x0885, B:326:0x0898, B:325:0x0891, B:279:0x0733, B:263:0x069a, B:265:0x069e, B:222:0x0584, B:228:0x059a, B:234:0x05dd, B:237:0x05e3, B:229:0x05ae, B:231:0x05b4, B:232:0x05c8, B:184:0x0438, B:187:0x0444, B:188:0x045f, B:179:0x0407, B:170:0x03d8, B:172:0x03e3, B:160:0x03b6, B:161:0x03bd, B:162:0x03c4, B:137:0x036c, B:138:0x0371, B:111:0x0301, B:113:0x0307, B:87:0x0271, B:54:0x0137, B:56:0x013d, B:57:0x0143, B:60:0x014d, B:61:0x0157, B:62:0x0169, B:64:0x016f, B:65:0x0186, B:67:0x018d, B:69:0x0195, B:70:0x01c5, B:51:0x011e, B:72:0x020e, B:562:0x0cb8, B:370:0x0928), top: B:630:0x002c, inners: #1, #3 }] */
    /* JADX WARN: Code duplicated, block: B:610:0x0e0c  */
    /* JADX WARN: Code duplicated, block: B:612:0x0e14 A[Catch: Exception -> 0x0056, TryCatch #2 {Exception -> 0x0056, blocks: (B:13:0x002c, B:14:0x0038, B:16:0x0040, B:18:0x0051, B:19:0x0053, B:23:0x005a, B:25:0x0062, B:27:0x0074, B:28:0x0077, B:32:0x0080, B:35:0x0088, B:36:0x009e, B:38:0x00a6, B:39:0x00dc, B:41:0x00fe, B:44:0x0106, B:46:0x010e, B:49:0x0115, B:53:0x0129, B:71:0x01ee, B:73:0x0220, B:75:0x0232, B:78:0x023a, B:80:0x023e, B:82:0x025a, B:84:0x0261, B:88:0x0274, B:92:0x0280, B:94:0x028c, B:95:0x0292, B:97:0x029d, B:99:0x02a3, B:101:0x02af, B:102:0x02bb, B:103:0x02c5, B:105:0x02d5, B:107:0x02e5, B:109:0x02eb, B:118:0x0321, B:123:0x033e, B:133:0x0361, B:135:0x0367, B:139:0x0375, B:141:0x037b, B:147:0x0386, B:150:0x0399, B:164:0x03cc, B:166:0x03d0, B:174:0x03eb, B:176:0x03f2, B:178:0x03fa, B:181:0x0428, B:190:0x049f, B:196:0x04c2, B:198:0x04e6, B:200:0x04fc, B:202:0x0500, B:207:0x050c, B:208:0x0512, B:212:0x051f, B:218:0x0567, B:219:0x056a, B:213:0x0535, B:215:0x053d, B:216:0x0551, B:220:0x0575, B:240:0x05f5, B:251:0x060e, B:253:0x062c, B:256:0x065d, B:258:0x0667, B:260:0x067d, B:262:0x068e, B:270:0x06b9, B:274:0x06dc, B:276:0x06fb, B:278:0x0726, B:280:0x0742, B:282:0x0752, B:286:0x0761, B:288:0x0767, B:293:0x0777, B:295:0x0789, B:297:0x079c, B:334:0x08c8, B:336:0x08ce, B:345:0x08e7, B:347:0x08ed, B:355:0x0900, B:358:0x090a, B:362:0x0915, B:376:0x0936, B:380:0x0946, B:382:0x0976, B:383:0x097e, B:386:0x0985, B:451:0x0a9b, B:453:0x0ae6, B:454:0x0aed, B:457:0x0af7, B:459:0x0afb, B:461:0x0b01, B:479:0x0b5f, B:502:0x0bbf, B:531:0x0c05, B:540:0x0c44, B:542:0x0c4c, B:545:0x0c54, B:547:0x0c5c, B:551:0x0c67, B:568:0x0cfb, B:572:0x0d0b, B:587:0x0d50, B:589:0x0d56, B:591:0x0d5a, B:593:0x0d65, B:595:0x0d6b, B:597:0x0d75, B:599:0x0d86, B:601:0x0d92, B:603:0x0db2, B:605:0x0dbc, B:607:0x0de8, B:608:0x0df5, B:612:0x0e14, B:614:0x0e1a, B:616:0x0e22, B:618:0x0e28, B:619:0x0e4a, B:575:0x0d1a, B:583:0x0d2f, B:585:0x0d3b, B:552:0x0c8d, B:553:0x0c92, B:554:0x0c95, B:556:0x0c9d, B:558:0x0ca6, B:560:0x0cae, B:564:0x0ce7, B:565:0x0cf0, B:534:0x0c0f, B:536:0x0c17, B:538:0x0c3f, B:586:0x0d42, B:512:0x0bd3, B:517:0x0be0, B:520:0x0bea, B:523:0x0bf3, B:482:0x0b68, B:484:0x0b75, B:477:0x0b5a, B:388:0x099a, B:393:0x09aa, B:397:0x09bc, B:396:0x09b7, B:398:0x09cc, B:400:0x09da, B:401:0x09e3, B:403:0x09eb, B:404:0x09fa, B:405:0x0a03, B:407:0x0a09, B:410:0x0a16, B:413:0x0a20, B:414:0x0a23, B:416:0x0a29, B:419:0x0a32, B:421:0x0a3b, B:424:0x0a43, B:426:0x0a49, B:428:0x0a4d, B:430:0x0a55, B:436:0x0a63, B:438:0x0a69, B:440:0x0a6d, B:442:0x0a75, B:446:0x0a7c, B:448:0x0a8b, B:450:0x0a91, B:296:0x0795, B:299:0x07c4, B:301:0x07d6, B:303:0x07e9, B:302:0x07e2, B:309:0x081d, B:311:0x0825, B:316:0x083d, B:315:0x0838, B:322:0x0879, B:324:0x0885, B:326:0x0898, B:325:0x0891, B:279:0x0733, B:263:0x069a, B:265:0x069e, B:222:0x0584, B:228:0x059a, B:234:0x05dd, B:237:0x05e3, B:229:0x05ae, B:231:0x05b4, B:232:0x05c8, B:184:0x0438, B:187:0x0444, B:188:0x045f, B:179:0x0407, B:170:0x03d8, B:172:0x03e3, B:160:0x03b6, B:161:0x03bd, B:162:0x03c4, B:137:0x036c, B:138:0x0371, B:111:0x0301, B:113:0x0307, B:87:0x0271, B:54:0x0137, B:56:0x013d, B:57:0x0143, B:60:0x014d, B:61:0x0157, B:62:0x0169, B:64:0x016f, B:65:0x0186, B:67:0x018d, B:69:0x0195, B:70:0x01c5, B:51:0x011e, B:72:0x020e, B:562:0x0cb8, B:370:0x0928), top: B:630:0x002c, inners: #1, #3 }] */
    /* JADX WARN: Code duplicated, block: B:626:0x0b7d A[EXC_TOP_SPLITTER, SYNTHETIC] */
    /* JADX WARN: Instruction removed from duplicated block: B:179:0x0407, please report this as an issue */
    /* JADX WARN: Instruction removed from duplicated block: B:187:0x0444, please report this as an issue */
    /* JADX WARN: Instruction removed from duplicated block: B:188:0x045f, please report this as an issue */
    /* JADX WARN: Instruction removed from duplicated block: B:231:0x05b4, please report this as an issue */
    /* JADX WARN: Instruction removed from duplicated block: B:232:0x05c8, please report this as an issue */
    /* JADX WARN: Instruction removed from duplicated block: B:270:0x06b9, please report this as an issue */
    /* JADX WARN: Multi-variable type inference failed */
    /* JADX WARN: Type inference failed for: r11v13 */
    /* JADX WARN: Type inference failed for: r11v14, types: [android.net.Uri] */
    /* JADX WARN: Type inference failed for: r11v17 */
    /* JADX WARN: Type inference failed for: r11v18 */
    /* JADX WARN: Type inference failed for: r11v19 */
    /* JADX WARN: Type inference failed for: r14v10 */
    /* JADX WARN: Type inference failed for: r14v23 */
    /* JADX WARN: Type inference failed for: r14v24 */
    /* JADX WARN: Type inference failed for: r14v25 */
    /* JADX WARN: Type inference failed for: r14v26 */
    /* JADX WARN: Type inference failed for: r14v37 */
    /* JADX WARN: Type inference failed for: r14v38 */
    /* JADX WARN: Type inference failed for: r14v39 */
    /* JADX WARN: Type inference failed for: r14v40 */
    /* JADX WARN: Type inference failed for: r14v41 */
    /* JADX WARN: Type inference failed for: r1v10 */
    /* JADX WARN: Type inference failed for: r1v11 */
    /* JADX WARN: Type inference failed for: r1v12 */
    /* JADX WARN: Type inference failed for: r1v6, types: [org.telegram.messenger.BaseController, org.telegram.messenger.NotificationsController] */
    /* JADX WARN: Unreachable blocks removed: 2, instructions: 3 */
    private void showOrUpdateNotification(boolean z) {
        String str;
        long j;
        long j2;
        MessageObject messageObject;
        Bitmap bitmap;
        TLRPC.Chat chat;
        boolean z2;
        long j3;
        Bitmap bitmap2;
        TLRPC.User user;
        long j4;
        long j5;
        boolean zIsGlobalNotificationsEnabled;
        SharedPreferences sharedPreferences;
        ?? r1;
        String userName;
        boolean z3;
        String string;
        String string2;
        boolean z4;
        boolean z5;
        long j6;
        TLRPC.Chat chat2;
        String firstName;
        NotificationCompat.Builder builder;
        int i;
        long j7;
        CharSequence charSequence;
        boolean[] zArr;
        String stringForMessage;
        boolean zIsSilentMessage;
        String strReplace;
        String str2;
        ?? r14;
        String str3;
        boolean z6;
        String str4;
        CharSequence charSequence2;
        SharedPreferences sharedPreferences2;
        long j8;
        boolean z7;
        long j9;
        String path;
        boolean z8;
        long j10;
        long j11;
        long j12;
        int property;
        int property2;
        boolean z9;
        String propertyString;
        Integer numValueOf;
        boolean z10;
        long j13;
        String string3;
        boolean z11;
        int i2;
        String str5;
        boolean z12;
        int iIntValue;
        int i3;
        int i4;
        int i5;
        int i6;
        int i7;
        String str6;
        int i8;
        boolean z13;
        boolean z14;
        int i9;
        int i10;
        int i11;
        String str7;
        Intent intent;
        String str8;
        long j14;
        int i12;
        int i13;
        long j15;
        TLRPC.User user2;
        TLRPC.Chat chat3;
        TLRPC.FileLocation fileLocation;
        long[] jArr;
        int i14;
        int i15;
        NotificationCompat.Builder builder2;
        long[] jArr2;
        BitmapDrawable imageFromMemory;
        File pathToAttach;
        float fDp;
        int i16;
        Bitmap bitmapDecodeFile;
        int i17;
        int i18;
        long[] jArr3;
        ?? r11;
        int i19;
        long[] jArr4;
        boolean z15;
        TLRPC.ReplyMarkup replyMarkup;
        Object obj;
        Uri uri;
        Uri uriForFile;
        Intent intent2;
        int i20;
        String str9;
        int ringerMode;
        String string4;
        boolean z16;
        String string5;
        boolean z17;
        String string6;
        boolean z18;
        long property3;
        int i21;
        int iMin;
        boolean[] zArr2;
        int i22;
        ?? IsSilentMessage;
        String stringForMessage2;
        CharSequence charSequence3;
        boolean z19;
        NotificationsController notificationsController;
        boolean z20;
        int i23;
        String pluralString;
        if (!getUserConfig().isClientActivated() || ((this.pushMessages.isEmpty() && this.storyPushMessages.isEmpty()) || (!SharedConfig.showNotificationsForAllAccounts && this.currentAccount != UserConfig.selectedAccount))) {
            dismissNotification();
            return;
        }
        try {
            getConnectionsManager().resumeNetworkMaybe();
            long j16 = 0;
            Object obj2 = null;
            for (int i24 = 0; i24 < this.pushMessages.size(); i24++) {
                MessageObject messageObject2 = this.pushMessages.get(i24);
                int i25 = messageObject2.messageOwner.date;
                if (j16 < i25) {
                    j16 = i25;
                    obj2 = messageObject2;
                }
            }
            for (int i26 = 0; i26 < this.storyPushMessages.size(); i26++) {
                StoryNotification storyNotification = this.storyPushMessages.get(i26);
                long j17 = storyNotification.date;
                if (j16 < j17 / 1000) {
                    obj2 = storyNotification;
                    j16 = j17 / 1000;
                }
            }
            if (obj2 == null) {
                return;
            }
            boolean z21 = obj2 instanceof StoryNotification;
            String str10 = _UrlKt.FRAGMENT_ENCODE_SET;
            if (z21) {
                StoryNotification storyNotification2 = (StoryNotification) obj2;
                TLRPC.TL_message tL_message = new TLRPC.TL_message();
                tL_message.date = (int) (System.currentTimeMillis() / 1000);
                int i27 = 0;
                boolean z22 = false;
                j = 1000;
                j2 = 0;
                int size = 0;
                while (i27 < this.storyPushMessages.size()) {
                    z22 |= this.storyPushMessages.get(i27).hidden;
                    tL_message.date = Math.min(tL_message.date, (int) (this.storyPushMessages.get(i27).date / 1000));
                    size += this.storyPushMessages.get(i27).dateByIds.size();
                    i27++;
                    str10 = str10;
                }
                String str11 = str10;
                TLRPC.TL_peerUser tL_peerUser = new TLRPC.TL_peerUser();
                long j18 = storyNotification2.dialogId;
                tL_peerUser.user_id = j18;
                tL_message.dialog_id = j18;
                tL_message.peer_id = tL_peerUser;
                ArrayList<String> arrayList = new ArrayList<>();
                ArrayList<Object> arrayList2 = new ArrayList<>();
                parseStoryPushes(arrayList, arrayList2);
                Bitmap bitmapLoadMultipleAvatars = SharedConfig.getDevicePerformanceClass() >= 1 ? loadMultipleAvatars(arrayList2) : null;
                if (z22 || this.storyPushMessages.size() >= 2 || arrayList.isEmpty()) {
                    pluralString = LocaleController.formatPluralString("Stories", size, new Object[0]);
                } else {
                    pluralString = arrayList.get(0);
                }
                String str12 = pluralString;
                if (z22) {
                    tL_message.message = LocaleController.formatPluralString("StoryNotificationHidden", size, new Object[0]);
                    str = str11;
                } else if (arrayList.isEmpty()) {
                    str = str11;
                    tL_message.message = str;
                } else {
                    str = str11;
                    if (arrayList.size() == 1) {
                        if (size == 1) {
                            tL_message.message = LocaleController.getString("StoryNotificationSingle");
                        } else {
                            tL_message.message = LocaleController.formatPluralString("StoryNotification1", size, arrayList.get(0));
                        }
                    } else if (arrayList.size() == 2) {
                        tL_message.message = LocaleController.formatString(R.string.StoryNotification2, arrayList.get(0), arrayList.get(1));
                    } else if (arrayList.size() == 3 && this.storyPushMessages.size() == 3) {
                        tL_message.message = LocaleController.formatString(R.string.StoryNotification3, cutLastName(arrayList.get(0)), cutLastName(arrayList.get(1)), cutLastName(arrayList.get(2)));
                    } else {
                        tL_message.message = LocaleController.formatPluralString("StoryNotification4", this.storyPushMessages.size() - 2, cutLastName(arrayList.get(0)), cutLastName(arrayList.get(1)));
                    }
                }
                MessageObject messageObject3 = new MessageObject(this.currentAccount, tL_message, tL_message.message, str12, str12, false, false, false, false);
                messageObject3.isStoryPush = true;
                messageObject = messageObject3;
                bitmap = bitmapLoadMultipleAvatars;
            } else {
                str = _UrlKt.FRAGMENT_ENCODE_SET;
                j = 1000;
                j2 = 0;
                messageObject = this.pushMessages.get(0);
                bitmap = null;
            }
            SharedPreferences notificationsSettings = getAccountInstance().getNotificationsSettings();
            int i28 = notificationsSettings.getInt("dismissDate", 0);
            if (!messageObject.isStoryPush && (i23 = messageObject.messageOwner.date) > 0 && i23 <= i28) {
                dismissNotification();
                return;
            }
            long dialogId = messageObject.getDialogId();
            long topicId = MessageObject.getTopicId(this.currentAccount, messageObject.messageOwner, getMessagesController().isForum(messageObject));
            boolean z23 = messageObject.isStoryPush;
            long fromChatId = messageObject.messageOwner.mentioned ? messageObject.getFromChatId() : dialogId;
            messageObject.getId();
            TLRPC.Peer peer = messageObject.messageOwner.peer_id;
            long jLongValue = peer.chat_id;
            if (jLongValue == j2) {
                jLongValue = peer.channel_id;
            }
            CharSequence charSequence4 = str;
            long jLongValue2 = peer.user_id;
            if (messageObject.isFromUser() && (jLongValue2 == j2 || jLongValue2 == getUserConfig().getClientUserId())) {
                jLongValue2 = messageObject.messageOwner.from_id.user_id;
            }
            if (messageObject.getDialogId() == UserObject.VERIFY && messageObject.getForwardedFromId() != null) {
                if (messageObject.getForwardedFromId().longValue() >= j2) {
                    jLongValue2 = messageObject.getForwardedFromId().longValue();
                    jLongValue = j2;
                } else {
                    jLongValue = messageObject.getForwardedFromId().longValue();
                    jLongValue2 = j2;
                }
            }
            TLRPC.User user3 = getMessagesController().getUser(Long.valueOf(jLongValue2));
            if (jLongValue != j2) {
                long j19 = jLongValue;
                TLRPC.Chat chat4 = getMessagesController().getChat(Long.valueOf(j19));
                if (chat4 == null && messageObject.isFcmMessage()) {
                    z20 = messageObject.localChannel;
                } else {
                    z20 = ChatObject.isChannel(chat4) && !chat4.megagroup;
                }
                z2 = z20;
                j3 = j19;
                chat = chat4;
            } else {
                chat = null;
                z2 = false;
                j3 = jLongValue;
            }
            int notifyOverride = getNotifyOverride(notificationsSettings, fromChatId, topicId);
            long j20 = fromChatId;
            if (notifyOverride == -1) {
                try {
                    Boolean boolValueOf = Boolean.valueOf(z2);
                    boolean z24 = messageObject.isReactionPush;
                    NotificationsController notificationsController2 = this;
                    bitmap2 = bitmap;
                    user = user3;
                    j4 = dialogId;
                    j5 = topicId;
                    zIsGlobalNotificationsEnabled = notificationsController2.isGlobalNotificationsEnabled(j4, boolValueOf, z24, z24);
                    sharedPreferences = notificationsSettings;
                    r1 = notificationsController2;
                } catch (Exception e) {
                    e = e;
                    FileLog.e(e);
                }
            } else {
                notificationsController = this;
                bitmap2 = bitmap;
                user = user3;
                j4 = dialogId;
                j5 = topicId;
                zIsGlobalNotificationsEnabled = notifyOverride != 2;
            }
            if (((r15 != 0 && chat == null) || user == null) && messageObject.isFcmMessage()) {
                userName = messageObject.localName;
            } else if (chat != null) {
                userName = r1.getTitle(chat);
            } else {
                userName = UserObject.getUserName(user);
            }
            boolean z25 = AndroidUtilities.needShowPasscode() || SharedConfig.isWaitingForPasscodeEnter;
            boolean z26 = zIsGlobalNotificationsEnabled;
            String str13 = userName;
            boolean zEqualsIgnoreCase = "samsung".equalsIgnoreCase(Build.MANUFACTURER);
            try {
                if (!DialogObject.isEncryptedDialog(j4)) {
                    if (!zEqualsIgnoreCase) {
                        z3 = zEqualsIgnoreCase;
                        z19 = true;
                        if (r1.pushDialogs.size() <= 1) {
                        }
                        if (!messageObject.isReactionPush || messageObject.isStoryReactionPush) {
                            z5 = z4;
                            if (!sharedPreferences.getBoolean("EnableReactionsPreview", true)) {
                                string2 = LocaleController.getString(R.string.NotificationHiddenName);
                            }
                        } else {
                            z5 = z4;
                        }
                        if (z3) {
                            j6 = jLongValue2;
                            chat2 = chat;
                            firstName = charSequence4;
                        } else {
                            if (UserConfig.getActivatedAccountsCount() <= 1) {
                                firstName = charSequence4;
                            } else if (r1.pushDialogs.size() == 1) {
                                firstName = UserObject.getFirstName(r1.getUserConfig().getCurrentUser());
                            } else {
                                firstName = UserObject.getFirstName(r1.getUserConfig().getCurrentUser()) + "・";
                            }
                            chat2 = chat;
                            if (r1.pushDialogs.size() == 1) {
                                j6 = jLongValue2;
                            } else {
                                j6 = jLongValue2;
                                if (r1.pushDialogs.size() == 1) {
                                    firstName = firstName + LocaleController.formatPluralString("NewMessages", r1.total_unread_count, new Object[0]);
                                } else {
                                    firstName = firstName + LocaleController.formatString(R.string.NotificationMessagesPeopleDisplayOrder, LocaleController.formatPluralString("NewMessages", r1.total_unread_count, new Object[0]), LocaleController.formatPluralString("FromChats", r1.pushDialogs.size(), new Object[0]));
                                }
                            }
                        }
                        builder = new NotificationCompat.Builder(ApplicationLoader.applicationContext);
                        i = 1;
                        if (r1.pushMessages.size() <= 1) {
                            j7 = j4;
                            charSequence = charSequence4;
                            zArr = new boolean[i];
                            stringForMessage = r1.getStringForMessage(messageObject, false, zArr, null);
                            zIsSilentMessage = r1.isSilentMessage(messageObject);
                            if (stringForMessage == null) {
                                return;
                            }
                            if (z5) {
                                strReplace = stringForMessage;
                            } else if (chat2 == null && !z3) {
                                strReplace = stringForMessage.replace(" @ " + string2, charSequence);
                            } else if (zArr[0]) {
                                strReplace = stringForMessage.replace(string2 + ": ", charSequence);
                            } else {
                                strReplace = stringForMessage.replace(string2 + " ", charSequence);
                            }
                            builder.setContentText(strReplace);
                            if (z3) {
                                firstName = strReplace;
                            }
                            builder.setStyle(new NotificationCompat.BigTextStyle().bigText(strReplace));
                            str2 = stringForMessage;
                            r14 = zIsSilentMessage;
                        } else if (z3) {
                            i = 1;
                            j7 = j4;
                            charSequence = charSequence4;
                            zArr = new boolean[i];
                            stringForMessage = r1.getStringForMessage(messageObject, false, zArr, null);
                            zIsSilentMessage = r1.isSilentMessage(messageObject);
                            if (stringForMessage == null) {
                                return;
                            }
                            if (z5) {
                                strReplace = stringForMessage;
                            } else if (chat2 == null) {
                                if (zArr[0]) {
                                    strReplace = stringForMessage.replace(string2 + ": ", charSequence);
                                } else {
                                    strReplace = stringForMessage.replace(string2 + " ", charSequence);
                                }
                            } else if (zArr[0]) {
                                strReplace = stringForMessage.replace(string2 + ": ", charSequence);
                            } else {
                                strReplace = stringForMessage.replace(string2 + " ", charSequence);
                            }
                            builder.setContentText(strReplace);
                            if (z3) {
                                firstName = strReplace;
                            }
                            builder.setStyle(new NotificationCompat.BigTextStyle().bigText(strReplace));
                            str2 = stringForMessage;
                            r14 = zIsSilentMessage;
                        } else {
                            builder.setContentText(firstName);
                            NotificationCompat.InboxStyle inboxStyle = new NotificationCompat.InboxStyle();
                            inboxStyle.setBigContentTitle(string2);
                            iMin = Math.min(10, r1.pushMessages.size());
                            zArr2 = new boolean[1];
                            i22 = 0;
                            IsSilentMessage = 2;
                            String str14 = null;
                            while (i22 < iMin) {
                                int i29 = iMin;
                                MessageObject messageObject4 = r1.pushMessages.get(i22);
                                long j21 = j4;
                                int i30 = i22;
                                stringForMessage2 = r1.getStringForMessage(messageObject4, false, zArr2, null);
                                if (stringForMessage2 != null || (!messageObject4.isStoryPush && messageObject4.messageOwner.date <= i28)) {
                                    charSequence3 = charSequence4;
                                } else {
                                    IsSilentMessage = IsSilentMessage;
                                    if (IsSilentMessage == 2) {
                                        str14 = stringForMessage2;
                                        IsSilentMessage = r1.isSilentMessage(messageObject4);
                                    }
                                    if (r1.pushDialogs.size() != 1 || !z5) {
                                        charSequence3 = charSequence4;
                                    } else if (chat2 != null) {
                                        charSequence3 = charSequence4;
                                        stringForMessage2 = stringForMessage2.replace(" @ " + string2, charSequence3);
                                    } else {
                                        charSequence3 = charSequence4;
                                        stringForMessage2 = zArr2[0] ? stringForMessage2.replace(string2 + ": ", charSequence3) : stringForMessage2.replace(string2 + " ", charSequence3);
                                    }
                                    inboxStyle.addLine(stringForMessage2);
                                }
                                charSequence4 = charSequence3;
                                iMin = i29;
                                i22 = i30 + 1;
                                j4 = j21;
                                IsSilentMessage = IsSilentMessage;
                            }
                            j7 = j4;
                            charSequence = charSequence4;
                            inboxStyle.setSummaryText(firstName);
                            builder.setStyle(inboxStyle);
                            str2 = str14;
                            r14 = IsSilentMessage;
                        }
                        str3 = firstName;
                        if (z || !z26 || MediaController.getInstance().isRecordingAudio() || r14 == 1) {
                            z6 = true;
                        } else {
                            z6 = false;
                        }
                        if (z6 && j7 == j20 && chat2 != null) {
                            StringBuilder sb = new StringBuilder();
                            sb.append(NotificationsSettingsFacade.PROPERTY_CUSTOM);
                            j8 = j7;
                            sb.append(j8);
                            sharedPreferences2 = sharedPreferences;
                            int i31 = 180;
                            if (sharedPreferences2.getBoolean(sb.toString(), false)) {
                                i21 = sharedPreferences2.getInt("smart_max_count_" + j8, 2);
                                i31 = sharedPreferences2.getInt("smart_delay_" + j8, 180);
                            } else {
                                i21 = 2;
                            }
                            if (i21 == 0) {
                                str4 = str3;
                                charSequence2 = charSequence;
                            } else {
                                Point point = (Point) r1.smartNotificationsDialogs.get(j8);
                                if (point == null) {
                                    r1.smartNotificationsDialogs.put(j8, new Point(1, (int) (SystemClock.elapsedRealtime() / j)));
                                    str4 = str3;
                                    charSequence2 = charSequence;
                                } else {
                                    int i32 = point.y + i31;
                                    str4 = str3;
                                    charSequence2 = charSequence;
                                    if (i32 < SystemClock.elapsedRealtime() / j) {
                                        point.set(1, (int) (SystemClock.elapsedRealtime() / j));
                                    } else {
                                        int i33 = point.x;
                                        if (i33 < i21) {
                                            point.set(i33 + 1, (int) (SystemClock.elapsedRealtime() / j));
                                        } else {
                                            z7 = true;
                                        }
                                    }
                                }
                            }
                            if (z7) {
                                j9 = j5;
                            } else {
                                if (!sharedPreferences2.getBoolean("sound_enabled_" + getSharedPrefKey(j8, j9), true)) {
                                    j9 = j5;
                                    z7 = true;
                                }
                            }
                            j9 = j5;
                            path = Settings.System.DEFAULT_NOTIFICATION_URI.getPath();
                            z8 = ApplicationLoader.mainInterfacePaused;
                            boolean z27 = !z8;
                            getSharedPrefKey(j8, j9);
                            j10 = j8;
                            j11 = j9;
                            if (r1.dialogsNotificationsFacade.getProperty(NotificationsSettingsFacade.PROPERTY_CUSTOM, j10, j11, false)) {
                                property = r1.dialogsNotificationsFacade.getProperty("vibrate_", j10, j11, 0);
                                property2 = r1.dialogsNotificationsFacade.getProperty("priority_", j10, j11, 3);
                                property3 = r1.dialogsNotificationsFacade.getProperty("sound_document_id_", j10, j11, 0L);
                                if (property3 != j2) {
                                    propertyString = r1.getMediaDataController().ringtoneDataStore.getSoundPath(property3);
                                    z9 = true;
                                } else {
                                    propertyString = r1.dialogsNotificationsFacade.getPropertyString("sound_path_", j10, j11, null);
                                    z9 = false;
                                }
                                int property4 = r1.dialogsNotificationsFacade.getProperty("color_", j10, j11, 0);
                                j12 = j10;
                                numValueOf = property4 != 0 ? Integer.valueOf(property4) : null;
                                z10 = z7;
                                if (!messageObject.isReactionPush || messageObject.isStoryReactionPush) {
                                    j13 = sharedPreferences2.getLong("ReactionSoundDocId", 0L);
                                    if (j13 != 0) {
                                        string3 = r1.getMediaDataController().ringtoneDataStore.getSoundPath(j13);
                                        z11 = true;
                                    } else {
                                        string3 = sharedPreferences2.getString("ReactionSoundPath", path);
                                        z11 = false;
                                    }
                                    i2 = sharedPreferences2.getInt("vibrate_react", 0);
                                    str5 = string3;
                                    int i34 = sharedPreferences2.getInt("priority_react", 1);
                                    z12 = z11;
                                    iIntValue = sharedPreferences2.getInt("ReactionsLed", -16776961);
                                    if (messageObject.isStoryReactionPush) {
                                        i3 = 5;
                                    } else {
                                        i3 = 4;
                                    }
                                    i4 = i3;
                                    i5 = i34;
                                } else {
                                    if (jLongValue == j2) {
                                        str2 = str2;
                                        z8 = z8;
                                        builder = builder;
                                        long j22 = j2;
                                        if (j6 != j22) {
                                            long j23 = sharedPreferences2.getLong(z23 != 0 ? "StoriesSoundDocId" : "GlobalSoundDocId", j22);
                                            if (j23 != j22) {
                                                string4 = r1.getMediaDataController().ringtoneDataStore.getSoundPath(j23);
                                                z16 = true;
                                            } else {
                                                string4 = sharedPreferences2.getString(z23 != 0 ? "StoriesSoundPath" : "GlobalSoundPath", path);
                                                z16 = false;
                                            }
                                            int i35 = sharedPreferences2.getInt("vibrate_messages", 0);
                                            String str15 = string4;
                                            int i36 = sharedPreferences2.getInt("priority_messages", 1);
                                            boolean z28 = z16;
                                            iIntValue = sharedPreferences2.getInt("MessagesLed", -16776961);
                                            i4 = z23 ? 3 : 1;
                                            i5 = i36;
                                            i6 = 4;
                                            i7 = i35;
                                            str6 = str15;
                                            z12 = z28;
                                        } else {
                                            i5 = 0;
                                            iIntValue = -16776961;
                                            str6 = null;
                                            i7 = 0;
                                            i6 = 4;
                                            i4 = 1;
                                            z12 = false;
                                        }
                                    } else if (z2) {
                                        long j24 = j2;
                                        long j25 = sharedPreferences2.getLong("ChannelSoundDocId", j24);
                                        if (j25 != j24) {
                                            string6 = r1.getMediaDataController().ringtoneDataStore.getSoundPath(j25);
                                            z18 = true;
                                        } else {
                                            string6 = sharedPreferences2.getString("ChannelSoundPath", path);
                                            z18 = false;
                                        }
                                        i2 = sharedPreferences2.getInt("vibrate_channel", 0);
                                        str5 = string6;
                                        int i37 = sharedPreferences2.getInt("priority_channel", 1);
                                        z12 = z18;
                                        iIntValue = sharedPreferences2.getInt("ChannelLed", -16776961);
                                        i5 = i37;
                                        i4 = 2;
                                    } else {
                                        long j26 = sharedPreferences2.getLong("GroupSoundDocId", 0L);
                                        if (j26 != 0) {
                                            string5 = r1.getMediaDataController().ringtoneDataStore.getSoundPath(j26);
                                            z17 = true;
                                        } else {
                                            string5 = sharedPreferences2.getString("GroupSoundPath", path);
                                            z17 = false;
                                        }
                                        i2 = sharedPreferences2.getInt("vibrate_group", 0);
                                        str5 = string5;
                                        int i38 = sharedPreferences2.getInt("priority_group", 1);
                                        z12 = z17;
                                        iIntValue = sharedPreferences2.getInt("GroupLed", -16776961);
                                        i5 = i38;
                                        i4 = 0;
                                    }
                                    if (i7 == i6) {
                                        z13 = true;
                                        i8 = 0;
                                    } else {
                                        i8 = i7;
                                        z13 = false;
                                    }
                                    if (!TextUtils.isEmpty(propertyString) || TextUtils.equals(str6, propertyString)) {
                                        propertyString = str6;
                                        z9 = z12;
                                        z14 = true;
                                    } else {
                                        z14 = false;
                                    }
                                    if (property2 != 3 && i5 != property2) {
                                        i5 = property2;
                                        z14 = false;
                                    }
                                    if (numValueOf != null && numValueOf.intValue() != iIntValue) {
                                        iIntValue = numValueOf.intValue();
                                        z14 = false;
                                    }
                                    if (property != 0 || property == 4 || property == i8) {
                                        property = i8;
                                    } else {
                                        z14 = false;
                                    }
                                    if (z8) {
                                        i9 = i5;
                                        i10 = property;
                                    } else {
                                        if (!sharedPreferences2.getBoolean("EnableInAppSounds", true)) {
                                            propertyString = null;
                                        }
                                        if (sharedPreferences2.getBoolean("EnableInAppVibrate", true)) {
                                            i10 = property;
                                        } else {
                                            i10 = 2;
                                        }
                                        if (sharedPreferences2.getBoolean("EnableInAppPopup", true)) {
                                            i9 = 2;
                                        } else {
                                            i9 = 0;
                                        }
                                    }
                                    if (z13 && i10 != 2) {
                                        try {
                                            ringerMode = audioManager.getRingerMode();
                                            if (ringerMode != 0 && ringerMode != 1) {
                                                i10 = 2;
                                            }
                                        } catch (Exception e2) {
                                            FileLog.e(e2);
                                        }
                                    }
                                    if (z10) {
                                        str7 = str4;
                                        i10 = 0;
                                        i9 = 0;
                                        i11 = 0;
                                        propertyString = null;
                                    } else {
                                        String str16 = str4;
                                        i11 = iIntValue;
                                        str7 = str16;
                                    }
                                    intent = new Intent(ApplicationLoader.applicationContext, (Class<?>) LaunchActivity.class);
                                    intent.setAction("com.tmessages.openchat" + Math.random() + Integer.MAX_VALUE);
                                    intent.setFlags(67108864);
                                    if (messageObject.isOauthPush) {
                                        intent.putExtra("oauth_url", messageObject.localName);
                                    }
                                    if (messageObject.isStoryReactionPush) {
                                        intent.putExtra("storyId", Math.abs(messageObject.getId()));
                                        i12 = i10;
                                        str8 = str7;
                                    } else {
                                        if (messageObject.isLiveStoryPush) {
                                            if (r15 != 0) {
                                                i15 = i10;
                                                str8 = str7;
                                                intent.putExtra("chatId", j3);
                                            } else {
                                                i15 = i10;
                                                str8 = str7;
                                                if (j6 != 0) {
                                                    intent.putExtra("userId", j6);
                                                }
                                            }
                                            intent.putExtra("storyId", Math.abs(messageObject.getId()));
                                            i12 = i15;
                                        } else {
                                            str8 = str7;
                                            j14 = j6;
                                            i12 = i10;
                                            i13 = i11;
                                            j15 = j3;
                                            if (messageObject.isStoryPush) {
                                                jArr = new long[r1.storyPushMessages.size()];
                                                for (i14 = 0; i14 < r1.storyPushMessages.size(); i14++) {
                                                    jArr[i14] = r1.storyPushMessages.get(i14).dialogId;
                                                }
                                                intent.putExtra("storyDialogIds", jArr);
                                                path = path;
                                            } else {
                                                if (!DialogObject.isEncryptedDialog(j12)) {
                                                    path = path;
                                                    if (r1.pushDialogs.size() == 1) {
                                                        if (r15 != 0) {
                                                            intent.putExtra("chatId", j15);
                                                        } else if (j14 != 0) {
                                                            intent.putExtra("userId", j14);
                                                        }
                                                    }
                                                    if (AndroidUtilities.needShowPasscode() && !SharedConfig.isWaitingForPasscodeEnter && r1.pushDialogs.size() == 1 && Build.VERSION.SDK_INT < 28) {
                                                        if (chat2 != null) {
                                                            chat3 = chat2;
                                                            TLRPC.ChatPhoto chatPhoto = chat3.photo;
                                                            if (chatPhoto == null || (fileLocation = chatPhoto.photo_small) == null || fileLocation.volume_id == 0 || fileLocation.local_id == 0) {
                                                                user2 = user;
                                                            } else {
                                                                user2 = user;
                                                            }
                                                        } else {
                                                            chat3 = chat2;
                                                            if (user != null) {
                                                                user2 = user;
                                                                TLRPC.UserProfilePhoto userProfilePhoto = user2.photo;
                                                                if (userProfilePhoto == null || (fileLocation = userProfilePhoto.photo_small) == null || fileLocation.volume_id == 0 || fileLocation.local_id == 0) {
                                                                }
                                                            } else {
                                                                user2 = user;
                                                            }
                                                        }
                                                    }
                                                } else {
                                                    path = path;
                                                    user2 = user;
                                                    chat3 = chat2;
                                                    if (r1.pushDialogs.size() == 1 && j12 != globalSecretChatId) {
                                                        intent.putExtra("encId", DialogObject.getEncryptedChatId(j12));
                                                    }
                                                }
                                                fileLocation = null;
                                            }
                                            user2 = user;
                                            chat3 = chat2;
                                            fileLocation = null;
                                        }
                                        intent.putExtra("currentAccount", r1.currentAccount);
                                        builder2 = builder;
                                        String str17 = propertyString;
                                        builder2.setContentTitle(string2).setSmallIcon(R.drawable.notification).setAutoCancel(true).setNumber(r1.total_unread_count).setContentIntent(PendingIntent.getActivity(ApplicationLoader.applicationContext, 0, intent, 1140850688)).setGroup(r1.notificationGroup).setGroupSummary(true).setShowWhen(true).setWhen(((long) messageObject.messageOwner.date) * j);
                                        if (Build.VERSION.SDK_INT < 31) {
                                            builder2.setColor(r1.getNotificationColor());
                                        }
                                        builder2.setCategory("msg");
                                        if (chat3 == null && user2 != null && (str9 = user2.phone) != null && str9.length() > 0) {
                                            builder2.addPerson("tel:+" + user2.phone);
                                        }
                                        intent2 = new Intent(ApplicationLoader.applicationContext, (Class<?>) NotificationDismissReceiver.class);
                                        intent2.putExtra("messageDate", messageObject.messageOwner.date);
                                        intent2.putExtra("currentAccount", r1.currentAccount);
                                        if (messageObject.isStoryPush) {
                                            intent2.putExtra("story", true);
                                        }
                                        if (messageObject.isStoryReactionPush) {
                                            i20 = 1;
                                            intent2.putExtra("storyReaction", true);
                                        } else {
                                            i20 = 1;
                                        }
                                        builder2.setDeleteIntent(PendingIntent.getBroadcast(ApplicationLoader.applicationContext, i20, intent2, 167772160));
                                        if (bitmap2 != null) {
                                            builder2.setLargeIcon(bitmap2);
                                        } else {
                                            if (fileLocation != null) {
                                                jArr2 = null;
                                                imageFromMemory = ImageLoader.getInstance().getImageFromMemory(fileLocation, null, "50_50");
                                                if (imageFromMemory != null) {
                                                    builder2.setLargeIcon(imageFromMemory.getBitmap());
                                                } else {
                                                    try {
                                                        pathToAttach = r1.getFileLoader().getPathToAttach(fileLocation, true);
                                                        if (pathToAttach.exists()) {
                                                            fDp = 160.0f / AndroidUtilities.dp(50.0f);
                                                            BitmapFactory.Options options = new BitmapFactory.Options();
                                                            if (fDp < 1.0f) {
                                                                i16 = 1;
                                                            } else {
                                                                i16 = (int) fDp;
                                                            }
                                                            options.inSampleSize = i16;
                                                            bitmapDecodeFile = BitmapFactory.decodeFile(pathToAttach.getAbsolutePath(), options);
                                                            if (bitmapDecodeFile != null) {
                                                                builder2.setLargeIcon(bitmapDecodeFile);
                                                            }
                                                        }
                                                    } catch (Throwable unused) {
                                                    }
                                                }
                                            }
                                            if (z || r14 == 1) {
                                                builder2.setPriority(-1);
                                                if (Build.VERSION.SDK_INT >= 26) {
                                                    i17 = 2;
                                                } else {
                                                    i17 = 0;
                                                }
                                            } else if (i9 == 0) {
                                                builder2.setPriority(0);
                                                if (Build.VERSION.SDK_INT >= 26) {
                                                    i17 = 3;
                                                } else {
                                                    i17 = 0;
                                                }
                                            } else {
                                                int i39 = 1;
                                                if (i9 == 1) {
                                                    builder2.setPriority(i39);
                                                    if (Build.VERSION.SDK_INT >= 26) {
                                                        i17 = 4;
                                                    } else {
                                                        i17 = 0;
                                                    }
                                                } else if (i9 == 2) {
                                                    i39 = 1;
                                                    builder2.setPriority(i39);
                                                    if (Build.VERSION.SDK_INT >= 26) {
                                                        i17 = 4;
                                                    } else {
                                                        i17 = 0;
                                                    }
                                                } else {
                                                    if (i9 == 4) {
                                                        builder2.setPriority(-2);
                                                        if (Build.VERSION.SDK_INT >= 26) {
                                                            i17 = 1;
                                                        }
                                                    } else if (i9 == 5) {
                                                        builder2.setPriority(-1);
                                                        if (Build.VERSION.SDK_INT >= 26) {
                                                            i17 = 2;
                                                        }
                                                    }
                                                    i17 = 0;
                                                }
                                            }
                                            if (r14 == 1 && !z10) {
                                                if (z8 || (sharedPreferences2.getBoolean("EnableInAppPreview", true) && str2 != null)) {
                                                    builder2.setTicker(str2.length() > 100 ? str2.substring(0, 100).replace('\n', ' ').trim() + "..." : str2);
                                                }
                                                if (str17 == null || str17.equalsIgnoreCase("NoSound")) {
                                                    obj = jArr2;
                                                } else {
                                                    int i40 = Build.VERSION.SDK_INT;
                                                    if (i40 >= 26) {
                                                        if (str17.equalsIgnoreCase("Default") || str17.equals(path)) {
                                                            obj = Settings.System.DEFAULT_NOTIFICATION_URI;
                                                        } else if (z9) {
                                                            uriForFile = FileProvider.getUriForFile(ApplicationLoader.applicationContext, ApplicationLoader.getApplicationId() + ".provider", new File(str17));
                                                            ApplicationLoader.applicationContext.grantUriPermission("com.android.systemui", uriForFile, 1);
                                                        } else {
                                                            uri = Uri.parse(str17);
                                                        }
                                                    } else {
                                                        if (str17.equals(path)) {
                                                            builder2.setSound(Settings.System.DEFAULT_NOTIFICATION_URI, 5);
                                                        } else if (i40 >= 24 && str17.startsWith("file://") && !AndroidUtilities.isInternalUri(Uri.parse(str17))) {
                                                            try {
                                                                Uri uriForFile2 = FileProvider.getUriForFile(ApplicationLoader.applicationContext, ApplicationLoader.getApplicationId() + ".provider", new File(str17.replace("file://", charSequence2)));
                                                                ApplicationLoader.applicationContext.grantUriPermission("com.android.systemui", uriForFile2, 1);
                                                                builder2.setSound(uriForFile2, 5);
                                                            } catch (Exception unused2) {
                                                                builder2.setSound(Uri.parse(str17), 5);
                                                            }
                                                        } else {
                                                            builder2.setSound(Uri.parse(str17), 5);
                                                        }
                                                        obj = jArr2;
                                                    }
                                                }
                                                if (i13 != 0) {
                                                    obj = uri;
                                                    obj = uriForFile;
                                                    i18 = i13;
                                                    builder2.setLights(i18, MediaDataController.MAX_STYLE_RUNS_COUNT, MediaDataController.MAX_STYLE_RUNS_COUNT);
                                                } else {
                                                    obj = uri;
                                                    obj = uriForFile;
                                                    i18 = i13;
                                                }
                                                int i41 = i12;
                                                if (i41 == 2) {
                                                    long[] jArr5 = {0, 0};
                                                    builder2.setVibrate(jArr5);
                                                    r11 = obj;
                                                    jArr3 = jArr5;
                                                } else {
                                                    if (i41 == 1) {
                                                        jArr3 = new long[]{0, 100, 0, 100};
                                                        builder2.setVibrate(jArr3);
                                                    } else if (i41 == 0 || i41 == 4) {
                                                        builder2.setDefaults(2);
                                                        jArr3 = new long[0];
                                                    } else {
                                                        if (i41 == 3) {
                                                            jArr2 = new long[]{0, 1000};
                                                            builder2.setVibrate(jArr2);
                                                        }
                                                        r11 = obj;
                                                        jArr3 = jArr2;
                                                    }
                                                    r11 = obj;
                                                }
                                            } else {
                                                i18 = i13;
                                                long[] jArr6 = {0, 0};
                                                builder2.setVibrate(jArr6);
                                                jArr3 = jArr6;
                                                r11 = jArr2;
                                            }
                                            if (!AndroidUtilities.needShowPasscode() || SharedConfig.isWaitingForPasscodeEnter || messageObject.getDialogId() != 777000 || (replyMarkup = messageObject.messageOwner.reply_markup) == null) {
                                                i19 = i17;
                                                jArr4 = jArr3;
                                                z15 = false;
                                            } else {
                                                ArrayList arrayList3 = replyMarkup.rows;
                                                int size2 = arrayList3.size();
                                                int i42 = 0;
                                                z15 = false;
                                                while (i42 < size2) {
                                                    TLRPC.TL_keyboardButtonRow tL_keyboardButtonRow = (TLRPC.TL_keyboardButtonRow) arrayList3.get(i42);
                                                    int size3 = tL_keyboardButtonRow.buttons.size();
                                                    boolean z29 = z15;
                                                    int i43 = 0;
                                                    while (i43 < size3) {
                                                        TLRPC.KeyboardButton keyboardButton = (TLRPC.KeyboardButton) tL_keyboardButtonRow.buttons.get(i43);
                                                        if (keyboardButton instanceof TLRPC.TL_keyboardButtonCallback) {
                                                            Intent intent3 = new Intent(ApplicationLoader.applicationContext, (Class<?>) NotificationCallbackReceiver.class);
                                                            intent3.putExtra("currentAccount", r1.currentAccount);
                                                            long j27 = j12;
                                                            intent3.putExtra("did", j27);
                                                            byte[] bArr = keyboardButton.data;
                                                            if (bArr != null) {
                                                                intent3.putExtra("data", bArr);
                                                            }
                                                            intent3.putExtra("mid", messageObject.getId());
                                                            String str18 = keyboardButton.text;
                                                            Context context = ApplicationLoader.applicationContext;
                                                            int i44 = r1.lastButtonId;
                                                            j12 = j27;
                                                            r1.lastButtonId = i44 + 1;
                                                            builder2.addAction(0, str18, PendingIntent.getBroadcast(context, i44, intent3, 201326592));
                                                            z29 = true;
                                                        }
                                                        i43++;
                                                        i17 = i17;
                                                        size2 = size2;
                                                        i42 = i42;
                                                        jArr3 = jArr3;
                                                    }
                                                    i42++;
                                                    z15 = z29;
                                                }
                                                i19 = i17;
                                                jArr4 = jArr3;
                                            }
                                            if (!z15 && Build.VERSION.SDK_INT < 24 && SharedConfig.passcodeHash.length() == 0 && r1.hasMessagesToReply()) {
                                                Intent intent4 = new Intent(ApplicationLoader.applicationContext, (Class<?>) PopupReplyReceiver.class);
                                                intent4.putExtra("currentAccount", r1.currentAccount);
                                                builder2.addAction(R.drawable.ic_ab_reply, LocaleController.getString(R.string.Reply), PendingIntent.getBroadcast(ApplicationLoader.applicationContext, 2, intent4, 201326592));
                                            }
                                            r1.showExtraNotifications(builder2, str8, j12, j11, str13, jArr4, i18, r11, i19, z14, z27, z10, i4);
                                            scheduleNotificationRepeat();
                                        }
                                        jArr2 = null;
                                        if (z) {
                                            builder2.setPriority(-1);
                                            if (Build.VERSION.SDK_INT >= 26) {
                                                i17 = 2;
                                            } else {
                                                i17 = 0;
                                            }
                                        } else {
                                            builder2.setPriority(-1);
                                            if (Build.VERSION.SDK_INT >= 26) {
                                                i17 = 2;
                                            } else {
                                                i17 = 0;
                                            }
                                        }
                                        if (r14 == 1) {
                                            i18 = i13;
                                            long[] jArr7 = {0, 0};
                                            builder2.setVibrate(jArr7);
                                            jArr3 = jArr7;
                                            r11 = jArr2;
                                        } else {
                                            i18 = i13;
                                            long[] jArr8 = {0, 0};
                                            builder2.setVibrate(jArr8);
                                            jArr3 = jArr8;
                                            r11 = jArr2;
                                        }
                                        if (AndroidUtilities.needShowPasscode()) {
                                            i19 = i17;
                                            jArr4 = jArr3;
                                            z15 = false;
                                        } else {
                                            i19 = i17;
                                            jArr4 = jArr3;
                                            z15 = false;
                                        }
                                        if (!z15) {
                                            Intent intent5 = new Intent(ApplicationLoader.applicationContext, (Class<?>) PopupReplyReceiver.class);
                                            intent5.putExtra("currentAccount", r1.currentAccount);
                                            builder2.addAction(R.drawable.ic_ab_reply, LocaleController.getString(R.string.Reply), PendingIntent.getBroadcast(ApplicationLoader.applicationContext, 2, intent5, 201326592));
                                        }
                                        r1.showExtraNotifications(builder2, str8, j12, j11, str13, jArr4, i18, r11, i19, z14, z27, z10, i4);
                                        scheduleNotificationRepeat();
                                    }
                                    i13 = i11;
                                    user2 = user;
                                    chat3 = chat2;
                                    fileLocation = null;
                                    intent.putExtra("currentAccount", r1.currentAccount);
                                    builder2 = builder;
                                    String str19 = propertyString;
                                    builder2.setContentTitle(string2).setSmallIcon(R.drawable.notification).setAutoCancel(true).setNumber(r1.total_unread_count).setContentIntent(PendingIntent.getActivity(ApplicationLoader.applicationContext, 0, intent, 1140850688)).setGroup(r1.notificationGroup).setGroupSummary(true).setShowWhen(true).setWhen(((long) messageObject.messageOwner.date) * j);
                                    if (Build.VERSION.SDK_INT < 31) {
                                        builder2.setColor(r1.getNotificationColor());
                                    }
                                    builder2.setCategory("msg");
                                    if (chat3 == null) {
                                        builder2.addPerson("tel:+" + user2.phone);
                                    }
                                    intent2 = new Intent(ApplicationLoader.applicationContext, (Class<?>) NotificationDismissReceiver.class);
                                    intent2.putExtra("messageDate", messageObject.messageOwner.date);
                                    intent2.putExtra("currentAccount", r1.currentAccount);
                                    if (messageObject.isStoryPush) {
                                        intent2.putExtra("story", true);
                                    }
                                    if (messageObject.isStoryReactionPush) {
                                        i20 = 1;
                                        intent2.putExtra("storyReaction", true);
                                    } else {
                                        i20 = 1;
                                    }
                                    builder2.setDeleteIntent(PendingIntent.getBroadcast(ApplicationLoader.applicationContext, i20, intent2, 167772160));
                                    if (bitmap2 != null) {
                                        builder2.setLargeIcon(bitmap2);
                                    } else {
                                        if (fileLocation != null) {
                                            jArr2 = null;
                                            imageFromMemory = ImageLoader.getInstance().getImageFromMemory(fileLocation, null, "50_50");
                                            if (imageFromMemory != null) {
                                                builder2.setLargeIcon(imageFromMemory.getBitmap());
                                            } else {
                                                pathToAttach = r1.getFileLoader().getPathToAttach(fileLocation, true);
                                                if (pathToAttach.exists()) {
                                                    fDp = 160.0f / AndroidUtilities.dp(50.0f);
                                                    BitmapFactory.Options options2 = new BitmapFactory.Options();
                                                    if (fDp < 1.0f) {
                                                        i16 = 1;
                                                    } else {
                                                        i16 = (int) fDp;
                                                    }
                                                    options2.inSampleSize = i16;
                                                    bitmapDecodeFile = BitmapFactory.decodeFile(pathToAttach.getAbsolutePath(), options2);
                                                    if (bitmapDecodeFile != null) {
                                                        builder2.setLargeIcon(bitmapDecodeFile);
                                                    }
                                                }
                                            }
                                        }
                                        if (z) {
                                            builder2.setPriority(-1);
                                            if (Build.VERSION.SDK_INT >= 26) {
                                                i17 = 2;
                                            } else {
                                                i17 = 0;
                                            }
                                        } else {
                                            builder2.setPriority(-1);
                                            if (Build.VERSION.SDK_INT >= 26) {
                                                i17 = 2;
                                            } else {
                                                i17 = 0;
                                            }
                                        }
                                        if (r14 == 1) {
                                            i18 = i13;
                                            long[] jArr9 = {0, 0};
                                            builder2.setVibrate(jArr9);
                                            jArr3 = jArr9;
                                            r11 = jArr2;
                                        } else {
                                            i18 = i13;
                                            long[] jArr10 = {0, 0};
                                            builder2.setVibrate(jArr10);
                                            jArr3 = jArr10;
                                            r11 = jArr2;
                                        }
                                        if (AndroidUtilities.needShowPasscode()) {
                                            i19 = i17;
                                            jArr4 = jArr3;
                                            z15 = false;
                                        } else {
                                            i19 = i17;
                                            jArr4 = jArr3;
                                            z15 = false;
                                        }
                                        if (!z15) {
                                            Intent intent6 = new Intent(ApplicationLoader.applicationContext, (Class<?>) PopupReplyReceiver.class);
                                            intent6.putExtra("currentAccount", r1.currentAccount);
                                            builder2.addAction(R.drawable.ic_ab_reply, LocaleController.getString(R.string.Reply), PendingIntent.getBroadcast(ApplicationLoader.applicationContext, 2, intent6, 201326592));
                                        }
                                        r1.showExtraNotifications(builder2, str8, j12, j11, str13, jArr4, i18, r11, i19, z14, z27, z10, i4);
                                        scheduleNotificationRepeat();
                                    }
                                    jArr2 = null;
                                    if (z) {
                                        builder2.setPriority(-1);
                                        if (Build.VERSION.SDK_INT >= 26) {
                                            i17 = 2;
                                        } else {
                                            i17 = 0;
                                        }
                                    } else {
                                        builder2.setPriority(-1);
                                        if (Build.VERSION.SDK_INT >= 26) {
                                            i17 = 2;
                                        } else {
                                            i17 = 0;
                                        }
                                    }
                                    if (r14 == 1) {
                                        i18 = i13;
                                        long[] jArr11 = {0, 0};
                                        builder2.setVibrate(jArr11);
                                        jArr3 = jArr11;
                                        r11 = jArr2;
                                    } else {
                                        i18 = i13;
                                        long[] jArr12 = {0, 0};
                                        builder2.setVibrate(jArr12);
                                        jArr3 = jArr12;
                                        r11 = jArr2;
                                    }
                                    if (AndroidUtilities.needShowPasscode()) {
                                        i19 = i17;
                                        jArr4 = jArr3;
                                        z15 = false;
                                    } else {
                                        i19 = i17;
                                        jArr4 = jArr3;
                                        z15 = false;
                                    }
                                    if (!z15) {
                                        Intent intent7 = new Intent(ApplicationLoader.applicationContext, (Class<?>) PopupReplyReceiver.class);
                                        intent7.putExtra("currentAccount", r1.currentAccount);
                                        builder2.addAction(R.drawable.ic_ab_reply, LocaleController.getString(R.string.Reply), PendingIntent.getBroadcast(ApplicationLoader.applicationContext, 2, intent7, 201326592));
                                    }
                                    r1.showExtraNotifications(builder2, str8, j12, j11, str13, jArr4, i18, r11, i19, z14, z27, z10, i4);
                                    scheduleNotificationRepeat();
                                }
                                i7 = i2;
                                str6 = str5;
                                i6 = 4;
                                if (i7 == i6) {
                                    z13 = true;
                                    i8 = 0;
                                } else {
                                    i8 = i7;
                                    z13 = false;
                                }
                                if (TextUtils.isEmpty(propertyString)) {
                                    propertyString = str6;
                                    z9 = z12;
                                    z14 = true;
                                } else {
                                    propertyString = str6;
                                    z9 = z12;
                                    z14 = true;
                                }
                                if (property2 != 3) {
                                    i5 = property2;
                                    z14 = false;
                                }
                                if (numValueOf != null) {
                                    iIntValue = numValueOf.intValue();
                                    z14 = false;
                                }
                                if (property != 0) {
                                    property = i8;
                                } else {
                                    property = i8;
                                }
                                if (z8) {
                                    if (!sharedPreferences2.getBoolean("EnableInAppSounds", true)) {
                                        propertyString = null;
                                    }
                                    if (sharedPreferences2.getBoolean("EnableInAppVibrate", true)) {
                                        i10 = 2;
                                    } else {
                                        i10 = property;
                                    }
                                    if (sharedPreferences2.getBoolean("EnableInAppPopup", true)) {
                                        i9 = 2;
                                    } else {
                                        i9 = 0;
                                    }
                                } else {
                                    i9 = i5;
                                    i10 = property;
                                }
                                if (z13) {
                                    ringerMode = audioManager.getRingerMode();
                                    if (ringerMode != 0) {
                                        i10 = 2;
                                    }
                                }
                                if (z10) {
                                    str7 = str4;
                                    i10 = 0;
                                    i9 = 0;
                                    i11 = 0;
                                    propertyString = null;
                                } else {
                                    String str110 = str4;
                                    i11 = iIntValue;
                                    str7 = str110;
                                }
                                intent = new Intent(ApplicationLoader.applicationContext, (Class<?>) LaunchActivity.class);
                                intent.setAction("com.tmessages.openchat" + Math.random() + Integer.MAX_VALUE);
                                intent.setFlags(67108864);
                                if (messageObject.isOauthPush) {
                                    intent.putExtra("oauth_url", messageObject.localName);
                                }
                                if (messageObject.isStoryReactionPush) {
                                    intent.putExtra("storyId", Math.abs(messageObject.getId()));
                                    i12 = i10;
                                    str8 = str7;
                                } else {
                                    if (messageObject.isLiveStoryPush) {
                                        if (r15 != 0) {
                                            i15 = i10;
                                            str8 = str7;
                                            intent.putExtra("chatId", j3);
                                        } else {
                                            i15 = i10;
                                            str8 = str7;
                                            if (j6 != 0) {
                                                intent.putExtra("userId", j6);
                                            }
                                        }
                                        intent.putExtra("storyId", Math.abs(messageObject.getId()));
                                        i12 = i15;
                                    } else {
                                        str8 = str7;
                                        j14 = j6;
                                        i12 = i10;
                                        i13 = i11;
                                        j15 = j3;
                                        if (messageObject.isStoryPush) {
                                            jArr = new long[r1.storyPushMessages.size()];
                                            while (i14 < r1.storyPushMessages.size()) {
                                                jArr[i14] = r1.storyPushMessages.get(i14).dialogId;
                                            }
                                            intent.putExtra("storyDialogIds", jArr);
                                            path = path;
                                        } else {
                                            if (!DialogObject.isEncryptedDialog(j12)) {
                                                path = path;
                                                if (r1.pushDialogs.size() == 1) {
                                                    if (r15 != 0) {
                                                        intent.putExtra("chatId", j15);
                                                    } else if (j14 != 0) {
                                                        intent.putExtra("userId", j14);
                                                    }
                                                }
                                                if (AndroidUtilities.needShowPasscode()) {
                                                }
                                            } else {
                                                path = path;
                                                user2 = user;
                                                chat3 = chat2;
                                                if (r1.pushDialogs.size() == 1) {
                                                    intent.putExtra("encId", DialogObject.getEncryptedChatId(j12));
                                                }
                                            }
                                            fileLocation = null;
                                        }
                                        user2 = user;
                                        chat3 = chat2;
                                        fileLocation = null;
                                    }
                                    intent.putExtra("currentAccount", r1.currentAccount);
                                    builder2 = builder;
                                    String str111 = propertyString;
                                    builder2.setContentTitle(string2).setSmallIcon(R.drawable.notification).setAutoCancel(true).setNumber(r1.total_unread_count).setContentIntent(PendingIntent.getActivity(ApplicationLoader.applicationContext, 0, intent, 1140850688)).setGroup(r1.notificationGroup).setGroupSummary(true).setShowWhen(true).setWhen(((long) messageObject.messageOwner.date) * j);
                                    if (Build.VERSION.SDK_INT < 31) {
                                        builder2.setColor(r1.getNotificationColor());
                                    }
                                    builder2.setCategory("msg");
                                    if (chat3 == null) {
                                        builder2.addPerson("tel:+" + user2.phone);
                                    }
                                    intent2 = new Intent(ApplicationLoader.applicationContext, (Class<?>) NotificationDismissReceiver.class);
                                    intent2.putExtra("messageDate", messageObject.messageOwner.date);
                                    intent2.putExtra("currentAccount", r1.currentAccount);
                                    if (messageObject.isStoryPush) {
                                        intent2.putExtra("story", true);
                                    }
                                    if (messageObject.isStoryReactionPush) {
                                        i20 = 1;
                                        intent2.putExtra("storyReaction", true);
                                    } else {
                                        i20 = 1;
                                    }
                                    builder2.setDeleteIntent(PendingIntent.getBroadcast(ApplicationLoader.applicationContext, i20, intent2, 167772160));
                                    if (bitmap2 != null) {
                                        builder2.setLargeIcon(bitmap2);
                                    } else {
                                        if (fileLocation != null) {
                                            jArr2 = null;
                                            imageFromMemory = ImageLoader.getInstance().getImageFromMemory(fileLocation, null, "50_50");
                                            if (imageFromMemory != null) {
                                                builder2.setLargeIcon(imageFromMemory.getBitmap());
                                            } else {
                                                pathToAttach = r1.getFileLoader().getPathToAttach(fileLocation, true);
                                                if (pathToAttach.exists()) {
                                                    fDp = 160.0f / AndroidUtilities.dp(50.0f);
                                                    BitmapFactory.Options options3 = new BitmapFactory.Options();
                                                    if (fDp < 1.0f) {
                                                        i16 = 1;
                                                    } else {
                                                        i16 = (int) fDp;
                                                    }
                                                    options3.inSampleSize = i16;
                                                    bitmapDecodeFile = BitmapFactory.decodeFile(pathToAttach.getAbsolutePath(), options3);
                                                    if (bitmapDecodeFile != null) {
                                                        builder2.setLargeIcon(bitmapDecodeFile);
                                                    }
                                                }
                                            }
                                        }
                                        if (z) {
                                            builder2.setPriority(-1);
                                            if (Build.VERSION.SDK_INT >= 26) {
                                                i17 = 2;
                                            } else {
                                                i17 = 0;
                                            }
                                        } else {
                                            builder2.setPriority(-1);
                                            if (Build.VERSION.SDK_INT >= 26) {
                                                i17 = 2;
                                            } else {
                                                i17 = 0;
                                            }
                                        }
                                        if (r14 == 1) {
                                            i18 = i13;
                                            long[] jArr13 = {0, 0};
                                            builder2.setVibrate(jArr13);
                                            jArr3 = jArr13;
                                            r11 = jArr2;
                                        } else {
                                            i18 = i13;
                                            long[] jArr14 = {0, 0};
                                            builder2.setVibrate(jArr14);
                                            jArr3 = jArr14;
                                            r11 = jArr2;
                                        }
                                        if (AndroidUtilities.needShowPasscode()) {
                                            i19 = i17;
                                            jArr4 = jArr3;
                                            z15 = false;
                                        } else {
                                            i19 = i17;
                                            jArr4 = jArr3;
                                            z15 = false;
                                        }
                                        if (!z15) {
                                            Intent intent8 = new Intent(ApplicationLoader.applicationContext, (Class<?>) PopupReplyReceiver.class);
                                            intent8.putExtra("currentAccount", r1.currentAccount);
                                            builder2.addAction(R.drawable.ic_ab_reply, LocaleController.getString(R.string.Reply), PendingIntent.getBroadcast(ApplicationLoader.applicationContext, 2, intent8, 201326592));
                                        }
                                        r1.showExtraNotifications(builder2, str8, j12, j11, str13, jArr4, i18, r11, i19, z14, z27, z10, i4);
                                        scheduleNotificationRepeat();
                                    }
                                    jArr2 = null;
                                    if (z) {
                                        builder2.setPriority(-1);
                                        if (Build.VERSION.SDK_INT >= 26) {
                                            i17 = 2;
                                        } else {
                                            i17 = 0;
                                        }
                                    } else {
                                        builder2.setPriority(-1);
                                        if (Build.VERSION.SDK_INT >= 26) {
                                            i17 = 2;
                                        } else {
                                            i17 = 0;
                                        }
                                    }
                                    if (r14 == 1) {
                                        i18 = i13;
                                        long[] jArr15 = {0, 0};
                                        builder2.setVibrate(jArr15);
                                        jArr3 = jArr15;
                                        r11 = jArr2;
                                    } else {
                                        i18 = i13;
                                        long[] jArr16 = {0, 0};
                                        builder2.setVibrate(jArr16);
                                        jArr3 = jArr16;
                                        r11 = jArr2;
                                    }
                                    if (AndroidUtilities.needShowPasscode()) {
                                        i19 = i17;
                                        jArr4 = jArr3;
                                        z15 = false;
                                    } else {
                                        i19 = i17;
                                        jArr4 = jArr3;
                                        z15 = false;
                                    }
                                    if (!z15) {
                                        Intent intent9 = new Intent(ApplicationLoader.applicationContext, (Class<?>) PopupReplyReceiver.class);
                                        intent9.putExtra("currentAccount", r1.currentAccount);
                                        builder2.addAction(R.drawable.ic_ab_reply, LocaleController.getString(R.string.Reply), PendingIntent.getBroadcast(ApplicationLoader.applicationContext, 2, intent9, 201326592));
                                    }
                                    r1.showExtraNotifications(builder2, str8, j12, j11, str13, jArr4, i18, r11, i19, z14, z27, z10, i4);
                                    scheduleNotificationRepeat();
                                }
                                i13 = i11;
                                user2 = user;
                                chat3 = chat2;
                                fileLocation = null;
                                intent.putExtra("currentAccount", r1.currentAccount);
                                builder2 = builder;
                                String str112 = propertyString;
                                builder2.setContentTitle(string2).setSmallIcon(R.drawable.notification).setAutoCancel(true).setNumber(r1.total_unread_count).setContentIntent(PendingIntent.getActivity(ApplicationLoader.applicationContext, 0, intent, 1140850688)).setGroup(r1.notificationGroup).setGroupSummary(true).setShowWhen(true).setWhen(((long) messageObject.messageOwner.date) * j);
                                if (Build.VERSION.SDK_INT < 31) {
                                    builder2.setColor(r1.getNotificationColor());
                                }
                                builder2.setCategory("msg");
                                if (chat3 == null) {
                                    builder2.addPerson("tel:+" + user2.phone);
                                }
                                intent2 = new Intent(ApplicationLoader.applicationContext, (Class<?>) NotificationDismissReceiver.class);
                                intent2.putExtra("messageDate", messageObject.messageOwner.date);
                                intent2.putExtra("currentAccount", r1.currentAccount);
                                if (messageObject.isStoryPush) {
                                    intent2.putExtra("story", true);
                                }
                                if (messageObject.isStoryReactionPush) {
                                    i20 = 1;
                                    intent2.putExtra("storyReaction", true);
                                } else {
                                    i20 = 1;
                                }
                                builder2.setDeleteIntent(PendingIntent.getBroadcast(ApplicationLoader.applicationContext, i20, intent2, 167772160));
                                if (bitmap2 != null) {
                                    builder2.setLargeIcon(bitmap2);
                                } else {
                                    if (fileLocation != null) {
                                        jArr2 = null;
                                        imageFromMemory = ImageLoader.getInstance().getImageFromMemory(fileLocation, null, "50_50");
                                        if (imageFromMemory != null) {
                                            builder2.setLargeIcon(imageFromMemory.getBitmap());
                                        } else {
                                            pathToAttach = r1.getFileLoader().getPathToAttach(fileLocation, true);
                                            if (pathToAttach.exists()) {
                                                fDp = 160.0f / AndroidUtilities.dp(50.0f);
                                                BitmapFactory.Options options4 = new BitmapFactory.Options();
                                                if (fDp < 1.0f) {
                                                    i16 = 1;
                                                } else {
                                                    i16 = (int) fDp;
                                                }
                                                options4.inSampleSize = i16;
                                                bitmapDecodeFile = BitmapFactory.decodeFile(pathToAttach.getAbsolutePath(), options4);
                                                if (bitmapDecodeFile != null) {
                                                    builder2.setLargeIcon(bitmapDecodeFile);
                                                }
                                            }
                                        }
                                    }
                                    if (z) {
                                        builder2.setPriority(-1);
                                        if (Build.VERSION.SDK_INT >= 26) {
                                            i17 = 2;
                                        } else {
                                            i17 = 0;
                                        }
                                    } else {
                                        builder2.setPriority(-1);
                                        if (Build.VERSION.SDK_INT >= 26) {
                                            i17 = 2;
                                        } else {
                                            i17 = 0;
                                        }
                                    }
                                    if (r14 == 1) {
                                        i18 = i13;
                                        long[] jArr17 = {0, 0};
                                        builder2.setVibrate(jArr17);
                                        jArr3 = jArr17;
                                        r11 = jArr2;
                                    } else {
                                        i18 = i13;
                                        long[] jArr18 = {0, 0};
                                        builder2.setVibrate(jArr18);
                                        jArr3 = jArr18;
                                        r11 = jArr2;
                                    }
                                    if (AndroidUtilities.needShowPasscode()) {
                                        i19 = i17;
                                        jArr4 = jArr3;
                                        z15 = false;
                                    } else {
                                        i19 = i17;
                                        jArr4 = jArr3;
                                        z15 = false;
                                    }
                                    if (!z15) {
                                        Intent intent10 = new Intent(ApplicationLoader.applicationContext, (Class<?>) PopupReplyReceiver.class);
                                        intent10.putExtra("currentAccount", r1.currentAccount);
                                        builder2.addAction(R.drawable.ic_ab_reply, LocaleController.getString(R.string.Reply), PendingIntent.getBroadcast(ApplicationLoader.applicationContext, 2, intent10, 201326592));
                                    }
                                    r1.showExtraNotifications(builder2, str8, j12, j11, str13, jArr4, i18, r11, i19, z14, z27, z10, i4);
                                    scheduleNotificationRepeat();
                                }
                                jArr2 = null;
                                if (z) {
                                    builder2.setPriority(-1);
                                    if (Build.VERSION.SDK_INT >= 26) {
                                        i17 = 2;
                                    } else {
                                        i17 = 0;
                                    }
                                } else {
                                    builder2.setPriority(-1);
                                    if (Build.VERSION.SDK_INT >= 26) {
                                        i17 = 2;
                                    } else {
                                        i17 = 0;
                                    }
                                }
                                if (r14 == 1) {
                                    i18 = i13;
                                    long[] jArr19 = {0, 0};
                                    builder2.setVibrate(jArr19);
                                    jArr3 = jArr19;
                                    r11 = jArr2;
                                } else {
                                    i18 = i13;
                                    long[] jArr110 = {0, 0};
                                    builder2.setVibrate(jArr110);
                                    jArr3 = jArr110;
                                    r11 = jArr2;
                                }
                                if (AndroidUtilities.needShowPasscode()) {
                                    i19 = i17;
                                    jArr4 = jArr3;
                                    z15 = false;
                                } else {
                                    i19 = i17;
                                    jArr4 = jArr3;
                                    z15 = false;
                                }
                                if (!z15) {
                                    Intent intent11 = new Intent(ApplicationLoader.applicationContext, (Class<?>) PopupReplyReceiver.class);
                                    intent11.putExtra("currentAccount", r1.currentAccount);
                                    builder2.addAction(R.drawable.ic_ab_reply, LocaleController.getString(R.string.Reply), PendingIntent.getBroadcast(ApplicationLoader.applicationContext, 2, intent11, 201326592));
                                }
                                r1.showExtraNotifications(builder2, str8, j12, j11, str13, jArr4, i18, r11, i19, z14, z27, z10, i4);
                                scheduleNotificationRepeat();
                            }
                            j12 = j10;
                            property = 0;
                            property2 = 3;
                            z9 = false;
                            propertyString = null;
                            z10 = z7;
                            if (!messageObject.isReactionPush) {
                                j13 = sharedPreferences2.getLong("ReactionSoundDocId", 0L);
                                if (j13 != 0) {
                                    string3 = r1.getMediaDataController().ringtoneDataStore.getSoundPath(j13);
                                    z11 = true;
                                } else {
                                    string3 = sharedPreferences2.getString("ReactionSoundPath", path);
                                    z11 = false;
                                }
                                i2 = sharedPreferences2.getInt("vibrate_react", 0);
                                str5 = string3;
                                int i310 = sharedPreferences2.getInt("priority_react", 1);
                                z12 = z11;
                                iIntValue = sharedPreferences2.getInt("ReactionsLed", -16776961);
                                if (messageObject.isStoryReactionPush) {
                                    i3 = 5;
                                } else {
                                    i3 = 4;
                                }
                                i4 = i3;
                                i5 = i310;
                                i7 = i2;
                                str6 = str5;
                                i6 = 4;
                            } else {
                                j13 = sharedPreferences2.getLong("ReactionSoundDocId", 0L);
                                if (j13 != 0) {
                                    string3 = r1.getMediaDataController().ringtoneDataStore.getSoundPath(j13);
                                    z11 = true;
                                } else {
                                    string3 = sharedPreferences2.getString("ReactionSoundPath", path);
                                    z11 = false;
                                }
                                i2 = sharedPreferences2.getInt("vibrate_react", 0);
                                str5 = string3;
                                int i311 = sharedPreferences2.getInt("priority_react", 1);
                                z12 = z11;
                                iIntValue = sharedPreferences2.getInt("ReactionsLed", -16776961);
                                if (messageObject.isStoryReactionPush) {
                                    i3 = 5;
                                } else {
                                    i3 = 4;
                                }
                                i4 = i3;
                                i5 = i311;
                                i7 = i2;
                                str6 = str5;
                                i6 = 4;
                            }
                            if (i7 == i6) {
                                z13 = true;
                                i8 = 0;
                            } else {
                                i8 = i7;
                                z13 = false;
                            }
                            if (TextUtils.isEmpty(propertyString)) {
                                propertyString = str6;
                                z9 = z12;
                                z14 = true;
                            } else {
                                propertyString = str6;
                                z9 = z12;
                                z14 = true;
                            }
                            if (property2 != 3) {
                                i5 = property2;
                                z14 = false;
                            }
                            if (numValueOf != null) {
                                iIntValue = numValueOf.intValue();
                                z14 = false;
                            }
                            if (property != 0) {
                                property = i8;
                            } else {
                                property = i8;
                            }
                            if (z8) {
                                if (!sharedPreferences2.getBoolean("EnableInAppSounds", true)) {
                                    propertyString = null;
                                }
                                if (sharedPreferences2.getBoolean("EnableInAppVibrate", true)) {
                                    i10 = 2;
                                } else {
                                    i10 = property;
                                }
                                if (sharedPreferences2.getBoolean("EnableInAppPopup", true)) {
                                    i9 = 2;
                                } else {
                                    i9 = 0;
                                }
                            } else {
                                i9 = i5;
                                i10 = property;
                            }
                            if (z13) {
                                ringerMode = audioManager.getRingerMode();
                                if (ringerMode != 0) {
                                    i10 = 2;
                                }
                            }
                            if (z10) {
                                str7 = str4;
                                i10 = 0;
                                i9 = 0;
                                i11 = 0;
                                propertyString = null;
                            } else {
                                String str113 = str4;
                                i11 = iIntValue;
                                str7 = str113;
                            }
                            intent = new Intent(ApplicationLoader.applicationContext, (Class<?>) LaunchActivity.class);
                            intent.setAction("com.tmessages.openchat" + Math.random() + Integer.MAX_VALUE);
                            intent.setFlags(67108864);
                            if (messageObject.isOauthPush) {
                                intent.putExtra("oauth_url", messageObject.localName);
                            }
                            if (messageObject.isStoryReactionPush) {
                                intent.putExtra("storyId", Math.abs(messageObject.getId()));
                                i12 = i10;
                                str8 = str7;
                            } else {
                                if (messageObject.isLiveStoryPush) {
                                    if (r15 != 0) {
                                        i15 = i10;
                                        str8 = str7;
                                        intent.putExtra("chatId", j3);
                                    } else {
                                        i15 = i10;
                                        str8 = str7;
                                        if (j6 != 0) {
                                            intent.putExtra("userId", j6);
                                        }
                                    }
                                    intent.putExtra("storyId", Math.abs(messageObject.getId()));
                                    i12 = i15;
                                } else {
                                    str8 = str7;
                                    j14 = j6;
                                    i12 = i10;
                                    i13 = i11;
                                    j15 = j3;
                                    if (messageObject.isStoryPush) {
                                        jArr = new long[r1.storyPushMessages.size()];
                                        while (i14 < r1.storyPushMessages.size()) {
                                            jArr[i14] = r1.storyPushMessages.get(i14).dialogId;
                                        }
                                        intent.putExtra("storyDialogIds", jArr);
                                        path = path;
                                    } else {
                                        if (!DialogObject.isEncryptedDialog(j12)) {
                                            path = path;
                                            if (r1.pushDialogs.size() == 1) {
                                                if (r15 != 0) {
                                                    intent.putExtra("chatId", j15);
                                                } else if (j14 != 0) {
                                                    intent.putExtra("userId", j14);
                                                }
                                            }
                                            if (AndroidUtilities.needShowPasscode()) {
                                            }
                                        } else {
                                            path = path;
                                            user2 = user;
                                            chat3 = chat2;
                                            if (r1.pushDialogs.size() == 1) {
                                                intent.putExtra("encId", DialogObject.getEncryptedChatId(j12));
                                            }
                                        }
                                        fileLocation = null;
                                    }
                                    user2 = user;
                                    chat3 = chat2;
                                    fileLocation = null;
                                }
                                intent.putExtra("currentAccount", r1.currentAccount);
                                builder2 = builder;
                                String str114 = propertyString;
                                builder2.setContentTitle(string2).setSmallIcon(R.drawable.notification).setAutoCancel(true).setNumber(r1.total_unread_count).setContentIntent(PendingIntent.getActivity(ApplicationLoader.applicationContext, 0, intent, 1140850688)).setGroup(r1.notificationGroup).setGroupSummary(true).setShowWhen(true).setWhen(((long) messageObject.messageOwner.date) * j);
                                if (Build.VERSION.SDK_INT < 31) {
                                    builder2.setColor(r1.getNotificationColor());
                                }
                                builder2.setCategory("msg");
                                if (chat3 == null) {
                                    builder2.addPerson("tel:+" + user2.phone);
                                }
                                intent2 = new Intent(ApplicationLoader.applicationContext, (Class<?>) NotificationDismissReceiver.class);
                                intent2.putExtra("messageDate", messageObject.messageOwner.date);
                                intent2.putExtra("currentAccount", r1.currentAccount);
                                if (messageObject.isStoryPush) {
                                    intent2.putExtra("story", true);
                                }
                                if (messageObject.isStoryReactionPush) {
                                    i20 = 1;
                                    intent2.putExtra("storyReaction", true);
                                } else {
                                    i20 = 1;
                                }
                                builder2.setDeleteIntent(PendingIntent.getBroadcast(ApplicationLoader.applicationContext, i20, intent2, 167772160));
                                if (bitmap2 != null) {
                                    builder2.setLargeIcon(bitmap2);
                                } else {
                                    if (fileLocation != null) {
                                        jArr2 = null;
                                        imageFromMemory = ImageLoader.getInstance().getImageFromMemory(fileLocation, null, "50_50");
                                        if (imageFromMemory != null) {
                                            builder2.setLargeIcon(imageFromMemory.getBitmap());
                                        } else {
                                            pathToAttach = r1.getFileLoader().getPathToAttach(fileLocation, true);
                                            if (pathToAttach.exists()) {
                                                fDp = 160.0f / AndroidUtilities.dp(50.0f);
                                                BitmapFactory.Options options5 = new BitmapFactory.Options();
                                                if (fDp < 1.0f) {
                                                    i16 = 1;
                                                } else {
                                                    i16 = (int) fDp;
                                                }
                                                options5.inSampleSize = i16;
                                                bitmapDecodeFile = BitmapFactory.decodeFile(pathToAttach.getAbsolutePath(), options5);
                                                if (bitmapDecodeFile != null) {
                                                    builder2.setLargeIcon(bitmapDecodeFile);
                                                }
                                            }
                                        }
                                    }
                                    if (z) {
                                        builder2.setPriority(-1);
                                        if (Build.VERSION.SDK_INT >= 26) {
                                            i17 = 2;
                                        } else {
                                            i17 = 0;
                                        }
                                    } else {
                                        builder2.setPriority(-1);
                                        if (Build.VERSION.SDK_INT >= 26) {
                                            i17 = 2;
                                        } else {
                                            i17 = 0;
                                        }
                                    }
                                    if (r14 == 1) {
                                        i18 = i13;
                                        long[] jArr111 = {0, 0};
                                        builder2.setVibrate(jArr111);
                                        jArr3 = jArr111;
                                        r11 = jArr2;
                                    } else {
                                        i18 = i13;
                                        long[] jArr112 = {0, 0};
                                        builder2.setVibrate(jArr112);
                                        jArr3 = jArr112;
                                        r11 = jArr2;
                                    }
                                    if (AndroidUtilities.needShowPasscode()) {
                                        i19 = i17;
                                        jArr4 = jArr3;
                                        z15 = false;
                                    } else {
                                        i19 = i17;
                                        jArr4 = jArr3;
                                        z15 = false;
                                    }
                                    if (!z15) {
                                        Intent intent12 = new Intent(ApplicationLoader.applicationContext, (Class<?>) PopupReplyReceiver.class);
                                        intent12.putExtra("currentAccount", r1.currentAccount);
                                        builder2.addAction(R.drawable.ic_ab_reply, LocaleController.getString(R.string.Reply), PendingIntent.getBroadcast(ApplicationLoader.applicationContext, 2, intent12, 201326592));
                                    }
                                    r1.showExtraNotifications(builder2, str8, j12, j11, str13, jArr4, i18, r11, i19, z14, z27, z10, i4);
                                    scheduleNotificationRepeat();
                                }
                                jArr2 = null;
                                if (z) {
                                    builder2.setPriority(-1);
                                    if (Build.VERSION.SDK_INT >= 26) {
                                        i17 = 2;
                                    } else {
                                        i17 = 0;
                                    }
                                } else {
                                    builder2.setPriority(-1);
                                    if (Build.VERSION.SDK_INT >= 26) {
                                        i17 = 2;
                                    } else {
                                        i17 = 0;
                                    }
                                }
                                if (r14 == 1) {
                                    i18 = i13;
                                    long[] jArr113 = {0, 0};
                                    builder2.setVibrate(jArr113);
                                    jArr3 = jArr113;
                                    r11 = jArr2;
                                } else {
                                    i18 = i13;
                                    long[] jArr114 = {0, 0};
                                    builder2.setVibrate(jArr114);
                                    jArr3 = jArr114;
                                    r11 = jArr2;
                                }
                                if (AndroidUtilities.needShowPasscode()) {
                                    i19 = i17;
                                    jArr4 = jArr3;
                                    z15 = false;
                                } else {
                                    i19 = i17;
                                    jArr4 = jArr3;
                                    z15 = false;
                                }
                                if (!z15) {
                                    Intent intent13 = new Intent(ApplicationLoader.applicationContext, (Class<?>) PopupReplyReceiver.class);
                                    intent13.putExtra("currentAccount", r1.currentAccount);
                                    builder2.addAction(R.drawable.ic_ab_reply, LocaleController.getString(R.string.Reply), PendingIntent.getBroadcast(ApplicationLoader.applicationContext, 2, intent13, 201326592));
                                }
                                r1.showExtraNotifications(builder2, str8, j12, j11, str13, jArr4, i18, r11, i19, z14, z27, z10, i4);
                                scheduleNotificationRepeat();
                            }
                            i13 = i11;
                            user2 = user;
                            chat3 = chat2;
                            fileLocation = null;
                            intent.putExtra("currentAccount", r1.currentAccount);
                            builder2 = builder;
                            String str115 = propertyString;
                            builder2.setContentTitle(string2).setSmallIcon(R.drawable.notification).setAutoCancel(true).setNumber(r1.total_unread_count).setContentIntent(PendingIntent.getActivity(ApplicationLoader.applicationContext, 0, intent, 1140850688)).setGroup(r1.notificationGroup).setGroupSummary(true).setShowWhen(true).setWhen(((long) messageObject.messageOwner.date) * j);
                            if (Build.VERSION.SDK_INT < 31) {
                                builder2.setColor(r1.getNotificationColor());
                            }
                            builder2.setCategory("msg");
                            if (chat3 == null) {
                                builder2.addPerson("tel:+" + user2.phone);
                            }
                            intent2 = new Intent(ApplicationLoader.applicationContext, (Class<?>) NotificationDismissReceiver.class);
                            intent2.putExtra("messageDate", messageObject.messageOwner.date);
                            intent2.putExtra("currentAccount", r1.currentAccount);
                            if (messageObject.isStoryPush) {
                                intent2.putExtra("story", true);
                            }
                            if (messageObject.isStoryReactionPush) {
                                i20 = 1;
                                intent2.putExtra("storyReaction", true);
                            } else {
                                i20 = 1;
                            }
                            builder2.setDeleteIntent(PendingIntent.getBroadcast(ApplicationLoader.applicationContext, i20, intent2, 167772160));
                            if (bitmap2 != null) {
                                builder2.setLargeIcon(bitmap2);
                            } else {
                                if (fileLocation != null) {
                                    jArr2 = null;
                                    imageFromMemory = ImageLoader.getInstance().getImageFromMemory(fileLocation, null, "50_50");
                                    if (imageFromMemory != null) {
                                        builder2.setLargeIcon(imageFromMemory.getBitmap());
                                    } else {
                                        pathToAttach = r1.getFileLoader().getPathToAttach(fileLocation, true);
                                        if (pathToAttach.exists()) {
                                            fDp = 160.0f / AndroidUtilities.dp(50.0f);
                                            BitmapFactory.Options options6 = new BitmapFactory.Options();
                                            if (fDp < 1.0f) {
                                                i16 = 1;
                                            } else {
                                                i16 = (int) fDp;
                                            }
                                            options6.inSampleSize = i16;
                                            bitmapDecodeFile = BitmapFactory.decodeFile(pathToAttach.getAbsolutePath(), options6);
                                            if (bitmapDecodeFile != null) {
                                                builder2.setLargeIcon(bitmapDecodeFile);
                                            }
                                        }
                                    }
                                }
                                if (z) {
                                    builder2.setPriority(-1);
                                    if (Build.VERSION.SDK_INT >= 26) {
                                        i17 = 2;
                                    } else {
                                        i17 = 0;
                                    }
                                } else {
                                    builder2.setPriority(-1);
                                    if (Build.VERSION.SDK_INT >= 26) {
                                        i17 = 2;
                                    } else {
                                        i17 = 0;
                                    }
                                }
                                if (r14 == 1) {
                                    i18 = i13;
                                    long[] jArr115 = {0, 0};
                                    builder2.setVibrate(jArr115);
                                    jArr3 = jArr115;
                                    r11 = jArr2;
                                } else {
                                    i18 = i13;
                                    long[] jArr116 = {0, 0};
                                    builder2.setVibrate(jArr116);
                                    jArr3 = jArr116;
                                    r11 = jArr2;
                                }
                                if (AndroidUtilities.needShowPasscode()) {
                                    i19 = i17;
                                    jArr4 = jArr3;
                                    z15 = false;
                                } else {
                                    i19 = i17;
                                    jArr4 = jArr3;
                                    z15 = false;
                                }
                                if (!z15) {
                                    Intent intent14 = new Intent(ApplicationLoader.applicationContext, (Class<?>) PopupReplyReceiver.class);
                                    intent14.putExtra("currentAccount", r1.currentAccount);
                                    builder2.addAction(R.drawable.ic_ab_reply, LocaleController.getString(R.string.Reply), PendingIntent.getBroadcast(ApplicationLoader.applicationContext, 2, intent14, 201326592));
                                }
                                r1.showExtraNotifications(builder2, str8, j12, j11, str13, jArr4, i18, r11, i19, z14, z27, z10, i4);
                                scheduleNotificationRepeat();
                            }
                            jArr2 = null;
                            if (z) {
                                builder2.setPriority(-1);
                                if (Build.VERSION.SDK_INT >= 26) {
                                    i17 = 2;
                                } else {
                                    i17 = 0;
                                }
                            } else {
                                builder2.setPriority(-1);
                                if (Build.VERSION.SDK_INT >= 26) {
                                    i17 = 2;
                                } else {
                                    i17 = 0;
                                }
                            }
                            if (r14 == 1) {
                                i18 = i13;
                                long[] jArr117 = {0, 0};
                                builder2.setVibrate(jArr117);
                                jArr3 = jArr117;
                                r11 = jArr2;
                            } else {
                                i18 = i13;
                                long[] jArr118 = {0, 0};
                                builder2.setVibrate(jArr118);
                                jArr3 = jArr118;
                                r11 = jArr2;
                            }
                            if (AndroidUtilities.needShowPasscode()) {
                                i19 = i17;
                                jArr4 = jArr3;
                                z15 = false;
                            } else {
                                i19 = i17;
                                jArr4 = jArr3;
                                z15 = false;
                            }
                            if (!z15) {
                                Intent intent15 = new Intent(ApplicationLoader.applicationContext, (Class<?>) PopupReplyReceiver.class);
                                intent15.putExtra("currentAccount", r1.currentAccount);
                                builder2.addAction(R.drawable.ic_ab_reply, LocaleController.getString(R.string.Reply), PendingIntent.getBroadcast(ApplicationLoader.applicationContext, 2, intent15, 201326592));
                            }
                            r1.showExtraNotifications(builder2, str8, j12, j11, str13, jArr4, i18, r11, i19, z14, z27, z10, i4);
                            scheduleNotificationRepeat();
                        }
                        str4 = str3;
                        charSequence2 = charSequence;
                        sharedPreferences2 = sharedPreferences;
                        j8 = j7;
                        z7 = z6;
                        if (z7) {
                            if (!sharedPreferences2.getBoolean("sound_enabled_" + getSharedPrefKey(j8, j9), true)) {
                                j9 = j5;
                                z7 = true;
                            }
                        } else {
                            j9 = j5;
                        }
                        j9 = j5;
                        path = Settings.System.DEFAULT_NOTIFICATION_URI.getPath();
                        z8 = ApplicationLoader.mainInterfacePaused;
                        boolean z210 = !z8;
                        getSharedPrefKey(j8, j9);
                        j10 = j8;
                        j11 = j9;
                        if (r1.dialogsNotificationsFacade.getProperty(NotificationsSettingsFacade.PROPERTY_CUSTOM, j10, j11, false)) {
                            property = r1.dialogsNotificationsFacade.getProperty("vibrate_", j10, j11, 0);
                            property2 = r1.dialogsNotificationsFacade.getProperty("priority_", j10, j11, 3);
                            property3 = r1.dialogsNotificationsFacade.getProperty("sound_document_id_", j10, j11, 0L);
                            if (property3 != j2) {
                                propertyString = r1.getMediaDataController().ringtoneDataStore.getSoundPath(property3);
                                z9 = true;
                            } else {
                                propertyString = r1.dialogsNotificationsFacade.getPropertyString("sound_path_", j10, j11, null);
                                z9 = false;
                            }
                            int property5 = r1.dialogsNotificationsFacade.getProperty("color_", j10, j11, 0);
                            j12 = j10;
                            if (property5 != 0) {
                            }
                            z10 = z7;
                            if (!messageObject.isReactionPush) {
                                j13 = sharedPreferences2.getLong("ReactionSoundDocId", 0L);
                                if (j13 != 0) {
                                    string3 = r1.getMediaDataController().ringtoneDataStore.getSoundPath(j13);
                                    z11 = true;
                                } else {
                                    string3 = sharedPreferences2.getString("ReactionSoundPath", path);
                                    z11 = false;
                                }
                                i2 = sharedPreferences2.getInt("vibrate_react", 0);
                                str5 = string3;
                                int i312 = sharedPreferences2.getInt("priority_react", 1);
                                z12 = z11;
                                iIntValue = sharedPreferences2.getInt("ReactionsLed", -16776961);
                                if (messageObject.isStoryReactionPush) {
                                    i3 = 5;
                                } else {
                                    i3 = 4;
                                }
                                i4 = i3;
                                i5 = i312;
                                i7 = i2;
                                str6 = str5;
                                i6 = 4;
                            } else {
                                j13 = sharedPreferences2.getLong("ReactionSoundDocId", 0L);
                                if (j13 != 0) {
                                    string3 = r1.getMediaDataController().ringtoneDataStore.getSoundPath(j13);
                                    z11 = true;
                                } else {
                                    string3 = sharedPreferences2.getString("ReactionSoundPath", path);
                                    z11 = false;
                                }
                                i2 = sharedPreferences2.getInt("vibrate_react", 0);
                                str5 = string3;
                                int i313 = sharedPreferences2.getInt("priority_react", 1);
                                z12 = z11;
                                iIntValue = sharedPreferences2.getInt("ReactionsLed", -16776961);
                                if (messageObject.isStoryReactionPush) {
                                    i3 = 5;
                                } else {
                                    i3 = 4;
                                }
                                i4 = i3;
                                i5 = i313;
                                i7 = i2;
                                str6 = str5;
                                i6 = 4;
                            }
                            if (i7 == i6) {
                                z13 = true;
                                i8 = 0;
                            } else {
                                i8 = i7;
                                z13 = false;
                            }
                            if (TextUtils.isEmpty(propertyString)) {
                                propertyString = str6;
                                z9 = z12;
                                z14 = true;
                            } else {
                                propertyString = str6;
                                z9 = z12;
                                z14 = true;
                            }
                            if (property2 != 3) {
                                i5 = property2;
                                z14 = false;
                            }
                            if (numValueOf != null) {
                                iIntValue = numValueOf.intValue();
                                z14 = false;
                            }
                            if (property != 0) {
                                property = i8;
                            } else {
                                property = i8;
                            }
                            if (z8) {
                                if (!sharedPreferences2.getBoolean("EnableInAppSounds", true)) {
                                    propertyString = null;
                                }
                                if (sharedPreferences2.getBoolean("EnableInAppVibrate", true)) {
                                    i10 = 2;
                                } else {
                                    i10 = property;
                                }
                                if (sharedPreferences2.getBoolean("EnableInAppPopup", true)) {
                                    i9 = 2;
                                } else {
                                    i9 = 0;
                                }
                            } else {
                                i9 = i5;
                                i10 = property;
                            }
                            if (z13) {
                                ringerMode = audioManager.getRingerMode();
                                if (ringerMode != 0) {
                                    i10 = 2;
                                }
                            }
                            if (z10) {
                                str7 = str4;
                                i10 = 0;
                                i9 = 0;
                                i11 = 0;
                                propertyString = null;
                            } else {
                                String str116 = str4;
                                i11 = iIntValue;
                                str7 = str116;
                            }
                            intent = new Intent(ApplicationLoader.applicationContext, (Class<?>) LaunchActivity.class);
                            intent.setAction("com.tmessages.openchat" + Math.random() + Integer.MAX_VALUE);
                            intent.setFlags(67108864);
                            if (messageObject.isOauthPush) {
                                intent.putExtra("oauth_url", messageObject.localName);
                            }
                            if (messageObject.isStoryReactionPush) {
                                intent.putExtra("storyId", Math.abs(messageObject.getId()));
                                i12 = i10;
                                str8 = str7;
                            } else {
                                if (messageObject.isLiveStoryPush) {
                                    if (r15 != 0) {
                                        i15 = i10;
                                        str8 = str7;
                                        intent.putExtra("chatId", j3);
                                    } else {
                                        i15 = i10;
                                        str8 = str7;
                                        if (j6 != 0) {
                                            intent.putExtra("userId", j6);
                                        }
                                    }
                                    intent.putExtra("storyId", Math.abs(messageObject.getId()));
                                    i12 = i15;
                                } else {
                                    str8 = str7;
                                    j14 = j6;
                                    i12 = i10;
                                    i13 = i11;
                                    j15 = j3;
                                    if (messageObject.isStoryPush) {
                                        jArr = new long[r1.storyPushMessages.size()];
                                        while (i14 < r1.storyPushMessages.size()) {
                                            jArr[i14] = r1.storyPushMessages.get(i14).dialogId;
                                        }
                                        intent.putExtra("storyDialogIds", jArr);
                                        path = path;
                                    } else {
                                        if (!DialogObject.isEncryptedDialog(j12)) {
                                            path = path;
                                            if (r1.pushDialogs.size() == 1) {
                                                if (r15 != 0) {
                                                    intent.putExtra("chatId", j15);
                                                } else if (j14 != 0) {
                                                    intent.putExtra("userId", j14);
                                                }
                                            }
                                            if (AndroidUtilities.needShowPasscode()) {
                                            }
                                        } else {
                                            path = path;
                                            user2 = user;
                                            chat3 = chat2;
                                            if (r1.pushDialogs.size() == 1) {
                                                intent.putExtra("encId", DialogObject.getEncryptedChatId(j12));
                                            }
                                        }
                                        fileLocation = null;
                                    }
                                    user2 = user;
                                    chat3 = chat2;
                                    fileLocation = null;
                                }
                                intent.putExtra("currentAccount", r1.currentAccount);
                                builder2 = builder;
                                String str117 = propertyString;
                                builder2.setContentTitle(string2).setSmallIcon(R.drawable.notification).setAutoCancel(true).setNumber(r1.total_unread_count).setContentIntent(PendingIntent.getActivity(ApplicationLoader.applicationContext, 0, intent, 1140850688)).setGroup(r1.notificationGroup).setGroupSummary(true).setShowWhen(true).setWhen(((long) messageObject.messageOwner.date) * j);
                                if (Build.VERSION.SDK_INT < 31) {
                                    builder2.setColor(r1.getNotificationColor());
                                }
                                builder2.setCategory("msg");
                                if (chat3 == null) {
                                    builder2.addPerson("tel:+" + user2.phone);
                                }
                                intent2 = new Intent(ApplicationLoader.applicationContext, (Class<?>) NotificationDismissReceiver.class);
                                intent2.putExtra("messageDate", messageObject.messageOwner.date);
                                intent2.putExtra("currentAccount", r1.currentAccount);
                                if (messageObject.isStoryPush) {
                                    intent2.putExtra("story", true);
                                }
                                if (messageObject.isStoryReactionPush) {
                                    i20 = 1;
                                    intent2.putExtra("storyReaction", true);
                                } else {
                                    i20 = 1;
                                }
                                builder2.setDeleteIntent(PendingIntent.getBroadcast(ApplicationLoader.applicationContext, i20, intent2, 167772160));
                                if (bitmap2 != null) {
                                    builder2.setLargeIcon(bitmap2);
                                } else {
                                    if (fileLocation != null) {
                                        jArr2 = null;
                                        imageFromMemory = ImageLoader.getInstance().getImageFromMemory(fileLocation, null, "50_50");
                                        if (imageFromMemory != null) {
                                            builder2.setLargeIcon(imageFromMemory.getBitmap());
                                        } else {
                                            pathToAttach = r1.getFileLoader().getPathToAttach(fileLocation, true);
                                            if (pathToAttach.exists()) {
                                                fDp = 160.0f / AndroidUtilities.dp(50.0f);
                                                BitmapFactory.Options options7 = new BitmapFactory.Options();
                                                if (fDp < 1.0f) {
                                                    i16 = 1;
                                                } else {
                                                    i16 = (int) fDp;
                                                }
                                                options7.inSampleSize = i16;
                                                bitmapDecodeFile = BitmapFactory.decodeFile(pathToAttach.getAbsolutePath(), options7);
                                                if (bitmapDecodeFile != null) {
                                                    builder2.setLargeIcon(bitmapDecodeFile);
                                                }
                                            }
                                        }
                                    }
                                    if (z) {
                                        builder2.setPriority(-1);
                                        if (Build.VERSION.SDK_INT >= 26) {
                                            i17 = 2;
                                        } else {
                                            i17 = 0;
                                        }
                                    } else {
                                        builder2.setPriority(-1);
                                        if (Build.VERSION.SDK_INT >= 26) {
                                            i17 = 2;
                                        } else {
                                            i17 = 0;
                                        }
                                    }
                                    if (r14 == 1) {
                                        i18 = i13;
                                        long[] jArr119 = {0, 0};
                                        builder2.setVibrate(jArr119);
                                        jArr3 = jArr119;
                                        r11 = jArr2;
                                    } else {
                                        i18 = i13;
                                        long[] jArr1110 = {0, 0};
                                        builder2.setVibrate(jArr1110);
                                        jArr3 = jArr1110;
                                        r11 = jArr2;
                                    }
                                    if (AndroidUtilities.needShowPasscode()) {
                                        i19 = i17;
                                        jArr4 = jArr3;
                                        z15 = false;
                                    } else {
                                        i19 = i17;
                                        jArr4 = jArr3;
                                        z15 = false;
                                    }
                                    if (!z15) {
                                        Intent intent16 = new Intent(ApplicationLoader.applicationContext, (Class<?>) PopupReplyReceiver.class);
                                        intent16.putExtra("currentAccount", r1.currentAccount);
                                        builder2.addAction(R.drawable.ic_ab_reply, LocaleController.getString(R.string.Reply), PendingIntent.getBroadcast(ApplicationLoader.applicationContext, 2, intent16, 201326592));
                                    }
                                    r1.showExtraNotifications(builder2, str8, j12, j11, str13, jArr4, i18, r11, i19, z14, z210, z10, i4);
                                    scheduleNotificationRepeat();
                                }
                                jArr2 = null;
                                if (z) {
                                    builder2.setPriority(-1);
                                    if (Build.VERSION.SDK_INT >= 26) {
                                        i17 = 2;
                                    } else {
                                        i17 = 0;
                                    }
                                } else {
                                    builder2.setPriority(-1);
                                    if (Build.VERSION.SDK_INT >= 26) {
                                        i17 = 2;
                                    } else {
                                        i17 = 0;
                                    }
                                }
                                if (r14 == 1) {
                                    i18 = i13;
                                    long[] jArr1111 = {0, 0};
                                    builder2.setVibrate(jArr1111);
                                    jArr3 = jArr1111;
                                    r11 = jArr2;
                                } else {
                                    i18 = i13;
                                    long[] jArr1112 = {0, 0};
                                    builder2.setVibrate(jArr1112);
                                    jArr3 = jArr1112;
                                    r11 = jArr2;
                                }
                                if (AndroidUtilities.needShowPasscode()) {
                                    i19 = i17;
                                    jArr4 = jArr3;
                                    z15 = false;
                                } else {
                                    i19 = i17;
                                    jArr4 = jArr3;
                                    z15 = false;
                                }
                                if (!z15) {
                                    Intent intent17 = new Intent(ApplicationLoader.applicationContext, (Class<?>) PopupReplyReceiver.class);
                                    intent17.putExtra("currentAccount", r1.currentAccount);
                                    builder2.addAction(R.drawable.ic_ab_reply, LocaleController.getString(R.string.Reply), PendingIntent.getBroadcast(ApplicationLoader.applicationContext, 2, intent17, 201326592));
                                }
                                r1.showExtraNotifications(builder2, str8, j12, j11, str13, jArr4, i18, r11, i19, z14, z210, z10, i4);
                                scheduleNotificationRepeat();
                            }
                            i13 = i11;
                            user2 = user;
                            chat3 = chat2;
                            fileLocation = null;
                            intent.putExtra("currentAccount", r1.currentAccount);
                            builder2 = builder;
                            String str118 = propertyString;
                            builder2.setContentTitle(string2).setSmallIcon(R.drawable.notification).setAutoCancel(true).setNumber(r1.total_unread_count).setContentIntent(PendingIntent.getActivity(ApplicationLoader.applicationContext, 0, intent, 1140850688)).setGroup(r1.notificationGroup).setGroupSummary(true).setShowWhen(true).setWhen(((long) messageObject.messageOwner.date) * j);
                            if (Build.VERSION.SDK_INT < 31) {
                                builder2.setColor(r1.getNotificationColor());
                            }
                            builder2.setCategory("msg");
                            if (chat3 == null) {
                                builder2.addPerson("tel:+" + user2.phone);
                            }
                            intent2 = new Intent(ApplicationLoader.applicationContext, (Class<?>) NotificationDismissReceiver.class);
                            intent2.putExtra("messageDate", messageObject.messageOwner.date);
                            intent2.putExtra("currentAccount", r1.currentAccount);
                            if (messageObject.isStoryPush) {
                                intent2.putExtra("story", true);
                            }
                            if (messageObject.isStoryReactionPush) {
                                i20 = 1;
                                intent2.putExtra("storyReaction", true);
                            } else {
                                i20 = 1;
                            }
                            builder2.setDeleteIntent(PendingIntent.getBroadcast(ApplicationLoader.applicationContext, i20, intent2, 167772160));
                            if (bitmap2 != null) {
                                builder2.setLargeIcon(bitmap2);
                            } else {
                                if (fileLocation != null) {
                                    jArr2 = null;
                                    imageFromMemory = ImageLoader.getInstance().getImageFromMemory(fileLocation, null, "50_50");
                                    if (imageFromMemory != null) {
                                        builder2.setLargeIcon(imageFromMemory.getBitmap());
                                    } else {
                                        pathToAttach = r1.getFileLoader().getPathToAttach(fileLocation, true);
                                        if (pathToAttach.exists()) {
                                            fDp = 160.0f / AndroidUtilities.dp(50.0f);
                                            BitmapFactory.Options options8 = new BitmapFactory.Options();
                                            if (fDp < 1.0f) {
                                                i16 = 1;
                                            } else {
                                                i16 = (int) fDp;
                                            }
                                            options8.inSampleSize = i16;
                                            bitmapDecodeFile = BitmapFactory.decodeFile(pathToAttach.getAbsolutePath(), options8);
                                            if (bitmapDecodeFile != null) {
                                                builder2.setLargeIcon(bitmapDecodeFile);
                                            }
                                        }
                                    }
                                }
                                if (z) {
                                    builder2.setPriority(-1);
                                    if (Build.VERSION.SDK_INT >= 26) {
                                        i17 = 2;
                                    } else {
                                        i17 = 0;
                                    }
                                } else {
                                    builder2.setPriority(-1);
                                    if (Build.VERSION.SDK_INT >= 26) {
                                        i17 = 2;
                                    } else {
                                        i17 = 0;
                                    }
                                }
                                if (r14 == 1) {
                                    i18 = i13;
                                    long[] jArr1113 = {0, 0};
                                    builder2.setVibrate(jArr1113);
                                    jArr3 = jArr1113;
                                    r11 = jArr2;
                                } else {
                                    i18 = i13;
                                    long[] jArr1114 = {0, 0};
                                    builder2.setVibrate(jArr1114);
                                    jArr3 = jArr1114;
                                    r11 = jArr2;
                                }
                                if (AndroidUtilities.needShowPasscode()) {
                                    i19 = i17;
                                    jArr4 = jArr3;
                                    z15 = false;
                                } else {
                                    i19 = i17;
                                    jArr4 = jArr3;
                                    z15 = false;
                                }
                                if (!z15) {
                                    Intent intent18 = new Intent(ApplicationLoader.applicationContext, (Class<?>) PopupReplyReceiver.class);
                                    intent18.putExtra("currentAccount", r1.currentAccount);
                                    builder2.addAction(R.drawable.ic_ab_reply, LocaleController.getString(R.string.Reply), PendingIntent.getBroadcast(ApplicationLoader.applicationContext, 2, intent18, 201326592));
                                }
                                r1.showExtraNotifications(builder2, str8, j12, j11, str13, jArr4, i18, r11, i19, z14, z210, z10, i4);
                                scheduleNotificationRepeat();
                            }
                            jArr2 = null;
                            if (z) {
                                builder2.setPriority(-1);
                                if (Build.VERSION.SDK_INT >= 26) {
                                    i17 = 2;
                                } else {
                                    i17 = 0;
                                }
                            } else {
                                builder2.setPriority(-1);
                                if (Build.VERSION.SDK_INT >= 26) {
                                    i17 = 2;
                                } else {
                                    i17 = 0;
                                }
                            }
                            if (r14 == 1) {
                                i18 = i13;
                                long[] jArr1115 = {0, 0};
                                builder2.setVibrate(jArr1115);
                                jArr3 = jArr1115;
                                r11 = jArr2;
                            } else {
                                i18 = i13;
                                long[] jArr1116 = {0, 0};
                                builder2.setVibrate(jArr1116);
                                jArr3 = jArr1116;
                                r11 = jArr2;
                            }
                            if (AndroidUtilities.needShowPasscode()) {
                                i19 = i17;
                                jArr4 = jArr3;
                                z15 = false;
                            } else {
                                i19 = i17;
                                jArr4 = jArr3;
                                z15 = false;
                            }
                            if (!z15) {
                                Intent intent19 = new Intent(ApplicationLoader.applicationContext, (Class<?>) PopupReplyReceiver.class);
                                intent19.putExtra("currentAccount", r1.currentAccount);
                                builder2.addAction(R.drawable.ic_ab_reply, LocaleController.getString(R.string.Reply), PendingIntent.getBroadcast(ApplicationLoader.applicationContext, 2, intent19, 201326592));
                            }
                            r1.showExtraNotifications(builder2, str8, j12, j11, str13, jArr4, i18, r11, i19, z14, z210, z10, i4);
                            scheduleNotificationRepeat();
                        }
                        j12 = j10;
                        property = 0;
                        property2 = 3;
                        z9 = false;
                        propertyString = null;
                        z10 = z7;
                        if (!messageObject.isReactionPush) {
                            j13 = sharedPreferences2.getLong("ReactionSoundDocId", 0L);
                            if (j13 != 0) {
                                string3 = r1.getMediaDataController().ringtoneDataStore.getSoundPath(j13);
                                z11 = true;
                            } else {
                                string3 = sharedPreferences2.getString("ReactionSoundPath", path);
                                z11 = false;
                            }
                            i2 = sharedPreferences2.getInt("vibrate_react", 0);
                            str5 = string3;
                            int i314 = sharedPreferences2.getInt("priority_react", 1);
                            z12 = z11;
                            iIntValue = sharedPreferences2.getInt("ReactionsLed", -16776961);
                            if (messageObject.isStoryReactionPush) {
                                i3 = 5;
                            } else {
                                i3 = 4;
                            }
                            i4 = i3;
                            i5 = i314;
                            i7 = i2;
                            str6 = str5;
                            i6 = 4;
                        } else {
                            j13 = sharedPreferences2.getLong("ReactionSoundDocId", 0L);
                            if (j13 != 0) {
                                string3 = r1.getMediaDataController().ringtoneDataStore.getSoundPath(j13);
                                z11 = true;
                            } else {
                                string3 = sharedPreferences2.getString("ReactionSoundPath", path);
                                z11 = false;
                            }
                            i2 = sharedPreferences2.getInt("vibrate_react", 0);
                            str5 = string3;
                            int i315 = sharedPreferences2.getInt("priority_react", 1);
                            z12 = z11;
                            iIntValue = sharedPreferences2.getInt("ReactionsLed", -16776961);
                            if (messageObject.isStoryReactionPush) {
                                i3 = 5;
                            } else {
                                i3 = 4;
                            }
                            i4 = i3;
                            i5 = i315;
                            i7 = i2;
                            str6 = str5;
                            i6 = 4;
                        }
                        if (i7 == i6) {
                            z13 = true;
                            i8 = 0;
                        } else {
                            i8 = i7;
                            z13 = false;
                        }
                        if (TextUtils.isEmpty(propertyString)) {
                            propertyString = str6;
                            z9 = z12;
                            z14 = true;
                        } else {
                            propertyString = str6;
                            z9 = z12;
                            z14 = true;
                        }
                        if (property2 != 3) {
                            i5 = property2;
                            z14 = false;
                        }
                        if (numValueOf != null) {
                            iIntValue = numValueOf.intValue();
                            z14 = false;
                        }
                        if (property != 0) {
                            property = i8;
                        } else {
                            property = i8;
                        }
                        if (z8) {
                            if (!sharedPreferences2.getBoolean("EnableInAppSounds", true)) {
                                propertyString = null;
                            }
                            if (sharedPreferences2.getBoolean("EnableInAppVibrate", true)) {
                                i10 = 2;
                            } else {
                                i10 = property;
                            }
                            if (sharedPreferences2.getBoolean("EnableInAppPopup", true)) {
                                i9 = 2;
                            } else {
                                i9 = 0;
                            }
                        } else {
                            i9 = i5;
                            i10 = property;
                        }
                        if (z13) {
                            ringerMode = audioManager.getRingerMode();
                            if (ringerMode != 0) {
                                i10 = 2;
                            }
                        }
                        if (z10) {
                            str7 = str4;
                            i10 = 0;
                            i9 = 0;
                            i11 = 0;
                            propertyString = null;
                        } else {
                            String str119 = str4;
                            i11 = iIntValue;
                            str7 = str119;
                        }
                        intent = new Intent(ApplicationLoader.applicationContext, (Class<?>) LaunchActivity.class);
                        intent.setAction("com.tmessages.openchat" + Math.random() + Integer.MAX_VALUE);
                        intent.setFlags(67108864);
                        if (messageObject.isOauthPush) {
                            intent.putExtra("oauth_url", messageObject.localName);
                        }
                        if (messageObject.isStoryReactionPush) {
                            intent.putExtra("storyId", Math.abs(messageObject.getId()));
                            i12 = i10;
                            str8 = str7;
                        } else {
                            if (messageObject.isLiveStoryPush) {
                                if (r15 != 0) {
                                    i15 = i10;
                                    str8 = str7;
                                    intent.putExtra("chatId", j3);
                                } else {
                                    i15 = i10;
                                    str8 = str7;
                                    if (j6 != 0) {
                                        intent.putExtra("userId", j6);
                                    }
                                }
                                intent.putExtra("storyId", Math.abs(messageObject.getId()));
                                i12 = i15;
                            } else {
                                str8 = str7;
                                j14 = j6;
                                i12 = i10;
                                i13 = i11;
                                j15 = j3;
                                if (messageObject.isStoryPush) {
                                    jArr = new long[r1.storyPushMessages.size()];
                                    while (i14 < r1.storyPushMessages.size()) {
                                        jArr[i14] = r1.storyPushMessages.get(i14).dialogId;
                                    }
                                    intent.putExtra("storyDialogIds", jArr);
                                    path = path;
                                } else {
                                    if (!DialogObject.isEncryptedDialog(j12)) {
                                        path = path;
                                        if (r1.pushDialogs.size() == 1) {
                                            if (r15 != 0) {
                                                intent.putExtra("chatId", j15);
                                            } else if (j14 != 0) {
                                                intent.putExtra("userId", j14);
                                            }
                                        }
                                        if (AndroidUtilities.needShowPasscode()) {
                                        }
                                    } else {
                                        path = path;
                                        user2 = user;
                                        chat3 = chat2;
                                        if (r1.pushDialogs.size() == 1) {
                                            intent.putExtra("encId", DialogObject.getEncryptedChatId(j12));
                                        }
                                    }
                                    fileLocation = null;
                                }
                                user2 = user;
                                chat3 = chat2;
                                fileLocation = null;
                            }
                            intent.putExtra("currentAccount", r1.currentAccount);
                            builder2 = builder;
                            String str1110 = propertyString;
                            builder2.setContentTitle(string2).setSmallIcon(R.drawable.notification).setAutoCancel(true).setNumber(r1.total_unread_count).setContentIntent(PendingIntent.getActivity(ApplicationLoader.applicationContext, 0, intent, 1140850688)).setGroup(r1.notificationGroup).setGroupSummary(true).setShowWhen(true).setWhen(((long) messageObject.messageOwner.date) * j);
                            if (Build.VERSION.SDK_INT < 31) {
                                builder2.setColor(r1.getNotificationColor());
                            }
                            builder2.setCategory("msg");
                            if (chat3 == null) {
                                builder2.addPerson("tel:+" + user2.phone);
                            }
                            intent2 = new Intent(ApplicationLoader.applicationContext, (Class<?>) NotificationDismissReceiver.class);
                            intent2.putExtra("messageDate", messageObject.messageOwner.date);
                            intent2.putExtra("currentAccount", r1.currentAccount);
                            if (messageObject.isStoryPush) {
                                intent2.putExtra("story", true);
                            }
                            if (messageObject.isStoryReactionPush) {
                                i20 = 1;
                                intent2.putExtra("storyReaction", true);
                            } else {
                                i20 = 1;
                            }
                            builder2.setDeleteIntent(PendingIntent.getBroadcast(ApplicationLoader.applicationContext, i20, intent2, 167772160));
                            if (bitmap2 != null) {
                                builder2.setLargeIcon(bitmap2);
                            } else {
                                if (fileLocation != null) {
                                    jArr2 = null;
                                    imageFromMemory = ImageLoader.getInstance().getImageFromMemory(fileLocation, null, "50_50");
                                    if (imageFromMemory != null) {
                                        builder2.setLargeIcon(imageFromMemory.getBitmap());
                                    } else {
                                        pathToAttach = r1.getFileLoader().getPathToAttach(fileLocation, true);
                                        if (pathToAttach.exists()) {
                                            fDp = 160.0f / AndroidUtilities.dp(50.0f);
                                            BitmapFactory.Options options9 = new BitmapFactory.Options();
                                            if (fDp < 1.0f) {
                                                i16 = 1;
                                            } else {
                                                i16 = (int) fDp;
                                            }
                                            options9.inSampleSize = i16;
                                            bitmapDecodeFile = BitmapFactory.decodeFile(pathToAttach.getAbsolutePath(), options9);
                                            if (bitmapDecodeFile != null) {
                                                builder2.setLargeIcon(bitmapDecodeFile);
                                            }
                                        }
                                    }
                                }
                                if (z) {
                                    builder2.setPriority(-1);
                                    if (Build.VERSION.SDK_INT >= 26) {
                                        i17 = 2;
                                    } else {
                                        i17 = 0;
                                    }
                                } else {
                                    builder2.setPriority(-1);
                                    if (Build.VERSION.SDK_INT >= 26) {
                                        i17 = 2;
                                    } else {
                                        i17 = 0;
                                    }
                                }
                                if (r14 == 1) {
                                    i18 = i13;
                                    long[] jArr1117 = {0, 0};
                                    builder2.setVibrate(jArr1117);
                                    jArr3 = jArr1117;
                                    r11 = jArr2;
                                } else {
                                    i18 = i13;
                                    long[] jArr1118 = {0, 0};
                                    builder2.setVibrate(jArr1118);
                                    jArr3 = jArr1118;
                                    r11 = jArr2;
                                }
                                if (AndroidUtilities.needShowPasscode()) {
                                    i19 = i17;
                                    jArr4 = jArr3;
                                    z15 = false;
                                } else {
                                    i19 = i17;
                                    jArr4 = jArr3;
                                    z15 = false;
                                }
                                if (!z15) {
                                    Intent intent110 = new Intent(ApplicationLoader.applicationContext, (Class<?>) PopupReplyReceiver.class);
                                    intent110.putExtra("currentAccount", r1.currentAccount);
                                    builder2.addAction(R.drawable.ic_ab_reply, LocaleController.getString(R.string.Reply), PendingIntent.getBroadcast(ApplicationLoader.applicationContext, 2, intent110, 201326592));
                                }
                                r1.showExtraNotifications(builder2, str8, j12, j11, str13, jArr4, i18, r11, i19, z14, z210, z10, i4);
                                scheduleNotificationRepeat();
                            }
                            jArr2 = null;
                            if (z) {
                                builder2.setPriority(-1);
                                if (Build.VERSION.SDK_INT >= 26) {
                                    i17 = 2;
                                } else {
                                    i17 = 0;
                                }
                            } else {
                                builder2.setPriority(-1);
                                if (Build.VERSION.SDK_INT >= 26) {
                                    i17 = 2;
                                } else {
                                    i17 = 0;
                                }
                            }
                            if (r14 == 1) {
                                i18 = i13;
                                long[] jArr1119 = {0, 0};
                                builder2.setVibrate(jArr1119);
                                jArr3 = jArr1119;
                                r11 = jArr2;
                            } else {
                                i18 = i13;
                                long[] jArr11110 = {0, 0};
                                builder2.setVibrate(jArr11110);
                                jArr3 = jArr11110;
                                r11 = jArr2;
                            }
                            if (AndroidUtilities.needShowPasscode()) {
                                i19 = i17;
                                jArr4 = jArr3;
                                z15 = false;
                            } else {
                                i19 = i17;
                                jArr4 = jArr3;
                                z15 = false;
                            }
                            if (!z15) {
                                Intent intent111 = new Intent(ApplicationLoader.applicationContext, (Class<?>) PopupReplyReceiver.class);
                                intent111.putExtra("currentAccount", r1.currentAccount);
                                builder2.addAction(R.drawable.ic_ab_reply, LocaleController.getString(R.string.Reply), PendingIntent.getBroadcast(ApplicationLoader.applicationContext, 2, intent111, 201326592));
                            }
                            r1.showExtraNotifications(builder2, str8, j12, j11, str13, jArr4, i18, r11, i19, z14, z210, z10, i4);
                            scheduleNotificationRepeat();
                        }
                        i13 = i11;
                        user2 = user;
                        chat3 = chat2;
                        fileLocation = null;
                        intent.putExtra("currentAccount", r1.currentAccount);
                        builder2 = builder;
                        String str1111 = propertyString;
                        builder2.setContentTitle(string2).setSmallIcon(R.drawable.notification).setAutoCancel(true).setNumber(r1.total_unread_count).setContentIntent(PendingIntent.getActivity(ApplicationLoader.applicationContext, 0, intent, 1140850688)).setGroup(r1.notificationGroup).setGroupSummary(true).setShowWhen(true).setWhen(((long) messageObject.messageOwner.date) * j);
                        if (Build.VERSION.SDK_INT < 31) {
                            builder2.setColor(r1.getNotificationColor());
                        }
                        builder2.setCategory("msg");
                        if (chat3 == null) {
                            builder2.addPerson("tel:+" + user2.phone);
                        }
                        intent2 = new Intent(ApplicationLoader.applicationContext, (Class<?>) NotificationDismissReceiver.class);
                        intent2.putExtra("messageDate", messageObject.messageOwner.date);
                        intent2.putExtra("currentAccount", r1.currentAccount);
                        if (messageObject.isStoryPush) {
                            intent2.putExtra("story", true);
                        }
                        if (messageObject.isStoryReactionPush) {
                            i20 = 1;
                            intent2.putExtra("storyReaction", true);
                        } else {
                            i20 = 1;
                        }
                        builder2.setDeleteIntent(PendingIntent.getBroadcast(ApplicationLoader.applicationContext, i20, intent2, 167772160));
                        if (bitmap2 != null) {
                            builder2.setLargeIcon(bitmap2);
                        } else {
                            if (fileLocation != null) {
                                jArr2 = null;
                                imageFromMemory = ImageLoader.getInstance().getImageFromMemory(fileLocation, null, "50_50");
                                if (imageFromMemory != null) {
                                    builder2.setLargeIcon(imageFromMemory.getBitmap());
                                } else {
                                    pathToAttach = r1.getFileLoader().getPathToAttach(fileLocation, true);
                                    if (pathToAttach.exists()) {
                                        fDp = 160.0f / AndroidUtilities.dp(50.0f);
                                        BitmapFactory.Options options10 = new BitmapFactory.Options();
                                        if (fDp < 1.0f) {
                                            i16 = 1;
                                        } else {
                                            i16 = (int) fDp;
                                        }
                                        options10.inSampleSize = i16;
                                        bitmapDecodeFile = BitmapFactory.decodeFile(pathToAttach.getAbsolutePath(), options10);
                                        if (bitmapDecodeFile != null) {
                                            builder2.setLargeIcon(bitmapDecodeFile);
                                        }
                                    }
                                }
                            }
                            if (z) {
                                builder2.setPriority(-1);
                                if (Build.VERSION.SDK_INT >= 26) {
                                    i17 = 2;
                                } else {
                                    i17 = 0;
                                }
                            } else {
                                builder2.setPriority(-1);
                                if (Build.VERSION.SDK_INT >= 26) {
                                    i17 = 2;
                                } else {
                                    i17 = 0;
                                }
                            }
                            if (r14 == 1) {
                                i18 = i13;
                                long[] jArr11111 = {0, 0};
                                builder2.setVibrate(jArr11111);
                                jArr3 = jArr11111;
                                r11 = jArr2;
                            } else {
                                i18 = i13;
                                long[] jArr11112 = {0, 0};
                                builder2.setVibrate(jArr11112);
                                jArr3 = jArr11112;
                                r11 = jArr2;
                            }
                            if (AndroidUtilities.needShowPasscode()) {
                                i19 = i17;
                                jArr4 = jArr3;
                                z15 = false;
                            } else {
                                i19 = i17;
                                jArr4 = jArr3;
                                z15 = false;
                            }
                            if (!z15) {
                                Intent intent112 = new Intent(ApplicationLoader.applicationContext, (Class<?>) PopupReplyReceiver.class);
                                intent112.putExtra("currentAccount", r1.currentAccount);
                                builder2.addAction(R.drawable.ic_ab_reply, LocaleController.getString(R.string.Reply), PendingIntent.getBroadcast(ApplicationLoader.applicationContext, 2, intent112, 201326592));
                            }
                            r1.showExtraNotifications(builder2, str8, j12, j11, str13, jArr4, i18, r11, i19, z14, z210, z10, i4);
                            scheduleNotificationRepeat();
                        }
                        jArr2 = null;
                        if (z) {
                            builder2.setPriority(-1);
                            if (Build.VERSION.SDK_INT >= 26) {
                                i17 = 2;
                            } else {
                                i17 = 0;
                            }
                        } else {
                            builder2.setPriority(-1);
                            if (Build.VERSION.SDK_INT >= 26) {
                                i17 = 2;
                            } else {
                                i17 = 0;
                            }
                        }
                        if (r14 == 1) {
                            i18 = i13;
                            long[] jArr11113 = {0, 0};
                            builder2.setVibrate(jArr11113);
                            jArr3 = jArr11113;
                            r11 = jArr2;
                        } else {
                            i18 = i13;
                            long[] jArr11114 = {0, 0};
                            builder2.setVibrate(jArr11114);
                            jArr3 = jArr11114;
                            r11 = jArr2;
                        }
                        if (AndroidUtilities.needShowPasscode()) {
                            i19 = i17;
                            jArr4 = jArr3;
                            z15 = false;
                        } else {
                            i19 = i17;
                            jArr4 = jArr3;
                            z15 = false;
                        }
                        if (!z15) {
                            Intent intent113 = new Intent(ApplicationLoader.applicationContext, (Class<?>) PopupReplyReceiver.class);
                            intent113.putExtra("currentAccount", r1.currentAccount);
                            builder2.addAction(R.drawable.ic_ab_reply, LocaleController.getString(R.string.Reply), PendingIntent.getBroadcast(ApplicationLoader.applicationContext, 2, intent113, 201326592));
                        }
                        r1.showExtraNotifications(builder2, str8, j12, j11, str13, jArr4, i18, r11, i19, z14, z210, z10, i4);
                        scheduleNotificationRepeat();
                    }
                    z3 = zEqualsIgnoreCase;
                    z19 = true;
                    if (!z25) {
                        z4 = z19;
                        string2 = str13;
                    }
                    if (messageObject.isReactionPush) {
                        z5 = z4;
                        if (!sharedPreferences.getBoolean("EnableReactionsPreview", true)) {
                            string2 = LocaleController.getString(R.string.NotificationHiddenName);
                        }
                    } else {
                        z5 = z4;
                        if (!sharedPreferences.getBoolean("EnableReactionsPreview", true)) {
                            string2 = LocaleController.getString(R.string.NotificationHiddenName);
                        }
                    }
                    if (z3) {
                        if (UserConfig.getActivatedAccountsCount() <= 1) {
                            firstName = charSequence4;
                        } else if (r1.pushDialogs.size() == 1) {
                            firstName = UserObject.getFirstName(r1.getUserConfig().getCurrentUser());
                        } else {
                            firstName = UserObject.getFirstName(r1.getUserConfig().getCurrentUser()) + "・";
                        }
                        chat2 = chat;
                        if (r1.pushDialogs.size() == 1) {
                            j6 = jLongValue2;
                        } else {
                            j6 = jLongValue2;
                            if (r1.pushDialogs.size() == 1) {
                                firstName = firstName + LocaleController.formatPluralString("NewMessages", r1.total_unread_count, new Object[0]);
                            } else {
                                firstName = firstName + LocaleController.formatString(R.string.NotificationMessagesPeopleDisplayOrder, LocaleController.formatPluralString("NewMessages", r1.total_unread_count, new Object[0]), LocaleController.formatPluralString("FromChats", r1.pushDialogs.size(), new Object[0]));
                            }
                        }
                    } else {
                        j6 = jLongValue2;
                        chat2 = chat;
                        firstName = charSequence4;
                    }
                    builder = new NotificationCompat.Builder(ApplicationLoader.applicationContext);
                    i = 1;
                    if (r1.pushMessages.size() <= 1) {
                        j7 = j4;
                        charSequence = charSequence4;
                        zArr = new boolean[i];
                        stringForMessage = r1.getStringForMessage(messageObject, false, zArr, null);
                        zIsSilentMessage = r1.isSilentMessage(messageObject);
                        if (stringForMessage == null) {
                            return;
                        }
                        if (z5) {
                            strReplace = stringForMessage;
                        } else if (chat2 == null) {
                            if (zArr[0]) {
                                strReplace = stringForMessage.replace(string2 + ": ", charSequence);
                            } else {
                                strReplace = stringForMessage.replace(string2 + " ", charSequence);
                            }
                        } else if (zArr[0]) {
                            strReplace = stringForMessage.replace(string2 + ": ", charSequence);
                        } else {
                            strReplace = stringForMessage.replace(string2 + " ", charSequence);
                        }
                        builder.setContentText(strReplace);
                        if (z3) {
                            firstName = strReplace;
                        }
                        builder.setStyle(new NotificationCompat.BigTextStyle().bigText(strReplace));
                        str2 = stringForMessage;
                        r14 = zIsSilentMessage;
                    } else if (z3) {
                        i = 1;
                        j7 = j4;
                        charSequence = charSequence4;
                        zArr = new boolean[i];
                        stringForMessage = r1.getStringForMessage(messageObject, false, zArr, null);
                        zIsSilentMessage = r1.isSilentMessage(messageObject);
                        if (stringForMessage == null) {
                            return;
                        }
                        if (z5) {
                            strReplace = stringForMessage;
                        } else if (chat2 == null) {
                            if (zArr[0]) {
                                strReplace = stringForMessage.replace(string2 + ": ", charSequence);
                            } else {
                                strReplace = stringForMessage.replace(string2 + " ", charSequence);
                            }
                        } else if (zArr[0]) {
                            strReplace = stringForMessage.replace(string2 + ": ", charSequence);
                        } else {
                            strReplace = stringForMessage.replace(string2 + " ", charSequence);
                        }
                        builder.setContentText(strReplace);
                        if (z3) {
                            firstName = strReplace;
                        }
                        builder.setStyle(new NotificationCompat.BigTextStyle().bigText(strReplace));
                        str2 = stringForMessage;
                        r14 = zIsSilentMessage;
                    } else {
                        builder.setContentText(firstName);
                        NotificationCompat.InboxStyle inboxStyle2 = new NotificationCompat.InboxStyle();
                        inboxStyle2.setBigContentTitle(string2);
                        iMin = Math.min(10, r1.pushMessages.size());
                        zArr2 = new boolean[1];
                        i22 = 0;
                        IsSilentMessage = 2;
                        String str120 = null;
                        while (i22 < iMin) {
                            int i210 = iMin;
                            MessageObject messageObject5 = r1.pushMessages.get(i22);
                            long j28 = j4;
                            int i316 = i22;
                            stringForMessage2 = r1.getStringForMessage(messageObject5, false, zArr2, null);
                            if (stringForMessage2 != null) {
                                charSequence3 = charSequence4;
                            } else {
                                charSequence3 = charSequence4;
                            }
                            charSequence4 = charSequence3;
                            iMin = i210;
                            i22 = i316 + 1;
                            j4 = j28;
                            IsSilentMessage = IsSilentMessage;
                        }
                        j7 = j4;
                        charSequence = charSequence4;
                        inboxStyle2.setSummaryText(firstName);
                        builder.setStyle(inboxStyle2);
                        str2 = str120;
                        r14 = IsSilentMessage;
                    }
                    str3 = firstName;
                    if (z) {
                        z6 = true;
                    } else {
                        z6 = true;
                    }
                    if (z6) {
                        str4 = str3;
                        charSequence2 = charSequence;
                        sharedPreferences2 = sharedPreferences;
                        j8 = j7;
                        z7 = z6;
                    } else {
                        str4 = str3;
                        charSequence2 = charSequence;
                        sharedPreferences2 = sharedPreferences;
                        j8 = j7;
                        z7 = z6;
                    }
                    if (z7) {
                        if (!sharedPreferences2.getBoolean("sound_enabled_" + getSharedPrefKey(j8, j9), true)) {
                            j9 = j5;
                            z7 = true;
                        }
                    } else {
                        j9 = j5;
                    }
                    j9 = j5;
                    path = Settings.System.DEFAULT_NOTIFICATION_URI.getPath();
                    z8 = ApplicationLoader.mainInterfacePaused;
                    boolean z211 = !z8;
                    getSharedPrefKey(j8, j9);
                    j10 = j8;
                    j11 = j9;
                    if (r1.dialogsNotificationsFacade.getProperty(NotificationsSettingsFacade.PROPERTY_CUSTOM, j10, j11, false)) {
                        property = r1.dialogsNotificationsFacade.getProperty("vibrate_", j10, j11, 0);
                        property2 = r1.dialogsNotificationsFacade.getProperty("priority_", j10, j11, 3);
                        property3 = r1.dialogsNotificationsFacade.getProperty("sound_document_id_", j10, j11, 0L);
                        if (property3 != j2) {
                            propertyString = r1.getMediaDataController().ringtoneDataStore.getSoundPath(property3);
                            z9 = true;
                        } else {
                            propertyString = r1.dialogsNotificationsFacade.getPropertyString("sound_path_", j10, j11, null);
                            z9 = false;
                        }
                        int property6 = r1.dialogsNotificationsFacade.getProperty("color_", j10, j11, 0);
                        j12 = j10;
                        if (property6 != 0) {
                        }
                        z10 = z7;
                        if (!messageObject.isReactionPush) {
                            j13 = sharedPreferences2.getLong("ReactionSoundDocId", 0L);
                            if (j13 != 0) {
                                string3 = r1.getMediaDataController().ringtoneDataStore.getSoundPath(j13);
                                z11 = true;
                            } else {
                                string3 = sharedPreferences2.getString("ReactionSoundPath", path);
                                z11 = false;
                            }
                            i2 = sharedPreferences2.getInt("vibrate_react", 0);
                            str5 = string3;
                            int i317 = sharedPreferences2.getInt("priority_react", 1);
                            z12 = z11;
                            iIntValue = sharedPreferences2.getInt("ReactionsLed", -16776961);
                            if (messageObject.isStoryReactionPush) {
                                i3 = 5;
                            } else {
                                i3 = 4;
                            }
                            i4 = i3;
                            i5 = i317;
                            i7 = i2;
                            str6 = str5;
                            i6 = 4;
                        } else {
                            j13 = sharedPreferences2.getLong("ReactionSoundDocId", 0L);
                            if (j13 != 0) {
                                string3 = r1.getMediaDataController().ringtoneDataStore.getSoundPath(j13);
                                z11 = true;
                            } else {
                                string3 = sharedPreferences2.getString("ReactionSoundPath", path);
                                z11 = false;
                            }
                            i2 = sharedPreferences2.getInt("vibrate_react", 0);
                            str5 = string3;
                            int i318 = sharedPreferences2.getInt("priority_react", 1);
                            z12 = z11;
                            iIntValue = sharedPreferences2.getInt("ReactionsLed", -16776961);
                            if (messageObject.isStoryReactionPush) {
                                i3 = 5;
                            } else {
                                i3 = 4;
                            }
                            i4 = i3;
                            i5 = i318;
                            i7 = i2;
                            str6 = str5;
                            i6 = 4;
                        }
                        if (i7 == i6) {
                            z13 = true;
                            i8 = 0;
                        } else {
                            i8 = i7;
                            z13 = false;
                        }
                        if (TextUtils.isEmpty(propertyString)) {
                            propertyString = str6;
                            z9 = z12;
                            z14 = true;
                        } else {
                            propertyString = str6;
                            z9 = z12;
                            z14 = true;
                        }
                        if (property2 != 3) {
                            i5 = property2;
                            z14 = false;
                        }
                        if (numValueOf != null) {
                            iIntValue = numValueOf.intValue();
                            z14 = false;
                        }
                        if (property != 0) {
                            property = i8;
                        } else {
                            property = i8;
                        }
                        if (z8) {
                            if (!sharedPreferences2.getBoolean("EnableInAppSounds", true)) {
                                propertyString = null;
                            }
                            if (sharedPreferences2.getBoolean("EnableInAppVibrate", true)) {
                                i10 = 2;
                            } else {
                                i10 = property;
                            }
                            if (sharedPreferences2.getBoolean("EnableInAppPopup", true)) {
                                i9 = 2;
                            } else {
                                i9 = 0;
                            }
                        } else {
                            i9 = i5;
                            i10 = property;
                        }
                        if (z13) {
                            ringerMode = audioManager.getRingerMode();
                            if (ringerMode != 0) {
                                i10 = 2;
                            }
                        }
                        if (z10) {
                            str7 = str4;
                            i10 = 0;
                            i9 = 0;
                            i11 = 0;
                            propertyString = null;
                        } else {
                            String str1112 = str4;
                            i11 = iIntValue;
                            str7 = str1112;
                        }
                        intent = new Intent(ApplicationLoader.applicationContext, (Class<?>) LaunchActivity.class);
                        intent.setAction("com.tmessages.openchat" + Math.random() + Integer.MAX_VALUE);
                        intent.setFlags(67108864);
                        if (messageObject.isOauthPush) {
                            intent.putExtra("oauth_url", messageObject.localName);
                        }
                        if (messageObject.isStoryReactionPush) {
                            intent.putExtra("storyId", Math.abs(messageObject.getId()));
                            i12 = i10;
                            str8 = str7;
                        } else {
                            if (messageObject.isLiveStoryPush) {
                                if (r15 != 0) {
                                    i15 = i10;
                                    str8 = str7;
                                    intent.putExtra("chatId", j3);
                                } else {
                                    i15 = i10;
                                    str8 = str7;
                                    if (j6 != 0) {
                                        intent.putExtra("userId", j6);
                                    }
                                }
                                intent.putExtra("storyId", Math.abs(messageObject.getId()));
                                i12 = i15;
                            } else {
                                str8 = str7;
                                j14 = j6;
                                i12 = i10;
                                i13 = i11;
                                j15 = j3;
                                if (messageObject.isStoryPush) {
                                    jArr = new long[r1.storyPushMessages.size()];
                                    while (i14 < r1.storyPushMessages.size()) {
                                        jArr[i14] = r1.storyPushMessages.get(i14).dialogId;
                                    }
                                    intent.putExtra("storyDialogIds", jArr);
                                    path = path;
                                } else {
                                    if (!DialogObject.isEncryptedDialog(j12)) {
                                        path = path;
                                        if (r1.pushDialogs.size() == 1) {
                                            if (r15 != 0) {
                                                intent.putExtra("chatId", j15);
                                            } else if (j14 != 0) {
                                                intent.putExtra("userId", j14);
                                            }
                                        }
                                        if (AndroidUtilities.needShowPasscode()) {
                                        }
                                    } else {
                                        path = path;
                                        user2 = user;
                                        chat3 = chat2;
                                        if (r1.pushDialogs.size() == 1) {
                                            intent.putExtra("encId", DialogObject.getEncryptedChatId(j12));
                                        }
                                    }
                                    fileLocation = null;
                                }
                                user2 = user;
                                chat3 = chat2;
                                fileLocation = null;
                            }
                            intent.putExtra("currentAccount", r1.currentAccount);
                            builder2 = builder;
                            String str1113 = propertyString;
                            builder2.setContentTitle(string2).setSmallIcon(R.drawable.notification).setAutoCancel(true).setNumber(r1.total_unread_count).setContentIntent(PendingIntent.getActivity(ApplicationLoader.applicationContext, 0, intent, 1140850688)).setGroup(r1.notificationGroup).setGroupSummary(true).setShowWhen(true).setWhen(((long) messageObject.messageOwner.date) * j);
                            if (Build.VERSION.SDK_INT < 31) {
                                builder2.setColor(r1.getNotificationColor());
                            }
                            builder2.setCategory("msg");
                            if (chat3 == null) {
                                builder2.addPerson("tel:+" + user2.phone);
                            }
                            intent2 = new Intent(ApplicationLoader.applicationContext, (Class<?>) NotificationDismissReceiver.class);
                            intent2.putExtra("messageDate", messageObject.messageOwner.date);
                            intent2.putExtra("currentAccount", r1.currentAccount);
                            if (messageObject.isStoryPush) {
                                intent2.putExtra("story", true);
                            }
                            if (messageObject.isStoryReactionPush) {
                                i20 = 1;
                                intent2.putExtra("storyReaction", true);
                            } else {
                                i20 = 1;
                            }
                            builder2.setDeleteIntent(PendingIntent.getBroadcast(ApplicationLoader.applicationContext, i20, intent2, 167772160));
                            if (bitmap2 != null) {
                                builder2.setLargeIcon(bitmap2);
                            } else {
                                if (fileLocation != null) {
                                    jArr2 = null;
                                    imageFromMemory = ImageLoader.getInstance().getImageFromMemory(fileLocation, null, "50_50");
                                    if (imageFromMemory != null) {
                                        builder2.setLargeIcon(imageFromMemory.getBitmap());
                                    } else {
                                        pathToAttach = r1.getFileLoader().getPathToAttach(fileLocation, true);
                                        if (pathToAttach.exists()) {
                                            fDp = 160.0f / AndroidUtilities.dp(50.0f);
                                            BitmapFactory.Options options11 = new BitmapFactory.Options();
                                            if (fDp < 1.0f) {
                                                i16 = 1;
                                            } else {
                                                i16 = (int) fDp;
                                            }
                                            options11.inSampleSize = i16;
                                            bitmapDecodeFile = BitmapFactory.decodeFile(pathToAttach.getAbsolutePath(), options11);
                                            if (bitmapDecodeFile != null) {
                                                builder2.setLargeIcon(bitmapDecodeFile);
                                            }
                                        }
                                    }
                                }
                                if (z) {
                                    builder2.setPriority(-1);
                                    if (Build.VERSION.SDK_INT >= 26) {
                                        i17 = 2;
                                    } else {
                                        i17 = 0;
                                    }
                                } else {
                                    builder2.setPriority(-1);
                                    if (Build.VERSION.SDK_INT >= 26) {
                                        i17 = 2;
                                    } else {
                                        i17 = 0;
                                    }
                                }
                                if (r14 == 1) {
                                    i18 = i13;
                                    long[] jArr11115 = {0, 0};
                                    builder2.setVibrate(jArr11115);
                                    jArr3 = jArr11115;
                                    r11 = jArr2;
                                } else {
                                    i18 = i13;
                                    long[] jArr11116 = {0, 0};
                                    builder2.setVibrate(jArr11116);
                                    jArr3 = jArr11116;
                                    r11 = jArr2;
                                }
                                if (AndroidUtilities.needShowPasscode()) {
                                    i19 = i17;
                                    jArr4 = jArr3;
                                    z15 = false;
                                } else {
                                    i19 = i17;
                                    jArr4 = jArr3;
                                    z15 = false;
                                }
                                if (!z15) {
                                    Intent intent114 = new Intent(ApplicationLoader.applicationContext, (Class<?>) PopupReplyReceiver.class);
                                    intent114.putExtra("currentAccount", r1.currentAccount);
                                    builder2.addAction(R.drawable.ic_ab_reply, LocaleController.getString(R.string.Reply), PendingIntent.getBroadcast(ApplicationLoader.applicationContext, 2, intent114, 201326592));
                                }
                                r1.showExtraNotifications(builder2, str8, j12, j11, str13, jArr4, i18, r11, i19, z14, z211, z10, i4);
                                scheduleNotificationRepeat();
                            }
                            jArr2 = null;
                            if (z) {
                                builder2.setPriority(-1);
                                if (Build.VERSION.SDK_INT >= 26) {
                                    i17 = 2;
                                } else {
                                    i17 = 0;
                                }
                            } else {
                                builder2.setPriority(-1);
                                if (Build.VERSION.SDK_INT >= 26) {
                                    i17 = 2;
                                } else {
                                    i17 = 0;
                                }
                            }
                            if (r14 == 1) {
                                i18 = i13;
                                long[] jArr11117 = {0, 0};
                                builder2.setVibrate(jArr11117);
                                jArr3 = jArr11117;
                                r11 = jArr2;
                            } else {
                                i18 = i13;
                                long[] jArr11118 = {0, 0};
                                builder2.setVibrate(jArr11118);
                                jArr3 = jArr11118;
                                r11 = jArr2;
                            }
                            if (AndroidUtilities.needShowPasscode()) {
                                i19 = i17;
                                jArr4 = jArr3;
                                z15 = false;
                            } else {
                                i19 = i17;
                                jArr4 = jArr3;
                                z15 = false;
                            }
                            if (!z15) {
                                Intent intent115 = new Intent(ApplicationLoader.applicationContext, (Class<?>) PopupReplyReceiver.class);
                                intent115.putExtra("currentAccount", r1.currentAccount);
                                builder2.addAction(R.drawable.ic_ab_reply, LocaleController.getString(R.string.Reply), PendingIntent.getBroadcast(ApplicationLoader.applicationContext, 2, intent115, 201326592));
                            }
                            r1.showExtraNotifications(builder2, str8, j12, j11, str13, jArr4, i18, r11, i19, z14, z211, z10, i4);
                            scheduleNotificationRepeat();
                        }
                        i13 = i11;
                        user2 = user;
                        chat3 = chat2;
                        fileLocation = null;
                        intent.putExtra("currentAccount", r1.currentAccount);
                        builder2 = builder;
                        String str1114 = propertyString;
                        builder2.setContentTitle(string2).setSmallIcon(R.drawable.notification).setAutoCancel(true).setNumber(r1.total_unread_count).setContentIntent(PendingIntent.getActivity(ApplicationLoader.applicationContext, 0, intent, 1140850688)).setGroup(r1.notificationGroup).setGroupSummary(true).setShowWhen(true).setWhen(((long) messageObject.messageOwner.date) * j);
                        if (Build.VERSION.SDK_INT < 31) {
                            builder2.setColor(r1.getNotificationColor());
                        }
                        builder2.setCategory("msg");
                        if (chat3 == null) {
                            builder2.addPerson("tel:+" + user2.phone);
                        }
                        intent2 = new Intent(ApplicationLoader.applicationContext, (Class<?>) NotificationDismissReceiver.class);
                        intent2.putExtra("messageDate", messageObject.messageOwner.date);
                        intent2.putExtra("currentAccount", r1.currentAccount);
                        if (messageObject.isStoryPush) {
                            intent2.putExtra("story", true);
                        }
                        if (messageObject.isStoryReactionPush) {
                            i20 = 1;
                            intent2.putExtra("storyReaction", true);
                        } else {
                            i20 = 1;
                        }
                        builder2.setDeleteIntent(PendingIntent.getBroadcast(ApplicationLoader.applicationContext, i20, intent2, 167772160));
                        if (bitmap2 != null) {
                            builder2.setLargeIcon(bitmap2);
                        } else {
                            if (fileLocation != null) {
                                jArr2 = null;
                                imageFromMemory = ImageLoader.getInstance().getImageFromMemory(fileLocation, null, "50_50");
                                if (imageFromMemory != null) {
                                    builder2.setLargeIcon(imageFromMemory.getBitmap());
                                } else {
                                    pathToAttach = r1.getFileLoader().getPathToAttach(fileLocation, true);
                                    if (pathToAttach.exists()) {
                                        fDp = 160.0f / AndroidUtilities.dp(50.0f);
                                        BitmapFactory.Options options12 = new BitmapFactory.Options();
                                        if (fDp < 1.0f) {
                                            i16 = 1;
                                        } else {
                                            i16 = (int) fDp;
                                        }
                                        options12.inSampleSize = i16;
                                        bitmapDecodeFile = BitmapFactory.decodeFile(pathToAttach.getAbsolutePath(), options12);
                                        if (bitmapDecodeFile != null) {
                                            builder2.setLargeIcon(bitmapDecodeFile);
                                        }
                                    }
                                }
                            }
                            if (z) {
                                builder2.setPriority(-1);
                                if (Build.VERSION.SDK_INT >= 26) {
                                    i17 = 2;
                                } else {
                                    i17 = 0;
                                }
                            } else {
                                builder2.setPriority(-1);
                                if (Build.VERSION.SDK_INT >= 26) {
                                    i17 = 2;
                                } else {
                                    i17 = 0;
                                }
                            }
                            if (r14 == 1) {
                                i18 = i13;
                                long[] jArr11119 = {0, 0};
                                builder2.setVibrate(jArr11119);
                                jArr3 = jArr11119;
                                r11 = jArr2;
                            } else {
                                i18 = i13;
                                long[] jArr111110 = {0, 0};
                                builder2.setVibrate(jArr111110);
                                jArr3 = jArr111110;
                                r11 = jArr2;
                            }
                            if (AndroidUtilities.needShowPasscode()) {
                                i19 = i17;
                                jArr4 = jArr3;
                                z15 = false;
                            } else {
                                i19 = i17;
                                jArr4 = jArr3;
                                z15 = false;
                            }
                            if (!z15) {
                                Intent intent116 = new Intent(ApplicationLoader.applicationContext, (Class<?>) PopupReplyReceiver.class);
                                intent116.putExtra("currentAccount", r1.currentAccount);
                                builder2.addAction(R.drawable.ic_ab_reply, LocaleController.getString(R.string.Reply), PendingIntent.getBroadcast(ApplicationLoader.applicationContext, 2, intent116, 201326592));
                            }
                            r1.showExtraNotifications(builder2, str8, j12, j11, str13, jArr4, i18, r11, i19, z14, z211, z10, i4);
                            scheduleNotificationRepeat();
                        }
                        jArr2 = null;
                        if (z) {
                            builder2.setPriority(-1);
                            if (Build.VERSION.SDK_INT >= 26) {
                                i17 = 2;
                            } else {
                                i17 = 0;
                            }
                        } else {
                            builder2.setPriority(-1);
                            if (Build.VERSION.SDK_INT >= 26) {
                                i17 = 2;
                            } else {
                                i17 = 0;
                            }
                        }
                        if (r14 == 1) {
                            i18 = i13;
                            long[] jArr111111 = {0, 0};
                            builder2.setVibrate(jArr111111);
                            jArr3 = jArr111111;
                            r11 = jArr2;
                        } else {
                            i18 = i13;
                            long[] jArr111112 = {0, 0};
                            builder2.setVibrate(jArr111112);
                            jArr3 = jArr111112;
                            r11 = jArr2;
                        }
                        if (AndroidUtilities.needShowPasscode()) {
                            i19 = i17;
                            jArr4 = jArr3;
                            z15 = false;
                        } else {
                            i19 = i17;
                            jArr4 = jArr3;
                            z15 = false;
                        }
                        if (!z15) {
                            Intent intent117 = new Intent(ApplicationLoader.applicationContext, (Class<?>) PopupReplyReceiver.class);
                            intent117.putExtra("currentAccount", r1.currentAccount);
                            builder2.addAction(R.drawable.ic_ab_reply, LocaleController.getString(R.string.Reply), PendingIntent.getBroadcast(ApplicationLoader.applicationContext, 2, intent117, 201326592));
                        }
                        r1.showExtraNotifications(builder2, str8, j12, j11, str13, jArr4, i18, r11, i19, z14, z211, z10, i4);
                        scheduleNotificationRepeat();
                    }
                    j12 = j10;
                    property = 0;
                    property2 = 3;
                    z9 = false;
                    propertyString = null;
                    z10 = z7;
                    if (!messageObject.isReactionPush) {
                        j13 = sharedPreferences2.getLong("ReactionSoundDocId", 0L);
                        if (j13 != 0) {
                            string3 = r1.getMediaDataController().ringtoneDataStore.getSoundPath(j13);
                            z11 = true;
                        } else {
                            string3 = sharedPreferences2.getString("ReactionSoundPath", path);
                            z11 = false;
                        }
                        i2 = sharedPreferences2.getInt("vibrate_react", 0);
                        str5 = string3;
                        int i319 = sharedPreferences2.getInt("priority_react", 1);
                        z12 = z11;
                        iIntValue = sharedPreferences2.getInt("ReactionsLed", -16776961);
                        if (messageObject.isStoryReactionPush) {
                            i3 = 5;
                        } else {
                            i3 = 4;
                        }
                        i4 = i3;
                        i5 = i319;
                        i7 = i2;
                        str6 = str5;
                        i6 = 4;
                    } else {
                        j13 = sharedPreferences2.getLong("ReactionSoundDocId", 0L);
                        if (j13 != 0) {
                            string3 = r1.getMediaDataController().ringtoneDataStore.getSoundPath(j13);
                            z11 = true;
                        } else {
                            string3 = sharedPreferences2.getString("ReactionSoundPath", path);
                            z11 = false;
                        }
                        i2 = sharedPreferences2.getInt("vibrate_react", 0);
                        str5 = string3;
                        int i3110 = sharedPreferences2.getInt("priority_react", 1);
                        z12 = z11;
                        iIntValue = sharedPreferences2.getInt("ReactionsLed", -16776961);
                        if (messageObject.isStoryReactionPush) {
                            i3 = 5;
                        } else {
                            i3 = 4;
                        }
                        i4 = i3;
                        i5 = i3110;
                        i7 = i2;
                        str6 = str5;
                        i6 = 4;
                    }
                    if (i7 == i6) {
                        z13 = true;
                        i8 = 0;
                    } else {
                        i8 = i7;
                        z13 = false;
                    }
                    if (TextUtils.isEmpty(propertyString)) {
                        propertyString = str6;
                        z9 = z12;
                        z14 = true;
                    } else {
                        propertyString = str6;
                        z9 = z12;
                        z14 = true;
                    }
                    if (property2 != 3) {
                        i5 = property2;
                        z14 = false;
                    }
                    if (numValueOf != null) {
                        iIntValue = numValueOf.intValue();
                        z14 = false;
                    }
                    if (property != 0) {
                        property = i8;
                    } else {
                        property = i8;
                    }
                    if (z8) {
                        if (!sharedPreferences2.getBoolean("EnableInAppSounds", true)) {
                            propertyString = null;
                        }
                        if (sharedPreferences2.getBoolean("EnableInAppVibrate", true)) {
                            i10 = 2;
                        } else {
                            i10 = property;
                        }
                        if (sharedPreferences2.getBoolean("EnableInAppPopup", true)) {
                            i9 = 2;
                        } else {
                            i9 = 0;
                        }
                    } else {
                        i9 = i5;
                        i10 = property;
                    }
                    if (z13) {
                        ringerMode = audioManager.getRingerMode();
                        if (ringerMode != 0) {
                            i10 = 2;
                        }
                    }
                    if (z10) {
                        str7 = str4;
                        i10 = 0;
                        i9 = 0;
                        i11 = 0;
                        propertyString = null;
                    } else {
                        String str1115 = str4;
                        i11 = iIntValue;
                        str7 = str1115;
                    }
                    intent = new Intent(ApplicationLoader.applicationContext, (Class<?>) LaunchActivity.class);
                    intent.setAction("com.tmessages.openchat" + Math.random() + Integer.MAX_VALUE);
                    intent.setFlags(67108864);
                    if (messageObject.isOauthPush) {
                        intent.putExtra("oauth_url", messageObject.localName);
                    }
                    if (messageObject.isStoryReactionPush) {
                        intent.putExtra("storyId", Math.abs(messageObject.getId()));
                        i12 = i10;
                        str8 = str7;
                    } else {
                        if (messageObject.isLiveStoryPush) {
                            if (r15 != 0) {
                                i15 = i10;
                                str8 = str7;
                                intent.putExtra("chatId", j3);
                            } else {
                                i15 = i10;
                                str8 = str7;
                                if (j6 != 0) {
                                    intent.putExtra("userId", j6);
                                }
                            }
                            intent.putExtra("storyId", Math.abs(messageObject.getId()));
                            i12 = i15;
                        } else {
                            str8 = str7;
                            j14 = j6;
                            i12 = i10;
                            i13 = i11;
                            j15 = j3;
                            if (messageObject.isStoryPush) {
                                jArr = new long[r1.storyPushMessages.size()];
                                while (i14 < r1.storyPushMessages.size()) {
                                    jArr[i14] = r1.storyPushMessages.get(i14).dialogId;
                                }
                                intent.putExtra("storyDialogIds", jArr);
                                path = path;
                            } else {
                                if (!DialogObject.isEncryptedDialog(j12)) {
                                    path = path;
                                    if (r1.pushDialogs.size() == 1) {
                                        if (r15 != 0) {
                                            intent.putExtra("chatId", j15);
                                        } else if (j14 != 0) {
                                            intent.putExtra("userId", j14);
                                        }
                                    }
                                    if (AndroidUtilities.needShowPasscode()) {
                                    }
                                } else {
                                    path = path;
                                    user2 = user;
                                    chat3 = chat2;
                                    if (r1.pushDialogs.size() == 1) {
                                        intent.putExtra("encId", DialogObject.getEncryptedChatId(j12));
                                    }
                                }
                                fileLocation = null;
                            }
                            user2 = user;
                            chat3 = chat2;
                            fileLocation = null;
                        }
                        intent.putExtra("currentAccount", r1.currentAccount);
                        builder2 = builder;
                        String str1116 = propertyString;
                        builder2.setContentTitle(string2).setSmallIcon(R.drawable.notification).setAutoCancel(true).setNumber(r1.total_unread_count).setContentIntent(PendingIntent.getActivity(ApplicationLoader.applicationContext, 0, intent, 1140850688)).setGroup(r1.notificationGroup).setGroupSummary(true).setShowWhen(true).setWhen(((long) messageObject.messageOwner.date) * j);
                        if (Build.VERSION.SDK_INT < 31) {
                            builder2.setColor(r1.getNotificationColor());
                        }
                        builder2.setCategory("msg");
                        if (chat3 == null) {
                            builder2.addPerson("tel:+" + user2.phone);
                        }
                        intent2 = new Intent(ApplicationLoader.applicationContext, (Class<?>) NotificationDismissReceiver.class);
                        intent2.putExtra("messageDate", messageObject.messageOwner.date);
                        intent2.putExtra("currentAccount", r1.currentAccount);
                        if (messageObject.isStoryPush) {
                            intent2.putExtra("story", true);
                        }
                        if (messageObject.isStoryReactionPush) {
                            i20 = 1;
                            intent2.putExtra("storyReaction", true);
                        } else {
                            i20 = 1;
                        }
                        builder2.setDeleteIntent(PendingIntent.getBroadcast(ApplicationLoader.applicationContext, i20, intent2, 167772160));
                        if (bitmap2 != null) {
                            builder2.setLargeIcon(bitmap2);
                        } else {
                            if (fileLocation != null) {
                                jArr2 = null;
                                imageFromMemory = ImageLoader.getInstance().getImageFromMemory(fileLocation, null, "50_50");
                                if (imageFromMemory != null) {
                                    builder2.setLargeIcon(imageFromMemory.getBitmap());
                                } else {
                                    pathToAttach = r1.getFileLoader().getPathToAttach(fileLocation, true);
                                    if (pathToAttach.exists()) {
                                        fDp = 160.0f / AndroidUtilities.dp(50.0f);
                                        BitmapFactory.Options options13 = new BitmapFactory.Options();
                                        if (fDp < 1.0f) {
                                            i16 = 1;
                                        } else {
                                            i16 = (int) fDp;
                                        }
                                        options13.inSampleSize = i16;
                                        bitmapDecodeFile = BitmapFactory.decodeFile(pathToAttach.getAbsolutePath(), options13);
                                        if (bitmapDecodeFile != null) {
                                            builder2.setLargeIcon(bitmapDecodeFile);
                                        }
                                    }
                                }
                            }
                            if (z) {
                                builder2.setPriority(-1);
                                if (Build.VERSION.SDK_INT >= 26) {
                                    i17 = 2;
                                } else {
                                    i17 = 0;
                                }
                            } else {
                                builder2.setPriority(-1);
                                if (Build.VERSION.SDK_INT >= 26) {
                                    i17 = 2;
                                } else {
                                    i17 = 0;
                                }
                            }
                            if (r14 == 1) {
                                i18 = i13;
                                long[] jArr111113 = {0, 0};
                                builder2.setVibrate(jArr111113);
                                jArr3 = jArr111113;
                                r11 = jArr2;
                            } else {
                                i18 = i13;
                                long[] jArr111114 = {0, 0};
                                builder2.setVibrate(jArr111114);
                                jArr3 = jArr111114;
                                r11 = jArr2;
                            }
                            if (AndroidUtilities.needShowPasscode()) {
                                i19 = i17;
                                jArr4 = jArr3;
                                z15 = false;
                            } else {
                                i19 = i17;
                                jArr4 = jArr3;
                                z15 = false;
                            }
                            if (!z15) {
                                Intent intent118 = new Intent(ApplicationLoader.applicationContext, (Class<?>) PopupReplyReceiver.class);
                                intent118.putExtra("currentAccount", r1.currentAccount);
                                builder2.addAction(R.drawable.ic_ab_reply, LocaleController.getString(R.string.Reply), PendingIntent.getBroadcast(ApplicationLoader.applicationContext, 2, intent118, 201326592));
                            }
                            r1.showExtraNotifications(builder2, str8, j12, j11, str13, jArr4, i18, r11, i19, z14, z211, z10, i4);
                            scheduleNotificationRepeat();
                        }
                        jArr2 = null;
                        if (z) {
                            builder2.setPriority(-1);
                            if (Build.VERSION.SDK_INT >= 26) {
                                i17 = 2;
                            } else {
                                i17 = 0;
                            }
                        } else {
                            builder2.setPriority(-1);
                            if (Build.VERSION.SDK_INT >= 26) {
                                i17 = 2;
                            } else {
                                i17 = 0;
                            }
                        }
                        if (r14 == 1) {
                            i18 = i13;
                            long[] jArr111115 = {0, 0};
                            builder2.setVibrate(jArr111115);
                            jArr3 = jArr111115;
                            r11 = jArr2;
                        } else {
                            i18 = i13;
                            long[] jArr111116 = {0, 0};
                            builder2.setVibrate(jArr111116);
                            jArr3 = jArr111116;
                            r11 = jArr2;
                        }
                        if (AndroidUtilities.needShowPasscode()) {
                            i19 = i17;
                            jArr4 = jArr3;
                            z15 = false;
                        } else {
                            i19 = i17;
                            jArr4 = jArr3;
                            z15 = false;
                        }
                        if (!z15) {
                            Intent intent119 = new Intent(ApplicationLoader.applicationContext, (Class<?>) PopupReplyReceiver.class);
                            intent119.putExtra("currentAccount", r1.currentAccount);
                            builder2.addAction(R.drawable.ic_ab_reply, LocaleController.getString(R.string.Reply), PendingIntent.getBroadcast(ApplicationLoader.applicationContext, 2, intent119, 201326592));
                        }
                        r1.showExtraNotifications(builder2, str8, j12, j11, str13, jArr4, i18, r11, i19, z14, z211, z10, i4);
                        scheduleNotificationRepeat();
                    }
                    i13 = i11;
                    user2 = user;
                    chat3 = chat2;
                    fileLocation = null;
                    intent.putExtra("currentAccount", r1.currentAccount);
                    builder2 = builder;
                    String str1117 = propertyString;
                    builder2.setContentTitle(string2).setSmallIcon(R.drawable.notification).setAutoCancel(true).setNumber(r1.total_unread_count).setContentIntent(PendingIntent.getActivity(ApplicationLoader.applicationContext, 0, intent, 1140850688)).setGroup(r1.notificationGroup).setGroupSummary(true).setShowWhen(true).setWhen(((long) messageObject.messageOwner.date) * j);
                    if (Build.VERSION.SDK_INT < 31) {
                        builder2.setColor(r1.getNotificationColor());
                    }
                    builder2.setCategory("msg");
                    if (chat3 == null) {
                        builder2.addPerson("tel:+" + user2.phone);
                    }
                    intent2 = new Intent(ApplicationLoader.applicationContext, (Class<?>) NotificationDismissReceiver.class);
                    intent2.putExtra("messageDate", messageObject.messageOwner.date);
                    intent2.putExtra("currentAccount", r1.currentAccount);
                    if (messageObject.isStoryPush) {
                        intent2.putExtra("story", true);
                    }
                    if (messageObject.isStoryReactionPush) {
                        i20 = 1;
                        intent2.putExtra("storyReaction", true);
                    } else {
                        i20 = 1;
                    }
                    builder2.setDeleteIntent(PendingIntent.getBroadcast(ApplicationLoader.applicationContext, i20, intent2, 167772160));
                    if (bitmap2 != null) {
                        builder2.setLargeIcon(bitmap2);
                    } else {
                        if (fileLocation != null) {
                            jArr2 = null;
                            imageFromMemory = ImageLoader.getInstance().getImageFromMemory(fileLocation, null, "50_50");
                            if (imageFromMemory != null) {
                                builder2.setLargeIcon(imageFromMemory.getBitmap());
                            } else {
                                pathToAttach = r1.getFileLoader().getPathToAttach(fileLocation, true);
                                if (pathToAttach.exists()) {
                                    fDp = 160.0f / AndroidUtilities.dp(50.0f);
                                    BitmapFactory.Options options14 = new BitmapFactory.Options();
                                    if (fDp < 1.0f) {
                                        i16 = 1;
                                    } else {
                                        i16 = (int) fDp;
                                    }
                                    options14.inSampleSize = i16;
                                    bitmapDecodeFile = BitmapFactory.decodeFile(pathToAttach.getAbsolutePath(), options14);
                                    if (bitmapDecodeFile != null) {
                                        builder2.setLargeIcon(bitmapDecodeFile);
                                    }
                                }
                            }
                        }
                        if (z) {
                            builder2.setPriority(-1);
                            if (Build.VERSION.SDK_INT >= 26) {
                                i17 = 2;
                            } else {
                                i17 = 0;
                            }
                        } else {
                            builder2.setPriority(-1);
                            if (Build.VERSION.SDK_INT >= 26) {
                                i17 = 2;
                            } else {
                                i17 = 0;
                            }
                        }
                        if (r14 == 1) {
                            i18 = i13;
                            long[] jArr111117 = {0, 0};
                            builder2.setVibrate(jArr111117);
                            jArr3 = jArr111117;
                            r11 = jArr2;
                        } else {
                            i18 = i13;
                            long[] jArr111118 = {0, 0};
                            builder2.setVibrate(jArr111118);
                            jArr3 = jArr111118;
                            r11 = jArr2;
                        }
                        if (AndroidUtilities.needShowPasscode()) {
                            i19 = i17;
                            jArr4 = jArr3;
                            z15 = false;
                        } else {
                            i19 = i17;
                            jArr4 = jArr3;
                            z15 = false;
                        }
                        if (!z15) {
                            Intent intent1110 = new Intent(ApplicationLoader.applicationContext, (Class<?>) PopupReplyReceiver.class);
                            intent1110.putExtra("currentAccount", r1.currentAccount);
                            builder2.addAction(R.drawable.ic_ab_reply, LocaleController.getString(R.string.Reply), PendingIntent.getBroadcast(ApplicationLoader.applicationContext, 2, intent1110, 201326592));
                        }
                        r1.showExtraNotifications(builder2, str8, j12, j11, str13, jArr4, i18, r11, i19, z14, z211, z10, i4);
                        scheduleNotificationRepeat();
                    }
                    jArr2 = null;
                    if (z) {
                        builder2.setPriority(-1);
                        if (Build.VERSION.SDK_INT >= 26) {
                            i17 = 2;
                        } else {
                            i17 = 0;
                        }
                    } else {
                        builder2.setPriority(-1);
                        if (Build.VERSION.SDK_INT >= 26) {
                            i17 = 2;
                        } else {
                            i17 = 0;
                        }
                    }
                    if (r14 == 1) {
                        i18 = i13;
                        long[] jArr111119 = {0, 0};
                        builder2.setVibrate(jArr111119);
                        jArr3 = jArr111119;
                        r11 = jArr2;
                    } else {
                        i18 = i13;
                        long[] jArr1111110 = {0, 0};
                        builder2.setVibrate(jArr1111110);
                        jArr3 = jArr1111110;
                        r11 = jArr2;
                    }
                    if (AndroidUtilities.needShowPasscode()) {
                        i19 = i17;
                        jArr4 = jArr3;
                        z15 = false;
                    } else {
                        i19 = i17;
                        jArr4 = jArr3;
                        z15 = false;
                    }
                    if (!z15) {
                        Intent intent1111 = new Intent(ApplicationLoader.applicationContext, (Class<?>) PopupReplyReceiver.class);
                        intent1111.putExtra("currentAccount", r1.currentAccount);
                        builder2.addAction(R.drawable.ic_ab_reply, LocaleController.getString(R.string.Reply), PendingIntent.getBroadcast(ApplicationLoader.applicationContext, 2, intent1111, 201326592));
                    }
                    r1.showExtraNotifications(builder2, str8, j12, j11, str13, jArr4, i18, r11, i19, z14, z211, z10, i4);
                    scheduleNotificationRepeat();
                }
                z3 = zEqualsIgnoreCase;
                if (r1.dialogsNotificationsFacade.getProperty(NotificationsSettingsFacade.PROPERTY_CUSTOM, j10, j11, false)) {
                    property = r1.dialogsNotificationsFacade.getProperty("vibrate_", j10, j11, 0);
                    property2 = r1.dialogsNotificationsFacade.getProperty("priority_", j10, j11, 3);
                    property3 = r1.dialogsNotificationsFacade.getProperty("sound_document_id_", j10, j11, 0L);
                    if (property3 != j2) {
                        propertyString = r1.getMediaDataController().ringtoneDataStore.getSoundPath(property3);
                        z9 = true;
                    } else {
                        propertyString = r1.dialogsNotificationsFacade.getPropertyString("sound_path_", j10, j11, null);
                        z9 = false;
                    }
                    int property7 = r1.dialogsNotificationsFacade.getProperty("color_", j10, j11, 0);
                    j12 = j10;
                    if (property7 != 0) {
                    }
                    z10 = z7;
                    if (!messageObject.isReactionPush) {
                        j13 = sharedPreferences2.getLong("ReactionSoundDocId", 0L);
                        if (j13 != 0) {
                            string3 = r1.getMediaDataController().ringtoneDataStore.getSoundPath(j13);
                            z11 = true;
                        } else {
                            string3 = sharedPreferences2.getString("ReactionSoundPath", path);
                            z11 = false;
                        }
                        i2 = sharedPreferences2.getInt("vibrate_react", 0);
                        str5 = string3;
                        int i3111 = sharedPreferences2.getInt("priority_react", 1);
                        z12 = z11;
                        iIntValue = sharedPreferences2.getInt("ReactionsLed", -16776961);
                        if (messageObject.isStoryReactionPush) {
                            i3 = 5;
                        } else {
                            i3 = 4;
                        }
                        i4 = i3;
                        i5 = i3111;
                        i7 = i2;
                        str6 = str5;
                        i6 = 4;
                    } else {
                        j13 = sharedPreferences2.getLong("ReactionSoundDocId", 0L);
                        if (j13 != 0) {
                            string3 = r1.getMediaDataController().ringtoneDataStore.getSoundPath(j13);
                            z11 = true;
                        } else {
                            string3 = sharedPreferences2.getString("ReactionSoundPath", path);
                            z11 = false;
                        }
                        i2 = sharedPreferences2.getInt("vibrate_react", 0);
                        str5 = string3;
                        int i3112 = sharedPreferences2.getInt("priority_react", 1);
                        z12 = z11;
                        iIntValue = sharedPreferences2.getInt("ReactionsLed", -16776961);
                        if (messageObject.isStoryReactionPush) {
                            i3 = 5;
                        } else {
                            i3 = 4;
                        }
                        i4 = i3;
                        i5 = i3112;
                        i7 = i2;
                        str6 = str5;
                        i6 = 4;
                    }
                    if (i7 == i6) {
                        z13 = true;
                        i8 = 0;
                    } else {
                        i8 = i7;
                        z13 = false;
                    }
                    if (TextUtils.isEmpty(propertyString)) {
                        propertyString = str6;
                        z9 = z12;
                        z14 = true;
                    } else {
                        propertyString = str6;
                        z9 = z12;
                        z14 = true;
                    }
                    if (property2 != 3) {
                        i5 = property2;
                        z14 = false;
                    }
                    if (numValueOf != null) {
                        iIntValue = numValueOf.intValue();
                        z14 = false;
                    }
                    if (property != 0) {
                        property = i8;
                    } else {
                        property = i8;
                    }
                    if (z8) {
                        if (!sharedPreferences2.getBoolean("EnableInAppSounds", true)) {
                            propertyString = null;
                        }
                        if (sharedPreferences2.getBoolean("EnableInAppVibrate", true)) {
                            i10 = 2;
                        } else {
                            i10 = property;
                        }
                        if (sharedPreferences2.getBoolean("EnableInAppPopup", true)) {
                            i9 = 2;
                        } else {
                            i9 = 0;
                        }
                    } else {
                        i9 = i5;
                        i10 = property;
                    }
                    if (z13) {
                        ringerMode = audioManager.getRingerMode();
                        if (ringerMode != 0) {
                            i10 = 2;
                        }
                    }
                    if (z10) {
                        str7 = str4;
                        i10 = 0;
                        i9 = 0;
                        i11 = 0;
                        propertyString = null;
                    } else {
                        String str1118 = str4;
                        i11 = iIntValue;
                        str7 = str1118;
                    }
                    intent = new Intent(ApplicationLoader.applicationContext, (Class<?>) LaunchActivity.class);
                    intent.setAction("com.tmessages.openchat" + Math.random() + Integer.MAX_VALUE);
                    intent.setFlags(67108864);
                    if (messageObject.isOauthPush) {
                        intent.putExtra("oauth_url", messageObject.localName);
                    }
                    if (messageObject.isStoryReactionPush) {
                        intent.putExtra("storyId", Math.abs(messageObject.getId()));
                        i12 = i10;
                        str8 = str7;
                    } else {
                        if (messageObject.isLiveStoryPush) {
                            if (r15 != 0) {
                                i15 = i10;
                                str8 = str7;
                                intent.putExtra("chatId", j3);
                            } else {
                                i15 = i10;
                                str8 = str7;
                                if (j6 != 0) {
                                    intent.putExtra("userId", j6);
                                }
                            }
                            intent.putExtra("storyId", Math.abs(messageObject.getId()));
                            i12 = i15;
                        } else {
                            str8 = str7;
                            j14 = j6;
                            i12 = i10;
                            i13 = i11;
                            j15 = j3;
                            if (messageObject.isStoryPush) {
                                jArr = new long[r1.storyPushMessages.size()];
                                while (i14 < r1.storyPushMessages.size()) {
                                    jArr[i14] = r1.storyPushMessages.get(i14).dialogId;
                                }
                                intent.putExtra("storyDialogIds", jArr);
                                path = path;
                            } else {
                                if (!DialogObject.isEncryptedDialog(j12)) {
                                    path = path;
                                    if (r1.pushDialogs.size() == 1) {
                                        if (r15 != 0) {
                                            intent.putExtra("chatId", j15);
                                        } else if (j14 != 0) {
                                            intent.putExtra("userId", j14);
                                        }
                                    }
                                    if (AndroidUtilities.needShowPasscode()) {
                                    }
                                } else {
                                    path = path;
                                    user2 = user;
                                    chat3 = chat2;
                                    if (r1.pushDialogs.size() == 1) {
                                        intent.putExtra("encId", DialogObject.getEncryptedChatId(j12));
                                    }
                                }
                                fileLocation = null;
                            }
                            user2 = user;
                            chat3 = chat2;
                            fileLocation = null;
                        }
                        intent.putExtra("currentAccount", r1.currentAccount);
                        builder2 = builder;
                        String str1119 = propertyString;
                        builder2.setContentTitle(string2).setSmallIcon(R.drawable.notification).setAutoCancel(true).setNumber(r1.total_unread_count).setContentIntent(PendingIntent.getActivity(ApplicationLoader.applicationContext, 0, intent, 1140850688)).setGroup(r1.notificationGroup).setGroupSummary(true).setShowWhen(true).setWhen(((long) messageObject.messageOwner.date) * j);
                        if (Build.VERSION.SDK_INT < 31) {
                            builder2.setColor(r1.getNotificationColor());
                        }
                        builder2.setCategory("msg");
                        if (chat3 == null) {
                            builder2.addPerson("tel:+" + user2.phone);
                        }
                        intent2 = new Intent(ApplicationLoader.applicationContext, (Class<?>) NotificationDismissReceiver.class);
                        intent2.putExtra("messageDate", messageObject.messageOwner.date);
                        intent2.putExtra("currentAccount", r1.currentAccount);
                        if (messageObject.isStoryPush) {
                            intent2.putExtra("story", true);
                        }
                        if (messageObject.isStoryReactionPush) {
                            i20 = 1;
                            intent2.putExtra("storyReaction", true);
                        } else {
                            i20 = 1;
                        }
                        builder2.setDeleteIntent(PendingIntent.getBroadcast(ApplicationLoader.applicationContext, i20, intent2, 167772160));
                        if (bitmap2 != null) {
                            builder2.setLargeIcon(bitmap2);
                        } else {
                            if (fileLocation != null) {
                                jArr2 = null;
                                imageFromMemory = ImageLoader.getInstance().getImageFromMemory(fileLocation, null, "50_50");
                                if (imageFromMemory != null) {
                                    builder2.setLargeIcon(imageFromMemory.getBitmap());
                                } else {
                                    pathToAttach = r1.getFileLoader().getPathToAttach(fileLocation, true);
                                    if (pathToAttach.exists()) {
                                        fDp = 160.0f / AndroidUtilities.dp(50.0f);
                                        BitmapFactory.Options options15 = new BitmapFactory.Options();
                                        if (fDp < 1.0f) {
                                            i16 = 1;
                                        } else {
                                            i16 = (int) fDp;
                                        }
                                        options15.inSampleSize = i16;
                                        bitmapDecodeFile = BitmapFactory.decodeFile(pathToAttach.getAbsolutePath(), options15);
                                        if (bitmapDecodeFile != null) {
                                            builder2.setLargeIcon(bitmapDecodeFile);
                                        }
                                    }
                                }
                            }
                            if (z) {
                                builder2.setPriority(-1);
                                if (Build.VERSION.SDK_INT >= 26) {
                                    i17 = 2;
                                } else {
                                    i17 = 0;
                                }
                            } else {
                                builder2.setPriority(-1);
                                if (Build.VERSION.SDK_INT >= 26) {
                                    i17 = 2;
                                } else {
                                    i17 = 0;
                                }
                            }
                            if (r14 == 1) {
                                i18 = i13;
                                long[] jArr1111111 = {0, 0};
                                builder2.setVibrate(jArr1111111);
                                jArr3 = jArr1111111;
                                r11 = jArr2;
                            } else {
                                i18 = i13;
                                long[] jArr1111112 = {0, 0};
                                builder2.setVibrate(jArr1111112);
                                jArr3 = jArr1111112;
                                r11 = jArr2;
                            }
                            if (AndroidUtilities.needShowPasscode()) {
                                i19 = i17;
                                jArr4 = jArr3;
                                z15 = false;
                            } else {
                                i19 = i17;
                                jArr4 = jArr3;
                                z15 = false;
                            }
                            if (!z15) {
                                Intent intent1112 = new Intent(ApplicationLoader.applicationContext, (Class<?>) PopupReplyReceiver.class);
                                intent1112.putExtra("currentAccount", r1.currentAccount);
                                builder2.addAction(R.drawable.ic_ab_reply, LocaleController.getString(R.string.Reply), PendingIntent.getBroadcast(ApplicationLoader.applicationContext, 2, intent1112, 201326592));
                            }
                            r1.showExtraNotifications(builder2, str8, j12, j11, str13, jArr4, i18, r11, i19, z14, z211, z10, i4);
                            scheduleNotificationRepeat();
                        }
                        jArr2 = null;
                        if (z) {
                            builder2.setPriority(-1);
                            if (Build.VERSION.SDK_INT >= 26) {
                                i17 = 2;
                            } else {
                                i17 = 0;
                            }
                        } else {
                            builder2.setPriority(-1);
                            if (Build.VERSION.SDK_INT >= 26) {
                                i17 = 2;
                            } else {
                                i17 = 0;
                            }
                        }
                        if (r14 == 1) {
                            i18 = i13;
                            long[] jArr1111113 = {0, 0};
                            builder2.setVibrate(jArr1111113);
                            jArr3 = jArr1111113;
                            r11 = jArr2;
                        } else {
                            i18 = i13;
                            long[] jArr1111114 = {0, 0};
                            builder2.setVibrate(jArr1111114);
                            jArr3 = jArr1111114;
                            r11 = jArr2;
                        }
                        if (AndroidUtilities.needShowPasscode()) {
                            i19 = i17;
                            jArr4 = jArr3;
                            z15 = false;
                        } else {
                            i19 = i17;
                            jArr4 = jArr3;
                            z15 = false;
                        }
                        if (!z15) {
                            Intent intent1113 = new Intent(ApplicationLoader.applicationContext, (Class<?>) PopupReplyReceiver.class);
                            intent1113.putExtra("currentAccount", r1.currentAccount);
                            builder2.addAction(R.drawable.ic_ab_reply, LocaleController.getString(R.string.Reply), PendingIntent.getBroadcast(ApplicationLoader.applicationContext, 2, intent1113, 201326592));
                        }
                        r1.showExtraNotifications(builder2, str8, j12, j11, str13, jArr4, i18, r11, i19, z14, z211, z10, i4);
                        scheduleNotificationRepeat();
                    }
                    i13 = i11;
                    user2 = user;
                    chat3 = chat2;
                    fileLocation = null;
                    intent.putExtra("currentAccount", r1.currentAccount);
                    builder2 = builder;
                    String str11110 = propertyString;
                    builder2.setContentTitle(string2).setSmallIcon(R.drawable.notification).setAutoCancel(true).setNumber(r1.total_unread_count).setContentIntent(PendingIntent.getActivity(ApplicationLoader.applicationContext, 0, intent, 1140850688)).setGroup(r1.notificationGroup).setGroupSummary(true).setShowWhen(true).setWhen(((long) messageObject.messageOwner.date) * j);
                    if (Build.VERSION.SDK_INT < 31) {
                        builder2.setColor(r1.getNotificationColor());
                    }
                    builder2.setCategory("msg");
                    if (chat3 == null) {
                        builder2.addPerson("tel:+" + user2.phone);
                    }
                    intent2 = new Intent(ApplicationLoader.applicationContext, (Class<?>) NotificationDismissReceiver.class);
                    intent2.putExtra("messageDate", messageObject.messageOwner.date);
                    intent2.putExtra("currentAccount", r1.currentAccount);
                    if (messageObject.isStoryPush) {
                        intent2.putExtra("story", true);
                    }
                    if (messageObject.isStoryReactionPush) {
                        i20 = 1;
                        intent2.putExtra("storyReaction", true);
                    } else {
                        i20 = 1;
                    }
                    builder2.setDeleteIntent(PendingIntent.getBroadcast(ApplicationLoader.applicationContext, i20, intent2, 167772160));
                    if (bitmap2 != null) {
                        builder2.setLargeIcon(bitmap2);
                    } else {
                        if (fileLocation != null) {
                            jArr2 = null;
                            imageFromMemory = ImageLoader.getInstance().getImageFromMemory(fileLocation, null, "50_50");
                            if (imageFromMemory != null) {
                                builder2.setLargeIcon(imageFromMemory.getBitmap());
                            } else {
                                pathToAttach = r1.getFileLoader().getPathToAttach(fileLocation, true);
                                if (pathToAttach.exists()) {
                                    fDp = 160.0f / AndroidUtilities.dp(50.0f);
                                    BitmapFactory.Options options16 = new BitmapFactory.Options();
                                    if (fDp < 1.0f) {
                                        i16 = 1;
                                    } else {
                                        i16 = (int) fDp;
                                    }
                                    options16.inSampleSize = i16;
                                    bitmapDecodeFile = BitmapFactory.decodeFile(pathToAttach.getAbsolutePath(), options16);
                                    if (bitmapDecodeFile != null) {
                                        builder2.setLargeIcon(bitmapDecodeFile);
                                    }
                                }
                            }
                        }
                        if (z) {
                            builder2.setPriority(-1);
                            if (Build.VERSION.SDK_INT >= 26) {
                                i17 = 2;
                            } else {
                                i17 = 0;
                            }
                        } else {
                            builder2.setPriority(-1);
                            if (Build.VERSION.SDK_INT >= 26) {
                                i17 = 2;
                            } else {
                                i17 = 0;
                            }
                        }
                        if (r14 == 1) {
                            i18 = i13;
                            long[] jArr1111115 = {0, 0};
                            builder2.setVibrate(jArr1111115);
                            jArr3 = jArr1111115;
                            r11 = jArr2;
                        } else {
                            i18 = i13;
                            long[] jArr1111116 = {0, 0};
                            builder2.setVibrate(jArr1111116);
                            jArr3 = jArr1111116;
                            r11 = jArr2;
                        }
                        if (AndroidUtilities.needShowPasscode()) {
                            i19 = i17;
                            jArr4 = jArr3;
                            z15 = false;
                        } else {
                            i19 = i17;
                            jArr4 = jArr3;
                            z15 = false;
                        }
                        if (!z15) {
                            Intent intent1114 = new Intent(ApplicationLoader.applicationContext, (Class<?>) PopupReplyReceiver.class);
                            intent1114.putExtra("currentAccount", r1.currentAccount);
                            builder2.addAction(R.drawable.ic_ab_reply, LocaleController.getString(R.string.Reply), PendingIntent.getBroadcast(ApplicationLoader.applicationContext, 2, intent1114, 201326592));
                        }
                        r1.showExtraNotifications(builder2, str8, j12, j11, str13, jArr4, i18, r11, i19, z14, z211, z10, i4);
                        scheduleNotificationRepeat();
                    }
                    jArr2 = null;
                    if (z) {
                        builder2.setPriority(-1);
                        if (Build.VERSION.SDK_INT >= 26) {
                            i17 = 2;
                        } else {
                            i17 = 0;
                        }
                    } else {
                        builder2.setPriority(-1);
                        if (Build.VERSION.SDK_INT >= 26) {
                            i17 = 2;
                        } else {
                            i17 = 0;
                        }
                    }
                    if (r14 == 1) {
                        i18 = i13;
                        long[] jArr1111117 = {0, 0};
                        builder2.setVibrate(jArr1111117);
                        jArr3 = jArr1111117;
                        r11 = jArr2;
                    } else {
                        i18 = i13;
                        long[] jArr1111118 = {0, 0};
                        builder2.setVibrate(jArr1111118);
                        jArr3 = jArr1111118;
                        r11 = jArr2;
                    }
                    if (AndroidUtilities.needShowPasscode()) {
                        i19 = i17;
                        jArr4 = jArr3;
                        z15 = false;
                    } else {
                        i19 = i17;
                        jArr4 = jArr3;
                        z15 = false;
                    }
                    if (!z15) {
                        Intent intent1115 = new Intent(ApplicationLoader.applicationContext, (Class<?>) PopupReplyReceiver.class);
                        intent1115.putExtra("currentAccount", r1.currentAccount);
                        builder2.addAction(R.drawable.ic_ab_reply, LocaleController.getString(R.string.Reply), PendingIntent.getBroadcast(ApplicationLoader.applicationContext, 2, intent1115, 201326592));
                    }
                    r1.showExtraNotifications(builder2, str8, j12, j11, str13, jArr4, i18, r11, i19, z14, z211, z10, i4);
                    scheduleNotificationRepeat();
                }
                j12 = j10;
                property = 0;
                property2 = 3;
                z9 = false;
                propertyString = null;
                if (messageObject.isStoryReactionPush) {
                    intent.putExtra("storyId", Math.abs(messageObject.getId()));
                    i12 = i10;
                    str8 = str7;
                } else {
                    if (messageObject.isLiveStoryPush) {
                        if (r15 != 0) {
                            i15 = i10;
                            str8 = str7;
                            intent.putExtra("chatId", j3);
                        } else {
                            i15 = i10;
                            str8 = str7;
                            if (j6 != 0) {
                                intent.putExtra("userId", j6);
                            }
                        }
                        intent.putExtra("storyId", Math.abs(messageObject.getId()));
                        i12 = i15;
                    } else {
                        str8 = str7;
                        j14 = j6;
                        i12 = i10;
                        i13 = i11;
                        j15 = j3;
                        if (messageObject.isStoryPush) {
                            jArr = new long[r1.storyPushMessages.size()];
                            while (i14 < r1.storyPushMessages.size()) {
                                jArr[i14] = r1.storyPushMessages.get(i14).dialogId;
                            }
                            intent.putExtra("storyDialogIds", jArr);
                            path = path;
                        } else {
                            if (!DialogObject.isEncryptedDialog(j12)) {
                                path = path;
                                if (r1.pushDialogs.size() == 1) {
                                    if (r15 != 0) {
                                        intent.putExtra("chatId", j15);
                                    } else if (j14 != 0) {
                                        intent.putExtra("userId", j14);
                                    }
                                }
                                if (AndroidUtilities.needShowPasscode()) {
                                }
                            } else {
                                path = path;
                                user2 = user;
                                chat3 = chat2;
                                if (r1.pushDialogs.size() == 1) {
                                    intent.putExtra("encId", DialogObject.getEncryptedChatId(j12));
                                }
                            }
                            fileLocation = null;
                        }
                        user2 = user;
                        chat3 = chat2;
                        fileLocation = null;
                    }
                    intent.putExtra("currentAccount", r1.currentAccount);
                    builder2 = builder;
                    String str11111 = propertyString;
                    builder2.setContentTitle(string2).setSmallIcon(R.drawable.notification).setAutoCancel(true).setNumber(r1.total_unread_count).setContentIntent(PendingIntent.getActivity(ApplicationLoader.applicationContext, 0, intent, 1140850688)).setGroup(r1.notificationGroup).setGroupSummary(true).setShowWhen(true).setWhen(((long) messageObject.messageOwner.date) * j);
                    if (Build.VERSION.SDK_INT < 31) {
                        builder2.setColor(r1.getNotificationColor());
                    }
                    builder2.setCategory("msg");
                    if (chat3 == null) {
                        builder2.addPerson("tel:+" + user2.phone);
                    }
                    intent2 = new Intent(ApplicationLoader.applicationContext, (Class<?>) NotificationDismissReceiver.class);
                    intent2.putExtra("messageDate", messageObject.messageOwner.date);
                    intent2.putExtra("currentAccount", r1.currentAccount);
                    if (messageObject.isStoryPush) {
                        intent2.putExtra("story", true);
                    }
                    if (messageObject.isStoryReactionPush) {
                        i20 = 1;
                        intent2.putExtra("storyReaction", true);
                    } else {
                        i20 = 1;
                    }
                    builder2.setDeleteIntent(PendingIntent.getBroadcast(ApplicationLoader.applicationContext, i20, intent2, 167772160));
                    if (bitmap2 != null) {
                        builder2.setLargeIcon(bitmap2);
                    } else {
                        if (fileLocation != null) {
                            jArr2 = null;
                            imageFromMemory = ImageLoader.getInstance().getImageFromMemory(fileLocation, null, "50_50");
                            if (imageFromMemory != null) {
                                builder2.setLargeIcon(imageFromMemory.getBitmap());
                            } else {
                                pathToAttach = r1.getFileLoader().getPathToAttach(fileLocation, true);
                                if (pathToAttach.exists()) {
                                    fDp = 160.0f / AndroidUtilities.dp(50.0f);
                                    BitmapFactory.Options options17 = new BitmapFactory.Options();
                                    if (fDp < 1.0f) {
                                        i16 = 1;
                                    } else {
                                        i16 = (int) fDp;
                                    }
                                    options17.inSampleSize = i16;
                                    bitmapDecodeFile = BitmapFactory.decodeFile(pathToAttach.getAbsolutePath(), options17);
                                    if (bitmapDecodeFile != null) {
                                        builder2.setLargeIcon(bitmapDecodeFile);
                                    }
                                }
                            }
                        }
                        if (z) {
                            builder2.setPriority(-1);
                            if (Build.VERSION.SDK_INT >= 26) {
                                i17 = 2;
                            } else {
                                i17 = 0;
                            }
                        } else {
                            builder2.setPriority(-1);
                            if (Build.VERSION.SDK_INT >= 26) {
                                i17 = 2;
                            } else {
                                i17 = 0;
                            }
                        }
                        if (r14 == 1) {
                            i18 = i13;
                            long[] jArr1111119 = {0, 0};
                            builder2.setVibrate(jArr1111119);
                            jArr3 = jArr1111119;
                            r11 = jArr2;
                        } else {
                            i18 = i13;
                            long[] jArr11111110 = {0, 0};
                            builder2.setVibrate(jArr11111110);
                            jArr3 = jArr11111110;
                            r11 = jArr2;
                        }
                        if (AndroidUtilities.needShowPasscode()) {
                            i19 = i17;
                            jArr4 = jArr3;
                            z15 = false;
                        } else {
                            i19 = i17;
                            jArr4 = jArr3;
                            z15 = false;
                        }
                        if (!z15) {
                            Intent intent1116 = new Intent(ApplicationLoader.applicationContext, (Class<?>) PopupReplyReceiver.class);
                            intent1116.putExtra("currentAccount", r1.currentAccount);
                            builder2.addAction(R.drawable.ic_ab_reply, LocaleController.getString(R.string.Reply), PendingIntent.getBroadcast(ApplicationLoader.applicationContext, 2, intent1116, 201326592));
                        }
                        r1.showExtraNotifications(builder2, str8, j12, j11, str13, jArr4, i18, r11, i19, z14, z211, z10, i4);
                        scheduleNotificationRepeat();
                    }
                    jArr2 = null;
                    if (z) {
                        builder2.setPriority(-1);
                        if (Build.VERSION.SDK_INT >= 26) {
                            i17 = 2;
                        } else {
                            i17 = 0;
                        }
                    } else {
                        builder2.setPriority(-1);
                        if (Build.VERSION.SDK_INT >= 26) {
                            i17 = 2;
                        } else {
                            i17 = 0;
                        }
                    }
                    if (r14 == 1) {
                        i18 = i13;
                        long[] jArr11111111 = {0, 0};
                        builder2.setVibrate(jArr11111111);
                        jArr3 = jArr11111111;
                        r11 = jArr2;
                    } else {
                        i18 = i13;
                        long[] jArr11111112 = {0, 0};
                        builder2.setVibrate(jArr11111112);
                        jArr3 = jArr11111112;
                        r11 = jArr2;
                    }
                    if (AndroidUtilities.needShowPasscode()) {
                        i19 = i17;
                        jArr4 = jArr3;
                        z15 = false;
                    } else {
                        i19 = i17;
                        jArr4 = jArr3;
                        z15 = false;
                    }
                    if (!z15) {
                        Intent intent1117 = new Intent(ApplicationLoader.applicationContext, (Class<?>) PopupReplyReceiver.class);
                        intent1117.putExtra("currentAccount", r1.currentAccount);
                        builder2.addAction(R.drawable.ic_ab_reply, LocaleController.getString(R.string.Reply), PendingIntent.getBroadcast(ApplicationLoader.applicationContext, 2, intent1117, 201326592));
                    }
                    r1.showExtraNotifications(builder2, str8, j12, j11, str13, jArr4, i18, r11, i19, z14, z211, z10, i4);
                    scheduleNotificationRepeat();
                }
                intent2 = new Intent(ApplicationLoader.applicationContext, (Class<?>) NotificationDismissReceiver.class);
                intent2.putExtra("messageDate", messageObject.messageOwner.date);
                intent2.putExtra("currentAccount", r1.currentAccount);
                if (messageObject.isStoryPush) {
                    intent2.putExtra("story", true);
                }
                if (messageObject.isStoryReactionPush) {
                    i20 = 1;
                    intent2.putExtra("storyReaction", true);
                } else {
                    i20 = 1;
                }
                builder2.setDeleteIntent(PendingIntent.getBroadcast(ApplicationLoader.applicationContext, i20, intent2, 167772160));
            } catch (Throwable th) {
                FileLog.e(th);
            }
            if (!z25) {
                string = LocaleController.getString(R.string.AppName);
            } else if (r15 != 0) {
                string = LocaleController.getString(R.string.NotificationHiddenChatName);
            } else {
                string = LocaleController.getString(R.string.NotificationHiddenName);
            }
            string2 = string;
            z4 = false;
            if (messageObject.isReactionPush) {
                z5 = z4;
                if (!sharedPreferences.getBoolean("EnableReactionsPreview", true)) {
                    string2 = LocaleController.getString(R.string.NotificationHiddenName);
                }
            } else {
                z5 = z4;
                if (!sharedPreferences.getBoolean("EnableReactionsPreview", true)) {
                    string2 = LocaleController.getString(R.string.NotificationHiddenName);
                }
            }
            if (z3) {
                if (UserConfig.getActivatedAccountsCount() <= 1) {
                    firstName = charSequence4;
                } else if (r1.pushDialogs.size() == 1) {
                    firstName = UserObject.getFirstName(r1.getUserConfig().getCurrentUser());
                } else {
                    firstName = UserObject.getFirstName(r1.getUserConfig().getCurrentUser()) + "・";
                }
                chat2 = chat;
                if (r1.pushDialogs.size() == 1) {
                    j6 = jLongValue2;
                } else {
                    j6 = jLongValue2;
                    if (r1.pushDialogs.size() == 1) {
                        firstName = firstName + LocaleController.formatPluralString("NewMessages", r1.total_unread_count, new Object[0]);
                    } else {
                        firstName = firstName + LocaleController.formatString(R.string.NotificationMessagesPeopleDisplayOrder, LocaleController.formatPluralString("NewMessages", r1.total_unread_count, new Object[0]), LocaleController.formatPluralString("FromChats", r1.pushDialogs.size(), new Object[0]));
                    }
                }
            } else {
                j6 = jLongValue2;
                chat2 = chat;
                firstName = charSequence4;
            }
            builder = new NotificationCompat.Builder(ApplicationLoader.applicationContext);
            i = 1;
            if (r1.pushMessages.size() <= 1) {
                j7 = j4;
                charSequence = charSequence4;
                zArr = new boolean[i];
                stringForMessage = r1.getStringForMessage(messageObject, false, zArr, null);
                zIsSilentMessage = r1.isSilentMessage(messageObject);
                if (stringForMessage == null) {
                    return;
                }
                if (z5) {
                    strReplace = stringForMessage;
                } else if (chat2 == null) {
                    if (zArr[0]) {
                        strReplace = stringForMessage.replace(string2 + ": ", charSequence);
                    } else {
                        strReplace = stringForMessage.replace(string2 + " ", charSequence);
                    }
                } else if (zArr[0]) {
                    strReplace = stringForMessage.replace(string2 + ": ", charSequence);
                } else {
                    strReplace = stringForMessage.replace(string2 + " ", charSequence);
                }
                builder.setContentText(strReplace);
                if (z3) {
                    firstName = strReplace;
                }
                builder.setStyle(new NotificationCompat.BigTextStyle().bigText(strReplace));
                str2 = stringForMessage;
                r14 = zIsSilentMessage;
            } else if (z3) {
                i = 1;
                j7 = j4;
                charSequence = charSequence4;
                zArr = new boolean[i];
                stringForMessage = r1.getStringForMessage(messageObject, false, zArr, null);
                zIsSilentMessage = r1.isSilentMessage(messageObject);
                if (stringForMessage == null) {
                    return;
                }
                if (z5) {
                    strReplace = stringForMessage;
                } else if (chat2 == null) {
                    if (zArr[0]) {
                        strReplace = stringForMessage.replace(string2 + ": ", charSequence);
                    } else {
                        strReplace = stringForMessage.replace(string2 + " ", charSequence);
                    }
                } else if (zArr[0]) {
                    strReplace = stringForMessage.replace(string2 + ": ", charSequence);
                } else {
                    strReplace = stringForMessage.replace(string2 + " ", charSequence);
                }
                builder.setContentText(strReplace);
                if (z3) {
                    firstName = strReplace;
                }
                builder.setStyle(new NotificationCompat.BigTextStyle().bigText(strReplace));
                str2 = stringForMessage;
                r14 = zIsSilentMessage;
            } else {
                builder.setContentText(firstName);
                NotificationCompat.InboxStyle inboxStyle3 = new NotificationCompat.InboxStyle();
                inboxStyle3.setBigContentTitle(string2);
                iMin = Math.min(10, r1.pushMessages.size());
                zArr2 = new boolean[1];
                i22 = 0;
                IsSilentMessage = 2;
                String str121 = null;
                while (i22 < iMin) {
                    int i211 = iMin;
                    MessageObject messageObject6 = r1.pushMessages.get(i22);
                    long j29 = j4;
                    int i3113 = i22;
                    stringForMessage2 = r1.getStringForMessage(messageObject6, false, zArr2, null);
                    if (stringForMessage2 != null) {
                        charSequence3 = charSequence4;
                    } else {
                        charSequence3 = charSequence4;
                    }
                    charSequence4 = charSequence3;
                    iMin = i211;
                    i22 = i3113 + 1;
                    j4 = j29;
                    IsSilentMessage = IsSilentMessage;
                }
                j7 = j4;
                charSequence = charSequence4;
                inboxStyle3.setSummaryText(firstName);
                builder.setStyle(inboxStyle3);
                str2 = str121;
                r14 = IsSilentMessage;
            }
            str3 = firstName;
            if (z) {
                z6 = true;
            } else {
                z6 = true;
            }
            if (z6) {
                str4 = str3;
                charSequence2 = charSequence;
                sharedPreferences2 = sharedPreferences;
                j8 = j7;
                z7 = z6;
            } else {
                str4 = str3;
                charSequence2 = charSequence;
                sharedPreferences2 = sharedPreferences;
                j8 = j7;
                z7 = z6;
            }
            if (z7) {
                if (!sharedPreferences2.getBoolean("sound_enabled_" + getSharedPrefKey(j8, j9), true)) {
                    j9 = j5;
                    z7 = true;
                }
            } else {
                j9 = j5;
            }
            j9 = j5;
            path = Settings.System.DEFAULT_NOTIFICATION_URI.getPath();
            z8 = ApplicationLoader.mainInterfacePaused;
            boolean z212 = !z8;
            getSharedPrefKey(j8, j9);
            j10 = j8;
            j11 = j9;
            z10 = z7;
            if (!messageObject.isReactionPush) {
                j13 = sharedPreferences2.getLong("ReactionSoundDocId", 0L);
                if (j13 != 0) {
                    string3 = r1.getMediaDataController().ringtoneDataStore.getSoundPath(j13);
                    z11 = true;
                } else {
                    string3 = sharedPreferences2.getString("ReactionSoundPath", path);
                    z11 = false;
                }
                i2 = sharedPreferences2.getInt("vibrate_react", 0);
                str5 = string3;
                int i3114 = sharedPreferences2.getInt("priority_react", 1);
                z12 = z11;
                iIntValue = sharedPreferences2.getInt("ReactionsLed", -16776961);
                if (messageObject.isStoryReactionPush) {
                    i3 = 5;
                } else {
                    i3 = 4;
                }
                i4 = i3;
                i5 = i3114;
                i7 = i2;
                str6 = str5;
                i6 = 4;
            } else {
                j13 = sharedPreferences2.getLong("ReactionSoundDocId", 0L);
                if (j13 != 0) {
                    string3 = r1.getMediaDataController().ringtoneDataStore.getSoundPath(j13);
                    z11 = true;
                } else {
                    string3 = sharedPreferences2.getString("ReactionSoundPath", path);
                    z11 = false;
                }
                i2 = sharedPreferences2.getInt("vibrate_react", 0);
                str5 = string3;
                int i3115 = sharedPreferences2.getInt("priority_react", 1);
                z12 = z11;
                iIntValue = sharedPreferences2.getInt("ReactionsLed", -16776961);
                if (messageObject.isStoryReactionPush) {
                    i3 = 5;
                } else {
                    i3 = 4;
                }
                i4 = i3;
                i5 = i3115;
                i7 = i2;
                str6 = str5;
                i6 = 4;
            }
            if (i7 == i6) {
                z13 = true;
                i8 = 0;
            } else {
                i8 = i7;
                z13 = false;
            }
            if (TextUtils.isEmpty(propertyString)) {
                propertyString = str6;
                z9 = z12;
                z14 = true;
            } else {
                propertyString = str6;
                z9 = z12;
                z14 = true;
            }
            if (property2 != 3) {
                i5 = property2;
                z14 = false;
            }
            if (numValueOf != null) {
                iIntValue = numValueOf.intValue();
                z14 = false;
            }
            if (property != 0) {
                property = i8;
            } else {
                property = i8;
            }
            if (z8) {
                if (!sharedPreferences2.getBoolean("EnableInAppSounds", true)) {
                    propertyString = null;
                }
                if (sharedPreferences2.getBoolean("EnableInAppVibrate", true)) {
                    i10 = 2;
                } else {
                    i10 = property;
                }
                if (sharedPreferences2.getBoolean("EnableInAppPopup", true)) {
                    i9 = 2;
                } else {
                    i9 = 0;
                }
            } else {
                i9 = i5;
                i10 = property;
            }
            if (z13) {
                ringerMode = audioManager.getRingerMode();
                if (ringerMode != 0) {
                    i10 = 2;
                }
            }
            if (z10) {
                str7 = str4;
                i10 = 0;
                i9 = 0;
                i11 = 0;
                propertyString = null;
            } else {
                String str11112 = str4;
                i11 = iIntValue;
                str7 = str11112;
            }
            intent = new Intent(ApplicationLoader.applicationContext, (Class<?>) LaunchActivity.class);
            intent.setAction("com.tmessages.openchat" + Math.random() + Integer.MAX_VALUE);
            intent.setFlags(67108864);
            if (messageObject.isOauthPush) {
                intent.putExtra("oauth_url", messageObject.localName);
            }
            i13 = i11;
            user2 = user;
            chat3 = chat2;
            fileLocation = null;
            intent.putExtra("currentAccount", r1.currentAccount);
            builder2 = builder;
            String str11113 = propertyString;
            builder2.setContentTitle(string2).setSmallIcon(R.drawable.notification).setAutoCancel(true).setNumber(r1.total_unread_count).setContentIntent(PendingIntent.getActivity(ApplicationLoader.applicationContext, 0, intent, 1140850688)).setGroup(r1.notificationGroup).setGroupSummary(true).setShowWhen(true).setWhen(((long) messageObject.messageOwner.date) * j);
            if (Build.VERSION.SDK_INT < 31) {
                builder2.setColor(r1.getNotificationColor());
            }
            builder2.setCategory("msg");
            if (chat3 == null) {
                builder2.addPerson("tel:+" + user2.phone);
            }
            if (bitmap2 != null) {
                builder2.setLargeIcon(bitmap2);
            } else {
                if (fileLocation != null) {
                    jArr2 = null;
                    imageFromMemory = ImageLoader.getInstance().getImageFromMemory(fileLocation, null, "50_50");
                    if (imageFromMemory != null) {
                        builder2.setLargeIcon(imageFromMemory.getBitmap());
                    } else {
                        pathToAttach = r1.getFileLoader().getPathToAttach(fileLocation, true);
                        if (pathToAttach.exists()) {
                            fDp = 160.0f / AndroidUtilities.dp(50.0f);
                            BitmapFactory.Options options18 = new BitmapFactory.Options();
                            if (fDp < 1.0f) {
                                i16 = 1;
                            } else {
                                i16 = (int) fDp;
                            }
                            options18.inSampleSize = i16;
                            bitmapDecodeFile = BitmapFactory.decodeFile(pathToAttach.getAbsolutePath(), options18);
                            if (bitmapDecodeFile != null) {
                                builder2.setLargeIcon(bitmapDecodeFile);
                            }
                        }
                    }
                }
                if (z) {
                    builder2.setPriority(-1);
                    if (Build.VERSION.SDK_INT >= 26) {
                        i17 = 2;
                    } else {
                        i17 = 0;
                    }
                } else {
                    builder2.setPriority(-1);
                    if (Build.VERSION.SDK_INT >= 26) {
                        i17 = 2;
                    } else {
                        i17 = 0;
                    }
                }
                if (r14 == 1) {
                    i18 = i13;
                    long[] jArr11111113 = {0, 0};
                    builder2.setVibrate(jArr11111113);
                    jArr3 = jArr11111113;
                    r11 = jArr2;
                } else {
                    i18 = i13;
                    long[] jArr11111114 = {0, 0};
                    builder2.setVibrate(jArr11111114);
                    jArr3 = jArr11111114;
                    r11 = jArr2;
                }
                if (AndroidUtilities.needShowPasscode()) {
                    i19 = i17;
                    jArr4 = jArr3;
                    z15 = false;
                } else {
                    i19 = i17;
                    jArr4 = jArr3;
                    z15 = false;
                }
                if (!z15) {
                    Intent intent1118 = new Intent(ApplicationLoader.applicationContext, (Class<?>) PopupReplyReceiver.class);
                    intent1118.putExtra("currentAccount", r1.currentAccount);
                    builder2.addAction(R.drawable.ic_ab_reply, LocaleController.getString(R.string.Reply), PendingIntent.getBroadcast(ApplicationLoader.applicationContext, 2, intent1118, 201326592));
                }
                r1.showExtraNotifications(builder2, str8, j12, j11, str13, jArr4, i18, r11, i19, z14, z212, z10, i4);
                scheduleNotificationRepeat();
            }
            jArr2 = null;
            if (z) {
                builder2.setPriority(-1);
                if (Build.VERSION.SDK_INT >= 26) {
                    i17 = 2;
                } else {
                    i17 = 0;
                }
            } else {
                builder2.setPriority(-1);
                if (Build.VERSION.SDK_INT >= 26) {
                    i17 = 2;
                } else {
                    i17 = 0;
                }
            }
            if (r14 == 1) {
                i18 = i13;
                long[] jArr11111115 = {0, 0};
                builder2.setVibrate(jArr11111115);
                jArr3 = jArr11111115;
                r11 = jArr2;
            } else {
                i18 = i13;
                long[] jArr11111116 = {0, 0};
                builder2.setVibrate(jArr11111116);
                jArr3 = jArr11111116;
                r11 = jArr2;
            }
            if (AndroidUtilities.needShowPasscode()) {
                i19 = i17;
                jArr4 = jArr3;
                z15 = false;
            } else {
                i19 = i17;
                jArr4 = jArr3;
                z15 = false;
            }
            if (!z15) {
                Intent intent1119 = new Intent(ApplicationLoader.applicationContext, (Class<?>) PopupReplyReceiver.class);
                intent1119.putExtra("currentAccount", r1.currentAccount);
                builder2.addAction(R.drawable.ic_ab_reply, LocaleController.getString(R.string.Reply), PendingIntent.getBroadcast(ApplicationLoader.applicationContext, 2, intent1119, 201326592));
            }
            r1.showExtraNotifications(builder2, str8, j12, j11, str13, jArr4, i18, r11, i19, z14, z212, z10, i4);
            scheduleNotificationRepeat();
        } catch (Exception e3) {
            e = e3;
            FileLog.e(e);
        }
    }

    private int getNotificationColor() {
        return AppUtils.getNotificationColor();
    }

    private boolean isSilentMessage(MessageObject messageObject) {
        return messageObject.messageOwner.silent || messageObject.isReactionPush;
    }

    @SuppressLint({"NewApi"})
    private void setNotificationChannel(Notification notification, NotificationCompat.Builder builder, boolean z) {
        if (z) {
            builder.setChannelId(OTHER_NOTIFICATIONS_CHANNEL);
        } else {
            builder.setChannelId(notification.getChannelId());
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void resetNotificationSound(NotificationCompat.Builder builder, long j, long j2, String str, long[] jArr, int i, Uri uri, int i2, boolean z, boolean z2, boolean z3, int i3) {
        FileLog.d("resetNotificationSound");
        Uri uri2 = Settings.System.DEFAULT_RINGTONE_URI;
        if (uri2 == null || uri == null || TextUtils.equals(uri2.toString(), uri.toString())) {
            return;
        }
        SharedPreferences.Editor editorEdit = getAccountInstance().getNotificationsSettings().edit();
        String string = uri2.toString();
        String string2 = LocaleController.getString(R.string.DefaultRingtone);
        if (z) {
            if (i3 == 2) {
                editorEdit.putString("ChannelSound", string2);
            } else if (i3 == 0) {
                editorEdit.putString("GroupSound", string2);
            } else if (i3 == 1) {
                editorEdit.putString("GlobalSound", string2);
            } else if (i3 == 3) {
                editorEdit.putString("StoriesSound", string2);
            } else if (i3 == 4 || i3 == 5) {
                editorEdit.putString("ReactionSound", string2);
            }
            if (i3 == 2) {
                editorEdit.putString("ChannelSoundPath", string);
            } else if (i3 == 0) {
                editorEdit.putString("GroupSoundPath", string);
            } else if (i3 == 1) {
                editorEdit.putString("GlobalSoundPath", string);
            } else if (i3 == 3) {
                editorEdit.putString("StoriesSoundPath", string);
            } else if (i3 == 4 || i3 == 5) {
                editorEdit.putString("ReactionSound", string);
            }
            getNotificationsController().lambda$deleteNotificationChannelGlobal$41(i3, -1);
        } else {
            editorEdit.putString("sound_" + getSharedPrefKey(j, j2), string2);
            editorEdit.putString("sound_path_" + getSharedPrefKey(j, j2), string);
            lambda$deleteNotificationChannel$40(j, j2, -1);
        }
        editorEdit.apply();
        builder.setChannelId(validateChannelId(j, j2, str, jArr, i, uri2, i2, z, z2, z3, i3));
        notificationManager.notify(this.notificationId, builder.build());
    }

    /*  JADX ERROR: Type inference failed
        jadx.core.utils.exceptions.JadxOverflowException: Type inference error: updates count limit reached with updateSeq = 57761. Try increasing type updates limit count.
        	at jadx.core.utils.ErrorsCounter.addError(ErrorsCounter.java:59)
        	at jadx.core.utils.ErrorsCounter.error(ErrorsCounter.java:31)
        	at jadx.core.dex.attributes.nodes.NotificationAttrNode.addError(NotificationAttrNode.java:19)
        	at jadx.core.dex.visitors.typeinference.TypeInferenceVisitor.visit(TypeInferenceVisitor.java:79)
        */
    @android.annotation.SuppressLint({"InlinedApi"})
    private void showExtraNotifications(androidx.core.app.NotificationCompat.Builder r81, java.lang.String r82, long r83, long r85, java.lang.String r87, long[] r88, int r89, android.net.Uri r90, int r91, boolean r92, boolean r93, boolean r94, int r95) {
        /*
            Method dump skipped, instruction units count: 5776
            To view this dump add '--comments-level debug' option
        */
        throw new UnsupportedOperationException("Method not decompiled: org.telegram.messenger.NotificationsController.showExtraNotifications(androidx.core.app.NotificationCompat$Builder, java.lang.String, long, long, java.lang.String, long[], int, android.net.Uri, int, boolean, boolean, boolean, int):void");
    }

    /* JADX INFO: renamed from: org.telegram.messenger.NotificationsController$1NotificationHolder, reason: invalid class name */
    class C1NotificationHolder {
        TLRPC.Chat chat;
        long dialogId;
        int id;
        String name;
        NotificationCompat.Builder notification;
        boolean story;
        long topicId;
        TLRPC.User user;
        final /* synthetic */ String val$chatName;
        final /* synthetic */ int val$chatType;
        final /* synthetic */ int val$importance;
        final /* synthetic */ boolean val$isDefault;
        final /* synthetic */ boolean val$isInApp;
        final /* synthetic */ boolean val$isSilent;
        final /* synthetic */ long val$lastTopicId;
        final /* synthetic */ int val$ledColor;
        final /* synthetic */ Uri val$sound;
        final /* synthetic */ long[] val$vibrationPattern;

        C1NotificationHolder(int i, long j, boolean z, long j2, String str, TLRPC.User user, TLRPC.Chat chat, NotificationCompat.Builder builder, long j3, String str2, long[] jArr, int i2, Uri uri, int i3, boolean z2, boolean z3, boolean z4, int i4) {
            this.val$lastTopicId = j3;
            this.val$chatName = str2;
            this.val$vibrationPattern = jArr;
            this.val$ledColor = i2;
            this.val$sound = uri;
            this.val$importance = i3;
            this.val$isDefault = z2;
            this.val$isInApp = z3;
            this.val$isSilent = z4;
            this.val$chatType = i4;
            this.id = i;
            this.name = str;
            this.user = user;
            this.chat = chat;
            this.notification = builder;
            this.dialogId = j;
            this.story = z;
            this.topicId = j2;
        }

        void call() {
            if (BuildVars.LOGS_ENABLED) {
                FileLog.w("show dialog notification with id " + this.id + " " + this.dialogId + " user=" + this.user + " chat=" + this.chat);
            }
            try {
                NotificationsController.notificationManager.notify(this.id, this.notification.build());
            } catch (SecurityException e) {
                FileLog.e(e);
                NotificationsController.this.resetNotificationSound(this.notification, this.dialogId, this.val$lastTopicId, this.val$chatName, this.val$vibrationPattern, this.val$ledColor, this.val$sound, this.val$importance, this.val$isDefault, this.val$isInApp, this.val$isSilent, this.val$chatType);
            }
        }
    }

    /* JADX INFO: renamed from: $r8$lambda$m6od9kHW-9uO1euU4_-KKWj-l1k, reason: not valid java name */
    public static /* synthetic */ void m3652$r8$lambda$m6od9kHW9uO1euU4_KKWjl1k(Uri uri, File file) {
        try {
            ApplicationLoader.applicationContext.revokeUriPermission(uri, 1);
        } catch (Exception e) {
            FileLog.e(e);
        }
        if (file != null) {
            try {
                file.delete();
            } catch (Exception e2) {
                FileLog.e(e2);
            }
        }
    }

    private String cutLastName(String str) {
        if (str == null) {
            return null;
        }
        int iIndexOf = str.indexOf(32);
        if (iIndexOf < 0) {
            return str;
        }
        StringBuilder sb = new StringBuilder();
        sb.append(str.substring(0, iIndexOf));
        sb.append(str.endsWith("…") ? "…" : _UrlKt.FRAGMENT_ENCODE_SET);
        return sb.toString();
    }

    private Pair<Integer, Boolean> parseStoryPushes(ArrayList<String> arrayList, ArrayList<Object> arrayList2) {
        String userName;
        TLRPC.FileLocation fileLocation;
        int iMin = Math.min(3, this.storyPushMessages.size());
        boolean z = false;
        int size = 0;
        for (int i = 0; i < iMin; i++) {
            StoryNotification storyNotification = this.storyPushMessages.get(i);
            size += storyNotification.dateByIds.size();
            z |= storyNotification.hidden;
            TLRPC.User user = getMessagesController().getUser(Long.valueOf(storyNotification.dialogId));
            if (user == null && (user = getMessagesStorage().getUserSync(storyNotification.dialogId)) != null) {
                getMessagesController().putUser(user, true);
            }
            Object obj = null;
            if (user != null) {
                userName = UserObject.getUserName(user);
                TLRPC.UserProfilePhoto userProfilePhoto = user.photo;
                if (userProfilePhoto != null && (fileLocation = userProfilePhoto.photo_small) != null && fileLocation.volume_id != 0 && fileLocation.local_id != 0) {
                    File pathToAttach = getFileLoader().getPathToAttach(user.photo.photo_small, true);
                    if (!pathToAttach.exists()) {
                        pathToAttach = user.photo.photo_big != null ? getFileLoader().getPathToAttach(user.photo.photo_big, true) : null;
                        if (pathToAttach != null && !pathToAttach.exists()) {
                            pathToAttach = null;
                        }
                    }
                    if (pathToAttach != null) {
                        obj = pathToAttach;
                    }
                }
            } else {
                userName = storyNotification.localName;
                if (userName != null) {
                }
            }
            if (userName.length() > 50) {
                userName = userName.substring(0, 25) + "…";
            }
            arrayList.add(userName);
            if (obj == null && user != null) {
                arrayList2.add(user);
            } else if (obj != null) {
                arrayList2.add(obj);
            }
        }
        if (z) {
            arrayList2.clear();
        }
        return new Pair<>(Integer.valueOf(size), Boolean.valueOf(z));
    }

    public static Person.Builder loadRoundAvatar(long j, File file, Person.Builder builder) {
        if (j == UserObject.OAUTH) {
            builder.setIcon(IconCompat.createWithResource(ApplicationLoader.applicationContext, R.drawable.ic_launcher_dr));
            return builder;
        }
        if (file != null && Build.VERSION.SDK_INT >= 28) {
            try {
                builder.setIcon(IconCompat.createWithBitmap(ImageDecoder.decodeBitmap(ImageDecoder.createSource(file), new ImageDecoder.OnHeaderDecodedListener() { // from class: org.telegram.messenger.NotificationsController$$ExternalSyntheticLambda23
                    @Override // android.graphics.ImageDecoder.OnHeaderDecodedListener
                    public final void onHeaderDecoded(ImageDecoder imageDecoder, ImageDecoder.ImageInfo imageInfo, ImageDecoder.Source source) {
                        imageDecoder.setPostProcessor(new PostProcessor() { // from class: org.telegram.messenger.NotificationsController$$ExternalSyntheticLambda19
                            @Override // android.graphics.PostProcessor
                            public final int onPostProcess(Canvas canvas) {
                                return NotificationsController.$r8$lambda$CnmFC0bhiPjnt6BqvCrfOMLJsRA(canvas);
                            }
                        });
                    }
                })));
            } catch (Throwable unused) {
            }
        }
        return builder;
    }

    public static /* synthetic */ int $r8$lambda$CnmFC0bhiPjnt6BqvCrfOMLJsRA(Canvas canvas) {
        Path path = new Path();
        path.setFillType(Path.FillType.INVERSE_EVEN_ODD);
        int width = canvas.getWidth();
        float f = width / 2;
        path.addRoundRect(0.0f, 0.0f, width, canvas.getHeight(), f, f, Path.Direction.CW);
        Paint paint = new Paint();
        paint.setAntiAlias(true);
        paint.setColor(0);
        paint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.SRC));
        canvas.drawPath(path, paint);
        return -3;
    }

    public static Bitmap loadMultipleAvatars(ArrayList<Object> arrayList) {
        float f;
        int i;
        Bitmap bitmap;
        Paint paint;
        boolean z;
        float f2;
        char c;
        ArrayList<Object> arrayList2 = arrayList;
        if (Build.VERSION.SDK_INT < 28 || arrayList2 == null || arrayList2.size() == 0) {
            return null;
        }
        int iDp = AndroidUtilities.dp(64.0f);
        Bitmap bitmapCreateBitmap = Bitmap.createBitmap(iDp, iDp, Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(bitmapCreateBitmap);
        Matrix matrix = new Matrix();
        Paint paint2 = new Paint(3);
        boolean z2 = true;
        Paint paint3 = new Paint(1);
        Rect rect = new Rect();
        paint3.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.CLEAR));
        char c2 = 2;
        if (arrayList2.size() == 1) {
            f = 1.0f;
        } else {
            f = arrayList2.size() == 2 ? 0.65f : 0.5f;
        }
        int i2 = 0;
        TextPaint textPaint = null;
        while (i2 < arrayList2.size()) {
            float f3 = iDp;
            float f4 = (1.0f - f) * f3;
            try {
                float size = (f4 / arrayList2.size()) * ((arrayList2.size() - 1) - i2);
                try {
                    float size2 = i2 * (f4 / arrayList2.size());
                    float f5 = f3 * f;
                    float f6 = f5 / 2.0f;
                    i = iDp;
                    float f7 = size + f6;
                    bitmap = bitmapCreateBitmap;
                    float f8 = size2 + f6;
                    f2 = f;
                    try {
                        canvas.drawCircle(f7, f8, AndroidUtilities.dp(2.0f) + f6, paint3);
                        Object obj = arrayList2.get(i2);
                        paint = paint3;
                        try {
                            if (obj instanceof File) {
                                String absolutePath = ((File) arrayList2.get(i2)).getAbsolutePath();
                                BitmapFactory.Options options = new BitmapFactory.Options();
                                z = true;
                                try {
                                    options.inJustDecodeBounds = true;
                                    BitmapFactory.decodeFile(absolutePath, options);
                                    int i3 = (int) f5;
                                    options.inSampleSize = StoryEntry.calculateInSampleSize(options, i3, i3);
                                    try {
                                        options.inJustDecodeBounds = false;
                                        z = true;
                                        options.inDither = true;
                                        Bitmap bitmapDecodeFile = BitmapFactory.decodeFile(absolutePath, options);
                                        Shader.TileMode tileMode = Shader.TileMode.CLAMP;
                                        BitmapShader bitmapShader = new BitmapShader(bitmapDecodeFile, tileMode, tileMode);
                                        matrix.reset();
                                        matrix.postScale(f5 / bitmapDecodeFile.getWidth(), f5 / bitmapDecodeFile.getHeight());
                                        matrix.postTranslate(size, size2);
                                        bitmapShader.setLocalMatrix(matrix);
                                        paint2.setShader(bitmapShader);
                                        canvas.drawCircle(f7, f8, f6, paint2);
                                        bitmapDecodeFile.recycle();
                                    } catch (Throwable unused) {
                                        c = 2;
                                    }
                                } catch (Throwable unused2) {
                                    c = 2;
                                }
                            } else {
                                if (obj instanceof TLRPC.User) {
                                    TLRPC.User user = (TLRPC.User) obj;
                                    c = 2;
                                    try {
                                        paint2.setShader(new LinearGradient(size, size2, size, size2 + f5, new int[]{Theme.getColor(Theme.keys_avatar_background[AvatarDrawable.getColorIndex(user.id)]), Theme.getColor(Theme.keys_avatar_background2[AvatarDrawable.getColorIndex(user.id)])}, new float[]{0.0f, 1.0f}, Shader.TileMode.CLAMP));
                                        canvas.drawCircle(f7, f8, f6, paint2);
                                        if (textPaint == null) {
                                            try {
                                                z = true;
                                                try {
                                                    TextPaint textPaint2 = new TextPaint(1);
                                                    try {
                                                        textPaint2.setTypeface(AndroidUtilities.bold());
                                                        textPaint2.setTextSize(f3 * 0.25f);
                                                        textPaint2.setColor(-1);
                                                        textPaint = textPaint2;
                                                    } catch (Throwable unused3) {
                                                        textPaint = textPaint2;
                                                    }
                                                } catch (Throwable unused4) {
                                                }
                                            } catch (Throwable unused5) {
                                                z = true;
                                            }
                                        } else {
                                            z = true;
                                        }
                                        StringBuilder sb = new StringBuilder();
                                        AvatarDrawable.getAvatarSymbols(user.first_name, user.last_name, null, sb);
                                        String string = sb.toString();
                                        try {
                                            textPaint.getTextBounds(string, 0, string.length(), rect);
                                            canvas.drawText(string, (f7 - (rect.width() / 2.0f)) - rect.left, (f8 - (rect.height() / 2.0f)) - rect.top, textPaint);
                                        } catch (Throwable unused6) {
                                        }
                                    } catch (Throwable unused7) {
                                        z = true;
                                    }
                                    i2++;
                                    c2 = c;
                                    z2 = z;
                                    iDp = i;
                                    bitmapCreateBitmap = bitmap;
                                    f = f2;
                                    paint3 = paint;
                                    arrayList2 = arrayList;
                                }
                                z = true;
                                i2++;
                                c2 = c;
                                z2 = z;
                                iDp = i;
                                bitmapCreateBitmap = bitmap;
                                f = f2;
                                paint3 = paint;
                                arrayList2 = arrayList;
                            }
                        } catch (Throwable unused8) {
                        }
                    } catch (Throwable unused9) {
                        paint = paint3;
                    }
                    c = 2;
                    z = true;
                } catch (Throwable unused10) {
                    i = iDp;
                    bitmap = bitmapCreateBitmap;
                    paint = paint3;
                    z = z2;
                    f2 = f;
                }
            } catch (Throwable unused11) {
                i = iDp;
                bitmap = bitmapCreateBitmap;
                paint = paint3;
                z = z2;
                f2 = f;
                c = c2;
            }
            i2++;
            c2 = c;
            z2 = z;
            iDp = i;
            bitmapCreateBitmap = bitmap;
            f = f2;
            paint3 = paint;
            arrayList2 = arrayList;
        }
        return bitmapCreateBitmap;
    }

    public void playOutChatSound() {
        if (!this.inChatSoundEnabled || MediaController.getInstance().isRecordingAudio()) {
            return;
        }
        try {
            if (audioManager.getRingerMode() == 0) {
                return;
            }
        } catch (Exception e) {
            FileLog.e(e);
        }
        notificationsQueue.postRunnable(new Runnable() { // from class: org.telegram.messenger.NotificationsController$$ExternalSyntheticLambda43
            @Override // java.lang.Runnable
            public final void run() {
                this.f$0.lambda$playOutChatSound$47();
            }
        });
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$playOutChatSound$47() {
        try {
            if (Math.abs(SystemClock.elapsedRealtime() - this.lastSoundOutPlay) <= 100) {
                return;
            }
            this.lastSoundOutPlay = SystemClock.elapsedRealtime();
            if (this.soundPool == null) {
                SoundPool soundPool = new SoundPool(3, 1, 0);
                this.soundPool = soundPool;
                soundPool.setOnLoadCompleteListener(new SoundPool.OnLoadCompleteListener() { // from class: org.telegram.messenger.NotificationsController$$ExternalSyntheticLambda36
                    @Override // android.media.SoundPool.OnLoadCompleteListener
                    public final void onLoadComplete(SoundPool soundPool2, int i, int i2) {
                        NotificationsController.m3651$r8$lambda$gKQNTsOhV2njT98D4ijWx9ye7E(soundPool2, i, i2);
                    }
                });
            }
            if (this.soundOut == 0 && !this.soundOutLoaded) {
                this.soundOutLoaded = true;
                this.soundOut = this.soundPool.load(ApplicationLoader.applicationContext, R.raw.sound_out, 1);
            }
            int i = this.soundOut;
            if (i != 0) {
                try {
                    this.soundPool.play(i, 1.0f, 1.0f, 1, 0, 1.0f);
                } catch (Exception e) {
                    FileLog.e(e);
                }
            }
        } catch (Exception e2) {
            FileLog.e(e2);
        }
    }

    /* JADX INFO: renamed from: $r8$lambda$gKQNTsOhV2njT98D4ijW-x9ye7E, reason: not valid java name */
    public static /* synthetic */ void m3651$r8$lambda$gKQNTsOhV2njT98D4ijWx9ye7E(SoundPool soundPool, int i, int i2) {
        if (i2 == 0) {
            try {
                soundPool.play(i, 1.0f, 1.0f, 1, 0, 1.0f);
            } catch (Exception e) {
                FileLog.e(e);
            }
        }
    }

    public void clearDialogNotificationsSettings(long j, long j2) {
        SharedPreferences.Editor editorEdit = getAccountInstance().getNotificationsSettings().edit();
        String sharedPrefKey = getSharedPrefKey(j, j2);
        editorEdit.remove(NotificationsSettingsFacade.PROPERTY_NOTIFY + sharedPrefKey).remove(NotificationsSettingsFacade.PROPERTY_CUSTOM + sharedPrefKey);
        getMessagesStorage().setDialogFlags(j, 0L);
        TLRPC.Dialog dialog = (TLRPC.Dialog) getMessagesController().dialogs_dict.get(j);
        if (dialog != null) {
            dialog.notify_settings = new TLRPC.TL_peerNotifySettings();
        }
        editorEdit.apply();
        getNotificationsController().updateServerNotificationsSettings(j, j2, true);
    }

    public void setDialogNotificationsSettings(long j, long j2, int i) {
        SharedPreferences.Editor editorEdit = getAccountInstance().getNotificationsSettings().edit();
        TLRPC.Dialog dialog = (TLRPC.Dialog) MessagesController.getInstance(UserConfig.selectedAccount).dialogs_dict.get(j);
        if (i == 4) {
            if (isGlobalNotificationsEnabled(j, false, false)) {
                editorEdit.remove(NotificationsSettingsFacade.PROPERTY_NOTIFY + getSharedPrefKey(j, j2));
            } else {
                editorEdit.putInt(NotificationsSettingsFacade.PROPERTY_NOTIFY + getSharedPrefKey(j, j2), 0);
            }
            getMessagesStorage().setDialogFlags(j, 0L);
            if (dialog != null) {
                dialog.notify_settings = new TLRPC.TL_peerNotifySettings();
            }
        } else {
            int currentTime = ConnectionsManager.getInstance(UserConfig.selectedAccount).getCurrentTime();
            if (i == 0) {
                currentTime += 3600;
            } else if (i == 1) {
                currentTime += 28800;
            } else if (i == 2) {
                currentTime += 172800;
            } else if (i == 3) {
                currentTime = Integer.MAX_VALUE;
            }
            long j3 = 1;
            if (i == 3) {
                editorEdit.putInt(NotificationsSettingsFacade.PROPERTY_NOTIFY + getSharedPrefKey(j, j2), 2);
            } else {
                editorEdit.putInt(NotificationsSettingsFacade.PROPERTY_NOTIFY + getSharedPrefKey(j, j2), 3);
                editorEdit.putInt(NotificationsSettingsFacade.PROPERTY_NOTIFY_UNTIL + getSharedPrefKey(j, j2), currentTime);
                j3 = 1 | (((long) currentTime) << 32);
            }
            getInstance(UserConfig.selectedAccount).removeNotificationsForDialog(j);
            MessagesStorage.getInstance(UserConfig.selectedAccount).setDialogFlags(j, j3);
            if (dialog != null) {
                TLRPC.TL_peerNotifySettings tL_peerNotifySettings = new TLRPC.TL_peerNotifySettings();
                dialog.notify_settings = tL_peerNotifySettings;
                tL_peerNotifySettings.mute_until = currentTime;
            }
        }
        editorEdit.apply();
        updateServerNotificationsSettings(j, j2);
    }

    public void updateServerNotificationsSettings(long j, long j2) {
        updateServerNotificationsSettings(j, j2, true);
    }

    public void updateServerNotificationsSettings(long j, long j2, boolean z) {
        if (z) {
            getNotificationCenter().lambda$postNotificationNameOnUIThread$1(NotificationCenter.notificationsSettingsUpdated, new Object[0]);
        }
        if (DialogObject.isEncryptedDialog(j)) {
            return;
        }
        SharedPreferences notificationsSettings = getAccountInstance().getNotificationsSettings();
        TL_account.updateNotifySettings updatenotifysettings = new TL_account.updateNotifySettings();
        updatenotifysettings.settings = new TLRPC.TL_inputPeerNotifySettings();
        String sharedPrefKey = getSharedPrefKey(j, j2);
        TLRPC.TL_inputPeerNotifySettings tL_inputPeerNotifySettings = updatenotifysettings.settings;
        tL_inputPeerNotifySettings.flags |= 1;
        tL_inputPeerNotifySettings.show_previews = notificationsSettings.getBoolean(NotificationsSettingsFacade.PROPERTY_CONTENT_PREVIEW + sharedPrefKey, true);
        TLRPC.TL_inputPeerNotifySettings tL_inputPeerNotifySettings2 = updatenotifysettings.settings;
        tL_inputPeerNotifySettings2.flags = tL_inputPeerNotifySettings2.flags | 2;
        tL_inputPeerNotifySettings2.silent = notificationsSettings.getBoolean(NotificationsSettingsFacade.PROPERTY_SILENT + sharedPrefKey, false);
        if (notificationsSettings.contains(NotificationsSettingsFacade.PROPERTY_STORIES_NOTIFY + sharedPrefKey)) {
            TLRPC.TL_inputPeerNotifySettings tL_inputPeerNotifySettings3 = updatenotifysettings.settings;
            tL_inputPeerNotifySettings3.flags |= 64;
            tL_inputPeerNotifySettings3.stories_muted = !notificationsSettings.getBoolean(NotificationsSettingsFacade.PROPERTY_STORIES_NOTIFY + sharedPrefKey, true);
        }
        int i = notificationsSettings.getInt(NotificationsSettingsFacade.PROPERTY_NOTIFY + getSharedPrefKey(j, j2), -1);
        if (i != -1) {
            TLRPC.TL_inputPeerNotifySettings tL_inputPeerNotifySettings4 = updatenotifysettings.settings;
            tL_inputPeerNotifySettings4.flags |= 4;
            if (i == 3) {
                tL_inputPeerNotifySettings4.mute_until = notificationsSettings.getInt(NotificationsSettingsFacade.PROPERTY_NOTIFY_UNTIL + getSharedPrefKey(j, j2), 0);
            } else {
                tL_inputPeerNotifySettings4.mute_until = i == 2 ? Integer.MAX_VALUE : 0;
            }
        }
        long j3 = notificationsSettings.getLong("sound_document_id_" + getSharedPrefKey(j, j2), 0L);
        String string = notificationsSettings.getString("sound_path_" + getSharedPrefKey(j, j2), null);
        TLRPC.TL_inputPeerNotifySettings tL_inputPeerNotifySettings5 = updatenotifysettings.settings;
        tL_inputPeerNotifySettings5.flags = tL_inputPeerNotifySettings5.flags | 8;
        if (j3 != 0) {
            TLRPC.TL_notificationSoundRingtone tL_notificationSoundRingtone = new TLRPC.TL_notificationSoundRingtone();
            tL_notificationSoundRingtone.id = j3;
            updatenotifysettings.settings.sound = tL_notificationSoundRingtone;
        } else if (string != null) {
            if (string.equalsIgnoreCase("NoSound")) {
                updatenotifysettings.settings.sound = new TLRPC.TL_notificationSoundNone();
            } else {
                TLRPC.TL_notificationSoundLocal tL_notificationSoundLocal = new TLRPC.TL_notificationSoundLocal();
                tL_notificationSoundLocal.title = notificationsSettings.getString("sound_" + getSharedPrefKey(j, j2), null);
                tL_notificationSoundLocal.data = string;
                updatenotifysettings.settings.sound = tL_notificationSoundLocal;
            }
        } else {
            tL_inputPeerNotifySettings5.sound = new TLRPC.TL_notificationSoundDefault();
        }
        if (j2 != 0 && j != getUserConfig().getClientUserId()) {
            TLRPC.TL_inputNotifyForumTopic tL_inputNotifyForumTopic = new TLRPC.TL_inputNotifyForumTopic();
            tL_inputNotifyForumTopic.peer = getMessagesController().getInputPeer(j);
            tL_inputNotifyForumTopic.top_msg_id = (int) j2;
            updatenotifysettings.peer = tL_inputNotifyForumTopic;
        } else {
            TLRPC.TL_inputNotifyPeer tL_inputNotifyPeer = new TLRPC.TL_inputNotifyPeer();
            updatenotifysettings.peer = tL_inputNotifyPeer;
            tL_inputNotifyPeer.peer = getMessagesController().getInputPeer(j);
        }
        getConnectionsManager().sendRequest(updatenotifysettings, new RequestDelegate() { // from class: org.telegram.messenger.NotificationsController$$ExternalSyntheticLambda55
            @Override // org.telegram.tgnet.RequestDelegate
            public final void run(TLObject tLObject, TLRPC.TL_error tL_error) {
                NotificationsController.$r8$lambda$XCcar2pyUU3PNWJSRPBNWRsFJM0(tLObject, tL_error);
            }
        });
    }

    public void updateServerNotificationsSettings(int i) {
        SharedPreferences notificationsSettings = getAccountInstance().getNotificationsSettings();
        if (i == 4 || i == 5) {
            TL_account.setReactionsNotifySettings setreactionsnotifysettings = new TL_account.setReactionsNotifySettings();
            setreactionsnotifysettings.settings = new TL_account.TL_reactionsNotifySettings();
            if (notificationsSettings.getBoolean("EnableReactionsMessages", true)) {
                setreactionsnotifysettings.settings.flags |= 1;
                if (notificationsSettings.getBoolean("EnableReactionsMessagesContacts", false)) {
                    setreactionsnotifysettings.settings.messages_notify_from = new TL_account.TL_reactionNotificationsFromContacts();
                } else {
                    setreactionsnotifysettings.settings.messages_notify_from = new TL_account.TL_reactionNotificationsFromAll();
                }
            }
            if (notificationsSettings.getBoolean("EnableReactionsStories", true)) {
                setreactionsnotifysettings.settings.flags |= 2;
                if (notificationsSettings.getBoolean("EnableReactionsStoriesContacts", false)) {
                    setreactionsnotifysettings.settings.stories_notify_from = new TL_account.TL_reactionNotificationsFromContacts();
                } else {
                    setreactionsnotifysettings.settings.stories_notify_from = new TL_account.TL_reactionNotificationsFromAll();
                }
            }
            setreactionsnotifysettings.settings.show_previews = notificationsSettings.getBoolean("EnableReactionsPreview", true);
            setreactionsnotifysettings.settings.sound = getInputSound(notificationsSettings, "ReactionSound", "ReactionSoundDocId", "ReactionSoundPath");
            getConnectionsManager().sendRequest(setreactionsnotifysettings, new RequestDelegate() { // from class: org.telegram.messenger.NotificationsController$$ExternalSyntheticLambda28
                @Override // org.telegram.tgnet.RequestDelegate
                public final void run(TLObject tLObject, TLRPC.TL_error tL_error) {
                    NotificationsController.$r8$lambda$m8WhB6SNQirs3X5XQWhhP5mgO4s(tLObject, tL_error);
                }
            });
            return;
        }
        TL_account.updateNotifySettings updatenotifysettings = new TL_account.updateNotifySettings();
        TLRPC.TL_inputPeerNotifySettings tL_inputPeerNotifySettings = new TLRPC.TL_inputPeerNotifySettings();
        updatenotifysettings.settings = tL_inputPeerNotifySettings;
        tL_inputPeerNotifySettings.flags = 5;
        if (i == 0) {
            updatenotifysettings.peer = new TLRPC.TL_inputNotifyChats();
            updatenotifysettings.settings.mute_until = notificationsSettings.getInt("EnableGroup2", 0);
            updatenotifysettings.settings.show_previews = notificationsSettings.getBoolean("EnablePreviewGroup", true);
            TLRPC.TL_inputPeerNotifySettings tL_inputPeerNotifySettings2 = updatenotifysettings.settings;
            tL_inputPeerNotifySettings2.flags |= 8;
            tL_inputPeerNotifySettings2.sound = getInputSound(notificationsSettings, "GroupSound", "GroupSoundDocId", "GroupSoundPath");
        } else if (i == 1 || i == 3) {
            updatenotifysettings.peer = new TLRPC.TL_inputNotifyUsers();
            updatenotifysettings.settings.mute_until = notificationsSettings.getInt("EnableAll2", 0);
            updatenotifysettings.settings.show_previews = notificationsSettings.getBoolean("EnablePreviewAll", true);
            TLRPC.TL_inputPeerNotifySettings tL_inputPeerNotifySettings3 = updatenotifysettings.settings;
            tL_inputPeerNotifySettings3.flags |= 128;
            tL_inputPeerNotifySettings3.stories_hide_sender = notificationsSettings.getBoolean("EnableHideStoriesSenders", false);
            if (notificationsSettings.contains("EnableAllStories")) {
                TLRPC.TL_inputPeerNotifySettings tL_inputPeerNotifySettings4 = updatenotifysettings.settings;
                tL_inputPeerNotifySettings4.flags |= 64;
                tL_inputPeerNotifySettings4.stories_muted = !notificationsSettings.getBoolean("EnableAllStories", true);
            }
            TLRPC.TL_inputPeerNotifySettings tL_inputPeerNotifySettings5 = updatenotifysettings.settings;
            tL_inputPeerNotifySettings5.flags |= 8;
            tL_inputPeerNotifySettings5.sound = getInputSound(notificationsSettings, "GlobalSound", "GlobalSoundDocId", "GlobalSoundPath");
            TLRPC.TL_inputPeerNotifySettings tL_inputPeerNotifySettings6 = updatenotifysettings.settings;
            tL_inputPeerNotifySettings6.flags |= 256;
            tL_inputPeerNotifySettings6.stories_sound = getInputSound(notificationsSettings, "StoriesSound", "StoriesSoundDocId", "StoriesSoundPath");
        } else {
            updatenotifysettings.peer = new TLRPC.TL_inputNotifyBroadcasts();
            updatenotifysettings.settings.mute_until = notificationsSettings.getInt("EnableChannel2", 0);
            updatenotifysettings.settings.show_previews = notificationsSettings.getBoolean("EnablePreviewChannel", true);
            TLRPC.TL_inputPeerNotifySettings tL_inputPeerNotifySettings7 = updatenotifysettings.settings;
            tL_inputPeerNotifySettings7.flags |= 8;
            tL_inputPeerNotifySettings7.sound = getInputSound(notificationsSettings, "ChannelSound", "ChannelSoundDocId", "ChannelSoundPath");
        }
        getConnectionsManager().sendRequest(updatenotifysettings, new RequestDelegate() { // from class: org.telegram.messenger.NotificationsController$$ExternalSyntheticLambda29
            @Override // org.telegram.tgnet.RequestDelegate
            public final void run(TLObject tLObject, TLRPC.TL_error tL_error) {
                NotificationsController.m3637$r8$lambda$1t1axbSYGQIU_GMVkHnzrj3Llc(tLObject, tL_error);
            }
        });
    }

    private TLRPC.NotificationSound getInputSound(SharedPreferences sharedPreferences, String str, String str2, String str3) {
        long j = sharedPreferences.getLong(str2, 0L);
        String string = sharedPreferences.getString(str3, "NoSound");
        if (j != 0) {
            TLRPC.TL_notificationSoundRingtone tL_notificationSoundRingtone = new TLRPC.TL_notificationSoundRingtone();
            tL_notificationSoundRingtone.id = j;
            return tL_notificationSoundRingtone;
        }
        if (string != null) {
            if (string.equalsIgnoreCase("NoSound")) {
                return new TLRPC.TL_notificationSoundNone();
            }
            TLRPC.TL_notificationSoundLocal tL_notificationSoundLocal = new TLRPC.TL_notificationSoundLocal();
            tL_notificationSoundLocal.title = sharedPreferences.getString(str, null);
            tL_notificationSoundLocal.data = string;
            return tL_notificationSoundLocal;
        }
        return new TLRPC.TL_notificationSoundDefault();
    }

    public boolean isGlobalNotificationsEnabled(long j, boolean z, boolean z2) {
        return isGlobalNotificationsEnabled(j, null, z, z2);
    }

    /* JADX WARN: Code duplicated, block: B:12:0x0018  */
    /* JADX WARN: Code duplicated, block: B:13:0x001a  */
    public boolean isGlobalNotificationsEnabled(long j, Boolean bool, boolean z, boolean z2) {
        int i;
        if (z) {
            i = 4;
        } else if (z2) {
            i = 5;
        } else if (!DialogObject.isChatDialog(j)) {
            i = 1;
        } else if (bool != null) {
            if (bool.booleanValue()) {
                i = 2;
            } else {
                i = 0;
            }
        } else {
            TLRPC.Chat chat = getMessagesController().getChat(Long.valueOf(-j));
            if (!ChatObject.isChannel(chat) || chat.megagroup) {
                i = 0;
            } else {
                i = 2;
            }
        }
        return isGlobalNotificationsEnabled(i);
    }

    public boolean isGlobalNotificationsEnabled(int i) {
        if (i == 4) {
            return getAccountInstance().getNotificationsSettings().getBoolean("EnableReactionsMessages", true);
        }
        if (i == 5) {
            return getAccountInstance().getNotificationsSettings().getBoolean("EnableReactionsStories", true);
        }
        if (i == 3) {
            return getAccountInstance().getNotificationsSettings().getBoolean("EnableAllStories", true);
        }
        return getAccountInstance().getNotificationsSettings().getInt(getGlobalNotificationsKey(i), 0) < getConnectionsManager().getCurrentTime();
    }

    public void setGlobalNotificationsEnabled(int i, int i2) {
        getAccountInstance().getNotificationsSettings().edit().putInt(getGlobalNotificationsKey(i), i2).apply();
        updateServerNotificationsSettings(i);
        getMessagesStorage().updateMutedDialogsFiltersCounters();
        deleteNotificationChannelGlobal(i);
    }

    public static String getGlobalNotificationsKey(int i) {
        if (i == 0) {
            return "EnableGroup2";
        }
        if (i == 1) {
            return "EnableAll2";
        }
        return "EnableChannel2";
    }

    public void muteDialog(long j, long j2, boolean z) {
        if (z) {
            getInstance(this.currentAccount).muteUntil(j, j2, Integer.MAX_VALUE);
            return;
        }
        boolean zIsGlobalNotificationsEnabled = getInstance(this.currentAccount).isGlobalNotificationsEnabled(j, false, false);
        boolean z2 = j2 != 0;
        SharedPreferences.Editor editorEdit = MessagesController.getNotificationsSettings(this.currentAccount).edit();
        if (zIsGlobalNotificationsEnabled && !z2) {
            editorEdit.remove(NotificationsSettingsFacade.PROPERTY_NOTIFY + getSharedPrefKey(j, j2));
        } else {
            editorEdit.putInt(NotificationsSettingsFacade.PROPERTY_NOTIFY + getSharedPrefKey(j, j2), 0);
        }
        if (j2 == 0) {
            getMessagesStorage().setDialogFlags(j, 0L);
            TLRPC.Dialog dialog = (TLRPC.Dialog) getMessagesController().dialogs_dict.get(j);
            if (dialog != null) {
                dialog.notify_settings = new TLRPC.TL_peerNotifySettings();
            }
        }
        editorEdit.apply();
        updateServerNotificationsSettings(j, j2);
    }

    public NotificationsSettingsFacade getNotificationsSettingsFacade() {
        return this.dialogsNotificationsFacade;
    }

    public void loadTopicsNotificationsExceptions(final long j, final Consumer<HashSet<Integer>> consumer) {
        getMessagesStorage().getStorageQueue().postRunnable(new Runnable() { // from class: org.telegram.messenger.NotificationsController$$ExternalSyntheticLambda54
            @Override // java.lang.Runnable
            public final void run() {
                this.f$0.lambda$loadTopicsNotificationsExceptions$52(j, consumer);
            }
        });
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$loadTopicsNotificationsExceptions$52(long j, final Consumer consumer) {
        final HashSet hashSet = new HashSet();
        Iterator<Map.Entry<String, ?>> it = MessagesController.getNotificationsSettings(this.currentAccount).getAll().entrySet().iterator();
        while (it.hasNext()) {
            String key = it.next().getKey();
            if (key != null) {
                if (key.startsWith(NotificationsSettingsFacade.PROPERTY_NOTIFY + j)) {
                    Integer num = Utilities.parseInt((CharSequence) key.replace(NotificationsSettingsFacade.PROPERTY_NOTIFY + j, _UrlKt.FRAGMENT_ENCODE_SET));
                    int iIntValue = num.intValue();
                    if (iIntValue != 0 && getMessagesController().isDialogMuted(j, iIntValue) != getMessagesController().isDialogMuted(j, 0L)) {
                        hashSet.add(num);
                    }
                }
            }
        }
        AndroidUtilities.runOnUIThread(new Runnable() { // from class: org.telegram.messenger.NotificationsController$$ExternalSyntheticLambda7
            @Override // java.lang.Runnable
            public final void run() {
                NotificationsController.$r8$lambda$0CEGUOl_qmFmYy3ALvzz2hIMSaw(consumer, hashSet);
            }
        });
    }

    public static /* synthetic */ void $r8$lambda$0CEGUOl_qmFmYy3ALvzz2hIMSaw(Consumer consumer, HashSet hashSet) {
        if (consumer != null) {
            consumer.v(hashSet);
        }
    }

    private static class DialogKey {
        final long dialogId;
        final boolean story;
        final long topicId;

        private DialogKey(long j, long j2, boolean z) {
            this.dialogId = j;
            this.topicId = j2;
            this.story = z;
        }
    }

    public static class StoryNotification {
        public long date;
        final HashMap<Integer, Pair<Long, Long>> dateByIds;
        final long dialogId;
        boolean hidden;
        String localName;

        public StoryNotification(long j, String str, int i, long j2) {
            this(j, str, i, j2, j2 + 86400000);
        }

        public StoryNotification(long j, String str, int i, long j2, long j3) {
            HashMap<Integer, Pair<Long, Long>> map = new HashMap<>();
            this.dateByIds = map;
            this.dialogId = j;
            this.localName = str;
            map.put(Integer.valueOf(i), new Pair<>(Long.valueOf(j2), Long.valueOf(j3)));
            this.date = j2;
        }

        public long getLeastDate() {
            long jLongValue = -1;
            for (Pair<Long, Long> pair : this.dateByIds.values()) {
                if (jLongValue == -1 || jLongValue > ((Long) pair.first).longValue()) {
                    jLongValue = ((Long) pair.first).longValue();
                }
            }
            return jLongValue;
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void checkStoryPushes() {
        long jCurrentTimeMillis = System.currentTimeMillis();
        int i = 0;
        boolean z = false;
        while (i < this.storyPushMessages.size()) {
            StoryNotification storyNotification = this.storyPushMessages.get(i);
            Iterator<Map.Entry<Integer, Pair<Long, Long>>> it = storyNotification.dateByIds.entrySet().iterator();
            while (it.hasNext()) {
                if (jCurrentTimeMillis >= ((Long) it.next().getValue().second).longValue()) {
                    it.remove();
                    z = true;
                }
            }
            if (z) {
                if (storyNotification.dateByIds.isEmpty()) {
                    getMessagesStorage().deleteStoryPushMessage(storyNotification.dialogId);
                    this.storyPushMessages.remove(i);
                    i--;
                } else {
                    getMessagesStorage().putStoryPushMessage(storyNotification);
                }
            }
            i++;
        }
        if (z) {
            showOrUpdateNotification(false);
        }
        updateStoryPushesRunnable();
    }

    private void updateStoryPushesRunnable() {
        long jMin = Long.MAX_VALUE;
        for (int i = 0; i < this.storyPushMessages.size(); i++) {
            Iterator<Pair<Long, Long>> it = this.storyPushMessages.get(i).dateByIds.values().iterator();
            while (it.hasNext()) {
                jMin = Math.min(jMin, ((Long) it.next().second).longValue());
            }
        }
        DispatchQueue dispatchQueue = notificationsQueue;
        dispatchQueue.cancelRunnable(this.checkStoryPushesRunnable);
        long jCurrentTimeMillis = jMin - System.currentTimeMillis();
        if (jMin != Long.MAX_VALUE) {
            dispatchQueue.postRunnable(this.checkStoryPushesRunnable, Math.max(0L, jCurrentTimeMillis));
        }
    }

    private String getTitle(TLRPC.Chat chat) {
        if (chat == null) {
            return null;
        }
        if (chat.monoforum) {
            return ForumUtilities.getMonoForumTitle(this.currentAccount, chat);
        }
        return chat.title;
    }
}
