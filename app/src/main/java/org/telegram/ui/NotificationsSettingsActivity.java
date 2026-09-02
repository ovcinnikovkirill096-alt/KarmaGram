package org.telegram.ui;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Paint;
import android.graphics.drawable.Drawable;
import android.media.Ringtone;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Parcelable;
import android.provider.Settings;
import android.text.TextUtils;
import android.util.LongSparseArray;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.TextView;
import android.widget.Toast;
import androidx.annotation.Keep;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import j$.util.Comparator;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Map;
import java.util.function.ToDoubleFunction;
import okhttp3.internal.url._UrlKt;
import org.telegram.messenger.AndroidUtilities;
import org.telegram.messenger.ApplicationLoader;
import org.telegram.messenger.ChatObject;
import org.telegram.messenger.DialogObject;
import org.telegram.messenger.FileLog;
import org.telegram.messenger.LocaleController;
import org.telegram.messenger.MediaDataController;
import org.telegram.messenger.MessagesController;
import org.telegram.messenger.MessagesStorage;
import org.telegram.messenger.NotificationCenter;
import org.telegram.messenger.NotificationsController;
import org.telegram.messenger.NotificationsSettingsFacade;
import org.telegram.messenger.R;
import org.telegram.messenger.SharedConfig;
import org.telegram.messenger.UserConfig;
import org.telegram.messenger.Utilities;
import org.telegram.tgnet.ConnectionsManager;
import org.telegram.tgnet.RequestDelegate;
import org.telegram.tgnet.TLObject;
import org.telegram.tgnet.TLRPC;
import org.telegram.tgnet.tl.TL_account;
import org.telegram.ui.ActionBar.ActionBar;
import org.telegram.ui.ActionBar.AlertDialog;
import org.telegram.ui.ActionBar.BaseFragment;
import org.telegram.ui.ActionBar.Theme;
import org.telegram.ui.ActionBar.ThemeDescription;
import org.telegram.ui.Cells.HeaderCell;
import org.telegram.ui.Cells.NotificationsCheckCell;
import org.telegram.ui.Cells.ShadowSectionCell;
import org.telegram.ui.Cells.TextCheckCell;
import org.telegram.ui.Cells.TextDetailSettingsCell;
import org.telegram.ui.Cells.TextInfoPrivacyCell;
import org.telegram.ui.Cells.TextSettingsCell;
import org.telegram.ui.Components.AlertsCreator;
import org.telegram.ui.Components.LayoutHelper;
import org.telegram.ui.Components.RecyclerListView;

public class NotificationsSettingsActivity extends BaseFragment implements NotificationCenter.NotificationCenterDelegate {

    @Keep
    private int accountsAllRow;
    private int accountsInfoRow;
    private int accountsSectionRow;
    private ListAdapter adapter;
    private int androidAutoAlertRow;

    @Keep
    private int badgeNumberMessagesRow;

    @Keep
    private int badgeNumberMutedRow;
    private int badgeNumberSection;
    private int badgeNumberSection2Row;

    @Keep
    private int badgeNumberShowRow;
    private int callsRingtoneRow;
    private int callsSection2Row;
    private int callsSectionRow;
    private int callsVibrateRow;

    @Keep
    private int channelsRow;

    @Keep
    private int contactJoinedRow;
    private int eventsSection2Row;
    private int eventsSectionRow;

    @Keep
    private int groupRow;

    @Keep
    private int inappPreviewRow;

    @Keep
    private int inappPriorityRow;
    private int inappSectionRow;

    @Keep
    private int inappSoundRow;

    @Keep
    private int inappVibrateRow;

    @Keep
    private int inchatSoundRow;
    private LinearLayoutManager layoutManager;
    private RecyclerListView listView;
    private int notificationsSection2Row;
    private int notificationsSectionRow;
    private int notificationsServiceConnectionRow;
    private int notificationsServiceRow;
    private int otherSection2Row;
    private int otherSectionRow;

    @Keep
    private int pinnedMessageRow;

    @Keep
    private int privateRow;

    @Keep
    private int reactionsRow;
    private int repeatRow;

    @Keep
    private int resetNotificationsRow;
    private int resetNotificationsSectionRow;
    private int resetSection2Row;
    private int resetSectionRow;

    @Keep
    private int storiesRow;
    private boolean updateRepeatNotifications;
    private boolean updateRingtone;
    private boolean updateVibrate;
    private boolean reseting = false;
    private ArrayList exceptionUsers = null;
    private ArrayList exceptionChats = null;
    private ArrayList exceptionChannels = null;
    private ArrayList exceptionStories = null;
    private ArrayList exceptionAutoStories = null;
    private int rowCount = 0;

    public static class NotificationException {
        public boolean auto;
        public long did;
        public boolean hasCustom;
        public int muteUntil;
        public int notify;
        public boolean story;
    }

    public static /* synthetic */ void $r8$lambda$z8728ZbQxis9p3M0Wak40C7BT78(TLObject tLObject, TLRPC.TL_error tL_error) {
    }

    @Override // org.telegram.ui.ActionBar.BaseFragment
    public boolean isSupportEdgeToEdge() {
        return true;
    }

    @Override // org.telegram.ui.ActionBar.BaseFragment
    public boolean onFragmentCreate() {
        MessagesController.getInstance(this.currentAccount).loadSignUpNotificationsSettings();
        loadExceptions(null);
        if (UserConfig.getActivatedAccountsCount() > 1) {
            int i = this.rowCount;
            this.accountsSectionRow = i;
            this.accountsAllRow = i + 1;
            this.rowCount = i + 3;
            this.accountsInfoRow = i + 2;
        } else {
            this.accountsSectionRow = -1;
            this.accountsAllRow = -1;
            this.accountsInfoRow = -1;
        }
        int i2 = this.rowCount;
        this.notificationsSectionRow = i2;
        this.privateRow = i2 + 1;
        this.groupRow = i2 + 2;
        this.channelsRow = i2 + 3;
        this.storiesRow = i2 + 4;
        this.reactionsRow = i2 + 5;
        this.notificationsSection2Row = i2 + 6;
        this.callsSectionRow = i2 + 7;
        this.callsVibrateRow = i2 + 8;
        this.callsRingtoneRow = i2 + 9;
        this.eventsSection2Row = i2 + 10;
        this.badgeNumberSection = i2 + 11;
        this.badgeNumberShowRow = i2 + 12;
        this.badgeNumberMutedRow = i2 + 13;
        this.badgeNumberMessagesRow = i2 + 14;
        this.badgeNumberSection2Row = i2 + 15;
        this.inappSectionRow = i2 + 16;
        this.inappSoundRow = i2 + 17;
        this.inappVibrateRow = i2 + 18;
        this.inappPreviewRow = i2 + 19;
        this.inchatSoundRow = i2 + 20;
        this.inappPriorityRow = i2 + 21;
        this.callsSection2Row = i2 + 22;
        this.eventsSectionRow = i2 + 23;
        this.contactJoinedRow = i2 + 24;
        this.pinnedMessageRow = i2 + 25;
        this.otherSection2Row = i2 + 26;
        this.otherSectionRow = i2 + 27;
        this.notificationsServiceRow = i2 + 28;
        this.notificationsServiceConnectionRow = i2 + 29;
        this.androidAutoAlertRow = -1;
        this.repeatRow = i2 + 30;
        this.resetSection2Row = i2 + 31;
        this.resetSectionRow = i2 + 32;
        this.resetNotificationsRow = i2 + 33;
        this.rowCount = i2 + 35;
        this.resetNotificationsSectionRow = i2 + 34;
        NotificationCenter.getInstance(this.currentAccount).addObserver(this, NotificationCenter.notificationsSettingsUpdated);
        getMessagesController().reloadReactionsNotifySettings();
        return super.onFragmentCreate();
    }

    public void loadExceptions(final Runnable runnable) {
        MediaDataController.getInstance(this.currentAccount).loadHints(true);
        final ArrayList arrayList = new ArrayList(MediaDataController.getInstance(this.currentAccount).hints);
        MessagesStorage.getInstance(this.currentAccount).getStorageQueue().postRunnable(new Runnable() { // from class: org.telegram.ui.NotificationsSettingsActivity$$ExternalSyntheticLambda1
            @Override // java.lang.Runnable
            public final void run() {
                this.f$0.lambda$loadExceptions$2(arrayList, runnable);
            }
        });
    }

    /* JADX INFO: Access modifiers changed from: private */
    /* JADX WARN: Code duplicated, block: B:143:0x0354  */
    /* JADX WARN: Code duplicated, block: B:145:0x035e  */
    /* JADX WARN: Code duplicated, block: B:149:0x0366  */
    /* JADX WARN: Code duplicated, block: B:161:0x039c  */
    /* JADX WARN: Code duplicated, block: B:164:0x03a7  */
    /* JADX WARN: Code duplicated, block: B:168:0x03b6 A[LOOP:5: B:167:0x03b4->B:168:0x03b6, LOOP_END] */
    /* JADX WARN: Code duplicated, block: B:171:0x03d0  */
    /* JADX WARN: Code duplicated, block: B:173:0x03da  */
    /* JADX WARN: Code duplicated, block: B:174:0x03e9  */
    /* JADX WARN: Code duplicated, block: B:239:0x03ac A[SYNTHETIC] */
    public /* synthetic */ void lambda$loadExceptions$2(ArrayList arrayList, final Runnable runnable) {
        boolean z;
        ArrayList<TLRPC.Chat> arrayList2;
        ArrayList<TLRPC.User> arrayList3;
        ArrayList<TLRPC.EncryptedChat> arrayList4;
        int size;
        int i;
        ArrayList arrayList5;
        int size2;
        int i2;
        int size3;
        int i3;
        int size4;
        int i4;
        TLRPC.User user;
        TLRPC.Chat chat;
        ArrayList arrayList6;
        int i5;
        ArrayList arrayList7;
        ArrayList arrayList8;
        ArrayList arrayList9;
        ArrayList arrayList10;
        ArrayList<TLRPC.EncryptedChat> arrayList11;
        final ArrayList arrayList12 = new ArrayList();
        final ArrayList arrayList13 = new ArrayList();
        final ArrayList arrayList14 = new ArrayList();
        ArrayList arrayList15 = new ArrayList();
        ArrayList arrayList16 = new ArrayList();
        LongSparseArray longSparseArray = new LongSparseArray();
        ArrayList<Long> arrayList17 = new ArrayList<>();
        ArrayList arrayList18 = new ArrayList();
        ArrayList arrayList19 = new ArrayList();
        ArrayList<TLRPC.User> arrayList20 = new ArrayList<>();
        ArrayList<TLRPC.Chat> arrayList21 = new ArrayList<>();
        ArrayList<TLRPC.EncryptedChat> arrayList22 = new ArrayList<>();
        long j = UserConfig.getInstance(this.currentAccount).clientUserId;
        SharedPreferences notificationsSettings = MessagesController.getNotificationsSettings(this.currentAccount);
        Map<String, ?> all = notificationsSettings.getAll();
        Iterator<Map.Entry<String, ?>> it = all.entrySet().iterator();
        while (true) {
            arrayList21 = arrayList21;
            if (!it.hasNext()) {
                break;
            }
            Map.Entry<String, ?> next = it.next();
            String key = next.getKey();
            arrayList20 = arrayList20;
            if (key.startsWith(NotificationsSettingsFacade.PROPERTY_NOTIFY)) {
                arrayList11 = arrayList22;
                String strReplace = key.replace(NotificationsSettingsFacade.PROPERTY_NOTIFY, _UrlKt.FRAGMENT_ENCODE_SET);
                if (strReplace.contains("_")) {
                    arrayList22 = arrayList11;
                } else {
                    Long l = Utilities.parseLong(strReplace);
                    ArrayList arrayList23 = arrayList15;
                    arrayList10 = arrayList16;
                    long jLongValue = l.longValue();
                    if (jLongValue == 0 || jLongValue == j) {
                        arrayList9 = arrayList23;
                    } else {
                        arrayList9 = arrayList23;
                        NotificationException notificationException = new NotificationException();
                        notificationException.did = jLongValue;
                        StringBuilder sb = new StringBuilder();
                        ArrayList arrayList24 = arrayList13;
                        sb.append(NotificationsSettingsFacade.PROPERTY_CUSTOM);
                        sb.append(jLongValue);
                        notificationException.hasCustom = notificationsSettings.getBoolean(sb.toString(), false);
                        int iIntValue = ((Integer) next.getValue()).intValue();
                        notificationException.notify = iIntValue;
                        if (iIntValue != 0) {
                            Integer num = (Integer) all.get(NotificationsSettingsFacade.PROPERTY_NOTIFY_UNTIL + strReplace);
                            if (num != null) {
                                notificationException.muteUntil = num.intValue();
                            }
                        }
                        if (DialogObject.isEncryptedDialog(jLongValue)) {
                            int encryptedChatId = DialogObject.getEncryptedChatId(jLongValue);
                            TLRPC.EncryptedChat encryptedChat = MessagesController.getInstance(this.currentAccount).getEncryptedChat(Integer.valueOf(encryptedChatId));
                            if (encryptedChat == null) {
                                arrayList19.add(Integer.valueOf(encryptedChatId));
                                longSparseArray.put(jLongValue, notificationException);
                            } else {
                                TLRPC.User user2 = MessagesController.getInstance(this.currentAccount).getUser(Long.valueOf(encryptedChat.user_id));
                                if (user2 == null) {
                                    arrayList17.add(Long.valueOf(encryptedChat.user_id));
                                    longSparseArray.put(encryptedChat.user_id, notificationException);
                                } else if (user2.deleted) {
                                    arrayList22 = arrayList11;
                                    arrayList16 = arrayList10;
                                    arrayList15 = arrayList9;
                                    arrayList13 = arrayList24;
                                }
                            }
                            arrayList12.add(notificationException);
                            arrayList13 = arrayList24;
                        } else if (DialogObject.isUserDialog(jLongValue)) {
                            TLRPC.User user3 = MessagesController.getInstance(this.currentAccount).getUser(l);
                            if (user3 == null) {
                                arrayList17.add(l);
                                longSparseArray.put(jLongValue, notificationException);
                            } else if (user3.deleted) {
                                arrayList22 = arrayList11;
                                arrayList16 = arrayList10;
                                arrayList15 = arrayList9;
                                arrayList13 = arrayList24;
                            }
                            arrayList12.add(notificationException);
                            arrayList13 = arrayList24;
                        } else {
                            long j2 = -jLongValue;
                            TLRPC.Chat chat2 = MessagesController.getInstance(this.currentAccount).getChat(Long.valueOf(j2));
                            if (chat2 == null) {
                                arrayList18.add(Long.valueOf(j2));
                                longSparseArray.put(jLongValue, notificationException);
                            } else if (!chat2.left && !chat2.kicked && chat2.migrated_to == null) {
                                if (ChatObject.isChannel(chat2) && !chat2.megagroup) {
                                    arrayList14.add(notificationException);
                                    arrayList13 = arrayList24;
                                } else {
                                    arrayList13 = arrayList24;
                                    arrayList13.add(notificationException);
                                }
                            }
                            arrayList22 = arrayList11;
                            arrayList16 = arrayList10;
                            arrayList15 = arrayList9;
                            arrayList13 = arrayList24;
                        }
                    }
                }
            } else {
                arrayList9 = arrayList15;
                arrayList10 = arrayList16;
                arrayList11 = arrayList22;
            }
            arrayList22 = arrayList11;
            arrayList16 = arrayList10;
            arrayList15 = arrayList9;
        }
        ArrayList arrayList25 = arrayList15;
        ArrayList arrayList26 = arrayList16;
        ArrayList<TLRPC.User> arrayList27 = arrayList20;
        ArrayList<TLRPC.EncryptedChat> arrayList28 = arrayList22;
        HashSet hashSet = new HashSet();
        Iterator<Map.Entry<String, ?>> it2 = all.entrySet().iterator();
        while (true) {
            z = true;
            if (!it2.hasNext()) {
                break;
            }
            Map.Entry<String, ?> next2 = it2.next();
            String key2 = next2.getKey();
            if (key2.startsWith(NotificationsSettingsFacade.PROPERTY_STORIES_NOTIFY)) {
                try {
                    Long l2 = Utilities.parseLong(key2.substring(8));
                    long jLongValue2 = l2.longValue();
                    if (jLongValue2 != 0 && jLongValue2 != j) {
                        NotificationException notificationException2 = new NotificationException();
                        notificationException2.did = jLongValue2;
                        notificationException2.notify = ((Boolean) next2.getValue()).booleanValue() ? 0 : Integer.MAX_VALUE;
                        notificationException2.story = true;
                        if (DialogObject.isUserDialog(jLongValue2)) {
                            TLRPC.User user4 = getMessagesController().getUser(l2);
                            if (user4 == null) {
                                arrayList17.add(l2);
                                longSparseArray.put(jLongValue2, notificationException2);
                            } else if (user4.deleted) {
                            }
                            arrayList8 = arrayList25;
                            try {
                                arrayList8.add(notificationException2);
                                hashSet.add(l2);
                            } catch (Exception unused) {
                            }
                            arrayList25 = arrayList8;
                        }
                    }
                } catch (Exception unused2) {
                }
            }
            arrayList8 = arrayList25;
            arrayList25 = arrayList8;
        }
        final ArrayList arrayList29 = arrayList25;
        if (arrayList != null) {
            ArrayList arrayList30 = arrayList;
            Collections.sort(arrayList30, Comparator.CC.comparingDouble(new ToDoubleFunction() { // from class: org.telegram.ui.NotificationsSettingsActivity$$ExternalSyntheticLambda2
                @Override // java.util.function.ToDoubleFunction
                public final double applyAsDouble(Object obj) {
                    return ((TLRPC.TL_topPeer) obj).rating;
                }
            }));
            int iMax = Math.max(0, arrayList30.size() - 5);
            while (iMax < arrayList30.size()) {
                long peerDialogId = DialogObject.getPeerDialogId(((TLRPC.TL_topPeer) arrayList30.get(iMax)).peer);
                if (hashSet.contains(Long.valueOf(peerDialogId))) {
                    i5 = iMax;
                } else {
                    NotificationException notificationException3 = new NotificationException();
                    notificationException3.did = peerDialogId;
                    i5 = iMax;
                    notificationException3.notify = 0;
                    notificationException3.auto = z;
                    notificationException3.story = z;
                    if (DialogObject.isUserDialog(peerDialogId)) {
                        TLRPC.User user5 = getMessagesController().getUser(Long.valueOf(peerDialogId));
                        if (user5 == null) {
                            arrayList17.add(Long.valueOf(peerDialogId));
                            longSparseArray.put(peerDialogId, notificationException3);
                        } else if (user5.deleted) {
                        }
                        arrayList7 = arrayList26;
                        arrayList7.add(0, notificationException3);
                        hashSet.add(Long.valueOf(peerDialogId));
                    }
                    arrayList26 = arrayList7;
                    iMax = i5 + 1;
                    z = true;
                    arrayList30 = arrayList;
                }
                arrayList7 = arrayList26;
                arrayList26 = arrayList7;
                iMax = i5 + 1;
                z = true;
                arrayList30 = arrayList;
            }
        }
        ArrayList arrayList31 = arrayList26;
        if (longSparseArray.size() != 0) {
            try {
                if (arrayList19.isEmpty()) {
                    arrayList4 = arrayList28;
                } else {
                    try {
                        arrayList4 = arrayList28;
                        try {
                            MessagesStorage.getInstance(this.currentAccount).getEncryptedChatsInternal(TextUtils.join(",", arrayList19), arrayList4, arrayList17);
                        } catch (Exception e) {
                            e = e;
                            arrayList2 = arrayList21;
                            arrayList3 = arrayList27;
                            FileLog.e(e);
                            size = arrayList2.size();
                            i = 0;
                            while (i < size) {
                                chat = arrayList2.get(i);
                                if (chat.left) {
                                    arrayList6 = arrayList31;
                                } else {
                                    arrayList6 = arrayList31;
                                }
                                i++;
                                arrayList31 = arrayList6;
                            }
                            arrayList5 = arrayList31;
                            size2 = arrayList3.size();
                            for (i2 = 0; i2 < size2; i2++) {
                                user = arrayList3.get(i2);
                                if (!user.deleted) {
                                    longSparseArray.remove(user.id);
                                }
                            }
                            size3 = arrayList4.size();
                            for (i3 = 0; i3 < size3; i3++) {
                                longSparseArray.remove(DialogObject.makeEncryptedDialogId(arrayList4.get(i3).id));
                            }
                            size4 = longSparseArray.size();
                            for (i4 = 0; i4 < size4; i4++) {
                                if (DialogObject.isChatDialog(longSparseArray.keyAt(i4))) {
                                    arrayList13.remove(longSparseArray.valueAt(i4));
                                    arrayList14.remove(longSparseArray.valueAt(i4));
                                } else {
                                    arrayList12.remove(longSparseArray.valueAt(i4));
                                }
                            }
                            final ArrayList<TLRPC.Chat> arrayList32 = arrayList2;
                            final ArrayList<TLRPC.User> arrayList33 = arrayList3;
                            final ArrayList<TLRPC.EncryptedChat> arrayList34 = arrayList4;
                            final ArrayList arrayList35 = arrayList5;
                            AndroidUtilities.runOnUIThread(new Runnable() { // from class: org.telegram.ui.NotificationsSettingsActivity$$ExternalSyntheticLambda3
                                @Override // java.lang.Runnable
                                public final void run() {
                                    this.f$0.lambda$loadExceptions$1(arrayList33, arrayList32, arrayList34, arrayList12, arrayList13, arrayList14, arrayList29, arrayList35, runnable);
                                }
                            });
                        }
                    } catch (Exception e2) {
                        e = e2;
                        arrayList4 = arrayList28;
                        arrayList2 = arrayList21;
                        arrayList3 = arrayList27;
                        FileLog.e(e);
                        size = arrayList2.size();
                        i = 0;
                        while (i < size) {
                            chat = arrayList2.get(i);
                            if (chat.left) {
                                arrayList6 = arrayList31;
                            } else {
                                arrayList6 = arrayList31;
                            }
                            i++;
                            arrayList31 = arrayList6;
                        }
                        arrayList5 = arrayList31;
                        size2 = arrayList3.size();
                        while (i2 < size2) {
                            user = arrayList3.get(i2);
                            if (!user.deleted) {
                                longSparseArray.remove(user.id);
                            }
                        }
                        size3 = arrayList4.size();
                        while (i3 < size3) {
                            longSparseArray.remove(DialogObject.makeEncryptedDialogId(arrayList4.get(i3).id));
                        }
                        size4 = longSparseArray.size();
                        while (i4 < size4) {
                            if (DialogObject.isChatDialog(longSparseArray.keyAt(i4))) {
                                arrayList13.remove(longSparseArray.valueAt(i4));
                                arrayList14.remove(longSparseArray.valueAt(i4));
                            } else {
                                arrayList12.remove(longSparseArray.valueAt(i4));
                            }
                        }
                        final ArrayList arrayList36 = arrayList2;
                        final ArrayList arrayList37 = arrayList3;
                        final ArrayList arrayList38 = arrayList4;
                        final ArrayList arrayList39 = arrayList5;
                        AndroidUtilities.runOnUIThread(new Runnable() { // from class: org.telegram.ui.NotificationsSettingsActivity$$ExternalSyntheticLambda3
                            @Override // java.lang.Runnable
                            public final void run() {
                                this.f$0.lambda$loadExceptions$1(arrayList37, arrayList36, arrayList38, arrayList12, arrayList13, arrayList14, arrayList29, arrayList39, runnable);
                            }
                        });
                    }
                }
                if (arrayList17.isEmpty()) {
                    arrayList3 = arrayList27;
                } else {
                    try {
                        arrayList3 = arrayList27;
                        try {
                            MessagesStorage.getInstance(this.currentAccount).getUsersInternal(arrayList17, arrayList3);
                        } catch (Exception e3) {
                            e = e3;
                            arrayList2 = arrayList21;
                            FileLog.e(e);
                            size = arrayList2.size();
                            i = 0;
                            while (i < size) {
                                chat = arrayList2.get(i);
                                if (chat.left) {
                                    arrayList6 = arrayList31;
                                } else {
                                    arrayList6 = arrayList31;
                                }
                                i++;
                                arrayList31 = arrayList6;
                            }
                            arrayList5 = arrayList31;
                            size2 = arrayList3.size();
                            while (i2 < size2) {
                                user = arrayList3.get(i2);
                                if (!user.deleted) {
                                    longSparseArray.remove(user.id);
                                }
                            }
                            size3 = arrayList4.size();
                            while (i3 < size3) {
                                longSparseArray.remove(DialogObject.makeEncryptedDialogId(arrayList4.get(i3).id));
                            }
                            size4 = longSparseArray.size();
                            while (i4 < size4) {
                                if (DialogObject.isChatDialog(longSparseArray.keyAt(i4))) {
                                    arrayList13.remove(longSparseArray.valueAt(i4));
                                    arrayList14.remove(longSparseArray.valueAt(i4));
                                } else {
                                    arrayList12.remove(longSparseArray.valueAt(i4));
                                }
                            }
                            final ArrayList arrayList310 = arrayList2;
                            final ArrayList arrayList311 = arrayList3;
                            final ArrayList arrayList312 = arrayList4;
                            final ArrayList arrayList313 = arrayList5;
                            AndroidUtilities.runOnUIThread(new Runnable() { // from class: org.telegram.ui.NotificationsSettingsActivity$$ExternalSyntheticLambda3
                                @Override // java.lang.Runnable
                                public final void run() {
                                    this.f$0.lambda$loadExceptions$1(arrayList311, arrayList310, arrayList312, arrayList12, arrayList13, arrayList14, arrayList29, arrayList313, runnable);
                                }
                            });
                        }
                    } catch (Exception e4) {
                        e = e4;
                        arrayList3 = arrayList27;
                        arrayList2 = arrayList21;
                        FileLog.e(e);
                        size = arrayList2.size();
                        i = 0;
                        while (i < size) {
                            chat = arrayList2.get(i);
                            if (chat.left) {
                                arrayList6 = arrayList31;
                            } else {
                                arrayList6 = arrayList31;
                            }
                            i++;
                            arrayList31 = arrayList6;
                        }
                        arrayList5 = arrayList31;
                        size2 = arrayList3.size();
                        while (i2 < size2) {
                            user = arrayList3.get(i2);
                            if (!user.deleted) {
                                longSparseArray.remove(user.id);
                            }
                        }
                        size3 = arrayList4.size();
                        while (i3 < size3) {
                            longSparseArray.remove(DialogObject.makeEncryptedDialogId(arrayList4.get(i3).id));
                        }
                        size4 = longSparseArray.size();
                        while (i4 < size4) {
                            if (DialogObject.isChatDialog(longSparseArray.keyAt(i4))) {
                                arrayList13.remove(longSparseArray.valueAt(i4));
                                arrayList14.remove(longSparseArray.valueAt(i4));
                            } else {
                                arrayList12.remove(longSparseArray.valueAt(i4));
                            }
                        }
                        final ArrayList arrayList314 = arrayList2;
                        final ArrayList arrayList315 = arrayList3;
                        final ArrayList arrayList316 = arrayList4;
                        final ArrayList arrayList317 = arrayList5;
                        AndroidUtilities.runOnUIThread(new Runnable() { // from class: org.telegram.ui.NotificationsSettingsActivity$$ExternalSyntheticLambda3
                            @Override // java.lang.Runnable
                            public final void run() {
                                this.f$0.lambda$loadExceptions$1(arrayList315, arrayList314, arrayList316, arrayList12, arrayList13, arrayList14, arrayList29, arrayList317, runnable);
                            }
                        });
                    }
                }
                if (arrayList18.isEmpty()) {
                    arrayList2 = arrayList21;
                } else {
                    MessagesStorage messagesStorage = MessagesStorage.getInstance(this.currentAccount);
                    String strJoin = TextUtils.join(",", arrayList18);
                    arrayList2 = arrayList21;
                    try {
                        messagesStorage.getChatsInternal(strJoin, arrayList2);
                    } catch (Exception e5) {
                        e = e5;
                        FileLog.e(e);
                    }
                }
            } catch (Exception e6) {
                e = e6;
                arrayList2 = arrayList21;
                arrayList3 = arrayList27;
                arrayList4 = arrayList28;
            }
            size = arrayList2.size();
            i = 0;
            while (i < size) {
                chat = arrayList2.get(i);
                if (chat.left || chat.kicked || chat.migrated_to != null) {
                    arrayList6 = arrayList31;
                } else {
                    NotificationException notificationException4 = (NotificationException) longSparseArray.get(-chat.id);
                    arrayList6 = arrayList31;
                    longSparseArray.remove(-chat.id);
                    if (notificationException4 != null) {
                        if (ChatObject.isChannel(chat) && !chat.megagroup) {
                            arrayList14.add(notificationException4);
                        } else {
                            arrayList13.add(notificationException4);
                        }
                    }
                }
                i++;
                arrayList31 = arrayList6;
            }
            arrayList5 = arrayList31;
            size2 = arrayList3.size();
            while (i2 < size2) {
                user = arrayList3.get(i2);
                if (!user.deleted) {
                    longSparseArray.remove(user.id);
                }
            }
            size3 = arrayList4.size();
            while (i3 < size3) {
                longSparseArray.remove(DialogObject.makeEncryptedDialogId(arrayList4.get(i3).id));
            }
            size4 = longSparseArray.size();
            while (i4 < size4) {
                if (DialogObject.isChatDialog(longSparseArray.keyAt(i4))) {
                    arrayList13.remove(longSparseArray.valueAt(i4));
                    arrayList14.remove(longSparseArray.valueAt(i4));
                } else {
                    arrayList12.remove(longSparseArray.valueAt(i4));
                }
            }
        } else {
            arrayList5 = arrayList31;
            arrayList2 = arrayList21;
            arrayList3 = arrayList27;
            arrayList4 = arrayList28;
        }
        final ArrayList arrayList318 = arrayList2;
        final ArrayList arrayList319 = arrayList3;
        final ArrayList arrayList3110 = arrayList4;
        final ArrayList arrayList3111 = arrayList5;
        AndroidUtilities.runOnUIThread(new Runnable() { // from class: org.telegram.ui.NotificationsSettingsActivity$$ExternalSyntheticLambda3
            @Override // java.lang.Runnable
            public final void run() {
                this.f$0.lambda$loadExceptions$1(arrayList319, arrayList318, arrayList3110, arrayList12, arrayList13, arrayList14, arrayList29, arrayList3111, runnable);
            }
        });
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$loadExceptions$1(ArrayList arrayList, ArrayList arrayList2, ArrayList arrayList3, ArrayList arrayList4, ArrayList arrayList5, ArrayList arrayList6, ArrayList arrayList7, ArrayList arrayList8, Runnable runnable) {
        MessagesController.getInstance(this.currentAccount).putUsers(arrayList, true);
        MessagesController.getInstance(this.currentAccount).putChats(arrayList2, true);
        MessagesController.getInstance(this.currentAccount).putEncryptedChats(arrayList3, true);
        this.exceptionUsers = arrayList4;
        this.exceptionChats = arrayList5;
        this.exceptionChannels = arrayList6;
        this.exceptionStories = arrayList7;
        this.exceptionAutoStories = arrayList8;
        ListAdapter listAdapter = this.adapter;
        if (listAdapter != null) {
            listAdapter.notifyItemChanged(this.privateRow);
            this.adapter.notifyItemChanged(this.groupRow);
            this.adapter.notifyItemChanged(this.channelsRow);
            this.adapter.notifyItemChanged(this.storiesRow);
        }
        if (runnable != null) {
            runnable.run();
        }
    }

    public NotificationsCustomSettingsActivity makeNotificationsCustomSettingsActivity(int i) {
        ArrayList arrayList;
        ArrayList arrayList2;
        ArrayList arrayList3 = null;
        if (i == 1) {
            arrayList = this.exceptionUsers;
        } else if (i == 0) {
            arrayList = this.exceptionChats;
        } else {
            if (i == 4) {
                arrayList2 = null;
            } else if (i == 3) {
                arrayList3 = this.exceptionStories;
                arrayList2 = this.exceptionAutoStories;
            } else {
                arrayList = this.exceptionChannels;
            }
            return new NotificationsCustomSettingsActivity(i, arrayList3, arrayList2);
        }
        arrayList3 = arrayList;
        arrayList2 = null;
        return new NotificationsCustomSettingsActivity(i, arrayList3, arrayList2);
    }

    @Override // org.telegram.ui.ActionBar.BaseFragment
    public void onFragmentDestroy() {
        super.onFragmentDestroy();
        NotificationCenter.getInstance(this.currentAccount).removeObserver(this, NotificationCenter.notificationsSettingsUpdated);
    }

    @Override // org.telegram.ui.ActionBar.BaseFragment
    public View createView(Context context) {
        this.actionBar.setBackButtonImage(R.drawable.ic_ab_back);
        this.actionBar.setAllowOverlayTitle(true);
        this.actionBar.setTitle(LocaleController.getString(R.string.NotificationsAndSounds));
        this.actionBar.setActionBarMenuOnItemClick(new ActionBar.ActionBarMenuOnItemClick() { // from class: org.telegram.ui.NotificationsSettingsActivity.1
            @Override // org.telegram.ui.ActionBar.ActionBar.ActionBarMenuOnItemClick
            public void onItemClick(int i) {
                if (i == -1) {
                    NotificationsSettingsActivity.this.finishFragment();
                }
            }
        });
        FrameLayout frameLayout = new FrameLayout(context);
        this.fragmentView = frameLayout;
        frameLayout.setBackgroundColor(Theme.getColor(Theme.key_windowBackgroundGray));
        RecyclerListView recyclerListView = new RecyclerListView(context);
        this.listView = recyclerListView;
        recyclerListView.setSections();
        this.actionBar.setAdaptiveBackground(this.listView);
        this.listView.setItemAnimator(null);
        this.listView.setLayoutAnimation(null);
        RecyclerListView recyclerListView2 = this.listView;
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(context, 1, false) { // from class: org.telegram.ui.NotificationsSettingsActivity.2
            @Override // androidx.recyclerview.widget.LinearLayoutManager, androidx.recyclerview.widget.RecyclerView.LayoutManager
            public boolean supportsPredictiveItemAnimations() {
                return false;
            }
        };
        this.layoutManager = linearLayoutManager;
        recyclerListView2.setLayoutManager(linearLayoutManager);
        this.listView.setVerticalScrollBarEnabled(false);
        frameLayout.addView(this.listView, LayoutHelper.createFrame(-1, -1.0f));
        RecyclerListView recyclerListView3 = this.listView;
        ListAdapter listAdapter = new ListAdapter(context);
        this.adapter = listAdapter;
        recyclerListView3.setAdapter(listAdapter);
        this.listView.setOnItemClickListener(new RecyclerListView.OnItemClickListenerExtended() { // from class: org.telegram.ui.NotificationsSettingsActivity$$ExternalSyntheticLambda0
            @Override // org.telegram.ui.Components.RecyclerListView.OnItemClickListenerExtended
            public /* synthetic */ boolean hasDoubleTap(View view, int i) {
                return RecyclerListView.OnItemClickListenerExtended.CC.$default$hasDoubleTap(this, view, i);
            }

            @Override // org.telegram.ui.Components.RecyclerListView.OnItemClickListenerExtended
            public /* synthetic */ void onDoubleTap(View view, int i, float f, float f2) {
                RecyclerListView.OnItemClickListenerExtended.CC.$default$onDoubleTap(this, view, i, f, f2);
            }

            @Override // org.telegram.ui.Components.RecyclerListView.OnItemClickListenerExtended
            public final void onItemClick(View view, int i, float f, float f2) {
                this.f$0.lambda$createView$10(view, i, f, f2);
            }
        });
        return this.fragmentView;
    }

    /* JADX INFO: Access modifiers changed from: private */
    /* JADX WARN: Code duplicated, block: B:141:0x04ac  */
    /* JADX WARN: Code duplicated, block: B:149:? A[RETURN, SYNTHETIC] */
    public /* synthetic */ void lambda$createView$10(View view, final int i, float f, float f2) {
        ArrayList arrayList;
        final boolean zIsGlobalNotificationsEnabled;
        final int i2;
        if (getParentActivity() == null) {
            return;
        }
        int i3 = this.privateRow;
        ArrayList arrayList2 = null;
        parcelable = null;
        Parcelable parcelable = null;
        z = false;
        boolean z = false;
        if (i != i3 && i != this.groupRow && i != this.channelsRow && i != this.storiesRow && i != this.reactionsRow) {
            if (i == this.callsRingtoneRow) {
                try {
                    SharedPreferences notificationsSettings = MessagesController.getNotificationsSettings(this.currentAccount);
                    Intent intent = new Intent("android.intent.action.RINGTONE_PICKER");
                    intent.putExtra("android.intent.extra.ringtone.TYPE", 1);
                    intent.putExtra("android.intent.extra.ringtone.SHOW_DEFAULT", true);
                    intent.putExtra("android.intent.extra.ringtone.SHOW_SILENT", true);
                    intent.putExtra("android.intent.extra.ringtone.DEFAULT_URI", RingtoneManager.getDefaultUri(1));
                    Uri uri = Settings.System.DEFAULT_RINGTONE_URI;
                    String path = uri != null ? uri.getPath() : null;
                    String string = notificationsSettings.getString("CallsRingtonePath", path);
                    if (string != null && !string.equals("NoSound")) {
                        parcelable = string.equals(path) ? uri : Uri.parse(string);
                    }
                    intent.putExtra("android.intent.extra.ringtone.EXISTING_URI", parcelable);
                    startActivityForResult(intent, i);
                } catch (Exception e) {
                    FileLog.e(e);
                }
            } else if (i == this.resetNotificationsRow) {
                AlertDialog.Builder builder = new AlertDialog.Builder(getParentActivity());
                builder.setTitle(LocaleController.getString("ResetNotificationsAlertTitle", R.string.ResetNotificationsAlertTitle));
                builder.setMessage(LocaleController.getString("ResetNotificationsAlert", R.string.ResetNotificationsAlert));
                builder.setPositiveButton(LocaleController.getString("Reset", R.string.Reset), new AlertDialog.OnButtonClickListener() { // from class: org.telegram.ui.NotificationsSettingsActivity$$ExternalSyntheticLambda5
                    @Override // org.telegram.ui.ActionBar.AlertDialog.OnButtonClickListener
                    public final void onClick(AlertDialog alertDialog, int i4) {
                        this.f$0.lambda$createView$6(alertDialog, i4);
                    }
                });
                builder.setNegativeButton(LocaleController.getString("Cancel", R.string.Cancel), null);
                AlertDialog alertDialogCreate = builder.create();
                showDialog(alertDialogCreate);
                TextView textView = (TextView) alertDialogCreate.getButton(-1);
                if (textView != null) {
                    textView.setTextColor(Theme.getColor(Theme.key_text_RedBold));
                }
            } else {
                if (i == this.inappSoundRow) {
                    SharedPreferences notificationsSettings2 = MessagesController.getNotificationsSettings(this.currentAccount);
                    SharedPreferences.Editor editorEdit = notificationsSettings2.edit();
                    z = notificationsSettings2.getBoolean("EnableInAppSounds", true);
                    editorEdit.putBoolean("EnableInAppSounds", !z);
                    editorEdit.apply();
                } else if (i == this.inappVibrateRow) {
                    SharedPreferences notificationsSettings3 = MessagesController.getNotificationsSettings(this.currentAccount);
                    SharedPreferences.Editor editorEdit2 = notificationsSettings3.edit();
                    z = notificationsSettings3.getBoolean("EnableInAppVibrate", true);
                    editorEdit2.putBoolean("EnableInAppVibrate", !z);
                    editorEdit2.apply();
                } else if (i == this.inappPreviewRow) {
                    SharedPreferences notificationsSettings4 = MessagesController.getNotificationsSettings(this.currentAccount);
                    SharedPreferences.Editor editorEdit3 = notificationsSettings4.edit();
                    z = notificationsSettings4.getBoolean("EnableInAppPreview", true);
                    editorEdit3.putBoolean("EnableInAppPreview", !z);
                    editorEdit3.apply();
                } else if (i == this.inchatSoundRow) {
                    SharedPreferences notificationsSettings5 = MessagesController.getNotificationsSettings(this.currentAccount);
                    SharedPreferences.Editor editorEdit4 = notificationsSettings5.edit();
                    z = notificationsSettings5.getBoolean("EnableInChatSound", true);
                    editorEdit4.putBoolean("EnableInChatSound", !z);
                    editorEdit4.apply();
                    getNotificationsController().setInChatSoundEnabled(!z);
                } else if (i == this.inappPriorityRow) {
                    SharedPreferences notificationsSettings6 = MessagesController.getNotificationsSettings(this.currentAccount);
                    SharedPreferences.Editor editorEdit5 = notificationsSettings6.edit();
                    z = notificationsSettings6.getBoolean("EnableInAppPopup", true);
                    editorEdit5.putBoolean("EnableInAppPopup", !z);
                    editorEdit5.apply();
                } else if (i == this.contactJoinedRow) {
                    SharedPreferences notificationsSettings7 = MessagesController.getNotificationsSettings(this.currentAccount);
                    SharedPreferences.Editor editorEdit6 = notificationsSettings7.edit();
                    z = notificationsSettings7.getBoolean("EnableContactJoined", true);
                    MessagesController.getInstance(this.currentAccount).enableJoined = !z;
                    editorEdit6.putBoolean("EnableContactJoined", !z);
                    editorEdit6.apply();
                    TL_account.setContactSignUpNotification setcontactsignupnotification = new TL_account.setContactSignUpNotification();
                    setcontactsignupnotification.silent = z;
                    ConnectionsManager.getInstance(this.currentAccount).sendRequest(setcontactsignupnotification, new RequestDelegate() { // from class: org.telegram.ui.NotificationsSettingsActivity$$ExternalSyntheticLambda6
                        @Override // org.telegram.tgnet.RequestDelegate
                        public final void run(TLObject tLObject, TLRPC.TL_error tL_error) {
                            NotificationsSettingsActivity.$r8$lambda$z8728ZbQxis9p3M0Wak40C7BT78(tLObject, tL_error);
                        }
                    });
                } else if (i == this.pinnedMessageRow) {
                    SharedPreferences notificationsSettings8 = MessagesController.getNotificationsSettings(this.currentAccount);
                    SharedPreferences.Editor editorEdit7 = notificationsSettings8.edit();
                    z = notificationsSettings8.getBoolean("PinnedMessages", true);
                    editorEdit7.putBoolean("PinnedMessages", !z);
                    editorEdit7.apply();
                } else if (i == this.androidAutoAlertRow) {
                    SharedPreferences notificationsSettings9 = MessagesController.getNotificationsSettings(this.currentAccount);
                    SharedPreferences.Editor editorEdit8 = notificationsSettings9.edit();
                    z = notificationsSettings9.getBoolean("EnableAutoNotifications", false);
                    editorEdit8.putBoolean("EnableAutoNotifications", !z);
                    editorEdit8.apply();
                } else if (i == this.badgeNumberShowRow) {
                    SharedPreferences.Editor editorEdit9 = MessagesController.getNotificationsSettings(this.currentAccount).edit();
                    z = getNotificationsController().showBadgeNumber;
                    getNotificationsController().showBadgeNumber = !z;
                    editorEdit9.putBoolean("badgeNumber", getNotificationsController().showBadgeNumber);
                    editorEdit9.apply();
                    getNotificationsController().updateBadge();
                } else if (i == this.badgeNumberMutedRow) {
                    SharedPreferences.Editor editorEdit10 = MessagesController.getNotificationsSettings(this.currentAccount).edit();
                    z = getNotificationsController().showBadgeMuted;
                    getNotificationsController().showBadgeMuted = !z;
                    editorEdit10.putBoolean("badgeNumberMuted", getNotificationsController().showBadgeMuted);
                    editorEdit10.apply();
                    getNotificationsController().updateBadge();
                    getMessagesStorage().updateMutedDialogsFiltersCounters();
                } else if (i == this.badgeNumberMessagesRow) {
                    SharedPreferences.Editor editorEdit11 = MessagesController.getNotificationsSettings(this.currentAccount).edit();
                    z = getNotificationsController().showBadgeMessages;
                    getNotificationsController().showBadgeMessages = !z;
                    editorEdit11.putBoolean("badgeNumberMessages", getNotificationsController().showBadgeMessages);
                    editorEdit11.apply();
                    getNotificationsController().updateBadge();
                } else if (i == this.notificationsServiceConnectionRow) {
                    SharedPreferences notificationsSettings10 = MessagesController.getNotificationsSettings(this.currentAccount);
                    boolean z2 = notificationsSettings10.getBoolean("pushConnection", getMessagesController().backgroundConnection);
                    SharedPreferences.Editor editorEdit12 = notificationsSettings10.edit();
                    editorEdit12.putBoolean("pushConnection", !z2);
                    editorEdit12.apply();
                    if (!z2) {
                        ConnectionsManager.getInstance(this.currentAccount).setPushConnectionEnabled(true);
                    } else {
                        ConnectionsManager.getInstance(this.currentAccount).setPushConnectionEnabled(false);
                    }
                    z = z2;
                } else if (i == this.accountsAllRow) {
                    SharedPreferences globalNotificationsSettings = MessagesController.getGlobalNotificationsSettings();
                    zIsGlobalNotificationsEnabled = globalNotificationsSettings.getBoolean("AllAccounts", true);
                    SharedPreferences.Editor editorEdit13 = globalNotificationsSettings.edit();
                    editorEdit13.putBoolean("AllAccounts", !zIsGlobalNotificationsEnabled);
                    editorEdit13.apply();
                    SharedConfig.showNotificationsForAllAccounts = !zIsGlobalNotificationsEnabled;
                    for (int i4 = 0; i4 < 16; i4++) {
                        if (SharedConfig.showNotificationsForAllAccounts) {
                            NotificationsController.getInstance(i4).showNotifications();
                        } else if (i4 == this.currentAccount) {
                            NotificationsController.getInstance(i4).showNotifications();
                        } else {
                            NotificationsController.getInstance(i4).hideNotifications();
                        }
                    }
                } else if (i == this.notificationsServiceRow) {
                    SharedPreferences notificationsSettings11 = MessagesController.getNotificationsSettings(this.currentAccount);
                    z = notificationsSettings11.getBoolean("pushService", getMessagesController().keepAliveService);
                    SharedPreferences.Editor editorEdit14 = notificationsSettings11.edit();
                    editorEdit14.putBoolean("pushService", !z);
                    editorEdit14.apply();
                    ApplicationLoader.startPushService();
                } else if (i == this.callsVibrateRow) {
                    if (getParentActivity() == null) {
                        return;
                    } else {
                        showDialog(AlertsCreator.createVibrationSelectDialog(getParentActivity(), 0L, 0L, i == this.callsVibrateRow ? "vibrate_calls" : null, new Runnable() { // from class: org.telegram.ui.NotificationsSettingsActivity$$ExternalSyntheticLambda7
                            @Override // java.lang.Runnable
                            public final void run() {
                                this.f$0.lambda$createView$8(i);
                            }
                        }));
                    }
                } else if (i == this.repeatRow) {
                    AlertDialog.Builder builder2 = new AlertDialog.Builder(getParentActivity());
                    builder2.setTitle(LocaleController.getString("RepeatNotifications", R.string.RepeatNotifications));
                    builder2.setItems(new CharSequence[]{LocaleController.getString("RepeatDisabled", R.string.RepeatDisabled), LocaleController.formatPluralString("Minutes", 5, new Object[0]), LocaleController.formatPluralString("Minutes", 10, new Object[0]), LocaleController.formatPluralString("Minutes", 30, new Object[0]), LocaleController.formatPluralString("Hours", 1, new Object[0]), LocaleController.formatPluralString("Hours", 2, new Object[0]), LocaleController.formatPluralString("Hours", 4, new Object[0])}, new DialogInterface.OnClickListener() { // from class: org.telegram.ui.NotificationsSettingsActivity$$ExternalSyntheticLambda8
                        @Override // android.content.DialogInterface.OnClickListener
                        public final void onClick(DialogInterface dialogInterface, int i5) {
                            this.f$0.lambda$createView$9(i, dialogInterface, i5);
                        }
                    });
                    builder2.setNegativeButton(LocaleController.getString("Cancel", R.string.Cancel), null);
                    showDialog(builder2.create());
                }
                if (view instanceof TextCheckCell) {
                    ((TextCheckCell) view).setChecked(!z);
                }
            }
            if (view instanceof TextCheckCell) {
                ((TextCheckCell) view).setChecked(!z);
            }
        }
        if (i == i3) {
            arrayList2 = this.exceptionUsers;
            arrayList = null;
            zIsGlobalNotificationsEnabled = getNotificationsController().isGlobalNotificationsEnabled(1);
            i2 = 1;
        } else if (i == this.groupRow) {
            arrayList2 = this.exceptionChats;
            arrayList = null;
            zIsGlobalNotificationsEnabled = getNotificationsController().isGlobalNotificationsEnabled(0);
            i2 = 0;
        } else if (i == this.storiesRow) {
            arrayList2 = this.exceptionStories;
            arrayList = this.exceptionAutoStories;
            zIsGlobalNotificationsEnabled = getNotificationsSettings().getBoolean("EnableAllStories", false);
            i2 = 3;
        } else if (i == this.reactionsRow) {
            i2 = 4;
            arrayList = null;
            zIsGlobalNotificationsEnabled = getNotificationsSettings().getBoolean("EnableReactionsMessages", true) || getNotificationsSettings().getBoolean("EnableReactionsStories", true);
        } else {
            arrayList2 = this.exceptionChannels;
            arrayList = null;
            zIsGlobalNotificationsEnabled = getNotificationsController().isGlobalNotificationsEnabled(2);
            i2 = 2;
        }
        if (arrayList2 == null && i2 != 4) {
            return;
        }
        final NotificationsCheckCell notificationsCheckCell = (NotificationsCheckCell) view;
        if ((LocaleController.isRTL && f <= AndroidUtilities.dp(76.0f)) || (!LocaleController.isRTL && f >= view.getMeasuredWidth() - AndroidUtilities.dp(76.0f))) {
            showExceptionsAlert(i, new Runnable() { // from class: org.telegram.ui.NotificationsSettingsActivity$$ExternalSyntheticLambda4
                @Override // java.lang.Runnable
                public final void run() {
                    this.f$0.lambda$createView$3(i2, zIsGlobalNotificationsEnabled, notificationsCheckCell, i);
                }
            });
        } else {
            presentFragment(new NotificationsCustomSettingsActivity(i2, arrayList2, arrayList));
        }
        z = zIsGlobalNotificationsEnabled;
        if (view instanceof TextCheckCell) {
            ((TextCheckCell) view).setChecked(!z);
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$createView$3(int i, boolean z, NotificationsCheckCell notificationsCheckCell, int i2) {
        if (i == 3) {
            SharedPreferences.Editor editorEdit = getNotificationsSettings().edit();
            if (z) {
                editorEdit.remove("EnableAllStories");
            } else {
                editorEdit.putBoolean("EnableAllStories", true);
            }
            editorEdit.apply();
            getNotificationsController().updateServerNotificationsSettings(i);
        } else if (i == 4 || i == 5) {
            SharedPreferences.Editor editorEdit2 = getNotificationsSettings().edit();
            if (z) {
                editorEdit2.putBoolean("EnableReactionsMessages", false);
                editorEdit2.putBoolean("EnableReactionsStories", false);
            } else {
                editorEdit2.putBoolean("EnableReactionsMessages", true);
                editorEdit2.putBoolean("EnableReactionsStories", true);
            }
            editorEdit2.apply();
            getNotificationsController().updateServerNotificationsSettings(i);
            getNotificationsController().deleteNotificationChannelGlobal(i);
        } else {
            getNotificationsController().setGlobalNotificationsEnabled(i, !z ? 0 : Integer.MAX_VALUE);
        }
        notificationsCheckCell.setChecked(!z, 0);
        this.adapter.notifyItemChanged(i2);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$createView$6(AlertDialog alertDialog, int i) {
        if (this.reseting) {
            return;
        }
        this.reseting = true;
        ConnectionsManager.getInstance(this.currentAccount).sendRequest(new TL_account.resetNotifySettings(), new RequestDelegate() { // from class: org.telegram.ui.NotificationsSettingsActivity$$ExternalSyntheticLambda11
            @Override // org.telegram.tgnet.RequestDelegate
            public final void run(TLObject tLObject, TLRPC.TL_error tL_error) {
                this.f$0.lambda$createView$5(tLObject, tL_error);
            }
        });
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$createView$5(TLObject tLObject, TLRPC.TL_error tL_error) {
        AndroidUtilities.runOnUIThread(new Runnable() { // from class: org.telegram.ui.NotificationsSettingsActivity$$ExternalSyntheticLambda12
            @Override // java.lang.Runnable
            public final void run() {
                this.f$0.lambda$createView$4();
            }
        });
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$createView$4() {
        getMessagesController().enableJoined = true;
        this.reseting = false;
        SharedPreferences.Editor editorEdit = MessagesController.getNotificationsSettings(this.currentAccount).edit();
        editorEdit.clear();
        editorEdit.apply();
        this.exceptionChats.clear();
        this.exceptionUsers.clear();
        this.adapter.notifyDataSetChanged();
        if (getParentActivity() != null) {
            Toast.makeText(getParentActivity(), LocaleController.getString("ResetNotificationsText", R.string.ResetNotificationsText), 0).show();
        }
        getMessagesStorage().updateMutedDialogsFiltersCounters();
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$createView$8(int i) {
        this.updateVibrate = true;
        this.adapter.notifyItemChanged(i);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$createView$9(int i, DialogInterface dialogInterface, int i2) {
        int i3 = 5;
        if (i2 != 1) {
            if (i2 == 2) {
                i3 = 10;
            } else if (i2 == 3) {
                i3 = 30;
            } else if (i2 == 4) {
                i3 = 60;
            } else if (i2 == 5) {
                i3 = 120;
            } else {
                i3 = i2 == 6 ? 240 : 0;
            }
        }
        MessagesController.getNotificationsSettings(this.currentAccount).edit().putInt("repeat_messages", i3).apply();
        this.updateRepeatNotifications = true;
        this.adapter.notifyItemChanged(i);
    }

    @Override // org.telegram.ui.ActionBar.BaseFragment
    public void onActivityResultFragment(int i, int i2, Intent intent) {
        String title;
        Ringtone ringtone;
        super.onActivityResultFragment(i, i2, intent);
        if (i2 == -1) {
            Uri uri = (Uri) intent.getParcelableExtra("android.intent.extra.ringtone.PICKED_URI");
            if (uri == null || (ringtone = RingtoneManager.getRingtone(getParentActivity(), uri)) == null) {
                title = null;
            } else {
                if (i == this.callsRingtoneRow) {
                    if (uri.equals(Settings.System.DEFAULT_RINGTONE_URI)) {
                        title = LocaleController.getString("DefaultRingtone", R.string.DefaultRingtone);
                    } else {
                        title = ringtone.getTitle(getParentActivity());
                    }
                } else if (uri.equals(Settings.System.DEFAULT_NOTIFICATION_URI)) {
                    title = LocaleController.getString("SoundDefault", R.string.SoundDefault);
                } else {
                    title = ringtone.getTitle(getParentActivity());
                }
                ringtone.stop();
            }
            SharedPreferences.Editor editorEdit = MessagesController.getNotificationsSettings(this.currentAccount).edit();
            if (i == this.callsRingtoneRow) {
                if (title != null && uri != null) {
                    editorEdit.putString("CallsRingtone", title);
                    editorEdit.putString("CallsRingtonePath", uri.toString());
                } else {
                    editorEdit.putString("CallsRingtone", "NoSound");
                    editorEdit.putString("CallsRingtonePath", "NoSound");
                }
                this.updateRingtone = true;
            }
            editorEdit.apply();
            this.adapter.notifyItemChanged(i);
        }
    }

    /* JADX WARN: Code duplicated, block: B:17:0x003b A[PHI: r8
  0x003b: PHI (r8v3 java.util.ArrayList) = 
  (r8v1 java.util.ArrayList)
  (r8v1 java.util.ArrayList)
  (r8v2 java.util.ArrayList)
  (r8v2 java.util.ArrayList)
  (r8v5 java.util.ArrayList)
  (r8v5 java.util.ArrayList)
 binds: [B:30:0x0062, B:32:0x0068, B:21:0x0043, B:23:0x0049, B:12:0x0025, B:14:0x002b] A[DONT_GENERATE, DONT_INLINE]] */
    private void showExceptionsAlert(int i, final Runnable runnable) {
        final ArrayList arrayList;
        String pluralString;
        final ArrayList arrayList2;
        String pluralString2 = null;
        if (i == this.storiesRow) {
            arrayList = this.exceptionStories;
            arrayList2 = this.exceptionAutoStories;
            if (arrayList != null && !arrayList.isEmpty()) {
                pluralString2 = LocaleController.formatPluralString("ChatsException", arrayList.size(), new Object[0]);
            }
        } else if (i == this.privateRow) {
            arrayList = this.exceptionUsers;
            if (arrayList == null || arrayList.isEmpty()) {
                arrayList2 = null;
            } else {
                pluralString = LocaleController.formatPluralString("ChatsException", arrayList.size(), new Object[0]);
                pluralString2 = pluralString;
                arrayList2 = null;
            }
        } else if (i == this.groupRow) {
            arrayList = this.exceptionChats;
            if (arrayList == null || arrayList.isEmpty()) {
                arrayList2 = null;
            } else {
                pluralString = LocaleController.formatPluralString("Groups", arrayList.size(), new Object[0]);
                pluralString2 = pluralString;
                arrayList2 = null;
            }
        } else {
            if (i == this.reactionsRow) {
                runnable.run();
                return;
            }
            arrayList = this.exceptionChannels;
            if (arrayList == null || arrayList.isEmpty()) {
                arrayList2 = null;
            } else {
                pluralString = LocaleController.formatPluralString("Channels", arrayList.size(), new Object[0]);
                pluralString2 = pluralString;
                arrayList2 = null;
            }
        }
        if (pluralString2 == null) {
            runnable.run();
            return;
        }
        AlertDialog.Builder builder = new AlertDialog.Builder(getParentActivity());
        if (arrayList.size() == 1) {
            builder.setMessage(AndroidUtilities.replaceTags(LocaleController.formatString(R.string.NotificationsExceptionsSingleAlert, pluralString2)));
        } else {
            builder.setMessage(AndroidUtilities.replaceTags(LocaleController.formatString(R.string.NotificationsExceptionsAlert, pluralString2)));
        }
        builder.setTitle(LocaleController.getString("NotificationsExceptions", R.string.NotificationsExceptions));
        builder.setNeutralButton(LocaleController.getString("ViewExceptions", R.string.ViewExceptions), new AlertDialog.OnButtonClickListener() { // from class: org.telegram.ui.NotificationsSettingsActivity$$ExternalSyntheticLambda9
            @Override // org.telegram.ui.ActionBar.AlertDialog.OnButtonClickListener
            public final void onClick(AlertDialog alertDialog, int i2) {
                this.f$0.lambda$showExceptionsAlert$11(arrayList, arrayList2, alertDialog, i2);
            }
        });
        builder.setNegativeButton(LocaleController.getString("OK", R.string.OK), new AlertDialog.OnButtonClickListener() { // from class: org.telegram.ui.NotificationsSettingsActivity$$ExternalSyntheticLambda10
            @Override // org.telegram.ui.ActionBar.AlertDialog.OnButtonClickListener
            public final void onClick(AlertDialog alertDialog, int i2) {
                runnable.run();
            }
        });
        showDialog(builder.create());
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$showExceptionsAlert$11(ArrayList arrayList, ArrayList arrayList2, AlertDialog alertDialog, int i) {
        presentFragment(new NotificationsCustomSettingsActivity(-1, arrayList, arrayList2));
    }

    @Override // org.telegram.ui.ActionBar.BaseFragment
    public void onResume() {
        super.onResume();
        ListAdapter listAdapter = this.adapter;
        if (listAdapter != null) {
            listAdapter.notifyDataSetChanged();
        }
    }

    @Override // org.telegram.messenger.NotificationCenter.NotificationCenterDelegate
    public void didReceivedNotification(int i, int i2, Object... objArr) {
        if (i == NotificationCenter.notificationsSettingsUpdated) {
            this.adapter.notifyDataSetChanged();
        }
    }

    private class ListAdapter extends RecyclerListView.SelectionAdapter {
        private Context mContext;

        public ListAdapter(Context context) {
            this.mContext = context;
        }

        @Override // org.telegram.ui.Components.RecyclerListView.SelectionAdapter
        public boolean isEnabled(RecyclerView.ViewHolder viewHolder) {
            int adapterPosition = viewHolder.getAdapterPosition();
            return (adapterPosition == NotificationsSettingsActivity.this.notificationsSectionRow || adapterPosition == NotificationsSettingsActivity.this.notificationsSection2Row || adapterPosition == NotificationsSettingsActivity.this.inappSectionRow || adapterPosition == NotificationsSettingsActivity.this.eventsSectionRow || adapterPosition == NotificationsSettingsActivity.this.otherSectionRow || adapterPosition == NotificationsSettingsActivity.this.resetSectionRow || adapterPosition == NotificationsSettingsActivity.this.badgeNumberSection || adapterPosition == NotificationsSettingsActivity.this.otherSection2Row || adapterPosition == NotificationsSettingsActivity.this.resetSection2Row || adapterPosition == NotificationsSettingsActivity.this.callsSection2Row || adapterPosition == NotificationsSettingsActivity.this.callsSectionRow || adapterPosition == NotificationsSettingsActivity.this.badgeNumberSection2Row || adapterPosition == NotificationsSettingsActivity.this.accountsSectionRow || adapterPosition == NotificationsSettingsActivity.this.accountsInfoRow || adapterPosition == NotificationsSettingsActivity.this.resetNotificationsSectionRow || adapterPosition == NotificationsSettingsActivity.this.eventsSection2Row) ? false : true;
        }

        @Override // androidx.recyclerview.widget.RecyclerView.Adapter
        public int getItemCount() {
            return NotificationsSettingsActivity.this.rowCount;
        }

        @Override // androidx.recyclerview.widget.RecyclerView.Adapter
        public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
            View headerCell;
            if (i == 0) {
                headerCell = new HeaderCell(this.mContext, ((BaseFragment) NotificationsSettingsActivity.this).resourceProvider);
            } else if (i == 1) {
                headerCell = new TextCheckCell(this.mContext, ((BaseFragment) NotificationsSettingsActivity.this).resourceProvider);
            } else if (i == 2) {
                headerCell = new TextDetailSettingsCell(this.mContext);
            } else if (i == 3) {
                headerCell = new NotificationsCheckCell(this.mContext, 21, 64, true, ((BaseFragment) NotificationsSettingsActivity.this).resourceProvider);
            } else if (i == 4) {
                headerCell = new ShadowSectionCell(this.mContext, ((BaseFragment) NotificationsSettingsActivity.this).resourceProvider);
            } else if (i == 5) {
                headerCell = new TextSettingsCell(this.mContext, ((BaseFragment) NotificationsSettingsActivity.this).resourceProvider);
            } else {
                headerCell = new TextInfoPrivacyCell(this.mContext, ((BaseFragment) NotificationsSettingsActivity.this).resourceProvider);
            }
            return new RecyclerListView.Holder(headerCell);
        }

        /* JADX WARN: Code duplicated, block: B:100:0x0289  */
        /* JADX WARN: Code duplicated, block: B:124:0x02fb A[DONT_INVERT] */
        /* JADX WARN: Code duplicated, block: B:125:0x02fd  */
        /* JADX WARN: Code duplicated, block: B:135:0x0338  */
        /* JADX WARN: Code duplicated, block: B:138:0x034d  */
        /* JADX WARN: Code duplicated, block: B:139:0x034f  */
        /* JADX WARN: Code duplicated, block: B:77:0x0224  */
        /* JADX WARN: Code duplicated, block: B:78:0x0227  */
        /* JADX WARN: Code duplicated, block: B:81:0x022e  */
        /* JADX WARN: Code duplicated, block: B:88:0x024f A[DONT_INVERT] */
        /* JADX WARN: Code duplicated, block: B:89:0x0251  */
        /* JADX WARN: Code duplicated, block: B:90:0x025e  */
        /* JADX WARN: Code duplicated, block: B:92:0x0265  */
        /* JADX WARN: Code duplicated, block: B:95:0x0274  */
        /* JADX WARN: Code duplicated, block: B:97:0x027a  */
        /* JADX WARN: Multi-variable type inference failed */
        @Override // androidx.recyclerview.widget.RecyclerView.Adapter
        public void onBindViewHolder(RecyclerView.ViewHolder viewHolder, int i) {
            CharSequence string;
            ArrayList arrayList;
            int i2;
            int i3;
            int i4;
            ArrayList arrayList2;
            int i5;
            int i6;
            int i7;
            StringBuilder sb;
            boolean z;
            boolean z2;
            String pluralString;
            int itemViewType = viewHolder.getItemViewType();
            if (itemViewType == 0) {
                HeaderCell headerCell = (HeaderCell) viewHolder.itemView;
                if (i == NotificationsSettingsActivity.this.notificationsSectionRow) {
                    headerCell.setText(LocaleController.getString("NotificationsForChats", R.string.NotificationsForChats));
                    return;
                }
                if (i == NotificationsSettingsActivity.this.inappSectionRow) {
                    headerCell.setText(LocaleController.getString("InAppNotifications", R.string.InAppNotifications));
                    return;
                }
                if (i == NotificationsSettingsActivity.this.eventsSectionRow) {
                    headerCell.setText(LocaleController.getString("Events", R.string.Events));
                    return;
                }
                if (i == NotificationsSettingsActivity.this.otherSectionRow) {
                    headerCell.setText(LocaleController.getString("NotificationsOther", R.string.NotificationsOther));
                    return;
                }
                if (i == NotificationsSettingsActivity.this.resetSectionRow) {
                    headerCell.setText(LocaleController.getString("Reset", R.string.Reset));
                    return;
                }
                if (i == NotificationsSettingsActivity.this.callsSectionRow) {
                    headerCell.setText(LocaleController.getString("VoipNotificationSettings", R.string.VoipNotificationSettings));
                    return;
                } else if (i == NotificationsSettingsActivity.this.badgeNumberSection) {
                    headerCell.setText(LocaleController.getString("BadgeNumber", R.string.BadgeNumber));
                    return;
                } else {
                    if (i == NotificationsSettingsActivity.this.accountsSectionRow) {
                        headerCell.setText(LocaleController.getString("ShowNotificationsFor", R.string.ShowNotificationsFor));
                        return;
                    }
                    return;
                }
            }
            if (itemViewType == 1) {
                TextCheckCell textCheckCell = (TextCheckCell) viewHolder.itemView;
                SharedPreferences notificationsSettings = MessagesController.getNotificationsSettings(((BaseFragment) NotificationsSettingsActivity.this).currentAccount);
                if (i == NotificationsSettingsActivity.this.inappSoundRow) {
                    textCheckCell.setTextAndCheck(LocaleController.getString(R.string.InAppSounds), notificationsSettings.getBoolean("EnableInAppSounds", true), true);
                    return;
                }
                if (i == NotificationsSettingsActivity.this.inappVibrateRow) {
                    textCheckCell.setTextAndCheck(LocaleController.getString(R.string.InAppVibrate), notificationsSettings.getBoolean("EnableInAppVibrate", true), true);
                    return;
                }
                if (i == NotificationsSettingsActivity.this.inappPreviewRow) {
                    textCheckCell.setTextAndCheck(LocaleController.getString(R.string.InAppPreview), notificationsSettings.getBoolean("EnableInAppPreview", true), true);
                    return;
                }
                if (i == NotificationsSettingsActivity.this.inappPriorityRow) {
                    textCheckCell.setTextAndValueAndCheck(LocaleController.getString(R.string.InAppPopup), LocaleController.getString(R.string.InAppPopupInfo), notificationsSettings.getBoolean("EnableInAppPopup", true), true, false);
                    return;
                }
                if (i == NotificationsSettingsActivity.this.contactJoinedRow) {
                    textCheckCell.setTextAndCheck(LocaleController.getString("ContactJoined", R.string.ContactJoined), notificationsSettings.getBoolean("EnableContactJoined", true), true);
                    return;
                }
                if (i == NotificationsSettingsActivity.this.pinnedMessageRow) {
                    textCheckCell.setTextAndCheck(LocaleController.getString("PinnedMessages", R.string.PinnedMessages), notificationsSettings.getBoolean("PinnedMessages", true), false);
                    return;
                }
                if (i == NotificationsSettingsActivity.this.androidAutoAlertRow) {
                    textCheckCell.setTextAndCheck("Android Auto", notificationsSettings.getBoolean("EnableAutoNotifications", false), true);
                    return;
                }
                if (i == NotificationsSettingsActivity.this.notificationsServiceRow) {
                    textCheckCell.setTextAndValueAndCheck(LocaleController.getString("NotificationsService", R.string.NotificationsService), LocaleController.getString("NotificationsServiceInfo", R.string.NotificationsServiceInfo), notificationsSettings.getBoolean("pushService", NotificationsSettingsActivity.this.getMessagesController().keepAliveService), true, true);
                    return;
                }
                if (i == NotificationsSettingsActivity.this.notificationsServiceConnectionRow) {
                    textCheckCell.setTextAndValueAndCheck(LocaleController.getString("NotificationsServiceConnection", R.string.NotificationsServiceConnection), LocaleController.getString("NotificationsServiceConnectionInfo", R.string.NotificationsServiceConnectionInfo), notificationsSettings.getBoolean("pushConnection", NotificationsSettingsActivity.this.getMessagesController().backgroundConnection), true, true);
                    return;
                }
                if (i == NotificationsSettingsActivity.this.badgeNumberShowRow) {
                    textCheckCell.setTextAndCheck(LocaleController.getString("BadgeNumberShow", R.string.BadgeNumberShow), NotificationsSettingsActivity.this.getNotificationsController().showBadgeNumber, true);
                    return;
                }
                if (i == NotificationsSettingsActivity.this.badgeNumberMutedRow) {
                    textCheckCell.setTextAndCheck(LocaleController.getString("BadgeNumberMutedChats", R.string.BadgeNumberMutedChats), NotificationsSettingsActivity.this.getNotificationsController().showBadgeMuted, true);
                    return;
                }
                if (i == NotificationsSettingsActivity.this.badgeNumberMessagesRow) {
                    textCheckCell.setTextAndCheck(LocaleController.getString("BadgeNumberUnread", R.string.BadgeNumberUnread), NotificationsSettingsActivity.this.getNotificationsController().showBadgeMessages, false);
                    return;
                }
                if (i == NotificationsSettingsActivity.this.inchatSoundRow) {
                    textCheckCell.setTextAndCheck(LocaleController.getString("InChatSound", R.string.InChatSound), notificationsSettings.getBoolean("EnableInChatSound", true), true);
                    return;
                } else if (i == NotificationsSettingsActivity.this.callsVibrateRow) {
                    textCheckCell.setTextAndCheck(LocaleController.getString("Vibrate", R.string.Vibrate), notificationsSettings.getBoolean("EnableCallVibrate", true), true);
                    return;
                } else {
                    if (i == NotificationsSettingsActivity.this.accountsAllRow) {
                        textCheckCell.setTextAndCheck(LocaleController.getString("AllAccounts", R.string.AllAccounts), MessagesController.getGlobalNotificationsSettings().getBoolean("AllAccounts", true), false);
                        return;
                    }
                    return;
                }
            }
            if (itemViewType == 2) {
                TextDetailSettingsCell textDetailSettingsCell = (TextDetailSettingsCell) viewHolder.itemView;
                textDetailSettingsCell.setMultilineDetail(true);
                if (i == NotificationsSettingsActivity.this.resetNotificationsRow) {
                    textDetailSettingsCell.setTextAndValue(LocaleController.getString("ResetAllNotifications", R.string.ResetAllNotifications), LocaleController.getString("UndoAllCustom", R.string.UndoAllCustom), false);
                    return;
                }
                return;
            }
            if (itemViewType != 3) {
                if (itemViewType != 5) {
                    if (itemViewType != 6) {
                        return;
                    }
                    TextInfoPrivacyCell textInfoPrivacyCell = (TextInfoPrivacyCell) viewHolder.itemView;
                    if (i == NotificationsSettingsActivity.this.accountsInfoRow) {
                        textInfoPrivacyCell.setText(LocaleController.getString("ShowNotificationsForInfo", R.string.ShowNotificationsForInfo));
                        return;
                    }
                    return;
                }
                TextSettingsCell textSettingsCell = (TextSettingsCell) viewHolder.itemView;
                SharedPreferences notificationsSettings2 = MessagesController.getNotificationsSettings(((BaseFragment) NotificationsSettingsActivity.this).currentAccount);
                if (i == NotificationsSettingsActivity.this.callsRingtoneRow) {
                    String string2 = notificationsSettings2.getString("CallsRingtone", LocaleController.getString("DefaultRingtone", R.string.DefaultRingtone));
                    if (string2.equals("NoSound")) {
                        string2 = LocaleController.getString("NoSound", R.string.NoSound);
                    }
                    textSettingsCell.setTextAndValue(LocaleController.getString("VoipSettingsRingtone", R.string.VoipSettingsRingtone), string2, NotificationsSettingsActivity.this.updateRingtone, false);
                    NotificationsSettingsActivity.this.updateRingtone = false;
                    return;
                }
                if (i == NotificationsSettingsActivity.this.callsVibrateRow) {
                    int i8 = notificationsSettings2.getInt("vibrate_calls", 0);
                    if (i8 == 0) {
                        textSettingsCell.setTextAndValue(LocaleController.getString("Vibrate", R.string.Vibrate), LocaleController.getString("VibrationDefault", R.string.VibrationDefault), NotificationsSettingsActivity.this.updateVibrate, true);
                    } else if (i8 == 1) {
                        textSettingsCell.setTextAndValue(LocaleController.getString("Vibrate", R.string.Vibrate), LocaleController.getString("Short", R.string.Short), NotificationsSettingsActivity.this.updateVibrate, true);
                    } else if (i8 == 2) {
                        textSettingsCell.setTextAndValue(LocaleController.getString("Vibrate", R.string.Vibrate), LocaleController.getString("VibrationDisabled", R.string.VibrationDisabled), NotificationsSettingsActivity.this.updateVibrate, true);
                    } else if (i8 == 3) {
                        textSettingsCell.setTextAndValue(LocaleController.getString("Vibrate", R.string.Vibrate), LocaleController.getString("Long", R.string.Long), NotificationsSettingsActivity.this.updateVibrate, true);
                    } else if (i8 == 4) {
                        textSettingsCell.setTextAndValue(LocaleController.getString("Vibrate", R.string.Vibrate), LocaleController.getString("OnlyIfSilent", R.string.OnlyIfSilent), NotificationsSettingsActivity.this.updateVibrate, true);
                    }
                    NotificationsSettingsActivity.this.updateVibrate = false;
                    return;
                }
                if (i == NotificationsSettingsActivity.this.repeatRow) {
                    int i9 = notificationsSettings2.getInt("repeat_messages", 60);
                    if (i9 == 0) {
                        pluralString = LocaleController.getString("RepeatNotificationsNever", R.string.RepeatNotificationsNever);
                    } else if (i9 < 60) {
                        pluralString = LocaleController.formatPluralString("Minutes", i9, new Object[0]);
                    } else {
                        pluralString = LocaleController.formatPluralString("Hours", i9 / 60, new Object[0]);
                    }
                    textSettingsCell.setTextAndValue(LocaleController.getString("RepeatNotifications", R.string.RepeatNotifications), pluralString, NotificationsSettingsActivity.this.updateRepeatNotifications, false);
                    NotificationsSettingsActivity.this.updateRepeatNotifications = false;
                    return;
                }
                return;
            }
            NotificationsCheckCell notificationsCheckCell = (NotificationsCheckCell) viewHolder.itemView;
            SharedPreferences notificationsSettings3 = MessagesController.getNotificationsSettings(((BaseFragment) NotificationsSettingsActivity.this).currentAccount);
            int currentTime = ConnectionsManager.getInstance(((BaseFragment) NotificationsSettingsActivity.this).currentAccount).getCurrentTime();
            ArrayList arrayList3 = null;
            if (i == NotificationsSettingsActivity.this.privateRow) {
                string = LocaleController.getString(R.string.NotificationsPrivateChats);
                arrayList = NotificationsSettingsActivity.this.exceptionUsers;
                i2 = notificationsSettings3.getInt("EnableAll2", 0);
                i3 = R.drawable.msg_openprofile;
            } else if (i == NotificationsSettingsActivity.this.groupRow) {
                string = LocaleController.getString(R.string.NotificationsGroups);
                arrayList = NotificationsSettingsActivity.this.exceptionChats;
                i2 = notificationsSettings3.getInt("EnableGroup2", 0);
                i3 = R.drawable.msg_groups;
            } else {
                if (i == NotificationsSettingsActivity.this.storiesRow) {
                    string = LocaleController.getString(R.string.NotificationStories);
                    arrayList3 = NotificationsSettingsActivity.this.exceptionStories;
                    arrayList2 = NotificationsSettingsActivity.this.exceptionAutoStories;
                    i4 = notificationsSettings3.getBoolean("EnableAllStories", false) ? 0 : Integer.MAX_VALUE;
                    i3 = R.drawable.msg_menu_stories;
                } else if (i == NotificationsSettingsActivity.this.reactionsRow) {
                    string = LocaleController.getString(R.string.NotificationReactions);
                    int i10 = (notificationsSettings3.getBoolean("EnableReactionsMessages", true) || notificationsSettings3.getBoolean("EnableReactionsStories", true)) ? 0 : Integer.MAX_VALUE;
                    i3 = R.drawable.msg_reactions;
                    i4 = i10;
                    arrayList2 = null;
                } else {
                    string = LocaleController.getString(R.string.NotificationsChannels);
                    arrayList = NotificationsSettingsActivity.this.exceptionChannels;
                    i2 = notificationsSettings3.getInt("EnableChannel2", 0);
                    i3 = R.drawable.msg_channel;
                }
                if (i4 < currentTime) {
                    i5 = 1;
                } else {
                    i5 = 0;
                }
                if (i5 != 0 && i4 - 31536000 < currentTime) {
                    i6 = 0;
                    i7 = 2;
                } else {
                    i7 = 0;
                    i6 = 0;
                }
                sb = new StringBuilder();
                CharSequence charSequence = string;
                int i11 = i7;
                if (i == NotificationsSettingsActivity.this.reactionsRow) {
                    if (arrayList3 == null && !arrayList3.isEmpty()) {
                        i5 = i4 < currentTime ? 1 : i6;
                        if (i5 != 0) {
                            sb.append(LocaleController.getString("NotificationsOn", R.string.NotificationsOn));
                        } else if (i4 - 31536000 >= currentTime) {
                            sb.append(LocaleController.getString("NotificationsOff", R.string.NotificationsOff));
                        } else {
                            int i12 = R.string.NotificationsOffUntil;
                            Object[] objArr = new Object[1];
                            objArr[i6] = LocaleController.stringForMessageListDate(i4);
                            sb.append(LocaleController.formatString("NotificationsOffUntil", i12, objArr));
                        }
                        if (sb.length() != 0) {
                            sb.append(", ");
                        }
                        int size = arrayList3.size();
                        if (i == NotificationsSettingsActivity.this.storiesRow && !notificationsSettings3.contains("EnableAllStories") && arrayList2 != null) {
                            size += arrayList2.size();
                        }
                        sb.append(LocaleController.formatPluralString("Exception", size, new Object[i6]));
                    } else if (arrayList2 == null && !arrayList2.isEmpty()) {
                        if (i4 <= 0) {
                            sb.append(LocaleController.getString("NotificationsOn", R.string.NotificationsOn));
                        } else {
                            sb.append(LocaleController.getString("NotificationsOff", R.string.NotificationsOff));
                        }
                        if (!arrayList2.isEmpty() && !notificationsSettings3.contains("EnableAllStories")) {
                            sb.append(", ");
                            sb.append(LocaleController.formatPluralString("AutoException", arrayList2.size(), new Object[0]));
                        }
                    } else {
                        sb.append(LocaleController.getString("TapToChange", R.string.TapToChange));
                    }
                    z = i5;
                } else if (i4 > 0) {
                    sb.append(LocaleController.getString("NotificationsOff", R.string.NotificationsOff));
                    z = i6;
                } else {
                    if (notificationsSettings3.getBoolean("EnableReactionsMessages", true)) {
                        sb.append(LocaleController.getString(R.string.NotificationReactionsMessages));
                    }
                    if (notificationsSettings3.getBoolean("EnableReactionsStories", true)) {
                        if (sb.length() > 0) {
                            sb.append(", ");
                        }
                        sb.append(LocaleController.getString(R.string.NotificationReactionsStories));
                    }
                    z = 1;
                }
                int i13 = i3;
                if (i != NotificationsSettingsActivity.this.reactionsRow) {
                    z2 = true;
                } else {
                    z2 = false;
                }
                notificationsCheckCell.setTextAndValueAndIconAndCheck(charSequence, sb, i13, z, i11, false, z2);
            }
            int i14 = i2;
            arrayList2 = null;
            arrayList3 = arrayList;
            i4 = i14;
            if (i4 < currentTime) {
                i5 = 1;
            } else {
                i5 = 0;
            }
            if (i5 != 0) {
                i7 = 0;
                i6 = 0;
            } else {
                i6 = 0;
                i7 = 2;
            }
            sb = new StringBuilder();
            CharSequence charSequence2 = string;
            int i15 = i7;
            if (i == NotificationsSettingsActivity.this.reactionsRow) {
                if (arrayList3 == null) {
                    if (arrayList2 == null) {
                        sb.append(LocaleController.getString("TapToChange", R.string.TapToChange));
                    } else {
                        sb.append(LocaleController.getString("TapToChange", R.string.TapToChange));
                    }
                } else if (arrayList2 == null) {
                    sb.append(LocaleController.getString("TapToChange", R.string.TapToChange));
                } else {
                    sb.append(LocaleController.getString("TapToChange", R.string.TapToChange));
                }
                z = i5;
            } else if (i4 > 0) {
                sb.append(LocaleController.getString("NotificationsOff", R.string.NotificationsOff));
                z = i6;
            } else {
                if (notificationsSettings3.getBoolean("EnableReactionsMessages", true)) {
                    sb.append(LocaleController.getString(R.string.NotificationReactionsMessages));
                }
                if (notificationsSettings3.getBoolean("EnableReactionsStories", true)) {
                    if (sb.length() > 0) {
                        sb.append(", ");
                    }
                    sb.append(LocaleController.getString(R.string.NotificationReactionsStories));
                }
                z = 1;
            }
            int i16 = i3;
            if (i != NotificationsSettingsActivity.this.reactionsRow) {
                z2 = true;
            } else {
                z2 = false;
            }
            notificationsCheckCell.setTextAndValueAndIconAndCheck(charSequence2, sb, i16, z, i15, false, z2);
        }

        @Override // androidx.recyclerview.widget.RecyclerView.Adapter
        public int getItemViewType(int i) {
            if (i == NotificationsSettingsActivity.this.eventsSectionRow || i == NotificationsSettingsActivity.this.otherSectionRow || i == NotificationsSettingsActivity.this.resetSectionRow || i == NotificationsSettingsActivity.this.callsSectionRow || i == NotificationsSettingsActivity.this.badgeNumberSection || i == NotificationsSettingsActivity.this.inappSectionRow || i == NotificationsSettingsActivity.this.notificationsSectionRow || i == NotificationsSettingsActivity.this.accountsSectionRow) {
                return 0;
            }
            if (i == NotificationsSettingsActivity.this.inappSoundRow || i == NotificationsSettingsActivity.this.inappVibrateRow || i == NotificationsSettingsActivity.this.notificationsServiceConnectionRow || i == NotificationsSettingsActivity.this.inappPreviewRow || i == NotificationsSettingsActivity.this.contactJoinedRow || i == NotificationsSettingsActivity.this.pinnedMessageRow || i == NotificationsSettingsActivity.this.notificationsServiceRow || i == NotificationsSettingsActivity.this.badgeNumberMutedRow || i == NotificationsSettingsActivity.this.badgeNumberMessagesRow || i == NotificationsSettingsActivity.this.badgeNumberShowRow || i == NotificationsSettingsActivity.this.inappPriorityRow || i == NotificationsSettingsActivity.this.inchatSoundRow || i == NotificationsSettingsActivity.this.androidAutoAlertRow || i == NotificationsSettingsActivity.this.accountsAllRow) {
                return 1;
            }
            if (i == NotificationsSettingsActivity.this.resetNotificationsRow) {
                return 2;
            }
            if (i == NotificationsSettingsActivity.this.privateRow || i == NotificationsSettingsActivity.this.groupRow || i == NotificationsSettingsActivity.this.channelsRow || i == NotificationsSettingsActivity.this.storiesRow || i == NotificationsSettingsActivity.this.reactionsRow) {
                return 3;
            }
            if (i == NotificationsSettingsActivity.this.eventsSection2Row || i == NotificationsSettingsActivity.this.notificationsSection2Row || i == NotificationsSettingsActivity.this.otherSection2Row || i == NotificationsSettingsActivity.this.resetSection2Row || i == NotificationsSettingsActivity.this.callsSection2Row || i == NotificationsSettingsActivity.this.badgeNumberSection2Row || i == NotificationsSettingsActivity.this.resetNotificationsSectionRow) {
                return 4;
            }
            return i == NotificationsSettingsActivity.this.accountsInfoRow ? 6 : 5;
        }
    }

    @Override // org.telegram.ui.ActionBar.BaseFragment
    public ArrayList getThemeDescriptions() {
        ArrayList arrayList = new ArrayList();
        arrayList.add(new ThemeDescription(this.listView, ThemeDescription.FLAG_CELLBACKGROUNDCOLOR, new Class[]{HeaderCell.class, TextCheckCell.class, TextDetailSettingsCell.class, TextSettingsCell.class, NotificationsCheckCell.class}, null, null, null, Theme.key_windowBackgroundWhite));
        arrayList.add(new ThemeDescription(this.fragmentView, ThemeDescription.FLAG_BACKGROUND, null, null, null, null, Theme.key_windowBackgroundGray));
        arrayList.add(new ThemeDescription(this.listView, ThemeDescription.FLAG_LISTGLOWCOLOR, null, null, null, null, Theme.key_actionBarDefault));
        arrayList.add(new ThemeDescription(this.actionBar, ThemeDescription.FLAG_AB_ITEMSCOLOR, null, null, null, null, Theme.key_actionBarDefaultIcon));
        arrayList.add(new ThemeDescription(this.actionBar, ThemeDescription.FLAG_AB_TITLECOLOR, null, null, null, null, Theme.key_actionBarDefaultTitle));
        arrayList.add(new ThemeDescription(this.actionBar, ThemeDescription.FLAG_AB_SELECTORCOLOR, null, null, null, null, Theme.key_actionBarDefaultSelector));
        arrayList.add(new ThemeDescription(this.listView, ThemeDescription.FLAG_SELECTOR, null, null, null, null, Theme.key_listSelector));
        arrayList.add(new ThemeDescription(this.listView, 0, new Class[]{View.class}, Theme.dividerPaint, null, null, Theme.key_divider));
        arrayList.add(new ThemeDescription(this.listView, 0, new Class[]{HeaderCell.class}, new String[]{"textView"}, (Paint[]) null, (Drawable[]) null, (ThemeDescription.ThemeDescriptionDelegate) null, Theme.key_windowBackgroundWhiteBlueHeader));
        int i = Theme.key_windowBackgroundWhiteBlackText;
        arrayList.add(new ThemeDescription(this.listView, 0, new Class[]{NotificationsCheckCell.class}, new String[]{"textView"}, (Paint[]) null, (Drawable[]) null, (ThemeDescription.ThemeDescriptionDelegate) null, i));
        int i2 = Theme.key_windowBackgroundWhiteGrayText2;
        arrayList.add(new ThemeDescription(this.listView, 0, new Class[]{NotificationsCheckCell.class}, new String[]{"valueTextView"}, (Paint[]) null, (Drawable[]) null, (ThemeDescription.ThemeDescriptionDelegate) null, i2));
        int i3 = Theme.key_switchTrack;
        arrayList.add(new ThemeDescription(this.listView, 0, new Class[]{NotificationsCheckCell.class}, new String[]{"checkBox"}, (Paint[]) null, (Drawable[]) null, (ThemeDescription.ThemeDescriptionDelegate) null, i3));
        int i4 = Theme.key_switchTrackChecked;
        arrayList.add(new ThemeDescription(this.listView, 0, new Class[]{NotificationsCheckCell.class}, new String[]{"checkBox"}, (Paint[]) null, (Drawable[]) null, (ThemeDescription.ThemeDescriptionDelegate) null, i4));
        arrayList.add(new ThemeDescription(this.listView, 0, new Class[]{TextCheckCell.class}, new String[]{"textView"}, (Paint[]) null, (Drawable[]) null, (ThemeDescription.ThemeDescriptionDelegate) null, i));
        arrayList.add(new ThemeDescription(this.listView, 0, new Class[]{TextCheckCell.class}, new String[]{"valueTextView"}, (Paint[]) null, (Drawable[]) null, (ThemeDescription.ThemeDescriptionDelegate) null, i2));
        arrayList.add(new ThemeDescription(this.listView, 0, new Class[]{TextCheckCell.class}, new String[]{"checkBox"}, (Paint[]) null, (Drawable[]) null, (ThemeDescription.ThemeDescriptionDelegate) null, i3));
        arrayList.add(new ThemeDescription(this.listView, 0, new Class[]{TextCheckCell.class}, new String[]{"checkBox"}, (Paint[]) null, (Drawable[]) null, (ThemeDescription.ThemeDescriptionDelegate) null, i4));
        arrayList.add(new ThemeDescription(this.listView, 0, new Class[]{TextSettingsCell.class}, new String[]{"textView"}, (Paint[]) null, (Drawable[]) null, (ThemeDescription.ThemeDescriptionDelegate) null, i));
        arrayList.add(new ThemeDescription(this.listView, 0, new Class[]{TextSettingsCell.class}, new String[]{"valueTextView"}, (Paint[]) null, (Drawable[]) null, (ThemeDescription.ThemeDescriptionDelegate) null, Theme.key_windowBackgroundWhiteValueText));
        arrayList.add(new ThemeDescription(this.listView, 0, new Class[]{TextDetailSettingsCell.class}, new String[]{"textView"}, (Paint[]) null, (Drawable[]) null, (ThemeDescription.ThemeDescriptionDelegate) null, i));
        arrayList.add(new ThemeDescription(this.listView, 0, new Class[]{TextDetailSettingsCell.class}, new String[]{"valueTextView"}, (Paint[]) null, (Drawable[]) null, (ThemeDescription.ThemeDescriptionDelegate) null, i2));
        arrayList.add(new ThemeDescription(this.listView, 0, new Class[]{TextInfoPrivacyCell.class}, new String[]{"textView"}, (Paint[]) null, (Drawable[]) null, (ThemeDescription.ThemeDescriptionDelegate) null, Theme.key_windowBackgroundWhiteGrayText4));
        arrayList.add(new ThemeDescription(this.listView, ThemeDescription.FLAG_LINKCOLOR, new Class[]{TextInfoPrivacyCell.class}, new String[]{"textView"}, (Paint[]) null, (Drawable[]) null, (ThemeDescription.ThemeDescriptionDelegate) null, Theme.key_windowBackgroundWhiteLinkText));
        return arrayList;
    }

    @Override // org.telegram.ui.ActionBar.BaseFragment
    public void onInsets(int i, int i2, int i3, int i4) {
        this.listView.setPadding(0, 0, 0, i4);
        this.listView.setClipToPadding(false);
    }
}
