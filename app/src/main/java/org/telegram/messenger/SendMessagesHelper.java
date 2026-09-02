package org.telegram.messenger;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.AssetFileDescriptor;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffColorFilter;
import android.graphics.drawable.BitmapDrawable;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.media.MediaMetadataRetriever;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.SystemClock;
import android.text.Spannable;
import android.text.TextUtils;
import android.util.Base64;
import android.util.Log;
import android.util.SparseArray;
import android.webkit.MimeTypeMap;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;
import androidx.collection.LongSparseArray;
import androidx.core.view.inputmethod.InputContentInfoCompat;
import com.exteragram.messenger.plugins.PluginsController;
import com.exteragram.messenger.plugins.hooks.PluginsHooks;
import com.exteragram.messenger.utils.text.LocaleUtils;
import com.radolyn.ayugram.AyuConstants;
import com.radolyn.ayugram.AyuForward;
import com.radolyn.ayugram.AyuState;
import com.radolyn.ayugram.controllers.AyuGhostController;
import j$.util.Objects;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.RandomAccessFile;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;
import okhttp3.internal.url._UrlKt;
import org.json.JSONObject;
import org.mvel2.MVEL;
import org.telegram.messenger.support.SparseLongArray;
import org.telegram.messenger.utils.tlutils.AmountUtils$Amount;
import org.telegram.messenger.utils.tlutils.AmountUtils$Currency;
import org.telegram.tgnet.ConnectionsManager;
import org.telegram.tgnet.NativeByteBuffer;
import org.telegram.tgnet.QuickAckDelegate;
import org.telegram.tgnet.RequestDelegate;
import org.telegram.tgnet.SerializedData;
import org.telegram.tgnet.TLObject;
import org.telegram.tgnet.TLRPC;
import org.telegram.tgnet.tl.TL_account;
import org.telegram.tgnet.tl.TL_stories;
import org.telegram.ui.ActionBar.AlertDialog;
import org.telegram.ui.ActionBar.BaseFragment;
import org.telegram.ui.ActionBar.Theme;
import org.telegram.ui.Business.QuickRepliesController;
import org.telegram.ui.Cells.ChatMessageCell;
import org.telegram.ui.ChatActivity;
import org.telegram.ui.Components.AlertsCreator;
import org.telegram.ui.Components.AnimatedEmojiSpan;
import org.telegram.ui.Components.AnimatedFileDrawable;
import org.telegram.ui.Components.LayoutHelper;
import org.telegram.ui.Components.Point;
import org.telegram.ui.Components.Reactions.ReactionsLayoutInBubble;
import org.telegram.ui.Components.Reactions.ReactionsUtils;
import org.telegram.ui.LaunchActivity;
import org.telegram.ui.OAuthSheet;
import org.telegram.ui.PaymentFormActivity;
import org.telegram.ui.Stars.StarsController;
import org.telegram.ui.Stars.StarsIntroActivity;
import org.telegram.ui.TON.TONIntroActivity;
import org.telegram.ui.TwoStepVerificationActivity;
import org.telegram.ui.TwoStepVerificationSetupActivity;
import org.telegram.ui.bots.BotWebViewSheet;
import org.webrtc.MediaStreamTrack;

public class SendMessagesHelper extends BaseController implements NotificationCenter.NotificationCenterDelegate {
    private static final int ERROR_TYPE_FILE_TOO_LARGE = 2;
    private static final int ERROR_TYPE_UNSUPPORTED = 1;
    private static volatile SendMessagesHelper[] Instance = null;
    public static final int MEDIA_TYPE_DICE = 11;
    public static final int MEDIA_TYPE_STORY = 12;
    private static DispatchQueue mediaSendQueue = new DispatchQueue("mediaSendQueue");
    private static ThreadPoolExecutor mediaSendThreadPool;
    private final HashMap<String, ArrayList<DelayedMessage>> delayedMessages;
    private final SparseArray<TLRPC.Message> editingMessages;
    private final PluginsHooks hooks;
    private final HashMap<String, ImportingHistory> importingHistoryFiles;
    private final LongSparseArray importingHistoryMap;
    private final HashMap<String, ImportingStickers> importingStickersFiles;
    private final HashMap<String, ImportingStickers> importingStickersMap;
    private LocationProvider locationProvider;
    private final SparseArray<TLRPC.Message> sendingMessages;
    private final LongSparseArray sendingMessagesIdDialogs;
    private final SparseArray<MessageObject> unsentMessages;
    private final SparseArray<TLRPC.Message> uploadMessages;
    private final LongSparseArray uploadingMessagesIdDialogs;
    private final LongSparseArray voteSendTime;
    private final HashMap<String, Boolean> waitingForCallback;
    private final HashMap<String, List<String>> waitingForCallbackMap;
    private final HashMap<String, MessageObject> waitingForLocation;
    private final HashMap<Integer, Boolean> waitingForTodoUpdate;
    private final HashMap<String, byte[]> waitingForVote;

    public static class SendingMediaInfo {
        public boolean canDeleteAfter;
        public String caption;
        public String coverPath;
        public TLRPC.Photo coverPhoto;
        public TLRPC.VideoSize emojiMarkup;
        public ArrayList<TLRPC.MessageEntity> entities;
        public boolean forceImage;
        public boolean hasMediaSpoilers;
        public boolean highQuality;
        public TLRPC.BotInlineResult inlineResult;
        public boolean isVideo;
        public ArrayList<TLRPC.InputDocument> masks;
        public MediaController.PhotoEntry originalPhotoEntry;
        public String paintPath;
        public HashMap<String, String> params;
        public String path;
        public MediaController.SearchImage searchImage;
        public long stars;
        public String thumbPath;
        public int ttl;
        public boolean updateStickersOrder;
        public Uri uri;
        public VideoEditedInfo videoEditedInfo;
    }

    /* JADX INFO: renamed from: $r8$lambda$siqOgfLwE48Ihs8Cu1gZ-PVBQmE, reason: not valid java name */
    public static /* synthetic */ void m3729$r8$lambda$siqOgfLwE48Ihs8Cu1gZPVBQmE(String str) {
    }

    public static boolean checkUpdateStickersOrder(CharSequence charSequence) {
        if (charSequence instanceof Spannable) {
            for (AnimatedEmojiSpan animatedEmojiSpan : (AnimatedEmojiSpan[]) ((Spannable) charSequence).getSpans(0, charSequence.length(), AnimatedEmojiSpan.class)) {
                if (animatedEmojiSpan.fromEmojiKeyboard) {
                    return true;
                }
            }
        }
        return false;
    }

    public TLRPC.InputReplyTo createReplyInput(TL_stories.StoryItem storyItem) {
        TLRPC.TL_inputReplyToStory tL_inputReplyToStory = new TLRPC.TL_inputReplyToStory();
        tL_inputReplyToStory.story_id = storyItem.id;
        tL_inputReplyToStory.peer = getMessagesController().getInputPeer(storyItem.dialogId);
        return tL_inputReplyToStory;
    }

    public TLRPC.InputReplyTo createReplyInput(int i) {
        return createReplyInput(null, i, 0, null);
    }

    public TLRPC.InputReplyTo createReplyInput(TLRPC.InputPeer inputPeer, int i, int i2, ChatActivity.ReplyQuote replyQuote) {
        MessageObject messageObject;
        TLRPC.TodoItem todoItem;
        TLRPC.TL_inputReplyToMessage tL_inputReplyToMessage = new TLRPC.TL_inputReplyToMessage();
        tL_inputReplyToMessage.reply_to_msg_id = i;
        if (i2 != 0) {
            tL_inputReplyToMessage.flags |= 1;
            tL_inputReplyToMessage.top_msg_id = i2;
        }
        if (replyQuote != null && replyQuote.todo && (todoItem = replyQuote.task) != null) {
            tL_inputReplyToMessage.flags |= 64;
            tL_inputReplyToMessage.todo_item_id = todoItem.id;
        } else if (replyQuote != null && !replyQuote.todo) {
            String text = replyQuote.getText();
            tL_inputReplyToMessage.quote_text = text;
            if (!TextUtils.isEmpty(text)) {
                tL_inputReplyToMessage.flags |= 4;
                ArrayList filteredEntities = replyQuote.getFilteredEntities();
                tL_inputReplyToMessage.quote_entities = filteredEntities;
                if (filteredEntities != null && !filteredEntities.isEmpty()) {
                    tL_inputReplyToMessage.quote_entities = new ArrayList(tL_inputReplyToMessage.quote_entities);
                    tL_inputReplyToMessage.flags |= 8;
                }
                tL_inputReplyToMessage.flags |= 16;
                tL_inputReplyToMessage.quote_offset = replyQuote.start;
            }
        }
        if (replyQuote != null && (messageObject = replyQuote.message) != null) {
            TLRPC.InputPeer inputPeer2 = getMessagesController().getInputPeer(messageObject.getDialogId());
            if (inputPeer2 != null && !MessageObject.peersEqual(inputPeer2, inputPeer)) {
                tL_inputReplyToMessage.flags |= 2;
                tL_inputReplyToMessage.reply_to_peer_id = inputPeer2;
            }
        }
        return tL_inputReplyToMessage;
    }

    public TLRPC.InputReplyTo createReplyInput(TLRPC.TL_messageReplyHeader tL_messageReplyHeader) {
        TLRPC.TL_inputReplyToMessage tL_inputReplyToMessage = new TLRPC.TL_inputReplyToMessage();
        tL_inputReplyToMessage.reply_to_msg_id = tL_messageReplyHeader.reply_to_msg_id;
        int i = tL_messageReplyHeader.flags;
        if ((i & 2) != 0) {
            tL_inputReplyToMessage.flags |= 1;
            tL_inputReplyToMessage.top_msg_id = tL_messageReplyHeader.reply_to_top_id;
        }
        if ((i & 1) != 0) {
            tL_inputReplyToMessage.flags |= 2;
            tL_inputReplyToMessage.reply_to_peer_id = MessagesController.getInstance(this.currentAccount).getInputPeer(tL_messageReplyHeader.reply_to_peer_id);
        }
        if (tL_messageReplyHeader.quote) {
            int i2 = tL_messageReplyHeader.flags;
            if ((i2 & 64) != 0) {
                tL_inputReplyToMessage.flags |= 4;
                tL_inputReplyToMessage.quote_text = tL_messageReplyHeader.quote_text;
            }
            if ((i2 & 128) != 0) {
                tL_inputReplyToMessage.flags |= 8;
                tL_inputReplyToMessage.quote_entities = tL_messageReplyHeader.quote_entities;
            }
            if ((i2 & 1024) != 0) {
                tL_inputReplyToMessage.flags |= 16;
                tL_inputReplyToMessage.quote_offset = tL_messageReplyHeader.quote_offset;
            }
        }
        if ((tL_messageReplyHeader.flags & 2048) != 0) {
            tL_inputReplyToMessage.flags |= 64;
            tL_inputReplyToMessage.todo_item_id = tL_messageReplyHeader.todo_item_id;
        }
        return tL_inputReplyToMessage;
    }

    public class ImportingHistory {
        public long dialogId;
        public double estimatedUploadSpeed;
        public String historyPath;
        public long importId;
        private long lastUploadSize;
        private long lastUploadTime;
        public TLRPC.InputPeer peer;
        public long totalSize;
        public int uploadProgress;
        public long uploadedSize;
        public ArrayList<Uri> mediaPaths = new ArrayList<>();
        public HashSet<String> uploadSet = new HashSet<>();
        public HashMap<String, Float> uploadProgresses = new HashMap<>();
        public HashMap<String, Long> uploadSize = new HashMap<>();
        public ArrayList<String> uploadMedia = new ArrayList<>();
        public int timeUntilFinish = Integer.MAX_VALUE;

        public ImportingHistory() {
        }

        /* JADX INFO: Access modifiers changed from: private */
        public void initImport(TLRPC.InputFile inputFile) {
            TLRPC.TL_messages_initHistoryImport tL_messages_initHistoryImport = new TLRPC.TL_messages_initHistoryImport();
            tL_messages_initHistoryImport.file = inputFile;
            tL_messages_initHistoryImport.media_count = this.mediaPaths.size();
            tL_messages_initHistoryImport.peer = this.peer;
            SendMessagesHelper.this.getConnectionsManager().sendRequest(tL_messages_initHistoryImport, new AnonymousClass1(tL_messages_initHistoryImport), 2);
        }

        /* JADX INFO: renamed from: org.telegram.messenger.SendMessagesHelper$ImportingHistory$1, reason: invalid class name */
        class AnonymousClass1 implements RequestDelegate {
            final /* synthetic */ TLRPC.TL_messages_initHistoryImport val$req;

            AnonymousClass1(TLRPC.TL_messages_initHistoryImport tL_messages_initHistoryImport) {
                this.val$req = tL_messages_initHistoryImport;
            }

            @Override // org.telegram.tgnet.RequestDelegate
            public void run(final TLObject tLObject, final TLRPC.TL_error tL_error) {
                final TLRPC.TL_messages_initHistoryImport tL_messages_initHistoryImport = this.val$req;
                AndroidUtilities.runOnUIThread(new Runnable() { // from class: org.telegram.messenger.SendMessagesHelper$ImportingHistory$1$$ExternalSyntheticLambda0
                    @Override // java.lang.Runnable
                    public final void run() {
                        this.f$0.lambda$run$0(tLObject, tL_messages_initHistoryImport, tL_error);
                    }
                });
            }

            /* JADX INFO: Access modifiers changed from: private */
            public /* synthetic */ void lambda$run$0(TLObject tLObject, TLRPC.TL_messages_initHistoryImport tL_messages_initHistoryImport, TLRPC.TL_error tL_error) {
                if (tLObject instanceof TLRPC.TL_messages_historyImport) {
                    ImportingHistory importingHistory = ImportingHistory.this;
                    importingHistory.importId = ((TLRPC.TL_messages_historyImport) tLObject).id;
                    importingHistory.uploadSet.remove(importingHistory.historyPath);
                    SendMessagesHelper.this.getNotificationCenter().lambda$postNotificationNameOnUIThread$1(NotificationCenter.historyImportProgressChanged, Long.valueOf(ImportingHistory.this.dialogId));
                    if (ImportingHistory.this.uploadSet.isEmpty()) {
                        ImportingHistory.this.startImport();
                    }
                    ImportingHistory.this.lastUploadTime = SystemClock.elapsedRealtime();
                    int size = ImportingHistory.this.uploadMedia.size();
                    for (int i = 0; i < size; i++) {
                        SendMessagesHelper.this.getFileLoader().uploadFile(ImportingHistory.this.uploadMedia.get(i), false, true, 67108864);
                    }
                    return;
                }
                SendMessagesHelper.this.importingHistoryMap.remove(ImportingHistory.this.dialogId);
                SendMessagesHelper.this.getNotificationCenter().lambda$postNotificationNameOnUIThread$1(NotificationCenter.historyImportProgressChanged, Long.valueOf(ImportingHistory.this.dialogId), tL_messages_initHistoryImport, tL_error);
            }
        }

        public long getUploadedCount() {
            return this.uploadedSize;
        }

        public long getTotalCount() {
            return this.totalSize;
        }

        /* JADX INFO: Access modifiers changed from: private */
        public void onFileFailedToUpload(String str) {
            if (str.equals(this.historyPath)) {
                SendMessagesHelper.this.importingHistoryMap.remove(this.dialogId);
                TLRPC.TL_error tL_error = new TLRPC.TL_error();
                tL_error.code = 400;
                tL_error.text = "IMPORT_UPLOAD_FAILED";
                SendMessagesHelper.this.getNotificationCenter().lambda$postNotificationNameOnUIThread$1(NotificationCenter.historyImportProgressChanged, Long.valueOf(this.dialogId), new TLRPC.TL_messages_initHistoryImport(), tL_error);
                return;
            }
            this.uploadSet.remove(str);
        }

        /* JADX INFO: Access modifiers changed from: private */
        public void addUploadProgress(String str, long j, float f) {
            this.uploadProgresses.put(str, Float.valueOf(f));
            this.uploadSize.put(str, Long.valueOf(j));
            this.uploadedSize = 0L;
            Iterator<Map.Entry<String, Long>> it = this.uploadSize.entrySet().iterator();
            while (it.hasNext()) {
                this.uploadedSize += it.next().getValue().longValue();
            }
            long jElapsedRealtime = SystemClock.elapsedRealtime();
            if (!str.equals(this.historyPath)) {
                long j2 = this.uploadedSize;
                long j3 = this.lastUploadSize;
                if (j2 != j3) {
                    long j4 = this.lastUploadTime;
                    if (jElapsedRealtime != j4) {
                        double d = (j2 - j3) / ((jElapsedRealtime - j4) / 1000.0d);
                        double d2 = this.estimatedUploadSpeed;
                        if (d2 == 0.0d) {
                            this.estimatedUploadSpeed = d;
                        } else {
                            this.estimatedUploadSpeed = (d * 0.01d) + (0.99d * d2);
                        }
                        this.timeUntilFinish = (int) (((this.totalSize - j2) * 1000) / this.estimatedUploadSpeed);
                        this.lastUploadSize = j2;
                        this.lastUploadTime = jElapsedRealtime;
                    }
                }
            }
            int uploadedCount = (int) ((getUploadedCount() / getTotalCount()) * 100.0f);
            if (this.uploadProgress != uploadedCount) {
                this.uploadProgress = uploadedCount;
                SendMessagesHelper.this.getNotificationCenter().lambda$postNotificationNameOnUIThread$1(NotificationCenter.historyImportProgressChanged, Long.valueOf(this.dialogId));
            }
        }

        /* JADX INFO: Access modifiers changed from: private */
        public void onMediaImport(String str, long j, TLRPC.InputFile inputFile) {
            String lowerCase;
            addUploadProgress(str, j, 1.0f);
            TLRPC.TL_messages_uploadImportedMedia tL_messages_uploadImportedMedia = new TLRPC.TL_messages_uploadImportedMedia();
            tL_messages_uploadImportedMedia.peer = this.peer;
            tL_messages_uploadImportedMedia.import_id = this.importId;
            tL_messages_uploadImportedMedia.file_name = new File(str).getName();
            MimeTypeMap singleton = MimeTypeMap.getSingleton();
            int iLastIndexOf = tL_messages_uploadImportedMedia.file_name.lastIndexOf(46);
            if (iLastIndexOf == -1) {
                lowerCase = "txt";
            } else {
                lowerCase = tL_messages_uploadImportedMedia.file_name.substring(iLastIndexOf + 1).toLowerCase();
            }
            String mimeTypeFromExtension = singleton.getMimeTypeFromExtension(lowerCase);
            if (mimeTypeFromExtension == null) {
                if ("opus".equals(lowerCase)) {
                    mimeTypeFromExtension = "audio/opus";
                } else if ("webp".equals(lowerCase)) {
                    mimeTypeFromExtension = "image/webp";
                } else {
                    mimeTypeFromExtension = "text/plain";
                }
            }
            if (mimeTypeFromExtension.equals("image/jpg") || mimeTypeFromExtension.equals("image/jpeg")) {
                TLRPC.TL_inputMediaUploadedPhoto tL_inputMediaUploadedPhoto = new TLRPC.TL_inputMediaUploadedPhoto();
                tL_inputMediaUploadedPhoto.file = inputFile;
                tL_messages_uploadImportedMedia.media = tL_inputMediaUploadedPhoto;
            } else {
                TLRPC.TL_inputMediaUploadedDocument tL_inputMediaUploadedDocument = new TLRPC.TL_inputMediaUploadedDocument();
                tL_inputMediaUploadedDocument.file = inputFile;
                tL_inputMediaUploadedDocument.mime_type = mimeTypeFromExtension;
                tL_messages_uploadImportedMedia.media = tL_inputMediaUploadedDocument;
            }
            SendMessagesHelper.this.getConnectionsManager().sendRequest(tL_messages_uploadImportedMedia, new AnonymousClass2(str), 2);
        }

        /* JADX INFO: renamed from: org.telegram.messenger.SendMessagesHelper$ImportingHistory$2, reason: invalid class name */
        class AnonymousClass2 implements RequestDelegate {
            final /* synthetic */ String val$path;

            AnonymousClass2(String str) {
                this.val$path = str;
            }

            @Override // org.telegram.tgnet.RequestDelegate
            public void run(TLObject tLObject, TLRPC.TL_error tL_error) {
                final String str = this.val$path;
                AndroidUtilities.runOnUIThread(new Runnable() { // from class: org.telegram.messenger.SendMessagesHelper$ImportingHistory$2$$ExternalSyntheticLambda0
                    @Override // java.lang.Runnable
                    public final void run() {
                        this.f$0.lambda$run$0(str);
                    }
                });
            }

            /* JADX INFO: Access modifiers changed from: private */
            public /* synthetic */ void lambda$run$0(String str) {
                ImportingHistory.this.uploadSet.remove(str);
                SendMessagesHelper.this.getNotificationCenter().lambda$postNotificationNameOnUIThread$1(NotificationCenter.historyImportProgressChanged, Long.valueOf(ImportingHistory.this.dialogId));
                if (ImportingHistory.this.uploadSet.isEmpty()) {
                    ImportingHistory.this.startImport();
                }
            }
        }

        /* JADX INFO: Access modifiers changed from: private */
        public void startImport() {
            TLRPC.TL_messages_startHistoryImport tL_messages_startHistoryImport = new TLRPC.TL_messages_startHistoryImport();
            tL_messages_startHistoryImport.peer = this.peer;
            tL_messages_startHistoryImport.import_id = this.importId;
            SendMessagesHelper.this.getConnectionsManager().sendRequest(tL_messages_startHistoryImport, new AnonymousClass3(tL_messages_startHistoryImport));
        }

        /* JADX INFO: renamed from: org.telegram.messenger.SendMessagesHelper$ImportingHistory$3, reason: invalid class name */
        class AnonymousClass3 implements RequestDelegate {
            final /* synthetic */ TLRPC.TL_messages_startHistoryImport val$req;

            AnonymousClass3(TLRPC.TL_messages_startHistoryImport tL_messages_startHistoryImport) {
                this.val$req = tL_messages_startHistoryImport;
            }

            @Override // org.telegram.tgnet.RequestDelegate
            public void run(TLObject tLObject, final TLRPC.TL_error tL_error) {
                final TLRPC.TL_messages_startHistoryImport tL_messages_startHistoryImport = this.val$req;
                AndroidUtilities.runOnUIThread(new Runnable() { // from class: org.telegram.messenger.SendMessagesHelper$ImportingHistory$3$$ExternalSyntheticLambda0
                    @Override // java.lang.Runnable
                    public final void run() {
                        this.f$0.lambda$run$0(tL_error, tL_messages_startHistoryImport);
                    }
                });
            }

            /* JADX INFO: Access modifiers changed from: private */
            public /* synthetic */ void lambda$run$0(TLRPC.TL_error tL_error, TLRPC.TL_messages_startHistoryImport tL_messages_startHistoryImport) {
                SendMessagesHelper.this.importingHistoryMap.remove(ImportingHistory.this.dialogId);
                if (tL_error == null) {
                    SendMessagesHelper.this.getNotificationCenter().lambda$postNotificationNameOnUIThread$1(NotificationCenter.historyImportProgressChanged, Long.valueOf(ImportingHistory.this.dialogId));
                } else {
                    SendMessagesHelper.this.getNotificationCenter().lambda$postNotificationNameOnUIThread$1(NotificationCenter.historyImportProgressChanged, Long.valueOf(ImportingHistory.this.dialogId), tL_messages_startHistoryImport, tL_error);
                }
            }
        }

        public void setImportProgress(int i) {
            if (i == 100) {
                SendMessagesHelper.this.importingHistoryMap.remove(this.dialogId);
            }
            SendMessagesHelper.this.getNotificationCenter().lambda$postNotificationNameOnUIThread$1(NotificationCenter.historyImportProgressChanged, Long.valueOf(this.dialogId));
        }
    }

    public static class ImportingSticker {
        public boolean animated;
        public String emoji;
        public TLRPC.TL_inputStickerSetItem item;
        public String mimeType;
        public String path;
        public boolean validated;
        public VideoEditedInfo videoEditedInfo;

        public void uploadMedia(int i, TLRPC.InputFile inputFile, Runnable runnable) {
            TLRPC.TL_messages_uploadMedia tL_messages_uploadMedia = new TLRPC.TL_messages_uploadMedia();
            tL_messages_uploadMedia.peer = new TLRPC.TL_inputPeerSelf();
            TLRPC.TL_inputMediaUploadedDocument tL_inputMediaUploadedDocument = new TLRPC.TL_inputMediaUploadedDocument();
            tL_messages_uploadMedia.media = tL_inputMediaUploadedDocument;
            tL_inputMediaUploadedDocument.file = inputFile;
            tL_inputMediaUploadedDocument.mime_type = this.mimeType;
            ConnectionsManager.getInstance(i).sendRequest(tL_messages_uploadMedia, new AnonymousClass1(runnable), 2);
        }

        /* JADX INFO: renamed from: org.telegram.messenger.SendMessagesHelper$ImportingSticker$1, reason: invalid class name */
        class AnonymousClass1 implements RequestDelegate {
            final /* synthetic */ Runnable val$onFinish;

            AnonymousClass1(Runnable runnable) {
                this.val$onFinish = runnable;
            }

            @Override // org.telegram.tgnet.RequestDelegate
            public void run(final TLObject tLObject, TLRPC.TL_error tL_error) {
                final Runnable runnable = this.val$onFinish;
                AndroidUtilities.runOnUIThread(new Runnable() { // from class: org.telegram.messenger.SendMessagesHelper$ImportingSticker$1$$ExternalSyntheticLambda0
                    @Override // java.lang.Runnable
                    public final void run() {
                        this.f$0.lambda$run$0(tLObject, runnable);
                    }
                });
            }

            /* JADX INFO: Access modifiers changed from: private */
            public /* synthetic */ void lambda$run$0(TLObject tLObject, Runnable runnable) {
                if (tLObject instanceof TLRPC.TL_messageMediaDocument) {
                    ImportingSticker.this.item = new TLRPC.TL_inputStickerSetItem();
                    ImportingSticker.this.item.document = new TLRPC.TL_inputDocument();
                    ImportingSticker importingSticker = ImportingSticker.this;
                    TLRPC.TL_inputStickerSetItem tL_inputStickerSetItem = importingSticker.item;
                    TLRPC.InputDocument inputDocument = tL_inputStickerSetItem.document;
                    TLRPC.Document document = ((TLRPC.TL_messageMediaDocument) tLObject).document;
                    inputDocument.id = document.id;
                    inputDocument.access_hash = document.access_hash;
                    inputDocument.file_reference = document.file_reference;
                    String str = importingSticker.emoji;
                    if (str == null) {
                        str = _UrlKt.FRAGMENT_ENCODE_SET;
                    }
                    tL_inputStickerSetItem.emoji = str;
                    importingSticker.mimeType = document.mime_type;
                } else {
                    ImportingSticker importingSticker2 = ImportingSticker.this;
                    if (importingSticker2.animated) {
                        importingSticker2.mimeType = "application/x-bad-tgsticker";
                    }
                }
                runnable.run();
            }
        }
    }

    public class ImportingStickers {
        public double estimatedUploadSpeed;
        private long lastUploadSize;
        private long lastUploadTime;
        public String shortName;
        public String software;
        public String title;
        public long totalSize;
        public int uploadProgress;
        public long uploadedSize;
        public HashMap<String, ImportingSticker> uploadSet = new HashMap<>();
        public HashMap<String, Float> uploadProgresses = new HashMap<>();
        public HashMap<String, Long> uploadSize = new HashMap<>();
        public ArrayList<ImportingSticker> uploadMedia = new ArrayList<>();
        public int timeUntilFinish = Integer.MAX_VALUE;

        public ImportingStickers() {
        }

        /* JADX INFO: Access modifiers changed from: private */
        public void initImport() {
            SendMessagesHelper.this.getNotificationCenter().lambda$postNotificationNameOnUIThread$1(NotificationCenter.stickersImportProgressChanged, this.shortName);
            this.lastUploadTime = SystemClock.elapsedRealtime();
            int size = this.uploadMedia.size();
            for (int i = 0; i < size; i++) {
                SendMessagesHelper.this.getFileLoader().uploadFile(this.uploadMedia.get(i).path, false, true, 67108864);
            }
        }

        public long getUploadedCount() {
            return this.uploadedSize;
        }

        public long getTotalCount() {
            return this.totalSize;
        }

        /* JADX INFO: Access modifiers changed from: private */
        public void onFileFailedToUpload(String str) {
            ImportingSticker importingStickerRemove = this.uploadSet.remove(str);
            if (importingStickerRemove != null) {
                this.uploadMedia.remove(importingStickerRemove);
            }
        }

        /* JADX INFO: Access modifiers changed from: private */
        public void addUploadProgress(String str, long j, float f) {
            this.uploadProgresses.put(str, Float.valueOf(f));
            this.uploadSize.put(str, Long.valueOf(j));
            this.uploadedSize = 0L;
            Iterator<Map.Entry<String, Long>> it = this.uploadSize.entrySet().iterator();
            while (it.hasNext()) {
                this.uploadedSize += it.next().getValue().longValue();
            }
            long jElapsedRealtime = SystemClock.elapsedRealtime();
            long j2 = this.uploadedSize;
            long j3 = this.lastUploadSize;
            if (j2 != j3) {
                long j4 = this.lastUploadTime;
                if (jElapsedRealtime != j4) {
                    double d = (j2 - j3) / ((jElapsedRealtime - j4) / 1000.0d);
                    double d2 = this.estimatedUploadSpeed;
                    if (d2 == 0.0d) {
                        this.estimatedUploadSpeed = d;
                    } else {
                        this.estimatedUploadSpeed = (d * 0.01d) + (0.99d * d2);
                    }
                    this.timeUntilFinish = (int) (((this.totalSize - j2) * 1000) / this.estimatedUploadSpeed);
                    this.lastUploadSize = j2;
                    this.lastUploadTime = jElapsedRealtime;
                }
            }
            int uploadedCount = (int) ((getUploadedCount() / getTotalCount()) * 100.0f);
            if (this.uploadProgress != uploadedCount) {
                this.uploadProgress = uploadedCount;
                SendMessagesHelper.this.getNotificationCenter().lambda$postNotificationNameOnUIThread$1(NotificationCenter.stickersImportProgressChanged, this.shortName);
            }
        }

        /* JADX INFO: Access modifiers changed from: private */
        public void onMediaImport(final String str, long j, TLRPC.InputFile inputFile) {
            addUploadProgress(str, j, 1.0f);
            ImportingSticker importingSticker = this.uploadSet.get(str);
            if (importingSticker == null) {
                return;
            }
            importingSticker.uploadMedia(SendMessagesHelper.this.currentAccount, inputFile, new Runnable() { // from class: org.telegram.messenger.SendMessagesHelper$ImportingStickers$$ExternalSyntheticLambda0
                @Override // java.lang.Runnable
                public final void run() {
                    this.f$0.lambda$onMediaImport$0(str);
                }
            });
        }

        /* JADX INFO: Access modifiers changed from: private */
        public /* synthetic */ void lambda$onMediaImport$0(String str) {
            this.uploadSet.remove(str);
            SendMessagesHelper.this.getNotificationCenter().lambda$postNotificationNameOnUIThread$1(NotificationCenter.stickersImportProgressChanged, this.shortName);
            if (this.uploadSet.isEmpty()) {
                startImport();
            }
        }

        /* JADX INFO: Access modifiers changed from: private */
        public void startImport() {
            TLRPC.TL_stickers_createStickerSet tL_stickers_createStickerSet = new TLRPC.TL_stickers_createStickerSet();
            tL_stickers_createStickerSet.user_id = new TLRPC.TL_inputUserSelf();
            tL_stickers_createStickerSet.title = this.title;
            tL_stickers_createStickerSet.short_name = this.shortName;
            String str = this.software;
            if (str != null) {
                tL_stickers_createStickerSet.software = str;
                tL_stickers_createStickerSet.flags |= 8;
            }
            int size = this.uploadMedia.size();
            for (int i = 0; i < size; i++) {
                TLRPC.TL_inputStickerSetItem tL_inputStickerSetItem = this.uploadMedia.get(i).item;
                if (tL_inputStickerSetItem != null) {
                    tL_stickers_createStickerSet.stickers.add(tL_inputStickerSetItem);
                }
            }
            SendMessagesHelper.this.getConnectionsManager().sendRequest(tL_stickers_createStickerSet, new AnonymousClass1(tL_stickers_createStickerSet));
        }

        /* JADX INFO: renamed from: org.telegram.messenger.SendMessagesHelper$ImportingStickers$1, reason: invalid class name */
        class AnonymousClass1 implements RequestDelegate {
            final /* synthetic */ TLRPC.TL_stickers_createStickerSet val$req;

            AnonymousClass1(TLRPC.TL_stickers_createStickerSet tL_stickers_createStickerSet) {
                this.val$req = tL_stickers_createStickerSet;
            }

            @Override // org.telegram.tgnet.RequestDelegate
            public void run(final TLObject tLObject, final TLRPC.TL_error tL_error) {
                final TLRPC.TL_stickers_createStickerSet tL_stickers_createStickerSet = this.val$req;
                AndroidUtilities.runOnUIThread(new Runnable() { // from class: org.telegram.messenger.SendMessagesHelper$ImportingStickers$1$$ExternalSyntheticLambda0
                    @Override // java.lang.Runnable
                    public final void run() {
                        this.f$0.lambda$run$0(tL_error, tL_stickers_createStickerSet, tLObject);
                    }
                });
            }

            /* JADX INFO: Access modifiers changed from: private */
            public /* synthetic */ void lambda$run$0(TLRPC.TL_error tL_error, TLRPC.TL_stickers_createStickerSet tL_stickers_createStickerSet, TLObject tLObject) {
                SendMessagesHelper.this.importingStickersMap.remove(ImportingStickers.this.shortName);
                if (tL_error == null) {
                    SendMessagesHelper.this.getNotificationCenter().lambda$postNotificationNameOnUIThread$1(NotificationCenter.stickersImportProgressChanged, ImportingStickers.this.shortName);
                } else {
                    SendMessagesHelper.this.getNotificationCenter().lambda$postNotificationNameOnUIThread$1(NotificationCenter.stickersImportProgressChanged, ImportingStickers.this.shortName, tL_stickers_createStickerSet, tL_error);
                }
                if (tLObject instanceof TLRPC.TL_messages_stickerSet) {
                    NotificationCenter notificationCenter = SendMessagesHelper.this.getNotificationCenter();
                    int i = NotificationCenter.stickersImportComplete;
                    if (notificationCenter.hasObservers(i)) {
                        SendMessagesHelper.this.getNotificationCenter().lambda$postNotificationNameOnUIThread$1(i, tLObject);
                    } else {
                        SendMessagesHelper.this.getMediaDataController().toggleStickerSet(null, tLObject, 2, null, false, false);
                    }
                }
            }
        }

        public void setImportProgress(int i) {
            if (i == 100) {
                SendMessagesHelper.this.importingStickersMap.remove(this.shortName);
            }
            SendMessagesHelper.this.getNotificationCenter().lambda$postNotificationNameOnUIThread$1(NotificationCenter.stickersImportProgressChanged, this.shortName);
        }
    }

    static {
        int iAvailableProcessors = Runtime.getRuntime().availableProcessors();
        mediaSendThreadPool = new ThreadPoolExecutor(iAvailableProcessors, iAvailableProcessors, 60L, TimeUnit.SECONDS, new LinkedBlockingQueue());
        Instance = new SendMessagesHelper[16];
    }

    /* JADX INFO: Access modifiers changed from: private */
    static class MediaSendPrepareWorker {
        public volatile String parentObject;
        public volatile TLRPC.TL_photo photo;
        public CountDownLatch sync;

        private MediaSendPrepareWorker() {
        }
    }

    @SuppressLint({"MissingPermission"})
    public static class LocationProvider {
        private LocationProviderDelegate delegate;
        private GpsLocationListener gpsLocationListener;
        private Location lastKnownLocation;
        private LocationManager locationManager;
        private Runnable locationQueryCancelRunnable;
        private GpsLocationListener networkLocationListener;

        public interface LocationProviderDelegate {
            void onLocationAcquired(Location location);

            void onUnableLocationAcquire();
        }

        private class GpsLocationListener implements LocationListener {
            @Override // android.location.LocationListener
            public void onProviderDisabled(String str) {
            }

            @Override // android.location.LocationListener
            public void onProviderEnabled(String str) {
            }

            @Override // android.location.LocationListener
            public void onStatusChanged(String str, int i, Bundle bundle) {
            }

            private GpsLocationListener() {
            }

            @Override // android.location.LocationListener
            public void onLocationChanged(Location location) {
                if (location == null || LocationProvider.this.locationQueryCancelRunnable == null) {
                    return;
                }
                if (BuildVars.LOGS_ENABLED) {
                    FileLog.d("found location " + location);
                }
                LocationProvider.this.lastKnownLocation = location;
                if (location.getAccuracy() < 100.0f) {
                    if (LocationProvider.this.delegate != null) {
                        LocationProvider.this.delegate.onLocationAcquired(location);
                    }
                    if (LocationProvider.this.locationQueryCancelRunnable != null) {
                        AndroidUtilities.cancelRunOnUIThread(LocationProvider.this.locationQueryCancelRunnable);
                    }
                    LocationProvider.this.cleanup();
                }
            }
        }

        public LocationProvider() {
            this.gpsLocationListener = new GpsLocationListener();
            this.networkLocationListener = new GpsLocationListener();
        }

        public LocationProvider(LocationProviderDelegate locationProviderDelegate) {
            this.gpsLocationListener = new GpsLocationListener();
            this.networkLocationListener = new GpsLocationListener();
            this.delegate = locationProviderDelegate;
        }

        public void setDelegate(LocationProviderDelegate locationProviderDelegate) {
            this.delegate = locationProviderDelegate;
        }

        /* JADX INFO: Access modifiers changed from: private */
        public void cleanup() {
            this.locationManager.removeUpdates(this.gpsLocationListener);
            this.locationManager.removeUpdates(this.networkLocationListener);
            this.lastKnownLocation = null;
            this.locationQueryCancelRunnable = null;
        }

        public void start() {
            if (this.locationManager == null) {
                this.locationManager = (LocationManager) ApplicationLoader.applicationContext.getSystemService("location");
            }
            try {
                this.locationManager.requestLocationUpdates("gps", 1L, 0.0f, this.gpsLocationListener);
            } catch (Exception e) {
                FileLog.e(e);
            }
            try {
                this.locationManager.requestLocationUpdates("network", 1L, 0.0f, this.networkLocationListener);
            } catch (Exception e2) {
                FileLog.e(e2);
            }
            try {
                Location lastKnownLocation = this.locationManager.getLastKnownLocation("gps");
                this.lastKnownLocation = lastKnownLocation;
                if (lastKnownLocation == null) {
                    this.lastKnownLocation = this.locationManager.getLastKnownLocation("network");
                }
            } catch (Exception e3) {
                FileLog.e(e3);
            }
            Runnable runnable = this.locationQueryCancelRunnable;
            if (runnable != null) {
                AndroidUtilities.cancelRunOnUIThread(runnable);
            }
            Runnable runnable2 = new Runnable() { // from class: org.telegram.messenger.SendMessagesHelper$LocationProvider$$ExternalSyntheticLambda0
                @Override // java.lang.Runnable
                public final void run() {
                    this.f$0.lambda$start$0();
                }
            };
            this.locationQueryCancelRunnable = runnable2;
            AndroidUtilities.runOnUIThread(runnable2, 5000L);
        }

        /* JADX INFO: Access modifiers changed from: private */
        public /* synthetic */ void lambda$start$0() {
            LocationProviderDelegate locationProviderDelegate = this.delegate;
            if (locationProviderDelegate != null) {
                Location location = this.lastKnownLocation;
                if (location != null) {
                    locationProviderDelegate.onLocationAcquired(location);
                } else {
                    locationProviderDelegate.onUnableLocationAcquire();
                }
            }
            cleanup();
        }

        public void stop() {
            if (this.locationManager == null) {
                return;
            }
            Runnable runnable = this.locationQueryCancelRunnable;
            if (runnable != null) {
                AndroidUtilities.cancelRunOnUIThread(runnable);
            }
            cleanup();
        }
    }

    protected class DelayedMessageSendAfterRequest {
        public DelayedMessage delayedMessage;
        public MessageObject msgObj;
        public ArrayList<MessageObject> msgObjs;
        public String originalPath;
        public ArrayList<String> originalPaths;
        public Object parentObject;
        public ArrayList<Object> parentObjects;
        public TLObject request;
        public boolean scheduled;

        protected DelayedMessageSendAfterRequest() {
        }
    }

    protected class DelayedMessage {
        public TLRPC.InputFile coverFile;
        public TLRPC.PhotoSize coverPhotoSize;
        public TLRPC.EncryptedChat encryptedChat;
        public HashMap<Object, Object> extraHashMap;
        public int finalGroupMessage;
        public boolean forceReupload;
        public long groupId;
        public String httpLocation;
        public ArrayList<String> httpLocations;
        public ArrayList<TLRPC.InputMedia> inputMedias;
        public TLRPC.InputMedia inputUploadMedia;
        public TLObject locationParent;
        public ArrayList<TLRPC.PhotoSize> locations;
        public ArrayList<MessageObject> messageObjects;
        public ArrayList<TLRPC.Message> messages;
        public MessageObject obj;
        public String originalPath;
        public ArrayList<String> originalPaths;
        public boolean paidMedia;
        public Object parentObject;
        public ArrayList<Object> parentObjects;
        public long peer;
        public boolean performCoverUpload;
        public boolean performMediaUpload;
        public TLRPC.PhotoSize photoSize;
        ArrayList<DelayedMessageSendAfterRequest> requests;
        private boolean retriedToSend;
        public boolean[] retriedToSendArray;
        public boolean scheduled;
        public TLObject sendEncryptedRequest;
        public TLObject sendRequest;
        public int topMessageId;
        public int type;
        public VideoEditedInfo videoEditedInfo;
        public ArrayList<VideoEditedInfo> videoEditedInfos;

        public boolean getRetriedToSend(int i) {
            boolean[] zArr;
            if (i < 0 || (zArr = this.retriedToSendArray) == null || i >= zArr.length) {
                return this.retriedToSend;
            }
            return zArr[i];
        }

        public void setRetriedToSend(int i, boolean z) {
            if (i < 0) {
                this.retriedToSend = z;
                return;
            }
            if (this.retriedToSendArray == null) {
                this.retriedToSendArray = new boolean[this.messageObjects.size()];
            }
            this.retriedToSendArray[i] = z;
        }

        public DelayedMessage(long j) {
            this.peer = j;
        }

        public void initForGroup(long j) {
            this.type = 4;
            this.groupId = j;
            this.messageObjects = new ArrayList<>();
            this.messages = new ArrayList<>();
            this.inputMedias = new ArrayList<>();
            this.originalPaths = new ArrayList<>();
            this.parentObjects = new ArrayList<>();
            this.extraHashMap = new HashMap<>();
            this.locations = new ArrayList<>();
            this.httpLocations = new ArrayList<>();
            this.videoEditedInfos = new ArrayList<>();
        }

        public void addDelayedRequest(TLObject tLObject, MessageObject messageObject, String str, Object obj, DelayedMessage delayedMessage, boolean z) {
            DelayedMessageSendAfterRequest delayedMessageSendAfterRequest = SendMessagesHelper.this.new DelayedMessageSendAfterRequest();
            delayedMessageSendAfterRequest.request = tLObject;
            delayedMessageSendAfterRequest.msgObj = messageObject;
            delayedMessageSendAfterRequest.originalPath = str;
            delayedMessageSendAfterRequest.delayedMessage = delayedMessage;
            delayedMessageSendAfterRequest.parentObject = obj;
            delayedMessageSendAfterRequest.scheduled = z;
            if (this.requests == null) {
                this.requests = new ArrayList<>();
            }
            this.requests.add(delayedMessageSendAfterRequest);
        }

        public void addDelayedRequest(TLObject tLObject, ArrayList<MessageObject> arrayList, ArrayList<String> arrayList2, ArrayList<Object> arrayList3, DelayedMessage delayedMessage, boolean z) {
            DelayedMessageSendAfterRequest delayedMessageSendAfterRequest = SendMessagesHelper.this.new DelayedMessageSendAfterRequest();
            delayedMessageSendAfterRequest.request = tLObject;
            delayedMessageSendAfterRequest.msgObjs = arrayList;
            delayedMessageSendAfterRequest.originalPaths = arrayList2;
            delayedMessageSendAfterRequest.delayedMessage = delayedMessage;
            delayedMessageSendAfterRequest.parentObjects = arrayList3;
            delayedMessageSendAfterRequest.scheduled = z;
            if (this.requests == null) {
                this.requests = new ArrayList<>();
            }
            this.requests.add(delayedMessageSendAfterRequest);
        }

        public void sendDelayedRequests() {
            ArrayList<DelayedMessageSendAfterRequest> arrayList = this.requests;
            if (arrayList != null) {
                int i = this.type;
                if (i == 4 || i == 0) {
                    int size = arrayList.size();
                    for (int i2 = 0; i2 < size; i2++) {
                        DelayedMessageSendAfterRequest delayedMessageSendAfterRequest = this.requests.get(i2);
                        TLObject tLObject = delayedMessageSendAfterRequest.request;
                        if (tLObject instanceof TLRPC.TL_messages_sendEncryptedMultiMedia) {
                            SendMessagesHelper.this.getSecretChatHelper().performSendEncryptedRequest((TLRPC.TL_messages_sendEncryptedMultiMedia) delayedMessageSendAfterRequest.request, this);
                        } else if (tLObject instanceof TLRPC.TL_messages_sendMultiMedia) {
                            SendMessagesHelper.this.lambda$performSendMessageRequestMulti$57((TLRPC.TL_messages_sendMultiMedia) tLObject, delayedMessageSendAfterRequest.msgObjs, delayedMessageSendAfterRequest.originalPaths, delayedMessageSendAfterRequest.parentObjects, delayedMessageSendAfterRequest.delayedMessage, delayedMessageSendAfterRequest.scheduled);
                        } else if ((tLObject instanceof TLRPC.TL_messages_sendMedia) && (((TLRPC.TL_messages_sendMedia) tLObject).media instanceof TLRPC.TL_inputMediaPaidMedia)) {
                            SendMessagesHelper.this.lambda$performSendMessageRequestMulti$57((TLRPC.TL_messages_sendMedia) tLObject, delayedMessageSendAfterRequest.msgObjs, delayedMessageSendAfterRequest.originalPaths, delayedMessageSendAfterRequest.parentObjects, delayedMessageSendAfterRequest.delayedMessage, delayedMessageSendAfterRequest.scheduled);
                        } else {
                            SendMessagesHelper.this.performSendMessageRequest(tLObject, delayedMessageSendAfterRequest.msgObj, delayedMessageSendAfterRequest.originalPath, delayedMessageSendAfterRequest.delayedMessage, delayedMessageSendAfterRequest.parentObject, null, delayedMessageSendAfterRequest.scheduled);
                        }
                    }
                    this.requests = null;
                }
            }
        }

        public void markAsError() {
            if (this.type == 4) {
                for (int i = 0; i < this.messageObjects.size(); i++) {
                    MessageObject messageObject = this.messageObjects.get(i);
                    SendMessagesHelper.this.getMessagesStorage().markMessageAsSendError(messageObject.messageOwner, messageObject.scheduled ? 1 : 0);
                    TLRPC.Message message = messageObject.messageOwner;
                    message.send_state = 2;
                    message.errorAllowedPriceStars = 0L;
                    message.errorNewPriceStars = 0L;
                    SendMessagesHelper.this.getNotificationCenter().lambda$postNotificationNameOnUIThread$1(NotificationCenter.messageSendError, Integer.valueOf(messageObject.getId()));
                    SendMessagesHelper.this.processSentMessage(messageObject.getId());
                    SendMessagesHelper.this.removeFromUploadingMessages(messageObject.getId(), this.scheduled);
                }
                SendMessagesHelper.this.delayedMessages.remove("group_" + this.groupId);
            } else {
                MessagesStorage messagesStorage = SendMessagesHelper.this.getMessagesStorage();
                MessageObject messageObject2 = this.obj;
                messagesStorage.markMessageAsSendError(messageObject2.messageOwner, messageObject2.scheduled ? 1 : 0);
                TLRPC.Message message2 = this.obj.messageOwner;
                message2.send_state = 2;
                message2.errorAllowedPriceStars = 0L;
                message2.errorNewPriceStars = 0L;
                SendMessagesHelper.this.getNotificationCenter().lambda$postNotificationNameOnUIThread$1(NotificationCenter.messageSendError, Integer.valueOf(this.obj.getId()));
                SendMessagesHelper.this.processSentMessage(this.obj.getId());
                SendMessagesHelper.this.removeFromUploadingMessages(this.obj.getId(), this.scheduled);
            }
            sendDelayedRequests();
        }
    }

    public static SendMessagesHelper getInstance(int i) {
        SendMessagesHelper sendMessagesHelper;
        SendMessagesHelper sendMessagesHelper2 = Instance[i];
        if (sendMessagesHelper2 != null) {
            return sendMessagesHelper2;
        }
        synchronized (SendMessagesHelper.class) {
            try {
                sendMessagesHelper = Instance[i];
                if (sendMessagesHelper == null) {
                    SendMessagesHelper[] sendMessagesHelperArr = Instance;
                    SendMessagesHelper sendMessagesHelper3 = new SendMessagesHelper(i);
                    sendMessagesHelperArr[i] = sendMessagesHelper3;
                    sendMessagesHelper = sendMessagesHelper3;
                }
            } catch (Throwable th) {
                throw th;
            }
        }
        return sendMessagesHelper;
    }

    public SendMessagesHelper(int i) {
        super(i);
        this.delayedMessages = new HashMap<>();
        this.unsentMessages = new SparseArray<>();
        this.sendingMessages = new SparseArray<>();
        this.editingMessages = new SparseArray<>();
        this.uploadMessages = new SparseArray<>();
        this.sendingMessagesIdDialogs = new LongSparseArray();
        this.uploadingMessagesIdDialogs = new LongSparseArray();
        this.waitingForLocation = new HashMap<>();
        this.waitingForCallback = new HashMap<>();
        this.waitingForCallbackMap = new HashMap<>();
        this.waitingForVote = new HashMap<>();
        this.voteSendTime = new LongSparseArray();
        this.importingHistoryFiles = new HashMap<>();
        this.importingHistoryMap = new LongSparseArray();
        this.importingStickersFiles = new HashMap<>();
        this.importingStickersMap = new HashMap<>();
        this.locationProvider = new LocationProvider(new LocationProvider.LocationProviderDelegate() { // from class: org.telegram.messenger.SendMessagesHelper.1
            @Override // org.telegram.messenger.SendMessagesHelper.LocationProvider.LocationProviderDelegate
            public void onLocationAcquired(Location location) {
                SendMessagesHelper.this.sendLocation(location);
                SendMessagesHelper.this.waitingForLocation.clear();
            }

            @Override // org.telegram.messenger.SendMessagesHelper.LocationProvider.LocationProviderDelegate
            public void onUnableLocationAcquire() {
                SendMessagesHelper.this.getNotificationCenter().lambda$postNotificationNameOnUIThread$1(NotificationCenter.wasUnableToFindCurrentLocation, new HashMap(SendMessagesHelper.this.waitingForLocation));
                SendMessagesHelper.this.waitingForLocation.clear();
            }
        });
        this.waitingForTodoUpdate = new HashMap<>();
        this.hooks = PluginsController.getInstance();
        AndroidUtilities.runOnUIThread(new Runnable() { // from class: org.telegram.messenger.SendMessagesHelper$$ExternalSyntheticLambda21
            @Override // java.lang.Runnable
            public final void run() {
                this.f$0.lambda$new$0();
            }
        });
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$new$0() {
        getNotificationCenter().addObserver(this, NotificationCenter.fileUploaded);
        getNotificationCenter().addObserver(this, NotificationCenter.fileUploadProgressChanged);
        getNotificationCenter().addObserver(this, NotificationCenter.fileUploadFailed);
        getNotificationCenter().addObserver(this, NotificationCenter.filePreparingStarted);
        getNotificationCenter().addObserver(this, NotificationCenter.fileNewChunkAvailable);
        getNotificationCenter().addObserver(this, NotificationCenter.filePreparingFailed);
        getNotificationCenter().addObserver(this, NotificationCenter.httpFileDidFailedLoad);
        getNotificationCenter().addObserver(this, NotificationCenter.httpFileDidLoad);
        getNotificationCenter().addObserver(this, NotificationCenter.fileLoaded);
        getNotificationCenter().addObserver(this, NotificationCenter.fileLoadFailed);
    }

    public void cleanup() {
        this.delayedMessages.clear();
        this.unsentMessages.clear();
        this.sendingMessages.clear();
        this.editingMessages.clear();
        this.sendingMessagesIdDialogs.clear();
        this.uploadMessages.clear();
        this.uploadingMessagesIdDialogs.clear();
        this.waitingForLocation.clear();
        this.waitingForCallback.clear();
        this.waitingForVote.clear();
        this.importingHistoryFiles.clear();
        this.importingHistoryMap.clear();
        this.importingStickersFiles.clear();
        this.importingStickersMap.clear();
        this.locationProvider.stop();
    }

    /* JADX WARN: Code duplicated, block: B:142:0x03c3  */
    /* JADX WARN: Code duplicated, block: B:355:0x08a1  */
    /* JADX WARN: Code duplicated, block: B:357:0x08df  */
    /* JADX WARN: Code duplicated, block: B:359:0x08e2  */
    /* JADX WARN: Instruction removed from duplicated block: B:355:0x08a1, please report this as an issue */
    /* JADX WARN: Instruction removed from duplicated block: B:359:0x08e2, please report this as an issue */
    /* JADX WARN: Multi-variable type inference failed */
    /* JADX WARN: Type inference failed for: r12v0 */
    /* JADX WARN: Type inference failed for: r12v11 */
    /* JADX WARN: Type inference failed for: r12v12 */
    /* JADX WARN: Type inference failed for: r12v14 */
    /* JADX WARN: Type inference failed for: r12v5, types: [boolean] */
    /* JADX WARN: Type inference failed for: r9v12 */
    /* JADX WARN: Type inference failed for: r9v18, types: [int] */
    /* JADX WARN: Type inference failed for: r9v29 */
    @Override // org.telegram.messenger.NotificationCenter.NotificationCenterDelegate
    public void didReceivedNotification(int i, int i2, Object... objArr) {
        String str;
        ArrayList<DelayedMessage> arrayList;
        byte b;
        final MessageObject messageObject;
        MessageObject messageObject2;
        String str2;
        ArrayList<DelayedMessage> arrayList2;
        ArrayList<DelayedMessage> arrayList3;
        int quickReplyId;
        int i3;
        int quickReplyId2;
        int i4;
        TLRPC.InputMedia inputMedia;
        int i5;
        char c;
        String str3;
        TLRPC.InputFile inputFile;
        ArrayList<DelayedMessage> arrayList4;
        String str4;
        TLRPC.InputEncryptedFile inputEncryptedFile;
        int i6;
        TLObject tLObject;
        TLRPC.TL_decryptedMessage tL_decryptedMessage;
        String str5;
        ArrayList<DelayedMessage> arrayList5;
        int i7;
        TLRPC.InputEncryptedFile inputEncryptedFile2;
        int i8;
        int i9;
        TLRPC.PhotoSize photoSize;
        TLRPC.PhotoSize photoSize2;
        TLRPC.PhotoSize photoSize3;
        TLRPC.PhotoSize photoSize4;
        TLRPC.PhotoSize photoSize5;
        MessageObject messageObject3;
        VideoEditedInfo videoEditedInfo;
        final SendMessagesHelper sendMessagesHelper = this;
        int i10 = 0;
        ?? r12 = 1;
        if (i == NotificationCenter.fileUploadProgressChanged) {
            String str6 = (String) objArr[0];
            ImportingHistory importingHistory = sendMessagesHelper.importingHistoryFiles.get(str6);
            if (importingHistory != null) {
                Long l = (Long) objArr[1];
                importingHistory.addUploadProgress(str6, l.longValue(), l.longValue() / ((Long) objArr[2]).longValue());
            }
            ImportingStickers importingStickers = sendMessagesHelper.importingStickersFiles.get(str6);
            if (importingStickers != null) {
                Long l2 = (Long) objArr[1];
                importingStickers.addUploadProgress(str6, l2.longValue(), l2.longValue() / ((Long) objArr[2]).longValue());
                return;
            }
            return;
        }
        char c2 = 5;
        String str7 = "_t";
        int i11 = 4;
        if (i == NotificationCenter.fileUploaded) {
            String str8 = (String) objArr[0];
            TLRPC.InputFile inputFile2 = (TLRPC.InputFile) objArr[1];
            TLRPC.InputEncryptedFile inputEncryptedFile3 = (TLRPC.InputEncryptedFile) objArr[2];
            ImportingHistory importingHistory2 = sendMessagesHelper.importingHistoryFiles.get(str8);
            if (importingHistory2 != null) {
                if (str8.equals(importingHistory2.historyPath)) {
                    importingHistory2.initImport(inputFile2);
                } else {
                    importingHistory2.onMediaImport(str8, ((Long) objArr[5]).longValue(), inputFile2);
                }
            }
            ImportingStickers importingStickers2 = sendMessagesHelper.importingStickersFiles.get(str8);
            if (importingStickers2 != null) {
                importingStickers2.onMediaImport(str8, ((Long) objArr[5]).longValue(), inputFile2);
            }
            ArrayList<DelayedMessage> arrayList6 = sendMessagesHelper.delayedMessages.get(str8);
            if (arrayList6 != null) {
                int i12 = 0;
                while (i12 < arrayList6.size()) {
                    DelayedMessage delayedMessage = arrayList6.get(i12);
                    TLObject tLObject2 = delayedMessage.sendRequest;
                    if (tLObject2 instanceof TLRPC.TL_messages_sendMedia) {
                        inputMedia = ((TLRPC.TL_messages_sendMedia) tLObject2).media;
                        if (inputMedia instanceof TLRPC.TL_inputMediaPaidMedia) {
                            HashMap<Object, Object> map = delayedMessage.extraHashMap;
                            if (map == null) {
                                inputMedia = (TLRPC.InputMedia) ((TLRPC.TL_inputMediaPaidMedia) inputMedia).extended_media.get(i10);
                            } else {
                                inputMedia = (TLRPC.InputMedia) map.get(str8);
                            }
                        }
                    } else if (tLObject2 instanceof TLRPC.TL_messages_editMessage) {
                        inputMedia = ((TLRPC.TL_messages_editMessage) tLObject2).media;
                    } else {
                        inputMedia = tLObject2 instanceof TLRPC.TL_messages_sendMultiMedia ? (TLRPC.InputMedia) delayedMessage.extraHashMap.get(str8) : null;
                    }
                    if (inputFile2 == null || inputMedia == null) {
                        int i13 = i11;
                        i5 = i10;
                        c = c2;
                        str3 = str8;
                        inputFile = inputFile2;
                        arrayList4 = arrayList6;
                        int i14 = i12;
                        str4 = str7;
                        inputEncryptedFile = inputEncryptedFile3;
                        if (inputEncryptedFile == null || (tLObject = delayedMessage.sendEncryptedRequest) == null) {
                            r12 = r12 == true ? 1 : 0;
                            i6 = i14;
                        } else {
                            if (delayedMessage.type == i13) {
                                TLRPC.TL_messages_sendEncryptedMultiMedia tL_messages_sendEncryptedMultiMedia = (TLRPC.TL_messages_sendEncryptedMultiMedia) tLObject;
                                TLRPC.InputEncryptedFile inputEncryptedFile4 = (TLRPC.InputEncryptedFile) delayedMessage.extraHashMap.get(str3);
                                int iIndexOf = tL_messages_sendEncryptedMultiMedia.files.indexOf(inputEncryptedFile4);
                                if (iIndexOf >= 0) {
                                    tL_messages_sendEncryptedMultiMedia.files.set(iIndexOf, inputEncryptedFile);
                                    if (inputEncryptedFile4.id == 1) {
                                        delayedMessage.photoSize = (TLRPC.PhotoSize) delayedMessage.extraHashMap.get(str3 + str4);
                                    }
                                    tL_decryptedMessage = (TLRPC.TL_decryptedMessage) tL_messages_sendEncryptedMultiMedia.messages.get(iIndexOf);
                                } else {
                                    tL_decryptedMessage = null;
                                }
                            } else {
                                tL_decryptedMessage = (TLRPC.TL_decryptedMessage) tLObject;
                            }
                            if (tL_decryptedMessage != null) {
                                TLRPC.DecryptedMessageMedia decryptedMessageMedia = tL_decryptedMessage.media;
                                if ((decryptedMessageMedia instanceof TLRPC.TL_decryptedMessageMediaVideo) || (decryptedMessageMedia instanceof TLRPC.TL_decryptedMessageMediaPhoto) || (decryptedMessageMedia instanceof TLRPC.TL_decryptedMessageMediaDocument)) {
                                    tL_decryptedMessage.media.size = ((Long) objArr[c]).longValue();
                                }
                                TLRPC.DecryptedMessageMedia decryptedMessageMedia2 = tL_decryptedMessage.media;
                                decryptedMessageMedia2.key = (byte[]) objArr[3];
                                decryptedMessageMedia2.iv = (byte[]) objArr[i13];
                                if (delayedMessage.type == i13) {
                                    sendMessagesHelper.uploadMultiMedia(delayedMessage, null, inputEncryptedFile, str3);
                                } else {
                                    SecretChatHelper secretChatHelper = sendMessagesHelper.getSecretChatHelper();
                                    MessageObject messageObject4 = delayedMessage.obj;
                                    secretChatHelper.performSendEncryptedRequest(tL_decryptedMessage, messageObject4.messageOwner, delayedMessage.encryptedChat, inputEncryptedFile, delayedMessage.originalPath, messageObject4);
                                }
                            }
                            arrayList4.remove(i14);
                            i6 = i14 - 1;
                        }
                    } else {
                        int i15 = delayedMessage.type;
                        if (i15 == 0) {
                            inputMedia.file = inputFile2;
                            i5 = i10;
                            c = c2;
                            str5 = str7;
                            i7 = i12;
                            inputFile = inputFile2;
                            inputEncryptedFile2 = inputEncryptedFile3;
                            arrayList5 = arrayList6;
                            str3 = str8;
                            sendMessagesHelper.lambda$performSendMessageRequest$71(delayedMessage.sendRequest, delayedMessage.obj, delayedMessage.originalPath, delayedMessage, true, null, delayedMessage.parentObject, null, delayedMessage.scheduled);
                        } else {
                            str5 = str7;
                            int i16 = i11;
                            arrayList5 = arrayList6;
                            i7 = i12;
                            i5 = i10;
                            c = c2;
                            inputEncryptedFile2 = inputEncryptedFile3;
                            str3 = str8;
                            inputFile = inputFile2;
                            if (i15 == r12) {
                                if (inputMedia.file == null && ((photoSize4 = delayedMessage.coverPhotoSize) == null || delayedMessage.performMediaUpload)) {
                                    inputMedia.file = inputFile;
                                    if (delayedMessage.coverFile == null && photoSize4 != null) {
                                        sendMessagesHelper.performSendDelayedMessage(delayedMessage);
                                    } else if (inputMedia.thumb == null && (photoSize5 = delayedMessage.photoSize) != null && photoSize5.location != null && ((messageObject3 = delayedMessage.obj) == null || (videoEditedInfo = messageObject3.videoEditedInfo) == null || !videoEditedInfo.isSticker)) {
                                        sendMessagesHelper.performSendDelayedMessage(delayedMessage);
                                    } else {
                                        sendMessagesHelper.performSendMessageRequest(delayedMessage.sendRequest, delayedMessage.obj, delayedMessage.originalPath, null, delayedMessage.parentObject, null, delayedMessage.scheduled);
                                    }
                                } else if (delayedMessage.coverFile == null && delayedMessage.coverPhotoSize != null) {
                                    delayedMessage.coverFile = inputFile;
                                    delayedMessage.performCoverUpload = r12;
                                    sendMessagesHelper.performSendDelayedMessage(delayedMessage);
                                } else {
                                    inputMedia.thumb = inputFile;
                                    inputMedia.flags |= i16;
                                    sendMessagesHelper.performSendMessageRequest(delayedMessage.sendRequest, delayedMessage.obj, delayedMessage.originalPath, null, delayedMessage.parentObject, null, delayedMessage.scheduled);
                                }
                            } else if (i15 == 2) {
                                if (inputMedia.file == null) {
                                    inputMedia.file = inputFile;
                                    if (inputMedia.thumb == null && (photoSize3 = delayedMessage.photoSize) != null && photoSize3.location != null) {
                                        sendMessagesHelper.performSendDelayedMessage(delayedMessage);
                                    } else {
                                        sendMessagesHelper.performSendMessageRequest(delayedMessage.sendRequest, delayedMessage.obj, delayedMessage.originalPath, null, delayedMessage.parentObject, null, delayedMessage.scheduled);
                                        sendMessagesHelper = this;
                                    }
                                } else {
                                    inputMedia.thumb = inputFile;
                                    inputMedia.flags |= i16;
                                    sendMessagesHelper = this;
                                    sendMessagesHelper.performSendMessageRequest(delayedMessage.sendRequest, delayedMessage.obj, delayedMessage.originalPath, null, delayedMessage.parentObject, null, delayedMessage.scheduled);
                                }
                            } else {
                                if (i15 == 3) {
                                    inputMedia.file = inputFile;
                                    sendMessagesHelper = this;
                                    sendMessagesHelper.performSendMessageRequest(delayedMessage.sendRequest, delayedMessage.obj, delayedMessage.originalPath, null, delayedMessage.parentObject, null, delayedMessage.scheduled);
                                } else {
                                    sendMessagesHelper = this;
                                    if (i15 != i16) {
                                        str4 = str5;
                                    } else if (inputMedia instanceof TLRPC.TL_inputMediaUploadedDocument) {
                                        if (inputMedia.file == null) {
                                            inputMedia.file = inputFile;
                                            int iIndexOf2 = delayedMessage.messageObjects.indexOf((MessageObject) delayedMessage.extraHashMap.get(str3 + "_i"));
                                            HashMap<Object, Object> map2 = delayedMessage.extraHashMap;
                                            StringBuilder sb = new StringBuilder();
                                            sb.append(str3);
                                            str4 = str5;
                                            sb.append(str4);
                                            if (map2.containsKey(sb.toString())) {
                                                delayedMessage.photoSize = (TLRPC.PhotoSize) delayedMessage.extraHashMap.get(str3 + str4);
                                            }
                                            if (delayedMessage.extraHashMap.containsKey(str3 + "_ct")) {
                                                delayedMessage.coverPhotoSize = (TLRPC.PhotoSize) delayedMessage.extraHashMap.get(str3 + "_ct");
                                            }
                                            if (inputMedia.video_cover == null && (photoSize2 = delayedMessage.coverPhotoSize) != null && photoSize2.location != null) {
                                                delayedMessage.performCoverUpload = r12;
                                                sendMessagesHelper.performSendDelayedMessage(delayedMessage, iIndexOf2);
                                            } else if (inputMedia.thumb == null && (photoSize = delayedMessage.photoSize) != null && photoSize.location != null) {
                                                delayedMessage.performMediaUpload = r12;
                                                sendMessagesHelper.performSendDelayedMessage(delayedMessage, iIndexOf2);
                                            } else {
                                                sendMessagesHelper.uploadMultiMedia(delayedMessage, inputMedia, null, str3);
                                            }
                                        } else {
                                            str4 = str5;
                                            String str9 = (String) delayedMessage.extraHashMap.get(str3 + "_doc");
                                            MessageObject messageObject5 = (MessageObject) delayedMessage.extraHashMap.get(str9 + "_i");
                                            if (delayedMessage.extraHashMap.containsKey(str9 + str4)) {
                                                delayedMessage.photoSize = (TLRPC.PhotoSize) delayedMessage.extraHashMap.get(str9 + str4);
                                            }
                                            if (delayedMessage.extraHashMap.containsKey(str9 + "_ct")) {
                                                delayedMessage.coverPhotoSize = (TLRPC.PhotoSize) delayedMessage.extraHashMap.get(str9 + "_ct");
                                            }
                                            int iIndexOf3 = delayedMessage.messageObjects.indexOf(messageObject5);
                                            if (delayedMessage.coverFile == null && delayedMessage.coverPhotoSize != null) {
                                                delayedMessage.coverFile = inputFile;
                                                delayedMessage.performCoverUpload = r12;
                                                sendMessagesHelper.performSendDelayedMessage(delayedMessage, iIndexOf3);
                                            } else {
                                                inputMedia.thumb = inputFile;
                                                inputMedia.flags |= i16;
                                                sendMessagesHelper.uploadMultiMedia(delayedMessage, inputMedia, null, (String) delayedMessage.extraHashMap.get(str3 + "_o"));
                                            }
                                        }
                                    } else {
                                        str4 = str5;
                                        if (inputMedia instanceof TLRPC.TL_inputMediaDocument) {
                                            HashMap<Object, Object> map3 = delayedMessage.extraHashMap;
                                            if (map3 != null) {
                                                if (map3.containsKey(str3 + "_doc")) {
                                                    i9 = r12 == true ? 1 : 0;
                                                } else {
                                                    i9 = i5;
                                                }
                                            } else {
                                                i9 = i5;
                                            }
                                            String str10 = i9 != 0 ? (String) delayedMessage.extraHashMap.get(str3 + "_doc") : str3;
                                            MessageObject messageObject6 = (MessageObject) delayedMessage.extraHashMap.get(str10 + "_i");
                                            HashMap<Object, Object> map4 = delayedMessage.extraHashMap;
                                            if (map4 != null) {
                                                if (map4.containsKey(str10 + str4)) {
                                                    delayedMessage.photoSize = (TLRPC.PhotoSize) delayedMessage.extraHashMap.get(str10 + str4);
                                                }
                                            }
                                            HashMap<Object, Object> map5 = delayedMessage.extraHashMap;
                                            if (map5 != null) {
                                                if (map5.containsKey(str10 + "_ct")) {
                                                    delayedMessage.coverPhotoSize = (TLRPC.PhotoSize) delayedMessage.extraHashMap.get(str10 + "_ct");
                                                }
                                            }
                                            int iIndexOf4 = delayedMessage.messageObjects.indexOf(messageObject6);
                                            if (i9 != 0 && delayedMessage.coverFile == null && delayedMessage.coverPhotoSize != null) {
                                                delayedMessage.coverFile = inputFile;
                                                delayedMessage.performCoverUpload = r12;
                                                sendMessagesHelper.performSendDelayedMessage(delayedMessage, iIndexOf4);
                                            } else if (delayedMessage.photoSize != null && inputMedia.thumb == null) {
                                                inputMedia.thumb = inputFile;
                                                inputMedia.flags |= i16;
                                                sendMessagesHelper.uploadMultiMedia(delayedMessage, inputMedia, null, (String) delayedMessage.extraHashMap.get(str3 + "_o"));
                                            } else {
                                                inputMedia.file = inputFile;
                                                sendMessagesHelper.uploadMultiMedia(delayedMessage, inputMedia, null, str3);
                                            }
                                        } else {
                                            inputMedia.file = inputFile;
                                            sendMessagesHelper.uploadMultiMedia(delayedMessage, inputMedia, null, str3);
                                        }
                                    }
                                    i8 = i7;
                                    arrayList4 = arrayList5;
                                }
                                arrayList4.remove(i8);
                                i6 = i8 - 1;
                                r12 = r12 == true ? 1 : 0;
                                inputEncryptedFile = inputEncryptedFile2;
                            }
                        }
                        i8 = i7;
                        arrayList4 = arrayList5;
                        str4 = str5;
                        arrayList4.remove(i8);
                        i6 = i8 - 1;
                        r12 = r12 == true ? 1 : 0;
                        inputEncryptedFile = inputEncryptedFile2;
                    }
                    i12 = i6 + r12;
                    inputEncryptedFile3 = inputEncryptedFile;
                    str7 = str4;
                    r12 = r12;
                    inputFile2 = inputFile;
                    i11 = 4;
                    c2 = c;
                    arrayList6 = arrayList4;
                    str8 = str3;
                    i10 = i5;
                }
                String str11 = str8;
                if (arrayList6.isEmpty()) {
                    sendMessagesHelper.delayedMessages.remove(str11);
                    return;
                }
                return;
            }
            return;
        }
        boolean z = true;
        if (i == NotificationCenter.fileUploadFailed) {
            String str12 = (String) objArr[0];
            boolean zBooleanValue = ((Boolean) objArr[1]).booleanValue();
            ImportingHistory importingHistory3 = sendMessagesHelper.importingHistoryFiles.get(str12);
            if (importingHistory3 != null) {
                importingHistory3.onFileFailedToUpload(str12);
            }
            ImportingStickers importingStickers3 = sendMessagesHelper.importingStickersFiles.get(str12);
            if (importingStickers3 != null) {
                importingStickers3.onFileFailedToUpload(str12);
            }
            ArrayList<DelayedMessage> arrayList7 = sendMessagesHelper.delayedMessages.get(str12);
            if (arrayList7 != null) {
                int i17 = 0;
                while (i17 < arrayList7.size()) {
                    DelayedMessage delayedMessage2 = arrayList7.get(i17);
                    if ((zBooleanValue && delayedMessage2.sendEncryptedRequest != null) || (!zBooleanValue && delayedMessage2.sendRequest != null)) {
                        delayedMessage2.markAsError();
                        arrayList7.remove(i17);
                        i17--;
                    }
                    i17++;
                }
                if (arrayList7.isEmpty()) {
                    sendMessagesHelper.delayedMessages.remove(str12);
                    return;
                }
                return;
            }
            return;
        }
        if (i == NotificationCenter.filePreparingStarted) {
            MessageObject messageObject7 = (MessageObject) objArr[0];
            if (messageObject7.getId() == 0) {
                return;
            }
            ArrayList<DelayedMessage> arrayList8 = sendMessagesHelper.delayedMessages.get(messageObject7.messageOwner.attachPath);
            if (arrayList8 != null) {
                int i18 = 0;
                while (i18 < arrayList8.size()) {
                    DelayedMessage delayedMessage3 = arrayList8.get(i18);
                    if (delayedMessage3.type == 4) {
                        int iIndexOf5 = delayedMessage3.messageObjects.indexOf(messageObject7);
                        delayedMessage3.photoSize = (TLRPC.PhotoSize) delayedMessage3.extraHashMap.get(messageObject7.messageOwner.attachPath + "_t");
                        if (delayedMessage3.extraHashMap.containsKey(messageObject7.messageOwner.attachPath + "_ct")) {
                            delayedMessage3.coverPhotoSize = (TLRPC.PhotoSize) delayedMessage3.extraHashMap.get(messageObject7.messageOwner.attachPath + "_ct");
                        }
                        delayedMessage3.performMediaUpload = z;
                        sendMessagesHelper.performSendDelayedMessage(delayedMessage3, iIndexOf5);
                        arrayList8.remove(i18);
                        break;
                    }
                    if (delayedMessage3.obj == messageObject7) {
                        delayedMessage3.videoEditedInfo = null;
                        sendMessagesHelper.performSendDelayedMessage(delayedMessage3);
                        arrayList8.remove(i18);
                        break;
                    }
                    i18++;
                    z = true;
                }
                if (arrayList8.isEmpty()) {
                    sendMessagesHelper.delayedMessages.remove(messageObject7.messageOwner.attachPath);
                    return;
                }
                return;
            }
            return;
        }
        if (i == NotificationCenter.fileNewChunkAvailable) {
            MessageObject messageObject8 = (MessageObject) objArr[0];
            if (messageObject8.getId() == 0) {
                return;
            }
            String str13 = (String) objArr[1];
            long jLongValue = ((Long) objArr[2]).longValue();
            long jLongValue2 = ((Long) objArr[3]).longValue();
            sendMessagesHelper.getFileLoader().checkUploadNewDataAvailable(str13, DialogObject.isEncryptedDialog(messageObject8.getDialogId()), jLongValue, jLongValue2, (Float) objArr[4]);
            if (jLongValue2 == 0 || (arrayList3 = sendMessagesHelper.delayedMessages.get(messageObject8.messageOwner.attachPath)) == null) {
                return;
            }
            for (int i19 = 0; i19 < arrayList3.size(); i19++) {
                DelayedMessage delayedMessage4 = arrayList3.get(i19);
                if (delayedMessage4.type == 4) {
                    for (int i20 = 0; i20 < delayedMessage4.messageObjects.size(); i20++) {
                        MessageObject messageObject9 = delayedMessage4.messageObjects.get(i20);
                        if (messageObject9 == messageObject8) {
                            delayedMessage4.obj.shouldRemoveVideoEditedInfo = true;
                            messageObject9.messageOwner.params.remove("ve");
                            TLRPC.Document document = delayedMessage4.obj.getDocument();
                            if (document != null) {
                                document.size = jLongValue2;
                            }
                            ArrayList<TLRPC.Message> arrayList9 = new ArrayList<>();
                            arrayList9.add(messageObject9.messageOwner);
                            if (messageObject9.isQuickReply()) {
                                quickReplyId2 = messageObject9.getQuickReplyId();
                                i4 = 5;
                            } else if (messageObject9.scheduled) {
                                quickReplyId2 = 0;
                                i4 = 1;
                            } else {
                                quickReplyId2 = 0;
                                i4 = 0;
                            }
                            if (delayedMessage4.paidMedia && i20 != 0) {
                                break;
                            }
                            sendMessagesHelper.getMessagesStorage().putMessages(arrayList9, false, true, false, 0, i4, quickReplyId2);
                            break;
                        }
                    }
                } else {
                    MessageObject messageObject10 = delayedMessage4.obj;
                    if (messageObject10 == messageObject8) {
                        messageObject10.shouldRemoveVideoEditedInfo = true;
                        messageObject10.messageOwner.params.remove("ve");
                        TLRPC.Document document2 = delayedMessage4.obj.getDocument();
                        if (document2 != null) {
                            document2.size = jLongValue2;
                        }
                        ArrayList<TLRPC.Message> arrayList10 = new ArrayList<>();
                        arrayList10.add(delayedMessage4.obj.messageOwner);
                        if (delayedMessage4.obj.isQuickReply()) {
                            quickReplyId = delayedMessage4.obj.getQuickReplyId();
                            i3 = 5;
                        } else if (delayedMessage4.obj.scheduled) {
                            quickReplyId = 0;
                            i3 = 1;
                        } else {
                            quickReplyId = 0;
                            i3 = 0;
                        }
                        sendMessagesHelper.getMessagesStorage().putMessages(arrayList10, false, true, false, 0, i3, quickReplyId);
                        return;
                    }
                }
            }
            return;
        }
        if (i == NotificationCenter.filePreparingFailed) {
            MessageObject messageObject11 = (MessageObject) objArr[0];
            if (messageObject11.getId() == 0 || (arrayList2 = sendMessagesHelper.delayedMessages.get((str2 = (String) objArr[1]))) == null) {
                return;
            }
            int i21 = 0;
            while (i21 < arrayList2.size()) {
                DelayedMessage delayedMessage5 = arrayList2.get(i21);
                if (delayedMessage5.type == 4) {
                    for (int i22 = 0; i22 < delayedMessage5.messages.size(); i22++) {
                        if (delayedMessage5.messageObjects.get(i22) == messageObject11) {
                            delayedMessage5.markAsError();
                            arrayList2.remove(i21);
                            i21--;
                            break;
                        }
                    }
                } else if (delayedMessage5.obj == messageObject11) {
                    delayedMessage5.markAsError();
                    arrayList2.remove(i21);
                    i21--;
                    break;
                }
                i21++;
            }
            if (arrayList2.isEmpty()) {
                sendMessagesHelper.delayedMessages.remove(str2);
                return;
            }
            return;
        }
        if (i == NotificationCenter.httpFileDidLoad) {
            final String str14 = (String) objArr[0];
            ArrayList<DelayedMessage> arrayList11 = sendMessagesHelper.delayedMessages.get(str14);
            if (arrayList11 != null) {
                for (int i23 = 0; i23 < arrayList11.size(); i23++) {
                    final DelayedMessage delayedMessage6 = arrayList11.get(i23);
                    int i24 = delayedMessage6.type;
                    if (i24 == 0) {
                        messageObject2 = delayedMessage6.obj;
                    } else {
                        if (i24 == 2) {
                            messageObject2 = delayedMessage6.obj;
                        } else {
                            if (i24 == 4) {
                                messageObject2 = (MessageObject) delayedMessage6.extraHashMap.get(str14);
                                if (messageObject2.getDocument() != null) {
                                }
                            } else {
                                b = -1;
                                messageObject = null;
                            }
                            if (b == 0) {
                                final File file = new File(FileLoader.getDirectory(4), Utilities.MD5(str14) + "." + ImageLoader.getHttpUrlExtension(str14, "file"));
                                sendMessagesHelper = this;
                                Utilities.globalQueue.postRunnable(new Runnable() { // from class: org.telegram.messenger.SendMessagesHelper$$ExternalSyntheticLambda64
                                    @Override // java.lang.Runnable
                                    public final void run() {
                                        this.f$0.lambda$didReceivedNotification$2(file, messageObject, delayedMessage6, str14);
                                    }
                                });
                            } else {
                                if (b == 1) {
                                    final File file2 = new File(FileLoader.getDirectory(4), Utilities.MD5(str14) + ".gif");
                                    Utilities.globalQueue.postRunnable(new Runnable() { // from class: org.telegram.messenger.SendMessagesHelper$$ExternalSyntheticLambda65
                                        @Override // java.lang.Runnable
                                        public final void run() {
                                            this.f$0.lambda$didReceivedNotification$4(delayedMessage6, file2, messageObject);
                                        }
                                    });
                                }
                            }
                        }
                        messageObject = messageObject2;
                        b = 1;
                        if (b == 0) {
                            final File file3 = new File(FileLoader.getDirectory(4), Utilities.MD5(str14) + "." + ImageLoader.getHttpUrlExtension(str14, "file"));
                            sendMessagesHelper = this;
                            Utilities.globalQueue.postRunnable(new Runnable() { // from class: org.telegram.messenger.SendMessagesHelper$$ExternalSyntheticLambda64
                                @Override // java.lang.Runnable
                                public final void run() {
                                    this.f$0.lambda$didReceivedNotification$2(file3, messageObject, delayedMessage6, str14);
                                }
                            });
                        } else {
                            if (b == 1) {
                                final File file4 = new File(FileLoader.getDirectory(4), Utilities.MD5(str14) + ".gif");
                                Utilities.globalQueue.postRunnable(new Runnable() { // from class: org.telegram.messenger.SendMessagesHelper$$ExternalSyntheticLambda65
                                    @Override // java.lang.Runnable
                                    public final void run() {
                                        this.f$0.lambda$didReceivedNotification$4(delayedMessage6, file4, messageObject);
                                    }
                                });
                            }
                        }
                    }
                    messageObject = messageObject2;
                    b = 0;
                    if (b == 0) {
                        final File file5 = new File(FileLoader.getDirectory(4), Utilities.MD5(str14) + "." + ImageLoader.getHttpUrlExtension(str14, "file"));
                        sendMessagesHelper = this;
                        Utilities.globalQueue.postRunnable(new Runnable() { // from class: org.telegram.messenger.SendMessagesHelper$$ExternalSyntheticLambda64
                            @Override // java.lang.Runnable
                            public final void run() {
                                this.f$0.lambda$didReceivedNotification$2(file5, messageObject, delayedMessage6, str14);
                            }
                        });
                    } else {
                        if (b == 1) {
                            final File file6 = new File(FileLoader.getDirectory(4), Utilities.MD5(str14) + ".gif");
                            Utilities.globalQueue.postRunnable(new Runnable() { // from class: org.telegram.messenger.SendMessagesHelper$$ExternalSyntheticLambda65
                                @Override // java.lang.Runnable
                                public final void run() {
                                    this.f$0.lambda$didReceivedNotification$4(delayedMessage6, file6, messageObject);
                                }
                            });
                        }
                    }
                }
                sendMessagesHelper.delayedMessages.remove(str14);
                return;
            }
            return;
        }
        if (i == NotificationCenter.fileLoaded) {
            String str15 = (String) objArr[0];
            ArrayList<DelayedMessage> arrayList12 = sendMessagesHelper.delayedMessages.get(str15);
            if (arrayList12 != null) {
                for (int i25 = 0; i25 < arrayList12.size(); i25++) {
                    sendMessagesHelper.performSendDelayedMessage(arrayList12.get(i25));
                }
                sendMessagesHelper.delayedMessages.remove(str15);
                return;
            }
            return;
        }
        if ((i == NotificationCenter.httpFileDidFailedLoad || i == NotificationCenter.fileLoadFailed) && (arrayList = sendMessagesHelper.delayedMessages.get((str = (String) objArr[0]))) != null) {
            for (int i26 = 0; i26 < arrayList.size(); i26++) {
                arrayList.get(i26).markAsError();
            }
            sendMessagesHelper.delayedMessages.remove(str);
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$didReceivedNotification$2(final File file, final MessageObject messageObject, final DelayedMessage delayedMessage, final String str) {
        final TLRPC.TL_photo tL_photoGeneratePhotoSizes = generatePhotoSizes(file.toString(), null);
        AndroidUtilities.runOnUIThread(new Runnable() { // from class: org.telegram.messenger.SendMessagesHelper$$ExternalSyntheticLambda55
            @Override // java.lang.Runnable
            public final void run() {
                this.f$0.lambda$didReceivedNotification$1(tL_photoGeneratePhotoSizes, messageObject, file, delayedMessage, str);
            }
        });
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$didReceivedNotification$1(TLRPC.TL_photo tL_photo, MessageObject messageObject, File file, DelayedMessage delayedMessage, String str) {
        if (tL_photo != null) {
            TLRPC.Message message = messageObject.messageOwner;
            message.media.photo = tL_photo;
            message.attachPath = file.toString();
            ArrayList<TLRPC.Message> arrayList = new ArrayList<>();
            arrayList.add(messageObject.messageOwner);
            getMessagesStorage().putMessages(arrayList, false, true, false, 0, messageObject.scheduled ? 1 : 0, 0L);
            getNotificationCenter().lambda$postNotificationNameOnUIThread$1(NotificationCenter.updateMessageMedia, messageObject.messageOwner);
            ArrayList arrayList2 = tL_photo.sizes;
            delayedMessage.photoSize = (TLRPC.PhotoSize) arrayList2.get(arrayList2.size() - 1);
            delayedMessage.locationParent = tL_photo;
            delayedMessage.httpLocation = null;
            if (delayedMessage.type == 4) {
                delayedMessage.performMediaUpload = true;
                performSendDelayedMessage(delayedMessage, delayedMessage.messageObjects.indexOf(messageObject));
                return;
            } else {
                performSendDelayedMessage(delayedMessage);
                return;
            }
        }
        if (BuildVars.LOGS_ENABLED) {
            FileLog.e("can't load image " + str + " to file " + file.toString());
        }
        delayedMessage.markAsError();
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$didReceivedNotification$4(final DelayedMessage delayedMessage, final File file, final MessageObject messageObject) {
        final TLRPC.Document document = delayedMessage.obj.getDocument();
        if (document.thumbs.isEmpty() || (document.thumbs.get(0).location instanceof TLRPC.TL_fileLocationUnavailable)) {
            try {
                Bitmap bitmapLoadBitmap = ImageLoader.loadBitmap(file.getAbsolutePath(), null, 90.0f, 90.0f, true);
                if (bitmapLoadBitmap != null) {
                    document.thumbs.clear();
                    document.thumbs.add(ImageLoader.scaleAndSaveImage(bitmapLoadBitmap, 90.0f, 90.0f, 55, delayedMessage.sendEncryptedRequest != null));
                    bitmapLoadBitmap.recycle();
                }
            } catch (Exception e) {
                document.thumbs.clear();
                FileLog.e(e);
            }
        }
        AndroidUtilities.runOnUIThread(new Runnable() { // from class: org.telegram.messenger.SendMessagesHelper$$ExternalSyntheticLambda75
            @Override // java.lang.Runnable
            public final void run() {
                this.f$0.lambda$didReceivedNotification$3(delayedMessage, file, document, messageObject);
            }
        });
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$didReceivedNotification$3(DelayedMessage delayedMessage, File file, TLRPC.Document document, MessageObject messageObject) {
        delayedMessage.httpLocation = null;
        delayedMessage.obj.messageOwner.attachPath = file.toString();
        if (!document.thumbs.isEmpty()) {
            TLRPC.PhotoSize photoSize = document.thumbs.get(0);
            if (!(photoSize instanceof TLRPC.TL_photoStrippedSize)) {
                delayedMessage.photoSize = photoSize;
                delayedMessage.locationParent = document;
            }
        }
        ArrayList<TLRPC.Message> arrayList = new ArrayList<>();
        arrayList.add(messageObject.messageOwner);
        getMessagesStorage().putMessages(arrayList, false, true, false, 0, messageObject.scheduled ? 1 : 0, 0L);
        delayedMessage.performMediaUpload = true;
        performSendDelayedMessage(delayedMessage);
        getNotificationCenter().lambda$postNotificationNameOnUIThread$1(NotificationCenter.updateMessageMedia, delayedMessage.obj.messageOwner);
    }

    private void revertEditingMessageObject(MessageObject messageObject) {
        messageObject.cancelEditing = true;
        TLRPC.Message message = messageObject.messageOwner;
        message.media = messageObject.previousMedia;
        message.message = messageObject.previousMessage;
        ArrayList<TLRPC.MessageEntity> arrayList = messageObject.previousMessageEntities;
        message.entities = arrayList;
        message.attachPath = messageObject.previousAttachPath;
        message.send_state = 0;
        if (arrayList != null) {
            message.flags |= 128;
        } else {
            message.flags &= -129;
        }
        messageObject.previousMedia = null;
        messageObject.previousMessage = null;
        messageObject.previousMessageEntities = null;
        messageObject.previousAttachPath = null;
        messageObject.videoEditedInfo = null;
        messageObject.type = -1;
        messageObject.setType();
        messageObject.caption = null;
        if (messageObject.type != 0) {
            messageObject.generateCaption();
        } else {
            messageObject.resetLayout();
        }
        ArrayList<TLRPC.Message> arrayList2 = new ArrayList<>();
        arrayList2.add(messageObject.messageOwner);
        getMessagesStorage().putMessages(arrayList2, false, true, false, 0, messageObject.scheduled ? 1 : 0, 0L);
        ArrayList arrayList3 = new ArrayList();
        arrayList3.add(messageObject);
        getNotificationCenter().lambda$postNotificationNameOnUIThread$1(NotificationCenter.replaceMessagesObjects, Long.valueOf(messageObject.getDialogId()), arrayList3);
    }

    public void cancelSendingMessage(MessageObject messageObject) {
        ArrayList<MessageObject> arrayList = new ArrayList<>();
        arrayList.add(messageObject);
        if (messageObject != null && messageObject.type == 29) {
            Iterator<Map.Entry<String, ArrayList<DelayedMessage>>> it = this.delayedMessages.entrySet().iterator();
            DelayedMessage delayedMessage = null;
            while (it.hasNext()) {
                ArrayList<DelayedMessage> value = it.next().getValue();
                for (int i = 0; i < value.size(); i++) {
                    DelayedMessage delayedMessage2 = value.get(i);
                    if (delayedMessage2.type == 4) {
                        for (int i2 = 0; i2 < delayedMessage2.messageObjects.size(); i2++) {
                            if (delayedMessage2.messageObjects.get(i2).getId() == messageObject.getId()) {
                                delayedMessage = delayedMessage2;
                                break;
                            }
                        }
                    }
                    if (delayedMessage != null) {
                        break;
                    }
                }
            }
            if (delayedMessage != null) {
                arrayList.clear();
                arrayList.addAll(delayedMessage.messageObjects);
            }
        }
        cancelSendingMessage(arrayList);
    }

    public void cancelSendingMessage(ArrayList<MessageObject> arrayList) {
        ArrayList<Integer> arrayList2;
        int i;
        ArrayList arrayList3 = new ArrayList();
        ArrayList arrayList4 = new ArrayList();
        ArrayList<Integer> arrayList5 = new ArrayList<>();
        long j = 0;
        int i2 = 0;
        boolean z = false;
        int i3 = 0;
        int quickReplyId = 0;
        while (true) {
            boolean z2 = true;
            if (i2 >= arrayList.size()) {
                break;
            }
            MessageObject messageObject = arrayList.get(i2);
            int i4 = messageObject.scheduled ? 1 : i3;
            long dialogId = messageObject.getDialogId();
            arrayList5.add(Integer.valueOf(messageObject.getId()));
            AyuState.permitDeleteMessage(dialogId, messageObject.getId());
            if (messageObject.isQuickReply()) {
                quickReplyId = messageObject.getQuickReplyId();
            }
            TLRPC.Message messageRemoveFromSendingMessages = removeFromSendingMessages(messageObject.getId(), messageObject.scheduled);
            if (messageRemoveFromSendingMessages != null) {
                getConnectionsManager().cancelRequest(messageRemoveFromSendingMessages.reqId, true);
            }
            StarsController.getInstance(this.currentAccount).hidePaidMessageToast(messageObject);
            for (Map.Entry<String, ArrayList<DelayedMessage>> entry : this.delayedMessages.entrySet()) {
                ArrayList<DelayedMessage> value = entry.getValue();
                boolean z3 = z2;
                int i5 = 0;
                while (true) {
                    if (i5 >= value.size()) {
                        arrayList2 = arrayList5;
                        i = i2;
                        break;
                    }
                    DelayedMessage delayedMessage = value.get(i5);
                    arrayList2 = arrayList5;
                    i = i2;
                    if (delayedMessage.type == 4) {
                        MessageObject messageObject2 = null;
                        int i6 = 0;
                        while (true) {
                            if (i6 >= delayedMessage.messageObjects.size()) {
                                i6 = -1;
                                break;
                            }
                            messageObject2 = delayedMessage.messageObjects.get(i6);
                            if (messageObject2.getId() == messageObject.getId()) {
                                removeFromUploadingMessages(messageObject.getId(), messageObject.scheduled);
                                break;
                            }
                            i6++;
                        }
                        if (i6 < 0) {
                            break;
                        }
                        delayedMessage.messageObjects.remove(i6);
                        delayedMessage.messages.remove(i6);
                        delayedMessage.originalPaths.remove(i6);
                        if (!delayedMessage.parentObjects.isEmpty()) {
                            delayedMessage.parentObjects.remove(i6);
                        }
                        TLObject tLObject = delayedMessage.sendRequest;
                        if (tLObject instanceof TLRPC.TL_messages_sendMultiMedia) {
                            ((TLRPC.TL_messages_sendMultiMedia) tLObject).multi_media.remove(i6);
                        } else if ((tLObject instanceof TLRPC.TL_messages_sendMedia) && (((TLRPC.TL_messages_sendMedia) tLObject).media instanceof TLRPC.TL_inputMediaPaidMedia)) {
                            ((TLRPC.TL_inputMediaPaidMedia) ((TLRPC.TL_messages_sendMedia) tLObject).media).extended_media.remove(i6);
                        } else {
                            TLRPC.TL_messages_sendEncryptedMultiMedia tL_messages_sendEncryptedMultiMedia = (TLRPC.TL_messages_sendEncryptedMultiMedia) delayedMessage.sendEncryptedRequest;
                            tL_messages_sendEncryptedMultiMedia.messages.remove(i6);
                            tL_messages_sendEncryptedMultiMedia.files.remove(i6);
                        }
                        MediaController.getInstance().cancelVideoConvert(messageObject);
                        String str = (String) delayedMessage.extraHashMap.get(messageObject2);
                        if (str != null) {
                            arrayList3.add(str);
                        }
                        if (delayedMessage.messageObjects.isEmpty()) {
                            delayedMessage.sendDelayedRequests();
                            break;
                        }
                        if (delayedMessage.finalGroupMessage == messageObject.getId()) {
                            ArrayList<MessageObject> arrayList6 = delayedMessage.messageObjects;
                            MessageObject messageObject3 = arrayList6.get(arrayList6.size() - 1);
                            delayedMessage.finalGroupMessage = messageObject3.getId();
                            messageObject3.messageOwner.params.put("final", "1");
                            TLRPC.TL_messages_messages tL_messages_messages = new TLRPC.TL_messages_messages();
                            tL_messages_messages.messages.add(messageObject3.messageOwner);
                            getMessagesStorage().putMessages((TLRPC.messages_Messages) tL_messages_messages, delayedMessage.peer, -2, 0, false, i4, 0L);
                        }
                        if (!arrayList4.contains(delayedMessage)) {
                            arrayList4.add(delayedMessage);
                            break;
                        }
                        break;
                    }
                    if (delayedMessage.obj.getId() == messageObject.getId()) {
                        removeFromUploadingMessages(messageObject.getId(), messageObject.scheduled);
                        value.remove(i5);
                        delayedMessage.sendDelayedRequests();
                        MediaController.getInstance().cancelVideoConvert(delayedMessage.obj);
                        if (value.size() != 0) {
                            break;
                        }
                        arrayList3.add(entry.getKey());
                        if (delayedMessage.sendEncryptedRequest == null) {
                            break;
                        }
                        z = z3;
                        break;
                    }
                    i5++;
                    arrayList5 = arrayList2;
                    i2 = i;
                }
                z2 = z3;
                arrayList5 = arrayList2;
                i2 = i;
            }
            i2++;
            j = dialogId;
            i3 = i4;
        }
        ArrayList<Integer> arrayList7 = arrayList5;
        for (int i7 = 0; i7 < arrayList3.size(); i7++) {
            String str2 = (String) arrayList3.get(i7);
            if (str2.startsWith("http")) {
                ImageLoader.getInstance().cancelLoadHttpFile(str2);
            } else {
                getFileLoader().cancelFileUpload(str2, z);
            }
            this.delayedMessages.remove(str2);
        }
        int size = arrayList4.size();
        for (int i8 = 0; i8 < size; i8++) {
            sendReadyToSendGroup((DelayedMessage) arrayList4.get(i8), false, true);
        }
        if (arrayList.size() == 1 && arrayList.get(0).isEditing() && arrayList.get(0).previousMedia != null) {
            revertEditingMessageObject(arrayList.get(0));
            return;
        }
        getMessagesController().deleteMessages(arrayList7, null, null, j, quickReplyId, false, (arrayList.isEmpty() || !arrayList.get(0).isQuickReply()) ? i3 != 0 ? 1 : 0 : 5);
    }

    public boolean retrySendMessage(MessageObject messageObject, boolean z, long j) {
        if (messageObject.getId() >= 0) {
            if (messageObject.isEditing()) {
                editMessage(messageObject, null, null, null, null, null, null, true, messageObject.hasMediaSpoilers(), messageObject);
            }
            return false;
        }
        TLRPC.MessageAction messageAction = messageObject.messageOwner.action;
        if (messageAction instanceof TLRPC.TL_messageEncryptedAction) {
            TLRPC.EncryptedChat encryptedChat = getMessagesController().getEncryptedChat(Integer.valueOf(DialogObject.getEncryptedChatId(messageObject.getDialogId())));
            if (encryptedChat == null) {
                getMessagesStorage().markMessageAsSendError(messageObject.messageOwner, messageObject.scheduled ? 1 : 0);
                messageObject.messageOwner.send_state = 2;
                getNotificationCenter().lambda$postNotificationNameOnUIThread$1(NotificationCenter.messageSendError, Integer.valueOf(messageObject.getId()));
                processSentMessage(messageObject.getId());
                return false;
            }
            TLRPC.Message message = messageObject.messageOwner;
            if (message.random_id == 0) {
                message.random_id = getNextRandomId();
            }
            TLRPC.DecryptedMessageAction decryptedMessageAction = messageObject.messageOwner.action.encryptedAction;
            if (decryptedMessageAction instanceof TLRPC.TL_decryptedMessageActionSetMessageTTL) {
                getSecretChatHelper().sendTTLMessage(encryptedChat, messageObject.messageOwner);
            } else if (decryptedMessageAction instanceof TLRPC.TL_decryptedMessageActionDeleteMessages) {
                getSecretChatHelper().sendMessagesDeleteMessage(encryptedChat, null, messageObject.messageOwner);
            } else if (decryptedMessageAction instanceof TLRPC.TL_decryptedMessageActionFlushHistory) {
                getSecretChatHelper().sendClearHistoryMessage(encryptedChat, messageObject.messageOwner);
            } else if (decryptedMessageAction instanceof TLRPC.TL_decryptedMessageActionNotifyLayer) {
                getSecretChatHelper().sendNotifyLayerMessage(encryptedChat, messageObject.messageOwner);
            } else if (decryptedMessageAction instanceof TLRPC.TL_decryptedMessageActionReadMessages) {
                getSecretChatHelper().sendMessagesReadMessage(encryptedChat, null, messageObject.messageOwner);
            } else if (decryptedMessageAction instanceof TLRPC.TL_decryptedMessageActionScreenshotMessages) {
                getSecretChatHelper().sendScreenshotMessage(encryptedChat, null, messageObject.messageOwner);
            } else if (!(decryptedMessageAction instanceof TLRPC.TL_decryptedMessageActionTyping)) {
                if (decryptedMessageAction instanceof TLRPC.TL_decryptedMessageActionResend) {
                    getSecretChatHelper().sendResendMessage(encryptedChat, 0, 0, messageObject.messageOwner);
                } else if (decryptedMessageAction instanceof TLRPC.TL_decryptedMessageActionCommitKey) {
                    getSecretChatHelper().sendCommitKeyMessage(encryptedChat, messageObject.messageOwner);
                } else if (decryptedMessageAction instanceof TLRPC.TL_decryptedMessageActionAbortKey) {
                    getSecretChatHelper().sendAbortKeyMessage(encryptedChat, messageObject.messageOwner, 0L);
                } else if (decryptedMessageAction instanceof TLRPC.TL_decryptedMessageActionRequestKey) {
                    getSecretChatHelper().sendRequestKeyMessage(encryptedChat, messageObject.messageOwner);
                } else if (decryptedMessageAction instanceof TLRPC.TL_decryptedMessageActionAcceptKey) {
                    getSecretChatHelper().sendAcceptKeyMessage(encryptedChat, messageObject.messageOwner);
                } else if (decryptedMessageAction instanceof TLRPC.TL_decryptedMessageActionNoop) {
                    getSecretChatHelper().sendNoopMessage(encryptedChat, messageObject.messageOwner);
                }
            }
            return true;
        }
        if (messageAction instanceof TLRPC.TL_messageActionScreenshotTaken) {
            sendScreenshotMessage(getMessagesController().getUser(Long.valueOf(messageObject.getDialogId())), messageObject.getReplyMsgId(), messageObject.messageOwner);
        }
        if (z) {
            this.unsentMessages.put(messageObject.getId(), messageObject);
        }
        SendMessageParams sendMessageParamsOf = SendMessageParams.of(messageObject);
        sendMessageParamsOf.payStars = j;
        sendMessage(sendMessageParamsOf);
        return true;
    }

    protected void processSentMessage(int i) {
        int size = this.unsentMessages.size();
        this.unsentMessages.remove(i);
        if (size == 0 || this.unsentMessages.size() != 0) {
            return;
        }
        checkUnsentMessages();
    }

    public void processForwardFromMyName(MessageObject messageObject, long j, long j2, long j3, MessageSuggestionParams messageSuggestionParams) {
        if (messageObject == null) {
            return;
        }
        TLRPC.Message message = messageObject.messageOwner;
        TLRPC.MessageMedia messageMedia = message.media;
        ArrayList arrayList = null;
        map = null;
        map = null;
        HashMap map = null;
        arrayList = null;
        if (messageMedia != null && !(messageMedia instanceof TLRPC.TL_messageMediaEmpty) && !(messageMedia instanceof TLRPC.TL_messageMediaWebPage) && !(messageMedia instanceof TLRPC.TL_messageMediaGame) && !(messageMedia instanceof TLRPC.TL_messageMediaInvoice)) {
            if (DialogObject.isEncryptedDialog(j)) {
                TLRPC.Message message2 = messageObject.messageOwner;
                if (message2.peer_id != null) {
                    TLRPC.MessageMedia messageMedia2 = message2.media;
                    if ((messageMedia2.photo instanceof TLRPC.TL_photo) || (messageMedia2.document instanceof TLRPC.TL_document)) {
                        map = new HashMap();
                        map.put("parentObject", "sent_" + messageObject.messageOwner.peer_id.channel_id + "_" + messageObject.getId() + "_" + messageObject.getDialogId() + "_" + messageObject.type + "_" + messageObject.getSize());
                    }
                }
            }
            HashMap map2 = map;
            TLRPC.Message message3 = messageObject.messageOwner;
            TLRPC.MessageMedia messageMedia3 = message3.media;
            TLRPC.Photo photo = messageMedia3.photo;
            if (photo instanceof TLRPC.TL_photo) {
                SendMessageParams sendMessageParamsOf = SendMessageParams.of((TLRPC.TL_photo) photo, null, j, messageObject.replyMessageObject, null, message3.message, message3.entities, null, map2, true, 0, 0, messageMedia3.ttl_seconds, messageObject, false);
                sendMessageParamsOf.payStars = j2;
                sendMessageParamsOf.monoForumPeer = j3;
                sendMessageParamsOf.suggestionParams = messageSuggestionParams;
                sendMessage(sendMessageParamsOf);
                return;
            }
            TLRPC.Document document = messageMedia3.document;
            if (document instanceof TLRPC.TL_document) {
                SendMessageParams sendMessageParamsOf2 = SendMessageParams.of((TLRPC.TL_document) document, null, message3.attachPath, j, messageObject.replyMessageObject, null, message3.message, message3.entities, null, map2, true, 0, 0, messageMedia3.ttl_seconds, messageObject, null, false);
                sendMessageParamsOf2.payStars = j2;
                sendMessageParamsOf2.monoForumPeer = j3;
                sendMessageParamsOf2.suggestionParams = messageSuggestionParams;
                sendMessage(sendMessageParamsOf2);
                return;
            }
            if ((messageMedia3 instanceof TLRPC.TL_messageMediaVenue) || (messageMedia3 instanceof TLRPC.TL_messageMediaGeo)) {
                SendMessageParams sendMessageParamsOf3 = SendMessageParams.of(messageMedia3, j, messageObject.replyMessageObject, (MessageObject) null, (TLRPC.ReplyMarkup) null, (HashMap<String, String>) null, true, 0, 0);
                sendMessageParamsOf3.payStars = j2;
                sendMessageParamsOf3.monoForumPeer = j3;
                sendMessageParamsOf3.suggestionParams = messageSuggestionParams;
                sendMessage(sendMessageParamsOf3);
                return;
            }
            if (messageMedia3.phone_number != null) {
                TLRPC.TL_userContact_old2 tL_userContact_old2 = new TLRPC.TL_userContact_old2();
                TLRPC.MessageMedia messageMedia4 = messageObject.messageOwner.media;
                tL_userContact_old2.phone = messageMedia4.phone_number;
                tL_userContact_old2.first_name = messageMedia4.first_name;
                tL_userContact_old2.last_name = messageMedia4.last_name;
                tL_userContact_old2.id = messageMedia4.user_id;
                SendMessageParams sendMessageParamsOf4 = SendMessageParams.of((TLRPC.User) tL_userContact_old2, j, messageObject.replyMessageObject, (MessageObject) null, (TLRPC.ReplyMarkup) null, (HashMap<String, String>) null, true, 0, 0);
                sendMessageParamsOf4.monoForumPeer = j3;
                sendMessageParamsOf4.suggestionParams = messageSuggestionParams;
                sendMessageParamsOf4.payStars = j2;
                sendMessage(sendMessageParamsOf4);
                return;
            }
            if (DialogObject.isEncryptedDialog(j)) {
                return;
            }
            ArrayList<MessageObject> arrayList2 = new ArrayList<>();
            arrayList2.add(messageObject);
            sendMessage(arrayList2, j, true, false, true, 0, 0, null, -1, j2, j3, messageSuggestionParams);
            return;
        }
        if (message.message != null) {
            TLRPC.WebPage webPage = messageMedia instanceof TLRPC.TL_messageMediaWebPage ? messageMedia.webpage : null;
            ArrayList arrayList3 = message.entities;
            if (arrayList3 != null && !arrayList3.isEmpty()) {
                arrayList = new ArrayList();
                for (int i = 0; i < messageObject.messageOwner.entities.size(); i++) {
                    TLRPC.MessageEntity messageEntity = (TLRPC.MessageEntity) messageObject.messageOwner.entities.get(i);
                    if ((messageEntity instanceof TLRPC.TL_messageEntityBold) || (messageEntity instanceof TLRPC.TL_messageEntityItalic) || (messageEntity instanceof TLRPC.TL_messageEntityPre) || (messageEntity instanceof TLRPC.TL_messageEntityCode) || (messageEntity instanceof TLRPC.TL_messageEntityTextUrl) || (messageEntity instanceof TLRPC.TL_messageEntitySpoiler) || (messageEntity instanceof TLRPC.TL_messageEntityCustomEmoji)) {
                        arrayList.add(messageEntity);
                    }
                }
            }
            SendMessageParams sendMessageParamsOf5 = SendMessageParams.of(messageObject.messageOwner.message, j, messageObject.replyMessageObject, null, webPage, true, arrayList, null, null, true, 0, 0, null, false);
            sendMessageParamsOf5.payStars = j2;
            sendMessageParamsOf5.monoForumPeer = j3;
            sendMessageParamsOf5.suggestionParams = messageSuggestionParams;
            sendMessage(sendMessageParamsOf5);
            return;
        }
        if (DialogObject.isEncryptedDialog(j)) {
            ArrayList<MessageObject> arrayList4 = new ArrayList<>();
            arrayList4.add(messageObject);
            sendMessage(arrayList4, j, true, false, true, 0, 0, null, -1, j2, j3, messageSuggestionParams);
        }
    }

    public void sendScreenshotMessage(TLRPC.User user, int i, TLRPC.Message message) {
        if (user == null || i == 0) {
            return;
        }
        getUserConfig().getClientUserId();
    }

    public void sendScreenshotMessage2(TLRPC.User user, int i, TLRPC.Message message) {
        if (user == null || user.id == getUserConfig().getClientUserId()) {
            return;
        }
        TLRPC.TL_messages_sendScreenshotNotification tL_messages_sendScreenshotNotification = new TLRPC.TL_messages_sendScreenshotNotification();
        TLRPC.TL_inputPeerUser tL_inputPeerUser = new TLRPC.TL_inputPeerUser();
        tL_messages_sendScreenshotNotification.peer = tL_inputPeerUser;
        tL_inputPeerUser.access_hash = user.access_hash;
        tL_inputPeerUser.user_id = user.id;
        tL_messages_sendScreenshotNotification.reply_to = createReplyInput(i);
        TLRPC.TL_messageService tL_messageService = new TLRPC.TL_messageService();
        tL_messageService.random_id = getNextRandomId();
        tL_messageService.dialog_id = user.id;
        tL_messageService.unread = true;
        tL_messageService.out = true;
        int newMessageId = getUserConfig().getNewMessageId();
        tL_messageService.id = newMessageId;
        tL_messageService.local_id = newMessageId;
        TLRPC.TL_peerUser tL_peerUser = new TLRPC.TL_peerUser();
        tL_messageService.from_id = tL_peerUser;
        tL_peerUser.user_id = getUserConfig().getClientUserId();
        tL_messageService.flags |= 264;
        TLRPC.TL_messageReplyHeader tL_messageReplyHeader = new TLRPC.TL_messageReplyHeader();
        tL_messageService.reply_to = tL_messageReplyHeader;
        tL_messageReplyHeader.flags |= 16;
        tL_messageReplyHeader.reply_to_msg_id = i;
        TLRPC.TL_peerUser tL_peerUser2 = new TLRPC.TL_peerUser();
        tL_messageService.peer_id = tL_peerUser2;
        tL_peerUser2.user_id = user.id;
        tL_messageService.date = getConnectionsManager().getCurrentTime();
        tL_messageService.action = new TLRPC.TL_messageActionScreenshotTaken();
        getUserConfig().saveConfig(false);
        tL_messages_sendScreenshotNotification.random_id = tL_messageService.random_id;
        MessageObject messageObject = new MessageObject(this.currentAccount, tL_messageService, false, true);
        messageObject.messageOwner.send_state = 1;
        messageObject.wasJustSent = true;
        getNotificationCenter().lambda$postNotificationNameOnUIThread$1(NotificationCenter.dialogsNeedReload, new Object[0]);
        performSendMessageRequest(tL_messages_sendScreenshotNotification, messageObject, null, null, null, null, false);
    }

    public void sendSticker(TLRPC.Document document, String str, long j, MessageObject messageObject, MessageObject messageObject2, TL_stories.StoryItem storyItem, ChatActivity.ReplyQuote replyQuote, MessageObject.SendAnimationData sendAnimationData, boolean z, int i, int i2, boolean z2, Object obj, String str2, int i3, long j2, long j3, MessageSuggestionParams messageSuggestionParams) {
        sendSticker(document, str, j, null, null, messageObject, messageObject2, storyItem, replyQuote, sendAnimationData, z, i, i2, z2, obj, str2, i3, j2, j3, messageSuggestionParams);
    }

    public void sendSticker(TLRPC.Document document, String str, long j, CharSequence charSequence, VideoEditedInfo videoEditedInfo, MessageObject messageObject, MessageObject messageObject2, TL_stories.StoryItem storyItem, ChatActivity.ReplyQuote replyQuote, MessageObject.SendAnimationData sendAnimationData, boolean z, int i, int i2, boolean z2, Object obj, String str2, int i3, long j2, long j3, MessageSuggestionParams messageSuggestionParams) {
        sendSticker(document, str, j, charSequence, videoEditedInfo, messageObject, messageObject2, storyItem, replyQuote, sendAnimationData, z, i, i2, z2, obj, str2, i3, j2, j3, messageSuggestionParams, false);
    }

    public void sendSticker(TLRPC.Document document, String str, final long j, final CharSequence charSequence, final VideoEditedInfo videoEditedInfo, final MessageObject messageObject, final MessageObject messageObject2, final TL_stories.StoryItem storyItem, final ChatActivity.ReplyQuote replyQuote, final MessageObject.SendAnimationData sendAnimationData, final boolean z, final int i, final int i2, boolean z2, final Object obj, final String str2, final int i3, final long j2, final long j3, final MessageSuggestionParams messageSuggestionParams, final boolean z3) {
        final TLRPC.Document tL_document_layer82;
        HashMap map;
        TLRPC.PhotoSize tL_photoStrippedSize;
        byte[] bArr;
        if (document == null) {
            return;
        }
        if (DialogObject.isEncryptedDialog(j)) {
            if (getMessagesController().getEncryptedChat(Integer.valueOf(DialogObject.getEncryptedChatId(j))) == null) {
                return;
            }
            tL_document_layer82 = new TLRPC.TL_document_layer82();
            tL_document_layer82.id = document.id;
            tL_document_layer82.access_hash = document.access_hash;
            tL_document_layer82.date = document.date;
            tL_document_layer82.mime_type = document.mime_type;
            byte[] bArr2 = document.file_reference;
            tL_document_layer82.file_reference = bArr2;
            if (bArr2 == null) {
                tL_document_layer82.file_reference = new byte[0];
            }
            tL_document_layer82.size = document.size;
            tL_document_layer82.dc_id = document.dc_id;
            tL_document_layer82.attributes = new ArrayList<>();
            for (int i4 = 0; i4 < document.attributes.size(); i4++) {
                TLRPC.DocumentAttribute documentAttribute = document.attributes.get(i4);
                if (documentAttribute instanceof TLRPC.TL_documentAttributeVideo) {
                    TLRPC.TL_documentAttributeVideo_layer159 tL_documentAttributeVideo_layer159 = new TLRPC.TL_documentAttributeVideo_layer159();
                    tL_documentAttributeVideo_layer159.flags = documentAttribute.flags;
                    tL_documentAttributeVideo_layer159.round_message = documentAttribute.round_message;
                    tL_documentAttributeVideo_layer159.supports_streaming = documentAttribute.supports_streaming;
                    tL_documentAttributeVideo_layer159.duration = documentAttribute.duration;
                    tL_documentAttributeVideo_layer159.w = documentAttribute.w;
                    tL_documentAttributeVideo_layer159.h = documentAttribute.h;
                    tL_document_layer82.attributes.add(tL_documentAttributeVideo_layer159);
                } else {
                    tL_document_layer82.attributes.add(documentAttribute);
                }
            }
            if (tL_document_layer82.mime_type == null) {
                tL_document_layer82.mime_type = _UrlKt.FRAGMENT_ENCODE_SET;
            }
            TLRPC.PhotoSize closestPhotoSizeWithSize = FileLoader.getClosestPhotoSizeWithSize(document.thumbs, 10);
            if ((closestPhotoSizeWithSize instanceof TLRPC.TL_photoSize) || (closestPhotoSizeWithSize instanceof TLRPC.TL_photoSizeProgressive) || (closestPhotoSizeWithSize instanceof TLRPC.TL_photoStrippedSize)) {
                File pathToAttach = FileLoader.getInstance(this.currentAccount).getPathToAttach(closestPhotoSizeWithSize, true);
                if ((closestPhotoSizeWithSize instanceof TLRPC.TL_photoStrippedSize) || pathToAttach.exists()) {
                    try {
                        if (closestPhotoSizeWithSize instanceof TLRPC.TL_photoStrippedSize) {
                            tL_photoStrippedSize = new TLRPC.TL_photoStrippedSize();
                            bArr = closestPhotoSizeWithSize.bytes;
                        } else {
                            TLRPC.TL_photoCachedSize tL_photoCachedSize = new TLRPC.TL_photoCachedSize();
                            pathToAttach.length();
                            byte[] bArr3 = new byte[(int) pathToAttach.length()];
                            new RandomAccessFile(pathToAttach, "r").readFully(bArr3);
                            tL_photoStrippedSize = tL_photoCachedSize;
                            bArr = bArr3;
                        }
                        TLRPC.TL_fileLocation_layer82 tL_fileLocation_layer82 = new TLRPC.TL_fileLocation_layer82();
                        TLRPC.FileLocation fileLocation = closestPhotoSizeWithSize.location;
                        tL_fileLocation_layer82.dc_id = fileLocation.dc_id;
                        tL_fileLocation_layer82.volume_id = fileLocation.volume_id;
                        tL_fileLocation_layer82.local_id = fileLocation.local_id;
                        tL_fileLocation_layer82.secret = fileLocation.secret;
                        tL_photoStrippedSize.location = tL_fileLocation_layer82;
                        tL_photoStrippedSize.size = closestPhotoSizeWithSize.size;
                        tL_photoStrippedSize.w = closestPhotoSizeWithSize.w;
                        tL_photoStrippedSize.h = closestPhotoSizeWithSize.h;
                        tL_photoStrippedSize.type = closestPhotoSizeWithSize.type;
                        tL_photoStrippedSize.bytes = bArr;
                        tL_document_layer82.thumbs.add(tL_photoStrippedSize);
                        tL_document_layer82.flags |= 1;
                    } catch (Exception e) {
                        FileLog.e(e);
                    }
                }
            }
            if (tL_document_layer82.thumbs.isEmpty()) {
                TLRPC.TL_photoSizeEmpty tL_photoSizeEmpty = new TLRPC.TL_photoSizeEmpty();
                tL_photoSizeEmpty.type = "s";
                tL_document_layer82.thumbs.add(tL_photoSizeEmpty);
            }
        } else {
            tL_document_layer82 = document;
        }
        if (MessageObject.isGifDocument(tL_document_layer82)) {
            mediaSendQueue.postRunnable(new Runnable() { // from class: org.telegram.messenger.SendMessagesHelper$$ExternalSyntheticLambda12
                @Override // java.lang.Runnable
                public final void run() {
                    this.f$0.lambda$sendSticker$6(tL_document_layer82, videoEditedInfo, j, messageObject, messageObject2, z, i, i2, obj, sendAnimationData, storyItem, replyQuote, str2, i3, j2, j3, messageSuggestionParams, charSequence, z3);
                }
            });
            return;
        }
        if (TextUtils.isEmpty(str)) {
            map = null;
        } else {
            map = new HashMap();
            map.put("query", str);
        }
        SendMessageParams sendMessageParamsOf = SendMessageParams.of((TLRPC.TL_document) tL_document_layer82, null, null, j, messageObject, messageObject2, null, null, null, map, z, i, i2, 0, obj, sendAnimationData, z2);
        sendMessageParamsOf.replyToStoryItem = storyItem;
        sendMessageParamsOf.replyQuote = replyQuote;
        sendMessageParamsOf.quick_reply_shortcut = str2;
        sendMessageParamsOf.quick_reply_shortcut_id = i3;
        sendMessageParamsOf.payStars = j2;
        sendMessageParamsOf.monoForumPeer = j3;
        sendMessageParamsOf.suggestionParams = messageSuggestionParams;
        sendMessageParamsOf.invert_media = z3;
        sendMessage(sendMessageParamsOf);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$sendSticker$6(final TLRPC.Document document, final VideoEditedInfo videoEditedInfo, final long j, final MessageObject messageObject, final MessageObject messageObject2, final boolean z, final int i, final int i2, final Object obj, final MessageObject.SendAnimationData sendAnimationData, final TL_stories.StoryItem storyItem, final ChatActivity.ReplyQuote replyQuote, final String str, final int i3, final long j2, final long j3, final MessageSuggestionParams messageSuggestionParams, final CharSequence charSequence, final boolean z2) {
        String str2;
        final Bitmap[] bitmapArr = new Bitmap[1];
        String key = ImageLocation.getForDocument(document).getKey(null, null, false);
        if ("video/mp4".equals(document.mime_type)) {
            str2 = ".mp4";
        } else if ("video/x-matroska".equals(document.mime_type)) {
            str2 = ".mkv";
        } else {
            str2 = _UrlKt.FRAGMENT_ENCODE_SET;
        }
        File file = new File(FileLoader.getDirectory(3), key + str2);
        if (!file.exists()) {
            file = new File(FileLoader.getDirectory(2), key + str2);
        }
        ensureMediaThumbExists(getAccountInstance(), false, document, file.getAbsolutePath(), null, 0L);
        final String[] strArr = {getKeyForPhotoSize(getAccountInstance(), FileLoader.getClosestPhotoSizeWithSize(document.thumbs, 320), bitmapArr, true, true)};
        AndroidUtilities.runOnUIThread(new Runnable() { // from class: org.telegram.messenger.SendMessagesHelper$$ExternalSyntheticLambda28
            @Override // java.lang.Runnable
            public final void run() {
                this.f$0.lambda$sendSticker$5(bitmapArr, strArr, document, videoEditedInfo, j, messageObject, messageObject2, z, i, i2, obj, sendAnimationData, storyItem, replyQuote, str, i3, j2, j3, messageSuggestionParams, charSequence, z2);
            }
        });
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$sendSticker$5(Bitmap[] bitmapArr, String[] strArr, TLRPC.Document document, VideoEditedInfo videoEditedInfo, long j, MessageObject messageObject, MessageObject messageObject2, boolean z, int i, int i2, Object obj, MessageObject.SendAnimationData sendAnimationData, TL_stories.StoryItem storyItem, ChatActivity.ReplyQuote replyQuote, String str, int i3, long j2, long j3, MessageSuggestionParams messageSuggestionParams, CharSequence charSequence, boolean z2) {
        if (bitmapArr[0] != null && strArr[0] != null) {
            ImageLoader.getInstance().putImageToCache(new BitmapDrawable(bitmapArr[0]), strArr[0], false);
        }
        SendMessageParams sendMessageParamsOf = SendMessageParams.of((TLRPC.TL_document) document, videoEditedInfo, null, j, messageObject, messageObject2, null, null, null, null, z, i, i2, 0, obj, sendAnimationData, false);
        sendMessageParamsOf.replyToStoryItem = storyItem;
        sendMessageParamsOf.replyQuote = replyQuote;
        sendMessageParamsOf.quick_reply_shortcut = str;
        sendMessageParamsOf.quick_reply_shortcut_id = i3;
        sendMessageParamsOf.payStars = j2;
        sendMessageParamsOf.monoForumPeer = j3;
        sendMessageParamsOf.suggestionParams = messageSuggestionParams;
        sendMessageParamsOf.caption = charSequence != null ? charSequence.toString() : null;
        sendMessageParamsOf.invert_media = z2;
        sendMessage(sendMessageParamsOf);
    }

    public int sendMessage(ArrayList<MessageObject> arrayList, long j, boolean z, boolean z2, boolean z3, int i, long j2) {
        return sendMessage(arrayList, j, z, z2, z3, i, null, -1, j2);
    }

    public int sendMessage(ArrayList<MessageObject> arrayList, long j, boolean z, boolean z2, boolean z3, int i, MessageObject messageObject, int i2, long j2) {
        return sendMessage(arrayList, j, z, z2, z3, i, 0, messageObject, i2, j2, 0L, null);
    }

    /* JADX WARN: Code duplicated, block: B:193:0x0436  */
    /* JADX WARN: Code duplicated, block: B:195:0x0447  */
    /* JADX WARN: Code duplicated, block: B:198:0x0457  */
    /* JADX WARN: Code duplicated, block: B:201:0x0467  */
    /* JADX WARN: Code duplicated, block: B:204:0x0479  */
    /* JADX WARN: Code duplicated, block: B:214:0x04c0  */
    /* JADX WARN: Code duplicated, block: B:216:0x04c4  */
    /* JADX WARN: Code duplicated, block: B:218:0x04e2  */
    /* JADX WARN: Code duplicated, block: B:219:0x04f4  */
    /* JADX WARN: Code duplicated, block: B:225:0x051e  */
    /* JADX WARN: Code duplicated, block: B:241:0x0567  */
    /* JADX WARN: Code duplicated, block: B:245:0x0587  */
    /* JADX WARN: Code duplicated, block: B:373:0x0882  */
    /* JADX WARN: Code duplicated, block: B:414:0x09b3  */
    /* JADX WARN: Code duplicated, block: B:422:0x09f5  */
    /* JADX WARN: Code duplicated, block: B:425:0x0a02  */
    /* JADX WARN: Code duplicated, block: B:426:0x0a05  */
    /* JADX WARN: Code duplicated, block: B:429:0x0a1c  */
    /* JADX WARN: Code duplicated, block: B:430:0x0a1e  */
    /* JADX WARN: Code duplicated, block: B:433:0x0a40  */
    /* JADX WARN: Code duplicated, block: B:439:0x0a6e  */
    /* JADX WARN: Code duplicated, block: B:442:0x0a73  */
    /* JADX WARN: Code duplicated, block: B:444:0x0a81  */
    /* JADX WARN: Code duplicated, block: B:446:0x0a8f  */
    /* JADX WARN: Code duplicated, block: B:447:0x0a99  */
    /* JADX WARN: Code duplicated, block: B:450:0x0aa5  */
    /* JADX WARN: Code duplicated, block: B:452:0x0acb  */
    /* JADX WARN: Code duplicated, block: B:453:0x0ad0  */
    /* JADX WARN: Code duplicated, block: B:459:0x0afc  */
    /* JADX WARN: Code duplicated, block: B:462:0x0b03  */
    /* JADX WARN: Code duplicated, block: B:464:0x0b0f  */
    /* JADX WARN: Code duplicated, block: B:466:0x0b24  */
    /* JADX WARN: Code duplicated, block: B:468:0x0b2c  */
    /* JADX WARN: Code duplicated, block: B:471:0x0b49  */
    /* JADX WARN: Code duplicated, block: B:473:0x0b4d  */
    /* JADX WARN: Code duplicated, block: B:476:0x0b87  */
    /* JADX WARN: Code duplicated, block: B:479:0x0b92  */
    /* JADX WARN: Code duplicated, block: B:59:0x018a  */
    /* JADX WARN: Code duplicated, block: B:60:0x0197  */
    /* JADX WARN: Instruction removed from duplicated block: B:433:0x0a40, please report this as an issue */
    /* JADX WARN: Multi-variable type inference failed */
    /* JADX WARN: Type inference failed for: r10v26 */
    /* JADX WARN: Type inference failed for: r10v27, types: [int] */
    /* JADX WARN: Type inference failed for: r10v31 */
    /* JADX WARN: Type inference failed for: r14v10, types: [boolean, int] */
    /* JADX WARN: Type inference failed for: r14v27 */
    /* JADX WARN: Type inference failed for: r14v28 */
    /* JADX WARN: Type inference failed for: r14v31 */
    /* JADX WARN: Type inference failed for: r14v32 */
    /* JADX WARN: Type inference failed for: r14v38 */
    /* JADX WARN: Type inference failed for: r14v39 */
    /* JADX WARN: Type inference failed for: r14v48 */
    /* JADX WARN: Type inference failed for: r14v49 */
    /* JADX WARN: Type inference failed for: r14v54 */
    /* JADX WARN: Type inference failed for: r14v55 */
    /* JADX WARN: Type inference failed for: r14v56 */
    /* JADX WARN: Type inference failed for: r14v57 */
    /* JADX WARN: Type inference failed for: r14v58 */
    /* JADX WARN: Type inference failed for: r1v103 */
    /* JADX WARN: Type inference failed for: r1v104, types: [int] */
    /* JADX WARN: Type inference failed for: r1v109 */
    /* JADX WARN: Type inference failed for: r2v105 */
    /* JADX WARN: Type inference failed for: r2v106 */
    /* JADX WARN: Type inference failed for: r2v107 */
    /* JADX WARN: Type inference failed for: r2v37 */
    /* JADX WARN: Type inference failed for: r2v39 */
    /* JADX WARN: Type inference failed for: r2v41, types: [java.util.ArrayList] */
    /* JADX WARN: Type inference failed for: r2v46 */
    /* JADX WARN: Type inference failed for: r36v1 */
    /* JADX WARN: Type inference failed for: r36v10 */
    /* JADX WARN: Type inference failed for: r36v12 */
    /* JADX WARN: Type inference failed for: r36v13 */
    /* JADX WARN: Type inference failed for: r36v14 */
    /* JADX WARN: Type inference failed for: r36v15 */
    /* JADX WARN: Type inference failed for: r36v2, types: [int] */
    /* JADX WARN: Type inference failed for: r36v3 */
    /* JADX WARN: Type inference failed for: r36v4 */
    /* JADX WARN: Type inference failed for: r36v5 */
    /* JADX WARN: Type inference failed for: r36v6 */
    /* JADX WARN: Type inference failed for: r36v7 */
    /* JADX WARN: Type inference failed for: r36v8 */
    /* JADX WARN: Type inference failed for: r36v9 */
    /* JADX WARN: Type inference failed for: r47v0 */
    /* JADX WARN: Type inference failed for: r49v5 */
    /* JADX WARN: Type inference failed for: r51v7 */
    public int sendMessage(final ArrayList<MessageObject> arrayList, final long j, final boolean z, final boolean z2, boolean z3, int i, final int i2, final MessageObject messageObject, final int i3, long j2, final long j3, final MessageSuggestionParams messageSuggestionParams) {
        final int i4;
        TLRPC.UserFull userFull;
        TLRPC.Chat chat;
        int i5;
        boolean z4;
        boolean z5;
        String adminRank;
        boolean zCanSendRoundVideo;
        boolean zCanSendVoice;
        boolean zCanSendMusic;
        boolean z6;
        boolean z7;
        boolean z8;
        boolean z9;
        boolean z10;
        boolean z11;
        String str;
        boolean z12;
        boolean z13;
        long j4;
        ArrayList arrayList2;
        final LongSparseArray longSparseArray;
        int i6;
        ArrayList arrayList3;
        TLRPC.Peer peer;
        ArrayList<MessageObject> arrayList4;
        TLRPC.InputPeer inputPeer;
        final SendMessagesHelper sendMessagesHelper;
        LongSparseArray longSparseArray2;
        int i7;
        boolean z14;
        TLRPC.WebPage webPage;
        int i8;
        LongSparseArray longSparseArray3;
        LongSparseArray longSparseArray4;
        ArrayList arrayList5;
        TLRPC.Chat chat2;
        String str2;
        LongSparseArray longSparseArray5;
        long j5;
        TLRPC.TL_message tL_message;
        ArrayList<MessageObject> arrayList6;
        MessageObject messageObject2;
        int i9;
        int i10;
        final TLRPC.TL_messages_forwardMessages tL_messages_forwardMessages;
        boolean z15;
        int i11;
        TLRPC.InputPeer inputPeer2;
        ArrayList arrayList7;
        boolean z16;
        final ArrayList arrayList8;
        final boolean z17;
        Runnable runnable;
        TLRPC.Chat chat3;
        TLRPC.TL_inputPeerChannel tL_inputPeerChannel;
        int i12;
        TLRPC.Message message;
        TLRPC.MessageReplyHeader messageReplyHeader;
        ?? r2;
        ?? r47;
        TLRPC.TL_keyboardButtonRow tL_keyboardButtonRow;
        boolean z18;
        TLRPC.MessageFwdHeader messageFwdHeader;
        TLRPC.Message message2;
        TLRPC.Peer peer2;
        TLRPC.Peer peer3;
        TLRPC.User user;
        TLRPC.MessageFwdHeader messageFwdHeader2;
        TLRPC.Peer peer4;
        TLRPC.TL_messageFwdHeader tL_messageFwdHeader;
        TLRPC.MessageFwdHeader messageFwdHeader3;
        ?? r14;
        ?? r36;
        ?? r37;
        char c;
        LongSparseArray longSparseArray6;
        if (arrayList == null || arrayList.isEmpty()) {
            return 0;
        }
        final boolean z19 = getAyuGhostController().isSendWithoutSound() ? !z3 : z3;
        if (getAyuGhostController().isUseScheduledMessages() && !DialogObject.isEncryptedDialog(j) && i == 0) {
            int currentTime = ConnectionsManager.getInstance(this.currentAccount).getCurrentTime() + 12;
            AyuState.setAutomaticallyScheduled(true, 1);
            i4 = currentTime;
        } else {
            i4 = i;
        }
        if (AyuForward.isFullAyuForwardsNeeded(this.currentAccount, arrayList)) {
            final boolean z20 = z19;
            AyuForward.getQueue().postRunnable(new Runnable() { // from class: org.telegram.messenger.SendMessagesHelper$$ExternalSyntheticLambda4
                @Override // java.lang.Runnable
                public final void run() {
                    this.f$0.lambda$sendMessage$7(arrayList, j, z2, z20, messageObject);
                }
            });
            AndroidUtilities.runOnUIThread(new Runnable() { // from class: org.telegram.messenger.SendMessagesHelper$$ExternalSyntheticLambda5
                @Override // java.lang.Runnable
                public final void run() {
                    this.f$0.lambda$sendMessage$8();
                }
            }, 500L);
            return 0;
        }
        if (AyuForward.isAyuForwardNeeded(arrayList)) {
            AyuForward.getQueue().postRunnable(new Runnable() { // from class: org.telegram.messenger.SendMessagesHelper$$ExternalSyntheticLambda6
                @Override // java.lang.Runnable
                public final void run() {
                    this.f$0.lambda$sendMessage$9(arrayList, j, z, z2, z19, i4, messageObject);
                }
            });
            AndroidUtilities.runOnUIThread(new Runnable() { // from class: org.telegram.messenger.SendMessagesHelper$$ExternalSyntheticLambda7
                @Override // java.lang.Runnable
                public final void run() {
                    this.f$0.lambda$sendMessage$10();
                }
            }, 500L);
            return 0;
        }
        final long j6 = j;
        long clientUserId = getUserConfig().getClientUserId();
        if (!DialogObject.isEncryptedDialog(j6)) {
            TLRPC.Peer peer5 = getMessagesController().getPeer(j6);
            long sendPaidMessagesStars = getMessagesController().getSendPaidMessagesStars(j6);
            if (sendPaidMessagesStars <= 0) {
                sendPaidMessagesStars = DialogObject.getMessagesStarsPrice(getMessagesController().isUserContactBlocked(j6));
            }
            if (sendPaidMessagesStars != j2) {
                AlertsCreator.ensurePaidMessageConfirmation(this.currentAccount, j6, Math.max(1, arrayList.size()), new Utilities.Callback() { // from class: org.telegram.messenger.SendMessagesHelper$$ExternalSyntheticLambda8
                    @Override // org.telegram.messenger.Utilities.Callback
                    public final void run(Object obj) {
                        this.f$0.lambda$sendMessage$11(arrayList, j6, z, z2, z19, i4, i2, messageObject, i3, j3, messageSuggestionParams, (Long) obj);
                    }
                });
                return 0;
            }
            boolean z21 = z19;
            boolean z22 = false;
            boolean z23 = true;
            if (DialogObject.isUserDialog(j6)) {
                if (getMessagesController().getUser(Long.valueOf(j6)) == null) {
                    return 0;
                }
                TLRPC.UserFull userFull2 = getMessagesController().getUserFull(j6);
                boolean z24 = userFull2 != null ? !userFull2.voice_messages_forbidden : true;
                z12 = true;
                z13 = true;
                z9 = true;
                z8 = true;
                z7 = true;
                z6 = true;
                zCanSendMusic = true;
                zCanSendVoice = z24;
                zCanSendRoundVideo = z24;
                i5 = i4;
                z11 = false;
                z10 = false;
                j4 = 0;
                chat = null;
                str = null;
            } else {
                chat = getMessagesController().getChat(Long.valueOf(-j6));
                if (ChatObject.isChannel(chat)) {
                    z4 = chat.signatures;
                    boolean z25 = chat.megagroup;
                    boolean z26 = !z25;
                    if (z25 || !chat.has_link) {
                        z5 = z26;
                        i5 = i4;
                    } else {
                        z5 = z26;
                        i5 = i4;
                        TLRPC.ChatFull chatFull = getMessagesController().getChatFull(chat.id);
                        long j7 = chatFull != null ? chatFull.linked_chat_id : 0L;
                        if (chat != null) {
                            adminRank = getMessagesController().getAdminRank(chat.id, clientUserId);
                        } else {
                            adminRank = null;
                        }
                        boolean zCanSendStickers = ChatObject.canSendStickers(chat);
                        boolean zCanSendPhoto = ChatObject.canSendPhoto(chat);
                        boolean zCanSendVideo = ChatObject.canSendVideo(chat);
                        boolean zCanSendDocument = ChatObject.canSendDocument(chat);
                        boolean zCanSendEmbed = ChatObject.canSendEmbed(chat);
                        boolean zCanSendPolls = ChatObject.canSendPolls(chat);
                        zCanSendRoundVideo = ChatObject.canSendRoundVideo(chat);
                        zCanSendVoice = ChatObject.canSendVoice(chat);
                        zCanSendMusic = ChatObject.canSendMusic(chat);
                        z6 = zCanSendPolls;
                        z7 = zCanSendEmbed;
                        z8 = zCanSendDocument;
                        z9 = zCanSendVideo;
                        z10 = z5;
                        z11 = z4;
                        str = adminRank;
                        z12 = zCanSendStickers;
                        z13 = zCanSendPhoto;
                        j4 = j7;
                    }
                } else {
                    i5 = i4;
                    z4 = false;
                    z5 = false;
                }
                if (chat != null) {
                    adminRank = getMessagesController().getAdminRank(chat.id, clientUserId);
                } else {
                    adminRank = null;
                }
                boolean zCanSendStickers2 = ChatObject.canSendStickers(chat);
                boolean zCanSendPhoto2 = ChatObject.canSendPhoto(chat);
                boolean zCanSendVideo2 = ChatObject.canSendVideo(chat);
                boolean zCanSendDocument2 = ChatObject.canSendDocument(chat);
                boolean zCanSendEmbed2 = ChatObject.canSendEmbed(chat);
                boolean zCanSendPolls2 = ChatObject.canSendPolls(chat);
                zCanSendRoundVideo = ChatObject.canSendRoundVideo(chat);
                zCanSendVoice = ChatObject.canSendVoice(chat);
                zCanSendMusic = ChatObject.canSendMusic(chat);
                z6 = zCanSendPolls2;
                z7 = zCanSendEmbed2;
                z8 = zCanSendDocument2;
                z9 = zCanSendVideo2;
                z10 = z5;
                z11 = z4;
                str = adminRank;
                z12 = zCanSendStickers2;
                z13 = zCanSendPhoto2;
                j4 = j7;
            }
            LongSparseArray longSparseArray7 = new LongSparseArray();
            ArrayList<MessageObject> arrayList9 = new ArrayList<>();
            ArrayList arrayList10 = new ArrayList();
            ArrayList arrayList11 = new ArrayList();
            ArrayList arrayList12 = new ArrayList();
            LongSparseArray longSparseArray8 = new LongSparseArray();
            TLRPC.InputPeer inputPeer3 = getMessagesController().getInputPeer(j6);
            boolean z27 = j6 == clientUserId;
            int i13 = 0;
            ArrayList arrayList13 = arrayList10;
            ArrayList arrayList14 = arrayList11;
            ArrayList arrayList15 = arrayList12;
            LongSparseArray longSparseArray9 = longSparseArray8;
            TLRPC.InputPeer inputPeer4 = inputPeer3;
            ?? r38 = 0;
            LongSparseArray longSparseArray10 = longSparseArray7;
            while (i13 < arrayList.size()) {
                final MessageObject messageObject3 = arrayList.get(i13);
                if (messageObject3.getId() <= 0 || messageObject3.needDrawBluredPreview()) {
                    arrayList2 = arrayList15;
                    chat = chat;
                    str = str;
                    j4 = j4;
                    clientUserId = clientUserId;
                    arrayList14 = arrayList14;
                    longSparseArray = longSparseArray9;
                    i6 = i13;
                    z27 = z27;
                    arrayList3 = arrayList13;
                    peer = peer5;
                    arrayList4 = arrayList9;
                    inputPeer = inputPeer4;
                    sendMessagesHelper = this;
                    longSparseArray2 = longSparseArray10;
                    i7 = i5;
                    z14 = z23;
                    longSparseArray3 = longSparseArray2;
                    if (messageObject3.type != 0 || TextUtils.isEmpty(messageObject3.messageText)) {
                        longSparseArray3 = longSparseArray2;
                        j6 = j;
                        i5 = i7;
                        z21 = z21;
                        i8 = i6;
                        longSparseArray4 = longSparseArray3;
                    } else {
                        TLRPC.MessageMedia messageMedia = messageObject3.messageOwner.media;
                        if (messageMedia != null) {
                            longSparseArray3 = longSparseArray2;
                            webPage = messageMedia.webpage;
                        } else {
                            longSparseArray3 = longSparseArray2;
                            webPage = null;
                        }
                        boolean z28 = z21;
                        i8 = i6;
                        SendMessageParams sendMessageParamsOf = SendMessageParams.of(messageObject3.messageText.toString(), j, null, messageObject, webPage, webPage != null ? z14 : false, messageObject3.messageOwner.entities, null, null, z28, i7, i2, null, false);
                        j6 = j;
                        z21 = z28;
                        i5 = i7;
                        sendMessageParamsOf.suggestionParams = messageSuggestionParams;
                        sendMessageParamsOf.monoForumPeer = j3;
                        sendMessageParamsOf.quick_reply_shortcut = messageObject3.getQuickReplyName();
                        sendMessageParamsOf.quick_reply_shortcut_id = messageObject3.getQuickReplyId();
                        sendMessagesHelper.sendMessage(sendMessageParamsOf);
                        longSparseArray4 = longSparseArray2;
                    }
                    arrayList14 = arrayList14;
                    arrayList15 = arrayList2;
                    longSparseArray9 = longSparseArray;
                    arrayList13 = arrayList3;
                } else {
                    boolean z29 = (messageObject3.isSticker() || messageObject3.isAnimatedSticker() || messageObject3.isGif() || messageObject3.isGame()) ? z23 : z22;
                    i8 = i13;
                    if (!z12 && z29) {
                        if (r38 == 0) {
                            r14 = ChatObject.isActionBannedByDefault(chat, 8) ? 4 : z23;
                            r37 = r14;
                            r36 = r37;
                            z14 = z23;
                            peer = peer5;
                            arrayList4 = arrayList9;
                            inputPeer = inputPeer4;
                            longSparseArray4 = longSparseArray10;
                            r38 = r36;
                        }
                        arrayList2 = arrayList15;
                        chat = chat;
                        str = str;
                        j6 = j6;
                        j4 = j4;
                        clientUserId = clientUserId;
                        longSparseArray = longSparseArray9;
                        i8 = i8;
                        arrayList3 = arrayList13;
                        z14 = z23;
                        peer = peer5;
                        arrayList4 = arrayList9;
                        inputPeer = inputPeer4;
                        longSparseArray6 = longSparseArray10;
                        longSparseArray4 = longSparseArray6;
                        arrayList14 = arrayList14;
                        arrayList15 = arrayList2;
                        longSparseArray9 = longSparseArray;
                        arrayList13 = arrayList3;
                    } else if (!z13 && (messageObject3.messageOwner.media instanceof TLRPC.TL_messageMediaPhoto) && !messageObject3.isVideo() && !z29) {
                        if (r38 == 0) {
                            r14 = ChatObject.isActionBannedByDefault(chat, 16) ? 10 : 12;
                            r37 = r14;
                            r36 = r37;
                            z14 = z23;
                            peer = peer5;
                            arrayList4 = arrayList9;
                            inputPeer = inputPeer4;
                            longSparseArray4 = longSparseArray10;
                            r38 = r36;
                        }
                        arrayList2 = arrayList15;
                        chat = chat;
                        str = str;
                        j6 = j6;
                        j4 = j4;
                        clientUserId = clientUserId;
                        longSparseArray = longSparseArray9;
                        i8 = i8;
                        arrayList3 = arrayList13;
                        z14 = z23;
                        peer = peer5;
                        arrayList4 = arrayList9;
                        inputPeer = inputPeer4;
                        longSparseArray6 = longSparseArray10;
                        longSparseArray4 = longSparseArray6;
                        arrayList14 = arrayList14;
                        arrayList15 = arrayList2;
                        longSparseArray9 = longSparseArray;
                        arrayList13 = arrayList3;
                    } else if (!zCanSendMusic && messageObject3.isMusic()) {
                        if (r38 == 0) {
                            r14 = ChatObject.isActionBannedByDefault(chat, 18) ? 19 : 20;
                            r37 = r14;
                            r36 = r37;
                            z14 = z23;
                            peer = peer5;
                            arrayList4 = arrayList9;
                            inputPeer = inputPeer4;
                            longSparseArray4 = longSparseArray10;
                            r38 = r36;
                        }
                        arrayList2 = arrayList15;
                        chat = chat;
                        str = str;
                        j6 = j6;
                        j4 = j4;
                        clientUserId = clientUserId;
                        longSparseArray = longSparseArray9;
                        i8 = i8;
                        arrayList3 = arrayList13;
                        z14 = z23;
                        peer = peer5;
                        arrayList4 = arrayList9;
                        inputPeer = inputPeer4;
                        longSparseArray6 = longSparseArray10;
                        longSparseArray4 = longSparseArray6;
                        arrayList14 = arrayList14;
                        arrayList15 = arrayList2;
                        longSparseArray9 = longSparseArray;
                        arrayList13 = arrayList3;
                    } else if (!z9 && (messageObject3.messageOwner.media instanceof TLRPC.TL_messageMediaPhoto) && messageObject3.isVideo() && !z29) {
                        if (r38 == 0) {
                            c = ChatObject.isActionBannedByDefault(chat, 17) ? '\t' : (char) 11;
                            r37 = c;
                            r36 = r37;
                            z14 = z23;
                            peer = peer5;
                            arrayList4 = arrayList9;
                            inputPeer = inputPeer4;
                            longSparseArray4 = longSparseArray10;
                            r38 = r36;
                        }
                        arrayList2 = arrayList15;
                        chat = chat;
                        str = str;
                        j6 = j6;
                        j4 = j4;
                        clientUserId = clientUserId;
                        longSparseArray = longSparseArray9;
                        i8 = i8;
                        arrayList3 = arrayList13;
                        z14 = z23;
                        peer = peer5;
                        arrayList4 = arrayList9;
                        inputPeer = inputPeer4;
                        longSparseArray6 = longSparseArray10;
                        longSparseArray4 = longSparseArray6;
                        arrayList14 = arrayList14;
                        arrayList15 = arrayList2;
                        longSparseArray9 = longSparseArray;
                        arrayList13 = arrayList3;
                    } else if (!z6 && (messageObject3.messageOwner.media instanceof TLRPC.TL_messageMediaPoll)) {
                        if (r38 == 0) {
                            c = ChatObject.isActionBannedByDefault(chat, 10) ? (char) 6 : (char) 3;
                            r37 = c;
                            r36 = r37;
                            z14 = z23;
                            peer = peer5;
                            arrayList4 = arrayList9;
                            inputPeer = inputPeer4;
                            longSparseArray4 = longSparseArray10;
                            r38 = r36;
                        }
                        arrayList2 = arrayList15;
                        chat = chat;
                        str = str;
                        j6 = j6;
                        j4 = j4;
                        clientUserId = clientUserId;
                        longSparseArray = longSparseArray9;
                        i8 = i8;
                        arrayList3 = arrayList13;
                        z14 = z23;
                        peer = peer5;
                        arrayList4 = arrayList9;
                        inputPeer = inputPeer4;
                        longSparseArray6 = longSparseArray10;
                        longSparseArray4 = longSparseArray6;
                        arrayList14 = arrayList14;
                        arrayList15 = arrayList2;
                        longSparseArray9 = longSparseArray;
                        arrayList13 = arrayList3;
                    } else if (z6 || !(messageObject3.messageOwner.media instanceof TLRPC.TL_messageMediaToDo)) {
                        if (zCanSendVoice || !MessageObject.isVoiceMessage(messageObject3.messageOwner)) {
                            if (zCanSendRoundVideo || !MessageObject.isRoundVideoMessage(messageObject3.messageOwner)) {
                                if (z8 || !(messageObject3.messageOwner.media instanceof TLRPC.TL_messageMediaDocument) || z29) {
                                    TLRPC.TL_message tL_message2 = new TLRPC.TL_message();
                                    if (z) {
                                        arrayList5 = arrayList15;
                                        chat2 = chat;
                                        str2 = str;
                                        longSparseArray5 = longSparseArray9;
                                    } else {
                                        if (messageObject3.getDialogId() == clientUserId && messageObject3.isFromUser()) {
                                            arrayList5 = arrayList15;
                                            chat2 = chat;
                                            if (messageObject3.messageOwner.from_id.user_id == clientUserId) {
                                                z18 = z23;
                                            }
                                            if (messageObject3.isForwarded()) {
                                                tL_messageFwdHeader = new TLRPC.TL_messageFwdHeader();
                                                tL_message2.fwd_from = tL_messageFwdHeader;
                                                messageFwdHeader3 = messageObject3.messageOwner.fwd_from;
                                                if ((messageFwdHeader3.flags & 1) != 0) {
                                                    tL_messageFwdHeader.flags |= 1;
                                                    tL_messageFwdHeader.from_id = messageFwdHeader3.from_id;
                                                }
                                                if ((messageFwdHeader3.flags & 32) != 0) {
                                                    tL_messageFwdHeader.flags |= 32;
                                                    tL_messageFwdHeader.from_name = messageFwdHeader3.from_name;
                                                }
                                                if ((messageFwdHeader3.flags & 4) != 0) {
                                                    tL_messageFwdHeader.flags |= 4;
                                                    tL_messageFwdHeader.channel_post = messageFwdHeader3.channel_post;
                                                }
                                                if ((messageFwdHeader3.flags & 8) != 0) {
                                                    tL_messageFwdHeader.flags |= 8;
                                                    tL_messageFwdHeader.post_author = messageFwdHeader3.post_author;
                                                }
                                                if ((j6 != clientUserId || z10) && (messageFwdHeader3.flags & 16) != 0 && !UserObject.isReplyUser(messageObject3.getDialogId())) {
                                                    TLRPC.MessageFwdHeader messageFwdHeader4 = tL_message2.fwd_from;
                                                    messageFwdHeader4.flags |= 16;
                                                    TLRPC.MessageFwdHeader messageFwdHeader5 = messageObject3.messageOwner.fwd_from;
                                                    messageFwdHeader4.saved_from_peer = messageFwdHeader5.saved_from_peer;
                                                    messageFwdHeader4.saved_from_msg_id = messageFwdHeader5.saved_from_msg_id;
                                                }
                                                tL_message2.fwd_from.date = messageObject3.messageOwner.fwd_from.date;
                                                tL_message2.flags = 4;
                                            } else {
                                                if (!z18) {
                                                    long fromChatId = messageObject3.getFromChatId();
                                                    TLRPC.TL_messageFwdHeader tL_messageFwdHeader2 = new TLRPC.TL_messageFwdHeader();
                                                    tL_message2.fwd_from = tL_messageFwdHeader2;
                                                    tL_messageFwdHeader2.channel_post = messageObject3.getId();
                                                    tL_message2.fwd_from.flags |= 4;
                                                    if (messageObject3.isFromUser()) {
                                                        TLRPC.MessageFwdHeader messageFwdHeader6 = tL_message2.fwd_from;
                                                        messageFwdHeader6.from_id = messageObject3.messageOwner.from_id;
                                                        messageFwdHeader6.flags |= 1;
                                                        str2 = str;
                                                        longSparseArray5 = longSparseArray9;
                                                    } else {
                                                        tL_message2.fwd_from.from_id = new TLRPC.TL_peerChannel();
                                                        messageFwdHeader = tL_message2.fwd_from;
                                                        TLRPC.Peer peer6 = messageFwdHeader.from_id;
                                                        message2 = messageObject3.messageOwner;
                                                        longSparseArray5 = longSparseArray9;
                                                        peer2 = message2.peer_id;
                                                        str2 = str;
                                                        peer6.channel_id = peer2.channel_id;
                                                        messageFwdHeader.flags |= 1;
                                                        if (message2.post && fromChatId > 0) {
                                                            peer3 = message2.from_id;
                                                            if (peer3 != null) {
                                                                peer2 = peer3;
                                                            }
                                                            messageFwdHeader.from_id = peer2;
                                                        }
                                                    }
                                                    if (messageObject3.messageOwner.post_author == null && !messageObject3.isOutOwner() && fromChatId > 0 && messageObject3.messageOwner.post && (user = getMessagesController().getUser(Long.valueOf(fromChatId))) != null) {
                                                        tL_message2.fwd_from.post_author = ContactsController.formatName(user.first_name, user.last_name);
                                                        tL_message2.fwd_from.flags |= 8;
                                                    }
                                                    tL_message2.date = messageObject3.messageOwner.date;
                                                    tL_message2.flags = 4;
                                                }
                                                if (j6 == clientUserId && (messageFwdHeader2 = tL_message2.fwd_from) != null) {
                                                    messageFwdHeader2.flags |= 16;
                                                    messageFwdHeader2.saved_from_msg_id = messageObject3.getId();
                                                    TLRPC.MessageFwdHeader messageFwdHeader7 = tL_message2.fwd_from;
                                                    peer4 = messageObject3.messageOwner.peer_id;
                                                    messageFwdHeader7.saved_from_peer = peer4;
                                                    if (peer4.user_id == clientUserId) {
                                                        peer4.user_id = messageObject3.getDialogId();
                                                    }
                                                }
                                            }
                                            str2 = str;
                                            longSparseArray5 = longSparseArray9;
                                            if (j6 == clientUserId) {
                                                messageFwdHeader2.flags |= 16;
                                                messageFwdHeader2.saved_from_msg_id = messageObject3.getId();
                                                TLRPC.MessageFwdHeader messageFwdHeader8 = tL_message2.fwd_from;
                                                peer4 = messageObject3.messageOwner.peer_id;
                                                messageFwdHeader8.saved_from_peer = peer4;
                                                if (peer4.user_id == clientUserId) {
                                                    peer4.user_id = messageObject3.getDialogId();
                                                }
                                            }
                                        } else {
                                            arrayList5 = arrayList15;
                                            chat2 = chat;
                                        }
                                        z18 = z22;
                                        if (messageObject3.isForwarded()) {
                                            tL_messageFwdHeader = new TLRPC.TL_messageFwdHeader();
                                            tL_message2.fwd_from = tL_messageFwdHeader;
                                            messageFwdHeader3 = messageObject3.messageOwner.fwd_from;
                                            if ((messageFwdHeader3.flags & 1) != 0) {
                                                tL_messageFwdHeader.flags |= 1;
                                                tL_messageFwdHeader.from_id = messageFwdHeader3.from_id;
                                            }
                                            if ((messageFwdHeader3.flags & 32) != 0) {
                                                tL_messageFwdHeader.flags |= 32;
                                                tL_messageFwdHeader.from_name = messageFwdHeader3.from_name;
                                            }
                                            if ((messageFwdHeader3.flags & 4) != 0) {
                                                tL_messageFwdHeader.flags |= 4;
                                                tL_messageFwdHeader.channel_post = messageFwdHeader3.channel_post;
                                            }
                                            if ((messageFwdHeader3.flags & 8) != 0) {
                                                tL_messageFwdHeader.flags |= 8;
                                                tL_messageFwdHeader.post_author = messageFwdHeader3.post_author;
                                            }
                                            if (j6 != clientUserId) {
                                                TLRPC.MessageFwdHeader messageFwdHeader9 = tL_message2.fwd_from;
                                                messageFwdHeader9.flags |= 16;
                                                TLRPC.MessageFwdHeader messageFwdHeader10 = messageObject3.messageOwner.fwd_from;
                                                messageFwdHeader9.saved_from_peer = messageFwdHeader10.saved_from_peer;
                                                messageFwdHeader9.saved_from_msg_id = messageFwdHeader10.saved_from_msg_id;
                                            } else {
                                                TLRPC.MessageFwdHeader messageFwdHeader11 = tL_message2.fwd_from;
                                                messageFwdHeader11.flags |= 16;
                                                TLRPC.MessageFwdHeader messageFwdHeader12 = messageObject3.messageOwner.fwd_from;
                                                messageFwdHeader11.saved_from_peer = messageFwdHeader12.saved_from_peer;
                                                messageFwdHeader11.saved_from_msg_id = messageFwdHeader12.saved_from_msg_id;
                                            }
                                            tL_message2.fwd_from.date = messageObject3.messageOwner.fwd_from.date;
                                            tL_message2.flags = 4;
                                        } else {
                                            if (!z18) {
                                                long fromChatId2 = messageObject3.getFromChatId();
                                                TLRPC.TL_messageFwdHeader tL_messageFwdHeader3 = new TLRPC.TL_messageFwdHeader();
                                                tL_message2.fwd_from = tL_messageFwdHeader3;
                                                tL_messageFwdHeader3.channel_post = messageObject3.getId();
                                                tL_message2.fwd_from.flags |= 4;
                                                if (messageObject3.isFromUser()) {
                                                    TLRPC.MessageFwdHeader messageFwdHeader13 = tL_message2.fwd_from;
                                                    messageFwdHeader13.from_id = messageObject3.messageOwner.from_id;
                                                    messageFwdHeader13.flags |= 1;
                                                    str2 = str;
                                                    longSparseArray5 = longSparseArray9;
                                                } else {
                                                    tL_message2.fwd_from.from_id = new TLRPC.TL_peerChannel();
                                                    messageFwdHeader = tL_message2.fwd_from;
                                                    TLRPC.Peer peer7 = messageFwdHeader.from_id;
                                                    message2 = messageObject3.messageOwner;
                                                    longSparseArray5 = longSparseArray9;
                                                    peer2 = message2.peer_id;
                                                    str2 = str;
                                                    peer7.channel_id = peer2.channel_id;
                                                    messageFwdHeader.flags |= 1;
                                                    if (message2.post) {
                                                        peer3 = message2.from_id;
                                                        if (peer3 != null) {
                                                            peer2 = peer3;
                                                        }
                                                        messageFwdHeader.from_id = peer2;
                                                    }
                                                }
                                                if (messageObject3.messageOwner.post_author == null) {
                                                    tL_message2.fwd_from.post_author = ContactsController.formatName(user.first_name, user.last_name);
                                                    tL_message2.fwd_from.flags |= 8;
                                                }
                                                tL_message2.date = messageObject3.messageOwner.date;
                                                tL_message2.flags = 4;
                                            }
                                            if (j6 == clientUserId) {
                                                messageFwdHeader2.flags |= 16;
                                                messageFwdHeader2.saved_from_msg_id = messageObject3.getId();
                                                TLRPC.MessageFwdHeader messageFwdHeader14 = tL_message2.fwd_from;
                                                peer4 = messageObject3.messageOwner.peer_id;
                                                messageFwdHeader14.saved_from_peer = peer4;
                                                if (peer4.user_id == clientUserId) {
                                                    peer4.user_id = messageObject3.getDialogId();
                                                }
                                            }
                                        }
                                        str2 = str;
                                        longSparseArray5 = longSparseArray9;
                                        if (j6 == clientUserId) {
                                            messageFwdHeader2.flags |= 16;
                                            messageFwdHeader2.saved_from_msg_id = messageObject3.getId();
                                            TLRPC.MessageFwdHeader messageFwdHeader15 = tL_message2.fwd_from;
                                            peer4 = messageObject3.messageOwner.peer_id;
                                            messageFwdHeader15.saved_from_peer = peer4;
                                            if (peer4.user_id == clientUserId) {
                                                peer4.user_id = messageObject3.getDialogId();
                                            }
                                        }
                                    }
                                    HashMap map = new HashMap();
                                    tL_message2.params = map;
                                    map.put("fwd_id", _UrlKt.FRAGMENT_ENCODE_SET + messageObject3.getId());
                                    HashMap map2 = tL_message2.params;
                                    StringBuilder sb = new StringBuilder();
                                    sb.append(_UrlKt.FRAGMENT_ENCODE_SET);
                                    String str3 = str2;
                                    sb.append(messageObject3.getDialogId());
                                    map2.put("fwd_peer", sb.toString());
                                    if (!messageObject3.messageOwner.restriction_reason.isEmpty()) {
                                        tL_message2.restriction_reason = messageObject3.messageOwner.restriction_reason;
                                        tL_message2.flags |= 4194304;
                                    }
                                    if (!z7 && (messageObject3.messageOwner.media instanceof TLRPC.TL_messageMediaWebPage)) {
                                        tL_message2.media = new TLRPC.TL_messageMediaEmpty();
                                    } else {
                                        tL_message2.media = messageObject3.messageOwner.media;
                                    }
                                    TLRPC.Message message3 = messageObject3.messageOwner;
                                    tL_message2.invert_media = message3.invert_media;
                                    if (tL_message2.media != null) {
                                        tL_message2.flags |= 512;
                                    }
                                    long j8 = message3.via_bot_id;
                                    if (j8 != 0) {
                                        tL_message2.via_bot_id = j8;
                                        tL_message2.flags |= 2048;
                                    }
                                    if (j4 != 0) {
                                        TLRPC.TL_messageReplies tL_messageReplies = new TLRPC.TL_messageReplies();
                                        tL_message2.replies = tL_messageReplies;
                                        boolean z30 = z23;
                                        tL_messageReplies.comments = z30;
                                        tL_messageReplies.channel_id = j4;
                                        tL_messageReplies.flags = (tL_messageReplies.flags | (z30 ? 1 : 0)) == true ? 1 : 0;
                                        tL_message2.flags |= 8388608;
                                    }
                                    if (!z2 || tL_message2.media == null) {
                                        tL_message2.message = messageObject3.messageOwner.message;
                                    }
                                    if (tL_message2.message == null) {
                                        tL_message2.message = _UrlKt.FRAGMENT_ENCODE_SET;
                                    }
                                    tL_message2.fwd_msg_id = messageObject3.getId();
                                    TLRPC.Message message4 = messageObject3.messageOwner;
                                    tL_message2.attachPath = message4.attachPath;
                                    tL_message2.entities = message4.entities;
                                    if (message4.reply_markup instanceof TLRPC.TL_replyInlineMarkup) {
                                        tL_message2.reply_markup = new TLRPC.TL_replyInlineMarkup();
                                        int size = messageObject3.messageOwner.reply_markup.rows.size();
                                        boolean z31 = z22;
                                        boolean z32 = z31;
                                        while (true) {
                                            if (r2 >= size) {
                                                r2 = z31;
                                                boolean z33 = z32 ? 1 : 0;
                                                break;
                                            }
                                            r2 = z31;
                                            TLRPC.TL_keyboardButtonRow tL_keyboardButtonRow2 = (TLRPC.TL_keyboardButtonRow) messageObject3.messageOwner.reply_markup.rows.get(r2 == true ? 1 : 0);
                                            int size2 = tL_keyboardButtonRow2.buttons.size();
                                            int i14 = size;
                                            ?? r1 = z22;
                                            TLRPC.TL_keyboardButtonRow tL_keyboardButtonRow3 = null;
                                            ?? r3 = r2;
                                            boolean z34 = z32;
                                            while (true) {
                                                r47 = r3;
                                                if (r1 >= size2) {
                                                    boolean z35 = z34 ? 1 : 0;
                                                    z32 = z34;
                                                    break;
                                                }
                                                TLRPC.KeyboardButton keyboardButton = (TLRPC.KeyboardButton) tL_keyboardButtonRow2.buttons.get(r1);
                                                ?? r49 = r1;
                                                boolean z36 = keyboardButton instanceof TLRPC.TL_keyboardButtonUrlAuth;
                                                if (!z36 && !(keyboardButton instanceof TLRPC.TL_keyboardButtonUrl) && !(keyboardButton instanceof TLRPC.TL_keyboardButtonSwitchInline) && !(keyboardButton instanceof TLRPC.TL_keyboardButtonBuy)) {
                                                    z32 = true;
                                                    break;
                                                }
                                                if (z36) {
                                                    TLRPC.TL_keyboardButtonUrlAuth tL_keyboardButtonUrlAuth = new TLRPC.TL_keyboardButtonUrlAuth();
                                                    tL_keyboardButtonUrlAuth.flags = keyboardButton.flags;
                                                    String str4 = keyboardButton.fwd_text;
                                                    if (str4 != null) {
                                                        tL_keyboardButtonUrlAuth.fwd_text = str4;
                                                        tL_keyboardButtonUrlAuth.text = str4;
                                                    } else {
                                                        tL_keyboardButtonUrlAuth.text = keyboardButton.text;
                                                    }
                                                    tL_keyboardButtonUrlAuth.url = keyboardButton.url;
                                                    tL_keyboardButtonUrlAuth.button_id = keyboardButton.button_id;
                                                    keyboardButton = tL_keyboardButtonUrlAuth;
                                                }
                                                if (tL_keyboardButtonRow3 == null) {
                                                    tL_keyboardButtonRow = new TLRPC.TL_keyboardButtonRow();
                                                    tL_message2.reply_markup.rows.add(tL_keyboardButtonRow);
                                                } else {
                                                    tL_keyboardButtonRow = tL_keyboardButtonRow3;
                                                }
                                                tL_keyboardButtonRow.buttons.add(keyboardButton);
                                                tL_keyboardButtonRow3 = tL_keyboardButtonRow;
                                                r1 = (r49 == true ? 1 : 0) + 1;
                                                r3 = r47 == true ? 1 : 0;
                                                z34 = z34 ? 1 : 0;
                                            }
                                            if (z32) {
                                                break;
                                            }
                                            size = i14;
                                            r2 = (r47 == true ? 1 : 0) + 1;
                                            z32 = z32;
                                        }
                                        if (!z32) {
                                            tL_message2.flags |= 64;
                                        } else {
                                            messageObject3.messageOwner.reply_markup = null;
                                            tL_message2.flags &= -65;
                                        }
                                    }
                                    if (!tL_message2.entities.isEmpty()) {
                                        tL_message2.flags |= 128;
                                    }
                                    if (tL_message2.attachPath == null) {
                                        tL_message2.attachPath = _UrlKt.FRAGMENT_ENCODE_SET;
                                    }
                                    int newMessageId = getUserConfig().getNewMessageId();
                                    tL_message2.id = newMessageId;
                                    tL_message2.local_id = newMessageId;
                                    tL_message2.out = true;
                                    long j9 = messageObject3.messageOwner.grouped_id;
                                    if (j9 != 0) {
                                        Long lValueOf = (Long) longSparseArray10.get(j9);
                                        if (lValueOf == null) {
                                            lValueOf = Long.valueOf(Utilities.random.nextLong());
                                            longSparseArray10.put(messageObject3.messageOwner.grouped_id, lValueOf);
                                        }
                                        tL_message2.grouped_id = lValueOf.longValue();
                                        tL_message2.flags |= 131072;
                                    }
                                    if (peer5.channel_id != 0 && z10) {
                                        if (z11) {
                                            TLRPC.TL_peerUser tL_peerUser = new TLRPC.TL_peerUser();
                                            tL_message2.from_id = tL_peerUser;
                                            tL_peerUser.user_id = clientUserId;
                                        } else {
                                            tL_message2.from_id = peer5;
                                        }
                                        tL_message2.post = true;
                                        j5 = j;
                                        j4 = j4;
                                        chat = chat2;
                                    } else {
                                        j5 = j;
                                        j4 = j4;
                                        chat = chat2;
                                        long sendAsPeerId = ChatObject.getSendAsPeerId(chat, getMessagesController().getChatFull(-j5), true);
                                        if (sendAsPeerId == clientUserId) {
                                            TLRPC.TL_peerUser tL_peerUser2 = new TLRPC.TL_peerUser();
                                            tL_message2.from_id = tL_peerUser2;
                                            tL_peerUser2.user_id = clientUserId;
                                            tL_message2.flags |= 256;
                                        } else {
                                            tL_message2.from_id = getMessagesController().getPeer(sendAsPeerId);
                                            if (str3 != null) {
                                                tL_message2.post_author = str3;
                                                tL_message2.flags |= 65536;
                                            }
                                        }
                                    }
                                    if (tL_message2.random_id == 0) {
                                        tL_message2.random_id = getNextRandomId();
                                    }
                                    arrayList14.add(Long.valueOf(tL_message2.random_id));
                                    longSparseArray = longSparseArray5;
                                    longSparseArray.put(tL_message2.random_id, tL_message2);
                                    ArrayList arrayList16 = arrayList5;
                                    arrayList16.add(Integer.valueOf(tL_message2.fwd_msg_id));
                                    tL_message2.date = i5 != 0 ? i5 : getConnectionsManager().getCurrentTime();
                                    TLRPC.InputPeer inputPeer5 = inputPeer4;
                                    boolean z37 = inputPeer5 instanceof TLRPC.TL_inputPeerChannel;
                                    if (!z37 || !z10) {
                                        TLRPC.Message message5 = messageObject3.messageOwner;
                                        if ((message5.flags & 1024) != 0 && i5 == 0) {
                                            tL_message2.views = message5.views;
                                            tL_message2.flags |= 1024;
                                        }
                                        tL_message2.unread = true;
                                    } else if (i5 == 0) {
                                        tL_message2.views = 1;
                                        tL_message2.flags |= 1024;
                                    }
                                    tL_message2.dialog_id = j5;
                                    tL_message2.peer_id = peer5;
                                    if (MessageObject.isVoiceMessage(tL_message2) || MessageObject.isRoundVideoMessage(tL_message2)) {
                                        if (z37 && messageObject3.getChannelId() != 0) {
                                            tL_message2.media_unread = messageObject3.isContentUnread();
                                        } else {
                                            tL_message2.media_unread = true;
                                        }
                                    }
                                    if (messageObject == null && messageSuggestionParams == null && (messageReplyHeader = (message = messageObject3.messageOwner).reply_to) != null) {
                                        TLRPC.Peer peer8 = messageReplyHeader.reply_to_peer_id;
                                        if (peer8 == null || MessageObject.peersEqual(peer8, message.peer_id)) {
                                            TLRPC.MessageReplyHeader messageReplyHeader2 = messageObject3.messageOwner.reply_to;
                                            if ((messageReplyHeader2.flags & 16) != 0 && arrayList16.contains(Integer.valueOf(messageReplyHeader2.reply_to_msg_id))) {
                                                tL_message2.flags |= 8;
                                                tL_message2.reply_to = messageObject3.messageOwner.reply_to;
                                            }
                                        } else {
                                            tL_message2.flags |= 8;
                                            tL_message2.reply_to = messageObject3.messageOwner.reply_to;
                                        }
                                    }
                                    if (j2 > 0) {
                                        tL_message2.flags2 |= 64;
                                        tL_message2.paid_message_stars = j2;
                                    }
                                    str = str3;
                                    if (j3 != 0) {
                                        tL_message = tL_message2;
                                        tL_message.saved_peer_id = getMessagesController().getPeer(j3);
                                        tL_message.flags |= 268435456;
                                    } else {
                                        tL_message = tL_message2;
                                    }
                                    if (messageSuggestionParams != null) {
                                        tL_message.suggested_post = messageSuggestionParams.toTl();
                                    }
                                    LongSparseArray longSparseArray11 = longSparseArray10;
                                    clientUserId = clientUserId;
                                    MessageObject messageObject4 = new MessageObject(this.currentAccount, tL_message, true, true);
                                    messageObject4.scheduled = i5 != 0 ? true : z22;
                                    messageObject4.messageOwner.send_state = 1;
                                    messageObject4.wasJustSent = true;
                                    ArrayList<MessageObject> arrayList17 = arrayList9;
                                    arrayList17.add(messageObject4);
                                    ArrayList arrayList18 = arrayList13;
                                    arrayList18.add(tL_message);
                                    StarsController.getInstance(this.currentAccount).beforeSendingMessage(messageObject4);
                                    final TLRPC.Peer peer9 = peer5;
                                    if (messageObject3.replyMessageObject == null) {
                                        arrayList6 = arrayList;
                                        break;
                                    }
                                    ?? r10 = z22;
                                    while (true) {
                                        if (r10 >= arrayList.size()) {
                                            arrayList6 = arrayList;
                                            break;
                                        }
                                        arrayList6 = arrayList;
                                        ?? r51 = r10;
                                        if (arrayList6.get(r10).getId() == messageObject3.replyMessageObject.getId()) {
                                            TLRPC.Message message6 = messageObject4.messageOwner;
                                            MessageObject messageObject5 = messageObject3.replyMessageObject;
                                            message6.replyMessage = messageObject5.messageOwner;
                                            messageObject4.replyMessageObject = messageObject5;
                                            break;
                                        }
                                        r10 = (r51 == true ? 1 : 0) + 1;
                                    }
                                    putToSendingMessages(tL_message, i5 != 0 ? true : z22);
                                    if (BuildVars.LOGS_ENABLED) {
                                        FileLog.d("forward message user_id = " + inputPeer5.user_id + " chat_id = " + inputPeer5.chat_id + " channel_id = " + inputPeer5.channel_id + " access_hash = " + inputPeer5.access_hash);
                                    }
                                    if (messageObject == null || messageSuggestionParams != null) {
                                        messageObject2 = messageObject;
                                    } else {
                                        TLRPC.TL_messageReplyHeader tL_messageReplyHeader = new TLRPC.TL_messageReplyHeader();
                                        tL_message.reply_to = tL_messageReplyHeader;
                                        tL_messageReplyHeader.flags |= 16;
                                        tL_messageReplyHeader.reply_to_msg_id = messageObject.getId();
                                        messageObject2 = messageObject;
                                        if (messageObject2.isTopicMainMessage) {
                                            TLRPC.MessageReplyHeader messageReplyHeader3 = tL_message.reply_to;
                                            messageReplyHeader3.forum_topic = true;
                                            messageReplyHeader3.flags |= 8;
                                        }
                                        if (arrayList18.size() != 100) {
                                            i6 = i8;
                                            if (i6 != arrayList6.size() - 1 && (i6 == arrayList6.size() - 1 || arrayList6.get(i6 + 1).getDialogId() == messageObject3.getDialogId())) {
                                                arrayList2 = arrayList16;
                                                j6 = j5;
                                                arrayList3 = arrayList18;
                                                peer = peer9;
                                                z14 = true;
                                                inputPeer = inputPeer5;
                                                arrayList4 = arrayList17;
                                                i8 = i6;
                                                longSparseArray6 = longSparseArray11;
                                                longSparseArray4 = longSparseArray6;
                                                arrayList14 = arrayList14;
                                                arrayList15 = arrayList2;
                                                longSparseArray9 = longSparseArray;
                                                arrayList13 = arrayList3;
                                            }
                                        } else {
                                            i6 = i8;
                                        }
                                        MessagesStorage messagesStorage = getMessagesStorage();
                                        ArrayList<TLRPC.Message> arrayList19 = new ArrayList<>(arrayList18);
                                        if (i5 != 0) {
                                            i9 = 1;
                                        } else {
                                            i9 = z22;
                                        }
                                        messagesStorage.putMessages(arrayList19, false, true, false, 0, i9, 0L);
                                        MessagesController messagesController = getMessagesController();
                                        if (i5 != 0) {
                                            i10 = 1;
                                        } else {
                                            i10 = z22;
                                        }
                                        messagesController.updateInterfaceWithMessages(j5, arrayList17, i10);
                                        int i15 = z22;
                                        getNotificationCenter().lambda$postNotificationNameOnUIThread$1(NotificationCenter.dialogsNeedReload, new Object[i15]);
                                        getUserConfig().saveConfig(i15);
                                        tL_messages_forwardMessages = new TLRPC.TL_messages_forwardMessages();
                                        tL_messages_forwardMessages.to_peer = inputPeer5;
                                        if (z21) {
                                            if (MessagesController.getNotificationsSettings(this.currentAccount).getBoolean(NotificationsSettingsFacade.PROPERTY_SILENT + j5, false) || AyuGhostController.getInstance(this.currentAccount).isSendWithoutSound()) {
                                                z15 = false;
                                            } else {
                                                z15 = true;
                                            }
                                        } else {
                                            z15 = true;
                                        }
                                        tL_messages_forwardMessages.silent = z15;
                                        if (messageObject2 != null) {
                                            tL_messages_forwardMessages.top_msg_id = messageObject2.getId();
                                            tL_messages_forwardMessages.flags |= 512;
                                        }
                                        if (i5 != 0) {
                                            i11 = i5;
                                            tL_messages_forwardMessages.schedule_date = i11;
                                            i12 = tL_messages_forwardMessages.flags;
                                            tL_messages_forwardMessages.flags = i12 | 1024;
                                            if (i2 != 0) {
                                                tL_messages_forwardMessages.schedule_repeat_period = i2;
                                                tL_messages_forwardMessages.flags = i12 | 16778240;
                                            }
                                        } else {
                                            i11 = i5;
                                        }
                                        if (messageObject3.messageOwner.peer_id instanceof TLRPC.TL_peerChannel) {
                                            inputPeer2 = inputPeer5;
                                            chat3 = getMessagesController().getChat(Long.valueOf(messageObject3.messageOwner.peer_id.channel_id));
                                            tL_inputPeerChannel = new TLRPC.TL_inputPeerChannel();
                                            tL_messages_forwardMessages.from_peer = tL_inputPeerChannel;
                                            arrayList7 = arrayList18;
                                            tL_inputPeerChannel.channel_id = messageObject3.messageOwner.peer_id.channel_id;
                                            if (chat3 != null) {
                                                tL_inputPeerChannel.access_hash = chat3.access_hash;
                                            }
                                        } else {
                                            inputPeer2 = inputPeer5;
                                            arrayList7 = arrayList18;
                                            tL_messages_forwardMessages.from_peer = new TLRPC.TL_inputPeerEmpty();
                                        }
                                        tL_messages_forwardMessages.random_id = arrayList14;
                                        tL_messages_forwardMessages.id = arrayList16;
                                        tL_messages_forwardMessages.drop_author = z;
                                        tL_messages_forwardMessages.drop_media_captions = z2;
                                        if (arrayList6.size() == 1 || !arrayList6.get(0).messageOwner.with_my_score) {
                                            z16 = false;
                                        } else {
                                            z16 = true;
                                        }
                                        tL_messages_forwardMessages.with_my_score = z16;
                                        if (i3 >= 0) {
                                            tL_messages_forwardMessages.flags |= 1048576;
                                            tL_messages_forwardMessages.video_timestamp = i3;
                                        }
                                        if (j2 > 0) {
                                            tL_messages_forwardMessages.flags |= TLObject.FLAG_21;
                                            tL_messages_forwardMessages.allow_paid_stars = ((long) tL_messages_forwardMessages.id.size()) * j2;
                                        }
                                        if (messageSuggestionParams != null) {
                                            tL_messages_forwardMessages.suggested_post = messageSuggestionParams.toTl();
                                            if (messageObject != null) {
                                                TLRPC.TL_inputReplyToMessage tL_inputReplyToMessage = new TLRPC.TL_inputReplyToMessage();
                                                tL_messages_forwardMessages.reply_to = tL_inputReplyToMessage;
                                                tL_inputReplyToMessage.reply_to_msg_id = messageObject.getId();
                                            }
                                        }
                                        applyMonoForumPeerId(tL_messages_forwardMessages, j3);
                                        arrayList8 = new ArrayList(arrayList17);
                                        final int i16 = i11;
                                        if (i11 == 2147483646) {
                                            z17 = true;
                                        } else {
                                            z17 = false;
                                        }
                                        arrayList2 = arrayList16;
                                        arrayList14 = arrayList14;
                                        final ArrayList arrayList20 = arrayList7;
                                        final boolean z38 = z27;
                                        arrayList4 = arrayList17;
                                        inputPeer = inputPeer2;
                                        z14 = true;
                                        final Runnable runnable2 = new Runnable() { // from class: org.telegram.messenger.SendMessagesHelper$$ExternalSyntheticLambda9
                                            @Override // java.lang.Runnable
                                            public final void run() {
                                                this.f$0.lambda$sendMessage$21(tL_messages_forwardMessages, j, i16, z17, z38, longSparseArray, arrayList20, arrayList8, messageObject3, peer9);
                                            }
                                        };
                                        sendMessagesHelper = this;
                                        z27 = z38;
                                        arrayList3 = arrayList20;
                                        peer = peer9;
                                        i7 = i16;
                                        runnable = new Runnable() { // from class: org.telegram.messenger.SendMessagesHelper$$ExternalSyntheticLambda10
                                            @Override // java.lang.Runnable
                                            public final void run() {
                                                this.f$0.lambda$sendMessage$22(tL_messages_forwardMessages, arrayList8, runnable2);
                                            }
                                        };
                                        if (StarsController.getInstance(sendMessagesHelper.currentAccount).beforeSendingFinalRequest(tL_messages_forwardMessages, arrayList8, runnable)) {
                                            runnable.run();
                                        }
                                        longSparseArray3 = longSparseArray11;
                                        if (i6 != arrayList.size() - 1) {
                                            ArrayList<MessageObject> arrayList21 = new ArrayList<>();
                                            arrayList13 = new ArrayList();
                                            arrayList4 = arrayList21;
                                            arrayList14 = new ArrayList();
                                            arrayList15 = new ArrayList();
                                            z21 = z21;
                                            j6 = j;
                                            i8 = i6;
                                            longSparseArray9 = new LongSparseArray();
                                            i5 = i7;
                                            r38 = r38;
                                            longSparseArray4 = longSparseArray11;
                                        }
                                        longSparseArray3 = longSparseArray2;
                                        j6 = j;
                                        i5 = i7;
                                        z21 = z21;
                                        i8 = i6;
                                        longSparseArray4 = longSparseArray3;
                                        arrayList14 = arrayList14;
                                        arrayList15 = arrayList2;
                                        longSparseArray9 = longSparseArray;
                                        arrayList13 = arrayList3;
                                    }
                                    if (arrayList18.size() != 100) {
                                        i6 = i8;
                                        if (i6 != arrayList6.size() - 1) {
                                            arrayList2 = arrayList16;
                                            j6 = j5;
                                            arrayList3 = arrayList18;
                                            peer = peer9;
                                            z14 = true;
                                            inputPeer = inputPeer5;
                                            arrayList4 = arrayList17;
                                            i8 = i6;
                                            longSparseArray6 = longSparseArray11;
                                            longSparseArray4 = longSparseArray6;
                                            arrayList14 = arrayList14;
                                            arrayList15 = arrayList2;
                                            longSparseArray9 = longSparseArray;
                                            arrayList13 = arrayList3;
                                        }
                                    } else {
                                        i6 = i8;
                                    }
                                    MessagesStorage messagesStorage2 = getMessagesStorage();
                                    ArrayList<TLRPC.Message> arrayList110 = new ArrayList<>(arrayList18);
                                    if (i5 != 0) {
                                        i9 = 1;
                                    } else {
                                        i9 = z22;
                                    }
                                    messagesStorage2.putMessages(arrayList110, false, true, false, 0, i9, 0L);
                                    MessagesController messagesController2 = getMessagesController();
                                    if (i5 != 0) {
                                        i10 = 1;
                                    } else {
                                        i10 = z22;
                                    }
                                    messagesController2.updateInterfaceWithMessages(j5, arrayList17, i10);
                                    int i17 = z22;
                                    getNotificationCenter().lambda$postNotificationNameOnUIThread$1(NotificationCenter.dialogsNeedReload, new Object[i17]);
                                    getUserConfig().saveConfig(i17);
                                    tL_messages_forwardMessages = new TLRPC.TL_messages_forwardMessages();
                                    tL_messages_forwardMessages.to_peer = inputPeer5;
                                    if (z21) {
                                        if (MessagesController.getNotificationsSettings(this.currentAccount).getBoolean(NotificationsSettingsFacade.PROPERTY_SILENT + j5, false)) {
                                        }
                                        z15 = false;
                                    } else {
                                        z15 = true;
                                    }
                                    tL_messages_forwardMessages.silent = z15;
                                    if (messageObject2 != null) {
                                        tL_messages_forwardMessages.top_msg_id = messageObject2.getId();
                                        tL_messages_forwardMessages.flags |= 512;
                                    }
                                    if (i5 != 0) {
                                        i11 = i5;
                                        tL_messages_forwardMessages.schedule_date = i11;
                                        i12 = tL_messages_forwardMessages.flags;
                                        tL_messages_forwardMessages.flags = i12 | 1024;
                                        if (i2 != 0) {
                                            tL_messages_forwardMessages.schedule_repeat_period = i2;
                                            tL_messages_forwardMessages.flags = i12 | 16778240;
                                        }
                                    } else {
                                        i11 = i5;
                                    }
                                    if (messageObject3.messageOwner.peer_id instanceof TLRPC.TL_peerChannel) {
                                        inputPeer2 = inputPeer5;
                                        chat3 = getMessagesController().getChat(Long.valueOf(messageObject3.messageOwner.peer_id.channel_id));
                                        tL_inputPeerChannel = new TLRPC.TL_inputPeerChannel();
                                        tL_messages_forwardMessages.from_peer = tL_inputPeerChannel;
                                        arrayList7 = arrayList18;
                                        tL_inputPeerChannel.channel_id = messageObject3.messageOwner.peer_id.channel_id;
                                        if (chat3 != null) {
                                            tL_inputPeerChannel.access_hash = chat3.access_hash;
                                        }
                                    } else {
                                        inputPeer2 = inputPeer5;
                                        arrayList7 = arrayList18;
                                        tL_messages_forwardMessages.from_peer = new TLRPC.TL_inputPeerEmpty();
                                    }
                                    tL_messages_forwardMessages.random_id = arrayList14;
                                    tL_messages_forwardMessages.id = arrayList16;
                                    tL_messages_forwardMessages.drop_author = z;
                                    tL_messages_forwardMessages.drop_media_captions = z2;
                                    if (arrayList6.size() == 1) {
                                        z16 = false;
                                    } else {
                                        z16 = false;
                                    }
                                    tL_messages_forwardMessages.with_my_score = z16;
                                    if (i3 >= 0) {
                                        tL_messages_forwardMessages.flags |= 1048576;
                                        tL_messages_forwardMessages.video_timestamp = i3;
                                    }
                                    if (j2 > 0) {
                                        tL_messages_forwardMessages.flags |= TLObject.FLAG_21;
                                        tL_messages_forwardMessages.allow_paid_stars = ((long) tL_messages_forwardMessages.id.size()) * j2;
                                    }
                                    if (messageSuggestionParams != null) {
                                        tL_messages_forwardMessages.suggested_post = messageSuggestionParams.toTl();
                                        if (messageObject != null) {
                                            TLRPC.TL_inputReplyToMessage tL_inputReplyToMessage2 = new TLRPC.TL_inputReplyToMessage();
                                            tL_messages_forwardMessages.reply_to = tL_inputReplyToMessage2;
                                            tL_inputReplyToMessage2.reply_to_msg_id = messageObject.getId();
                                        }
                                    }
                                    applyMonoForumPeerId(tL_messages_forwardMessages, j3);
                                    arrayList8 = new ArrayList(arrayList17);
                                    final int i18 = i11;
                                    if (i11 == 2147483646) {
                                        z17 = true;
                                    } else {
                                        z17 = false;
                                    }
                                    arrayList2 = arrayList16;
                                    arrayList14 = arrayList14;
                                    final ArrayList arrayList22 = arrayList7;
                                    final boolean z39 = z27;
                                    arrayList4 = arrayList17;
                                    inputPeer = inputPeer2;
                                    z14 = true;
                                    final Runnable runnable3 = new Runnable() { // from class: org.telegram.messenger.SendMessagesHelper$$ExternalSyntheticLambda9
                                        @Override // java.lang.Runnable
                                        public final void run() {
                                            this.f$0.lambda$sendMessage$21(tL_messages_forwardMessages, j, i18, z17, z39, longSparseArray, arrayList22, arrayList8, messageObject3, peer9);
                                        }
                                    };
                                    sendMessagesHelper = this;
                                    z27 = z39;
                                    arrayList3 = arrayList22;
                                    peer = peer9;
                                    i7 = i18;
                                    runnable = new Runnable() { // from class: org.telegram.messenger.SendMessagesHelper$$ExternalSyntheticLambda10
                                        @Override // java.lang.Runnable
                                        public final void run() {
                                            this.f$0.lambda$sendMessage$22(tL_messages_forwardMessages, arrayList8, runnable3);
                                        }
                                    };
                                    if (StarsController.getInstance(sendMessagesHelper.currentAccount).beforeSendingFinalRequest(tL_messages_forwardMessages, arrayList8, runnable)) {
                                        runnable.run();
                                    }
                                    longSparseArray3 = longSparseArray11;
                                    if (i6 != arrayList.size() - 1) {
                                        ArrayList<MessageObject> arrayList23 = new ArrayList<>();
                                        arrayList13 = new ArrayList();
                                        arrayList4 = arrayList23;
                                        arrayList14 = new ArrayList();
                                        arrayList15 = new ArrayList();
                                        z21 = z21;
                                        j6 = j;
                                        i8 = i6;
                                        longSparseArray9 = new LongSparseArray();
                                        i5 = i7;
                                        r38 = r38;
                                        longSparseArray4 = longSparseArray11;
                                    }
                                    longSparseArray3 = longSparseArray2;
                                    j6 = j;
                                    i5 = i7;
                                    z21 = z21;
                                    i8 = i6;
                                    longSparseArray4 = longSparseArray3;
                                    arrayList14 = arrayList14;
                                    arrayList15 = arrayList2;
                                    longSparseArray9 = longSparseArray;
                                    arrayList13 = arrayList3;
                                } else if (r38 == 0) {
                                    r14 = ChatObject.isActionBannedByDefault(chat, 19) ? 17 : 18;
                                    r37 = r14;
                                    r36 = r37;
                                    z14 = z23;
                                    peer = peer5;
                                    arrayList4 = arrayList9;
                                    inputPeer = inputPeer4;
                                    longSparseArray4 = longSparseArray10;
                                    r38 = r36;
                                }
                            } else if (chat != null) {
                                if (r38 == 0) {
                                    r14 = ChatObject.isActionBannedByDefault(chat, 21) ? 15 : 16;
                                    r37 = r14;
                                    r36 = r37;
                                    z14 = z23;
                                    peer = peer5;
                                    arrayList4 = arrayList9;
                                    inputPeer = inputPeer4;
                                    longSparseArray4 = longSparseArray10;
                                    r38 = r36;
                                }
                            } else if (r38 == 0) {
                                chat = chat;
                                str = str;
                                j6 = j6;
                                j4 = j4;
                                clientUserId = clientUserId;
                                r36 = 8;
                                z14 = z23;
                                peer = peer5;
                                arrayList4 = arrayList9;
                                inputPeer = inputPeer4;
                                longSparseArray4 = longSparseArray10;
                                r38 = r36;
                            }
                        } else if (chat != null) {
                            if (r38 == 0) {
                                c = ChatObject.isActionBannedByDefault(chat, 20) ? '\r' : (char) 14;
                                r37 = c;
                                r36 = r37;
                                z14 = z23;
                                peer = peer5;
                                arrayList4 = arrayList9;
                                inputPeer = inputPeer4;
                                longSparseArray4 = longSparseArray10;
                                r38 = r36;
                            }
                        } else if (r38 == 0) {
                            r37 = 7;
                            r36 = r37;
                            z14 = z23;
                            peer = peer5;
                            arrayList4 = arrayList9;
                            inputPeer = inputPeer4;
                            longSparseArray4 = longSparseArray10;
                            r38 = r36;
                        }
                        arrayList2 = arrayList15;
                        chat = chat;
                        str = str;
                        j6 = j6;
                        j4 = j4;
                        clientUserId = clientUserId;
                        longSparseArray = longSparseArray9;
                        i8 = i8;
                        arrayList3 = arrayList13;
                        z14 = z23;
                        peer = peer5;
                        arrayList4 = arrayList9;
                        inputPeer = inputPeer4;
                        longSparseArray6 = longSparseArray10;
                        longSparseArray4 = longSparseArray6;
                        arrayList14 = arrayList14;
                        arrayList15 = arrayList2;
                        longSparseArray9 = longSparseArray;
                        arrayList13 = arrayList3;
                    } else {
                        if (r38 == 0) {
                            r14 = ChatObject.isActionBannedByDefault(chat, 10) ? 21 : 22;
                            r37 = r14;
                            r36 = r37;
                            z14 = z23;
                            peer = peer5;
                            arrayList4 = arrayList9;
                            inputPeer = inputPeer4;
                            longSparseArray4 = longSparseArray10;
                            r38 = r36;
                        }
                        arrayList2 = arrayList15;
                        chat = chat;
                        str = str;
                        j6 = j6;
                        j4 = j4;
                        clientUserId = clientUserId;
                        longSparseArray = longSparseArray9;
                        i8 = i8;
                        arrayList3 = arrayList13;
                        z14 = z23;
                        peer = peer5;
                        arrayList4 = arrayList9;
                        inputPeer = inputPeer4;
                        longSparseArray6 = longSparseArray10;
                        longSparseArray4 = longSparseArray6;
                        arrayList14 = arrayList14;
                        arrayList15 = arrayList2;
                        longSparseArray9 = longSparseArray;
                        arrayList13 = arrayList3;
                    }
                }
                i13 = i8 + 1;
                z21 = z21;
                z23 = z14;
                longSparseArray10 = longSparseArray4;
                peer5 = peer;
                j4 = j4;
                clientUserId = clientUserId;
                z22 = false;
                arrayList13 = arrayList13;
                i5 = i5;
                inputPeer4 = inputPeer;
                chat = chat;
                arrayList9 = arrayList4;
                z27 = z27;
                j6 = j6;
                str = str;
                r38 = r38;
            }
            return r38;
        }
        SendMessagesHelper sendMessagesHelper2 = this;
        long j10 = j6;
        long j11 = sendMessagesHelper2.getMessagesController().getEncryptedChat(Integer.valueOf((int) j10)).user_id;
        boolean z40 = (!DialogObject.isUserDialog(j11) || sendMessagesHelper2.getMessagesController().getUser(Long.valueOf(j11)) == null || (userFull = sendMessagesHelper2.getMessagesController().getUserFull(j11)) == null) ? true : !userFull.voice_messages_forbidden;
        int i19 = 0;
        for (int i20 = 0; i20 < arrayList.size(); i20++) {
            MessageObject messageObject6 = arrayList.get(i20);
            if (z40 || !MessageObject.isVoiceMessage(messageObject6.messageOwner)) {
                if (!z40 && MessageObject.isRoundVideoMessage(messageObject6.messageOwner) && i19 == 0) {
                    i19 = 8;
                }
            } else if (i19 == 0) {
                i19 = 7;
            }
        }
        if (i19 == 0) {
            int i21 = 0;
            while (i21 < arrayList.size()) {
                sendMessagesHelper2.processForwardFromMyName(arrayList.get(i21), j10, j2, j3, messageSuggestionParams);
                i21++;
                sendMessagesHelper2 = this;
                j10 = j;
            }
        }
        return i19;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$sendMessage$7(ArrayList arrayList, long j, boolean z, boolean z2, MessageObject messageObject) {
        try {
            AyuForward.forwardMessages(this.currentAccount, arrayList, j, z, z2, messageObject, null);
        } catch (Exception e) {
            AyuForward.forceStop();
            Log.w("AyuForward", e);
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$sendMessage$8() {
        getNotificationCenter().lambda$postNotificationNameOnUIThread$1(AyuConstants.FIX_FORWARD, new Object[0]);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$sendMessage$9(ArrayList arrayList, long j, boolean z, boolean z2, boolean z3, int i, MessageObject messageObject) {
        try {
            AyuForward.intelligentForward(this.currentAccount, arrayList, j, z, z2, z3, i, messageObject);
        } catch (Exception e) {
            AyuForward.forceStop();
            Log.w("AyuForward", e);
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$sendMessage$10() {
        getNotificationCenter().lambda$postNotificationNameOnUIThread$1(AyuConstants.FIX_FORWARD, new Object[0]);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$sendMessage$11(ArrayList arrayList, long j, boolean z, boolean z2, boolean z3, int i, int i2, MessageObject messageObject, int i3, long j2, MessageSuggestionParams messageSuggestionParams, Long l) {
        sendMessage(arrayList, j, z, z2, z3, i, i2, messageObject, i3, l.longValue(), j2, messageSuggestionParams);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$sendMessage$21(final TLRPC.TL_messages_forwardMessages tL_messages_forwardMessages, final long j, final int i, final boolean z, final boolean z2, final LongSparseArray longSparseArray, final ArrayList arrayList, final ArrayList arrayList2, final MessageObject messageObject, final TLRPC.Peer peer) {
        getConnectionsManager().sendRequest(tL_messages_forwardMessages, new RequestDelegate() { // from class: org.telegram.messenger.SendMessagesHelper$$ExternalSyntheticLambda47
            @Override // org.telegram.tgnet.RequestDelegate
            public final void run(TLObject tLObject, TLRPC.TL_error tL_error) {
                this.f$0.lambda$sendMessage$20(j, i, z, z2, longSparseArray, arrayList, arrayList2, messageObject, peer, tL_messages_forwardMessages, tLObject, tL_error);
            }
        }, 68);
    }

    /* JADX INFO: Access modifiers changed from: private */
    /* JADX WARN: Code duplicated, block: B:47:0x010e  */
    /* JADX WARN: Code duplicated, block: B:50:0x0114  */
    /* JADX WARN: Code duplicated, block: B:52:0x011c  */
    /* JADX WARN: Code duplicated, block: B:53:0x011e  */
    /* JADX WARN: Code duplicated, block: B:56:0x0123  */
    /* JADX WARN: Code duplicated, block: B:57:0x012c  */
    /* JADX WARN: Code duplicated, block: B:62:0x0143  */
    public /* synthetic */ void lambda$sendMessage$20(final long j, final int i, boolean z, boolean z2, LongSparseArray longSparseArray, ArrayList arrayList, final ArrayList arrayList2, final MessageObject messageObject, final TLRPC.Peer peer, final TLRPC.TL_messages_forwardMessages tL_messages_forwardMessages, TLObject tLObject, final TLRPC.TL_error tL_error) {
        int i2;
        String str;
        String str2;
        int i3;
        TLRPC.Message message;
        TLRPC.Message message2;
        int i4;
        int i5;
        long j2;
        TLRPC.Updates updates;
        int i6;
        int i7;
        final TLRPC.Message message3;
        int iIndexOf;
        boolean z3;
        final SendMessagesHelper sendMessagesHelper = this;
        int i8 = 1;
        int i9 = 0;
        if (tL_error == null) {
            SparseLongArray sparseLongArray = new SparseLongArray();
            TLRPC.Updates updates2 = (TLRPC.Updates) tLObject;
            int i10 = 0;
            while (i10 < updates2.updates.size()) {
                TLRPC.Update update = updates2.updates.get(i10);
                if (update instanceof TLRPC.TL_updateMessageID) {
                    TLRPC.TL_updateMessageID tL_updateMessageID = (TLRPC.TL_updateMessageID) update;
                    sparseLongArray.put(tL_updateMessageID.id, tL_updateMessageID.random_id);
                    updates2.updates.remove(i10);
                    i10--;
                }
                i10++;
            }
            sendMessagesHelper.getNotificationCenter().postNotificationNameOnUIThread(NotificationCenter.savedMessagesForwarded, sparseLongArray);
            Integer numValueOf = sendMessagesHelper.getMessagesController().dialogs_read_outbox_max.get(Long.valueOf(j));
            if (numValueOf == null) {
                numValueOf = Integer.valueOf(sendMessagesHelper.getMessagesStorage().getDialogReadMax(true, j));
                sendMessagesHelper.getMessagesController().dialogs_read_outbox_max.put(Long.valueOf(j), numValueOf);
            }
            Integer num = numValueOf;
            int i11 = 0;
            int i12 = 0;
            while (i11 < updates2.updates.size()) {
                TLRPC.Update update2 = updates2.updates.get(i11);
                boolean z4 = update2 instanceof TLRPC.TL_updateNewMessage;
                if (z4 || (update2 instanceof TLRPC.TL_updateNewChannelMessage) || (update2 instanceof TLRPC.TL_updateNewScheduledMessage) || (update2 instanceof TLRPC.TL_updateQuickReplyMessage)) {
                    int i13 = i != 0 ? i8 : i9;
                    updates2.updates.remove(i11);
                    int i14 = i11 - 1;
                    if (z4) {
                        TLRPC.TL_updateNewMessage tL_updateNewMessage = (TLRPC.TL_updateNewMessage) update2;
                        message = tL_updateNewMessage.message;
                        i3 = i12;
                        sendMessagesHelper.getMessagesController().processNewDifferenceParams(-1, tL_updateNewMessage.pts, -1, tL_updateNewMessage.pts_count);
                    } else {
                        i3 = i12;
                        if (update2 instanceof TLRPC.TL_updateNewScheduledMessage) {
                            message2 = ((TLRPC.TL_updateNewScheduledMessage) update2).message;
                            i4 = 1;
                        } else if (update2 instanceof TLRPC.TL_updateQuickReplyMessage) {
                            QuickRepliesController.getInstance(sendMessagesHelper.currentAccount).processUpdate(update2, null, 0);
                            message = ((TLRPC.TL_updateQuickReplyMessage) update2).message;
                        } else {
                            TLRPC.TL_updateNewChannelMessage tL_updateNewChannelMessage = (TLRPC.TL_updateNewChannelMessage) update2;
                            message = tL_updateNewChannelMessage.message;
                            sendMessagesHelper.getMessagesController().processNewChannelDifferenceParams(tL_updateNewChannelMessage.pts, tL_updateNewChannelMessage.pts_count, message.peer_id.channel_id);
                        }
                        if (z || message2.date == 2147483646) {
                            i5 = i4;
                        } else {
                            i5 = 0;
                        }
                        ImageLoader.saveMessageThumbs(message2);
                        if (i5 == 0) {
                            if (num.intValue() < message2.id) {
                                z3 = true;
                            } else {
                                z3 = false;
                            }
                            message2.unread = z3;
                        }
                        if (z2) {
                            message2.out = true;
                            message2.unread = false;
                            message2.media_unread = false;
                        }
                        j2 = sparseLongArray.get(message2.id);
                        if (j2 != 0 || (message3 = (TLRPC.Message) longSparseArray.get(j2)) == null || (iIndexOf = arrayList.indexOf(message3)) == -1) {
                            sendMessagesHelper = this;
                            updates = updates2;
                            i11 = i14;
                            i6 = i3;
                        } else {
                            MessageObject messageObject2 = (MessageObject) arrayList2.get(iIndexOf);
                            arrayList.remove(iIndexOf);
                            arrayList2.remove(iIndexOf);
                            final int i15 = message3.id;
                            final ArrayList arrayList3 = new ArrayList();
                            arrayList3.add(message2);
                            TLRPC.Message message4 = messageObject2.messageOwner;
                            message4.post_author = message2.post_author;
                            if ((message2.flags & 33554432) != 0) {
                                message4.ttl_period = message2.ttl_period;
                                message4.flags |= 33554432;
                            }
                            final int i16 = i13;
                            int i17 = i3;
                            TLRPC.Updates updates3 = updates2;
                            updateMediaPaths(messageObject2, message2, message2.id, null, true);
                            final int mediaExistanceFlags = messageObject2.getMediaExistanceFlags();
                            message3.id = message2.id;
                            i6 = i17 + 1;
                            if (i16 != i5) {
                                sendMessagesHelper = this;
                                final int i18 = i5;
                                final TLRPC.Message message5 = message2;
                                AndroidUtilities.runOnUIThread(new Runnable() { // from class: org.telegram.messenger.SendMessagesHelper$$ExternalSyntheticLambda48
                                    @Override // java.lang.Runnable
                                    public final void run() {
                                        this.f$0.lambda$sendMessage$14(arrayList3, i18, i15, message3, i16, message5, messageObject, i);
                                    }
                                });
                                updates = updates3;
                            } else {
                                sendMessagesHelper = this;
                                updates = updates3;
                                final TLRPC.Message message6 = message2;
                                getMessagesStorage().getStorageQueue().postRunnable(new Runnable() { // from class: org.telegram.messenger.SendMessagesHelper$$ExternalSyntheticLambda49
                                    @Override // java.lang.Runnable
                                    public final void run() {
                                        this.f$0.lambda$sendMessage$16(i, message6, message3, peer, i15, arrayList3, j, mediaExistanceFlags);
                                    }
                                });
                            }
                            i11 = i14;
                        }
                        i7 = 1;
                    }
                    message2 = message;
                    i4 = 0;
                    if (z) {
                        i5 = i4;
                    } else {
                        i5 = i4;
                    }
                    ImageLoader.saveMessageThumbs(message2);
                    if (i5 == 0) {
                        if (num.intValue() < message2.id) {
                            z3 = true;
                        } else {
                            z3 = false;
                        }
                        message2.unread = z3;
                    }
                    if (z2) {
                        message2.out = true;
                        message2.unread = false;
                        message2.media_unread = false;
                    }
                    j2 = sparseLongArray.get(message2.id);
                    if (j2 != 0) {
                        sendMessagesHelper = this;
                        updates = updates2;
                        i11 = i14;
                        i6 = i3;
                    } else {
                        sendMessagesHelper = this;
                        updates = updates2;
                        i11 = i14;
                        i6 = i3;
                    }
                    i7 = 1;
                } else {
                    i6 = i12;
                    sparseLongArray = sparseLongArray;
                    i7 = i8;
                    updates = updates2;
                }
                i11 += i7;
                updates2 = updates;
                sparseLongArray = sparseLongArray;
                i12 = i6;
                i9 = 0;
                i8 = i7;
            }
            int i19 = i12;
            i2 = i8;
            TLRPC.Updates updates4 = updates2;
            if (updates4.updates.isEmpty()) {
                i9 = 0;
            } else {
                i9 = 0;
                sendMessagesHelper.getMessagesController().processUpdates(updates4, false);
            }
            sendMessagesHelper.getStatsController().incrementSentItemsCount(ApplicationLoader.getCurrentNetworkType(), i2, i19);
        } else {
            i2 = 1;
            AndroidUtilities.runOnUIThread(new Runnable() { // from class: org.telegram.messenger.SendMessagesHelper$$ExternalSyntheticLambda50
                @Override // java.lang.Runnable
                public final void run() {
                    this.f$0.lambda$sendMessage$17(tL_error, tL_messages_forwardMessages);
                }
            });
        }
        for (int i20 = i9; i20 < arrayList.size(); i20++) {
            final TLRPC.Message message7 = (TLRPC.Message) arrayList.get(i20);
            sendMessagesHelper.getMessagesStorage().markMessageAsSendError(message7, i != 0 ? i2 : i9);
            if (tL_error != null && (str2 = tL_error.text) != null && str2.startsWith("ALLOW_PAYMENT_REQUIRED_")) {
                StarsController.getInstance(sendMessagesHelper.currentAccount);
                message7.errorAllowedPriceStars = StarsController.getAllowedPaidStars(tL_messages_forwardMessages);
                message7.errorNewPriceStars = Long.parseLong(tL_error.text.substring(23)) / ((long) tL_messages_forwardMessages.id.size());
                sendMessagesHelper.getMessagesStorage().updateMessageCustomParams(MessageObject.getDialogId(message7), message7);
            }
            AndroidUtilities.runOnUIThread(new Runnable() { // from class: org.telegram.messenger.SendMessagesHelper$$ExternalSyntheticLambda51
                @Override // java.lang.Runnable
                public final void run() {
                    this.f$0.lambda$sendMessage$18(message7, i);
                }
            });
        }
        if (tL_error == null || (str = tL_error.text) == null || !str.startsWith("ALLOW_PAYMENT_REQUIRED_")) {
            return;
        }
        AndroidUtilities.runOnUIThread(new Runnable() { // from class: org.telegram.messenger.SendMessagesHelper$$ExternalSyntheticLambda52
            @Override // java.lang.Runnable
            public final void run() {
                this.f$0.lambda$sendMessage$19(arrayList2);
            }
        });
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$sendMessage$14(final ArrayList arrayList, final int i, final int i2, final TLRPC.Message message, final int i3, final TLRPC.Message message2, final MessageObject messageObject, final int i4) {
        getMessagesStorage().getStorageQueue().postRunnable(new Runnable() { // from class: org.telegram.messenger.SendMessagesHelper$$ExternalSyntheticLambda104
            @Override // java.lang.Runnable
            public final void run() {
                this.f$0.lambda$sendMessage$13(arrayList, i, i2, message, i3, message2, messageObject, i4);
            }
        });
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$sendMessage$13(ArrayList arrayList, final int i, final int i2, final TLRPC.Message message, final int i3, final TLRPC.Message message2, final MessageObject messageObject, final int i4) {
        getMessagesStorage().putMessages((ArrayList<TLRPC.Message>) arrayList, true, false, false, 0, i, 0L);
        AndroidUtilities.runOnUIThread(new Runnable() { // from class: org.telegram.messenger.SendMessagesHelper$$ExternalSyntheticLambda0
            @Override // java.lang.Runnable
            public final void run() {
                this.f$0.lambda$sendMessage$12(i2, message, i3, i, message2, messageObject, i4);
            }
        });
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$sendMessage$12(int i, TLRPC.Message message, int i2, int i3, TLRPC.Message message2, MessageObject messageObject, int i4) {
        ArrayList<Integer> arrayList = new ArrayList<>();
        arrayList.add(Integer.valueOf(i));
        boolean z = true;
        getMessagesController().deleteMessages(arrayList, null, null, message.dialog_id, false, i2, false, 0L, null, 0, i3 == 1, message2.id);
        ArrayList<MessageObject> arrayList2 = new ArrayList<>();
        arrayList2.add(new MessageObject(messageObject.currentAccount, messageObject.messageOwner, true, true));
        getMessagesController().updateInterfaceWithMessages(message.dialog_id, arrayList2, i3);
        getMediaDataController().increasePeerRaiting(message.dialog_id);
        processSentMessage(i);
        if (i4 == 0) {
            z = false;
        }
        removeFromSendingMessages(i, z);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$sendMessage$16(final int i, final TLRPC.Message message, final TLRPC.Message message2, TLRPC.Peer peer, final int i2, ArrayList arrayList, final long j, final int i3) {
        int i4 = i != 0 ? 1 : 0;
        if (message.quick_reply_shortcut_id != 0 || message.quick_reply_shortcut != null) {
            i4 = 5;
        }
        int i5 = i4;
        getMessagesStorage().updateMessageStateAndId(message2.random_id, MessageObject.getPeerId(peer), Integer.valueOf(i2), message2.id, 0, false, i != 0 ? 1 : 0, message.quick_reply_shortcut_id);
        getMessagesStorage().putMessages((ArrayList<TLRPC.Message>) arrayList, true, false, false, 0, i5, message.quick_reply_shortcut_id);
        AndroidUtilities.runOnUIThread(new Runnable() { // from class: org.telegram.messenger.SendMessagesHelper$$ExternalSyntheticLambda16
            @Override // java.lang.Runnable
            public final void run() {
                this.f$0.lambda$sendMessage$15(message2, j, i2, message, i3, i);
            }
        });
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$sendMessage$15(TLRPC.Message message, long j, int i, TLRPC.Message message2, int i2, int i3) {
        message.send_state = 0;
        getMediaDataController().increasePeerRaiting(j);
        getNotificationCenter().lambda$postNotificationNameOnUIThread$1(NotificationCenter.messageReceivedByServer, Integer.valueOf(i), Integer.valueOf(message2.id), message2, Long.valueOf(j), 0L, Integer.valueOf(i2), Boolean.valueOf(i3 != 0));
        getNotificationCenter().lambda$postNotificationNameOnUIThread$1(NotificationCenter.messageReceivedByServer2, Integer.valueOf(i), Integer.valueOf(message2.id), message2, Long.valueOf(j), 0L, Integer.valueOf(i2), Boolean.valueOf(i3 != 0));
        processSentMessage(i);
        removeFromSendingMessages(i, i3 != 0);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$sendMessage$17(TLRPC.TL_error tL_error, TLRPC.TL_messages_forwardMessages tL_messages_forwardMessages) {
        AlertsCreator.processError(this.currentAccount, tL_error, null, tL_messages_forwardMessages, new Object[0]);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$sendMessage$18(TLRPC.Message message, int i) {
        message.send_state = 2;
        getNotificationCenter().lambda$postNotificationNameOnUIThread$1(NotificationCenter.messageSendError, Integer.valueOf(message.id));
        processSentMessage(message.id);
        removeFromSendingMessages(message.id, i != 0);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$sendMessage$19(ArrayList arrayList) {
        StarsController.getInstance(this.currentAccount).showPriceChangedToast(arrayList);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$sendMessage$22(TLRPC.TL_messages_forwardMessages tL_messages_forwardMessages, ArrayList arrayList, Runnable runnable) {
        if (BotForumHelper.getInstance(this.currentAccount).beforeSendingFinalRequest(tL_messages_forwardMessages, arrayList, runnable)) {
            runnable.run();
        }
    }

    public static int canSendMessageToChat(TLRPC.Chat chat, MessageObject messageObject) {
        boolean zCanSendStickers = ChatObject.canSendStickers(chat);
        boolean zCanSendPhoto = ChatObject.canSendPhoto(chat);
        boolean zCanSendVideo = ChatObject.canSendVideo(chat);
        boolean zCanSendDocument = ChatObject.canSendDocument(chat);
        ChatObject.canSendEmbed(chat);
        boolean zCanSendPolls = ChatObject.canSendPolls(chat);
        boolean zCanSendRoundVideo = ChatObject.canSendRoundVideo(chat);
        boolean zCanSendVoice = ChatObject.canSendVoice(chat);
        boolean zCanSendMusic = ChatObject.canSendMusic(chat);
        boolean z = messageObject.isSticker() || messageObject.isAnimatedSticker() || messageObject.isGif() || messageObject.isGame();
        if (!zCanSendStickers && z) {
            return ChatObject.isActionBannedByDefault(chat, 8) ? 4 : 1;
        }
        if (!zCanSendPhoto && (messageObject.messageOwner.media instanceof TLRPC.TL_messageMediaPhoto) && !messageObject.isVideo() && !z) {
            return ChatObject.isActionBannedByDefault(chat, 16) ? 10 : 12;
        }
        if (!zCanSendMusic && messageObject.isMusic()) {
            return ChatObject.isActionBannedByDefault(chat, 18) ? 19 : 20;
        }
        if (!zCanSendVideo && messageObject.isVideo() && !z) {
            return ChatObject.isActionBannedByDefault(chat, 17) ? 9 : 11;
        }
        if (!zCanSendPolls && (messageObject.messageOwner.media instanceof TLRPC.TL_messageMediaPoll)) {
            return ChatObject.isActionBannedByDefault(chat, 10) ? 6 : 3;
        }
        if (!zCanSendPolls && (messageObject.messageOwner.media instanceof TLRPC.TL_messageMediaToDo)) {
            return ChatObject.isActionBannedByDefault(chat, 10) ? 21 : 22;
        }
        if (!zCanSendVoice && MessageObject.isVoiceMessage(messageObject.messageOwner)) {
            return ChatObject.isActionBannedByDefault(chat, 20) ? 13 : 14;
        }
        if (!zCanSendRoundVideo && MessageObject.isRoundVideoMessage(messageObject.messageOwner)) {
            return ChatObject.isActionBannedByDefault(chat, 21) ? 15 : 16;
        }
        if (zCanSendDocument || !(messageObject.messageOwner.media instanceof TLRPC.TL_messageMediaDocument) || z) {
            return 0;
        }
        return ChatObject.isActionBannedByDefault(chat, 19) ? 17 : 18;
    }

    private void writePreviousMessageData(TLRPC.Message message, SerializedData serializedData) {
        TLRPC.MessageMedia messageMedia = message.media;
        if (messageMedia == null) {
            new TLRPC.TL_messageMediaEmpty().serializeToStream(serializedData);
        } else {
            messageMedia.serializeToStream(serializedData);
        }
        String str = message.message;
        String str2 = _UrlKt.FRAGMENT_ENCODE_SET;
        if (str == null) {
            str = _UrlKt.FRAGMENT_ENCODE_SET;
        }
        serializedData.writeString(str);
        String str3 = message.attachPath;
        if (str3 != null) {
            str2 = str3;
        }
        serializedData.writeString(str2);
        int size = message.entities.size();
        serializedData.writeInt32(size);
        for (int i = 0; i < size; i++) {
            ((TLRPC.MessageEntity) message.entities.get(i)).serializeToStream(serializedData);
        }
    }

    /* JADX WARN: Multi-variable type inference failed */
    /* JADX WARN: Type inference failed for: r32v0, types: [org.telegram.messenger.BaseController, org.telegram.messenger.SendMessagesHelper] */
    /* JADX WARN: Type inference failed for: r36v21 */
    /* JADX WARN: Type inference failed for: r36v22 */
    /* JADX WARN: Type inference failed for: r36v23 */
    /* JADX WARN: Type inference failed for: r36v24 */
    /* JADX WARN: Type inference failed for: r36v7 */
    /* JADX WARN: Type inference failed for: r3v1 */
    /* JADX WARN: Type inference failed for: r6v16 */
    public void editMessage(MessageObject messageObject, TLRPC.TL_photo tL_photo, VideoEditedInfo videoEditedInfo, TLRPC.TL_document tL_document, String str, TLRPC.PhotoSize photoSize, HashMap<String, String> map, boolean z, boolean z2, Object obj) {
        char c;
        String str2;
        byte b;
        HashMap<String, String> map2;
        VideoEditedInfo videoEditedInfo2;
        Object obj2;
        VideoEditedInfo videoEditedInfo3;
        char c2;
        long j;
        TLRPC.InputMedia inputMedia;
        boolean z3;
        TLRPC.InputMedia inputMedia2;
        DelayedMessage delayedMessage;
        boolean z4;
        boolean z5;
        VideoEditedInfo videoEditedInfo4;
        boolean z6;
        TLRPC.InputMedia inputMedia3;
        String str3;
        char c3;
        TLRPC.InputMedia inputMedia4;
        boolean z7;
        TLRPC.InputMedia inputMedia5;
        String str4;
        char c4;
        TLRPC.InputMedia inputMedia6;
        ?? r36;
        int i;
        char c5;
        char c6;
        TLRPC.InputMedia tL_inputMediaEmpty;
        TLRPC.EncryptedChat encryptedChat;
        TLRPC.TL_photo tL_photo2 = tL_photo;
        TLRPC.TL_document tL_document2 = tL_document;
        if (messageObject == null) {
            return;
        }
        HashMap<String, String> map3 = map == null ? new HashMap<>() : map;
        LocaleUtils.replaceCustomEmojis(this.currentAccount, messageObject.getDialogId(), messageObject.editingMessageEntities);
        TLRPC.Message message = messageObject.messageOwner;
        messageObject.cancelEditing = false;
        try {
            long dialogId = messageObject.getDialogId();
            boolean z8 = !DialogObject.isEncryptedDialog(dialogId) || ((encryptedChat = getMessagesController().getEncryptedChat(Integer.valueOf(DialogObject.getEncryptedChatId(dialogId)))) != null && AndroidUtilities.getPeerLayerVersion(encryptedChat.layer) >= 101);
            if (z) {
                TLRPC.MessageMedia messageMedia = messageObject.messageOwner.media;
                if ((messageMedia instanceof TLRPC.TL_messageMediaWebPage) || messageMedia == null || (messageMedia instanceof TLRPC.TL_messageMediaEmpty)) {
                    videoEditedInfo2 = videoEditedInfo;
                    b = 1;
                } else if (messageMedia instanceof TLRPC.TL_messageMediaPhoto) {
                    tL_photo2 = (TLRPC.TL_photo) messageMedia.photo;
                    videoEditedInfo2 = videoEditedInfo;
                    b = 2;
                } else if (messageMedia instanceof TLRPC.TL_messageMediaDocument) {
                    tL_document2 = (TLRPC.TL_document) messageMedia.document;
                    b = (MessageObject.isVideoDocument(tL_document2) || videoEditedInfo != null) ? (byte) 3 : (byte) 7;
                    videoEditedInfo2 = messageObject.videoEditedInfo;
                } else {
                    videoEditedInfo2 = videoEditedInfo;
                    b = messageMedia instanceof TLRPC.TL_messageMediaToDo ? (byte) 10 : (byte) -1;
                }
                map2 = message.params;
                obj2 = (obj == null && map2 != null && map2.containsKey("parentObject")) ? map2.get("parentObject") : obj;
                messageObject.editingMessage = message.message;
                messageObject.editingMessageEntities = message.entities;
                str2 = message.attachPath;
            } else {
                TLRPC.MessageMedia messageMedia2 = message.media;
                messageObject.previousMedia = messageMedia2;
                messageObject.previousMessage = message.message;
                messageObject.previousMessageEntities = message.entities;
                messageObject.previousAttachPath = message.attachPath;
                if (messageMedia2 == null) {
                    new TLRPC.TL_messageMediaEmpty();
                }
                SerializedData serializedData = new SerializedData(true);
                writePreviousMessageData(message, serializedData);
                SerializedData serializedData2 = new SerializedData(serializedData.length());
                writePreviousMessageData(message, serializedData2);
                map3.put("prevMedia", Base64.encodeToString(serializedData2.toByteArray(), 0));
                serializedData2.cleanup();
                if (tL_photo2 != null) {
                    TLRPC.TL_messageMediaPhoto tL_messageMediaPhoto = new TLRPC.TL_messageMediaPhoto();
                    message.media = tL_messageMediaPhoto;
                    tL_messageMediaPhoto.flags |= 3;
                    tL_messageMediaPhoto.photo = tL_photo2;
                    tL_messageMediaPhoto.spoiler = z2;
                    if (str != null && str.length() > 0 && str.startsWith("http")) {
                        message.attachPath = str;
                    } else {
                        ArrayList arrayList = tL_photo2.sizes;
                        message.attachPath = FileLoader.getInstance(this.currentAccount).getPathToAttach(((TLRPC.PhotoSize) arrayList.get(arrayList.size() - 1)).location, true).toString();
                    }
                    c = 2;
                } else if (tL_document2 != null) {
                    TLRPC.TL_messageMediaDocument tL_messageMediaDocument = new TLRPC.TL_messageMediaDocument();
                    message.media = tL_messageMediaDocument;
                    tL_messageMediaDocument.flags |= 3;
                    tL_messageMediaDocument.document = tL_document2;
                    tL_messageMediaDocument.spoiler = z2;
                    c = (MessageObject.isVideoDocument(tL_document2) || videoEditedInfo != null) ? (char) 3 : (char) 7;
                    if (videoEditedInfo != null) {
                        map3.put("ve", videoEditedInfo.getString());
                    }
                    message.attachPath = str;
                    if (photoSize instanceof ImageLoader.PhotoSizeFromPhoto) {
                        TLRPC.MessageMedia messageMedia3 = message.media;
                        messageMedia3.flags |= 512;
                        messageMedia3.video_cover = ((ImageLoader.PhotoSizeFromPhoto) photoSize).photo;
                    } else if (photoSize != null) {
                        TLRPC.TL_photo tL_photo3 = new TLRPC.TL_photo();
                        tL_photo3.date = getConnectionsManager().getCurrentTime();
                        tL_photo3.sizes.add(photoSize);
                        tL_photo3.file_reference = new byte[0];
                        TLRPC.MessageMedia messageMedia4 = message.media;
                        messageMedia4.video_cover = tL_photo3;
                        messageMedia4.flags |= 512;
                    }
                } else {
                    c = messageObject.messageOwner.media instanceof TLRPC.TL_messageMediaToDo ? '\n' : (char) 1;
                }
                message.params = map3;
                message.send_state = 3;
                message.errorNewPriceStars = 0L;
                message.errorAllowedPriceStars = 0L;
                str2 = str;
                b = c;
                map2 = map3;
                videoEditedInfo2 = videoEditedInfo;
                obj2 = obj;
            }
            if (message.attachPath == null) {
                message.attachPath = _UrlKt.FRAGMENT_ENCODE_SET;
            }
            message.local_id = 0;
            int i2 = messageObject.type;
            VideoEditedInfo videoEditedInfo5 = videoEditedInfo2;
            if ((i2 == 3 || videoEditedInfo5 != null || i2 == 2) && !TextUtils.isEmpty(message.attachPath)) {
                messageObject.attachPathExists = true;
            }
            VideoEditedInfo videoEditedInfo6 = messageObject.videoEditedInfo;
            if (videoEditedInfo6 == null || videoEditedInfo5 != null) {
                videoEditedInfo6 = videoEditedInfo5;
            }
            if (z) {
                videoEditedInfo3 = videoEditedInfo6;
            } else {
                CharSequence charSequence = messageObject.editingMessage;
                if (charSequence != null) {
                    String str5 = message.message;
                    String string = charSequence.toString();
                    message.message = string;
                    videoEditedInfo3 = videoEditedInfo6;
                    messageObject.caption = null;
                    if (b == 1) {
                        ArrayList<TLRPC.MessageEntity> arrayList2 = messageObject.editingMessageEntities;
                        if (arrayList2 != null) {
                            message.entities = arrayList2;
                            message.flags |= 128;
                        } else if (!TextUtils.equals(str5, string)) {
                            message.flags &= -129;
                        }
                        TLRPC.Message message2 = messageObject.messageOwner;
                        if (message2 != null && (message2.media instanceof TLRPC.TL_messageMediaPaidMedia)) {
                            messageObject.generateCaption();
                        }
                    } else {
                        ArrayList<TLRPC.MessageEntity> arrayList3 = messageObject.editingMessageEntities;
                        if (arrayList3 != null) {
                            message.entities = arrayList3;
                            message.flags |= 128;
                        } else {
                            ArrayList<TLRPC.MessageEntity> entities = getMediaDataController().getEntities(new CharSequence[]{messageObject.editingMessage}, z8);
                            if (entities != null && !entities.isEmpty()) {
                                message.entities = entities;
                                message.flags |= 128;
                            } else if (!TextUtils.equals(str5, message.message)) {
                                message.flags &= -129;
                            }
                        }
                        messageObject.generateCaption();
                    }
                } else {
                    videoEditedInfo3 = videoEditedInfo6;
                }
                ArrayList<TLRPC.Message> arrayList4 = new ArrayList<>();
                arrayList4.add(message);
                getMessagesStorage().putMessages(arrayList4, false, true, false, 0, messageObject.scheduled ? 1 : 0, 0L);
                getMessagesController().getTopicsController().processEditedMessage(message);
                messageObject.type = -1;
                messageObject.setType();
                if (b == 1) {
                    TLRPC.MessageMedia messageMedia5 = messageObject.messageOwner.media;
                    if ((messageMedia5 instanceof TLRPC.TL_messageMediaPhoto) || (messageMedia5 instanceof TLRPC.TL_messageMediaDocument)) {
                        messageObject.generateCaption();
                    } else {
                        messageObject.resetLayout();
                        messageObject.checkLayout();
                    }
                }
                messageObject.createMessageSendInfo();
                ArrayList arrayList5 = new ArrayList();
                arrayList5.add(messageObject);
                getNotificationCenter().lambda$postNotificationNameOnUIThread$1(NotificationCenter.replaceMessagesObjects, Long.valueOf(dialogId), arrayList5);
            }
            String str6 = (map2 == null || !map2.containsKey("originalPath")) ? null : map2.get("originalPath");
            char c7 = '\b';
            if (b < 1 || b > 3) {
                if ((b < 5 || b > 8) && b != 10) {
                    return;
                }
            }
            if (b == 1) {
                TLRPC.MessageMedia messageMedia6 = message.media;
                if ((messageMedia6 == null || (messageMedia6 instanceof TLRPC.TL_messageMediaEmpty) || (messageMedia6 != null && (messageMedia6.webpage instanceof TLRPC.TL_webPageEmpty))) && !messageObject.editingMessageSearchWebPage) {
                    tL_inputMediaEmpty = new TLRPC.TL_inputMediaEmpty();
                } else if (messageMedia6 == null || messageMedia6.webpage == null) {
                    c4 = '\b';
                    j = dialogId;
                    c4 = c2;
                    c4 = c2;
                    c4 = c2;
                    z5 = false;
                    inputMedia6 = null;
                    c5 = c4;
                    delayedMessage = null;
                    z4 = false;
                    inputMedia5 = inputMedia6;
                    r36 = c5;
                } else {
                    TLRPC.TL_inputMediaWebPage tL_inputMediaWebPage = new TLRPC.TL_inputMediaWebPage();
                    TLRPC.MessageMedia messageMedia7 = message.media;
                    tL_inputMediaWebPage.url = messageMedia7.webpage.url;
                    tL_inputMediaWebPage.force_small_media = messageMedia7.force_small_media;
                    tL_inputMediaWebPage.force_large_media = messageMedia7.force_large_media;
                    tL_inputMediaEmpty = tL_inputMediaWebPage;
                }
                inputMedia = tL_inputMediaEmpty;
                c6 = '\b';
                j = dialogId;
                z5 = false;
                inputMedia6 = inputMedia;
                c5 = c6;
                delayedMessage = null;
                z4 = false;
                inputMedia5 = inputMedia6;
                r36 = c5;
            } else if (b == 2) {
                TLRPC.TL_inputMediaUploadedPhoto tL_inputMediaUploadedPhoto = new TLRPC.TL_inputMediaUploadedPhoto();
                tL_inputMediaUploadedPhoto.spoiler = z2;
                if (map2 == null || (str4 = map2.get("masks")) == null) {
                    c3 = '\b';
                } else {
                    SerializedData serializedData3 = new SerializedData(Utilities.hexToBytes(str4));
                    boolean z9 = false;
                    int int32 = serializedData3.readInt32(false);
                    int i3 = 0;
                    while (i3 < int32) {
                        tL_inputMediaUploadedPhoto.stickers.add(TLRPC.InputDocument.TLdeserialize(serializedData3, serializedData3.readInt32(z9), z9));
                        i3++;
                        int32 = int32;
                        c7 = c7;
                        z9 = false;
                    }
                    c3 = c7;
                    tL_inputMediaUploadedPhoto.flags |= 1;
                    serializedData3.cleanup();
                }
                if (tL_photo2.access_hash == 0) {
                    inputMedia4 = tL_inputMediaUploadedPhoto;
                    z7 = true;
                } else {
                    TLRPC.TL_inputMediaPhoto tL_inputMediaPhoto = new TLRPC.TL_inputMediaPhoto();
                    TLRPC.TL_inputPhoto tL_inputPhoto = new TLRPC.TL_inputPhoto();
                    tL_inputMediaPhoto.id = tL_inputPhoto;
                    tL_inputPhoto.id = tL_photo2.id;
                    tL_inputPhoto.access_hash = tL_photo2.access_hash;
                    byte[] bArr = tL_photo2.file_reference;
                    tL_inputPhoto.file_reference = bArr;
                    if (bArr == null) {
                        tL_inputPhoto.file_reference = new byte[0];
                    }
                    tL_inputMediaPhoto.spoiler = z2;
                    inputMedia4 = tL_inputMediaPhoto;
                    z7 = false;
                }
                j = dialogId;
                DelayedMessage delayedMessage2 = new DelayedMessage(j);
                delayedMessage2.type = 0;
                delayedMessage2.obj = messageObject;
                delayedMessage2.originalPath = str6;
                delayedMessage2.parentObject = obj2;
                delayedMessage2.inputUploadMedia = tL_inputMediaUploadedPhoto;
                delayedMessage2.performMediaUpload = z7;
                if (str2 != null && str2.length() > 0 && str2.startsWith("http")) {
                    delayedMessage2.httpLocation = str2;
                } else {
                    ArrayList arrayList6 = tL_photo2.sizes;
                    delayedMessage2.photoSize = (TLRPC.PhotoSize) arrayList6.get(arrayList6.size() - 1);
                    delayedMessage2.locationParent = tL_photo2;
                }
                z4 = z7;
                delayedMessage = delayedMessage2;
                z5 = false;
                inputMedia5 = inputMedia4;
                r36 = c3;
            } else {
                c2 = '\b';
                j = dialogId;
                if (b == 3) {
                    TLRPC.TL_inputMediaUploadedDocument tL_inputMediaUploadedDocument = new TLRPC.TL_inputMediaUploadedDocument();
                    tL_inputMediaUploadedDocument.spoiler = z2;
                    if (map2 != null && (str3 = map2.get("masks")) != null) {
                        SerializedData serializedData4 = new SerializedData(Utilities.hexToBytes(str3));
                        boolean z10 = false;
                        int int33 = serializedData4.readInt32(false);
                        int i4 = 0;
                        while (i4 < int33) {
                            tL_inputMediaUploadedDocument.stickers.add(TLRPC.InputDocument.TLdeserialize(serializedData4, serializedData4.readInt32(z10), z10));
                            i4++;
                            int33 = int33;
                            z10 = false;
                        }
                        tL_inputMediaUploadedDocument.flags |= 1;
                        serializedData4.cleanup();
                    }
                    tL_inputMediaUploadedDocument.mime_type = tL_document2.mime_type;
                    tL_inputMediaUploadedDocument.attributes = tL_document2.attributes;
                    if (messageObject.isGif()) {
                        videoEditedInfo4 = videoEditedInfo3;
                    } else {
                        if (videoEditedInfo3 != null) {
                            videoEditedInfo4 = videoEditedInfo3;
                            if (!videoEditedInfo4.muted) {
                            }
                        } else {
                            videoEditedInfo4 = videoEditedInfo3;
                        }
                        tL_inputMediaUploadedDocument.nosound_video = true;
                        if (BuildVars.DEBUG_VERSION) {
                            FileLog.d("nosound_video = true");
                        }
                    }
                    if (tL_document2.access_hash == 0) {
                        inputMedia3 = tL_inputMediaUploadedDocument;
                        z6 = true;
                    } else {
                        TLRPC.TL_inputMediaDocument tL_inputMediaDocument = new TLRPC.TL_inputMediaDocument();
                        TLRPC.TL_inputDocument tL_inputDocument = new TLRPC.TL_inputDocument();
                        tL_inputMediaDocument.id = tL_inputDocument;
                        tL_inputDocument.id = tL_document2.id;
                        tL_inputDocument.access_hash = tL_document2.access_hash;
                        byte[] bArr2 = tL_document2.file_reference;
                        tL_inputDocument.file_reference = bArr2;
                        if (bArr2 == null) {
                            tL_inputDocument.file_reference = new byte[0];
                        }
                        tL_inputMediaDocument.spoiler = z2;
                        z6 = false;
                        inputMedia3 = tL_inputMediaDocument;
                    }
                    delayedMessage = new DelayedMessage(j);
                    delayedMessage.type = 1;
                    delayedMessage.obj = messageObject;
                    delayedMessage.originalPath = str6;
                    delayedMessage.parentObject = obj2;
                    delayedMessage.inputUploadMedia = tL_inputMediaUploadedDocument;
                    if (!tL_document2.thumbs.isEmpty()) {
                        TLRPC.PhotoSize photoSize2 = tL_document2.thumbs.get(0);
                        if (!(photoSize2 instanceof TLRPC.TL_photoStrippedSize)) {
                            delayedMessage.photoSize = photoSize2;
                            delayedMessage.locationParent = tL_document2;
                        }
                    }
                    delayedMessage.videoEditedInfo = videoEditedInfo4;
                    if (photoSize instanceof ImageLoader.PhotoSizeFromPhoto) {
                        tL_inputMediaUploadedDocument.video_cover = ((ImageLoader.PhotoSizeFromPhoto) photoSize).inputPhoto;
                        tL_inputMediaUploadedDocument.flags |= 64;
                    } else {
                        if (photoSize != null && !(photoSize instanceof TLRPC.TL_photoStrippedSize)) {
                            delayedMessage.coverPhotoSize = photoSize;
                            z5 = true;
                        }
                        delayedMessage.performMediaUpload = z6;
                        delayedMessage.performCoverUpload = z5;
                        z4 = z6;
                        inputMedia5 = inputMedia3;
                        r36 = c2;
                    }
                    z5 = false;
                    delayedMessage.performMediaUpload = z6;
                    delayedMessage.performCoverUpload = z5;
                    z4 = z6;
                    inputMedia5 = inputMedia3;
                    r36 = c2;
                } else {
                    VideoEditedInfo videoEditedInfo7 = videoEditedInfo3;
                    if (b == 7) {
                        TLRPC.TL_inputMediaUploadedDocument tL_inputMediaUploadedDocument2 = new TLRPC.TL_inputMediaUploadedDocument();
                        tL_inputMediaUploadedDocument2.mime_type = tL_document2.mime_type;
                        tL_inputMediaUploadedDocument2.attributes = tL_document2.attributes;
                        tL_inputMediaUploadedDocument2.spoiler = z2;
                        if (tL_document2.access_hash == 0) {
                            inputMedia2 = tL_inputMediaUploadedDocument2;
                            z3 = true;
                        } else {
                            TLRPC.TL_inputMediaDocument tL_inputMediaDocument2 = new TLRPC.TL_inputMediaDocument();
                            TLRPC.TL_inputDocument tL_inputDocument2 = new TLRPC.TL_inputDocument();
                            tL_inputMediaDocument2.id = tL_inputDocument2;
                            tL_inputDocument2.id = tL_document2.id;
                            tL_inputDocument2.access_hash = tL_document2.access_hash;
                            byte[] bArr3 = tL_document2.file_reference;
                            tL_inputDocument2.file_reference = bArr3;
                            if (bArr3 == null) {
                                tL_inputDocument2.file_reference = new byte[0];
                            }
                            tL_inputMediaDocument2.spoiler = z2;
                            z3 = false;
                            inputMedia2 = tL_inputMediaDocument2;
                        }
                        delayedMessage = new DelayedMessage(j);
                        delayedMessage.originalPath = str6;
                        delayedMessage.type = 2;
                        delayedMessage.obj = messageObject;
                        if (!tL_document2.thumbs.isEmpty() && (videoEditedInfo7 == null || !videoEditedInfo7.isSticker)) {
                            TLRPC.PhotoSize photoSize3 = tL_document2.thumbs.get(0);
                            if (!(photoSize3 instanceof TLRPC.TL_photoStrippedSize)) {
                                delayedMessage.photoSize = photoSize3;
                                delayedMessage.locationParent = tL_document2;
                            }
                        }
                        delayedMessage.parentObject = obj2;
                        delayedMessage.inputUploadMedia = tL_inputMediaUploadedDocument2;
                        delayedMessage.performMediaUpload = z3;
                        z4 = z3;
                        z5 = false;
                        inputMedia5 = inputMedia2;
                        r36 = c2;
                    } else {
                        if (b == 10) {
                            TLRPC.MessageMedia media = MessageObject.getMedia(messageObject.messageOwner);
                            if (media instanceof TLRPC.TL_messageMediaToDo) {
                                c4 = c2;
                                c4 = c2;
                                TLRPC.TL_inputMediaTodo tL_inputMediaTodo = new TLRPC.TL_inputMediaTodo();
                                tL_inputMediaTodo.todo = ((TLRPC.TL_messageMediaToDo) media).todo;
                                inputMedia = tL_inputMediaTodo;
                                c6 = c2;
                                z5 = false;
                                inputMedia6 = inputMedia;
                                c5 = c6;
                            }
                            delayedMessage = null;
                            z4 = false;
                            inputMedia5 = inputMedia6;
                            r36 = c5;
                        }
                        c4 = c2;
                        c4 = c2;
                        c4 = c2;
                        z5 = false;
                        inputMedia6 = null;
                        c5 = c4;
                        delayedMessage = null;
                        z4 = false;
                        inputMedia5 = inputMedia6;
                        r36 = c5;
                    }
                }
            }
            boolean z11 = inputMedia5 instanceof TLRPC.TL_inputMediaEmpty;
            TLRPC.InputMedia inputMedia7 = inputMedia5;
            if (z11 && ((i = messageObject.type) == 0 || i == 19)) {
                inputMedia7 = inputMedia5;
                inputMedia7 = null;
            }
            inputMedia7 = inputMedia5;
            TLRPC.TL_messages_editMessage tL_messages_editMessage = new TLRPC.TL_messages_editMessage();
            tL_messages_editMessage.id = messageObject.getId();
            tL_messages_editMessage.peer = getMessagesController().getInputPeer(j);
            TLRPC.Message message3 = messageObject.messageOwner;
            tL_messages_editMessage.invert_media = message3.invert_media;
            if (inputMedia7 != null) {
                tL_messages_editMessage.flags |= 16384;
                tL_messages_editMessage.media = inputMedia7;
            } else if (!messageObject.editingMessageSearchWebPage) {
                tL_messages_editMessage.no_webpage = true;
            }
            if (messageObject.scheduled) {
                tL_messages_editMessage.schedule_date = message3.date;
                int i5 = tL_messages_editMessage.flags;
                tL_messages_editMessage.flags = 32768 | i5;
                int i6 = message3.schedule_repeat_period;
                if (i6 != 0) {
                    tL_messages_editMessage.schedule_repeat_period = i6;
                    tL_messages_editMessage.flags = i5 | 294912;
                }
            }
            if ((message3.flags & TLObject.FLAG_30) != 0) {
                tL_messages_editMessage.quick_reply_shortcut_id = message3.quick_reply_shortcut_id;
                tL_messages_editMessage.flags |= 131072;
            }
            CharSequence charSequence2 = messageObject.editingMessage;
            if (charSequence2 != null) {
                tL_messages_editMessage.message = charSequence2.toString();
                int i7 = tL_messages_editMessage.flags;
                tL_messages_editMessage.flags = i7 | 2048;
                tL_messages_editMessage.no_webpage = !messageObject.editingMessageSearchWebPage;
                ArrayList<TLRPC.MessageEntity> arrayList7 = messageObject.editingMessageEntities;
                if (arrayList7 != null) {
                    tL_messages_editMessage.entities = arrayList7;
                    tL_messages_editMessage.flags = i7 | 2056;
                } else {
                    ArrayList<TLRPC.MessageEntity> entities2 = getMediaDataController().getEntities(new CharSequence[]{messageObject.editingMessage}, z8);
                    if (entities2 != null && !entities2.isEmpty()) {
                        tL_messages_editMessage.entities = entities2;
                        tL_messages_editMessage.flags |= 8;
                    }
                }
                messageObject.editingMessage = null;
                messageObject.editingMessageEntities = null;
            }
            if (delayedMessage != null) {
                delayedMessage.sendRequest = tL_messages_editMessage;
            }
            try {
                if (b == 1) {
                    performSendMessageRequest(tL_messages_editMessage, messageObject, null, delayedMessage, obj2, map2, messageObject.scheduled);
                    return;
                }
                DelayedMessage delayedMessage3 = delayedMessage;
                try {
                    if (b == 2) {
                        if (z4 || z5) {
                            performSendDelayedMessage(delayedMessage3);
                            return;
                        } else {
                            lambda$performSendMessageRequest$71(tL_messages_editMessage, messageObject, str6, null, true, delayedMessage3, obj2, map2, messageObject.scheduled);
                            return;
                        }
                    }
                    String str7 = str6;
                    HashMap<String, String> map4 = map2;
                    try {
                        if (b == 3) {
                            if (!z4 && !z5) {
                                performSendMessageRequest(tL_messages_editMessage, messageObject, str7, delayedMessage3, obj2, map4, messageObject.scheduled);
                                return;
                            }
                            performSendDelayedMessage(delayedMessage3);
                            return;
                        }
                        if (b == 6) {
                            performSendMessageRequest(tL_messages_editMessage, messageObject, str7, delayedMessage3, obj2, map4, messageObject.scheduled);
                            return;
                        }
                        if (b == 7) {
                            if (z4 || z5) {
                                performSendDelayedMessage(delayedMessage3);
                                return;
                            } else {
                                performSendMessageRequest(tL_messages_editMessage, messageObject, str7, delayedMessage3, obj2, map4, messageObject.scheduled);
                                return;
                            }
                        }
                        if (b != r36) {
                            if (b == 10) {
                                performSendMessageRequest(tL_messages_editMessage, messageObject, str7, delayedMessage3, obj2, map4, messageObject.scheduled);
                            }
                        } else {
                            if (!z4 && !z5) {
                                performSendMessageRequest(tL_messages_editMessage, messageObject, str7, delayedMessage3, obj2, map4, messageObject.scheduled);
                                return;
                            }
                            performSendDelayedMessage(delayedMessage3);
                        }
                    } catch (Exception e) {
                        e = e;
                        FileLog.e(e);
                        revertEditingMessageObject(messageObject);
                    }
                } catch (Exception e2) {
                    e = e2;
                    FileLog.e(e);
                    revertEditingMessageObject(messageObject);
                }
            } catch (Exception e3) {
                e = e3;
            }
        } catch (Exception e4) {
            e = e4;
        }
    }

    public int editMessage(MessageObject messageObject, String str, boolean z, final BaseFragment baseFragment, ArrayList<TLRPC.MessageEntity> arrayList, int i, int i2) {
        if (baseFragment == null || baseFragment.getParentActivity() == null) {
            return 0;
        }
        LocaleUtils.replaceCustomEmojis(this.currentAccount, messageObject.getDialogId(), arrayList);
        final TLRPC.TL_messages_editMessage tL_messages_editMessage = new TLRPC.TL_messages_editMessage();
        tL_messages_editMessage.peer = getMessagesController().getInputPeer(messageObject.getDialogId());
        if (str != null) {
            tL_messages_editMessage.message = str;
            tL_messages_editMessage.flags |= 2048;
            tL_messages_editMessage.no_webpage = !z;
        }
        tL_messages_editMessage.id = messageObject.getId();
        TLRPC.Message message = messageObject.messageOwner;
        if (message != null && (message.flags & TLObject.FLAG_30) != 0) {
            tL_messages_editMessage.quick_reply_shortcut_id = message.quick_reply_shortcut_id;
            tL_messages_editMessage.flags |= 131072;
        }
        if (arrayList != null) {
            tL_messages_editMessage.entities = arrayList;
            tL_messages_editMessage.flags |= 8;
        }
        if (i != 0) {
            tL_messages_editMessage.schedule_date = i;
            int i3 = tL_messages_editMessage.flags;
            tL_messages_editMessage.flags = 32768 | i3;
            if (i2 != 0) {
                tL_messages_editMessage.schedule_repeat_period = i2;
                tL_messages_editMessage.flags = i3 | 294912;
            }
        }
        return getConnectionsManager().sendRequest(tL_messages_editMessage, new RequestDelegate() { // from class: org.telegram.messenger.SendMessagesHelper$$ExternalSyntheticLambda54
            @Override // org.telegram.tgnet.RequestDelegate
            public final void run(TLObject tLObject, TLRPC.TL_error tL_error) {
                this.f$0.lambda$editMessage$24(baseFragment, tL_messages_editMessage, tLObject, tL_error);
            }
        });
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$editMessage$24(final BaseFragment baseFragment, final TLRPC.TL_messages_editMessage tL_messages_editMessage, TLObject tLObject, final TLRPC.TL_error tL_error) {
        if (tL_error == null) {
            getMessagesController().processUpdates((TLRPC.Updates) tLObject, false);
        } else {
            AndroidUtilities.runOnUIThread(new Runnable() { // from class: org.telegram.messenger.SendMessagesHelper$$ExternalSyntheticLambda22
                @Override // java.lang.Runnable
                public final void run() {
                    this.f$0.lambda$editMessage$23(tL_error, baseFragment, tL_messages_editMessage);
                }
            });
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$editMessage$23(TLRPC.TL_error tL_error, BaseFragment baseFragment, TLRPC.TL_messages_editMessage tL_messages_editMessage) {
        AlertsCreator.processError(this.currentAccount, tL_error, baseFragment, tL_messages_editMessage, new Object[0]);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void sendLocation(Location location) {
        TLRPC.TL_messageMediaGeo tL_messageMediaGeo = new TLRPC.TL_messageMediaGeo();
        TLRPC.TL_geoPoint tL_geoPoint = new TLRPC.TL_geoPoint();
        tL_messageMediaGeo.geo = tL_geoPoint;
        tL_geoPoint.lat = AndroidUtilities.fixLocationCoord(location.getLatitude());
        tL_messageMediaGeo.geo._long = AndroidUtilities.fixLocationCoord(location.getLongitude());
        Iterator<Map.Entry<String, MessageObject>> it = this.waitingForLocation.entrySet().iterator();
        while (it.hasNext()) {
            MessageObject value = it.next().getValue();
            sendMessage(SendMessageParams.of((TLRPC.MessageMedia) tL_messageMediaGeo, value.getDialogId(), value, (MessageObject) null, (TLRPC.ReplyMarkup) null, (HashMap<String, String>) null, true, 0, 0));
        }
    }

    public void sendCurrentLocation(MessageObject messageObject, TLRPC.KeyboardButton keyboardButton) {
        if (messageObject == null || keyboardButton == null) {
            return;
        }
        StringBuilder sb = new StringBuilder();
        sb.append(messageObject.getDialogId());
        sb.append("_");
        sb.append(messageObject.getId());
        sb.append("_");
        sb.append(Utilities.bytesToHex(keyboardButton.data));
        sb.append("_");
        sb.append(keyboardButton instanceof TLRPC.TL_keyboardButtonGame ? "1" : MVEL.VERSION_SUB);
        this.waitingForLocation.put(sb.toString(), messageObject);
        this.locationProvider.start();
    }

    public boolean isSendingCurrentLocation(MessageObject messageObject, TLRPC.KeyboardButton keyboardButton) {
        if (messageObject == null || keyboardButton == null) {
            return false;
        }
        StringBuilder sb = new StringBuilder();
        sb.append(messageObject.getDialogId());
        sb.append("_");
        sb.append(messageObject.getId());
        sb.append("_");
        sb.append(Utilities.bytesToHex(keyboardButton.data));
        sb.append("_");
        sb.append(keyboardButton instanceof TLRPC.TL_keyboardButtonGame ? "1" : MVEL.VERSION_SUB);
        return this.waitingForLocation.containsKey(sb.toString());
    }

    public void sendNotificationCallback(final long j, final int i, final byte[] bArr) {
        AndroidUtilities.runOnUIThread(new Runnable() { // from class: org.telegram.messenger.SendMessagesHelper$$ExternalSyntheticLambda29
            @Override // java.lang.Runnable
            public final void run() {
                this.f$0.lambda$sendNotificationCallback$27(j, i, bArr);
            }
        });
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$sendNotificationCallback$27(long j, int i, byte[] bArr) {
        TLRPC.Chat chatSync;
        TLRPC.User userSync;
        final String str = j + "_" + i + "_" + Utilities.bytesToHex(bArr) + "_0";
        this.waitingForCallback.put(str, Boolean.TRUE);
        final List<String> list = this.waitingForCallbackMap.get(j + "_" + i);
        if (list == null) {
            ArrayList arrayList = new ArrayList();
            this.waitingForCallbackMap.put(j + "_" + i, arrayList);
            list = arrayList;
        }
        list.add(str);
        if (DialogObject.isUserDialog(j)) {
            if (getMessagesController().getUser(Long.valueOf(j)) == null && (userSync = getMessagesStorage().getUserSync(j)) != null) {
                getMessagesController().putUser(userSync, true);
            }
        } else {
            long j2 = -j;
            if (getMessagesController().getChat(Long.valueOf(j2)) == null && (chatSync = getMessagesStorage().getChatSync(j2)) != null) {
                getMessagesController().putChat(chatSync, true);
            }
        }
        TLRPC.TL_messages_getBotCallbackAnswer tL_messages_getBotCallbackAnswer = new TLRPC.TL_messages_getBotCallbackAnswer();
        tL_messages_getBotCallbackAnswer.peer = getMessagesController().getInputPeer(j);
        tL_messages_getBotCallbackAnswer.msg_id = i;
        tL_messages_getBotCallbackAnswer.game = false;
        if (bArr != null) {
            tL_messages_getBotCallbackAnswer.flags |= 1;
            tL_messages_getBotCallbackAnswer.data = bArr;
        }
        getConnectionsManager().sendRequest(tL_messages_getBotCallbackAnswer, new RequestDelegate() { // from class: org.telegram.messenger.SendMessagesHelper$$ExternalSyntheticLambda103
            @Override // org.telegram.tgnet.RequestDelegate
            public final void run(TLObject tLObject, TLRPC.TL_error tL_error) {
                this.f$0.lambda$sendNotificationCallback$26(str, list, tLObject, tL_error);
            }
        }, 2);
        getMessagesController().markDialogAsRead(j, i, i, 0, false, 0L, 0, true, 0);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$sendNotificationCallback$26(final String str, final List list, TLObject tLObject, TLRPC.TL_error tL_error) {
        AndroidUtilities.runOnUIThread(new Runnable() { // from class: org.telegram.messenger.SendMessagesHelper$$ExternalSyntheticLambda77
            @Override // java.lang.Runnable
            public final void run() {
                this.f$0.lambda$sendNotificationCallback$25(str, list);
            }
        });
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$sendNotificationCallback$25(String str, List list) {
        this.waitingForCallback.remove(str);
        list.remove(str);
    }

    public void onMessageEdited(TLRPC.Message message) {
        if (message == null || message.reply_markup == null) {
            return;
        }
        List<String> listRemove = this.waitingForCallbackMap.remove(message.dialog_id + "_" + message.id);
        if (listRemove != null) {
            Iterator<String> it = listRemove.iterator();
            while (it.hasNext()) {
                this.waitingForCallback.remove(it.next());
            }
        }
    }

    public byte[] isSendingVote(MessageObject messageObject) {
        if (messageObject == null) {
            return null;
        }
        return this.waitingForVote.get("poll_" + messageObject.getPollId());
    }

    public int sendVote(MessageObject messageObject, ArrayList<TLRPC.PollAnswer> arrayList, Runnable runnable) {
        return sendVote(messageObject, arrayList, runnable, true);
    }

    public int sendVote(final MessageObject messageObject, ArrayList<TLRPC.PollAnswer> arrayList, final Runnable runnable, final boolean z) {
        byte[] bArr;
        if (messageObject == null) {
            return 0;
        }
        final String str = "poll_" + messageObject.getPollId();
        if (this.waitingForCallback.containsKey(str)) {
            return 0;
        }
        TLRPC.TL_messages_sendVote tL_messages_sendVote = new TLRPC.TL_messages_sendVote();
        tL_messages_sendVote.msg_id = messageObject.getId();
        tL_messages_sendVote.peer = getMessagesController().getInputPeer(messageObject.getDialogId());
        if (arrayList != null) {
            bArr = new byte[arrayList.size()];
            for (int i = 0; i < arrayList.size(); i++) {
                TLRPC.PollAnswer pollAnswer = arrayList.get(i);
                if (pollAnswer != null) {
                    tL_messages_sendVote.options.add(pollAnswer.option);
                    bArr[i] = pollAnswer.option[0];
                }
            }
        } else {
            bArr = new byte[0];
        }
        this.waitingForVote.put(str, bArr);
        return getConnectionsManager().sendRequest(tL_messages_sendVote, new RequestDelegate() { // from class: org.telegram.messenger.SendMessagesHelper$$ExternalSyntheticLambda53
            @Override // org.telegram.tgnet.RequestDelegate
            public final void run(TLObject tLObject, TLRPC.TL_error tL_error) {
                this.f$0.lambda$sendVote$29(messageObject, z, str, runnable, tLObject, tL_error);
            }
        });
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$sendVote$29(MessageObject messageObject, boolean z, final String str, final Runnable runnable, TLObject tLObject, TLRPC.TL_error tL_error) {
        if (tL_error == null) {
            this.voteSendTime.put(messageObject.getPollId(), 0L);
            if (z) {
                getMessagesController().processUpdates((TLRPC.Updates) tLObject, false);
            }
            this.voteSendTime.put(messageObject.getPollId(), Long.valueOf(SystemClock.elapsedRealtime()));
        }
        AndroidUtilities.runOnUIThread(new Runnable() { // from class: org.telegram.messenger.SendMessagesHelper$$ExternalSyntheticLambda26
            @Override // java.lang.Runnable
            public final void run() {
                this.f$0.lambda$sendVote$28(str, runnable);
            }
        });
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$sendVote$28(String str, Runnable runnable) {
        this.waitingForVote.remove(str);
        if (runnable != null) {
            runnable.run();
        }
    }

    public Boolean getSendingTodoValue(MessageObject messageObject, TLRPC.TodoItem todoItem) {
        return this.waitingForTodoUpdate.get(Integer.valueOf(Objects.hash(Long.valueOf(messageObject.getDialogId()), Integer.valueOf(messageObject.getId()), Integer.valueOf(todoItem.id))));
    }

    public int toggleTodo(final long j, final MessageObject messageObject, final TLRPC.TodoItem todoItem, final boolean z, final Runnable runnable) {
        if (messageObject == null) {
            return 0;
        }
        final int iHash = Objects.hash(Long.valueOf(messageObject.getDialogId()), Integer.valueOf(messageObject.getId()), Integer.valueOf(todoItem.id));
        this.waitingForTodoUpdate.put(Integer.valueOf(iHash), Boolean.valueOf(z));
        TLRPC.TL_messages_toggleTodoCompleted tL_messages_toggleTodoCompleted = new TLRPC.TL_messages_toggleTodoCompleted();
        tL_messages_toggleTodoCompleted.msg_id = messageObject.getId();
        tL_messages_toggleTodoCompleted.peer = getMessagesController().getInputPeer(messageObject.getDialogId());
        if (z) {
            tL_messages_toggleTodoCompleted.completed.add(Integer.valueOf(todoItem.id));
        } else {
            tL_messages_toggleTodoCompleted.incompleted.add(Integer.valueOf(todoItem.id));
        }
        return getConnectionsManager().sendRequest(tL_messages_toggleTodoCompleted, new RequestDelegate() { // from class: org.telegram.messenger.SendMessagesHelper$$ExternalSyntheticLambda79
            @Override // org.telegram.tgnet.RequestDelegate
            public final void run(TLObject tLObject, TLRPC.TL_error tL_error) {
                this.f$0.lambda$toggleTodo$31(messageObject, todoItem, z, j, iHash, runnable, tLObject, tL_error);
            }
        });
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$toggleTodo$31(MessageObject messageObject, TLRPC.TodoItem todoItem, final boolean z, long j, final int i, final Runnable runnable, TLObject tLObject, TLRPC.TL_error tL_error) {
        if (tL_error == null) {
            getMessagesStorage().toggleTodo(messageObject.getDialogId(), messageObject.getId(), todoItem.id, z, j);
            getMessagesController().processUpdates((TLRPC.Updates) tLObject, false);
        }
        AndroidUtilities.runOnUIThread(new Runnable() { // from class: org.telegram.messenger.SendMessagesHelper$$ExternalSyntheticLambda18
            @Override // java.lang.Runnable
            public final void run() {
                this.f$0.lambda$toggleTodo$30(i, z, runnable);
            }
        });
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$toggleTodo$30(int i, boolean z, Runnable runnable) {
        Boolean bool = this.waitingForTodoUpdate.get(Integer.valueOf(i));
        if (bool != null && bool.booleanValue() == z) {
            this.waitingForTodoUpdate.remove(Integer.valueOf(i));
        }
        if (runnable != null) {
            runnable.run();
        }
    }

    protected long getVoteSendTime(long j) {
        return ((Long) this.voteSendTime.get(j, 0L)).longValue();
    }

    public void sendReaction(MessageObject messageObject, ArrayList<ReactionsLayoutInBubble.VisibleReaction> arrayList, ReactionsLayoutInBubble.VisibleReaction visibleReaction, boolean z, boolean z2, BaseFragment baseFragment, final Runnable runnable) {
        if (messageObject == null || baseFragment == null) {
            return;
        }
        TLRPC.TL_messages_sendReaction tL_messages_sendReaction = new TLRPC.TL_messages_sendReaction();
        TLRPC.Message message = messageObject.messageOwner;
        if (message.isThreadMessage && message.fwd_from != null) {
            tL_messages_sendReaction.peer = getMessagesController().getInputPeer(messageObject.getFromChatId());
            tL_messages_sendReaction.msg_id = messageObject.messageOwner.fwd_from.saved_from_msg_id;
        } else {
            tL_messages_sendReaction.peer = getMessagesController().getInputPeer(messageObject.getDialogId());
            tL_messages_sendReaction.msg_id = messageObject.getId();
        }
        tL_messages_sendReaction.add_to_recent = z2;
        if (z2 && visibleReaction != null) {
            MediaDataController.getInstance(this.currentAccount).recentReactions.add(0, ReactionsUtils.toTLReaction(visibleReaction));
        }
        if (arrayList != null && !arrayList.isEmpty()) {
            for (int i = 0; i < arrayList.size(); i++) {
                ReactionsLayoutInBubble.VisibleReaction visibleReaction2 = arrayList.get(i);
                if (visibleReaction2.documentId != 0) {
                    TLRPC.TL_reactionCustomEmoji tL_reactionCustomEmoji = new TLRPC.TL_reactionCustomEmoji();
                    tL_reactionCustomEmoji.document_id = visibleReaction2.documentId;
                    tL_messages_sendReaction.reaction.add(tL_reactionCustomEmoji);
                    tL_messages_sendReaction.flags |= 1;
                } else if (visibleReaction2.emojicon != null) {
                    TLRPC.TL_reactionEmoji tL_reactionEmoji = new TLRPC.TL_reactionEmoji();
                    tL_reactionEmoji.emoticon = visibleReaction2.emojicon;
                    tL_messages_sendReaction.reaction.add(tL_reactionEmoji);
                    tL_messages_sendReaction.flags |= 1;
                }
            }
        }
        if (z) {
            tL_messages_sendReaction.flags |= 2;
            tL_messages_sendReaction.big = true;
        }
        getConnectionsManager().sendRequest(tL_messages_sendReaction, new RequestDelegate() { // from class: org.telegram.messenger.SendMessagesHelper$$ExternalSyntheticLambda113
            @Override // org.telegram.tgnet.RequestDelegate
            public final void run(TLObject tLObject, TLRPC.TL_error tL_error) {
                this.f$0.lambda$sendReaction$32(runnable, tLObject, tL_error);
            }
        });
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$sendReaction$32(Runnable runnable, TLObject tLObject, TLRPC.TL_error tL_error) {
        if (tLObject != null) {
            getMessagesController().processUpdates((TLRPC.Updates) tLObject, false);
            if (runnable != null) {
                AndroidUtilities.runOnUIThread(runnable);
            }
        }
    }

    public void requestUrlAuth(final String str, final ChatActivity chatActivity, final boolean z) {
        final TLRPC.TL_messages_requestUrlAuth tL_messages_requestUrlAuth = new TLRPC.TL_messages_requestUrlAuth();
        tL_messages_requestUrlAuth.url = str;
        tL_messages_requestUrlAuth.flags |= 4;
        getConnectionsManager().sendRequest(tL_messages_requestUrlAuth, new RequestDelegate() { // from class: org.telegram.messenger.SendMessagesHelper$$ExternalSyntheticLambda96
            @Override // org.telegram.tgnet.RequestDelegate
            public final void run(TLObject tLObject, TLRPC.TL_error tL_error) {
                this.f$0.lambda$requestUrlAuth$34(tL_messages_requestUrlAuth, chatActivity, str, z, tLObject, tL_error);
            }
        }, 2);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$requestUrlAuth$34(final TLRPC.TL_messages_requestUrlAuth tL_messages_requestUrlAuth, final ChatActivity chatActivity, final String str, final boolean z, final TLObject tLObject, TLRPC.TL_error tL_error) {
        AndroidUtilities.runOnUIThread(new Runnable() { // from class: org.telegram.messenger.SendMessagesHelper$$ExternalSyntheticLambda40
            @Override // java.lang.Runnable
            public final void run() {
                this.f$0.lambda$requestUrlAuth$33(tLObject, tL_messages_requestUrlAuth, chatActivity, str, z);
            }
        });
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$requestUrlAuth$33(TLObject tLObject, TLRPC.TL_messages_requestUrlAuth tL_messages_requestUrlAuth, ChatActivity chatActivity, String str, boolean z) {
        if (tLObject != null) {
            if (tLObject instanceof TLRPC.TL_urlAuthResultRequest) {
                OAuthSheet.handle(false, this.currentAccount, tL_messages_requestUrlAuth, (TLRPC.TL_urlAuthResultRequest) tLObject);
                return;
            } else if (tLObject instanceof TLRPC.TL_urlAuthResultAccepted) {
                OAuthSheet.handle(false, this.currentAccount, tL_messages_requestUrlAuth, (TLRPC.TL_urlAuthResultAccepted) tLObject);
                return;
            } else {
                if (tLObject instanceof TLRPC.TL_urlAuthResultDefault) {
                    AlertsCreator.showOpenUrlAlert(chatActivity, str, false, z);
                    return;
                }
                return;
            }
        }
        AlertsCreator.showOpenUrlAlert(chatActivity, str, false, z);
    }

    public void sendCallback(boolean z, MessageObject messageObject, TLRPC.KeyboardButton keyboardButton, ChatActivity chatActivity) {
        lambda$sendCallback$37(z, messageObject, keyboardButton, null, null, chatActivity);
    }

    /* JADX WARN: Code duplicated, block: B:20:0x0080  */
    /* JADX WARN: Code duplicated, block: B:21:0x00a6  */
    /* JADX WARN: Code duplicated, block: B:24:0x00bd  */
    /* JADX WARN: Code duplicated, block: B:26:0x00c5 A[DONT_INVERT] */
    /* JADX WARN: Code duplicated, block: B:27:0x00c7  */
    /* JADX WARN: Code duplicated, block: B:29:0x00f3  */
    /* JADX WARN: Code duplicated, block: B:31:0x00f7  */
    /* JADX WARN: Code duplicated, block: B:33:0x0101  */
    /* JADX WARN: Code duplicated, block: B:35:0x0128  */
    /* JADX WARN: Code duplicated, block: B:38:0x0144  */
    /* JADX WARN: Code duplicated, block: B:40:0x0169  */
    /* JADX WARN: Code duplicated, block: B:42:0x018a A[DONT_INVERT] */
    /* JADX WARN: Code duplicated, block: B:43:0x018c  */
    /* JADX WARN: Code duplicated, block: B:44:0x018f  */
    /* JADX WARN: Code duplicated, block: B:48:0x01a0  */
    /* JADX WARN: Instruction removed from duplicated block: B:20:0x0080, please report this as an issue */
    /* JADX INFO: renamed from: sendCallback, reason: merged with bridge method [inline-methods] */
    public void lambda$sendCallback$37(final boolean z, final MessageObject messageObject, final TLRPC.KeyboardButton keyboardButton, final TLRPC.InputCheckPasswordSRP inputCheckPasswordSRP, final TwoStepVerificationActivity twoStepVerificationActivity, final ChatActivity chatActivity) {
        final boolean z2;
        int i;
        final String str;
        List<String> list;
        final List<String> list2;
        final TLObject[] tLObjectArr;
        RequestDelegate requestDelegate;
        TLRPC.TL_messages_getBotCallbackAnswer tL_messages_getBotCallbackAnswer;
        byte[] bArr;
        TLRPC.InputCheckPasswordSRP tL_inputCheckPasswordEmpty;
        TLRPC.TL_payments_getPaymentForm tL_payments_getPaymentForm;
        JSONObject jSONObjectMakeThemeParams;
        if (messageObject == null || keyboardButton == null || chatActivity == null) {
            return;
        }
        boolean z3 = keyboardButton instanceof TLRPC.TL_keyboardButtonUrlAuth;
        if (!z3) {
            if (keyboardButton instanceof TLRPC.TL_keyboardButtonGame) {
                i = 1;
            } else if (keyboardButton instanceof TLRPC.TL_keyboardButtonBuy) {
                z2 = z;
                i = 2;
            } else {
                z2 = z;
                i = 0;
            }
            str = messageObject.getDialogId() + "_" + messageObject.getId() + "_" + Utilities.bytesToHex(keyboardButton.data) + "_" + i;
            this.waitingForCallback.put(str, Boolean.TRUE);
            list = this.waitingForCallbackMap.get(messageObject.getDialogId() + "_" + messageObject.getId());
            if (list == null) {
                HashMap<String, List<String>> map = this.waitingForCallbackMap;
                String str2 = messageObject.getDialogId() + "_" + messageObject.getId();
                ArrayList arrayList = new ArrayList();
                map.put(str2, arrayList);
                list2 = arrayList;
            } else {
                list2 = list;
            }
            list2.add(str);
            tLObjectArr = new TLObject[1];
            requestDelegate = new RequestDelegate() { // from class: org.telegram.messenger.SendMessagesHelper$$ExternalSyntheticLambda118
                @Override // org.telegram.tgnet.RequestDelegate
                public final void run(TLObject tLObject, TLRPC.TL_error tL_error) {
                    this.f$0.lambda$sendCallback$43(str, list2, z2, messageObject, keyboardButton, chatActivity, twoStepVerificationActivity, tLObjectArr, inputCheckPasswordSRP, z, tLObject, tL_error);
                }
            };
            if (z2) {
                getMessagesStorage().getBotCache(str, requestDelegate);
                return;
            }
            if (z3) {
                TLRPC.TL_messages_requestUrlAuth tL_messages_requestUrlAuth = new TLRPC.TL_messages_requestUrlAuth();
                tL_messages_requestUrlAuth.peer = getMessagesController().getInputPeer(messageObject.getDialogId());
                tL_messages_requestUrlAuth.msg_id = messageObject.getId();
                tL_messages_requestUrlAuth.button_id = keyboardButton.button_id;
                tL_messages_requestUrlAuth.flags |= 2;
                tLObjectArr[0] = tL_messages_requestUrlAuth;
                getConnectionsManager().sendRequest(tL_messages_requestUrlAuth, requestDelegate, 2);
                return;
            }
            if (keyboardButton instanceof TLRPC.TL_keyboardButtonBuy) {
                if ((messageObject.messageOwner.media.flags & 4) == 0) {
                    tL_payments_getPaymentForm = new TLRPC.TL_payments_getPaymentForm();
                    TLRPC.TL_inputInvoiceMessage tL_inputInvoiceMessage = new TLRPC.TL_inputInvoiceMessage();
                    tL_inputInvoiceMessage.msg_id = messageObject.getId();
                    tL_inputInvoiceMessage.peer = getMessagesController().getInputPeer(messageObject.messageOwner.peer_id);
                    tL_payments_getPaymentForm.invoice = tL_inputInvoiceMessage;
                    jSONObjectMakeThemeParams = BotWebViewSheet.makeThemeParams(null);
                    if (jSONObjectMakeThemeParams != null) {
                        TLRPC.TL_dataJSON tL_dataJSON = new TLRPC.TL_dataJSON();
                        tL_payments_getPaymentForm.theme_params = tL_dataJSON;
                        tL_dataJSON.data = jSONObjectMakeThemeParams.toString();
                        tL_payments_getPaymentForm.flags |= 1;
                    }
                    tLObjectArr[0] = tL_payments_getPaymentForm;
                    getConnectionsManager().sendRequest(tL_payments_getPaymentForm, requestDelegate, 2);
                    return;
                }
                TLRPC.TL_payments_getPaymentReceipt tL_payments_getPaymentReceipt = new TLRPC.TL_payments_getPaymentReceipt();
                tL_payments_getPaymentReceipt.msg_id = messageObject.messageOwner.media.receipt_msg_id;
                tL_payments_getPaymentReceipt.peer = getMessagesController().getInputPeer(messageObject.messageOwner.peer_id);
                tLObjectArr[0] = tL_payments_getPaymentReceipt;
                getConnectionsManager().sendRequest(tL_payments_getPaymentReceipt, requestDelegate, 2);
                return;
            }
            tL_messages_getBotCallbackAnswer = new TLRPC.TL_messages_getBotCallbackAnswer();
            tL_messages_getBotCallbackAnswer.peer = getMessagesController().getInputPeer(messageObject.getDialogId());
            tL_messages_getBotCallbackAnswer.msg_id = messageObject.getId();
            tL_messages_getBotCallbackAnswer.game = keyboardButton instanceof TLRPC.TL_keyboardButtonGame;
            if (keyboardButton.requires_password) {
                if (inputCheckPasswordSRP != null) {
                    tL_inputCheckPasswordEmpty = inputCheckPasswordSRP;
                } else {
                    tL_inputCheckPasswordEmpty = new TLRPC.TL_inputCheckPasswordEmpty();
                }
                tL_messages_getBotCallbackAnswer.password = tL_inputCheckPasswordEmpty;
                tL_messages_getBotCallbackAnswer.flags |= 4;
            }
            bArr = keyboardButton.data;
            if (bArr != null) {
                tL_messages_getBotCallbackAnswer.flags |= 1;
                tL_messages_getBotCallbackAnswer.data = bArr;
            }
            getConnectionsManager().sendRequest(tL_messages_getBotCallbackAnswer, requestDelegate, 2);
        }
        i = 3;
        z2 = false;
        str = messageObject.getDialogId() + "_" + messageObject.getId() + "_" + Utilities.bytesToHex(keyboardButton.data) + "_" + i;
        this.waitingForCallback.put(str, Boolean.TRUE);
        list = this.waitingForCallbackMap.get(messageObject.getDialogId() + "_" + messageObject.getId());
        if (list == null) {
            HashMap<String, List<String>> map2 = this.waitingForCallbackMap;
            String str3 = messageObject.getDialogId() + "_" + messageObject.getId();
            ArrayList arrayList2 = new ArrayList();
            map2.put(str3, arrayList2);
            list2 = arrayList2;
        } else {
            list2 = list;
        }
        list2.add(str);
        tLObjectArr = new TLObject[1];
        requestDelegate = new RequestDelegate() { // from class: org.telegram.messenger.SendMessagesHelper$$ExternalSyntheticLambda118
            @Override // org.telegram.tgnet.RequestDelegate
            public final void run(TLObject tLObject, TLRPC.TL_error tL_error) {
                this.f$0.lambda$sendCallback$43(str, list2, z2, messageObject, keyboardButton, chatActivity, twoStepVerificationActivity, tLObjectArr, inputCheckPasswordSRP, z, tLObject, tL_error);
            }
        };
        if (z2) {
            getMessagesStorage().getBotCache(str, requestDelegate);
            return;
        }
        if (z3) {
            TLRPC.TL_messages_requestUrlAuth tL_messages_requestUrlAuth2 = new TLRPC.TL_messages_requestUrlAuth();
            tL_messages_requestUrlAuth2.peer = getMessagesController().getInputPeer(messageObject.getDialogId());
            tL_messages_requestUrlAuth2.msg_id = messageObject.getId();
            tL_messages_requestUrlAuth2.button_id = keyboardButton.button_id;
            tL_messages_requestUrlAuth2.flags |= 2;
            tLObjectArr[0] = tL_messages_requestUrlAuth2;
            getConnectionsManager().sendRequest(tL_messages_requestUrlAuth2, requestDelegate, 2);
            return;
        }
        if (keyboardButton instanceof TLRPC.TL_keyboardButtonBuy) {
            if ((messageObject.messageOwner.media.flags & 4) == 0) {
                tL_payments_getPaymentForm = new TLRPC.TL_payments_getPaymentForm();
                TLRPC.TL_inputInvoiceMessage tL_inputInvoiceMessage2 = new TLRPC.TL_inputInvoiceMessage();
                tL_inputInvoiceMessage2.msg_id = messageObject.getId();
                tL_inputInvoiceMessage2.peer = getMessagesController().getInputPeer(messageObject.messageOwner.peer_id);
                tL_payments_getPaymentForm.invoice = tL_inputInvoiceMessage2;
                jSONObjectMakeThemeParams = BotWebViewSheet.makeThemeParams(null);
                if (jSONObjectMakeThemeParams != null) {
                    TLRPC.TL_dataJSON tL_dataJSON2 = new TLRPC.TL_dataJSON();
                    tL_payments_getPaymentForm.theme_params = tL_dataJSON2;
                    tL_dataJSON2.data = jSONObjectMakeThemeParams.toString();
                    tL_payments_getPaymentForm.flags |= 1;
                }
                tLObjectArr[0] = tL_payments_getPaymentForm;
                getConnectionsManager().sendRequest(tL_payments_getPaymentForm, requestDelegate, 2);
                return;
            }
            TLRPC.TL_payments_getPaymentReceipt tL_payments_getPaymentReceipt2 = new TLRPC.TL_payments_getPaymentReceipt();
            tL_payments_getPaymentReceipt2.msg_id = messageObject.messageOwner.media.receipt_msg_id;
            tL_payments_getPaymentReceipt2.peer = getMessagesController().getInputPeer(messageObject.messageOwner.peer_id);
            tLObjectArr[0] = tL_payments_getPaymentReceipt2;
            getConnectionsManager().sendRequest(tL_payments_getPaymentReceipt2, requestDelegate, 2);
            return;
        }
        tL_messages_getBotCallbackAnswer = new TLRPC.TL_messages_getBotCallbackAnswer();
        tL_messages_getBotCallbackAnswer.peer = getMessagesController().getInputPeer(messageObject.getDialogId());
        tL_messages_getBotCallbackAnswer.msg_id = messageObject.getId();
        tL_messages_getBotCallbackAnswer.game = keyboardButton instanceof TLRPC.TL_keyboardButtonGame;
        if (keyboardButton.requires_password) {
            if (inputCheckPasswordSRP != null) {
                tL_inputCheckPasswordEmpty = inputCheckPasswordSRP;
            } else {
                tL_inputCheckPasswordEmpty = new TLRPC.TL_inputCheckPasswordEmpty();
            }
            tL_messages_getBotCallbackAnswer.password = tL_inputCheckPasswordEmpty;
            tL_messages_getBotCallbackAnswer.flags |= 4;
        }
        bArr = keyboardButton.data;
        if (bArr != null) {
            tL_messages_getBotCallbackAnswer.flags |= 1;
            tL_messages_getBotCallbackAnswer.data = bArr;
        }
        getConnectionsManager().sendRequest(tL_messages_getBotCallbackAnswer, requestDelegate, 2);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$sendCallback$43(final String str, final List list, final boolean z, final MessageObject messageObject, final TLRPC.KeyboardButton keyboardButton, final ChatActivity chatActivity, final TwoStepVerificationActivity twoStepVerificationActivity, final TLObject[] tLObjectArr, final TLRPC.InputCheckPasswordSRP inputCheckPasswordSRP, final boolean z2, final TLObject tLObject, final TLRPC.TL_error tL_error) {
        AndroidUtilities.runOnUIThread(new Runnable() { // from class: org.telegram.messenger.SendMessagesHelper$$ExternalSyntheticLambda78
            @Override // java.lang.Runnable
            public final void run() {
                this.f$0.lambda$sendCallback$42(str, list, z, tLObject, messageObject, keyboardButton, chatActivity, twoStepVerificationActivity, tLObjectArr, tL_error, inputCheckPasswordSRP, z2);
            }
        });
    }

    /* JADX INFO: Access modifiers changed from: private */
    /* JADX WARN: Code duplicated, block: B:104:0x01dd  */
    /* JADX WARN: Code duplicated, block: B:22:0x0069  */
    public /* synthetic */ void lambda$sendCallback$42(final String str, final List list, boolean z, TLObject tLObject, final MessageObject messageObject, final TLRPC.KeyboardButton keyboardButton, final ChatActivity chatActivity, final TwoStepVerificationActivity twoStepVerificationActivity, TLObject[] tLObjectArr, TLRPC.TL_error tL_error, TLRPC.InputCheckPasswordSRP inputCheckPasswordSRP, final boolean z2) {
        int i;
        String name;
        boolean z3;
        this.waitingForCallback.remove(str);
        list.remove(str);
        if (z && tLObject == null) {
            sendCallback(false, messageObject, keyboardButton, chatActivity);
            return;
        }
        if (tLObject != null) {
            if (twoStepVerificationActivity != null) {
                twoStepVerificationActivity.needHideProgress();
                twoStepVerificationActivity.finishFragment();
            }
            long fromChatId = messageObject.getFromChatId();
            long j = messageObject.messageOwner.via_bot_id;
            if (j != 0) {
                fromChatId = j;
            }
            if (fromChatId > 0) {
                TLRPC.User user = getMessagesController().getUser(Long.valueOf(fromChatId));
                if (user != null) {
                    name = ContactsController.formatName(user.first_name, user.last_name);
                } else {
                    name = null;
                }
            } else {
                TLRPC.Chat chat = getMessagesController().getChat(Long.valueOf(-fromChatId));
                if (chat != null) {
                    name = chat.title;
                } else {
                    name = null;
                }
            }
            if (name == null) {
                name = "bot";
            }
            if (keyboardButton instanceof TLRPC.TL_keyboardButtonUrlAuth) {
                if (tLObject instanceof TLRPC.TL_urlAuthResultRequest) {
                    OAuthSheet.handle(false, this.currentAccount, (TLRPC.TL_messages_requestUrlAuth) tLObjectArr[0], (TLRPC.TL_urlAuthResultRequest) tLObject, keyboardButton.url, null, null, false, null);
                    return;
                } else if (tLObject instanceof TLRPC.TL_urlAuthResultAccepted) {
                    OAuthSheet.handle(false, this.currentAccount, (TLRPC.TL_messages_requestUrlAuth) tLObjectArr[0], (TLRPC.TL_urlAuthResultAccepted) tLObject, keyboardButton.url, null, null, false, null);
                    return;
                } else {
                    if (tLObject instanceof TLRPC.TL_urlAuthResultDefault) {
                        AlertsCreator.showOpenUrlAlert(chatActivity, keyboardButton.url, false, true);
                        return;
                    }
                    return;
                }
            }
            if (keyboardButton instanceof TLRPC.TL_keyboardButtonBuy) {
                if (tLObject instanceof TLRPC.TL_payments_paymentFormStars) {
                    StarsController.getInstance(this.currentAccount).openPaymentForm(messageObject, ((TLRPC.TL_payments_getPaymentForm) tLObjectArr[0]).invoice, (TLRPC.TL_payments_paymentFormStars) tLObject, new Runnable() { // from class: org.telegram.messenger.SendMessagesHelper$$ExternalSyntheticLambda91
                        @Override // java.lang.Runnable
                        public final void run() {
                            this.f$0.lambda$sendCallback$35(str, list);
                        }
                    }, new Utilities.Callback() { // from class: org.telegram.messenger.SendMessagesHelper$$ExternalSyntheticLambda92
                        @Override // org.telegram.messenger.Utilities.Callback
                        public final void run(Object obj) {
                            SendMessagesHelper.m3729$r8$lambda$siqOgfLwE48Ihs8Cu1gZPVBQmE((String) obj);
                        }
                    });
                    return;
                }
                if (tLObject instanceof TLRPC.PaymentForm) {
                    TLRPC.PaymentForm paymentForm = (TLRPC.PaymentForm) tLObject;
                    getMessagesController().putUsers(paymentForm.users, false);
                    chatActivity.presentFragment(new PaymentFormActivity(paymentForm, messageObject, chatActivity));
                    return;
                } else {
                    if (tLObject instanceof TLRPC.TL_payments_paymentReceiptStars) {
                        Context context = LaunchActivity.instance;
                        if (context == null) {
                            context = ApplicationLoader.applicationContext;
                        }
                        StarsIntroActivity.showTransactionSheet(context, false, this.currentAccount, (TLRPC.TL_payments_paymentReceiptStars) tLObject, (Theme.ResourcesProvider) null);
                        return;
                    }
                    if (tLObject instanceof TLRPC.PaymentReceipt) {
                        chatActivity.presentFragment(new PaymentFormActivity((TLRPC.PaymentReceipt) tLObject));
                        return;
                    }
                    return;
                }
            }
            TLRPC.TL_messages_botCallbackAnswer tL_messages_botCallbackAnswer = (TLRPC.TL_messages_botCallbackAnswer) tLObject;
            if (!z && tL_messages_botCallbackAnswer.cache_time != 0 && !keyboardButton.requires_password) {
                getMessagesStorage().saveBotCache(str, tL_messages_botCallbackAnswer);
            }
            String str2 = tL_messages_botCallbackAnswer.message;
            if (str2 != null) {
                if (tL_messages_botCallbackAnswer.alert) {
                    if (chatActivity.getParentActivity() == null) {
                        return;
                    }
                    AlertDialog.Builder builder = new AlertDialog.Builder(chatActivity.getParentActivity());
                    builder.setTitle(name);
                    builder.setPositiveButton(LocaleController.getString(R.string.OK), null);
                    builder.setMessage(tL_messages_botCallbackAnswer.message);
                    chatActivity.showDialog(builder.create());
                    return;
                }
                chatActivity.showAlert(name, str2);
                return;
            }
            if (tL_messages_botCallbackAnswer.url == null || chatActivity.getParentActivity() == null) {
                return;
            }
            TLRPC.User user2 = getMessagesController().getUser(Long.valueOf(fromChatId));
            boolean z4 = user2 != null && user2.verified;
            if (keyboardButton instanceof TLRPC.TL_keyboardButtonGame) {
                TLRPC.MessageMedia messageMedia = messageObject.messageOwner.media;
                TLRPC.TL_game tL_game = messageMedia instanceof TLRPC.TL_messageMediaGame ? messageMedia.game : null;
                if (tL_game == null) {
                    return;
                }
                String str3 = tL_messages_botCallbackAnswer.url;
                if (z4) {
                    z3 = false;
                } else {
                    if (MessagesController.getNotificationsSettings(this.currentAccount).getBoolean("askgame_" + fromChatId, true)) {
                        z3 = true;
                    } else {
                        z3 = false;
                    }
                }
                chatActivity.showOpenGameAlert(tL_game, messageObject, str3, z3, fromChatId);
                return;
            }
            AlertsCreator.showOpenUrlAlert(chatActivity, tL_messages_botCallbackAnswer.url, false, false);
            return;
        }
        if (tL_error == null || chatActivity.getParentActivity() == null) {
            return;
        }
        if ("PASSWORD_HASH_INVALID".equals(tL_error.text)) {
            if (inputCheckPasswordSRP == null) {
                AlertDialog.Builder builder2 = new AlertDialog.Builder(chatActivity.getParentActivity());
                builder2.setTitle(LocaleController.getString(R.string.BotOwnershipTransfer));
                builder2.setMessage(AndroidUtilities.replaceTags(LocaleController.formatString("BotOwnershipTransferReadyAlertText", R.string.BotOwnershipTransferReadyAlertText, new Object[0])));
                builder2.setPositiveButton(LocaleController.getString(R.string.BotOwnershipTransferChangeOwner), new AlertDialog.OnButtonClickListener() { // from class: org.telegram.messenger.SendMessagesHelper$$ExternalSyntheticLambda93
                    @Override // org.telegram.ui.ActionBar.AlertDialog.OnButtonClickListener
                    public final void onClick(AlertDialog alertDialog, int i2) {
                        this.f$0.lambda$sendCallback$38(z2, messageObject, keyboardButton, chatActivity, alertDialog, i2);
                    }
                });
                builder2.setNegativeButton(LocaleController.getString(R.string.Cancel), null);
                chatActivity.showDialog(builder2.create());
                return;
            }
            return;
        }
        if ("PASSWORD_MISSING".equals(tL_error.text) || tL_error.text.startsWith("PASSWORD_TOO_FRESH_") || tL_error.text.startsWith("SESSION_TOO_FRESH_")) {
            if (twoStepVerificationActivity != null) {
                twoStepVerificationActivity.needHideProgress();
            }
            AlertDialog.Builder builder3 = new AlertDialog.Builder(chatActivity.getParentActivity());
            builder3.setTitle(LocaleController.getString(R.string.EditAdminTransferAlertTitle));
            LinearLayout linearLayout = new LinearLayout(chatActivity.getParentActivity());
            linearLayout.setPadding(AndroidUtilities.dp(24.0f), AndroidUtilities.dp(2.0f), AndroidUtilities.dp(24.0f), 0);
            linearLayout.setOrientation(1);
            builder3.setView(linearLayout);
            TextView textView = new TextView(chatActivity.getParentActivity());
            int i2 = Theme.key_dialogTextBlack;
            textView.setTextColor(Theme.getColor(i2));
            textView.setTextSize(1, 16.0f);
            textView.setGravity((LocaleController.isRTL ? 5 : 3) | 48);
            textView.setText(AndroidUtilities.replaceTags(LocaleController.formatString("BotOwnershipTransferAlertText", R.string.BotOwnershipTransferAlertText, new Object[0])));
            linearLayout.addView(textView, LayoutHelper.createLinear(-1, -2));
            LinearLayout linearLayout2 = new LinearLayout(chatActivity.getParentActivity());
            linearLayout2.setOrientation(0);
            linearLayout.addView(linearLayout2, LayoutHelper.createLinear(-1, -2, 0.0f, 11.0f, 0.0f, 0.0f));
            ImageView imageView = new ImageView(chatActivity.getParentActivity());
            imageView.setImageResource(R.drawable.list_circle);
            imageView.setPadding(LocaleController.isRTL ? AndroidUtilities.dp(11.0f) : 0, AndroidUtilities.dp(9.0f), LocaleController.isRTL ? 0 : AndroidUtilities.dp(11.0f), 0);
            int color = Theme.getColor(i2);
            PorterDuff.Mode mode = PorterDuff.Mode.MULTIPLY;
            imageView.setColorFilter(new PorterDuffColorFilter(color, mode));
            TextView textView2 = new TextView(chatActivity.getParentActivity());
            textView2.setTextColor(Theme.getColor(i2));
            textView2.setTextSize(1, 16.0f);
            textView2.setGravity((LocaleController.isRTL ? 5 : 3) | 48);
            textView2.setText(AndroidUtilities.replaceTags(LocaleController.getString(R.string.EditAdminTransferAlertText1)));
            if (LocaleController.isRTL) {
                linearLayout2.addView(textView2, LayoutHelper.createLinear(-1, -2));
                linearLayout2.addView(imageView, LayoutHelper.createLinear(-2, -2, 5));
            } else {
                linearLayout2.addView(imageView, LayoutHelper.createLinear(-2, -2));
                linearLayout2.addView(textView2, LayoutHelper.createLinear(-1, -2));
            }
            LinearLayout linearLayout3 = new LinearLayout(chatActivity.getParentActivity());
            linearLayout3.setOrientation(0);
            linearLayout.addView(linearLayout3, LayoutHelper.createLinear(-1, -2, 0.0f, 11.0f, 0.0f, 0.0f));
            ImageView imageView2 = new ImageView(chatActivity.getParentActivity());
            imageView2.setImageResource(R.drawable.list_circle);
            imageView2.setPadding(LocaleController.isRTL ? AndroidUtilities.dp(11.0f) : 0, AndroidUtilities.dp(9.0f), LocaleController.isRTL ? 0 : AndroidUtilities.dp(11.0f), 0);
            imageView2.setColorFilter(new PorterDuffColorFilter(Theme.getColor(i2), mode));
            TextView textView3 = new TextView(chatActivity.getParentActivity());
            textView3.setTextColor(Theme.getColor(i2));
            textView3.setTextSize(1, 16.0f);
            textView3.setGravity((LocaleController.isRTL ? 5 : 3) | 48);
            textView3.setText(AndroidUtilities.replaceTags(LocaleController.getString(R.string.EditAdminTransferAlertText2)));
            if (LocaleController.isRTL) {
                linearLayout3.addView(textView3, LayoutHelper.createLinear(-1, -2));
                i = 5;
                linearLayout3.addView(imageView2, LayoutHelper.createLinear(-2, -2, 5));
            } else {
                i = 5;
                linearLayout3.addView(imageView2, LayoutHelper.createLinear(-2, -2));
                linearLayout3.addView(textView3, LayoutHelper.createLinear(-1, -2));
            }
            if ("PASSWORD_MISSING".equals(tL_error.text)) {
                builder3.setPositiveButton(LocaleController.getString(R.string.EditAdminTransferSetPassword), new AlertDialog.OnButtonClickListener() { // from class: org.telegram.messenger.SendMessagesHelper$$ExternalSyntheticLambda94
                    @Override // org.telegram.ui.ActionBar.AlertDialog.OnButtonClickListener
                    public final void onClick(AlertDialog alertDialog, int i3) {
                        chatActivity.presentFragment(new TwoStepVerificationSetupActivity(6, null));
                    }
                });
                builder3.setNegativeButton(LocaleController.getString(R.string.Cancel), null);
            } else {
                TextView textView4 = new TextView(chatActivity.getParentActivity());
                textView4.setTextColor(Theme.getColor(i2));
                textView4.setTextSize(1, 16.0f);
                if (!LocaleController.isRTL) {
                    i = 3;
                }
                textView4.setGravity(i | 48);
                textView4.setText(LocaleController.getString(R.string.EditAdminTransferAlertText3));
                linearLayout.addView(textView4, LayoutHelper.createLinear(-1, -2, 0.0f, 11.0f, 0.0f, 0.0f));
                builder3.setNegativeButton(LocaleController.getString(R.string.OK), null);
            }
            chatActivity.showDialog(builder3.create());
            return;
        }
        if ("SRP_ID_INVALID".equals(tL_error.text)) {
            ConnectionsManager.getInstance(this.currentAccount).sendRequest(new TL_account.getPassword(), new RequestDelegate() { // from class: org.telegram.messenger.SendMessagesHelper$$ExternalSyntheticLambda95
                @Override // org.telegram.tgnet.RequestDelegate
                public final void run(TLObject tLObject2, TLRPC.TL_error tL_error2) {
                    this.f$0.lambda$sendCallback$41(twoStepVerificationActivity, z2, messageObject, keyboardButton, chatActivity, tLObject2, tL_error2);
                }
            }, 8);
        } else if (twoStepVerificationActivity != null) {
            twoStepVerificationActivity.needHideProgress();
            twoStepVerificationActivity.finishFragment();
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$sendCallback$35(String str, List list) {
        this.waitingForCallback.remove(str);
        list.remove(str);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$sendCallback$38(final boolean z, final MessageObject messageObject, final TLRPC.KeyboardButton keyboardButton, final ChatActivity chatActivity, AlertDialog alertDialog, int i) {
        final TwoStepVerificationActivity twoStepVerificationActivity = new TwoStepVerificationActivity();
        twoStepVerificationActivity.setDelegate(0, new TwoStepVerificationActivity.TwoStepVerificationActivityDelegate() { // from class: org.telegram.messenger.SendMessagesHelper$$ExternalSyntheticLambda117
            @Override // org.telegram.ui.TwoStepVerificationActivity.TwoStepVerificationActivityDelegate
            public final void didEnterPassword(TLRPC.InputCheckPasswordSRP inputCheckPasswordSRP) {
                this.f$0.lambda$sendCallback$37(z, messageObject, keyboardButton, twoStepVerificationActivity, chatActivity, inputCheckPasswordSRP);
            }
        });
        chatActivity.presentFragment(twoStepVerificationActivity);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$sendCallback$41(final TwoStepVerificationActivity twoStepVerificationActivity, final boolean z, final MessageObject messageObject, final TLRPC.KeyboardButton keyboardButton, final ChatActivity chatActivity, final TLObject tLObject, final TLRPC.TL_error tL_error) {
        AndroidUtilities.runOnUIThread(new Runnable() { // from class: org.telegram.messenger.SendMessagesHelper$$ExternalSyntheticLambda74
            @Override // java.lang.Runnable
            public final void run() {
                this.f$0.lambda$sendCallback$40(tL_error, tLObject, twoStepVerificationActivity, z, messageObject, keyboardButton, chatActivity);
            }
        });
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$sendCallback$40(TLRPC.TL_error tL_error, TLObject tLObject, TwoStepVerificationActivity twoStepVerificationActivity, boolean z, MessageObject messageObject, TLRPC.KeyboardButton keyboardButton, ChatActivity chatActivity) {
        if (tL_error == null) {
            TL_account.Password password = (TL_account.Password) tLObject;
            twoStepVerificationActivity.setCurrentPasswordInfo(null, password);
            TwoStepVerificationActivity.initPasswordNewAlgo(password);
            lambda$sendCallback$37(z, messageObject, keyboardButton, twoStepVerificationActivity.getNewSrpPassword(), twoStepVerificationActivity, chatActivity);
        }
    }

    public boolean isSendingCallback(MessageObject messageObject, TLRPC.KeyboardButton keyboardButton) {
        int i = 0;
        if (messageObject == null || keyboardButton == null) {
            return false;
        }
        if (keyboardButton instanceof TLRPC.TL_keyboardButtonUrlAuth) {
            i = 3;
        } else if (keyboardButton instanceof TLRPC.TL_keyboardButtonGame) {
            i = 1;
        } else if (keyboardButton instanceof TLRPC.TL_keyboardButtonBuy) {
            i = 2;
        }
        return this.waitingForCallback.containsKey(messageObject.getDialogId() + "_" + messageObject.getId() + "_" + Utilities.bytesToHex(keyboardButton.data) + "_" + i);
    }

    public void sendGame(TLRPC.InputPeer inputPeer, TLRPC.TL_inputMediaGame tL_inputMediaGame, long j, final long j2) {
        NativeByteBuffer nativeByteBuffer;
        if (inputPeer == null || tL_inputMediaGame == null) {
            return;
        }
        TLRPC.TL_messages_sendMedia tL_messages_sendMedia = new TLRPC.TL_messages_sendMedia();
        tL_messages_sendMedia.peer = inputPeer;
        if (inputPeer instanceof TLRPC.TL_inputPeerChannel) {
            SharedPreferences notificationsSettings = MessagesController.getNotificationsSettings(this.currentAccount);
            StringBuilder sb = new StringBuilder();
            sb.append(NotificationsSettingsFacade.PROPERTY_SILENT);
            sb.append(-inputPeer.channel_id);
            tL_messages_sendMedia.silent = notificationsSettings.getBoolean(sb.toString(), false) && !AyuGhostController.getInstance(this.currentAccount).isSendWithoutSound();
        } else if (inputPeer instanceof TLRPC.TL_inputPeerChat) {
            SharedPreferences notificationsSettings2 = MessagesController.getNotificationsSettings(this.currentAccount);
            StringBuilder sb2 = new StringBuilder();
            sb2.append(NotificationsSettingsFacade.PROPERTY_SILENT);
            sb2.append(-inputPeer.chat_id);
            tL_messages_sendMedia.silent = notificationsSettings2.getBoolean(sb2.toString(), false) && !AyuGhostController.getInstance(this.currentAccount).isSendWithoutSound();
        } else {
            SharedPreferences notificationsSettings3 = MessagesController.getNotificationsSettings(this.currentAccount);
            StringBuilder sb3 = new StringBuilder();
            sb3.append(NotificationsSettingsFacade.PROPERTY_SILENT);
            sb3.append(inputPeer.user_id);
            tL_messages_sendMedia.silent = notificationsSettings3.getBoolean(sb3.toString(), false) && !AyuGhostController.getInstance(this.currentAccount).isSendWithoutSound();
        }
        tL_messages_sendMedia.random_id = j != 0 ? j : getNextRandomId();
        tL_messages_sendMedia.message = _UrlKt.FRAGMENT_ENCODE_SET;
        tL_messages_sendMedia.media = tL_inputMediaGame;
        long sendAsPeerId = ChatObject.getSendAsPeerId(getMessagesController().getChat(Long.valueOf(inputPeer.chat_id)), getMessagesController().getChatFull(inputPeer.chat_id));
        if (sendAsPeerId != UserConfig.getInstance(this.currentAccount).getClientUserId()) {
            tL_messages_sendMedia.send_as = getMessagesController().getInputPeer(sendAsPeerId);
        }
        long sendPaidMessagesStars = getMessagesController().getSendPaidMessagesStars(DialogObject.getPeerDialogId(inputPeer));
        if (sendPaidMessagesStars <= 0) {
            sendPaidMessagesStars = DialogObject.getMessagesStarsPrice(getMessagesController().isUserContactBlocked(DialogObject.getPeerDialogId(inputPeer)));
        }
        if (sendPaidMessagesStars > 0) {
            tL_messages_sendMedia.flags |= TLObject.FLAG_21;
            tL_messages_sendMedia.allow_paid_stars = sendPaidMessagesStars;
        }
        if (j2 == 0) {
            NativeByteBuffer nativeByteBuffer2 = null;
            try {
                nativeByteBuffer = new NativeByteBuffer(inputPeer.getObjectSize() + tL_inputMediaGame.getObjectSize() + 12);
                try {
                    nativeByteBuffer.writeInt32(3);
                    nativeByteBuffer.writeInt64(j);
                    inputPeer.serializeToStream(nativeByteBuffer);
                    tL_inputMediaGame.serializeToStream(nativeByteBuffer);
                } catch (Exception e) {
                    e = e;
                    nativeByteBuffer2 = nativeByteBuffer;
                    FileLog.e(e);
                    nativeByteBuffer = nativeByteBuffer2;
                }
            } catch (Exception e2) {
                e = e2;
            }
            j2 = getMessagesStorage().createPendingTask(nativeByteBuffer);
        }
        getConnectionsManager().sendRequest(tL_messages_sendMedia, new RequestDelegate() { // from class: org.telegram.messenger.SendMessagesHelper$$ExternalSyntheticLambda116
            @Override // org.telegram.tgnet.RequestDelegate
            public final void run(TLObject tLObject, TLRPC.TL_error tL_error) {
                this.f$0.lambda$sendGame$44(j2, tLObject, tL_error);
            }
        });
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$sendGame$44(long j, TLObject tLObject, TLRPC.TL_error tL_error) {
        if (tL_error == null) {
            getMessagesController().processUpdates((TLRPC.Updates) tLObject, false);
        }
        if (j != 0) {
            getMessagesStorage().removePendingTask(j);
        }
    }

    /*  JADX ERROR: Type inference failed
        jadx.core.utils.exceptions.JadxOverflowException: Type inference error: updates count limit reached with updateSeq = 106851. Try increasing type updates limit count.
        	at jadx.core.utils.ErrorsCounter.addError(ErrorsCounter.java:59)
        	at jadx.core.utils.ErrorsCounter.error(ErrorsCounter.java:31)
        	at jadx.core.dex.attributes.nodes.NotificationAttrNode.addError(NotificationAttrNode.java:19)
        	at jadx.core.dex.visitors.typeinference.TypeInferenceVisitor.visit(TypeInferenceVisitor.java:79)
        */
    public void sendMessage(org.telegram.messenger.SendMessagesHelper.SendMessageParams r102) {
        /*
            Method dump skipped, instruction units count: 10685
            To view this dump add '--comments-level debug' option
        */
        throw new UnsupportedOperationException("Method not decompiled: org.telegram.messenger.SendMessagesHelper.sendMessage(org.telegram.messenger.SendMessagesHelper$SendMessageParams):void");
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$sendMessage$45(SendMessageParams sendMessageParams, Long l) {
        sendMessageParams.payStars = l.longValue();
        sendMessage(sendMessageParams);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$sendMessage$46() {
        getNotificationCenter().lambda$postNotificationNameOnUIThread$1(AyuConstants.FIX_SCHEDULED_BAR, new Object[0]);
    }

    private void performSendDelayedMessage(DelayedMessage delayedMessage) {
        performSendDelayedMessage(delayedMessage, -1);
    }

    private TLRPC.PhotoSize getThumbForSecretChat(ArrayList<TLRPC.PhotoSize> arrayList) {
        if (arrayList != null && !arrayList.isEmpty()) {
            int size = arrayList.size();
            for (int i = 0; i < size; i++) {
                TLRPC.PhotoSize photoSize = arrayList.get(i);
                if (photoSize != null && !(photoSize instanceof TLRPC.TL_photoPathSize) && !(photoSize instanceof TLRPC.TL_photoSizeEmpty) && photoSize.location != null) {
                    if (photoSize instanceof TLRPC.TL_photoStrippedSize) {
                        return photoSize;
                    }
                    TLRPC.TL_photoSize_layer127 tL_photoSize_layer127 = new TLRPC.TL_photoSize_layer127();
                    tL_photoSize_layer127.type = photoSize.type;
                    tL_photoSize_layer127.w = photoSize.w;
                    tL_photoSize_layer127.h = photoSize.h;
                    tL_photoSize_layer127.size = photoSize.size;
                    byte[] bArr = photoSize.bytes;
                    tL_photoSize_layer127.bytes = bArr;
                    if (bArr == null) {
                        tL_photoSize_layer127.bytes = new byte[0];
                    }
                    TLRPC.TL_fileLocation_layer82 tL_fileLocation_layer82 = new TLRPC.TL_fileLocation_layer82();
                    tL_photoSize_layer127.location = tL_fileLocation_layer82;
                    TLRPC.FileLocation fileLocation = photoSize.location;
                    tL_fileLocation_layer82.dc_id = fileLocation.dc_id;
                    tL_fileLocation_layer82.volume_id = fileLocation.volume_id;
                    tL_fileLocation_layer82.local_id = fileLocation.local_id;
                    tL_fileLocation_layer82.secret = fileLocation.secret;
                    return tL_photoSize_layer127;
                }
            }
        }
        return null;
    }

    private void performSendDelayedMessage(final DelayedMessage delayedMessage, int i) {
        boolean z;
        Object obj;
        TLRPC.InputFile inputFile;
        boolean z2;
        TLRPC.InputMedia inputMedia;
        TLRPC.InputPeer inputPeer;
        TLRPC.InputMedia inputMedia2;
        TLRPC.PhotoSize photoSize;
        final TLRPC.InputMedia inputMedia3;
        TLRPC.InputPeer inputPeer2;
        String str;
        VideoEditedInfo videoEditedInfo;
        TLRPC.InputMedia inputMedia4;
        TLRPC.PhotoSize photoSize2;
        int i2 = delayedMessage.type;
        boolean z3 = true;
        if (i2 == 0) {
            String str2 = delayedMessage.httpLocation;
            if (str2 != null) {
                putToDelayedMessages(str2, delayedMessage);
                ImageLoader.getInstance().loadHttpFile(delayedMessage.httpLocation, "file", this.currentAccount);
                return;
            }
            if (delayedMessage.sendRequest != null) {
                String string = FileLoader.getInstance(this.currentAccount).getPathToAttach(delayedMessage.photoSize).toString();
                putToDelayedMessages(string, delayedMessage);
                getFileLoader().uploadFile(string, false, true, 16777216);
                putToUploadingMessages(delayedMessage.obj);
                return;
            }
            String string2 = FileLoader.getInstance(this.currentAccount).getPathToAttach(delayedMessage.photoSize).toString();
            if (delayedMessage.sendEncryptedRequest != null && (photoSize2 = delayedMessage.photoSize) != null && photoSize2.location.dc_id != 0) {
                File file = new File(string2);
                if (!file.exists()) {
                    string2 = FileLoader.getInstance(this.currentAccount).getPathToAttach(delayedMessage.photoSize, true).toString();
                    file = new File(string2);
                }
                if (!file.exists()) {
                    putToDelayedMessages(FileLoader.getAttachFileName(delayedMessage.photoSize), delayedMessage);
                    getFileLoader().loadFile(ImageLocation.getForObject(delayedMessage.photoSize, delayedMessage.locationParent), delayedMessage.parentObject, "jpg", 3, 0);
                    return;
                }
            }
            putToDelayedMessages(string2, delayedMessage);
            getFileLoader().uploadFile(string2, true, true, 16777216);
            putToUploadingMessages(delayedMessage.obj);
            return;
        }
        if (i2 == 1) {
            VideoEditedInfo videoEditedInfo2 = delayedMessage.videoEditedInfo;
            if (videoEditedInfo2 != null && videoEditedInfo2.needConvert() && delayedMessage.performMediaUpload) {
                MessageObject messageObject = delayedMessage.obj;
                String string3 = messageObject.messageOwner.attachPath;
                TLRPC.Document document = messageObject.getDocument();
                if (string3 == null) {
                    StringBuilder sb = new StringBuilder();
                    sb.append(FileLoader.getDirectory(4));
                    sb.append("/");
                    sb.append(document.id);
                    sb.append(".");
                    sb.append(delayedMessage.videoEditedInfo.isSticker ? "webm" : "mp4");
                    string3 = sb.toString();
                }
                putToDelayedMessages(string3, delayedMessage);
                if (!delayedMessage.videoEditedInfo.alreadyScheduledConverting) {
                    MediaController.getInstance().scheduleVideoConvert(delayedMessage.obj);
                }
                putToUploadingMessages(delayedMessage.obj);
                return;
            }
            VideoEditedInfo videoEditedInfo3 = delayedMessage.videoEditedInfo;
            if (videoEditedInfo3 != null) {
                TLRPC.InputFile inputFile2 = videoEditedInfo3.file;
                if (inputFile2 != null) {
                    TLObject tLObject = delayedMessage.sendRequest;
                    if (tLObject instanceof TLRPC.TL_messages_sendMedia) {
                        inputMedia4 = ((TLRPC.TL_messages_sendMedia) tLObject).media;
                    } else {
                        inputMedia4 = ((TLRPC.TL_messages_editMessage) tLObject).media;
                    }
                    inputMedia4.file = inputFile2;
                    videoEditedInfo3.file = null;
                } else if (videoEditedInfo3.encryptedFile != null) {
                    TLRPC.TL_decryptedMessage tL_decryptedMessage = (TLRPC.TL_decryptedMessage) delayedMessage.sendEncryptedRequest;
                    TLRPC.DecryptedMessageMedia decryptedMessageMedia = tL_decryptedMessage.media;
                    decryptedMessageMedia.size = videoEditedInfo3.estimatedSize;
                    decryptedMessageMedia.key = videoEditedInfo3.key;
                    decryptedMessageMedia.iv = videoEditedInfo3.iv;
                    SecretChatHelper secretChatHelper = getSecretChatHelper();
                    MessageObject messageObject2 = delayedMessage.obj;
                    secretChatHelper.performSendEncryptedRequest(tL_decryptedMessage, messageObject2.messageOwner, delayedMessage.encryptedChat, delayedMessage.videoEditedInfo.encryptedFile, delayedMessage.originalPath, messageObject2);
                    delayedMessage.videoEditedInfo.encryptedFile = null;
                    return;
                }
            }
            TLObject tLObject2 = delayedMessage.sendRequest;
            if (tLObject2 != null) {
                if (tLObject2 instanceof TLRPC.TL_messages_sendMedia) {
                    TLRPC.TL_messages_sendMedia tL_messages_sendMedia = (TLRPC.TL_messages_sendMedia) tLObject2;
                    inputMedia3 = tL_messages_sendMedia.media;
                    inputPeer2 = tL_messages_sendMedia.peer;
                } else {
                    TLRPC.TL_messages_editMessage tL_messages_editMessage = (TLRPC.TL_messages_editMessage) tLObject2;
                    inputMedia3 = tL_messages_editMessage.media;
                    inputPeer2 = tL_messages_editMessage.peer;
                }
                if (inputMedia3 instanceof TLRPC.TL_inputMediaPaidMedia) {
                    TLRPC.TL_inputMediaPaidMedia tL_inputMediaPaidMedia = (TLRPC.TL_inputMediaPaidMedia) inputMedia3;
                    if (!tL_inputMediaPaidMedia.extended_media.isEmpty()) {
                        inputMedia3 = (TLRPC.InputMedia) tL_inputMediaPaidMedia.extended_media.get(0);
                    }
                }
                if (inputMedia3.file == null && !(inputMedia3 instanceof TLRPC.TL_inputMediaDocument) && delayedMessage.performMediaUpload) {
                    MessageObject messageObject3 = delayedMessage.obj;
                    String str3 = messageObject3.messageOwner.attachPath;
                    TLRPC.Document document2 = messageObject3.getDocument();
                    if (str3 == null) {
                        str3 = FileLoader.getDirectory(4) + "/" + document2.id + ".mp4";
                    }
                    String str4 = str3;
                    putToDelayedMessages(str4, delayedMessage);
                    VideoEditedInfo videoEditedInfo4 = delayedMessage.obj.videoEditedInfo;
                    if (videoEditedInfo4 == null || !videoEditedInfo4.notReadyYet) {
                        if (videoEditedInfo4 != null && videoEditedInfo4.needConvert()) {
                            getFileLoader().uploadFile(str4, false, false, document2.size, 33554432, false);
                        } else {
                            getFileLoader().uploadFile(str4, false, false, 33554432);
                        }
                    }
                    putToUploadingMessages(delayedMessage.obj);
                    return;
                }
                TLRPC.InputPhoto inputPhoto = inputMedia3.video_cover;
                if (inputPhoto == null && delayedMessage.coverFile == null && delayedMessage.coverPhotoSize != null && delayedMessage.performCoverUpload) {
                    String str5 = FileLoader.getDirectory(r14) + "/" + delayedMessage.coverPhotoSize.location.volume_id + "_" + delayedMessage.coverPhotoSize.location.local_id + ".jpg";
                    putToDelayedMessages(str5, delayedMessage);
                    getFileLoader().uploadFile(str5, false, true, 16777216);
                    putToUploadingMessages(delayedMessage.obj);
                    return;
                }
                if (inputPhoto == null && delayedMessage.coverFile != null && delayedMessage.performCoverUpload) {
                    TLRPC.TL_messages_uploadMedia tL_messages_uploadMedia = new TLRPC.TL_messages_uploadMedia();
                    tL_messages_uploadMedia.peer = inputPeer2;
                    TLRPC.TL_inputMediaUploadedPhoto tL_inputMediaUploadedPhoto = new TLRPC.TL_inputMediaUploadedPhoto();
                    tL_inputMediaUploadedPhoto.file = delayedMessage.coverFile;
                    tL_messages_uploadMedia.media = tL_inputMediaUploadedPhoto;
                    getConnectionsManager().sendRequest(tL_messages_uploadMedia, new RequestDelegate() { // from class: org.telegram.messenger.SendMessagesHelper$$ExternalSyntheticLambda82
                        @Override // org.telegram.tgnet.RequestDelegate
                        public final void run(TLObject tLObject3, TLRPC.TL_error tL_error) {
                            this.f$0.lambda$performSendDelayedMessage$48(inputMedia3, delayedMessage, tLObject3, tL_error);
                        }
                    });
                    return;
                }
                MessageObject messageObject4 = delayedMessage.obj;
                if (messageObject4 != null && (videoEditedInfo = messageObject4.videoEditedInfo) != null && videoEditedInfo.isSticker) {
                    str = "webp";
                } else {
                    str = "jpg";
                }
                String str6 = FileLoader.getDirectory(r14) + "/" + delayedMessage.photoSize.location.volume_id + "_" + delayedMessage.photoSize.location.local_id + "." + str;
                putToDelayedMessages(str6, delayedMessage);
                getFileLoader().uploadFile(str6, false, true, 16777216);
                putToUploadingMessages(delayedMessage.obj);
                return;
            }
            MessageObject messageObject5 = delayedMessage.obj;
            String str7 = messageObject5.messageOwner.attachPath;
            TLRPC.Document document3 = messageObject5.getDocument();
            if (str7 == null) {
                str7 = FileLoader.getDirectory(r14) + "/" + document3.id + ".mp4";
            }
            if (delayedMessage.sendEncryptedRequest != null && document3.dc_id != 0) {
                File file2 = new File(str7);
                if (!file2.exists() && (file2 = getFileLoader().getPathToMessage(delayedMessage.obj.messageOwner)) != null && file2.exists()) {
                    TLRPC.Message message = delayedMessage.obj.messageOwner;
                    String absolutePath = file2.getAbsolutePath();
                    message.attachPath = absolutePath;
                    delayedMessage.obj.attachPathExists = true;
                    str7 = absolutePath;
                }
                if ((file2 == null || (!file2.exists() && delayedMessage.obj.getDocument() != null)) && (file2 = getFileLoader().getPathToAttach(delayedMessage.obj.getDocument(), false)) != null && file2.exists()) {
                    TLRPC.Message message2 = delayedMessage.obj.messageOwner;
                    String absolutePath2 = file2.getAbsolutePath();
                    message2.attachPath = absolutePath2;
                    delayedMessage.obj.attachPathExists = true;
                    str7 = absolutePath2;
                }
                if (file2 == null || !file2.exists()) {
                    putToDelayedMessages(FileLoader.getAttachFileName(document3), delayedMessage);
                    getFileLoader().loadFile(document3, delayedMessage.parentObject, 3, 0);
                    return;
                }
            }
            putToDelayedMessages(str7, delayedMessage);
            VideoEditedInfo videoEditedInfo5 = delayedMessage.obj.videoEditedInfo;
            if (videoEditedInfo5 == null || !videoEditedInfo5.notReadyYet) {
                if (videoEditedInfo5 != null && videoEditedInfo5.needConvert()) {
                    getFileLoader().uploadFile(str7, true, false, document3.size, 33554432, false);
                } else {
                    getFileLoader().uploadFile(str7, true, false, 33554432);
                }
            }
            putToUploadingMessages(delayedMessage.obj);
            return;
        }
        if (i2 == 2) {
            String str8 = delayedMessage.httpLocation;
            if (str8 != null) {
                putToDelayedMessages(str8, delayedMessage);
                ImageLoader.getInstance().loadHttpFile(delayedMessage.httpLocation, "gif", this.currentAccount);
                return;
            }
            TLObject tLObject3 = delayedMessage.sendRequest;
            if (tLObject3 != null) {
                if (tLObject3 instanceof TLRPC.TL_messages_sendMedia) {
                    inputMedia2 = ((TLRPC.TL_messages_sendMedia) tLObject3).media;
                } else {
                    inputMedia2 = ((TLRPC.TL_messages_editMessage) tLObject3).media;
                }
                if (inputMedia2.file == null) {
                    String str9 = delayedMessage.obj.messageOwner.attachPath;
                    putToDelayedMessages(str9, delayedMessage);
                    getFileLoader().uploadFile(str9, delayedMessage.sendRequest == null, false, 67108864);
                    putToUploadingMessages(delayedMessage.obj);
                    return;
                }
                if (inputMedia2.thumb != null || (photoSize = delayedMessage.photoSize) == null || (photoSize instanceof TLRPC.TL_photoStrippedSize)) {
                    return;
                }
                String str10 = FileLoader.getDirectory(4) + "/" + delayedMessage.photoSize.location.volume_id + "_" + delayedMessage.photoSize.location.local_id + ".jpg";
                putToDelayedMessages(str10, delayedMessage);
                getFileLoader().uploadFile(str10, false, true, 16777216);
                putToUploadingMessages(delayedMessage.obj);
                return;
            }
            MessageObject messageObject6 = delayedMessage.obj;
            String str11 = messageObject6.messageOwner.attachPath;
            TLRPC.Document document4 = messageObject6.getDocument();
            if (delayedMessage.sendEncryptedRequest != null && document4.dc_id != 0) {
                File file3 = new File(str11);
                if (!file3.exists() && (file3 = getFileLoader().getPathToMessage(delayedMessage.obj.messageOwner)) != null && file3.exists()) {
                    TLRPC.Message message3 = delayedMessage.obj.messageOwner;
                    String absolutePath3 = file3.getAbsolutePath();
                    message3.attachPath = absolutePath3;
                    delayedMessage.obj.attachPathExists = true;
                    str11 = absolutePath3;
                }
                if ((file3 == null || (!file3.exists() && delayedMessage.obj.getDocument() != null)) && (file3 = getFileLoader().getPathToAttach(delayedMessage.obj.getDocument(), false)) != null && file3.exists()) {
                    TLRPC.Message message4 = delayedMessage.obj.messageOwner;
                    String absolutePath4 = file3.getAbsolutePath();
                    message4.attachPath = absolutePath4;
                    delayedMessage.obj.attachPathExists = true;
                    str11 = absolutePath4;
                }
                if (file3 == null || !file3.exists()) {
                    putToDelayedMessages(FileLoader.getAttachFileName(document4), delayedMessage);
                    getFileLoader().loadFile(document4, delayedMessage.parentObject, 3, 0);
                    return;
                }
            }
            putToDelayedMessages(str11, delayedMessage);
            getFileLoader().uploadFile(str11, true, false, 67108864);
            putToUploadingMessages(delayedMessage.obj);
            return;
        }
        if (i2 == 3) {
            String str12 = delayedMessage.obj.messageOwner.attachPath;
            putToDelayedMessages(str12, delayedMessage);
            getFileLoader().uploadFile(str12, delayedMessage.sendRequest == null, true, ConnectionsManager.FileTypeAudio);
            putToUploadingMessages(delayedMessage.obj);
            return;
        }
        if (i2 != 4) {
            if (i2 == 5) {
                final String str13 = "stickerset_" + delayedMessage.obj.getId();
                TLRPC.TL_messages_getStickerSet tL_messages_getStickerSet = new TLRPC.TL_messages_getStickerSet();
                tL_messages_getStickerSet.stickerset = (TLRPC.InputStickerSet) delayedMessage.parentObject;
                getConnectionsManager().sendRequest(tL_messages_getStickerSet, new RequestDelegate() { // from class: org.telegram.messenger.SendMessagesHelper$$ExternalSyntheticLambda84
                    @Override // org.telegram.tgnet.RequestDelegate
                    public final void run(TLObject tLObject4, TLRPC.TL_error tL_error) {
                        this.f$0.lambda$performSendDelayedMessage$52(delayedMessage, str13, tLObject4, tL_error);
                    }
                });
                putToDelayedMessages(str13, delayedMessage);
                return;
            }
            return;
        }
        boolean z4 = i < 0;
        if (delayedMessage.performMediaUpload || delayedMessage.performCoverUpload) {
            int size = i < 0 ? delayedMessage.messageObjects.size() - 1 : i;
            final MessageObject messageObject7 = delayedMessage.messageObjects.get(size);
            TLRPC.Document document5 = messageObject7.getDocument();
            if (document5 == null && (MessageObject.getMedia(messageObject7) instanceof TLRPC.TL_messageMediaPaidMedia)) {
                TLRPC.TL_messageMediaPaidMedia tL_messageMediaPaidMedia = (TLRPC.TL_messageMediaPaidMedia) MessageObject.getMedia(messageObject7);
                TLRPC.MessageExtendedMedia messageExtendedMedia = size >= tL_messageMediaPaidMedia.extended_media.size() ? null : tL_messageMediaPaidMedia.extended_media.get(size);
                TLRPC.MessageMedia messageMedia = messageExtendedMedia instanceof TLRPC.TL_messageExtendedMedia ? ((TLRPC.TL_messageExtendedMedia) messageExtendedMedia).media : null;
                document5 = messageMedia == null ? null : messageMedia.document;
            }
            if (document5 != null) {
                VideoEditedInfo videoEditedInfo6 = delayedMessage.videoEditedInfo;
                if (videoEditedInfo6 != null && videoEditedInfo6.needConvert() && delayedMessage.performMediaUpload) {
                    String str14 = messageObject7.messageOwner.attachPath;
                    if (str14 == null) {
                        str14 = FileLoader.getDirectory(4) + "/" + document5.id + ".mp4";
                    }
                    putToDelayedMessages(str14, delayedMessage);
                    delayedMessage.extraHashMap.put(messageObject7, str14);
                    delayedMessage.extraHashMap.put(str14 + "_i", messageObject7);
                    TLRPC.PhotoSize photoSize3 = delayedMessage.photoSize;
                    if (photoSize3 != null && photoSize3.location != null) {
                        delayedMessage.extraHashMap.put(str14 + "_t", delayedMessage.photoSize);
                    }
                    TLRPC.PhotoSize photoSize4 = delayedMessage.coverPhotoSize;
                    if (photoSize4 != null && photoSize4.location != null) {
                        delayedMessage.extraHashMap.put(str14 + "_ct", delayedMessage.coverPhotoSize);
                    }
                    if (!delayedMessage.videoEditedInfo.alreadyScheduledConverting) {
                        MediaController.getInstance().scheduleVideoConvert(messageObject7);
                    }
                    delayedMessage.obj = messageObject7;
                    putToUploadingMessages(messageObject7);
                    z = z4;
                } else {
                    String str15 = messageObject7.messageOwner.attachPath;
                    if (str15 == null) {
                        str15 = FileLoader.getDirectory(4) + "/" + document5.id + ".mp4";
                    }
                    TLObject tLObject4 = delayedMessage.sendRequest;
                    if (tLObject4 != null) {
                        if (tLObject4 instanceof TLRPC.TL_messages_sendMultiMedia) {
                            TLRPC.TL_messages_sendMultiMedia tL_messages_sendMultiMedia = (TLRPC.TL_messages_sendMultiMedia) tLObject4;
                            inputPeer = tL_messages_sendMultiMedia.peer;
                            inputMedia = ((TLRPC.TL_inputSingleMedia) tL_messages_sendMultiMedia.multi_media.get(size)).media;
                        } else if (tLObject4 instanceof TLRPC.TL_messages_sendMedia) {
                            TLRPC.TL_messages_sendMedia tL_messages_sendMedia2 = (TLRPC.TL_messages_sendMedia) tLObject4;
                            inputPeer = tL_messages_sendMedia2.peer;
                            TLRPC.InputMedia inputMedia5 = tL_messages_sendMedia2.media;
                            inputMedia = inputMedia5 instanceof TLRPC.TL_inputMediaPaidMedia ? (TLRPC.InputMedia) ((TLRPC.TL_inputMediaPaidMedia) inputMedia5).extended_media.get(size) : null;
                        } else {
                            inputMedia = null;
                            inputPeer = null;
                        }
                        if (inputMedia != null && inputMedia.file == null && !(inputMedia instanceof TLRPC.TL_inputMediaDocument) && delayedMessage.performMediaUpload) {
                            putToDelayedMessages(str15, delayedMessage);
                            delayedMessage.extraHashMap.put(messageObject7, str15);
                            delayedMessage.extraHashMap.put(str15, inputMedia);
                            delayedMessage.extraHashMap.put(str15 + "_i", messageObject7);
                            TLRPC.PhotoSize photoSize5 = delayedMessage.photoSize;
                            if (photoSize5 != null && photoSize5.location != null) {
                                delayedMessage.extraHashMap.put(str15 + "_t", delayedMessage.photoSize);
                            }
                            TLRPC.PhotoSize photoSize6 = delayedMessage.coverPhotoSize;
                            if (photoSize6 != null && photoSize6.location != null) {
                                String str16 = FileLoader.getDirectory(4) + "/" + delayedMessage.coverPhotoSize.location.volume_id + "_" + delayedMessage.coverPhotoSize.location.local_id + ".jpg";
                                delayedMessage.extraHashMap.put(str15 + "_ct", delayedMessage.coverPhotoSize);
                                delayedMessage.extraHashMap.put(str16 + "_doc", str15);
                            }
                            VideoEditedInfo videoEditedInfo7 = messageObject7.videoEditedInfo;
                            if (videoEditedInfo7 != null && videoEditedInfo7.needConvert()) {
                                getFileLoader().uploadFile(str15, false, false, document5.size, 33554432, false);
                            } else {
                                getFileLoader().uploadFile(str15, false, false, 33554432);
                            }
                            putToUploadingMessages(messageObject7);
                            z = z4;
                        } else {
                            final String str17 = str15;
                            TLRPC.PhotoSize photoSize7 = delayedMessage.coverPhotoSize;
                            if (photoSize7 != null && delayedMessage.coverFile == null && inputMedia.video_cover == null) {
                                StringBuilder sb2 = new StringBuilder();
                                sb2.append(FileLoader.getDirectory(4));
                                sb2.append("/");
                                z = z4;
                                sb2.append(delayedMessage.coverPhotoSize.location.volume_id);
                                sb2.append("_");
                                sb2.append(delayedMessage.coverPhotoSize.location.local_id);
                                sb2.append(".jpg");
                                String string4 = sb2.toString();
                                putToDelayedMessages(string4, delayedMessage);
                                TLRPC.PhotoSize photoSize8 = delayedMessage.coverPhotoSize;
                                if (photoSize8 != null && photoSize8.location != null) {
                                    String str18 = FileLoader.getDirectory(4) + "/" + delayedMessage.coverPhotoSize.location.volume_id + "_" + delayedMessage.coverPhotoSize.location.local_id + ".jpg";
                                    delayedMessage.extraHashMap.put(str17 + "_ct", delayedMessage.coverPhotoSize);
                                    delayedMessage.extraHashMap.put(str18 + "_doc", str17);
                                }
                                delayedMessage.extraHashMap.put(string4 + "_o", str17);
                                delayedMessage.extraHashMap.put(str17 + "_i", messageObject7);
                                delayedMessage.extraHashMap.put(messageObject7, string4);
                                delayedMessage.extraHashMap.put(string4, inputMedia);
                                getFileLoader().uploadFile(string4, false, true, 16777216);
                                putToUploadingMessages(messageObject7);
                            } else {
                                z = z4;
                                if (photoSize7 != null && delayedMessage.coverFile != null && inputMedia != null && inputMedia.video_cover == null) {
                                    TLRPC.TL_messages_uploadMedia tL_messages_uploadMedia2 = new TLRPC.TL_messages_uploadMedia();
                                    tL_messages_uploadMedia2.peer = inputPeer;
                                    TLRPC.TL_inputMediaUploadedPhoto tL_inputMediaUploadedPhoto2 = new TLRPC.TL_inputMediaUploadedPhoto();
                                    tL_inputMediaUploadedPhoto2.file = delayedMessage.coverFile;
                                    tL_messages_uploadMedia2.media = tL_inputMediaUploadedPhoto2;
                                    final TLRPC.InputMedia inputMedia6 = inputMedia;
                                    getConnectionsManager().sendRequest(tL_messages_uploadMedia2, new RequestDelegate() { // from class: org.telegram.messenger.SendMessagesHelper$$ExternalSyntheticLambda83
                                        @Override // org.telegram.tgnet.RequestDelegate
                                        public final void run(TLObject tLObject5, TLRPC.TL_error tL_error) {
                                            this.f$0.lambda$performSendDelayedMessage$50(inputMedia6, delayedMessage, str17, messageObject7, tLObject5, tL_error);
                                        }
                                    });
                                } else if (delayedMessage.photoSize != null) {
                                    String str19 = FileLoader.getDirectory(4) + "/" + delayedMessage.photoSize.location.volume_id + "_" + delayedMessage.photoSize.location.local_id + ".jpg";
                                    putToDelayedMessages(str19, delayedMessage);
                                    delayedMessage.extraHashMap.put(str19 + "_o", str17);
                                    delayedMessage.extraHashMap.put(messageObject7, str19);
                                    delayedMessage.extraHashMap.put(str19, inputMedia);
                                    getFileLoader().uploadFile(str19, false, true, 16777216);
                                    putToUploadingMessages(messageObject7);
                                }
                            }
                        }
                    } else {
                        z = z4;
                        TLRPC.TL_messages_sendEncryptedMultiMedia tL_messages_sendEncryptedMultiMedia = (TLRPC.TL_messages_sendEncryptedMultiMedia) delayedMessage.sendEncryptedRequest;
                        putToDelayedMessages(str15, delayedMessage);
                        delayedMessage.extraHashMap.put(messageObject7, str15);
                        delayedMessage.extraHashMap.put(str15, tL_messages_sendEncryptedMultiMedia.files.get(size));
                        delayedMessage.extraHashMap.put(str15 + "_i", messageObject7);
                        TLRPC.PhotoSize photoSize9 = delayedMessage.photoSize;
                        if (photoSize9 != null && photoSize9.location != null) {
                            delayedMessage.extraHashMap.put(str15 + "_t", delayedMessage.photoSize);
                        }
                        VideoEditedInfo videoEditedInfo8 = messageObject7.videoEditedInfo;
                        if (videoEditedInfo8 != null && videoEditedInfo8.needConvert()) {
                            getFileLoader().uploadFile(str15, true, false, document5.size, 33554432, false);
                        } else {
                            getFileLoader().uploadFile(str15, true, false, 33554432);
                        }
                        putToUploadingMessages(messageObject7);
                    }
                }
                inputFile = null;
                delayedMessage.videoEditedInfo = null;
                delayedMessage.photoSize = null;
                delayedMessage.coverPhotoSize = null;
            } else {
                z = z4;
                String str20 = delayedMessage.httpLocation;
                if (str20 != null) {
                    putToDelayedMessages(str20, delayedMessage);
                    delayedMessage.extraHashMap.put(messageObject7, delayedMessage.httpLocation);
                    delayedMessage.extraHashMap.put(delayedMessage.httpLocation, messageObject7);
                    ImageLoader.getInstance().loadHttpFile(delayedMessage.httpLocation, "file", this.currentAccount);
                    inputFile = null;
                    delayedMessage.httpLocation = null;
                } else {
                    TLObject tLObject5 = delayedMessage.sendRequest;
                    if (tLObject5 instanceof TLRPC.TL_messages_sendMultiMedia) {
                        obj = ((TLRPC.TL_inputSingleMedia) ((TLRPC.TL_messages_sendMultiMedia) tLObject5).multi_media.get(size)).media;
                    } else if ((tLObject5 instanceof TLRPC.TL_messages_sendMedia) && (((TLRPC.TL_messages_sendMedia) tLObject5).media instanceof TLRPC.TL_inputMediaPaidMedia)) {
                        obj = (TLObject) ((TLRPC.TL_inputMediaPaidMedia) ((TLRPC.TL_messages_sendMedia) tLObject5).media).extended_media.get(size);
                    } else {
                        obj = (TLObject) ((TLRPC.TL_messages_sendEncryptedMultiMedia) delayedMessage.sendEncryptedRequest).files.get(size);
                    }
                    String string5 = FileLoader.getInstance(this.currentAccount).getPathToAttach(delayedMessage.photoSize).toString();
                    putToDelayedMessages(string5, delayedMessage);
                    delayedMessage.extraHashMap.put(string5, obj);
                    delayedMessage.extraHashMap.put(messageObject7, string5);
                    z3 = true;
                    getFileLoader().uploadFile(string5, delayedMessage.sendEncryptedRequest != null, true, 16777216);
                    putToUploadingMessages(messageObject7);
                    inputFile = null;
                    delayedMessage.photoSize = null;
                }
                delayedMessage.coverFile = inputFile;
                delayedMessage.performMediaUpload = false;
                delayedMessage.performCoverUpload = false;
                z2 = z;
            }
            z3 = true;
            delayedMessage.coverFile = inputFile;
            delayedMessage.performMediaUpload = false;
            delayedMessage.performCoverUpload = false;
            z2 = z;
        } else {
            if (!delayedMessage.messageObjects.isEmpty()) {
                ArrayList<MessageObject> arrayList = delayedMessage.messageObjects;
                putToSendingMessages(arrayList.get(arrayList.size() - 1).messageOwner, delayedMessage.finalGroupMessage != 0);
            }
            z2 = z4;
        }
        sendReadyToSendGroup(delayedMessage, z2, z3);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$performSendDelayedMessage$48(final TLRPC.InputMedia inputMedia, final DelayedMessage delayedMessage, final TLObject tLObject, TLRPC.TL_error tL_error) {
        AndroidUtilities.runOnUIThread(new Runnable() { // from class: org.telegram.messenger.SendMessagesHelper$$ExternalSyntheticLambda39
            @Override // java.lang.Runnable
            public final void run() {
                this.f$0.lambda$performSendDelayedMessage$47(tLObject, inputMedia, delayedMessage);
            }
        });
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$performSendDelayedMessage$47(TLObject tLObject, TLRPC.InputMedia inputMedia, DelayedMessage delayedMessage) {
        TLRPC.PhotoSize photoSize;
        MessageObject messageObject;
        VideoEditedInfo videoEditedInfo;
        if (tLObject instanceof TLRPC.TL_messageMediaPhoto) {
            TLRPC.Photo photo = ((TLRPC.TL_messageMediaPhoto) tLObject).photo;
            TLRPC.TL_inputPhoto tL_inputPhoto = new TLRPC.TL_inputPhoto();
            tL_inputPhoto.id = photo.id;
            tL_inputPhoto.access_hash = photo.access_hash;
            tL_inputPhoto.file_reference = photo.file_reference;
            if (inputMedia instanceof TLRPC.TL_inputMediaUploadedDocument) {
                inputMedia.flags |= 64;
                inputMedia.video_cover = tL_inputPhoto;
            } else if (inputMedia instanceof TLRPC.TL_inputMediaDocument) {
                inputMedia.flags |= 8;
                inputMedia.video_cover = tL_inputPhoto;
            }
            TLRPC.InputMedia inputMedia2 = delayedMessage.inputUploadMedia;
            if (inputMedia2 instanceof TLRPC.TL_inputMediaUploadedDocument) {
                inputMedia2.flags |= 64;
                inputMedia2.video_cover = tL_inputPhoto;
            }
            if (delayedMessage.performMediaUpload && inputMedia.thumb == null && (photoSize = delayedMessage.photoSize) != null && photoSize.location != null && ((messageObject = delayedMessage.obj) == null || (videoEditedInfo = messageObject.videoEditedInfo) == null || !videoEditedInfo.isSticker)) {
                performSendDelayedMessage(delayedMessage);
                return;
            } else {
                performSendMessageRequest(delayedMessage.sendRequest, delayedMessage.obj, delayedMessage.originalPath, delayedMessage, delayedMessage.parentObject, null, delayedMessage.scheduled);
                return;
            }
        }
        delayedMessage.markAsError();
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$performSendDelayedMessage$50(final TLRPC.InputMedia inputMedia, final DelayedMessage delayedMessage, final String str, final MessageObject messageObject, final TLObject tLObject, TLRPC.TL_error tL_error) {
        AndroidUtilities.runOnUIThread(new Runnable() { // from class: org.telegram.messenger.SendMessagesHelper$$ExternalSyntheticLambda23
            @Override // java.lang.Runnable
            public final void run() {
                this.f$0.lambda$performSendDelayedMessage$49(tLObject, inputMedia, delayedMessage, str, messageObject);
            }
        });
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$performSendDelayedMessage$49(TLObject tLObject, TLRPC.InputMedia inputMedia, DelayedMessage delayedMessage, String str, MessageObject messageObject) {
        if (tLObject instanceof TLRPC.TL_messageMediaPhoto) {
            TLRPC.Photo photo = ((TLRPC.TL_messageMediaPhoto) tLObject).photo;
            TLRPC.TL_inputPhoto tL_inputPhoto = new TLRPC.TL_inputPhoto();
            tL_inputPhoto.id = photo.id;
            tL_inputPhoto.access_hash = photo.access_hash;
            tL_inputPhoto.file_reference = photo.file_reference;
            if (inputMedia instanceof TLRPC.TL_inputMediaUploadedDocument) {
                inputMedia.flags |= 64;
                inputMedia.video_cover = tL_inputPhoto;
            } else if (inputMedia instanceof TLRPC.TL_inputMediaDocument) {
                inputMedia.flags |= 8;
                inputMedia.video_cover = tL_inputPhoto;
            }
            TLRPC.PhotoSize photoSize = null;
            delayedMessage.coverFile = null;
            delayedMessage.coverPhotoSize = null;
            HashMap<Object, Object> map = delayedMessage.extraHashMap;
            if (map != null) {
                map.remove(str + "_ct");
            }
            int iIndexOf = delayedMessage.messageObjects.indexOf(messageObject);
            ArrayList<TLRPC.InputMedia> arrayList = delayedMessage.inputMedias;
            if (arrayList != null && iIndexOf >= 0 && iIndexOf < arrayList.size()) {
                TLRPC.InputMedia inputMedia2 = delayedMessage.inputMedias.get(iIndexOf);
                if (inputMedia2 instanceof TLRPC.TL_inputMediaUploadedDocument) {
                    inputMedia2.flags |= 64;
                    inputMedia2.video_cover = tL_inputPhoto;
                }
            }
            HashMap<Object, Object> map2 = delayedMessage.extraHashMap;
            if (map2 != null) {
                if (map2.containsKey(str + "_t")) {
                    photoSize = (TLRPC.PhotoSize) delayedMessage.extraHashMap.get(str + "_t");
                }
            }
            delayedMessage.photoSize = photoSize;
            if (inputMedia.thumb == null && photoSize != null && photoSize.location != null) {
                delayedMessage.performMediaUpload = true;
                performSendDelayedMessage(delayedMessage, iIndexOf);
                return;
            } else {
                sendReadyToSendGroup(delayedMessage, false, true);
                return;
            }
        }
        delayedMessage.markAsError();
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$performSendDelayedMessage$52(final DelayedMessage delayedMessage, final String str, final TLObject tLObject, TLRPC.TL_error tL_error) {
        AndroidUtilities.runOnUIThread(new Runnable() { // from class: org.telegram.messenger.SendMessagesHelper$$ExternalSyntheticLambda20
            @Override // java.lang.Runnable
            public final void run() {
                this.f$0.lambda$performSendDelayedMessage$51(tLObject, delayedMessage, str);
            }
        });
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$performSendDelayedMessage$51(TLObject tLObject, DelayedMessage delayedMessage, String str) {
        boolean z;
        if (tLObject != null) {
            TLRPC.TL_messages_stickerSet tL_messages_stickerSet = (TLRPC.TL_messages_stickerSet) tLObject;
            getMediaDataController().storeTempStickerSet(tL_messages_stickerSet);
            TLRPC.TL_documentAttributeSticker_layer55 tL_documentAttributeSticker_layer55 = (TLRPC.TL_documentAttributeSticker_layer55) delayedMessage.locationParent;
            TLRPC.TL_inputStickerSetShortName tL_inputStickerSetShortName = new TLRPC.TL_inputStickerSetShortName();
            tL_documentAttributeSticker_layer55.stickerset = tL_inputStickerSetShortName;
            tL_inputStickerSetShortName.short_name = tL_messages_stickerSet.set.short_name;
            z = true;
        } else {
            z = false;
        }
        ArrayList<DelayedMessage> arrayListRemove = this.delayedMessages.remove(str);
        if (arrayListRemove == null || arrayListRemove.isEmpty()) {
            return;
        }
        if (z) {
            getMessagesStorage().replaceMessageIfExists(arrayListRemove.get(0).obj.messageOwner, null, null, false);
        }
        SecretChatHelper secretChatHelper = getSecretChatHelper();
        TLRPC.DecryptedMessage decryptedMessage = (TLRPC.DecryptedMessage) delayedMessage.sendEncryptedRequest;
        MessageObject messageObject = delayedMessage.obj;
        secretChatHelper.performSendEncryptedRequest(decryptedMessage, messageObject.messageOwner, delayedMessage.encryptedChat, null, null, messageObject);
    }

    private void uploadMultiMedia(final DelayedMessage delayedMessage, final TLRPC.InputMedia inputMedia, TLRPC.InputEncryptedFile inputEncryptedFile, String str) {
        if (inputMedia == null) {
            if (inputEncryptedFile != null) {
                TLRPC.TL_messages_sendEncryptedMultiMedia tL_messages_sendEncryptedMultiMedia = (TLRPC.TL_messages_sendEncryptedMultiMedia) delayedMessage.sendEncryptedRequest;
                for (int i = 0; i < tL_messages_sendEncryptedMultiMedia.files.size(); i++) {
                    if (tL_messages_sendEncryptedMultiMedia.files.get(i) == inputEncryptedFile) {
                        putToSendingMessages(delayedMessage.messages.get(i), delayedMessage.scheduled);
                        getNotificationCenter().lambda$postNotificationNameOnUIThread$1(NotificationCenter.fileUploadProgressChanged, str, -1L, -1L, Boolean.FALSE);
                        break;
                    }
                }
                sendReadyToSendGroup(delayedMessage, false, true);
                return;
            }
            return;
        }
        TLRPC.TL_messages_uploadMedia tL_messages_uploadMedia = new TLRPC.TL_messages_uploadMedia();
        tL_messages_uploadMedia.media = inputMedia;
        TLObject tLObject = delayedMessage.sendRequest;
        if (tLObject instanceof TLRPC.TL_messages_sendMultiMedia) {
            TLRPC.TL_messages_sendMultiMedia tL_messages_sendMultiMedia = (TLRPC.TL_messages_sendMultiMedia) tLObject;
            tL_messages_uploadMedia.peer = tL_messages_sendMultiMedia.peer;
            for (int i2 = 0; i2 < tL_messages_sendMultiMedia.multi_media.size(); i2++) {
                if (((TLRPC.TL_inputSingleMedia) tL_messages_sendMultiMedia.multi_media.get(i2)).media == inputMedia) {
                    putToSendingMessages(delayedMessage.messages.get(i2), delayedMessage.scheduled);
                    getNotificationCenter().lambda$postNotificationNameOnUIThread$1(NotificationCenter.fileUploadProgressChanged, str, -1L, -1L, Boolean.FALSE);
                    break;
                }
            }
        } else if ((tLObject instanceof TLRPC.TL_messages_sendMedia) && (((TLRPC.TL_messages_sendMedia) tLObject).media instanceof TLRPC.TL_inputMediaPaidMedia)) {
            TLRPC.TL_messages_sendMedia tL_messages_sendMedia = (TLRPC.TL_messages_sendMedia) tLObject;
            tL_messages_uploadMedia.peer = tL_messages_sendMedia.peer;
            TLRPC.TL_inputMediaPaidMedia tL_inputMediaPaidMedia = (TLRPC.TL_inputMediaPaidMedia) tL_messages_sendMedia.media;
            for (int i3 = 0; i3 < tL_inputMediaPaidMedia.extended_media.size(); i3++) {
                if (tL_inputMediaPaidMedia.extended_media.get(i3) == inputMedia) {
                    putToSendingMessages(delayedMessage.messages.get(i3), delayedMessage.scheduled);
                    getNotificationCenter().lambda$postNotificationNameOnUIThread$1(NotificationCenter.fileUploadProgressChanged, str, -1L, -1L, Boolean.FALSE);
                    break;
                }
            }
        }
        getConnectionsManager().sendRequest(tL_messages_uploadMedia, new RequestDelegate() { // from class: org.telegram.messenger.SendMessagesHelper$$ExternalSyntheticLambda36
            @Override // org.telegram.tgnet.RequestDelegate
            public final void run(TLObject tLObject2, TLRPC.TL_error tL_error) {
                this.f$0.lambda$uploadMultiMedia$54(inputMedia, delayedMessage, tLObject2, tL_error);
            }
        });
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$uploadMultiMedia$54(final TLRPC.InputMedia inputMedia, final DelayedMessage delayedMessage, final TLObject tLObject, TLRPC.TL_error tL_error) {
        AndroidUtilities.runOnUIThread(new Runnable() { // from class: org.telegram.messenger.SendMessagesHelper$$ExternalSyntheticLambda115
            @Override // java.lang.Runnable
            public final void run() {
                this.f$0.lambda$uploadMultiMedia$53(tLObject, inputMedia, delayedMessage);
            }
        });
    }

    /* JADX INFO: Access modifiers changed from: private */
    /* JADX WARN: Code duplicated, block: B:20:0x0083  */
    public /* synthetic */ void lambda$uploadMultiMedia$53(TLObject tLObject, TLRPC.InputMedia inputMedia, DelayedMessage delayedMessage) {
        TLRPC.InputMedia inputMedia2;
        TLRPC.TL_inputMediaPhoto tL_inputMediaPhoto;
        if (tLObject != null) {
            TLRPC.MessageMedia messageMedia = (TLRPC.MessageMedia) tLObject;
            if ((inputMedia instanceof TLRPC.TL_inputMediaUploadedPhoto) && (messageMedia instanceof TLRPC.TL_messageMediaPhoto)) {
                tL_inputMediaPhoto = new TLRPC.TL_inputMediaPhoto();
                TLRPC.TL_inputPhoto tL_inputPhoto = new TLRPC.TL_inputPhoto();
                tL_inputMediaPhoto.id = tL_inputPhoto;
                TLRPC.Photo photo = messageMedia.photo;
                tL_inputPhoto.id = photo.id;
                tL_inputPhoto.access_hash = photo.access_hash;
                tL_inputPhoto.file_reference = photo.file_reference;
                tL_inputMediaPhoto.spoiler = inputMedia.spoiler;
                if (BuildVars.DEBUG_VERSION) {
                    inputMedia2 = tL_inputMediaPhoto;
                    FileLog.d("set uploaded photo");
                    inputMedia2 = tL_inputMediaPhoto;
                }
            } else if ((inputMedia instanceof TLRPC.TL_inputMediaUploadedDocument) && (messageMedia instanceof TLRPC.TL_messageMediaDocument)) {
                TLRPC.TL_inputMediaDocument tL_inputMediaDocument = new TLRPC.TL_inputMediaDocument();
                TLRPC.TL_inputDocument tL_inputDocument = new TLRPC.TL_inputDocument();
                tL_inputMediaDocument.id = tL_inputDocument;
                TLRPC.Document document = messageMedia.document;
                tL_inputDocument.id = document.id;
                tL_inputDocument.access_hash = document.access_hash;
                tL_inputDocument.file_reference = document.file_reference;
                tL_inputMediaDocument.spoiler = inputMedia.spoiler;
                TLRPC.Photo photo2 = messageMedia.video_cover;
                if (photo2 != null) {
                    TLRPC.TL_inputPhoto tL_inputPhoto2 = new TLRPC.TL_inputPhoto();
                    tL_inputPhoto2.id = photo2.id;
                    tL_inputPhoto2.access_hash = photo2.access_hash;
                    tL_inputPhoto2.file_reference = photo2.file_reference;
                    tL_inputMediaDocument.flags |= 8;
                    tL_inputMediaDocument.video_cover = tL_inputPhoto2;
                }
                inputMedia2 = tL_inputMediaDocument;
                if (BuildVars.DEBUG_VERSION) {
                    FileLog.d("set uploaded document");
                    inputMedia2 = tL_inputMediaDocument;
                }
            } else {
                inputMedia2 = null;
            }
        } else {
            inputMedia2 = null;
        }
        if (inputMedia2 != null) {
            int i = inputMedia.ttl_seconds;
            if (i != 0) {
                inputMedia2.ttl_seconds = i;
                inputMedia2.flags |= 1;
            }
            TLObject tLObject2 = delayedMessage.sendRequest;
            if (tLObject2 instanceof TLRPC.TL_messages_sendMultiMedia) {
                TLRPC.TL_messages_sendMultiMedia tL_messages_sendMultiMedia = (TLRPC.TL_messages_sendMultiMedia) tLObject2;
                for (int i2 = 0; i2 < tL_messages_sendMultiMedia.multi_media.size(); i2++) {
                    if (((TLRPC.TL_inputSingleMedia) tL_messages_sendMultiMedia.multi_media.get(i2)).media == inputMedia) {
                        ((TLRPC.TL_inputSingleMedia) tL_messages_sendMultiMedia.multi_media.get(i2)).media = inputMedia2;
                        break;
                    }
                }
            } else if ((tLObject2 instanceof TLRPC.TL_messages_sendMedia) && (((TLRPC.TL_messages_sendMedia) tLObject2).media instanceof TLRPC.TL_inputMediaPaidMedia)) {
                TLRPC.TL_inputMediaPaidMedia tL_inputMediaPaidMedia = (TLRPC.TL_inputMediaPaidMedia) ((TLRPC.TL_messages_sendMedia) tLObject2).media;
                for (int i3 = 0; i3 < tL_inputMediaPaidMedia.extended_media.size(); i3++) {
                    if (tL_inputMediaPaidMedia.extended_media.get(i3) == inputMedia) {
                        tL_inputMediaPaidMedia.extended_media.set(i3, inputMedia2);
                        break;
                    }
                }
            }
            sendReadyToSendGroup(delayedMessage, false, true);
            return;
        }
        delayedMessage.markAsError();
    }

    private void sendReadyToSendGroup(DelayedMessage delayedMessage, boolean z, boolean z2) {
        DelayedMessage delayedMessageFindMaxDelayedMessageForMessageId;
        DelayedMessage delayedMessageFindMaxDelayedMessageForMessageId2;
        ArrayList<MessageObject> arrayList;
        int i;
        if (delayedMessage.messageObjects.isEmpty()) {
            delayedMessage.markAsError();
            return;
        }
        String str = "group_" + delayedMessage.groupId;
        int i2 = delayedMessage.finalGroupMessage;
        ArrayList<MessageObject> arrayList2 = delayedMessage.messageObjects;
        int i3 = 1;
        if (i2 != arrayList2.get(arrayList2.size() - 1).getId()) {
            if (z) {
                if (BuildVars.DEBUG_VERSION) {
                    FileLog.d("final message not added, add");
                }
                putToDelayedMessages(str, delayedMessage);
                return;
            } else {
                if (BuildVars.DEBUG_VERSION) {
                    FileLog.d("final message not added");
                    return;
                }
                return;
            }
        }
        int i4 = 0;
        if (z) {
            this.delayedMessages.remove(str);
            if (delayedMessage.scheduled) {
                i = i3;
            } else {
                MessageObject messageObject = delayedMessage.obj;
                if ((messageObject == null || !messageObject.isQuickReply()) && ((arrayList = delayedMessage.messageObjects) == null || arrayList.isEmpty() || !delayedMessage.messageObjects.get(0).isQuickReply())) {
                    i = 0;
                } else {
                    i3 = 5;
                    i = i3;
                }
            }
            if (delayedMessage.paidMedia) {
                ArrayList<MessageObject> arrayList3 = new ArrayList<>();
                arrayList3.add(delayedMessage.messageObjects.get(0));
                ArrayList<TLRPC.Message> arrayList4 = new ArrayList<>();
                arrayList4.add(delayedMessage.messages.get(0));
                getMessagesStorage().putMessages(arrayList4, false, true, false, 0, i, 0L);
                getMessagesController().updateInterfaceWithMessages(delayedMessage.peer, arrayList3, i);
            } else {
                getMessagesStorage().putMessages(delayedMessage.messages, false, true, false, 0, i, 0L);
                getMessagesController().updateInterfaceWithMessages(delayedMessage.peer, delayedMessage.messageObjects, i);
            }
            if (!delayedMessage.scheduled) {
                getNotificationCenter().lambda$postNotificationNameOnUIThread$1(NotificationCenter.dialogsNeedReload, new Object[0]);
            }
            if (BuildVars.DEBUG_VERSION) {
                FileLog.d("add message");
            }
        }
        TLObject tLObject = delayedMessage.sendRequest;
        if (tLObject instanceof TLRPC.TL_messages_sendMultiMedia) {
            TLRPC.TL_messages_sendMultiMedia tL_messages_sendMultiMedia = (TLRPC.TL_messages_sendMultiMedia) tLObject;
            while (i4 < tL_messages_sendMultiMedia.multi_media.size()) {
                TLRPC.InputMedia inputMedia = ((TLRPC.TL_inputSingleMedia) tL_messages_sendMultiMedia.multi_media.get(i4)).media;
                if ((inputMedia instanceof TLRPC.TL_inputMediaUploadedPhoto) || (inputMedia instanceof TLRPC.TL_inputMediaUploadedDocument)) {
                    if (BuildVars.DEBUG_VERSION) {
                        FileLog.d("multi media not ready");
                        return;
                    }
                    return;
                }
                if ((inputMedia instanceof TLRPC.TL_inputMediaDocument) && i4 < delayedMessage.messageObjects.size()) {
                    MessageObject messageObject2 = delayedMessage.messageObjects.get(i4);
                    String str2 = messageObject2.messageOwner.attachPath;
                    if (str2 == null) {
                        str2 = FileLoader.getDirectory(4) + "/" + messageObject2.getDocument().id + ".mp4";
                    }
                    if (delayedMessage.extraHashMap.containsKey(str2 + "_ct") && inputMedia.video_cover == null) {
                        if (BuildVars.DEBUG_VERSION) {
                            FileLog.d("cover media not ready");
                            return;
                        }
                        return;
                    }
                }
                i4++;
            }
            if (z2 && (delayedMessageFindMaxDelayedMessageForMessageId2 = findMaxDelayedMessageForMessageId(delayedMessage.finalGroupMessage, delayedMessage.peer)) != null) {
                delayedMessageFindMaxDelayedMessageForMessageId2.addDelayedRequest(delayedMessage.sendRequest, delayedMessage.messageObjects, delayedMessage.originalPaths, delayedMessage.parentObjects, delayedMessage, delayedMessage.scheduled);
                ArrayList<DelayedMessageSendAfterRequest> arrayList5 = delayedMessage.requests;
                if (arrayList5 != null) {
                    delayedMessageFindMaxDelayedMessageForMessageId2.requests.addAll(arrayList5);
                }
                if (BuildVars.DEBUG_VERSION) {
                    FileLog.d("has maxDelayedMessage, delay");
                    return;
                }
                return;
            }
        } else if ((tLObject instanceof TLRPC.TL_messages_sendMedia) && (((TLRPC.TL_messages_sendMedia) tLObject).media instanceof TLRPC.TL_inputMediaPaidMedia)) {
            TLRPC.TL_inputMediaPaidMedia tL_inputMediaPaidMedia = (TLRPC.TL_inputMediaPaidMedia) ((TLRPC.TL_messages_sendMedia) tLObject).media;
            while (i4 < tL_inputMediaPaidMedia.extended_media.size()) {
                TLRPC.InputMedia inputMedia2 = (TLRPC.InputMedia) tL_inputMediaPaidMedia.extended_media.get(i4);
                if ((inputMedia2 instanceof TLRPC.TL_inputMediaUploadedPhoto) || (inputMedia2 instanceof TLRPC.TL_inputMediaUploadedDocument)) {
                    if (BuildVars.DEBUG_VERSION) {
                        FileLog.d("multi media not ready");
                        return;
                    }
                    return;
                }
                i4++;
            }
            if (z2 && (delayedMessageFindMaxDelayedMessageForMessageId = findMaxDelayedMessageForMessageId(delayedMessage.finalGroupMessage, delayedMessage.peer)) != null) {
                delayedMessageFindMaxDelayedMessageForMessageId.addDelayedRequest(delayedMessage.sendRequest, delayedMessage.messageObjects, delayedMessage.originalPaths, delayedMessage.parentObjects, delayedMessage, delayedMessage.scheduled);
                ArrayList<DelayedMessageSendAfterRequest> arrayList6 = delayedMessage.requests;
                if (arrayList6 != null) {
                    delayedMessageFindMaxDelayedMessageForMessageId.requests.addAll(arrayList6);
                }
                if (BuildVars.DEBUG_VERSION) {
                    FileLog.d("has maxDelayedMessage, delay");
                    return;
                }
                return;
            }
        } else {
            TLRPC.TL_messages_sendEncryptedMultiMedia tL_messages_sendEncryptedMultiMedia = (TLRPC.TL_messages_sendEncryptedMultiMedia) delayedMessage.sendEncryptedRequest;
            while (i4 < tL_messages_sendEncryptedMultiMedia.files.size()) {
                if (((TLRPC.InputEncryptedFile) tL_messages_sendEncryptedMultiMedia.files.get(i4)) instanceof TLRPC.TL_inputEncryptedFile) {
                    return;
                } else {
                    i4++;
                }
            }
        }
        TLObject tLObject2 = delayedMessage.sendRequest;
        if (tLObject2 instanceof TLRPC.TL_messages_sendMultiMedia) {
            lambda$performSendMessageRequestMulti$57((TLRPC.TL_messages_sendMultiMedia) tLObject2, delayedMessage.messageObjects, delayedMessage.originalPaths, delayedMessage.parentObjects, delayedMessage, delayedMessage.scheduled);
        } else if (tLObject2 instanceof TLRPC.TL_messages_sendMedia) {
            lambda$performSendMessageRequestMulti$57((TLRPC.TL_messages_sendMedia) tLObject2, delayedMessage.messageObjects, delayedMessage.originalPaths, delayedMessage.parentObjects, delayedMessage, delayedMessage.scheduled);
        } else {
            getSecretChatHelper().performSendEncryptedRequest((TLRPC.TL_messages_sendEncryptedMultiMedia) delayedMessage.sendEncryptedRequest, delayedMessage);
        }
        delayedMessage.sendDelayedRequests();
    }

    protected void putToSendingMessages(final TLRPC.Message message, final boolean z) {
        if (Thread.currentThread() != ApplicationLoader.applicationHandler.getLooper().getThread()) {
            AndroidUtilities.runOnUIThread(new Runnable() { // from class: org.telegram.messenger.SendMessagesHelper$$ExternalSyntheticLambda119
                @Override // java.lang.Runnable
                public final void run() {
                    this.f$0.lambda$putToSendingMessages$55(message, z);
                }
            });
        } else {
            putToSendingMessages(message, z, true);
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$putToSendingMessages$55(TLRPC.Message message, boolean z) {
        putToSendingMessages(message, z, true);
    }

    protected void putToSendingMessages(TLRPC.Message message, boolean z, boolean z2) {
        if (message == null) {
            return;
        }
        int i = message.id;
        if (i > 0) {
            this.editingMessages.put(i, message);
            return;
        }
        boolean z3 = this.sendingMessages.indexOfKey(i) >= 0;
        removeFromUploadingMessages(message.id, z);
        this.sendingMessages.put(message.id, message);
        if (z || z3) {
            return;
        }
        long dialogId = MessageObject.getDialogId(message);
        LongSparseArray longSparseArray = this.sendingMessagesIdDialogs;
        longSparseArray.put(dialogId, Integer.valueOf(((Integer) longSparseArray.get(dialogId, 0)).intValue() + 1));
        if (z2) {
            getNotificationCenter().lambda$postNotificationNameOnUIThread$1(NotificationCenter.sendingMessagesChanged, new Object[0]);
        }
    }

    protected TLRPC.Message removeFromSendingMessages(int i, boolean z) {
        if (i > 0) {
            TLRPC.Message message = this.editingMessages.get(i);
            if (message != null) {
                this.editingMessages.remove(i);
            }
            return message;
        }
        TLRPC.Message message2 = this.sendingMessages.get(i);
        if (message2 != null) {
            this.sendingMessages.remove(i);
            if (!z) {
                long dialogId = MessageObject.getDialogId(message2);
                Integer num = (Integer) this.sendingMessagesIdDialogs.get(dialogId);
                if (num != null) {
                    int iIntValue = num.intValue() - 1;
                    if (iIntValue <= 0) {
                        this.sendingMessagesIdDialogs.remove(dialogId);
                    } else {
                        this.sendingMessagesIdDialogs.put(dialogId, Integer.valueOf(iIntValue));
                    }
                    getNotificationCenter().lambda$postNotificationNameOnUIThread$1(NotificationCenter.sendingMessagesChanged, new Object[0]);
                }
            }
        }
        return message2;
    }

    public int getSendingMessageId(long j) {
        for (int i = 0; i < this.sendingMessages.size(); i++) {
            TLRPC.Message messageValueAt = this.sendingMessages.valueAt(i);
            if (messageValueAt.dialog_id == j) {
                return messageValueAt.id;
            }
        }
        for (int i2 = 0; i2 < this.uploadMessages.size(); i2++) {
            TLRPC.Message messageValueAt2 = this.uploadMessages.valueAt(i2);
            if (messageValueAt2.dialog_id == j) {
                return messageValueAt2.id;
            }
        }
        return 0;
    }

    protected void putToUploadingMessages(MessageObject messageObject) {
        if (messageObject == null || messageObject.getId() > 0 || messageObject.scheduled) {
            return;
        }
        TLRPC.Message message = messageObject.messageOwner;
        boolean z = this.uploadMessages.indexOfKey(message.id) >= 0;
        this.uploadMessages.put(message.id, message);
        if (z) {
            return;
        }
        long dialogId = MessageObject.getDialogId(message);
        LongSparseArray longSparseArray = this.uploadingMessagesIdDialogs;
        longSparseArray.put(dialogId, Integer.valueOf(((Integer) longSparseArray.get(dialogId, 0)).intValue() + 1));
        getNotificationCenter().lambda$postNotificationNameOnUIThread$1(NotificationCenter.sendingMessagesChanged, new Object[0]);
    }

    protected void removeFromUploadingMessages(int i, boolean z) {
        TLRPC.Message message;
        if (i > 0 || z || (message = this.uploadMessages.get(i)) == null) {
            return;
        }
        this.uploadMessages.remove(i);
        long dialogId = MessageObject.getDialogId(message);
        Integer num = (Integer) this.uploadingMessagesIdDialogs.get(dialogId);
        if (num != null) {
            int iIntValue = num.intValue() - 1;
            if (iIntValue <= 0) {
                this.uploadingMessagesIdDialogs.remove(dialogId);
            } else {
                this.uploadingMessagesIdDialogs.put(dialogId, Integer.valueOf(iIntValue));
            }
            getNotificationCenter().lambda$postNotificationNameOnUIThread$1(NotificationCenter.sendingMessagesChanged, new Object[0]);
        }
    }

    public boolean isSendingMessage(int i) {
        return this.sendingMessages.indexOfKey(i) >= 0 || this.editingMessages.indexOfKey(i) >= 0;
    }

    public boolean isSendingPaidMessage(int i, int i2) {
        HashMap<String, ArrayList<DelayedMessage>> map = this.delayedMessages;
        DelayedMessage delayedMessage = null;
        if (map != null) {
            for (ArrayList<DelayedMessage> arrayList : map.values()) {
                if (arrayList != null) {
                    int size = arrayList.size();
                    int i3 = 0;
                    while (i3 < size) {
                        DelayedMessage delayedMessage2 = arrayList.get(i3);
                        i3++;
                        DelayedMessage delayedMessage3 = delayedMessage2;
                        ArrayList<TLRPC.Message> arrayList2 = delayedMessage3.messages;
                        if (arrayList2 != null) {
                            int size2 = arrayList2.size();
                            int i4 = 0;
                            while (i4 < size2) {
                                TLRPC.Message message = arrayList2.get(i4);
                                i4++;
                                TLRPC.Message message2 = message;
                                if (message2 != null && message2.id == i) {
                                    delayedMessage = delayedMessage3;
                                    break;
                                }
                            }
                            if (delayedMessage != null) {
                                break;
                            }
                        }
                    }
                    if (delayedMessage != null) {
                        break;
                    }
                }
            }
        }
        if (delayedMessage != null && i2 >= 0 && i2 < delayedMessage.messages.size()) {
            i = delayedMessage.messages.get(i2).id;
        }
        return this.sendingMessages.indexOfKey(i) >= 0 || this.editingMessages.indexOfKey(i) >= 0;
    }

    public boolean isSendingMessageIdDialog(long j) {
        return ((Integer) this.sendingMessagesIdDialogs.get(j, 0)).intValue() > 0;
    }

    public boolean isUploadingMessageIdDialog(long j) {
        return ((Integer) this.uploadingMessagesIdDialogs.get(j, 0)).intValue() > 0;
    }

    /* JADX INFO: Access modifiers changed from: protected */
    /* JADX INFO: renamed from: performSendMessageRequestMulti, reason: merged with bridge method [inline-methods] and merged with bridge method [inline-methods] */
    public void lambda$performSendMessageRequestMulti$57(final TLObject tLObject, final ArrayList<MessageObject> arrayList, final ArrayList<String> arrayList2, final ArrayList<Object> arrayList3, final DelayedMessage delayedMessage, final boolean z) {
        int size = arrayList.size();
        for (int i = 0; i < size; i++) {
            putToSendingMessages(arrayList.get(i).messageOwner, z);
        }
        if (StarsController.getInstance(this.currentAccount).beforeSendingFinalRequest(tLObject, arrayList, new Runnable() { // from class: org.telegram.messenger.SendMessagesHelper$$ExternalSyntheticLambda70
            @Override // java.lang.Runnable
            public final void run() {
                this.f$0.lambda$performSendMessageRequestMulti$56(tLObject, arrayList, arrayList2, arrayList3, delayedMessage, z);
            }
        }) && BotForumHelper.getInstance(this.currentAccount).beforeSendingFinalRequest(tLObject, arrayList, new Runnable() { // from class: org.telegram.messenger.SendMessagesHelper$$ExternalSyntheticLambda71
            @Override // java.lang.Runnable
            public final void run() {
                this.f$0.lambda$performSendMessageRequestMulti$57(tLObject, arrayList, arrayList2, arrayList3, delayedMessage, z);
            }
        })) {
            getConnectionsManager().sendRequest(tLObject, new RequestDelegate() { // from class: org.telegram.messenger.SendMessagesHelper$$ExternalSyntheticLambda72
                @Override // org.telegram.tgnet.RequestDelegate
                public final void run(TLObject tLObject2, TLRPC.TL_error tL_error) {
                    this.f$0.lambda$performSendMessageRequestMulti$66(arrayList3, tLObject, arrayList, arrayList2, delayedMessage, z, tLObject2, tL_error);
                }
            }, (QuickAckDelegate) null, 68);
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$performSendMessageRequestMulti$66(ArrayList arrayList, final TLObject tLObject, final ArrayList arrayList2, final ArrayList arrayList3, final DelayedMessage delayedMessage, final boolean z, final TLObject tLObject2, final TLRPC.TL_error tL_error) {
        if (tL_error != null && FileRefController.isFileRefError(tL_error.text)) {
            final int fileRefErrorIndex = FileRefController.getFileRefErrorIndex(tL_error.text);
            if (arrayList != null) {
                ArrayList arrayList4 = new ArrayList(arrayList);
                if (fileRefErrorIndex >= 0) {
                    int i = 0;
                    while (i < arrayList4.size()) {
                        arrayList4.set(i, fileRefErrorIndex == i ? arrayList4.get(i) : null);
                        i++;
                    }
                }
                getFileRefController().requestReference(arrayList4, tLObject, arrayList2, arrayList3, arrayList4, delayedMessage, Boolean.valueOf(z));
                return;
            }
            if (delayedMessage != null && !delayedMessage.getRetriedToSend(fileRefErrorIndex)) {
                delayedMessage.setRetriedToSend(fileRefErrorIndex, true);
                AndroidUtilities.runOnUIThread(new Runnable() { // from class: org.telegram.messenger.SendMessagesHelper$$ExternalSyntheticLambda31
                    @Override // java.lang.Runnable
                    public final void run() {
                        this.f$0.lambda$performSendMessageRequestMulti$58(tLObject, fileRefErrorIndex, delayedMessage, arrayList2, z);
                    }
                });
                return;
            }
        }
        AndroidUtilities.runOnUIThread(new Runnable() { // from class: org.telegram.messenger.SendMessagesHelper$$ExternalSyntheticLambda32
            @Override // java.lang.Runnable
            public final void run() {
                this.f$0.lambda$performSendMessageRequestMulti$65(tL_error, tLObject2, z, arrayList2, arrayList3, tLObject);
            }
        });
    }

    /* JADX INFO: Access modifiers changed from: private */
    /* JADX WARN: Code duplicated, block: B:12:0x0020  */
    /* JADX WARN: Code duplicated, block: B:21:0x007c  */
    /* JADX WARN: Code duplicated, block: B:34:0x00a5  */
    /* JADX WARN: Code duplicated, block: B:36:0x00be  */
    /* JADX WARN: Code duplicated, block: B:38:0x00cd  */
    /* JADX WARN: Code duplicated, block: B:40:0x00d1  */
    /* JADX WARN: Code duplicated, block: B:43:0x0103 A[ADDED_TO_REGION] */
    /* JADX WARN: Code duplicated, block: B:44:0x0105  */
    public /* synthetic */ void lambda$performSendMessageRequestMulti$58(TLObject tLObject, int i, DelayedMessage delayedMessage, ArrayList arrayList, boolean z) {
        boolean z2;
        TLRPC.InputMedia inputMedia;
        TLRPC.InputMedia inputMedia2;
        TLRPC.TL_inputSingleMedia tL_inputSingleMedia;
        TLRPC.InputMedia inputMedia3;
        if (tLObject instanceof TLRPC.TL_messages_sendMultiMedia) {
            TLRPC.TL_messages_sendMultiMedia tL_messages_sendMultiMedia = (TLRPC.TL_messages_sendMultiMedia) tLObject;
            int size = tL_messages_sendMultiMedia.multi_media.size();
            int i2 = 0;
            z2 = false;
            while (i2 < size) {
                if (i >= 0) {
                    if (i == i2) {
                        removeFromSendingMessages(((MessageObject) arrayList.get(i2)).getId(), z);
                        tL_inputSingleMedia = (TLRPC.TL_inputSingleMedia) tL_messages_sendMultiMedia.multi_media.get(i2);
                        inputMedia3 = tL_inputSingleMedia.media;
                        if ((inputMedia3 instanceof TLRPC.TL_inputMediaPhoto) || (inputMedia3 instanceof TLRPC.TL_inputMediaDocument)) {
                            tL_inputSingleMedia.media = delayedMessage.inputMedias.get(i2);
                        }
                        delayedMessage.videoEditedInfo = delayedMessage.videoEditedInfos.get(i2);
                        delayedMessage.httpLocation = delayedMessage.httpLocations.get(i2);
                        TLRPC.PhotoSize photoSize = delayedMessage.locations.get(i2);
                        delayedMessage.photoSize = photoSize;
                        delayedMessage.performMediaUpload = true;
                        z2 = z2;
                        if (tL_inputSingleMedia.media.file != null || photoSize != null) {
                            z2 = true;
                        }
                        performSendDelayedMessage(delayedMessage, i2);
                    }
                } else if (delayedMessage.parentObjects.get(i2) != null) {
                    removeFromSendingMessages(((MessageObject) arrayList.get(i2)).getId(), z);
                    tL_inputSingleMedia = (TLRPC.TL_inputSingleMedia) tL_messages_sendMultiMedia.multi_media.get(i2);
                    inputMedia3 = tL_inputSingleMedia.media;
                    if (inputMedia3 instanceof TLRPC.TL_inputMediaPhoto) {
                        tL_inputSingleMedia.media = delayedMessage.inputMedias.get(i2);
                    } else {
                        tL_inputSingleMedia.media = delayedMessage.inputMedias.get(i2);
                    }
                    delayedMessage.videoEditedInfo = delayedMessage.videoEditedInfos.get(i2);
                    delayedMessage.httpLocation = delayedMessage.httpLocations.get(i2);
                    TLRPC.PhotoSize photoSize2 = delayedMessage.locations.get(i2);
                    delayedMessage.photoSize = photoSize2;
                    delayedMessage.performMediaUpload = true;
                    z2 = z2;
                    if (tL_inputSingleMedia.media.file != null) {
                        z2 = true;
                    } else {
                        z2 = true;
                    }
                    performSendDelayedMessage(delayedMessage, i2);
                }
                i2++;
                z2 = z2;
            }
        } else if (tLObject instanceof TLRPC.TL_messages_sendMedia) {
            TLRPC.TL_inputMediaPaidMedia tL_inputMediaPaidMedia = (TLRPC.TL_inputMediaPaidMedia) ((TLRPC.TL_messages_sendMedia) tLObject).media;
            int size2 = tL_inputMediaPaidMedia.extended_media.size();
            int i3 = 0;
            z2 = false;
            while (i3 < size2) {
                if (i >= 0) {
                    if (i == i3) {
                        removeFromSendingMessages(((MessageObject) arrayList.get(i3)).getId(), z);
                        inputMedia = (TLRPC.InputMedia) tL_inputMediaPaidMedia.extended_media.get(i3);
                        if (inputMedia instanceof TLRPC.TL_inputMediaPhoto) {
                            ArrayList arrayList2 = tL_inputMediaPaidMedia.extended_media;
                            inputMedia2 = delayedMessage.inputMedias.get(i3);
                            arrayList2.set(i3, inputMedia2);
                        } else {
                            if (inputMedia instanceof TLRPC.TL_inputMediaDocument) {
                                ArrayList arrayList3 = tL_inputMediaPaidMedia.extended_media;
                                inputMedia2 = delayedMessage.inputMedias.get(i3);
                                arrayList3.set(i3, inputMedia2);
                            }
                            delayedMessage.videoEditedInfo = delayedMessage.videoEditedInfos.get(i3);
                            delayedMessage.httpLocation = delayedMessage.httpLocations.get(i3);
                            TLRPC.PhotoSize photoSize3 = delayedMessage.locations.get(i3);
                            delayedMessage.photoSize = photoSize3;
                            delayedMessage.performMediaUpload = true;
                            z2 = z2;
                            if (inputMedia.file != null || photoSize3 != null) {
                                z2 = true;
                            }
                            performSendDelayedMessage(delayedMessage, i3);
                        }
                        inputMedia = inputMedia2;
                        delayedMessage.videoEditedInfo = delayedMessage.videoEditedInfos.get(i3);
                        delayedMessage.httpLocation = delayedMessage.httpLocations.get(i3);
                        TLRPC.PhotoSize photoSize4 = delayedMessage.locations.get(i3);
                        delayedMessage.photoSize = photoSize4;
                        delayedMessage.performMediaUpload = true;
                        z2 = z2;
                        if (inputMedia.file != null) {
                            z2 = true;
                        } else {
                            z2 = true;
                        }
                        performSendDelayedMessage(delayedMessage, i3);
                    }
                } else if (delayedMessage.parentObjects.get(i3) != null) {
                    removeFromSendingMessages(((MessageObject) arrayList.get(i3)).getId(), z);
                    inputMedia = (TLRPC.InputMedia) tL_inputMediaPaidMedia.extended_media.get(i3);
                    if (inputMedia instanceof TLRPC.TL_inputMediaPhoto) {
                        ArrayList arrayList4 = tL_inputMediaPaidMedia.extended_media;
                        inputMedia2 = delayedMessage.inputMedias.get(i3);
                        arrayList4.set(i3, inputMedia2);
                    } else {
                        if (inputMedia instanceof TLRPC.TL_inputMediaDocument) {
                            ArrayList arrayList5 = tL_inputMediaPaidMedia.extended_media;
                            inputMedia2 = delayedMessage.inputMedias.get(i3);
                            arrayList5.set(i3, inputMedia2);
                        }
                        delayedMessage.videoEditedInfo = delayedMessage.videoEditedInfos.get(i3);
                        delayedMessage.httpLocation = delayedMessage.httpLocations.get(i3);
                        TLRPC.PhotoSize photoSize5 = delayedMessage.locations.get(i3);
                        delayedMessage.photoSize = photoSize5;
                        delayedMessage.performMediaUpload = true;
                        z2 = z2;
                        if (inputMedia.file != null) {
                            z2 = true;
                        } else {
                            z2 = true;
                        }
                        performSendDelayedMessage(delayedMessage, i3);
                    }
                    inputMedia = inputMedia2;
                    delayedMessage.videoEditedInfo = delayedMessage.videoEditedInfos.get(i3);
                    delayedMessage.httpLocation = delayedMessage.httpLocations.get(i3);
                    TLRPC.PhotoSize photoSize6 = delayedMessage.locations.get(i3);
                    delayedMessage.photoSize = photoSize6;
                    delayedMessage.performMediaUpload = true;
                    z2 = z2;
                    if (inputMedia.file != null) {
                        z2 = true;
                    } else {
                        z2 = true;
                    }
                    performSendDelayedMessage(delayedMessage, i3);
                }
                i3++;
                z2 = z2;
            }
        } else {
            z2 = false;
        }
        if (z2) {
            return;
        }
        for (int i4 = 0; i4 < arrayList.size(); i4++) {
            TLRPC.Message message = ((MessageObject) arrayList.get(i4)).messageOwner;
            getMessagesStorage().markMessageAsSendError(message, z ? 1 : 0);
            message.send_state = 2;
            message.errorAllowedPriceStars = 0L;
            message.errorNewPriceStars = 0L;
            getNotificationCenter().lambda$postNotificationNameOnUIThread$1(NotificationCenter.messageSendError, Integer.valueOf(message.id));
            processSentMessage(message.id);
            removeFromSendingMessages(message.id, z);
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    /* JADX WARN: Multi-variable type inference failed */
    /* JADX WARN: Type inference failed for: r8v1 */
    /* JADX WARN: Type inference failed for: r8v2, types: [boolean, int] */
    public /* synthetic */ void lambda$performSendMessageRequestMulti$65(TLRPC.TL_error tL_error, TLObject tLObject, final boolean z, ArrayList arrayList, ArrayList arrayList2, TLObject tLObject2) {
        boolean z2;
        TLRPC.TL_error tL_error2;
        TLObject tLObject3;
        boolean z3;
        SendMessagesHelper sendMessagesHelper;
        int i;
        String str;
        int i2;
        int i3;
        final SendMessagesHelper sendMessagesHelper2;
        TLRPC.Message message;
        TLRPC.Message message2;
        ArrayList arrayList3;
        TLRPC.Message message3;
        MessageObject messageObject;
        SparseArray sparseArray;
        int i4;
        String quickReplyName;
        TLRPC.MessageReplyHeader messageReplyHeader;
        final SendMessagesHelper sendMessagesHelper3 = this;
        ArrayList arrayList4 = arrayList;
        if (tL_error == null) {
            SparseArray sparseArray2 = new SparseArray();
            LongSparseArray longSparseArray = new LongSparseArray();
            TLRPC.Updates updates = (TLRPC.Updates) tLObject;
            ArrayList<TLRPC.Update> arrayList5 = updates.updates;
            boolean z4 = z ? 1 : 0;
            int i5 = 0;
            LongSparseArray longSparseArray2 = null;
            while (i5 < arrayList5.size()) {
                TLRPC.Update update = arrayList5.get(i5);
                if (update instanceof TLRPC.TL_updateMessageID) {
                    TLRPC.TL_updateMessageID tL_updateMessageID = (TLRPC.TL_updateMessageID) update;
                    longSparseArray.put(tL_updateMessageID.random_id, Integer.valueOf(tL_updateMessageID.id));
                    arrayList5.remove(i5);
                    i5--;
                    sparseArray = sparseArray2;
                } else {
                    if (update instanceof TLRPC.TL_updateNewMessage) {
                        final TLRPC.TL_updateNewMessage tL_updateNewMessage = (TLRPC.TL_updateNewMessage) update;
                        TLRPC.Message message4 = tL_updateNewMessage.message;
                        sparseArray2.put(message4.id, message4);
                        Utilities.stageQueue.postRunnable(new Runnable() { // from class: org.telegram.messenger.SendMessagesHelper$$ExternalSyntheticLambda56
                            @Override // java.lang.Runnable
                            public final void run() {
                                this.f$0.lambda$performSendMessageRequestMulti$59(tL_updateNewMessage);
                            }
                        });
                        arrayList5.remove(i5);
                        i5--;
                        sparseArray = sparseArray2;
                    } else if (update instanceof TLRPC.TL_updateNewChannelMessage) {
                        final TLRPC.TL_updateNewChannelMessage tL_updateNewChannelMessage = (TLRPC.TL_updateNewChannelMessage) update;
                        final long updateChannelId = MessagesController.getUpdateChannelId(tL_updateNewChannelMessage);
                        TLRPC.Chat chat = sendMessagesHelper3.getMessagesController().getChat(Long.valueOf(updateChannelId));
                        if ((chat == null || chat.megagroup) && (messageReplyHeader = tL_updateNewChannelMessage.message.reply_to) != null && (messageReplyHeader.reply_to_top_id != 0 || messageReplyHeader.reply_to_msg_id != 0)) {
                            if (longSparseArray2 == null) {
                                longSparseArray2 = new LongSparseArray();
                            }
                            long dialogId = MessageObject.getDialogId(tL_updateNewChannelMessage.message);
                            SparseArray sparseArray3 = (SparseArray) longSparseArray2.get(dialogId);
                            if (sparseArray3 == null) {
                                sparseArray3 = new SparseArray();
                                longSparseArray2.put(dialogId, sparseArray3);
                            }
                            TLRPC.MessageReplyHeader messageReplyHeader2 = tL_updateNewChannelMessage.message.reply_to;
                            int i6 = messageReplyHeader2.reply_to_top_id;
                            if (i6 == 0) {
                                i6 = messageReplyHeader2.reply_to_msg_id;
                            }
                            TLRPC.MessageReplies tL_messageReplies = (TLRPC.MessageReplies) sparseArray3.get(i6);
                            if (tL_messageReplies == null) {
                                tL_messageReplies = new TLRPC.TL_messageReplies();
                                sparseArray3.put(i6, tL_messageReplies);
                            }
                            TLRPC.Peer peer = tL_updateNewChannelMessage.message.from_id;
                            if (peer != null) {
                                tL_messageReplies.recent_repliers.add(0, peer);
                            }
                            tL_messageReplies.replies++;
                        }
                        TLRPC.Message message5 = tL_updateNewChannelMessage.message;
                        sparseArray = sparseArray2;
                        sparseArray.put(message5.id, message5);
                        Utilities.stageQueue.postRunnable(new Runnable() { // from class: org.telegram.messenger.SendMessagesHelper$$ExternalSyntheticLambda57
                            @Override // java.lang.Runnable
                            public final void run() {
                                this.f$0.lambda$performSendMessageRequestMulti$60(tL_updateNewChannelMessage);
                            }
                        });
                        arrayList5.remove(i5);
                        i5--;
                        if (tL_updateNewChannelMessage.message.pinned) {
                            Utilities.stageQueue.postRunnable(new Runnable() { // from class: org.telegram.messenger.SendMessagesHelper$$ExternalSyntheticLambda58
                                @Override // java.lang.Runnable
                                public final void run() {
                                    this.f$0.lambda$performSendMessageRequestMulti$61(tL_updateNewChannelMessage, updateChannelId);
                                }
                            });
                        }
                    } else {
                        sparseArray = sparseArray2;
                        if (update instanceof TLRPC.TL_updateNewScheduledMessage) {
                            TLRPC.Message message6 = ((TLRPC.TL_updateNewScheduledMessage) update).message;
                            sparseArray.put(message6.id, message6);
                            arrayList5.remove(i5);
                            i5--;
                            z4 = true;
                        } else if (update instanceof TLRPC.TL_updateQuickReplyMessage) {
                            QuickRepliesController quickRepliesController = QuickRepliesController.getInstance(sendMessagesHelper3.currentAccount);
                            if (arrayList4.isEmpty()) {
                                quickReplyName = null;
                                i4 = 0;
                            } else {
                                i4 = 0;
                                quickReplyName = ((MessageObject) arrayList4.get(0)).getQuickReplyName();
                            }
                            quickRepliesController.processUpdate(update, quickReplyName, (arrayList4.isEmpty() ? null : Integer.valueOf(((MessageObject) arrayList4.get(i4)).getQuickReplyId())).intValue());
                            TLRPC.Message message7 = ((TLRPC.TL_updateQuickReplyMessage) update).message;
                            sparseArray.put(message7.id, message7);
                            arrayList5.remove(i5);
                            i5--;
                        }
                    }
                    z4 = false;
                }
                i5++;
                sparseArray2 = sparseArray;
            }
            SparseArray sparseArray4 = sparseArray2;
            char c = 2;
            char c2 = 4;
            if (longSparseArray2 != null) {
                i2 = 1;
                sendMessagesHelper3.getMessagesStorage().putChannelViews(null, null, longSparseArray2, true);
                i3 = 0;
                sendMessagesHelper3.getNotificationCenter().lambda$postNotificationNameOnUIThread$1(NotificationCenter.didUpdateMessagesViews, null, null, longSparseArray2, Boolean.TRUE);
            } else {
                i2 = 1;
                i3 = 0;
            }
            int[] iArr = new int[i2];
            iArr[i3] = i3;
            int[] iArr2 = {i3};
            ArrayList arrayList6 = new ArrayList();
            int i7 = 0;
            SendMessagesHelper sendMessagesHelper4 = sendMessagesHelper3;
            while (true) {
                if (i7 >= arrayList4.size()) {
                    z3 = false;
                    sendMessagesHelper2 = sendMessagesHelper4;
                    break;
                }
                MessageObject messageObject2 = (MessageObject) arrayList4.get(i7);
                String str2 = (String) arrayList2.get(i7);
                TLRPC.Message message8 = messageObject2.messageOwner;
                char c3 = c2;
                final int i8 = message8.id;
                ArrayList arrayList7 = new ArrayList();
                final ArrayList arrayList8 = arrayList6;
                Integer num = (Integer) longSparseArray.get(message8.random_id);
                if (num == null || (message = (TLRPC.Message) sparseArray4.get(num.intValue())) == null) {
                    sendMessagesHelper2 = this;
                    z3 = true;
                    break;
                }
                MessageObject.getDialogId(message);
                arrayList7.add(message);
                if ((message.flags & 33554432) != 0) {
                    TLRPC.Message message9 = messageObject2.messageOwner;
                    message9.ttl_period = message.ttl_period;
                    message9.flags |= 33554432;
                }
                if (tLObject2 instanceof TLRPC.TL_messages_sendMedia) {
                    message3 = message8;
                    arrayList3 = arrayList7;
                    message2 = message;
                    updateMediaPaths((MessageObject) arrayList4.get(0), message2, message.id, arrayList2, false, -1);
                    messageObject = messageObject2;
                } else {
                    message2 = message;
                    arrayList3 = arrayList7;
                    message3 = message8;
                    messageObject = messageObject2;
                    updateMediaPaths(messageObject, message2, message2.id, str2, false);
                }
                TLRPC.Updates updates2 = updates;
                final int mediaExistanceFlags = messageObject.getMediaExistanceFlags();
                message3.id = message2.id;
                int i9 = message2.quick_reply_shortcut_id;
                message3.quick_reply_shortcut_id = i9;
                if (i9 != 0) {
                    message3.flags |= TLObject.FLAG_30;
                }
                final int[] iArr3 = iArr2;
                LongSparseArray longSparseArray3 = longSparseArray;
                final long j = message2.grouped_id;
                if (z) {
                    longSparseArray3 = longSparseArray3;
                } else {
                    Integer numValueOf = getMessagesController().dialogs_read_outbox_max.get(Long.valueOf(message2.dialog_id));
                    if (numValueOf == null) {
                        numValueOf = Integer.valueOf(getMessagesStorage().getDialogReadMax(message2.out, message2.dialog_id));
                        getMessagesController().dialogs_read_outbox_max.put(Long.valueOf(message2.dialog_id), numValueOf);
                    }
                    message2.unread = numValueOf.intValue() < message2.id;
                }
                iArr[0] = iArr[0] + 1;
                arrayList8.add(Integer.valueOf(i8));
                getStatsController().incrementSentItemsCount(ApplicationLoader.getCurrentNetworkType(), 1, 1);
                message3.send_state = 0;
                message3.errorAllowedPriceStars = 0L;
                message3.errorNewPriceStars = 0L;
                NotificationCenter notificationCenter = getNotificationCenter();
                int i10 = NotificationCenter.messageReceivedByServer;
                Integer numValueOf2 = Integer.valueOf(i8);
                Integer numValueOf3 = Integer.valueOf(message3.id);
                Long lValueOf = Long.valueOf(message3.dialog_id);
                Long lValueOf2 = Long.valueOf(j);
                Integer numValueOf4 = Integer.valueOf(mediaExistanceFlags);
                Boolean boolValueOf = Boolean.valueOf(z4);
                Object[] objArr = new Object[7];
                objArr[0] = numValueOf2;
                objArr[1] = numValueOf3;
                objArr[c] = message3;
                objArr[3] = lValueOf;
                objArr[c3] = lValueOf2;
                objArr[5] = numValueOf4;
                objArr[6] = boolValueOf;
                notificationCenter.lambda$postNotificationNameOnUIThread$1(i10, objArr);
                NotificationCenter notificationCenter2 = getNotificationCenter();
                int i11 = NotificationCenter.messageReceivedByServer2;
                Integer numValueOf5 = Integer.valueOf(i8);
                Integer numValueOf6 = Integer.valueOf(message3.id);
                Long lValueOf3 = Long.valueOf(message3.dialog_id);
                Long lValueOf4 = Long.valueOf(j);
                Integer numValueOf7 = Integer.valueOf(mediaExistanceFlags);
                Boolean boolValueOf2 = Boolean.valueOf(z4);
                Object[] objArr2 = new Object[7];
                objArr2[0] = numValueOf5;
                objArr2[1] = numValueOf6;
                objArr2[c] = message3;
                objArr2[3] = lValueOf3;
                objArr2[c3] = lValueOf4;
                objArr2[5] = numValueOf7;
                objArr2[6] = boolValueOf2;
                notificationCenter2.lambda$postNotificationNameOnUIThread$1(i11, objArr2);
                final SparseArray sparseArray5 = sparseArray4;
                final int[] iArr4 = iArr;
                final TLRPC.Message message10 = message3;
                final boolean z5 = z4;
                final MessageObject messageObject3 = messageObject;
                final ArrayList arrayList9 = arrayList3;
                sendMessagesHelper4 = this;
                getMessagesStorage().getStorageQueue().postRunnable(new Runnable() { // from class: org.telegram.messenger.SendMessagesHelper$$ExternalSyntheticLambda59
                    @Override // java.lang.Runnable
                    public final void run() {
                        this.f$0.lambda$performSendMessageRequestMulti$63(z5, message10, i8, arrayList9, iArr3, iArr4, z, messageObject3, sparseArray5, arrayList8, j, mediaExistanceFlags);
                    }
                });
                iArr2 = iArr3;
                updates = updates2;
                c2 = c3;
                longSparseArray = longSparseArray3;
                c = 2;
                z4 = z5 ? 1 : 0;
                i7++;
                arrayList6 = arrayList8;
                sparseArray4 = sparseArray5;
                iArr = iArr4;
                arrayList4 = arrayList;
            }
            final TLRPC.Updates updates3 = updates;
            Utilities.stageQueue.postRunnable(new Runnable() { // from class: org.telegram.messenger.SendMessagesHelper$$ExternalSyntheticLambda60
                @Override // java.lang.Runnable
                public final void run() {
                    this.f$0.lambda$performSendMessageRequestMulti$64(updates3);
                }
            });
            tL_error2 = tL_error;
            tLObject3 = tLObject2;
            sendMessagesHelper = sendMessagesHelper2;
            z2 = z;
        } else {
            z2 = z ? 1 : 0;
            tL_error2 = tL_error;
            tLObject3 = tLObject2;
            AlertsCreator.processError(sendMessagesHelper3.currentAccount, tL_error2, null, tLObject3, new Object[0]);
            z3 = true;
        }
        if (!z3) {
            sendMessagesHelper = sendMessagesHelper3;
            return;
        }
        sendMessagesHelper = sendMessagesHelper3;
        for (int i12 = 0; i12 < arrayList.size(); i12++) {
            MessageObject messageObject4 = (MessageObject) arrayList.get(i12);
            TLRPC.Message message11 = messageObject4.messageOwner;
            sendMessagesHelper.getMessagesStorage().markMessageAsSendError(message11, z2);
            message11.send_state = 2;
            if (z2 != 0 || tL_error2 == null || (str = tL_error2.text) == null || !str.startsWith("ALLOW_PAYMENT_REQUIRED_")) {
                i = 1;
            } else {
                StarsController.getInstance(sendMessagesHelper.currentAccount);
                message11.errorAllowedPriceStars = StarsController.getAllowedPaidStars(tLObject3);
                message11.errorNewPriceStars = Long.parseLong(tL_error2.text.substring(23));
                i = 1;
                StarsController.getInstance(sendMessagesHelper.currentAccount).showPriceChangedToast(Arrays.asList(messageObject4));
                sendMessagesHelper.getMessagesStorage().updateMessageCustomParams(MessageObject.getDialogId(message11), message11);
            }
            NotificationCenter notificationCenter3 = sendMessagesHelper.getNotificationCenter();
            int i13 = NotificationCenter.messageSendError;
            Object[] objArr3 = new Object[i];
            objArr3[0] = Integer.valueOf(message11.id);
            notificationCenter3.lambda$postNotificationNameOnUIThread$1(i13, objArr3);
            sendMessagesHelper.processSentMessage(message11.id);
            sendMessagesHelper.removeFromSendingMessages(message11.id, z2);
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$performSendMessageRequestMulti$59(TLRPC.TL_updateNewMessage tL_updateNewMessage) {
        getMessagesController().processNewDifferenceParams(-1, tL_updateNewMessage.pts, -1, tL_updateNewMessage.pts_count);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$performSendMessageRequestMulti$60(TLRPC.TL_updateNewChannelMessage tL_updateNewChannelMessage) {
        getMessagesController().processNewChannelDifferenceParams(tL_updateNewChannelMessage.pts, tL_updateNewChannelMessage.pts_count, tL_updateNewChannelMessage.message.peer_id.channel_id);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$performSendMessageRequestMulti$61(TLRPC.TL_updateNewChannelMessage tL_updateNewChannelMessage, long j) {
        ArrayList<Integer> arrayList = new ArrayList<>();
        arrayList.add(Integer.valueOf(tL_updateNewChannelMessage.message.id));
        getMessagesStorage().updatePinnedMessages(-j, arrayList, true, -1, 0, false, null);
    }

    /* JADX INFO: Access modifiers changed from: private */
    /* JADX WARN: Multi-variable type inference failed */
    /* JADX WARN: Type inference failed for: r10v0, types: [org.telegram.messenger.MessagesStorage] */
    /* JADX WARN: Type inference failed for: r16v1 */
    /* JADX WARN: Type inference failed for: r19v0 */
    /* JADX WARN: Type inference failed for: r19v1, types: [int] */
    /* JADX WARN: Type inference failed for: r19v2 */
    public /* synthetic */ void lambda$performSendMessageRequestMulti$63(final boolean z, final TLRPC.Message message, final int i, ArrayList arrayList, final int[] iArr, final int[] iArr2, final boolean z2, final MessageObject messageObject, final SparseArray sparseArray, final ArrayList arrayList2, final long j, final int i2) {
        ?? r19 = (message.quick_reply_shortcut_id == 0 && message.quick_reply_shortcut == null) ? z : 5;
        getMessagesStorage().updateMessageStateAndId(message.random_id, MessageObject.getPeerId(message.peer_id), Integer.valueOf(i), message.id, 0, false, r19, message.quick_reply_shortcut_id);
        getMessagesStorage().putMessages((ArrayList<TLRPC.Message>) arrayList, true, false, false, 0, r19 == true ? 1 : 0, message.quick_reply_shortcut_id);
        AndroidUtilities.runOnUIThread(new Runnable() { // from class: org.telegram.messenger.SendMessagesHelper$$ExternalSyntheticLambda17
            @Override // java.lang.Runnable
            public final void run() {
                this.f$0.lambda$performSendMessageRequestMulti$62(iArr, iArr2, z2, z, messageObject, sparseArray, arrayList2, message, i, j, i2);
            }
        });
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$performSendMessageRequestMulti$62(int[] iArr, int[] iArr2, boolean z, boolean z2, MessageObject messageObject, SparseArray sparseArray, ArrayList arrayList, TLRPC.Message message, int i, long j, int i2) {
        char c;
        int i3 = iArr[0] + 1;
        iArr[0] = i3;
        if (i3 != iArr2[0] || z == z2) {
            c = 1;
        } else {
            c = 1;
            getMessagesController().deleteMessages(arrayList, null, null, messageObject.getDialogId(), false, z ? 1 : 0, false, 0L, null, 0, z2 && !z, (!z2 || sparseArray.size() <= 1) ? 0 : sparseArray.keyAt(0));
        }
        getMediaDataController().increasePeerRaiting(message.dialog_id);
        NotificationCenter notificationCenter = getNotificationCenter();
        int i4 = NotificationCenter.messageReceivedByServer;
        Integer numValueOf = Integer.valueOf(i);
        Integer numValueOf2 = Integer.valueOf(message.id);
        Long lValueOf = Long.valueOf(message.dialog_id);
        Long lValueOf2 = Long.valueOf(j);
        Integer numValueOf3 = Integer.valueOf(i2);
        Boolean boolValueOf = Boolean.valueOf(z2);
        Object[] objArr = new Object[7];
        objArr[r2] = numValueOf;
        objArr[c] = numValueOf2;
        objArr[2] = message;
        objArr[3] = lValueOf;
        objArr[4] = lValueOf2;
        objArr[5] = numValueOf3;
        objArr[6] = boolValueOf;
        notificationCenter.lambda$postNotificationNameOnUIThread$1(i4, objArr);
        NotificationCenter notificationCenter2 = getNotificationCenter();
        int i5 = NotificationCenter.messageReceivedByServer2;
        Integer numValueOf4 = Integer.valueOf(i);
        Integer numValueOf5 = Integer.valueOf(message.id);
        Long lValueOf3 = Long.valueOf(message.dialog_id);
        Long lValueOf4 = Long.valueOf(j);
        Integer numValueOf6 = Integer.valueOf(i2);
        Boolean boolValueOf2 = Boolean.valueOf(z2);
        Object[] objArr2 = new Object[7];
        objArr2[0] = numValueOf4;
        objArr2[c] = numValueOf5;
        objArr2[2] = message;
        objArr2[3] = lValueOf3;
        objArr2[4] = lValueOf4;
        objArr2[5] = numValueOf6;
        objArr2[6] = boolValueOf2;
        notificationCenter2.lambda$postNotificationNameOnUIThread$1(i5, objArr2);
        processSentMessage(i);
        removeFromSendingMessages(i, z);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$performSendMessageRequestMulti$64(TLRPC.Updates updates) {
        getMessagesController().processUpdates(updates, false);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void performSendMessageRequest(TLObject tLObject, MessageObject messageObject, String str, DelayedMessage delayedMessage, Object obj, HashMap<String, String> map, boolean z) {
        lambda$performSendMessageRequest$71(tLObject, messageObject, str, null, false, delayedMessage, obj, map, z);
    }

    private DelayedMessage findMaxDelayedMessageForMessageId(int i, long j) {
        int id;
        Iterator<Map.Entry<String, ArrayList<DelayedMessage>>> it = this.delayedMessages.entrySet().iterator();
        DelayedMessage delayedMessage = null;
        int i2 = Integer.MIN_VALUE;
        while (it.hasNext()) {
            ArrayList<DelayedMessage> value = it.next().getValue();
            int size = value.size();
            for (int i3 = 0; i3 < size; i3++) {
                DelayedMessage delayedMessage2 = value.get(i3);
                int i4 = delayedMessage2.type;
                if ((i4 == 4 || i4 == 0) && delayedMessage2.peer == j) {
                    MessageObject messageObject = delayedMessage2.obj;
                    if (messageObject != null) {
                        id = messageObject.getId();
                    } else {
                        ArrayList<MessageObject> arrayList = delayedMessage2.messageObjects;
                        if (arrayList == null || arrayList.isEmpty()) {
                            id = 0;
                        } else {
                            ArrayList<MessageObject> arrayList2 = delayedMessage2.messageObjects;
                            id = arrayList2.get(arrayList2.size() - 1).getId();
                        }
                    }
                    if (id != 0 && id > i && delayedMessage == null && i2 < id) {
                        delayedMessage = delayedMessage2;
                        i2 = id;
                    }
                }
            }
        }
        return delayedMessage;
    }

    /* JADX INFO: Access modifiers changed from: protected */
    /* JADX INFO: renamed from: performSendMessageRequest, reason: merged with bridge method [inline-methods] and merged with bridge method [inline-methods] and merged with bridge method [inline-methods] */
    public void lambda$performSendMessageRequest$71(final TLObject tLObject, final MessageObject messageObject, final String str, final DelayedMessage delayedMessage, final boolean z, final DelayedMessage delayedMessage2, final Object obj, final HashMap<String, String> map, final boolean z2) {
        DelayedMessage delayedMessageFindMaxDelayedMessageForMessageId;
        ArrayList<DelayedMessageSendAfterRequest> arrayList;
        if (!(tLObject instanceof TLRPC.TL_messages_editMessage) && z && (delayedMessageFindMaxDelayedMessageForMessageId = findMaxDelayedMessageForMessageId(messageObject.getId(), messageObject.getDialogId())) != null) {
            delayedMessageFindMaxDelayedMessageForMessageId.addDelayedRequest(tLObject, messageObject, str, obj, delayedMessage2, delayedMessage != null ? delayedMessage.scheduled : false);
            if (delayedMessage == null || (arrayList = delayedMessage.requests) == null) {
                return;
            }
            delayedMessageFindMaxDelayedMessageForMessageId.requests.addAll(arrayList);
            return;
        }
        final TLRPC.Message message = messageObject.messageOwner;
        putToSendingMessages(message, z2);
        if (StarsController.getInstance(this.currentAccount).beforeSendingFinalRequest(tLObject, messageObject, new Runnable() { // from class: org.telegram.messenger.SendMessagesHelper$$ExternalSyntheticLambda86
            @Override // java.lang.Runnable
            public final void run() {
                this.f$0.lambda$performSendMessageRequest$67(tLObject, messageObject, str, delayedMessage, z, delayedMessage2, obj, map, z2);
            }
        }) && BotForumHelper.getInstance(this.currentAccount).beforeSendingFinalRequest(tLObject, messageObject, new Runnable() { // from class: org.telegram.messenger.SendMessagesHelper$$ExternalSyntheticLambda87
            @Override // java.lang.Runnable
            public final void run() {
                this.f$0.lambda$performSendMessageRequest$68(tLObject, messageObject, str, delayedMessage, z, delayedMessage2, obj, map, z2);
            }
        })) {
            message.reqId = getConnectionsManager().sendRequest(tLObject, new RequestDelegate() { // from class: org.telegram.messenger.SendMessagesHelper$$ExternalSyntheticLambda88
                @Override // org.telegram.tgnet.RequestDelegate
                public final void run(TLObject tLObject2, TLRPC.TL_error tL_error) {
                    this.f$0.lambda$performSendMessageRequest$86(tLObject, messageObject, str, delayedMessage, z, delayedMessage2, obj, map, z2, message, tLObject2, tL_error);
                }
            }, new QuickAckDelegate() { // from class: org.telegram.messenger.SendMessagesHelper$$ExternalSyntheticLambda89
                @Override // org.telegram.tgnet.QuickAckDelegate
                public final void run() {
                    this.f$0.lambda$performSendMessageRequest$88(message);
                }
            }, (tLObject instanceof TLRPC.TL_messages_sendMessage ? 128 : 0) | 68);
            if (delayedMessage != null) {
                delayedMessage.sendDelayedRequests();
            }
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$performSendMessageRequest$86(final TLObject tLObject, final MessageObject messageObject, final String str, final DelayedMessage delayedMessage, final boolean z, final DelayedMessage delayedMessage2, final Object obj, final HashMap map, final boolean z2, final TLRPC.Message message, final TLObject tLObject2, final TLRPC.TL_error tL_error) {
        if (tL_error != null && (((tLObject instanceof TLRPC.TL_messages_sendMedia) || (tLObject instanceof TLRPC.TL_messages_editMessage)) && FileRefController.isFileRefError(tL_error.text))) {
            if (FileRefController.isFileRefErrorCover(tL_error.text)) {
                if (removeCoverFromRequest(tLObject)) {
                    lambda$performSendMessageRequest$71(tLObject, messageObject, str, delayedMessage, z, delayedMessage2, obj, map, z2);
                    return;
                }
            } else if (obj != null) {
                getFileRefController().requestReference(obj, tLObject, messageObject, str, delayedMessage, Boolean.valueOf(z), delayedMessage2, Boolean.valueOf(z2));
                return;
            } else if (delayedMessage2 != null) {
                AndroidUtilities.runOnUIThread(new Runnable() { // from class: org.telegram.messenger.SendMessagesHelper$$ExternalSyntheticLambda41
                    @Override // java.lang.Runnable
                    public final void run() {
                        this.f$0.lambda$performSendMessageRequest$69(message, z2, tLObject, delayedMessage2);
                    }
                });
                return;
            }
        }
        if (tL_error != null && (tLObject instanceof TLRPC.TL_messages_sendMedia)) {
            TLRPC.TL_messages_sendMedia tL_messages_sendMedia = (TLRPC.TL_messages_sendMedia) tLObject;
            if (tL_messages_sendMedia.media instanceof TLRPC.TL_inputMediaStakeDice) {
                if ("GAME_HASH_INVALID".equalsIgnoreCase(tL_error.text)) {
                    getConnectionsManager().sendRequestTyped(new TLRPC.TL_messages_getEmojiGameInfo(), new BotForumHelper$$ExternalSyntheticLambda2(), new Utilities.Callback2() { // from class: org.telegram.messenger.SendMessagesHelper$$ExternalSyntheticLambda42
                        @Override // org.telegram.messenger.Utilities.Callback2
                        public final void run(Object obj2, Object obj3) {
                            this.f$0.lambda$performSendMessageRequest$70(tLObject, messageObject, str, delayedMessage, z, delayedMessage2, obj, map, z2, (TLRPC.EmojiGameInfo) obj2, (TLRPC.TL_error) obj3);
                        }
                    });
                    return;
                } else if ("BALANCE_TOO_LOW".equalsIgnoreCase(tL_error.text)) {
                    final TLRPC.TL_inputMediaStakeDice tL_inputMediaStakeDice = (TLRPC.TL_inputMediaStakeDice) tL_messages_sendMedia.media;
                    final BaseFragment safeLastFragment = LaunchActivity.getSafeLastFragment();
                    if (safeLastFragment != null) {
                        AndroidUtilities.runOnUIThread(new Runnable() { // from class: org.telegram.messenger.SendMessagesHelper$$ExternalSyntheticLambda43
                            @Override // java.lang.Runnable
                            public final void run() {
                                this.f$0.lambda$performSendMessageRequest$72(safeLastFragment, tL_inputMediaStakeDice, tLObject, messageObject, str, delayedMessage, z, delayedMessage2, obj, map, z2);
                            }
                        });
                        return;
                    }
                }
            }
        }
        if (tLObject instanceof TLRPC.TL_messages_editMessage) {
            AndroidUtilities.runOnUIThread(new Runnable() { // from class: org.telegram.messenger.SendMessagesHelper$$ExternalSyntheticLambda44
                @Override // java.lang.Runnable
                public final void run() {
                    this.f$0.lambda$performSendMessageRequest$75(tL_error, message, tLObject2, messageObject, str, z2, tLObject);
                }
            });
        } else {
            AndroidUtilities.runOnUIThread(new Runnable() { // from class: org.telegram.messenger.SendMessagesHelper$$ExternalSyntheticLambda45
                @Override // java.lang.Runnable
                public final void run() {
                    this.f$0.lambda$performSendMessageRequest$85(z2, tL_error, message, tLObject2, messageObject, str, tLObject);
                }
            });
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$performSendMessageRequest$69(TLRPC.Message message, boolean z, TLObject tLObject, DelayedMessage delayedMessage) {
        removeFromSendingMessages(message.id, z);
        if (tLObject instanceof TLRPC.TL_messages_sendMedia) {
            TLRPC.TL_messages_sendMedia tL_messages_sendMedia = (TLRPC.TL_messages_sendMedia) tLObject;
            TLRPC.InputMedia inputMedia = tL_messages_sendMedia.media;
            if ((inputMedia instanceof TLRPC.TL_inputMediaPhoto) || (inputMedia instanceof TLRPC.TL_inputMediaDocument)) {
                tL_messages_sendMedia.media = delayedMessage.inputUploadMedia;
            }
        } else if (tLObject instanceof TLRPC.TL_messages_editMessage) {
            TLRPC.TL_messages_editMessage tL_messages_editMessage = (TLRPC.TL_messages_editMessage) tLObject;
            TLRPC.InputMedia inputMedia2 = tL_messages_editMessage.media;
            if ((inputMedia2 instanceof TLRPC.TL_inputMediaPhoto) || (inputMedia2 instanceof TLRPC.TL_inputMediaDocument)) {
                tL_messages_editMessage.media = delayedMessage.inputUploadMedia;
            }
        }
        delayedMessage.performMediaUpload = true;
        performSendDelayedMessage(delayedMessage);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$performSendMessageRequest$70(TLObject tLObject, MessageObject messageObject, String str, DelayedMessage delayedMessage, boolean z, DelayedMessage delayedMessage2, Object obj, HashMap map, boolean z2, TLRPC.EmojiGameInfo emojiGameInfo, TLRPC.TL_error tL_error) {
        if (emojiGameInfo instanceof TLRPC.TL_emojiGameDiceInfo) {
            String str2 = ((TLRPC.TL_emojiGameDiceInfo) emojiGameInfo).game_hash;
            TLRPC.TL_messages_sendMedia tL_messages_sendMedia = (TLRPC.TL_messages_sendMedia) tLObject;
            TLRPC.InputMedia inputMedia = tL_messages_sendMedia.media;
            if (inputMedia instanceof TLRPC.TL_inputMediaStakeDice) {
                ((TLRPC.TL_inputMediaStakeDice) inputMedia).game_hash = str2;
            }
            lambda$performSendMessageRequest$71(tL_messages_sendMedia, messageObject, str, delayedMessage, z, delayedMessage2, obj, map, z2);
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$performSendMessageRequest$72(BaseFragment baseFragment, TLRPC.TL_inputMediaStakeDice tL_inputMediaStakeDice, final TLObject tLObject, final MessageObject messageObject, final String str, final DelayedMessage delayedMessage, final boolean z, final DelayedMessage delayedMessage2, final Object obj, final HashMap map, final boolean z2) {
        new TONIntroActivity.StarsNeededSheet(baseFragment.getContext(), baseFragment.getResourceProvider(), AmountUtils$Amount.fromNano(tL_inputMediaStakeDice.ton_amount, AmountUtils$Currency.TON), false, new Runnable() { // from class: org.telegram.messenger.SendMessagesHelper$$ExternalSyntheticLambda46
            @Override // java.lang.Runnable
            public final void run() {
                this.f$0.lambda$performSendMessageRequest$71(tLObject, messageObject, str, delayedMessage, z, delayedMessage2, obj, map, z2);
            }
        }).show();
        ArrayList<MessageObject> arrayList = new ArrayList<>();
        arrayList.add(messageObject);
        cancelSendingMessage(arrayList);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$performSendMessageRequest$75(TLRPC.TL_error tL_error, final TLRPC.Message message, TLObject tLObject, MessageObject messageObject, String str, final boolean z, TLObject tLObject2) {
        TLRPC.Message message2 = null;
        if (tL_error == null) {
            String str2 = message.attachPath;
            final TLRPC.Updates updates = (TLRPC.Updates) tLObject;
            ArrayList<TLRPC.Update> arrayList = updates.updates;
            for (int i = 0; i < arrayList.size(); i++) {
                TLRPC.Update update = arrayList.get(i);
                if (update instanceof TLRPC.TL_updateEditMessage) {
                    message2 = ((TLRPC.TL_updateEditMessage) update).message;
                    break;
                }
                if (update instanceof TLRPC.TL_updateEditChannelMessage) {
                    message2 = ((TLRPC.TL_updateEditChannelMessage) update).message;
                    break;
                }
                if (update instanceof TLRPC.TL_updateNewScheduledMessage) {
                    message2 = ((TLRPC.TL_updateNewScheduledMessage) update).message;
                    break;
                } else {
                    if (update instanceof TLRPC.TL_updateQuickReplyMessage) {
                        QuickRepliesController.getInstance(this.currentAccount).processUpdate(update, MessageObject.getQuickReplyName(message), MessageObject.getQuickReplyId(message));
                        message2 = ((TLRPC.TL_updateQuickReplyMessage) update).message;
                        break;
                    }
                }
            }
            TLRPC.Message message3 = message2;
            if (message3 != null) {
                ImageLoader.saveMessageThumbs(message3);
                updateMediaPaths(messageObject, message3, message3.id, str, false);
            }
            Utilities.stageQueue.postRunnable(new Runnable() { // from class: org.telegram.messenger.SendMessagesHelper$$ExternalSyntheticLambda80
                @Override // java.lang.Runnable
                public final void run() {
                    this.f$0.lambda$performSendMessageRequest$74(updates, message, z);
                }
            });
            return;
        }
        AlertsCreator.processError(this.currentAccount, tL_error, null, tLObject2, new Object[0]);
        removeFromSendingMessages(message.id, z);
        revertEditingMessageObject(messageObject);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$performSendMessageRequest$74(TLRPC.Updates updates, final TLRPC.Message message, final boolean z) {
        getMessagesController().processUpdates(updates, false);
        AndroidUtilities.runOnUIThread(new Runnable() { // from class: org.telegram.messenger.SendMessagesHelper$$ExternalSyntheticLambda11
            @Override // java.lang.Runnable
            public final void run() {
                this.f$0.lambda$performSendMessageRequest$73(message, z);
            }
        });
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$performSendMessageRequest$73(TLRPC.Message message, boolean z) {
        processSentMessage(message.id);
        removeFromSendingMessages(message.id, z);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$performSendMessageRequest$76(TLRPC.TL_updateShortSentMessage tL_updateShortSentMessage) {
        getMessagesController().processNewDifferenceParams(-1, tL_updateShortSentMessage.pts, tL_updateShortSentMessage.date, tL_updateShortSentMessage.pts_count);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$performSendMessageRequest$77(TLRPC.TL_updateNewMessage tL_updateNewMessage) {
        getMessagesController().processNewDifferenceParams(-1, tL_updateNewMessage.pts, -1, tL_updateNewMessage.pts_count);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$performSendMessageRequest$78(TLRPC.TL_updateNewChannelMessage tL_updateNewChannelMessage) {
        getMessagesController().processNewChannelDifferenceParams(tL_updateNewChannelMessage.pts, tL_updateNewChannelMessage.pts_count, tL_updateNewChannelMessage.message.peer_id.channel_id);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$performSendMessageRequest$79(TLRPC.TL_updateNewChannelMessage tL_updateNewChannelMessage, long j) {
        ArrayList<Integer> arrayList = new ArrayList<>();
        arrayList.add(Integer.valueOf(tL_updateNewChannelMessage.message.id));
        getMessagesStorage().updatePinnedMessages(-j, arrayList, true, -1, 0, false, null);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$performSendMessageRequest$80(TLRPC.Updates updates) {
        getMessagesController().processUpdates(updates, false);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$performSendMessageRequest$82(ArrayList arrayList, final boolean z, final boolean z2, final TLRPC.Message message, final ArrayList arrayList2, final ArrayList arrayList3, final int i) {
        getMessagesStorage().putMessages(arrayList, true, false, false, 0, false, !z ? 1 : 0, 0L);
        AndroidUtilities.runOnUIThread(new Runnable() { // from class: org.telegram.messenger.SendMessagesHelper$$ExternalSyntheticLambda81
            @Override // java.lang.Runnable
            public final void run() {
                this.f$0.lambda$performSendMessageRequest$81(z2, message, arrayList2, z, arrayList3, i);
            }
        });
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$performSendMessageRequest$81(boolean z, TLRPC.Message message, ArrayList arrayList, boolean z2, ArrayList arrayList2, int i) {
        boolean z3;
        int i2;
        if (!z || message == null) {
            z3 = false;
            i2 = 0;
        } else {
            i2 = message.id;
            z3 = false;
        }
        MessagesController messagesController = getMessagesController();
        long j = message.dialog_id;
        if (!z2 && z) {
            z3 = true;
        }
        messagesController.deleteMessages(arrayList, null, null, j, false, z2 ? 1 : 0, false, 0L, null, 0, z3, i2);
        getMessagesController().updateInterfaceWithMessages(message.dialog_id, arrayList2, z ? 1 : 0);
        getMediaDataController().increasePeerRaiting(message.dialog_id);
        processSentMessage(i);
        removeFromSendingMessages(i, z2);
    }

    /* JADX INFO: Access modifiers changed from: private */
    /* JADX WARN: Code duplicated, block: B:157:0x03d3  */
    /* JADX WARN: Code duplicated, block: B:159:0x03ea  */
    /* JADX WARN: Code duplicated, block: B:160:0x0426  */
    /* JADX WARN: Code duplicated, block: B:161:0x04b6  */
    /* JADX WARN: Multi-variable type inference failed */
    /* JADX WARN: Type inference failed for: r2v0 */
    /* JADX WARN: Type inference failed for: r2v1, types: [boolean, int] */
    /* JADX WARN: Type inference failed for: r2v28 */
    public /* synthetic */ void lambda$performSendMessageRequest$85(final boolean z, TLRPC.TL_error tL_error, final TLRPC.Message message, TLObject tLObject, MessageObject messageObject, String str, TLObject tLObject2) {
        SendMessagesHelper sendMessagesHelper;
        MessageObject messageObject2;
        ?? r2;
        final TLRPC.Message message2;
        int i;
        TLObject tLObject3;
        boolean z2;
        SendMessagesHelper sendMessagesHelper2;
        String str2;
        long j;
        ArrayList arrayList;
        SendMessagesHelper sendMessagesHelper3;
        final boolean z3;
        int mediaExistanceFlags;
        final SendMessagesHelper sendMessagesHelper4;
        boolean z4;
        boolean z5;
        TLRPC.MessageReplyHeader messageReplyHeader;
        final SendMessagesHelper sendMessagesHelper5;
        boolean z6;
        SendMessagesHelper sendMessagesHelper6;
        boolean z7;
        if (tL_error == null) {
            Long l = 0L;
            int i2 = message.id;
            ArrayList arrayList2 = new ArrayList();
            boolean z8 = message.date == 2147483646;
            i = 2;
            if (tLObject instanceof TLRPC.TL_updateShortSentMessage) {
                final TLRPC.TL_updateShortSentMessage tL_updateShortSentMessage = (TLRPC.TL_updateShortSentMessage) tLObject;
                j = 0;
                arrayList = arrayList2;
                updateMediaPaths(messageObject, null, tL_updateShortSentMessage.id, null, false);
                mediaExistanceFlags = messageObject.getMediaExistanceFlags();
                int i3 = tL_updateShortSentMessage.id;
                message.id = i3;
                message.local_id = i3;
                message.date = tL_updateShortSentMessage.date;
                message.entities = tL_updateShortSentMessage.entities;
                message.out = tL_updateShortSentMessage.out;
                if ((tL_updateShortSentMessage.flags & 33554432) != 0) {
                    message.ttl_period = tL_updateShortSentMessage.ttl_period;
                    message.flags |= 33554432;
                }
                TLRPC.MessageMedia messageMedia = tL_updateShortSentMessage.media;
                if (messageMedia != null) {
                    message.media = messageMedia;
                    message.flags |= 512;
                    ImageLoader.saveMessageThumbs(message);
                }
                TLRPC.MessageMedia messageMedia2 = tL_updateShortSentMessage.media;
                if (((messageMedia2 instanceof TLRPC.TL_messageMediaGame) || (messageMedia2 instanceof TLRPC.TL_messageMediaInvoice)) && !TextUtils.isEmpty(tL_updateShortSentMessage.message)) {
                    message.message = tL_updateShortSentMessage.message;
                }
                if (!message.entities.isEmpty()) {
                    message.flags |= 128;
                }
                Integer numValueOf = getMessagesController().dialogs_read_outbox_max.get(Long.valueOf(message.dialog_id));
                if (numValueOf == null) {
                    z7 = true;
                    numValueOf = Integer.valueOf(getMessagesStorage().getDialogReadMax(message.out, message.dialog_id));
                    getMessagesController().dialogs_read_outbox_max.put(Long.valueOf(message.dialog_id), numValueOf);
                } else {
                    z7 = true;
                }
                message.unread = numValueOf.intValue() < message.id ? z7 : false;
                Utilities.stageQueue.postRunnable(new Runnable() { // from class: org.telegram.messenger.SendMessagesHelper$$ExternalSyntheticLambda106
                    @Override // java.lang.Runnable
                    public final void run() {
                        this.f$0.lambda$performSendMessageRequest$76(tL_updateShortSentMessage);
                    }
                });
                arrayList.add(message);
                sendMessagesHelper3 = this;
                messageObject2 = messageObject;
                z3 = false;
            } else {
                j = 0;
                arrayList = arrayList2;
                if (tLObject instanceof TLRPC.Updates) {
                    final TLRPC.Updates updates = (TLRPC.Updates) tLObject;
                    ArrayList<TLRPC.Update> arrayList3 = updates.updates;
                    boolean z9 = z;
                    TLRPC.Message message3 = null;
                    int i4 = 0;
                    LongSparseArray longSparseArray = null;
                    while (i4 < arrayList3.size()) {
                        TLRPC.Update update = arrayList3.get(i4);
                        if (update instanceof TLRPC.TL_updateNewMessage) {
                            final TLRPC.TL_updateNewMessage tL_updateNewMessage = (TLRPC.TL_updateNewMessage) update;
                            TLRPC.Message message4 = tL_updateNewMessage.message;
                            z5 = z8;
                            if (message4.action == null) {
                                arrayList.add(message4);
                                Utilities.stageQueue.postRunnable(new Runnable() { // from class: org.telegram.messenger.SendMessagesHelper$$ExternalSyntheticLambda107
                                    @Override // java.lang.Runnable
                                    public final void run() {
                                        this.f$0.lambda$performSendMessageRequest$77(tL_updateNewMessage);
                                    }
                                });
                                arrayList3.remove(i4);
                                i4--;
                                message3 = message4;
                            }
                            l = l;
                        } else {
                            z5 = z8;
                            if (update instanceof TLRPC.TL_updateNewChannelMessage) {
                                final TLRPC.TL_updateNewChannelMessage tL_updateNewChannelMessage = (TLRPC.TL_updateNewChannelMessage) update;
                                final long updateChannelId = MessagesController.getUpdateChannelId(tL_updateNewChannelMessage);
                                i2 = i2;
                                TLRPC.Chat chat = getMessagesController().getChat(Long.valueOf(updateChannelId));
                                if ((chat == null || chat.megagroup) && (messageReplyHeader = tL_updateNewChannelMessage.message.reply_to) != null && (messageReplyHeader.reply_to_top_id != 0 || messageReplyHeader.reply_to_msg_id != 0)) {
                                    if (longSparseArray == null) {
                                        longSparseArray = new LongSparseArray();
                                    }
                                    long dialogId = MessageObject.getDialogId(tL_updateNewChannelMessage.message);
                                    SparseArray sparseArray = (SparseArray) longSparseArray.get(dialogId);
                                    if (sparseArray == null) {
                                        sparseArray = new SparseArray();
                                        longSparseArray.put(dialogId, sparseArray);
                                    }
                                    TLRPC.MessageReplyHeader messageReplyHeader2 = tL_updateNewChannelMessage.message.reply_to;
                                    int i5 = messageReplyHeader2.reply_to_top_id;
                                    if (i5 == 0) {
                                        i5 = messageReplyHeader2.reply_to_msg_id;
                                    }
                                    TLRPC.MessageReplies tL_messageReplies = (TLRPC.MessageReplies) sparseArray.get(i5);
                                    if (tL_messageReplies == null) {
                                        tL_messageReplies = new TLRPC.TL_messageReplies();
                                        sparseArray.put(i5, tL_messageReplies);
                                    }
                                    TLRPC.Peer peer = tL_updateNewChannelMessage.message.from_id;
                                    if (peer != null) {
                                        tL_messageReplies.recent_repliers.add(0, peer);
                                    }
                                    tL_messageReplies.replies++;
                                    longSparseArray = longSparseArray;
                                }
                                TLRPC.Message message5 = tL_updateNewChannelMessage.message;
                                arrayList.add(message5);
                                Utilities.stageQueue.postRunnable(new Runnable() { // from class: org.telegram.messenger.SendMessagesHelper$$ExternalSyntheticLambda108
                                    @Override // java.lang.Runnable
                                    public final void run() {
                                        this.f$0.lambda$performSendMessageRequest$78(tL_updateNewChannelMessage);
                                    }
                                });
                                arrayList3.remove(i4);
                                i4--;
                                if (tL_updateNewChannelMessage.message.pinned) {
                                    Utilities.stageQueue.postRunnable(new Runnable() { // from class: org.telegram.messenger.SendMessagesHelper$$ExternalSyntheticLambda109
                                        @Override // java.lang.Runnable
                                        public final void run() {
                                            this.f$0.lambda$performSendMessageRequest$79(tL_updateNewChannelMessage, updateChannelId);
                                        }
                                    });
                                }
                                message3 = message5;
                            } else {
                                i2 = i2;
                                l = l;
                                if (update instanceof TLRPC.TL_updateNewScheduledMessage) {
                                    TLRPC.TL_updateNewScheduledMessage tL_updateNewScheduledMessage = (TLRPC.TL_updateNewScheduledMessage) update;
                                    for (int i6 = 0; i6 < arrayList.size(); i6++) {
                                        if (((TLRPC.Message) arrayList.get(i6)).id == tL_updateNewScheduledMessage.message.id) {
                                            arrayList.remove(i6);
                                            break;
                                        }
                                    }
                                    message3 = tL_updateNewScheduledMessage.message;
                                    arrayList.add(message3);
                                    arrayList3.remove(i4);
                                    i4--;
                                    z9 = true;
                                } else if (update instanceof TLRPC.TL_updateQuickReplyMessage) {
                                    QuickRepliesController.getInstance(this.currentAccount).processUpdate(update, messageObject.getQuickReplyName(), messageObject.getQuickReplyId());
                                    message3 = ((TLRPC.TL_updateQuickReplyMessage) update).message;
                                    arrayList.add(message3);
                                    arrayList3.remove(i4);
                                    i4--;
                                } else if (update instanceof TLRPC.TL_updateDeleteScheduledMessages) {
                                    TLRPC.TL_updateDeleteScheduledMessages tL_updateDeleteScheduledMessages = (TLRPC.TL_updateDeleteScheduledMessages) update;
                                    if (messageObject.getDialogId() == DialogObject.getPeerDialogId(tL_updateDeleteScheduledMessages.peer)) {
                                        ArrayList arrayList4 = tL_updateDeleteScheduledMessages.messages;
                                        int size = arrayList4.size();
                                        int i7 = 0;
                                        while (i7 < size) {
                                            Object obj = arrayList4.get(i7);
                                            i7++;
                                            int iIntValue = ((Integer) obj).intValue();
                                            ArrayList arrayList5 = arrayList4;
                                            for (int i8 = 0; i8 < arrayList.size(); i8++) {
                                                if (((TLRPC.Message) arrayList.get(i8)).id == iIntValue) {
                                                    arrayList.remove(i8);
                                                    break;
                                                }
                                            }
                                            arrayList4 = arrayList5;
                                        }
                                        arrayList3.remove(i4);
                                        i4--;
                                    }
                                }
                            }
                            z9 = false;
                        }
                        i4++;
                        z8 = z5;
                        i2 = i2;
                        l = l;
                    }
                    boolean z10 = z8;
                    i2 = i2;
                    l = l;
                    if (longSparseArray != null) {
                        getMessagesStorage().putChannelViews(null, null, longSparseArray, true);
                        getNotificationCenter().lambda$postNotificationNameOnUIThread$1(NotificationCenter.didUpdateMessagesViews, null, null, longSparseArray, Boolean.TRUE);
                    }
                    if (message3 != null) {
                        MessageObject.getDialogId(message3);
                        if (z10 && message3.date != 2147483646) {
                            z9 = false;
                        }
                        ImageLoader.saveMessageThumbs(message3);
                        if (!z9) {
                            Integer numValueOf2 = getMessagesController().dialogs_read_outbox_max.get(Long.valueOf(message3.dialog_id));
                            if (numValueOf2 == null) {
                                numValueOf2 = Integer.valueOf(getMessagesStorage().getDialogReadMax(message3.out, message3.dialog_id));
                                getMessagesController().dialogs_read_outbox_max.put(Long.valueOf(message3.dialog_id), numValueOf2);
                            }
                            message3.unread = numValueOf2.intValue() < message3.id;
                        }
                        TLRPC.Message message6 = messageObject.messageOwner;
                        message6.post_author = message3.post_author;
                        if ((message3.flags & 33554432) != 0) {
                            message6.ttl_period = message3.ttl_period;
                            message6.flags |= 33554432;
                        }
                        message6.entities = message3.entities;
                        int i9 = message3.quick_reply_shortcut_id;
                        message6.quick_reply_shortcut_id = i9;
                        if (i9 != 0) {
                            message6.flags |= TLObject.FLAG_30;
                        }
                        TLRPC.Message message7 = message3;
                        updateMediaPaths(messageObject, message7, message3.id, str, false);
                        messageObject2 = messageObject;
                        sendMessagesHelper4 = this;
                        mediaExistanceFlags = messageObject2.getMediaExistanceFlags();
                        message.id = message7.id;
                        z4 = false;
                    } else {
                        sendMessagesHelper4 = this;
                        messageObject2 = messageObject;
                        if (BuildVars.LOGS_ENABLED) {
                            StringBuilder sb = new StringBuilder();
                            for (int i10 = 0; i10 < arrayList3.size(); i10++) {
                                sb.append(arrayList3.get(i10).getClass().getSimpleName());
                                sb.append(", ");
                            }
                            FileLog.d("can't find message in updates " + ((Object) sb));
                        }
                        mediaExistanceFlags = 0;
                        z4 = true;
                    }
                    Utilities.stageQueue.postRunnable(new Runnable() { // from class: org.telegram.messenger.SendMessagesHelper$$ExternalSyntheticLambda110
                        @Override // java.lang.Runnable
                        public final void run() {
                            this.f$0.lambda$performSendMessageRequest$80(updates);
                        }
                    });
                    z2 = z4;
                    z3 = z9;
                    sendMessagesHelper5 = sendMessagesHelper4;
                } else {
                    sendMessagesHelper3 = this;
                    messageObject2 = messageObject;
                    z3 = z;
                    mediaExistanceFlags = 0;
                }
                if (MessageObject.isLiveLocationMessage(message) && message.via_bot_id == j && TextUtils.isEmpty(message.via_bot_name)) {
                    sendMessagesHelper5.getLocationController().addSharingLocation(message);
                }
                if (z2) {
                    z6 = z;
                    message2 = message;
                    sendMessagesHelper6 = sendMessagesHelper5;
                } else {
                    sendMessagesHelper5.getStatsController().incrementSentItemsCount(ApplicationLoader.getCurrentNetworkType(), 1, 1);
                    message.send_state = 0;
                    long j2 = j;
                    message.errorNewPriceStars = j2;
                    message.errorAllowedPriceStars = j2;
                    if (z != z3) {
                        final ArrayList arrayList6 = new ArrayList();
                        arrayList6.add(Integer.valueOf(i2));
                        final ArrayList arrayList7 = new ArrayList();
                        arrayList7.add(new MessageObject(messageObject2.currentAccount, messageObject2.messageOwner, true, true));
                        final ArrayList arrayList8 = arrayList;
                        final int i11 = i2;
                        message2 = message;
                        sendMessagesHelper5.getMessagesStorage().getStorageQueue().postRunnable(new Runnable() { // from class: org.telegram.messenger.SendMessagesHelper$$ExternalSyntheticLambda111
                            @Override // java.lang.Runnable
                            public final void run() {
                                this.f$0.lambda$performSendMessageRequest$82(arrayList8, z, z3, message, arrayList6, arrayList7, i11);
                            }
                        });
                        sendMessagesHelper6 = this;
                        z6 = z;
                    } else {
                        message2 = message;
                        final int i12 = i2;
                        getNotificationCenter().lambda$postNotificationNameOnUIThread$1(NotificationCenter.messageReceivedByServer, Integer.valueOf(i12), Integer.valueOf(message2.id), message2, Long.valueOf(message2.dialog_id), l, Integer.valueOf(mediaExistanceFlags), Boolean.valueOf(z));
                        getNotificationCenter().lambda$postNotificationNameOnUIThread$1(NotificationCenter.messageReceivedByServer2, Integer.valueOf(i12), Integer.valueOf(message2.id), message2, Long.valueOf(message2.dialog_id), l, Integer.valueOf(mediaExistanceFlags), Boolean.valueOf(z));
                        final int i13 = mediaExistanceFlags;
                        final SendMessagesHelper sendMessagesHelper7 = this;
                        final boolean z11 = z;
                        final ArrayList arrayList9 = arrayList;
                        getMessagesStorage().getStorageQueue().postRunnable(new Runnable() { // from class: org.telegram.messenger.SendMessagesHelper$$ExternalSyntheticLambda112
                            @Override // java.lang.Runnable
                            public final void run() {
                                this.f$0.lambda$performSendMessageRequest$84(z11, message2, i12, arrayList9, i13);
                            }
                        });
                        sendMessagesHelper6 = sendMessagesHelper7;
                        z6 = z11;
                    }
                }
                tLObject3 = tLObject2;
                sendMessagesHelper2 = sendMessagesHelper6;
                r2 = z6;
            }
            z2 = false;
            sendMessagesHelper5 = sendMessagesHelper3;
            if (MessageObject.isLiveLocationMessage(message)) {
                sendMessagesHelper5.getLocationController().addSharingLocation(message);
            }
            if (z2) {
                sendMessagesHelper5.getStatsController().incrementSentItemsCount(ApplicationLoader.getCurrentNetworkType(), 1, 1);
                message.send_state = 0;
                long j3 = j;
                message.errorNewPriceStars = j3;
                message.errorAllowedPriceStars = j3;
                if (z != z3) {
                    final ArrayList arrayList10 = new ArrayList();
                    arrayList10.add(Integer.valueOf(i2));
                    final ArrayList arrayList11 = new ArrayList();
                    arrayList11.add(new MessageObject(messageObject2.currentAccount, messageObject2.messageOwner, true, true));
                    final ArrayList arrayList12 = arrayList;
                    final int i14 = i2;
                    message2 = message;
                    sendMessagesHelper5.getMessagesStorage().getStorageQueue().postRunnable(new Runnable() { // from class: org.telegram.messenger.SendMessagesHelper$$ExternalSyntheticLambda111
                        @Override // java.lang.Runnable
                        public final void run() {
                            this.f$0.lambda$performSendMessageRequest$82(arrayList12, z, z3, message, arrayList10, arrayList11, i14);
                        }
                    });
                    sendMessagesHelper6 = this;
                    z6 = z;
                } else {
                    message2 = message;
                    final int i15 = i2;
                    getNotificationCenter().lambda$postNotificationNameOnUIThread$1(NotificationCenter.messageReceivedByServer, Integer.valueOf(i15), Integer.valueOf(message2.id), message2, Long.valueOf(message2.dialog_id), l, Integer.valueOf(mediaExistanceFlags), Boolean.valueOf(z));
                    getNotificationCenter().lambda$postNotificationNameOnUIThread$1(NotificationCenter.messageReceivedByServer2, Integer.valueOf(i15), Integer.valueOf(message2.id), message2, Long.valueOf(message2.dialog_id), l, Integer.valueOf(mediaExistanceFlags), Boolean.valueOf(z));
                    final int i16 = mediaExistanceFlags;
                    final SendMessagesHelper sendMessagesHelper8 = this;
                    final boolean z12 = z;
                    final ArrayList arrayList13 = arrayList;
                    getMessagesStorage().getStorageQueue().postRunnable(new Runnable() { // from class: org.telegram.messenger.SendMessagesHelper$$ExternalSyntheticLambda112
                        @Override // java.lang.Runnable
                        public final void run() {
                            this.f$0.lambda$performSendMessageRequest$84(z12, message2, i15, arrayList13, i16);
                        }
                    });
                    sendMessagesHelper6 = sendMessagesHelper8;
                    z6 = z12;
                }
            } else {
                z6 = z;
                message2 = message;
                sendMessagesHelper6 = sendMessagesHelper5;
            }
            tLObject3 = tLObject2;
            sendMessagesHelper2 = sendMessagesHelper6;
            r2 = z6;
        } else {
            sendMessagesHelper = this;
            messageObject2 = messageObject;
            r2 = z;
            message2 = message;
            i = 2;
            tLObject3 = tLObject2;
            AlertsCreator.processError(sendMessagesHelper.currentAccount, tL_error, null, tLObject3, new Object[0]);
            z2 = true;
        }
        if (!z2) {
            sendMessagesHelper2 = sendMessagesHelper;
            return;
        }
        sendMessagesHelper2 = sendMessagesHelper;
        sendMessagesHelper2.getMessagesStorage().markMessageAsSendError(message2, r2);
        message2.send_state = i;
        if (tL_error != null && (str2 = tL_error.text) != null && str2.startsWith("ALLOW_PAYMENT_REQUIRED_")) {
            StarsController.getInstance(sendMessagesHelper2.currentAccount);
            message2.errorAllowedPriceStars = StarsController.getAllowedPaidStars(tLObject3);
            message2.errorNewPriceStars = Long.parseLong(tL_error.text.substring(23));
            StarsController.getInstance(sendMessagesHelper2.currentAccount).showPriceChangedToast(Arrays.asList(messageObject2));
            sendMessagesHelper2.getMessagesStorage().updateMessageCustomParams(MessageObject.getDialogId(message2), message2);
        }
        sendMessagesHelper2.getNotificationCenter().lambda$postNotificationNameOnUIThread$1(NotificationCenter.messageSendError, Integer.valueOf(message2.id));
        sendMessagesHelper2.processSentMessage(message2.id);
        sendMessagesHelper2.removeFromSendingMessages(message2.id, r2);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$performSendMessageRequest$84(final boolean z, final TLRPC.Message message, final int i, ArrayList arrayList, final int i2) {
        int i3 = (message.quick_reply_shortcut_id == 0 && message.quick_reply_shortcut == null) ? z ? 1 : 0 : 5;
        getMessagesStorage().updateMessageStateAndId(message.random_id, MessageObject.getPeerId(message.peer_id), Integer.valueOf(i), message.id, 0, false, z ? 1 : 0, message.quick_reply_shortcut_id);
        getMessagesStorage().putMessages((ArrayList<TLRPC.Message>) arrayList, true, false, false, 0, i3, message.quick_reply_shortcut_id);
        AndroidUtilities.runOnUIThread(new Runnable() { // from class: org.telegram.messenger.SendMessagesHelper$$ExternalSyntheticLambda90
            @Override // java.lang.Runnable
            public final void run() {
                this.f$0.lambda$performSendMessageRequest$83(message, i, i2, z);
            }
        });
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$performSendMessageRequest$83(TLRPC.Message message, int i, int i2, boolean z) {
        getMediaDataController().increasePeerRaiting(message.dialog_id);
        getNotificationCenter().lambda$postNotificationNameOnUIThread$1(NotificationCenter.messageReceivedByServer, Integer.valueOf(i), Integer.valueOf(message.id), message, Long.valueOf(message.dialog_id), 0L, Integer.valueOf(i2), Boolean.valueOf(z));
        getNotificationCenter().lambda$postNotificationNameOnUIThread$1(NotificationCenter.messageReceivedByServer2, Integer.valueOf(i), Integer.valueOf(message.id), message, Long.valueOf(message.dialog_id), 0L, Integer.valueOf(i2), Boolean.valueOf(z));
        processSentMessage(i);
        removeFromSendingMessages(i, z);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$performSendMessageRequest$88(final TLRPC.Message message) {
        final int i = message.id;
        AndroidUtilities.runOnUIThread(new Runnable() { // from class: org.telegram.messenger.SendMessagesHelper$$ExternalSyntheticLambda27
            @Override // java.lang.Runnable
            public final void run() {
                this.f$0.lambda$performSendMessageRequest$87(message, i);
            }
        });
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$performSendMessageRequest$87(TLRPC.Message message, int i) {
        message.send_state = 0;
        getNotificationCenter().lambda$postNotificationNameOnUIThread$1(NotificationCenter.messageReceivedByAck, Integer.valueOf(i));
    }

    private boolean removeCoverFromRequest(TLObject tLObject) {
        if (tLObject instanceof TLRPC.TL_messages_sendMedia) {
            TLRPC.InputMedia inputMedia = ((TLRPC.TL_messages_sendMedia) tLObject).media;
            if (inputMedia instanceof TLRPC.TL_inputMediaUploadedDocument) {
                TLRPC.TL_inputMediaUploadedDocument tL_inputMediaUploadedDocument = (TLRPC.TL_inputMediaUploadedDocument) inputMedia;
                tL_inputMediaUploadedDocument.video_cover = null;
                tL_inputMediaUploadedDocument.flags &= -65;
                return true;
            }
            if (inputMedia instanceof TLRPC.TL_inputMediaDocument) {
                TLRPC.TL_inputMediaDocument tL_inputMediaDocument = (TLRPC.TL_inputMediaDocument) inputMedia;
                tL_inputMediaDocument.video_cover = null;
                tL_inputMediaDocument.flags &= -9;
                return true;
            }
            if (!(inputMedia instanceof TLRPC.TL_inputMediaDocumentExternal)) {
                return false;
            }
            TLRPC.TL_inputMediaDocumentExternal tL_inputMediaDocumentExternal = (TLRPC.TL_inputMediaDocumentExternal) inputMedia;
            tL_inputMediaDocumentExternal.video_cover = null;
            tL_inputMediaDocumentExternal.flags &= -5;
            return true;
        }
        if (!(tLObject instanceof TLRPC.TL_messages_editMessage)) {
            return false;
        }
        TLRPC.InputMedia inputMedia2 = ((TLRPC.TL_messages_editMessage) tLObject).media;
        if (inputMedia2 instanceof TLRPC.TL_inputMediaUploadedDocument) {
            TLRPC.TL_inputMediaUploadedDocument tL_inputMediaUploadedDocument2 = (TLRPC.TL_inputMediaUploadedDocument) inputMedia2;
            tL_inputMediaUploadedDocument2.video_cover = null;
            tL_inputMediaUploadedDocument2.flags &= -65;
            return true;
        }
        if (inputMedia2 instanceof TLRPC.TL_inputMediaDocument) {
            TLRPC.TL_inputMediaDocument tL_inputMediaDocument2 = (TLRPC.TL_inputMediaDocument) inputMedia2;
            tL_inputMediaDocument2.video_cover = null;
            tL_inputMediaDocument2.flags &= -9;
            return true;
        }
        if (!(inputMedia2 instanceof TLRPC.TL_inputMediaDocumentExternal)) {
            return false;
        }
        TLRPC.TL_inputMediaDocumentExternal tL_inputMediaDocumentExternal2 = (TLRPC.TL_inputMediaDocumentExternal) inputMedia2;
        tL_inputMediaDocumentExternal2.video_cover = null;
        tL_inputMediaDocumentExternal2.flags &= -5;
        return true;
    }

    private void updateMediaPaths(MessageObject messageObject, TLRPC.Message message, int i, String str, boolean z) {
        updateMediaPaths(messageObject, message, i, Collections.singletonList(str), z, -1);
    }

    /* JADX WARN: Code duplicated, block: B:133:0x01fa  */
    /* JADX WARN: Code duplicated, block: B:140:0x0264  */
    /* JADX WARN: Code duplicated, block: B:26:0x0061  */
    /* JADX WARN: Code duplicated, block: B:28:0x0067  */
    /* JADX WARN: Code duplicated, block: B:29:0x0085  */
    /* JADX WARN: Code duplicated, block: B:31:0x008b  */
    /* JADX WARN: Code duplicated, block: B:329:0x0708  */
    /* JADX WARN: Code duplicated, block: B:339:0x07c3  */
    /* JADX WARN: Code duplicated, block: B:341:0x07dc  */
    /* JADX WARN: Code duplicated, block: B:355:0x0877 A[DONT_INVERT] */
    /* JADX WARN: Code duplicated, block: B:356:0x0879 A[ADDED_TO_REGION] */
    /* JADX WARN: Code duplicated, block: B:362:0x0888 A[DONT_INVERT] */
    /* JADX WARN: Code duplicated, block: B:363:0x088a  */
    /* JADX WARN: Code duplicated, block: B:367:0x0894  */
    /* JADX WARN: Code duplicated, block: B:371:0x08b7  */
    /* JADX WARN: Code duplicated, block: B:374:0x08c8 A[LOOP:2: B:369:0x08ad->B:374:0x08c8, LOOP_END] */
    /* JADX WARN: Code duplicated, block: B:378:0x08d6  */
    /* JADX WARN: Code duplicated, block: B:381:0x08e1  */
    /* JADX WARN: Code duplicated, block: B:383:0x08ef  */
    /* JADX WARN: Code duplicated, block: B:38:0x00a2  */
    /* JADX WARN: Code duplicated, block: B:392:0x0921  */
    /* JADX WARN: Code duplicated, block: B:410:0x0975  */
    /* JADX WARN: Code duplicated, block: B:411:0x097c  */
    /* JADX WARN: Code duplicated, block: B:413:0x0984  */
    /* JADX WARN: Code duplicated, block: B:419:0x09ad  */
    /* JADX WARN: Code duplicated, block: B:41:0x00b1  */
    /* JADX WARN: Code duplicated, block: B:420:0x09af  */
    /* JADX WARN: Code duplicated, block: B:423:0x09ba  */
    /* JADX WARN: Code duplicated, block: B:425:0x09c0 A[DONT_INVERT] */
    /* JADX WARN: Code duplicated, block: B:426:0x09c2  */
    /* JADX WARN: Code duplicated, block: B:427:0x09c5  */
    /* JADX WARN: Code duplicated, block: B:428:0x09c8 A[DONT_INVERT] */
    /* JADX WARN: Code duplicated, block: B:430:0x09cb  */
    /* JADX WARN: Code duplicated, block: B:433:0x09d1  */
    /* JADX WARN: Code duplicated, block: B:436:0x09dc  */
    /* JADX WARN: Code duplicated, block: B:438:0x09e2  */
    /* JADX WARN: Code duplicated, block: B:43:0x00b5  */
    /* JADX WARN: Code duplicated, block: B:440:0x09e6  */
    /* JADX WARN: Code duplicated, block: B:442:0x09f1  */
    /* JADX WARN: Code duplicated, block: B:443:0x09f4  */
    /* JADX WARN: Code duplicated, block: B:445:0x09f8  */
    /* JADX WARN: Code duplicated, block: B:447:0x0a03  */
    /* JADX WARN: Code duplicated, block: B:451:0x0a15  */
    /* JADX WARN: Code duplicated, block: B:453:0x0a18  */
    /* JADX WARN: Code duplicated, block: B:497:0x08cb A[SYNTHETIC] */
    /* JADX WARN: Code duplicated, block: B:498:0x08c5 A[SYNTHETIC] */
    /* JADX WARN: Code duplicated, block: B:501:0x08f7 A[SYNTHETIC] */
    /* JADX WARN: Code duplicated, block: B:50:0x00cc  */
    /* JADX WARN: Code duplicated, block: B:513:? A[RETURN, SYNTHETIC] */
    /* JADX WARN: Code duplicated, block: B:514:? A[RETURN, SYNTHETIC] */
    /* JADX WARN: Code duplicated, block: B:52:0x00d2  */
    /* JADX WARN: Code duplicated, block: B:54:0x00d6  */
    /* JADX WARN: Code duplicated, block: B:56:0x00da  */
    /* JADX WARN: Code duplicated, block: B:65:0x00f5  */
    /* JADX WARN: Code duplicated, block: B:67:0x00fd  */
    /* JADX WARN: Code duplicated, block: B:69:0x0101  */
    /* JADX WARN: Code duplicated, block: B:78:0x011c  */
    /* JADX WARN: Code duplicated, block: B:80:0x0124  */
    /* JADX WARN: Instruction removed from duplicated block: B:329:0x0708, please report this as an issue */
    private void updateMediaPaths(MessageObject messageObject, TLRPC.Message message, int i, List<String> list, boolean z, int i2) {
        List<String> list2;
        String str;
        boolean z2;
        SendMessagesHelper sendMessagesHelper;
        MessageObject messageObject2;
        boolean z3;
        TLRPC.TL_messageExtendedMedia tL_messageExtendedMedia;
        TLRPC.PhotoSize closestPhotoSizeWithSize;
        TLRPC.TL_messageExtendedMedia tL_messageExtendedMedia2;
        SendMessagesHelper sendMessagesHelper2;
        TLRPC.Photo photo;
        String str2;
        byte[] bArr;
        TLRPC.PhotoSize closestPhotoSizeWithSize2;
        TLRPC.PhotoSize closestPhotoSizeWithSize3;
        TLRPC.FileLocation fileLocation;
        TLRPC.FileLocation fileLocation2;
        int i3;
        byte[] bArr2;
        TLRPC.Message message2;
        TLRPC.TL_messageExtendedMedia tL_messageExtendedMedia3;
        String str3;
        File file;
        boolean z4;
        File pathToAttach;
        String str4;
        int i4;
        TLRPC.DocumentAttribute documentAttribute;
        TLRPC.DocumentAttribute documentAttribute2;
        TLRPC.Photo photo2;
        String str5;
        TLRPC.MessageMedia messageMedia;
        String str6;
        String str7;
        File file2;
        VideoEditedInfo videoEditedInfo;
        TLRPC.PhotoSize photoSize;
        String str8;
        File file3;
        TLRPC.MessageMedia messageMedia2;
        TLRPC.Photo photo3;
        TLRPC.Document document;
        TLRPC.WebPage webPage;
        boolean z5;
        TLRPC.TL_messageExtendedMedia tL_messageExtendedMedia4;
        TLRPC.PhotoSize closestPhotoSizeWithSize4;
        TLRPC.TL_messageExtendedMedia tL_messageExtendedMedia5;
        TLObject tLObject;
        TLRPC.PhotoSize closestPhotoSizeWithSize5;
        TLObject tLObject2;
        TLRPC.Document document2;
        TLRPC.Photo photo4;
        TLRPC.Photo photo5;
        TLRPC.Document document3;
        TLRPC.PhotoSize closestPhotoSizeWithSize6;
        TLRPC.MessageMedia messageMedia3;
        TLRPC.WebPage webPage2;
        TLRPC.Document document4;
        TLRPC.MessageMedia messageMedia4;
        TLRPC.WebPage webPage3;
        TLRPC.Photo photo6;
        TLRPC.MessageMedia messageMedia5;
        TLRPC.Document document5;
        TLRPC.MessageMedia messageMedia6;
        TLRPC.Photo photo7;
        SendMessagesHelper sendMessagesHelper3 = this;
        TLRPC.Message message3 = messageObject.messageOwner;
        if (list.isEmpty() || Math.max(0, i2) >= list.size()) {
            list2 = list;
            str = null;
        } else {
            list2 = list;
            str = list2.get(Math.max(0, i2));
        }
        TLRPC.MessageMedia messageMedia7 = message == null ? null : message.media;
        TLRPC.MessageMedia messageMedia8 = message3 == null ? null : message3.media;
        TLRPC.MessageMedia messageMedia9 = message3.media;
        if (messageMedia9 != null) {
            if (messageMedia9.storyItem != null) {
                message.media = messageMedia9;
            } else if (messageObject.isLiveLocation()) {
                TLRPC.MessageMedia messageMedia10 = message.media;
                if (messageMedia10 instanceof TLRPC.TL_messageMediaGeoLive) {
                    message3.media.period = messageMedia10.period;
                } else if (messageObject.isDice()) {
                    TLRPC.TL_messageMediaDice tL_messageMediaDice = (TLRPC.TL_messageMediaDice) message3.media;
                    TLRPC.TL_messageMediaDice tL_messageMediaDice2 = (TLRPC.TL_messageMediaDice) message.media;
                    tL_messageMediaDice.value = tL_messageMediaDice2.value;
                    tL_messageMediaDice.flags = tL_messageMediaDice2.flags;
                    tL_messageMediaDice.game_outcome = tL_messageMediaDice2.game_outcome;
                    StarsController.getInstance(sendMessagesHelper3.currentAccount, true).invalidateBalance();
                } else {
                    messageMedia2 = message3.media;
                    photo3 = messageMedia2.photo;
                    if (photo3 != null) {
                        closestPhotoSizeWithSize4 = FileLoader.getClosestPhotoSizeWithSize(photo3.sizes, 40);
                        if (message != null || (messageMedia6 = message.media) == null || (photo7 = messageMedia6.photo) == null) {
                            closestPhotoSizeWithSize6 = closestPhotoSizeWithSize4;
                        } else {
                            closestPhotoSizeWithSize6 = FileLoader.getClosestPhotoSizeWithSize(photo7.sizes, 40);
                        }
                        tLObject = message3.media.photo;
                    } else {
                        document = messageMedia2.document;
                        if (document != null) {
                            closestPhotoSizeWithSize4 = FileLoader.getClosestPhotoSizeWithSize(document.thumbs, 40);
                            if (message != null || (messageMedia5 = message.media) == null || (document5 = messageMedia5.document) == null) {
                                closestPhotoSizeWithSize6 = closestPhotoSizeWithSize4;
                            } else {
                                closestPhotoSizeWithSize6 = FileLoader.getClosestPhotoSizeWithSize(document5.thumbs, 40);
                            }
                            tLObject = message3.media.document;
                        } else {
                            webPage = messageMedia2.webpage;
                            if (webPage != null) {
                                photo5 = webPage.photo;
                                if (photo5 != null) {
                                    closestPhotoSizeWithSize4 = FileLoader.getClosestPhotoSizeWithSize(photo5.sizes, 40);
                                    if (message != null || (messageMedia4 = message.media) == null || (webPage3 = messageMedia4.webpage) == null || (photo6 = webPage3.photo) == null) {
                                        closestPhotoSizeWithSize6 = closestPhotoSizeWithSize4;
                                    } else {
                                        closestPhotoSizeWithSize6 = FileLoader.getClosestPhotoSizeWithSize(photo6.sizes, 40);
                                    }
                                    tLObject = message3.media.webpage.photo;
                                } else {
                                    document3 = webPage.document;
                                    if (document3 != null) {
                                        closestPhotoSizeWithSize4 = FileLoader.getClosestPhotoSizeWithSize(document3.thumbs, 40);
                                        if (message != null || (messageMedia3 = message.media) == null || (webPage2 = messageMedia3.webpage) == null || (document4 = webPage2.document) == null) {
                                            closestPhotoSizeWithSize6 = closestPhotoSizeWithSize4;
                                        } else {
                                            closestPhotoSizeWithSize6 = FileLoader.getClosestPhotoSizeWithSize(document4.thumbs, 40);
                                        }
                                        tLObject = message3.media.webpage.document;
                                    }
                                    if ((closestPhotoSizeWithSize instanceof TLRPC.TL_photoStrippedSize) || !(closestPhotoSizeWithSize4 instanceof TLRPC.TL_photoStrippedSize)) {
                                        message3 = message3;
                                        tL_messageExtendedMedia5 = tL_messageExtendedMedia5;
                                    } else {
                                        ImageLoader.getInstance().replaceImageInCache("stripped" + FileRefController.getKeyForParentObject(messageObject2), message != null ? "stripped" + FileRefController.getKeyForParentObject(message) : "strippedmessage" + i + "_" + messageObject2.getChannelId() + "_" + messageObject2.scheduled, ImageLocation.getForObject(closestPhotoSizeWithSize, tLObject), z2);
                                    }
                                    z3 = z5;
                                    tL_messageExtendedMedia = tL_messageExtendedMedia5;
                                }
                            } else {
                                if (!(messageMedia2 instanceof TLRPC.TL_messageMediaPaidMedia) && (messageMedia7 instanceof TLRPC.TL_messageMediaPaidMedia)) {
                                    TLRPC.TL_messageMediaPaidMedia tL_messageMediaPaidMedia = (TLRPC.TL_messageMediaPaidMedia) messageMedia2;
                                    TLRPC.TL_messageMediaPaidMedia tL_messageMediaPaidMedia2 = (TLRPC.TL_messageMediaPaidMedia) messageMedia7;
                                    if (!tL_messageMediaPaidMedia.extended_media.isEmpty() && !tL_messageMediaPaidMedia2.extended_media.isEmpty()) {
                                        if (i2 == -1) {
                                            int i5 = 0;
                                            while (i5 < tL_messageMediaPaidMedia.extended_media.size()) {
                                                sendMessagesHelper3.updateMediaPaths(messageObject, message, i, list2, z, i5);
                                                i5++;
                                                sendMessagesHelper3 = sendMessagesHelper3;
                                                list2 = list;
                                            }
                                        } else {
                                            z2 = z;
                                            sendMessagesHelper = sendMessagesHelper3;
                                            messageObject2 = messageObject;
                                            z5 = tL_messageMediaPaidMedia2.extended_media.size() > 1;
                                            if (i2 >= 0 && i2 < tL_messageMediaPaidMedia2.extended_media.size()) {
                                                TLRPC.MessageExtendedMedia messageExtendedMedia = tL_messageMediaPaidMedia2.extended_media.get(i2);
                                                if (messageExtendedMedia instanceof TLRPC.TL_messageExtendedMedia) {
                                                    TLRPC.TL_messageExtendedMedia tL_messageExtendedMedia6 = (TLRPC.TL_messageExtendedMedia) messageExtendedMedia;
                                                    tL_messageExtendedMedia4 = tL_messageExtendedMedia6;
                                                    messageMedia7 = tL_messageExtendedMedia6.media;
                                                } else {
                                                    tL_messageExtendedMedia4 = null;
                                                }
                                                TLRPC.MessageExtendedMedia messageExtendedMedia2 = tL_messageMediaPaidMedia.extended_media.get(i2);
                                                if (messageExtendedMedia2 instanceof TLRPC.TL_messageExtendedMedia) {
                                                    TLRPC.TL_messageExtendedMedia tL_messageExtendedMedia7 = (TLRPC.TL_messageExtendedMedia) messageExtendedMedia2;
                                                    messageMedia8 = tL_messageExtendedMedia7.media;
                                                    TLRPC.Photo photo8 = messageMedia8.photo;
                                                    if (photo8 != null) {
                                                        closestPhotoSizeWithSize5 = FileLoader.getClosestPhotoSizeWithSize(photo8.sizes, 40);
                                                        TLRPC.PhotoSize closestPhotoSizeWithSize7 = (messageMedia7 == null || (photo4 = messageMedia7.photo) == null) ? closestPhotoSizeWithSize5 : FileLoader.getClosestPhotoSizeWithSize(photo4.sizes, 40);
                                                        tLObject2 = messageMedia8.photo;
                                                        closestPhotoSizeWithSize = closestPhotoSizeWithSize7;
                                                    } else {
                                                        TLRPC.Document document6 = messageMedia8.document;
                                                        if (document6 != null) {
                                                            closestPhotoSizeWithSize5 = FileLoader.getClosestPhotoSizeWithSize(document6.thumbs, 40);
                                                            closestPhotoSizeWithSize = (messageMedia7 == null || (document2 = messageMedia7.document) == null) ? closestPhotoSizeWithSize5 : FileLoader.getClosestPhotoSizeWithSize(document2.thumbs, 40);
                                                            tLObject2 = messageMedia8.document;
                                                        } else {
                                                            closestPhotoSizeWithSize5 = null;
                                                            closestPhotoSizeWithSize = null;
                                                            tLObject2 = null;
                                                        }
                                                    }
                                                    TLRPC.TL_messageExtendedMedia tL_messageExtendedMedia8 = tL_messageExtendedMedia4;
                                                    tL_messageExtendedMedia5 = tL_messageExtendedMedia7;
                                                    closestPhotoSizeWithSize4 = closestPhotoSizeWithSize5;
                                                    tLObject = tLObject2;
                                                    tL_messageExtendedMedia2 = tL_messageExtendedMedia8;
                                                } else {
                                                    tL_messageExtendedMedia2 = tL_messageExtendedMedia4;
                                                    closestPhotoSizeWithSize4 = null;
                                                    tL_messageExtendedMedia5 = null;
                                                    tLObject = null;
                                                    closestPhotoSizeWithSize = null;
                                                }
                                            }
                                        }
                                    }
                                    return;
                                }
                                if (closestPhotoSizeWithSize instanceof TLRPC.TL_photoStrippedSize) {
                                    message3 = message3;
                                    tL_messageExtendedMedia5 = tL_messageExtendedMedia5;
                                } else {
                                    message3 = message3;
                                    tL_messageExtendedMedia5 = tL_messageExtendedMedia5;
                                }
                                z3 = z5;
                                tL_messageExtendedMedia = tL_messageExtendedMedia5;
                            }
                        }
                    }
                    z2 = z;
                    closestPhotoSizeWithSize = closestPhotoSizeWithSize6;
                    tL_messageExtendedMedia2 = null;
                    sendMessagesHelper = sendMessagesHelper3;
                    messageObject2 = messageObject;
                    z5 = false;
                    tL_messageExtendedMedia5 = null;
                    if (closestPhotoSizeWithSize instanceof TLRPC.TL_photoStrippedSize) {
                        message3 = message3;
                        tL_messageExtendedMedia5 = tL_messageExtendedMedia5;
                    } else {
                        message3 = message3;
                        tL_messageExtendedMedia5 = tL_messageExtendedMedia5;
                    }
                    z3 = z5;
                    tL_messageExtendedMedia = tL_messageExtendedMedia5;
                }
            } else if (messageObject.isDice()) {
                TLRPC.TL_messageMediaDice tL_messageMediaDice3 = (TLRPC.TL_messageMediaDice) message3.media;
                TLRPC.TL_messageMediaDice tL_messageMediaDice4 = (TLRPC.TL_messageMediaDice) message.media;
                tL_messageMediaDice3.value = tL_messageMediaDice4.value;
                tL_messageMediaDice3.flags = tL_messageMediaDice4.flags;
                tL_messageMediaDice3.game_outcome = tL_messageMediaDice4.game_outcome;
                StarsController.getInstance(sendMessagesHelper3.currentAccount, true).invalidateBalance();
            } else {
                messageMedia2 = message3.media;
                photo3 = messageMedia2.photo;
                if (photo3 != null) {
                    closestPhotoSizeWithSize4 = FileLoader.getClosestPhotoSizeWithSize(photo3.sizes, 40);
                    if (message != null) {
                        closestPhotoSizeWithSize6 = closestPhotoSizeWithSize4;
                    } else {
                        closestPhotoSizeWithSize6 = closestPhotoSizeWithSize4;
                    }
                    tLObject = message3.media.photo;
                } else {
                    document = messageMedia2.document;
                    if (document != null) {
                        closestPhotoSizeWithSize4 = FileLoader.getClosestPhotoSizeWithSize(document.thumbs, 40);
                        if (message != null) {
                            closestPhotoSizeWithSize6 = closestPhotoSizeWithSize4;
                        } else {
                            closestPhotoSizeWithSize6 = closestPhotoSizeWithSize4;
                        }
                        tLObject = message3.media.document;
                    } else {
                        webPage = messageMedia2.webpage;
                        if (webPage != null) {
                            photo5 = webPage.photo;
                            if (photo5 != null) {
                                closestPhotoSizeWithSize4 = FileLoader.getClosestPhotoSizeWithSize(photo5.sizes, 40);
                                if (message != null) {
                                    closestPhotoSizeWithSize6 = closestPhotoSizeWithSize4;
                                } else {
                                    closestPhotoSizeWithSize6 = closestPhotoSizeWithSize4;
                                }
                                tLObject = message3.media.webpage.photo;
                            } else {
                                document3 = webPage.document;
                                if (document3 != null) {
                                    closestPhotoSizeWithSize4 = FileLoader.getClosestPhotoSizeWithSize(document3.thumbs, 40);
                                    if (message != null) {
                                        closestPhotoSizeWithSize6 = closestPhotoSizeWithSize4;
                                    } else {
                                        closestPhotoSizeWithSize6 = closestPhotoSizeWithSize4;
                                    }
                                    tLObject = message3.media.webpage.document;
                                }
                                if (closestPhotoSizeWithSize instanceof TLRPC.TL_photoStrippedSize) {
                                    message3 = message3;
                                    tL_messageExtendedMedia5 = tL_messageExtendedMedia5;
                                } else {
                                    message3 = message3;
                                    tL_messageExtendedMedia5 = tL_messageExtendedMedia5;
                                }
                                z3 = z5;
                                tL_messageExtendedMedia = tL_messageExtendedMedia5;
                            }
                        } else if (!(messageMedia2 instanceof TLRPC.TL_messageMediaPaidMedia)) {
                        }
                    }
                }
                z2 = z;
                closestPhotoSizeWithSize = closestPhotoSizeWithSize6;
                tL_messageExtendedMedia2 = null;
                sendMessagesHelper = sendMessagesHelper3;
                messageObject2 = messageObject;
                z5 = false;
                tL_messageExtendedMedia5 = null;
                if (closestPhotoSizeWithSize instanceof TLRPC.TL_photoStrippedSize) {
                    message3 = message3;
                    tL_messageExtendedMedia5 = tL_messageExtendedMedia5;
                } else {
                    message3 = message3;
                    tL_messageExtendedMedia5 = tL_messageExtendedMedia5;
                }
                z3 = z5;
                tL_messageExtendedMedia = tL_messageExtendedMedia5;
            }
            z2 = z;
            sendMessagesHelper = sendMessagesHelper3;
            messageObject2 = messageObject;
            z5 = false;
            closestPhotoSizeWithSize4 = null;
            tL_messageExtendedMedia5 = null;
            tLObject = null;
            closestPhotoSizeWithSize = null;
            tL_messageExtendedMedia2 = null;
            if (closestPhotoSizeWithSize instanceof TLRPC.TL_photoStrippedSize) {
                message3 = message3;
                tL_messageExtendedMedia5 = tL_messageExtendedMedia5;
            } else {
                message3 = message3;
                tL_messageExtendedMedia5 = tL_messageExtendedMedia5;
            }
            z3 = z5;
            tL_messageExtendedMedia = tL_messageExtendedMedia5;
        } else {
            z2 = z;
            sendMessagesHelper = sendMessagesHelper3;
            messageObject2 = messageObject;
            message3 = message3;
            z3 = false;
            tL_messageExtendedMedia = null;
            closestPhotoSizeWithSize = null;
            tL_messageExtendedMedia2 = null;
        }
        TLRPC.MessageMedia messageMedia11 = messageMedia7;
        if (message != null) {
            String str9 = "s";
            boolean z6 = z3;
            if ((messageMedia11 instanceof TLRPC.TL_messageMediaPhoto) && messageMedia11.photo != null && (messageMedia8 instanceof TLRPC.TL_messageMediaPhoto) && messageMedia8.photo != null) {
                if (messageMedia11.ttl_seconds == 0 && !messageObject2.scheduled) {
                    sendMessagesHelper.getMessagesStorage().putSentFile(str, messageMedia11.photo, messageObject2.sentHighQuality ? 6 : 0, "sent_" + message.peer_id.channel_id + "_" + message.id + "_" + DialogObject.getPeerDialogId(message.peer_id) + "_1_" + MessageObject.getMediaSize(messageMedia8));
                }
                if (messageMedia8.photo.sizes.size() == 1 && (((TLRPC.PhotoSize) messageMedia8.photo.sizes.get(0)).location instanceof TLRPC.TL_fileLocationUnavailable)) {
                    messageMedia8.photo.sizes = messageMedia11.photo.sizes;
                } else {
                    int i6 = 0;
                    while (i6 < messageMedia8.photo.sizes.size()) {
                        TLRPC.PhotoSize photoSize2 = (TLRPC.PhotoSize) messageMedia8.photo.sizes.get(i6);
                        if (photoSize2 == null || photoSize2.location == null || photoSize2.type == null) {
                            photoSize = closestPhotoSizeWithSize;
                        } else {
                            int i7 = 0;
                            while (true) {
                                if (i7 < messageMedia11.photo.sizes.size()) {
                                    TLRPC.PhotoSize photoSize3 = (TLRPC.PhotoSize) messageMedia11.photo.sizes.get(i7);
                                    if (photoSize3 == null || photoSize3.location == null || (photoSize3 instanceof TLRPC.TL_photoSizeEmpty) || (str8 = photoSize3.type) == null || !((photoSize2.location.volume_id == -2147483648L && str8.equals(photoSize2.type)) || (photoSize3.w == photoSize2.w && photoSize3.h == photoSize2.h))) {
                                        i7++;
                                        sendMessagesHelper = this;
                                    } else {
                                        String str10 = photoSize2.location.volume_id + "_" + photoSize2.location.local_id;
                                        String str11 = photoSize3.location.volume_id + "_" + photoSize3.location.local_id;
                                        if (!str10.equals(str11)) {
                                            File file4 = new File(FileLoader.getDirectory(4), str10 + ".jpg");
                                            if (messageMedia11.ttl_seconds == 0 && ((messageMedia11.photo.sizes.size() == 1 || photoSize3.w > 90 || photoSize3.h > 90) && !z6)) {
                                                file3 = FileLoader.getInstance(sendMessagesHelper.currentAccount).getPathToAttach(photoSize3);
                                            } else {
                                                file3 = new File(FileLoader.getDirectory(4), str11 + ".jpg");
                                            }
                                            file4.renameTo(file3);
                                            ImageLoader.getInstance().replaceImageInCache(str10, str11, ImageLocation.getForPhoto(photoSize3, messageMedia11.photo), z2);
                                            photoSize2.location = photoSize3.location;
                                            photoSize2.size = photoSize3.size;
                                        }
                                    }
                                } else {
                                    String str12 = photoSize2.location.volume_id + "_" + photoSize2.location.local_id;
                                    new File(FileLoader.getDirectory(4), str12 + ".jpg").delete();
                                    if ("s".equals(photoSize2.type) && closestPhotoSizeWithSize != null) {
                                        photoSize = closestPhotoSizeWithSize;
                                        messageMedia8.photo.sizes.set(i6, photoSize);
                                        ImageLocation forPhoto = ImageLocation.getForPhoto(photoSize, messageMedia11.photo);
                                        ImageLoader.getInstance().replaceImageInCache(str12, forPhoto.getKey(message, null, false), forPhoto, z2);
                                    }
                                }
                                photoSize = closestPhotoSizeWithSize;
                            }
                        }
                        i6++;
                        sendMessagesHelper = this;
                        closestPhotoSizeWithSize = photoSize;
                    }
                }
                if (!z6) {
                    TLRPC.Message message4 = message3;
                    message4.message = message.message;
                    message.attachPath = message4.attachPath;
                } else if (tL_messageExtendedMedia != 0 && tL_messageExtendedMedia2 != 0) {
                    tL_messageExtendedMedia.attachPath = tL_messageExtendedMedia2.attachPath;
                }
                TLRPC.Photo photo9 = messageMedia8.photo;
                TLRPC.Photo photo10 = messageMedia11.photo;
                photo9.id = photo10.id;
                photo9.dc_id = photo10.dc_id;
                photo9.access_hash = photo10.access_hash;
                return;
            }
            if ((messageMedia11 instanceof TLRPC.TL_messageMediaDocument) && messageMedia11.document != null && (messageMedia8 instanceof TLRPC.TL_messageMediaDocument) && messageMedia8.document != null) {
                if (messageMedia11.ttl_seconds == 0 && ((videoEditedInfo = messageObject2.videoEditedInfo) == null || (videoEditedInfo.mediaEntities == null && TextUtils.isEmpty(videoEditedInfo.paintPath) && messageObject2.videoEditedInfo.cropState == null))) {
                    boolean zIsVideoMessage = MessageObject.isVideoMessage(message);
                    if ((zIsVideoMessage || MessageObject.isGifMessage(message)) && MessageObject.isGifDocument(messageMedia11.document) == MessageObject.isGifDocument(messageMedia8.document)) {
                        if (messageObject2.scheduled) {
                            sendMessagesHelper2 = this;
                        } else {
                            sendMessagesHelper2 = this;
                            MessageObject messageObject3 = new MessageObject(sendMessagesHelper2.currentAccount, message, false, false);
                            sendMessagesHelper2.getMessagesStorage().putSentFile(str, messageMedia11.document, 2, "sent_" + message.peer_id.channel_id + "_" + message.id + "_" + DialogObject.getPeerDialogId(message.peer_id) + "_" + messageObject3.type + "_" + messageObject3.getSize());
                        }
                        if (zIsVideoMessage) {
                            message.attachPath = message3.attachPath;
                        }
                    } else {
                        sendMessagesHelper2 = this;
                        str9 = "s";
                        tL_messageExtendedMedia = tL_messageExtendedMedia;
                        if (!MessageObject.isVoiceMessage(message) && !MessageObject.isRoundVideoMessage(message) && !messageObject2.scheduled) {
                            MessageObject messageObject4 = new MessageObject(sendMessagesHelper2.currentAccount, message, false, false);
                            sendMessagesHelper2.getMessagesStorage().putSentFile(str, messageMedia11.document, 1, "sent_" + message.peer_id.channel_id + "_" + message.id + "_" + DialogObject.getPeerDialogId(message.peer_id) + "_" + messageObject4.type + "_" + messageObject4.getSize());
                        }
                        photo = messageMedia8.video_cover;
                        if (photo == null && (photo2 = messageMedia11.video_cover) != null) {
                            TLRPC.PhotoSize closestPhotoSizeWithSize8 = FileLoader.getClosestPhotoSizeWithSize(photo.sizes, 40);
                            if (photo2 != null) {
                                closestPhotoSizeWithSize8 = FileLoader.getClosestPhotoSizeWithSize(photo2.sizes, 40);
                            }
                            if (photo.sizes.size() == 1 && (((TLRPC.PhotoSize) photo.sizes.get(0)).location instanceof TLRPC.TL_fileLocationUnavailable)) {
                                photo.sizes = photo2.sizes;
                            } else {
                                int i8 = 0;
                                while (i8 < photo.sizes.size()) {
                                    TLRPC.PhotoSize photoSize4 = (TLRPC.PhotoSize) photo.sizes.get(i8);
                                    if (photoSize4 == null || photoSize4.location == null || photoSize4.type == null) {
                                        str5 = str;
                                        messageMedia = messageMedia8;
                                    } else {
                                        int i9 = 0;
                                        while (true) {
                                            if (i9 < photo2.sizes.size()) {
                                                TLRPC.PhotoSize photoSize5 = (TLRPC.PhotoSize) photo2.sizes.get(i9);
                                                if (photoSize5 == null || photoSize5.location == null || (photoSize5 instanceof TLRPC.TL_photoSizeEmpty) || (str7 = photoSize5.type) == null) {
                                                    str5 = str;
                                                    messageMedia = messageMedia8;
                                                } else {
                                                    str5 = str;
                                                    messageMedia = messageMedia8;
                                                    if ((photoSize4.location.volume_id == -2147483648L && str7.equals(photoSize4.type)) || (photoSize5.w == photoSize4.w && photoSize5.h == photoSize4.h)) {
                                                        String str13 = photoSize4.location.volume_id + "_" + photoSize4.location.local_id;
                                                        String str14 = photoSize5.location.volume_id + "_" + photoSize5.location.local_id;
                                                        if (!str13.equals(str14)) {
                                                            File file5 = new File(FileLoader.getDirectory(4), str13 + ".jpg");
                                                            if (messageMedia11.ttl_seconds != 0 || (photo2.sizes.size() != 1 && photoSize5.w <= 90 && photoSize5.h <= 90)) {
                                                                file2 = new File(FileLoader.getDirectory(4), str14 + ".jpg");
                                                            } else if (!z6) {
                                                                file2 = FileLoader.getInstance(sendMessagesHelper2.currentAccount).getPathToAttach(photoSize5, true);
                                                            } else {
                                                                file2 = new File(FileLoader.getDirectory(4), str14 + ".jpg");
                                                            }
                                                            file5.renameTo(file2);
                                                            ImageLoader.getInstance().replaceImageInCache(str13, str14, ImageLocation.getForPhoto(photoSize5, photo2), z);
                                                            photoSize4.location = photoSize5.location;
                                                            photoSize4.size = photoSize5.size;
                                                        }
                                                    }
                                                }
                                                i9++;
                                                sendMessagesHelper2 = this;
                                                str = str5;
                                                messageMedia8 = messageMedia;
                                            } else {
                                                str5 = str;
                                                messageMedia = messageMedia8;
                                                String str15 = photoSize4.location.volume_id + "_" + photoSize4.location.local_id;
                                                new File(FileLoader.getDirectory(4), str15 + ".jpg").delete();
                                                str6 = str9;
                                                if (str6.equals(photoSize4.type) && closestPhotoSizeWithSize8 != null) {
                                                    photo.sizes.set(i8, closestPhotoSizeWithSize8);
                                                    ImageLocation forPhoto2 = ImageLocation.getForPhoto(closestPhotoSizeWithSize8, photo2);
                                                    ImageLoader.getInstance().replaceImageInCache(str15, forPhoto2.getKey(message, null, false), forPhoto2, z);
                                                }
                                            }
                                            i8++;
                                            sendMessagesHelper2 = this;
                                            str9 = str6;
                                            str = str5;
                                            messageMedia8 = messageMedia;
                                        }
                                    }
                                    str6 = str9;
                                    i8++;
                                    sendMessagesHelper2 = this;
                                    str9 = str6;
                                    str = str5;
                                    messageMedia8 = messageMedia;
                                }
                            }
                            str2 = str;
                            TLRPC.MessageMedia messageMedia12 = messageMedia8;
                            bArr = null;
                            photo.id = photo2.id;
                            photo.dc_id = photo2.dc_id;
                            photo.access_hash = photo2.access_hash;
                            messageMedia8 = messageMedia12;
                        } else {
                            str2 = str;
                            bArr = null;
                            closestPhotoSizeWithSize2 = FileLoader.getClosestPhotoSizeWithSize(messageMedia8.document.thumbs, 320);
                            closestPhotoSizeWithSize3 = FileLoader.getClosestPhotoSizeWithSize(messageMedia11.document.thumbs, 320);
                            if (closestPhotoSizeWithSize2 == null && (fileLocation2 = closestPhotoSizeWithSize2.location) != null && fileLocation2.volume_id == -2147483648L && closestPhotoSizeWithSize3 != null && closestPhotoSizeWithSize3.location != null && !(closestPhotoSizeWithSize3 instanceof TLRPC.TL_photoSizeEmpty) && !(closestPhotoSizeWithSize2 instanceof TLRPC.TL_photoSizeEmpty)) {
                                String str16 = closestPhotoSizeWithSize2.location.volume_id + "_" + closestPhotoSizeWithSize2.location.local_id;
                                String str17 = closestPhotoSizeWithSize3.location.volume_id + "_" + closestPhotoSizeWithSize3.location.local_id;
                                if (!str16.equals(str17)) {
                                    new File(FileLoader.getDirectory(4), str16 + ".jpg").renameTo(new File(FileLoader.getDirectory(4), str17 + ".jpg"));
                                    ImageLoader.getInstance().replaceImageInCache(str16, str17, ImageLocation.getForDocument(closestPhotoSizeWithSize3, messageMedia11.document), z);
                                    closestPhotoSizeWithSize2.location = closestPhotoSizeWithSize3.location;
                                    closestPhotoSizeWithSize2.size = closestPhotoSizeWithSize3.size;
                                }
                            } else if (closestPhotoSizeWithSize3 == null && closestPhotoSizeWithSize2 != null && MessageObject.isStickerMessage(message) && (fileLocation = closestPhotoSizeWithSize2.location) != null) {
                                closestPhotoSizeWithSize3.location = fileLocation;
                            } else if (closestPhotoSizeWithSize2 != null || (closestPhotoSizeWithSize2.location instanceof TLRPC.TL_fileLocationUnavailable) || (closestPhotoSizeWithSize2 instanceof TLRPC.TL_photoSizeEmpty)) {
                                messageMedia8.document.thumbs = messageMedia11.document.thumbs;
                            }
                        }
                        TLRPC.Document document7 = messageMedia8.document;
                        TLRPC.Document document8 = messageMedia11.document;
                        document7.dc_id = document8.dc_id;
                        document7.id = document8.id;
                        document7.access_hash = document8.access_hash;
                        i3 = 0;
                        while (true) {
                            if (i3 < messageMedia8.document.attributes.size()) {
                                bArr2 = bArr;
                                break;
                            }
                            documentAttribute2 = messageMedia8.document.attributes.get(i3);
                            if (documentAttribute2 instanceof TLRPC.TL_documentAttributeAudio) {
                                bArr2 = documentAttribute2.waveform;
                                break;
                            }
                            i3++;
                        }
                        messageMedia8.document.attributes = messageMedia11.document.attributes;
                        if (bArr2 != null) {
                            for (i4 = 0; i4 < messageMedia8.document.attributes.size(); i4++) {
                                documentAttribute = messageMedia8.document.attributes.get(i4);
                                if (documentAttribute instanceof TLRPC.TL_documentAttributeAudio) {
                                    documentAttribute.waveform = bArr2;
                                    documentAttribute.flags |= 4;
                                }
                            }
                        }
                        TLRPC.Document document9 = messageMedia8.document;
                        TLRPC.Document document10 = messageMedia11.document;
                        document9.size = document10.size;
                        document9.mime_type = document10.mime_type;
                        if ((message.flags & 4) != 0 && ((MessageObject.isOut(message) || message.dialog_id == getUserConfig().getClientUserId()) && !MessageObject.isQuickReply(message))) {
                            if (MessageObject.isNewGifDocument(messageMedia11.document)) {
                                if (MessageObject.isDocumentHasAttachedStickers(messageMedia11.document) ? getMessagesController().saveGifsWithStickers : true) {
                                    getMediaDataController().addRecentGif(messageMedia11.document, message.date, true);
                                }
                            } else if (MessageObject.isStickerDocument(messageMedia11.document) || MessageObject.isAnimatedStickerDocument(messageMedia11.document, true)) {
                                getMediaDataController().addRecentSticker(0, message, messageMedia11.document, message.date, false);
                            }
                        }
                        if (tL_messageExtendedMedia != null) {
                            tL_messageExtendedMedia3 = tL_messageExtendedMedia;
                            str3 = tL_messageExtendedMedia3.attachPath;
                        } else {
                            tL_messageExtendedMedia3 = tL_messageExtendedMedia;
                            str3 = message2.attachPath;
                        }
                        if (str3 != null) {
                            message2 = message3;
                            if (str3.startsWith(FileLoader.getDirectory(4).getAbsolutePath()) && !MessageObject.isGifDocument(messageMedia11.document)) {
                                file = new File(str3);
                                FileLoader fileLoader = FileLoader.getInstance(this.currentAccount);
                                TLRPC.Document document11 = messageMedia11.document;
                                if (messageMedia11.ttl_seconds != 0) {
                                    message2 = message3;
                                    z4 = true;
                                } else {
                                    message2 = message3;
                                    z4 = false;
                                }
                                pathToAttach = fileLoader.getPathToAttach(document11, z4);
                                if (!file.renameTo(pathToAttach)) {
                                    if (file.exists()) {
                                        if (tL_messageExtendedMedia3 != null) {
                                            tL_messageExtendedMedia3.attachPath = str3;
                                        } else {
                                            message.attachPath = str3;
                                        }
                                    } else if (tL_messageExtendedMedia3 == null) {
                                        messageObject.attachPathExists = false;
                                    }
                                    if (tL_messageExtendedMedia3 == null) {
                                        messageObject.mediaExists = pathToAttach.exists();
                                    }
                                    message.message = message2.message;
                                    return;
                                }
                                if (MessageObject.isVideoMessage(message)) {
                                    messageObject.attachPathExists = true;
                                    return;
                                }
                                messageObject.mediaExists = messageObject.attachPathExists;
                                messageObject.attachPathExists = false;
                                if (tL_messageExtendedMedia3 != null) {
                                    tL_messageExtendedMedia3.attachPath = _UrlKt.FRAGMENT_ENCODE_SET;
                                } else {
                                    message2.attachPath = _UrlKt.FRAGMENT_ENCODE_SET;
                                }
                                if (str2 != null) {
                                    str4 = str2;
                                    if (str4.startsWith("http")) {
                                        getMessagesStorage().addRecentLocalFile(str4, pathToAttach.toString(), messageMedia8.document);
                                        return;
                                    }
                                    return;
                                }
                                return;
                            }
                        }
                        message2 = message3;
                        message2 = message3;
                        message2 = message3;
                        message2 = message3;
                        if (tL_messageExtendedMedia3 != null) {
                            tL_messageExtendedMedia3.attachPath = str3;
                            return;
                        } else {
                            message.attachPath = str3;
                            message.message = message2.message;
                            return;
                        }
                    }
                } else {
                    sendMessagesHelper2 = this;
                    str9 = "s";
                    tL_messageExtendedMedia = tL_messageExtendedMedia;
                }
                photo = messageMedia8.video_cover;
                if (photo == null) {
                    str2 = str;
                    bArr = null;
                    closestPhotoSizeWithSize2 = FileLoader.getClosestPhotoSizeWithSize(messageMedia8.document.thumbs, 320);
                    closestPhotoSizeWithSize3 = FileLoader.getClosestPhotoSizeWithSize(messageMedia11.document.thumbs, 320);
                    if (closestPhotoSizeWithSize2 == null) {
                        if (closestPhotoSizeWithSize3 == null) {
                            if (closestPhotoSizeWithSize2 != null) {
                                messageMedia8.document.thumbs = messageMedia11.document.thumbs;
                            } else {
                                messageMedia8.document.thumbs = messageMedia11.document.thumbs;
                            }
                        } else if (closestPhotoSizeWithSize2 != null) {
                            messageMedia8.document.thumbs = messageMedia11.document.thumbs;
                        } else {
                            messageMedia8.document.thumbs = messageMedia11.document.thumbs;
                        }
                    } else if (closestPhotoSizeWithSize3 == null) {
                        if (closestPhotoSizeWithSize2 != null) {
                            messageMedia8.document.thumbs = messageMedia11.document.thumbs;
                        } else {
                            messageMedia8.document.thumbs = messageMedia11.document.thumbs;
                        }
                    } else if (closestPhotoSizeWithSize2 != null) {
                        messageMedia8.document.thumbs = messageMedia11.document.thumbs;
                    } else {
                        messageMedia8.document.thumbs = messageMedia11.document.thumbs;
                    }
                } else {
                    str2 = str;
                    bArr = null;
                    closestPhotoSizeWithSize2 = FileLoader.getClosestPhotoSizeWithSize(messageMedia8.document.thumbs, 320);
                    closestPhotoSizeWithSize3 = FileLoader.getClosestPhotoSizeWithSize(messageMedia11.document.thumbs, 320);
                    if (closestPhotoSizeWithSize2 == null) {
                        if (closestPhotoSizeWithSize3 == null) {
                            if (closestPhotoSizeWithSize2 != null) {
                                messageMedia8.document.thumbs = messageMedia11.document.thumbs;
                            } else {
                                messageMedia8.document.thumbs = messageMedia11.document.thumbs;
                            }
                        } else if (closestPhotoSizeWithSize2 != null) {
                            messageMedia8.document.thumbs = messageMedia11.document.thumbs;
                        } else {
                            messageMedia8.document.thumbs = messageMedia11.document.thumbs;
                        }
                    } else if (closestPhotoSizeWithSize3 == null) {
                        if (closestPhotoSizeWithSize2 != null) {
                            messageMedia8.document.thumbs = messageMedia11.document.thumbs;
                        } else {
                            messageMedia8.document.thumbs = messageMedia11.document.thumbs;
                        }
                    } else if (closestPhotoSizeWithSize2 != null) {
                        messageMedia8.document.thumbs = messageMedia11.document.thumbs;
                    } else {
                        messageMedia8.document.thumbs = messageMedia11.document.thumbs;
                    }
                }
                TLRPC.Document document12 = messageMedia8.document;
                TLRPC.Document document13 = messageMedia11.document;
                document12.dc_id = document13.dc_id;
                document12.id = document13.id;
                document12.access_hash = document13.access_hash;
                i3 = 0;
                while (true) {
                    if (i3 < messageMedia8.document.attributes.size()) {
                        bArr2 = bArr;
                        break;
                    }
                    documentAttribute2 = messageMedia8.document.attributes.get(i3);
                    if (documentAttribute2 instanceof TLRPC.TL_documentAttributeAudio) {
                        bArr2 = documentAttribute2.waveform;
                        break;
                    }
                    i3++;
                }
                messageMedia8.document.attributes = messageMedia11.document.attributes;
                if (bArr2 != null) {
                    while (i4 < messageMedia8.document.attributes.size()) {
                        documentAttribute = messageMedia8.document.attributes.get(i4);
                        if (documentAttribute instanceof TLRPC.TL_documentAttributeAudio) {
                            documentAttribute.waveform = bArr2;
                            documentAttribute.flags |= 4;
                        }
                    }
                }
                TLRPC.Document document14 = messageMedia8.document;
                TLRPC.Document document15 = messageMedia11.document;
                document14.size = document15.size;
                document14.mime_type = document15.mime_type;
                if ((message.flags & 4) != 0) {
                }
                if (tL_messageExtendedMedia != null) {
                    tL_messageExtendedMedia3 = tL_messageExtendedMedia;
                    str3 = tL_messageExtendedMedia3.attachPath;
                } else {
                    tL_messageExtendedMedia3 = tL_messageExtendedMedia;
                    str3 = message2.attachPath;
                }
                if (str3 != null) {
                    message2 = message3;
                    if (str3.startsWith(FileLoader.getDirectory(4).getAbsolutePath())) {
                        file = new File(str3);
                        FileLoader fileLoader2 = FileLoader.getInstance(this.currentAccount);
                        TLRPC.Document document16 = messageMedia11.document;
                        if (messageMedia11.ttl_seconds != 0) {
                            message2 = message3;
                            z4 = true;
                        } else {
                            message2 = message3;
                            z4 = false;
                        }
                        pathToAttach = fileLoader2.getPathToAttach(document16, z4);
                        if (!file.renameTo(pathToAttach)) {
                            if (file.exists()) {
                                if (tL_messageExtendedMedia3 != null) {
                                    tL_messageExtendedMedia3.attachPath = str3;
                                } else {
                                    message.attachPath = str3;
                                }
                            } else if (tL_messageExtendedMedia3 == null) {
                                messageObject.attachPathExists = false;
                            }
                            if (tL_messageExtendedMedia3 == null) {
                                messageObject.mediaExists = pathToAttach.exists();
                            }
                            message.message = message2.message;
                            return;
                        }
                        if (MessageObject.isVideoMessage(message)) {
                            messageObject.attachPathExists = true;
                            return;
                        }
                        messageObject.mediaExists = messageObject.attachPathExists;
                        messageObject.attachPathExists = false;
                        if (tL_messageExtendedMedia3 != null) {
                            tL_messageExtendedMedia3.attachPath = _UrlKt.FRAGMENT_ENCODE_SET;
                        } else {
                            message2.attachPath = _UrlKt.FRAGMENT_ENCODE_SET;
                        }
                        if (str2 != null) {
                            str4 = str2;
                            if (str4.startsWith("http")) {
                                getMessagesStorage().addRecentLocalFile(str4, pathToAttach.toString(), messageMedia8.document);
                                return;
                            }
                            return;
                        }
                        return;
                    }
                }
                message2 = message3;
                message2 = message3;
                message2 = message3;
                message2 = message3;
                if (tL_messageExtendedMedia3 != null) {
                    tL_messageExtendedMedia3.attachPath = str3;
                    return;
                } else {
                    message.attachPath = str3;
                    message.message = message2.message;
                    return;
                }
            }
            TLRPC.MessageMedia messageMedia13 = message.media;
            if ((messageMedia13 instanceof TLRPC.TL_messageMediaContact) && (message3.media instanceof TLRPC.TL_messageMediaContact)) {
                message3.media = messageMedia13;
                return;
            }
            if (messageMedia13 instanceof TLRPC.TL_messageMediaWebPage) {
                message3.media = messageMedia13;
                return;
            }
            if (messageMedia13 instanceof TLRPC.TL_messageMediaGeo) {
                TLRPC.GeoPoint geoPoint = messageMedia13.geo;
                TLRPC.GeoPoint geoPoint2 = message3.media.geo;
                geoPoint.lat = geoPoint2.lat;
                geoPoint._long = geoPoint2._long;
                return;
            }
            if ((messageMedia13 instanceof TLRPC.TL_messageMediaGame) || (messageMedia13 instanceof TLRPC.TL_messageMediaInvoice)) {
                message3.media = messageMedia13;
                if (!TextUtils.isEmpty(message.message)) {
                    message3.entities = message.entities;
                    message3.message = message.message;
                }
                TLRPC.ReplyMarkup replyMarkup = message.reply_markup;
                if (replyMarkup != null) {
                    message3.reply_markup = replyMarkup;
                    message3.flags |= 64;
                    return;
                }
                return;
            }
            if (messageMedia13 instanceof TLRPC.TL_messageMediaPoll) {
                message3.media = messageMedia13;
            }
        }
    }

    private void putToDelayedMessages(String str, DelayedMessage delayedMessage) {
        ArrayList<DelayedMessage> arrayList = this.delayedMessages.get(str);
        if (arrayList == null) {
            arrayList = new ArrayList<>();
            this.delayedMessages.put(str, arrayList);
        }
        arrayList.add(delayedMessage);
    }

    protected ArrayList<DelayedMessage> getDelayedMessages(String str) {
        return this.delayedMessages.get(str);
    }

    public long getNextRandomId() {
        long jNextLong = 0;
        while (jNextLong == 0) {
            jNextLong = Utilities.random.nextLong();
        }
        return jNextLong;
    }

    public void checkUnsentMessages() {
        getMessagesStorage().getUnsentMessages(MediaDataController.MAX_STYLE_RUNS_COUNT);
    }

    protected void processUnsentMessages(final ArrayList<TLRPC.Message> arrayList, final ArrayList<TLRPC.Message> arrayList2, final ArrayList<TLRPC.User> arrayList3, final ArrayList<TLRPC.Chat> arrayList4, final ArrayList<TLRPC.EncryptedChat> arrayList5) {
        AndroidUtilities.runOnUIThread(new Runnable() { // from class: org.telegram.messenger.SendMessagesHelper$$ExternalSyntheticLambda85
            @Override // java.lang.Runnable
            public final void run() {
                this.f$0.lambda$processUnsentMessages$89(arrayList3, arrayList4, arrayList5, arrayList, arrayList2);
            }
        });
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$processUnsentMessages$89(ArrayList arrayList, ArrayList arrayList2, ArrayList arrayList3, ArrayList arrayList4, ArrayList arrayList5) {
        HashMap map;
        getMessagesController().putUsers(arrayList, true);
        getMessagesController().putChats(arrayList2, true);
        getMessagesController().putEncryptedChats(arrayList3, true);
        int size = arrayList4.size();
        for (int i = 0; i < size; i++) {
            MessageObject messageObject = new MessageObject(this.currentAccount, (TLRPC.Message) arrayList4.get(i), false, true);
            long groupId = messageObject.getGroupId();
            if (groupId != 0 && (map = messageObject.messageOwner.params) != null && !map.containsKey("final") && (i == size - 1 || ((TLRPC.Message) arrayList4.get(i + 1)).grouped_id != groupId)) {
                messageObject.messageOwner.params.put("final", "1");
            }
            retrySendMessage(messageObject, true, 0L);
        }
        if (arrayList5 != null) {
            for (int i2 = 0; i2 < arrayList5.size(); i2++) {
                MessageObject messageObject2 = new MessageObject(this.currentAccount, (TLRPC.Message) arrayList5.get(i2), false, true);
                messageObject2.scheduled = true;
                retrySendMessage(messageObject2, true, 0L);
            }
        }
    }

    public ImportingStickers getImportingStickers(String str) {
        return this.importingStickersMap.get(str);
    }

    public ImportingHistory getImportingHistory(long j) {
        return (ImportingHistory) this.importingHistoryMap.get(j);
    }

    public boolean isImportingStickers() {
        return this.importingStickersMap.size() != 0;
    }

    public boolean isImportingHistory() {
        return this.importingHistoryMap.size() != 0;
    }

    public void prepareImportHistory(final long j, final Uri uri, final ArrayList<Uri> arrayList, final MessagesStorage.LongCallback longCallback) {
        if (this.importingHistoryMap.get(j) != null) {
            longCallback.run(0L);
            return;
        }
        if (DialogObject.isChatDialog(j)) {
            long j2 = -j;
            TLRPC.Chat chat = getMessagesController().getChat(Long.valueOf(j2));
            if (chat != null && !chat.megagroup) {
                getMessagesController().convertToMegaGroup(null, j2, null, new MessagesStorage.LongCallback() { // from class: org.telegram.messenger.SendMessagesHelper$$ExternalSyntheticLambda33
                    @Override // org.telegram.messenger.MessagesStorage.LongCallback
                    public final void run(long j3) {
                        this.f$0.lambda$prepareImportHistory$90(uri, arrayList, longCallback, j3);
                    }
                });
                return;
            }
        }
        new Thread(new Runnable() { // from class: org.telegram.messenger.SendMessagesHelper$$ExternalSyntheticLambda34
            @Override // java.lang.Runnable
            public final void run() {
                this.f$0.lambda$prepareImportHistory$95(arrayList, j, uri, longCallback);
            }
        }).start();
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$prepareImportHistory$90(Uri uri, ArrayList arrayList, MessagesStorage.LongCallback longCallback, long j) {
        if (j != 0) {
            prepareImportHistory(-j, uri, arrayList, longCallback);
        } else {
            longCallback.run(0L);
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    /* JADX WARN: Code duplicated, block: B:100:0x0135 A[SYNTHETIC] */
    /* JADX WARN: Code duplicated, block: B:103:0x0149 A[SYNTHETIC] */
    /* JADX WARN: Code duplicated, block: B:79:0x0133 A[DONT_INVERT] */
    public /* synthetic */ void lambda$prepareImportHistory$95(ArrayList arrayList, final long j, Uri uri, final MessagesStorage.LongCallback longCallback) {
        ArrayList arrayList2 = arrayList != null ? arrayList : new ArrayList();
        final ImportingHistory importingHistory = new ImportingHistory();
        importingHistory.mediaPaths = arrayList2;
        importingHistory.dialogId = j;
        importingHistory.peer = getMessagesController().getInputPeer(j);
        final HashMap map = new HashMap();
        int size = arrayList2.size();
        int i = 0;
        while (i < size + 1) {
            Uri uri2 = i == 0 ? uri : (Uri) arrayList2.get(i - 1);
            if (uri2 != null && !AndroidUtilities.isInternalUri(uri2)) {
                String strFixFileName = FileLoader.fixFileName(MediaController.getFileName(uri));
                String str = (strFixFileName == null || !strFixFileName.endsWith(".zip")) ? "txt" : "zip";
                String strCopyFileToCache = MediaController.copyFileToCache(uri2, str);
                if ("zip".equals(str)) {
                    File file = new File(strCopyFileToCache);
                    try {
                        try {
                            ZipInputStream zipInputStream = new ZipInputStream(new FileInputStream(file));
                            try {
                                ZipEntry nextEntry = zipInputStream.getNextEntry();
                                while (nextEntry != null) {
                                    String name = nextEntry.getName();
                                    if (name == null) {
                                        nextEntry = zipInputStream.getNextEntry();
                                    } else {
                                        int iLastIndexOf = name.lastIndexOf("/");
                                        if (iLastIndexOf >= 0) {
                                            name = name.substring(iLastIndexOf + 1);
                                        }
                                        if (name.endsWith(".txt")) {
                                            File fileCreateFileInCache = MediaController.createFileInCache(name, "txt");
                                            strCopyFileToCache = fileCreateFileInCache.getAbsolutePath();
                                            FileOutputStream fileOutputStream = new FileOutputStream(fileCreateFileInCache);
                                            byte[] bArr = new byte[1024];
                                            while (true) {
                                                int i2 = zipInputStream.read(bArr);
                                                if (i2 <= 0) {
                                                    break;
                                                } else {
                                                    fileOutputStream.write(bArr, 0, i2);
                                                }
                                            }
                                            fileOutputStream.close();
                                            break;
                                        }
                                        nextEntry = zipInputStream.getNextEntry();
                                    }
                                }
                                zipInputStream.closeEntry();
                                zipInputStream.close();
                                try {
                                    file.delete();
                                } catch (Exception e) {
                                    FileLog.e(e);
                                }
                            } catch (Throwable th) {
                                try {
                                    zipInputStream.close();
                                } catch (Throwable th2) {
                                    th.addSuppressed(th2);
                                }
                                throw th;
                            }
                        } catch (IOException e2) {
                            FileLog.e(e2);
                        }
                    } catch (Exception e3) {
                        FileLog.e(e3);
                    }
                }
                if (strCopyFileToCache == null) {
                    continue;
                } else {
                    File file2 = new File(strCopyFileToCache);
                    if (file2.exists()) {
                        long length = file2.length();
                        if (length != 0) {
                            importingHistory.totalSize += length;
                            if (i != 0) {
                                importingHistory.uploadMedia.add(strCopyFileToCache);
                            } else {
                                if (length > 33554432) {
                                    file2.delete();
                                    AndroidUtilities.runOnUIThread(new Runnable() { // from class: org.telegram.messenger.SendMessagesHelper$$ExternalSyntheticLambda68
                                        @Override // java.lang.Runnable
                                        public final void run() {
                                            SendMessagesHelper.$r8$lambda$uTT8YrZ8JnVqxP3dplbdrGCaA_E(longCallback);
                                        }
                                    });
                                    return;
                                }
                                importingHistory.historyPath = strCopyFileToCache;
                            }
                            importingHistory.uploadSet.add(strCopyFileToCache);
                            map.put(strCopyFileToCache, importingHistory);
                        } else if (i == 0) {
                            AndroidUtilities.runOnUIThread(new Runnable() { // from class: org.telegram.messenger.SendMessagesHelper$$ExternalSyntheticLambda67
                                @Override // java.lang.Runnable
                                public final void run() {
                                    longCallback.run(0L);
                                }
                            });
                            return;
                        }
                    } else if (i == 0) {
                        AndroidUtilities.runOnUIThread(new Runnable() { // from class: org.telegram.messenger.SendMessagesHelper$$ExternalSyntheticLambda67
                            @Override // java.lang.Runnable
                            public final void run() {
                                longCallback.run(0L);
                            }
                        });
                        return;
                    }
                }
            } else if (i == 0) {
                AndroidUtilities.runOnUIThread(new Runnable() { // from class: org.telegram.messenger.SendMessagesHelper$$ExternalSyntheticLambda66
                    @Override // java.lang.Runnable
                    public final void run() {
                        longCallback.run(0L);
                    }
                });
                return;
            }
            i++;
        }
        AndroidUtilities.runOnUIThread(new Runnable() { // from class: org.telegram.messenger.SendMessagesHelper$$ExternalSyntheticLambda69
            @Override // java.lang.Runnable
            public final void run() {
                this.f$0.lambda$prepareImportHistory$94(map, j, importingHistory, longCallback);
            }
        });
    }

    public static /* synthetic */ void $r8$lambda$uTT8YrZ8JnVqxP3dplbdrGCaA_E(MessagesStorage.LongCallback longCallback) {
        Toast.makeText(ApplicationLoader.applicationContext, LocaleController.getString(R.string.ImportFileTooLarge), 0).show();
        longCallback.run(0L);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$prepareImportHistory$94(HashMap map, long j, ImportingHistory importingHistory, MessagesStorage.LongCallback longCallback) {
        this.importingHistoryFiles.putAll(map);
        this.importingHistoryMap.put(j, importingHistory);
        getFileLoader().uploadFile(importingHistory.historyPath, false, true, 0L, 67108864, true);
        getNotificationCenter().lambda$postNotificationNameOnUIThread$1(NotificationCenter.historyImportProgressChanged, Long.valueOf(j));
        longCallback.run(j);
        try {
            ApplicationLoader.applicationContext.startService(new Intent(ApplicationLoader.applicationContext, (Class<?>) ImportingService.class));
        } catch (Throwable th) {
            FileLog.e(th);
        }
    }

    public void prepareImportStickers(final String str, final String str2, final String str3, final ArrayList<ImportingSticker> arrayList, final MessagesStorage.StringCallback stringCallback) {
        if (this.importingStickersMap.get(str2) != null) {
            stringCallback.run(null);
        } else {
            new Thread(new Runnable() { // from class: org.telegram.messenger.SendMessagesHelper$$ExternalSyntheticLambda19
                @Override // java.lang.Runnable
                public final void run() {
                    this.f$0.lambda$prepareImportStickers$98(str, str2, str3, arrayList, stringCallback);
                }
            }).start();
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    /* JADX WARN: Code duplicated, block: B:10:0x004c A[DONT_INVERT] */
    /* JADX WARN: Code duplicated, block: B:17:0x004e A[SYNTHETIC] */
    /* JADX WARN: Code duplicated, block: B:19:0x0057 A[SYNTHETIC] */
    public /* synthetic */ void lambda$prepareImportStickers$98(String str, final String str2, String str3, ArrayList arrayList, final MessagesStorage.StringCallback stringCallback) {
        final ImportingStickers importingStickers = new ImportingStickers();
        importingStickers.title = str;
        importingStickers.shortName = str2;
        importingStickers.software = str3;
        final HashMap map = new HashMap();
        int size = arrayList.size();
        for (int i = 0; i < size; i++) {
            ImportingSticker importingSticker = (ImportingSticker) arrayList.get(i);
            File file = new File(importingSticker.path);
            if (file.exists()) {
                long length = file.length();
                if (length != 0) {
                    importingStickers.totalSize += length;
                    importingStickers.uploadMedia.add(importingSticker);
                    importingStickers.uploadSet.put(importingSticker.path, importingSticker);
                    map.put(importingSticker.path, importingStickers);
                } else if (i == 0) {
                    AndroidUtilities.runOnUIThread(new Runnable() { // from class: org.telegram.messenger.SendMessagesHelper$$ExternalSyntheticLambda61
                        @Override // java.lang.Runnable
                        public final void run() {
                            stringCallback.run(null);
                        }
                    });
                    return;
                }
            } else if (i == 0) {
                AndroidUtilities.runOnUIThread(new Runnable() { // from class: org.telegram.messenger.SendMessagesHelper$$ExternalSyntheticLambda61
                    @Override // java.lang.Runnable
                    public final void run() {
                        stringCallback.run(null);
                    }
                });
                return;
            }
        }
        AndroidUtilities.runOnUIThread(new Runnable() { // from class: org.telegram.messenger.SendMessagesHelper$$ExternalSyntheticLambda62
            @Override // java.lang.Runnable
            public final void run() {
                this.f$0.lambda$prepareImportStickers$97(importingStickers, map, str2, stringCallback);
            }
        });
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$prepareImportStickers$97(ImportingStickers importingStickers, HashMap map, String str, MessagesStorage.StringCallback stringCallback) {
        if (importingStickers.uploadMedia.get(0).item != null) {
            importingStickers.startImport();
        } else {
            this.importingStickersFiles.putAll(map);
            this.importingStickersMap.put(str, importingStickers);
            importingStickers.initImport();
            getNotificationCenter().lambda$postNotificationNameOnUIThread$1(NotificationCenter.historyImportProgressChanged, str);
            stringCallback.run(str);
        }
        try {
            ApplicationLoader.applicationContext.startService(new Intent(ApplicationLoader.applicationContext, (Class<?>) ImportingService.class));
        } catch (Throwable th) {
            FileLog.e(th);
        }
    }

    public TLRPC.TL_photo generatePhotoSizes(String str, Uri uri) {
        return generatePhotoSizes(null, str, uri, false);
    }

    public TLRPC.TL_photo generatePhotoSizes(TLRPC.TL_photo tL_photo, String str, Uri uri, boolean z) {
        TLRPC.PhotoSize photoSizeScaleAndSaveImage;
        Bitmap bitmap;
        Bitmap bitmapLoadBitmap = ImageLoader.loadBitmap(str, uri, AndroidUtilities.getPhotoSize(z), AndroidUtilities.getPhotoSize(z), true);
        if (bitmapLoadBitmap == null) {
            bitmapLoadBitmap = ImageLoader.loadBitmap(str, uri, 800.0f, 800.0f, true);
        }
        Bitmap bitmap2 = bitmapLoadBitmap;
        ArrayList arrayList = new ArrayList();
        TLRPC.PhotoSize photoSizeScaleAndSaveImage2 = ImageLoader.scaleAndSaveImage(bitmap2, 90.0f, 90.0f, 55, true);
        if (photoSizeScaleAndSaveImage2 != null) {
            arrayList.add(photoSizeScaleAndSaveImage2);
        }
        if (z) {
            bitmap = bitmap2;
            photoSizeScaleAndSaveImage = ImageLoader.scaleAndSaveImage(null, bitmap, Bitmap.CompressFormat.JPEG, true, AndroidUtilities.getPhotoSize(z), AndroidUtilities.getPhotoSize(z), 99, false, 101, 101, false);
        } else {
            photoSizeScaleAndSaveImage = ImageLoader.scaleAndSaveImage(bitmap2, AndroidUtilities.getPhotoSize(z), AndroidUtilities.getPhotoSize(z), true, 80, false, 101, 101);
            bitmap = bitmap2;
        }
        if (photoSizeScaleAndSaveImage != null) {
            arrayList.add(photoSizeScaleAndSaveImage);
        }
        if (bitmap != null) {
            bitmap.recycle();
        }
        if (arrayList.isEmpty()) {
            return null;
        }
        getUserConfig().saveConfig(false);
        TLRPC.TL_photo tL_photo2 = tL_photo == null ? new TLRPC.TL_photo() : tL_photo;
        tL_photo2.date = getConnectionsManager().getCurrentTime();
        tL_photo2.sizes = arrayList;
        tL_photo2.file_reference = new byte[0];
        return tL_photo2;
    }

    /* JADX WARN: Code duplicated, block: B:104:0x0175  */
    /* JADX WARN: Code duplicated, block: B:141:0x01f2  */
    /* JADX WARN: Code duplicated, block: B:143:0x0204  */
    /* JADX WARN: Code duplicated, block: B:146:0x020e  */
    /* JADX WARN: Code duplicated, block: B:149:0x0216  */
    /* JADX WARN: Code duplicated, block: B:150:0x021a  */
    /* JADX WARN: Code duplicated, block: B:152:0x0221  */
    /* JADX WARN: Code duplicated, block: B:154:0x0229  */
    /* JADX WARN: Code duplicated, block: B:155:0x022e A[DONT_INVERT] */
    /* JADX WARN: Code duplicated, block: B:156:0x0230  */
    /* JADX WARN: Code duplicated, block: B:159:0x024d  */
    /* JADX WARN: Code duplicated, block: B:160:0x0264  */
    /* JADX WARN: Code duplicated, block: B:163:0x0271 A[ADDED_TO_REGION] */
    /* JADX WARN: Code duplicated, block: B:169:0x0298  */
    /* JADX WARN: Code duplicated, block: B:177:0x02ac A[ADDED_TO_REGION] */
    /* JADX WARN: Code duplicated, block: B:187:0x02cf  */
    /* JADX WARN: Code duplicated, block: B:205:0x0342  */
    /* JADX WARN: Code duplicated, block: B:208:0x035c  */
    /* JADX WARN: Code duplicated, block: B:210:0x038e  */
    /* JADX WARN: Code duplicated, block: B:213:0x039b  */
    /* JADX WARN: Code duplicated, block: B:215:0x03a2  */
    /* JADX WARN: Code duplicated, block: B:216:0x03a5  */
    /* JADX WARN: Code duplicated, block: B:219:0x03af  */
    /* JADX WARN: Code duplicated, block: B:220:0x03b1  */
    /* JADX WARN: Code duplicated, block: B:223:0x03b8  */
    /* JADX WARN: Code duplicated, block: B:224:0x03ba  */
    /* JADX WARN: Code duplicated, block: B:227:0x03c1  */
    /* JADX WARN: Code duplicated, block: B:228:0x03c4  */
    /* JADX WARN: Code duplicated, block: B:231:0x03cb  */
    /* JADX WARN: Code duplicated, block: B:232:0x03cd  */
    /* JADX WARN: Code duplicated, block: B:235:0x03d4  */
    /* JADX WARN: Code duplicated, block: B:236:0x03d6  */
    /* JADX WARN: Code duplicated, block: B:239:0x03df  */
    /* JADX WARN: Code duplicated, block: B:241:0x03e3 A[DONT_INVERT] */
    /* JADX WARN: Code duplicated, block: B:242:0x03e5  */
    /* JADX WARN: Code duplicated, block: B:243:0x03e8  */
    /* JADX WARN: Code duplicated, block: B:244:0x03eb  */
    /* JADX WARN: Code duplicated, block: B:245:0x03ee  */
    /* JADX WARN: Code duplicated, block: B:246:0x03f3  */
    /* JADX WARN: Code duplicated, block: B:247:0x03f8  */
    /* JADX WARN: Code duplicated, block: B:248:0x03fd  */
    /* JADX WARN: Code duplicated, block: B:249:0x0402  */
    /* JADX WARN: Code duplicated, block: B:250:0x0407  */
    /* JADX WARN: Code duplicated, block: B:253:0x040d  */
    /* JADX WARN: Code duplicated, block: B:259:0x0425  */
    /* JADX WARN: Code duplicated, block: B:275:0x0468  */
    /* JADX WARN: Code duplicated, block: B:277:0x0472  */
    /* JADX WARN: Code duplicated, block: B:281:0x048a A[ADDED_TO_REGION] */
    /* JADX WARN: Code duplicated, block: B:289:0x04a0  */
    /* JADX WARN: Code duplicated, block: B:298:0x0508  */
    /* JADX WARN: Code duplicated, block: B:301:0x0510  */
    /* JADX WARN: Code duplicated, block: B:302:0x0516  */
    /* JADX WARN: Code duplicated, block: B:305:0x051e  */
    /* JADX WARN: Code duplicated, block: B:308:0x052a A[ADDED_TO_REGION] */
    /* JADX WARN: Code duplicated, block: B:311:0x0533  */
    /* JADX WARN: Code duplicated, block: B:312:0x053c  */
    /* JADX WARN: Code duplicated, block: B:315:0x0542  */
    /* JADX WARN: Code duplicated, block: B:317:0x0548  */
    /* JADX WARN: Code duplicated, block: B:320:0x055d  */
    /* JADX WARN: Code duplicated, block: B:322:0x0561  */
    /* JADX WARN: Code duplicated, block: B:328:0x0580  */
    /* JADX WARN: Code duplicated, block: B:334:0x0594  */
    /* JADX WARN: Code duplicated, block: B:335:0x059d  */
    /* JADX WARN: Code duplicated, block: B:337:0x05a3  */
    /* JADX WARN: Code duplicated, block: B:339:0x05a9 A[ADDED_TO_REGION] */
    /* JADX WARN: Code duplicated, block: B:351:0x05ea  */
    /* JADX WARN: Code duplicated, block: B:364:0x01b0 A[EXC_TOP_SPLITTER, SYNTHETIC] */
    /* JADX WARN: Code duplicated, block: B:385:0x019d A[EXC_TOP_SPLITTER, SYNTHETIC] */
    /* JADX WARN: Code duplicated, block: B:391:? A[SYNTHETIC] */
    /* JADX WARN: Code duplicated, block: B:392:? A[SYNTHETIC] */
    /* JADX WARN: Code duplicated, block: B:393:? A[SYNTHETIC] */
    /* JADX WARN: Code duplicated, block: B:394:? A[SYNTHETIC] */
    /* JADX WARN: Code duplicated, block: B:395:? A[SYNTHETIC] */
    /* JADX WARN: Code duplicated, block: B:396:? A[SYNTHETIC] */
    /* JADX WARN: Code duplicated, block: B:397:? A[SYNTHETIC] */
    /* JADX WARN: Instruction removed from duplicated block: B:156:0x0230, please report this as an issue */
    /* JADX WARN: Instruction removed from duplicated block: B:159:0x024d, please report this as an issue */
    /*  JADX ERROR: NullPointerException in pass: PrepareForCodeGen
        java.lang.NullPointerException
        */
    public static int prepareSendingDocumentInternal(org.telegram.messenger.AccountInstance r35, java.lang.String r36, java.lang.String r37, android.net.Uri r38, java.lang.String r39, final long r40, final org.telegram.messenger.MessageObject r42, final org.telegram.messenger.MessageObject r43, final org.telegram.tgnet.tl.TL_stories.StoryItem r44, final org.telegram.ui.ChatActivity.ReplyQuote r45, final java.util.ArrayList<org.telegram.tgnet.TLRPC.MessageEntity> r46, final org.telegram.messenger.MessageObject r47, long[] r48, boolean r49, java.lang.CharSequence r50, final boolean r51, int r52, final int r53, java.lang.Integer[] r54, boolean r55, final java.lang.String r56, final int r57, final long r58, final boolean r60, final long r61, final long r63, final org.telegram.messenger.MessageSuggestionParams r65) throws java.lang.Throwable {
        /*
            Method dump skipped, instruction units count: 1606
            To view this dump add '--comments-level debug' option
        */
        throw new UnsupportedOperationException("Method not decompiled: org.telegram.messenger.SendMessagesHelper.prepareSendingDocumentInternal(org.telegram.messenger.AccountInstance, java.lang.String, java.lang.String, android.net.Uri, java.lang.String, long, org.telegram.messenger.MessageObject, org.telegram.messenger.MessageObject, org.telegram.tgnet.tl.TL_stories$StoryItem, org.telegram.ui.ChatActivity$ReplyQuote, java.util.ArrayList, org.telegram.messenger.MessageObject, long[], boolean, java.lang.CharSequence, boolean, int, int, java.lang.Integer[], boolean, java.lang.String, int, long, boolean, long, long, org.telegram.messenger.MessageSuggestionParams):int");
    }

    /* JADX INFO: renamed from: $r8$lambda$8xiG_jLvnTnVCpfRIoN3y4kkO-E, reason: not valid java name */
    public static /* synthetic */ void m3706$r8$lambda$8xiG_jLvnTnVCpfRIoN3y4kkOE(MessageObject messageObject, AccountInstance accountInstance, TLRPC.TL_document tL_document, String str, HashMap map, String str2, long j, MessageObject messageObject2, MessageObject messageObject3, String str3, ArrayList arrayList, boolean z, int i, int i2, TL_stories.StoryItem storyItem, ChatActivity.ReplyQuote replyQuote, String str4, int i3, long j2, boolean z2, long j3, long j4, MessageSuggestionParams messageSuggestionParams) {
        if (messageObject != null) {
            accountInstance.getSendMessagesHelper().editMessage(messageObject, null, null, tL_document, str, null, map, false, false, str2);
            return;
        }
        SendMessageParams sendMessageParamsOf = SendMessageParams.of(tL_document, null, str, j, messageObject2, messageObject3, str3, arrayList, null, map, z, i, i2, 0, str2, null, false);
        sendMessageParamsOf.replyToStoryItem = storyItem;
        sendMessageParamsOf.replyQuote = replyQuote;
        sendMessageParamsOf.quick_reply_shortcut = str4;
        sendMessageParamsOf.quick_reply_shortcut_id = i3;
        sendMessageParamsOf.effect_id = j2;
        sendMessageParamsOf.invert_media = z2;
        sendMessageParamsOf.payStars = j3;
        sendMessageParamsOf.monoForumPeer = j4;
        sendMessageParamsOf.suggestionParams = messageSuggestionParams;
        accountInstance.getSendMessagesHelper().sendMessage(sendMessageParamsOf);
    }

    private static boolean checkFileSize(AccountInstance accountInstance, Uri uri) {
        long j = 0;
        try {
            AssetFileDescriptor assetFileDescriptorOpenAssetFileDescriptor = ApplicationLoader.applicationContext.getContentResolver().openAssetFileDescriptor(uri, "r", null);
            if (assetFileDescriptorOpenAssetFileDescriptor != null) {
                assetFileDescriptorOpenAssetFileDescriptor.getLength();
            }
            Cursor cursorQuery = ApplicationLoader.applicationContext.getContentResolver().query(uri, new String[]{"_size"}, null, null, null);
            int columnIndex = cursorQuery.getColumnIndex("_size");
            cursorQuery.moveToFirst();
            j = cursorQuery.getLong(columnIndex);
            cursorQuery.close();
        } catch (Exception e) {
            FileLog.e(e);
        }
        return !FileLoader.checkUploadFileSize(accountInstance.getCurrentAccount(), j);
    }

    public static void prepareSendingDocument(AccountInstance accountInstance, String str, String str2, Uri uri, String str3, String str4, long j, MessageObject messageObject, MessageObject messageObject2, TL_stories.StoryItem storyItem, ChatActivity.ReplyQuote replyQuote, MessageObject messageObject3, boolean z, int i, InputContentInfoCompat inputContentInfoCompat, String str5, int i2, boolean z2) {
        ArrayList arrayList;
        if ((str == null || str2 == null) && uri == null) {
            return;
        }
        ArrayList arrayList2 = new ArrayList();
        ArrayList arrayList3 = new ArrayList();
        if (uri != null) {
            arrayList = new ArrayList();
            arrayList.add(uri);
        } else {
            arrayList = null;
        }
        if (str != null) {
            arrayList2.add(str);
            arrayList3.add(str2);
        }
        prepareSendingDocuments(accountInstance, arrayList2, arrayList3, arrayList, str3, str4, j, messageObject, messageObject2, storyItem, replyQuote, messageObject3, z, i, inputContentInfoCompat, str5, i2, 0L, z2, 0L);
    }

    public static void prepareSendingAudioDocuments(final AccountInstance accountInstance, final ArrayList<MessageObject> arrayList, final CharSequence charSequence, final long j, final MessageObject messageObject, final MessageObject messageObject2, final TL_stories.StoryItem storyItem, final boolean z, final int i, final int i2, final MessageObject messageObject3, final String str, final int i3, final long j2, final boolean z2, final long j3) {
        new Thread(new Runnable() { // from class: org.telegram.messenger.SendMessagesHelper$$ExternalSyntheticLambda76
            @Override // java.lang.Runnable
            public final void run() {
                SendMessagesHelper.$r8$lambda$c5s7B6KcStwQCN0LVEXqlqVccSo(arrayList, j, accountInstance, charSequence, messageObject3, messageObject, messageObject2, z, i, i2, storyItem, str, i3, j2, z2, j3);
            }
        }).start();
    }

    /* JADX WARN: Code duplicated, block: B:23:0x0076  */
    public static /* synthetic */ void $r8$lambda$c5s7B6KcStwQCN0LVEXqlqVccSo(ArrayList arrayList, final long j, final AccountInstance accountInstance, CharSequence charSequence, final MessageObject messageObject, final MessageObject messageObject2, final MessageObject messageObject3, final boolean z, final int i, final int i2, final TL_stories.StoryItem storyItem, final String str, final int i3, final long j2, final boolean z2, final long j3) {
        String str2;
        TLRPC.TL_document tL_document;
        int size = arrayList.size();
        long jNextLong = 0;
        int i4 = 0;
        int i5 = 0;
        while (i4 < size) {
            final MessageObject messageObject4 = (MessageObject) arrayList.get(i4);
            String str3 = messageObject4.messageOwner.attachPath;
            File file = new File(str3);
            boolean zIsEncryptedDialog = DialogObject.isEncryptedDialog(j);
            if (!zIsEncryptedDialog && size > 1 && i5 % 10 == 0) {
                jNextLong = Utilities.random.nextLong();
                i5 = 0;
            }
            if (str3 != null) {
                str3 = str3 + MediaStreamTrack.AUDIO_TRACK_KIND + file.length();
            }
            if (zIsEncryptedDialog) {
                str2 = null;
                tL_document = null;
            } else {
                Object[] sentFile = accountInstance.getMessagesStorage().getSentFile(str3, !zIsEncryptedDialog ? 1 : 4);
                if (sentFile != null) {
                    Object obj = sentFile[0];
                    if (obj instanceof TLRPC.TL_document) {
                        tL_document = (TLRPC.TL_document) obj;
                        str2 = (String) sentFile[1];
                        ensureMediaThumbExists(accountInstance, zIsEncryptedDialog, tL_document, str3, null, 0L);
                    } else {
                        str2 = null;
                        tL_document = null;
                    }
                } else {
                    str2 = null;
                    tL_document = null;
                }
            }
            if (tL_document == null) {
                tL_document = (TLRPC.TL_document) messageObject4.messageOwner.media.document;
            }
            final TLRPC.TL_document tL_document2 = tL_document;
            if (zIsEncryptedDialog && accountInstance.getMessagesController().getEncryptedChat(Integer.valueOf(DialogObject.getEncryptedChatId(j))) == null) {
                return;
            }
            CharSequence[] charSequenceArr = {charSequence};
            final ArrayList<TLRPC.MessageEntity> entities = i4 == 0 ? accountInstance.getMediaDataController().getEntities(charSequenceArr, true) : null;
            final String string = i4 == 0 ? charSequenceArr[0].toString() : null;
            final HashMap map = new HashMap();
            if (str3 != null) {
                map.put("originalPath", str3);
            }
            if (str2 != null) {
                map.put("parentObject", str2);
            }
            i5++;
            map.put("groupId", _UrlKt.FRAGMENT_ENCODE_SET + jNextLong);
            if (i5 == 10 || i4 == size - 1) {
                map.put("final", "1");
            }
            final String str4 = str2;
            AndroidUtilities.runOnUIThread(new Runnable() { // from class: org.telegram.messenger.SendMessagesHelper$$ExternalSyntheticLambda25
                @Override // java.lang.Runnable
                public final void run() {
                    SendMessagesHelper.$r8$lambda$ciH4a4NfdkFD0xdQbtV3CHwOLLk(messageObject, accountInstance, tL_document2, messageObject4, map, str4, j, messageObject2, messageObject3, string, entities, z, i, i2, storyItem, str, i3, j2, z2, j3);
                }
            });
            i4++;
        }
    }

    public static /* synthetic */ void $r8$lambda$ciH4a4NfdkFD0xdQbtV3CHwOLLk(MessageObject messageObject, AccountInstance accountInstance, TLRPC.TL_document tL_document, MessageObject messageObject2, HashMap map, String str, long j, MessageObject messageObject3, MessageObject messageObject4, String str2, ArrayList arrayList, boolean z, int i, int i2, TL_stories.StoryItem storyItem, String str3, int i3, long j2, boolean z2, long j3) {
        if (messageObject != null) {
            accountInstance.getSendMessagesHelper().editMessage(messageObject, null, null, tL_document, messageObject2.messageOwner.attachPath, null, map, false, false, str);
            return;
        }
        SendMessageParams sendMessageParamsOf = SendMessageParams.of(tL_document, null, messageObject2.messageOwner.attachPath, j, messageObject3, messageObject4, str2, arrayList, null, map, z, i, i2, 0, str, null, false, false);
        sendMessageParamsOf.replyToStoryItem = storyItem;
        sendMessageParamsOf.quick_reply_shortcut = str3;
        sendMessageParamsOf.quick_reply_shortcut_id = i3;
        sendMessageParamsOf.effect_id = j2;
        sendMessageParamsOf.invert_media = z2;
        sendMessageParamsOf.payStars = j3;
        accountInstance.getSendMessagesHelper().sendMessage(sendMessageParamsOf);
    }

    private static void finishGroup(final AccountInstance accountInstance, final long j, final int i) {
        AndroidUtilities.runOnUIThread(new Runnable() { // from class: org.telegram.messenger.SendMessagesHelper$$ExternalSyntheticLambda37
            @Override // java.lang.Runnable
            public final void run() {
                SendMessagesHelper.$r8$lambda$RQ6ACJW90ZIBgHIKz_NtgbEe3qE(accountInstance, j, i);
            }
        });
    }

    public static /* synthetic */ void $r8$lambda$RQ6ACJW90ZIBgHIKz_NtgbEe3qE(AccountInstance accountInstance, long j, int i) {
        SendMessagesHelper sendMessagesHelper = accountInstance.getSendMessagesHelper();
        ArrayList<DelayedMessage> arrayList = sendMessagesHelper.delayedMessages.get("group_" + j);
        if (arrayList == null || arrayList.isEmpty()) {
            return;
        }
        DelayedMessage delayedMessage = arrayList.get(0);
        ArrayList<MessageObject> arrayList2 = delayedMessage.messageObjects;
        MessageObject messageObject = arrayList2.get(arrayList2.size() - 1);
        delayedMessage.finalGroupMessage = messageObject.getId();
        messageObject.messageOwner.params.put("final", "1");
        TLRPC.TL_messages_messages tL_messages_messages = new TLRPC.TL_messages_messages();
        tL_messages_messages.messages.add(messageObject.messageOwner);
        if (!delayedMessage.paidMedia) {
            accountInstance.getMessagesStorage().putMessages((TLRPC.messages_Messages) tL_messages_messages, delayedMessage.peer, -2, 0, false, i != 0 ? 1 : 0, 0L);
        }
        sendMessagesHelper.sendReadyToSendGroup(delayedMessage, true, true);
    }

    public static void prepareSendingDocuments(AccountInstance accountInstance, ArrayList<String> arrayList, ArrayList<String> arrayList2, ArrayList<Uri> arrayList3, String str, String str2, long j, MessageObject messageObject, MessageObject messageObject2, TL_stories.StoryItem storyItem, ChatActivity.ReplyQuote replyQuote, MessageObject messageObject3, boolean z, int i, InputContentInfoCompat inputContentInfoCompat, String str3, int i2, long j2, boolean z2, long j3) {
        prepareSendingDocuments(accountInstance, arrayList, arrayList2, arrayList3, str, null, str2, j, messageObject, messageObject2, storyItem, replyQuote, messageObject3, z, i, 0, inputContentInfoCompat, str3, i2, j2, z2, j3, 0L, null);
    }

    public static void prepareSendingDocuments(final AccountInstance accountInstance, final ArrayList<String> arrayList, final ArrayList<String> arrayList2, final ArrayList<Uri> arrayList3, final String str, final ArrayList<TLRPC.MessageEntity> arrayList4, final String str2, final long j, final MessageObject messageObject, final MessageObject messageObject2, final TL_stories.StoryItem storyItem, final ChatActivity.ReplyQuote replyQuote, final MessageObject messageObject3, final boolean z, final int i, final int i2, final InputContentInfoCompat inputContentInfoCompat, final String str3, final int i3, final long j2, final boolean z2, final long j3, final long j4, final MessageSuggestionParams messageSuggestionParams) {
        if (arrayList == null && arrayList2 == null && arrayList3 == null) {
            return;
        }
        if (arrayList == null || arrayList2 == null || arrayList.size() == arrayList2.size()) {
            Utilities.globalQueue.postRunnable(new Runnable() { // from class: org.telegram.messenger.SendMessagesHelper$$ExternalSyntheticLambda38
                @Override // java.lang.Runnable
                public final void run() throws Throwable {
                    SendMessagesHelper.$r8$lambda$eVNCLZirG2QhafzvlRKlTCFuFEw(j, arrayList, str, accountInstance, i, arrayList2, str2, messageObject, messageObject2, storyItem, replyQuote, arrayList4, messageObject3, z, i2, inputContentInfoCompat, str3, i3, j2, z2, j3, j4, messageSuggestionParams, arrayList3);
                }
            });
        }
    }

    /* JADX WARN: Multi-variable type inference failed */
    public static /* synthetic */ void $r8$lambda$eVNCLZirG2QhafzvlRKlTCFuFEw(long j, ArrayList arrayList, String str, AccountInstance accountInstance, int i, ArrayList arrayList2, String str2, MessageObject messageObject, MessageObject messageObject2, TL_stories.StoryItem storyItem, ChatActivity.ReplyQuote replyQuote, ArrayList arrayList3, MessageObject messageObject3, boolean z, int i2, InputContentInfoCompat inputContentInfoCompat, String str3, int i3, long j2, boolean z2, long j3, long j4, MessageSuggestionParams messageSuggestionParams, ArrayList arrayList4) throws Throwable {
        int i4;
        boolean z3;
        ArrayList arrayList5 = arrayList;
        int i5 = i;
        int i6 = 1;
        long[] jArr = new long[1];
        Integer[] numArr = new Integer[1];
        boolean zIsEncryptedDialog = DialogObject.isEncryptedDialog(j);
        int i7 = 10;
        if (arrayList5 != null) {
            int size = arrayList5.size();
            z3 = true;
            int i8 = 0;
            i4 = 0;
            int i9 = 0;
            while (i8 < size) {
                String str4 = i8 == 0 ? str : null;
                if (!zIsEncryptedDialog && size > i6 && i9 % 10 == 0) {
                    long j5 = jArr[0];
                    if (j5 != 0) {
                        finishGroup(accountInstance, j5, i5);
                    }
                    jArr[0] = Utilities.random.nextLong();
                    i9 = 0;
                }
                int i10 = i9 + 1;
                long j6 = jArr[0];
                int i11 = i6;
                Integer[] numArr2 = numArr;
                int i12 = i5;
                int i13 = size;
                int i14 = i8;
                int iPrepareSendingDocumentInternal = prepareSendingDocumentInternal(accountInstance, (String) arrayList5.get(i8), (String) arrayList2.get(i8), null, str2, j, messageObject, messageObject2, storyItem, replyQuote, i8 == 0 ? arrayList3 : null, messageObject3, jArr, (i10 == i7 || i8 == size + (-1)) ? i11 : 0, str4, z, i12, i2, numArr2, inputContentInfoCompat == null ? i11 : 0, str3, i3, z3 ? j2 : 0L, z2, j3, j4, messageSuggestionParams);
                long j7 = jArr[0];
                i9 = (j6 != j7 || j7 == -1) ? 1 : i10;
                i8 = i14 + 1;
                arrayList5 = arrayList;
                i4 = iPrepareSendingDocumentInternal;
                i5 = i12;
                numArr = numArr2;
                z3 = false;
                size = i13;
                i6 = 1;
                i7 = 10;
            }
        } else {
            i4 = 0;
            z3 = true;
        }
        int i15 = i5;
        Integer[] numArr3 = numArr;
        if (arrayList4 != null) {
            jArr[0] = 0;
            int size2 = arrayList4.size();
            int i16 = 0;
            int i17 = 0;
            while (i16 < arrayList4.size()) {
                String str5 = (i16 == 0 && (arrayList == null || arrayList.size() == 0)) ? str : null;
                ArrayList arrayList6 = (i16 == 0 && (arrayList == null || arrayList.size() == 0)) ? arrayList3 : null;
                if (!zIsEncryptedDialog && size2 > 1 && i17 % 10 == 0) {
                    long j8 = jArr[0];
                    if (j8 != 0) {
                        finishGroup(accountInstance, j8, i15);
                    }
                    jArr[0] = Utilities.random.nextLong();
                    i17 = 0;
                }
                int i18 = i17 + 1;
                long j9 = jArr[0];
                int i19 = size2;
                int i20 = i16;
                int iPrepareSendingDocumentInternal2 = prepareSendingDocumentInternal(accountInstance, null, null, (Uri) arrayList4.get(i16), str2, j, messageObject, messageObject2, storyItem, replyQuote, arrayList6, messageObject3, jArr, i18 == 10 || i16 == size2 + (-1), str5, z, i, i2, numArr3, inputContentInfoCompat == null, str3, i3, z3 ? j2 : 0L, z2, j3, j4, messageSuggestionParams);
                long j10 = jArr[0];
                i17 = (j9 != j10 || j10 == -1) ? 1 : i18;
                i16 = i20 + 1;
                i15 = i;
                i4 = iPrepareSendingDocumentInternal2;
                z3 = false;
                size2 = i19;
            }
        }
        if (inputContentInfoCompat != null) {
            inputContentInfoCompat.releasePermission();
        }
        handleError(i4, accountInstance);
    }

    private static void handleError(final int i, final AccountInstance accountInstance) {
        if (i != 0) {
            AndroidUtilities.runOnUIThread(new Runnable() { // from class: org.telegram.messenger.SendMessagesHelper$$ExternalSyntheticLambda30
                @Override // java.lang.Runnable
                public final void run() {
                    SendMessagesHelper.$r8$lambda$orSjvOn3jVUQYbWSosrEdOy5IR0(i, accountInstance);
                }
            });
        }
    }

    public static /* synthetic */ void $r8$lambda$orSjvOn3jVUQYbWSosrEdOy5IR0(int i, AccountInstance accountInstance) {
        try {
            if (i == 1) {
                NotificationCenter.getGlobalInstance().lambda$postNotificationNameOnUIThread$1(NotificationCenter.showBulletin, 1, LocaleController.getString(R.string.UnsupportedAttachment));
            } else if (i == 2) {
                NotificationCenter.getInstance(accountInstance.getCurrentAccount()).lambda$postNotificationNameOnUIThread$1(NotificationCenter.currentUserShowLimitReachedDialog, 6);
            }
        } catch (Exception e) {
            FileLog.e(e);
        }
    }

    public static void prepareSendingPhoto(AccountInstance accountInstance, String str, Uri uri, long j, MessageObject messageObject, MessageObject messageObject2, ChatActivity.ReplyQuote replyQuote, CharSequence charSequence, ArrayList<TLRPC.MessageEntity> arrayList, ArrayList<TLRPC.InputDocument> arrayList2, InputContentInfoCompat inputContentInfoCompat, int i, MessageObject messageObject3, boolean z, int i2, int i3, String str2, int i4) {
        prepareSendingPhoto(accountInstance, str, null, uri, j, messageObject, messageObject2, null, null, arrayList, arrayList2, inputContentInfoCompat, i, messageObject3, null, z, i2, 0, i3, false, charSequence, str2, i4, 0L, 0L, 0L, null);
    }

    public static void prepareSendingPhoto(AccountInstance accountInstance, String str, String str2, Uri uri, long j, MessageObject messageObject, MessageObject messageObject2, TL_stories.StoryItem storyItem, ChatActivity.ReplyQuote replyQuote, ArrayList<TLRPC.MessageEntity> arrayList, ArrayList<TLRPC.InputDocument> arrayList2, InputContentInfoCompat inputContentInfoCompat, int i, MessageObject messageObject3, VideoEditedInfo videoEditedInfo, boolean z, int i2, int i3, boolean z2, CharSequence charSequence, String str3, int i4, long j2, long j3) {
        prepareSendingPhoto(accountInstance, str, str2, uri, j, messageObject, messageObject2, storyItem, replyQuote, arrayList, arrayList2, inputContentInfoCompat, i, messageObject3, videoEditedInfo, z, i2, 0, i3, z2, charSequence, str3, i4, j2, j3, 0L, null);
    }

    public static void prepareSendingPhoto(AccountInstance accountInstance, String str, String str2, Uri uri, long j, MessageObject messageObject, MessageObject messageObject2, TL_stories.StoryItem storyItem, ChatActivity.ReplyQuote replyQuote, ArrayList<TLRPC.MessageEntity> arrayList, ArrayList<TLRPC.InputDocument> arrayList2, InputContentInfoCompat inputContentInfoCompat, int i, MessageObject messageObject3, VideoEditedInfo videoEditedInfo, boolean z, int i2, int i3, int i4, boolean z2, CharSequence charSequence, String str3, int i5, long j2, long j3, long j4, MessageSuggestionParams messageSuggestionParams) {
        SendingMediaInfo sendingMediaInfo = new SendingMediaInfo();
        sendingMediaInfo.path = str;
        sendingMediaInfo.thumbPath = str2;
        sendingMediaInfo.uri = uri;
        if (charSequence != null) {
            sendingMediaInfo.caption = charSequence.toString();
        }
        sendingMediaInfo.entities = arrayList;
        sendingMediaInfo.ttl = i;
        if (arrayList2 != null) {
            sendingMediaInfo.masks = new ArrayList<>(arrayList2);
        }
        sendingMediaInfo.videoEditedInfo = videoEditedInfo;
        ArrayList arrayList3 = new ArrayList();
        arrayList3.add(sendingMediaInfo);
        prepareSendingMedia(accountInstance, arrayList3, j, messageObject, messageObject2, null, replyQuote, z2, false, messageObject3, z, i2, 0, i4, false, inputContentInfoCompat, str3, i5, j2, false, j3, j4, messageSuggestionParams);
    }

    public static void prepareSendingBotContextResult(BaseFragment baseFragment, AccountInstance accountInstance, TLRPC.BotInlineResult botInlineResult, HashMap<String, String> map, long j, MessageObject messageObject, MessageObject messageObject2, TL_stories.StoryItem storyItem, ChatActivity.ReplyQuote replyQuote, boolean z, int i, int i2, String str, int i3, long j2) {
        prepareSendingBotContextResult(baseFragment, accountInstance, botInlineResult, map, j, messageObject, messageObject2, storyItem, replyQuote, z, i, i2, str, i3, j2, 0L);
    }

    public static void prepareSendingBotContextResult(final BaseFragment baseFragment, final AccountInstance accountInstance, final TLRPC.BotInlineResult botInlineResult, final HashMap<String, String> map, final long j, final MessageObject messageObject, final MessageObject messageObject2, final TL_stories.StoryItem storyItem, final ChatActivity.ReplyQuote replyQuote, final boolean z, final int i, final int i2, final String str, final int i3, final long j2, final long j3) {
        SendMessageParams sendMessageParamsOf;
        TLRPC.TL_webPagePending tL_webPagePending;
        if (botInlineResult == null) {
            return;
        }
        TLRPC.BotInlineMessage botInlineMessage = botInlineResult.send_message;
        if (botInlineMessage instanceof TLRPC.TL_botInlineMessageMediaAuto) {
            new Thread(new Runnable() { // from class: org.telegram.messenger.SendMessagesHelper$$ExternalSyntheticLambda102
                @Override // java.lang.Runnable
                public final void run() {
                    SendMessagesHelper.m3724$r8$lambda$mauLnil48fYTmTyAutVIsp8r6w(j, botInlineResult, accountInstance, map, baseFragment, messageObject, messageObject2, z, i, i2, str, i3, storyItem, replyQuote, j2, j3);
                }
            }).run();
            return;
        }
        if (botInlineMessage instanceof TLRPC.TL_botInlineMessageText) {
            if (!DialogObject.isEncryptedDialog(j)) {
                tL_webPagePending = null;
                break;
            }
            int i4 = 0;
            while (true) {
                if (i4 >= botInlineResult.send_message.entities.size()) {
                    tL_webPagePending = null;
                    break;
                }
                TLRPC.MessageEntity messageEntity = (TLRPC.MessageEntity) botInlineResult.send_message.entities.get(i4);
                if (messageEntity instanceof TLRPC.TL_messageEntityUrl) {
                    tL_webPagePending = new TLRPC.TL_webPagePending();
                    String str2 = botInlineResult.send_message.message;
                    int i5 = messageEntity.offset;
                    tL_webPagePending.url = str2.substring(i5, messageEntity.length + i5);
                    break;
                }
                i4++;
            }
            TLRPC.TL_webPagePending tL_webPagePending2 = tL_webPagePending;
            TLRPC.BotInlineMessage botInlineMessage2 = botInlineResult.send_message;
            SendMessageParams sendMessageParamsOf2 = SendMessageParams.of(botInlineMessage2.message, j, messageObject, messageObject2, tL_webPagePending2, !botInlineMessage2.no_webpage, botInlineMessage2.entities, botInlineMessage2.reply_markup, map, z, i, i2, null, false);
            sendMessageParamsOf2.quick_reply_shortcut = str;
            sendMessageParamsOf2.quick_reply_shortcut_id = i3;
            sendMessageParamsOf2.replyQuote = replyQuote;
            sendMessageParamsOf2.payStars = j2;
            sendMessageParamsOf2.monoForumPeer = j3;
            accountInstance.getSendMessagesHelper().sendMessage(sendMessageParamsOf2);
            return;
        }
        if (botInlineMessage instanceof TLRPC.TL_botInlineMessageMediaVenue) {
            TLRPC.TL_messageMediaVenue tL_messageMediaVenue = new TLRPC.TL_messageMediaVenue();
            TLRPC.BotInlineMessage botInlineMessage3 = botInlineResult.send_message;
            tL_messageMediaVenue.geo = botInlineMessage3.geo;
            tL_messageMediaVenue.address = botInlineMessage3.address;
            tL_messageMediaVenue.title = botInlineMessage3.title;
            tL_messageMediaVenue.provider = botInlineMessage3.provider;
            tL_messageMediaVenue.venue_id = botInlineMessage3.venue_id;
            String str3 = botInlineMessage3.venue_type;
            tL_messageMediaVenue.venue_id = str3;
            tL_messageMediaVenue.venue_type = str3;
            if (str3 == null) {
                tL_messageMediaVenue.venue_type = _UrlKt.FRAGMENT_ENCODE_SET;
            }
            SendMessageParams sendMessageParamsOf3 = SendMessageParams.of(tL_messageMediaVenue, j, messageObject, messageObject2, botInlineMessage3.reply_markup, map, z, i, i2);
            sendMessageParamsOf3.quick_reply_shortcut = str;
            sendMessageParamsOf3.quick_reply_shortcut_id = i3;
            sendMessageParamsOf3.replyQuote = replyQuote;
            sendMessageParamsOf3.payStars = j2;
            sendMessageParamsOf3.monoForumPeer = j3;
            accountInstance.getSendMessagesHelper().sendMessage(sendMessageParamsOf3);
            return;
        }
        if (botInlineMessage instanceof TLRPC.TL_botInlineMessageMediaGeo) {
            if (botInlineMessage.period != 0 || botInlineMessage.proximity_notification_radius != 0) {
                TLRPC.TL_messageMediaGeoLive tL_messageMediaGeoLive = new TLRPC.TL_messageMediaGeoLive();
                TLRPC.BotInlineMessage botInlineMessage4 = botInlineResult.send_message;
                int i6 = botInlineMessage4.period;
                if (i6 == 0) {
                    i6 = 900;
                }
                tL_messageMediaGeoLive.period = i6;
                tL_messageMediaGeoLive.geo = botInlineMessage4.geo;
                tL_messageMediaGeoLive.heading = botInlineMessage4.heading;
                tL_messageMediaGeoLive.proximity_notification_radius = botInlineMessage4.proximity_notification_radius;
                sendMessageParamsOf = SendMessageParams.of(tL_messageMediaGeoLive, j, messageObject, messageObject2, botInlineMessage4.reply_markup, map, z, i, i2);
            } else {
                TLRPC.TL_messageMediaGeo tL_messageMediaGeo = new TLRPC.TL_messageMediaGeo();
                TLRPC.BotInlineMessage botInlineMessage5 = botInlineResult.send_message;
                tL_messageMediaGeo.geo = botInlineMessage5.geo;
                tL_messageMediaGeo.heading = botInlineMessage5.heading;
                sendMessageParamsOf = SendMessageParams.of(tL_messageMediaGeo, j, messageObject, messageObject2, botInlineMessage5.reply_markup, map, z, i, i2);
            }
            sendMessageParamsOf.quick_reply_shortcut = str;
            sendMessageParamsOf.quick_reply_shortcut_id = i3;
            sendMessageParamsOf.replyQuote = replyQuote;
            sendMessageParamsOf.payStars = j2;
            sendMessageParamsOf.monoForumPeer = j3;
            accountInstance.getSendMessagesHelper().sendMessage(sendMessageParamsOf);
            return;
        }
        if (botInlineMessage instanceof TLRPC.TL_botInlineMessageMediaContact) {
            TLRPC.TL_user tL_user = new TLRPC.TL_user();
            TLRPC.BotInlineMessage botInlineMessage6 = botInlineResult.send_message;
            tL_user.phone = botInlineMessage6.phone_number;
            tL_user.first_name = botInlineMessage6.first_name;
            tL_user.last_name = botInlineMessage6.last_name;
            TLRPC.RestrictionReason restrictionReason = new TLRPC.RestrictionReason();
            restrictionReason.text = botInlineResult.send_message.vcard;
            restrictionReason.platform = _UrlKt.FRAGMENT_ENCODE_SET;
            restrictionReason.reason = _UrlKt.FRAGMENT_ENCODE_SET;
            tL_user.restriction_reason.add(restrictionReason);
            SendMessageParams sendMessageParamsOf4 = SendMessageParams.of(tL_user, j, messageObject, messageObject2, botInlineResult.send_message.reply_markup, map, z, i, i2);
            sendMessageParamsOf4.quick_reply_shortcut = str;
            sendMessageParamsOf4.quick_reply_shortcut_id = i3;
            sendMessageParamsOf4.replyQuote = replyQuote;
            sendMessageParamsOf4.payStars = j2;
            sendMessageParamsOf4.monoForumPeer = j3;
            accountInstance.getSendMessagesHelper().sendMessage(sendMessageParamsOf4);
            return;
        }
        if (botInlineMessage instanceof TLRPC.TL_botInlineMessageMediaInvoice) {
            if (DialogObject.isEncryptedDialog(j)) {
                return;
            }
            TLRPC.TL_botInlineMessageMediaInvoice tL_botInlineMessageMediaInvoice = (TLRPC.TL_botInlineMessageMediaInvoice) botInlineResult.send_message;
            TLRPC.TL_messageMediaInvoice tL_messageMediaInvoice = new TLRPC.TL_messageMediaInvoice();
            tL_messageMediaInvoice.shipping_address_requested = tL_botInlineMessageMediaInvoice.shipping_address_requested;
            tL_messageMediaInvoice.test = tL_botInlineMessageMediaInvoice.test;
            tL_messageMediaInvoice.title = tL_botInlineMessageMediaInvoice.title;
            tL_messageMediaInvoice.description = tL_botInlineMessageMediaInvoice.description;
            TLRPC.WebDocument webDocument = tL_botInlineMessageMediaInvoice.photo;
            if (webDocument != null) {
                tL_messageMediaInvoice.webPhoto = webDocument;
                tL_messageMediaInvoice.flags |= 1;
            }
            tL_messageMediaInvoice.currency = tL_botInlineMessageMediaInvoice.currency;
            tL_messageMediaInvoice.total_amount = tL_botInlineMessageMediaInvoice.total_amount;
            tL_messageMediaInvoice.start_param = _UrlKt.FRAGMENT_ENCODE_SET;
            SendMessageParams sendMessageParamsOf5 = SendMessageParams.of(tL_messageMediaInvoice, j, messageObject, messageObject2, botInlineResult.send_message.reply_markup, map, z, i, i2);
            sendMessageParamsOf5.quick_reply_shortcut = str;
            sendMessageParamsOf5.quick_reply_shortcut_id = i3;
            sendMessageParamsOf5.replyQuote = replyQuote;
            sendMessageParamsOf5.payStars = j2;
            sendMessageParamsOf5.monoForumPeer = j3;
            accountInstance.getSendMessagesHelper().sendMessage(sendMessageParamsOf5);
            return;
        }
        if (botInlineMessage instanceof TLRPC.TL_botInlineMessageMediaWebPage) {
            TLRPC.TL_webPagePending tL_webPagePending3 = new TLRPC.TL_webPagePending();
            tL_webPagePending3.url = ((TLRPC.TL_botInlineMessageMediaWebPage) botInlineMessage).url;
            TLRPC.BotInlineMessage botInlineMessage7 = botInlineResult.send_message;
            SendMessageParams sendMessageParamsOf6 = SendMessageParams.of(botInlineMessage7.message, j, messageObject, messageObject2, tL_webPagePending3, !botInlineMessage7.no_webpage, botInlineMessage7.entities, botInlineMessage7.reply_markup, map, z, i, i2, null, false);
            sendMessageParamsOf6.quick_reply_shortcut = str;
            sendMessageParamsOf6.quick_reply_shortcut_id = i3;
            sendMessageParamsOf6.replyQuote = replyQuote;
            sendMessageParamsOf6.payStars = j2;
            sendMessageParamsOf6.monoForumPeer = j3;
            accountInstance.getSendMessagesHelper().sendMessage(sendMessageParamsOf6);
        }
    }

    /* JADX WARN: Can't fix incorrect switch cases order, some code will duplicate */
    /* JADX WARN: Code duplicated, block: B:200:0x04ce  */
    /* JADX WARN: Code duplicated, block: B:202:0x04d8  */
    /* JADX WARN: Code duplicated, block: B:206:0x050b  */
    /* JADX WARN: Code duplicated, block: B:208:0x0525  */
    /* JADX WARN: Code duplicated, block: B:209:0x0533  */
    /* JADX WARN: Code duplicated, block: B:211:0x054d  */
    /* JADX WARN: Code duplicated, block: B:214:0x0556  */
    /* JADX WARN: Code duplicated, block: B:215:0x0561  */
    /* JADX WARN: Code duplicated, block: B:240:0x0502 A[SYNTHETIC] */
    /* JADX WARN: Code duplicated, block: B:40:0x00e9  */
    /* JADX WARN: Code duplicated, block: B:81:0x01c7  */
    /* JADX WARN: Multi-variable type inference failed */
    /* JADX WARN: Type inference failed for: r14v10, types: [org.telegram.tgnet.TLRPC$TL_photo] */
    /* JADX WARN: Type inference failed for: r14v11 */
    /* JADX WARN: Type inference failed for: r14v6 */
    /* JADX WARN: Type inference failed for: r14v7 */
    /* JADX WARN: Type inference failed for: r14v8 */
    /* JADX WARN: Type inference failed for: r14v9 */
    /* JADX WARN: Type inference failed for: r4v10, types: [java.lang.String] */
    /* JADX INFO: renamed from: $r8$lambda$ma-uLnil48fYTmTyAutVIsp8r6w, reason: not valid java name */
    public static /* synthetic */ void m3724$r8$lambda$mauLnil48fYTmTyAutVIsp8r6w(long j, final TLRPC.BotInlineResult botInlineResult, AccountInstance accountInstance, final HashMap map, final BaseFragment baseFragment, final MessageObject messageObject, final MessageObject messageObject2, final boolean z, final int i, final int i2, final String str, final int i3, final TL_stories.StoryItem storyItem, final ChatActivity.ReplyQuote replyQuote, final long j2, final long j3) {
        String absolutePath;
        String str2;
        Bitmap bitmapLoadBitmap;
        Object obj;
        TLRPC.TL_document tL_document;
        TLRPC.TL_game tL_game;
        final ?? r14;
        final Bitmap[] bitmapArr;
        final String[] strArr;
        AccountInstance accountInstance2;
        TLRPC.TL_document tL_document2;
        final long j4;
        TLRPC.InputPeer inputPeer;
        File pathToAttach;
        boolean z2;
        int i4;
        TLRPC.WebDocument webDocument;
        boolean zIsEncryptedDialog = DialogObject.isEncryptedDialog(j);
        if (!"game".equals(botInlineResult.type)) {
            if (botInlineResult instanceof TLRPC.TL_botInlineMediaResult) {
                TLRPC.Document document = botInlineResult.document;
                if (document != null) {
                    if (document instanceof TLRPC.TL_document) {
                        tL_document = (TLRPC.TL_document) document;
                        tL_game = null;
                        obj = null;
                    }
                    r14 = obj;
                } else {
                    TLRPC.Photo photo = botInlineResult.photo;
                    if (photo != null && (photo instanceof TLRPC.TL_photo)) {
                        r14 = (TLRPC.TL_photo) photo;
                        tL_game = null;
                        tL_document = null;
                        obj = null;
                    }
                }
                tL_game = null;
                tL_document = null;
            } else {
                TLRPC.WebDocument webDocument2 = botInlineResult.content;
                if (webDocument2 != null) {
                    String httpUrlExtension = ImageLoader.getHttpUrlExtension(webDocument2.url, null);
                    File file = new File(FileLoader.getDirectory(4), Utilities.MD5(botInlineResult.content.url) + (TextUtils.isEmpty(httpUrlExtension) ? FileLoader.getExtensionByMimeType(botInlineResult.content.mime_type) : "." + httpUrlExtension));
                    if (file.exists()) {
                        absolutePath = file.getAbsolutePath();
                    } else {
                        absolutePath = botInlineResult.content.url;
                    }
                    String str3 = absolutePath;
                    String str4 = botInlineResult.type;
                    str4.getClass();
                    switch (str4) {
                        case "sticker":
                        case "gif":
                        case "file":
                        case "audio":
                        case "video":
                        case "voice":
                            TLRPC.TL_document tL_document3 = new TLRPC.TL_document();
                            tL_document3.id = 0L;
                            tL_document3.size = 0L;
                            tL_document3.dc_id = 0;
                            tL_document3.mime_type = botInlineResult.content.mime_type;
                            tL_document3.file_reference = new byte[0];
                            tL_document3.date = accountInstance.getConnectionsManager().getCurrentTime();
                            TLRPC.TL_documentAttributeFilename tL_documentAttributeFilename = new TLRPC.TL_documentAttributeFilename();
                            tL_document3.attributes.add(tL_documentAttributeFilename);
                            String str5 = botInlineResult.type;
                            str5.getClass();
                            switch (str5) {
                                case "sticker":
                                    str2 = str3;
                                    TLRPC.TL_documentAttributeSticker tL_documentAttributeSticker = new TLRPC.TL_documentAttributeSticker();
                                    tL_documentAttributeSticker.alt = _UrlKt.FRAGMENT_ENCODE_SET;
                                    tL_documentAttributeSticker.stickerset = new TLRPC.TL_inputStickerSetEmpty();
                                    tL_document3.attributes.add(tL_documentAttributeSticker);
                                    TLRPC.TL_documentAttributeImageSize tL_documentAttributeImageSize = new TLRPC.TL_documentAttributeImageSize();
                                    int[] inlineResultWidthAndHeight = MessageObject.getInlineResultWidthAndHeight(botInlineResult);
                                    tL_documentAttributeImageSize.w = inlineResultWidthAndHeight[0];
                                    tL_documentAttributeImageSize.h = inlineResultWidthAndHeight[1];
                                    tL_document3.attributes.add(tL_documentAttributeImageSize);
                                    tL_documentAttributeFilename.file_name = "sticker.webp";
                                    try {
                                        if (botInlineResult.thumb != null) {
                                            Bitmap bitmapLoadBitmap2 = ImageLoader.loadBitmap(new File(FileLoader.getDirectory(4), Utilities.MD5(botInlineResult.thumb.url) + "." + ImageLoader.getHttpUrlExtension(botInlineResult.thumb.url, "webp")).getAbsolutePath(), null, 90.0f, 90.0f, true);
                                            if (bitmapLoadBitmap2 != null) {
                                                TLRPC.PhotoSize photoSizeScaleAndSaveImage = ImageLoader.scaleAndSaveImage(bitmapLoadBitmap2, 90.0f, 90.0f, 55, false);
                                                if (photoSizeScaleAndSaveImage != null) {
                                                    tL_document3.thumbs.add(photoSizeScaleAndSaveImage);
                                                    tL_document3.flags |= 1;
                                                }
                                                bitmapLoadBitmap2.recycle();
                                            }
                                        }
                                        break;
                                    } catch (Throwable th) {
                                        FileLog.e(th);
                                        break;
                                    }
                                    break;
                                case "gif":
                                    tL_documentAttributeFilename.file_name = "animation.gif";
                                    str2 = str3;
                                    if (str2.endsWith("mp4")) {
                                        tL_document3.mime_type = "video/mp4";
                                        tL_document3.attributes.add(new TLRPC.TL_documentAttributeAnimated());
                                    } else {
                                        tL_document3.mime_type = "image/gif";
                                    }
                                    int i5 = zIsEncryptedDialog ? 90 : 320;
                                    try {
                                        if (str2.endsWith("mp4")) {
                                            bitmapLoadBitmap = createVideoThumbnail(str2, 1);
                                            if (bitmapLoadBitmap == null) {
                                                TLRPC.WebDocument webDocument3 = botInlineResult.thumb;
                                                if ((webDocument3 instanceof TLRPC.TL_webDocument) && "video/mp4".equals(webDocument3.mime_type)) {
                                                    String httpUrlExtension2 = ImageLoader.getHttpUrlExtension(botInlineResult.thumb.url, null);
                                                    bitmapLoadBitmap = createVideoThumbnail(new File(FileLoader.getDirectory(4), Utilities.MD5(botInlineResult.thumb.url) + (TextUtils.isEmpty(httpUrlExtension2) ? FileLoader.getExtensionByMimeType(botInlineResult.thumb.mime_type) : "." + httpUrlExtension2)).getAbsolutePath(), 1);
                                                }
                                            }
                                        } else {
                                            float f = i5;
                                            bitmapLoadBitmap = ImageLoader.loadBitmap(str2, null, f, f, true);
                                        }
                                        if (bitmapLoadBitmap != null) {
                                            float f2 = i5;
                                            TLRPC.PhotoSize photoSizeScaleAndSaveImage2 = ImageLoader.scaleAndSaveImage(bitmapLoadBitmap, f2, f2, i5 > 90 ? 80 : 55, false);
                                            if (photoSizeScaleAndSaveImage2 != null) {
                                                tL_document3.thumbs.add(photoSizeScaleAndSaveImage2);
                                                tL_document3.flags |= 1;
                                            }
                                            bitmapLoadBitmap.recycle();
                                        }
                                        break;
                                    } catch (Throwable th2) {
                                        FileLog.e(th2);
                                        break;
                                    }
                                    break;
                                case "file":
                                    int iLastIndexOf = botInlineResult.content.mime_type.lastIndexOf(47);
                                    if (iLastIndexOf != -1) {
                                        tL_documentAttributeFilename.file_name = "file." + botInlineResult.content.mime_type.substring(iLastIndexOf + 1);
                                    } else {
                                        tL_documentAttributeFilename.file_name = "file";
                                    }
                                    str2 = str3;
                                    break;
                                case "audio":
                                    TLRPC.TL_documentAttributeAudio tL_documentAttributeAudio = new TLRPC.TL_documentAttributeAudio();
                                    tL_documentAttributeAudio.duration = MessageObject.getInlineResultDuration(botInlineResult);
                                    tL_documentAttributeAudio.title = botInlineResult.title;
                                    int i6 = tL_documentAttributeAudio.flags;
                                    tL_documentAttributeAudio.flags = i6 | 1;
                                    String str6 = botInlineResult.description;
                                    if (str6 != null) {
                                        tL_documentAttributeAudio.performer = str6;
                                        tL_documentAttributeAudio.flags = i6 | 3;
                                    }
                                    tL_documentAttributeFilename.file_name = "audio.mp3";
                                    tL_document3.attributes.add(tL_documentAttributeAudio);
                                    str2 = str3;
                                    break;
                                case "video":
                                    tL_documentAttributeFilename.file_name = "video.mp4";
                                    TLRPC.TL_documentAttributeVideo tL_documentAttributeVideo = new TLRPC.TL_documentAttributeVideo();
                                    int[] inlineResultWidthAndHeight2 = MessageObject.getInlineResultWidthAndHeight(botInlineResult);
                                    tL_documentAttributeVideo.w = inlineResultWidthAndHeight2[0];
                                    tL_documentAttributeVideo.h = inlineResultWidthAndHeight2[1];
                                    tL_documentAttributeVideo.duration = MessageObject.getInlineResultDuration(botInlineResult);
                                    tL_documentAttributeVideo.supports_streaming = true;
                                    tL_document3.attributes.add(tL_documentAttributeVideo);
                                    try {
                                        if (botInlineResult.thumb != null) {
                                            Bitmap bitmapLoadBitmap3 = ImageLoader.loadBitmap(new File(FileLoader.getDirectory(4), Utilities.MD5(botInlineResult.thumb.url) + "." + ImageLoader.getHttpUrlExtension(botInlineResult.thumb.url, "jpg")).getAbsolutePath(), null, 90.0f, 90.0f, true);
                                            if (bitmapLoadBitmap3 != null) {
                                                TLRPC.PhotoSize photoSizeScaleAndSaveImage3 = ImageLoader.scaleAndSaveImage(bitmapLoadBitmap3, 90.0f, 90.0f, 55, false);
                                                if (photoSizeScaleAndSaveImage3 != null) {
                                                    tL_document3.thumbs.add(photoSizeScaleAndSaveImage3);
                                                    tL_document3.flags |= 1;
                                                }
                                                bitmapLoadBitmap3.recycle();
                                            }
                                        }
                                        break;
                                    } catch (Throwable th3) {
                                        FileLog.e(th3);
                                    }
                                    str2 = str3;
                                    break;
                                case "voice":
                                    TLRPC.TL_documentAttributeAudio tL_documentAttributeAudio2 = new TLRPC.TL_documentAttributeAudio();
                                    tL_documentAttributeAudio2.duration = MessageObject.getInlineResultDuration(botInlineResult);
                                    tL_documentAttributeAudio2.voice = true;
                                    tL_documentAttributeFilename.file_name = "audio.ogg";
                                    tL_document3.attributes.add(tL_documentAttributeAudio2);
                                    str2 = str3;
                                    break;
                                default:
                                    str2 = str3;
                                    break;
                            }
                            if (tL_documentAttributeFilename.file_name == null) {
                                tL_documentAttributeFilename.file_name = "file";
                            }
                            if (tL_document3.mime_type == null) {
                                tL_document3.mime_type = "application/octet-stream";
                            }
                            if (tL_document3.thumbs.isEmpty()) {
                                TLRPC.TL_photoSize tL_photoSize = new TLRPC.TL_photoSize();
                                int[] inlineResultWidthAndHeight3 = MessageObject.getInlineResultWidthAndHeight(botInlineResult);
                                tL_photoSize.w = inlineResultWidthAndHeight3[0];
                                tL_photoSize.h = inlineResultWidthAndHeight3[1];
                                tL_photoSize.size = 0;
                                tL_photoSize.location = new TLRPC.TL_fileLocationUnavailable();
                                tL_photoSize.type = "x";
                                tL_document3.thumbs.add(tL_photoSize);
                                tL_document3.flags |= 1;
                            }
                            obj = str2;
                            tL_document = tL_document3;
                            tL_game = null;
                            r14 = 0;
                            break;
                        case "photo":
                            TLRPC.TL_photo tL_photoGeneratePhotoSizes = file.exists() ? accountInstance.getSendMessagesHelper().generatePhotoSizes(str3, null) : null;
                            if (tL_photoGeneratePhotoSizes == null) {
                                tL_photoGeneratePhotoSizes = new TLRPC.TL_photo();
                                tL_photoGeneratePhotoSizes.date = accountInstance.getConnectionsManager().getCurrentTime();
                                tL_photoGeneratePhotoSizes.file_reference = new byte[0];
                                TLRPC.TL_photoSize tL_photoSize2 = new TLRPC.TL_photoSize();
                                int[] inlineResultWidthAndHeight4 = MessageObject.getInlineResultWidthAndHeight(botInlineResult);
                                tL_photoSize2.w = inlineResultWidthAndHeight4[0];
                                tL_photoSize2.h = inlineResultWidthAndHeight4[1];
                                tL_photoSize2.size = 1;
                                tL_photoSize2.location = new TLRPC.TL_fileLocationUnavailable();
                                tL_photoSize2.type = "x";
                                tL_photoGeneratePhotoSizes.sizes.add(tL_photoSize2);
                            }
                            r14 = tL_photoGeneratePhotoSizes;
                            obj = str3;
                            tL_game = null;
                            tL_document = null;
                            break;
                        default:
                            obj = str3;
                            tL_game = null;
                            tL_document = null;
                            r14 = 0;
                            break;
                    }
                } else {
                    tL_game = null;
                    tL_document = null;
                }
            }
            if (map != null && (webDocument = botInlineResult.content) != null) {
                map.put("originalPath", webDocument.url);
            }
            bitmapArr = new Bitmap[1];
            strArr = new String[1];
            if (zIsEncryptedDialog && tL_document != null) {
                for (i4 = 0; i4 < tL_document.attributes.size(); i4++) {
                    if (tL_document.attributes.get(i4) instanceof TLRPC.TL_documentAttributeVideo) {
                        TLRPC.TL_documentAttributeVideo tL_documentAttributeVideo2 = (TLRPC.TL_documentAttributeVideo) tL_document.attributes.get(i4);
                        TLRPC.TL_documentAttributeVideo_layer159 tL_documentAttributeVideo_layer159 = new TLRPC.TL_documentAttributeVideo_layer159();
                        tL_documentAttributeVideo_layer159.flags = tL_documentAttributeVideo2.flags;
                        tL_documentAttributeVideo_layer159.round_message = tL_documentAttributeVideo2.round_message;
                        tL_documentAttributeVideo_layer159.supports_streaming = tL_documentAttributeVideo2.supports_streaming;
                        tL_documentAttributeVideo_layer159.duration = tL_documentAttributeVideo2.duration;
                        tL_documentAttributeVideo_layer159.w = tL_documentAttributeVideo2.w;
                        tL_documentAttributeVideo_layer159.h = tL_documentAttributeVideo2.h;
                        tL_document.attributes.set(i4, tL_documentAttributeVideo_layer159);
                    }
                }
            }
            if (MessageObject.isGifDocument(tL_document)) {
                TLRPC.PhotoSize closestPhotoSizeWithSize = FileLoader.getClosestPhotoSizeWithSize(tL_document.thumbs, 320);
                pathToAttach = FileLoader.getInstance(accountInstance.getCurrentAccount()).getPathToAttach(tL_document);
                if (pathToAttach.exists()) {
                    z2 = true;
                } else {
                    z2 = true;
                    pathToAttach = FileLoader.getInstance(accountInstance.getCurrentAccount()).getPathToAttach(tL_document, true);
                }
                String absolutePath2 = pathToAttach.getAbsolutePath();
                boolean z3 = z2;
                tL_document2 = tL_document;
                accountInstance2 = accountInstance;
                ensureMediaThumbExists(accountInstance2, zIsEncryptedDialog, tL_document2, absolutePath2, null, 0L);
                strArr[0] = getKeyForPhotoSize(accountInstance2, closestPhotoSizeWithSize, bitmapArr, z3, z3);
            } else {
                accountInstance2 = accountInstance;
                tL_document2 = tL_document;
            }
            if (DialogObject.isEncryptedDialog(j)) {
                j4 = j;
                inputPeer = null;
            } else {
                j4 = j;
                inputPeer = accountInstance2.getMessagesController().getInputPeer(j4);
            }
            if (inputPeer == null && inputPeer.user_id != 0 && accountInstance2.getMessagesController().getUserFull(inputPeer.user_id) != null && accountInstance2.getMessagesController().getUserFull(inputPeer.user_id).voice_messages_forbidden && tL_document2 != null) {
                if (MessageObject.isVoiceDocument(tL_document2)) {
                    AndroidUtilities.runOnUIThread(new Runnable() { // from class: org.telegram.messenger.SendMessagesHelper$$ExternalSyntheticLambda1
                        @Override // java.lang.Runnable
                        public final void run() {
                            AlertsCreator.showSendMediaAlert(7, baseFragment, null);
                        }
                    });
                    return;
                } else {
                    if (MessageObject.isRoundVideoDocument(tL_document2)) {
                        AndroidUtilities.runOnUIThread(new Runnable() { // from class: org.telegram.messenger.SendMessagesHelper$$ExternalSyntheticLambda2
                            @Override // java.lang.Runnable
                            public final void run() {
                                AlertsCreator.showSendMediaAlert(8, baseFragment, null);
                            }
                        });
                        return;
                    }
                    return;
                }
            }
            final TLRPC.TL_game tL_game2 = tL_game;
            final AccountInstance accountInstance3 = accountInstance2;
            final TLRPC.TL_document tL_document4 = tL_document2;
            final ?? r4 = obj;
            AndroidUtilities.runOnUIThread(new Runnable() { // from class: org.telegram.messenger.SendMessagesHelper$$ExternalSyntheticLambda3
                @Override // java.lang.Runnable
                public final void run() {
                    SendMessagesHelper.$r8$lambda$H7EHUjYkj9BzyDbyxcHmmvwznK0(tL_document4, bitmapArr, strArr, r4, j4, messageObject, messageObject2, botInlineResult, map, z, i, i2, r14, tL_game2, str, i3, storyItem, replyQuote, j2, j3, accountInstance3);
                }
            });
        }
        if (zIsEncryptedDialog) {
            return;
        }
        tL_game = new TLRPC.TL_game();
        tL_game.title = botInlineResult.title;
        tL_game.description = botInlineResult.description;
        tL_game.short_name = botInlineResult.id;
        TLRPC.Photo photo2 = botInlineResult.photo;
        tL_game.photo = photo2;
        if (photo2 == null) {
            tL_game.photo = new TLRPC.TL_photoEmpty();
        }
        TLRPC.Document document2 = botInlineResult.document;
        if (document2 instanceof TLRPC.TL_document) {
            tL_game.document = document2;
            tL_game.flags |= 1;
        }
        tL_document = null;
        obj = tL_document;
        r14 = obj;
        if (map != null) {
            map.put("originalPath", webDocument.url);
        }
        bitmapArr = new Bitmap[1];
        strArr = new String[1];
        if (zIsEncryptedDialog) {
            while (i4 < tL_document.attributes.size()) {
                if (tL_document.attributes.get(i4) instanceof TLRPC.TL_documentAttributeVideo) {
                    TLRPC.TL_documentAttributeVideo tL_documentAttributeVideo3 = (TLRPC.TL_documentAttributeVideo) tL_document.attributes.get(i4);
                    TLRPC.TL_documentAttributeVideo_layer159 tL_documentAttributeVideo_layer1510 = new TLRPC.TL_documentAttributeVideo_layer159();
                    tL_documentAttributeVideo_layer1510.flags = tL_documentAttributeVideo3.flags;
                    tL_documentAttributeVideo_layer1510.round_message = tL_documentAttributeVideo3.round_message;
                    tL_documentAttributeVideo_layer1510.supports_streaming = tL_documentAttributeVideo3.supports_streaming;
                    tL_documentAttributeVideo_layer1510.duration = tL_documentAttributeVideo3.duration;
                    tL_documentAttributeVideo_layer1510.w = tL_documentAttributeVideo3.w;
                    tL_documentAttributeVideo_layer1510.h = tL_documentAttributeVideo3.h;
                    tL_document.attributes.set(i4, tL_documentAttributeVideo_layer1510);
                }
            }
        }
        if (MessageObject.isGifDocument(tL_document)) {
            TLRPC.PhotoSize closestPhotoSizeWithSize2 = FileLoader.getClosestPhotoSizeWithSize(tL_document.thumbs, 320);
            pathToAttach = FileLoader.getInstance(accountInstance.getCurrentAccount()).getPathToAttach(tL_document);
            if (pathToAttach.exists()) {
                z2 = true;
                pathToAttach = FileLoader.getInstance(accountInstance.getCurrentAccount()).getPathToAttach(tL_document, true);
            } else {
                z2 = true;
            }
            String absolutePath3 = pathToAttach.getAbsolutePath();
            boolean z4 = z2;
            tL_document2 = tL_document;
            accountInstance2 = accountInstance;
            ensureMediaThumbExists(accountInstance2, zIsEncryptedDialog, tL_document2, absolutePath3, null, 0L);
            strArr[0] = getKeyForPhotoSize(accountInstance2, closestPhotoSizeWithSize2, bitmapArr, z4, z4);
        } else {
            accountInstance2 = accountInstance;
            tL_document2 = tL_document;
        }
        if (DialogObject.isEncryptedDialog(j)) {
            j4 = j;
            inputPeer = accountInstance2.getMessagesController().getInputPeer(j4);
        } else {
            j4 = j;
            inputPeer = null;
        }
        if (inputPeer == null) {
        }
        final TLRPC.TL_game tL_game3 = tL_game;
        final AccountInstance accountInstance4 = accountInstance2;
        final TLRPC.TL_document tL_document5 = tL_document2;
        final String r5 = obj;
        AndroidUtilities.runOnUIThread(new Runnable() { // from class: org.telegram.messenger.SendMessagesHelper$$ExternalSyntheticLambda3
            @Override // java.lang.Runnable
            public final void run() {
                SendMessagesHelper.$r8$lambda$H7EHUjYkj9BzyDbyxcHmmvwznK0(tL_document5, bitmapArr, strArr, r5, j4, messageObject, messageObject2, botInlineResult, map, z, i, i2, r14, tL_game3, str, i3, storyItem, replyQuote, j2, j3, accountInstance4);
            }
        });
    }

    public static /* synthetic */ void $r8$lambda$H7EHUjYkj9BzyDbyxcHmmvwznK0(TLRPC.TL_document tL_document, Bitmap[] bitmapArr, String[] strArr, String str, long j, MessageObject messageObject, MessageObject messageObject2, TLRPC.BotInlineResult botInlineResult, HashMap map, boolean z, int i, int i2, TLRPC.TL_photo tL_photo, TLRPC.TL_game tL_game, String str2, int i3, TL_stories.StoryItem storyItem, ChatActivity.ReplyQuote replyQuote, long j2, long j3, AccountInstance accountInstance) {
        SendMessageParams sendMessageParamsOf;
        if (tL_document != null) {
            if (bitmapArr[0] != null && strArr[0] != null) {
                ImageLoader.getInstance().putImageToCache(new BitmapDrawable(bitmapArr[0]), strArr[0], false);
            }
            TLRPC.BotInlineMessage botInlineMessage = botInlineResult.send_message;
            sendMessageParamsOf = SendMessageParams.of(tL_document, null, str, j, messageObject, messageObject2, botInlineMessage.message, botInlineMessage.entities, botInlineMessage.reply_markup, map, z, i, i2, 0, botInlineResult, null, false);
        } else {
            sendMessageParamsOf = null;
            if (tL_photo != null) {
                TLRPC.WebDocument webDocument = botInlineResult.content;
                String str3 = webDocument != null ? webDocument.url : null;
                TLRPC.BotInlineMessage botInlineMessage2 = botInlineResult.send_message;
                sendMessageParamsOf = SendMessageParams.of(tL_photo, str3, j, messageObject, messageObject2, botInlineMessage2.message, botInlineMessage2.entities, botInlineMessage2.reply_markup, map, z, i, i2, 0, botInlineResult, false);
            } else if (tL_game != null) {
                sendMessageParamsOf = SendMessageParams.of(tL_game, j, messageObject, messageObject2, botInlineResult.send_message.reply_markup, (HashMap<String, String>) map, z, i, i2);
            }
        }
        if (sendMessageParamsOf != null) {
            sendMessageParamsOf.quick_reply_shortcut = str2;
            sendMessageParamsOf.quick_reply_shortcut_id = i3;
            sendMessageParamsOf.replyToStoryItem = storyItem;
            sendMessageParamsOf.replyQuote = replyQuote;
            sendMessageParamsOf.payStars = j2;
            sendMessageParamsOf.monoForumPeer = j3;
            accountInstance.getSendMessagesHelper().sendMessage(sendMessageParamsOf);
        }
    }

    public static String getTrimmedString(String str) {
        String strTrim = str.trim();
        if (strTrim.length() == 0) {
            return strTrim;
        }
        while (str.startsWith("\n")) {
            str = str.substring(1);
        }
        while (str.endsWith("\n")) {
            str = str.substring(0, str.length() - 1);
        }
        return str;
    }

    public static void prepareSendingText(AccountInstance accountInstance, String str, long j, boolean z, int i, int i2, long j2) {
        prepareSendingText(accountInstance, str, j, 0L, z, i, i2, j2);
    }

    public static void prepareSendingText(final AccountInstance accountInstance, final String str, final long j, final long j2, final boolean z, final int i, final int i2, final long j3) {
        accountInstance.getMessagesStorage().getStorageQueue().postRunnable(new Runnable() { // from class: org.telegram.messenger.SendMessagesHelper$$ExternalSyntheticLambda63
            @Override // java.lang.Runnable
            public final void run() {
                Utilities.stageQueue.postRunnable(new Runnable() { // from class: org.telegram.messenger.SendMessagesHelper$$ExternalSyntheticLambda105
                    @Override // java.lang.Runnable
                    public final void run() {
                        AndroidUtilities.runOnUIThread(new Runnable() { // from class: org.telegram.messenger.SendMessagesHelper$$ExternalSyntheticLambda114
                            @Override // java.lang.Runnable
                            public final void run() {
                                SendMessagesHelper.m3700$r8$lambda$1K39PKhS6JOtkmycJhn5Dlv2Hk(str, j, accountInstance, j, z, i, i, j);
                            }
                        });
                    }
                });
            }
        });
    }

    /* JADX WARN: Code duplicated, block: B:15:0x004c  */
    /* JADX WARN: Code duplicated, block: B:17:0x0074  */
    /* JADX WARN: Code duplicated, block: B:18:0x0079  */
    /* JADX INFO: renamed from: $r8$lambda$1K-39PKhS6JOtkmycJhn5Dlv2Hk, reason: not valid java name */
    public static /* synthetic */ void m3700$r8$lambda$1K39PKhS6JOtkmycJhn5Dlv2Hk(String str, long j, AccountInstance accountInstance, long j2, boolean z, int i, int i2, long j3) {
        long j4;
        MessageObject messageObject;
        MessageObject messageObject2;
        SendMessageParams sendMessageParamsOf;
        String trimmedString = getTrimmedString(str);
        if (trimmedString.length() != 0) {
            int iCeil = (int) Math.ceil(trimmedString.length() / 4096.0f);
            int i3 = 0;
            if (j != 0) {
                j4 = j2;
                TLRPC.TL_forumTopic tL_forumTopicFindTopic = accountInstance.getMessagesController().getTopicsController().findTopic(-j4, j);
                if (tL_forumTopicFindTopic != null && tL_forumTopicFindTopic.topicStartMessage != null) {
                    messageObject = new MessageObject(accountInstance.getCurrentAccount(), tL_forumTopicFindTopic.topicStartMessage, false, false);
                    messageObject.isTopicMainMessage = true;
                }
                messageObject2 = messageObject;
                while (i3 < iCeil) {
                    int i4 = i3 + 1;
                    sendMessageParamsOf = SendMessageParams.of(trimmedString.substring(i3 * 4096, Math.min(i4 * 4096, trimmedString.length())), j4, messageObject2, messageObject2, null, true, null, null, null, z, i, i2, null, false);
                    if (i3 == 0) {
                        sendMessageParamsOf.effect_id = j3;
                    }
                    accountInstance.getSendMessagesHelper().sendMessage(sendMessageParamsOf);
                    j4 = j2;
                    i3 = i4;
                }
            }
            j4 = j2;
            messageObject = null;
            messageObject2 = messageObject;
            while (i3 < iCeil) {
                int i5 = i3 + 1;
                sendMessageParamsOf = SendMessageParams.of(trimmedString.substring(i3 * 4096, Math.min(i5 * 4096, trimmedString.length())), j4, messageObject2, messageObject2, null, true, null, null, null, z, i, i2, null, false);
                if (i3 == 0) {
                    sendMessageParamsOf.effect_id = j3;
                }
                accountInstance.getSendMessagesHelper().sendMessage(sendMessageParamsOf);
                j4 = j2;
                i3 = i5;
            }
        }
    }

    public static void ensureMediaThumbExists(AccountInstance accountInstance, boolean z, TLObject tLObject, String str, Uri uri, long j) {
        ensureMediaThumbExists(accountInstance, z, tLObject, str, uri, j, false);
    }

    public static void ensureMediaThumbExists(AccountInstance accountInstance, boolean z, TLObject tLObject, String str, Uri uri, long j, boolean z2) {
        TLRPC.PhotoSize photoSizeScaleAndSaveImage;
        TLRPC.PhotoSize photoSizeScaleAndSaveImage2;
        if (tLObject instanceof TLRPC.TL_photo) {
            TLRPC.TL_photo tL_photo = (TLRPC.TL_photo) tLObject;
            TLRPC.PhotoSize closestPhotoSizeWithSize = FileLoader.getClosestPhotoSizeWithSize(tL_photo.sizes, 90);
            boolean zExists = ((closestPhotoSizeWithSize instanceof TLRPC.TL_photoStrippedSize) || (closestPhotoSizeWithSize instanceof TLRPC.TL_photoPathSize)) ? true : FileLoader.getInstance(accountInstance.getCurrentAccount()).getPathToAttach(closestPhotoSizeWithSize, true).exists();
            TLRPC.PhotoSize closestPhotoSizeWithSize2 = FileLoader.getClosestPhotoSizeWithSize(tL_photo.sizes, AndroidUtilities.getPhotoSize(z2));
            boolean zExists2 = FileLoader.getInstance(accountInstance.getCurrentAccount()).getPathToAttach(closestPhotoSizeWithSize2, false).exists();
            if (zExists && zExists2) {
                return;
            }
            Bitmap bitmapLoadBitmap = ImageLoader.loadBitmap(str, uri, AndroidUtilities.getPhotoSize(), AndroidUtilities.getPhotoSize(), true);
            if (bitmapLoadBitmap == null) {
                bitmapLoadBitmap = ImageLoader.loadBitmap(str, uri, 800.0f, 800.0f, true);
            }
            Bitmap bitmap = bitmapLoadBitmap;
            if (!zExists2 && (photoSizeScaleAndSaveImage2 = ImageLoader.scaleAndSaveImage(closestPhotoSizeWithSize2, bitmap, Bitmap.CompressFormat.JPEG, true, AndroidUtilities.getPhotoSize(), AndroidUtilities.getPhotoSize(), 80, false, 101, 101, false)) != closestPhotoSizeWithSize2) {
                tL_photo.sizes.add(0, photoSizeScaleAndSaveImage2);
            }
            if (!zExists && (photoSizeScaleAndSaveImage = ImageLoader.scaleAndSaveImage(closestPhotoSizeWithSize, bitmap, 90.0f, 90.0f, 55, true, false)) != closestPhotoSizeWithSize) {
                tL_photo.sizes.add(0, photoSizeScaleAndSaveImage);
            }
            if (bitmap != null) {
                bitmap.recycle();
                return;
            }
            return;
        }
        if (tLObject instanceof TLRPC.TL_document) {
            TLRPC.TL_document tL_document = (TLRPC.TL_document) tLObject;
            if ((MessageObject.isVideoDocument(tL_document) || MessageObject.isNewGifDocument(tL_document)) && MessageObject.isDocumentHasThumb(tL_document)) {
                TLRPC.PhotoSize closestPhotoSizeWithSize3 = FileLoader.getClosestPhotoSizeWithSize(tL_document.thumbs, 320);
                if ((closestPhotoSizeWithSize3 instanceof TLRPC.TL_photoStrippedSize) || (closestPhotoSizeWithSize3 instanceof TLRPC.TL_photoPathSize) || FileLoader.getInstance(accountInstance.getCurrentAccount()).getPathToAttach(closestPhotoSizeWithSize3, true).exists()) {
                    return;
                }
                Bitmap bitmapCreateVideoThumbnailAtTime = createVideoThumbnailAtTime(str, j);
                if (bitmapCreateVideoThumbnailAtTime == null) {
                    bitmapCreateVideoThumbnailAtTime = createVideoThumbnail(str, 1);
                }
                int i = z ? 90 : 320;
                float f = i;
                tL_document.thumbs.set(0, ImageLoader.scaleAndSaveImage(closestPhotoSizeWithSize3, bitmapCreateVideoThumbnailAtTime, f, f, i > 90 ? 80 : 55, false, true));
            }
        }
    }

    public static String getKeyForPhotoSize(AccountInstance accountInstance, TLRPC.PhotoSize photoSize, Bitmap[] bitmapArr, boolean z, boolean z2) {
        if (photoSize == null || photoSize.location == null) {
            return null;
        }
        Point messageSize = ChatMessageCell.getMessageSize(photoSize.w, photoSize.h);
        if (bitmapArr != null) {
            try {
                BitmapFactory.Options options = new BitmapFactory.Options();
                options.inJustDecodeBounds = true;
                File pathToAttach = FileLoader.getInstance(accountInstance.getCurrentAccount()).getPathToAttach(photoSize, z2);
                FileInputStream fileInputStream = new FileInputStream(pathToAttach);
                BitmapFactory.decodeStream(fileInputStream, null, options);
                fileInputStream.close();
                float fMax = Math.max(options.outWidth / messageSize.x, options.outHeight / messageSize.y);
                if (fMax < 1.0f) {
                    fMax = 1.0f;
                }
                options.inJustDecodeBounds = false;
                options.inSampleSize = (int) fMax;
                options.inPreferredConfig = Bitmap.Config.RGB_565;
                FileInputStream fileInputStream2 = new FileInputStream(pathToAttach);
                bitmapArr[0] = BitmapFactory.decodeStream(fileInputStream2, null, options);
                fileInputStream2.close();
            } catch (Throwable unused) {
            }
        }
        return String.format(Locale.US, z ? "%d_%d@%d_%d_b" : "%d_%d@%d_%d", Long.valueOf(photoSize.location.volume_id), Integer.valueOf(photoSize.location.local_id), Integer.valueOf((int) (messageSize.x / AndroidUtilities.density)), Integer.valueOf((int) (messageSize.y / AndroidUtilities.density)));
    }

    public static boolean shouldSendWebPAsSticker(String str, Uri uri) {
        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = true;
        if (str != null) {
            try {
                BitmapFactory.decodeFile(str, options);
            } catch (Exception e) {
                FileLog.e(e);
            }
        } else {
            try {
                InputStream inputStreamOpenInputStream = ApplicationLoader.applicationContext.getContentResolver().openInputStream(uri);
                try {
                    BitmapFactory.decodeStream(inputStreamOpenInputStream, null, options);
                    if (inputStreamOpenInputStream != null) {
                        inputStreamOpenInputStream.close();
                    }
                } catch (Throwable th) {
                    if (inputStreamOpenInputStream != null) {
                        try {
                            inputStreamOpenInputStream.close();
                        } catch (Throwable th2) {
                            th.addSuppressed(th2);
                        }
                    }
                    throw th;
                }
            } catch (Exception unused) {
            }
        }
        return options.outWidth < 800 && options.outHeight < 800;
    }

    public static void prepareSendingMedia(final AccountInstance accountInstance, final ArrayList<SendingMediaInfo> arrayList, final long j, final MessageObject messageObject, final MessageObject messageObject2, final TL_stories.StoryItem storyItem, final ChatActivity.ReplyQuote replyQuote, final boolean z, boolean z2, final MessageObject messageObject3, final boolean z3, final int i, final int i2, int i3, final boolean z4, final InputContentInfoCompat inputContentInfoCompat, final String str, final int i4, final long j2, final boolean z5, final long j3, final long j4, final MessageSuggestionParams messageSuggestionParams) {
        final boolean z6;
        if (arrayList.isEmpty()) {
            return;
        }
        int size = arrayList.size();
        for (int i5 = 0; i5 < size; i5++) {
            if (arrayList.get(i5).ttl > 0) {
                z6 = false;
                mediaSendQueue.postRunnable(new Runnable() { // from class: org.telegram.messenger.SendMessagesHelper$$ExternalSyntheticLambda35
                    @Override // java.lang.Runnable
                    public final void run() {
                        SendMessagesHelper.lambda$prepareSendingMedia$117(arrayList, j, z, z6, accountInstance, messageObject3, messageObject, messageObject2, z3, i, i2, storyItem, replyQuote, str, i4, j2, z5, j3, j4, messageSuggestionParams, inputContentInfoCompat, z4);
                    }
                });
            }
        }
        z6 = z2;
        mediaSendQueue.postRunnable(new Runnable() { // from class: org.telegram.messenger.SendMessagesHelper$$ExternalSyntheticLambda35
            @Override // java.lang.Runnable
            public final void run() {
                SendMessagesHelper.lambda$prepareSendingMedia$117(arrayList, j, z, z6, accountInstance, messageObject3, messageObject, messageObject2, z3, i, i2, storyItem, replyQuote, str, i4, j2, z5, j3, j4, messageSuggestionParams, inputContentInfoCompat, z4);
            }
        });
    }

    /* JADX INFO: Access modifiers changed from: private */
    /* JADX WARN: Code duplicated, block: B:175:0x036b  */
    /* JADX WARN: Code duplicated, block: B:176:0x0394  */
    /* JADX WARN: Code duplicated, block: B:182:0x03a8  */
    /* JADX WARN: Code duplicated, block: B:185:0x03b2  */
    /* JADX WARN: Code duplicated, block: B:275:0x0676  */
    /* JADX WARN: Code duplicated, block: B:277:0x0680  */
    /* JADX WARN: Code duplicated, block: B:307:0x06e8  */
    /* JADX WARN: Code duplicated, block: B:308:0x06f4  */
    /* JADX WARN: Code duplicated, block: B:310:0x06f9  */
    /* JADX WARN: Code duplicated, block: B:312:0x06fd  */
    /* JADX WARN: Code duplicated, block: B:314:0x071d  */
    /* JADX WARN: Code duplicated, block: B:317:0x074d  */
    /* JADX WARN: Code duplicated, block: B:319:0x0751  */
    /* JADX WARN: Code duplicated, block: B:321:0x077b  */
    /* JADX WARN: Code duplicated, block: B:323:0x0783  */
    /* JADX WARN: Code duplicated, block: B:331:0x07bb  */
    /* JADX WARN: Code duplicated, block: B:332:0x07bd  */
    /* JADX WARN: Code duplicated, block: B:358:0x0820  */
    /* JADX WARN: Code duplicated, block: B:361:0x0850  */
    /* JADX WARN: Code duplicated, block: B:363:0x086d  */
    /* JADX WARN: Code duplicated, block: B:365:0x087b  */
    /* JADX WARN: Code duplicated, block: B:369:0x088c  */
    /* JADX WARN: Code duplicated, block: B:371:0x0890  */
    /* JADX WARN: Code duplicated, block: B:373:0x089d  */
    /* JADX WARN: Code duplicated, block: B:376:0x08a5  */
    /* JADX WARN: Code duplicated, block: B:379:0x08aa  */
    /* JADX WARN: Code duplicated, block: B:382:0x08cb A[LOOP:2: B:380:0x08c3->B:382:0x08cb, LOOP_END] */
    /* JADX WARN: Code duplicated, block: B:385:0x08e9  */
    /* JADX WARN: Code duplicated, block: B:387:0x08ee  */
    /* JADX WARN: Code duplicated, block: B:395:0x08fe  */
    /* JADX WARN: Code duplicated, block: B:398:0x090d A[Catch: Exception -> 0x0915, TRY_LEAVE, TryCatch #7 {Exception -> 0x0915, blocks: (B:396:0x08ff, B:398:0x090d), top: B:716:0x08ff }] */
    /* JADX WARN: Code duplicated, block: B:403:0x091b  */
    /* JADX WARN: Code duplicated, block: B:405:0x0935  */
    /* JADX WARN: Code duplicated, block: B:408:0x0940  */
    /* JADX WARN: Code duplicated, block: B:410:0x0949  */
    /* JADX WARN: Code duplicated, block: B:412:0x0998  */
    /* JADX WARN: Code duplicated, block: B:414:0x09a1  */
    /* JADX WARN: Code duplicated, block: B:416:0x09c3  */
    /* JADX WARN: Code duplicated, block: B:479:0x0b88  */
    /* JADX WARN: Code duplicated, block: B:481:0x0b8c  */
    /* JADX WARN: Code duplicated, block: B:482:0x0b91  */
    /* JADX WARN: Code duplicated, block: B:488:0x0ba6  */
    /* JADX WARN: Code duplicated, block: B:493:0x0bba  */
    /* JADX WARN: Code duplicated, block: B:499:0x0bc6  */
    /* JADX WARN: Code duplicated, block: B:502:0x0bce  */
    /* JADX WARN: Code duplicated, block: B:503:0x0bd1  */
    /* JADX WARN: Code duplicated, block: B:511:0x0c11  */
    /* JADX WARN: Code duplicated, block: B:514:0x0c31  */
    /* JADX WARN: Code duplicated, block: B:517:0x0c42  */
    /* JADX WARN: Code duplicated, block: B:520:0x0c4c  */
    /* JADX WARN: Code duplicated, block: B:523:0x0c5a  */
    /* JADX WARN: Code duplicated, block: B:530:0x0ca8  */
    /* JADX WARN: Code duplicated, block: B:532:0x0cad  */
    /* JADX WARN: Code duplicated, block: B:533:0x0cb3  */
    /* JADX WARN: Code duplicated, block: B:536:0x0cc2  */
    /* JADX WARN: Code duplicated, block: B:541:0x0ccd  */
    /* JADX WARN: Code duplicated, block: B:562:0x0d27  */
    /* JADX WARN: Code duplicated, block: B:565:0x0d42  */
    /* JADX WARN: Code duplicated, block: B:567:0x0d59  */
    /* JADX WARN: Code duplicated, block: B:569:0x0d5d  */
    /* JADX WARN: Code duplicated, block: B:570:0x0d62  */
    /* JADX WARN: Code duplicated, block: B:577:0x0d77  */
    /* JADX WARN: Code duplicated, block: B:582:0x0d8b  */
    /* JADX WARN: Code duplicated, block: B:588:0x0d97  */
    /* JADX WARN: Code duplicated, block: B:591:0x0d9f  */
    /* JADX WARN: Code duplicated, block: B:592:0x0da2  */
    /* JADX WARN: Code duplicated, block: B:598:0x0dc5  */
    /* JADX WARN: Code duplicated, block: B:599:0x0dca  */
    /* JADX WARN: Code duplicated, block: B:601:0x0dd6  */
    /* JADX WARN: Code duplicated, block: B:603:0x0de2  */
    /* JADX WARN: Code duplicated, block: B:615:0x0e10  */
    /* JADX WARN: Code duplicated, block: B:618:0x0e1b A[LOOP:3: B:614:0x0e0e->B:618:0x0e1b, LOOP_END] */
    /* JADX WARN: Code duplicated, block: B:630:0x0e6a  */
    /* JADX WARN: Code duplicated, block: B:633:0x0e73  */
    /* JADX WARN: Code duplicated, block: B:635:0x0e78  */
    /* JADX WARN: Code duplicated, block: B:645:0x0eb4  */
    /* JADX WARN: Code duplicated, block: B:657:0x0efa A[LOOP:4: B:655:0x0ef2->B:657:0x0efa, LOOP_END] */
    /* JADX WARN: Code duplicated, block: B:66:0x00db  */
    /* JADX WARN: Code duplicated, block: B:67:0x0101  */
    /* JADX WARN: Code duplicated, block: B:69:0x0107  */
    /* JADX WARN: Code duplicated, block: B:714:0x0790 A[EXC_TOP_SPLITTER, SYNTHETIC] */
    /* JADX WARN: Code duplicated, block: B:728:0x08f3 A[EXC_TOP_SPLITTER, SYNTHETIC] */
    /* JADX WARN: Code duplicated, block: B:732:0x0660 A[EXC_TOP_SPLITTER, SYNTHETIC] */
    /* JADX WARN: Code duplicated, block: B:745:0x0e1e A[SYNTHETIC] */
    /* JADX WARN: Code duplicated, block: B:746:0x0e28 A[EDGE_INSN: B:746:0x0e28->B:620:0x0e28 BREAK  A[LOOP:3: B:614:0x0e0e->B:618:0x0e1b], SYNTHETIC] */
    /* JADX WARN: Code duplicated, block: B:84:0x0135  */
    /* JADX WARN: Code duplicated, block: B:94:0x0176  */
    /* JADX WARN: Code duplicated, block: B:97:0x018c  */
    /* JADX WARN: Code duplicated, block: B:98:0x0192  */
    /* JADX WARN: Instruction removed from duplicated block: B:66:0x00db, please report this as an issue */
    /* JADX WARN: Multi-variable type inference failed */
    /* JADX WARN: Type inference failed for: r0v46 */
    /* JADX WARN: Type inference failed for: r0v47, types: [boolean] */
    /* JADX WARN: Type inference failed for: r0v60 */
    /* JADX WARN: Type inference failed for: r21v1 */
    /* JADX WARN: Type inference failed for: r21v2, types: [boolean] */
    /* JADX WARN: Type inference failed for: r21v8 */
    /* JADX WARN: Type inference failed for: r2v0 */
    /* JADX WARN: Type inference failed for: r2v104, types: [boolean] */
    /* JADX WARN: Type inference failed for: r2v106 */
    /* JADX WARN: Type inference failed for: r6v49, types: [org.telegram.messenger.MediaController$PhotoEntry] */
    /* JADX WARN: Type inference failed for: r7v72 */
    /* JADX WARN: Type inference failed for: r7v78 */
    /* JADX WARN: Type inference failed for: r7v79 */
    /* JADX WARN: Type inference failed for: r9v65 */
    /* JADX WARN: Type inference fix 'apply assigned field type' failed
    java.lang.UnsupportedOperationException: ArgType.getObject(), call class: class jadx.core.dex.instructions.args.ArgType$UnknownArg
    	at jadx.core.dex.instructions.args.ArgType.getObject(ArgType.java:596)
    	at jadx.core.dex.attributes.nodes.ClassTypeVarsAttr.getTypeVarsMapFor(ClassTypeVarsAttr.java:35)
    	at jadx.core.dex.nodes.utils.TypeUtils.replaceClassGenerics(TypeUtils.java:177)
    	at jadx.core.dex.visitors.typeinference.FixTypesVisitor.insertExplicitUseCast(FixTypesVisitor.java:397)
    	at jadx.core.dex.visitors.typeinference.FixTypesVisitor.tryFieldTypeWithNewCasts(FixTypesVisitor.java:359)
    	at jadx.core.dex.visitors.typeinference.FixTypesVisitor.applyFieldType(FixTypesVisitor.java:309)
    	at jadx.core.dex.visitors.typeinference.FixTypesVisitor.visit(FixTypesVisitor.java:94)
     */
    public static /* synthetic */ void lambda$prepareSendingMedia$117(ArrayList arrayList, final long j, boolean z, boolean z2, final AccountInstance accountInstance, final MessageObject messageObject, final MessageObject messageObject2, final MessageObject messageObject3, final boolean z3, final int i, final int i2, final TL_stories.StoryItem storyItem, final ChatActivity.ReplyQuote replyQuote, final String str, final int i3, final long j2, final boolean z4, final long j3, final long j4, final MessageSuggestionParams messageSuggestionParams, InputContentInfoCompat inputContentInfoCompat, final boolean z5) {
        int i4;
        int i5;
        HashMap map;
        boolean z6;
        int i6;
        long j5;
        boolean z7;
        HashMap map2;
        String str2;
        String str3;
        String str4;
        int i7;
        long j6;
        VideoEditedInfo videoEditedInfoCreateCompressionSettings;
        int i8;
        Object obj;
        String str5;
        long j7;
        boolean z8;
        Object obj2;
        VideoEditedInfo videoEditedInfo;
        String str6;
        TLRPC.TL_document tL_document;
        boolean z9;
        AccountInstance accountInstance2;
        SendingMediaInfo sendingMediaInfo;
        String str7;
        Object obj3;
        Object obj4;
        Object obj5;
        boolean z10;
        VideoEditedInfo videoEditedInfo2;
        Object obj6;
        Object obj7;
        Bitmap bitmap;
        String keyForPhotoSize;
        String str8;
        Bitmap bitmapCreateVideoThumbnailAtTime;
        Bitmap bitmap2;
        Object obj8;
        Object obj9;
        int i9;
        TLRPC.PhotoSize photoSizeScaleAndSaveImage;
        int iMax;
        Bitmap.CompressFormat compressFormat;
        int i10;
        Object obj10;
        Object obj11;
        Object obj12;
        Object obj13;
        Object obj14;
        TLRPC.PhotoSize photoSizeFileToSize;
        String absolutePath;
        final HashMap map3;
        String str9;
        long j8;
        int i11;
        int i12;
        ArrayList<TLRPC.InputDocument> arrayList2;
        SerializedData serializedData;
        int i13;
        int size;
        int i14;
        String str10;
        Bitmap bitmapCreateVideoThumbnailAtTime2;
        Bitmap bitmap3;
        Object obj15;
        Object obj16;
        Object obj17;
        Object obj18;
        Object obj19;
        String str11;
        int i15;
        boolean z11;
        TLRPC.PhotoSize photoSizeScaleAndSaveImage2;
        String keyForPhotoSize2;
        Object obj20;
        Object obj21;
        Object obj22;
        Object obj23;
        Object obj24;
        TLRPC.TL_document tL_document2;
        VideoEditedInfo videoEditedInfo3;
        VideoEditedInfo videoEditedInfo4;
        String str12;
        TLRPC.TL_documentAttributeVideo tL_documentAttributeVideo;
        Object obj25;
        int i16;
        int i17;
        TLRPC.FileLocation fileLocation;
        int iMax2;
        Bitmap.CompressFormat compressFormat2;
        int i18;
        Object obj26;
        Object obj27;
        Object obj28;
        Object obj29;
        Object obj30;
        MediaController.SearchImage searchImage;
        String string;
        String strCopyFileToCache;
        String str13;
        String str14;
        boolean z12;
        ArrayList arrayList3;
        String str15;
        String string2;
        Object obj31;
        String str16;
        int i19;
        AccountInstance accountInstance3;
        Object obj32;
        String str17;
        TLRPC.TL_photo tL_photo;
        String str18;
        final TLRPC.TL_photo tL_photo2;
        String str19;
        int i20;
        TLRPC.TL_photo tL_photo3;
        String str20;
        TLRPC.TL_photo tL_photo4;
        TLRPC.TL_photo tL_photo5;
        Object obj33;
        Object obj34;
        ArrayList arrayList4;
        final HashMap map4;
        final Bitmap[] bitmapArr;
        final String[] strArr;
        ArrayList<TLRPC.InputDocument> arrayList5;
        ?? r0;
        boolean z13;
        int i21;
        long j9;
        TLRPC.PhotoSize closestPhotoSizeWithSize;
        SerializedData serializedData2;
        int i22;
        MediaSendPrepareWorker mediaSendPrepareWorker;
        TLRPC.TL_photo tL_photo6;
        String str21;
        ArrayList arrayList6;
        ArrayList arrayList7;
        Uri uri;
        FileOutputStream fileOutputStream;
        InputStream inputStreamOpenInputStream;
        Uri uri2;
        final boolean z14;
        long j10;
        int i23;
        int i24;
        int i25;
        File file;
        TLRPC.TL_document tL_document3;
        Object obj35;
        File file2;
        final TLRPC.TL_document tL_document4;
        String string3;
        String str22;
        int i26;
        Bitmap bitmapCreateVideoThumbnail;
        String string4;
        boolean zIsWebp;
        int i27;
        String str23;
        final AccountInstance accountInstance4;
        TLRPC.TL_photo tL_photo7;
        String str24;
        final MediaSendPrepareWorker mediaSendPrepareWorker2;
        String str25;
        TLRPC.TL_photo tL_photo8;
        Object[] sentFile;
        Uri uri3;
        Uri uri4;
        ArrayList arrayList8 = arrayList;
        long jCurrentTimeMillis = System.currentTimeMillis();
        int size2 = arrayList8.size();
        final boolean zIsEncryptedDialog = DialogObject.isEncryptedDialog(j);
        String str26 = ".gif";
        String str27 = "_";
        ?? r2 = 1;
        if (z || !z2) {
            i4 = 1;
            i5 = 0;
            map = null;
        } else {
            HashMap map5 = new HashMap();
            int i28 = 0;
            while (i28 < size2) {
                final SendingMediaInfo sendingMediaInfo2 = (SendingMediaInfo) arrayList8.get(i28);
                if (sendingMediaInfo2.searchImage == null && !sendingMediaInfo2.isVideo && sendingMediaInfo2.videoEditedInfo == null) {
                    ?? r6 = sendingMediaInfo2.originalPhotoEntry;
                    if (r6 != 0 && sendingMediaInfo2.highQuality) {
                        r6.rebuildPhoto(r2);
                        String str28 = sendingMediaInfo2.originalPhotoEntry.imagePath;
                        if (str28 != null) {
                            sendingMediaInfo2.path = str28;
                        }
                    }
                    String path = sendingMediaInfo2.path;
                    if (path != null || (uri4 = sendingMediaInfo2.uri) == null) {
                        string4 = path;
                    } else {
                        path = AndroidUtilities.getPath(uri4);
                        string4 = sendingMediaInfo2.uri.toString();
                    }
                    if (path == null || sendingMediaInfo2.ttl > 0) {
                        zIsWebp = false;
                    } else {
                        if (path.endsWith(".gif")) {
                            zIsWebp = false;
                        } else {
                            zIsWebp = path.endsWith(".webp");
                            if (zIsWebp) {
                            }
                            if (path != null) {
                                File file3 = new File(path);
                                str23 = string4 + file3.length() + "_" + file3.lastModified();
                            } else {
                                str23 = null;
                            }
                            if (zIsEncryptedDialog == 0 || sendingMediaInfo2.ttl != 0) {
                                accountInstance4 = accountInstance;
                                i28 = i28;
                                zIsEncryptedDialog = zIsEncryptedDialog;
                                tL_photo7 = null;
                                str24 = null;
                            } else {
                                int i29 = sendingMediaInfo2.highQuality ? !zIsEncryptedDialog ? 6 : 7 : zIsEncryptedDialog == 0 ? i27 : 3;
                                Object[] sentFile2 = accountInstance.getMessagesStorage().getSentFile(str23, i29);
                                if (sentFile2 != null) {
                                    Object obj36 = sentFile2[i27];
                                    if (obj36 instanceof TLRPC.TL_photo) {
                                        tL_photo8 = (TLRPC.TL_photo) obj36;
                                        str25 = (String) sentFile2[r2 == true ? 1 : 0];
                                    } else {
                                        str25 = null;
                                        tL_photo8 = null;
                                    }
                                } else {
                                    str25 = null;
                                    tL_photo8 = null;
                                }
                                if (tL_photo8 == null && sendingMediaInfo2.uri != null && (sentFile = accountInstance.getMessagesStorage().getSentFile(AndroidUtilities.getPath(sendingMediaInfo2.uri), i29)) != null) {
                                    Object obj37 = sentFile[i27];
                                    if (obj37 instanceof TLRPC.TL_photo) {
                                        tL_photo8 = (TLRPC.TL_photo) obj37;
                                        str25 = (String) sentFile[r2];
                                    }
                                }
                                str24 = str25;
                                int i30 = i28;
                                TLRPC.TL_photo tL_photo9 = tL_photo8;
                                i28 = i30;
                                zIsEncryptedDialog = zIsEncryptedDialog;
                                accountInstance4 = accountInstance;
                                ensureMediaThumbExists(accountInstance4, zIsEncryptedDialog, tL_photo9, sendingMediaInfo2.path, sendingMediaInfo2.uri, 0L, sendingMediaInfo2.highQuality);
                                tL_photo7 = tL_photo9;
                            }
                            mediaSendPrepareWorker2 = new MediaSendPrepareWorker();
                            map5.put(sendingMediaInfo2, mediaSendPrepareWorker2);
                            if (tL_photo7 != null) {
                                mediaSendPrepareWorker2.parentObject = str24;
                                mediaSendPrepareWorker2.photo = tL_photo7;
                                r2 = 1;
                            } else {
                                r2 = 1;
                                mediaSendPrepareWorker2.sync = new CountDownLatch(1);
                                mediaSendThreadPool.execute(new Runnable() { // from class: org.telegram.messenger.SendMessagesHelper$$ExternalSyntheticLambda97
                                    @Override // java.lang.Runnable
                                    public final void run() {
                                        SendMessagesHelper.$r8$lambda$wUL7g7xJTiOhNKMBL2NHaB41Kps(mediaSendPrepareWorker2, accountInstance4, sendingMediaInfo2, zIsEncryptedDialog);
                                    }
                                });
                            }
                        }
                        if (arrayList8.size() > r2 || ((zIsWebp && !shouldSendWebPAsSticker(path, null)) || !TextUtils.isEmpty(sendingMediaInfo2.caption))) {
                            sendingMediaInfo2.forceImage = r2;
                            i27 = 0;
                            if (path != null) {
                                File file4 = new File(path);
                                str23 = string4 + file4.length() + "_" + file4.lastModified();
                            } else {
                                str23 = null;
                            }
                            if (zIsEncryptedDialog == 0) {
                                accountInstance4 = accountInstance;
                                i28 = i28;
                                zIsEncryptedDialog = zIsEncryptedDialog;
                                tL_photo7 = null;
                                str24 = null;
                            } else {
                                accountInstance4 = accountInstance;
                                i28 = i28;
                                zIsEncryptedDialog = zIsEncryptedDialog;
                                tL_photo7 = null;
                                str24 = null;
                            }
                            mediaSendPrepareWorker2 = new MediaSendPrepareWorker();
                            map5.put(sendingMediaInfo2, mediaSendPrepareWorker2);
                            if (tL_photo7 != null) {
                                mediaSendPrepareWorker2.parentObject = str24;
                                mediaSendPrepareWorker2.photo = tL_photo7;
                                r2 = 1;
                            } else {
                                r2 = 1;
                                mediaSendPrepareWorker2.sync = new CountDownLatch(1);
                                mediaSendThreadPool.execute(new Runnable() { // from class: org.telegram.messenger.SendMessagesHelper$$ExternalSyntheticLambda97
                                    @Override // java.lang.Runnable
                                    public final void run() {
                                        SendMessagesHelper.$r8$lambda$wUL7g7xJTiOhNKMBL2NHaB41Kps(mediaSendPrepareWorker2, accountInstance4, sendingMediaInfo2, zIsEncryptedDialog);
                                    }
                                });
                            }
                        }
                    }
                    i27 = 0;
                    if (!ImageLoader.shouldSendImageAsDocument(sendingMediaInfo2.path, sendingMediaInfo2.uri)) {
                        if (path == null && (uri3 = sendingMediaInfo2.uri) != null && (MediaController.isGif(uri3) || (zIsWebp = MediaController.isWebp(sendingMediaInfo2.uri)))) {
                            if (arrayList8.size() > r2 || ((zIsWebp && !shouldSendWebPAsSticker(null, sendingMediaInfo2.uri)) || !TextUtils.isEmpty(sendingMediaInfo2.caption))) {
                                sendingMediaInfo2.forceImage = r2;
                            }
                        }
                        if (path != null) {
                            File file5 = new File(path);
                            str23 = string4 + file5.length() + "_" + file5.lastModified();
                        } else {
                            str23 = null;
                        }
                        if (zIsEncryptedDialog == 0) {
                            accountInstance4 = accountInstance;
                            i28 = i28;
                            zIsEncryptedDialog = zIsEncryptedDialog;
                            tL_photo7 = null;
                            str24 = null;
                        } else {
                            accountInstance4 = accountInstance;
                            i28 = i28;
                            zIsEncryptedDialog = zIsEncryptedDialog;
                            tL_photo7 = null;
                            str24 = null;
                        }
                        mediaSendPrepareWorker2 = new MediaSendPrepareWorker();
                        map5.put(sendingMediaInfo2, mediaSendPrepareWorker2);
                        if (tL_photo7 != null) {
                            mediaSendPrepareWorker2.parentObject = str24;
                            mediaSendPrepareWorker2.photo = tL_photo7;
                            r2 = 1;
                        } else {
                            r2 = 1;
                            mediaSendPrepareWorker2.sync = new CountDownLatch(1);
                            mediaSendThreadPool.execute(new Runnable() { // from class: org.telegram.messenger.SendMessagesHelper$$ExternalSyntheticLambda97
                                @Override // java.lang.Runnable
                                public final void run() {
                                    SendMessagesHelper.$r8$lambda$wUL7g7xJTiOhNKMBL2NHaB41Kps(mediaSendPrepareWorker2, accountInstance4, sendingMediaInfo2, zIsEncryptedDialog);
                                }
                            });
                        }
                    }
                }
                i28++;
                r2 = r2;
            }
            i4 = r2 == true ? 1 : 0;
            i5 = 0;
            map = map5;
        }
        String str29 = ".webp";
        ArrayList arrayList9 = null;
        ArrayList arrayList10 = null;
        String fileExtension = null;
        ArrayList arrayList11 = null;
        ArrayList arrayList12 = null;
        ArrayList arrayList13 = null;
        int i31 = i5;
        int i32 = i31;
        long j11 = 0;
        long jNextLong = 0;
        while (i32 < size2) {
            final SendingMediaInfo sendingMediaInfo3 = (SendingMediaInfo) arrayList8.get(i32);
            final ?? r21 = i31 == 0 ? i4 : i5;
            if (z2 && size2 > i4 && i31 % 10 == 0) {
                jNextLong = Utilities.random.nextLong();
                i6 = i5;
                j5 = jNextLong;
            } else {
                i6 = i31;
                j5 = j11;
            }
            MediaController.SearchImage searchImage2 = sendingMediaInfo3.searchImage;
            ArrayList arrayList14 = arrayList10;
            int i33 = i32;
            String str30 = "1";
            String str31 = "final";
            ArrayList arrayList15 = arrayList9;
            int i34 = size2;
            if (searchImage2 != null && sendingMediaInfo3.videoEditedInfo == null) {
                HashMap map6 = map;
                String str32 = str26;
                if (searchImage2.type == 1) {
                    final HashMap map7 = new HashMap();
                    TLRPC.Document document = sendingMediaInfo3.searchImage.document;
                    if (document instanceof TLRPC.TL_document) {
                        tL_document3 = (TLRPC.TL_document) document;
                        file = FileLoader.getInstance(accountInstance.getCurrentAccount()).getPathToAttach(tL_document3, true);
                    } else {
                        file = new File(FileLoader.getDirectory(4), Utilities.MD5(sendingMediaInfo3.searchImage.imageUrl) + "." + ImageLoader.getHttpUrlExtension(sendingMediaInfo3.searchImage.imageUrl, "jpg"));
                        tL_document3 = null;
                    }
                    if (tL_document3 == null) {
                        TLRPC.TL_document tL_document5 = new TLRPC.TL_document();
                        tL_document5.id = 0L;
                        tL_document5.file_reference = new byte[i5];
                        tL_document5.date = accountInstance.getConnectionsManager().getCurrentTime();
                        TLRPC.TL_documentAttributeFilename tL_documentAttributeFilename = new TLRPC.TL_documentAttributeFilename();
                        tL_documentAttributeFilename.file_name = "animation.gif";
                        tL_document5.attributes.add(tL_documentAttributeFilename);
                        tL_document5.size = sendingMediaInfo3.searchImage.size;
                        tL_document5.dc_id = 0;
                        if (!z && file.toString().endsWith("mp4")) {
                            tL_document5.mime_type = "video/mp4";
                            tL_document5.attributes.add(new TLRPC.TL_documentAttributeAnimated());
                        } else {
                            tL_document5.mime_type = "image/gif";
                        }
                        if (file.exists()) {
                            file2 = file;
                        } else {
                            file = null;
                            file2 = null;
                        }
                        if (file == null) {
                            file = new File(FileLoader.getDirectory(4), Utilities.MD5(sendingMediaInfo3.searchImage.thumbUrl) + "." + ImageLoader.getHttpUrlExtension(sendingMediaInfo3.searchImage.thumbUrl, "jpg"));
                            if (!file.exists()) {
                                file = null;
                            }
                        }
                        if (file != null) {
                            if (!zIsEncryptedDialog) {
                                try {
                                    i26 = sendingMediaInfo3.ttl != 0 ? 90 : 320;
                                } catch (Exception e) {
                                    e = e;
                                    obj35 = null;
                                    FileLog.e(e);
                                    if (tL_document5.thumbs.isEmpty()) {
                                        TLRPC.TL_photoSize tL_photoSize = new TLRPC.TL_photoSize();
                                        MediaController.SearchImage searchImage3 = sendingMediaInfo3.searchImage;
                                        tL_photoSize.w = searchImage3.width;
                                        tL_photoSize.h = searchImage3.height;
                                        tL_photoSize.size = 0;
                                        tL_photoSize.location = new TLRPC.TL_fileLocationUnavailable();
                                        tL_photoSize.type = "x";
                                        tL_document5.thumbs.add(tL_photoSize);
                                        tL_document5.flags |= 1;
                                    }
                                    tL_document4 = tL_document5;
                                    string3 = sendingMediaInfo3.searchImage.imageUrl;
                                    if (file2 != null) {
                                        string3 = file2.toString();
                                    }
                                    str22 = sendingMediaInfo3.searchImage.imageUrl;
                                    if (str22 != null) {
                                        map7.put("originalPath", str22);
                                    }
                                    Object obj38 = obj35;
                                    final String str33 = null;
                                    final String str34 = string3;
                                    z7 = zIsEncryptedDialog;
                                    str3 = str27;
                                    i8 = i33;
                                    str4 = str29;
                                    map2 = map6;
                                    str2 = str32;
                                    AndroidUtilities.runOnUIThread(new Runnable() { // from class: org.telegram.messenger.SendMessagesHelper$$ExternalSyntheticLambda98
                                        @Override // java.lang.Runnable
                                        public final void run() {
                                            SendMessagesHelper.$r8$lambda$8cE258W29ZQqUsOyx0P3xSaj2tI(messageObject, accountInstance, tL_document4, str34, map7, sendingMediaInfo3, str33, j, messageObject2, messageObject3, z3, i, i2, storyItem, replyQuote, str, i3, r21, j2, z4, j3, j4, messageSuggestionParams);
                                        }
                                    });
                                    accountInstance = accountInstance;
                                    i31 = i6;
                                    arrayList9 = arrayList15;
                                    j6 = j5;
                                    arrayList10 = arrayList14;
                                    i7 = i34;
                                    obj = obj38;
                                    i32 = i8 + 1;
                                    arrayList8 = arrayList;
                                    zIsEncryptedDialog = z7;
                                    map = map2;
                                    str29 = str4;
                                    str26 = str2;
                                    str27 = str3;
                                    size2 = i7;
                                    j11 = j6;
                                    i4 = 1;
                                    i5 = 0;
                                }
                            }
                            if (file.getAbsolutePath().endsWith("mp4")) {
                                try {
                                    bitmapCreateVideoThumbnail = createVideoThumbnail(file.getAbsolutePath(), 1);
                                    obj35 = null;
                                } catch (Exception e2) {
                                    e = e2;
                                    obj35 = null;
                                    FileLog.e(e);
                                    if (tL_document5.thumbs.isEmpty()) {
                                        TLRPC.TL_photoSize tL_photoSize2 = new TLRPC.TL_photoSize();
                                        MediaController.SearchImage searchImage4 = sendingMediaInfo3.searchImage;
                                        tL_photoSize2.w = searchImage4.width;
                                        tL_photoSize2.h = searchImage4.height;
                                        tL_photoSize2.size = 0;
                                        tL_photoSize2.location = new TLRPC.TL_fileLocationUnavailable();
                                        tL_photoSize2.type = "x";
                                        tL_document5.thumbs.add(tL_photoSize2);
                                        tL_document5.flags |= 1;
                                    }
                                    tL_document4 = tL_document5;
                                    string3 = sendingMediaInfo3.searchImage.imageUrl;
                                    if (file2 != null) {
                                        string3 = file2.toString();
                                    }
                                    str22 = sendingMediaInfo3.searchImage.imageUrl;
                                    if (str22 != null) {
                                        map7.put("originalPath", str22);
                                    }
                                    Object obj39 = obj35;
                                    final String str35 = null;
                                    final String str36 = string3;
                                    z7 = zIsEncryptedDialog;
                                    str3 = str27;
                                    i8 = i33;
                                    str4 = str29;
                                    map2 = map6;
                                    str2 = str32;
                                    AndroidUtilities.runOnUIThread(new Runnable() { // from class: org.telegram.messenger.SendMessagesHelper$$ExternalSyntheticLambda98
                                        @Override // java.lang.Runnable
                                        public final void run() {
                                            SendMessagesHelper.$r8$lambda$8cE258W29ZQqUsOyx0P3xSaj2tI(messageObject, accountInstance, tL_document4, str36, map7, sendingMediaInfo3, str35, j, messageObject2, messageObject3, z3, i, i2, storyItem, replyQuote, str, i3, r21, j2, z4, j3, j4, messageSuggestionParams);
                                        }
                                    });
                                    accountInstance = accountInstance;
                                    i31 = i6;
                                    arrayList9 = arrayList15;
                                    j6 = j5;
                                    arrayList10 = arrayList14;
                                    i7 = i34;
                                    obj = obj39;
                                    i32 = i8 + 1;
                                    arrayList8 = arrayList;
                                    zIsEncryptedDialog = z7;
                                    map = map2;
                                    str29 = str4;
                                    str26 = str2;
                                    str27 = str3;
                                    size2 = i7;
                                    j11 = j6;
                                    i4 = 1;
                                    i5 = 0;
                                }
                            } else {
                                String absolutePath2 = file.getAbsolutePath();
                                float f = i26;
                                obj35 = null;
                                bitmapCreateVideoThumbnail = ImageLoader.loadBitmap(absolutePath2, null, f, f, true);
                            }
                            if (bitmapCreateVideoThumbnail != null) {
                                try {
                                    float f2 = i26;
                                    TLRPC.PhotoSize photoSizeScaleAndSaveImage3 = ImageLoader.scaleAndSaveImage(bitmapCreateVideoThumbnail, f2, f2, i26 > 90 ? 80 : 55, zIsEncryptedDialog);
                                    if (photoSizeScaleAndSaveImage3 != null) {
                                        tL_document5.thumbs.add(photoSizeScaleAndSaveImage3);
                                        tL_document5.flags |= 1;
                                    }
                                    bitmapCreateVideoThumbnail.recycle();
                                } catch (Exception e3) {
                                    e = e3;
                                    FileLog.e(e);
                                }
                            }
                        } else {
                            obj35 = null;
                        }
                        if (tL_document5.thumbs.isEmpty()) {
                            TLRPC.TL_photoSize tL_photoSize3 = new TLRPC.TL_photoSize();
                            MediaController.SearchImage searchImage5 = sendingMediaInfo3.searchImage;
                            tL_photoSize3.w = searchImage5.width;
                            tL_photoSize3.h = searchImage5.height;
                            tL_photoSize3.size = 0;
                            tL_photoSize3.location = new TLRPC.TL_fileLocationUnavailable();
                            tL_photoSize3.type = "x";
                            tL_document5.thumbs.add(tL_photoSize3);
                            tL_document5.flags |= 1;
                        }
                        tL_document4 = tL_document5;
                    } else {
                        obj35 = null;
                        file2 = file;
                        tL_document4 = tL_document3;
                    }
                    string3 = sendingMediaInfo3.searchImage.imageUrl;
                    if (file2 != null) {
                        string3 = file2.toString();
                    }
                    str22 = sendingMediaInfo3.searchImage.imageUrl;
                    if (str22 != null) {
                        map7.put("originalPath", str22);
                    }
                    Object obj310 = obj35;
                    final String str37 = null;
                    final String str38 = string3;
                    z7 = zIsEncryptedDialog;
                    str3 = str27;
                    i8 = i33;
                    str4 = str29;
                    map2 = map6;
                    str2 = str32;
                    AndroidUtilities.runOnUIThread(new Runnable() { // from class: org.telegram.messenger.SendMessagesHelper$$ExternalSyntheticLambda98
                        @Override // java.lang.Runnable
                        public final void run() {
                            SendMessagesHelper.$r8$lambda$8cE258W29ZQqUsOyx0P3xSaj2tI(messageObject, accountInstance, tL_document4, str38, map7, sendingMediaInfo3, str37, j, messageObject2, messageObject3, z3, i, i2, storyItem, replyQuote, str, i3, r21, j2, z4, j3, j4, messageSuggestionParams);
                        }
                    });
                    accountInstance = accountInstance;
                    i31 = i6;
                    arrayList9 = arrayList15;
                    j6 = j5;
                    arrayList10 = arrayList14;
                    i7 = i34;
                    obj = obj310;
                } else {
                    z7 = zIsEncryptedDialog;
                    str3 = str27;
                    str4 = str29;
                    map2 = map6;
                    str2 = str32;
                    TLRPC.Photo photo = searchImage2.photo;
                    TLRPC.TL_photo tL_photo10 = photo instanceof TLRPC.TL_photo ? (TLRPC.TL_photo) photo : null;
                    if (tL_photo10 == null) {
                        File file6 = new File(FileLoader.getDirectory(4), Utilities.MD5(sendingMediaInfo3.searchImage.imageUrl) + "." + ImageLoader.getHttpUrlExtension(sendingMediaInfo3.searchImage.imageUrl, "jpg"));
                        boolean z15 = !file6.exists() || file6.length() == 0 || (tL_photo10 = accountInstance.getSendMessagesHelper().generatePhotoSizes(file6.toString(), null)) == null;
                        if (tL_photo10 == null) {
                            File file7 = new File(FileLoader.getDirectory(4), Utilities.MD5(sendingMediaInfo3.searchImage.thumbUrl) + "." + ImageLoader.getHttpUrlExtension(sendingMediaInfo3.searchImage.thumbUrl, "jpg"));
                            if (file7.exists()) {
                                tL_photo10 = accountInstance.getSendMessagesHelper().generatePhotoSizes(file7.toString(), null);
                            }
                            if (tL_photo10 == null) {
                                tL_photo10 = new TLRPC.TL_photo();
                                tL_photo10.date = accountInstance.getConnectionsManager().getCurrentTime();
                                tL_photo10.file_reference = new byte[0];
                                TLRPC.TL_photoSize tL_photoSize4 = new TLRPC.TL_photoSize();
                                MediaController.SearchImage searchImage6 = sendingMediaInfo3.searchImage;
                                tL_photoSize4.w = searchImage6.width;
                                tL_photoSize4.h = searchImage6.height;
                                tL_photoSize4.size = 0;
                                tL_photoSize4.location = new TLRPC.TL_fileLocationUnavailable();
                                tL_photoSize4.type = "x";
                                tL_photo10.sizes.add(tL_photoSize4);
                            }
                        }
                        z14 = z15;
                    } else {
                        z14 = true;
                    }
                    final HashMap map8 = new HashMap();
                    String str39 = sendingMediaInfo3.searchImage.imageUrl;
                    if (str39 != null) {
                        map8.put("originalPath", str39);
                    }
                    if (z2) {
                        i6++;
                        StringBuilder sb = new StringBuilder();
                        sb.append(_UrlKt.FRAGMENT_ENCODE_SET);
                        j10 = j5;
                        sb.append(j10);
                        map8.put("groupId", sb.toString());
                        if (i6 != 10) {
                            i25 = i33;
                            if (i25 != i23 - 1) {
                                i23 = i34;
                                i24 = i25;
                            }
                        } else {
                            i23 = i34;
                            i25 = i33;
                        }
                        i23 = i34;
                        map8.put("final", "1");
                        i24 = i25;
                        jNextLong = 0;
                    } else {
                        j10 = j5;
                        i23 = i34;
                        i24 = i33;
                    }
                    long j12 = j10;
                    final String str40 = null;
                    j6 = j12;
                    i7 = i23;
                    final TLRPC.TL_photo tL_photo11 = tL_photo10;
                    AndroidUtilities.runOnUIThread(new Runnable() { // from class: org.telegram.messenger.SendMessagesHelper$$ExternalSyntheticLambda99
                        @Override // java.lang.Runnable
                        public final void run() {
                            SendMessagesHelper.$r8$lambda$QK2E_w0HUeKhLwnjwB42Kh8FiBU(messageObject, accountInstance, tL_photo11, z14, sendingMediaInfo3, map8, str40, j, messageObject2, messageObject3, z3, i, i2, storyItem, replyQuote, i3, str, j2, z4, j3, j4, messageSuggestionParams);
                        }
                    });
                    accountInstance = accountInstance;
                    obj = null;
                    i31 = i6;
                    arrayList10 = arrayList14;
                    i8 = i24;
                    arrayList9 = arrayList15;
                }
            } else {
                z7 = zIsEncryptedDialog;
                map2 = map;
                str2 = str26;
                str3 = str27;
                str4 = str29;
                i7 = i34;
                j6 = j5;
                if (sendingMediaInfo3.isVideo || sendingMediaInfo3.videoEditedInfo != null) {
                    if (z) {
                        videoEditedInfoCreateCompressionSettings = null;
                    } else {
                        videoEditedInfoCreateCompressionSettings = sendingMediaInfo3.videoEditedInfo;
                        if (videoEditedInfoCreateCompressionSettings == null) {
                            videoEditedInfoCreateCompressionSettings = createCompressionSettings(sendingMediaInfo3.path);
                        }
                    }
                    if (!z && (videoEditedInfoCreateCompressionSettings != null || sendingMediaInfo3.path.endsWith("mp4"))) {
                        if (sendingMediaInfo3.path == null && (searchImage = sendingMediaInfo3.searchImage) != null) {
                            if (searchImage.photo instanceof TLRPC.TL_photo) {
                                sendingMediaInfo3.path = FileLoader.getInstance(accountInstance.getCurrentAccount()).getPathToAttach(sendingMediaInfo3.searchImage.photo, true).getAbsolutePath();
                            } else {
                                sendingMediaInfo3.path = new File(FileLoader.getDirectory(4), Utilities.MD5(sendingMediaInfo3.searchImage.imageUrl) + "." + ImageLoader.getHttpUrlExtension(sendingMediaInfo3.searchImage.imageUrl, "jpg")).getAbsolutePath();
                            }
                        }
                        String str41 = sendingMediaInfo3.path;
                        File file8 = new File(str41);
                        StringBuilder sb2 = new StringBuilder();
                        sb2.append(str41);
                        sb2.append(file8.length());
                        str3 = str3;
                        sb2.append(str3);
                        sb2.append(file8.lastModified());
                        String string5 = sb2.toString();
                        if (videoEditedInfoCreateCompressionSettings != null) {
                            boolean z16 = videoEditedInfoCreateCompressionSettings.muted;
                            StringBuilder sb3 = new StringBuilder();
                            sb3.append(string5);
                            sb3.append(videoEditedInfoCreateCompressionSettings.estimatedDuration);
                            sb3.append(str3);
                            sb3.append(videoEditedInfoCreateCompressionSettings.startTime);
                            sb3.append(str3);
                            sb3.append(videoEditedInfoCreateCompressionSettings.endTime);
                            sb3.append(videoEditedInfoCreateCompressionSettings.muted ? "_m" : _UrlKt.FRAGMENT_ENCODE_SET);
                            String string6 = sb3.toString();
                            if (videoEditedInfoCreateCompressionSettings.resultWidth != videoEditedInfoCreateCompressionSettings.originalWidth) {
                                string6 = string6 + str3 + videoEditedInfoCreateCompressionSettings.resultWidth;
                            }
                            long j13 = videoEditedInfoCreateCompressionSettings.startTime;
                            if (j13 < 0) {
                                j13 = 0;
                            }
                            long j14 = j13;
                            str5 = string6;
                            j7 = j14;
                            z8 = z16;
                        } else {
                            str5 = string5;
                            j7 = 0;
                            z8 = false;
                        }
                        if (!z7 && sendingMediaInfo3.ttl == 0 && (videoEditedInfoCreateCompressionSettings == null || (videoEditedInfoCreateCompressionSettings.filterState == null && videoEditedInfoCreateCompressionSettings.paintPath == null && videoEditedInfoCreateCompressionSettings.mediaEntities == null && videoEditedInfoCreateCompressionSettings.cropState == null))) {
                            Object[] sentFile3 = accountInstance.getMessagesStorage().getSentFile(str5, !z7 ? 2 : 5);
                            if (sentFile3 != null) {
                                Object obj40 = sentFile3[0];
                                if (obj40 instanceof TLRPC.TL_document) {
                                    String str42 = (String) sentFile3[1];
                                    obj2 = "1";
                                    tL_document = (TLRPC.TL_document) obj40;
                                    videoEditedInfo = videoEditedInfoCreateCompressionSettings;
                                    z9 = z7;
                                    accountInstance2 = accountInstance;
                                    ensureMediaThumbExists(accountInstance2, z9, tL_document, sendingMediaInfo3.path, null, j7);
                                    str6 = str42;
                                }
                            }
                            if (tL_document == null) {
                                str10 = sendingMediaInfo3.thumbPath;
                                if (str10 != null) {
                                    bitmapCreateVideoThumbnailAtTime2 = BitmapFactory.decodeFile(str10);
                                } else {
                                    bitmapCreateVideoThumbnailAtTime2 = null;
                                }
                                if (bitmapCreateVideoThumbnailAtTime2 == null && (bitmapCreateVideoThumbnailAtTime2 = createVideoThumbnailAtTime(sendingMediaInfo3.path, j7)) == null) {
                                    bitmapCreateVideoThumbnailAtTime2 = createVideoThumbnail(sendingMediaInfo3.path, 1);
                                }
                                if (bitmapCreateVideoThumbnailAtTime2 != null) {
                                    if (z9 && sendingMediaInfo3.ttl == 0) {
                                        iMax2 = Math.max(bitmapCreateVideoThumbnailAtTime2.getWidth(), bitmapCreateVideoThumbnailAtTime2.getHeight());
                                    } else {
                                        iMax2 = 90;
                                    }
                                    if (videoEditedInfo == null && videoEditedInfo.isSticker) {
                                        compressFormat2 = Bitmap.CompressFormat.WEBP;
                                    } else {
                                        compressFormat2 = Bitmap.CompressFormat.JPEG;
                                    }
                                    float f3 = iMax2;
                                    if (iMax2 > 90) {
                                        i18 = 80;
                                    } else {
                                        i18 = 55;
                                    }
                                    sendingMediaInfo = sendingMediaInfo3;
                                    bitmap3 = bitmapCreateVideoThumbnailAtTime2;
                                    obj26 = "parentObject";
                                    Bitmap.CompressFormat compressFormat3 = compressFormat2;
                                    obj27 = "masks";
                                    str7 = str41;
                                    str11 = "video/mp4";
                                    obj28 = "originalPath";
                                    obj29 = "final";
                                    z11 = z9;
                                    videoEditedInfo2 = videoEditedInfo;
                                    obj30 = obj2;
                                    photoSizeScaleAndSaveImage2 = ImageLoader.scaleAndSaveImage(null, bitmap3, compressFormat3, false, f3, f3, i18, z11, 0, 0, false);
                                    if (photoSizeScaleAndSaveImage2 != null || photoSizeScaleAndSaveImage2.location == null) {
                                        i15 = 0;
                                        obj18 = obj28;
                                        obj15 = obj26;
                                        obj16 = obj27;
                                        obj19 = obj30;
                                        obj17 = obj29;
                                    } else {
                                        i15 = 0;
                                        keyForPhotoSize2 = getKeyForPhotoSize(accountInstance2, photoSizeScaleAndSaveImage2, null, true, false);
                                        obj24 = obj28;
                                        obj23 = obj26;
                                        obj22 = obj27;
                                        obj21 = obj30;
                                        obj20 = obj29;
                                    }
                                    tL_document2 = new TLRPC.TL_document();
                                    tL_document2.file_reference = new byte[i15];
                                    if (photoSizeScaleAndSaveImage2 != null) {
                                        tL_document2.thumbs.add(photoSizeScaleAndSaveImage2);
                                        tL_document2.flags |= 1;
                                    }
                                    videoEditedInfo3 = sendingMediaInfo.videoEditedInfo;
                                    if (videoEditedInfo3 == null && videoEditedInfo3.isSticker) {
                                        tL_document2.mime_type = "video/webm";
                                    } else {
                                        tL_document2.mime_type = str11;
                                    }
                                    accountInstance2.getUserConfig().saveConfig(false);
                                    videoEditedInfo4 = sendingMediaInfo.videoEditedInfo;
                                    if (videoEditedInfo4 == null && videoEditedInfo4.isSticker) {
                                        tL_document2.attributes.add(new TLRPC.TL_documentAttributeAnimated());
                                        TLRPC.TL_documentAttributeSticker tL_documentAttributeSticker = new TLRPC.TL_documentAttributeSticker();
                                        tL_documentAttributeSticker.alt = "👍";
                                        tL_documentAttributeSticker.stickerset = new TLRPC.TL_inputStickerSetEmpty();
                                        tL_document2.attributes.add(tL_documentAttributeSticker);
                                        if (photoSizeScaleAndSaveImage2 == null || (fileLocation = photoSizeScaleAndSaveImage2.location) == null) {
                                            str12 = keyForPhotoSize2;
                                        } else {
                                            str12 = String.format(Locale.US, "%d_%d@b1", Long.valueOf(fileLocation.volume_id), Integer.valueOf(photoSizeScaleAndSaveImage2.location.local_id));
                                        }
                                    } else {
                                        str12 = keyForPhotoSize2;
                                    }
                                    if (z11) {
                                        tL_documentAttributeVideo = new TLRPC.TL_documentAttributeVideo_layer159();
                                    } else {
                                        tL_documentAttributeVideo = new TLRPC.TL_documentAttributeVideo();
                                        tL_documentAttributeVideo.supports_streaming = true;
                                    }
                                    tL_document2.attributes.add(tL_documentAttributeVideo);
                                    if (videoEditedInfo2 == null && (videoEditedInfo2.needConvert() || !sendingMediaInfo.isVideo)) {
                                        if (sendingMediaInfo.isVideo && videoEditedInfo2.muted) {
                                            fillVideoAttribute(sendingMediaInfo.path, tL_documentAttributeVideo, videoEditedInfo2);
                                            videoEditedInfo2.originalWidth = tL_documentAttributeVideo.w;
                                            videoEditedInfo2.originalHeight = tL_documentAttributeVideo.h;
                                        } else {
                                            tL_documentAttributeVideo.duration = (int) (videoEditedInfo2.estimatedDuration / 1000);
                                        }
                                        int i35 = videoEditedInfo2.rotationValue;
                                        MediaController.CropState cropState = videoEditedInfo2.cropState;
                                        if (cropState != null) {
                                            i16 = cropState.transformWidth;
                                            i17 = cropState.transformHeight;
                                        } else {
                                            i16 = videoEditedInfo2.resultWidth;
                                            i17 = videoEditedInfo2.resultHeight;
                                        }
                                        if (i35 == 90 || i35 == 270) {
                                            tL_documentAttributeVideo.w = i17;
                                            tL_documentAttributeVideo.h = i16;
                                        } else {
                                            tL_documentAttributeVideo.w = i16;
                                            tL_documentAttributeVideo.h = i17;
                                        }
                                        tL_document2.size = videoEditedInfo2.estimatedSize;
                                        obj25 = obj23;
                                    } else {
                                        obj25 = obj23;
                                        if (r17.exists()) {
                                            tL_document2.size = (int) file8.length();
                                        }
                                        fillVideoAttribute(sendingMediaInfo.path, tL_documentAttributeVideo, null);
                                    }
                                    tL_document = tL_document2;
                                    obj6 = obj22;
                                    obj7 = obj25;
                                    z7 = z11;
                                    keyForPhotoSize = str12;
                                    bitmap = bitmap3;
                                    obj14 = obj24;
                                    obj13 = obj21;
                                    obj12 = obj20;
                                } else {
                                    bitmap3 = bitmapCreateVideoThumbnailAtTime2;
                                    obj15 = "parentObject";
                                    sendingMediaInfo = sendingMediaInfo3;
                                    obj16 = "masks";
                                    obj17 = "final";
                                    obj18 = r14;
                                    obj19 = obj2;
                                    str11 = r0;
                                    i15 = 0;
                                    z11 = z9;
                                    videoEditedInfo2 = videoEditedInfo;
                                    str7 = str41;
                                    photoSizeScaleAndSaveImage2 = null;
                                }
                                keyForPhotoSize2 = null;
                                obj24 = obj18;
                                obj23 = obj15;
                                obj22 = obj16;
                                obj21 = obj19;
                                obj20 = obj17;
                                tL_document2 = new TLRPC.TL_document();
                                tL_document2.file_reference = new byte[i15];
                                if (photoSizeScaleAndSaveImage2 != null) {
                                    tL_document2.thumbs.add(photoSizeScaleAndSaveImage2);
                                    tL_document2.flags |= 1;
                                }
                                videoEditedInfo3 = sendingMediaInfo.videoEditedInfo;
                                if (videoEditedInfo3 == null) {
                                    tL_document2.mime_type = str11;
                                } else {
                                    tL_document2.mime_type = str11;
                                }
                                accountInstance2.getUserConfig().saveConfig(false);
                                videoEditedInfo4 = sendingMediaInfo.videoEditedInfo;
                                if (videoEditedInfo4 == null) {
                                    str12 = keyForPhotoSize2;
                                } else {
                                    str12 = keyForPhotoSize2;
                                }
                                if (z11) {
                                    tL_documentAttributeVideo = new TLRPC.TL_documentAttributeVideo_layer159();
                                } else {
                                    tL_documentAttributeVideo = new TLRPC.TL_documentAttributeVideo();
                                    tL_documentAttributeVideo.supports_streaming = true;
                                }
                                tL_document2.attributes.add(tL_documentAttributeVideo);
                                if (videoEditedInfo2 == null) {
                                    obj25 = obj23;
                                    if (r17.exists()) {
                                        tL_document2.size = (int) file8.length();
                                    }
                                    fillVideoAttribute(sendingMediaInfo.path, tL_documentAttributeVideo, null);
                                } else {
                                    obj25 = obj23;
                                    if (r17.exists()) {
                                        tL_document2.size = (int) file8.length();
                                    }
                                    fillVideoAttribute(sendingMediaInfo.path, tL_documentAttributeVideo, null);
                                }
                                tL_document = tL_document2;
                                obj6 = obj22;
                                obj7 = obj25;
                                z7 = z11;
                                keyForPhotoSize = str12;
                                bitmap = bitmap3;
                                obj14 = obj24;
                                obj13 = obj21;
                                obj12 = obj20;
                            } else {
                                sendingMediaInfo = sendingMediaInfo3;
                                str7 = str41;
                                obj3 = "final";
                                obj4 = r14;
                                obj5 = obj2;
                                z10 = z9;
                                videoEditedInfo2 = videoEditedInfo;
                                if (tL_document.thumbs.isEmpty()) {
                                    obj6 = "masks";
                                    obj7 = "parentObject";
                                    z7 = z10;
                                    bitmap = null;
                                    keyForPhotoSize = null;
                                    obj14 = obj4;
                                    obj13 = obj5;
                                    obj12 = obj3;
                                } else {
                                    str8 = sendingMediaInfo.thumbPath;
                                    if (str8 != null) {
                                        bitmapCreateVideoThumbnailAtTime = BitmapFactory.decodeFile(str8);
                                    } else {
                                        bitmapCreateVideoThumbnailAtTime = null;
                                    }
                                    if (bitmapCreateVideoThumbnailAtTime == null && (bitmapCreateVideoThumbnailAtTime = createVideoThumbnailAtTime(sendingMediaInfo.path, j7)) == null) {
                                        bitmapCreateVideoThumbnailAtTime = createVideoThumbnail(sendingMediaInfo.path, 1);
                                    }
                                    bitmap2 = bitmapCreateVideoThumbnailAtTime;
                                    if (bitmap2 != null) {
                                        if (z10 && sendingMediaInfo.ttl == 0) {
                                            iMax = Math.max(bitmap2.getWidth(), bitmap2.getHeight());
                                        } else {
                                            iMax = 90;
                                        }
                                        if (videoEditedInfo2 == null && videoEditedInfo2.isSticker) {
                                            compressFormat = Bitmap.CompressFormat.WEBP;
                                        } else {
                                            compressFormat = Bitmap.CompressFormat.JPEG;
                                        }
                                        Bitmap.CompressFormat compressFormat4 = compressFormat;
                                        float f4 = iMax;
                                        if (iMax > 90) {
                                            i10 = 80;
                                        } else {
                                            i10 = 55;
                                        }
                                        obj10 = "masks";
                                        obj11 = "parentObject";
                                        photoSizeScaleAndSaveImage = ImageLoader.scaleAndSaveImage(null, bitmap2, compressFormat4, false, f4, f4, i10, z10, 0, 0, false);
                                        z7 = z10;
                                        if (photoSizeScaleAndSaveImage != null || photoSizeScaleAndSaveImage.location == null) {
                                            i9 = 1;
                                            keyForPhotoSize = null;
                                            obj8 = obj10;
                                            obj9 = obj11;
                                        } else {
                                            i9 = 1;
                                            keyForPhotoSize = getKeyForPhotoSize(accountInstance2, photoSizeScaleAndSaveImage, null, true, false);
                                        }
                                    } else {
                                        obj8 = "masks";
                                        obj9 = "parentObject";
                                        z7 = z10;
                                        i9 = 1;
                                        photoSizeScaleAndSaveImage = null;
                                        keyForPhotoSize = null;
                                    }
                                    if (photoSizeScaleAndSaveImage != null) {
                                        obj8 = obj10;
                                        obj9 = obj11;
                                        tL_document.thumbs.add(photoSizeScaleAndSaveImage);
                                        tL_document.flags |= i9;
                                    }
                                    obj8 = obj10;
                                    obj9 = obj11;
                                    bitmap = bitmap2;
                                    obj14 = obj4;
                                    obj6 = obj8;
                                    obj7 = obj9;
                                    obj13 = obj5;
                                    obj12 = obj3;
                                }
                            }
                            photoSizeFileToSize = ImageLoader.fileToSize(sendingMediaInfo.coverPath, false);
                            if (photoSizeFileToSize == null && sendingMediaInfo.coverPhoto != null) {
                                photoSizeFileToSize = new ImageLoader.PhotoSizeFromPhoto(sendingMediaInfo.coverPhoto);
                            }
                            final TLRPC.PhotoSize photoSize = photoSizeFileToSize;
                            if (videoEditedInfo2 != null && videoEditedInfo2.muted) {
                                size = tL_document.attributes.size();
                                i14 = 0;
                                while (true) {
                                    if (i14 < size) {
                                        if (tL_document.attributes.get(i14) instanceof TLRPC.TL_documentAttributeAnimated) {
                                            break;
                                        } else {
                                            i14++;
                                        }
                                    } else {
                                        tL_document.attributes.add(new TLRPC.TL_documentAttributeAnimated());
                                        break;
                                    }
                                }
                            }
                            if (videoEditedInfo2 != null || (!videoEditedInfo2.needConvert() && sendingMediaInfo.isVideo)) {
                                absolutePath = str7;
                            } else {
                                File file9 = new File(FileLoader.getDirectory(4), "-2147483648_" + SharedConfig.getLastLocalId() + "." + (videoEditedInfo2.isSticker ? "webm" : "mp4"));
                                SharedConfig.saveConfig();
                                absolutePath = file9.getAbsolutePath();
                            }
                            map3 = new HashMap();
                            if (str5 != null) {
                                map3.put(obj14, str5);
                            }
                            if (str6 != null) {
                                map3.put(obj7, str6);
                            }
                            if (z8 && z2) {
                                i12 = i6 + 1;
                                StringBuilder sb4 = new StringBuilder();
                                sb4.append(_UrlKt.FRAGMENT_ENCODE_SET);
                                str9 = absolutePath;
                                j8 = j6;
                                sb4.append(j8);
                                map3.put("groupId", sb4.toString());
                                if (i12 != 10) {
                                    i11 = i33;
                                    if (i11 == i7 - 1) {
                                    }
                                } else {
                                    i7 = i7;
                                    i11 = r18;
                                }
                                i7 = i7;
                                map3.put(obj12, obj13);
                                jNextLong = 0;
                            } else {
                                str9 = absolutePath;
                                j8 = j6;
                                i7 = i7;
                                i11 = r18;
                                i12 = i6;
                            }
                            if (!z7 && ((videoEditedInfo2 == null || !videoEditedInfo2.isSticker) && (arrayList2 = sendingMediaInfo.masks) != null && !arrayList2.isEmpty())) {
                                tL_document.attributes.add(new TLRPC.TL_documentAttributeHasStickers());
                                serializedData = new SerializedData((sendingMediaInfo.masks.size() * 20) + 4);
                                serializedData.writeInt32(sendingMediaInfo.masks.size());
                                for (i13 = 0; i13 < sendingMediaInfo.masks.size(); i13++) {
                                    sendingMediaInfo.masks.get(i13).serializeToStream(serializedData);
                                }
                                map3.put(obj6, Utilities.bytesToHex(serializedData.toByteArray()));
                                serializedData.cleanup();
                            }
                            final VideoEditedInfo videoEditedInfo5 = videoEditedInfo2;
                            j6 = j8;
                            final String str43 = str9;
                            i8 = i11;
                            final Bitmap bitmap4 = bitmap;
                            obj = null;
                            final TLRPC.TL_document tL_document6 = tL_document;
                            final String str44 = str6;
                            final SendingMediaInfo sendingMediaInfo4 = sendingMediaInfo;
                            final String str45 = keyForPhotoSize;
                            accountInstance = accountInstance;
                            AndroidUtilities.runOnUIThread(new Runnable() { // from class: org.telegram.messenger.SendMessagesHelper$$ExternalSyntheticLambda100
                                @Override // java.lang.Runnable
                                public final void run() {
                                    SendMessagesHelper.m3708$r8$lambda$Ba7ZedawABohTrcWU_I8SylM10(bitmap4, str45, messageObject, accountInstance, videoEditedInfo5, tL_document6, str43, map3, sendingMediaInfo4, str44, j, messageObject2, messageObject3, z3, i, i2, storyItem, replyQuote, str, i3, j2, z4, photoSize, j3, j4, messageSuggestionParams);
                                }
                            });
                            i6 = i12;
                            arrayList10 = arrayList14;
                            arrayList9 = arrayList15;
                        } else {
                            z8 = z8;
                        }
                        z9 = z7;
                        accountInstance2 = accountInstance;
                        obj2 = "1";
                        videoEditedInfo = videoEditedInfoCreateCompressionSettings;
                        tL_document = null;
                        str6 = null;
                        if (tL_document == null) {
                            str10 = sendingMediaInfo3.thumbPath;
                            if (str10 != null) {
                                bitmapCreateVideoThumbnailAtTime2 = BitmapFactory.decodeFile(str10);
                            } else {
                                bitmapCreateVideoThumbnailAtTime2 = null;
                            }
                            if (bitmapCreateVideoThumbnailAtTime2 == null) {
                                bitmapCreateVideoThumbnailAtTime2 = createVideoThumbnail(sendingMediaInfo3.path, 1);
                            }
                            if (bitmapCreateVideoThumbnailAtTime2 != null) {
                                if (z9) {
                                    iMax2 = 90;
                                } else {
                                    iMax2 = 90;
                                }
                                if (videoEditedInfo == null) {
                                    compressFormat2 = Bitmap.CompressFormat.JPEG;
                                } else {
                                    compressFormat2 = Bitmap.CompressFormat.JPEG;
                                }
                                float f5 = iMax2;
                                if (iMax2 > 90) {
                                    i18 = 80;
                                } else {
                                    i18 = 55;
                                }
                                sendingMediaInfo = sendingMediaInfo3;
                                bitmap3 = bitmapCreateVideoThumbnailAtTime2;
                                obj26 = "parentObject";
                                Bitmap.CompressFormat compressFormat5 = compressFormat2;
                                obj27 = "masks";
                                str7 = str41;
                                str11 = "video/mp4";
                                obj28 = "originalPath";
                                obj29 = "final";
                                z11 = z9;
                                videoEditedInfo2 = videoEditedInfo;
                                obj30 = obj2;
                                photoSizeScaleAndSaveImage2 = ImageLoader.scaleAndSaveImage(null, bitmap3, compressFormat5, false, f5, f5, i18, z11, 0, 0, false);
                                if (photoSizeScaleAndSaveImage2 != null) {
                                }
                                i15 = 0;
                                obj18 = obj28;
                                obj15 = obj26;
                                obj16 = obj27;
                                obj19 = obj30;
                                obj17 = obj29;
                            } else {
                                bitmap3 = bitmapCreateVideoThumbnailAtTime2;
                                obj15 = "parentObject";
                                sendingMediaInfo = sendingMediaInfo3;
                                obj16 = "masks";
                                obj17 = "final";
                                obj18 = r14;
                                obj19 = obj2;
                                str11 = r0;
                                i15 = 0;
                                z11 = z9;
                                videoEditedInfo2 = videoEditedInfo;
                                str7 = str41;
                                photoSizeScaleAndSaveImage2 = null;
                            }
                            keyForPhotoSize2 = null;
                            obj24 = obj18;
                            obj23 = obj15;
                            obj22 = obj16;
                            obj21 = obj19;
                            obj20 = obj17;
                            tL_document2 = new TLRPC.TL_document();
                            tL_document2.file_reference = new byte[i15];
                            if (photoSizeScaleAndSaveImage2 != null) {
                                tL_document2.thumbs.add(photoSizeScaleAndSaveImage2);
                                tL_document2.flags |= 1;
                            }
                            videoEditedInfo3 = sendingMediaInfo.videoEditedInfo;
                            if (videoEditedInfo3 == null) {
                                tL_document2.mime_type = str11;
                            } else {
                                tL_document2.mime_type = str11;
                            }
                            accountInstance2.getUserConfig().saveConfig(false);
                            videoEditedInfo4 = sendingMediaInfo.videoEditedInfo;
                            if (videoEditedInfo4 == null) {
                                str12 = keyForPhotoSize2;
                            } else {
                                str12 = keyForPhotoSize2;
                            }
                            if (z11) {
                                tL_documentAttributeVideo = new TLRPC.TL_documentAttributeVideo_layer159();
                            } else {
                                tL_documentAttributeVideo = new TLRPC.TL_documentAttributeVideo();
                                tL_documentAttributeVideo.supports_streaming = true;
                            }
                            tL_document2.attributes.add(tL_documentAttributeVideo);
                            if (videoEditedInfo2 == null) {
                                obj25 = obj23;
                                if (r17.exists()) {
                                    tL_document2.size = (int) file8.length();
                                }
                                fillVideoAttribute(sendingMediaInfo.path, tL_documentAttributeVideo, null);
                            } else {
                                obj25 = obj23;
                                if (r17.exists()) {
                                    tL_document2.size = (int) file8.length();
                                }
                                fillVideoAttribute(sendingMediaInfo.path, tL_documentAttributeVideo, null);
                            }
                            tL_document = tL_document2;
                            obj6 = obj22;
                            obj7 = obj25;
                            z7 = z11;
                            keyForPhotoSize = str12;
                            bitmap = bitmap3;
                            obj14 = obj24;
                            obj13 = obj21;
                            obj12 = obj20;
                        } else {
                            sendingMediaInfo = sendingMediaInfo3;
                            str7 = str41;
                            obj3 = "final";
                            obj4 = r14;
                            obj5 = obj2;
                            z10 = z9;
                            videoEditedInfo2 = videoEditedInfo;
                            if (tL_document.thumbs.isEmpty()) {
                                str8 = sendingMediaInfo.thumbPath;
                                if (str8 != null) {
                                    bitmapCreateVideoThumbnailAtTime = BitmapFactory.decodeFile(str8);
                                } else {
                                    bitmapCreateVideoThumbnailAtTime = null;
                                }
                                if (bitmapCreateVideoThumbnailAtTime == null) {
                                    bitmapCreateVideoThumbnailAtTime = createVideoThumbnail(sendingMediaInfo.path, 1);
                                }
                                bitmap2 = bitmapCreateVideoThumbnailAtTime;
                                if (bitmap2 != null) {
                                    if (z10) {
                                        iMax = 90;
                                    } else {
                                        iMax = 90;
                                    }
                                    if (videoEditedInfo2 == null) {
                                        compressFormat = Bitmap.CompressFormat.JPEG;
                                    } else {
                                        compressFormat = Bitmap.CompressFormat.JPEG;
                                    }
                                    Bitmap.CompressFormat compressFormat6 = compressFormat;
                                    float f6 = iMax;
                                    if (iMax > 90) {
                                        i10 = 80;
                                    } else {
                                        i10 = 55;
                                    }
                                    obj10 = "masks";
                                    obj11 = "parentObject";
                                    photoSizeScaleAndSaveImage = ImageLoader.scaleAndSaveImage(null, bitmap2, compressFormat6, false, f6, f6, i10, z10, 0, 0, false);
                                    z7 = z10;
                                    if (photoSizeScaleAndSaveImage != null) {
                                        i9 = 1;
                                        keyForPhotoSize = null;
                                        obj8 = obj10;
                                        obj9 = obj11;
                                    } else {
                                        i9 = 1;
                                        keyForPhotoSize = null;
                                        obj8 = obj10;
                                        obj9 = obj11;
                                    }
                                } else {
                                    obj8 = "masks";
                                    obj9 = "parentObject";
                                    z7 = z10;
                                    i9 = 1;
                                    photoSizeScaleAndSaveImage = null;
                                    keyForPhotoSize = null;
                                }
                                if (photoSizeScaleAndSaveImage != null) {
                                    obj8 = obj10;
                                    obj9 = obj11;
                                    tL_document.thumbs.add(photoSizeScaleAndSaveImage);
                                    tL_document.flags |= i9;
                                }
                                obj8 = obj10;
                                obj9 = obj11;
                                bitmap = bitmap2;
                                obj14 = obj4;
                                obj6 = obj8;
                                obj7 = obj9;
                                obj13 = obj5;
                                obj12 = obj3;
                            } else {
                                obj6 = "masks";
                                obj7 = "parentObject";
                                z7 = z10;
                                bitmap = null;
                                keyForPhotoSize = null;
                                obj14 = obj4;
                                obj13 = obj5;
                                obj12 = obj3;
                            }
                        }
                        photoSizeFileToSize = ImageLoader.fileToSize(sendingMediaInfo.coverPath, false);
                        if (photoSizeFileToSize == null) {
                            photoSizeFileToSize = new ImageLoader.PhotoSizeFromPhoto(sendingMediaInfo.coverPhoto);
                        }
                        final TLRPC.PhotoSize photoSize2 = photoSizeFileToSize;
                        if (videoEditedInfo2 != null) {
                            size = tL_document.attributes.size();
                            i14 = 0;
                            while (true) {
                                if (i14 < size) {
                                    if (tL_document.attributes.get(i14) instanceof TLRPC.TL_documentAttributeAnimated) {
                                        break;
                                        break;
                                    }
                                    i14++;
                                } else {
                                    tL_document.attributes.add(new TLRPC.TL_documentAttributeAnimated());
                                    break;
                                }
                            }
                        }
                        if (videoEditedInfo2 != null) {
                            absolutePath = str7;
                        } else {
                            absolutePath = str7;
                        }
                        map3 = new HashMap();
                        if (str5 != null) {
                            map3.put(obj14, str5);
                        }
                        if (str6 != null) {
                            map3.put(obj7, str6);
                        }
                        if (z8) {
                            str9 = absolutePath;
                            j8 = j6;
                            i7 = i7;
                            i11 = r18;
                            i12 = i6;
                        } else {
                            str9 = absolutePath;
                            j8 = j6;
                            i7 = i7;
                            i11 = r18;
                            i12 = i6;
                        }
                        if (!z7) {
                            tL_document.attributes.add(new TLRPC.TL_documentAttributeHasStickers());
                            serializedData = new SerializedData((sendingMediaInfo.masks.size() * 20) + 4);
                            serializedData.writeInt32(sendingMediaInfo.masks.size());
                            while (i13 < sendingMediaInfo.masks.size()) {
                                sendingMediaInfo.masks.get(i13).serializeToStream(serializedData);
                            }
                            map3.put(obj6, Utilities.bytesToHex(serializedData.toByteArray()));
                            serializedData.cleanup();
                        }
                        final VideoEditedInfo videoEditedInfo6 = videoEditedInfo2;
                        j6 = j8;
                        final String str46 = str9;
                        i8 = i11;
                        final Bitmap bitmap5 = bitmap;
                        obj = null;
                        final TLRPC.TL_document tL_document7 = tL_document;
                        final String str47 = str6;
                        final SendingMediaInfo sendingMediaInfo5 = sendingMediaInfo;
                        final String str48 = keyForPhotoSize;
                        accountInstance = accountInstance;
                        AndroidUtilities.runOnUIThread(new Runnable() { // from class: org.telegram.messenger.SendMessagesHelper$$ExternalSyntheticLambda100
                            @Override // java.lang.Runnable
                            public final void run() {
                                SendMessagesHelper.m3708$r8$lambda$Ba7ZedawABohTrcWU_I8SylM10(bitmap5, str48, messageObject, accountInstance, videoEditedInfo6, tL_document7, str46, map3, sendingMediaInfo5, str47, j, messageObject2, messageObject3, z3, i, i2, storyItem, replyQuote, str, i3, j2, z4, photoSize2, j3, j4, messageSuggestionParams);
                            }
                        });
                        i6 = i12;
                        arrayList10 = arrayList14;
                        arrayList9 = arrayList15;
                    } else {
                        accountInstance = accountInstance;
                        i8 = i33;
                        str3 = str3;
                        j6 = j6;
                        obj = null;
                        if (arrayList15 == null) {
                            arrayList9 = new ArrayList();
                            arrayList10 = new ArrayList();
                            arrayList12 = new ArrayList();
                            arrayList13 = new ArrayList();
                            arrayList11 = new ArrayList();
                        } else {
                            arrayList10 = arrayList14;
                            arrayList9 = arrayList15;
                        }
                        ArrayList arrayList16 = arrayList11;
                        ArrayList arrayList17 = arrayList12;
                        ArrayList arrayList18 = arrayList13;
                        arrayList9.add(sendingMediaInfo3.path);
                        arrayList10.add(sendingMediaInfo3.path);
                        arrayList16.add(sendingMediaInfo3.uri);
                        arrayList17.add(sendingMediaInfo3.caption);
                        arrayList18.add(sendingMediaInfo3.entities);
                        arrayList11 = arrayList16;
                        arrayList12 = arrayList17;
                        arrayList13 = arrayList18;
                    }
                } else {
                    String str49 = sendingMediaInfo3.path;
                    if (str49 != null || (uri2 = sendingMediaInfo3.uri) == null) {
                        string = str49;
                        strCopyFileToCache = string;
                    } else {
                        String path2 = (Build.VERSION.SDK_INT < 30 || !"content".equals(uri2.getScheme())) ? AndroidUtilities.getPath(sendingMediaInfo3.uri) : null;
                        string = sendingMediaInfo3.uri.toString();
                        strCopyFileToCache = path2;
                    }
                    if (inputContentInfoCompat == null || sendingMediaInfo3.uri == null || !inputContentInfoCompat.getDescription().hasMimeType("image/png")) {
                        str13 = str4;
                    } else {
                        try {
                            BitmapFactory.Options options = new BitmapFactory.Options();
                            inputStreamOpenInputStream = ApplicationLoader.applicationContext.getContentResolver().openInputStream(sendingMediaInfo3.uri);
                            try {
                                Bitmap bitmapDecodeStream = BitmapFactory.decodeStream(inputStreamOpenInputStream, null, options);
                                StringBuilder sb5 = new StringBuilder();
                                sb5.append("-2147483648_");
                                sb5.append(SharedConfig.getLastLocalId());
                                str13 = str4;
                                try {
                                    sb5.append(str13);
                                    File file10 = new File(FileLoader.getDirectory(4), sb5.toString());
                                    fileOutputStream = new FileOutputStream(file10);
                                    try {
                                        bitmapDecodeStream.compress(Bitmap.CompressFormat.WEBP, 100, fileOutputStream);
                                        SharedConfig.saveConfig();
                                        sendingMediaInfo3.uri = Uri.fromFile(file10);
                                        if (inputStreamOpenInputStream != null) {
                                            try {
                                                inputStreamOpenInputStream.close();
                                            } catch (Exception unused) {
                                            }
                                        }
                                    } catch (Throwable th) {
                                        th = th;
                                        try {
                                            FileLog.e(th);
                                            if (inputStreamOpenInputStream != null) {
                                                try {
                                                    inputStreamOpenInputStream.close();
                                                } catch (Exception unused2) {
                                                }
                                            }
                                            if (fileOutputStream != null) {
                                            }
                                            if (z) {
                                                str2 = str2;
                                                if (strCopyFileToCache != null) {
                                                    fileExtension = FileLoader.getFileExtension(new File(strCopyFileToCache));
                                                } else {
                                                    fileExtension = _UrlKt.FRAGMENT_ENCODE_SET;
                                                }
                                                str14 = strCopyFileToCache;
                                                z12 = true;
                                            } else {
                                                str2 = str2;
                                                if (strCopyFileToCache != null) {
                                                    fileExtension = FileLoader.getFileExtension(new File(strCopyFileToCache));
                                                } else {
                                                    fileExtension = _UrlKt.FRAGMENT_ENCODE_SET;
                                                }
                                                str14 = strCopyFileToCache;
                                                z12 = true;
                                            }
                                            if (z12) {
                                                if (arrayList15 == null) {
                                                    arrayList6 = new ArrayList();
                                                    arrayList7 = new ArrayList();
                                                    arrayList12 = new ArrayList();
                                                    arrayList13 = new ArrayList();
                                                    arrayList11 = new ArrayList();
                                                } else {
                                                    arrayList6 = arrayList15;
                                                    arrayList7 = arrayList14;
                                                }
                                                ArrayList arrayList19 = arrayList11;
                                                ArrayList arrayList20 = arrayList12;
                                                ArrayList arrayList21 = arrayList13;
                                                arrayList6.add(str14);
                                                arrayList7.add(string);
                                                arrayList19.add(sendingMediaInfo3.uri);
                                                arrayList20.add(sendingMediaInfo3.caption);
                                                arrayList21.add(sendingMediaInfo3.entities);
                                                accountInstance = accountInstance;
                                                arrayList11 = arrayList19;
                                                arrayList12 = arrayList20;
                                                arrayList13 = arrayList21;
                                                str2 = str2;
                                                str4 = str13;
                                                i8 = i33;
                                                obj = null;
                                                arrayList9 = arrayList6;
                                                arrayList10 = arrayList7;
                                                i31 = i6;
                                            } else {
                                                if (str14 != null) {
                                                    arrayList3 = arrayList15;
                                                    File file11 = new File(str14);
                                                    StringBuilder sb6 = new StringBuilder();
                                                    sb6.append(string);
                                                    sb6.append(file11.length());
                                                    str15 = str3;
                                                    sb6.append(str15);
                                                    sb6.append(file11.lastModified());
                                                    string2 = sb6.toString();
                                                } else {
                                                    arrayList3 = arrayList15;
                                                    str15 = str3;
                                                    string2 = null;
                                                }
                                                map2 = map2;
                                                if (map2 != null) {
                                                    mediaSendPrepareWorker = (MediaSendPrepareWorker) map2.get(sendingMediaInfo3);
                                                    tL_photo6 = mediaSendPrepareWorker.photo;
                                                    str21 = mediaSendPrepareWorker.parentObject;
                                                    if (tL_photo6 == null) {
                                                        try {
                                                            mediaSendPrepareWorker.sync.await();
                                                        } catch (Exception e4) {
                                                            FileLog.e(e4);
                                                        }
                                                        tL_photo6 = mediaSendPrepareWorker.photo;
                                                        str21 = mediaSendPrepareWorker.parentObject;
                                                    }
                                                    str14 = str14;
                                                    obj34 = "groupId";
                                                    str16 = str15;
                                                    map2 = map2;
                                                    str30 = "1";
                                                    str2 = str2;
                                                    str13 = str13;
                                                    str31 = "final";
                                                    arrayList3 = arrayList3;
                                                    i19 = 1;
                                                    accountInstance3 = accountInstance;
                                                    string2 = string2;
                                                    obj33 = "originalPath";
                                                    str18 = str21;
                                                    tL_photo2 = tL_photo6;
                                                } else {
                                                    if (z7) {
                                                        obj31 = "groupId";
                                                        str16 = str15;
                                                        i19 = 1;
                                                        accountInstance3 = accountInstance;
                                                        obj32 = "originalPath";
                                                        str17 = null;
                                                        tL_photo = null;
                                                    } else {
                                                        obj31 = "groupId";
                                                        str16 = str15;
                                                        i19 = 1;
                                                        accountInstance3 = accountInstance;
                                                        obj32 = "originalPath";
                                                        str17 = null;
                                                        tL_photo = null;
                                                    }
                                                    if (tL_photo == null) {
                                                        TLRPC.TL_photo tL_photoGeneratePhotoSizes = accountInstance3.getSendMessagesHelper().generatePhotoSizes(sendingMediaInfo3.path, sendingMediaInfo3.uri);
                                                        if (z7) {
                                                            new File(sendingMediaInfo3.path).delete();
                                                        }
                                                        tL_photo2 = tL_photoGeneratePhotoSizes;
                                                        str18 = str17;
                                                        obj34 = obj31;
                                                        obj33 = obj32;
                                                    } else {
                                                        str18 = str17;
                                                        tL_photo2 = tL_photo;
                                                    }
                                                }
                                                if (tL_photo2 != null) {
                                                    obj34 = obj31;
                                                    obj33 = obj32;
                                                    map4 = new HashMap();
                                                    bitmapArr = new Bitmap[i19];
                                                    strArr = new String[i19];
                                                    arrayList5 = sendingMediaInfo3.masks;
                                                    if (arrayList5 != null) {
                                                        r0 = 0;
                                                    } else {
                                                        r0 = 0;
                                                    }
                                                    tL_photo2.has_stickers = r0;
                                                    if (r0 != 0) {
                                                        serializedData2 = new SerializedData((sendingMediaInfo3.masks.size() * 20) + 4);
                                                        serializedData2.writeInt32(sendingMediaInfo3.masks.size());
                                                        for (i22 = 0; i22 < sendingMediaInfo3.masks.size(); i22++) {
                                                            sendingMediaInfo3.masks.get(i22).serializeToStream(serializedData2);
                                                        }
                                                        map4.put("masks", Utilities.bytesToHex(serializedData2.toByteArray()));
                                                        serializedData2.cleanup();
                                                    }
                                                    if (string2 != null) {
                                                        map4.put(obj33, string2);
                                                    }
                                                    if (str18 != null) {
                                                        map4.put("parentObject", str18);
                                                    }
                                                    try {
                                                        if (z2) {
                                                            try {
                                                                z13 = true;
                                                                if (arrayList.size() == 1) {
                                                                }
                                                            } catch (Exception e5) {
                                                                e = e5;
                                                                z13 = true;
                                                                FileLog.e(e);
                                                                if (z2) {
                                                                    i31 = i6 + 1;
                                                                    StringBuilder sb7 = new StringBuilder();
                                                                    sb7.append(_UrlKt.FRAGMENT_ENCODE_SET);
                                                                    j9 = j6;
                                                                    sb7.append(j9);
                                                                    map4.put(obj34, sb7.toString());
                                                                    if (i31 != 10) {
                                                                        i21 = i33;
                                                                        if (i21 == i7 - 1) {
                                                                        }
                                                                    } else {
                                                                        i21 = i33;
                                                                    }
                                                                    map4.put(str31, str30);
                                                                    jNextLong = 0;
                                                                } else {
                                                                    i21 = i33;
                                                                    j9 = j6;
                                                                    i31 = i6;
                                                                }
                                                                final boolean z17 = sendingMediaInfo3.highQuality;
                                                                i8 = i21;
                                                                final String str50 = str18;
                                                                str4 = str13;
                                                                AndroidUtilities.runOnUIThread(new Runnable() { // from class: org.telegram.messenger.SendMessagesHelper$$ExternalSyntheticLambda101
                                                                    @Override // java.lang.Runnable
                                                                    public final void run() {
                                                                        SendMessagesHelper.$r8$lambda$9xGUoyyhNB9hDrYLEcLGjcuNz1I(bitmapArr, strArr, messageObject, accountInstance, tL_photo2, map4, sendingMediaInfo3, str50, j, messageObject2, messageObject3, z3, i, i2, z5, storyItem, replyQuote, str, i3, j2, z4, j3, j4, messageSuggestionParams, z17);
                                                                    }
                                                                });
                                                                accountInstance = accountInstance;
                                                                arrayList10 = arrayList14;
                                                                arrayList9 = arrayList3;
                                                                str3 = str16;
                                                                j6 = j9;
                                                                obj = null;
                                                                i32 = i8 + 1;
                                                                arrayList8 = arrayList;
                                                                zIsEncryptedDialog = z7;
                                                                map = map2;
                                                                str29 = str4;
                                                                str26 = str2;
                                                                str27 = str3;
                                                                size2 = i7;
                                                                j11 = j6;
                                                                i4 = 1;
                                                                i5 = 0;
                                                            }
                                                            if (z2) {
                                                                i31 = i6 + 1;
                                                                StringBuilder sb8 = new StringBuilder();
                                                                sb8.append(_UrlKt.FRAGMENT_ENCODE_SET);
                                                                j9 = j6;
                                                                sb8.append(j9);
                                                                map4.put(obj34, sb8.toString());
                                                                if (i31 != 10) {
                                                                    i21 = i33;
                                                                    if (i21 == i7 - 1) {
                                                                    }
                                                                } else {
                                                                    i21 = i33;
                                                                }
                                                                map4.put(str31, str30);
                                                                jNextLong = 0;
                                                            } else {
                                                                i21 = i33;
                                                                j9 = j6;
                                                                i31 = i6;
                                                            }
                                                            final boolean z18 = sendingMediaInfo3.highQuality;
                                                            i8 = i21;
                                                            final String str51 = str18;
                                                            str4 = str13;
                                                            AndroidUtilities.runOnUIThread(new Runnable() { // from class: org.telegram.messenger.SendMessagesHelper$$ExternalSyntheticLambda101
                                                                @Override // java.lang.Runnable
                                                                public final void run() {
                                                                    SendMessagesHelper.$r8$lambda$9xGUoyyhNB9hDrYLEcLGjcuNz1I(bitmapArr, strArr, messageObject, accountInstance, tL_photo2, map4, sendingMediaInfo3, str51, j, messageObject2, messageObject3, z3, i, i2, z5, storyItem, replyQuote, str, i3, j2, z4, j3, j4, messageSuggestionParams, z18);
                                                                }
                                                            });
                                                            accountInstance = accountInstance;
                                                            arrayList10 = arrayList14;
                                                            arrayList9 = arrayList3;
                                                            str3 = str16;
                                                            j6 = j9;
                                                            obj = null;
                                                        } else {
                                                            z13 = true;
                                                        }
                                                        closestPhotoSizeWithSize = FileLoader.getClosestPhotoSizeWithSize(tL_photo2.sizes, AndroidUtilities.getPhotoSize(sendingMediaInfo3.highQuality));
                                                        if (closestPhotoSizeWithSize != null) {
                                                            strArr[0] = getKeyForPhotoSize(accountInstance3, closestPhotoSizeWithSize, bitmapArr, false, false);
                                                        }
                                                    } catch (Exception e6) {
                                                        e = e6;
                                                        FileLog.e(e);
                                                    }
                                                    if (z2) {
                                                        i31 = i6 + 1;
                                                        StringBuilder sb9 = new StringBuilder();
                                                        sb9.append(_UrlKt.FRAGMENT_ENCODE_SET);
                                                        j9 = j6;
                                                        sb9.append(j9);
                                                        map4.put(obj34, sb9.toString());
                                                        if (i31 != 10) {
                                                            i21 = i33;
                                                            if (i21 == i7 - 1) {
                                                            }
                                                        } else {
                                                            i21 = i33;
                                                        }
                                                        map4.put(str31, str30);
                                                        jNextLong = 0;
                                                    } else {
                                                        i21 = i33;
                                                        j9 = j6;
                                                        i31 = i6;
                                                    }
                                                    final boolean z19 = sendingMediaInfo3.highQuality;
                                                    i8 = i21;
                                                    final String str52 = str18;
                                                    str4 = str13;
                                                    AndroidUtilities.runOnUIThread(new Runnable() { // from class: org.telegram.messenger.SendMessagesHelper$$ExternalSyntheticLambda101
                                                        @Override // java.lang.Runnable
                                                        public final void run() {
                                                            SendMessagesHelper.$r8$lambda$9xGUoyyhNB9hDrYLEcLGjcuNz1I(bitmapArr, strArr, messageObject, accountInstance, tL_photo2, map4, sendingMediaInfo3, str52, j, messageObject2, messageObject3, z3, i, i2, z5, storyItem, replyQuote, str, i3, j2, z4, j3, j4, messageSuggestionParams, z19);
                                                        }
                                                    });
                                                    accountInstance = accountInstance;
                                                    arrayList10 = arrayList14;
                                                    arrayList9 = arrayList3;
                                                    str3 = str16;
                                                    j6 = j9;
                                                    obj = null;
                                                } else {
                                                    obj34 = obj31;
                                                    obj33 = obj32;
                                                    str4 = str13;
                                                    i8 = i33;
                                                    if (arrayList3 == null) {
                                                        arrayList4 = new ArrayList();
                                                        arrayList10 = new ArrayList();
                                                        arrayList12 = new ArrayList();
                                                        arrayList13 = new ArrayList();
                                                        arrayList11 = new ArrayList();
                                                    } else {
                                                        arrayList10 = arrayList14;
                                                        arrayList4 = arrayList3;
                                                    }
                                                    ArrayList arrayList22 = arrayList11;
                                                    ArrayList arrayList23 = arrayList12;
                                                    ArrayList arrayList24 = arrayList13;
                                                    arrayList4.add(str14);
                                                    arrayList10.add(string2);
                                                    arrayList22.add(sendingMediaInfo3.uri);
                                                    arrayList23.add(sendingMediaInfo3.caption);
                                                    arrayList24.add(sendingMediaInfo3.entities);
                                                    arrayList9 = arrayList4;
                                                    arrayList11 = arrayList22;
                                                    arrayList12 = arrayList23;
                                                    arrayList13 = arrayList24;
                                                    i31 = i6;
                                                    str3 = str16;
                                                    j6 = j6;
                                                    obj = null;
                                                    accountInstance = accountInstance;
                                                }
                                            }
                                            i32 = i8 + 1;
                                            arrayList8 = arrayList;
                                            zIsEncryptedDialog = z7;
                                            map = map2;
                                            str29 = str4;
                                            str26 = str2;
                                            str27 = str3;
                                            size2 = i7;
                                            j11 = j6;
                                            i4 = 1;
                                            i5 = 0;
                                        } catch (Throwable th2) {
                                            if (inputStreamOpenInputStream != null) {
                                                try {
                                                    inputStreamOpenInputStream.close();
                                                } catch (Exception unused3) {
                                                }
                                            }
                                            if (fileOutputStream != null) {
                                                try {
                                                    fileOutputStream.close();
                                                    throw th2;
                                                } catch (Exception unused4) {
                                                    throw th2;
                                                }
                                            }
                                            throw th2;
                                        }
                                    }
                                } catch (Throwable th3) {
                                    th = th3;
                                    fileOutputStream = null;
                                    FileLog.e(th);
                                    if (inputStreamOpenInputStream != null) {
                                        inputStreamOpenInputStream.close();
                                    }
                                    if (fileOutputStream != null) {
                                        fileOutputStream.close();
                                    }
                                    if (z) {
                                        str2 = str2;
                                        if (strCopyFileToCache != null) {
                                            fileExtension = FileLoader.getFileExtension(new File(strCopyFileToCache));
                                        } else {
                                            fileExtension = _UrlKt.FRAGMENT_ENCODE_SET;
                                        }
                                        str14 = strCopyFileToCache;
                                        z12 = true;
                                    } else {
                                        str2 = str2;
                                        if (strCopyFileToCache != null) {
                                            fileExtension = FileLoader.getFileExtension(new File(strCopyFileToCache));
                                        } else {
                                            fileExtension = _UrlKt.FRAGMENT_ENCODE_SET;
                                        }
                                        str14 = strCopyFileToCache;
                                        z12 = true;
                                    }
                                    if (z12) {
                                        if (arrayList15 == null) {
                                            arrayList6 = new ArrayList();
                                            arrayList7 = new ArrayList();
                                            arrayList12 = new ArrayList();
                                            arrayList13 = new ArrayList();
                                            arrayList11 = new ArrayList();
                                        } else {
                                            arrayList6 = arrayList15;
                                            arrayList7 = arrayList14;
                                        }
                                        ArrayList arrayList110 = arrayList11;
                                        ArrayList arrayList25 = arrayList12;
                                        ArrayList arrayList26 = arrayList13;
                                        arrayList6.add(str14);
                                        arrayList7.add(string);
                                        arrayList110.add(sendingMediaInfo3.uri);
                                        arrayList25.add(sendingMediaInfo3.caption);
                                        arrayList26.add(sendingMediaInfo3.entities);
                                        accountInstance = accountInstance;
                                        arrayList11 = arrayList110;
                                        arrayList12 = arrayList25;
                                        arrayList13 = arrayList26;
                                        str2 = str2;
                                        str4 = str13;
                                        i8 = i33;
                                        obj = null;
                                        arrayList9 = arrayList6;
                                        arrayList10 = arrayList7;
                                        i31 = i6;
                                    } else {
                                        if (str14 != null) {
                                            arrayList3 = arrayList15;
                                            File file12 = new File(str14);
                                            StringBuilder sb10 = new StringBuilder();
                                            sb10.append(string);
                                            sb10.append(file12.length());
                                            str15 = str3;
                                            sb10.append(str15);
                                            sb10.append(file12.lastModified());
                                            string2 = sb10.toString();
                                        } else {
                                            arrayList3 = arrayList15;
                                            str15 = str3;
                                            string2 = null;
                                        }
                                        map2 = map2;
                                        if (map2 != null) {
                                            mediaSendPrepareWorker = (MediaSendPrepareWorker) map2.get(sendingMediaInfo3);
                                            tL_photo6 = mediaSendPrepareWorker.photo;
                                            str21 = mediaSendPrepareWorker.parentObject;
                                            if (tL_photo6 == null) {
                                                mediaSendPrepareWorker.sync.await();
                                                tL_photo6 = mediaSendPrepareWorker.photo;
                                                str21 = mediaSendPrepareWorker.parentObject;
                                            }
                                            str14 = str14;
                                            obj34 = "groupId";
                                            str16 = str15;
                                            map2 = map2;
                                            str30 = "1";
                                            str2 = str2;
                                            str13 = str13;
                                            str31 = "final";
                                            arrayList3 = arrayList3;
                                            i19 = 1;
                                            accountInstance3 = accountInstance;
                                            string2 = string2;
                                            obj33 = "originalPath";
                                            str18 = str21;
                                            tL_photo2 = tL_photo6;
                                        } else {
                                            if (z7) {
                                                obj31 = "groupId";
                                                str16 = str15;
                                                i19 = 1;
                                                accountInstance3 = accountInstance;
                                                obj32 = "originalPath";
                                                str17 = null;
                                                tL_photo = null;
                                            } else {
                                                obj31 = "groupId";
                                                str16 = str15;
                                                i19 = 1;
                                                accountInstance3 = accountInstance;
                                                obj32 = "originalPath";
                                                str17 = null;
                                                tL_photo = null;
                                            }
                                            if (tL_photo == null) {
                                                TLRPC.TL_photo tL_photoGeneratePhotoSizes2 = accountInstance3.getSendMessagesHelper().generatePhotoSizes(sendingMediaInfo3.path, sendingMediaInfo3.uri);
                                                if (z7) {
                                                    new File(sendingMediaInfo3.path).delete();
                                                }
                                                tL_photo2 = tL_photoGeneratePhotoSizes2;
                                                str18 = str17;
                                                obj34 = obj31;
                                                obj33 = obj32;
                                            } else {
                                                str18 = str17;
                                                tL_photo2 = tL_photo;
                                            }
                                        }
                                        if (tL_photo2 != null) {
                                            obj34 = obj31;
                                            obj33 = obj32;
                                            map4 = new HashMap();
                                            bitmapArr = new Bitmap[i19];
                                            strArr = new String[i19];
                                            arrayList5 = sendingMediaInfo3.masks;
                                            if (arrayList5 != null) {
                                                r0 = 0;
                                            } else {
                                                r0 = 0;
                                            }
                                            tL_photo2.has_stickers = r0;
                                            if (r0 != 0) {
                                                serializedData2 = new SerializedData((sendingMediaInfo3.masks.size() * 20) + 4);
                                                serializedData2.writeInt32(sendingMediaInfo3.masks.size());
                                                while (i22 < sendingMediaInfo3.masks.size()) {
                                                    sendingMediaInfo3.masks.get(i22).serializeToStream(serializedData2);
                                                }
                                                map4.put("masks", Utilities.bytesToHex(serializedData2.toByteArray()));
                                                serializedData2.cleanup();
                                            }
                                            if (string2 != null) {
                                                map4.put(obj33, string2);
                                            }
                                            if (str18 != null) {
                                                map4.put("parentObject", str18);
                                            }
                                            if (z2) {
                                                z13 = true;
                                                if (arrayList.size() == 1) {
                                                }
                                                if (z2) {
                                                    i31 = i6 + 1;
                                                    StringBuilder sb11 = new StringBuilder();
                                                    sb11.append(_UrlKt.FRAGMENT_ENCODE_SET);
                                                    j9 = j6;
                                                    sb11.append(j9);
                                                    map4.put(obj34, sb11.toString());
                                                    if (i31 != 10) {
                                                        i21 = i33;
                                                        if (i21 == i7 - 1) {
                                                        }
                                                    } else {
                                                        i21 = i33;
                                                    }
                                                    map4.put(str31, str30);
                                                    jNextLong = 0;
                                                } else {
                                                    i21 = i33;
                                                    j9 = j6;
                                                    i31 = i6;
                                                }
                                                final boolean z110 = sendingMediaInfo3.highQuality;
                                                i8 = i21;
                                                final String str53 = str18;
                                                str4 = str13;
                                                AndroidUtilities.runOnUIThread(new Runnable() { // from class: org.telegram.messenger.SendMessagesHelper$$ExternalSyntheticLambda101
                                                    @Override // java.lang.Runnable
                                                    public final void run() {
                                                        SendMessagesHelper.$r8$lambda$9xGUoyyhNB9hDrYLEcLGjcuNz1I(bitmapArr, strArr, messageObject, accountInstance, tL_photo2, map4, sendingMediaInfo3, str53, j, messageObject2, messageObject3, z3, i, i2, z5, storyItem, replyQuote, str, i3, j2, z4, j3, j4, messageSuggestionParams, z110);
                                                    }
                                                });
                                                accountInstance = accountInstance;
                                                arrayList10 = arrayList14;
                                                arrayList9 = arrayList3;
                                                str3 = str16;
                                                j6 = j9;
                                                obj = null;
                                            } else {
                                                z13 = true;
                                            }
                                            closestPhotoSizeWithSize = FileLoader.getClosestPhotoSizeWithSize(tL_photo2.sizes, AndroidUtilities.getPhotoSize(sendingMediaInfo3.highQuality));
                                            if (closestPhotoSizeWithSize != null) {
                                                strArr[0] = getKeyForPhotoSize(accountInstance3, closestPhotoSizeWithSize, bitmapArr, false, false);
                                            }
                                            if (z2) {
                                                i31 = i6 + 1;
                                                StringBuilder sb12 = new StringBuilder();
                                                sb12.append(_UrlKt.FRAGMENT_ENCODE_SET);
                                                j9 = j6;
                                                sb12.append(j9);
                                                map4.put(obj34, sb12.toString());
                                                if (i31 != 10) {
                                                    i21 = i33;
                                                    if (i21 == i7 - 1) {
                                                    }
                                                } else {
                                                    i21 = i33;
                                                }
                                                map4.put(str31, str30);
                                                jNextLong = 0;
                                            } else {
                                                i21 = i33;
                                                j9 = j6;
                                                i31 = i6;
                                            }
                                            final boolean z111 = sendingMediaInfo3.highQuality;
                                            i8 = i21;
                                            final String str54 = str18;
                                            str4 = str13;
                                            AndroidUtilities.runOnUIThread(new Runnable() { // from class: org.telegram.messenger.SendMessagesHelper$$ExternalSyntheticLambda101
                                                @Override // java.lang.Runnable
                                                public final void run() {
                                                    SendMessagesHelper.$r8$lambda$9xGUoyyhNB9hDrYLEcLGjcuNz1I(bitmapArr, strArr, messageObject, accountInstance, tL_photo2, map4, sendingMediaInfo3, str54, j, messageObject2, messageObject3, z3, i, i2, z5, storyItem, replyQuote, str, i3, j2, z4, j3, j4, messageSuggestionParams, z111);
                                                }
                                            });
                                            accountInstance = accountInstance;
                                            arrayList10 = arrayList14;
                                            arrayList9 = arrayList3;
                                            str3 = str16;
                                            j6 = j9;
                                            obj = null;
                                        } else {
                                            obj34 = obj31;
                                            obj33 = obj32;
                                            str4 = str13;
                                            i8 = i33;
                                            if (arrayList3 == null) {
                                                arrayList4 = new ArrayList();
                                                arrayList10 = new ArrayList();
                                                arrayList12 = new ArrayList();
                                                arrayList13 = new ArrayList();
                                                arrayList11 = new ArrayList();
                                            } else {
                                                arrayList10 = arrayList14;
                                                arrayList4 = arrayList3;
                                            }
                                            ArrayList arrayList27 = arrayList11;
                                            ArrayList arrayList28 = arrayList12;
                                            ArrayList arrayList29 = arrayList13;
                                            arrayList4.add(str14);
                                            arrayList10.add(string2);
                                            arrayList27.add(sendingMediaInfo3.uri);
                                            arrayList28.add(sendingMediaInfo3.caption);
                                            arrayList29.add(sendingMediaInfo3.entities);
                                            arrayList9 = arrayList4;
                                            arrayList11 = arrayList27;
                                            arrayList12 = arrayList28;
                                            arrayList13 = arrayList29;
                                            i31 = i6;
                                            str3 = str16;
                                            j6 = j6;
                                            obj = null;
                                            accountInstance = accountInstance;
                                        }
                                    }
                                    i32 = i8 + 1;
                                    arrayList8 = arrayList;
                                    zIsEncryptedDialog = z7;
                                    map = map2;
                                    str29 = str4;
                                    str26 = str2;
                                    str27 = str3;
                                    size2 = i7;
                                    j11 = j6;
                                    i4 = 1;
                                    i5 = 0;
                                }
                            } catch (Throwable th4) {
                                th = th4;
                                str13 = str4;
                            }
                        } catch (Throwable th5) {
                            th = th5;
                            str13 = str4;
                            fileOutputStream = null;
                            inputStreamOpenInputStream = null;
                        }
                        try {
                            fileOutputStream.close();
                        } catch (Exception unused5) {
                        }
                    }
                    if (z || ImageLoader.shouldSendImageAsDocument(sendingMediaInfo3.path, sendingMediaInfo3.uri)) {
                        str2 = str2;
                        if (strCopyFileToCache != null) {
                            fileExtension = FileLoader.getFileExtension(new File(strCopyFileToCache));
                        } else {
                            fileExtension = _UrlKt.FRAGMENT_ENCODE_SET;
                        }
                    } else {
                        if (sendingMediaInfo3.forceImage || strCopyFileToCache == null) {
                            str2 = str2;
                        } else {
                            str2 = str2;
                            if ((strCopyFileToCache.endsWith(str2) || strCopyFileToCache.endsWith(str13)) && sendingMediaInfo3.ttl <= 0) {
                                fileExtension = strCopyFileToCache.endsWith(str2) ? "gif" : "webp";
                            }
                            if (z12) {
                                if (arrayList15 == null) {
                                    arrayList6 = new ArrayList();
                                    arrayList7 = new ArrayList();
                                    arrayList12 = new ArrayList();
                                    arrayList13 = new ArrayList();
                                    arrayList11 = new ArrayList();
                                } else {
                                    arrayList6 = arrayList15;
                                    arrayList7 = arrayList14;
                                }
                                ArrayList arrayList111 = arrayList11;
                                ArrayList arrayList210 = arrayList12;
                                ArrayList arrayList211 = arrayList13;
                                arrayList6.add(str14);
                                arrayList7.add(string);
                                arrayList111.add(sendingMediaInfo3.uri);
                                arrayList210.add(sendingMediaInfo3.caption);
                                arrayList211.add(sendingMediaInfo3.entities);
                                accountInstance = accountInstance;
                                arrayList11 = arrayList111;
                                arrayList12 = arrayList210;
                                arrayList13 = arrayList211;
                                str2 = str2;
                                str4 = str13;
                                i8 = i33;
                                obj = null;
                                arrayList9 = arrayList6;
                                arrayList10 = arrayList7;
                            } else {
                                if (str14 != null) {
                                    arrayList3 = arrayList15;
                                    File file13 = new File(str14);
                                    StringBuilder sb13 = new StringBuilder();
                                    sb13.append(string);
                                    sb13.append(file13.length());
                                    str15 = str3;
                                    sb13.append(str15);
                                    sb13.append(file13.lastModified());
                                    string2 = sb13.toString();
                                } else {
                                    arrayList3 = arrayList15;
                                    str15 = str3;
                                    string2 = null;
                                }
                                map2 = map2;
                                if (map2 != null) {
                                    mediaSendPrepareWorker = (MediaSendPrepareWorker) map2.get(sendingMediaInfo3);
                                    tL_photo6 = mediaSendPrepareWorker.photo;
                                    str21 = mediaSendPrepareWorker.parentObject;
                                    if (tL_photo6 == null) {
                                        mediaSendPrepareWorker.sync.await();
                                        tL_photo6 = mediaSendPrepareWorker.photo;
                                        str21 = mediaSendPrepareWorker.parentObject;
                                    }
                                    str14 = str14;
                                    obj34 = "groupId";
                                    str16 = str15;
                                    map2 = map2;
                                    str30 = "1";
                                    str2 = str2;
                                    str13 = str13;
                                    str31 = "final";
                                    arrayList3 = arrayList3;
                                    i19 = 1;
                                    accountInstance3 = accountInstance;
                                    string2 = string2;
                                    obj33 = "originalPath";
                                    str18 = str21;
                                    tL_photo2 = tL_photo6;
                                } else {
                                    if (z7 || sendingMediaInfo3.ttl != 0) {
                                        obj31 = "groupId";
                                        str16 = str15;
                                        i19 = 1;
                                        accountInstance3 = accountInstance;
                                        obj32 = "originalPath";
                                        str17 = null;
                                        tL_photo = null;
                                    } else {
                                        int i36 = sendingMediaInfo3.highQuality ? !z7 ? 6 : 7 : !z7 ? 0 : 3;
                                        Object[] sentFile4 = accountInstance.getMessagesStorage().getSentFile(string2, i36);
                                        if (sentFile4 != null) {
                                            Object obj41 = sentFile4[0];
                                            str19 = str15;
                                            if (obj41 instanceof TLRPC.TL_photo) {
                                                tL_photo3 = (TLRPC.TL_photo) obj41;
                                                i20 = 1;
                                                str20 = (String) sentFile4[1];
                                            }
                                            if (tL_photo3 == null || sendingMediaInfo3.uri == null) {
                                                tL_photo4 = tL_photo3;
                                            } else {
                                                tL_photo4 = tL_photo3;
                                                Object[] sentFile5 = accountInstance.getMessagesStorage().getSentFile(AndroidUtilities.getPath(sendingMediaInfo3.uri), i36);
                                                if (sentFile5 != null) {
                                                    Object obj42 = sentFile5[0];
                                                    if (obj42 instanceof TLRPC.TL_photo) {
                                                        tL_photo5 = (TLRPC.TL_photo) obj42;
                                                        str17 = (String) sentFile5[i20];
                                                    }
                                                }
                                                obj31 = "groupId";
                                                obj32 = "originalPath";
                                                str16 = str19;
                                                tL_photo = tL_photo5;
                                                i19 = i20;
                                                accountInstance3 = accountInstance;
                                                ensureMediaThumbExists(accountInstance3, z7, tL_photo, sendingMediaInfo3.path, sendingMediaInfo3.uri, 0L);
                                            }
                                            str17 = str20;
                                            tL_photo5 = tL_photo4;
                                            obj31 = "groupId";
                                            obj32 = "originalPath";
                                            str16 = str19;
                                            tL_photo = tL_photo5;
                                            i19 = i20;
                                            accountInstance3 = accountInstance;
                                            ensureMediaThumbExists(accountInstance3, z7, tL_photo, sendingMediaInfo3.path, sendingMediaInfo3.uri, 0L);
                                        } else {
                                            str19 = str15;
                                        }
                                        i20 = 1;
                                        tL_photo3 = null;
                                        str20 = null;
                                        if (tL_photo3 == null) {
                                            tL_photo4 = tL_photo3;
                                            str17 = str20;
                                            tL_photo5 = tL_photo4;
                                        } else {
                                            tL_photo4 = tL_photo3;
                                            str17 = str20;
                                            tL_photo5 = tL_photo4;
                                        }
                                        obj31 = "groupId";
                                        obj32 = "originalPath";
                                        str16 = str19;
                                        tL_photo = tL_photo5;
                                        i19 = i20;
                                        accountInstance3 = accountInstance;
                                        ensureMediaThumbExists(accountInstance3, z7, tL_photo, sendingMediaInfo3.path, sendingMediaInfo3.uri, 0L);
                                    }
                                    if (tL_photo == null) {
                                        TLRPC.TL_photo tL_photoGeneratePhotoSizes3 = accountInstance3.getSendMessagesHelper().generatePhotoSizes(sendingMediaInfo3.path, sendingMediaInfo3.uri);
                                        if (z7 && sendingMediaInfo3.canDeleteAfter) {
                                            new File(sendingMediaInfo3.path).delete();
                                        }
                                        tL_photo2 = tL_photoGeneratePhotoSizes3;
                                        str18 = str17;
                                        obj34 = obj31;
                                        obj33 = obj32;
                                    } else {
                                        str18 = str17;
                                        tL_photo2 = tL_photo;
                                    }
                                }
                                if (tL_photo2 != null) {
                                    obj34 = obj31;
                                    obj33 = obj32;
                                    map4 = new HashMap();
                                    bitmapArr = new Bitmap[i19];
                                    strArr = new String[i19];
                                    arrayList5 = sendingMediaInfo3.masks;
                                    if (arrayList5 != null || arrayList5.isEmpty()) {
                                        r0 = 0;
                                    } else {
                                        r0 = i19;
                                    }
                                    tL_photo2.has_stickers = r0;
                                    if (r0 != 0) {
                                        serializedData2 = new SerializedData((sendingMediaInfo3.masks.size() * 20) + 4);
                                        serializedData2.writeInt32(sendingMediaInfo3.masks.size());
                                        while (i22 < sendingMediaInfo3.masks.size()) {
                                            sendingMediaInfo3.masks.get(i22).serializeToStream(serializedData2);
                                        }
                                        map4.put("masks", Utilities.bytesToHex(serializedData2.toByteArray()));
                                        serializedData2.cleanup();
                                    }
                                    if (string2 != null) {
                                        map4.put(obj33, string2);
                                    }
                                    if (str18 != null) {
                                        map4.put("parentObject", str18);
                                    }
                                    if (z2) {
                                        z13 = true;
                                        if (arrayList.size() == 1) {
                                        }
                                        if (z2) {
                                            i31 = i6 + 1;
                                            StringBuilder sb14 = new StringBuilder();
                                            sb14.append(_UrlKt.FRAGMENT_ENCODE_SET);
                                            j9 = j6;
                                            sb14.append(j9);
                                            map4.put(obj34, sb14.toString());
                                            if (i31 != 10) {
                                                i21 = i33;
                                                if (i21 == i7 - 1) {
                                                }
                                            } else {
                                                i21 = i33;
                                            }
                                            map4.put(str31, str30);
                                            jNextLong = 0;
                                        } else {
                                            i21 = i33;
                                            j9 = j6;
                                            i31 = i6;
                                        }
                                        final boolean z112 = sendingMediaInfo3.highQuality;
                                        i8 = i21;
                                        final String str55 = str18;
                                        str4 = str13;
                                        AndroidUtilities.runOnUIThread(new Runnable() { // from class: org.telegram.messenger.SendMessagesHelper$$ExternalSyntheticLambda101
                                            @Override // java.lang.Runnable
                                            public final void run() {
                                                SendMessagesHelper.$r8$lambda$9xGUoyyhNB9hDrYLEcLGjcuNz1I(bitmapArr, strArr, messageObject, accountInstance, tL_photo2, map4, sendingMediaInfo3, str55, j, messageObject2, messageObject3, z3, i, i2, z5, storyItem, replyQuote, str, i3, j2, z4, j3, j4, messageSuggestionParams, z112);
                                            }
                                        });
                                        accountInstance = accountInstance;
                                        arrayList10 = arrayList14;
                                        arrayList9 = arrayList3;
                                        str3 = str16;
                                        j6 = j9;
                                        obj = null;
                                    } else {
                                        z13 = true;
                                    }
                                    closestPhotoSizeWithSize = FileLoader.getClosestPhotoSizeWithSize(tL_photo2.sizes, AndroidUtilities.getPhotoSize(sendingMediaInfo3.highQuality));
                                    if (closestPhotoSizeWithSize != null) {
                                        strArr[0] = getKeyForPhotoSize(accountInstance3, closestPhotoSizeWithSize, bitmapArr, false, false);
                                    }
                                    if (z2) {
                                        i31 = i6 + 1;
                                        StringBuilder sb15 = new StringBuilder();
                                        sb15.append(_UrlKt.FRAGMENT_ENCODE_SET);
                                        j9 = j6;
                                        sb15.append(j9);
                                        map4.put(obj34, sb15.toString());
                                        if (i31 != 10) {
                                            i21 = i33;
                                            if (i21 == i7 - 1) {
                                            }
                                        } else {
                                            i21 = i33;
                                        }
                                        map4.put(str31, str30);
                                        jNextLong = 0;
                                    } else {
                                        i21 = i33;
                                        j9 = j6;
                                        i31 = i6;
                                    }
                                    final boolean z113 = sendingMediaInfo3.highQuality;
                                    i8 = i21;
                                    final String str56 = str18;
                                    str4 = str13;
                                    AndroidUtilities.runOnUIThread(new Runnable() { // from class: org.telegram.messenger.SendMessagesHelper$$ExternalSyntheticLambda101
                                        @Override // java.lang.Runnable
                                        public final void run() {
                                            SendMessagesHelper.$r8$lambda$9xGUoyyhNB9hDrYLEcLGjcuNz1I(bitmapArr, strArr, messageObject, accountInstance, tL_photo2, map4, sendingMediaInfo3, str56, j, messageObject2, messageObject3, z3, i, i2, z5, storyItem, replyQuote, str, i3, j2, z4, j3, j4, messageSuggestionParams, z113);
                                        }
                                    });
                                    accountInstance = accountInstance;
                                    arrayList10 = arrayList14;
                                    arrayList9 = arrayList3;
                                    str3 = str16;
                                    j6 = j9;
                                    obj = null;
                                } else {
                                    obj34 = obj31;
                                    obj33 = obj32;
                                    str4 = str13;
                                    i8 = i33;
                                    if (arrayList3 == null) {
                                        arrayList4 = new ArrayList();
                                        arrayList10 = new ArrayList();
                                        arrayList12 = new ArrayList();
                                        arrayList13 = new ArrayList();
                                        arrayList11 = new ArrayList();
                                    } else {
                                        arrayList10 = arrayList14;
                                        arrayList4 = arrayList3;
                                    }
                                    ArrayList arrayList212 = arrayList11;
                                    ArrayList arrayList213 = arrayList12;
                                    ArrayList arrayList214 = arrayList13;
                                    arrayList4.add(str14);
                                    arrayList10.add(string2);
                                    arrayList212.add(sendingMediaInfo3.uri);
                                    arrayList213.add(sendingMediaInfo3.caption);
                                    arrayList214.add(sendingMediaInfo3.entities);
                                    arrayList9 = arrayList4;
                                    arrayList11 = arrayList212;
                                    arrayList12 = arrayList213;
                                    arrayList13 = arrayList214;
                                    i31 = i6;
                                    str3 = str16;
                                    j6 = j6;
                                    obj = null;
                                    accountInstance = accountInstance;
                                }
                            }
                        }
                        if (!sendingMediaInfo3.forceImage && strCopyFileToCache == null && (uri = sendingMediaInfo3.uri) != null) {
                            if (MediaController.isGif(uri)) {
                                string = sendingMediaInfo3.uri.toString();
                                strCopyFileToCache = MediaController.copyFileToCache(sendingMediaInfo3.uri, "gif");
                            } else {
                                if (MediaController.isWebp(sendingMediaInfo3.uri)) {
                                    string = sendingMediaInfo3.uri.toString();
                                    strCopyFileToCache = MediaController.copyFileToCache(sendingMediaInfo3.uri, "webp");
                                }
                                if (z12) {
                                    if (arrayList15 == null) {
                                        arrayList6 = new ArrayList();
                                        arrayList7 = new ArrayList();
                                        arrayList12 = new ArrayList();
                                        arrayList13 = new ArrayList();
                                        arrayList11 = new ArrayList();
                                    } else {
                                        arrayList6 = arrayList15;
                                        arrayList7 = arrayList14;
                                    }
                                    ArrayList arrayList112 = arrayList11;
                                    ArrayList arrayList215 = arrayList12;
                                    ArrayList arrayList216 = arrayList13;
                                    arrayList6.add(str14);
                                    arrayList7.add(string);
                                    arrayList112.add(sendingMediaInfo3.uri);
                                    arrayList215.add(sendingMediaInfo3.caption);
                                    arrayList216.add(sendingMediaInfo3.entities);
                                    accountInstance = accountInstance;
                                    arrayList11 = arrayList112;
                                    arrayList12 = arrayList215;
                                    arrayList13 = arrayList216;
                                    str2 = str2;
                                    str4 = str13;
                                    i8 = i33;
                                    obj = null;
                                    arrayList9 = arrayList6;
                                    arrayList10 = arrayList7;
                                } else {
                                    if (str14 != null) {
                                        arrayList3 = arrayList15;
                                        File file14 = new File(str14);
                                        StringBuilder sb16 = new StringBuilder();
                                        sb16.append(string);
                                        sb16.append(file14.length());
                                        str15 = str3;
                                        sb16.append(str15);
                                        sb16.append(file14.lastModified());
                                        string2 = sb16.toString();
                                    } else {
                                        arrayList3 = arrayList15;
                                        str15 = str3;
                                        string2 = null;
                                    }
                                    map2 = map2;
                                    if (map2 != null) {
                                        mediaSendPrepareWorker = (MediaSendPrepareWorker) map2.get(sendingMediaInfo3);
                                        tL_photo6 = mediaSendPrepareWorker.photo;
                                        str21 = mediaSendPrepareWorker.parentObject;
                                        if (tL_photo6 == null) {
                                            mediaSendPrepareWorker.sync.await();
                                            tL_photo6 = mediaSendPrepareWorker.photo;
                                            str21 = mediaSendPrepareWorker.parentObject;
                                        }
                                        str14 = str14;
                                        obj34 = "groupId";
                                        str16 = str15;
                                        map2 = map2;
                                        str30 = "1";
                                        str2 = str2;
                                        str13 = str13;
                                        str31 = "final";
                                        arrayList3 = arrayList3;
                                        i19 = 1;
                                        accountInstance3 = accountInstance;
                                        string2 = string2;
                                        obj33 = "originalPath";
                                        str18 = str21;
                                        tL_photo2 = tL_photo6;
                                    } else {
                                        if (z7) {
                                            obj31 = "groupId";
                                            str16 = str15;
                                            i19 = 1;
                                            accountInstance3 = accountInstance;
                                            obj32 = "originalPath";
                                            str17 = null;
                                            tL_photo = null;
                                        } else {
                                            obj31 = "groupId";
                                            str16 = str15;
                                            i19 = 1;
                                            accountInstance3 = accountInstance;
                                            obj32 = "originalPath";
                                            str17 = null;
                                            tL_photo = null;
                                        }
                                        if (tL_photo == null) {
                                            TLRPC.TL_photo tL_photoGeneratePhotoSizes4 = accountInstance3.getSendMessagesHelper().generatePhotoSizes(sendingMediaInfo3.path, sendingMediaInfo3.uri);
                                            if (z7) {
                                                new File(sendingMediaInfo3.path).delete();
                                            }
                                            tL_photo2 = tL_photoGeneratePhotoSizes4;
                                            str18 = str17;
                                            obj34 = obj31;
                                            obj33 = obj32;
                                        } else {
                                            str18 = str17;
                                            tL_photo2 = tL_photo;
                                        }
                                    }
                                    if (tL_photo2 != null) {
                                        obj34 = obj31;
                                        obj33 = obj32;
                                        map4 = new HashMap();
                                        bitmapArr = new Bitmap[i19];
                                        strArr = new String[i19];
                                        arrayList5 = sendingMediaInfo3.masks;
                                        if (arrayList5 != null) {
                                            r0 = 0;
                                        } else {
                                            r0 = 0;
                                        }
                                        tL_photo2.has_stickers = r0;
                                        if (r0 != 0) {
                                            serializedData2 = new SerializedData((sendingMediaInfo3.masks.size() * 20) + 4);
                                            serializedData2.writeInt32(sendingMediaInfo3.masks.size());
                                            while (i22 < sendingMediaInfo3.masks.size()) {
                                                sendingMediaInfo3.masks.get(i22).serializeToStream(serializedData2);
                                            }
                                            map4.put("masks", Utilities.bytesToHex(serializedData2.toByteArray()));
                                            serializedData2.cleanup();
                                        }
                                        if (string2 != null) {
                                            map4.put(obj33, string2);
                                        }
                                        if (str18 != null) {
                                            map4.put("parentObject", str18);
                                        }
                                        if (z2) {
                                            z13 = true;
                                            if (arrayList.size() == 1) {
                                            }
                                            if (z2) {
                                                i31 = i6 + 1;
                                                StringBuilder sb17 = new StringBuilder();
                                                sb17.append(_UrlKt.FRAGMENT_ENCODE_SET);
                                                j9 = j6;
                                                sb17.append(j9);
                                                map4.put(obj34, sb17.toString());
                                                if (i31 != 10) {
                                                    i21 = i33;
                                                    if (i21 == i7 - 1) {
                                                    }
                                                } else {
                                                    i21 = i33;
                                                }
                                                map4.put(str31, str30);
                                                jNextLong = 0;
                                            } else {
                                                i21 = i33;
                                                j9 = j6;
                                                i31 = i6;
                                            }
                                            final boolean z114 = sendingMediaInfo3.highQuality;
                                            i8 = i21;
                                            final String str57 = str18;
                                            str4 = str13;
                                            AndroidUtilities.runOnUIThread(new Runnable() { // from class: org.telegram.messenger.SendMessagesHelper$$ExternalSyntheticLambda101
                                                @Override // java.lang.Runnable
                                                public final void run() {
                                                    SendMessagesHelper.$r8$lambda$9xGUoyyhNB9hDrYLEcLGjcuNz1I(bitmapArr, strArr, messageObject, accountInstance, tL_photo2, map4, sendingMediaInfo3, str57, j, messageObject2, messageObject3, z3, i, i2, z5, storyItem, replyQuote, str, i3, j2, z4, j3, j4, messageSuggestionParams, z114);
                                                }
                                            });
                                            accountInstance = accountInstance;
                                            arrayList10 = arrayList14;
                                            arrayList9 = arrayList3;
                                            str3 = str16;
                                            j6 = j9;
                                            obj = null;
                                        } else {
                                            z13 = true;
                                        }
                                        closestPhotoSizeWithSize = FileLoader.getClosestPhotoSizeWithSize(tL_photo2.sizes, AndroidUtilities.getPhotoSize(sendingMediaInfo3.highQuality));
                                        if (closestPhotoSizeWithSize != null) {
                                            strArr[0] = getKeyForPhotoSize(accountInstance3, closestPhotoSizeWithSize, bitmapArr, false, false);
                                        }
                                        if (z2) {
                                            i31 = i6 + 1;
                                            StringBuilder sb18 = new StringBuilder();
                                            sb18.append(_UrlKt.FRAGMENT_ENCODE_SET);
                                            j9 = j6;
                                            sb18.append(j9);
                                            map4.put(obj34, sb18.toString());
                                            if (i31 != 10) {
                                                i21 = i33;
                                                if (i21 == i7 - 1) {
                                                }
                                            } else {
                                                i21 = i33;
                                            }
                                            map4.put(str31, str30);
                                            jNextLong = 0;
                                        } else {
                                            i21 = i33;
                                            j9 = j6;
                                            i31 = i6;
                                        }
                                        final boolean z115 = sendingMediaInfo3.highQuality;
                                        i8 = i21;
                                        final String str58 = str18;
                                        str4 = str13;
                                        AndroidUtilities.runOnUIThread(new Runnable() { // from class: org.telegram.messenger.SendMessagesHelper$$ExternalSyntheticLambda101
                                            @Override // java.lang.Runnable
                                            public final void run() {
                                                SendMessagesHelper.$r8$lambda$9xGUoyyhNB9hDrYLEcLGjcuNz1I(bitmapArr, strArr, messageObject, accountInstance, tL_photo2, map4, sendingMediaInfo3, str58, j, messageObject2, messageObject3, z3, i, i2, z5, storyItem, replyQuote, str, i3, j2, z4, j3, j4, messageSuggestionParams, z115);
                                            }
                                        });
                                        accountInstance = accountInstance;
                                        arrayList10 = arrayList14;
                                        arrayList9 = arrayList3;
                                        str3 = str16;
                                        j6 = j9;
                                        obj = null;
                                    } else {
                                        obj34 = obj31;
                                        obj33 = obj32;
                                        str4 = str13;
                                        i8 = i33;
                                        if (arrayList3 == null) {
                                            arrayList4 = new ArrayList();
                                            arrayList10 = new ArrayList();
                                            arrayList12 = new ArrayList();
                                            arrayList13 = new ArrayList();
                                            arrayList11 = new ArrayList();
                                        } else {
                                            arrayList10 = arrayList14;
                                            arrayList4 = arrayList3;
                                        }
                                        ArrayList arrayList217 = arrayList11;
                                        ArrayList arrayList218 = arrayList12;
                                        ArrayList arrayList219 = arrayList13;
                                        arrayList4.add(str14);
                                        arrayList10.add(string2);
                                        arrayList217.add(sendingMediaInfo3.uri);
                                        arrayList218.add(sendingMediaInfo3.caption);
                                        arrayList219.add(sendingMediaInfo3.entities);
                                        arrayList9 = arrayList4;
                                        arrayList11 = arrayList217;
                                        arrayList12 = arrayList218;
                                        arrayList13 = arrayList219;
                                        i31 = i6;
                                        str3 = str16;
                                        j6 = j6;
                                        obj = null;
                                        accountInstance = accountInstance;
                                    }
                                }
                            }
                        }
                        str14 = strCopyFileToCache;
                        z12 = false;
                        if (z12) {
                            if (arrayList15 == null) {
                                arrayList6 = new ArrayList();
                                arrayList7 = new ArrayList();
                                arrayList12 = new ArrayList();
                                arrayList13 = new ArrayList();
                                arrayList11 = new ArrayList();
                            } else {
                                arrayList6 = arrayList15;
                                arrayList7 = arrayList14;
                            }
                            ArrayList arrayList113 = arrayList11;
                            ArrayList arrayList2110 = arrayList12;
                            ArrayList arrayList2111 = arrayList13;
                            arrayList6.add(str14);
                            arrayList7.add(string);
                            arrayList113.add(sendingMediaInfo3.uri);
                            arrayList2110.add(sendingMediaInfo3.caption);
                            arrayList2111.add(sendingMediaInfo3.entities);
                            accountInstance = accountInstance;
                            arrayList11 = arrayList113;
                            arrayList12 = arrayList2110;
                            arrayList13 = arrayList2111;
                            str2 = str2;
                            str4 = str13;
                            i8 = i33;
                            obj = null;
                            arrayList9 = arrayList6;
                            arrayList10 = arrayList7;
                        } else {
                            if (str14 != null) {
                                arrayList3 = arrayList15;
                                File file15 = new File(str14);
                                StringBuilder sb19 = new StringBuilder();
                                sb19.append(string);
                                sb19.append(file15.length());
                                str15 = str3;
                                sb19.append(str15);
                                sb19.append(file15.lastModified());
                                string2 = sb19.toString();
                            } else {
                                arrayList3 = arrayList15;
                                str15 = str3;
                                string2 = null;
                            }
                            map2 = map2;
                            if (map2 != null) {
                                mediaSendPrepareWorker = (MediaSendPrepareWorker) map2.get(sendingMediaInfo3);
                                tL_photo6 = mediaSendPrepareWorker.photo;
                                str21 = mediaSendPrepareWorker.parentObject;
                                if (tL_photo6 == null) {
                                    mediaSendPrepareWorker.sync.await();
                                    tL_photo6 = mediaSendPrepareWorker.photo;
                                    str21 = mediaSendPrepareWorker.parentObject;
                                }
                                str14 = str14;
                                obj34 = "groupId";
                                str16 = str15;
                                map2 = map2;
                                str30 = "1";
                                str2 = str2;
                                str13 = str13;
                                str31 = "final";
                                arrayList3 = arrayList3;
                                i19 = 1;
                                accountInstance3 = accountInstance;
                                string2 = string2;
                                obj33 = "originalPath";
                                str18 = str21;
                                tL_photo2 = tL_photo6;
                            } else {
                                if (z7) {
                                    obj31 = "groupId";
                                    str16 = str15;
                                    i19 = 1;
                                    accountInstance3 = accountInstance;
                                    obj32 = "originalPath";
                                    str17 = null;
                                    tL_photo = null;
                                } else {
                                    obj31 = "groupId";
                                    str16 = str15;
                                    i19 = 1;
                                    accountInstance3 = accountInstance;
                                    obj32 = "originalPath";
                                    str17 = null;
                                    tL_photo = null;
                                }
                                if (tL_photo == null) {
                                    TLRPC.TL_photo tL_photoGeneratePhotoSizes5 = accountInstance3.getSendMessagesHelper().generatePhotoSizes(sendingMediaInfo3.path, sendingMediaInfo3.uri);
                                    if (z7) {
                                        new File(sendingMediaInfo3.path).delete();
                                    }
                                    tL_photo2 = tL_photoGeneratePhotoSizes5;
                                    str18 = str17;
                                    obj34 = obj31;
                                    obj33 = obj32;
                                } else {
                                    str18 = str17;
                                    tL_photo2 = tL_photo;
                                }
                            }
                            if (tL_photo2 != null) {
                                obj34 = obj31;
                                obj33 = obj32;
                                map4 = new HashMap();
                                bitmapArr = new Bitmap[i19];
                                strArr = new String[i19];
                                arrayList5 = sendingMediaInfo3.masks;
                                if (arrayList5 != null) {
                                    r0 = 0;
                                } else {
                                    r0 = 0;
                                }
                                tL_photo2.has_stickers = r0;
                                if (r0 != 0) {
                                    serializedData2 = new SerializedData((sendingMediaInfo3.masks.size() * 20) + 4);
                                    serializedData2.writeInt32(sendingMediaInfo3.masks.size());
                                    while (i22 < sendingMediaInfo3.masks.size()) {
                                        sendingMediaInfo3.masks.get(i22).serializeToStream(serializedData2);
                                    }
                                    map4.put("masks", Utilities.bytesToHex(serializedData2.toByteArray()));
                                    serializedData2.cleanup();
                                }
                                if (string2 != null) {
                                    map4.put(obj33, string2);
                                }
                                if (str18 != null) {
                                    map4.put("parentObject", str18);
                                }
                                if (z2) {
                                    z13 = true;
                                    if (arrayList.size() == 1) {
                                    }
                                    if (z2) {
                                        i31 = i6 + 1;
                                        StringBuilder sb110 = new StringBuilder();
                                        sb110.append(_UrlKt.FRAGMENT_ENCODE_SET);
                                        j9 = j6;
                                        sb110.append(j9);
                                        map4.put(obj34, sb110.toString());
                                        if (i31 != 10) {
                                            i21 = i33;
                                            if (i21 == i7 - 1) {
                                            }
                                        } else {
                                            i21 = i33;
                                        }
                                        map4.put(str31, str30);
                                        jNextLong = 0;
                                    } else {
                                        i21 = i33;
                                        j9 = j6;
                                        i31 = i6;
                                    }
                                    final boolean z116 = sendingMediaInfo3.highQuality;
                                    i8 = i21;
                                    final String str59 = str18;
                                    str4 = str13;
                                    AndroidUtilities.runOnUIThread(new Runnable() { // from class: org.telegram.messenger.SendMessagesHelper$$ExternalSyntheticLambda101
                                        @Override // java.lang.Runnable
                                        public final void run() {
                                            SendMessagesHelper.$r8$lambda$9xGUoyyhNB9hDrYLEcLGjcuNz1I(bitmapArr, strArr, messageObject, accountInstance, tL_photo2, map4, sendingMediaInfo3, str59, j, messageObject2, messageObject3, z3, i, i2, z5, storyItem, replyQuote, str, i3, j2, z4, j3, j4, messageSuggestionParams, z116);
                                        }
                                    });
                                    accountInstance = accountInstance;
                                    arrayList10 = arrayList14;
                                    arrayList9 = arrayList3;
                                    str3 = str16;
                                    j6 = j9;
                                    obj = null;
                                } else {
                                    z13 = true;
                                }
                                closestPhotoSizeWithSize = FileLoader.getClosestPhotoSizeWithSize(tL_photo2.sizes, AndroidUtilities.getPhotoSize(sendingMediaInfo3.highQuality));
                                if (closestPhotoSizeWithSize != null) {
                                    strArr[0] = getKeyForPhotoSize(accountInstance3, closestPhotoSizeWithSize, bitmapArr, false, false);
                                }
                                if (z2) {
                                    i31 = i6 + 1;
                                    StringBuilder sb111 = new StringBuilder();
                                    sb111.append(_UrlKt.FRAGMENT_ENCODE_SET);
                                    j9 = j6;
                                    sb111.append(j9);
                                    map4.put(obj34, sb111.toString());
                                    if (i31 != 10) {
                                        i21 = i33;
                                        if (i21 == i7 - 1) {
                                        }
                                    } else {
                                        i21 = i33;
                                    }
                                    map4.put(str31, str30);
                                    jNextLong = 0;
                                } else {
                                    i21 = i33;
                                    j9 = j6;
                                    i31 = i6;
                                }
                                final boolean z117 = sendingMediaInfo3.highQuality;
                                i8 = i21;
                                final String str510 = str18;
                                str4 = str13;
                                AndroidUtilities.runOnUIThread(new Runnable() { // from class: org.telegram.messenger.SendMessagesHelper$$ExternalSyntheticLambda101
                                    @Override // java.lang.Runnable
                                    public final void run() {
                                        SendMessagesHelper.$r8$lambda$9xGUoyyhNB9hDrYLEcLGjcuNz1I(bitmapArr, strArr, messageObject, accountInstance, tL_photo2, map4, sendingMediaInfo3, str510, j, messageObject2, messageObject3, z3, i, i2, z5, storyItem, replyQuote, str, i3, j2, z4, j3, j4, messageSuggestionParams, z117);
                                    }
                                });
                                accountInstance = accountInstance;
                                arrayList10 = arrayList14;
                                arrayList9 = arrayList3;
                                str3 = str16;
                                j6 = j9;
                                obj = null;
                            } else {
                                obj34 = obj31;
                                obj33 = obj32;
                                str4 = str13;
                                i8 = i33;
                                if (arrayList3 == null) {
                                    arrayList4 = new ArrayList();
                                    arrayList10 = new ArrayList();
                                    arrayList12 = new ArrayList();
                                    arrayList13 = new ArrayList();
                                    arrayList11 = new ArrayList();
                                } else {
                                    arrayList10 = arrayList14;
                                    arrayList4 = arrayList3;
                                }
                                ArrayList arrayList2112 = arrayList11;
                                ArrayList arrayList2113 = arrayList12;
                                ArrayList arrayList2114 = arrayList13;
                                arrayList4.add(str14);
                                arrayList10.add(string2);
                                arrayList2112.add(sendingMediaInfo3.uri);
                                arrayList2113.add(sendingMediaInfo3.caption);
                                arrayList2114.add(sendingMediaInfo3.entities);
                                arrayList9 = arrayList4;
                                arrayList11 = arrayList2112;
                                arrayList12 = arrayList2113;
                                arrayList13 = arrayList2114;
                                i31 = i6;
                                str3 = str16;
                                j6 = j6;
                                obj = null;
                                accountInstance = accountInstance;
                            }
                        }
                    }
                    str14 = strCopyFileToCache;
                    z12 = true;
                    if (z12) {
                        if (arrayList15 == null) {
                            arrayList6 = new ArrayList();
                            arrayList7 = new ArrayList();
                            arrayList12 = new ArrayList();
                            arrayList13 = new ArrayList();
                            arrayList11 = new ArrayList();
                        } else {
                            arrayList6 = arrayList15;
                            arrayList7 = arrayList14;
                        }
                        ArrayList arrayList114 = arrayList11;
                        ArrayList arrayList2115 = arrayList12;
                        ArrayList arrayList2116 = arrayList13;
                        arrayList6.add(str14);
                        arrayList7.add(string);
                        arrayList114.add(sendingMediaInfo3.uri);
                        arrayList2115.add(sendingMediaInfo3.caption);
                        arrayList2116.add(sendingMediaInfo3.entities);
                        accountInstance = accountInstance;
                        arrayList11 = arrayList114;
                        arrayList12 = arrayList2115;
                        arrayList13 = arrayList2116;
                        str2 = str2;
                        str4 = str13;
                        i8 = i33;
                        obj = null;
                        arrayList9 = arrayList6;
                        arrayList10 = arrayList7;
                    } else {
                        if (str14 != null) {
                            arrayList3 = arrayList15;
                            File file16 = new File(str14);
                            StringBuilder sb112 = new StringBuilder();
                            sb112.append(string);
                            sb112.append(file16.length());
                            str15 = str3;
                            sb112.append(str15);
                            sb112.append(file16.lastModified());
                            string2 = sb112.toString();
                        } else {
                            arrayList3 = arrayList15;
                            str15 = str3;
                            string2 = null;
                        }
                        map2 = map2;
                        if (map2 != null) {
                            mediaSendPrepareWorker = (MediaSendPrepareWorker) map2.get(sendingMediaInfo3);
                            tL_photo6 = mediaSendPrepareWorker.photo;
                            str21 = mediaSendPrepareWorker.parentObject;
                            if (tL_photo6 == null) {
                                mediaSendPrepareWorker.sync.await();
                                tL_photo6 = mediaSendPrepareWorker.photo;
                                str21 = mediaSendPrepareWorker.parentObject;
                            }
                            str14 = str14;
                            obj34 = "groupId";
                            str16 = str15;
                            map2 = map2;
                            str30 = "1";
                            str2 = str2;
                            str13 = str13;
                            str31 = "final";
                            arrayList3 = arrayList3;
                            i19 = 1;
                            accountInstance3 = accountInstance;
                            string2 = string2;
                            obj33 = "originalPath";
                            str18 = str21;
                            tL_photo2 = tL_photo6;
                        } else {
                            if (z7) {
                                obj31 = "groupId";
                                str16 = str15;
                                i19 = 1;
                                accountInstance3 = accountInstance;
                                obj32 = "originalPath";
                                str17 = null;
                                tL_photo = null;
                            } else {
                                obj31 = "groupId";
                                str16 = str15;
                                i19 = 1;
                                accountInstance3 = accountInstance;
                                obj32 = "originalPath";
                                str17 = null;
                                tL_photo = null;
                            }
                            if (tL_photo == null) {
                                TLRPC.TL_photo tL_photoGeneratePhotoSizes6 = accountInstance3.getSendMessagesHelper().generatePhotoSizes(sendingMediaInfo3.path, sendingMediaInfo3.uri);
                                if (z7) {
                                    new File(sendingMediaInfo3.path).delete();
                                }
                                tL_photo2 = tL_photoGeneratePhotoSizes6;
                                str18 = str17;
                                obj34 = obj31;
                                obj33 = obj32;
                            } else {
                                str18 = str17;
                                tL_photo2 = tL_photo;
                            }
                        }
                        if (tL_photo2 != null) {
                            obj34 = obj31;
                            obj33 = obj32;
                            map4 = new HashMap();
                            bitmapArr = new Bitmap[i19];
                            strArr = new String[i19];
                            arrayList5 = sendingMediaInfo3.masks;
                            if (arrayList5 != null) {
                                r0 = 0;
                            } else {
                                r0 = 0;
                            }
                            tL_photo2.has_stickers = r0;
                            if (r0 != 0) {
                                serializedData2 = new SerializedData((sendingMediaInfo3.masks.size() * 20) + 4);
                                serializedData2.writeInt32(sendingMediaInfo3.masks.size());
                                while (i22 < sendingMediaInfo3.masks.size()) {
                                    sendingMediaInfo3.masks.get(i22).serializeToStream(serializedData2);
                                }
                                map4.put("masks", Utilities.bytesToHex(serializedData2.toByteArray()));
                                serializedData2.cleanup();
                            }
                            if (string2 != null) {
                                map4.put(obj33, string2);
                            }
                            if (str18 != null) {
                                map4.put("parentObject", str18);
                            }
                            if (z2) {
                                z13 = true;
                                if (arrayList.size() == 1) {
                                }
                                if (z2) {
                                    i31 = i6 + 1;
                                    StringBuilder sb113 = new StringBuilder();
                                    sb113.append(_UrlKt.FRAGMENT_ENCODE_SET);
                                    j9 = j6;
                                    sb113.append(j9);
                                    map4.put(obj34, sb113.toString());
                                    if (i31 != 10) {
                                        i21 = i33;
                                        if (i21 == i7 - 1) {
                                        }
                                    } else {
                                        i21 = i33;
                                    }
                                    map4.put(str31, str30);
                                    jNextLong = 0;
                                } else {
                                    i21 = i33;
                                    j9 = j6;
                                    i31 = i6;
                                }
                                final boolean z118 = sendingMediaInfo3.highQuality;
                                i8 = i21;
                                final String str511 = str18;
                                str4 = str13;
                                AndroidUtilities.runOnUIThread(new Runnable() { // from class: org.telegram.messenger.SendMessagesHelper$$ExternalSyntheticLambda101
                                    @Override // java.lang.Runnable
                                    public final void run() {
                                        SendMessagesHelper.$r8$lambda$9xGUoyyhNB9hDrYLEcLGjcuNz1I(bitmapArr, strArr, messageObject, accountInstance, tL_photo2, map4, sendingMediaInfo3, str511, j, messageObject2, messageObject3, z3, i, i2, z5, storyItem, replyQuote, str, i3, j2, z4, j3, j4, messageSuggestionParams, z118);
                                    }
                                });
                                accountInstance = accountInstance;
                                arrayList10 = arrayList14;
                                arrayList9 = arrayList3;
                                str3 = str16;
                                j6 = j9;
                                obj = null;
                            } else {
                                z13 = true;
                            }
                            closestPhotoSizeWithSize = FileLoader.getClosestPhotoSizeWithSize(tL_photo2.sizes, AndroidUtilities.getPhotoSize(sendingMediaInfo3.highQuality));
                            if (closestPhotoSizeWithSize != null) {
                                strArr[0] = getKeyForPhotoSize(accountInstance3, closestPhotoSizeWithSize, bitmapArr, false, false);
                            }
                            if (z2) {
                                i31 = i6 + 1;
                                StringBuilder sb114 = new StringBuilder();
                                sb114.append(_UrlKt.FRAGMENT_ENCODE_SET);
                                j9 = j6;
                                sb114.append(j9);
                                map4.put(obj34, sb114.toString());
                                if (i31 != 10) {
                                    i21 = i33;
                                    if (i21 == i7 - 1) {
                                    }
                                } else {
                                    i21 = i33;
                                }
                                map4.put(str31, str30);
                                jNextLong = 0;
                            } else {
                                i21 = i33;
                                j9 = j6;
                                i31 = i6;
                            }
                            final boolean z119 = sendingMediaInfo3.highQuality;
                            i8 = i21;
                            final String str512 = str18;
                            str4 = str13;
                            AndroidUtilities.runOnUIThread(new Runnable() { // from class: org.telegram.messenger.SendMessagesHelper$$ExternalSyntheticLambda101
                                @Override // java.lang.Runnable
                                public final void run() {
                                    SendMessagesHelper.$r8$lambda$9xGUoyyhNB9hDrYLEcLGjcuNz1I(bitmapArr, strArr, messageObject, accountInstance, tL_photo2, map4, sendingMediaInfo3, str512, j, messageObject2, messageObject3, z3, i, i2, z5, storyItem, replyQuote, str, i3, j2, z4, j3, j4, messageSuggestionParams, z119);
                                }
                            });
                            accountInstance = accountInstance;
                            arrayList10 = arrayList14;
                            arrayList9 = arrayList3;
                            str3 = str16;
                            j6 = j9;
                            obj = null;
                        } else {
                            obj34 = obj31;
                            obj33 = obj32;
                            str4 = str13;
                            i8 = i33;
                            if (arrayList3 == null) {
                                arrayList4 = new ArrayList();
                                arrayList10 = new ArrayList();
                                arrayList12 = new ArrayList();
                                arrayList13 = new ArrayList();
                                arrayList11 = new ArrayList();
                            } else {
                                arrayList10 = arrayList14;
                                arrayList4 = arrayList3;
                            }
                            ArrayList arrayList2117 = arrayList11;
                            ArrayList arrayList2118 = arrayList12;
                            ArrayList arrayList2119 = arrayList13;
                            arrayList4.add(str14);
                            arrayList10.add(string2);
                            arrayList2117.add(sendingMediaInfo3.uri);
                            arrayList2118.add(sendingMediaInfo3.caption);
                            arrayList2119.add(sendingMediaInfo3.entities);
                            arrayList9 = arrayList4;
                            arrayList11 = arrayList2117;
                            arrayList12 = arrayList2118;
                            arrayList13 = arrayList2119;
                            i31 = i6;
                            str3 = str16;
                            j6 = j6;
                            obj = null;
                            accountInstance = accountInstance;
                        }
                    }
                }
                i31 = i6;
            }
            i32 = i8 + 1;
            arrayList8 = arrayList;
            zIsEncryptedDialog = z7;
            map = map2;
            str29 = str4;
            str26 = str2;
            str27 = str3;
            size2 = i7;
            j11 = j6;
            i4 = 1;
            i5 = 0;
        }
        boolean z20 = zIsEncryptedDialog;
        ArrayList arrayList30 = arrayList9;
        int i37 = size2;
        ArrayList arrayList31 = arrayList10;
        long j15 = jNextLong;
        if (j15 != 0) {
            finishGroup(accountInstance, j15, i);
        }
        if (inputContentInfoCompat != null) {
            inputContentInfoCompat.releasePermission();
        }
        if (arrayList30 != null && !arrayList30.isEmpty()) {
            int i38 = 1;
            long[] jArr = new long[1];
            int size3 = arrayList30.size();
            int i39 = i31;
            int i40 = 0;
            while (i40 < size3) {
                if (!z || z20 || i37 <= i38 || i39 % 10 != 0) {
                    z6 = false;
                } else {
                    z6 = false;
                    jArr[0] = Utilities.random.nextLong();
                    i39 = 0;
                }
                int i41 = i39 + 1;
                ArrayList arrayList32 = arrayList30;
                ArrayList arrayList33 = arrayList31;
                ArrayList arrayList34 = arrayList11;
                ArrayList arrayList35 = arrayList13;
                ArrayList arrayList36 = arrayList12;
                arrayList30 = arrayList32;
                arrayList31 = arrayList33;
                arrayList11 = arrayList34;
                arrayList13 = arrayList35;
                arrayList12 = arrayList36;
                handleError(prepareSendingDocumentInternal(accountInstance, (String) arrayList32.get(i40), (String) arrayList33.get(i40), (Uri) arrayList34.get(i40), fileExtension, j, messageObject2, messageObject3, storyItem, replyQuote, (ArrayList) arrayList35.get(i40), messageObject, jArr, (i41 == 10 || i40 == size3 + (-1)) ? true : z6, (CharSequence) arrayList36.get(i40), z3, i, 0, null, z, str, i3, j2, z4, j3, j4, messageSuggestionParams), accountInstance);
                i40++;
                size3 = size3;
                i39 = i41;
                i38 = 1;
                i37 = i37;
            }
        }
        if (BuildVars.LOGS_ENABLED) {
            FileLog.d("total send time = " + (System.currentTimeMillis() - jCurrentTimeMillis));
        }
    }

    public static /* synthetic */ void $r8$lambda$wUL7g7xJTiOhNKMBL2NHaB41Kps(MediaSendPrepareWorker mediaSendPrepareWorker, AccountInstance accountInstance, SendingMediaInfo sendingMediaInfo, boolean z) {
        mediaSendPrepareWorker.photo = accountInstance.getSendMessagesHelper().generatePhotoSizes(null, sendingMediaInfo.path, sendingMediaInfo.uri, sendingMediaInfo.highQuality);
        if (z && sendingMediaInfo.canDeleteAfter) {
            new File(sendingMediaInfo.path).delete();
        }
        mediaSendPrepareWorker.sync.countDown();
    }

    public static /* synthetic */ void $r8$lambda$8cE258W29ZQqUsOyx0P3xSaj2tI(MessageObject messageObject, AccountInstance accountInstance, TLRPC.TL_document tL_document, String str, HashMap map, SendingMediaInfo sendingMediaInfo, String str2, long j, MessageObject messageObject2, MessageObject messageObject3, boolean z, int i, int i2, TL_stories.StoryItem storyItem, ChatActivity.ReplyQuote replyQuote, String str3, int i3, boolean z2, long j2, boolean z3, long j3, long j4, MessageSuggestionParams messageSuggestionParams) {
        if (messageObject != null) {
            accountInstance.getSendMessagesHelper().editMessage(messageObject, null, null, tL_document, str, null, map, false, sendingMediaInfo.hasMediaSpoilers, str2);
            return;
        }
        SendMessageParams sendMessageParamsOf = SendMessageParams.of(tL_document, null, str, j, messageObject2, messageObject3, sendingMediaInfo.caption, sendingMediaInfo.entities, null, map, z, i, i2, 0, str2, null, false, sendingMediaInfo.hasMediaSpoilers);
        sendMessageParamsOf.replyToStoryItem = storyItem;
        sendMessageParamsOf.replyQuote = replyQuote;
        sendMessageParamsOf.quick_reply_shortcut = str3;
        sendMessageParamsOf.quick_reply_shortcut_id = i3;
        if (z2) {
            sendMessageParamsOf.effect_id = j2;
        }
        sendMessageParamsOf.invert_media = z3;
        sendMessageParamsOf.payStars = j3;
        sendMessageParamsOf.monoForumPeer = j4;
        sendMessageParamsOf.suggestionParams = messageSuggestionParams;
        accountInstance.getSendMessagesHelper().sendMessage(sendMessageParamsOf);
    }

    public static /* synthetic */ void $r8$lambda$QK2E_w0HUeKhLwnjwB42Kh8FiBU(MessageObject messageObject, AccountInstance accountInstance, TLRPC.TL_photo tL_photo, boolean z, SendingMediaInfo sendingMediaInfo, HashMap map, String str, long j, MessageObject messageObject2, MessageObject messageObject3, boolean z2, int i, int i2, TL_stories.StoryItem storyItem, ChatActivity.ReplyQuote replyQuote, int i3, String str2, long j2, boolean z3, long j3, long j4, MessageSuggestionParams messageSuggestionParams) {
        if (messageObject != null) {
            accountInstance.getSendMessagesHelper().editMessage(messageObject, tL_photo, null, null, z ? sendingMediaInfo.searchImage.imageUrl : null, null, map, false, sendingMediaInfo.hasMediaSpoilers, str);
            return;
        }
        SendMessageParams sendMessageParamsOf = SendMessageParams.of(tL_photo, z ? sendingMediaInfo.searchImage.imageUrl : null, j, messageObject2, messageObject3, sendingMediaInfo.caption, sendingMediaInfo.entities, null, map, z2, i, i2, sendingMediaInfo.ttl, str, false, sendingMediaInfo.hasMediaSpoilers);
        sendMessageParamsOf.replyToStoryItem = storyItem;
        sendMessageParamsOf.replyQuote = replyQuote;
        sendMessageParamsOf.quick_reply_shortcut_id = i3;
        sendMessageParamsOf.quick_reply_shortcut = str2;
        sendMessageParamsOf.effect_id = j2;
        sendMessageParamsOf.invert_media = z3;
        sendMessageParamsOf.payStars = j3;
        sendMessageParamsOf.monoForumPeer = j4;
        sendMessageParamsOf.suggestionParams = messageSuggestionParams;
        accountInstance.getSendMessagesHelper().sendMessage(sendMessageParamsOf);
    }

    /* JADX INFO: renamed from: $r8$lambda$Ba7ZedawABohTrcW-U_I8SylM10, reason: not valid java name */
    public static /* synthetic */ void m3708$r8$lambda$Ba7ZedawABohTrcWU_I8SylM10(Bitmap bitmap, String str, MessageObject messageObject, AccountInstance accountInstance, VideoEditedInfo videoEditedInfo, TLRPC.TL_document tL_document, String str2, HashMap map, SendingMediaInfo sendingMediaInfo, String str3, long j, MessageObject messageObject2, MessageObject messageObject3, boolean z, int i, int i2, TL_stories.StoryItem storyItem, ChatActivity.ReplyQuote replyQuote, String str4, int i3, long j2, boolean z2, TLRPC.PhotoSize photoSize, long j3, long j4, MessageSuggestionParams messageSuggestionParams) {
        if (bitmap != null && str != null) {
            ImageLoader.getInstance().putImageToCache(new BitmapDrawable(bitmap), str, false);
        }
        if (messageObject != null) {
            accountInstance.getSendMessagesHelper().editMessage(messageObject, null, videoEditedInfo, tL_document, str2, null, map, false, sendingMediaInfo.hasMediaSpoilers, str3);
            return;
        }
        SendMessageParams sendMessageParamsOf = SendMessageParams.of(tL_document, videoEditedInfo, str2, j, messageObject2, messageObject3, sendingMediaInfo.caption, sendingMediaInfo.entities, null, map, z, i, i2, sendingMediaInfo.ttl, str3, null, false, sendingMediaInfo.hasMediaSpoilers);
        sendMessageParamsOf.replyToStoryItem = storyItem;
        sendMessageParamsOf.replyQuote = replyQuote;
        sendMessageParamsOf.quick_reply_shortcut = str4;
        sendMessageParamsOf.quick_reply_shortcut_id = i3;
        sendMessageParamsOf.effect_id = j2;
        sendMessageParamsOf.invert_media = z2;
        sendMessageParamsOf.stars = sendingMediaInfo.stars;
        sendMessageParamsOf.cover = photoSize;
        sendMessageParamsOf.payStars = j3;
        sendMessageParamsOf.monoForumPeer = j4;
        sendMessageParamsOf.suggestionParams = messageSuggestionParams;
        accountInstance.getSendMessagesHelper().sendMessage(sendMessageParamsOf);
    }

    public static /* synthetic */ void $r8$lambda$9xGUoyyhNB9hDrYLEcLGjcuNz1I(Bitmap[] bitmapArr, String[] strArr, MessageObject messageObject, AccountInstance accountInstance, TLRPC.TL_photo tL_photo, HashMap map, SendingMediaInfo sendingMediaInfo, String str, long j, MessageObject messageObject2, MessageObject messageObject3, boolean z, int i, int i2, boolean z2, TL_stories.StoryItem storyItem, ChatActivity.ReplyQuote replyQuote, String str2, int i3, long j2, boolean z3, long j3, long j4, MessageSuggestionParams messageSuggestionParams, boolean z4) {
        if (bitmapArr[0] != null && strArr[0] != null) {
            ImageLoader.getInstance().putImageToCache(new BitmapDrawable(bitmapArr[0]), strArr[0], false);
        }
        if (messageObject != null) {
            accountInstance.getSendMessagesHelper().editMessage(messageObject, tL_photo, null, null, null, null, map, false, sendingMediaInfo.hasMediaSpoilers, str);
            return;
        }
        SendMessageParams sendMessageParamsOf = SendMessageParams.of(tL_photo, null, j, messageObject2, messageObject3, sendingMediaInfo.caption, sendingMediaInfo.entities, null, map, z, i, i2, sendingMediaInfo.ttl, str, z2, sendingMediaInfo.hasMediaSpoilers);
        sendMessageParamsOf.replyToStoryItem = storyItem;
        sendMessageParamsOf.replyQuote = replyQuote;
        sendMessageParamsOf.quick_reply_shortcut = str2;
        sendMessageParamsOf.quick_reply_shortcut_id = i3;
        sendMessageParamsOf.effect_id = j2;
        sendMessageParamsOf.invert_media = z3;
        sendMessageParamsOf.stars = sendingMediaInfo.stars;
        sendMessageParamsOf.payStars = j3;
        sendMessageParamsOf.monoForumPeer = j4;
        sendMessageParamsOf.suggestionParams = messageSuggestionParams;
        sendMessageParamsOf.sendingHighQuality = z4;
        accountInstance.getSendMessagesHelper().sendMessage(sendMessageParamsOf);
    }

    /* JADX WARN: Multi-variable type inference failed */
    /* JADX WARN: Type inference failed for: r2v0 */
    /* JADX WARN: Type inference failed for: r2v1 */
    /* JADX WARN: Type inference failed for: r2v10 */
    /* JADX WARN: Type inference failed for: r2v19 */
    /* JADX WARN: Type inference failed for: r2v2, types: [android.media.MediaMetadataRetriever] */
    /* JADX WARN: Type inference failed for: r2v25 */
    /* JADX WARN: Type inference failed for: r2v26 */
    /* JADX WARN: Type inference failed for: r2v27 */
    /* JADX WARN: Type inference failed for: r2v28 */
    /* JADX WARN: Type inference failed for: r2v29 */
    /* JADX WARN: Type inference failed for: r2v3, types: [android.media.MediaMetadataRetriever] */
    /* JADX WARN: Type inference failed for: r2v30 */
    /* JADX WARN: Type inference failed for: r2v31 */
    /* JADX WARN: Type inference failed for: r2v32 */
    /* JADX WARN: Type inference failed for: r2v33 */
    /* JADX WARN: Type inference failed for: r2v34 */
    /* JADX WARN: Type inference failed for: r2v4 */
    /* JADX WARN: Type inference failed for: r2v7, types: [double] */
    /* JADX WARN: Type inference failed for: r2v8 */
    /* JADX WARN: Type inference failed for: r2v9 */
    public static void fillVideoAttribute(String str, TLRPC.TL_documentAttributeVideo tL_documentAttributeVideo, VideoEditedInfo videoEditedInfo) {
        int iIntValue;
        ?? r2 = 0;
        ?? duration = 0;
        try {
            try {
                MediaMetadataRetriever mediaMetadataRetriever = new MediaMetadataRetriever();
                try {
                    mediaMetadataRetriever.setDataSource(str);
                    String strExtractMetadata = mediaMetadataRetriever.extractMetadata(18);
                    if (strExtractMetadata != null) {
                        tL_documentAttributeVideo.w = Integer.parseInt(strExtractMetadata);
                    }
                    String strExtractMetadata2 = mediaMetadataRetriever.extractMetadata(19);
                    if (strExtractMetadata2 != null) {
                        tL_documentAttributeVideo.h = Integer.parseInt(strExtractMetadata2);
                    }
                    String strExtractMetadata3 = mediaMetadataRetriever.extractMetadata(9);
                    if (strExtractMetadata3 != null) {
                        tL_documentAttributeVideo.duration = Long.parseLong(strExtractMetadata3) / 1000.0d;
                    }
                    String strExtractMetadata4 = mediaMetadataRetriever.extractMetadata(24);
                    ?? r3 = strExtractMetadata4;
                    if (strExtractMetadata4 != null) {
                        iIntValue = Utilities.parseInt((CharSequence) strExtractMetadata4).intValue();
                        if (videoEditedInfo != null) {
                            videoEditedInfo.rotationValue = iIntValue;
                            r3 = iIntValue;
                        } else if (iIntValue == 90 || iIntValue == 270) {
                            r3 = iIntValue;
                            int i = tL_documentAttributeVideo.w;
                            int i2 = tL_documentAttributeVideo.h;
                            tL_documentAttributeVideo.w = i2;
                            tL_documentAttributeVideo.h = i;
                            r3 = i2;
                        }
                    }
                    try {
                        r3 = iIntValue;
                        mediaMetadataRetriever.release();
                        r2 = r3;
                    } catch (Exception e) {
                        FileLog.e(e);
                        r2 = r3;
                    }
                } catch (Exception e2) {
                    e = e2;
                    duration = mediaMetadataRetriever;
                    FileLog.e(e);
                    if (duration != 0) {
                        try {
                            duration.release();
                        } catch (Exception e3) {
                            FileLog.e(e3);
                        }
                    }
                    try {
                        Context context = ApplicationLoader.applicationContext;
                        File file = new File(str);
                        MediaPlayer mediaPlayerCreate = MediaPlayer.create(context, Uri.fromFile(file));
                        r2 = file;
                        if (mediaPlayerCreate != null) {
                            duration = ((double) mediaPlayerCreate.getDuration()) / 1000.0d;
                            tL_documentAttributeVideo.duration = duration;
                            tL_documentAttributeVideo.w = mediaPlayerCreate.getVideoWidth();
                            tL_documentAttributeVideo.h = mediaPlayerCreate.getVideoHeight();
                            mediaPlayerCreate.release();
                            r2 = duration;
                        }
                    } catch (Exception e4) {
                        FileLog.e(e4);
                        r2 = duration;
                    }
                } catch (Throwable th) {
                    th = th;
                    r2 = mediaMetadataRetriever;
                    if (r2 != 0) {
                        try {
                            r2.release();
                        } catch (Exception e5) {
                            FileLog.e(e5);
                        }
                    }
                    throw th;
                }
            } catch (Throwable th2) {
                th = th2;
            }
        } catch (Exception e6) {
            e = e6;
        }
    }

    public static Bitmap createVideoThumbnail(String str, int i) {
        float f;
        if (i == 2) {
            f = 1920.0f;
        } else {
            f = i == 3 ? 96.0f : 512.0f;
        }
        Bitmap bitmapCreateVideoThumbnailAtTime = createVideoThumbnailAtTime(str, 0L);
        if (bitmapCreateVideoThumbnailAtTime == null) {
            return bitmapCreateVideoThumbnailAtTime;
        }
        int width = bitmapCreateVideoThumbnailAtTime.getWidth();
        int height = bitmapCreateVideoThumbnailAtTime.getHeight();
        float f2 = width;
        if (f2 <= f && height <= f) {
            return bitmapCreateVideoThumbnailAtTime;
        }
        float fMax = Math.max(width, height) / f;
        return Bitmap.createScaledBitmap(bitmapCreateVideoThumbnailAtTime, (int) (f2 / fMax), (int) (height / fMax), true);
    }

    public static Bitmap createVideoThumbnailAtTime(String str, long j) {
        return createVideoThumbnailAtTime(str, j, null, false);
    }

    public static Bitmap createVideoThumbnailAtTime(String str, long j, int[] iArr, boolean z) {
        if (z) {
            AnimatedFileDrawable animatedFileDrawable = new AnimatedFileDrawable(new File(str), true, 0L, 0, null, null, null, 0L, 0, true, null);
            Bitmap frameAtTime = animatedFileDrawable.getFrameAtTime(j, z);
            if (iArr != null) {
                iArr[0] = animatedFileDrawable.getOrientation();
            }
            animatedFileDrawable.recycle();
            return frameAtTime == null ? createVideoThumbnailAtTime(str, j, iArr, false) : frameAtTime;
        }
        MediaMetadataRetriever mediaMetadataRetriever = new MediaMetadataRetriever();
        Bitmap frameAtTime2 = null;
        try {
            mediaMetadataRetriever.setDataSource(str);
            frameAtTime2 = mediaMetadataRetriever.getFrameAtTime(j, 1);
            if (frameAtTime2 == null) {
                frameAtTime2 = mediaMetadataRetriever.getFrameAtTime(j, 3);
            }
        } catch (Exception unused) {
        } catch (Throwable th) {
            try {
                mediaMetadataRetriever.release();
            } catch (Throwable unused2) {
            }
            throw th;
        }
        try {
            mediaMetadataRetriever.release();
        } catch (Throwable unused3) {
        }
        return frameAtTime2;
    }

    public static VideoEditedInfo createCompressionSettings(String str) {
        float f;
        int[] iArr = new int[11];
        AnimatedFileDrawable.getVideoInfo(str, iArr);
        if (iArr[0] == 0) {
            if (!BuildVars.LOGS_ENABLED) {
                return null;
            }
            FileLog.d("video hasn't avc1 atom");
            return null;
        }
        long length = new File(str).length();
        int videoBitrate = MediaController.getVideoBitrate(str);
        if (videoBitrate == -1) {
            videoBitrate = iArr[3];
        }
        int i = 4;
        float f2 = iArr[4];
        long j = iArr[5];
        int i2 = iArr[7];
        VideoEditedInfo videoEditedInfo = new VideoEditedInfo();
        videoEditedInfo.startTime = -1L;
        videoEditedInfo.endTime = -1L;
        videoEditedInfo.bitrate = videoBitrate;
        videoEditedInfo.originalPath = str;
        videoEditedInfo.framerate = i2;
        videoEditedInfo.estimatedDuration = (long) Math.ceil(f2);
        boolean z = true;
        int i3 = iArr[1];
        videoEditedInfo.originalWidth = i3;
        videoEditedInfo.resultWidth = i3;
        int i4 = iArr[2];
        videoEditedInfo.originalHeight = i4;
        videoEditedInfo.resultHeight = i4;
        videoEditedInfo.rotationValue = iArr[8];
        videoEditedInfo.originalDuration = (long) (f2 * 1000.0f);
        float fMax = Math.max(i3, i4);
        if (fMax > 3840.0f) {
            i = 7;
        } else if (fMax > 2560.0f) {
            i = 6;
        } else if (fMax > 1920.0f) {
            i = 5;
        } else if (fMax <= 1280.0f) {
            if (fMax > 854.0f) {
                i = 3;
            } else {
                i = fMax > 640.0f ? 2 : 1;
            }
        }
        int iRound = Math.round(DownloadController.getInstance(UserConfig.selectedAccount).getMaxVideoBitrate() / (100.0f / i));
        if (iRound > i) {
            iRound = i;
        }
        if (new File(str).length() < 1048576000) {
            if (iRound != i || Math.max(videoEditedInfo.originalWidth, videoEditedInfo.originalHeight) > 1280) {
                if (iRound == 1) {
                    f = 480.0f;
                } else if (iRound == 2) {
                    f = 854.0f;
                } else if (iRound == 3) {
                    f = 1280.0f;
                } else if (iRound != 5) {
                    f = iRound != 6 ? 1920.0f : 3840.0f;
                } else {
                    f = 2560.0f;
                }
                int i5 = videoEditedInfo.originalWidth;
                int i6 = videoEditedInfo.originalHeight;
                float f3 = f / (i5 > i6 ? i5 : i6);
                videoEditedInfo.resultWidth = Math.round((i5 * f3) / 2.0f) * 2;
                videoEditedInfo.resultHeight = Math.round((videoEditedInfo.originalHeight * f3) / 2.0f) * 2;
            } else {
                z = false;
            }
            videoBitrate = MediaController.makeVideoBitrate(videoEditedInfo.originalHeight, videoEditedInfo.originalWidth, videoBitrate, videoEditedInfo.resultHeight, videoEditedInfo.resultWidth);
        } else {
            z = false;
        }
        if (!z) {
            videoEditedInfo.resultWidth = videoEditedInfo.originalWidth;
            videoEditedInfo.resultHeight = videoEditedInfo.originalHeight;
            videoEditedInfo.bitrate = videoBitrate;
            videoEditedInfo.estimatedSize = length;
        } else {
            videoEditedInfo.bitrate = videoBitrate;
            videoEditedInfo.estimatedSize = (long) (j + (((f2 / 1000.0f) * MediaController.extractRealEncoderBitrate(videoEditedInfo.resultWidth, videoEditedInfo.resultHeight, videoBitrate, false)) / 8.0f));
        }
        if (videoEditedInfo.estimatedSize == 0) {
            videoEditedInfo.estimatedSize = 1L;
        }
        return videoEditedInfo;
    }

    public static void prepareSendingVideo(AccountInstance accountInstance, String str, VideoEditedInfo videoEditedInfo, String str2, TLRPC.Photo photo, long j, MessageObject messageObject, MessageObject messageObject2, TL_stories.StoryItem storyItem, ChatActivity.ReplyQuote replyQuote, ArrayList<TLRPC.MessageEntity> arrayList, int i, MessageObject messageObject3, boolean z, int i2, int i3, boolean z2, boolean z3, CharSequence charSequence, String str3, int i4, long j2, long j3) {
        prepareSendingVideo(accountInstance, str, videoEditedInfo, str2, photo, j, messageObject, messageObject2, storyItem, replyQuote, arrayList, i, messageObject3, z, i2, i3, z2, z3, charSequence, str3, i4, j2, j3, 0L, null);
    }

    public static void prepareSendingVideo(AccountInstance accountInstance, String str, VideoEditedInfo videoEditedInfo, String str2, TLRPC.Photo photo, long j, MessageObject messageObject, MessageObject messageObject2, TL_stories.StoryItem storyItem, ChatActivity.ReplyQuote replyQuote, ArrayList<TLRPC.MessageEntity> arrayList, int i, MessageObject messageObject3, boolean z, int i2, int i3, boolean z2, boolean z3, CharSequence charSequence, String str3, int i4, long j2, long j3, long j4, MessageSuggestionParams messageSuggestionParams) {
        prepareSendingVideo(accountInstance, str, videoEditedInfo, str2, photo, j, messageObject, messageObject2, storyItem, replyQuote, arrayList, i, messageObject3, z, i2, i3, z2, z3, charSequence, str3, i4, j2, j3, j4, messageSuggestionParams, false);
    }

    public static void prepareSendingVideo(final AccountInstance accountInstance, final String str, final VideoEditedInfo videoEditedInfo, final String str2, final TLRPC.Photo photo, final long j, final MessageObject messageObject, final MessageObject messageObject2, final TL_stories.StoryItem storyItem, final ChatActivity.ReplyQuote replyQuote, final ArrayList<TLRPC.MessageEntity> arrayList, final int i, final MessageObject messageObject3, final boolean z, final int i2, final int i3, final boolean z2, final boolean z3, final CharSequence charSequence, final String str3, final int i4, final long j2, final long j3, final long j4, final MessageSuggestionParams messageSuggestionParams, final boolean z4) {
        if (str == null || str.length() == 0) {
            return;
        }
        new Thread(new Runnable() { // from class: org.telegram.messenger.SendMessagesHelper$$ExternalSyntheticLambda13
            @Override // java.lang.Runnable
            public final void run() throws Throwable {
                SendMessagesHelper.$r8$lambda$epsLGXhWLg8CXd0lXWI5ho0LvIo(videoEditedInfo, str, j, i, accountInstance, str2, photo, charSequence, messageObject3, z3, messageObject, messageObject2, arrayList, z, i2, i3, storyItem, replyQuote, i4, str3, j2, j3, j4, messageSuggestionParams, z4, z2);
            }
        }).start();
    }

    /* JADX WARN: Code duplicated, block: B:101:0x0296  */
    /* JADX WARN: Code duplicated, block: B:107:0x02c6  */
    /* JADX WARN: Code duplicated, block: B:109:0x02cd  */
    /* JADX WARN: Code duplicated, block: B:127:0x031e  */
    /* JADX WARN: Code duplicated, block: B:129:0x0324  */
    /* JADX WARN: Code duplicated, block: B:164:0x045a  */
    /* JADX WARN: Code duplicated, block: B:49:0x00fc  */
    /* JADX WARN: Code duplicated, block: B:93:0x025a  */
    /* JADX WARN: Code duplicated, block: B:96:0x027b  */
    /* JADX WARN: Code duplicated, block: B:98:0x028d A[RETURN] */
    /* JADX WARN: Code duplicated, block: B:99:0x028e  */
    public static /* synthetic */ void $r8$lambda$epsLGXhWLg8CXd0lXWI5ho0LvIo(VideoEditedInfo videoEditedInfo, String str, final long j, final int i, final AccountInstance accountInstance, String str2, TLRPC.Photo photo, CharSequence charSequence, final MessageObject messageObject, final boolean z, final MessageObject messageObject2, final MessageObject messageObject3, final ArrayList arrayList, final boolean z2, final int i2, final int i3, final TL_stories.StoryItem storyItem, final ChatActivity.ReplyQuote replyQuote, final int i4, final String str3, final long j2, final long j3, final long j4, final MessageSuggestionParams messageSuggestionParams, final boolean z3, boolean z4) throws Throwable {
        long j5;
        TLRPC.TL_document tL_document;
        final String str4;
        TLRPC.TL_document tL_document2;
        final Bitmap bitmap;
        final String str5;
        Bitmap bitmapCreateScaledBitmap;
        String str6;
        String absolutePath;
        String str7;
        TLRPC.TL_documentAttributeVideo tL_documentAttributeVideo;
        int i5;
        int i6;
        Bitmap bitmapCreateScaledBitmap2;
        String str8;
        long j6;
        VideoEditedInfo videoEditedInfoCreateCompressionSettings = videoEditedInfo != null ? videoEditedInfo : createCompressionSettings(str);
        boolean zIsEncryptedDialog = DialogObject.isEncryptedDialog(j);
        boolean z5 = videoEditedInfoCreateCompressionSettings != null && videoEditedInfoCreateCompressionSettings.roundVideo;
        if (videoEditedInfoCreateCompressionSettings != null || str.endsWith("mp4") || z5) {
            File file = new File(str);
            String string = str + file.length() + "_" + file.lastModified();
            String string2 = _UrlKt.FRAGMENT_ENCODE_SET;
            if (videoEditedInfoCreateCompressionSettings != null) {
                if (z5) {
                    j6 = 0;
                } else {
                    StringBuilder sb = new StringBuilder();
                    sb.append(string);
                    j6 = 0;
                    sb.append(videoEditedInfoCreateCompressionSettings.estimatedDuration);
                    sb.append("_");
                    sb.append(videoEditedInfoCreateCompressionSettings.startTime);
                    sb.append("_");
                    sb.append(videoEditedInfoCreateCompressionSettings.endTime);
                    sb.append(videoEditedInfoCreateCompressionSettings.muted ? "_m" : _UrlKt.FRAGMENT_ENCODE_SET);
                    string = sb.toString();
                    if (videoEditedInfoCreateCompressionSettings.resultWidth != videoEditedInfoCreateCompressionSettings.originalWidth) {
                        string = string + "_" + videoEditedInfoCreateCompressionSettings.resultWidth;
                    }
                }
                long j7 = videoEditedInfoCreateCompressionSettings.startTime;
                if (j7 < j6) {
                    j7 = j6;
                }
                j5 = j7;
            } else {
                j5 = 0;
            }
            String str9 = string;
            if (!zIsEncryptedDialog && i == 0 && (videoEditedInfoCreateCompressionSettings == null || (videoEditedInfoCreateCompressionSettings.filterState == null && videoEditedInfoCreateCompressionSettings.paintPath == null && videoEditedInfoCreateCompressionSettings.mediaEntities == null && videoEditedInfoCreateCompressionSettings.cropState == null))) {
                Object[] sentFile = accountInstance.getMessagesStorage().getSentFile(str9, !zIsEncryptedDialog ? 2 : 5);
                if (sentFile != null) {
                    Object obj = sentFile[0];
                    if (obj instanceof TLRPC.TL_document) {
                        tL_document = (TLRPC.TL_document) obj;
                        String str10 = (String) sentFile[1];
                        ensureMediaThumbExists(accountInstance, zIsEncryptedDialog, tL_document, str, null, j5);
                        str4 = str10;
                    } else {
                        tL_document = null;
                        str4 = null;
                    }
                } else {
                    tL_document = null;
                    str4 = null;
                }
            } else {
                tL_document = null;
                str4 = null;
            }
            if (tL_document == null) {
                Bitmap bitmapCreateVideoThumbnail = (videoEditedInfoCreateCompressionSettings == null || !videoEditedInfoCreateCompressionSettings.notReadyYet) ? null : videoEditedInfoCreateCompressionSettings.thumb;
                if (bitmapCreateVideoThumbnail == null) {
                    bitmapCreateVideoThumbnail = createVideoThumbnailAtTime(str, j5);
                }
                if (bitmapCreateVideoThumbnail == null) {
                    bitmapCreateVideoThumbnail = createVideoThumbnail(str, 1);
                }
                int i7 = (zIsEncryptedDialog || i != 0) ? 90 : 320;
                float f = i7;
                TLRPC.PhotoSize photoSizeScaleAndSaveImage = ImageLoader.scaleAndSaveImage(bitmapCreateVideoThumbnail, f, f, i7 > 90 ? 80 : 55, zIsEncryptedDialog);
                if (bitmapCreateVideoThumbnail == null || photoSizeScaleAndSaveImage == null) {
                    bitmap = bitmapCreateVideoThumbnail;
                } else {
                    if (z5) {
                        if (zIsEncryptedDialog) {
                            bitmapCreateScaledBitmap2 = Bitmap.createScaledBitmap(bitmapCreateVideoThumbnail, 90, 90, true);
                            Utilities.blurBitmap(bitmapCreateScaledBitmap2, 7, 1, bitmapCreateScaledBitmap2.getWidth(), bitmapCreateScaledBitmap2.getHeight(), bitmapCreateScaledBitmap2.getRowBytes());
                            Utilities.blurBitmap(bitmapCreateScaledBitmap2, 7, 1, bitmapCreateScaledBitmap2.getWidth(), bitmapCreateScaledBitmap2.getHeight(), bitmapCreateScaledBitmap2.getRowBytes());
                            Utilities.blurBitmap(bitmapCreateScaledBitmap2, 7, 1, bitmapCreateScaledBitmap2.getWidth(), bitmapCreateScaledBitmap2.getHeight(), bitmapCreateScaledBitmap2.getRowBytes());
                            str8 = String.format(photoSizeScaleAndSaveImage.location.volume_id + "_" + photoSizeScaleAndSaveImage.location.local_id + "@%d_%d_b2", Integer.valueOf((int) (AndroidUtilities.roundMessageSize / AndroidUtilities.density)), Integer.valueOf((int) (AndroidUtilities.roundMessageSize / AndroidUtilities.density)));
                        } else {
                            bitmapCreateScaledBitmap2 = bitmapCreateVideoThumbnail;
                            Utilities.blurBitmap(bitmapCreateScaledBitmap2, 3, 1, bitmapCreateVideoThumbnail.getWidth(), bitmapCreateVideoThumbnail.getHeight(), bitmapCreateVideoThumbnail.getRowBytes());
                            str8 = String.format(photoSizeScaleAndSaveImage.location.volume_id + "_" + photoSizeScaleAndSaveImage.location.local_id + "@%d_%d_b", Integer.valueOf((int) (AndroidUtilities.roundMessageSize / AndroidUtilities.density)), Integer.valueOf((int) (AndroidUtilities.roundMessageSize / AndroidUtilities.density)));
                        }
                        str7 = str8;
                        bitmap = bitmapCreateScaledBitmap2;
                    } else {
                        bitmap = null;
                    }
                    tL_document2 = new TLRPC.TL_document();
                    if (photoSizeScaleAndSaveImage != null) {
                        tL_document2.thumbs.add(photoSizeScaleAndSaveImage);
                        tL_document2.flags |= 1;
                    }
                    tL_document2.file_reference = new byte[0];
                    tL_document2.mime_type = "video/mp4";
                    accountInstance.getUserConfig().saveConfig(false);
                    if (zIsEncryptedDialog) {
                        if (accountInstance.getMessagesController().getEncryptedChat(Integer.valueOf(DialogObject.getEncryptedChatId(j))) == null) {
                            return;
                        } else {
                            tL_documentAttributeVideo = new TLRPC.TL_documentAttributeVideo_layer159();
                        }
                    } else {
                        tL_documentAttributeVideo = new TLRPC.TL_documentAttributeVideo();
                        tL_documentAttributeVideo.supports_streaming = true;
                    }
                    tL_documentAttributeVideo.round_message = z5;
                    tL_document2.attributes.add(tL_documentAttributeVideo);
                    if (videoEditedInfoCreateCompressionSettings == null && videoEditedInfoCreateCompressionSettings.notReadyYet) {
                        tL_documentAttributeVideo.w = videoEditedInfoCreateCompressionSettings.resultWidth;
                        tL_documentAttributeVideo.h = videoEditedInfoCreateCompressionSettings.resultHeight;
                        tL_documentAttributeVideo.duration = videoEditedInfoCreateCompressionSettings.estimatedDuration / 1000.0d;
                        tL_document2.size = videoEditedInfoCreateCompressionSettings.estimatedSize;
                    } else if (videoEditedInfoCreateCompressionSettings == null && videoEditedInfoCreateCompressionSettings.needConvert()) {
                        if (videoEditedInfoCreateCompressionSettings.muted) {
                            tL_document2.attributes.add(new TLRPC.TL_documentAttributeAnimated());
                            fillVideoAttribute(str, tL_documentAttributeVideo, videoEditedInfoCreateCompressionSettings);
                            videoEditedInfoCreateCompressionSettings.originalWidth = tL_documentAttributeVideo.w;
                            videoEditedInfoCreateCompressionSettings.originalHeight = tL_documentAttributeVideo.h;
                        } else {
                            tL_documentAttributeVideo.duration = videoEditedInfoCreateCompressionSettings.estimatedDuration / 1000.0d;
                        }
                        int i8 = videoEditedInfoCreateCompressionSettings.rotationValue;
                        MediaController.CropState cropState = videoEditedInfoCreateCompressionSettings.cropState;
                        if (cropState != null) {
                            i5 = cropState.transformWidth;
                            i6 = cropState.transformHeight;
                            i8 += cropState.transformRotation;
                        } else {
                            i5 = videoEditedInfoCreateCompressionSettings.resultWidth;
                            i6 = videoEditedInfoCreateCompressionSettings.resultHeight;
                        }
                        if (i8 == 90 || i8 == 270) {
                            tL_documentAttributeVideo.w = i6;
                            tL_documentAttributeVideo.h = i5;
                        } else {
                            tL_documentAttributeVideo.w = i5;
                            tL_documentAttributeVideo.h = i6;
                        }
                        tL_document2.size = videoEditedInfoCreateCompressionSettings.estimatedSize;
                    } else {
                        if (file.exists()) {
                            tL_document2.size = (int) file.length();
                        }
                        fillVideoAttribute(str, tL_documentAttributeVideo, null);
                    }
                    str5 = str7;
                }
                str7 = null;
                tL_document2 = new TLRPC.TL_document();
                if (photoSizeScaleAndSaveImage != null) {
                    tL_document2.thumbs.add(photoSizeScaleAndSaveImage);
                    tL_document2.flags |= 1;
                }
                tL_document2.file_reference = new byte[0];
                tL_document2.mime_type = "video/mp4";
                accountInstance.getUserConfig().saveConfig(false);
                if (zIsEncryptedDialog) {
                    if (accountInstance.getMessagesController().getEncryptedChat(Integer.valueOf(DialogObject.getEncryptedChatId(j))) == null) {
                        return;
                    } else {
                        tL_documentAttributeVideo = new TLRPC.TL_documentAttributeVideo_layer159();
                    }
                } else {
                    tL_documentAttributeVideo = new TLRPC.TL_documentAttributeVideo();
                    tL_documentAttributeVideo.supports_streaming = true;
                }
                tL_documentAttributeVideo.round_message = z5;
                tL_document2.attributes.add(tL_documentAttributeVideo);
                if (videoEditedInfoCreateCompressionSettings == null) {
                    if (videoEditedInfoCreateCompressionSettings == null) {
                        if (file.exists()) {
                            tL_document2.size = (int) file.length();
                        }
                        fillVideoAttribute(str, tL_documentAttributeVideo, null);
                    } else {
                        if (file.exists()) {
                            tL_document2.size = (int) file.length();
                        }
                        fillVideoAttribute(str, tL_documentAttributeVideo, null);
                    }
                } else if (videoEditedInfoCreateCompressionSettings == null) {
                    if (file.exists()) {
                        tL_document2.size = (int) file.length();
                    }
                    fillVideoAttribute(str, tL_documentAttributeVideo, null);
                } else {
                    if (file.exists()) {
                        tL_document2.size = (int) file.length();
                    }
                    fillVideoAttribute(str, tL_documentAttributeVideo, null);
                }
                str5 = str7;
            } else {
                boolean z6 = z5;
                if (tL_document.thumbs.isEmpty()) {
                    Bitmap bitmapCreateVideoThumbnail2 = (videoEditedInfoCreateCompressionSettings == null || !videoEditedInfoCreateCompressionSettings.notReadyYet) ? null : videoEditedInfoCreateCompressionSettings.thumb;
                    if (bitmapCreateVideoThumbnail2 == null) {
                        bitmapCreateVideoThumbnail2 = createVideoThumbnailAtTime(str, j5);
                    }
                    if (bitmapCreateVideoThumbnail2 == null) {
                        bitmapCreateVideoThumbnail2 = createVideoThumbnail(str, 1);
                    }
                    int i9 = (zIsEncryptedDialog || i != 0) ? 90 : 320;
                    float f2 = i9;
                    TLRPC.PhotoSize photoSizeScaleAndSaveImage2 = ImageLoader.scaleAndSaveImage(bitmapCreateVideoThumbnail2, f2, f2, i9 > 90 ? 80 : 55, zIsEncryptedDialog);
                    if (bitmapCreateVideoThumbnail2 == null || photoSizeScaleAndSaveImage2 == null) {
                        bitmapCreateScaledBitmap = bitmapCreateVideoThumbnail2;
                    } else {
                        if (!z6) {
                            bitmapCreateScaledBitmap = null;
                        } else if (zIsEncryptedDialog) {
                            bitmapCreateScaledBitmap = Bitmap.createScaledBitmap(bitmapCreateVideoThumbnail2, 90, 90, true);
                            Utilities.blurBitmap(bitmapCreateScaledBitmap, 7, 1, bitmapCreateScaledBitmap.getWidth(), bitmapCreateScaledBitmap.getHeight(), bitmapCreateScaledBitmap.getRowBytes());
                            Utilities.blurBitmap(bitmapCreateScaledBitmap, 7, 1, bitmapCreateScaledBitmap.getWidth(), bitmapCreateScaledBitmap.getHeight(), bitmapCreateScaledBitmap.getRowBytes());
                            Utilities.blurBitmap(bitmapCreateScaledBitmap, 7, 1, bitmapCreateScaledBitmap.getWidth(), bitmapCreateScaledBitmap.getHeight(), bitmapCreateScaledBitmap.getRowBytes());
                            str6 = String.format(photoSizeScaleAndSaveImage2.location.volume_id + "_" + photoSizeScaleAndSaveImage2.location.local_id + "@%d_%d_b2", Integer.valueOf((int) (AndroidUtilities.roundMessageSize / AndroidUtilities.density)), Integer.valueOf((int) (AndroidUtilities.roundMessageSize / AndroidUtilities.density)));
                        } else {
                            bitmapCreateScaledBitmap = bitmapCreateVideoThumbnail2;
                            Utilities.blurBitmap(bitmapCreateScaledBitmap, 3, 1, bitmapCreateVideoThumbnail2.getWidth(), bitmapCreateVideoThumbnail2.getHeight(), bitmapCreateVideoThumbnail2.getRowBytes());
                            str6 = String.format(photoSizeScaleAndSaveImage2.location.volume_id + "_" + photoSizeScaleAndSaveImage2.location.local_id + "@%d_%d_b", Integer.valueOf((int) (AndroidUtilities.roundMessageSize / AndroidUtilities.density)), Integer.valueOf((int) (AndroidUtilities.roundMessageSize / AndroidUtilities.density)));
                        }
                        if (photoSizeScaleAndSaveImage2 != null) {
                            tL_document.thumbs.add(photoSizeScaleAndSaveImage2);
                            tL_document.flags |= 1;
                        }
                        tL_document2 = tL_document;
                        bitmap = bitmapCreateScaledBitmap;
                        str5 = str6;
                    }
                    str6 = null;
                    if (photoSizeScaleAndSaveImage2 != null) {
                        tL_document.thumbs.add(photoSizeScaleAndSaveImage2);
                        tL_document.flags |= 1;
                    }
                    tL_document2 = tL_document;
                    bitmap = bitmapCreateScaledBitmap;
                    str5 = str6;
                } else {
                    tL_document2 = tL_document;
                    bitmap = null;
                    str5 = null;
                }
            }
            TLRPC.PhotoSize photoSizeFileToSize = ImageLoader.fileToSize(str2, false);
            if (photoSizeFileToSize == null && photo != null) {
                photoSizeFileToSize = new ImageLoader.PhotoSizeFromPhoto(photo);
            }
            final TLRPC.PhotoSize photoSize = photoSizeFileToSize;
            if (videoEditedInfoCreateCompressionSettings == null || !videoEditedInfoCreateCompressionSettings.needConvert()) {
                absolutePath = str;
            } else {
                File file2 = new File(FileLoader.getDirectory(4), "-2147483648_" + SharedConfig.getLastLocalId() + ".mp4");
                SharedConfig.saveConfig();
                absolutePath = file2.getAbsolutePath();
            }
            final HashMap map = new HashMap();
            if (charSequence != null) {
                string2 = charSequence.toString();
            }
            final String str11 = string2;
            if (str9 != null) {
                map.put("originalPath", str9);
            }
            if (str4 != null) {
                map.put("parentObject", str4);
            }
            final TLRPC.TL_document tL_document3 = tL_document2;
            final VideoEditedInfo videoEditedInfo2 = videoEditedInfoCreateCompressionSettings;
            final String str12 = absolutePath;
            AndroidUtilities.runOnUIThread(new Runnable() { // from class: org.telegram.messenger.SendMessagesHelper$$ExternalSyntheticLambda24
                @Override // java.lang.Runnable
                public final void run() {
                    SendMessagesHelper.m3709$r8$lambda$E9EttGeiF1ssPDxXtv0kTmca04(bitmap, str5, messageObject, accountInstance, videoEditedInfo2, tL_document3, str12, photoSize, map, z, str4, j, messageObject2, messageObject3, str11, arrayList, z2, i2, i3, i, storyItem, replyQuote, i4, str3, j2, j3, j4, messageSuggestionParams, z3);
                }
            });
            return;
        }
        prepareSendingDocumentInternal(accountInstance, str, str, null, null, j, messageObject2, messageObject3, storyItem, replyQuote, arrayList, messageObject, null, false, charSequence, z2, i2, i3, null, z4, str3, i4, 0L, z3, j3, j4, messageSuggestionParams);
    }

    /* JADX INFO: renamed from: $r8$lambda$E9EttGeiF-1ssPDxXtv0kTmca04, reason: not valid java name */
    public static /* synthetic */ void m3709$r8$lambda$E9EttGeiF1ssPDxXtv0kTmca04(Bitmap bitmap, String str, MessageObject messageObject, AccountInstance accountInstance, VideoEditedInfo videoEditedInfo, TLRPC.TL_document tL_document, String str2, TLRPC.PhotoSize photoSize, HashMap map, boolean z, String str3, long j, MessageObject messageObject2, MessageObject messageObject3, String str4, ArrayList arrayList, boolean z2, int i, int i2, int i3, TL_stories.StoryItem storyItem, ChatActivity.ReplyQuote replyQuote, int i4, String str5, long j2, long j3, long j4, MessageSuggestionParams messageSuggestionParams, boolean z3) {
        if (bitmap != null && str != null) {
            ImageLoader.getInstance().putImageToCache(new BitmapDrawable(bitmap), str, false);
        }
        if (messageObject != null) {
            accountInstance.getSendMessagesHelper().editMessage(messageObject, null, videoEditedInfo, tL_document, str2, photoSize, map, false, z, str3);
            return;
        }
        SendMessageParams sendMessageParamsOf = SendMessageParams.of(tL_document, videoEditedInfo, str2, j, messageObject2, messageObject3, str4, arrayList, null, map, z2, i, i2, i3, str3, null, false, z);
        sendMessageParamsOf.replyToStoryItem = storyItem;
        sendMessageParamsOf.replyQuote = replyQuote;
        sendMessageParamsOf.quick_reply_shortcut_id = i4;
        sendMessageParamsOf.quick_reply_shortcut = str5;
        sendMessageParamsOf.effect_id = j2;
        sendMessageParamsOf.cover = photoSize;
        sendMessageParamsOf.payStars = j3;
        sendMessageParamsOf.monoForumPeer = j4;
        sendMessageParamsOf.suggestionParams = messageSuggestionParams;
        sendMessageParamsOf.invert_media = z3;
        accountInstance.getSendMessagesHelper().sendMessage(sendMessageParamsOf);
    }

    public static class SendMessageParams {
        public String caption;
        public TLRPC.PhotoSize cover;
        public long dice_stake;
        public TLRPC.TL_document document;
        public long effect_id;
        public ArrayList<TLRPC.MessageEntity> entities;
        public TLRPC.TL_game game;
        public boolean hasMediaSpoilers;
        public boolean invert_media;
        public TLRPC.TL_messageMediaInvoice invoice;
        public TLRPC.MessageMedia location;
        public TLRPC.TL_messageMediaWebPage mediaWebPage;
        public String message;
        public long monoForumPeer;
        public boolean notify;
        public HashMap<String, String> params;
        public Object parentObject;
        public String path;
        public long payStars;
        public long peer;
        public TLRPC.TL_photo photo;
        public TLRPC.TL_messageMediaPoll poll;
        public String quick_reply_shortcut;
        public int quick_reply_shortcut_id;
        public TLRPC.ReplyMarkup replyMarkup;
        public ChatActivity.ReplyQuote replyQuote;
        public MessageObject replyToMsg;
        public TL_stories.StoryItem replyToStoryItem;
        public MessageObject replyToTopMsg;
        public MessageObject retryMessageObject;
        public int scheduleDate;
        public int scheduleRepeatPeriod;
        public boolean searchLinks = true;
        public MessageObject.SendAnimationData sendAnimationData;
        public boolean sendingHighQuality;
        public TL_stories.StoryItem sendingStory;
        public long stars;
        public MessageSuggestionParams suggestionParams;
        public TLRPC.TL_messageMediaToDo todo;
        public int ttl;
        public boolean updateStickersOrder;
        public TLRPC.User user;
        public VideoEditedInfo videoEditedInfo;
        public TLRPC.WebPage webPage;

        public static SendMessageParams of(String str, long j) {
            return of(str, null, null, null, null, null, null, null, null, null, j, null, null, null, null, true, null, null, null, null, false, 0, 0, 0, null, null, false);
        }

        public static SendMessageParams of(MessageObject messageObject) {
            long dialogId = messageObject.getDialogId();
            TLRPC.Message message = messageObject.messageOwner;
            SendMessageParams sendMessageParamsOf = of(null, null, null, null, null, null, null, null, null, null, dialogId, message.attachPath, null, null, null, true, messageObject, null, message.reply_markup, message.params, !message.silent, messageObject.scheduled ? message.date : 0, 0, 0, null, null, false);
            TLRPC.Message message2 = messageObject.messageOwner;
            if (message2 != null) {
                TLRPC.InputQuickReplyShortcut inputQuickReplyShortcut = message2.quick_reply_shortcut;
                if (inputQuickReplyShortcut instanceof TLRPC.TL_inputQuickReplyShortcut) {
                    sendMessageParamsOf.quick_reply_shortcut = ((TLRPC.TL_inputQuickReplyShortcut) inputQuickReplyShortcut).shortcut;
                }
                sendMessageParamsOf.quick_reply_shortcut_id = messageObject.getQuickReplyId();
                sendMessageParamsOf.payStars = messageObject.messageOwner.paid_message_stars;
            }
            return sendMessageParamsOf;
        }

        public static SendMessageParams of(TLRPC.User user, long j, MessageObject messageObject, MessageObject messageObject2, TLRPC.ReplyMarkup replyMarkup, HashMap<String, String> map, boolean z, int i, int i2) {
            return of(null, null, null, null, null, user, null, null, null, null, j, null, messageObject, messageObject2, null, true, null, null, replyMarkup, map, z, i, i2, 0, null, null, false);
        }

        public static SendMessageParams of(TLRPC.TL_messageMediaInvoice tL_messageMediaInvoice, long j, MessageObject messageObject, MessageObject messageObject2, TLRPC.ReplyMarkup replyMarkup, HashMap<String, String> map, boolean z, int i, int i2) {
            return of(null, null, null, null, null, null, null, null, null, tL_messageMediaInvoice, j, null, messageObject, messageObject2, null, true, null, null, replyMarkup, map, z, i, i2, 0, null, null, false);
        }

        public static SendMessageParams of(TLRPC.TL_document tL_document, VideoEditedInfo videoEditedInfo, String str, long j, MessageObject messageObject, MessageObject messageObject2, String str2, ArrayList<TLRPC.MessageEntity> arrayList, TLRPC.ReplyMarkup replyMarkup, HashMap<String, String> map, boolean z, int i, int i2, int i3, Object obj, MessageObject.SendAnimationData sendAnimationData, boolean z2) {
            return of(null, str2, null, null, videoEditedInfo, null, tL_document, null, null, null, j, str, messageObject, messageObject2, null, true, null, arrayList, replyMarkup, map, z, i, i2, i3, obj, sendAnimationData, z2);
        }

        public static SendMessageParams of(TLRPC.TL_document tL_document, VideoEditedInfo videoEditedInfo, String str, long j, MessageObject messageObject, MessageObject messageObject2, String str2, ArrayList<TLRPC.MessageEntity> arrayList, TLRPC.ReplyMarkup replyMarkup, HashMap<String, String> map, boolean z, int i, int i2, int i3, Object obj, MessageObject.SendAnimationData sendAnimationData, boolean z2, boolean z3) {
            return of(null, str2, null, null, videoEditedInfo, null, tL_document, null, null, null, j, str, messageObject, messageObject2, null, true, null, arrayList, replyMarkup, map, z, i, i2, i3, obj, sendAnimationData, z2, z3);
        }

        public static SendMessageParams of(String str, long j, MessageObject messageObject, MessageObject messageObject2, TLRPC.WebPage webPage, boolean z, ArrayList<TLRPC.MessageEntity> arrayList, TLRPC.ReplyMarkup replyMarkup, HashMap<String, String> map, boolean z2, int i, int i2, MessageObject.SendAnimationData sendAnimationData, boolean z3) {
            return of(str, null, null, null, null, null, null, null, null, null, j, null, messageObject, messageObject2, webPage, z, null, arrayList, replyMarkup, map, z2, i, i2, 0, null, sendAnimationData, z3);
        }

        public static SendMessageParams of(TLRPC.MessageMedia messageMedia, long j, MessageObject messageObject, MessageObject messageObject2, TLRPC.ReplyMarkup replyMarkup, HashMap<String, String> map, boolean z, int i, int i2) {
            return of(null, null, messageMedia, null, null, null, null, null, null, null, j, null, messageObject, messageObject2, null, true, null, null, replyMarkup, map, z, i, i2, 0, null, null, false);
        }

        public static SendMessageParams of(TLRPC.TL_messageMediaPoll tL_messageMediaPoll, long j, MessageObject messageObject, MessageObject messageObject2, TLRPC.ReplyMarkup replyMarkup, HashMap<String, String> map, boolean z, int i, int i2) {
            return of(null, null, null, null, null, null, null, null, tL_messageMediaPoll, null, j, null, messageObject, messageObject2, null, true, null, null, replyMarkup, map, z, i, i2, 0, null, null, false);
        }

        public static SendMessageParams of(TLRPC.TL_game tL_game, long j, MessageObject messageObject, MessageObject messageObject2, TLRPC.ReplyMarkup replyMarkup, HashMap<String, String> map, boolean z, int i, int i2) {
            return of(null, null, null, null, null, null, null, tL_game, null, null, j, null, messageObject, messageObject2, null, true, null, null, replyMarkup, map, z, i, i2, 0, null, null, false);
        }

        public static SendMessageParams of(TLRPC.TL_photo tL_photo, String str, long j, MessageObject messageObject, MessageObject messageObject2, String str2, ArrayList<TLRPC.MessageEntity> arrayList, TLRPC.ReplyMarkup replyMarkup, HashMap<String, String> map, boolean z, int i, int i2, int i3, Object obj, boolean z2, boolean z3) {
            return of(null, str2, null, tL_photo, null, null, null, null, null, null, j, str, messageObject, messageObject2, null, true, null, arrayList, replyMarkup, map, z, i, i2, i3, obj, null, z2, z3);
        }

        public static SendMessageParams of(TLRPC.TL_photo tL_photo, String str, long j, MessageObject messageObject, MessageObject messageObject2, String str2, ArrayList<TLRPC.MessageEntity> arrayList, TLRPC.ReplyMarkup replyMarkup, HashMap<String, String> map, boolean z, int i, int i2, int i3, Object obj, boolean z2) {
            return of(null, str2, null, tL_photo, null, null, null, null, null, null, j, str, messageObject, messageObject2, null, true, null, arrayList, replyMarkup, map, z, i, i2, i3, obj, null, z2);
        }

        private static SendMessageParams of(String str, String str2, TLRPC.MessageMedia messageMedia, TLRPC.TL_photo tL_photo, VideoEditedInfo videoEditedInfo, TLRPC.User user, TLRPC.TL_document tL_document, TLRPC.TL_game tL_game, TLRPC.TL_messageMediaPoll tL_messageMediaPoll, TLRPC.TL_messageMediaInvoice tL_messageMediaInvoice, long j, String str3, MessageObject messageObject, MessageObject messageObject2, TLRPC.WebPage webPage, boolean z, MessageObject messageObject3, ArrayList<TLRPC.MessageEntity> arrayList, TLRPC.ReplyMarkup replyMarkup, HashMap<String, String> map, boolean z2, int i, int i2, int i3, Object obj, MessageObject.SendAnimationData sendAnimationData, boolean z3) {
            return of(str, str2, messageMedia, tL_photo, videoEditedInfo, user, tL_document, tL_game, tL_messageMediaPoll, tL_messageMediaInvoice, j, str3, messageObject, messageObject2, webPage, z, messageObject3, arrayList, replyMarkup, map, z2, i, i2, i3, obj, sendAnimationData, z3, false);
        }

        public static SendMessageParams of(String str, String str2, TLRPC.MessageMedia messageMedia, TLRPC.TL_photo tL_photo, VideoEditedInfo videoEditedInfo, TLRPC.User user, TLRPC.TL_document tL_document, TLRPC.TL_game tL_game, TLRPC.TL_messageMediaPoll tL_messageMediaPoll, TLRPC.TL_messageMediaInvoice tL_messageMediaInvoice, long j, String str3, MessageObject messageObject, MessageObject messageObject2, TLRPC.WebPage webPage, boolean z, MessageObject messageObject3, ArrayList<TLRPC.MessageEntity> arrayList, TLRPC.ReplyMarkup replyMarkup, HashMap<String, String> map, boolean z2, int i, int i2, int i3, Object obj, MessageObject.SendAnimationData sendAnimationData, boolean z3, boolean z4) {
            SendMessageParams sendMessageParams = new SendMessageParams();
            sendMessageParams.message = str;
            sendMessageParams.caption = str2;
            sendMessageParams.location = messageMedia;
            sendMessageParams.photo = tL_photo;
            sendMessageParams.videoEditedInfo = videoEditedInfo;
            sendMessageParams.user = user;
            sendMessageParams.document = tL_document;
            sendMessageParams.game = tL_game;
            sendMessageParams.poll = tL_messageMediaPoll;
            sendMessageParams.invoice = tL_messageMediaInvoice;
            sendMessageParams.peer = j;
            sendMessageParams.path = str3;
            sendMessageParams.replyToMsg = messageObject;
            sendMessageParams.replyToTopMsg = messageObject2;
            sendMessageParams.webPage = webPage;
            sendMessageParams.searchLinks = z;
            sendMessageParams.retryMessageObject = messageObject3;
            sendMessageParams.entities = arrayList;
            sendMessageParams.replyMarkup = replyMarkup;
            sendMessageParams.params = map;
            sendMessageParams.notify = z2;
            sendMessageParams.scheduleDate = i;
            sendMessageParams.scheduleRepeatPeriod = i2;
            sendMessageParams.ttl = i3;
            sendMessageParams.parentObject = obj;
            sendMessageParams.sendAnimationData = sendAnimationData;
            sendMessageParams.updateStickersOrder = z3;
            sendMessageParams.hasMediaSpoilers = z4;
            if (AyuGhostController.getInstance(UserConfig.selectedAccount).isSendWithoutSound()) {
                sendMessageParams.notify = !sendMessageParams.notify;
            }
            return sendMessageParams;
        }
    }

    public TLRPC.Message getMessageFromUpdate(TLRPC.Update update) {
        if (update instanceof TLRPC.TL_updateNewMessage) {
            return ((TLRPC.TL_updateNewMessage) update).message;
        }
        if (update instanceof TLRPC.TL_updateNewChannelMessage) {
            return ((TLRPC.TL_updateNewChannelMessage) update).message;
        }
        if (update instanceof TLRPC.TL_updateNewScheduledMessage) {
            return ((TLRPC.TL_updateNewScheduledMessage) update).message;
        }
        if (update instanceof TLRPC.TL_updateQuickReplyMessage) {
            return ((TLRPC.TL_updateQuickReplyMessage) update).message;
        }
        return null;
    }

    private void applyMonoForumPeerId(TLRPC.TL_messages_sendInlineBotResult tL_messages_sendInlineBotResult, long j) {
        if (j != 0) {
            TLRPC.InputPeer inputPeer = getMessagesController().getInputPeer(j);
            TLRPC.InputReplyTo inputReplyTo = tL_messages_sendInlineBotResult.reply_to;
            if (inputReplyTo != null) {
                if (inputReplyTo instanceof TLRPC.TL_inputReplyToMessage) {
                    inputReplyTo.monoforum_peer_id = inputPeer;
                    inputReplyTo.flags |= 32;
                    return;
                }
                return;
            }
            TLRPC.TL_inputReplyToMonoForum tL_inputReplyToMonoForum = new TLRPC.TL_inputReplyToMonoForum();
            tL_messages_sendInlineBotResult.reply_to = tL_inputReplyToMonoForum;
            tL_inputReplyToMonoForum.monoforum_peer_id = inputPeer;
            tL_messages_sendInlineBotResult.flags |= 1;
        }
    }

    private void applyMonoForumPeerId(TLRPC.TL_messages_sendMessage tL_messages_sendMessage, long j) {
        if (j != 0) {
            TLRPC.InputPeer inputPeer = getMessagesController().getInputPeer(j);
            TLRPC.InputReplyTo inputReplyTo = tL_messages_sendMessage.reply_to;
            if (inputReplyTo != null) {
                if (inputReplyTo instanceof TLRPC.TL_inputReplyToMessage) {
                    inputReplyTo.monoforum_peer_id = inputPeer;
                    inputReplyTo.flags |= 32;
                    return;
                }
                return;
            }
            TLRPC.TL_inputReplyToMonoForum tL_inputReplyToMonoForum = new TLRPC.TL_inputReplyToMonoForum();
            tL_messages_sendMessage.reply_to = tL_inputReplyToMonoForum;
            tL_inputReplyToMonoForum.monoforum_peer_id = inputPeer;
            tL_messages_sendMessage.flags |= 1;
        }
    }

    private void applyMonoForumPeerId(TLRPC.TL_messages_sendMedia tL_messages_sendMedia, long j) {
        if (j != 0) {
            TLRPC.InputPeer inputPeer = getMessagesController().getInputPeer(j);
            TLRPC.InputReplyTo inputReplyTo = tL_messages_sendMedia.reply_to;
            if (inputReplyTo != null) {
                if (inputReplyTo instanceof TLRPC.TL_inputReplyToMessage) {
                    inputReplyTo.monoforum_peer_id = inputPeer;
                    inputReplyTo.flags |= 32;
                    return;
                }
                return;
            }
            TLRPC.TL_inputReplyToMonoForum tL_inputReplyToMonoForum = new TLRPC.TL_inputReplyToMonoForum();
            tL_messages_sendMedia.reply_to = tL_inputReplyToMonoForum;
            tL_inputReplyToMonoForum.monoforum_peer_id = inputPeer;
            tL_messages_sendMedia.flags |= 1;
        }
    }

    private void applyMonoForumPeerId(TLRPC.TL_messages_sendMultiMedia tL_messages_sendMultiMedia, long j) {
        if (j != 0) {
            TLRPC.InputPeer inputPeer = getMessagesController().getInputPeer(j);
            TLRPC.InputReplyTo inputReplyTo = tL_messages_sendMultiMedia.reply_to;
            if (inputReplyTo != null) {
                if (inputReplyTo instanceof TLRPC.TL_inputReplyToMessage) {
                    inputReplyTo.monoforum_peer_id = inputPeer;
                    inputReplyTo.flags |= 32;
                    return;
                }
                return;
            }
            TLRPC.TL_inputReplyToMonoForum tL_inputReplyToMonoForum = new TLRPC.TL_inputReplyToMonoForum();
            tL_messages_sendMultiMedia.reply_to = tL_inputReplyToMonoForum;
            tL_inputReplyToMonoForum.monoforum_peer_id = inputPeer;
            tL_messages_sendMultiMedia.flags |= 1;
        }
    }

    private void applyMonoForumPeerId(TLRPC.TL_messages_forwardMessages tL_messages_forwardMessages, long j) {
        if (j != 0) {
            TLRPC.InputPeer inputPeer = getMessagesController().getInputPeer(j);
            TLRPC.InputReplyTo inputReplyTo = tL_messages_forwardMessages.reply_to;
            if (inputReplyTo != null) {
                if (inputReplyTo instanceof TLRPC.TL_inputReplyToMessage) {
                    inputReplyTo.monoforum_peer_id = inputPeer;
                    inputReplyTo.flags |= 32;
                    return;
                }
                return;
            }
            TLRPC.TL_inputReplyToMonoForum tL_inputReplyToMonoForum = new TLRPC.TL_inputReplyToMonoForum();
            tL_messages_forwardMessages.reply_to = tL_inputReplyToMonoForum;
            tL_inputReplyToMonoForum.monoforum_peer_id = inputPeer;
        }
    }
}
